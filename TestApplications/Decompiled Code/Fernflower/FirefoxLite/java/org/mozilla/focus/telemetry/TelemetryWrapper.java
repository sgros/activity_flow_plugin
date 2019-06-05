package org.mozilla.focus.telemetry;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.Inject;
import org.mozilla.focus.search.SearchEngine;
import org.mozilla.focus.search.SearchEngineManager;
import org.mozilla.focus.utils.AdjustHelper;
import org.mozilla.focus.utils.Browsers;
import org.mozilla.focus.utils.Settings;
import org.mozilla.rocket.theme.ThemeManager;
import org.mozilla.telemetry.Telemetry;
import org.mozilla.telemetry.TelemetryHolder;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.event.TelemetryEvent;
import org.mozilla.telemetry.measurement.DefaultSearchMeasurement;
import org.mozilla.telemetry.measurement.EventsMeasurement;
import org.mozilla.telemetry.measurement.SettingsMeasurement;
import org.mozilla.telemetry.measurement.TelemetryMeasurement;
import org.mozilla.telemetry.net.HttpURLConnectionTelemetryClient;
import org.mozilla.telemetry.net.TelemetryClient;
import org.mozilla.telemetry.ping.TelemetryCorePingBuilder;
import org.mozilla.telemetry.ping.TelemetryEventPingBuilder;
import org.mozilla.telemetry.ping.TelemetryPingBuilder;
import org.mozilla.telemetry.schedule.TelemetryScheduler;
import org.mozilla.telemetry.schedule.jobscheduler.JobSchedulerTelemetryScheduler;
import org.mozilla.telemetry.serialize.JSONPingSerializer;
import org.mozilla.telemetry.serialize.TelemetryPingSerializer;
import org.mozilla.telemetry.storage.FileTelemetryStorage;
import org.mozilla.telemetry.storage.TelemetryStorage;
import org.mozilla.threadutils.ThreadUtils;

public final class TelemetryWrapper {
   public static final TelemetryWrapper INSTANCE = new TelemetryWrapper();
   private static final AtomicInteger sRefCount = new AtomicInteger(0);

   private TelemetryWrapper() {
   }

   public static final void addNewTabFromContextMenu() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "add", "browser_contextmenu", "link"), false, 1, (Object)null);
   }

   public static final void addNewTabFromHome() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "add", "tab", "home"), false, 1, (Object)null);
   }

   public static final void bookmarkEditItem() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "edit", "panel", "bookmark"), false, 1, (Object)null);
   }

   public static final void bookmarkOpenItem() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "open", "panel", "bookmark"), false, 1, (Object)null);
   }

   public static final void bookmarkRemoveItem() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "remove", "panel", "bookmark"), false, 1, (Object)null);
   }

   public static final void browseEnterFullScreenEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "fullscreen", "browser", "enter"), false, 1, (Object)null);
   }

   private final void browseEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "open", "search_bar", "link"), false, 1, (Object)null);
   }

   public static final void browseExitFullScreenEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "fullscreen", "browser", "exit"), false, 1, (Object)null);
   }

   public static final void browseFilePermissionEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "permission", "browser", "file"), false, 1, (Object)null);
   }

   public static final void browseGeoLocationPermissionEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "permission", "browser", "geolocation"), false, 1, (Object)null);
   }

   public static final void browsePermissionEvent(String[] var0) {
      Intrinsics.checkParameterIsNotNull(var0, "requests");
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         String var3 = var0[var2];
         int var4 = var3.hashCode();
         String var5;
         if (var4 != -1660821873) {
            if (var4 != 968612586) {
               if (var4 != 1069496794) {
                  if (var4 != 1233677653) {
                     var5 = var3;
                  } else {
                     var5 = var3;
                     if (var3.equals("android.webkit.resource.MIDI_SYSEX")) {
                        var5 = "midi";
                     }
                  }
               } else {
                  var5 = var3;
                  if (var3.equals("android.webkit.resource.PROTECTED_MEDIA_ID")) {
                     var5 = "eme";
                  }
               }
            } else {
               var5 = var3;
               if (var3.equals("android.webkit.resource.AUDIO_CAPTURE")) {
                  var5 = "audio";
               }
            }
         } else {
            var5 = var3;
            if (var3.equals("android.webkit.resource.VIDEO_CAPTURE")) {
               var5 = "video";
            }
         }

         TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "permission", "browser", var5), false, 1, (Object)null);
      }

   }

   public static final void cancelWebContextMenuEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "cancel", "browser_contextmenu", (String)null, 8, (DefaultConstructorMarker)null), false, 1, (Object)null);
   }

   public static final void changeThemeTo(String var0) {
      Intrinsics.checkParameterIsNotNull(var0, "themeName");
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "change", "themetoy", (String)null, 8, (DefaultConstructorMarker)null)).extra("to", var0), false, 1, (Object)null);
   }

   public static final void clearHistory() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "clear", "panel", "history"), false, 1, (Object)null);
   }

   public static final void clickAddTabToolbar() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "add", "tab", "toolbar"), false, 1, (Object)null);
   }

   public static final void clickAddTabTray() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "add", "tab", "tab_tray"), false, 1, (Object)null);
   }

   public static final void clickAddToHome() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "pin_shortcut", "toolbar", "link"), false, 1, (Object)null);
      AdjustHelper.trackEvent("g1tpbz");
   }

   public static final void clickBannerBackground(String var0) {
      Intrinsics.checkParameterIsNotNull(var0, "pageId");
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "click", "banner", "background")).extra("source", var0), false, 1, (Object)null);
   }

   public static final void clickBannerItem(String var0, int var1) {
      Intrinsics.checkParameterIsNotNull(var0, "pageId");
      TelemetryWrapper.EventBuilder var2 = (new TelemetryWrapper.EventBuilder("action", "click", "banner", "item")).extra("source", var0);
      var0 = Integer.toString(var1);
      Intrinsics.checkExpressionValueIsNotNull(var0, "Integer.toString(itemPosition)");
      TelemetryWrapper.EventBuilder.queue$default(var2.extra("on", var0), false, 1, (Object)null);
   }

   public static final void clickDefaultBrowserInSetting() {
      AdjustHelper.trackEvent("iyqe7a");
   }

   public static final void clickDefaultSettingNotification() {
      TelemetryWrapper.EventBuilder var0 = (new TelemetryWrapper.EventBuilder("action", "click", "default_browser", (String)null, 8, (DefaultConstructorMarker)null)).extra("source", "notification");
      String var1 = Integer.toString(2);
      Intrinsics.checkExpressionValueIsNotNull(var1, "Integer.toString(DEFAULT…CATION_TELEMETRY_VERSION)");
      TelemetryWrapper.EventBuilder.queue$default(var0.extra("version", var1), false, 1, (Object)null);
   }

   public static final void clickMenuBookmark() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "menu", "bookmark"), false, 1, (Object)null);
   }

   public static final void clickMenuCapture() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "menu", "capture"), false, 1, (Object)null);
   }

   public static final void clickMenuClearCache() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "menu", "clear_cache"), false, 1, (Object)null);
   }

   public static final void clickMenuDownload() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "menu", "download"), false, 1, (Object)null);
   }

   public static final void clickMenuExit() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "menu", "exit"), false, 1, (Object)null);
   }

   public static final void clickMenuHistory() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "menu", "history"), false, 1, (Object)null);
   }

   public static final void clickMenuSettings() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "menu", "settings"), false, 1, (Object)null);
   }

   public static final void clickQuickSearchEngine(String var0) {
      Intrinsics.checkParameterIsNotNull(var0, "engineName");
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("search", "click", "quicksearch", (String)null)).extra("engine", var0), false, 1, (Object)null);
   }

   public static final void clickRateApp(String var0, String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "source");
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "click", "feedback", var0)).extra("source", var1).extra("version", String.valueOf(3)), false, 1, (Object)null);
      if (Intrinsics.areEqual("positive", var0)) {
         AdjustHelper.trackEvent("uivfof");
      }

   }

   public static final void clickTabFromTabTray() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "change", "tab", "tab_tray"), false, 1, (Object)null);
   }

   public static final void clickToolbarBookmark(boolean var0) {
      TelemetryWrapper.EventBuilder var1 = new TelemetryWrapper.EventBuilder("action", "share", "toolbar", "bookmark");
      String var2 = Boolean.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var2, "java.lang.Boolean.toString(isAdd)");
      TelemetryWrapper.EventBuilder.queue$default(var1.extra("to", var2), false, 1, (Object)null);
      if (var0) {
         AdjustHelper.trackEvent("sj5vxj");
      }

   }

   public static final void clickToolbarCapture(String var0, int var1) {
      Intrinsics.checkParameterIsNotNull(var0, "category");
      TelemetryWrapper.EventBuilder var2 = new TelemetryWrapper.EventBuilder("action", "click", "toolbar", "capture");
      String var3 = Integer.toString(3);
      Intrinsics.checkExpressionValueIsNotNull(var3, "Integer.toString(TOOL_BA…APTURE_TELEMETRY_VERSION)");
      TelemetryWrapper.EventBuilder var4 = var2.extra("version", var3).extra("category", var0);
      String var5 = Integer.toString(var1);
      Intrinsics.checkExpressionValueIsNotNull(var5, "Integer.toString(categoryVersion)");
      TelemetryWrapper.EventBuilder.queue$default(var4.extra("category_versio", var5), false, 1, (Object)null);
      AdjustHelper.trackEvent("ky29gk");
   }

   public static final void clickToolbarForward() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "toolbar", "forward"), false, 1, (Object)null);
   }

   public static final void clickToolbarReload() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "toolbar", "reload"), false, 1, (Object)null);
   }

   public static final void clickToolbarSearch() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "show", "search_bar", "search_btn"), false, 1, (Object)null);
   }

   public static final void clickToolbarShare() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "share", "toolbar", "link"), false, 1, (Object)null);
   }

   public static final void clickTopSiteOn(int var0, String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "source");
      TelemetryWrapper.EventBuilder var2 = new TelemetryWrapper.EventBuilder("action", "open", "home", "link");
      String var3 = Integer.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var3, "Integer.toString(index)");
      TelemetryWrapper.EventBuilder.queue$default(var2.extra("on", var3).extra("source", var1).extra("version", "2"), false, 1, (Object)null);
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "add", "tab", "top_site"), false, 1, (Object)null);
   }

   public static final void clickUrlbar() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "show", "search_bar", "mini_urlbar"), false, 1, (Object)null);
   }

   public static final void clickVpnRecommend(boolean var0) {
      String var1;
      if (var0) {
         var1 = "positive";
      } else {
         var1 = "negative";
      }

      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "vpn_doorhanger", var1), false, 1, (Object)null);
   }

   public static final void clickVpnRecommender(boolean var0) {
      TelemetryWrapper.EventBuilder var1 = new TelemetryWrapper.EventBuilder("action", "click", "home", "vpn_recommend");
      String var2 = Boolean.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var2, "java.lang.Boolean.toString(installed)");
      TelemetryWrapper.EventBuilder.queue$default(var1.extra("vpn_installed", var2), false, 1, (Object)null);
   }

   public static final void clickVpnSurvey() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "home", "vpn"), false, 1, (Object)null);
   }

   public static final void clickWifiFinderSurvey() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "home", "wifi_finder"), false, 1, (Object)null);
   }

   public static final void closeAllTabFromTabTray() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "close_all", "tab_tray"), false, 1, (Object)null);
   }

   public static final void closeTabFromTabTray() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "remove", "tab", "tab_tray"), false, 1, (Object)null);
   }

   public static final void copyImageEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "copy", "browser_contextmenu", "image"), false, 1, (Object)null);
   }

   public static final void copyLinkEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "copy", "browser_contextmenu", "link"), false, 1, (Object)null);
   }

   private final DefaultSearchMeasurement.DefaultSearchEngineProvider createDefaultSearchProvider(final Context var1) {
      return (DefaultSearchMeasurement.DefaultSearchEngineProvider)(new DefaultSearchMeasurement.DefaultSearchEngineProvider() {
         public final String getDefaultSearchEngineIdentifier() {
            SearchEngine var1x = SearchEngineManager.getInstance().getDefaultSearchEngine(var1);
            Intrinsics.checkExpressionValueIsNotNull(var1x, "SearchEngineManager.getI…aultSearchEngine(context)");
            return var1x.getIdentifier();
         }
      });
   }

   public static final void deleteCaptureImage(String var0, int var1) {
      Intrinsics.checkParameterIsNotNull(var0, "category");
      TelemetryWrapper.EventBuilder var2 = (new TelemetryWrapper.EventBuilder("action", "delete", "capture", "image")).extra("category", var0);
      var0 = Integer.toString(var1);
      Intrinsics.checkExpressionValueIsNotNull(var0, "Integer.toString(categoryVersion)");
      TelemetryWrapper.EventBuilder.queue$default(var2.extra("category_versio", var0), false, 1, (Object)null);
   }

   public static final void dismissVpnRecommend() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "vpn_doorhanger", "dismiss"), false, 1, (Object)null);
   }

   public static final void downloadDeleteFile() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "delete", "panel", "file"), false, 1, (Object)null);
   }

   public static final void downloadOpenFile(boolean var0) {
      TelemetryWrapper.EventBuilder var1 = new TelemetryWrapper.EventBuilder("action", "open", "panel", "file");
      String var2 = Boolean.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var2, "java.lang.Boolean.toString(fromSnackBar)");
      TelemetryWrapper.EventBuilder.queue$default(var1.extra("snackbar", var2), false, 1, (Object)null);
   }

   public static final void downloadRemoveFile() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "remove", "panel", "file"), false, 1, (Object)null);
   }

   public static final void editCaptureImage(boolean var0, String var1, int var2) {
      Intrinsics.checkParameterIsNotNull(var1, "category");
      TelemetryWrapper.EventBuilder var3 = new TelemetryWrapper.EventBuilder("action", "edit", "capture", "image");
      String var4 = Boolean.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var4, "java.lang.Boolean.toString(editAppResolved)");
      TelemetryWrapper.EventBuilder var5 = var3.extra("success", var4).extra("category", var1);
      var4 = Integer.toString(var2);
      Intrinsics.checkExpressionValueIsNotNull(var4, "Integer.toString(categoryVersion)");
      TelemetryWrapper.EventBuilder.queue$default(var5.extra("category_versio", var4), false, 1, (Object)null);
   }

   public static final void erasePrivateModeNotification() {
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "clear", "private_mode", (String)null, 8, (DefaultConstructorMarker)null)).extra("source", "notification"), false, 1, (Object)null);
   }

   public static final void findInPage(TelemetryWrapper.FIND_IN_PAGE var0) {
      // $FF: Couldn't be decompiled
   }

   public static final void finishFirstRunEvent(long var0, int var2) {
      TelemetryWrapper.EventBuilder var3 = new TelemetryWrapper.EventBuilder("action", "show", "firstrun", "finish");
      String var4 = Long.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var4, "java.lang.Long.toString(duration)");
      TelemetryWrapper.EventBuilder var6 = var3.extra("on", var4);
      String var5 = Integer.toString(var2);
      Intrinsics.checkExpressionValueIsNotNull(var5, "Integer.toString(mode)");
      TelemetryWrapper.EventBuilder.queue$default(var6.extra("mode", var5), false, 1, (Object)null);
   }

   public static final void historyOpenLink() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "open", "panel", "link"), false, 1, (Object)null);
   }

   public static final void historyRemoveLink() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "remove", "panel", "link"), false, 1, (Object)null);
   }

   public static final boolean isTelemetryEnabled(Context var0) {
      boolean var1;
      if (var0 == null) {
         var1 = false;
      } else {
         var1 = Inject.isTelemetryEnabled(var0);
      }

      return var1;
   }

   public static final void launchByAppLauncherEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "launch", "app", "launcher"), false, 1, (Object)null);
   }

   public static final void launchByExternalAppEvent(String var0) {
      if (var0 == null) {
         TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "launch", "app", "external_app"), false, 1, (Object)null);
      } else {
         TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "launch", "app", "external_app")).extra("type", var0), false, 1, (Object)null);
      }

   }

   public static final void launchByHomeScreenShortcutEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "launch", "app", "shortcut"), false, 1, (Object)null);
   }

   public static final void longPressDownloadIndicator() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("Downloads", "long_press", "toolbar", "download"), false, 1, (Object)null);
   }

   public static final void menuBlockImageChangeTo(boolean var0) {
      TelemetryWrapper.EventBuilder var1 = new TelemetryWrapper.EventBuilder("action", "change", "menu", "block_image");
      String var2 = Boolean.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var2, "java.lang.Boolean.toString(enable)");
      TelemetryWrapper.EventBuilder.queue$default(var1.extra("to", var2), false, 1, (Object)null);
   }

   public static final void menuNightModeChangeTo(boolean var0) {
      TelemetryWrapper.EventBuilder var1 = new TelemetryWrapper.EventBuilder("action", "change", "menu", "night_mode");
      String var2 = Boolean.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var2, "java.lang.Boolean.toString(enable)");
      TelemetryWrapper.EventBuilder.queue$default(var1.extra("to", var2), false, 1, (Object)null);
   }

   public static final void menuTurboChangeTo(boolean var0) {
      TelemetryWrapper.EventBuilder var1 = new TelemetryWrapper.EventBuilder("action", "change", "menu", "turbo");
      String var2 = Boolean.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var2, "java.lang.Boolean.toString(enable)");
      TelemetryWrapper.EventBuilder.queue$default(var1.extra("to", var2), false, 1, (Object)null);
   }

   public static final void nightModeBrightnessChangeTo(int var0, boolean var1) {
      TelemetryWrapper.EventBuilder var2 = new TelemetryWrapper.EventBuilder("action", "change", "setting", "night_mode_brightness");
      String var3;
      if (var1) {
         var3 = "setting";
      } else {
         var3 = "menu";
      }

      TelemetryWrapper.EventBuilder.queue$default(var2.extra("source", var3).extra("to", String.valueOf(var0)), false, 1, (Object)null);
   }

   public static final void onDefaultBrowserServiceFailed() {
      TelemetryWrapper.EventBuilder var0 = new TelemetryWrapper.EventBuilder("action", "change", "default_browser", (String)null, 8, (DefaultConstructorMarker)null);
      String var1 = Boolean.toString(false);
      Intrinsics.checkExpressionValueIsNotNull(var1, "java.lang.Boolean.toString(false)");
      TelemetryWrapper.EventBuilder.queue$default(var0.extra("success", var1), false, 1, (Object)null);
   }

   public static final void openCapture() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "open", "panel", "capture"), false, 1, (Object)null);
   }

   public static final void openCaptureLink(String var0, int var1) {
      Intrinsics.checkParameterIsNotNull(var0, "category");
      TelemetryWrapper.EventBuilder var3 = (new TelemetryWrapper.EventBuilder("action", "open", "capture", "link")).extra("category", var0);
      String var2 = Integer.toString(var1);
      Intrinsics.checkExpressionValueIsNotNull(var2, "Integer.toString(categoryVersion)");
      TelemetryWrapper.EventBuilder.queue$default(var3.extra("category_versio", var2), false, 1, (Object)null);
   }

   public static final void openLifeFeedEc() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "open", "panel", "lifefeed_ec"), false, 1, (Object)null);
   }

   public static final void openLifeFeedNews() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "open", "panel", "lifefeed_news"), false, 1, (Object)null);
   }

   public static final void openWebContextMenuEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "long_press", "browser", (String)null, 8, (DefaultConstructorMarker)null), false, 1, (Object)null);
   }

   public static final void privateModeTray() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "private_mode", "tab_tray"), false, 1, (Object)null);
   }

   public static final void promoteShareClickEvent(String var0, String var1) {
      Intrinsics.checkParameterIsNotNull(var0, "value");
      Intrinsics.checkParameterIsNotNull(var1, "source");
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "click", "promote_share", var0)).extra("source", var1), false, 1, (Object)null);
      if (Intrinsics.areEqual("share", var0)) {
         AdjustHelper.trackEvent("3obefy");
      }

   }

   public static final void receiveFirstrunConfig(long var0, String var2) {
      TelemetryWrapper.EventBuilder var3 = (new TelemetryWrapper.EventBuilder("action", "get", "firstrun_push", (String)null, 8, (DefaultConstructorMarker)null)).extra("delay", String.valueOf(var0));
      if (var2 == null) {
         var2 = "";
      }

      TelemetryWrapper.EventBuilder.queue$default(var3.extra("message", var2), false, 1, (Object)null);
   }

   public static final void removeTopSite(boolean var0) {
      TelemetryWrapper.EventBuilder var1 = new TelemetryWrapper.EventBuilder("action", "remove", "home", "link");
      String var2 = Boolean.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var2, "java.lang.Boolean.toString(isDefault)");
      TelemetryWrapper.EventBuilder.queue$default(var1.extra("default", var2), false, 1, (Object)null);
   }

   public static final void resetThemeToDefault() {
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "reset", "themetoy", (String)null, 8, (DefaultConstructorMarker)null)).extra("to", "default"), false, 1, (Object)null);
   }

   public static final void saveImageEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "save", "browser_contextmenu", "image"), false, 1, (Object)null);
   }

   public static final void searchClear() {
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "clear", "search_bar", (String)null, 8, (DefaultConstructorMarker)null)).extra("version", "2"), false, 1, (Object)null);
   }

   public static final void searchDismiss() {
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "cancel", "search_bar", (String)null, 8, (DefaultConstructorMarker)null)).extra("version", "2"), false, 1, (Object)null);
   }

   private final void searchEnterEvent() {
      Telemetry var1 = TelemetryHolder.get();
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "type_query", "search_bar", (String)null, 8, (DefaultConstructorMarker)null), false, 1, (Object)null);
      SearchEngineManager var2 = SearchEngineManager.getInstance();
      Intrinsics.checkExpressionValueIsNotNull(var1, "telemetry");
      TelemetryConfiguration var3 = var1.getConfiguration();
      Intrinsics.checkExpressionValueIsNotNull(var3, "telemetry.configuration");
      SearchEngine var4 = var2.getDefaultSearchEngine(var3.getContext());
      Intrinsics.checkExpressionValueIsNotNull(var4, "searchEngine");
      var1.recordSearch("actionbar", var4.getIdentifier());
   }

   public static final void searchSelectEvent() {
      Telemetry var0 = TelemetryHolder.get();
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "select_query", "search_bar", (String)null, 8, (DefaultConstructorMarker)null), false, 1, (Object)null);
      SearchEngineManager var1 = SearchEngineManager.getInstance();
      Intrinsics.checkExpressionValueIsNotNull(var0, "telemetry");
      TelemetryConfiguration var2 = var0.getConfiguration();
      Intrinsics.checkExpressionValueIsNotNull(var2, "telemetry.configuration");
      SearchEngine var3 = var1.getDefaultSearchEngine(var2.getContext());
      Intrinsics.checkExpressionValueIsNotNull(var3, "searchEngine");
      var0.recordSearch("suggestion", var3.getIdentifier());
   }

   public static final void searchSuggestionLongClick() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "long_press", "search_suggestion", (String)null, 8, (DefaultConstructorMarker)null), false, 1, (Object)null);
   }

   public static final void setTelemetryEnabled(Context var0, final boolean var1) {
      Intrinsics.checkParameterIsNotNull(var0, "context");
      Resources var2 = var0.getResources();
      SharedPreferences var3 = PreferenceManager.getDefaultSharedPreferences(var0);
      final String var4 = var2.getString(2131755331);
      var3.edit().putBoolean(var4, var1).apply();
      ThreadUtils.postToBackgroundThread((Runnable)(new Runnable() {
         public final void run() {
            String var1x = var4;
            Intrinsics.checkExpressionValueIsNotNull(var1x, "key");
            TelemetryWrapper.settingsEvent(var1x, String.valueOf(var1), true);
            Telemetry var2 = TelemetryHolder.get();
            Intrinsics.checkExpressionValueIsNotNull(var2, "TelemetryHolder.get()");
            TelemetryConfiguration var3 = var2.getConfiguration();
            Intrinsics.checkExpressionValueIsNotNull(var3, "TelemetryHolder.get()\n  …           .configuration");
            var3.setCollectionEnabled(var1);
         }
      }));
   }

   public static final void settingsClickEvent(String var0) {
      Intrinsics.checkParameterIsNotNull(var0, "key");
      var0 = FirebaseEvent.getValidPrefKey(var0);
      if (var0 != null) {
         TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "setting", var0), false, 1, (Object)null);
      }

   }

   public static final void settingsEvent(String var0, String var1, boolean var2) {
      Intrinsics.checkParameterIsNotNull(var0, "key");
      Intrinsics.checkParameterIsNotNull(var1, "value");
      var0 = FirebaseEvent.getValidPrefKey(var0);
      if (var0 != null) {
         (new TelemetryWrapper.EventBuilder("action", "change", "setting", var0)).extra("to", var1).queue(var2);
      }

   }

   public static final void settingsLearnMoreClickEvent(String var0) {
      Intrinsics.checkParameterIsNotNull(var0, "source");
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "click", "setting", "learn_more")).extra("source", var0), false, 1, (Object)null);
   }

   public static final void settingsLocaleChangeEvent(String var0, String var1, boolean var2) {
      Intrinsics.checkParameterIsNotNull(var0, "key");
      Intrinsics.checkParameterIsNotNull(var1, "value");
      TelemetryWrapper.EventBuilder var3 = (new TelemetryWrapper.EventBuilder("action", "change", "setting", var0)).extra("to", var1);
      var1 = Boolean.toString(var2);
      Intrinsics.checkExpressionValueIsNotNull(var1, "java.lang.Boolean.toString(isDefault)");
      TelemetryWrapper.EventBuilder.queue$default(var3.extra("default", var1), false, 1, (Object)null);
   }

   public static final void shareCaptureImage(boolean var0, String var1, int var2) {
      Intrinsics.checkParameterIsNotNull(var1, "category");
      TelemetryWrapper.EventBuilder var3 = new TelemetryWrapper.EventBuilder("action", "share", "capture", "image");
      String var4 = Boolean.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var4, "java.lang.Boolean.toString(fromSnackBar)");
      TelemetryWrapper.EventBuilder var5 = var3.extra("snackbar", var4).extra("category", var1);
      var4 = Integer.toString(var2);
      Intrinsics.checkExpressionValueIsNotNull(var4, "Integer.toString(categoryVersion)");
      TelemetryWrapper.EventBuilder.queue$default(var5.extra("category_versio", var4), false, 1, (Object)null);
   }

   public static final void shareImageEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "share", "browser_contextmenu", "image"), false, 1, (Object)null);
   }

   public static final void shareLinkEvent() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "share", "browser_contextmenu", "link"), false, 1, (Object)null);
   }

   public static final void showBannerNew(String var0) {
      Intrinsics.checkParameterIsNotNull(var0, "id");
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "show", "banner", "new")).extra("to", var0), false, 1, (Object)null);
   }

   public static final void showBannerReturn(String var0) {
      Intrinsics.checkParameterIsNotNull(var0, "id");
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "show", "banner", "return")).extra("to", var0), false, 1, (Object)null);
   }

   public static final void showBannerSwipe(String var0) {
      Intrinsics.checkParameterIsNotNull(var0, "id");
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "show", "banner", "swipe")).extra("to", var0), false, 1, (Object)null);
   }

   public static final void showBannerUpdate(String var0) {
      Intrinsics.checkParameterIsNotNull(var0, "id");
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "show", "banner", "update")).extra("to", var0), false, 1, (Object)null);
   }

   public static final void showBookmarkContextMenu() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "show", "menu", "bookmark"), false, 1, (Object)null);
   }

   public static final void showCaptureInfo(String var0, int var1) {
      Intrinsics.checkParameterIsNotNull(var0, "category");
      TelemetryWrapper.EventBuilder var3 = (new TelemetryWrapper.EventBuilder("action", "show", "capture", "info")).extra("category", var0);
      String var2 = Integer.toString(var1);
      Intrinsics.checkExpressionValueIsNotNull(var2, "Integer.toString(categoryVersion)");
      TelemetryWrapper.EventBuilder.queue$default(var3.extra("category_versio", var2), false, 1, (Object)null);
   }

   public static final void showDefaultSettingNotification() {
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "show", "default_browser", (String)null, 8, (DefaultConstructorMarker)null)).extra("source", "notification"), false, 1, (Object)null);
   }

   public static final void showFileContextMenu() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "show", "menu", "download"), false, 1, (Object)null);
   }

   public static final void showFirstrunNotification(long var0, String var2) {
      TelemetryWrapper.EventBuilder var3 = (new TelemetryWrapper.EventBuilder("action", "show", "firstrun_push", (String)null, 8, (DefaultConstructorMarker)null)).extra("delay", String.valueOf(var0));
      if (var2 == null) {
         var2 = "";
      }

      TelemetryWrapper.EventBuilder.queue$default(var3.extra("message", var2), false, 1, (Object)null);
   }

   public static final void showHistoryContextMenu() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "show", "menu", "history"), false, 1, (Object)null);
   }

   public static final void showHome() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "show", "home", (String)null, 8, (DefaultConstructorMarker)null), false, 1, (Object)null);
   }

   public static final void showMenuHome() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "show", "menu", "home"), false, 1, (Object)null);
   }

   public static final void showMenuToolbar() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "show", "menu", "toolbar"), false, 1, (Object)null);
   }

   public static final void showPanelBookmark() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "panel", "bookmark"), false, 1, (Object)null);
   }

   public static final void showPanelCapture() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "panel", "capture"), false, 1, (Object)null);
   }

   public static final void showPanelDownload() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "panel", "download"), false, 1, (Object)null);
   }

   public static final void showPanelHistory() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "click", "panel", "history"), false, 1, (Object)null);
   }

   public static final void showPromoteShareDialog() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "show", "promote_share", (String)null, 8, (DefaultConstructorMarker)null), false, 1, (Object)null);
   }

   public static final void showRateApp(boolean var0) {
      TelemetryWrapper.EventBuilder var1 = new TelemetryWrapper.EventBuilder("action", "show", "feedback", (String)null, 8, (DefaultConstructorMarker)null);
      if (var0) {
         var1.extra("source", "notification");
      }

      TelemetryWrapper.EventBuilder.queue$default(var1, false, 1, (Object)null);
   }

   public static final void showSearchBarHome() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "show", "search_bar", "search_box"), false, 1, (Object)null);
   }

   public static final void showTabTrayHome() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "show", "tab_tray", "home"), false, 1, (Object)null);
   }

   public static final void showTabTrayToolbar() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "show", "tab_tray", "toolbar"), false, 1, (Object)null);
   }

   public static final void showVpnRecommender(boolean var0) {
      TelemetryWrapper.EventBuilder var1 = new TelemetryWrapper.EventBuilder("action", "show", "home", "vpn_recommend");
      String var2 = Boolean.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var2, "java.lang.Boolean.toString(installed)");
      TelemetryWrapper.EventBuilder.queue$default(var1.extra("vpn_installed", var2), false, 1, (Object)null);
   }

   public static final void startSession() {
      if (sRefCount.getAndIncrement() == 0) {
         TelemetryHolder.get().recordSessionStart();
      }

      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "foreground", "app", (String)null, 8, (DefaultConstructorMarker)null), false, 1, (Object)null);
   }

   public static final void stopMainActivity() {
      TelemetryHolder.get().queuePing("core").queuePing("focus-event").scheduleUpload();
   }

   public static final void stopSession() {
      if (sRefCount.decrementAndGet() == 0) {
         TelemetryHolder.get().recordSessionEnd();
      }

      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "background", "app", (String)null, 8, (DefaultConstructorMarker)null), false, 1, (Object)null);
   }

   public static final void surveyResult(String var0, String var1) {
      Intrinsics.checkParameterIsNotNull(var0, "result");
      Intrinsics.checkParameterIsNotNull(var1, "source");
      TelemetryWrapper.EventBuilder.queue$default((new TelemetryWrapper.EventBuilder("action", "click", "doorhanger", var0)).extra("source", var1), false, 1, (Object)null);
   }

   public static final void swipeBannerItem(int var0, int var1) {
      TelemetryWrapper.EventBuilder var2 = new TelemetryWrapper.EventBuilder("action", "swipe", "banner", "page");
      String var3 = Integer.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var3, "Integer.toString(directionX)");
      var2 = var2.extra("direction", var3);
      var3 = Integer.toString(var1);
      Intrinsics.checkExpressionValueIsNotNull(var3, "Integer.toString(toItemPosition)");
      TelemetryWrapper.EventBuilder.queue$default(var2.extra("to", var3), false, 1, (Object)null);
   }

   public static final void swipeTabFromTabTray() {
      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "swipe", "tab", "tab_tray"), false, 1, (Object)null);
   }

   public static final void toggleFirstRunPageEvent(boolean var0) {
      TelemetryWrapper.EventBuilder var1 = new TelemetryWrapper.EventBuilder("action", "change", "firstrun", "turbo");
      String var2 = Boolean.toString(var0);
      Intrinsics.checkExpressionValueIsNotNull(var2, "java.lang.Boolean.toString(enableTurboMode)");
      TelemetryWrapper.EventBuilder.queue$default(var1.extra("to", var2), false, 1, (Object)null);
   }

   public static final void togglePrivateMode(boolean var0) {
      String var1;
      if (var0) {
         var1 = "enter";
      } else {
         var1 = "exit";
      }

      TelemetryWrapper.EventBuilder.queue$default(new TelemetryWrapper.EventBuilder("action", "change", "private_mode", var1), false, 1, (Object)null);
      if (var0) {
         AdjustHelper.trackEvent("dccqsh");
      }

   }

   private final void updateDefaultBrowserStatus(Context var1) {
      Settings.updatePrefDefaultBrowserIfNeeded(var1, Browsers.isDefaultBrowser(var1));
   }

   public static final void urlBarEvent(boolean var0, boolean var1) {
      if (var0) {
         INSTANCE.browseEvent();
      } else if (var1) {
         searchSelectEvent();
      } else {
         INSTANCE.searchEnterEvent();
      }

   }

   public final TelemetryWrapper.EventBuilder clickFindInPageNext$app_focusWebkitRelease() {
      TelemetryWrapper.EventBuilder var1 = new TelemetryWrapper.EventBuilder("action", "click", "find_in_page", "next");
      String var2 = Integer.toString(2);
      Intrinsics.checkExpressionValueIsNotNull(var2, "Integer.toString(FIND_IN_PAGE_VERSION)");
      return var1.extra("version", var2);
   }

   public final TelemetryWrapper.EventBuilder clickFindInPagePrevious$app_focusWebkitRelease() {
      TelemetryWrapper.EventBuilder var1 = new TelemetryWrapper.EventBuilder("action", "click", "find_in_page", "previous");
      String var2 = Integer.toString(2);
      Intrinsics.checkExpressionValueIsNotNull(var2, "Integer.toString(FIND_IN_PAGE_VERSION)");
      return var1.extra("version", var2);
   }

   public final TelemetryWrapper.EventBuilder clickMenuFindInPage$app_focusWebkitRelease() {
      TelemetryWrapper.EventBuilder var1 = new TelemetryWrapper.EventBuilder("action", "click", "menu", "find_in_page");
      String var2 = Integer.toString(2);
      Intrinsics.checkExpressionValueIsNotNull(var2, "Integer.toString(FIND_IN_PAGE_VERSION)");
      return var1.extra("version", var2);
   }

   public final void clickOnEcItem(String var1, String var2, String var3) {
      Intrinsics.checkParameterIsNotNull(var1, "pos");
      TelemetryWrapper.EventBuilder var4 = (new TelemetryWrapper.EventBuilder("action", "click", "panel", "lifefeed_ec")).extra("position", var1);
      if (var2 == null) {
         var2 = "";
      }

      var4 = var4.extra("source", var2);
      if (var3 == null) {
         var3 = "";
      }

      TelemetryWrapper.EventBuilder.queue$default(var4.extra("category", var3), false, 1, (Object)null);
   }

   public final void clickOnNewsItem(String var1, String var2, String var3, String var4, String var5) {
      Intrinsics.checkParameterIsNotNull(var1, "pos");
      Intrinsics.checkParameterIsNotNull(var2, "feed");
      TelemetryWrapper.EventBuilder var6 = (new TelemetryWrapper.EventBuilder("action", "click", "panel", "lifefeed_news")).extra("position", var1).extra("feed", var2);
      if (var3 == null) {
         var3 = "";
      }

      var6 = var6.extra("source", var3);
      if (var4 == null) {
         var4 = "";
      }

      var6 = var6.extra("category", var4);
      if (var5 == null) {
         var5 = "";
      }

      TelemetryWrapper.EventBuilder.queue$default(var6.extra("subcategory", var5), false, 1, (Object)null);
   }

   public final void init(Context var1) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      ThreadPolicy var2 = StrictMode.allowThreadDiskWrites();

      try {
         Resources var3 = var1.getResources();
         boolean var4 = isTelemetryEnabled(var1);
         this.updateDefaultBrowserStatus(var1);
         TelemetryConfiguration var5 = new TelemetryConfiguration(var1);
         var5 = var5.setServerEndpoint("https://incoming.telemetry.mozilla.org").setAppName("Zerda").setUpdateChannel("release").setPreferencesImportantForTelemetry(var3.getString(2131755325), var3.getString(2131755332), var3.getString(2131755316), var3.getString(2131755303), var3.getString(2131755330), var3.getString(2131755333), var3.getString(2131755334), var3.getString(2131755312));
         TelemetryWrapper.CustomSettingsProvider var11 = new TelemetryWrapper.CustomSettingsProvider();
         TelemetryConfiguration var12 = var5.setSettingsProvider((SettingsMeasurement.SettingsProvider)var11).setCollectionEnabled(var4).setUploadEnabled(true);
         JSONPingSerializer var13 = new JSONPingSerializer();
         FileTelemetryStorage var6 = new FileTelemetryStorage(var12, (TelemetryPingSerializer)var13);
         HttpURLConnectionTelemetryClient var7 = new HttpURLConnectionTelemetryClient();
         JobSchedulerTelemetryScheduler var8 = new JobSchedulerTelemetryScheduler();
         Telemetry var14 = new Telemetry(var12, (TelemetryStorage)var6, (TelemetryClient)var7, (TelemetryScheduler)var8);
         TelemetryCorePingBuilder var15 = new TelemetryCorePingBuilder(var12);
         Telemetry var17 = var14.addPingBuilder((TelemetryPingBuilder)var15);
         TelemetryEventPingBuilder var16 = new TelemetryEventPingBuilder(var12);
         TelemetryHolder.set(var17.addPingBuilder((TelemetryPingBuilder)var16).setDefaultSearchProvider(this.createDefaultSearchProvider(var1)));
      } finally {
         StrictMode.setThreadPolicy(var2);
      }

   }

   private static final class CaptureCountMeasurement extends TelemetryMeasurement {
      public static final TelemetryWrapper.CaptureCountMeasurement.Companion Companion = new TelemetryWrapper.CaptureCountMeasurement.Companion((DefaultConstructorMarker)null);
      private final Context context;

      public CaptureCountMeasurement(Context var1) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         super("capture_count");
         this.context = var1;
      }

      public Object flush() {
         // $FF: Couldn't be decompiled
      }

      public static final class Companion {
         private Companion() {
         }

         // $FF: synthetic method
         public Companion(DefaultConstructorMarker var1) {
            this();
         }
      }
   }

   private static final class CustomSettingsProvider extends SettingsMeasurement.SharedPreferenceSettingsProvider {
      private final HashMap custom = new HashMap(2);

      public CustomSettingsProvider() {
      }

      public final void addCustomPing$app_focusWebkitRelease(TelemetryConfiguration var1, TelemetryMeasurement var2) {
         Intrinsics.checkParameterIsNotNull(var1, "configuration");
         Intrinsics.checkParameterIsNotNull(var2, "measurement");
         Set var3 = var1.getPreferencesImportantForTelemetry();
         Set var4 = var3;
         if (var3 == null) {
            var1.setPreferencesImportantForTelemetry();
            var4 = var1.getPreferencesImportantForTelemetry();
         }

         if (var4 == null) {
            Intrinsics.throwNpe();
         }

         String var6 = var2.getFieldName();
         Intrinsics.checkExpressionValueIsNotNull(var6, "measurement.fieldName");
         var4.add(var6);
         Map var7 = (Map)this.custom;
         String var5 = var2.getFieldName();
         Intrinsics.checkExpressionValueIsNotNull(var5, "measurement.fieldName");
         Object var8 = var2.flush();
         Intrinsics.checkExpressionValueIsNotNull(var8, "measurement.flush()");
         var7.put(var5, var8);
      }

      public boolean containsKey(String var1) {
         Intrinsics.checkParameterIsNotNull(var1, "key");
         boolean var2 = super.containsKey(var1);
         return this.custom.containsKey(var1) | var2;
      }

      public Object getValue(String var1) {
         Intrinsics.checkParameterIsNotNull(var1, "key");
         Object var2 = this.custom.get(var1);
         Object var3;
         if (var2 != null) {
            var3 = var2;
         } else {
            var3 = super.getValue(var1);
            Intrinsics.checkExpressionValueIsNotNull(var3, "super.getValue(key)");
         }

         return var3;
      }

      public void update(TelemetryConfiguration var1) {
         Intrinsics.checkParameterIsNotNull(var1, "configuration");
         super.update(var1);
         Context var2 = var1.getContext();
         Intrinsics.checkExpressionValueIsNotNull(var2, "context");
         this.addCustomPing$app_focusWebkitRelease(var1, (TelemetryMeasurement)(new TelemetryWrapper.ThemeToyMeasurement(var2)));
         this.addCustomPing$app_focusWebkitRelease(var1, (TelemetryMeasurement)(new TelemetryWrapper.CaptureCountMeasurement(var2)));
      }
   }

   public static final class EventBuilder {
      public static final TelemetryWrapper.EventBuilder.Companion Companion = new TelemetryWrapper.EventBuilder.Companion((DefaultConstructorMarker)null);
      private FirebaseEvent firebaseEvent;
      private TelemetryEvent telemetryEvent;

      public EventBuilder(String var1, String var2, String var3, String var4) {
         Intrinsics.checkParameterIsNotNull(var1, "category");
         Intrinsics.checkParameterIsNotNull(var2, "method");
         super();
         Companion.lazyInit();
         StringBuilder var5 = new StringBuilder();
         var5.append("EVENT:");
         var5.append(var1);
         var5.append('/');
         var5.append(var2);
         var5.append('/');
         var5.append(var3);
         var5.append('/');
         var5.append(var4);
         Log.d("TelemetryWrapper", var5.toString());
         TelemetryEvent var6 = TelemetryEvent.create(var1, var2, var3, var4);
         Intrinsics.checkExpressionValueIsNotNull(var6, "TelemetryEvent.create(ca… method, `object`, value)");
         this.telemetryEvent = var6;
         FirebaseEvent var7 = FirebaseEvent.create(var1, var2, var3, var4);
         Intrinsics.checkExpressionValueIsNotNull(var7, "FirebaseEvent.create(cat… method, `object`, value)");
         this.firebaseEvent = var7;
      }

      // $FF: synthetic method
      public EventBuilder(String var1, String var2, String var3, String var4, int var5, DefaultConstructorMarker var6) {
         if ((var5 & 8) != 0) {
            var4 = (String)null;
         }

         this(var1, var2, var3, var4);
      }

      // $FF: synthetic method
      public static void queue$default(TelemetryWrapper.EventBuilder var0, boolean var1, int var2, Object var3) {
         if ((var2 & 1) != 0) {
            var1 = false;
         }

         var0.queue(var1);
      }

      private final void sendEventNow(TelemetryEvent var1) {
         Telemetry var2 = TelemetryHolder.get();
         TelemetryPingBuilder var3 = (TelemetryPingBuilder)null;
         Intrinsics.checkExpressionValueIsNotNull(var2, "telemetry");
         Iterator var4 = var2.getBuilders().iterator();

         while(var4.hasNext()) {
            TelemetryPingBuilder var5 = (TelemetryPingBuilder)var4.next();
            if (var5 instanceof TelemetryEventPingBuilder) {
               var3 = var5;
            }
         }

         if (var3 != null) {
            TelemetryEventPingBuilder var6 = (TelemetryEventPingBuilder)var3;
            EventsMeasurement var8 = var6.getEventsMeasurement();
            Intrinsics.checkExpressionValueIsNotNull(var8, "(focusEventBuilder as Te…uilder).eventsMeasurement");
            String var7 = var6.getType();
            Intrinsics.checkExpressionValueIsNotNull(var7, "focusEventBuilder.type");
            var8.add(var1);
            var2.queuePing(var7).scheduleUpload();
         } else {
            throw (Throwable)(new IllegalStateException("Expect either TelemetryEventPingBuilder or TelemetryMobileEventPingBuilder to be added to queue events"));
         }
      }

      public final TelemetryWrapper.EventBuilder extra(String var1, String var2) {
         Intrinsics.checkParameterIsNotNull(var1, "key");
         Intrinsics.checkParameterIsNotNull(var2, "value");
         StringBuilder var3 = new StringBuilder();
         var3.append("EXTRA:");
         var3.append(var1);
         var3.append('/');
         var3.append(var2);
         Log.d("TelemetryWrapper", var3.toString());
         this.telemetryEvent.extra(var1, var2);
         this.firebaseEvent.param(var1, var2);
         return this;
      }

      public final void queue(boolean var1) {
         Telemetry var2 = TelemetryHolder.get();
         Intrinsics.checkExpressionValueIsNotNull(var2, "TelemetryHolder.get()");
         TelemetryConfiguration var4 = var2.getConfiguration();
         Intrinsics.checkExpressionValueIsNotNull(var4, "TelemetryHolder.get().configuration");
         Context var5 = var4.getContext();
         if (var5 != null) {
            if (var1) {
               Telemetry var3 = TelemetryHolder.get();
               Intrinsics.checkExpressionValueIsNotNull(var3, "TelemetryHolder.get()");
               TelemetryConfiguration var6 = var3.getConfiguration();
               Intrinsics.checkExpressionValueIsNotNull(var6, "TelemetryHolder.get().configuration");
               var6.setMinimumEventsForUpload(1);
               this.sendEventNow(this.telemetryEvent);
            } else {
               this.telemetryEvent.queue();
            }

            this.firebaseEvent.event(var5);
         }

      }

      public static final class Companion {
         private Companion() {
         }

         // $FF: synthetic method
         public Companion(DefaultConstructorMarker var1) {
            this();
         }

         public final void lazyInit() {
            if (!FirebaseEvent.isInitialized()) {
               Telemetry var1 = TelemetryHolder.get();
               Intrinsics.checkExpressionValueIsNotNull(var1, "TelemetryHolder.get()");
               TelemetryConfiguration var5 = var1.getConfiguration();
               Intrinsics.checkExpressionValueIsNotNull(var5, "TelemetryHolder.get().configuration");
               Context var2 = var5.getContext();
               if (var2 != null) {
                  HashMap var6 = new HashMap();
                  Map var3 = (Map)var6;
                  String var4 = var2.getString(2131755325);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.string.pref_key_search_engine)");
                  var3.put(var4, "search_engine");
                  var4 = var2.getString(2131755334);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.string.pref_s_news)");
                  var3.put(var4, "pref_s_news");
                  var4 = var2.getString(2131755319);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…ef_key_privacy_block_ads)");
                  var3.put(var4, "privacy_ads");
                  var4 = var2.getString(2131755320);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…_privacy_block_analytics)");
                  var3.put(var4, "privacy_analytics");
                  var4 = var2.getString(2131755322);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…key_privacy_block_social)");
                  var3.put(var4, "privacy_social");
                  var4 = var2.getString(2131755321);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…_key_privacy_block_other)");
                  var3.put(var4, "privacy_other");
                  var4 = var2.getString(2131755332);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.string.pref_key_turbo_mode)");
                  var3.put(var4, "turbo_mode");
                  var4 = var2.getString(2131755317);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…rformance_block_webfonts)");
                  var3.put(var4, "block_webfonts");
                  var4 = var2.getString(2131755316);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…performance_block_images)");
                  var3.put(var4, "block_images");
                  var4 = var2.getString(2131755303);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…pref_key_default_browser)");
                  var3.put(var4, "default_browser");
                  var4 = var2.getString(2131755331);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.string.pref_key_telemetry)");
                  var3.put(var4, "telemetry");
                  var4 = var2.getString(2131755310);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.string.pref_key_give_feedback)");
                  var3.put(var4, "give_feedback");
                  var4 = var2.getString(2131755327);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…f_key_share_with_friends)");
                  var3.put(var4, "share_with_friends");
                  var4 = var2.getString(2131755295);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.string.pref_key_about)");
                  var3.put(var4, "key_about");
                  var4 = var2.getString(2131755311);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.string.pref_key_help)");
                  var3.put(var4, "help");
                  var4 = var2.getString(2131755324);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.string.pref_key_rights)");
                  var3.put(var4, "rights");
                  var4 = var2.getString(2131755333);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…pref_key_webview_version)");
                  var3.put(var4, "webview_version");
                  var4 = var2.getString(2131755299);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…ey_data_saving_block_ads)");
                  var3.put(var4, "saving_block_ads");
                  var4 = var2.getString(2131755302);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…ta_saving_block_webfonts)");
                  var3.put(var4, "data_webfont");
                  var4 = var2.getString(2131755300);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…data_saving_block_images)");
                  var3.put(var4, "data_images");
                  var4 = var2.getString(2131755301);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…saving_block_tab_restore)");
                  var3.put(var4, "tab_restore");
                  var4 = var2.getString(2131755329);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…rage_clear_browsing_data)");
                  var3.put(var4, "clear_browsing_data)");
                  var4 = var2.getString(2131755323);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…rage_available_on_create)");
                  var3.put(var4, "remove_storage");
                  var4 = var2.getString(2131755330);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…torage_save_downloads_to)");
                  var3.put(var4, "save_downloads_to");
                  var4 = var2.getString(2131755328);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…y_showed_storage_message)");
                  var3.put(var4, "storage_message)");
                  var4 = var2.getString(2131755335);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…e_clear_browsing_history)");
                  var3.put(var4, "clear_browsing_his");
                  var4 = var2.getString(2131755338);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…value_clear_form_history)");
                  var3.put(var4, "clear_form_his");
                  var4 = var2.getString(2131755337);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…pref_value_clear_cookies)");
                  var3.put(var4, "clear_cookies");
                  var4 = var2.getString(2131755336);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.string.pref_value_clear_cache)");
                  var3.put(var4, "clear_cache");
                  var4 = var2.getString(2131755340);
                  Intrinsics.checkExpressionValueIsNotNull(var4, "context.getString(R.stri…alue_saving_path_sd_card)");
                  var3.put(var4, "path_sd_card");
                  String var7 = var2.getString(2131755339);
                  Intrinsics.checkExpressionValueIsNotNull(var7, "context.getString(R.stri…ng_path_internal_storage)");
                  var3.put(var7, "path_internal_storage");
                  FirebaseEvent.setPrefKeyWhitelist(var6);
               }
            }
         }
      }
   }

   public static enum FIND_IN_PAGE {
      CLICK_NEXT,
      CLICK_PREVIOUS,
      OPEN_BY_MENU;

      static {
         TelemetryWrapper.FIND_IN_PAGE var0 = new TelemetryWrapper.FIND_IN_PAGE("OPEN_BY_MENU", 0);
         OPEN_BY_MENU = var0;
         TelemetryWrapper.FIND_IN_PAGE var1 = new TelemetryWrapper.FIND_IN_PAGE("CLICK_PREVIOUS", 1);
         CLICK_PREVIOUS = var1;
         TelemetryWrapper.FIND_IN_PAGE var2 = new TelemetryWrapper.FIND_IN_PAGE("CLICK_NEXT", 2);
         CLICK_NEXT = var2;
      }
   }

   private static final class ThemeToyMeasurement extends TelemetryMeasurement {
      public static final TelemetryWrapper.ThemeToyMeasurement.Companion Companion = new TelemetryWrapper.ThemeToyMeasurement.Companion((DefaultConstructorMarker)null);
      private static final String MEASUREMENT_CURRENT_THEME = "current_theme";
      private Context context;

      public ThemeToyMeasurement(Context var1) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         super(MEASUREMENT_CURRENT_THEME);
         this.context = var1;
      }

      public Object flush() {
         String var1 = ThemeManager.getCurrentThemeName(this.context);
         Intrinsics.checkExpressionValueIsNotNull(var1, "ThemeManager.getCurrentThemeName(context)");
         return var1;
      }

      public static final class Companion {
         private Companion() {
         }

         // $FF: synthetic method
         public Companion(DefaultConstructorMarker var1) {
            this();
         }
      }
   }
}
