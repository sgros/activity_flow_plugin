package android.support.transition;

import android.os.Build.VERSION;
import android.view.ViewGroup;

class ViewGroupUtils {
   static ViewGroupOverlayImpl getOverlay(ViewGroup var0) {
      return (ViewGroupOverlayImpl)(VERSION.SDK_INT >= 18 ? new ViewGroupOverlayApi18(var0) : ViewGroupOverlayApi14.createFrom(var0));
   }

   static void suppressLayout(ViewGroup var0, boolean var1) {
      if (VERSION.SDK_INT >= 18) {
         ViewGroupUtilsApi18.suppressLayout(var0, var1);
      } else {
         ViewGroupUtilsApi14.suppressLayout(var0, var1);
      }

   }
}
