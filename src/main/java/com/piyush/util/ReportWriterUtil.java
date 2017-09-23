package com.piyush.util;

import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import java.io.File;

/**
 * @author Piyush on 5/7/17.
 */
public class ReportWriterUtil {
  private ReportWriterUtil() {
  }

  /**
   * get CsvWriter object for passed model, file path and header.
   * depend on header model and file path it will written the writer.
   *
   * @param model    model for which csv writer is needed.
   * @param filePath file path for date to be written.
   * @param <T>      Parameter for passing Generic type.
   * @return {@link CsvWriter}
   */
  public static <T> CsvWriter getCsvWriter(Class<T> model, String filePath, Character seperator) {
    CsvWriterSettings settings = new CsvWriterSettings();
    settings.setRowWriterProcessor(new BeanWriterProcessor<>(model));
    settings.setNullValue("");
    CsvFormat csvFormat = new CsvFormat();
    csvFormat.setDelimiter(seperator);
    settings.setFormat(csvFormat);
    CsvWriter writer = new CsvWriter(new File(filePath), settings);
    writer.writeHeaders();
    return writer;
  }
}