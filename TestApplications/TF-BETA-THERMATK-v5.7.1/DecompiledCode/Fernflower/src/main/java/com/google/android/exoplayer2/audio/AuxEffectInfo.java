package com.google.android.exoplayer2.audio;

public final class AuxEffectInfo {
   public final int effectId;
   public final float sendLevel;

   public AuxEffectInfo(int var1, float var2) {
      this.effectId = var1;
      this.sendLevel = var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && AuxEffectInfo.class == var1.getClass()) {
         AuxEffectInfo var3 = (AuxEffectInfo)var1;
         if (this.effectId != var3.effectId || Float.compare(var3.sendLevel, this.sendLevel) != 0) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (527 + this.effectId) * 31 + Float.floatToIntBits(this.sendLevel);
   }
}
