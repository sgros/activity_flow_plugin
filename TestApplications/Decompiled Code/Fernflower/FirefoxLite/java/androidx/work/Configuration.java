package androidx.work;

import android.os.Build.VERSION;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class Configuration {
   private final Executor mExecutor;
   private final int mLoggingLevel;
   private final int mMaxJobSchedulerId;
   private final int mMaxSchedulerLimit;
   private final int mMinJobSchedulerId;
   private final WorkerFactory mWorkerFactory;

   Configuration(Configuration.Builder var1) {
      if (var1.mExecutor == null) {
         this.mExecutor = this.createDefaultExecutor();
      } else {
         this.mExecutor = var1.mExecutor;
      }

      if (var1.mWorkerFactory == null) {
         this.mWorkerFactory = WorkerFactory.getDefaultWorkerFactory();
      } else {
         this.mWorkerFactory = var1.mWorkerFactory;
      }

      this.mLoggingLevel = var1.mLoggingLevel;
      this.mMinJobSchedulerId = var1.mMinJobSchedulerId;
      this.mMaxJobSchedulerId = var1.mMaxJobSchedulerId;
      this.mMaxSchedulerLimit = var1.mMaxSchedulerLimit;
   }

   private Executor createDefaultExecutor() {
      return Executors.newFixedThreadPool(Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 4)));
   }

   public Executor getExecutor() {
      return this.mExecutor;
   }

   public int getMaxJobSchedulerId() {
      return this.mMaxJobSchedulerId;
   }

   public int getMaxSchedulerLimit() {
      return VERSION.SDK_INT == 23 ? this.mMaxSchedulerLimit / 2 : this.mMaxSchedulerLimit;
   }

   public int getMinJobSchedulerId() {
      return this.mMinJobSchedulerId;
   }

   public int getMinimumLoggingLevel() {
      return this.mLoggingLevel;
   }

   public WorkerFactory getWorkerFactory() {
      return this.mWorkerFactory;
   }

   public static final class Builder {
      Executor mExecutor;
      int mLoggingLevel = 4;
      int mMaxJobSchedulerId = Integer.MAX_VALUE;
      int mMaxSchedulerLimit = 20;
      int mMinJobSchedulerId = 0;
      WorkerFactory mWorkerFactory;

      public Configuration build() {
         return new Configuration(this);
      }
   }
}
