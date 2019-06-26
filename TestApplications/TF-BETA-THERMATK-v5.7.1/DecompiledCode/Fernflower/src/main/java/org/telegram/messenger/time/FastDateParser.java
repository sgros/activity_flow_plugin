package org.telegram.messenger.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastDateParser implements DateParser, Serializable {
   private static final FastDateParser.Strategy ABBREVIATED_YEAR_STRATEGY = new FastDateParser.NumberStrategy(1) {
      void setCalendar(FastDateParser var1, Calendar var2, String var3) {
         int var4 = Integer.parseInt(var3);
         int var5 = var4;
         if (var4 < 100) {
            var5 = var1.adjustYear(var4);
         }

         var2.set(1, var5);
      }
   };
   private static final FastDateParser.Strategy DAY_OF_MONTH_STRATEGY = new FastDateParser.NumberStrategy(5);
   private static final FastDateParser.Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new FastDateParser.NumberStrategy(8);
   private static final FastDateParser.Strategy DAY_OF_YEAR_STRATEGY = new FastDateParser.NumberStrategy(6);
   private static final FastDateParser.Strategy HOUR_OF_DAY_STRATEGY = new FastDateParser.NumberStrategy(11);
   private static final FastDateParser.Strategy HOUR_STRATEGY = new FastDateParser.NumberStrategy(10);
   static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
   private static final FastDateParser.Strategy LITERAL_YEAR_STRATEGY = new FastDateParser.NumberStrategy(1);
   private static final FastDateParser.Strategy MILLISECOND_STRATEGY = new FastDateParser.NumberStrategy(14);
   private static final FastDateParser.Strategy MINUTE_STRATEGY = new FastDateParser.NumberStrategy(12);
   private static final FastDateParser.Strategy MODULO_HOUR_OF_DAY_STRATEGY = new FastDateParser.NumberStrategy(11) {
      int modify(int var1) {
         return var1 % 24;
      }
   };
   private static final FastDateParser.Strategy MODULO_HOUR_STRATEGY = new FastDateParser.NumberStrategy(10) {
      int modify(int var1) {
         return var1 % 12;
      }
   };
   private static final FastDateParser.Strategy NUMBER_MONTH_STRATEGY = new FastDateParser.NumberStrategy(2) {
      int modify(int var1) {
         return var1 - 1;
      }
   };
   private static final FastDateParser.Strategy SECOND_STRATEGY = new FastDateParser.NumberStrategy(13);
   private static final FastDateParser.Strategy WEEK_OF_MONTH_STRATEGY = new FastDateParser.NumberStrategy(4);
   private static final FastDateParser.Strategy WEEK_OF_YEAR_STRATEGY = new FastDateParser.NumberStrategy(3);
   private static final ConcurrentMap[] caches = new ConcurrentMap[17];
   private static final Pattern formatPattern = Pattern.compile("D+|E+|F+|G+|H+|K+|M+|L+|S+|W+|Z+|a+|d+|h+|k+|m+|s+|w+|y+|z+|''|'[^']++(''[^']*+)*+'|[^'A-Za-z]++");
   private static final long serialVersionUID = 2L;
   private final int century;
   private transient String currentFormatField;
   private final Locale locale;
   private transient FastDateParser.Strategy nextStrategy;
   private transient Pattern parsePattern;
   private final String pattern;
   private final int startYear;
   private transient FastDateParser.Strategy[] strategies;
   private final TimeZone timeZone;

   protected FastDateParser(String var1, TimeZone var2, Locale var3) {
      this(var1, var2, var3, (Date)null);
   }

   protected FastDateParser(String var1, TimeZone var2, Locale var3, Date var4) {
      this.pattern = var1;
      this.timeZone = var2;
      this.locale = var3;
      Calendar var6 = Calendar.getInstance(var2, var3);
      int var5;
      if (var4 != null) {
         var6.setTime(var4);
         var5 = var6.get(1);
      } else if (var3.equals(JAPANESE_IMPERIAL)) {
         var5 = 0;
      } else {
         var6.setTime(new Date());
         var5 = var6.get(1) - 80;
      }

      this.century = var5 / 100 * 100;
      this.startYear = var5 - this.century;
      this.init(var6);
   }

   // $FF: synthetic method
   static StringBuilder access$100(StringBuilder var0, String var1, boolean var2) {
      escapeRegex(var0, var1, var2);
      return var0;
   }

   private int adjustYear(int var1) {
      int var2 = this.century + var1;
      if (var1 >= this.startYear) {
         var1 = var2;
      } else {
         var1 = var2 + 100;
      }

      return var1;
   }

   private static StringBuilder escapeRegex(StringBuilder var0, String var1, boolean var2) {
      var0.append("\\Q");

      int var5;
      for(int var3 = 0; var3 < var1.length(); var3 = var5 + 1) {
         char var4 = var1.charAt(var3);
         if (var4 != '\'') {
            if (var4 != '\\') {
               var5 = var3;
            } else {
               ++var3;
               if (var3 == var1.length()) {
                  var5 = var3;
               } else {
                  var0.append(var4);
                  char var6 = var1.charAt(var3);
                  var5 = var3;
                  var4 = var6;
                  if (var6 == 'E') {
                     var0.append("E\\\\E\\");
                     byte var8 = 81;
                     var5 = var3;
                     var4 = (char)var8;
                  }
               }
            }
         } else {
            var5 = var3;
            if (var2) {
               var5 = var3 + 1;
               if (var5 == var1.length()) {
                  return var0;
               }

               char var7 = var1.charAt(var5);
               var4 = var7;
            }
         }

         var0.append(var4);
      }

      var0.append("\\E");
      return var0;
   }

   private static ConcurrentMap getCache(int var0) {
      ConcurrentMap[] var1 = caches;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label130: {
         label129: {
            ConcurrentMap[] var2;
            ConcurrentHashMap var3;
            try {
               if (caches[var0] != null) {
                  break label129;
               }

               var2 = caches;
               var3 = new ConcurrentHashMap(3);
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label130;
            }

            var2[var0] = var3;
         }

         label123:
         try {
            ConcurrentMap var17 = caches[var0];
            return var17;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label123;
         }
      }

      while(true) {
         Throwable var16 = var10000;

         try {
            throw var16;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   private static String[] getDisplayNameArray(int var0, boolean var1, Locale var2) {
      DateFormatSymbols var3 = new DateFormatSymbols(var2);
      if (var0 != 0) {
         String[] var4;
         if (var0 != 2) {
            if (var0 != 7) {
               return var0 != 9 ? null : var3.getAmPmStrings();
            } else {
               if (var1) {
                  var4 = var3.getWeekdays();
               } else {
                  var4 = var3.getShortWeekdays();
               }

               return var4;
            }
         } else {
            if (var1) {
               var4 = var3.getMonths();
            } else {
               var4 = var3.getShortMonths();
            }

            return var4;
         }
      } else {
         return var3.getEras();
      }
   }

   private static Map getDisplayNames(int var0, Calendar var1, Locale var2) {
      return getDisplayNames(var0, var2);
   }

   private static Map getDisplayNames(int var0, Locale var1) {
      HashMap var2 = new HashMap();
      insertValuesInMap(var2, getDisplayNameArray(var0, false, var1));
      insertValuesInMap(var2, getDisplayNameArray(var0, true, var1));
      HashMap var3 = var2;
      if (var2.isEmpty()) {
         var3 = null;
      }

      return var3;
   }

   private FastDateParser.Strategy getLocaleSpecificStrategy(int var1, Calendar var2) {
      ConcurrentMap var3 = getCache(var1);
      FastDateParser.Strategy var4 = (FastDateParser.Strategy)var3.get(this.locale);
      Object var5 = var4;
      if (var4 == null) {
         Object var6;
         if (var1 == 15) {
            var6 = new FastDateParser.TimeZoneStrategy(this.locale);
         } else {
            var6 = new FastDateParser.TextStrategy(var1, var2, this.locale);
         }

         var4 = (FastDateParser.Strategy)var3.putIfAbsent(this.locale, var6);
         var5 = var6;
         if (var4 != null) {
            return var4;
         }
      }

      return (FastDateParser.Strategy)var5;
   }

   private FastDateParser.Strategy getStrategy(String var1, Calendar var2) {
      char var3 = var1.charAt(0);
      FastDateParser.Strategy var4;
      if (var3 == 'y') {
         if (var1.length() > 2) {
            var4 = LITERAL_YEAR_STRATEGY;
         } else {
            var4 = ABBREVIATED_YEAR_STRATEGY;
         }

         return var4;
      } else {
         if (var3 != 'z') {
            switch(var3) {
            case '\'':
               if (var1.length() > 2) {
                  return new FastDateParser.CopyQuotedStrategy(var1.substring(1, var1.length() - 1));
               }

               return new FastDateParser.CopyQuotedStrategy(var1);
            case 'S':
               return MILLISECOND_STRATEGY;
            case 'W':
               return WEEK_OF_MONTH_STRATEGY;
            case 'Z':
               break;
            case 'a':
               return this.getLocaleSpecificStrategy(9, var2);
            case 'd':
               return DAY_OF_MONTH_STRATEGY;
            case 'h':
               return MODULO_HOUR_STRATEGY;
            case 'k':
               return HOUR_OF_DAY_STRATEGY;
            case 'm':
               return MINUTE_STRATEGY;
            case 's':
               return SECOND_STRATEGY;
            case 'w':
               return WEEK_OF_YEAR_STRATEGY;
            default:
               switch(var3) {
               case 'D':
                  return DAY_OF_YEAR_STRATEGY;
               case 'E':
                  return this.getLocaleSpecificStrategy(7, var2);
               case 'F':
                  return DAY_OF_WEEK_IN_MONTH_STRATEGY;
               case 'G':
                  return this.getLocaleSpecificStrategy(0, var2);
               case 'H':
                  return MODULO_HOUR_OF_DAY_STRATEGY;
               default:
                  switch(var3) {
                  case 'K':
                     return HOUR_STRATEGY;
                  case 'L':
                  case 'M':
                     if (var1.length() >= 3) {
                        var4 = this.getLocaleSpecificStrategy(2, var2);
                     } else {
                        var4 = NUMBER_MONTH_STRATEGY;
                     }

                     return var4;
                  default:
                     return new FastDateParser.CopyQuotedStrategy(var1);
                  }
               }
            }
         }

         return this.getLocaleSpecificStrategy(15, var2);
      }
   }

   private void init(Calendar var1) {
      StringBuilder var2 = new StringBuilder();
      ArrayList var3 = new ArrayList();
      Matcher var4 = formatPattern.matcher(this.pattern);
      StringBuilder var7;
      if (var4.lookingAt()) {
         this.currentFormatField = var4.group();
         FastDateParser.Strategy var5 = this.getStrategy(this.currentFormatField, var1);

         while(true) {
            var4.region(var4.end(), var4.regionEnd());
            if (!var4.lookingAt()) {
               this.nextStrategy = null;
               if (var4.regionStart() == var4.regionEnd()) {
                  if (var5.addRegex(this, var2)) {
                     var3.add(var5);
                  }

                  this.currentFormatField = null;
                  this.strategies = (FastDateParser.Strategy[])var3.toArray(new FastDateParser.Strategy[var3.size()]);
                  this.parsePattern = Pattern.compile(var2.toString());
                  return;
               }

               var7 = new StringBuilder();
               var7.append("Failed to parse \"");
               var7.append(this.pattern);
               var7.append("\" ; gave up at index ");
               var7.append(var4.regionStart());
               throw new IllegalArgumentException(var7.toString());
            }

            String var6 = var4.group();
            this.nextStrategy = this.getStrategy(var6, var1);
            if (var5.addRegex(this, var2)) {
               var3.add(var5);
            }

            this.currentFormatField = var6;
            var5 = this.nextStrategy;
         }
      } else {
         var7 = new StringBuilder();
         var7.append("Illegal pattern character '");
         var7.append(this.pattern.charAt(var4.regionStart()));
         var7.append("'");
         throw new IllegalArgumentException(var7.toString());
      }
   }

   private static void insertValuesInMap(Map var0, String[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] != null && var1[var2].length() > 0) {
               var0.put(var1[var2], var2);
            }
         }

      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.init(Calendar.getInstance(this.timeZone, this.locale));
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof FastDateParser;
      boolean var3 = false;
      if (!var2) {
         return false;
      } else {
         FastDateParser var4 = (FastDateParser)var1;
         var2 = var3;
         if (this.pattern.equals(var4.pattern)) {
            var2 = var3;
            if (this.timeZone.equals(var4.timeZone)) {
               var2 = var3;
               if (this.locale.equals(var4.locale)) {
                  var2 = true;
               }
            }
         }

         return var2;
      }
   }

   int getFieldWidth() {
      return this.currentFormatField.length();
   }

   public Locale getLocale() {
      return this.locale;
   }

   Pattern getParsePattern() {
      return this.parsePattern;
   }

   public String getPattern() {
      return this.pattern;
   }

   public TimeZone getTimeZone() {
      return this.timeZone;
   }

   public int hashCode() {
      return this.pattern.hashCode() + (this.timeZone.hashCode() + this.locale.hashCode() * 13) * 13;
   }

   boolean isNextNumber() {
      FastDateParser.Strategy var1 = this.nextStrategy;
      boolean var2;
      if (var1 != null && var1.isNumber()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public Date parse(String var1) throws ParseException {
      Date var2 = this.parse(var1, new ParsePosition(0));
      if (var2 == null) {
         StringBuilder var3;
         if (this.locale.equals(JAPANESE_IMPERIAL)) {
            var3 = new StringBuilder();
            var3.append("(The ");
            var3.append(this.locale);
            var3.append(" locale does not support dates before 1868 AD)\nUnparseable date: \"");
            var3.append(var1);
            var3.append("\" does not match ");
            var3.append(this.parsePattern.pattern());
            throw new ParseException(var3.toString(), 0);
         } else {
            var3 = new StringBuilder();
            var3.append("Unparseable date: \"");
            var3.append(var1);
            var3.append("\" does not match ");
            var3.append(this.parsePattern.pattern());
            throw new ParseException(var3.toString(), 0);
         }
      } else {
         return var2;
      }
   }

   public Date parse(String var1, ParsePosition var2) {
      int var3 = var2.getIndex();
      Matcher var4 = this.parsePattern.matcher(var1.substring(var3));
      if (!var4.lookingAt()) {
         return null;
      } else {
         Calendar var8 = Calendar.getInstance(this.timeZone, this.locale);
         var8.clear();
         int var5 = 0;

         while(true) {
            FastDateParser.Strategy[] var6 = this.strategies;
            if (var5 >= var6.length) {
               var2.setIndex(var3 + var4.end());
               return var8.getTime();
            }

            int var7 = var5 + 1;
            var6[var5].setCalendar(this, var8, var4.group(var7));
            var5 = var7;
         }
      }
   }

   public Object parseObject(String var1) throws ParseException {
      return this.parse(var1);
   }

   public Object parseObject(String var1, ParsePosition var2) {
      return this.parse(var1, var2);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("FastDateParser[");
      var1.append(this.pattern);
      var1.append(",");
      var1.append(this.locale);
      var1.append(",");
      var1.append(this.timeZone.getID());
      var1.append("]");
      return var1.toString();
   }

   private static class CopyQuotedStrategy extends FastDateParser.Strategy {
      private final String formatField;

      CopyQuotedStrategy(String var1) {
         super(null);
         this.formatField = var1;
      }

      boolean addRegex(FastDateParser var1, StringBuilder var2) {
         FastDateParser.access$100(var2, this.formatField, true);
         return false;
      }

      boolean isNumber() {
         char var1 = this.formatField.charAt(0);
         char var2 = var1;
         if (var1 == '\'') {
            var1 = this.formatField.charAt(1);
            var2 = var1;
         }

         return Character.isDigit(var2);
      }
   }

   private static class NumberStrategy extends FastDateParser.Strategy {
      private final int field;

      NumberStrategy(int var1) {
         super(null);
         this.field = var1;
      }

      boolean addRegex(FastDateParser var1, StringBuilder var2) {
         if (var1.isNextNumber()) {
            var2.append("(\\p{Nd}{");
            var2.append(var1.getFieldWidth());
            var2.append("}+)");
         } else {
            var2.append("(\\p{Nd}++)");
         }

         return true;
      }

      boolean isNumber() {
         return true;
      }

      int modify(int var1) {
         return var1;
      }

      void setCalendar(FastDateParser var1, Calendar var2, String var3) {
         var2.set(this.field, this.modify(Integer.parseInt(var3)));
      }
   }

   private abstract static class Strategy {
      private Strategy() {
      }

      // $FF: synthetic method
      Strategy(Object var1) {
         this();
      }

      abstract boolean addRegex(FastDateParser var1, StringBuilder var2);

      boolean isNumber() {
         return false;
      }

      void setCalendar(FastDateParser var1, Calendar var2, String var3) {
      }
   }

   private static class TextStrategy extends FastDateParser.Strategy {
      private final int field;
      private final Map keyValues;

      TextStrategy(int var1, Calendar var2, Locale var3) {
         super(null);
         this.field = var1;
         this.keyValues = FastDateParser.getDisplayNames(var1, var2, var3);
      }

      boolean addRegex(FastDateParser var1, StringBuilder var2) {
         var2.append('(');
         Iterator var3 = this.keyValues.keySet().iterator();

         while(var3.hasNext()) {
            FastDateParser.access$100(var2, (String)var3.next(), false);
            var2.append('|');
         }

         var2.setCharAt(var2.length() - 1, ')');
         return true;
      }

      void setCalendar(FastDateParser var1, Calendar var2, String var3) {
         Integer var4 = (Integer)this.keyValues.get(var3);
         if (var4 != null) {
            var2.set(this.field, var4);
         } else {
            StringBuilder var5 = new StringBuilder(var3);
            var5.append(" not in (");
            Iterator var6 = this.keyValues.keySet().iterator();

            while(var6.hasNext()) {
               var5.append((String)var6.next());
               var5.append(' ');
            }

            var5.setCharAt(var5.length() - 1, ')');
            throw new IllegalArgumentException(var5.toString());
         }
      }
   }

   private static class TimeZoneStrategy extends FastDateParser.Strategy {
      private static final int ID = 0;
      private static final int LONG_DST = 3;
      private static final int LONG_STD = 1;
      private static final int SHORT_DST = 4;
      private static final int SHORT_STD = 2;
      private final SortedMap tzNames;
      private final String validTimeZoneChars;

      TimeZoneStrategy(Locale var1) {
         super(null);
         this.tzNames = new TreeMap(String.CASE_INSENSITIVE_ORDER);
         String[][] var2 = DateFormatSymbols.getInstance(var1).getZoneStrings();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String[] var6 = var2[var4];
            if (!var6[0].startsWith("GMT")) {
               TimeZone var5 = TimeZone.getTimeZone(var6[0]);
               if (!this.tzNames.containsKey(var6[1])) {
                  this.tzNames.put(var6[1], var5);
               }

               if (!this.tzNames.containsKey(var6[2])) {
                  this.tzNames.put(var6[2], var5);
               }

               if (var5.useDaylightTime()) {
                  if (!this.tzNames.containsKey(var6[3])) {
                     this.tzNames.put(var6[3], var5);
                  }

                  if (!this.tzNames.containsKey(var6[4])) {
                     this.tzNames.put(var6[4], var5);
                  }
               }
            }
         }

         StringBuilder var8 = new StringBuilder();
         var8.append("(GMT[+\\-]\\d{0,1}\\d{2}|[+\\-]\\d{2}:?\\d{2}|");
         Iterator var7 = this.tzNames.keySet().iterator();

         while(var7.hasNext()) {
            FastDateParser.access$100(var8, (String)var7.next(), false);
            var8.append('|');
         }

         var8.setCharAt(var8.length() - 1, ')');
         this.validTimeZoneChars = var8.toString();
      }

      boolean addRegex(FastDateParser var1, StringBuilder var2) {
         var2.append(this.validTimeZoneChars);
         return true;
      }

      void setCalendar(FastDateParser var1, Calendar var2, String var3) {
         StringBuilder var4;
         TimeZone var5;
         if (var3.charAt(0) != '+' && var3.charAt(0) != '-') {
            if (var3.startsWith("GMT")) {
               var5 = TimeZone.getTimeZone(var3);
            } else {
               var5 = (TimeZone)this.tzNames.get(var3);
               if (var5 == null) {
                  var4 = new StringBuilder();
                  var4.append(var3);
                  var4.append(" is not a supported timezone name");
                  throw new IllegalArgumentException(var4.toString());
               }
            }
         } else {
            var4 = new StringBuilder();
            var4.append("GMT");
            var4.append(var3);
            var5 = TimeZone.getTimeZone(var4.toString());
         }

         var2.setTimeZone(var5);
      }
   }
}
