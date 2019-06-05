// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.lifecycle;

import android.os.IBinder;
import android.content.Intent;
import android.app.Service;

public class LifecycleService extends Service implements LifecycleOwner
{
    private final ServiceLifecycleDispatcher mDispatcher;
    
    public LifecycleService() {
        this.mDispatcher = new ServiceLifecycleDispatcher(this);
    }
    
    public Lifecycle getLifecycle() {
        return this.mDispatcher.getLifecycle();
    }
    
    public IBinder onBind(final Intent intent) {
        this.mDispatcher.onServicePreSuperOnBind();
        return null;
    }
    
    public void onCreate() {
        this.mDispatcher.onServicePreSuperOnCreate();
        super.onCreate();
    }
    
    public void onDestroy() {
        this.mDispatcher.onServicePreSuperOnDestroy();
        super.onDestroy();
    }
    
    public void onStart(final Intent intent, final int n) {
        this.mDispatcher.onServicePreSuperOnStart();
        super.onStart(intent, n);
    }
    
    public int onStartCommand(final Intent intent, final int n, final int n2) {
        return super.onStartCommand(intent, n, n2);
    }
}
