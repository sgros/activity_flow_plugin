// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.component;

import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.focus.activity.MainActivity;
import android.net.Uri;
import org.mozilla.focus.activity.SettingsActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.content.Intent;
import android.content.Context;
import kotlin.jvm.internal.Intrinsics;

public final class LaunchIntentDispatcher
{
    public static final Companion Companion;
    
    static {
        Companion = new Companion(null);
    }
    
    public enum Action
    {
        HANDLED, 
        NORMAL;
    }
    
    public enum Command
    {
        SET_DEFAULT("SET_DEFAULT");
        
        private final String value;
        
        protected Command(final String value) {
            Intrinsics.checkParameterIsNotNull(value, "value");
            super(name, ordinal);
            this.value = value;
        }
        
        public final String getValue() {
            return this.value;
        }
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        public final Action dispatch(final Context context, final Intent intent) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(intent, "intent");
            if (intent.getBooleanExtra(LaunchMethod.EXTRA_BOOL_HOME_SCREEN_SHORTCUT.getValue(), false)) {
                TelemetryWrapper.launchByHomeScreenShortcutEvent();
                return Action.NORMAL;
            }
            if (intent.getBooleanExtra(LaunchMethod.EXTRA_BOOL_TEXT_SELECTION.getValue(), false)) {
                TelemetryWrapper.launchByExternalAppEvent("text_selection");
                return Action.NORMAL;
            }
            if (intent.getBooleanExtra(LaunchMethod.EXTRA_BOOL_WEB_SEARCH.getValue(), false)) {
                TelemetryWrapper.launchByExternalAppEvent("web_search");
                return Action.NORMAL;
            }
            if (intent.getBooleanExtra("_intent_to_resolve_browser_", false)) {
                context.startActivity(new Intent(context, (Class)SettingsActivity.class));
                return Action.HANDLED;
            }
            final String stringExtra = intent.getStringExtra("push_open_url");
            if (stringExtra != null) {
                intent.setData(Uri.parse(stringExtra));
                intent.setAction("android.intent.action.VIEW");
                intent.setClass(context, (Class)MainActivity.class);
                intent.putExtra("open_new_tab", true);
            }
            final String stringExtra2 = intent.getStringExtra("push_command");
            if (stringExtra2 != null && Intrinsics.areEqual(stringExtra2, Command.SET_DEFAULT.getValue())) {
                if (!IntentUtils.openDefaultAppsSettings(context)) {
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse(SupportUtils.getSumoURLForTopic(context, "rocket-default")));
                    return Action.NORMAL;
                }
                return Action.HANDLED;
            }
            else {
                if (Intrinsics.areEqual("android.intent.action.MAIN", intent.getAction())) {
                    TelemetryWrapper.launchByAppLauncherEvent();
                    return Action.NORMAL;
                }
                if (Intrinsics.areEqual("android.intent.action.VIEW", intent.getAction())) {
                    TelemetryWrapper.launchByExternalAppEvent(null);
                    return Action.NORMAL;
                }
                return Action.NORMAL;
            }
        }
    }
    
    public enum LaunchMethod
    {
        EXTRA_BOOL_HOME_SCREEN_SHORTCUT("shortcut"), 
        EXTRA_BOOL_TEXT_SELECTION("text_selection"), 
        EXTRA_BOOL_WEB_SEARCH("web_search");
        
        private final String value;
        
        protected LaunchMethod(final String value) {
            Intrinsics.checkParameterIsNotNull(value, "value");
            super(name, ordinal);
            this.value = value;
        }
        
        public final String getValue() {
            return this.value;
        }
    }
}
