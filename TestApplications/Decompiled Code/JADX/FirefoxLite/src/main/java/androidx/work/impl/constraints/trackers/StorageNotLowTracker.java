package androidx.work.impl.constraints.trackers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.work.Logger;

public class StorageNotLowTracker extends BroadcastReceiverConstraintTracker<Boolean> {
    private static final String TAG = Logger.tagWithPrefix("StorageNotLowTracker");

    public StorageNotLowTracker(Context context) {
        super(context);
    }

    public Boolean getInitialState() {
        Intent registerReceiver = this.mAppContext.registerReceiver(null, getIntentFilter());
        if (registerReceiver == null || registerReceiver.getAction() == null) {
            return Boolean.valueOf(true);
        }
        String action = registerReceiver.getAction();
        Object obj = -1;
        int hashCode = action.hashCode();
        if (hashCode != -1181163412) {
            if (hashCode == -730838620 && action.equals("android.intent.action.DEVICE_STORAGE_OK")) {
                obj = null;
            }
        } else if (action.equals("android.intent.action.DEVICE_STORAGE_LOW")) {
            obj = 1;
        }
        switch (obj) {
            case null:
                return Boolean.valueOf(true);
            case 1:
                return Boolean.valueOf(false);
            default:
                return null;
        }
    }

    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.DEVICE_STORAGE_OK");
        intentFilter.addAction("android.intent.action.DEVICE_STORAGE_LOW");
        return intentFilter;
    }

    public void onBroadcastReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            Logger.get().debug(TAG, String.format("Received %s", new Object[]{intent.getAction()}), new Throwable[0]);
            String action = intent.getAction();
            Object obj = -1;
            int hashCode = action.hashCode();
            if (hashCode != -1181163412) {
                if (hashCode == -730838620 && action.equals("android.intent.action.DEVICE_STORAGE_OK")) {
                    obj = null;
                }
            } else if (action.equals("android.intent.action.DEVICE_STORAGE_LOW")) {
                obj = 1;
            }
            switch (obj) {
                case null:
                    setState(Boolean.valueOf(true));
                    break;
                case 1:
                    setState(Boolean.valueOf(false));
                    break;
            }
        }
    }
}
