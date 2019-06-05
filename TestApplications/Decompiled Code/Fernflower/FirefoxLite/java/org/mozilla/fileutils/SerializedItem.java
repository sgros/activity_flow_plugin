package org.mozilla.fileutils;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class SerializedItem implements Serializable {
   private String className;
   private String key;
   private byte[] value;

   public String getClassName() {
      return this.className;
   }

   public String getKey() {
      return this.key;
   }

   public byte[] getValue() {
      byte[] var1;
      if (this.value != null) {
         var1 = ByteBuffer.wrap(this.value).array();
      } else {
         var1 = null;
      }

      return var1;
   }

   public void setClassName(String var1) {
      this.className = var1;
   }

   public void setKey(String var1) {
      this.key = var1;
   }

   public void setValue(byte[] var1) {
      if (var1 != null) {
         var1 = ByteBuffer.wrap(var1).array();
      } else {
         var1 = null;
      }

      this.value = var1;
   }
}
