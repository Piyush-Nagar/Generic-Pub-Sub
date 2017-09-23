package com.piyush.enums;

import java.util.Locale;

/**
 * @author Piyush on 7/5/17.
 * While running the jar, defined the feature type here.
 * We can customize the paramter of enum according to use case.
 */
public enum FeatureType {
  TEST_FEATURE("test_feature", '\t');

  private final String featureType;
  private final char seperator;


  FeatureType(String portType, char seperator) {
    this.featureType = portType;
    this.seperator = seperator;
  }

  public String getFeatureType() {
    return featureType;
  }

  public char getSeperator() {
    return seperator;
  }

  public static char getSeperator(String reportType) {
    return FeatureType.valueOf(reportType.toUpperCase(Locale.ENGLISH)).getSeperator();
  }
}
