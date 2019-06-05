package android.support.design.button;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.support.design.R;
import android.support.design.internal.ViewUtils;
import android.support.design.resources.MaterialResources;
import android.support.design.ripple.RippleUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;

class MaterialButtonHelper {
   private static final boolean IS_LOLLIPOP;
   private GradientDrawable backgroundDrawableLollipop;
   private boolean backgroundOverwritten = false;
   private ColorStateList backgroundTint;
   private Mode backgroundTintMode;
   private final Rect bounds = new Rect();
   private final Paint buttonStrokePaint = new Paint(1);
   private GradientDrawable colorableBackgroundDrawableCompat;
   private int cornerRadius;
   private int insetBottom;
   private int insetLeft;
   private int insetRight;
   private int insetTop;
   private GradientDrawable maskDrawableLollipop;
   private final MaterialButton materialButton;
   private final RectF rectF = new RectF();
   private ColorStateList rippleColor;
   private GradientDrawable rippleDrawableCompat;
   private ColorStateList strokeColor;
   private GradientDrawable strokeDrawableLollipop;
   private int strokeWidth;
   private Drawable tintableBackgroundDrawableCompat;
   private Drawable tintableRippleDrawableCompat;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 21) {
         var0 = true;
      } else {
         var0 = false;
      }

      IS_LOLLIPOP = var0;
   }

   public MaterialButtonHelper(MaterialButton var1) {
      this.materialButton = var1;
   }

   private Drawable createBackgroundCompat() {
      this.colorableBackgroundDrawableCompat = new GradientDrawable();
      this.colorableBackgroundDrawableCompat.setCornerRadius((float)this.cornerRadius + 1.0E-5F);
      this.colorableBackgroundDrawableCompat.setColor(-1);
      this.tintableBackgroundDrawableCompat = DrawableCompat.wrap(this.colorableBackgroundDrawableCompat);
      DrawableCompat.setTintList(this.tintableBackgroundDrawableCompat, this.backgroundTint);
      if (this.backgroundTintMode != null) {
         DrawableCompat.setTintMode(this.tintableBackgroundDrawableCompat, this.backgroundTintMode);
      }

      this.rippleDrawableCompat = new GradientDrawable();
      this.rippleDrawableCompat.setCornerRadius((float)this.cornerRadius + 1.0E-5F);
      this.rippleDrawableCompat.setColor(-1);
      this.tintableRippleDrawableCompat = DrawableCompat.wrap(this.rippleDrawableCompat);
      DrawableCompat.setTintList(this.tintableRippleDrawableCompat, this.rippleColor);
      return this.wrapDrawableWithInset(new LayerDrawable(new Drawable[]{this.tintableBackgroundDrawableCompat, this.tintableRippleDrawableCompat}));
   }

   @TargetApi(21)
   private Drawable createBackgroundLollipop() {
      this.backgroundDrawableLollipop = new GradientDrawable();
      this.backgroundDrawableLollipop.setCornerRadius((float)this.cornerRadius + 1.0E-5F);
      this.backgroundDrawableLollipop.setColor(-1);
      this.updateTintAndTintModeLollipop();
      this.strokeDrawableLollipop = new GradientDrawable();
      this.strokeDrawableLollipop.setCornerRadius((float)this.cornerRadius + 1.0E-5F);
      this.strokeDrawableLollipop.setColor(0);
      this.strokeDrawableLollipop.setStroke(this.strokeWidth, this.strokeColor);
      InsetDrawable var1 = this.wrapDrawableWithInset(new LayerDrawable(new Drawable[]{this.backgroundDrawableLollipop, this.strokeDrawableLollipop}));
      this.maskDrawableLollipop = new GradientDrawable();
      this.maskDrawableLollipop.setCornerRadius((float)this.cornerRadius + 1.0E-5F);
      this.maskDrawableLollipop.setColor(-1);
      return new MaterialButtonBackgroundDrawable(RippleUtils.convertToRippleDrawableColor(this.rippleColor), var1, this.maskDrawableLollipop);
   }

   private GradientDrawable unwrapBackgroundDrawable() {
      return IS_LOLLIPOP && this.materialButton.getBackground() != null ? (GradientDrawable)((LayerDrawable)((InsetDrawable)((RippleDrawable)this.materialButton.getBackground()).getDrawable(0)).getDrawable()).getDrawable(0) : null;
   }

   private GradientDrawable unwrapStrokeDrawable() {
      return IS_LOLLIPOP && this.materialButton.getBackground() != null ? (GradientDrawable)((LayerDrawable)((InsetDrawable)((RippleDrawable)this.materialButton.getBackground()).getDrawable(0)).getDrawable()).getDrawable(1) : null;
   }

   private void updateStroke() {
      if (IS_LOLLIPOP && this.strokeDrawableLollipop != null) {
         this.materialButton.setInternalBackground(this.createBackgroundLollipop());
      } else if (!IS_LOLLIPOP) {
         this.materialButton.invalidate();
      }

   }

   private void updateTintAndTintModeLollipop() {
      if (this.backgroundDrawableLollipop != null) {
         DrawableCompat.setTintList(this.backgroundDrawableLollipop, this.backgroundTint);
         if (this.backgroundTintMode != null) {
            DrawableCompat.setTintMode(this.backgroundDrawableLollipop, this.backgroundTintMode);
         }
      }

   }

   private InsetDrawable wrapDrawableWithInset(Drawable var1) {
      return new InsetDrawable(var1, this.insetLeft, this.insetTop, this.insetRight, this.insetBottom);
   }

   void drawStroke(Canvas var1) {
      if (var1 != null && this.strokeColor != null && this.strokeWidth > 0) {
         this.bounds.set(this.materialButton.getBackground().getBounds());
         this.rectF.set((float)this.bounds.left + (float)this.strokeWidth / 2.0F + (float)this.insetLeft, (float)this.bounds.top + (float)this.strokeWidth / 2.0F + (float)this.insetTop, (float)this.bounds.right - (float)this.strokeWidth / 2.0F - (float)this.insetRight, (float)this.bounds.bottom - (float)this.strokeWidth / 2.0F - (float)this.insetBottom);
         float var2 = (float)this.cornerRadius - (float)this.strokeWidth / 2.0F;
         var1.drawRoundRect(this.rectF, var2, var2, this.buttonStrokePaint);
      }

   }

   int getCornerRadius() {
      return this.cornerRadius;
   }

   ColorStateList getRippleColor() {
      return this.rippleColor;
   }

   ColorStateList getStrokeColor() {
      return this.strokeColor;
   }

   int getStrokeWidth() {
      return this.strokeWidth;
   }

   ColorStateList getSupportBackgroundTintList() {
      return this.backgroundTint;
   }

   Mode getSupportBackgroundTintMode() {
      return this.backgroundTintMode;
   }

   boolean isBackgroundOverwritten() {
      return this.backgroundOverwritten;
   }

   public void loadFromAttributes(TypedArray var1) {
      int var2 = R.styleable.MaterialButton_android_insetLeft;
      int var3 = 0;
      this.insetLeft = var1.getDimensionPixelOffset(var2, 0);
      this.insetRight = var1.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetRight, 0);
      this.insetTop = var1.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetTop, 0);
      this.insetBottom = var1.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetBottom, 0);
      this.cornerRadius = var1.getDimensionPixelSize(R.styleable.MaterialButton_cornerRadius, 0);
      this.strokeWidth = var1.getDimensionPixelSize(R.styleable.MaterialButton_strokeWidth, 0);
      this.backgroundTintMode = ViewUtils.parseTintMode(var1.getInt(R.styleable.MaterialButton_backgroundTintMode, -1), Mode.SRC_IN);
      this.backgroundTint = MaterialResources.getColorStateList(this.materialButton.getContext(), var1, R.styleable.MaterialButton_backgroundTint);
      this.strokeColor = MaterialResources.getColorStateList(this.materialButton.getContext(), var1, R.styleable.MaterialButton_strokeColor);
      this.rippleColor = MaterialResources.getColorStateList(this.materialButton.getContext(), var1, R.styleable.MaterialButton_rippleColor);
      this.buttonStrokePaint.setStyle(Style.STROKE);
      this.buttonStrokePaint.setStrokeWidth((float)this.strokeWidth);
      Paint var7 = this.buttonStrokePaint;
      if (this.strokeColor != null) {
         var3 = this.strokeColor.getColorForState(this.materialButton.getDrawableState(), 0);
      }

      var7.setColor(var3);
      var2 = ViewCompat.getPaddingStart(this.materialButton);
      int var4 = this.materialButton.getPaddingTop();
      var3 = ViewCompat.getPaddingEnd(this.materialButton);
      int var5 = this.materialButton.getPaddingBottom();
      MaterialButton var6 = this.materialButton;
      Drawable var8;
      if (IS_LOLLIPOP) {
         var8 = this.createBackgroundLollipop();
      } else {
         var8 = this.createBackgroundCompat();
      }

      var6.setInternalBackground(var8);
      ViewCompat.setPaddingRelative(this.materialButton, var2 + this.insetLeft, var4 + this.insetTop, var3 + this.insetRight, var5 + this.insetBottom);
   }

   void setBackgroundColor(int var1) {
      if (IS_LOLLIPOP && this.backgroundDrawableLollipop != null) {
         this.backgroundDrawableLollipop.setColor(var1);
      } else if (!IS_LOLLIPOP && this.colorableBackgroundDrawableCompat != null) {
         this.colorableBackgroundDrawableCompat.setColor(var1);
      }

   }

   void setBackgroundOverwritten() {
      this.backgroundOverwritten = true;
      this.materialButton.setSupportBackgroundTintList(this.backgroundTint);
      this.materialButton.setSupportBackgroundTintMode(this.backgroundTintMode);
   }

   void setCornerRadius(int var1) {
      if (this.cornerRadius != var1) {
         this.cornerRadius = var1;
         GradientDrawable var2;
         float var3;
         if (IS_LOLLIPOP && this.backgroundDrawableLollipop != null && this.strokeDrawableLollipop != null && this.maskDrawableLollipop != null) {
            if (VERSION.SDK_INT == 21) {
               var2 = this.unwrapBackgroundDrawable();
               var3 = (float)var1 + 1.0E-5F;
               var2.setCornerRadius(var3);
               this.unwrapStrokeDrawable().setCornerRadius(var3);
            }

            var2 = this.backgroundDrawableLollipop;
            var3 = (float)var1 + 1.0E-5F;
            var2.setCornerRadius(var3);
            this.strokeDrawableLollipop.setCornerRadius(var3);
            this.maskDrawableLollipop.setCornerRadius(var3);
         } else if (!IS_LOLLIPOP && this.colorableBackgroundDrawableCompat != null && this.rippleDrawableCompat != null) {
            var2 = this.colorableBackgroundDrawableCompat;
            var3 = (float)var1 + 1.0E-5F;
            var2.setCornerRadius(var3);
            this.rippleDrawableCompat.setCornerRadius(var3);
            this.materialButton.invalidate();
         }
      }

   }

   void setRippleColor(ColorStateList var1) {
      if (this.rippleColor != var1) {
         this.rippleColor = var1;
         if (IS_LOLLIPOP && this.materialButton.getBackground() instanceof RippleDrawable) {
            ((RippleDrawable)this.materialButton.getBackground()).setColor(var1);
         } else if (!IS_LOLLIPOP && this.tintableRippleDrawableCompat != null) {
            DrawableCompat.setTintList(this.tintableRippleDrawableCompat, var1);
         }
      }

   }

   void setStrokeColor(ColorStateList var1) {
      if (this.strokeColor != var1) {
         this.strokeColor = var1;
         Paint var2 = this.buttonStrokePaint;
         int var3 = 0;
         if (var1 != null) {
            var3 = var1.getColorForState(this.materialButton.getDrawableState(), 0);
         }

         var2.setColor(var3);
         this.updateStroke();
      }

   }

   void setStrokeWidth(int var1) {
      if (this.strokeWidth != var1) {
         this.strokeWidth = var1;
         this.buttonStrokePaint.setStrokeWidth((float)var1);
         this.updateStroke();
      }

   }

   void setSupportBackgroundTintList(ColorStateList var1) {
      if (this.backgroundTint != var1) {
         this.backgroundTint = var1;
         if (IS_LOLLIPOP) {
            this.updateTintAndTintModeLollipop();
         } else if (this.tintableBackgroundDrawableCompat != null) {
            DrawableCompat.setTintList(this.tintableBackgroundDrawableCompat, this.backgroundTint);
         }
      }

   }

   void setSupportBackgroundTintMode(Mode var1) {
      if (this.backgroundTintMode != var1) {
         this.backgroundTintMode = var1;
         if (IS_LOLLIPOP) {
            this.updateTintAndTintModeLollipop();
         } else if (this.tintableBackgroundDrawableCompat != null && this.backgroundTintMode != null) {
            DrawableCompat.setTintMode(this.tintableBackgroundDrawableCompat, this.backgroundTintMode);
         }
      }

   }

   void updateMaskBounds(int var1, int var2) {
      if (this.maskDrawableLollipop != null) {
         this.maskDrawableLollipop.setBounds(this.insetLeft, this.insetTop, var2 - this.insetRight, var1 - this.insetBottom);
      }

   }
}
