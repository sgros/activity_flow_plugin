// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemalarm;

import android.content.Intent;
import java.util.Iterator;
import java.util.List;
import androidx.work.impl.model.WorkSpec;
import java.util.ArrayList;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.Logger;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import android.content.Context;

class ConstraintsCommandHandler
{
    private static final String TAG;
    private final Context mContext;
    private final SystemAlarmDispatcher mDispatcher;
    private final int mStartId;
    private final WorkConstraintsTracker mWorkConstraintsTracker;
    
    static {
        TAG = Logger.tagWithPrefix("ConstraintsCmdHandler");
    }
    
    ConstraintsCommandHandler(final Context mContext, final int mStartId, final SystemAlarmDispatcher mDispatcher) {
        this.mContext = mContext;
        this.mStartId = mStartId;
        this.mDispatcher = mDispatcher;
        this.mWorkConstraintsTracker = new WorkConstraintsTracker(this.mContext, null);
    }
    
    void handleConstraintsChanged() {
        final List<WorkSpec> scheduledWork = this.mDispatcher.getWorkManager().getWorkDatabase().workSpecDao().getScheduledWork();
        ConstraintProxy.updateAll(this.mContext, scheduledWork);
        this.mWorkConstraintsTracker.replace(scheduledWork);
        final ArrayList list = new ArrayList<WorkSpec>(scheduledWork.size());
        final long currentTimeMillis = System.currentTimeMillis();
        for (final WorkSpec workSpec : scheduledWork) {
            final String id = workSpec.id;
            if (currentTimeMillis >= workSpec.calculateNextRunTime() && (!workSpec.hasConstraints() || this.mWorkConstraintsTracker.areAllConstraintsMet(id))) {
                list.add(workSpec);
            }
        }
        final Iterator<WorkSpec> iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            final String id2 = iterator2.next().id;
            final Intent delayMetIntent = CommandHandler.createDelayMetIntent(this.mContext, id2);
            Logger.get().debug(ConstraintsCommandHandler.TAG, String.format("Creating a delay_met command for workSpec with id (%s)", id2), new Throwable[0]);
            this.mDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, delayMetIntent, this.mStartId));
        }
        this.mWorkConstraintsTracker.reset();
    }
}
