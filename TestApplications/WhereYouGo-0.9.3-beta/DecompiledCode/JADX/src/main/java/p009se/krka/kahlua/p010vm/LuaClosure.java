package p009se.krka.kahlua.p010vm;

/* renamed from: se.krka.kahlua.vm.LuaClosure */
public final class LuaClosure {
    public LuaTable env;
    public LuaPrototype prototype;
    public UpValue[] upvalues;

    public LuaClosure(LuaPrototype prototype, LuaTable env) {
        this.prototype = prototype;
        this.env = env;
        this.upvalues = new UpValue[prototype.numUpvalues];
    }

    public String toString() {
        if (this.prototype.lines.length > 0) {
            return "function " + this.prototype.toString() + ":" + this.prototype.lines[0];
        }
        return "function[" + Integer.toString(hashCode(), 36) + "]";
    }
}
