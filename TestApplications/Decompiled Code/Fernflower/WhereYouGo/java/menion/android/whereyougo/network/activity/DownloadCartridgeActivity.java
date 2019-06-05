package menion.android.whereyougo.network.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.activity.XmlSettingsActivity;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.network.DownloadCartridgeTask;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.utils.UtilsFormat;

public class DownloadCartridgeActivity extends CustomActivity {
   private static final String TAG = "DownloadCartridgeActivity";
   private String cguid;
   private DownloadCartridgeTask downloadTask;

   public void onCreate(Bundle var1) {
      super.onCreate(var1);

      try {
         this.cguid = this.getIntent().getData().getQueryParameter("CGUID");
      } catch (Exception var6) {
      }

      if (this.cguid == null) {
         this.finish();
      } else {
         this.setContentView(2130903052);
         ((TextView)this.findViewById(2131492940)).setText(2131165333);
         TextView var2 = (TextView)this.findViewById(2131492943);
         TextView var3 = (TextView)this.findViewById(2131492941);
         File var7 = FileSystem.findFile(this.cguid);
         if (var7 != null) {
            var2.setText(String.format("CGUID:\n%s\n%s", this.cguid, var7.getName().replace(this.cguid + "_", "")));
            var3.setText(String.format("%s\n%s", this.getString(2131165338), UtilsFormat.formatDatetime(var7.lastModified())));
         } else {
            var2.setText(String.format("CGUID:\n%s", this.cguid));
         }

         ImageView var8 = (ImageView)this.findViewById(2131492936);
         var8.getLayoutParams().width = -2;

         try {
            var8.setImageBitmap(Images.getImageB(2130837554));
         } catch (Exception var5) {
         }

         CustomDialog.setBottom(this, this.getString(2131165332), new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog var1, View var2, int var3) {
               if (DownloadCartridgeActivity.this.downloadTask != null && DownloadCartridgeActivity.this.downloadTask.getStatus() != Status.FINISHED) {
                  DownloadCartridgeActivity.this.downloadTask.cancel(true);
                  DownloadCartridgeActivity.this.downloadTask = null;
               } else {
                  String var4 = Preferences.GC_USERNAME;
                  String var5 = Preferences.GC_PASSWORD;
                  DownloadCartridgeActivity.this.downloadTask = DownloadCartridgeActivity.this.new DownloadTask(DownloadCartridgeActivity.this, var4, var5);
                  DownloadCartridgeActivity.this.downloadTask.execute(new String[]{DownloadCartridgeActivity.this.cguid});
               }

               return true;
            }
         }, (String)null, (CustomDialog.OnClickListener)null, this.getString(2131165309), new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog var1, View var2, int var3) {
               Intent var4 = new Intent(DownloadCartridgeActivity.this, MainActivity.class);
               var4.putExtra("cguid", DownloadCartridgeActivity.this.cguid);
               var4.addFlags(335544320);
               DownloadCartridgeActivity.this.startActivity(var4);
               DownloadCartridgeActivity.this.finish();
               return true;
            }
         });
         Button var9 = (Button)this.findViewById(2131492932);
         var9 = (Button)this.findViewById(2131492934);
         boolean var4;
         if (var7 != null) {
            var4 = true;
         } else {
            var4 = false;
         }

         var9.setEnabled(var4);
      }

   }

   public void onDestroy() {
      super.onDestroy();
      if (this.downloadTask != null && this.downloadTask.getStatus() != Status.FINISHED) {
         this.downloadTask.cancel(true);
         this.downloadTask = null;
      }

   }

   class DownloadTask extends DownloadCartridgeTask {
      final ProgressDialog progressDialog;

      public DownloadTask(final Context var2, String var3, String var4) {
         super(var2, var3, var4);
         this.progressDialog = new ProgressDialog(var2);
         this.progressDialog.setMessage("");
         this.progressDialog.setProgressStyle(1);
         this.progressDialog.setMax(1);
         this.progressDialog.setIndeterminate(true);
         this.progressDialog.setCanceledOnTouchOutside(false);
         this.progressDialog.setCancelable(true);
         this.progressDialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface var1) {
               if (DownloadCartridgeActivity.this.downloadTask != null && DownloadCartridgeActivity.this.downloadTask.getStatus() != Status.FINISHED) {
                  DownloadCartridgeActivity.this.downloadTask.cancel(false);
                  DownloadCartridgeActivity.this.downloadTask = null;
                  Log.i("down", "cancel");
                  ManagerNotify.toastShortMessage(var2, DownloadCartridgeActivity.this.getString(2131165327));
               }

            }
         });
      }

      protected void onPostExecute(Boolean var1) {
         super.onPostExecute(var1);
         if (var1) {
            this.progressDialog.dismiss();
            MainActivity.refreshCartridges();
            DownloadCartridgeActivity.this.finish();
            DownloadCartridgeActivity.this.startActivity(DownloadCartridgeActivity.this.getIntent());
         } else {
            this.progressDialog.setIndeterminate(false);
         }

         DownloadCartridgeActivity.this.downloadTask = null;
      }

      protected void onPreExecute() {
         super.onPreExecute();
         this.progressDialog.show();
      }

      protected void onProgressUpdate(DownloadCartridgeTask.Progress... var1) {
         super.onProgressUpdate(var1);
         DownloadCartridgeTask.Progress var2 = var1[0];
         String var3 = "";
         if (var2.getState() == DownloadCartridgeTask.State.SUCCESS) {
            var3 = String.format(": %s", DownloadCartridgeActivity.this.getString(2131165230));
         } else if (var2.getState() == DownloadCartridgeTask.State.FAIL) {
            if (var2.getMessage() == null) {
               var3 = String.format(": %s", DownloadCartridgeActivity.this.getString(2131165200));
            } else {
               var3 = String.format(": %s(%s)", DownloadCartridgeActivity.this.getString(2131165200), var2.getMessage());
            }
         }

         switch(var2.getTask()) {
         case INIT:
         case PING:
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setMessage(Html.fromHtml(DownloadCartridgeActivity.this.getString(2131165334) + var3));
            break;
         case LOGIN:
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setMessage(Html.fromHtml(DownloadCartridgeActivity.this.getString(2131165336) + var3));
            if (var2.getState() == DownloadCartridgeTask.State.FAIL) {
               Intent var4 = new Intent(DownloadCartridgeActivity.this, XmlSettingsActivity.class);
               var4.putExtra(DownloadCartridgeActivity.this.getString(2131165592), true);
               DownloadCartridgeActivity.this.startActivity(var4);
            }
            break;
         case LOGOUT:
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setMessage(Html.fromHtml(DownloadCartridgeActivity.this.getString(2131165337) + var3));
            break;
         case DOWNLOAD:
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setMessage(Html.fromHtml(DownloadCartridgeActivity.this.getString(2131165335) + var3));
            break;
         case DOWNLOAD_SINGLE:
            this.progressDialog.setIndeterminate(false);
            this.progressDialog.setMax((int)var2.getTotal());
            this.progressDialog.setProgress((int)var2.getCompleted());
            this.progressDialog.setMessage(Html.fromHtml(DownloadCartridgeActivity.this.getString(2131165335) + var3));
         }

      }
   }
}
