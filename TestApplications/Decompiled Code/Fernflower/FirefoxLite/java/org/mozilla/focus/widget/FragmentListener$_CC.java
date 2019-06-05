package org.mozilla.focus.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

// $FF: synthetic class
public final class FragmentListener$_CC {
   public static void notifyParent(Fragment var0, FragmentListener.TYPE var1, Object var2) {
      FragmentActivity var3 = var0.getActivity();
      if (var3 instanceof FragmentListener) {
         ((FragmentListener)var3).onNotified(var0, var1, var2);
      }

   }
}
