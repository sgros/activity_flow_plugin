package org.telegram.p004ui.Components;

import android.net.Uri;
import android.text.style.URLSpan;
import android.view.View;
import org.telegram.messenger.browser.Browser;

/* renamed from: org.telegram.ui.Components.URLSpanBrowser */
public class URLSpanBrowser extends URLSpan {
    public URLSpanBrowser(String str) {
        super(str);
    }

    public void onClick(View view) {
        Browser.openUrl(view.getContext(), Uri.parse(getURL()));
    }
}
