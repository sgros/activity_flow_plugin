// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.fragment;

import android.text.TextUtils;
import android.util.AttributeSet;
import org.mozilla.focus.web.WebViewProvider;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.graphics.Bitmap;
import android.content.Context;
import org.mozilla.focus.webkit.DefaultWebViewClient;
import android.webkit.WebViewClient;
import android.os.Message;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.os.Bundle;
import android.widget.ProgressBar;

public class InfoFragment extends DefaultWebFragment
{
    private ProgressBar progressView;
    
    public static InfoFragment create(final String s) {
        final Bundle arguments = new Bundle();
        arguments.putString("url", s);
        final InfoFragment infoFragment = new InfoFragment();
        infoFragment.setArguments(arguments);
        return infoFragment;
    }
    
    public boolean canGoBack() {
        return this.webView != null && this.webView.canGoBack();
    }
    
    @Override
    public WebChromeClient createWebChromeClient() {
        return new WebChromeClient() {
            public boolean onCreateWindow(final WebView webView, final boolean b, final boolean b2, final Message message) {
                return false;
            }
            
            public void onProgressChanged(final WebView webView, final int progress) {
                InfoFragment.this.progressView.setProgress(progress);
            }
        };
    }
    
    @Override
    public WebViewClient createWebViewClient() {
        return new DefaultWebViewClient(this.getContext().getApplicationContext()) {
            public void onPageFinished(final WebView webView, final String s) {
                super.onPageFinished(webView, s);
                InfoFragment.this.progressView.announceForAccessibility((CharSequence)InfoFragment.this.getString(2131755055));
                InfoFragment.this.progressView.setVisibility(4);
                if (InfoFragment.this.webView.getVisibility() != 0) {
                    InfoFragment.this.webView.setVisibility(0);
                }
            }
            
            @Override
            public void onPageStarted(final WebView webView, final String s, final Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                InfoFragment.this.progressView.announceForAccessibility((CharSequence)InfoFragment.this.getString(2131755054));
                InfoFragment.this.progressView.setVisibility(0);
            }
        };
    }
    
    @Override
    public String getInitialUrl() {
        return this.getArguments().getString("url");
    }
    
    public void goBack() {
        if (this.webView != null) {
            this.webView.goBack();
        }
    }
    
    @Override
    public View inflateLayout(final LayoutInflater layoutInflater, ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2131492967, viewGroup, false);
        this.progressView = (ProgressBar)inflate.findViewById(2131296576);
        viewGroup = (ViewGroup)inflate.findViewById(2131296723);
        viewGroup.addView((View)(this.webView = (WebView)WebViewProvider.createDefaultWebView(this.getContext(), null)));
        final String initialUrl = this.getInitialUrl();
        if (!TextUtils.isEmpty((CharSequence)initialUrl) && !initialUrl.startsWith("http://") && !initialUrl.startsWith("https://")) {
            this.webView.setVisibility(4);
        }
        return inflate;
    }
}
