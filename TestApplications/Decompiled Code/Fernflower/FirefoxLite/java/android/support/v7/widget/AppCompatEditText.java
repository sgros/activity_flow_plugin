package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.appcompat.R;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.ActionMode.Callback;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

public class AppCompatEditText extends EditText implements TintableBackgroundView {
   private final AppCompatBackgroundHelper mBackgroundTintHelper;
   private final AppCompatTextHelper mTextHelper;

   public AppCompatEditText(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatEditText(Context var1, AttributeSet var2) {
      this(var1, var2, R.attr.editTextStyle);
   }

   public AppCompatEditText(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      this.mBackgroundTintHelper = new AppCompatBackgroundHelper(this);
      this.mBackgroundTintHelper.loadFromAttributes(var2, var3);
      this.mTextHelper = new AppCompatTextHelper(this);
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

   public Editable getText() {
      return VERSION.SDK_INT >= 28 ? super.getText() : super.getEditableText();
   }

   public InputConnection onCreateInputConnection(EditorInfo var1) {
      return AppCompatHintHelper.onCreateInputConnection(super.onCreateInputConnection(var1), var1, this);
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
}
