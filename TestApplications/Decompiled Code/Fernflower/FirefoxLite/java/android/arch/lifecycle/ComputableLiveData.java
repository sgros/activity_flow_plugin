package android.arch.lifecycle;

import android.arch.core.executor.ArchTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ComputableLiveData {
   private AtomicBoolean mComputing;
   private final Executor mExecutor;
   private AtomicBoolean mInvalid;
   final Runnable mInvalidationRunnable;
   private final LiveData mLiveData;
   final Runnable mRefreshRunnable;

   public ComputableLiveData() {
      this(ArchTaskExecutor.getIOThreadExecutor());
   }

   public ComputableLiveData(Executor var1) {
      this.mInvalid = new AtomicBoolean(true);
      this.mComputing = new AtomicBoolean(false);
      this.mRefreshRunnable = new Runnable() {
         public void run() {
            boolean var2;
            label134:
            do {
               if (!ComputableLiveData.this.mComputing.compareAndSet(false, true)) {
                  var2 = false;
               } else {
                  Object var1 = null;
                  var2 = false;

                  Throwable var10000;
                  while(true) {
                     label137: {
                        boolean var10001;
                        try {
                           if (ComputableLiveData.this.mInvalid.compareAndSet(true, false)) {
                              var1 = ComputableLiveData.this.compute();
                              break label137;
                           }
                        } catch (Throwable var8) {
                           var10000 = var8;
                           var10001 = false;
                           break;
                        }

                        if (var2) {
                           try {
                              ComputableLiveData.this.mLiveData.postValue(var1);
                           } catch (Throwable var7) {
                              var10000 = var7;
                              var10001 = false;
                              break;
                           }
                        }

                        ComputableLiveData.this.mComputing.set(false);
                        continue label134;
                     }

                     var2 = true;
                  }

                  Throwable var9 = var10000;
                  ComputableLiveData.this.mComputing.set(false);
                  throw var9;
               }
            } while(var2 && ComputableLiveData.this.mInvalid.get());

         }
      };
      this.mInvalidationRunnable = new Runnable() {
         public void run() {
            boolean var1 = ComputableLiveData.this.mLiveData.hasActiveObservers();
            if (ComputableLiveData.this.mInvalid.compareAndSet(false, true) && var1) {
               ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
            }

         }
      };
      this.mExecutor = var1;
      this.mLiveData = new LiveData() {
         protected void onActive() {
            ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
         }
      };
   }

   protected abstract Object compute();

   public LiveData getLiveData() {
      return this.mLiveData;
   }

   public void invalidate() {
      ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable);
   }
}
