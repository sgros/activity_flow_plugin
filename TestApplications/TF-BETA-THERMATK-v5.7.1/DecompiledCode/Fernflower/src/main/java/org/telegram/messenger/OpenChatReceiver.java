package org.telegram.messenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.telegram.ui.LaunchActivity;

public class OpenChatReceiver extends Activity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      Intent var7 = this.getIntent();
      if (var7 == null) {
         this.finish();
      }

      if (var7.getAction() != null && var7.getAction().startsWith("com.tmessages.openchat")) {
         int var2;
         int var3;
         int var4;
         try {
            var2 = var7.getIntExtra("chatId", 0);
            var3 = var7.getIntExtra("userId", 0);
            var4 = var7.getIntExtra("encId", 0);
         } catch (Throwable var6) {
            return;
         }

         if (var2 != 0 || var3 != 0 || var4 != 0) {
            Intent var5 = new Intent(this, LaunchActivity.class);
            var5.setAction(var7.getAction());
            var5.putExtras(var7);
            this.startActivity(var5);
            this.finish();
         }
      } else {
         this.finish();
      }
   }
}
