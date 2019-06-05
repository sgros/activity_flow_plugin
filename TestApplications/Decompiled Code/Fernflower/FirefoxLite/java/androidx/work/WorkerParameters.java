package androidx.work;

import android.net.Network;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;

public final class WorkerParameters {
   private Executor mBackgroundExecutor;
   private UUID mId;
   private Data mInputData;
   private int mRunAttemptCount;
   private WorkerParameters.RuntimeExtras mRuntimeExtras;
   private Set mTags;
   private TaskExecutor mWorkTaskExecutor;
   private WorkerFactory mWorkerFactory;

   public WorkerParameters(UUID var1, Data var2, Collection var3, WorkerParameters.RuntimeExtras var4, int var5, Executor var6, TaskExecutor var7, WorkerFactory var8) {
      this.mId = var1;
      this.mInputData = var2;
      this.mTags = new HashSet(var3);
      this.mRuntimeExtras = var4;
      this.mRunAttemptCount = var5;
      this.mBackgroundExecutor = var6;
      this.mWorkTaskExecutor = var7;
      this.mWorkerFactory = var8;
   }

   public Executor getBackgroundExecutor() {
      return this.mBackgroundExecutor;
   }

   public UUID getId() {
      return this.mId;
   }

   public Data getInputData() {
      return this.mInputData;
   }

   public WorkerFactory getWorkerFactory() {
      return this.mWorkerFactory;
   }

   public static class RuntimeExtras {
      public Network network;
      public List triggeredContentAuthorities = Collections.emptyList();
      public List triggeredContentUris = Collections.emptyList();
   }
}
