package menion.android.whereyougo.gui.activity.wherigo;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.util.Vector;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import p005cz.matejcik.openwig.Action;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.Thing;
import p009se.krka.kahlua.p010vm.LuaTable;

public class ListTargetsActivity extends ListVariousActivity {
    private static Action action;
    private static Thing thing;
    private static Vector<Object> validStuff;

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.ListTargetsActivity$1 */
    class C02741 implements OnClickListener {
        C02741() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ListTargetsActivity.this.finish();
        }
    }

    private static void makeValidStuff() {
        LuaTable current = Engine.instance.cartridge.currentThings();
        validStuff = new Vector();
        Object key = null;
        while (true) {
            key = current.next(key);
            if (key == null) {
                break;
            }
            validStuff.addElement(current.rawget(key));
        }
        while (true) {
            key = Engine.instance.player.inventory.next(key);
            if (key == null) {
                break;
            }
            validStuff.addElement(Engine.instance.player.inventory.rawget(key));
        }
        int i = 0;
        while (i < validStuff.size()) {
            Thing t = (Thing) validStuff.elementAt(i);
            if (!t.isVisible() || !action.isTarget(t)) {
                int i2 = i - 1;
                validStuff.removeElementAt(i);
                i = i2;
            }
            i++;
        }
    }

    public static void reset(String title, Action what, Thing actor) {
        action = what;
        thing = actor;
        makeValidStuff();
    }

    /* Access modifiers changed, original: protected */
    public void callStuff(Object what) {
        MainActivity.wui.showScreen(1, DetailsActivity.f101et);
        Engine.callEvent(action.getActor(), "On" + action.getName(), what);
        finish();
    }

    /* Access modifiers changed, original: protected */
    public String getStuffName(Object what) {
        return ((Thing) what).name;
    }

    /* Access modifiers changed, original: protected */
    public Vector<Object> getValidStuff() {
        return validStuff;
    }

    public void refresh() {
        if (validStuff.isEmpty()) {
            UtilsGUI.showDialogInfo(this, C0254R.string.no_target, new C02741());
        } else {
            super.refresh();
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean stillValid() {
        return thing.visibleToPlayer();
    }
}
