package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class UnknownDescriptor extends BaseDescriptor {
   private static Logger log = Logger.getLogger(UnknownDescriptor.class.getName());
   private ByteBuffer data;

   public void parseDetail(ByteBuffer var1) throws IOException {
      this.data = (ByteBuffer)var1.slice().limit(this.getSizeOfInstance());
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("UnknownDescriptor");
      var1.append("{tag=");
      var1.append(super.tag);
      var1.append(", sizeOfInstance=");
      var1.append(super.sizeOfInstance);
      var1.append(", data=");
      var1.append(this.data);
      var1.append('}');
      return var1.toString();
   }
}
