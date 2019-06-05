package org.mozilla.rocket.content;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.p001v4.app.FragmentActivity;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.utils.Settings;
import org.mozilla.lite.partner.Repository;
import org.mozilla.rocket.content.ContentPortalView.NewsListListener;
import org.mozilla.threadutils.ThreadUtils;

/* compiled from: NewsPresenter.kt */
public final class NewsPresenter implements NewsListListener {
    public static final Companion Companion = new Companion();
    private static final long LOADMORE_THRESHOLD = LOADMORE_THRESHOLD;
    private boolean isLoading;
    private final NewsViewContract newsViewContract;
    private NewsViewModel newsViewModel;

    /* compiled from: NewsPresenter.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public NewsPresenter(NewsViewContract newsViewContract) {
        Intrinsics.checkParameterIsNotNull(newsViewContract, "newsViewContract");
        this.newsViewContract = newsViewContract;
    }

    public final void setupNewsViewModel(FragmentActivity fragmentActivity) {
        if (fragmentActivity != null) {
            this.newsViewModel = (NewsViewModel) ViewModelProviders.m2of(fragmentActivity).get(NewsViewModel.class);
            Repository instance = NewsRepository.Companion.getInstance(fragmentActivity);
            instance.setOnDataChangedListener(this.newsViewModel);
            NewsViewModel newsViewModel = this.newsViewModel;
            if (newsViewModel != null) {
                newsViewModel.setRepository(instance);
            }
            NewsViewModel newsViewModel2 = this.newsViewModel;
            if (newsViewModel2 != null) {
                MutableLiveData items = newsViewModel2.getItems();
                if (items != null) {
                    items.observe(this.newsViewContract.getViewLifecycleOwner(), new NewsPresenter$setupNewsViewModel$1(this));
                }
            }
            newsViewModel2 = this.newsViewModel;
            if (newsViewModel2 != null) {
                newsViewModel2.loadMore();
            }
        }
    }

    public void loadMore() {
        if (!this.isLoading) {
            NewsViewModel newsViewModel = this.newsViewModel;
            if (newsViewModel != null) {
                newsViewModel.loadMore();
            }
            this.isLoading = true;
            ThreadUtils.postToMainThreadDelayed(new NewsPresenter$loadMore$1(this), LOADMORE_THRESHOLD);
        }
    }

    public void onShow(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        updateSourcePriority(context);
        checkNewsRepositoryReset(context);
    }

    public final void checkNewsRepositoryReset(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        if (NewsRepository.Companion.isEmpty() && this.newsViewModel != null) {
            Repository instance = NewsRepository.Companion.getInstance(context);
            instance.setOnDataChangedListener(this.newsViewModel);
            NewsViewModel newsViewModel = this.newsViewModel;
            if (newsViewModel != null) {
                newsViewModel.setRepository(instance);
            }
            NewsViewModel newsViewModel2 = this.newsViewModel;
            if (newsViewModel2 != null) {
                MutableLiveData items = newsViewModel2.getItems();
                if (items != null) {
                    items.setValue(null);
                }
            }
            newsViewModel2 = this.newsViewModel;
            if (newsViewModel2 != null) {
                newsViewModel2.loadMore();
            }
        }
    }

    private final void updateSourcePriority(Context context) {
        Settings.getInstance(context).setPriority("pref_int_news_priority", 2);
    }
}
