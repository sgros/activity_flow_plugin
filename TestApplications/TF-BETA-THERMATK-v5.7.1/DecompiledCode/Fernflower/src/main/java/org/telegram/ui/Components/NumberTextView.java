package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.view.View;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;

public class NumberTextView extends View {
   private ObjectAnimator animator;
   private int currentNumber = 1;
   private ArrayList letters = new ArrayList();
   private ArrayList oldLetters = new ArrayList();
   private float progress = 0.0F;
   private TextPaint textPaint = new TextPaint(1);

   public NumberTextView(Context var1) {
      super(var1);
   }

   public float getProgress() {
      return this.progress;
   }

   protected void onDraw(Canvas var1) {
      if (!this.letters.isEmpty()) {
         float var2 = (float)((StaticLayout)this.letters.get(0)).getHeight();
         var1.save();
         var1.translate((float)this.getPaddingLeft(), ((float)this.getMeasuredHeight() - var2) / 2.0F);
         int var3 = Math.max(this.letters.size(), this.oldLetters.size());

         for(int var4 = 0; var4 < var3; ++var4) {
            var1.save();
            int var5 = this.oldLetters.size();
            StaticLayout var6 = null;
            StaticLayout var7;
            if (var4 < var5) {
               var7 = (StaticLayout)this.oldLetters.get(var4);
            } else {
               var7 = null;
            }

            if (var4 < this.letters.size()) {
               var6 = (StaticLayout)this.letters.get(var4);
            }

            float var8 = this.progress;
            if (var8 > 0.0F) {
               if (var7 != null) {
                  this.textPaint.setAlpha((int)(var8 * 255.0F));
                  var1.save();
                  var1.translate(0.0F, (this.progress - 1.0F) * var2);
                  var7.draw(var1);
                  var1.restore();
                  if (var6 != null) {
                     this.textPaint.setAlpha((int)((1.0F - this.progress) * 255.0F));
                     var1.translate(0.0F, this.progress * var2);
                  }
               } else {
                  this.textPaint.setAlpha(255);
               }
            } else if (var8 < 0.0F) {
               if (var7 != null) {
                  this.textPaint.setAlpha((int)(-var8 * 255.0F));
                  var1.save();
                  var1.translate(0.0F, (this.progress + 1.0F) * var2);
                  var7.draw(var1);
                  var1.restore();
               }

               if (var6 != null) {
                  if (var4 != var3 - 1 && var7 == null) {
                     this.textPaint.setAlpha(255);
                  } else {
                     this.textPaint.setAlpha((int)((this.progress + 1.0F) * 255.0F));
                     var1.translate(0.0F, this.progress * var2);
                  }
               }
            } else if (var6 != null) {
               this.textPaint.setAlpha(255);
            }

            if (var6 != null) {
               var6.draw(var1);
            }

            var1.restore();
            if (var6 != null) {
               var8 = var6.getLineWidth(0);
            } else {
               var8 = var7.getLineWidth(0) + (float)AndroidUtilities.dp(1.0F);
            }

            var1.translate(var8, 0.0F);
         }

         var1.restore();
      }
   }

   public void setNumber(int var1, boolean var2) {
      if (this.currentNumber != var1 || !var2) {
         ObjectAnimator var3 = this.animator;
         if (var3 != null) {
            var3.cancel();
            this.animator = null;
         }

         this.oldLetters.clear();
         this.oldLetters.addAll(this.letters);
         this.letters.clear();
         String var4 = String.format(Locale.US, "%d", this.currentNumber);
         String var5 = String.format(Locale.US, "%d", var1);
         boolean var6;
         if (var1 > this.currentNumber) {
            var6 = true;
         } else {
            var6 = false;
         }

         this.currentNumber = var1;
         this.progress = 0.0F;

         int var7;
         for(var1 = 0; var1 < var5.length(); var1 = var7) {
            var7 = var1 + 1;
            String var8 = var5.substring(var1, var7);
            String var10;
            if (!this.oldLetters.isEmpty() && var1 < var4.length()) {
               var10 = var4.substring(var1, var7);
            } else {
               var10 = null;
            }

            if (var10 != null && var10.equals(var8)) {
               this.letters.add(this.oldLetters.get(var1));
               this.oldLetters.set(var1, (Object)null);
            } else {
               TextPaint var11 = this.textPaint;
               StaticLayout var12 = new StaticLayout(var8, var11, (int)Math.ceil((double)var11.measureText(var8)), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
               this.letters.add(var12);
            }
         }

         if (var2 && !this.oldLetters.isEmpty()) {
            float var9;
            if (var6) {
               var9 = -1.0F;
            } else {
               var9 = 1.0F;
            }

            this.animator = ObjectAnimator.ofFloat(this, "progress", new float[]{var9, 0.0F});
            this.animator.setDuration(150L);
            this.animator.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  NumberTextView.this.animator = null;
                  NumberTextView.this.oldLetters.clear();
               }
            });
            this.animator.start();
         }

         this.invalidate();
      }
   }

   public void setProgress(float var1) {
      if (this.progress != var1) {
         this.progress = var1;
         this.invalidate();
      }
   }

   public void setTextColor(int var1) {
      this.textPaint.setColor(var1);
      this.invalidate();
   }

   public void setTextSize(int var1) {
      this.textPaint.setTextSize((float)AndroidUtilities.dp((float)var1));
      this.oldLetters.clear();
      this.letters.clear();
      this.setNumber(this.currentNumber, false);
   }

   public void setTypeface(Typeface var1) {
      this.textPaint.setTypeface(var1);
      this.oldLetters.clear();
      this.letters.clear();
      this.setNumber(this.currentNumber, false);
   }
}
