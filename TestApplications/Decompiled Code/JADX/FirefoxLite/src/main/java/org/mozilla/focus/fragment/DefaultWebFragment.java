package org.mozilla.focus.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.mozilla.focus.locale.LocaleAwareFragment;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.focus.utils.SupportUtils;

public abstract class DefaultWebFragment extends LocaleAwareFragment {
    private boolean isWebViewAvailable;
    private String pendingUrl = null;
    protected WebView webView;
    private Bundle webViewState;

    public abstract WebChromeClient createWebChromeClient();

    public abstract WebViewClient createWebViewClient();

    public abstract String getInitialUrl();

    public abstract View inflateLayout(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle);

    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflateLayout = inflateLayout(layoutInflater, viewGroup, bundle);
        this.isWebViewAvailable = true;
        this.webView.setWebViewClient(createWebViewClient());
        this.webView.setWebChromeClient(createWebChromeClient());
        return inflateLayout;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (bundle == null) {
            if (this.webViewState != null) {
                this.webView.restoreState(this.webViewState);
            }
            CharSequence initialUrl = this.webViewState == null ? getInitialUrl() : this.pendingUrl;
            if (!TextUtils.isEmpty(initialUrl)) {
                loadUrl(initialUrl);
                return;
            }
            return;
        }
        this.webView.restoreState(bundle);
    }

    public void applyLocale() {
        new WebView(getContext()).destroy();
    }

    public void onPause() {
        this.webView.onPause();
        super.onPause();
    }

    public void onResume() {
        this.webView.onResume();
        super.onResume();
    }

    public void onSaveInstanceState(Bundle bundle) {
        this.webView.saveState(bundle);
        if (bundle.containsKey("WEBVIEW_CHROMIUM_STATE") && bundle.getByteArray("WEBVIEW_CHROMIUM_STATE").length > 300000) {
            bundle.remove("WEBVIEW_CHROMIUM_STATE");
        }
        super.onSaveInstanceState(bundle);
    }

    public void onDestroy() {
        if (this.webView != null) {
            this.webView.destroy();
            this.webView = null;
        }
        super.onDestroy();
    }

    public void onDestroyView() {
        this.isWebViewAvailable = false;
        if (this.webView != null) {
            this.webView.setWebViewClient(null);
            this.webView.setWebChromeClient(null);
            this.webViewState = new Bundle();
            this.webView.saveState(this.webViewState);
        }
        super.onDestroyView();
    }

    public void loadUrl(String str) {
        WebView webView = getWebView();
        if (webView == null) {
            this.pendingUrl = str;
        } else if (SupportUtils.isUrl(str)) {
            this.pendingUrl = null;
            if (!AppConstants.isDevBuild()) {
                str = SupportUtils.normalize(str);
            }
            webView.loadUrl(str);
        } else if (AppConstants.isDevBuild()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("trying to open a invalid url: ");
            stringBuilder.append(str);
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public WebView getWebView() {
        return this.isWebViewAvailable ? this.webView : null;
    }
}
