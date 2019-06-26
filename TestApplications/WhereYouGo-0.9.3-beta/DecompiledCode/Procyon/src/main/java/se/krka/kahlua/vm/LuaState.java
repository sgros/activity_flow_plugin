// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.vm;

import se.krka.kahlua.stdlib.TableLib;
import se.krka.kahlua.stdlib.OsLib;
import se.krka.kahlua.stdlib.StringLib;
import java.io.InputStream;
import java.io.IOException;
import se.krka.kahlua.stdlib.MathLib;
import se.krka.kahlua.stdlib.CoroutineLib;
import se.krka.kahlua.stdlib.BaseLib;
import java.util.Random;
import java.io.PrintStream;

public class LuaState
{
    public static final int FIELDS_PER_FLUSH = 50;
    static final int MAX_INDEX_RECURSION = 100;
    public static final int OP_ADD = 12;
    public static final int OP_CALL = 28;
    public static final int OP_CLOSE = 35;
    public static final int OP_CLOSURE = 36;
    public static final int OP_CONCAT = 21;
    public static final int OP_DIV = 15;
    public static final int OP_EQ = 23;
    public static final int OP_FORLOOP = 31;
    public static final int OP_FORPREP = 32;
    public static final int OP_GETGLOBAL = 5;
    public static final int OP_GETTABLE = 6;
    public static final int OP_GETUPVAL = 4;
    public static final int OP_JMP = 22;
    public static final int OP_LE = 25;
    public static final int OP_LEN = 20;
    public static final int OP_LOADBOOL = 2;
    public static final int OP_LOADK = 1;
    public static final int OP_LOADNIL = 3;
    public static final int OP_LT = 24;
    public static final int OP_MOD = 16;
    public static final int OP_MOVE = 0;
    public static final int OP_MUL = 14;
    public static final int OP_NEWTABLE = 10;
    public static final int OP_NOT = 19;
    public static final int OP_POW = 17;
    public static final int OP_RETURN = 30;
    public static final int OP_SELF = 11;
    public static final int OP_SETGLOBAL = 7;
    public static final int OP_SETLIST = 34;
    public static final int OP_SETTABLE = 9;
    public static final int OP_SETUPVAL = 8;
    public static final int OP_SUB = 13;
    public static final int OP_TAILCALL = 29;
    public static final int OP_TEST = 26;
    public static final int OP_TESTSET = 27;
    public static final int OP_TFORLOOP = 33;
    public static final int OP_UNM = 18;
    public static final int OP_VARARG = 37;
    private static final String[] meta_ops;
    private final LuaTable classMetatables;
    public LuaThread currentThread;
    protected final PrintStream out;
    public final Random random;
    private final LuaTable userdataMetatables;
    
    static {
        (meta_ops = new String[38])[12] = "__add";
        LuaState.meta_ops[13] = "__sub";
        LuaState.meta_ops[14] = "__mul";
        LuaState.meta_ops[15] = "__div";
        LuaState.meta_ops[16] = "__mod";
        LuaState.meta_ops[17] = "__pow";
        LuaState.meta_ops[23] = "__eq";
        LuaState.meta_ops[24] = "__lt";
        LuaState.meta_ops[25] = "__le";
    }
    
    public LuaState() {
        this(System.out, true);
    }
    
    public LuaState(final PrintStream printStream) {
        this(printStream, true);
    }
    
    protected LuaState(final PrintStream out, final boolean b) {
        this.random = new Random();
        final LuaTableImpl metatable = new LuaTableImpl();
        metatable.rawset("__mode", "k");
        (this.userdataMetatables = new LuaTableImpl()).setMetatable(metatable);
        this.classMetatables = new LuaTableImpl();
        this.out = out;
        if (b) {
            this.reset();
        }
    }
    
    public static boolean boolEval(final Object o) {
        return o != null && o != Boolean.FALSE;
    }
    
    private int callJava(final JavaFunction javaFunction, int n, final int n2, int top) {
        final LuaThread currentThread = this.currentThread;
        final LuaCallFrame pushNewCallFrame = currentThread.pushNewCallFrame(null, javaFunction, n, n2, top, false, false);
        final int call = javaFunction.call(pushNewCallFrame, top);
        top = pushNewCallFrame.getTop();
        n = n2 - n;
        pushNewCallFrame.stackCopy(top - call, n, call);
        pushNewCallFrame.setTop(call + n);
        currentThread.popCallFrame();
        return call;
    }
    
    public static double fromDouble(final Object o) {
        return (double)o;
    }
    
    private static final int getA8(final int n) {
        return n >>> 6 & 0xFF;
    }
    
    private static final int getB9(final int n) {
        return n >>> 23 & 0x1FF;
    }
    
    private final Object getBinMetaOp(Object o, final Object o2, final String s) {
        o = this.getMetaOp(o, s);
        if (o == null) {
            o = this.getMetaOp(o2, s);
        }
        return o;
    }
    
    private static final int getBx(final int n) {
        return n >>> 14;
    }
    
    private static final int getC9(final int n) {
        return n >>> 14 & 0x1FF;
    }
    
    private final Object getCompMetaOp(Object rawget, final Object o, final String s) {
        final LuaTable luaTable = (LuaTable)this.getmetatable(rawget, true);
        if (luaTable != this.getmetatable(o, true) || luaTable == null) {
            rawget = null;
        }
        else {
            rawget = luaTable.rawget(s);
        }
        return rawget;
    }
    
    private final Object getRegisterOrConstant(final LuaCallFrame luaCallFrame, final int n, final LuaPrototype luaPrototype) {
        final int n2 = n - 256;
        Object value;
        if (n2 < 0) {
            value = luaCallFrame.get(n);
        }
        else {
            value = luaPrototype.constants[n2];
        }
        return value;
    }
    
    private static final int getSBx(final int n) {
        return (n >>> 14) - 131071;
    }
    
    public static boolean luaEquals(final Object o, final Object o2) {
        boolean b = true;
        if (o == null || o2 == null) {
            if (o != o2) {
                b = false;
            }
        }
        else if (o instanceof Double && o2 instanceof Double) {
            if ((double)o != (double)o2) {
                b = false;
            }
        }
        else if (o != o2) {
            b = false;
        }
        return b;
    }
    
    private final void luaMainloop() {
        LuaCallFrame luaCallFrame = this.currentThread.currentCallFrame();
        LuaClosure luaClosure = luaCallFrame.closure;
        LuaPrototype luaPrototype = luaClosure.prototype;
        int[] array = luaPrototype.code;
        int n = luaCallFrame.returnBase;
        LuaClosure luaClosure2;
        int[] array2;
        LuaPrototype luaPrototype2;
        int n2;
        int pc;
        int n3;
        int n4;
        LuaCallFrame currentCallFrame;
        boolean b;
        LuaThread parent;
        LuaCallFrame currentCallFrame2;
        int compareTo;
        int a8;
        int n5;
        LuaCallFrame currentCallFrame3;
        Object prepareMetatableCall;
        Object registerOrConstant;
        Object registerOrConstant2;
        Object compMetaOp;
        Object o;
        int n6;
        Object o2;
        Boolean b2;
        int n7;
        int a9;
        LuaPrototype luaPrototype3;
        LuaClosure luaClosure3;
        int numUpvalues;
        int n8;
        int a10;
        int b3;
        int c9;
        Object value;
        int a11;
        int b4;
        boolean b5;
        int n9;
        int a12;
        int sBx;
        Object value2;
        Object o3;
        int n10;
        int n11;
        double fromDouble;
        double fromDouble2;
        Double rawTonumber;
        Double rawTonumber2;
        Object o4;
        int n12;
        int n13;
        Object prepareMetatableCall2;
        int n14;
        int n15;
        int n16;
        LuaCallFrame pushNewCallFrame;
        int n17;
        LuaThread currentThread;
        Object value3;
        Object metaOp;
        Object o5;
        int n18;
        double n19;
        double fromDouble3;
        int n20;
        Object value4;
        Object string;
        Object binMetaOp;
        boolean b6;
        int n21;
        int n22;
        int nArguments;
        int localBase;
        int a13;
        double fromDouble4;
        double fromDouble5;
        Double double1;
        int n23;
        int n24;
        Object value5;
        StringBuffer sb;
        int n25;
        int n26;
        int n27;
        int c10;
        boolean b7;
        int b8;
        int a14;
        int b9;
        int c11;
        int a15;
        int b10;
        int c12;
        Object registerOrConstant3;
        Object registerOrConstant4;
        Object o6;
        String rawTostring;
        LuaCallFrame currentCallFrame4;
        LuaPrototype prototype;
        LuaClosure closure;
        int[] code;
        int returnBase;
        int a16;
        Object binMetaOp2;
        int a17;
        Object value6;
        LuaCallFrame luaCallFrame2;
        int c13;
        int n28;
        Double rawTonumber3;
        LuaTable luaTable;
        int n29;
        String str;
        String str2;
        LuaCallFrame currentCallFrame5;
        LuaPrototype prototype2;
        LuaClosure closure2;
        int[] code2;
        int returnBase2;
        int a18;
        int n30;
        int n31;
        LuaThread currentThread2;
        int a19;
        int c14;
        Object value7;
        int a20;
        int b11;
        Object registerOrConstant5;
        Object value8;
        int n32;
        Object value9;
        int b12;
        int c15;
        int a21;
        int b13;
        int c16;
        int n33;
        int a22;
        int a23;
        int localBase2;
        int n34;
        Object compMetaOp2;
        int localBase3;
        int b14;
        Label_0449_Outer:Block_90_Outer:Label_3340_Outer:Block_75_Outer:
        while (true) {
            luaClosure2 = luaClosure;
            array2 = array;
            luaPrototype2 = luaPrototype;
            n2 = n;
            Label_8133: {
                try {
                    pc = luaCallFrame.pc;
                    luaClosure2 = luaClosure;
                    array2 = array;
                    luaPrototype2 = luaPrototype;
                    n2 = n;
                    luaCallFrame.pc = pc + 1;
                    n3 = array[pc];
                    n4 = (n3 & 0x3F);
                    switch (n4) {
                        default: {
                            continue;
                        }
                        case 0: {
                            luaClosure2 = luaClosure;
                            array2 = array;
                            luaPrototype2 = luaPrototype;
                            n2 = n;
                            luaCallFrame.set(getA8(n3), luaCallFrame.get(getB9(n3)));
                            continue;
                        }
                        case 1: {
                            break Label_8133;
                        }
                        case 2: {
                            break Label_8133;
                        }
                        case 3: {
                            break Label_8133;
                        }
                        case 4: {
                            break Label_8133;
                        }
                        case 5: {
                            break Label_8133;
                        }
                        case 6: {
                            break Label_8133;
                        }
                        case 7: {
                            break Label_8133;
                        }
                        case 8: {
                            break Label_8133;
                        }
                        case 9: {
                            break Label_8133;
                        }
                        case 10: {
                            break Label_8133;
                        }
                        case 11: {
                            break Label_8133;
                        }
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17: {
                            break Label_8133;
                        }
                        case 18: {
                            break Label_8133;
                        }
                        case 19: {
                            break Label_8133;
                        }
                        case 20: {
                            break Label_8133;
                        }
                        case 21: {
                            break Label_8133;
                        }
                        case 22: {
                            break Label_8133;
                        }
                        case 23:
                        case 24:
                        case 25: {
                            break Label_8133;
                        }
                        case 26: {
                            break Label_8133;
                        }
                        case 27: {
                            break Label_8133;
                        }
                        case 28: {
                            break Label_8133;
                        }
                        case 29: {
                            break Label_8133;
                        }
                        case 30: {
                            break Label_8133;
                        }
                        case 32: {
                            break Label_8133;
                        }
                        case 31: {
                            break Label_8133;
                        }
                        case 33: {
                            break Label_8133;
                        }
                        case 34: {
                            break Label_8133;
                        }
                        case 35: {
                            break Label_8133;
                        }
                        case 36: {
                            break Label_8133;
                        }
                        case 37: {
                            break Label_8133;
                        }
                    }
                }
                catch (RuntimeException ex) {
                    while (true) {
                        currentCallFrame = this.currentThread.currentCallFrame();
                        if (currentCallFrame.isLua()) {
                            break;
                        }
                        this.currentThread.addStackTrace(currentCallFrame);
                        this.currentThread.popCallFrame();
                    }
                    b = true;
                    while (true) {
                        do {
                            luaCallFrame = this.currentThread.currentCallFrame();
                            if (luaCallFrame == null) {
                                parent = this.currentThread.parent;
                                luaClosure = luaClosure2;
                                array = array2;
                                luaPrototype = luaPrototype2;
                                n = n2;
                                if (parent != null) {
                                    this.currentThread.parent = null;
                                    currentCallFrame2 = parent.currentCallFrame();
                                    currentCallFrame2.push(Boolean.FALSE);
                                    currentCallFrame2.push(ex.getMessage());
                                    currentCallFrame2.push(this.currentThread.stackTrace);
                                    this.currentThread.state.currentThread = parent;
                                    this.currentThread = parent;
                                    luaCallFrame = this.currentThread.currentCallFrame();
                                    luaClosure = luaCallFrame.closure;
                                    luaPrototype = luaClosure.prototype;
                                    array = luaPrototype.code;
                                    n = luaCallFrame.returnBase;
                                    b = false;
                                }
                                if (luaCallFrame != null) {
                                    luaCallFrame.closeUpvalues(0);
                                }
                                if (b) {
                                    throw ex;
                                }
                                continue Label_0449_Outer;
                            }
                            else {
                                this.currentThread.addStackTrace(luaCallFrame);
                                this.currentThread.popCallFrame();
                            }
                        } while (luaCallFrame.fromLua);
                        luaClosure = luaClosure2;
                        array = array2;
                        luaPrototype = luaPrototype2;
                        n = n2;
                        continue Block_90_Outer;
                    }
                Label_3614:
                    while (true) {
                        Label_5585: {
                        Label_3340:
                            while (true) {
                                Block_34: {
                                Block_52_Outer:
                                    while (true) {
                                        while (true) {
                                            Block_76_Outer:Block_53_Outer:Label_7718_Outer:
                                            while (true) {
                                                Block_89: {
                                                    Label_7718:Block_47_Outer:Block_29_Outer:
                                                    while (true) {
                                                        Block_92: {
                                                            while (true) {
                                                                Label_3275: {
                                                                    while (true) {
                                                                        Label_3211: {
                                                                            Label_3283_Outer:Block_50_Outer:
                                                                            while (true) {
                                                                                Label_3622: {
                                                                                    Label_1832: {
                                                                                        while (true) {
                                                                                            Label_4430: {
                                                                                            Label_3568_Outer:
                                                                                                while (true) {
                                                                                                    Label_3686: {
                                                                                                    Label_3568:
                                                                                                        while (true) {
                                                                                                        Label_3283:
                                                                                                            while (true) {
                                                                                                                Label_3560_Outer:Block_39_Outer:Block_79_Outer:
                                                                                                                while (true) {
                                                                                                                    while (true) {
                                                                                                                        Block_77: {
                                                                                                                            Label_1983: {
                                                                                                                                Label_6158: {
                                                                                                                                    while (true) {
                                                                                                                                    Block_63:
                                                                                                                                        while (true) {
                                                                                                                                            Block_67: {
                                                                                                                                            Label_2229:
                                                                                                                                                while (true) {
                                                                                                                                                Block_15:
                                                                                                                                                    while (true) {
                                                                                                                                                        Block_38: {
                                                                                                                                                        Label_2647_Outer:
                                                                                                                                                            while (true) {
                                                                                                                                                                Label_2533: {
                                                                                                                                                                    Label_7195: {
                                                                                                                                                                    Label_2647:
                                                                                                                                                                        while (true) {
                                                                                                                                                                            Block_20: {
                                                                                                                                                                            Label_3895_Outer:
                                                                                                                                                                                while (true) {
                                                                                                                                                                                    Label_2745: {
                                                                                                                                                                                    Label_3895:
                                                                                                                                                                                        while (true) {
                                                                                                                                                                                            Label_3991: {
                                                                                                                                                                                                Label_2485:Label_7690_Outer:
                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                Label_2896:
                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                        Block_91: {
                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                Block_56: {
                                                                                                                                                                                                                    Label_3560:Block_12_Outer:
                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                        Block_43: {
                                                                                                                                                                                                                            Label_4650: {
                                                                                                                                                                                                                            Block_9_Outer:
                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                    Block_44: {
                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                            Label_0625: {
                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                    Block_31_Outer:Label_5748_Outer:
                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                    Label_5748:
                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                            Block_82: {
                                                                                                                                                                                                                                                            Block_49_Outer:
                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                    Block_30: {
                                                                                                                                                                                                                                                                    Block_28:
                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                            Block_84_Outer:Block_87_Outer:
                                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                                    Label_6669: {
                                                                                                                                                                                                                                                                                        Block_36: {
                                                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                                                                Label_5925: {
                                                                                                                                                                                                                                                                                                    Block_86: {
                                                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                                                            Label_6435: {
                                                                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                                                                    Block_10: {
                                                                                                                                                                                                                                                                                                                    Label_3630:
                                                                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                                                                                                Block_73: {
                                                                                                                                                                                                                                                                                                                                Label_7956:
                                                                                                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                                                                                                        Label_8064: {
                                                                                                                                                                                                                                                                                                                                            Block_83: {
                                                                                                                                                                                                                                                                                                                                            Label_3348_Outer:
                                                                                                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                                                                                                    Block_23: {
                                                                                                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                                                                                                            Block_51: {
                                                                                                                                                                                                                                                                                                                                                            Label_3203:
                                                                                                                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                                                                                                                    Block_27: {
                                                                                                                                                                                                                                                                                                                                                                        Label_2587: {
                                                                                                                                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                                                                                                                                                Block_11: {
                                                                                                                                                                                                                                                                                                                                                                                Label_3348:
                                                                                                                                                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                                                                                                                                            Label_7276: {
                                                                                                                                                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                                                                                                                                                                        Block_35: {
                                                                                                                                                                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                                                                                                                                                                                Block_41: {
                                                                                                                                                                                                                                                                                                                                                                                                                    Label_7632: {
                                                                                                                                                                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                                                                                                                                                                            Block_81: {
                                                                                                                                                                                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                                                                                                                                                                                Label_3694:
                                                                                                                                                                                                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                                                                                                                                                                                                        Block_55: {
                                                                                                                                                                                                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                Block_48: {
                                                                                                                                                                                                                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                        Block_17: {
                                                                                                                                                                                                                                                                                                                                                                                                                                                            Block_62: {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Block_60: {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Block_46: {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            break Block_46;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            n5 = luaCallFrame.getTop() - a8 - 1;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            break Label_7632;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            Label_6209:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            break Block_81;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            BaseLib.fail("Tried to call a non-function: " + prepareMetatableCall);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            break Label_5925;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            Label_4974:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            return;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            break Label_3686;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            compMetaOp = this.getCompMetaOp(registerOrConstant, registerOrConstant2, "__lt");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            o = registerOrConstant;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            n6 = 1;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            o2 = registerOrConstant2;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Label_3860: {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Label_3860;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    b2 = Boolean.FALSE;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Label_0625;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    n7 = 1;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Label_3275;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    a9 = getA8(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaPrototype3 = luaPrototype.prototypes[getBx(n3)];
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaClosure3 = new LuaClosure(luaPrototype3, luaClosure.env);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaCallFrame.set(a9, luaClosure3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    numUpvalues = luaPrototype3.numUpvalues;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    n8 = 0;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Label_7956;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    a10 = getA8(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    b3 = getB9(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    c9 = getC9(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    value = luaCallFrame.get(b3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Block_62;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Block_44;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Label_8101:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaClosure3.upvalues[n8] = luaClosure.upvalues[b4];
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Label_8064;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Label_3251:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    b5 = false;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Label_3211;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    n9 = array[luaCallFrame.pc++];
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    b4 = getB9(n9);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    a12 = getA8(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    sBx = getSBx(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaCallFrame.set(a12, toDouble(fromDouble(luaCallFrame.get(a12)) - fromDouble(luaCallFrame.get(a12 + 2))));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaCallFrame.pc += sBx;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Block_41;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Label_2008:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    o3 = this.call(this.getMetaOp(value2, "__unm"), value2, null, null);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Label_1983;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Block_92;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Label_4030:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    n11 = 0;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Label_3694;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    fromDouble = fromDouble(registerOrConstant);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    fromDouble2 = fromDouble(registerOrConstant2);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Block_26: {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Block_26;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Label_8070:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        luaClosure3.upvalues[n8] = luaCallFrame.findUpvalue(b4);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Label_8064;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Block_60;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Block_43;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        luaCallFrame.pc += getSBx(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Label_1857:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        o4 = this.primitiveMath(rawTonumber, rawTonumber2, n4);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Label_1832;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Block_36;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        pushNewCallFrame = this.currentThread.pushNewCallFrame((LuaClosure)prepareMetatableCall2, null, n14, n15, n16, true, luaCallFrame.insideCoroutine);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        pushNewCallFrame.init();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        luaCallFrame = pushNewCallFrame;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        luaClosure = pushNewCallFrame.closure;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        luaPrototype = luaClosure.prototype;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        array = luaPrototype.code;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        n = luaCallFrame.returnBase;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        this.tableSet(luaCallFrame.get(getA8(n3)), this.getRegisterOrConstant(luaCallFrame, getB9(n3), luaPrototype), this.getRegisterOrConstant(luaCallFrame, getC9(n3), luaPrototype));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Block_51;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        this.currentThread.parent = currentThread.parent;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        currentThread.parent = null;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        this.currentThread.parent.currentCallFrame().push(Boolean.TRUE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Label_6158;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Label_2307:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        metaOp = this.getMetaOp(value3, "__len");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        BaseLib.luaAssert(metaOp != null, "__len not defined for operand");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        o5 = this.call(metaOp, value3, null, null);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Label_2229;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Block_27;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    n18 = 1;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Label_3630;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaCallFrame.set(getA8(n3), new LuaTableImpl());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Label_7195;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Label_7508:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaCallFrame.set(getA8(n3), luaPrototype.constants[getBx(n3)]);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaClosure = luaCallFrame.closure;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaPrototype = luaClosure.prototype;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    array = luaPrototype.code;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    n = luaCallFrame.returnBase;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                break Block_55;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Label_4024:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                n17 = 0;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                continue Block_53_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                value4 = luaCallFrame.get(n20);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                binMetaOp = this.getBinMetaOp(value4, string, "__concat");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                break Block_23;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Label_3245:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                b6 = false;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                break Label_3203;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                n21 = n;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                nArguments = n22 + 1;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                localBase = n21;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                break Label_5585;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                a13 = getA8(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                fromDouble4 = fromDouble(luaCallFrame.get(a13));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                fromDouble3 = fromDouble(luaCallFrame.get(a13 + 1));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                fromDouble5 = fromDouble(luaCallFrame.get(a13 + 2));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                n19 = fromDouble4 + fromDouble5;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                double1 = toDouble(n19);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                luaCallFrame.set(a13, double1);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                continue Label_5748_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            Label_7268:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            break Label_7276;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            break Block_48;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        n23 = 1;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Label_3622;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Block_35;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    value5 = luaCallFrame.get(n24);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    --n24;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Label_2587;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaCallFrame.set(getA8(n3), luaClosure.upvalues[getB9(n3)].getValue());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaCallFrame.pushVarargs(getA8(n3), getB9(n3) - 1);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    sb.append(BaseLib.rawTostring(luaCallFrame.get(n25)));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ++n25;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Label_2647;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Block_33: {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Block_33;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        n27 = 0;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        n24 = c10;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Label_2533;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        n17 = (b7 ? 1 : 0);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        break Block_56;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    break Block_17;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    a14 = getA8(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    b9 = getB9(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    c11 = getC9(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    continue Block_31_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                Label_6868:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                this.currentThread.popCallFrame();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                break Label_6669;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                Label_0679:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                b2 = Boolean.TRUE;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                break Label_0625;
                                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                                            luaCallFrame.set(a10, value);
                                                                                                                                                                                                                                                                                                                                                                                                                                                            continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                            a15 = getA8(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                            b10 = getB9(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                            c12 = getC9(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                            registerOrConstant3 = this.getRegisterOrConstant(luaCallFrame, b10, luaPrototype);
                                                                                                                                                                                                                                                                                                                                                                                                                                                            registerOrConstant4 = this.getRegisterOrConstant(luaCallFrame, c12, luaPrototype);
                                                                                                                                                                                                                                                                                                                                                                                                                                                            rawTonumber = BaseLib.rawTonumber(registerOrConstant3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                            break Block_10;
                                                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                                                        rawTostring = BaseLib.rawTostring(o6);
                                                                                                                                                                                                                                                                                                                                                                                                                                                        n20 = c10;
                                                                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_3895_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                    currentCallFrame4.setTop(prototype.maxStacksize);
                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaCallFrame = currentCallFrame4;
                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaClosure = closure;
                                                                                                                                                                                                                                                                                                                                                                                                                                                    array = code;
                                                                                                                                                                                                                                                                                                                                                                                                                                                    luaPrototype = prototype;
                                                                                                                                                                                                                                                                                                                                                                                                                                                    n = returnBase;
                                                                                                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                                                ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                                continue Label_2647_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                                        b7 = luaEquals(o2, o);
                                                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_3895;
                                                                                                                                                                                                                                                                                                                                                                                                                                        a16 = getA8(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                        b8 = getB9(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                        c10 = getC9(n3);
                                                                                                                                                                                                                                                                                                                                                                                                                                        o6 = luaCallFrame.get(c10);
                                                                                                                                                                                                                                                                                                                                                                                                                                        --c10;
                                                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_2485;
                                                                                                                                                                                                                                                                                                                                                                                                                                        n11 = 1;
                                                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_3694;
                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                    luaClosure.upvalues[getB9(n3)].setValue(luaCallFrame.get(getA8(n3)));
                                                                                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                    continue Block_79_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                                o4 = this.call(binMetaOp2, registerOrConstant3, registerOrConstant4, null);
                                                                                                                                                                                                                                                                                                                                                                                                                                break Label_1832;
                                                                                                                                                                                                                                                                                                                                                                                                                                luaCallFrame.restoreTop = false;
                                                                                                                                                                                                                                                                                                                                                                                                                                value6 = luaCallFrame.get(a17);
                                                                                                                                                                                                                                                                                                                                                                                                                                BaseLib.luaAssert(value6 != null, "Tried to call nil");
                                                                                                                                                                                                                                                                                                                                                                                                                                prepareMetatableCall = this.prepareMetatableCall(value6);
                                                                                                                                                                                                                                                                                                                                                                                                                                break Block_73;
                                                                                                                                                                                                                                                                                                                                                                                                                                n17 = 1;
                                                                                                                                                                                                                                                                                                                                                                                                                                break Label_3686;
                                                                                                                                                                                                                                                                                                                                                                                                                                ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                            luaCallFrame2 = (luaCallFrame = this.currentThread.currentCallFrame());
                                                                                                                                                                                                                                                                                                                                                                                                                            break Block_82;
                                                                                                                                                                                                                                                                                                                                                                                                                            localBase = n + 1;
                                                                                                                                                                                                                                                                                                                                                                                                                            nArguments = n22;
                                                                                                                                                                                                                                                                                                                                                                                                                            continue Label_3340_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                        luaCallFrame.closeUpvalues(getA8(n3));
                                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                    break Block_91;
                                                                                                                                                                                                                                                                                                                                                                                                                    o3 = toDouble(-fromDouble(rawTonumber3));
                                                                                                                                                                                                                                                                                                                                                                                                                    break Label_1983;
                                                                                                                                                                                                                                                                                                                                                                                                                    luaTable = (LuaTable)luaCallFrame.get(a8);
                                                                                                                                                                                                                                                                                                                                                                                                                    n10 = 1;
                                                                                                                                                                                                                                                                                                                                                                                                                    continue Label_7718;
                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                                continue Block_39_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                            Label_3329:
                                                                                                                                                                                                                                                                                                                                                                                                            break Block_34;
                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                        n13 = 1;
                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_3348;
                                                                                                                                                                                                                                                                                                                                                                                                        Label_3382:
                                                                                                                                                                                                                                                                                                                                                                                                        n12 = 0;
                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_3340;
                                                                                                                                                                                                                                                                                                                                                                                                        Label_4975:
                                                                                                                                                                                                                                                                                                                                                                                                        closure = currentCallFrame4.closure;
                                                                                                                                                                                                                                                                                                                                                                                                        prototype = closure.prototype;
                                                                                                                                                                                                                                                                                                                                                                                                        code = prototype.code;
                                                                                                                                                                                                                                                                                                                                                                                                        returnBase = currentCallFrame4.returnBase;
                                                                                                                                                                                                                                                                                                                                                                                                        luaCallFrame = currentCallFrame4;
                                                                                                                                                                                                                                                                                                                                                                                                        luaClosure = closure;
                                                                                                                                                                                                                                                                                                                                                                                                        array = code;
                                                                                                                                                                                                                                                                                                                                                                                                        luaPrototype = prototype;
                                                                                                                                                                                                                                                                                                                                                                                                        n = returnBase;
                                                                                                                                                                                                                                                                                                                                                                                                        continue Block_50_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                    break Block_28;
                                                                                                                                                                                                                                                                                                                                                                                                    n17 = 1;
                                                                                                                                                                                                                                                                                                                                                                                                    continue Block_53_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                luaCallFrame.localBase = localBase;
                                                                                                                                                                                                                                                                                                                                                                                                luaCallFrame.nArguments = nArguments;
                                                                                                                                                                                                                                                                                                                                                                                                luaCallFrame.closure = (LuaClosure)prepareMetatableCall;
                                                                                                                                                                                                                                                                                                                                                                                                luaCallFrame.init();
                                                                                                                                                                                                                                                                                                                                                                                                continue Label_5748;
                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                            luaCallFrame.clearFromIndex(a13);
                                                                                                                                                                                                                                                                                                                                                                                            continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                            n22 = luaCallFrame.getTop() - a17 - 1;
                                                                                                                                                                                                                                                                                                                                                                                            continue Block_49_Outer;
                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                        luaCallFrame.stackClear(getA8(n3), getB9(n3));
                                                                                                                                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                        Label_3317:
                                                                                                                                                                                                                                                                                                                                                                                        n7 = 0;
                                                                                                                                                                                                                                                                                                                                                                                        break Label_3275;
                                                                                                                                                                                                                                                                                                                                                                                        Label_3602:
                                                                                                                                                                                                                                                                                                                                                                                        n29 = 0;
                                                                                                                                                                                                                                                                                                                                                                                        continue Label_3560;
                                                                                                                                                                                                                                                                                                                                                                                        str = LuaState.meta_ops[n4];
                                                                                                                                                                                                                                                                                                                                                                                        binMetaOp2 = this.getBinMetaOp(registerOrConstant3, registerOrConstant4, str);
                                                                                                                                                                                                                                                                                                                                                                                        break Block_11;
                                                                                                                                                                                                                                                                                                                                                                                        Label_2695:
                                                                                                                                                                                                                                                                                                                                                                                        sb.append(rawTostring);
                                                                                                                                                                                                                                                                                                                                                                                        string = sb.toString();
                                                                                                                                                                                                                                                                                                                                                                                        n20 = c10 - n27;
                                                                                                                                                                                                                                                                                                                                                                                        break Label_2745;
                                                                                                                                                                                                                                                                                                                                                                                        Label_3388:
                                                                                                                                                                                                                                                                                                                                                                                        n13 = 0;
                                                                                                                                                                                                                                                                                                                                                                                        continue Label_3348;
                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                    luaCallFrame.set(getA8(n3), toBoolean(!boolEval(luaCallFrame.get(getB9(n3)))));
                                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                BaseLib.fail(str + " not defined for operands");
                                                                                                                                                                                                                                                                                                                                                                                continue Block_49_Outer;
                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                            break Block_86;
                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                        n20 = c10;
                                                                                                                                                                                                                                                                                                                                                                        string = o6;
                                                                                                                                                                                                                                                                                                                                                                        break Block_20;
                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                    b6 = true;
                                                                                                                                                                                                                                                                                                                                                                    continue Label_3203;
                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                BaseLib.fail(str2 + " not defined for operand");
                                                                                                                                                                                                                                                                                                                                                                break Label_3991;
                                                                                                                                                                                                                                                                                                                                                                currentCallFrame5.setTop(prototype2.maxStacksize);
                                                                                                                                                                                                                                                                                                                                                                luaCallFrame = currentCallFrame5;
                                                                                                                                                                                                                                                                                                                                                                luaClosure = closure2;
                                                                                                                                                                                                                                                                                                                                                                array = code2;
                                                                                                                                                                                                                                                                                                                                                                luaPrototype = prototype2;
                                                                                                                                                                                                                                                                                                                                                                n = returnBase2;
                                                                                                                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                            ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                            continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                            a18 = getA8(n3);
                                                                                                                                                                                                                                                                                                                                                            n30 = getB9(n3) - 1;
                                                                                                                                                                                                                                                                                                                                                            this.currentThread.closeUpvalues(luaCallFrame.localBase);
                                                                                                                                                                                                                                                                                                                                                            n31 = n30;
                                                                                                                                                                                                                                                                                                                                                            break Block_83;
                                                                                                                                                                                                                                                                                                                                                            Label_3915:
                                                                                                                                                                                                                                                                                                                                                            continue Block_87_Outer;
                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                    BaseLib.fail("__concat not defined for operands: " + value4 + " and " + string);
                                                                                                                                                                                                                                                                                                                                                    break Label_2896;
                                                                                                                                                                                                                                                                                                                                                    Label_3516:
                                                                                                                                                                                                                                                                                                                                                    compareTo = ((String)registerOrConstant).compareTo((String)registerOrConstant2);
                                                                                                                                                                                                                                                                                                                                                    continue Label_3348_Outer;
                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                Label_3394:
                                                                                                                                                                                                                                                                                                                                                break Block_38;
                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                            n31 = luaCallFrame.getTop() - a18;
                                                                                                                                                                                                                                                                                                                                            break Label_6435;
                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                        ++n8;
                                                                                                                                                                                                                                                                                                                                        continue Label_7956;
                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                    Label_2932:
                                                                                                                                                                                                                                                                                                                                    luaCallFrame.set(a16, o6);
                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                BaseLib.fail("Object " + value6 + " did not have __call metatable set");
                                                                                                                                                                                                                                                                                                                                continue Block_12_Outer;
                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                            Label_3670:
                                                                                                                                                                                                                                                                                                                            n18 = 0;
                                                                                                                                                                                                                                                                                                                            continue Label_3630;
                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                        ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                    rawTonumber2 = BaseLib.rawTonumber(registerOrConstant4);
                                                                                                                                                                                                                                                                                                                    continue Block_84_Outer;
                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                this.callJava((JavaFunction)prepareMetatableCall2, n14, n15, n16);
                                                                                                                                                                                                                                                                                                                currentCallFrame4 = this.currentThread.currentCallFrame();
                                                                                                                                                                                                                                                                                                                return;
                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                            this.currentThread.stackCopy(luaCallFrame.localBase + a18, n, n31);
                                                                                                                                                                                                                                                                                                            this.currentThread.setTop(n + n31);
                                                                                                                                                                                                                                                                                                            continue Block_87_Outer;
                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                        Label_2254:
                                                                                                                                                                                                                                                                                                        break Block_15;
                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                    luaCallFrame.localBase = luaCallFrame.returnBase;
                                                                                                                                                                                                                                                                                                    currentThread2 = this.currentThread;
                                                                                                                                                                                                                                                                                                    CoroutineLib.yieldHelper(luaCallFrame, luaCallFrame, n31);
                                                                                                                                                                                                                                                                                                    currentThread2.popCallFrame();
                                                                                                                                                                                                                                                                                                    break Label_6669;
                                                                                                                                                                                                                                                                                                    a19 = getA8(n3);
                                                                                                                                                                                                                                                                                                    c14 = getC9(n3);
                                                                                                                                                                                                                                                                                                    luaCallFrame.setTop(a19 + 6);
                                                                                                                                                                                                                                                                                                    luaCallFrame.stackCopy(a19, a19 + 3, 3);
                                                                                                                                                                                                                                                                                                    this.call(2);
                                                                                                                                                                                                                                                                                                    luaCallFrame.clearFromIndex(a19 + 3 + c14);
                                                                                                                                                                                                                                                                                                    luaCallFrame.setPrototypeStacksize();
                                                                                                                                                                                                                                                                                                    value7 = luaCallFrame.get(a19 + 3);
                                                                                                                                                                                                                                                                                                    break Block_89;
                                                                                                                                                                                                                                                                                                    Label_4303:
                                                                                                                                                                                                                                                                                                    ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                    Label_3257:
                                                                                                                                                                                                                                                                                                    break Block_30;
                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                currentThread = this.currentThread;
                                                                                                                                                                                                                                                                                                this.callJava((JavaFunction)prepareMetatableCall, localBase, n, nArguments);
                                                                                                                                                                                                                                                                                                currentCallFrame3 = this.currentThread.currentCallFrame();
                                                                                                                                                                                                                                                                                                currentThread.popCallFrame();
                                                                                                                                                                                                                                                                                                break Block_77;
                                                                                                                                                                                                                                                                                                Label_4877:
                                                                                                                                                                                                                                                                                                continue Label_3568_Outer;
                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                            a20 = getA8(n3);
                                                                                                                                                                                                                                                                                            b11 = getB9(n3);
                                                                                                                                                                                                                                                                                            registerOrConstant5 = this.getRegisterOrConstant(luaCallFrame, getC9(n3), luaPrototype);
                                                                                                                                                                                                                                                                                            value8 = luaCallFrame.get(b11);
                                                                                                                                                                                                                                                                                            luaCallFrame.set(a20, this.tableGet(value8, registerOrConstant5));
                                                                                                                                                                                                                                                                                            luaCallFrame.set(a20 + 1, value8);
                                                                                                                                                                                                                                                                                            continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                        ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                    currentCallFrame5 = this.currentThread.currentCallFrame();
                                                                                                                                                                                                                                                                                    closure2 = currentCallFrame5.closure;
                                                                                                                                                                                                                                                                                    prototype2 = closure2.prototype;
                                                                                                                                                                                                                                                                                    code2 = prototype2.code;
                                                                                                                                                                                                                                                                                    returnBase2 = currentCallFrame5.returnBase;
                                                                                                                                                                                                                                                                                    luaCallFrame = currentCallFrame5;
                                                                                                                                                                                                                                                                                    luaClosure = closure2;
                                                                                                                                                                                                                                                                                    array = code2;
                                                                                                                                                                                                                                                                                    luaPrototype = prototype2;
                                                                                                                                                                                                                                                                                    n = returnBase2;
                                                                                                                                                                                                                                                                                    continue Block_9_Outer;
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                Label_5151:
                                                                                                                                                                                                                                                                                throw new RuntimeException("Tried to call a non-function: " + prepareMetatableCall2);
                                                                                                                                                                                                                                                                                continue Block_79_Outer;
                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                            Label_3676:
                                                                                                                                                                                                                                                                            continue Block_29_Outer;
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        b5 = true;
                                                                                                                                                                                                                                                                        break Label_3211;
                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                    continue Label_3560_Outer;
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                            luaCallFrame2.setTop(luaCallFrame2.closure.prototype.maxStacksize);
                                                                                                                                                                                                                                                            luaCallFrame = luaCallFrame2;
                                                                                                                                                                                                                                                            continue Label_5748;
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        Label_2689:
                                                                                                                                                                                                                                                        ++n27;
                                                                                                                                                                                                                                                        break Label_2533;
                                                                                                                                                                                                                                                        Label_5835:
                                                                                                                                                                                                                                                        continue Block_53_Outer;
                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                    o5 = toDouble(((LuaTable)value3).len());
                                                                                                                                                                                                                                                    break Label_2229;
                                                                                                                                                                                                                                                    BaseLib.fail("Object " + value9 + " did not have __call metatable set");
                                                                                                                                                                                                                                                    break Label_4650;
                                                                                                                                                                                                                                                    a11 = getA8(n3);
                                                                                                                                                                                                                                                    b12 = getB9(n3);
                                                                                                                                                                                                                                                    c15 = getC9(n3);
                                                                                                                                                                                                                                                    registerOrConstant = this.getRegisterOrConstant(luaCallFrame, b12, luaPrototype);
                                                                                                                                                                                                                                                    registerOrConstant2 = this.getRegisterOrConstant(luaCallFrame, c15, luaPrototype);
                                                                                                                                                                                                                                                    continue Block_79_Outer;
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                a21 = getA8(n3);
                                                                                                                                                                                                                                                b13 = getB9(n3);
                                                                                                                                                                                                                                                c16 = getC9(n3);
                                                                                                                                                                                                                                                n33 = b13 - 1;
                                                                                                                                                                                                                                                break Block_63;
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            luaCallFrame.set(a14, b2);
                                                                                                                                                                                                                                            continue Label_3568_Outer;
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        Label_6892:
                                                                                                                                                                                                                                        this.currentThread.popCallFrame();
                                                                                                                                                                                                                                        return;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    n32 = 1;
                                                                                                                                                                                                                                    continue Label_3568;
                                                                                                                                                                                                                                    Label_3664:
                                                                                                                                                                                                                                    n23 = 0;
                                                                                                                                                                                                                                    break Label_3622;
                                                                                                                                                                                                                                    a22 = getA8(n3);
                                                                                                                                                                                                                                    value2 = luaCallFrame.get(getB9(n3));
                                                                                                                                                                                                                                    rawTonumber3 = BaseLib.rawTonumber(value2);
                                                                                                                                                                                                                                    continue Label_7690_Outer;
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            n16 = n33;
                                                                                                                                                                                                                            break Block_67;
                                                                                                                                                                                                                            Label_4837:
                                                                                                                                                                                                                            n33 = luaCallFrame.getTop() - a21 - 1;
                                                                                                                                                                                                                            break Label_4430;
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        n29 = 1;
                                                                                                                                                                                                                        continue Label_3560;
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                }
                                                                                                                                                                                                                continue Block_75_Outer;
                                                                                                                                                                                                            }
                                                                                                                                                                                                        }
                                                                                                                                                                                                        n28 = array[luaCallFrame.pc++];
                                                                                                                                                                                                        continue Block_75_Outer;
                                                                                                                                                                                                    }
                                                                                                                                                                                                    o6 = this.call(binMetaOp, value4, string, null);
                                                                                                                                                                                                    c10 = n20 - 1;
                                                                                                                                                                                                    continue Label_2485;
                                                                                                                                                                                                }
                                                                                                                                                                                                luaCallFrame.set(a23, o5);
                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                            }
                                                                                                                                                                                            b7 = boolEval(this.call(compMetaOp, o2, o, null));
                                                                                                                                                                                            continue Label_3895;
                                                                                                                                                                                        }
                                                                                                                                                                                    }
                                                                                                                                                                                    c10 = n20;
                                                                                                                                                                                    o6 = string;
                                                                                                                                                                                    continue Label_3340_Outer;
                                                                                                                                                                                }
                                                                                                                                                                            }
                                                                                                                                                                            sb = new StringBuffer();
                                                                                                                                                                            n25 = c10 - n27 + 1;
                                                                                                                                                                            continue Label_2647;
                                                                                                                                                                        }
                                                                                                                                                                    }
                                                                                                                                                                    luaCallFrame.pc += getSBx(n3);
                                                                                                                                                                    luaCallFrame.set(a13 + 3, double1);
                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                }
                                                                                                                                                                continue Label_3283_Outer;
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                        continue Label_7718_Outer;
                                                                                                                                                    }
                                                                                                                                                    o5 = toDouble(((String)value3).length());
                                                                                                                                                    continue Label_2229;
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            n14 = n15;
                                                                                                                                            n16 = n33 + 1;
                                                                                                                                            continue Block_29_Outer;
                                                                                                                                        }
                                                                                                                                        luaCallFrame.setTop(a21 + n33 + 1);
                                                                                                                                        break Label_4430;
                                                                                                                                        localBase2 = luaCallFrame.localBase;
                                                                                                                                        this.currentThread.closeUpvalues(localBase2);
                                                                                                                                        a17 = getA8(n3);
                                                                                                                                        continue Label_3568_Outer;
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                return;
                                                                                                                            }
                                                                                                                            luaCallFrame.set(a22, o3);
                                                                                                                            continue Label_0449_Outer;
                                                                                                                        }
                                                                                                                        continue Block_47_Outer;
                                                                                                                    }
                                                                                                                    Label_3323:
                                                                                                                    n26 = 0;
                                                                                                                    continue Label_3283;
                                                                                                                    o2 = registerOrConstant;
                                                                                                                    o = registerOrConstant2;
                                                                                                                    n6 = n34;
                                                                                                                    compMetaOp = compMetaOp2;
                                                                                                                    continue Label_3560_Outer;
                                                                                                                }
                                                                                                                luaCallFrame.set(getA8(n3), this.tableGet(luaClosure.env, luaPrototype.constants[getBx(n3)]));
                                                                                                                continue Label_0449_Outer;
                                                                                                                n26 = 1;
                                                                                                                continue Label_3283;
                                                                                                            }
                                                                                                            this.tableSet(luaClosure.env, luaPrototype.constants[getBx(n3)], luaCallFrame.get(getA8(n3)));
                                                                                                            continue Label_0449_Outer;
                                                                                                            Label_3608:
                                                                                                            n32 = 0;
                                                                                                            continue Label_3568;
                                                                                                        }
                                                                                                    }
                                                                                                    continue Block_29_Outer;
                                                                                                }
                                                                                            }
                                                                                            luaCallFrame.restoreTop = (c16 != 0);
                                                                                            localBase3 = luaCallFrame.localBase;
                                                                                            n14 = localBase3 + a21 + 1;
                                                                                            n15 = localBase3 + a21;
                                                                                            value9 = luaCallFrame.get(a21);
                                                                                            BaseLib.luaAssert(value9 != null, "Tried to call nil");
                                                                                            prepareMetatableCall2 = this.prepareMetatableCall(value9);
                                                                                            continue Block_52_Outer;
                                                                                        }
                                                                                    }
                                                                                    luaCallFrame.set(a15, o4);
                                                                                    continue Label_0449_Outer;
                                                                                }
                                                                                continue Label_3340_Outer;
                                                                            }
                                                                        }
                                                                        continue Block_75_Outer;
                                                                    }
                                                                }
                                                                continue;
                                                            }
                                                        }
                                                        luaTable.rawset(toDouble((n28 - 1) * 50 + n10), luaCallFrame.get(a8 + n10));
                                                        ++n10;
                                                        continue Label_7718;
                                                    }
                                                }
                                                luaCallFrame.set(a19 + 2, value7);
                                                continue Label_0449_Outer;
                                                a8 = getA8(n3);
                                                b14 = getB9(n3);
                                                c13 = getC9(n3);
                                                n5 = b14;
                                                continue Block_76_Outer;
                                            }
                                            Label_3728:
                                            n34 = 0;
                                            str2 = LuaState.meta_ops[n4];
                                            compMetaOp2 = this.getCompMetaOp(registerOrConstant, registerOrConstant2, str2);
                                            o2 = registerOrConstant;
                                            o = registerOrConstant2;
                                            n6 = n34;
                                            continue;
                                        }
                                        a23 = getA8(n3);
                                        value3 = luaCallFrame.get(getB9(n3));
                                        continue Block_52_Outer;
                                    }
                                    luaCallFrame.set(getA8(n3), this.tableGet(luaCallFrame.get(getB9(n3)), this.getRegisterOrConstant(luaCallFrame, getC9(n3), luaPrototype)));
                                    continue Label_0449_Outer;
                                }
                                n12 = 1;
                                continue Label_3340;
                            }
                        }
                        this.currentThread.stackCopy(localBase2 + a17, n, nArguments + 1);
                        this.currentThread.setTop(n + nArguments + 1);
                        continue;
                    }
                }
                // iftrue(Label_3664:, compareTo > 0)
                // iftrue(Label_4974:, !currentCallFrame3.fromLua)
                // iftrue(Label_4303:, boolEval(value) == c9 == 0)
                // iftrue(Label_3608:, a11 != 0)
                // switch([Lcom.strobel.decompiler.ast.Label;@75f6d034, n9 & 0x3F)
                // iftrue(Label_0030:, registerOrConstant.equals(registerOrConstant2) != a11 == 0)
                // iftrue(Label_0030:, n10 > n5)
                // iftrue(Label_3257:, n4 != 23)
                // iftrue(Label_0030:, boolEval(luaCallFrame.get(getA8(n3))) != getC9(n3) == 0)
                // iftrue(Label_3602:, compareTo >= 0)
                // iftrue(Label_0030:, n12 != n13)
                // iftrue(Label_0030:, n17 != n11)
                // iftrue(Label_3245:, fromDouble != fromDouble2)
                // iftrue(Label_7276:, n19 > fromDouble3)
                // iftrue(Label_3915:, compMetaOp != null || n4 != 23)
                // iftrue(Label_2896:, binMetaOp != null)
                // iftrue(Label_7268:, fromDouble5 <= 0.0)
                // iftrue(Label_7195:, n19 >= fromDouble3)
                // iftrue(Label_0030:, n23 != n18)
                // iftrue(Label_3388:, a11 != 0)
                // iftrue(Label_2689:, BaseLib.rawTostring(value5) != null)
                // iftrue(Label_0030:, n7 != n26)
                // iftrue(Label_3686:, n6 == 0)
                // iftrue(Label_2932:, b8 > c10)
                // iftrue(Label_0679:, b9 != 0)
                // iftrue(Label_1681:, rawTonumber == null)
                // iftrue(Label_2745:, string = o6 == null)
                // iftrue(Label_2695:, n25 > c10)
                // iftrue(Label_4877:, !prepareMetatableCall2 instanceof LuaClosure)
                // iftrue(Label_5554:, prepareMetatableCall != null)
                // iftrue(Label_5748:, !luaCallFrame2.restoreTop)
                // iftrue(Label_5585:, prepareMetatableCall == value6)
                // iftrue(Label_7690:, n28 = c13 != 0)
                // iftrue(Label_0030:, n8 >= numUpvalues)
                // iftrue(Label_3382:, fromDouble > fromDouble2)
                // iftrue(Label_0030:, !currentCallFrame4.restoreTop)
                // iftrue(Label_3251:, a11 != 0)
                // iftrue(Label_1805:, binMetaOp2 != null)
                // iftrue(Label_6868:, !luaCallFrame.insideCoroutine || this.currentThread.callFrameTop != 1)
                // iftrue(Label_2745:, n27 <= 0)
                // iftrue(Label_6435:, n30 != -1)
                // iftrue(Label_3991:, compMetaOp != null)
                // iftrue(Label_3614:, n4 != 24)
                // iftrue(Label_3676:, !registerOrConstant instanceof String || !registerOrConstant2 instanceof String)
                // iftrue(Label_1857:, rawTonumber2 != null)
                // iftrue(Label_4975:, !currentCallFrame4.isJava())
                // iftrue(Label_6892:, !luaCallFrame.fromLua)
                // iftrue(Label_2307:, !value3 instanceof String)
                // iftrue(Label_4974:, this.currentThread.currentCallFrame().isJava())
                // iftrue(Label_7508:, value7 == null)
                // iftrue(Label_3329:, n4 != 24)
                // iftrue(Label_6209:, currentThread == this.currentThread)
                // iftrue(Label_5151:, !prepareMetatableCall2 instanceof JavaFunction)
                // iftrue(Label_0030:, !currentCallFrame5.restoreTop)
                // iftrue(Label_0030:, n29 != n32)
                // iftrue(Label_3728:, registerOrConstant != registerOrConstant2)
                // iftrue(Label_3317:, fromDouble >= fromDouble2)
                // iftrue(Label_5925:, prepareMetatableCall instanceof JavaFunction)
                // iftrue(Label_3394:, !registerOrConstant instanceof Double || !registerOrConstant2 instanceof Double)
                // iftrue(Label_4837:, n33 == -1)
                // iftrue(Label_0030:, c11 == 0)
                // iftrue(Label_2008:, rawTonumber3 == null)
                // iftrue(Label_4671:, prepareMetatableCall2 == value9)
                // iftrue(Label_4024:, b7)
                // iftrue(Label_2485:, b8 > n20)
                // iftrue(Label_2587:, b8 > n24)
                // iftrue(Label_3516:, n4 != 23)
                // iftrue(Label_5379:, n22 = getB9(n3) - 1 != -1)
                // iftrue(Label_5748:, !luaCallFrame = this.currentThread.currentCallFrame().isJava())
                // iftrue(Label_6158:, !currentThread.isDead() || this.currentThread.parent != currentThread)
                // iftrue(Label_3860:, n4 != 25)
                // iftrue(Label_4030:, a11 != 0)
                // iftrue(Label_4650:, prepareMetatableCall2 != null)
                // iftrue(Label_3670:, a11 != 0)
                // iftrue(Label_0030:, b6 != b5)
                // iftrue(Label_3323:, a11 != 0)
                // iftrue(Label_7632:, b14 != 0)
                // iftrue(Label_3860:, compMetaOp = compMetaOp2 != null)
                // iftrue(Label_2254:, !value3 instanceof LuaTable)
                // iftrue(Label_5835:, !prepareMetatableCall instanceof LuaClosure)
            }
        }
    }
    
    private final Object prepareMetatableCall(Object metaOp) {
        if (!(metaOp instanceof JavaFunction) && !(metaOp instanceof LuaClosure)) {
            metaOp = this.getMetaOp(metaOp, "__call");
        }
        return metaOp;
    }
    
    private Double primitiveMath(final Double n, final Double n2, final int n3) {
        final double fromDouble = fromDouble(n);
        final double fromDouble2 = fromDouble(n2);
        double pow = 0.0;
        switch (n3) {
            case 12: {
                pow = fromDouble + fromDouble2;
                break;
            }
            case 13: {
                pow = fromDouble - fromDouble2;
                break;
            }
            case 14: {
                pow = fromDouble * fromDouble2;
                break;
            }
            case 15: {
                pow = fromDouble / fromDouble2;
                break;
            }
            case 16: {
                if (fromDouble2 == 0.0) {
                    pow = Double.NaN;
                    break;
                }
                pow = fromDouble - (int)(fromDouble / fromDouble2) * fromDouble2;
                break;
            }
            case 17: {
                pow = MathLib.pow(fromDouble, fromDouble2);
                break;
            }
        }
        return toDouble(pow);
    }
    
    private void setUserdataMetatable(final Object o, final LuaTable luaTable) {
        this.userdataMetatables.rawset(o, luaTable);
    }
    
    public static Boolean toBoolean(final boolean b) {
        Boolean b2;
        if (b) {
            b2 = Boolean.TRUE;
        }
        else {
            b2 = Boolean.FALSE;
        }
        return b2;
    }
    
    public static Double toDouble(final double value) {
        return new Double(value);
    }
    
    public static Double toDouble(final long n) {
        return toDouble((double)n);
    }
    
    public int call(int callJava) {
        final int n = this.currentThread.getTop() - callJava - 1;
        final Object o = this.currentThread.objectStack[n];
        if (o == null) {
            throw new RuntimeException("tried to call nil");
        }
        if (o instanceof JavaFunction) {
            callJava = this.callJava((JavaFunction)o, n + 1, n, callJava);
        }
        else {
            if (!(o instanceof LuaClosure)) {
                throw new RuntimeException("tried to call a non-function");
            }
            this.currentThread.pushNewCallFrame((LuaClosure)o, null, n + 1, n, callJava, false, false).init();
            this.luaMainloop();
            callJava = this.currentThread.getTop() - n;
            this.currentThread.stackTrace = "";
        }
        return callJava;
    }
    
    public Object call(Object o, final Object o2, final Object o3, final Object o4) {
        final int top = this.currentThread.getTop();
        this.currentThread.setTop(top + 1 + 3);
        this.currentThread.objectStack[top] = o;
        this.currentThread.objectStack[top + 1] = o2;
        this.currentThread.objectStack[top + 2] = o3;
        this.currentThread.objectStack[top + 3] = o4;
        final int call = this.call(3);
        o = null;
        if (call >= 1) {
            o = this.currentThread.objectStack[top];
        }
        this.currentThread.setTop(top);
        return o;
    }
    
    public Object call(Object o, final Object[] array) {
        final int top = this.currentThread.getTop();
        int length;
        if (array == null) {
            length = 0;
        }
        else {
            length = array.length;
        }
        this.currentThread.setTop(top + 1 + length);
        this.currentThread.objectStack[top] = o;
        for (int i = 1; i <= length; ++i) {
            this.currentThread.objectStack[top + i] = array[i - 1];
        }
        final int call = this.call(length);
        o = null;
        if (call >= 1) {
            o = this.currentThread.objectStack[top];
        }
        this.currentThread.setTop(top);
        return o;
    }
    
    public LuaTable getClassMetatable(final Class clazz) {
        return (LuaTable)this.classMetatables.rawget(clazz);
    }
    
    public LuaTable getEnvironment() {
        return this.currentThread.environment;
    }
    
    public Object getMetaOp(Object rawget, final String s) {
        final LuaTable luaTable = (LuaTable)this.getmetatable(rawget, true);
        if (luaTable == null) {
            rawget = null;
        }
        else {
            rawget = luaTable.rawget(s);
        }
        return rawget;
    }
    
    public PrintStream getOut() {
        return this.out;
    }
    
    public Object getmetatable(Object rawget, final boolean b) {
        if (rawget == null) {
            rawget = null;
        }
        else {
            LuaTable metatable;
            if (rawget instanceof LuaTable) {
                metatable = ((LuaTable)rawget).getMetatable();
            }
            else {
                metatable = (LuaTable)this.userdataMetatables.rawget(rawget);
            }
            LuaTable luaTable = metatable;
            if (metatable == null) {
                luaTable = (LuaTable)this.classMetatables.rawget(rawget.getClass());
            }
            if (b || luaTable == null || (rawget = luaTable.rawget("__metatable")) == null) {
                rawget = luaTable;
            }
        }
        return rawget;
    }
    
    public LuaClosure loadByteCodeFromResource(final String str, final LuaTable luaTable) {
        final InputStream resourceAsStream = this.getClass().getResourceAsStream(str + ".lbc");
        LuaClosure loadByteCode;
        if (resourceAsStream == null) {
            loadByteCode = null;
        }
        else {
            try {
                loadByteCode = LuaPrototype.loadByteCode(resourceAsStream, luaTable);
            }
            catch (IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return loadByteCode;
    }
    
    public void lock() {
    }
    
    public int pcall(int call) {
        final LuaThread currentThread = this.currentThread;
        currentThread.currentCallFrame();
        currentThread.stackTrace = "";
        final int n = currentThread.getTop() - call - 1;
        try {
            call = this.call(call);
            currentThread.setTop(n + call + 1);
            currentThread.stackCopy(n, n + 1, call);
            currentThread.objectStack[n] = Boolean.TRUE;
            return ++call;
        }
        catch (LuaException ex) {
            final Object errorMessage = ex.errorMessage;
        }
        catch (Throwable t) {
            t.getMessage();
            goto Label_0082;
        }
        goto Label_0093;
    }
    
    public Object[] pcall(final Object o) {
        return this.pcall(o, null);
    }
    
    public Object[] pcall(final Object o, final Object[] array) {
        int length;
        if (array == null) {
            length = 0;
        }
        else {
            length = array.length;
        }
        final LuaThread currentThread = this.currentThread;
        final int top = currentThread.getTop();
        currentThread.setTop(top + 1 + length);
        currentThread.objectStack[top] = o;
        if (length > 0) {
            System.arraycopy(array, 0, currentThread.objectStack, top + 1, length);
        }
        final int pcall = this.pcall(length);
        BaseLib.luaAssert(currentThread == this.currentThread, "Internal Kahlua error - thread changed in pcall");
        final Object[] array2 = new Object[pcall];
        System.arraycopy(currentThread.objectStack, top, array2, 0, pcall);
        currentThread.setTop(top);
        return array2;
    }
    
    protected final void reset() {
        this.currentThread = new LuaThread(this, new LuaTableImpl());
        this.getEnvironment().rawset("_G", this.getEnvironment());
        this.getEnvironment().rawset("_VERSION", "Lua 5.1 for CLDC 1.1");
        BaseLib.register(this);
        StringLib.register(this);
        MathLib.register(this);
        CoroutineLib.register(this);
        OsLib.register(this);
        TableLib.register(this);
    }
    
    public void setClassMetatable(final Class clazz, final LuaTable luaTable) {
        this.classMetatables.rawset(clazz, luaTable);
    }
    
    public void setmetatable(final Object o, final LuaTable metatable) {
        BaseLib.luaAssert(o != null, "Can't set metatable for nil");
        if (o instanceof LuaTable) {
            ((LuaTable)o).setMetatable(metatable);
        }
        else {
            this.userdataMetatables.rawset(o, metatable);
        }
    }
    
    public Object tableGet(Object call, final Object o) {
        Object obj = call;
        int i = 100;
        while (i > 0) {
            final boolean b = obj instanceof LuaTable;
            if (b) {
                final Object rawget = ((LuaTable)obj).rawget(o);
                if (rawget != null) {
                    call = rawget;
                    return call;
                }
            }
            final Object metaOp = this.getMetaOp(obj, "__index");
            if (metaOp == null) {
                if (!b) {
                    throw new RuntimeException("attempted index of non-table: " + obj);
                }
                call = null;
            }
            else {
                if (!(metaOp instanceof JavaFunction) && !(metaOp instanceof LuaClosure)) {
                    obj = metaOp;
                    --i;
                    continue;
                }
                call = this.call(metaOp, call, o, null);
            }
            return call;
        }
        throw new RuntimeException("loop in gettable");
    }
    
    public void tableSet(final Object o, final Object o2, final Object o3) {
        Object o4 = o;
        int i = 100;
        while (i > 0) {
            if (o4 instanceof LuaTable) {
                final LuaTable luaTable = (LuaTable)o4;
                if (luaTable.rawget(o2) != null) {
                    luaTable.rawset(o2, o3);
                    return;
                }
                if ((o4 = this.getMetaOp(o4, "__newindex")) == null) {
                    luaTable.rawset(o2, o3);
                    return;
                }
            }
            else {
                o4 = this.getMetaOp(o4, "__newindex");
                BaseLib.luaAssert(o4 != null, "attempted index of non-table");
            }
            if (!(o4 instanceof JavaFunction) && !(o4 instanceof LuaClosure)) {
                --i;
                continue;
            }
            this.call(o4, o, o2, o3);
            return;
        }
        throw new RuntimeException("loop in settable");
    }
    
    public void unlock() {
    }
}
