package org.telegram.tgnet;

public class TLObject {
   private static final ThreadLocal sizeCalculator = new ThreadLocal() {
      protected NativeByteBuffer initialValue() {
         return new NativeByteBuffer(true);
      }
   };
   public boolean disableFree = false;
   public int networkType;

   public TLObject deserializeResponse(AbstractSerializedData var1, int var2, boolean var3) {
      return null;
   }

   public void freeResources() {
   }

   public int getObjectSize() {
      NativeByteBuffer var1 = (NativeByteBuffer)sizeCalculator.get();
      var1.rewind();
      this.serializeToStream((AbstractSerializedData)sizeCalculator.get());
      return var1.length();
   }

   public void readParams(AbstractSerializedData var1, boolean var2) {
   }

   public void serializeToStream(AbstractSerializedData var1) {
   }
}
