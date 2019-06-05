// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.support.v4.widget.TextViewCompat;
import android.annotation.TargetApi;
import android.view.accessibility.AccessibilityNodeInfo;
import android.support.v7.app.ActionBar;
import android.view.accessibility.AccessibilityEvent;
import android.support.v7.widget.TooltipCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.view.ViewGroup$MarginLayoutParams;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.support.design.ripple.RippleUtils;
import android.text.Layout;
import android.support.v4.view.PointerIconCompat;
import android.widget.TextView;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Build$VERSION;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.widget.LinearLayout;
import android.support.v7.content.res.AppCompatResources;
import android.animation.Animator$AnimatorListener;
import java.util.Iterator;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.view.ViewParent;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.support.design.animation.AnimationUtils;
import android.widget.LinearLayout$LayoutParams;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.content.res.Resources;
import android.support.design.internal.ViewUtils;
import android.content.res.TypedArray;
import android.support.design.resources.MaterialResources;
import android.support.design.internal.ThemeEnforcement;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.FrameLayout$LayoutParams;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import java.util.ArrayList;
import android.animation.ValueAnimator;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.util.Pools;
import android.support.v4.view.ViewPager;
import android.widget.HorizontalScrollView;

@ViewPager.DecorView
public class TabLayout extends HorizontalScrollView
{
    private static final Pools.Pool<Tab> tabPool;
    private AdapterChangeListener adapterChangeListener;
    private int contentInsetStart;
    private BaseOnTabSelectedListener currentVpSelectedListener;
    boolean inlineLabel;
    int mode;
    private TabLayoutOnPageChangeListener pageChangeListener;
    private PagerAdapter pagerAdapter;
    private DataSetObserver pagerAdapterObserver;
    private final int requestedTabMaxWidth;
    private final int requestedTabMinWidth;
    private ValueAnimator scrollAnimator;
    private final int scrollableTabMinWidth;
    private BaseOnTabSelectedListener selectedListener;
    private final ArrayList<BaseOnTabSelectedListener> selectedListeners;
    private Tab selectedTab;
    private boolean setupViewPagerImplicitly;
    private final SlidingTabIndicator slidingTabIndicator;
    final int tabBackgroundResId;
    int tabGravity;
    ColorStateList tabIconTint;
    PorterDuff$Mode tabIconTintMode;
    int tabIndicatorAnimationDuration;
    boolean tabIndicatorFullWidth;
    int tabIndicatorGravity;
    int tabMaxWidth;
    int tabPaddingBottom;
    int tabPaddingEnd;
    int tabPaddingStart;
    int tabPaddingTop;
    ColorStateList tabRippleColorStateList;
    Drawable tabSelectedIndicator;
    int tabTextAppearance;
    ColorStateList tabTextColors;
    float tabTextMultiLineSize;
    float tabTextSize;
    private final RectF tabViewContentBounds;
    private final Pools.Pool<TabView> tabViewPool;
    private final ArrayList<Tab> tabs;
    boolean unboundedRipple;
    ViewPager viewPager;
    
    static {
        tabPool = new Pools.SynchronizedPool<Tab>(16);
    }
    
    public TabLayout(final Context context) {
        this(context, null);
    }
    
    public TabLayout(final Context context, final AttributeSet set) {
        this(context, set, R.attr.tabStyle);
    }
    
    public TabLayout(final Context context, AttributeSet obtainStyledAttributes, int n) {
        super(context, obtainStyledAttributes, n);
        this.tabs = new ArrayList<Tab>();
        this.tabViewContentBounds = new RectF();
        this.tabMaxWidth = Integer.MAX_VALUE;
        this.selectedListeners = new ArrayList<BaseOnTabSelectedListener>();
        this.tabViewPool = new Pools.SimplePool<TabView>(12);
        this.setHorizontalScrollBarEnabled(false);
        super.addView((View)(this.slidingTabIndicator = new SlidingTabIndicator(context)), 0, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-2, -1));
        final TypedArray obtainStyledAttributes2 = ThemeEnforcement.obtainStyledAttributes(context, obtainStyledAttributes, R.styleable.TabLayout, n, R.style.Widget_Design_TabLayout, R.styleable.TabLayout_tabTextAppearance);
        this.slidingTabIndicator.setSelectedIndicatorHeight(obtainStyledAttributes2.getDimensionPixelSize(R.styleable.TabLayout_tabIndicatorHeight, -1));
        this.slidingTabIndicator.setSelectedIndicatorColor(obtainStyledAttributes2.getColor(R.styleable.TabLayout_tabIndicatorColor, 0));
        this.setSelectedTabIndicator(MaterialResources.getDrawable(context, obtainStyledAttributes2, R.styleable.TabLayout_tabIndicator));
        this.setSelectedTabIndicatorGravity(obtainStyledAttributes2.getInt(R.styleable.TabLayout_tabIndicatorGravity, 0));
        this.setTabIndicatorFullWidth(obtainStyledAttributes2.getBoolean(R.styleable.TabLayout_tabIndicatorFullWidth, true));
        n = obtainStyledAttributes2.getDimensionPixelSize(R.styleable.TabLayout_tabPadding, 0);
        this.tabPaddingBottom = n;
        this.tabPaddingEnd = n;
        this.tabPaddingTop = n;
        this.tabPaddingStart = n;
        this.tabPaddingStart = obtainStyledAttributes2.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingStart, this.tabPaddingStart);
        this.tabPaddingTop = obtainStyledAttributes2.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingTop, this.tabPaddingTop);
        this.tabPaddingEnd = obtainStyledAttributes2.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingEnd, this.tabPaddingEnd);
        this.tabPaddingBottom = obtainStyledAttributes2.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingBottom, this.tabPaddingBottom);
        this.tabTextAppearance = obtainStyledAttributes2.getResourceId(R.styleable.TabLayout_tabTextAppearance, R.style.TextAppearance_Design_Tab);
        obtainStyledAttributes = (AttributeSet)context.obtainStyledAttributes(this.tabTextAppearance, android.support.v7.appcompat.R.styleable.TextAppearance);
        try {
            this.tabTextSize = (float)((TypedArray)obtainStyledAttributes).getDimensionPixelSize(android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize, 0);
            this.tabTextColors = MaterialResources.getColorStateList(context, (TypedArray)obtainStyledAttributes, android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor);
            ((TypedArray)obtainStyledAttributes).recycle();
            if (obtainStyledAttributes2.hasValue(R.styleable.TabLayout_tabTextColor)) {
                this.tabTextColors = MaterialResources.getColorStateList(context, obtainStyledAttributes2, R.styleable.TabLayout_tabTextColor);
            }
            if (obtainStyledAttributes2.hasValue(R.styleable.TabLayout_tabSelectedTextColor)) {
                n = obtainStyledAttributes2.getColor(R.styleable.TabLayout_tabSelectedTextColor, 0);
                this.tabTextColors = createColorStateList(this.tabTextColors.getDefaultColor(), n);
            }
            this.tabIconTint = MaterialResources.getColorStateList(context, obtainStyledAttributes2, R.styleable.TabLayout_tabIconTint);
            this.tabIconTintMode = ViewUtils.parseTintMode(obtainStyledAttributes2.getInt(R.styleable.TabLayout_tabIconTintMode, -1), null);
            this.tabRippleColorStateList = MaterialResources.getColorStateList(context, obtainStyledAttributes2, R.styleable.TabLayout_tabRippleColor);
            this.tabIndicatorAnimationDuration = obtainStyledAttributes2.getInt(R.styleable.TabLayout_tabIndicatorAnimationDuration, 300);
            this.requestedTabMinWidth = obtainStyledAttributes2.getDimensionPixelSize(R.styleable.TabLayout_tabMinWidth, -1);
            this.requestedTabMaxWidth = obtainStyledAttributes2.getDimensionPixelSize(R.styleable.TabLayout_tabMaxWidth, -1);
            this.tabBackgroundResId = obtainStyledAttributes2.getResourceId(R.styleable.TabLayout_tabBackground, 0);
            this.contentInsetStart = obtainStyledAttributes2.getDimensionPixelSize(R.styleable.TabLayout_tabContentStart, 0);
            this.mode = obtainStyledAttributes2.getInt(R.styleable.TabLayout_tabMode, 1);
            this.tabGravity = obtainStyledAttributes2.getInt(R.styleable.TabLayout_tabGravity, 0);
            this.inlineLabel = obtainStyledAttributes2.getBoolean(R.styleable.TabLayout_tabInlineLabel, false);
            this.unboundedRipple = obtainStyledAttributes2.getBoolean(R.styleable.TabLayout_tabUnboundedRipple, false);
            obtainStyledAttributes2.recycle();
            final Resources resources = this.getResources();
            this.tabTextMultiLineSize = (float)resources.getDimensionPixelSize(R.dimen.design_tab_text_size_2line);
            this.scrollableTabMinWidth = resources.getDimensionPixelSize(R.dimen.design_tab_scrollable_min_width);
            this.applyModeAndGravity();
        }
        finally {
            ((TypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    private void addTabFromItemView(final TabItem tabItem) {
        final Tab tab = this.newTab();
        if (tabItem.text != null) {
            tab.setText(tabItem.text);
        }
        if (tabItem.icon != null) {
            tab.setIcon(tabItem.icon);
        }
        if (tabItem.customLayout != 0) {
            tab.setCustomView(tabItem.customLayout);
        }
        if (!TextUtils.isEmpty(tabItem.getContentDescription())) {
            tab.setContentDescription(tabItem.getContentDescription());
        }
        this.addTab(tab);
    }
    
    private void addTabView(final Tab tab) {
        this.slidingTabIndicator.addView((View)tab.view, tab.getPosition(), (ViewGroup$LayoutParams)this.createLayoutParamsForTabs());
    }
    
    private void addViewInternal(final View view) {
        if (view instanceof TabItem) {
            this.addTabFromItemView((TabItem)view);
            return;
        }
        throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
    }
    
    private void animateToTab(final int n) {
        if (n == -1) {
            return;
        }
        if (this.getWindowToken() != null && ViewCompat.isLaidOut((View)this) && !this.slidingTabIndicator.childrenNeedLayout()) {
            final int scrollX = this.getScrollX();
            final int calculateScrollXForTab = this.calculateScrollXForTab(n, 0.0f);
            if (scrollX != calculateScrollXForTab) {
                this.ensureScrollAnimator();
                this.scrollAnimator.setIntValues(new int[] { scrollX, calculateScrollXForTab });
                this.scrollAnimator.start();
            }
            this.slidingTabIndicator.animateIndicatorToPosition(n, this.tabIndicatorAnimationDuration);
            return;
        }
        this.setScrollPosition(n, 0.0f, true);
    }
    
    private void applyModeAndGravity() {
        int max;
        if (this.mode == 0) {
            max = Math.max(0, this.contentInsetStart - this.tabPaddingStart);
        }
        else {
            max = 0;
        }
        ViewCompat.setPaddingRelative((View)this.slidingTabIndicator, max, 0, 0, 0);
        switch (this.mode) {
            case 1: {
                this.slidingTabIndicator.setGravity(1);
                break;
            }
            case 0: {
                this.slidingTabIndicator.setGravity(8388611);
                break;
            }
        }
        this.updateTabViews(true);
    }
    
    private int calculateScrollXForTab(int width, final float n) {
        final int mode = this.mode;
        int width2 = 0;
        if (mode == 0) {
            final View child = this.slidingTabIndicator.getChildAt(width);
            View child2;
            if (++width < this.slidingTabIndicator.getChildCount()) {
                child2 = this.slidingTabIndicator.getChildAt(width);
            }
            else {
                child2 = null;
            }
            if (child != null) {
                width = child.getWidth();
            }
            else {
                width = 0;
            }
            if (child2 != null) {
                width2 = child2.getWidth();
            }
            final int n2 = child.getLeft() + width / 2 - this.getWidth() / 2;
            width = (int)((width + width2) * 0.5f * n);
            if (ViewCompat.getLayoutDirection((View)this) == 0) {
                width += n2;
            }
            else {
                width = n2 - width;
            }
            return width;
        }
        return 0;
    }
    
    private void configureTab(final Tab element, int n) {
        element.setPosition(n);
        this.tabs.add(n, element);
        while (++n < this.tabs.size()) {
            this.tabs.get(n).setPosition(n);
        }
    }
    
    private static ColorStateList createColorStateList(final int n, final int n2) {
        return new ColorStateList(new int[][] { TabLayout.SELECTED_STATE_SET, TabLayout.EMPTY_STATE_SET }, new int[] { n2, n });
    }
    
    private LinearLayout$LayoutParams createLayoutParamsForTabs() {
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(-2, -1);
        this.updateTabViewLayoutParams(linearLayout$LayoutParams);
        return linearLayout$LayoutParams;
    }
    
    private TabView createTabView(final Tab tab) {
        TabView tabView;
        if (this.tabViewPool != null) {
            tabView = this.tabViewPool.acquire();
        }
        else {
            tabView = null;
        }
        TabView tabView2 = tabView;
        if (tabView == null) {
            tabView2 = new TabView(this.getContext());
        }
        tabView2.setTab(tab);
        tabView2.setFocusable(true);
        tabView2.setMinimumWidth(this.getTabMinWidth());
        if (TextUtils.isEmpty(tab.contentDesc)) {
            tabView2.setContentDescription(tab.text);
        }
        else {
            tabView2.setContentDescription(tab.contentDesc);
        }
        return tabView2;
    }
    
    private void dispatchTabReselected(final Tab tab) {
        for (int i = this.selectedListeners.size() - 1; i >= 0; --i) {
            ((BaseOnTabSelectedListener<Tab>)this.selectedListeners.get(i)).onTabReselected(tab);
        }
    }
    
    private void dispatchTabSelected(final Tab tab) {
        for (int i = this.selectedListeners.size() - 1; i >= 0; --i) {
            ((BaseOnTabSelectedListener<Tab>)this.selectedListeners.get(i)).onTabSelected(tab);
        }
    }
    
    private void dispatchTabUnselected(final Tab tab) {
        for (int i = this.selectedListeners.size() - 1; i >= 0; --i) {
            ((BaseOnTabSelectedListener<Tab>)this.selectedListeners.get(i)).onTabUnselected(tab);
        }
    }
    
    private void ensureScrollAnimator() {
        if (this.scrollAnimator == null) {
            (this.scrollAnimator = new ValueAnimator()).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.scrollAnimator.setDuration((long)this.tabIndicatorAnimationDuration);
            this.scrollAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                    TabLayout.this.scrollTo((int)valueAnimator.getAnimatedValue(), 0);
                }
            });
        }
    }
    
    private int getDefaultHeight() {
        final int size = this.tabs.size();
        final int n = 0;
        int index = 0;
        int n2;
        while (true) {
            n2 = n;
            if (index >= size) {
                break;
            }
            final Tab tab = this.tabs.get(index);
            if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty(tab.getText())) {
                n2 = 1;
                break;
            }
            ++index;
        }
        int n3;
        if (n2 != 0 && !this.inlineLabel) {
            n3 = 72;
        }
        else {
            n3 = 48;
        }
        return n3;
    }
    
    private int getTabMinWidth() {
        if (this.requestedTabMinWidth != -1) {
            return this.requestedTabMinWidth;
        }
        int scrollableTabMinWidth;
        if (this.mode == 0) {
            scrollableTabMinWidth = this.scrollableTabMinWidth;
        }
        else {
            scrollableTabMinWidth = 0;
        }
        return scrollableTabMinWidth;
    }
    
    private int getTabScrollRange() {
        return Math.max(0, this.slidingTabIndicator.getWidth() - this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
    }
    
    private void removeTabViewAt(final int n) {
        final TabView tabView = (TabView)this.slidingTabIndicator.getChildAt(n);
        this.slidingTabIndicator.removeViewAt(n);
        if (tabView != null) {
            tabView.reset();
            this.tabViewPool.release(tabView);
        }
        this.requestLayout();
    }
    
    private void setSelectedTabView(final int n) {
        final int childCount = this.slidingTabIndicator.getChildCount();
        if (n < childCount) {
            for (int i = 0; i < childCount; ++i) {
                final View child = this.slidingTabIndicator.getChildAt(i);
                final boolean b = true;
                child.setSelected(i == n);
                child.setActivated(i == n && b);
            }
        }
    }
    
    private void setupWithViewPager(final ViewPager viewPager, final boolean autoRefresh, final boolean setupViewPagerImplicitly) {
        if (this.viewPager != null) {
            if (this.pageChangeListener != null) {
                this.viewPager.removeOnPageChangeListener((ViewPager.OnPageChangeListener)this.pageChangeListener);
            }
            if (this.adapterChangeListener != null) {
                this.viewPager.removeOnAdapterChangeListener((ViewPager.OnAdapterChangeListener)this.adapterChangeListener);
            }
        }
        if (this.currentVpSelectedListener != null) {
            this.removeOnTabSelectedListener(this.currentVpSelectedListener);
            this.currentVpSelectedListener = null;
        }
        if (viewPager != null) {
            this.viewPager = viewPager;
            if (this.pageChangeListener == null) {
                this.pageChangeListener = new TabLayoutOnPageChangeListener(this);
            }
            this.pageChangeListener.reset();
            viewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)this.pageChangeListener);
            this.addOnTabSelectedListener(this.currentVpSelectedListener = (BaseOnTabSelectedListener)new ViewPagerOnTabSelectedListener(viewPager));
            final PagerAdapter adapter = viewPager.getAdapter();
            if (adapter != null) {
                this.setPagerAdapter(adapter, autoRefresh);
            }
            if (this.adapterChangeListener == null) {
                this.adapterChangeListener = new AdapterChangeListener();
            }
            this.adapterChangeListener.setAutoRefresh(autoRefresh);
            viewPager.addOnAdapterChangeListener((ViewPager.OnAdapterChangeListener)this.adapterChangeListener);
            this.setScrollPosition(viewPager.getCurrentItem(), 0.0f, true);
        }
        else {
            this.viewPager = null;
            this.setPagerAdapter(null, false);
        }
        this.setupViewPagerImplicitly = setupViewPagerImplicitly;
    }
    
    private void updateAllTabs() {
        for (int size = this.tabs.size(), i = 0; i < size; ++i) {
            this.tabs.get(i).updateView();
        }
    }
    
    private void updateTabViewLayoutParams(final LinearLayout$LayoutParams linearLayout$LayoutParams) {
        if (this.mode == 1 && this.tabGravity == 0) {
            linearLayout$LayoutParams.width = 0;
            linearLayout$LayoutParams.weight = 1.0f;
        }
        else {
            linearLayout$LayoutParams.width = -2;
            linearLayout$LayoutParams.weight = 0.0f;
        }
    }
    
    public void addOnTabSelectedListener(final BaseOnTabSelectedListener baseOnTabSelectedListener) {
        if (!this.selectedListeners.contains(baseOnTabSelectedListener)) {
            this.selectedListeners.add(baseOnTabSelectedListener);
        }
    }
    
    public void addTab(final Tab tab) {
        this.addTab(tab, this.tabs.isEmpty());
    }
    
    public void addTab(final Tab tab, final int n, final boolean b) {
        if (tab.parent == this) {
            this.configureTab(tab, n);
            this.addTabView(tab);
            if (b) {
                tab.select();
            }
            return;
        }
        throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
    }
    
    public void addTab(final Tab tab, final boolean b) {
        this.addTab(tab, this.tabs.size(), b);
    }
    
    public void addView(final View view) {
        this.addViewInternal(view);
    }
    
    public void addView(final View view, final int n) {
        this.addViewInternal(view);
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.addViewInternal(view);
    }
    
    public void addView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.addViewInternal(view);
    }
    
    protected Tab createTabFromPool() {
        Tab tab;
        if ((tab = TabLayout.tabPool.acquire()) == null) {
            tab = new Tab();
        }
        return tab;
    }
    
    int dpToPx(final int n) {
        return Math.round(this.getResources().getDisplayMetrics().density * n);
    }
    
    public FrameLayout$LayoutParams generateLayoutParams(final AttributeSet set) {
        return this.generateDefaultLayoutParams();
    }
    
    public int getSelectedTabPosition() {
        int position;
        if (this.selectedTab != null) {
            position = this.selectedTab.getPosition();
        }
        else {
            position = -1;
        }
        return position;
    }
    
    public Tab getTabAt(final int index) {
        Tab tab;
        if (index >= 0 && index < this.getTabCount()) {
            tab = this.tabs.get(index);
        }
        else {
            tab = null;
        }
        return tab;
    }
    
    public int getTabCount() {
        return this.tabs.size();
    }
    
    public int getTabGravity() {
        return this.tabGravity;
    }
    
    public ColorStateList getTabIconTint() {
        return this.tabIconTint;
    }
    
    public int getTabIndicatorGravity() {
        return this.tabIndicatorGravity;
    }
    
    int getTabMaxWidth() {
        return this.tabMaxWidth;
    }
    
    public int getTabMode() {
        return this.mode;
    }
    
    public ColorStateList getTabRippleColor() {
        return this.tabRippleColorStateList;
    }
    
    public Drawable getTabSelectedIndicator() {
        return this.tabSelectedIndicator;
    }
    
    public ColorStateList getTabTextColors() {
        return this.tabTextColors;
    }
    
    public Tab newTab() {
        final Tab tabFromPool = this.createTabFromPool();
        tabFromPool.parent = this;
        tabFromPool.view = this.createTabView(tabFromPool);
        return tabFromPool;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.viewPager == null) {
            final ViewParent parent = this.getParent();
            if (parent instanceof ViewPager) {
                this.setupWithViewPager((ViewPager)parent, true, true);
            }
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.setupViewPagerImplicitly) {
            this.setupWithViewPager(null);
            this.setupViewPagerImplicitly = false;
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
            final View child = this.slidingTabIndicator.getChildAt(i);
            if (child instanceof TabView) {
                ((TabView)child).drawBackground(canvas);
            }
        }
        super.onDraw(canvas);
    }
    
    protected void onMeasure(int childMeasureSpec, int n) {
        final int a = this.dpToPx(this.getDefaultHeight()) + this.getPaddingTop() + this.getPaddingBottom();
        final int mode = View$MeasureSpec.getMode(n);
        if (mode != Integer.MIN_VALUE) {
            if (mode == 0) {
                n = View$MeasureSpec.makeMeasureSpec(a, 1073741824);
            }
        }
        else {
            n = View$MeasureSpec.makeMeasureSpec(Math.min(a, View$MeasureSpec.getSize(n)), 1073741824);
        }
        final int size = View$MeasureSpec.getSize(childMeasureSpec);
        if (View$MeasureSpec.getMode(childMeasureSpec) != 0) {
            int requestedTabMaxWidth;
            if (this.requestedTabMaxWidth > 0) {
                requestedTabMaxWidth = this.requestedTabMaxWidth;
            }
            else {
                requestedTabMaxWidth = size - this.dpToPx(56);
            }
            this.tabMaxWidth = requestedTabMaxWidth;
        }
        super.onMeasure(childMeasureSpec, n);
        if (this.getChildCount() == 1) {
            childMeasureSpec = 0;
            final View child = this.getChildAt(0);
            Label_0199: {
                switch (this.mode) {
                    default: {
                        break Label_0199;
                    }
                    case 1: {
                        if (child.getMeasuredWidth() != this.getMeasuredWidth()) {
                            break;
                        }
                        break Label_0199;
                    }
                    case 0: {
                        if (child.getMeasuredWidth() < this.getMeasuredWidth()) {
                            break;
                        }
                        break Label_0199;
                    }
                }
                childMeasureSpec = 1;
            }
            if (childMeasureSpec != 0) {
                childMeasureSpec = getChildMeasureSpec(n, this.getPaddingTop() + this.getPaddingBottom(), child.getLayoutParams().height);
                child.measure(View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824), childMeasureSpec);
            }
        }
    }
    
    void populateFromPagerAdapter() {
        this.removeAllTabs();
        if (this.pagerAdapter != null) {
            final int count = this.pagerAdapter.getCount();
            for (int i = 0; i < count; ++i) {
                this.addTab(this.newTab().setText(this.pagerAdapter.getPageTitle(i)), false);
            }
            if (this.viewPager != null && count > 0) {
                final int currentItem = this.viewPager.getCurrentItem();
                if (currentItem != this.getSelectedTabPosition() && currentItem < this.getTabCount()) {
                    this.selectTab(this.getTabAt(currentItem));
                }
            }
        }
    }
    
    protected boolean releaseFromTabPool(final Tab tab) {
        return TabLayout.tabPool.release(tab);
    }
    
    public void removeAllTabs() {
        for (int i = this.slidingTabIndicator.getChildCount() - 1; i >= 0; --i) {
            this.removeTabViewAt(i);
        }
        final Iterator<Tab> iterator = this.tabs.iterator();
        while (iterator.hasNext()) {
            final Tab tab = iterator.next();
            iterator.remove();
            tab.reset();
            this.releaseFromTabPool(tab);
        }
        this.selectedTab = null;
    }
    
    public void removeOnTabSelectedListener(final BaseOnTabSelectedListener o) {
        this.selectedListeners.remove(o);
    }
    
    void selectTab(final Tab tab) {
        this.selectTab(tab, true);
    }
    
    void selectTab(final Tab selectedTab, final boolean b) {
        final Tab selectedTab2 = this.selectedTab;
        if (selectedTab2 == selectedTab) {
            if (selectedTab2 != null) {
                this.dispatchTabReselected(selectedTab);
                this.animateToTab(selectedTab.getPosition());
            }
        }
        else {
            int position;
            if (selectedTab != null) {
                position = selectedTab.getPosition();
            }
            else {
                position = -1;
            }
            if (b) {
                if ((selectedTab2 == null || selectedTab2.getPosition() == -1) && position != -1) {
                    this.setScrollPosition(position, 0.0f, true);
                }
                else {
                    this.animateToTab(position);
                }
                if (position != -1) {
                    this.setSelectedTabView(position);
                }
            }
            this.selectedTab = selectedTab;
            if (selectedTab2 != null) {
                this.dispatchTabUnselected(selectedTab2);
            }
            if (selectedTab != null) {
                this.dispatchTabSelected(selectedTab);
            }
        }
    }
    
    public void setInlineLabel(final boolean inlineLabel) {
        if (this.inlineLabel != inlineLabel) {
            this.inlineLabel = inlineLabel;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
                final View child = this.slidingTabIndicator.getChildAt(i);
                if (child instanceof TabView) {
                    ((TabView)child).updateOrientation();
                }
            }
            this.applyModeAndGravity();
        }
    }
    
    public void setInlineLabelResource(final int n) {
        this.setInlineLabel(this.getResources().getBoolean(n));
    }
    
    @Deprecated
    public void setOnTabSelectedListener(final BaseOnTabSelectedListener selectedListener) {
        if (this.selectedListener != null) {
            this.removeOnTabSelectedListener(this.selectedListener);
        }
        if ((this.selectedListener = selectedListener) != null) {
            this.addOnTabSelectedListener(selectedListener);
        }
    }
    
    void setPagerAdapter(final PagerAdapter pagerAdapter, final boolean b) {
        if (this.pagerAdapter != null && this.pagerAdapterObserver != null) {
            this.pagerAdapter.unregisterDataSetObserver(this.pagerAdapterObserver);
        }
        this.pagerAdapter = pagerAdapter;
        if (b && pagerAdapter != null) {
            if (this.pagerAdapterObserver == null) {
                this.pagerAdapterObserver = new PagerAdapterObserver();
            }
            pagerAdapter.registerDataSetObserver(this.pagerAdapterObserver);
        }
        this.populateFromPagerAdapter();
    }
    
    void setScrollAnimatorListener(final Animator$AnimatorListener animator$AnimatorListener) {
        this.ensureScrollAnimator();
        this.scrollAnimator.addListener(animator$AnimatorListener);
    }
    
    public void setScrollPosition(final int n, final float n2, final boolean b) {
        this.setScrollPosition(n, n2, b, true);
    }
    
    void setScrollPosition(final int n, final float n2, final boolean b, final boolean b2) {
        final int round = Math.round(n + n2);
        if (round >= 0 && round < this.slidingTabIndicator.getChildCount()) {
            if (b2) {
                this.slidingTabIndicator.setIndicatorPositionFromTabPosition(n, n2);
            }
            if (this.scrollAnimator != null && this.scrollAnimator.isRunning()) {
                this.scrollAnimator.cancel();
            }
            this.scrollTo(this.calculateScrollXForTab(n, n2), 0);
            if (b) {
                this.setSelectedTabView(round);
            }
        }
    }
    
    public void setSelectedTabIndicator(final int n) {
        if (n != 0) {
            this.setSelectedTabIndicator(AppCompatResources.getDrawable(this.getContext(), n));
        }
        else {
            this.setSelectedTabIndicator(null);
        }
    }
    
    public void setSelectedTabIndicator(final Drawable tabSelectedIndicator) {
        if (this.tabSelectedIndicator != tabSelectedIndicator) {
            this.tabSelectedIndicator = tabSelectedIndicator;
            ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
        }
    }
    
    public void setSelectedTabIndicatorColor(final int selectedIndicatorColor) {
        this.slidingTabIndicator.setSelectedIndicatorColor(selectedIndicatorColor);
    }
    
    public void setSelectedTabIndicatorGravity(final int tabIndicatorGravity) {
        if (this.tabIndicatorGravity != tabIndicatorGravity) {
            this.tabIndicatorGravity = tabIndicatorGravity;
            ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
        }
    }
    
    @Deprecated
    public void setSelectedTabIndicatorHeight(final int selectedIndicatorHeight) {
        this.slidingTabIndicator.setSelectedIndicatorHeight(selectedIndicatorHeight);
    }
    
    public void setTabGravity(final int tabGravity) {
        if (this.tabGravity != tabGravity) {
            this.tabGravity = tabGravity;
            this.applyModeAndGravity();
        }
    }
    
    public void setTabIconTint(final ColorStateList tabIconTint) {
        if (this.tabIconTint != tabIconTint) {
            this.tabIconTint = tabIconTint;
            this.updateAllTabs();
        }
    }
    
    public void setTabIconTintResource(final int n) {
        this.setTabIconTint(AppCompatResources.getColorStateList(this.getContext(), n));
    }
    
    public void setTabIndicatorFullWidth(final boolean tabIndicatorFullWidth) {
        this.tabIndicatorFullWidth = tabIndicatorFullWidth;
        ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
    }
    
    public void setTabMode(final int mode) {
        if (mode != this.mode) {
            this.mode = mode;
            this.applyModeAndGravity();
        }
    }
    
    public void setTabRippleColor(final ColorStateList tabRippleColorStateList) {
        if (this.tabRippleColorStateList != tabRippleColorStateList) {
            this.tabRippleColorStateList = tabRippleColorStateList;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
                final View child = this.slidingTabIndicator.getChildAt(i);
                if (child instanceof TabView) {
                    ((TabView)child).updateBackgroundDrawable(this.getContext());
                }
            }
        }
    }
    
    public void setTabRippleColorResource(final int n) {
        this.setTabRippleColor(AppCompatResources.getColorStateList(this.getContext(), n));
    }
    
    public void setTabTextColors(final ColorStateList tabTextColors) {
        if (this.tabTextColors != tabTextColors) {
            this.tabTextColors = tabTextColors;
            this.updateAllTabs();
        }
    }
    
    @Deprecated
    public void setTabsFromPagerAdapter(final PagerAdapter pagerAdapter) {
        this.setPagerAdapter(pagerAdapter, false);
    }
    
    public void setUnboundedRipple(final boolean unboundedRipple) {
        if (this.unboundedRipple != unboundedRipple) {
            this.unboundedRipple = unboundedRipple;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
                final View child = this.slidingTabIndicator.getChildAt(i);
                if (child instanceof TabView) {
                    ((TabView)child).updateBackgroundDrawable(this.getContext());
                }
            }
        }
    }
    
    public void setUnboundedRippleResource(final int n) {
        this.setUnboundedRipple(this.getResources().getBoolean(n));
    }
    
    public void setupWithViewPager(final ViewPager viewPager) {
        this.setupWithViewPager(viewPager, true);
    }
    
    public void setupWithViewPager(final ViewPager viewPager, final boolean b) {
        this.setupWithViewPager(viewPager, b, false);
    }
    
    public boolean shouldDelayChildPressedState() {
        return this.getTabScrollRange() > 0;
    }
    
    void updateTabViews(final boolean b) {
        for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
            final View child = this.slidingTabIndicator.getChildAt(i);
            child.setMinimumWidth(this.getTabMinWidth());
            this.updateTabViewLayoutParams((LinearLayout$LayoutParams)child.getLayoutParams());
            if (b) {
                child.requestLayout();
            }
        }
    }
    
    private class AdapterChangeListener implements OnAdapterChangeListener
    {
        private boolean autoRefresh;
        
        AdapterChangeListener() {
        }
        
        @Override
        public void onAdapterChanged(final ViewPager viewPager, final PagerAdapter pagerAdapter, final PagerAdapter pagerAdapter2) {
            if (TabLayout.this.viewPager == viewPager) {
                TabLayout.this.setPagerAdapter(pagerAdapter2, this.autoRefresh);
            }
        }
        
        void setAutoRefresh(final boolean autoRefresh) {
            this.autoRefresh = autoRefresh;
        }
    }
    
    public interface BaseOnTabSelectedListener<T extends Tab>
    {
        void onTabReselected(final T p0);
        
        void onTabSelected(final T p0);
        
        void onTabUnselected(final T p0);
    }
    
    public interface OnTabSelectedListener extends BaseOnTabSelectedListener
    {
    }
    
    private class PagerAdapterObserver extends DataSetObserver
    {
        PagerAdapterObserver() {
        }
        
        public void onChanged() {
            TabLayout.this.populateFromPagerAdapter();
        }
        
        public void onInvalidated() {
            TabLayout.this.populateFromPagerAdapter();
        }
    }
    
    private class SlidingTabIndicator extends LinearLayout
    {
        private final GradientDrawable defaultSelectionIndicator;
        private ValueAnimator indicatorAnimator;
        private int indicatorLeft;
        private int indicatorRight;
        private int layoutDirection;
        private int selectedIndicatorHeight;
        private final Paint selectedIndicatorPaint;
        int selectedPosition;
        float selectionOffset;
        
        SlidingTabIndicator(final Context context) {
            super(context);
            this.selectedPosition = -1;
            this.layoutDirection = -1;
            this.indicatorLeft = -1;
            this.indicatorRight = -1;
            this.setWillNotDraw(false);
            this.selectedIndicatorPaint = new Paint();
            this.defaultSelectionIndicator = new GradientDrawable();
        }
        
        private void calculateTabViewContentBounds(final TabView tabView, final RectF rectF) {
            int n;
            if ((n = tabView.getContentWidth()) < TabLayout.this.dpToPx(24)) {
                n = TabLayout.this.dpToPx(24);
            }
            final int n2 = (tabView.getLeft() + tabView.getRight()) / 2;
            final int n3 = n / 2;
            rectF.set((float)(n2 - n3), 0.0f, (float)(n2 + n3), 0.0f);
        }
        
        private void updateIndicatorPosition() {
            final View child = this.getChildAt(this.selectedPosition);
            int n3;
            int n4;
            if (child != null && child.getWidth() > 0) {
                final int left = child.getLeft();
                final int right = child.getRight();
                int n = left;
                int n2 = right;
                if (!TabLayout.this.tabIndicatorFullWidth) {
                    n = left;
                    n2 = right;
                    if (child instanceof TabView) {
                        this.calculateTabViewContentBounds((TabView)child, TabLayout.this.tabViewContentBounds);
                        n = (int)TabLayout.this.tabViewContentBounds.left;
                        n2 = (int)TabLayout.this.tabViewContentBounds.right;
                    }
                }
                n3 = n;
                n4 = n2;
                if (this.selectionOffset > 0.0f) {
                    n3 = n;
                    n4 = n2;
                    if (this.selectedPosition < this.getChildCount() - 1) {
                        final View child2 = this.getChildAt(this.selectedPosition + 1);
                        final int left2 = child2.getLeft();
                        final int right2 = child2.getRight();
                        int n5 = left2;
                        int n6 = right2;
                        if (!TabLayout.this.tabIndicatorFullWidth) {
                            n5 = left2;
                            n6 = right2;
                            if (child2 instanceof TabView) {
                                this.calculateTabViewContentBounds((TabView)child2, TabLayout.this.tabViewContentBounds);
                                n5 = (int)TabLayout.this.tabViewContentBounds.left;
                                n6 = (int)TabLayout.this.tabViewContentBounds.right;
                            }
                        }
                        n3 = (int)(this.selectionOffset * n5 + (1.0f - this.selectionOffset) * n);
                        n4 = (int)(this.selectionOffset * n6 + (1.0f - this.selectionOffset) * n2);
                    }
                }
            }
            else {
                n3 = -1;
                n4 = -1;
            }
            this.setIndicatorPosition(n3, n4);
        }
        
        void animateIndicatorToPosition(final int n, final int n2) {
            if (this.indicatorAnimator != null && this.indicatorAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }
            final View child = this.getChildAt(n);
            if (child == null) {
                this.updateIndicatorPosition();
                return;
            }
            final int left = child.getLeft();
            final int right = child.getRight();
            int n3 = left;
            int n4 = right;
            if (!TabLayout.this.tabIndicatorFullWidth) {
                n3 = left;
                n4 = right;
                if (child instanceof TabView) {
                    this.calculateTabViewContentBounds((TabView)child, TabLayout.this.tabViewContentBounds);
                    n3 = (int)TabLayout.this.tabViewContentBounds.left;
                    n4 = (int)TabLayout.this.tabViewContentBounds.right;
                }
            }
            final int indicatorLeft = this.indicatorLeft;
            final int indicatorRight = this.indicatorRight;
            if (indicatorLeft != n3 || indicatorRight != n4) {
                final ValueAnimator indicatorAnimator = new ValueAnimator();
                (this.indicatorAnimator = indicatorAnimator).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                indicatorAnimator.setDuration((long)n2);
                indicatorAnimator.setFloatValues(new float[] { 0.0f, 1.0f });
                indicatorAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                    public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                        final float animatedFraction = valueAnimator.getAnimatedFraction();
                        SlidingTabIndicator.this.setIndicatorPosition(AnimationUtils.lerp(indicatorLeft, n3, animatedFraction), AnimationUtils.lerp(indicatorRight, n4, animatedFraction));
                    }
                });
                indicatorAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        SlidingTabIndicator.this.selectedPosition = n;
                        SlidingTabIndicator.this.selectionOffset = 0.0f;
                    }
                });
                indicatorAnimator.start();
            }
        }
        
        boolean childrenNeedLayout() {
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                if (this.getChildAt(i).getWidth() <= 0) {
                    return true;
                }
            }
            return false;
        }
        
        public void draw(final Canvas canvas) {
            final Drawable tabSelectedIndicator = TabLayout.this.tabSelectedIndicator;
            final int n = 0;
            int n2;
            if (tabSelectedIndicator != null) {
                n2 = TabLayout.this.tabSelectedIndicator.getIntrinsicHeight();
            }
            else {
                n2 = 0;
            }
            if (this.selectedIndicatorHeight >= 0) {
                n2 = this.selectedIndicatorHeight;
            }
            int n3 = n2;
            int n4 = n;
            while (true) {
                switch (TabLayout.this.tabIndicatorGravity) {
                    default: {
                        n3 = 0;
                        n4 = n;
                    }
                    case 2: {
                        if (this.indicatorLeft >= 0 && this.indicatorRight > this.indicatorLeft) {
                            Object o;
                            if (TabLayout.this.tabSelectedIndicator != null) {
                                o = TabLayout.this.tabSelectedIndicator;
                            }
                            else {
                                o = this.defaultSelectionIndicator;
                            }
                            final Drawable wrap = DrawableCompat.wrap((Drawable)o);
                            wrap.setBounds(this.indicatorLeft, n4, this.indicatorRight, n3);
                            if (this.selectedIndicatorPaint != null) {
                                if (Build$VERSION.SDK_INT == 21) {
                                    wrap.setColorFilter(this.selectedIndicatorPaint.getColor(), PorterDuff$Mode.SRC_IN);
                                }
                                else {
                                    DrawableCompat.setTint(wrap, this.selectedIndicatorPaint.getColor());
                                }
                            }
                            wrap.draw(canvas);
                        }
                        super.draw(canvas);
                    }
                    case 3: {
                        n3 = this.getHeight();
                        n4 = n;
                        continue;
                    }
                    case 1: {
                        n4 = (this.getHeight() - n2) / 2;
                        n3 = (this.getHeight() + n2) / 2;
                        continue;
                    }
                    case 0: {
                        n4 = this.getHeight() - n2;
                        n3 = this.getHeight();
                        continue;
                    }
                }
                break;
            }
        }
        
        protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
            super.onLayout(b, n, n2, n3, n4);
            if (this.indicatorAnimator != null && this.indicatorAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
                this.animateIndicatorToPosition(this.selectedPosition, Math.round((1.0f - this.indicatorAnimator.getAnimatedFraction()) * this.indicatorAnimator.getDuration()));
            }
            else {
                this.updateIndicatorPosition();
            }
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(n, n2);
            if (View$MeasureSpec.getMode(n) != 1073741824) {
                return;
            }
            final int mode = TabLayout.this.mode;
            final int n3 = 1;
            if (mode == 1 && TabLayout.this.tabGravity == 1) {
                final int childCount = this.getChildCount();
                final int n4 = 0;
                int i = 0;
                int n5 = 0;
                while (i < childCount) {
                    final View child = this.getChildAt(i);
                    int max = n5;
                    if (child.getVisibility() == 0) {
                        max = Math.max(n5, child.getMeasuredWidth());
                    }
                    ++i;
                    n5 = max;
                }
                if (n5 <= 0) {
                    return;
                }
                int n6;
                if (n5 * childCount <= this.getMeasuredWidth() - TabLayout.this.dpToPx(16) * 2) {
                    n6 = 0;
                    for (int j = n4; j < childCount; ++j) {
                        final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)this.getChildAt(j).getLayoutParams();
                        if (linearLayout$LayoutParams.width != n5 || linearLayout$LayoutParams.weight != 0.0f) {
                            linearLayout$LayoutParams.width = n5;
                            linearLayout$LayoutParams.weight = 0.0f;
                            n6 = 1;
                        }
                    }
                }
                else {
                    TabLayout.this.tabGravity = 0;
                    TabLayout.this.updateTabViews(false);
                    n6 = n3;
                }
                if (n6 != 0) {
                    super.onMeasure(n, n2);
                }
            }
        }
        
        public void onRtlPropertiesChanged(final int layoutDirection) {
            super.onRtlPropertiesChanged(layoutDirection);
            if (Build$VERSION.SDK_INT < 23 && this.layoutDirection != layoutDirection) {
                this.requestLayout();
                this.layoutDirection = layoutDirection;
            }
        }
        
        void setIndicatorPosition(final int indicatorLeft, final int indicatorRight) {
            if (indicatorLeft != this.indicatorLeft || indicatorRight != this.indicatorRight) {
                this.indicatorLeft = indicatorLeft;
                this.indicatorRight = indicatorRight;
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }
        
        void setIndicatorPositionFromTabPosition(final int selectedPosition, final float selectionOffset) {
            if (this.indicatorAnimator != null && this.indicatorAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }
            this.selectedPosition = selectedPosition;
            this.selectionOffset = selectionOffset;
            this.updateIndicatorPosition();
        }
        
        void setSelectedIndicatorColor(final int color) {
            if (this.selectedIndicatorPaint.getColor() != color) {
                this.selectedIndicatorPaint.setColor(color);
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }
        
        void setSelectedIndicatorHeight(final int selectedIndicatorHeight) {
            if (this.selectedIndicatorHeight != selectedIndicatorHeight) {
                this.selectedIndicatorHeight = selectedIndicatorHeight;
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }
    }
    
    public static class Tab
    {
        private CharSequence contentDesc;
        private View customView;
        private Drawable icon;
        public TabLayout parent;
        private int position;
        private Object tag;
        private CharSequence text;
        public TabView view;
        
        public Tab() {
            this.position = -1;
        }
        
        public View getCustomView() {
            return this.customView;
        }
        
        public Drawable getIcon() {
            return this.icon;
        }
        
        public int getPosition() {
            return this.position;
        }
        
        public CharSequence getText() {
            return this.text;
        }
        
        public boolean isSelected() {
            if (this.parent != null) {
                return this.parent.getSelectedTabPosition() == this.position;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }
        
        void reset() {
            this.parent = null;
            this.view = null;
            this.tag = null;
            this.icon = null;
            this.text = null;
            this.contentDesc = null;
            this.position = -1;
            this.customView = null;
        }
        
        public void select() {
            if (this.parent != null) {
                this.parent.selectTab(this);
                return;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }
        
        public Tab setContentDescription(final CharSequence contentDesc) {
            this.contentDesc = contentDesc;
            this.updateView();
            return this;
        }
        
        public Tab setCustomView(final int n) {
            return this.setCustomView(LayoutInflater.from(this.view.getContext()).inflate(n, (ViewGroup)this.view, false));
        }
        
        public Tab setCustomView(final View customView) {
            this.customView = customView;
            this.updateView();
            return this;
        }
        
        public Tab setIcon(final Drawable icon) {
            this.icon = icon;
            this.updateView();
            return this;
        }
        
        void setPosition(final int position) {
            this.position = position;
        }
        
        public Tab setText(final CharSequence charSequence) {
            if (TextUtils.isEmpty(this.contentDesc) && !TextUtils.isEmpty(charSequence)) {
                this.view.setContentDescription(charSequence);
            }
            this.text = charSequence;
            this.updateView();
            return this;
        }
        
        void updateView() {
            if (this.view != null) {
                this.view.update();
            }
        }
    }
    
    public static class TabLayoutOnPageChangeListener implements OnPageChangeListener
    {
        private int previousScrollState;
        private int scrollState;
        private final WeakReference<TabLayout> tabLayoutRef;
        
        public TabLayoutOnPageChangeListener(final TabLayout referent) {
            this.tabLayoutRef = new WeakReference<TabLayout>(referent);
        }
        
        @Override
        public void onPageScrollStateChanged(final int scrollState) {
            this.previousScrollState = this.scrollState;
            this.scrollState = scrollState;
        }
        
        @Override
        public void onPageScrolled(final int n, final float n2, int scrollState) {
            final TabLayout tabLayout = this.tabLayoutRef.get();
            if (tabLayout != null) {
                scrollState = this.scrollState;
                boolean b = false;
                final boolean b2 = scrollState != 2 || this.previousScrollState == 1;
                if (this.scrollState != 2 || this.previousScrollState != 0) {
                    b = true;
                }
                tabLayout.setScrollPosition(n, n2, b2, b);
            }
        }
        
        @Override
        public void onPageSelected(final int n) {
            final TabLayout tabLayout = this.tabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != n && n < tabLayout.getTabCount()) {
                tabLayout.selectTab(tabLayout.getTabAt(n), this.scrollState == 0 || (this.scrollState == 2 && this.previousScrollState == 0));
            }
        }
        
        void reset() {
            this.scrollState = 0;
            this.previousScrollState = 0;
        }
    }
    
    class TabView extends LinearLayout
    {
        private Drawable baseBackgroundDrawable;
        private ImageView customIconView;
        private TextView customTextView;
        private View customView;
        private int defaultMaxLines;
        private ImageView iconView;
        private Tab tab;
        private TextView textView;
        
        public TabView(final Context context) {
            super(context);
            this.defaultMaxLines = 2;
            this.updateBackgroundDrawable(context);
            ViewCompat.setPaddingRelative((View)this, TabLayout.this.tabPaddingStart, TabLayout.this.tabPaddingTop, TabLayout.this.tabPaddingEnd, TabLayout.this.tabPaddingBottom);
            this.setGravity(17);
            this.setOrientation((int)((TabLayout.this.inlineLabel ^ true) ? 1 : 0));
            this.setClickable(true);
            ViewCompat.setPointerIcon((View)this, PointerIconCompat.getSystemIcon(this.getContext(), 1002));
        }
        
        private float approximateLineWidth(final Layout layout, final int n, final float n2) {
            return layout.getLineWidth(n) * (n2 / layout.getPaint().getTextSize());
        }
        
        private void drawBackground(final Canvas canvas) {
            if (this.baseBackgroundDrawable != null) {
                this.baseBackgroundDrawable.setBounds(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
                this.baseBackgroundDrawable.draw(canvas);
            }
        }
        
        private int getContentWidth() {
            final View[] array = new View[3];
            final TextView textView = this.textView;
            int i = 0;
            array[0] = (View)textView;
            array[1] = (View)this.iconView;
            array[2] = this.customView;
            final int length = array.length;
            int a = 0;
            int a2 = 0;
            int n = 0;
            while (i < length) {
                final View view = array[i];
                int n2 = a;
                int n3 = a2;
                int n4 = n;
                if (view != null) {
                    n2 = a;
                    n3 = a2;
                    n4 = n;
                    if (view.getVisibility() == 0) {
                        int n5;
                        if (n != 0) {
                            n5 = Math.min(a2, view.getLeft());
                        }
                        else {
                            n5 = view.getLeft();
                        }
                        int n6;
                        if (n != 0) {
                            n6 = Math.max(a, view.getRight());
                        }
                        else {
                            n6 = view.getRight();
                        }
                        n4 = 1;
                        n3 = n5;
                        n2 = n6;
                    }
                }
                ++i;
                a = n2;
                a2 = n3;
                n = n4;
            }
            return a - a2;
        }
        
        private void updateBackgroundDrawable(final Context context) {
            if (TabLayout.this.tabBackgroundResId != 0) {
                this.baseBackgroundDrawable = AppCompatResources.getDrawable(context, TabLayout.this.tabBackgroundResId);
                if (this.baseBackgroundDrawable != null && this.baseBackgroundDrawable.isStateful()) {
                    this.baseBackgroundDrawable.setState(this.getDrawableState());
                }
            }
            else {
                this.baseBackgroundDrawable = null;
            }
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(0);
            Object o = gradientDrawable;
            if (TabLayout.this.tabRippleColorStateList != null) {
                GradientDrawable gradientDrawable2 = new GradientDrawable();
                gradientDrawable2.setCornerRadius(1.0E-5f);
                gradientDrawable2.setColor(-1);
                final ColorStateList convertToRippleDrawableColor = RippleUtils.convertToRippleDrawableColor(TabLayout.this.tabRippleColorStateList);
                if (Build$VERSION.SDK_INT >= 21) {
                    if (TabLayout.this.unboundedRipple) {
                        gradientDrawable = null;
                    }
                    if (TabLayout.this.unboundedRipple) {
                        gradientDrawable2 = null;
                    }
                    o = new RippleDrawable(convertToRippleDrawableColor, (Drawable)gradientDrawable, (Drawable)gradientDrawable2);
                }
                else {
                    final Drawable wrap = DrawableCompat.wrap((Drawable)gradientDrawable2);
                    DrawableCompat.setTintList(wrap, convertToRippleDrawableColor);
                    o = new LayerDrawable(new Drawable[] { (Drawable)gradientDrawable, wrap });
                }
            }
            ViewCompat.setBackground((View)this, (Drawable)o);
            TabLayout.this.invalidate();
        }
        
        private void updateTextAndIcon(final TextView textView, final ImageView imageView) {
            Drawable mutate;
            if (this.tab != null && this.tab.getIcon() != null) {
                mutate = DrawableCompat.wrap(this.tab.getIcon()).mutate();
            }
            else {
                mutate = null;
            }
            CharSequence text;
            if (this.tab != null) {
                text = this.tab.getText();
            }
            else {
                text = null;
            }
            if (imageView != null) {
                if (mutate != null) {
                    imageView.setImageDrawable(mutate);
                    imageView.setVisibility(0);
                    this.setVisibility(0);
                }
                else {
                    imageView.setVisibility(8);
                    imageView.setImageDrawable((Drawable)null);
                }
            }
            final boolean b = TextUtils.isEmpty(text) ^ true;
            if (textView != null) {
                if (b) {
                    textView.setText(text);
                    textView.setVisibility(0);
                    this.setVisibility(0);
                }
                else {
                    textView.setVisibility(8);
                    textView.setText((CharSequence)null);
                }
            }
            if (imageView != null) {
                final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)imageView.getLayoutParams();
                int dpToPx;
                if (b && imageView.getVisibility() == 0) {
                    dpToPx = TabLayout.this.dpToPx(8);
                }
                else {
                    dpToPx = 0;
                }
                if (TabLayout.this.inlineLabel) {
                    if (dpToPx != MarginLayoutParamsCompat.getMarginEnd(viewGroup$MarginLayoutParams)) {
                        MarginLayoutParamsCompat.setMarginEnd(viewGroup$MarginLayoutParams, dpToPx);
                        viewGroup$MarginLayoutParams.bottomMargin = 0;
                        imageView.setLayoutParams((ViewGroup$LayoutParams)viewGroup$MarginLayoutParams);
                        imageView.requestLayout();
                    }
                }
                else if (dpToPx != viewGroup$MarginLayoutParams.bottomMargin) {
                    viewGroup$MarginLayoutParams.bottomMargin = dpToPx;
                    MarginLayoutParamsCompat.setMarginEnd(viewGroup$MarginLayoutParams, 0);
                    imageView.setLayoutParams((ViewGroup$LayoutParams)viewGroup$MarginLayoutParams);
                    imageView.requestLayout();
                }
            }
            CharSequence access$100;
            if (this.tab != null) {
                access$100 = this.tab.contentDesc;
            }
            else {
                access$100 = null;
            }
            if (b) {
                access$100 = null;
            }
            TooltipCompat.setTooltipText((View)this, access$100);
        }
        
        protected void drawableStateChanged() {
            super.drawableStateChanged();
            final int[] drawableState = this.getDrawableState();
            final Drawable baseBackgroundDrawable = this.baseBackgroundDrawable;
            int n = 0;
            if (baseBackgroundDrawable != null) {
                n = n;
                if (this.baseBackgroundDrawable.isStateful()) {
                    n = ((false | this.baseBackgroundDrawable.setState(drawableState)) ? 1 : 0);
                }
            }
            if (n != 0) {
                this.invalidate();
                TabLayout.this.invalidate();
            }
        }
        
        public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }
        
        @TargetApi(14)
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }
        
        public void onMeasure(int maxLines, final int n) {
            final int size = View$MeasureSpec.getSize(maxLines);
            final int mode = View$MeasureSpec.getMode(maxLines);
            final int tabMaxWidth = TabLayout.this.getTabMaxWidth();
            int measureSpec = maxLines;
            Label_0057: {
                if (tabMaxWidth > 0) {
                    if (mode != 0) {
                        measureSpec = maxLines;
                        if (size <= tabMaxWidth) {
                            break Label_0057;
                        }
                    }
                    measureSpec = View$MeasureSpec.makeMeasureSpec(TabLayout.this.tabMaxWidth, Integer.MIN_VALUE);
                }
            }
            super.onMeasure(measureSpec, n);
            if (this.textView != null) {
                final float tabTextSize = TabLayout.this.tabTextSize;
                final int defaultMaxLines = this.defaultMaxLines;
                final ImageView iconView = this.iconView;
                final boolean b = true;
                float tabTextMultiLineSize;
                if (iconView != null && this.iconView.getVisibility() == 0) {
                    maxLines = 1;
                    tabTextMultiLineSize = tabTextSize;
                }
                else {
                    tabTextMultiLineSize = tabTextSize;
                    maxLines = defaultMaxLines;
                    if (this.textView != null) {
                        tabTextMultiLineSize = tabTextSize;
                        maxLines = defaultMaxLines;
                        if (this.textView.getLineCount() > 1) {
                            tabTextMultiLineSize = TabLayout.this.tabTextMultiLineSize;
                            maxLines = defaultMaxLines;
                        }
                    }
                }
                final float textSize = this.textView.getTextSize();
                final int lineCount = this.textView.getLineCount();
                final int maxLines2 = TextViewCompat.getMaxLines(this.textView);
                final float n2 = fcmpl(tabTextMultiLineSize, textSize);
                if (n2 != 0 || (maxLines2 >= 0 && maxLines != maxLines2)) {
                    int n3 = b ? 1 : 0;
                    Label_0291: {
                        if (TabLayout.this.mode == 1) {
                            n3 = (b ? 1 : 0);
                            if (n2 > 0) {
                                n3 = (b ? 1 : 0);
                                if (lineCount == 1) {
                                    final Layout layout = this.textView.getLayout();
                                    if (layout != null) {
                                        n3 = (b ? 1 : 0);
                                        if (this.approximateLineWidth(layout, 0, tabTextMultiLineSize) <= this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight()) {
                                            break Label_0291;
                                        }
                                    }
                                    n3 = 0;
                                }
                            }
                        }
                    }
                    if (n3 != 0) {
                        this.textView.setTextSize(0, tabTextMultiLineSize);
                        this.textView.setMaxLines(maxLines);
                        super.onMeasure(measureSpec, n);
                    }
                }
            }
        }
        
        public boolean performClick() {
            final boolean performClick = super.performClick();
            if (this.tab != null) {
                if (!performClick) {
                    this.playSoundEffect(0);
                }
                this.tab.select();
                return true;
            }
            return performClick;
        }
        
        void reset() {
            this.setTab(null);
            this.setSelected(false);
        }
        
        public void setSelected(final boolean b) {
            final boolean b2 = this.isSelected() != b;
            super.setSelected(b);
            if (b2 && b && Build$VERSION.SDK_INT < 16) {
                this.sendAccessibilityEvent(4);
            }
            if (this.textView != null) {
                this.textView.setSelected(b);
            }
            if (this.iconView != null) {
                this.iconView.setSelected(b);
            }
            if (this.customView != null) {
                this.customView.setSelected(b);
            }
        }
        
        void setTab(final Tab tab) {
            if (tab != this.tab) {
                this.tab = tab;
                this.update();
            }
        }
        
        final void update() {
            final Tab tab = this.tab;
            final Drawable drawable = null;
            View customView;
            if (tab != null) {
                customView = tab.getCustomView();
            }
            else {
                customView = null;
            }
            if (customView != null) {
                final ViewParent parent = customView.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((ViewGroup)parent).removeView(customView);
                    }
                    this.addView(customView);
                }
                this.customView = customView;
                if (this.textView != null) {
                    this.textView.setVisibility(8);
                }
                if (this.iconView != null) {
                    this.iconView.setVisibility(8);
                    this.iconView.setImageDrawable((Drawable)null);
                }
                this.customTextView = (TextView)customView.findViewById(16908308);
                if (this.customTextView != null) {
                    this.defaultMaxLines = TextViewCompat.getMaxLines(this.customTextView);
                }
                this.customIconView = (ImageView)customView.findViewById(16908294);
            }
            else {
                if (this.customView != null) {
                    this.removeView(this.customView);
                    this.customView = null;
                }
                this.customTextView = null;
                this.customIconView = null;
            }
            final View customView2 = this.customView;
            final boolean b = false;
            if (customView2 == null) {
                if (this.iconView == null) {
                    final ImageView iconView = (ImageView)LayoutInflater.from(this.getContext()).inflate(R.layout.design_layout_tab_icon, (ViewGroup)this, false);
                    this.addView((View)iconView, 0);
                    this.iconView = iconView;
                }
                Drawable mutate = drawable;
                if (tab != null) {
                    mutate = drawable;
                    if (tab.getIcon() != null) {
                        mutate = DrawableCompat.wrap(tab.getIcon()).mutate();
                    }
                }
                if (mutate != null) {
                    DrawableCompat.setTintList(mutate, TabLayout.this.tabIconTint);
                    if (TabLayout.this.tabIconTintMode != null) {
                        DrawableCompat.setTintMode(mutate, TabLayout.this.tabIconTintMode);
                    }
                }
                if (this.textView == null) {
                    final TextView textView = (TextView)LayoutInflater.from(this.getContext()).inflate(R.layout.design_layout_tab_text, (ViewGroup)this, false);
                    this.addView((View)textView);
                    this.textView = textView;
                    this.defaultMaxLines = TextViewCompat.getMaxLines(this.textView);
                }
                TextViewCompat.setTextAppearance(this.textView, TabLayout.this.tabTextAppearance);
                if (TabLayout.this.tabTextColors != null) {
                    this.textView.setTextColor(TabLayout.this.tabTextColors);
                }
                this.updateTextAndIcon(this.textView, this.iconView);
            }
            else if (this.customTextView != null || this.customIconView != null) {
                this.updateTextAndIcon(this.customTextView, this.customIconView);
            }
            if (tab != null && !TextUtils.isEmpty(tab.contentDesc)) {
                this.setContentDescription(tab.contentDesc);
            }
            boolean selected = b;
            if (tab != null) {
                selected = b;
                if (tab.isSelected()) {
                    selected = true;
                }
            }
            this.setSelected(selected);
        }
        
        final void updateOrientation() {
            this.setOrientation((int)((TabLayout.this.inlineLabel ^ true) ? 1 : 0));
            if (this.customTextView == null && this.customIconView == null) {
                this.updateTextAndIcon(this.textView, this.iconView);
            }
            else {
                this.updateTextAndIcon(this.customTextView, this.customIconView);
            }
        }
    }
    
    public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener
    {
        private final ViewPager viewPager;
        
        public ViewPagerOnTabSelectedListener(final ViewPager viewPager) {
            this.viewPager = viewPager;
        }
        
        @Override
        public void onTabReselected(final Tab tab) {
        }
        
        @Override
        public void onTabSelected(final Tab tab) {
            this.viewPager.setCurrentItem(tab.getPosition());
        }
        
        @Override
        public void onTabUnselected(final Tab tab) {
        }
    }
}
