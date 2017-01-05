package com.github.catstiger.mvc.converter;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import com.github.catstiger.mvc.config.Initializer;
import com.github.catstiger.utils.StringUtils;

public class DateValueConverter implements ValueConverter<Date> {

  @Override
  public Date convert(Object value) {
    if(value == null) {
      return null;
    }
    
    String trimmed = StringUtils.trimToEmpty(value.toString());
    if("".equals(trimmed)) {
      return null;  
    }
    
    if(StringUtils.isNumber(trimmed)) {
      return new Date(Long.valueOf(trimmed));
    } else {
      return parseDate(trimmed);
    }
  }
  
  private static Date parseDate(String datetime) {
    if(StringUtils.isBlank(datetime)) {
      return null;
    }
    Initializer initializer = Initializer.getInstance();
    
    DateTimeParser[] parsers = { DateTimeFormat.forPattern(initializer.getTimeFormat()).getParser(),
        DateTimeFormat.forPattern(initializer.getMinuteFormat()).getParser(),
        DateTimeFormat.forPattern(initializer.getHourFormat()).getParser(), 
        DateTimeFormat.forPattern(initializer.getDateFormat()).getParser() };
    DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();
    DateTime dt = formatter.parseDateTime(datetime);
    return dt == null ? null : dt.toDate();
  }

}
