package org.telegram.messenger.time;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public interface DateParser {
   Locale getLocale();

   String getPattern();

   TimeZone getTimeZone();

   Date parse(String var1) throws ParseException;

   Date parse(String var1, ParsePosition var2);

   Object parseObject(String var1) throws ParseException;

   Object parseObject(String var1, ParsePosition var2);
}
