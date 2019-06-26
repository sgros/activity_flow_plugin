package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.appcompat.R$styleable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.AutoSizeableTextView;
import androidx.core.widget.TextViewCompat;
import java.lang.ref.WeakReference;

class AppCompatTextHelper {
   private boolean mAsyncFontPending;
   private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
   private TintInfo mDrawableBottomTint;
   private TintInfo mDrawableEndTint;
   private TintInfo mDrawableLeftTint;
   private TintInfo mDrawableRightTint;
   private TintInfo mDrawableStartTint;
   private TintInfo mDrawableTopTint;
   private Typeface mFontTypeface;
   private int mStyle = 0;
   private final TextView mView;

   AppCompatTextHelper(TextView var1) {
      this.mView = var1;
      this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(this.mView);
   }

   private void applyCompoundDrawableTint(Drawable var1, TintInfo var2) {
      if (var1 != null && var2 != null) {
         AppCompatDrawableManager.tintDrawable(var1, var2, this.mView.getDrawableState());
      }

   }

   private static TintInfo createTintInfo(Context var0, AppCompatDrawableManager var1, int var2) {
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
      this.mStyle = var2.getInt(R$styleable.TextAppearance_android_textStyle, this.mStyle);
      boolean var3 = var2.hasValue(R$styleable.TextAppearance_android_fontFamily);
      boolean var4 = false;
      int var5;
      if (!var3 && !var2.hasValue(R$styleable.TextAppearance_fontFamily)) {
         if (var2.hasValue(R$styleable.TextAppearance_android_typeface)) {
            this.mAsyncFontPending = false;
            var5 = var2.getInt(R$styleable.TextAppearance_android_typeface, 1);
            if (var5 != 1) {
               if (var5 != 2) {
                  if (var5 == 3) {
                     this.mFontTypeface = Typeface.MONOSPACE;
                  }
               } else {
                  this.mFontTypeface = Typeface.SERIF;
               }
            } else {
               this.mFontTypeface = Typeface.SANS_SERIF;
            }
         }

      } else {
         this.mFontTypeface = null;
         if (var2.hasValue(R$styleable.TextAppearance_fontFamily)) {
            var5 = R$styleable.TextAppearance_fontFamily;
         } else {
            var5 = R$styleable.TextAppearance_android_fontFamily;
         }

         if (!var1.isRestricted()) {
            label71: {
               ResourcesCompat.FontCallback var9 = new ResourcesCompat.FontCallback(new WeakReference(this.mView)) {
                  // $FF: synthetic field
                  final WeakReference val$textViewWeak;

                  {
                     this.val$textViewWeak = var2;
                  }

                  public void onFontRetrievalFailed(int var1) {
                  }

                  public void onFontRetrieved(Typeface var1) {
                     AppCompatTextHelper.this.onAsyncTypefaceReceived(this.val$textViewWeak, var1);
                  }
               };

               boolean var10001;
               try {
                  this.mFontTypeface = var2.getFont(var5, this.mStyle, var9);
               } catch (NotFoundException | UnsupportedOperationException var8) {
                  var10001 = false;
                  break label71;
               }

               label55: {
                  try {
                     if (this.mFontTypeface != null) {
                        break label55;
                     }
                  } catch (NotFoundException | UnsupportedOperationException var7) {
                     var10001 = false;
                     break label71;
                  }

                  var4 = true;
               }

               try {
                  this.mAsyncFontPending = var4;
               } catch (NotFoundException | UnsupportedOperationException var6) {
                  var10001 = false;
               }
            }
         }

         if (this.mFontTypeface == null) {
            String var10 = var2.getString(var5);
            if (var10 != null) {
               this.mFontTypeface = Typeface.create(var10, this.mStyle);
            }
         }

      }
   }

   void applyCompoundDrawablesTints() {
      Drawable[] var1;
      if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
         var1 = this.mView.getCompoundDrawables();
         this.applyCompoundDrawableTint(var1[0], this.mDrawableLeftTint);
         this.applyCompoundDrawableTint(var1[1], this.mDrawableTopTint);
         this.applyCompoundDrawableTint(var1[2], this.mDrawableRightTint);
         this.applyCompoundDrawableTint(var1[3], this.mDrawableBottomTint);
      }

      if (VERSION.SDK_INT >= 17 && (this.mDrawableStartTint != null || this.mDrawableEndTint != null)) {
         var1 = this.mView.getCompoundDrawablesRelative();
         this.applyCompoundDrawableTint(var1[0], this.mDrawableStartTint);
         this.applyCompoundDrawableTint(var1[2], this.mDrawableEndTint);
      }

   }

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

   boolean isAutoSizeEnabled() {
      return this.mAutoSizeTextHelper.isAutoSizeEnabled();
   }

   @SuppressLint({"NewApi"})
   void loadFromAttributes(AttributeSet var1, int var2) {
      Context var3 = this.mView.getContext();
      AppCompatDrawableManager var4 = AppCompatDrawableManager.get();
      TintTypedArray var5 = TintTypedArray.obtainStyledAttributes(var3, var1, R$styleable.AppCompatTextHelper, var2, 0);
      int var6 = var5.getResourceId(R$styleable.AppCompatTextHelper_android_textAppearance, -1);
      if (var5.hasValue(R$styleable.AppCompatTextHelper_android_drawableLeft)) {
         this.mDrawableLeftTint = createTintInfo(var3, var4, var5.getResourceId(R$styleable.AppCompatTextHelper_android_drawableLeft, 0));
      }

      if (var5.hasValue(R$styleable.AppCompatTextHelper_android_drawableTop)) {
         this.mDrawableTopTint = createTintInfo(var3, var4, var5.getResourceId(R$styleable.AppCompatTextHelper_android_drawableTop, 0));
      }

      if (var5.hasValue(R$styleable.AppCompatTextHelper_android_drawableRight)) {
         this.mDrawableRightTint = createTintInfo(var3, var4, var5.getResourceId(R$styleable.AppCompatTextHelper_android_drawableRight, 0));
      }

      if (var5.hasValue(R$styleable.AppCompatTextHelper_android_drawableBottom)) {
         this.mDrawableBottomTint = createTintInfo(var3, var4, var5.getResourceId(R$styleable.AppCompatTextHelper_android_drawableBottom, 0));
      }

      if (VERSION.SDK_INT >= 17) {
         if (var5.hasValue(R$styleable.AppCompatTextHelper_android_drawableStart)) {
            this.mDrawableStartTint = createTintInfo(var3, var4, var5.getResourceId(R$styleable.AppCompatTextHelper_android_drawableStart, 0));
         }

         if (var5.hasValue(R$styleable.AppCompatTextHelper_android_drawableEnd)) {
            this.mDrawableEndTint = createTintInfo(var3, var4, var5.getResourceId(R$styleable.AppCompatTextHelper_android_drawableEnd, 0));
         }
      }

      var5.recycle();
      boolean var7 = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
      boolean var8 = true;
      ColorStateList var17 = null;
      TintTypedArray var9 = null;
      ColorStateList var10 = null;
      boolean var11;
      ColorStateList var16;
      boolean var18;
      if (var6 != -1) {
         var9 = TintTypedArray.obtainStyledAttributes(var3, var6, R$styleable.TextAppearance);
         if (!var7 && var9.hasValue(R$styleable.TextAppearance_textAllCaps)) {
            var11 = var9.getBoolean(R$styleable.TextAppearance_textAllCaps, false);
            var18 = true;
         } else {
            var18 = false;
            var11 = false;
         }

         this.updateTypefaceAndStyle(var3, var9);
         if (VERSION.SDK_INT < 23) {
            if (var9.hasValue(R$styleable.TextAppearance_android_textColor)) {
               var17 = var9.getColorStateList(R$styleable.TextAppearance_android_textColor);
            } else {
               var17 = null;
            }

            if (var9.hasValue(R$styleable.TextAppearance_android_textColorHint)) {
               var16 = var9.getColorStateList(R$styleable.TextAppearance_android_textColorHint);
            } else {
               var16 = null;
            }

            if (var9.hasValue(R$styleable.TextAppearance_android_textColorLink)) {
               var10 = var9.getColorStateList(R$styleable.TextAppearance_android_textColorLink);
            }
         } else {
            var10 = null;
            var16 = var10;
         }

         var9.recycle();
      } else {
         var10 = null;
         var16 = var10;
         var18 = false;
         var11 = false;
         var17 = var9;
      }

      TintTypedArray var12 = TintTypedArray.obtainStyledAttributes(var3, var1, R$styleable.TextAppearance, var2, 0);
      if (!var7 && var12.hasValue(R$styleable.TextAppearance_textAllCaps)) {
         var11 = var12.getBoolean(R$styleable.TextAppearance_textAllCaps, false);
         var18 = var8;
      }

      ColorStateList var13 = var17;
      ColorStateList var14 = var10;
      ColorStateList var22 = var16;
      if (VERSION.SDK_INT < 23) {
         if (var12.hasValue(R$styleable.TextAppearance_android_textColor)) {
            var17 = var12.getColorStateList(R$styleable.TextAppearance_android_textColor);
         }

         if (var12.hasValue(R$styleable.TextAppearance_android_textColorHint)) {
            var16 = var12.getColorStateList(R$styleable.TextAppearance_android_textColorHint);
         }

         var13 = var17;
         var14 = var10;
         var22 = var16;
         if (var12.hasValue(R$styleable.TextAppearance_android_textColorLink)) {
            var14 = var12.getColorStateList(R$styleable.TextAppearance_android_textColorLink);
            var22 = var16;
            var13 = var17;
         }
      }

      if (VERSION.SDK_INT >= 28 && var12.hasValue(R$styleable.TextAppearance_android_textSize) && var12.getDimensionPixelSize(R$styleable.TextAppearance_android_textSize, -1) == 0) {
         this.mView.setTextSize(0, 0.0F);
      }

      this.updateTypefaceAndStyle(var3, var12);
      var12.recycle();
      if (var13 != null) {
         this.mView.setTextColor(var13);
      }

      if (var22 != null) {
         this.mView.setHintTextColor(var22);
      }

      if (var14 != null) {
         this.mView.setLinkTextColor(var14);
      }

      if (!var7 && var18) {
         this.setAllCaps(var11);
      }

      Typeface var19 = this.mFontTypeface;
      if (var19 != null) {
         this.mView.setTypeface(var19, this.mStyle);
      }

      this.mAutoSizeTextHelper.loadFromAttributes(var1, var2);
      if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
         int[] var20 = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
         if (var20.length > 0) {
            if ((float)this.mView.getAutoSizeStepGranularity() != -1.0F) {
               this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
            } else {
               this.mView.setAutoSizeTextTypeUniformWithPresetSizes(var20, 0);
            }
         }
      }

      TintTypedArray var15 = TintTypedArray.obtainStyledAttributes(var3, var1, R$styleable.AppCompatTextView);
      int var21 = var15.getDimensionPixelSize(R$styleable.AppCompatTextView_firstBaselineToTopHeight, -1);
      var2 = var15.getDimensionPixelSize(R$styleable.AppCompatTextView_lastBaselineToBottomHeight, -1);
      var6 = var15.getDimensionPixelSize(R$styleable.AppCompatTextView_lineHeight, -1);
      var15.recycle();
      if (var21 != -1) {
         TextViewCompat.setFirstBaselineToTopHeight(this.mView, var21);
      }

      if (var2 != -1) {
         TextViewCompat.setLastBaselineToBottomHeight(this.mView, var2);
      }

      if (var6 != -1) {
         TextViewCompat.setLineHeight(this.mView, var6);
      }

   }

   void onAsyncTypefaceReceived(WeakReference var1, Typeface var2) {
      if (this.mAsyncFontPending) {
         this.mFontTypeface = var2;
         TextView var3 = (TextView)var1.get();
         if (var3 != null) {
            var3.setTypeface(var2, this.mStyle);
         }
      }

   }

   void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
         this.autoSizeText();
      }

   }

   void onSetTextAppearance(Context var1, int var2) {
      TintTypedArray var3 = TintTypedArray.obtainStyledAttributes(var1, var2, R$styleable.TextAppearance);
      if (var3.hasValue(R$styleable.TextAppearance_textAllCaps)) {
         this.setAllCaps(var3.getBoolean(R$styleable.TextAppearance_textAllCaps, false));
      }

      if (VERSION.SDK_INT < 23 && var3.hasValue(R$styleable.TextAppearance_android_textColor)) {
         ColorStateList var4 = var3.getColorStateList(R$styleable.TextAppearance_android_textColor);
         if (var4 != null) {
            this.mView.setTextColor(var4);
         }
      }

      if (var3.hasValue(R$styleable.TextAppearance_android_textSize) && var3.getDimensionPixelSize(R$styleable.TextAppearance_android_textSize, -1) == 0) {
         this.mView.setTextSize(0, 0.0F);
      }

      this.updateTypefaceAndStyle(var1, var3);
      var3.recycle();
      Typeface var5 = this.mFontTypeface;
      if (var5 != null) {
         this.mView.setTypeface(var5, this.mStyle);
      }

   }

   void setAllCaps(boolean var1) {
      this.mView.setAllCaps(var1);
   }

   void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException {
      this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
   }

   void setAutoSizeTextTypeUniformWithPresetSizes(int[] var1, int var2) throws IllegalArgumentException {
      this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
   }

   void setAutoSizeTextTypeWithDefaults(int var1) {
      this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(var1);
   }

   void setTextSize(int var1, float var2) {
      if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && !this.isAutoSizeEnabled()) {
         this.setTextSizeInternal(var1, var2);
      }

   }
}