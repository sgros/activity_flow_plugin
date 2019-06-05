package androidx.work.impl.background.systemalarm;

import android.content.Context;
import android.content.Intent;
import androidx.work.Logger;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import androidx.work.impl.model.WorkSpec;
import java.util.ArrayList;
import java.util.List;

class ConstraintsCommandHandler {
    private static final String TAG = Logger.tagWithPrefix("ConstraintsCmdHandler");
    private final Context mContext;
    private final SystemAlarmDispatcher mDispatcher;
    private final int mStartId;
    private final WorkConstraintsTracker mWorkConstraintsTracker = new WorkConstraintsTracker(this.mContext, null);

    ConstraintsCommandHandler(Context context, int i, SystemAlarmDispatcher systemAlarmDispatcher) {
        this.mContext = context;
        this.mStartId = i;
        this.mDispatcher = systemAlarmDispatcher;
    }

    /* Access modifiers changed, original: 0000 */
    public void handleConstraintsChanged() {
        List<WorkSpec> scheduledWork = this.mDispatcher.getWorkManager().getWorkDatabase().workSpecDao().getScheduledWork();
        ConstraintProxy.updateAll(this.mContext, scheduledWork);
        this.mWorkConstraintsTracker.replace(scheduledWork);
        ArrayList<WorkSpec> arrayList = new ArrayList(scheduledWork.size());
        long currentTimeMillis = System.currentTimeMillis();
        for (WorkSpec workSpec : scheduledWork) {
            String str = workSpec.f30id;
            if (currentTimeMillis >= workSpec.calculateNextRunTime() && (!workSpec.hasConstraints() || this.mWorkConstraintsTracker.areAllConstraintsMet(str))) {
                arrayList.add(workSpec);
            }
        }
        for (WorkSpec workSpec2 : arrayList) {
            Intent createDelayMetIntent = CommandHandler.createDelayMetIntent(this.mContext, workSpec2.f30id);
            Logger.get().debug(TAG, String.format("Creating a delay_met command for workSpec with id (%s)", new Object[]{r1}), new Throwable[0]);
            this.mDispatcher.postOnMainThread(new AddRunnable(this.mDispatcher, createDelayMetIntent, this.mStartId));
        }
        this.mWorkConstraintsTracker.reset();
    }
}
