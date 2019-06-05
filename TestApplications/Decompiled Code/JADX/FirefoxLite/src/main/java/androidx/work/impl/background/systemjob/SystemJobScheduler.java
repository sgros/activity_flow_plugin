package androidx.work.impl.background.systemjob;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.PersistableBundle;
import androidx.work.Logger;
import androidx.work.WorkInfo.State;
import androidx.work.impl.Scheduler;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.SystemIdInfo;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.IdGenerator;
import java.util.List;

public class SystemJobScheduler implements Scheduler {
    private static final String TAG = Logger.tagWithPrefix("SystemJobScheduler");
    private final IdGenerator mIdGenerator;
    private final JobScheduler mJobScheduler;
    private final SystemJobInfoConverter mSystemJobInfoConverter;
    private final WorkManagerImpl mWorkManager;

    public SystemJobScheduler(Context context, WorkManagerImpl workManagerImpl) {
        this(context, workManagerImpl, (JobScheduler) context.getSystemService("jobscheduler"), new SystemJobInfoConverter(context));
    }

    public SystemJobScheduler(Context context, WorkManagerImpl workManagerImpl, JobScheduler jobScheduler, SystemJobInfoConverter systemJobInfoConverter) {
        this.mWorkManager = workManagerImpl;
        this.mJobScheduler = jobScheduler;
        this.mIdGenerator = new IdGenerator(context);
        this.mSystemJobInfoConverter = systemJobInfoConverter;
    }

    public void schedule(WorkSpec... workSpecArr) {
        WorkDatabase workDatabase = this.mWorkManager.getWorkDatabase();
        int length = workSpecArr.length;
        int i = 0;
        while (i < length) {
            WorkSpec workSpec = workSpecArr[i];
            workDatabase.beginTransaction();
            try {
                WorkSpec workSpec2 = workDatabase.workSpecDao().getWorkSpec(workSpec.f30id);
                Logger logger;
                String str;
                StringBuilder stringBuilder;
                if (workSpec2 == null) {
                    logger = Logger.get();
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Skipping scheduling ");
                    stringBuilder.append(workSpec.f30id);
                    stringBuilder.append(" because it's no longer in the DB");
                    logger.warning(str, stringBuilder.toString(), new Throwable[0]);
                } else if (workSpec2.state != State.ENQUEUED) {
                    logger = Logger.get();
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Skipping scheduling ");
                    stringBuilder.append(workSpec.f30id);
                    stringBuilder.append(" because it is no longer enqueued");
                    logger.warning(str, stringBuilder.toString(), new Throwable[0]);
                } else {
                    SystemIdInfo systemIdInfo = workDatabase.systemIdInfoDao().getSystemIdInfo(workSpec.f30id);
                    if (systemIdInfo == null || getPendingJobInfo(this.mJobScheduler, workSpec.f30id) == null) {
                        int nextJobSchedulerIdWithRange = systemIdInfo != null ? systemIdInfo.systemId : this.mIdGenerator.nextJobSchedulerIdWithRange(this.mWorkManager.getConfiguration().getMinJobSchedulerId(), this.mWorkManager.getConfiguration().getMaxJobSchedulerId());
                        if (systemIdInfo == null) {
                            this.mWorkManager.getWorkDatabase().systemIdInfoDao().insertSystemIdInfo(new SystemIdInfo(workSpec.f30id, nextJobSchedulerIdWithRange));
                        }
                        scheduleInternal(workSpec, nextJobSchedulerIdWithRange);
                        if (VERSION.SDK_INT == 23) {
                            scheduleInternal(workSpec, this.mIdGenerator.nextJobSchedulerIdWithRange(this.mWorkManager.getConfiguration().getMinJobSchedulerId(), this.mWorkManager.getConfiguration().getMaxJobSchedulerId()));
                        }
                        workDatabase.setTransactionSuccessful();
                    } else {
                        Logger.get().debug(TAG, String.format("Skipping scheduling %s because JobScheduler is aware of it already.", new Object[]{workSpec.f30id}), new Throwable[0]);
                    }
                }
                workDatabase.endTransaction();
                i++;
            } catch (Throwable th) {
                workDatabase.endTransaction();
            }
        }
    }

    public void scheduleInternal(WorkSpec workSpec, int i) {
        JobInfo convert = this.mSystemJobInfoConverter.convert(workSpec, i);
        Logger.get().debug(TAG, String.format("Scheduling work ID %s Job ID %s", new Object[]{workSpec.f30id, Integer.valueOf(i)}), new Throwable[0]);
        this.mJobScheduler.schedule(convert);
    }

    public void cancel(String str) {
        List<JobInfo> allPendingJobs = this.mJobScheduler.getAllPendingJobs();
        if (allPendingJobs != null) {
            for (JobInfo jobInfo : allPendingJobs) {
                if (str.equals(jobInfo.getExtras().getString("EXTRA_WORK_SPEC_ID"))) {
                    this.mWorkManager.getWorkDatabase().systemIdInfoDao().removeSystemIdInfo(str);
                    this.mJobScheduler.cancel(jobInfo.getId());
                    if (VERSION.SDK_INT != 23) {
                        return;
                    }
                }
            }
        }
    }

    public static void jobSchedulerCancelAll(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        if (jobScheduler != null) {
            List<JobInfo> allPendingJobs = jobScheduler.getAllPendingJobs();
            if (allPendingJobs != null) {
                for (JobInfo jobInfo : allPendingJobs) {
                    if (jobInfo.getExtras().containsKey("EXTRA_WORK_SPEC_ID")) {
                        jobScheduler.cancel(jobInfo.getId());
                    }
                }
            }
        }
    }

    private static JobInfo getPendingJobInfo(JobScheduler jobScheduler, String str) {
        List<JobInfo> allPendingJobs = jobScheduler.getAllPendingJobs();
        if (allPendingJobs != null) {
            for (JobInfo jobInfo : allPendingJobs) {
                PersistableBundle extras = jobInfo.getExtras();
                if (extras != null && extras.containsKey("EXTRA_WORK_SPEC_ID") && str.equals(extras.getString("EXTRA_WORK_SPEC_ID"))) {
                    return jobInfo;
                }
            }
        }
        return null;
    }
}
