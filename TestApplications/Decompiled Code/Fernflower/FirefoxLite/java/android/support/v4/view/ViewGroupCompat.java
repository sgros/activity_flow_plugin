package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.compat.R;
import android.view.ViewGroup;

public final class ViewGroupCompat {
   public static boolean isTransitionGroup(ViewGroup var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0.isTransitionGroup();
      } else {
         Boolean var1 = (Boolean)var0.getTag(R.id.tag_transition_group);
         boolean var2;
         if ((var1 == null || !var1) && var0.getBackground() == null && ViewCompat.getTransitionName(var0) == null) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }
   }
}
