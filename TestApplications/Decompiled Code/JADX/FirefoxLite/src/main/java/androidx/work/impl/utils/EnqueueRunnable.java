package androidx.work.impl.utils;

import android.text.TextUtils;
import androidx.work.Constraints;
import androidx.work.Data.Builder;
import androidx.work.Logger;
import androidx.work.Operation;
import androidx.work.Operation.State.FAILURE;
import androidx.work.impl.OperationImpl;
import androidx.work.impl.Schedulers;
import androidx.work.impl.WorkContinuationImpl;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.background.systemalarm.RescheduleReceiver;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.workers.ConstraintTrackingWorker;
import java.util.List;

public class EnqueueRunnable implements Runnable {
    private static final String TAG = Logger.tagWithPrefix("EnqueueRunnable");
    private final OperationImpl mOperation = new OperationImpl();
    private final WorkContinuationImpl mWorkContinuation;

    public EnqueueRunnable(WorkContinuationImpl workContinuationImpl) {
        this.mWorkContinuation = workContinuationImpl;
    }

    public void run() {
        try {
            if (this.mWorkContinuation.hasCycles()) {
                throw new IllegalStateException(String.format("WorkContinuation has cycles (%s)", new Object[]{this.mWorkContinuation}));
            }
            if (addToDatabase()) {
                PackageManagerHelper.setComponentEnabled(this.mWorkContinuation.getWorkManagerImpl().getApplicationContext(), RescheduleReceiver.class, true);
                scheduleWorkInBackground();
            }
            this.mOperation.setState(Operation.SUCCESS);
        } catch (Throwable th) {
            this.mOperation.setState(new FAILURE(th));
        }
    }

    public Operation getOperation() {
        return this.mOperation;
    }

    public boolean addToDatabase() {
        WorkDatabase workDatabase = this.mWorkContinuation.getWorkManagerImpl().getWorkDatabase();
        workDatabase.beginTransaction();
        try {
            boolean processContinuation = processContinuation(this.mWorkContinuation);
            workDatabase.setTransactionSuccessful();
            return processContinuation;
        } finally {
            workDatabase.endTransaction();
        }
    }

    public void scheduleWorkInBackground() {
        WorkManagerImpl workManagerImpl = this.mWorkContinuation.getWorkManagerImpl();
        Schedulers.schedule(workManagerImpl.getConfiguration(), workManagerImpl.getWorkDatabase(), workManagerImpl.getSchedulers());
    }

    private static boolean processContinuation(WorkContinuationImpl workContinuationImpl) {
        List<WorkContinuationImpl> parents = workContinuationImpl.getParents();
        int i = 0;
        if (parents != null) {
            int i2 = 0;
            for (WorkContinuationImpl workContinuationImpl2 : parents) {
                if (workContinuationImpl2.isEnqueued()) {
                    Logger.get().warning(TAG, String.format("Already enqueued work ids (%s).", new Object[]{TextUtils.join(", ", workContinuationImpl2.getIds())}), new Throwable[0]);
                } else {
                    i2 |= processContinuation(workContinuationImpl2);
                }
            }
            i = i2;
        }
        return enqueueContinuation(workContinuationImpl) | i;
    }

    private static boolean enqueueContinuation(WorkContinuationImpl workContinuationImpl) {
        boolean enqueueWorkWithPrerequisites = enqueueWorkWithPrerequisites(workContinuationImpl.getWorkManagerImpl(), workContinuationImpl.getWork(), (String[]) WorkContinuationImpl.prerequisitesFor(workContinuationImpl).toArray(new String[0]), workContinuationImpl.getName(), workContinuationImpl.getExistingWorkPolicy());
        workContinuationImpl.markEnqueued();
        return enqueueWorkWithPrerequisites;
    }

    /* JADX WARNING: Removed duplicated region for block: B:95:0x0165  */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x016f  */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x019c A:{LOOP_END, LOOP:6: B:102:0x0196->B:104:0x019c} */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x01c5 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x01b5  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x011e  */
    private static boolean enqueueWorkWithPrerequisites(androidx.work.impl.WorkManagerImpl r18, java.util.List<? extends androidx.work.WorkRequest> r19, java.lang.String[] r20, java.lang.String r21, androidx.work.ExistingWorkPolicy r22) {
        /*
        r0 = r20;
        r1 = r21;
        r2 = r22;
        r3 = java.lang.System.currentTimeMillis();
        r5 = r18.getWorkDatabase();
        r6 = 0;
        r7 = 1;
        if (r0 == 0) goto L_0x0017;
    L_0x0012:
        r8 = r0.length;
        if (r8 <= 0) goto L_0x0017;
    L_0x0015:
        r8 = 1;
        goto L_0x0018;
    L_0x0017:
        r8 = 0;
    L_0x0018:
        if (r8 == 0) goto L_0x005b;
    L_0x001a:
        r9 = r0.length;
        r10 = 0;
        r11 = 1;
        r12 = 0;
        r13 = 0;
    L_0x001f:
        if (r10 >= r9) goto L_0x005e;
    L_0x0021:
        r14 = r0[r10];
        r15 = r5.workSpecDao();
        r15 = r15.getWorkSpec(r14);
        if (r15 != 0) goto L_0x0043;
    L_0x002d:
        r0 = androidx.work.Logger.get();
        r1 = TAG;
        r2 = "Prerequisite %s doesn't exist; not enqueuing";
        r3 = new java.lang.Object[r7];
        r3[r6] = r14;
        r2 = java.lang.String.format(r2, r3);
        r3 = new java.lang.Throwable[r6];
        r0.error(r1, r2, r3);
        return r6;
    L_0x0043:
        r14 = r15.state;
        r15 = androidx.work.WorkInfo.State.SUCCEEDED;
        if (r14 != r15) goto L_0x004b;
    L_0x0049:
        r15 = 1;
        goto L_0x004c;
    L_0x004b:
        r15 = 0;
    L_0x004c:
        r11 = r11 & r15;
        r15 = androidx.work.WorkInfo.State.FAILED;
        if (r14 != r15) goto L_0x0053;
    L_0x0051:
        r12 = 1;
        goto L_0x0058;
    L_0x0053:
        r15 = androidx.work.WorkInfo.State.CANCELLED;
        if (r14 != r15) goto L_0x0058;
    L_0x0057:
        r13 = 1;
    L_0x0058:
        r10 = r10 + 1;
        goto L_0x001f;
    L_0x005b:
        r11 = 1;
        r12 = 0;
        r13 = 0;
    L_0x005e:
        r9 = android.text.TextUtils.isEmpty(r21);
        r9 = r9 ^ r7;
        if (r9 == 0) goto L_0x0069;
    L_0x0065:
        if (r8 != 0) goto L_0x0069;
    L_0x0067:
        r10 = 1;
        goto L_0x006a;
    L_0x0069:
        r10 = 0;
    L_0x006a:
        if (r10 == 0) goto L_0x0113;
    L_0x006c:
        r10 = r5.workSpecDao();
        r10 = r10.getWorkSpecIdAndStatesForName(r1);
        r14 = r10.isEmpty();
        if (r14 != 0) goto L_0x0113;
    L_0x007a:
        r14 = androidx.work.ExistingWorkPolicy.APPEND;
        if (r2 != r14) goto L_0x00cd;
    L_0x007e:
        r2 = r5.dependencyDao();
        r8 = new java.util.ArrayList;
        r8.<init>();
        r10 = r10.iterator();
    L_0x008b:
        r14 = r10.hasNext();
        if (r14 == 0) goto L_0x00c0;
    L_0x0091:
        r14 = r10.next();
        r14 = (androidx.work.impl.model.WorkSpec.IdAndState) r14;
        r15 = r14.f28id;
        r15 = r2.hasDependents(r15);
        if (r15 != 0) goto L_0x00be;
    L_0x009f:
        r15 = r14.state;
        r7 = androidx.work.WorkInfo.State.SUCCEEDED;
        if (r15 != r7) goto L_0x00a7;
    L_0x00a5:
        r7 = 1;
        goto L_0x00a8;
    L_0x00a7:
        r7 = 0;
    L_0x00a8:
        r7 = r7 & r11;
        r11 = r14.state;
        r15 = androidx.work.WorkInfo.State.FAILED;
        if (r11 != r15) goto L_0x00b1;
    L_0x00af:
        r12 = 1;
        goto L_0x00b8;
    L_0x00b1:
        r11 = r14.state;
        r15 = androidx.work.WorkInfo.State.CANCELLED;
        if (r11 != r15) goto L_0x00b8;
    L_0x00b7:
        r13 = 1;
    L_0x00b8:
        r11 = r14.f28id;
        r8.add(r11);
        r11 = r7;
    L_0x00be:
        r7 = 1;
        goto L_0x008b;
    L_0x00c0:
        r0 = r8.toArray(r0);
        r0 = (java.lang.String[]) r0;
        r2 = r0.length;
        if (r2 <= 0) goto L_0x00cb;
    L_0x00c9:
        r8 = 1;
        goto L_0x0113;
    L_0x00cb:
        r8 = 0;
        goto L_0x0113;
    L_0x00cd:
        r7 = androidx.work.ExistingWorkPolicy.KEEP;
        if (r2 != r7) goto L_0x00ee;
    L_0x00d1:
        r2 = r10.iterator();
    L_0x00d5:
        r7 = r2.hasNext();
        if (r7 == 0) goto L_0x00ee;
    L_0x00db:
        r7 = r2.next();
        r7 = (androidx.work.impl.model.WorkSpec.IdAndState) r7;
        r14 = r7.state;
        r15 = androidx.work.WorkInfo.State.ENQUEUED;
        if (r14 == r15) goto L_0x00ed;
    L_0x00e7:
        r7 = r7.state;
        r14 = androidx.work.WorkInfo.State.RUNNING;
        if (r7 != r14) goto L_0x00d5;
    L_0x00ed:
        return r6;
    L_0x00ee:
        r2 = r18;
        r2 = androidx.work.impl.utils.CancelWorkRunnable.forName(r1, r2, r6);
        r2.run();
        r2 = r5.workSpecDao();
        r7 = r10.iterator();
    L_0x00ff:
        r10 = r7.hasNext();
        if (r10 == 0) goto L_0x0111;
    L_0x0105:
        r10 = r7.next();
        r10 = (androidx.work.impl.model.WorkSpec.IdAndState) r10;
        r10 = r10.f28id;
        r2.delete(r10);
        goto L_0x00ff;
    L_0x0111:
        r2 = 1;
        goto L_0x0114;
    L_0x0113:
        r2 = 0;
    L_0x0114:
        r7 = r19.iterator();
    L_0x0118:
        r10 = r7.hasNext();
        if (r10 == 0) goto L_0x01cc;
    L_0x011e:
        r10 = r7.next();
        r10 = (androidx.work.WorkRequest) r10;
        r14 = r10.getWorkSpec();
        if (r8 == 0) goto L_0x013f;
    L_0x012a:
        if (r11 != 0) goto L_0x013f;
    L_0x012c:
        if (r12 == 0) goto L_0x0133;
    L_0x012e:
        r15 = androidx.work.WorkInfo.State.FAILED;
        r14.state = r15;
        goto L_0x0147;
    L_0x0133:
        if (r13 == 0) goto L_0x013a;
    L_0x0135:
        r15 = androidx.work.WorkInfo.State.CANCELLED;
        r14.state = r15;
        goto L_0x0147;
    L_0x013a:
        r15 = androidx.work.WorkInfo.State.BLOCKED;
        r14.state = r15;
        goto L_0x0147;
    L_0x013f:
        r15 = r14.isPeriodic();
        if (r15 != 0) goto L_0x014a;
    L_0x0145:
        r14.periodStartTime = r3;
    L_0x0147:
        r16 = r7;
        goto L_0x0150;
    L_0x014a:
        r16 = r7;
        r6 = 0;
        r14.periodStartTime = r6;
    L_0x0150:
        r6 = android.os.Build.VERSION.SDK_INT;
        r7 = 23;
        if (r6 < r7) goto L_0x015f;
    L_0x0156:
        r6 = android.os.Build.VERSION.SDK_INT;
        r7 = 25;
        if (r6 > r7) goto L_0x015f;
    L_0x015c:
        tryDelegateConstrainedWorkSpec(r14);
    L_0x015f:
        r6 = r14.state;
        r7 = androidx.work.WorkInfo.State.ENQUEUED;
        if (r6 != r7) goto L_0x0166;
    L_0x0165:
        r2 = 1;
    L_0x0166:
        r6 = r5.workSpecDao();
        r6.insertWorkSpec(r14);
        if (r8 == 0) goto L_0x018c;
    L_0x016f:
        r6 = r0.length;
        r7 = 0;
    L_0x0171:
        if (r7 >= r6) goto L_0x018c;
    L_0x0173:
        r14 = r0[r7];
        r15 = new androidx.work.impl.model.Dependency;
        r17 = r0;
        r0 = r10.getStringId();
        r15.<init>(r0, r14);
        r0 = r5.dependencyDao();
        r0.insertDependency(r15);
        r7 = r7 + 1;
        r0 = r17;
        goto L_0x0171;
    L_0x018c:
        r17 = r0;
        r0 = r10.getTags();
        r0 = r0.iterator();
    L_0x0196:
        r6 = r0.hasNext();
        if (r6 == 0) goto L_0x01b3;
    L_0x019c:
        r6 = r0.next();
        r6 = (java.lang.String) r6;
        r7 = r5.workTagDao();
        r14 = new androidx.work.impl.model.WorkTag;
        r15 = r10.getStringId();
        r14.<init>(r6, r15);
        r7.insert(r14);
        goto L_0x0196;
    L_0x01b3:
        if (r9 == 0) goto L_0x01c5;
    L_0x01b5:
        r0 = r5.workNameDao();
        r6 = new androidx.work.impl.model.WorkName;
        r7 = r10.getStringId();
        r6.<init>(r1, r7);
        r0.insert(r6);
    L_0x01c5:
        r7 = r16;
        r0 = r17;
        r6 = 0;
        goto L_0x0118;
    L_0x01cc:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.impl.utils.EnqueueRunnable.enqueueWorkWithPrerequisites(androidx.work.impl.WorkManagerImpl, java.util.List, java.lang.String[], java.lang.String, androidx.work.ExistingWorkPolicy):boolean");
    }

    private static void tryDelegateConstrainedWorkSpec(WorkSpec workSpec) {
        Constraints constraints = workSpec.constraints;
        if (constraints.requiresBatteryNotLow() || constraints.requiresStorageNotLow()) {
            String str = workSpec.workerClassName;
            Builder builder = new Builder();
            builder.putAll(workSpec.input).putString("androidx.work.impl.workers.ConstraintTrackingWorker.ARGUMENT_CLASS_NAME", str);
            workSpec.workerClassName = ConstraintTrackingWorker.class.getName();
            workSpec.input = builder.build();
        }
    }
}
