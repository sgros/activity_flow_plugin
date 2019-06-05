package org.mozilla.rocket.content;

import android.arch.lifecycle.Observer;
import java.util.List;
import org.mozilla.lite.partner.NewsItem;

/* compiled from: NewsPresenter.kt */
final class NewsPresenter$setupNewsViewModel$1<T> implements Observer<List<? extends NewsItem>> {
    final /* synthetic */ NewsPresenter this$0;

    NewsPresenter$setupNewsViewModel$1(NewsPresenter newsPresenter) {
        this.this$0 = newsPresenter;
    }

    public final void onChanged(List<? extends NewsItem> list) {
        this.this$0.newsViewContract.updateNews(list);
        this.this$0.isLoading = false;
    }
}
