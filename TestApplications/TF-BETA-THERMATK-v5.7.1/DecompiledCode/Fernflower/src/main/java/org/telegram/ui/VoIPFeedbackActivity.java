package org.telegram.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import org.telegram.ui.Components.voip.VoIPHelper;

public class VoIPFeedbackActivity extends Activity {
   public void finish() {
      super.finish();
      this.overridePendingTransition(0, 0);
   }

   protected void onCreate(Bundle var1) {
      this.getWindow().addFlags(524288);
      super.onCreate(var1);
      this.overridePendingTransition(0, 0);
      this.setContentView(new View(this));
      VoIPHelper.showRateAlert(this, new Runnable() {
         public void run() {
            VoIPFeedbackActivity.this.finish();
         }
      }, this.getIntent().getLongExtra("call_id", 0L), this.getIntent().getLongExtra("call_access_hash", 0L), this.getIntent().getIntExtra("account", 0), false);
   }
}
