package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import org.secuso.privacyfriendlynetmonitor.Assistant.PrefManager;

public class SplashActivity extends AppCompatActivity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      new PrefManager(this);
      Intent var2;
      if (PrefManager.isFirstTimeLaunch()) {
         var2 = new Intent(this, TutorialActivity.class);
         PrefManager.setFirstTimeLaunch(false);
      } else {
         var2 = new Intent(this, MainActivity.class);
         var2.addFlags(67108864);
      }

      this.startActivity(var2);
      this.finish();
   }
}
