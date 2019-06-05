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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WorkContinuationImpl extends WorkContinuation {
   private static final String TAG = Logger.tagWithPrefix("WorkContinuationImpl");
   private final List mAllIds;
   private boolean mEnqueued;
   private final ExistingWorkPolicy mExistingWorkPolicy;
   private final List mIds;
   private final String mName;
   private Operation mOperation;
   private final List mParents;
   private final List mWork;
   private final WorkManagerImpl mWorkManagerImpl;

   WorkContinuationImpl(WorkManagerImpl var1, String var2, ExistingWorkPolicy var3, List var4, List var5) {
      this.mWorkManagerImpl = var1;
      this.mName = var2;
      this.mExistingWorkPolicy = var3;
      this.mWork = var4;
      this.mParents = var5;
      this.mIds = new ArrayList(this.mWork.size());
      this.mAllIds = new ArrayList();
      if (var5 != null) {
         Iterator var7 = var5.iterator();

         while(var7.hasNext()) {
            WorkContinuationImpl var9 = (WorkContinuationImpl)var7.next();
            this.mAllIds.addAll(var9.mAllIds);
         }
      }

      for(int var6 = 0; var6 < var4.size(); ++var6) {
         String var8 = ((WorkRequest)var4.get(var6)).getStringId();
         this.mIds.add(var8);
         this.mAllIds.add(var8);
      }

   }

   WorkContinuationImpl(WorkManagerImpl var1, List var2) {
      this(var1, (String)null, ExistingWorkPolicy.KEEP, var2, (List)null);
   }

   private static boolean hasCycles(WorkContinuationImpl var0, Set var1) {
      var1.addAll(var0.getIds());
      Set var2 = prerequisitesFor(var0);
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         if (var2.contains((String)var3.next())) {
            return true;
         }
      }

      List var4 = var0.getParents();
      if (var4 != null && !var4.isEmpty()) {
         var3 = var4.iterator();

         while(var3.hasNext()) {
            if (hasCycles((WorkContinuationImpl)var3.next(), var1)) {
               return true;
            }
         }
      }

      var1.removeAll(var0.getIds());
      return false;
   }

   public static Set prerequisitesFor(WorkContinuationImpl var0) {
      HashSet var1 = new HashSet();
      List var2 = var0.getParents();
      if (var2 != null && !var2.isEmpty()) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            var1.addAll(((WorkContinuationImpl)var3.next()).getIds());
         }
      }

      return var1;
   }

   public Operation enqueue() {
      if (!this.mEnqueued) {
         EnqueueRunnable var1 = new EnqueueRunnable(this);
         this.mWorkManagerImpl.getWorkTaskExecutor().executeOnBackgroundThread(var1);
         this.mOperation = var1.getOperation();
      } else {
         Logger.get().warning(TAG, String.format("Already enqueued work ids (%s)", TextUtils.join(", ", this.mIds)));
      }

      return this.mOperation;
   }

   public ExistingWorkPolicy getExistingWorkPolicy() {
      return this.mExistingWorkPolicy;
   }

   public List getIds() {
      return this.mIds;
   }

   public String getName() {
      return this.mName;
   }

   public List getParents() {
      return this.mParents;
   }

   public List getWork() {
      return this.mWork;
   }

   public WorkManagerImpl getWorkManagerImpl() {
      return this.mWorkManagerImpl;
   }

   public boolean hasCycles() {
      return hasCycles(this, new HashSet());
   }

   public boolean isEnqueued() {
      return this.mEnqueued;
   }

   public void markEnqueued() {
      this.mEnqueued = true;
   }
}
