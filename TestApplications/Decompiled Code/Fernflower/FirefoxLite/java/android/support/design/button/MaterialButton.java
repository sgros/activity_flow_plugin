package android.support.design.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.design.R;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.internal.ViewUtils;
import android.support.design.resources.MaterialResources;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;

public class MaterialButton extends AppCompatButton {
   private Drawable icon;
   private int iconGravity;
   private int iconLeft;
   private int iconPadding;
   private int iconSize;
   private ColorStateList iconTint;
   private Mode iconTintMode;
   private final MaterialButtonHelper materialButtonHelper;

   public MaterialButton(Context var1, AttributeSet var2) {
      this(var1, var2, R.attr.materialButtonStyle);
   }

   public MaterialButton(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      TypedArray var4 = ThemeEnforcement.obtainStyledAttributes(var1, var2, R.styleable.MaterialButton, var3, R.style.Widget_MaterialComponents_Button);
      this.iconPadding = var4.getDimensionPixelSize(R.styleable.MaterialButton_iconPadding, 0);
      this.iconTintMode = ViewUtils.parseTintMode(var4.getInt(R.styleable.MaterialButton_iconTintMode, -1), Mode.SRC_IN);
      this.iconTint = MaterialResources.getColorStateList(this.getContext(), var4, R.styleable.MaterialButton_iconTint);
      this.icon = MaterialResources.getDrawable(this.getContext(), var4, R.styleable.MaterialButton_icon);
      this.iconGravity = var4.getInteger(R.styleable.MaterialButton_iconGravity, 1);
      this.iconSize = var4.getDimensionPixelSize(R.styleable.MaterialButton_iconSize, 0);
      this.materialButtonHelper = new MaterialButtonHelper(this);
      this.materialButtonHelper.loadFromAttributes(var4);
      var4.recycle();
      this.setCompoundDrawablePadding(this.iconPadding);
      this.updateIcon();
   }

   private boolean isLayoutRTL() {
      int var1 = ViewCompat.getLayoutDirection(this);
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   private boolean isUsingOriginalBackground() {
      boolean var1;
      if (this.materialButtonHelper != null && !this.materialButtonHelper.isBackgroundOverwritten()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void updateIcon() {
      if (this.icon != null) {
         this.icon = this.icon.mutate();
         DrawableCompat.setTintList(this.icon, this.iconTint);
         if (this.iconTintMode != null) {
            DrawableCompat.setTintMode(this.icon, this.iconTintMode);
         }

         int var1;
         if (this.iconSize != 0) {
            var1 = this.iconSize;
         } else {
            var1 = this.icon.getIntrinsicWidth();
         }

         int var2;
         if (this.iconSize != 0) {
            var2 = this.iconSize;
         } else {
            var2 = this.icon.getIntrinsicHeight();
         }

         this.icon.setBounds(this.iconLeft, 0, this.iconLeft + var1, var2);
      }

      TextViewCompat.setCompoundDrawablesRelative(this, this.icon, (Drawable)null, (Drawable)null, (Drawable)null);
   }

   public ColorStateList getBackgroundTintList() {
      return this.getSupportBackgroundTintList();
   }

   public Mode getBackgroundTintMode() {
      return this.getSupportBackgroundTintMode();
   }

   public int getCornerRadius() {
      int var1;
      if (this.isUsingOriginalBackground()) {
         var1 = this.materialButtonHelper.getCornerRadius();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public Drawable getIcon() {
      return this.icon;
   }

   public int getIconGravity() {
      return this.iconGravity;
   }

   public int getIconPadding() {
      return this.iconPadding;
   }

   public int getIconSize() {
      return this.iconSize;
   }

   public ColorStateList getIconTint() {
      return this.iconTint;
   }

   public Mode getIconTintMode() {
      return this.iconTintMode;
   }

   public ColorStateList getRippleColor() {
      ColorStateList var1;
      if (this.isUsingOriginalBackground()) {
         var1 = this.materialButtonHelper.getRippleColor();
      } else {
         var1 = null;
      }

      return var1;
   }

   public ColorStateList getStrokeColor() {
      ColorStateList var1;
      if (this.isUsingOriginalBackground()) {
         var1 = this.materialButtonHelper.getStrokeColor();
      } else {
         var1 = null;
      }

      return var1;
   }

   public int getStrokeWidth() {
      int var1;
      if (this.isUsingOriginalBackground()) {
         var1 = this.materialButtonHelper.getStrokeWidth();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public ColorStateList getSupportBackgroundTintList() {
      return this.isUsingOriginalBackground() ? this.materialButtonHelper.getSupportBackgroundTintList() : super.getSupportBackgroundTintList();
   }

   public Mode getSupportBackgroundTintMode() {
      return this.isUsingOriginalBackground() ? this.materialButtonHelper.getSupportBackgroundTintMode() : super.getSupportBackgroundTintMode();
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (VERSION.SDK_INT < 21 && this.isUsingOriginalBackground()) {
         this.materialButtonHelper.drawStroke(var1);
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (VERSION.SDK_INT == 21 && this.materialButtonHelper != null) {
         this.materialButtonHelper.updateMaskBounds(var5 - var3, var4 - var2);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      if (this.icon != null && this.iconGravity == 2) {
         var2 = (int)this.getPaint().measureText(this.getText().toString());
         if (this.iconSize == 0) {
            var1 = this.icon.getIntrinsicWidth();
         } else {
            var1 = this.iconSize;
         }

         var2 = (this.getMeasuredWidth() - var2 - ViewCompat.getPaddingEnd(this) - var1 - this.iconPadding - ViewCompat.getPaddingStart(this)) / 2;
         var1 = var2;
         if (this.isLayoutRTL()) {
            var1 = -var2;
         }

         if (this.iconLeft != var1) {
            this.iconLeft = var1;
            this.updateIcon();
         }

      }
   }

   public void setBackground(Drawable var1) {
      this.setBackgroundDrawable(var1);
   }

   public void setBackgroundColor(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setBackgroundColor(var1);
      } else {
         super.setBackgroundColor(var1);
      }

   }

   public void setBackgroundDrawable(Drawable var1) {
      if (this.isUsingOriginalBackground()) {
         if (var1 != this.getBackground()) {
            Log.i("MaterialButton", "Setting a custom background is not supported.");
            this.materialButtonHelper.setBackgroundOverwritten();
            super.setBackgroundDrawable(var1);
         } else {
            this.getBackground().setState(var1.getState());
         }
      } else {
         super.setBackgroundDrawable(var1);
      }

   }

   public void setBackgroundResource(int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = AppCompatResources.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setBackgroundDrawable(var2);
   }

   public void setBackgroundTintList(ColorStateList var1) {
      this.setSupportBackgroundTintList(var1);
   }

   public void setBackgroundTintMode(Mode var1) {
      this.setSupportBackgroundTintMode(var1);
   }

   public void setCornerRadius(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setCornerRadius(var1);
      }

   }

   public void setCornerRadiusResource(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.setCornerRadius(this.getResources().getDimensionPixelSize(var1));
      }

   }

   public void setIcon(Drawable var1) {
      if (this.icon != var1) {
         this.icon = var1;
         this.updateIcon();
      }

   }

   public void setIconGravity(int var1) {
      this.iconGravity = var1;
   }

   public void setIconPadding(int var1) {
      if (this.iconPadding != var1) {
         this.iconPadding = var1;
         this.setCompoundDrawablePadding(var1);
      }

   }

   public void setIconResource(int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = AppCompatResources.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setIcon(var2);
   }

   public void setIconSize(int var1) {
      if (var1 >= 0) {
         if (this.iconSize != var1) {
            this.iconSize = var1;
            this.updateIcon();
         }

      } else {
         throw new IllegalArgumentException("iconSize cannot be less than 0");
      }
   }

   public void setIconTint(ColorStateList var1) {
      if (this.iconTint != var1) {
         this.iconTint = var1;
         this.updateIcon();
      }

   }

   public void setIconTintMode(Mode var1) {
      if (this.iconTintMode != var1) {
         this.iconTintMode = var1;
         this.updateIcon();
      }

   }

   public void setIconTintResource(int var1) {
      this.setIconTint(AppCompatResources.getColorStateList(this.getContext(), var1));
   }

   void setInternalBackground(Drawable var1) {
      super.setBackgroundDrawable(var1);
   }

   public void setRippleColor(ColorStateList var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setRippleColor(var1);
      }

   }

   public void setRippleColorResource(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.setRippleColor(AppCompatResources.getColorStateList(this.getContext(), var1));
      }

   }

   public void setStrokeColor(ColorStateList var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setStrokeColor(var1);
      }

   }

   public void setStrokeColorResource(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.setStrokeColor(AppCompatResources.getColorStateList(this.getContext(), var1));
      }

   }

   public void setStrokeWidth(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setStrokeWidth(var1);
      }

   }

   public void setStrokeWidthResource(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.setStrokeWidth(this.getResources().getDimensionPixelSize(var1));
      }

   }

   public void setSupportBackgroundTintList(ColorStateList var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setSupportBackgroundTintList(var1);
      } else if (this.materialButtonHelper != null) {
         super.setSupportBackgroundTintList(var1);
      }

   }

   public void setSupportBackgroundTintMode(Mode var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setSupportBackgroundTintMode(var1);
      } else if (this.materialButtonHelper != null) {
         super.setSupportBackgroundTintMode(var1);
      }

   }
}
