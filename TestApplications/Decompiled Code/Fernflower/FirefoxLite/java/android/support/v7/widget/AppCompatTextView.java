package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.text.PrecomputedTextCompat;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.widget.AutoSizeableTextView;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.view.ActionMode.Callback;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AppCompatTextView extends TextView implements TintableBackgroundView, AutoSizeableTextView {
   private final AppCompatBackgroundHelper mBackgroundTintHelper;
   private Future mPrecomputedTextFuture;
   private final AppCompatTextHelper mTextHelper;

   public AppCompatTextView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatTextView(Context var1, AttributeSet var2) {
      this(var1, var2, 16842884);
   }

   public AppCompatTextView(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      this.mBackgroundTintHelper = new AppCompatBackgroundHelper(this);
      this.mBackgroundTintHelper.loadFromAttributes(var2, var3);
      this.mTextHelper = new AppCompatTextHelper(this);
      this.mTextHelper.loadFromAttributes(var2, var3);
      this.mTextHelper.applyCompoundDrawablesTints();
   }

   private void consumeTextFutureAndSetBlocking() {
      if (this.mPrecomputedTextFuture != null) {
         try {
            Future var1 = this.mPrecomputedTextFuture;
            this.mPrecomputedTextFuture = null;
            TextViewCompat.setPrecomputedText(this, (PrecomputedTextCompat)var1.get());
         } catch (ExecutionException | InterruptedException var2) {
         }
      }

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

   public int getAutoSizeMaxTextSize() {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         return super.getAutoSizeMaxTextSize();
      } else {
         return this.mTextHelper != null ? this.mTextHelper.getAutoSizeMaxTextSize() : -1;
      }
   }

   public int getAutoSizeMinTextSize() {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         return super.getAutoSizeMinTextSize();
      } else {
         return this.mTextHelper != null ? this.mTextHelper.getAutoSizeMinTextSize() : -1;
      }
   }

   public int getAutoSizeStepGranularity() {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         return super.getAutoSizeStepGranularity();
      } else {
         return this.mTextHelper != null ? this.mTextHelper.getAutoSizeStepGranularity() : -1;
      }
   }

   public int[] getAutoSizeTextAvailableSizes() {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         return super.getAutoSizeTextAvailableSizes();
      } else {
         return this.mTextHelper != null ? this.mTextHelper.getAutoSizeTextAvailableSizes() : new int[0];
      }
   }

   public int getAutoSizeTextType() {
      boolean var1 = PLATFORM_SUPPORTS_AUTOSIZE;
      byte var2 = 0;
      if (var1) {
         if (super.getAutoSizeTextType() == 1) {
            var2 = 1;
         }

         return var2;
      } else {
         return this.mTextHelper != null ? this.mTextHelper.getAutoSizeTextType() : 0;
      }
   }

   public int getFirstBaselineToTopHeight() {
      return TextViewCompat.getFirstBaselineToTopHeight(this);
   }

   public int getLastBaselineToBottomHeight() {
      return TextViewCompat.getLastBaselineToBottomHeight(this);
   }

   public ColorStateList getSupportBackgroundTintList() {
      ColorStateList var1;
      if (this.mBackgroundTintHelper != null) {
         var1 = this.mBackgroundTintHelper.getSupportBackgroundTintList();
      } else {
         var1 = null;
      }

      return var1;
   }

   public Mode getSupportBackgroundTintMode() {
      Mode var1;
      if (this.mBackgroundTintHelper != null) {
         var1 = this.mBackgroundTintHelper.getSupportBackgroundTintMode();
      } else {
         var1 = null;
      }

      return var1;
   }

   public CharSequence getText() {
      this.consumeTextFutureAndSetBlocking();
      return super.getText();
   }

   public PrecomputedTextCompat.Params getTextMetricsParamsCompat() {
      return TextViewCompat.getTextMetricsParams(this);
   }

   public InputConnection onCreateInputConnection(EditorInfo var1) {
      return AppCompatHintHelper.onCreateInputConnection(super.onCreateInputConnection(var1), var1, this);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (this.mTextHelper != null) {
         this.mTextHelper.onLayout(var1, var2, var3, var4, var5);
      }

   }

   protected void onMeasure(int var1, int var2) {
      this.consumeTextFutureAndSetBlocking();
      super.onMeasure(var1, var2);
   }

   protected void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      super.onTextChanged(var1, var2, var3, var4);
      if (this.mTextHelper != null && !PLATFORM_SUPPORTS_AUTOSIZE && this.mTextHelper.isAutoSizeEnabled()) {
         this.mTextHelper.autoSizeText();
      }

   }

   public void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         super.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
      } else if (this.mTextHelper != null) {
         this.mTextHelper.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
      }

   }

   public void setAutoSizeTextTypeUniformWithPresetSizes(int[] var1, int var2) throws IllegalArgumentException {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         super.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
      } else if (this.mTextHelper != null) {
         this.mTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
      }

   }

   public void setAutoSizeTextTypeWithDefaults(int var1) {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
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

   public void setBackgroundResource(int var1) {
      super.setBackgroundResource(var1);
      if (this.mBackgroundTintHelper != null) {
         this.mBackgroundTintHelper.onSetBackgroundResource(var1);
      }

   }

   public void setCustomSelectionActionModeCallback(Callback var1) {
      super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, var1));
   }

   public void setFirstBaselineToTopHeight(int var1) {
      if (VERSION.SDK_INT >= 28) {
         super.setFirstBaselineToTopHeight(var1);
      } else {
         TextViewCompat.setFirstBaselineToTopHeight(this, var1);
      }

   }

   public void setLastBaselineToBottomHeight(int var1) {
      if (VERSION.SDK_INT >= 28) {
         super.setLastBaselineToBottomHeight(var1);
      } else {
         TextViewCompat.setLastBaselineToBottomHeight(this, var1);
      }

   }

   public void setLineHeight(int var1) {
      TextViewCompat.setLineHeight(this, var1);
   }

   public void setPrecomputedText(PrecomputedTextCompat var1) {
      TextViewCompat.setPrecomputedText(this, var1);
   }

   public void setSupportBackgroundTintList(ColorStateList var1) {
      if (this.mBackgroundTintHelper != null) {
         this.mBackgroundTintHelper.setSupportBackgroundTintList(var1);
      }

   }

   public void setSupportBackgroundTintMode(Mode var1) {
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

   public void setTextFuture(Future var1) {
      this.mPrecomputedTextFuture = var1;
      this.requestLayout();
   }

   public void setTextMetricsParamsCompat(PrecomputedTextCompat.Params var1) {
      TextViewCompat.setTextMetricsParams(this, var1);
   }

   public void setTextSize(int var1, float var2) {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         super.setTextSize(var1, var2);
      } else if (this.mTextHelper != null) {
         this.mTextHelper.setTextSize(var1, var2);
      }

   }
}
