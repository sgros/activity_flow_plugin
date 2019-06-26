// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.Theme;
import android.text.TextPaint;
import android.view.View;

public class URLSpanUserMention extends URLSpanNoUnderline
{
    private int currentType;
    
    public URLSpanUserMention(final String s, final int currentType) {
        super(s);
        this.currentType = currentType;
    }
    
    @Override
    public void onClick(final View view) {
        super.onClick(view);
    }
    
    @Override
    public void updateDrawState(final TextPaint textPaint) {
        super.updateDrawState(textPaint);
        final int currentType = this.currentType;
        if (currentType == 2) {
            textPaint.setColor(-1);
        }
        else if (currentType == 1) {
            textPaint.setColor(Theme.getColor("chat_messageLinkOut"));
        }
        else {
            textPaint.setColor(Theme.getColor("chat_messageLinkIn"));
        }
        textPaint.setUnderlineText(false);
    }
}
