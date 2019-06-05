package org.mozilla.telemetry.config;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.mozilla.telemetry.measurement.SettingsMeasurement;
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
   private SettingsMeasurement.SettingsProvider settingsProvider;
   private Set telemetryPreferences;
   private String updateChannel;
   private boolean uploadEnabled;
   private String userAgent;

   public TelemetryConfiguration(Context var1) {
      this.context = var1.getApplicationContext();
      this.telemetryPreferences = Collections.emptySet();
      this.setAppName(ContextUtils.getAppName(var1));
      this.setAppVersion(ContextUtils.getVersionName(var1));
      this.setBuildId(String.valueOf(ContextUtils.getVersionCode(var1)));
      this.setUpdateChannel("unknown");
      this.setDataDirectory(new File(var1.getApplicationInfo().dataDir, "telemetry"));
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

   public Set getPreferencesImportantForTelemetry() {
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

   public TelemetryConfiguration setAppName(String var1) {
      this.appName = var1;
      return this;
   }

   public TelemetryConfiguration setAppVersion(String var1) {
      this.appVersion = var1;
      return this;
   }

   public TelemetryConfiguration setBuildId(String var1) {
      this.buildId = var1;
      return this;
   }

   public TelemetryConfiguration setCollectionEnabled(boolean var1) {
      this.collectionEnabled = var1;
      return this;
   }

   public TelemetryConfiguration setConnectTimeout(int var1) {
      this.connectTimeout = var1;
      return this;
   }

   public TelemetryConfiguration setDataDirectory(File var1) {
      StringBuilder var2;
      if (!var1.exists() && !var1.mkdirs()) {
         var2 = new StringBuilder();
         var2.append("Telemetry data directory does not exist and can't be created: ");
         var2.append(var1.getAbsolutePath());
         throw new IllegalStateException(var2.toString());
      } else if (var1.isDirectory() && var1.canWrite()) {
         this.dataDirectory = var1;
         return this;
      } else {
         var2 = new StringBuilder();
         var2.append("Telemetry data directory is not writeable directory");
         var2.append(var1.getAbsolutePath());
         throw new IllegalStateException(var2.toString());
      }
   }

   public TelemetryConfiguration setInitialBackoffForUpload(long var1) {
      this.initialBackoffForUpload = var1;
      return this;
   }

   public TelemetryConfiguration setMaximumNumberOfEventsPerPing(int var1) {
      this.maximumNumberOfEventsPerPing = var1;
      return this;
   }

   public TelemetryConfiguration setMaximumNumberOfPingUploadsPerDay(int var1) {
      this.maximumNumberOfPingUploadsPerDay = var1;
      return this;
   }

   public TelemetryConfiguration setMaximumNumberOfPingsPerType(int var1) {
      this.maximumNumberOfPingsPerType = var1;
      return this;
   }

   public TelemetryConfiguration setMinimumEventsForUpload(int var1) {
      if (var1 > 0) {
         this.minimumEventsForUpload = var1;
         return this;
      } else {
         throw new IllegalArgumentException("minimumEventsForUpload needs to be >= 1");
      }
   }

   public TelemetryConfiguration setPreferencesImportantForTelemetry(String... var1) {
      HashSet var2 = new HashSet();
      Collections.addAll(var2, var1);
      this.telemetryPreferences = var2;
      return this;
   }

   public TelemetryConfiguration setReadTimeout(int var1) {
      this.readTimeout = var1;
      return this;
   }

   public TelemetryConfiguration setServerEndpoint(String var1) {
      this.serverEndpoint = var1;
      return this;
   }

   public TelemetryConfiguration setSettingsProvider(SettingsMeasurement.SettingsProvider var1) {
      this.settingsProvider = var1;
      return this;
   }

   public TelemetryConfiguration setUpdateChannel(String var1) {
      this.updateChannel = var1;
      return this;
   }

   public TelemetryConfiguration setUploadEnabled(boolean var1) {
      this.uploadEnabled = var1;
      return this;
   }

   public TelemetryConfiguration setUserAgent(String var1) {
      this.userAgent = var1;
      return this;
   }
}
