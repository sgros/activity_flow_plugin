package android.support.v7.graphics.drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DrawerArrowDrawable extends Drawable {
   public static final int ARROW_DIRECTION_END = 3;
   public static final int ARROW_DIRECTION_LEFT = 0;
   public static final int ARROW_DIRECTION_RIGHT = 1;
   public static final int ARROW_DIRECTION_START = 2;
   private static final float ARROW_HEAD_ANGLE = (float)Math.toRadians(45.0D);
   private float mArrowHeadLength;
   private float mArrowShaftLength;
   private float mBarGap;
   private float mBarLength;
   private int mDirection = 2;
   private float mMaxCutForBarSize;
   private final Paint mPaint = new Paint();
   private final Path mPath = new Path();
   private float mProgress;
   private final int mSize;
   private boolean mSpin;
   private boolean mVerticalMirror = false;

   public DrawerArrowDrawable(Context var1) {
      this.mPaint.setStyle(Style.STROKE);
      this.mPaint.setStrokeJoin(Join.MITER);
      this.mPaint.setStrokeCap(Cap.BUTT);
      this.mPaint.setAntiAlias(true);
      TypedArray var2 = var1.getTheme().obtainStyledAttributes((AttributeSet)null, R.styleable.DrawerArrowToggle, R.attr.drawerArrowStyle, R.style.Base_Widget_AppCompat_DrawerArrowToggle);
      this.setColor(var2.getColor(R.styleable.DrawerArrowToggle_color, 0));
      this.setBarThickness(var2.getDimension(R.styleable.DrawerArrowToggle_thickness, 0.0F));
      this.setSpinEnabled(var2.getBoolean(R.styleable.DrawerArrowToggle_spinBars, true));
      this.setGapSize((float)Math.round(var2.getDimension(R.styleable.DrawerArrowToggle_gapBetweenBars, 0.0F)));
      this.mSize = var2.getDimensionPixelSize(R.styleable.DrawerArrowToggle_drawableSize, 0);
      this.mBarLength = (float)Math.round(var2.getDimension(R.styleable.DrawerArrowToggle_barLength, 0.0F));
      this.mArrowHeadLength = (float)Math.round(var2.getDimension(R.styleable.DrawerArrowToggle_arrowHeadLength, 0.0F));
      this.mArrowShaftLength = var2.getDimension(R.styleable.DrawerArrowToggle_arrowShaftLength, 0.0F);
      var2.recycle();
   }

   private static float lerp(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * var2;
   }

   public void draw(Canvas var1) {
      Rect var2;
      byte var5;
      boolean var6;
      label41: {
         var2 = this.getBounds();
         int var3 = this.mDirection;
         boolean var4 = false;
         var5 = 1;
         if (var3 != 3) {
            var6 = var4;
            switch(var3) {
            case 0:
               break label41;
            case 1:
               break;
            default:
               var6 = var4;
               if (DrawableCompat.getLayoutDirection(this) != 1) {
                  break label41;
               }
            }
         } else {
            var6 = var4;
            if (DrawableCompat.getLayoutDirection(this) != 0) {
               break label41;
            }
         }

         var6 = true;
      }

      float var7 = (float)Math.sqrt((double)(this.mArrowHeadLength * this.mArrowHeadLength * 2.0F));
      float var8 = lerp(this.mBarLength, var7, this.mProgress);
      float var9 = lerp(this.mBarLength, this.mArrowShaftLength, this.mProgress);
      float var10 = (float)Math.round(lerp(0.0F, this.mMaxCutForBarSize, this.mProgress));
      float var11 = lerp(0.0F, ARROW_HEAD_ANGLE, this.mProgress);
      if (var6) {
         var7 = 0.0F;
      } else {
         var7 = -180.0F;
      }

      float var12;
      if (var6) {
         var12 = 180.0F;
      } else {
         var12 = 0.0F;
      }

      var7 = lerp(var7, var12, this.mProgress);
      double var13 = (double)var8;
      double var15 = (double)var11;
      float var17 = (float)Math.round(var13 * Math.cos(var15));
      var11 = (float)Math.round(var13 * Math.sin(var15));
      this.mPath.rewind();
      var12 = lerp(this.mBarGap + this.mPaint.getStrokeWidth(), -this.mMaxCutForBarSize, this.mProgress);
      var8 = -var9 / 2.0F;
      this.mPath.moveTo(var8 + var10, 0.0F);
      this.mPath.rLineTo(var9 - var10 * 2.0F, 0.0F);
      this.mPath.moveTo(var8, var12);
      this.mPath.rLineTo(var17, var11);
      this.mPath.moveTo(var8, -var12);
      this.mPath.rLineTo(var17, -var11);
      this.mPath.close();
      var1.save();
      var9 = this.mPaint.getStrokeWidth();
      var10 = (float)((int)((float)var2.height() - 3.0F * var9 - this.mBarGap * 2.0F) / 4 * 2);
      var12 = this.mBarGap;
      var1.translate((float)var2.centerX(), var10 + var9 * 1.5F + var12);
      if (this.mSpin) {
         if (this.mVerticalMirror ^ var6) {
            var5 = -1;
         }

         var1.rotate(var7 * (float)var5);
      } else if (var6) {
         var1.rotate(180.0F);
      }

      var1.drawPath(this.mPath, this.mPaint);
      var1.restore();
   }

   public float getArrowHeadLength() {
      return this.mArrowHeadLength;
   }

   public float getArrowShaftLength() {
      return this.mArrowShaftLength;
   }

   public float getBarLength() {
      return this.mBarLength;
   }

   public float getBarThickness() {
      return this.mPaint.getStrokeWidth();
   }

   @ColorInt
   public int getColor() {
      return this.mPaint.getColor();
   }

   public int getDirection() {
      return this.mDirection;
   }

   public float getGapSize() {
      return this.mBarGap;
   }

   public int getIntrinsicHeight() {
      return this.mSize;
   }

   public int getIntrinsicWidth() {
      return this.mSize;
   }

   public int getOpacity() {
      return -3;
   }

   public final Paint getPaint() {
      return this.mPaint;
   }

   @FloatRange(
      from = 0.0D,
      to = 1.0D
   )
   public float getProgress() {
      return this.mProgress;
   }

   public boolean isSpinEnabled() {
      return this.mSpin;
   }

   public void setAlpha(int var1) {
      if (var1 != this.mPaint.getAlpha()) {
         this.mPaint.setAlpha(var1);
         this.invalidateSelf();
      }

   }

   public void setArrowHeadLength(float var1) {
      if (this.mArrowHeadLength != var1) {
         this.mArrowHeadLength = var1;
         this.invalidateSelf();
      }

   }

   public void setArrowShaftLength(float var1) {
      if (this.mArrowShaftLength != var1) {
         this.mArrowShaftLength = var1;
         this.invalidateSelf();
      }

   }

   public void setBarLength(float var1) {
      if (this.mBarLength != var1) {
         this.mBarLength = var1;
         this.invalidateSelf();
      }

   }

   public void setBarThickness(float var1) {
      if (this.mPaint.getStrokeWidth() != var1) {
         this.mPaint.setStrokeWidth(var1);
         this.mMaxCutForBarSize = (float)((double)(var1 / 2.0F) * Math.cos((double)ARROW_HEAD_ANGLE));
         this.invalidateSelf();
      }

   }

   public void setColor(@ColorInt int var1) {
      if (var1 != this.mPaint.getColor()) {
         this.mPaint.setColor(var1);
         this.invalidateSelf();
      }

   }

   public void setColorFilter(ColorFilter var1) {
      this.mPaint.setColorFilter(var1);
      this.invalidateSelf();
   }

   public void setDirection(int var1) {
      if (var1 != this.mDirection) {
         this.mDirection = var1;
         this.invalidateSelf();
      }

   }

   public void setGapSize(float var1) {
      if (var1 != this.mBarGap) {
         this.mBarGap = var1;
         this.invalidateSelf();
      }

   }

   public void setProgress(@FloatRange(from = 0.0D,to = 1.0D) float var1) {
      if (this.mProgress != var1) {
         this.mProgress = var1;
         this.invalidateSelf();
      }

   }

   public void setSpinEnabled(boolean var1) {
      if (this.mSpin != var1) {
         this.mSpin = var1;
         this.invalidateSelf();
      }

   }

   public void setVerticalMirror(boolean var1) {
      if (this.mVerticalMirror != var1) {
         this.mVerticalMirror = var1;
         this.invalidateSelf();
      }

   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface ArrowDirection {
   }
}
