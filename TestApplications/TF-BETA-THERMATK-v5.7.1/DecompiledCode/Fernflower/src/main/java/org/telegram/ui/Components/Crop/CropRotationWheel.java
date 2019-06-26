package org.telegram.ui.Components.Crop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class CropRotationWheel extends FrameLayout {
   private static final int DELTA_ANGLE = 5;
   private static final int MAX_ANGLE = 45;
   private ImageView aspectRatioButton;
   private Paint bluePaint;
   private TextView degreesLabel;
   private float prevX;
   protected float rotation;
   private CropRotationWheel.RotationWheelListener rotationListener;
   private RectF tempRect = new RectF(0.0F, 0.0F, 0.0F, 0.0F);
   private Paint whitePaint = new Paint();

   public CropRotationWheel(Context var1) {
      super(var1);
      this.whitePaint.setStyle(Style.FILL);
      this.whitePaint.setColor(-1);
      this.whitePaint.setAlpha(255);
      this.whitePaint.setAntiAlias(true);
      this.bluePaint = new Paint();
      this.bluePaint.setStyle(Style.FILL);
      this.bluePaint.setColor(-11420173);
      this.bluePaint.setAlpha(255);
      this.bluePaint.setAntiAlias(true);
      this.aspectRatioButton = new ImageView(var1);
      this.aspectRatioButton.setImageResource(2131165883);
      this.aspectRatioButton.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
      this.aspectRatioButton.setScaleType(ScaleType.CENTER);
      this.aspectRatioButton.setOnClickListener(new _$$Lambda$CropRotationWheel$o9DV_6J5Q1lFFifNNTKEl8cNF9w(this));
      this.aspectRatioButton.setContentDescription(LocaleController.getString("AccDescrAspectRatio", 2131558412));
      this.addView(this.aspectRatioButton, LayoutHelper.createFrame(70, 64, 19));
      ImageView var2 = new ImageView(var1);
      var2.setImageResource(2131165885);
      var2.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
      var2.setScaleType(ScaleType.CENTER);
      var2.setOnClickListener(new _$$Lambda$CropRotationWheel$Is9w1zkokBjEYWq9bkepljuCiac(this));
      var2.setContentDescription(LocaleController.getString("AccDescrRotate", 2131558466));
      this.addView(var2, LayoutHelper.createFrame(70, 64, 21));
      this.degreesLabel = new TextView(var1);
      this.degreesLabel.setTextColor(-1);
      this.addView(this.degreesLabel, LayoutHelper.createFrame(-2, -2, 49));
      this.setWillNotDraw(false);
      this.setRotation(0.0F, false);
   }

   protected void drawLine(Canvas var1, int var2, float var3, int var4, int var5, boolean var6, Paint var7) {
      int var8 = (int)((float)var4 / 2.0F - (float)AndroidUtilities.dp(70.0F));
      float var9 = (float)(var2 * 5);
      double var10 = (double)var8;
      double var12 = Math.cos(Math.toRadians((double)(90.0F - (var9 + var3))));
      Double.isNaN(var10);
      var2 = (int)(var10 * var12);
      var4 = var4 / 2 + var2;
      var3 = (float)Math.abs(var2) / (float)var8;
      var2 = Math.min(255, Math.max(0, (int)((1.0F - var3 * var3) * 255.0F)));
      if (var6) {
         var7 = this.bluePaint;
      }

      var7.setAlpha(var2);
      byte var14;
      if (var6) {
         var14 = 4;
      } else {
         var14 = 2;
      }

      if (var6) {
         var3 = 16.0F;
      } else {
         var3 = 12.0F;
      }

      var8 = AndroidUtilities.dp(var3);
      var2 = var14 / 2;
      var1.drawRect((float)(var4 - var2), (float)((var5 - var8) / 2), (float)(var4 + var2), (float)((var5 + var8) / 2), var7);
   }

   // $FF: synthetic method
   public void lambda$new$0$CropRotationWheel(View var1) {
      CropRotationWheel.RotationWheelListener var2 = this.rotationListener;
      if (var2 != null) {
         var2.aspectRatioPressed();
      }

   }

   // $FF: synthetic method
   public void lambda$new$1$CropRotationWheel(View var1) {
      CropRotationWheel.RotationWheelListener var2 = this.rotationListener;
      if (var2 != null) {
         var2.rotate90Pressed();
      }

   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      int var2 = this.getWidth();
      int var3 = this.getHeight();
      float var4 = -this.rotation * 2.0F;
      float var5 = var4 % 5.0F;
      int var6 = (int)Math.floor((double)(var4 / 5.0F));

      for(int var7 = 0; var7 < 16; ++var7) {
         Paint var9;
         label38: {
            Paint var8 = this.whitePaint;
            if (var7 >= var6) {
               var9 = var8;
               if (var7 != 0) {
                  break label38;
               }

               var9 = var8;
               if (var5 >= 0.0F) {
                  break label38;
               }
            }

            var9 = this.bluePaint;
         }

         boolean var10;
         if (var7 == var6 || var7 == 0 && var6 == -1) {
            var10 = true;
         } else {
            var10 = false;
         }

         this.drawLine(var1, var7, var5, var2, var3, var10, var9);
         if (var7 != 0) {
            int var11 = -var7;
            if (var11 > var6) {
               var9 = this.bluePaint;
            } else {
               var9 = this.whitePaint;
            }

            if (var11 == var6 + 1) {
               var10 = true;
            } else {
               var10 = false;
            }

            this.drawLine(var1, var11, var5, var2, var3, var10, var9);
         }
      }

      this.bluePaint.setAlpha(255);
      this.tempRect.left = (float)((var2 - AndroidUtilities.dp(2.5F)) / 2);
      this.tempRect.top = (float)((var3 - AndroidUtilities.dp(22.0F)) / 2);
      this.tempRect.right = (float)((var2 + AndroidUtilities.dp(2.5F)) / 2);
      this.tempRect.bottom = (float)((var3 + AndroidUtilities.dp(22.0F)) / 2);
      var1.drawRoundRect(this.tempRect, (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(2.0F), this.bluePaint);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(Math.min(MeasureSpec.getSize(var1), AndroidUtilities.dp(400.0F)), 1073741824), var2);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      float var3 = var1.getX();
      CropRotationWheel.RotationWheelListener var8;
      if (var2 == 0) {
         this.prevX = var3;
         var8 = this.rotationListener;
         if (var8 != null) {
            var8.onStart();
         }
      } else if (var2 != 1 && var2 != 3) {
         if (var2 == 2) {
            float var4 = this.prevX;
            float var5 = this.rotation;
            double var6 = (double)((var4 - var3) / AndroidUtilities.density);
            Double.isNaN(var6);
            var5 = Math.max(-45.0F, Math.min(45.0F, var5 + (float)(var6 / 3.141592653589793D / 1.649999976158142D)));
            if ((double)Math.abs(var5 - this.rotation) > 0.001D) {
               var4 = var5;
               if ((double)Math.abs(var5) < 0.05D) {
                  var4 = 0.0F;
               }

               this.setRotation(var4, false);
               var8 = this.rotationListener;
               if (var8 != null) {
                  var8.onChange(this.rotation);
               }

               this.prevX = var3;
            }
         }
      } else {
         var8 = this.rotationListener;
         if (var8 != null) {
            var8.onEnd(this.rotation);
         }

         AndroidUtilities.makeAccessibilityAnnouncement(String.format("%.1f°", this.rotation));
      }

      return true;
   }

   public void reset() {
      this.setRotation(0.0F, false);
   }

   public void setAspectLock(boolean var1) {
      ImageView var2 = this.aspectRatioButton;
      PorterDuffColorFilter var3;
      if (var1) {
         var3 = new PorterDuffColorFilter(-11420173, Mode.MULTIPLY);
      } else {
         var3 = null;
      }

      var2.setColorFilter(var3);
   }

   public void setFreeform(boolean var1) {
      ImageView var2 = this.aspectRatioButton;
      byte var3;
      if (var1) {
         var3 = 0;
      } else {
         var3 = 8;
      }

      var2.setVisibility(var3);
   }

   public void setListener(CropRotationWheel.RotationWheelListener var1) {
      this.rotationListener = var1;
   }

   public void setRotation(float var1, boolean var2) {
      this.rotation = var1;
      float var3 = this.rotation;
      var1 = var3;
      if ((double)Math.abs(var3) < 0.099D) {
         var1 = Math.abs(var3);
      }

      this.degreesLabel.setText(String.format("%.1fº", var1));
      this.invalidate();
   }

   public interface RotationWheelListener {
      void aspectRatioPressed();

      void onChange(float var1);

      void onEnd(float var1);

      void onStart();

      void rotate90Pressed();
   }
}
