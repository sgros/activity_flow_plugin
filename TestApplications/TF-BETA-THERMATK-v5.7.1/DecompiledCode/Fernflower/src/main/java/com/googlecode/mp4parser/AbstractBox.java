package com.googlecode.mp4parser;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Logger;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractBox implements Box {
   private static Logger LOG = Logger.getLogger(AbstractBox.class);
   private ByteBuffer content;
   long contentStartPosition;
   DataSource dataSource;
   private ByteBuffer deadBytes = null;
   boolean isParsed;
   boolean isRead;
   long memMapSize = -1L;
   private Container parent;
   protected String type;
   private byte[] userType;

   protected AbstractBox(String var1) {
      this.type = var1;
      this.isRead = true;
      this.isParsed = true;
   }

   private void getHeader(ByteBuffer var1) {
      if (this.isSmallBox()) {
         IsoTypeWriter.writeUInt32(var1, this.getSize());
         var1.put(IsoFile.fourCCtoBytes(this.getType()));
      } else {
         IsoTypeWriter.writeUInt32(var1, 1L);
         var1.put(IsoFile.fourCCtoBytes(this.getType()));
         IsoTypeWriter.writeUInt64(var1, this.getSize());
      }

      if ("uuid".equals(this.getType())) {
         var1.put(this.getUserType());
      }

   }

   private boolean isSmallBox() {
      byte var1;
      if ("uuid".equals(this.getType())) {
         var1 = 24;
      } else {
         var1 = 8;
      }

      if (this.isRead) {
         if (this.isParsed) {
            long var2 = this.getContentSize();
            ByteBuffer var4 = this.deadBytes;
            int var5;
            if (var4 != null) {
               var5 = var4.limit();
            } else {
               var5 = 0;
            }

            return var2 + (long)var5 + (long)var1 < 4294967296L;
         } else {
            return (long)(this.content.limit() + var1) < 4294967296L;
         }
      } else {
         return this.memMapSize + (long)var1 < 4294967296L;
      }
   }

   private void readContent() {
      // $FF: Couldn't be decompiled
   }

   protected abstract void _parseDetails(ByteBuffer var1);

   public void getBox(WritableByteChannel var1) throws IOException {
      boolean var2 = this.isRead;
      byte var3 = 8;
      byte var4 = 0;
      byte var5 = 16;
      ByteBuffer var6;
      if (var2) {
         if (this.isParsed) {
            var6 = ByteBuffer.allocate(CastUtils.l2i(this.getSize()));
            this.getHeader(var6);
            this.getContent(var6);
            ByteBuffer var7 = this.deadBytes;
            if (var7 != null) {
               var7.rewind();

               while(this.deadBytes.remaining() > 0) {
                  var6.put(this.deadBytes);
               }
            }

            var1.write((ByteBuffer)var6.rewind());
         } else {
            if (!this.isSmallBox()) {
               var3 = 16;
            }

            if (!"uuid".equals(this.getType())) {
               var5 = 0;
            }

            var6 = ByteBuffer.allocate(var3 + var5);
            this.getHeader(var6);
            var1.write((ByteBuffer)var6.rewind());
            var1.write((ByteBuffer)this.content.position(0));
         }
      } else {
         if (!this.isSmallBox()) {
            var3 = 16;
         }

         var5 = var4;
         if ("uuid".equals(this.getType())) {
            var5 = 16;
         }

         var6 = ByteBuffer.allocate(var3 + var5);
         this.getHeader(var6);
         var1.write((ByteBuffer)var6.rewind());
         this.dataSource.transferTo(this.contentStartPosition, this.memMapSize, var1);
      }

   }

   protected abstract void getContent(ByteBuffer var1);

   protected abstract long getContentSize();

   public long getSize() {
      boolean var1 = this.isRead;
      byte var2 = 0;
      long var3;
      ByteBuffer var5;
      int var6;
      if (var1) {
         if (this.isParsed) {
            var3 = this.getContentSize();
         } else {
            var5 = this.content;
            if (var5 != null) {
               var6 = var5.limit();
            } else {
               var6 = 0;
            }

            var3 = (long)var6;
         }
      } else {
         var3 = this.memMapSize;
      }

      byte var10;
      if (var3 >= 4294967288L) {
         var10 = 8;
      } else {
         var10 = 0;
      }

      byte var7;
      if ("uuid".equals(this.getType())) {
         var7 = 16;
      } else {
         var7 = 0;
      }

      long var8 = (long)(var10 + 8 + var7);
      var5 = this.deadBytes;
      if (var5 == null) {
         var6 = var2;
      } else {
         var6 = var5.limit();
      }

      return var3 + var8 + (long)var6;
   }

   public String getType() {
      return this.type;
   }

   public byte[] getUserType() {
      return this.userType;
   }

   public boolean isParsed() {
      return this.isParsed;
   }

   public final void parseDetails() {
      synchronized(this){}

      try {
         this.readContent();
         Logger var1 = LOG;
         StringBuilder var2 = new StringBuilder("parsing details of ");
         var2.append(this.getType());
         var1.logDebug(var2.toString());
         if (this.content != null) {
            ByteBuffer var5 = this.content;
            this.isParsed = true;
            var5.rewind();
            this._parseDetails(var5);
            if (var5.remaining() > 0) {
               this.deadBytes = var5.slice();
            }

            this.content = null;
         }
      } finally {
         ;
      }

   }

   public void setParent(Container var1) {
      this.parent = var1;
   }
}
