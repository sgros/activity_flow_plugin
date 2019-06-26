package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShareBroadcastReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      String var4 = var2.getDataString();
      if (var4 != null) {
         Intent var3 = new Intent("android.intent.action.SEND");
         var3.setType("text/plain");
         var3.putExtra("android.intent.extra.TEXT", var4);
         var2 = Intent.createChooser(var3, LocaleController.getString("ShareLink", 2131560749));
         var2.setFlags(268435456);
         var1.startActivity(var2);
      }

   }
}
