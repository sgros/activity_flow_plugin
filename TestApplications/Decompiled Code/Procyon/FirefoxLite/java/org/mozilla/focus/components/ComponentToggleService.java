// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.components;

import java.lang.ref.WeakReference;
import android.content.pm.PackageManager;
import android.content.ComponentName;
import org.mozilla.rocket.component.ConfigActivity;
import org.mozilla.focus.utils.Browsers;
import android.support.v4.content.LocalBroadcastManager;
import java.util.TimerTask;
import android.content.res.Configuration;
import android.os.IBinder;
import org.mozilla.focus.notification.NotificationUtil;
import android.support.v4.app.NotificationManagerCompat;
import java.util.Iterator;
import android.app.ActivityManager$RunningServiceInfo;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import org.mozilla.focus.activity.SettingsActivity;
import android.app.PendingIntent;
import java.util.Timer;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.app.Service;

public class ComponentToggleService extends Service
{
    public static final IntentFilter SERVICE_STOP_INTENT_FILTER;
    private static final IntentFilter sIntentFilter;
    private BroadcastReceiver mPackageStatusReceiver;
    private Timer timer;
    
    static {
        (SERVICE_STOP_INTENT_FILTER = new IntentFilter()).addAction("_component_service_stopped_");
        (sIntentFilter = new IntentFilter()).addAction("android.intent.action.PACKAGE_ADDED");
        ComponentToggleService.sIntentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        ComponentToggleService.sIntentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        ComponentToggleService.sIntentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        ComponentToggleService.sIntentFilter.addDataScheme("package");
    }
    
    public ComponentToggleService() {
        this.timer = new Timer();
    }
    
    private PendingIntent buildIntent() {
        return PendingIntent.getActivity(this.getApplicationContext(), 38183, new Intent(this.getApplicationContext(), (Class)SettingsActivity.class), 134217728);
    }
    
    public static boolean isAlive(final Context context) {
        final Iterator<ActivityManager$RunningServiceInfo> iterator = ((ActivityManager)context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE).iterator();
        while (iterator.hasNext()) {
            if (ComponentToggleService.class.getName().equals(iterator.next().service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    
    private void removeFromForeground() {
        NotificationManagerCompat.from(this.getApplicationContext()).notify(57083, NotificationUtil.importantBuilder(this.getApplicationContext()).setContentTitle(this.getString(2131755395)).setAutoCancel(true).setContentIntent(this.buildIntent()).build());
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
        this.stopSelf();
    }
    
    private void startToForeground() {
        this.startForeground(57082, NotificationUtil.importantBuilder(this.getApplicationContext()).setContentTitle(this.getString(2131755397)).setContentText(this.getString(2131755396)).setAutoCancel(false).setOngoing(true).setContentIntent(this.buildIntent()).build());
    }
    
    public IBinder onBind(final Intent intent) {
        return null;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }
    
    public void onCreate() {
        super.onCreate();
        this.mPackageStatusReceiver = new PackageStatusReceiver((Listener)new Listener() {
            @Override
            public void onPackageChanged(final Intent intent) {
                if (!ComponentToggleService.sIntentFilter.hasAction(intent.getAction())) {
                    return;
                }
                if (ComponentToggleService.this.getPackageName().equals(intent.getData().getEncodedSchemeSpecificPart())) {
                    ComponentToggleService.this.startService(new Intent(ComponentToggleService.this.getApplicationContext(), (Class)ComponentToggleService.class));
                }
            }
        });
        this.timer.schedule(new BombTask(this), 30000L);
        this.registerReceiver(this.mPackageStatusReceiver, ComponentToggleService.sIntentFilter);
    }
    
    public void onDestroy() {
        this.unregisterReceiver(this.mPackageStatusReceiver);
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
        LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcast(new Intent("_component_service_stopped_"));
        this.stopForeground(true);
        super.onDestroy();
    }
    
    public int onStartCommand(final Intent intent, int n, int n2) {
        super.onStartCommand(intent, n, n2);
        final boolean hasDefaultBrowser = Browsers.hasDefaultBrowser(this.getApplicationContext());
        final boolean defaultBrowser = Browsers.isDefaultBrowser(this.getApplicationContext());
        final int n3 = 0;
        if (!defaultBrowser && !hasDefaultBrowser) {
            n = 1;
        }
        else {
            n = 0;
        }
        final PackageManager packageManager = this.getPackageManager();
        final ComponentName componentName = new ComponentName(this.getApplicationContext(), (Class)ConfigActivity.class);
        if (packageManager.getComponentEnabledSetting(componentName) == 1) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        final boolean b = n2 == 0 && hasDefaultBrowser && !defaultBrowser;
        Label_0120: {
            if (n == 0) {
                n = n3;
                if (n2 == 0) {
                    break Label_0120;
                }
            }
            n = 1;
        }
        if (b) {
            packageManager.setComponentEnabledSetting(componentName, 1, 1);
            this.startToForeground();
        }
        else if (n != 0) {
            packageManager.setComponentEnabledSetting(componentName, 2, 1);
            this.removeFromForeground();
        }
        return 1;
    }
    
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }
    
    private static final class BombTask extends TimerTask
    {
        final WeakReference<ComponentToggleService> service;
        
        BombTask(final ComponentToggleService referent) {
            this.service = new WeakReference<ComponentToggleService>(referent);
        }
        
        @Override
        public void run() {
            final ComponentToggleService componentToggleService = this.service.get();
            if (componentToggleService != null) {
                componentToggleService.removeFromForeground();
            }
        }
    }
    
    private static final class PackageStatusReceiver extends BroadcastReceiver
    {
        private final Listener mListener;
        
        PackageStatusReceiver(final Listener mListener) {
            this.mListener = mListener;
        }
        
        public void onReceive(final Context context, final Intent intent) {
            if (intent != null && intent.getData() != null) {
                this.mListener.onPackageChanged(intent);
            }
        }
        
        interface Listener
        {
            void onPackageChanged(final Intent p0);
        }
    }
}
