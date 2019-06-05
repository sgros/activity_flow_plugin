package android.support.v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListAdapter;

public class AlertDialog extends AppCompatDialog implements DialogInterface {
   final AlertController mAlert = new AlertController(this.getContext(), this, this.getWindow());

   protected AlertDialog(Context var1, int var2) {
      super(var1, resolveDialogTheme(var1, var2));
   }

   static int resolveDialogTheme(Context var0, int var1) {
      if ((var1 >>> 24 & 255) >= 1) {
         return var1;
      } else {
         TypedValue var2 = new TypedValue();
         var0.getTheme().resolveAttribute(R.attr.alertDialogTheme, var2, true);
         return var2.resourceId;
      }
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

   public void setTitle(CharSequence var1) {
      super.setTitle(var1);
      this.mAlert.setTitle(var1);
   }

   public void setView(View var1) {
      this.mAlert.setView(var1);
   }

   public static class Builder {
      private final AlertController.AlertParams P;
      private final int mTheme;

      public Builder(Context var1) {
         this(var1, AlertDialog.resolveDialogTheme(var1, 0));
      }

      public Builder(Context var1, int var2) {
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

      public AlertDialog.Builder setCustomTitle(View var1) {
         this.P.mCustomTitleView = var1;
         return this;
      }

      public AlertDialog.Builder setIcon(Drawable var1) {
         this.P.mIcon = var1;
         return this;
      }

      public AlertDialog.Builder setMessage(CharSequence var1) {
         this.P.mMessage = var1;
         return this;
      }

      public AlertDialog.Builder setNegativeButton(int var1, OnClickListener var2) {
         this.P.mNegativeButtonText = this.P.mContext.getText(var1);
         this.P.mNegativeButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setNegativeButton(CharSequence var1, OnClickListener var2) {
         this.P.mNegativeButtonText = var1;
         this.P.mNegativeButtonListener = var2;
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

      public AlertDialog.Builder setOnKeyListener(OnKeyListener var1) {
         this.P.mOnKeyListener = var1;
         return this;
      }

      public AlertDialog.Builder setPositiveButton(int var1, OnClickListener var2) {
         this.P.mPositiveButtonText = this.P.mContext.getText(var1);
         this.P.mPositiveButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setPositiveButton(CharSequence var1, OnClickListener var2) {
         this.P.mPositiveButtonText = var1;
         this.P.mPositiveButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setTitle(int var1) {
         this.P.mTitle = this.P.mContext.getText(var1);
         return this;
      }

      public AlertDialog.Builder setTitle(CharSequence var1) {
         this.P.mTitle = var1;
         return this;
      }

      public AlertDialog.Builder setView(View var1) {
         this.P.mView = var1;
         this.P.mViewLayoutResId = 0;
         this.P.mViewSpacingSpecified = false;
         return this;
      }
   }
}
