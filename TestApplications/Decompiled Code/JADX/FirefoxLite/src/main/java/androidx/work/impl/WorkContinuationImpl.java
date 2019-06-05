package androidx.work.impl;

import android.text.TextUtils;
import androidx.work.ExistingWorkPolicy;
import androidx.work.Logger;
import androidx.work.Operation;
import androidx.work.WorkContinuation;
import androidx.work.WorkRequest;
import androidx.work.impl.utils.EnqueueRunnable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkContinuationImpl extends WorkContinuation {
    private static final String TAG = Logger.tagWithPrefix("WorkContinuationImpl");
    private final List<String> mAllIds;
    private boolean mEnqueued;
    private final ExistingWorkPolicy mExistingWorkPolicy;
    private final List<String> mIds;
    private final String mName;
    private Operation mOperation;
    private final List<WorkContinuationImpl> mParents;
    private final List<? extends WorkRequest> mWork;
    private final WorkManagerImpl mWorkManagerImpl;

    public WorkManagerImpl getWorkManagerImpl() {
        return this.mWorkManagerImpl;
    }

    public String getName() {
        return this.mName;
    }

    public ExistingWorkPolicy getExistingWorkPolicy() {
        return this.mExistingWorkPolicy;
    }

    public List<? extends WorkRequest> getWork() {
        return this.mWork;
    }

    public List<String> getIds() {
        return this.mIds;
    }

    public boolean isEnqueued() {
        return this.mEnqueued;
    }

    public void markEnqueued() {
        this.mEnqueued = true;
    }

    public List<WorkContinuationImpl> getParents() {
        return this.mParents;
    }

    WorkContinuationImpl(WorkManagerImpl workManagerImpl, List<? extends WorkRequest> list) {
        this(workManagerImpl, null, ExistingWorkPolicy.KEEP, list, null);
    }

    WorkContinuationImpl(WorkManagerImpl workManagerImpl, String str, ExistingWorkPolicy existingWorkPolicy, List<? extends WorkRequest> list, List<WorkContinuationImpl> list2) {
        this.mWorkManagerImpl = workManagerImpl;
        this.mName = str;
        this.mExistingWorkPolicy = existingWorkPolicy;
        this.mWork = list;
        this.mParents = list2;
        this.mIds = new ArrayList(this.mWork.size());
        this.mAllIds = new ArrayList();
        if (list2 != null) {
            for (WorkContinuationImpl workContinuationImpl : list2) {
                this.mAllIds.addAll(workContinuationImpl.mAllIds);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            str = ((WorkRequest) list.get(i)).getStringId();
            this.mIds.add(str);
            this.mAllIds.add(str);
        }
    }

    public Operation enqueue() {
        if (this.mEnqueued) {
            Logger.get().warning(TAG, String.format("Already enqueued work ids (%s)", new Object[]{TextUtils.join(", ", this.mIds)}), new Throwable[0]);
        } else {
            EnqueueRunnable enqueueRunnable = new EnqueueRunnable(this);
            this.mWorkManagerImpl.getWorkTaskExecutor().executeOnBackgroundThread(enqueueRunnable);
            this.mOperation = enqueueRunnable.getOperation();
        }
        return this.mOperation;
    }

    public boolean hasCycles() {
        return hasCycles(this, new HashSet());
    }

    private static boolean hasCycles(WorkContinuationImpl workContinuationImpl, Set<String> set) {
        set.addAll(workContinuationImpl.getIds());
        Set prerequisitesFor = prerequisitesFor(workContinuationImpl);
        for (String contains : set) {
            if (prerequisitesFor.contains(contains)) {
                return true;
            }
        }
        List<WorkContinuationImpl> parents = workContinuationImpl.getParents();
        if (!(parents == null || parents.isEmpty())) {
            for (WorkContinuationImpl hasCycles : parents) {
                if (hasCycles(hasCycles, set)) {
                    return true;
                }
            }
        }
        set.removeAll(workContinuationImpl.getIds());
        return false;
    }

    public static Set<String> prerequisitesFor(WorkContinuationImpl workContinuationImpl) {
        HashSet hashSet = new HashSet();
        List<WorkContinuationImpl> parents = workContinuationImpl.getParents();
        if (!(parents == null || parents.isEmpty())) {
            for (WorkContinuationImpl ids : parents) {
                hashSet.addAll(ids.getIds());
            }
        }
        return hashSet;
    }
}
