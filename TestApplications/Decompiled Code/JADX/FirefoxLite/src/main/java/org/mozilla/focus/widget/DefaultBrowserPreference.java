package org.mozilla.focus.widget;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.preference.Preference;
import android.support.p001v4.app.NotificationManagerCompat;
import android.support.p001v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Switch;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.components.ComponentToggleService;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.Browsers;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.rocket.C0769R;

@TargetApi(24)
public class DefaultBrowserPreference extends Preference {
    private DefaultBrowserAction action;
    private Switch switchView;

    private interface DefaultBrowserAction {
        void onFragmentPause();

        void onFragmentResume();

        void onPrefClicked();
    }

    private static class ServiceReceiver extends BroadcastReceiver {
        DefaultBrowserPreference pref;

        ServiceReceiver(DefaultBrowserPreference defaultBrowserPreference) {
            this.pref = defaultBrowserPreference;
        }

        public void onReceive(Context context, Intent intent) {
            this.pref.update();
            NotificationManagerCompat.from(context).cancel(57083);
            boolean isDefaultBrowser = Browsers.isDefaultBrowser(context);
            boolean hasDefaultBrowser = Browsers.hasDefaultBrowser(context);
            if (hasDefaultBrowser && !isDefaultBrowser) {
                TelemetryWrapper.onDefaultBrowserServiceFailed();
            }
            if (!isDefaultBrowser && !hasDefaultBrowser) {
                this.pref.triggerWebOpen();
            }
        }
    }

    private static class DefaultAction implements DefaultBrowserAction {
        DefaultBrowserPreference pref;

        public void onFragmentPause() {
        }

        public void onFragmentResume() {
        }

        DefaultAction(DefaultBrowserPreference defaultBrowserPreference) {
            this.pref = defaultBrowserPreference;
        }

        public void onPrefClicked() {
            if (!IntentUtils.openDefaultAppsSettings(this.pref.getContext())) {
                this.pref.openSumoPage(this.pref.getContext());
            }
        }
    }

    private static class LowSdkAction implements DefaultBrowserAction {
        DefaultBrowserPreference pref;
        BroadcastReceiver receiver;

        LowSdkAction(DefaultBrowserPreference defaultBrowserPreference) {
            this.pref = defaultBrowserPreference;
            this.receiver = new ServiceReceiver(defaultBrowserPreference);
        }

        public void onPrefClicked() {
            Context context = this.pref.getContext();
            boolean isDefaultBrowser = Browsers.isDefaultBrowser(context);
            boolean hasDefaultBrowser = Browsers.hasDefaultBrowser(context);
            if (isDefaultBrowser) {
                this.pref.openAppDetailSettings(context);
            } else if (hasDefaultBrowser) {
                this.pref.setEnabled(false);
                this.pref.setSummary(C0769R.string.preference_default_browser_is_setting);
                this.pref.clearDefaultBrowser(context);
            } else {
                this.pref.triggerWebOpen();
            }
        }

        public void onFragmentResume() {
            LocalBroadcastManager.getInstance(this.pref.getContext()).registerReceiver(this.receiver, ComponentToggleService.SERVICE_STOP_INTENT_FILTER);
        }

        public void onFragmentPause() {
            if (VERSION.SDK_INT < 24) {
                LocalBroadcastManager.getInstance(this.pref.getContext()).unregisterReceiver(this.receiver);
            }
        }
    }

    public DefaultBrowserPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setWidgetLayoutResource(C0769R.layout.preference_default_browser);
        init();
    }

    public DefaultBrowserPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWidgetLayoutResource(C0769R.layout.preference_default_browser);
        init();
    }

    /* Access modifiers changed, original: protected */
    public void onBindView(View view) {
        super.onBindView(view);
        this.switchView = (Switch) view.findViewById(C0427R.C0426id.switch_widget);
        update();
    }

    public void update() {
        if (this.switchView != null) {
            boolean isDefaultBrowser = Browsers.isDefaultBrowser(getContext());
            this.switchView.setChecked(isDefaultBrowser);
            if (ComponentToggleService.isAlive(getContext())) {
                setEnabled(false);
                setSummary(C0769R.string.preference_default_browser_is_setting);
            } else {
                setEnabled(true);
                setSummary(null);
            }
            Settings.updatePrefDefaultBrowserIfNeeded(getContext(), isDefaultBrowser);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onClick() {
        this.action.onPrefClicked();
    }

    public void onFragmentResume() {
        update();
        this.action.onFragmentResume();
    }

    public void onFragmentPause() {
        this.action.onFragmentPause();
    }

    private void init() {
        if (this.action == null) {
            this.action = VERSION.SDK_INT >= 23 ? new DefaultAction(this) : new LowSdkAction(this);
        }
    }

    private void openAppDetailSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }

    private void clearDefaultBrowser(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context, ComponentToggleService.class));
        context.startService(intent);
    }

    private void openSumoPage(Context context) {
        context.startActivity(InfoActivity.getIntentFor(context, SupportUtils.getSumoURLForTopic(context, "rocket-default"), getTitle().toString()));
    }

    private void triggerWebOpen() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("http://mozilla.org"));
        intent.putExtra("_intent_to_resolve_browser_", true);
        getContext().startActivity(intent);
    }
}
