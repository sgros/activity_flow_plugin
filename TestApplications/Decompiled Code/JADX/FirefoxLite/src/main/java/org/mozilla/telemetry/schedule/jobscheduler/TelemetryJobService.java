package org.mozilla.telemetry.schedule.jobscheduler;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import java.util.Calendar;
import mozilla.components.support.base.log.logger.Logger;
import org.mozilla.telemetry.Telemetry;
import org.mozilla.telemetry.TelemetryHolder;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.net.TelemetryClient;
import org.mozilla.telemetry.ping.TelemetryPingBuilder;
import org.mozilla.telemetry.storage.TelemetryStorage;
import org.mozilla.telemetry.storage.TelemetryStorage.TelemetryStorageCallback;

public class TelemetryJobService extends JobService {
    private final Logger logger = new Logger("telemetry/service");
    private UploadPingsTask uploadTask;

    @SuppressLint({"StaticFieldLeak"})
    private class UploadPingsTask extends AsyncTask<JobParameters, Void, Void> {
        private UploadPingsTask() {
        }

        /* synthetic */ UploadPingsTask(TelemetryJobService telemetryJobService, C06031 c06031) {
            this();
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(JobParameters... jobParametersArr) {
            TelemetryJobService.this.uploadPingsInBackground(this, jobParametersArr[0]);
            return null;
        }
    }

    public boolean onStartJob(JobParameters jobParameters) {
        this.uploadTask = new UploadPingsTask(this, null);
        this.uploadTask.execute(new JobParameters[]{jobParameters});
        return true;
    }

    public boolean onStopJob(JobParameters jobParameters) {
        if (this.uploadTask != null) {
            this.uploadTask.cancel(true);
        }
        return true;
    }

    public void uploadPingsInBackground(AsyncTask asyncTask, JobParameters jobParameters) {
        Telemetry telemetry = TelemetryHolder.get();
        TelemetryConfiguration configuration = telemetry.getConfiguration();
        TelemetryStorage storage = telemetry.getStorage();
        for (TelemetryPingBuilder type : telemetry.getBuilders()) {
            String type2 = type.getType();
            Logger logger = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Performing upload of ping type: ");
            stringBuilder.append(type2);
            logger.debug(stringBuilder.toString(), null);
            if (asyncTask.isCancelled()) {
                this.logger.debug("Job stopped. Exiting.", null);
                return;
            } else if (storage.countStoredPings(type2) == 0) {
                logger = this.logger;
                stringBuilder = new StringBuilder();
                stringBuilder.append("No pings of type ");
                stringBuilder.append(type2);
                stringBuilder.append(" to upload");
                logger.debug(stringBuilder.toString(), null);
            } else if (hasReachedUploadLimit(configuration, type2)) {
                logger = this.logger;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Daily upload limit for type ");
                stringBuilder.append(type2);
                stringBuilder.append(" reached");
                logger.debug(stringBuilder.toString(), null);
            } else if (!performPingUpload(telemetry, type2)) {
                this.logger.info("Upload aborted. Rescheduling job if limit not reached.", null);
                jobFinished(jobParameters, hasReachedUploadLimit(configuration, type2) ^ 1);
                return;
            }
        }
        this.logger.debug("All uploads performed", null);
        jobFinished(jobParameters, false);
    }

    private boolean incrementUploadCount(TelemetryConfiguration telemetryConfiguration, String str) {
        SharedPreferences sharedPreferences = telemetryConfiguration.getSharedPreferences();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("last_uploade_");
        stringBuilder.append(str);
        long j = sharedPreferences.getLong(stringBuilder.toString(), 0);
        long now = now();
        boolean isSameDay = isSameDay(j, now);
        j = 1;
        if (isSameDay) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("upload_count_");
            stringBuilder.append(str);
            j = 1 + sharedPreferences.getLong(stringBuilder.toString(), 0);
        }
        Editor edit = sharedPreferences.edit();
        stringBuilder = new StringBuilder();
        stringBuilder.append("last_uploade_");
        stringBuilder.append(str);
        edit = edit.putLong(stringBuilder.toString(), now);
        stringBuilder = new StringBuilder();
        stringBuilder.append("upload_count_");
        stringBuilder.append(str);
        edit.putLong(stringBuilder.toString(), j).apply();
        return true;
    }

    private boolean hasReachedUploadLimit(TelemetryConfiguration telemetryConfiguration, String str) {
        SharedPreferences sharedPreferences = telemetryConfiguration.getSharedPreferences();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("last_uploade_");
        stringBuilder.append(str);
        long j = sharedPreferences.getLong(stringBuilder.toString(), 0);
        stringBuilder = new StringBuilder();
        stringBuilder.append("upload_count_");
        stringBuilder.append(str);
        return isSameDay(j, now()) && sharedPreferences.getLong(stringBuilder.toString(), 0) >= ((long) telemetryConfiguration.getMaximumNumberOfPingUploadsPerDay());
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isSameDay(long j, long j2) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(j2);
        if (instance.get(0) == instance2.get(0) && instance.get(1) == instance2.get(1) && instance.get(6) == instance2.get(6)) {
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public long now() {
        return System.currentTimeMillis();
    }

    private boolean performPingUpload(Telemetry telemetry, final String str) {
        final TelemetryConfiguration configuration = telemetry.getConfiguration();
        TelemetryStorage storage = telemetry.getStorage();
        final TelemetryClient client = telemetry.getClient();
        return storage.process(str, new TelemetryStorageCallback() {
            public boolean onTelemetryPingLoaded(String str, String str2) {
                return !TelemetryJobService.this.hasReachedUploadLimit(configuration, str) && client.uploadPing(configuration, str, str2) && TelemetryJobService.this.incrementUploadCount(configuration, str);
            }
        });
    }
}
