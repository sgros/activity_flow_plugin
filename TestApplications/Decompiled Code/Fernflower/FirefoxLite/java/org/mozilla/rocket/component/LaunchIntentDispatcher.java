package org.mozilla.rocket.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.focus.activity.SettingsActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.focus.utils.SupportUtils;

public final class LaunchIntentDispatcher {
   public static final LaunchIntentDispatcher.Companion Companion = new LaunchIntentDispatcher.Companion((DefaultConstructorMarker)null);

   public static enum Action {
      HANDLED,
      NORMAL;

      static {
         LaunchIntentDispatcher.Action var0 = new LaunchIntentDispatcher.Action("NORMAL", 0);
         NORMAL = var0;
         LaunchIntentDispatcher.Action var1 = new LaunchIntentDispatcher.Action("HANDLED", 1);
         HANDLED = var1;
      }
   }

   public static enum Command {
      SET_DEFAULT;

      private final String value;

      static {
         LaunchIntentDispatcher.Command var0 = new LaunchIntentDispatcher.Command("SET_DEFAULT", 0, "SET_DEFAULT");
         SET_DEFAULT = var0;
      }

      protected Command(String var3) {
         Intrinsics.checkParameterIsNotNull(var3, "value");
         super(var1, var2);
         this.value = var3;
      }

      public final String getValue() {
         return this.value;
      }
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final LaunchIntentDispatcher.Action dispatch(Context var1, Intent var2) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         Intrinsics.checkParameterIsNotNull(var2, "intent");
         if (var2.getBooleanExtra(LaunchIntentDispatcher.LaunchMethod.EXTRA_BOOL_HOME_SCREEN_SHORTCUT.getValue(), false)) {
            TelemetryWrapper.launchByHomeScreenShortcutEvent();
            return LaunchIntentDispatcher.Action.NORMAL;
         } else if (var2.getBooleanExtra(LaunchIntentDispatcher.LaunchMethod.EXTRA_BOOL_TEXT_SELECTION.getValue(), false)) {
            TelemetryWrapper.launchByExternalAppEvent("text_selection");
            return LaunchIntentDispatcher.Action.NORMAL;
         } else if (var2.getBooleanExtra(LaunchIntentDispatcher.LaunchMethod.EXTRA_BOOL_WEB_SEARCH.getValue(), false)) {
            TelemetryWrapper.launchByExternalAppEvent("web_search");
            return LaunchIntentDispatcher.Action.NORMAL;
         } else if (var2.getBooleanExtra("_intent_to_resolve_browser_", false)) {
            var1.startActivity(new Intent(var1, SettingsActivity.class));
            return LaunchIntentDispatcher.Action.HANDLED;
         } else {
            String var3 = var2.getStringExtra("push_open_url");
            if (var3 != null) {
               var2.setData(Uri.parse(var3));
               var2.setAction("android.intent.action.VIEW");
               var2.setClass(var1, MainActivity.class);
               var2.putExtra("open_new_tab", true);
            }

            var3 = var2.getStringExtra("push_command");
            if (var3 != null && Intrinsics.areEqual(var3, LaunchIntentDispatcher.Command.SET_DEFAULT.getValue())) {
               if (!IntentUtils.openDefaultAppsSettings(var1)) {
                  var2.setAction("android.intent.action.VIEW");
                  var2.setData(Uri.parse(SupportUtils.getSumoURLForTopic(var1, "rocket-default")));
                  return LaunchIntentDispatcher.Action.NORMAL;
               } else {
                  return LaunchIntentDispatcher.Action.HANDLED;
               }
            } else if (Intrinsics.areEqual("android.intent.action.MAIN", var2.getAction())) {
               TelemetryWrapper.launchByAppLauncherEvent();
               return LaunchIntentDispatcher.Action.NORMAL;
            } else if (Intrinsics.areEqual("android.intent.action.VIEW", var2.getAction())) {
               TelemetryWrapper.launchByExternalAppEvent((String)null);
               return LaunchIntentDispatcher.Action.NORMAL;
            } else {
               return LaunchIntentDispatcher.Action.NORMAL;
            }
         }
      }
   }

   public static enum LaunchMethod {
      EXTRA_BOOL_HOME_SCREEN_SHORTCUT,
      EXTRA_BOOL_TEXT_SELECTION,
      EXTRA_BOOL_WEB_SEARCH;

      private final String value;

      static {
         LaunchIntentDispatcher.LaunchMethod var0 = new LaunchIntentDispatcher.LaunchMethod("EXTRA_BOOL_WEB_SEARCH", 0, "web_search");
         EXTRA_BOOL_WEB_SEARCH = var0;
         LaunchIntentDispatcher.LaunchMethod var1 = new LaunchIntentDispatcher.LaunchMethod("EXTRA_BOOL_TEXT_SELECTION", 1, "text_selection");
         EXTRA_BOOL_TEXT_SELECTION = var1;
         LaunchIntentDispatcher.LaunchMethod var2 = new LaunchIntentDispatcher.LaunchMethod("EXTRA_BOOL_HOME_SCREEN_SHORTCUT", 2, "shortcut");
         EXTRA_BOOL_HOME_SCREEN_SHORTCUT = var2;
      }

      protected LaunchMethod(String var3) {
         Intrinsics.checkParameterIsNotNull(var3, "value");
         super(var1, var2);
         this.value = var3;
      }

      public final String getValue() {
         return this.value;
      }
   }
}
