// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.ui.Components.voip.VoIPHelper;
import android.content.Context;
import android.content.Intent;
import org.telegram.messenger.voip.VoIPService;
import android.os.Bundle;
import android.app.Activity;

public class VoIPPermissionActivity extends Activity
{
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.requestPermissions(new String[] { "android.permission.RECORD_AUDIO" }, 101);
    }
    
    public void onRequestPermissionsResult(final int n, final String[] array, final int[] array2) {
        if (n == 101) {
            if (array2.length > 0 && array2[0] == 0) {
                if (VoIPService.getSharedInstance() != null) {
                    VoIPService.getSharedInstance().acceptIncomingCall();
                }
                this.finish();
                this.startActivity(new Intent((Context)this, (Class)VoIPActivity.class));
            }
            else {
                if (!this.shouldShowRequestPermissionRationale("android.permission.RECORD_AUDIO")) {
                    if (VoIPService.getSharedInstance() != null) {
                        VoIPService.getSharedInstance().declineIncomingCall();
                    }
                    VoIPHelper.permissionDenied(this, new Runnable() {
                        @Override
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
