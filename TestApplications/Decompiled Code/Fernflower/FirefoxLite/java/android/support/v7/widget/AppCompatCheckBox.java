package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TintableCompoundButton;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class AppCompatCheckBox extends CheckBox implements TintableCompoundButton {
   private final AppCompatCompoundButtonHelper mCompoundButtonHelper;

   public AppCompatCheckBox(Context var1, AttributeSet var2) {
      this(var1, var2, R.attr.checkboxStyle);
   }

   public AppCompatCheckBox(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      this.mCompoundButtonHelper = new AppCompatCompoundButtonHelper(this);
      this.mCompoundButtonHelper.loadFromAttributes(var2, var3);
   }

   public int getCompoundPaddingLeft() {
      int var1 = super.getCompoundPaddingLeft();
      int var2 = var1;
      if (this.mCompoundButtonHelper != null) {
         var2 = this.mCompoundButtonHelper.getCompoundPaddingLeft(var1);
      }

      return var2;
   }

   public ColorStateList getSupportButtonTintList() {
      ColorStateList var1;
      if (this.mCompoundButtonHelper != null) {
         var1 = this.mCompoundButtonHelper.getSupportButtonTintList();
      } else {
         var1 = null;
      }

      return var1;
   }

   public Mode getSupportButtonTintMode() {
      Mode var1;
      if (this.mCompoundButtonHelper != null) {
         var1 = this.mCompoundButtonHelper.getSupportButtonTintMode();
      } else {
         var1 = null;
      }

      return var1;
   }

   public void setButtonDrawable(int var1) {
      this.setButtonDrawable(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setButtonDrawable(Drawable var1) {
      super.setButtonDrawable(var1);
      if (this.mCompoundButtonHelper != null) {
         this.mCompoundButtonHelper.onSetButtonDrawable();
      }

   }

   public void setSupportButtonTintList(ColorStateList var1) {
      if (this.mCompoundButtonHelper != null) {
         this.mCompoundButtonHelper.setSupportButtonTintList(var1);
      }

   }

   public void setSupportButtonTintMode(Mode var1) {
      if (this.mCompoundButtonHelper != null) {
         this.mCompoundButtonHelper.setSupportButtonTintMode(var1);
      }

   }
}
