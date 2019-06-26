// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.Theme;
import android.text.TextPaint;

public class URLSpanBotCommand extends URLSpanNoUnderline
{
    public static boolean enabled = true;
    public int currentType;
    
    public URLSpanBotCommand(final String s, final int currentType) {
        super(s);
        this.currentType = currentType;
    }
    
    @Override
    public void updateDrawState(final TextPaint textPaint) {
        super.updateDrawState(textPaint);
        final int currentType = this.currentType;
        if (currentType == 2) {
            textPaint.setColor(-1);
        }
        else if (currentType == 1) {
            String s;
            if (URLSpanBotCommand.enabled) {
                s = "chat_messageLinkOut";
            }
            else {
                s = "chat_messageTextOut";
            }
            textPaint.setColor(Theme.getColor(s));
        }
        else {
            String s2;
            if (URLSpanBotCommand.enabled) {
                s2 = "chat_messageLinkIn";
            }
            else {
                s2 = "chat_messageTextIn";
            }
            textPaint.setColor(Theme.getColor(s2));
        }
        textPaint.setUnderlineText(false);
    }
}
