package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$styleable;
import androidx.appcompat.app.ActionBar$LayoutParams;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.core.view.GravityCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import java.util.ArrayList;
import java.util.List;

public class Toolbar extends ViewGroup {
   private MenuPresenter.Callback mActionMenuPresenterCallback;
   int mButtonGravity;
   ImageButton mCollapseButtonView;
   private CharSequence mCollapseDescription;
   private Drawable mCollapseIcon;
   private boolean mCollapsible;
   private int mContentInsetEndWithActions;
   private int mContentInsetStartWithNavigation;
   private RtlSpacingHelper mContentInsets;
   private boolean mEatingHover;
   private boolean mEatingTouch;
   View mExpandedActionView;
   private Toolbar.ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
   private int mGravity;
   private final ArrayList mHiddenViews;
   private ImageView mLogoView;
   private int mMaxButtonHeight;
   private MenuBuilder.Callback mMenuBuilderCallback;
   private ActionMenuView mMenuView;
   private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
   private ImageButton mNavButtonView;
   Toolbar.OnMenuItemClickListener mOnMenuItemClickListener;
   private ActionMenuPresenter mOuterActionMenuPresenter;
   private Context mPopupContext;
   private int mPopupTheme;
   private final Runnable mShowOverflowMenuRunnable;
   private CharSequence mSubtitleText;
   private int mSubtitleTextAppearance;
   private int mSubtitleTextColor;
   private TextView mSubtitleTextView;
   private final int[] mTempMargins;
   private final ArrayList mTempViews;
   private int mTitleMarginBottom;
   private int mTitleMarginEnd;
   private int mTitleMarginStart;
   private int mTitleMarginTop;
   private CharSequence mTitleText;
   private int mTitleTextAppearance;
   private int mTitleTextColor;
   private TextView mTitleTextView;
   private ToolbarWidgetWrapper mWrapper;

   public Toolbar(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public Toolbar(Context var1, AttributeSet var2) {
      this(var1, var2, R$attr.toolbarStyle);
   }

   public Toolbar(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mGravity = 8388627;
      this.mTempViews = new ArrayList();
      this.mHiddenViews = new ArrayList();
      this.mTempMargins = new int[2];
      this.mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener() {
         public boolean onMenuItemClick(MenuItem var1) {
            Toolbar.OnMenuItemClickListener var2 = Toolbar.this.mOnMenuItemClickListener;
            return var2 != null ? var2.onMenuItemClick(var1) : false;
         }
      };
      this.mShowOverflowMenuRunnable = new Runnable() {
         public void run() {
            Toolbar.this.showOverflowMenu();
         }
      };
      TintTypedArray var7 = TintTypedArray.obtainStyledAttributes(this.getContext(), var2, R$styleable.Toolbar, var3, 0);
      this.mTitleTextAppearance = var7.getResourceId(R$styleable.Toolbar_titleTextAppearance, 0);
      this.mSubtitleTextAppearance = var7.getResourceId(R$styleable.Toolbar_subtitleTextAppearance, 0);
      this.mGravity = var7.getInteger(R$styleable.Toolbar_android_gravity, this.mGravity);
      this.mButtonGravity = var7.getInteger(R$styleable.Toolbar_buttonGravity, 48);
      int var4 = var7.getDimensionPixelOffset(R$styleable.Toolbar_titleMargin, 0);
      var3 = var4;
      if (var7.hasValue(R$styleable.Toolbar_titleMargins)) {
         var3 = var7.getDimensionPixelOffset(R$styleable.Toolbar_titleMargins, var4);
      }

      this.mTitleMarginBottom = var3;
      this.mTitleMarginTop = var3;
      this.mTitleMarginEnd = var3;
      this.mTitleMarginStart = var3;
      var3 = var7.getDimensionPixelOffset(R$styleable.Toolbar_titleMarginStart, -1);
      if (var3 >= 0) {
         this.mTitleMarginStart = var3;
      }

      var3 = var7.getDimensionPixelOffset(R$styleable.Toolbar_titleMarginEnd, -1);
      if (var3 >= 0) {
         this.mTitleMarginEnd = var3;
      }

      var3 = var7.getDimensionPixelOffset(R$styleable.Toolbar_titleMarginTop, -1);
      if (var3 >= 0) {
         this.mTitleMarginTop = var3;
      }

      var3 = var7.getDimensionPixelOffset(R$styleable.Toolbar_titleMarginBottom, -1);
      if (var3 >= 0) {
         this.mTitleMarginBottom = var3;
      }

      this.mMaxButtonHeight = var7.getDimensionPixelSize(R$styleable.Toolbar_maxButtonHeight, -1);
      var3 = var7.getDimensionPixelOffset(R$styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
      int var5 = var7.getDimensionPixelOffset(R$styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
      int var6 = var7.getDimensionPixelSize(R$styleable.Toolbar_contentInsetLeft, 0);
      var4 = var7.getDimensionPixelSize(R$styleable.Toolbar_contentInsetRight, 0);
      this.ensureContentInsets();
      this.mContentInsets.setAbsolute(var6, var4);
      if (var3 != Integer.MIN_VALUE || var5 != Integer.MIN_VALUE) {
         this.mContentInsets.setRelative(var3, var5);
      }

      this.mContentInsetStartWithNavigation = var7.getDimensionPixelOffset(R$styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
      this.mContentInsetEndWithActions = var7.getDimensionPixelOffset(R$styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
      this.mCollapseIcon = var7.getDrawable(R$styleable.Toolbar_collapseIcon);
      this.mCollapseDescription = var7.getText(R$styleable.Toolbar_collapseContentDescription);
      CharSequence var8 = var7.getText(R$styleable.Toolbar_title);
      if (!TextUtils.isEmpty(var8)) {
         this.setTitle(var8);
      }

      var8 = var7.getText(R$styleable.Toolbar_subtitle);
      if (!TextUtils.isEmpty(var8)) {
         this.setSubtitle(var8);
      }

      this.mPopupContext = this.getContext();
      this.setPopupTheme(var7.getResourceId(R$styleable.Toolbar_popupTheme, 0));
      Drawable var9 = var7.getDrawable(R$styleable.Toolbar_navigationIcon);
      if (var9 != null) {
         this.setNavigationIcon(var9);
      }

      var8 = var7.getText(R$styleable.Toolbar_navigationContentDescription);
      if (!TextUtils.isEmpty(var8)) {
         this.setNavigationContentDescription(var8);
      }

      var9 = var7.getDrawable(R$styleable.Toolbar_logo);
      if (var9 != null) {
         this.setLogo(var9);
      }

      var8 = var7.getText(R$styleable.Toolbar_logoDescription);
      if (!TextUtils.isEmpty(var8)) {
         this.setLogoDescription(var8);
      }

      if (var7.hasValue(R$styleable.Toolbar_titleTextColor)) {
         this.setTitleTextColor(var7.getColor(R$styleable.Toolbar_titleTextColor, -1));
      }

      if (var7.hasValue(R$styleable.Toolbar_subtitleTextColor)) {
         this.setSubtitleTextColor(var7.getColor(R$styleable.Toolbar_subtitleTextColor, -1));
      }

      var7.recycle();
   }

   private void addCustomViewsWithGravity(List var1, int var2) {
      int var3 = ViewCompat.getLayoutDirection(this);
      byte var4 = 0;
      boolean var9;
      if (var3 == 1) {
         var9 = true;
      } else {
         var9 = false;
      }

      int var5 = this.getChildCount();
      int var6 = GravityCompat.getAbsoluteGravity(var2, ViewCompat.getLayoutDirection(this));
      var1.clear();
      var2 = var4;
      if (var9) {
         for(var2 = var5 - 1; var2 >= 0; --var2) {
            View var10 = this.getChildAt(var2);
            Toolbar.LayoutParams var11 = (Toolbar.LayoutParams)var10.getLayoutParams();
            if (var11.mViewType == 0 && this.shouldLayout(var10) && this.getChildHorizontalGravity(var11.gravity) == var6) {
               var1.add(var10);
            }
         }
      } else {
         for(; var2 < var5; ++var2) {
            View var8 = this.getChildAt(var2);
            Toolbar.LayoutParams var7 = (Toolbar.LayoutParams)var8.getLayoutParams();
            if (var7.mViewType == 0 && this.shouldLayout(var8) && this.getChildHorizontalGravity(var7.gravity) == var6) {
               var1.add(var8);
            }
         }
      }

   }

   private void addSystemView(View var1, boolean var2) {
      android.view.ViewGroup.LayoutParams var3 = var1.getLayoutParams();
      Toolbar.LayoutParams var4;
      if (var3 == null) {
         var4 = this.generateDefaultLayoutParams();
      } else if (!this.checkLayoutParams(var3)) {
         var4 = this.generateLayoutParams(var3);
      } else {
         var4 = (Toolbar.LayoutParams)var3;
      }

      var4.mViewType = 1;
      if (var2 && this.mExpandedActionView != null) {
         var1.setLayoutParams(var4);
         this.mHiddenViews.add(var1);
      } else {
         this.addView(var1, var4);
      }

   }

   private void ensureContentInsets() {
      if (this.mContentInsets == null) {
         this.mContentInsets = new RtlSpacingHelper();
      }

   }

   private void ensureLogoView() {
      if (this.mLogoView == null) {
         this.mLogoView = new AppCompatImageView(this.getContext());
      }

   }

   private void ensureMenu() {
      this.ensureMenuView();
      if (this.mMenuView.peekMenu() == null) {
         MenuBuilder var1 = (MenuBuilder)this.mMenuView.getMenu();
         if (this.mExpandedMenuPresenter == null) {
            this.mExpandedMenuPresenter = new Toolbar.ExpandedActionViewMenuPresenter();
         }

         this.mMenuView.setExpandedActionViewsExclusive(true);
         var1.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
      }

   }

   private void ensureMenuView() {
      if (this.mMenuView == null) {
         this.mMenuView = new ActionMenuView(this.getContext());
         this.mMenuView.setPopupTheme(this.mPopupTheme);
         this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
         this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
         Toolbar.LayoutParams var1 = this.generateDefaultLayoutParams();
         var1.gravity = 8388613 | this.mButtonGravity & 112;
         this.mMenuView.setLayoutParams(var1);
         this.addSystemView(this.mMenuView, false);
      }

   }

   private void ensureNavButtonView() {
      if (this.mNavButtonView == null) {
         this.mNavButtonView = new AppCompatImageButton(this.getContext(), (AttributeSet)null, R$attr.toolbarNavigationButtonStyle);
         Toolbar.LayoutParams var1 = this.generateDefaultLayoutParams();
         var1.gravity = 8388611 | this.mButtonGravity & 112;
         this.mNavButtonView.setLayoutParams(var1);
      }

   }

   private int getChildHorizontalGravity(int var1) {
      int var2 = ViewCompat.getLayoutDirection(this);
      int var3 = GravityCompat.getAbsoluteGravity(var1, var2) & 7;
      if (var3 != 1) {
         byte var4 = 3;
         if (var3 != 3 && var3 != 5) {
            if (var2 == 1) {
               var4 = 5;
            }

            return var4;
         }
      }

      return var3;
   }

   private int getChildTop(View var1, int var2) {
      Toolbar.LayoutParams var3 = (Toolbar.LayoutParams)var1.getLayoutParams();
      int var4 = var1.getMeasuredHeight();
      if (var2 > 0) {
         var2 = (var4 - var2) / 2;
      } else {
         var2 = 0;
      }

      int var5 = this.getChildVerticalGravity(var3.gravity);
      if (var5 != 48) {
         if (var5 != 80) {
            int var6 = this.getPaddingTop();
            int var7 = this.getPaddingBottom();
            int var8 = this.getHeight();
            var5 = (var8 - var6 - var7 - var4) / 2;
            var2 = var3.topMargin;
            if (var5 >= var2) {
               var7 = var8 - var7 - var4 - var5 - var6;
               var4 = var3.bottomMargin;
               var2 = var5;
               if (var7 < var4) {
                  var2 = Math.max(0, var5 - (var4 - var7));
               }
            }

            return var6 + var2;
         } else {
            return this.getHeight() - this.getPaddingBottom() - var4 - var3.bottomMargin - var2;
         }
      } else {
         return this.getPaddingTop() - var2;
      }
   }

   private int getChildVerticalGravity(int var1) {
      int var2 = var1 & 112;
      var1 = var2;
      if (var2 != 16) {
         var1 = var2;
         if (var2 != 48) {
            var1 = var2;
            if (var2 != 80) {
               var1 = this.mGravity & 112;
            }
         }
      }

      return var1;
   }

   private int getHorizontalMargins(View var1) {
      MarginLayoutParams var2 = (MarginLayoutParams)var1.getLayoutParams();
      return MarginLayoutParamsCompat.getMarginStart(var2) + MarginLayoutParamsCompat.getMarginEnd(var2);
   }

   private MenuInflater getMenuInflater() {
      return new SupportMenuInflater(this.getContext());
   }

   private int getVerticalMargins(View var1) {
      MarginLayoutParams var2 = (MarginLayoutParams)var1.getLayoutParams();
      return var2.topMargin + var2.bottomMargin;
   }

   private int getViewListMeasuredWidth(List var1, int[] var2) {
      int var3 = var2[0];
      int var4 = var2[1];
      int var5 = var1.size();
      int var6 = 0;

      int var7;
      for(var7 = 0; var6 < var5; ++var6) {
         View var11 = (View)var1.get(var6);
         Toolbar.LayoutParams var8 = (Toolbar.LayoutParams)var11.getLayoutParams();
         var3 = var8.leftMargin - var3;
         var4 = var8.rightMargin - var4;
         int var9 = Math.max(0, var3);
         int var10 = Math.max(0, var4);
         var3 = Math.max(0, -var3);
         var4 = Math.max(0, -var4);
         var7 += var9 + var11.getMeasuredWidth() + var10;
      }

      return var7;
   }

   private boolean isChildOrHidden(View var1) {
      boolean var2;
      if (var1.getParent() != this && !this.mHiddenViews.contains(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private int layoutChildLeft(View var1, int var2, int[] var3, int var4) {
      Toolbar.LayoutParams var5 = (Toolbar.LayoutParams)var1.getLayoutParams();
      int var6 = var5.leftMargin - var3[0];
      var2 += Math.max(0, var6);
      var3[0] = Math.max(0, -var6);
      var4 = this.getChildTop(var1, var4);
      var6 = var1.getMeasuredWidth();
      var1.layout(var2, var4, var2 + var6, var1.getMeasuredHeight() + var4);
      return var2 + var6 + var5.rightMargin;
   }

   private int layoutChildRight(View var1, int var2, int[] var3, int var4) {
      Toolbar.LayoutParams var5 = (Toolbar.LayoutParams)var1.getLayoutParams();
      int var6 = var5.rightMargin - var3[1];
      var2 -= Math.max(0, var6);
      var3[1] = Math.max(0, -var6);
      var6 = this.getChildTop(var1, var4);
      var4 = var1.getMeasuredWidth();
      var1.layout(var2 - var4, var6, var2, var1.getMeasuredHeight() + var6);
      return var2 - (var4 + var5.leftMargin);
   }

   private int measureChildCollapseMargins(View var1, int var2, int var3, int var4, int var5, int[] var6) {
      MarginLayoutParams var7 = (MarginLayoutParams)var1.getLayoutParams();
      int var8 = var7.leftMargin - var6[0];
      int var9 = var7.rightMargin - var6[1];
      int var10 = Math.max(0, var8) + Math.max(0, var9);
      var6[0] = Math.max(0, -var8);
      var6[1] = Math.max(0, -var9);
      var1.measure(ViewGroup.getChildMeasureSpec(var2, this.getPaddingLeft() + this.getPaddingRight() + var10 + var3, var7.width), ViewGroup.getChildMeasureSpec(var4, this.getPaddingTop() + this.getPaddingBottom() + var7.topMargin + var7.bottomMargin + var5, var7.height));
      return var1.getMeasuredWidth() + var10;
   }

   private void measureChildConstrained(View var1, int var2, int var3, int var4, int var5, int var6) {
      MarginLayoutParams var7 = (MarginLayoutParams)var1.getLayoutParams();
      int var8 = ViewGroup.getChildMeasureSpec(var2, this.getPaddingLeft() + this.getPaddingRight() + var7.leftMargin + var7.rightMargin + var3, var7.width);
      var3 = ViewGroup.getChildMeasureSpec(var4, this.getPaddingTop() + this.getPaddingBottom() + var7.topMargin + var7.bottomMargin + var5, var7.height);
      var4 = MeasureSpec.getMode(var3);
      var2 = var3;
      if (var4 != 1073741824) {
         var2 = var3;
         if (var6 >= 0) {
            var2 = var6;
            if (var4 != 0) {
               var2 = Math.min(MeasureSpec.getSize(var3), var6);
            }

            var2 = MeasureSpec.makeMeasureSpec(var2, 1073741824);
         }
      }

      var1.measure(var8, var2);
   }

   private void postShowOverflowMenu() {
      this.removeCallbacks(this.mShowOverflowMenuRunnable);
      this.post(this.mShowOverflowMenuRunnable);
   }

   private boolean shouldCollapse() {
      if (!this.mCollapsible) {
         return false;
      } else {
         int var1 = this.getChildCount();

         for(int var2 = 0; var2 < var1; ++var2) {
            View var3 = this.getChildAt(var2);
            if (this.shouldLayout(var3) && var3.getMeasuredWidth() > 0 && var3.getMeasuredHeight() > 0) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean shouldLayout(View var1) {
      boolean var2;
      if (var1 != null && var1.getParent() == this && var1.getVisibility() != 8) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   void addChildrenForExpandedActionView() {
      for(int var1 = this.mHiddenViews.size() - 1; var1 >= 0; --var1) {
         this.addView((View)this.mHiddenViews.get(var1));
      }

      this.mHiddenViews.clear();
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      boolean var2;
      if (super.checkLayoutParams(var1) && var1 instanceof Toolbar.LayoutParams) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void collapseActionView() {
      Toolbar.ExpandedActionViewMenuPresenter var1 = this.mExpandedMenuPresenter;
      MenuItemImpl var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = var1.mCurrentExpandedItem;
      }

      if (var2 != null) {
         var2.collapseActionView();
      }

   }

   void ensureCollapseButtonView() {
      if (this.mCollapseButtonView == null) {
         this.mCollapseButtonView = new AppCompatImageButton(this.getContext(), (AttributeSet)null, R$attr.toolbarNavigationButtonStyle);
         this.mCollapseButtonView.setImageDrawable(this.mCollapseIcon);
         this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
         Toolbar.LayoutParams var1 = this.generateDefaultLayoutParams();
         var1.gravity = 8388611 | this.mButtonGravity & 112;
         var1.mViewType = 2;
         this.mCollapseButtonView.setLayoutParams(var1);
         this.mCollapseButtonView.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               Toolbar.this.collapseActionView();
            }
         });
      }

   }

   protected Toolbar.LayoutParams generateDefaultLayoutParams() {
      return new Toolbar.LayoutParams(-2, -2);
   }

   public Toolbar.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new Toolbar.LayoutParams(this.getContext(), var1);
   }

   protected Toolbar.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      if (var1 instanceof Toolbar.LayoutParams) {
         return new Toolbar.LayoutParams((Toolbar.LayoutParams)var1);
      } else if (var1 instanceof ActionBar$LayoutParams) {
         return new Toolbar.LayoutParams((ActionBar$LayoutParams)var1);
      } else {
         return var1 instanceof MarginLayoutParams ? new Toolbar.LayoutParams((MarginLayoutParams)var1) : new Toolbar.LayoutParams(var1);
      }
   }

   public int getContentInsetEnd() {
      RtlSpacingHelper var1 = this.mContentInsets;
      int var2;
      if (var1 != null) {
         var2 = var1.getEnd();
      } else {
         var2 = 0;
      }

      return var2;
   }

   public int getContentInsetEndWithActions() {
      int var1 = this.mContentInsetEndWithActions;
      if (var1 == Integer.MIN_VALUE) {
         var1 = this.getContentInsetEnd();
      }

      return var1;
   }

   public int getContentInsetLeft() {
      RtlSpacingHelper var1 = this.mContentInsets;
      int var2;
      if (var1 != null) {
         var2 = var1.getLeft();
      } else {
         var2 = 0;
      }

      return var2;
   }

   public int getContentInsetRight() {
      RtlSpacingHelper var1 = this.mContentInsets;
      int var2;
      if (var1 != null) {
         var2 = var1.getRight();
      } else {
         var2 = 0;
      }

      return var2;
   }

   public int getContentInsetStart() {
      RtlSpacingHelper var1 = this.mContentInsets;
      int var2;
      if (var1 != null) {
         var2 = var1.getStart();
      } else {
         var2 = 0;
      }

      return var2;
   }

   public int getContentInsetStartWithNavigation() {
      int var1 = this.mContentInsetStartWithNavigation;
      if (var1 == Integer.MIN_VALUE) {
         var1 = this.getContentInsetStart();
      }

      return var1;
   }

   public int getCurrentContentInsetEnd() {
      boolean var2;
      label19: {
         ActionMenuView var1 = this.mMenuView;
         if (var1 != null) {
            MenuBuilder var3 = var1.peekMenu();
            if (var3 != null && var3.hasVisibleItems()) {
               var2 = true;
               break label19;
            }
         }

         var2 = false;
      }

      int var4;
      if (var2) {
         var4 = Math.max(this.getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0));
      } else {
         var4 = this.getContentInsetEnd();
      }

      return var4;
   }

   public int getCurrentContentInsetLeft() {
      int var1;
      if (ViewCompat.getLayoutDirection(this) == 1) {
         var1 = this.getCurrentContentInsetEnd();
      } else {
         var1 = this.getCurrentContentInsetStart();
      }

      return var1;
   }

   public int getCurrentContentInsetRight() {
      int var1;
      if (ViewCompat.getLayoutDirection(this) == 1) {
         var1 = this.getCurrentContentInsetStart();
      } else {
         var1 = this.getCurrentContentInsetEnd();
      }

      return var1;
   }

   public int getCurrentContentInsetStart() {
      int var1;
      if (this.getNavigationIcon() != null) {
         var1 = Math.max(this.getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0));
      } else {
         var1 = this.getContentInsetStart();
      }

      return var1;
   }

   public Drawable getLogo() {
      ImageView var1 = this.mLogoView;
      Drawable var2;
      if (var1 != null) {
         var2 = var1.getDrawable();
      } else {
         var2 = null;
      }

      return var2;
   }

   public CharSequence getLogoDescription() {
      ImageView var1 = this.mLogoView;
      CharSequence var2;
      if (var1 != null) {
         var2 = var1.getContentDescription();
      } else {
         var2 = null;
      }

      return var2;
   }

   public Menu getMenu() {
      this.ensureMenu();
      return this.mMenuView.getMenu();
   }

   public CharSequence getNavigationContentDescription() {
      ImageButton var1 = this.mNavButtonView;
      CharSequence var2;
      if (var1 != null) {
         var2 = var1.getContentDescription();
      } else {
         var2 = null;
      }

      return var2;
   }

   public Drawable getNavigationIcon() {
      ImageButton var1 = this.mNavButtonView;
      Drawable var2;
      if (var1 != null) {
         var2 = var1.getDrawable();
      } else {
         var2 = null;
      }

      return var2;
   }

   ActionMenuPresenter getOuterActionMenuPresenter() {
      return this.mOuterActionMenuPresenter;
   }

   public Drawable getOverflowIcon() {
      this.ensureMenu();
      return this.mMenuView.getOverflowIcon();
   }

   Context getPopupContext() {
      return this.mPopupContext;
   }

   public int getPopupTheme() {
      return this.mPopupTheme;
   }

   public CharSequence getSubtitle() {
      return this.mSubtitleText;
   }

   public CharSequence getTitle() {
      return this.mTitleText;
   }

   public int getTitleMarginBottom() {
      return this.mTitleMarginBottom;
   }

   public int getTitleMarginEnd() {
      return this.mTitleMarginEnd;
   }

   public int getTitleMarginStart() {
      return this.mTitleMarginStart;
   }

   public int getTitleMarginTop() {
      return this.mTitleMarginTop;
   }

   public DecorToolbar getWrapper() {
      if (this.mWrapper == null) {
         this.mWrapper = new ToolbarWidgetWrapper(this, true);
      }

      return this.mWrapper;
   }

   public boolean isOverflowMenuShowing() {
      ActionMenuView var1 = this.mMenuView;
      boolean var2;
      if (var1 != null && var1.isOverflowMenuShowing()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.removeCallbacks(this.mShowOverflowMenuRunnable);
   }

   public boolean onHoverEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      if (var2 == 9) {
         this.mEatingHover = false;
      }

      if (!this.mEatingHover) {
         boolean var3 = super.onHoverEvent(var1);
         if (var2 == 9 && !var3) {
            this.mEatingHover = true;
         }
      }

      if (var2 == 10 || var2 == 3) {
         this.mEatingHover = false;
      }

      return true;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      boolean var6;
      if (ViewCompat.getLayoutDirection(this) == 1) {
         var6 = true;
      } else {
         var6 = false;
      }

      int var7 = this.getWidth();
      int var8 = this.getHeight();
      int var9 = this.getPaddingLeft();
      int var10 = this.getPaddingRight();
      int var11 = this.getPaddingTop();
      int var12 = this.getPaddingBottom();
      int var13 = var7 - var10;
      int[] var14 = this.mTempMargins;
      var14[1] = 0;
      var14[0] = 0;
      var2 = ViewCompat.getMinimumHeight(this);
      if (var2 >= 0) {
         var5 = Math.min(var2, var5 - var3);
      } else {
         var5 = 0;
      }

      int var15;
      label190: {
         if (this.shouldLayout(this.mNavButtonView)) {
            if (var6) {
               var15 = this.layoutChildRight(this.mNavButtonView, var13, var14, var5);
               var4 = var9;
               break label190;
            }

            var4 = this.layoutChildLeft(this.mNavButtonView, var9, var14, var5);
         } else {
            var4 = var9;
         }

         var15 = var13;
      }

      var2 = var4;
      var3 = var15;
      if (this.shouldLayout(this.mCollapseButtonView)) {
         if (var6) {
            var3 = this.layoutChildRight(this.mCollapseButtonView, var15, var14, var5);
            var2 = var4;
         } else {
            var2 = this.layoutChildLeft(this.mCollapseButtonView, var4, var14, var5);
            var3 = var15;
         }
      }

      var15 = var2;
      var4 = var3;
      if (this.shouldLayout(this.mMenuView)) {
         if (var6) {
            var15 = this.layoutChildLeft(this.mMenuView, var2, var14, var5);
            var4 = var3;
         } else {
            var4 = this.layoutChildRight(this.mMenuView, var3, var14, var5);
            var15 = var2;
         }
      }

      var3 = this.getCurrentContentInsetLeft();
      var2 = this.getCurrentContentInsetRight();
      var14[0] = Math.max(0, var3 - var15);
      var14[1] = Math.max(0, var2 - (var13 - var4));
      var3 = Math.max(var15, var3);
      var4 = Math.min(var4, var13 - var2);
      var2 = var3;
      var15 = var4;
      if (this.shouldLayout(this.mExpandedActionView)) {
         if (var6) {
            var15 = this.layoutChildRight(this.mExpandedActionView, var4, var14, var5);
            var2 = var3;
         } else {
            var2 = this.layoutChildLeft(this.mExpandedActionView, var3, var14, var5);
            var15 = var4;
         }
      }

      var4 = var2;
      var3 = var15;
      if (this.shouldLayout(this.mLogoView)) {
         if (var6) {
            var3 = this.layoutChildRight(this.mLogoView, var15, var14, var5);
            var4 = var2;
         } else {
            var4 = this.layoutChildLeft(this.mLogoView, var2, var14, var5);
            var3 = var15;
         }
      }

      boolean var16 = this.shouldLayout(this.mTitleTextView);
      var1 = this.shouldLayout(this.mSubtitleTextView);
      Toolbar.LayoutParams var17;
      if (var16) {
         var17 = (Toolbar.LayoutParams)this.mTitleTextView.getLayoutParams();
         var2 = var17.topMargin + this.mTitleTextView.getMeasuredHeight() + var17.bottomMargin + 0;
      } else {
         var2 = 0;
      }

      if (var1) {
         var17 = (Toolbar.LayoutParams)this.mSubtitleTextView.getLayoutParams();
         var2 += var17.topMargin + this.mSubtitleTextView.getMeasuredHeight() + var17.bottomMargin;
      }

      int var21;
      label199: {
         if (!var16 && !var1) {
            var2 = var4;
         } else {
            TextView var23;
            if (var16) {
               var23 = this.mTitleTextView;
            } else {
               var23 = this.mSubtitleTextView;
            }

            TextView var18;
            if (var1) {
               var18 = this.mSubtitleTextView;
            } else {
               var18 = this.mTitleTextView;
            }

            var17 = (Toolbar.LayoutParams)var23.getLayoutParams();
            Toolbar.LayoutParams var25 = (Toolbar.LayoutParams)var18.getLayoutParams();
            boolean var24;
            if ((!var16 || this.mTitleTextView.getMeasuredWidth() <= 0) && (!var1 || this.mSubtitleTextView.getMeasuredWidth() <= 0)) {
               var24 = false;
            } else {
               var24 = true;
            }

            var13 = this.mGravity & 112;
            if (var13 != 48) {
               if (var13 != 80) {
                  var13 = (var8 - var11 - var12 - var2) / 2;
                  int var19 = var17.topMargin;
                  int var20 = this.mTitleMarginTop;
                  if (var13 < var19 + var20) {
                     var2 = var19 + var20;
                  } else {
                     var8 = var8 - var12 - var2 - var13 - var11;
                     var19 = var17.bottomMargin;
                     var12 = this.mTitleMarginBottom;
                     var2 = var13;
                     if (var8 < var19 + var12) {
                        var2 = Math.max(0, var13 - (var25.bottomMargin + var12 - var8));
                     }
                  }

                  var2 += var11;
               } else {
                  var2 = var8 - var12 - var25.bottomMargin - this.mTitleMarginBottom - var2;
               }
            } else {
               var2 = this.getPaddingTop() + var17.topMargin + this.mTitleMarginTop;
            }

            if (!var6) {
               if (var24) {
                  var21 = this.mTitleMarginStart;
               } else {
                  var21 = 0;
               }

               var21 -= var14[0];
               var4 += Math.max(0, var21);
               var14[0] = Math.max(0, -var21);
               if (var16) {
                  var17 = (Toolbar.LayoutParams)this.mTitleTextView.getLayoutParams();
                  var21 = this.mTitleTextView.getMeasuredWidth() + var4;
                  var13 = this.mTitleTextView.getMeasuredHeight() + var2;
                  this.mTitleTextView.layout(var4, var2, var21, var13);
                  var21 += this.mTitleMarginEnd;
                  var2 = var13 + var17.bottomMargin;
               } else {
                  var21 = var4;
               }

               if (var1) {
                  var17 = (Toolbar.LayoutParams)this.mSubtitleTextView.getLayoutParams();
                  var13 = var2 + var17.topMargin;
                  var2 = this.mSubtitleTextView.getMeasuredWidth() + var4;
                  var11 = this.mSubtitleTextView.getMeasuredHeight();
                  this.mSubtitleTextView.layout(var4, var13, var2, var11 + var13);
                  var13 = var2 + this.mTitleMarginEnd;
                  var2 = var17.bottomMargin;
               } else {
                  var13 = var4;
               }

               var2 = var4;
               var4 = var3;
               if (var24) {
                  var2 = Math.max(var21, var13);
                  var4 = var3;
               }
               break label199;
            }

            if (var24) {
               var21 = this.mTitleMarginStart;
            } else {
               var21 = 0;
            }

            var21 -= var14[1];
            var3 -= Math.max(0, var21);
            var14[1] = Math.max(0, -var21);
            if (var16) {
               var17 = (Toolbar.LayoutParams)this.mTitleTextView.getLayoutParams();
               var13 = var3 - this.mTitleTextView.getMeasuredWidth();
               var21 = this.mTitleTextView.getMeasuredHeight() + var2;
               this.mTitleTextView.layout(var13, var2, var3, var21);
               var2 = var13 - this.mTitleMarginEnd;
               var21 += var17.bottomMargin;
            } else {
               var21 = var2;
               var2 = var3;
            }

            if (var1) {
               var17 = (Toolbar.LayoutParams)this.mSubtitleTextView.getLayoutParams();
               var21 += var17.topMargin;
               var13 = this.mSubtitleTextView.getMeasuredWidth();
               var11 = this.mSubtitleTextView.getMeasuredHeight();
               this.mSubtitleTextView.layout(var3 - var13, var21, var3, var11 + var21);
               var21 = var3 - this.mTitleMarginEnd;
               var13 = var17.bottomMargin;
            } else {
               var21 = var3;
            }

            if (var24) {
               var3 = Math.min(var2, var21);
            }

            var2 = var4;
         }

         var4 = var3;
      }

      byte var22 = 0;
      this.addCustomViewsWithGravity(this.mTempViews, 3);
      var21 = this.mTempViews.size();

      for(var3 = 0; var3 < var21; ++var3) {
         var2 = this.layoutChildLeft((View)this.mTempViews.get(var3), var2, var14, var5);
      }

      this.addCustomViewsWithGravity(this.mTempViews, 5);
      var21 = this.mTempViews.size();

      for(var3 = 0; var3 < var21; ++var3) {
         var4 = this.layoutChildRight((View)this.mTempViews.get(var3), var4, var14, var5);
      }

      this.addCustomViewsWithGravity(this.mTempViews, 1);
      var21 = this.getViewListMeasuredWidth(this.mTempViews, var14);
      var3 = var9 + (var7 - var9 - var10) / 2 - var21 / 2;
      var15 = var21 + var3;
      if (var3 >= var2) {
         if (var15 > var4) {
            var2 = var3 - (var15 - var4);
         } else {
            var2 = var3;
         }
      }

      var4 = this.mTempViews.size();

      for(var3 = var22; var3 < var4; ++var3) {
         var2 = this.layoutChildLeft((View)this.mTempViews.get(var3), var2, var14, var5);
      }

      this.mTempViews.clear();
   }

   protected void onMeasure(int var1, int var2) {
      int[] var3 = this.mTempMargins;
      byte var4;
      byte var5;
      if (ViewUtils.isLayoutRtl(this)) {
         var4 = 1;
         var5 = 0;
      } else {
         var4 = 0;
         var5 = 1;
      }

      int var6;
      int var7;
      int var8;
      if (this.shouldLayout(this.mNavButtonView)) {
         this.measureChildConstrained(this.mNavButtonView, var1, 0, var2, 0, this.mMaxButtonHeight);
         var6 = this.mNavButtonView.getMeasuredWidth() + this.getHorizontalMargins(this.mNavButtonView);
         var7 = Math.max(0, this.mNavButtonView.getMeasuredHeight() + this.getVerticalMargins(this.mNavButtonView));
         var8 = View.combineMeasuredStates(0, this.mNavButtonView.getMeasuredState());
      } else {
         var6 = 0;
         var7 = 0;
         var8 = 0;
      }

      int var9 = var6;
      int var10 = var7;
      var6 = var8;
      if (this.shouldLayout(this.mCollapseButtonView)) {
         this.measureChildConstrained(this.mCollapseButtonView, var1, 0, var2, 0, this.mMaxButtonHeight);
         var9 = this.mCollapseButtonView.getMeasuredWidth() + this.getHorizontalMargins(this.mCollapseButtonView);
         var10 = Math.max(var7, this.mCollapseButtonView.getMeasuredHeight() + this.getVerticalMargins(this.mCollapseButtonView));
         var6 = View.combineMeasuredStates(var8, this.mCollapseButtonView.getMeasuredState());
      }

      var8 = this.getCurrentContentInsetStart();
      int var11 = 0 + Math.max(var8, var9);
      var3[var4] = Math.max(0, var8 - var9);
      if (this.shouldLayout(this.mMenuView)) {
         this.measureChildConstrained(this.mMenuView, var1, var11, var2, 0, this.mMaxButtonHeight);
         var8 = this.mMenuView.getMeasuredWidth() + this.getHorizontalMargins(this.mMenuView);
         var7 = Math.max(var10, this.mMenuView.getMeasuredHeight() + this.getVerticalMargins(this.mMenuView));
         var6 = View.combineMeasuredStates(var6, this.mMenuView.getMeasuredState());
         var10 = var8;
         var8 = var7;
      } else {
         byte var16 = 0;
         var8 = var10;
         var10 = var16;
      }

      var7 = this.getCurrentContentInsetEnd();
      int var14 = var11 + Math.max(var7, var10);
      var3[var5] = Math.max(0, var7 - var10);
      int var15 = var14;
      var7 = var8;
      var10 = var6;
      if (this.shouldLayout(this.mExpandedActionView)) {
         var15 = var14 + this.measureChildCollapseMargins(this.mExpandedActionView, var1, var14, var2, 0, var3);
         var7 = Math.max(var8, this.mExpandedActionView.getMeasuredHeight() + this.getVerticalMargins(this.mExpandedActionView));
         var10 = View.combineMeasuredStates(var6, this.mExpandedActionView.getMeasuredState());
      }

      var8 = var15;
      var14 = var7;
      var6 = var10;
      if (this.shouldLayout(this.mLogoView)) {
         var8 = var15 + this.measureChildCollapseMargins(this.mLogoView, var1, var15, var2, 0, var3);
         var14 = Math.max(var7, this.mLogoView.getMeasuredHeight() + this.getVerticalMargins(this.mLogoView));
         var6 = View.combineMeasuredStates(var10, this.mLogoView.getMeasuredState());
      }

      var11 = this.getChildCount();
      var15 = var14;
      var10 = 0;

      for(var7 = var8; var10 < var11; var15 = var8) {
         View var12 = this.getChildAt(var10);
         var9 = var7;
         var14 = var6;
         var8 = var15;
         if (((Toolbar.LayoutParams)var12.getLayoutParams()).mViewType == 0) {
            if (!this.shouldLayout(var12)) {
               var9 = var7;
               var14 = var6;
               var8 = var15;
            } else {
               var9 = var7 + this.measureChildCollapseMargins(var12, var1, var7, var2, 0, var3);
               var8 = Math.max(var15, var12.getMeasuredHeight() + this.getVerticalMargins(var12));
               var14 = View.combineMeasuredStates(var6, var12.getMeasuredState());
            }
         }

         ++var10;
         var7 = var9;
         var6 = var14;
      }

      var14 = this.mTitleMarginTop + this.mTitleMarginBottom;
      var9 = this.mTitleMarginStart + this.mTitleMarginEnd;
      if (this.shouldLayout(this.mTitleTextView)) {
         this.measureChildCollapseMargins(this.mTitleTextView, var1, var7 + var9, var2, var14, var3);
         var10 = this.mTitleTextView.getMeasuredWidth();
         var11 = this.getHorizontalMargins(this.mTitleTextView);
         var8 = this.mTitleTextView.getMeasuredHeight();
         int var13 = this.getVerticalMargins(this.mTitleTextView);
         var6 = View.combineMeasuredStates(var6, this.mTitleTextView.getMeasuredState());
         var8 += var13;
         var10 += var11;
      } else {
         var10 = 0;
         var8 = 0;
      }

      if (this.shouldLayout(this.mSubtitleTextView)) {
         var10 = Math.max(var10, this.measureChildCollapseMargins(this.mSubtitleTextView, var1, var7 + var9, var2, var8 + var14, var3));
         var8 += this.mSubtitleTextView.getMeasuredHeight() + this.getVerticalMargins(this.mSubtitleTextView);
         var6 = View.combineMeasuredStates(var6, this.mSubtitleTextView.getMeasuredState());
      }

      var15 = Math.max(var15, var8);
      var11 = this.getPaddingLeft();
      var9 = this.getPaddingRight();
      var14 = this.getPaddingTop();
      var8 = this.getPaddingBottom();
      var10 = View.resolveSizeAndState(Math.max(var7 + var10 + var11 + var9, this.getSuggestedMinimumWidth()), var1, -16777216 & var6);
      var1 = View.resolveSizeAndState(Math.max(var15 + var14 + var8, this.getSuggestedMinimumHeight()), var2, var6 << 16);
      if (this.shouldCollapse()) {
         var1 = 0;
      }

      this.setMeasuredDimension(var10, var1);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof Toolbar.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         Toolbar.SavedState var2 = (Toolbar.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         ActionMenuView var4 = this.mMenuView;
         MenuBuilder var5;
         if (var4 != null) {
            var5 = var4.peekMenu();
         } else {
            var5 = null;
         }

         int var3 = var2.expandedMenuItemId;
         if (var3 != 0 && this.mExpandedMenuPresenter != null && var5 != null) {
            MenuItem var6 = var5.findItem(var3);
            if (var6 != null) {
               var6.expandActionView();
            }
         }

         if (var2.isOverflowOpen) {
            this.postShowOverflowMenu();
         }

      }
   }

   public void onRtlPropertiesChanged(int var1) {
      if (VERSION.SDK_INT >= 17) {
         super.onRtlPropertiesChanged(var1);
      }

      this.ensureContentInsets();
      RtlSpacingHelper var2 = this.mContentInsets;
      boolean var3 = true;
      if (var1 != 1) {
         var3 = false;
      }

      var2.setDirection(var3);
   }

   protected Parcelable onSaveInstanceState() {
      Toolbar.SavedState var1 = new Toolbar.SavedState(super.onSaveInstanceState());
      Toolbar.ExpandedActionViewMenuPresenter var2 = this.mExpandedMenuPresenter;
      if (var2 != null) {
         MenuItemImpl var3 = var2.mCurrentExpandedItem;
         if (var3 != null) {
            var1.expandedMenuItemId = var3.getItemId();
         }
      }

      var1.isOverflowOpen = this.isOverflowMenuShowing();
      return var1;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      if (var2 == 0) {
         this.mEatingTouch = false;
      }

      if (!this.mEatingTouch) {
         boolean var3 = super.onTouchEvent(var1);
         if (var2 == 0 && !var3) {
            this.mEatingTouch = true;
         }
      }

      if (var2 == 1 || var2 == 3) {
         this.mEatingTouch = false;
      }

      return true;
   }

   void removeChildrenForExpandedActionView() {
      for(int var1 = this.getChildCount() - 1; var1 >= 0; --var1) {
         View var2 = this.getChildAt(var1);
         if (((Toolbar.LayoutParams)var2.getLayoutParams()).mViewType != 2 && var2 != this.mMenuView) {
            this.removeViewAt(var1);
            this.mHiddenViews.add(var2);
         }
      }

   }

   public void setCollapsible(boolean var1) {
      this.mCollapsible = var1;
      this.requestLayout();
   }

   public void setContentInsetEndWithActions(int var1) {
      int var2 = var1;
      if (var1 < 0) {
         var2 = Integer.MIN_VALUE;
      }

      if (var2 != this.mContentInsetEndWithActions) {
         this.mContentInsetEndWithActions = var2;
         if (this.getNavigationIcon() != null) {
            this.requestLayout();
         }
      }

   }

   public void setContentInsetStartWithNavigation(int var1) {
      int var2 = var1;
      if (var1 < 0) {
         var2 = Integer.MIN_VALUE;
      }

      if (var2 != this.mContentInsetStartWithNavigation) {
         this.mContentInsetStartWithNavigation = var2;
         if (this.getNavigationIcon() != null) {
            this.requestLayout();
         }
      }

   }

   public void setContentInsetsRelative(int var1, int var2) {
      this.ensureContentInsets();
      this.mContentInsets.setRelative(var1, var2);
   }

   public void setLogo(int var1) {
      this.setLogo(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setLogo(Drawable var1) {
      ImageView var2;
      if (var1 != null) {
         this.ensureLogoView();
         if (!this.isChildOrHidden(this.mLogoView)) {
            this.addSystemView(this.mLogoView, true);
         }
      } else {
         var2 = this.mLogoView;
         if (var2 != null && this.isChildOrHidden(var2)) {
            this.removeView(this.mLogoView);
            this.mHiddenViews.remove(this.mLogoView);
         }
      }

      var2 = this.mLogoView;
      if (var2 != null) {
         var2.setImageDrawable(var1);
      }

   }

   public void setLogoDescription(int var1) {
      this.setLogoDescription(this.getContext().getText(var1));
   }

   public void setLogoDescription(CharSequence var1) {
      if (!TextUtils.isEmpty(var1)) {
         this.ensureLogoView();
      }

      ImageView var2 = this.mLogoView;
      if (var2 != null) {
         var2.setContentDescription(var1);
      }

   }

   public void setNavigationContentDescription(int var1) {
      CharSequence var2;
      if (var1 != 0) {
         var2 = this.getContext().getText(var1);
      } else {
         var2 = null;
      }

      this.setNavigationContentDescription(var2);
   }

   public void setNavigationContentDescription(CharSequence var1) {
      if (!TextUtils.isEmpty(var1)) {
         this.ensureNavButtonView();
      }

      ImageButton var2 = this.mNavButtonView;
      if (var2 != null) {
         var2.setContentDescription(var1);
      }

   }

   public void setNavigationIcon(int var1) {
      this.setNavigationIcon(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setNavigationIcon(Drawable var1) {
      ImageButton var2;
      if (var1 != null) {
         this.ensureNavButtonView();
         if (!this.isChildOrHidden(this.mNavButtonView)) {
            this.addSystemView(this.mNavButtonView, true);
         }
      } else {
         var2 = this.mNavButtonView;
         if (var2 != null && this.isChildOrHidden(var2)) {
            this.removeView(this.mNavButtonView);
            this.mHiddenViews.remove(this.mNavButtonView);
         }
      }

      var2 = this.mNavButtonView;
      if (var2 != null) {
         var2.setImageDrawable(var1);
      }

   }

   public void setNavigationOnClickListener(OnClickListener var1) {
      this.ensureNavButtonView();
      this.mNavButtonView.setOnClickListener(var1);
   }

   public void setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener var1) {
      this.mOnMenuItemClickListener = var1;
   }

   public void setOverflowIcon(Drawable var1) {
      this.ensureMenu();
      this.mMenuView.setOverflowIcon(var1);
   }

   public void setPopupTheme(int var1) {
      if (this.mPopupTheme != var1) {
         this.mPopupTheme = var1;
         if (var1 == 0) {
            this.mPopupContext = this.getContext();
         } else {
            this.mPopupContext = new ContextThemeWrapper(this.getContext(), var1);
         }
      }

   }

   public void setSubtitle(int var1) {
      this.setSubtitle(this.getContext().getText(var1));
   }

   public void setSubtitle(CharSequence var1) {
      TextView var4;
      if (!TextUtils.isEmpty(var1)) {
         if (this.mSubtitleTextView == null) {
            Context var2 = this.getContext();
            this.mSubtitleTextView = new AppCompatTextView(var2);
            this.mSubtitleTextView.setSingleLine();
            this.mSubtitleTextView.setEllipsize(TruncateAt.END);
            int var3 = this.mSubtitleTextAppearance;
            if (var3 != 0) {
               this.mSubtitleTextView.setTextAppearance(var2, var3);
            }

            var3 = this.mSubtitleTextColor;
            if (var3 != 0) {
               this.mSubtitleTextView.setTextColor(var3);
            }
         }

         if (!this.isChildOrHidden(this.mSubtitleTextView)) {
            this.addSystemView(this.mSubtitleTextView, true);
         }
      } else {
         var4 = this.mSubtitleTextView;
         if (var4 != null && this.isChildOrHidden(var4)) {
            this.removeView(this.mSubtitleTextView);
            this.mHiddenViews.remove(this.mSubtitleTextView);
         }
      }

      var4 = this.mSubtitleTextView;
      if (var4 != null) {
         var4.setText(var1);
      }

      this.mSubtitleText = var1;
   }

   public void setSubtitleTextAppearance(Context var1, int var2) {
      this.mSubtitleTextAppearance = var2;
      TextView var3 = this.mSubtitleTextView;
      if (var3 != null) {
         var3.setTextAppearance(var1, var2);
      }

   }

   public void setSubtitleTextColor(int var1) {
      this.mSubtitleTextColor = var1;
      TextView var2 = this.mSubtitleTextView;
      if (var2 != null) {
         var2.setTextColor(var1);
      }

   }

   public void setTitle(int var1) {
      this.setTitle(this.getContext().getText(var1));
   }

   public void setTitle(CharSequence var1) {
      TextView var4;
      if (!TextUtils.isEmpty(var1)) {
         if (this.mTitleTextView == null) {
            Context var2 = this.getContext();
            this.mTitleTextView = new AppCompatTextView(var2);
            this.mTitleTextView.setSingleLine();
            this.mTitleTextView.setEllipsize(TruncateAt.END);
            int var3 = this.mTitleTextAppearance;
            if (var3 != 0) {
               this.mTitleTextView.setTextAppearance(var2, var3);
            }

            var3 = this.mTitleTextColor;
            if (var3 != 0) {
               this.mTitleTextView.setTextColor(var3);
            }
         }

         if (!this.isChildOrHidden(this.mTitleTextView)) {
            this.addSystemView(this.mTitleTextView, true);
         }
      } else {
         var4 = this.mTitleTextView;
         if (var4 != null && this.isChildOrHidden(var4)) {
            this.removeView(this.mTitleTextView);
            this.mHiddenViews.remove(this.mTitleTextView);
         }
      }

      var4 = this.mTitleTextView;
      if (var4 != null) {
         var4.setText(var1);
      }

      this.mTitleText = var1;
   }

   public void setTitleMarginBottom(int var1) {
      this.mTitleMarginBottom = var1;
      this.requestLayout();
   }

   public void setTitleMarginEnd(int var1) {
      this.mTitleMarginEnd = var1;
      this.requestLayout();
   }

   public void setTitleMarginStart(int var1) {
      this.mTitleMarginStart = var1;
      this.requestLayout();
   }

   public void setTitleMarginTop(int var1) {
      this.mTitleMarginTop = var1;
      this.requestLayout();
   }

   public void setTitleTextAppearance(Context var1, int var2) {
      this.mTitleTextAppearance = var2;
      TextView var3 = this.mTitleTextView;
      if (var3 != null) {
         var3.setTextAppearance(var1, var2);
      }

   }

   public void setTitleTextColor(int var1) {
      this.mTitleTextColor = var1;
      TextView var2 = this.mTitleTextView;
      if (var2 != null) {
         var2.setTextColor(var1);
      }

   }

   public boolean showOverflowMenu() {
      ActionMenuView var1 = this.mMenuView;
      boolean var2;
      if (var1 != null && var1.showOverflowMenu()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private class ExpandedActionViewMenuPresenter implements MenuPresenter {
      MenuItemImpl mCurrentExpandedItem;
      MenuBuilder mMenu;

      ExpandedActionViewMenuPresenter() {
      }

      public boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2) {
         View var3 = Toolbar.this.mExpandedActionView;
         if (var3 instanceof CollapsibleActionView) {
            ((CollapsibleActionView)var3).onActionViewCollapsed();
         }

         Toolbar var4 = Toolbar.this;
         var4.removeView(var4.mExpandedActionView);
         var4 = Toolbar.this;
         var4.removeView(var4.mCollapseButtonView);
         var4 = Toolbar.this;
         var4.mExpandedActionView = null;
         var4.addChildrenForExpandedActionView();
         this.mCurrentExpandedItem = null;
         Toolbar.this.requestLayout();
         var2.setActionViewExpanded(false);
         return true;
      }

      public boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2) {
         Toolbar.this.ensureCollapseButtonView();
         ViewParent var4 = Toolbar.this.mCollapseButtonView.getParent();
         Toolbar var3 = Toolbar.this;
         Toolbar var5;
         if (var4 != var3) {
            if (var4 instanceof ViewGroup) {
               ((ViewGroup)var4).removeView(var3.mCollapseButtonView);
            }

            var5 = Toolbar.this;
            var5.addView(var5.mCollapseButtonView);
         }

         Toolbar.this.mExpandedActionView = var2.getActionView();
         this.mCurrentExpandedItem = var2;
         ViewParent var8 = Toolbar.this.mExpandedActionView.getParent();
         var5 = Toolbar.this;
         if (var8 != var5) {
            if (var8 instanceof ViewGroup) {
               ((ViewGroup)var8).removeView(var5.mExpandedActionView);
            }

            Toolbar.LayoutParams var6 = Toolbar.this.generateDefaultLayoutParams();
            var3 = Toolbar.this;
            var6.gravity = 8388611 | var3.mButtonGravity & 112;
            var6.mViewType = 2;
            var3.mExpandedActionView.setLayoutParams(var6);
            var5 = Toolbar.this;
            var5.addView(var5.mExpandedActionView);
         }

         Toolbar.this.removeChildrenForExpandedActionView();
         Toolbar.this.requestLayout();
         var2.setActionViewExpanded(true);
         View var7 = Toolbar.this.mExpandedActionView;
         if (var7 instanceof CollapsibleActionView) {
            ((CollapsibleActionView)var7).onActionViewExpanded();
         }

         return true;
      }

      public boolean flagActionItems() {
         return false;
      }

      public void initForMenu(Context var1, MenuBuilder var2) {
         MenuBuilder var4 = this.mMenu;
         if (var4 != null) {
            MenuItemImpl var3 = this.mCurrentExpandedItem;
            if (var3 != null) {
               var4.collapseItemActionView(var3);
            }
         }

         this.mMenu = var2;
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
      }

      public boolean onSubMenuSelected(SubMenuBuilder var1) {
         return false;
      }

      public void updateMenuView(boolean var1) {
         if (this.mCurrentExpandedItem != null) {
            MenuBuilder var2 = this.mMenu;
            boolean var3 = false;
            boolean var4 = var3;
            if (var2 != null) {
               int var5 = var2.size();
               int var6 = 0;

               while(true) {
                  var4 = var3;
                  if (var6 >= var5) {
                     break;
                  }

                  if (this.mMenu.getItem(var6) == this.mCurrentExpandedItem) {
                     var4 = true;
                     break;
                  }

                  ++var6;
               }
            }

            if (!var4) {
               this.collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
            }
         }

      }
   }

   public static class LayoutParams extends ActionBar$LayoutParams {
      int mViewType = 0;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         super.gravity = 8388627;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super((android.view.ViewGroup.LayoutParams)var1);
         this.copyMarginsFromCompat(var1);
      }

      public LayoutParams(ActionBar$LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(Toolbar.LayoutParams var1) {
         super((ActionBar$LayoutParams)var1);
         this.mViewType = var1.mViewType;
      }

      void copyMarginsFromCompat(MarginLayoutParams var1) {
         super.leftMargin = var1.leftMargin;
         super.topMargin = var1.topMargin;
         super.rightMargin = var1.rightMargin;
         super.bottomMargin = var1.bottomMargin;
      }
   }

   public interface OnMenuItemClickListener {
      boolean onMenuItemClick(MenuItem var1);
   }

   public static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public Toolbar.SavedState createFromParcel(Parcel var1) {
            return new Toolbar.SavedState(var1, (ClassLoader)null);
         }

         public Toolbar.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new Toolbar.SavedState(var1, var2);
         }

         public Toolbar.SavedState[] newArray(int var1) {
            return new Toolbar.SavedState[var1];
         }
      };
      int expandedMenuItemId;
      boolean isOverflowOpen;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.expandedMenuItemId = var1.readInt();
         boolean var3;
         if (var1.readInt() != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.isOverflowOpen = var3;
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.expandedMenuItemId);
         var1.writeInt(this.isOverflowOpen);
      }
   }
}
