package org.telegram.ui.Components;

import android.text.TextPaint;

public class URLSpanUserMentionPhotoViewer extends URLSpanUserMention {
   public URLSpanUserMentionPhotoViewer(String var1, boolean var2) {
      super(var1, 2);
   }

   public void updateDrawState(TextPaint var1) {
      super.updateDrawState(var1);
      var1.setColor(-1);
      var1.setUnderlineText(false);
   }
}
