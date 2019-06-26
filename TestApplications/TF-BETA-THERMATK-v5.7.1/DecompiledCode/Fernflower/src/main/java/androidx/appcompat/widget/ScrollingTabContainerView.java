package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import androidx.appcompat.R$attr;
import androidx.appcompat.app.ActionBar$Tab;
import androidx.appcompat.view.ActionBarPolicy;

public class ScrollingTabContainerView extends HorizontalScrollView implements OnItemSelectedListener {
   private static final Interpolator sAlphaInterpolator = new DecelerateInterpolator();
   private boolean mAllowCollapse;
   private int mContentHeight;
   int mMaxTabWidth;
   private int mSelectedTabIndex;
   int mStackedTabMaxWidth;
   private ScrollingTabContainerView.TabClickListener mTabClickListener;
   LinearLayoutCompat mTabLayout;
   Runnable mTabSelector;
   private Spinner mTabSpinner;

   private Spinner createSpinner() {
      AppCompatSpinner var1 = new AppCompatSpinner(this.getContext(), (AttributeSet)null, R$attr.actionDropDownStyle);
      var1.setLayoutParams(new LinearLayoutCompat.LayoutParams(-2, -1));
      var1.setOnItemSelectedListener(this);
      return var1;
   }

   private boolean isCollapsed() {
      Spinner var1 = this.mTabSpinner;
      boolean var2;
      if (var1 != null && var1.getParent() == this) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private void performCollapse() {
      if (!this.isCollapsed()) {
         if (this.mTabSpinner == null) {
            this.mTabSpinner = this.createSpinner();
         }

         this.removeView(this.mTabLayout);
         this.addView(this.mTabSpinner, new LayoutParams(-2, -1));
         if (this.mTabSpinner.getAdapter() == null) {
            this.mTabSpinner.setAdapter(new ScrollingTabContainerView.TabAdapter());
         }

         Runnable var1 = this.mTabSelector;
         if (var1 != null) {
            this.removeCallbacks(var1);
            this.mTabSelector = null;
         }

         this.mTabSpinner.setSelection(this.mSelectedTabIndex);
      }
   }

   private boolean performExpand() {
      if (!this.isCollapsed()) {
         return false;
      } else {
         this.removeView(this.mTabSpinner);
         this.addView(this.mTabLayout, new LayoutParams(-2, -1));
         this.setTabSelected(this.mTabSpinner.getSelectedItemPosition());
         return false;
      }
   }

   public void animateToTab(int var1) {
      final View var2 = this.mTabLayout.getChildAt(var1);
      Runnable var3 = this.mTabSelector;
      if (var3 != null) {
         this.removeCallbacks(var3);
      }

      this.mTabSelector = new Runnable() {
         public void run() {
            int var1 = var2.getLeft();
            int var2x = (ScrollingTabContainerView.this.getWidth() - var2.getWidth()) / 2;
            ScrollingTabContainerView.this.smoothScrollTo(var1 - var2x, 0);
            ScrollingTabContainerView.this.mTabSelector = null;
         }
      };
      this.post(this.mTabSelector);
   }

   ScrollingTabContainerView.TabView createTabView(ActionBar$Tab var1, boolean var2) {
      ScrollingTabContainerView.TabView var3 = new ScrollingTabContainerView.TabView(this.getContext(), var1, var2);
      if (var2) {
         var3.setBackgroundDrawable((Drawable)null);
         var3.setLayoutParams(new android.widget.AbsListView.LayoutParams(-1, this.mContentHeight));
      } else {
         var3.setFocusable(true);
         if (this.mTabClickListener == null) {
            this.mTabClickListener = new ScrollingTabContainerView.TabClickListener();
         }

         var3.setOnClickListener(this.mTabClickListener);
      }

      return var3;
   }

   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      Runnable var1 = this.mTabSelector;
      if (var1 != null) {
         this.post(var1);
      }

   }

   protected void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      ActionBarPolicy var2 = ActionBarPolicy.get(this.getContext());
      this.setContentHeight(var2.getTabContainerHeight());
      this.mStackedTabMaxWidth = var2.getStackedTabMaxWidth();
   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      Runnable var1 = this.mTabSelector;
      if (var1 != null) {
         this.removeCallbacks(var1);
      }

   }

   public void onItemSelected(AdapterView var1, View var2, int var3, long var4) {
      ((ScrollingTabContainerView.TabView)var2).getTab().select();
   }

   public void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getMode(var1);
      boolean var6 = true;
      boolean var4;
      if (var3 == 1073741824) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.setFillViewport(var4);
      int var5 = this.mTabLayout.getChildCount();
      if (var5 <= 1 || var3 != 1073741824 && var3 != Integer.MIN_VALUE) {
         this.mMaxTabWidth = -1;
      } else {
         if (var5 > 2) {
            this.mMaxTabWidth = (int)((float)MeasureSpec.getSize(var1) * 0.4F);
         } else {
            this.mMaxTabWidth = MeasureSpec.getSize(var1) / 2;
         }

         this.mMaxTabWidth = Math.min(this.mMaxTabWidth, this.mStackedTabMaxWidth);
      }

      var3 = MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824);
      if (var4 || !this.mAllowCollapse) {
         var6 = false;
      }

      if (var6) {
         this.mTabLayout.measure(0, var3);
         if (this.mTabLayout.getMeasuredWidth() > MeasureSpec.getSize(var1)) {
            this.performCollapse();
         } else {
            this.performExpand();
         }
      } else {
         this.performExpand();
      }

      var2 = this.getMeasuredWidth();
      super.onMeasure(var1, var3);
      var1 = this.getMeasuredWidth();
      if (var4 && var2 != var1) {
         this.setTabSelected(this.mSelectedTabIndex);
      }

   }

   public void onNothingSelected(AdapterView var1) {
   }

   public void setAllowCollapse(boolean var1) {
      this.mAllowCollapse = var1;
   }

   public void setContentHeight(int var1) {
      this.mContentHeight = var1;
      this.requestLayout();
   }

   public void setTabSelected(int var1) {
      this.mSelectedTabIndex = var1;
      int var2 = this.mTabLayout.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         View var4 = this.mTabLayout.getChildAt(var3);
         boolean var5;
         if (var3 == var1) {
            var5 = true;
         } else {
            var5 = false;
         }

         var4.setSelected(var5);
         if (var5) {
            this.animateToTab(var1);
         }
      }

      Spinner var6 = this.mTabSpinner;
      if (var6 != null && var1 >= 0) {
         var6.setSelection(var1);
      }

   }

   private class TabAdapter extends BaseAdapter {
      TabAdapter() {
      }

      public int getCount() {
         return ScrollingTabContainerView.this.mTabLayout.getChildCount();
      }

      public Object getItem(int var1) {
         return ((ScrollingTabContainerView.TabView)ScrollingTabContainerView.this.mTabLayout.getChildAt(var1)).getTab();
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public View getView(int var1, View var2, ViewGroup var3) {
         if (var2 == null) {
            var2 = ScrollingTabContainerView.this.createTabView((ActionBar$Tab)this.getItem(var1), true);
         } else {
            ((ScrollingTabContainerView.TabView)var2).bindTab((ActionBar$Tab)this.getItem(var1));
         }

         return (View)var2;
      }
   }

   private class TabClickListener implements OnClickListener {
      TabClickListener() {
      }

      public void onClick(View var1) {
         ((ScrollingTabContainerView.TabView)var1).getTab().select();
         int var2 = ScrollingTabContainerView.this.mTabLayout.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = ScrollingTabContainerView.this.mTabLayout.getChildAt(var3);
            boolean var5;
            if (var4 == var1) {
               var5 = true;
            } else {
               var5 = false;
            }

            var4.setSelected(var5);
         }

      }
   }

   private class TabView extends LinearLayout {
      private final int[] BG_ATTRS = new int[]{16842964};
      private View mCustomView;
      private ImageView mIconView;
      private ActionBar$Tab mTab;
      private TextView mTextView;

      public TabView(Context var2, ActionBar$Tab var3, boolean var4) {
         super(var2, (AttributeSet)null, R$attr.actionBarTabStyle);
         this.mTab = var3;
         TintTypedArray var5 = TintTypedArray.obtainStyledAttributes(var2, (AttributeSet)null, this.BG_ATTRS, R$attr.actionBarTabStyle, 0);
         if (var5.hasValue(0)) {
            this.setBackgroundDrawable(var5.getDrawable(0));
         }

         var5.recycle();
         if (var4) {
            this.setGravity(8388627);
         }

         this.update();
      }

      public void bindTab(ActionBar$Tab var1) {
         this.mTab = var1;
         this.update();
      }

      public ActionBar$Tab getTab() {
         return this.mTab;
      }

      public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
         super.onInitializeAccessibilityEvent(var1);
         var1.setClassName(ActionBar$Tab.class.getName());
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setClassName(ActionBar$Tab.class.getName());
      }

      public void onMeasure(int var1, int var2) {
         super.onMeasure(var1, var2);
         if (ScrollingTabContainerView.this.mMaxTabWidth > 0) {
            var1 = this.getMeasuredWidth();
            int var3 = ScrollingTabContainerView.this.mMaxTabWidth;
            if (var1 > var3) {
               super.onMeasure(MeasureSpec.makeMeasureSpec(var3, 1073741824), var2);
            }
         }

      }

      public void setSelected(boolean var1) {
         boolean var2;
         if (this.isSelected() != var1) {
            var2 = true;
         } else {
            var2 = false;
         }

         super.setSelected(var1);
         if (var2 && var1) {
            this.sendAccessibilityEvent(4);
         }

      }

      public void update() {
         ActionBar$Tab var1 = this.mTab;
         View var2 = var1.getCustomView();
         CharSequence var3 = null;
         if (var2 != null) {
            ViewParent var10 = var2.getParent();
            if (var10 != this) {
               if (var10 != null) {
                  ((ViewGroup)var10).removeView(var2);
               }

               this.addView(var2);
            }

            this.mCustomView = var2;
            TextView var12 = this.mTextView;
            if (var12 != null) {
               var12.setVisibility(8);
            }

            ImageView var13 = this.mIconView;
            if (var13 != null) {
               var13.setVisibility(8);
               this.mIconView.setImageDrawable((Drawable)null);
            }
         } else {
            var2 = this.mCustomView;
            if (var2 != null) {
               this.removeView(var2);
               this.mCustomView = null;
            }

            Drawable var4 = var1.getIcon();
            CharSequence var8 = var1.getText();
            if (var4 != null) {
               if (this.mIconView == null) {
                  AppCompatImageView var5 = new AppCompatImageView(this.getContext());
                  android.widget.LinearLayout.LayoutParams var6 = new android.widget.LinearLayout.LayoutParams(-2, -2);
                  var6.gravity = 16;
                  var5.setLayoutParams(var6);
                  this.addView(var5, 0);
                  this.mIconView = var5;
               }

               this.mIconView.setImageDrawable(var4);
               this.mIconView.setVisibility(0);
            } else {
               ImageView var15 = this.mIconView;
               if (var15 != null) {
                  var15.setVisibility(8);
                  this.mIconView.setImageDrawable((Drawable)null);
               }
            }

            boolean var7 = TextUtils.isEmpty(var8) ^ true;
            if (var7) {
               if (this.mTextView == null) {
                  AppCompatTextView var16 = new AppCompatTextView(this.getContext(), (AttributeSet)null, R$attr.actionBarTabTextStyle);
                  var16.setEllipsize(TruncateAt.END);
                  android.widget.LinearLayout.LayoutParams var14 = new android.widget.LinearLayout.LayoutParams(-2, -2);
                  var14.gravity = 16;
                  var16.setLayoutParams(var14);
                  this.addView(var16);
                  this.mTextView = var16;
               }

               this.mTextView.setText(var8);
               this.mTextView.setVisibility(0);
            } else {
               TextView var9 = this.mTextView;
               if (var9 != null) {
                  var9.setVisibility(8);
                  this.mTextView.setText((CharSequence)null);
               }
            }

            ImageView var11 = this.mIconView;
            if (var11 != null) {
               var11.setContentDescription(var1.getContentDescription());
            }

            if (!var7) {
               var3 = var1.getContentDescription();
            }

            TooltipCompat.setTooltipText(this, var3);
         }

      }
   }
}
