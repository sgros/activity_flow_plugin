package com.adjust.sdk;

import android.content.Context;
import java.util.List;

public class AdjustConfig {
   public static final String ENVIRONMENT_PRODUCTION = "production";
   public static final String ENVIRONMENT_SANDBOX = "sandbox";
   String appToken;
   Context context;
   Class deepLinkComponent;
   String defaultTracker;
   Double delayStart;
   Boolean deviceKnown;
   String environment;
   boolean eventBufferingEnabled;
   ILogger logger;
   OnAttributionChangedListener onAttributionChangedListener;
   OnDeeplinkResponseListener onDeeplinkResponseListener;
   OnEventTrackingFailedListener onEventTrackingFailedListener;
   OnEventTrackingSucceededListener onEventTrackingSucceededListener;
   OnSessionTrackingFailedListener onSessionTrackingFailedListener;
   OnSessionTrackingSucceededListener onSessionTrackingSucceededListener;
   String processName;
   String pushToken;
   String referrer;
   long referrerClickTime;
   String sdkPrefix;
   boolean sendInBackground;
   List sessionParametersActionsArray;
   String userAgent;

   public AdjustConfig(Context var1, String var2, String var3) {
      this.init(var1, var2, var3, false);
   }

   public AdjustConfig(Context var1, String var2, String var3, boolean var4) {
      this.init(var1, var2, var3, var4);
   }

   private boolean checkAppToken(String var1) {
      if (var1 == null) {
         this.logger.error("Missing App Token");
         return false;
      } else if (var1.length() != 12) {
         this.logger.error("Malformed App Token '%s'", var1);
         return false;
      } else {
         return true;
      }
   }

   private boolean checkContext(Context var1) {
      if (var1 == null) {
         this.logger.error("Missing context");
         return false;
      } else if (!Util.checkPermission(var1, "android.permission.INTERNET")) {
         this.logger.error("Missing permission: INTERNET");
         return false;
      } else {
         return true;
      }
   }

   private boolean checkEnvironment(String var1) {
      if (var1 == null) {
         this.logger.error("Missing environment");
         return false;
      } else if (var1.equals("sandbox")) {
         this.logger.warnInProduction("SANDBOX: Adjust is running in Sandbox mode. Use this setting for testing. Don't forget to set the environment to `production` before publishing!");
         return true;
      } else if (var1.equals("production")) {
         this.logger.warnInProduction("PRODUCTION: Adjust is running in Production mode. Use this setting only for the build that you want to publish. Set the environment to `sandbox` if you want to test your app!");
         return true;
      } else {
         this.logger.error("Unknown environment '%s'", var1);
         return false;
      }
   }

   private void init(Context var1, String var2, String var3, boolean var4) {
      this.logger = AdjustFactory.getLogger();
      if (var4 && "production".equals(var3)) {
         this.setLogLevel(LogLevel.SUPRESS, var3);
      } else {
         this.setLogLevel(LogLevel.INFO, var3);
      }

      if (this.isValid(var1, var2, var3)) {
         this.context = var1.getApplicationContext();
         this.appToken = var2;
         this.environment = var3;
         this.eventBufferingEnabled = false;
         this.sendInBackground = false;
      }
   }

   private boolean isValid(Context var1, String var2, String var3) {
      if (!this.checkAppToken(var2)) {
         return false;
      } else if (!this.checkEnvironment(var3)) {
         return false;
      } else {
         return this.checkContext(var1);
      }
   }

   private void setLogLevel(LogLevel var1, String var2) {
      this.logger.setLogLevel(var1, "production".equals(var2));
   }

   public boolean isValid() {
      boolean var1;
      if (this.appToken != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void setDeepLinkComponent(Class var1) {
      this.deepLinkComponent = var1;
   }

   public void setDefaultTracker(String var1) {
      this.defaultTracker = var1;
   }

   public void setDelayStart(double var1) {
      this.delayStart = var1;
   }

   public void setDeviceKnown(boolean var1) {
      this.deviceKnown = var1;
   }

   public void setEventBufferingEnabled(Boolean var1) {
      if (var1 == null) {
         this.eventBufferingEnabled = false;
      } else {
         this.eventBufferingEnabled = var1;
      }
   }

   public void setLogLevel(LogLevel var1) {
      this.setLogLevel(var1, this.environment);
   }

   public void setOnAttributionChangedListener(OnAttributionChangedListener var1) {
      this.onAttributionChangedListener = var1;
   }

   public void setOnDeeplinkResponseListener(OnDeeplinkResponseListener var1) {
      this.onDeeplinkResponseListener = var1;
   }

   public void setOnEventTrackingFailedListener(OnEventTrackingFailedListener var1) {
      this.onEventTrackingFailedListener = var1;
   }

   public void setOnEventTrackingSucceededListener(OnEventTrackingSucceededListener var1) {
      this.onEventTrackingSucceededListener = var1;
   }

   public void setOnSessionTrackingFailedListener(OnSessionTrackingFailedListener var1) {
      this.onSessionTrackingFailedListener = var1;
   }

   public void setOnSessionTrackingSucceededListener(OnSessionTrackingSucceededListener var1) {
      this.onSessionTrackingSucceededListener = var1;
   }

   public void setProcessName(String var1) {
      this.processName = var1;
   }

   public void setSdkPrefix(String var1) {
      this.sdkPrefix = var1;
   }

   public void setSendInBackground(boolean var1) {
      this.sendInBackground = var1;
   }

   public void setUserAgent(String var1) {
      this.userAgent = var1;
   }
}
