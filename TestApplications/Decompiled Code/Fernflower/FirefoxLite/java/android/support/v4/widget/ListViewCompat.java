package android.support.v4.widget;

import android.os.Build.VERSION;
import android.view.View;
import android.widget.ListView;

public final class ListViewCompat {
   public static void scrollListBy(ListView var0, int var1) {
      if (VERSION.SDK_INT >= 19) {
         var0.scrollListBy(var1);
      } else {
         int var2 = var0.getFirstVisiblePosition();
         if (var2 == -1) {
            return;
         }

         View var3 = var0.getChildAt(0);
         if (var3 == null) {
            return;
         }

         var0.setSelectionFromTop(var2, var3.getTop() - var1);
      }

   }
}
