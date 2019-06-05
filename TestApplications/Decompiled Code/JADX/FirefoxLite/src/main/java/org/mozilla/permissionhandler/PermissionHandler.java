package org.mozilla.permissionhandler;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import android.support.p001v4.app.ActivityCompat;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.content.ContextCompat;
import android.view.View;
import org.mozilla.rocket.permissionhandler.C0597R;

public class PermissionHandler {
    private int actionId = -1;
    private Parcelable params;
    private String permission;
    private PermissionHandle permissionHandle;

    /* renamed from: org.mozilla.permissionhandler.PermissionHandler$3 */
    class C05853 extends Callback {
        C05853() {
        }

        public void onDismissed(Snackbar snackbar, int i) {
            if (i != 1) {
                PermissionHandler.this.permissionNotGranted();
            }
        }
    }

    public PermissionHandler(PermissionHandle permissionHandle) {
        this.permissionHandle = permissionHandle;
    }

    public void tryAction(final Activity activity, String str, int i, Parcelable parcelable) {
        tryAction(activity, str, i, parcelable, ActivityCompat.shouldShowRequestPermissionRationale(activity, str), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PermissionHandler.intentOpenSettings(activity, 39528);
            }
        });
    }

    public void tryAction(final Fragment fragment, String str, int i, Parcelable parcelable) {
        tryAction(fragment.getActivity(), str, i, parcelable, fragment.shouldShowRequestPermissionRationale(str), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PermissionHandler.intentOpenSettings(fragment, 39528);
            }
        });
    }

    private void tryAction(Activity activity, String str, int i, Parcelable parcelable, boolean z, OnClickListener onClickListener) {
        if (ContextCompat.checkSelfPermission(activity, str) == 0) {
            this.permissionHandle.doActionDirect(str, i, parcelable);
        } else if (VERSION.SDK_INT >= 23 && this.actionId == -1) {
            setAction(str, i, parcelable);
            if (isFirstTimeAsking(activity, str) || z) {
                this.permissionHandle.requestPermissions(i);
            } else {
                showSnackBar();
            }
        }
    }

    private void showSnackBar() {
        Snackbar makeAskAgainSnackBar = this.permissionHandle.makeAskAgainSnackBar(this.actionId);
        makeAskAgainSnackBar.addCallback(new C05853());
        makeAskAgainSnackBar.show();
    }

    private boolean isFirstTimeAsking(Context context, String str) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PERM_");
        stringBuilder.append(str);
        return defaultSharedPreferences.contains(stringBuilder.toString()) ^ 1;
    }

    private void setPermissionAsked(Context context, String str) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PERM_");
        stringBuilder.append(str);
        defaultSharedPreferences.edit().putBoolean(stringBuilder.toString(), true).apply();
    }

    private void setAction(String str, int i, Parcelable parcelable) {
        this.permission = str;
        this.actionId = i;
        this.params = parcelable;
    }

    private void permissionNotGranted() {
        this.permissionHandle.doActionNoPermission(this.permission, this.actionId, this.params);
        clearAction();
    }

    private void clearAction() {
        setAction(null, -1, null);
    }

    public void onActivityResult(Activity activity, int i, int i2, Intent intent) {
        if (i == 39528 && this.actionId != -1) {
            if (ContextCompat.checkSelfPermission(activity, this.permission) == 0) {
                this.permissionHandle.doActionSetting(this.permission, this.actionId, this.params);
                clearAction();
                return;
            }
            permissionNotGranted();
        }
    }

    public void onRequestPermissionsResult(Context context, int i, String[] strArr, int[] iArr) {
        if (strArr.length == 0 || iArr.length == 0) {
            clearAction();
            return;
        }
        if (i == this.actionId) {
            setPermissionAsked(context, strArr[0]);
            if (iArr.length <= 0 || iArr[0] != 0) {
                this.permissionHandle.permissionDeniedToast(this.actionId);
                permissionNotGranted();
            } else {
                this.permissionHandle.doActionGranted(this.permission, this.actionId, this.params);
                clearAction();
            }
        }
    }

    public void onRestoreInstanceState(Bundle bundle) {
        this.permission = bundle.getString("HANDLER_PERMISSION_KEY");
        this.actionId = bundle.getInt("HANDLER_ACTION_ID_KEY");
        this.params = bundle.getParcelable("HANDLER_PARAMS_KEY");
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("HANDLER_PERMISSION_KEY", this.permission);
        bundle.putInt("HANDLER_ACTION_ID_KEY", this.actionId);
        bundle.putParcelable("HANDLER_PARAMS_KEY", this.params);
    }

    public static Snackbar makeAskAgainSnackBar(final Activity activity, View view, int i) {
        return Snackbar.make(view, i, 0).setAction(C0597R.string.permission_handler_permission_dialog_setting, new View.OnClickListener() {
            public void onClick(View view) {
                PermissionHandler.intentOpenSettings(activity, 39528);
            }
        });
    }

    public static Snackbar makeAskAgainSnackBar(final Fragment fragment, View view, int i) {
        return Snackbar.make(view, i, 0).setAction(C0597R.string.permission_handler_permission_dialog_setting, new View.OnClickListener() {
            public void onClick(View view) {
                PermissionHandler.intentOpenSettings(fragment, 39528);
            }
        });
    }

    private static void intentOpenSettings(Activity activity, int i) {
        activity.startActivityForResult(buildOpenSettingsIntent(activity.getPackageName()), i);
    }

    private static void intentOpenSettings(Fragment fragment, int i) {
        FragmentActivity activity = fragment.getActivity();
        if (activity != null) {
            fragment.startActivityForResult(buildOpenSettingsIntent(activity.getPackageName()), i);
        }
    }

    private static Intent buildOpenSettingsIntent(String str) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", str, null));
        return intent;
    }
}
