// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.widget.Toast;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class CustomTabsCopyReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        final String dataString = intent.getDataString();
        if (dataString != null) {
            AndroidUtilities.addToClipboard(dataString);
            Toast.makeText(context, (CharSequence)LocaleController.getString("LinkCopied", 2131559751), 0).show();
        }
    }
}
