package org.mozilla.permissionhandler;

import android.os.Parcelable;
import android.support.design.widget.Snackbar;

public interface PermissionHandle {
   void doActionDirect(String var1, int var2, Parcelable var3);

   void doActionGranted(String var1, int var2, Parcelable var3);

   void doActionNoPermission(String var1, int var2, Parcelable var3);

   void doActionSetting(String var1, int var2, Parcelable var3);

   Snackbar makeAskAgainSnackBar(int var1);

   void permissionDeniedToast(int var1);

   void requestPermissions(int var1);
}
