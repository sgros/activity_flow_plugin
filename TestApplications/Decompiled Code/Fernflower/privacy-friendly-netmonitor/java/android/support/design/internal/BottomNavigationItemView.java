package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.R;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.TooltipCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class BottomNavigationItemView extends FrameLayout implements MenuView.ItemView {
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   public static final int INVALID_ITEM_POSITION = -1;
   private final int mDefaultMargin;
   private ImageView mIcon;
   private ColorStateList mIconTint;
   private MenuItemImpl mItemData;
   private int mItemPosition;
   private final TextView mLargeLabel;
   private final float mScaleDownFactor;
   private final float mScaleUpFactor;
   private final int mShiftAmount;
   private boolean mShiftingMode;
   private final TextView mSmallLabel;

   public BottomNavigationItemView(@NonNull Context var1) {
      this(var1, (AttributeSet)null);
   }

   public BottomNavigationItemView(@NonNull Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public BottomNavigationItemView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mItemPosition = -1;
      Resources var7 = this.getResources();
      int var4 = var7.getDimensionPixelSize(R.dimen.design_bottom_navigation_text_size);
      var3 = var7.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_text_size);
      this.mDefaultMargin = var7.getDimensionPixelSize(R.dimen.design_bottom_navigation_margin);
      this.mShiftAmount = var4 - var3;
      float var5 = (float)var3;
      float var6 = (float)var4;
      this.mScaleUpFactor = 1.0F * var5 / var6;
      this.mScaleDownFactor = 1.0F * var6 / var5;
      LayoutInflater.from(var1).inflate(R.layout.design_bottom_navigation_item, this, true);
      this.setBackgroundResource(R.drawable.design_bottom_navigation_item_background);
      this.mIcon = (ImageView)this.findViewById(R.id.icon);
      this.mSmallLabel = (TextView)this.findViewById(R.id.smallLabel);
      this.mLargeLabel = (TextView)this.findViewById(R.id.largeLabel);
   }

   public MenuItemImpl getItemData() {
      return this.mItemData;
   }

   public int getItemPosition() {
      return this.mItemPosition;
   }

   public void initialize(MenuItemImpl var1, int var2) {
      this.mItemData = var1;
      this.setCheckable(var1.isCheckable());
      this.setChecked(var1.isChecked());
      this.setEnabled(var1.isEnabled());
      this.setIcon(var1.getIcon());
      this.setTitle(var1.getTitle());
      this.setId(var1.getItemId());
      this.setContentDescription(var1.getContentDescription());
      TooltipCompat.setTooltipText(this, var1.getTooltipText());
   }

   public int[] onCreateDrawableState(int var1) {
      int[] var2 = super.onCreateDrawableState(var1 + 1);
      if (this.mItemData != null && this.mItemData.isCheckable() && this.mItemData.isChecked()) {
         mergeDrawableStates(var2, CHECKED_STATE_SET);
      }

      return var2;
   }

   public boolean prefersCondensedTitle() {
      return false;
   }

   public void setCheckable(boolean var1) {
      this.refreshDrawableState();
   }

   public void setChecked(boolean var1) {
      this.mLargeLabel.setPivotX((float)(this.mLargeLabel.getWidth() / 2));
      this.mLargeLabel.setPivotY((float)this.mLargeLabel.getBaseline());
      this.mSmallLabel.setPivotX((float)(this.mSmallLabel.getWidth() / 2));
      this.mSmallLabel.setPivotY((float)this.mSmallLabel.getBaseline());
      LayoutParams var2;
      if (this.mShiftingMode) {
         if (var1) {
            var2 = (LayoutParams)this.mIcon.getLayoutParams();
            var2.gravity = 49;
            var2.topMargin = this.mDefaultMargin;
            this.mIcon.setLayoutParams(var2);
            this.mLargeLabel.setVisibility(0);
            this.mLargeLabel.setScaleX(1.0F);
            this.mLargeLabel.setScaleY(1.0F);
         } else {
            var2 = (LayoutParams)this.mIcon.getLayoutParams();
            var2.gravity = 17;
            var2.topMargin = this.mDefaultMargin;
            this.mIcon.setLayoutParams(var2);
            this.mLargeLabel.setVisibility(4);
            this.mLargeLabel.setScaleX(0.5F);
            this.mLargeLabel.setScaleY(0.5F);
         }

         this.mSmallLabel.setVisibility(4);
      } else if (var1) {
         var2 = (LayoutParams)this.mIcon.getLayoutParams();
         var2.gravity = 49;
         var2.topMargin = this.mDefaultMargin + this.mShiftAmount;
         this.mIcon.setLayoutParams(var2);
         this.mLargeLabel.setVisibility(0);
         this.mSmallLabel.setVisibility(4);
         this.mLargeLabel.setScaleX(1.0F);
         this.mLargeLabel.setScaleY(1.0F);
         this.mSmallLabel.setScaleX(this.mScaleUpFactor);
         this.mSmallLabel.setScaleY(this.mScaleUpFactor);
      } else {
         var2 = (LayoutParams)this.mIcon.getLayoutParams();
         var2.gravity = 49;
         var2.topMargin = this.mDefaultMargin;
         this.mIcon.setLayoutParams(var2);
         this.mLargeLabel.setVisibility(4);
         this.mSmallLabel.setVisibility(0);
         this.mLargeLabel.setScaleX(this.mScaleDownFactor);
         this.mLargeLabel.setScaleY(this.mScaleDownFactor);
         this.mSmallLabel.setScaleX(1.0F);
         this.mSmallLabel.setScaleY(1.0F);
      }

      this.refreshDrawableState();
   }

   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      this.mSmallLabel.setEnabled(var1);
      this.mLargeLabel.setEnabled(var1);
      this.mIcon.setEnabled(var1);
      if (var1) {
         ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(this.getContext(), 1002));
      } else {
         ViewCompat.setPointerIcon(this, (PointerIconCompat)null);
      }

   }

   public void setIcon(Drawable var1) {
      Drawable var2 = var1;
      if (var1 != null) {
         ConstantState var3 = var1.getConstantState();
         if (var3 != null) {
            var1 = var3.newDrawable();
         }

         var2 = DrawableCompat.wrap(var1).mutate();
         DrawableCompat.setTintList(var2, this.mIconTint);
      }

      this.mIcon.setImageDrawable(var2);
   }

   public void setIconTintList(ColorStateList var1) {
      this.mIconTint = var1;
      if (this.mItemData != null) {
         this.setIcon(this.mItemData.getIcon());
      }

   }

   public void setItemBackground(int var1) {
      Drawable var2;
      if (var1 == 0) {
         var2 = null;
      } else {
         var2 = ContextCompat.getDrawable(this.getContext(), var1);
      }

      ViewCompat.setBackground(this, var2);
   }

   public void setItemPosition(int var1) {
      this.mItemPosition = var1;
   }

   public void setShiftingMode(boolean var1) {
      this.mShiftingMode = var1;
   }

   public void setShortcut(boolean var1, char var2) {
   }

   public void setTextColor(ColorStateList var1) {
      this.mSmallLabel.setTextColor(var1);
      this.mLargeLabel.setTextColor(var1);
   }

   public void setTitle(CharSequence var1) {
      this.mSmallLabel.setText(var1);
      this.mLargeLabel.setText(var1);
   }

   public boolean showsIcon() {
      return true;
   }
}
