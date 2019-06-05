// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import se.krka.kahlua.vm.LuaState;

public class Task extends EventTable
{
    public static final int DONE = 1;
    public static final int FAILED = 2;
    public static final int PENDING = 0;
    private boolean active;
    private boolean complete;
    private int state;
    
    public Task() {
        this.state = 1;
    }
    
    public boolean isComplete() {
        return this.complete;
    }
    
    @Override
    public boolean isVisible() {
        return this.visible && this.active;
    }
    
    @Override
    protected String luaTostring() {
        return "a ZTask instance";
    }
    
    @Override
    protected void setItem(String anotherString, final Object o) {
        if ("Active".equals(anotherString)) {
            final boolean boolEval = LuaState.boolEval(o);
            if (boolEval != this.active) {
                this.active = boolEval;
                this.callEvent("OnSetActive", null);
            }
        }
        else if ("Complete".equals(anotherString)) {
            final boolean boolEval2 = LuaState.boolEval(o);
            if (boolEval2 != this.complete) {
                this.complete = boolEval2;
                this.callEvent("OnSetComplete", null);
            }
        }
        else if ("CorrectState".equals(anotherString) && o instanceof String) {
            anotherString = (String)o;
            int state = 1;
            if ("Incorrect".equalsIgnoreCase(anotherString) || "NotCorrect".equalsIgnoreCase(anotherString)) {
                state = 2;
            }
            if (state != this.state) {
                this.state = state;
                this.callEvent("OnSetCorrectState", null);
            }
        }
        else {
            super.setItem(anotherString, o);
        }
    }
    
    public int state() {
        int state;
        if (!this.complete) {
            state = 0;
        }
        else {
            state = this.state;
        }
        return state;
    }
}
