// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.vm;

public final class LuaClosure
{
    public LuaTable env;
    public LuaPrototype prototype;
    public UpValue[] upvalues;
    
    public LuaClosure(final LuaPrototype prototype, final LuaTable env) {
        this.prototype = prototype;
        this.env = env;
        this.upvalues = new UpValue[prototype.numUpvalues];
    }
    
    @Override
    public String toString() {
        String s;
        if (this.prototype.lines.length > 0) {
            s = "function " + this.prototype.toString() + ":" + this.prototype.lines[0];
        }
        else {
            s = "function[" + Integer.toString(this.hashCode(), 36) + "]";
        }
        return s;
    }
}
