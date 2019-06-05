package android.arch.core.executor;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultTaskExecutor extends TaskExecutor {
   private ExecutorService mDiskIO = Executors.newFixedThreadPool(2);
   private final Object mLock = new Object();
   private volatile Handler mMainHandler;

   public void executeOnDiskIO(Runnable var1) {
      this.mDiskIO.execute(var1);
   }

   public boolean isMainThread() {
      boolean var1;
      if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void postToMainThread(Runnable var1) {
      if (this.mMainHandler == null) {
         label150: {
            Object var2 = this.mLock;
            synchronized(var2){}

            Throwable var10000;
            boolean var10001;
            label144: {
               try {
                  if (this.mMainHandler == null) {
                     Handler var3 = new Handler(Looper.getMainLooper());
                     this.mMainHandler = var3;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label144;
               }

               label141:
               try {
                  break label150;
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label141;
               }
            }

            while(true) {
               Throwable var16 = var10000;

               try {
                  throw var16;
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      this.mMainHandler.post(var1);
   }
}
