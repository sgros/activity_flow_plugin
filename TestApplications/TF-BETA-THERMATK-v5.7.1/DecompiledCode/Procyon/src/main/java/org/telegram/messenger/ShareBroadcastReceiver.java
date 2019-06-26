// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class ShareBroadcastReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, Intent chooser) {
        final String dataString = chooser.getDataString();
        if (dataString != null) {
            final Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", dataString);
            chooser = Intent.createChooser(intent, (CharSequence)LocaleController.getString("ShareLink", 2131560749));
            chooser.setFlags(268435456);
            context.startActivity(chooser);
        }
    }
}
