package org.mozilla.focus.components;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.p001v4.app.NotificationManagerCompat;
import android.support.p001v4.content.LocalBroadcastManager;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
import org.mozilla.focus.activity.SettingsActivity;
import org.mozilla.focus.notification.NotificationUtil;
import org.mozilla.focus.utils.Browsers;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.component.ConfigActivity;

public class ComponentToggleService extends Service {
    public static final IntentFilter SERVICE_STOP_INTENT_FILTER = new IntentFilter();
    private static final IntentFilter sIntentFilter = new IntentFilter();
    private BroadcastReceiver mPackageStatusReceiver;
    private Timer timer = new Timer();

    private static final class BombTask extends TimerTask {
        final WeakReference<ComponentToggleService> service;

        BombTask(ComponentToggleService componentToggleService) {
            this.service = new WeakReference(componentToggleService);
        }

        public void run() {
            ComponentToggleService componentToggleService = (ComponentToggleService) this.service.get();
            if (componentToggleService != null) {
                componentToggleService.removeFromForeground();
            }
        }
    }

    private static final class PackageStatusReceiver extends BroadcastReceiver {
        private final Listener mListener;

        interface Listener {
            void onPackageChanged(Intent intent);
        }

        PackageStatusReceiver(Listener listener) {
            this.mListener = listener;
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getData() != null) {
                this.mListener.onPackageChanged(intent);
            }
        }
    }

    /* renamed from: org.mozilla.focus.components.ComponentToggleService$1 */
    class C04431 implements Listener {
        C04431() {
        }

        public void onPackageChanged(Intent intent) {
            if (ComponentToggleService.sIntentFilter.hasAction(intent.getAction())) {
                if (ComponentToggleService.this.getPackageName().equals(intent.getData().getEncodedSchemeSpecificPart())) {
                    ComponentToggleService.this.startService(new Intent(ComponentToggleService.this.getApplicationContext(), ComponentToggleService.class));
                }
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    static {
        SERVICE_STOP_INTENT_FILTER.addAction("_component_service_stopped_");
        sIntentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        sIntentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        sIntentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        sIntentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        sIntentFilter.addDataScheme("package");
    }

    public void onCreate() {
        super.onCreate();
        this.mPackageStatusReceiver = new PackageStatusReceiver(new C04431());
        this.timer.schedule(new BombTask(this), 30000);
        registerReceiver(this.mPackageStatusReceiver, sIntentFilter);
    }

    public void onDestroy() {
        unregisterReceiver(this.mPackageStatusReceiver);
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("_component_service_stopped_"));
        stopForeground(true);
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        boolean hasDefaultBrowser = Browsers.hasDefaultBrowser(getApplicationContext());
        boolean isDefaultBrowser = Browsers.isDefaultBrowser(getApplicationContext());
        Object obj = null;
        Object obj2 = (isDefaultBrowser || hasDefaultBrowser) ? null : 1;
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(getApplicationContext(), ConfigActivity.class);
        Object obj3 = packageManager.getComponentEnabledSetting(componentName) == 1 ? 1 : null;
        Object obj4 = (obj3 == null && hasDefaultBrowser && !isDefaultBrowser) ? 1 : null;
        if (!(obj2 == null && obj3 == null)) {
            obj = 1;
        }
        if (obj4 != null) {
            packageManager.setComponentEnabledSetting(componentName, 1, 1);
            startToForeground();
        } else if (obj != null) {
            packageManager.setComponentEnabledSetting(componentName, 2, 1);
            removeFromForeground();
        }
        return 1;
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public static boolean isAlive(Context context) {
        for (RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (ComponentToggleService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void startToForeground() {
        startForeground(57082, NotificationUtil.importantBuilder(getApplicationContext()).setContentTitle(getString(C0769R.string.setting_default_browser_notification_title)).setContentText(getString(C0769R.string.setting_default_browser_notification_text)).setAutoCancel(false).setOngoing(true).setContentIntent(buildIntent()).build());
    }

    private void removeFromForeground() {
        NotificationManagerCompat.from(getApplicationContext()).notify(57083, NotificationUtil.importantBuilder(getApplicationContext()).setContentTitle(getString(C0769R.string.setting_default_browser_notification_clickable_text)).setAutoCancel(true).setContentIntent(buildIntent()).build());
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
        stopSelf();
    }

    private PendingIntent buildIntent() {
        return PendingIntent.getActivity(getApplicationContext(), 38183, new Intent(getApplicationContext(), SettingsActivity.class), 134217728);
    }
}
