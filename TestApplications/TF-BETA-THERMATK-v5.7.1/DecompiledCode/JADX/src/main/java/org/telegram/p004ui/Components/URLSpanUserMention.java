package org.telegram.p004ui.Components;

import android.text.TextPaint;
import android.view.View;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Components.URLSpanUserMention */
public class URLSpanUserMention extends URLSpanNoUnderline {
    private int currentType;

    public URLSpanUserMention(String str, int i) {
        super(str);
        this.currentType = i;
    }

    public void onClick(View view) {
        super.onClick(view);
    }

    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        int i = this.currentType;
        if (i == 2) {
            textPaint.setColor(-1);
        } else if (i == 1) {
            textPaint.setColor(Theme.getColor(Theme.key_chat_messageLinkOut));
        } else {
            textPaint.setColor(Theme.getColor(Theme.key_chat_messageLinkIn));
        }
        textPaint.setUnderlineText(false);
    }
}