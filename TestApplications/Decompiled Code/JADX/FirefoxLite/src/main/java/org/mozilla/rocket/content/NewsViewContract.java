package org.mozilla.rocket.content;

import android.arch.lifecycle.LifecycleOwner;
import java.util.List;
import org.mozilla.lite.partner.NewsItem;

/* compiled from: NewsPresenter.kt */
public interface NewsViewContract {
    LifecycleOwner getViewLifecycleOwner();

    void updateNews(List<? extends NewsItem> list);
}
