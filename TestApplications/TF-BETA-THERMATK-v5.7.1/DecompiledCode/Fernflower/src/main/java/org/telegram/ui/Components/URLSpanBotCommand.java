package org.telegram.ui.Components;

import android.text.TextPaint;
import org.telegram.ui.ActionBar.Theme;

public class URLSpanBotCommand extends URLSpanNoUnderline {
   public static boolean enabled;
   public int currentType;

   public URLSpanBotCommand(String var1, int var2) {
      super(var1);
      this.currentType = var2;
   }

   public void updateDrawState(TextPaint var1) {
      super.updateDrawState(var1);
      int var2 = this.currentType;
      if (var2 == 2) {
         var1.setColor(-1);
      } else {
         String var3;
         if (var2 == 1) {
            if (enabled) {
               var3 = "chat_messageLinkOut";
            } else {
               var3 = "chat_messageTextOut";
            }

            var1.setColor(Theme.getColor(var3));
         } else {
            if (enabled) {
               var3 = "chat_messageLinkIn";
            } else {
               var3 = "chat_messageTextIn";
            }

            var1.setColor(Theme.getColor(var3));
         }
      }

      var1.setUnderlineText(false);
   }
}
