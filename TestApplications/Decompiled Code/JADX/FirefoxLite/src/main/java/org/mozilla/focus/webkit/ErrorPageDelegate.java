package org.mozilla.focus.webkit;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

public class ErrorPageDelegate {
    private final ErrorViewFactory<HtmlErrorViewHolder> factory;

    public static abstract class ErrorViewFactory<T extends ErrorViewHolder> {
        private T errorViewHolder;

        public abstract void onBindErrorView(T t, int i, String str, String str2);

        public abstract void onBindSslErrorView(T t, SslErrorHandler sslErrorHandler, SslError sslError);

        public abstract T onCreateErrorView();

        public abstract void onErrorViewCreated(T t);

        public abstract void onErrorViewDestroyed(T t);

        private void createErrorView() {
            if (this.errorViewHolder != null) {
                destroyErrorView();
            }
            ErrorViewHolder onCreateErrorView = onCreateErrorView();
            this.errorViewHolder = onCreateErrorView;
            onErrorViewCreated(onCreateErrorView);
        }

        private void destroyErrorView() {
            if (this.errorViewHolder != null) {
                onErrorViewDestroyed(this.errorViewHolder);
            }
            this.errorViewHolder = null;
        }

        private T getErrorViewHolder() {
            return this.errorViewHolder;
        }
    }

    public static class ErrorViewHolder {
        public View rootView;

        ErrorViewHolder(View view) {
            this.rootView = view;
        }
    }

    private static abstract class JsInterface {
        public abstract void onReloadOnJsThread();

        private JsInterface() {
        }

        /* synthetic */ JsInterface(C05491 c05491) {
            this();
        }

        @JavascriptInterface
        public void onRetryClicked() {
            onReloadOnJsThread();
        }
    }

    public static class HtmlErrorViewHolder extends ErrorViewHolder {
        WebView webView;

        HtmlErrorViewHolder(View view) {
            super(view);
        }
    }

    public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
    }

    ErrorPageDelegate(final WebView webView) {
        this.factory = new ErrorViewFactory<HtmlErrorViewHolder>() {

            /* renamed from: org.mozilla.focus.webkit.ErrorPageDelegate$1$1 */
            class C05501 extends JsInterface {

                /* renamed from: org.mozilla.focus.webkit.ErrorPageDelegate$1$1$1 */
                class C05481 implements Runnable {
                    C05481() {
                    }

                    public void run() {
                        webView.reload();
                    }
                }

                C05501() {
                    super();
                }

                /* Access modifiers changed, original: 0000 */
                public void onReloadOnJsThread() {
                    webView.post(new C05481());
                }
            }

            public HtmlErrorViewHolder onCreateErrorView() {
                WebView webView = new WebView(webView.getContext());
                HtmlErrorViewHolder htmlErrorViewHolder = new HtmlErrorViewHolder(webView);
                htmlErrorViewHolder.webView = webView;
                return htmlErrorViewHolder;
            }

            public void onBindErrorView(HtmlErrorViewHolder htmlErrorViewHolder, int i, String str, String str2) {
                ErrorPage.loadErrorPage(htmlErrorViewHolder.webView, str2, i);
                bindRetryButton(htmlErrorViewHolder);
            }

            public void onBindSslErrorView(HtmlErrorViewHolder htmlErrorViewHolder, SslErrorHandler sslErrorHandler, SslError sslError) {
                if (webView.copyBackForwardList().getCurrentItem() != null) {
                    webView.removeView(htmlErrorViewHolder.rootView);
                    ErrorPage.loadErrorPage(webView, sslError.getUrl(), sslError.getPrimaryError());
                }
            }

            public void onErrorViewCreated(HtmlErrorViewHolder htmlErrorViewHolder) {
                webView.addView(htmlErrorViewHolder.rootView, new LayoutParams(-1, -1));
            }

            public void onErrorViewDestroyed(HtmlErrorViewHolder htmlErrorViewHolder) {
                if (htmlErrorViewHolder.webView != null) {
                    webView.removeView(htmlErrorViewHolder.rootView);
                    htmlErrorViewHolder.webView.removeJavascriptInterface("jsInterface");
                    htmlErrorViewHolder.webView.destroy();
                }
            }

            @SuppressLint({"SetJavaScriptEnabled"})
            private void bindRetryButton(HtmlErrorViewHolder htmlErrorViewHolder) {
                htmlErrorViewHolder.webView.getSettings().setJavaScriptEnabled(true);
                htmlErrorViewHolder.webView.addJavascriptInterface(new C05501(), "jsInterface");
            }
        };
    }

    public void onPageStarted() {
        dismissErrorView();
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        createErrorView();
        this.factory.onBindErrorView(this.factory.getErrorViewHolder(), i, str, str2);
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        createErrorView();
        this.factory.onBindSslErrorView(this.factory.getErrorViewHolder(), sslErrorHandler, sslError);
    }

    private void createErrorView() {
        this.factory.createErrorView();
    }

    private void dismissErrorView() {
        this.factory.destroyErrorView();
    }

    public void onWebViewScrolled(int i, int i2) {
        ErrorViewHolder access$100 = this.factory.getErrorViewHolder();
        if (access$100 != null) {
            access$100.rootView.setTranslationX((float) i);
            access$100.rootView.setTranslationY((float) i2);
        }
    }
}
