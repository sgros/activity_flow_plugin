package p005cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import p009se.krka.kahlua.p010vm.JavaFunction;
import p009se.krka.kahlua.p010vm.LuaCallFrame;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.p010vm.LuaTableImpl;
import p009se.krka.kahlua.stdlib.TableLib;

/* renamed from: cz.matejcik.openwig.Cartridge */
public class Cartridge extends EventTable {
    private static JavaFunction requestSync = new C04001();
    public LuaTable allZObjects = new LuaTableImpl();
    public Vector tasks = new Vector();
    public Vector things = new Vector();
    public Vector timers = new Vector();
    public Vector universalActions = new Vector();
    public Vector zones = new Vector();

    /* renamed from: cz.matejcik.openwig.Cartridge$1 */
    static class C04001 implements JavaFunction {
        C04001() {
        }

        public int call(LuaCallFrame callFrame, int nArguments) {
            Engine.instance.store();
            return 0;
        }
    }

    public static void register() {
        Engine.instance.savegame.addJavafunc(requestSync);
    }

    /* Access modifiers changed, original: protected */
    public String luaTostring() {
        return "a ZCartridge instance";
    }

    public Cartridge() {
        this.table.rawset("RequestSync", requestSync);
        this.table.rawset("AllZObjects", this.allZObjects);
        TableLib.rawappend(this.allZObjects, this);
    }

    public void walk(ZonePoint zp) {
        for (int i = 0; i < this.zones.size(); i++) {
            ((Zone) this.zones.elementAt(i)).walk(zp);
        }
    }

    public void tick() {
        int i;
        for (i = 0; i < this.zones.size(); i++) {
            ((Zone) this.zones.elementAt(i)).tick();
        }
        for (i = 0; i < this.timers.size(); i++) {
            ((Timer) this.timers.elementAt(i)).updateRemaining();
        }
    }

    public int visibleZones() {
        int count = 0;
        for (int i = 0; i < this.zones.size(); i++) {
            if (((Zone) this.zones.elementAt(i)).isVisible()) {
                count++;
            }
        }
        return count;
    }

    public int visibleThings() {
        int count = 0;
        for (int i = 0; i < this.zones.size(); i++) {
            count += ((Zone) this.zones.elementAt(i)).visibleThings();
        }
        return count;
    }

    public LuaTable currentThings() {
        LuaTable ret = new LuaTableImpl();
        for (int i = 0; i < this.zones.size(); i++) {
            ((Zone) this.zones.elementAt(i)).collectThings(ret);
        }
        return ret;
    }

    public int visibleUniversalActions() {
        int count = 0;
        for (int i = 0; i < this.universalActions.size(); i++) {
            Action a = (Action) this.universalActions.elementAt(i);
            if (a.isEnabled() && a.getActor().visibleToPlayer()) {
                count++;
            }
        }
        return count;
    }

    public int visibleTasks() {
        int count = 0;
        for (int i = 0; i < this.tasks.size(); i++) {
            if (((Task) this.tasks.elementAt(i)).isVisible()) {
                count++;
            }
        }
        return count;
    }

    public void addObject(Object o) {
        TableLib.rawappend(this.allZObjects, o);
        sortObject(o);
    }

    private void sortObject(Object o) {
        if (o instanceof Task) {
            this.tasks.addElement(o);
        } else if (o instanceof Zone) {
            this.zones.addElement(o);
        } else if (o instanceof Timer) {
            this.timers.addElement(o);
        } else if (o instanceof Thing) {
            this.things.addElement(o);
        }
    }

    public void deserialize(DataInputStream in) throws IOException {
        super.deserialize(in);
        Engine.instance.cartridge = this;
        this.allZObjects = (LuaTable) this.table.rawget("AllZObjects");
        Object next = null;
        while (true) {
            next = this.allZObjects.next(next);
            if (next != null) {
                sortObject(this.allZObjects.rawget(next));
            } else {
                return;
            }
        }
    }
}
