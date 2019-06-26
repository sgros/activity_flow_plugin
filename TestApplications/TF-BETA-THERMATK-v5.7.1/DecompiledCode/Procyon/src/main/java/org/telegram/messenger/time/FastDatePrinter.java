// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.time;

import java.util.ArrayList;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.util.Date;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.TimeZone;
import java.util.Locale;
import java.util.concurrent.ConcurrentMap;
import java.io.Serializable;

public class FastDatePrinter implements DatePrinter, Serializable
{
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache;
    private static final long serialVersionUID = 1L;
    private final Locale mLocale;
    private transient int mMaxLengthEstimate;
    private final String mPattern;
    private transient Rule[] mRules;
    private final TimeZone mTimeZone;
    
    static {
        cTimeZoneDisplayCache = new ConcurrentHashMap<TimeZoneDisplayKey, String>(7);
    }
    
    protected FastDatePrinter(final String mPattern, final TimeZone mTimeZone, final Locale mLocale) {
        this.mPattern = mPattern;
        this.mTimeZone = mTimeZone;
        this.mLocale = mLocale;
        this.init();
    }
    
    private String applyRulesToString(final Calendar calendar) {
        return this.applyRules(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }
    
    static String getTimeZoneDisplay(final TimeZone timeZone, final boolean daylight, final int style, final Locale locale) {
        final TimeZoneDisplayKey timeZoneDisplayKey = new TimeZoneDisplayKey(timeZone, daylight, style, locale);
        String displayName;
        if ((displayName = FastDatePrinter.cTimeZoneDisplayCache.get(timeZoneDisplayKey)) == null) {
            displayName = timeZone.getDisplayName(daylight, style, locale);
            final String s = FastDatePrinter.cTimeZoneDisplayCache.putIfAbsent(timeZoneDisplayKey, displayName);
            if (s != null) {
                displayName = s;
            }
        }
        return displayName;
    }
    
    private void init() {
        final List<Rule> pattern = this.parsePattern();
        this.mRules = pattern.toArray(new Rule[pattern.size()]);
        int length = this.mRules.length;
        int mMaxLengthEstimate = 0;
        while (--length >= 0) {
            mMaxLengthEstimate += this.mRules[length].estimateLength();
        }
        this.mMaxLengthEstimate = mMaxLengthEstimate;
    }
    
    private GregorianCalendar newCalendar() {
        return new GregorianCalendar(this.mTimeZone, this.mLocale);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.init();
    }
    
    protected StringBuffer applyRules(final Calendar calendar, final StringBuffer sb) {
        final Rule[] mRules = this.mRules;
        for (int length = mRules.length, i = 0; i < length; ++i) {
            mRules[i].appendTo(sb, calendar);
        }
        return sb;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof FastDatePrinter;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final FastDatePrinter fastDatePrinter = (FastDatePrinter)o;
        boolean b3 = b2;
        if (this.mPattern.equals(fastDatePrinter.mPattern)) {
            b3 = b2;
            if (this.mTimeZone.equals(fastDatePrinter.mTimeZone)) {
                b3 = b2;
                if (this.mLocale.equals(fastDatePrinter.mLocale)) {
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    @Override
    public String format(final long timeInMillis) {
        final GregorianCalendar calendar = this.newCalendar();
        calendar.setTimeInMillis(timeInMillis);
        return this.applyRulesToString(calendar);
    }
    
    @Override
    public String format(final Calendar calendar) {
        return this.format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }
    
    @Override
    public String format(final Date time) {
        final GregorianCalendar calendar = this.newCalendar();
        calendar.setTime(time);
        return this.applyRulesToString(calendar);
    }
    
    @Override
    public StringBuffer format(final long date, final StringBuffer sb) {
        return this.format(new Date(date), sb);
    }
    
    @Override
    public StringBuffer format(final Object o, final StringBuffer sb, final FieldPosition fieldPosition) {
        if (o instanceof Date) {
            return this.format((Date)o, sb);
        }
        if (o instanceof Calendar) {
            return this.format((Calendar)o, sb);
        }
        if (o instanceof Long) {
            return this.format((long)o, sb);
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Unknown class: ");
        String name;
        if (o == null) {
            name = "<null>";
        }
        else {
            name = o.getClass().getName();
        }
        sb2.append(name);
        throw new IllegalArgumentException(sb2.toString());
    }
    
    @Override
    public StringBuffer format(final Calendar calendar, final StringBuffer sb) {
        return this.applyRules(calendar, sb);
    }
    
    @Override
    public StringBuffer format(final Date time, final StringBuffer sb) {
        final GregorianCalendar calendar = this.newCalendar();
        calendar.setTime(time);
        return this.applyRules(calendar, sb);
    }
    
    @Override
    public Locale getLocale() {
        return this.mLocale;
    }
    
    public int getMaxLengthEstimate() {
        return this.mMaxLengthEstimate;
    }
    
    @Override
    public String getPattern() {
        return this.mPattern;
    }
    
    @Override
    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }
    
    @Override
    public int hashCode() {
        return this.mPattern.hashCode() + (this.mTimeZone.hashCode() + this.mLocale.hashCode() * 13) * 13;
    }
    
    protected List<Rule> parsePattern() {
        final DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(this.mLocale);
        final ArrayList<Rule> list = new ArrayList<Rule>();
        final String[] eras = dateFormatSymbols.getEras();
        final String[] months = dateFormatSymbols.getMonths();
        final String[] shortMonths = dateFormatSymbols.getShortMonths();
        final String[] weekdays = dateFormatSymbols.getWeekdays();
        final String[] shortWeekdays = dateFormatSymbols.getShortWeekdays();
        final String[] amPmStrings = dateFormatSymbols.getAmPmStrings();
        final int length = this.mPattern.length();
        final int[] array = { 0 };
        int n;
        for (int i = 0; i < length; i = n + 1) {
            array[0] = i;
            final String token = this.parseToken(this.mPattern, array);
            n = array[0];
            int length2 = token.length();
            if (length2 == 0) {
                break;
            }
            final char char1 = token.charAt(0);
            final int n2 = 4;
            Rule rule = null;
            if (char1 != 'y') {
                Label_0549: {
                    if (char1 != 'z') {
                        switch (char1) {
                            default: {
                                switch (char1) {
                                    default: {
                                        switch (char1) {
                                            default: {
                                                final StringBuilder sb = new StringBuilder();
                                                sb.append("Illegal pattern component: ");
                                                sb.append(token);
                                                throw new IllegalArgumentException(sb.toString());
                                            }
                                            case 77: {
                                                if (length2 >= 4) {
                                                    rule = new TextField(2, months);
                                                    break Label_0549;
                                                }
                                                if (length2 == 3) {
                                                    rule = new TextField(2, shortMonths);
                                                    break Label_0549;
                                                }
                                                if (length2 == 2) {
                                                    rule = TwoDigitMonthField.INSTANCE;
                                                    break Label_0549;
                                                }
                                                rule = UnpaddedMonthField.INSTANCE;
                                                break Label_0549;
                                            }
                                            case 76: {
                                                if (length2 >= 4) {
                                                    rule = new TextField(2, months);
                                                    break Label_0549;
                                                }
                                                if (length2 == 3) {
                                                    rule = new TextField(2, shortMonths);
                                                    break Label_0549;
                                                }
                                                if (length2 == 2) {
                                                    rule = TwoDigitMonthField.INSTANCE;
                                                    break Label_0549;
                                                }
                                                rule = UnpaddedMonthField.INSTANCE;
                                                break Label_0549;
                                            }
                                            case 75: {
                                                rule = this.selectNumberRule(10, length2);
                                                break Label_0549;
                                            }
                                        }
                                        break;
                                    }
                                    case 72: {
                                        rule = this.selectNumberRule(11, length2);
                                        break Label_0549;
                                    }
                                    case 71: {
                                        rule = new TextField(0, eras);
                                        break Label_0549;
                                    }
                                    case 70: {
                                        rule = this.selectNumberRule(8, length2);
                                        break Label_0549;
                                    }
                                    case 69: {
                                        String[] array2;
                                        if (length2 < 4) {
                                            array2 = shortWeekdays;
                                        }
                                        else {
                                            array2 = weekdays;
                                        }
                                        rule = new TextField(7, array2);
                                        break Label_0549;
                                    }
                                    case 68: {
                                        rule = this.selectNumberRule(6, length2);
                                        break Label_0549;
                                    }
                                }
                                break;
                            }
                            case 119: {
                                rule = this.selectNumberRule(3, length2);
                                break;
                            }
                            case 115: {
                                rule = this.selectNumberRule(13, length2);
                                break;
                            }
                            case 109: {
                                rule = this.selectNumberRule(12, length2);
                                break;
                            }
                            case 107: {
                                rule = new TwentyFourHourField(this.selectNumberRule(11, length2));
                                break;
                            }
                            case 104: {
                                rule = new TwelveHourField(this.selectNumberRule(10, length2));
                                break;
                            }
                            case 100: {
                                rule = this.selectNumberRule(5, length2);
                                break;
                            }
                            case 97: {
                                rule = new TextField(9, amPmStrings);
                                break;
                            }
                            case 90: {
                                if (length2 == 1) {
                                    rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
                                    break;
                                }
                                rule = TimeZoneNumberRule.INSTANCE_COLON;
                                break;
                            }
                            case 87: {
                                rule = this.selectNumberRule(4, length2);
                                break;
                            }
                            case 83: {
                                rule = this.selectNumberRule(14, length2);
                                break;
                            }
                            case 39: {
                                final String substring = token.substring(1);
                                if (substring.length() == 1) {
                                    rule = new CharacterLiteral(substring.charAt(0));
                                    break;
                                }
                                rule = new StringLiteral(substring);
                                break;
                            }
                        }
                    }
                    else if (length2 >= 4) {
                        rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 1);
                    }
                    else {
                        rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
                    }
                }
            }
            else if (length2 == 2) {
                rule = TwoDigitYearField.INSTANCE;
            }
            else {
                if (length2 < 4) {
                    length2 = n2;
                }
                rule = this.selectNumberRule(1, length2);
            }
            list.add(rule);
        }
        return list;
    }
    
    protected String parseToken(final String s, final int[] array) {
        final StringBuilder sb = new StringBuilder();
        int n = array[0];
        final int length = s.length();
        final char char1 = s.charAt(n);
        int n2;
        if ((char1 >= 'A' && char1 <= 'Z') || (char1 >= 'a' && char1 <= 'z')) {
            sb.append(char1);
            while (true) {
                final int index = n + 1;
                n2 = n;
                if (index >= length) {
                    break;
                }
                n2 = n;
                if (s.charAt(index) != char1) {
                    break;
                }
                sb.append(char1);
                n = index;
            }
        }
        else {
            sb.append('\'');
            int n3 = 0;
            while (true) {
                n2 = n;
                if (n >= length) {
                    break;
                }
                final char char2 = s.charAt(n);
                if (char2 == '\'') {
                    final int index2 = n + 1;
                    if (index2 < length && s.charAt(index2) == '\'') {
                        sb.append(char2);
                        n = index2;
                    }
                    else {
                        n3 ^= 0x1;
                    }
                }
                else {
                    if (n3 == 0 && ((char2 >= 'A' && char2 <= 'Z') || (char2 >= 'a' && char2 <= 'z'))) {
                        n2 = n - 1;
                        break;
                    }
                    sb.append(char2);
                }
                ++n;
            }
        }
        array[0] = n2;
        return sb.toString();
    }
    
    protected NumberRule selectNumberRule(final int n, final int n2) {
        if (n2 == 1) {
            return (NumberRule)new UnpaddedNumberField(n);
        }
        if (n2 != 2) {
            return (NumberRule)new PaddedNumberField(n, n2);
        }
        return (NumberRule)new TwoDigitNumberField(n);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("FastDatePrinter[");
        sb.append(this.mPattern);
        sb.append(",");
        sb.append(this.mLocale);
        sb.append(",");
        sb.append(this.mTimeZone.getID());
        sb.append("]");
        return sb.toString();
    }
    
    private static class CharacterLiteral implements Rule
    {
        private final char mValue;
        
        CharacterLiteral(final char c) {
            this.mValue = c;
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final Calendar calendar) {
            sb.append(this.mValue);
        }
        
        @Override
        public int estimateLength() {
            return 1;
        }
    }
    
    private interface NumberRule extends Rule
    {
        void appendTo(final StringBuffer p0, final int p1);
    }
    
    private static class PaddedNumberField implements NumberRule
    {
        private final int mField;
        private final int mSize;
        
        PaddedNumberField(final int mField, final int mSize) {
            if (mSize >= 3) {
                this.mField = mField;
                this.mSize = mSize;
                return;
            }
            throw new IllegalArgumentException();
        }
        
        @Override
        public final void appendTo(final StringBuffer sb, final int n) {
            if (n < 100) {
                int mSize = this.mSize;
                while (--mSize >= 2) {
                    sb.append('0');
                }
                sb.append((char)(n / 10 + 48));
                sb.append((char)(n % 10 + 48));
            }
            else {
                int length;
                if (n < 1000) {
                    length = 3;
                }
                else {
                    length = Integer.toString(n).length();
                }
                int mSize2 = this.mSize;
                while (--mSize2 >= length) {
                    sb.append('0');
                }
                sb.append(Integer.toString(n));
            }
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final Calendar calendar) {
            this.appendTo(sb, calendar.get(this.mField));
        }
        
        @Override
        public int estimateLength() {
            return 4;
        }
    }
    
    private interface Rule
    {
        void appendTo(final StringBuffer p0, final Calendar p1);
        
        int estimateLength();
    }
    
    private static class StringLiteral implements Rule
    {
        private final String mValue;
        
        StringLiteral(final String mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final Calendar calendar) {
            sb.append(this.mValue);
        }
        
        @Override
        public int estimateLength() {
            return this.mValue.length();
        }
    }
    
    private static class TextField implements Rule
    {
        private final int mField;
        private final String[] mValues;
        
        TextField(final int mField, final String[] mValues) {
            this.mField = mField;
            this.mValues = mValues;
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final Calendar calendar) {
            sb.append(this.mValues[calendar.get(this.mField)]);
        }
        
        @Override
        public int estimateLength() {
            int length = this.mValues.length;
            int n = 0;
            while (true) {
                final int n2 = length - 1;
                if (n2 < 0) {
                    break;
                }
                final int length2 = this.mValues[n2].length();
                length = n2;
                if (length2 <= n) {
                    continue;
                }
                n = length2;
                length = n2;
            }
            return n;
        }
    }
    
    private static class TimeZoneDisplayKey
    {
        private final Locale mLocale;
        private final int mStyle;
        private final TimeZone mTimeZone;
        
        TimeZoneDisplayKey(final TimeZone mTimeZone, final boolean b, final int mStyle, final Locale mLocale) {
            this.mTimeZone = mTimeZone;
            if (b) {
                this.mStyle = (Integer.MIN_VALUE | mStyle);
            }
            else {
                this.mStyle = mStyle;
            }
            this.mLocale = mLocale;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (o instanceof TimeZoneDisplayKey) {
                final TimeZoneDisplayKey timeZoneDisplayKey = (TimeZoneDisplayKey)o;
                if (!this.mTimeZone.equals(timeZoneDisplayKey.mTimeZone) || this.mStyle != timeZoneDisplayKey.mStyle || !this.mLocale.equals(timeZoneDisplayKey.mLocale)) {
                    b = false;
                }
                return b;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return (this.mStyle * 31 + this.mLocale.hashCode()) * 31 + this.mTimeZone.hashCode();
        }
    }
    
    private static class TimeZoneNameRule implements Rule
    {
        private final String mDaylight;
        private final Locale mLocale;
        private final String mStandard;
        private final int mStyle;
        
        TimeZoneNameRule(final TimeZone timeZone, final Locale mLocale, final int mStyle) {
            this.mLocale = mLocale;
            this.mStyle = mStyle;
            this.mStandard = FastDatePrinter.getTimeZoneDisplay(timeZone, false, mStyle, mLocale);
            this.mDaylight = FastDatePrinter.getTimeZoneDisplay(timeZone, true, mStyle, mLocale);
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final Calendar calendar) {
            final TimeZone timeZone = calendar.getTimeZone();
            if (timeZone.useDaylightTime() && calendar.get(16) != 0) {
                sb.append(FastDatePrinter.getTimeZoneDisplay(timeZone, true, this.mStyle, this.mLocale));
            }
            else {
                sb.append(FastDatePrinter.getTimeZoneDisplay(timeZone, false, this.mStyle, this.mLocale));
            }
        }
        
        @Override
        public int estimateLength() {
            return Math.max(this.mStandard.length(), this.mDaylight.length());
        }
    }
    
    private static class TimeZoneNumberRule implements Rule
    {
        static final TimeZoneNumberRule INSTANCE_COLON;
        static final TimeZoneNumberRule INSTANCE_NO_COLON;
        final boolean mColon;
        
        static {
            INSTANCE_COLON = new TimeZoneNumberRule(true);
            INSTANCE_NO_COLON = new TimeZoneNumberRule(false);
        }
        
        TimeZoneNumberRule(final boolean mColon) {
            this.mColon = mColon;
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final Calendar calendar) {
            int n = calendar.get(15) + calendar.get(16);
            if (n < 0) {
                sb.append('-');
                n = -n;
            }
            else {
                sb.append('+');
            }
            final int n2 = n / 3600000;
            sb.append((char)(n2 / 10 + 48));
            sb.append((char)(n2 % 10 + 48));
            if (this.mColon) {
                sb.append(':');
            }
            final int n3 = n / 60000 - n2 * 60;
            sb.append((char)(n3 / 10 + 48));
            sb.append((char)(n3 % 10 + 48));
        }
        
        @Override
        public int estimateLength() {
            return 5;
        }
    }
    
    private static class TwelveHourField implements NumberRule
    {
        private final NumberRule mRule;
        
        TwelveHourField(final NumberRule mRule) {
            this.mRule = mRule;
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final int n) {
            this.mRule.appendTo(sb, n);
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final Calendar calendar) {
            int value;
            if ((value = calendar.get(10)) == 0) {
                value = calendar.getLeastMaximum(10) + 1;
            }
            this.mRule.appendTo(sb, value);
        }
        
        @Override
        public int estimateLength() {
            return ((Rule)this.mRule).estimateLength();
        }
    }
    
    private static class TwentyFourHourField implements NumberRule
    {
        private final NumberRule mRule;
        
        TwentyFourHourField(final NumberRule mRule) {
            this.mRule = mRule;
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final int n) {
            this.mRule.appendTo(sb, n);
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final Calendar calendar) {
            int value;
            if ((value = calendar.get(11)) == 0) {
                value = calendar.getMaximum(11) + 1;
            }
            this.mRule.appendTo(sb, value);
        }
        
        @Override
        public int estimateLength() {
            return ((Rule)this.mRule).estimateLength();
        }
    }
    
    private static class TwoDigitMonthField implements NumberRule
    {
        static final TwoDigitMonthField INSTANCE;
        
        static {
            INSTANCE = new TwoDigitMonthField();
        }
        
        TwoDigitMonthField() {
        }
        
        @Override
        public final void appendTo(final StringBuffer sb, final int n) {
            sb.append((char)(n / 10 + 48));
            sb.append((char)(n % 10 + 48));
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final Calendar calendar) {
            this.appendTo(sb, calendar.get(2) + 1);
        }
        
        @Override
        public int estimateLength() {
            return 2;
        }
    }
    
    private static class TwoDigitNumberField implements NumberRule
    {
        private final int mField;
        
        TwoDigitNumberField(final int mField) {
            this.mField = mField;
        }
        
        @Override
        public final void appendTo(final StringBuffer sb, final int i) {
            if (i < 100) {
                sb.append((char)(i / 10 + 48));
                sb.append((char)(i % 10 + 48));
            }
            else {
                sb.append(Integer.toString(i));
            }
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final Calendar calendar) {
            this.appendTo(sb, calendar.get(this.mField));
        }
        
        @Override
        public int estimateLength() {
            return 2;
        }
    }
    
    private static class TwoDigitYearField implements NumberRule
    {
        static final TwoDigitYearField INSTANCE;
        
        static {
            INSTANCE = new TwoDigitYearField();
        }
        
        TwoDigitYearField() {
        }
        
        @Override
        public final void appendTo(final StringBuffer sb, final int n) {
            sb.append((char)(n / 10 + 48));
            sb.append((char)(n % 10 + 48));
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final Calendar calendar) {
            this.appendTo(sb, calendar.get(1) % 100);
        }
        
        @Override
        public int estimateLength() {
            return 2;
        }
    }
    
    private static class UnpaddedMonthField implements NumberRule
    {
        static final UnpaddedMonthField INSTANCE;
        
        static {
            INSTANCE = new UnpaddedMonthField();
        }
        
        UnpaddedMonthField() {
        }
        
        @Override
        public final void appendTo(final StringBuffer sb, final int n) {
            if (n < 10) {
                sb.append((char)(n + 48));
            }
            else {
                sb.append((char)(n / 10 + 48));
                sb.append((char)(n % 10 + 48));
            }
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final Calendar calendar) {
            this.appendTo(sb, calendar.get(2) + 1);
        }
        
        @Override
        public int estimateLength() {
            return 2;
        }
    }
    
    private static class UnpaddedNumberField implements NumberRule
    {
        private final int mField;
        
        UnpaddedNumberField(final int mField) {
            this.mField = mField;
        }
        
        @Override
        public final void appendTo(final StringBuffer sb, final int i) {
            if (i < 10) {
                sb.append((char)(i + 48));
            }
            else if (i < 100) {
                sb.append((char)(i / 10 + 48));
                sb.append((char)(i % 10 + 48));
            }
            else {
                sb.append(Integer.toString(i));
            }
        }
        
        @Override
        public void appendTo(final StringBuffer sb, final Calendar calendar) {
            this.appendTo(sb, calendar.get(this.mField));
        }
        
        @Override
        public int estimateLength() {
            return 4;
        }
    }
}
