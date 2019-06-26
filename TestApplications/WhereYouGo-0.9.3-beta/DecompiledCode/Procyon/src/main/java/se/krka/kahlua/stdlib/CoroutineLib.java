// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.stdlib;

import se.krka.kahlua.vm.LuaTableImpl;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaThread;
import se.krka.kahlua.vm.JavaFunction;

public class CoroutineLib implements JavaFunction
{
    private static final int CREATE = 0;
    private static final Class LUA_THREAD_CLASS;
    private static final int NUM_FUNCTIONS = 5;
    private static final int RESUME = 1;
    private static final int RUNNING = 4;
    private static final int STATUS = 3;
    private static final int YIELD = 2;
    private static CoroutineLib[] functions;
    private static final String[] names;
    private int index;
    
    static {
        LUA_THREAD_CLASS = new LuaThread(null, null).getClass();
        (names = new String[5])[0] = "create";
        CoroutineLib.names[1] = "resume";
        CoroutineLib.names[2] = "yield";
        CoroutineLib.names[3] = "status";
        CoroutineLib.names[4] = "running";
        CoroutineLib.functions = new CoroutineLib[5];
        for (int i = 0; i < 5; ++i) {
            CoroutineLib.functions[i] = new CoroutineLib(i);
        }
    }
    
    public CoroutineLib(final int index) {
        this.index = index;
    }
    
    private int create(final LuaCallFrame luaCallFrame, final int n) {
        final LuaClosure function = this.getFunction(luaCallFrame, n);
        final LuaThread luaThread = new LuaThread(luaCallFrame.thread.state, luaCallFrame.thread.environment);
        luaThread.pushNewCallFrame(function, null, 0, 0, -1, true, true);
        luaCallFrame.push(luaThread);
        return 1;
    }
    
    private LuaThread getCoroutine(final LuaCallFrame luaCallFrame, final int n) {
        boolean b = true;
        if (n < 1) {
            b = false;
        }
        BaseLib.luaAssert(b, "not enough arguments");
        final Object value = luaCallFrame.get(0);
        BaseLib.luaAssert(value instanceof LuaThread, "argument 1 must be a coroutine");
        return (LuaThread)value;
    }
    
    private LuaClosure getFunction(final LuaCallFrame luaCallFrame, final int n) {
        boolean b = true;
        if (n < 1) {
            b = false;
        }
        BaseLib.luaAssert(b, "not enough arguments");
        final Object value = luaCallFrame.get(0);
        BaseLib.luaAssert(value instanceof LuaClosure, "argument 1 must be a lua function");
        return (LuaClosure)value;
    }
    
    private String getStatus(final LuaThread luaThread, final LuaThread luaThread2) {
        String s;
        if (luaThread.parent == null) {
            if (luaThread.isDead()) {
                s = "dead";
            }
            else {
                s = "suspended";
            }
        }
        else if (luaThread2 == luaThread) {
            s = "running";
        }
        else {
            s = "normal";
        }
        return s;
    }
    
    public static void register(final LuaState luaState) {
        final LuaTableImpl luaTableImpl = new LuaTableImpl();
        luaState.getEnvironment().rawset("coroutine", luaTableImpl);
        for (int i = 0; i < 5; ++i) {
            luaTableImpl.rawset(CoroutineLib.names[i], CoroutineLib.functions[i]);
        }
        luaTableImpl.rawset("__index", luaTableImpl);
        luaState.setClassMetatable(CoroutineLib.LUA_THREAD_CLASS, luaTableImpl);
    }
    
    private int resume(final LuaCallFrame luaCallFrame, final int n) {
        final LuaThread coroutine = this.getCoroutine(luaCallFrame, n);
        final String status = this.getStatus(coroutine, luaCallFrame.thread);
        if (status != "suspended") {
            BaseLib.fail("Can not resume thread that is in status: " + status);
        }
        coroutine.parent = luaCallFrame.thread;
        final LuaCallFrame currentCallFrame = coroutine.currentCallFrame();
        if (currentCallFrame.nArguments == -1) {
            currentCallFrame.setTop(0);
        }
        for (int i = 1; i < n; ++i) {
            currentCallFrame.push(luaCallFrame.get(i));
        }
        if (currentCallFrame.nArguments == -1) {
            currentCallFrame.nArguments = n - 1;
            currentCallFrame.init();
        }
        luaCallFrame.thread.state.currentThread = coroutine;
        return 0;
    }
    
    private int running(final LuaCallFrame luaCallFrame, final int n) {
        LuaThread thread;
        if ((thread = luaCallFrame.thread).parent == null) {
            thread = null;
        }
        luaCallFrame.push(thread);
        return 1;
    }
    
    private int status(final LuaCallFrame luaCallFrame, final int n) {
        luaCallFrame.push(this.getStatus(this.getCoroutine(luaCallFrame, n), luaCallFrame.thread));
        return 1;
    }
    
    public static int yield(final LuaCallFrame luaCallFrame, final int n) {
        final LuaThread thread = luaCallFrame.thread;
        BaseLib.luaAssert(thread.parent != null, "Can not yield outside of a coroutine");
        yieldHelper(thread.callFrameStack[thread.callFrameTop - 2], luaCallFrame, n);
        return 0;
    }
    
    public static void yieldHelper(final LuaCallFrame luaCallFrame, final LuaCallFrame luaCallFrame2, final int n) {
        BaseLib.luaAssert(luaCallFrame.insideCoroutine, "Can not yield outside of a coroutine");
        final LuaThread thread = luaCallFrame.thread;
        final LuaThread parent = thread.parent;
        thread.parent = null;
        final LuaCallFrame currentCallFrame = parent.currentCallFrame();
        currentCallFrame.push(Boolean.TRUE);
        for (int i = 0; i < n; ++i) {
            currentCallFrame.push(luaCallFrame2.get(i));
        }
        thread.state.currentThread = parent;
    }
    
    @Override
    public int call(final LuaCallFrame luaCallFrame, int n) {
        switch (this.index) {
            default: {
                n = 0;
                break;
            }
            case 0: {
                n = this.create(luaCallFrame, n);
                break;
            }
            case 2: {
                n = yield(luaCallFrame, n);
                break;
            }
            case 1: {
                n = this.resume(luaCallFrame, n);
                break;
            }
            case 3: {
                n = this.status(luaCallFrame, n);
                break;
            }
            case 4: {
                n = this.running(luaCallFrame, n);
                break;
            }
        }
        return n;
    }
    
    @Override
    public String toString() {
        return "coroutine." + CoroutineLib.names[this.index];
    }
}
