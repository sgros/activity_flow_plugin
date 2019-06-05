package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.text.StaticLayout.Builder;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;

class AppCompatTextViewAutoSizeHelper {
   private static final int DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX = 1;
   private static final int DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP = 112;
   private static final int DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP = 12;
   private static final String TAG = "ACTVAutoSizeHelper";
   private static final RectF TEMP_RECTF = new RectF();
   static final float UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE = -1.0F;
   private static final int VERY_WIDE = 1048576;
   private static Hashtable sTextViewMethodByNameCache = new Hashtable();
   private float mAutoSizeMaxTextSizeInPx = -1.0F;
   private float mAutoSizeMinTextSizeInPx = -1.0F;
   private float mAutoSizeStepGranularityInPx = -1.0F;
   private int[] mAutoSizeTextSizesInPx = new int[0];
   private int mAutoSizeTextType = 0;
   private final Context mContext;
   private boolean mHasPresetAutoSizeValues = false;
   private boolean mNeedsAutoSizeText = false;
   private TextPaint mTempTextPaint;
   private final TextView mTextView;

   AppCompatTextViewAutoSizeHelper(TextView var1) {
      this.mTextView = var1;
      this.mContext = this.mTextView.getContext();
   }

   private int[] cleanupAutoSizePresetSizes(int[] var1) {
      int var2 = var1.length;
      if (var2 == 0) {
         return var1;
      } else {
         Arrays.sort(var1);
         ArrayList var3 = new ArrayList();
         byte var4 = 0;

         int var5;
         for(var5 = 0; var5 < var2; ++var5) {
            int var6 = var1[var5];
            if (var6 > 0 && Collections.binarySearch(var3, var6) < 0) {
               var3.add(var6);
            }
         }

         if (var2 == var3.size()) {
            return var1;
         } else {
            var2 = var3.size();
            var1 = new int[var2];

            for(var5 = var4; var5 < var2; ++var5) {
               var1[var5] = (Integer)var3.get(var5);
            }

            return var1;
         }
      }
   }

   private void clearAutoSizeConfiguration() {
      this.mAutoSizeTextType = 0;
      this.mAutoSizeMinTextSizeInPx = -1.0F;
      this.mAutoSizeMaxTextSizeInPx = -1.0F;
      this.mAutoSizeStepGranularityInPx = -1.0F;
      this.mAutoSizeTextSizesInPx = new int[0];
      this.mNeedsAutoSizeText = false;
   }

   @TargetApi(23)
   private StaticLayout createStaticLayoutForMeasuring(CharSequence var1, Alignment var2, int var3, int var4) {
      TextDirectionHeuristic var5 = (TextDirectionHeuristic)this.invokeAndReturnWithDefault(this.mTextView, "getTextDirectionHeuristic", TextDirectionHeuristics.FIRSTSTRONG_LTR);
      Builder var6 = Builder.obtain(var1, 0, var1.length(), this.mTempTextPaint, var3).setAlignment(var2).setLineSpacing(this.mTextView.getLineSpacingExtra(), this.mTextView.getLineSpacingMultiplier()).setIncludePad(this.mTextView.getIncludeFontPadding()).setBreakStrategy(this.mTextView.getBreakStrategy()).setHyphenationFrequency(this.mTextView.getHyphenationFrequency());
      var3 = var4;
      if (var4 == -1) {
         var3 = Integer.MAX_VALUE;
      }

      return var6.setMaxLines(var3).setTextDirection(var5).build();
   }

   @TargetApi(14)
   private StaticLayout createStaticLayoutForMeasuringPre23(CharSequence var1, Alignment var2, int var3) {
      float var4;
      float var5;
      boolean var6;
      if (VERSION.SDK_INT >= 16) {
         var4 = this.mTextView.getLineSpacingMultiplier();
         var5 = this.mTextView.getLineSpacingExtra();
         var6 = this.mTextView.getIncludeFontPadding();
      } else {
         var4 = (Float)this.invokeAndReturnWithDefault(this.mTextView, "getLineSpacingMultiplier", 1.0F);
         var5 = (Float)this.invokeAndReturnWithDefault(this.mTextView, "getLineSpacingExtra", 0.0F);
         var6 = (Boolean)this.invokeAndReturnWithDefault(this.mTextView, "getIncludeFontPadding", true);
      }

      return new StaticLayout(var1, this.mTempTextPaint, var3, var2, var4, var5, var6);
   }

   private int findLargestTextSizeWhichFits(RectF var1) {
      int var2 = this.mAutoSizeTextSizesInPx.length;
      if (var2 == 0) {
         throw new IllegalStateException("No available text sizes to choose from.");
      } else {
         int var3 = 0;
         int var4 = 1;
         --var2;

         while(var4 <= var2) {
            var3 = (var4 + var2) / 2;
            if (this.suggestedSizeFitsInSpace(this.mAutoSizeTextSizesInPx[var3], var1)) {
               int var5 = var3 + 1;
               var3 = var4;
               var4 = var5;
            } else {
               --var3;
               var2 = var3;
            }
         }

         return this.mAutoSizeTextSizesInPx[var3];
      }
   }

   @Nullable
   private Method getTextViewMethod(@NonNull String var1) {
      Exception var10000;
      label43: {
         boolean var10001;
         Method var2;
         try {
            var2 = (Method)sTextViewMethodByNameCache.get(var1);
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
            break label43;
         }

         Method var3 = var2;
         if (var2 != null) {
            return var3;
         }

         try {
            var2 = TextView.class.getDeclaredMethod(var1);
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
            break label43;
         }

         var3 = var2;
         if (var2 == null) {
            return var3;
         }

         try {
            var2.setAccessible(true);
            sTextViewMethodByNameCache.put(var1, var2);
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
            break label43;
         }

         var3 = var2;
         return var3;
      }

      Exception var8 = var10000;
      StringBuilder var7 = new StringBuilder();
      var7.append("Failed to retrieve TextView#");
      var7.append(var1);
      var7.append("() method");
      Log.w("ACTVAutoSizeHelper", var7.toString(), var8);
      return null;
   }

   private Object invokeAndReturnWithDefault(@NonNull Object var1, @NonNull String var2, @NonNull Object var3) {
      try {
         var1 = this.getTextViewMethod(var2).invoke(var1);
         return var1;
      } catch (Exception var7) {
         StringBuilder var9 = new StringBuilder();
         var9.append("Failed to invoke TextView#");
         var9.append(var2);
         var9.append("() method");
         Log.w("ACTVAutoSizeHelper", var9.toString(), var7);
      } finally {
         ;
      }

      var1 = var3;
      return var1;
   }

   private void setRawTextSize(float var1) {
      if (var1 != this.mTextView.getPaint().getTextSize()) {
         this.mTextView.getPaint().setTextSize(var1);
         boolean var2;
         if (VERSION.SDK_INT >= 18) {
            var2 = this.mTextView.isInLayout();
         } else {
            var2 = false;
         }

         if (this.mTextView.getLayout() != null) {
            this.mNeedsAutoSizeText = false;

            label36: {
               Exception var10000;
               label44: {
                  boolean var10001;
                  Method var3;
                  try {
                     var3 = this.getTextViewMethod("nullLayouts");
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                     break label44;
                  }

                  if (var3 == null) {
                     break label36;
                  }

                  try {
                     var3.invoke(this.mTextView);
                     break label36;
                  } catch (Exception var4) {
                     var10000 = var4;
                     var10001 = false;
                  }
               }

               Exception var6 = var10000;
               Log.w("ACTVAutoSizeHelper", "Failed to invoke TextView#nullLayouts() method", var6);
            }

            if (!var2) {
               this.mTextView.requestLayout();
            } else {
               this.mTextView.forceLayout();
            }

            this.mTextView.invalidate();
         }
      }

   }

   private boolean setupAutoSizeText() {
      boolean var1 = this.supportsAutoSizeText();
      int var2 = 0;
      if (var1 && this.mAutoSizeTextType == 1) {
         if (!this.mHasPresetAutoSizeValues || this.mAutoSizeTextSizesInPx.length == 0) {
            float var3 = (float)Math.round(this.mAutoSizeMinTextSizeInPx);

            int var4;
            for(var4 = 1; Math.round(this.mAutoSizeStepGranularityInPx + var3) <= Math.round(this.mAutoSizeMaxTextSizeInPx); var3 += this.mAutoSizeStepGranularityInPx) {
               ++var4;
            }

            int[] var5 = new int[var4];

            for(var3 = this.mAutoSizeMinTextSizeInPx; var2 < var4; ++var2) {
               var5[var2] = Math.round(var3);
               var3 += this.mAutoSizeStepGranularityInPx;
            }

            this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(var5);
         }

         this.mNeedsAutoSizeText = true;
      } else {
         this.mNeedsAutoSizeText = false;
      }

      return this.mNeedsAutoSizeText;
   }

   private void setupAutoSizeUniformPresetSizes(TypedArray var1) {
      int var2 = var1.length();
      int[] var3 = new int[var2];
      if (var2 > 0) {
         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = var1.getDimensionPixelSize(var4, -1);
         }

         this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(var3);
         this.setupAutoSizeUniformPresetSizesConfiguration();
      }

   }

   private boolean setupAutoSizeUniformPresetSizesConfiguration() {
      int var1 = this.mAutoSizeTextSizesInPx.length;
      boolean var2;
      if (var1 > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mHasPresetAutoSizeValues = var2;
      if (this.mHasPresetAutoSizeValues) {
         this.mAutoSizeTextType = 1;
         this.mAutoSizeMinTextSizeInPx = (float)this.mAutoSizeTextSizesInPx[0];
         this.mAutoSizeMaxTextSizeInPx = (float)this.mAutoSizeTextSizesInPx[var1 - 1];
         this.mAutoSizeStepGranularityInPx = -1.0F;
      }

      return this.mHasPresetAutoSizeValues;
   }

   private boolean suggestedSizeFitsInSpace(int var1, RectF var2) {
      CharSequence var3 = this.mTextView.getText();
      int var4;
      if (VERSION.SDK_INT >= 16) {
         var4 = this.mTextView.getMaxLines();
      } else {
         var4 = -1;
      }

      if (this.mTempTextPaint == null) {
         this.mTempTextPaint = new TextPaint();
      } else {
         this.mTempTextPaint.reset();
      }

      this.mTempTextPaint.set(this.mTextView.getPaint());
      this.mTempTextPaint.setTextSize((float)var1);
      Alignment var5 = (Alignment)this.invokeAndReturnWithDefault(this.mTextView, "getLayoutAlignment", Alignment.ALIGN_NORMAL);
      StaticLayout var6;
      if (VERSION.SDK_INT >= 23) {
         var6 = this.createStaticLayoutForMeasuring(var3, var5, Math.round(var2.right), var4);
      } else {
         var6 = this.createStaticLayoutForMeasuringPre23(var3, var5, Math.round(var2.right));
      }

      if (var4 != -1 && (var6.getLineCount() > var4 || var6.getLineEnd(var6.getLineCount() - 1) != var3.length())) {
         return false;
      } else {
         return (float)var6.getHeight() <= var2.bottom;
      }
   }

   private boolean supportsAutoSizeText() {
      boolean var1;
      if (!(this.mTextView instanceof AppCompatEditText)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void validateAndSetAutoSizeTextTypeUniformConfiguration(float var1, float var2, float var3) throws IllegalArgumentException {
      StringBuilder var4;
      if (var1 <= 0.0F) {
         var4 = new StringBuilder();
         var4.append("Minimum auto-size text size (");
         var4.append(var1);
         var4.append("px) is less or equal to (0px)");
         throw new IllegalArgumentException(var4.toString());
      } else if (var2 <= var1) {
         var4 = new StringBuilder();
         var4.append("Maximum auto-size text size (");
         var4.append(var2);
         var4.append("px) is less or equal to minimum auto-size ");
         var4.append("text size (");
         var4.append(var1);
         var4.append("px)");
         throw new IllegalArgumentException(var4.toString());
      } else if (var3 <= 0.0F) {
         var4 = new StringBuilder();
         var4.append("The auto-size step granularity (");
         var4.append(var3);
         var4.append("px) is less or equal to (0px)");
         throw new IllegalArgumentException(var4.toString());
      } else {
         this.mAutoSizeTextType = 1;
         this.mAutoSizeMinTextSizeInPx = var1;
         this.mAutoSizeMaxTextSizeInPx = var2;
         this.mAutoSizeStepGranularityInPx = var3;
         this.mHasPresetAutoSizeValues = false;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void autoSizeText() {
      if (this.isAutoSizeEnabled()) {
         if (this.mNeedsAutoSizeText) {
            label269: {
               if (this.mTextView.getMeasuredHeight() <= 0 || this.mTextView.getMeasuredWidth() <= 0) {
                  return;
               }

               int var1;
               if ((Boolean)this.invokeAndReturnWithDefault(this.mTextView, "getHorizontallyScrolling", false)) {
                  var1 = 1048576;
               } else {
                  var1 = this.mTextView.getMeasuredWidth() - this.mTextView.getTotalPaddingLeft() - this.mTextView.getTotalPaddingRight();
               }

               int var2 = this.mTextView.getHeight() - this.mTextView.getCompoundPaddingBottom() - this.mTextView.getCompoundPaddingTop();
               if (var1 <= 0 || var2 <= 0) {
                  return;
               }

               RectF var3 = TEMP_RECTF;
               synchronized(var3){}

               Throwable var10000;
               boolean var10001;
               label258: {
                  try {
                     TEMP_RECTF.setEmpty();
                     TEMP_RECTF.right = (float)var1;
                     TEMP_RECTF.bottom = (float)var2;
                     float var4 = (float)this.findLargestTextSizeWhichFits(TEMP_RECTF);
                     if (var4 != this.mTextView.getTextSize()) {
                        this.setTextSizeInternal(0, var4);
                     }
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label258;
                  }

                  label255:
                  try {
                     break label269;
                  } catch (Throwable var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label255;
                  }
               }

               while(true) {
                  Throwable var5 = var10000;

                  try {
                     throw var5;
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     continue;
                  }
               }
            }
         }

         this.mNeedsAutoSizeText = true;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getAutoSizeMaxTextSize() {
      return Math.round(this.mAutoSizeMaxTextSizeInPx);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getAutoSizeMinTextSize() {
      return Math.round(this.mAutoSizeMinTextSizeInPx);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getAutoSizeStepGranularity() {
      return Math.round(this.mAutoSizeStepGranularityInPx);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int[] getAutoSizeTextAvailableSizes() {
      return this.mAutoSizeTextSizesInPx;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getAutoSizeTextType() {
      return this.mAutoSizeTextType;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   boolean isAutoSizeEnabled() {
      boolean var1;
      if (this.supportsAutoSizeText() && this.mAutoSizeTextType != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      TypedArray var3 = this.mContext.obtainStyledAttributes(var1, R.styleable.AppCompatTextView, var2, 0);
      if (var3.hasValue(R.styleable.AppCompatTextView_autoSizeTextType)) {
         this.mAutoSizeTextType = var3.getInt(R.styleable.AppCompatTextView_autoSizeTextType, 0);
      }

      float var4;
      if (var3.hasValue(R.styleable.AppCompatTextView_autoSizeStepGranularity)) {
         var4 = var3.getDimension(R.styleable.AppCompatTextView_autoSizeStepGranularity, -1.0F);
      } else {
         var4 = -1.0F;
      }

      float var5;
      if (var3.hasValue(R.styleable.AppCompatTextView_autoSizeMinTextSize)) {
         var5 = var3.getDimension(R.styleable.AppCompatTextView_autoSizeMinTextSize, -1.0F);
      } else {
         var5 = -1.0F;
      }

      float var6;
      if (var3.hasValue(R.styleable.AppCompatTextView_autoSizeMaxTextSize)) {
         var6 = var3.getDimension(R.styleable.AppCompatTextView_autoSizeMaxTextSize, -1.0F);
      } else {
         var6 = -1.0F;
      }

      if (var3.hasValue(R.styleable.AppCompatTextView_autoSizePresetSizes)) {
         var2 = var3.getResourceId(R.styleable.AppCompatTextView_autoSizePresetSizes, 0);
         if (var2 > 0) {
            TypedArray var8 = var3.getResources().obtainTypedArray(var2);
            this.setupAutoSizeUniformPresetSizes(var8);
            var8.recycle();
         }
      }

      var3.recycle();
      if (this.supportsAutoSizeText()) {
         if (this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues) {
               DisplayMetrics var9 = this.mContext.getResources().getDisplayMetrics();
               float var7 = var5;
               if (var5 == -1.0F) {
                  var7 = TypedValue.applyDimension(2, 12.0F, var9);
               }

               var5 = var6;
               if (var6 == -1.0F) {
                  var5 = TypedValue.applyDimension(2, 112.0F, var9);
               }

               var6 = var4;
               if (var4 == -1.0F) {
                  var6 = 1.0F;
               }

               this.validateAndSetAutoSizeTextTypeUniformConfiguration(var7, var5, var6);
            }

            this.setupAutoSizeText();
         }
      } else {
         this.mAutoSizeTextType = 0;
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException {
      if (this.supportsAutoSizeText()) {
         DisplayMetrics var5 = this.mContext.getResources().getDisplayMetrics();
         this.validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(var4, (float)var1, var5), TypedValue.applyDimension(var4, (float)var2, var5), TypedValue.applyDimension(var4, (float)var3, var5));
         if (this.setupAutoSizeText()) {
            this.autoSizeText();
         }
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] var1, int var2) throws IllegalArgumentException {
      if (this.supportsAutoSizeText()) {
         int var3 = 0;
         int var4 = var1.length;
         if (var4 <= 0) {
            this.mHasPresetAutoSizeValues = false;
         } else {
            int[] var5 = new int[var4];
            int[] var6;
            if (var2 == 0) {
               var6 = Arrays.copyOf(var1, var4);
            } else {
               DisplayMetrics var7 = this.mContext.getResources().getDisplayMetrics();

               while(true) {
                  var6 = var5;
                  if (var3 >= var4) {
                     break;
                  }

                  var5[var3] = Math.round(TypedValue.applyDimension(var2, (float)var1[var3], var7));
                  ++var3;
               }
            }

            this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(var6);
            if (!this.setupAutoSizeUniformPresetSizesConfiguration()) {
               StringBuilder var8 = new StringBuilder();
               var8.append("None of the preset sizes is valid: ");
               var8.append(Arrays.toString(var1));
               throw new IllegalArgumentException(var8.toString());
            }
         }

         if (this.setupAutoSizeText()) {
            this.autoSizeText();
         }
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void setAutoSizeTextTypeWithDefaults(int var1) {
      if (this.supportsAutoSizeText()) {
         switch(var1) {
         case 0:
            this.clearAutoSizeConfiguration();
            break;
         case 1:
            DisplayMetrics var2 = this.mContext.getResources().getDisplayMetrics();
            this.validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(2, 12.0F, var2), TypedValue.applyDimension(2, 112.0F, var2), 1.0F);
            if (this.setupAutoSizeText()) {
               this.autoSizeText();
            }
            break;
         default:
            StringBuilder var3 = new StringBuilder();
            var3.append("Unknown auto-size text type: ");
            var3.append(var1);
            throw new IllegalArgumentException(var3.toString());
         }
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void setTextSizeInternal(int var1, float var2) {
      Resources var3;
      if (this.mContext == null) {
         var3 = Resources.getSystem();
      } else {
         var3 = this.mContext.getResources();
      }

      this.setRawTextSize(TypedValue.applyDimension(var1, var2, var3.getDisplayMetrics()));
   }
}
