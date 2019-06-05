// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints.trackers;

import android.content.IntentFilter;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import androidx.work.Logger;

public class StorageNotLowTracker extends BroadcastReceiverConstraintTracker<Boolean>
{
    private static final String TAG;
    
    static {
        TAG = Logger.tagWithPrefix("StorageNotLowTracker");
    }
    
    public StorageNotLowTracker(final Context context) {
        super(context);
    }
    
    @Override
    public Boolean getInitialState() {
        final Intent registerReceiver = this.mAppContext.registerReceiver((BroadcastReceiver)null, this.getIntentFilter());
        if (registerReceiver == null || registerReceiver.getAction() == null) {
            return true;
        }
        final String action = registerReceiver.getAction();
        int n = -1;
        final int hashCode = action.hashCode();
        if (hashCode != -1181163412) {
            if (hashCode == -730838620) {
                if (action.equals("android.intent.action.DEVICE_STORAGE_OK")) {
                    n = 0;
                }
            }
        }
        else if (action.equals("android.intent.action.DEVICE_STORAGE_LOW")) {
            n = 1;
        }
        switch (n) {
            default: {
                return null;
            }
            case 1: {
                return false;
            }
            case 0: {
                return true;
            }
        }
    }
    
    @Override
    public IntentFilter getIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.DEVICE_STORAGE_OK");
        intentFilter.addAction("android.intent.action.DEVICE_STORAGE_LOW");
        return intentFilter;
    }
    
    @Override
    public void onBroadcastReceive(final Context context, final Intent intent) {
        if (intent.getAction() == null) {
            return;
        }
        Logger.get().debug(StorageNotLowTracker.TAG, String.format("Received %s", intent.getAction()), new Throwable[0]);
        final String action = intent.getAction();
        int n = -1;
        final int hashCode = action.hashCode();
        if (hashCode != -1181163412) {
            if (hashCode == -730838620) {
                if (action.equals("android.intent.action.DEVICE_STORAGE_OK")) {
                    n = 0;
                }
            }
        }
        else if (action.equals("android.intent.action.DEVICE_STORAGE_LOW")) {
            n = 1;
        }
        switch (n) {
            case 1: {
                this.setState(false);
                break;
            }
            case 0: {
                this.setState(true);
                break;
            }
        }
    }
}
