// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl;

import android.text.TextUtils;
import androidx.work.impl.utils.EnqueueRunnable;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import androidx.work.Logger;
import androidx.work.WorkRequest;
import androidx.work.Operation;
import androidx.work.ExistingWorkPolicy;
import java.util.List;
import androidx.work.WorkContinuation;

public class WorkContinuationImpl extends WorkContinuation
{
    private static final String TAG;
    private final List<String> mAllIds;
    private boolean mEnqueued;
    private final ExistingWorkPolicy mExistingWorkPolicy;
    private final List<String> mIds;
    private final String mName;
    private Operation mOperation;
    private final List<WorkContinuationImpl> mParents;
    private final List<? extends WorkRequest> mWork;
    private final WorkManagerImpl mWorkManagerImpl;
    
    static {
        TAG = Logger.tagWithPrefix("WorkContinuationImpl");
    }
    
    WorkContinuationImpl(final WorkManagerImpl mWorkManagerImpl, final String mName, final ExistingWorkPolicy mExistingWorkPolicy, final List<? extends WorkRequest> mWork, final List<WorkContinuationImpl> mParents) {
        this.mWorkManagerImpl = mWorkManagerImpl;
        this.mName = mName;
        this.mExistingWorkPolicy = mExistingWorkPolicy;
        this.mWork = mWork;
        this.mParents = mParents;
        this.mIds = new ArrayList<String>(this.mWork.size());
        this.mAllIds = new ArrayList<String>();
        if (mParents != null) {
            final Iterator<WorkContinuationImpl> iterator = mParents.iterator();
            while (iterator.hasNext()) {
                this.mAllIds.addAll(iterator.next().mAllIds);
            }
        }
        for (int i = 0; i < mWork.size(); ++i) {
            final String stringId = ((WorkRequest)mWork.get(i)).getStringId();
            this.mIds.add(stringId);
            this.mAllIds.add(stringId);
        }
    }
    
    WorkContinuationImpl(final WorkManagerImpl workManagerImpl, final List<? extends WorkRequest> list) {
        this(workManagerImpl, null, ExistingWorkPolicy.KEEP, list, null);
    }
    
    private static boolean hasCycles(final WorkContinuationImpl workContinuationImpl, final Set<String> set) {
        set.addAll(workContinuationImpl.getIds());
        final Set<String> prerequisites = prerequisitesFor(workContinuationImpl);
        final Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (prerequisites.contains(iterator.next())) {
                return true;
            }
        }
        final List<WorkContinuationImpl> parents = workContinuationImpl.getParents();
        if (parents != null && !parents.isEmpty()) {
            final Iterator<WorkContinuationImpl> iterator2 = parents.iterator();
            while (iterator2.hasNext()) {
                if (hasCycles(iterator2.next(), set)) {
                    return true;
                }
            }
        }
        set.removeAll(workContinuationImpl.getIds());
        return false;
    }
    
    public static Set<String> prerequisitesFor(final WorkContinuationImpl workContinuationImpl) {
        final HashSet<Object> set = (HashSet<Object>)new HashSet<String>();
        final List<WorkContinuationImpl> parents = workContinuationImpl.getParents();
        if (parents != null && !parents.isEmpty()) {
            final Iterator<WorkContinuationImpl> iterator = parents.iterator();
            while (iterator.hasNext()) {
                set.addAll(iterator.next().getIds());
            }
        }
        return (Set<String>)set;
    }
    
    public Operation enqueue() {
        if (!this.mEnqueued) {
            final EnqueueRunnable enqueueRunnable = new EnqueueRunnable(this);
            this.mWorkManagerImpl.getWorkTaskExecutor().executeOnBackgroundThread(enqueueRunnable);
            this.mOperation = enqueueRunnable.getOperation();
        }
        else {
            Logger.get().warning(WorkContinuationImpl.TAG, String.format("Already enqueued work ids (%s)", TextUtils.join((CharSequence)", ", (Iterable)this.mIds)), new Throwable[0]);
        }
        return this.mOperation;
    }
    
    public ExistingWorkPolicy getExistingWorkPolicy() {
        return this.mExistingWorkPolicy;
    }
    
    public List<String> getIds() {
        return this.mIds;
    }
    
    public String getName() {
        return this.mName;
    }
    
    public List<WorkContinuationImpl> getParents() {
        return this.mParents;
    }
    
    public List<? extends WorkRequest> getWork() {
        return this.mWork;
    }
    
    public WorkManagerImpl getWorkManagerImpl() {
        return this.mWorkManagerImpl;
    }
    
    public boolean hasCycles() {
        return hasCycles(this, new HashSet<String>());
    }
    
    public boolean isEnqueued() {
        return this.mEnqueued;
    }
    
    public void markEnqueued() {
        this.mEnqueued = true;
    }
}
