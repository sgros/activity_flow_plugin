// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.vm;

public final class UpValue
{
    public int index;
    public LuaThread thread;
    public Object value;
    
    public final Object getValue() {
        Object value;
        if (this.thread == null) {
            value = this.value;
        }
        else {
            value = this.thread.objectStack[this.index];
        }
        return value;
    }
    
    public final void setValue(final Object value) {
        if (this.thread == null) {
            this.value = value;
        }
        else {
            this.thread.objectStack[this.index] = value;
        }
    }
}
