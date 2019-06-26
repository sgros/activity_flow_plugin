// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.AndroidUtilities;
import android.text.TextPaint;

public class URLSpanNoUnderlineBold extends URLSpanNoUnderline
{
    public URLSpanNoUnderlineBold(final String s) {
        super(s);
    }
    
    @Override
    public void updateDrawState(final TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textPaint.setUnderlineText(false);
    }
}
