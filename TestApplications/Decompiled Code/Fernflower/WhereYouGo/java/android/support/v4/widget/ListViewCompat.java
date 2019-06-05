package android.support.v4.widget;

import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.widget.ListView;

public final class ListViewCompat {
   private ListViewCompat() {
   }

   public static void scrollListBy(@NonNull ListView var0, int var1) {
      if (VERSION.SDK_INT >= 19) {
         ListViewCompatKitKat.scrollListBy(var0, var1);
      } else {
         ListViewCompatGingerbread.scrollListBy(var0, var1);
      }

   }
}
