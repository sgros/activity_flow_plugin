package android.support.design.widget;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Path.FillType;
import android.graphics.Shader.TileMode;
import android.support.v7.graphics.drawable.DrawableWrapper;

public class ShadowDrawableWrapper extends DrawableWrapper {
   static final double COS_45 = Math.cos(Math.toRadians(45.0D));
   private boolean addPaddingForCorners;
   final RectF contentBounds;
   float cornerRadius;
   final Paint cornerShadowPaint;
   Path cornerShadowPath;
   private boolean dirty;
   final Paint edgeShadowPaint;
   float maxShadowSize;
   private boolean printedShadowClipWarning;
   float rawMaxShadowSize;
   float rawShadowSize;
   private float rotation;
   private final int shadowEndColor;
   private final int shadowMiddleColor;
   float shadowSize;
   private final int shadowStartColor;

   private void buildComponents(Rect var1) {
      float var2 = this.rawMaxShadowSize * 1.5F;
      this.contentBounds.set((float)var1.left + this.rawMaxShadowSize, (float)var1.top + var2, (float)var1.right - this.rawMaxShadowSize, (float)var1.bottom - var2);
      this.getWrappedDrawable().setBounds((int)this.contentBounds.left, (int)this.contentBounds.top, (int)this.contentBounds.right, (int)this.contentBounds.bottom);
      this.buildShadowCorners();
   }

   private void buildShadowCorners() {
      RectF var1 = new RectF(-this.cornerRadius, -this.cornerRadius, this.cornerRadius, this.cornerRadius);
      RectF var2 = new RectF(var1);
      var2.inset(-this.shadowSize, -this.shadowSize);
      if (this.cornerShadowPath == null) {
         this.cornerShadowPath = new Path();
      } else {
         this.cornerShadowPath.reset();
      }

      this.cornerShadowPath.setFillType(FillType.EVEN_ODD);
      this.cornerShadowPath.moveTo(-this.cornerRadius, 0.0F);
      this.cornerShadowPath.rLineTo(-this.shadowSize, 0.0F);
      this.cornerShadowPath.arcTo(var2, 180.0F, 90.0F, false);
      this.cornerShadowPath.arcTo(var1, 270.0F, -90.0F, false);
      this.cornerShadowPath.close();
      float var3 = -var2.top;
      float var4;
      float var5;
      int var7;
      int var8;
      int var9;
      if (var3 > 0.0F) {
         var4 = this.cornerRadius / var3;
         var5 = (1.0F - var4) / 2.0F;
         Paint var6 = this.cornerShadowPaint;
         var7 = this.shadowStartColor;
         var8 = this.shadowMiddleColor;
         var9 = this.shadowEndColor;
         TileMode var10 = TileMode.CLAMP;
         var6.setShader(new RadialGradient(0.0F, 0.0F, var3, new int[]{0, var7, var8, var9}, new float[]{0.0F, var4, var5 + var4, 1.0F}, var10));
      }

      Paint var12 = this.edgeShadowPaint;
      var5 = var1.top;
      var4 = var2.top;
      var8 = this.shadowStartColor;
      var9 = this.shadowMiddleColor;
      var7 = this.shadowEndColor;
      TileMode var11 = TileMode.CLAMP;
      var12.setShader(new LinearGradient(0.0F, var5, 0.0F, var4, new int[]{var8, var9, var7}, new float[]{0.0F, 0.5F, 1.0F}, var11));
      this.edgeShadowPaint.setAntiAlias(false);
   }

   public static float calculateHorizontalPadding(float var0, float var1, boolean var2) {
      return var2 ? (float)((double)var0 + (1.0D - COS_45) * (double)var1) : var0;
   }

   public static float calculateVerticalPadding(float var0, float var1, boolean var2) {
      return var2 ? (float)((double)(var0 * 1.5F) + (1.0D - COS_45) * (double)var1) : var0 * 1.5F;
   }

   private void drawShadow(Canvas var1) {
      int var2 = var1.save();
      var1.rotate(this.rotation, this.contentBounds.centerX(), this.contentBounds.centerY());
      float var3 = -this.cornerRadius - this.shadowSize;
      float var4 = this.cornerRadius;
      float var5 = this.contentBounds.width();
      float var6 = var4 * 2.0F;
      boolean var7;
      if (var5 - var6 > 0.0F) {
         var7 = true;
      } else {
         var7 = false;
      }

      boolean var8;
      if (this.contentBounds.height() - var6 > 0.0F) {
         var8 = true;
      } else {
         var8 = false;
      }

      float var9 = this.rawShadowSize;
      float var10 = this.rawShadowSize;
      float var11 = this.rawShadowSize;
      float var12 = this.rawShadowSize;
      var5 = this.rawShadowSize;
      float var13 = this.rawShadowSize;
      var11 = var4 / (var11 - var12 * 0.5F + var4);
      var10 = var4 / (var9 - var10 * 0.25F + var4);
      var5 = var4 / (var5 - var13 * 1.0F + var4);
      int var14 = var1.save();
      var1.translate(this.contentBounds.left + var4, this.contentBounds.top + var4);
      var1.scale(var11, var10);
      var1.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
      if (var7) {
         var1.scale(1.0F / var11, 1.0F);
         var1.drawRect(0.0F, var3, this.contentBounds.width() - var6, -this.cornerRadius, this.edgeShadowPaint);
      }

      var1.restoreToCount(var14);
      var14 = var1.save();
      var1.translate(this.contentBounds.right - var4, this.contentBounds.bottom - var4);
      var1.scale(var11, var5);
      var1.rotate(180.0F);
      var1.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
      if (var7) {
         var1.scale(1.0F / var11, 1.0F);
         var1.drawRect(0.0F, var3, this.contentBounds.width() - var6, -this.cornerRadius + this.shadowSize, this.edgeShadowPaint);
      }

      var1.restoreToCount(var14);
      int var15 = var1.save();
      var1.translate(this.contentBounds.left + var4, this.contentBounds.bottom - var4);
      var1.scale(var11, var5);
      var1.rotate(270.0F);
      var1.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
      if (var8) {
         var1.scale(1.0F / var5, 1.0F);
         var1.drawRect(0.0F, var3, this.contentBounds.height() - var6, -this.cornerRadius, this.edgeShadowPaint);
      }

      var1.restoreToCount(var15);
      var15 = var1.save();
      var1.translate(this.contentBounds.right - var4, this.contentBounds.top + var4);
      var1.scale(var11, var10);
      var1.rotate(90.0F);
      var1.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
      if (var8) {
         var1.scale(1.0F / var10, 1.0F);
         var1.drawRect(0.0F, var3, this.contentBounds.height() - var6, -this.cornerRadius, this.edgeShadowPaint);
      }

      var1.restoreToCount(var15);
      var1.restoreToCount(var2);
   }

   private static int toEven(float var0) {
      int var1 = Math.round(var0);
      int var2 = var1;
      if (var1 % 2 == 1) {
         var2 = var1 - 1;
      }

      return var2;
   }

   public void draw(Canvas var1) {
      if (this.dirty) {
         this.buildComponents(this.getBounds());
         this.dirty = false;
      }

      this.drawShadow(var1);
      super.draw(var1);
   }

   public int getOpacity() {
      return -3;
   }

   public boolean getPadding(Rect var1) {
      int var2 = (int)Math.ceil((double)calculateVerticalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
      int var3 = (int)Math.ceil((double)calculateHorizontalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
      var1.set(var3, var2, var3, var2);
      return true;
   }

   public float getShadowSize() {
      return this.rawShadowSize;
   }

   protected void onBoundsChange(Rect var1) {
      this.dirty = true;
   }

   public void setAlpha(int var1) {
      super.setAlpha(var1);
      this.cornerShadowPaint.setAlpha(var1);
      this.edgeShadowPaint.setAlpha(var1);
   }

   public final void setRotation(float var1) {
      if (this.rotation != var1) {
         this.rotation = var1;
         this.invalidateSelf();
      }

   }

   public void setShadowSize(float var1) {
      this.setShadowSize(var1, this.rawMaxShadowSize);
   }

   public void setShadowSize(float var1, float var2) {
      if (var1 >= 0.0F && var2 >= 0.0F) {
         float var3 = (float)toEven(var1);
         var2 = (float)toEven(var2);
         var1 = var3;
         if (var3 > var2) {
            if (!this.printedShadowClipWarning) {
               this.printedShadowClipWarning = true;
            }

            var1 = var2;
         }

         if (this.rawShadowSize != var1 || this.rawMaxShadowSize != var2) {
            this.rawShadowSize = var1;
            this.rawMaxShadowSize = var2;
            this.shadowSize = (float)Math.round(var1 * 1.5F);
            this.maxShadowSize = var2;
            this.dirty = true;
            this.invalidateSelf();
         }
      } else {
         throw new IllegalArgumentException("invalid shadow size");
      }
   }
}
