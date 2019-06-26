package org.telegram.messenger.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FastDatePrinter implements DatePrinter, Serializable {
   public static final int FULL = 0;
   public static final int LONG = 1;
   public static final int MEDIUM = 2;
   public static final int SHORT = 3;
   private static final ConcurrentMap cTimeZoneDisplayCache = new ConcurrentHashMap(7);
   private static final long serialVersionUID = 1L;
   private final Locale mLocale;
   private transient int mMaxLengthEstimate;
   private final String mPattern;
   private transient FastDatePrinter.Rule[] mRules;
   private final TimeZone mTimeZone;

   protected FastDatePrinter(String var1, TimeZone var2, Locale var3) {
      this.mPattern = var1;
      this.mTimeZone = var2;
      this.mLocale = var3;
      this.init();
   }

   private String applyRulesToString(Calendar var1) {
      return this.applyRules(var1, new StringBuffer(this.mMaxLengthEstimate)).toString();
   }

   static String getTimeZoneDisplay(TimeZone var0, boolean var1, int var2, Locale var3) {
      FastDatePrinter.TimeZoneDisplayKey var4 = new FastDatePrinter.TimeZoneDisplayKey(var0, var1, var2, var3);
      String var5 = (String)cTimeZoneDisplayCache.get(var4);
      String var6 = var5;
      if (var5 == null) {
         var6 = var0.getDisplayName(var1, var2, var3);
         String var7 = (String)cTimeZoneDisplayCache.putIfAbsent(var4, var6);
         if (var7 != null) {
            var6 = var7;
         }
      }

      return var6;
   }

   private void init() {
      List var1 = this.parsePattern();
      this.mRules = (FastDatePrinter.Rule[])var1.toArray(new FastDatePrinter.Rule[var1.size()]);
      int var2 = this.mRules.length;
      int var3 = 0;

      while(true) {
         --var2;
         if (var2 < 0) {
            this.mMaxLengthEstimate = var3;
            return;
         }

         var3 += this.mRules[var2].estimateLength();
      }
   }

   private GregorianCalendar newCalendar() {
      return new GregorianCalendar(this.mTimeZone, this.mLocale);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.init();
   }

   protected StringBuffer applyRules(Calendar var1, StringBuffer var2) {
      FastDatePrinter.Rule[] var3 = this.mRules;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         var3[var5].appendTo(var2, var1);
      }

      return var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof FastDatePrinter;
      boolean var3 = false;
      if (!var2) {
         return false;
      } else {
         FastDatePrinter var4 = (FastDatePrinter)var1;
         var2 = var3;
         if (this.mPattern.equals(var4.mPattern)) {
            var2 = var3;
            if (this.mTimeZone.equals(var4.mTimeZone)) {
               var2 = var3;
               if (this.mLocale.equals(var4.mLocale)) {
                  var2 = true;
               }
            }
         }

         return var2;
      }
   }

   public String format(long var1) {
      GregorianCalendar var3 = this.newCalendar();
      var3.setTimeInMillis(var1);
      return this.applyRulesToString(var3);
   }

   public String format(Calendar var1) {
      return this.format(var1, new StringBuffer(this.mMaxLengthEstimate)).toString();
   }

   public String format(Date var1) {
      GregorianCalendar var2 = this.newCalendar();
      var2.setTime(var1);
      return this.applyRulesToString(var2);
   }

   public StringBuffer format(long var1, StringBuffer var3) {
      return this.format(new Date(var1), var3);
   }

   public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      if (var1 instanceof Date) {
         return this.format((Date)var1, var2);
      } else if (var1 instanceof Calendar) {
         return this.format((Calendar)var1, var2);
      } else if (var1 instanceof Long) {
         return this.format((Long)var1, var2);
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Unknown class: ");
         String var4;
         if (var1 == null) {
            var4 = "<null>";
         } else {
            var4 = var1.getClass().getName();
         }

         var5.append(var4);
         throw new IllegalArgumentException(var5.toString());
      }
   }

   public StringBuffer format(Calendar var1, StringBuffer var2) {
      return this.applyRules(var1, var2);
   }

   public StringBuffer format(Date var1, StringBuffer var2) {
      GregorianCalendar var3 = this.newCalendar();
      var3.setTime(var1);
      return this.applyRules(var3, var2);
   }

   public Locale getLocale() {
      return this.mLocale;
   }

   public int getMaxLengthEstimate() {
      return this.mMaxLengthEstimate;
   }

   public String getPattern() {
      return this.mPattern;
   }

   public TimeZone getTimeZone() {
      return this.mTimeZone;
   }

   public int hashCode() {
      return this.mPattern.hashCode() + (this.mTimeZone.hashCode() + this.mLocale.hashCode() * 13) * 13;
   }

   protected List parsePattern() {
      DateFormatSymbols var1 = new DateFormatSymbols(this.mLocale);
      ArrayList var2 = new ArrayList();
      String[] var3 = var1.getEras();
      String[] var4 = var1.getMonths();
      String[] var5 = var1.getShortMonths();
      String[] var6 = var1.getWeekdays();
      String[] var7 = var1.getShortWeekdays();
      String[] var8 = var1.getAmPmStrings();
      int var9 = this.mPattern.length();
      int[] var10 = new int[1];

      int var12;
      for(int var11 = 0; var11 < var9; var11 = var12 + 1) {
         var10[0] = var11;
         String var15 = this.parseToken(this.mPattern, var10);
         var12 = var10[0];
         var11 = var15.length();
         if (var11 == 0) {
            break;
         }

         char var13 = var15.charAt(0);
         byte var14 = 4;
         Object var16;
         if (var13 != 'y') {
            if (var13 != 'z') {
               switch(var13) {
               case '\'':
                  var15 = var15.substring(1);
                  if (var15.length() == 1) {
                     var16 = new FastDatePrinter.CharacterLiteral(var15.charAt(0));
                  } else {
                     var16 = new FastDatePrinter.StringLiteral(var15);
                  }
                  break;
               case 'S':
                  var16 = this.selectNumberRule(14, var11);
                  break;
               case 'W':
                  var16 = this.selectNumberRule(4, var11);
                  break;
               case 'Z':
                  if (var11 == 1) {
                     var16 = FastDatePrinter.TimeZoneNumberRule.INSTANCE_NO_COLON;
                  } else {
                     var16 = FastDatePrinter.TimeZoneNumberRule.INSTANCE_COLON;
                  }
                  break;
               case 'a':
                  var16 = new FastDatePrinter.TextField(9, var8);
                  break;
               case 'd':
                  var16 = this.selectNumberRule(5, var11);
                  break;
               case 'h':
                  var16 = new FastDatePrinter.TwelveHourField(this.selectNumberRule(10, var11));
                  break;
               case 'k':
                  var16 = new FastDatePrinter.TwentyFourHourField(this.selectNumberRule(11, var11));
                  break;
               case 'm':
                  var16 = this.selectNumberRule(12, var11);
                  break;
               case 's':
                  var16 = this.selectNumberRule(13, var11);
                  break;
               case 'w':
                  var16 = this.selectNumberRule(3, var11);
                  break;
               default:
                  switch(var13) {
                  case 'D':
                     var16 = this.selectNumberRule(6, var11);
                     break;
                  case 'E':
                     String[] var18;
                     if (var11 < 4) {
                        var18 = var7;
                     } else {
                        var18 = var6;
                     }

                     var16 = new FastDatePrinter.TextField(7, var18);
                     break;
                  case 'F':
                     var16 = this.selectNumberRule(8, var11);
                     break;
                  case 'G':
                     var16 = new FastDatePrinter.TextField(0, var3);
                     break;
                  case 'H':
                     var16 = this.selectNumberRule(11, var11);
                     break;
                  default:
                     switch(var13) {
                     case 'K':
                        var16 = this.selectNumberRule(10, var11);
                        break;
                     case 'L':
                        if (var11 >= 4) {
                           var16 = new FastDatePrinter.TextField(2, var4);
                        } else if (var11 == 3) {
                           var16 = new FastDatePrinter.TextField(2, var5);
                        } else if (var11 == 2) {
                           var16 = FastDatePrinter.TwoDigitMonthField.INSTANCE;
                        } else {
                           var16 = FastDatePrinter.UnpaddedMonthField.INSTANCE;
                        }
                        break;
                     case 'M':
                        if (var11 >= 4) {
                           var16 = new FastDatePrinter.TextField(2, var4);
                        } else if (var11 == 3) {
                           var16 = new FastDatePrinter.TextField(2, var5);
                        } else if (var11 == 2) {
                           var16 = FastDatePrinter.TwoDigitMonthField.INSTANCE;
                        } else {
                           var16 = FastDatePrinter.UnpaddedMonthField.INSTANCE;
                        }
                        break;
                     default:
                        StringBuilder var17 = new StringBuilder();
                        var17.append("Illegal pattern component: ");
                        var17.append(var15);
                        throw new IllegalArgumentException(var17.toString());
                     }
                  }
               }
            } else if (var11 >= 4) {
               var16 = new FastDatePrinter.TimeZoneNameRule(this.mTimeZone, this.mLocale, 1);
            } else {
               var16 = new FastDatePrinter.TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
            }
         } else if (var11 == 2) {
            var16 = FastDatePrinter.TwoDigitYearField.INSTANCE;
         } else {
            if (var11 < 4) {
               var11 = var14;
            }

            var16 = this.selectNumberRule(1, var11);
         }

         var2.add(var16);
      }

      return var2;
   }

   protected String parseToken(String var1, int[] var2) {
      StringBuilder var3 = new StringBuilder();
      int var4 = var2[0];
      int var5 = var1.length();
      char var6 = var1.charAt(var4);
      int var8;
      if ((var6 < 'A' || var6 > 'Z') && (var6 < 'a' || var6 > 'z')) {
         var3.append('\'');
         boolean var9 = false;

         while(true) {
            var8 = var4;
            if (var4 >= var5) {
               break;
            }

            var6 = var1.charAt(var4);
            if (var6 == '\'') {
               var8 = var4 + 1;
               if (var8 < var5 && var1.charAt(var8) == '\'') {
                  var3.append(var6);
                  var4 = var8;
               } else {
                  var9 ^= true;
               }
            } else {
               if (!var9 && (var6 >= 'A' && var6 <= 'Z' || var6 >= 'a' && var6 <= 'z')) {
                  var8 = var4 - 1;
                  break;
               }

               var3.append(var6);
            }

            ++var4;
         }
      } else {
         var3.append(var6);

         while(true) {
            int var7 = var4 + 1;
            var8 = var4;
            if (var7 >= var5) {
               break;
            }

            var8 = var4;
            if (var1.charAt(var7) != var6) {
               break;
            }

            var3.append(var6);
            var4 = var7;
         }
      }

      var2[0] = var8;
      return var3.toString();
   }

   protected FastDatePrinter.NumberRule selectNumberRule(int var1, int var2) {
      if (var2 != 1) {
         return (FastDatePrinter.NumberRule)(var2 != 2 ? new FastDatePrinter.PaddedNumberField(var1, var2) : new FastDatePrinter.TwoDigitNumberField(var1));
      } else {
         return new FastDatePrinter.UnpaddedNumberField(var1);
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("FastDatePrinter[");
      var1.append(this.mPattern);
      var1.append(",");
      var1.append(this.mLocale);
      var1.append(",");
      var1.append(this.mTimeZone.getID());
      var1.append("]");
      return var1.toString();
   }

   private static class CharacterLiteral implements FastDatePrinter.Rule {
      private final char mValue;

      CharacterLiteral(char var1) {
         this.mValue = (char)var1;
      }

      public void appendTo(StringBuffer var1, Calendar var2) {
         var1.append(this.mValue);
      }

      public int estimateLength() {
         return 1;
      }
   }

   private interface NumberRule extends FastDatePrinter.Rule {
      void appendTo(StringBuffer var1, int var2);
   }

   private static class PaddedNumberField implements FastDatePrinter.NumberRule {
      private final int mField;
      private final int mSize;

      PaddedNumberField(int var1, int var2) {
         if (var2 >= 3) {
            this.mField = var1;
            this.mSize = var2;
         } else {
            throw new IllegalArgumentException();
         }
      }

      public final void appendTo(StringBuffer var1, int var2) {
         int var3;
         if (var2 < 100) {
            var3 = this.mSize;

            while(true) {
               --var3;
               if (var3 < 2) {
                  var1.append((char)(var2 / 10 + 48));
                  var1.append((char)(var2 % 10 + 48));
                  break;
               }

               var1.append('0');
            }
         } else {
            if (var2 < 1000) {
               var3 = 3;
            } else {
               var3 = Integer.toString(var2).length();
            }

            int var4 = this.mSize;

            while(true) {
               --var4;
               if (var4 < var3) {
                  var1.append(Integer.toString(var2));
                  break;
               }

               var1.append('0');
            }
         }

      }

      public void appendTo(StringBuffer var1, Calendar var2) {
         this.appendTo(var1, var2.get(this.mField));
      }

      public int estimateLength() {
         return 4;
      }
   }

   private interface Rule {
      void appendTo(StringBuffer var1, Calendar var2);

      int estimateLength();
   }

   private static class StringLiteral implements FastDatePrinter.Rule {
      private final String mValue;

      StringLiteral(String var1) {
         this.mValue = var1;
      }

      public void appendTo(StringBuffer var1, Calendar var2) {
         var1.append(this.mValue);
      }

      public int estimateLength() {
         return this.mValue.length();
      }
   }

   private static class TextField implements FastDatePrinter.Rule {
      private final int mField;
      private final String[] mValues;

      TextField(int var1, String[] var2) {
         this.mField = var1;
         this.mValues = var2;
      }

      public void appendTo(StringBuffer var1, Calendar var2) {
         var1.append(this.mValues[var2.get(this.mField)]);
      }

      public int estimateLength() {
         int var1 = this.mValues.length;
         int var2 = 0;

         while(true) {
            int var3 = var1 - 1;
            if (var3 < 0) {
               return var2;
            }

            int var4 = this.mValues[var3].length();
            var1 = var3;
            if (var4 > var2) {
               var2 = var4;
               var1 = var3;
            }
         }
      }
   }

   private static class TimeZoneDisplayKey {
      private final Locale mLocale;
      private final int mStyle;
      private final TimeZone mTimeZone;

      TimeZoneDisplayKey(TimeZone var1, boolean var2, int var3, Locale var4) {
         this.mTimeZone = var1;
         if (var2) {
            this.mStyle = Integer.MIN_VALUE | var3;
         } else {
            this.mStyle = var3;
         }

         this.mLocale = var4;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof FastDatePrinter.TimeZoneDisplayKey)) {
            return false;
         } else {
            FastDatePrinter.TimeZoneDisplayKey var3 = (FastDatePrinter.TimeZoneDisplayKey)var1;
            if (!this.mTimeZone.equals(var3.mTimeZone) || this.mStyle != var3.mStyle || !this.mLocale.equals(var3.mLocale)) {
               var2 = false;
            }

            return var2;
         }
      }

      public int hashCode() {
         return (this.mStyle * 31 + this.mLocale.hashCode()) * 31 + this.mTimeZone.hashCode();
      }
   }

   private static class TimeZoneNameRule implements FastDatePrinter.Rule {
      private final String mDaylight;
      private final Locale mLocale;
      private final String mStandard;
      private final int mStyle;

      TimeZoneNameRule(TimeZone var1, Locale var2, int var3) {
         this.mLocale = var2;
         this.mStyle = var3;
         this.mStandard = FastDatePrinter.getTimeZoneDisplay(var1, false, var3, var2);
         this.mDaylight = FastDatePrinter.getTimeZoneDisplay(var1, true, var3, var2);
      }

      public void appendTo(StringBuffer var1, Calendar var2) {
         TimeZone var3 = var2.getTimeZone();
         if (var3.useDaylightTime() && var2.get(16) != 0) {
            var1.append(FastDatePrinter.getTimeZoneDisplay(var3, true, this.mStyle, this.mLocale));
         } else {
            var1.append(FastDatePrinter.getTimeZoneDisplay(var3, false, this.mStyle, this.mLocale));
         }

      }

      public int estimateLength() {
         return Math.max(this.mStandard.length(), this.mDaylight.length());
      }
   }

   private static class TimeZoneNumberRule implements FastDatePrinter.Rule {
      static final FastDatePrinter.TimeZoneNumberRule INSTANCE_COLON = new FastDatePrinter.TimeZoneNumberRule(true);
      static final FastDatePrinter.TimeZoneNumberRule INSTANCE_NO_COLON = new FastDatePrinter.TimeZoneNumberRule(false);
      final boolean mColon;

      TimeZoneNumberRule(boolean var1) {
         this.mColon = var1;
      }

      public void appendTo(StringBuffer var1, Calendar var2) {
         int var3 = var2.get(15) + var2.get(16);
         if (var3 < 0) {
            var1.append('-');
            var3 = -var3;
         } else {
            var1.append('+');
         }

         int var4 = var3 / 3600000;
         var1.append((char)(var4 / 10 + 48));
         var1.append((char)(var4 % 10 + 48));
         if (this.mColon) {
            var1.append(':');
         }

         var3 = var3 / '\uea60' - var4 * 60;
         var1.append((char)(var3 / 10 + 48));
         var1.append((char)(var3 % 10 + 48));
      }

      public int estimateLength() {
         return 5;
      }
   }

   private static class TwelveHourField implements FastDatePrinter.NumberRule {
      private final FastDatePrinter.NumberRule mRule;

      TwelveHourField(FastDatePrinter.NumberRule var1) {
         this.mRule = var1;
      }

      public void appendTo(StringBuffer var1, int var2) {
         this.mRule.appendTo(var1, var2);
      }

      public void appendTo(StringBuffer var1, Calendar var2) {
         int var3 = var2.get(10);
         int var4 = var3;
         if (var3 == 0) {
            var4 = var2.getLeastMaximum(10) + 1;
         }

         this.mRule.appendTo(var1, var4);
      }

      public int estimateLength() {
         return this.mRule.estimateLength();
      }
   }

   private static class TwentyFourHourField implements FastDatePrinter.NumberRule {
      private final FastDatePrinter.NumberRule mRule;

      TwentyFourHourField(FastDatePrinter.NumberRule var1) {
         this.mRule = var1;
      }

      public void appendTo(StringBuffer var1, int var2) {
         this.mRule.appendTo(var1, var2);
      }

      public void appendTo(StringBuffer var1, Calendar var2) {
         int var3 = var2.get(11);
         int var4 = var3;
         if (var3 == 0) {
            var4 = var2.getMaximum(11) + 1;
         }

         this.mRule.appendTo(var1, var4);
      }

      public int estimateLength() {
         return this.mRule.estimateLength();
      }
   }

   private static class TwoDigitMonthField implements FastDatePrinter.NumberRule {
      static final FastDatePrinter.TwoDigitMonthField INSTANCE = new FastDatePrinter.TwoDigitMonthField();

      TwoDigitMonthField() {
      }

      public final void appendTo(StringBuffer var1, int var2) {
         var1.append((char)(var2 / 10 + 48));
         var1.append((char)(var2 % 10 + 48));
      }

      public void appendTo(StringBuffer var1, Calendar var2) {
         this.appendTo(var1, var2.get(2) + 1);
      }

      public int estimateLength() {
         return 2;
      }
   }

   private static class TwoDigitNumberField implements FastDatePrinter.NumberRule {
      private final int mField;

      TwoDigitNumberField(int var1) {
         this.mField = var1;
      }

      public final void appendTo(StringBuffer var1, int var2) {
         if (var2 < 100) {
            var1.append((char)(var2 / 10 + 48));
            var1.append((char)(var2 % 10 + 48));
         } else {
            var1.append(Integer.toString(var2));
         }

      }

      public void appendTo(StringBuffer var1, Calendar var2) {
         this.appendTo(var1, var2.get(this.mField));
      }

      public int estimateLength() {
         return 2;
      }
   }

   private static class TwoDigitYearField implements FastDatePrinter.NumberRule {
      static final FastDatePrinter.TwoDigitYearField INSTANCE = new FastDatePrinter.TwoDigitYearField();

      TwoDigitYearField() {
      }

      public final void appendTo(StringBuffer var1, int var2) {
         var1.append((char)(var2 / 10 + 48));
         var1.append((char)(var2 % 10 + 48));
      }

      public void appendTo(StringBuffer var1, Calendar var2) {
         this.appendTo(var1, var2.get(1) % 100);
      }

      public int estimateLength() {
         return 2;
      }
   }

   private static class UnpaddedMonthField implements FastDatePrinter.NumberRule {
      static final FastDatePrinter.UnpaddedMonthField INSTANCE = new FastDatePrinter.UnpaddedMonthField();

      UnpaddedMonthField() {
      }

      public final void appendTo(StringBuffer var1, int var2) {
         if (var2 < 10) {
            var1.append((char)(var2 + 48));
         } else {
            var1.append((char)(var2 / 10 + 48));
            var1.append((char)(var2 % 10 + 48));
         }

      }

      public void appendTo(StringBuffer var1, Calendar var2) {
         this.appendTo(var1, var2.get(2) + 1);
      }

      public int estimateLength() {
         return 2;
      }
   }

   private static class UnpaddedNumberField implements FastDatePrinter.NumberRule {
      private final int mField;

      UnpaddedNumberField(int var1) {
         this.mField = var1;
      }

      public final void appendTo(StringBuffer var1, int var2) {
         if (var2 < 10) {
            var1.append((char)(var2 + 48));
         } else if (var2 < 100) {
            var1.append((char)(var2 / 10 + 48));
            var1.append((char)(var2 % 10 + 48));
         } else {
            var1.append(Integer.toString(var2));
         }

      }

      public void appendTo(StringBuffer var1, Calendar var2) {
         this.appendTo(var1, var2.get(this.mField));
      }

      public int estimateLength() {
         return 4;
      }
   }
}
