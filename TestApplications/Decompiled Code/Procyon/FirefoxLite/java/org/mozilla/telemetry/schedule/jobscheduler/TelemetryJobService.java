// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.schedule.jobscheduler;

import android.annotation.SuppressLint;
import java.util.Iterator;
import org.mozilla.telemetry.ping.TelemetryPingBuilder;
import org.mozilla.telemetry.TelemetryHolder;
import android.os.AsyncTask;
import android.app.job.JobParameters;
import java.util.Calendar;
import org.mozilla.telemetry.net.TelemetryClient;
import org.mozilla.telemetry.storage.TelemetryStorage;
import org.mozilla.telemetry.Telemetry;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import mozilla.components.support.base.log.logger.Logger;
import android.app.job.JobService;

public class TelemetryJobService extends JobService
{
    private final Logger logger;
    private UploadPingsTask uploadTask;
    
    public TelemetryJobService() {
        this.logger = new Logger("telemetry/service");
    }
    
    private boolean hasReachedUploadLimit(final TelemetryConfiguration telemetryConfiguration, final String s) {
        final SharedPreferences sharedPreferences = telemetryConfiguration.getSharedPreferences();
        final StringBuilder sb = new StringBuilder();
        sb.append("last_uploade_");
        sb.append(s);
        final long long1 = sharedPreferences.getLong(sb.toString(), 0L);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("upload_count_");
        sb2.append(s);
        final long long2 = sharedPreferences.getLong(sb2.toString(), 0L);
        return this.isSameDay(long1, this.now()) && long2 >= telemetryConfiguration.getMaximumNumberOfPingUploadsPerDay();
    }
    
    private boolean incrementUploadCount(final TelemetryConfiguration telemetryConfiguration, final String s) {
        final SharedPreferences sharedPreferences = telemetryConfiguration.getSharedPreferences();
        final StringBuilder sb = new StringBuilder();
        sb.append("last_uploade_");
        sb.append(s);
        final long long1 = sharedPreferences.getLong(sb.toString(), 0L);
        final long now = this.now();
        final boolean sameDay = this.isSameDay(long1, now);
        long n = 1L;
        if (sameDay) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("upload_count_");
            sb2.append(s);
            n = 1L + sharedPreferences.getLong(sb2.toString(), 0L);
        }
        final SharedPreferences$Editor edit = sharedPreferences.edit();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("last_uploade_");
        sb3.append(s);
        final SharedPreferences$Editor putLong = edit.putLong(sb3.toString(), now);
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("upload_count_");
        sb4.append(s);
        putLong.putLong(sb4.toString(), n).apply();
        return true;
    }
    
    private boolean performPingUpload(final Telemetry telemetry, final String s) {
        return telemetry.getStorage().process(s, (TelemetryStorage.TelemetryStorageCallback)new TelemetryStorage.TelemetryStorageCallback() {
            final /* synthetic */ TelemetryClient val$client = telemetry.getClient();
            final /* synthetic */ TelemetryConfiguration val$configuration = telemetry.getConfiguration();
            
            @Override
            public boolean onTelemetryPingLoaded(final String s, final String s2) {
                return !TelemetryJobService.this.hasReachedUploadLimit(this.val$configuration, s) && this.val$client.uploadPing(this.val$configuration, s, s2) && TelemetryJobService.this.incrementUploadCount(this.val$configuration, s);
            }
        });
    }
    
    boolean isSameDay(final long timeInMillis, final long timeInMillis2) {
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeInMillis);
        final Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(timeInMillis2);
        boolean b2;
        final boolean b = b2 = false;
        if (instance.get(0) == instance2.get(0)) {
            b2 = b;
            if (instance.get(1) == instance2.get(1)) {
                b2 = b;
                if (instance.get(6) == instance2.get(6)) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    long now() {
        return System.currentTimeMillis();
    }
    
    public boolean onStartJob(final JobParameters jobParameters) {
        (this.uploadTask = new UploadPingsTask()).execute((Object[])new JobParameters[] { jobParameters });
        return true;
    }
    
    public boolean onStopJob(final JobParameters jobParameters) {
        if (this.uploadTask != null) {
            this.uploadTask.cancel(true);
        }
        return true;
    }
    
    public void uploadPingsInBackground(final AsyncTask asyncTask, final JobParameters jobParameters) {
        final Telemetry value = TelemetryHolder.get();
        final TelemetryConfiguration configuration = value.getConfiguration();
        final TelemetryStorage storage = value.getStorage();
        final Iterator<TelemetryPingBuilder> iterator = value.getBuilders().iterator();
        while (iterator.hasNext()) {
            final String type = iterator.next().getType();
            final Logger logger = this.logger;
            final StringBuilder sb = new StringBuilder();
            sb.append("Performing upload of ping type: ");
            sb.append(type);
            logger.debug(sb.toString(), null);
            if (asyncTask.isCancelled()) {
                this.logger.debug("Job stopped. Exiting.", null);
                return;
            }
            if (storage.countStoredPings(type) == 0) {
                final Logger logger2 = this.logger;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("No pings of type ");
                sb2.append(type);
                sb2.append(" to upload");
                logger2.debug(sb2.toString(), null);
            }
            else if (this.hasReachedUploadLimit(configuration, type)) {
                final Logger logger3 = this.logger;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Daily upload limit for type ");
                sb3.append(type);
                sb3.append(" reached");
                logger3.debug(sb3.toString(), null);
            }
            else {
                if (!this.performPingUpload(value, type)) {
                    this.logger.info("Upload aborted. Rescheduling job if limit not reached.", null);
                    this.jobFinished(jobParameters, this.hasReachedUploadLimit(configuration, type) ^ true);
                    return;
                }
                continue;
            }
        }
        this.logger.debug("All uploads performed", null);
        this.jobFinished(jobParameters, false);
    }
    
    @SuppressLint({ "StaticFieldLeak" })
    private class UploadPingsTask extends AsyncTask<JobParameters, Void, Void>
    {
        protected Void doInBackground(final JobParameters... array) {
            TelemetryJobService.this.uploadPingsInBackground(this, array[0]);
            return null;
        }
    }
}
