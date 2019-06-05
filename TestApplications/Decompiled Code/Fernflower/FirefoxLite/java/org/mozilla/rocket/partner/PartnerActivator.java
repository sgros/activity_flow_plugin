package org.mozilla.rocket.partner;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PartnerActivator {
   private static Executor executor = Executors.newCachedThreadPool();
   private Activation activation;
   private final Context context;
   private String[] partnerActivateKeys;

   public PartnerActivator(Context var1) {
      this.context = var1.getApplicationContext();
   }

   // $FF: synthetic method
   static Activation access$500(PartnerActivator var0) {
      return var0.activation;
   }

   // $FF: synthetic method
   static Activation access$502(PartnerActivator var0, Activation var1) {
      var0.activation = var1;
      return var1;
   }

   // $FF: synthetic method
   static void access$600(PartnerActivator var0, long var1) {
      var0.setSnoozeDuration(var1);
   }

   // $FF: synthetic method
   static boolean access$700(PartnerActivator var0) {
      return var0.inSnooze();
   }

   // $FF: synthetic method
   static void access$800(PartnerActivator var0, long var1) {
      var0.setLastCheckedTimestamp(var1);
   }

   // $FF: synthetic method
   static Context access$900(PartnerActivator var0) {
      return var0.context;
   }

   private long getLastFetchedTimestamp() {
      return getPreferences(this.context).getLong("long_fetch_timestamp", 0L);
   }

   private static SharedPreferences getPreferences(Context var0) {
      return var0.getSharedPreferences("partner_activator", 0);
   }

   private long getSnoozeDuration() {
      return getPreferences(this.context).getLong("long_snooze_duration", 0L);
   }

   private PartnerActivator.Status getStatus() {
      String var1 = getPreferences(this.context).getString("string_activation_status", PartnerActivator.Status.Default.toString());
      PartnerActivator.Status var3;
      if (TextUtils.isEmpty(var1)) {
         var3 = PartnerActivator.Status.Default;
      } else {
         try {
            var3 = PartnerActivator.Status.valueOf(var1);
         } catch (Exception var2) {
            var3 = PartnerActivator.Status.Default;
         }
      }

      return var3;
   }

   private boolean inSnooze() {
      long var1 = this.getLastFetchedTimestamp();
      long var3 = this.getSnoozeDuration();
      long var5 = System.currentTimeMillis();
      boolean var7 = false;
      if (var1 > 0L && var3 > 0L) {
         if (var1 + var3 >= var5) {
            var7 = true;
         }

         return var7;
      } else {
         return false;
      }
   }

   private void postWorker(Runnable var1) {
      executor.execute(var1);
   }

   private void setLastCheckedTimestamp(long var1) {
      getPreferences(this.context).edit().putLong("long_fetch_timestamp", var1).apply();
   }

   private void setSnoozeDuration(long var1) {
      getPreferences(this.context).edit().putLong("long_snooze_duration", var1).apply();
   }

   private void setStatus(PartnerActivator.Status var1) {
      getPreferences(this.context).edit().putString("string_activation_status", var1.toString()).apply();
   }

   private boolean statusInvalidate(PartnerActivator.Status var1) {
      switch(var1) {
      case Disabled:
      case Done:
         StringBuilder var2 = new StringBuilder();
         var2.append("status: ");
         var2.append(var1);
         PartnerUtil.log(var2.toString());
         return true;
      case Snooze:
         if (this.inSnooze()) {
            PartnerUtil.log("status: inSnooze");
            return true;
         }

         this.setStatus(PartnerActivator.Status.Default);
         return false;
      default:
         return false;
      }
   }

   public void launch() {
      this.postWorker(new PartnerActivator.QueryActivationStatus(this));
   }

   private static class ActivationJobs {
      final PartnerActivator partnerActivator;

      ActivationJobs(PartnerActivator var1) {
         this.partnerActivator = var1;
      }
   }

   private static final class FetchActivation extends PartnerActivator.ActivationJobs implements Runnable {
      private final String[] activationKeys;
      private final String sourceUrl;

      FetchActivation(PartnerActivator var1, String var2) {
         super(var1);
         this.sourceUrl = var2;
         this.activationKeys = var1.partnerActivateKeys;
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }

   private static class PingActivation extends PartnerActivator.ActivationJobs implements Runnable {
      PingActivation(PartnerActivator var1) {
         super(var1);
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }

   private static final class QueryActivationStatus extends PartnerActivator.ActivationJobs implements Runnable {
      QueryActivationStatus(PartnerActivator var1) {
         super(var1);
      }

      public void run() {
         PartnerActivator.Status var1 = this.partnerActivator.getStatus();
         if (!this.partnerActivator.statusInvalidate(var1)) {
            String var2 = PartnerUtil.getProperty("ro.vendor.partner");
            if (TextUtils.isEmpty(var2)) {
               PartnerUtil.log("partner key not found, disabled");
               this.partnerActivator.setStatus(PartnerActivator.Status.Disabled);
            } else {
               this.partnerActivator.partnerActivateKeys = var2.split("/");
               if (this.partnerActivator.partnerActivateKeys != null && this.partnerActivator.partnerActivateKeys.length == 3) {
                  this.partnerActivator.postWorker(new PartnerActivator.FetchActivation(this.partnerActivator, "https://firefox.settings.services.mozilla.com/v1/buckets/main/collections/rocket-prefs/records"));
               } else {
                  PartnerUtil.log("partner key format invalid");
               }
            }
         }
      }
   }

   public static enum Status {
      Default,
      Disabled,
      Done,
      Snooze;
   }
}
