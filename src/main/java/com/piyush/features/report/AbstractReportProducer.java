package com.piyush.features.report;

import com.google.common.collect.ImmutableList;
import com.piyush.features.AbstractProducer;
import io.reactivex.Flowable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.springframework.core.env.Environment;

/**
 * @author Piyush on 5/11/17.
 * Producer for all kind of reports.
 */
public abstract class AbstractReportProducer<V> extends AbstractProducer<String, V> {

  private static final Logger LOGGER = Logger.getLogger(AbstractReportProducer.class.getName());
  private static final String PRODUCTION = "production";
  private static final String EXCEPTION_FOR_UPIN_ERROR_MESSAGE =
      "Exception for id {}, Error  Message: {}";
  private final Environment environment;

  private final List<AbstractReportValueSetter<V>> valueSetters;

  /**
   * @param consumers    {@link Consumer} list. Register consumer for report model.
   *                     Ex Write to File.
   * @param valueSetters {@link AbstractReportValueSetter}
   *                     for building the report model suing various dao.
   * @param environment  {@link Environment}
   */
  public AbstractReportProducer(List<Consumer<V>> consumers,
                                List<AbstractReportValueSetter<V>> valueSetters,
                                Environment environment) {
    super(consumers);
    this.valueSetters = valueSetters;
    this.environment = environment;
  }

  /**
   * @return It return the model class for corresponding the file row.
   */
  public abstract Class<V> getModelType();

  @Override
  public Optional<V> changeBeforeConsume(String id) {
    V v = getNewInstanceOfVType(id);
    return valueSetters.stream()
        .anyMatch(valueSetter -> !valueSetter.setValue(v, id))
        ? Optional.empty() : Optional.of(v);
  }

  private V getNewInstanceOfVType(String id) {
    try {
      return getModelType().newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      LOGGER.warning("Bean not Instantiated Error: {}," + id + e.getMessage() + e);
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Flowable<String> getFlowableOfInputModel() {
    boolean filterTestUsers = ImmutableList.copyOf(environment.getActiveProfiles())
        .contains(PRODUCTION);
    return Flowable.empty();
  }

  @Override
  public void logErrorForRecord(String id, Throwable th) {
    LOGGER.warning(EXCEPTION_FOR_UPIN_ERROR_MESSAGE + id + th.getMessage() + th);
  }
}

