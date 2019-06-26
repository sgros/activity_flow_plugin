package p009se.krka.kahlua.p010vm;

/* renamed from: se.krka.kahlua.vm.LuaTable */
public interface LuaTable {
    LuaTable getMetatable();

    int len();

    Object next(Object obj);

    Object rawget(Object obj);

    void rawset(Object obj, Object obj2);

    void setMetatable(LuaTable luaTable);
}
