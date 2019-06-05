// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.config;

import java.util.Collection;
import java.util.HashSet;
import android.content.SharedPreferences;
import org.mozilla.telemetry.util.ContextUtils;
import java.util.Collections;
import java.util.Set;
import org.mozilla.telemetry.measurement.SettingsMeasurement;
import java.io.File;
import android.content.Context;

public class TelemetryConfiguration
{
    private static final long classLoadTimestampMillis;
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
    private SettingsMeasurement.SettingsProvider settingsProvider;
    private Set<String> telemetryPreferences;
    private String updateChannel;
    private boolean uploadEnabled;
    private String userAgent;
    
    static {
        classLoadTimestampMillis = System.currentTimeMillis();
    }
    
    public TelemetryConfiguration(final Context context) {
        this.context = context.getApplicationContext();
        this.telemetryPreferences = Collections.emptySet();
        this.setAppName(ContextUtils.getAppName(context));
        this.setAppVersion(ContextUtils.getVersionName(context));
        this.setBuildId(String.valueOf(ContextUtils.getVersionCode(context)));
        this.setUpdateChannel("unknown");
        this.setDataDirectory(new File(context.getApplicationInfo().dataDir, "telemetry"));
        this.setServerEndpoint("https://incoming.telemetry.mozilla.org");
        this.setInitialBackoffForUpload(30000L);
        this.setConnectTimeout(10000);
        this.setReadTimeout(30000);
        this.setUserAgent("Telemetry/1.0 (Android)");
        this.setMinimumEventsForUpload(3);
        this.setCollectionEnabled(true);
        this.setUploadEnabled(true);
        this.setMaximumNumberOfEventsPerPing(500);
        this.setMaximumNumberOfPingsPerType(40);
        this.setMaximumNumberOfPingUploadsPerDay(100);
        this.setSettingsProvider(new SettingsMeasurement.SharedPreferenceSettingsProvider());
    }
    
    public String getAppName() {
        return this.appName;
    }
    
    public String getAppVersion() {
        return this.appVersion;
    }
    
    public String getBuildId() {
        return this.buildId;
    }
    
    public int getConnectTimeout() {
        return this.connectTimeout;
    }
    
    public Context getContext() {
        return this.context;
    }
    
    public File getDataDirectory() {
        return this.dataDirectory;
    }
    
    public long getInitialBackoffForUpload() {
        return this.initialBackoffForUpload;
    }
    
    public int getMaximumNumberOfEventsPerPing() {
        return this.maximumNumberOfEventsPerPing;
    }
    
    public int getMaximumNumberOfPingUploadsPerDay() {
        return this.maximumNumberOfPingUploadsPerDay;
    }
    
    public int getMaximumNumberOfPingsPerType() {
        return this.maximumNumberOfPingsPerType;
    }
    
    public int getMinimumEventsForUpload() {
        return this.minimumEventsForUpload;
    }
    
    public Set<String> getPreferencesImportantForTelemetry() {
        return this.telemetryPreferences;
    }
    
    public int getReadTimeout() {
        return this.readTimeout;
    }
    
    public String getServerEndpoint() {
        return this.serverEndpoint;
    }
    
    public SettingsMeasurement.SettingsProvider getSettingsProvider() {
        return this.settingsProvider;
    }
    
    public SharedPreferences getSharedPreferences() {
        return this.context.getSharedPreferences("telemetry_preferences", 0);
    }
    
    public String getUpdateChannel() {
        return this.updateChannel;
    }
    
    public String getUserAgent() {
        return this.userAgent;
    }
    
    public boolean isCollectionEnabled() {
        return this.collectionEnabled;
    }
    
    public boolean isUploadEnabled() {
        return this.uploadEnabled;
    }
    
    public TelemetryConfiguration setAppName(final String appName) {
        this.appName = appName;
        return this;
    }
    
    public TelemetryConfiguration setAppVersion(final String appVersion) {
        this.appVersion = appVersion;
        return this;
    }
    
    public TelemetryConfiguration setBuildId(final String buildId) {
        this.buildId = buildId;
        return this;
    }
    
    public TelemetryConfiguration setCollectionEnabled(final boolean collectionEnabled) {
        this.collectionEnabled = collectionEnabled;
        return this;
    }
    
    public TelemetryConfiguration setConnectTimeout(final int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }
    
    public TelemetryConfiguration setDataDirectory(final File dataDirectory) {
        if (!dataDirectory.exists() && !dataDirectory.mkdirs()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Telemetry data directory does not exist and can't be created: ");
            sb.append(dataDirectory.getAbsolutePath());
            throw new IllegalStateException(sb.toString());
        }
        if (dataDirectory.isDirectory() && dataDirectory.canWrite()) {
            this.dataDirectory = dataDirectory;
            return this;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Telemetry data directory is not writeable directory");
        sb2.append(dataDirectory.getAbsolutePath());
        throw new IllegalStateException(sb2.toString());
    }
    
    public TelemetryConfiguration setInitialBackoffForUpload(final long initialBackoffForUpload) {
        this.initialBackoffForUpload = initialBackoffForUpload;
        return this;
    }
    
    public TelemetryConfiguration setMaximumNumberOfEventsPerPing(final int maximumNumberOfEventsPerPing) {
        this.maximumNumberOfEventsPerPing = maximumNumberOfEventsPerPing;
        return this;
    }
    
    public TelemetryConfiguration setMaximumNumberOfPingUploadsPerDay(final int maximumNumberOfPingUploadsPerDay) {
        this.maximumNumberOfPingUploadsPerDay = maximumNumberOfPingUploadsPerDay;
        return this;
    }
    
    public TelemetryConfiguration setMaximumNumberOfPingsPerType(final int maximumNumberOfPingsPerType) {
        this.maximumNumberOfPingsPerType = maximumNumberOfPingsPerType;
        return this;
    }
    
    public TelemetryConfiguration setMinimumEventsForUpload(final int minimumEventsForUpload) {
        if (minimumEventsForUpload > 0) {
            this.minimumEventsForUpload = minimumEventsForUpload;
            return this;
        }
        throw new IllegalArgumentException("minimumEventsForUpload needs to be >= 1");
    }
    
    public TelemetryConfiguration setPreferencesImportantForTelemetry(final String... elements) {
        final HashSet<String> set = new HashSet<String>();
        Collections.addAll(set, elements);
        this.telemetryPreferences = set;
        return this;
    }
    
    public TelemetryConfiguration setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }
    
    public TelemetryConfiguration setServerEndpoint(final String serverEndpoint) {
        this.serverEndpoint = serverEndpoint;
        return this;
    }
    
    public TelemetryConfiguration setSettingsProvider(final SettingsMeasurement.SettingsProvider settingsProvider) {
        this.settingsProvider = settingsProvider;
        return this;
    }
    
    public TelemetryConfiguration setUpdateChannel(final String updateChannel) {
        this.updateChannel = updateChannel;
        return this;
    }
    
    public TelemetryConfiguration setUploadEnabled(final boolean uploadEnabled) {
        this.uploadEnabled = uploadEnabled;
        return this;
    }
    
    public TelemetryConfiguration setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
        return this;
    }
}
