// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.Context;
import android.content.Intent;
import org.telegram.ui.LaunchActivity;
import android.os.Bundle;
import android.app.Activity;

public class OpenChatReceiver extends Activity
{
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final Intent intent = this.getIntent();
        if (intent == null) {
            this.finish();
        }
        Label_0119: {
            if (intent.getAction() == null || !intent.getAction().startsWith("com.tmessages.openchat")) {
                break Label_0119;
            }
            try {
                final int intExtra = intent.getIntExtra("chatId", 0);
                final int intExtra2 = intent.getIntExtra("userId", 0);
                final int intExtra3 = intent.getIntExtra("encId", 0);
                if (intExtra == 0 && intExtra2 == 0 && intExtra3 == 0) {
                    return;
                }
                final Intent intent2 = new Intent((Context)this, (Class)LaunchActivity.class);
                intent2.setAction(intent.getAction());
                intent2.putExtras(intent);
                this.startActivity(intent2);
                this.finish();
                return;
                this.finish();
            }
            catch (Throwable t) {}
        }
    }
}
