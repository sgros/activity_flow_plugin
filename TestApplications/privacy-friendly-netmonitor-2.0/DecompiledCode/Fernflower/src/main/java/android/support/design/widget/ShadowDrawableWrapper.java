package android.support.design.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.Path.FillType;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.support.design.R;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;

class ShadowDrawableWrapper extends DrawableWrapper {
   static final double COS_45 = Math.cos(Math.toRadians(45.0D));
   static final float SHADOW_BOTTOM_SCALE = 1.0F;
   static final float SHADOW_HORIZ_SCALE = 0.5F;
   static final float SHADOW_MULTIPLIER = 1.5F;
   static final float SHADOW_TOP_SCALE = 0.25F;
   private boolean mAddPaddingForCorners = true;
   final RectF mContentBounds;
   float mCornerRadius;
   final Paint mCornerShadowPaint;
   Path mCornerShadowPath;
   private boolean mDirty = true;
   final Paint mEdgeShadowPaint;
   float mMaxShadowSize;
   private boolean mPrintedShadowClipWarning = false;
   float mRawMaxShadowSize;
   float mRawShadowSize;
   private float mRotation;
   private final int mShadowEndColor;
   private final int mShadowMiddleColor;
   float mShadowSize;
   private final int mShadowStartColor;

   public ShadowDrawableWrapper(Context var1, Drawable var2, float var3, float var4, float var5) {
      super(var2);
      this.mShadowStartColor = ContextCompat.getColor(var1, R.color.design_fab_shadow_start_color);
      this.mShadowMiddleColor = ContextCompat.getColor(var1, R.color.design_fab_shadow_mid_color);
      this.mShadowEndColor = ContextCompat.getColor(var1, R.color.design_fab_shadow_end_color);
      this.mCornerShadowPaint = new Paint(5);
      this.mCornerShadowPaint.setStyle(Style.FILL);
      this.mCornerRadius = (float)Math.round(var3);
      this.mContentBounds = new RectF();
      this.mEdgeShadowPaint = new Paint(this.mCornerShadowPaint);
      this.mEdgeShadowPaint.setAntiAlias(false);
      this.setShadowSize(var4, var5);
   }

   private void buildComponents(Rect var1) {
      float var2 = this.mRawMaxShadowSize * 1.5F;
      this.mContentBounds.set((float)var1.left + this.mRawMaxShadowSize, (float)var1.top + var2, (float)var1.right - this.mRawMaxShadowSize, (float)var1.bottom - var2);
      this.getWrappedDrawable().setBounds((int)this.mContentBounds.left, (int)this.mContentBounds.top, (int)this.mContentBounds.right, (int)this.mContentBounds.bottom);
      this.buildShadowCorners();
   }

   private void buildShadowCorners() {
      RectF var1 = new RectF(-this.mCornerRadius, -this.mCornerRadius, this.mCornerRadius, this.mCornerRadius);
      RectF var2 = new RectF(var1);
      var2.inset(-this.mShadowSize, -this.mShadowSize);
      if (this.mCornerShadowPath == null) {
         this.mCornerShadowPath = new Path();
      } else {
         this.mCornerShadowPath.reset();
      }

      this.mCornerShadowPath.setFillType(FillType.EVEN_ODD);
      this.mCornerShadowPath.moveTo(-this.mCornerRadius, 0.0F);
      this.mCornerShadowPath.rLineTo(-this.mShadowSize, 0.0F);
      this.mCornerShadowPath.arcTo(var2, 180.0F, 90.0F, false);
      this.mCornerShadowPath.arcTo(var1, 270.0F, -90.0F, false);
      this.mCornerShadowPath.close();
      float var3 = -var2.top;
      float var5;
      int var7;
      int var8;
      int var9;
      if (var3 > 0.0F) {
         float var4 = this.mCornerRadius / var3;
         var5 = (1.0F - var4) / 2.0F;
         Paint var6 = this.mCornerShadowPaint;
         var7 = this.mShadowStartColor;
         var8 = this.mShadowMiddleColor;
         var9 = this.mShadowEndColor;
         TileMode var10 = TileMode.CLAMP;
         var6.setShader(new RadialGradient(0.0F, 0.0F, var3, new int[]{0, var7, var8, var9}, new float[]{0.0F, var4, var5 + var4, 1.0F}, var10));
      }

      Paint var12 = this.mEdgeShadowPaint;
      var5 = var1.top;
      var3 = var2.top;
      var7 = this.mShadowStartColor;
      var9 = this.mShadowMiddleColor;
      var8 = this.mShadowEndColor;
      TileMode var11 = TileMode.CLAMP;
      var12.setShader(new LinearGradient(0.0F, var5, 0.0F, var3, new int[]{var7, var9, var8}, new float[]{0.0F, 0.5F, 1.0F}, var11));
      this.mEdgeShadowPaint.setAntiAlias(false);
   }

   public static float calculateHorizontalPadding(float var0, float var1, boolean var2) {
      return var2 ? (float)((double)var0 + (1.0D - COS_45) * (double)var1) : var0;
   }

   public static float calculateVerticalPadding(float var0, float var1, boolean var2) {
      return var2 ? (float)((double)(var0 * 1.5F) + (1.0D - COS_45) * (double)var1) : var0 * 1.5F;
   }

   private void drawShadow(Canvas var1) {
      int var2 = var1.save();
      var1.rotate(this.mRotation, this.mContentBounds.centerX(), this.mContentBounds.centerY());
      float var3 = -this.mCornerRadius - this.mShadowSize;
      float var4 = this.mCornerRadius;
      float var5 = this.mContentBounds.width();
      float var6 = 2.0F * var4;
      boolean var7;
      if (var5 - var6 > 0.0F) {
         var7 = true;
      } else {
         var7 = false;
      }

      boolean var8;
      if (this.mContentBounds.height() - var6 > 0.0F) {
         var8 = true;
      } else {
         var8 = false;
      }

      float var9 = this.mRawShadowSize;
      float var10 = this.mRawShadowSize;
      float var11 = this.mRawShadowSize;
      float var12 = this.mRawShadowSize;
      var5 = this.mRawShadowSize;
      float var13 = this.mRawShadowSize;
      var11 = var4 / (var11 - var12 * 0.5F + var4);
      var10 = var4 / (var9 - var10 * 0.25F + var4);
      var5 = var4 / (var5 - var13 * 1.0F + var4);
      int var14 = var1.save();
      var1.translate(this.mContentBounds.left + var4, this.mContentBounds.top + var4);
      var1.scale(var11, var10);
      var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
      if (var7) {
         var1.scale(1.0F / var11, 1.0F);
         var1.drawRect(0.0F, var3, this.mContentBounds.width() - var6, -this.mCornerRadius, this.mEdgeShadowPaint);
      }

      var1.restoreToCount(var14);
      var14 = var1.save();
      var1.translate(this.mContentBounds.right - var4, this.mContentBounds.bottom - var4);
      var1.scale(var11, var5);
      var1.rotate(180.0F);
      var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
      if (var7) {
         var1.scale(1.0F / var11, 1.0F);
         var1.drawRect(0.0F, var3, this.mContentBounds.width() - var6, -this.mCornerRadius + this.mShadowSize, this.mEdgeShadowPaint);
      }

      var1.restoreToCount(var14);
      int var15 = var1.save();
      var1.translate(this.mContentBounds.left + var4, this.mContentBounds.bottom - var4);
      var1.scale(var11, var5);
      var1.rotate(270.0F);
      var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
      if (var8) {
         var1.scale(1.0F / var5, 1.0F);
         var1.drawRect(0.0F, var3, this.mContentBounds.height() - var6, -this.mCornerRadius, this.mEdgeShadowPaint);
      }

      var1.restoreToCount(var15);
      var15 = var1.save();
      var1.translate(this.mContentBounds.right - var4, this.mContentBounds.top + var4);
      var1.scale(var11, var10);
      var1.rotate(90.0F);
      var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
      if (var8) {
         var1.scale(1.0F / var10, 1.0F);
         var1.drawRect(0.0F, var3, this.mContentBounds.height() - var6, -this.mCornerRadius, this.mEdgeShadowPaint);
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
      if (this.mDirty) {
         this.buildComponents(this.getBounds());
         this.mDirty = false;
      }

      this.drawShadow(var1);
      super.draw(var1);
   }

   public float getCornerRadius() {
      return this.mCornerRadius;
   }

   public float getMaxShadowSize() {
      return this.mRawMaxShadowSize;
   }

   public float getMinHeight() {
      return Math.max(this.mRawMaxShadowSize, this.mCornerRadius + this.mRawMaxShadowSize * 1.5F / 2.0F) * 2.0F + this.mRawMaxShadowSize * 1.5F * 2.0F;
   }

   public float getMinWidth() {
      return Math.max(this.mRawMaxShadowSize, this.mCornerRadius + this.mRawMaxShadowSize / 2.0F) * 2.0F + this.mRawMaxShadowSize * 2.0F;
   }

   public int getOpacity() {
      return -3;
   }

   public boolean getPadding(Rect var1) {
      int var2 = (int)Math.ceil((double)calculateVerticalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
      int var3 = (int)Math.ceil((double)calculateHorizontalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
      var1.set(var3, var2, var3, var2);
      return true;
   }

   public float getShadowSize() {
      return this.mRawShadowSize;
   }

   protected void onBoundsChange(Rect var1) {
      this.mDirty = true;
   }

   public void setAddPaddingForCorners(boolean var1) {
      this.mAddPaddingForCorners = var1;
      this.invalidateSelf();
   }

   public void setAlpha(int var1) {
      super.setAlpha(var1);
      this.mCornerShadowPaint.setAlpha(var1);
      this.mEdgeShadowPaint.setAlpha(var1);
   }

   public void setCornerRadius(float var1) {
      var1 = (float)Math.round(var1);
      if (this.mCornerRadius != var1) {
         this.mCornerRadius = var1;
         this.mDirty = true;
         this.invalidateSelf();
      }
   }

   public void setMaxShadowSize(float var1) {
      this.setShadowSize(this.mRawShadowSize, var1);
   }

   final void setRotation(float var1) {
      if (this.mRotation != var1) {
         this.mRotation = var1;
         this.invalidateSelf();
      }

   }

   public void setShadowSize(float var1) {
      this.setShadowSize(var1, this.mRawMaxShadowSize);
   }

   void setShadowSize(float var1, float var2) {
      if (var1 >= 0.0F && var2 >= 0.0F) {
         float var3 = (float)toEven(var1);
         var2 = (float)toEven(var2);
         var1 = var3;
         if (var3 > var2) {
            if (!this.mPrintedShadowClipWarning) {
               this.mPrintedShadowClipWarning = true;
            }

            var1 = var2;
         }

         if (this.mRawShadowSize != var1 || this.mRawMaxShadowSize != var2) {
            this.mRawShadowSize = var1;
            this.mRawMaxShadowSize = var2;
            this.mShadowSize = (float)Math.round(var1 * 1.5F);
            this.mMaxShadowSize = var2;
            this.mDirty = true;
            this.invalidateSelf();
         }
      } else {
         throw new IllegalArgumentException("invalid shadow size");
      }
   }
}
