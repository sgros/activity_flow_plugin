package org.telegram.ui.Components;

import android.net.Uri;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;
import org.telegram.messenger.browser.Browser;

public class URLSpanNoUnderline extends URLSpan {
   public URLSpanNoUnderline(String var1) {
      super(var1);
   }

   public void onClick(View var1) {
      String var2 = this.getURL();
      if (var2.startsWith("@")) {
         StringBuilder var3 = new StringBuilder();
         var3.append("https://t.me/");
         var3.append(var2.substring(1));
         Uri var4 = Uri.parse(var3.toString());
         Browser.openUrl(var1.getContext(), var4);
      } else {
         Browser.openUrl(var1.getContext(), var2);
      }

   }

   public void updateDrawState(TextPaint var1) {
      super.updateDrawState(var1);
      var1.setUnderlineText(false);
   }
}
