package android.support.v7.widget;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.ActionMode.Callback;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.CheckedTextView;

public class AppCompatCheckedTextView extends CheckedTextView {
   private static final int[] TINT_ATTRS = new int[]{16843016};
   private final AppCompatTextHelper mTextHelper;

   public AppCompatCheckedTextView(Context var1, AttributeSet var2) {
      this(var1, var2, 16843720);
   }

   public AppCompatCheckedTextView(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      this.mTextHelper = new AppCompatTextHelper(this);
      this.mTextHelper.loadFromAttributes(var2, var3);
      this.mTextHelper.applyCompoundDrawablesTints();
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(this.getContext(), var2, TINT_ATTRS, var3, 0);
      this.setCheckMarkDrawable(var4.getDrawable(0));
      var4.recycle();
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      if (this.mTextHelper != null) {
         this.mTextHelper.applyCompoundDrawablesTints();
      }

   }

   public InputConnection onCreateInputConnection(EditorInfo var1) {
      return AppCompatHintHelper.onCreateInputConnection(super.onCreateInputConnection(var1), var1, this);
   }

   public void setCheckMarkDrawable(int var1) {
      this.setCheckMarkDrawable(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setCustomSelectionActionModeCallback(Callback var1) {
      super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, var1));
   }

   public void setTextAppearance(Context var1, int var2) {
      super.setTextAppearance(var1, var2);
      if (this.mTextHelper != null) {
         this.mTextHelper.onSetTextAppearance(var1, var2);
      }

   }
}
