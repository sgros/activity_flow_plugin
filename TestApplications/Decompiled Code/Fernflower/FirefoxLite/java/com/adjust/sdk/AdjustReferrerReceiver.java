package com.adjust.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class AdjustReferrerReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      String var4 = var2.getStringExtra("referrer");
      if (var4 != null) {
         try {
            var4 = URLDecoder.decode(var4, "UTF-8");
         } catch (UnsupportedEncodingException var3) {
            var4 = "malformed";
         }

         Adjust.getDefaultInstance().sendReferrer(var4);
      }
   }
}
