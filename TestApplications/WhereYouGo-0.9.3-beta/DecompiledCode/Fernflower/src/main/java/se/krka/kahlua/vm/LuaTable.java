package se.krka.kahlua.vm;

public interface LuaTable {
   LuaTable getMetatable();

   int len();

   Object next(Object var1);

   Object rawget(Object var1);

   void rawset(Object var1, Object var2);

   void setMetatable(LuaTable var1);
}
