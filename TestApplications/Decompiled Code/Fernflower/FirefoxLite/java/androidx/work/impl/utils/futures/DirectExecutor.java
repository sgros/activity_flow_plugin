package androidx.work.impl.utils.futures;

import java.util.concurrent.Executor;

enum DirectExecutor implements Executor {
   INSTANCE;

   public void execute(Runnable var1) {
      var1.run();
   }

   public String toString() {
      return "DirectExecutor";
   }
}
