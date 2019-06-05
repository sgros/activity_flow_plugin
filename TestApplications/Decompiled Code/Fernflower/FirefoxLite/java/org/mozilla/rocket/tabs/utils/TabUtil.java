package org.mozilla.rocket.tabs.utils;

import android.os.Bundle;
import android.text.TextUtils;

public final class TabUtil {
   public static Bundle argument(String var0, boolean var1, boolean var2) {
      Bundle var3 = new Bundle();
      if (!TextUtils.isEmpty(var0)) {
         var3.putString("_tab_parent_", var0);
      }

      var3.putBoolean("_tab_external_", var1);
      var3.putBoolean("_tab_focus_", var2);
      return var3;
   }

   public static String getParentId(Bundle var0) {
      return var0.getString("_tab_parent_");
   }

   public static boolean isFromExternal(Bundle var0) {
      return var0.getBoolean("_tab_external_", false);
   }

   public static boolean toFocus(Bundle var0) {
      return var0.getBoolean("_tab_focus_", false);
   }
}
