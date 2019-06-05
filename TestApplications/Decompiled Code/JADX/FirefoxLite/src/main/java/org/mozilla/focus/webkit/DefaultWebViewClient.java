package org.mozilla.focus.webkit;

import android.content.Context;
import android.net.Uri;
import android.webkit.WebView;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.urlutils.UrlUtils;

public class DefaultWebViewClient extends TrackingProtectionWebViewClient {
    public DefaultWebViewClient(Context context) {
        super(context);
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        webView.getSettings().setLoadsImagesAutomatically(true);
        if (shouldOverrideInternalPages(webView, str)) {
            return true;
        }
        if (!UrlUtils.isSupportedProtocol(Uri.parse(str).getScheme()) && IntentUtils.handleExternalUri(webView.getContext(), str)) {
            return true;
        }
        webView.getSettings().setLoadsImagesAutomatically(1 ^ Settings.getInstance(webView.getContext()).shouldBlockImages());
        return super.shouldOverrideUrlLoading(webView, str);
    }

    private boolean shouldOverrideInternalPages(WebView webView, String str) {
        if (!SupportUtils.isTemplateSupportPages(str)) {
            return false;
        }
        SupportUtils.loadSupportPages(webView, str);
        return true;
    }
}
