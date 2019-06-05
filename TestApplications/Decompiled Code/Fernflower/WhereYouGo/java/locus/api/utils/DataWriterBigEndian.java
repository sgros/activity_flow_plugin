package locus.api.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import locus.api.objects.Storable;

public class DataWriterBigEndian {
   private byte[] mBuf;
   private int mCount;
   private int mCurrentPos;
   private int mSavedPos;
   private byte[] mWriteBuffer;

   public DataWriterBigEndian() {
      this(256);
   }

   public DataWriterBigEndian(int var1) {
      this.mWriteBuffer = new byte[8];
      if (var1 < 0) {
         throw new IllegalArgumentException("Negative initial size: " + var1);
      } else {
         this.mBuf = new byte[var1];
         this.reset();
      }
   }

   private void ensureCapacity(int var1) {
      if (var1 - this.mBuf.length > 0) {
         this.grow(var1);
      }

   }

   private void grow(int var1) {
      int var2 = this.mBuf.length << 1;
      int var3 = var2;
      if (var2 - var1 < 0) {
         var3 = var1;
      }

      var2 = var3;
      if (var3 < 0) {
         if (var1 < 0) {
            throw new OutOfMemoryError();
         }

         var2 = Integer.MAX_VALUE;
      }

      this.mBuf = Utils.copyOf(this.mBuf, var2);
   }

   private void setNewPositions(int var1) {
      if (this.mCurrentPos + var1 < this.mCount) {
         this.mCurrentPos += var1;
      } else {
         this.mCurrentPos += var1;
         this.mCount = this.mCurrentPos;
      }

   }

   public void moveTo(int var1) {
      if (var1 >= 0 && var1 <= this.mCount) {
         this.mCurrentPos = var1;
      } else {
         throw new IllegalArgumentException("Invalid move index:" + var1 + ", count:" + this.mCount);
      }
   }

   public void reset() {
      synchronized(this){}

      try {
         this.mCount = 0;
         this.mCurrentPos = 0;
         this.mSavedPos = 0;
      } finally {
         ;
      }

   }

   public void restorePosition() {
      this.mCurrentPos = this.mSavedPos;
   }

   public int size() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.mCount;
      } finally {
         ;
      }

      return var1;
   }

   public void storePosition() {
      this.mSavedPos = this.mCurrentPos;
   }

   public byte[] toByteArray() {
      synchronized(this){}

      byte[] var1;
      try {
         var1 = Utils.copyOf(this.mBuf, this.mCount);
      } finally {
         ;
      }

      return var1;
   }

   public void write(int var1) {
      synchronized(this){}

      try {
         this.ensureCapacity(this.mCurrentPos + 1);
         this.mBuf[this.mCurrentPos] = (byte)((byte)var1);
         this.setNewPositions(1);
      } finally {
         ;
      }

   }

   public void write(byte[] var1) {
      synchronized(this){}

      try {
         this.write(var1, 0, var1.length);
      } finally {
         ;
      }

   }

   public void write(byte[] var1, int var2, int var3) {
      Throwable var10000;
      label183: {
         boolean var10001;
         label186: {
            synchronized(this){}
            if (var2 >= 0) {
               label185: {
                  try {
                     if (var2 > var1.length) {
                        break label185;
                     }
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label183;
                  }

                  if (var3 >= 0) {
                     try {
                        if (var2 + var3 - var1.length <= 0) {
                           break label186;
                        }
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label183;
                     }
                  }
               }
            }

            try {
               IndexOutOfBoundsException var25 = new IndexOutOfBoundsException();
               throw var25;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label183;
            }
         }

         try {
            this.ensureCapacity(this.mCurrentPos + var3);
            System.arraycopy(var1, var2, this.mBuf, this.mCurrentPos, var3);
            this.setNewPositions(var3);
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label183;
         }

         return;
      }

      Throwable var24 = var10000;
      throw var24;
   }

   public final void writeBoolean(boolean var1) throws IOException {
      byte var2;
      if (var1) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      this.write(var2);
   }

   public final void writeDouble(double var1) throws IOException {
      this.writeLong(Double.doubleToLongBits(var1));
   }

   public final void writeFloat(float var1) throws IOException {
      this.writeInt(Float.floatToIntBits(var1));
   }

   public final void writeInt(int var1) throws IOException {
      this.mWriteBuffer[0] = (byte)((byte)(var1 >>> 24 & 255));
      this.mWriteBuffer[1] = (byte)((byte)(var1 >>> 16 & 255));
      this.mWriteBuffer[2] = (byte)((byte)(var1 >>> 8 & 255));
      this.mWriteBuffer[3] = (byte)((byte)(var1 >>> 0 & 255));
      this.write(this.mWriteBuffer, 0, 4);
   }

   public void writeListStorable(List var1) throws IOException {
      int var2;
      if (var1 == null) {
         var2 = 0;
      } else {
         var2 = var1.size();
      }

      this.writeInt(var2);
      if (var2 != 0) {
         var2 = 0;

         for(int var3 = var1.size(); var2 < var3; ++var2) {
            ((Storable)var1.get(var2)).write(this);
         }
      }

   }

   public void writeListString(List var1) throws IOException {
      if (var1 != null && var1.size() != 0) {
         this.writeInt(var1.size());
         int var2 = 0;

         for(int var3 = var1.size(); var2 < var3; ++var2) {
            this.writeString((String)var1.get(var2));
         }
      } else {
         this.writeInt(0);
      }

   }

   public final void writeLong(long var1) throws IOException {
      this.mWriteBuffer[0] = (byte)((byte)((int)(var1 >>> 56)));
      this.mWriteBuffer[1] = (byte)((byte)((int)(var1 >>> 48)));
      this.mWriteBuffer[2] = (byte)((byte)((int)(var1 >>> 40)));
      this.mWriteBuffer[3] = (byte)((byte)((int)(var1 >>> 32)));
      this.mWriteBuffer[4] = (byte)((byte)((int)(var1 >>> 24)));
      this.mWriteBuffer[5] = (byte)((byte)((int)(var1 >>> 16)));
      this.mWriteBuffer[6] = (byte)((byte)((int)(var1 >>> 8)));
      this.mWriteBuffer[7] = (byte)((byte)((int)(var1 >>> 0)));
      this.write(this.mWriteBuffer, 0, 8);
   }

   public final void writeShort(int var1) throws IOException {
      this.write(var1 >>> 8 & 255);
      this.write(var1 >>> 0 & 255);
   }

   public final void writeStorable(Storable var1) throws IOException {
      var1.write(this);
   }

   public final void writeString(String var1) throws IOException {
      if (var1 != null && var1.length() != 0) {
         byte[] var2 = var1.getBytes("UTF-8");
         this.writeInt(var2.length);
         this.write(var2, 0, var2.length);
      } else {
         this.writeInt(0);
      }

   }

   @Deprecated
   public final void writeStringDos(String var1) throws IOException {
      if (var1 != null && var1.length() != 0) {
         byte[] var2 = var1.getBytes("UTF-8");
         this.writeShort(var2.length);
         this.write(var2, 0, var2.length);
      } else {
         this.writeShort(0);
      }

   }

   public void writeTo(OutputStream var1) throws IOException {
      synchronized(this){}

      try {
         var1.write(this.mBuf, 0, this.mCount);
      } finally {
         ;
      }

   }
}
