package android.support.design.widget;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.v4.math.MathUtils;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.TintTypedArray;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.animation.Interpolator;

final class CollapsingTextHelper {
   private static final boolean DEBUG_DRAW = false;
   private static final Paint DEBUG_DRAW_PAINT;
   private static final boolean USE_SCALING_TEXTURE;
   private boolean mBoundsChanged;
   private final Rect mCollapsedBounds;
   private float mCollapsedDrawX;
   private float mCollapsedDrawY;
   private int mCollapsedShadowColor;
   private float mCollapsedShadowDx;
   private float mCollapsedShadowDy;
   private float mCollapsedShadowRadius;
   private ColorStateList mCollapsedTextColor;
   private int mCollapsedTextGravity = 16;
   private float mCollapsedTextSize = 15.0F;
   private Typeface mCollapsedTypeface;
   private final RectF mCurrentBounds;
   private float mCurrentDrawX;
   private float mCurrentDrawY;
   private float mCurrentTextSize;
   private Typeface mCurrentTypeface;
   private boolean mDrawTitle;
   private final Rect mExpandedBounds;
   private float mExpandedDrawX;
   private float mExpandedDrawY;
   private float mExpandedFraction;
   private int mExpandedShadowColor;
   private float mExpandedShadowDx;
   private float mExpandedShadowDy;
   private float mExpandedShadowRadius;
   private ColorStateList mExpandedTextColor;
   private int mExpandedTextGravity = 16;
   private float mExpandedTextSize = 15.0F;
   private Bitmap mExpandedTitleTexture;
   private Typeface mExpandedTypeface;
   private boolean mIsRtl;
   private Interpolator mPositionInterpolator;
   private float mScale;
   private int[] mState;
   private CharSequence mText;
   private final TextPaint mTextPaint;
   private Interpolator mTextSizeInterpolator;
   private CharSequence mTextToDraw;
   private float mTextureAscent;
   private float mTextureDescent;
   private Paint mTexturePaint;
   private boolean mUseTexture;
   private final View mView;

   static {
      boolean var0;
      if (VERSION.SDK_INT < 18) {
         var0 = true;
      } else {
         var0 = false;
      }

      USE_SCALING_TEXTURE = var0;
      if (DEBUG_DRAW_PAINT != null) {
         DEBUG_DRAW_PAINT.setAntiAlias(true);
         DEBUG_DRAW_PAINT.setColor(-65281);
      }

   }

   public CollapsingTextHelper(View var1) {
      this.mView = var1;
      this.mTextPaint = new TextPaint(129);
      this.mCollapsedBounds = new Rect();
      this.mExpandedBounds = new Rect();
      this.mCurrentBounds = new RectF();
   }

   private boolean areTypefacesDifferent(Typeface var1, Typeface var2) {
      boolean var3;
      if ((var1 == null || var1.equals(var2)) && (var1 != null || var2 == null)) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   private static int blendColors(int var0, int var1, float var2) {
      float var3 = 1.0F - var2;
      float var4 = (float)Color.alpha(var0);
      float var5 = (float)Color.alpha(var1);
      float var6 = (float)Color.red(var0);
      float var7 = (float)Color.red(var1);
      float var8 = (float)Color.green(var0);
      float var9 = (float)Color.green(var1);
      float var10 = (float)Color.blue(var0);
      float var11 = (float)Color.blue(var1);
      return Color.argb((int)(var4 * var3 + var5 * var2), (int)(var6 * var3 + var7 * var2), (int)(var8 * var3 + var9 * var2), (int)(var10 * var3 + var11 * var2));
   }

   private void calculateBaseOffsets() {
      float var1 = this.mCurrentTextSize;
      this.calculateUsingTextSize(this.mCollapsedTextSize);
      CharSequence var2 = this.mTextToDraw;
      float var3 = 0.0F;
      float var4;
      if (var2 != null) {
         var4 = this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length());
      } else {
         var4 = 0.0F;
      }

      int var5 = GravityCompat.getAbsoluteGravity(this.mCollapsedTextGravity, this.mIsRtl);
      int var6 = var5 & 112;
      float var7;
      if (var6 != 48) {
         if (var6 != 80) {
            var7 = (this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0F;
            float var8 = this.mTextPaint.descent();
            this.mCollapsedDrawY = (float)this.mCollapsedBounds.centerY() + (var7 - var8);
         } else {
            this.mCollapsedDrawY = (float)this.mCollapsedBounds.bottom;
         }
      } else {
         this.mCollapsedDrawY = (float)this.mCollapsedBounds.top - this.mTextPaint.ascent();
      }

      var5 &= 8388615;
      if (var5 != 1) {
         if (var5 != 5) {
            this.mCollapsedDrawX = (float)this.mCollapsedBounds.left;
         } else {
            this.mCollapsedDrawX = (float)this.mCollapsedBounds.right - var4;
         }
      } else {
         this.mCollapsedDrawX = (float)this.mCollapsedBounds.centerX() - var4 / 2.0F;
      }

      this.calculateUsingTextSize(this.mExpandedTextSize);
      var4 = var3;
      if (this.mTextToDraw != null) {
         var4 = this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length());
      }

      var5 = GravityCompat.getAbsoluteGravity(this.mExpandedTextGravity, this.mIsRtl);
      var6 = var5 & 112;
      if (var6 != 48) {
         if (var6 != 80) {
            var3 = (this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0F;
            var7 = this.mTextPaint.descent();
            this.mExpandedDrawY = (float)this.mExpandedBounds.centerY() + (var3 - var7);
         } else {
            this.mExpandedDrawY = (float)this.mExpandedBounds.bottom;
         }
      } else {
         this.mExpandedDrawY = (float)this.mExpandedBounds.top - this.mTextPaint.ascent();
      }

      var5 &= 8388615;
      if (var5 != 1) {
         if (var5 != 5) {
            this.mExpandedDrawX = (float)this.mExpandedBounds.left;
         } else {
            this.mExpandedDrawX = (float)this.mExpandedBounds.right - var4;
         }
      } else {
         this.mExpandedDrawX = (float)this.mExpandedBounds.centerX() - var4 / 2.0F;
      }

      this.clearTexture();
      this.setInterpolatedTextSize(var1);
   }

   private void calculateCurrentOffsets() {
      this.calculateOffsets(this.mExpandedFraction);
   }

   private boolean calculateIsRtl(CharSequence var1) {
      int var2 = ViewCompat.getLayoutDirection(this.mView);
      boolean var3 = true;
      if (var2 != 1) {
         var3 = false;
      }

      TextDirectionHeuristicCompat var4;
      if (var3) {
         var4 = TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL;
      } else {
         var4 = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
      }

      return var4.isRtl((CharSequence)var1, 0, var1.length());
   }

   private void calculateOffsets(float var1) {
      this.interpolateBounds(var1);
      this.mCurrentDrawX = lerp(this.mExpandedDrawX, this.mCollapsedDrawX, var1, this.mPositionInterpolator);
      this.mCurrentDrawY = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, var1, this.mPositionInterpolator);
      this.setInterpolatedTextSize(lerp(this.mExpandedTextSize, this.mCollapsedTextSize, var1, this.mTextSizeInterpolator));
      if (this.mCollapsedTextColor != this.mExpandedTextColor) {
         this.mTextPaint.setColor(blendColors(this.getCurrentExpandedTextColor(), this.getCurrentCollapsedTextColor(), var1));
      } else {
         this.mTextPaint.setColor(this.getCurrentCollapsedTextColor());
      }

      this.mTextPaint.setShadowLayer(lerp(this.mExpandedShadowRadius, this.mCollapsedShadowRadius, var1, (Interpolator)null), lerp(this.mExpandedShadowDx, this.mCollapsedShadowDx, var1, (Interpolator)null), lerp(this.mExpandedShadowDy, this.mCollapsedShadowDy, var1, (Interpolator)null), blendColors(this.mExpandedShadowColor, this.mCollapsedShadowColor, var1));
      ViewCompat.postInvalidateOnAnimation(this.mView);
   }

   private void calculateUsingTextSize(float var1) {
      if (this.mText != null) {
         float var2 = (float)this.mCollapsedBounds.width();
         float var3 = (float)this.mExpandedBounds.width();
         boolean var4 = isClose(var1, this.mCollapsedTextSize);
         boolean var5 = true;
         float var6;
         boolean var7;
         if (var4) {
            var6 = this.mCollapsedTextSize;
            this.mScale = 1.0F;
            if (this.areTypefacesDifferent(this.mCurrentTypeface, this.mCollapsedTypeface)) {
               this.mCurrentTypeface = this.mCollapsedTypeface;
               var7 = true;
            } else {
               var7 = false;
            }

            var1 = var2;
         } else {
            var6 = this.mExpandedTextSize;
            if (this.areTypefacesDifferent(this.mCurrentTypeface, this.mExpandedTypeface)) {
               this.mCurrentTypeface = this.mExpandedTypeface;
               var7 = true;
            } else {
               var7 = false;
            }

            if (isClose(var1, this.mExpandedTextSize)) {
               this.mScale = 1.0F;
            } else {
               this.mScale = var1 / this.mExpandedTextSize;
            }

            var1 = this.mCollapsedTextSize / this.mExpandedTextSize;
            if (var3 * var1 > var2) {
               var1 = Math.min(var2 / var1, var3);
            } else {
               var1 = var3;
            }
         }

         boolean var8 = var7;
         if (var1 > 0.0F) {
            if (this.mCurrentTextSize == var6 && !this.mBoundsChanged && !var7) {
               var7 = false;
            } else {
               var7 = true;
            }

            this.mCurrentTextSize = var6;
            this.mBoundsChanged = false;
            var8 = var7;
         }

         if (this.mTextToDraw == null || var8) {
            this.mTextPaint.setTextSize(this.mCurrentTextSize);
            this.mTextPaint.setTypeface(this.mCurrentTypeface);
            TextPaint var9 = this.mTextPaint;
            if (this.mScale == 1.0F) {
               var5 = false;
            }

            var9.setLinearText(var5);
            CharSequence var10 = TextUtils.ellipsize(this.mText, this.mTextPaint, var1, TruncateAt.END);
            if (!TextUtils.equals(var10, this.mTextToDraw)) {
               this.mTextToDraw = var10;
               this.mIsRtl = this.calculateIsRtl(this.mTextToDraw);
            }
         }

      }
   }

   private void clearTexture() {
      if (this.mExpandedTitleTexture != null) {
         this.mExpandedTitleTexture.recycle();
         this.mExpandedTitleTexture = null;
      }

   }

   private void ensureExpandedTexture() {
      if (this.mExpandedTitleTexture == null && !this.mExpandedBounds.isEmpty() && !TextUtils.isEmpty(this.mTextToDraw)) {
         this.calculateOffsets(0.0F);
         this.mTextureAscent = this.mTextPaint.ascent();
         this.mTextureDescent = this.mTextPaint.descent();
         int var1 = Math.round(this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length()));
         int var2 = Math.round(this.mTextureDescent - this.mTextureAscent);
         if (var1 > 0 && var2 > 0) {
            this.mExpandedTitleTexture = Bitmap.createBitmap(var1, var2, Config.ARGB_8888);
            (new Canvas(this.mExpandedTitleTexture)).drawText(this.mTextToDraw, 0, this.mTextToDraw.length(), 0.0F, (float)var2 - this.mTextPaint.descent(), this.mTextPaint);
            if (this.mTexturePaint == null) {
               this.mTexturePaint = new Paint(3);
            }

         }
      }
   }

   @ColorInt
   private int getCurrentCollapsedTextColor() {
      return this.mState != null ? this.mCollapsedTextColor.getColorForState(this.mState, 0) : this.mCollapsedTextColor.getDefaultColor();
   }

   @ColorInt
   private int getCurrentExpandedTextColor() {
      return this.mState != null ? this.mExpandedTextColor.getColorForState(this.mState, 0) : this.mExpandedTextColor.getDefaultColor();
   }

   private void interpolateBounds(float var1) {
      this.mCurrentBounds.left = lerp((float)this.mExpandedBounds.left, (float)this.mCollapsedBounds.left, var1, this.mPositionInterpolator);
      this.mCurrentBounds.top = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, var1, this.mPositionInterpolator);
      this.mCurrentBounds.right = lerp((float)this.mExpandedBounds.right, (float)this.mCollapsedBounds.right, var1, this.mPositionInterpolator);
      this.mCurrentBounds.bottom = lerp((float)this.mExpandedBounds.bottom, (float)this.mCollapsedBounds.bottom, var1, this.mPositionInterpolator);
   }

   private static boolean isClose(float var0, float var1) {
      boolean var2;
      if (Math.abs(var0 - var1) < 0.001F) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static float lerp(float var0, float var1, float var2, Interpolator var3) {
      float var4 = var2;
      if (var3 != null) {
         var4 = var3.getInterpolation(var2);
      }

      return AnimationUtils.lerp(var0, var1, var4);
   }

   private Typeface readFontFamilyTypeface(int var1) {
      TypedArray var2 = this.mView.getContext().obtainStyledAttributes(var1, new int[]{16843692});

      Throwable var10000;
      label78: {
         boolean var10001;
         String var3;
         try {
            var3 = var2.getString(0);
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label78;
         }

         if (var3 == null) {
            var2.recycle();
            return null;
         }

         Typeface var11;
         try {
            var11 = Typeface.create(var3, 0);
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label78;
         }

         var2.recycle();
         return var11;
      }

      Throwable var10 = var10000;
      var2.recycle();
      throw var10;
   }

   private static boolean rectEquals(Rect var0, int var1, int var2, int var3, int var4) {
      boolean var5;
      if (var0.left == var1 && var0.top == var2 && var0.right == var3 && var0.bottom == var4) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   private void setInterpolatedTextSize(float var1) {
      this.calculateUsingTextSize(var1);
      boolean var2;
      if (USE_SCALING_TEXTURE && this.mScale != 1.0F) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mUseTexture = var2;
      if (this.mUseTexture) {
         this.ensureExpandedTexture();
      }

      ViewCompat.postInvalidateOnAnimation(this.mView);
   }

   public void draw(Canvas var1) {
      int var2 = var1.save();
      if (this.mTextToDraw != null && this.mDrawTitle) {
         float var3 = this.mCurrentDrawX;
         float var4 = this.mCurrentDrawY;
         boolean var5;
         if (this.mUseTexture && this.mExpandedTitleTexture != null) {
            var5 = true;
         } else {
            var5 = false;
         }

         float var6;
         float var7;
         if (var5) {
            var6 = this.mTextureAscent * this.mScale;
            var7 = this.mTextureDescent;
            var7 = this.mScale;
         } else {
            var6 = this.mTextPaint.ascent() * this.mScale;
            this.mTextPaint.descent();
            var7 = this.mScale;
         }

         var7 = var4;
         if (var5) {
            var7 = var4 + var6;
         }

         if (this.mScale != 1.0F) {
            var1.scale(this.mScale, this.mScale, var3, var7);
         }

         if (var5) {
            var1.drawBitmap(this.mExpandedTitleTexture, var3, var7, this.mTexturePaint);
         } else {
            var1.drawText(this.mTextToDraw, 0, this.mTextToDraw.length(), var3, var7, this.mTextPaint);
         }
      }

      var1.restoreToCount(var2);
   }

   ColorStateList getCollapsedTextColor() {
      return this.mCollapsedTextColor;
   }

   int getCollapsedTextGravity() {
      return this.mCollapsedTextGravity;
   }

   float getCollapsedTextSize() {
      return this.mCollapsedTextSize;
   }

   Typeface getCollapsedTypeface() {
      Typeface var1;
      if (this.mCollapsedTypeface != null) {
         var1 = this.mCollapsedTypeface;
      } else {
         var1 = Typeface.DEFAULT;
      }

      return var1;
   }

   ColorStateList getExpandedTextColor() {
      return this.mExpandedTextColor;
   }

   int getExpandedTextGravity() {
      return this.mExpandedTextGravity;
   }

   float getExpandedTextSize() {
      return this.mExpandedTextSize;
   }

   Typeface getExpandedTypeface() {
      Typeface var1;
      if (this.mExpandedTypeface != null) {
         var1 = this.mExpandedTypeface;
      } else {
         var1 = Typeface.DEFAULT;
      }

      return var1;
   }

   float getExpansionFraction() {
      return this.mExpandedFraction;
   }

   CharSequence getText() {
      return this.mText;
   }

   final boolean isStateful() {
      boolean var1;
      if ((this.mCollapsedTextColor == null || !this.mCollapsedTextColor.isStateful()) && (this.mExpandedTextColor == null || !this.mExpandedTextColor.isStateful())) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   void onBoundsChanged() {
      boolean var1;
      if (this.mCollapsedBounds.width() > 0 && this.mCollapsedBounds.height() > 0 && this.mExpandedBounds.width() > 0 && this.mExpandedBounds.height() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.mDrawTitle = var1;
   }

   public void recalculate() {
      if (this.mView.getHeight() > 0 && this.mView.getWidth() > 0) {
         this.calculateBaseOffsets();
         this.calculateCurrentOffsets();
      }

   }

   void setCollapsedBounds(int var1, int var2, int var3, int var4) {
      if (!rectEquals(this.mCollapsedBounds, var1, var2, var3, var4)) {
         this.mCollapsedBounds.set(var1, var2, var3, var4);
         this.mBoundsChanged = true;
         this.onBoundsChanged();
      }

   }

   void setCollapsedTextAppearance(int var1) {
      TintTypedArray var2 = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), var1, R.styleable.TextAppearance);
      if (var2.hasValue(R.styleable.TextAppearance_android_textColor)) {
         this.mCollapsedTextColor = var2.getColorStateList(R.styleable.TextAppearance_android_textColor);
      }

      if (var2.hasValue(R.styleable.TextAppearance_android_textSize)) {
         this.mCollapsedTextSize = (float)var2.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.mCollapsedTextSize);
      }

      this.mCollapsedShadowColor = var2.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
      this.mCollapsedShadowDx = var2.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0F);
      this.mCollapsedShadowDy = var2.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0F);
      this.mCollapsedShadowRadius = var2.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0F);
      var2.recycle();
      if (VERSION.SDK_INT >= 16) {
         this.mCollapsedTypeface = this.readFontFamilyTypeface(var1);
      }

      this.recalculate();
   }

   void setCollapsedTextColor(ColorStateList var1) {
      if (this.mCollapsedTextColor != var1) {
         this.mCollapsedTextColor = var1;
         this.recalculate();
      }

   }

   void setCollapsedTextGravity(int var1) {
      if (this.mCollapsedTextGravity != var1) {
         this.mCollapsedTextGravity = var1;
         this.recalculate();
      }

   }

   void setCollapsedTextSize(float var1) {
      if (this.mCollapsedTextSize != var1) {
         this.mCollapsedTextSize = var1;
         this.recalculate();
      }

   }

   void setCollapsedTypeface(Typeface var1) {
      if (this.areTypefacesDifferent(this.mCollapsedTypeface, var1)) {
         this.mCollapsedTypeface = var1;
         this.recalculate();
      }

   }

   void setExpandedBounds(int var1, int var2, int var3, int var4) {
      if (!rectEquals(this.mExpandedBounds, var1, var2, var3, var4)) {
         this.mExpandedBounds.set(var1, var2, var3, var4);
         this.mBoundsChanged = true;
         this.onBoundsChanged();
      }

   }

   void setExpandedTextAppearance(int var1) {
      TintTypedArray var2 = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), var1, R.styleable.TextAppearance);
      if (var2.hasValue(R.styleable.TextAppearance_android_textColor)) {
         this.mExpandedTextColor = var2.getColorStateList(R.styleable.TextAppearance_android_textColor);
      }

      if (var2.hasValue(R.styleable.TextAppearance_android_textSize)) {
         this.mExpandedTextSize = (float)var2.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.mExpandedTextSize);
      }

      this.mExpandedShadowColor = var2.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
      this.mExpandedShadowDx = var2.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0F);
      this.mExpandedShadowDy = var2.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0F);
      this.mExpandedShadowRadius = var2.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0F);
      var2.recycle();
      if (VERSION.SDK_INT >= 16) {
         this.mExpandedTypeface = this.readFontFamilyTypeface(var1);
      }

      this.recalculate();
   }

   void setExpandedTextColor(ColorStateList var1) {
      if (this.mExpandedTextColor != var1) {
         this.mExpandedTextColor = var1;
         this.recalculate();
      }

   }

   void setExpandedTextGravity(int var1) {
      if (this.mExpandedTextGravity != var1) {
         this.mExpandedTextGravity = var1;
         this.recalculate();
      }

   }

   void setExpandedTextSize(float var1) {
      if (this.mExpandedTextSize != var1) {
         this.mExpandedTextSize = var1;
         this.recalculate();
      }

   }

   void setExpandedTypeface(Typeface var1) {
      if (this.areTypefacesDifferent(this.mExpandedTypeface, var1)) {
         this.mExpandedTypeface = var1;
         this.recalculate();
      }

   }

   void setExpansionFraction(float var1) {
      var1 = MathUtils.clamp(var1, 0.0F, 1.0F);
      if (var1 != this.mExpandedFraction) {
         this.mExpandedFraction = var1;
         this.calculateCurrentOffsets();
      }

   }

   void setPositionInterpolator(Interpolator var1) {
      this.mPositionInterpolator = var1;
      this.recalculate();
   }

   final boolean setState(int[] var1) {
      this.mState = var1;
      if (this.isStateful()) {
         this.recalculate();
         return true;
      } else {
         return false;
      }
   }

   void setText(CharSequence var1) {
      if (var1 == null || !var1.equals(this.mText)) {
         this.mText = var1;
         this.mTextToDraw = null;
         this.clearTexture();
         this.recalculate();
      }

   }

   void setTextSizeInterpolator(Interpolator var1) {
      this.mTextSizeInterpolator = var1;
      this.recalculate();
   }

   void setTypefaces(Typeface var1) {
      this.mExpandedTypeface = var1;
      this.mCollapsedTypeface = var1;
      this.recalculate();
   }
}
