// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.tabs;

import org.mozilla.rocket.tabs.web.DownloadCallback;
import android.os.Bundle;
import android.view.View;
import android.os.Message;

public interface TabView
{
    void bindOnNewWindowCreation(final Message p0);
    
    boolean canGoBack();
    
    boolean canGoForward();
    
    void destroy();
    
    String getTitle();
    
    String getUrl();
    
    View getView();
    
    void goBack();
    
    void goForward();
    
    void insertBrowsingHistory();
    
    void loadUrl(final String p0);
    
    void onPause();
    
    void onResume();
    
    void performExitFullScreen();
    
    void reload();
    
    void restoreViewState(final Bundle p0);
    
    void saveViewState(final Bundle p0);
    
    void setChromeClient(final TabChromeClient p0);
    
    void setContentBlockingEnabled(final boolean p0);
    
    void setDownloadCallback(final DownloadCallback p0);
    
    void setFindListener(final FindListener p0);
    
    void setImageBlockingEnabled(final boolean p0);
    
    void setViewClient(final TabViewClient p0);
    
    void stopLoading();
    
    public interface FindListener
    {
        void onFindResultReceived(final int p0, final int p1, final boolean p2);
    }
    
    public interface FullscreenCallback
    {
        void fullScreenExited();
    }
    
    public static class HitTarget
    {
        public final String imageURL;
        public final boolean isImage;
        public final boolean isLink;
        public final String linkURL;
        public final TabView source;
        
        public HitTarget(final TabView source, final boolean isLink, final String linkURL, final boolean isImage, final String imageURL) {
            if (isLink && linkURL == null) {
                throw new IllegalStateException("link hittarget must contain URL");
            }
            if (isImage && imageURL == null) {
                throw new IllegalStateException("image hittarget must contain URL");
            }
            this.source = source;
            this.isLink = isLink;
            this.linkURL = linkURL;
            this.isImage = isImage;
            this.imageURL = imageURL;
        }
    }
}
