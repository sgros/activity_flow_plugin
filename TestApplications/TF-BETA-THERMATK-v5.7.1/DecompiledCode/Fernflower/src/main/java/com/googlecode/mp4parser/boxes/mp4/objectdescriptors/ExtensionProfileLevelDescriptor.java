package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import java.io.IOException;
import java.nio.ByteBuffer;

@Descriptor(
   tags = {19}
)
public class ExtensionProfileLevelDescriptor extends BaseDescriptor {
   byte[] bytes;

   public void parseDetail(ByteBuffer var1) throws IOException {
      if (this.getSize() > 0) {
         this.bytes = new byte[this.getSize()];
         var1.get(this.bytes);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ExtensionDescriptor");
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
