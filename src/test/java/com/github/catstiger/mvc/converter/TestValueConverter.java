package com.github.catstiger.mvc.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestValueConverter implements ValueConverter<Object> {
  private static Logger logger = LoggerFactory.getLogger(TestValueConverter.class);
  @Override
  public Object convert(Object value) {
    logger.debug("Using test converter.");
    return "catstiger@gmail.com";
  }

}
