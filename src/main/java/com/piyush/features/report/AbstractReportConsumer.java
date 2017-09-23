package com.piyush.features.report;

import com.piyush.enums.FeatureType;
import com.piyush.util.ReportWriterUtil;
import com.univocity.parsers.csv.CsvWriter;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Piyush on 7/28/17.
 */
public abstract class AbstractReportConsumer<V> implements Consumer<V> {
  private static final String STRING_FORMAT_WITH_THREE_PARAMETERS = "{0}{1}{2}";
  private CsvWriter csvWriter;
  @Value("${feature.type:}")
  private String reportType;
  @Value("${output.file.name}")
  private String fileName;
  @Value("${output.file.path}")
  private String filePath;


  @PostConstruct
  protected void initWritter() {
    String file = buildAbsoluteFileName();
    Class<V> modelType = getModelType();
    csvWriter =
        ReportWriterUtil.getCsvWriter(modelType, file, FeatureType.getSeperator(reportType));
  }

  public String getFilePath() {
    return filePath;
  }

  public String getFileName() {
    return fileName;
  }


  /**
   * default file name.
   *
   * @return file name is default.
   */
  public String buildAbsoluteFileName() {
    return MessageFormat.format(STRING_FORMAT_WITH_THREE_PARAMETERS,
                                filePath,
                                fileName,
                                LocalDate.now());
  }

  protected abstract Class<V> getModelType();

  protected void write(V v) {
    synchronized (this) {
      csvWriter.processRecord(v);
    }
  }

  @PreDestroy
  public void closeWriter() {
    csvWriter.close();
  }
}