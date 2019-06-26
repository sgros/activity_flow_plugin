package androidx.appcompat.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$id;
import androidx.appcompat.R$layout;
import androidx.appcompat.R$styleable;
import androidx.core.view.ViewCompat;

public class ActionBarContextView extends AbsActionBarView {
   private View mClose;
   private int mCloseItemLayout;
   private View mCustomView;
   private CharSequence mSubtitle;
   private int mSubtitleStyleRes;
   private TextView mSubtitleView;
   private CharSequence mTitle;
   private LinearLayout mTitleLayout;
   private boolean mTitleOptional;
   private int mTitleStyleRes;
   private TextView mTitleView;

   public ActionBarContextView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ActionBarContextView(Context var1, AttributeSet var2) {
      this(var1, var2, R$attr.actionModeStyle);
   }

   public ActionBarContextView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(var1, var2, R$styleable.ActionMode, var3, 0);
      ViewCompat.setBackground(this, var4.getDrawable(R$styleable.ActionMode_background));
      this.mTitleStyleRes = var4.getResourceId(R$styleable.ActionMode_titleTextStyle, 0);
      this.mSubtitleStyleRes = var4.getResourceId(R$styleable.ActionMode_subtitleTextStyle, 0);
      super.mContentHeight = var4.getLayoutDimension(R$styleable.ActionMode_height, 0);
      this.mCloseItemLayout = var4.getResourceId(R$styleable.ActionMode_closeItemLayout, R$layout.abc_action_mode_close_item_material);
      var4.recycle();
   }

   private void initTitle() {
      if (this.mTitleLayout == null) {
         LayoutInflater.from(this.getContext()).inflate(R$layout.abc_action_bar_title_item, this);
         this.mTitleLayout = (LinearLayout)this.getChildAt(this.getChildCount() - 1);
         this.mTitleView = (TextView)this.mTitleLayout.findViewById(R$id.action_bar_title);
         this.mSubtitleView = (TextView)this.mTitleLayout.findViewById(R$id.action_bar_subtitle);
         if (this.mTitleStyleRes != 0) {
            this.mTitleView.setTextAppearance(this.getContext(), this.mTitleStyleRes);
         }

         if (this.mSubtitleStyleRes != 0) {
            this.mSubtitleView.setTextAppearance(this.getContext(), this.mSubtitleStyleRes);
         }
      }

      this.mTitleView.setText(this.mTitle);
      this.mSubtitleView.setText(this.mSubtitle);
      boolean var1 = TextUtils.isEmpty(this.mTitle);
      boolean var2 = TextUtils.isEmpty(this.mSubtitle) ^ true;
      TextView var3 = this.mSubtitleView;
      byte var4 = 0;
      byte var5;
      if (var2) {
         var5 = 0;
      } else {
         var5 = 8;
      }

      var3.setVisibility(var5);
      LinearLayout var6 = this.mTitleLayout;
      var5 = var4;
      if (!(var1 ^ true)) {
         if (var2) {
            var5 = var4;
         } else {
            var5 = 8;
         }
      }

      var6.setVisibility(var5);
      if (this.mTitleLayout.getParent() == null) {
         this.addView(this.mTitleLayout);
      }

   }

   protected LayoutParams generateDefaultLayoutParams() {
      return new MarginLayoutParams(-1, -2);
   }

   public LayoutParams generateLayoutParams(AttributeSet var1) {
      return new MarginLayoutParams(this.getContext(), var1);
   }

   public CharSequence getSubtitle() {
      return this.mSubtitle;
   }

   public CharSequence getTitle() {
      return this.mTitle;
   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      ActionMenuPresenter var1 = super.mActionMenuPresenter;
      if (var1 != null) {
         var1.hideOverflowMenu();
         super.mActionMenuPresenter.hideSubMenus();
      }

   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      if (var1.getEventType() == 32) {
         var1.setSource(this);
         var1.setClassName(ActionBarContextView.class.getName());
         var1.setPackageName(this.getContext().getPackageName());
         var1.setContentDescription(this.mTitle);
      } else {
         super.onInitializeAccessibilityEvent(var1);
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var1 = ViewUtils.isLayoutRtl(this);
      int var6;
      if (var1) {
         var6 = var4 - var2 - this.getPaddingRight();
      } else {
         var6 = this.getPaddingLeft();
      }

      int var7 = this.getPaddingTop();
      int var8 = var5 - var3 - this.getPaddingTop() - this.getPaddingBottom();
      View var9 = this.mClose;
      if (var9 != null && var9.getVisibility() != 8) {
         MarginLayoutParams var10 = (MarginLayoutParams)this.mClose.getLayoutParams();
         if (var1) {
            var3 = var10.rightMargin;
         } else {
            var3 = var10.leftMargin;
         }

         if (var1) {
            var5 = var10.leftMargin;
         } else {
            var5 = var10.rightMargin;
         }

         var3 = AbsActionBarView.next(var6, var3, var1);
         var3 = AbsActionBarView.next(var3 + this.positionChild(this.mClose, var3, var7, var8, var1), var5, var1);
      } else {
         var3 = var6;
      }

      LinearLayout var11 = this.mTitleLayout;
      var5 = var3;
      if (var11 != null) {
         var5 = var3;
         if (this.mCustomView == null) {
            var5 = var3;
            if (var11.getVisibility() != 8) {
               var5 = var3 + this.positionChild(this.mTitleLayout, var3, var7, var8, var1);
            }
         }
      }

      var9 = this.mCustomView;
      if (var9 != null) {
         this.positionChild(var9, var5, var7, var8, var1);
      }

      if (var1) {
         var2 = this.getPaddingLeft();
      } else {
         var2 = var4 - var2 - this.getPaddingRight();
      }

      ActionMenuView var12 = super.mMenuView;
      if (var12 != null) {
         this.positionChild(var12, var2, var7, var8, var1 ^ true);
      }

   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getMode(var1);
      int var4 = 1073741824;
      StringBuilder var9;
      if (var3 != 1073741824) {
         var9 = new StringBuilder();
         var9.append(ActionBarContextView.class.getSimpleName());
         var9.append(" can only be used ");
         var9.append("with android:layout_width=\"match_parent\" (or fill_parent)");
         throw new IllegalStateException(var9.toString());
      } else if (MeasureSpec.getMode(var2) != 0) {
         int var5 = MeasureSpec.getSize(var1);
         var3 = super.mContentHeight;
         if (var3 <= 0) {
            var3 = MeasureSpec.getSize(var2);
         }

         int var6 = this.getPaddingTop() + this.getPaddingBottom();
         var1 = var5 - this.getPaddingLeft() - this.getPaddingRight();
         int var7 = var3 - var6;
         int var8 = MeasureSpec.makeMeasureSpec(var7, Integer.MIN_VALUE);
         View var14 = this.mClose;
         byte var10 = 0;
         var2 = var1;
         if (var14 != null) {
            var1 = this.measureChildView(var14, var1, var8, 0);
            MarginLayoutParams var15 = (MarginLayoutParams)this.mClose.getLayoutParams();
            var2 = var1 - (var15.leftMargin + var15.rightMargin);
         }

         ActionMenuView var16 = super.mMenuView;
         var1 = var2;
         if (var16 != null) {
            var1 = var2;
            if (var16.getParent() == this) {
               var1 = this.measureChildView(super.mMenuView, var2, var8, 0);
            }
         }

         LinearLayout var17 = this.mTitleLayout;
         var2 = var1;
         int var11;
         if (var17 != null) {
            var2 = var1;
            if (this.mCustomView == null) {
               if (this.mTitleOptional) {
                  var2 = MeasureSpec.makeMeasureSpec(0, 0);
                  this.mTitleLayout.measure(var2, var8);
                  var11 = this.mTitleLayout.getMeasuredWidth();
                  boolean var13;
                  if (var11 <= var1) {
                     var13 = true;
                  } else {
                     var13 = false;
                  }

                  var2 = var1;
                  if (var13) {
                     var2 = var1 - var11;
                  }

                  var17 = this.mTitleLayout;
                  byte var12;
                  if (var13) {
                     var12 = 0;
                  } else {
                     var12 = 8;
                  }

                  var17.setVisibility(var12);
               } else {
                  var2 = this.measureChildView(var17, var1, var8, 0);
               }
            }
         }

         var14 = this.mCustomView;
         if (var14 != null) {
            LayoutParams var18 = var14.getLayoutParams();
            if (var18.width != -2) {
               var1 = 1073741824;
            } else {
               var1 = Integer.MIN_VALUE;
            }

            var11 = var18.width;
            var8 = var2;
            if (var11 >= 0) {
               var8 = Math.min(var11, var2);
            }

            if (var18.height != -2) {
               var2 = var4;
            } else {
               var2 = Integer.MIN_VALUE;
            }

            var11 = var18.height;
            var4 = var7;
            if (var11 >= 0) {
               var4 = Math.min(var11, var7);
            }

            this.mCustomView.measure(MeasureSpec.makeMeasureSpec(var8, var1), MeasureSpec.makeMeasureSpec(var4, var2));
         }

         if (super.mContentHeight <= 0) {
            var4 = this.getChildCount();
            var2 = 0;

            for(var1 = var10; var1 < var4; var2 = var3) {
               var8 = this.getChildAt(var1).getMeasuredHeight() + var6;
               var3 = var2;
               if (var8 > var2) {
                  var3 = var8;
               }

               ++var1;
            }

            this.setMeasuredDimension(var5, var2);
         } else {
            this.setMeasuredDimension(var5, var3);
         }

      } else {
         var9 = new StringBuilder();
         var9.append(ActionBarContextView.class.getSimpleName());
         var9.append(" can only be used ");
         var9.append("with android:layout_height=\"wrap_content\"");
         throw new IllegalStateException(var9.toString());
      }
   }

   public void setContentHeight(int var1) {
      super.mContentHeight = var1;
   }

   public void setCustomView(View var1) {
      View var2 = this.mCustomView;
      if (var2 != null) {
         this.removeView(var2);
      }

      this.mCustomView = var1;
      if (var1 != null) {
         LinearLayout var3 = this.mTitleLayout;
         if (var3 != null) {
            this.removeView(var3);
            this.mTitleLayout = null;
         }
      }

      if (var1 != null) {
         this.addView(var1);
      }

      this.requestLayout();
   }

   public void setSubtitle(CharSequence var1) {
      this.mSubtitle = var1;
      this.initTitle();
   }

   public void setTitle(CharSequence var1) {
      this.mTitle = var1;
      this.initTitle();
   }

   public void setTitleOptional(boolean var1) {
      if (var1 != this.mTitleOptional) {
         this.requestLayout();
      }

      this.mTitleOptional = var1;
   }

   public boolean shouldDelayChildPressedState() {
      return false;
   }
}
