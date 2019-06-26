package org.telegram.messenger.audioinfo.mp3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.telegram.messenger.audioinfo.util.PositionInputStream;

public class ID3v2TagHeader {
   private boolean compression;
   private int footerSize;
   private int headerSize;
   private int paddingSize;
   private int revision;
   private int totalTagSize;
   private boolean unsynchronization;
   private int version;

   public ID3v2TagHeader(InputStream var1) throws IOException, ID3v2Exception {
      this(new PositionInputStream(var1));
   }

   ID3v2TagHeader(PositionInputStream var1) throws IOException, ID3v2Exception {
      boolean var2 = false;
      boolean var3 = false;
      this.version = 0;
      this.revision = 0;
      this.headerSize = 0;
      this.totalTagSize = 0;
      this.paddingSize = 0;
      this.footerSize = 0;
      long var4 = var1.getPosition();
      ID3v2DataInput var6 = new ID3v2DataInput(var1);
      String var7 = new String(var6.readFully(3), "ISO-8859-1");
      StringBuilder var10;
      if ("ID3".equals(var7)) {
         this.version = var6.readByte();
         int var8 = this.version;
         if (var8 != 2 && var8 != 3 && var8 != 4) {
            var10 = new StringBuilder();
            var10.append("Unsupported ID3v2 version: ");
            var10.append(this.version);
            throw new ID3v2Exception(var10.toString());
         } else {
            this.revision = var6.readByte();
            byte var11 = var6.readByte();
            this.totalTagSize = var6.readSyncsafeInt() + 10;
            if (this.version == 2) {
               if ((var11 & 128) != 0) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               this.unsynchronization = var2;
               var2 = var3;
               if ((var11 & 64) != 0) {
                  var2 = true;
               }

               this.compression = var2;
            } else {
               if ((var11 & 128) != 0) {
                  var2 = true;
               }

               this.unsynchronization = var2;
               if ((var11 & 64) != 0) {
                  if (this.version == 3) {
                     int var9 = var6.readInt();
                     var6.readByte();
                     var6.readByte();
                     this.paddingSize = var6.readInt();
                     var6.skipFully((long)(var9 - 6));
                  } else {
                     var6.skipFully((long)(var6.readSyncsafeInt() - 4));
                  }
               }

               if (this.version >= 4 && (var11 & 16) != 0) {
                  this.footerSize = 10;
                  this.totalTagSize += 10;
               }
            }

            this.headerSize = (int)(var1.getPosition() - var4);
         }
      } else {
         var10 = new StringBuilder();
         var10.append("Invalid ID3 identifier: ");
         var10.append(var7);
         throw new ID3v2Exception(var10.toString());
      }
   }

   public int getFooterSize() {
      return this.footerSize;
   }

   public int getHeaderSize() {
      return this.headerSize;
   }

   public int getPaddingSize() {
      return this.paddingSize;
   }

   public int getRevision() {
      return this.revision;
   }

   public int getTotalTagSize() {
      return this.totalTagSize;
   }

   public int getVersion() {
      return this.version;
   }

   public boolean isCompression() {
      return this.compression;
   }

   public boolean isUnsynchronization() {
      return this.unsynchronization;
   }

   public ID3v2TagBody tagBody(InputStream var1) throws IOException, ID3v2Exception {
      if (this.compression) {
         throw new ID3v2Exception("Tag compression is not supported");
      } else if (this.version < 4 && this.unsynchronization) {
         byte[] var8 = (new ID3v2DataInput(var1)).readFully(this.totalTagSize - this.headerSize);
         int var2 = var8.length;
         int var3 = 0;
         int var4 = 0;

         int var7;
         for(boolean var9 = false; var3 < var2; var4 = var7) {
            byte var6;
            label26: {
               var6 = var8[var3];
               if (var9) {
                  var7 = var4;
                  if (var6 == 0) {
                     break label26;
                  }
               }

               var8[var4] = (byte)var6;
               var7 = var4 + 1;
            }

            if (var6 == -1) {
               var9 = true;
            } else {
               var9 = false;
            }

            ++var3;
         }

         return new ID3v2TagBody(new ByteArrayInputStream(var8, 0, var4), (long)this.headerSize, var4, this);
      } else {
         int var5 = this.headerSize;
         return new ID3v2TagBody(var1, (long)var5, this.totalTagSize - var5 - this.footerSize, this);
      }
   }

   public String toString() {
      return String.format("%s[version=%s, totalTagSize=%d]", ID3v2TagHeader.class.getSimpleName(), this.version, this.totalTagSize);
   }
}
