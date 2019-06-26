package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

@Descriptor(
   tags = {6}
)
public class SLConfigDescriptor extends BaseDescriptor {
   int predefined;

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && SLConfigDescriptor.class == var1.getClass()) {
         SLConfigDescriptor var2 = (SLConfigDescriptor)var1;
         return this.predefined == var2.predefined;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.predefined;
   }

   public void parseDetail(ByteBuffer var1) throws IOException {
      this.predefined = IsoTypeReader.readUInt8(var1);
   }

   public ByteBuffer serialize() {
      ByteBuffer var1 = ByteBuffer.allocate(3);
      IsoTypeWriter.writeUInt8(var1, 6);
      IsoTypeWriter.writeUInt8(var1, 1);
      IsoTypeWriter.writeUInt8(var1, this.predefined);
      return var1;
   }

   public int serializedSize() {
      return 3;
   }

   public void setPredefined(int var1) {
      this.predefined = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("SLConfigDescriptor");
      var1.append("{predefined=");
      var1.append(this.predefined);
      var1.append('}');
      return var1.toString();
   }
}
