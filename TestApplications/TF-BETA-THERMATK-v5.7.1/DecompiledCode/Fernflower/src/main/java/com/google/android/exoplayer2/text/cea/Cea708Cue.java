package com.google.android.exoplayer2.text.cea;

import android.text.Layout.Alignment;
import com.google.android.exoplayer2.text.Cue;

final class Cea708Cue extends Cue implements Comparable {
   public final int priority;

   public Cea708Cue(CharSequence var1, Alignment var2, float var3, int var4, int var5, float var6, int var7, float var8, boolean var9, int var10, int var11) {
      super(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      this.priority = var11;
   }

   public int compareTo(Cea708Cue var1) {
      int var2 = var1.priority;
      int var3 = this.priority;
      if (var2 < var3) {
         return -1;
      } else {
         return var2 > var3 ? 1 : 0;
      }
   }
}
