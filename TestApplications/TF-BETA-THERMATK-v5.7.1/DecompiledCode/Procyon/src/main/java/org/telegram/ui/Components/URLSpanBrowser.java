// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.browser.Browser;
import android.net.Uri;
import android.view.View;
import android.text.style.URLSpan;

public class URLSpanBrowser extends URLSpan
{
    public URLSpanBrowser(final String s) {
        super(s);
    }
    
    public void onClick(final View view) {
        Browser.openUrl(view.getContext(), Uri.parse(this.getURL()));
    }
}
