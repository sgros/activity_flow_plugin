package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.widget.AutoSizeableTextView;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

public class AppCompatButton extends Button implements TintableBackgroundView, AutoSizeableTextView {
   private final AppCompatBackgroundHelper mBackgroundTintHelper;
   private final AppCompatTextHelper mTextHelper;

   public AppCompatButton(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatButton(Context var1, AttributeSet var2) {
      this(var1, var2, R.attr.buttonStyle);
   }

   public AppCompatButton(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      this.mBackgroundTintHelper = new AppCompatBackgroundHelper(this);
      this.mBackgroundTintHelper.loadFromAttributes(var2, var3);
      this.mTextHelper = AppCompatTextHelper.create(this);
      this.mTextHelper.loadFromAttributes(var2, var3);
      this.mTextHelper.applyCompoundDrawablesTints();
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      if (this.mBackgroundTintHelper != null) {
         this.mBackgroundTintHelper.applySupportBackgroundTint();
      }

      if (this.mTextHelper != null) {
         this.mTextHelper.applyCompoundDrawablesTints();
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int getAutoSizeMaxTextSize() {
      if (VERSION.SDK_INT >= 26) {
         return super.getAutoSizeMaxTextSize();
      } else {
         return this.mTextHelper != null ? this.mTextHelper.getAutoSizeMaxTextSize() : -1;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int getAutoSizeMinTextSize() {
      if (VERSION.SDK_INT >= 26) {
         return super.getAutoSizeMinTextSize();
      } else {
         return this.mTextHelper != null ? this.mTextHelper.getAutoSizeMinTextSize() : -1;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int getAutoSizeStepGranularity() {
      if (VERSION.SDK_INT >= 26) {
         return super.getAutoSizeStepGranularity();
      } else {
         return this.mTextHelper != null ? this.mTextHelper.getAutoSizeStepGranularity() : -1;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int[] getAutoSizeTextAvailableSizes() {
      if (VERSION.SDK_INT >= 26) {
         return super.getAutoSizeTextAvailableSizes();
      } else {
         return this.mTextHelper != null ? this.mTextHelper.getAutoSizeTextAvailableSizes() : new int[0];
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int getAutoSizeTextType() {
      int var1 = VERSION.SDK_INT;
      byte var2 = 0;
      if (var1 >= 26) {
         if (super.getAutoSizeTextType() == 1) {
            var2 = 1;
         }

         return var2;
      } else {
         return this.mTextHelper != null ? this.mTextHelper.getAutoSizeTextType() : 0;
      }
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public ColorStateList getSupportBackgroundTintList() {
      ColorStateList var1;
      if (this.mBackgroundTintHelper != null) {
         var1 = this.mBackgroundTintHelper.getSupportBackgroundTintList();
      } else {
         var1 = null;
      }

      return var1;
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public Mode getSupportBackgroundTintMode() {
      Mode var1;
      if (this.mBackgroundTintHelper != null) {
         var1 = this.mBackgroundTintHelper.getSupportBackgroundTintMode();
      } else {
         var1 = null;
      }

      return var1;
   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      super.onInitializeAccessibilityEvent(var1);
      var1.setClassName(Button.class.getName());
   }

   @RequiresApi(14)
   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setClassName(Button.class.getName());
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (this.mTextHelper != null) {
         this.mTextHelper.onLayout(var1, var2, var3, var4, var5);
      }

   }

   protected void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      super.onTextChanged(var1, var2, var3, var4);
      if (this.mTextHelper != null && VERSION.SDK_INT < 26 && this.mTextHelper.isAutoSizeEnabled()) {
         this.mTextHelper.autoSizeText();
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException {
      if (VERSION.SDK_INT >= 26) {
         super.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
      } else if (this.mTextHelper != null) {
         this.mTextHelper.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] var1, int var2) throws IllegalArgumentException {
      if (VERSION.SDK_INT >= 26) {
         super.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
      } else if (this.mTextHelper != null) {
         this.mTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setAutoSizeTextTypeWithDefaults(int var1) {
      if (VERSION.SDK_INT >= 26) {
         super.setAutoSizeTextTypeWithDefaults(var1);
      } else if (this.mTextHelper != null) {
         this.mTextHelper.setAutoSizeTextTypeWithDefaults(var1);
      }

   }

   public void setBackgroundDrawable(Drawable var1) {
      super.setBackgroundDrawable(var1);
      if (this.mBackgroundTintHelper != null) {
         this.mBackgroundTintHelper.onSetBackgroundDrawable(var1);
      }

   }

   public void setBackgroundResource(@DrawableRes int var1) {
      super.setBackgroundResource(var1);
      if (this.mBackgroundTintHelper != null) {
         this.mBackgroundTintHelper.onSetBackgroundResource(var1);
      }

   }

   public void setSupportAllCaps(boolean var1) {
      if (this.mTextHelper != null) {
         this.mTextHelper.setAllCaps(var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setSupportBackgroundTintList(@Nullable ColorStateList var1) {
      if (this.mBackgroundTintHelper != null) {
         this.mBackgroundTintHelper.setSupportBackgroundTintList(var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setSupportBackgroundTintMode(@Nullable Mode var1) {
      if (this.mBackgroundTintHelper != null) {
         this.mBackgroundTintHelper.setSupportBackgroundTintMode(var1);
      }

   }

   public void setTextAppearance(Context var1, int var2) {
      super.setTextAppearance(var1, var2);
      if (this.mTextHelper != null) {
         this.mTextHelper.onSetTextAppearance(var1, var2);
      }

   }

   public void setTextSize(int var1, float var2) {
      if (VERSION.SDK_INT >= 26) {
         super.setTextSize(var1, var2);
      } else if (this.mTextHelper != null) {
         this.mTextHelper.setTextSize(var1, var2);
      }

   }
}
