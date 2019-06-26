package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class BetterRatingView extends View {
   private Bitmap filledStar = BitmapFactory.decodeResource(this.getResources(), 2131165466).extractAlpha();
   private Bitmap hollowStar = BitmapFactory.decodeResource(this.getResources(), 2131165465).extractAlpha();
   private BetterRatingView.OnRatingChangeListener listener;
   private int numStars = 5;
   private Paint paint = new Paint();
   private int selectedRating = 0;

   public BetterRatingView(Context var1) {
      super(var1);
   }

   public int getRating() {
      return this.selectedRating;
   }

   protected void onDraw(Canvas var1) {
      for(int var2 = 0; var2 < this.numStars; ++var2) {
         Paint var3 = this.paint;
         String var4;
         if (var2 < this.selectedRating) {
            var4 = "dialogTextBlue";
         } else {
            var4 = "dialogTextHint";
         }

         var3.setColor(Theme.getColor(var4));
         Bitmap var5;
         if (var2 < this.selectedRating) {
            var5 = this.filledStar;
         } else {
            var5 = this.hollowStar;
         }

         var1.drawBitmap(var5, (float)(AndroidUtilities.dp(48.0F) * var2), 0.0F, this.paint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(this.numStars * AndroidUtilities.dp(32.0F) + (this.numStars - 1) * AndroidUtilities.dp(16.0F), AndroidUtilities.dp(32.0F));
   }

   public boolean onTouchEvent(MotionEvent var1) {
      float var2 = (float)AndroidUtilities.dp(-8.0F);

      for(int var3 = 0; var3 < this.numStars; ++var3) {
         if (var1.getX() > var2 && var1.getX() < (float)AndroidUtilities.dp(48.0F) + var2) {
            int var4 = this.selectedRating;
            int var5 = var3 + 1;
            if (var4 != var5) {
               this.selectedRating = var5;
               BetterRatingView.OnRatingChangeListener var6 = this.listener;
               if (var6 != null) {
                  var6.onRatingChanged(this.selectedRating);
               }

               this.invalidate();
               break;
            }
         }

         var2 += (float)AndroidUtilities.dp(48.0F);
      }

      return true;
   }

   public void setOnRatingChangeListener(BetterRatingView.OnRatingChangeListener var1) {
      this.listener = var1;
   }

   public interface OnRatingChangeListener {
      void onRatingChanged(int var1);
   }
}
