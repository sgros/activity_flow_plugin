package p009se.krka.kahlua.p010vm;

import java.util.Vector;
import p009se.krka.kahlua.stdlib.BaseLib;

/* renamed from: se.krka.kahlua.vm.LuaThread */
public class LuaThread {
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
    public String stackTrace = "";
    public LuaState state;
    public int top;

    public LuaThread(LuaState state, LuaTable environment) {
        this.state = state;
        this.environment = environment;
        this.objectStack = new Object[10];
        this.callFrameStack = new LuaCallFrame[10];
        this.liveUpvalues = new Vector();
    }

    public final LuaCallFrame pushNewCallFrame(LuaClosure closure, JavaFunction javaFunction, int localBase, int returnBase, int nArguments, boolean fromLua, boolean insideCoroutine) {
        setCallFrameStackTop(this.callFrameTop + 1);
        LuaCallFrame callFrame = currentCallFrame();
        callFrame.localBase = localBase;
        callFrame.returnBase = returnBase;
        callFrame.nArguments = nArguments;
        callFrame.fromLua = fromLua;
        callFrame.insideCoroutine = insideCoroutine;
        callFrame.closure = closure;
        callFrame.javaFunction = javaFunction;
        return callFrame;
    }

    public void popCallFrame() {
        if (isDead()) {
            throw new RuntimeException("Stack underflow");
        }
        setCallFrameStackTop(this.callFrameTop - 1);
    }

    private final void ensureCallFrameStackSize(int index) {
        if (index > 100) {
            throw new RuntimeException("Stack overflow");
        }
        int oldSize = this.callFrameStack.length;
        int newSize = oldSize;
        while (newSize <= index) {
            newSize *= 2;
        }
        if (newSize > oldSize) {
            LuaCallFrame[] newStack = new LuaCallFrame[newSize];
            System.arraycopy(this.callFrameStack, 0, newStack, 0, oldSize);
            this.callFrameStack = newStack;
        }
    }

    public final void setCallFrameStackTop(int newTop) {
        if (newTop > this.callFrameTop) {
            ensureCallFrameStackSize(newTop);
        } else {
            callFrameStackClear(newTop, this.callFrameTop - 1);
        }
        this.callFrameTop = newTop;
    }

    private void callFrameStackClear(int startIndex, int endIndex) {
        while (startIndex <= endIndex) {
            if (this.callFrameStack[startIndex] != null) {
                this.callFrameStack[startIndex].closure = null;
                this.callFrameStack[startIndex].javaFunction = null;
            }
            startIndex++;
        }
    }

    private final void ensureStacksize(int index) {
        if (index > 1000) {
            throw new RuntimeException("Stack overflow");
        }
        int oldSize = this.objectStack.length;
        int newSize = oldSize;
        while (newSize <= index) {
            newSize *= 2;
        }
        if (newSize > oldSize) {
            Object[] newStack = new Object[newSize];
            System.arraycopy(this.objectStack, 0, newStack, 0, oldSize);
            this.objectStack = newStack;
        }
    }

    public final void setTop(int newTop) {
        if (this.top < newTop) {
            ensureStacksize(newTop);
        } else {
            stackClear(newTop, this.top - 1);
        }
        this.top = newTop;
    }

    public final void stackCopy(int startIndex, int destIndex, int len) {
        if (len > 0 && startIndex != destIndex) {
            System.arraycopy(this.objectStack, startIndex, this.objectStack, destIndex, len);
        }
    }

    public final void stackClear(int startIndex, int endIndex) {
        while (startIndex <= endIndex) {
            this.objectStack[startIndex] = null;
            startIndex++;
        }
    }

    public final void closeUpvalues(int closeIndex) {
        int loopIndex = this.liveUpvalues.size();
        while (true) {
            loopIndex--;
            if (loopIndex >= 0) {
                UpValue uv = (UpValue) this.liveUpvalues.elementAt(loopIndex);
                if (uv.index >= closeIndex) {
                    uv.value = this.objectStack[uv.index];
                    uv.thread = null;
                    this.liveUpvalues.removeElementAt(loopIndex);
                } else {
                    return;
                }
            }
            return;
        }
    }

    public final UpValue findUpvalue(int scanIndex) {
        UpValue uv;
        int loopIndex = this.liveUpvalues.size();
        do {
            loopIndex--;
            if (loopIndex < 0) {
                break;
            }
            uv = (UpValue) this.liveUpvalues.elementAt(loopIndex);
            if (uv.index == scanIndex) {
                return uv;
            }
        } while (uv.index >= scanIndex);
        uv = new UpValue();
        uv.thread = this;
        uv.index = scanIndex;
        this.liveUpvalues.insertElementAt(uv, loopIndex + 1);
        return uv;
    }

    public final LuaCallFrame currentCallFrame() {
        if (isDead()) {
            return null;
        }
        LuaCallFrame callFrame = this.callFrameStack[this.callFrameTop - 1];
        if (callFrame != null) {
            return callFrame;
        }
        callFrame = new LuaCallFrame(this);
        this.callFrameStack[this.callFrameTop - 1] = callFrame;
        return callFrame;
    }

    public int getTop() {
        return this.top;
    }

    public LuaCallFrame getParent(int level) {
        boolean z;
        boolean z2 = true;
        if (level >= 0) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Level must be non-negative");
        int index = (this.callFrameTop - level) - 1;
        if (index < 0) {
            z2 = false;
        }
        BaseLib.luaAssert(z2, "Level too high");
        return this.callFrameStack[index];
    }

    public String getCurrentStackTrace(int level, int count, int haltAt) {
        if (level < 0) {
            level = 0;
        }
        if (count < 0) {
            count = 0;
        }
        StringBuffer buffer = new StringBuffer();
        int i = (this.callFrameTop - 1) - level;
        int count2 = count;
        while (i >= haltAt) {
            count = count2 - 1;
            if (count2 <= 0) {
                break;
            }
            buffer.append(getStackTrace(this.callFrameStack[i]));
            i--;
            count2 = count;
        }
        count = count2;
        return buffer.toString();
    }

    public void cleanCallFrames(LuaCallFrame callerFrame) {
        while (true) {
            LuaCallFrame frame = currentCallFrame();
            if (frame != null && frame != callerFrame) {
                addStackTrace(frame);
                popCallFrame();
            } else {
                return;
            }
        }
    }

    public void addStackTrace(LuaCallFrame frame) {
        this.stackTrace += getStackTrace(frame);
    }

    private String getStackTrace(LuaCallFrame frame) {
        if (!frame.isLua()) {
            return "at " + frame.javaFunction;
        }
        int[] lines = frame.closure.prototype.lines;
        if (lines != null) {
            int pc = frame.f79pc - 1;
            if (pc >= 0 && pc < lines.length) {
                return "at " + frame.closure.prototype + ":" + lines[pc] + "\n";
            }
        }
        return "";
    }

    public boolean isDead() {
        return this.callFrameTop == 0;
    }
}
