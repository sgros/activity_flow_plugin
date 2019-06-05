package org.mozilla.permissionhandler;

import android.os.Parcelable;
import android.support.design.widget.Snackbar;

public interface PermissionHandle {
    void doActionDirect(String str, int i, Parcelable parcelable);

    void doActionGranted(String str, int i, Parcelable parcelable);

    void doActionNoPermission(String str, int i, Parcelable parcelable);

    void doActionSetting(String str, int i, Parcelable parcelable);

    Snackbar makeAskAgainSnackBar(int i);

    void permissionDeniedToast(int i);

    void requestPermissions(int i);
}
