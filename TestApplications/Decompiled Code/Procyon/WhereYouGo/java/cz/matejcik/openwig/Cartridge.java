// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import java.io.IOException;
import java.io.DataInputStream;
import se.krka.kahlua.stdlib.TableLib;
import se.krka.kahlua.vm.LuaTableImpl;
import se.krka.kahlua.vm.LuaCallFrame;
import java.util.Vector;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.JavaFunction;

public class Cartridge extends EventTable
{
    private static JavaFunction requestSync;
    public LuaTable allZObjects;
    public Vector tasks;
    public Vector things;
    public Vector timers;
    public Vector universalActions;
    public Vector zones;
    
    static {
        Cartridge.requestSync = new JavaFunction() {
            @Override
            public int call(final LuaCallFrame luaCallFrame, final int n) {
                Engine.instance.store();
                return 0;
            }
        };
    }
    
    public Cartridge() {
        this.zones = new Vector();
        this.timers = new Vector();
        this.things = new Vector();
        this.universalActions = new Vector();
        this.tasks = new Vector();
        this.allZObjects = new LuaTableImpl();
        this.table.rawset("RequestSync", Cartridge.requestSync);
        this.table.rawset("AllZObjects", this.allZObjects);
        TableLib.rawappend(this.allZObjects, this);
    }
    
    public static void register() {
        Engine.instance.savegame.addJavafunc(Cartridge.requestSync);
    }
    
    private void sortObject(final Object o) {
        if (o instanceof Task) {
            this.tasks.addElement(o);
        }
        else if (o instanceof Zone) {
            this.zones.addElement(o);
        }
        else if (o instanceof Timer) {
            this.timers.addElement(o);
        }
        else if (o instanceof Thing) {
            this.things.addElement(o);
        }
    }
    
    public void addObject(final Object o) {
        TableLib.rawappend(this.allZObjects, o);
        this.sortObject(o);
    }
    
    public LuaTable currentThings() {
        final LuaTableImpl luaTableImpl = new LuaTableImpl();
        for (int i = 0; i < this.zones.size(); ++i) {
            ((Zone)this.zones.elementAt(i)).collectThings(luaTableImpl);
        }
        return luaTableImpl;
    }
    
    @Override
    public void deserialize(final DataInputStream dataInputStream) throws IOException {
        super.deserialize(dataInputStream);
        Engine.instance.cartridge = this;
        this.allZObjects = (LuaTable)this.table.rawget("AllZObjects");
        Object next = null;
        while (true) {
            next = this.allZObjects.next(next);
            if (next == null) {
                break;
            }
            this.sortObject(this.allZObjects.rawget(next));
        }
    }
    
    @Override
    protected String luaTostring() {
        return "a ZCartridge instance";
    }
    
    public void tick() {
        for (int i = 0; i < this.zones.size(); ++i) {
            ((Zone)this.zones.elementAt(i)).tick();
        }
        for (int j = 0; j < this.timers.size(); ++j) {
            ((Timer)this.timers.elementAt(j)).updateRemaining();
        }
    }
    
    public int visibleTasks() {
        int n = 0;
        int n2;
        for (int i = 0; i < this.tasks.size(); ++i, n = n2) {
            n2 = n;
            if (((Task)this.tasks.elementAt(i)).isVisible()) {
                n2 = n + 1;
            }
        }
        return n;
    }
    
    public int visibleThings() {
        int n = 0;
        for (int i = 0; i < this.zones.size(); ++i) {
            n += ((Zone)this.zones.elementAt(i)).visibleThings();
        }
        return n;
    }
    
    public int visibleUniversalActions() {
        int n = 0;
        int n2;
        for (int i = 0; i < this.universalActions.size(); ++i, n = n2) {
            final Action action = this.universalActions.elementAt(i);
            n2 = n;
            if (action.isEnabled()) {
                n2 = n;
                if (action.getActor().visibleToPlayer()) {
                    n2 = n + 1;
                }
            }
        }
        return n;
    }
    
    public int visibleZones() {
        int n = 0;
        int n2;
        for (int i = 0; i < this.zones.size(); ++i, n = n2) {
            n2 = n;
            if (((Zone)this.zones.elementAt(i)).isVisible()) {
                n2 = n + 1;
            }
        }
        return n;
    }
    
    public void walk(final ZonePoint zonePoint) {
        for (int i = 0; i < this.zones.size(); ++i) {
            ((Zone)this.zones.elementAt(i)).walk(zonePoint);
        }
    }
}
