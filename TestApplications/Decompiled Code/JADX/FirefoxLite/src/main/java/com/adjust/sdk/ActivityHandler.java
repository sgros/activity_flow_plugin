package com.adjust.sdk;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.net.UrlQuerySanitizer.ParameterValuePair;
import android.os.Handler;
import android.os.Process;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ActivityHandler implements IActivityHandler {
    private static final String ACTIVITY_STATE_NAME = "Activity state";
    private static final String ADJUST_PREFIX = "adjust_";
    private static final String ATTRIBUTION_NAME = "Attribution";
    private static long BACKGROUND_TIMER_INTERVAL = 0;
    private static final String BACKGROUND_TIMER_NAME = "Background timer";
    private static final String DELAY_START_TIMER_NAME = "Delay Start timer";
    private static long FOREGROUND_TIMER_INTERVAL = 0;
    private static final String FOREGROUND_TIMER_NAME = "Foreground timer";
    private static long FOREGROUND_TIMER_START = 0;
    private static final String SESSION_CALLBACK_PARAMETERS_NAME = "Session Callback parameters";
    private static long SESSION_INTERVAL = 0;
    private static final String SESSION_PARTNER_PARAMETERS_NAME = "Session Partner parameters";
    private static long SUBSESSION_INTERVAL = 0;
    private static final String TIME_TRAVEL = "Time travel!";
    private ActivityState activityState;
    private AdjustConfig adjustConfig;
    private AdjustAttribution attribution;
    private IAttributionHandler attributionHandler;
    private TimerOnce backgroundTimer;
    private TimerOnce delayStartTimer;
    private DeviceInfo deviceInfo;
    private TimerCycle foregroundTimer;
    private InternalState internalState;
    private ILogger logger = AdjustFactory.getLogger();
    private IPackageHandler packageHandler;
    private CustomScheduledExecutor scheduledExecutor;
    private ISdkClickHandler sdkClickHandler;
    private SessionParameters sessionParameters;

    /* renamed from: com.adjust.sdk.ActivityHandler$13 */
    class C029313 implements Runnable {
        C029313() {
        }

        public void run() {
            ActivityHandler.this.sendFirstPackagesI();
        }
    }

    /* renamed from: com.adjust.sdk.ActivityHandler$18 */
    class C029818 implements Runnable {
        C029818() {
        }

        public void run() {
            ActivityHandler.this.resetSessionCallbackParametersI();
        }
    }

    /* renamed from: com.adjust.sdk.ActivityHandler$19 */
    class C029919 implements Runnable {
        C029919() {
        }

        public void run() {
            ActivityHandler.this.resetSessionPartnerParametersI();
        }
    }

    /* renamed from: com.adjust.sdk.ActivityHandler$1 */
    class C03001 implements Runnable {
        C03001() {
        }

        public void run() {
            ActivityHandler.this.initI();
        }
    }

    /* renamed from: com.adjust.sdk.ActivityHandler$21 */
    class C030221 implements Runnable {
        C030221() {
        }

        public void run() {
            ActivityHandler.this.foregroundTimerFiredI();
        }
    }

    /* renamed from: com.adjust.sdk.ActivityHandler$22 */
    class C030322 implements Runnable {
        C030322() {
        }

        public void run() {
            ActivityHandler.this.backgroundTimerFiredI();
        }
    }

    /* renamed from: com.adjust.sdk.ActivityHandler$23 */
    class C030423 implements Runnable {
        C030423() {
        }

        public void run() {
            ActivityHandler.this.updateHandlersStatusAndSendI();
        }
    }

    /* renamed from: com.adjust.sdk.ActivityHandler$24 */
    class C030524 implements Runnable {
        C030524() {
        }

        public void run() {
            ActivityHandler.this.foregroundTimerFired();
        }
    }

    /* renamed from: com.adjust.sdk.ActivityHandler$25 */
    class C030625 implements Runnable {
        C030625() {
        }

        public void run() {
            ActivityHandler.this.backgroundTimerFired();
        }
    }

    /* renamed from: com.adjust.sdk.ActivityHandler$26 */
    class C030726 implements Runnable {
        C030726() {
        }

        public void run() {
            ActivityHandler.this.sendFirstPackages();
        }
    }

    /* renamed from: com.adjust.sdk.ActivityHandler$2 */
    class C03112 implements Runnable {
        C03112() {
        }

        public void run() {
            ActivityHandler.this.delayStartI();
            ActivityHandler.this.stopBackgroundTimerI();
            ActivityHandler.this.startForegroundTimerI();
            ActivityHandler.this.logger.verbose("Subsession start", new Object[0]);
            ActivityHandler.this.startI();
        }
    }

    /* renamed from: com.adjust.sdk.ActivityHandler$31 */
    class C031331 implements Runnable {
        C031331() {
        }

        public void run() {
            ActivityHandler.this.adjustConfig.onAttributionChangedListener.onAttributionChanged(ActivityHandler.this.attribution);
        }
    }

    /* renamed from: com.adjust.sdk.ActivityHandler$3 */
    class C03153 implements Runnable {
        C03153() {
        }

        public void run() {
            ActivityHandler.this.stopForegroundTimerI();
            ActivityHandler.this.startBackgroundTimerI();
            ActivityHandler.this.logger.verbose("Subsession end", new Object[0]);
            ActivityHandler.this.endI();
        }
    }

    public class InternalState {
        boolean background;
        boolean delayStart;
        boolean enabled;
        boolean firstLaunch;
        boolean offline;
        boolean sessionResponseProcessed;
        boolean updatePackages;

        public boolean isEnabled() {
            return this.enabled;
        }

        public boolean isDisabled() {
            return this.enabled ^ 1;
        }

        public boolean isOffline() {
            return this.offline;
        }

        public boolean isOnline() {
            return this.offline ^ 1;
        }

        public boolean isBackground() {
            return this.background;
        }

        public boolean isForeground() {
            return this.background ^ 1;
        }

        public boolean isDelayStart() {
            return this.delayStart;
        }

        public boolean isToStartNow() {
            return this.delayStart ^ 1;
        }

        public boolean isToUpdatePackages() {
            return this.updatePackages;
        }

        public boolean isFirstLaunch() {
            return this.firstLaunch;
        }

        public boolean isSessionResponseProcessed() {
            return this.sessionResponseProcessed;
        }
    }

    public void teardown(boolean z) {
        if (this.backgroundTimer != null) {
            this.backgroundTimer.teardown();
        }
        if (this.foregroundTimer != null) {
            this.foregroundTimer.teardown();
        }
        if (this.delayStartTimer != null) {
            this.delayStartTimer.teardown();
        }
        if (this.scheduledExecutor != null) {
            try {
                this.scheduledExecutor.shutdownNow();
            } catch (SecurityException unused) {
            }
        }
        if (this.packageHandler != null) {
            this.packageHandler.teardown(z);
        }
        if (this.attributionHandler != null) {
            this.attributionHandler.teardown();
        }
        if (this.sdkClickHandler != null) {
            this.sdkClickHandler.teardown();
        }
        if (this.sessionParameters != null) {
            if (this.sessionParameters.callbackParameters != null) {
                this.sessionParameters.callbackParameters.clear();
            }
            if (this.sessionParameters.partnerParameters != null) {
                this.sessionParameters.partnerParameters.clear();
            }
        }
        teardownActivityStateS(z);
        teardownAttributionS(z);
        teardownAllSessionParametersS(z);
        this.packageHandler = null;
        this.logger = null;
        this.foregroundTimer = null;
        this.scheduledExecutor = null;
        this.backgroundTimer = null;
        this.delayStartTimer = null;
        this.internalState = null;
        this.deviceInfo = null;
        this.adjustConfig = null;
        this.attributionHandler = null;
        this.sdkClickHandler = null;
        this.sessionParameters = null;
    }

    private ActivityHandler(AdjustConfig adjustConfig) {
        init(adjustConfig);
        this.logger.lockLogLevel();
        this.scheduledExecutor = new CustomScheduledExecutor("ActivityHandler", false);
        this.internalState = new InternalState();
        this.internalState.enabled = true;
        this.internalState.offline = false;
        this.internalState.background = true;
        this.internalState.delayStart = false;
        this.internalState.updatePackages = false;
        this.internalState.sessionResponseProcessed = false;
        this.scheduledExecutor.submit(new C03001());
    }

    public void init(AdjustConfig adjustConfig) {
        this.adjustConfig = adjustConfig;
    }

    public static ActivityHandler getInstance(AdjustConfig adjustConfig) {
        if (adjustConfig == null) {
            AdjustFactory.getLogger().error("AdjustConfig missing", new Object[0]);
            return null;
        } else if (adjustConfig.isValid()) {
            if (adjustConfig.processName != null) {
                int myPid = Process.myPid();
                ActivityManager activityManager = (ActivityManager) adjustConfig.context.getSystemService("activity");
                if (activityManager == null) {
                    return null;
                }
                for (RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
                    if (runningAppProcessInfo.pid == myPid) {
                        if (!runningAppProcessInfo.processName.equalsIgnoreCase(adjustConfig.processName)) {
                            AdjustFactory.getLogger().info("Skipping initialization in background process (%s)", runningAppProcessInfo.processName);
                            return null;
                        }
                    }
                }
            }
            return new ActivityHandler(adjustConfig);
        } else {
            AdjustFactory.getLogger().error("AdjustConfig not initialized correctly", new Object[0]);
            return null;
        }
    }

    public void onResume() {
        this.internalState.background = false;
        this.scheduledExecutor.submit(new C03112());
    }

    public void onPause() {
        this.internalState.background = true;
        this.scheduledExecutor.submit(new C03153());
    }

    public void trackEvent(final AdjustEvent adjustEvent) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                if (ActivityHandler.this.activityState == null) {
                    ActivityHandler.this.logger.warn("Event tracked before first activity resumed.\nIf it was triggered in the Application class, it might timestamp or even send an install long before the user opens the app.\nPlease check https://github.com/adjust/android_sdk#can-i-trigger-an-event-at-application-launch for more information.", new Object[0]);
                    ActivityHandler.this.startI();
                }
                ActivityHandler.this.trackEventI(adjustEvent);
            }
        });
    }

    public void finishedTrackingActivity(ResponseData responseData) {
        if (responseData instanceof SessionResponseData) {
            this.attributionHandler.checkSessionResponse((SessionResponseData) responseData);
        } else if (responseData instanceof SdkClickResponseData) {
            this.attributionHandler.checkSdkClickResponse((SdkClickResponseData) responseData);
        } else if (responseData instanceof EventResponseData) {
            launchEventResponseTasks((EventResponseData) responseData);
        }
    }

    public void setEnabled(final boolean z) {
        if (hasChangedState(isEnabled(), z, "Adjust already enabled", "Adjust already disabled")) {
            this.internalState.enabled = z;
            if (this.activityState == null) {
                updateStatus(z ^ 1, "Handlers will start as paused due to the SDK being disabled", "Handlers will still start as paused", "Handlers will start as active due to the SDK being enabled");
                return;
            }
            writeActivityStateS(new Runnable() {
                public void run() {
                    ActivityHandler.this.activityState.enabled = z;
                }
            });
            updateStatus(z ^ 1, "Pausing handlers due to SDK being disabled", "Handlers remain paused", "Resuming handlers due to SDK being enabled");
        }
    }

    private void updateStatus(boolean z, String str, String str2, String str3) {
        if (z) {
            this.logger.info(str, new Object[0]);
        } else if (!pausedI(false)) {
            this.logger.info(str3, new Object[0]);
        } else if (pausedI(true)) {
            this.logger.info(str2, new Object[0]);
        } else {
            ILogger iLogger = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(", except the Sdk Click Handler");
            iLogger.info(stringBuilder.toString(), new Object[0]);
        }
        updateHandlersStatusAndSend();
    }

    private boolean hasChangedState(boolean z, boolean z2, String str, String str2) {
        if (z != z2) {
            return true;
        }
        if (z) {
            this.logger.debug(str, new Object[0]);
        } else {
            this.logger.debug(str2, new Object[0]);
        }
        return false;
    }

    public void setOfflineMode(boolean z) {
        if (hasChangedState(this.internalState.isOffline(), z, "Adjust already in offline mode", "Adjust already in online mode")) {
            this.internalState.offline = z;
            if (this.activityState == null) {
                updateStatus(z, "Handlers will start paused due to SDK being offline", "Handlers will still start as paused", "Handlers will start as active due to SDK being online");
            } else {
                updateStatus(z, "Pausing handlers to put SDK offline mode", "Handlers remain paused", "Resuming handlers to put SDK in online mode");
            }
        }
    }

    public boolean isEnabled() {
        return isEnabledI();
    }

    private boolean isEnabledI() {
        if (this.activityState != null) {
            return this.activityState.enabled;
        }
        return this.internalState.isEnabled();
    }

    public void readOpenUrl(final Uri uri, final long j) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                ActivityHandler.this.readOpenUrlI(uri, j);
            }
        });
    }

    private void updateAdidI(String str) {
        if (str != null && !str.equals(this.activityState.adid)) {
            this.activityState.adid = str;
            writeActivityStateI();
        }
    }

    public boolean updateAttributionI(AdjustAttribution adjustAttribution) {
        if (adjustAttribution == null || adjustAttribution.equals(this.attribution)) {
            return false;
        }
        this.attribution = adjustAttribution;
        writeAttributionI();
        return true;
    }

    public void setAskingAttribution(final boolean z) {
        writeActivityStateS(new Runnable() {
            public void run() {
                ActivityHandler.this.activityState.askingAttribution = z;
            }
        });
    }

    public void sendReferrer(final String str, final long j) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                ActivityHandler.this.sendReferrerI(str, j);
            }
        });
    }

    public void launchEventResponseTasks(final EventResponseData eventResponseData) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                ActivityHandler.this.launchEventResponseTasksI(eventResponseData);
            }
        });
    }

    public void launchSdkClickResponseTasks(final SdkClickResponseData sdkClickResponseData) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                ActivityHandler.this.launchSdkClickResponseTasksI(sdkClickResponseData);
            }
        });
    }

    public void launchSessionResponseTasks(final SessionResponseData sessionResponseData) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                ActivityHandler.this.launchSessionResponseTasksI(sessionResponseData);
            }
        });
    }

    public void launchAttributionResponseTasks(final AttributionResponseData attributionResponseData) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                ActivityHandler.this.launchAttributionResponseTasksI(attributionResponseData);
            }
        });
    }

    public void sendFirstPackages() {
        this.scheduledExecutor.submit(new C029313());
    }

    public void addSessionCallbackParameter(final String str, final String str2) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                ActivityHandler.this.addSessionCallbackParameterI(str, str2);
            }
        });
    }

    public void addSessionPartnerParameter(final String str, final String str2) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                ActivityHandler.this.addSessionPartnerParameterI(str, str2);
            }
        });
    }

    public void removeSessionCallbackParameter(final String str) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                ActivityHandler.this.removeSessionCallbackParameterI(str);
            }
        });
    }

    public void removeSessionPartnerParameter(final String str) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                ActivityHandler.this.removeSessionPartnerParameterI(str);
            }
        });
    }

    public void resetSessionCallbackParameters() {
        this.scheduledExecutor.submit(new C029818());
    }

    public void resetSessionPartnerParameters() {
        this.scheduledExecutor.submit(new C029919());
    }

    public void setPushToken(final String str) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                if (ActivityHandler.this.activityState == null) {
                    ActivityHandler.this.startI();
                }
                ActivityHandler.this.setPushTokenI(str);
            }
        });
    }

    public void foregroundTimerFired() {
        this.scheduledExecutor.submit(new C030221());
    }

    public void backgroundTimerFired() {
        this.scheduledExecutor.submit(new C030322());
    }

    public String getAdid() {
        if (this.activityState == null) {
            return null;
        }
        return this.activityState.adid;
    }

    public AdjustAttribution getAttribution() {
        return this.attribution;
    }

    public ActivityPackage getAttributionPackageI() {
        return new PackageBuilder(this.adjustConfig, this.deviceInfo, this.activityState, System.currentTimeMillis()).buildAttributionPackage();
    }

    public InternalState getInternalState() {
        return this.internalState;
    }

    private void updateHandlersStatusAndSend() {
        this.scheduledExecutor.submit(new C030423());
    }

    private void initI() {
        SESSION_INTERVAL = AdjustFactory.getSessionInterval();
        SUBSESSION_INTERVAL = AdjustFactory.getSubsessionInterval();
        FOREGROUND_TIMER_INTERVAL = AdjustFactory.getTimerInterval();
        FOREGROUND_TIMER_START = AdjustFactory.getTimerStart();
        BACKGROUND_TIMER_INTERVAL = AdjustFactory.getTimerInterval();
        readAttributionI(this.adjustConfig.context);
        readActivityStateI(this.adjustConfig.context);
        this.sessionParameters = new SessionParameters();
        readSessionCallbackParametersI(this.adjustConfig.context);
        readSessionPartnerParametersI(this.adjustConfig.context);
        if (this.activityState != null) {
            this.internalState.enabled = this.activityState.enabled;
            this.internalState.updatePackages = this.activityState.updatePackages;
            this.internalState.firstLaunch = false;
        } else {
            this.internalState.firstLaunch = true;
        }
        readConfigFile(this.adjustConfig.context);
        this.deviceInfo = new DeviceInfo(this.adjustConfig.context, this.adjustConfig.sdkPrefix);
        if (this.adjustConfig.eventBufferingEnabled) {
            this.logger.info("Event buffering is enabled", new Object[0]);
        }
        if (Util.getPlayAdId(this.adjustConfig.context) == null) {
            this.logger.warn("Unable to get Google Play Services Advertising ID at start time", new Object[0]);
            if (this.deviceInfo.macSha1 == null && this.deviceInfo.macShortMd5 == null && this.deviceInfo.androidId == null) {
                this.logger.error("Unable to get any device id's. Please check if Proguard is correctly set with Adjust SDK", new Object[0]);
            }
        } else {
            this.logger.info("Google Play Services Advertising ID read correctly at start time", new Object[0]);
        }
        if (this.adjustConfig.defaultTracker != null) {
            this.logger.info("Default tracker: '%s'", this.adjustConfig.defaultTracker);
        }
        if (this.adjustConfig.pushToken != null) {
            this.logger.info("Push token: '%s'", this.adjustConfig.pushToken);
            if (this.activityState != null) {
                setPushToken(this.adjustConfig.pushToken);
            }
        }
        this.foregroundTimer = new TimerCycle(new C030524(), FOREGROUND_TIMER_START, FOREGROUND_TIMER_INTERVAL, FOREGROUND_TIMER_NAME);
        if (this.adjustConfig.sendInBackground) {
            this.logger.info("Send in background configured", new Object[0]);
            this.backgroundTimer = new TimerOnce(new C030625(), BACKGROUND_TIMER_NAME);
        }
        if (this.activityState == null && this.adjustConfig.delayStart != null && this.adjustConfig.delayStart.doubleValue() > 0.0d) {
            this.logger.info("Delay start configured", new Object[0]);
            this.internalState.delayStart = true;
            this.delayStartTimer = new TimerOnce(new C030726(), DELAY_START_TIMER_NAME);
        }
        UtilNetworking.setUserAgent(this.adjustConfig.userAgent);
        this.packageHandler = AdjustFactory.getPackageHandler(this, this.adjustConfig.context, toSendI(false));
        this.attributionHandler = AdjustFactory.getAttributionHandler(this, getAttributionPackageI(), toSendI(false));
        this.sdkClickHandler = AdjustFactory.getSdkClickHandler(this, toSendI(true));
        if (isToUpdatePackagesI()) {
            updatePackagesI();
        }
        if (this.adjustConfig.referrer != null) {
            sendReferrerI(this.adjustConfig.referrer, this.adjustConfig.referrerClickTime);
        }
        sessionParametersActionsI(this.adjustConfig.sessionParametersActionsArray);
    }

    private void readConfigFile(Context context) {
        try {
            InputStream open = context.getAssets().open("adjust_config.properties");
            Properties properties = new Properties();
            properties.load(open);
            this.logger.verbose("adjust_config.properties file read and loaded", new Object[0]);
            String property = properties.getProperty("defaultTracker");
            if (property != null) {
                this.adjustConfig.defaultTracker = property;
            }
        } catch (Exception e) {
            this.logger.debug("%s file not found in this app", e.getMessage());
        }
    }

    private void sessionParametersActionsI(List<IRunActivityHandler> list) {
        if (list != null) {
            for (IRunActivityHandler run : list) {
                run.run(this);
            }
        }
    }

    private void startI() {
        if (this.activityState == null || this.activityState.enabled) {
            updateHandlersStatusAndSendI();
            processSessionI();
            checkAttributionStateI();
        }
    }

    private void processSessionI() {
        long currentTimeMillis = System.currentTimeMillis();
        if (this.activityState == null) {
            this.activityState = new ActivityState();
            this.activityState.sessionCount = 1;
            this.activityState.pushToken = this.adjustConfig.pushToken;
            transferSessionPackageI(currentTimeMillis);
            this.activityState.resetSessionAttributes(currentTimeMillis);
            this.activityState.enabled = this.internalState.isEnabled();
            this.activityState.updatePackages = this.internalState.isToUpdatePackages();
            writeActivityStateI();
            return;
        }
        long j = currentTimeMillis - this.activityState.lastActivity;
        if (j < 0) {
            this.logger.error(TIME_TRAVEL, new Object[0]);
            this.activityState.lastActivity = currentTimeMillis;
            writeActivityStateI();
        } else if (j > SESSION_INTERVAL) {
            ActivityState activityState = this.activityState;
            activityState.sessionCount++;
            this.activityState.lastInterval = j;
            transferSessionPackageI(currentTimeMillis);
            this.activityState.resetSessionAttributes(currentTimeMillis);
            writeActivityStateI();
        } else if (j > SUBSESSION_INTERVAL) {
            ActivityState activityState2 = this.activityState;
            activityState2.subsessionCount++;
            activityState2 = this.activityState;
            activityState2.sessionLength += j;
            this.activityState.lastActivity = currentTimeMillis;
            this.logger.verbose("Started subsession %d of session %d", Integer.valueOf(this.activityState.subsessionCount), Integer.valueOf(this.activityState.sessionCount));
            writeActivityStateI();
        } else {
            this.logger.verbose("Time span since last activity too short for a new subsession", new Object[0]);
        }
    }

    private void checkAttributionStateI() {
        if (checkActivityStateI(this.activityState)) {
            ILogger iLogger = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("isFirstLaunch: ");
            stringBuilder.append(this.internalState.isFirstLaunch());
            stringBuilder.append(" isSessionResponseProcessed: ");
            stringBuilder.append(this.internalState.isSessionResponseProcessed());
            iLogger.verbose(stringBuilder.toString(), new Object[0]);
            if (!this.internalState.isFirstLaunch() || this.internalState.isSessionResponseProcessed()) {
                iLogger = this.logger;
                stringBuilder = new StringBuilder();
                stringBuilder.append("attribution != null: ");
                stringBuilder.append(this.attribution != null);
                stringBuilder.append(" askingAttribution: ");
                stringBuilder.append(this.activityState.askingAttribution);
                iLogger.verbose(stringBuilder.toString(), new Object[0]);
                if (this.attribution == null || this.activityState.askingAttribution) {
                    this.attributionHandler.getAttribution();
                }
            }
        }
    }

    private void endI() {
        if (!toSendI()) {
            pauseSendingI();
        }
        if (updateActivityStateI(System.currentTimeMillis())) {
            writeActivityStateI();
        }
    }

    private void trackEventI(AdjustEvent adjustEvent) {
        if (checkActivityStateI(this.activityState) && isEnabledI() && checkEventI(adjustEvent) && checkOrderIdI(adjustEvent.orderId)) {
            long currentTimeMillis = System.currentTimeMillis();
            ActivityState activityState = this.activityState;
            activityState.eventCount++;
            updateActivityStateI(currentTimeMillis);
            this.packageHandler.addPackage(new PackageBuilder(this.adjustConfig, this.deviceInfo, this.activityState, currentTimeMillis).buildEventPackage(adjustEvent, this.sessionParameters, this.internalState.isDelayStart()));
            if (this.adjustConfig.eventBufferingEnabled) {
                this.logger.info("Buffered event %s", r9.getSuffix());
            } else {
                this.packageHandler.sendFirstPackage();
            }
            if (this.adjustConfig.sendInBackground && this.internalState.isBackground()) {
                startBackgroundTimerI();
            }
            writeActivityStateI();
        }
    }

    private void launchEventResponseTasksI(final EventResponseData eventResponseData) {
        updateAdidI(eventResponseData.adid);
        Handler handler = new Handler(this.adjustConfig.context.getMainLooper());
        if (eventResponseData.success && this.adjustConfig.onEventTrackingSucceededListener != null) {
            this.logger.debug("Launching success event tracking listener", new Object[0]);
            handler.post(new Runnable() {
                public void run() {
                    ActivityHandler.this.adjustConfig.onEventTrackingSucceededListener.onFinishedEventTrackingSucceeded(eventResponseData.getSuccessResponseData());
                }
            });
        } else if (!eventResponseData.success && this.adjustConfig.onEventTrackingFailedListener != null) {
            this.logger.debug("Launching failed event tracking listener", new Object[0]);
            handler.post(new Runnable() {
                public void run() {
                    ActivityHandler.this.adjustConfig.onEventTrackingFailedListener.onFinishedEventTrackingFailed(eventResponseData.getFailureResponseData());
                }
            });
        }
    }

    private void launchSdkClickResponseTasksI(SdkClickResponseData sdkClickResponseData) {
        updateAdidI(sdkClickResponseData.adid);
        Handler handler = new Handler(this.adjustConfig.context.getMainLooper());
        if (updateAttributionI(sdkClickResponseData.attribution)) {
            launchAttributionListenerI(handler);
        }
    }

    private void launchSessionResponseTasksI(SessionResponseData sessionResponseData) {
        ILogger iLogger = this.logger;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("launchSessionResponseTasksI ");
        stringBuilder.append(sessionResponseData);
        iLogger.verbose(stringBuilder.toString(), new Object[0]);
        updateAdidI(sessionResponseData.adid);
        Handler handler = new Handler(this.adjustConfig.context.getMainLooper());
        if (updateAttributionI(sessionResponseData.attribution)) {
            launchAttributionListenerI(handler);
        }
        launchSessionResponseListenerI(sessionResponseData, handler);
        this.logger.verbose("sessionResponseProcessed = true", new Object[0]);
        this.internalState.sessionResponseProcessed = true;
    }

    private void launchSessionResponseListenerI(final SessionResponseData sessionResponseData, Handler handler) {
        if (sessionResponseData.success && this.adjustConfig.onSessionTrackingSucceededListener != null) {
            this.logger.debug("Launching success session tracking listener", new Object[0]);
            handler.post(new Runnable() {
                public void run() {
                    ActivityHandler.this.adjustConfig.onSessionTrackingSucceededListener.onFinishedSessionTrackingSucceeded(sessionResponseData.getSuccessResponseData());
                }
            });
        } else if (!sessionResponseData.success && this.adjustConfig.onSessionTrackingFailedListener != null) {
            this.logger.debug("Launching failed session tracking listener", new Object[0]);
            handler.post(new Runnable() {
                public void run() {
                    ActivityHandler.this.adjustConfig.onSessionTrackingFailedListener.onFinishedSessionTrackingFailed(sessionResponseData.getFailureResponseData());
                }
            });
        }
    }

    private void launchAttributionResponseTasksI(AttributionResponseData attributionResponseData) {
        updateAdidI(attributionResponseData.adid);
        Handler handler = new Handler(this.adjustConfig.context.getMainLooper());
        if (updateAttributionI(attributionResponseData.attribution)) {
            launchAttributionListenerI(handler);
        }
        prepareDeeplinkI(attributionResponseData.deeplink, handler);
    }

    private void launchAttributionListenerI(Handler handler) {
        if (this.adjustConfig.onAttributionChangedListener != null) {
            handler.post(new C031331());
        }
    }

    private void prepareDeeplinkI(final Uri uri, Handler handler) {
        if (uri != null) {
            this.logger.info("Deferred deeplink received (%s)", uri);
            final Intent createDeeplinkIntentI = createDeeplinkIntentI(uri);
            handler.post(new Runnable() {
                public void run() {
                    if (ActivityHandler.this.adjustConfig.onDeeplinkResponseListener != null ? ActivityHandler.this.adjustConfig.onDeeplinkResponseListener.launchReceivedDeeplink(uri) : true) {
                        ActivityHandler.this.launchDeeplinkMain(createDeeplinkIntentI, uri);
                    }
                }
            });
        }
    }

    private Intent createDeeplinkIntentI(Uri uri) {
        Intent intent;
        if (this.adjustConfig.deepLinkComponent == null) {
            intent = new Intent("android.intent.action.VIEW", uri);
        } else {
            intent = new Intent("android.intent.action.VIEW", uri, this.adjustConfig.context, this.adjustConfig.deepLinkComponent);
        }
        intent.setFlags(268435456);
        intent.setPackage(this.adjustConfig.context.getPackageName());
        return intent;
    }

    private void launchDeeplinkMain(Intent intent, Uri uri) {
        if ((this.adjustConfig.context.getPackageManager().queryIntentActivities(intent, 0).size() > 0 ? 1 : null) == null) {
            this.logger.error("Unable to open deferred deep link (%s)", uri);
            return;
        }
        this.logger.info("Open deferred deep link (%s)", uri);
        this.adjustConfig.context.startActivity(intent);
    }

    private void sendReferrerI(String str, long j) {
        if (str != null && str.length() != 0) {
            this.logger.verbose("Referrer to parse (%s)", str);
            UrlQuerySanitizer urlQuerySanitizer = new UrlQuerySanitizer();
            urlQuerySanitizer.setUnregisteredParameterValueSanitizer(UrlQuerySanitizer.getAllButNulLegal());
            urlQuerySanitizer.setAllowUnregisteredParamaters(true);
            urlQuerySanitizer.parseQuery(str);
            PackageBuilder queryStringClickPackageBuilderI = queryStringClickPackageBuilderI(urlQuerySanitizer.getParameterList());
            if (queryStringClickPackageBuilderI != null) {
                queryStringClickPackageBuilderI.referrer = str;
                queryStringClickPackageBuilderI.clickTime = j;
                this.sdkClickHandler.sendSdkClick(queryStringClickPackageBuilderI.buildClickPackage(Constants.REFTAG, this.sessionParameters));
            }
        }
    }

    private void readOpenUrlI(Uri uri, long j) {
        if (uri != null) {
            String uri2 = uri.toString();
            if (uri2 != null && uri2.length() != 0) {
                this.logger.verbose("Url to parse (%s)", uri);
                UrlQuerySanitizer urlQuerySanitizer = new UrlQuerySanitizer();
                urlQuerySanitizer.setUnregisteredParameterValueSanitizer(UrlQuerySanitizer.getAllButNulLegal());
                urlQuerySanitizer.setAllowUnregisteredParamaters(true);
                urlQuerySanitizer.parseUrl(uri2);
                PackageBuilder queryStringClickPackageBuilderI = queryStringClickPackageBuilderI(urlQuerySanitizer.getParameterList());
                if (queryStringClickPackageBuilderI != null) {
                    queryStringClickPackageBuilderI.deeplink = uri.toString();
                    queryStringClickPackageBuilderI.clickTime = j;
                    this.sdkClickHandler.sendSdkClick(queryStringClickPackageBuilderI.buildClickPackage(Constants.DEEPLINK, this.sessionParameters));
                }
            }
        }
    }

    private PackageBuilder queryStringClickPackageBuilderI(List<ParameterValuePair> list) {
        if (list == null) {
            return null;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        AdjustAttribution adjustAttribution = new AdjustAttribution();
        for (ParameterValuePair parameterValuePair : list) {
            readQueryStringI(parameterValuePair.mParameter, parameterValuePair.mValue, linkedHashMap, adjustAttribution);
        }
        String str = (String) linkedHashMap.remove(Constants.REFTAG);
        long currentTimeMillis = System.currentTimeMillis();
        if (this.activityState != null) {
            this.activityState.lastInterval = currentTimeMillis - this.activityState.lastActivity;
        }
        PackageBuilder packageBuilder = new PackageBuilder(this.adjustConfig, this.deviceInfo, this.activityState, currentTimeMillis);
        packageBuilder.extraParameters = linkedHashMap;
        packageBuilder.attribution = adjustAttribution;
        packageBuilder.reftag = str;
        return packageBuilder;
    }

    /* JADX WARNING: Missing block: B:17:0x0032, code skipped:
            return false;
     */
    private boolean readQueryStringI(java.lang.String r3, java.lang.String r4, java.util.Map<java.lang.String, java.lang.String> r5, com.adjust.sdk.AdjustAttribution r6) {
        /*
        r2 = this;
        r0 = 0;
        if (r3 == 0) goto L_0x0032;
    L_0x0003:
        if (r4 != 0) goto L_0x0006;
    L_0x0005:
        goto L_0x0032;
    L_0x0006:
        r1 = "adjust_";
        r1 = r3.startsWith(r1);
        if (r1 != 0) goto L_0x000f;
    L_0x000e:
        return r0;
    L_0x000f:
        r1 = "adjust_";
        r1 = r1.length();
        r3 = r3.substring(r1);
        r1 = r3.length();
        if (r1 != 0) goto L_0x0020;
    L_0x001f:
        return r0;
    L_0x0020:
        r1 = r4.length();
        if (r1 != 0) goto L_0x0027;
    L_0x0026:
        return r0;
    L_0x0027:
        r6 = r2.trySetAttributionI(r6, r3, r4);
        if (r6 != 0) goto L_0x0030;
    L_0x002d:
        r5.put(r3, r4);
    L_0x0030:
        r3 = 1;
        return r3;
    L_0x0032:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adjust.sdk.ActivityHandler.readQueryStringI(java.lang.String, java.lang.String, java.util.Map, com.adjust.sdk.AdjustAttribution):boolean");
    }

    private boolean trySetAttributionI(AdjustAttribution adjustAttribution, String str, String str2) {
        if (str.equals("tracker")) {
            adjustAttribution.trackerName = str2;
            return true;
        } else if (str.equals("campaign")) {
            adjustAttribution.campaign = str2;
            return true;
        } else if (str.equals("adgroup")) {
            adjustAttribution.adgroup = str2;
            return true;
        } else if (!str.equals("creative")) {
            return false;
        } else {
            adjustAttribution.creative = str2;
            return true;
        }
    }

    private void updateHandlersStatusAndSendI() {
        if (toSendI()) {
            resumeSendingI();
            if (!this.adjustConfig.eventBufferingEnabled) {
                this.packageHandler.sendFirstPackage();
            }
            return;
        }
        pauseSendingI();
    }

    private void pauseSendingI() {
        this.attributionHandler.pauseSending();
        this.packageHandler.pauseSending();
        if (toSendI(true)) {
            this.sdkClickHandler.resumeSending();
        } else {
            this.sdkClickHandler.pauseSending();
        }
    }

    private void resumeSendingI() {
        this.attributionHandler.resumeSending();
        this.packageHandler.resumeSending();
        this.sdkClickHandler.resumeSending();
    }

    private boolean updateActivityStateI(long j) {
        if (!checkActivityStateI(this.activityState)) {
            return false;
        }
        long j2 = j - this.activityState.lastActivity;
        if (j2 > SESSION_INTERVAL) {
            return false;
        }
        this.activityState.lastActivity = j;
        if (j2 < 0) {
            this.logger.error(TIME_TRAVEL, new Object[0]);
        } else {
            ActivityState activityState = this.activityState;
            activityState.sessionLength += j2;
            activityState = this.activityState;
            activityState.timeSpent += j2;
        }
        return true;
    }

    public static boolean deleteActivityState(Context context) {
        return context.deleteFile(Constants.ACTIVITY_STATE_FILENAME);
    }

    public static boolean deleteAttribution(Context context) {
        return context.deleteFile(Constants.ATTRIBUTION_FILENAME);
    }

    public static boolean deleteSessionCallbackParameters(Context context) {
        return context.deleteFile(Constants.SESSION_CALLBACK_PARAMETERS_FILENAME);
    }

    public static boolean deleteSessionPartnerParameters(Context context) {
        return context.deleteFile(Constants.SESSION_PARTNER_PARAMETERS_FILENAME);
    }

    private void transferSessionPackageI(long j) {
        this.packageHandler.addPackage(new PackageBuilder(this.adjustConfig, this.deviceInfo, this.activityState, j).buildSessionPackage(this.sessionParameters, this.internalState.isDelayStart()));
        this.packageHandler.sendFirstPackage();
    }

    private void startForegroundTimerI() {
        if (isEnabledI()) {
            this.foregroundTimer.start();
        }
    }

    private void stopForegroundTimerI() {
        this.foregroundTimer.suspend();
    }

    private void foregroundTimerFiredI() {
        if (isEnabledI()) {
            if (toSendI()) {
                this.packageHandler.sendFirstPackage();
            }
            if (updateActivityStateI(System.currentTimeMillis())) {
                writeActivityStateI();
            }
            return;
        }
        stopForegroundTimerI();
    }

    private void startBackgroundTimerI() {
        if (this.backgroundTimer != null && toSendI() && this.backgroundTimer.getFireIn() <= 0) {
            this.backgroundTimer.startIn(BACKGROUND_TIMER_INTERVAL);
        }
    }

    private void stopBackgroundTimerI() {
        if (this.backgroundTimer != null) {
            this.backgroundTimer.cancel();
        }
    }

    private void backgroundTimerFiredI() {
        if (toSendI()) {
            this.packageHandler.sendFirstPackage();
        }
    }

    private void delayStartI() {
        if (!this.internalState.isToStartNow() && !isToUpdatePackagesI()) {
            String format;
            double doubleValue = this.adjustConfig.delayStart != null ? this.adjustConfig.delayStart.doubleValue() : 0.0d;
            long maxDelayStart = AdjustFactory.getMaxDelayStart();
            long j = (long) (1000.0d * doubleValue);
            if (j > maxDelayStart) {
                double d = (double) (maxDelayStart / 1000);
                format = Util.SecondsDisplayFormat.format(doubleValue);
                String format2 = Util.SecondsDisplayFormat.format(d);
                this.logger.warn("Delay start of %s seconds bigger than max allowed value of %s seconds", format, format2);
                doubleValue = d;
            } else {
                maxDelayStart = j;
            }
            format = Util.SecondsDisplayFormat.format(doubleValue);
            this.logger.info("Waiting %s seconds before starting first session", format);
            this.delayStartTimer.startIn(maxDelayStart);
            this.internalState.updatePackages = true;
            if (this.activityState != null) {
                this.activityState.updatePackages = true;
                writeActivityStateI();
            }
        }
    }

    private void sendFirstPackagesI() {
        if (this.internalState.isToStartNow()) {
            this.logger.info("Start delay expired or never configured", new Object[0]);
            return;
        }
        updatePackagesI();
        this.internalState.delayStart = false;
        this.delayStartTimer.cancel();
        this.delayStartTimer = null;
        updateHandlersStatusAndSendI();
    }

    private void updatePackagesI() {
        this.packageHandler.updatePackages(this.sessionParameters);
        this.internalState.updatePackages = false;
        if (this.activityState != null) {
            this.activityState.updatePackages = false;
            writeActivityStateI();
        }
    }

    private boolean isToUpdatePackagesI() {
        if (this.activityState != null) {
            return this.activityState.updatePackages;
        }
        return this.internalState.isToUpdatePackages();
    }

    public void addSessionCallbackParameterI(String str, String str2) {
        if (Util.isValidParameter(str, "key", "Session Callback") && Util.isValidParameter(str2, "value", "Session Callback")) {
            if (this.sessionParameters.callbackParameters == null) {
                this.sessionParameters.callbackParameters = new LinkedHashMap();
            }
            String str3 = (String) this.sessionParameters.callbackParameters.get(str);
            if (str2.equals(str3)) {
                this.logger.verbose("Key %s already present with the same value", str);
                return;
            }
            if (str3 != null) {
                this.logger.warn("Key %s will be overwritten", str);
            }
            this.sessionParameters.callbackParameters.put(str, str2);
            writeSessionCallbackParametersI();
        }
    }

    public void addSessionPartnerParameterI(String str, String str2) {
        if (Util.isValidParameter(str, "key", "Session Partner") && Util.isValidParameter(str2, "value", "Session Partner")) {
            if (this.sessionParameters.partnerParameters == null) {
                this.sessionParameters.partnerParameters = new LinkedHashMap();
            }
            String str3 = (String) this.sessionParameters.partnerParameters.get(str);
            if (str2.equals(str3)) {
                this.logger.verbose("Key %s already present with the same value", str);
                return;
            }
            if (str3 != null) {
                this.logger.warn("Key %s will be overwritten", str);
            }
            this.sessionParameters.partnerParameters.put(str, str2);
            writeSessionPartnerParametersI();
        }
    }

    public void removeSessionCallbackParameterI(String str) {
        if (!Util.isValidParameter(str, "key", "Session Callback")) {
            return;
        }
        if (this.sessionParameters.callbackParameters == null) {
            this.logger.warn("Session Callback parameters are not set", new Object[0]);
        } else if (((String) this.sessionParameters.callbackParameters.remove(str)) == null) {
            this.logger.warn("Key %s does not exist", str);
        } else {
            this.logger.debug("Key %s will be removed", str);
            writeSessionCallbackParametersI();
        }
    }

    public void removeSessionPartnerParameterI(String str) {
        if (!Util.isValidParameter(str, "key", "Session Partner")) {
            return;
        }
        if (this.sessionParameters.partnerParameters == null) {
            this.logger.warn("Session Partner parameters are not set", new Object[0]);
        } else if (((String) this.sessionParameters.partnerParameters.remove(str)) == null) {
            this.logger.warn("Key %s does not exist", str);
        } else {
            this.logger.debug("Key %s will be removed", str);
            writeSessionPartnerParametersI();
        }
    }

    public void resetSessionCallbackParametersI() {
        if (this.sessionParameters.callbackParameters == null) {
            this.logger.warn("Session Callback parameters are not set", new Object[0]);
        }
        this.sessionParameters.callbackParameters = null;
        writeSessionCallbackParametersI();
    }

    public void resetSessionPartnerParametersI() {
        if (this.sessionParameters.partnerParameters == null) {
            this.logger.warn("Session Partner parameters are not set", new Object[0]);
        }
        this.sessionParameters.partnerParameters = null;
        writeSessionPartnerParametersI();
    }

    private void setPushTokenI(String str) {
        if (str != null && !str.equals(this.activityState.pushToken)) {
            this.activityState.pushToken = str;
            writeActivityStateI();
            this.packageHandler.addPackage(new PackageBuilder(this.adjustConfig, this.deviceInfo, this.activityState, System.currentTimeMillis()).buildInfoPackage(Constants.PUSH));
            this.packageHandler.sendFirstPackage();
        }
    }

    private void readActivityStateI(Context context) {
        try {
            this.activityState = (ActivityState) Util.readObject(context, Constants.ACTIVITY_STATE_FILENAME, ACTIVITY_STATE_NAME, ActivityState.class);
        } catch (Exception e) {
            this.logger.error("Failed to read %s file (%s)", ACTIVITY_STATE_NAME, e.getMessage());
            this.activityState = null;
        }
    }

    private void readAttributionI(Context context) {
        try {
            this.attribution = (AdjustAttribution) Util.readObject(context, Constants.ATTRIBUTION_FILENAME, ATTRIBUTION_NAME, AdjustAttribution.class);
        } catch (Exception e) {
            this.logger.error("Failed to read %s file (%s)", ATTRIBUTION_NAME, e.getMessage());
            this.attribution = null;
        }
    }

    private void readSessionCallbackParametersI(Context context) {
        try {
            this.sessionParameters.callbackParameters = (Map) Util.readObject(context, Constants.SESSION_CALLBACK_PARAMETERS_FILENAME, SESSION_CALLBACK_PARAMETERS_NAME, Map.class);
        } catch (Exception e) {
            this.logger.error("Failed to read %s file (%s)", SESSION_CALLBACK_PARAMETERS_NAME, e.getMessage());
            this.sessionParameters.callbackParameters = null;
        }
    }

    private void readSessionPartnerParametersI(Context context) {
        try {
            this.sessionParameters.partnerParameters = (Map) Util.readObject(context, Constants.SESSION_PARTNER_PARAMETERS_FILENAME, SESSION_PARTNER_PARAMETERS_NAME, Map.class);
        } catch (Exception e) {
            this.logger.error("Failed to read %s file (%s)", SESSION_PARTNER_PARAMETERS_NAME, e.getMessage());
            this.sessionParameters.partnerParameters = null;
        }
    }

    private void writeActivityStateI() {
        writeActivityStateS(null);
    }

    private void writeActivityStateS(Runnable runnable) {
        synchronized (ActivityState.class) {
            if (this.activityState == null) {
                return;
            }
            if (runnable != null) {
                runnable.run();
            }
            Util.writeObject(this.activityState, this.adjustConfig.context, Constants.ACTIVITY_STATE_FILENAME, ACTIVITY_STATE_NAME);
        }
    }

    private void teardownActivityStateS(boolean z) {
        synchronized (ActivityState.class) {
            if (this.activityState == null) {
                return;
            }
            if (!(!z || this.adjustConfig == null || this.adjustConfig.context == null)) {
                deleteActivityState(this.adjustConfig.context);
            }
            this.activityState = null;
        }
    }

    private void writeAttributionI() {
        synchronized (AdjustAttribution.class) {
            if (this.attribution == null) {
                return;
            }
            Util.writeObject(this.attribution, this.adjustConfig.context, Constants.ATTRIBUTION_FILENAME, ATTRIBUTION_NAME);
        }
    }

    private void teardownAttributionS(boolean z) {
        synchronized (AdjustAttribution.class) {
            if (this.attribution == null) {
                return;
            }
            if (!(!z || this.adjustConfig == null || this.adjustConfig.context == null)) {
                deleteAttribution(this.adjustConfig.context);
            }
            this.attribution = null;
        }
    }

    private void writeSessionCallbackParametersI() {
        synchronized (SessionParameters.class) {
            if (this.sessionParameters == null) {
                return;
            }
            Util.writeObject(this.sessionParameters.callbackParameters, this.adjustConfig.context, Constants.SESSION_CALLBACK_PARAMETERS_FILENAME, SESSION_CALLBACK_PARAMETERS_NAME);
        }
    }

    private void writeSessionPartnerParametersI() {
        synchronized (SessionParameters.class) {
            if (this.sessionParameters == null) {
                return;
            }
            Util.writeObject(this.sessionParameters.partnerParameters, this.adjustConfig.context, Constants.SESSION_PARTNER_PARAMETERS_FILENAME, SESSION_PARTNER_PARAMETERS_NAME);
        }
    }

    private void teardownAllSessionParametersS(boolean z) {
        synchronized (SessionParameters.class) {
            if (this.sessionParameters == null) {
                return;
            }
            if (!(!z || this.adjustConfig == null || this.adjustConfig.context == null)) {
                deleteSessionCallbackParameters(this.adjustConfig.context);
                deleteSessionPartnerParameters(this.adjustConfig.context);
            }
            this.sessionParameters = null;
        }
    }

    private boolean checkEventI(AdjustEvent adjustEvent) {
        if (adjustEvent == null) {
            this.logger.error("Event missing", new Object[0]);
            return false;
        } else if (adjustEvent.isValid()) {
            return true;
        } else {
            this.logger.error("Event not initialized correctly", new Object[0]);
            return false;
        }
    }

    private boolean checkOrderIdI(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        if (this.activityState.findOrderId(str)) {
            this.logger.info("Skipping duplicated order ID '%s'", str);
            return false;
        }
        this.activityState.addOrderId(str);
        this.logger.verbose("Added order ID '%s'", str);
        return true;
    }

    private boolean checkActivityStateI(ActivityState activityState) {
        if (activityState != null) {
            return true;
        }
        this.logger.error("Missing activity state", new Object[0]);
        return false;
    }

    private boolean pausedI() {
        return pausedI(false);
    }

    private boolean pausedI(boolean z) {
        boolean z2 = false;
        if (z) {
            if (this.internalState.isOffline() || !isEnabledI()) {
                z2 = true;
            }
            return z2;
        }
        if (this.internalState.isOffline() || !isEnabledI() || this.internalState.isDelayStart()) {
            z2 = true;
        }
        return z2;
    }

    private boolean toSendI() {
        return toSendI(false);
    }

    private boolean toSendI(boolean z) {
        if (pausedI(z)) {
            return false;
        }
        if (this.adjustConfig.sendInBackground) {
            return true;
        }
        return this.internalState.isForeground();
    }
}
