package com.google.android.exoplayer2.text.ttml;

final class TtmlRegion {
   public final String id;
   public final float line;
   public final int lineAnchor;
   public final int lineType;
   public final float position;
   public final float textSize;
   public final int textSizeType;
   public final float width;

   public TtmlRegion(String var1) {
      this(var1, Float.MIN_VALUE, Float.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE);
   }

   public TtmlRegion(String var1, float var2, float var3, int var4, int var5, float var6, int var7, float var8) {
      this.id = var1;
      this.position = var2;
      this.line = var3;
      this.lineType = var4;
      this.lineAnchor = var5;
      this.width = var6;
      this.textSizeType = var7;
      this.textSize = var8;
   }
}
