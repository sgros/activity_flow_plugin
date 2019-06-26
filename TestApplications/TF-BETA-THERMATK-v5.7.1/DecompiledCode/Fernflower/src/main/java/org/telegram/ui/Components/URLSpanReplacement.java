package org.telegram.ui.Components;

import android.net.Uri;
import android.text.style.URLSpan;
import android.view.View;
import org.telegram.messenger.browser.Browser;

public class URLSpanReplacement extends URLSpan {
   public URLSpanReplacement(String var1) {
      super(var1);
   }

   public void onClick(View var1) {
      Uri var2 = Uri.parse(this.getURL());
      Browser.openUrl(var1.getContext(), var2);
   }
}
