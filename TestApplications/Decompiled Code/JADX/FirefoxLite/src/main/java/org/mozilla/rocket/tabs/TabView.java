package org.mozilla.rocket.tabs;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import org.mozilla.rocket.tabs.web.DownloadCallback;

public interface TabView {

    public interface FindListener {
        void onFindResultReceived(int i, int i2, boolean z);
    }

    public interface FullscreenCallback {
        void fullScreenExited();
    }

    public static class HitTarget {
        public final String imageURL;
        public final boolean isImage;
        public final boolean isLink;
        public final String linkURL;
        public final TabView source;

        public HitTarget(TabView tabView, boolean z, String str, boolean z2, String str2) {
            if (z && str == null) {
                throw new IllegalStateException("link hittarget must contain URL");
            } else if (z2 && str2 == null) {
                throw new IllegalStateException("image hittarget must contain URL");
            } else {
                this.source = tabView;
                this.isLink = z;
                this.linkURL = str;
                this.isImage = z2;
                this.imageURL = str2;
            }
        }
    }

    void bindOnNewWindowCreation(Message message);

    boolean canGoBack();

    boolean canGoForward();

    void destroy();

    String getTitle();

    String getUrl();

    View getView();

    void goBack();

    void goForward();

    void insertBrowsingHistory();

    void loadUrl(String str);

    void onPause();

    void onResume();

    void performExitFullScreen();

    void reload();

    void restoreViewState(Bundle bundle);

    void saveViewState(Bundle bundle);

    void setChromeClient(TabChromeClient tabChromeClient);

    void setContentBlockingEnabled(boolean z);

    void setDownloadCallback(DownloadCallback downloadCallback);

    void setFindListener(FindListener findListener);

    void setImageBlockingEnabled(boolean z);

    void setViewClient(TabViewClient tabViewClient);

    void stopLoading();
}
