// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity.wherigo;

import java.util.Vector;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import menion.android.whereyougo.gui.activity.MainActivity;
import cz.matejcik.openwig.Action;
import cz.matejcik.openwig.Thing;

public class ListActionsActivity extends ListVariousActivity
{
    private static Thing thing;
    
    public static void callAction(final Action action) {
        final String string = "On" + action.getName();
        if (action.hasParameter()) {
            if (action.getActor() == ListActionsActivity.thing) {
                ListTargetsActivity.reset(ListActionsActivity.thing.name + ": " + action.text, action, ListActionsActivity.thing);
                MainActivity.wui.showScreen(13, null);
            }
            else {
                Engine.callEvent(action.getActor(), string, ListActionsActivity.thing);
            }
        }
        else {
            Engine.callEvent(ListActionsActivity.thing, string, null);
        }
    }
    
    public static Vector<Object> getValidActions(final Thing thing) {
        final Vector<Action> vector = new Vector<Action>();
        for (int i = 0; i < thing.actions.size(); ++i) {
            vector.add((Action)thing.actions.get(i));
        }
        int n;
        for (int j = 0; j < vector.size(); j = n + 1) {
            final Action action = vector.elementAt(j);
            if (action.isEnabled()) {
                n = j;
                if (action.getActor().visibleToPlayer()) {
                    continue;
                }
            }
            vector.removeElementAt(j);
            n = j - 1;
        }
        return (Vector<Object>)vector;
    }
    
    public static void reset(final Thing thing) {
        ListActionsActivity.thing = thing;
    }
    
    @Override
    protected void callStuff(final Object o) {
        callAction((Action)o);
        this.finish();
    }
    
    @Override
    protected String getStuffName(final Object o) {
        final Action action = (Action)o;
        String s;
        if (action.getActor() == ListActionsActivity.thing) {
            s = action.text;
        }
        else {
            s = String.format("%s: %s", action.getActor().name, action.text);
        }
        return s;
    }
    
    @Override
    protected Vector<Object> getValidStuff() {
        return getValidActions(ListActionsActivity.thing);
    }
    
    @Override
    protected boolean stillValid() {
        return ListActionsActivity.thing.visibleToPlayer() && ListActionsActivity.thing.visibleActions() > 0;
    }
}
