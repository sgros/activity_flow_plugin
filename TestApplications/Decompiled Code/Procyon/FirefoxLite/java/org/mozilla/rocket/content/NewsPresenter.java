// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.content;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;
import org.mozilla.threadutils.ThreadUtils;
import android.arch.lifecycle.MutableLiveData;
import org.mozilla.lite.partner.NewsItem;
import java.util.List;
import org.mozilla.lite.partner.Repository;
import org.mozilla.focus.utils.Settings;
import android.content.Context;
import kotlin.jvm.internal.Intrinsics;

public final class NewsPresenter implements NewsListListener
{
    public static final Companion Companion;
    private static final long LOADMORE_THRESHOLD = 3000L;
    private boolean isLoading;
    private final NewsViewContract newsViewContract;
    private NewsViewModel newsViewModel;
    
    static {
        Companion = new Companion(null);
    }
    
    public NewsPresenter(final NewsViewContract newsViewContract) {
        Intrinsics.checkParameterIsNotNull(newsViewContract, "newsViewContract");
        this.newsViewContract = newsViewContract;
    }
    
    private final void updateSourcePriority(final Context context) {
        Settings.getInstance(context).setPriority("pref_int_news_priority", 2);
    }
    
    public final void checkNewsRepositoryReset(final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        if (NewsRepository.Companion.isEmpty() && this.newsViewModel != null) {
            final Repository<? extends NewsItem> instance = NewsRepository.Companion.getInstance(context);
            instance.setOnDataChangedListener((Repository.OnDataChangedListener)this.newsViewModel);
            final NewsViewModel newsViewModel = this.newsViewModel;
            if (newsViewModel != null) {
                newsViewModel.setRepository(instance);
            }
            final NewsViewModel newsViewModel2 = this.newsViewModel;
            if (newsViewModel2 != null) {
                final MutableLiveData<List<NewsItem>> items = newsViewModel2.getItems();
                if (items != null) {
                    items.setValue(null);
                }
            }
            final NewsViewModel newsViewModel3 = this.newsViewModel;
            if (newsViewModel3 != null) {
                newsViewModel3.loadMore();
            }
        }
    }
    
    @Override
    public void loadMore() {
        if (!this.isLoading) {
            final NewsViewModel newsViewModel = this.newsViewModel;
            if (newsViewModel != null) {
                newsViewModel.loadMore();
            }
            this.isLoading = true;
            ThreadUtils.postToMainThreadDelayed((Runnable)new NewsPresenter$loadMore.NewsPresenter$loadMore$1(this), NewsPresenter.LOADMORE_THRESHOLD);
        }
    }
    
    @Override
    public void onShow(final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.updateSourcePriority(context);
        this.checkNewsRepositoryReset(context);
    }
    
    public final void setupNewsViewModel(final FragmentActivity fragmentActivity) {
        if (fragmentActivity == null) {
            return;
        }
        this.newsViewModel = ViewModelProviders.of(fragmentActivity).get(NewsViewModel.class);
        final Repository<? extends NewsItem> instance = NewsRepository.Companion.getInstance((Context)fragmentActivity);
        instance.setOnDataChangedListener((Repository.OnDataChangedListener)this.newsViewModel);
        final NewsViewModel newsViewModel = this.newsViewModel;
        if (newsViewModel != null) {
            newsViewModel.setRepository(instance);
        }
        final NewsViewModel newsViewModel2 = this.newsViewModel;
        if (newsViewModel2 != null) {
            final MutableLiveData<List<NewsItem>> items = newsViewModel2.getItems();
            if (items != null) {
                items.observe(this.newsViewContract.getViewLifecycleOwner(), (Observer<List<NewsItem>>)new NewsPresenter$setupNewsViewModel.NewsPresenter$setupNewsViewModel$1(this));
            }
        }
        final NewsViewModel newsViewModel3 = this.newsViewModel;
        if (newsViewModel3 != null) {
            newsViewModel3.loadMore();
        }
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
}
