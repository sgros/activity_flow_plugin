// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.ui.Components.voip.VoIPHelper;
import android.content.Context;
import android.view.View;
import android.os.Bundle;
import android.app.Activity;

public class VoIPFeedbackActivity extends Activity
{
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }
    
    protected void onCreate(final Bundle bundle) {
        this.getWindow().addFlags(524288);
        super.onCreate(bundle);
        this.overridePendingTransition(0, 0);
        this.setContentView(new View((Context)this));
        VoIPHelper.showRateAlert((Context)this, new Runnable() {
            @Override
            public void run() {
                VoIPFeedbackActivity.this.finish();
            }
        }, this.getIntent().getLongExtra("call_id", 0L), this.getIntent().getLongExtra("call_access_hash", 0L), this.getIntent().getIntExtra("account", 0), false);
    }
}
