package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import org.telegram.messenger.AndroidUtilities;

public class SeekBar {
   private static Paint paint;
   private static int thumbWidth;
   private int backgroundColor;
   private int backgroundSelectedColor;
   private float bufferedProgress;
   private int cacheColor;
   private int circleColor;
   private SeekBar.SeekBarDelegate delegate;
   private int height;
   private int lineHeight = AndroidUtilities.dp(2.0F);
   private boolean pressed = false;
   private int progressColor;
   private RectF rect = new RectF();
   private boolean selected;
   private int thumbDX = 0;
   private int thumbX = 0;
   private int width;

   public SeekBar(Context var1) {
      if (paint == null) {
         paint = new Paint(1);
         thumbWidth = AndroidUtilities.dp(24.0F);
      }

   }

   public void draw(Canvas var1) {
      RectF var2 = this.rect;
      int var3 = thumbWidth;
      float var4 = (float)(var3 / 2);
      int var5 = this.height;
      int var6 = var5 / 2;
      int var7 = this.lineHeight;
      var2.set(var4, (float)(var6 - var7 / 2), (float)(this.width - var3 / 2), (float)(var5 / 2 + var7 / 2));
      Paint var10 = paint;
      if (this.selected) {
         var7 = this.backgroundSelectedColor;
      } else {
         var7 = this.backgroundColor;
      }

      var10.setColor(var7);
      var2 = this.rect;
      var7 = thumbWidth;
      var1.drawRoundRect(var2, (float)(var7 / 2), (float)(var7 / 2), paint);
      if (this.bufferedProgress > 0.0F) {
         var10 = paint;
         if (this.selected) {
            var7 = this.backgroundSelectedColor;
         } else {
            var7 = this.cacheColor;
         }

         var10.setColor(var7);
         var2 = this.rect;
         var5 = thumbWidth;
         var4 = (float)(var5 / 2);
         var6 = this.height;
         var3 = var6 / 2;
         var7 = this.lineHeight;
         var2.set(var4, (float)(var3 - var7 / 2), (float)(var5 / 2) + this.bufferedProgress * (float)(this.width - var5), (float)(var6 / 2 + var7 / 2));
         var2 = this.rect;
         var7 = thumbWidth;
         var1.drawRoundRect(var2, (float)(var7 / 2), (float)(var7 / 2), paint);
      }

      var2 = this.rect;
      var6 = thumbWidth;
      var4 = (float)(var6 / 2);
      var3 = this.height;
      var7 = var3 / 2;
      var5 = this.lineHeight;
      var2.set(var4, (float)(var7 - var5 / 2), (float)(var6 / 2 + this.thumbX), (float)(var3 / 2 + var5 / 2));
      paint.setColor(this.progressColor);
      var2 = this.rect;
      var7 = thumbWidth;
      var1.drawRoundRect(var2, (float)(var7 / 2), (float)(var7 / 2), paint);
      paint.setColor(this.circleColor);
      float var8 = (float)(this.thumbX + thumbWidth / 2);
      float var9 = (float)(this.height / 2);
      if (this.pressed) {
         var4 = 8.0F;
      } else {
         var4 = 6.0F;
      }

      var1.drawCircle(var8, var9, (float)AndroidUtilities.dp(var4), paint);
   }

   public float getProgress() {
      return (float)this.thumbX / (float)(this.width - thumbWidth);
   }

   public boolean isDragging() {
      return this.pressed;
   }

   public boolean onTouch(int var1, float var2, float var3) {
      int var4;
      int var6;
      if (var1 == 0) {
         var1 = this.height;
         var4 = thumbWidth;
         int var5 = (var1 - var4) / 2;
         var6 = this.thumbX;
         if ((float)(var6 - var5) <= var2 && var2 <= (float)(var4 + var6 + var5) && var3 >= 0.0F && var3 <= (float)var1) {
            this.pressed = true;
            this.thumbDX = (int)(var2 - (float)var6);
            return true;
         }
      } else if (var1 != 1 && var1 != 3) {
         if (var1 == 2 && this.pressed) {
            this.thumbX = (int)(var2 - (float)this.thumbDX);
            var6 = this.thumbX;
            if (var6 < 0) {
               this.thumbX = 0;
            } else {
               var4 = this.width;
               var1 = thumbWidth;
               if (var6 > var4 - var1) {
                  this.thumbX = var4 - var1;
               }
            }

            return true;
         }
      } else if (this.pressed) {
         if (var1 == 1) {
            SeekBar.SeekBarDelegate var7 = this.delegate;
            if (var7 != null) {
               var7.onSeekBarDrag((float)this.thumbX / (float)(this.width - thumbWidth));
            }
         }

         this.pressed = false;
         return true;
      }

      return false;
   }

   public void setBufferedProgress(float var1) {
      this.bufferedProgress = var1;
   }

   public void setColors(int var1, int var2, int var3, int var4, int var5) {
      this.backgroundColor = var1;
      this.cacheColor = var2;
      this.circleColor = var4;
      this.progressColor = var3;
      this.backgroundSelectedColor = var5;
   }

   public void setDelegate(SeekBar.SeekBarDelegate var1) {
      this.delegate = var1;
   }

   public void setLineHeight(int var1) {
      this.lineHeight = var1;
   }

   public void setProgress(float var1) {
      this.thumbX = (int)Math.ceil((double)((float)(this.width - thumbWidth) * var1));
      int var2 = this.thumbX;
      if (var2 < 0) {
         this.thumbX = 0;
      } else {
         int var3 = this.width;
         int var4 = thumbWidth;
         if (var2 > var3 - var4) {
            this.thumbX = var3 - var4;
         }
      }

   }

   public void setSelected(boolean var1) {
      this.selected = var1;
   }

   public void setSize(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }

   public interface SeekBarDelegate {
      void onSeekBarDrag(float var1);
   }
}
