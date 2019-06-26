package p005cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import p009se.krka.kahlua.p010vm.JavaFunction;
import p009se.krka.kahlua.p010vm.LuaCallFrame;
import p009se.krka.kahlua.p010vm.LuaClosure;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.p010vm.LuaTableImpl;
import p009se.krka.kahlua.stdlib.BaseLib;

/* renamed from: cz.matejcik.openwig.EventTable */
public class EventTable implements LuaTable, Serializable {
    public String description;
    public Media icon;
    public Media media;
    private LuaTable metatable = new LuaTableImpl();
    public String name;
    public ZonePoint position = null;
    public LuaTable table = new LuaTableImpl();
    protected boolean visible = false;

    /* renamed from: cz.matejcik.openwig.EventTable$TostringJavaFunc */
    private static class TostringJavaFunc implements JavaFunction {
        public EventTable parent;

        public TostringJavaFunc(EventTable parent) {
            this.parent = parent;
        }

        public int call(LuaCallFrame callFrame, int nArguments) {
            callFrame.push(this.parent.luaTostring());
            return 1;
        }
    }

    /* Access modifiers changed, original: protected */
    public String luaTostring() {
        return "a ZObject instance";
    }

    public EventTable() {
        this.metatable.rawset("__tostring", new TostringJavaFunc(this));
    }

    public void serialize(DataOutputStream out) throws IOException {
        Engine.instance.savegame.storeValue(this.table, out);
    }

    public void deserialize(DataInputStream in) throws IOException {
        Engine.instance.savegame.restoreValue(in, this);
    }

    public byte[] getMedia() throws IOException {
        return Engine.mediaFile(this.media);
    }

    public byte[] getIcon() throws IOException {
        return Engine.mediaFile(this.icon);
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setPosition(ZonePoint location) {
        this.position = location;
        this.table.rawset("ObjectLocation", location);
    }

    public boolean isLocated() {
        return this.position != null;
    }

    /* Access modifiers changed, original: protected */
    public void setItem(String key, Object value) {
        if ("Name".equals(key)) {
            this.name = BaseLib.rawTostring(value);
        } else if ("Description".equals(key)) {
            this.description = Engine.removeHtml(BaseLib.rawTostring(value));
        } else if ("Visible".equals(key)) {
            this.visible = LuaState.boolEval(value);
        } else if ("ObjectLocation".equals(key)) {
            this.position = (ZonePoint) value;
        } else if ("Media".equals(key)) {
            this.media = (Media) value;
        } else if ("Icon".equals(key)) {
            this.icon = (Media) value;
        }
    }

    /* Access modifiers changed, original: protected */
    public Object getItem(String key) {
        if ("CurrentDistance".equals(key)) {
            if (isLocated()) {
                return LuaState.toDouble(this.position.distance(Engine.instance.player.position));
            }
            return LuaState.toDouble(-1);
        } else if (!"CurrentBearing".equals(key)) {
            return this.table.rawget(key);
        } else {
            if (isLocated()) {
                return LuaState.toDouble(ZonePoint.angle2azimuth(this.position.bearing(Engine.instance.player.position)));
            }
            return LuaState.toDouble(0);
        }
    }

    public void setTable(LuaTable table) {
        Object n = null;
        while (true) {
            n = table.next(n);
            if (n != null) {
                rawset(n, table.rawget(n));
            } else {
                return;
            }
        }
    }

    public void callEvent(String name, Object param) {
        try {
            Object o = this.table.rawget(name);
            if (o instanceof LuaClosure) {
                Engine.log("EVNT: " + toString() + "." + name + (param != null ? " (" + param.toString() + ")" : ""), 1);
                Engine.state.call((LuaClosure) o, this, param, null);
                Engine.log("EEND: " + toString() + "." + name, 1);
            }
        } catch (Throwable t) {
            Engine.stacktrace(t);
        }
    }

    public boolean hasEvent(String name) {
        return this.table.rawget(name) instanceof LuaClosure;
    }

    public String toString() {
        return this.name == null ? "(unnamed)" : this.name;
    }

    public void rawset(Object key, Object value) {
        if (key instanceof String) {
            setItem((String) key, value);
        }
        this.table.rawset(key, value);
        Engine.log("PROP: " + toString() + "." + key + " is set to " + (value == null ? BaseLib.TYPE_NIL : value.toString()), 0);
    }

    public void setMetatable(LuaTable metatable) {
    }

    public LuaTable getMetatable() {
        return this.metatable;
    }

    public Object rawget(Object key) {
        if (key instanceof String) {
            return getItem((String) key);
        }
        return this.table.rawget(key);
    }

    public Object next(Object key) {
        return this.table.next(key);
    }

    public int len() {
        return this.table.len();
    }
}
