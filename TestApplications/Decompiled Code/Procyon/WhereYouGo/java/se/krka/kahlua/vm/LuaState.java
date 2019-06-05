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
        Object value;
        Object string;
        int n5;
        Object binMetaOp;
        LuaCallFrame currentCallFrame3;
        LuaClosure closure;
        LuaPrototype prototype;
        int[] code;
        int returnBase;
        boolean b2;
        Object value2;
        Object o;
        LuaCallFrame currentCallFrame4;
        LuaClosure closure2;
        LuaPrototype prototype2;
        int[] code2;
        int returnBase2;
        Object metaOp;
        int a8;
        Object o2;
        int a9;
        int n6;
        int n7;
        int n8;
        Object registerOrConstant;
        Object registerOrConstant2;
        Object compMetaOp;
        Object o3;
        int n9;
        Object o4;
        boolean b3;
        Object prepareMetatableCall;
        double fromDouble;
        double fromDouble2;
        boolean b4;
        int n10;
        int c9;
        int a10;
        int n11;
        int n12;
        int compareTo;
        int n13;
        int n14;
        double n15;
        double fromDouble3;
        int n16;
        int n17;
        Boolean b5;
        LuaClosure luaClosure3;
        int n18;
        int b6;
        String str;
        int b7;
        boolean b8;
        LuaCallFrame luaCallFrame2;
        int a11;
        int n19;
        int a12;
        int b9;
        int c10;
        int n20;
        StringBuffer sb;
        String rawTostring;
        int n22;
        int n21;
        int n24;
        int n23;
        int n25;
        boolean b10;
        Object prepareMetatableCall2;
        int n26;
        int a13;
        int n27;
        Object binMetaOp2;
        Object registerOrConstant3;
        Object registerOrConstant4;
        Object o5;
        Double rawTonumber;
        Double rawTonumber2;
        LuaCallFrame currentCallFrame5;
        int a14;
        int c11;
        Object value3;
        int a15;
        Object value4;
        Object o6;
        int a16;
        int a17;
        int a18;
        int b11;
        int c12;
        int localBase;
        int nArguments;
        String str2;
        Double rawTonumber3;
        int a19;
        LuaThread currentThread;
        int b12;
        int c13;
        int a20;
        int b13;
        Object registerOrConstant5;
        Object value5;
        Object value6;
        int n28;
        Object compMetaOp2;
        int numUpvalues;
        int n29;
        int a21;
        int sBx;
        int a22;
        LuaPrototype luaPrototype3;
        int a23;
        int b14;
        int c14;
        Object value7;
        int n30;
        int n31;
        int b15;
        int c15;
        int localBase2;
        int b16;
        int c16;
        double fromDouble4;
        double fromDouble5;
        Double double1;
        int n32;
        LuaThread currentThread2;
        LuaTable luaTable;
        Object value8;
        int localBase3;
        Object value9;
        LuaCallFrame pushNewCallFrame;
        Label_0449_Outer:Block_23_Outer:Block_22_Outer:Block_14_Outer:Label_2229_Outer:Label_7718_Outer:
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
                        continue Block_23_Outer;
                    }
                    // iftrue(Label_2896:, binMetaOp != null)
                    // iftrue(Label_0030:, !currentCallFrame3.restoreTop)
                    // iftrue(Label_0030:, !currentCallFrame4.restoreTop)
                    // iftrue(Label_3608:, a9 != 0)
                    // iftrue(Label_0030:, n7 != n8)
                    // iftrue(Label_3251:, a9 != 0)
                    // iftrue(Label_3245:, fromDouble != fromDouble2)
                    // iftrue(Label_3686:, n9 == 0)
                    // iftrue(Label_2695:, n10 > c9)
                    // iftrue(Label_4024:, b4)
                    // iftrue(Label_3664:, compareTo > 0)
                    // iftrue(Label_6868:, !luaCallFrame.insideCoroutine || this.currentThread.callFrameTop != 1)
                    // iftrue(Label_7195:, n15 >= fromDouble3)
                    // iftrue(Label_0030:, n16 != n17)
                    // iftrue(Label_3991:, compMetaOp != null)
                    // iftrue(Label_0030:, boolEval(luaCallFrame.get(getA8(n3))) != getC9(n3) == 0)
                    // iftrue(Label_0030:, n12 != n6)
                    // iftrue(Label_2587:, b7 > n14)
                    // iftrue(Label_5748:, !luaCallFrame2.restoreTop)
                    // iftrue(Label_6892:, !luaCallFrame.fromLua)
                    // iftrue(Label_7632:, b9 != 0)
                    // iftrue(Label_4030:, a9 != 0)
                    // iftrue(Label_3670:, a9 != 0)
                    // iftrue(Label_4881:, !prepareMetatableCall2 instanceof LuaClosure)
                    // iftrue(Label_3516:, n4 != 23)
                    // iftrue(Label_3329:, n4 != 24)
                    // iftrue(Label_3728:, registerOrConstant != registerOrConstant2)
                    // iftrue(Label_4978:, !currentCallFrame5.fromLua)
                    // iftrue(Label_0030:, n26 != n11)
                    // iftrue(Label_7508:, value3 == null)
                    // iftrue(Label_3388:, a9 != 0)
                    // iftrue(Label_2485:, b7 > n5)
                    // iftrue(Label_2932:, b7 > c9)
                    // iftrue(Label_0679:, b11 != 0)
                    // iftrue(Label_4978:, this.currentThread.currentCallFrame().isJava())
                    // iftrue(Label_0030:, registerOrConstant.equals(registerOrConstant2) != a9 == 0)
                    // iftrue(Label_3602:, compareTo >= 0)
                    // iftrue(Label_3915:, compMetaOp != null || n4 != 23)
                    // iftrue(Label_3394:, !registerOrConstant instanceof Double || !registerOrConstant2 instanceof Double)
                    // iftrue(Label_0030:, c12 == 0)
                    // iftrue(Label_3257:, n4 != 23)
                    // iftrue(Label_4979:, !currentCallFrame3.isJava())
                    // iftrue(Label_5925:, prepareMetatableCall instanceof JavaFunction)
                    // iftrue(Label_3860:, compMetaOp = compMetaOp2 != null)
                    // iftrue(Label_1857:, rawTonumber2 != null)
                    // iftrue(Label_1805:, binMetaOp2 != null)
                    // iftrue(Label_0030:, n18 >= numUpvalues)
                    // switch([Lcom.strobel.decompiler.ast.Label;@79f4f290, n29 & 0x3F)
                    // iftrue(Label_4303:, boolEval(value7) == c14 == 0)
                    // iftrue(Label_5748:, !luaCallFrame = this.currentThread.currentCallFrame().isJava())
                    // iftrue(Label_5155:, !prepareMetatableCall2 instanceof JavaFunction)
                    // iftrue(Label_2254:, !value2 instanceof LuaTable)
                    // iftrue(Label_0030:, n31 > n20)
                    // iftrue(Label_3860:, n4 != 25)
                    // iftrue(Label_4841:, n24 == -1)
                    // iftrue(Label_5383:, n27 = getB9(n3) - 1 != -1)
                    // iftrue(Label_1681:, rawTonumber == null)
                    // iftrue(Label_3614:, n4 != 24)
                    // iftrue(Label_5835:, !prepareMetatableCall instanceof LuaClosure)
                    // iftrue(Label_0030:, b2 != b3)
                    // iftrue(Label_7268:, fromDouble5 <= 0.0)
                    // iftrue(Label_6435:, n32 != -1)
                    // iftrue(Label_2745:, string = o6 == null)
                    // iftrue(Label_5558:, prepareMetatableCall != null)
                    // iftrue(Label_2008:, rawTonumber3 == null)
                    // iftrue(Label_0030:, b10 != b8)
                    // iftrue(Label_3676:, !registerOrConstant instanceof String || !registerOrConstant2 instanceof String)
                    // iftrue(Label_3323:, a9 != 0)
                    // iftrue(Label_7690:, n30 = c10 != 0)
                    // iftrue(Label_4650:, prepareMetatableCall2 != null)
                    // iftrue(Label_5585:, prepareMetatableCall == value6)
                    // iftrue(Label_2689:, BaseLib.rawTostring(value9) != null)
                    // iftrue(Label_3382:, fromDouble > fromDouble2)
                    // iftrue(Label_4675:, prepareMetatableCall2 == value4)
                    // iftrue(Label_2307:, !value2 instanceof String)
                    // iftrue(Label_3317:, fromDouble >= fromDouble2)
                    // iftrue(Label_7276:, n15 > fromDouble3)
                    // iftrue(Label_6209:, currentThread2 == this.currentThread)
                    // iftrue(Label_2745:, n13 <= 0)
                    // iftrue(Label_6158:, !currentThread2.isDead() || this.currentThread.parent != currentThread2)
                Label_7718:
                    while (true) {
                        Label_3348:Label_3622_Outer:Label_5748_Outer:Block_79_Outer:
                        while (true) {
                            Block_35: {
                                while (true) {
                                Label_5748:
                                    while (true) {
                                    Label_3622:
                                        while (true) {
                                        Label_3211:
                                            while (true) {
                                                Label_1983:Label_3895_Outer:Block_20_Outer:
                                                while (true) {
                                                Label_7195_Outer:
                                                    while (true) {
                                                        Label_2587: {
                                                            Label_5925: {
                                                                while (true) {
                                                                    Label_3895:Label_3630_Outer:Label_3283_Outer:
                                                                    while (true) {
                                                                        Label_3991: {
                                                                            while (true) {
                                                                                Block_30: {
                                                                                    while (true) {
                                                                                        Block_68: {
                                                                                            Label_8064:Block_67_Outer:
                                                                                            while (true) {
                                                                                            Label_4430_Outer:
                                                                                                while (true) {
                                                                                                    Label_4650: {
                                                                                                    Label_4430:
                                                                                                        while (true) {
                                                                                                            Label_3283:Block_38_Outer:Label_3275_Outer:Block_34_Outer:
                                                                                                            while (true) {
                                                                                                                while (true) {
                                                                                                                    Label_3275:Block_74_Outer:
                                                                                                                    while (true) {
                                                                                                                        while (true) {
                                                                                                                            Label_5558:Block_91_Outer:
                                                                                                                            while (true) {
                                                                                                                            Block_32_Outer:
                                                                                                                                while (true) {
                                                                                                                                    Label_7632: {
                                                                                                                                        while (true) {
                                                                                                                                        Block_12_Outer:
                                                                                                                                            while (true) {
                                                                                                                                                while (true) {
                                                                                                                                                    while (true) {
                                                                                                                                                        Label_3630:Block_18_Outer:Block_73_Outer:
                                                                                                                                                        while (true) {
                                                                                                                                                            Block_47: {
                                                                                                                                                                while (true) {
                                                                                                                                                                    Label_5383: {
                                                                                                                                                                        while (true) {
                                                                                                                                                                            Label_2229:Label_3568_Outer:Label_6158_Outer:
                                                                                                                                                                            while (true) {
                                                                                                                                                                                while (true) {
                                                                                                                                                                                Label_3568:
                                                                                                                                                                                    while (true) {
                                                                                                                                                                                        Label_3340:Label_3686_Outer:
                                                                                                                                                                                        while (true) {
                                                                                                                                                                                            Label_3686:Label_6435_Outer:
                                                                                                                                                                                            while (true) {
                                                                                                                                                                                            Block_49:
                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                    Block_55: {
                                                                                                                                                                                                    Block_75_Outer:
                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                            Label_5585:
                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                        Block_89: {
                                                                                                                                                                                                                        Block_63_Outer:
                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                        Block_52: {
                                                                                                                                                                                                                                        Label_7956:
                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                    Label_2647:
                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                            Block_21: {
                                                                                                                                                                                                                                                            Label_0625:
                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                    Block_8: {
                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                                            Label_3203:
                                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                                Block_41_Outer:
                                                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                                                        Block_25: {
                                                                                                                                                                                                                                                                                        Block_9:
                                                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                                                                Label_2896: {
                                                                                                                                                                                                                                                                                                    Label_3860: {
                                                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                                                                                Label_6669:Label_3694_Outer:Label_3912_Outer:
                                                                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                                                                Label_3912:
                                                                                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                                                                                    Label_3694:
                                                                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                                                                            Label_7276: {
                                                                                                                                                                                                                                                                                                                                Block_58: {
                                                                                                                                                                                                                                                                                                                                    Block_51: {
                                                                                                                                                                                                                                                                                                                                    Label_3560_Outer:
                                                                                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                                                                                                                BaseLib.fail("__concat not defined for operands: " + value + " and " + string);
                                                                                                                                                                                                                                                                                                                                                break Label_2896;
                                                                                                                                                                                                                                                                                                                                                luaCallFrame.set(getA8(n3), new LuaTableImpl());
                                                                                                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                value = luaCallFrame.get(n5);
                                                                                                                                                                                                                                                                                                                                                binMetaOp = this.getBinMetaOp(value, string, "__concat");
                                                                                                                                                                                                                                                                                                                                                continue Block_22_Outer;
                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                            Label_4979: {
                                                                                                                                                                                                                                                                                                                                                closure = currentCallFrame3.closure;
                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                            prototype = closure.prototype;
                                                                                                                                                                                                                                                                                                                                            code = prototype.code;
                                                                                                                                                                                                                                                                                                                                            returnBase = currentCallFrame3.returnBase;
                                                                                                                                                                                                                                                                                                                                            luaCallFrame = currentCallFrame3;
                                                                                                                                                                                                                                                                                                                                            luaClosure = closure;
                                                                                                                                                                                                                                                                                                                                            array = code;
                                                                                                                                                                                                                                                                                                                                            luaPrototype = prototype;
                                                                                                                                                                                                                                                                                                                                            n = returnBase;
                                                                                                                                                                                                                                                                                                                                            Label_2745: {
                                                                                                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                                                                                                                    Label_2533:
                                                                                                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                                                                                                                                Label_1805: {
                                                                                                                                                                                                                                                                                                                                                                    Block_70: {
                                                                                                                                                                                                                                                                                                                                                                        break Block_70;
                                                                                                                                                                                                                                                                                                                                                                        Label_3602:
                                                                                                                                                                                                                                                                                                                                                                        b2 = false;
                                                                                                                                                                                                                                                                                                                                                                        break Label_3560;
                                                                                                                                                                                                                                                                                                                                                                        o = toDouble(((LuaTable)value2).len());
                                                                                                                                                                                                                                                                                                                                                                        break Label_2229;
                                                                                                                                                                                                                                                                                                                                                                        currentCallFrame4 = this.currentThread.currentCallFrame();
                                                                                                                                                                                                                                                                                                                                                                        closure2 = currentCallFrame4.closure;
                                                                                                                                                                                                                                                                                                                                                                        prototype2 = closure2.prototype;
                                                                                                                                                                                                                                                                                                                                                                        code2 = prototype2.code;
                                                                                                                                                                                                                                                                                                                                                                        returnBase2 = currentCallFrame4.returnBase;
                                                                                                                                                                                                                                                                                                                                                                        luaCallFrame = currentCallFrame4;
                                                                                                                                                                                                                                                                                                                                                                        luaClosure = closure2;
                                                                                                                                                                                                                                                                                                                                                                        array = code2;
                                                                                                                                                                                                                                                                                                                                                                        luaPrototype = prototype2;
                                                                                                                                                                                                                                                                                                                                                                        n = returnBase2;
                                                                                                                                                                                                                                                                                                                                                                        Block_46: {
                                                                                                                                                                                                                                                                                                                                                                            Label_4675: {
                                                                                                                                                                                                                                                                                                                                                                                Block_27: {
                                                                                                                                                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                                                                                                                                            Block_87: {
                                                                                                                                                                                                                                                                                                                                                                                                break Block_87;
                                                                                                                                                                                                                                                                                                                                                                                                this.tableSet(luaCallFrame.get(getA8(n3)), this.getRegisterOrConstant(luaCallFrame, getB9(n3), luaPrototype), this.getRegisterOrConstant(luaCallFrame, getC9(n3), luaPrototype));
                                                                                                                                                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                Label_2307:
                                                                                                                                                                                                                                                                                                                                                                                                metaOp = this.getMetaOp(value2, "__len");
                                                                                                                                                                                                                                                                                                                                                                                                BaseLib.luaAssert(metaOp != null, "__len not defined for operand");
                                                                                                                                                                                                                                                                                                                                                                                                o = this.call(metaOp, value2, null, null);
                                                                                                                                                                                                                                                                                                                                                                                                break Label_2229;
                                                                                                                                                                                                                                                                                                                                                                                                this.tableSet(luaClosure.env, luaPrototype.constants[getBx(n3)], luaCallFrame.get(getA8(n3)));
                                                                                                                                                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                luaCallFrame.set(a8, o2);
                                                                                                                                                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                break Label_3340;
                                                                                                                                                                                                                                                                                                                                                                                                Label_3388:
                                                                                                                                                                                                                                                                                                                                                                                                n6 = 0;
                                                                                                                                                                                                                                                                                                                                                                                                break Label_3348;
                                                                                                                                                                                                                                                                                                                                                                                                luaCallFrame.set(getA8(n3), toBoolean(!boolEval(luaCallFrame.get(getB9(n3)))));
                                                                                                                                                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                                                                                                                                                                        ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                        break Block_51;
                                                                                                                                                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                                                                                                                                                            n7 = 1;
                                                                                                                                                                                                                                                                                                                                                                                                            break Label_3912;
                                                                                                                                                                                                                                                                                                                                                                                                            compMetaOp = this.getCompMetaOp(registerOrConstant, registerOrConstant2, "__lt");
                                                                                                                                                                                                                                                                                                                                                                                                            o3 = registerOrConstant2;
                                                                                                                                                                                                                                                                                                                                                                                                            n9 = 1;
                                                                                                                                                                                                                                                                                                                                                                                                            o4 = registerOrConstant;
                                                                                                                                                                                                                                                                                                                                                                                                            break Label_3860;
                                                                                                                                                                                                                                                                                                                                                                                                            break Label_1983;
                                                                                                                                                                                                                                                                                                                                                                                                            n8 = 1;
                                                                                                                                                                                                                                                                                                                                                                                                            continue Label_3694;
                                                                                                                                                                                                                                                                                                                                                                                                            Label_3608:
                                                                                                                                                                                                                                                                                                                                                                                                            b3 = false;
                                                                                                                                                                                                                                                                                                                                                                                                            break Label_3568;
                                                                                                                                                                                                                                                                                                                                                                                                            BaseLib.fail("Tried to call a non-function: " + prepareMetatableCall);
                                                                                                                                                                                                                                                                                                                                                                                                            break Label_5925;
                                                                                                                                                                                                                                                                                                                                                                                                            break Block_27;
                                                                                                                                                                                                                                                                                                                                                                                                            n7 = (b4 ? 1 : 0);
                                                                                                                                                                                                                                                                                                                                                                                                            Block_56: {
                                                                                                                                                                                                                                                                                                                                                                                                                break Block_56;
                                                                                                                                                                                                                                                                                                                                                                                                                break Block_21;
                                                                                                                                                                                                                                                                                                                                                                                                                Label_6892:
                                                                                                                                                                                                                                                                                                                                                                                                                this.currentThread.popCallFrame();
                                                                                                                                                                                                                                                                                                                                                                                                                return;
                                                                                                                                                                                                                                                                                                                                                                                                                luaCallFrame.set(a10, o);
                                                                                                                                                                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                            continue Label_3912_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                        ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                        Label_3670:
                                                                                                                                                                                                                                                                                                                                                                                                        n11 = 0;
                                                                                                                                                                                                                                                                                                                                                                                                        break Label_3630;
                                                                                                                                                                                                                                                                                                                                                                                                        Label_3382:
                                                                                                                                                                                                                                                                                                                                                                                                        n12 = 0;
                                                                                                                                                                                                                                                                                                                                                                                                        break Label_3340;
                                                                                                                                                                                                                                                                                                                                                                                                        Label_3614:
                                                                                                                                                                                                                                                                                                                                                                                                        break Block_46;
                                                                                                                                                                                                                                                                                                                                                                                                        break Label_3912;
                                                                                                                                                                                                                                                                                                                                                                                                        luaCallFrame.closeUpvalues(getA8(n3));
                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                        n13 = 0;
                                                                                                                                                                                                                                                                                                                                                                                                        n14 = c9;
                                                                                                                                                                                                                                                                                                                                                                                                        break Label_2533;
                                                                                                                                                                                                                                                                                                                                                                                                        Label_6868:
                                                                                                                                                                                                                                                                                                                                                                                                        this.currentThread.popCallFrame();
                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_6669;
                                                                                                                                                                                                                                                                                                                                                                                                        Label_7268:
                                                                                                                                                                                                                                                                                                                                                                                                        break Label_7276;
                                                                                                                                                                                                                                                                                                                                                                                                        continue Label_3694_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                    Label_3915:
                                                                                                                                                                                                                                                                                                                                                                                                    break Block_58;
                                                                                                                                                                                                                                                                                                                                                                                                    break Block_75_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                    Label_0679:
                                                                                                                                                                                                                                                                                                                                                                                                    b5 = Boolean.TRUE;
                                                                                                                                                                                                                                                                                                                                                                                                    break Label_0625;
                                                                                                                                                                                                                                                                                                                                                                                                    continue Label_3630_Outer;
                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                Label_8070:
                                                                                                                                                                                                                                                                                                                                                                                                luaClosure3.upvalues[n18] = luaCallFrame.findUpvalue(b6);
                                                                                                                                                                                                                                                                                                                                                                                                break Label_8064;
                                                                                                                                                                                                                                                                                                                                                                                                BaseLib.fail(str + " not defined for operands");
                                                                                                                                                                                                                                                                                                                                                                                                break Label_1805;
                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                            currentCallFrame4.setTop(prototype2.maxStacksize);
                                                                                                                                                                                                                                                                                                                                                                                            luaCallFrame = currentCallFrame4;
                                                                                                                                                                                                                                                                                                                                                                                            luaClosure = closure2;
                                                                                                                                                                                                                                                                                                                                                                                            array = code2;
                                                                                                                                                                                                                                                                                                                                                                                            luaPrototype = prototype2;
                                                                                                                                                                                                                                                                                                                                                                                            n = returnBase2;
                                                                                                                                                                                                                                                                                                                                                                                            continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                            break Label_3686;
                                                                                                                                                                                                                                                                                                                                                                                            break Label_3275;
                                                                                                                                                                                                                                                                                                                                                                                            Label_3251:
                                                                                                                                                                                                                                                                                                                                                                                            b8 = false;
                                                                                                                                                                                                                                                                                                                                                                                            break Label_3211;
                                                                                                                                                                                                                                                                                                                                                                                            luaCallFrame2 = (luaCallFrame = this.currentThread.currentCallFrame());
                                                                                                                                                                                                                                                                                                                                                                                            break Label_3622;
                                                                                                                                                                                                                                                                                                                                                                                            ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                                                            continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                            luaCallFrame.pc += getSBx(n3);
                                                                                                                                                                                                                                                                                                                                                                                            continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                                            this.currentThread.stackCopy(luaCallFrame.localBase + a11, n, n19);
                                                                                                                                                                                                                                                                                                                                                                                            this.currentThread.setTop(n + n19);
                                                                                                                                                                                                                                                                                                                                                                                            continue Block_18_Outer;
                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                        a12 = getA8(n3);
                                                                                                                                                                                                                                                                                                                                                                                        b9 = getB9(n3);
                                                                                                                                                                                                                                                                                                                                                                                        c10 = getC9(n3);
                                                                                                                                                                                                                                                                                                                                                                                        n20 = b9;
                                                                                                                                                                                                                                                                                                                                                                                        break Block_41_Outer;
                                                                                                                                                                                                                                                                                                                                                                                        Label_2695:
                                                                                                                                                                                                                                                                                                                                                                                        sb.append(rawTostring);
                                                                                                                                                                                                                                                                                                                                                                                        string = sb.toString();
                                                                                                                                                                                                                                                                                                                                                                                        n5 = c9 - n13;
                                                                                                                                                                                                                                                                                                                                                                                        break Label_2745;
                                                                                                                                                                                                                                                                                                                                                                                        continue Label_3568_Outer;
                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                    n21 = n22;
                                                                                                                                                                                                                                                                                                                                                                                    n23 = n24 + 1;
                                                                                                                                                                                                                                                                                                                                                                                    n25 = n21;
                                                                                                                                                                                                                                                                                                                                                                                    break Label_4675;
                                                                                                                                                                                                                                                                                                                                                                                    break Block_47;
                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                b10 = true;
                                                                                                                                                                                                                                                                                                                                                                                continue Label_3203;
                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                            break Block_68;
                                                                                                                                                                                                                                                                                                                                                                            ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                                            continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                        n26 = 1;
                                                                                                                                                                                                                                                                                                                                                                        continue Label_3622;
                                                                                                                                                                                                                                                                                                                                                                        luaClosure = luaCallFrame.closure;
                                                                                                                                                                                                                                                                                                                                                                        luaPrototype = luaClosure.prototype;
                                                                                                                                                                                                                                                                                                                                                                        array = luaPrototype.code;
                                                                                                                                                                                                                                                                                                                                                                        n = luaCallFrame.returnBase;
                                                                                                                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                    currentCallFrame3.setTop(prototype.maxStacksize);
                                                                                                                                                                                                                                                                                                                                                                    luaCallFrame = currentCallFrame3;
                                                                                                                                                                                                                                                                                                                                                                    luaClosure = closure;
                                                                                                                                                                                                                                                                                                                                                                    array = code;
                                                                                                                                                                                                                                                                                                                                                                    luaPrototype = prototype;
                                                                                                                                                                                                                                                                                                                                                                    n = returnBase;
                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                    n27 = luaCallFrame.getTop() - a13 - 1;
                                                                                                                                                                                                                                                                                                                                                                    break Label_5383;
                                                                                                                                                                                                                                                                                                                                                                    break Label_6669;
                                                                                                                                                                                                                                                                                                                                                                    ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                                                    Label_3257:
                                                                                                                                                                                                                                                                                                                                                                    break Block_30;
                                                                                                                                                                                                                                                                                                                                                                    Label_3676:
                                                                                                                                                                                                                                                                                                                                                                    break Block_49;
                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                o5 = this.call(binMetaOp2, registerOrConstant3, registerOrConstant4, null);
                                                                                                                                                                                                                                                                                                                                                                break Label_3560_Outer;
                                                                                                                                                                                                                                                                                                                                                                Label_1857:
                                                                                                                                                                                                                                                                                                                                                                o5 = this.primitiveMath(rawTonumber, rawTonumber2, n4);
                                                                                                                                                                                                                                                                                                                                                                break Label_3560_Outer;
                                                                                                                                                                                                                                                                                                                                                                Label_6209:
                                                                                                                                                                                                                                                                                                                                                                continue Block_41_Outer;
                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                            Label_2689:
                                                                                                                                                                                                                                                                                                                                                            ++n13;
                                                                                                                                                                                                                                                                                                                                                            continue Label_2533;
                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                        continue Label_5748_Outer;
                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                    a14 = getA8(n3);
                                                                                                                                                                                                                                                                                                                                                    c11 = getC9(n3);
                                                                                                                                                                                                                                                                                                                                                    luaCallFrame.setTop(a14 + 6);
                                                                                                                                                                                                                                                                                                                                                    luaCallFrame.stackCopy(a14, a14 + 3, 3);
                                                                                                                                                                                                                                                                                                                                                    this.call(2);
                                                                                                                                                                                                                                                                                                                                                    luaCallFrame.clearFromIndex(a14 + 3 + c11);
                                                                                                                                                                                                                                                                                                                                                    luaCallFrame.setPrototypeStacksize();
                                                                                                                                                                                                                                                                                                                                                    value3 = luaCallFrame.get(a14 + 3);
                                                                                                                                                                                                                                                                                                                                                    break Block_89;
                                                                                                                                                                                                                                                                                                                                                    b2 = true;
                                                                                                                                                                                                                                                                                                                                                    continue Block_14_Outer;
                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                luaCallFrame.setTop(a15 + n24 + 1);
                                                                                                                                                                                                                                                                                                                                                break Label_4430;
                                                                                                                                                                                                                                                                                                                                                break Block_35;
                                                                                                                                                                                                                                                                                                                                                BaseLib.fail("Object " + value4 + " did not have __call metatable set");
                                                                                                                                                                                                                                                                                                                                                break Label_4650;
                                                                                                                                                                                                                                                                                                                                                sb = new StringBuffer();
                                                                                                                                                                                                                                                                                                                                                n10 = c9 - n13 + 1;
                                                                                                                                                                                                                                                                                                                                                continue Label_2647;
                                                                                                                                                                                                                                                                                                                                                Label_4303:
                                                                                                                                                                                                                                                                                                                                                ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                            c9 = n5;
                                                                                                                                                                                                                                                                                                                                            o6 = string;
                                                                                                                                                                                                                                                                                                                                            continue Label_3560_Outer;
                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                        luaCallFrame.set(a16, o5);
                                                                                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                        a17 = getA8(n3);
                                                                                                                                                                                                                                                                                                                                        b7 = getB9(n3);
                                                                                                                                                                                                                                                                                                                                        c9 = getC9(n3);
                                                                                                                                                                                                                                                                                                                                        o6 = luaCallFrame.get(c9);
                                                                                                                                                                                                                                                                                                                                        --c9;
                                                                                                                                                                                                                                                                                                                                        break Label_2229;
                                                                                                                                                                                                                                                                                                                                        a18 = getA8(n3);
                                                                                                                                                                                                                                                                                                                                        b11 = getB9(n3);
                                                                                                                                                                                                                                                                                                                                        c12 = getC9(n3);
                                                                                                                                                                                                                                                                                                                                        break Block_8;
                                                                                                                                                                                                                                                                                                                                        luaCallFrame.localBase = localBase;
                                                                                                                                                                                                                                                                                                                                        luaCallFrame.nArguments = nArguments;
                                                                                                                                                                                                                                                                                                                                        luaCallFrame.closure = (LuaClosure)prepareMetatableCall;
                                                                                                                                                                                                                                                                                                                                        luaCallFrame.init();
                                                                                                                                                                                                                                                                                                                                        continue Label_5748;
                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                    ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                BaseLib.fail(str2 + " not defined for operand");
                                                                                                                                                                                                                                                                                                                                break Label_3991;
                                                                                                                                                                                                                                                                                                                                luaCallFrame.pushVarargs(getA8(n3), getB9(n3) - 1);
                                                                                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                luaCallFrame.set(getA8(n3), luaClosure.upvalues[getB9(n3)].getValue());
                                                                                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                                o2 = toDouble(-fromDouble(rawTonumber3));
                                                                                                                                                                                                                                                                                                                                continue Label_1983;
                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                            luaCallFrame.clearFromIndex(a19);
                                                                                                                                                                                                                                                                                                                            continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                                            Label_4030: {
                                                                                                                                                                                                                                                                                                                                n8 = 0;
                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                            continue Label_3694;
                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                        Label_4978: {
                                                                                                                                                                                                                                                                                                                            return;
                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                        Label_4024:
                                                                                                                                                                                                                                                                                                                        n7 = 0;
                                                                                                                                                                                                                                                                                                                        continue Label_3912;
                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                    luaCallFrame.localBase = luaCallFrame.returnBase;
                                                                                                                                                                                                                                                                                                                    currentThread = this.currentThread;
                                                                                                                                                                                                                                                                                                                    CoroutineLib.yieldHelper(luaCallFrame, luaCallFrame, n19);
                                                                                                                                                                                                                                                                                                                    currentThread.popCallFrame();
                                                                                                                                                                                                                                                                                                                    continue Label_6669;
                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                continue Label_6435_Outer;
                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                            continue Block_63_Outer;
                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                    break Block_55;
                                                                                                                                                                                                                                                                                                    a9 = getA8(n3);
                                                                                                                                                                                                                                                                                                    b12 = getB9(n3);
                                                                                                                                                                                                                                                                                                    c13 = getC9(n3);
                                                                                                                                                                                                                                                                                                    registerOrConstant = this.getRegisterOrConstant(luaCallFrame, b12, luaPrototype);
                                                                                                                                                                                                                                                                                                    registerOrConstant2 = this.getRegisterOrConstant(luaCallFrame, c13, luaPrototype);
                                                                                                                                                                                                                                                                                                    break Block_25;
                                                                                                                                                                                                                                                                                                    luaCallFrame.set(a18, b5);
                                                                                                                                                                                                                                                                                                    break Block_9;
                                                                                                                                                                                                                                                                                                    a20 = getA8(n3);
                                                                                                                                                                                                                                                                                                    b13 = getB9(n3);
                                                                                                                                                                                                                                                                                                    registerOrConstant5 = this.getRegisterOrConstant(luaCallFrame, getC9(n3), luaPrototype);
                                                                                                                                                                                                                                                                                                    value5 = luaCallFrame.get(b13);
                                                                                                                                                                                                                                                                                                    luaCallFrame.set(a20, this.tableGet(value5, registerOrConstant5));
                                                                                                                                                                                                                                                                                                    luaCallFrame.set(a20 + 1, value5);
                                                                                                                                                                                                                                                                                                    continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                o6 = this.call(binMetaOp, value, string, null);
                                                                                                                                                                                                                                                                                                c9 = n5 - 1;
                                                                                                                                                                                                                                                                                                continue Block_75_Outer;
                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                            ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                                            continue Label_0449_Outer;
                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                        fromDouble = fromDouble(registerOrConstant);
                                                                                                                                                                                                                                                                                        fromDouble2 = fromDouble(registerOrConstant2);
                                                                                                                                                                                                                                                                                        continue Label_3895_Outer;
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                    n20 = luaCallFrame.getTop() - a12 - 1;
                                                                                                                                                                                                                                                                                    break Label_7632;
                                                                                                                                                                                                                                                                                    this.callJava((JavaFunction)prepareMetatableCall2, n25, n22, n23);
                                                                                                                                                                                                                                                                                    currentCallFrame3 = this.currentThread.currentCallFrame();
                                                                                                                                                                                                                                                                                    return;
                                                                                                                                                                                                                                                                                    Label_3245: {
                                                                                                                                                                                                                                                                                        b10 = false;
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                    continue Label_3203;
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                BaseLib.fail("Object " + value6 + " did not have __call metatable set");
                                                                                                                                                                                                                                                                                break Label_5558;
                                                                                                                                                                                                                                                                                Label_5835: {
                                                                                                                                                                                                                                                                                    continue Label_3895_Outer;
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                            Label_3317: {
                                                                                                                                                                                                                                                                                n16 = 0;
                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                            break Label_3275;
                                                                                                                                                                                                                                                                            Label_3728:
                                                                                                                                                                                                                                                                            n28 = 0;
                                                                                                                                                                                                                                                                            str2 = LuaState.meta_ops[n4];
                                                                                                                                                                                                                                                                            compMetaOp2 = this.getCompMetaOp(registerOrConstant, registerOrConstant2, str2);
                                                                                                                                                                                                                                                                            o3 = registerOrConstant;
                                                                                                                                                                                                                                                                            o4 = registerOrConstant2;
                                                                                                                                                                                                                                                                            n9 = n28;
                                                                                                                                                                                                                                                                            break Block_52;
                                                                                                                                                                                                                                                                            rawTonumber2 = BaseLib.rawTonumber(registerOrConstant4);
                                                                                                                                                                                                                                                                            Label_1681:
                                                                                                                                                                                                                                                                            str = LuaState.meta_ops[n4];
                                                                                                                                                                                                                                                                            binMetaOp2 = this.getBinMetaOp(registerOrConstant3, registerOrConstant4, str);
                                                                                                                                                                                                                                                                            continue Label_3686_Outer;
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        luaCallFrame.stackClear(getA8(n3), getB9(n3));
                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                        Label_7508: {
                                                                                                                                                                                                                                                                            ++luaCallFrame.pc;
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                                                                        n29 = array[luaCallFrame.pc++];
                                                                                                                                                                                                                                                                        b6 = getB9(n29);
                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                    b5 = Boolean.FALSE;
                                                                                                                                                                                                                                                                    continue Label_0625;
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                a21 = getA8(n3);
                                                                                                                                                                                                                                                                sBx = getSBx(n3);
                                                                                                                                                                                                                                                                luaCallFrame.set(a21, toDouble(fromDouble(luaCallFrame.get(a21)) - fromDouble(luaCallFrame.get(a21 + 2))));
                                                                                                                                                                                                                                                                luaCallFrame.pc += sBx;
                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                luaClosure.upvalues[getB9(n3)].setValue(luaCallFrame.get(getA8(n3)));
                                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                                a22 = getA8(n3);
                                                                                                                                                                                                                                                                luaPrototype3 = luaPrototype.prototypes[getBx(n3)];
                                                                                                                                                                                                                                                                luaClosure3 = new LuaClosure(luaPrototype3, luaClosure.env);
                                                                                                                                                                                                                                                                luaCallFrame.set(a22, luaClosure3);
                                                                                                                                                                                                                                                                numUpvalues = luaPrototype3.numUpvalues;
                                                                                                                                                                                                                                                                n18 = 0;
                                                                                                                                                                                                                                                                continue Label_7956;
                                                                                                                                                                                                                                                                a23 = getA8(n3);
                                                                                                                                                                                                                                                                b14 = getB9(n3);
                                                                                                                                                                                                                                                                c14 = getC9(n3);
                                                                                                                                                                                                                                                                value7 = luaCallFrame.get(b14);
                                                                                                                                                                                                                                                                break Block_12_Outer;
                                                                                                                                                                                                                                                                return;
                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                            sb.append(BaseLib.rawTostring(luaCallFrame.get(n10)));
                                                                                                                                                                                                                                                            ++n10;
                                                                                                                                                                                                                                                            continue Label_2647;
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        n30 = array[luaCallFrame.pc++];
                                                                                                                                                                                                                                                        break Label_3630;
                                                                                                                                                                                                                                                        Label_4881: {
                                                                                                                                                                                                                                                            continue Block_73_Outer;
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                    a10 = getA8(n3);
                                                                                                                                                                                                                                                    value2 = luaCallFrame.get(getB9(n3));
                                                                                                                                                                                                                                                    continue Label_2229_Outer;
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                luaCallFrame.set(getA8(n3), luaPrototype.constants[getBx(n3)]);
                                                                                                                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                                                                                                                ++n18;
                                                                                                                                                                                                                                                continue Label_7956;
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            localBase = n;
                                                                                                                                                                                                                                            nArguments = n27 + 1;
                                                                                                                                                                                                                                            break Label_5585;
                                                                                                                                                                                                                                            break Label_3348;
                                                                                                                                                                                                                                            Label_5155: {
                                                                                                                                                                                                                                                throw new RuntimeException("Tried to call a non-function: " + prepareMetatableCall2);
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        o3 = registerOrConstant;
                                                                                                                                                                                                                                        o4 = registerOrConstant2;
                                                                                                                                                                                                                                        n9 = n28;
                                                                                                                                                                                                                                        compMetaOp = compMetaOp2;
                                                                                                                                                                                                                                        continue Label_3568_Outer;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    a15 = getA8(n3);
                                                                                                                                                                                                                                    b15 = getB9(n3);
                                                                                                                                                                                                                                    c15 = getC9(n3);
                                                                                                                                                                                                                                    n24 = b15 - 1;
                                                                                                                                                                                                                                    continue Label_4430_Outer;
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                localBase2 = luaCallFrame.localBase;
                                                                                                                                                                                                                                this.currentThread.closeUpvalues(localBase2);
                                                                                                                                                                                                                                a13 = getA8(n3);
                                                                                                                                                                                                                                continue Block_38_Outer;
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            ++luaCallFrame.pc;
                                                                                                                                                                                                                            continue Label_0449_Outer;
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        luaCallFrame.set(a14 + 2, value3);
                                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                                        a16 = getA8(n3);
                                                                                                                                                                                                                        b16 = getB9(n3);
                                                                                                                                                                                                                        c16 = getC9(n3);
                                                                                                                                                                                                                        registerOrConstant3 = this.getRegisterOrConstant(luaCallFrame, b16, luaPrototype);
                                                                                                                                                                                                                        registerOrConstant4 = this.getRegisterOrConstant(luaCallFrame, c16, luaPrototype);
                                                                                                                                                                                                                        rawTonumber = BaseLib.rawTonumber(registerOrConstant3);
                                                                                                                                                                                                                        continue Label_6158_Outer;
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    n17 = 1;
                                                                                                                                                                                                                    continue Label_3283;
                                                                                                                                                                                                                    Label_3516: {
                                                                                                                                                                                                                        compareTo = ((String)registerOrConstant).compareTo((String)registerOrConstant2);
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    continue Block_73_Outer;
                                                                                                                                                                                                                }
                                                                                                                                                                                                                this.currentThread.stackCopy(localBase2 + a13, n, nArguments + 1);
                                                                                                                                                                                                                this.currentThread.setTop(n + nArguments + 1);
                                                                                                                                                                                                                continue Block_12_Outer;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            continue Label_4430_Outer;
                                                                                                                                                                                                        }
                                                                                                                                                                                                        ++luaCallFrame.pc;
                                                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                                                        a19 = getA8(n3);
                                                                                                                                                                                                        fromDouble4 = fromDouble(luaCallFrame.get(a19));
                                                                                                                                                                                                        fromDouble3 = fromDouble(luaCallFrame.get(a19 + 1));
                                                                                                                                                                                                        fromDouble5 = fromDouble(luaCallFrame.get(a19 + 2));
                                                                                                                                                                                                        n15 = fromDouble4 + fromDouble5;
                                                                                                                                                                                                        double1 = toDouble(n15);
                                                                                                                                                                                                        luaCallFrame.set(a19, double1);
                                                                                                                                                                                                        break Label_3895;
                                                                                                                                                                                                    }
                                                                                                                                                                                                    b4 = luaEquals(o3, o4);
                                                                                                                                                                                                    continue Label_3895;
                                                                                                                                                                                                    a11 = getA8(n3);
                                                                                                                                                                                                    n32 = getB9(n3) - 1;
                                                                                                                                                                                                    this.currentThread.closeUpvalues(luaCallFrame.localBase);
                                                                                                                                                                                                    n19 = n32;
                                                                                                                                                                                                    n19 = luaCallFrame.getTop() - a11;
                                                                                                                                                                                                    continue Block_67_Outer;
                                                                                                                                                                                                }
                                                                                                                                                                                                n7 = 1;
                                                                                                                                                                                                continue Label_3686;
                                                                                                                                                                                            }
                                                                                                                                                                                            n12 = 1;
                                                                                                                                                                                            continue Label_3340;
                                                                                                                                                                                        }
                                                                                                                                                                                        b3 = true;
                                                                                                                                                                                        continue Label_3568;
                                                                                                                                                                                    }
                                                                                                                                                                                    this.currentThread.parent = currentThread2.parent;
                                                                                                                                                                                    currentThread2.parent = null;
                                                                                                                                                                                    this.currentThread.parent.currentCallFrame().push(Boolean.TRUE);
                                                                                                                                                                                    continue Block_91_Outer;
                                                                                                                                                                                }
                                                                                                                                                                                o = toDouble(((String)value2).length());
                                                                                                                                                                                continue Label_2229;
                                                                                                                                                                            }
                                                                                                                                                                            rawTostring = BaseLib.rawTostring(o6);
                                                                                                                                                                            n5 = c9;
                                                                                                                                                                            continue Label_3283_Outer;
                                                                                                                                                                        }
                                                                                                                                                                        luaCallFrame.set(getA8(n3), this.tableGet(luaCallFrame.get(getB9(n3)), this.getRegisterOrConstant(luaCallFrame, getC9(n3), luaPrototype)));
                                                                                                                                                                        continue Label_0449_Outer;
                                                                                                                                                                    }
                                                                                                                                                                    luaCallFrame.restoreTop = false;
                                                                                                                                                                    value6 = luaCallFrame.get(a13);
                                                                                                                                                                    BaseLib.luaAssert(value6 != null, "Tried to call nil");
                                                                                                                                                                    prepareMetatableCall = this.prepareMetatableCall(value6);
                                                                                                                                                                    continue Label_3275_Outer;
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                            n11 = 1;
                                                                                                                                                            continue Label_3630;
                                                                                                                                                        }
                                                                                                                                                        luaTable = (LuaTable)luaCallFrame.get(a12);
                                                                                                                                                        n31 = 1;
                                                                                                                                                        continue Label_7718;
                                                                                                                                                        a8 = getA8(n3);
                                                                                                                                                        value8 = luaCallFrame.get(getB9(n3));
                                                                                                                                                        rawTonumber3 = BaseLib.rawTonumber(value8);
                                                                                                                                                        continue Label_3275_Outer;
                                                                                                                                                    }
                                                                                                                                                    continue Block_32_Outer;
                                                                                                                                                }
                                                                                                                                                luaCallFrame.pc += getSBx(n3);
                                                                                                                                                luaCallFrame.set(a19 + 3, double1);
                                                                                                                                                continue Label_0449_Outer;
                                                                                                                                                Label_3394: {
                                                                                                                                                    continue Label_4430_Outer;
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            luaCallFrame.set(a23, value7);
                                                                                                                                            continue Label_0449_Outer;
                                                                                                                                            continue Block_34_Outer;
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    continue Block_74_Outer;
                                                                                                                                }
                                                                                                                                luaCallFrame.restoreTop = (c15 != 0);
                                                                                                                                localBase3 = luaCallFrame.localBase;
                                                                                                                                n25 = localBase3 + a15 + 1;
                                                                                                                                n22 = localBase3 + a15;
                                                                                                                                value4 = luaCallFrame.get(a15);
                                                                                                                                BaseLib.luaAssert(value4 != null, "Tried to call nil");
                                                                                                                                prepareMetatableCall2 = this.prepareMetatableCall(value4);
                                                                                                                                continue Block_20_Outer;
                                                                                                                            }
                                                                                                                            localBase = n + 1;
                                                                                                                            nArguments = n27;
                                                                                                                            continue Label_7718_Outer;
                                                                                                                        }
                                                                                                                        n16 = 1;
                                                                                                                        continue Label_3275;
                                                                                                                    }
                                                                                                                    value9 = luaCallFrame.get(n14);
                                                                                                                    --n14;
                                                                                                                    break Label_2587;
                                                                                                                    Label_3329: {
                                                                                                                        continue Block_79_Outer;
                                                                                                                    }
                                                                                                                }
                                                                                                                Label_3323: {
                                                                                                                    n17 = 0;
                                                                                                                }
                                                                                                                continue Label_3283;
                                                                                                            }
                                                                                                            Label_4841: {
                                                                                                                n24 = luaCallFrame.getTop() - a15 - 1;
                                                                                                            }
                                                                                                            continue Label_4430;
                                                                                                        }
                                                                                                        Label_2932: {
                                                                                                            luaCallFrame.set(a17, o6);
                                                                                                        }
                                                                                                        continue Label_0449_Outer;
                                                                                                    }
                                                                                                    n23 = n24;
                                                                                                    continue Label_3622_Outer;
                                                                                                }
                                                                                                Label_8101: {
                                                                                                    luaClosure3.upvalues[n18] = luaClosure.upvalues[b6];
                                                                                                }
                                                                                                continue Label_8064;
                                                                                            }
                                                                                            luaCallFrame.set(getA8(n3), this.tableGet(luaClosure.env, luaPrototype.constants[getBx(n3)]));
                                                                                            continue Label_0449_Outer;
                                                                                        }
                                                                                        pushNewCallFrame = this.currentThread.pushNewCallFrame((LuaClosure)prepareMetatableCall2, null, n25, n22, n23, true, luaCallFrame.insideCoroutine);
                                                                                        pushNewCallFrame.init();
                                                                                        luaCallFrame = pushNewCallFrame;
                                                                                        luaClosure = pushNewCallFrame.closure;
                                                                                        luaPrototype = luaClosure.prototype;
                                                                                        array = luaPrototype.code;
                                                                                        n = luaCallFrame.returnBase;
                                                                                        continue Label_0449_Outer;
                                                                                        Label_2254: {
                                                                                            continue Label_7195_Outer;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                continue;
                                                                            }
                                                                        }
                                                                        b4 = boolEval(this.call(compMetaOp, o3, o4, null));
                                                                        continue Label_3895;
                                                                    }
                                                                    continue;
                                                                }
                                                            }
                                                            currentThread2 = this.currentThread;
                                                            this.callJava((JavaFunction)prepareMetatableCall, localBase, n, nArguments);
                                                            currentCallFrame5 = this.currentThread.currentCallFrame();
                                                            currentThread2.popCallFrame();
                                                            break Label_5748;
                                                        }
                                                        n5 = c9;
                                                        string = o6;
                                                        continue Label_7718_Outer;
                                                    }
                                                    Label_2008: {
                                                        o2 = this.call(this.getMetaOp(value8, "__unm"), value8, null, null);
                                                    }
                                                    continue Label_1983;
                                                }
                                                b8 = true;
                                                continue Label_3211;
                                            }
                                            Label_3664: {
                                                n26 = 0;
                                            }
                                            continue Label_3622;
                                        }
                                        luaCallFrame2.setTop(luaCallFrame2.closure.prototype.maxStacksize);
                                        luaCallFrame = luaCallFrame2;
                                        continue Label_5748;
                                    }
                                    continue;
                                }
                            }
                            n6 = 1;
                            continue Label_3348;
                        }
                        luaTable.rawset(toDouble((n30 - 1) * 50 + n31), luaCallFrame.get(a12 + n31));
                        ++n31;
                        continue Label_7718;
                    }
                }
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
