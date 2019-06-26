package org.telegram.p004ui.Components;

import android.text.TextPaint;
import org.telegram.messenger.AndroidUtilities;

/* renamed from: org.telegram.ui.Components.URLSpanNoUnderlineBold */
public class URLSpanNoUnderlineBold extends URLSpanNoUnderline {
    public URLSpanNoUnderlineBold(String str) {
        super(str);
    }

    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textPaint.setUnderlineText(false);
    }
}
