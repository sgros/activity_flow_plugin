package org.mozilla.focus.tabs.tabtray;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class TabTray {
   public static void dismiss(FragmentManager var0) {
      Fragment var1 = var0.findFragmentByTag("tab_tray");
      if (var1 != null) {
         ((DialogFragment)var1).dismissAllowingStateLoss();
      }

   }

   public static boolean isShowing(FragmentManager var0) {
      boolean var1;
      if (var0 != null && var0.findFragmentByTag("tab_tray") != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static void show(FragmentManager var0) {
      if (!var0.isStateSaved()) {
         TabTrayFragment.newInstance().show(var0, "tab_tray");
      }

   }
}
