package p009se.krka.kahlua.stdlib;

import p009se.krka.kahlua.p010vm.JavaFunction;
import p009se.krka.kahlua.p010vm.LuaCallFrame;
import p009se.krka.kahlua.p010vm.LuaClosure;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.p010vm.LuaTableImpl;
import p009se.krka.kahlua.p010vm.LuaThread;

/* renamed from: se.krka.kahlua.stdlib.CoroutineLib */
public class CoroutineLib implements JavaFunction {
    private static final int CREATE = 0;
    private static final Class LUA_THREAD_CLASS = new LuaThread(null, null).getClass();
    private static final int NUM_FUNCTIONS = 5;
    private static final int RESUME = 1;
    private static final int RUNNING = 4;
    private static final int STATUS = 3;
    private static final int YIELD = 2;
    private static CoroutineLib[] functions = new CoroutineLib[5];
    private static final String[] names = new String[5];
    private int index;

    static {
        names[0] = "create";
        names[1] = "resume";
        names[2] = "yield";
        names[3] = "status";
        names[4] = "running";
        for (int i = 0; i < 5; i++) {
            functions[i] = new CoroutineLib(i);
        }
    }

    public String toString() {
        return "coroutine." + names[this.index];
    }

    public CoroutineLib(int index) {
        this.index = index;
    }

    public static void register(LuaState state) {
        LuaTable coroutine = new LuaTableImpl();
        state.getEnvironment().rawset("coroutine", coroutine);
        for (int i = 0; i < 5; i++) {
            coroutine.rawset(names[i], functions[i]);
        }
        coroutine.rawset("__index", coroutine);
        state.setClassMetatable(LUA_THREAD_CLASS, coroutine);
    }

    public int call(LuaCallFrame callFrame, int nArguments) {
        switch (this.index) {
            case 0:
                return create(callFrame, nArguments);
            case 1:
                return resume(callFrame, nArguments);
            case 2:
                return CoroutineLib.yield(callFrame, nArguments);
            case 3:
                return status(callFrame, nArguments);
            case 4:
                return running(callFrame, nArguments);
            default:
                return 0;
        }
    }

    private int running(LuaCallFrame callFrame, int nArguments) {
        LuaThread t = callFrame.thread;
        if (t.parent == null) {
            t = null;
        }
        callFrame.push(t);
        return 1;
    }

    private int status(LuaCallFrame callFrame, int nArguments) {
        callFrame.push(getStatus(getCoroutine(callFrame, nArguments), callFrame.thread));
        return 1;
    }

    private String getStatus(LuaThread t, LuaThread caller) {
        if (t.parent == null) {
            if (t.isDead()) {
                return "dead";
            }
            return "suspended";
        } else if (caller == t) {
            return "running";
        } else {
            return "normal";
        }
    }

    private int resume(LuaCallFrame callFrame, int nArguments) {
        LuaThread t = getCoroutine(callFrame, nArguments);
        String status = getStatus(t, callFrame.thread);
        if (status != "suspended") {
            BaseLib.fail("Can not resume thread that is in status: " + status);
        }
        t.parent = callFrame.thread;
        LuaCallFrame nextCallFrame = t.currentCallFrame();
        if (nextCallFrame.nArguments == -1) {
            nextCallFrame.setTop(0);
        }
        for (int i = 1; i < nArguments; i++) {
            nextCallFrame.push(callFrame.get(i));
        }
        if (nextCallFrame.nArguments == -1) {
            nextCallFrame.nArguments = nArguments - 1;
            nextCallFrame.init();
        }
        callFrame.thread.state.currentThread = t;
        return 0;
    }

    public static int yield(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        LuaThread t = callFrame.thread;
        if (t.parent != null) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Can not yield outside of a coroutine");
        CoroutineLib.yieldHelper(t.callFrameStack[t.callFrameTop - 2], callFrame, nArguments);
        return 0;
    }

    public static void yieldHelper(LuaCallFrame callFrame, LuaCallFrame argsCallFrame, int nArguments) {
        BaseLib.luaAssert(callFrame.insideCoroutine, "Can not yield outside of a coroutine");
        LuaThread t = callFrame.thread;
        LuaThread parent = t.parent;
        t.parent = null;
        LuaCallFrame nextCallFrame = parent.currentCallFrame();
        nextCallFrame.push(Boolean.TRUE);
        for (int i = 0; i < nArguments; i++) {
            nextCallFrame.push(argsCallFrame.get(i));
        }
        t.state.currentThread = parent;
    }

    private int create(LuaCallFrame callFrame, int nArguments) {
        LuaClosure c = getFunction(callFrame, nArguments);
        LuaThread newThread = new LuaThread(callFrame.thread.state, callFrame.thread.environment);
        newThread.pushNewCallFrame(c, null, 0, 0, -1, true, true);
        callFrame.push(newThread);
        return 1;
    }

    private LuaClosure getFunction(LuaCallFrame callFrame, int nArguments) {
        boolean z = true;
        if (nArguments < 1) {
            z = false;
        }
        BaseLib.luaAssert(z, "not enough arguments");
        LuaClosure o = callFrame.get(0);
        BaseLib.luaAssert(o instanceof LuaClosure, "argument 1 must be a lua function");
        return o;
    }

    private LuaThread getCoroutine(LuaCallFrame callFrame, int nArguments) {
        boolean z = true;
        if (nArguments < 1) {
            z = false;
        }
        BaseLib.luaAssert(z, "not enough arguments");
        LuaThread o = callFrame.get(0);
        BaseLib.luaAssert(o instanceof LuaThread, "argument 1 must be a coroutine");
        return o;
    }
}
