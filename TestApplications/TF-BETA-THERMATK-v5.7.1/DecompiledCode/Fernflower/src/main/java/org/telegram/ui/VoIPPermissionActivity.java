package org.telegram.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.ui.Components.voip.VoIPHelper;

public class VoIPPermissionActivity extends Activity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 101);
   }

   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
      if (var1 == 101) {
         if (var3.length > 0 && var3[0] == 0) {
            if (VoIPService.getSharedInstance() != null) {
               VoIPService.getSharedInstance().acceptIncomingCall();
            }

            this.finish();
            this.startActivity(new Intent(this, VoIPActivity.class));
         } else {
            if (!this.shouldShowRequestPermissionRationale("android.permission.RECORD_AUDIO")) {
               if (VoIPService.getSharedInstance() != null) {
                  VoIPService.getSharedInstance().declineIncomingCall();
               }

               VoIPHelper.permissionDenied(this, new Runnable() {
                  public void run() {
                     VoIPPermissionActivity.this.finish();
                  }
               });
               return;
            }

            this.finish();
         }
      }

   }
}
