package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;

public class PhotoEditorSeekBar extends View {
   private PhotoEditorSeekBar.PhotoEditorSeekBarDelegate delegate;
   private Paint innerPaint = new Paint();
   private int maxValue;
   private int minValue;
   private Paint outerPaint = new Paint(1);
   private boolean pressed = false;
   private float progress = 0.0F;
   private int thumbDX = 0;
   private int thumbSize = AndroidUtilities.dp(16.0F);

   public PhotoEditorSeekBar(Context var1) {
      super(var1);
      this.innerPaint.setColor(-11711155);
      this.outerPaint.setColor(-1);
   }

   public int getProgress() {
      int var1 = this.minValue;
      return (int)((float)var1 + this.progress * (float)(this.maxValue - var1));
   }

   protected void onDraw(Canvas var1) {
      int var2 = (this.getMeasuredHeight() - this.thumbSize) / 2;
      int var3 = this.getMeasuredWidth();
      int var4 = this.thumbSize;
      var3 = (int)((float)(var3 - var4) * this.progress);
      var1.drawRect((float)(var4 / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0F)), (float)(this.getMeasuredWidth() - this.thumbSize / 2), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0F)), this.innerPaint);
      if (this.minValue == 0) {
         var1.drawRect((float)(this.thumbSize / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0F)), (float)var3, (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0F)), this.outerPaint);
      } else if (this.progress > 0.5F) {
         var1.drawRect((float)(this.getMeasuredWidth() / 2 - AndroidUtilities.dp(1.0F)), (float)((this.getMeasuredHeight() - this.thumbSize) / 2), (float)(this.getMeasuredWidth() / 2), (float)((this.getMeasuredHeight() + this.thumbSize) / 2), this.outerPaint);
         var1.drawRect((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0F)), (float)var3, (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0F)), this.outerPaint);
      } else {
         var1.drawRect((float)(this.getMeasuredWidth() / 2), (float)((this.getMeasuredHeight() - this.thumbSize) / 2), (float)(this.getMeasuredWidth() / 2 + AndroidUtilities.dp(1.0F)), (float)((this.getMeasuredHeight() + this.thumbSize) / 2), this.outerPaint);
         var1.drawRect((float)var3, (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0F)), (float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0F)), this.outerPaint);
      }

      var4 = this.thumbSize;
      var1.drawCircle((float)(var3 + var4 / 2), (float)(var2 + var4 / 2), (float)(var4 / 2), this.outerPaint);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (var1 == null) {
         return false;
      } else {
         float var2 = var1.getX();
         float var3 = var1.getY();
         float var4 = (float)((int)((float)(this.getMeasuredWidth() - this.thumbSize) * this.progress));
         int var5 = var1.getAction();
         float var6 = 0.0F;
         if (var5 == 0) {
            var5 = this.getMeasuredHeight();
            int var7 = this.thumbSize;
            var6 = (float)((var5 - var7) / 2);
            if (var4 - var6 <= var2 && var2 <= (float)var7 + var4 + var6 && var3 >= 0.0F && var3 <= (float)this.getMeasuredHeight()) {
               this.pressed = true;
               this.thumbDX = (int)(var2 - var4);
               this.getParent().requestDisallowInterceptTouchEvent(true);
               this.invalidate();
               return true;
            }
         } else if (var1.getAction() != 1 && var1.getAction() != 3) {
            if (var1.getAction() == 2 && this.pressed) {
               var2 = (float)((int)(var2 - (float)this.thumbDX));
               if (var2 >= 0.0F) {
                  if (var2 > (float)(this.getMeasuredWidth() - this.thumbSize)) {
                     var6 = (float)(this.getMeasuredWidth() - this.thumbSize);
                  } else {
                     var6 = var2;
                  }
               }

               this.progress = var6 / (float)(this.getMeasuredWidth() - this.thumbSize);
               PhotoEditorSeekBar.PhotoEditorSeekBarDelegate var8 = this.delegate;
               if (var8 != null) {
                  var8.onProgressChanged((Integer)this.getTag(), this.getProgress());
               }

               this.invalidate();
               return true;
            }
         } else if (this.pressed) {
            this.pressed = false;
            this.invalidate();
            return true;
         }

         return false;
      }
   }

   public void setDelegate(PhotoEditorSeekBar.PhotoEditorSeekBarDelegate var1) {
      this.delegate = var1;
   }

   public void setMinMax(int var1, int var2) {
      this.minValue = var1;
      this.maxValue = var2;
   }

   public void setProgress(int var1) {
      this.setProgress(var1, true);
   }

   public void setProgress(int var1, boolean var2) {
      int var3 = this.minValue;
      if (var1 < var3) {
         var1 = var3;
      } else {
         var3 = this.maxValue;
         if (var1 > var3) {
            var1 = var3;
         }
      }

      var3 = this.minValue;
      this.progress = (float)(var1 - var3) / (float)(this.maxValue - var3);
      this.invalidate();
      if (var2) {
         PhotoEditorSeekBar.PhotoEditorSeekBarDelegate var4 = this.delegate;
         if (var4 != null) {
            var4.onProgressChanged((Integer)this.getTag(), this.getProgress());
         }
      }

   }

   public interface PhotoEditorSeekBarDelegate {
      void onProgressChanged(int var1, int var2);
   }
}
