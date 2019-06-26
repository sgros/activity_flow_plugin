// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.stdlib.BaseLib;
import java.io.DataOutputStream;
import se.krka.kahlua.vm.LuaState;
import java.io.IOException;
import java.io.DataInputStream;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.LuaTableImpl;
import se.krka.kahlua.vm.LuaTable;

public class EventTable implements LuaTable, Serializable
{
    public String description;
    public Media icon;
    public Media media;
    private LuaTable metatable;
    public String name;
    public ZonePoint position;
    public LuaTable table;
    protected boolean visible;
    
    public EventTable() {
        this.table = new LuaTableImpl();
        this.metatable = new LuaTableImpl();
        this.position = null;
        this.visible = false;
        this.metatable.rawset("__tostring", new TostringJavaFunc(this));
    }
    
    public void callEvent(final String s, Object o) {
        try {
            final Object rawget = this.table.rawget(s);
            if (rawget instanceof LuaClosure) {
                final StringBuilder append = new StringBuilder().append("EVNT: ").append(this.toString()).append(".").append(s);
                String string;
                if (o != null) {
                    string = " (" + o.toString() + ")";
                }
                else {
                    string = "";
                }
                Engine.log(append.append(string).toString(), 1);
                Engine.state.call(rawget, this, o, null);
                o = new StringBuilder();
                Engine.log(((StringBuilder)o).append("EEND: ").append(this.toString()).append(".").append(s).toString(), 1);
            }
        }
        catch (Throwable t) {
            Engine.stacktrace(t);
        }
    }
    
    @Override
    public void deserialize(final DataInputStream dataInputStream) throws IOException {
        Engine.instance.savegame.restoreValue(dataInputStream, this);
    }
    
    public byte[] getIcon() throws IOException {
        return Engine.mediaFile(this.icon);
    }
    
    protected Object getItem(final String s) {
        Object o;
        if ("CurrentDistance".equals(s)) {
            if (this.isLocated()) {
                o = LuaState.toDouble(this.position.distance(Engine.instance.player.position));
            }
            else {
                o = LuaState.toDouble(-1L);
            }
        }
        else if ("CurrentBearing".equals(s)) {
            if (this.isLocated()) {
                o = LuaState.toDouble(ZonePoint.angle2azimuth(this.position.bearing(Engine.instance.player.position)));
            }
            else {
                o = LuaState.toDouble(0L);
            }
        }
        else {
            o = this.table.rawget(s);
        }
        return o;
    }
    
    public byte[] getMedia() throws IOException {
        return Engine.mediaFile(this.media);
    }
    
    @Override
    public LuaTable getMetatable() {
        return this.metatable;
    }
    
    public boolean hasEvent(final String s) {
        return this.table.rawget(s) instanceof LuaClosure;
    }
    
    public boolean isLocated() {
        return this.position != null;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    @Override
    public int len() {
        return this.table.len();
    }
    
    protected String luaTostring() {
        return "a ZObject instance";
    }
    
    @Override
    public Object next(final Object o) {
        return this.table.next(o);
    }
    
    @Override
    public Object rawget(Object o) {
        if (o instanceof String) {
            o = this.getItem((String)o);
        }
        else {
            o = this.table.rawget(o);
        }
        return o;
    }
    
    @Override
    public void rawset(final Object obj, final Object o) {
        if (obj instanceof String) {
            this.setItem((String)obj, o);
        }
        this.table.rawset(obj, o);
        final StringBuilder append = new StringBuilder().append("PROP: ").append(this.toString()).append(".").append(obj).append(" is set to ");
        String string;
        if (o == null) {
            string = "nil";
        }
        else {
            string = o.toString();
        }
        Engine.log(append.append(string).toString(), 0);
    }
    
    @Override
    public void serialize(final DataOutputStream dataOutputStream) throws IOException {
        Engine.instance.savegame.storeValue(this.table, dataOutputStream);
    }
    
    protected void setItem(final String s, final Object o) {
        if ("Name".equals(s)) {
            this.name = BaseLib.rawTostring(o);
        }
        else if ("Description".equals(s)) {
            this.description = Engine.removeHtml(BaseLib.rawTostring(o));
        }
        else if ("Visible".equals(s)) {
            this.visible = LuaState.boolEval(o);
        }
        else if ("ObjectLocation".equals(s)) {
            this.position = (ZonePoint)o;
        }
        else if ("Media".equals(s)) {
            this.media = (Media)o;
        }
        else if ("Icon".equals(s)) {
            this.icon = (Media)o;
        }
    }
    
    @Override
    public void setMetatable(final LuaTable luaTable) {
    }
    
    public void setPosition(final ZonePoint position) {
        this.position = position;
        this.table.rawset("ObjectLocation", position);
    }
    
    public void setTable(final LuaTable luaTable) {
        Object next = null;
        while (true) {
            next = luaTable.next(next);
            if (next == null) {
                break;
            }
            this.rawset(next, luaTable.rawget(next));
        }
    }
    
    @Override
    public String toString() {
        String name;
        if (this.name == null) {
            name = "(unnamed)";
        }
        else {
            name = this.name;
        }
        return name;
    }
    
    private static class TostringJavaFunc implements JavaFunction
    {
        public EventTable parent;
        
        public TostringJavaFunc(final EventTable parent) {
            this.parent = parent;
        }
        
        @Override
        public int call(final LuaCallFrame luaCallFrame, final int n) {
            luaCallFrame.push(this.parent.luaTostring());
            return 1;
        }
    }
}
