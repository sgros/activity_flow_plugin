package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractContainerBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class SampleDescriptionBox extends AbstractContainerBox implements FullBox {
   private int flags;
   private int version;

   public SampleDescriptionBox() {
      super("stsd");
   }

   public void getBox(WritableByteChannel var1) throws IOException {
      var1.write(this.getHeader());
      ByteBuffer var2 = ByteBuffer.allocate(8);
      IsoTypeWriter.writeUInt8(var2, this.version);
      IsoTypeWriter.writeUInt24(var2, this.flags);
      IsoTypeWriter.writeUInt32(var2, (long)this.getBoxes().size());
      var1.write((ByteBuffer)var2.rewind());
      this.writeContainer(var1);
   }

   public long getSize() {
      long var1 = this.getContainerSize() + 8L;
      byte var3;
      if (!super.largeBox && 8L + var1 < 4294967296L) {
         var3 = 8;
      } else {
         var3 = 16;
      }

      return var1 + (long)var3;
   }
}
