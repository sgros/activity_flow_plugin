// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.vm;

import se.krka.kahlua.stdlib.BaseLib;
import java.util.Vector;

public class LuaThread
{
    private static final int INITIAL_CALL_FRAME_STACK_SIZE = 10;
    public static final int INITIAL_STACK_SIZE = 10;
    private static final int MAX_CALL_FRAME_STACK_SIZE = 100;
    public static final int MAX_STACK_SIZE = 1000;
    public LuaCallFrame[] callFrameStack;
    public int callFrameTop;
    public LuaTable environment;
    public int expectedResults;
    public Vector liveUpvalues;
    public Object[] objectStack;
    public LuaThread parent;
    public String stackTrace;
    public LuaState state;
    public int top;
    
    public LuaThread(final LuaState state, final LuaTable environment) {
        this.stackTrace = "";
        this.state = state;
        this.environment = environment;
        this.objectStack = new Object[10];
        this.callFrameStack = new LuaCallFrame[10];
        this.liveUpvalues = new Vector();
    }
    
    private void callFrameStackClear(int i, final int n) {
        while (i <= n) {
            if (this.callFrameStack[i] != null) {
                this.callFrameStack[i].closure = null;
                this.callFrameStack[i].javaFunction = null;
            }
            ++i;
        }
    }
    
    private final void ensureCallFrameStackSize(final int n) {
        if (n > 100) {
            throw new RuntimeException("Stack overflow");
        }
        int i;
        int n2;
        for (n2 = (i = this.callFrameStack.length); i <= n; i *= 2) {}
        if (i > n2) {
            final LuaCallFrame[] callFrameStack = new LuaCallFrame[i];
            System.arraycopy(this.callFrameStack, 0, callFrameStack, 0, n2);
            this.callFrameStack = callFrameStack;
        }
    }
    
    private final void ensureStacksize(final int n) {
        if (n > 1000) {
            throw new RuntimeException("Stack overflow");
        }
        int i;
        int n2;
        for (n2 = (i = this.objectStack.length); i <= n; i *= 2) {}
        if (i > n2) {
            final Object[] objectStack = new Object[i];
            System.arraycopy(this.objectStack, 0, objectStack, 0, n2);
            this.objectStack = objectStack;
        }
    }
    
    private String getStackTrace(final LuaCallFrame luaCallFrame) {
        String s;
        if (luaCallFrame.isLua()) {
            final int[] lines = luaCallFrame.closure.prototype.lines;
            if (lines != null) {
                final int n = luaCallFrame.pc - 1;
                if (n >= 0 && n < lines.length) {
                    s = "at " + luaCallFrame.closure.prototype + ":" + lines[n] + "\n";
                    return s;
                }
            }
            s = "";
        }
        else {
            s = "at " + luaCallFrame.javaFunction;
        }
        return s;
    }
    
    public void addStackTrace(final LuaCallFrame luaCallFrame) {
        this.stackTrace += this.getStackTrace(luaCallFrame);
    }
    
    public void cleanCallFrames(final LuaCallFrame luaCallFrame) {
        while (true) {
            final LuaCallFrame currentCallFrame = this.currentCallFrame();
            if (currentCallFrame == null || currentCallFrame == luaCallFrame) {
                break;
            }
            this.addStackTrace(currentCallFrame);
            this.popCallFrame();
        }
    }
    
    public final void closeUpvalues(final int n) {
        int size = this.liveUpvalues.size();
        while (--size >= 0) {
            final UpValue upValue = this.liveUpvalues.elementAt(size);
            if (upValue.index < n) {
                break;
            }
            upValue.value = this.objectStack[upValue.index];
            upValue.thread = null;
            this.liveUpvalues.removeElementAt(size);
        }
    }
    
    public final LuaCallFrame currentCallFrame() {
        LuaCallFrame luaCallFrame;
        if (this.isDead()) {
            luaCallFrame = null;
        }
        else if ((luaCallFrame = this.callFrameStack[this.callFrameTop - 1]) == null) {
            luaCallFrame = new LuaCallFrame(this);
            this.callFrameStack[this.callFrameTop - 1] = luaCallFrame;
        }
        return luaCallFrame;
    }
    
    public final UpValue findUpvalue(final int index) {
        int size = this.liveUpvalues.size();
        UpValue obj;
        int index2;
        do {
            index2 = size - 1;
            if (index2 < 0) {
                break;
            }
            obj = (UpValue)this.liveUpvalues.elementAt(index2);
            if (obj.index == index) {
                return obj;
            }
            size = index2;
        } while (obj.index >= index);
        obj = new UpValue();
        obj.thread = this;
        obj.index = index;
        this.liveUpvalues.insertElementAt(obj, index2 + 1);
        return obj;
    }
    
    public String getCurrentStackTrace(int n, int i, final int n2) {
        int n3 = n;
        if (n < 0) {
            n3 = 0;
        }
        if ((n = i) < 0) {
            n = 0;
        }
        final StringBuffer sb = new StringBuffer();
        for (i = this.callFrameTop - 1 - n3; i >= n2; --i, --n) {
            if (n <= 0) {
                return sb.toString();
            }
            sb.append(this.getStackTrace(this.callFrameStack[i]));
        }
        return sb.toString();
    }
    
    public LuaCallFrame getParent(int n) {
        final boolean b = true;
        BaseLib.luaAssert(n >= 0, "Level must be non-negative");
        n = this.callFrameTop - n - 1;
        BaseLib.luaAssert(n >= 0 && b, "Level too high");
        return this.callFrameStack[n];
    }
    
    public int getTop() {
        return this.top;
    }
    
    public boolean isDead() {
        return this.callFrameTop == 0;
    }
    
    public void popCallFrame() {
        if (this.isDead()) {
            throw new RuntimeException("Stack underflow");
        }
        this.setCallFrameStackTop(this.callFrameTop - 1);
    }
    
    public final LuaCallFrame pushNewCallFrame(final LuaClosure closure, final JavaFunction javaFunction, final int localBase, final int returnBase, final int nArguments, final boolean fromLua, final boolean insideCoroutine) {
        this.setCallFrameStackTop(this.callFrameTop + 1);
        final LuaCallFrame currentCallFrame = this.currentCallFrame();
        currentCallFrame.localBase = localBase;
        currentCallFrame.returnBase = returnBase;
        currentCallFrame.nArguments = nArguments;
        currentCallFrame.fromLua = fromLua;
        currentCallFrame.insideCoroutine = insideCoroutine;
        currentCallFrame.closure = closure;
        currentCallFrame.javaFunction = javaFunction;
        return currentCallFrame;
    }
    
    public final void setCallFrameStackTop(final int callFrameTop) {
        if (callFrameTop > this.callFrameTop) {
            this.ensureCallFrameStackSize(callFrameTop);
        }
        else {
            this.callFrameStackClear(callFrameTop, this.callFrameTop - 1);
        }
        this.callFrameTop = callFrameTop;
    }
    
    public final void setTop(final int top) {
        if (this.top < top) {
            this.ensureStacksize(top);
        }
        else {
            this.stackClear(top, this.top - 1);
        }
        this.top = top;
    }
    
    public final void stackClear(int i, final int n) {
        while (i <= n) {
            this.objectStack[i] = null;
            ++i;
        }
    }
    
    public final void stackCopy(final int n, final int n2, final int n3) {
        if (n3 > 0 && n != n2) {
            System.arraycopy(this.objectStack, n, this.objectStack, n2, n3);
        }
    }
}
