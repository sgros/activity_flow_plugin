// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.vm;

public class LuaCallFrame
{
    public LuaClosure closure;
    boolean fromLua;
    public boolean insideCoroutine;
    public JavaFunction javaFunction;
    public int localBase;
    public int nArguments;
    public int pc;
    boolean restoreTop;
    int returnBase;
    public LuaThread thread;
    
    public LuaCallFrame(final LuaThread thread) {
        this.thread = thread;
    }
    
    public void clearFromIndex(final int top) {
        if (this.getTop() < top) {
            this.setTop(top);
        }
        this.stackClear(top, this.getTop() - 1);
    }
    
    public void closeUpvalues(final int n) {
        this.thread.closeUpvalues(this.localBase + n);
    }
    
    public UpValue findUpvalue(final int n) {
        return this.thread.findUpvalue(this.localBase + n);
    }
    
    public final Object get(final int n) {
        return this.thread.objectStack[this.localBase + n];
    }
    
    public LuaTable getEnvironment() {
        LuaTable luaTable;
        if (this.isLua()) {
            luaTable = this.closure.env;
        }
        else {
            luaTable = this.thread.environment;
        }
        return luaTable;
    }
    
    public int getTop() {
        return this.thread.getTop() - this.localBase;
    }
    
    public void init() {
        if (this.isLua()) {
            this.pc = 0;
            if (this.closure.prototype.isVararg) {
                this.localBase += this.nArguments;
                this.setTop(this.closure.prototype.maxStacksize);
                this.stackCopy(-this.nArguments, 0, Math.min(this.nArguments, this.closure.prototype.numParams));
            }
            else {
                this.setTop(this.closure.prototype.maxStacksize);
                this.stackClear(this.closure.prototype.numParams, this.nArguments);
            }
        }
    }
    
    public boolean isJava() {
        return !this.isLua();
    }
    
    public boolean isLua() {
        return this.closure != null;
    }
    
    public int push(final Object o) {
        final int top = this.getTop();
        this.setTop(top + 1);
        this.set(top, o);
        return 1;
    }
    
    public int push(final Object o, final Object o2) {
        final int top = this.getTop();
        this.setTop(top + 2);
        this.set(top, o);
        this.set(top + 1, o2);
        return 2;
    }
    
    public int pushNil() {
        return this.push(null);
    }
    
    public void pushVarargs(final int n, int n2) {
        final int numParams = this.closure.prototype.numParams;
        int n3;
        if ((n3 = this.nArguments - numParams) < 0) {
            n3 = 0;
        }
        int n4;
        if ((n4 = n2) == -1) {
            n4 = n3;
            this.setTop(n + n4);
        }
        if ((n2 = n3) > n4) {
            n2 = n4;
        }
        this.stackCopy(-this.nArguments + numParams, n, n2);
        if (n4 - n2 > 0) {
            this.stackClear(n + n2, n + n4 - 1);
        }
    }
    
    public final void set(final int n, final Object o) {
        this.thread.objectStack[this.localBase + n] = o;
    }
    
    public void setPrototypeStacksize() {
        if (this.isLua()) {
            this.setTop(this.closure.prototype.maxStacksize);
        }
    }
    
    public final void setTop(final int n) {
        this.thread.setTop(this.localBase + n);
    }
    
    public void stackClear(int i, final int n) {
        while (i <= n) {
            this.thread.objectStack[this.localBase + i] = null;
            ++i;
        }
    }
    
    public final void stackCopy(final int n, final int n2, final int n3) {
        this.thread.stackCopy(this.localBase + n, this.localBase + n2, n3);
    }
    
    @Override
    public String toString() {
        String s;
        if (this.closure != null) {
            s = "Callframe at: " + this.closure.toString();
        }
        else if (this.javaFunction != null) {
            s = "Callframe at: " + this.javaFunction.toString();
        }
        else {
            s = super.toString();
        }
        return s;
    }
}
