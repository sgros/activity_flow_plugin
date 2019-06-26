package org.telegram.p004ui.Components;

import android.text.TextPaint;

/* renamed from: org.telegram.ui.Components.URLSpanUserMentionPhotoViewer */
public class URLSpanUserMentionPhotoViewer extends URLSpanUserMention {
    public URLSpanUserMentionPhotoViewer(String str, boolean z) {
        super(str, 2);
    }

    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setColor(-1);
        textPaint.setUnderlineText(false);
    }
}
