package androidx.work;

import android.content.Context;
import androidx.work.impl.WorkManagerImpl;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Collections;
import java.util.List;

public abstract class WorkManager {
   protected WorkManager() {
   }

   public static WorkManager getInstance() {
      WorkManagerImpl var0 = WorkManagerImpl.getInstance();
      if (var0 != null) {
         return var0;
      } else {
         throw new IllegalStateException("WorkManager is not initialized properly.  The most likely cause is that you disabled WorkManagerInitializer in your manifest but forgot to call WorkManager#initialize in your Application#onCreate or a ContentProvider.");
      }
   }

   public static void initialize(Context var0, Configuration var1) {
      WorkManagerImpl.initialize(var0, var1);
   }

   public abstract Operation cancelAllWorkByTag(String var1);

   public final Operation enqueue(WorkRequest var1) {
      return this.enqueue(Collections.singletonList(var1));
   }

   public abstract Operation enqueue(List var1);

   public abstract ListenableFuture getWorkInfosByTag(String var1);
}
