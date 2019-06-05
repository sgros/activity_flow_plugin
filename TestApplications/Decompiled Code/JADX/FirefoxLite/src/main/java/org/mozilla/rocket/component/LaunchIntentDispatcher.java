package org.mozilla.rocket.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.focus.activity.SettingsActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.focus.utils.SupportUtils;

/* compiled from: LaunchIntentDispatcher.kt */
public final class LaunchIntentDispatcher {
    public static final Companion Companion = new Companion();

    /* compiled from: LaunchIntentDispatcher.kt */
    public enum Action {
        NORMAL,
        HANDLED
    }

    /* compiled from: LaunchIntentDispatcher.kt */
    public enum Command {
        SET_DEFAULT("SET_DEFAULT");
        
        private final String value;

        protected Command(String str) {
            Intrinsics.checkParameterIsNotNull(str, "value");
            this.value = str;
        }

        public final String getValue() {
            return this.value;
        }
    }

    /* compiled from: LaunchIntentDispatcher.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Action dispatch(Context context, Intent intent) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(intent, "intent");
            if (intent.getBooleanExtra(LaunchMethod.EXTRA_BOOL_HOME_SCREEN_SHORTCUT.getValue(), false)) {
                TelemetryWrapper.launchByHomeScreenShortcutEvent();
                return Action.NORMAL;
            } else if (intent.getBooleanExtra(LaunchMethod.EXTRA_BOOL_TEXT_SELECTION.getValue(), false)) {
                TelemetryWrapper.launchByExternalAppEvent("text_selection");
                return Action.NORMAL;
            } else if (intent.getBooleanExtra(LaunchMethod.EXTRA_BOOL_WEB_SEARCH.getValue(), false)) {
                TelemetryWrapper.launchByExternalAppEvent("web_search");
                return Action.NORMAL;
            } else if (intent.getBooleanExtra("_intent_to_resolve_browser_", false)) {
                context.startActivity(new Intent(context, SettingsActivity.class));
                return Action.HANDLED;
            } else {
                String stringExtra = intent.getStringExtra("push_open_url");
                if (stringExtra != null) {
                    intent.setData(Uri.parse(stringExtra));
                    intent.setAction("android.intent.action.VIEW");
                    intent.setClass(context, MainActivity.class);
                    intent.putExtra("open_new_tab", true);
                }
                stringExtra = intent.getStringExtra("push_command");
                if (stringExtra == null || !Intrinsics.areEqual(stringExtra, Command.SET_DEFAULT.getValue())) {
                    if (Intrinsics.areEqual("android.intent.action.MAIN", intent.getAction())) {
                        TelemetryWrapper.launchByAppLauncherEvent();
                        return Action.NORMAL;
                    } else if (!Intrinsics.areEqual("android.intent.action.VIEW", intent.getAction())) {
                        return Action.NORMAL;
                    } else {
                        TelemetryWrapper.launchByExternalAppEvent(null);
                        return Action.NORMAL;
                    }
                } else if (IntentUtils.openDefaultAppsSettings(context)) {
                    return Action.HANDLED;
                } else {
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse(SupportUtils.getSumoURLForTopic(context, "rocket-default")));
                    return Action.NORMAL;
                }
            }
        }
    }

    /* compiled from: LaunchIntentDispatcher.kt */
    public enum LaunchMethod {
        EXTRA_BOOL_WEB_SEARCH("web_search"),
        EXTRA_BOOL_TEXT_SELECTION("text_selection"),
        EXTRA_BOOL_HOME_SCREEN_SHORTCUT("shortcut");
        
        private final String value;

        protected LaunchMethod(String str) {
            Intrinsics.checkParameterIsNotNull(str, "value");
            this.value = str;
        }

        public final String getValue() {
            return this.value;
        }
    }
}
