package menion.android.whereyougo.gui.extension.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;

public abstract class CustomDialogFragment extends DialogFragment {
   public abstract Dialog createDialog(Bundle var1);

   public boolean isDialogVisible() {
      boolean var1;
      if (this.isAdded() && !this.isHidden()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void onActivityCreated(Bundle var1) {
      try {
         super.onActivityCreated(var1);
      } catch (Exception var2) {
         this.dismissAllowingStateLoss();
      }

   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setRetainInstance(this.shouldRetainInstance());
   }

   public Dialog onCreateDialog(Bundle var1) {
      Dialog var2 = this.createDialog(var1);
      if (var2 != null) {
         var2.setCancelable(this.isCancelable());
      }

      return var2;
   }

   public void onDestroy() {
      super.onDestroy();
   }

   public void onDestroyView() {
      if (this.getDialog() != null && this.getRetainInstance()) {
         this.getDialog().setDismissMessage((Message)null);
      }

      super.onDestroyView();
   }

   public void onDetach() {
      super.onDetach();
   }

   public boolean shouldRetainInstance() {
      return true;
   }
}
