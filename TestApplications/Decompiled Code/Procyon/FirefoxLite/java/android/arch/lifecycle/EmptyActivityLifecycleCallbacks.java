// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.lifecycle;

import android.os.Bundle;
import android.app.Activity;
import android.app.Application$ActivityLifecycleCallbacks;

class EmptyActivityLifecycleCallbacks implements Application$ActivityLifecycleCallbacks
{
    public void onActivityCreated(final Activity activity, final Bundle bundle) {
    }
    
    public void onActivityDestroyed(final Activity activity) {
    }
    
    public void onActivityPaused(final Activity activity) {
    }
    
    public void onActivityResumed(final Activity activity) {
    }
    
    public void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
    }
    
    public void onActivityStarted(final Activity activity) {
    }
    
    public void onActivityStopped(final Activity activity) {
    }
}
