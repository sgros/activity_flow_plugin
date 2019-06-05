// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import android.content.ContentResolver;
import java.util.HashMap;
import java.util.Iterator;
import android.text.TextUtils;
import org.json.JSONObject;
import java.util.Locale;
import java.util.Map;

class PackageBuilder
{
    private static ILogger logger;
    private ActivityStateCopy activityStateCopy;
    private AdjustConfig adjustConfig;
    AdjustAttribution attribution;
    long clickTime;
    private long createdAt;
    String deeplink;
    private DeviceInfo deviceInfo;
    Map<String, String> extraParameters;
    String referrer;
    String reftag;
    
    static {
        PackageBuilder.logger = AdjustFactory.getLogger();
    }
    
    public PackageBuilder(final AdjustConfig adjustConfig, final DeviceInfo deviceInfo, final ActivityState activityState, final long createdAt) {
        this.adjustConfig = adjustConfig;
        this.deviceInfo = deviceInfo;
        this.activityStateCopy = new ActivityStateCopy(activityState);
        this.createdAt = createdAt;
    }
    
    public static void addBoolean(final Map<String, String> map, final String s, final Boolean b) {
        if (b == null) {
            return;
        }
        addInt(map, s, ((boolean)b) ? 1 : 0);
    }
    
    public static void addDate(final Map<String, String> map, final String s, final long l) {
        if (l < 0L) {
            return;
        }
        addString(map, s, Util.dateFormatter.format(l));
    }
    
    public static void addDouble(final Map<String, String> map, final String s, final Double n) {
        if (n == null) {
            return;
        }
        addString(map, s, String.format(Locale.US, "%.5f", n));
    }
    
    public static void addDuration(final Map<String, String> map, final String s, final long n) {
        if (n < 0L) {
            return;
        }
        addInt(map, s, (n + 500L) / 1000L);
    }
    
    public static void addInt(final Map<String, String> map, final String s, final long i) {
        if (i < 0L) {
            return;
        }
        addString(map, s, Long.toString(i));
    }
    
    public static void addMapJson(final Map<String, String> map, final String s, final Map<String, String> map2) {
        if (map2 == null) {
            return;
        }
        if (map2.size() == 0) {
            return;
        }
        addString(map, s, new JSONObject((Map)map2).toString());
    }
    
    public static void addString(final Map<String, String> map, final String s, final String s2) {
        if (TextUtils.isEmpty((CharSequence)s2)) {
            return;
        }
        map.put(s, s2);
    }
    
    private void checkDeviceIds(final Map<String, String> map) {
        if (!map.containsKey("mac_sha1") && !map.containsKey("mac_md5") && !map.containsKey("android_id") && !map.containsKey("gps_adid")) {
            PackageBuilder.logger.error("Missing device id's. Please check if Proguard is correctly set with Adjust SDK", new Object[0]);
        }
    }
    
    private void fillPluginKeys(final Map<String, String> map) {
        if (this.deviceInfo.pluginKeys == null) {
            return;
        }
        for (final Map.Entry<String, String> entry : this.deviceInfo.pluginKeys.entrySet()) {
            addString(map, entry.getKey(), entry.getValue());
        }
    }
    
    private Map<String, String> getAttributableParameters(final SessionParameters sessionParameters) {
        final Map<String, String> defaultParameters = this.getDefaultParameters();
        addDuration(defaultParameters, "last_interval", this.activityStateCopy.lastInterval);
        addString(defaultParameters, "default_tracker", this.adjustConfig.defaultTracker);
        addString(defaultParameters, "installed_at", this.deviceInfo.appInstallTime);
        addString(defaultParameters, "updated_at", this.deviceInfo.appUpdateTime);
        if (sessionParameters != null) {
            addMapJson(defaultParameters, "callback_params", sessionParameters.callbackParameters);
            addMapJson(defaultParameters, "partner_params", sessionParameters.partnerParameters);
        }
        return defaultParameters;
    }
    
    private ActivityPackage getDefaultActivityPackage(final ActivityKind activityKind) {
        final ActivityPackage activityPackage = new ActivityPackage(activityKind);
        activityPackage.setClientSdk(this.deviceInfo.clientSdk);
        return activityPackage;
    }
    
    private Map<String, String> getDefaultParameters() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        this.injectDeviceInfo(hashMap);
        this.injectConfig(hashMap);
        this.injectActivityState(hashMap);
        this.injectCommonParameters(hashMap);
        this.checkDeviceIds(hashMap);
        return hashMap;
    }
    
    private String getEventSuffix(final AdjustEvent adjustEvent) {
        if (adjustEvent.revenue == null) {
            return String.format(Locale.US, "'%s'", adjustEvent.eventToken);
        }
        return String.format(Locale.US, "(%.5f %s, '%s')", adjustEvent.revenue, adjustEvent.currency, adjustEvent.eventToken);
    }
    
    private Map<String, String> getIdsParameters() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        this.injectDeviceInfoIds(hashMap);
        this.injectConfig(hashMap);
        this.injectCommonParameters(hashMap);
        this.checkDeviceIds(hashMap);
        return hashMap;
    }
    
    private void injectActivityState(final Map<String, String> map) {
        addString(map, "android_uuid", this.activityStateCopy.uuid);
        addInt(map, "session_count", this.activityStateCopy.sessionCount);
        addInt(map, "subsession_count", this.activityStateCopy.subsessionCount);
        addDuration(map, "session_length", this.activityStateCopy.sessionLength);
        addDuration(map, "time_spent", this.activityStateCopy.timeSpent);
    }
    
    private void injectAttribution(final Map<String, String> map) {
        if (this.attribution == null) {
            return;
        }
        addString(map, "tracker", this.attribution.trackerName);
        addString(map, "campaign", this.attribution.campaign);
        addString(map, "adgroup", this.attribution.adgroup);
        addString(map, "creative", this.attribution.creative);
    }
    
    private void injectCommonParameters(final Map<String, String> map) {
        addDate(map, "created_at", this.createdAt);
        addBoolean(map, "attribution_deeplink", true);
    }
    
    private void injectConfig(final Map<String, String> map) {
        addString(map, "app_token", this.adjustConfig.appToken);
        addString(map, "environment", this.adjustConfig.environment);
        addBoolean(map, "device_known", this.adjustConfig.deviceKnown);
        addBoolean(map, "needs_response_details", true);
        addString(map, "gps_adid", Util.getPlayAdId(this.adjustConfig.context));
        addBoolean(map, "tracking_enabled", Util.isPlayTrackingEnabled(this.adjustConfig.context));
        addBoolean(map, "event_buffering_enabled", this.adjustConfig.eventBufferingEnabled);
        addString(map, "push_token", this.activityStateCopy.pushToken);
        final ContentResolver contentResolver = this.adjustConfig.context.getContentResolver();
        addString(map, "fire_adid", Util.getFireAdvertisingId(contentResolver));
        addBoolean(map, "fire_tracking_enabled", Util.getFireTrackingEnabled(contentResolver));
    }
    
    private void injectDeviceInfo(final Map<String, String> map) {
        this.injectDeviceInfoIds(map);
        addString(map, "fb_id", this.deviceInfo.fbAttributionId);
        addString(map, "package_name", this.deviceInfo.packageName);
        addString(map, "app_version", this.deviceInfo.appVersion);
        addString(map, "device_type", this.deviceInfo.deviceType);
        addString(map, "device_name", this.deviceInfo.deviceName);
        addString(map, "device_manufacturer", this.deviceInfo.deviceManufacturer);
        addString(map, "os_name", this.deviceInfo.osName);
        addString(map, "os_version", this.deviceInfo.osVersion);
        addString(map, "api_level", this.deviceInfo.apiLevel);
        addString(map, "language", this.deviceInfo.language);
        addString(map, "country", this.deviceInfo.country);
        addString(map, "screen_size", this.deviceInfo.screenSize);
        addString(map, "screen_format", this.deviceInfo.screenFormat);
        addString(map, "screen_density", this.deviceInfo.screenDensity);
        addString(map, "display_width", this.deviceInfo.displayWidth);
        addString(map, "display_height", this.deviceInfo.displayHeight);
        addString(map, "hardware_name", this.deviceInfo.hardwareName);
        addString(map, "cpu_type", this.deviceInfo.abi);
        addString(map, "os_build", this.deviceInfo.buildName);
        addString(map, "vm_isa", this.deviceInfo.vmInstructionSet);
        this.fillPluginKeys(map);
    }
    
    private void injectDeviceInfoIds(final Map<String, String> map) {
        addString(map, "mac_sha1", this.deviceInfo.macSha1);
        addString(map, "mac_md5", this.deviceInfo.macShortMd5);
        addString(map, "android_id", this.deviceInfo.androidId);
    }
    
    public ActivityPackage buildAttributionPackage() {
        final Map<String, String> idsParameters = this.getIdsParameters();
        final ActivityPackage defaultActivityPackage = this.getDefaultActivityPackage(ActivityKind.ATTRIBUTION);
        defaultActivityPackage.setPath("attribution");
        defaultActivityPackage.setSuffix("");
        defaultActivityPackage.setParameters(idsParameters);
        return defaultActivityPackage;
    }
    
    public ActivityPackage buildClickPackage(final String s, final SessionParameters sessionParameters) {
        final Map<String, String> attributableParameters = this.getAttributableParameters(sessionParameters);
        addString(attributableParameters, "source", s);
        addDate(attributableParameters, "click_time", this.clickTime);
        addString(attributableParameters, "reftag", this.reftag);
        addMapJson(attributableParameters, "params", this.extraParameters);
        addString(attributableParameters, "referrer", this.referrer);
        addString(attributableParameters, "deeplink", this.deeplink);
        this.injectAttribution(attributableParameters);
        final ActivityPackage defaultActivityPackage = this.getDefaultActivityPackage(ActivityKind.CLICK);
        defaultActivityPackage.setPath("/sdk_click");
        defaultActivityPackage.setSuffix("");
        defaultActivityPackage.setParameters(attributableParameters);
        return defaultActivityPackage;
    }
    
    public ActivityPackage buildEventPackage(final AdjustEvent adjustEvent, final SessionParameters sessionParameters, final boolean b) {
        final Map<String, String> defaultParameters = this.getDefaultParameters();
        addInt(defaultParameters, "event_count", this.activityStateCopy.eventCount);
        addString(defaultParameters, "event_token", adjustEvent.eventToken);
        addDouble(defaultParameters, "revenue", adjustEvent.revenue);
        addString(defaultParameters, "currency", adjustEvent.currency);
        if (!b) {
            addMapJson(defaultParameters, "callback_params", Util.mergeParameters(sessionParameters.callbackParameters, adjustEvent.callbackParameters, "Callback"));
            addMapJson(defaultParameters, "partner_params", Util.mergeParameters(sessionParameters.partnerParameters, adjustEvent.partnerParameters, "Partner"));
        }
        final ActivityPackage defaultActivityPackage = this.getDefaultActivityPackage(ActivityKind.EVENT);
        defaultActivityPackage.setPath("/event");
        defaultActivityPackage.setSuffix(this.getEventSuffix(adjustEvent));
        defaultActivityPackage.setParameters(defaultParameters);
        if (b) {
            defaultActivityPackage.setCallbackParameters(adjustEvent.callbackParameters);
            defaultActivityPackage.setPartnerParameters(adjustEvent.partnerParameters);
        }
        return defaultActivityPackage;
    }
    
    public ActivityPackage buildInfoPackage(final String s) {
        final Map<String, String> idsParameters = this.getIdsParameters();
        addString(idsParameters, "source", s);
        final ActivityPackage defaultActivityPackage = this.getDefaultActivityPackage(ActivityKind.INFO);
        defaultActivityPackage.setPath("/sdk_info");
        defaultActivityPackage.setSuffix("");
        defaultActivityPackage.setParameters(idsParameters);
        return defaultActivityPackage;
    }
    
    public ActivityPackage buildSessionPackage(final SessionParameters sessionParameters, final boolean b) {
        Map<String, String> parameters;
        if (!b) {
            parameters = this.getAttributableParameters(sessionParameters);
        }
        else {
            parameters = this.getAttributableParameters(null);
        }
        final ActivityPackage defaultActivityPackage = this.getDefaultActivityPackage(ActivityKind.SESSION);
        defaultActivityPackage.setPath("/session");
        defaultActivityPackage.setSuffix("");
        defaultActivityPackage.setParameters(parameters);
        return defaultActivityPackage;
    }
    
    private class ActivityStateCopy
    {
        int eventCount;
        long lastInterval;
        String pushToken;
        int sessionCount;
        long sessionLength;
        int subsessionCount;
        long timeSpent;
        String uuid;
        
        ActivityStateCopy(final ActivityState activityState) {
            this.lastInterval = -1L;
            this.eventCount = -1;
            this.uuid = null;
            this.sessionCount = -1;
            this.subsessionCount = -1;
            this.sessionLength = -1L;
            this.timeSpent = -1L;
            this.pushToken = null;
            if (activityState == null) {
                return;
            }
            this.lastInterval = activityState.lastInterval;
            this.eventCount = activityState.eventCount;
            this.uuid = activityState.uuid;
            this.sessionCount = activityState.sessionCount;
            this.subsessionCount = activityState.subsessionCount;
            this.sessionLength = activityState.sessionLength;
            this.timeSpent = activityState.timeSpent;
            this.pushToken = activityState.pushToken;
        }
    }
}
