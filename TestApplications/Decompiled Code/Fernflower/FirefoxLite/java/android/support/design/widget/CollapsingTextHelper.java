package android.support.design.widget;

import android.animation.TimeInterpolator;
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
import android.support.design.animation.AnimationUtils;
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

public final class CollapsingTextHelper {
   private static final Paint DEBUG_DRAW_PAINT;
   private static final boolean USE_SCALING_TEXTURE;
   private boolean boundsChanged;
   private final Rect collapsedBounds;
   private float collapsedDrawX;
   private float collapsedDrawY;
   private int collapsedShadowColor;
   private float collapsedShadowDx;
   private float collapsedShadowDy;
   private float collapsedShadowRadius;
   private ColorStateList collapsedTextColor;
   private int collapsedTextGravity = 16;
   private float collapsedTextSize = 15.0F;
   private Typeface collapsedTypeface;
   private final RectF currentBounds;
   private float currentDrawX;
   private float currentDrawY;
   private float currentTextSize;
   private Typeface currentTypeface;
   private boolean drawTitle;
   private final Rect expandedBounds;
   private float expandedDrawX;
   private float expandedDrawY;
   private float expandedFraction;
   private int expandedShadowColor;
   private float expandedShadowDx;
   private float expandedShadowDy;
   private float expandedShadowRadius;
   private ColorStateList expandedTextColor;
   private int expandedTextGravity = 16;
   private float expandedTextSize = 15.0F;
   private Bitmap expandedTitleTexture;
   private Typeface expandedTypeface;
   private boolean isRtl;
   private TimeInterpolator positionInterpolator;
   private float scale;
   private int[] state;
   private CharSequence text;
   private final TextPaint textPaint;
   private TimeInterpolator textSizeInterpolator;
   private CharSequence textToDraw;
   private float textureAscent;
   private float textureDescent;
   private Paint texturePaint;
   private final TextPaint tmpPaint;
   private boolean useTexture;
   private final View view;

   static {
      boolean var0;
      if (VERSION.SDK_INT < 18) {
         var0 = true;
      } else {
         var0 = false;
      }

      USE_SCALING_TEXTURE = var0;
      DEBUG_DRAW_PAINT = null;
      if (DEBUG_DRAW_PAINT != null) {
         DEBUG_DRAW_PAINT.setAntiAlias(true);
         DEBUG_DRAW_PAINT.setColor(-65281);
      }

   }

   public CollapsingTextHelper(View var1) {
      this.view = var1;
      this.textPaint = new TextPaint(129);
      this.tmpPaint = new TextPaint(this.textPaint);
      this.collapsedBounds = new Rect();
      this.expandedBounds = new Rect();
      this.currentBounds = new RectF();
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
      float var1 = this.currentTextSize;
      this.calculateUsingTextSize(this.collapsedTextSize);
      CharSequence var2 = this.textToDraw;
      float var3 = 0.0F;
      float var4;
      if (var2 != null) {
         var4 = this.textPaint.measureText(this.textToDraw, 0, this.textToDraw.length());
      } else {
         var4 = 0.0F;
      }

      int var5 = GravityCompat.getAbsoluteGravity(this.collapsedTextGravity, this.isRtl);
      int var6 = var5 & 112;
      float var8;
      if (var6 != 48) {
         if (var6 != 80) {
            float var7 = (this.textPaint.descent() - this.textPaint.ascent()) / 2.0F;
            var8 = this.textPaint.descent();
            this.collapsedDrawY = (float)this.collapsedBounds.centerY() + (var7 - var8);
         } else {
            this.collapsedDrawY = (float)this.collapsedBounds.bottom;
         }
      } else {
         this.collapsedDrawY = (float)this.collapsedBounds.top - this.textPaint.ascent();
      }

      var6 = var5 & 8388615;
      if (var6 != 1) {
         if (var6 != 5) {
            this.collapsedDrawX = (float)this.collapsedBounds.left;
         } else {
            this.collapsedDrawX = (float)this.collapsedBounds.right - var4;
         }
      } else {
         this.collapsedDrawX = (float)this.collapsedBounds.centerX() - var4 / 2.0F;
      }

      this.calculateUsingTextSize(this.expandedTextSize);
      var4 = var3;
      if (this.textToDraw != null) {
         var4 = this.textPaint.measureText(this.textToDraw, 0, this.textToDraw.length());
      }

      var5 = GravityCompat.getAbsoluteGravity(this.expandedTextGravity, this.isRtl);
      var6 = var5 & 112;
      if (var6 != 48) {
         if (var6 != 80) {
            var3 = (this.textPaint.descent() - this.textPaint.ascent()) / 2.0F;
            var8 = this.textPaint.descent();
            this.expandedDrawY = (float)this.expandedBounds.centerY() + (var3 - var8);
         } else {
            this.expandedDrawY = (float)this.expandedBounds.bottom;
         }
      } else {
         this.expandedDrawY = (float)this.expandedBounds.top - this.textPaint.ascent();
      }

      var6 = var5 & 8388615;
      if (var6 != 1) {
         if (var6 != 5) {
            this.expandedDrawX = (float)this.expandedBounds.left;
         } else {
            this.expandedDrawX = (float)this.expandedBounds.right - var4;
         }
      } else {
         this.expandedDrawX = (float)this.expandedBounds.centerX() - var4 / 2.0F;
      }

      this.clearTexture();
      this.setInterpolatedTextSize(var1);
   }

   private void calculateCurrentOffsets() {
      this.calculateOffsets(this.expandedFraction);
   }

   private boolean calculateIsRtl(CharSequence var1) {
      int var2 = ViewCompat.getLayoutDirection(this.view);
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

      return var4.isRtl(var1, 0, var1.length());
   }

   private void calculateOffsets(float var1) {
      this.interpolateBounds(var1);
      this.currentDrawX = lerp(this.expandedDrawX, this.collapsedDrawX, var1, this.positionInterpolator);
      this.currentDrawY = lerp(this.expandedDrawY, this.collapsedDrawY, var1, this.positionInterpolator);
      this.setInterpolatedTextSize(lerp(this.expandedTextSize, this.collapsedTextSize, var1, this.textSizeInterpolator));
      if (this.collapsedTextColor != this.expandedTextColor) {
         this.textPaint.setColor(blendColors(this.getCurrentExpandedTextColor(), this.getCurrentCollapsedTextColor(), var1));
      } else {
         this.textPaint.setColor(this.getCurrentCollapsedTextColor());
      }

      this.textPaint.setShadowLayer(lerp(this.expandedShadowRadius, this.collapsedShadowRadius, var1, (TimeInterpolator)null), lerp(this.expandedShadowDx, this.collapsedShadowDx, var1, (TimeInterpolator)null), lerp(this.expandedShadowDy, this.collapsedShadowDy, var1, (TimeInterpolator)null), blendColors(this.expandedShadowColor, this.collapsedShadowColor, var1));
      ViewCompat.postInvalidateOnAnimation(this.view);
   }

   private void calculateUsingTextSize(float var1) {
      if (this.text != null) {
         float var2 = (float)this.collapsedBounds.width();
         float var3 = (float)this.expandedBounds.width();
         boolean var4 = isClose(var1, this.collapsedTextSize);
         boolean var5 = true;
         float var6;
         boolean var7;
         if (var4) {
            var6 = this.collapsedTextSize;
            this.scale = 1.0F;
            if (this.currentTypeface != this.collapsedTypeface) {
               this.currentTypeface = this.collapsedTypeface;
               var7 = true;
            } else {
               var7 = false;
            }

            var1 = var2;
         } else {
            var6 = this.expandedTextSize;
            if (this.currentTypeface != this.expandedTypeface) {
               this.currentTypeface = this.expandedTypeface;
               var7 = true;
            } else {
               var7 = false;
            }

            if (isClose(var1, this.expandedTextSize)) {
               this.scale = 1.0F;
            } else {
               this.scale = var1 / this.expandedTextSize;
            }

            var1 = this.collapsedTextSize / this.expandedTextSize;
            if (var3 * var1 > var2) {
               var1 = Math.min(var2 / var1, var3);
            } else {
               var1 = var3;
            }
         }

         boolean var8 = var7;
         if (var1 > 0.0F) {
            if (this.currentTextSize == var6 && !this.boundsChanged && !var7) {
               var7 = false;
            } else {
               var7 = true;
            }

            this.currentTextSize = var6;
            this.boundsChanged = false;
            var8 = var7;
         }

         if (this.textToDraw == null || var8) {
            this.textPaint.setTextSize(this.currentTextSize);
            this.textPaint.setTypeface(this.currentTypeface);
            TextPaint var9 = this.textPaint;
            if (this.scale == 1.0F) {
               var5 = false;
            }

            var9.setLinearText(var5);
            CharSequence var10 = TextUtils.ellipsize(this.text, this.textPaint, var1, TruncateAt.END);
            if (!TextUtils.equals(var10, this.textToDraw)) {
               this.textToDraw = var10;
               this.isRtl = this.calculateIsRtl(this.textToDraw);
            }
         }

      }
   }

   private void clearTexture() {
      if (this.expandedTitleTexture != null) {
         this.expandedTitleTexture.recycle();
         this.expandedTitleTexture = null;
      }

   }

   private void ensureExpandedTexture() {
      if (this.expandedTitleTexture == null && !this.expandedBounds.isEmpty() && !TextUtils.isEmpty(this.textToDraw)) {
         this.calculateOffsets(0.0F);
         this.textureAscent = this.textPaint.ascent();
         this.textureDescent = this.textPaint.descent();
         int var1 = Math.round(this.textPaint.measureText(this.textToDraw, 0, this.textToDraw.length()));
         int var2 = Math.round(this.textureDescent - this.textureAscent);
         if (var1 > 0 && var2 > 0) {
            this.expandedTitleTexture = Bitmap.createBitmap(var1, var2, Config.ARGB_8888);
            (new Canvas(this.expandedTitleTexture)).drawText(this.textToDraw, 0, this.textToDraw.length(), 0.0F, (float)var2 - this.textPaint.descent(), this.textPaint);
            if (this.texturePaint == null) {
               this.texturePaint = new Paint(3);
            }

         }
      }
   }

   private int getCurrentExpandedTextColor() {
      return this.state != null ? this.expandedTextColor.getColorForState(this.state, 0) : this.expandedTextColor.getDefaultColor();
   }

   private void getTextPaintCollapsed(TextPaint var1) {
      var1.setTextSize(this.collapsedTextSize);
      var1.setTypeface(this.collapsedTypeface);
   }

   private void interpolateBounds(float var1) {
      this.currentBounds.left = lerp((float)this.expandedBounds.left, (float)this.collapsedBounds.left, var1, this.positionInterpolator);
      this.currentBounds.top = lerp(this.expandedDrawY, this.collapsedDrawY, var1, this.positionInterpolator);
      this.currentBounds.right = lerp((float)this.expandedBounds.right, (float)this.collapsedBounds.right, var1, this.positionInterpolator);
      this.currentBounds.bottom = lerp((float)this.expandedBounds.bottom, (float)this.collapsedBounds.bottom, var1, this.positionInterpolator);
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

   private static float lerp(float var0, float var1, float var2, TimeInterpolator var3) {
      float var4 = var2;
      if (var3 != null) {
         var4 = var3.getInterpolation(var2);
      }

      return AnimationUtils.lerp(var0, var1, var4);
   }

   private Typeface readFontFamilyTypeface(int var1) {
      TypedArray var2 = this.view.getContext().obtainStyledAttributes(var1, new int[]{16843692});

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
      if (USE_SCALING_TEXTURE && this.scale != 1.0F) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.useTexture = var2;
      if (this.useTexture) {
         this.ensureExpandedTexture();
      }

      ViewCompat.postInvalidateOnAnimation(this.view);
   }

   public float calculateCollapsedTextWidth() {
      if (this.text == null) {
         return 0.0F;
      } else {
         this.getTextPaintCollapsed(this.tmpPaint);
         return this.tmpPaint.measureText(this.text, 0, this.text.length());
      }
   }

   public void draw(Canvas var1) {
      int var2 = var1.save();
      if (this.textToDraw != null && this.drawTitle) {
         float var3 = this.currentDrawX;
         float var4 = this.currentDrawY;
         boolean var5;
         if (this.useTexture && this.expandedTitleTexture != null) {
            var5 = true;
         } else {
            var5 = false;
         }

         float var6;
         float var7;
         if (var5) {
            var6 = this.textureAscent * this.scale;
            var7 = this.textureDescent;
            var7 = this.scale;
         } else {
            var6 = this.textPaint.ascent() * this.scale;
            this.textPaint.descent();
            var7 = this.scale;
         }

         var7 = var4;
         if (var5) {
            var7 = var4 + var6;
         }

         if (this.scale != 1.0F) {
            var1.scale(this.scale, this.scale, var3, var7);
         }

         if (var5) {
            var1.drawBitmap(this.expandedTitleTexture, var3, var7, this.texturePaint);
         } else {
            var1.drawText(this.textToDraw, 0, this.textToDraw.length(), var3, var7, this.textPaint);
         }
      }

      var1.restoreToCount(var2);
   }

   public void getCollapsedTextActualBounds(RectF var1) {
      boolean var2 = this.calculateIsRtl(this.text);
      float var3;
      if (!var2) {
         var3 = (float)this.collapsedBounds.left;
      } else {
         var3 = (float)this.collapsedBounds.right - this.calculateCollapsedTextWidth();
      }

      var1.left = var3;
      var1.top = (float)this.collapsedBounds.top;
      if (!var2) {
         var3 = var1.left + this.calculateCollapsedTextWidth();
      } else {
         var3 = (float)this.collapsedBounds.right;
      }

      var1.right = var3;
      var1.bottom = (float)this.collapsedBounds.top + this.getCollapsedTextHeight();
   }

   public ColorStateList getCollapsedTextColor() {
      return this.collapsedTextColor;
   }

   public float getCollapsedTextHeight() {
      this.getTextPaintCollapsed(this.tmpPaint);
      return -this.tmpPaint.ascent();
   }

   public int getCurrentCollapsedTextColor() {
      return this.state != null ? this.collapsedTextColor.getColorForState(this.state, 0) : this.collapsedTextColor.getDefaultColor();
   }

   public float getExpansionFraction() {
      return this.expandedFraction;
   }

   public final boolean isStateful() {
      boolean var1;
      if ((this.collapsedTextColor == null || !this.collapsedTextColor.isStateful()) && (this.expandedTextColor == null || !this.expandedTextColor.isStateful())) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   void onBoundsChanged() {
      boolean var1;
      if (this.collapsedBounds.width() > 0 && this.collapsedBounds.height() > 0 && this.expandedBounds.width() > 0 && this.expandedBounds.height() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.drawTitle = var1;
   }

   public void recalculate() {
      if (this.view.getHeight() > 0 && this.view.getWidth() > 0) {
         this.calculateBaseOffsets();
         this.calculateCurrentOffsets();
      }

   }

   public void setCollapsedBounds(int var1, int var2, int var3, int var4) {
      if (!rectEquals(this.collapsedBounds, var1, var2, var3, var4)) {
         this.collapsedBounds.set(var1, var2, var3, var4);
         this.boundsChanged = true;
         this.onBoundsChanged();
      }

   }

   public void setCollapsedTextAppearance(int var1) {
      TintTypedArray var2 = TintTypedArray.obtainStyledAttributes(this.view.getContext(), var1, R.styleable.TextAppearance);
      if (var2.hasValue(R.styleable.TextAppearance_android_textColor)) {
         this.collapsedTextColor = var2.getColorStateList(R.styleable.TextAppearance_android_textColor);
      }

      if (var2.hasValue(R.styleable.TextAppearance_android_textSize)) {
         this.collapsedTextSize = (float)var2.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.collapsedTextSize);
      }

      this.collapsedShadowColor = var2.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
      this.collapsedShadowDx = var2.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0F);
      this.collapsedShadowDy = var2.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0F);
      this.collapsedShadowRadius = var2.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0F);
      var2.recycle();
      if (VERSION.SDK_INT >= 16) {
         this.collapsedTypeface = this.readFontFamilyTypeface(var1);
      }

      this.recalculate();
   }

   public void setCollapsedTextColor(ColorStateList var1) {
      if (this.collapsedTextColor != var1) {
         this.collapsedTextColor = var1;
         this.recalculate();
      }

   }

   public void setCollapsedTextGravity(int var1) {
      if (this.collapsedTextGravity != var1) {
         this.collapsedTextGravity = var1;
         this.recalculate();
      }

   }

   public void setExpandedBounds(int var1, int var2, int var3, int var4) {
      if (!rectEquals(this.expandedBounds, var1, var2, var3, var4)) {
         this.expandedBounds.set(var1, var2, var3, var4);
         this.boundsChanged = true;
         this.onBoundsChanged();
      }

   }

   public void setExpandedTextColor(ColorStateList var1) {
      if (this.expandedTextColor != var1) {
         this.expandedTextColor = var1;
         this.recalculate();
      }

   }

   public void setExpandedTextGravity(int var1) {
      if (this.expandedTextGravity != var1) {
         this.expandedTextGravity = var1;
         this.recalculate();
      }

   }

   public void setExpandedTextSize(float var1) {
      if (this.expandedTextSize != var1) {
         this.expandedTextSize = var1;
         this.recalculate();
      }

   }

   public void setExpansionFraction(float var1) {
      var1 = android.support.v4.math.MathUtils.clamp(var1, 0.0F, 1.0F);
      if (var1 != this.expandedFraction) {
         this.expandedFraction = var1;
         this.calculateCurrentOffsets();
      }

   }

   public void setPositionInterpolator(TimeInterpolator var1) {
      this.positionInterpolator = var1;
      this.recalculate();
   }

   public final boolean setState(int[] var1) {
      this.state = var1;
      if (this.isStateful()) {
         this.recalculate();
         return true;
      } else {
         return false;
      }
   }

   public void setText(CharSequence var1) {
      if (var1 == null || !var1.equals(this.text)) {
         this.text = var1;
         this.textToDraw = null;
         this.clearTexture();
         this.recalculate();
      }

   }

   public void setTextSizeInterpolator(TimeInterpolator var1) {
      this.textSizeInterpolator = var1;
      this.recalculate();
   }

   public void setTypefaces(Typeface var1) {
      this.expandedTypeface = var1;
      this.collapsedTypeface = var1;
      this.recalculate();
   }
}
