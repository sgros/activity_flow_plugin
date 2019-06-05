package org.mozilla.rocket.content;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import java.util.Random;
import org.mozilla.focus.utils.Settings;
import org.mozilla.threadutils.ThreadUtils;

public class NewsSourceManager {
    private static NewsSourceManager instance = new NewsSourceManager();
    private boolean loadHasBeenTriggered;
    private String newsSource = null;
    private String newsSourceUrl = "";

    public static NewsSourceManager getInstance() {
        return instance;
    }

    private NewsSourceManager() {
    }

    public void init(Context context) {
        ThreadUtils.postToBackgroundThread(new C0591-$$Lambda$NewsSourceManager$0RgRaWEZjKRkWCKW_LiJ-UBTgPE(this, context));
    }

    public static /* synthetic */ void lambda$init$0(NewsSourceManager newsSourceManager, Context context) {
        newsSourceManager.loadHasBeenTriggered = true;
        Settings instance = Settings.getInstance(context);
        if (TextUtils.isEmpty(instance.getNewsSource())) {
            if (new Random().nextInt(1) % 2 == 0) {
                newsSourceManager.newsSource = "DainikBhaskar.com";
            } else {
                newsSourceManager.newsSource = "Newspoint";
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("NewsSourceManager sets default:");
            stringBuilder.append(newsSourceManager.newsSource);
            Log.d("NewsSource", stringBuilder.toString());
            instance.setNewsSource(newsSourceManager.newsSource);
            instance.setPriority("pref_int_news_priority", 0);
            return;
        }
        newsSourceManager.newsSource = instance.getNewsSource();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("NewsSourceManager already set:");
        stringBuilder2.append(newsSourceManager.newsSource);
        Log.d("NewsSource", stringBuilder2.toString());
    }

    public String getNewsSource() {
        awaitLoadingNewsSourceLocked();
        return this.newsSource;
    }

    public void setNewsSource(String str) {
        this.newsSource = str;
        NewsRepository.reset();
    }

    public String getNewsSourceUrl() {
        return this.newsSourceUrl;
    }

    public void setNewsSourceUrl(String str) {
        this.newsSourceUrl = str;
        NewsRepository.resetSubscriptionUrl(str);
    }

    private void awaitLoadingNewsSourceLocked() {
        if (this.loadHasBeenTriggered) {
            while (this.newsSource == null) {
                try {
                    wait();
                } catch (InterruptedException unused) {
                }
            }
            return;
        }
        throw new IllegalStateException("Attempting to retrieve search engines without a corresponding init()");
    }
}
