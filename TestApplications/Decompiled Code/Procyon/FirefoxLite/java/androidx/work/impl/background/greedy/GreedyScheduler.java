// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.greedy;

import java.util.Collection;
import android.text.TextUtils;
import android.os.Build$VERSION;
import androidx.work.WorkInfo;
import java.util.Iterator;
import java.util.ArrayList;
import android.content.Context;
import androidx.work.Logger;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import androidx.work.impl.model.WorkSpec;
import java.util.List;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.Scheduler;
import androidx.work.impl.ExecutionListener;

public class GreedyScheduler implements ExecutionListener, Scheduler, WorkConstraintsCallback
{
    private static final String TAG;
    private List<WorkSpec> mConstrainedWorkSpecs;
    private final Object mLock;
    private boolean mRegisteredExecutionListener;
    private WorkConstraintsTracker mWorkConstraintsTracker;
    private WorkManagerImpl mWorkManagerImpl;
    
    static {
        TAG = Logger.tagWithPrefix("GreedyScheduler");
    }
    
    public GreedyScheduler(final Context context, final WorkManagerImpl mWorkManagerImpl) {
        this.mConstrainedWorkSpecs = new ArrayList<WorkSpec>();
        this.mWorkManagerImpl = mWorkManagerImpl;
        this.mWorkConstraintsTracker = new WorkConstraintsTracker(context, this);
        this.mLock = new Object();
    }
    
    private void registerExecutionListenerIfNeeded() {
        if (!this.mRegisteredExecutionListener) {
            this.mWorkManagerImpl.getProcessor().addExecutionListener(this);
            this.mRegisteredExecutionListener = true;
        }
    }
    
    private void removeConstraintTrackingFor(final String anObject) {
        synchronized (this.mLock) {
            for (int size = this.mConstrainedWorkSpecs.size(), i = 0; i < size; ++i) {
                if (this.mConstrainedWorkSpecs.get(i).id.equals(anObject)) {
                    Logger.get().debug(GreedyScheduler.TAG, String.format("Stopping tracking for %s", anObject), new Throwable[0]);
                    this.mConstrainedWorkSpecs.remove(i);
                    this.mWorkConstraintsTracker.replace(this.mConstrainedWorkSpecs);
                    break;
                }
            }
        }
    }
    
    @Override
    public void cancel(final String s) {
        this.registerExecutionListenerIfNeeded();
        Logger.get().debug(GreedyScheduler.TAG, String.format("Cancelling work ID %s", s), new Throwable[0]);
        this.mWorkManagerImpl.stopWork(s);
    }
    
    @Override
    public void onAllConstraintsMet(final List<String> list) {
        for (final String s : list) {
            Logger.get().debug(GreedyScheduler.TAG, String.format("Constraints met: Scheduling work ID %s", s), new Throwable[0]);
            this.mWorkManagerImpl.startWork(s);
        }
    }
    
    @Override
    public void onAllConstraintsNotMet(final List<String> list) {
        for (final String s : list) {
            Logger.get().debug(GreedyScheduler.TAG, String.format("Constraints not met: Cancelling work ID %s", s), new Throwable[0]);
            this.mWorkManagerImpl.stopWork(s);
        }
    }
    
    @Override
    public void onExecuted(final String s, final boolean b) {
        this.removeConstraintTrackingFor(s);
    }
    
    @Override
    public void schedule(final WorkSpec... array) {
        this.registerExecutionListenerIfNeeded();
        final ArrayList<WorkSpec> list = new ArrayList<WorkSpec>();
        final ArrayList<String> list2 = new ArrayList<String>();
        for (final WorkSpec workSpec : array) {
            if (workSpec.state == WorkInfo.State.ENQUEUED && !workSpec.isPeriodic() && workSpec.initialDelay == 0L && !workSpec.isBackedOff()) {
                if (workSpec.hasConstraints()) {
                    if (Build$VERSION.SDK_INT < 24 || !workSpec.constraints.hasContentUriTriggers()) {
                        list.add(workSpec);
                        list2.add(workSpec.id);
                    }
                }
                else {
                    Logger.get().debug(GreedyScheduler.TAG, String.format("Starting work for %s", workSpec.id), new Throwable[0]);
                    this.mWorkManagerImpl.startWork(workSpec.id);
                }
            }
        }
        synchronized (this.mLock) {
            if (!list.isEmpty()) {
                Logger.get().debug(GreedyScheduler.TAG, String.format("Starting tracking for [%s]", TextUtils.join((CharSequence)",", (Iterable)list2)), new Throwable[0]);
                this.mConstrainedWorkSpecs.addAll(list);
                this.mWorkConstraintsTracker.replace(this.mConstrainedWorkSpecs);
            }
        }
    }
}
