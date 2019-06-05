package org.mozilla.focus;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import org.mozilla.rocket.privately.PrivateModeActivity;

/* compiled from: FocusApplication.kt */
public final class FocusApplication$monitorPrivateProcess$1 implements ActivityLifecycleCallbacks {
    final /* synthetic */ FocusApplication this$0;

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    FocusApplication$monitorPrivateProcess$1(FocusApplication focusApplication) {
        this.this$0 = focusApplication;
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (activity instanceof PrivateModeActivity) {
            this.this$0.setInPrivateProcess(true);
        }
    }
}
