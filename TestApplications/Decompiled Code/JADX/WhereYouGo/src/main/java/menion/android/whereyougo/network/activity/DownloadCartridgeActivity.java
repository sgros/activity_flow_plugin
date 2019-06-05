package menion.android.whereyougo.network.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.activity.XmlSettingsActivity;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog.OnClickListener;
import menion.android.whereyougo.network.DownloadCartridgeTask;
import menion.android.whereyougo.network.DownloadCartridgeTask.Progress;
import menion.android.whereyougo.network.DownloadCartridgeTask.State;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.utils.UtilsFormat;

public class DownloadCartridgeActivity extends CustomActivity {
    private static final String TAG = "DownloadCartridgeActivity";
    private String cguid;
    private DownloadCartridgeTask downloadTask;

    /* renamed from: menion.android.whereyougo.network.activity.DownloadCartridgeActivity$1 */
    class C04251 implements OnClickListener {
        C04251() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            if (DownloadCartridgeActivity.this.downloadTask == null || DownloadCartridgeActivity.this.downloadTask.getStatus() == Status.FINISHED) {
                DownloadCartridgeActivity.this.downloadTask = new DownloadTask(DownloadCartridgeActivity.this, Preferences.GC_USERNAME, Preferences.GC_PASSWORD);
                DownloadCartridgeActivity.this.downloadTask.execute(new String[]{DownloadCartridgeActivity.this.cguid});
            } else {
                DownloadCartridgeActivity.this.downloadTask.cancel(true);
                DownloadCartridgeActivity.this.downloadTask = null;
            }
            return true;
        }
    }

    /* renamed from: menion.android.whereyougo.network.activity.DownloadCartridgeActivity$2 */
    class C04262 implements OnClickListener {
        C04262() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            Intent intent = new Intent(DownloadCartridgeActivity.this, MainActivity.class);
            intent.putExtra("cguid", DownloadCartridgeActivity.this.cguid);
            intent.addFlags(335544320);
            DownloadCartridgeActivity.this.startActivity(intent);
            DownloadCartridgeActivity.this.finish();
            return true;
        }
    }

    class DownloadTask extends DownloadCartridgeTask {
        final ProgressDialog progressDialog;

        public DownloadTask(final Context context, String username, String password) {
            super(context, username, password);
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setMessage("");
            this.progressDialog.setProgressStyle(1);
            this.progressDialog.setMax(1);
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setCanceledOnTouchOutside(false);
            this.progressDialog.setCancelable(true);
            this.progressDialog.setOnCancelListener(new OnCancelListener(DownloadCartridgeActivity.this) {
                public void onCancel(DialogInterface arg0) {
                    if (DownloadCartridgeActivity.this.downloadTask != null && DownloadCartridgeActivity.this.downloadTask.getStatus() != Status.FINISHED) {
                        DownloadCartridgeActivity.this.downloadTask.cancel(false);
                        DownloadCartridgeActivity.this.downloadTask = null;
                        Log.i("down", "cancel");
                        ManagerNotify.toastShortMessage(context, DownloadCartridgeActivity.this.getString(C0254R.string.cancelled));
                    }
                }
            });
        }

        /* Access modifiers changed, original: protected */
        public void onPreExecute() {
            super.onPreExecute();
            this.progressDialog.show();
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result.booleanValue()) {
                this.progressDialog.dismiss();
                MainActivity.refreshCartridges();
                DownloadCartridgeActivity.this.finish();
                DownloadCartridgeActivity.this.startActivity(DownloadCartridgeActivity.this.getIntent());
            } else {
                this.progressDialog.setIndeterminate(false);
            }
            DownloadCartridgeActivity.this.downloadTask = null;
        }

        /* Access modifiers changed, original: protected|varargs */
        public void onProgressUpdate(Progress... values) {
            super.onProgressUpdate(values);
            Progress progress = values[0];
            String suffix = "";
            if (progress.getState() == State.SUCCESS) {
                suffix = String.format(": %s", new Object[]{DownloadCartridgeActivity.this.getString(C0254R.string.f48ok)});
            } else if (progress.getState() == State.FAIL) {
                if (progress.getMessage() == null) {
                    suffix = String.format(": %s", new Object[]{DownloadCartridgeActivity.this.getString(C0254R.string.error)});
                } else {
                    suffix = String.format(": %s(%s)", new Object[]{DownloadCartridgeActivity.this.getString(C0254R.string.error), progress.getMessage()});
                }
            }
            switch (progress.getTask()) {
                case INIT:
                case PING:
                    this.progressDialog.setIndeterminate(true);
                    this.progressDialog.setMessage(Html.fromHtml(DownloadCartridgeActivity.this.getString(C0254R.string.download_state_connect) + suffix));
                    return;
                case LOGIN:
                    this.progressDialog.setIndeterminate(true);
                    this.progressDialog.setMessage(Html.fromHtml(DownloadCartridgeActivity.this.getString(C0254R.string.download_state_login) + suffix));
                    if (progress.getState() == State.FAIL) {
                        Intent loginPreferenceIntent = new Intent(DownloadCartridgeActivity.this, XmlSettingsActivity.class);
                        loginPreferenceIntent.putExtra(DownloadCartridgeActivity.this.getString(C0254R.string.pref_KEY_X_LOGIN_PREFERENCES), true);
                        DownloadCartridgeActivity.this.startActivity(loginPreferenceIntent);
                        return;
                    }
                    return;
                case LOGOUT:
                    this.progressDialog.setIndeterminate(true);
                    this.progressDialog.setMessage(Html.fromHtml(DownloadCartridgeActivity.this.getString(C0254R.string.download_state_logout) + suffix));
                    return;
                case DOWNLOAD:
                    this.progressDialog.setIndeterminate(true);
                    this.progressDialog.setMessage(Html.fromHtml(DownloadCartridgeActivity.this.getString(C0254R.string.download_state_download) + suffix));
                    return;
                case DOWNLOAD_SINGLE:
                    this.progressDialog.setIndeterminate(false);
                    this.progressDialog.setMax((int) progress.getTotal());
                    this.progressDialog.setProgress((int) progress.getCompleted());
                    this.progressDialog.setMessage(Html.fromHtml(DownloadCartridgeActivity.this.getString(C0254R.string.download_state_download) + suffix));
                    return;
                default:
                    return;
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.cguid = getIntent().getData().getQueryParameter("CGUID");
        } catch (Exception e) {
        }
        if (this.cguid == null) {
            finish();
            return;
        }
        boolean z;
        setContentView(C0254R.layout.layout_details);
        ((TextView) findViewById(C0254R.C0253id.layoutDetailsTextViewName)).setText(C0254R.string.download_cartridge);
        TextView tvDescription = (TextView) findViewById(C0254R.C0253id.layoutDetailsTextViewDescription);
        TextView tvState = (TextView) findViewById(C0254R.C0253id.layoutDetailsTextViewState);
        File cartridgeFile = FileSystem.findFile(this.cguid);
        if (cartridgeFile != null) {
            tvDescription.setText(String.format("CGUID:\n%s\n%s", new Object[]{this.cguid, cartridgeFile.getName().replace(this.cguid + "_", "")}));
            TextView textView = tvState;
            textView.setText(String.format("%s\n%s", new Object[]{getString(C0254R.string.download_successful), UtilsFormat.formatDatetime(cartridgeFile.lastModified())}));
        } else {
            tvDescription.setText(String.format("CGUID:\n%s", new Object[]{this.cguid}));
        }
        ImageView ivImage = (ImageView) findViewById(C0254R.C0253id.mediaImageView);
        ivImage.getLayoutParams().width = -2;
        try {
            ivImage.setImageBitmap(Images.getImageB(C0254R.C0252drawable.icon_gc_wherigo));
        } catch (Exception e2) {
        }
        CustomDialog.setBottom(this, getString(C0254R.string.download), new C04251(), null, null, getString(C0254R.string.start), new C04262());
        Button buttonDownload = (Button) findViewById(C0254R.C0253id.button_positive);
        Button buttonStart = (Button) findViewById(C0254R.C0253id.button_negative);
        if (cartridgeFile != null) {
            z = true;
        } else {
            z = false;
        }
        buttonStart.setEnabled(z);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.downloadTask != null && this.downloadTask.getStatus() != Status.FINISHED) {
            this.downloadTask.cancel(true);
            this.downloadTask = null;
        }
    }
}
