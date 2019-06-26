// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.text.TextPaint;

public class URLSpanUserMentionPhotoViewer extends URLSpanUserMention
{
    public URLSpanUserMentionPhotoViewer(final String s, final boolean b) {
        super(s, 2);
    }
    
    @Override
    public void updateDrawState(final TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setColor(-1);
        textPaint.setUnderlineText(false);
    }
}
