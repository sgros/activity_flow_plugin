// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.stdlib;

import se.krka.kahlua.vm.LuaTableImpl;
import java.util.Date;
import se.krka.kahlua.vm.LuaTable;
import java.util.Calendar;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaCallFrame;
import java.util.TimeZone;
import se.krka.kahlua.vm.JavaFunction;

public class OsLib implements JavaFunction
{
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
    public static final double TIME_DIVIDEND_INVERTED = 0.001;
    private static final String WDAY = "wday";
    private static final String YDAY = "yday";
    private static final String YEAR = "year";
    private static String[] funcnames;
    private static OsLib[] funcs;
    private static String[] longDayNames;
    private static String[] longMonthNames;
    private static String[] shortDayNames;
    private static String[] shortMonthNames;
    private static TimeZone tzone;
    private int methodId;
    
    static {
        (OsLib.funcnames = new String[3])[0] = "date";
        OsLib.funcnames[1] = "difftime";
        OsLib.funcnames[2] = "time";
        OsLib.funcs = new OsLib[3];
        for (int i = 0; i < 3; ++i) {
            OsLib.funcs[i] = new OsLib(i);
        }
        MILLISECOND = "milli";
        OsLib.tzone = TimeZone.getDefault();
        OsLib.shortDayNames = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        OsLib.longDayNames = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        OsLib.shortMonthNames = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        OsLib.longMonthNames = new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    }
    
    private OsLib(final int methodId) {
        this.methodId = methodId;
    }
    
    private int date(final LuaCallFrame luaCallFrame, int n) {
        if (n == 0) {
            n = luaCallFrame.push(getdate("%c"));
        }
        else {
            final String rawTostring = BaseLib.rawTostring(luaCallFrame.get(0));
            if (n == 1) {
                n = luaCallFrame.push(getdate(rawTostring));
            }
            else {
                n = luaCallFrame.push(getdate(rawTostring, (long)(BaseLib.rawTonumber(luaCallFrame.get(1)) * 1000.0)));
            }
        }
        return n;
    }
    
    private int difftime(final LuaCallFrame luaCallFrame, final int n) {
        luaCallFrame.push(LuaState.toDouble(BaseLib.rawTonumber(luaCallFrame.get(0)) - BaseLib.rawTonumber(luaCallFrame.get(1))));
        return 1;
    }
    
    public static String formatTime(final String s, final Calendar calendar) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) != '%' || i + 1 == s.length()) {
                sb.append(s.charAt(i));
            }
            else {
                ++i;
                sb.append(strftime(s.charAt(i), calendar));
            }
        }
        return sb.toString();
    }
    
    public static Date getDateFromTable(final LuaTable luaTable) {
        final Calendar instance = Calendar.getInstance(OsLib.tzone);
        instance.set(1, (int)LuaState.fromDouble(luaTable.rawget("year")));
        instance.set(2, (int)LuaState.fromDouble(luaTable.rawget("month")) - 1);
        instance.set(5, (int)LuaState.fromDouble(luaTable.rawget("day")));
        final Object rawget = luaTable.rawget("hour");
        final Object rawget2 = luaTable.rawget("min");
        final Object rawget3 = luaTable.rawget("sec");
        final Object rawget4 = luaTable.rawget(OsLib.MILLISECOND);
        if (rawget != null) {
            instance.set(11, (int)LuaState.fromDouble(rawget));
        }
        else {
            instance.set(11, 0);
        }
        if (rawget2 != null) {
            instance.set(12, (int)LuaState.fromDouble(rawget2));
        }
        else {
            instance.set(12, 0);
        }
        if (rawget3 != null) {
            instance.set(13, (int)LuaState.fromDouble(rawget3));
        }
        else {
            instance.set(13, 0);
        }
        if (rawget4 != null) {
            instance.set(14, (int)LuaState.fromDouble(rawget4));
        }
        else {
            instance.set(14, 0);
        }
        return instance.getTime();
    }
    
    public static int getDayOfYear(final Calendar calendar) {
        final Calendar instance = Calendar.getInstance(calendar.getTimeZone());
        instance.setTime(calendar.getTime());
        instance.set(2, 0);
        instance.set(5, 1);
        return (int)Math.ceil((calendar.getTime().getTime() - instance.getTime().getTime()) / 8.64E7);
    }
    
    public static LuaTable getTableFromDate(final Calendar calendar) {
        final LuaTableImpl luaTableImpl = new LuaTableImpl();
        luaTableImpl.rawset("year", LuaState.toDouble(calendar.get(1)));
        luaTableImpl.rawset("month", LuaState.toDouble(calendar.get(2) + 1));
        luaTableImpl.rawset("day", LuaState.toDouble(calendar.get(5)));
        luaTableImpl.rawset("hour", LuaState.toDouble(calendar.get(11)));
        luaTableImpl.rawset("min", LuaState.toDouble(calendar.get(12)));
        luaTableImpl.rawset("sec", LuaState.toDouble(calendar.get(13)));
        luaTableImpl.rawset("wday", LuaState.toDouble(calendar.get(7)));
        luaTableImpl.rawset("yday", LuaState.toDouble(getDayOfYear(calendar)));
        luaTableImpl.rawset(OsLib.MILLISECOND, LuaState.toDouble(calendar.get(14)));
        return luaTableImpl;
    }
    
    public static int getWeekOfYear(final Calendar calendar, final boolean b, final boolean b2) {
        final Calendar instance = Calendar.getInstance(calendar.getTimeZone());
        instance.setTime(calendar.getTime());
        instance.set(2, 0);
        instance.set(5, 1);
        final int value = instance.get(7);
        if (b && value != 1) {
            instance.set(5, 7 - value + 1);
        }
        else if (value != 2) {
            instance.set(5, 7 - value + 1 + 1);
        }
        int n2;
        final int n = n2 = (int)((calendar.getTime().getTime() - instance.getTime().getTime()) / 604800000L);
        if (b2) {
            n2 = n;
            if (7 - value >= 4) {
                n2 = n + 1;
            }
        }
        return n2;
    }
    
    public static Object getdate(final String s) {
        return getdate(s, Calendar.getInstance().getTime().getTime());
    }
    
    public static Object getdate(String s, final long date) {
        int n = 0;
        Calendar calendar;
        if (s.charAt(0) == '!') {
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            n = 0 + 1;
        }
        else {
            calendar = Calendar.getInstance(OsLib.tzone);
        }
        calendar.setTime(new Date(date));
        if (calendar == null) {
            s = null;
        }
        else if (s.substring(n, n + 2).equals("*t")) {
            s = (String)getTableFromDate(calendar);
        }
        else {
            s = formatTime(s.substring(n), calendar);
        }
        return s;
    }
    
    public static void register(final LuaState luaState) {
        final LuaTableImpl luaTableImpl = new LuaTableImpl();
        luaState.getEnvironment().rawset("os", luaTableImpl);
        for (int i = 0; i < 3; ++i) {
            luaTableImpl.rawset(OsLib.funcnames[i], OsLib.funcs[i]);
        }
    }
    
    public static void setTimeZone(final TimeZone tzone) {
        OsLib.tzone = tzone;
    }
    
    private static String strftime(final char c, final Calendar calendar) {
        String s = null;
        switch (c) {
            default: {
                s = null;
                break;
            }
            case 'a': {
                s = OsLib.shortDayNames[calendar.get(7) - 1];
                break;
            }
            case 'A': {
                s = OsLib.longDayNames[calendar.get(7) - 1];
                break;
            }
            case 'b': {
                s = OsLib.shortMonthNames[calendar.get(2)];
                break;
            }
            case 'B': {
                s = OsLib.longMonthNames[calendar.get(2)];
                break;
            }
            case 'c': {
                s = calendar.getTime().toString();
                break;
            }
            case 'C': {
                s = Integer.toString(calendar.get(1) / 100);
                break;
            }
            case 'd': {
                s = Integer.toString(calendar.get(5));
                break;
            }
            case 'D': {
                s = formatTime("%m/%d/%y", calendar);
                break;
            }
            case 'e': {
                if (calendar.get(5) < 10) {
                    s = " " + strftime('d', calendar);
                    break;
                }
                s = strftime('d', calendar);
                break;
            }
            case 'h': {
                s = strftime('b', calendar);
                break;
            }
            case 'H': {
                s = Integer.toString(calendar.get(11));
                break;
            }
            case 'I': {
                s = Integer.toString(calendar.get(10));
                break;
            }
            case 'j': {
                s = Integer.toString(getDayOfYear(calendar));
                break;
            }
            case 'm': {
                s = Integer.toString(calendar.get(2) + 1);
                break;
            }
            case 'M': {
                s = Integer.toString(calendar.get(12));
                break;
            }
            case 'n': {
                s = "\n";
                break;
            }
            case 'p': {
                if (calendar.get(9) == 0) {
                    s = "AM";
                    break;
                }
                s = "PM";
                break;
            }
            case 'r': {
                s = formatTime("%I:%M:%S %p", calendar);
                break;
            }
            case 'R': {
                s = formatTime("%H:%M", calendar);
                break;
            }
            case 'S': {
                s = Integer.toString(calendar.get(13));
                break;
            }
            case 'U': {
                s = Integer.toString(getWeekOfYear(calendar, true, false));
                break;
            }
            case 'V': {
                s = Integer.toString(getWeekOfYear(calendar, false, true));
                break;
            }
            case 'w': {
                s = Integer.toString(calendar.get(7) - 1);
                break;
            }
            case 'W': {
                s = Integer.toString(getWeekOfYear(calendar, false, false));
                break;
            }
            case 'y': {
                s = Integer.toString(calendar.get(1) % 100);
                break;
            }
            case 'Y': {
                s = Integer.toString(calendar.get(1));
                break;
            }
            case 'Z': {
                s = calendar.getTimeZone().getID();
                break;
            }
        }
        return s;
    }
    
    private int time(final LuaCallFrame luaCallFrame, final int n) {
        if (n == 0) {
            luaCallFrame.push(LuaState.toDouble(System.currentTimeMillis() * 0.001));
        }
        else {
            luaCallFrame.push(LuaState.toDouble(getDateFromTable((LuaTable)BaseLib.getArg(luaCallFrame, 1, "table", "time")).getTime() * 0.001));
        }
        return 1;
    }
    
    @Override
    public int call(final LuaCallFrame luaCallFrame, int n) {
        switch (this.methodId) {
            default: {
                throw new RuntimeException("Undefined method called on os.");
            }
            case 0: {
                n = this.date(luaCallFrame, n);
                break;
            }
            case 1: {
                n = this.difftime(luaCallFrame, n);
                break;
            }
            case 2: {
                n = this.time(luaCallFrame, n);
                break;
            }
        }
        return n;
    }
}
