package com.adjust.sdk;

import android.net.Uri;
import java.lang.ref.WeakReference;
import org.json.JSONObject;

public class AttributionHandler implements IAttributionHandler {
   private static final String ATTRIBUTION_TIMER_NAME = "Attribution timer";
   private WeakReference activityHandlerWeakRef;
   private ActivityPackage attributionPackage;
   private ILogger logger = AdjustFactory.getLogger();
   private boolean paused;
   private CustomScheduledExecutor scheduledExecutor = new CustomScheduledExecutor("AttributionHandler", false);
   private TimerOnce timer = new TimerOnce(new Runnable() {
      public void run() {
         AttributionHandler.this.sendAttributionRequest();
      }
   }, "Attribution timer");

   public AttributionHandler(IActivityHandler var1, ActivityPackage var2, boolean var3) {
      this.init(var1, var2, var3);
   }

   private void checkAttributionI(IActivityHandler var1, ResponseData var2) {
      if (var2.jsonResponse != null) {
         long var3 = var2.jsonResponse.optLong("ask_in", -1L);
         if (var3 >= 0L) {
            var1.setAskingAttribution(true);
            this.getAttributionI(var3);
         } else {
            var1.setAskingAttribution(false);
            var2.attribution = AdjustAttribution.fromJson(var2.jsonResponse.optJSONObject("attribution"), var2.adid);
         }
      }
   }

   private void checkAttributionResponseI(IActivityHandler var1, AttributionResponseData var2) {
      this.checkAttributionI(var1, var2);
      this.checkDeeplinkI(var2);
      var1.launchAttributionResponseTasks(var2);
   }

   private void checkDeeplinkI(AttributionResponseData var1) {
      if (var1.jsonResponse != null) {
         JSONObject var2 = var1.jsonResponse.optJSONObject("attribution");
         if (var2 != null) {
            String var3 = var2.optString("deeplink", (String)null);
            if (var3 != null) {
               var1.deeplink = Uri.parse(var3);
            }
         }
      }
   }

   private void checkSdkClickResponseI(IActivityHandler var1, SdkClickResponseData var2) {
      this.checkAttributionI(var1, var2);
      var1.launchSdkClickResponseTasks(var2);
   }

   private void checkSessionResponseI(IActivityHandler var1, SessionResponseData var2) {
      this.checkAttributionI(var1, var2);
      var1.launchSessionResponseTasks(var2);
   }

   private void getAttributionI(long var1) {
      if (this.timer.getFireIn() <= var1) {
         if (var1 != 0L) {
            double var3 = (double)var1 / 1000.0D;
            String var5 = Util.SecondsDisplayFormat.format(var3);
            this.logger.debug("Waiting to query attribution in %s seconds", var5);
         }

         this.timer.startIn(var1);
      }
   }

   private void sendAttributionRequestI() {
      if (this.paused) {
         this.logger.debug("Attribution handler is paused");
      } else {
         this.logger.verbose("%s", this.attributionPackage.getExtendedString());

         try {
            ResponseData var1 = UtilNetworking.createGETHttpsURLConnection(this.attributionPackage);
            if (var1 instanceof AttributionResponseData) {
               this.checkAttributionResponse((AttributionResponseData)var1);
            }
         } catch (Exception var2) {
            this.logger.error("Failed to get attribution (%s)", var2.getMessage());
         }
      }
   }

   public void checkAttributionResponse(final AttributionResponseData var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            IActivityHandler var1x = (IActivityHandler)AttributionHandler.this.activityHandlerWeakRef.get();
            if (var1x != null) {
               AttributionHandler.this.checkAttributionResponseI(var1x, var1);
            }
         }
      });
   }

   public void checkSdkClickResponse(final SdkClickResponseData var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            IActivityHandler var1x = (IActivityHandler)AttributionHandler.this.activityHandlerWeakRef.get();
            if (var1x != null) {
               AttributionHandler.this.checkSdkClickResponseI(var1x, var1);
            }
         }
      });
   }

   public void checkSessionResponse(final SessionResponseData var1) {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            IActivityHandler var1x = (IActivityHandler)AttributionHandler.this.activityHandlerWeakRef.get();
            if (var1x != null) {
               AttributionHandler.this.checkSessionResponseI(var1x, var1);
            }
         }
      });
   }

   public void getAttribution() {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            AttributionHandler.this.getAttributionI(0L);
         }
      });
   }

   public void init(IActivityHandler var1, ActivityPackage var2, boolean var3) {
      this.activityHandlerWeakRef = new WeakReference(var1);
      this.attributionPackage = var2;
      this.paused = var3 ^ true;
   }

   public void pauseSending() {
      this.paused = true;
   }

   public void resumeSending() {
      this.paused = false;
   }

   public void sendAttributionRequest() {
      this.scheduledExecutor.submit(new Runnable() {
         public void run() {
            AttributionHandler.this.sendAttributionRequestI();
         }
      });
   }

   public void teardown() {
      this.logger.verbose("AttributionHandler teardown");
      if (this.timer != null) {
         this.timer.teardown();
      }

      if (this.scheduledExecutor != null) {
         try {
            this.scheduledExecutor.shutdownNow();
         } catch (SecurityException var2) {
         }
      }

      if (this.activityHandlerWeakRef != null) {
         this.activityHandlerWeakRef.clear();
      }

      this.scheduledExecutor = null;
      this.activityHandlerWeakRef = null;
      this.logger = null;
      this.attributionPackage = null;
      this.timer = null;
   }
}
