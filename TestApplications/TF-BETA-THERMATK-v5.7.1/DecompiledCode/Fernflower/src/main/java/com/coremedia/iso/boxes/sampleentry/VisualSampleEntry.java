package com.coremedia.iso.boxes.sampleentry;

import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.Utf8;
import com.coremedia.iso.boxes.Container;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public final class VisualSampleEntry extends AbstractSampleEntry implements Container {
   private String compressorname = "";
   private int depth = 24;
   private int frameCount = 1;
   private int height;
   private double horizresolution = 72.0D;
   private long[] predefined = new long[3];
   private double vertresolution = 72.0D;
   private int width;

   public VisualSampleEntry(String var1) {
      super(var1);
   }

   public void getBox(WritableByteChannel var1) throws IOException {
      var1.write(this.getHeader());
      ByteBuffer var2 = ByteBuffer.allocate(78);
      var2.position(6);
      IsoTypeWriter.writeUInt16(var2, super.dataReferenceIndex);
      IsoTypeWriter.writeUInt16(var2, 0);
      IsoTypeWriter.writeUInt16(var2, 0);
      IsoTypeWriter.writeUInt32(var2, this.predefined[0]);
      IsoTypeWriter.writeUInt32(var2, this.predefined[1]);
      IsoTypeWriter.writeUInt32(var2, this.predefined[2]);
      IsoTypeWriter.writeUInt16(var2, this.getWidth());
      IsoTypeWriter.writeUInt16(var2, this.getHeight());
      IsoTypeWriter.writeFixedPoint1616(var2, this.getHorizresolution());
      IsoTypeWriter.writeFixedPoint1616(var2, this.getVertresolution());
      IsoTypeWriter.writeUInt32(var2, 0L);
      IsoTypeWriter.writeUInt16(var2, this.getFrameCount());
      IsoTypeWriter.writeUInt8(var2, Utf8.utf8StringLengthInBytes(this.getCompressorname()));
      var2.put(Utf8.convert(this.getCompressorname()));
      int var3 = Utf8.utf8StringLengthInBytes(this.getCompressorname());

      while(var3 < 31) {
         ++var3;
         var2.put((byte)0);
      }

      IsoTypeWriter.writeUInt16(var2, this.getDepth());
      IsoTypeWriter.writeUInt16(var2, 65535);
      var1.write((ByteBuffer)var2.rewind());
      this.writeContainer(var1);
   }

   public String getCompressorname() {
      return this.compressorname;
   }

   public int getDepth() {
      return this.depth;
   }

   public int getFrameCount() {
      return this.frameCount;
   }

   public int getHeight() {
      return this.height;
   }

   public double getHorizresolution() {
      return this.horizresolution;
   }

   public long getSize() {
      long var1 = this.getContainerSize() + 78L;
      byte var3;
      if (!super.largeBox && 8L + var1 < 4294967296L) {
         var3 = 8;
      } else {
         var3 = 16;
      }

      return var1 + (long)var3;
   }

   public double getVertresolution() {
      return this.vertresolution;
   }

   public int getWidth() {
      return this.width;
   }

   public void setDepth(int var1) {
      this.depth = var1;
   }

   public void setFrameCount(int var1) {
      this.frameCount = var1;
   }

   public void setHeight(int var1) {
      this.height = var1;
   }

   public void setHorizresolution(double var1) {
      this.horizresolution = var1;
   }

   public void setVertresolution(double var1) {
      this.vertresolution = var1;
   }

   public void setWidth(int var1) {
      this.width = var1;
   }
}
