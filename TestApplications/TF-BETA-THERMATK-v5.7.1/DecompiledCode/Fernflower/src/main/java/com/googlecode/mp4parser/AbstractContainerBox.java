package com.googlecode.mp4parser;

import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class AbstractContainerBox extends BasicContainer implements Box {
   protected boolean largeBox;
   Container parent;
   protected String type;

   public AbstractContainerBox(String var1) {
      this.type = var1;
   }

   public void getBox(WritableByteChannel var1) throws IOException {
      var1.write(this.getHeader());
      this.writeContainer(var1);
   }

   protected ByteBuffer getHeader() {
      byte[] var1;
      ByteBuffer var2;
      if (!this.largeBox && this.getSize() < 4294967296L) {
         var1 = new byte[8];
         var1[4] = (byte)this.type.getBytes()[0];
         var1[5] = (byte)this.type.getBytes()[1];
         var1[6] = (byte)this.type.getBytes()[2];
         var1[7] = (byte)this.type.getBytes()[3];
         var2 = ByteBuffer.wrap(var1);
         IsoTypeWriter.writeUInt32(var2, this.getSize());
      } else {
         var1 = new byte[16];
         var1[3] = (byte)1;
         var1[4] = (byte)this.type.getBytes()[0];
         var1[5] = (byte)this.type.getBytes()[1];
         var1[6] = (byte)this.type.getBytes()[2];
         var1[7] = (byte)this.type.getBytes()[3];
         var2 = ByteBuffer.wrap(var1);
         var2.position(8);
         IsoTypeWriter.writeUInt64(var2, this.getSize());
      }

      var2.rewind();
      return var2;
   }

   public long getSize() {
      long var1 = this.getContainerSize();
      byte var3;
      if (!this.largeBox && 8L + var1 < 4294967296L) {
         var3 = 8;
      } else {
         var3 = 16;
      }

      return var1 + (long)var3;
   }

   public void setParent(Container var1) {
      this.parent = var1;
   }
}
