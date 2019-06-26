package menion.android.whereyougo.gui.activity.wherigo;

import android.graphics.Bitmap;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Task;
import java.util.Vector;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.utils.Images;

public class ListTasksActivity extends ListVariousActivity {
   private static final Bitmap[] stateIcons = new Bitmap[3];

   static {
      stateIcons[0] = Images.getImageB(2130837571);
      stateIcons[1] = Images.getImageB(2130837569);
      stateIcons[2] = Images.getImageB(2130837570);
   }

   protected void callStuff(Object var1) {
      Task var2 = (Task)var1;
      if (var2.hasEvent("OnClick")) {
         Engine.callEvent(var2, "OnClick", (Object)null);
      } else {
         MainActivity.wui.showScreen(1, var2);
      }

      this.finish();
   }

   protected Bitmap getStuffIcon(Object var1) {
      Bitmap var2 = super.getStuffIcon(var1);
      Bitmap var3 = var2;
      if (var2 == Images.IMAGE_EMPTY_B) {
         var3 = stateIcons[0];
      }

      if (((Task)var1).state() != 0) {
         var3 = Images.overlayBitmapToCenter(var3, stateIcons[((Task)var1).state()]);
      }

      return var3;
   }

   protected String getStuffName(Object var1) {
      return ((Task)var1).name;
   }

   protected Vector getValidStuff() {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < Engine.instance.cartridge.tasks.size(); ++var2) {
         Task var3 = (Task)Engine.instance.cartridge.tasks.get(var2);
         if (var3.isVisible()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   protected boolean stillValid() {
      return true;
   }
}
