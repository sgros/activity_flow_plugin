package p005cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.IOException;
import p009se.krka.kahlua.p010vm.JavaFunction;
import p009se.krka.kahlua.p010vm.LuaCallFrame;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTableImpl;
import p009se.krka.kahlua.stdlib.TableLib;

/* renamed from: cz.matejcik.openwig.Player */
public class Player extends Thing {
    private static JavaFunction refreshLocation = new C04031();
    private LuaTableImpl insideOfZones = new LuaTableImpl();

    /* renamed from: cz.matejcik.openwig.Player$1 */
    static class C04031 implements JavaFunction {
        C04031() {
        }

        public int call(LuaCallFrame callFrame, int nArguments) {
            Engine.instance.player.refreshLocation();
            return 0;
        }
    }

    public static void register() {
        Engine.instance.savegame.addJavafunc(refreshLocation);
    }

    public Player() {
        super(true);
        this.table.rawset("RefreshLocation", refreshLocation);
        this.table.rawset("InsideOfZones", this.insideOfZones);
        setPosition(new ZonePoint(360.0d, 360.0d, 0.0d));
    }

    public void moveTo(Container c) {
    }

    public void enterZone(Zone z) {
        this.container = z;
        if (!TableLib.contains(this.insideOfZones, z)) {
            TableLib.rawappend(this.insideOfZones, z);
        }
    }

    public void leaveZone(Zone z) {
        TableLib.removeItem(this.insideOfZones, z);
        if (this.insideOfZones.len() > 0) {
            this.container = (Container) this.insideOfZones.rawget(new Double((double) this.insideOfZones.len()));
        }
    }

    /* Access modifiers changed, original: protected */
    public String luaTostring() {
        return "a Player instance";
    }

    public void deserialize(DataInputStream in) throws IOException {
        super.deserialize(in);
        Engine.instance.player = this;
    }

    public int visibleThings() {
        int count = 0;
        Object key = null;
        while (true) {
            key = this.inventory.next(key);
            if (key == null) {
                return count;
            }
            Object o = this.inventory.rawget(key);
            if ((o instanceof Thing) && ((Thing) o).isVisible()) {
                count++;
            }
        }
    }

    public void refreshLocation() {
        this.position.latitude = Engine.gps.getLatitude();
        this.position.longitude = Engine.gps.getLongitude();
        this.position.altitude = Engine.gps.getAltitude();
        this.table.rawset("PositionAccuracy", LuaState.toDouble(Engine.gps.getPrecision()));
        Engine.instance.cartridge.walk(this.position);
    }

    public void rawset(Object key, Object value) {
        if (!"ObjectLocation".equals(key)) {
            super.rawset(key, value);
        }
    }

    public Object rawget(Object key) {
        if ("ObjectLocation".equals(key)) {
            return ZonePoint.copy(this.position);
        }
        return super.rawget(key);
    }
}
