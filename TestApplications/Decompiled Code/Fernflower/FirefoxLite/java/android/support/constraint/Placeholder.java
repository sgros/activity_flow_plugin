package android.support.constraint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.view.View;

public class Placeholder extends View {
   private View mContent;
   private int mContentId;
   private int mEmptyVisibility;

   public View getContent() {
      return this.mContent;
   }

   public int getEmptyVisibility() {
      return this.mEmptyVisibility;
   }

   public void onDraw(Canvas var1) {
      if (this.isInEditMode()) {
         var1.drawRGB(223, 223, 223);
         Paint var2 = new Paint();
         var2.setARGB(255, 210, 210, 210);
         var2.setTextAlign(Align.CENTER);
         var2.setTypeface(Typeface.create(Typeface.DEFAULT, 0));
         Rect var3 = new Rect();
         var1.getClipBounds(var3);
         var2.setTextSize((float)var3.height());
         int var4 = var3.height();
         int var5 = var3.width();
         var2.setTextAlign(Align.LEFT);
         var2.getTextBounds("?", 0, "?".length(), var3);
         var1.drawText("?", (float)var5 / 2.0F - (float)var3.width() / 2.0F - (float)var3.left, (float)var4 / 2.0F + (float)var3.height() / 2.0F - (float)var3.bottom, var2);
      }

   }

   public void setContentId(int var1) {
      if (this.mContentId != var1) {
         if (this.mContent != null) {
            this.mContent.setVisibility(0);
            ((ConstraintLayout.LayoutParams)this.mContent.getLayoutParams()).isInPlaceholder = false;
            this.mContent = null;
         }

         this.mContentId = var1;
         if (var1 != -1) {
            View var2 = ((View)this.getParent()).findViewById(var1);
            if (var2 != null) {
               var2.setVisibility(8);
            }
         }

      }
   }

   public void setEmptyVisibility(int var1) {
      this.mEmptyVisibility = var1;
   }

   public void updatePostMeasure(ConstraintLayout var1) {
      if (this.mContent != null) {
         ConstraintLayout.LayoutParams var3 = (ConstraintLayout.LayoutParams)this.getLayoutParams();
         ConstraintLayout.LayoutParams var2 = (ConstraintLayout.LayoutParams)this.mContent.getLayoutParams();
         var2.widget.setVisibility(0);
         var3.widget.setWidth(var2.widget.getWidth());
         var3.widget.setHeight(var2.widget.getHeight());
         var2.widget.setVisibility(8);
      }
   }

   public void updatePreLayout(ConstraintLayout var1) {
      if (this.mContentId == -1 && !this.isInEditMode()) {
         this.setVisibility(this.mEmptyVisibility);
      }

      this.mContent = var1.findViewById(this.mContentId);
      if (this.mContent != null) {
         ((ConstraintLayout.LayoutParams)this.mContent.getLayoutParams()).isInPlaceholder = true;
         this.mContent.setVisibility(0);
         this.setVisibility(0);
      }

   }
}
