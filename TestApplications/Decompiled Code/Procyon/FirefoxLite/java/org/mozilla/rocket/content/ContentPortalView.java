// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.content;

import android.support.v7.recyclerview.extensions.ListAdapter;
import org.mozilla.focus.navigation.ScreenNavigator;
import android.view.animation.Animation;
import android.view.animation.Animation$AnimationListener;
import android.view.animation.AnimationUtils;
import java.util.List;
import java.util.ArrayList;
import org.mozilla.rocket.content.data.ShoppingLink;
import android.support.v7.widget.GridLayoutManager;
import kotlin.TypeCastException;
import android.widget.Button;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View$OnClickListener;
import org.mozilla.focus.utils.AppConfigWrapper;
import android.util.AttributeSet;
import kotlin.jvm.internal.Intrinsics;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import org.mozilla.lite.partner.NewsItem;
import android.view.View;
import android.support.design.widget.BottomSheetBehavior;
import android.widget.LinearLayout;
import android.support.design.widget.CoordinatorLayout;

public final class ContentPortalView extends CoordinatorLayout implements ContentPortalListener
{
    public static final Companion Companion;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private NewsAdapter<NewsItem> newsAdapter;
    private View newsEmptyView;
    private LinearLayoutManager newsListLayoutManager;
    private NewsListListener newsListListener;
    private ProgressBar newsProgressCenter;
    private RecyclerView recyclerView;
    
    static {
        Companion = new Companion(null);
    }
    
    public ContentPortalView(final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context);
    }
    
    public ContentPortalView(final Context context, final AttributeSet set) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context, set);
    }
    
    public ContentPortalView(final Context context, final AttributeSet set, final int n) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context, set, n);
    }
    
    private final void init() {
        this.setupContentPortalView();
        if (AppConfigWrapper.hasEcommerceShoppingLink()) {
            this.setupViewShoppingLink();
        }
        else {
            this.setupViewNews();
        }
    }
    
    private final void setupContentPortalView() {
        this.setOnClickListener((View$OnClickListener)new ContentPortalView$setupContentPortalView.ContentPortalView$setupContentPortalView$1(this));
        this.bottomSheet = (LinearLayout)this.findViewById(2131296328);
        this.bottomSheetBehavior = BottomSheetBehavior.from((View)this.bottomSheet);
        final BottomSheetBehavior<View> bottomSheetBehavior = this.bottomSheetBehavior;
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(3);
        }
        final BottomSheetBehavior<View> bottomSheetBehavior2 = this.bottomSheetBehavior;
        if (bottomSheetBehavior2 != null) {
            bottomSheetBehavior2.setBottomSheetCallback((BottomSheetBehavior.BottomSheetCallback)new ContentPortalView$setupContentPortalView.ContentPortalView$setupContentPortalView$2(this));
        }
    }
    
    private final void setupViewNews() {
        final Object systemService = this.getContext().getSystemService("layout_inflater");
        if (systemService != null) {
            ((LayoutInflater)systemService).inflate(2131492921, (ViewGroup)this.bottomSheet);
            final Button button = (Button)this.findViewById(2131296546);
            if (button != null) {
                button.setOnClickListener((View$OnClickListener)new ContentPortalView$setupViewNews.ContentPortalView$setupViewNews$1(this));
            }
            this.recyclerView = (RecyclerView)this.findViewById(2131296543);
            this.newsEmptyView = this.findViewById(2131296422);
            this.newsProgressCenter = (ProgressBar)this.findViewById(2131296544);
            this.newsAdapter = new NewsAdapter<NewsItem>(this);
            final RecyclerView recyclerView = this.recyclerView;
            if (recyclerView != null) {
                recyclerView.setAdapter((RecyclerView.Adapter)this.newsAdapter);
            }
            this.newsListLayoutManager = new LinearLayoutManager(this.getContext(), 1, false);
            final RecyclerView recyclerView2 = this.recyclerView;
            if (recyclerView2 != null) {
                recyclerView2.setLayoutManager((RecyclerView.LayoutManager)this.newsListLayoutManager);
            }
            final LinearLayoutManager newsListLayoutManager = this.newsListLayoutManager;
            if (newsListLayoutManager != null) {
                final RecyclerView recyclerView3 = this.recyclerView;
                if (recyclerView3 != null) {
                    recyclerView3.addOnScrollListener((RecyclerView.OnScrollListener)new ContentPortalView$setupViewNews$$inlined$let$lambda.ContentPortalView$setupViewNews$$inlined$let$lambda$1(newsListLayoutManager, this));
                }
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.view.LayoutInflater");
    }
    
    private final void setupViewShoppingLink() {
        final Object systemService = this.getContext().getSystemService("layout_inflater");
        if (systemService != null) {
            ((LayoutInflater)systemService).inflate(2131492923, (ViewGroup)this.bottomSheet);
            this.recyclerView = (RecyclerView)this.findViewById(2131296387);
            final ShoppingLinkAdapter shoppingLinkAdapter = new ShoppingLinkAdapter(this);
            final RecyclerView recyclerView = this.recyclerView;
            if (recyclerView != null) {
                recyclerView.setLayoutManager((RecyclerView.LayoutManager)new GridLayoutManager(this.getContext(), 2));
            }
            final RecyclerView recyclerView2 = this.recyclerView;
            if (recyclerView2 != null) {
                recyclerView2.setAdapter((RecyclerView.Adapter)shoppingLinkAdapter);
            }
            final ArrayList<ShoppingLink> ecommerceShoppingLinks = AppConfigWrapper.getEcommerceShoppingLinks();
            ecommerceShoppingLinks.add(new ShoppingLink("", "footer", "", ""));
            ((ListAdapter<ShoppingLink, VH>)shoppingLinkAdapter).submitList(ecommerceShoppingLinks);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.view.LayoutInflater");
    }
    
    private final void showInternal() {
        this.setVisibility(0);
        final BottomSheetBehavior<View> bottomSheetBehavior = this.bottomSheetBehavior;
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(3);
        }
    }
    
    public final NewsListListener getNewsListListener() {
        return this.newsListListener;
    }
    
    public final boolean hide() {
        if (this.getVisibility() == 8) {
            return false;
        }
        HomeFragmentViewState.reset();
        NewsRepository.Companion.reset();
        final Animation loadAnimation = AnimationUtils.loadAnimation(this.getContext(), 2130771990);
        if (loadAnimation != null) {
            loadAnimation.setAnimationListener((Animation$AnimationListener)new ContentPortalView$hide$$inlined$also$lambda.ContentPortalView$hide$$inlined$also$lambda$1(this));
            this.startAnimation(loadAnimation);
        }
        return true;
    }
    
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.init();
    }
    
    @Override
    public void onItemClicked(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "url");
        ScreenNavigator.get(this.getContext()).showBrowserScreen(s, true, false);
        final LinearLayoutManager newsListLayoutManager = this.newsListLayoutManager;
        if (newsListLayoutManager != null) {
            HomeFragmentViewState.INSTANCE.setLastScrollPos(newsListLayoutManager.findFirstVisibleItemPosition());
        }
    }
    
    public final void onResume() {
        if (HomeFragmentViewState.INSTANCE.isLastOpenNews()) {
            this.show(false);
        }
        else {
            this.hide();
        }
    }
    
    public void onStatus(final List<? extends NewsItem> list) {
        if (list == null) {
            final RecyclerView recyclerView = this.recyclerView;
            if (recyclerView != null) {
                recyclerView.setVisibility(8);
            }
            final View newsEmptyView = this.newsEmptyView;
            if (newsEmptyView != null) {
                newsEmptyView.setVisibility(8);
            }
            final ProgressBar newsProgressCenter = this.newsProgressCenter;
            if (newsProgressCenter != null) {
                newsProgressCenter.setVisibility(0);
            }
            final BottomSheetBehavior<View> bottomSheetBehavior = this.bottomSheetBehavior;
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.setSkipCollapsed(true);
            }
        }
        else if (list.size() == 0) {
            final RecyclerView recyclerView2 = this.recyclerView;
            if (recyclerView2 != null) {
                recyclerView2.setVisibility(8);
            }
            final View newsEmptyView2 = this.newsEmptyView;
            if (newsEmptyView2 != null) {
                newsEmptyView2.setVisibility(0);
            }
            final ProgressBar newsProgressCenter2 = this.newsProgressCenter;
            if (newsProgressCenter2 != null) {
                newsProgressCenter2.setVisibility(8);
            }
            final BottomSheetBehavior<View> bottomSheetBehavior2 = this.bottomSheetBehavior;
            if (bottomSheetBehavior2 != null) {
                bottomSheetBehavior2.setSkipCollapsed(true);
            }
        }
        else {
            final RecyclerView recyclerView3 = this.recyclerView;
            if (recyclerView3 != null) {
                recyclerView3.setVisibility(0);
            }
            final View newsEmptyView3 = this.newsEmptyView;
            if (newsEmptyView3 != null) {
                newsEmptyView3.setVisibility(8);
            }
            final ProgressBar newsProgressCenter3 = this.newsProgressCenter;
            if (newsProgressCenter3 != null) {
                newsProgressCenter3.setVisibility(8);
            }
        }
    }
    
    public final void setNewsContent(final List<? extends NewsItem> list) {
        this.onStatus(list);
        final NewsAdapter<NewsItem> newsAdapter = this.newsAdapter;
        if (newsAdapter != null) {
            newsAdapter.submitList(list);
        }
        final Integer lastScrollPos = HomeFragmentViewState.INSTANCE.getLastScrollPos();
        if (lastScrollPos != null) {
            final int intValue = lastScrollPos.intValue();
            Integer value;
            if (list != null) {
                value = list.size();
            }
            else {
                value = null;
            }
            if (value != null && value > intValue) {
                final LinearLayoutManager newsListLayoutManager = this.newsListLayoutManager;
                if (newsListLayoutManager != null) {
                    newsListLayoutManager.scrollToPosition(intValue);
                }
                HomeFragmentViewState.INSTANCE.setLastScrollPos(null);
            }
        }
    }
    
    public final void setNewsListListener(final NewsListListener newsListListener) {
        this.newsListListener = newsListListener;
    }
    
    public final void show(final boolean b) {
        if (this.getVisibility() == 0) {
            return;
        }
        HomeFragmentViewState.lastOpenNews();
        final NewsListListener newsListListener = this.newsListListener;
        if (newsListListener != null) {
            final Context context = this.getContext();
            Intrinsics.checkExpressionValueIsNotNull(context, "context");
            newsListListener.onShow(context);
        }
        if (!b) {
            this.showInternal();
            return;
        }
        final Animation loadAnimation = AnimationUtils.loadAnimation(this.getContext(), 2130771989);
        if (loadAnimation != null) {
            loadAnimation.setAnimationListener((Animation$AnimationListener)new ContentPortalView$show$$inlined$also$lambda.ContentPortalView$show$$inlined$also$lambda$1(this));
            this.startAnimation(loadAnimation);
        }
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
    
    public interface NewsListListener
    {
        void loadMore();
        
        void onShow(final Context p0);
    }
}
