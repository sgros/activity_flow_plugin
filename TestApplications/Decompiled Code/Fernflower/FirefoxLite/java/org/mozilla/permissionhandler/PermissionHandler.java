package org.mozilla.permissionhandler;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.rocket.permissionhandler.R;

public class PermissionHandler {
   private int actionId = -1;
   private Parcelable params;
   private String permission;
   private PermissionHandle permissionHandle;

   public PermissionHandler(PermissionHandle var1) {
      this.permissionHandle = var1;
   }

   private static Intent buildOpenSettingsIntent(String var0) {
      Intent var1 = new Intent();
      var1.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
      var1.setData(Uri.fromParts("package", var0, (String)null));
      return var1;
   }

   private void clearAction() {
      this.setAction((String)null, -1, (Parcelable)null);
   }

   private static void intentOpenSettings(Activity var0, int var1) {
      var0.startActivityForResult(buildOpenSettingsIntent(var0.getPackageName()), var1);
   }

   private static void intentOpenSettings(Fragment var0, int var1) {
      FragmentActivity var2 = var0.getActivity();
      if (var2 != null) {
         var0.startActivityForResult(buildOpenSettingsIntent(var2.getPackageName()), var1);
      }

   }

   private boolean isFirstTimeAsking(Context var1, String var2) {
      SharedPreferences var4 = PreferenceManager.getDefaultSharedPreferences(var1);
      StringBuilder var3 = new StringBuilder();
      var3.append("PERM_");
      var3.append(var2);
      return var4.contains(var3.toString()) ^ true;
   }

   public static Snackbar makeAskAgainSnackBar(final Activity var0, View var1, int var2) {
      return Snackbar.make(var1, var2, 0).setAction(R.string.permission_handler_permission_dialog_setting, new OnClickListener() {
         public void onClick(View var1) {
            PermissionHandler.intentOpenSettings((Activity)var0, 39528);
         }
      });
   }

   public static Snackbar makeAskAgainSnackBar(final Fragment var0, View var1, int var2) {
      return Snackbar.make(var1, var2, 0).setAction(R.string.permission_handler_permission_dialog_setting, new OnClickListener() {
         public void onClick(View var1) {
            PermissionHandler.intentOpenSettings((Fragment)var0, 39528);
         }
      });
   }

   private void permissionNotGranted() {
      this.permissionHandle.doActionNoPermission(this.permission, this.actionId, this.params);
      this.clearAction();
   }

   private void setAction(String var1, int var2, Parcelable var3) {
      this.permission = var1;
      this.actionId = var2;
      this.params = var3;
   }

   private void setPermissionAsked(Context var1, String var2) {
      SharedPreferences var4 = PreferenceManager.getDefaultSharedPreferences(var1);
      StringBuilder var3 = new StringBuilder();
      var3.append("PERM_");
      var3.append(var2);
      var2 = var3.toString();
      var4.edit().putBoolean(var2, true).apply();
   }

   private void showSnackBar() {
      Snackbar var1 = this.permissionHandle.makeAskAgainSnackBar(this.actionId);
      var1.addCallback(new Snackbar.Callback() {
         public void onDismissed(Snackbar var1, int var2) {
            if (var2 != 1) {
               PermissionHandler.this.permissionNotGranted();
            }

         }
      });
      var1.show();
   }

   private void tryAction(Activity var1, String var2, int var3, Parcelable var4, boolean var5, android.content.DialogInterface.OnClickListener var6) {
      if (ContextCompat.checkSelfPermission(var1, var2) == 0) {
         this.permissionHandle.doActionDirect(var2, var3, var4);
      } else {
         if (VERSION.SDK_INT < 23) {
            return;
         }

         if (this.actionId != -1) {
            return;
         }

         this.setAction(var2, var3, var4);
         if (!this.isFirstTimeAsking(var1, var2) && !var5) {
            this.showSnackBar();
         } else {
            this.permissionHandle.requestPermissions(var3);
         }
      }

   }

   public void onActivityResult(Activity var1, int var2, int var3, Intent var4) {
      if (var2 == 39528 && this.actionId != -1) {
         if (ContextCompat.checkSelfPermission(var1, this.permission) == 0) {
            this.permissionHandle.doActionSetting(this.permission, this.actionId, this.params);
            this.clearAction();
         } else {
            this.permissionNotGranted();
         }
      }

   }

   public void onRequestPermissionsResult(Context var1, int var2, String[] var3, int[] var4) {
      if (var3.length != 0 && var4.length != 0) {
         if (var2 == this.actionId) {
            this.setPermissionAsked(var1, var3[0]);
            if (var4.length > 0 && var4[0] == 0) {
               this.permissionHandle.doActionGranted(this.permission, this.actionId, this.params);
               this.clearAction();
            } else {
               this.permissionHandle.permissionDeniedToast(this.actionId);
               this.permissionNotGranted();
            }
         }

      } else {
         this.clearAction();
      }
   }

   public void onRestoreInstanceState(Bundle var1) {
      this.permission = var1.getString("HANDLER_PERMISSION_KEY");
      this.actionId = var1.getInt("HANDLER_ACTION_ID_KEY");
      this.params = var1.getParcelable("HANDLER_PARAMS_KEY");
   }

   public void onSaveInstanceState(Bundle var1) {
      var1.putString("HANDLER_PERMISSION_KEY", this.permission);
      var1.putInt("HANDLER_ACTION_ID_KEY", this.actionId);
      var1.putParcelable("HANDLER_PARAMS_KEY", this.params);
   }

   public void tryAction(final Activity var1, String var2, int var3, Parcelable var4) {
      this.tryAction(var1, var2, var3, var4, ActivityCompat.shouldShowRequestPermissionRationale(var1, var2), new android.content.DialogInterface.OnClickListener() {
         public void onClick(DialogInterface var1x, int var2) {
            PermissionHandler.intentOpenSettings((Activity)var1, 39528);
         }
      });
   }

   public void tryAction(final Fragment var1, String var2, int var3, Parcelable var4) {
      this.tryAction(var1.getActivity(), var2, var3, var4, var1.shouldShowRequestPermissionRationale(var2), new android.content.DialogInterface.OnClickListener() {
         public void onClick(DialogInterface var1x, int var2) {
            PermissionHandler.intentOpenSettings((Fragment)var1, 39528);
         }
      });
   }
}
