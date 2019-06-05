package com.adjust.sdk;

import java.util.LinkedHashMap;
import java.util.Map;

public class AdjustEvent {
   private static ILogger logger = AdjustFactory.getLogger();
   Map callbackParameters;
   String currency;
   String eventToken;
   String orderId;
   Map partnerParameters;
   Double revenue;

   public AdjustEvent(String var1) {
      if (checkEventToken(var1, logger)) {
         this.eventToken = var1;
      }
   }

   private static boolean checkEventToken(String var0, ILogger var1) {
      if (var0 == null) {
         var1.error("Missing Event Token");
         return false;
      } else if (var0.length() != 6) {
         var1.error("Malformed Event Token '%s'", var0);
         return false;
      } else {
         return true;
      }
   }

   private boolean checkRevenue(Double var1, String var2) {
      if (var1 != null) {
         if (var1 < 0.0D) {
            logger.error("Invalid amount %.5f", var1);
            return false;
         }

         if (var2 == null) {
            logger.error("Currency must be set with revenue");
            return false;
         }

         if (var2.equals("")) {
            logger.error("Currency is empty");
            return false;
         }
      } else if (var2 != null) {
         logger.error("Revenue must be set with currency");
         return false;
      }

      return true;
   }

   public void addCallbackParameter(String var1, String var2) {
      if (Util.isValidParameter(var1, "key", "Callback")) {
         if (Util.isValidParameter(var2, "value", "Callback")) {
            if (this.callbackParameters == null) {
               this.callbackParameters = new LinkedHashMap();
            }

            if ((String)this.callbackParameters.put(var1, var2) != null) {
               logger.warn("Key %s was overwritten", var1);
            }

         }
      }
   }

   public void addPartnerParameter(String var1, String var2) {
      if (Util.isValidParameter(var1, "key", "Partner")) {
         if (Util.isValidParameter(var2, "value", "Partner")) {
            if (this.partnerParameters == null) {
               this.partnerParameters = new LinkedHashMap();
            }

            if ((String)this.partnerParameters.put(var1, var2) != null) {
               logger.warn("Key %s was overwritten", var1);
            }

         }
      }
   }

   public boolean isValid() {
      boolean var1;
      if (this.eventToken != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void setOrderId(String var1) {
      this.orderId = var1;
   }

   public void setRevenue(double var1, String var3) {
      if (this.checkRevenue(var1, var3)) {
         this.revenue = var1;
         this.currency = var3;
      }
   }
}
