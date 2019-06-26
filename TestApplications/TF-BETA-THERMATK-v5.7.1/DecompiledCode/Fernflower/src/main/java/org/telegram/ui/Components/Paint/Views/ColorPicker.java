package org.telegram.ui.Components.Paint.Views;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Paint.Swatch;

public class ColorPicker extends FrameLayout {
   private static final int[] COLORS = new int[]{-1431751, -2409774, -13610525, -11942419, -8337308, -205211, -223667, -16777216, -1};
   private static final float[] LOCATIONS = new float[]{0.0F, 0.14F, 0.24F, 0.39F, 0.49F, 0.62F, 0.73F, 0.85F, 1.0F};
   private Paint backgroundPaint = new Paint(1);
   private boolean changingWeight;
   private ColorPicker.ColorPickerDelegate delegate;
   private boolean dragging;
   private float draggingFactor;
   private Paint gradientPaint = new Paint(1);
   private boolean interacting;
   private OvershootInterpolator interpolator = new OvershootInterpolator(1.02F);
   private float location = 1.0F;
   private RectF rectF = new RectF();
   private ImageView settingsButton;
   private Drawable shadowDrawable;
   private Paint swatchPaint = new Paint(1);
   private Paint swatchStrokePaint = new Paint(1);
   private ImageView undoButton;
   private boolean wasChangingWeight;
   private float weight = 0.27F;

   public ColorPicker(Context var1) {
      super(var1);
      this.setWillNotDraw(false);
      this.shadowDrawable = this.getResources().getDrawable(2131165520);
      this.backgroundPaint.setColor(-1);
      this.swatchStrokePaint.setStyle(Style.STROKE);
      this.swatchStrokePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0F));
      this.settingsButton = new ImageView(var1);
      this.settingsButton.setScaleType(ScaleType.CENTER);
      this.settingsButton.setImageResource(2131165746);
      this.addView(this.settingsButton, LayoutHelper.createFrame(60, 52.0F));
      this.settingsButton.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if (ColorPicker.this.delegate != null) {
               ColorPicker.this.delegate.onSettingsPressed();
            }

         }
      });
      this.undoButton = new ImageView(var1);
      this.undoButton.setScaleType(ScaleType.CENTER);
      this.undoButton.setImageResource(2131165752);
      this.addView(this.undoButton, LayoutHelper.createFrame(60, 52.0F));
      this.undoButton.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if (ColorPicker.this.delegate != null) {
               ColorPicker.this.delegate.onUndoPressed();
            }

         }
      });
      this.location = var1.getSharedPreferences("paint", 0).getFloat("last_color_location", 1.0F);
      this.setLocation(this.location);
   }

   private int interpolateColors(int var1, int var2, float var3) {
      var3 = Math.min(Math.max(var3, 0.0F), 1.0F);
      int var4 = Color.red(var1);
      int var5 = Color.red(var2);
      int var6 = Color.green(var1);
      int var7 = Color.green(var2);
      var1 = Color.blue(var1);
      var2 = Color.blue(var2);
      return Color.argb(255, Math.min(255, (int)((float)var4 + (float)(var5 - var4) * var3)), Math.min(255, (int)((float)var6 + (float)(var7 - var6) * var3)), Math.min(255, (int)((float)var1 + (float)(var2 - var1) * var3)));
   }

   private void setDragging(boolean var1, boolean var2) {
      if (this.dragging != var1) {
         this.dragging = var1;
         float var3;
         if (this.dragging) {
            var3 = 1.0F;
         } else {
            var3 = 0.0F;
         }

         if (var2) {
            ObjectAnimator var4 = ObjectAnimator.ofFloat(this, "draggingFactor", new float[]{this.draggingFactor, var3});
            var4.setInterpolator(this.interpolator);
            int var5 = 300;
            if (this.wasChangingWeight) {
               var5 = (int)((float)300 + this.weight * 75.0F);
            }

            var4.setDuration((long)var5);
            var4.start();
         } else {
            this.setDraggingFactor(var3);
         }

      }
   }

   private void setDraggingFactor(float var1) {
      this.draggingFactor = var1;
      this.invalidate();
   }

   public int colorForLocation(float var1) {
      if (var1 <= 0.0F) {
         return COLORS[0];
      } else {
         int var2 = 1;
         if (var1 >= 1.0F) {
            int[] var9 = COLORS;
            return var9[var9.length - 1];
         } else {
            float[] var3;
            int var5;
            while(true) {
               var3 = LOCATIONS;
               int var4 = var3.length;
               var5 = -1;
               if (var2 >= var4) {
                  var2 = -1;
                  break;
               }

               if (var3[var2] > var1) {
                  var5 = var2 - 1;
                  break;
               }

               ++var2;
            }

            var3 = LOCATIONS;
            float var6 = var3[var5];
            int[] var7 = COLORS;
            var5 = var7[var5];
            float var8 = var3[var2];
            return this.interpolateColors(var5, var7[var2], (var1 - var6) / (var8 - var6));
         }
      }
   }

   public float getDraggingFactor() {
      return this.draggingFactor;
   }

   public View getSettingsButton() {
      return this.settingsButton;
   }

   public Swatch getSwatch() {
      return new Swatch(this.colorForLocation(this.location), this.location, this.weight);
   }

   protected void onDraw(Canvas var1) {
      var1.drawRoundRect(this.rectF, (float)AndroidUtilities.dp(6.0F), (float)AndroidUtilities.dp(6.0F), this.gradientPaint);
      RectF var2 = this.rectF;
      int var3 = (int)(var2.left + var2.width() * this.location);
      float var4 = this.rectF.centerY();
      float var5 = this.draggingFactor;
      float var6 = (float)(-AndroidUtilities.dp(70.0F));
      float var7;
      if (this.changingWeight) {
         var7 = this.weight * (float)AndroidUtilities.dp(190.0F);
      } else {
         var7 = 0.0F;
      }

      int var8 = (int)(var4 + var5 * var6 - var7);
      int var9 = (int)((float)AndroidUtilities.dp(24.0F) * (this.draggingFactor + 1.0F) * 0.5F);
      this.shadowDrawable.setBounds(var3 - var9, var8 - var9, var3 + var9, var9 + var8);
      this.shadowDrawable.draw(var1);
      var4 = (float)((int)Math.floor((double)((float)AndroidUtilities.dp(4.0F) + (float)(AndroidUtilities.dp(19.0F) - AndroidUtilities.dp(4.0F)) * this.weight))) * (this.draggingFactor + 1.0F) / 2.0F;
      var7 = (float)var3;
      var5 = (float)var8;
      var1.drawCircle(var7, var5, (float)(AndroidUtilities.dp(22.0F) / 2) * (this.draggingFactor + 1.0F), this.backgroundPaint);
      var1.drawCircle(var7, var5, var4, this.swatchPaint);
      var1.drawCircle(var7, var5, var4 - (float)AndroidUtilities.dp(0.5F), this.swatchStrokePaint);
   }

   @SuppressLint({"DrawAllocation"})
   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var2 = var4 - var2;
      var3 = var5 - var3;
      this.gradientPaint.setShader(new LinearGradient((float)AndroidUtilities.dp(56.0F), 0.0F, (float)(var2 - AndroidUtilities.dp(56.0F)), 0.0F, COLORS, LOCATIONS, TileMode.REPEAT));
      var4 = var3 - AndroidUtilities.dp(32.0F);
      this.rectF.set((float)AndroidUtilities.dp(56.0F), (float)var4, (float)(var2 - AndroidUtilities.dp(56.0F)), (float)(var4 + AndroidUtilities.dp(12.0F)));
      ImageView var6 = this.settingsButton;
      var6.layout(var2 - var6.getMeasuredWidth(), var3 - AndroidUtilities.dp(52.0F), var2, var3);
      this.undoButton.layout(0, var3 - AndroidUtilities.dp(52.0F), this.settingsButton.getMeasuredWidth(), var3);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (var1.getPointerCount() > 1) {
         return false;
      } else {
         float var2 = var1.getX();
         float var3 = this.rectF.left;
         float var4 = var1.getY() - this.rectF.top;
         if (!this.interacting && var4 < (float)(-AndroidUtilities.dp(10.0F))) {
            return false;
         } else {
            int var5 = var1.getActionMasked();
            ColorPicker.ColorPickerDelegate var6;
            if (var5 != 3 && var5 != 1 && var5 != 6) {
               if (var5 == 0 || var5 == 2) {
                  if (!this.interacting) {
                     this.interacting = true;
                     var6 = this.delegate;
                     if (var6 != null) {
                        var6.onBeganColorPicking();
                     }
                  }

                  this.setLocation(Math.max(0.0F, Math.min(1.0F, (var2 - var3) / this.rectF.width())));
                  this.setDragging(true, true);
                  if (var4 < (float)(-AndroidUtilities.dp(10.0F))) {
                     this.changingWeight = true;
                     this.setWeight(Math.max(0.0F, Math.min(1.0F, (-var4 - (float)AndroidUtilities.dp(10.0F)) / (float)AndroidUtilities.dp(190.0F))));
                  }

                  var6 = this.delegate;
                  if (var6 != null) {
                     var6.onColorValueChanged();
                  }

                  return true;
               }
            } else {
               if (this.interacting) {
                  var6 = this.delegate;
                  if (var6 != null) {
                     var6.onFinishedColorPicking();
                     this.getContext().getSharedPreferences("paint", 0).edit().putFloat("last_color_location", this.location).commit();
                  }
               }

               this.interacting = false;
               this.wasChangingWeight = this.changingWeight;
               this.changingWeight = false;
               this.setDragging(false, true);
            }

            return false;
         }
      }
   }

   public void setDelegate(ColorPicker.ColorPickerDelegate var1) {
      this.delegate = var1;
   }

   public void setLocation(float var1) {
      this.location = var1;
      int var2 = this.colorForLocation(var1);
      this.swatchPaint.setColor(var2);
      float[] var3 = new float[3];
      Color.colorToHSV(var2, var3);
      if ((double)var3[0] < 0.001D && (double)var3[1] < 0.001D && var3[2] > 0.92F) {
         var2 = (int)((1.0F - (var3[2] - 0.92F) / 0.08F * 0.22F) * 255.0F);
         this.swatchStrokePaint.setColor(Color.rgb(var2, var2, var2));
      } else {
         this.swatchStrokePaint.setColor(var2);
      }

      this.invalidate();
   }

   public void setSettingsButtonImage(int var1) {
      this.settingsButton.setImageResource(var1);
   }

   public void setSwatch(Swatch var1) {
      this.setLocation(var1.colorLocation);
      this.setWeight(var1.brushWeight);
   }

   public void setUndoEnabled(boolean var1) {
      ImageView var2 = this.undoButton;
      float var3;
      if (var1) {
         var3 = 1.0F;
      } else {
         var3 = 0.3F;
      }

      var2.setAlpha(var3);
      this.undoButton.setEnabled(var1);
   }

   public void setWeight(float var1) {
      this.weight = var1;
      this.invalidate();
   }

   public interface ColorPickerDelegate {
      void onBeganColorPicking();

      void onColorValueChanged();

      void onFinishedColorPicking();

      void onSettingsPressed();

      void onUndoPressed();
   }
}
