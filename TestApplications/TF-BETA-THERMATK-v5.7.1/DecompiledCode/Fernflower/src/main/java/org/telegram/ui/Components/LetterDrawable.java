package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.ui.ActionBar.Theme;

public class LetterDrawable extends Drawable {
   private static TextPaint namePaint;
   public static Paint paint = new Paint();
   private RectF rect = new RectF();
   private StringBuilder stringBuilder = new StringBuilder(5);
   private float textHeight;
   private StaticLayout textLayout;
   private float textLeft;
   private float textWidth;

   public LetterDrawable() {
      if (namePaint == null) {
         namePaint = new TextPaint(1);
      }

      namePaint.setTextSize((float)AndroidUtilities.dp(28.0F));
      paint.setColor(Theme.getColor("sharedMedia_linkPlaceholder"));
      namePaint.setColor(Theme.getColor("sharedMedia_linkPlaceholderText"));
   }

   public void draw(Canvas var1) {
      android.graphics.Rect var2 = this.getBounds();
      if (var2 != null) {
         this.rect.set((float)var2.left, (float)var2.top, (float)var2.right, (float)var2.bottom);
         var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), paint);
         var1.save();
         if (this.textLayout != null) {
            int var3 = var2.width();
            float var4 = (float)var2.left;
            float var5 = (float)var3;
            var1.translate(var4 + (var5 - this.textWidth) / 2.0F - this.textLeft, (float)var2.top + (var5 - this.textHeight) / 2.0F);
            this.textLayout.draw(var1);
         }

         var1.restore();
      }
   }

   public int getIntrinsicHeight() {
      return 0;
   }

   public int getIntrinsicWidth() {
      return 0;
   }

   public int getOpacity() {
      return -2;
   }

   public void setAlpha(int var1) {
   }

   public void setBackgroundColor(int var1) {
      paint.setColor(var1);
   }

   public void setColor(int var1) {
      namePaint.setColor(var1);
   }

   public void setColorFilter(ColorFilter var1) {
   }

   public void setTitle(String var1) {
      this.stringBuilder.setLength(0);
      if (var1 != null && var1.length() > 0) {
         this.stringBuilder.append(var1.substring(0, 1));
      }

      if (this.stringBuilder.length() > 0) {
         var1 = this.stringBuilder.toString().toUpperCase();

         try {
            StaticLayout var2 = new StaticLayout(var1, namePaint, AndroidUtilities.dp(100.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            this.textLayout = var2;
            if (this.textLayout.getLineCount() > 0) {
               this.textLeft = this.textLayout.getLineLeft(0);
               this.textWidth = this.textLayout.getLineWidth(0);
               this.textHeight = (float)this.textLayout.getLineBottom(0);
            }
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }
      } else {
         this.textLayout = null;
      }

   }
}
