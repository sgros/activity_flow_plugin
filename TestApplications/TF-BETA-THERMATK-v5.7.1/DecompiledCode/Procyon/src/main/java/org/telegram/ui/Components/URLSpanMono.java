// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.Theme;
import android.graphics.Typeface;
import org.telegram.messenger.SharedConfig;
import android.text.TextPaint;
import org.telegram.messenger.AndroidUtilities;
import android.text.style.MetricAffectingSpan;

public class URLSpanMono extends MetricAffectingSpan
{
    private int currentEnd;
    private CharSequence currentMessage;
    private int currentStart;
    private byte currentType;
    
    public URLSpanMono(final CharSequence currentMessage, final int currentStart, final int currentEnd, final byte b) {
        this.currentMessage = currentMessage;
        this.currentStart = currentStart;
        this.currentEnd = currentEnd;
        this.currentType = b;
    }
    
    public void copyToClipboard() {
        AndroidUtilities.addToClipboard(this.currentMessage.subSequence(this.currentStart, this.currentEnd).toString());
    }
    
    public void updateDrawState(final TextPaint textPaint) {
        textPaint.setTextSize((float)AndroidUtilities.dp((float)(SharedConfig.fontSize - 1)));
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setUnderlineText(false);
        final byte currentType = this.currentType;
        if (currentType == 2) {
            textPaint.setColor(-1);
        }
        else if (currentType == 1) {
            textPaint.setColor(Theme.getColor("chat_messageTextOut"));
        }
        else {
            textPaint.setColor(Theme.getColor("chat_messageTextIn"));
        }
    }
    
    public void updateMeasureState(final TextPaint textPaint) {
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextSize((float)AndroidUtilities.dp((float)(SharedConfig.fontSize - 1)));
        textPaint.setFlags(textPaint.getFlags() | 0x80);
    }
}
