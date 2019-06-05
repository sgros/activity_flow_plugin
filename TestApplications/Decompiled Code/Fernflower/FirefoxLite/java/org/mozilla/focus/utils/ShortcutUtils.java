package org.mozilla.focus.utils;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v4.graphics.drawable.IconCompat;
import android.text.TextUtils;
import java.util.ArrayList;
import org.mozilla.icon.FavIconUtils;

public class ShortcutUtils {
   public static void requestPinShortcut(Context var0, Intent var1, String var2, String var3, Bitmap var4) {
      Resources var5 = var0.getResources();
      char var6 = FavIconUtils.getRepresentativeCharacter(var3);
      if (var4 == null) {
         var4 = DimenUtils.getInitialBitmap(var5, (Bitmap)null, var6);
      } else {
         var4 = DimenUtils.getRefinedShortcutIcon(var5, var4, var6);
      }

      String var10 = var2;
      if (TextUtils.isEmpty(var2)) {
         var10 = var3;
      }

      ShortcutInfoCompat var7 = (new ShortcutInfoCompat.Builder(var0, var3)).setShortLabel(var10).setIcon(IconCompat.createWithBitmap(var4)).setIntent(var1).build();
      Intent var8 = new Intent("android.intent.action.MAIN");
      var8.addCategory("android.intent.category.HOME");
      var8.setFlags(268435456);
      IntentSender var9 = PendingIntent.getActivity(var0, 0, var8, 134217728).getIntentSender();
      updateShortcut26(var0, var7);
      if (ShortcutManagerCompat.isRequestPinShortcutSupported(var0)) {
         ShortcutManagerCompat.requestPinShortcut(var0, var7, var9);
      }

   }

   @TargetApi(26)
   private static void updateShortcut26(Context var0, ShortcutInfoCompat var1) {
      if (VERSION.SDK_INT >= 26) {
         ShortcutManager var2 = (ShortcutManager)var0.getSystemService(ShortcutManager.class);
         if (var2 != null) {
            ArrayList var3 = new ArrayList();
            var3.add(var1.toShortcutInfo());
            var2.updateShortcuts(var3);
         }
      }

   }
}
