package androidx.work.impl.utils;

import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.futures.SettableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;

public abstract class StatusRunnable implements Runnable {
   private final SettableFuture mFuture = SettableFuture.create();

   public static StatusRunnable forTag(final WorkManagerImpl var0, final String var1) {
      return new StatusRunnable() {
         List runInternal() {
            List var1x = var0.getWorkDatabase().workSpecDao().getWorkStatusPojoForTag(var1);
            return (List)WorkSpec.WORK_INFO_MAPPER.apply(var1x);
         }
      };
   }

   public ListenableFuture getFuture() {
      return this.mFuture;
   }

   public void run() {
      try {
         Object var1 = this.runInternal();
         this.mFuture.set(var1);
      } catch (Throwable var2) {
         this.mFuture.setException(var2);
      }

   }

   abstract Object runInternal();
}
