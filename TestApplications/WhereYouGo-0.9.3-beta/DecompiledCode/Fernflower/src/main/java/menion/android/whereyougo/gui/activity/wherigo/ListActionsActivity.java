package menion.android.whereyougo.gui.activity.wherigo;

import cz.matejcik.openwig.Action;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Thing;
import java.util.Vector;
import menion.android.whereyougo.gui.activity.MainActivity;

public class ListActionsActivity extends ListVariousActivity {
   private static Thing thing;

   public static void callAction(Action var0) {
      String var1 = "On" + var0.getName();
      if (var0.hasParameter()) {
         if (var0.getActor() == thing) {
            ListTargetsActivity.reset(thing.name + ": " + var0.text, var0, thing);
            MainActivity.wui.showScreen(13, (EventTable)null);
         } else {
            Engine.callEvent(var0.getActor(), var1, thing);
         }
      } else {
         Engine.callEvent(thing, var1, (Object)null);
      }

   }

   public static Vector getValidActions(Thing var0) {
      Vector var1 = new Vector();

      int var2;
      for(var2 = 0; var2 < var0.actions.size(); ++var2) {
         var1.add(var0.actions.get(var2));
      }

      int var3;
      for(var2 = 0; var2 < var1.size(); var2 = var3 + 1) {
         Action var4 = (Action)var1.elementAt(var2);
         if (var4.isEnabled()) {
            var3 = var2;
            if (var4.getActor().visibleToPlayer()) {
               continue;
            }
         }

         var1.removeElementAt(var2);
         var3 = var2 - 1;
      }

      return var1;
   }

   public static void reset(Thing var0) {
      thing = var0;
   }

   protected void callStuff(Object var1) {
      callAction((Action)var1);
      this.finish();
   }

   protected String getStuffName(Object var1) {
      Action var2 = (Action)var1;
      String var3;
      if (var2.getActor() == thing) {
         var3 = var2.text;
      } else {
         var3 = String.format("%s: %s", var2.getActor().name, var2.text);
      }

      return var3;
   }

   protected Vector getValidStuff() {
      return getValidActions(thing);
   }

   protected boolean stillValid() {
      boolean var1;
      if (thing.visibleToPlayer() && thing.visibleActions() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
