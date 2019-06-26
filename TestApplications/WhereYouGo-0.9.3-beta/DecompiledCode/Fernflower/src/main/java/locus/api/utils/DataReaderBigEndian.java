package locus.api.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import locus.api.objects.Storable;

public class DataReaderBigEndian {
   private static final String TAG = DataReaderBigEndian.class.getSimpleName();
   private byte[] mBuffer;
   private int mPosition;

   public DataReaderBigEndian(byte[] var1) throws IOException {
      if (var1 == null) {
         throw new IOException("Invalid parameter");
      } else {
         this.mPosition = 0;
         this.mBuffer = var1;
      }
   }

   private void checkPosition(int var1) {
      this.mPosition += var1;
      if (this.mPosition > this.mBuffer.length) {
         throw new ArrayIndexOutOfBoundsException("Invalid position for data load. Current:" + this.mPosition + ", " + "length:" + this.mBuffer.length + ", " + "increment:" + var1);
      }
   }

   public int available() {
      return this.mBuffer.length - this.mPosition;
   }

   public long length() {
      return (long)this.mBuffer.length;
   }

   public boolean readBoolean() {
      boolean var1 = true;
      this.checkPosition(1);
      if (this.mBuffer[this.mPosition - 1] == 0) {
         var1 = false;
      }

      return var1;
   }

   public void readBytes(byte[] var1) {
      this.checkPosition(var1.length);
      System.arraycopy(this.mBuffer, this.mPosition - var1.length, var1, 0, var1.length);
   }

   public byte[] readBytes(int var1) {
      this.checkPosition(var1);
      byte[] var2 = new byte[var1];
      System.arraycopy(this.mBuffer, this.mPosition - var1, var2, 0, var1);
      return var2;
   }

   public final double readDouble() {
      return Double.longBitsToDouble(this.readLong());
   }

   public final float readFloat() {
      return Float.intBitsToFloat(this.readInt());
   }

   public int readInt() {
      this.checkPosition(4);
      return this.mBuffer[this.mPosition - 4] << 24 | (this.mBuffer[this.mPosition - 3] & 255) << 16 | (this.mBuffer[this.mPosition - 2] & 255) << 8 | this.mBuffer[this.mPosition - 1] & 255;
   }

   public List readListStorable(Class var1) throws IOException {
      ArrayList var2 = new ArrayList();
      int var3 = this.readInt();
      if (var3 != 0) {
         for(int var4 = 0; var4 < var3; ++var4) {
            try {
               Storable var5 = (Storable)var1.newInstance();
               var5.read(this);
               var2.add(var5);
            } catch (InstantiationException var6) {
               Logger.logE(TAG, "readList(" + var1 + ")", var6);
            } catch (IllegalAccessException var7) {
               Logger.logE(TAG, "readList(" + var1 + ")", var7);
            }
         }
      }

      return var2;
   }

   public List readListString() throws IOException {
      ArrayList var1 = new ArrayList();
      int var2 = this.readInt();
      if (var2 != 0) {
         for(int var3 = 0; var3 < var2; ++var3) {
            var1.add(this.readString());
         }
      }

      return var1;
   }

   public long readLong() {
      this.checkPosition(8);
      return ((long)this.mBuffer[this.mPosition - 8] & 255L) << 56 | ((long)this.mBuffer[this.mPosition - 7] & 255L) << 48 | ((long)this.mBuffer[this.mPosition - 6] & 255L) << 40 | ((long)this.mBuffer[this.mPosition - 5] & 255L) << 32 | ((long)this.mBuffer[this.mPosition - 4] & 255L) << 24 | ((long)this.mBuffer[this.mPosition - 3] & 255L) << 16 | ((long)this.mBuffer[this.mPosition - 2] & 255L) << 8 | (long)this.mBuffer[this.mPosition - 1] & 255L;
   }

   public short readShort() {
      this.checkPosition(2);
      return (short)((this.mBuffer[this.mPosition - 2] & 255) << 8 | this.mBuffer[this.mPosition - 1] & 255);
   }

   public Storable readStorable(Class var1) throws InstantiationException, IllegalAccessException, IOException {
      return Storable.read(var1, this);
   }

   public String readString() throws IOException {
      int var1 = this.readInt();
      String var2;
      if (var1 == 0) {
         var2 = "";
      } else {
         this.checkPosition(var1);
         var2 = new String(this.mBuffer, this.mPosition - var1, var1, "UTF-8");
      }

      return var2;
   }

   @Deprecated
   public String readStringDis() throws IOException {
      short var1 = this.readShort();
      String var2;
      if (var1 == 0) {
         var2 = "";
      } else {
         this.checkPosition(var1);
         var2 = new String(this.mBuffer, this.mPosition - var1, var1, "UTF-8");
      }

      return var2;
   }

   public void seek(int var1) {
      this.mPosition = var1;
   }
}
