package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Descriptor(
   tags = {4}
)
public class DecoderConfigDescriptor extends BaseDescriptor {
   private static Logger log = Logger.getLogger(DecoderConfigDescriptor.class.getName());
   AudioSpecificConfig audioSpecificInfo;
   long avgBitRate;
   int bufferSizeDB;
   byte[] configDescriptorDeadBytes;
   DecoderSpecificInfo decoderSpecificInfo;
   long maxBitRate;
   int objectTypeIndication;
   List profileLevelIndicationDescriptors = new ArrayList();
   int streamType;
   int upStream;

   public void parseDetail(ByteBuffer var1) throws IOException {
      this.objectTypeIndication = IsoTypeReader.readUInt8(var1);
      int var2 = IsoTypeReader.readUInt8(var1);
      this.streamType = var2 >>> 2;
      this.upStream = var2 >> 1 & 1;
      this.bufferSizeDB = IsoTypeReader.readUInt24(var1);
      this.maxBitRate = IsoTypeReader.readUInt32(var1);
      this.avgBitRate = IsoTypeReader.readUInt32(var1);
      Integer var7;
      if (var1.remaining() > 2) {
         var2 = var1.position();
         BaseDescriptor var3 = ObjectDescriptorFactory.createFrom(this.objectTypeIndication, var1);
         int var4 = var1.position() - var2;
         Logger var5 = log;
         StringBuilder var6 = new StringBuilder();
         var6.append(var3);
         var6.append(" - DecoderConfigDescr1 read: ");
         var6.append(var4);
         var6.append(", size: ");
         if (var3 != null) {
            var7 = var3.getSize();
         } else {
            var7 = null;
         }

         var6.append(var7);
         var5.finer(var6.toString());
         if (var3 != null) {
            var2 = var3.getSize();
            if (var4 < var2) {
               this.configDescriptorDeadBytes = new byte[var2 - var4];
               var1.get(this.configDescriptorDeadBytes);
            }
         }

         if (var3 instanceof DecoderSpecificInfo) {
            this.decoderSpecificInfo = (DecoderSpecificInfo)var3;
         }

         if (var3 instanceof AudioSpecificConfig) {
            this.audioSpecificInfo = (AudioSpecificConfig)var3;
         }
      }

      while(var1.remaining() > 2) {
         long var8 = (long)var1.position();
         BaseDescriptor var13 = ObjectDescriptorFactory.createFrom(this.objectTypeIndication, var1);
         long var10 = (long)var1.position();
         Logger var14 = log;
         StringBuilder var12 = new StringBuilder();
         var12.append(var13);
         var12.append(" - DecoderConfigDescr2 read: ");
         var12.append(var10 - var8);
         var12.append(", size: ");
         if (var13 != null) {
            var7 = var13.getSize();
         } else {
            var7 = null;
         }

         var12.append(var7);
         var14.finer(var12.toString());
         if (var13 instanceof ProfileLevelIndicationDescriptor) {
            this.profileLevelIndicationDescriptors.add((ProfileLevelIndicationDescriptor)var13);
         }
      }

   }

   public ByteBuffer serialize() {
      ByteBuffer var1 = ByteBuffer.allocate(this.serializedSize());
      IsoTypeWriter.writeUInt8(var1, 4);
      IsoTypeWriter.writeUInt8(var1, this.serializedSize() - 2);
      IsoTypeWriter.writeUInt8(var1, this.objectTypeIndication);
      IsoTypeWriter.writeUInt8(var1, this.streamType << 2 | this.upStream << 1 | 1);
      IsoTypeWriter.writeUInt24(var1, this.bufferSizeDB);
      IsoTypeWriter.writeUInt32(var1, this.maxBitRate);
      IsoTypeWriter.writeUInt32(var1, this.avgBitRate);
      AudioSpecificConfig var2 = this.audioSpecificInfo;
      if (var2 != null) {
         var1.put(var2.serialize().array());
      }

      return var1;
   }

   public int serializedSize() {
      AudioSpecificConfig var1 = this.audioSpecificInfo;
      int var2;
      if (var1 == null) {
         var2 = 0;
      } else {
         var2 = var1.serializedSize();
      }

      return var2 + 15;
   }

   public void setAudioSpecificInfo(AudioSpecificConfig var1) {
      this.audioSpecificInfo = var1;
   }

   public void setAvgBitRate(long var1) {
      this.avgBitRate = var1;
   }

   public void setBufferSizeDB(int var1) {
      this.bufferSizeDB = var1;
   }

   public void setMaxBitRate(long var1) {
      this.maxBitRate = var1;
   }

   public void setObjectTypeIndication(int var1) {
      this.objectTypeIndication = var1;
   }

   public void setStreamType(int var1) {
      this.streamType = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DecoderConfigDescriptor");
      var1.append("{objectTypeIndication=");
      var1.append(this.objectTypeIndication);
      var1.append(", streamType=");
      var1.append(this.streamType);
      var1.append(", upStream=");
      var1.append(this.upStream);
      var1.append(", bufferSizeDB=");
      var1.append(this.bufferSizeDB);
      var1.append(", maxBitRate=");
      var1.append(this.maxBitRate);
      var1.append(", avgBitRate=");
      var1.append(this.avgBitRate);
      var1.append(", decoderSpecificInfo=");
      var1.append(this.decoderSpecificInfo);
      var1.append(", audioSpecificInfo=");
      var1.append(this.audioSpecificInfo);
      var1.append(", configDescriptorDeadBytes=");
      byte[] var2 = this.configDescriptorDeadBytes;
      if (var2 == null) {
         var2 = new byte[0];
      }

      var1.append(Hex.encodeHex(var2));
      var1.append(", profileLevelIndicationDescriptors=");
      List var3 = this.profileLevelIndicationDescriptors;
      String var4;
      if (var3 == null) {
         var4 = "null";
      } else {
         var4 = Arrays.asList(var3).toString();
      }

      var1.append(var4);
      var1.append('}');
      return var1.toString();
   }
}
