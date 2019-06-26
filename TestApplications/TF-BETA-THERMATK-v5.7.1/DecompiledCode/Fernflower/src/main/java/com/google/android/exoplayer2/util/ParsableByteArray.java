package com.google.android.exoplayer2.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public final class ParsableByteArray {
   public byte[] data;
   private int limit;
   private int position;

   public ParsableByteArray() {
      this.data = Util.EMPTY_BYTE_ARRAY;
   }

   public ParsableByteArray(int var1) {
      this.data = new byte[var1];
      this.limit = var1;
   }

   public ParsableByteArray(byte[] var1) {
      this.data = var1;
      this.limit = var1.length;
   }

   public ParsableByteArray(byte[] var1, int var2) {
      this.data = var1;
      this.limit = var2;
   }

   public int bytesLeft() {
      return this.limit - this.position;
   }

   public int capacity() {
      return this.data.length;
   }

   public int getPosition() {
      return this.position;
   }

   public int limit() {
      return this.limit;
   }

   public char peekChar() {
      byte[] var1 = this.data;
      int var2 = this.position;
      byte var3 = var1[var2];
      return (char)(var1[var2 + 1] & 255 | (var3 & 255) << 8);
   }

   public int peekUnsignedByte() {
      return this.data[this.position] & 255;
   }

   public void readBytes(ParsableBitArray var1, int var2) {
      this.readBytes(var1.data, 0, var2);
      var1.setPosition(0);
   }

   public void readBytes(ByteBuffer var1, int var2) {
      var1.put(this.data, this.position, var2);
      this.position += var2;
   }

   public void readBytes(byte[] var1, int var2, int var3) {
      System.arraycopy(this.data, this.position, var1, var2, var3);
      this.position += var3;
   }

   public double readDouble() {
      return Double.longBitsToDouble(this.readLong());
   }

   public float readFloat() {
      return Float.intBitsToFloat(this.readInt());
   }

   public int readInt() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      byte var6 = var1[var2];
      int var3 = this.position++;
      byte var7 = var1[var3];
      int var4 = this.position++;
      byte var8 = var1[var4];
      int var5 = this.position++;
      return var1[var5] & 255 | (var6 & 255) << 24 | (var7 & 255) << 16 | (var8 & 255) << 8;
   }

   public int readInt24() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      byte var5 = var1[var2];
      int var3 = this.position++;
      byte var4 = var1[var3];
      var3 = this.position++;
      return var1[var3] & 255 | (var5 & 255) << 24 >> 8 | (var4 & 255) << 8;
   }

   public String readLine() {
      if (this.bytesLeft() == 0) {
         return null;
      } else {
         int var1;
         for(var1 = this.position; var1 < this.limit && !Util.isLinebreak(this.data[var1]); ++var1) {
         }

         int var2 = this.position;
         byte[] var3;
         if (var1 - var2 >= 3) {
            var3 = this.data;
            if (var3[var2] == -17 && var3[var2 + 1] == -69 && var3[var2 + 2] == -65) {
               this.position = var2 + 3;
            }
         }

         var3 = this.data;
         var2 = this.position;
         String var5 = Util.fromUtf8Bytes(var3, var2, var1 - var2);
         this.position = var1;
         var1 = this.position;
         var2 = this.limit;
         if (var1 == var2) {
            return var5;
         } else {
            if (this.data[var1] == 13) {
               this.position = var1 + 1;
               if (this.position == var2) {
                  return var5;
               }
            }

            byte[] var4 = this.data;
            var1 = this.position;
            if (var4[var1] == 10) {
               this.position = var1 + 1;
            }

            return var5;
         }
      }
   }

   public int readLittleEndianInt() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      byte var6 = var1[var2];
      int var3 = this.position++;
      byte var7 = var1[var3];
      int var4 = this.position++;
      byte var8 = var1[var4];
      int var5 = this.position++;
      return (var1[var5] & 255) << 24 | var6 & 255 | (var7 & 255) << 8 | (var8 & 255) << 16;
   }

   public int readLittleEndianInt24() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      byte var5 = var1[var2];
      int var3 = this.position++;
      byte var4 = var1[var3];
      var3 = this.position++;
      return (var1[var3] & 255) << 16 | var5 & 255 | (var4 & 255) << 8;
   }

   public long readLittleEndianLong() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      long var3 = (long)var1[var2];
      var2 = this.position++;
      long var5 = (long)var1[var2];
      var2 = this.position++;
      long var7 = (long)var1[var2];
      var2 = this.position++;
      long var9 = (long)var1[var2];
      var2 = this.position++;
      long var11 = (long)var1[var2];
      var2 = this.position++;
      long var13 = (long)var1[var2];
      var2 = this.position++;
      long var15 = (long)var1[var2];
      var2 = this.position++;
      return var3 & 255L | (var5 & 255L) << 8 | (var7 & 255L) << 16 | (var9 & 255L) << 24 | (var11 & 255L) << 32 | (var13 & 255L) << 40 | (var15 & 255L) << 48 | (255L & (long)var1[var2]) << 56;
   }

   public short readLittleEndianShort() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      byte var4 = var1[var2];
      int var3 = this.position++;
      return (short)((var1[var3] & 255) << 8 | var4 & 255);
   }

   public long readLittleEndianUnsignedInt() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      long var3 = (long)var1[var2];
      var2 = this.position++;
      long var5 = (long)var1[var2];
      var2 = this.position++;
      long var7 = (long)var1[var2];
      var2 = this.position++;
      return var3 & 255L | (var5 & 255L) << 8 | (var7 & 255L) << 16 | (255L & (long)var1[var2]) << 24;
   }

   public int readLittleEndianUnsignedInt24() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      byte var5 = var1[var2];
      int var3 = this.position++;
      byte var6 = var1[var3];
      int var4 = this.position++;
      return (var1[var4] & 255) << 16 | var5 & 255 | (var6 & 255) << 8;
   }

   public int readLittleEndianUnsignedIntToInt() {
      int var1 = this.readLittleEndianInt();
      if (var1 >= 0) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Top bit not zero: ");
         var2.append(var1);
         throw new IllegalStateException(var2.toString());
      }
   }

   public int readLittleEndianUnsignedShort() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      byte var4 = var1[var2];
      int var3 = this.position++;
      return (var1[var3] & 255) << 8 | var4 & 255;
   }

   public long readLong() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      long var3 = (long)var1[var2];
      var2 = this.position++;
      long var5 = (long)var1[var2];
      var2 = this.position++;
      long var7 = (long)var1[var2];
      var2 = this.position++;
      long var9 = (long)var1[var2];
      var2 = this.position++;
      long var11 = (long)var1[var2];
      var2 = this.position++;
      long var13 = (long)var1[var2];
      var2 = this.position++;
      long var15 = (long)var1[var2];
      var2 = this.position++;
      return (var3 & 255L) << 56 | (var5 & 255L) << 48 | (var7 & 255L) << 40 | (var9 & 255L) << 32 | (var11 & 255L) << 24 | (var13 & 255L) << 16 | (var15 & 255L) << 8 | 255L & (long)var1[var2];
   }

   public String readNullTerminatedString() {
      if (this.bytesLeft() == 0) {
         return null;
      } else {
         int var1;
         for(var1 = this.position; var1 < this.limit && this.data[var1] != 0; ++var1) {
         }

         byte[] var2 = this.data;
         int var3 = this.position;
         String var4 = Util.fromUtf8Bytes(var2, var3, var1 - var3);
         this.position = var1;
         var1 = this.position;
         if (var1 < this.limit) {
            this.position = var1 + 1;
         }

         return var4;
      }
   }

   public String readNullTerminatedString(int var1) {
      if (var1 == 0) {
         return "";
      } else {
         int var2 = this.position + var1 - 1;
         if (var2 < this.limit && this.data[var2] == 0) {
            var2 = var1 - 1;
         } else {
            var2 = var1;
         }

         String var3 = Util.fromUtf8Bytes(this.data, this.position, var2);
         this.position += var1;
         return var3;
      }
   }

   public short readShort() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      byte var3 = var1[var2];
      var2 = this.position++;
      return (short)(var1[var2] & 255 | (var3 & 255) << 8);
   }

   public String readString(int var1) {
      return this.readString(var1, Charset.forName("UTF-8"));
   }

   public String readString(int var1, Charset var2) {
      String var3 = new String(this.data, this.position, var1, var2);
      this.position += var1;
      return var3;
   }

   public int readSynchSafeInt() {
      return this.readUnsignedByte() << 21 | this.readUnsignedByte() << 14 | this.readUnsignedByte() << 7 | this.readUnsignedByte();
   }

   public int readUnsignedByte() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      return var1[var2] & 255;
   }

   public int readUnsignedFixedPoint1616() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      byte var4 = var1[var2];
      int var3 = this.position++;
      byte var5 = var1[var3];
      this.position += 2;
      return var5 & 255 | (var4 & 255) << 8;
   }

   public long readUnsignedInt() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      long var3 = (long)var1[var2];
      var2 = this.position++;
      long var5 = (long)var1[var2];
      var2 = this.position++;
      long var7 = (long)var1[var2];
      var2 = this.position++;
      return (var3 & 255L) << 24 | (var5 & 255L) << 16 | (var7 & 255L) << 8 | 255L & (long)var1[var2];
   }

   public int readUnsignedInt24() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      byte var5 = var1[var2];
      int var3 = this.position++;
      byte var4 = var1[var3];
      var3 = this.position++;
      return var1[var3] & 255 | (var5 & 255) << 16 | (var4 & 255) << 8;
   }

   public int readUnsignedIntToInt() {
      int var1 = this.readInt();
      if (var1 >= 0) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Top bit not zero: ");
         var2.append(var1);
         throw new IllegalStateException(var2.toString());
      }
   }

   public long readUnsignedLongToLong() {
      long var1 = this.readLong();
      if (var1 >= 0L) {
         return var1;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Top bit not zero: ");
         var3.append(var1);
         throw new IllegalStateException(var3.toString());
      }
   }

   public int readUnsignedShort() {
      byte[] var1 = this.data;
      int var2 = this.position++;
      byte var4 = var1[var2];
      int var3 = this.position++;
      return var1[var3] & 255 | (var4 & 255) << 8;
   }

   public long readUtf8EncodedLong() {
      long var1 = (long)this.data[this.position];
      int var3 = 7;

      int var4;
      while(true) {
         var4 = 1;
         if (var3 >= 0) {
            int var5 = 1 << var3;
            if (((long)var5 & var1) != 0L) {
               --var3;
               continue;
            }

            if (var3 < 6) {
               var1 &= (long)(var5 - 1);
               var3 = 7 - var3;
               break;
            }

            if (var3 == 7) {
               var3 = 1;
               break;
            }
         }

         var3 = 0;
         break;
      }

      StringBuilder var6;
      if (var3 != 0) {
         while(var4 < var3) {
            byte var7 = this.data[this.position + var4];
            if ((var7 & 192) != 128) {
               var6 = new StringBuilder();
               var6.append("Invalid UTF-8 sequence continuation byte: ");
               var6.append(var1);
               throw new NumberFormatException(var6.toString());
            }

            var1 = var1 << 6 | (long)(var7 & 63);
            ++var4;
         }

         this.position += var3;
         return var1;
      } else {
         var6 = new StringBuilder();
         var6.append("Invalid UTF-8 sequence first byte: ");
         var6.append(var1);
         throw new NumberFormatException(var6.toString());
      }
   }

   public void reset() {
      this.position = 0;
      this.limit = 0;
   }

   public void reset(int var1) {
      byte[] var2;
      if (this.capacity() < var1) {
         var2 = new byte[var1];
      } else {
         var2 = this.data;
      }

      this.reset(var2, var1);
   }

   public void reset(byte[] var1) {
      this.reset(var1, var1.length);
   }

   public void reset(byte[] var1, int var2) {
      this.data = var1;
      this.limit = var2;
      this.position = 0;
   }

   public void setLimit(int var1) {
      boolean var2;
      if (var1 >= 0 && var1 <= this.data.length) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkArgument(var2);
      this.limit = var1;
   }

   public void setPosition(int var1) {
      boolean var2;
      if (var1 >= 0 && var1 <= this.limit) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkArgument(var2);
      this.position = var1;
   }

   public void skipBytes(int var1) {
      this.setPosition(this.position + var1);
   }
}
