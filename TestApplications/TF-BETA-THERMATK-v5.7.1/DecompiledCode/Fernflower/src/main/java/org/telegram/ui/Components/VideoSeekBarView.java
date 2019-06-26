package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;

public class VideoSeekBarView extends View {
   private VideoSeekBarView.SeekBarDelegate delegate;
   private Paint paint = new Paint();
   private Paint paint2 = new Paint(1);
   private boolean pressed = false;
   private float progress = 0.0F;
   private int thumbDX = 0;
   private int thumbHeight = AndroidUtilities.dp(12.0F);
   private int thumbWidth = AndroidUtilities.dp(12.0F);

   public VideoSeekBarView(Context var1) {
      super(var1);
      this.paint.setColor(-10724260);
      this.paint2.setColor(-1);
   }

   public float getProgress() {
      return this.progress;
   }

   protected void onDraw(Canvas var1) {
      int var2 = (this.getMeasuredHeight() - this.thumbHeight) / 2;
      int var3 = this.getMeasuredWidth();
      int var4 = this.thumbWidth;
      var3 = (int)((float)(var3 - var4) * this.progress);
      var1.drawRect((float)(var4 / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0F)), (float)(this.getMeasuredWidth() - this.thumbWidth / 2), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0F)), this.paint);
      var4 = this.thumbWidth;
      var1.drawCircle((float)(var3 + var4 / 2), (float)(var2 + this.thumbHeight / 2), (float)(var4 / 2), this.paint2);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (var1 == null) {
         return false;
      } else {
         float var2 = var1.getX();
         float var3 = var1.getY();
         float var4 = (float)((int)((float)(this.getMeasuredWidth() - this.thumbWidth) * this.progress));
         int var5 = var1.getAction();
         float var6 = 0.0F;
         if (var5 == 0) {
            var5 = this.getMeasuredHeight();
            int var7 = this.thumbWidth;
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
               var3 = (float)((int)(var2 - (float)this.thumbDX));
               if (var3 >= 0.0F) {
                  if (var3 > (float)(this.getMeasuredWidth() - this.thumbWidth)) {
                     var6 = (float)(this.getMeasuredWidth() - this.thumbWidth);
                  } else {
                     var6 = var3;
                  }
               }

               this.progress = var6 / (float)(this.getMeasuredWidth() - this.thumbWidth);
               this.invalidate();
               return true;
            }
         } else if (this.pressed) {
            if (var1.getAction() == 1) {
               VideoSeekBarView.SeekBarDelegate var8 = this.delegate;
               if (var8 != null) {
                  var8.onSeekBarDrag(var4 / (float)(this.getMeasuredWidth() - this.thumbWidth));
               }
            }

            this.pressed = false;
            this.invalidate();
            return true;
         }

         return false;
      }
   }

   public void setDelegate(VideoSeekBarView.SeekBarDelegate var1) {
      this.delegate = var1;
   }

   public void setProgress(float var1) {
      float var2;
      if (var1 < 0.0F) {
         var2 = 0.0F;
      } else {
         var2 = var1;
         if (var1 > 1.0F) {
            var2 = 1.0F;
         }
      }

      this.progress = var2;
      this.invalidate();
   }

   public interface SeekBarDelegate {
      void onSeekBarDrag(float var1);
   }
}
