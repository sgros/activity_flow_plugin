package android.arch.core.executor;

import java.util.concurrent.Executor;

public class ArchTaskExecutor extends TaskExecutor {
   private static final Executor sIOThreadExecutor = new Executor() {
      public void execute(Runnable var1) {
         ArchTaskExecutor.getInstance().executeOnDiskIO(var1);
      }
   };
   private static volatile ArchTaskExecutor sInstance;
   private static final Executor sMainThreadExecutor = new Executor() {
      public void execute(Runnable var1) {
         ArchTaskExecutor.getInstance().postToMainThread(var1);
      }
   };
   private TaskExecutor mDefaultTaskExecutor = new DefaultTaskExecutor();
   private TaskExecutor mDelegate;

   private ArchTaskExecutor() {
      this.mDelegate = this.mDefaultTaskExecutor;
   }

   public static Executor getIOThreadExecutor() {
      return sIOThreadExecutor;
   }

   public static ArchTaskExecutor getInstance() {
      if (sInstance != null) {
         return sInstance;
      } else {
         synchronized(ArchTaskExecutor.class){}

         Throwable var10000;
         boolean var10001;
         label145: {
            try {
               if (sInstance == null) {
                  ArchTaskExecutor var0 = new ArchTaskExecutor();
                  sInstance = var0;
               }
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label145;
            }

            label142:
            try {
               return sInstance;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break label142;
            }
         }

         while(true) {
            Throwable var13 = var10000;

            try {
               throw var13;
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void executeOnDiskIO(Runnable var1) {
      this.mDelegate.executeOnDiskIO(var1);
   }

   public boolean isMainThread() {
      return this.mDelegate.isMainThread();
   }

   public void postToMainThread(Runnable var1) {
      this.mDelegate.postToMainThread(var1);
   }
}
