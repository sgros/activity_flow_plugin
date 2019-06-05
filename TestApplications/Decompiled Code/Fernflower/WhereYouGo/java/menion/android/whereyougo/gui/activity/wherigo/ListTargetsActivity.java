package menion.android.whereyougo.gui.activity.wherigo;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import cz.matejcik.openwig.Action;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Thing;
import java.util.Vector;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import se.krka.kahlua.vm.LuaTable;

public class ListTargetsActivity extends ListVariousActivity {
   private static Action action;
   private static Thing thing;
   private static Vector validStuff;

   private static void makeValidStuff() {
      LuaTable var0 = Engine.instance.cartridge.currentThings();
      validStuff = new Vector();
      Object var1 = null;

      while(true) {
         var1 = var0.next(var1);
         Object var2 = var1;
         if (var1 == null) {
            while(true) {
               var2 = Engine.instance.player.inventory.next(var2);
               if (var2 == null) {
                  int var4;
                  for(int var3 = 0; var3 < validStuff.size(); var3 = var4 + 1) {
                     Thing var5 = (Thing)validStuff.elementAt(var3);
                     if (var5.isVisible()) {
                        var4 = var3;
                        if (action.isTarget(var5)) {
                           continue;
                        }
                     }

                     validStuff.removeElementAt(var3);
                     var4 = var3 - 1;
                  }

                  return;
               }

               validStuff.addElement(Engine.instance.player.inventory.rawget(var2));
            }
         }

         validStuff.addElement(var0.rawget(var1));
      }
   }

   public static void reset(String var0, Action var1, Thing var2) {
      action = var1;
      thing = var2;
      makeValidStuff();
   }

   protected void callStuff(Object var1) {
      MainActivity.wui.showScreen(1, DetailsActivity.et);
      String var2 = "On" + action.getName();
      Engine.callEvent(action.getActor(), var2, var1);
      this.finish();
   }

   protected String getStuffName(Object var1) {
      return ((Thing)var1).name;
   }

   protected Vector getValidStuff() {
      return validStuff;
   }

   public void refresh() {
      if (validStuff.isEmpty()) {
         UtilsGUI.showDialogInfo(this, 2131165228, new OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
               ListTargetsActivity.this.finish();
            }
         });
      } else {
         super.refresh();
      }

   }

   protected boolean stillValid() {
      return thing.visibleToPlayer();
   }
}
