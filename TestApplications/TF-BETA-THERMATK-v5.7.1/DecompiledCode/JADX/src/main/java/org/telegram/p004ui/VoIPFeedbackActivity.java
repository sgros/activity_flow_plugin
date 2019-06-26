package org.telegram.p004ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import org.telegram.p004ui.Components.voip.VoIPHelper;

/* renamed from: org.telegram.ui.VoIPFeedbackActivity */
public class VoIPFeedbackActivity extends Activity {

    /* renamed from: org.telegram.ui.VoIPFeedbackActivity$1 */
    class C32831 implements Runnable {
        C32831() {
        }

        public void run() {
            VoIPFeedbackActivity.this.finish();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        getWindow().addFlags(524288);
        super.onCreate(bundle);
        overridePendingTransition(0, 0);
        setContentView(new View(this));
        VoIPHelper.showRateAlert(this, new C32831(), getIntent().getLongExtra("call_id", 0), getIntent().getLongExtra("call_access_hash", 0), getIntent().getIntExtra("account", 0), false);
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
