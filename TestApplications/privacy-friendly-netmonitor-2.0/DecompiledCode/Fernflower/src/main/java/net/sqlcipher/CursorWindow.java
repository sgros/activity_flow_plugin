package net.sqlcipher;

import android.database.CharArrayBuffer;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CursorWindow extends android.database.CursorWindow implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public CursorWindow createFromParcel(Parcel var1) {
         return new CursorWindow(var1, 0);
      }

      public CursorWindow[] newArray(int var1) {
         return new CursorWindow[var1];
      }
   };
   private int mStartPos;
   private long nWindow;

   public CursorWindow(Parcel var1, int var2) {
      super(true);
      IBinder var3 = var1.readStrongBinder();
      this.mStartPos = var1.readInt();
      this.native_init(var3);
   }

   public CursorWindow(boolean var1) {
      super(var1);
      this.mStartPos = 0;
      this.native_init(var1);
   }

   private native boolean allocRow_native();

   private native void close_native();

   private native char[] copyStringToBuffer_native(int var1, int var2, int var3, CharArrayBuffer var4);

   private native void freeLastRow_native();

   private native byte[] getBlob_native(int var1, int var2);

   private native double getDouble_native(int var1, int var2);

   private native long getLong_native(int var1, int var2);

   private native int getNumRows_native();

   private native String getString_native(int var1, int var2);

   private native int getType_native(int var1, int var2);

   private native boolean isBlob_native(int var1, int var2);

   private native boolean isFloat_native(int var1, int var2);

   private native boolean isInteger_native(int var1, int var2);

   private native boolean isNull_native(int var1, int var2);

   private native boolean isString_native(int var1, int var2);

   private native void native_clear();

   private native IBinder native_getBinder();

   private native void native_init(IBinder var1);

   private native void native_init(boolean var1);

   public static CursorWindow newFromParcel(Parcel var0) {
      return (CursorWindow)CREATOR.createFromParcel(var0);
   }

   private native boolean putBlob_native(byte[] var1, int var2, int var3);

   private native boolean putDouble_native(double var1, int var3, int var4);

   private native boolean putLong_native(long var1, int var3, int var4);

   private native boolean putNull_native(int var1, int var2);

   private native boolean putString_native(String var1, int var2, int var3);

   private native boolean setNumColumns_native(int var1);

   public boolean allocRow() {
      this.acquireReference();

      boolean var1;
      try {
         var1 = this.allocRow_native();
      } finally {
         this.releaseReference();
      }

      return var1;
   }

   public void clear() {
      this.acquireReference();

      try {
         this.mStartPos = 0;
         this.native_clear();
      } finally {
         this.releaseReference();
      }

   }

   public void close() {
      this.releaseReference();
   }

   public void copyStringToBuffer(int var1, int var2, CharArrayBuffer var3) {
      if (var3 == null) {
         throw new IllegalArgumentException("CharArrayBuffer should not be null");
      } else {
         if (var3.data == null) {
            var3.data = new char[64];
         }

         this.acquireReference();

         label92: {
            Throwable var10000;
            label99: {
               boolean var10001;
               char[] var4;
               try {
                  var4 = this.copyStringToBuffer_native(var1 - this.mStartPos, var2, var3.data.length, var3);
               } catch (Throwable var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label99;
               }

               if (var4 == null) {
                  break label92;
               }

               label87:
               try {
                  var3.data = var4;
                  break label92;
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label87;
               }
            }

            Throwable var11 = var10000;
            this.releaseReference();
            throw var11;
         }

         this.releaseReference();
      }
   }

   public int describeContents() {
      return 0;
   }

   protected void finalize() {
      if (this.nWindow != 0L) {
         this.close_native();
      }
   }

   public void freeLastRow() {
      this.acquireReference();

      try {
         this.freeLastRow_native();
      } finally {
         this.releaseReference();
      }

   }

   public byte[] getBlob(int var1, int var2) {
      this.acquireReference();

      byte[] var3;
      try {
         var3 = this.getBlob_native(var1 - this.mStartPos, var2);
      } finally {
         this.releaseReference();
      }

      return var3;
   }

   public double getDouble(int var1, int var2) {
      this.acquireReference();

      double var3;
      try {
         var3 = this.getDouble_native(var1 - this.mStartPos, var2);
      } finally {
         this.releaseReference();
      }

      return var3;
   }

   public float getFloat(int var1, int var2) {
      this.acquireReference();
      boolean var8 = false;

      double var3;
      try {
         var8 = true;
         var3 = this.getDouble_native(var1 - this.mStartPos, var2);
         var8 = false;
      } finally {
         if (var8) {
            this.releaseReference();
         }
      }

      float var5 = (float)var3;
      this.releaseReference();
      return var5;
   }

   public int getInt(int var1, int var2) {
      this.acquireReference();
      boolean var7 = false;

      long var3;
      try {
         var7 = true;
         var3 = this.getLong_native(var1 - this.mStartPos, var2);
         var7 = false;
      } finally {
         if (var7) {
            this.releaseReference();
         }
      }

      var1 = (int)var3;
      this.releaseReference();
      return var1;
   }

   public long getLong(int var1, int var2) {
      this.acquireReference();

      long var3;
      try {
         var3 = this.getLong_native(var1 - this.mStartPos, var2);
      } finally {
         this.releaseReference();
      }

      return var3;
   }

   public int getNumRows() {
      this.acquireReference();

      int var1;
      try {
         var1 = this.getNumRows_native();
      } finally {
         this.releaseReference();
      }

      return var1;
   }

   public short getShort(int var1, int var2) {
      this.acquireReference();
      boolean var8 = false;

      long var3;
      try {
         var8 = true;
         var3 = this.getLong_native(var1 - this.mStartPos, var2);
         var8 = false;
      } finally {
         if (var8) {
            this.releaseReference();
         }
      }

      short var5 = (short)((int)var3);
      this.releaseReference();
      return var5;
   }

   public int getStartPosition() {
      return this.mStartPos;
   }

   public String getString(int var1, int var2) {
      this.acquireReference();

      String var3;
      try {
         var3 = this.getString_native(var1 - this.mStartPos, var2);
      } finally {
         this.releaseReference();
      }

      return var3;
   }

   public int getType(int var1, int var2) {
      this.acquireReference();

      try {
         var1 = this.getType_native(var1 - this.mStartPos, var2);
      } finally {
         this.releaseReference();
      }

      return var1;
   }

   public boolean isBlob(int var1, int var2) {
      this.acquireReference();

      boolean var3;
      try {
         var3 = this.isBlob_native(var1 - this.mStartPos, var2);
      } finally {
         this.releaseReference();
      }

      return var3;
   }

   public boolean isFloat(int var1, int var2) {
      this.acquireReference();

      boolean var3;
      try {
         var3 = this.isFloat_native(var1 - this.mStartPos, var2);
      } finally {
         this.releaseReference();
      }

      return var3;
   }

   public boolean isLong(int var1, int var2) {
      this.acquireReference();

      boolean var3;
      try {
         var3 = this.isInteger_native(var1 - this.mStartPos, var2);
      } finally {
         this.releaseReference();
      }

      return var3;
   }

   public boolean isNull(int var1, int var2) {
      this.acquireReference();

      boolean var3;
      try {
         var3 = this.isNull_native(var1 - this.mStartPos, var2);
      } finally {
         this.releaseReference();
      }

      return var3;
   }

   public boolean isString(int var1, int var2) {
      this.acquireReference();

      boolean var3;
      try {
         var3 = this.isString_native(var1 - this.mStartPos, var2);
      } finally {
         this.releaseReference();
      }

      return var3;
   }

   protected void onAllReferencesReleased() {
      this.close_native();
      super.onAllReferencesReleased();
   }

   public boolean putBlob(byte[] var1, int var2, int var3) {
      this.acquireReference();

      boolean var4;
      try {
         var4 = this.putBlob_native(var1, var2 - this.mStartPos, var3);
      } finally {
         this.releaseReference();
      }

      return var4;
   }

   public boolean putDouble(double var1, int var3, int var4) {
      this.acquireReference();

      boolean var5;
      try {
         var5 = this.putDouble_native(var1, var3 - this.mStartPos, var4);
      } finally {
         this.releaseReference();
      }

      return var5;
   }

   public boolean putLong(long var1, int var3, int var4) {
      this.acquireReference();

      boolean var5;
      try {
         var5 = this.putLong_native(var1, var3 - this.mStartPos, var4);
      } finally {
         this.releaseReference();
      }

      return var5;
   }

   public boolean putNull(int var1, int var2) {
      this.acquireReference();

      boolean var3;
      try {
         var3 = this.putNull_native(var1 - this.mStartPos, var2);
      } finally {
         this.releaseReference();
      }

      return var3;
   }

   public boolean putString(String var1, int var2, int var3) {
      this.acquireReference();

      boolean var4;
      try {
         var4 = this.putString_native(var1, var2 - this.mStartPos, var3);
      } finally {
         this.releaseReference();
      }

      return var4;
   }

   public boolean setNumColumns(int var1) {
      this.acquireReference();

      boolean var2;
      try {
         var2 = this.setNumColumns_native(var1);
      } finally {
         this.releaseReference();
      }

      return var2;
   }

   public void setStartPosition(int var1) {
      this.mStartPos = var1;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeStrongBinder(this.native_getBinder());
      var1.writeInt(this.mStartPos);
   }
}
