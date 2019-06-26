package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class HintEditText extends EditTextBoldCursor {
   private String hintText;
   private float numberSize;
   private Paint paint = new Paint();
   private android.graphics.Rect rect = new android.graphics.Rect();
   private float spaceSize;
   private float textOffset;

   public HintEditText(Context var1) {
      super(var1);
      this.paint.setColor(Theme.getColor("windowBackgroundWhiteHintText"));
   }

   public String getHintText() {
      return this.hintText;
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.hintText != null && this.length() < this.hintText.length()) {
         int var2 = this.getMeasuredHeight() / 2;
         float var3 = this.textOffset;

         for(int var4 = this.length(); var4 < this.hintText.length(); ++var4) {
            float var5;
            if (this.hintText.charAt(var4) == ' ') {
               var5 = this.spaceSize;
            } else {
               this.rect.set((int)var3 + AndroidUtilities.dp(1.0F), var2, (int)(this.numberSize + var3) - AndroidUtilities.dp(1.0F), AndroidUtilities.dp(2.0F) + var2);
               var1.drawRect(this.rect, this.paint);
               var5 = this.numberSize;
            }

            var3 += var5;
         }
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.onTextChange();
   }

   public void onTextChange() {
      float var1;
      if (this.length() > 0) {
         var1 = this.getPaint().measureText(this.getText(), 0, this.length());
      } else {
         var1 = 0.0F;
      }

      this.textOffset = var1;
      this.spaceSize = this.getPaint().measureText(" ");
      this.numberSize = this.getPaint().measureText("1");
      this.invalidate();
   }

   public void setHintText(String var1) {
      this.hintText = var1;
      this.onTextChange();
      this.setText(this.getText());
   }
}
