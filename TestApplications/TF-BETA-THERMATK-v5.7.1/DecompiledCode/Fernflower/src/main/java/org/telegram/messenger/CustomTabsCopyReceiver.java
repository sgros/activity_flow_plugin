package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomTabsCopyReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      String var3 = var2.getDataString();
      if (var3 != null) {
         AndroidUtilities.addToClipboard(var3);
         Toast.makeText(var1, LocaleController.getString("LinkCopied", 2131559751), 0).show();
      }

   }
}
