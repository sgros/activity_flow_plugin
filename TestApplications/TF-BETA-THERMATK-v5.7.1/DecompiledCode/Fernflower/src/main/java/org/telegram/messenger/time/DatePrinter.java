package org.telegram.messenger.time;

import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public interface DatePrinter {
   String format(long var1);

   String format(Calendar var1);

   String format(Date var1);

   StringBuffer format(long var1, StringBuffer var3);

   StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3);

   StringBuffer format(Calendar var1, StringBuffer var2);

   StringBuffer format(Date var1, StringBuffer var2);

   Locale getLocale();

   String getPattern();

   TimeZone getTimeZone();
}
