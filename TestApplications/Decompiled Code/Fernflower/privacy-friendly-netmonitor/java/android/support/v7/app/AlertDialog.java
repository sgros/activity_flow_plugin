package android.support.v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AlertDialog extends AppCompatDialog implements DialogInterface {
   static final int LAYOUT_HINT_NONE = 0;
   static final int LAYOUT_HINT_SIDE = 1;
   final AlertController mAlert;

   protected AlertDialog(@NonNull Context var1) {
      this(var1, 0);
   }

   protected AlertDialog(@NonNull Context var1, @StyleRes int var2) {
      super(var1, resolveDialogTheme(var1, var2));
      this.mAlert = new AlertController(this.getContext(), this, this.getWindow());
   }

   protected AlertDialog(@NonNull Context var1, boolean var2, @Nullable OnCancelListener var3) {
      this(var1, 0);
      this.setCancelable(var2);
      this.setOnCancelListener(var3);
   }

   static int resolveDialogTheme(@NonNull Context var0, @StyleRes int var1) {
      if ((var1 >>> 24 & 255) >= 1) {
         return var1;
      } else {
         TypedValue var2 = new TypedValue();
         var0.getTheme().resolveAttribute(R.attr.alertDialogTheme, var2, true);
         return var2.resourceId;
      }
   }

   public Button getButton(int var1) {
      return this.mAlert.getButton(var1);
   }

   public ListView getListView() {
      return this.mAlert.getListView();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.mAlert.installContent();
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      return this.mAlert.onKeyDown(var1, var2) ? true : super.onKeyDown(var1, var2);
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      return this.mAlert.onKeyUp(var1, var2) ? true : super.onKeyUp(var1, var2);
   }

   public void setButton(int var1, CharSequence var2, OnClickListener var3) {
      this.mAlert.setButton(var1, var2, var3, (Message)null);
   }

   public void setButton(int var1, CharSequence var2, Message var3) {
      this.mAlert.setButton(var1, var2, (OnClickListener)null, var3);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void setButtonPanelLayoutHint(int var1) {
      this.mAlert.setButtonPanelLayoutHint(var1);
   }

   public void setCustomTitle(View var1) {
      this.mAlert.setCustomTitle(var1);
   }

   public void setIcon(int var1) {
      this.mAlert.setIcon(var1);
   }

   public void setIcon(Drawable var1) {
      this.mAlert.setIcon(var1);
   }

   public void setIconAttribute(int var1) {
      TypedValue var2 = new TypedValue();
      this.getContext().getTheme().resolveAttribute(var1, var2, true);
      this.mAlert.setIcon(var2.resourceId);
   }

   public void setMessage(CharSequence var1) {
      this.mAlert.setMessage(var1);
   }

   public void setTitle(CharSequence var1) {
      super.setTitle(var1);
      this.mAlert.setTitle(var1);
   }

   public void setView(View var1) {
      this.mAlert.setView(var1);
   }

   public void setView(View var1, int var2, int var3, int var4, int var5) {
      this.mAlert.setView(var1, var2, var3, var4, var5);
   }

   public static class Builder {
      private final AlertController.AlertParams P;
      private final int mTheme;

      public Builder(@NonNull Context var1) {
         this(var1, AlertDialog.resolveDialogTheme(var1, 0));
      }

      public Builder(@NonNull Context var1, @StyleRes int var2) {
         this.P = new AlertController.AlertParams(new ContextThemeWrapper(var1, AlertDialog.resolveDialogTheme(var1, var2)));
         this.mTheme = var2;
      }

      public AlertDialog create() {
         AlertDialog var1 = new AlertDialog(this.P.mContext, this.mTheme);
         this.P.apply(var1.mAlert);
         var1.setCancelable(this.P.mCancelable);
         if (this.P.mCancelable) {
            var1.setCanceledOnTouchOutside(true);
         }

         var1.setOnCancelListener(this.P.mOnCancelListener);
         var1.setOnDismissListener(this.P.mOnDismissListener);
         if (this.P.mOnKeyListener != null) {
            var1.setOnKeyListener(this.P.mOnKeyListener);
         }

         return var1;
      }

      @NonNull
      public Context getContext() {
         return this.P.mContext;
      }

      public AlertDialog.Builder setAdapter(ListAdapter var1, OnClickListener var2) {
         this.P.mAdapter = var1;
         this.P.mOnClickListener = var2;
         return this;
      }

      public AlertDialog.Builder setCancelable(boolean var1) {
         this.P.mCancelable = var1;
         return this;
      }

      public AlertDialog.Builder setCursor(Cursor var1, OnClickListener var2, String var3) {
         this.P.mCursor = var1;
         this.P.mLabelColumn = var3;
         this.P.mOnClickListener = var2;
         return this;
      }

      public AlertDialog.Builder setCustomTitle(@Nullable View var1) {
         this.P.mCustomTitleView = var1;
         return this;
      }

      public AlertDialog.Builder setIcon(@DrawableRes int var1) {
         this.P.mIconId = var1;
         return this;
      }

      public AlertDialog.Builder setIcon(@Nullable Drawable var1) {
         this.P.mIcon = var1;
         return this;
      }

      public AlertDialog.Builder setIconAttribute(@AttrRes int var1) {
         TypedValue var2 = new TypedValue();
         this.P.mContext.getTheme().resolveAttribute(var1, var2, true);
         this.P.mIconId = var2.resourceId;
         return this;
      }

      @Deprecated
      public AlertDialog.Builder setInverseBackgroundForced(boolean var1) {
         this.P.mForceInverseBackground = var1;
         return this;
      }

      public AlertDialog.Builder setItems(@ArrayRes int var1, OnClickListener var2) {
         this.P.mItems = this.P.mContext.getResources().getTextArray(var1);
         this.P.mOnClickListener = var2;
         return this;
      }

      public AlertDialog.Builder setItems(CharSequence[] var1, OnClickListener var2) {
         this.P.mItems = var1;
         this.P.mOnClickListener = var2;
         return this;
      }

      public AlertDialog.Builder setMessage(@StringRes int var1) {
         this.P.mMessage = this.P.mContext.getText(var1);
         return this;
      }

      public AlertDialog.Builder setMessage(@Nullable CharSequence var1) {
         this.P.mMessage = var1;
         return this;
      }

      public AlertDialog.Builder setMultiChoiceItems(@ArrayRes int var1, boolean[] var2, OnMultiChoiceClickListener var3) {
         this.P.mItems = this.P.mContext.getResources().getTextArray(var1);
         this.P.mOnCheckboxClickListener = var3;
         this.P.mCheckedItems = var2;
         this.P.mIsMultiChoice = true;
         return this;
      }

      public AlertDialog.Builder setMultiChoiceItems(Cursor var1, String var2, String var3, OnMultiChoiceClickListener var4) {
         this.P.mCursor = var1;
         this.P.mOnCheckboxClickListener = var4;
         this.P.mIsCheckedColumn = var2;
         this.P.mLabelColumn = var3;
         this.P.mIsMultiChoice = true;
         return this;
      }

      public AlertDialog.Builder setMultiChoiceItems(CharSequence[] var1, boolean[] var2, OnMultiChoiceClickListener var3) {
         this.P.mItems = var1;
         this.P.mOnCheckboxClickListener = var3;
         this.P.mCheckedItems = var2;
         this.P.mIsMultiChoice = true;
         return this;
      }

      public AlertDialog.Builder setNegativeButton(@StringRes int var1, OnClickListener var2) {
         this.P.mNegativeButtonText = this.P.mContext.getText(var1);
         this.P.mNegativeButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setNegativeButton(CharSequence var1, OnClickListener var2) {
         this.P.mNegativeButtonText = var1;
         this.P.mNegativeButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setNeutralButton(@StringRes int var1, OnClickListener var2) {
         this.P.mNeutralButtonText = this.P.mContext.getText(var1);
         this.P.mNeutralButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setNeutralButton(CharSequence var1, OnClickListener var2) {
         this.P.mNeutralButtonText = var1;
         this.P.mNeutralButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setOnCancelListener(OnCancelListener var1) {
         this.P.mOnCancelListener = var1;
         return this;
      }

      public AlertDialog.Builder setOnDismissListener(OnDismissListener var1) {
         this.P.mOnDismissListener = var1;
         return this;
      }

      public AlertDialog.Builder setOnItemSelectedListener(OnItemSelectedListener var1) {
         this.P.mOnItemSelectedListener = var1;
         return this;
      }

      public AlertDialog.Builder setOnKeyListener(OnKeyListener var1) {
         this.P.mOnKeyListener = var1;
         return this;
      }

      public AlertDialog.Builder setPositiveButton(@StringRes int var1, OnClickListener var2) {
         this.P.mPositiveButtonText = this.P.mContext.getText(var1);
         this.P.mPositiveButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setPositiveButton(CharSequence var1, OnClickListener var2) {
         this.P.mPositiveButtonText = var1;
         this.P.mPositiveButtonListener = var2;
         return this;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public AlertDialog.Builder setRecycleOnMeasureEnabled(boolean var1) {
         this.P.mRecycleOnMeasure = var1;
         return this;
      }

      public AlertDialog.Builder setSingleChoiceItems(@ArrayRes int var1, int var2, OnClickListener var3) {
         this.P.mItems = this.P.mContext.getResources().getTextArray(var1);
         this.P.mOnClickListener = var3;
         this.P.mCheckedItem = var2;
         this.P.mIsSingleChoice = true;
         return this;
      }

      public AlertDialog.Builder setSingleChoiceItems(Cursor var1, int var2, String var3, OnClickListener var4) {
         this.P.mCursor = var1;
         this.P.mOnClickListener = var4;
         this.P.mCheckedItem = var2;
         this.P.mLabelColumn = var3;
         this.P.mIsSingleChoice = true;
         return this;
      }

      public AlertDialog.Builder setSingleChoiceItems(ListAdapter var1, int var2, OnClickListener var3) {
         this.P.mAdapter = var1;
         this.P.mOnClickListener = var3;
         this.P.mCheckedItem = var2;
         this.P.mIsSingleChoice = true;
         return this;
      }

      public AlertDialog.Builder setSingleChoiceItems(CharSequence[] var1, int var2, OnClickListener var3) {
         this.P.mItems = var1;
         this.P.mOnClickListener = var3;
         this.P.mCheckedItem = var2;
         this.P.mIsSingleChoice = true;
         return this;
      }

      public AlertDialog.Builder setTitle(@StringRes int var1) {
         this.P.mTitle = this.P.mContext.getText(var1);
         return this;
      }

      public AlertDialog.Builder setTitle(@Nullable CharSequence var1) {
         this.P.mTitle = var1;
         return this;
      }

      public AlertDialog.Builder setView(int var1) {
         this.P.mView = null;
         this.P.mViewLayoutResId = var1;
         this.P.mViewSpacingSpecified = false;
         return this;
      }

      public AlertDialog.Builder setView(View var1) {
         this.P.mView = var1;
         this.P.mViewLayoutResId = 0;
         this.P.mViewSpacingSpecified = false;
         return this;
      }

      @Deprecated
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public AlertDialog.Builder setView(View var1, int var2, int var3, int var4, int var5) {
         this.P.mView = var1;
         this.P.mViewLayoutResId = 0;
         this.P.mViewSpacingSpecified = true;
         this.P.mViewSpacingLeft = var2;
         this.P.mViewSpacingTop = var3;
         this.P.mViewSpacingRight = var4;
         this.P.mViewSpacingBottom = var5;
         return this;
      }

      public AlertDialog show() {
         AlertDialog var1 = this.create();
         var1.show();
         return var1;
      }
   }
}
