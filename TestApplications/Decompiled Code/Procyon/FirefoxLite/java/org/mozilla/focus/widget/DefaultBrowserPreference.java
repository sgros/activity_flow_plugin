// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.Browsers;
import android.view.View;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.utils.SupportUtils;
import android.net.Uri;
import android.os.Build$VERSION;
import android.content.ComponentName;
import org.mozilla.focus.components.ComponentToggleService;
import android.content.Intent;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.Switch;
import android.annotation.TargetApi;
import android.preference.Preference;

@TargetApi(24)
public class DefaultBrowserPreference extends Preference
{
    private DefaultBrowserAction action;
    private Switch switchView;
    
    public DefaultBrowserPreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.setWidgetLayoutResource(2131493011);
        this.init();
    }
    
    public DefaultBrowserPreference(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.setWidgetLayoutResource(2131493011);
        this.init();
    }
    
    private void clearDefaultBrowser(final Context context) {
        final Intent intent = new Intent();
        intent.setComponent(new ComponentName(context, (Class)ComponentToggleService.class));
        context.startService(intent);
    }
    
    private void init() {
        if (this.action == null) {
            DefaultBrowserAction action;
            if (Build$VERSION.SDK_INT >= 23) {
                action = new DefaultAction(this);
            }
            else {
                action = new LowSdkAction(this);
            }
            this.action = action;
        }
    }
    
    private void openAppDetailSettings(final Context context) {
        final Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", context.getPackageName(), (String)null));
        context.startActivity(intent);
    }
    
    private void openSumoPage(final Context context) {
        context.startActivity(InfoActivity.getIntentFor(context, SupportUtils.getSumoURLForTopic(context, "rocket-default"), this.getTitle().toString()));
    }
    
    private void triggerWebOpen() {
        final Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("http://mozilla.org"));
        intent.putExtra("_intent_to_resolve_browser_", true);
        this.getContext().startActivity(intent);
    }
    
    protected void onBindView(final View view) {
        super.onBindView(view);
        this.switchView = (Switch)view.findViewById(2131296677);
        this.update();
    }
    
    protected void onClick() {
        this.action.onPrefClicked();
    }
    
    public void onFragmentPause() {
        this.action.onFragmentPause();
    }
    
    public void onFragmentResume() {
        this.update();
        this.action.onFragmentResume();
    }
    
    public void update() {
        if (this.switchView != null) {
            final boolean defaultBrowser = Browsers.isDefaultBrowser(this.getContext());
            this.switchView.setChecked(defaultBrowser);
            if (ComponentToggleService.isAlive(this.getContext())) {
                this.setEnabled(false);
                this.setSummary(2131755344);
            }
            else {
                this.setEnabled(true);
                this.setSummary((CharSequence)null);
            }
            Settings.updatePrefDefaultBrowserIfNeeded(this.getContext(), defaultBrowser);
        }
    }
    
    private static class DefaultAction implements DefaultBrowserAction
    {
        DefaultBrowserPreference pref;
        
        DefaultAction(final DefaultBrowserPreference pref) {
            this.pref = pref;
        }
        
        @Override
        public void onFragmentPause() {
        }
        
        @Override
        public void onFragmentResume() {
        }
        
        @Override
        public void onPrefClicked() {
            if (!IntentUtils.openDefaultAppsSettings(this.pref.getContext())) {
                this.pref.openSumoPage(this.pref.getContext());
            }
        }
    }
    
    private interface DefaultBrowserAction
    {
        void onFragmentPause();
        
        void onFragmentResume();
        
        void onPrefClicked();
    }
    
    private static class LowSdkAction implements DefaultBrowserAction
    {
        DefaultBrowserPreference pref;
        BroadcastReceiver receiver;
        
        LowSdkAction(final DefaultBrowserPreference pref) {
            this.pref = pref;
            this.receiver = new ServiceReceiver(pref);
        }
        
        @Override
        public void onFragmentPause() {
            if (Build$VERSION.SDK_INT < 24) {
                LocalBroadcastManager.getInstance(this.pref.getContext()).unregisterReceiver(this.receiver);
            }
        }
        
        @Override
        public void onFragmentResume() {
            LocalBroadcastManager.getInstance(this.pref.getContext()).registerReceiver(this.receiver, ComponentToggleService.SERVICE_STOP_INTENT_FILTER);
        }
        
        @Override
        public void onPrefClicked() {
            final Context context = this.pref.getContext();
            final boolean defaultBrowser = Browsers.isDefaultBrowser(context);
            final boolean hasDefaultBrowser = Browsers.hasDefaultBrowser(context);
            if (defaultBrowser) {
                this.pref.openAppDetailSettings(context);
            }
            else if (hasDefaultBrowser) {
                this.pref.setEnabled(false);
                this.pref.setSummary(2131755344);
                this.pref.clearDefaultBrowser(context);
            }
            else {
                this.pref.triggerWebOpen();
            }
        }
    }
    
    private static class ServiceReceiver extends BroadcastReceiver
    {
        DefaultBrowserPreference pref;
        
        ServiceReceiver(final DefaultBrowserPreference pref) {
            this.pref = pref;
        }
        
        public void onReceive(final Context context, final Intent intent) {
            this.pref.update();
            NotificationManagerCompat.from(context).cancel(57083);
            final boolean defaultBrowser = Browsers.isDefaultBrowser(context);
            final boolean hasDefaultBrowser = Browsers.hasDefaultBrowser(context);
            if (hasDefaultBrowser && !defaultBrowser) {
                TelemetryWrapper.onDefaultBrowserServiceFailed();
            }
            if (!defaultBrowser && !hasDefaultBrowser) {
                this.pref.triggerWebOpen();
            }
        }
    }
}
