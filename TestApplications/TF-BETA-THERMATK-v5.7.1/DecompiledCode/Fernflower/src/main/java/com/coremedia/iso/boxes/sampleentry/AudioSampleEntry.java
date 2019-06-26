package com.coremedia.iso.boxes.sampleentry;

import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public final class AudioSampleEntry extends AbstractSampleEntry {
   private long bytesPerFrame;
   private long bytesPerPacket;
   private long bytesPerSample;
   private int channelCount;
   private int compressionId;
   private int packetSize;
   private int reserved1;
   private long reserved2;
   private long sampleRate;
   private int sampleSize;
   private long samplesPerPacket;
   private int soundVersion;
   private byte[] soundVersion2Data;

   public AudioSampleEntry(String var1) {
      super(var1);
   }

   public void getBox(WritableByteChannel var1) throws IOException {
      var1.write(this.getHeader());
      int var2 = this.soundVersion;
      byte var3 = 0;
      byte var5;
      if (var2 == 1) {
         var5 = 16;
      } else {
         var5 = 0;
      }

      if (this.soundVersion == 2) {
         var3 = 36;
      }

      ByteBuffer var4 = ByteBuffer.allocate(var5 + 28 + var3);
      var4.position(6);
      IsoTypeWriter.writeUInt16(var4, super.dataReferenceIndex);
      IsoTypeWriter.writeUInt16(var4, this.soundVersion);
      IsoTypeWriter.writeUInt16(var4, this.reserved1);
      IsoTypeWriter.writeUInt32(var4, this.reserved2);
      IsoTypeWriter.writeUInt16(var4, this.channelCount);
      IsoTypeWriter.writeUInt16(var4, this.sampleSize);
      IsoTypeWriter.writeUInt16(var4, this.compressionId);
      IsoTypeWriter.writeUInt16(var4, this.packetSize);
      if (super.type.equals("mlpa")) {
         IsoTypeWriter.writeUInt32(var4, this.getSampleRate());
      } else {
         IsoTypeWriter.writeUInt32(var4, this.getSampleRate() << 16);
      }

      if (this.soundVersion == 1) {
         IsoTypeWriter.writeUInt32(var4, this.samplesPerPacket);
         IsoTypeWriter.writeUInt32(var4, this.bytesPerPacket);
         IsoTypeWriter.writeUInt32(var4, this.bytesPerFrame);
         IsoTypeWriter.writeUInt32(var4, this.bytesPerSample);
      }

      if (this.soundVersion == 2) {
         IsoTypeWriter.writeUInt32(var4, this.samplesPerPacket);
         IsoTypeWriter.writeUInt32(var4, this.bytesPerPacket);
         IsoTypeWriter.writeUInt32(var4, this.bytesPerFrame);
         IsoTypeWriter.writeUInt32(var4, this.bytesPerSample);
         var4.put(this.soundVersion2Data);
      }

      var1.write((ByteBuffer)var4.rewind());
      this.writeContainer(var1);
   }

   public int getChannelCount() {
      return this.channelCount;
   }

   public long getSampleRate() {
      return this.sampleRate;
   }

   public long getSize() {
      int var1 = this.soundVersion;
      byte var2 = 16;
      byte var3 = 0;
      byte var6;
      if (var1 == 1) {
         var6 = 16;
      } else {
         var6 = 0;
      }

      if (this.soundVersion == 2) {
         var3 = 36;
      }

      long var4 = (long)(var6 + 28 + var3) + this.getContainerSize();
      var6 = var2;
      if (!super.largeBox) {
         if (8L + var4 >= 4294967296L) {
            var6 = var2;
         } else {
            var6 = 8;
         }
      }

      return var4 + (long)var6;
   }

   public void setChannelCount(int var1) {
      this.channelCount = var1;
   }

   public void setSampleRate(long var1) {
      this.sampleRate = var1;
   }

   public void setSampleSize(int var1) {
      this.sampleSize = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("AudioSampleEntry{bytesPerSample=");
      var1.append(this.bytesPerSample);
      var1.append(", bytesPerFrame=");
      var1.append(this.bytesPerFrame);
      var1.append(", bytesPerPacket=");
      var1.append(this.bytesPerPacket);
      var1.append(", samplesPerPacket=");
      var1.append(this.samplesPerPacket);
      var1.append(", packetSize=");
      var1.append(this.packetSize);
      var1.append(", compressionId=");
      var1.append(this.compressionId);
      var1.append(", soundVersion=");
      var1.append(this.soundVersion);
      var1.append(", sampleRate=");
      var1.append(this.sampleRate);
      var1.append(", sampleSize=");
      var1.append(this.sampleSize);
      var1.append(", channelCount=");
      var1.append(this.channelCount);
      var1.append(", boxes=");
      var1.append(this.getBoxes());
      var1.append('}');
      return var1.toString();
   }
}
