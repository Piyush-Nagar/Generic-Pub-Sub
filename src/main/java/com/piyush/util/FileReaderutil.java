package com.piyush.util;

import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;

/**
 * @author Piyush on 5/7/17.
 */
public class FileReaderutil {
  private FileReaderutil() {
  }

  /**
   * get CsvWriter object for passed model, file path and header.
   * depend on header model and file path it will written the writer.
   *
   * @return {@link CsvWriter}
   */
  public static AbstractParser getAbstractParser(Character seperator) {
    CsvParserSettings settings = new CsvParserSettings();
    settings.setNullValue("");
    settings.trimValues(true);
    CsvFormat csvFormat = new CsvFormat();
    csvFormat.setDelimiter(seperator);
    settings.setFormat(csvFormat);
    return new CsvParser(settings);
  }
}