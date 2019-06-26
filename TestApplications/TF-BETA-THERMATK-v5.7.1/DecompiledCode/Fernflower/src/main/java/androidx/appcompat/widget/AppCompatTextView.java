package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.ActionMode.Callback;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import androidx.core.text.PrecomputedTextCompat;
import androidx.core.view.TintableBackgroundView;
import androidx.core.widget.AutoSizeableTextView;
import androidx.core.widget.TextViewCompat;
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
      Future var1 = this.mPrecomputedTextFuture;
      if (var1 != null) {
         try {
            this.mPrecomputedTextFuture = null;
            TextViewCompat.setPrecomputedText(this, (PrecomputedTextCompat)var1.get());
         } catch (ExecutionException | InterruptedException var2) {
         }
      }

   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      if (var1 != null) {
         var1.applySupportBackgroundTint();
      }

      AppCompatTextHelper var2 = this.mTextHelper;
      if (var2 != null) {
         var2.applyCompoundDrawablesTints();
      }

   }

   public int getAutoSizeMaxTextSize() {
      if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
         return super.getAutoSizeMaxTextSize();
      } else {
         AppCompatTextHelper var1 = this.mTextHelper;
         return var1 != null ? var1.getAutoSizeMaxTextSize() : -1;
      }
   }

   public int getAutoSizeMinTextSize() {
      if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
         return super.getAutoSizeMinTextSize();
      } else {
         AppCompatTextHelper var1 = this.mTextHelper;
         return var1 != null ? var1.getAutoSizeMinTextSize() : -1;
      }
   }

   public int getAutoSizeStepGranularity() {
      if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
         return super.getAutoSizeStepGranularity();
      } else {
         AppCompatTextHelper var1 = this.mTextHelper;
         return var1 != null ? var1.getAutoSizeStepGranularity() : -1;
      }
   }

   public int[] getAutoSizeTextAvailableSizes() {
      if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
         return super.getAutoSizeTextAvailableSizes();
      } else {
         AppCompatTextHelper var1 = this.mTextHelper;
         return var1 != null ? var1.getAutoSizeTextAvailableSizes() : new int[0];
      }
   }

   public int getAutoSizeTextType() {
      boolean var1 = AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE;
      byte var2 = 0;
      if (var1) {
         if (super.getAutoSizeTextType() == 1) {
            var2 = 1;
         }

         return var2;
      } else {
         AppCompatTextHelper var3 = this.mTextHelper;
         return var3 != null ? var3.getAutoSizeTextType() : 0;
      }
   }

   public int getFirstBaselineToTopHeight() {
      return TextViewCompat.getFirstBaselineToTopHeight(this);
   }

   public int getLastBaselineToBottomHeight() {
      return TextViewCompat.getLastBaselineToBottomHeight(this);
   }

   public ColorStateList getSupportBackgroundTintList() {
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      ColorStateList var2;
      if (var1 != null) {
         var2 = var1.getSupportBackgroundTintList();
      } else {
         var2 = null;
      }

      return var2;
   }

   public Mode getSupportBackgroundTintMode() {
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      Mode var2;
      if (var1 != null) {
         var2 = var1.getSupportBackgroundTintMode();
      } else {
         var2 = null;
      }

      return var2;
   }

   public CharSequence getText() {
      this.consumeTextFutureAndSetBlocking();
      return super.getText();
   }

   public PrecomputedTextCompat.Params getTextMetricsParamsCompat() {
      return TextViewCompat.getTextMetricsParams(this);
   }

   public InputConnection onCreateInputConnection(EditorInfo var1) {
      InputConnection var2 = super.onCreateInputConnection(var1);
      AppCompatHintHelper.onCreateInputConnection(var2, var1, this);
      return var2;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      AppCompatTextHelper var6 = this.mTextHelper;
      if (var6 != null) {
         var6.onLayout(var1, var2, var3, var4, var5);
      }

   }

   protected void onMeasure(int var1, int var2) {
      this.consumeTextFutureAndSetBlocking();
      super.onMeasure(var1, var2);
   }

   protected void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      super.onTextChanged(var1, var2, var3, var4);
      AppCompatTextHelper var5 = this.mTextHelper;
      if (var5 != null && !AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && var5.isAutoSizeEnabled()) {
         this.mTextHelper.autoSizeText();
      }

   }

   public void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException {
      if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
         super.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
      } else {
         AppCompatTextHelper var5 = this.mTextHelper;
         if (var5 != null) {
            var5.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
         }
      }

   }

   public void setAutoSizeTextTypeUniformWithPresetSizes(int[] var1, int var2) throws IllegalArgumentException {
      if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
         super.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
      } else {
         AppCompatTextHelper var3 = this.mTextHelper;
         if (var3 != null) {
            var3.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
         }
      }

   }

   public void setAutoSizeTextTypeWithDefaults(int var1) {
      if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
         super.setAutoSizeTextTypeWithDefaults(var1);
      } else {
         AppCompatTextHelper var2 = this.mTextHelper;
         if (var2 != null) {
            var2.setAutoSizeTextTypeWithDefaults(var1);
         }
      }

   }

   public void setBackgroundDrawable(Drawable var1) {
      super.setBackgroundDrawable(var1);
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.onSetBackgroundDrawable(var1);
      }

   }

   public void setBackgroundResource(int var1) {
      super.setBackgroundResource(var1);
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.onSetBackgroundResource(var1);
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
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.setSupportBackgroundTintList(var1);
      }

   }

   public void setSupportBackgroundTintMode(Mode var1) {
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.setSupportBackgroundTintMode(var1);
      }

   }

   public void setTextAppearance(Context var1, int var2) {
      super.setTextAppearance(var1, var2);
      AppCompatTextHelper var3 = this.mTextHelper;
      if (var3 != null) {
         var3.onSetTextAppearance(var1, var2);
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
      if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
         super.setTextSize(var1, var2);
      } else {
         AppCompatTextHelper var3 = this.mTextHelper;
         if (var3 != null) {
            var3.setTextSize(var1, var2);
         }
      }

   }
}
