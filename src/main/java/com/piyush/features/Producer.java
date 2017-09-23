package com.piyush.features;

import io.reactivex.Flowable;
import java.util.Optional;

/**
 * @author Piyush on 7/23/17. Interface for all type of producer.
 */

public interface Producer<U, V> {

  Optional<V> changeBeforeConsume(U u);

  /**
   * If filtering required on input data. We can write code for that.
   * By default filtering on input data is disable.
   *
   * @param u input data.
   * @return true if filtering is not needed.
   */
  boolean filterBeforeMap(U u);

  /**
   * If filtering required on out put data. We can write code for that.
   * By default filtering on out put data is disable.
   *
   * @param v input data.
   * @return true if filtering is not needed.
   */
  boolean filterAfterMap(V v);

  /**
   * source of data.
   *
   * @return {@link Flowable} of source data.
   */
  Flowable<U> getFlowableOfInputModel();

  /**
   * log for error during pocessing in observable chain.
   *
   * @param u         U
   * @param throwable {@link Throwable}.
   */
  void logErrorForRecord(U u, Throwable throwable);
}
