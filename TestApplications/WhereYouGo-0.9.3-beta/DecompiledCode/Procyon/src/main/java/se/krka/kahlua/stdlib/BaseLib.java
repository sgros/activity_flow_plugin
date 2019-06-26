// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.stdlib;

import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaException;
import se.krka.kahlua.vm.LuaThread;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.JavaFunction;

public final class BaseLib implements JavaFunction
{
    private static final int BYTECODELOADER = 18;
    private static final int COLLECTGARBAGE = 16;
    private static final int DEBUGSTACKTRACE = 17;
    private static final Object DOUBLE_ONE;
    private static final int ERROR = 8;
    private static final int GETFENV = 12;
    private static final int GETMETATABLE = 6;
    public static final Object MODE_KEY;
    private static final int NEXT = 10;
    private static final int NUM_FUNCTIONS = 19;
    private static final int PCALL = 0;
    private static final int PRINT = 1;
    private static final int RAWEQUAL = 13;
    private static final int RAWGET = 15;
    private static final int RAWSET = 14;
    private static final Runtime RUNTIME;
    private static final int SELECT = 2;
    private static final int SETFENV = 11;
    private static final int SETMETATABLE = 7;
    private static final int TONUMBER = 5;
    private static final int TOSTRING = 4;
    private static final int TYPE = 3;
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_FUNCTION = "function";
    public static final String TYPE_NIL = "nil";
    public static final String TYPE_NUMBER = "number";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_TABLE = "table";
    public static final String TYPE_THREAD = "thread";
    public static final String TYPE_USERDATA = "userdata";
    private static final int UNPACK = 9;
    private static BaseLib[] functions;
    private static final String[] names;
    private int index;
    
    static {
        RUNTIME = Runtime.getRuntime();
        MODE_KEY = "__mode";
        DOUBLE_ONE = new Double(1.0);
        (names = new String[19])[0] = "pcall";
        BaseLib.names[1] = "print";
        BaseLib.names[2] = "select";
        BaseLib.names[3] = "type";
        BaseLib.names[4] = "tostring";
        BaseLib.names[5] = "tonumber";
        BaseLib.names[6] = "getmetatable";
        BaseLib.names[7] = "setmetatable";
        BaseLib.names[8] = "error";
        BaseLib.names[9] = "unpack";
        BaseLib.names[10] = "next";
        BaseLib.names[11] = "setfenv";
        BaseLib.names[12] = "getfenv";
        BaseLib.names[13] = "rawequal";
        BaseLib.names[14] = "rawset";
        BaseLib.names[15] = "rawget";
        BaseLib.names[16] = "collectgarbage";
        BaseLib.names[17] = "debugstacktrace";
        BaseLib.names[18] = "bytecodeloader";
    }
    
    public BaseLib(final int index) {
        this.index = index;
    }
    
    private static int bytecodeloader(final LuaCallFrame luaCallFrame, int endIndex) {
        final String s = (String)getArg(luaCallFrame, 1, "string", "loader");
        final String s2 = (String)((LuaTable)luaCallFrame.getEnvironment().rawget("package")).rawget("classpath");
        for (int i = 0; i < s2.length(); i = endIndex) {
            if ((endIndex = s2.indexOf(";", i)) == -1) {
                endIndex = s2.length();
            }
            final String substring = s2.substring(i, endIndex);
            if (substring.length() > 0) {
                String string = substring;
                if (!substring.endsWith("/")) {
                    string = substring + "/";
                }
                final LuaClosure loadByteCodeFromResource = luaCallFrame.thread.state.loadByteCodeFromResource(string + s, luaCallFrame.getEnvironment());
                if (loadByteCodeFromResource != null) {
                    endIndex = luaCallFrame.push(loadByteCodeFromResource);
                    return endIndex;
                }
            }
        }
        endIndex = luaCallFrame.push("Could not find the bytecode for '" + s + "' in classpath");
        return endIndex;
    }
    
    public static int collectgarbage(final LuaCallFrame luaCallFrame, int n) {
        final int n2 = 3;
        Object value = null;
        if (n > 0) {
            value = luaCallFrame.get(0);
        }
        if (value == null || value.equals("step") || value.equals("collect")) {
            System.gc();
            n = 0;
        }
        else {
            if (!value.equals("count")) {
                throw new RuntimeException("invalid option: " + value);
            }
            final long freeMemory = BaseLib.RUNTIME.freeMemory();
            final long totalMemory = BaseLib.RUNTIME.totalMemory();
            luaCallFrame.setTop(3);
            luaCallFrame.set(0, toKiloBytes(totalMemory - freeMemory));
            luaCallFrame.set(1, toKiloBytes(freeMemory));
            luaCallFrame.set(2, toKiloBytes(totalMemory));
            n = n2;
        }
        return n;
    }
    
    private int debugstacktrace(final LuaCallFrame luaCallFrame, int intValue) {
        LuaThread thread;
        if ((thread = (LuaThread)getOptArg(luaCallFrame, 1, "thread")) == null) {
            thread = luaCallFrame.thread;
        }
        final Double n = (Double)getOptArg(luaCallFrame, 2, "number");
        intValue = 0;
        if (n != null) {
            intValue = n.intValue();
        }
        final Double n2 = (Double)getOptArg(luaCallFrame, 3, "number");
        int intValue2 = Integer.MAX_VALUE;
        if (n2 != null) {
            intValue2 = n2.intValue();
        }
        final Double n3 = (Double)getOptArg(luaCallFrame, 4, "number");
        int intValue3 = 0;
        if (n3 != null) {
            intValue3 = n3.intValue();
        }
        return luaCallFrame.push(thread.getCurrentStackTrace(intValue, intValue2, intValue3));
    }
    
    private int error(final LuaCallFrame luaCallFrame, final int n) {
        if (n >= 1) {
            String stackTrace;
            if ((stackTrace = (String)getOptArg(luaCallFrame, 2, "string")) == null) {
                stackTrace = "";
            }
            luaCallFrame.thread.stackTrace = stackTrace;
            throw new LuaException(luaCallFrame.get(0));
        }
        return 0;
    }
    
    public static void fail(final String message) {
        throw new RuntimeException(message);
    }
    
    public static Object getArg(final LuaCallFrame luaCallFrame, final int i, final String s, final String str) {
        Object o = luaCallFrame.get(i - 1);
        if (o == null) {
            throw new RuntimeException("bad argument #" + i + "to '" + str + "' (" + s + " expected, got no value)");
        }
        Label_0150: {
            if (s == "string") {
                final String rawTostring = rawTostring(o);
                if (rawTostring == null) {
                    break Label_0150;
                }
                o = rawTostring;
            }
            else {
                if (s != "number") {
                    break Label_0150;
                }
                o = rawTonumber(o);
                if (o == null) {
                    throw new RuntimeException("bad argument #" + i + " to '" + str + "' (number expected, got string)");
                }
            }
            return o;
        }
        if (s != null) {
            final String type = type(o);
            if (s != type) {
                fail("bad argument #" + i + " to '" + str + "' (" + s + " expected, got " + type + ")");
            }
        }
        return o;
    }
    
    public static Object getOptArg(final LuaCallFrame luaCallFrame, final int n, final String s) {
        Object o;
        if (n - 1 >= luaCallFrame.getTop()) {
            o = null;
        }
        else {
            final Object value = luaCallFrame.get(n - 1);
            if (value == null) {
                o = null;
            }
            else if (s == "string") {
                o = rawTostring(value);
            }
            else {
                o = value;
                if (s == "number") {
                    o = rawTonumber(value);
                }
            }
        }
        return o;
    }
    
    private int getfenv(final LuaCallFrame luaCallFrame, int intValue) {
        final boolean b = false;
        Object o = BaseLib.DOUBLE_ONE;
        if (intValue >= 1) {
            o = luaCallFrame.get(0);
        }
        LuaTable luaTable;
        if (o == null || o instanceof JavaFunction) {
            luaTable = luaCallFrame.thread.environment;
        }
        else if (o instanceof LuaClosure) {
            luaTable = ((LuaClosure)o).env;
        }
        else {
            final Double rawTonumber = rawTonumber(o);
            luaAssert(rawTonumber != null, "Expected number");
            intValue = rawTonumber.intValue();
            boolean b2 = b;
            if (intValue >= 0) {
                b2 = true;
            }
            luaAssert(b2, "level must be non-negative");
            luaTable = luaCallFrame.thread.getParent(intValue).getEnvironment();
        }
        luaCallFrame.push(luaTable);
        return 1;
    }
    
    private static int getmetatable(final LuaCallFrame luaCallFrame, final int n) {
        luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(luaCallFrame.thread.state.getmetatable(luaCallFrame.get(0), false));
        return 1;
    }
    
    private static void initFunctions() {
        synchronized (BaseLib.class) {
            if (BaseLib.functions == null) {
                BaseLib.functions = new BaseLib[19];
                for (int i = 0; i < 19; ++i) {
                    BaseLib.functions[i] = new BaseLib(i);
                }
            }
        }
    }
    
    public static void luaAssert(final boolean b, final String s) {
        if (!b) {
            fail(s);
        }
    }
    
    private int next(final LuaCallFrame luaCallFrame, int n) {
        final int n2 = 1;
        luaAssert(n >= 1, "Not enough arguments");
        final LuaTable luaTable = (LuaTable)luaCallFrame.get(0);
        Object value = null;
        if (n >= 2) {
            value = luaCallFrame.get(1);
        }
        final Object next = luaTable.next(value);
        if (next == null) {
            luaCallFrame.setTop(1);
            luaCallFrame.set(0, null);
            n = n2;
        }
        else {
            final Object rawget = luaTable.rawget(next);
            luaCallFrame.setTop(2);
            luaCallFrame.set(0, next);
            luaCallFrame.set(1, rawget);
            n = 2;
        }
        return n;
    }
    
    public static String numberToString(final Double n) {
        String s;
        if (n.isNaN()) {
            s = "nan";
        }
        else if (n.isInfinite()) {
            if (MathLib.isNegative(n)) {
                s = "-inf";
            }
            else {
                s = "inf";
            }
        }
        else {
            final double doubleValue = n;
            if (Math.floor(doubleValue) == doubleValue && Math.abs(doubleValue) < 1.0E14) {
                s = String.valueOf(n.longValue());
            }
            else {
                s = n.toString();
            }
        }
        return s;
    }
    
    public static int pcall(final LuaCallFrame luaCallFrame, final int n) {
        return luaCallFrame.thread.state.pcall(n - 1);
    }
    
    private static int print(final LuaCallFrame luaCallFrame, final int n) {
        final LuaState state = luaCallFrame.thread.state;
        final Object tableGet = state.tableGet(state.getEnvironment(), "tostring");
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; ++i) {
            if (i > 0) {
                sb.append("\t");
            }
            sb.append(state.call(tableGet, luaCallFrame.get(i), null, null));
        }
        state.getOut().println(sb.toString());
        return 0;
    }
    
    public static Double rawTonumber(final Object o) {
        Double tonumber;
        if (o instanceof Double) {
            tonumber = (Double)o;
        }
        else if (o instanceof String) {
            tonumber = tonumber((String)o);
        }
        else {
            tonumber = null;
        }
        return tonumber;
    }
    
    public static String rawTostring(final Object o) {
        String numberToString;
        if (o instanceof String) {
            numberToString = (String)o;
        }
        else if (o instanceof Double) {
            numberToString = numberToString((Double)o);
        }
        else {
            numberToString = null;
        }
        return numberToString;
    }
    
    private int rawequal(final LuaCallFrame luaCallFrame, final int n) {
        luaAssert(n >= 2, "Not enough arguments");
        luaCallFrame.push(toBoolean(LuaState.luaEquals(luaCallFrame.get(0), luaCallFrame.get(1))));
        return 1;
    }
    
    private int rawget(final LuaCallFrame luaCallFrame, final int n) {
        luaAssert(n >= 2, "Not enough arguments");
        luaCallFrame.push(((LuaTable)luaCallFrame.get(0)).rawget(luaCallFrame.get(1)));
        return 1;
    }
    
    private int rawset(final LuaCallFrame luaCallFrame, final int n) {
        luaAssert(n >= 3, "Not enough arguments");
        ((LuaTable)luaCallFrame.get(0)).rawset(luaCallFrame.get(1), luaCallFrame.get(2));
        luaCallFrame.setTop(1);
        return 1;
    }
    
    public static void register(final LuaState luaState) {
        initFunctions();
        for (int i = 0; i < 19; ++i) {
            luaState.getEnvironment().rawset(BaseLib.names[i], BaseLib.functions[i]);
        }
    }
    
    private static int select(final LuaCallFrame luaCallFrame, int n) {
        final int n2 = 1;
        luaAssert(n >= 1, "Not enough arguments");
        final Object value = luaCallFrame.get(0);
        if (value instanceof String && ((String)value).startsWith("#")) {
            luaCallFrame.push(LuaState.toDouble(n - 1));
            n = n2;
        }
        else {
            final int n3 = (int)LuaState.fromDouble(rawTonumber(value));
            if (n3 >= 1 && n3 <= n - 1) {
                n -= n3;
            }
            else {
                n = 0;
            }
        }
        return n;
    }
    
    private int setfenv(final LuaCallFrame luaCallFrame, int intValue) {
        final int n = 0;
        luaAssert(intValue >= 2, "Not enough arguments");
        final LuaTable luaTable = (LuaTable)luaCallFrame.get(1);
        luaAssert(luaTable != null, "expected a table");
        final Object value = luaCallFrame.get(0);
        LuaClosure closure;
        if (value instanceof LuaClosure) {
            closure = (LuaClosure)value;
        }
        else {
            final Double rawTonumber = rawTonumber(value);
            luaAssert(rawTonumber != null, "expected a lua function or a number");
            intValue = rawTonumber.intValue();
            if (intValue == 0) {
                luaCallFrame.thread.environment = luaTable;
                intValue = n;
                return intValue;
            }
            final LuaCallFrame parent = luaCallFrame.thread.getParent(intValue);
            if (!parent.isLua()) {
                fail("No closure found at this level: " + intValue);
            }
            closure = parent.closure;
        }
        closure.env = luaTable;
        luaCallFrame.setTop(1);
        intValue = 1;
        return intValue;
    }
    
    private static int setmetatable(final LuaCallFrame luaCallFrame, final int n) {
        luaAssert(n >= 2, "Not enough arguments");
        setmetatable(luaCallFrame.thread.state, luaCallFrame.get(0), (LuaTable)luaCallFrame.get(1), false);
        luaCallFrame.setTop(1);
        return 1;
    }
    
    public static void setmetatable(final LuaState luaState, final Object o, final LuaTable luaTable, final boolean b) {
        luaAssert(o != null, "Expected table, got nil");
        final Object getmetatable = luaState.getmetatable(o, b);
        if (!b && getmetatable != null && luaState.tableGet(getmetatable, "__metatable") != null) {
            throw new RuntimeException("Can not set metatable of protected object");
        }
        luaState.setmetatable(o, luaTable);
    }
    
    private static final Boolean toBoolean(final boolean b) {
        Boolean b2;
        if (b) {
            b2 = Boolean.TRUE;
        }
        else {
            b2 = Boolean.FALSE;
        }
        return b2;
    }
    
    private static Double toKiloBytes(final long n) {
        return LuaState.toDouble(n / 1024.0);
    }
    
    private static int tonumber(final LuaCallFrame luaCallFrame, int n) {
        final boolean b = false;
        luaAssert(n >= 1, "Not enough arguments");
        final Object value = luaCallFrame.get(0);
        if (n == 1) {
            luaCallFrame.push(rawTonumber(value));
        }
        else {
            final String s = (String)value;
            final Double rawTonumber = rawTonumber(luaCallFrame.get(1));
            boolean b2 = b;
            if (rawTonumber != null) {
                b2 = true;
            }
            luaAssert(b2, "Argument 2 must be a number");
            final double fromDouble = LuaState.fromDouble(rawTonumber);
            n = (int)fromDouble;
            if (n != fromDouble) {
                throw new RuntimeException("base is not an integer");
            }
            luaCallFrame.push(tonumber(s, n));
        }
        return 1;
    }
    
    public static Double tonumber(final String s) {
        return tonumber(s, 10);
    }
    
    public static Double tonumber(String s, final int radix) {
        if (radix < 2 || radix > 36) {
            throw new RuntimeException("base out of range");
        }
        Label_0037: {
            if (radix != 10) {
                break Label_0037;
            }
            while (true) {
                try {
                    s = Double.valueOf((String)s);
                    return (Double)s;
                    s = LuaState.toDouble(Integer.parseInt((String)s, radix));
                    return (Double)s;
                }
                catch (NumberFormatException ex) {
                    s = ((String)s).toLowerCase();
                    if (((String)s).endsWith("nan")) {
                        s = LuaState.toDouble(Double.NaN);
                        return (Double)s;
                    }
                    if (!((String)s).endsWith("inf")) {
                        s = null;
                        return (Double)s;
                    }
                    if (((String)s).charAt(0) == '-') {
                        s = LuaState.toDouble(Double.NEGATIVE_INFINITY);
                        return (Double)s;
                    }
                    s = LuaState.toDouble(Double.POSITIVE_INFINITY);
                    return (Double)s;
                }
                return (Double)s;
            }
        }
    }
    
    private static int tostring(final LuaCallFrame luaCallFrame, final int n) {
        luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(tostring(luaCallFrame.get(0), luaCallFrame.thread.state));
        return 1;
    }
    
    public static String tostring(final Object o, final LuaState luaState) {
        String s;
        if (o == null) {
            s = "nil";
        }
        else if (o instanceof String) {
            s = (String)o;
        }
        else if (o instanceof Double) {
            s = rawTostring(o);
        }
        else if (o instanceof Boolean) {
            if (o == Boolean.TRUE) {
                s = "true";
            }
            else {
                s = "false";
            }
        }
        else if (o instanceof JavaFunction) {
            s = "function 0x" + System.identityHashCode(o);
        }
        else if (o instanceof LuaClosure) {
            s = "function 0x" + System.identityHashCode(o);
        }
        else {
            final Object metaOp = luaState.getMetaOp(o, "__tostring");
            if (metaOp != null) {
                s = (String)luaState.call(metaOp, o, null, null);
            }
            else {
                if (!(o instanceof LuaTable)) {
                    throw new RuntimeException("no __tostring found on object");
                }
                s = "table 0x" + System.identityHashCode(o);
            }
        }
        return s;
    }
    
    private static int type(final LuaCallFrame luaCallFrame, final int n) {
        luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(type(luaCallFrame.get(0)));
        return 1;
    }
    
    public static String type(final Object o) {
        String s;
        if (o == null) {
            s = "nil";
        }
        else if (o instanceof String) {
            s = "string";
        }
        else if (o instanceof Double) {
            s = "number";
        }
        else if (o instanceof Boolean) {
            s = "boolean";
        }
        else if (o instanceof JavaFunction || o instanceof LuaClosure) {
            s = "function";
        }
        else if (o instanceof LuaTable) {
            s = "table";
        }
        else if (o instanceof LuaThread) {
            s = "thread";
        }
        else {
            s = "userdata";
        }
        return s;
    }
    
    private int unpack(final LuaCallFrame luaCallFrame, int n) {
        final int n2 = 0;
        luaAssert(n >= 1, "Not enough arguments");
        final LuaTable luaTable = (LuaTable)luaCallFrame.get(0);
        Object value = null;
        Object value2 = null;
        if (n >= 2) {
            value = luaCallFrame.get(1);
        }
        if (n >= 3) {
            value2 = luaCallFrame.get(2);
        }
        if (value != null) {
            n = (int)LuaState.fromDouble(value);
        }
        else {
            n = 1;
        }
        int len;
        if (value2 != null) {
            len = (int)LuaState.fromDouble(value2);
        }
        else {
            len = luaTable.len();
        }
        final int top = len + 1 - n;
        if (top <= 0) {
            luaCallFrame.setTop(0);
            n = n2;
        }
        else {
            luaCallFrame.setTop(top);
            for (int i = 0; i < top; ++i) {
                luaCallFrame.set(i, luaTable.rawget(LuaState.toDouble(n + i)));
            }
            n = top;
        }
        return n;
    }
    
    @Override
    public int call(final LuaCallFrame luaCallFrame, int n) {
        switch (this.index) {
            default: {
                n = 0;
                break;
            }
            case 0: {
                n = pcall(luaCallFrame, n);
                break;
            }
            case 1: {
                n = print(luaCallFrame, n);
                break;
            }
            case 2: {
                n = select(luaCallFrame, n);
                break;
            }
            case 3: {
                n = type(luaCallFrame, n);
                break;
            }
            case 4: {
                n = tostring(luaCallFrame, n);
                break;
            }
            case 5: {
                n = tonumber(luaCallFrame, n);
                break;
            }
            case 6: {
                n = getmetatable(luaCallFrame, n);
                break;
            }
            case 7: {
                n = setmetatable(luaCallFrame, n);
                break;
            }
            case 8: {
                n = this.error(luaCallFrame, n);
                break;
            }
            case 9: {
                n = this.unpack(luaCallFrame, n);
                break;
            }
            case 10: {
                n = this.next(luaCallFrame, n);
                break;
            }
            case 11: {
                n = this.setfenv(luaCallFrame, n);
                break;
            }
            case 12: {
                n = this.getfenv(luaCallFrame, n);
                break;
            }
            case 13: {
                n = this.rawequal(luaCallFrame, n);
                break;
            }
            case 14: {
                n = this.rawset(luaCallFrame, n);
                break;
            }
            case 15: {
                n = this.rawget(luaCallFrame, n);
                break;
            }
            case 16: {
                n = collectgarbage(luaCallFrame, n);
                break;
            }
            case 17: {
                n = this.debugstacktrace(luaCallFrame, n);
                break;
            }
            case 18: {
                n = bytecodeloader(luaCallFrame, n);
                break;
            }
        }
        return n;
    }
    
    @Override
    public String toString() {
        return BaseLib.names[this.index];
    }
}
