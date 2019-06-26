package org.telegram.messenger.voip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

public class VoIPMediaButtonReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      if ("android.intent.action.MEDIA_BUTTON".equals(var2.getAction())) {
         if (VoIPService.getSharedInstance() == null) {
            return;
         }

         KeyEvent var3 = (KeyEvent)var2.getParcelableExtra("android.intent.extra.KEY_EVENT");
         VoIPService.getSharedInstance().onMediaButtonEvent(var3);
      }

   }
}
