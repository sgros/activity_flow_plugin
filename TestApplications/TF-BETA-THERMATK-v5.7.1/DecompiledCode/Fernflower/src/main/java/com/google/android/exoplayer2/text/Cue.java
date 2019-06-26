package com.google.android.exoplayer2.text;

import android.graphics.Bitmap;
import android.text.Layout.Alignment;

public class Cue {
   public final Bitmap bitmap;
   public final float bitmapHeight;
   public final float line;
   public final int lineAnchor;
   public final int lineType;
   public final float position;
   public final int positionAnchor;
   public final float size;
   public final CharSequence text;
   public final Alignment textAlignment;
   public final float textSize;
   public final int textSizeType;
   public final int windowColor;
   public final boolean windowColorSet;

   public Cue(Bitmap var1, float var2, int var3, float var4, int var5, float var6, float var7) {
      this((CharSequence)null, (Alignment)null, var1, var4, 0, var5, var2, var3, Integer.MIN_VALUE, Float.MIN_VALUE, var6, var7, false, -16777216);
   }

   public Cue(CharSequence var1) {
      this(var1, (Alignment)null, Float.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE);
   }

   public Cue(CharSequence var1, Alignment var2, float var3, int var4, int var5, float var6, int var7, float var8) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, false, -16777216);
   }

   public Cue(CharSequence var1, Alignment var2, float var3, int var4, int var5, float var6, int var7, float var8, int var9, float var10) {
      this(var1, var2, (Bitmap)null, var3, var4, var5, var6, var7, var9, var10, var8, Float.MIN_VALUE, false, -16777216);
   }

   public Cue(CharSequence var1, Alignment var2, float var3, int var4, int var5, float var6, int var7, float var8, boolean var9, int var10) {
      this(var1, var2, (Bitmap)null, var3, var4, var5, var6, var7, Integer.MIN_VALUE, Float.MIN_VALUE, var8, Float.MIN_VALUE, var9, var10);
   }

   private Cue(CharSequence var1, Alignment var2, Bitmap var3, float var4, int var5, int var6, float var7, int var8, int var9, float var10, float var11, float var12, boolean var13, int var14) {
      this.text = var1;
      this.textAlignment = var2;
      this.bitmap = var3;
      this.line = var4;
      this.lineType = var5;
      this.lineAnchor = var6;
      this.position = var7;
      this.positionAnchor = var8;
      this.size = var11;
      this.bitmapHeight = var12;
      this.windowColorSet = var13;
      this.windowColor = var14;
      this.textSizeType = var9;
      this.textSize = var10;
   }
}
