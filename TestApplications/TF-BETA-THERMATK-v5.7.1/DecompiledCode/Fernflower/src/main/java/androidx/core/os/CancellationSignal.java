package androidx.core.os;

import android.os.Build.VERSION;

public final class CancellationSignal {
   private boolean mCancelInProgress;
   private Object mCancellationSignalObj;
   private boolean mIsCanceled;
   private CancellationSignal.OnCancelListener mOnCancelListener;

   public void cancel() {
      // $FF: Couldn't be decompiled
   }

   public Object getCancellationSignalObject() {
      if (VERSION.SDK_INT < 16) {
         return null;
      } else {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (this.mCancellationSignalObj == null) {
                  android.os.CancellationSignal var1 = new android.os.CancellationSignal();
                  this.mCancellationSignalObj = var1;
                  if (this.mIsCanceled) {
                     ((android.os.CancellationSignal)this.mCancellationSignalObj).cancel();
                  }
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               Object var15 = this.mCancellationSignalObj;
               return var15;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var14 = var10000;

            try {
               throw var14;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public interface OnCancelListener {
      void onCancel();
   }
}
