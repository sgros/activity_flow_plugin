package org.mozilla.rocket.component;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import org.mozilla.focus.activity.SettingsActivity;
import org.mozilla.focus.components.ComponentToggleService;

public class ConfigActivity extends AppCompatActivity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.startService(new Intent(this.getApplicationContext(), ComponentToggleService.class));
      Intent var2 = new Intent(this.getApplicationContext(), SettingsActivity.class);
      var2.setFlags(268435456);
      this.startActivity(var2);
      this.finishAndRemoveTask();
   }
}
