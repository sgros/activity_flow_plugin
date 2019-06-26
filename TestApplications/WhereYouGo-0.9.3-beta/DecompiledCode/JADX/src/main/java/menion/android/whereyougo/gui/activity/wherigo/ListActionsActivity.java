package menion.android.whereyougo.gui.activity.wherigo;

import java.util.Vector;
import menion.android.whereyougo.gui.activity.MainActivity;
import p005cz.matejcik.openwig.Action;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.Thing;

public class ListActionsActivity extends ListVariousActivity {
    private static Thing thing;

    public static void callAction(Action z) {
        String eventName = "On" + z.getName();
        if (!z.hasParameter()) {
            Engine.callEvent(thing, eventName, null);
        } else if (z.getActor() == thing) {
            ListTargetsActivity.reset(thing.name + ": " + z.text, z, thing);
            MainActivity.wui.showScreen(13, null);
        } else {
            Engine.callEvent(z.getActor(), eventName, thing);
        }
    }

    public static Vector<Object> getValidActions(Thing thing) {
        int i;
        Vector<Object> newActions = new Vector();
        for (i = 0; i < thing.actions.size(); i++) {
            newActions.add(thing.actions.get(i));
        }
        i = 0;
        while (i < newActions.size()) {
            Action a = (Action) newActions.elementAt(i);
            if (!a.isEnabled() || !a.getActor().visibleToPlayer()) {
                int i2 = i - 1;
                newActions.removeElementAt(i);
                i = i2;
            }
            i++;
        }
        return newActions;
    }

    public static void reset(Thing what) {
        thing = what;
    }

    /* Access modifiers changed, original: protected */
    public void callStuff(Object what) {
        callAction((Action) what);
        finish();
    }

    /* Access modifiers changed, original: protected */
    public String getStuffName(Object what) {
        Action a = (Action) what;
        if (a.getActor() == thing) {
            return a.text;
        }
        return String.format("%s: %s", new Object[]{a.getActor().name, a.text});
    }

    /* Access modifiers changed, original: protected */
    public Vector<Object> getValidStuff() {
        return getValidActions(thing);
    }

    /* Access modifiers changed, original: protected */
    public boolean stillValid() {
        return thing.visibleToPlayer() && thing.visibleActions() > 0;
    }
}
