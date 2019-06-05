package com.adjust.sdk;

import android.content.ContentResolver;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONObject;

class PackageBuilder {
    private static ILogger logger = AdjustFactory.getLogger();
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

    private class ActivityStateCopy {
        int eventCount = -1;
        long lastInterval = -1;
        String pushToken = null;
        int sessionCount = -1;
        long sessionLength = -1;
        int subsessionCount = -1;
        long timeSpent = -1;
        String uuid = null;

        ActivityStateCopy(ActivityState activityState) {
            if (activityState != null) {
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

    public PackageBuilder(AdjustConfig adjustConfig, DeviceInfo deviceInfo, ActivityState activityState, long j) {
        this.adjustConfig = adjustConfig;
        this.deviceInfo = deviceInfo;
        this.activityStateCopy = new ActivityStateCopy(activityState);
        this.createdAt = j;
    }

    public ActivityPackage buildSessionPackage(SessionParameters sessionParameters, boolean z) {
        Map attributableParameters;
        if (z) {
            attributableParameters = getAttributableParameters(null);
        } else {
            attributableParameters = getAttributableParameters(sessionParameters);
        }
        ActivityPackage defaultActivityPackage = getDefaultActivityPackage(ActivityKind.SESSION);
        defaultActivityPackage.setPath("/session");
        defaultActivityPackage.setSuffix("");
        defaultActivityPackage.setParameters(attributableParameters);
        return defaultActivityPackage;
    }

    public ActivityPackage buildEventPackage(AdjustEvent adjustEvent, SessionParameters sessionParameters, boolean z) {
        Map defaultParameters = getDefaultParameters();
        addInt(defaultParameters, "event_count", (long) this.activityStateCopy.eventCount);
        addString(defaultParameters, "event_token", adjustEvent.eventToken);
        addDouble(defaultParameters, "revenue", adjustEvent.revenue);
        addString(defaultParameters, "currency", adjustEvent.currency);
        if (!z) {
            addMapJson(defaultParameters, Constants.CALLBACK_PARAMETERS, Util.mergeParameters(sessionParameters.callbackParameters, adjustEvent.callbackParameters, "Callback"));
            addMapJson(defaultParameters, Constants.PARTNER_PARAMETERS, Util.mergeParameters(sessionParameters.partnerParameters, adjustEvent.partnerParameters, "Partner"));
        }
        ActivityPackage defaultActivityPackage = getDefaultActivityPackage(ActivityKind.EVENT);
        defaultActivityPackage.setPath("/event");
        defaultActivityPackage.setSuffix(getEventSuffix(adjustEvent));
        defaultActivityPackage.setParameters(defaultParameters);
        if (z) {
            defaultActivityPackage.setCallbackParameters(adjustEvent.callbackParameters);
            defaultActivityPackage.setPartnerParameters(adjustEvent.partnerParameters);
        }
        return defaultActivityPackage;
    }

    public ActivityPackage buildClickPackage(String str, SessionParameters sessionParameters) {
        Map attributableParameters = getAttributableParameters(sessionParameters);
        addString(attributableParameters, "source", str);
        addDate(attributableParameters, "click_time", this.clickTime);
        addString(attributableParameters, Constants.REFTAG, this.reftag);
        addMapJson(attributableParameters, "params", this.extraParameters);
        addString(attributableParameters, Constants.REFERRER, this.referrer);
        addString(attributableParameters, Constants.DEEPLINK, this.deeplink);
        injectAttribution(attributableParameters);
        ActivityPackage defaultActivityPackage = getDefaultActivityPackage(ActivityKind.CLICK);
        defaultActivityPackage.setPath("/sdk_click");
        defaultActivityPackage.setSuffix("");
        defaultActivityPackage.setParameters(attributableParameters);
        return defaultActivityPackage;
    }

    public ActivityPackage buildInfoPackage(String str) {
        Map idsParameters = getIdsParameters();
        addString(idsParameters, "source", str);
        ActivityPackage defaultActivityPackage = getDefaultActivityPackage(ActivityKind.INFO);
        defaultActivityPackage.setPath("/sdk_info");
        defaultActivityPackage.setSuffix("");
        defaultActivityPackage.setParameters(idsParameters);
        return defaultActivityPackage;
    }

    public ActivityPackage buildAttributionPackage() {
        Map idsParameters = getIdsParameters();
        ActivityPackage defaultActivityPackage = getDefaultActivityPackage(ActivityKind.ATTRIBUTION);
        defaultActivityPackage.setPath("attribution");
        defaultActivityPackage.setSuffix("");
        defaultActivityPackage.setParameters(idsParameters);
        return defaultActivityPackage;
    }

    private ActivityPackage getDefaultActivityPackage(ActivityKind activityKind) {
        ActivityPackage activityPackage = new ActivityPackage(activityKind);
        activityPackage.setClientSdk(this.deviceInfo.clientSdk);
        return activityPackage;
    }

    private Map<String, String> getAttributableParameters(SessionParameters sessionParameters) {
        Map defaultParameters = getDefaultParameters();
        addDuration(defaultParameters, "last_interval", this.activityStateCopy.lastInterval);
        addString(defaultParameters, "default_tracker", this.adjustConfig.defaultTracker);
        addString(defaultParameters, "installed_at", this.deviceInfo.appInstallTime);
        addString(defaultParameters, "updated_at", this.deviceInfo.appUpdateTime);
        if (sessionParameters != null) {
            addMapJson(defaultParameters, Constants.CALLBACK_PARAMETERS, sessionParameters.callbackParameters);
            addMapJson(defaultParameters, Constants.PARTNER_PARAMETERS, sessionParameters.partnerParameters);
        }
        return defaultParameters;
    }

    private Map<String, String> getDefaultParameters() {
        HashMap hashMap = new HashMap();
        injectDeviceInfo(hashMap);
        injectConfig(hashMap);
        injectActivityState(hashMap);
        injectCommonParameters(hashMap);
        checkDeviceIds(hashMap);
        return hashMap;
    }

    private Map<String, String> getIdsParameters() {
        HashMap hashMap = new HashMap();
        injectDeviceInfoIds(hashMap);
        injectConfig(hashMap);
        injectCommonParameters(hashMap);
        checkDeviceIds(hashMap);
        return hashMap;
    }

    private void injectDeviceInfo(Map<String, String> map) {
        injectDeviceInfoIds(map);
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
        fillPluginKeys(map);
    }

    private void injectDeviceInfoIds(Map<String, String> map) {
        addString(map, "mac_sha1", this.deviceInfo.macSha1);
        addString(map, "mac_md5", this.deviceInfo.macShortMd5);
        addString(map, "android_id", this.deviceInfo.androidId);
    }

    private void injectConfig(Map<String, String> map) {
        addString(map, "app_token", this.adjustConfig.appToken);
        addString(map, "environment", this.adjustConfig.environment);
        addBoolean(map, "device_known", this.adjustConfig.deviceKnown);
        addBoolean(map, "needs_response_details", Boolean.valueOf(true));
        addString(map, "gps_adid", Util.getPlayAdId(this.adjustConfig.context));
        addBoolean(map, "tracking_enabled", Util.isPlayTrackingEnabled(this.adjustConfig.context));
        addBoolean(map, "event_buffering_enabled", Boolean.valueOf(this.adjustConfig.eventBufferingEnabled));
        addString(map, "push_token", this.activityStateCopy.pushToken);
        ContentResolver contentResolver = this.adjustConfig.context.getContentResolver();
        addString(map, "fire_adid", Util.getFireAdvertisingId(contentResolver));
        addBoolean(map, "fire_tracking_enabled", Util.getFireTrackingEnabled(contentResolver));
    }

    private void injectActivityState(Map<String, String> map) {
        addString(map, "android_uuid", this.activityStateCopy.uuid);
        addInt(map, "session_count", (long) this.activityStateCopy.sessionCount);
        addInt(map, "subsession_count", (long) this.activityStateCopy.subsessionCount);
        addDuration(map, "session_length", this.activityStateCopy.sessionLength);
        addDuration(map, "time_spent", this.activityStateCopy.timeSpent);
    }

    private void injectCommonParameters(Map<String, String> map) {
        addDate(map, "created_at", this.createdAt);
        addBoolean(map, "attribution_deeplink", Boolean.valueOf(true));
    }

    private void injectAttribution(Map<String, String> map) {
        if (this.attribution != null) {
            addString(map, "tracker", this.attribution.trackerName);
            addString(map, "campaign", this.attribution.campaign);
            addString(map, "adgroup", this.attribution.adgroup);
            addString(map, "creative", this.attribution.creative);
        }
    }

    private void checkDeviceIds(Map<String, String> map) {
        if (!map.containsKey("mac_sha1") && !map.containsKey("mac_md5") && !map.containsKey("android_id") && !map.containsKey("gps_adid")) {
            logger.error("Missing device id's. Please check if Proguard is correctly set with Adjust SDK", new Object[0]);
        }
    }

    private void fillPluginKeys(Map<String, String> map) {
        if (this.deviceInfo.pluginKeys != null) {
            for (Entry entry : this.deviceInfo.pluginKeys.entrySet()) {
                addString(map, (String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    private String getEventSuffix(AdjustEvent adjustEvent) {
        if (adjustEvent.revenue == null) {
            return String.format(Locale.US, "'%s'", new Object[]{adjustEvent.eventToken});
        }
        return String.format(Locale.US, "(%.5f %s, '%s')", new Object[]{adjustEvent.revenue, adjustEvent.currency, adjustEvent.eventToken});
    }

    public static void addString(Map<String, String> map, String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            map.put(str, str2);
        }
    }

    public static void addInt(Map<String, String> map, String str, long j) {
        if (j >= 0) {
            addString(map, str, Long.toString(j));
        }
    }

    public static void addDate(Map<String, String> map, String str, long j) {
        if (j >= 0) {
            addString(map, str, Util.dateFormatter.format(Long.valueOf(j)));
        }
    }

    public static void addDuration(Map<String, String> map, String str, long j) {
        if (j >= 0) {
            addInt(map, str, (j + 500) / 1000);
        }
    }

    public static void addMapJson(Map<String, String> map, String str, Map<String, String> map2) {
        if (map2 != null && map2.size() != 0) {
            addString(map, str, new JSONObject(map2).toString());
        }
    }

    public static void addBoolean(Map<String, String> map, String str, Boolean bool) {
        if (bool != null) {
            addInt(map, str, (long) bool.booleanValue());
        }
    }

    public static void addDouble(Map<String, String> map, String str, Double d) {
        if (d != null) {
            addString(map, str, String.format(Locale.US, "%.5f", new Object[]{d}));
        }
    }
}
