// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.text.TextPaint;
import org.telegram.messenger.browser.Browser;
import android.net.Uri;
import android.view.View;
import android.text.style.URLSpan;

public class URLSpanNoUnderline extends URLSpan
{
    public URLSpanNoUnderline(final String s) {
        super(s);
    }
    
    public void onClick(final View view) {
        final String url = this.getURL();
        if (url.startsWith("@")) {
            final StringBuilder sb = new StringBuilder();
            sb.append("https://t.me/");
            sb.append(url.substring(1));
            Browser.openUrl(view.getContext(), Uri.parse(sb.toString()));
        }
        else {
            Browser.openUrl(view.getContext(), url);
        }
    }
    
    public void updateDrawState(final TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setUnderlineText(false);
    }
}
