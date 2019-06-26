// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity.wherigo;

import android.os.Bundle;
import se.krka.kahlua.vm.LuaTable;
import java.util.Vector;
import menion.android.whereyougo.gui.activity.MainActivity;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Thing;

public class ListThingsActivity extends ListVariousActivity
{
    public static final int INVENTORY = 0;
    public static final int SURROUNDINGS = 1;
    private int mode;
    
    @Override
    protected void callStuff(final Object o) {
        final Thing thing = (Thing)o;
        if (thing.hasEvent("OnClick")) {
            Engine.callEvent(thing, "OnClick", null);
        }
        else {
            MainActivity.wui.showScreen(1, thing);
        }
        this.finish();
    }
    
    @Override
    protected String getStuffName(final Object o) {
        return ((Thing)o).name;
    }
    
    @Override
    protected Vector<Object> getValidStuff() {
        LuaTable luaTable;
        if (this.mode == 0) {
            luaTable = Engine.instance.player.inventory;
        }
        else {
            luaTable = Engine.instance.cartridge.currentThings();
        }
        final Vector<Thing> vector = (Vector<Thing>)new Vector<Object>();
        Object o = null;
        while (true) {
            final Object next = luaTable.next(o);
            if (next == null) {
                break;
            }
            final Thing e = (Thing)luaTable.rawget(next);
            o = next;
            if (!e.isVisible()) {
                continue;
            }
            vector.add(e);
            o = next;
        }
        return (Vector<Object>)vector;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mode = this.getIntent().getIntExtra("mode", 0);
    }
    
    @Override
    protected boolean stillValid() {
        return true;
    }
}
