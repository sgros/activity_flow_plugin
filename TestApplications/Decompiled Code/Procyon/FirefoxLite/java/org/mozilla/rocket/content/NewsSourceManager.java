// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.content;

import org.mozilla.threadutils.ThreadUtils;
import android.util.Log;
import java.util.Random;
import android.text.TextUtils;
import org.mozilla.focus.utils.Settings;
import android.content.Context;

public class NewsSourceManager
{
    private static NewsSourceManager instance;
    private boolean loadHasBeenTriggered;
    private String newsSource;
    private String newsSourceUrl;
    
    static {
        NewsSourceManager.instance = new NewsSourceManager();
    }
    
    private NewsSourceManager() {
        this.newsSource = null;
        this.newsSourceUrl = "";
    }
    
    private void awaitLoadingNewsSourceLocked() {
        if (this.loadHasBeenTriggered) {
            while (this.newsSource == null) {
                try {
                    this.wait();
                }
                catch (InterruptedException ex) {}
            }
            return;
        }
        throw new IllegalStateException("Attempting to retrieve search engines without a corresponding init()");
    }
    
    public static NewsSourceManager getInstance() {
        return NewsSourceManager.instance;
    }
    
    public String getNewsSource() {
        this.awaitLoadingNewsSourceLocked();
        return this.newsSource;
    }
    
    public String getNewsSourceUrl() {
        return this.newsSourceUrl;
    }
    
    public void init(final Context context) {
        ThreadUtils.postToBackgroundThread(new _$$Lambda$NewsSourceManager$0RgRaWEZjKRkWCKW_LiJ_UBTgPE(this, context));
    }
    
    public void setNewsSource(final String newsSource) {
        this.newsSource = newsSource;
        NewsRepository.reset();
    }
    
    public void setNewsSourceUrl(final String newsSourceUrl) {
        NewsRepository.resetSubscriptionUrl(this.newsSourceUrl = newsSourceUrl);
    }
}
