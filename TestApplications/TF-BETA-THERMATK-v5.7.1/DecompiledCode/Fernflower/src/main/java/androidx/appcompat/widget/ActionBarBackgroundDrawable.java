package androidx.appcompat.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;

class ActionBarBackgroundDrawable extends Drawable {
   final ActionBarContainer mContainer;

   public ActionBarBackgroundDrawable(ActionBarContainer var1) {
      this.mContainer = var1;
   }

   public void draw(Canvas var1) {
      ActionBarContainer var2 = this.mContainer;
      Drawable var4;
      if (var2.mIsSplit) {
         var4 = var2.mSplitBackground;
         if (var4 != null) {
            var4.draw(var1);
         }
      } else {
         var4 = var2.mBackground;
         if (var4 != null) {
            var4.draw(var1);
         }

         ActionBarContainer var3 = this.mContainer;
         var4 = var3.mStackedBackground;
         if (var4 != null && var3.mIsStacked) {
            var4.draw(var1);
         }
      }

   }

   public int getOpacity() {
      return 0;
   }

   public void getOutline(Outline var1) {
      ActionBarContainer var2 = this.mContainer;
      Drawable var3;
      if (var2.mIsSplit) {
         var3 = var2.mSplitBackground;
         if (var3 != null) {
            var3.getOutline(var1);
         }
      } else {
         var3 = var2.mBackground;
         if (var3 != null) {
            var3.getOutline(var1);
         }
      }

   }

   public void setAlpha(int var1) {
   }

   public void setColorFilter(ColorFilter var1) {
   }
}
