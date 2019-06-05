package androidx.work;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Keep;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.UUID;
import java.util.concurrent.Executor;

public abstract class ListenableWorker {
   private Context mAppContext;
   private volatile boolean mStopped;
   private boolean mUsed;
   private WorkerParameters mWorkerParams;

   @SuppressLint({"BanKeepAnnotation"})
   @Keep
   public ListenableWorker(Context var1, WorkerParameters var2) {
      if (var1 != null) {
         if (var2 != null) {
            this.mAppContext = var1;
            this.mWorkerParams = var2;
         } else {
            throw new IllegalArgumentException("WorkerParameters is null");
         }
      } else {
         throw new IllegalArgumentException("Application Context is null");
      }
   }

   public final Context getApplicationContext() {
      return this.mAppContext;
   }

   public Executor getBackgroundExecutor() {
      return this.mWorkerParams.getBackgroundExecutor();
   }

   public final UUID getId() {
      return this.mWorkerParams.getId();
   }

   public final Data getInputData() {
      return this.mWorkerParams.getInputData();
   }

   public WorkerFactory getWorkerFactory() {
      return this.mWorkerParams.getWorkerFactory();
   }

   public final boolean isUsed() {
      return this.mUsed;
   }

   public void onStopped() {
   }

   public final void setUsed() {
      this.mUsed = true;
   }

   public abstract ListenableFuture startWork();

   public final void stop() {
      this.mStopped = true;
      this.onStopped();
   }

   public abstract static class Result {
      Result() {
      }

      public static ListenableWorker.Result failure() {
         return new ListenableWorker.Result.Failure();
      }

      public static ListenableWorker.Result retry() {
         return new ListenableWorker.Result.Retry();
      }

      public static ListenableWorker.Result success() {
         return new ListenableWorker.Result.Success();
      }

      public static ListenableWorker.Result success(Data var0) {
         return new ListenableWorker.Result.Success(var0);
      }

      public static final class Failure extends ListenableWorker.Result {
         private final Data mOutputData;

         public Failure() {
            this(Data.EMPTY);
         }

         public Failure(Data var1) {
            this.mOutputData = var1;
         }

         public boolean equals(Object var1) {
            if (this == var1) {
               return true;
            } else if (var1 != null && this.getClass() == var1.getClass()) {
               ListenableWorker.Result.Failure var2 = (ListenableWorker.Result.Failure)var1;
               return this.mOutputData.equals(var2.mOutputData);
            } else {
               return false;
            }
         }

         public Data getOutputData() {
            return this.mOutputData;
         }

         public int hashCode() {
            return ListenableWorker.Result.Failure.class.getName().hashCode() * 31 + this.mOutputData.hashCode();
         }

         public String toString() {
            StringBuilder var1 = new StringBuilder();
            var1.append("Failure {mOutputData=");
            var1.append(this.mOutputData);
            var1.append('}');
            return var1.toString();
         }
      }

      public static final class Retry extends ListenableWorker.Result {
         public boolean equals(Object var1) {
            boolean var2 = true;
            if (this == var1) {
               return true;
            } else {
               if (var1 == null || this.getClass() != var1.getClass()) {
                  var2 = false;
               }

               return var2;
            }
         }

         public int hashCode() {
            return ListenableWorker.Result.Retry.class.getName().hashCode();
         }

         public String toString() {
            return "Retry";
         }
      }

      public static final class Success extends ListenableWorker.Result {
         private final Data mOutputData;

         public Success() {
            this(Data.EMPTY);
         }

         public Success(Data var1) {
            this.mOutputData = var1;
         }

         public boolean equals(Object var1) {
            if (this == var1) {
               return true;
            } else if (var1 != null && this.getClass() == var1.getClass()) {
               ListenableWorker.Result.Success var2 = (ListenableWorker.Result.Success)var1;
               return this.mOutputData.equals(var2.mOutputData);
            } else {
               return false;
            }
         }

         public Data getOutputData() {
            return this.mOutputData;
         }

         public int hashCode() {
            return ListenableWorker.Result.Success.class.getName().hashCode() * 31 + this.mOutputData.hashCode();
         }

         public String toString() {
            StringBuilder var1 = new StringBuilder();
            var1.append("Success {mOutputData=");
            var1.append(this.mOutputData);
            var1.append('}');
            return var1.toString();
         }
      }
   }
}
