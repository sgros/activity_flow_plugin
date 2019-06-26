package org.telegram.messenger.audioinfo.mp3;

import java.io.IOException;
import java.io.InputStream;
import org.telegram.messenger.audioinfo.util.RangeInputStream;

public class ID3v2FrameBody {
   static final ThreadLocal textBuffer = new ThreadLocal() {
      protected ID3v2FrameBody.Buffer initialValue() {
         return new ID3v2FrameBody.Buffer(4096);
      }
   };
   private final ID3v2DataInput data;
   private final ID3v2FrameHeader frameHeader;
   private final RangeInputStream input;
   private final ID3v2TagHeader tagHeader;

   ID3v2FrameBody(InputStream var1, long var2, int var4, ID3v2TagHeader var5, ID3v2FrameHeader var6) throws IOException {
      this.input = new RangeInputStream(var1, var2, (long)var4);
      this.data = new ID3v2DataInput(this.input);
      this.tagHeader = var5;
      this.frameHeader = var6;
   }

   private String extractString(byte[] var1, int var2, int var3, ID3v2Encoding var4, boolean var5) {
      int var6 = var3;
      if (var5) {
         int var7 = 0;
         int var8 = 0;

         while(true) {
            var6 = var3;
            if (var7 >= var3) {
               break;
            }

            var6 = var2 + var7;
            if (var1[var6] != 0 || var4 == ID3v2Encoding.UTF_16 && var8 == 0 && var6 % 2 != 0) {
               var8 = 0;
            } else {
               var6 = var8 + 1;
               var8 = var6;
               if (var6 == var4.getZeroBytes()) {
                  var6 = var7 + 1 - var4.getZeroBytes();
                  break;
               }
            }

            ++var7;
         }
      }

      boolean var10001;
      String var9;
      try {
         var9 = new String(var1, var2, var6, var4.getCharset().name());
      } catch (Exception var13) {
         var10001 = false;
         return "";
      }

      String var14 = var9;

      label72: {
         try {
            if (var9.length() <= 0) {
               break label72;
            }
         } catch (Exception var12) {
            var10001 = false;
            return "";
         }

         var14 = var9;

         try {
            if (var9.charAt(0) == '\ufeff') {
               var14 = var9.substring(1);
            }
         } catch (Exception var11) {
            var10001 = false;
            return "";
         }
      }

      try {
         return var14;
      } catch (Exception var10) {
         var10001 = false;
         return "";
      }
   }

   public ID3v2DataInput getData() {
      return this.data;
   }

   public ID3v2FrameHeader getFrameHeader() {
      return this.frameHeader;
   }

   public long getPosition() {
      return this.input.getPosition();
   }

   public long getRemainingLength() {
      return this.input.getRemainingLength();
   }

   public ID3v2TagHeader getTagHeader() {
      return this.tagHeader;
   }

   public ID3v2Encoding readEncoding() throws IOException, ID3v2Exception {
      byte var1 = this.data.readByte();
      if (var1 != 0) {
         if (var1 != 1) {
            if (var1 != 2) {
               if (var1 == 3) {
                  return ID3v2Encoding.UTF_8;
               } else {
                  StringBuilder var2 = new StringBuilder();
                  var2.append("Invalid encoding: ");
                  var2.append(var1);
                  throw new ID3v2Exception(var2.toString());
               }
            } else {
               return ID3v2Encoding.UTF_16BE;
            }
         } else {
            return ID3v2Encoding.UTF_16;
         }
      } else {
         return ID3v2Encoding.ISO_8859_1;
      }
   }

   public String readFixedLengthString(int var1, ID3v2Encoding var2) throws IOException, ID3v2Exception {
      if ((long)var1 <= this.getRemainingLength()) {
         byte[] var3 = ((ID3v2FrameBody.Buffer)textBuffer.get()).bytes(var1);
         this.data.readFully(var3, 0, var1);
         return this.extractString(var3, 0, var1, var2, true);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Could not read fixed-length string of length: ");
         var4.append(var1);
         throw new ID3v2Exception(var4.toString());
      }
   }

   public String readZeroTerminatedString(int var1, ID3v2Encoding var2) throws IOException, ID3v2Exception {
      int var3 = Math.min(var1, (int)this.getRemainingLength());
      byte[] var4 = ((ID3v2FrameBody.Buffer)textBuffer.get()).bytes(var3);
      int var5 = 0;

      for(var1 = 0; var5 < var3; ++var5) {
         byte var6 = this.data.readByte();
         var4[var5] = (byte)var6;
         if (var6 != 0 || var2 == ID3v2Encoding.UTF_16 && var1 == 0 && var5 % 2 != 0) {
            var1 = 0;
         } else {
            int var7 = var1 + 1;
            var1 = var7;
            if (var7 == var2.getZeroBytes()) {
               return this.extractString(var4, 0, var5 + 1 - var2.getZeroBytes(), var2, false);
            }
         }
      }

      throw new ID3v2Exception("Could not read zero-termiated string");
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("id3v2frame[pos=");
      var1.append(this.getPosition());
      var1.append(", ");
      var1.append(this.getRemainingLength());
      var1.append(" left]");
      return var1.toString();
   }

   static final class Buffer {
      byte[] bytes;

      Buffer(int var1) {
         this.bytes = new byte[var1];
      }

      byte[] bytes(int var1) {
         byte[] var2 = this.bytes;
         if (var1 > var2.length) {
            int var3 = var2.length;

            while(true) {
               var3 *= 2;
               if (var1 <= var3) {
                  this.bytes = new byte[var3];
                  break;
               }
            }
         }

         return this.bytes;
      }
   }
}
