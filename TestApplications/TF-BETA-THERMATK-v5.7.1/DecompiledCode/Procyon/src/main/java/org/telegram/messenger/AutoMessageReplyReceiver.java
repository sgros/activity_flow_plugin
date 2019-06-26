// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.os.Bundle;
import java.util.HashMap;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;
import androidx.core.app.RemoteInput;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class AutoMessageReplyReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        ApplicationLoader.postInitApplication();
        final Bundle resultsFromIntent = RemoteInput.getResultsFromIntent(intent);
        if (resultsFromIntent == null) {
            return;
        }
        final CharSequence charSequence = resultsFromIntent.getCharSequence("extra_voice_reply");
        if (charSequence != null) {
            if (charSequence.length() != 0) {
                final long longExtra = intent.getLongExtra("dialog_id", 0L);
                final int intExtra = intent.getIntExtra("max_id", 0);
                final int intExtra2 = intent.getIntExtra("currentAccount", 0);
                if (longExtra != 0L) {
                    if (intExtra != 0) {
                        SendMessagesHelper.getInstance(intExtra2).sendMessage(charSequence.toString(), longExtra, null, null, true, null, null, null);
                        MessagesController.getInstance(intExtra2).markDialogAsRead(longExtra, intExtra, intExtra, 0, false, 0, true);
                    }
                }
            }
        }
    }
}
