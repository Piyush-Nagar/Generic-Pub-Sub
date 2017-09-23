package com.piyush.features.loader;

import com.piyush.features.AbstractProducer;
import io.reactivex.Flowable;
import java.text.MessageFormat;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Piyush on 8/4/17.
 */
public abstract class AbstractFileProducer<U, V> extends AbstractProducer<U, V> {

  private static final Logger LOGGER = Logger.getLogger(AbstractFileProducer.class.getName());
  private static final String STRING_FORMAT_WITH_TWO_PARAMETERS = "{0}{1}";
  @Value("${input.file.name}")
  private String fileName;
  @Value("${input.file.path}")
  private String filePath;
  @Value("${feature.type}")
  private String reportType;
  private String file;


  @PostConstruct
  protected void initWritter() {
    this.file = MessageFormat.format(STRING_FORMAT_WITH_TWO_PARAMETERS,
                                     filePath,
                                     fileName);
  }

  public AbstractFileProducer(List<Consumer<V>> consumers) {
    super(consumers);
  }

  public abstract Class<U> getModelClass();

  @Override
  public Flowable<U> getFlowableOfInputModel() {
    return Flowable.empty();
  }

  @Override
  public void logErrorForRecord(U u, Throwable throwable) {
    LOGGER.warning("Exception Message: {}" + throwable.getMessage() + throwable);
  }
}
