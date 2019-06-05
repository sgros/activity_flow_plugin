// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.content;

import kotlin.jvm.internal.Intrinsics;
import java.util.List;
import android.arch.lifecycle.MutableLiveData;
import org.mozilla.lite.partner.NewsItem;
import org.mozilla.lite.partner.Repository;
import android.arch.lifecycle.ViewModel;

public final class NewsViewModel extends ViewModel implements OnDataChangedListener<NewsItem>
{
    private final MutableLiveData<List<NewsItem>> items;
    private Repository<? extends NewsItem> repository;
    
    public NewsViewModel() {
        this.items = new MutableLiveData<List<NewsItem>>();
    }
    
    public final MutableLiveData<List<NewsItem>> getItems() {
        return this.items;
    }
    
    public final void loadMore() {
        final Repository<? extends NewsItem> repository = this.repository;
        if (repository != null) {
            repository.loadMore();
        }
    }
    
    @Override
    public void onDataChanged(final List<? extends NewsItem> value) {
        this.items.setValue((List<NewsItem>)value);
    }
    
    public final void setRepository(final Repository<? extends NewsItem> repository) {
        if (Intrinsics.areEqual(this.repository, repository) ^ true) {
            this.items.setValue(null);
        }
        this.repository = repository;
    }
}
