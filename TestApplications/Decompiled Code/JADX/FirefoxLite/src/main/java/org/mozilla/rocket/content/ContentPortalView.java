package org.mozilla.rocket.content;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.p004v7.widget.GridLayoutManager;
import android.support.p004v7.widget.LinearLayoutManager;
import android.support.p004v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import java.util.ArrayList;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.lite.partner.NewsItem;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.content.data.ShoppingLink;

/* compiled from: ContentPortalView.kt */
public final class ContentPortalView extends CoordinatorLayout implements ContentPortalListener {
    public static final Companion Companion = new Companion();
    private LinearLayout bottomSheet;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private NewsAdapter<NewsItem> newsAdapter;
    private View newsEmptyView;
    private LinearLayoutManager newsListLayoutManager;
    private NewsListListener newsListListener;
    private ProgressBar newsProgressCenter;
    private RecyclerView recyclerView;

    /* compiled from: ContentPortalView.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: ContentPortalView.kt */
    public interface NewsListListener {
        void loadMore();

        void onShow(Context context);
    }

    public final NewsListListener getNewsListListener() {
        return this.newsListListener;
    }

    public final void setNewsListListener(NewsListListener newsListListener) {
        this.newsListListener = newsListListener;
    }

    public ContentPortalView(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context);
    }

    public ContentPortalView(Context context, AttributeSet attributeSet) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context, attributeSet);
    }

    public ContentPortalView(Context context, AttributeSet attributeSet, int i) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context, attributeSet, i);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private final void init() {
        setupContentPortalView();
        if (AppConfigWrapper.hasEcommerceShoppingLink()) {
            setupViewShoppingLink();
        } else {
            setupViewNews();
        }
    }

    private final void setupViewShoppingLink() {
        Object systemService = getContext().getSystemService("layout_inflater");
        if (systemService != null) {
            ((LayoutInflater) systemService).inflate(C0769R.layout.content_shoppinglink, this.bottomSheet);
            this.recyclerView = (RecyclerView) findViewById(C0427R.C0426id.ct_shoppinglink_list);
            ShoppingLinkAdapter shoppingLinkAdapter = new ShoppingLinkAdapter(this);
            RecyclerView recyclerView = this.recyclerView;
            if (recyclerView != null) {
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            }
            recyclerView = this.recyclerView;
            if (recyclerView != null) {
                recyclerView.setAdapter(shoppingLinkAdapter);
            }
            ArrayList ecommerceShoppingLinks = AppConfigWrapper.getEcommerceShoppingLinks();
            ecommerceShoppingLinks.add(new ShoppingLink("", "footer", "", ""));
            shoppingLinkAdapter.submitList(ecommerceShoppingLinks);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.view.LayoutInflater");
    }

    private final void setupViewNews() {
        Object systemService = getContext().getSystemService("layout_inflater");
        if (systemService != null) {
            ((LayoutInflater) systemService).inflate(C0769R.layout.content_news, this.bottomSheet);
            Button button = (Button) findViewById(C0427R.C0426id.news_try_again);
            if (button != null) {
                button.setOnClickListener(new ContentPortalView$setupViewNews$1(this));
            }
            this.recyclerView = (RecyclerView) findViewById(C0427R.C0426id.news_list);
            this.newsEmptyView = findViewById(C0427R.C0426id.empty_view_container);
            this.newsProgressCenter = (ProgressBar) findViewById(C0427R.C0426id.news_progress_center);
            this.newsAdapter = new NewsAdapter(this);
            RecyclerView recyclerView = this.recyclerView;
            if (recyclerView != null) {
                recyclerView.setAdapter(this.newsAdapter);
            }
            this.newsListLayoutManager = new LinearLayoutManager(getContext(), 1, false);
            recyclerView = this.recyclerView;
            if (recyclerView != null) {
                recyclerView.setLayoutManager(this.newsListLayoutManager);
            }
            LinearLayoutManager linearLayoutManager = this.newsListLayoutManager;
            if (linearLayoutManager != null) {
                RecyclerView recyclerView2 = this.recyclerView;
                if (recyclerView2 != null) {
                    recyclerView2.addOnScrollListener(new ContentPortalView$setupViewNews$$inlined$let$lambda$1(linearLayoutManager, this));
                    return;
                }
                return;
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.view.LayoutInflater");
    }

    private final void showInternal() {
        setVisibility(0);
        BottomSheetBehavior bottomSheetBehavior = this.bottomSheetBehavior;
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(3);
        }
    }

    public final void show(boolean z) {
        if (getVisibility() != 0) {
            HomeFragmentViewState.lastOpenNews();
            NewsListListener newsListListener = this.newsListListener;
            if (newsListListener != null) {
                Context context = getContext();
                Intrinsics.checkExpressionValueIsNotNull(context, "context");
                newsListListener.onShow(context);
            }
            if (z) {
                Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), C0769R.anim.tab_transition_fade_in);
                if (loadAnimation != null) {
                    loadAnimation.setAnimationListener(new ContentPortalView$show$$inlined$also$lambda$1(this));
                    startAnimation(loadAnimation);
                }
                return;
            }
            showInternal();
        }
    }

    public final boolean hide() {
        if (getVisibility() == 8) {
            return false;
        }
        HomeFragmentViewState.reset();
        NewsRepository.Companion.reset();
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), C0769R.anim.tab_transition_fade_out);
        if (loadAnimation != null) {
            loadAnimation.setAnimationListener(new ContentPortalView$hide$$inlined$also$lambda$1(this));
            startAnimation(loadAnimation);
        }
        return true;
    }

    private final void setupContentPortalView() {
        setOnClickListener(new ContentPortalView$setupContentPortalView$1(this));
        this.bottomSheet = (LinearLayout) findViewById(C0427R.C0426id.bottom_sheet);
        this.bottomSheetBehavior = BottomSheetBehavior.from(this.bottomSheet);
        BottomSheetBehavior bottomSheetBehavior = this.bottomSheetBehavior;
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(3);
        }
        bottomSheetBehavior = this.bottomSheetBehavior;
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setBottomSheetCallback(new ContentPortalView$setupContentPortalView$2(this));
        }
    }

    public void onStatus(List<? extends NewsItem> list) {
        RecyclerView recyclerView;
        View view;
        ProgressBar progressBar;
        BottomSheetBehavior bottomSheetBehavior;
        if (list == null) {
            recyclerView = this.recyclerView;
            if (recyclerView != null) {
                recyclerView.setVisibility(8);
            }
            view = this.newsEmptyView;
            if (view != null) {
                view.setVisibility(8);
            }
            progressBar = this.newsProgressCenter;
            if (progressBar != null) {
                progressBar.setVisibility(0);
            }
            bottomSheetBehavior = this.bottomSheetBehavior;
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.setSkipCollapsed(true);
            }
        } else if (list.size() == 0) {
            recyclerView = this.recyclerView;
            if (recyclerView != null) {
                recyclerView.setVisibility(8);
            }
            view = this.newsEmptyView;
            if (view != null) {
                view.setVisibility(0);
            }
            progressBar = this.newsProgressCenter;
            if (progressBar != null) {
                progressBar.setVisibility(8);
            }
            bottomSheetBehavior = this.bottomSheetBehavior;
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.setSkipCollapsed(true);
            }
        } else {
            recyclerView = this.recyclerView;
            if (recyclerView != null) {
                recyclerView.setVisibility(0);
            }
            view = this.newsEmptyView;
            if (view != null) {
                view.setVisibility(8);
            }
            progressBar = this.newsProgressCenter;
            if (progressBar != null) {
                progressBar.setVisibility(8);
            }
        }
    }

    public void onItemClicked(String str) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        ScreenNavigator.get(getContext()).showBrowserScreen(str, true, false);
        LinearLayoutManager linearLayoutManager = this.newsListLayoutManager;
        if (linearLayoutManager != null) {
            HomeFragmentViewState.INSTANCE.setLastScrollPos(Integer.valueOf(linearLayoutManager.findFirstVisibleItemPosition()));
        }
    }

    public final void setNewsContent(List<? extends NewsItem> list) {
        onStatus(list);
        NewsAdapter newsAdapter = this.newsAdapter;
        if (newsAdapter != null) {
            newsAdapter.submitList(list);
        }
        Integer lastScrollPos = HomeFragmentViewState.INSTANCE.getLastScrollPos();
        if (lastScrollPos != null) {
            int intValue = lastScrollPos.intValue();
            Integer valueOf = list != null ? Integer.valueOf(list.size()) : null;
            if (valueOf != null && valueOf.intValue() > intValue) {
                LinearLayoutManager linearLayoutManager = this.newsListLayoutManager;
                if (linearLayoutManager != null) {
                    linearLayoutManager.scrollToPosition(intValue);
                }
                HomeFragmentViewState.INSTANCE.setLastScrollPos((Integer) null);
            }
        }
    }

    public final void onResume() {
        if (HomeFragmentViewState.INSTANCE.isLastOpenNews()) {
            show(false);
        } else {
            hide();
        }
    }
}
