package com.adjust.sdk;

import android.content.ContentResolver;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONObject;

class PackageBuilder {
   private static ILogger logger = AdjustFactory.getLogger();
   private PackageBuilder.ActivityStateCopy activityStateCopy;
   private AdjustConfig adjustConfig;
   AdjustAttribution attribution;
   long clickTime;
   private long createdAt;
   String deeplink;
   private DeviceInfo deviceInfo;
   Map extraParameters;
   String referrer;
   String reftag;

   public PackageBuilder(AdjustConfig var1, DeviceInfo var2, ActivityState var3, long var4) {
      this.adjustConfig = var1;
      this.deviceInfo = var2;
      this.activityStateCopy = new PackageBuilder.ActivityStateCopy(var3);
      this.createdAt = var4;
   }

   public static void addBoolean(Map var0, String var1, Boolean var2) {
      if (var2 != null) {
         addInt(var0, var1, (long)var2);
      }
   }

   public static void addDate(Map var0, String var1, long var2) {
      if (var2 >= 0L) {
         addString(var0, var1, Util.dateFormatter.format(var2));
      }
   }

   public static void addDouble(Map var0, String var1, Double var2) {
      if (var2 != null) {
         addString(var0, var1, String.format(Locale.US, "%.5f", var2));
      }
   }

   public static void addDuration(Map var0, String var1, long var2) {
      if (var2 >= 0L) {
         addInt(var0, var1, (var2 + 500L) / 1000L);
      }
   }

   public static void addInt(Map var0, String var1, long var2) {
      if (var2 >= 0L) {
         addString(var0, var1, Long.toString(var2));
      }
   }

   public static void addMapJson(Map var0, String var1, Map var2) {
      if (var2 != null) {
         if (var2.size() != 0) {
            addString(var0, var1, (new JSONObject(var2)).toString());
         }
      }
   }

   public static void addString(Map var0, String var1, String var2) {
      if (!TextUtils.isEmpty(var2)) {
         var0.put(var1, var2);
      }
   }

   private void checkDeviceIds(Map var1) {
      if (!var1.containsKey("mac_sha1") && !var1.containsKey("mac_md5") && !var1.containsKey("android_id") && !var1.containsKey("gps_adid")) {
         logger.error("Missing device id's. Please check if Proguard is correctly set with Adjust SDK");
      }

   }

   private void fillPluginKeys(Map var1) {
      if (this.deviceInfo.pluginKeys != null) {
         Iterator var2 = this.deviceInfo.pluginKeys.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            addString(var1, (String)var3.getKey(), (String)var3.getValue());
         }

      }
   }

   private Map getAttributableParameters(SessionParameters var1) {
      Map var2 = this.getDefaultParameters();
      addDuration(var2, "last_interval", this.activityStateCopy.lastInterval);
      addString(var2, "default_tracker", this.adjustConfig.defaultTracker);
      addString(var2, "installed_at", this.deviceInfo.appInstallTime);
      addString(var2, "updated_at", this.deviceInfo.appUpdateTime);
      if (var1 != null) {
         addMapJson(var2, "callback_params", var1.callbackParameters);
         addMapJson(var2, "partner_params", var1.partnerParameters);
      }

      return var2;
   }

   private ActivityPackage getDefaultActivityPackage(ActivityKind var1) {
      ActivityPackage var2 = new ActivityPackage(var1);
      var2.setClientSdk(this.deviceInfo.clientSdk);
      return var2;
   }

   private Map getDefaultParameters() {
      HashMap var1 = new HashMap();
      this.injectDeviceInfo(var1);
      this.injectConfig(var1);
      this.injectActivityState(var1);
      this.injectCommonParameters(var1);
      this.checkDeviceIds(var1);
      return var1;
   }

   private String getEventSuffix(AdjustEvent var1) {
      return var1.revenue == null ? String.format(Locale.US, "'%s'", var1.eventToken) : String.format(Locale.US, "(%.5f %s, '%s')", var1.revenue, var1.currency, var1.eventToken);
   }

   private Map getIdsParameters() {
      HashMap var1 = new HashMap();
      this.injectDeviceInfoIds(var1);
      this.injectConfig(var1);
      this.injectCommonParameters(var1);
      this.checkDeviceIds(var1);
      return var1;
   }

   private void injectActivityState(Map var1) {
      addString(var1, "android_uuid", this.activityStateCopy.uuid);
      addInt(var1, "session_count", (long)this.activityStateCopy.sessionCount);
      addInt(var1, "subsession_count", (long)this.activityStateCopy.subsessionCount);
      addDuration(var1, "session_length", this.activityStateCopy.sessionLength);
      addDuration(var1, "time_spent", this.activityStateCopy.timeSpent);
   }

   private void injectAttribution(Map var1) {
      if (this.attribution != null) {
         addString(var1, "tracker", this.attribution.trackerName);
         addString(var1, "campaign", this.attribution.campaign);
         addString(var1, "adgroup", this.attribution.adgroup);
         addString(var1, "creative", this.attribution.creative);
      }
   }

   private void injectCommonParameters(Map var1) {
      addDate(var1, "created_at", this.createdAt);
      addBoolean(var1, "attribution_deeplink", true);
   }

   private void injectConfig(Map var1) {
      addString(var1, "app_token", this.adjustConfig.appToken);
      addString(var1, "environment", this.adjustConfig.environment);
      addBoolean(var1, "device_known", this.adjustConfig.deviceKnown);
      addBoolean(var1, "needs_response_details", true);
      addString(var1, "gps_adid", Util.getPlayAdId(this.adjustConfig.context));
      addBoolean(var1, "tracking_enabled", Util.isPlayTrackingEnabled(this.adjustConfig.context));
      addBoolean(var1, "event_buffering_enabled", this.adjustConfig.eventBufferingEnabled);
      addString(var1, "push_token", this.activityStateCopy.pushToken);
      ContentResolver var2 = this.adjustConfig.context.getContentResolver();
      addString(var1, "fire_adid", Util.getFireAdvertisingId(var2));
      addBoolean(var1, "fire_tracking_enabled", Util.getFireTrackingEnabled(var2));
   }

   private void injectDeviceInfo(Map var1) {
      this.injectDeviceInfoIds(var1);
      addString(var1, "fb_id", this.deviceInfo.fbAttributionId);
      addString(var1, "package_name", this.deviceInfo.packageName);
      addString(var1, "app_version", this.deviceInfo.appVersion);
      addString(var1, "device_type", this.deviceInfo.deviceType);
      addString(var1, "device_name", this.deviceInfo.deviceName);
      addString(var1, "device_manufacturer", this.deviceInfo.deviceManufacturer);
      addString(var1, "os_name", this.deviceInfo.osName);
      addString(var1, "os_version", this.deviceInfo.osVersion);
      addString(var1, "api_level", this.deviceInfo.apiLevel);
      addString(var1, "language", this.deviceInfo.language);
      addString(var1, "country", this.deviceInfo.country);
      addString(var1, "screen_size", this.deviceInfo.screenSize);
      addString(var1, "screen_format", this.deviceInfo.screenFormat);
      addString(var1, "screen_density", this.deviceInfo.screenDensity);
      addString(var1, "display_width", this.deviceInfo.displayWidth);
      addString(var1, "display_height", this.deviceInfo.displayHeight);
      addString(var1, "hardware_name", this.deviceInfo.hardwareName);
      addString(var1, "cpu_type", this.deviceInfo.abi);
      addString(var1, "os_build", this.deviceInfo.buildName);
      addString(var1, "vm_isa", this.deviceInfo.vmInstructionSet);
      this.fillPluginKeys(var1);
   }

   private void injectDeviceInfoIds(Map var1) {
      addString(var1, "mac_sha1", this.deviceInfo.macSha1);
      addString(var1, "mac_md5", this.deviceInfo.macShortMd5);
      addString(var1, "android_id", this.deviceInfo.androidId);
   }

   public ActivityPackage buildAttributionPackage() {
      Map var1 = this.getIdsParameters();
      ActivityPackage var2 = this.getDefaultActivityPackage(ActivityKind.ATTRIBUTION);
      var2.setPath("attribution");
      var2.setSuffix("");
      var2.setParameters(var1);
      return var2;
   }

   public ActivityPackage buildClickPackage(String var1, SessionParameters var2) {
      Map var4 = this.getAttributableParameters(var2);
      addString(var4, "source", var1);
      addDate(var4, "click_time", this.clickTime);
      addString(var4, "reftag", this.reftag);
      addMapJson(var4, "params", this.extraParameters);
      addString(var4, "referrer", this.referrer);
      addString(var4, "deeplink", this.deeplink);
      this.injectAttribution(var4);
      ActivityPackage var3 = this.getDefaultActivityPackage(ActivityKind.CLICK);
      var3.setPath("/sdk_click");
      var3.setSuffix("");
      var3.setParameters(var4);
      return var3;
   }

   public ActivityPackage buildEventPackage(AdjustEvent var1, SessionParameters var2, boolean var3) {
      Map var4 = this.getDefaultParameters();
      addInt(var4, "event_count", (long)this.activityStateCopy.eventCount);
      addString(var4, "event_token", var1.eventToken);
      addDouble(var4, "revenue", var1.revenue);
      addString(var4, "currency", var1.currency);
      if (!var3) {
         addMapJson(var4, "callback_params", Util.mergeParameters(var2.callbackParameters, var1.callbackParameters, "Callback"));
         addMapJson(var4, "partner_params", Util.mergeParameters(var2.partnerParameters, var1.partnerParameters, "Partner"));
      }

      ActivityPackage var5 = this.getDefaultActivityPackage(ActivityKind.EVENT);
      var5.setPath("/event");
      var5.setSuffix(this.getEventSuffix(var1));
      var5.setParameters(var4);
      if (var3) {
         var5.setCallbackParameters(var1.callbackParameters);
         var5.setPartnerParameters(var1.partnerParameters);
      }

      return var5;
   }

   public ActivityPackage buildInfoPackage(String var1) {
      Map var2 = this.getIdsParameters();
      addString(var2, "source", var1);
      ActivityPackage var3 = this.getDefaultActivityPackage(ActivityKind.INFO);
      var3.setPath("/sdk_info");
      var3.setSuffix("");
      var3.setParameters(var2);
      return var3;
   }

   public ActivityPackage buildSessionPackage(SessionParameters var1, boolean var2) {
      Map var4;
      if (!var2) {
         var4 = this.getAttributableParameters(var1);
      } else {
         var4 = this.getAttributableParameters((SessionParameters)null);
      }

      ActivityPackage var3 = this.getDefaultActivityPackage(ActivityKind.SESSION);
      var3.setPath("/session");
      var3.setSuffix("");
      var3.setParameters(var4);
      return var3;
   }

   private class ActivityStateCopy {
      int eventCount = -1;
      long lastInterval = -1L;
      String pushToken = null;
      int sessionCount = -1;
      long sessionLength = -1L;
      int subsessionCount = -1;
      long timeSpent = -1L;
      String uuid = null;

      ActivityStateCopy(ActivityState var2) {
         if (var2 != null) {
            this.lastInterval = var2.lastInterval;
            this.eventCount = var2.eventCount;
            this.uuid = var2.uuid;
            this.sessionCount = var2.sessionCount;
            this.subsessionCount = var2.subsessionCount;
            this.sessionLength = var2.sessionLength;
            this.timeSpent = var2.timeSpent;
            this.pushToken = var2.pushToken;
         }
      }
   }
}
