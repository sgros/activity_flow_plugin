package menion.android.whereyougo.gui.activity.wherigo;

import android.os.Bundle;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Thing;
import java.util.Vector;
import menion.android.whereyougo.gui.activity.MainActivity;
import se.krka.kahlua.vm.LuaTable;

public class ListThingsActivity extends ListVariousActivity {
   public static final int INVENTORY = 0;
   public static final int SURROUNDINGS = 1;
   private int mode;

   protected void callStuff(Object var1) {
      Thing var2 = (Thing)var1;
      if (var2.hasEvent("OnClick")) {
         Engine.callEvent(var2, "OnClick", (Object)null);
      } else {
         MainActivity.wui.showScreen(1, var2);
      }

      this.finish();
   }

   protected String getStuffName(Object var1) {
      return ((Thing)var1).name;
   }

   protected Vector getValidStuff() {
      LuaTable var1;
      if (this.mode == 0) {
         var1 = Engine.instance.player.inventory;
      } else {
         var1 = Engine.instance.cartridge.currentThings();
      }

      Vector var2 = new Vector();
      Object var3 = null;

      while(true) {
         Object var4 = var1.next(var3);
         if (var4 == null) {
            return var2;
         }

         Thing var5 = (Thing)var1.rawget(var4);
         var3 = var4;
         if (var5.isVisible()) {
            var2.add(var5);
            var3 = var4;
         }
      }
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.mode = this.getIntent().getIntExtra("mode", 0);
   }

   protected boolean stillValid() {
      return true;
   }
}
