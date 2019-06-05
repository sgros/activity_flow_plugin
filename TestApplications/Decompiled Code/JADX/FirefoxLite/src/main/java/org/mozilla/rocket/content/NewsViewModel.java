package org.mozilla.rocket.content;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.lite.partner.NewsItem;
import org.mozilla.lite.partner.Repository;
import org.mozilla.lite.partner.Repository.OnDataChangedListener;

/* compiled from: NewsViewModel.kt */
public final class NewsViewModel extends ViewModel implements OnDataChangedListener<NewsItem> {
    private final MutableLiveData<List<NewsItem>> items = new MutableLiveData();
    private Repository<? extends NewsItem> repository;

    public final void setRepository(Repository<? extends NewsItem> repository) {
        if ((Intrinsics.areEqual(this.repository, repository) ^ 1) != 0) {
            this.items.setValue(null);
        }
        this.repository = repository;
    }

    public final MutableLiveData<List<NewsItem>> getItems() {
        return this.items;
    }

    public void onDataChanged(List<? extends NewsItem> list) {
        this.items.setValue(list);
    }

    public final void loadMore() {
        Repository repository = this.repository;
        if (repository != null) {
            repository.loadMore();
        }
    }
}
