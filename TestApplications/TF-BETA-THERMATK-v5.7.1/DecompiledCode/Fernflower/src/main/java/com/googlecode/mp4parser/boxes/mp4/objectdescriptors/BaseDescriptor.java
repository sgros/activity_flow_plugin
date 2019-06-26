package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import java.io.IOException;
import java.nio.ByteBuffer;

@Descriptor(
   tags = {0}
)
public abstract class BaseDescriptor {
   int sizeBytes;
   int sizeOfInstance;
   int tag;

   public int getSize() {
      return this.sizeOfInstance + 1 + this.sizeBytes;
   }

   public int getSizeBytes() {
      return this.sizeBytes;
   }

   public int getSizeOfInstance() {
      return this.sizeOfInstance;
   }

   public final void parse(int var1, ByteBuffer var2) throws IOException {
      this.tag = var1;
      int var3 = IsoTypeReader.readUInt8(var2);
      this.sizeOfInstance = var3 & 127;

      for(var1 = 1; var3 >>> 7 == 1; this.sizeOfInstance = this.sizeOfInstance << 7 | var3 & 127) {
         var3 = IsoTypeReader.readUInt8(var2);
         ++var1;
      }

      this.sizeBytes = var1;
      ByteBuffer var4 = var2.slice();
      var4.limit(this.sizeOfInstance);
      this.parseDetail(var4);
      var2.position(var2.position() + this.sizeOfInstance);
   }

   public abstract void parseDetail(ByteBuffer var1) throws IOException;

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("BaseDescriptor");
      var1.append("{tag=");
      var1.append(this.tag);
      var1.append(", sizeOfInstance=");
      var1.append(this.sizeOfInstance);
      var1.append('}');
      return var1.toString();
   }
}
