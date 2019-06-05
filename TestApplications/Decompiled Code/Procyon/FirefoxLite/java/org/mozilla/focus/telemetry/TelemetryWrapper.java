// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.telemetry;

import org.mozilla.rocket.theme.ThemeManager;
import org.mozilla.telemetry.measurement.EventsMeasurement;
import java.util.Iterator;
import android.util.Log;
import org.mozilla.telemetry.event.TelemetryEvent;
import java.util.Set;
import java.util.HashMap;
import kotlin.io.CloseableKt;
import kotlin.Unit;
import android.database.Cursor;
import java.io.Closeable;
import org.mozilla.focus.provider.ScreenshotContract;
import org.mozilla.telemetry.measurement.TelemetryMeasurement;
import android.os.StrictMode$ThreadPolicy;
import org.mozilla.telemetry.ping.TelemetryEventPingBuilder;
import org.mozilla.telemetry.ping.TelemetryPingBuilder;
import org.mozilla.telemetry.ping.TelemetryCorePingBuilder;
import org.mozilla.telemetry.schedule.TelemetryScheduler;
import org.mozilla.telemetry.net.TelemetryClient;
import org.mozilla.telemetry.storage.TelemetryStorage;
import org.mozilla.telemetry.schedule.jobscheduler.JobSchedulerTelemetryScheduler;
import org.mozilla.telemetry.net.HttpURLConnectionTelemetryClient;
import org.mozilla.telemetry.serialize.TelemetryPingSerializer;
import org.mozilla.telemetry.serialize.JSONPingSerializer;
import org.mozilla.telemetry.storage.FileTelemetryStorage;
import org.mozilla.telemetry.measurement.SettingsMeasurement;
import android.os.StrictMode;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.Browsers;
import android.content.SharedPreferences;
import android.content.res.Resources;
import org.mozilla.threadutils.ThreadUtils;
import android.preference.PreferenceManager;
import org.mozilla.focus.search.SearchEngine;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.Telemetry;
import org.mozilla.focus.search.SearchEngineManager;
import org.mozilla.telemetry.TelemetryHolder;
import org.mozilla.focus.Inject;
import kotlin.NoWhenBranchMatchedException;
import org.mozilla.telemetry.measurement.DefaultSearchMeasurement;
import android.content.Context;
import org.mozilla.focus.utils.AdjustHelper;
import kotlin.jvm.internal.Intrinsics;
import java.util.concurrent.atomic.AtomicInteger;

public final class TelemetryWrapper
{
    public static final TelemetryWrapper INSTANCE;
    private static final AtomicInteger sRefCount;
    
    static {
        INSTANCE = new TelemetryWrapper();
        sRefCount = new AtomicInteger(0);
    }
    
    private TelemetryWrapper() {
    }
    
    public static final void addNewTabFromContextMenu() {
        EventBuilder.queue$default(new EventBuilder("action", "add", "browser_contextmenu", "link"), false, 1, null);
    }
    
    public static final void addNewTabFromHome() {
        EventBuilder.queue$default(new EventBuilder("action", "add", "tab", "home"), false, 1, null);
    }
    
    public static final void bookmarkEditItem() {
        EventBuilder.queue$default(new EventBuilder("action", "edit", "panel", "bookmark"), false, 1, null);
    }
    
    public static final void bookmarkOpenItem() {
        EventBuilder.queue$default(new EventBuilder("action", "open", "panel", "bookmark"), false, 1, null);
    }
    
    public static final void bookmarkRemoveItem() {
        EventBuilder.queue$default(new EventBuilder("action", "remove", "panel", "bookmark"), false, 1, null);
    }
    
    public static final void browseEnterFullScreenEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "fullscreen", "browser", "enter"), false, 1, null);
    }
    
    private final void browseEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "open", "search_bar", "link"), false, 1, null);
    }
    
    public static final void browseExitFullScreenEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "fullscreen", "browser", "exit"), false, 1, null);
    }
    
    public static final void browseFilePermissionEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "permission", "browser", "file"), false, 1, null);
    }
    
    public static final void browseGeoLocationPermissionEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "permission", "browser", "geolocation"), false, 1, null);
    }
    
    public static final void browsePermissionEvent(final String[] array) {
        Intrinsics.checkParameterIsNotNull(array, "requests");
        for (final String s : array) {
            final int hashCode = s.hashCode();
            String s2;
            if (hashCode != -1660821873) {
                if (hashCode != 968612586) {
                    if (hashCode != 1069496794) {
                        if (hashCode != 1233677653) {
                            s2 = s;
                        }
                        else {
                            s2 = s;
                            if (s.equals("android.webkit.resource.MIDI_SYSEX")) {
                                s2 = "midi";
                            }
                        }
                    }
                    else {
                        s2 = s;
                        if (s.equals("android.webkit.resource.PROTECTED_MEDIA_ID")) {
                            s2 = "eme";
                        }
                    }
                }
                else {
                    s2 = s;
                    if (s.equals("android.webkit.resource.AUDIO_CAPTURE")) {
                        s2 = "audio";
                    }
                }
            }
            else {
                s2 = s;
                if (s.equals("android.webkit.resource.VIDEO_CAPTURE")) {
                    s2 = "video";
                }
            }
            EventBuilder.queue$default(new EventBuilder("action", "permission", "browser", s2), false, 1, null);
        }
    }
    
    public static final void cancelWebContextMenuEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "cancel", "browser_contextmenu", null, 8, null), false, 1, null);
    }
    
    public static final void changeThemeTo(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "themeName");
        EventBuilder.queue$default(new EventBuilder("action", "change", "themetoy", null, 8, null).extra("to", s), false, 1, null);
    }
    
    public static final void clearHistory() {
        EventBuilder.queue$default(new EventBuilder("action", "clear", "panel", "history"), false, 1, null);
    }
    
    public static final void clickAddTabToolbar() {
        EventBuilder.queue$default(new EventBuilder("action", "add", "tab", "toolbar"), false, 1, null);
    }
    
    public static final void clickAddTabTray() {
        EventBuilder.queue$default(new EventBuilder("action", "add", "tab", "tab_tray"), false, 1, null);
    }
    
    public static final void clickAddToHome() {
        EventBuilder.queue$default(new EventBuilder("action", "pin_shortcut", "toolbar", "link"), false, 1, null);
        AdjustHelper.trackEvent("g1tpbz");
    }
    
    public static final void clickBannerBackground(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "pageId");
        EventBuilder.queue$default(new EventBuilder("action", "click", "banner", "background").extra("source", s), false, 1, null);
    }
    
    public static final void clickBannerItem(String string, final int i) {
        Intrinsics.checkParameterIsNotNull(string, "pageId");
        final EventBuilder extra = new EventBuilder("action", "click", "banner", "item").extra("source", string);
        string = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(string, "Integer.toString(itemPosition)");
        EventBuilder.queue$default(extra.extra("on", string), false, 1, null);
    }
    
    public static final void clickDefaultBrowserInSetting() {
        AdjustHelper.trackEvent("iyqe7a");
    }
    
    public static final void clickDefaultSettingNotification() {
        final EventBuilder extra = new EventBuilder("action", "click", "default_browser", null, 8, null).extra("source", "notification");
        final String string = Integer.toString(2);
        Intrinsics.checkExpressionValueIsNotNull(string, "Integer.toString(DEFAULT\u2026CATION_TELEMETRY_VERSION)");
        EventBuilder.queue$default(extra.extra("version", string), false, 1, null);
    }
    
    public static final void clickMenuBookmark() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "bookmark"), false, 1, null);
    }
    
    public static final void clickMenuCapture() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "capture"), false, 1, null);
    }
    
    public static final void clickMenuClearCache() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "clear_cache"), false, 1, null);
    }
    
    public static final void clickMenuDownload() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "download"), false, 1, null);
    }
    
    public static final void clickMenuExit() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "exit"), false, 1, null);
    }
    
    public static final void clickMenuHistory() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "history"), false, 1, null);
    }
    
    public static final void clickMenuSettings() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "settings"), false, 1, null);
    }
    
    public static final void clickQuickSearchEngine(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "engineName");
        EventBuilder.queue$default(new EventBuilder("search", "click", "quicksearch", null).extra("engine", s), false, 1, null);
    }
    
    public static final void clickRateApp(final String s, final String s2) {
        Intrinsics.checkParameterIsNotNull(s2, "source");
        EventBuilder.queue$default(new EventBuilder("action", "click", "feedback", s).extra("source", s2).extra("version", String.valueOf(3)), false, 1, null);
        if (Intrinsics.areEqual("positive", s)) {
            AdjustHelper.trackEvent("uivfof");
        }
    }
    
    public static final void clickTabFromTabTray() {
        EventBuilder.queue$default(new EventBuilder("action", "change", "tab", "tab_tray"), false, 1, null);
    }
    
    public static final void clickToolbarBookmark(final boolean b) {
        final EventBuilder eventBuilder = new EventBuilder("action", "share", "toolbar", "bookmark");
        final String string = Boolean.toString(b);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Boolean.toString(isAdd)");
        EventBuilder.queue$default(eventBuilder.extra("to", string), false, 1, null);
        if (b) {
            AdjustHelper.trackEvent("sj5vxj");
        }
    }
    
    public static final void clickToolbarCapture(final String s, final int i) {
        Intrinsics.checkParameterIsNotNull(s, "category");
        final EventBuilder eventBuilder = new EventBuilder("action", "click", "toolbar", "capture");
        final String string = Integer.toString(3);
        Intrinsics.checkExpressionValueIsNotNull(string, "Integer.toString(TOOL_BA\u2026APTURE_TELEMETRY_VERSION)");
        final EventBuilder extra = eventBuilder.extra("version", string).extra("category", s);
        final String string2 = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(string2, "Integer.toString(categoryVersion)");
        EventBuilder.queue$default(extra.extra("category_versio", string2), false, 1, null);
        AdjustHelper.trackEvent("ky29gk");
    }
    
    public static final void clickToolbarForward() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "toolbar", "forward"), false, 1, null);
    }
    
    public static final void clickToolbarReload() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "toolbar", "reload"), false, 1, null);
    }
    
    public static final void clickToolbarSearch() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "search_bar", "search_btn"), false, 1, null);
    }
    
    public static final void clickToolbarShare() {
        EventBuilder.queue$default(new EventBuilder("action", "share", "toolbar", "link"), false, 1, null);
    }
    
    public static final void clickTopSiteOn(final int i, final String s) {
        Intrinsics.checkParameterIsNotNull(s, "source");
        final EventBuilder eventBuilder = new EventBuilder("action", "open", "home", "link");
        final String string = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(string, "Integer.toString(index)");
        EventBuilder.queue$default(eventBuilder.extra("on", string).extra("source", s).extra("version", "2"), false, 1, null);
        EventBuilder.queue$default(new EventBuilder("action", "add", "tab", "top_site"), false, 1, null);
    }
    
    public static final void clickUrlbar() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "search_bar", "mini_urlbar"), false, 1, null);
    }
    
    public static final void clickVpnRecommend(final boolean b) {
        String s;
        if (b) {
            s = "positive";
        }
        else {
            s = "negative";
        }
        EventBuilder.queue$default(new EventBuilder("action", "click", "vpn_doorhanger", s), false, 1, null);
    }
    
    public static final void clickVpnRecommender(final boolean b) {
        final EventBuilder eventBuilder = new EventBuilder("action", "click", "home", "vpn_recommend");
        final String string = Boolean.toString(b);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Boolean.toString(installed)");
        EventBuilder.queue$default(eventBuilder.extra("vpn_installed", string), false, 1, null);
    }
    
    public static final void clickVpnSurvey() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "home", "vpn"), false, 1, null);
    }
    
    public static final void clickWifiFinderSurvey() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "home", "wifi_finder"), false, 1, null);
    }
    
    public static final void closeAllTabFromTabTray() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "close_all", "tab_tray"), false, 1, null);
    }
    
    public static final void closeTabFromTabTray() {
        EventBuilder.queue$default(new EventBuilder("action", "remove", "tab", "tab_tray"), false, 1, null);
    }
    
    public static final void copyImageEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "copy", "browser_contextmenu", "image"), false, 1, null);
    }
    
    public static final void copyLinkEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "copy", "browser_contextmenu", "link"), false, 1, null);
    }
    
    private final DefaultSearchMeasurement.DefaultSearchEngineProvider createDefaultSearchProvider(final Context context) {
        return (DefaultSearchMeasurement.DefaultSearchEngineProvider)new TelemetryWrapper$createDefaultSearchProvider.TelemetryWrapper$createDefaultSearchProvider$1(context);
    }
    
    public static final void deleteCaptureImage(String string, final int i) {
        Intrinsics.checkParameterIsNotNull(string, "category");
        final EventBuilder extra = new EventBuilder("action", "delete", "capture", "image").extra("category", string);
        string = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(string, "Integer.toString(categoryVersion)");
        EventBuilder.queue$default(extra.extra("category_versio", string), false, 1, null);
    }
    
    public static final void dismissVpnRecommend() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "vpn_doorhanger", "dismiss"), false, 1, null);
    }
    
    public static final void downloadDeleteFile() {
        EventBuilder.queue$default(new EventBuilder("action", "delete", "panel", "file"), false, 1, null);
    }
    
    public static final void downloadOpenFile(final boolean b) {
        final EventBuilder eventBuilder = new EventBuilder("action", "open", "panel", "file");
        final String string = Boolean.toString(b);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Boolean.toString(fromSnackBar)");
        EventBuilder.queue$default(eventBuilder.extra("snackbar", string), false, 1, null);
    }
    
    public static final void downloadRemoveFile() {
        EventBuilder.queue$default(new EventBuilder("action", "remove", "panel", "file"), false, 1, null);
    }
    
    public static final void editCaptureImage(final boolean b, final String s, final int i) {
        Intrinsics.checkParameterIsNotNull(s, "category");
        final EventBuilder eventBuilder = new EventBuilder("action", "edit", "capture", "image");
        final String string = Boolean.toString(b);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Boolean.toString(editAppResolved)");
        final EventBuilder extra = eventBuilder.extra("success", string).extra("category", s);
        final String string2 = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(string2, "Integer.toString(categoryVersion)");
        EventBuilder.queue$default(extra.extra("category_versio", string2), false, 1, null);
    }
    
    public static final void erasePrivateModeNotification() {
        EventBuilder.queue$default(new EventBuilder("action", "clear", "private_mode", null, 8, null).extra("source", "notification"), false, 1, null);
    }
    
    public static final void findInPage(final FIND_IN_PAGE find_IN_PAGE) {
        Intrinsics.checkParameterIsNotNull(find_IN_PAGE, "type");
        EventBuilder eventBuilder = null;
        switch (TelemetryWrapper$WhenMappings.$EnumSwitchMapping$0[find_IN_PAGE.ordinal()]) {
            default: {
                throw new NoWhenBranchMatchedException();
            }
            case 3: {
                eventBuilder = TelemetryWrapper.INSTANCE.clickFindInPageNext$app_focusWebkitRelease();
                break;
            }
            case 2: {
                eventBuilder = TelemetryWrapper.INSTANCE.clickFindInPagePrevious$app_focusWebkitRelease();
                break;
            }
            case 1: {
                eventBuilder = TelemetryWrapper.INSTANCE.clickMenuFindInPage$app_focusWebkitRelease();
                break;
            }
        }
        EventBuilder.queue$default(eventBuilder, false, 1, null);
    }
    
    public static final void finishFirstRunEvent(final long i, final int j) {
        final EventBuilder eventBuilder = new EventBuilder("action", "show", "firstrun", "finish");
        final String string = Long.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Long.toString(duration)");
        final EventBuilder extra = eventBuilder.extra("on", string);
        final String string2 = Integer.toString(j);
        Intrinsics.checkExpressionValueIsNotNull(string2, "Integer.toString(mode)");
        EventBuilder.queue$default(extra.extra("mode", string2), false, 1, null);
    }
    
    public static final void historyOpenLink() {
        EventBuilder.queue$default(new EventBuilder("action", "open", "panel", "link"), false, 1, null);
    }
    
    public static final void historyRemoveLink() {
        EventBuilder.queue$default(new EventBuilder("action", "remove", "panel", "link"), false, 1, null);
    }
    
    public static final boolean isTelemetryEnabled(final Context context) {
        return context != null && Inject.isTelemetryEnabled(context);
    }
    
    public static final void launchByAppLauncherEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "launch", "app", "launcher"), false, 1, null);
    }
    
    public static final void launchByExternalAppEvent(final String s) {
        if (s == null) {
            EventBuilder.queue$default(new EventBuilder("action", "launch", "app", "external_app"), false, 1, null);
        }
        else {
            EventBuilder.queue$default(new EventBuilder("action", "launch", "app", "external_app").extra("type", s), false, 1, null);
        }
    }
    
    public static final void launchByHomeScreenShortcutEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "launch", "app", "shortcut"), false, 1, null);
    }
    
    public static final void longPressDownloadIndicator() {
        EventBuilder.queue$default(new EventBuilder("Downloads", "long_press", "toolbar", "download"), false, 1, null);
    }
    
    public static final void menuBlockImageChangeTo(final boolean b) {
        final EventBuilder eventBuilder = new EventBuilder("action", "change", "menu", "block_image");
        final String string = Boolean.toString(b);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Boolean.toString(enable)");
        EventBuilder.queue$default(eventBuilder.extra("to", string), false, 1, null);
    }
    
    public static final void menuNightModeChangeTo(final boolean b) {
        final EventBuilder eventBuilder = new EventBuilder("action", "change", "menu", "night_mode");
        final String string = Boolean.toString(b);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Boolean.toString(enable)");
        EventBuilder.queue$default(eventBuilder.extra("to", string), false, 1, null);
    }
    
    public static final void menuTurboChangeTo(final boolean b) {
        final EventBuilder eventBuilder = new EventBuilder("action", "change", "menu", "turbo");
        final String string = Boolean.toString(b);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Boolean.toString(enable)");
        EventBuilder.queue$default(eventBuilder.extra("to", string), false, 1, null);
    }
    
    public static final void nightModeBrightnessChangeTo(final int i, final boolean b) {
        final EventBuilder eventBuilder = new EventBuilder("action", "change", "setting", "night_mode_brightness");
        String s;
        if (b) {
            s = "setting";
        }
        else {
            s = "menu";
        }
        EventBuilder.queue$default(eventBuilder.extra("source", s).extra("to", String.valueOf(i)), false, 1, null);
    }
    
    public static final void onDefaultBrowserServiceFailed() {
        final EventBuilder eventBuilder = new EventBuilder("action", "change", "default_browser", null, 8, null);
        final String string = Boolean.toString(false);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Boolean.toString(false)");
        EventBuilder.queue$default(eventBuilder.extra("success", string), false, 1, null);
    }
    
    public static final void openCapture() {
        EventBuilder.queue$default(new EventBuilder("action", "open", "panel", "capture"), false, 1, null);
    }
    
    public static final void openCaptureLink(final String s, final int i) {
        Intrinsics.checkParameterIsNotNull(s, "category");
        final EventBuilder extra = new EventBuilder("action", "open", "capture", "link").extra("category", s);
        final String string = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(string, "Integer.toString(categoryVersion)");
        EventBuilder.queue$default(extra.extra("category_versio", string), false, 1, null);
    }
    
    public static final void openLifeFeedEc() {
        EventBuilder.queue$default(new EventBuilder("action", "open", "panel", "lifefeed_ec"), false, 1, null);
    }
    
    public static final void openLifeFeedNews() {
        EventBuilder.queue$default(new EventBuilder("action", "open", "panel", "lifefeed_news"), false, 1, null);
    }
    
    public static final void openWebContextMenuEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "long_press", "browser", null, 8, null), false, 1, null);
    }
    
    public static final void privateModeTray() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "private_mode", "tab_tray"), false, 1, null);
    }
    
    public static final void promoteShareClickEvent(final String s, final String s2) {
        Intrinsics.checkParameterIsNotNull(s, "value");
        Intrinsics.checkParameterIsNotNull(s2, "source");
        EventBuilder.queue$default(new EventBuilder("action", "click", "promote_share", s).extra("source", s2), false, 1, null);
        if (Intrinsics.areEqual("share", s)) {
            AdjustHelper.trackEvent("3obefy");
        }
    }
    
    public static final void receiveFirstrunConfig(final long l, String s) {
        final EventBuilder extra = new EventBuilder("action", "get", "firstrun_push", null, 8, null).extra("delay", String.valueOf(l));
        if (s == null) {
            s = "";
        }
        EventBuilder.queue$default(extra.extra("message", s), false, 1, null);
    }
    
    public static final void removeTopSite(final boolean b) {
        final EventBuilder eventBuilder = new EventBuilder("action", "remove", "home", "link");
        final String string = Boolean.toString(b);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Boolean.toString(isDefault)");
        EventBuilder.queue$default(eventBuilder.extra("default", string), false, 1, null);
    }
    
    public static final void resetThemeToDefault() {
        EventBuilder.queue$default(new EventBuilder("action", "reset", "themetoy", null, 8, null).extra("to", "default"), false, 1, null);
    }
    
    public static final void saveImageEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "save", "browser_contextmenu", "image"), false, 1, null);
    }
    
    public static final void searchClear() {
        EventBuilder.queue$default(new EventBuilder("action", "clear", "search_bar", null, 8, null).extra("version", "2"), false, 1, null);
    }
    
    public static final void searchDismiss() {
        EventBuilder.queue$default(new EventBuilder("action", "cancel", "search_bar", null, 8, null).extra("version", "2"), false, 1, null);
    }
    
    private final void searchEnterEvent() {
        final Telemetry value = TelemetryHolder.get();
        EventBuilder.queue$default(new EventBuilder("action", "type_query", "search_bar", null, 8, null), false, 1, null);
        final SearchEngineManager instance = SearchEngineManager.getInstance();
        Intrinsics.checkExpressionValueIsNotNull(value, "telemetry");
        final TelemetryConfiguration configuration = value.getConfiguration();
        Intrinsics.checkExpressionValueIsNotNull(configuration, "telemetry.configuration");
        final SearchEngine defaultSearchEngine = instance.getDefaultSearchEngine(configuration.getContext());
        Intrinsics.checkExpressionValueIsNotNull(defaultSearchEngine, "searchEngine");
        value.recordSearch("actionbar", defaultSearchEngine.getIdentifier());
    }
    
    public static final void searchSelectEvent() {
        final Telemetry value = TelemetryHolder.get();
        EventBuilder.queue$default(new EventBuilder("action", "select_query", "search_bar", null, 8, null), false, 1, null);
        final SearchEngineManager instance = SearchEngineManager.getInstance();
        Intrinsics.checkExpressionValueIsNotNull(value, "telemetry");
        final TelemetryConfiguration configuration = value.getConfiguration();
        Intrinsics.checkExpressionValueIsNotNull(configuration, "telemetry.configuration");
        final SearchEngine defaultSearchEngine = instance.getDefaultSearchEngine(configuration.getContext());
        Intrinsics.checkExpressionValueIsNotNull(defaultSearchEngine, "searchEngine");
        value.recordSearch("suggestion", defaultSearchEngine.getIdentifier());
    }
    
    public static final void searchSuggestionLongClick() {
        EventBuilder.queue$default(new EventBuilder("action", "long_press", "search_suggestion", null, 8, null), false, 1, null);
    }
    
    public static final void setTelemetryEnabled(final Context context, final boolean b) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        final Resources resources = context.getResources();
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String string = resources.getString(2131755331);
        defaultSharedPreferences.edit().putBoolean(string, b).apply();
        ThreadUtils.postToBackgroundThread((Runnable)new TelemetryWrapper$setTelemetryEnabled.TelemetryWrapper$setTelemetryEnabled$1(string, b));
    }
    
    public static final void settingsClickEvent(String validPrefKey) {
        Intrinsics.checkParameterIsNotNull(validPrefKey, "key");
        validPrefKey = FirebaseEvent.getValidPrefKey(validPrefKey);
        if (validPrefKey != null) {
            EventBuilder.queue$default(new EventBuilder("action", "click", "setting", validPrefKey), false, 1, null);
        }
    }
    
    public static final void settingsEvent(String validPrefKey, final String s, final boolean b) {
        Intrinsics.checkParameterIsNotNull(validPrefKey, "key");
        Intrinsics.checkParameterIsNotNull(s, "value");
        validPrefKey = FirebaseEvent.getValidPrefKey(validPrefKey);
        if (validPrefKey != null) {
            new EventBuilder("action", "change", "setting", validPrefKey).extra("to", s).queue(b);
        }
    }
    
    public static final void settingsLearnMoreClickEvent(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "source");
        EventBuilder.queue$default(new EventBuilder("action", "click", "setting", "learn_more").extra("source", s), false, 1, null);
    }
    
    public static final void settingsLocaleChangeEvent(final String s, String string, final boolean b) {
        Intrinsics.checkParameterIsNotNull(s, "key");
        Intrinsics.checkParameterIsNotNull(string, "value");
        final EventBuilder extra = new EventBuilder("action", "change", "setting", s).extra("to", string);
        string = Boolean.toString(b);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Boolean.toString(isDefault)");
        EventBuilder.queue$default(extra.extra("default", string), false, 1, null);
    }
    
    public static final void shareCaptureImage(final boolean b, final String s, final int i) {
        Intrinsics.checkParameterIsNotNull(s, "category");
        final EventBuilder eventBuilder = new EventBuilder("action", "share", "capture", "image");
        final String string = Boolean.toString(b);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Boolean.toString(fromSnackBar)");
        final EventBuilder extra = eventBuilder.extra("snackbar", string).extra("category", s);
        final String string2 = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(string2, "Integer.toString(categoryVersion)");
        EventBuilder.queue$default(extra.extra("category_versio", string2), false, 1, null);
    }
    
    public static final void shareImageEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "share", "browser_contextmenu", "image"), false, 1, null);
    }
    
    public static final void shareLinkEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "share", "browser_contextmenu", "link"), false, 1, null);
    }
    
    public static final void showBannerNew(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "id");
        EventBuilder.queue$default(new EventBuilder("action", "show", "banner", "new").extra("to", s), false, 1, null);
    }
    
    public static final void showBannerReturn(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "id");
        EventBuilder.queue$default(new EventBuilder("action", "show", "banner", "return").extra("to", s), false, 1, null);
    }
    
    public static final void showBannerSwipe(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "id");
        EventBuilder.queue$default(new EventBuilder("action", "show", "banner", "swipe").extra("to", s), false, 1, null);
    }
    
    public static final void showBannerUpdate(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "id");
        EventBuilder.queue$default(new EventBuilder("action", "show", "banner", "update").extra("to", s), false, 1, null);
    }
    
    public static final void showBookmarkContextMenu() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "menu", "bookmark"), false, 1, null);
    }
    
    public static final void showCaptureInfo(final String s, final int i) {
        Intrinsics.checkParameterIsNotNull(s, "category");
        final EventBuilder extra = new EventBuilder("action", "show", "capture", "info").extra("category", s);
        final String string = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(string, "Integer.toString(categoryVersion)");
        EventBuilder.queue$default(extra.extra("category_versio", string), false, 1, null);
    }
    
    public static final void showDefaultSettingNotification() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "default_browser", null, 8, null).extra("source", "notification"), false, 1, null);
    }
    
    public static final void showFileContextMenu() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "menu", "download"), false, 1, null);
    }
    
    public static final void showFirstrunNotification(final long l, String s) {
        final EventBuilder extra = new EventBuilder("action", "show", "firstrun_push", null, 8, null).extra("delay", String.valueOf(l));
        if (s == null) {
            s = "";
        }
        EventBuilder.queue$default(extra.extra("message", s), false, 1, null);
    }
    
    public static final void showHistoryContextMenu() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "menu", "history"), false, 1, null);
    }
    
    public static final void showHome() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "home", null, 8, null), false, 1, null);
    }
    
    public static final void showMenuHome() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "menu", "home"), false, 1, null);
    }
    
    public static final void showMenuToolbar() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "menu", "toolbar"), false, 1, null);
    }
    
    public static final void showPanelBookmark() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "panel", "bookmark"), false, 1, null);
    }
    
    public static final void showPanelCapture() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "panel", "capture"), false, 1, null);
    }
    
    public static final void showPanelDownload() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "panel", "download"), false, 1, null);
    }
    
    public static final void showPanelHistory() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "panel", "history"), false, 1, null);
    }
    
    public static final void showPromoteShareDialog() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "promote_share", null, 8, null), false, 1, null);
    }
    
    public static final void showRateApp(final boolean b) {
        final EventBuilder eventBuilder = new EventBuilder("action", "show", "feedback", null, 8, null);
        if (b) {
            eventBuilder.extra("source", "notification");
        }
        EventBuilder.queue$default(eventBuilder, false, 1, null);
    }
    
    public static final void showSearchBarHome() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "search_bar", "search_box"), false, 1, null);
    }
    
    public static final void showTabTrayHome() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "tab_tray", "home"), false, 1, null);
    }
    
    public static final void showTabTrayToolbar() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "tab_tray", "toolbar"), false, 1, null);
    }
    
    public static final void showVpnRecommender(final boolean b) {
        final EventBuilder eventBuilder = new EventBuilder("action", "show", "home", "vpn_recommend");
        final String string = Boolean.toString(b);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Boolean.toString(installed)");
        EventBuilder.queue$default(eventBuilder.extra("vpn_installed", string), false, 1, null);
    }
    
    public static final void startSession() {
        if (TelemetryWrapper.sRefCount.getAndIncrement() == 0) {
            TelemetryHolder.get().recordSessionStart();
        }
        EventBuilder.queue$default(new EventBuilder("action", "foreground", "app", null, 8, null), false, 1, null);
    }
    
    public static final void stopMainActivity() {
        TelemetryHolder.get().queuePing("core").queuePing("focus-event").scheduleUpload();
    }
    
    public static final void stopSession() {
        if (TelemetryWrapper.sRefCount.decrementAndGet() == 0) {
            TelemetryHolder.get().recordSessionEnd();
        }
        EventBuilder.queue$default(new EventBuilder("action", "background", "app", null, 8, null), false, 1, null);
    }
    
    public static final void surveyResult(final String s, final String s2) {
        Intrinsics.checkParameterIsNotNull(s, "result");
        Intrinsics.checkParameterIsNotNull(s2, "source");
        EventBuilder.queue$default(new EventBuilder("action", "click", "doorhanger", s).extra("source", s2), false, 1, null);
    }
    
    public static final void swipeBannerItem(final int i, final int j) {
        final EventBuilder eventBuilder = new EventBuilder("action", "swipe", "banner", "page");
        final String string = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(string, "Integer.toString(directionX)");
        final EventBuilder extra = eventBuilder.extra("direction", string);
        final String string2 = Integer.toString(j);
        Intrinsics.checkExpressionValueIsNotNull(string2, "Integer.toString(toItemPosition)");
        EventBuilder.queue$default(extra.extra("to", string2), false, 1, null);
    }
    
    public static final void swipeTabFromTabTray() {
        EventBuilder.queue$default(new EventBuilder("action", "swipe", "tab", "tab_tray"), false, 1, null);
    }
    
    public static final void toggleFirstRunPageEvent(final boolean b) {
        final EventBuilder eventBuilder = new EventBuilder("action", "change", "firstrun", "turbo");
        final String string = Boolean.toString(b);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Boolean.toString(enableTurboMode)");
        EventBuilder.queue$default(eventBuilder.extra("to", string), false, 1, null);
    }
    
    public static final void togglePrivateMode(final boolean b) {
        String s;
        if (b) {
            s = "enter";
        }
        else {
            s = "exit";
        }
        EventBuilder.queue$default(new EventBuilder("action", "change", "private_mode", s), false, 1, null);
        if (b) {
            AdjustHelper.trackEvent("dccqsh");
        }
    }
    
    private final void updateDefaultBrowserStatus(final Context context) {
        Settings.updatePrefDefaultBrowserIfNeeded(context, Browsers.isDefaultBrowser(context));
    }
    
    public static final void urlBarEvent(final boolean b, final boolean b2) {
        if (b) {
            TelemetryWrapper.INSTANCE.browseEvent();
        }
        else if (b2) {
            searchSelectEvent();
        }
        else {
            TelemetryWrapper.INSTANCE.searchEnterEvent();
        }
    }
    
    public final EventBuilder clickFindInPageNext$app_focusWebkitRelease() {
        final EventBuilder eventBuilder = new EventBuilder("action", "click", "find_in_page", "next");
        final String string = Integer.toString(2);
        Intrinsics.checkExpressionValueIsNotNull(string, "Integer.toString(FIND_IN_PAGE_VERSION)");
        return eventBuilder.extra("version", string);
    }
    
    public final EventBuilder clickFindInPagePrevious$app_focusWebkitRelease() {
        final EventBuilder eventBuilder = new EventBuilder("action", "click", "find_in_page", "previous");
        final String string = Integer.toString(2);
        Intrinsics.checkExpressionValueIsNotNull(string, "Integer.toString(FIND_IN_PAGE_VERSION)");
        return eventBuilder.extra("version", string);
    }
    
    public final EventBuilder clickMenuFindInPage$app_focusWebkitRelease() {
        final EventBuilder eventBuilder = new EventBuilder("action", "click", "menu", "find_in_page");
        final String string = Integer.toString(2);
        Intrinsics.checkExpressionValueIsNotNull(string, "Integer.toString(FIND_IN_PAGE_VERSION)");
        return eventBuilder.extra("version", string);
    }
    
    public final void clickOnEcItem(final String s, String s2, String s3) {
        Intrinsics.checkParameterIsNotNull(s, "pos");
        final EventBuilder extra = new EventBuilder("action", "click", "panel", "lifefeed_ec").extra("position", s);
        if (s2 == null) {
            s2 = "";
        }
        final EventBuilder extra2 = extra.extra("source", s2);
        if (s3 == null) {
            s3 = "";
        }
        EventBuilder.queue$default(extra2.extra("category", s3), false, 1, null);
    }
    
    public final void clickOnNewsItem(final String s, final String s2, String s3, String s4, String s5) {
        Intrinsics.checkParameterIsNotNull(s, "pos");
        Intrinsics.checkParameterIsNotNull(s2, "feed");
        final EventBuilder extra = new EventBuilder("action", "click", "panel", "lifefeed_news").extra("position", s).extra("feed", s2);
        if (s3 == null) {
            s3 = "";
        }
        final EventBuilder extra2 = extra.extra("source", s3);
        if (s4 == null) {
            s4 = "";
        }
        final EventBuilder extra3 = extra2.extra("category", s4);
        if (s5 == null) {
            s5 = "";
        }
        EventBuilder.queue$default(extra3.extra("subcategory", s5), false, 1, null);
    }
    
    public final void init(final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        final StrictMode$ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        try {
            final Resources resources = context.getResources();
            final boolean telemetryEnabled = isTelemetryEnabled(context);
            this.updateDefaultBrowserStatus(context);
            final TelemetryConfiguration setUploadEnabled = new TelemetryConfiguration(context).setServerEndpoint("https://incoming.telemetry.mozilla.org").setAppName("Zerda").setUpdateChannel("release").setPreferencesImportantForTelemetry(resources.getString(2131755325), resources.getString(2131755332), resources.getString(2131755316), resources.getString(2131755303), resources.getString(2131755330), resources.getString(2131755333), resources.getString(2131755334), resources.getString(2131755312)).setSettingsProvider(new CustomSettingsProvider()).setCollectionEnabled(telemetryEnabled).setUploadEnabled(true);
            TelemetryHolder.set(new Telemetry(setUploadEnabled, new FileTelemetryStorage(setUploadEnabled, new JSONPingSerializer()), new HttpURLConnectionTelemetryClient(), new JobSchedulerTelemetryScheduler()).addPingBuilder(new TelemetryCorePingBuilder(setUploadEnabled)).addPingBuilder(new TelemetryEventPingBuilder(setUploadEnabled)).setDefaultSearchProvider(this.createDefaultSearchProvider(context)));
        }
        finally {
            StrictMode.setThreadPolicy(allowThreadDiskWrites);
        }
    }
    
    private static final class CaptureCountMeasurement extends TelemetryMeasurement
    {
        public static final Companion Companion;
        private final Context context;
        
        static {
            Companion = new Companion(null);
        }
        
        public CaptureCountMeasurement(final Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            super("capture_count");
            this.context = context;
        }
        
        @Override
        public Object flush() {
            final Thread currentThread = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
            if (!Intrinsics.areEqual("main", currentThread.getName())) {
                int count;
                try {
                    final Cursor query = this.context.getContentResolver().query(ScreenshotContract.Screenshot.CONTENT_URI, (String[])null, (String)null, (String[])null, (String)null);
                    if (query == null) {
                        Intrinsics.throwNpe();
                    }
                    final Closeable closeable = (Closeable)query;
                    Throwable t2;
                    final Throwable t = t2 = null;
                    try {
                        try {
                            count = ((Cursor)closeable).getCount();
                            t2 = t;
                            final Unit instance = Unit.INSTANCE;
                            CloseableKt.closeFinally(closeable, t);
                        }
                        finally {}
                    }
                    catch (Throwable t3) {
                        throw t3;
                    }
                    CloseableKt.closeFinally(closeable, t2);
                }
                catch (Exception ex) {
                    count = -1;
                }
                return count;
            }
            throw new RuntimeException("Call from main thread exception");
        }
        
        public static final class Companion
        {
            private Companion() {
            }
        }
    }
    
    private static final class CustomSettingsProvider extends SharedPreferenceSettingsProvider
    {
        private final HashMap<String, Object> custom;
        
        public CustomSettingsProvider() {
            this.custom = new HashMap<String, Object>(2);
        }
        
        public final void addCustomPing$app_focusWebkitRelease(final TelemetryConfiguration telemetryConfiguration, final TelemetryMeasurement telemetryMeasurement) {
            Intrinsics.checkParameterIsNotNull(telemetryConfiguration, "configuration");
            Intrinsics.checkParameterIsNotNull(telemetryMeasurement, "measurement");
            Set<String> set;
            if ((set = telemetryConfiguration.getPreferencesImportantForTelemetry()) == null) {
                telemetryConfiguration.setPreferencesImportantForTelemetry(new String[0]);
                set = telemetryConfiguration.getPreferencesImportantForTelemetry();
            }
            if (set == null) {
                Intrinsics.throwNpe();
            }
            final String fieldName = telemetryMeasurement.getFieldName();
            Intrinsics.checkExpressionValueIsNotNull(fieldName, "measurement.fieldName");
            set.add(fieldName);
            final HashMap<String, Object> hashMap = this.custom;
            final String fieldName2 = telemetryMeasurement.getFieldName();
            Intrinsics.checkExpressionValueIsNotNull(fieldName2, "measurement.fieldName");
            final Object flush = telemetryMeasurement.flush();
            Intrinsics.checkExpressionValueIsNotNull(flush, "measurement.flush()");
            hashMap.put(fieldName2, flush);
        }
        
        @Override
        public boolean containsKey(final String key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return this.custom.containsKey(key) | super.containsKey(key);
        }
        
        @Override
        public Object getValue(final String key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            final Object value = this.custom.get(key);
            Object value2;
            if (value != null) {
                value2 = value;
            }
            else {
                value2 = super.getValue(key);
                Intrinsics.checkExpressionValueIsNotNull(value2, "super.getValue(key)");
            }
            return value2;
        }
        
        @Override
        public void update(final TelemetryConfiguration telemetryConfiguration) {
            Intrinsics.checkParameterIsNotNull(telemetryConfiguration, "configuration");
            super.update(telemetryConfiguration);
            final Context context = telemetryConfiguration.getContext();
            Intrinsics.checkExpressionValueIsNotNull(context, "context");
            this.addCustomPing$app_focusWebkitRelease(telemetryConfiguration, new ThemeToyMeasurement(context));
            this.addCustomPing$app_focusWebkitRelease(telemetryConfiguration, new CaptureCountMeasurement(context));
        }
    }
    
    public static final class EventBuilder
    {
        public static final Companion Companion;
        private FirebaseEvent firebaseEvent;
        private TelemetryEvent telemetryEvent;
        
        static {
            Companion = new Companion(null);
        }
        
        public EventBuilder(final String str, final String str2, final String str3, final String str4) {
            Intrinsics.checkParameterIsNotNull(str, "category");
            Intrinsics.checkParameterIsNotNull(str2, "method");
            EventBuilder.Companion.lazyInit();
            final StringBuilder sb = new StringBuilder();
            sb.append("EVENT:");
            sb.append(str);
            sb.append('/');
            sb.append(str2);
            sb.append('/');
            sb.append(str3);
            sb.append('/');
            sb.append(str4);
            Log.d("TelemetryWrapper", sb.toString());
            final TelemetryEvent create = TelemetryEvent.create(str, str2, str3, str4);
            Intrinsics.checkExpressionValueIsNotNull(create, "TelemetryEvent.create(ca\u2026 method, `object`, value)");
            this.telemetryEvent = create;
            final FirebaseEvent create2 = FirebaseEvent.create(str, str2, str3, str4);
            Intrinsics.checkExpressionValueIsNotNull(create2, "FirebaseEvent.create(cat\u2026 method, `object`, value)");
            this.firebaseEvent = create2;
        }
        
        public static /* synthetic */ void queue$default(final EventBuilder eventBuilder, boolean b, final int n, final Object o) {
            if ((n & 0x1) != 0x0) {
                b = false;
            }
            eventBuilder.queue(b);
        }
        
        private final void sendEventNow(final TelemetryEvent telemetryEvent) {
            final Telemetry value = TelemetryHolder.get();
            TelemetryPingBuilder telemetryPingBuilder = null;
            Intrinsics.checkExpressionValueIsNotNull(value, "telemetry");
            for (final TelemetryPingBuilder telemetryPingBuilder2 : value.getBuilders()) {
                if (telemetryPingBuilder2 instanceof TelemetryEventPingBuilder) {
                    telemetryPingBuilder = telemetryPingBuilder2;
                }
            }
            if (telemetryPingBuilder != null) {
                final TelemetryEventPingBuilder telemetryEventPingBuilder = (TelemetryEventPingBuilder)telemetryPingBuilder;
                final EventsMeasurement eventsMeasurement = telemetryEventPingBuilder.getEventsMeasurement();
                Intrinsics.checkExpressionValueIsNotNull(eventsMeasurement, "(focusEventBuilder as Te\u2026uilder).eventsMeasurement");
                final String type = telemetryEventPingBuilder.getType();
                Intrinsics.checkExpressionValueIsNotNull(type, "focusEventBuilder.type");
                eventsMeasurement.add(telemetryEvent);
                value.queuePing(type).scheduleUpload();
                return;
            }
            throw new IllegalStateException("Expect either TelemetryEventPingBuilder or TelemetryMobileEventPingBuilder to be added to queue events");
        }
        
        public final EventBuilder extra(final String str, final String str2) {
            Intrinsics.checkParameterIsNotNull(str, "key");
            Intrinsics.checkParameterIsNotNull(str2, "value");
            final StringBuilder sb = new StringBuilder();
            sb.append("EXTRA:");
            sb.append(str);
            sb.append('/');
            sb.append(str2);
            Log.d("TelemetryWrapper", sb.toString());
            this.telemetryEvent.extra(str, str2);
            this.firebaseEvent.param(str, str2);
            return this;
        }
        
        public final void queue(final boolean b) {
            final Telemetry value = TelemetryHolder.get();
            Intrinsics.checkExpressionValueIsNotNull(value, "TelemetryHolder.get()");
            final TelemetryConfiguration configuration = value.getConfiguration();
            Intrinsics.checkExpressionValueIsNotNull(configuration, "TelemetryHolder.get().configuration");
            final Context context = configuration.getContext();
            if (context != null) {
                if (b) {
                    final Telemetry value2 = TelemetryHolder.get();
                    Intrinsics.checkExpressionValueIsNotNull(value2, "TelemetryHolder.get()");
                    final TelemetryConfiguration configuration2 = value2.getConfiguration();
                    Intrinsics.checkExpressionValueIsNotNull(configuration2, "TelemetryHolder.get().configuration");
                    configuration2.setMinimumEventsForUpload(1);
                    this.sendEventNow(this.telemetryEvent);
                }
                else {
                    this.telemetryEvent.queue();
                }
                this.firebaseEvent.event(context);
            }
        }
        
        public static final class Companion
        {
            private Companion() {
            }
            
            public final void lazyInit() {
                if (FirebaseEvent.isInitialized()) {
                    return;
                }
                final Telemetry value = TelemetryHolder.get();
                Intrinsics.checkExpressionValueIsNotNull(value, "TelemetryHolder.get()");
                final TelemetryConfiguration configuration = value.getConfiguration();
                Intrinsics.checkExpressionValueIsNotNull(configuration, "TelemetryHolder.get().configuration");
                final Context context = configuration.getContext();
                if (context != null) {
                    final HashMap<String, String> prefKeyWhitelist = new HashMap<String, String>();
                    final HashMap<String, String> hashMap = prefKeyWhitelist;
                    final String string = context.getString(2131755325);
                    Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.string.pref_key_search_engine)");
                    hashMap.put(string, "search_engine");
                    final String string2 = context.getString(2131755334);
                    Intrinsics.checkExpressionValueIsNotNull(string2, "context.getString(R.string.pref_s_news)");
                    hashMap.put(string2, "pref_s_news");
                    final String string3 = context.getString(2131755319);
                    Intrinsics.checkExpressionValueIsNotNull(string3, "context.getString(R.stri\u2026ef_key_privacy_block_ads)");
                    hashMap.put(string3, "privacy_ads");
                    final String string4 = context.getString(2131755320);
                    Intrinsics.checkExpressionValueIsNotNull(string4, "context.getString(R.stri\u2026_privacy_block_analytics)");
                    hashMap.put(string4, "privacy_analytics");
                    final String string5 = context.getString(2131755322);
                    Intrinsics.checkExpressionValueIsNotNull(string5, "context.getString(R.stri\u2026key_privacy_block_social)");
                    hashMap.put(string5, "privacy_social");
                    final String string6 = context.getString(2131755321);
                    Intrinsics.checkExpressionValueIsNotNull(string6, "context.getString(R.stri\u2026_key_privacy_block_other)");
                    hashMap.put(string6, "privacy_other");
                    final String string7 = context.getString(2131755332);
                    Intrinsics.checkExpressionValueIsNotNull(string7, "context.getString(R.string.pref_key_turbo_mode)");
                    hashMap.put(string7, "turbo_mode");
                    final String string8 = context.getString(2131755317);
                    Intrinsics.checkExpressionValueIsNotNull(string8, "context.getString(R.stri\u2026rformance_block_webfonts)");
                    hashMap.put(string8, "block_webfonts");
                    final String string9 = context.getString(2131755316);
                    Intrinsics.checkExpressionValueIsNotNull(string9, "context.getString(R.stri\u2026performance_block_images)");
                    hashMap.put(string9, "block_images");
                    final String string10 = context.getString(2131755303);
                    Intrinsics.checkExpressionValueIsNotNull(string10, "context.getString(R.stri\u2026pref_key_default_browser)");
                    hashMap.put(string10, "default_browser");
                    final String string11 = context.getString(2131755331);
                    Intrinsics.checkExpressionValueIsNotNull(string11, "context.getString(R.string.pref_key_telemetry)");
                    hashMap.put(string11, "telemetry");
                    final String string12 = context.getString(2131755310);
                    Intrinsics.checkExpressionValueIsNotNull(string12, "context.getString(R.string.pref_key_give_feedback)");
                    hashMap.put(string12, "give_feedback");
                    final String string13 = context.getString(2131755327);
                    Intrinsics.checkExpressionValueIsNotNull(string13, "context.getString(R.stri\u2026f_key_share_with_friends)");
                    hashMap.put(string13, "share_with_friends");
                    final String string14 = context.getString(2131755295);
                    Intrinsics.checkExpressionValueIsNotNull(string14, "context.getString(R.string.pref_key_about)");
                    hashMap.put(string14, "key_about");
                    final String string15 = context.getString(2131755311);
                    Intrinsics.checkExpressionValueIsNotNull(string15, "context.getString(R.string.pref_key_help)");
                    hashMap.put(string15, "help");
                    final String string16 = context.getString(2131755324);
                    Intrinsics.checkExpressionValueIsNotNull(string16, "context.getString(R.string.pref_key_rights)");
                    hashMap.put(string16, "rights");
                    final String string17 = context.getString(2131755333);
                    Intrinsics.checkExpressionValueIsNotNull(string17, "context.getString(R.stri\u2026pref_key_webview_version)");
                    hashMap.put(string17, "webview_version");
                    final String string18 = context.getString(2131755299);
                    Intrinsics.checkExpressionValueIsNotNull(string18, "context.getString(R.stri\u2026ey_data_saving_block_ads)");
                    hashMap.put(string18, "saving_block_ads");
                    final String string19 = context.getString(2131755302);
                    Intrinsics.checkExpressionValueIsNotNull(string19, "context.getString(R.stri\u2026ta_saving_block_webfonts)");
                    hashMap.put(string19, "data_webfont");
                    final String string20 = context.getString(2131755300);
                    Intrinsics.checkExpressionValueIsNotNull(string20, "context.getString(R.stri\u2026data_saving_block_images)");
                    hashMap.put(string20, "data_images");
                    final String string21 = context.getString(2131755301);
                    Intrinsics.checkExpressionValueIsNotNull(string21, "context.getString(R.stri\u2026saving_block_tab_restore)");
                    hashMap.put(string21, "tab_restore");
                    final String string22 = context.getString(2131755329);
                    Intrinsics.checkExpressionValueIsNotNull(string22, "context.getString(R.stri\u2026rage_clear_browsing_data)");
                    hashMap.put(string22, "clear_browsing_data)");
                    final String string23 = context.getString(2131755323);
                    Intrinsics.checkExpressionValueIsNotNull(string23, "context.getString(R.stri\u2026rage_available_on_create)");
                    hashMap.put(string23, "remove_storage");
                    final String string24 = context.getString(2131755330);
                    Intrinsics.checkExpressionValueIsNotNull(string24, "context.getString(R.stri\u2026torage_save_downloads_to)");
                    hashMap.put(string24, "save_downloads_to");
                    final String string25 = context.getString(2131755328);
                    Intrinsics.checkExpressionValueIsNotNull(string25, "context.getString(R.stri\u2026y_showed_storage_message)");
                    hashMap.put(string25, "storage_message)");
                    final String string26 = context.getString(2131755335);
                    Intrinsics.checkExpressionValueIsNotNull(string26, "context.getString(R.stri\u2026e_clear_browsing_history)");
                    hashMap.put(string26, "clear_browsing_his");
                    final String string27 = context.getString(2131755338);
                    Intrinsics.checkExpressionValueIsNotNull(string27, "context.getString(R.stri\u2026value_clear_form_history)");
                    hashMap.put(string27, "clear_form_his");
                    final String string28 = context.getString(2131755337);
                    Intrinsics.checkExpressionValueIsNotNull(string28, "context.getString(R.stri\u2026pref_value_clear_cookies)");
                    hashMap.put(string28, "clear_cookies");
                    final String string29 = context.getString(2131755336);
                    Intrinsics.checkExpressionValueIsNotNull(string29, "context.getString(R.string.pref_value_clear_cache)");
                    hashMap.put(string29, "clear_cache");
                    final String string30 = context.getString(2131755340);
                    Intrinsics.checkExpressionValueIsNotNull(string30, "context.getString(R.stri\u2026alue_saving_path_sd_card)");
                    hashMap.put(string30, "path_sd_card");
                    final String string31 = context.getString(2131755339);
                    Intrinsics.checkExpressionValueIsNotNull(string31, "context.getString(R.stri\u2026ng_path_internal_storage)");
                    hashMap.put(string31, "path_internal_storage");
                    FirebaseEvent.setPrefKeyWhitelist(prefKeyWhitelist);
                }
            }
        }
    }
    
    public enum FIND_IN_PAGE
    {
        CLICK_NEXT, 
        CLICK_PREVIOUS, 
        OPEN_BY_MENU;
    }
    
    private static final class ThemeToyMeasurement extends TelemetryMeasurement
    {
        public static final Companion Companion;
        private static final String MEASUREMENT_CURRENT_THEME = "current_theme";
        private Context context;
        
        static {
            Companion = new Companion(null);
        }
        
        public ThemeToyMeasurement(final Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            super(ThemeToyMeasurement.MEASUREMENT_CURRENT_THEME);
            this.context = context;
        }
        
        @Override
        public Object flush() {
            final String currentThemeName = ThemeManager.getCurrentThemeName(this.context);
            Intrinsics.checkExpressionValueIsNotNull(currentThemeName, "ThemeManager.getCurrentThemeName(context)");
            return currentThemeName;
        }
        
        public static final class Companion
        {
            private Companion() {
            }
        }
    }
}
