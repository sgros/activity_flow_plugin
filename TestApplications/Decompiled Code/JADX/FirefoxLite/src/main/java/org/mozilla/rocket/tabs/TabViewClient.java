package org.mozilla.rocket.tabs;

public class TabViewClient {

    public interface HttpAuthCallback {
        void cancel();

        void proceed(String str, String str2);
    }

    public boolean handleExternalUrl(String str) {
        return false;
    }

    public void onHttpAuthRequest(HttpAuthCallback httpAuthCallback, String str, String str2) {
    }

    public void onPageFinished(boolean z) {
    }

    public void onPageStarted(String str) {
    }

    public void onURLChanged(String str) {
    }

    public void updateFailingUrl(String str, boolean z) {
    }
}
