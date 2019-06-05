// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.fragment;

import android.text.TextUtils;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.focus.utils.SupportUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.os.Bundle;
import android.webkit.WebView;
import org.mozilla.focus.locale.LocaleAwareFragment;

public abstract class DefaultWebFragment extends LocaleAwareFragment
{
    private boolean isWebViewAvailable;
    private String pendingUrl;
    protected WebView webView;
    private Bundle webViewState;
    
    public DefaultWebFragment() {
        this.pendingUrl = null;
    }
    
    @Override
    public void applyLocale() {
        new WebView(this.getContext()).destroy();
    }
    
    public abstract WebChromeClient createWebChromeClient();
    
    public abstract WebViewClient createWebViewClient();
    
    public abstract String getInitialUrl();
    
    protected WebView getWebView() {
        WebView webView;
        if (this.isWebViewAvailable) {
            webView = this.webView;
        }
        else {
            webView = null;
        }
        return webView;
    }
    
    public abstract View inflateLayout(final LayoutInflater p0, final ViewGroup p1, final Bundle p2);
    
    public void loadUrl(String normalize) {
        final WebView webView = this.getWebView();
        if (webView != null) {
            if (SupportUtils.isUrl(normalize)) {
                this.pendingUrl = null;
                if (!AppConstants.isDevBuild()) {
                    normalize = SupportUtils.normalize(normalize);
                }
                webView.loadUrl(normalize);
            }
            else if (AppConstants.isDevBuild()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("trying to open a invalid url: ");
                sb.append(normalize);
                throw new RuntimeException(sb.toString());
            }
        }
        else {
            this.pendingUrl = normalize;
        }
    }
    
    @Override
    public final View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflateLayout = this.inflateLayout(layoutInflater, viewGroup, bundle);
        this.isWebViewAvailable = true;
        this.webView.setWebViewClient(this.createWebViewClient());
        this.webView.setWebChromeClient(this.createWebChromeClient());
        return inflateLayout;
    }
    
    @Override
    public void onDestroy() {
        if (this.webView != null) {
            this.webView.destroy();
            this.webView = null;
        }
        super.onDestroy();
    }
    
    @Override
    public void onDestroyView() {
        this.isWebViewAvailable = false;
        if (this.webView != null) {
            this.webView.setWebViewClient((WebViewClient)null);
            this.webView.setWebChromeClient((WebChromeClient)null);
            this.webViewState = new Bundle();
            this.webView.saveState(this.webViewState);
        }
        super.onDestroyView();
    }
    
    @Override
    public void onPause() {
        this.webView.onPause();
        super.onPause();
    }
    
    @Override
    public void onResume() {
        this.webView.onResume();
        super.onResume();
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        this.webView.saveState(bundle);
        if (bundle.containsKey("WEBVIEW_CHROMIUM_STATE") && bundle.getByteArray("WEBVIEW_CHROMIUM_STATE").length > 300000) {
            bundle.remove("WEBVIEW_CHROMIUM_STATE");
        }
        super.onSaveInstanceState(bundle);
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (bundle == null) {
            if (this.webViewState != null) {
                this.webView.restoreState(this.webViewState);
            }
            String s;
            if (this.webViewState == null) {
                s = this.getInitialUrl();
            }
            else {
                s = this.pendingUrl;
            }
            if (!TextUtils.isEmpty((CharSequence)s)) {
                this.loadUrl(s);
            }
        }
        else {
            this.webView.restoreState(bundle);
        }
    }
}
