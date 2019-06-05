package org.mozilla.telemetry.schedule.jobscheduler;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import java.util.Calendar;
import java.util.Iterator;
import mozilla.components.support.base.log.logger.Logger;
import org.mozilla.telemetry.Telemetry;
import org.mozilla.telemetry.TelemetryHolder;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.net.TelemetryClient;
import org.mozilla.telemetry.ping.TelemetryPingBuilder;
import org.mozilla.telemetry.storage.TelemetryStorage;

public class TelemetryJobService extends JobService {
   private final Logger logger = new Logger("telemetry/service");
   private TelemetryJobService.UploadPingsTask uploadTask;

   private boolean hasReachedUploadLimit(TelemetryConfiguration var1, String var2) {
      SharedPreferences var3 = var1.getSharedPreferences();
      StringBuilder var4 = new StringBuilder();
      var4.append("last_uploade_");
      var4.append(var2);
      long var5 = var3.getLong(var4.toString(), 0L);
      var4 = new StringBuilder();
      var4.append("upload_count_");
      var4.append(var2);
      long var7 = var3.getLong(var4.toString(), 0L);
      boolean var9;
      if (this.isSameDay(var5, this.now()) && var7 >= (long)var1.getMaximumNumberOfPingUploadsPerDay()) {
         var9 = true;
      } else {
         var9 = false;
      }

      return var9;
   }

   private boolean incrementUploadCount(TelemetryConfiguration var1, String var2) {
      SharedPreferences var9 = var1.getSharedPreferences();
      StringBuilder var3 = new StringBuilder();
      var3.append("last_uploade_");
      var3.append(var2);
      long var4 = var9.getLong(var3.toString(), 0L);
      long var6 = this.now();
      boolean var8 = this.isSameDay(var4, var6);
      var4 = 1L;
      if (var8) {
         var3 = new StringBuilder();
         var3.append("upload_count_");
         var3.append(var2);
         var4 = 1L + var9.getLong(var3.toString(), 0L);
      }

      Editor var12 = var9.edit();
      StringBuilder var10 = new StringBuilder();
      var10.append("last_uploade_");
      var10.append(var2);
      Editor var11 = var12.putLong(var10.toString(), var6);
      var3 = new StringBuilder();
      var3.append("upload_count_");
      var3.append(var2);
      var11.putLong(var3.toString(), var4).apply();
      return true;
   }

   private boolean performPingUpload(Telemetry var1, final String var2) {
      final TelemetryConfiguration var3 = var1.getConfiguration();
      return var1.getStorage().process(var2, new TelemetryStorage.TelemetryStorageCallback(var1.getClient()) {
         // $FF: synthetic field
         final TelemetryClient val$client;

         {
            this.val$client = var4;
         }

         public boolean onTelemetryPingLoaded(String var1, String var2x) {
            boolean var3x;
            if (!TelemetryJobService.this.hasReachedUploadLimit(var3, var2) && this.val$client.uploadPing(var3, var1, var2x) && TelemetryJobService.this.incrementUploadCount(var3, var2)) {
               var3x = true;
            } else {
               var3x = false;
            }

            return var3x;
         }
      });
   }

   boolean isSameDay(long var1, long var3) {
      Calendar var5 = Calendar.getInstance();
      var5.setTimeInMillis(var1);
      Calendar var6 = Calendar.getInstance();
      var6.setTimeInMillis(var3);
      boolean var7 = false;
      boolean var8 = var7;
      if (var5.get(0) == var6.get(0)) {
         var8 = var7;
         if (var5.get(1) == var6.get(1)) {
            var8 = var7;
            if (var5.get(6) == var6.get(6)) {
               var8 = true;
            }
         }
      }

      return var8;
   }

   long now() {
      return System.currentTimeMillis();
   }

   public boolean onStartJob(JobParameters var1) {
      this.uploadTask = new TelemetryJobService.UploadPingsTask();
      this.uploadTask.execute(new JobParameters[]{var1});
      return true;
   }

   public boolean onStopJob(JobParameters var1) {
      if (this.uploadTask != null) {
         this.uploadTask.cancel(true);
      }

      return true;
   }

   public void uploadPingsInBackground(AsyncTask var1, JobParameters var2) {
      Telemetry var3 = TelemetryHolder.get();
      TelemetryConfiguration var4 = var3.getConfiguration();
      TelemetryStorage var5 = var3.getStorage();
      Iterator var6 = var3.getBuilders().iterator();

      while(var6.hasNext()) {
         String var7 = ((TelemetryPingBuilder)var6.next()).getType();
         Logger var8 = this.logger;
         StringBuilder var9 = new StringBuilder();
         var9.append("Performing upload of ping type: ");
         var9.append(var7);
         var8.debug(var9.toString(), (Throwable)null);
         if (var1.isCancelled()) {
            this.logger.debug("Job stopped. Exiting.", (Throwable)null);
            return;
         }

         if (var5.countStoredPings(var7) == 0) {
            var8 = this.logger;
            var9 = new StringBuilder();
            var9.append("No pings of type ");
            var9.append(var7);
            var9.append(" to upload");
            var8.debug(var9.toString(), (Throwable)null);
         } else if (this.hasReachedUploadLimit(var4, var7)) {
            Logger var11 = this.logger;
            StringBuilder var10 = new StringBuilder();
            var10.append("Daily upload limit for type ");
            var10.append(var7);
            var10.append(" reached");
            var11.debug(var10.toString(), (Throwable)null);
         } else if (!this.performPingUpload(var3, var7)) {
            this.logger.info("Upload aborted. Rescheduling job if limit not reached.", (Throwable)null);
            this.jobFinished(var2, this.hasReachedUploadLimit(var4, var7) ^ true);
            return;
         }
      }

      this.logger.debug("All uploads performed", (Throwable)null);
      this.jobFinished(var2, false);
   }

   @SuppressLint({"StaticFieldLeak"})
   private class UploadPingsTask extends AsyncTask {
      private UploadPingsTask() {
      }

      // $FF: synthetic method
      UploadPingsTask(Object var2) {
         this();
      }

      protected Void doInBackground(JobParameters... var1) {
         JobParameters var2 = var1[0];
         TelemetryJobService.this.uploadPingsInBackground(this, var2);
         return null;
      }
   }
}
