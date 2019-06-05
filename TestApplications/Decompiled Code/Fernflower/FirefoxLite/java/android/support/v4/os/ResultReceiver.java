package android.support.v4.os;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ResultReceiver implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public ResultReceiver createFromParcel(Parcel var1) {
         return new ResultReceiver(var1);
      }

      public ResultReceiver[] newArray(int var1) {
         return new ResultReceiver[var1];
      }
   };
   final Handler mHandler = null;
   final boolean mLocal = false;
   IResultReceiver mReceiver;

   ResultReceiver(Parcel var1) {
      this.mReceiver = IResultReceiver.Stub.asInterface(var1.readStrongBinder());
   }

   public int describeContents() {
      return 0;
   }

   protected void onReceiveResult(int var1, Bundle var2) {
   }

   public void writeToParcel(Parcel var1, int var2) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (this.mReceiver == null) {
               ResultReceiver.MyResultReceiver var3 = new ResultReceiver.MyResultReceiver();
               this.mReceiver = var3;
            }
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            var1.writeStrongBinder(this.mReceiver.asBinder());
            return;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label119;
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

   class MyResultReceiver extends IResultReceiver.Stub {
      public void send(int var1, Bundle var2) {
         if (ResultReceiver.this.mHandler != null) {
            ResultReceiver.this.mHandler.post(ResultReceiver.this.new MyRunnable(var1, var2));
         } else {
            ResultReceiver.this.onReceiveResult(var1, var2);
         }

      }
   }

   class MyRunnable implements Runnable {
      final int mResultCode;
      final Bundle mResultData;

      MyRunnable(int var2, Bundle var3) {
         this.mResultCode = var2;
         this.mResultData = var3;
      }

      public void run() {
         ResultReceiver.this.onReceiveResult(this.mResultCode, this.mResultData);
      }
   }
}
