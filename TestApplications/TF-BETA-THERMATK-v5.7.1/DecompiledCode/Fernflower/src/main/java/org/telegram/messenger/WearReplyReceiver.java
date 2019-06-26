package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.RemoteInput;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.tgnet.TLRPC;

public class WearReplyReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      ApplicationLoader.postInitApplication();
      Bundle var7 = RemoteInput.getResultsFromIntent(var2);
      if (var7 != null) {
         CharSequence var8 = var7.getCharSequence("extra_voice_reply");
         if (var8 != null && var8.length() != 0) {
            long var3 = var2.getLongExtra("dialog_id", 0L);
            int var5 = var2.getIntExtra("max_id", 0);
            int var6 = var2.getIntExtra("currentAccount", 0);
            if (var3 != 0L && var5 != 0) {
               SendMessagesHelper.getInstance(var6).sendMessage(var8.toString(), var3, (MessageObject)null, (TLRPC.WebPage)null, true, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
               MessagesController.getInstance(var6).markDialogAsRead(var3, var5, var5, 0, false, 0, true);
            }
         }

      }
   }
}
