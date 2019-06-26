package p005cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.IOException;
import p009se.krka.kahlua.p010vm.JavaFunction;
import p009se.krka.kahlua.p010vm.LuaCallFrame;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.p010vm.LuaTableImpl;
import p009se.krka.kahlua.stdlib.TableLib;

/* renamed from: cz.matejcik.openwig.Container */
public class Container extends EventTable {
    private static JavaFunction contains = new C04022();
    private static JavaFunction moveTo = new C04011();
    public Container container = null;
    public LuaTable inventory = new LuaTableImpl();

    /* renamed from: cz.matejcik.openwig.Container$1 */
    static class C04011 implements JavaFunction {
        C04011() {
        }

        public int call(LuaCallFrame callFrame, int nArguments) {
            ((Container) callFrame.get(0)).moveTo((Container) callFrame.get(1));
            return 0;
        }
    }

    /* renamed from: cz.matejcik.openwig.Container$2 */
    static class C04022 implements JavaFunction {
        C04022() {
        }

        public int call(LuaCallFrame callFrame, int nArguments) {
            callFrame.push(LuaState.toBoolean(((Container) callFrame.get(0)).contains((Thing) callFrame.get(1))));
            return 1;
        }
    }

    public static void register() {
        Engine.instance.savegame.addJavafunc(moveTo);
        Engine.instance.savegame.addJavafunc(contains);
    }

    public Container() {
        this.table.rawset("MoveTo", moveTo);
        this.table.rawset("Contains", contains);
        this.table.rawset("Inventory", this.inventory);
        this.table.rawset("Container", this.container);
    }

    public void moveTo(Container c) {
        Engine.log("MOVE: " + this.name + " to " + (c == null ? "(nowhere)" : c.name), 1);
        if (this.container != null) {
            TableLib.removeItem(this.container.inventory, this);
        }
        if (c != null) {
            TableLib.rawappend(c.inventory, this);
            if (c == Engine.instance.player) {
                setPosition(null);
            } else if (this.position != null) {
                setPosition(c.position);
            } else if (this.container == Engine.instance.player) {
                setPosition(ZonePoint.copy(Engine.instance.player.position));
            }
            this.container = c;
        } else {
            this.container = null;
            rawset("ObjectLocation", null);
        }
        this.table.rawset("Container", this.container);
    }

    public boolean contains(Thing t) {
        Object key = null;
        while (true) {
            key = this.inventory.next(key);
            if (key == null) {
                return false;
            }
            Thing value = this.inventory.rawget(key);
            if ((value instanceof Thing) && (value == t || value.contains(t))) {
                return true;
            }
        }
    }

    public boolean visibleToPlayer() {
        if (!isVisible()) {
            return false;
        }
        if (this.container == Engine.instance.player) {
            return true;
        }
        if (this.container instanceof Zone) {
            return this.container.showThings();
        }
        return false;
    }

    public Object rawget(Object key) {
        if ("Container".equals(key)) {
            return this.container;
        }
        return super.rawget(key);
    }

    public void deserialize(DataInputStream in) throws IOException {
        super.deserialize(in);
        this.inventory = (LuaTable) this.table.rawget("Inventory");
        Object o = this.table.rawget("Container");
        if (o instanceof Container) {
            this.container = (Container) o;
        } else {
            this.container = null;
        }
    }
}
