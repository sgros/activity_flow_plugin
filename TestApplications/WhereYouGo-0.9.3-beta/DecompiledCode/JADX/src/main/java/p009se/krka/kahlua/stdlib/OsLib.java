package p009se.krka.kahlua.stdlib;

import com.google.zxing.pdf417.PDF417Common;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import locus.api.android.features.geocaching.fieldNotes.FieldNotesHelper.ColFieldNote;
import locus.api.objects.extra.ExtraData;
import locus.api.objects.geocaching.GeocachingData;
import p009se.krka.kahlua.p010vm.JavaFunction;
import p009se.krka.kahlua.p010vm.LuaCallFrame;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.p010vm.LuaTableImpl;

/* renamed from: se.krka.kahlua.stdlib.OsLib */
public class OsLib implements JavaFunction {
    private static final int DATE = 0;
    private static final String DAY = "day";
    private static final String DEFAULT_FORMAT = "%c";
    private static final int DIFFTIME = 1;
    private static final String HOUR = "hour";
    private static final Object MILLISECOND = "milli";
    private static final int MILLIS_PER_DAY = 86400000;
    private static final int MILLIS_PER_WEEK = 604800000;
    private static final String MIN = "min";
    private static final String MONTH = "month";
    private static final int NUM_FUNCS = 3;
    private static final String SEC = "sec";
    private static final String TABLE_FORMAT = "*t";
    private static final int TIME = 2;
    public static final int TIME_DIVIDEND = 1000;
    public static final double TIME_DIVIDEND_INVERTED = 0.001d;
    private static final String WDAY = "wday";
    private static final String YDAY = "yday";
    private static final String YEAR = "year";
    private static String[] funcnames = new String[3];
    private static OsLib[] funcs = new OsLib[3];
    private static String[] longDayNames = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static String[] longMonthNames = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static String[] shortDayNames = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static String[] shortMonthNames = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private static TimeZone tzone = TimeZone.getDefault();
    private int methodId;

    static {
        funcnames[0] = "date";
        funcnames[1] = "difftime";
        funcnames[2] = ColFieldNote.TIME;
        for (int i = 0; i < 3; i++) {
            funcs[i] = new OsLib(i);
        }
    }

    public static void register(LuaState state) {
        LuaTable os = new LuaTableImpl();
        state.getEnvironment().rawset("os", os);
        for (int i = 0; i < 3; i++) {
            os.rawset(funcnames[i], funcs[i]);
        }
    }

    public static void setTimeZone(TimeZone tz) {
        tzone = tz;
    }

    private OsLib(int methodId) {
        this.methodId = methodId;
    }

    public int call(LuaCallFrame cf, int nargs) {
        switch (this.methodId) {
            case 0:
                return date(cf, nargs);
            case 1:
                return difftime(cf, nargs);
            case 2:
                return time(cf, nargs);
            default:
                throw new RuntimeException("Undefined method called on os.");
        }
    }

    private int time(LuaCallFrame cf, int nargs) {
        if (nargs == 0) {
            cf.push(LuaState.toDouble(((double) System.currentTimeMillis()) * 0.001d));
        } else {
            cf.push(LuaState.toDouble(((double) OsLib.getDateFromTable((LuaTable) BaseLib.getArg(cf, 1, BaseLib.TYPE_TABLE, ColFieldNote.TIME)).getTime()) * 0.001d));
        }
        return 1;
    }

    private int difftime(LuaCallFrame cf, int nargs) {
        cf.push(LuaState.toDouble(BaseLib.rawTonumber(cf.get(0)).doubleValue() - BaseLib.rawTonumber(cf.get(1)).doubleValue()));
        return 1;
    }

    private int date(LuaCallFrame cf, int nargs) {
        if (nargs == 0) {
            return cf.push(OsLib.getdate(DEFAULT_FORMAT));
        }
        String format = BaseLib.rawTostring(cf.get(0));
        if (nargs == 1) {
            return cf.push(OsLib.getdate(format));
        }
        return cf.push(OsLib.getdate(format, (long) (BaseLib.rawTonumber(cf.get(1)).doubleValue() * 1000.0d)));
    }

    public static Object getdate(String format) {
        return OsLib.getdate(format, Calendar.getInstance().getTime().getTime());
    }

    public static Object getdate(String format, long time) {
        Calendar calendar;
        int si = 0;
        if (format.charAt(0) == '!') {
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            si = 0 + 1;
        } else {
            calendar = Calendar.getInstance(tzone);
        }
        calendar.setTime(new Date(time));
        if (calendar == null) {
            return null;
        }
        if (format.substring(si, si + 2).equals(TABLE_FORMAT)) {
            return OsLib.getTableFromDate(calendar);
        }
        return OsLib.formatTime(format.substring(si), calendar);
    }

    public static String formatTime(String format, Calendar cal) {
        StringBuffer buffer = new StringBuffer();
        int stringIndex = 0;
        while (stringIndex < format.length()) {
            if (format.charAt(stringIndex) != '%' || stringIndex + 1 == format.length()) {
                buffer.append(format.charAt(stringIndex));
            } else {
                stringIndex++;
                buffer.append(OsLib.strftime(format.charAt(stringIndex), cal));
            }
            stringIndex++;
        }
        return buffer.toString();
    }

    private static String strftime(char format, Calendar cal) {
        switch (format) {
            case 'A':
                return longDayNames[cal.get(7) - 1];
            case 'B':
                return longMonthNames[cal.get(2)];
            case 'C':
                return Integer.toString(cal.get(1) / 100);
            case 'D':
                return OsLib.formatTime("%m/%d/%y", cal);
            case 'H':
                return Integer.toString(cal.get(11));
            case 'I':
                return Integer.toString(cal.get(10));
            case 'M':
                return Integer.toString(cal.get(12));
            case 'R':
                return OsLib.formatTime("%H:%M", cal);
            case 'S':
                return Integer.toString(cal.get(13));
            case 'U':
                return Integer.toString(OsLib.getWeekOfYear(cal, true, false));
            case 'V':
                return Integer.toString(OsLib.getWeekOfYear(cal, false, true));
            case 'W':
                return Integer.toString(OsLib.getWeekOfYear(cal, false, false));
            case 'Y':
                return Integer.toString(cal.get(1));
            case PDF417Common.MAX_ROWS_IN_BARCODE /*90*/:
                return cal.getTimeZone().getID();
            case 'a':
                return shortDayNames[cal.get(7) - 1];
            case 'b':
                return shortMonthNames[cal.get(2)];
            case 'c':
                return cal.getTime().toString();
            case 'd':
                return Integer.toString(cal.get(5));
            case 'e':
                return cal.get(5) < 10 ? " " + OsLib.strftime('d', cal) : OsLib.strftime('d', cal);
            case 'h':
                return OsLib.strftime('b', cal);
            case GeocachingData.CACHE_SOURCE_OPENCACHING_PL /*106*/:
                return Integer.toString(OsLib.getDayOfYear(cal));
            case 'm':
                return Integer.toString(cal.get(2) + 1);
            case 'n':
                return "\n";
            case 'p':
                return cal.get(9) == 0 ? "AM" : "PM";
            case 'r':
                return OsLib.formatTime("%I:%M:%S %p", cal);
            case 'w':
                return Integer.toString(cal.get(7) - 1);
            case ExtraData.PAR_RTE_SIMPLE_ROUNDABOUTS /*121*/:
                return Integer.toString(cal.get(1) % 100);
            default:
                return null;
        }
    }

    public static LuaTable getTableFromDate(Calendar c) {
        LuaTable time = new LuaTableImpl();
        time.rawset(YEAR, LuaState.toDouble((long) c.get(1)));
        time.rawset(MONTH, LuaState.toDouble((long) (c.get(2) + 1)));
        time.rawset(DAY, LuaState.toDouble((long) c.get(5)));
        time.rawset(HOUR, LuaState.toDouble((long) c.get(11)));
        time.rawset(MIN, LuaState.toDouble((long) c.get(12)));
        time.rawset(SEC, LuaState.toDouble((long) c.get(13)));
        time.rawset(WDAY, LuaState.toDouble((long) c.get(7)));
        time.rawset(YDAY, LuaState.toDouble((long) OsLib.getDayOfYear(c)));
        time.rawset(MILLISECOND, LuaState.toDouble((long) c.get(14)));
        return time;
    }

    public static Date getDateFromTable(LuaTable time) {
        Calendar c = Calendar.getInstance(tzone);
        c.set(1, (int) LuaState.fromDouble(time.rawget(YEAR)));
        c.set(2, ((int) LuaState.fromDouble(time.rawget(MONTH))) - 1);
        c.set(5, (int) LuaState.fromDouble(time.rawget(DAY)));
        Object hour = time.rawget(HOUR);
        Object minute = time.rawget(MIN);
        Object seconds = time.rawget(SEC);
        Object milliseconds = time.rawget(MILLISECOND);
        if (hour != null) {
            c.set(11, (int) LuaState.fromDouble(hour));
        } else {
            c.set(11, 0);
        }
        if (minute != null) {
            c.set(12, (int) LuaState.fromDouble(minute));
        } else {
            c.set(12, 0);
        }
        if (seconds != null) {
            c.set(13, (int) LuaState.fromDouble(seconds));
        } else {
            c.set(13, 0);
        }
        if (milliseconds != null) {
            c.set(14, (int) LuaState.fromDouble(milliseconds));
        } else {
            c.set(14, 0);
        }
        return c.getTime();
    }

    public static int getDayOfYear(Calendar c) {
        Calendar c2 = Calendar.getInstance(c.getTimeZone());
        c2.setTime(c.getTime());
        c2.set(2, 0);
        c2.set(5, 1);
        return (int) Math.ceil(((double) (c.getTime().getTime() - c2.getTime().getTime())) / 8.64E7d);
    }

    public static int getWeekOfYear(Calendar c, boolean weekStartsSunday, boolean jan1midweek) {
        Calendar c2 = Calendar.getInstance(c.getTimeZone());
        c2.setTime(c.getTime());
        c2.set(2, 0);
        c2.set(5, 1);
        int dayOfWeek = c2.get(7);
        if (weekStartsSunday && dayOfWeek != 1) {
            c2.set(5, (7 - dayOfWeek) + 1);
        } else if (dayOfWeek != 2) {
            c2.set(5, ((7 - dayOfWeek) + 1) + 1);
        }
        int w = (int) ((c.getTime().getTime() - c2.getTime().getTime()) / 604800000);
        if (!jan1midweek || 7 - dayOfWeek < 4) {
            return w;
        }
        return w + 1;
    }
}
