// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.time;

import java.text.ParsePosition;
import java.text.ParseException;
import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.text.Format;

public class FastDateFormat extends Format implements DateParser, DatePrinter
{
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    private static final FormatCache<FastDateFormat> cache;
    private static final long serialVersionUID = 2L;
    private final FastDateParser parser;
    private final FastDatePrinter printer;
    
    static {
        cache = new FormatCache<FastDateFormat>() {
            @Override
            protected FastDateFormat createInstance(final String s, final TimeZone timeZone, final Locale locale) {
                return new FastDateFormat(s, timeZone, locale);
            }
        };
    }
    
    protected FastDateFormat(final String s, final TimeZone timeZone, final Locale locale) {
        this(s, timeZone, locale, null);
    }
    
    protected FastDateFormat(final String s, final TimeZone timeZone, final Locale locale, final Date date) {
        this.printer = new FastDatePrinter(s, timeZone, locale);
        this.parser = new FastDateParser(s, timeZone, locale, date);
    }
    
    public static FastDateFormat getDateInstance(final int n) {
        return FastDateFormat.cache.getDateInstance(n, null, null);
    }
    
    public static FastDateFormat getDateInstance(final int n, final Locale locale) {
        return FastDateFormat.cache.getDateInstance(n, null, locale);
    }
    
    public static FastDateFormat getDateInstance(final int n, final TimeZone timeZone) {
        return FastDateFormat.cache.getDateInstance(n, timeZone, null);
    }
    
    public static FastDateFormat getDateInstance(final int n, final TimeZone timeZone, final Locale locale) {
        return FastDateFormat.cache.getDateInstance(n, timeZone, locale);
    }
    
    public static FastDateFormat getDateTimeInstance(final int n, final int n2) {
        return FastDateFormat.cache.getDateTimeInstance(n, n2, null, null);
    }
    
    public static FastDateFormat getDateTimeInstance(final int n, final int n2, final Locale locale) {
        return FastDateFormat.cache.getDateTimeInstance(n, n2, null, locale);
    }
    
    public static FastDateFormat getDateTimeInstance(final int n, final int n2, final TimeZone timeZone) {
        return getDateTimeInstance(n, n2, timeZone, null);
    }
    
    public static FastDateFormat getDateTimeInstance(final int n, final int n2, final TimeZone timeZone, final Locale locale) {
        return FastDateFormat.cache.getDateTimeInstance(n, n2, timeZone, locale);
    }
    
    public static FastDateFormat getInstance() {
        return FastDateFormat.cache.getInstance();
    }
    
    public static FastDateFormat getInstance(final String s) {
        return FastDateFormat.cache.getInstance(s, null, null);
    }
    
    public static FastDateFormat getInstance(final String s, final Locale locale) {
        return FastDateFormat.cache.getInstance(s, null, locale);
    }
    
    public static FastDateFormat getInstance(final String s, final TimeZone timeZone) {
        return FastDateFormat.cache.getInstance(s, timeZone, null);
    }
    
    public static FastDateFormat getInstance(final String s, final TimeZone timeZone, final Locale locale) {
        return FastDateFormat.cache.getInstance(s, timeZone, locale);
    }
    
    public static FastDateFormat getTimeInstance(final int n) {
        return FastDateFormat.cache.getTimeInstance(n, null, null);
    }
    
    public static FastDateFormat getTimeInstance(final int n, final Locale locale) {
        return FastDateFormat.cache.getTimeInstance(n, null, locale);
    }
    
    public static FastDateFormat getTimeInstance(final int n, final TimeZone timeZone) {
        return FastDateFormat.cache.getTimeInstance(n, timeZone, null);
    }
    
    public static FastDateFormat getTimeInstance(final int n, final TimeZone timeZone, final Locale locale) {
        return FastDateFormat.cache.getTimeInstance(n, timeZone, locale);
    }
    
    protected StringBuffer applyRules(final Calendar calendar, final StringBuffer sb) {
        return this.printer.applyRules(calendar, sb);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof FastDateFormat && this.printer.equals(((FastDateFormat)o).printer);
    }
    
    @Override
    public String format(final long n) {
        return this.printer.format(n);
    }
    
    @Override
    public String format(final Calendar calendar) {
        return this.printer.format(calendar);
    }
    
    @Override
    public String format(final Date date) {
        return this.printer.format(date);
    }
    
    @Override
    public StringBuffer format(final long n, final StringBuffer sb) {
        return this.printer.format(n, sb);
    }
    
    @Override
    public StringBuffer format(final Object o, final StringBuffer sb, final FieldPosition fieldPosition) {
        return this.printer.format(o, sb, fieldPosition);
    }
    
    @Override
    public StringBuffer format(final Calendar calendar, final StringBuffer sb) {
        return this.printer.format(calendar, sb);
    }
    
    @Override
    public StringBuffer format(final Date date, final StringBuffer sb) {
        return this.printer.format(date, sb);
    }
    
    @Override
    public Locale getLocale() {
        return this.printer.getLocale();
    }
    
    public int getMaxLengthEstimate() {
        return this.printer.getMaxLengthEstimate();
    }
    
    @Override
    public String getPattern() {
        return this.printer.getPattern();
    }
    
    @Override
    public TimeZone getTimeZone() {
        return this.printer.getTimeZone();
    }
    
    @Override
    public int hashCode() {
        return this.printer.hashCode();
    }
    
    @Override
    public Date parse(final String s) throws ParseException {
        return this.parser.parse(s);
    }
    
    @Override
    public Date parse(final String s, final ParsePosition parsePosition) {
        return this.parser.parse(s, parsePosition);
    }
    
    @Override
    public Object parseObject(final String s, final ParsePosition parsePosition) {
        return this.parser.parseObject(s, parsePosition);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("FastDateFormat[");
        sb.append(this.printer.getPattern());
        sb.append(",");
        sb.append(this.printer.getLocale());
        sb.append(",");
        sb.append(this.printer.getTimeZone().getID());
        sb.append("]");
        return sb.toString();
    }
}
