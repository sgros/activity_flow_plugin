package org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.C0501R;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.PassiveService.AnalyzerBinder;

public class ServiceHandler {
    private boolean mIsBound;
    private PassiveService mPassiveService;
    private ServiceConnection mPassiveServiceConnection = new C04971();

    /* renamed from: org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.ServiceHandler$1 */
    class C04971 implements ServiceConnection {
        C04971() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ServiceHandler.this.mPassiveService = ((AnalyzerBinder) iBinder).getService();
            Toast.makeText(RunStore.getContext(), C0501R.string.passive_service_start, 0).show();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            ServiceHandler.this.mPassiveService = null;
            Toast.makeText(RunStore.getContext(), C0501R.string.passive_service_stop, 0).show();
        }
    }

    public boolean isServiceRunning(Class<?> cls) {
        for (RunningServiceInfo runningServiceInfo : ((ActivityManager) RunStore.getAppContext().getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void startPassiveService() {
        RunStore.getContext().startService(new Intent(RunStore.getAppContext(), PassiveService.class));
    }

    public void stopPassiveService() {
        if (isServiceRunning(PassiveService.class)) {
            RunStore.getContext().stopService(new Intent(RunStore.getAppContext(), PassiveService.class));
        }
    }

    public void bindPassiveService(Context context) {
        context.bindService(new Intent(RunStore.getAppContext(), PassiveService.class), this.mPassiveServiceConnection, 1);
        this.mIsBound = true;
    }

    public void unbindPassiveService(Context context) {
        if (this.mIsBound) {
            context.unbindService(this.mPassiveServiceConnection);
            this.mIsBound = false;
        }
    }
}
