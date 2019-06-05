// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit;

import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.urlutils.UrlUtils;
import android.net.Uri;
import org.mozilla.focus.utils.SupportUtils;
import android.webkit.WebView;
import android.content.Context;

public class DefaultWebViewClient extends TrackingProtectionWebViewClient
{
    public DefaultWebViewClient(final Context context) {
        super(context);
    }
    
    private boolean shouldOverrideInternalPages(final WebView webView, final String s) {
        if (SupportUtils.isTemplateSupportPages(s)) {
            SupportUtils.loadSupportPages(webView, s);
            return true;
        }
        return false;
    }
    
    public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
        webView.getSettings().setLoadsImagesAutomatically(true);
        if (this.shouldOverrideInternalPages(webView, s)) {
            return true;
        }
        if (!UrlUtils.isSupportedProtocol(Uri.parse(s).getScheme()) && IntentUtils.handleExternalUri(webView.getContext(), s)) {
            return true;
        }
        webView.getSettings().setLoadsImagesAutomatically(true ^ Settings.getInstance(webView.getContext()).shouldBlockImages());
        return super.shouldOverrideUrlLoading(webView, s);
    }
}
