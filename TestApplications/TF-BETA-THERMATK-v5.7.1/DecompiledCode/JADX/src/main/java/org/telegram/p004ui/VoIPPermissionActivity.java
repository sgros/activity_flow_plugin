package org.telegram.p004ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.p004ui.Components.voip.VoIPHelper;

/* renamed from: org.telegram.ui.VoIPPermissionActivity */
public class VoIPPermissionActivity extends Activity {

    /* renamed from: org.telegram.ui.VoIPPermissionActivity$1 */
    class C32841 implements Runnable {
        C32841() {
        }

        public void run() {
            VoIPPermissionActivity.this.finish();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 101);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 101) {
            if (iArr.length > 0 && iArr[0] == 0) {
                if (VoIPService.getSharedInstance() != null) {
                    VoIPService.getSharedInstance().acceptIncomingCall();
                }
                finish();
                startActivity(new Intent(this, VoIPActivity.class));
            } else if (shouldShowRequestPermissionRationale("android.permission.RECORD_AUDIO")) {
                finish();
            } else {
                if (VoIPService.getSharedInstance() != null) {
                    VoIPService.getSharedInstance().declineIncomingCall();
                }
                VoIPHelper.permissionDenied(this, new C32841());
            }
        }
    }
}
