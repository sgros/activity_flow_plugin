// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemalarm;

import java.util.List;
import androidx.work.impl.model.WorkSpec;
import java.util.Collections;
import androidx.work.impl.utils.WakeLocks;
import androidx.work.Logger;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import android.os.PowerManager$WakeLock;
import android.content.Context;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.ExecutionListener;

public class DelayMetCommandHandler implements ExecutionListener, TimeLimitExceededListener, WorkConstraintsCallback
{
    private static final String TAG;
    private final Context mContext;
    private final SystemAlarmDispatcher mDispatcher;
    private boolean mHasConstraints;
    private boolean mHasPendingStopWorkCommand;
    private final Object mLock;
    private final int mStartId;
    private PowerManager$WakeLock mWakeLock;
    private final WorkConstraintsTracker mWorkConstraintsTracker;
    private final String mWorkSpecId;
    
    static {
        TAG = Logger.tagWithPrefix("DelayMetCommandHandler");
    }
    
    DelayMetCommandHandler(final Context mContext, final int mStartId, final String mWorkSpecId, final SystemAlarmDispatcher mDispatcher) {
        this.mContext = mContext;
        this.mStartId = mStartId;
        this.mDispatcher = mDispatcher;
        this.mWorkSpecId = mWorkSpecId;
        this.mWorkConstraintsTracker = new WorkConstraintsTracker(this.mContext, this);
        this.mHasConstraints = false;
        this.mHasPendingStopWorkCommand = false;
        this.mLock = new Object();
    }
    
    private void cleanUp() {
        synchronized (this.mLock) {
            this.mDispatcher.getWorkTimer().stopTimer(this.mWorkSpecId);
            if (this.mWakeLock != null && this.mWakeLock.isHeld()) {
                Logger.get().debug(DelayMetCommandHandler.TAG, String.format("Releasing wakelock %s for WorkSpec %s", this.mWakeLock, this.mWorkSpecId), new Throwable[0]);
                this.mWakeLock.release();
            }
        }
    }
    
    private void stopWork() {
        synchronized (this.mLock) {
            if (!this.mHasPendingStopWorkCommand) {
                Logger.get().debug(DelayMetCommandHandler.TAG, String.format("Stopping work for workspec %s", this.mWorkSpecId), new Throwable[0]);
                this.mDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, CommandHandler.createStopWorkIntent(this.mContext, this.mWorkSpecId), this.mStartId));
                if (this.mDispatcher.getProcessor().isEnqueued(this.mWorkSpecId)) {
                    Logger.get().debug(DelayMetCommandHandler.TAG, String.format("WorkSpec %s needs to be rescheduled", this.mWorkSpecId), new Throwable[0]);
                    this.mDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, CommandHandler.createScheduleWorkIntent(this.mContext, this.mWorkSpecId), this.mStartId));
                }
                else {
                    Logger.get().debug(DelayMetCommandHandler.TAG, String.format("Processor does not have WorkSpec %s. No need to reschedule ", this.mWorkSpecId), new Throwable[0]);
                }
                this.mHasPendingStopWorkCommand = true;
            }
            else {
                Logger.get().debug(DelayMetCommandHandler.TAG, String.format("Already stopped work for %s", this.mWorkSpecId), new Throwable[0]);
            }
        }
    }
    
    void handleProcessWork() {
        this.mWakeLock = WakeLocks.newWakeLock(this.mContext, String.format("%s (%s)", this.mWorkSpecId, this.mStartId));
        Logger.get().debug(DelayMetCommandHandler.TAG, String.format("Acquiring wakelock %s for WorkSpec %s", this.mWakeLock, this.mWorkSpecId), new Throwable[0]);
        this.mWakeLock.acquire();
        final WorkSpec workSpec = this.mDispatcher.getWorkManager().getWorkDatabase().workSpecDao().getWorkSpec(this.mWorkSpecId);
        if (workSpec == null) {
            this.stopWork();
            return;
        }
        if (!(this.mHasConstraints = workSpec.hasConstraints())) {
            Logger.get().debug(DelayMetCommandHandler.TAG, String.format("No constraints for %s", this.mWorkSpecId), new Throwable[0]);
            this.onAllConstraintsMet(Collections.singletonList(this.mWorkSpecId));
        }
        else {
            this.mWorkConstraintsTracker.replace(Collections.singletonList(workSpec));
        }
    }
    
    @Override
    public void onAllConstraintsMet(final List<String> list) {
        if (!list.contains(this.mWorkSpecId)) {
            return;
        }
        Logger.get().debug(DelayMetCommandHandler.TAG, String.format("onAllConstraintsMet for %s", this.mWorkSpecId), new Throwable[0]);
        if (this.mDispatcher.getProcessor().startWork(this.mWorkSpecId)) {
            this.mDispatcher.getWorkTimer().startTimer(this.mWorkSpecId, 600000L, (WorkTimer.TimeLimitExceededListener)this);
        }
        else {
            this.cleanUp();
        }
    }
    
    @Override
    public void onAllConstraintsNotMet(final List<String> list) {
        this.stopWork();
    }
    
    @Override
    public void onExecuted(final String s, final boolean b) {
        Logger.get().debug(DelayMetCommandHandler.TAG, String.format("onExecuted %s, %s", s, b), new Throwable[0]);
        this.cleanUp();
        if (b) {
            this.mDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, CommandHandler.createScheduleWorkIntent(this.mContext, this.mWorkSpecId), this.mStartId));
        }
        if (this.mHasConstraints) {
            this.mDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, CommandHandler.createConstraintsChangedIntent(this.mContext), this.mStartId));
        }
    }
    
    @Override
    public void onTimeLimitExceeded(final String s) {
        Logger.get().debug(DelayMetCommandHandler.TAG, String.format("Exceeded time limits on execution for %s", s), new Throwable[0]);
        this.stopWork();
    }
}
