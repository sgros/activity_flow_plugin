package com.mp4parser.iso14496.part15;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitWriterBuffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AvcDecoderConfigurationRecord {
   public int avcLevelIndication;
   public int avcProfileIndication;
   public int bitDepthChromaMinus8;
   public int bitDepthChromaMinus8PaddingBits;
   public int bitDepthLumaMinus8;
   public int bitDepthLumaMinus8PaddingBits;
   public int chromaFormat = 1;
   public int chromaFormatPaddingBits;
   public int configurationVersion;
   public boolean hasExts = true;
   public int lengthSizeMinusOne;
   public int lengthSizeMinusOnePaddingBits;
   public int numberOfSequenceParameterSetsPaddingBits;
   public List pictureParameterSets = new ArrayList();
   public int profileCompatibility;
   public List sequenceParameterSetExts;
   public List sequenceParameterSets = new ArrayList();

   public AvcDecoderConfigurationRecord() {
      this.bitDepthLumaMinus8 = 0;
      this.bitDepthChromaMinus8 = 0;
      this.sequenceParameterSetExts = new ArrayList();
      this.lengthSizeMinusOnePaddingBits = 63;
      this.numberOfSequenceParameterSetsPaddingBits = 7;
      this.chromaFormatPaddingBits = 31;
      this.bitDepthLumaMinus8PaddingBits = 31;
      this.bitDepthChromaMinus8PaddingBits = 31;
   }

   public AvcDecoderConfigurationRecord(ByteBuffer var1) {
      byte var2 = 0;
      this.bitDepthLumaMinus8 = 0;
      this.bitDepthChromaMinus8 = 0;
      this.sequenceParameterSetExts = new ArrayList();
      this.lengthSizeMinusOnePaddingBits = 63;
      this.numberOfSequenceParameterSetsPaddingBits = 7;
      this.chromaFormatPaddingBits = 31;
      this.bitDepthLumaMinus8PaddingBits = 31;
      this.bitDepthChromaMinus8PaddingBits = 31;
      this.configurationVersion = IsoTypeReader.readUInt8(var1);
      this.avcProfileIndication = IsoTypeReader.readUInt8(var1);
      this.profileCompatibility = IsoTypeReader.readUInt8(var1);
      this.avcLevelIndication = IsoTypeReader.readUInt8(var1);
      BitReaderBuffer var3 = new BitReaderBuffer(var1);
      this.lengthSizeMinusOnePaddingBits = var3.readBits(6);
      this.lengthSizeMinusOne = var3.readBits(2);
      this.numberOfSequenceParameterSetsPaddingBits = var3.readBits(3);
      int var4 = var3.readBits(5);

      int var5;
      byte[] var8;
      for(var5 = 0; var5 < var4; ++var5) {
         var8 = new byte[IsoTypeReader.readUInt16(var1)];
         var1.get(var8);
         this.sequenceParameterSets.add(var8);
      }

      long var6 = (long)IsoTypeReader.readUInt8(var1);

      for(var5 = 0; (long)var5 < var6; ++var5) {
         var8 = new byte[IsoTypeReader.readUInt16(var1)];
         var1.get(var8);
         this.pictureParameterSets.add(var8);
      }

      if (var1.remaining() < 4) {
         this.hasExts = false;
      }

      if (this.hasExts) {
         var5 = this.avcProfileIndication;
         if (var5 == 100 || var5 == 110 || var5 == 122 || var5 == 144) {
            var3 = new BitReaderBuffer(var1);
            this.chromaFormatPaddingBits = var3.readBits(6);
            this.chromaFormat = var3.readBits(2);
            this.bitDepthLumaMinus8PaddingBits = var3.readBits(5);
            this.bitDepthLumaMinus8 = var3.readBits(3);
            this.bitDepthChromaMinus8PaddingBits = var3.readBits(5);
            this.bitDepthChromaMinus8 = var3.readBits(3);
            var6 = (long)IsoTypeReader.readUInt8(var1);

            for(var5 = var2; (long)var5 < var6; ++var5) {
               var8 = new byte[IsoTypeReader.readUInt16(var1)];
               var1.get(var8);
               this.sequenceParameterSetExts.add(var8);
            }

            return;
         }
      }

      this.chromaFormat = -1;
      this.bitDepthLumaMinus8 = -1;
      this.bitDepthChromaMinus8 = -1;
   }

   public void getContent(ByteBuffer var1) {
      IsoTypeWriter.writeUInt8(var1, this.configurationVersion);
      IsoTypeWriter.writeUInt8(var1, this.avcProfileIndication);
      IsoTypeWriter.writeUInt8(var1, this.profileCompatibility);
      IsoTypeWriter.writeUInt8(var1, this.avcLevelIndication);
      BitWriterBuffer var2 = new BitWriterBuffer(var1);
      var2.writeBits(this.lengthSizeMinusOnePaddingBits, 6);
      var2.writeBits(this.lengthSizeMinusOne, 2);
      var2.writeBits(this.numberOfSequenceParameterSetsPaddingBits, 3);
      var2.writeBits(this.pictureParameterSets.size(), 5);
      Iterator var5 = this.sequenceParameterSets.iterator();

      byte[] var4;
      while(var5.hasNext()) {
         var4 = (byte[])var5.next();
         IsoTypeWriter.writeUInt16(var1, var4.length);
         var1.put(var4);
      }

      IsoTypeWriter.writeUInt8(var1, this.pictureParameterSets.size());
      var5 = this.pictureParameterSets.iterator();

      while(var5.hasNext()) {
         var4 = (byte[])var5.next();
         IsoTypeWriter.writeUInt16(var1, var4.length);
         var1.put(var4);
      }

      if (this.hasExts) {
         int var3 = this.avcProfileIndication;
         if (var3 == 100 || var3 == 110 || var3 == 122 || var3 == 144) {
            var2 = new BitWriterBuffer(var1);
            var2.writeBits(this.chromaFormatPaddingBits, 6);
            var2.writeBits(this.chromaFormat, 2);
            var2.writeBits(this.bitDepthLumaMinus8PaddingBits, 5);
            var2.writeBits(this.bitDepthLumaMinus8, 3);
            var2.writeBits(this.bitDepthChromaMinus8PaddingBits, 5);
            var2.writeBits(this.bitDepthChromaMinus8, 3);
            var5 = this.sequenceParameterSetExts.iterator();

            while(var5.hasNext()) {
               var4 = (byte[])var5.next();
               IsoTypeWriter.writeUInt16(var1, var4.length);
               var1.put(var4);
            }
         }
      }

   }

   public long getContentSize() {
      Iterator var1 = this.sequenceParameterSets.iterator();

      long var2;
      for(var2 = 6L; var1.hasNext(); var2 = var2 + 2L + (long)((byte[])var1.next()).length) {
      }

      ++var2;

      for(var1 = this.pictureParameterSets.iterator(); var1.hasNext(); var2 = var2 + 2L + (long)((byte[])var1.next()).length) {
      }

      long var4 = var2;
      if (this.hasExts) {
         int var6 = this.avcProfileIndication;
         if (var6 != 100 && var6 != 110 && var6 != 122) {
            var4 = var2;
            if (var6 != 144) {
               return var4;
            }
         }

         var2 += 4L;

         for(var1 = this.sequenceParameterSetExts.iterator(); var1.hasNext(); var2 = var2 + 2L + (long)((byte[])var1.next()).length) {
         }

         var4 = var2;
      }

      return var4;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("AvcDecoderConfigurationRecord{configurationVersion=");
      var1.append(this.configurationVersion);
      var1.append(", avcProfileIndication=");
      var1.append(this.avcProfileIndication);
      var1.append(", profileCompatibility=");
      var1.append(this.profileCompatibility);
      var1.append(", avcLevelIndication=");
      var1.append(this.avcLevelIndication);
      var1.append(", lengthSizeMinusOne=");
      var1.append(this.lengthSizeMinusOne);
      var1.append(", hasExts=");
      var1.append(this.hasExts);
      var1.append(", chromaFormat=");
      var1.append(this.chromaFormat);
      var1.append(", bitDepthLumaMinus8=");
      var1.append(this.bitDepthLumaMinus8);
      var1.append(", bitDepthChromaMinus8=");
      var1.append(this.bitDepthChromaMinus8);
      var1.append(", lengthSizeMinusOnePaddingBits=");
      var1.append(this.lengthSizeMinusOnePaddingBits);
      var1.append(", numberOfSequenceParameterSetsPaddingBits=");
      var1.append(this.numberOfSequenceParameterSetsPaddingBits);
      var1.append(", chromaFormatPaddingBits=");
      var1.append(this.chromaFormatPaddingBits);
      var1.append(", bitDepthLumaMinus8PaddingBits=");
      var1.append(this.bitDepthLumaMinus8PaddingBits);
      var1.append(", bitDepthChromaMinus8PaddingBits=");
      var1.append(this.bitDepthChromaMinus8PaddingBits);
      var1.append('}');
      return var1.toString();
   }
}
