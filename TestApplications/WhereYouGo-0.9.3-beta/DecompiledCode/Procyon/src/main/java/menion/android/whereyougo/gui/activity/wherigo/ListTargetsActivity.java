// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity.wherigo;

import android.app.Activity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import cz.matejcik.openwig.EventTable;
import menion.android.whereyougo.gui.activity.MainActivity;
import se.krka.kahlua.vm.LuaTable;
import cz.matejcik.openwig.Engine;
import java.util.Vector;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Action;

public class ListTargetsActivity extends ListVariousActivity
{
    private static Action action;
    private static Thing thing;
    private static Vector<Object> validStuff;
    
    private static void makeValidStuff() {
        final LuaTable currentThings = Engine.instance.cartridge.currentThings();
        ListTargetsActivity.validStuff = new Vector<Object>();
        Object o = null;
        Object o2;
        while (true) {
            o = (o2 = currentThings.next(o));
            if (o == null) {
                break;
            }
            ListTargetsActivity.validStuff.addElement(currentThings.rawget(o));
        }
        while (true) {
            o2 = Engine.instance.player.inventory.next(o2);
            if (o2 == null) {
                break;
            }
            ListTargetsActivity.validStuff.addElement(Engine.instance.player.inventory.rawget(o2));
        }
        int n;
        for (int i = 0; i < ListTargetsActivity.validStuff.size(); i = n + 1) {
            final Thing thing = ListTargetsActivity.validStuff.elementAt(i);
            if (thing.isVisible()) {
                n = i;
                if (ListTargetsActivity.action.isTarget(thing)) {
                    continue;
                }
            }
            ListTargetsActivity.validStuff.removeElementAt(i);
            n = i - 1;
        }
    }
    
    public static void reset(final String s, final Action action, final Thing thing) {
        ListTargetsActivity.action = action;
        ListTargetsActivity.thing = thing;
        makeValidStuff();
    }
    
    @Override
    protected void callStuff(final Object o) {
        MainActivity.wui.showScreen(1, DetailsActivity.et);
        Engine.callEvent(ListTargetsActivity.action.getActor(), "On" + ListTargetsActivity.action.getName(), o);
        this.finish();
    }
    
    @Override
    protected String getStuffName(final Object o) {
        return ((Thing)o).name;
    }
    
    @Override
    protected Vector<Object> getValidStuff() {
        return ListTargetsActivity.validStuff;
    }
    
    @Override
    public void refresh() {
        if (ListTargetsActivity.validStuff.isEmpty()) {
            UtilsGUI.showDialogInfo(this, 2131165228, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    ListTargetsActivity.this.finish();
                }
            });
        }
        else {
            super.refresh();
        }
    }
    
    @Override
    protected boolean stillValid() {
        return ListTargetsActivity.thing.visibleToPlayer();
    }
}
