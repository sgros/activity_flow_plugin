package com.google.android.exoplayer2.decoder;

public abstract class Buffer {
   private int flags;

   public final void addFlag(int var1) {
      this.flags |= var1;
   }

   public void clear() {
      this.flags = 0;
   }

   public final void clearFlag(int var1) {
      this.flags &= ~var1;
   }

   protected final boolean getFlag(int var1) {
      boolean var2;
      if ((this.flags & var1) == var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public final boolean isDecodeOnly() {
      return this.getFlag(Integer.MIN_VALUE);
   }

   public final boolean isEndOfStream() {
      return this.getFlag(4);
   }

   public final boolean isKeyFrame() {
      return this.getFlag(1);
   }

   public final void setFlags(int var1) {
      this.flags = var1;
   }
}
