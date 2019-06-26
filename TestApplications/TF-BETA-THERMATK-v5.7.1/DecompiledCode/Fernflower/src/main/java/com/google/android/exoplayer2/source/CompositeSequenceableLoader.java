package com.google.android.exoplayer2.source;

public class CompositeSequenceableLoader implements SequenceableLoader {
   protected final SequenceableLoader[] loaders;

   public CompositeSequenceableLoader(SequenceableLoader[] var1) {
      this.loaders = var1;
   }

   public boolean continueLoading(long var1) {
      boolean var3 = false;

      while(true) {
         long var4 = this.getNextLoadPositionUs();
         if (var4 == Long.MIN_VALUE) {
            break;
         }

         SequenceableLoader[] var6 = this.loaders;
         int var7 = var6.length;
         int var8 = 0;

         boolean var9;
         boolean var14;
         for(var9 = false; var8 < var7; var9 = var14) {
            SequenceableLoader var10 = var6[var8];
            long var11 = var10.getNextLoadPositionUs();
            boolean var13;
            if (var11 != Long.MIN_VALUE && var11 <= var1) {
               var13 = true;
            } else {
               var13 = false;
            }

            label34: {
               if (var11 != var4) {
                  var14 = var9;
                  if (!var13) {
                     break label34;
                  }
               }

               var14 = var9 | var10.continueLoading(var1);
            }

            ++var8;
         }

         boolean var15 = var3 | var9;
         var3 = var15;
         if (!var9) {
            var3 = var15;
            break;
         }
      }

      return var3;
   }

   public final long getBufferedPositionUs() {
      SequenceableLoader[] var1 = this.loaders;
      int var2 = var1.length;
      int var3 = 0;

      long var4;
      long var8;
      for(var4 = Long.MAX_VALUE; var3 < var2; var4 = var8) {
         long var6 = var1[var3].getBufferedPositionUs();
         var8 = var4;
         if (var6 != Long.MIN_VALUE) {
            var8 = Math.min(var4, var6);
         }

         ++var3;
      }

      var8 = var4;
      if (var4 == Long.MAX_VALUE) {
         var8 = Long.MIN_VALUE;
      }

      return var8;
   }

   public final long getNextLoadPositionUs() {
      SequenceableLoader[] var1 = this.loaders;
      int var2 = var1.length;
      int var3 = 0;

      long var4;
      long var8;
      for(var4 = Long.MAX_VALUE; var3 < var2; var4 = var8) {
         long var6 = var1[var3].getNextLoadPositionUs();
         var8 = var4;
         if (var6 != Long.MIN_VALUE) {
            var8 = Math.min(var4, var6);
         }

         ++var3;
      }

      var8 = var4;
      if (var4 == Long.MAX_VALUE) {
         var8 = Long.MIN_VALUE;
      }

      return var8;
   }

   public final void reevaluateBuffer(long var1) {
      SequenceableLoader[] var3 = this.loaders;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         var3[var5].reevaluateBuffer(var1);
      }

   }
}
