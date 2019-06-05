// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.network.activity;

import java.io.File;
import menion.android.whereyougo.gui.activity.XmlSettingsActivity;
import android.text.Html;
import menion.android.whereyougo.utils.ManagerNotify;
import android.util.Log;
import android.content.DialogInterface;
import android.content.DialogInterface$OnCancelListener;
import android.app.ProgressDialog;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import menion.android.whereyougo.gui.activity.MainActivity;
import android.content.Context;
import menion.android.whereyougo.preferences.Preferences;
import android.os.AsyncTask$Status;
import android.view.View;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.utils.Images;
import android.widget.ImageView;
import menion.android.whereyougo.utils.UtilsFormat;
import menion.android.whereyougo.utils.FileSystem;
import android.widget.TextView;
import android.os.Bundle;
import menion.android.whereyougo.network.DownloadCartridgeTask;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;

public class DownloadCartridgeActivity extends CustomActivity
{
    private static final String TAG = "DownloadCartridgeActivity";
    private String cguid;
    private DownloadCartridgeTask downloadTask;
    
    @Override
    public void onCreate(Bundle file) {
        super.onCreate(file);
        while (true) {
            try {
                this.cguid = this.getIntent().getData().getQueryParameter("CGUID");
                if (this.cguid == null) {
                    this.finish();
                }
                else {
                    this.setContentView(2130903052);
                    ((TextView)this.findViewById(2131492940)).setText(2131165333);
                    final TextView textView = (TextView)this.findViewById(2131492943);
                    final TextView textView2 = (TextView)this.findViewById(2131492941);
                    file = (Bundle)FileSystem.findFile(this.cguid);
                    Label_0269: {
                        if (file == null) {
                            break Label_0269;
                        }
                        textView.setText((CharSequence)String.format("CGUID:\n%s\n%s", this.cguid, ((File)file).getName().replace(this.cguid + "_", "")));
                        textView2.setText((CharSequence)String.format("%s\n%s", this.getString(2131165338), UtilsFormat.formatDatetime(((File)file).lastModified())));
                        while (true) {
                            final ImageView imageView = (ImageView)this.findViewById(2131492936);
                            imageView.getLayoutParams().width = -2;
                            try {
                                imageView.setImageBitmap(Images.getImageB(2130837554));
                                CustomDialog.setBottom(this, this.getString(2131165332), (CustomDialog.OnClickListener)new CustomDialog.OnClickListener() {
                                    @Override
                                    public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                                        if (DownloadCartridgeActivity.this.downloadTask != null && DownloadCartridgeActivity.this.downloadTask.getStatus() != AsyncTask$Status.FINISHED) {
                                            DownloadCartridgeActivity.this.downloadTask.cancel(true);
                                            DownloadCartridgeActivity.this.downloadTask = null;
                                        }
                                        else {
                                            DownloadCartridgeActivity.this.downloadTask = new DownloadTask((Context)DownloadCartridgeActivity.this, Preferences.GC_USERNAME, Preferences.GC_PASSWORD);
                                            DownloadCartridgeActivity.this.downloadTask.execute((Object[])new String[] { DownloadCartridgeActivity.this.cguid });
                                        }
                                        return true;
                                    }
                                }, null, null, this.getString(2131165309), (CustomDialog.OnClickListener)new CustomDialog.OnClickListener() {
                                    @Override
                                    public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                                        final Intent intent = new Intent((Context)DownloadCartridgeActivity.this, (Class)MainActivity.class);
                                        intent.putExtra("cguid", DownloadCartridgeActivity.this.cguid);
                                        intent.addFlags(335544320);
                                        DownloadCartridgeActivity.this.startActivity(intent);
                                        DownloadCartridgeActivity.this.finish();
                                        return true;
                                    }
                                });
                                final Button button = (Button)this.findViewById(2131492932);
                                ((Button)this.findViewById(2131492934)).setEnabled(file != null);
                                break;
                                textView.setText((CharSequence)String.format("CGUID:\n%s", this.cguid));
                            }
                            catch (Exception ex) {}
                        }
                    }
                }
            }
            catch (Exception ex2) {
                continue;
            }
            break;
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.downloadTask != null && this.downloadTask.getStatus() != AsyncTask$Status.FINISHED) {
            this.downloadTask.cancel(true);
            this.downloadTask = null;
        }
    }
    
    class DownloadTask extends DownloadCartridgeTask
    {
        final ProgressDialog progressDialog;
        
        public DownloadTask(final Context context, final String s, final String s2) {
            super(context, s, s2);
            (this.progressDialog = new ProgressDialog(context)).setMessage((CharSequence)"");
            this.progressDialog.setProgressStyle(1);
            this.progressDialog.setMax(1);
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setCanceledOnTouchOutside(false);
            this.progressDialog.setCancelable(true);
            this.progressDialog.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
                public void onCancel(final DialogInterface dialogInterface) {
                    if (DownloadCartridgeActivity.this.downloadTask != null && DownloadCartridgeActivity.this.downloadTask.getStatus() != AsyncTask$Status.FINISHED) {
                        DownloadCartridgeActivity.this.downloadTask.cancel(false);
                        DownloadCartridgeActivity.this.downloadTask = null;
                        Log.i("down", "cancel");
                        ManagerNotify.toastShortMessage(context, DownloadCartridgeActivity.this.getString(2131165327));
                    }
                }
            });
        }
        
        protected void onPostExecute(final Boolean b) {
            super.onPostExecute((Object)b);
            if (b) {
                this.progressDialog.dismiss();
                MainActivity.refreshCartridges();
                DownloadCartridgeActivity.this.finish();
                DownloadCartridgeActivity.this.startActivity(DownloadCartridgeActivity.this.getIntent());
            }
            else {
                this.progressDialog.setIndeterminate(false);
            }
            DownloadCartridgeActivity.this.downloadTask = null;
        }
        
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog.show();
        }
        
        protected void onProgressUpdate(final Progress... array) {
            super.onProgressUpdate((Object[])array);
            final Progress progress = array[0];
            String str = "";
            if (progress.getState() == State.SUCCESS) {
                str = String.format(": %s", DownloadCartridgeActivity.this.getString(2131165230));
            }
            else if (progress.getState() == State.FAIL) {
                if (progress.getMessage() == null) {
                    str = String.format(": %s", DownloadCartridgeActivity.this.getString(2131165200));
                }
                else {
                    str = String.format(": %s(%s)", DownloadCartridgeActivity.this.getString(2131165200), progress.getMessage());
                }
            }
            switch (progress.getTask()) {
                case INIT:
                case PING: {
                    this.progressDialog.setIndeterminate(true);
                    this.progressDialog.setMessage((CharSequence)Html.fromHtml(DownloadCartridgeActivity.this.getString(2131165334) + str));
                    break;
                }
                case LOGIN: {
                    this.progressDialog.setIndeterminate(true);
                    this.progressDialog.setMessage((CharSequence)Html.fromHtml(DownloadCartridgeActivity.this.getString(2131165336) + str));
                    if (progress.getState() == State.FAIL) {
                        final Intent intent = new Intent((Context)DownloadCartridgeActivity.this, (Class)XmlSettingsActivity.class);
                        intent.putExtra(DownloadCartridgeActivity.this.getString(2131165592), true);
                        DownloadCartridgeActivity.this.startActivity(intent);
                        break;
                    }
                    break;
                }
                case LOGOUT: {
                    this.progressDialog.setIndeterminate(true);
                    this.progressDialog.setMessage((CharSequence)Html.fromHtml(DownloadCartridgeActivity.this.getString(2131165337) + str));
                    break;
                }
                case DOWNLOAD: {
                    this.progressDialog.setIndeterminate(true);
                    this.progressDialog.setMessage((CharSequence)Html.fromHtml(DownloadCartridgeActivity.this.getString(2131165335) + str));
                    break;
                }
                case DOWNLOAD_SINGLE: {
                    this.progressDialog.setIndeterminate(false);
                    this.progressDialog.setMax((int)progress.getTotal());
                    this.progressDialog.setProgress((int)progress.getCompleted());
                    this.progressDialog.setMessage((CharSequence)Html.fromHtml(DownloadCartridgeActivity.this.getString(2131165335) + str));
                    break;
                }
            }
        }
    }
}
