package org.telegram.messenger.audioinfo.mp3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;
import org.telegram.messenger.audioinfo.util.RangeInputStream;

public class ID3v2TagBody {
   private final ID3v2DataInput data;
   private final RangeInputStream input;
   private final ID3v2TagHeader tagHeader;

   ID3v2TagBody(InputStream var1, long var2, int var4, ID3v2TagHeader var5) throws IOException {
      this.input = new RangeInputStream(var1, var2, (long)var4);
      this.data = new ID3v2DataInput(this.input);
      this.tagHeader = var5;
   }

   public ID3v2FrameBody frameBody(ID3v2FrameHeader var1) throws IOException, ID3v2Exception {
      int var2 = var1.getBodySize();
      Object var3 = this.input;
      if (var1.isUnsynchronization()) {
         byte[] var9 = this.data.readFully(var1.getBodySize());
         int var4 = var9.length;
         int var5 = 0;
         var2 = 0;

         int var8;
         for(boolean var6 = false; var5 < var4; var2 = var8) {
            byte var7;
            label31: {
               var7 = var9[var5];
               if (var6) {
                  var8 = var2;
                  if (var7 == 0) {
                     break label31;
                  }
               }

               var9[var2] = (byte)var7;
               var8 = var2 + 1;
            }

            if (var7 == -1) {
               var6 = true;
            } else {
               var6 = false;
            }

            ++var5;
         }

         var3 = new ByteArrayInputStream(var9, 0, var2);
      }

      if (!var1.isEncryption()) {
         if (var1.isCompression()) {
            var2 = var1.getDataLengthIndicator();
            var3 = new InflaterInputStream((InputStream)var3);
         }

         return new ID3v2FrameBody((InputStream)var3, (long)var1.getHeaderSize(), var2, this.tagHeader, var1);
      } else {
         throw new ID3v2Exception("Frame encryption is not supported");
      }
   }

   public ID3v2DataInput getData() {
      return this.data;
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

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("id3v2tag[pos=");
      var1.append(this.getPosition());
      var1.append(", ");
      var1.append(this.getRemainingLength());
      var1.append(" left]");
      return var1.toString();
   }
}
