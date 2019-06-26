package org.telegram.p004ui.Components;

import android.text.TextPaint;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Components.URLSpanBotCommand */
public class URLSpanBotCommand extends URLSpanNoUnderline {
    public static boolean enabled = true;
    public int currentType;

    public URLSpanBotCommand(String str, int i) {
        super(str);
        this.currentType = i;
    }

    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        int i = this.currentType;
        if (i == 2) {
            textPaint.setColor(-1);
        } else if (i == 1) {
            textPaint.setColor(Theme.getColor(enabled ? Theme.key_chat_messageLinkOut : Theme.key_chat_messageTextOut));
        } else {
            textPaint.setColor(Theme.getColor(enabled ? Theme.key_chat_messageLinkIn : Theme.key_chat_messageTextIn));
        }
        textPaint.setUnderlineText(false);
    }
}
