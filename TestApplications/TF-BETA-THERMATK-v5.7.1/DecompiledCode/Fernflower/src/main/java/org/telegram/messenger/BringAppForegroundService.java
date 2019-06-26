package org.telegram.messenger;

import android.app.IntentService;
import android.content.Intent;
import org.telegram.ui.LaunchActivity;

public class BringAppForegroundService extends IntentService {
   public BringAppForegroundService() {
      super("BringAppForegroundService");
   }

   protected void onHandleIntent(Intent var1) {
      var1 = new Intent(this, LaunchActivity.class);
      var1.setFlags(268435456);
      this.startActivity(var1);
   }
}
