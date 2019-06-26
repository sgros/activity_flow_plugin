package org.telegram.messenger.audioinfo.mp3;

import java.io.IOException;

public class ID3v2FrameHeader {
   private int bodySize;
   private boolean compression;
   private int dataLengthIndicator;
   private boolean encryption;
   private String frameId;
   private int headerSize;
   private boolean unsynchronization;

   public ID3v2FrameHeader(ID3v2TagBody var1) throws IOException, ID3v2Exception {
      long var2 = var1.getPosition();
      ID3v2DataInput var4 = var1.getData();
      int var5 = var1.getTagHeader().getVersion();
      byte var6 = 2;
      if (var5 == 2) {
         this.frameId = new String(var4.readFully(3), "ISO-8859-1");
      } else {
         this.frameId = new String(var4.readFully(4), "ISO-8859-1");
      }

      var5 = var1.getTagHeader().getVersion();
      short var7 = 8;
      if (var5 == 2) {
         this.bodySize = (var4.readByte() & 255) << 16 | (var4.readByte() & 255) << 8 | var4.readByte() & 255;
      } else if (var1.getTagHeader().getVersion() == 3) {
         this.bodySize = var4.readInt();
      } else {
         this.bodySize = var4.readSyncsafeInt();
      }

      if (var1.getTagHeader().getVersion() > 2) {
         var4.readByte();
         byte var8 = var4.readByte();
         var5 = var1.getTagHeader().getVersion();
         byte var9 = 64;
         boolean var10 = false;
         byte var11;
         byte var13;
         if (var5 == 3) {
            var7 = 128;
            var6 = 0;
            var13 = 32;
            var11 = 0;
         } else {
            var13 = 64;
            var9 = 4;
            var11 = 1;
         }

         boolean var12;
         if ((var7 & var8) != 0) {
            var12 = true;
         } else {
            var12 = false;
         }

         this.compression = var12;
         if ((var8 & var6) != 0) {
            var12 = true;
         } else {
            var12 = false;
         }

         this.unsynchronization = var12;
         var12 = var10;
         if ((var8 & var9) != 0) {
            var12 = true;
         }

         this.encryption = var12;
         if (var1.getTagHeader().getVersion() == 3) {
            if (this.compression) {
               this.dataLengthIndicator = var4.readInt();
               this.bodySize -= 4;
            }

            if (this.encryption) {
               var4.readByte();
               --this.bodySize;
            }

            if ((var8 & var13) != 0) {
               var4.readByte();
               --this.bodySize;
            }
         } else {
            if ((var8 & var13) != 0) {
               var4.readByte();
               --this.bodySize;
            }

            if (this.encryption) {
               var4.readByte();
               --this.bodySize;
            }

            if ((var8 & var11) != 0) {
               this.dataLengthIndicator = var4.readSyncsafeInt();
               this.bodySize -= 4;
            }
         }
      }

      this.headerSize = (int)(var1.getPosition() - var2);
   }

   public int getBodySize() {
      return this.bodySize;
   }

   public int getDataLengthIndicator() {
      return this.dataLengthIndicator;
   }

   public String getFrameId() {
      return this.frameId;
   }

   public int getHeaderSize() {
      return this.headerSize;
   }

   public boolean isCompression() {
      return this.compression;
   }

   public boolean isEncryption() {
      return this.encryption;
   }

   public boolean isPadding() {
      boolean var1 = false;

      for(int var2 = 0; var2 < this.frameId.length(); ++var2) {
         if (this.frameId.charAt(0) != 0) {
            return false;
         }
      }

      if (this.bodySize == 0) {
         var1 = true;
      }

      return var1;
   }

   public boolean isUnsynchronization() {
      return this.unsynchronization;
   }

   public boolean isValid() {
      boolean var1 = false;

      for(int var2 = 0; var2 < this.frameId.length(); ++var2) {
         if ((this.frameId.charAt(var2) < 'A' || this.frameId.charAt(var2) > 'Z') && (this.frameId.charAt(var2) < '0' || this.frameId.charAt(var2) > '9')) {
            return false;
         }
      }

      if (this.bodySize > 0) {
         var1 = true;
      }

      return var1;
   }

   public String toString() {
      return String.format("%s[id=%s, bodysize=%d]", ID3v2FrameHeader.class.getSimpleName(), this.frameId, this.bodySize);
   }
}
