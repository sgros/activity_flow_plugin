package org.mozilla.focus.telemetry;

import android.content.Context;
import android.os.Bundle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import org.mozilla.focus.utils.FirebaseHelper;
import org.mozilla.rocket.util.LoggerWrapper;

class FirebaseEvent {
   private static HashMap prefKeyWhitelist = new HashMap();
   private String eventName;
   private Bundle eventParam;

   FirebaseEvent(String var1, String var2, String var3, String var4) {
      StringBuilder var7 = new StringBuilder();
      var7.append(var2);
      var7.append("__");
      var7.append(var3);
      var7.append("__");
      var1 = var7.toString();
      StringBuilder var8 = new StringBuilder();
      var8.append(var1);
      var8.append(var4);
      this.eventName = var8.toString();
      int var5 = var1.length();
      if (this.eventName.length() > 40) {
         if (var5 > 20) {
            var8 = new StringBuilder();
            var8.append("Event[");
            var8.append(this.eventName);
            var8.append("]'s prefixLength too long  ");
            var8.append(var5);
            var8.append(" of ");
            var8.append(20);
            LoggerWrapper.throwOrWarn("FirebaseEvent", var8.toString());
         }

         var8 = new StringBuilder();
         var8.append("Event[");
         var8.append(this.eventName);
         var8.append("] exceeds Firebase event name limit ");
         var8.append(this.eventName.length());
         var8.append(" of ");
         var8.append(40);
         LoggerWrapper.throwOrWarn("FirebaseEvent", var8.toString());
         if (var4 != null) {
            int var6 = var1.length();
            var5 = var4.length();
            var8 = new StringBuilder();
            var8.append(var1);
            var8.append(var4.substring(var5 - (40 - var6), var5));
            this.eventName = var8.toString();
         }
      }

   }

   public static FirebaseEvent create(String var0, String var1, String var2, String var3) {
      return new FirebaseEvent(var0, var1, var2, var3);
   }

   private boolean equalBundles(Bundle var1, Bundle var2) {
      if (var1 == var2) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (var1.size() != var2.size()) {
         return false;
      } else if (!var1.keySet().containsAll(var2.keySet())) {
         return false;
      } else {
         Iterator var3 = var1.keySet().iterator();

         String var4;
         String var5;
         do {
            if (!var3.hasNext()) {
               return true;
            }

            var4 = (String)var3.next();
            var5 = (String)var1.get(var4);
            var4 = (String)var2.get(var4);
         } while(var5 != null && var5.equals(var4));

         return false;
      }
   }

   static String getValidPrefKey(String var0) {
      return (String)prefKeyWhitelist.get(var0);
   }

   static boolean isInitialized() {
      boolean var0;
      if (prefKeyWhitelist.size() != 0) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   private static String safeParamLength(String var0, int var1) {
      if (var0.length() > var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Exceeding limit of param content length:");
         var2.append(var0.length());
         var2.append(" of ");
         var2.append(var1);
         LoggerWrapper.throwOrWarn("FirebaseEvent", var2.toString());
      }

      return var0.substring(0, Math.min(var1, var0.length()));
   }

   static void setPrefKeyWhitelist(HashMap var0) {
      prefKeyWhitelist = var0;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else if (!(var1 instanceof FirebaseEvent)) {
         return false;
      } else {
         FirebaseEvent var4 = (FirebaseEvent)var1;
         boolean var3 = var2;
         if (this.eventName.equals(var4.eventName)) {
            var3 = var2;
            if (this.equalBundles(this.eventParam, var4.eventParam)) {
               var3 = true;
            }
         }

         return var3;
      }
   }

   public void event(Context var1) {
      if (var1 != null) {
         if (TelemetryWrapper.isTelemetryEnabled(var1)) {
            FirebaseHelper.event(var1.getApplicationContext(), this.eventName, this.eventParam);
         }

      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.eventName, this.eventParam});
   }

   public FirebaseEvent param(String var1, String var2) {
      if (this.eventParam == null) {
         this.eventParam = new Bundle();
      }

      if (this.eventParam.size() >= 25) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Firebase event[");
         var3.append(this.eventName);
         var3.append("] has too many parameters");
         LoggerWrapper.throwOrWarn("FirebaseEvent", var3.toString());
      }

      this.eventParam.putString(safeParamLength(var1, 40), safeParamLength(var2, 100));
      return this;
   }
}
