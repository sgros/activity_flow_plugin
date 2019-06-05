package android.support.v4.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class DialogFragment extends Fragment implements OnCancelListener, OnDismissListener {
   int mBackStackId = -1;
   boolean mCancelable = true;
   Dialog mDialog;
   boolean mDismissed;
   boolean mShownByMe;
   boolean mShowsDialog = true;
   int mStyle = 0;
   int mTheme = 0;
   boolean mViewDestroyed;

   public void dismiss() {
      this.dismissInternal(false);
   }

   public void dismissAllowingStateLoss() {
      this.dismissInternal(true);
   }

   void dismissInternal(boolean var1) {
      if (!this.mDismissed) {
         this.mDismissed = true;
         this.mShownByMe = false;
         if (this.mDialog != null) {
            this.mDialog.dismiss();
         }

         this.mViewDestroyed = true;
         if (this.mBackStackId >= 0) {
            this.getFragmentManager().popBackStack(this.mBackStackId, 1);
            this.mBackStackId = -1;
         } else {
            FragmentTransaction var2 = this.getFragmentManager().beginTransaction();
            var2.remove(this);
            if (var1) {
               var2.commitAllowingStateLoss();
            } else {
               var2.commit();
            }
         }

      }
   }

   public Dialog getDialog() {
      return this.mDialog;
   }

   public int getTheme() {
      return this.mTheme;
   }

   public void onActivityCreated(Bundle var1) {
      super.onActivityCreated(var1);
      if (this.mShowsDialog) {
         View var2 = this.getView();
         if (var2 != null) {
            if (var2.getParent() != null) {
               throw new IllegalStateException("DialogFragment can not be attached to a container view");
            }

            this.mDialog.setContentView(var2);
         }

         FragmentActivity var3 = this.getActivity();
         if (var3 != null) {
            this.mDialog.setOwnerActivity(var3);
         }

         this.mDialog.setCancelable(this.mCancelable);
         this.mDialog.setOnCancelListener(this);
         this.mDialog.setOnDismissListener(this);
         if (var1 != null) {
            var1 = var1.getBundle("android:savedDialogState");
            if (var1 != null) {
               this.mDialog.onRestoreInstanceState(var1);
            }
         }

      }
   }

   public void onAttach(Context var1) {
      super.onAttach(var1);
      if (!this.mShownByMe) {
         this.mDismissed = false;
      }

   }

   public void onCancel(DialogInterface var1) {
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      boolean var2;
      if (this.mContainerId == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mShowsDialog = var2;
      if (var1 != null) {
         this.mStyle = var1.getInt("android:style", 0);
         this.mTheme = var1.getInt("android:theme", 0);
         this.mCancelable = var1.getBoolean("android:cancelable", true);
         this.mShowsDialog = var1.getBoolean("android:showsDialog", this.mShowsDialog);
         this.mBackStackId = var1.getInt("android:backStackId", -1);
      }

   }

   public Dialog onCreateDialog(Bundle var1) {
      return new Dialog(this.getActivity(), this.getTheme());
   }

   public void onDestroyView() {
      super.onDestroyView();
      if (this.mDialog != null) {
         this.mViewDestroyed = true;
         this.mDialog.dismiss();
         this.mDialog = null;
      }

   }

   public void onDetach() {
      super.onDetach();
      if (!this.mShownByMe && !this.mDismissed) {
         this.mDismissed = true;
      }

   }

   public void onDismiss(DialogInterface var1) {
      if (!this.mViewDestroyed) {
         this.dismissInternal(true);
      }

   }

   public LayoutInflater onGetLayoutInflater(Bundle var1) {
      if (!this.mShowsDialog) {
         return super.onGetLayoutInflater(var1);
      } else {
         this.mDialog = this.onCreateDialog(var1);
         if (this.mDialog != null) {
            this.setupDialog(this.mDialog, this.mStyle);
            return (LayoutInflater)this.mDialog.getContext().getSystemService("layout_inflater");
         } else {
            return (LayoutInflater)this.mHost.getContext().getSystemService("layout_inflater");
         }
      }
   }

   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      if (this.mDialog != null) {
         Bundle var2 = this.mDialog.onSaveInstanceState();
         if (var2 != null) {
            var1.putBundle("android:savedDialogState", var2);
         }
      }

      if (this.mStyle != 0) {
         var1.putInt("android:style", this.mStyle);
      }

      if (this.mTheme != 0) {
         var1.putInt("android:theme", this.mTheme);
      }

      if (!this.mCancelable) {
         var1.putBoolean("android:cancelable", this.mCancelable);
      }

      if (!this.mShowsDialog) {
         var1.putBoolean("android:showsDialog", this.mShowsDialog);
      }

      if (this.mBackStackId != -1) {
         var1.putInt("android:backStackId", this.mBackStackId);
      }

   }

   public void onStart() {
      super.onStart();
      if (this.mDialog != null) {
         this.mViewDestroyed = false;
         this.mDialog.show();
      }

   }

   public void onStop() {
      super.onStop();
      if (this.mDialog != null) {
         this.mDialog.hide();
      }

   }

   public void setCancelable(boolean var1) {
      this.mCancelable = var1;
      if (this.mDialog != null) {
         this.mDialog.setCancelable(var1);
      }

   }

   public void setStyle(int var1, int var2) {
      this.mStyle = var1;
      if (this.mStyle == 2 || this.mStyle == 3) {
         this.mTheme = 16973913;
      }

      if (var2 != 0) {
         this.mTheme = var2;
      }

   }

   public void setupDialog(Dialog var1, int var2) {
      switch(var2) {
      case 3:
         var1.getWindow().addFlags(24);
      case 1:
      case 2:
         var1.requestWindowFeature(1);
      default:
      }
   }

   public void show(FragmentManager var1, String var2) {
      this.mDismissed = false;
      this.mShownByMe = true;
      FragmentTransaction var3 = var1.beginTransaction();
      var3.add(this, var2);
      var3.commit();
   }
}
