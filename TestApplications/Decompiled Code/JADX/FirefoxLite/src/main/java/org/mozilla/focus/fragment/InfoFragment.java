package org.mozilla.focus.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.focus.webkit.DefaultWebViewClient;
import org.mozilla.rocket.C0769R;

public class InfoFragment extends DefaultWebFragment {
    private ProgressBar progressView;

    /* renamed from: org.mozilla.focus.fragment.InfoFragment$2 */
    class C04712 extends WebChromeClient {
        public boolean onCreateWindow(WebView webView, boolean z, boolean z2, Message message) {
            return false;
        }

        C04712() {
        }

        public void onProgressChanged(WebView webView, int i) {
            InfoFragment.this.progressView.setProgress(i);
        }
    }

    public static InfoFragment create(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("url", str);
        InfoFragment infoFragment = new InfoFragment();
        infoFragment.setArguments(bundle);
        return infoFragment;
    }

    public View inflateLayout(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C0769R.layout.fragment_info, viewGroup, false);
        this.progressView = (ProgressBar) inflate.findViewById(C0427R.C0426id.progress);
        viewGroup = (ViewGroup) inflate.findViewById(C0427R.C0426id.webview_slot);
        this.webView = (WebView) WebViewProvider.createDefaultWebView(getContext(), null);
        viewGroup.addView(this.webView);
        String initialUrl = getInitialUrl();
        if (!(TextUtils.isEmpty(initialUrl) || initialUrl.startsWith("http://") || initialUrl.startsWith("https://"))) {
            this.webView.setVisibility(4);
        }
        return inflate;
    }

    public WebViewClient createWebViewClient() {
        return new DefaultWebViewClient(getContext().getApplicationContext()) {
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                InfoFragment.this.progressView.announceForAccessibility(InfoFragment.this.getString(C0769R.string.accessibility_announcement_loading));
                InfoFragment.this.progressView.setVisibility(0);
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                InfoFragment.this.progressView.announceForAccessibility(InfoFragment.this.getString(C0769R.string.accessibility_announcement_loading_finished));
                InfoFragment.this.progressView.setVisibility(4);
                if (InfoFragment.this.webView.getVisibility() != 0) {
                    InfoFragment.this.webView.setVisibility(0);
                }
            }
        };
    }

    public WebChromeClient createWebChromeClient() {
        return new C04712();
    }

    public String getInitialUrl() {
        return getArguments().getString("url");
    }

    public void goBack() {
        if (this.webView != null) {
            this.webView.goBack();
        }
    }

    public boolean canGoBack() {
        return this.webView != null && this.webView.canGoBack();
    }
}
