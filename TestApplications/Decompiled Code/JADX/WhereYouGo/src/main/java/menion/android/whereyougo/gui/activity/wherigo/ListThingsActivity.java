package menion.android.whereyougo.gui.activity.wherigo;

import android.os.Bundle;
import java.util.Vector;
import menion.android.whereyougo.gui.activity.MainActivity;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.Thing;
import p009se.krka.kahlua.p010vm.LuaTable;

public class ListThingsActivity extends ListVariousActivity {
    public static final int INVENTORY = 0;
    public static final int SURROUNDINGS = 1;
    private int mode;

    /* Access modifiers changed, original: protected */
    public void callStuff(Object what) {
        Thing t = (Thing) what;
        if (t.hasEvent("OnClick")) {
            Engine.callEvent(t, "OnClick", null);
        } else {
            MainActivity.wui.showScreen(1, t);
        }
        finish();
    }

    /* Access modifiers changed, original: protected */
    public String getStuffName(Object what) {
        return ((Thing) what).name;
    }

    /* Access modifiers changed, original: protected */
    public Vector<Object> getValidStuff() {
        LuaTable container;
        if (this.mode == 0) {
            container = Engine.instance.player.inventory;
        } else {
            container = Engine.instance.cartridge.currentThings();
        }
        Vector<Object> newthings = new Vector();
        Object key = null;
        while (true) {
            key = container.next(key);
            if (key == null) {
                return newthings;
            }
            Thing t = (Thing) container.rawget(key);
            if (t.isVisible()) {
                newthings.add(t);
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mode = getIntent().getIntExtra("mode", 0);
    }

    /* Access modifiers changed, original: protected */
    public boolean stillValid() {
        return true;
    }
}
