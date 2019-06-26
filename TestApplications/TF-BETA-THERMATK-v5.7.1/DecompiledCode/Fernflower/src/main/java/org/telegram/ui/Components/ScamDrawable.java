package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;

public class ScamDrawable extends Drawable {
   private Paint paint = new Paint(1);
   private RectF rect = new RectF();
   private String text;
   private TextPaint textPaint = new TextPaint(1);
   private int textWidth;

   public ScamDrawable(int var1) {
      this.textPaint.setTextSize((float)AndroidUtilities.dp((float)var1));
      this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.paint.setStyle(Style.STROKE);
      this.paint.setStrokeWidth((float)AndroidUtilities.dp(1.0F));
      this.text = LocaleController.getString("ScamMessage", 2131560635);
      this.textWidth = (int)Math.ceil((double)this.textPaint.measureText(this.text));
   }

   public void checkText() {
      String var1 = LocaleController.getString("ScamMessage", 2131560635);
      if (!var1.equals(this.text)) {
         this.text = var1;
         this.textWidth = (int)Math.ceil((double)this.textPaint.measureText(this.text));
      }

   }

   public void draw(Canvas var1) {
      this.rect.set(this.getBounds());
      var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(2.0F), this.paint);
      var1.drawText(this.text, this.rect.left + (float)AndroidUtilities.dp(5.0F), this.rect.top + (float)AndroidUtilities.dp(12.0F), this.textPaint);
   }

   public int getIntrinsicHeight() {
      return AndroidUtilities.dp(16.0F);
   }

   public int getIntrinsicWidth() {
      return this.textWidth + AndroidUtilities.dp(10.0F);
   }

   public int getOpacity() {
      return -2;
   }

   public void setAlpha(int var1) {
   }

   public void setColor(int var1) {
      this.textPaint.setColor(var1);
      this.paint.setColor(var1);
   }

   public void setColorFilter(ColorFilter var1) {
   }
}
