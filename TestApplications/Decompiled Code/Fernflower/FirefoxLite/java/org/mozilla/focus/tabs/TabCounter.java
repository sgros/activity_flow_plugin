package org.mozilla.focus.tabs;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import java.text.NumberFormat;
import org.mozilla.focus.R;
import org.mozilla.focus.utils.DrawableUtils;
import org.mozilla.rocket.nightmode.themed.ThemedImageView;
import org.mozilla.rocket.nightmode.themed.ThemedRelativeLayout;
import org.mozilla.rocket.nightmode.themed.ThemedTextView;

public class TabCounter extends ThemedRelativeLayout {
   private final AnimatorSet animationSet;
   private final ThemedImageView bar;
   private final ThemedImageView box;
   private int count;
   private float currentTextRatio;
   private ColorStateList menuIconColor;
   private final ThemedTextView text;

   public TabCounter(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public TabCounter(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public TabCounter(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      int var4 = var1.getResources().getColor(2131099720);
      TypedArray var5 = var1.obtainStyledAttributes(var2, R.styleable.TabCounter, var3, 0);
      this.menuIconColor = var5.getColorStateList(0);
      var5.recycle();
      LayoutInflater.from(var1).inflate(2131492919, this);
      this.box = (ThemedImageView)this.findViewById(2131296384);
      this.bar = (ThemedImageView)this.findViewById(2131296383);
      this.text = (ThemedTextView)this.findViewById(2131296386);
      this.text.setText(":)");
      var3 = (int)TypedValue.applyDimension(1, 1.0F, var1.getResources().getDisplayMetrics());
      this.text.setPadding(0, 0, 0, var3);
      if (this.menuIconColor.getDefaultColor() != var4) {
         this.tintDrawables(this.menuIconColor);
      }

      this.animationSet = this.createAnimatorSet();
   }

   private void adjustTextSize(int var1) {
      final float var2;
      if (var1 <= 99 && var1 >= 10) {
         var2 = 0.5F;
      } else {
         var2 = 0.6F;
      }

      if (var2 != this.currentTextRatio) {
         this.currentTextRatio = var2;
         this.text.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
               TabCounter.this.text.getViewTreeObserver().removeOnGlobalLayoutListener(this);
               int var1 = (int)((float)TabCounter.this.box.getWidth() * var2);
               if (var1 > 0) {
                  TabCounter.this.text.setTextSize(0, (float)var1);
               }

            }
         });
      }

   }

   private AnimatorSet createAnimatorSet() {
      AnimatorSet var1 = new AnimatorSet();
      this.createBoxAnimatorSet(var1);
      this.createBarAnimatorSet(var1);
      this.createTextAnimatorSet(var1);
      return var1;
   }

   private void createBarAnimatorSet(AnimatorSet var1) {
      Animator var2 = (Animator)var1.getChildAnimations().get(0);
      ObjectAnimator var3 = ObjectAnimator.ofFloat(this.bar, "translationY", new float[]{0.0F, -7.0F}).setDuration(100L);
      ObjectAnimator var4 = ObjectAnimator.ofFloat(this.bar, "alpha", new float[]{1.0F, 0.0F}).setDuration(66L);
      var4.setStartDelay(48L);
      ObjectAnimator var5 = ObjectAnimator.ofFloat(this.bar, "translationY", new float[]{-7.0F, 0.0F}).setDuration(16L);
      ObjectAnimator var6 = ObjectAnimator.ofFloat(this.bar, "scaleX", new float[]{0.31F, 1.0F}).setDuration(166L);
      var6.setStartDelay(176L);
      ObjectAnimator var7 = ObjectAnimator.ofFloat(this.bar, "alpha", new float[]{0.0F, 1.0F}).setDuration(166L);
      var7.setStartDelay(176L);
      var1.play(var2).with(var3);
      var1.play(var2).before(var4);
      var1.play(var4).before(var5);
      var1.play(var5).before(var6);
      var1.play(var6).with(var7);
   }

   private void createBoxAnimatorSet(AnimatorSet var1) {
      ObjectAnimator var2 = ObjectAnimator.ofFloat(this.box, "alpha", new float[]{1.0F, 0.0F}).setDuration(33L);
      ObjectAnimator var3 = ObjectAnimator.ofFloat(this.box, "translationY", new float[]{0.0F, -5.3F}).setDuration(50L);
      ObjectAnimator var4 = ObjectAnimator.ofFloat(this.box, "translationY", new float[]{-5.3F, -1.0F}).setDuration(116L);
      ObjectAnimator var5 = ObjectAnimator.ofFloat(this.box, "alpha", new float[]{0.01F, 1.0F}).setDuration(66L);
      ObjectAnimator var6 = ObjectAnimator.ofFloat(this.box, "translationY", new float[]{-1.0F, 2.7F}).setDuration(116L);
      ObjectAnimator var7 = ObjectAnimator.ofFloat(this.box, "translationY", new float[]{2.7F, 0.0F}).setDuration(133L);
      ObjectAnimator var8 = ObjectAnimator.ofFloat(this.box, "scaleY", new float[]{0.02F, 1.05F}).setDuration(100L);
      var8.setStartDelay(16L);
      ObjectAnimator var9 = ObjectAnimator.ofFloat(this.box, "scaleY", new float[]{1.05F, 0.99F}).setDuration(116L);
      ObjectAnimator var10 = ObjectAnimator.ofFloat(this.box, "scaleY", new float[]{0.99F, 1.0F}).setDuration(133L);
      var1.play(var2).with(var3);
      var1.play(var3).before(var4);
      var1.play(var4).with(var5);
      var1.play(var4).before(var6);
      var1.play(var6).before(var7);
      var1.play(var3).before(var8);
      var1.play(var8).before(var9);
      var1.play(var9).before(var10);
   }

   private void createTextAnimatorSet(AnimatorSet var1) {
      Animator var2 = (Animator)var1.getChildAnimations().get(0);
      ObjectAnimator var3 = ObjectAnimator.ofFloat(this.text, "alpha", new float[]{1.0F, 0.0F}).setDuration(33L);
      ObjectAnimator var4 = ObjectAnimator.ofFloat(this.text, "alpha", new float[]{0.0F, 1.0F}).setDuration(66L);
      var4.setStartDelay(96L);
      ObjectAnimator var5 = ObjectAnimator.ofFloat(this.text, "translationY", new float[]{0.0F, 4.4F}).setDuration(66L);
      var5.setStartDelay(96L);
      ObjectAnimator var6 = ObjectAnimator.ofFloat(this.text, "translationY", new float[]{4.4F, 0.0F}).setDuration(66L);
      var1.play(var2).with(var3);
      var1.play(var3).before(var4);
      var1.play(var4).with(var5);
      var1.play(var5).before(var6);
   }

   private String formatForDisplay(int var1) {
      return var1 > 99 ? "∞" : NumberFormat.getInstance().format((long)var1);
   }

   private void tintDrawables(ColorStateList var1) {
      Drawable var2 = DrawableUtils.loadAndTintDrawable(this.getContext(), 2131230970, var1.getDefaultColor());
      this.box.setImageDrawable(var2);
      this.box.setImageTintList(var1);
      var2 = DrawableUtils.loadAndTintDrawable(this.getContext(), 2131230969, var1.getDefaultColor());
      this.bar.setImageDrawable(var2);
      this.bar.setImageTintList(var1);
      this.text.setTextColor(var1);
   }

   public CharSequence getText() {
      return this.text.getText();
   }

   public void setCount(int var1) {
      this.adjustTextSize(var1);
      this.text.setPadding(0, 0, 0, 0);
      this.text.setText(this.formatForDisplay(var1));
      this.count = var1;
   }

   public void setCountWithAnimation(int var1) {
      if (this.count == 0) {
         this.setCount(var1);
      } else if (this.count != var1) {
         if (this.count > 99 && var1 > 99) {
            this.count = var1;
         } else {
            this.adjustTextSize(var1);
            this.text.setPadding(0, 0, 0, 0);
            this.text.setText(this.formatForDisplay(var1));
            this.count = var1;
            if (this.animationSet.isRunning()) {
               this.animationSet.cancel();
            }

            this.animationSet.start();
         }
      }
   }

   public void setNightMode(boolean var1) {
      super.setNightMode(var1);
      this.tintDrawables(this.menuIconColor);
      this.bar.setNightMode(var1);
      this.box.setNightMode(var1);
      this.text.setNightMode(var1);
   }
}
