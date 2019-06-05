package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;

@RequiresApi(9)
class AppCompatTextHelper {
   @NonNull
   private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
   private TintInfo mDrawableBottomTint;
   private TintInfo mDrawableLeftTint;
   private TintInfo mDrawableRightTint;
   private TintInfo mDrawableTopTint;
   private Typeface mFontTypeface;
   private int mStyle = 0;
   final TextView mView;

   AppCompatTextHelper(TextView var1) {
      this.mView = var1;
      this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(this.mView);
   }

   static AppCompatTextHelper create(TextView var0) {
      return (AppCompatTextHelper)(VERSION.SDK_INT >= 17 ? new AppCompatTextHelperV17(var0) : new AppCompatTextHelper(var0));
   }

   protected static TintInfo createTintInfo(Context var0, AppCompatDrawableManager var1, int var2) {
      ColorStateList var3 = var1.getTintList(var0, var2);
      if (var3 != null) {
         TintInfo var4 = new TintInfo();
         var4.mHasTintList = true;
         var4.mTintList = var3;
         return var4;
      } else {
         return null;
      }
   }

   private void setTextSizeInternal(int var1, float var2) {
      this.mAutoSizeTextHelper.setTextSizeInternal(var1, var2);
   }

   private void updateTypefaceAndStyle(Context var1, TintTypedArray var2) {
      this.mStyle = var2.getInt(R.styleable.TextAppearance_android_textStyle, this.mStyle);
      if (var2.hasValue(R.styleable.TextAppearance_android_fontFamily) || var2.hasValue(R.styleable.TextAppearance_fontFamily)) {
         this.mFontTypeface = null;
         int var3;
         if (var2.hasValue(R.styleable.TextAppearance_android_fontFamily)) {
            var3 = R.styleable.TextAppearance_android_fontFamily;
         } else {
            var3 = R.styleable.TextAppearance_fontFamily;
         }

         if (!var1.isRestricted()) {
            try {
               this.mFontTypeface = var2.getFont(var3, this.mStyle, this.mView);
            } catch (NotFoundException | UnsupportedOperationException var4) {
            }
         }

         if (this.mFontTypeface == null) {
            this.mFontTypeface = Typeface.create(var2.getString(var3), this.mStyle);
         }
      }

   }

   final void applyCompoundDrawableTint(Drawable var1, TintInfo var2) {
      if (var1 != null && var2 != null) {
         AppCompatDrawableManager.tintDrawable(var1, var2, this.mView.getDrawableState());
      }

   }

   void applyCompoundDrawablesTints() {
      if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
         Drawable[] var1 = this.mView.getCompoundDrawables();
         this.applyCompoundDrawableTint(var1[0], this.mDrawableLeftTint);
         this.applyCompoundDrawableTint(var1[1], this.mDrawableTopTint);
         this.applyCompoundDrawableTint(var1[2], this.mDrawableRightTint);
         this.applyCompoundDrawableTint(var1[3], this.mDrawableBottomTint);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void autoSizeText() {
      this.mAutoSizeTextHelper.autoSizeText();
   }

   int getAutoSizeMaxTextSize() {
      return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
   }

   int getAutoSizeMinTextSize() {
      return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
   }

   int getAutoSizeStepGranularity() {
      return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
   }

   int[] getAutoSizeTextAvailableSizes() {
      return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
   }

   int getAutoSizeTextType() {
      return this.mAutoSizeTextHelper.getAutoSizeTextType();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   boolean isAutoSizeEnabled() {
      return this.mAutoSizeTextHelper.isAutoSizeEnabled();
   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      Context var3 = this.mView.getContext();
      AppCompatDrawableManager var4 = AppCompatDrawableManager.get();
      TintTypedArray var5 = TintTypedArray.obtainStyledAttributes(var3, var1, R.styleable.AppCompatTextHelper, var2, 0);
      int var6 = var5.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
      if (var5.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)) {
         this.mDrawableLeftTint = createTintInfo(var3, var4, var5.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
      }

      if (var5.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)) {
         this.mDrawableTopTint = createTintInfo(var3, var4, var5.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0));
      }

      if (var5.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)) {
         this.mDrawableRightTint = createTintInfo(var3, var4, var5.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0));
      }

      if (var5.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)) {
         this.mDrawableBottomTint = createTintInfo(var3, var4, var5.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
      }

      var5.recycle();
      boolean var7 = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
      ColorStateList var18 = null;
      TintTypedArray var8 = null;
      ColorStateList var9 = null;
      boolean var10;
      boolean var11;
      ColorStateList var19;
      if (var6 != -1) {
         var8 = TintTypedArray.obtainStyledAttributes(var3, var6, R.styleable.TextAppearance);
         if (!var7 && var8.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            var10 = var8.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
            var11 = true;
         } else {
            var11 = false;
            var10 = var11;
         }

         this.updateTypefaceAndStyle(var3, var8);
         if (VERSION.SDK_INT < 23) {
            if (var8.hasValue(R.styleable.TextAppearance_android_textColor)) {
               var18 = var8.getColorStateList(R.styleable.TextAppearance_android_textColor);
            } else {
               var18 = null;
            }

            if (var8.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
               var19 = var8.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
            } else {
               var19 = null;
            }

            if (var8.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
               var9 = var8.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
            }
         } else {
            var9 = null;
            var19 = var9;
         }

         var8.recycle();
      } else {
         var11 = false;
         var10 = var11;
         var9 = null;
         var19 = var9;
         var18 = var8;
      }

      TintTypedArray var12 = TintTypedArray.obtainStyledAttributes(var3, var1, R.styleable.TextAppearance, var2, 0);
      boolean var13 = var11;
      boolean var14 = var10;
      if (!var7) {
         var13 = var11;
         var14 = var10;
         if (var12.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            var14 = var12.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
            var13 = true;
         }
      }

      ColorStateList var15 = var18;
      ColorStateList var16 = var9;
      ColorStateList var20 = var19;
      if (VERSION.SDK_INT < 23) {
         if (var12.hasValue(R.styleable.TextAppearance_android_textColor)) {
            var18 = var12.getColorStateList(R.styleable.TextAppearance_android_textColor);
         }

         if (var12.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
            var19 = var12.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
         }

         var15 = var18;
         var16 = var9;
         var20 = var19;
         if (var12.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
            var16 = var12.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
            var20 = var19;
            var15 = var18;
         }
      }

      this.updateTypefaceAndStyle(var3, var12);
      var12.recycle();
      if (var15 != null) {
         this.mView.setTextColor(var15);
      }

      if (var20 != null) {
         this.mView.setHintTextColor(var20);
      }

      if (var16 != null) {
         this.mView.setLinkTextColor(var16);
      }

      if (!var7 && var13) {
         this.setAllCaps(var14);
      }

      if (this.mFontTypeface != null) {
         this.mView.setTypeface(this.mFontTypeface, this.mStyle);
      }

      this.mAutoSizeTextHelper.loadFromAttributes(var1, var2);
      if (VERSION.SDK_INT >= 26 && this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
         int[] var17 = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
         if (var17.length > 0) {
            if ((float)this.mView.getAutoSizeStepGranularity() != -1.0F) {
               this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
            } else {
               this.mView.setAutoSizeTextTypeUniformWithPresetSizes(var17, 0);
            }
         }
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (VERSION.SDK_INT < 26) {
         this.autoSizeText();
      }

   }

   void onSetTextAppearance(Context var1, int var2) {
      TintTypedArray var3 = TintTypedArray.obtainStyledAttributes(var1, var2, R.styleable.TextAppearance);
      if (var3.hasValue(R.styleable.TextAppearance_textAllCaps)) {
         this.setAllCaps(var3.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
      }

      if (VERSION.SDK_INT < 23 && var3.hasValue(R.styleable.TextAppearance_android_textColor)) {
         ColorStateList var4 = var3.getColorStateList(R.styleable.TextAppearance_android_textColor);
         if (var4 != null) {
            this.mView.setTextColor(var4);
         }
      }

      this.updateTypefaceAndStyle(var1, var3);
      var3.recycle();
      if (this.mFontTypeface != null) {
         this.mView.setTypeface(this.mFontTypeface, this.mStyle);
      }

   }

   void setAllCaps(boolean var1) {
      this.mView.setAllCaps(var1);
   }

   void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException {
      this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
   }

   void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] var1, int var2) throws IllegalArgumentException {
      this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
   }

   void setAutoSizeTextTypeWithDefaults(int var1) {
      this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(var1);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void setTextSize(int var1, float var2) {
      if (VERSION.SDK_INT < 26 && !this.isAutoSizeEnabled()) {
         this.setTextSizeInternal(var1, var2);
      }

   }
}
