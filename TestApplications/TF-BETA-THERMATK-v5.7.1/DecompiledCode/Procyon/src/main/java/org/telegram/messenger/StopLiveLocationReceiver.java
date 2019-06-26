// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class StopLiveLocationReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        for (int i = 0; i < 3; ++i) {
            LocationController.getInstance(i).removeAllLocationSharings();
        }
    }
}
