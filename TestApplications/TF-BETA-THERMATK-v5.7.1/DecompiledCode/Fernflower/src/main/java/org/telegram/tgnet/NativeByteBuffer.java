package org.telegram.tgnet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

public class NativeByteBuffer extends AbstractSerializedData {
   private static final ThreadLocal addressWrapper = new ThreadLocal() {
      protected NativeByteBuffer initialValue() {
         return new NativeByteBuffer(0, true);
      }
   };
   protected long address;
   public ByteBuffer buffer;
   private boolean justCalc;
   private int len;
   public boolean reused;

   public NativeByteBuffer(int var1) throws Exception {
      this.reused = true;
      if (var1 >= 0) {
         this.address = native_getFreeBuffer(var1);
         long var2 = this.address;
         if (var2 != 0L) {
            this.buffer = native_getJavaByteBuffer(var2);
            this.buffer.position(0);
            this.buffer.limit(var1);
            this.buffer.order(ByteOrder.LITTLE_ENDIAN);
         }

      } else {
         throw new Exception("invalid NativeByteBuffer size");
      }
   }

   private NativeByteBuffer(int var1, boolean var2) {
      this.reused = true;
   }

   // $FF: synthetic method
   NativeByteBuffer(int var1, boolean var2, Object var3) {
      this(var1, var2);
   }

   public NativeByteBuffer(boolean var1) {
      this.reused = true;
      this.justCalc = var1;
   }

   public static native long native_getFreeBuffer(int var0);

   public static native ByteBuffer native_getJavaByteBuffer(long var0);

   public static native int native_limit(long var0);

   public static native int native_position(long var0);

   public static native void native_reuse(long var0);

   public static NativeByteBuffer wrap(long var0) {
      NativeByteBuffer var2 = (NativeByteBuffer)addressWrapper.get();
      if (var0 != 0L) {
         if (!var2.reused && BuildVars.LOGS_ENABLED) {
            FileLog.e("forgot to reuse?");
         }

         var2.address = var0;
         var2.reused = false;
         var2.buffer = native_getJavaByteBuffer(var0);
         var2.buffer.limit(native_limit(var0));
         int var3 = native_position(var0);
         if (var3 <= var2.buffer.limit()) {
            var2.buffer.position(var3);
         }

         var2.buffer.order(ByteOrder.LITTLE_ENDIAN);
      }

      return var2;
   }

   public int capacity() {
      return this.buffer.capacity();
   }

   public void compact() {
      this.buffer.compact();
   }

   public int getIntFromByte(byte var1) {
      if (var1 < 0) {
         var1 += 256;
      }

      return var1;
   }

   public int getPosition() {
      return this.buffer.position();
   }

   public boolean hasRemaining() {
      return this.buffer.hasRemaining();
   }

   public int length() {
      return !this.justCalc ? this.buffer.position() : this.len;
   }

   public int limit() {
      return this.buffer.limit();
   }

   public void limit(int var1) {
      this.buffer.limit(var1);
   }

   public int position() {
      return this.buffer.position();
   }

   public void position(int var1) {
      this.buffer.position(var1);
   }

   public void put(ByteBuffer var1) {
      this.buffer.put(var1);
   }

   public boolean readBool(boolean var1) {
      int var2 = this.readInt32(var1);
      if (var2 == -1720552011) {
         return true;
      } else if (var2 == -1132882121) {
         return false;
      } else if (!var1) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("Not bool value!");
         }

         return false;
      } else {
         throw new RuntimeException("Not bool value!");
      }
   }

   public byte[] readByteArray(boolean var1) {
      Exception var10000;
      label53: {
         boolean var10001;
         int var2;
         try {
            var2 = this.getIntFromByte(this.buffer.get());
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
            break label53;
         }

         int var3;
         if (var2 >= 254) {
            try {
               var2 = this.getIntFromByte(this.buffer.get()) | this.getIntFromByte(this.buffer.get()) << 8 | this.getIntFromByte(this.buffer.get()) << 16;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label53;
            }

            var3 = 4;
         } else {
            var3 = 1;
         }

         byte[] var4;
         try {
            var4 = new byte[var2];
            this.buffer.get(var4);
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
            break label53;
         }

         while(true) {
            if ((var2 + var3) % 4 == 0) {
               return var4;
            }

            try {
               this.buffer.get();
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
               break;
            }

            ++var3;
         }
      }

      Exception var9 = var10000;
      if (!var1) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("read byte array error");
         }

         return new byte[0];
      } else {
         throw new RuntimeException("read byte array error", var9);
      }
   }

   public NativeByteBuffer readByteBuffer(boolean var1) {
      Exception var10000;
      label53: {
         int var2;
         boolean var10001;
         try {
            var2 = this.getIntFromByte(this.buffer.get());
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label53;
         }

         int var3;
         if (var2 >= 254) {
            try {
               var2 = this.getIntFromByte(this.buffer.get()) | this.getIntFromByte(this.buffer.get()) << 8 | this.getIntFromByte(this.buffer.get()) << 16;
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label53;
            }

            var3 = 4;
         } else {
            var3 = 1;
         }

         NativeByteBuffer var4;
         try {
            var4 = new NativeByteBuffer(var2);
            int var5 = this.buffer.limit();
            this.buffer.limit(this.buffer.position() + var2);
            var4.buffer.put(this.buffer);
            this.buffer.limit(var5);
            var4.buffer.position(0);
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
            break label53;
         }

         while(true) {
            if ((var2 + var3) % 4 == 0) {
               return var4;
            }

            try {
               this.buffer.get();
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break;
            }

            ++var3;
         }
      }

      Exception var10 = var10000;
      if (!var1) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("read byte array error");
         }

         return null;
      } else {
         throw new RuntimeException("read byte array error", var10);
      }
   }

   public void readBytes(byte[] var1, int var2, int var3, boolean var4) {
      try {
         this.buffer.get(var1, var2, var3);
      } catch (Exception var5) {
         if (var4) {
            throw new RuntimeException("read raw error", var5);
         }

         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("read raw error");
         }
      }

   }

   public void readBytes(byte[] var1, boolean var2) {
      try {
         this.buffer.get(var1);
      } catch (Exception var3) {
         if (var2) {
            throw new RuntimeException("read raw error", var3);
         }

         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("read raw error");
         }
      }

   }

   public byte[] readData(int var1, boolean var2) {
      byte[] var3 = new byte[var1];
      this.readBytes(var3, var2);
      return var3;
   }

   public double readDouble(boolean var1) {
      try {
         double var2 = Double.longBitsToDouble(this.readInt64(var1));
         return var2;
      } catch (Exception var5) {
         if (!var1) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.e("read double error");
            }

            return 0.0D;
         } else {
            throw new RuntimeException("read double error", var5);
         }
      }
   }

   public int readInt32(boolean var1) {
      try {
         int var2 = this.buffer.getInt();
         return var2;
      } catch (Exception var4) {
         if (!var1) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.e("read int32 error");
            }

            return 0;
         } else {
            throw new RuntimeException("read int32 error", var4);
         }
      }
   }

   public long readInt64(boolean var1) {
      try {
         long var2 = this.buffer.getLong();
         return var2;
      } catch (Exception var5) {
         if (!var1) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.e("read int64 error");
            }

            return 0L;
         } else {
            throw new RuntimeException("read int64 error", var5);
         }
      }
   }

   public String readString(boolean var1) {
      int var2 = this.getPosition();

      Exception var10000;
      label60: {
         int var3;
         boolean var10001;
         try {
            var3 = this.getIntFromByte(this.buffer.get());
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break label60;
         }

         int var4;
         if (var3 >= 254) {
            try {
               var3 = this.getIntFromByte(this.buffer.get()) | this.getIntFromByte(this.buffer.get()) << 8 | this.getIntFromByte(this.buffer.get()) << 16;
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label60;
            }

            var4 = 4;
         } else {
            var4 = 1;
         }

         byte[] var5;
         try {
            var5 = new byte[var3];
            this.buffer.get(var5);
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
            break label60;
         }

         for(; (var3 + var4) % 4 != 0; ++var4) {
            try {
               this.buffer.get();
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label60;
            }
         }

         try {
            String var12 = new String(var5, "UTF-8");
            return var12;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var11 = var10000;
      if (!var1) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("read string error");
         }

         this.position(var2);
         return "";
      } else {
         throw new RuntimeException("read string error", var11);
      }
   }

   public int remaining() {
      return this.buffer.remaining();
   }

   public void reuse() {
      long var1 = this.address;
      if (var1 != 0L) {
         this.reused = true;
         native_reuse(var1);
      }

   }

   public void rewind() {
      if (this.justCalc) {
         this.len = 0;
      } else {
         this.buffer.rewind();
      }

   }

   public void skip(int var1) {
      if (var1 != 0) {
         if (!this.justCalc) {
            ByteBuffer var2 = this.buffer;
            var2.position(var2.position() + var1);
         } else {
            this.len += var1;
         }

      }
   }

   public void writeBool(boolean var1) {
      if (!this.justCalc) {
         if (var1) {
            this.writeInt32(-1720552011);
         } else {
            this.writeInt32(-1132882121);
         }
      } else {
         this.len += 4;
      }

   }

   public void writeByte(byte var1) {
      try {
         if (!this.justCalc) {
            this.buffer.put(var1);
         } else {
            ++this.len;
         }
      } catch (Exception var3) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("write byte error");
         }
      }

   }

   public void writeByte(int var1) {
      this.writeByte((byte)var1);
   }

   public void writeByteArray(byte[] var1) {
      label107: {
         boolean var10001;
         label108: {
            label114: {
               try {
                  if (var1.length <= 253) {
                     if (this.justCalc) {
                        break label114;
                     }

                     this.buffer.put((byte)var1.length);
                     break label108;
                  }
               } catch (Exception var11) {
                  var10001 = false;
                  break label107;
               }

               try {
                  if (!this.justCalc) {
                     this.buffer.put((byte)-2);
                     this.buffer.put((byte)var1.length);
                     this.buffer.put((byte)(var1.length >> 8));
                     this.buffer.put((byte)(var1.length >> 16));
                     break label108;
                  }
               } catch (Exception var10) {
                  var10001 = false;
                  break label107;
               }

               try {
                  this.len += 4;
                  break label108;
               } catch (Exception var8) {
                  var10001 = false;
                  break label107;
               }
            }

            try {
               ++this.len;
            } catch (Exception var9) {
               var10001 = false;
               break label107;
            }
         }

         label115: {
            try {
               if (!this.justCalc) {
                  this.buffer.put(var1);
                  break label115;
               }
            } catch (Exception var7) {
               var10001 = false;
               break label107;
            }

            try {
               this.len += var1.length;
            } catch (Exception var6) {
               var10001 = false;
               break label107;
            }
         }

         int var2;
         label69: {
            label68: {
               try {
                  if (var1.length <= 253) {
                     break label68;
                  }
               } catch (Exception var5) {
                  var10001 = false;
                  break label107;
               }

               var2 = 4;
               break label69;
            }

            var2 = 1;
         }

         while(true) {
            label60: {
               try {
                  if ((var1.length + var2) % 4 == 0) {
                     return;
                  }

                  if (!this.justCalc) {
                     this.buffer.put((byte)0);
                     break label60;
                  }
               } catch (Exception var4) {
                  var10001 = false;
                  break;
               }

               try {
                  ++this.len;
               } catch (Exception var3) {
                  var10001 = false;
                  break;
               }
            }

            ++var2;
         }
      }

      if (BuildVars.LOGS_ENABLED) {
         FileLog.e("write byte array error");
      }

   }

   public void writeByteArray(byte[] var1, int var2, int var3) {
      label102: {
         boolean var10001;
         if (var3 <= 253) {
            label99: {
               try {
                  if (!this.justCalc) {
                     this.buffer.put((byte)var3);
                     break label99;
                  }
               } catch (Exception var10) {
                  var10001 = false;
                  break label102;
               }

               try {
                  ++this.len;
               } catch (Exception var9) {
                  var10001 = false;
                  break label102;
               }
            }
         } else {
            label103: {
               try {
                  if (!this.justCalc) {
                     this.buffer.put((byte)-2);
                     this.buffer.put((byte)var3);
                     this.buffer.put((byte)(var3 >> 8));
                     this.buffer.put((byte)(var3 >> 16));
                     break label103;
                  }
               } catch (Exception var11) {
                  var10001 = false;
                  break label102;
               }

               try {
                  this.len += 4;
               } catch (Exception var8) {
                  var10001 = false;
                  break label102;
               }
            }
         }

         label70: {
            try {
               if (!this.justCalc) {
                  this.buffer.put(var1, var2, var3);
                  break label70;
               }
            } catch (Exception var7) {
               var10001 = false;
               break label102;
            }

            try {
               this.len += var3;
            } catch (Exception var6) {
               var10001 = false;
               break label102;
            }
         }

         if (var3 <= 253) {
            var2 = 1;
         } else {
            var2 = 4;
         }

         while(true) {
            if ((var3 + var2) % 4 == 0) {
               return;
            }

            label58: {
               try {
                  if (!this.justCalc) {
                     this.buffer.put((byte)0);
                     break label58;
                  }
               } catch (Exception var5) {
                  var10001 = false;
                  break;
               }

               try {
                  ++this.len;
               } catch (Exception var4) {
                  var10001 = false;
                  break;
               }
            }

            ++var2;
         }
      }

      if (BuildVars.LOGS_ENABLED) {
         FileLog.e("write byte array error");
      }

   }

   public void writeByteBuffer(NativeByteBuffer var1) {
      Exception var10000;
      label99: {
         int var2;
         boolean var10001;
         try {
            var2 = var1.limit();
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label99;
         }

         if (var2 <= 253) {
            label100: {
               try {
                  if (!this.justCalc) {
                     this.buffer.put((byte)var2);
                     break label100;
                  }
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label99;
               }

               try {
                  ++this.len;
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label99;
               }
            }
         } else {
            label102: {
               try {
                  if (!this.justCalc) {
                     this.buffer.put((byte)-2);
                     this.buffer.put((byte)var2);
                     this.buffer.put((byte)(var2 >> 8));
                     this.buffer.put((byte)(var2 >> 16));
                     break label102;
                  }
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label99;
               }

               try {
                  this.len += 4;
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label99;
               }
            }
         }

         label71: {
            try {
               if (!this.justCalc) {
                  var1.rewind();
                  this.buffer.put(var1.buffer);
                  break label71;
               }
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label99;
            }

            try {
               this.len += var2;
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label99;
            }
         }

         int var3;
         if (var2 <= 253) {
            var3 = 1;
         } else {
            var3 = 4;
         }

         while(true) {
            if ((var2 + var3) % 4 == 0) {
               return;
            }

            label59: {
               try {
                  if (!this.justCalc) {
                     this.buffer.put((byte)0);
                     break label59;
                  }
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break;
               }

               try {
                  ++this.len;
               } catch (Exception var4) {
                  var10000 = var4;
                  var10001 = false;
                  break;
               }
            }

            ++var3;
         }
      }

      Exception var13 = var10000;
      FileLog.e((Throwable)var13);
   }

   public void writeBytes(NativeByteBuffer var1) {
      if (this.justCalc) {
         this.len += var1.limit();
      } else {
         var1.rewind();
         this.buffer.put(var1.buffer);
      }

   }

   public void writeBytes(byte[] var1) {
      try {
         if (!this.justCalc) {
            this.buffer.put(var1);
         } else {
            this.len += var1.length;
         }
      } catch (Exception var2) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("write raw error");
         }
      }

   }

   public void writeBytes(byte[] var1, int var2, int var3) {
      try {
         if (!this.justCalc) {
            this.buffer.put(var1, var2, var3);
         } else {
            this.len += var3;
         }
      } catch (Exception var4) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("write raw error");
         }
      }

   }

   public void writeDouble(double var1) {
      try {
         this.writeInt64(Double.doubleToRawLongBits(var1));
      } catch (Exception var4) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("write double error");
         }
      }

   }

   public void writeInt32(int var1) {
      try {
         if (!this.justCalc) {
            this.buffer.putInt(var1);
         } else {
            this.len += 4;
         }
      } catch (Exception var3) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("write int32 error");
         }
      }

   }

   public void writeInt64(long var1) {
      try {
         if (!this.justCalc) {
            this.buffer.putLong(var1);
         } else {
            this.len += 8;
         }
      } catch (Exception var4) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("write int64 error");
         }
      }

   }

   public void writeString(String var1) {
      try {
         this.writeByteArray(var1.getBytes("UTF-8"));
      } catch (Exception var2) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("write string error");
         }
      }

   }
}
