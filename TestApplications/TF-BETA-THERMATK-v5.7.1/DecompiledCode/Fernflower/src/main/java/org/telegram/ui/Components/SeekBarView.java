package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class SeekBarView extends FrameLayout {
   private float bufferedProgress;
   private SeekBarView.SeekBarViewDelegate delegate;
   private Paint innerPaint1;
   private Paint outerPaint1;
   private boolean pressed;
   private float progressToSet;
   private boolean reportChanges;
   private int thumbDX;
   private int thumbHeight;
   private int thumbWidth;
   private int thumbX;

   public SeekBarView(Context var1) {
      super(var1);
      this.setWillNotDraw(false);
      this.innerPaint1 = new Paint(1);
      this.innerPaint1.setColor(Theme.getColor("player_progressBackground"));
      this.outerPaint1 = new Paint(1);
      this.outerPaint1.setColor(Theme.getColor("player_progress"));
      this.thumbWidth = AndroidUtilities.dp(24.0F);
      this.thumbHeight = AndroidUtilities.dp(24.0F);
   }

   public boolean isDragging() {
      return this.pressed;
   }

   protected void onDraw(Canvas var1) {
      int var2 = (this.getMeasuredHeight() - this.thumbHeight) / 2;
      var1.drawRect((float)(this.thumbWidth / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0F)), (float)(this.getMeasuredWidth() - this.thumbWidth / 2), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0F)), this.innerPaint1);
      if (this.bufferedProgress > 0.0F) {
         var1.drawRect((float)(this.thumbWidth / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0F)), (float)(this.thumbWidth / 2) + this.bufferedProgress * (float)(this.getMeasuredWidth() - this.thumbWidth), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0F)), this.innerPaint1);
      }

      var1.drawRect((float)(this.thumbWidth / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0F)), (float)(this.thumbWidth / 2 + this.thumbX), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0F)), this.outerPaint1);
      float var3 = (float)(this.thumbX + this.thumbWidth / 2);
      float var4 = (float)(var2 + this.thumbHeight / 2);
      float var5;
      if (this.pressed) {
         var5 = 8.0F;
      } else {
         var5 = 6.0F;
      }

      var1.drawCircle(var3, var4, (float)AndroidUtilities.dp(var5), this.outerPaint1);
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      return this.onTouch(var1);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      if (this.progressToSet >= 0.0F && this.getMeasuredWidth() > 0) {
         this.setProgress(this.progressToSet);
         this.progressToSet = -1.0F;
      }

   }

   boolean onTouch(MotionEvent var1) {
      int var2;
      if (var1.getAction() == 0) {
         this.getParent().requestDisallowInterceptTouchEvent(true);
         var2 = (this.getMeasuredHeight() - this.thumbWidth) / 2;
         if (var1.getY() >= 0.0F && var1.getY() <= (float)this.getMeasuredHeight()) {
            if ((float)(this.thumbX - var2) > var1.getX() || var1.getX() > (float)(this.thumbX + this.thumbWidth + var2)) {
               this.thumbX = (int)var1.getX() - this.thumbWidth / 2;
               var2 = this.thumbX;
               if (var2 < 0) {
                  this.thumbX = 0;
               } else if (var2 > this.getMeasuredWidth() - this.thumbWidth) {
                  this.thumbX = this.getMeasuredWidth() - this.thumbWidth;
               }
            }

            this.thumbDX = (int)(var1.getX() - (float)this.thumbX);
            this.pressed = true;
            this.invalidate();
            return true;
         }
      } else if (var1.getAction() != 1 && var1.getAction() != 3) {
         if (var1.getAction() == 2 && this.pressed) {
            this.thumbX = (int)(var1.getX() - (float)this.thumbDX);
            var2 = this.thumbX;
            if (var2 < 0) {
               this.thumbX = 0;
            } else if (var2 > this.getMeasuredWidth() - this.thumbWidth) {
               this.thumbX = this.getMeasuredWidth() - this.thumbWidth;
            }

            if (this.reportChanges) {
               this.delegate.onSeekBarDrag((float)this.thumbX / (float)(this.getMeasuredWidth() - this.thumbWidth));
            }

            this.invalidate();
            return true;
         }
      } else if (this.pressed) {
         if (var1.getAction() == 1) {
            this.delegate.onSeekBarDrag((float)this.thumbX / (float)(this.getMeasuredWidth() - this.thumbWidth));
         }

         this.pressed = false;
         this.invalidate();
         return true;
      }

      return false;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      return this.onTouch(var1);
   }

   public void setBufferedProgress(float var1) {
      this.bufferedProgress = var1;
   }

   public void setColors(int var1, int var2) {
      this.innerPaint1.setColor(var1);
      this.outerPaint1.setColor(var2);
   }

   public void setDelegate(SeekBarView.SeekBarViewDelegate var1) {
      this.delegate = var1;
   }

   public void setInnerColor(int var1) {
      this.innerPaint1.setColor(var1);
   }

   public void setOuterColor(int var1) {
      this.outerPaint1.setColor(var1);
   }

   public void setProgress(float var1) {
      if (this.getMeasuredWidth() == 0) {
         this.progressToSet = var1;
      } else {
         this.progressToSet = -1.0F;
         int var2 = (int)Math.ceil((double)((float)(this.getMeasuredWidth() - this.thumbWidth) * var1));
         if (this.thumbX != var2) {
            this.thumbX = var2;
            var2 = this.thumbX;
            if (var2 < 0) {
               this.thumbX = 0;
            } else if (var2 > this.getMeasuredWidth() - this.thumbWidth) {
               this.thumbX = this.getMeasuredWidth() - this.thumbWidth;
            }

            this.invalidate();
         }

      }
   }

   public void setReportChanges(boolean var1) {
      this.reportChanges = var1;
   }

   public interface SeekBarViewDelegate {
      void onSeekBarDrag(float var1);
   }
}
