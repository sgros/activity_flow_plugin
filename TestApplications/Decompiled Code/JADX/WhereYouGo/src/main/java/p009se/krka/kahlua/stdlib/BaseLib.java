package p009se.krka.kahlua.stdlib;

import locus.api.android.features.geocaching.fieldNotes.FieldNotesHelper.ColFieldNote;
import p009se.krka.kahlua.p010vm.JavaFunction;
import p009se.krka.kahlua.p010vm.LuaCallFrame;
import p009se.krka.kahlua.p010vm.LuaClosure;
import p009se.krka.kahlua.p010vm.LuaException;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.p010vm.LuaThread;

/* renamed from: se.krka.kahlua.stdlib.BaseLib */
public final class BaseLib implements JavaFunction {
    private static final int BYTECODELOADER = 18;
    private static final int COLLECTGARBAGE = 16;
    private static final int DEBUGSTACKTRACE = 17;
    private static final Object DOUBLE_ONE = new Double(1.0d);
    private static final int ERROR = 8;
    private static final int GETFENV = 12;
    private static final int GETMETATABLE = 6;
    public static final Object MODE_KEY = "__mode";
    private static final int NEXT = 10;
    private static final int NUM_FUNCTIONS = 19;
    private static final int PCALL = 0;
    private static final int PRINT = 1;
    private static final int RAWEQUAL = 13;
    private static final int RAWGET = 15;
    private static final int RAWSET = 14;
    private static final Runtime RUNTIME = Runtime.getRuntime();
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
    private static final String[] names = new String[19];
    private int index;

    static {
        names[0] = "pcall";
        names[1] = "print";
        names[2] = "select";
        names[3] = ColFieldNote.TYPE;
        names[4] = "tostring";
        names[5] = "tonumber";
        names[6] = "getmetatable";
        names[7] = "setmetatable";
        names[8] = "error";
        names[9] = "unpack";
        names[10] = "next";
        names[11] = "setfenv";
        names[12] = "getfenv";
        names[13] = "rawequal";
        names[14] = "rawset";
        names[15] = "rawget";
        names[16] = "collectgarbage";
        names[17] = "debugstacktrace";
        names[18] = "bytecodeloader";
    }

    public BaseLib(int index) {
        this.index = index;
    }

    public static void register(LuaState state) {
        BaseLib.initFunctions();
        for (int i = 0; i < 19; i++) {
            state.getEnvironment().rawset(names[i], functions[i]);
        }
    }

    private static synchronized void initFunctions() {
        synchronized (BaseLib.class) {
            if (functions == null) {
                functions = new BaseLib[19];
                for (int i = 0; i < 19; i++) {
                    functions[i] = new BaseLib(i);
                }
            }
        }
    }

    public String toString() {
        return names[this.index];
    }

    public int call(LuaCallFrame callFrame, int nArguments) {
        switch (this.index) {
            case 0:
                return BaseLib.pcall(callFrame, nArguments);
            case 1:
                return BaseLib.print(callFrame, nArguments);
            case 2:
                return BaseLib.select(callFrame, nArguments);
            case 3:
                return BaseLib.type(callFrame, nArguments);
            case 4:
                return BaseLib.tostring(callFrame, nArguments);
            case 5:
                return BaseLib.tonumber(callFrame, nArguments);
            case 6:
                return BaseLib.getmetatable(callFrame, nArguments);
            case 7:
                return BaseLib.setmetatable(callFrame, nArguments);
            case 8:
                return error(callFrame, nArguments);
            case 9:
                return unpack(callFrame, nArguments);
            case 10:
                return next(callFrame, nArguments);
            case 11:
                return setfenv(callFrame, nArguments);
            case 12:
                return getfenv(callFrame, nArguments);
            case 13:
                return rawequal(callFrame, nArguments);
            case 14:
                return rawset(callFrame, nArguments);
            case 15:
                return rawget(callFrame, nArguments);
            case 16:
                return BaseLib.collectgarbage(callFrame, nArguments);
            case 17:
                return debugstacktrace(callFrame, nArguments);
            case 18:
                return BaseLib.bytecodeloader(callFrame, nArguments);
            default:
                return 0;
        }
    }

    private int debugstacktrace(LuaCallFrame callFrame, int nArguments) {
        LuaThread thread = (LuaThread) BaseLib.getOptArg(callFrame, 1, TYPE_THREAD);
        if (thread == null) {
            thread = callFrame.thread;
        }
        Double levelDouble = (Double) BaseLib.getOptArg(callFrame, 2, TYPE_NUMBER);
        int level = 0;
        if (levelDouble != null) {
            level = levelDouble.intValue();
        }
        Double countDouble = (Double) BaseLib.getOptArg(callFrame, 3, TYPE_NUMBER);
        int count = Integer.MAX_VALUE;
        if (countDouble != null) {
            count = countDouble.intValue();
        }
        Double haltAtDouble = (Double) BaseLib.getOptArg(callFrame, 4, TYPE_NUMBER);
        int haltAt = 0;
        if (haltAtDouble != null) {
            haltAt = haltAtDouble.intValue();
        }
        return callFrame.push(thread.getCurrentStackTrace(level, count, haltAt));
    }

    private int rawget(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 2) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        callFrame.push(((LuaTable) callFrame.get(0)).rawget(callFrame.get(1)));
        return 1;
    }

    private int rawset(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 3) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        ((LuaTable) callFrame.get(0)).rawset(callFrame.get(1), callFrame.get(2));
        callFrame.setTop(1);
        return 1;
    }

    private int rawequal(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 2) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        callFrame.push(BaseLib.toBoolean(LuaState.luaEquals(callFrame.get(0), callFrame.get(1))));
        return 1;
    }

    private static final Boolean toBoolean(boolean b) {
        if (b) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private int setfenv(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        LuaClosure closure;
        if (nArguments >= 2) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        LuaTable newEnv = (LuaTable) callFrame.get(1);
        if (newEnv != null) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "expected a table");
        Double o = callFrame.get(0);
        if (o instanceof LuaClosure) {
            closure = (LuaClosure) o;
        } else {
            o = BaseLib.rawTonumber(o);
            if (o != null) {
                z = true;
            } else {
                z = false;
            }
            BaseLib.luaAssert(z, "expected a lua function or a number");
            int level = o.intValue();
            if (level == 0) {
                callFrame.thread.environment = newEnv;
                return 0;
            }
            LuaCallFrame parentCallFrame = callFrame.thread.getParent(level);
            if (!parentCallFrame.isLua()) {
                BaseLib.fail("No closure found at this level: " + level);
            }
            closure = parentCallFrame.closure;
        }
        closure.env = newEnv;
        callFrame.setTop(1);
        return 1;
    }

    private int getfenv(LuaCallFrame callFrame, int nArguments) {
        Object res;
        boolean z = false;
        LuaClosure o = DOUBLE_ONE;
        if (nArguments >= 1) {
            o = callFrame.get(0);
        }
        if (o == null || (o instanceof JavaFunction)) {
            res = callFrame.thread.environment;
        } else if (o instanceof LuaClosure) {
            res = o.env;
        } else {
            boolean z2;
            Double d = BaseLib.rawTonumber(o);
            if (d != null) {
                z2 = true;
            } else {
                z2 = false;
            }
            BaseLib.luaAssert(z2, "Expected number");
            int level = d.intValue();
            if (level >= 0) {
                z = true;
            }
            BaseLib.luaAssert(z, "level must be non-negative");
            res = callFrame.thread.getParent(level).getEnvironment();
        }
        callFrame.push(res);
        return 1;
    }

    private int next(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 1) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        LuaTable t = (LuaTable) callFrame.get(0);
        Object key = null;
        if (nArguments >= 2) {
            key = callFrame.get(1);
        }
        Object nextKey = t.next(key);
        if (nextKey == null) {
            callFrame.setTop(1);
            callFrame.set(0, null);
            return 1;
        }
        Object value = t.rawget(nextKey);
        callFrame.setTop(2);
        callFrame.set(0, nextKey);
        callFrame.set(1, value);
        return 2;
    }

    private int unpack(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        int i;
        int j;
        if (nArguments >= 1) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        LuaTable t = (LuaTable) callFrame.get(0);
        Object di = null;
        Object dj = null;
        if (nArguments >= 2) {
            di = callFrame.get(1);
        }
        if (nArguments >= 3) {
            dj = callFrame.get(2);
        }
        if (di != null) {
            i = (int) LuaState.fromDouble(di);
        } else {
            i = 1;
        }
        if (dj != null) {
            j = (int) LuaState.fromDouble(dj);
        } else {
            j = t.len();
        }
        int nReturnValues = (j + 1) - i;
        if (nReturnValues <= 0) {
            callFrame.setTop(0);
            return 0;
        }
        callFrame.setTop(nReturnValues);
        for (int b = 0; b < nReturnValues; b++) {
            callFrame.set(b, t.rawget(LuaState.toDouble((long) (i + b))));
        }
        return nReturnValues;
    }

    private int error(LuaCallFrame callFrame, int nArguments) {
        if (nArguments < 1) {
            return 0;
        }
        String stacktrace = (String) BaseLib.getOptArg(callFrame, 2, TYPE_STRING);
        if (stacktrace == null) {
            stacktrace = "";
        }
        callFrame.thread.stackTrace = stacktrace;
        throw new LuaException(callFrame.get(0));
    }

    public static int pcall(LuaCallFrame callFrame, int nArguments) {
        return callFrame.thread.state.pcall(nArguments - 1);
    }

    private static int print(LuaCallFrame callFrame, int nArguments) {
        LuaState state = callFrame.thread.state;
        Object toStringFun = state.tableGet(state.getEnvironment(), "tostring");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < nArguments; i++) {
            if (i > 0) {
                sb.append("\t");
            }
            sb.append(state.call(toStringFun, callFrame.get(i), null, null));
        }
        state.getOut().println(sb.toString());
        return 0;
    }

    private static int select(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 1) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        Object arg1 = callFrame.get(0);
        if ((arg1 instanceof String) && ((String) arg1).startsWith("#")) {
            callFrame.push(LuaState.toDouble((long) (nArguments - 1)));
            return 1;
        }
        int index = (int) LuaState.fromDouble(BaseLib.rawTonumber(arg1));
        if (index < 1 || index > nArguments - 1) {
            return 0;
        }
        return nArguments - index;
    }

    public static void luaAssert(boolean b, String msg) {
        if (!b) {
            BaseLib.fail(msg);
        }
    }

    public static void fail(String msg) {
        throw new RuntimeException(msg);
    }

    public static String numberToString(Double num) {
        if (num.isNaN()) {
            return "nan";
        }
        if (!num.isInfinite()) {
            double n = num.doubleValue();
            if (Math.floor(n) != n || Math.abs(n) >= 1.0E14d) {
                return num.toString();
            }
            return String.valueOf(num.longValue());
        } else if (MathLib.isNegative(num.doubleValue())) {
            return "-inf";
        } else {
            return "inf";
        }
    }

    public static Object getArg(LuaCallFrame callFrame, int n, String type, String function) {
        String o = callFrame.get(n - 1);
        if (o == null) {
            throw new RuntimeException("bad argument #" + n + "to '" + function + "' (" + type + " expected, got no value)");
        }
        if (type == TYPE_STRING) {
            String res = BaseLib.rawTostring(o);
            if (res != null) {
                return res;
            }
        } else if (type == TYPE_NUMBER) {
            Double d = BaseLib.rawTonumber(o);
            if (d != null) {
                return d;
            }
            throw new RuntimeException("bad argument #" + n + " to '" + function + "' (number expected, got string)");
        }
        if (type != null) {
            String isType = BaseLib.type(o);
            if (type != isType) {
                BaseLib.fail("bad argument #" + n + " to '" + function + "' (" + type + " expected, got " + isType + ")");
            }
        }
        return o;
    }

    public static Object getOptArg(LuaCallFrame callFrame, int n, String type) {
        if (n - 1 >= callFrame.getTop()) {
            return null;
        }
        Object o = callFrame.get(n - 1);
        if (o == null) {
            return null;
        }
        if (type == TYPE_STRING) {
            return BaseLib.rawTostring(o);
        }
        if (type == TYPE_NUMBER) {
            return BaseLib.rawTonumber(o);
        }
        return o;
    }

    private static int getmetatable(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 1) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        callFrame.push(callFrame.thread.state.getmetatable(callFrame.get(0), false));
        return 1;
    }

    private static int setmetatable(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 2) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        BaseLib.setmetatable(callFrame.thread.state, callFrame.get(0), (LuaTable) callFrame.get(1), false);
        callFrame.setTop(1);
        return 1;
    }

    public static void setmetatable(LuaState state, Object o, LuaTable newMeta, boolean raw) {
        BaseLib.luaAssert(o != null, "Expected table, got nil");
        Object oldMeta = state.getmetatable(o, raw);
        if (raw || oldMeta == null || state.tableGet(oldMeta, "__metatable") == null) {
            state.setmetatable(o, newMeta);
            return;
        }
        throw new RuntimeException("Can not set metatable of protected object");
    }

    private static int type(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 1) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        callFrame.push(BaseLib.type(callFrame.get(0)));
        return 1;
    }

    public static String type(Object o) {
        if (o == null) {
            return TYPE_NIL;
        }
        if (o instanceof String) {
            return TYPE_STRING;
        }
        if (o instanceof Double) {
            return TYPE_NUMBER;
        }
        if (o instanceof Boolean) {
            return TYPE_BOOLEAN;
        }
        if ((o instanceof JavaFunction) || (o instanceof LuaClosure)) {
            return TYPE_FUNCTION;
        }
        if (o instanceof LuaTable) {
            return TYPE_TABLE;
        }
        if (o instanceof LuaThread) {
            return TYPE_THREAD;
        }
        return TYPE_USERDATA;
    }

    private static int tostring(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 1) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        callFrame.push(BaseLib.tostring(callFrame.get(0), callFrame.thread.state));
        return 1;
    }

    public static String tostring(Object o, LuaState state) {
        if (o == null) {
            return TYPE_NIL;
        }
        if (o instanceof String) {
            return (String) o;
        }
        if (o instanceof Double) {
            return BaseLib.rawTostring(o);
        }
        if (o instanceof Boolean) {
            return o == Boolean.TRUE ? "true" : "false";
        } else if (o instanceof JavaFunction) {
            return "function 0x" + System.identityHashCode(o);
        } else {
            if (o instanceof LuaClosure) {
                return "function 0x" + System.identityHashCode(o);
            }
            Object tostringFun = state.getMetaOp(o, "__tostring");
            if (tostringFun != null) {
                return (String) state.call(tostringFun, o, null, null);
            }
            if (o instanceof LuaTable) {
                return "table 0x" + System.identityHashCode(o);
            }
            throw new RuntimeException("no __tostring found on object");
        }
    }

    private static int tonumber(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        boolean z2 = false;
        if (nArguments >= 1) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        String o = callFrame.get(0);
        if (nArguments == 1) {
            callFrame.push(BaseLib.rawTonumber(o));
        } else {
            String s = o;
            Double radixDouble = BaseLib.rawTonumber(callFrame.get(1));
            if (radixDouble != null) {
                z2 = true;
            }
            BaseLib.luaAssert(z2, "Argument 2 must be a number");
            double dradix = LuaState.fromDouble(radixDouble);
            int radix = (int) dradix;
            if (((double) radix) != dradix) {
                throw new RuntimeException("base is not an integer");
            }
            callFrame.push(BaseLib.tonumber(s, radix));
        }
        return 1;
    }

    public static Double tonumber(String s) {
        return BaseLib.tonumber(s, 10);
    }

    public static Double tonumber(String s, int radix) {
        if (radix < 2 || radix > 36) {
            throw new RuntimeException("base out of range");
        } else if (radix != 10) {
            return LuaState.toDouble((long) Integer.parseInt(s, radix));
        } else {
            try {
                return Double.valueOf(s);
            } catch (NumberFormatException e) {
                s = s.toLowerCase();
                if (s.endsWith("nan")) {
                    return LuaState.toDouble(Double.NaN);
                }
                if (!s.endsWith("inf")) {
                    return null;
                }
                if (s.charAt(0) == '-') {
                    return LuaState.toDouble(Double.NEGATIVE_INFINITY);
                }
                return LuaState.toDouble(Double.POSITIVE_INFINITY);
            }
        }
    }

    public static int collectgarbage(LuaCallFrame callFrame, int nArguments) {
        Object option = null;
        if (nArguments > 0) {
            option = callFrame.get(0);
        }
        if (option == null || option.equals("step") || option.equals("collect")) {
            System.gc();
            return 0;
        } else if (option.equals("count")) {
            long freeMemory = RUNTIME.freeMemory();
            long totalMemory = RUNTIME.totalMemory();
            callFrame.setTop(3);
            callFrame.set(0, BaseLib.toKiloBytes(totalMemory - freeMemory));
            callFrame.set(1, BaseLib.toKiloBytes(freeMemory));
            callFrame.set(2, BaseLib.toKiloBytes(totalMemory));
            return 3;
        } else {
            throw new RuntimeException("invalid option: " + option);
        }
    }

    private static Double toKiloBytes(long freeMemory) {
        return LuaState.toDouble(((double) freeMemory) / 1024.0d);
    }

    public static String rawTostring(Object o) {
        if (o instanceof String) {
            return (String) o;
        }
        if (o instanceof Double) {
            return BaseLib.numberToString((Double) o);
        }
        return null;
    }

    public static Double rawTonumber(Object o) {
        if (o instanceof Double) {
            return (Double) o;
        }
        if (o instanceof String) {
            return BaseLib.tonumber((String) o);
        }
        return null;
    }

    private static int bytecodeloader(LuaCallFrame callFrame, int nArguments) {
        String modname = (String) BaseLib.getArg(callFrame, 1, TYPE_STRING, "loader");
        String classpath = (String) ((LuaTable) callFrame.getEnvironment().rawget("package")).rawget("classpath");
        int index = 0;
        while (index < classpath.length()) {
            int nextIndex = classpath.indexOf(";", index);
            if (nextIndex == -1) {
                nextIndex = classpath.length();
            }
            String path = classpath.substring(index, nextIndex);
            if (path.length() > 0) {
                if (!path.endsWith("/")) {
                    path = path + "/";
                }
                LuaClosure closure = callFrame.thread.state.loadByteCodeFromResource(path + modname, callFrame.getEnvironment());
                if (closure != null) {
                    return callFrame.push(closure);
                }
            }
            index = nextIndex;
        }
        return callFrame.push("Could not find the bytecode for '" + modname + "' in classpath");
    }
}
