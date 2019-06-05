package com.adjust.sdk;

import android.content.Context;
import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class AdjustFactory {
   private static IActivityHandler activityHandler;
   private static IAttributionHandler attributionHandler;
   private static HttpsURLConnection httpsURLConnection;
   private static ILogger logger;
   private static long maxDelayStart;
   private static IPackageHandler packageHandler;
   private static BackoffStrategy packageHandlerBackoffStrategy;
   private static IRequestHandler requestHandler;
   private static BackoffStrategy sdkClickBackoffStrategy;
   private static ISdkClickHandler sdkClickHandler;
   private static long sessionInterval;
   private static long subsessionInterval;
   private static long timerInterval;
   private static long timerStart;

   public static IActivityHandler getActivityHandler(AdjustConfig var0) {
      if (activityHandler == null) {
         return ActivityHandler.getInstance(var0);
      } else {
         activityHandler.init(var0);
         return activityHandler;
      }
   }

   public static IAttributionHandler getAttributionHandler(IActivityHandler var0, ActivityPackage var1, boolean var2) {
      if (attributionHandler == null) {
         return new AttributionHandler(var0, var1, var2);
      } else {
         attributionHandler.init(var0, var1, var2);
         return attributionHandler;
      }
   }

   public static HttpsURLConnection getHttpsURLConnection(URL var0) throws IOException {
      return httpsURLConnection == null ? (HttpsURLConnection)var0.openConnection() : httpsURLConnection;
   }

   public static ILogger getLogger() {
      if (logger == null) {
         logger = new Logger();
      }

      return logger;
   }

   public static long getMaxDelayStart() {
      return maxDelayStart == -1L ? 10000L : maxDelayStart;
   }

   public static IPackageHandler getPackageHandler(ActivityHandler var0, Context var1, boolean var2) {
      if (packageHandler == null) {
         return new PackageHandler(var0, var1, var2);
      } else {
         packageHandler.init(var0, var1, var2);
         return packageHandler;
      }
   }

   public static BackoffStrategy getPackageHandlerBackoffStrategy() {
      return packageHandlerBackoffStrategy == null ? BackoffStrategy.LONG_WAIT : packageHandlerBackoffStrategy;
   }

   public static IRequestHandler getRequestHandler(IPackageHandler var0) {
      if (requestHandler == null) {
         return new RequestHandler(var0);
      } else {
         requestHandler.init(var0);
         return requestHandler;
      }
   }

   public static BackoffStrategy getSdkClickBackoffStrategy() {
      return sdkClickBackoffStrategy == null ? BackoffStrategy.SHORT_WAIT : sdkClickBackoffStrategy;
   }

   public static ISdkClickHandler getSdkClickHandler(IActivityHandler var0, boolean var1) {
      if (sdkClickHandler == null) {
         return new SdkClickHandler(var0, var1);
      } else {
         sdkClickHandler.init(var0, var1);
         return sdkClickHandler;
      }
   }

   public static long getSessionInterval() {
      return sessionInterval == -1L ? 1800000L : sessionInterval;
   }

   public static long getSubsessionInterval() {
      return subsessionInterval == -1L ? 1000L : subsessionInterval;
   }

   public static long getTimerInterval() {
      return timerInterval == -1L ? 60000L : timerInterval;
   }

   public static long getTimerStart() {
      return timerStart == -1L ? 60000L : timerStart;
   }

   public static void setActivityHandler(IActivityHandler var0) {
      activityHandler = var0;
   }

   public static void setAttributionHandler(IAttributionHandler var0) {
      attributionHandler = var0;
   }

   public static void setHttpsURLConnection(HttpsURLConnection var0) {
      httpsURLConnection = var0;
   }

   public static void setLogger(ILogger var0) {
      logger = var0;
   }

   public static void setPackageHandler(IPackageHandler var0) {
      packageHandler = var0;
   }

   public static void setPackageHandlerBackoffStrategy(BackoffStrategy var0) {
      packageHandlerBackoffStrategy = var0;
   }

   public static void setRequestHandler(IRequestHandler var0) {
      requestHandler = var0;
   }

   public static void setSdkClickBackoffStrategy(BackoffStrategy var0) {
      sdkClickBackoffStrategy = var0;
   }

   public static void setSdkClickHandler(ISdkClickHandler var0) {
      sdkClickHandler = var0;
   }

   public static void setSessionInterval(long var0) {
      sessionInterval = var0;
   }

   public static void setSubsessionInterval(long var0) {
      subsessionInterval = var0;
   }

   public static void setTimerInterval(long var0) {
      timerInterval = var0;
   }

   public static void setTimerStart(long var0) {
      timerStart = var0;
   }

   public static class URLGetConnection {
      HttpsURLConnection httpsURLConnection;
      URL url;

      URLGetConnection(HttpsURLConnection var1, URL var2) {
         this.httpsURLConnection = var1;
         this.url = var2;
      }
   }
}
