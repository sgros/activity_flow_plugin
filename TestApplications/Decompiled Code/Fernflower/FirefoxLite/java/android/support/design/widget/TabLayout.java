package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.support.design.R;
import android.support.design.animation.AnimationUtils;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.internal.ViewUtils;
import android.support.design.resources.MaterialResources;
import android.support.design.ripple.RippleUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.Pools;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.TooltipCompat;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@ViewPager.DecorView
public class TabLayout extends HorizontalScrollView {
   private static final Pools.Pool tabPool = new Pools.SynchronizedPool(16);
   private TabLayout.AdapterChangeListener adapterChangeListener;
   private int contentInsetStart;
   private TabLayout.BaseOnTabSelectedListener currentVpSelectedListener;
   boolean inlineLabel;
   int mode;
   private TabLayout.TabLayoutOnPageChangeListener pageChangeListener;
   private PagerAdapter pagerAdapter;
   private DataSetObserver pagerAdapterObserver;
   private final int requestedTabMaxWidth;
   private final int requestedTabMinWidth;
   private ValueAnimator scrollAnimator;
   private final int scrollableTabMinWidth;
   private TabLayout.BaseOnTabSelectedListener selectedListener;
   private final ArrayList selectedListeners;
   private TabLayout.Tab selectedTab;
   private boolean setupViewPagerImplicitly;
   private final TabLayout.SlidingTabIndicator slidingTabIndicator;
   final int tabBackgroundResId;
   int tabGravity;
   ColorStateList tabIconTint;
   Mode tabIconTintMode;
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
   private final Pools.Pool tabViewPool;
   private final ArrayList tabs;
   boolean unboundedRipple;
   ViewPager viewPager;

   public TabLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public TabLayout(Context var1, AttributeSet var2) {
      this(var1, var2, R.attr.tabStyle);
   }

   public TabLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.tabs = new ArrayList();
      this.tabViewContentBounds = new RectF();
      this.tabMaxWidth = Integer.MAX_VALUE;
      this.selectedListeners = new ArrayList();
      this.tabViewPool = new Pools.SimplePool(12);
      this.setHorizontalScrollBarEnabled(false);
      this.slidingTabIndicator = new TabLayout.SlidingTabIndicator(var1);
      super.addView(this.slidingTabIndicator, 0, new LayoutParams(-2, -1));
      TypedArray var4 = ThemeEnforcement.obtainStyledAttributes(var1, var2, R.styleable.TabLayout, var3, R.style.Widget_Design_TabLayout, R.styleable.TabLayout_tabTextAppearance);
      this.slidingTabIndicator.setSelectedIndicatorHeight(var4.getDimensionPixelSize(R.styleable.TabLayout_tabIndicatorHeight, -1));
      this.slidingTabIndicator.setSelectedIndicatorColor(var4.getColor(R.styleable.TabLayout_tabIndicatorColor, 0));
      this.setSelectedTabIndicator(MaterialResources.getDrawable(var1, var4, R.styleable.TabLayout_tabIndicator));
      this.setSelectedTabIndicatorGravity(var4.getInt(R.styleable.TabLayout_tabIndicatorGravity, 0));
      this.setTabIndicatorFullWidth(var4.getBoolean(R.styleable.TabLayout_tabIndicatorFullWidth, true));
      var3 = var4.getDimensionPixelSize(R.styleable.TabLayout_tabPadding, 0);
      this.tabPaddingBottom = var3;
      this.tabPaddingEnd = var3;
      this.tabPaddingTop = var3;
      this.tabPaddingStart = var3;
      this.tabPaddingStart = var4.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingStart, this.tabPaddingStart);
      this.tabPaddingTop = var4.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingTop, this.tabPaddingTop);
      this.tabPaddingEnd = var4.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingEnd, this.tabPaddingEnd);
      this.tabPaddingBottom = var4.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingBottom, this.tabPaddingBottom);
      this.tabTextAppearance = var4.getResourceId(R.styleable.TabLayout_tabTextAppearance, R.style.TextAppearance_Design_Tab);
      TypedArray var8 = var1.obtainStyledAttributes(this.tabTextAppearance, android.support.v7.appcompat.R.styleable.TextAppearance);

      try {
         this.tabTextSize = (float)var8.getDimensionPixelSize(android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize, 0);
         this.tabTextColors = MaterialResources.getColorStateList(var1, var8, android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor);
      } finally {
         var8.recycle();
      }

      if (var4.hasValue(R.styleable.TabLayout_tabTextColor)) {
         this.tabTextColors = MaterialResources.getColorStateList(var1, var4, R.styleable.TabLayout_tabTextColor);
      }

      if (var4.hasValue(R.styleable.TabLayout_tabSelectedTextColor)) {
         var3 = var4.getColor(R.styleable.TabLayout_tabSelectedTextColor, 0);
         this.tabTextColors = createColorStateList(this.tabTextColors.getDefaultColor(), var3);
      }

      this.tabIconTint = MaterialResources.getColorStateList(var1, var4, R.styleable.TabLayout_tabIconTint);
      this.tabIconTintMode = ViewUtils.parseTintMode(var4.getInt(R.styleable.TabLayout_tabIconTintMode, -1), (Mode)null);
      this.tabRippleColorStateList = MaterialResources.getColorStateList(var1, var4, R.styleable.TabLayout_tabRippleColor);
      this.tabIndicatorAnimationDuration = var4.getInt(R.styleable.TabLayout_tabIndicatorAnimationDuration, 300);
      this.requestedTabMinWidth = var4.getDimensionPixelSize(R.styleable.TabLayout_tabMinWidth, -1);
      this.requestedTabMaxWidth = var4.getDimensionPixelSize(R.styleable.TabLayout_tabMaxWidth, -1);
      this.tabBackgroundResId = var4.getResourceId(R.styleable.TabLayout_tabBackground, 0);
      this.contentInsetStart = var4.getDimensionPixelSize(R.styleable.TabLayout_tabContentStart, 0);
      this.mode = var4.getInt(R.styleable.TabLayout_tabMode, 1);
      this.tabGravity = var4.getInt(R.styleable.TabLayout_tabGravity, 0);
      this.inlineLabel = var4.getBoolean(R.styleable.TabLayout_tabInlineLabel, false);
      this.unboundedRipple = var4.getBoolean(R.styleable.TabLayout_tabUnboundedRipple, false);
      var4.recycle();
      Resources var7 = this.getResources();
      this.tabTextMultiLineSize = (float)var7.getDimensionPixelSize(R.dimen.design_tab_text_size_2line);
      this.scrollableTabMinWidth = var7.getDimensionPixelSize(R.dimen.design_tab_scrollable_min_width);
      this.applyModeAndGravity();
   }

   private void addTabFromItemView(TabItem var1) {
      TabLayout.Tab var2 = this.newTab();
      if (var1.text != null) {
         var2.setText(var1.text);
      }

      if (var1.icon != null) {
         var2.setIcon(var1.icon);
      }

      if (var1.customLayout != 0) {
         var2.setCustomView(var1.customLayout);
      }

      if (!TextUtils.isEmpty(var1.getContentDescription())) {
         var2.setContentDescription(var1.getContentDescription());
      }

      this.addTab(var2);
   }

   private void addTabView(TabLayout.Tab var1) {
      TabLayout.TabView var2 = var1.view;
      this.slidingTabIndicator.addView(var2, var1.getPosition(), this.createLayoutParamsForTabs());
   }

   private void addViewInternal(View var1) {
      if (var1 instanceof TabItem) {
         this.addTabFromItemView((TabItem)var1);
      } else {
         throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
      }
   }

   private void animateToTab(int var1) {
      if (var1 != -1) {
         if (this.getWindowToken() != null && ViewCompat.isLaidOut(this) && !this.slidingTabIndicator.childrenNeedLayout()) {
            int var2 = this.getScrollX();
            int var3 = this.calculateScrollXForTab(var1, 0.0F);
            if (var2 != var3) {
               this.ensureScrollAnimator();
               this.scrollAnimator.setIntValues(new int[]{var2, var3});
               this.scrollAnimator.start();
            }

            this.slidingTabIndicator.animateIndicatorToPosition(var1, this.tabIndicatorAnimationDuration);
         } else {
            this.setScrollPosition(var1, 0.0F, true);
         }
      }
   }

   private void applyModeAndGravity() {
      int var1;
      if (this.mode == 0) {
         var1 = Math.max(0, this.contentInsetStart - this.tabPaddingStart);
      } else {
         var1 = 0;
      }

      ViewCompat.setPaddingRelative(this.slidingTabIndicator, var1, 0, 0, 0);
      switch(this.mode) {
      case 0:
         this.slidingTabIndicator.setGravity(8388611);
         break;
      case 1:
         this.slidingTabIndicator.setGravity(1);
      }

      this.updateTabViews(true);
   }

   private int calculateScrollXForTab(int var1, float var2) {
      int var3 = this.mode;
      int var4 = 0;
      if (var3 == 0) {
         View var5 = this.slidingTabIndicator.getChildAt(var1);
         ++var1;
         View var6;
         if (var1 < this.slidingTabIndicator.getChildCount()) {
            var6 = this.slidingTabIndicator.getChildAt(var1);
         } else {
            var6 = null;
         }

         if (var5 != null) {
            var1 = var5.getWidth();
         } else {
            var1 = 0;
         }

         if (var6 != null) {
            var4 = var6.getWidth();
         }

         var3 = var5.getLeft() + var1 / 2 - this.getWidth() / 2;
         var1 = (int)((float)(var1 + var4) * 0.5F * var2);
         if (ViewCompat.getLayoutDirection(this) == 0) {
            var1 += var3;
         } else {
            var1 = var3 - var1;
         }

         return var1;
      } else {
         return 0;
      }
   }

   private void configureTab(TabLayout.Tab var1, int var2) {
      var1.setPosition(var2);
      this.tabs.add(var2, var1);
      int var3 = this.tabs.size();

      while(true) {
         ++var2;
         if (var2 >= var3) {
            return;
         }

         ((TabLayout.Tab)this.tabs.get(var2)).setPosition(var2);
      }
   }

   private static ColorStateList createColorStateList(int var0, int var1) {
      return new ColorStateList(new int[][]{SELECTED_STATE_SET, EMPTY_STATE_SET}, new int[]{var1, var0});
   }

   private android.widget.LinearLayout.LayoutParams createLayoutParamsForTabs() {
      android.widget.LinearLayout.LayoutParams var1 = new android.widget.LinearLayout.LayoutParams(-2, -1);
      this.updateTabViewLayoutParams(var1);
      return var1;
   }

   private TabLayout.TabView createTabView(TabLayout.Tab var1) {
      TabLayout.TabView var2;
      if (this.tabViewPool != null) {
         var2 = (TabLayout.TabView)this.tabViewPool.acquire();
      } else {
         var2 = null;
      }

      TabLayout.TabView var3 = var2;
      if (var2 == null) {
         var3 = new TabLayout.TabView(this.getContext());
      }

      var3.setTab(var1);
      var3.setFocusable(true);
      var3.setMinimumWidth(this.getTabMinWidth());
      if (TextUtils.isEmpty(var1.contentDesc)) {
         var3.setContentDescription(var1.text);
      } else {
         var3.setContentDescription(var1.contentDesc);
      }

      return var3;
   }

   private void dispatchTabReselected(TabLayout.Tab var1) {
      for(int var2 = this.selectedListeners.size() - 1; var2 >= 0; --var2) {
         ((TabLayout.BaseOnTabSelectedListener)this.selectedListeners.get(var2)).onTabReselected(var1);
      }

   }

   private void dispatchTabSelected(TabLayout.Tab var1) {
      for(int var2 = this.selectedListeners.size() - 1; var2 >= 0; --var2) {
         ((TabLayout.BaseOnTabSelectedListener)this.selectedListeners.get(var2)).onTabSelected(var1);
      }

   }

   private void dispatchTabUnselected(TabLayout.Tab var1) {
      for(int var2 = this.selectedListeners.size() - 1; var2 >= 0; --var2) {
         ((TabLayout.BaseOnTabSelectedListener)this.selectedListeners.get(var2)).onTabUnselected(var1);
      }

   }

   private void ensureScrollAnimator() {
      if (this.scrollAnimator == null) {
         this.scrollAnimator = new ValueAnimator();
         this.scrollAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
         this.scrollAnimator.setDuration((long)this.tabIndicatorAnimationDuration);
         this.scrollAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator var1) {
               TabLayout.this.scrollTo((Integer)var1.getAnimatedValue(), 0);
            }
         });
      }

   }

   private int getDefaultHeight() {
      int var1 = this.tabs.size();
      boolean var2 = false;
      int var3 = 0;

      boolean var4;
      while(true) {
         var4 = var2;
         if (var3 >= var1) {
            break;
         }

         TabLayout.Tab var5 = (TabLayout.Tab)this.tabs.get(var3);
         if (var5 != null && var5.getIcon() != null && !TextUtils.isEmpty(var5.getText())) {
            var4 = true;
            break;
         }

         ++var3;
      }

      byte var6;
      if (var4 && !this.inlineLabel) {
         var6 = 72;
      } else {
         var6 = 48;
      }

      return var6;
   }

   private int getTabMinWidth() {
      if (this.requestedTabMinWidth != -1) {
         return this.requestedTabMinWidth;
      } else {
         int var1;
         if (this.mode == 0) {
            var1 = this.scrollableTabMinWidth;
         } else {
            var1 = 0;
         }

         return var1;
      }
   }

   private int getTabScrollRange() {
      return Math.max(0, this.slidingTabIndicator.getWidth() - this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
   }

   private void removeTabViewAt(int var1) {
      TabLayout.TabView var2 = (TabLayout.TabView)this.slidingTabIndicator.getChildAt(var1);
      this.slidingTabIndicator.removeViewAt(var1);
      if (var2 != null) {
         var2.reset();
         this.tabViewPool.release(var2);
      }

      this.requestLayout();
   }

   private void setSelectedTabView(int var1) {
      int var2 = this.slidingTabIndicator.getChildCount();
      if (var1 < var2) {
         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.slidingTabIndicator.getChildAt(var3);
            boolean var5 = true;
            boolean var6;
            if (var3 == var1) {
               var6 = true;
            } else {
               var6 = false;
            }

            var4.setSelected(var6);
            if (var3 == var1) {
               var6 = var5;
            } else {
               var6 = false;
            }

            var4.setActivated(var6);
         }
      }

   }

   private void setupWithViewPager(ViewPager var1, boolean var2, boolean var3) {
      if (this.viewPager != null) {
         if (this.pageChangeListener != null) {
            this.viewPager.removeOnPageChangeListener(this.pageChangeListener);
         }

         if (this.adapterChangeListener != null) {
            this.viewPager.removeOnAdapterChangeListener(this.adapterChangeListener);
         }
      }

      if (this.currentVpSelectedListener != null) {
         this.removeOnTabSelectedListener(this.currentVpSelectedListener);
         this.currentVpSelectedListener = null;
      }

      if (var1 != null) {
         this.viewPager = var1;
         if (this.pageChangeListener == null) {
            this.pageChangeListener = new TabLayout.TabLayoutOnPageChangeListener(this);
         }

         this.pageChangeListener.reset();
         var1.addOnPageChangeListener(this.pageChangeListener);
         this.currentVpSelectedListener = new TabLayout.ViewPagerOnTabSelectedListener(var1);
         this.addOnTabSelectedListener(this.currentVpSelectedListener);
         PagerAdapter var4 = var1.getAdapter();
         if (var4 != null) {
            this.setPagerAdapter(var4, var2);
         }

         if (this.adapterChangeListener == null) {
            this.adapterChangeListener = new TabLayout.AdapterChangeListener();
         }

         this.adapterChangeListener.setAutoRefresh(var2);
         var1.addOnAdapterChangeListener(this.adapterChangeListener);
         this.setScrollPosition(var1.getCurrentItem(), 0.0F, true);
      } else {
         this.viewPager = null;
         this.setPagerAdapter((PagerAdapter)null, false);
      }

      this.setupViewPagerImplicitly = var3;
   }

   private void updateAllTabs() {
      int var1 = this.tabs.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         ((TabLayout.Tab)this.tabs.get(var2)).updateView();
      }

   }

   private void updateTabViewLayoutParams(android.widget.LinearLayout.LayoutParams var1) {
      if (this.mode == 1 && this.tabGravity == 0) {
         var1.width = 0;
         var1.weight = 1.0F;
      } else {
         var1.width = -2;
         var1.weight = 0.0F;
      }

   }

   public void addOnTabSelectedListener(TabLayout.BaseOnTabSelectedListener var1) {
      if (!this.selectedListeners.contains(var1)) {
         this.selectedListeners.add(var1);
      }

   }

   public void addTab(TabLayout.Tab var1) {
      this.addTab(var1, this.tabs.isEmpty());
   }

   public void addTab(TabLayout.Tab var1, int var2, boolean var3) {
      if (var1.parent == this) {
         this.configureTab(var1, var2);
         this.addTabView(var1);
         if (var3) {
            var1.select();
         }

      } else {
         throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
      }
   }

   public void addTab(TabLayout.Tab var1, boolean var2) {
      this.addTab(var1, this.tabs.size(), var2);
   }

   public void addView(View var1) {
      this.addViewInternal(var1);
   }

   public void addView(View var1, int var2) {
      this.addViewInternal(var1);
   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      this.addViewInternal(var1);
   }

   public void addView(View var1, android.view.ViewGroup.LayoutParams var2) {
      this.addViewInternal(var1);
   }

   protected TabLayout.Tab createTabFromPool() {
      TabLayout.Tab var1 = (TabLayout.Tab)tabPool.acquire();
      TabLayout.Tab var2 = var1;
      if (var1 == null) {
         var2 = new TabLayout.Tab();
      }

      return var2;
   }

   int dpToPx(int var1) {
      return Math.round(this.getResources().getDisplayMetrics().density * (float)var1);
   }

   public LayoutParams generateLayoutParams(AttributeSet var1) {
      return this.generateDefaultLayoutParams();
   }

   public int getSelectedTabPosition() {
      int var1;
      if (this.selectedTab != null) {
         var1 = this.selectedTab.getPosition();
      } else {
         var1 = -1;
      }

      return var1;
   }

   public TabLayout.Tab getTabAt(int var1) {
      TabLayout.Tab var2;
      if (var1 >= 0 && var1 < this.getTabCount()) {
         var2 = (TabLayout.Tab)this.tabs.get(var1);
      } else {
         var2 = null;
      }

      return var2;
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

   public TabLayout.Tab newTab() {
      TabLayout.Tab var1 = this.createTabFromPool();
      var1.parent = this;
      var1.view = this.createTabView(var1);
      return var1;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.viewPager == null) {
         ViewParent var1 = this.getParent();
         if (var1 instanceof ViewPager) {
            this.setupWithViewPager((ViewPager)var1, true, true);
         }
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      if (this.setupViewPagerImplicitly) {
         this.setupWithViewPager((ViewPager)null);
         this.setupViewPagerImplicitly = false;
      }

   }

   protected void onDraw(Canvas var1) {
      for(int var2 = 0; var2 < this.slidingTabIndicator.getChildCount(); ++var2) {
         View var3 = this.slidingTabIndicator.getChildAt(var2);
         if (var3 instanceof TabLayout.TabView) {
            ((TabLayout.TabView)var3).drawBackground(var1);
         }
      }

      super.onDraw(var1);
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = this.dpToPx(this.getDefaultHeight()) + this.getPaddingTop() + this.getPaddingBottom();
      int var4 = MeasureSpec.getMode(var2);
      if (var4 != Integer.MIN_VALUE) {
         if (var4 == 0) {
            var2 = MeasureSpec.makeMeasureSpec(var3, 1073741824);
         }
      } else {
         var2 = MeasureSpec.makeMeasureSpec(Math.min(var3, MeasureSpec.getSize(var2)), 1073741824);
      }

      var4 = MeasureSpec.getSize(var1);
      if (MeasureSpec.getMode(var1) != 0) {
         if (this.requestedTabMaxWidth > 0) {
            var4 = this.requestedTabMaxWidth;
         } else {
            var4 -= this.dpToPx(56);
         }

         this.tabMaxWidth = var4;
      }

      super.onMeasure(var1, var2);
      if (this.getChildCount() == 1) {
         View var5;
         boolean var6;
         label37: {
            var6 = false;
            var5 = this.getChildAt(0);
            switch(this.mode) {
            case 0:
               if (var5.getMeasuredWidth() >= this.getMeasuredWidth()) {
                  break label37;
               }
               break;
            case 1:
               if (var5.getMeasuredWidth() == this.getMeasuredWidth()) {
                  break label37;
               }
               break;
            default:
               break label37;
            }

            var6 = true;
         }

         if (var6) {
            var1 = getChildMeasureSpec(var2, this.getPaddingTop() + this.getPaddingBottom(), var5.getLayoutParams().height);
            var5.measure(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824), var1);
         }
      }

   }

   void populateFromPagerAdapter() {
      this.removeAllTabs();
      if (this.pagerAdapter != null) {
         int var1 = this.pagerAdapter.getCount();

         int var2;
         for(var2 = 0; var2 < var1; ++var2) {
            this.addTab(this.newTab().setText(this.pagerAdapter.getPageTitle(var2)), false);
         }

         if (this.viewPager != null && var1 > 0) {
            var2 = this.viewPager.getCurrentItem();
            if (var2 != this.getSelectedTabPosition() && var2 < this.getTabCount()) {
               this.selectTab(this.getTabAt(var2));
            }
         }
      }

   }

   protected boolean releaseFromTabPool(TabLayout.Tab var1) {
      return tabPool.release(var1);
   }

   public void removeAllTabs() {
      for(int var1 = this.slidingTabIndicator.getChildCount() - 1; var1 >= 0; --var1) {
         this.removeTabViewAt(var1);
      }

      Iterator var2 = this.tabs.iterator();

      while(var2.hasNext()) {
         TabLayout.Tab var3 = (TabLayout.Tab)var2.next();
         var2.remove();
         var3.reset();
         this.releaseFromTabPool(var3);
      }

      this.selectedTab = null;
   }

   public void removeOnTabSelectedListener(TabLayout.BaseOnTabSelectedListener var1) {
      this.selectedListeners.remove(var1);
   }

   void selectTab(TabLayout.Tab var1) {
      this.selectTab(var1, true);
   }

   void selectTab(TabLayout.Tab var1, boolean var2) {
      TabLayout.Tab var3 = this.selectedTab;
      if (var3 == var1) {
         if (var3 != null) {
            this.dispatchTabReselected(var1);
            this.animateToTab(var1.getPosition());
         }
      } else {
         int var4;
         if (var1 != null) {
            var4 = var1.getPosition();
         } else {
            var4 = -1;
         }

         if (var2) {
            if ((var3 == null || var3.getPosition() == -1) && var4 != -1) {
               this.setScrollPosition(var4, 0.0F, true);
            } else {
               this.animateToTab(var4);
            }

            if (var4 != -1) {
               this.setSelectedTabView(var4);
            }
         }

         this.selectedTab = var1;
         if (var3 != null) {
            this.dispatchTabUnselected(var3);
         }

         if (var1 != null) {
            this.dispatchTabSelected(var1);
         }
      }

   }

   public void setInlineLabel(boolean var1) {
      if (this.inlineLabel != var1) {
         this.inlineLabel = var1;

         for(int var2 = 0; var2 < this.slidingTabIndicator.getChildCount(); ++var2) {
            View var3 = this.slidingTabIndicator.getChildAt(var2);
            if (var3 instanceof TabLayout.TabView) {
               ((TabLayout.TabView)var3).updateOrientation();
            }
         }

         this.applyModeAndGravity();
      }

   }

   public void setInlineLabelResource(int var1) {
      this.setInlineLabel(this.getResources().getBoolean(var1));
   }

   @Deprecated
   public void setOnTabSelectedListener(TabLayout.BaseOnTabSelectedListener var1) {
      if (this.selectedListener != null) {
         this.removeOnTabSelectedListener(this.selectedListener);
      }

      this.selectedListener = var1;
      if (var1 != null) {
         this.addOnTabSelectedListener(var1);
      }

   }

   void setPagerAdapter(PagerAdapter var1, boolean var2) {
      if (this.pagerAdapter != null && this.pagerAdapterObserver != null) {
         this.pagerAdapter.unregisterDataSetObserver(this.pagerAdapterObserver);
      }

      this.pagerAdapter = var1;
      if (var2 && var1 != null) {
         if (this.pagerAdapterObserver == null) {
            this.pagerAdapterObserver = new TabLayout.PagerAdapterObserver();
         }

         var1.registerDataSetObserver(this.pagerAdapterObserver);
      }

      this.populateFromPagerAdapter();
   }

   void setScrollAnimatorListener(AnimatorListener var1) {
      this.ensureScrollAnimator();
      this.scrollAnimator.addListener(var1);
   }

   public void setScrollPosition(int var1, float var2, boolean var3) {
      this.setScrollPosition(var1, var2, var3, true);
   }

   void setScrollPosition(int var1, float var2, boolean var3, boolean var4) {
      int var5 = Math.round((float)var1 + var2);
      if (var5 >= 0 && var5 < this.slidingTabIndicator.getChildCount()) {
         if (var4) {
            this.slidingTabIndicator.setIndicatorPositionFromTabPosition(var1, var2);
         }

         if (this.scrollAnimator != null && this.scrollAnimator.isRunning()) {
            this.scrollAnimator.cancel();
         }

         this.scrollTo(this.calculateScrollXForTab(var1, var2), 0);
         if (var3) {
            this.setSelectedTabView(var5);
         }

      }
   }

   public void setSelectedTabIndicator(int var1) {
      if (var1 != 0) {
         this.setSelectedTabIndicator(AppCompatResources.getDrawable(this.getContext(), var1));
      } else {
         this.setSelectedTabIndicator((Drawable)null);
      }

   }

   public void setSelectedTabIndicator(Drawable var1) {
      if (this.tabSelectedIndicator != var1) {
         this.tabSelectedIndicator = var1;
         ViewCompat.postInvalidateOnAnimation(this.slidingTabIndicator);
      }

   }

   public void setSelectedTabIndicatorColor(int var1) {
      this.slidingTabIndicator.setSelectedIndicatorColor(var1);
   }

   public void setSelectedTabIndicatorGravity(int var1) {
      if (this.tabIndicatorGravity != var1) {
         this.tabIndicatorGravity = var1;
         ViewCompat.postInvalidateOnAnimation(this.slidingTabIndicator);
      }

   }

   @Deprecated
   public void setSelectedTabIndicatorHeight(int var1) {
      this.slidingTabIndicator.setSelectedIndicatorHeight(var1);
   }

   public void setTabGravity(int var1) {
      if (this.tabGravity != var1) {
         this.tabGravity = var1;
         this.applyModeAndGravity();
      }

   }

   public void setTabIconTint(ColorStateList var1) {
      if (this.tabIconTint != var1) {
         this.tabIconTint = var1;
         this.updateAllTabs();
      }

   }

   public void setTabIconTintResource(int var1) {
      this.setTabIconTint(AppCompatResources.getColorStateList(this.getContext(), var1));
   }

   public void setTabIndicatorFullWidth(boolean var1) {
      this.tabIndicatorFullWidth = var1;
      ViewCompat.postInvalidateOnAnimation(this.slidingTabIndicator);
   }

   public void setTabMode(int var1) {
      if (var1 != this.mode) {
         this.mode = var1;
         this.applyModeAndGravity();
      }

   }

   public void setTabRippleColor(ColorStateList var1) {
      if (this.tabRippleColorStateList != var1) {
         this.tabRippleColorStateList = var1;

         for(int var2 = 0; var2 < this.slidingTabIndicator.getChildCount(); ++var2) {
            View var3 = this.slidingTabIndicator.getChildAt(var2);
            if (var3 instanceof TabLayout.TabView) {
               ((TabLayout.TabView)var3).updateBackgroundDrawable(this.getContext());
            }
         }
      }

   }

   public void setTabRippleColorResource(int var1) {
      this.setTabRippleColor(AppCompatResources.getColorStateList(this.getContext(), var1));
   }

   public void setTabTextColors(ColorStateList var1) {
      if (this.tabTextColors != var1) {
         this.tabTextColors = var1;
         this.updateAllTabs();
      }

   }

   @Deprecated
   public void setTabsFromPagerAdapter(PagerAdapter var1) {
      this.setPagerAdapter(var1, false);
   }

   public void setUnboundedRipple(boolean var1) {
      if (this.unboundedRipple != var1) {
         this.unboundedRipple = var1;

         for(int var2 = 0; var2 < this.slidingTabIndicator.getChildCount(); ++var2) {
            View var3 = this.slidingTabIndicator.getChildAt(var2);
            if (var3 instanceof TabLayout.TabView) {
               ((TabLayout.TabView)var3).updateBackgroundDrawable(this.getContext());
            }
         }
      }

   }

   public void setUnboundedRippleResource(int var1) {
      this.setUnboundedRipple(this.getResources().getBoolean(var1));
   }

   public void setupWithViewPager(ViewPager var1) {
      this.setupWithViewPager(var1, true);
   }

   public void setupWithViewPager(ViewPager var1, boolean var2) {
      this.setupWithViewPager(var1, var2, false);
   }

   public boolean shouldDelayChildPressedState() {
      boolean var1;
      if (this.getTabScrollRange() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   void updateTabViews(boolean var1) {
      for(int var2 = 0; var2 < this.slidingTabIndicator.getChildCount(); ++var2) {
         View var3 = this.slidingTabIndicator.getChildAt(var2);
         var3.setMinimumWidth(this.getTabMinWidth());
         this.updateTabViewLayoutParams((android.widget.LinearLayout.LayoutParams)var3.getLayoutParams());
         if (var1) {
            var3.requestLayout();
         }
      }

   }

   private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
      private boolean autoRefresh;

      AdapterChangeListener() {
      }

      public void onAdapterChanged(ViewPager var1, PagerAdapter var2, PagerAdapter var3) {
         if (TabLayout.this.viewPager == var1) {
            TabLayout.this.setPagerAdapter(var3, this.autoRefresh);
         }

      }

      void setAutoRefresh(boolean var1) {
         this.autoRefresh = var1;
      }
   }

   public interface BaseOnTabSelectedListener {
      void onTabReselected(TabLayout.Tab var1);

      void onTabSelected(TabLayout.Tab var1);

      void onTabUnselected(TabLayout.Tab var1);
   }

   public interface OnTabSelectedListener extends TabLayout.BaseOnTabSelectedListener {
   }

   private class PagerAdapterObserver extends DataSetObserver {
      PagerAdapterObserver() {
      }

      public void onChanged() {
         TabLayout.this.populateFromPagerAdapter();
      }

      public void onInvalidated() {
         TabLayout.this.populateFromPagerAdapter();
      }
   }

   private class SlidingTabIndicator extends LinearLayout {
      private final GradientDrawable defaultSelectionIndicator;
      private ValueAnimator indicatorAnimator;
      private int indicatorLeft = -1;
      private int indicatorRight = -1;
      private int layoutDirection = -1;
      private int selectedIndicatorHeight;
      private final Paint selectedIndicatorPaint;
      int selectedPosition = -1;
      float selectionOffset;

      SlidingTabIndicator(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
         this.selectedIndicatorPaint = new Paint();
         this.defaultSelectionIndicator = new GradientDrawable();
      }

      private void calculateTabViewContentBounds(TabLayout.TabView var1, RectF var2) {
         int var3 = var1.getContentWidth();
         int var4 = var3;
         if (var3 < TabLayout.this.dpToPx(24)) {
            var4 = TabLayout.this.dpToPx(24);
         }

         var3 = (var1.getLeft() + var1.getRight()) / 2;
         var4 /= 2;
         var2.set((float)(var3 - var4), 0.0F, (float)(var3 + var4), 0.0F);
      }

      private void updateIndicatorPosition() {
         View var1 = this.getChildAt(this.selectedPosition);
         int var2;
         int var3;
         if (var1 != null && var1.getWidth() > 0) {
            var2 = var1.getLeft();
            var3 = var1.getRight();
            int var4 = var2;
            int var5 = var3;
            if (!TabLayout.this.tabIndicatorFullWidth) {
               var4 = var2;
               var5 = var3;
               if (var1 instanceof TabLayout.TabView) {
                  this.calculateTabViewContentBounds((TabLayout.TabView)var1, TabLayout.this.tabViewContentBounds);
                  var4 = (int)TabLayout.this.tabViewContentBounds.left;
                  var5 = (int)TabLayout.this.tabViewContentBounds.right;
               }
            }

            var2 = var4;
            var3 = var5;
            if (this.selectionOffset > 0.0F) {
               var2 = var4;
               var3 = var5;
               if (this.selectedPosition < this.getChildCount() - 1) {
                  var1 = this.getChildAt(this.selectedPosition + 1);
                  int var6 = var1.getLeft();
                  int var7 = var1.getRight();
                  var2 = var6;
                  var3 = var7;
                  if (!TabLayout.this.tabIndicatorFullWidth) {
                     var2 = var6;
                     var3 = var7;
                     if (var1 instanceof TabLayout.TabView) {
                        this.calculateTabViewContentBounds((TabLayout.TabView)var1, TabLayout.this.tabViewContentBounds);
                        var2 = (int)TabLayout.this.tabViewContentBounds.left;
                        var3 = (int)TabLayout.this.tabViewContentBounds.right;
                     }
                  }

                  var2 = (int)(this.selectionOffset * (float)var2 + (1.0F - this.selectionOffset) * (float)var4);
                  var3 = (int)(this.selectionOffset * (float)var3 + (1.0F - this.selectionOffset) * (float)var5);
               }
            }
         } else {
            var2 = -1;
            var3 = -1;
         }

         this.setIndicatorPosition(var2, var3);
      }

      void animateIndicatorToPosition(final int var1, int var2) {
         if (this.indicatorAnimator != null && this.indicatorAnimator.isRunning()) {
            this.indicatorAnimator.cancel();
         }

         View var3 = this.getChildAt(var1);
         if (var3 == null) {
            this.updateIndicatorPosition();
         } else {
            final int var4 = var3.getLeft();
            final int var5 = var3.getRight();
            final int var6 = var4;
            final int var7 = var5;
            if (!TabLayout.this.tabIndicatorFullWidth) {
               var6 = var4;
               var7 = var5;
               if (var3 instanceof TabLayout.TabView) {
                  this.calculateTabViewContentBounds((TabLayout.TabView)var3, TabLayout.this.tabViewContentBounds);
                  var6 = (int)TabLayout.this.tabViewContentBounds.left;
                  var7 = (int)TabLayout.this.tabViewContentBounds.right;
               }
            }

            var4 = this.indicatorLeft;
            var5 = this.indicatorRight;
            if (var4 != var6 || var5 != var7) {
               ValueAnimator var8 = new ValueAnimator();
               this.indicatorAnimator = var8;
               var8.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
               var8.setDuration((long)var2);
               var8.setFloatValues(new float[]{0.0F, 1.0F});
               var8.addUpdateListener(new AnimatorUpdateListener() {
                  public void onAnimationUpdate(ValueAnimator var1) {
                     float var2 = var1.getAnimatedFraction();
                     SlidingTabIndicator.this.setIndicatorPosition(AnimationUtils.lerp(var4, var6, var2), AnimationUtils.lerp(var5, var7, var2));
                  }
               });
               var8.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1x) {
                     SlidingTabIndicator.this.selectedPosition = var1;
                     SlidingTabIndicator.this.selectionOffset = 0.0F;
                  }
               });
               var8.start();
            }

         }
      }

      boolean childrenNeedLayout() {
         int var1 = this.getChildCount();

         for(int var2 = 0; var2 < var1; ++var2) {
            if (this.getChildAt(var2).getWidth() <= 0) {
               return true;
            }
         }

         return false;
      }

      public void draw(Canvas var1) {
         Drawable var2 = TabLayout.this.tabSelectedIndicator;
         byte var3 = 0;
         int var4;
         if (var2 != null) {
            var4 = TabLayout.this.tabSelectedIndicator.getIntrinsicHeight();
         } else {
            var4 = 0;
         }

         if (this.selectedIndicatorHeight >= 0) {
            var4 = this.selectedIndicatorHeight;
         }

         int var5 = var4;
         int var6 = var3;
         switch(TabLayout.this.tabIndicatorGravity) {
         case 0:
            var6 = this.getHeight() - var4;
            var5 = this.getHeight();
            break;
         case 1:
            var6 = (this.getHeight() - var4) / 2;
            var5 = (this.getHeight() + var4) / 2;
         case 2:
            break;
         case 3:
            var5 = this.getHeight();
            var6 = var3;
            break;
         default:
            var5 = 0;
            var6 = var3;
         }

         if (this.indicatorLeft >= 0 && this.indicatorRight > this.indicatorLeft) {
            Object var7;
            if (TabLayout.this.tabSelectedIndicator != null) {
               var7 = TabLayout.this.tabSelectedIndicator;
            } else {
               var7 = this.defaultSelectionIndicator;
            }

            var2 = DrawableCompat.wrap((Drawable)var7);
            var2.setBounds(this.indicatorLeft, var6, this.indicatorRight, var5);
            if (this.selectedIndicatorPaint != null) {
               if (VERSION.SDK_INT == 21) {
                  var2.setColorFilter(this.selectedIndicatorPaint.getColor(), Mode.SRC_IN);
               } else {
                  DrawableCompat.setTint(var2, this.selectedIndicatorPaint.getColor());
               }
            }

            var2.draw(var1);
         }

         super.draw(var1);
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         super.onLayout(var1, var2, var3, var4, var5);
         if (this.indicatorAnimator != null && this.indicatorAnimator.isRunning()) {
            this.indicatorAnimator.cancel();
            long var6 = this.indicatorAnimator.getDuration();
            this.animateIndicatorToPosition(this.selectedPosition, Math.round((1.0F - this.indicatorAnimator.getAnimatedFraction()) * (float)var6));
         } else {
            this.updateIndicatorPosition();
         }

      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(var1, var2);
         if (MeasureSpec.getMode(var1) == 1073741824) {
            int var3 = TabLayout.this.mode;
            boolean var4 = true;
            if (var3 == 1 && TabLayout.this.tabGravity == 1) {
               int var5 = this.getChildCount();
               byte var6 = 0;
               var3 = 0;

               int var7;
               int var9;
               for(var7 = 0; var3 < var5; var7 = var9) {
                  View var8 = this.getChildAt(var3);
                  var9 = var7;
                  if (var8.getVisibility() == 0) {
                     var9 = Math.max(var7, var8.getMeasuredWidth());
                  }

                  ++var3;
               }

               if (var7 <= 0) {
                  return;
               }

               var3 = TabLayout.this.dpToPx(16);
               boolean var10;
               if (var7 * var5 > this.getMeasuredWidth() - var3 * 2) {
                  TabLayout.this.tabGravity = 0;
                  TabLayout.this.updateTabViews(false);
                  var10 = var4;
               } else {
                  var10 = false;

                  for(var9 = var6; var9 < var5; ++var9) {
                     android.widget.LinearLayout.LayoutParams var11 = (android.widget.LinearLayout.LayoutParams)this.getChildAt(var9).getLayoutParams();
                     if (var11.width != var7 || var11.weight != 0.0F) {
                        var11.width = var7;
                        var11.weight = 0.0F;
                        var10 = true;
                     }
                  }
               }

               if (var10) {
                  super.onMeasure(var1, var2);
               }
            }

         }
      }

      public void onRtlPropertiesChanged(int var1) {
         super.onRtlPropertiesChanged(var1);
         if (VERSION.SDK_INT < 23 && this.layoutDirection != var1) {
            this.requestLayout();
            this.layoutDirection = var1;
         }

      }

      void setIndicatorPosition(int var1, int var2) {
         if (var1 != this.indicatorLeft || var2 != this.indicatorRight) {
            this.indicatorLeft = var1;
            this.indicatorRight = var2;
            ViewCompat.postInvalidateOnAnimation(this);
         }

      }

      void setIndicatorPositionFromTabPosition(int var1, float var2) {
         if (this.indicatorAnimator != null && this.indicatorAnimator.isRunning()) {
            this.indicatorAnimator.cancel();
         }

         this.selectedPosition = var1;
         this.selectionOffset = var2;
         this.updateIndicatorPosition();
      }

      void setSelectedIndicatorColor(int var1) {
         if (this.selectedIndicatorPaint.getColor() != var1) {
            this.selectedIndicatorPaint.setColor(var1);
            ViewCompat.postInvalidateOnAnimation(this);
         }

      }

      void setSelectedIndicatorHeight(int var1) {
         if (this.selectedIndicatorHeight != var1) {
            this.selectedIndicatorHeight = var1;
            ViewCompat.postInvalidateOnAnimation(this);
         }

      }
   }

   public static class Tab {
      private CharSequence contentDesc;
      private View customView;
      private Drawable icon;
      public TabLayout parent;
      private int position = -1;
      private Object tag;
      private CharSequence text;
      public TabLayout.TabView view;

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
            boolean var1;
            if (this.parent.getSelectedTabPosition() == this.position) {
               var1 = true;
            } else {
               var1 = false;
            }

            return var1;
         } else {
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
         }
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
         } else {
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
         }
      }

      public TabLayout.Tab setContentDescription(CharSequence var1) {
         this.contentDesc = var1;
         this.updateView();
         return this;
      }

      public TabLayout.Tab setCustomView(int var1) {
         return this.setCustomView(LayoutInflater.from(this.view.getContext()).inflate(var1, this.view, false));
      }

      public TabLayout.Tab setCustomView(View var1) {
         this.customView = var1;
         this.updateView();
         return this;
      }

      public TabLayout.Tab setIcon(Drawable var1) {
         this.icon = var1;
         this.updateView();
         return this;
      }

      void setPosition(int var1) {
         this.position = var1;
      }

      public TabLayout.Tab setText(CharSequence var1) {
         if (TextUtils.isEmpty(this.contentDesc) && !TextUtils.isEmpty(var1)) {
            this.view.setContentDescription(var1);
         }

         this.text = var1;
         this.updateView();
         return this;
      }

      void updateView() {
         if (this.view != null) {
            this.view.update();
         }

      }
   }

   public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
      private int previousScrollState;
      private int scrollState;
      private final WeakReference tabLayoutRef;

      public TabLayoutOnPageChangeListener(TabLayout var1) {
         this.tabLayoutRef = new WeakReference(var1);
      }

      public void onPageScrollStateChanged(int var1) {
         this.previousScrollState = this.scrollState;
         this.scrollState = var1;
      }

      public void onPageScrolled(int var1, float var2, int var3) {
         TabLayout var4 = (TabLayout)this.tabLayoutRef.get();
         if (var4 != null) {
            var3 = this.scrollState;
            boolean var5 = false;
            boolean var6;
            if (var3 == 2 && this.previousScrollState != 1) {
               var6 = false;
            } else {
               var6 = true;
            }

            if (this.scrollState != 2 || this.previousScrollState != 0) {
               var5 = true;
            }

            var4.setScrollPosition(var1, var2, var6, var5);
         }

      }

      public void onPageSelected(int var1) {
         TabLayout var2 = (TabLayout)this.tabLayoutRef.get();
         if (var2 != null && var2.getSelectedTabPosition() != var1 && var1 < var2.getTabCount()) {
            boolean var3;
            if (this.scrollState == 0 || this.scrollState == 2 && this.previousScrollState == 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            var2.selectTab(var2.getTabAt(var1), var3);
         }

      }

      void reset() {
         this.scrollState = 0;
         this.previousScrollState = 0;
      }
   }

   class TabView extends LinearLayout {
      private Drawable baseBackgroundDrawable;
      private ImageView customIconView;
      private TextView customTextView;
      private View customView;
      private int defaultMaxLines = 2;
      private ImageView iconView;
      private TabLayout.Tab tab;
      private TextView textView;

      public TabView(Context var2) {
         super(var2);
         this.updateBackgroundDrawable(var2);
         ViewCompat.setPaddingRelative(this, TabLayout.this.tabPaddingStart, TabLayout.this.tabPaddingTop, TabLayout.this.tabPaddingEnd, TabLayout.this.tabPaddingBottom);
         this.setGravity(17);
         this.setOrientation(TabLayout.this.inlineLabel ^ 1);
         this.setClickable(true);
         ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(this.getContext(), 1002));
      }

      private float approximateLineWidth(Layout var1, int var2, float var3) {
         return var1.getLineWidth(var2) * (var3 / var1.getPaint().getTextSize());
      }

      private void drawBackground(Canvas var1) {
         if (this.baseBackgroundDrawable != null) {
            this.baseBackgroundDrawable.setBounds(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
            this.baseBackgroundDrawable.draw(var1);
         }

      }

      private int getContentWidth() {
         View[] var1 = new View[3];
         TextView var2 = this.textView;
         int var3 = 0;
         var1[0] = var2;
         var1[1] = this.iconView;
         var1[2] = this.customView;
         int var4 = var1.length;
         int var5 = 0;
         int var6 = 0;

         boolean var10;
         for(boolean var7 = false; var3 < var4; var7 = var10) {
            View var11 = var1[var3];
            int var8 = var5;
            int var9 = var6;
            var10 = var7;
            if (var11 != null) {
               var8 = var5;
               var9 = var6;
               var10 = var7;
               if (var11.getVisibility() == 0) {
                  if (var7) {
                     var6 = Math.min(var6, var11.getLeft());
                  } else {
                     var6 = var11.getLeft();
                  }

                  int var12;
                  if (var7) {
                     var12 = Math.max(var5, var11.getRight());
                  } else {
                     var12 = var11.getRight();
                  }

                  var10 = true;
                  var9 = var6;
                  var8 = var12;
               }
            }

            ++var3;
            var5 = var8;
            var6 = var9;
         }

         return var5 - var6;
      }

      private void updateBackgroundDrawable(Context var1) {
         if (TabLayout.this.tabBackgroundResId != 0) {
            this.baseBackgroundDrawable = AppCompatResources.getDrawable(var1, TabLayout.this.tabBackgroundResId);
            if (this.baseBackgroundDrawable != null && this.baseBackgroundDrawable.isStateful()) {
               this.baseBackgroundDrawable.setState(this.getDrawableState());
            }
         } else {
            this.baseBackgroundDrawable = null;
         }

         GradientDrawable var2 = new GradientDrawable();
         ((GradientDrawable)var2).setColor(0);
         Object var4 = var2;
         if (TabLayout.this.tabRippleColorStateList != null) {
            GradientDrawable var5 = new GradientDrawable();
            var5.setCornerRadius(1.0E-5F);
            var5.setColor(-1);
            ColorStateList var3 = RippleUtils.convertToRippleDrawableColor(TabLayout.this.tabRippleColorStateList);
            if (VERSION.SDK_INT >= 21) {
               if (TabLayout.this.unboundedRipple) {
                  var2 = null;
               }

               if (TabLayout.this.unboundedRipple) {
                  var5 = null;
               }

               var4 = new RippleDrawable(var3, var2, var5);
            } else {
               Drawable var6 = DrawableCompat.wrap(var5);
               DrawableCompat.setTintList(var6, var3);
               var4 = new LayerDrawable(new Drawable[]{var2, var6});
            }
         }

         ViewCompat.setBackground(this, (Drawable)var4);
         TabLayout.this.invalidate();
      }

      private void updateTextAndIcon(TextView var1, ImageView var2) {
         Drawable var3;
         if (this.tab != null && this.tab.getIcon() != null) {
            var3 = DrawableCompat.wrap(this.tab.getIcon()).mutate();
         } else {
            var3 = null;
         }

         CharSequence var4;
         if (this.tab != null) {
            var4 = this.tab.getText();
         } else {
            var4 = null;
         }

         if (var2 != null) {
            if (var3 != null) {
               var2.setImageDrawable(var3);
               var2.setVisibility(0);
               this.setVisibility(0);
            } else {
               var2.setVisibility(8);
               var2.setImageDrawable((Drawable)null);
            }
         }

         boolean var5 = TextUtils.isEmpty(var4) ^ true;
         if (var1 != null) {
            if (var5) {
               var1.setText(var4);
               var1.setVisibility(0);
               this.setVisibility(0);
            } else {
               var1.setVisibility(8);
               var1.setText((CharSequence)null);
            }
         }

         if (var2 != null) {
            MarginLayoutParams var7 = (MarginLayoutParams)var2.getLayoutParams();
            int var6;
            if (var5 && var2.getVisibility() == 0) {
               var6 = TabLayout.this.dpToPx(8);
            } else {
               var6 = 0;
            }

            if (TabLayout.this.inlineLabel) {
               if (var6 != MarginLayoutParamsCompat.getMarginEnd(var7)) {
                  MarginLayoutParamsCompat.setMarginEnd(var7, var6);
                  var7.bottomMargin = 0;
                  var2.setLayoutParams(var7);
                  var2.requestLayout();
               }
            } else if (var6 != var7.bottomMargin) {
               var7.bottomMargin = var6;
               MarginLayoutParamsCompat.setMarginEnd(var7, 0);
               var2.setLayoutParams(var7);
               var2.requestLayout();
            }
         }

         CharSequence var8;
         if (this.tab != null) {
            var8 = this.tab.contentDesc;
         } else {
            var8 = null;
         }

         if (var5) {
            var8 = null;
         }

         TooltipCompat.setTooltipText(this, var8);
      }

      protected void drawableStateChanged() {
         super.drawableStateChanged();
         int[] var1 = this.getDrawableState();
         Drawable var2 = this.baseBackgroundDrawable;
         boolean var3 = false;
         boolean var4 = var3;
         if (var2 != null) {
            var4 = var3;
            if (this.baseBackgroundDrawable.isStateful()) {
               var4 = false | this.baseBackgroundDrawable.setState(var1);
            }
         }

         if (var4) {
            this.invalidate();
            TabLayout.this.invalidate();
         }

      }

      public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
         super.onInitializeAccessibilityEvent(var1);
         var1.setClassName(ActionBar.Tab.class.getName());
      }

      @TargetApi(14)
      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setClassName(ActionBar.Tab.class.getName());
      }

      public void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         int var4 = MeasureSpec.getMode(var1);
         int var5 = TabLayout.this.getTabMaxWidth();
         int var6 = var1;
         if (var5 > 0) {
            label57: {
               if (var4 != 0) {
                  var6 = var1;
                  if (var3 <= var5) {
                     break label57;
                  }
               }

               var6 = MeasureSpec.makeMeasureSpec(TabLayout.this.tabMaxWidth, Integer.MIN_VALUE);
            }
         }

         super.onMeasure(var6, var2);
         if (this.textView != null) {
            float var7 = TabLayout.this.tabTextSize;
            var4 = this.defaultMaxLines;
            ImageView var8 = this.iconView;
            boolean var11 = true;
            float var9;
            if (var8 != null && this.iconView.getVisibility() == 0) {
               var1 = 1;
               var9 = var7;
            } else {
               var9 = var7;
               var1 = var4;
               if (this.textView != null) {
                  var9 = var7;
                  var1 = var4;
                  if (this.textView.getLineCount() > 1) {
                     var9 = TabLayout.this.tabTextMultiLineSize;
                     var1 = var4;
                  }
               }
            }

            var7 = this.textView.getTextSize();
            var5 = this.textView.getLineCount();
            var4 = TextViewCompat.getMaxLines(this.textView);
            float var14;
            int var10 = (var14 = var9 - var7) == 0.0F ? 0 : (var14 < 0.0F ? -1 : 1);
            if (var10 != 0 || var4 >= 0 && var1 != var4) {
               boolean var12 = var11;
               if (TabLayout.this.mode == 1) {
                  var12 = var11;
                  if (var10 > 0) {
                     var12 = var11;
                     if (var5 == 1) {
                        label65: {
                           Layout var13 = this.textView.getLayout();
                           if (var13 != null) {
                              var12 = var11;
                              if (this.approximateLineWidth(var13, 0, var9) <= (float)(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight())) {
                                 break label65;
                              }
                           }

                           var12 = false;
                        }
                     }
                  }
               }

               if (var12) {
                  this.textView.setTextSize(0, var9);
                  this.textView.setMaxLines(var1);
                  super.onMeasure(var6, var2);
               }
            }
         }

      }

      public boolean performClick() {
         boolean var1 = super.performClick();
         if (this.tab != null) {
            if (!var1) {
               this.playSoundEffect(0);
            }

            this.tab.select();
            return true;
         } else {
            return var1;
         }
      }

      void reset() {
         this.setTab((TabLayout.Tab)null);
         this.setSelected(false);
      }

      public void setSelected(boolean var1) {
         boolean var2;
         if (this.isSelected() != var1) {
            var2 = true;
         } else {
            var2 = false;
         }

         super.setSelected(var1);
         if (var2 && var1 && VERSION.SDK_INT < 16) {
            this.sendAccessibilityEvent(4);
         }

         if (this.textView != null) {
            this.textView.setSelected(var1);
         }

         if (this.iconView != null) {
            this.iconView.setSelected(var1);
         }

         if (this.customView != null) {
            this.customView.setSelected(var1);
         }

      }

      void setTab(TabLayout.Tab var1) {
         if (var1 != this.tab) {
            this.tab = var1;
            this.update();
         }

      }

      final void update() {
         TabLayout.Tab var1 = this.tab;
         Object var2 = null;
         View var3;
         if (var1 != null) {
            var3 = var1.getCustomView();
         } else {
            var3 = null;
         }

         if (var3 != null) {
            ViewParent var4 = var3.getParent();
            if (var4 != this) {
               if (var4 != null) {
                  ((ViewGroup)var4).removeView(var3);
               }

               this.addView(var3);
            }

            this.customView = var3;
            if (this.textView != null) {
               this.textView.setVisibility(8);
            }

            if (this.iconView != null) {
               this.iconView.setVisibility(8);
               this.iconView.setImageDrawable((Drawable)null);
            }

            this.customTextView = (TextView)var3.findViewById(16908308);
            if (this.customTextView != null) {
               this.defaultMaxLines = TextViewCompat.getMaxLines(this.customTextView);
            }

            this.customIconView = (ImageView)var3.findViewById(16908294);
         } else {
            if (this.customView != null) {
               this.removeView(this.customView);
               this.customView = null;
            }

            this.customTextView = null;
            this.customIconView = null;
         }

         var3 = this.customView;
         boolean var5 = false;
         if (var3 == null) {
            if (this.iconView == null) {
               ImageView var7 = (ImageView)LayoutInflater.from(this.getContext()).inflate(R.layout.design_layout_tab_icon, this, false);
               this.addView(var7, 0);
               this.iconView = var7;
            }

            Drawable var8 = (Drawable)var2;
            if (var1 != null) {
               var8 = (Drawable)var2;
               if (var1.getIcon() != null) {
                  var8 = DrawableCompat.wrap(var1.getIcon()).mutate();
               }
            }

            if (var8 != null) {
               DrawableCompat.setTintList(var8, TabLayout.this.tabIconTint);
               if (TabLayout.this.tabIconTintMode != null) {
                  DrawableCompat.setTintMode(var8, TabLayout.this.tabIconTintMode);
               }
            }

            if (this.textView == null) {
               TextView var9 = (TextView)LayoutInflater.from(this.getContext()).inflate(R.layout.design_layout_tab_text, this, false);
               this.addView(var9);
               this.textView = var9;
               this.defaultMaxLines = TextViewCompat.getMaxLines(this.textView);
            }

            TextViewCompat.setTextAppearance(this.textView, TabLayout.this.tabTextAppearance);
            if (TabLayout.this.tabTextColors != null) {
               this.textView.setTextColor(TabLayout.this.tabTextColors);
            }

            this.updateTextAndIcon(this.textView, this.iconView);
         } else if (this.customTextView != null || this.customIconView != null) {
            this.updateTextAndIcon(this.customTextView, this.customIconView);
         }

         if (var1 != null && !TextUtils.isEmpty(var1.contentDesc)) {
            this.setContentDescription(var1.contentDesc);
         }

         boolean var6 = var5;
         if (var1 != null) {
            var6 = var5;
            if (var1.isSelected()) {
               var6 = true;
            }
         }

         this.setSelected(var6);
      }

      final void updateOrientation() {
         this.setOrientation(TabLayout.this.inlineLabel ^ 1);
         if (this.customTextView == null && this.customIconView == null) {
            this.updateTextAndIcon(this.textView, this.iconView);
         } else {
            this.updateTextAndIcon(this.customTextView, this.customIconView);
         }

      }
   }

   public static class ViewPagerOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
      private final ViewPager viewPager;

      public ViewPagerOnTabSelectedListener(ViewPager var1) {
         this.viewPager = var1;
      }

      public void onTabReselected(TabLayout.Tab var1) {
      }

      public void onTabSelected(TabLayout.Tab var1) {
         this.viewPager.setCurrentItem(var1.getPosition());
      }

      public void onTabUnselected(TabLayout.Tab var1) {
      }
   }
}
