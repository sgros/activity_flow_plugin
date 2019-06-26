package org.telegram.ui.Components;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.Theme;

public class URLSpanMono extends MetricAffectingSpan {
   private int currentEnd;
   private CharSequence currentMessage;
   private int currentStart;
   private byte currentType;

   public URLSpanMono(CharSequence var1, int var2, int var3, byte var4) {
      this.currentMessage = var1;
      this.currentStart = var2;
      this.currentEnd = var3;
      this.currentType = (byte)var4;
   }

   public void copyToClipboard() {
      AndroidUtilities.addToClipboard(this.currentMessage.subSequence(this.currentStart, this.currentEnd).toString());
   }

   public void updateDrawState(TextPaint var1) {
      var1.setTextSize((float)AndroidUtilities.dp((float)(SharedConfig.fontSize - 1)));
      var1.setTypeface(Typeface.MONOSPACE);
      var1.setUnderlineText(false);
      byte var2 = this.currentType;
      if (var2 == 2) {
         var1.setColor(-1);
      } else if (var2 == 1) {
         var1.setColor(Theme.getColor("chat_messageTextOut"));
      } else {
         var1.setColor(Theme.getColor("chat_messageTextIn"));
      }

   }

   public void updateMeasureState(TextPaint var1) {
      var1.setTypeface(Typeface.MONOSPACE);
      var1.setTextSize((float)AndroidUtilities.dp((float)(SharedConfig.fontSize - 1)));
      var1.setFlags(var1.getFlags() | 128);
   }
}
