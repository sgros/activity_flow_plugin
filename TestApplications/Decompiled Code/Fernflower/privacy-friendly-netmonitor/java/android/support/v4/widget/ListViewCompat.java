package android.support.v4.widget;

import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

public final class ListViewCompat {
   private ListViewCompat() {
   }

   public static boolean canScrollList(@NonNull ListView var0, int var1) {
      if (VERSION.SDK_INT >= 19) {
         return var0.canScrollList(var1);
      } else {
         int var2 = var0.getChildCount();
         boolean var3 = false;
         boolean var4 = false;
         if (var2 == 0) {
            return false;
         } else {
            int var5 = var0.getFirstVisiblePosition();
            if (var1 > 0) {
               var1 = var0.getChildAt(var2 - 1).getBottom();
               if (var5 + var2 < var0.getCount() || var1 > var0.getHeight() - var0.getListPaddingBottom()) {
                  var4 = true;
               }

               return var4;
            } else {
               var1 = var0.getChildAt(0).getTop();
               if (var5 <= 0) {
                  var4 = var3;
                  if (var1 >= var0.getListPaddingTop()) {
                     return var4;
                  }
               }

               var4 = true;
               return var4;
            }
         }
      }
   }

   public static void scrollListBy(@NonNull ListView var0, int var1) {
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
