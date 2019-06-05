package org.mozilla.telemetry.config;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.mozilla.telemetry.measurement.SettingsMeasurement.SettingsProvider;
import org.mozilla.telemetry.measurement.SettingsMeasurement.SharedPreferenceSettingsProvider;
import org.mozilla.telemetry.util.ContextUtils;

public class TelemetryConfiguration {
    private static final long classLoadTimestampMillis = System.currentTimeMillis();
    private String appName;
    private String appVersion;
    private String buildId;
    private boolean collectionEnabled;
    private int connectTimeout;
    private final Context context;
    private File dataDirectory;
    private long initialBackoffForUpload;
    private int maximumNumberOfEventsPerPing;
    private int maximumNumberOfPingUploadsPerDay;
    private int maximumNumberOfPingsPerType;
    private int minimumEventsForUpload;
    private int readTimeout;
    private String serverEndpoint;
    private SettingsProvider settingsProvider;
    private Set<String> telemetryPreferences = Collections.emptySet();
    private String updateChannel;
    private boolean uploadEnabled;
    private String userAgent;

    public TelemetryConfiguration(Context context) {
        this.context = context.getApplicationContext();
        setAppName(ContextUtils.getAppName(context));
        setAppVersion(ContextUtils.getVersionName(context));
        setBuildId(String.valueOf(ContextUtils.getVersionCode(context)));
        setUpdateChannel("unknown");
        setDataDirectory(new File(context.getApplicationInfo().dataDir, "telemetry"));
        setServerEndpoint("https://incoming.telemetry.mozilla.org");
        setInitialBackoffForUpload(30000);
        setConnectTimeout(10000);
        setReadTimeout(30000);
        setUserAgent("Telemetry/1.0 (Android)");
        setMinimumEventsForUpload(3);
        setCollectionEnabled(true);
        setUploadEnabled(true);
        setMaximumNumberOfEventsPerPing(500);
        setMaximumNumberOfPingsPerType(40);
        setMaximumNumberOfPingUploadsPerDay(100);
        setSettingsProvider(new SharedPreferenceSettingsProvider());
    }

    public Context getContext() {
        return this.context;
    }

    public TelemetryConfiguration setDataDirectory(File file) {
        StringBuilder stringBuilder;
        if (!file.exists() && !file.mkdirs()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Telemetry data directory does not exist and can't be created: ");
            stringBuilder.append(file.getAbsolutePath());
            throw new IllegalStateException(stringBuilder.toString());
        } else if (file.isDirectory() && file.canWrite()) {
            this.dataDirectory = file;
            return this;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Telemetry data directory is not writeable directory");
            stringBuilder.append(file.getAbsolutePath());
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    public File getDataDirectory() {
        return this.dataDirectory;
    }

    public TelemetryConfiguration setServerEndpoint(String str) {
        this.serverEndpoint = str;
        return this;
    }

    public String getServerEndpoint() {
        return this.serverEndpoint;
    }

    public SharedPreferences getSharedPreferences() {
        return this.context.getSharedPreferences("telemetry_preferences", 0);
    }

    public TelemetryConfiguration setPreferencesImportantForTelemetry(String... strArr) {
        HashSet hashSet = new HashSet();
        Collections.addAll(hashSet, strArr);
        this.telemetryPreferences = hashSet;
        return this;
    }

    public Set<String> getPreferencesImportantForTelemetry() {
        return this.telemetryPreferences;
    }

    public long getInitialBackoffForUpload() {
        return this.initialBackoffForUpload;
    }

    public TelemetryConfiguration setInitialBackoffForUpload(long j) {
        this.initialBackoffForUpload = j;
        return this;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public TelemetryConfiguration setConnectTimeout(int i) {
        this.connectTimeout = i;
        return this;
    }

    public TelemetryConfiguration setReadTimeout(int i) {
        this.readTimeout = i;
        return this;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public TelemetryConfiguration setUserAgent(String str) {
        this.userAgent = str;
        return this;
    }

    public int getMinimumEventsForUpload() {
        return this.minimumEventsForUpload;
    }

    public TelemetryConfiguration setMinimumEventsForUpload(int i) {
        if (i > 0) {
            this.minimumEventsForUpload = i;
            return this;
        }
        throw new IllegalArgumentException("minimumEventsForUpload needs to be >= 1");
    }

    public boolean isCollectionEnabled() {
        return this.collectionEnabled;
    }

    public TelemetryConfiguration setCollectionEnabled(boolean z) {
        this.collectionEnabled = z;
        return this;
    }

    public TelemetryConfiguration setUploadEnabled(boolean z) {
        this.uploadEnabled = z;
        return this;
    }

    public boolean isUploadEnabled() {
        return this.uploadEnabled;
    }

    public String getAppName() {
        return this.appName;
    }

    public TelemetryConfiguration setAppName(String str) {
        this.appName = str;
        return this;
    }

    public TelemetryConfiguration setUpdateChannel(String str) {
        this.updateChannel = str;
        return this;
    }

    public String getUpdateChannel() {
        return this.updateChannel;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public TelemetryConfiguration setAppVersion(String str) {
        this.appVersion = str;
        return this;
    }

    public String getBuildId() {
        return this.buildId;
    }

    public TelemetryConfiguration setBuildId(String str) {
        this.buildId = str;
        return this;
    }

    public TelemetryConfiguration setMaximumNumberOfEventsPerPing(int i) {
        this.maximumNumberOfEventsPerPing = i;
        return this;
    }

    public int getMaximumNumberOfEventsPerPing() {
        return this.maximumNumberOfEventsPerPing;
    }

    public TelemetryConfiguration setMaximumNumberOfPingsPerType(int i) {
        this.maximumNumberOfPingsPerType = i;
        return this;
    }

    public int getMaximumNumberOfPingsPerType() {
        return this.maximumNumberOfPingsPerType;
    }

    public int getMaximumNumberOfPingUploadsPerDay() {
        return this.maximumNumberOfPingUploadsPerDay;
    }

    public TelemetryConfiguration setMaximumNumberOfPingUploadsPerDay(int i) {
        this.maximumNumberOfPingUploadsPerDay = i;
        return this;
    }

    public SettingsProvider getSettingsProvider() {
        return this.settingsProvider;
    }

    public TelemetryConfiguration setSettingsProvider(SettingsProvider settingsProvider) {
        this.settingsProvider = settingsProvider;
        return this;
    }
}
