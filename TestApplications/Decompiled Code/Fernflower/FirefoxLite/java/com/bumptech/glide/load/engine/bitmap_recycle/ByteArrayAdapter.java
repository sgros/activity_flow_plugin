package com.bumptech.glide.load.engine.bitmap_recycle;

public final class ByteArrayAdapter implements ArrayAdapterInterface {
   public int getArrayLength(byte[] var1) {
      return var1.length;
   }

   public int getElementSizeInBytes() {
      return 1;
   }

   public String getTag() {
      return "ByteArrayPool";
   }

   public byte[] newArray(int var1) {
      return new byte[var1];
   }
}
