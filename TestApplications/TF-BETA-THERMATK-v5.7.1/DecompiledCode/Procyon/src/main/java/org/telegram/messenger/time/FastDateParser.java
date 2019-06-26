// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.time;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Iterator;
import java.text.ParseException;
import java.text.ParsePosition;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.HashMap;
import java.text.DateFormatSymbols;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.util.concurrent.ConcurrentMap;
import java.util.Locale;
import java.io.Serializable;

public class FastDateParser implements DateParser, Serializable
{
    private static final Strategy ABBREVIATED_YEAR_STRATEGY;
    private static final Strategy DAY_OF_MONTH_STRATEGY;
    private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY;
    private static final Strategy DAY_OF_YEAR_STRATEGY;
    private static final Strategy HOUR_OF_DAY_STRATEGY;
    private static final Strategy HOUR_STRATEGY;
    static final Locale JAPANESE_IMPERIAL;
    private static final Strategy LITERAL_YEAR_STRATEGY;
    private static final Strategy MILLISECOND_STRATEGY;
    private static final Strategy MINUTE_STRATEGY;
    private static final Strategy MODULO_HOUR_OF_DAY_STRATEGY;
    private static final Strategy MODULO_HOUR_STRATEGY;
    private static final Strategy NUMBER_MONTH_STRATEGY;
    private static final Strategy SECOND_STRATEGY;
    private static final Strategy WEEK_OF_MONTH_STRATEGY;
    private static final Strategy WEEK_OF_YEAR_STRATEGY;
    private static final ConcurrentMap<Locale, Strategy>[] caches;
    private static final Pattern formatPattern;
    private static final long serialVersionUID = 2L;
    private final int century;
    private transient String currentFormatField;
    private final Locale locale;
    private transient Strategy nextStrategy;
    private transient Pattern parsePattern;
    private final String pattern;
    private final int startYear;
    private transient Strategy[] strategies;
    private final TimeZone timeZone;
    
    static {
        JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
        formatPattern = Pattern.compile("D+|E+|F+|G+|H+|K+|M+|L+|S+|W+|Z+|a+|d+|h+|k+|m+|s+|w+|y+|z+|''|'[^']++(''[^']*+)*+'|[^'A-Za-z]++");
        caches = new ConcurrentMap[17];
        ABBREVIATED_YEAR_STRATEGY = (Strategy)new NumberStrategy(1) {
            @Override
            void setCalendar(final FastDateParser fastDateParser, final Calendar calendar, final String s) {
                int value;
                final int n = value = Integer.parseInt(s);
                if (n < 100) {
                    value = fastDateParser.adjustYear(n);
                }
                calendar.set(1, value);
            }
        };
        NUMBER_MONTH_STRATEGY = (Strategy)new NumberStrategy(2) {
            @Override
            int modify(final int n) {
                return n - 1;
            }
        };
        LITERAL_YEAR_STRATEGY = (Strategy)new NumberStrategy(1);
        WEEK_OF_YEAR_STRATEGY = (Strategy)new NumberStrategy(3);
        WEEK_OF_MONTH_STRATEGY = (Strategy)new NumberStrategy(4);
        DAY_OF_YEAR_STRATEGY = (Strategy)new NumberStrategy(6);
        DAY_OF_MONTH_STRATEGY = (Strategy)new NumberStrategy(5);
        DAY_OF_WEEK_IN_MONTH_STRATEGY = (Strategy)new NumberStrategy(8);
        HOUR_OF_DAY_STRATEGY = (Strategy)new NumberStrategy(11);
        MODULO_HOUR_OF_DAY_STRATEGY = (Strategy)new NumberStrategy(11) {
            @Override
            int modify(final int n) {
                return n % 24;
            }
        };
        MODULO_HOUR_STRATEGY = (Strategy)new NumberStrategy(10) {
            @Override
            int modify(final int n) {
                return n % 12;
            }
        };
        HOUR_STRATEGY = (Strategy)new NumberStrategy(10);
        MINUTE_STRATEGY = (Strategy)new NumberStrategy(12);
        SECOND_STRATEGY = (Strategy)new NumberStrategy(13);
        MILLISECOND_STRATEGY = (Strategy)new NumberStrategy(14);
    }
    
    protected FastDateParser(final String s, final TimeZone timeZone, final Locale locale) {
        this(s, timeZone, locale, null);
    }
    
    protected FastDateParser(final String pattern, final TimeZone timeZone, final Locale locale, final Date time) {
        this.pattern = pattern;
        this.timeZone = timeZone;
        this.locale = locale;
        final Calendar instance = Calendar.getInstance(timeZone, locale);
        int value;
        if (time != null) {
            instance.setTime(time);
            value = instance.get(1);
        }
        else if (locale.equals(FastDateParser.JAPANESE_IMPERIAL)) {
            value = 0;
        }
        else {
            instance.setTime(new Date());
            value = instance.get(1) - 80;
        }
        this.century = value / 100 * 100;
        this.startYear = value - this.century;
        this.init(instance);
    }
    
    static /* synthetic */ StringBuilder access$100(final StringBuilder sb, final String s, final boolean b) {
        escapeRegex(sb, s, b);
        return sb;
    }
    
    private int adjustYear(int n) {
        final int n2 = this.century + n;
        if (n >= this.startYear) {
            n = n2;
        }
        else {
            n = n2 + 100;
        }
        return n;
    }
    
    private static StringBuilder escapeRegex(final StringBuilder sb, final String s, final boolean b) {
        sb.append("\\Q");
        int index;
        for (int i = 0; i < s.length(); i = index + 1) {
            char c = s.charAt(i);
            if (c != '\'') {
                if (c != '\\') {
                    index = i;
                }
                else if (++i == s.length()) {
                    index = i;
                }
                else {
                    sb.append(c);
                    final char char1 = s.charAt(i);
                    index = i;
                    if ((c = char1) == 'E') {
                        sb.append("E\\\\E\\");
                        final char c2 = 'Q';
                        index = i;
                        c = c2;
                    }
                }
            }
            else {
                index = i;
                if (b) {
                    index = i + 1;
                    if (index == s.length()) {
                        return sb;
                    }
                    c = s.charAt(index);
                }
            }
            sb.append(c);
        }
        sb.append("\\E");
        return sb;
    }
    
    private static ConcurrentMap<Locale, Strategy> getCache(final int n) {
        synchronized (FastDateParser.caches) {
            if (FastDateParser.caches[n] == null) {
                FastDateParser.caches[n] = new ConcurrentHashMap<Locale, Strategy>(3);
            }
            return FastDateParser.caches[n];
        }
    }
    
    private static String[] getDisplayNameArray(final int n, final boolean b, final Locale locale) {
        final DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
        if (n == 0) {
            return dateFormatSymbols.getEras();
        }
        if (n == 2) {
            String[] array;
            if (b) {
                array = dateFormatSymbols.getMonths();
            }
            else {
                array = dateFormatSymbols.getShortMonths();
            }
            return array;
        }
        if (n == 7) {
            String[] array2;
            if (b) {
                array2 = dateFormatSymbols.getWeekdays();
            }
            else {
                array2 = dateFormatSymbols.getShortWeekdays();
            }
            return array2;
        }
        if (n != 9) {
            return null;
        }
        return dateFormatSymbols.getAmPmStrings();
    }
    
    private static Map<String, Integer> getDisplayNames(final int n, final Calendar calendar, final Locale locale) {
        return getDisplayNames(n, locale);
    }
    
    private static Map<String, Integer> getDisplayNames(final int n, final Locale locale) {
        final HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        insertValuesInMap(hashMap, getDisplayNameArray(n, false, locale));
        insertValuesInMap(hashMap, getDisplayNameArray(n, true, locale));
        HashMap<String, Integer> hashMap2 = hashMap;
        if (hashMap.isEmpty()) {
            hashMap2 = null;
        }
        return hashMap2;
    }
    
    private Strategy getLocaleSpecificStrategy(final int n, final Calendar calendar) {
        final ConcurrentMap<Locale, Strategy> cache = getCache(n);
        Strategy strategy;
        if ((strategy = cache.get(this.locale)) == null) {
            Strategy strategy2;
            if (n == 15) {
                strategy2 = new TimeZoneStrategy(this.locale);
            }
            else {
                strategy2 = new TextStrategy(n, calendar, this.locale);
            }
            final Strategy strategy3 = cache.putIfAbsent(this.locale, strategy2);
            strategy = strategy2;
            if (strategy3 != null) {
                return strategy3;
            }
        }
        return strategy;
    }
    
    private Strategy getStrategy(final String s, final Calendar calendar) {
        final char char1 = s.charAt(0);
        if (char1 != 'y') {
            if (char1 != 'z') {
                Label_0305: {
                    switch (char1) {
                        default: {
                            switch (char1) {
                                default: {
                                    switch (char1) {
                                        default: {
                                            break Label_0305;
                                        }
                                        case 76:
                                        case 77: {
                                            Strategy strategy;
                                            if (s.length() >= 3) {
                                                strategy = this.getLocaleSpecificStrategy(2, calendar);
                                            }
                                            else {
                                                strategy = FastDateParser.NUMBER_MONTH_STRATEGY;
                                            }
                                            return strategy;
                                        }
                                        case 75: {
                                            return FastDateParser.HOUR_STRATEGY;
                                        }
                                    }
                                    break;
                                }
                                case 72: {
                                    return FastDateParser.MODULO_HOUR_OF_DAY_STRATEGY;
                                }
                                case 71: {
                                    return this.getLocaleSpecificStrategy(0, calendar);
                                }
                                case 70: {
                                    return FastDateParser.DAY_OF_WEEK_IN_MONTH_STRATEGY;
                                }
                                case 69: {
                                    return this.getLocaleSpecificStrategy(7, calendar);
                                }
                                case 68: {
                                    return FastDateParser.DAY_OF_YEAR_STRATEGY;
                                }
                            }
                            break;
                        }
                        case 119: {
                            return FastDateParser.WEEK_OF_YEAR_STRATEGY;
                        }
                        case 115: {
                            return FastDateParser.SECOND_STRATEGY;
                        }
                        case 109: {
                            return FastDateParser.MINUTE_STRATEGY;
                        }
                        case 107: {
                            return FastDateParser.HOUR_OF_DAY_STRATEGY;
                        }
                        case 104: {
                            return FastDateParser.MODULO_HOUR_STRATEGY;
                        }
                        case 100: {
                            return FastDateParser.DAY_OF_MONTH_STRATEGY;
                        }
                        case 97: {
                            return this.getLocaleSpecificStrategy(9, calendar);
                        }
                        case 87: {
                            return FastDateParser.WEEK_OF_MONTH_STRATEGY;
                        }
                        case 83: {
                            return FastDateParser.MILLISECOND_STRATEGY;
                        }
                        case 39: {
                            if (s.length() > 2) {
                                return (Strategy)new CopyQuotedStrategy(s.substring(1, s.length() - 1));
                            }
                            break;
                        }
                        case 90: {
                            return this.getLocaleSpecificStrategy(15, calendar);
                        }
                    }
                }
                return (Strategy)new CopyQuotedStrategy(s);
            }
            return this.getLocaleSpecificStrategy(15, calendar);
        }
        Strategy strategy2;
        if (s.length() > 2) {
            strategy2 = FastDateParser.LITERAL_YEAR_STRATEGY;
        }
        else {
            strategy2 = FastDateParser.ABBREVIATED_YEAR_STRATEGY;
        }
        return strategy2;
    }
    
    private void init(final Calendar calendar) {
        final StringBuilder sb = new StringBuilder();
        final ArrayList<Strategy> list = new ArrayList<Strategy>();
        final Matcher matcher = FastDateParser.formatPattern.matcher(this.pattern);
        if (!matcher.lookingAt()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Illegal pattern character '");
            sb2.append(this.pattern.charAt(matcher.regionStart()));
            sb2.append("'");
            throw new IllegalArgumentException(sb2.toString());
        }
        this.currentFormatField = matcher.group();
        Strategy strategy = this.getStrategy(this.currentFormatField, calendar);
        while (true) {
            matcher.region(matcher.end(), matcher.regionEnd());
            if (!matcher.lookingAt()) {
                break;
            }
            final String group = matcher.group();
            this.nextStrategy = this.getStrategy(group, calendar);
            if (strategy.addRegex(this, sb)) {
                list.add(strategy);
            }
            this.currentFormatField = group;
            strategy = this.nextStrategy;
        }
        this.nextStrategy = null;
        if (matcher.regionStart() == matcher.regionEnd()) {
            if (strategy.addRegex(this, sb)) {
                list.add(strategy);
            }
            this.currentFormatField = null;
            this.strategies = list.toArray(new Strategy[list.size()]);
            this.parsePattern = Pattern.compile(sb.toString());
            return;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Failed to parse \"");
        sb3.append(this.pattern);
        sb3.append("\" ; gave up at index ");
        sb3.append(matcher.regionStart());
        throw new IllegalArgumentException(sb3.toString());
    }
    
    private static void insertValuesInMap(final Map<String, Integer> map, final String[] array) {
        if (array == null) {
            return;
        }
        for (int i = 0; i < array.length; ++i) {
            if (array[i] != null && array[i].length() > 0) {
                map.put(array[i], i);
            }
        }
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.init(Calendar.getInstance(this.timeZone, this.locale));
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof FastDateParser;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final FastDateParser fastDateParser = (FastDateParser)o;
        boolean b3 = b2;
        if (this.pattern.equals(fastDateParser.pattern)) {
            b3 = b2;
            if (this.timeZone.equals(fastDateParser.timeZone)) {
                b3 = b2;
                if (this.locale.equals(fastDateParser.locale)) {
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    int getFieldWidth() {
        return this.currentFormatField.length();
    }
    
    @Override
    public Locale getLocale() {
        return this.locale;
    }
    
    Pattern getParsePattern() {
        return this.parsePattern;
    }
    
    @Override
    public String getPattern() {
        return this.pattern;
    }
    
    @Override
    public TimeZone getTimeZone() {
        return this.timeZone;
    }
    
    @Override
    public int hashCode() {
        return this.pattern.hashCode() + (this.timeZone.hashCode() + this.locale.hashCode() * 13) * 13;
    }
    
    boolean isNextNumber() {
        final Strategy nextStrategy = this.nextStrategy;
        return nextStrategy != null && nextStrategy.isNumber();
    }
    
    @Override
    public Date parse(final String s) throws ParseException {
        final Date parse = this.parse(s, new ParsePosition(0));
        if (parse != null) {
            return parse;
        }
        if (this.locale.equals(FastDateParser.JAPANESE_IMPERIAL)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("(The ");
            sb.append(this.locale);
            sb.append(" locale does not support dates before 1868 AD)\nUnparseable date: \"");
            sb.append(s);
            sb.append("\" does not match ");
            sb.append(this.parsePattern.pattern());
            throw new ParseException(sb.toString(), 0);
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Unparseable date: \"");
        sb2.append(s);
        sb2.append("\" does not match ");
        sb2.append(this.parsePattern.pattern());
        throw new ParseException(sb2.toString(), 0);
    }
    
    @Override
    public Date parse(final String s, final ParsePosition parsePosition) {
        final int index = parsePosition.getIndex();
        final Matcher matcher = this.parsePattern.matcher(s.substring(index));
        if (!matcher.lookingAt()) {
            return null;
        }
        final Calendar instance = Calendar.getInstance(this.timeZone, this.locale);
        instance.clear();
        int n = 0;
        while (true) {
            final Strategy[] strategies = this.strategies;
            if (n >= strategies.length) {
                break;
            }
            final int group = n + 1;
            strategies[n].setCalendar(this, instance, matcher.group(group));
            n = group;
        }
        parsePosition.setIndex(index + matcher.end());
        return instance.getTime();
    }
    
    @Override
    public Object parseObject(final String s) throws ParseException {
        return this.parse(s);
    }
    
    @Override
    public Object parseObject(final String s, final ParsePosition parsePosition) {
        return this.parse(s, parsePosition);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("FastDateParser[");
        sb.append(this.pattern);
        sb.append(",");
        sb.append(this.locale);
        sb.append(",");
        sb.append(this.timeZone.getID());
        sb.append("]");
        return sb.toString();
    }
    
    private static class CopyQuotedStrategy extends Strategy
    {
        private final String formatField;
        
        CopyQuotedStrategy(final String formatField) {
            this.formatField = formatField;
        }
        
        @Override
        boolean addRegex(final FastDateParser fastDateParser, final StringBuilder sb) {
            FastDateParser.access$100(sb, this.formatField, true);
            return false;
        }
        
        @Override
        boolean isNumber() {
            char ch;
            if ((ch = this.formatField.charAt(0)) == '\'') {
                ch = this.formatField.charAt(1);
            }
            return Character.isDigit(ch);
        }
    }
    
    private static class NumberStrategy extends Strategy
    {
        private final int field;
        
        NumberStrategy(final int field) {
            this.field = field;
        }
        
        @Override
        boolean addRegex(final FastDateParser fastDateParser, final StringBuilder sb) {
            if (fastDateParser.isNextNumber()) {
                sb.append("(\\p{Nd}{");
                sb.append(fastDateParser.getFieldWidth());
                sb.append("}+)");
            }
            else {
                sb.append("(\\p{Nd}++)");
            }
            return true;
        }
        
        @Override
        boolean isNumber() {
            return true;
        }
        
        int modify(final int n) {
            return n;
        }
        
        @Override
        void setCalendar(final FastDateParser fastDateParser, final Calendar calendar, final String s) {
            calendar.set(this.field, this.modify(Integer.parseInt(s)));
        }
    }
    
    private abstract static class Strategy
    {
        abstract boolean addRegex(final FastDateParser p0, final StringBuilder p1);
        
        boolean isNumber() {
            return false;
        }
        
        void setCalendar(final FastDateParser fastDateParser, final Calendar calendar, final String s) {
        }
    }
    
    private static class TextStrategy extends Strategy
    {
        private final int field;
        private final Map<String, Integer> keyValues;
        
        TextStrategy(final int field, final Calendar calendar, final Locale locale) {
            this.field = field;
            this.keyValues = getDisplayNames(field, calendar, locale);
        }
        
        @Override
        boolean addRegex(final FastDateParser fastDateParser, final StringBuilder sb) {
            sb.append('(');
            final Iterator<String> iterator = this.keyValues.keySet().iterator();
            while (iterator.hasNext()) {
                FastDateParser.access$100(sb, iterator.next(), false);
                sb.append('|');
            }
            sb.setCharAt(sb.length() - 1, ')');
            return true;
        }
        
        @Override
        void setCalendar(final FastDateParser fastDateParser, final Calendar calendar, final String str) {
            final Integer n = this.keyValues.get(str);
            if (n == null) {
                final StringBuilder sb = new StringBuilder(str);
                sb.append(" not in (");
                final Iterator<String> iterator = this.keyValues.keySet().iterator();
                while (iterator.hasNext()) {
                    sb.append(iterator.next());
                    sb.append(' ');
                }
                sb.setCharAt(sb.length() - 1, ')');
                throw new IllegalArgumentException(sb.toString());
            }
            calendar.set(this.field, n);
        }
    }
    
    private static class TimeZoneStrategy extends Strategy
    {
        private static final int ID = 0;
        private static final int LONG_DST = 3;
        private static final int LONG_STD = 1;
        private static final int SHORT_DST = 4;
        private static final int SHORT_STD = 2;
        private final SortedMap<String, TimeZone> tzNames;
        private final String validTimeZoneChars;
        
        TimeZoneStrategy(final Locale locale) {
            this.tzNames = new TreeMap<String, TimeZone>(String.CASE_INSENSITIVE_ORDER);
            for (final String[] array : DateFormatSymbols.getInstance(locale).getZoneStrings()) {
                if (!array[0].startsWith("GMT")) {
                    final TimeZone timeZone = TimeZone.getTimeZone(array[0]);
                    if (!this.tzNames.containsKey(array[1])) {
                        this.tzNames.put(array[1], timeZone);
                    }
                    if (!this.tzNames.containsKey(array[2])) {
                        this.tzNames.put(array[2], timeZone);
                    }
                    if (timeZone.useDaylightTime()) {
                        if (!this.tzNames.containsKey(array[3])) {
                            this.tzNames.put(array[3], timeZone);
                        }
                        if (!this.tzNames.containsKey(array[4])) {
                            this.tzNames.put(array[4], timeZone);
                        }
                    }
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("(GMT[+\\-]\\d{0,1}\\d{2}|[+\\-]\\d{2}:?\\d{2}|");
            final Iterator<String> iterator = this.tzNames.keySet().iterator();
            while (iterator.hasNext()) {
                FastDateParser.access$100(sb, iterator.next(), false);
                sb.append('|');
            }
            sb.setCharAt(sb.length() - 1, ')');
            this.validTimeZoneChars = sb.toString();
        }
        
        @Override
        boolean addRegex(final FastDateParser fastDateParser, final StringBuilder sb) {
            sb.append(this.validTimeZoneChars);
            return true;
        }
        
        @Override
        void setCalendar(final FastDateParser fastDateParser, final Calendar calendar, final String str) {
            TimeZone timeZone;
            if (str.charAt(0) != '+' && str.charAt(0) != '-') {
                if (str.startsWith("GMT")) {
                    timeZone = TimeZone.getTimeZone(str);
                }
                else {
                    timeZone = this.tzNames.get(str);
                    if (timeZone == null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(" is not a supported timezone name");
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("GMT");
                sb2.append(str);
                timeZone = TimeZone.getTimeZone(sb2.toString());
            }
            calendar.setTimeZone(timeZone);
        }
    }
}
