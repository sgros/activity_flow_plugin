package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
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
import android.support.annotation.Nullable;
import android.support.v7.cardview.R;

class RoundRectDrawableWithShadow extends Drawable {
   private static final double COS_45 = Math.cos(Math.toRadians(45.0D));
   private static final float SHADOW_MULTIPLIER = 1.5F;
   static RoundRectDrawableWithShadow.RoundRectHelper sRoundRectHelper;
   private boolean mAddPaddingForCorners = true;
   private ColorStateList mBackground;
   private final RectF mCardBounds;
   private float mCornerRadius;
   private Paint mCornerShadowPaint;
   private Path mCornerShadowPath;
   private boolean mDirty = true;
   private Paint mEdgeShadowPaint;
   private final int mInsetShadow;
   private Paint mPaint;
   private boolean mPrintedShadowClipWarning = false;
   private float mRawMaxShadowSize;
   private float mRawShadowSize;
   private final int mShadowEndColor;
   private float mShadowSize;
   private final int mShadowStartColor;

   RoundRectDrawableWithShadow(Resources var1, ColorStateList var2, float var3, float var4, float var5) {
      this.mShadowStartColor = var1.getColor(R.color.cardview_shadow_start_color);
      this.mShadowEndColor = var1.getColor(R.color.cardview_shadow_end_color);
      this.mInsetShadow = var1.getDimensionPixelSize(R.dimen.cardview_compat_inset_shadow);
      this.mPaint = new Paint(5);
      this.setBackground(var2);
      this.mCornerShadowPaint = new Paint(5);
      this.mCornerShadowPaint.setStyle(Style.FILL);
      this.mCornerRadius = (float)((int)(var3 + 0.5F));
      this.mCardBounds = new RectF();
      this.mEdgeShadowPaint = new Paint(this.mCornerShadowPaint);
      this.mEdgeShadowPaint.setAntiAlias(false);
      this.setShadowSize(var4, var5);
   }

   private void buildComponents(Rect var1) {
      float var2 = this.mRawMaxShadowSize * 1.5F;
      this.mCardBounds.set((float)var1.left + this.mRawMaxShadowSize, (float)var1.top + var2, (float)var1.right - this.mRawMaxShadowSize, (float)var1.bottom - var2);
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
      float var3 = this.mCornerRadius / (this.mCornerRadius + this.mShadowSize);
      Paint var12 = this.mCornerShadowPaint;
      float var4 = this.mCornerRadius;
      float var5 = this.mShadowSize;
      int var6 = this.mShadowStartColor;
      int var7 = this.mShadowStartColor;
      int var8 = this.mShadowEndColor;
      TileMode var10 = TileMode.CLAMP;
      var12.setShader(new RadialGradient(0.0F, 0.0F, var4 + var5, new int[]{var6, var7, var8}, new float[]{0.0F, var3, 1.0F}, var10));
      Paint var11 = this.mEdgeShadowPaint;
      float var9 = -this.mCornerRadius;
      var5 = this.mShadowSize;
      var4 = -this.mCornerRadius;
      var3 = this.mShadowSize;
      var8 = this.mShadowStartColor;
      var6 = this.mShadowStartColor;
      var7 = this.mShadowEndColor;
      TileMode var13 = TileMode.CLAMP;
      var11.setShader(new LinearGradient(0.0F, var9 + var5, 0.0F, var4 - var3, new int[]{var8, var6, var7}, new float[]{0.0F, 0.5F, 1.0F}, var13));
      this.mEdgeShadowPaint.setAntiAlias(false);
   }

   static float calculateHorizontalPadding(float var0, float var1, boolean var2) {
      return var2 ? (float)((double)var0 + (1.0D - COS_45) * (double)var1) : var0;
   }

   static float calculateVerticalPadding(float var0, float var1, boolean var2) {
      return var2 ? (float)((double)(var0 * 1.5F) + (1.0D - COS_45) * (double)var1) : var0 * 1.5F;
   }

   private void drawShadow(Canvas var1) {
      float var2 = -this.mCornerRadius - this.mShadowSize;
      float var3 = this.mCornerRadius + (float)this.mInsetShadow + this.mRawShadowSize / 2.0F;
      float var4 = this.mCardBounds.width();
      float var5 = 2.0F * var3;
      boolean var6;
      if (var4 - var5 > 0.0F) {
         var6 = true;
      } else {
         var6 = false;
      }

      boolean var7;
      if (this.mCardBounds.height() - var5 > 0.0F) {
         var7 = true;
      } else {
         var7 = false;
      }

      int var8 = var1.save();
      var1.translate(this.mCardBounds.left + var3, this.mCardBounds.top + var3);
      var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
      if (var6) {
         var1.drawRect(0.0F, var2, this.mCardBounds.width() - var5, -this.mCornerRadius, this.mEdgeShadowPaint);
      }

      var1.restoreToCount(var8);
      var8 = var1.save();
      var1.translate(this.mCardBounds.right - var3, this.mCardBounds.bottom - var3);
      var1.rotate(180.0F);
      var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
      if (var6) {
         var1.drawRect(0.0F, var2, this.mCardBounds.width() - var5, -this.mCornerRadius + this.mShadowSize, this.mEdgeShadowPaint);
      }

      var1.restoreToCount(var8);
      int var9 = var1.save();
      var1.translate(this.mCardBounds.left + var3, this.mCardBounds.bottom - var3);
      var1.rotate(270.0F);
      var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
      if (var7) {
         var1.drawRect(0.0F, var2, this.mCardBounds.height() - var5, -this.mCornerRadius, this.mEdgeShadowPaint);
      }

      var1.restoreToCount(var9);
      var9 = var1.save();
      var1.translate(this.mCardBounds.right - var3, this.mCardBounds.top + var3);
      var1.rotate(90.0F);
      var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
      if (var7) {
         var1.drawRect(0.0F, var2, this.mCardBounds.height() - var5, -this.mCornerRadius, this.mEdgeShadowPaint);
      }

      var1.restoreToCount(var9);
   }

   private void setBackground(ColorStateList var1) {
      ColorStateList var2 = var1;
      if (var1 == null) {
         var2 = ColorStateList.valueOf(0);
      }

      this.mBackground = var2;
      this.mPaint.setColor(this.mBackground.getColorForState(this.getState(), this.mBackground.getDefaultColor()));
   }

   private void setShadowSize(float var1, float var2) {
      StringBuilder var3;
      if (var1 < 0.0F) {
         var3 = new StringBuilder();
         var3.append("Invalid shadow size ");
         var3.append(var1);
         var3.append(". Must be >= 0");
         throw new IllegalArgumentException(var3.toString());
      } else if (var2 < 0.0F) {
         var3 = new StringBuilder();
         var3.append("Invalid max shadow size ");
         var3.append(var2);
         var3.append(". Must be >= 0");
         throw new IllegalArgumentException(var3.toString());
      } else {
         float var4 = (float)this.toEven(var1);
         var2 = (float)this.toEven(var2);
         var1 = var4;
         if (var4 > var2) {
            if (!this.mPrintedShadowClipWarning) {
               this.mPrintedShadowClipWarning = true;
            }

            var1 = var2;
         }

         if (this.mRawShadowSize != var1 || this.mRawMaxShadowSize != var2) {
            this.mRawShadowSize = var1;
            this.mRawMaxShadowSize = var2;
            this.mShadowSize = (float)((int)(var1 * 1.5F + (float)this.mInsetShadow + 0.5F));
            this.mDirty = true;
            this.invalidateSelf();
         }
      }
   }

   private int toEven(float var1) {
      int var2 = (int)(var1 + 0.5F);
      return var2 % 2 == 1 ? var2 - 1 : var2;
   }

   public void draw(Canvas var1) {
      if (this.mDirty) {
         this.buildComponents(this.getBounds());
         this.mDirty = false;
      }

      var1.translate(0.0F, this.mRawShadowSize / 2.0F);
      this.drawShadow(var1);
      var1.translate(0.0F, -this.mRawShadowSize / 2.0F);
      sRoundRectHelper.drawRoundRect(var1, this.mCardBounds, this.mCornerRadius, this.mPaint);
   }

   ColorStateList getColor() {
      return this.mBackground;
   }

   float getCornerRadius() {
      return this.mCornerRadius;
   }

   void getMaxShadowAndCornerPadding(Rect var1) {
      this.getPadding(var1);
   }

   float getMaxShadowSize() {
      return this.mRawMaxShadowSize;
   }

   float getMinHeight() {
      return Math.max(this.mRawMaxShadowSize, this.mCornerRadius + (float)this.mInsetShadow + this.mRawMaxShadowSize * 1.5F / 2.0F) * 2.0F + (this.mRawMaxShadowSize * 1.5F + (float)this.mInsetShadow) * 2.0F;
   }

   float getMinWidth() {
      return Math.max(this.mRawMaxShadowSize, this.mCornerRadius + (float)this.mInsetShadow + this.mRawMaxShadowSize / 2.0F) * 2.0F + (this.mRawMaxShadowSize + (float)this.mInsetShadow) * 2.0F;
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

   float getShadowSize() {
      return this.mRawShadowSize;
   }

   public boolean isStateful() {
      boolean var1;
      if ((this.mBackground == null || !this.mBackground.isStateful()) && !super.isStateful()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   protected void onBoundsChange(Rect var1) {
      super.onBoundsChange(var1);
      this.mDirty = true;
   }

   protected boolean onStateChange(int[] var1) {
      int var2 = this.mBackground.getColorForState(var1, this.mBackground.getDefaultColor());
      if (this.mPaint.getColor() == var2) {
         return false;
      } else {
         this.mPaint.setColor(var2);
         this.mDirty = true;
         this.invalidateSelf();
         return true;
      }
   }

   void setAddPaddingForCorners(boolean var1) {
      this.mAddPaddingForCorners = var1;
      this.invalidateSelf();
   }

   public void setAlpha(int var1) {
      this.mPaint.setAlpha(var1);
      this.mCornerShadowPaint.setAlpha(var1);
      this.mEdgeShadowPaint.setAlpha(var1);
   }

   void setColor(@Nullable ColorStateList var1) {
      this.setBackground(var1);
      this.invalidateSelf();
   }

   public void setColorFilter(ColorFilter var1) {
      this.mPaint.setColorFilter(var1);
   }

   void setCornerRadius(float var1) {
      if (var1 < 0.0F) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Invalid radius ");
         var2.append(var1);
         var2.append(". Must be >= 0");
         throw new IllegalArgumentException(var2.toString());
      } else {
         var1 = (float)((int)(var1 + 0.5F));
         if (this.mCornerRadius != var1) {
            this.mCornerRadius = var1;
            this.mDirty = true;
            this.invalidateSelf();
         }
      }
   }

   void setMaxShadowSize(float var1) {
      this.setShadowSize(this.mRawShadowSize, var1);
   }

   void setShadowSize(float var1) {
      this.setShadowSize(var1, this.mRawMaxShadowSize);
   }

   interface RoundRectHelper {
      void drawRoundRect(Canvas var1, RectF var2, float var3, Paint var4);
   }
}
