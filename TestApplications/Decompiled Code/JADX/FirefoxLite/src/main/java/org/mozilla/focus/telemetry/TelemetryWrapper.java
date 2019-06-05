package org.mozilla.focus.telemetry;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.preference.PreferenceManager;
import android.util.Log;
import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.p005io.CloseableKt;
import org.mozilla.focus.Inject;
import org.mozilla.focus.provider.ScreenshotContract.Screenshot;
import org.mozilla.focus.search.SearchEngine;
import org.mozilla.focus.search.SearchEngineManager;
import org.mozilla.focus.utils.AdjustHelper;
import org.mozilla.focus.utils.Browsers;
import org.mozilla.focus.utils.Settings;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.theme.ThemeManager;
import org.mozilla.telemetry.Telemetry;
import org.mozilla.telemetry.TelemetryHolder;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.event.TelemetryEvent;
import org.mozilla.telemetry.measurement.DefaultSearchMeasurement.DefaultSearchEngineProvider;
import org.mozilla.telemetry.measurement.EventsMeasurement;
import org.mozilla.telemetry.measurement.SettingsMeasurement.SharedPreferenceSettingsProvider;
import org.mozilla.telemetry.measurement.TelemetryMeasurement;
import org.mozilla.telemetry.net.HttpURLConnectionTelemetryClient;
import org.mozilla.telemetry.ping.TelemetryCorePingBuilder;
import org.mozilla.telemetry.ping.TelemetryEventPingBuilder;
import org.mozilla.telemetry.ping.TelemetryPingBuilder;
import org.mozilla.telemetry.schedule.jobscheduler.JobSchedulerTelemetryScheduler;
import org.mozilla.telemetry.serialize.JSONPingSerializer;
import org.mozilla.telemetry.storage.FileTelemetryStorage;
import org.mozilla.threadutils.ThreadUtils;

/* compiled from: TelemetryWrapper.kt */
public final class TelemetryWrapper {
    public static final TelemetryWrapper INSTANCE = new TelemetryWrapper();
    private static final AtomicInteger sRefCount = new AtomicInteger(0);

    /* compiled from: TelemetryWrapper.kt */
    public static final class EventBuilder {
        public static final Companion Companion = new Companion();
        private FirebaseEvent firebaseEvent;
        private TelemetryEvent telemetryEvent;

        /* compiled from: TelemetryWrapper.kt */
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public final void lazyInit() {
                if (!FirebaseEvent.isInitialized()) {
                    Telemetry telemetry = TelemetryHolder.get();
                    Intrinsics.checkExpressionValueIsNotNull(telemetry, "TelemetryHolder.get()");
                    TelemetryConfiguration configuration = telemetry.getConfiguration();
                    Intrinsics.checkExpressionValueIsNotNull(configuration, "TelemetryHolder.get().configuration");
                    Context context = configuration.getContext();
                    if (context != null) {
                        HashMap hashMap = new HashMap();
                        Map map = hashMap;
                        String string = context.getString(C0769R.string.pref_key_search_engine);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.string.pref_key_search_engine)");
                        map.put(string, "search_engine");
                        string = context.getString(C0769R.string.pref_s_news);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.string.pref_s_news)");
                        map.put(string, "pref_s_news");
                        string = context.getString(C0769R.string.pref_key_privacy_block_ads);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…ef_key_privacy_block_ads)");
                        map.put(string, "privacy_ads");
                        string = context.getString(C0769R.string.pref_key_privacy_block_analytics);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…_privacy_block_analytics)");
                        map.put(string, "privacy_analytics");
                        string = context.getString(C0769R.string.pref_key_privacy_block_social);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…key_privacy_block_social)");
                        map.put(string, "privacy_social");
                        string = context.getString(C0769R.string.pref_key_privacy_block_other);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…_key_privacy_block_other)");
                        map.put(string, "privacy_other");
                        string = context.getString(C0769R.string.pref_key_turbo_mode);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.string.pref_key_turbo_mode)");
                        map.put(string, "turbo_mode");
                        string = context.getString(C0769R.string.pref_key_performance_block_webfonts);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…rformance_block_webfonts)");
                        map.put(string, "block_webfonts");
                        string = context.getString(C0769R.string.pref_key_performance_block_images);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…performance_block_images)");
                        map.put(string, "block_images");
                        string = context.getString(C0769R.string.pref_key_default_browser);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…pref_key_default_browser)");
                        map.put(string, "default_browser");
                        string = context.getString(C0769R.string.pref_key_telemetry);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.string.pref_key_telemetry)");
                        map.put(string, "telemetry");
                        string = context.getString(C0769R.string.pref_key_give_feedback);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.string.pref_key_give_feedback)");
                        map.put(string, "give_feedback");
                        string = context.getString(C0769R.string.pref_key_share_with_friends);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…f_key_share_with_friends)");
                        map.put(string, "share_with_friends");
                        string = context.getString(C0769R.string.pref_key_about);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.string.pref_key_about)");
                        map.put(string, "key_about");
                        string = context.getString(C0769R.string.pref_key_help);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.string.pref_key_help)");
                        map.put(string, "help");
                        string = context.getString(C0769R.string.pref_key_rights);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.string.pref_key_rights)");
                        map.put(string, "rights");
                        string = context.getString(C0769R.string.pref_key_webview_version);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…pref_key_webview_version)");
                        map.put(string, "webview_version");
                        string = context.getString(C0769R.string.pref_key_data_saving_block_ads);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…ey_data_saving_block_ads)");
                        map.put(string, "saving_block_ads");
                        string = context.getString(C0769R.string.pref_key_data_saving_block_webfonts);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…ta_saving_block_webfonts)");
                        map.put(string, "data_webfont");
                        string = context.getString(C0769R.string.pref_key_data_saving_block_images);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…data_saving_block_images)");
                        map.put(string, "data_images");
                        string = context.getString(C0769R.string.pref_key_data_saving_block_tab_restore);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…saving_block_tab_restore)");
                        map.put(string, "tab_restore");
                        string = context.getString(C0769R.string.pref_key_storage_clear_browsing_data);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…rage_clear_browsing_data)");
                        map.put(string, "clear_browsing_data)");
                        string = context.getString(C0769R.string.pref_key_removable_storage_available_on_create);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…rage_available_on_create)");
                        map.put(string, "remove_storage");
                        string = context.getString(C0769R.string.pref_key_storage_save_downloads_to);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…torage_save_downloads_to)");
                        map.put(string, "save_downloads_to");
                        string = context.getString(C0769R.string.pref_key_showed_storage_message);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…y_showed_storage_message)");
                        map.put(string, "storage_message)");
                        string = context.getString(C0769R.string.pref_value_clear_browsing_history);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…e_clear_browsing_history)");
                        map.put(string, "clear_browsing_his");
                        string = context.getString(C0769R.string.pref_value_clear_form_history);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…value_clear_form_history)");
                        map.put(string, "clear_form_his");
                        string = context.getString(C0769R.string.pref_value_clear_cookies);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…pref_value_clear_cookies)");
                        map.put(string, "clear_cookies");
                        string = context.getString(C0769R.string.pref_value_clear_cache);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.string.pref_value_clear_cache)");
                        map.put(string, "clear_cache");
                        string = context.getString(C0769R.string.pref_value_saving_path_sd_card);
                        Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.stri…alue_saving_path_sd_card)");
                        map.put(string, "path_sd_card");
                        String string2 = context.getString(C0769R.string.pref_value_saving_path_internal_storage);
                        Intrinsics.checkExpressionValueIsNotNull(string2, "context.getString(R.stri…ng_path_internal_storage)");
                        map.put(string2, "path_internal_storage");
                        FirebaseEvent.setPrefKeyWhitelist(hashMap);
                    }
                }
            }
        }

        public EventBuilder(String str, String str2, String str3, String str4) {
            Intrinsics.checkParameterIsNotNull(str, "category");
            Intrinsics.checkParameterIsNotNull(str2, "method");
            Companion.lazyInit();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("EVENT:");
            stringBuilder.append(str);
            stringBuilder.append('/');
            stringBuilder.append(str2);
            stringBuilder.append('/');
            stringBuilder.append(str3);
            stringBuilder.append('/');
            stringBuilder.append(str4);
            Log.d("TelemetryWrapper", stringBuilder.toString());
            TelemetryEvent create = TelemetryEvent.create(str, str2, str3, str4);
            Intrinsics.checkExpressionValueIsNotNull(create, "TelemetryEvent.create(ca… method, `object`, value)");
            this.telemetryEvent = create;
            FirebaseEvent create2 = FirebaseEvent.create(str, str2, str3, str4);
            Intrinsics.checkExpressionValueIsNotNull(create2, "FirebaseEvent.create(cat… method, `object`, value)");
            this.firebaseEvent = create2;
        }

        public /* synthetic */ EventBuilder(String str, String str2, String str3, String str4, int i, DefaultConstructorMarker defaultConstructorMarker) {
            if ((i & 8) != 0) {
                str4 = (String) null;
            }
            this(str, str2, str3, str4);
        }

        public final EventBuilder extra(String str, String str2) {
            Intrinsics.checkParameterIsNotNull(str, "key");
            Intrinsics.checkParameterIsNotNull(str2, "value");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("EXTRA:");
            stringBuilder.append(str);
            stringBuilder.append('/');
            stringBuilder.append(str2);
            Log.d("TelemetryWrapper", stringBuilder.toString());
            this.telemetryEvent.extra(str, str2);
            this.firebaseEvent.param(str, str2);
            return this;
        }

        public static /* synthetic */ void queue$default(EventBuilder eventBuilder, boolean z, int i, Object obj) {
            if ((i & 1) != 0) {
                z = false;
            }
            eventBuilder.queue(z);
        }

        public final void queue(boolean z) {
            Telemetry telemetry = TelemetryHolder.get();
            Intrinsics.checkExpressionValueIsNotNull(telemetry, "TelemetryHolder.get()");
            TelemetryConfiguration configuration = telemetry.getConfiguration();
            Intrinsics.checkExpressionValueIsNotNull(configuration, "TelemetryHolder.get().configuration");
            Context context = configuration.getContext();
            if (context != null) {
                if (z) {
                    Telemetry telemetry2 = TelemetryHolder.get();
                    Intrinsics.checkExpressionValueIsNotNull(telemetry2, "TelemetryHolder.get()");
                    TelemetryConfiguration configuration2 = telemetry2.getConfiguration();
                    Intrinsics.checkExpressionValueIsNotNull(configuration2, "TelemetryHolder.get().configuration");
                    configuration2.setMinimumEventsForUpload(1);
                    sendEventNow(this.telemetryEvent);
                } else {
                    this.telemetryEvent.queue();
                }
                this.firebaseEvent.event(context);
            }
        }

        private final void sendEventNow(TelemetryEvent telemetryEvent) {
            Telemetry telemetry = TelemetryHolder.get();
            TelemetryPingBuilder telemetryPingBuilder = (TelemetryPingBuilder) null;
            Intrinsics.checkExpressionValueIsNotNull(telemetry, "telemetry");
            for (TelemetryPingBuilder telemetryPingBuilder2 : telemetry.getBuilders()) {
                if (telemetryPingBuilder2 instanceof TelemetryEventPingBuilder) {
                    telemetryPingBuilder = telemetryPingBuilder2;
                }
            }
            if (telemetryPingBuilder != null) {
                TelemetryEventPingBuilder telemetryEventPingBuilder = (TelemetryEventPingBuilder) telemetryPingBuilder;
                EventsMeasurement eventsMeasurement = telemetryEventPingBuilder.getEventsMeasurement();
                Intrinsics.checkExpressionValueIsNotNull(eventsMeasurement, "(focusEventBuilder as Te…uilder).eventsMeasurement");
                String type = telemetryEventPingBuilder.getType();
                Intrinsics.checkExpressionValueIsNotNull(type, "focusEventBuilder.type");
                eventsMeasurement.add(telemetryEvent);
                telemetry.queuePing(type).scheduleUpload();
                return;
            }
            throw new IllegalStateException("Expect either TelemetryEventPingBuilder or TelemetryMobileEventPingBuilder to be added to queue events");
        }
    }

    /* compiled from: TelemetryWrapper.kt */
    public enum FIND_IN_PAGE {
        OPEN_BY_MENU,
        CLICK_PREVIOUS,
        CLICK_NEXT
    }

    /* compiled from: TelemetryWrapper.kt */
    private static final class CaptureCountMeasurement extends TelemetryMeasurement {
        public static final Companion Companion = new Companion();
        private final Context context;

        /* compiled from: TelemetryWrapper.kt */
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }

        public CaptureCountMeasurement(Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            super("capture_count");
            this.context = context;
        }

        public Object flush() {
            Thread currentThread = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
            if (Intrinsics.areEqual("main", currentThread.getName())) {
                throw new RuntimeException("Call from main thread exception");
            }
            int count;
            Closeable closeable;
            Throwable th;
            try {
                Cursor query = this.context.getContentResolver().query(Screenshot.CONTENT_URI, null, null, null, null);
                if (query == null) {
                    Intrinsics.throwNpe();
                }
                closeable = query;
                th = (Throwable) null;
                count = ((Cursor) closeable).getCount();
                Unit unit = Unit.INSTANCE;
                CloseableKt.closeFinally(closeable, th);
            } catch (Exception unused) {
                count = -1;
            } catch (Throwable th2) {
                CloseableKt.closeFinally(closeable, th);
            }
            return Integer.valueOf(count);
        }
    }

    /* compiled from: TelemetryWrapper.kt */
    private static final class ThemeToyMeasurement extends TelemetryMeasurement {
        public static final Companion Companion = new Companion();
        private static final String MEASUREMENT_CURRENT_THEME = MEASUREMENT_CURRENT_THEME;
        private Context context;

        /* compiled from: TelemetryWrapper.kt */
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }

        public ThemeToyMeasurement(Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            super(MEASUREMENT_CURRENT_THEME);
            this.context = context;
        }

        public Object flush() {
            String currentThemeName = ThemeManager.getCurrentThemeName(this.context);
            Intrinsics.checkExpressionValueIsNotNull(currentThemeName, "ThemeManager.getCurrentThemeName(context)");
            return currentThemeName;
        }
    }

    /* compiled from: TelemetryWrapper.kt */
    private static final class CustomSettingsProvider extends SharedPreferenceSettingsProvider {
        private final HashMap<String, Object> custom = new HashMap(2);

        public void update(TelemetryConfiguration telemetryConfiguration) {
            Intrinsics.checkParameterIsNotNull(telemetryConfiguration, "configuration");
            super.update(telemetryConfiguration);
            Context context = telemetryConfiguration.getContext();
            Intrinsics.checkExpressionValueIsNotNull(context, "context");
            addCustomPing$app_focusWebkitRelease(telemetryConfiguration, new ThemeToyMeasurement(context));
            addCustomPing$app_focusWebkitRelease(telemetryConfiguration, new CaptureCountMeasurement(context));
        }

        public final void addCustomPing$app_focusWebkitRelease(TelemetryConfiguration telemetryConfiguration, TelemetryMeasurement telemetryMeasurement) {
            Intrinsics.checkParameterIsNotNull(telemetryConfiguration, "configuration");
            Intrinsics.checkParameterIsNotNull(telemetryMeasurement, "measurement");
            Set preferencesImportantForTelemetry = telemetryConfiguration.getPreferencesImportantForTelemetry();
            if (preferencesImportantForTelemetry == null) {
                telemetryConfiguration.setPreferencesImportantForTelemetry(new String[0]);
                preferencesImportantForTelemetry = telemetryConfiguration.getPreferencesImportantForTelemetry();
            }
            if (preferencesImportantForTelemetry == null) {
                Intrinsics.throwNpe();
            }
            String fieldName = telemetryMeasurement.getFieldName();
            Intrinsics.checkExpressionValueIsNotNull(fieldName, "measurement.fieldName");
            preferencesImportantForTelemetry.add(fieldName);
            Map map = this.custom;
            String fieldName2 = telemetryMeasurement.getFieldName();
            Intrinsics.checkExpressionValueIsNotNull(fieldName2, "measurement.fieldName");
            Object flush = telemetryMeasurement.flush();
            Intrinsics.checkExpressionValueIsNotNull(flush, "measurement.flush()");
            map.put(fieldName2, flush);
        }

        public boolean containsKey(String str) {
            Intrinsics.checkParameterIsNotNull(str, "key");
            return this.custom.containsKey(str) | super.containsKey(str);
        }

        public Object getValue(String str) {
            Intrinsics.checkParameterIsNotNull(str, "key");
            Object obj = this.custom.get(str);
            if (obj != null) {
                return obj;
            }
            obj = super.getValue(str);
            Intrinsics.checkExpressionValueIsNotNull(obj, "super.getValue(key)");
            return obj;
        }
    }

    private TelemetryWrapper() {
    }

    public static final boolean isTelemetryEnabled(Context context) {
        return context == null ? false : Inject.isTelemetryEnabled(context);
    }

    public static final void setTelemetryEnabled(Context context, boolean z) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Resources resources = context.getResources();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String string = resources.getString(C0769R.string.pref_key_telemetry);
        defaultSharedPreferences.edit().putBoolean(string, z).apply();
        ThreadUtils.postToBackgroundThread((Runnable) new TelemetryWrapper$setTelemetryEnabled$1(string, z));
    }

    public final void init(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        try {
            Resources resources = context.getResources();
            boolean isTelemetryEnabled = isTelemetryEnabled(context);
            updateDefaultBrowserStatus(context);
            TelemetryConfiguration uploadEnabled = new TelemetryConfiguration(context).setServerEndpoint("https://incoming.telemetry.mozilla.org").setAppName("Zerda").setUpdateChannel("release").setPreferencesImportantForTelemetry(resources.getString(C0769R.string.pref_key_search_engine), resources.getString(C0769R.string.pref_key_turbo_mode), resources.getString(C0769R.string.pref_key_performance_block_images), resources.getString(C0769R.string.pref_key_default_browser), resources.getString(C0769R.string.pref_key_storage_save_downloads_to), resources.getString(C0769R.string.pref_key_webview_version), resources.getString(C0769R.string.pref_s_news), resources.getString(C0769R.string.pref_key_locale)).setSettingsProvider(new CustomSettingsProvider()).setCollectionEnabled(isTelemetryEnabled).setUploadEnabled(true);
            TelemetryHolder.set(new Telemetry(uploadEnabled, new FileTelemetryStorage(uploadEnabled, new JSONPingSerializer()), new HttpURLConnectionTelemetryClient(), new JobSchedulerTelemetryScheduler()).addPingBuilder(new TelemetryCorePingBuilder(uploadEnabled)).addPingBuilder(new TelemetryEventPingBuilder(uploadEnabled)).setDefaultSearchProvider(createDefaultSearchProvider(context)));
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskWrites);
        }
    }

    private final void updateDefaultBrowserStatus(Context context) {
        Settings.updatePrefDefaultBrowserIfNeeded(context, Browsers.isDefaultBrowser(context));
    }

    private final DefaultSearchEngineProvider createDefaultSearchProvider(Context context) {
        return new TelemetryWrapper$createDefaultSearchProvider$1(context);
    }

    public static final void toggleFirstRunPageEvent(boolean z) {
        String bool = Boolean.toString(z);
        Intrinsics.checkExpressionValueIsNotNull(bool, "java.lang.Boolean.toString(enableTurboMode)");
        EventBuilder.queue$default(new EventBuilder("action", "change", "firstrun", "turbo").extra("to", bool), false, 1, null);
    }

    public static final void finishFirstRunEvent(long j, int i) {
        String l = Long.toString(j);
        Intrinsics.checkExpressionValueIsNotNull(l, "java.lang.Long.toString(duration)");
        String num = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(mode)");
        EventBuilder.queue$default(new EventBuilder("action", "show", "firstrun", "finish").extra("on", l).extra("mode", num), false, 1, null);
    }

    public static final void launchByAppLauncherEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "launch", "app", "launcher"), false, 1, null);
    }

    public static final void launchByHomeScreenShortcutEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "launch", "app", "shortcut"), false, 1, null);
    }

    public static final void launchByExternalAppEvent(String str) {
        if (str == null) {
            EventBuilder.queue$default(new EventBuilder("action", "launch", "app", "external_app"), false, 1, null);
        } else {
            EventBuilder.queue$default(new EventBuilder("action", "launch", "app", "external_app").extra("type", str), false, 1, null);
        }
    }

    public static final void settingsEvent(String str, String str2, boolean z) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        Intrinsics.checkParameterIsNotNull(str2, "value");
        str = FirebaseEvent.getValidPrefKey(str);
        if (str != null) {
            new EventBuilder("action", "change", "setting", str).extra("to", str2).queue(z);
        }
    }

    public static final void settingsClickEvent(String str) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        str = FirebaseEvent.getValidPrefKey(str);
        if (str != null) {
            EventBuilder.queue$default(new EventBuilder("action", "click", "setting", str), false, 1, null);
        }
    }

    public static final void settingsLearnMoreClickEvent(String str) {
        Intrinsics.checkParameterIsNotNull(str, "source");
        EventBuilder.queue$default(new EventBuilder("action", "click", "setting", "learn_more").extra("source", str), false, 1, null);
    }

    public static final void settingsLocaleChangeEvent(String str, String str2, boolean z) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        Intrinsics.checkParameterIsNotNull(str2, "value");
        String bool = Boolean.toString(z);
        Intrinsics.checkExpressionValueIsNotNull(bool, "java.lang.Boolean.toString(isDefault)");
        EventBuilder.queue$default(new EventBuilder("action", "change", "setting", str).extra("to", str2).extra("default", bool), false, 1, null);
    }

    public static final void startSession() {
        if (sRefCount.getAndIncrement() == 0) {
            TelemetryHolder.get().recordSessionStart();
        }
        EventBuilder.queue$default(new EventBuilder("action", "foreground", "app", null, 8, null), false, 1, null);
    }

    public static final void stopSession() {
        if (sRefCount.decrementAndGet() == 0) {
            TelemetryHolder.get().recordSessionEnd();
        }
        EventBuilder.queue$default(new EventBuilder("action", "background", "app", null, 8, null), false, 1, null);
    }

    public static final void stopMainActivity() {
        TelemetryHolder.get().queuePing("core").queuePing("focus-event").scheduleUpload();
    }

    public static final void openWebContextMenuEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "long_press", "browser", null, 8, null), false, 1, null);
    }

    public static final void cancelWebContextMenuEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "cancel", "browser_contextmenu", null, 8, null), false, 1, null);
    }

    public static final void shareLinkEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "share", "browser_contextmenu", "link"), false, 1, null);
    }

    public static final void shareImageEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "share", "browser_contextmenu", "image"), false, 1, null);
    }

    public static final void saveImageEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "save", "browser_contextmenu", "image"), false, 1, null);
    }

    public static final void copyLinkEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "copy", "browser_contextmenu", "link"), false, 1, null);
    }

    public static final void copyImageEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "copy", "browser_contextmenu", "image"), false, 1, null);
    }

    public static final void addNewTabFromContextMenu() {
        EventBuilder.queue$default(new EventBuilder("action", "add", "browser_contextmenu", "link"), false, 1, null);
    }

    public static final void browseGeoLocationPermissionEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "permission", "browser", "geolocation"), false, 1, null);
    }

    public static final void browseFilePermissionEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "permission", "browser", "file"), false, 1, null);
    }

    public static final void browsePermissionEvent(String[] strArr) {
        Intrinsics.checkParameterIsNotNull(strArr, "requests");
        for (String str : strArr) {
            String str2;
            int hashCode = str2.hashCode();
            if (hashCode != -1660821873) {
                if (hashCode != 968612586) {
                    if (hashCode != 1069496794) {
                        if (hashCode == 1233677653 && str2.equals("android.webkit.resource.MIDI_SYSEX")) {
                            str2 = "midi";
                        }
                    } else if (str2.equals("android.webkit.resource.PROTECTED_MEDIA_ID")) {
                        str2 = "eme";
                    }
                } else if (str2.equals("android.webkit.resource.AUDIO_CAPTURE")) {
                    str2 = "audio";
                }
            } else if (str2.equals("android.webkit.resource.VIDEO_CAPTURE")) {
                str2 = "video";
            }
            EventBuilder.queue$default(new EventBuilder("action", "permission", "browser", str2), false, 1, null);
        }
    }

    public static final void browseEnterFullScreenEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "fullscreen", "browser", "enter"), false, 1, null);
    }

    public static final void browseExitFullScreenEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "fullscreen", "browser", "exit"), false, 1, null);
    }

    public static final void showMenuHome() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "menu", "home"), false, 1, null);
    }

    public static final void showTabTrayHome() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "tab_tray", "home"), false, 1, null);
    }

    public static final void showTabTrayToolbar() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "tab_tray", "toolbar"), false, 1, null);
    }

    public static final void showMenuToolbar() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "menu", "toolbar"), false, 1, null);
    }

    public static final void clickMenuDownload() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "download"), false, 1, null);
    }

    public static final void clickMenuHistory() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "history"), false, 1, null);
    }

    public static final void clickMenuCapture() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "capture"), false, 1, null);
    }

    public static final void showPanelBookmark() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "panel", "bookmark"), false, 1, null);
    }

    public static final void showPanelDownload() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "panel", "download"), false, 1, null);
    }

    public static final void showPanelHistory() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "panel", "history"), false, 1, null);
    }

    public static final void showPanelCapture() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "panel", "capture"), false, 1, null);
    }

    public static final void menuTurboChangeTo(boolean z) {
        String bool = Boolean.toString(z);
        Intrinsics.checkExpressionValueIsNotNull(bool, "java.lang.Boolean.toString(enable)");
        EventBuilder.queue$default(new EventBuilder("action", "change", "menu", "turbo").extra("to", bool), false, 1, null);
    }

    public static final void menuNightModeChangeTo(boolean z) {
        String bool = Boolean.toString(z);
        Intrinsics.checkExpressionValueIsNotNull(bool, "java.lang.Boolean.toString(enable)");
        EventBuilder.queue$default(new EventBuilder("action", "change", "menu", "night_mode").extra("to", bool), false, 1, null);
    }

    public static final void menuBlockImageChangeTo(boolean z) {
        String bool = Boolean.toString(z);
        Intrinsics.checkExpressionValueIsNotNull(bool, "java.lang.Boolean.toString(enable)");
        EventBuilder.queue$default(new EventBuilder("action", "change", "menu", "block_image").extra("to", bool), false, 1, null);
    }

    public static final void clickMenuClearCache() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "clear_cache"), false, 1, null);
    }

    public static final void clickMenuSettings() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "settings"), false, 1, null);
    }

    public static final void clickMenuExit() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "exit"), false, 1, null);
    }

    public static final void clickMenuBookmark() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "menu", "bookmark"), false, 1, null);
    }

    public static final void clickToolbarForward() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "toolbar", "forward"), false, 1, null);
    }

    public static final void clickToolbarReload() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "toolbar", "reload"), false, 1, null);
    }

    public static final void clickToolbarShare() {
        EventBuilder.queue$default(new EventBuilder("action", "share", "toolbar", "link"), false, 1, null);
    }

    public static final void clickToolbarBookmark(boolean z) {
        String bool = Boolean.toString(z);
        Intrinsics.checkExpressionValueIsNotNull(bool, "java.lang.Boolean.toString(isAdd)");
        EventBuilder.queue$default(new EventBuilder("action", "share", "toolbar", "bookmark").extra("to", bool), false, 1, null);
        if (z) {
            AdjustHelper.trackEvent("sj5vxj");
        }
    }

    public static final void clickAddToHome() {
        EventBuilder.queue$default(new EventBuilder("action", "pin_shortcut", "toolbar", "link"), false, 1, null);
        AdjustHelper.trackEvent("g1tpbz");
    }

    public static final void clickToolbarCapture(String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "category");
        String num = Integer.toString(3);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(TOOL_BA…APTURE_TELEMETRY_VERSION)");
        String num2 = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(num2, "Integer.toString(categoryVersion)");
        EventBuilder.queue$default(new EventBuilder("action", "click", "toolbar", "capture").extra("version", num).extra("category", str).extra("category_versio", num2), false, 1, null);
        AdjustHelper.trackEvent("ky29gk");
    }

    public static final void clickTopSiteOn(int i, String str) {
        Intrinsics.checkParameterIsNotNull(str, "source");
        String num = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(index)");
        EventBuilder.queue$default(new EventBuilder("action", "open", "home", "link").extra("on", num).extra("source", str).extra("version", "2"), false, 1, null);
        EventBuilder.queue$default(new EventBuilder("action", "add", "tab", "top_site"), false, 1, null);
    }

    public static final void removeTopSite(boolean z) {
        String bool = Boolean.toString(z);
        Intrinsics.checkExpressionValueIsNotNull(bool, "java.lang.Boolean.toString(isDefault)");
        EventBuilder.queue$default(new EventBuilder("action", "remove", "home", "link").extra("default", bool), false, 1, null);
    }

    public static final void addNewTabFromHome() {
        EventBuilder.queue$default(new EventBuilder("action", "add", "tab", "home"), false, 1, null);
    }

    public static final void urlBarEvent(boolean z, boolean z2) {
        if (z) {
            INSTANCE.browseEvent();
        } else if (z2) {
            searchSelectEvent();
        } else {
            INSTANCE.searchEnterEvent();
        }
    }

    private final void browseEvent() {
        EventBuilder.queue$default(new EventBuilder("action", "open", "search_bar", "link"), false, 1, null);
    }

    public static final void searchSelectEvent() {
        Telemetry telemetry = TelemetryHolder.get();
        EventBuilder.queue$default(new EventBuilder("action", "select_query", "search_bar", null, 8, null), false, 1, null);
        SearchEngineManager instance = SearchEngineManager.getInstance();
        Intrinsics.checkExpressionValueIsNotNull(telemetry, "telemetry");
        TelemetryConfiguration configuration = telemetry.getConfiguration();
        Intrinsics.checkExpressionValueIsNotNull(configuration, "telemetry.configuration");
        SearchEngine defaultSearchEngine = instance.getDefaultSearchEngine(configuration.getContext());
        Intrinsics.checkExpressionValueIsNotNull(defaultSearchEngine, "searchEngine");
        telemetry.recordSearch("suggestion", defaultSearchEngine.getIdentifier());
    }

    private final void searchEnterEvent() {
        Telemetry telemetry = TelemetryHolder.get();
        EventBuilder.queue$default(new EventBuilder("action", "type_query", "search_bar", null, 8, null), false, 1, null);
        SearchEngineManager instance = SearchEngineManager.getInstance();
        Intrinsics.checkExpressionValueIsNotNull(telemetry, "telemetry");
        TelemetryConfiguration configuration = telemetry.getConfiguration();
        Intrinsics.checkExpressionValueIsNotNull(configuration, "telemetry.configuration");
        SearchEngine defaultSearchEngine = instance.getDefaultSearchEngine(configuration.getContext());
        Intrinsics.checkExpressionValueIsNotNull(defaultSearchEngine, "searchEngine");
        telemetry.recordSearch("actionbar", defaultSearchEngine.getIdentifier());
    }

    public static final void togglePrivateMode(boolean z) {
        EventBuilder.queue$default(new EventBuilder("action", "change", "private_mode", z ? "enter" : "exit"), false, 1, null);
        if (z) {
            AdjustHelper.trackEvent("dccqsh");
        }
    }

    public static final void searchSuggestionLongClick() {
        EventBuilder.queue$default(new EventBuilder("action", "long_press", "search_suggestion", null, 8, null), false, 1, null);
    }

    public static final void searchClear() {
        EventBuilder.queue$default(new EventBuilder("action", "clear", "search_bar", null, 8, null).extra("version", "2"), false, 1, null);
    }

    public static final void searchDismiss() {
        EventBuilder.queue$default(new EventBuilder("action", "cancel", "search_bar", null, 8, null).extra("version", "2"), false, 1, null);
    }

    public static final void showSearchBarHome() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "search_bar", "search_box"), false, 1, null);
    }

    public static final void clickUrlbar() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "search_bar", "mini_urlbar"), false, 1, null);
    }

    public static final void clickToolbarSearch() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "search_bar", "search_btn"), false, 1, null);
    }

    public static final void clickAddTabToolbar() {
        EventBuilder.queue$default(new EventBuilder("action", "add", "tab", "toolbar"), false, 1, null);
    }

    public static final void clickAddTabTray() {
        EventBuilder.queue$default(new EventBuilder("action", "add", "tab", "tab_tray"), false, 1, null);
    }

    public static final void privateModeTray() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "private_mode", "tab_tray"), false, 1, null);
    }

    public static final void clickTabFromTabTray() {
        EventBuilder.queue$default(new EventBuilder("action", "change", "tab", "tab_tray"), false, 1, null);
    }

    public static final void closeTabFromTabTray() {
        EventBuilder.queue$default(new EventBuilder("action", "remove", "tab", "tab_tray"), false, 1, null);
    }

    public static final void swipeTabFromTabTray() {
        EventBuilder.queue$default(new EventBuilder("action", "swipe", "tab", "tab_tray"), false, 1, null);
    }

    public static final void closeAllTabFromTabTray() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "close_all", "tab_tray"), false, 1, null);
    }

    public static final void downloadRemoveFile() {
        EventBuilder.queue$default(new EventBuilder("action", "remove", "panel", "file"), false, 1, null);
    }

    public static final void downloadDeleteFile() {
        EventBuilder.queue$default(new EventBuilder("action", "delete", "panel", "file"), false, 1, null);
    }

    public static final void downloadOpenFile(boolean z) {
        String bool = Boolean.toString(z);
        Intrinsics.checkExpressionValueIsNotNull(bool, "java.lang.Boolean.toString(fromSnackBar)");
        EventBuilder.queue$default(new EventBuilder("action", "open", "panel", "file").extra("snackbar", bool), false, 1, null);
    }

    public static final void openLifeFeedNews() {
        EventBuilder.queue$default(new EventBuilder("action", "open", "panel", "lifefeed_news"), false, 1, null);
    }

    public final void clickOnNewsItem(String str, String str2, String str3, String str4, String str5) {
        Intrinsics.checkParameterIsNotNull(str, "pos");
        Intrinsics.checkParameterIsNotNull(str2, "feed");
        EventBuilder extra = new EventBuilder("action", "click", "panel", "lifefeed_news").extra("position", str).extra("feed", str2);
        str2 = "source";
        if (str3 == null) {
            str3 = "";
        }
        extra = extra.extra(str2, str3);
        str2 = "category";
        if (str4 == null) {
            str4 = "";
        }
        extra = extra.extra(str2, str4);
        str2 = "subcategory";
        if (str5 == null) {
            str5 = "";
        }
        EventBuilder.queue$default(extra.extra(str2, str5), false, 1, null);
    }

    public static final void openLifeFeedEc() {
        EventBuilder.queue$default(new EventBuilder("action", "open", "panel", "lifefeed_ec"), false, 1, null);
    }

    public final void clickOnEcItem(String str, String str2, String str3) {
        Intrinsics.checkParameterIsNotNull(str, "pos");
        EventBuilder extra = new EventBuilder("action", "click", "panel", "lifefeed_ec").extra("position", str);
        String str4 = "source";
        if (str2 == null) {
            str2 = "";
        }
        extra = extra.extra(str4, str2);
        str2 = "category";
        if (str3 == null) {
            str3 = "";
        }
        EventBuilder.queue$default(extra.extra(str2, str3), false, 1, null);
    }

    public static final void showFileContextMenu() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "menu", "download"), false, 1, null);
    }

    public static final void historyOpenLink() {
        EventBuilder.queue$default(new EventBuilder("action", "open", "panel", "link"), false, 1, null);
    }

    public static final void historyRemoveLink() {
        EventBuilder.queue$default(new EventBuilder("action", "remove", "panel", "link"), false, 1, null);
    }

    public static final void bookmarkRemoveItem() {
        EventBuilder.queue$default(new EventBuilder("action", "remove", "panel", "bookmark"), false, 1, null);
    }

    public static final void bookmarkEditItem() {
        EventBuilder.queue$default(new EventBuilder("action", "edit", "panel", "bookmark"), false, 1, null);
    }

    public static final void bookmarkOpenItem() {
        EventBuilder.queue$default(new EventBuilder("action", "open", "panel", "bookmark"), false, 1, null);
    }

    public static final void showHistoryContextMenu() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "menu", "history"), false, 1, null);
    }

    public static final void showBookmarkContextMenu() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "menu", "bookmark"), false, 1, null);
    }

    public static final void clearHistory() {
        EventBuilder.queue$default(new EventBuilder("action", "clear", "panel", "history"), false, 1, null);
    }

    public static final void openCapture() {
        EventBuilder.queue$default(new EventBuilder("action", "open", "panel", "capture"), false, 1, null);
    }

    public static final void openCaptureLink(String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "category");
        String num = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(categoryVersion)");
        EventBuilder.queue$default(new EventBuilder("action", "open", "capture", "link").extra("category", str).extra("category_versio", num), false, 1, null);
    }

    public static final void editCaptureImage(boolean z, String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "category");
        String bool = Boolean.toString(z);
        Intrinsics.checkExpressionValueIsNotNull(bool, "java.lang.Boolean.toString(editAppResolved)");
        String num = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(categoryVersion)");
        EventBuilder.queue$default(new EventBuilder("action", "edit", "capture", "image").extra("success", bool).extra("category", str).extra("category_versio", num), false, 1, null);
    }

    public static final void shareCaptureImage(boolean z, String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "category");
        String bool = Boolean.toString(z);
        Intrinsics.checkExpressionValueIsNotNull(bool, "java.lang.Boolean.toString(fromSnackBar)");
        String num = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(categoryVersion)");
        EventBuilder.queue$default(new EventBuilder("action", "share", "capture", "image").extra("snackbar", bool).extra("category", str).extra("category_versio", num), false, 1, null);
    }

    public static final void showCaptureInfo(String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "category");
        String num = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(categoryVersion)");
        EventBuilder.queue$default(new EventBuilder("action", "show", "capture", "info").extra("category", str).extra("category_versio", num), false, 1, null);
    }

    public static final void deleteCaptureImage(String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "category");
        String num = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(categoryVersion)");
        EventBuilder.queue$default(new EventBuilder("action", "delete", "capture", "image").extra("category", str).extra("category_versio", num), false, 1, null);
    }

    public static final void clickRateApp(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str2, "source");
        EventBuilder.queue$default(new EventBuilder("action", "click", "feedback", str).extra("source", str2).extra("version", String.valueOf(3)), false, 1, null);
        if (Intrinsics.areEqual("positive", str)) {
            AdjustHelper.trackEvent("uivfof");
        }
    }

    public static final void showRateApp(boolean z) {
        EventBuilder eventBuilder = new EventBuilder("action", "show", "feedback", null, 8, null);
        if (z) {
            eventBuilder.extra("source", "notification");
        }
        EventBuilder.queue$default(eventBuilder, false, 1, null);
    }

    public static final void showDefaultSettingNotification() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "default_browser", null, 8, null).extra("source", "notification"), false, 1, null);
    }

    public static final void receiveFirstrunConfig(long j, String str) {
        EventBuilder extra = new EventBuilder("action", "get", "firstrun_push", null, 8, null).extra("delay", String.valueOf(j));
        String str2 = "message";
        if (str == null) {
            str = "";
        }
        EventBuilder.queue$default(extra.extra(str2, str), false, 1, null);
    }

    public static final void showFirstrunNotification(long j, String str) {
        EventBuilder extra = new EventBuilder("action", "show", "firstrun_push", null, 8, null).extra("delay", String.valueOf(j));
        String str2 = "message";
        if (str == null) {
            str = "";
        }
        EventBuilder.queue$default(extra.extra(str2, str), false, 1, null);
    }

    public static final void clickDefaultSettingNotification() {
        String num = Integer.toString(2);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(DEFAULT…CATION_TELEMETRY_VERSION)");
        EventBuilder.queue$default(new EventBuilder("action", "click", "default_browser", null, 8, null).extra("source", "notification").extra("version", num), false, 1, null);
    }

    public static final void onDefaultBrowserServiceFailed() {
        String bool = Boolean.toString(false);
        Intrinsics.checkExpressionValueIsNotNull(bool, "java.lang.Boolean.toString(false)");
        EventBuilder.queue$default(new EventBuilder("action", "change", "default_browser", null, 8, null).extra("success", bool), false, 1, null);
    }

    public static final void promoteShareClickEvent(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "value");
        Intrinsics.checkParameterIsNotNull(str2, "source");
        EventBuilder.queue$default(new EventBuilder("action", "click", "promote_share", str).extra("source", str2), false, 1, null);
        if (Intrinsics.areEqual("share", str)) {
            AdjustHelper.trackEvent("3obefy");
        }
    }

    public static final void showPromoteShareDialog() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "promote_share", null, 8, null), false, 1, null);
    }

    public static final void changeThemeTo(String str) {
        Intrinsics.checkParameterIsNotNull(str, "themeName");
        EventBuilder.queue$default(new EventBuilder("action", "change", "themetoy", null, 8, null).extra("to", str), false, 1, null);
    }

    public static final void resetThemeToDefault() {
        EventBuilder.queue$default(new EventBuilder("action", "reset", "themetoy", null, 8, null).extra("to", "default"), false, 1, null);
    }

    public static final void erasePrivateModeNotification() {
        EventBuilder.queue$default(new EventBuilder("action", "clear", "private_mode", null, 8, null).extra("source", "notification"), false, 1, null);
    }

    public static final void showHome() {
        EventBuilder.queue$default(new EventBuilder("action", "show", "home", null, 8, null), false, 1, null);
    }

    public static final void showBannerNew(String str) {
        Intrinsics.checkParameterIsNotNull(str, "id");
        EventBuilder.queue$default(new EventBuilder("action", "show", "banner", "new").extra("to", str), false, 1, null);
    }

    public static final void showBannerUpdate(String str) {
        Intrinsics.checkParameterIsNotNull(str, "id");
        EventBuilder.queue$default(new EventBuilder("action", "show", "banner", "update").extra("to", str), false, 1, null);
    }

    public static final void showBannerReturn(String str) {
        Intrinsics.checkParameterIsNotNull(str, "id");
        EventBuilder.queue$default(new EventBuilder("action", "show", "banner", "return").extra("to", str), false, 1, null);
    }

    public static final void showBannerSwipe(String str) {
        Intrinsics.checkParameterIsNotNull(str, "id");
        EventBuilder.queue$default(new EventBuilder("action", "show", "banner", "swipe").extra("to", str), false, 1, null);
    }

    public static final void clickBannerBackground(String str) {
        Intrinsics.checkParameterIsNotNull(str, "pageId");
        EventBuilder.queue$default(new EventBuilder("action", "click", "banner", "background").extra("source", str), false, 1, null);
    }

    public static final void clickBannerItem(String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "pageId");
        String num = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(itemPosition)");
        EventBuilder.queue$default(new EventBuilder("action", "click", "banner", "item").extra("source", str).extra("on", num), false, 1, null);
    }

    public static final void swipeBannerItem(int i, int i2) {
        String num = Integer.toString(i);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(directionX)");
        String num2 = Integer.toString(i2);
        Intrinsics.checkExpressionValueIsNotNull(num2, "Integer.toString(toItemPosition)");
        EventBuilder.queue$default(new EventBuilder("action", "swipe", "banner", "page").extra("direction", num).extra("to", num2), false, 1, null);
    }

    public static final void clickWifiFinderSurvey() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "home", "wifi_finder"), false, 1, null);
    }

    public static final void clickVpnSurvey() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "home", "vpn"), false, 1, null);
    }

    public static final void surveyResult(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "result");
        Intrinsics.checkParameterIsNotNull(str2, "source");
        EventBuilder.queue$default(new EventBuilder("action", "click", "doorhanger", str).extra("source", str2), false, 1, null);
    }

    public static final void showVpnRecommender(boolean z) {
        String bool = Boolean.toString(z);
        Intrinsics.checkExpressionValueIsNotNull(bool, "java.lang.Boolean.toString(installed)");
        EventBuilder.queue$default(new EventBuilder("action", "show", "home", "vpn_recommend").extra("vpn_installed", bool), false, 1, null);
    }

    public static final void clickVpnRecommender(boolean z) {
        String bool = Boolean.toString(z);
        Intrinsics.checkExpressionValueIsNotNull(bool, "java.lang.Boolean.toString(installed)");
        EventBuilder.queue$default(new EventBuilder("action", "click", "home", "vpn_recommend").extra("vpn_installed", bool), false, 1, null);
    }

    public static final void clickVpnRecommend(boolean z) {
        EventBuilder.queue$default(new EventBuilder("action", "click", "vpn_doorhanger", z ? "positive" : "negative"), false, 1, null);
    }

    public static final void dismissVpnRecommend() {
        EventBuilder.queue$default(new EventBuilder("action", "click", "vpn_doorhanger", "dismiss"), false, 1, null);
    }

    public static final void findInPage(FIND_IN_PAGE find_in_page) {
        EventBuilder clickMenuFindInPage$app_focusWebkitRelease;
        Intrinsics.checkParameterIsNotNull(find_in_page, "type");
        switch (find_in_page) {
            case OPEN_BY_MENU:
                clickMenuFindInPage$app_focusWebkitRelease = INSTANCE.clickMenuFindInPage$app_focusWebkitRelease();
                break;
            case CLICK_PREVIOUS:
                clickMenuFindInPage$app_focusWebkitRelease = INSTANCE.clickFindInPagePrevious$app_focusWebkitRelease();
                break;
            case CLICK_NEXT:
                clickMenuFindInPage$app_focusWebkitRelease = INSTANCE.clickFindInPageNext$app_focusWebkitRelease();
                break;
            default:
                throw new NoWhenBranchMatchedException();
        }
        EventBuilder.queue$default(clickMenuFindInPage$app_focusWebkitRelease, false, 1, null);
    }

    public static final void nightModeBrightnessChangeTo(int i, boolean z) {
        EventBuilder.queue$default(new EventBuilder("action", "change", "setting", "night_mode_brightness").extra("source", z ? "setting" : "menu").extra("to", String.valueOf(i)), false, 1, null);
    }

    public static final void longPressDownloadIndicator() {
        EventBuilder.queue$default(new EventBuilder("Downloads", "long_press", "toolbar", "download"), false, 1, null);
    }

    public final EventBuilder clickFindInPageNext$app_focusWebkitRelease() {
        String num = Integer.toString(2);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(FIND_IN_PAGE_VERSION)");
        return new EventBuilder("action", "click", "find_in_page", "next").extra("version", num);
    }

    public final EventBuilder clickFindInPagePrevious$app_focusWebkitRelease() {
        String num = Integer.toString(2);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(FIND_IN_PAGE_VERSION)");
        return new EventBuilder("action", "click", "find_in_page", "previous").extra("version", num);
    }

    public final EventBuilder clickMenuFindInPage$app_focusWebkitRelease() {
        String num = Integer.toString(2);
        Intrinsics.checkExpressionValueIsNotNull(num, "Integer.toString(FIND_IN_PAGE_VERSION)");
        return new EventBuilder("action", "click", "menu", "find_in_page").extra("version", num);
    }

    public static final void clickDefaultBrowserInSetting() {
        AdjustHelper.trackEvent("iyqe7a");
    }

    public static final void clickQuickSearchEngine(String str) {
        Intrinsics.checkParameterIsNotNull(str, "engineName");
        EventBuilder.queue$default(new EventBuilder("search", "click", "quicksearch", null).extra("engine", str), false, 1, null);
    }
}
