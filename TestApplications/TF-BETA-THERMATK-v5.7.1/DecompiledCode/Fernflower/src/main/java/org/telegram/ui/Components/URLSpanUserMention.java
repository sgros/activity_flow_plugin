package org.telegram.ui.Components;

import android.text.TextPaint;
import android.view.View;
import org.telegram.ui.ActionBar.Theme;

public class URLSpanUserMention extends URLSpanNoUnderline {
   private int currentType;

   public URLSpanUserMention(String var1, int var2) {
      super(var1);
      this.currentType = var2;
   }

   public void onClick(View var1) {
      super.onClick(var1);
   }

   public void updateDrawState(TextPaint var1) {
      super.updateDrawState(var1);
      int var2 = this.currentType;
      if (var2 == 2) {
         var1.setColor(-1);
      } else if (var2 == 1) {
         var1.setColor(Theme.getColor("chat_messageLinkOut"));
      } else {
         var1.setColor(Theme.getColor("chat_messageLinkIn"));
      }

      var1.setUnderlineText(false);
   }
}
