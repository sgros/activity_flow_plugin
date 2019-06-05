// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.permissionhandler;

import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.os.Build$VERSION;
import android.support.v4.content.ContextCompat;
import android.content.DialogInterface$OnClickListener;
import android.support.design.widget.BaseTransientBottomBar;
import android.view.View$OnClickListener;
import org.mozilla.rocket.permissionhandler.R;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.net.Uri;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Parcelable;

public class PermissionHandler
{
    private int actionId;
    private Parcelable params;
    private String permission;
    private PermissionHandle permissionHandle;
    
    public PermissionHandler(final PermissionHandle permissionHandle) {
        this.actionId = -1;
        this.permissionHandle = permissionHandle;
    }
    
    private static Intent buildOpenSettingsIntent(final String s) {
        final Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", s, (String)null));
        return intent;
    }
    
    private void clearAction() {
        this.setAction(null, -1, null);
    }
    
    private static void intentOpenSettings(final Activity activity, final int n) {
        activity.startActivityForResult(buildOpenSettingsIntent(activity.getPackageName()), n);
    }
    
    private static void intentOpenSettings(final Fragment fragment, final int n) {
        final FragmentActivity activity = fragment.getActivity();
        if (activity != null) {
            fragment.startActivityForResult(buildOpenSettingsIntent(activity.getPackageName()), n);
        }
    }
    
    private boolean isFirstTimeAsking(final Context context, final String str) {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final StringBuilder sb = new StringBuilder();
        sb.append("PERM_");
        sb.append(str);
        return defaultSharedPreferences.contains(sb.toString()) ^ true;
    }
    
    public static Snackbar makeAskAgainSnackBar(final Activity activity, final View view, final int n) {
        return Snackbar.make(view, n, 0).setAction(R.string.permission_handler_permission_dialog_setting, (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                intentOpenSettings(activity, 39528);
            }
        });
    }
    
    public static Snackbar makeAskAgainSnackBar(final Fragment fragment, final View view, final int n) {
        return Snackbar.make(view, n, 0).setAction(R.string.permission_handler_permission_dialog_setting, (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                intentOpenSettings(fragment, 39528);
            }
        });
    }
    
    private void permissionNotGranted() {
        this.permissionHandle.doActionNoPermission(this.permission, this.actionId, this.params);
        this.clearAction();
    }
    
    private void setAction(final String permission, final int actionId, final Parcelable params) {
        this.permission = permission;
        this.actionId = actionId;
        this.params = params;
    }
    
    private void setPermissionAsked(final Context context, String string) {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final StringBuilder sb = new StringBuilder();
        sb.append("PERM_");
        sb.append(string);
        string = sb.toString();
        defaultSharedPreferences.edit().putBoolean(string, true).apply();
    }
    
    private void showSnackBar() {
        final Snackbar askAgainSnackBar = this.permissionHandle.makeAskAgainSnackBar(this.actionId);
        askAgainSnackBar.addCallback((BaseTransientBottomBar.BaseCallback<Snackbar>)new Snackbar.Callback() {
            @Override
            public void onDismissed(final Snackbar snackbar, final int n) {
                if (n != 1) {
                    PermissionHandler.this.permissionNotGranted();
                }
            }
        });
        askAgainSnackBar.show();
    }
    
    private void tryAction(final Activity activity, final String s, final int n, final Parcelable parcelable, final boolean b, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
        if (ContextCompat.checkSelfPermission((Context)activity, s) == 0) {
            this.permissionHandle.doActionDirect(s, n, parcelable);
        }
        else {
            if (Build$VERSION.SDK_INT < 23) {
                return;
            }
            if (this.actionId != -1) {
                return;
            }
            this.setAction(s, n, parcelable);
            if (!this.isFirstTimeAsking((Context)activity, s) && !b) {
                this.showSnackBar();
            }
            else {
                this.permissionHandle.requestPermissions(n);
            }
        }
    }
    
    public void onActivityResult(final Activity activity, final int n, final int n2, final Intent intent) {
        if (n == 39528 && this.actionId != -1) {
            if (ContextCompat.checkSelfPermission((Context)activity, this.permission) == 0) {
                this.permissionHandle.doActionSetting(this.permission, this.actionId, this.params);
                this.clearAction();
            }
            else {
                this.permissionNotGranted();
            }
        }
    }
    
    public void onRequestPermissionsResult(final Context context, final int n, final String[] array, final int[] array2) {
        if (array.length != 0 && array2.length != 0) {
            if (n == this.actionId) {
                this.setPermissionAsked(context, array[0]);
                if (array2.length > 0 && array2[0] == 0) {
                    this.permissionHandle.doActionGranted(this.permission, this.actionId, this.params);
                    this.clearAction();
                }
                else {
                    this.permissionHandle.permissionDeniedToast(this.actionId);
                    this.permissionNotGranted();
                }
            }
            return;
        }
        this.clearAction();
    }
    
    public void onRestoreInstanceState(final Bundle bundle) {
        this.permission = bundle.getString("HANDLER_PERMISSION_KEY");
        this.actionId = bundle.getInt("HANDLER_ACTION_ID_KEY");
        this.params = bundle.getParcelable("HANDLER_PARAMS_KEY");
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        bundle.putString("HANDLER_PERMISSION_KEY", this.permission);
        bundle.putInt("HANDLER_ACTION_ID_KEY", this.actionId);
        bundle.putParcelable("HANDLER_PARAMS_KEY", this.params);
    }
    
    public void tryAction(final Activity activity, final String s, final int n, final Parcelable parcelable) {
        this.tryAction(activity, s, n, parcelable, ActivityCompat.shouldShowRequestPermissionRationale(activity, s), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                intentOpenSettings(activity, 39528);
            }
        });
    }
    
    public void tryAction(final Fragment fragment, final String s, final int n, final Parcelable parcelable) {
        this.tryAction(fragment.getActivity(), s, n, parcelable, fragment.shouldShowRequestPermissionRationale(s), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                intentOpenSettings(fragment, 39528);
            }
        });
    }
}
