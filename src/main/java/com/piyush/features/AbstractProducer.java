package com.piyush.features;

import io.reactivex.Flowable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Service use to converting data from one model to another model.
 * also apply the filtering if need on input models.
 * For Implementation Example:
 *
 * @author Piyush on 7/3/17.
 */
public abstract class AbstractProducer<U, V> implements Producer<U, V> {
  private static final Logger LOGGER = Logger.getLogger(AbstractProducer.class.getName());

  private final List<Consumer<V>> consumers;
  private final int consumerCount;

  /**
   * register all the consumers.
   *
   * @param consumers {@link List} of {@link Consumer}.
   */
  public AbstractProducer(List<Consumer<V>> consumers) {
    this.consumers = consumers;
    this.consumerCount = consumers.size();
  }

  void produceAndConsume() {
    ConnectableFlowable<V> dataToConsume = getData().publish();
    consume(dataToConsume);
  }

  /**
   * consume the data.
   *
   * @param dataToConsume data to be consume.
   */
  private void consume(ConnectableFlowable<V> dataToConsume) {
    CountDownLatch countDownLatch = new CountDownLatch(consumerCount);
    Flowable.fromIterable(consumers)
        .flatMap(Flowable::just)
        .subscribeOn(Schedulers.computation())
        .flatMap(consumer -> consume(dataToConsume, countDownLatch, consumer))
        .subscribe();
    dataToConsume.connect();

    try {
      countDownLatch.await();
    } catch (InterruptedException e) {
      LOGGER.warning("Exception occur {}" + e.getMessage() + e);
    }
  }

  /**
   * Consume the data.
   *
   * @param dataToConsume  data produce why {@link #getFlowableOfInputModel()}
   * @param countDownLatch {@link CountDownLatch} for consumer.
   * @param consumer       {@link List} of all the {@link Consumer}
   * @return {@link Flowable} of type V.
   */
  private Flowable<V> consume(ConnectableFlowable<V> dataToConsume,
                              CountDownLatch countDownLatch,
                              Consumer<V> consumer) {
    return dataToConsume.flatMap(v -> Flowable.just(v)
        .subscribeOn(Schedulers.computation())
        .doOnNext(consumer::accept))
        .doOnTerminate(countDownLatch::countDown);
  }

  /**
   * get source data.
   *
   * @return {@link Flowable} of source data.
   */
  private Flowable<V> getData() {
    return getFlowableOfInputModel()
        .subscribeOn(Schedulers.computation())
        .flatMap(this::getData);
  }

  private Flowable<V> getData(U u) {
    return Flowable.just(u)
        .subscribeOn(Schedulers.computation())
        .filter(this::filterBeforeMap)
        .map(this::changeBeforeConsume)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .filter(this::filterAfterMap)
        .onErrorResumeNext(throwable -> {
          logErrorForRecord(u, throwable);
          return Flowable.empty();
        });
  }

  /**
   * If filtering required on input data. We can write code for that.
   * By default filtering on input data is disable.
   *
   * @param u input data.
   * @return true if filtering is not needed.
   */
  @Override
  public boolean filterBeforeMap(U u) {
    return true;
  }

  /**
   * If filtering required for output data. We can write code for that.
   * By default filtering on input data is disable.
   *
   * @param v out put data data.
   * @return true if filtering is not needed.
   */
  @Override
  public boolean filterAfterMap(V v) {
    return true;
  }
}
