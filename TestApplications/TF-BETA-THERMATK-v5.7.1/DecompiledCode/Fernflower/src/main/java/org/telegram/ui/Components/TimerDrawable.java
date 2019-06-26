package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.ui.ActionBar.Theme;

public class TimerDrawable extends Drawable {
   private Paint linePaint = new Paint(1);
   private Paint paint = new Paint(1);
   private int time = 0;
   private int timeHeight = 0;
   private StaticLayout timeLayout;
   private TextPaint timePaint = new TextPaint(1);
   private float timeWidth = 0.0F;

   public TimerDrawable(Context var1) {
      this.timePaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.timePaint.setTextSize((float)AndroidUtilities.dp(11.0F));
      this.linePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0F));
      this.linePaint.setStyle(Style.STROKE);
   }

   public void draw(Canvas var1) {
      int var2 = this.getIntrinsicWidth();
      int var3 = this.getIntrinsicHeight();
      if (this.time == 0) {
         this.paint.setColor(Theme.getColor("chat_secretTimerBackground"));
         this.linePaint.setColor(Theme.getColor("chat_secretTimerText"));
         var1.drawCircle(AndroidUtilities.dpf2(9.0F), AndroidUtilities.dpf2(9.0F), AndroidUtilities.dpf2(7.5F), this.paint);
         var1.drawCircle(AndroidUtilities.dpf2(9.0F), AndroidUtilities.dpf2(9.0F), AndroidUtilities.dpf2(8.0F), this.linePaint);
         this.paint.setColor(Theme.getColor("chat_secretTimerText"));
         var1.drawLine((float)AndroidUtilities.dp(9.0F), (float)AndroidUtilities.dp(9.0F), (float)AndroidUtilities.dp(13.0F), (float)AndroidUtilities.dp(9.0F), this.linePaint);
         var1.drawLine((float)AndroidUtilities.dp(9.0F), (float)AndroidUtilities.dp(5.0F), (float)AndroidUtilities.dp(9.0F), (float)AndroidUtilities.dp(9.5F), this.linePaint);
         var1.drawRect(AndroidUtilities.dpf2(7.0F), AndroidUtilities.dpf2(0.0F), AndroidUtilities.dpf2(11.0F), AndroidUtilities.dpf2(1.5F), this.paint);
      } else {
         this.paint.setColor(Theme.getColor("chat_secretTimerBackground"));
         this.timePaint.setColor(Theme.getColor("chat_secretTimerText"));
         var1.drawCircle((float)AndroidUtilities.dp(9.5F), (float)AndroidUtilities.dp(9.5F), (float)AndroidUtilities.dp(9.5F), this.paint);
      }

      if (this.time != 0 && this.timeLayout != null) {
         byte var4 = 0;
         if (AndroidUtilities.density == 3.0F) {
            var4 = -1;
         }

         double var5 = (double)(var2 / 2);
         double var7 = Math.ceil((double)(this.timeWidth / 2.0F));
         Double.isNaN(var5);
         var1.translate((float)((int)(var5 - var7) + var4), (float)((var3 - this.timeHeight) / 2));
         this.timeLayout.draw(var1);
      }

   }

   public int getIntrinsicHeight() {
      return AndroidUtilities.dp(19.0F);
   }

   public int getIntrinsicWidth() {
      return AndroidUtilities.dp(19.0F);
   }

   public int getOpacity() {
      return 0;
   }

   public void setAlpha(int var1) {
   }

   public void setColorFilter(ColorFilter var1) {
   }

   public void setTime(int var1) {
      this.time = var1;
      int var2 = this.time;
      StringBuilder var3;
      String var4;
      String var6;
      if (var2 >= 1 && var2 < 60) {
         var3 = new StringBuilder();
         var3.append("");
         var3.append(var1);
         var4 = var3.toString();
         var6 = var4;
         if (var4.length() < 2) {
            var3 = new StringBuilder();
            var3.append(var4);
            var3.append("s");
            var6 = var3.toString();
         }
      } else {
         var2 = this.time;
         if (var2 >= 60 && var2 < 3600) {
            var3 = new StringBuilder();
            var3.append("");
            var3.append(var1 / 60);
            var4 = var3.toString();
            var6 = var4;
            if (var4.length() < 2) {
               var3 = new StringBuilder();
               var3.append(var4);
               var3.append("m");
               var6 = var3.toString();
            }
         } else {
            var2 = this.time;
            if (var2 >= 3600 && var2 < 86400) {
               var3 = new StringBuilder();
               var3.append("");
               var3.append(var1 / 60 / 60);
               var4 = var3.toString();
               var6 = var4;
               if (var4.length() < 2) {
                  var3 = new StringBuilder();
                  var3.append(var4);
                  var3.append("h");
                  var6 = var3.toString();
               }
            } else {
               var2 = this.time;
               if (var2 >= 86400 && var2 < 604800) {
                  var3 = new StringBuilder();
                  var3.append("");
                  var3.append(var1 / 60 / 60 / 24);
                  var4 = var3.toString();
                  var6 = var4;
                  if (var4.length() < 2) {
                     var3 = new StringBuilder();
                     var3.append(var4);
                     var3.append("d");
                     var6 = var3.toString();
                  }
               } else {
                  var3 = new StringBuilder();
                  var3.append("");
                  var3.append(var1 / 60 / 60 / 24 / 7);
                  var4 = var3.toString();
                  if (var4.length() < 2) {
                     var3 = new StringBuilder();
                     var3.append(var4);
                     var3.append("w");
                     var6 = var3.toString();
                  } else {
                     var6 = var4;
                     if (var4.length() > 2) {
                        var6 = "c";
                     }
                  }
               }
            }
         }
      }

      this.timeWidth = this.timePaint.measureText(var6);

      try {
         StaticLayout var7 = new StaticLayout(var6, this.timePaint, (int)Math.ceil((double)this.timeWidth), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         this.timeLayout = var7;
         this.timeHeight = this.timeLayout.getHeight();
      } catch (Exception var5) {
         this.timeLayout = null;
         FileLog.e((Throwable)var5);
      }

      this.invalidateSelf();
   }
}
