package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

@Descriptor(
   tags = {5}
)
public class DecoderSpecificInfo extends BaseDescriptor {
   byte[] bytes;

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && DecoderSpecificInfo.class == var1.getClass()) {
         DecoderSpecificInfo var2 = (DecoderSpecificInfo)var1;
         return Arrays.equals(this.bytes, var2.bytes);
      } else {
         return false;
      }
   }

   public int hashCode() {
      byte[] var1 = this.bytes;
      int var2;
      if (var1 != null) {
         var2 = Arrays.hashCode(var1);
      } else {
         var2 = 0;
      }

      return var2;
   }

   public void parseDetail(ByteBuffer var1) throws IOException {
      int var2 = super.sizeOfInstance;
      if (var2 > 0) {
         this.bytes = new byte[var2];
         var1.get(this.bytes);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DecoderSpecificInfo");
      var1.append("{bytes=");
      byte[] var2 = this.bytes;
      String var3;
      if (var2 == null) {
         var3 = "null";
      } else {
         var3 = Hex.encodeHex(var2);
      }

      var1.append(var3);
      var1.append('}');
      return var1.toString();
   }
}
