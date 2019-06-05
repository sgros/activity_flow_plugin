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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ActivityHandler implements IActivityHandler {
   private static final String ACTIVITY_STATE_NAME = "Activity state";
   private static final String ADJUST_PREFIX = "adjust_";
   private static final String ATTRIBUTION_NAME = "Attribution";
   private static long BACKGROUND_TIMER_INTERVAL;
   private static final String BACKGROUND_TIMER_NAME = "Background timer";
   private static final String DELAY_START_TIMER_NAME = "Delay Start timer";
   private static long FOREGROUND_TIMER_INTERVAL;
   private static final String FOREGROUND_TIMER_NAME = "Foreground timer";
   private static long FOREGROUND_TIMER_START;
   private static final String SESSION_CALLBACK_PARAMETERS_NAME = "Session Callback parameters";
   private static long SESSION_INTERVAL;
   private static final String SESSION_PARTNER_PARAMETERS_NAME = "Session Partner parameters";
   private static long SUBSESSION_INTERVAL;
   private static final String TIME_TRAVEL = "Time travel!";
   private ActivityState activityState;
   private AdjustConfig adjustConfig;
   private AdjustAttribution attribution;
   private IAttributionHandler attributionHandler;
   private TimerOnce backgroundTimer;
   private TimerOnce delayStartTimer;
   private DeviceInfo deviceInfo;
   private TimerCycle foregroundTimer;
   private ActivityHandler.InternalState internalState;
   private ILogger logger;
   private IPackageHandler packageHandler;
   private CustomScheduledExecutor scheduledExecutor;
   private ISdkClickHandler sdkClickHandler;
   private SessionParameters sessionParameters;

   private ActivityHandler(AdjustConfig var1) {
      this.init(var1);
      this.logger = AdjustFactory.getLogger();
      this.logger.lockLogLevel();
      this.scheduledExecutor = new CustomScheduledExecutor("ActivityHandler", false);
      this.internalState = new ActivityHandler.InternalState();
      this.internalState.enabled = true;
      this.internalState.offline = false;
      this.internalState.background = true;
      this.internalState.delayStart = false;
      this.internalState.updatePackages = false;
      this.internalState.sessionResponseProcessed = false;
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.initI();
         }
      });
   }

   private void backgroundTimerFiredI() {
      if (this.toSendI()) {
         this.packageHandler.sendFirstPackage();
      }

   }

   private boolean checkActivityStateI(ActivityState var1) {
      if (var1 == null) {
         this.logger.error("Missing activity state");
         return false;
      } else {
         return true;
      }
   }

   private void checkAttributionStateI() {
      if (this.checkActivityStateI(this.activityState)) {
         ILogger var1 = this.logger;
         StringBuilder var2 = new StringBuilder();
         var2.append("isFirstLaunch: ");
         var2.append(this.internalState.isFirstLaunch());
         var2.append(" isSessionResponseProcessed: ");
         var2.append(this.internalState.isSessionResponseProcessed());
         var1.verbose(var2.toString());
         if (!this.internalState.isFirstLaunch() || this.internalState.isSessionResponseProcessed()) {
            ILogger var5 = this.logger;
            StringBuilder var4 = new StringBuilder();
            var4.append("attribution != null: ");
            boolean var3;
            if (this.attribution != null) {
               var3 = true;
            } else {
               var3 = false;
            }

            var4.append(var3);
            var4.append(" askingAttribution: ");
            var4.append(this.activityState.askingAttribution);
            var5.verbose(var4.toString());
            if (this.attribution == null || this.activityState.askingAttribution) {
               this.attributionHandler.getAttribution();
            }
         }
      }
   }

   private boolean checkEventI(AdjustEvent var1) {
      if (var1 == null) {
         this.logger.error("Event missing");
         return false;
      } else if (!var1.isValid()) {
         this.logger.error("Event not initialized correctly");
         return false;
      } else {
         return true;
      }
   }

   private boolean checkOrderIdI(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         if (this.activityState.findOrderId(var1)) {
            this.logger.info("Skipping duplicated order ID '%s'", var1);
            return false;
         } else {
            this.activityState.addOrderId(var1);
            this.logger.verbose("Added order ID '%s'", var1);
            return true;
         }
      } else {
         return true;
      }
   }

   private Intent createDeeplinkIntentI(Uri var1) {
      Intent var2;
      if (this.adjustConfig.deepLinkComponent == null) {
         var2 = new Intent("android.intent.action.VIEW", var1);
      } else {
         var2 = new Intent("android.intent.action.VIEW", var1, this.adjustConfig.context, this.adjustConfig.deepLinkComponent);
      }

      var2.setFlags(268435456);
      var2.setPackage(this.adjustConfig.context.getPackageName());
      return var2;
   }

   private void delayStartI() {
      if (!this.internalState.isToStartNow()) {
         if (!this.isToUpdatePackagesI()) {
            double var1;
            if (this.adjustConfig.delayStart != null) {
               var1 = this.adjustConfig.delayStart;
            } else {
               var1 = 0.0D;
            }

            long var3 = AdjustFactory.getMaxDelayStart();
            long var5 = (long)(1000.0D * var1);
            String var9;
            if (var5 > var3) {
               double var7 = (double)(var3 / 1000L);
               var9 = Util.SecondsDisplayFormat.format(var1);
               String var10 = Util.SecondsDisplayFormat.format(var7);
               this.logger.warn("Delay start of %s seconds bigger than max allowed value of %s seconds", var9, var10);
               var1 = var7;
            } else {
               var3 = var5;
            }

            var9 = Util.SecondsDisplayFormat.format(var1);
            this.logger.info("Waiting %s seconds before starting first session", var9);
            this.delayStartTimer.startIn(var3);
            this.internalState.updatePackages = true;
            if (this.activityState != null) {
               this.activityState.updatePackages = true;
               this.writeActivityStateI();
            }

         }
      }
   }

   public static boolean deleteActivityState(Context var0) {
      return var0.deleteFile("AdjustIoActivityState");
   }

   public static boolean deleteAttribution(Context var0) {
      return var0.deleteFile("AdjustAttribution");
   }

   public static boolean deleteSessionCallbackParameters(Context var0) {
      return var0.deleteFile("AdjustSessionCallbackParameters");
   }

   public static boolean deleteSessionPartnerParameters(Context var0) {
      return var0.deleteFile("AdjustSessionPartnerParameters");
   }

   private void endI() {
      if (!this.toSendI()) {
         this.pauseSendingI();
      }

      if (this.updateActivityStateI(System.currentTimeMillis())) {
         this.writeActivityStateI();
      }

   }

   private void foregroundTimerFiredI() {
      if (!this.isEnabledI()) {
         this.stopForegroundTimerI();
      } else {
         if (this.toSendI()) {
            this.packageHandler.sendFirstPackage();
         }

         if (this.updateActivityStateI(System.currentTimeMillis())) {
            this.writeActivityStateI();
         }

      }
   }

   public static ActivityHandler getInstance(AdjustConfig var0) {
      if (var0 == null) {
         AdjustFactory.getLogger().error("AdjustConfig missing");
         return null;
      } else if (!var0.isValid()) {
         AdjustFactory.getLogger().error("AdjustConfig not initialized correctly");
         return null;
      } else {
         if (var0.processName != null) {
            int var1 = Process.myPid();
            ActivityManager var2 = (ActivityManager)var0.context.getSystemService("activity");
            if (var2 == null) {
               return null;
            }

            Iterator var3 = var2.getRunningAppProcesses().iterator();

            while(var3.hasNext()) {
               RunningAppProcessInfo var4 = (RunningAppProcessInfo)var3.next();
               if (var4.pid == var1) {
                  if (!var4.processName.equalsIgnoreCase(var0.processName)) {
                     AdjustFactory.getLogger().info("Skipping initialization in background process (%s)", var4.processName);
                     return null;
                  }
                  break;
               }
            }
         }

         return new ActivityHandler(var0);
      }
   }

   private boolean hasChangedState(boolean var1, boolean var2, String var3, String var4) {
      if (var1 != var2) {
         return true;
      } else {
         if (var1) {
            this.logger.debug(var3);
         } else {
            this.logger.debug(var4);
         }

         return false;
      }
   }

   private void initI() {
      SESSION_INTERVAL = AdjustFactory.getSessionInterval();
      SUBSESSION_INTERVAL = AdjustFactory.getSubsessionInterval();
      FOREGROUND_TIMER_INTERVAL = AdjustFactory.getTimerInterval();
      FOREGROUND_TIMER_START = AdjustFactory.getTimerStart();
      BACKGROUND_TIMER_INTERVAL = AdjustFactory.getTimerInterval();
      this.readAttributionI(this.adjustConfig.context);
      this.readActivityStateI(this.adjustConfig.context);
      this.sessionParameters = new SessionParameters();
      this.readSessionCallbackParametersI(this.adjustConfig.context);
      this.readSessionPartnerParametersI(this.adjustConfig.context);
      if (this.activityState != null) {
         this.internalState.enabled = this.activityState.enabled;
         this.internalState.updatePackages = this.activityState.updatePackages;
         this.internalState.firstLaunch = false;
      } else {
         this.internalState.firstLaunch = true;
      }

      this.readConfigFile(this.adjustConfig.context);
      this.deviceInfo = new DeviceInfo(this.adjustConfig.context, this.adjustConfig.sdkPrefix);
      if (this.adjustConfig.eventBufferingEnabled) {
         this.logger.info("Event buffering is enabled");
      }

      if (Util.getPlayAdId(this.adjustConfig.context) == null) {
         this.logger.warn("Unable to get Google Play Services Advertising ID at start time");
         if (this.deviceInfo.macSha1 == null && this.deviceInfo.macShortMd5 == null && this.deviceInfo.androidId == null) {
            this.logger.error("Unable to get any device id's. Please check if Proguard is correctly set with Adjust SDK");
         }
      } else {
         this.logger.info("Google Play Services Advertising ID read correctly at start time");
      }

      if (this.adjustConfig.defaultTracker != null) {
         this.logger.info("Default tracker: '%s'", this.adjustConfig.defaultTracker);
      }

      if (this.adjustConfig.pushToken != null) {
         this.logger.info("Push token: '%s'", this.adjustConfig.pushToken);
         if (this.activityState != null) {
            this.setPushToken(this.adjustConfig.pushToken);
         }
      }

      this.foregroundTimer = new TimerCycle(new Runnable() {
         public void run() {
            ActivityHandler.this.foregroundTimerFired();
         }
      }, FOREGROUND_TIMER_START, FOREGROUND_TIMER_INTERVAL, "Foreground timer");
      if (this.adjustConfig.sendInBackground) {
         this.logger.info("Send in background configured");
         this.backgroundTimer = new TimerOnce(new Runnable() {
            public void run() {
               ActivityHandler.this.backgroundTimerFired();
            }
         }, "Background timer");
      }

      if (this.activityState == null && this.adjustConfig.delayStart != null && this.adjustConfig.delayStart > 0.0D) {
         this.logger.info("Delay start configured");
         this.internalState.delayStart = true;
         this.delayStartTimer = new TimerOnce(new Runnable() {
            public void run() {
               ActivityHandler.this.sendFirstPackages();
            }
         }, "Delay Start timer");
      }

      UtilNetworking.setUserAgent(this.adjustConfig.userAgent);
      this.packageHandler = AdjustFactory.getPackageHandler(this, this.adjustConfig.context, this.toSendI(false));
      this.attributionHandler = AdjustFactory.getAttributionHandler(this, this.getAttributionPackageI(), this.toSendI(false));
      this.sdkClickHandler = AdjustFactory.getSdkClickHandler(this, this.toSendI(true));
      if (this.isToUpdatePackagesI()) {
         this.updatePackagesI();
      }

      if (this.adjustConfig.referrer != null) {
         this.sendReferrerI(this.adjustConfig.referrer, this.adjustConfig.referrerClickTime);
      }

      this.sessionParametersActionsI(this.adjustConfig.sessionParametersActionsArray);
   }

   private boolean isEnabledI() {
      return this.activityState != null ? this.activityState.enabled : this.internalState.isEnabled();
   }

   private boolean isToUpdatePackagesI() {
      return this.activityState != null ? this.activityState.updatePackages : this.internalState.isToUpdatePackages();
   }

   private void launchAttributionListenerI(Handler var1) {
      if (this.adjustConfig.onAttributionChangedListener != null) {
         var1.post(new Runnable() {
            public void run() {
               ActivityHandler.this.adjustConfig.onAttributionChangedListener.onAttributionChanged(ActivityHandler.this.attribution);
            }
         });
      }
   }

   private void launchAttributionResponseTasksI(AttributionResponseData var1) {
      this.updateAdidI(var1.adid);
      Handler var2 = new Handler(this.adjustConfig.context.getMainLooper());
      if (this.updateAttributionI(var1.attribution)) {
         this.launchAttributionListenerI(var2);
      }

      this.prepareDeeplinkI(var1.deeplink, var2);
   }

   private void launchDeeplinkMain(Intent var1, Uri var2) {
      boolean var3;
      if (this.adjustConfig.context.getPackageManager().queryIntentActivities(var1, 0).size() > 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (!var3) {
         this.logger.error("Unable to open deferred deep link (%s)", var2);
      } else {
         this.logger.info("Open deferred deep link (%s)", var2);
         this.adjustConfig.context.startActivity(var1);
      }
   }

   private void launchEventResponseTasksI(final EventResponseData var1) {
      this.updateAdidI(var1.adid);
      Handler var2 = new Handler(this.adjustConfig.context.getMainLooper());
      if (var1.success && this.adjustConfig.onEventTrackingSucceededListener != null) {
         this.logger.debug("Launching success event tracking listener");
         var2.post(new Runnable() {
            public void run() {
               ActivityHandler.this.adjustConfig.onEventTrackingSucceededListener.onFinishedEventTrackingSucceeded(var1.getSuccessResponseData());
            }
         });
      } else if (!var1.success && this.adjustConfig.onEventTrackingFailedListener != null) {
         this.logger.debug("Launching failed event tracking listener");
         var2.post(new Runnable() {
            public void run() {
               ActivityHandler.this.adjustConfig.onEventTrackingFailedListener.onFinishedEventTrackingFailed(var1.getFailureResponseData());
            }
         });
      }
   }

   private void launchSdkClickResponseTasksI(SdkClickResponseData var1) {
      this.updateAdidI(var1.adid);
      Handler var2 = new Handler(this.adjustConfig.context.getMainLooper());
      if (this.updateAttributionI(var1.attribution)) {
         this.launchAttributionListenerI(var2);
      }

   }

   private void launchSessionResponseListenerI(final SessionResponseData var1, Handler var2) {
      if (var1.success && this.adjustConfig.onSessionTrackingSucceededListener != null) {
         this.logger.debug("Launching success session tracking listener");
         var2.post(new Runnable() {
            public void run() {
               ActivityHandler.this.adjustConfig.onSessionTrackingSucceededListener.onFinishedSessionTrackingSucceeded(var1.getSuccessResponseData());
            }
         });
      } else if (!var1.success && this.adjustConfig.onSessionTrackingFailedListener != null) {
         this.logger.debug("Launching failed session tracking listener");
         var2.post(new Runnable() {
            public void run() {
               ActivityHandler.this.adjustConfig.onSessionTrackingFailedListener.onFinishedSessionTrackingFailed(var1.getFailureResponseData());
            }
         });
      }
   }

   private void launchSessionResponseTasksI(SessionResponseData var1) {
      ILogger var2 = this.logger;
      StringBuilder var3 = new StringBuilder();
      var3.append("launchSessionResponseTasksI ");
      var3.append(var1);
      var2.verbose(var3.toString());
      this.updateAdidI(var1.adid);
      Handler var4 = new Handler(this.adjustConfig.context.getMainLooper());
      if (this.updateAttributionI(var1.attribution)) {
         this.launchAttributionListenerI(var4);
      }

      this.launchSessionResponseListenerI(var1, var4);
      this.logger.verbose("sessionResponseProcessed = true");
      this.internalState.sessionResponseProcessed = true;
   }

   private void pauseSendingI() {
      this.attributionHandler.pauseSending();
      this.packageHandler.pauseSending();
      if (!this.toSendI(true)) {
         this.sdkClickHandler.pauseSending();
      } else {
         this.sdkClickHandler.resumeSending();
      }

   }

   private boolean pausedI() {
      return this.pausedI(false);
   }

   private boolean pausedI(boolean var1) {
      boolean var2 = false;
      boolean var3 = false;
      if (var1) {
         if (!this.internalState.isOffline()) {
            var1 = var3;
            if (this.isEnabledI()) {
               return var1;
            }
         }

         var1 = true;
         return var1;
      } else {
         if (!this.internalState.isOffline() && this.isEnabledI()) {
            var1 = var2;
            if (!this.internalState.isDelayStart()) {
               return var1;
            }
         }

         var1 = true;
         return var1;
      }
   }

   private void prepareDeeplinkI(final Uri var1, Handler var2) {
      if (var1 != null) {
         this.logger.info("Deferred deeplink received (%s)", var1);
         var2.post(new Runnable(this.createDeeplinkIntentI(var1)) {
            // $FF: synthetic field
            final Intent val$deeplinkIntent;

            {
               this.val$deeplinkIntent = var3;
            }

            public void run() {
               boolean var1x;
               if (ActivityHandler.this.adjustConfig.onDeeplinkResponseListener != null) {
                  var1x = ActivityHandler.this.adjustConfig.onDeeplinkResponseListener.launchReceivedDeeplink(var1);
               } else {
                  var1x = true;
               }

               if (var1x) {
                  ActivityHandler.this.launchDeeplinkMain(this.val$deeplinkIntent, var1);
               }

            }
         });
      }
   }

   private void processSessionI() {
      long var1 = System.currentTimeMillis();
      if (this.activityState == null) {
         this.activityState = new ActivityState();
         this.activityState.sessionCount = 1;
         this.activityState.pushToken = this.adjustConfig.pushToken;
         this.transferSessionPackageI(var1);
         this.activityState.resetSessionAttributes(var1);
         this.activityState.enabled = this.internalState.isEnabled();
         this.activityState.updatePackages = this.internalState.isToUpdatePackages();
         this.writeActivityStateI();
      } else {
         long var3 = var1 - this.activityState.lastActivity;
         if (var3 < 0L) {
            this.logger.error("Time travel!");
            this.activityState.lastActivity = var1;
            this.writeActivityStateI();
         } else {
            ActivityState var5;
            if (var3 > SESSION_INTERVAL) {
               var5 = this.activityState;
               ++var5.sessionCount;
               this.activityState.lastInterval = var3;
               this.transferSessionPackageI(var1);
               this.activityState.resetSessionAttributes(var1);
               this.writeActivityStateI();
            } else if (var3 > SUBSESSION_INTERVAL) {
               var5 = this.activityState;
               ++var5.subsessionCount;
               var5 = this.activityState;
               var5.sessionLength += var3;
               this.activityState.lastActivity = var1;
               this.logger.verbose("Started subsession %d of session %d", this.activityState.subsessionCount, this.activityState.sessionCount);
               this.writeActivityStateI();
            } else {
               this.logger.verbose("Time span since last activity too short for a new subsession");
            }
         }
      }
   }

   private PackageBuilder queryStringClickPackageBuilderI(List var1) {
      if (var1 == null) {
         return null;
      } else {
         LinkedHashMap var2 = new LinkedHashMap();
         AdjustAttribution var3 = new AdjustAttribution();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            ParameterValuePair var9 = (ParameterValuePair)var4.next();
            this.readQueryStringI(var9.mParameter, var9.mValue, var2, var3);
         }

         String var11 = (String)var2.remove("reftag");
         long var5 = System.currentTimeMillis();
         if (this.activityState != null) {
            long var7 = this.activityState.lastActivity;
            this.activityState.lastInterval = var5 - var7;
         }

         PackageBuilder var10 = new PackageBuilder(this.adjustConfig, this.deviceInfo, this.activityState, var5);
         var10.extraParameters = var2;
         var10.attribution = var3;
         var10.reftag = var11;
         return var10;
      }
   }

   private void readActivityStateI(Context var1) {
      try {
         this.activityState = (ActivityState)Util.readObject(var1, "AdjustIoActivityState", "Activity state", ActivityState.class);
      } catch (Exception var2) {
         this.logger.error("Failed to read %s file (%s)", "Activity state", var2.getMessage());
         this.activityState = null;
      }

   }

   private void readAttributionI(Context var1) {
      try {
         this.attribution = (AdjustAttribution)Util.readObject(var1, "AdjustAttribution", "Attribution", AdjustAttribution.class);
      } catch (Exception var2) {
         this.logger.error("Failed to read %s file (%s)", "Attribution", var2.getMessage());
         this.attribution = null;
      }

   }

   private void readConfigFile(Context var1) {
      Properties var4;
      try {
         InputStream var2 = var1.getAssets().open("adjust_config.properties");
         var4 = new Properties();
         var4.load(var2);
      } catch (Exception var3) {
         this.logger.debug("%s file not found in this app", var3.getMessage());
         return;
      }

      this.logger.verbose("adjust_config.properties file read and loaded");
      String var5 = var4.getProperty("defaultTracker");
      if (var5 != null) {
         this.adjustConfig.defaultTracker = var5;
      }

   }

   private void readOpenUrlI(Uri var1, long var2) {
      if (var1 != null) {
         String var4 = var1.toString();
         if (var4 != null && var4.length() != 0) {
            this.logger.verbose("Url to parse (%s)", var1);
            UrlQuerySanitizer var5 = new UrlQuerySanitizer();
            var5.setUnregisteredParameterValueSanitizer(UrlQuerySanitizer.getAllButNulLegal());
            var5.setAllowUnregisteredParamaters(true);
            var5.parseUrl(var4);
            PackageBuilder var7 = this.queryStringClickPackageBuilderI(var5.getParameterList());
            if (var7 != null) {
               var7.deeplink = var1.toString();
               var7.clickTime = var2;
               ActivityPackage var6 = var7.buildClickPackage("deeplink", this.sessionParameters);
               this.sdkClickHandler.sendSdkClick(var6);
            }
         }
      }
   }

   private boolean readQueryStringI(String var1, String var2, Map var3, AdjustAttribution var4) {
      if (var1 != null && var2 != null) {
         if (!var1.startsWith("adjust_")) {
            return false;
         } else {
            var1 = var1.substring("adjust_".length());
            if (var1.length() == 0) {
               return false;
            } else if (var2.length() == 0) {
               return false;
            } else {
               if (!this.trySetAttributionI(var4, var1, var2)) {
                  var3.put(var1, var2);
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   private void readSessionCallbackParametersI(Context var1) {
      try {
         this.sessionParameters.callbackParameters = (Map)Util.readObject(var1, "AdjustSessionCallbackParameters", "Session Callback parameters", Map.class);
      } catch (Exception var2) {
         this.logger.error("Failed to read %s file (%s)", "Session Callback parameters", var2.getMessage());
         this.sessionParameters.callbackParameters = null;
      }

   }

   private void readSessionPartnerParametersI(Context var1) {
      try {
         this.sessionParameters.partnerParameters = (Map)Util.readObject(var1, "AdjustSessionPartnerParameters", "Session Partner parameters", Map.class);
      } catch (Exception var2) {
         this.logger.error("Failed to read %s file (%s)", "Session Partner parameters", var2.getMessage());
         this.sessionParameters.partnerParameters = null;
      }

   }

   private void resumeSendingI() {
      this.attributionHandler.resumeSending();
      this.packageHandler.resumeSending();
      this.sdkClickHandler.resumeSending();
   }

   private void sendFirstPackagesI() {
      if (this.internalState.isToStartNow()) {
         this.logger.info("Start delay expired or never configured");
      } else {
         this.updatePackagesI();
         this.internalState.delayStart = false;
         this.delayStartTimer.cancel();
         this.delayStartTimer = null;
         this.updateHandlersStatusAndSendI();
      }
   }

   private void sendReferrerI(String var1, long var2) {
      if (var1 != null && var1.length() != 0) {
         this.logger.verbose("Referrer to parse (%s)", var1);
         UrlQuerySanitizer var4 = new UrlQuerySanitizer();
         var4.setUnregisteredParameterValueSanitizer(UrlQuerySanitizer.getAllButNulLegal());
         var4.setAllowUnregisteredParamaters(true);
         var4.parseQuery(var1);
         PackageBuilder var6 = this.queryStringClickPackageBuilderI(var4.getParameterList());
         if (var6 != null) {
            var6.referrer = var1;
            var6.clickTime = var2;
            ActivityPackage var5 = var6.buildClickPackage("reftag", this.sessionParameters);
            this.sdkClickHandler.sendSdkClick(var5);
         }
      }
   }

   private void sessionParametersActionsI(List var1) {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            ((IRunActivityHandler)var2.next()).run(this);
         }

      }
   }

   private void setPushTokenI(String var1) {
      if (var1 != null) {
         if (!var1.equals(this.activityState.pushToken)) {
            this.activityState.pushToken = var1;
            this.writeActivityStateI();
            long var2 = System.currentTimeMillis();
            ActivityPackage var4 = (new PackageBuilder(this.adjustConfig, this.deviceInfo, this.activityState, var2)).buildInfoPackage("push");
            this.packageHandler.addPackage(var4);
            this.packageHandler.sendFirstPackage();
         }
      }
   }

   private void startBackgroundTimerI() {
      if (this.backgroundTimer != null) {
         if (this.toSendI()) {
            if (this.backgroundTimer.getFireIn() <= 0L) {
               this.backgroundTimer.startIn(BACKGROUND_TIMER_INTERVAL);
            }
         }
      }
   }

   private void startForegroundTimerI() {
      if (this.isEnabledI()) {
         this.foregroundTimer.start();
      }
   }

   private void startI() {
      if (this.activityState == null || this.activityState.enabled) {
         this.updateHandlersStatusAndSendI();
         this.processSessionI();
         this.checkAttributionStateI();
      }
   }

   private void stopBackgroundTimerI() {
      if (this.backgroundTimer != null) {
         this.backgroundTimer.cancel();
      }
   }

   private void stopForegroundTimerI() {
      this.foregroundTimer.suspend();
   }

   private void teardownActivityStateS(boolean var1) {
      synchronized(ActivityState.class){}

      Throwable var10000;
      boolean var10001;
      label207: {
         try {
            if (this.activityState == null) {
               return;
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label207;
         }

         if (var1) {
            try {
               if (this.adjustConfig != null && this.adjustConfig.context != null) {
                  deleteActivityState(this.adjustConfig.context);
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label207;
            }
         }

         label196:
         try {
            this.activityState = null;
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label196;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   private void teardownAllSessionParametersS(boolean var1) {
      synchronized(SessionParameters.class){}

      Throwable var10000;
      boolean var10001;
      label207: {
         try {
            if (this.sessionParameters == null) {
               return;
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label207;
         }

         if (var1) {
            try {
               if (this.adjustConfig != null && this.adjustConfig.context != null) {
                  deleteSessionCallbackParameters(this.adjustConfig.context);
                  deleteSessionPartnerParameters(this.adjustConfig.context);
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label207;
            }
         }

         label196:
         try {
            this.sessionParameters = null;
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label196;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   private void teardownAttributionS(boolean var1) {
      synchronized(AdjustAttribution.class){}

      Throwable var10000;
      boolean var10001;
      label207: {
         try {
            if (this.attribution == null) {
               return;
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label207;
         }

         if (var1) {
            try {
               if (this.adjustConfig != null && this.adjustConfig.context != null) {
                  deleteAttribution(this.adjustConfig.context);
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label207;
            }
         }

         label196:
         try {
            this.attribution = null;
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label196;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   private boolean toSendI() {
      return this.toSendI(false);
   }

   private boolean toSendI(boolean var1) {
      if (this.pausedI(var1)) {
         return false;
      } else {
         return this.adjustConfig.sendInBackground ? true : this.internalState.isForeground();
      }
   }

   private void trackEventI(AdjustEvent var1) {
      if (this.checkActivityStateI(this.activityState)) {
         if (this.isEnabledI()) {
            if (this.checkEventI(var1)) {
               if (this.checkOrderIdI(var1.orderId)) {
                  long var2 = System.currentTimeMillis();
                  ActivityState var4 = this.activityState;
                  ++var4.eventCount;
                  this.updateActivityStateI(var2);
                  ActivityPackage var5 = (new PackageBuilder(this.adjustConfig, this.deviceInfo, this.activityState, var2)).buildEventPackage(var1, this.sessionParameters, this.internalState.isDelayStart());
                  this.packageHandler.addPackage(var5);
                  if (this.adjustConfig.eventBufferingEnabled) {
                     this.logger.info("Buffered event %s", var5.getSuffix());
                  } else {
                     this.packageHandler.sendFirstPackage();
                  }

                  if (this.adjustConfig.sendInBackground && this.internalState.isBackground()) {
                     this.startBackgroundTimerI();
                  }

                  this.writeActivityStateI();
               }
            }
         }
      }
   }

   private void transferSessionPackageI(long var1) {
      ActivityPackage var3 = (new PackageBuilder(this.adjustConfig, this.deviceInfo, this.activityState, var1)).buildSessionPackage(this.sessionParameters, this.internalState.isDelayStart());
      this.packageHandler.addPackage(var3);
      this.packageHandler.sendFirstPackage();
   }

   private boolean trySetAttributionI(AdjustAttribution var1, String var2, String var3) {
      if (var2.equals("tracker")) {
         var1.trackerName = var3;
         return true;
      } else if (var2.equals("campaign")) {
         var1.campaign = var3;
         return true;
      } else if (var2.equals("adgroup")) {
         var1.adgroup = var3;
         return true;
      } else if (var2.equals("creative")) {
         var1.creative = var3;
         return true;
      } else {
         return false;
      }
   }

   private boolean updateActivityStateI(long var1) {
      if (!this.checkActivityStateI(this.activityState)) {
         return false;
      } else {
         long var3 = var1 - this.activityState.lastActivity;
         if (var3 > SESSION_INTERVAL) {
            return false;
         } else {
            this.activityState.lastActivity = var1;
            if (var3 < 0L) {
               this.logger.error("Time travel!");
            } else {
               ActivityState var5 = this.activityState;
               var5.sessionLength += var3;
               var5 = this.activityState;
               var5.timeSpent += var3;
            }

            return true;
         }
      }
   }

   private void updateAdidI(String var1) {
      if (var1 != null) {
         if (!var1.equals(this.activityState.adid)) {
            this.activityState.adid = var1;
            this.writeActivityStateI();
         }
      }
   }

   private void updateHandlersStatusAndSend() {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.updateHandlersStatusAndSendI();
         }
      });
   }

   private void updateHandlersStatusAndSendI() {
      if (!this.toSendI()) {
         this.pauseSendingI();
      } else {
         this.resumeSendingI();
         if (!this.adjustConfig.eventBufferingEnabled) {
            this.packageHandler.sendFirstPackage();
         }

      }
   }

   private void updatePackagesI() {
      this.packageHandler.updatePackages(this.sessionParameters);
      this.internalState.updatePackages = false;
      if (this.activityState != null) {
         this.activityState.updatePackages = false;
         this.writeActivityStateI();
      }

   }

   private void updateStatus(boolean var1, String var2, String var3, String var4) {
      if (var1) {
         this.logger.info(var2);
      } else if (this.pausedI(false)) {
         if (this.pausedI(true)) {
            this.logger.info(var3);
         } else {
            ILogger var6 = this.logger;
            StringBuilder var5 = new StringBuilder();
            var5.append(var3);
            var5.append(", except the Sdk Click Handler");
            var6.info(var5.toString());
         }
      } else {
         this.logger.info(var4);
      }

      this.updateHandlersStatusAndSend();
   }

   private void writeActivityStateI() {
      this.writeActivityStateS((Runnable)null);
   }

   private void writeActivityStateS(Runnable var1) {
      synchronized(ActivityState.class){}

      Throwable var10000;
      boolean var10001;
      label187: {
         try {
            if (this.activityState == null) {
               return;
            }
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label187;
         }

         if (var1 != null) {
            try {
               var1.run();
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label187;
            }
         }

         label176:
         try {
            Util.writeObject(this.activityState, this.adjustConfig.context, "AdjustIoActivityState", "Activity state");
            return;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label176;
         }
      }

      while(true) {
         Throwable var22 = var10000;

         try {
            throw var22;
         } catch (Throwable var18) {
            var10000 = var18;
            var10001 = false;
            continue;
         }
      }
   }

   private void writeAttributionI() {
      synchronized(AdjustAttribution.class){}

      Throwable var10000;
      boolean var10001;
      label123: {
         try {
            if (this.attribution == null) {
               return;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            Util.writeObject(this.attribution, this.adjustConfig.context, "AdjustAttribution", "Attribution");
            return;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   private void writeSessionCallbackParametersI() {
      synchronized(SessionParameters.class){}

      Throwable var10000;
      boolean var10001;
      label123: {
         try {
            if (this.sessionParameters == null) {
               return;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            Util.writeObject(this.sessionParameters.callbackParameters, this.adjustConfig.context, "AdjustSessionCallbackParameters", "Session Callback parameters");
            return;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   private void writeSessionPartnerParametersI() {
      synchronized(SessionParameters.class){}

      Throwable var10000;
      boolean var10001;
      label123: {
         try {
            if (this.sessionParameters == null) {
               return;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            Util.writeObject(this.sessionParameters.partnerParameters, this.adjustConfig.context, "AdjustSessionPartnerParameters", "Session Partner parameters");
            return;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public void addSessionCallbackParameter(final String var1, final String var2) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.addSessionCallbackParameterI(var1, var2);
         }
      });
   }

   public void addSessionCallbackParameterI(String var1, String var2) {
      if (Util.isValidParameter(var1, "key", "Session Callback")) {
         if (Util.isValidParameter(var2, "value", "Session Callback")) {
            if (this.sessionParameters.callbackParameters == null) {
               this.sessionParameters.callbackParameters = new LinkedHashMap();
            }

            String var3 = (String)this.sessionParameters.callbackParameters.get(var1);
            if (var2.equals(var3)) {
               this.logger.verbose("Key %s already present with the same value", var1);
            } else {
               if (var3 != null) {
                  this.logger.warn("Key %s will be overwritten", var1);
               }

               this.sessionParameters.callbackParameters.put(var1, var2);
               this.writeSessionCallbackParametersI();
            }
         }
      }
   }

   public void addSessionPartnerParameter(final String var1, final String var2) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.addSessionPartnerParameterI(var1, var2);
         }
      });
   }

   public void addSessionPartnerParameterI(String var1, String var2) {
      if (Util.isValidParameter(var1, "key", "Session Partner")) {
         if (Util.isValidParameter(var2, "value", "Session Partner")) {
            if (this.sessionParameters.partnerParameters == null) {
               this.sessionParameters.partnerParameters = new LinkedHashMap();
            }

            String var3 = (String)this.sessionParameters.partnerParameters.get(var1);
            if (var2.equals(var3)) {
               this.logger.verbose("Key %s already present with the same value", var1);
            } else {
               if (var3 != null) {
                  this.logger.warn("Key %s will be overwritten", var1);
               }

               this.sessionParameters.partnerParameters.put(var1, var2);
               this.writeSessionPartnerParametersI();
            }
         }
      }
   }

   public void backgroundTimerFired() {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.backgroundTimerFiredI();
         }
      });
   }

   public void finishedTrackingActivity(ResponseData var1) {
      if (var1 instanceof SessionResponseData) {
         this.attributionHandler.checkSessionResponse((SessionResponseData)var1);
      } else if (var1 instanceof SdkClickResponseData) {
         this.attributionHandler.checkSdkClickResponse((SdkClickResponseData)var1);
      } else if (var1 instanceof EventResponseData) {
         this.launchEventResponseTasks((EventResponseData)var1);
      }
   }

   public void foregroundTimerFired() {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.foregroundTimerFiredI();
         }
      });
   }

   public String getAdid() {
      return this.activityState == null ? null : this.activityState.adid;
   }

   public AdjustAttribution getAttribution() {
      return this.attribution;
   }

   public ActivityPackage getAttributionPackageI() {
      long var1 = System.currentTimeMillis();
      return (new PackageBuilder(this.adjustConfig, this.deviceInfo, this.activityState, var1)).buildAttributionPackage();
   }

   public ActivityHandler.InternalState getInternalState() {
      return this.internalState;
   }

   public void init(AdjustConfig var1) {
      this.adjustConfig = var1;
   }

   public boolean isEnabled() {
      return this.isEnabledI();
   }

   public void launchAttributionResponseTasks(final AttributionResponseData var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.launchAttributionResponseTasksI(var1);
         }
      });
   }

   public void launchEventResponseTasks(final EventResponseData var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.launchEventResponseTasksI(var1);
         }
      });
   }

   public void launchSdkClickResponseTasks(final SdkClickResponseData var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.launchSdkClickResponseTasksI(var1);
         }
      });
   }

   public void launchSessionResponseTasks(final SessionResponseData var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.launchSessionResponseTasksI(var1);
         }
      });
   }

   public void onPause() {
      this.internalState.background = true;
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.stopForegroundTimerI();
            ActivityHandler.this.startBackgroundTimerI();
            ActivityHandler.this.logger.verbose("Subsession end");
            ActivityHandler.this.endI();
         }
      });
   }

   public void onResume() {
      this.internalState.background = false;
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.delayStartI();
            ActivityHandler.this.stopBackgroundTimerI();
            ActivityHandler.this.startForegroundTimerI();
            ActivityHandler.this.logger.verbose("Subsession start");
            ActivityHandler.this.startI();
         }
      });
   }

   public void readOpenUrl(final Uri var1, final long var2) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.readOpenUrlI(var1, var2);
         }
      });
   }

   public void removeSessionCallbackParameter(final String var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.removeSessionCallbackParameterI(var1);
         }
      });
   }

   public void removeSessionCallbackParameterI(String var1) {
      if (Util.isValidParameter(var1, "key", "Session Callback")) {
         if (this.sessionParameters.callbackParameters == null) {
            this.logger.warn("Session Callback parameters are not set");
         } else if ((String)this.sessionParameters.callbackParameters.remove(var1) == null) {
            this.logger.warn("Key %s does not exist", var1);
         } else {
            this.logger.debug("Key %s will be removed", var1);
            this.writeSessionCallbackParametersI();
         }
      }
   }

   public void removeSessionPartnerParameter(final String var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.removeSessionPartnerParameterI(var1);
         }
      });
   }

   public void removeSessionPartnerParameterI(String var1) {
      if (Util.isValidParameter(var1, "key", "Session Partner")) {
         if (this.sessionParameters.partnerParameters == null) {
            this.logger.warn("Session Partner parameters are not set");
         } else if ((String)this.sessionParameters.partnerParameters.remove(var1) == null) {
            this.logger.warn("Key %s does not exist", var1);
         } else {
            this.logger.debug("Key %s will be removed", var1);
            this.writeSessionPartnerParametersI();
         }
      }
   }

   public void resetSessionCallbackParameters() {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.resetSessionCallbackParametersI();
         }
      });
   }

   public void resetSessionCallbackParametersI() {
      if (this.sessionParameters.callbackParameters == null) {
         this.logger.warn("Session Callback parameters are not set");
      }

      this.sessionParameters.callbackParameters = null;
      this.writeSessionCallbackParametersI();
   }

   public void resetSessionPartnerParameters() {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.resetSessionPartnerParametersI();
         }
      });
   }

   public void resetSessionPartnerParametersI() {
      if (this.sessionParameters.partnerParameters == null) {
         this.logger.warn("Session Partner parameters are not set");
      }

      this.sessionParameters.partnerParameters = null;
      this.writeSessionPartnerParametersI();
   }

   public void sendFirstPackages() {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.sendFirstPackagesI();
         }
      });
   }

   public void sendReferrer(final String var1, final long var2) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            ActivityHandler.this.sendReferrerI(var1, var2);
         }
      });
   }

   public void setAskingAttribution(final boolean var1) {
      this.writeActivityStateS(new Runnable() {
         public void run() {
            ActivityHandler.this.activityState.askingAttribution = var1;
         }
      });
   }

   public void setEnabled(final boolean var1) {
      if (this.hasChangedState(this.isEnabled(), var1, "Adjust already enabled", "Adjust already disabled")) {
         this.internalState.enabled = var1;
         if (this.activityState == null) {
            this.updateStatus(var1 ^ true, "Handlers will start as paused due to the SDK being disabled", "Handlers will still start as paused", "Handlers will start as active due to the SDK being enabled");
         } else {
            this.writeActivityStateS(new Runnable() {
               public void run() {
                  ActivityHandler.this.activityState.enabled = var1;
               }
            });
            this.updateStatus(var1 ^ true, "Pausing handlers due to SDK being disabled", "Handlers remain paused", "Resuming handlers due to SDK being enabled");
         }
      }
   }

   public void setOfflineMode(boolean var1) {
      if (this.hasChangedState(this.internalState.isOffline(), var1, "Adjust already in offline mode", "Adjust already in online mode")) {
         this.internalState.offline = var1;
         if (this.activityState == null) {
            this.updateStatus(var1, "Handlers will start paused due to SDK being offline", "Handlers will still start as paused", "Handlers will start as active due to SDK being online");
         } else {
            this.updateStatus(var1, "Pausing handlers to put SDK offline mode", "Handlers remain paused", "Resuming handlers to put SDK in online mode");
         }
      }
   }

   public void setPushToken(final String var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            if (ActivityHandler.this.activityState == null) {
               ActivityHandler.this.startI();
            }

            ActivityHandler.this.setPushTokenI(var1);
         }
      });
   }

   public void teardown(boolean var1) {
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
         } catch (SecurityException var3) {
         }
      }

      if (this.packageHandler != null) {
         this.packageHandler.teardown(var1);
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

      this.teardownActivityStateS(var1);
      this.teardownAttributionS(var1);
      this.teardownAllSessionParametersS(var1);
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

   public void trackEvent(final AdjustEvent var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            if (ActivityHandler.this.activityState == null) {
               ActivityHandler.this.logger.warn("Event tracked before first activity resumed.\nIf it was triggered in the Application class, it might timestamp or even send an install long before the user opens the app.\nPlease check https://github.com/adjust/android_sdk#can-i-trigger-an-event-at-application-launch for more information.");
               ActivityHandler.this.startI();
            }

            ActivityHandler.this.trackEventI(var1);
         }
      });
   }

   public boolean updateAttributionI(AdjustAttribution var1) {
      if (var1 == null) {
         return false;
      } else if (var1.equals(this.attribution)) {
         return false;
      } else {
         this.attribution = var1;
         this.writeAttributionI();
         return true;
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

      public boolean isBackground() {
         return this.background;
      }

      public boolean isDelayStart() {
         return this.delayStart;
      }

      public boolean isDisabled() {
         return this.enabled ^ true;
      }

      public boolean isEnabled() {
         return this.enabled;
      }

      public boolean isFirstLaunch() {
         return this.firstLaunch;
      }

      public boolean isForeground() {
         return this.background ^ true;
      }

      public boolean isOffline() {
         return this.offline;
      }

      public boolean isOnline() {
         return this.offline ^ true;
      }

      public boolean isSessionResponseProcessed() {
         return this.sessionResponseProcessed;
      }

      public boolean isToStartNow() {
         return this.delayStart ^ true;
      }

      public boolean isToUpdatePackages() {
         return this.updatePackages;
      }
   }
}
