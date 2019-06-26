package p009se.krka.kahlua.p010vm;

import android.support.p000v4.view.InputDeviceCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;
import p009se.krka.kahlua.stdlib.BaseLib;
import p009se.krka.kahlua.stdlib.CoroutineLib;
import p009se.krka.kahlua.stdlib.MathLib;
import p009se.krka.kahlua.stdlib.OsLib;
import p009se.krka.kahlua.stdlib.StringLib;
import p009se.krka.kahlua.stdlib.TableLib;

/* renamed from: se.krka.kahlua.vm.LuaState */
public class LuaState {
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
    private static final String[] meta_ops = new String[38];
    private final LuaTable classMetatables;
    public LuaThread currentThread;
    protected final PrintStream out;
    public final Random random;
    private final LuaTable userdataMetatables;

    static {
        meta_ops[12] = "__add";
        meta_ops[13] = "__sub";
        meta_ops[14] = "__mul";
        meta_ops[15] = "__div";
        meta_ops[16] = "__mod";
        meta_ops[17] = "__pow";
        meta_ops[23] = "__eq";
        meta_ops[24] = "__lt";
        meta_ops[25] = "__le";
    }

    public LuaState(PrintStream stream) {
        this(stream, true);
    }

    public LuaState() {
        this(System.out, true);
    }

    protected LuaState(PrintStream stream, boolean callReset) {
        this.random = new Random();
        LuaTable weakKeyMetatable = new LuaTableImpl();
        weakKeyMetatable.rawset("__mode", "k");
        this.userdataMetatables = new LuaTableImpl();
        this.userdataMetatables.setMetatable(weakKeyMetatable);
        this.classMetatables = new LuaTableImpl();
        this.out = stream;
        if (callReset) {
            reset();
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void reset() {
        this.currentThread = new LuaThread(this, new LuaTableImpl());
        getEnvironment().rawset("_G", getEnvironment());
        getEnvironment().rawset("_VERSION", "Lua 5.1 for CLDC 1.1");
        BaseLib.register(this);
        StringLib.register(this);
        MathLib.register(this);
        CoroutineLib.register(this);
        OsLib.register(this);
        TableLib.register(this);
    }

    public int call(int nArguments) {
        int base = (this.currentThread.getTop() - nArguments) - 1;
        Object o = this.currentThread.objectStack[base];
        if (o == null) {
            throw new RuntimeException("tried to call nil");
        } else if (o instanceof JavaFunction) {
            return callJava((JavaFunction) o, base + 1, base, nArguments);
        } else {
            if (o instanceof LuaClosure) {
                this.currentThread.pushNewCallFrame((LuaClosure) o, null, base + 1, base, nArguments, false, false).init();
                luaMainloop();
                int nReturnValues = this.currentThread.getTop() - base;
                this.currentThread.stackTrace = "";
                return nReturnValues;
            }
            throw new RuntimeException("tried to call a non-function");
        }
    }

    private int callJava(JavaFunction f, int localBase, int returnBase, int nArguments) {
        LuaThread thread = this.currentThread;
        LuaCallFrame callFrame = thread.pushNewCallFrame(null, f, localBase, returnBase, nArguments, false, false);
        int nReturnValues = f.call(callFrame, nArguments);
        int diff = returnBase - localBase;
        callFrame.stackCopy(callFrame.getTop() - nReturnValues, diff, nReturnValues);
        callFrame.setTop(nReturnValues + diff);
        thread.popCallFrame();
        return nReturnValues;
    }

    private final Object prepareMetatableCall(Object o) {
        if ((o instanceof JavaFunction) || (o instanceof LuaClosure)) {
            return o;
        }
        return getMetaOp(o, "__call");
    }

    /* JADX WARNING: Removed duplicated region for block: B:81:0x03d5 A:{Catch:{ RuntimeException -> 0x0048 }} */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:325:0x0020 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:318:0x00cf A:{SYNTHETIC} */
    private final void luaMainloop() {
        /*
        r81 = this;
        r0 = r81;
        r4 = r0.currentThread;
        r26 = r4.currentCallFrame();
        r0 = r26;
        r0 = r0.closure;
        r30 = r0;
        r0 = r30;
        r0 = r0.prototype;
        r67 = r0;
        r0 = r67;
        r0 = r0.code;
        r64 = r0;
        r0 = r26;
        r0 = r0.returnBase;
        r72 = r0;
    L_0x0020:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r5 = r4 + 1;
        r0 = r26;
        r0.f79pc = r5;	 Catch:{ RuntimeException -> 0x0048 }
        r62 = r64[r4];	 Catch:{ RuntimeException -> 0x0048 }
        r63 = r62 & 63;
        switch(r63) {
            case 0: goto L_0x0032;
            case 1: goto L_0x00d0;
            case 2: goto L_0x00e5;
            case 3: goto L_0x010d;
            case 4: goto L_0x011e;
            case 5: goto L_0x0137;
            case 6: goto L_0x0158;
            case 7: goto L_0x018b;
            case 8: goto L_0x01ae;
            case 9: goto L_0x01c9;
            case 10: goto L_0x01fe;
            case 11: goto L_0x0210;
            case 12: goto L_0x024c;
            case 13: goto L_0x024c;
            case 14: goto L_0x024c;
            case 15: goto L_0x024c;
            case 16: goto L_0x024c;
            case 17: goto L_0x024c;
            case 18: goto L_0x02cf;
            case 19: goto L_0x030a;
            case 20: goto L_0x032e;
            case 21: goto L_0x0393;
            case 22: goto L_0x0462;
            case 23: goto L_0x0471;
            case 24: goto L_0x0471;
            case 25: goto L_0x0471;
            case 26: goto L_0x0616;
            case 27: goto L_0x063b;
            case 28: goto L_0x066f;
            case 29: goto L_0x0783;
            case 30: goto L_0x08de;
            case 31: goto L_0x09cf;
            case 32: goto L_0x099a;
            case 33: goto L_0x0a30;
            case 34: goto L_0x0a78;
            case 35: goto L_0x0aca;
            case 36: goto L_0x0ad5;
            case 37: goto L_0x0b39;
            default: goto L_0x0031;
        };	 Catch:{ RuntimeException -> 0x0048 }
    L_0x0031:
        goto L_0x0020;
    L_0x0032:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r16;
        r4 = r0.get(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r0.set(r12, r4);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0048:
        r35 = move-exception;
    L_0x0049:
        r0 = r81;
        r4 = r0.currentThread;
        r26 = r4.currentCallFrame();
        r4 = r26.isLua();
        if (r4 == 0) goto L_0x0b4c;
    L_0x0057:
        r71 = 1;
    L_0x0059:
        r0 = r81;
        r4 = r0.currentThread;
        r26 = r4.currentCallFrame();
        if (r26 != 0) goto L_0x0b5e;
    L_0x0063:
        r0 = r81;
        r4 = r0.currentThread;
        r0 = r4.parent;
        r65 = r0;
        if (r65 == 0) goto L_0x00c5;
    L_0x006d:
        r0 = r81;
        r4 = r0.currentThread;
        r5 = 0;
        r4.parent = r5;
        r57 = r65.currentCallFrame();
        r4 = java.lang.Boolean.FALSE;
        r0 = r57;
        r0.push(r4);
        r4 = r35.getMessage();
        r0 = r57;
        r0.push(r4);
        r0 = r81;
        r4 = r0.currentThread;
        r4 = r4.stackTrace;
        r0 = r57;
        r0.push(r4);
        r0 = r81;
        r4 = r0.currentThread;
        r4 = r4.state;
        r0 = r65;
        r4.currentThread = r0;
        r0 = r65;
        r1 = r81;
        r1.currentThread = r0;
        r0 = r81;
        r4 = r0.currentThread;
        r26 = r4.currentCallFrame();
        r0 = r26;
        r0 = r0.closure;
        r30 = r0;
        r0 = r30;
        r0 = r0.prototype;
        r67 = r0;
        r0 = r67;
        r0 = r0.code;
        r64 = r0;
        r0 = r26;
        r0 = r0.returnBase;
        r72 = r0;
        r71 = 0;
    L_0x00c5:
        if (r26 == 0) goto L_0x00cd;
    L_0x00c7:
        r4 = 0;
        r0 = r26;
        r0.closeUpvalues(r4);
    L_0x00cd:
        if (r71 == 0) goto L_0x0020;
    L_0x00cf:
        throw r35;
    L_0x00d0:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getBx(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r67;
        r4 = r0.constants;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4[r16];	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r0.set(r12, r4);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x00e5:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r25 = p009se.krka.kahlua.p010vm.LuaState.getC9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        if (r16 != 0) goto L_0x010a;
    L_0x00f3:
        r23 = java.lang.Boolean.FALSE;	 Catch:{ RuntimeException -> 0x0048 }
    L_0x00f5:
        r0 = r26;
        r1 = r23;
        r0.set(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        if (r25 == 0) goto L_0x0020;
    L_0x00fe:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + 1;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x010a:
        r23 = java.lang.Boolean.TRUE;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x00f5;
    L_0x010d:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r16;
        r0.stackClear(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x011e:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r30;
        r4 = r0.upvalues;	 Catch:{ RuntimeException -> 0x0048 }
        r79 = r4[r16];	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r79.getValue();	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r0.set(r12, r4);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0137:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getBx(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r30;
        r4 = r0.env;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r67;
        r5 = r0.constants;	 Catch:{ RuntimeException -> 0x0048 }
        r5 = r5[r16];	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r68 = r0.tableGet(r4, r5);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r68;
        r0.set(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0158:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r25 = p009se.krka.kahlua.p010vm.LuaState.getC9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r16;
        r17 = r0.get(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r26;
        r2 = r25;
        r3 = r67;
        r48 = r0.getRegisterOrConstant(r1, r2, r3);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r17;
        r2 = r48;
        r68 = r0.tableGet(r1, r2);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r68;
        r0.set(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x018b:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getBx(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r80 = r0.get(r12);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r67;
        r4 = r0.constants;	 Catch:{ RuntimeException -> 0x0048 }
        r48 = r4[r16];	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r30;
        r4 = r0.env;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r48;
        r2 = r80;
        r0.tableSet(r4, r1, r2);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x01ae:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r30;
        r4 = r0.upvalues;	 Catch:{ RuntimeException -> 0x0048 }
        r79 = r4[r16];	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r4 = r0.get(r12);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r79;
        r0.setValue(r4);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x01c9:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r25 = p009se.krka.kahlua.p010vm.LuaState.getC9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r14 = r0.get(r12);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r26;
        r2 = r16;
        r3 = r67;
        r48 = r0.getRegisterOrConstant(r1, r2, r3);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r26;
        r2 = r25;
        r3 = r67;
        r80 = r0.getRegisterOrConstant(r1, r2, r3);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r48;
        r2 = r80;
        r0.tableSet(r14, r1, r2);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x01fe:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r76 = new se.krka.kahlua.vm.LuaTableImpl;	 Catch:{ RuntimeException -> 0x0048 }
        r76.<init>();	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r76;
        r0.set(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0210:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r25 = p009se.krka.kahlua.p010vm.LuaState.getC9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r26;
        r2 = r25;
        r3 = r67;
        r48 = r0.getRegisterOrConstant(r1, r2, r3);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r16;
        r17 = r0.get(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r17;
        r2 = r48;
        r41 = r0.tableGet(r1, r2);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r41;
        r0.set(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r12 + 1;
        r0 = r26;
        r1 = r17;
        r0.set(r4, r1);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x024c:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r25 = p009se.krka.kahlua.p010vm.LuaState.getC9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r26;
        r2 = r16;
        r3 = r67;
        r22 = r0.getRegisterOrConstant(r1, r2, r3);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r26;
        r2 = r25;
        r3 = r67;
        r32 = r0.getRegisterOrConstant(r1, r2, r3);	 Catch:{ RuntimeException -> 0x0048 }
        r19 = 0;
        r27 = 0;
        r68 = 0;
        r19 = p009se.krka.kahlua.stdlib.BaseLib.rawTonumber(r22);	 Catch:{ RuntimeException -> 0x0048 }
        if (r19 == 0) goto L_0x0282;
    L_0x027c:
        r27 = p009se.krka.kahlua.stdlib.BaseLib.rawTonumber(r32);	 Catch:{ RuntimeException -> 0x0048 }
        if (r27 != 0) goto L_0x02c2;
    L_0x0282:
        r4 = meta_ops;	 Catch:{ RuntimeException -> 0x0048 }
        r51 = r4[r63];	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r22;
        r2 = r32;
        r3 = r51;
        r52 = r0.getBinMetaOp(r1, r2, r3);	 Catch:{ RuntimeException -> 0x0048 }
        if (r52 != 0) goto L_0x02ac;
    L_0x0294:
        r4 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x0048 }
        r4.<init>();	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r51;
        r4 = r4.append(r0);	 Catch:{ RuntimeException -> 0x0048 }
        r5 = " not defined for operands";
        r4 = r4.append(r5);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4.toString();	 Catch:{ RuntimeException -> 0x0048 }
        p009se.krka.kahlua.stdlib.BaseLib.fail(r4);	 Catch:{ RuntimeException -> 0x0048 }
    L_0x02ac:
        r4 = 0;
        r0 = r81;
        r1 = r52;
        r2 = r22;
        r3 = r32;
        r68 = r0.call(r1, r2, r3, r4);	 Catch:{ RuntimeException -> 0x0048 }
    L_0x02b9:
        r0 = r26;
        r1 = r68;
        r0.set(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x02c2:
        r0 = r81;
        r1 = r19;
        r2 = r27;
        r3 = r63;
        r68 = r0.primitiveMath(r1, r2, r3);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x02b9;
    L_0x02cf:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r16;
        r14 = r0.get(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r13 = p009se.krka.kahlua.stdlib.BaseLib.rawTonumber(r14);	 Catch:{ RuntimeException -> 0x0048 }
        if (r13 == 0) goto L_0x02f7;
    L_0x02e5:
        r4 = p009se.krka.kahlua.p010vm.LuaState.fromDouble(r13);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = -r4;
        r68 = p009se.krka.kahlua.p010vm.LuaState.toDouble(r4);	 Catch:{ RuntimeException -> 0x0048 }
    L_0x02ee:
        r0 = r26;
        r1 = r68;
        r0.set(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x02f7:
        r4 = "__unm";
        r0 = r81;
        r52 = r0.getMetaOp(r14, r4);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = 0;
        r5 = 0;
        r0 = r81;
        r1 = r52;
        r68 = r0.call(r1, r14, r4, r5);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x02ee;
    L_0x030a:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r16;
        r14 = r0.get(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = p009se.krka.kahlua.p010vm.LuaState.boolEval(r14);	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 != 0) goto L_0x032c;
    L_0x0320:
        r4 = 1;
    L_0x0321:
        r4 = p009se.krka.kahlua.p010vm.LuaState.toBoolean(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r0.set(r12, r4);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x032c:
        r4 = 0;
        goto L_0x0321;
    L_0x032e:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r16;
        r59 = r0.get(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r59;
        r4 = r0 instanceof p009se.krka.kahlua.p010vm.LuaTable;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x035c;
    L_0x0344:
        r0 = r59;
        r0 = (p009se.krka.kahlua.p010vm.LuaTable) r0;	 Catch:{ RuntimeException -> 0x0048 }
        r76 = r0;
        r4 = r76.len();	 Catch:{ RuntimeException -> 0x0048 }
        r4 = (long) r4;	 Catch:{ RuntimeException -> 0x0048 }
        r68 = p009se.krka.kahlua.p010vm.LuaState.toDouble(r4);	 Catch:{ RuntimeException -> 0x0048 }
    L_0x0353:
        r0 = r26;
        r1 = r68;
        r0.set(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x035c:
        r0 = r59;
        r4 = r0 instanceof java.lang.String;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x0372;
    L_0x0362:
        r0 = r59;
        r0 = (java.lang.String) r0;	 Catch:{ RuntimeException -> 0x0048 }
        r73 = r0;
        r4 = r73.length();	 Catch:{ RuntimeException -> 0x0048 }
        r4 = (long) r4;	 Catch:{ RuntimeException -> 0x0048 }
        r68 = p009se.krka.kahlua.p010vm.LuaState.toDouble(r4);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0353;
    L_0x0372:
        r4 = "__len";
        r0 = r81;
        r1 = r59;
        r38 = r0.getMetaOp(r1, r4);	 Catch:{ RuntimeException -> 0x0048 }
        if (r38 == 0) goto L_0x0391;
    L_0x037e:
        r4 = 1;
    L_0x037f:
        r5 = "__len not defined for operand";
        p009se.krka.kahlua.stdlib.BaseLib.luaAssert(r4, r5);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = 0;
        r5 = 0;
        r0 = r81;
        r1 = r38;
        r2 = r59;
        r68 = r0.call(r1, r2, r4, r5);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0353;
    L_0x0391:
        r4 = 0;
        goto L_0x037f;
    L_0x0393:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r25 = p009se.krka.kahlua.p010vm.LuaState.getC9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r39 = r16;
        r49 = r25;
        r0 = r26;
        r1 = r49;
        r68 = r0.get(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r49 = r49 + -1;
    L_0x03ad:
        r0 = r39;
        r1 = r49;
        if (r0 > r1) goto L_0x0459;
    L_0x03b3:
        r70 = p009se.krka.kahlua.stdlib.BaseLib.rawTostring(r68);	 Catch:{ RuntimeException -> 0x0048 }
        if (r68 == 0) goto L_0x0408;
    L_0x03b9:
        r53 = 0;
        r66 = r49;
    L_0x03bd:
        r0 = r39;
        r1 = r66;
        if (r0 > r1) goto L_0x03d3;
    L_0x03c3:
        r0 = r26;
        r1 = r66;
        r59 = r0.get(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r66 = r66 + -1;
        r4 = p009se.krka.kahlua.stdlib.BaseLib.rawTostring(r59);	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 != 0) goto L_0x03f8;
    L_0x03d3:
        if (r53 <= 0) goto L_0x0408;
    L_0x03d5:
        r33 = new java.lang.StringBuffer;	 Catch:{ RuntimeException -> 0x0048 }
        r33.<init>();	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r49 - r53;
        r40 = r4 + 1;
    L_0x03de:
        r0 = r40;
        r1 = r49;
        if (r0 > r1) goto L_0x03fb;
    L_0x03e4:
        r0 = r26;
        r1 = r40;
        r4 = r0.get(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = p009se.krka.kahlua.stdlib.BaseLib.rawTostring(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r33;
        r0.append(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r40 = r40 + 1;
        goto L_0x03de;
    L_0x03f8:
        r53 = r53 + 1;
        goto L_0x03bd;
    L_0x03fb:
        r0 = r33;
        r1 = r70;
        r0.append(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r68 = r33.toString();	 Catch:{ RuntimeException -> 0x0048 }
        r49 = r49 - r53;
    L_0x0408:
        r0 = r39;
        r1 = r49;
        if (r0 > r1) goto L_0x03ad;
    L_0x040e:
        r0 = r26;
        r1 = r49;
        r50 = r0.get(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = "__concat";
        r0 = r81;
        r1 = r50;
        r2 = r68;
        r52 = r0.getBinMetaOp(r1, r2, r4);	 Catch:{ RuntimeException -> 0x0048 }
        if (r52 != 0) goto L_0x0448;
    L_0x0424:
        r4 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x0048 }
        r4.<init>();	 Catch:{ RuntimeException -> 0x0048 }
        r5 = "__concat not defined for operands: ";
        r4 = r4.append(r5);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r50;
        r4 = r4.append(r0);	 Catch:{ RuntimeException -> 0x0048 }
        r5 = " and ";
        r4 = r4.append(r5);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r68;
        r4 = r4.append(r0);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4.toString();	 Catch:{ RuntimeException -> 0x0048 }
        p009se.krka.kahlua.stdlib.BaseLib.fail(r4);	 Catch:{ RuntimeException -> 0x0048 }
    L_0x0448:
        r4 = 0;
        r0 = r81;
        r1 = r52;
        r2 = r50;
        r3 = r68;
        r68 = r0.call(r1, r2, r3, r4);	 Catch:{ RuntimeException -> 0x0048 }
        r49 = r49 + -1;
        goto L_0x03ad;
    L_0x0459:
        r0 = r26;
        r1 = r68;
        r0.set(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0462:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r5 = p009se.krka.kahlua.p010vm.LuaState.getSBx(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + r5;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0471:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r25 = p009se.krka.kahlua.p010vm.LuaState.getC9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r26;
        r2 = r16;
        r3 = r67;
        r22 = r0.getRegisterOrConstant(r1, r2, r3);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r26;
        r2 = r25;
        r3 = r67;
        r32 = r0.getRegisterOrConstant(r1, r2, r3);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r22;
        r4 = r0 instanceof java.lang.Double;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x0509;
    L_0x049b:
        r0 = r32;
        r4 = r0 instanceof java.lang.Double;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x0509;
    L_0x04a1:
        r20 = p009se.krka.kahlua.p010vm.LuaState.fromDouble(r22);	 Catch:{ RuntimeException -> 0x0048 }
        r28 = p009se.krka.kahlua.p010vm.LuaState.fromDouble(r32);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = 23;
        r0 = r63;
        if (r0 != r4) goto L_0x04cb;
    L_0x04af:
        r4 = (r20 > r28 ? 1 : (r20 == r28 ? 0 : -1));
        if (r4 != 0) goto L_0x04c6;
    L_0x04b3:
        r4 = 1;
        r5 = r4;
    L_0x04b5:
        if (r12 != 0) goto L_0x04c9;
    L_0x04b7:
        r4 = 1;
    L_0x04b8:
        if (r5 != r4) goto L_0x0020;
    L_0x04ba:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + 1;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x04c6:
        r4 = 0;
        r5 = r4;
        goto L_0x04b5;
    L_0x04c9:
        r4 = 0;
        goto L_0x04b8;
    L_0x04cb:
        r4 = 24;
        r0 = r63;
        if (r0 != r4) goto L_0x04ed;
    L_0x04d1:
        r4 = (r20 > r28 ? 1 : (r20 == r28 ? 0 : -1));
        if (r4 >= 0) goto L_0x04e8;
    L_0x04d5:
        r4 = 1;
        r5 = r4;
    L_0x04d7:
        if (r12 != 0) goto L_0x04eb;
    L_0x04d9:
        r4 = 1;
    L_0x04da:
        if (r5 != r4) goto L_0x0020;
    L_0x04dc:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + 1;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x04e8:
        r4 = 0;
        r5 = r4;
        goto L_0x04d7;
    L_0x04eb:
        r4 = 0;
        goto L_0x04da;
    L_0x04ed:
        r4 = (r20 > r28 ? 1 : (r20 == r28 ? 0 : -1));
        if (r4 > 0) goto L_0x0504;
    L_0x04f1:
        r4 = 1;
        r5 = r4;
    L_0x04f3:
        if (r12 != 0) goto L_0x0507;
    L_0x04f5:
        r4 = 1;
    L_0x04f6:
        if (r5 != r4) goto L_0x0020;
    L_0x04f8:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + 1;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0504:
        r4 = 0;
        r5 = r4;
        goto L_0x04f3;
    L_0x0507:
        r4 = 0;
        goto L_0x04f6;
    L_0x0509:
        r0 = r22;
        r4 = r0 instanceof java.lang.String;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x0584;
    L_0x050f:
        r0 = r32;
        r4 = r0 instanceof java.lang.String;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x0584;
    L_0x0515:
        r4 = 23;
        r0 = r63;
        if (r0 != r4) goto L_0x0536;
    L_0x051b:
        r0 = r22;
        r1 = r32;
        r5 = r0.equals(r1);	 Catch:{ RuntimeException -> 0x0048 }
        if (r12 != 0) goto L_0x0534;
    L_0x0525:
        r4 = 1;
    L_0x0526:
        if (r5 != r4) goto L_0x0020;
    L_0x0528:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + 1;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0534:
        r4 = 0;
        goto L_0x0526;
    L_0x0536:
        r0 = r22;
        r0 = (java.lang.String) r0;	 Catch:{ RuntimeException -> 0x0048 }
        r24 = r0;
        r0 = r32;
        r0 = (java.lang.String) r0;	 Catch:{ RuntimeException -> 0x0048 }
        r34 = r0;
        r0 = r24;
        r1 = r34;
        r31 = r0.compareTo(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = 24;
        r0 = r63;
        if (r0 != r4) goto L_0x056a;
    L_0x0550:
        if (r31 >= 0) goto L_0x0565;
    L_0x0552:
        r4 = 1;
        r5 = r4;
    L_0x0554:
        if (r12 != 0) goto L_0x0568;
    L_0x0556:
        r4 = 1;
    L_0x0557:
        if (r5 != r4) goto L_0x0020;
    L_0x0559:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + 1;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0565:
        r4 = 0;
        r5 = r4;
        goto L_0x0554;
    L_0x0568:
        r4 = 0;
        goto L_0x0557;
    L_0x056a:
        if (r31 > 0) goto L_0x057f;
    L_0x056c:
        r4 = 1;
        r5 = r4;
    L_0x056e:
        if (r12 != 0) goto L_0x0582;
    L_0x0570:
        r4 = 1;
    L_0x0571:
        if (r5 != r4) goto L_0x0020;
    L_0x0573:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + 1;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x057f:
        r4 = 0;
        r5 = r4;
        goto L_0x056e;
    L_0x0582:
        r4 = 0;
        goto L_0x0571;
    L_0x0584:
        r0 = r22;
        r1 = r32;
        if (r0 != r1) goto L_0x059f;
    L_0x058a:
        r69 = 1;
    L_0x058c:
        if (r12 != 0) goto L_0x0613;
    L_0x058e:
        r4 = 1;
    L_0x058f:
        r0 = r69;
        if (r0 != r4) goto L_0x0020;
    L_0x0593:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + 1;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x059f:
        r44 = 0;
        r4 = meta_ops;	 Catch:{ RuntimeException -> 0x0048 }
        r51 = r4[r63];	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r22;
        r2 = r32;
        r3 = r51;
        r52 = r0.getCompMetaOp(r1, r2, r3);	 Catch:{ RuntimeException -> 0x0048 }
        if (r52 != 0) goto L_0x05cd;
    L_0x05b3:
        r4 = 25;
        r0 = r63;
        if (r0 != r4) goto L_0x05cd;
    L_0x05b9:
        r4 = "__lt";
        r0 = r81;
        r1 = r22;
        r2 = r32;
        r52 = r0.getCompMetaOp(r1, r2, r4);	 Catch:{ RuntimeException -> 0x0048 }
        r78 = r22;
        r22 = r32;
        r32 = r78;
        r44 = 1;
    L_0x05cd:
        if (r52 != 0) goto L_0x05e4;
    L_0x05cf:
        r4 = 23;
        r0 = r63;
        if (r0 != r4) goto L_0x05e4;
    L_0x05d5:
        r0 = r22;
        r1 = r32;
        r69 = p009se.krka.kahlua.p010vm.LuaState.luaEquals(r0, r1);	 Catch:{ RuntimeException -> 0x0048 }
    L_0x05dd:
        if (r44 == 0) goto L_0x058c;
    L_0x05df:
        if (r69 != 0) goto L_0x0610;
    L_0x05e1:
        r69 = 1;
    L_0x05e3:
        goto L_0x058c;
    L_0x05e4:
        if (r52 != 0) goto L_0x05fe;
    L_0x05e6:
        r4 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x0048 }
        r4.<init>();	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r51;
        r4 = r4.append(r0);	 Catch:{ RuntimeException -> 0x0048 }
        r5 = " not defined for operand";
        r4 = r4.append(r5);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4.toString();	 Catch:{ RuntimeException -> 0x0048 }
        p009se.krka.kahlua.stdlib.BaseLib.fail(r4);	 Catch:{ RuntimeException -> 0x0048 }
    L_0x05fe:
        r4 = 0;
        r0 = r81;
        r1 = r52;
        r2 = r22;
        r3 = r32;
        r68 = r0.call(r1, r2, r3, r4);	 Catch:{ RuntimeException -> 0x0048 }
        r69 = p009se.krka.kahlua.p010vm.LuaState.boolEval(r68);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x05dd;
    L_0x0610:
        r69 = 0;
        goto L_0x05e3;
    L_0x0613:
        r4 = 0;
        goto L_0x058f;
    L_0x0616:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r25 = p009se.krka.kahlua.p010vm.LuaState.getC9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r80 = r0.get(r12);	 Catch:{ RuntimeException -> 0x0048 }
        r5 = p009se.krka.kahlua.p010vm.LuaState.boolEval(r80);	 Catch:{ RuntimeException -> 0x0048 }
        if (r25 != 0) goto L_0x0639;
    L_0x062a:
        r4 = 1;
    L_0x062b:
        if (r5 != r4) goto L_0x0020;
    L_0x062d:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + 1;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0639:
        r4 = 0;
        goto L_0x062b;
    L_0x063b:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r25 = p009se.krka.kahlua.p010vm.LuaState.getC9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r16;
        r80 = r0.get(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r5 = p009se.krka.kahlua.p010vm.LuaState.boolEval(r80);	 Catch:{ RuntimeException -> 0x0048 }
        if (r25 != 0) goto L_0x0661;
    L_0x0655:
        r4 = 1;
    L_0x0656:
        if (r5 == r4) goto L_0x0663;
    L_0x0658:
        r0 = r26;
        r1 = r80;
        r0.set(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0661:
        r4 = 0;
        goto L_0x0656;
    L_0x0663:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + 1;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x066f:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r25 = p009se.krka.kahlua.p010vm.LuaState.getC9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r9 = r16 + -1;
        r4 = -1;
        if (r9 == r4) goto L_0x0713;
    L_0x0680:
        r4 = r12 + r9;
        r4 = r4 + 1;
        r0 = r26;
        r0.setTop(r4);	 Catch:{ RuntimeException -> 0x0048 }
    L_0x0689:
        if (r25 == 0) goto L_0x071c;
    L_0x068b:
        r4 = 1;
    L_0x068c:
        r0 = r26;
        r0.restoreTop = r4;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r0 = r0.localBase;	 Catch:{ RuntimeException -> 0x0048 }
        r18 = r0;
        r4 = r18 + r12;
        r7 = r4 + 1;
        r8 = r18 + r12;
        r0 = r26;
        r42 = r0.get(r12);	 Catch:{ RuntimeException -> 0x0048 }
        if (r42 == 0) goto L_0x071f;
    L_0x06a4:
        r4 = 1;
    L_0x06a5:
        r5 = "Tried to call nil";
        p009se.krka.kahlua.stdlib.BaseLib.luaAssert(r4, r5);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r42;
        r41 = r0.prepareMetatableCall(r1);	 Catch:{ RuntimeException -> 0x0048 }
        if (r41 != 0) goto L_0x06d2;
    L_0x06b4:
        r4 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x0048 }
        r4.<init>();	 Catch:{ RuntimeException -> 0x0048 }
        r5 = "Object ";
        r4 = r4.append(r5);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r42;
        r4 = r4.append(r0);	 Catch:{ RuntimeException -> 0x0048 }
        r5 = " did not have __call metatable set";
        r4 = r4.append(r5);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4.toString();	 Catch:{ RuntimeException -> 0x0048 }
        p009se.krka.kahlua.stdlib.BaseLib.fail(r4);	 Catch:{ RuntimeException -> 0x0048 }
    L_0x06d2:
        r0 = r41;
        r1 = r42;
        if (r0 == r1) goto L_0x06db;
    L_0x06d8:
        r7 = r8;
        r9 = r9 + 1;
    L_0x06db:
        r0 = r41;
        r4 = r0 instanceof p009se.krka.kahlua.p010vm.LuaClosure;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x0721;
    L_0x06e1:
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r41;
        r0 = (p009se.krka.kahlua.p010vm.LuaClosure) r0;	 Catch:{ RuntimeException -> 0x0048 }
        r5 = r0;
        r6 = 0;
        r10 = 1;
        r0 = r26;
        r11 = r0.insideCoroutine;	 Catch:{ RuntimeException -> 0x0048 }
        r54 = r4.pushNewCallFrame(r5, r6, r7, r8, r9, r10, r11);	 Catch:{ RuntimeException -> 0x0048 }
        r54.init();	 Catch:{ RuntimeException -> 0x0048 }
        r26 = r54;
        r0 = r54;
        r0 = r0.closure;	 Catch:{ RuntimeException -> 0x0048 }
        r30 = r0;
        r0 = r30;
        r0 = r0.prototype;	 Catch:{ RuntimeException -> 0x0048 }
        r67 = r0;
        r0 = r67;
        r0 = r0.code;	 Catch:{ RuntimeException -> 0x0048 }
        r64 = r0;
        r0 = r26;
        r0 = r0.returnBase;	 Catch:{ RuntimeException -> 0x0048 }
        r72 = r0;
        goto L_0x0020;
    L_0x0713:
        r4 = r26.getTop();	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 - r12;
        r9 = r4 + -1;
        goto L_0x0689;
    L_0x071c:
        r4 = 0;
        goto L_0x068c;
    L_0x071f:
        r4 = 0;
        goto L_0x06a5;
    L_0x0721:
        r0 = r41;
        r4 = r0 instanceof p009se.krka.kahlua.p010vm.JavaFunction;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x0768;
    L_0x0727:
        r41 = (p009se.krka.kahlua.p010vm.JavaFunction) r41;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r41;
        r0.callJava(r1, r7, r8, r9);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r26 = r4.currentCallFrame();	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r26.isJava();	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x073f;
    L_0x073e:
        return;
    L_0x073f:
        r0 = r26;
        r0 = r0.closure;	 Catch:{ RuntimeException -> 0x0048 }
        r30 = r0;
        r0 = r30;
        r0 = r0.prototype;	 Catch:{ RuntimeException -> 0x0048 }
        r67 = r0;
        r0 = r67;
        r0 = r0.code;	 Catch:{ RuntimeException -> 0x0048 }
        r64 = r0;
        r0 = r26;
        r0 = r0.returnBase;	 Catch:{ RuntimeException -> 0x0048 }
        r72 = r0;
        r0 = r26;
        r4 = r0.restoreTop;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x0020;
    L_0x075d:
        r0 = r67;
        r4 = r0.maxStacksize;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r0.setTop(r4);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0768:
        r4 = new java.lang.RuntimeException;	 Catch:{ RuntimeException -> 0x0048 }
        r5 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x0048 }
        r5.<init>();	 Catch:{ RuntimeException -> 0x0048 }
        r6 = "Tried to call a non-function: ";
        r5 = r5.append(r6);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r41;
        r5 = r5.append(r0);	 Catch:{ RuntimeException -> 0x0048 }
        r5 = r5.toString();	 Catch:{ RuntimeException -> 0x0048 }
        r4.<init>(r5);	 Catch:{ RuntimeException -> 0x0048 }
        throw r4;	 Catch:{ RuntimeException -> 0x0048 }
    L_0x0783:
        r0 = r26;
        r0 = r0.localBase;	 Catch:{ RuntimeException -> 0x0048 }
        r18 = r0;
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r18;
        r4.closeUpvalues(r0);	 Catch:{ RuntimeException -> 0x0048 }
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r9 = r16 + -1;
        r4 = -1;
        if (r9 != r4) goto L_0x07a6;
    L_0x079f:
        r4 = r26.getTop();	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 - r12;
        r9 = r4 + -1;
    L_0x07a6:
        r4 = 0;
        r0 = r26;
        r0.restoreTop = r4;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r42 = r0.get(r12);	 Catch:{ RuntimeException -> 0x0048 }
        if (r42 == 0) goto L_0x0838;
    L_0x07b3:
        r4 = 1;
    L_0x07b4:
        r5 = "Tried to call nil";
        p009se.krka.kahlua.stdlib.BaseLib.luaAssert(r4, r5);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r42;
        r41 = r0.prepareMetatableCall(r1);	 Catch:{ RuntimeException -> 0x0048 }
        if (r41 != 0) goto L_0x07e1;
    L_0x07c3:
        r4 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x0048 }
        r4.<init>();	 Catch:{ RuntimeException -> 0x0048 }
        r5 = "Object ";
        r4 = r4.append(r5);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r42;
        r4 = r4.append(r0);	 Catch:{ RuntimeException -> 0x0048 }
        r5 = " did not have __call metatable set";
        r4 = r4.append(r5);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4.toString();	 Catch:{ RuntimeException -> 0x0048 }
        p009se.krka.kahlua.stdlib.BaseLib.fail(r4);	 Catch:{ RuntimeException -> 0x0048 }
    L_0x07e1:
        r7 = r72 + 1;
        r0 = r41;
        r1 = r42;
        if (r0 == r1) goto L_0x07ed;
    L_0x07e9:
        r7 = r72;
        r9 = r9 + 1;
    L_0x07ed:
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r5 = r18 + r12;
        r6 = r9 + 1;
        r0 = r72;
        r4.stackCopy(r5, r0, r6);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r5 = r72 + r9;
        r5 = r5 + 1;
        r4.setTop(r5);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r41;
        r4 = r0 instanceof p009se.krka.kahlua.p010vm.LuaClosure;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x083b;
    L_0x080b:
        r0 = r26;
        r0.localBase = r7;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r0.nArguments = r9;	 Catch:{ RuntimeException -> 0x0048 }
        r41 = (p009se.krka.kahlua.p010vm.LuaClosure) r41;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r41;
        r1 = r26;
        r1.closure = r0;	 Catch:{ RuntimeException -> 0x0048 }
        r26.init();	 Catch:{ RuntimeException -> 0x0048 }
    L_0x081e:
        r0 = r26;
        r0 = r0.closure;	 Catch:{ RuntimeException -> 0x0048 }
        r30 = r0;
        r0 = r30;
        r0 = r0.prototype;	 Catch:{ RuntimeException -> 0x0048 }
        r67 = r0;
        r0 = r67;
        r0 = r0.code;	 Catch:{ RuntimeException -> 0x0048 }
        r64 = r0;
        r0 = r26;
        r0 = r0.returnBase;	 Catch:{ RuntimeException -> 0x0048 }
        r72 = r0;
        goto L_0x0020;
    L_0x0838:
        r4 = 0;
        goto L_0x07b4;
    L_0x083b:
        r0 = r41;
        r4 = r0 instanceof p009se.krka.kahlua.p010vm.JavaFunction;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 != 0) goto L_0x0859;
    L_0x0841:
        r4 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x0048 }
        r4.<init>();	 Catch:{ RuntimeException -> 0x0048 }
        r5 = "Tried to call a non-function: ";
        r4 = r4.append(r5);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r41;
        r4 = r4.append(r0);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4.toString();	 Catch:{ RuntimeException -> 0x0048 }
        p009se.krka.kahlua.stdlib.BaseLib.fail(r4);	 Catch:{ RuntimeException -> 0x0048 }
    L_0x0859:
        r0 = r81;
        r0 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r61 = r0;
        r41 = (p009se.krka.kahlua.p010vm.JavaFunction) r41;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r1 = r41;
        r2 = r72;
        r0.callJava(r1, r7, r2, r9);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r26 = r4.currentCallFrame();	 Catch:{ RuntimeException -> 0x0048 }
        r61.popCallFrame();	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r61;
        if (r0 == r4) goto L_0x08bb;
    L_0x087d:
        r4 = r61.isDead();	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x08ab;
    L_0x0883:
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4.parent;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r61;
        if (r4 != r0) goto L_0x08ab;
    L_0x088d:
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r61;
        r5 = r0.parent;	 Catch:{ RuntimeException -> 0x0048 }
        r4.parent = r5;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = 0;
        r0 = r61;
        r0.parent = r4;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4.parent;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4.currentCallFrame();	 Catch:{ RuntimeException -> 0x0048 }
        r5 = java.lang.Boolean.TRUE;	 Catch:{ RuntimeException -> 0x0048 }
        r4.push(r5);	 Catch:{ RuntimeException -> 0x0048 }
    L_0x08ab:
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r26 = r4.currentCallFrame();	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r26.isJava();	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x081e;
    L_0x08b9:
        goto L_0x073e;
    L_0x08bb:
        r0 = r26;
        r4 = r0.fromLua;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x073e;
    L_0x08c1:
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r26 = r4.currentCallFrame();	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r4 = r0.restoreTop;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x081e;
    L_0x08cf:
        r0 = r26;
        r4 = r0.closure;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4.prototype;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4.maxStacksize;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r0.setTop(r4);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x081e;
    L_0x08de:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = r4 + -1;
        r0 = r26;
        r0 = r0.localBase;	 Catch:{ RuntimeException -> 0x0048 }
        r18 = r0;
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r18;
        r4.closeUpvalues(r0);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = -1;
        r0 = r16;
        if (r0 != r4) goto L_0x0902;
    L_0x08fc:
        r4 = r26.getTop();	 Catch:{ RuntimeException -> 0x0048 }
        r16 = r4 - r12;
    L_0x0902:
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r5 = r0.localBase;	 Catch:{ RuntimeException -> 0x0048 }
        r5 = r5 + r12;
        r0 = r72;
        r1 = r16;
        r4.stackCopy(r5, r0, r1);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r5 = r72 + r16;
        r4.setTop(r5);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r4 = r0.fromLua;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x0991;
    L_0x0921:
        r0 = r26;
        r4 = r0.insideCoroutine;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x0989;
    L_0x0927:
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4.callFrameTop;	 Catch:{ RuntimeException -> 0x0048 }
        r5 = 1;
        if (r4 != r5) goto L_0x0989;
    L_0x0930:
        r0 = r26;
        r4 = r0.returnBase;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r0.localBase = r4;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r0 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r77 = r0;
        r0 = r26;
        r1 = r26;
        r2 = r16;
        p009se.krka.kahlua.stdlib.CoroutineLib.yieldHelper(r0, r1, r2);	 Catch:{ RuntimeException -> 0x0048 }
        r77.popCallFrame();	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r26 = r4.currentCallFrame();	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r26.isJava();	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 != 0) goto L_0x073e;
    L_0x0958:
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r26 = r4.currentCallFrame();	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r0 = r0.closure;	 Catch:{ RuntimeException -> 0x0048 }
        r30 = r0;
        r0 = r30;
        r0 = r0.prototype;	 Catch:{ RuntimeException -> 0x0048 }
        r67 = r0;
        r0 = r67;
        r0 = r0.code;	 Catch:{ RuntimeException -> 0x0048 }
        r64 = r0;
        r0 = r26;
        r0 = r0.returnBase;	 Catch:{ RuntimeException -> 0x0048 }
        r72 = r0;
        r0 = r26;
        r4 = r0.restoreTop;	 Catch:{ RuntimeException -> 0x0048 }
        if (r4 == 0) goto L_0x0020;
    L_0x097e:
        r0 = r67;
        r4 = r0.maxStacksize;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r0.setTop(r4);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0989:
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r4.popCallFrame();	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0958;
    L_0x0991:
        r0 = r81;
        r4 = r0.currentThread;	 Catch:{ RuntimeException -> 0x0048 }
        r4.popCallFrame();	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x073e;
    L_0x099a:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getSBx(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r4 = r0.get(r12);	 Catch:{ RuntimeException -> 0x0048 }
        r46 = p009se.krka.kahlua.p010vm.LuaState.fromDouble(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r12 + 2;
        r0 = r26;
        r4 = r0.get(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r74 = p009se.krka.kahlua.p010vm.LuaState.fromDouble(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r46 - r74;
        r4 = p009se.krka.kahlua.p010vm.LuaState.toDouble(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r0.set(r12, r4);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + r16;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x09cf:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r4 = r0.get(r12);	 Catch:{ RuntimeException -> 0x0048 }
        r46 = p009se.krka.kahlua.p010vm.LuaState.fromDouble(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r12 + 1;
        r0 = r26;
        r4 = r0.get(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r36 = p009se.krka.kahlua.p010vm.LuaState.fromDouble(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r12 + 2;
        r0 = r26;
        r4 = r0.get(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r74 = p009se.krka.kahlua.p010vm.LuaState.fromDouble(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r46 = r46 + r74;
        r45 = p009se.krka.kahlua.p010vm.LuaState.toDouble(r46);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r45;
        r0.set(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = 0;
        r4 = (r74 > r4 ? 1 : (r74 == r4 ? 0 : -1));
        if (r4 <= 0) goto L_0x0a25;
    L_0x0a08:
        r4 = (r46 > r36 ? 1 : (r46 == r36 ? 0 : -1));
        if (r4 > 0) goto L_0x0a29;
    L_0x0a0c:
        r16 = p009se.krka.kahlua.p010vm.LuaState.getSBx(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + r16;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r12 + 3;
        r0 = r26;
        r1 = r45;
        r0.set(r4, r1);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0a25:
        r4 = (r46 > r36 ? 1 : (r46 == r36 ? 0 : -1));
        if (r4 >= 0) goto L_0x0a0c;
    L_0x0a29:
        r0 = r26;
        r0.clearFromIndex(r12);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0a30:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r25 = p009se.krka.kahlua.p010vm.LuaState.getC9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r12 + 6;
        r0 = r26;
        r0.setTop(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r12 + 3;
        r5 = 3;
        r0 = r26;
        r0.stackCopy(r12, r4, r5);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = 2;
        r0 = r81;
        r0.call(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r12 + 3;
        r4 = r4 + r25;
        r0 = r26;
        r0.clearFromIndex(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r26.setPrototypeStacksize();	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r12 + 3;
        r0 = r26;
        r15 = r0.get(r4);	 Catch:{ RuntimeException -> 0x0048 }
        if (r15 == 0) goto L_0x0a6c;
    L_0x0a63:
        r4 = r12 + 2;
        r0 = r26;
        r0.set(r4, r15);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0a6c:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 + 1;
        r0 = r26;
        r0.f79pc = r4;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0a78:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r25 = p009se.krka.kahlua.p010vm.LuaState.getC9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        if (r16 != 0) goto L_0x0a8d;
    L_0x0a86:
        r4 = r26.getTop();	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r4 - r12;
        r16 = r4 + -1;
    L_0x0a8d:
        if (r25 != 0) goto L_0x0a9b;
    L_0x0a8f:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r5 = r4 + 1;
        r0 = r26;
        r0.f79pc = r5;	 Catch:{ RuntimeException -> 0x0048 }
        r25 = r64[r4];	 Catch:{ RuntimeException -> 0x0048 }
    L_0x0a9b:
        r4 = r25 + -1;
        r60 = r4 * 50;
        r0 = r26;
        r76 = r0.get(r12);	 Catch:{ RuntimeException -> 0x0048 }
        r76 = (p009se.krka.kahlua.p010vm.LuaTable) r76;	 Catch:{ RuntimeException -> 0x0048 }
        r43 = 1;
    L_0x0aa9:
        r0 = r43;
        r1 = r16;
        if (r0 > r1) goto L_0x0020;
    L_0x0aaf:
        r4 = r60 + r43;
        r4 = (long) r4;	 Catch:{ RuntimeException -> 0x0048 }
        r48 = p009se.krka.kahlua.p010vm.LuaState.toDouble(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = r12 + r43;
        r0 = r26;
        r80 = r0.get(r4);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r76;
        r1 = r48;
        r2 = r80;
        r0.rawset(r1, r2);	 Catch:{ RuntimeException -> 0x0048 }
        r43 = r43 + 1;
        goto L_0x0aa9;
    L_0x0aca:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r0.closeUpvalues(r12);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0ad5:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = p009se.krka.kahlua.p010vm.LuaState.getBx(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r67;
        r4 = r0.prototypes;	 Catch:{ RuntimeException -> 0x0048 }
        r56 = r4[r16];	 Catch:{ RuntimeException -> 0x0048 }
        r55 = new se.krka.kahlua.vm.LuaClosure;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r30;
        r4 = r0.env;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r55;
        r1 = r56;
        r0.<init>(r1, r4);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r55;
        r0.set(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r56;
        r0 = r0.numUpvalues;	 Catch:{ RuntimeException -> 0x0048 }
        r58 = r0;
        r43 = 0;
    L_0x0aff:
        r0 = r43;
        r1 = r58;
        if (r0 >= r1) goto L_0x0020;
    L_0x0b05:
        r0 = r26;
        r4 = r0.f79pc;	 Catch:{ RuntimeException -> 0x0048 }
        r5 = r4 + 1;
        r0 = r26;
        r0.f79pc = r5;	 Catch:{ RuntimeException -> 0x0048 }
        r62 = r64[r4];	 Catch:{ RuntimeException -> 0x0048 }
        r63 = r62 & 63;
        r16 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        switch(r63) {
            case 0: goto L_0x0b1d;
            case 4: goto L_0x0b2c;
            default: goto L_0x0b1a;
        };	 Catch:{ RuntimeException -> 0x0048 }
    L_0x0b1a:
        r43 = r43 + 1;
        goto L_0x0aff;
    L_0x0b1d:
        r0 = r55;
        r4 = r0.upvalues;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r26;
        r1 = r16;
        r5 = r0.findUpvalue(r1);	 Catch:{ RuntimeException -> 0x0048 }
        r4[r43] = r5;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0b1a;
    L_0x0b2c:
        r0 = r55;
        r4 = r0.upvalues;	 Catch:{ RuntimeException -> 0x0048 }
        r0 = r30;
        r5 = r0.upvalues;	 Catch:{ RuntimeException -> 0x0048 }
        r5 = r5[r16];	 Catch:{ RuntimeException -> 0x0048 }
        r4[r43] = r5;	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0b1a;
    L_0x0b39:
        r12 = p009se.krka.kahlua.p010vm.LuaState.getA8(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r4 = p009se.krka.kahlua.p010vm.LuaState.getB9(r62);	 Catch:{ RuntimeException -> 0x0048 }
        r16 = r4 + -1;
        r0 = r26;
        r1 = r16;
        r0.pushVarargs(r12, r1);	 Catch:{ RuntimeException -> 0x0048 }
        goto L_0x0020;
    L_0x0b4c:
        r0 = r81;
        r4 = r0.currentThread;
        r0 = r26;
        r4.addStackTrace(r0);
        r0 = r81;
        r4 = r0.currentThread;
        r4.popCallFrame();
        goto L_0x0049;
    L_0x0b5e:
        r0 = r81;
        r4 = r0.currentThread;
        r0 = r26;
        r4.addStackTrace(r0);
        r0 = r81;
        r4 = r0.currentThread;
        r4.popCallFrame();
        r0 = r26;
        r4 = r0.fromLua;
        if (r4 != 0) goto L_0x0059;
    L_0x0b74:
        goto L_0x00c5;
        */
        throw new UnsupportedOperationException("Method not decompiled: p009se.krka.kahlua.p010vm.LuaState.luaMainloop():void");
    }

    public Object getMetaOp(Object o, String meta_op) {
        LuaTable meta = (LuaTable) getmetatable(o, true);
        if (meta == null) {
            return null;
        }
        return meta.rawget(meta_op);
    }

    private final Object getCompMetaOp(Object a, Object b, String meta_op) {
        LuaTable meta1 = (LuaTable) getmetatable(a, true);
        if (meta1 != ((LuaTable) getmetatable(b, true)) || meta1 == null) {
            return null;
        }
        return meta1.rawget(meta_op);
    }

    private final Object getBinMetaOp(Object a, Object b, String meta_op) {
        Object op = getMetaOp(a, meta_op);
        return op != null ? op : getMetaOp(b, meta_op);
    }

    private void setUserdataMetatable(Object obj, LuaTable metatable) {
        this.userdataMetatables.rawset(obj, metatable);
    }

    private final Object getRegisterOrConstant(LuaCallFrame callFrame, int index, LuaPrototype prototype) {
        int cindex = index + InputDeviceCompat.SOURCE_ANY;
        if (cindex < 0) {
            return callFrame.get(index);
        }
        return prototype.constants[cindex];
    }

    private static final int getA8(int op) {
        return (op >>> 6) & 255;
    }

    private static final int getC9(int op) {
        return (op >>> 14) & 511;
    }

    private static final int getB9(int op) {
        return (op >>> 23) & 511;
    }

    private static final int getBx(int op) {
        return op >>> 14;
    }

    private static final int getSBx(int op) {
        return (op >>> 14) - 131071;
    }

    private Double primitiveMath(Double x, Double y, int opcode) {
        double v1 = LuaState.fromDouble(x);
        double v2 = LuaState.fromDouble(y);
        double res = 0.0d;
        switch (opcode) {
            case 12:
                res = v1 + v2;
                break;
            case 13:
                res = v1 - v2;
                break;
            case 14:
                res = v1 * v2;
                break;
            case 15:
                res = v1 / v2;
                break;
            case 16:
                if (v2 != 0.0d) {
                    res = v1 - (((double) ((int) (v1 / v2))) * v2);
                    break;
                }
                res = Double.NaN;
                break;
            case 17:
                res = MathLib.pow(v1, v2);
                break;
        }
        return LuaState.toDouble(res);
    }

    public Object call(Object fun, Object arg1, Object arg2, Object arg3) {
        int oldTop = this.currentThread.getTop();
        this.currentThread.setTop((oldTop + 1) + 3);
        this.currentThread.objectStack[oldTop] = fun;
        this.currentThread.objectStack[oldTop + 1] = arg1;
        this.currentThread.objectStack[oldTop + 2] = arg2;
        this.currentThread.objectStack[oldTop + 3] = arg3;
        Object ret = null;
        if (call(3) >= 1) {
            ret = this.currentThread.objectStack[oldTop];
        }
        this.currentThread.setTop(oldTop);
        return ret;
    }

    public Object call(Object fun, Object[] args) {
        int oldTop = this.currentThread.getTop();
        int argslen = args == null ? 0 : args.length;
        this.currentThread.setTop((oldTop + 1) + argslen);
        this.currentThread.objectStack[oldTop] = fun;
        for (int i = 1; i <= argslen; i++) {
            this.currentThread.objectStack[oldTop + i] = args[i - 1];
        }
        Object ret = null;
        if (call(argslen) >= 1) {
            ret = this.currentThread.objectStack[oldTop];
        }
        this.currentThread.setTop(oldTop);
        return ret;
    }

    public Object tableGet(Object table, Object key) {
        LuaTable curObj = table;
        int i = 100;
        while (i > 0) {
            boolean isTable = curObj instanceof LuaTable;
            if (isTable) {
                Object res = curObj.rawget(key);
                if (res != null) {
                    return res;
                }
            }
            LuaTable metaOp = getMetaOp(curObj, "__index");
            if (metaOp == null) {
                if (isTable) {
                    return null;
                }
                throw new RuntimeException("attempted index of non-table: " + curObj);
            } else if ((metaOp instanceof JavaFunction) || (metaOp instanceof LuaClosure)) {
                return call(metaOp, table, key, null);
            } else {
                curObj = metaOp;
                i--;
            }
        }
        throw new RuntimeException("loop in gettable");
    }

    public void tableSet(Object table, Object key, Object value) {
        LuaTable curObj = table;
        int i = 100;
        while (i > 0) {
            LuaTable metaOp;
            if (curObj instanceof LuaTable) {
                LuaTable t = curObj;
                if (t.rawget(key) != null) {
                    t.rawset(key, value);
                    return;
                }
                metaOp = getMetaOp(curObj, "__newindex");
                if (metaOp == null) {
                    t.rawset(key, value);
                    return;
                }
            }
            metaOp = getMetaOp(curObj, "__newindex");
            BaseLib.luaAssert(metaOp != null, "attempted index of non-table");
            if ((metaOp instanceof JavaFunction) || (metaOp instanceof LuaClosure)) {
                call(metaOp, table, key, value);
                return;
            } else {
                curObj = metaOp;
                i--;
            }
        }
        throw new RuntimeException("loop in settable");
    }

    public void setClassMetatable(Class clazz, LuaTable metatable) {
        this.classMetatables.rawset(clazz, metatable);
    }

    public void setmetatable(Object o, LuaTable metatable) {
        BaseLib.luaAssert(o != null, "Can't set metatable for nil");
        if (o instanceof LuaTable) {
            ((LuaTable) o).setMetatable(metatable);
        } else {
            this.userdataMetatables.rawset(o, metatable);
        }
    }

    public Object getmetatable(Object o, boolean raw) {
        if (o == null) {
            return null;
        }
        LuaTable metatable;
        if (o instanceof LuaTable) {
            metatable = ((LuaTable) o).getMetatable();
        } else {
            metatable = (LuaTable) this.userdataMetatables.rawget(o);
        }
        if (metatable == null) {
            metatable = (LuaTable) this.classMetatables.rawget(o.getClass());
        }
        if (!(raw || metatable == null)) {
            Object meta2 = metatable.rawget("__metatable");
            if (meta2 != null) {
                return meta2;
            }
        }
        return metatable;
    }

    public Object[] pcall(Object fun, Object[] args) {
        boolean z;
        int nArgs = args == null ? 0 : args.length;
        LuaThread thread = this.currentThread;
        int oldTop = thread.getTop();
        thread.setTop((oldTop + 1) + nArgs);
        thread.objectStack[oldTop] = fun;
        if (nArgs > 0) {
            System.arraycopy(args, 0, thread.objectStack, oldTop + 1, nArgs);
        }
        int nRet = pcall(nArgs);
        if (thread == this.currentThread) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Internal Kahlua error - thread changed in pcall");
        Object[] ret = new Object[nRet];
        System.arraycopy(thread.objectStack, oldTop, ret, 0, nRet);
        thread.setTop(oldTop);
        return ret;
    }

    public Object[] pcall(Object fun) {
        return pcall(fun, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0033  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0045  */
    public int pcall(int r12) {
        /*
        r11 = this;
        r9 = 0;
        r7 = r11.currentThread;
        r0 = r7.currentCallFrame();
        r8 = "";
        r7.stackTrace = r8;
        r8 = r7.getTop();
        r8 = r8 - r12;
        r6 = r8 + -1;
        r4 = r11.call(r12);	 Catch:{ LuaException -> 0x002b, Throwable -> 0x006c }
        r8 = r6 + r4;
        r5 = r8 + 1;
        r7.setTop(r5);	 Catch:{ LuaException -> 0x002b, Throwable -> 0x006c }
        r8 = r6 + 1;
        r7.stackCopy(r6, r8, r4);	 Catch:{ LuaException -> 0x002b, Throwable -> 0x006c }
        r8 = r7.objectStack;	 Catch:{ LuaException -> 0x002b, Throwable -> 0x006c }
        r10 = java.lang.Boolean.TRUE;	 Catch:{ LuaException -> 0x002b, Throwable -> 0x006c }
        r8[r6] = r10;	 Catch:{ LuaException -> 0x002b, Throwable -> 0x006c }
        r8 = r4 + 1;
    L_0x002a:
        return r8;
    L_0x002b:
        r1 = move-exception;
        r3 = r1;
        r2 = r1.errorMessage;
    L_0x002f:
        r8 = r11.currentThread;
        if (r7 != r8) goto L_0x0073;
    L_0x0033:
        r8 = 1;
    L_0x0034:
        r10 = "Internal Kahlua error - thread changed in pcall";
        p009se.krka.kahlua.stdlib.BaseLib.luaAssert(r8, r10);
        if (r0 == 0) goto L_0x003e;
    L_0x003b:
        r0.closeUpvalues(r9);
    L_0x003e:
        r7.cleanCallFrames(r0);
        r8 = r2 instanceof java.lang.String;
        if (r8 == 0) goto L_0x0047;
    L_0x0045:
        r2 = (java.lang.String) r2;
    L_0x0047:
        r8 = r6 + 4;
        r7.setTop(r8);
        r8 = r7.objectStack;
        r9 = java.lang.Boolean.FALSE;
        r8[r6] = r9;
        r8 = r7.objectStack;
        r9 = r6 + 1;
        r8[r9] = r2;
        r8 = r7.objectStack;
        r9 = r6 + 2;
        r10 = r7.stackTrace;
        r8[r9] = r10;
        r8 = r7.objectStack;
        r9 = r6 + 3;
        r8[r9] = r3;
        r8 = "";
        r7.stackTrace = r8;
        r8 = 4;
        goto L_0x002a;
    L_0x006c:
        r1 = move-exception;
        r3 = r1;
        r2 = r1.getMessage();
        goto L_0x002f;
    L_0x0073:
        r8 = r9;
        goto L_0x0034;
        */
        throw new UnsupportedOperationException("Method not decompiled: p009se.krka.kahlua.p010vm.LuaState.pcall(int):int");
    }

    public LuaTable getEnvironment() {
        return this.currentThread.environment;
    }

    public static boolean luaEquals(Object a, Object b) {
        if (a == null || b == null) {
            if (a == b) {
                return true;
            }
            return false;
        } else if ((a instanceof Double) && (b instanceof Double)) {
            if (((Double) a).doubleValue() != ((Double) b).doubleValue()) {
                return false;
            }
            return true;
        } else if (a != b) {
            return false;
        } else {
            return true;
        }
    }

    public static double fromDouble(Object o) {
        return ((Double) o).doubleValue();
    }

    public static Double toDouble(double d) {
        return new Double(d);
    }

    public static Double toDouble(long d) {
        return LuaState.toDouble((double) d);
    }

    public static boolean boolEval(Object o) {
        return (o == null || o == Boolean.FALSE) ? false : true;
    }

    public static Boolean toBoolean(boolean b) {
        return b ? Boolean.TRUE : Boolean.FALSE;
    }

    public LuaClosure loadByteCodeFromResource(String name, LuaTable environment) {
        InputStream stream = getClass().getResourceAsStream(name + ".lbc");
        if (stream == null) {
            return null;
        }
        try {
            return LuaPrototype.loadByteCode(stream, environment);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void lock() {
    }

    public void unlock() {
    }

    public PrintStream getOut() {
        return this.out;
    }

    public LuaTable getClassMetatable(Class clazz) {
        return (LuaTable) this.classMetatables.rawget(clazz);
    }
}
