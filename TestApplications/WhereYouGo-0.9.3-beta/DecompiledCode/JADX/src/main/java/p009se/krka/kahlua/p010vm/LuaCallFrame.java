package p009se.krka.kahlua.p010vm;

/* renamed from: se.krka.kahlua.vm.LuaCallFrame */
public class LuaCallFrame {
    public LuaClosure closure;
    boolean fromLua;
    public boolean insideCoroutine;
    public JavaFunction javaFunction;
    public int localBase;
    public int nArguments;
    /* renamed from: pc */
    public int f79pc;
    boolean restoreTop;
    int returnBase;
    public LuaThread thread;

    public LuaCallFrame(LuaThread thread) {
        this.thread = thread;
    }

    public final void set(int index, Object o) {
        this.thread.objectStack[this.localBase + index] = o;
    }

    public final Object get(int index) {
        return this.thread.objectStack[this.localBase + index];
    }

    public int push(Object x) {
        int top = getTop();
        setTop(top + 1);
        set(top, x);
        return 1;
    }

    public int push(Object x, Object y) {
        int top = getTop();
        setTop(top + 2);
        set(top, x);
        set(top + 1, y);
        return 2;
    }

    public int pushNil() {
        return push(null);
    }

    public final void stackCopy(int startIndex, int destIndex, int len) {
        this.thread.stackCopy(this.localBase + startIndex, this.localBase + destIndex, len);
    }

    public void stackClear(int startIndex, int endIndex) {
        while (startIndex <= endIndex) {
            this.thread.objectStack[this.localBase + startIndex] = null;
            startIndex++;
        }
    }

    public void clearFromIndex(int index) {
        if (getTop() < index) {
            setTop(index);
        }
        stackClear(index, getTop() - 1);
    }

    public final void setTop(int index) {
        this.thread.setTop(this.localBase + index);
    }

    public void closeUpvalues(int a) {
        this.thread.closeUpvalues(this.localBase + a);
    }

    public UpValue findUpvalue(int b) {
        return this.thread.findUpvalue(this.localBase + b);
    }

    public int getTop() {
        return this.thread.getTop() - this.localBase;
    }

    public void init() {
        if (isLua()) {
            this.f79pc = 0;
            if (this.closure.prototype.isVararg) {
                this.localBase += this.nArguments;
                setTop(this.closure.prototype.maxStacksize);
                stackCopy(-this.nArguments, 0, Math.min(this.nArguments, this.closure.prototype.numParams));
                return;
            }
            setTop(this.closure.prototype.maxStacksize);
            stackClear(this.closure.prototype.numParams, this.nArguments);
        }
    }

    public void setPrototypeStacksize() {
        if (isLua()) {
            setTop(this.closure.prototype.maxStacksize);
        }
    }

    public void pushVarargs(int index, int n) {
        int nParams = this.closure.prototype.numParams;
        int nVarargs = this.nArguments - nParams;
        if (nVarargs < 0) {
            nVarargs = 0;
        }
        if (n == -1) {
            n = nVarargs;
            setTop(index + n);
        }
        if (nVarargs > n) {
            nVarargs = n;
        }
        stackCopy((-this.nArguments) + nParams, index, nVarargs);
        if (n - nVarargs > 0) {
            stackClear(index + nVarargs, (index + n) - 1);
        }
    }

    public LuaTable getEnvironment() {
        if (isLua()) {
            return this.closure.env;
        }
        return this.thread.environment;
    }

    public boolean isJava() {
        return !isLua();
    }

    public boolean isLua() {
        return this.closure != null;
    }

    public String toString() {
        if (this.closure != null) {
            return "Callframe at: " + this.closure.toString();
        }
        if (this.javaFunction != null) {
            return "Callframe at: " + this.javaFunction.toString();
        }
        return super.toString();
    }
}
