// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.schedule.jobscheduler;

import android.app.job.JobInfo$Builder;
import android.content.ComponentName;
import android.app.job.JobScheduler;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.schedule.TelemetryScheduler;

public class JobSchedulerTelemetryScheduler implements TelemetryScheduler
{
    private final int jobId;
    
    public JobSchedulerTelemetryScheduler() {
        this(42);
    }
    
    public JobSchedulerTelemetryScheduler(final int jobId) {
        this.jobId = jobId;
    }
    
    @Override
    public void scheduleUpload(final TelemetryConfiguration telemetryConfiguration) {
        ((JobScheduler)telemetryConfiguration.getContext().getSystemService("jobscheduler")).schedule(new JobInfo$Builder(this.jobId, new ComponentName(telemetryConfiguration.getContext(), (Class)TelemetryJobService.class)).setRequiredNetworkType(1).setPersisted(true).setBackoffCriteria(telemetryConfiguration.getInitialBackoffForUpload(), 1).build());
    }
}
