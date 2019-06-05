// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.vm;

public interface LuaTable
{
    LuaTable getMetatable();
    
    int len();
    
    Object next(final Object p0);
    
    Object rawget(final Object p0);
    
    void rawset(final Object p0, final Object p1);
    
    void setMetatable(final LuaTable p0);
}
