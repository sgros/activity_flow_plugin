// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils;

import androidx.work.impl.Schedulers;
import androidx.work.impl.background.systemalarm.RescheduleReceiver;
import androidx.work.Operation;
import androidx.work.Constraints;
import androidx.work.impl.workers.ConstraintTrackingWorker;
import androidx.work.Data;
import androidx.work.impl.model.DependencyDao;
import androidx.work.impl.model.WorkSpecDao;
import java.util.Iterator;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.model.WorkName;
import androidx.work.impl.model.WorkTag;
import androidx.work.impl.model.Dependency;
import android.os.Build$VERSION;
import java.util.ArrayList;
import androidx.work.impl.model.WorkSpec;
import android.text.TextUtils;
import androidx.work.WorkInfo;
import androidx.work.ExistingWorkPolicy;
import androidx.work.WorkRequest;
import java.util.List;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.Logger;
import androidx.work.impl.WorkContinuationImpl;
import androidx.work.impl.OperationImpl;

public class EnqueueRunnable implements Runnable
{
    private static final String TAG;
    private final OperationImpl mOperation;
    private final WorkContinuationImpl mWorkContinuation;
    
    static {
        TAG = Logger.tagWithPrefix("EnqueueRunnable");
    }
    
    public EnqueueRunnable(final WorkContinuationImpl mWorkContinuation) {
        this.mWorkContinuation = mWorkContinuation;
        this.mOperation = new OperationImpl();
    }
    
    private static boolean enqueueContinuation(final WorkContinuationImpl workContinuationImpl) {
        final boolean enqueueWorkWithPrerequisites = enqueueWorkWithPrerequisites(workContinuationImpl.getWorkManagerImpl(), workContinuationImpl.getWork(), WorkContinuationImpl.prerequisitesFor(workContinuationImpl).toArray(new String[0]), workContinuationImpl.getName(), workContinuationImpl.getExistingWorkPolicy());
        workContinuationImpl.markEnqueued();
        return enqueueWorkWithPrerequisites;
    }
    
    private static boolean enqueueWorkWithPrerequisites(final WorkManagerImpl workManagerImpl, final List<? extends WorkRequest> list, final String[] array, final String s, final ExistingWorkPolicy existingWorkPolicy) {
        final long currentTimeMillis = System.currentTimeMillis();
        final WorkDatabase workDatabase = workManagerImpl.getWorkDatabase();
        int n;
        if (array != null && array.length > 0) {
            n = 1;
        }
        else {
            n = 0;
        }
        int n6;
        int n7;
        int n8;
        if (n != 0) {
            final int length = array.length;
            int n2 = 0;
            int n3 = 1;
            int n4 = 0;
            int n5 = 0;
            while (true) {
                n6 = n3;
                n7 = n4;
                n8 = n5;
                if (n2 >= length) {
                    break;
                }
                final String s2 = array[n2];
                final WorkSpec workSpec = workDatabase.workSpecDao().getWorkSpec(s2);
                if (workSpec == null) {
                    Logger.get().error(EnqueueRunnable.TAG, String.format("Prerequisite %s doesn't exist; not enqueuing", s2), new Throwable[0]);
                    return false;
                }
                final WorkInfo.State state = workSpec.state;
                n3 &= ((state == WorkInfo.State.SUCCEEDED) ? 1 : 0);
                int n9;
                if (state == WorkInfo.State.FAILED) {
                    n9 = 1;
                }
                else {
                    n9 = n4;
                    if (state == WorkInfo.State.CANCELLED) {
                        n5 = 1;
                        n9 = n4;
                    }
                }
                ++n2;
                n4 = n9;
            }
        }
        else {
            n6 = 1;
            n7 = 0;
            n8 = 0;
        }
        final boolean b = TextUtils.isEmpty((CharSequence)s) ^ true;
        final boolean b2 = b && n == 0;
        String[] array2 = array;
        int n10 = n;
        int n11 = n6;
        int n12 = n7;
        int n13 = n8;
        boolean b3 = false;
        String[] array3 = null;
        Label_0680: {
            if (b2) {
                final List<WorkSpec.IdAndState> workSpecIdAndStatesForName = workDatabase.workSpecDao().getWorkSpecIdAndStatesForName(s);
                array2 = array;
                n10 = n;
                n11 = n6;
                n12 = n7;
                n13 = n8;
                if (!workSpecIdAndStatesForName.isEmpty()) {
                    if (existingWorkPolicy != ExistingWorkPolicy.APPEND) {
                        if (existingWorkPolicy == ExistingWorkPolicy.KEEP) {
                            for (final WorkSpec.IdAndState idAndState : workSpecIdAndStatesForName) {
                                if (idAndState.state == WorkInfo.State.ENQUEUED || idAndState.state == WorkInfo.State.RUNNING) {
                                    return false;
                                }
                            }
                        }
                        CancelWorkRunnable.forName(s, workManagerImpl, false).run();
                        final WorkSpecDao workSpecDao = workDatabase.workSpecDao();
                        final Iterator<WorkSpec.IdAndState> iterator2 = workSpecIdAndStatesForName.iterator();
                        while (iterator2.hasNext()) {
                            workSpecDao.delete(iterator2.next().id);
                        }
                        b3 = true;
                        array3 = array;
                        break Label_0680;
                    }
                    final DependencyDao dependencyDao = workDatabase.dependencyDao();
                    final ArrayList<String> list2 = new ArrayList<String>();
                    for (final WorkSpec.IdAndState idAndState2 : workSpecIdAndStatesForName) {
                        boolean b4 = n6 != 0;
                        int n14 = n7;
                        int n15 = n8;
                        if (!dependencyDao.hasDependents(idAndState2.id)) {
                            final boolean b5 = idAndState2.state == WorkInfo.State.SUCCEEDED;
                            if (idAndState2.state == WorkInfo.State.FAILED) {
                                n14 = 1;
                            }
                            else {
                                n14 = n7;
                                if (idAndState2.state == WorkInfo.State.CANCELLED) {
                                    n8 = 1;
                                    n14 = n7;
                                }
                            }
                            list2.add(idAndState2.id);
                            b4 = (((b5 ? 1 : 0) & n6) != 0x0);
                            n15 = n8;
                        }
                        n6 = (b4 ? 1 : 0);
                        n7 = n14;
                        n8 = n15;
                    }
                    array2 = list2.toArray(array);
                    if (array2.length > 0) {
                        n10 = 1;
                        n11 = n6;
                        n12 = n7;
                        n13 = n8;
                    }
                    else {
                        n10 = 0;
                        n11 = n6;
                        n12 = n7;
                        n13 = n8;
                    }
                }
            }
            b3 = false;
            n8 = n13;
            n7 = n12;
            n6 = n11;
            n = n10;
            array3 = array2;
        }
        for (final WorkRequest workRequest : list) {
            final WorkSpec workSpec2 = workRequest.getWorkSpec();
            if (n != 0 && n6 == 0) {
                if (n7 != 0) {
                    workSpec2.state = WorkInfo.State.FAILED;
                }
                else if (n8 != 0) {
                    workSpec2.state = WorkInfo.State.CANCELLED;
                }
                else {
                    workSpec2.state = WorkInfo.State.BLOCKED;
                }
            }
            else if (!workSpec2.isPeriodic()) {
                workSpec2.periodStartTime = currentTimeMillis;
            }
            else {
                workSpec2.periodStartTime = 0L;
            }
            if (Build$VERSION.SDK_INT >= 23 && Build$VERSION.SDK_INT <= 25) {
                tryDelegateConstrainedWorkSpec(workSpec2);
            }
            if (workSpec2.state == WorkInfo.State.ENQUEUED) {
                b3 = true;
            }
            workDatabase.workSpecDao().insertWorkSpec(workSpec2);
            String[] array4 = array3;
            if (n != 0) {
                final int length2 = array3.length;
                int n16 = 0;
                while (true) {
                    array4 = array3;
                    if (n16 >= length2) {
                        break;
                    }
                    workDatabase.dependencyDao().insertDependency(new Dependency(workRequest.getStringId(), array3[n16]));
                    ++n16;
                }
            }
            final Iterator<String> iterator5 = workRequest.getTags().iterator();
            while (iterator5.hasNext()) {
                workDatabase.workTagDao().insert(new WorkTag(iterator5.next(), workRequest.getStringId()));
            }
            if (b) {
                workDatabase.workNameDao().insert(new WorkName(s, workRequest.getStringId()));
            }
            array3 = array4;
        }
        return b3;
    }
    
    private static boolean processContinuation(final WorkContinuationImpl workContinuationImpl) {
        final List<WorkContinuationImpl> parents = workContinuationImpl.getParents();
        boolean b = false;
        if (parents != null) {
            final Iterator<WorkContinuationImpl> iterator = parents.iterator();
            b = false;
            while (iterator.hasNext()) {
                final WorkContinuationImpl workContinuationImpl2 = iterator.next();
                if (!workContinuationImpl2.isEnqueued()) {
                    b |= processContinuation(workContinuationImpl2);
                }
                else {
                    Logger.get().warning(EnqueueRunnable.TAG, String.format("Already enqueued work ids (%s).", TextUtils.join((CharSequence)", ", (Iterable)workContinuationImpl2.getIds())), new Throwable[0]);
                }
            }
        }
        return enqueueContinuation(workContinuationImpl) | b;
    }
    
    private static void tryDelegateConstrainedWorkSpec(final WorkSpec workSpec) {
        final Constraints constraints = workSpec.constraints;
        if (constraints.requiresBatteryNotLow() || constraints.requiresStorageNotLow()) {
            final String workerClassName = workSpec.workerClassName;
            final Data.Builder builder = new Data.Builder();
            builder.putAll(workSpec.input).putString("androidx.work.impl.workers.ConstraintTrackingWorker.ARGUMENT_CLASS_NAME", workerClassName);
            workSpec.workerClassName = ConstraintTrackingWorker.class.getName();
            workSpec.input = builder.build();
        }
    }
    
    public boolean addToDatabase() {
        final WorkDatabase workDatabase = this.mWorkContinuation.getWorkManagerImpl().getWorkDatabase();
        workDatabase.beginTransaction();
        try {
            final boolean processContinuation = processContinuation(this.mWorkContinuation);
            workDatabase.setTransactionSuccessful();
            return processContinuation;
        }
        finally {
            workDatabase.endTransaction();
        }
    }
    
    public Operation getOperation() {
        return this.mOperation;
    }
    
    @Override
    public void run() {
        try {
            if (this.mWorkContinuation.hasCycles()) {
                throw new IllegalStateException(String.format("WorkContinuation has cycles (%s)", this.mWorkContinuation));
            }
            if (this.addToDatabase()) {
                PackageManagerHelper.setComponentEnabled(this.mWorkContinuation.getWorkManagerImpl().getApplicationContext(), RescheduleReceiver.class, true);
                this.scheduleWorkInBackground();
            }
            this.mOperation.setState((Operation.State)Operation.SUCCESS);
        }
        catch (Throwable t) {
            this.mOperation.setState((Operation.State)new Operation.State.FAILURE(t));
        }
    }
    
    public void scheduleWorkInBackground() {
        final WorkManagerImpl workManagerImpl = this.mWorkContinuation.getWorkManagerImpl();
        Schedulers.schedule(workManagerImpl.getConfiguration(), workManagerImpl.getWorkDatabase(), workManagerImpl.getSchedulers());
    }
}
