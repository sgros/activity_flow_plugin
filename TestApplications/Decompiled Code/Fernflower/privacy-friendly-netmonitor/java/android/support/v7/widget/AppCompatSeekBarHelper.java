package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.widget.SeekBar;

class AppCompatSeekBarHelper extends AppCompatProgressBarHelper {
   private boolean mHasTickMarkTint = false;
   private boolean mHasTickMarkTintMode = false;
   private Drawable mTickMark;
   private ColorStateList mTickMarkTintList = null;
   private Mode mTickMarkTintMode = null;
   private final SeekBar mView;

   AppCompatSeekBarHelper(SeekBar var1) {
      super(var1);
      this.mView = var1;
   }

   private void applyTickMarkTint() {
      if (this.mTickMark != null && (this.mHasTickMarkTint || this.mHasTickMarkTintMode)) {
         this.mTickMark = DrawableCompat.wrap(this.mTickMark.mutate());
         if (this.mHasTickMarkTint) {
            DrawableCompat.setTintList(this.mTickMark, this.mTickMarkTintList);
         }

         if (this.mHasTickMarkTintMode) {
            DrawableCompat.setTintMode(this.mTickMark, this.mTickMarkTintMode);
         }

         if (this.mTickMark.isStateful()) {
            this.mTickMark.setState(this.mView.getDrawableState());
         }
      }

   }

   void drawTickMarks(Canvas var1) {
      if (this.mTickMark != null) {
         int var2 = this.mView.getMax();
         int var3 = 1;
         if (var2 > 1) {
            int var4 = this.mTickMark.getIntrinsicWidth();
            int var5 = this.mTickMark.getIntrinsicHeight();
            if (var4 >= 0) {
               var4 /= 2;
            } else {
               var4 = 1;
            }

            if (var5 >= 0) {
               var3 = var5 / 2;
            }

            this.mTickMark.setBounds(-var4, -var3, var4, var3);
            float var6 = (float)(this.mView.getWidth() - this.mView.getPaddingLeft() - this.mView.getPaddingRight()) / (float)var2;
            var3 = var1.save();
            var1.translate((float)this.mView.getPaddingLeft(), (float)(this.mView.getHeight() / 2));

            for(var4 = 0; var4 <= var2; ++var4) {
               this.mTickMark.draw(var1);
               var1.translate(var6, 0.0F);
            }

            var1.restoreToCount(var3);
         }
      }

   }

   void drawableStateChanged() {
      Drawable var1 = this.mTickMark;
      if (var1 != null && var1.isStateful() && var1.setState(this.mView.getDrawableState())) {
         this.mView.invalidateDrawable(var1);
      }

   }

   @Nullable
   Drawable getTickMark() {
      return this.mTickMark;
   }

   @Nullable
   ColorStateList getTickMarkTintList() {
      return this.mTickMarkTintList;
   }

   @Nullable
   Mode getTickMarkTintMode() {
      return this.mTickMarkTintMode;
   }

   @RequiresApi(11)
   void jumpDrawablesToCurrentState() {
      if (this.mTickMark != null) {
         this.mTickMark.jumpToCurrentState();
      }

   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      super.loadFromAttributes(var1, var2);
      TintTypedArray var3 = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), var1, R.styleable.AppCompatSeekBar, var2, 0);
      Drawable var4 = var3.getDrawableIfKnown(R.styleable.AppCompatSeekBar_android_thumb);
      if (var4 != null) {
         this.mView.setThumb(var4);
      }

      this.setTickMark(var3.getDrawable(R.styleable.AppCompatSeekBar_tickMark));
      if (var3.hasValue(R.styleable.AppCompatSeekBar_tickMarkTintMode)) {
         this.mTickMarkTintMode = DrawableUtils.parseTintMode(var3.getInt(R.styleable.AppCompatSeekBar_tickMarkTintMode, -1), this.mTickMarkTintMode);
         this.mHasTickMarkTintMode = true;
      }

      if (var3.hasValue(R.styleable.AppCompatSeekBar_tickMarkTint)) {
         this.mTickMarkTintList = var3.getColorStateList(R.styleable.AppCompatSeekBar_tickMarkTint);
         this.mHasTickMarkTint = true;
      }

      var3.recycle();
      this.applyTickMarkTint();
   }

   void setTickMark(@Nullable Drawable var1) {
      if (this.mTickMark != null) {
         this.mTickMark.setCallback((Callback)null);
      }

      this.mTickMark = var1;
      if (var1 != null) {
         var1.setCallback(this.mView);
         DrawableCompat.setLayoutDirection(var1, ViewCompat.getLayoutDirection(this.mView));
         if (var1.isStateful()) {
            var1.setState(this.mView.getDrawableState());
         }

         this.applyTickMarkTint();
      }

      this.mView.invalidate();
   }

   void setTickMarkTintList(@Nullable ColorStateList var1) {
      this.mTickMarkTintList = var1;
      this.mHasTickMarkTint = true;
      this.applyTickMarkTint();
   }

   void setTickMarkTintMode(@Nullable Mode var1) {
      this.mTickMarkTintMode = var1;
      this.mHasTickMarkTintMode = true;
      this.applyTickMarkTint();
   }
}
