package android.support.v7.recyclerview.extensions;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class AsyncListDiffer {
   private static final Executor sMainThreadExecutor = new AsyncListDiffer.MainThreadExecutor();
   final AsyncDifferConfig mConfig;
   private List mList;
   final Executor mMainThreadExecutor;
   int mMaxScheduledGeneration;
   private List mReadOnlyList = Collections.emptyList();
   private final ListUpdateCallback mUpdateCallback;

   public AsyncListDiffer(ListUpdateCallback var1, AsyncDifferConfig var2) {
      this.mUpdateCallback = var1;
      this.mConfig = var2;
      if (var2.getMainThreadExecutor() != null) {
         this.mMainThreadExecutor = var2.getMainThreadExecutor();
      } else {
         this.mMainThreadExecutor = sMainThreadExecutor;
      }

   }

   public List getCurrentList() {
      return this.mReadOnlyList;
   }

   void latchList(List var1, DiffUtil.DiffResult var2) {
      this.mList = var1;
      this.mReadOnlyList = Collections.unmodifiableList(var1);
      var2.dispatchUpdatesTo(this.mUpdateCallback);
   }

   public void submitList(final List var1) {
      final int var2 = this.mMaxScheduledGeneration + 1;
      this.mMaxScheduledGeneration = var2;
      if (var1 != this.mList) {
         if (var1 == null) {
            var2 = this.mList.size();
            this.mList = null;
            this.mReadOnlyList = Collections.emptyList();
            this.mUpdateCallback.onRemoved(0, var2);
         } else if (this.mList == null) {
            this.mList = var1;
            this.mReadOnlyList = Collections.unmodifiableList(var1);
            this.mUpdateCallback.onInserted(0, var1.size());
         } else {
            final List var3 = this.mList;
            this.mConfig.getBackgroundThreadExecutor().execute(new Runnable() {
               public void run() {
                  final DiffUtil.DiffResult var1x = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                     public boolean areContentsTheSame(int var1x, int var2x) {
                        Object var3x = var3.get(var1x);
                        Object var4 = var1.get(var2x);
                        if (var3x != null && var4 != null) {
                           return AsyncListDiffer.this.mConfig.getDiffCallback().areContentsTheSame(var3x, var4);
                        } else if (var3x == null && var4 == null) {
                           return true;
                        } else {
                           throw new AssertionError();
                        }
                     }

                     public boolean areItemsTheSame(int var1x, int var2x) {
                        Object var3x = var3.get(var1x);
                        Object var4 = var1.get(var2x);
                        if (var3x != null && var4 != null) {
                           return AsyncListDiffer.this.mConfig.getDiffCallback().areItemsTheSame(var3x, var4);
                        } else {
                           boolean var5;
                           if (var3x == null && var4 == null) {
                              var5 = true;
                           } else {
                              var5 = false;
                           }

                           return var5;
                        }
                     }

                     public Object getChangePayload(int var1x, int var2x) {
                        Object var3x = var3.get(var1x);
                        Object var4 = var1.get(var2x);
                        if (var3x != null && var4 != null) {
                           return AsyncListDiffer.this.mConfig.getDiffCallback().getChangePayload(var3x, var4);
                        } else {
                           throw new AssertionError();
                        }
                     }

                     public int getNewListSize() {
                        return var1.size();
                     }

                     public int getOldListSize() {
                        return var3.size();
                     }
                  });
                  AsyncListDiffer.this.mMainThreadExecutor.execute(new Runnable() {
                     public void run() {
                        if (AsyncListDiffer.this.mMaxScheduledGeneration == var2) {
                           AsyncListDiffer.this.latchList(var1, var1x);
                        }

                     }
                  });
               }
            });
         }
      }
   }

   private static class MainThreadExecutor implements Executor {
      final Handler mHandler = new Handler(Looper.getMainLooper());

      MainThreadExecutor() {
      }

      public void execute(Runnable var1) {
         this.mHandler.post(var1);
      }
   }
}
