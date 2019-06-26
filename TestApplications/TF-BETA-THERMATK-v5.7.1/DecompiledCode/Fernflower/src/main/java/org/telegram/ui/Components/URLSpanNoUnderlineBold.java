package org.telegram.ui.Components;

import android.text.TextPaint;
import org.telegram.messenger.AndroidUtilities;

public class URLSpanNoUnderlineBold extends URLSpanNoUnderline {
   public URLSpanNoUnderlineBold(String var1) {
      super(var1);
   }

   public void updateDrawState(TextPaint var1) {
      super.updateDrawState(var1);
      var1.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var1.setUnderlineText(false);
   }
}
