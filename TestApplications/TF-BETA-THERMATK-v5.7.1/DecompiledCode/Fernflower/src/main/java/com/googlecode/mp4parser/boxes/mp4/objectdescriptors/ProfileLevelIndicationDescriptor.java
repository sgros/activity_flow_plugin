package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import java.io.IOException;
import java.nio.ByteBuffer;

@Descriptor(
   tags = {20}
)
public class ProfileLevelIndicationDescriptor extends BaseDescriptor {
   int profileLevelIndicationIndex;

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && ProfileLevelIndicationDescriptor.class == var1.getClass()) {
         ProfileLevelIndicationDescriptor var2 = (ProfileLevelIndicationDescriptor)var1;
         return this.profileLevelIndicationIndex == var2.profileLevelIndicationIndex;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.profileLevelIndicationIndex;
   }

   public void parseDetail(ByteBuffer var1) throws IOException {
      this.profileLevelIndicationIndex = IsoTypeReader.readUInt8(var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ProfileLevelIndicationDescriptor");
      var1.append("{profileLevelIndicationIndex=");
      var1.append(Integer.toHexString(this.profileLevelIndicationIndex));
      var1.append('}');
      return var1.toString();
   }
}
