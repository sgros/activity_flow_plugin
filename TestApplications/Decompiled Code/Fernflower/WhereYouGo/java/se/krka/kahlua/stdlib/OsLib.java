package se.krka.kahlua.stdlib;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;

public class OsLib implements JavaFunction {
   private static final int DATE = 0;
   private static final String DAY = "day";
   private static final String DEFAULT_FORMAT = "%c";
   private static final int DIFFTIME = 1;
   private static final String HOUR = "hour";
   private static final Object MILLISECOND;
   private static final int MILLIS_PER_DAY = 86400000;
   private static final int MILLIS_PER_WEEK = 604800000;
   private static final String MIN = "min";
   private static final String MONTH = "month";
   private static final int NUM_FUNCS = 3;
   private static final String SEC = "sec";
   private static final String TABLE_FORMAT = "*t";
   private static final int TIME = 2;
   public static final int TIME_DIVIDEND = 1000;
   public static final double TIME_DIVIDEND_INVERTED = 0.001D;
   private static final String WDAY = "wday";
   private static final String YDAY = "yday";
   private static final String YEAR = "year";
   private static String[] funcnames = new String[3];
   private static OsLib[] funcs;
   private static String[] longDayNames;
   private static String[] longMonthNames;
   private static String[] shortDayNames;
   private static String[] shortMonthNames;
   private static TimeZone tzone;
   private int methodId;

   static {
      funcnames[0] = "date";
      funcnames[1] = "difftime";
      funcnames[2] = "time";
      funcs = new OsLib[3];

      for(int var0 = 0; var0 < 3; ++var0) {
         funcs[var0] = new OsLib(var0);
      }

      MILLISECOND = "milli";
      tzone = TimeZone.getDefault();
      shortDayNames = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
      longDayNames = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
      shortMonthNames = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
      longMonthNames = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
   }

   private OsLib(int var1) {
      this.methodId = var1;
   }

   private int date(LuaCallFrame var1, int var2) {
      if (var2 == 0) {
         var2 = var1.push(getdate("%c"));
      } else {
         String var3 = BaseLib.rawTostring(var1.get(0));
         if (var2 == 1) {
            var2 = var1.push(getdate(var3));
         } else {
            var2 = var1.push(getdate(var3, (long)(BaseLib.rawTonumber(var1.get(1)) * 1000.0D)));
         }
      }

      return var2;
   }

   private int difftime(LuaCallFrame var1, int var2) {
      var1.push(LuaState.toDouble(BaseLib.rawTonumber(var1.get(0)) - BaseLib.rawTonumber(var1.get(1))));
      return 1;
   }

   public static String formatTime(String var0, Calendar var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         if (var0.charAt(var3) == '%' && var3 + 1 != var0.length()) {
            ++var3;
            var2.append(strftime(var0.charAt(var3), var1));
         } else {
            var2.append(var0.charAt(var3));
         }
      }

      return var2.toString();
   }

   public static Date getDateFromTable(LuaTable var0) {
      Calendar var1 = Calendar.getInstance(tzone);
      var1.set(1, (int)LuaState.fromDouble(var0.rawget("year")));
      var1.set(2, (int)LuaState.fromDouble(var0.rawget("month")) - 1);
      var1.set(5, (int)LuaState.fromDouble(var0.rawget("day")));
      Object var2 = var0.rawget("hour");
      Object var3 = var0.rawget("min");
      Object var4 = var0.rawget("sec");
      Object var5 = var0.rawget(MILLISECOND);
      if (var2 != null) {
         var1.set(11, (int)LuaState.fromDouble(var2));
      } else {
         var1.set(11, 0);
      }

      if (var3 != null) {
         var1.set(12, (int)LuaState.fromDouble(var3));
      } else {
         var1.set(12, 0);
      }

      if (var4 != null) {
         var1.set(13, (int)LuaState.fromDouble(var4));
      } else {
         var1.set(13, 0);
      }

      if (var5 != null) {
         var1.set(14, (int)LuaState.fromDouble(var5));
      } else {
         var1.set(14, 0);
      }

      return var1.getTime();
   }

   public static int getDayOfYear(Calendar var0) {
      Calendar var1 = Calendar.getInstance(var0.getTimeZone());
      var1.setTime(var0.getTime());
      var1.set(2, 0);
      var1.set(5, 1);
      return (int)Math.ceil((double)(var0.getTime().getTime() - var1.getTime().getTime()) / 8.64E7D);
   }

   public static LuaTable getTableFromDate(Calendar var0) {
      LuaTableImpl var1 = new LuaTableImpl();
      var1.rawset("year", LuaState.toDouble((long)var0.get(1)));
      var1.rawset("month", LuaState.toDouble((long)(var0.get(2) + 1)));
      var1.rawset("day", LuaState.toDouble((long)var0.get(5)));
      var1.rawset("hour", LuaState.toDouble((long)var0.get(11)));
      var1.rawset("min", LuaState.toDouble((long)var0.get(12)));
      var1.rawset("sec", LuaState.toDouble((long)var0.get(13)));
      var1.rawset("wday", LuaState.toDouble((long)var0.get(7)));
      var1.rawset("yday", LuaState.toDouble((long)getDayOfYear(var0)));
      var1.rawset(MILLISECOND, LuaState.toDouble((long)var0.get(14)));
      return var1;
   }

   public static int getWeekOfYear(Calendar var0, boolean var1, boolean var2) {
      Calendar var3 = Calendar.getInstance(var0.getTimeZone());
      var3.setTime(var0.getTime());
      var3.set(2, 0);
      var3.set(5, 1);
      int var4 = var3.get(7);
      if (var1 && var4 != 1) {
         var3.set(5, 7 - var4 + 1);
      } else if (var4 != 2) {
         var3.set(5, 7 - var4 + 1 + 1);
      }

      int var5 = (int)((var0.getTime().getTime() - var3.getTime().getTime()) / 604800000L);
      int var6 = var5;
      if (var2) {
         var6 = var5;
         if (7 - var4 >= 4) {
            var6 = var5 + 1;
         }
      }

      return var6;
   }

   public static Object getdate(String var0) {
      return getdate(var0, Calendar.getInstance().getTime().getTime());
   }

   public static Object getdate(String var0, long var1) {
      int var3 = 0;
      Calendar var4;
      if (var0.charAt(0) == '!') {
         var4 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
         var3 = 0 + 1;
      } else {
         var4 = Calendar.getInstance(tzone);
      }

      var4.setTime(new Date(var1));
      Object var5;
      if (var4 == null) {
         var5 = null;
      } else if (var0.substring(var3, var3 + 2).equals("*t")) {
         var5 = getTableFromDate(var4);
      } else {
         var5 = formatTime(var0.substring(var3), var4);
      }

      return var5;
   }

   public static void register(LuaState var0) {
      LuaTableImpl var1 = new LuaTableImpl();
      var0.getEnvironment().rawset("os", var1);

      for(int var2 = 0; var2 < 3; ++var2) {
         var1.rawset(funcnames[var2], funcs[var2]);
      }

   }

   public static void setTimeZone(TimeZone var0) {
      tzone = var0;
   }

   private static String strftime(char var0, Calendar var1) {
      String var2;
      switch(var0) {
      case 'A':
         var2 = longDayNames[var1.get(7) - 1];
         break;
      case 'B':
         var2 = longMonthNames[var1.get(2)];
         break;
      case 'C':
         var2 = Integer.toString(var1.get(1) / 100);
         break;
      case 'D':
         var2 = formatTime("%m/%d/%y", var1);
         break;
      case 'E':
      case 'F':
      case 'G':
      case 'J':
      case 'K':
      case 'L':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'T':
      case 'X':
      case '[':
      case '\\':
      case ']':
      case '^':
      case '_':
      case '`':
      case 'f':
      case 'g':
      case 'i':
      case 'k':
      case 'l':
      case 'o':
      case 'q':
      case 's':
      case 't':
      case 'u':
      case 'v':
      case 'x':
      default:
         var2 = null;
         break;
      case 'H':
         var2 = Integer.toString(var1.get(11));
         break;
      case 'I':
         var2 = Integer.toString(var1.get(10));
         break;
      case 'M':
         var2 = Integer.toString(var1.get(12));
         break;
      case 'R':
         var2 = formatTime("%H:%M", var1);
         break;
      case 'S':
         var2 = Integer.toString(var1.get(13));
         break;
      case 'U':
         var2 = Integer.toString(getWeekOfYear(var1, true, false));
         break;
      case 'V':
         var2 = Integer.toString(getWeekOfYear(var1, false, true));
         break;
      case 'W':
         var2 = Integer.toString(getWeekOfYear(var1, false, false));
         break;
      case 'Y':
         var2 = Integer.toString(var1.get(1));
         break;
      case 'Z':
         var2 = var1.getTimeZone().getID();
         break;
      case 'a':
         var2 = shortDayNames[var1.get(7) - 1];
         break;
      case 'b':
         var2 = shortMonthNames[var1.get(2)];
         break;
      case 'c':
         var2 = var1.getTime().toString();
         break;
      case 'd':
         var2 = Integer.toString(var1.get(5));
         break;
      case 'e':
         if (var1.get(5) < 10) {
            var2 = " " + strftime('d', var1);
         } else {
            var2 = strftime('d', var1);
         }
         break;
      case 'h':
         var2 = strftime('b', var1);
         break;
      case 'j':
         var2 = Integer.toString(getDayOfYear(var1));
         break;
      case 'm':
         var2 = Integer.toString(var1.get(2) + 1);
         break;
      case 'n':
         var2 = "\n";
         break;
      case 'p':
         if (var1.get(9) == 0) {
            var2 = "AM";
         } else {
            var2 = "PM";
         }
         break;
      case 'r':
         var2 = formatTime("%I:%M:%S %p", var1);
         break;
      case 'w':
         var2 = Integer.toString(var1.get(7) - 1);
         break;
      case 'y':
         var2 = Integer.toString(var1.get(1) % 100);
      }

      return var2;
   }

   private int time(LuaCallFrame var1, int var2) {
      if (var2 == 0) {
         var1.push(LuaState.toDouble((double)System.currentTimeMillis() * 0.001D));
      } else {
         var1.push(LuaState.toDouble((double)getDateFromTable((LuaTable)BaseLib.getArg(var1, 1, "table", "time")).getTime() * 0.001D));
      }

      return 1;
   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.methodId) {
      case 0:
         var2 = this.date(var1, var2);
         break;
      case 1:
         var2 = this.difftime(var1, var2);
         break;
      case 2:
         var2 = this.time(var1, var2);
         break;
      default:
         throw new RuntimeException("Undefined method called on os.");
      }

      return var2;
   }
}
