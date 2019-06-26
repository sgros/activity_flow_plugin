package androidx.vectordrawable.graphics.drawable;

import android.animation.TypeEvaluator;

public class ArgbEvaluator implements TypeEvaluator {
   private static final ArgbEvaluator sInstance = new ArgbEvaluator();

   public static ArgbEvaluator getInstance() {
      return sInstance;
   }

   public Object evaluate(float var1, Object var2, Object var3) {
      int var4 = (Integer)var2;
      float var5 = (float)(var4 >> 24 & 255) / 255.0F;
      float var6 = (float)(var4 >> 16 & 255) / 255.0F;
      float var7 = (float)(var4 >> 8 & 255) / 255.0F;
      float var8 = (float)(var4 & 255) / 255.0F;
      var4 = (Integer)var3;
      float var9 = (float)(var4 >> 24 & 255) / 255.0F;
      float var10 = (float)(var4 >> 16 & 255) / 255.0F;
      float var11 = (float)(var4 >> 8 & 255) / 255.0F;
      float var12 = (float)(var4 & 255) / 255.0F;
      var6 = (float)Math.pow((double)var6, 2.2D);
      var7 = (float)Math.pow((double)var7, 2.2D);
      var8 = (float)Math.pow((double)var8, 2.2D);
      var10 = (float)Math.pow((double)var10, 2.2D);
      var11 = (float)Math.pow((double)var11, 2.2D);
      var12 = (float)Math.pow((double)var12, 2.2D);
      var6 = (float)Math.pow((double)(var6 + (var10 - var6) * var1), 0.45454545454545453D);
      var7 = (float)Math.pow((double)(var7 + (var11 - var7) * var1), 0.45454545454545453D);
      var8 = (float)Math.pow((double)(var8 + var1 * (var12 - var8)), 0.45454545454545453D);
      var4 = Math.round((var5 + (var9 - var5) * var1) * 255.0F);
      return Math.round(var6 * 255.0F) << 16 | var4 << 24 | Math.round(var7 * 255.0F) << 8 | Math.round(var8 * 255.0F);
   }
}
