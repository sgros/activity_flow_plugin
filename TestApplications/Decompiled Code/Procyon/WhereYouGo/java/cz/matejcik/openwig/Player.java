// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.stdlib.TableLib;
import java.io.IOException;
import java.io.DataInputStream;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaTableImpl;
import se.krka.kahlua.vm.JavaFunction;

public class Player extends Thing
{
    private static JavaFunction refreshLocation;
    private LuaTableImpl insideOfZones;
    
    static {
        Player.refreshLocation = new JavaFunction() {
            @Override
            public int call(final LuaCallFrame luaCallFrame, final int n) {
                Engine.instance.player.refreshLocation();
                return 0;
            }
        };
    }
    
    public Player() {
        super(true);
        this.insideOfZones = new LuaTableImpl();
        this.table.rawset("RefreshLocation", Player.refreshLocation);
        this.table.rawset("InsideOfZones", this.insideOfZones);
        this.setPosition(new ZonePoint(360.0, 360.0, 0.0));
    }
    
    public static void register() {
        Engine.instance.savegame.addJavafunc(Player.refreshLocation);
    }
    
    @Override
    public void deserialize(final DataInputStream dataInputStream) throws IOException {
        super.deserialize(dataInputStream);
        Engine.instance.player = this;
    }
    
    public void enterZone(final Zone container) {
        this.container = container;
        if (!TableLib.contains(this.insideOfZones, container)) {
            TableLib.rawappend(this.insideOfZones, container);
        }
    }
    
    public void leaveZone(final Zone zone) {
        TableLib.removeItem(this.insideOfZones, zone);
        if (this.insideOfZones.len() > 0) {
            this.container = (Container)this.insideOfZones.rawget(new Double(this.insideOfZones.len()));
        }
    }
    
    @Override
    protected String luaTostring() {
        return "a Player instance";
    }
    
    @Override
    public void moveTo(final Container container) {
    }
    
    @Override
    public Object rawget(Object anObject) {
        if ("ObjectLocation".equals(anObject)) {
            anObject = ZonePoint.copy(this.position);
        }
        else {
            anObject = super.rawget(anObject);
        }
        return anObject;
    }
    
    @Override
    public void rawset(final Object anObject, final Object o) {
        if (!"ObjectLocation".equals(anObject)) {
            super.rawset(anObject, o);
        }
    }
    
    public void refreshLocation() {
        this.position.latitude = Engine.gps.getLatitude();
        this.position.longitude = Engine.gps.getLongitude();
        this.position.altitude = Engine.gps.getAltitude();
        this.table.rawset("PositionAccuracy", LuaState.toDouble(Engine.gps.getPrecision()));
        Engine.instance.cartridge.walk(this.position);
    }
    
    public int visibleThings() {
        int n = 0;
        Object o = null;
        while (true) {
            final Object next = this.inventory.next(o);
            if (next == null) {
                break;
            }
            final Object rawget = this.inventory.rawget(next);
            o = next;
            if (!(rawget instanceof Thing)) {
                continue;
            }
            o = next;
            if (!((Thing)rawget).isVisible()) {
                continue;
            }
            ++n;
            o = next;
        }
        return n;
    }
}
