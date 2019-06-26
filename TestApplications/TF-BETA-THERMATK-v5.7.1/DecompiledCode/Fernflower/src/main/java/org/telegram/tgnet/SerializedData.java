package org.telegram.tgnet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

public class SerializedData extends AbstractSerializedData {
   private DataInputStream in;
   private ByteArrayInputStream inbuf;
   protected boolean isOut = true;
   private boolean justCalc = false;
   private int len;
   private DataOutputStream out;
   private ByteArrayOutputStream outbuf;

   public SerializedData() {
      this.outbuf = new ByteArrayOutputStream();
      this.out = new DataOutputStream(this.outbuf);
   }

   public SerializedData(int var1) {
      this.outbuf = new ByteArrayOutputStream(var1);
      this.out = new DataOutputStream(this.outbuf);
   }

   public SerializedData(File var1) throws Exception {
      FileInputStream var2 = new FileInputStream(var1);
      byte[] var3 = new byte[(int)var1.length()];
      (new DataInputStream(var2)).readFully(var3);
      var2.close();
      this.isOut = false;
      this.inbuf = new ByteArrayInputStream(var3);
      this.in = new DataInputStream(this.inbuf);
   }

   public SerializedData(boolean var1) {
      if (!var1) {
         this.outbuf = new ByteArrayOutputStream();
         this.out = new DataOutputStream(this.outbuf);
      }

      this.justCalc = var1;
      this.len = 0;
   }

   public SerializedData(byte[] var1) {
      this.isOut = false;
      this.inbuf = new ByteArrayInputStream(var1);
      this.in = new DataInputStream(this.inbuf);
      this.len = 0;
   }

   private void writeInt32(int var1, DataOutputStream var2) {
      for(int var3 = 0; var3 < 4; ++var3) {
         try {
            var2.write(var1 >> var3 * 8);
         } catch (Exception var4) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.e("write int32 error");
            }
            break;
         }
      }

   }

   private void writeInt64(long var1, DataOutputStream var3) {
      for(int var4 = 0; var4 < 8; ++var4) {
         int var5 = (int)(var1 >> var4 * 8);

         try {
            var3.write(var5);
         } catch (Exception var6) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.e("write int64 error");
            }
            break;
         }
      }

   }

   public void cleanup() {
      try {
         if (this.inbuf != null) {
            this.inbuf.close();
            this.inbuf = null;
         }
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

      try {
         if (this.in != null) {
            this.in.close();
            this.in = null;
         }
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      try {
         if (this.outbuf != null) {
            this.outbuf.close();
            this.outbuf = null;
         }
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

      try {
         if (this.out != null) {
            this.out.close();
            this.out = null;
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   public int getPosition() {
      return this.len;
   }

   public int length() {
      if (!this.justCalc) {
         int var1;
         if (this.isOut) {
            var1 = this.outbuf.size();
         } else {
            var1 = this.inbuf.available();
         }

         return var1;
      } else {
         return this.len;
      }
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
            var2 = this.in.read();
            ++this.len;
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
            break label53;
         }

         int var3;
         if (var2 >= 254) {
            try {
               var2 = this.in.read() | this.in.read() << 8 | this.in.read() << 16;
               this.len += 3;
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
            this.in.read(var4);
            ++this.len;
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
               this.in.read();
               ++this.len;
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

         return null;
      } else {
         throw new RuntimeException("read byte array error", var9);
      }
   }

   public NativeByteBuffer readByteBuffer(boolean var1) {
      return null;
   }

   public void readBytes(byte[] var1, boolean var2) {
      try {
         this.in.read(var1);
         this.len += var1.length;
      } catch (Exception var3) {
         if (var2) {
            throw new RuntimeException("read bytes error", var3);
         }

         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("read bytes error");
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
      int var2 = 0;

      int var3;
      for(var3 = 0; var2 < 4; ++var2) {
         try {
            var3 |= this.in.read() << var2 * 8;
            ++this.len;
         } catch (Exception var5) {
            if (!var1) {
               if (BuildVars.LOGS_ENABLED) {
                  FileLog.e("read int32 error");
               }

               return 0;
            }

            throw new RuntimeException("read int32 error", var5);
         }
      }

      return var3;
   }

   public long readInt64(boolean var1) {
      int var2 = 0;

      long var3;
      for(var3 = 0L; var2 < 8; ++var2) {
         try {
            var3 |= (long)this.in.read() << var2 * 8;
            ++this.len;
         } catch (Exception var6) {
            if (!var1) {
               if (BuildVars.LOGS_ENABLED) {
                  FileLog.e("read int64 error");
               }

               return 0L;
            }

            throw new RuntimeException("read int64 error", var6);
         }
      }

      return var3;
   }

   public String readString(boolean var1) {
      Exception var10000;
      label57: {
         int var2;
         boolean var10001;
         try {
            var2 = this.in.read();
            ++this.len;
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label57;
         }

         int var3;
         if (var2 >= 254) {
            try {
               var2 = this.in.read() | this.in.read() << 8 | this.in.read() << 16;
               this.len += 3;
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label57;
            }

            var3 = 4;
         } else {
            var3 = 1;
         }

         byte[] var4;
         try {
            var4 = new byte[var2];
            this.in.read(var4);
            ++this.len;
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
            break label57;
         }

         for(; (var2 + var3) % 4 != 0; ++var3) {
            try {
               this.in.read();
               ++this.len;
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label57;
            }
         }

         try {
            String var11 = new String(var4, "UTF-8");
            return var11;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      Exception var10 = var10000;
      if (!var1) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("read string error");
         }

         return null;
      } else {
         throw new RuntimeException("read string error", var10);
      }
   }

   public int remaining() {
      try {
         int var1 = this.in.available();
         return var1;
      } catch (Exception var3) {
         return Integer.MAX_VALUE;
      }
   }

   protected void set(byte[] var1) {
      this.isOut = false;
      this.inbuf = new ByteArrayInputStream(var1);
      this.in = new DataInputStream(this.inbuf);
   }

   public void skip(int var1) {
      if (var1 != 0) {
         if (!this.justCalc) {
            DataInputStream var2 = this.in;
            if (var2 != null) {
               try {
                  var2.skipBytes(var1);
               } catch (Exception var3) {
                  FileLog.e((Throwable)var3);
               }
            }
         } else {
            this.len += var1;
         }

      }
   }

   public byte[] toByteArray() {
      return this.outbuf.toByteArray();
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
            this.out.writeByte(var1);
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
      try {
         if (!this.justCalc) {
            this.out.writeByte((byte)var1);
         } else {
            ++this.len;
         }
      } catch (Exception var3) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("write byte error");
         }
      }

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

                     this.out.write(var1.length);
                     break label108;
                  }
               } catch (Exception var11) {
                  var10001 = false;
                  break label107;
               }

               try {
                  if (!this.justCalc) {
                     this.out.write(254);
                     this.out.write(var1.length);
                     this.out.write(var1.length >> 8);
                     this.out.write(var1.length >> 16);
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
                  this.out.write(var1);
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
                     this.out.write(0);
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
      label104: {
         boolean var10001;
         if (var3 <= 253) {
            label100: {
               try {
                  if (!this.justCalc) {
                     this.out.write(var3);
                     break label100;
                  }
               } catch (Exception var10) {
                  var10001 = false;
                  break label104;
               }

               try {
                  ++this.len;
               } catch (Exception var9) {
                  var10001 = false;
                  break label104;
               }
            }
         } else {
            label103: {
               try {
                  if (!this.justCalc) {
                     this.out.write(254);
                     this.out.write(var3);
                     this.out.write(var3 >> 8);
                     this.out.write(var3 >> 16);
                     break label103;
                  }
               } catch (Exception var11) {
                  var10001 = false;
                  break label104;
               }

               try {
                  this.len += 4;
               } catch (Exception var8) {
                  var10001 = false;
                  break label104;
               }
            }
         }

         label70: {
            try {
               if (!this.justCalc) {
                  this.out.write(var1, var2, var3);
                  break label70;
               }
            } catch (Exception var7) {
               var10001 = false;
               break label104;
            }

            try {
               this.len += var3;
            } catch (Exception var6) {
               var10001 = false;
               break label104;
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
                     this.out.write(0);
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
   }

   public void writeBytes(byte[] var1) {
      try {
         if (!this.justCalc) {
            this.out.write(var1);
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
            this.out.write(var1, var2, var3);
         } else {
            this.len += var3;
         }
      } catch (Exception var4) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("write bytes error");
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
      if (!this.justCalc) {
         this.writeInt32(var1, this.out);
      } else {
         this.len += 4;
      }

   }

   public void writeInt64(long var1) {
      if (!this.justCalc) {
         this.writeInt64(var1, this.out);
      } else {
         this.len += 8;
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
