// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis;

import java.util.Iterator;
import android.app.ActivityManager$RunningServiceInfo;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;

public class ServiceHandler
{
    private boolean mIsBound;
    private PassiveService mPassiveService;
    private ServiceConnection mPassiveServiceConnection;
    
    public ServiceHandler() {
        this.mPassiveServiceConnection = (ServiceConnection)new ServiceConnection() {
            public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                ServiceHandler.this.mPassiveService = ((PassiveService.AnalyzerBinder)binder).getService();
                Toast.makeText(RunStore.getContext(), 2131624036, 0).show();
            }
            
            public void onServiceDisconnected(final ComponentName componentName) {
                ServiceHandler.this.mPassiveService = null;
                Toast.makeText(RunStore.getContext(), 2131624037, 0).show();
            }
        };
    }
    
    public void bindPassiveService(final Context context) {
        context.bindService(new Intent(RunStore.getAppContext(), (Class)PassiveService.class), this.mPassiveServiceConnection, 1);
        this.mIsBound = true;
    }
    
    public boolean isServiceRunning(final Class<?> clazz) {
        final Iterator<ActivityManager$RunningServiceInfo> iterator = ((ActivityManager)RunStore.getAppContext().getSystemService("activity")).getRunningServices(Integer.MAX_VALUE).iterator();
        while (iterator.hasNext()) {
            if (clazz.getName().equals(iterator.next().service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    
    public void startPassiveService() {
        RunStore.getContext().startService(new Intent(RunStore.getAppContext(), (Class)PassiveService.class));
    }
    
    public void stopPassiveService() {
        if (this.isServiceRunning(PassiveService.class)) {
            RunStore.getContext().stopService(new Intent(RunStore.getAppContext(), (Class)PassiveService.class));
        }
    }
    
    public void unbindPassiveService(final Context context) {
        if (this.mIsBound) {
            context.unbindService(this.mPassiveServiceConnection);
            this.mIsBound = false;
        }
    }
}
