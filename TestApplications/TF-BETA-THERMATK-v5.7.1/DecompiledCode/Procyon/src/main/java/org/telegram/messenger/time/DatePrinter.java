// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.time;

import java.util.TimeZone;
import java.util.Locale;
import java.text.FieldPosition;
import java.util.Date;
import java.util.Calendar;

public interface DatePrinter
{
    String format(final long p0);
    
    String format(final Calendar p0);
    
    String format(final Date p0);
    
    StringBuffer format(final long p0, final StringBuffer p1);
    
    StringBuffer format(final Object p0, final StringBuffer p1, final FieldPosition p2);
    
    StringBuffer format(final Calendar p0, final StringBuffer p1);
    
    StringBuffer format(final Date p0, final StringBuffer p1);
    
    Locale getLocale();
    
    String getPattern();
    
    TimeZone getTimeZone();
}
