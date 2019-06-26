package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Descriptor(
   tags = {3}
)
public class ESDescriptor extends BaseDescriptor {
   private static Logger log = Logger.getLogger(ESDescriptor.class.getName());
   int URLFlag;
   int URLLength = 0;
   String URLString;
   DecoderConfigDescriptor decoderConfigDescriptor;
   int dependsOnEsId;
   int esId;
   int oCREsId;
   int oCRstreamFlag;
   List otherDescriptors = new ArrayList();
   int remoteODFlag;
   SLConfigDescriptor slConfigDescriptor;
   int streamDependenceFlag;
   int streamPriority;

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && ESDescriptor.class == var1.getClass()) {
         ESDescriptor var3 = (ESDescriptor)var1;
         if (this.URLFlag != var3.URLFlag) {
            return false;
         } else if (this.URLLength != var3.URLLength) {
            return false;
         } else if (this.dependsOnEsId != var3.dependsOnEsId) {
            return false;
         } else if (this.esId != var3.esId) {
            return false;
         } else if (this.oCREsId != var3.oCREsId) {
            return false;
         } else if (this.oCRstreamFlag != var3.oCRstreamFlag) {
            return false;
         } else if (this.remoteODFlag != var3.remoteODFlag) {
            return false;
         } else if (this.streamDependenceFlag != var3.streamDependenceFlag) {
            return false;
         } else if (this.streamPriority != var3.streamPriority) {
            return false;
         } else {
            label81: {
               String var2 = this.URLString;
               if (var2 != null) {
                  if (var2.equals(var3.URLString)) {
                     break label81;
                  }
               } else if (var3.URLString == null) {
                  break label81;
               }

               return false;
            }

            DecoderConfigDescriptor var4 = this.decoderConfigDescriptor;
            if (var4 != null) {
               if (!var4.equals(var3.decoderConfigDescriptor)) {
                  return false;
               }
            } else if (var3.decoderConfigDescriptor != null) {
               return false;
            }

            List var5 = this.otherDescriptors;
            if (var5 != null) {
               if (!var5.equals(var3.otherDescriptors)) {
                  return false;
               }
            } else if (var3.otherDescriptors != null) {
               return false;
            }

            SLConfigDescriptor var6 = this.slConfigDescriptor;
            if (var6 != null) {
               if (!var6.equals(var3.slConfigDescriptor)) {
                  return false;
               }
            } else if (var3.slConfigDescriptor != null) {
               return false;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.esId;
      int var2 = this.streamDependenceFlag;
      int var3 = this.URLFlag;
      int var4 = this.oCRstreamFlag;
      int var5 = this.streamPriority;
      int var6 = this.URLLength;
      String var7 = this.URLString;
      int var8 = 0;
      int var9;
      if (var7 != null) {
         var9 = var7.hashCode();
      } else {
         var9 = 0;
      }

      int var10 = this.remoteODFlag;
      int var11 = this.dependsOnEsId;
      int var12 = this.oCREsId;
      DecoderConfigDescriptor var15 = this.decoderConfigDescriptor;
      int var13;
      if (var15 != null) {
         var13 = var15.hashCode();
      } else {
         var13 = 0;
      }

      SLConfigDescriptor var16 = this.slConfigDescriptor;
      int var14;
      if (var16 != null) {
         var14 = var16.hashCode();
      } else {
         var14 = 0;
      }

      List var17 = this.otherDescriptors;
      if (var17 != null) {
         var8 = var17.hashCode();
      }

      return (((((((((((var1 * 31 + var2) * 31 + var3) * 31 + var4) * 31 + var5) * 31 + var6) * 31 + var9) * 31 + var10) * 31 + var11) * 31 + var12) * 31 + var13) * 31 + var14) * 31 + var8;
   }

   public void parseDetail(ByteBuffer var1) throws IOException {
      this.esId = IsoTypeReader.readUInt16(var1);
      int var2 = IsoTypeReader.readUInt8(var1);
      this.streamDependenceFlag = var2 >>> 7;
      this.URLFlag = var2 >>> 6 & 1;
      this.oCRstreamFlag = var2 >>> 5 & 1;
      this.streamPriority = var2 & 31;
      if (this.streamDependenceFlag == 1) {
         this.dependsOnEsId = IsoTypeReader.readUInt16(var1);
      }

      if (this.URLFlag == 1) {
         this.URLLength = IsoTypeReader.readUInt8(var1);
         this.URLString = IsoTypeReader.readString(var1, this.URLLength);
      }

      if (this.oCRstreamFlag == 1) {
         this.oCREsId = IsoTypeReader.readUInt16(var1);
      }

      int var3 = this.getSizeBytes();
      var2 = this.streamDependenceFlag;
      byte var4 = 0;
      byte var12;
      if (var2 == 1) {
         var12 = 2;
      } else {
         var12 = 0;
      }

      int var5;
      if (this.URLFlag == 1) {
         var5 = this.URLLength + 1;
      } else {
         var5 = 0;
      }

      if (this.oCRstreamFlag == 1) {
         var4 = 2;
      }

      var5 = var3 + 1 + 2 + 1 + var12 + var5 + var4;
      int var13 = var1.position();
      var2 = var5;
      long var7;
      Logger var9;
      Integer var11;
      if (this.getSize() > var5 + 2) {
         BaseDescriptor var6 = ObjectDescriptorFactory.createFrom(-1, var1);
         var7 = (long)(var1.position() - var13);
         var9 = log;
         StringBuilder var10 = new StringBuilder();
         var10.append(var6);
         var10.append(" - ESDescriptor1 read: ");
         var10.append(var7);
         var10.append(", size: ");
         if (var6 != null) {
            var11 = var6.getSize();
         } else {
            var11 = null;
         }

         var10.append(var11);
         var9.finer(var10.toString());
         if (var6 != null) {
            var2 = var6.getSize();
            var1.position(var13 + var2);
            var5 += var2;
         } else {
            var5 = (int)((long)var5 + var7);
         }

         var2 = var5;
         if (var6 instanceof DecoderConfigDescriptor) {
            this.decoderConfigDescriptor = (DecoderConfigDescriptor)var6;
            var2 = var5;
         }
      }

      var5 = var1.position();
      BaseDescriptor var17;
      if (this.getSize() > var2 + 2) {
         var17 = ObjectDescriptorFactory.createFrom(-1, var1);
         var7 = (long)(var1.position() - var5);
         var9 = log;
         StringBuilder var14 = new StringBuilder();
         var14.append(var17);
         var14.append(" - ESDescriptor2 read: ");
         var14.append(var7);
         var14.append(", size: ");
         if (var17 != null) {
            var11 = var17.getSize();
         } else {
            var11 = null;
         }

         var14.append(var11);
         var9.finer(var14.toString());
         if (var17 != null) {
            var13 = var17.getSize();
            var1.position(var5 + var13);
            var5 = var2 + var13;
         } else {
            var5 = (int)((long)var2 + var7);
         }

         var2 = var5;
         if (var17 instanceof SLConfigDescriptor) {
            this.slConfigDescriptor = (SLConfigDescriptor)var17;
            var2 = var5;
         }
      } else {
         log.warning("SLConfigDescriptor is missing!");
      }

      for(; this.getSize() - var2 > 2; this.otherDescriptors.add(var17)) {
         var5 = var1.position();
         var17 = ObjectDescriptorFactory.createFrom(-1, var1);
         var7 = (long)(var1.position() - var5);
         Logger var15 = log;
         StringBuilder var16 = new StringBuilder();
         var16.append(var17);
         var16.append(" - ESDescriptor3 read: ");
         var16.append(var7);
         var16.append(", size: ");
         if (var17 != null) {
            var11 = var17.getSize();
         } else {
            var11 = null;
         }

         var16.append(var11);
         var15.finer(var16.toString());
         if (var17 != null) {
            var13 = var17.getSize();
            var1.position(var5 + var13);
            var2 += var13;
         } else {
            var2 = (int)((long)var2 + var7);
         }
      }

   }

   public ByteBuffer serialize() {
      ByteBuffer var1 = ByteBuffer.allocate(this.serializedSize());
      IsoTypeWriter.writeUInt8(var1, 3);
      IsoTypeWriter.writeUInt8(var1, this.serializedSize() - 2);
      IsoTypeWriter.writeUInt16(var1, this.esId);
      IsoTypeWriter.writeUInt8(var1, this.streamDependenceFlag << 7 | this.URLFlag << 6 | this.oCRstreamFlag << 5 | this.streamPriority & 31);
      if (this.streamDependenceFlag > 0) {
         IsoTypeWriter.writeUInt16(var1, this.dependsOnEsId);
      }

      if (this.URLFlag > 0) {
         IsoTypeWriter.writeUInt8(var1, this.URLLength);
         IsoTypeWriter.writeUtf8String(var1, this.URLString);
      }

      if (this.oCRstreamFlag > 0) {
         IsoTypeWriter.writeUInt16(var1, this.oCREsId);
      }

      ByteBuffer var2 = this.decoderConfigDescriptor.serialize();
      ByteBuffer var3 = this.slConfigDescriptor.serialize();
      var1.put(var2.array());
      var1.put(var3.array());
      return var1;
   }

   public int serializedSize() {
      byte var1;
      if (this.streamDependenceFlag > 0) {
         var1 = 7;
      } else {
         var1 = 5;
      }

      int var2 = var1;
      if (this.URLFlag > 0) {
         var2 = var1 + this.URLLength + 1;
      }

      int var3 = var2;
      if (this.oCRstreamFlag > 0) {
         var3 = var2 + 2;
      }

      return var3 + this.decoderConfigDescriptor.serializedSize() + this.slConfigDescriptor.serializedSize();
   }

   public void setDecoderConfigDescriptor(DecoderConfigDescriptor var1) {
      this.decoderConfigDescriptor = var1;
   }

   public void setEsId(int var1) {
      this.esId = var1;
   }

   public void setSlConfigDescriptor(SLConfigDescriptor var1) {
      this.slConfigDescriptor = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ESDescriptor");
      var1.append("{esId=");
      var1.append(this.esId);
      var1.append(", streamDependenceFlag=");
      var1.append(this.streamDependenceFlag);
      var1.append(", URLFlag=");
      var1.append(this.URLFlag);
      var1.append(", oCRstreamFlag=");
      var1.append(this.oCRstreamFlag);
      var1.append(", streamPriority=");
      var1.append(this.streamPriority);
      var1.append(", URLLength=");
      var1.append(this.URLLength);
      var1.append(", URLString='");
      var1.append(this.URLString);
      var1.append('\'');
      var1.append(", remoteODFlag=");
      var1.append(this.remoteODFlag);
      var1.append(", dependsOnEsId=");
      var1.append(this.dependsOnEsId);
      var1.append(", oCREsId=");
      var1.append(this.oCREsId);
      var1.append(", decoderConfigDescriptor=");
      var1.append(this.decoderConfigDescriptor);
      var1.append(", slConfigDescriptor=");
      var1.append(this.slConfigDescriptor);
      var1.append('}');
      return var1.toString();
   }
}
