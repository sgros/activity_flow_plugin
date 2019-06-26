// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.time;

import java.text.ParsePosition;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;
import java.util.Locale;

public interface DateParser
{
    Locale getLocale();
    
    String getPattern();
    
    TimeZone getTimeZone();
    
    Date parse(final String p0) throws ParseException;
    
    Date parse(final String p0, final ParsePosition p1);
    
    Object parseObject(final String p0) throws ParseException;
    
    Object parseObject(final String p0, final ParsePosition p1);
}
