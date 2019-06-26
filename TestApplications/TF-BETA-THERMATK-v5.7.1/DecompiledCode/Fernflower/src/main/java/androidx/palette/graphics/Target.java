package androidx.palette.graphics;

public final class Target {
   public static final Target DARK_MUTED;
   public static final Target DARK_VIBRANT;
   public static final Target LIGHT_MUTED;
   public static final Target LIGHT_VIBRANT = new Target();
   public static final Target MUTED;
   public static final Target VIBRANT;
   boolean mIsExclusive = true;
   final float[] mLightnessTargets = new float[3];
   final float[] mSaturationTargets = new float[3];
   final float[] mWeights = new float[3];

   static {
      setDefaultLightLightnessValues(LIGHT_VIBRANT);
      setDefaultVibrantSaturationValues(LIGHT_VIBRANT);
      VIBRANT = new Target();
      setDefaultNormalLightnessValues(VIBRANT);
      setDefaultVibrantSaturationValues(VIBRANT);
      DARK_VIBRANT = new Target();
      setDefaultDarkLightnessValues(DARK_VIBRANT);
      setDefaultVibrantSaturationValues(DARK_VIBRANT);
      LIGHT_MUTED = new Target();
      setDefaultLightLightnessValues(LIGHT_MUTED);
      setDefaultMutedSaturationValues(LIGHT_MUTED);
      MUTED = new Target();
      setDefaultNormalLightnessValues(MUTED);
      setDefaultMutedSaturationValues(MUTED);
      DARK_MUTED = new Target();
      setDefaultDarkLightnessValues(DARK_MUTED);
      setDefaultMutedSaturationValues(DARK_MUTED);
   }

   Target() {
      setTargetDefaultValues(this.mSaturationTargets);
      setTargetDefaultValues(this.mLightnessTargets);
      this.setDefaultWeights();
   }

   private static void setDefaultDarkLightnessValues(Target var0) {
      float[] var1 = var0.mLightnessTargets;
      var1[1] = 0.26F;
      var1[2] = 0.45F;
   }

   private static void setDefaultLightLightnessValues(Target var0) {
      float[] var1 = var0.mLightnessTargets;
      var1[0] = 0.55F;
      var1[1] = 0.74F;
   }

   private static void setDefaultMutedSaturationValues(Target var0) {
      float[] var1 = var0.mSaturationTargets;
      var1[1] = 0.3F;
      var1[2] = 0.4F;
   }

   private static void setDefaultNormalLightnessValues(Target var0) {
      float[] var1 = var0.mLightnessTargets;
      var1[0] = 0.3F;
      var1[1] = 0.5F;
      var1[2] = 0.7F;
   }

   private static void setDefaultVibrantSaturationValues(Target var0) {
      float[] var1 = var0.mSaturationTargets;
      var1[0] = 0.35F;
      var1[1] = 1.0F;
   }

   private void setDefaultWeights() {
      float[] var1 = this.mWeights;
      var1[0] = 0.24F;
      var1[1] = 0.52F;
      var1[2] = 0.24F;
   }

   private static void setTargetDefaultValues(float[] var0) {
      var0[0] = 0.0F;
      var0[1] = 0.5F;
      var0[2] = 1.0F;
   }

   public float getLightnessWeight() {
      return this.mWeights[1];
   }

   public float getMaximumLightness() {
      return this.mLightnessTargets[2];
   }

   public float getMaximumSaturation() {
      return this.mSaturationTargets[2];
   }

   public float getMinimumLightness() {
      return this.mLightnessTargets[0];
   }

   public float getMinimumSaturation() {
      return this.mSaturationTargets[0];
   }

   public float getPopulationWeight() {
      return this.mWeights[2];
   }

   public float getSaturationWeight() {
      return this.mWeights[0];
   }

   public float getTargetLightness() {
      return this.mLightnessTargets[1];
   }

   public float getTargetSaturation() {
      return this.mSaturationTargets[1];
   }

   public boolean isExclusive() {
      return this.mIsExclusive;
   }

   void normalizeWeights() {
      int var1 = this.mWeights.length;
      byte var2 = 0;
      int var3 = 0;

      float var4;
      float var6;
      for(var4 = 0.0F; var3 < var1; var4 = var6) {
         float var5 = this.mWeights[var3];
         var6 = var4;
         if (var5 > 0.0F) {
            var6 = var4 + var5;
         }

         ++var3;
      }

      if (var4 != 0.0F) {
         var1 = this.mWeights.length;

         for(var3 = var2; var3 < var1; ++var3) {
            float[] var7 = this.mWeights;
            if (var7[var3] > 0.0F) {
               var7[var3] /= var4;
            }
         }
      }

   }
}
