package org.mozilla.focus.download;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.util.List;
import org.mozilla.focus.components.RelocateService;
import org.mozilla.focus.download.DownloadInfoManager.AsyncInsertListener;
import org.mozilla.focus.download.DownloadInfoManager.AsyncQueryListener;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.tabs.web.Download;

public class EnqueueDownloadTask extends AsyncTask<Void, Void, ErrorCode> {
    private WeakReference<Activity> activityRef;
    private Download download;
    private String refererUrl;

    public enum ErrorCode {
        SUCCESS,
        GENERAL_ERROR,
        STORAGE_UNAVAILABLE,
        FILE_NOT_SUPPORTED,
        DOWNLOAD_MANAGER_DISABLED
    }

    public EnqueueDownloadTask(Activity activity, Download download, String str) {
        this.activityRef = new WeakReference(activity);
        this.download = download;
        this.refererUrl = str;
    }

    /* Access modifiers changed, original: protected|varargs */
    public ErrorCode doInBackground(Void... voidArr) {
        final Context context = (Context) this.activityRef.get();
        if (context == null) {
            return ErrorCode.GENERAL_ERROR;
        }
        String name;
        String cookie = CookieManager.getInstance().getCookie(this.download.getUrl());
        if (this.download.getName() != null) {
            name = this.download.getName();
        } else {
            name = URLUtil.guessFileName(this.download.getUrl(), this.download.getContentDisposition(), this.download.getMimeType());
        }
        String str = Environment.DIRECTORY_DOWNLOADS;
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return ErrorCode.STORAGE_UNAVAILABLE;
        }
        if (!URLUtil.isNetworkUrl(this.download.getUrl())) {
            return ErrorCode.FILE_NOT_SUPPORTED;
        }
        if (!isDownloadManagerEnabled(context)) {
            return ErrorCode.DOWNLOAD_MANAGER_DISABLED;
        }
        Request mimeType = new Request(Uri.parse(this.download.getUrl())).addRequestHeader("User-Agent", this.download.getUserAgent()).addRequestHeader("Cookie", cookie).addRequestHeader("Referer", this.refererUrl).setDestinationInExternalPublicDir(str, name).setNotificationVisibility(1).setMimeType(this.download.getMimeType());
        mimeType.allowScanningByMediaScanner();
        Long valueOf = Long.valueOf(((DownloadManager) context.getSystemService("download")).enqueue(mimeType));
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setDownloadId(valueOf);
        if (DownloadInfoManager.getInstance().recordExists(valueOf.longValue())) {
            DownloadInfoManager.getInstance().queryByDownloadId(valueOf, new AsyncQueryListener() {

                /* renamed from: org.mozilla.focus.download.EnqueueDownloadTask$2$1 */
                class C04551 implements AsyncInsertListener {
                    C04551() {
                    }

                    public void onInsertComplete(long j) {
                        DownloadInfoManager.notifyRowUpdated(context, j);
                        RelocateService.broadcastRelocateFinished(context, j);
                    }
                }

                public void onQueryComplete(List list) {
                    if (!list.isEmpty()) {
                        DownloadInfo downloadInfo = (DownloadInfo) list.get(0);
                        DownloadInfoManager.getInstance().delete(downloadInfo.getRowId(), null);
                        DownloadInfoManager.getInstance().insert(downloadInfo, new C04551());
                    }
                }
            });
        } else {
            DownloadInfoManager.getInstance().insert(downloadInfo, new AsyncInsertListener() {
                public void onInsertComplete(long j) {
                    DownloadInfoManager.notifyRowUpdated(context, j);
                }
            });
        }
        return ErrorCode.SUCCESS;
    }

    /* Access modifiers changed, original: protected */
    public void onPostExecute(ErrorCode errorCode) {
        Activity activity = (Activity) this.activityRef.get();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            switch (errorCode) {
                case STORAGE_UNAVAILABLE:
                    Toast.makeText(activity, C0769R.string.message_storage_unavailable_cancel_download, 1).show();
                    break;
                case FILE_NOT_SUPPORTED:
                    Toast.makeText(activity, C0769R.string.download_file_not_supported, 1).show();
                    break;
                case SUCCESS:
                    if (!this.download.isStartFromContextMenu()) {
                        Toast.makeText(activity, C0769R.string.download_started, 1).show();
                        break;
                    }
                    break;
            }
        }
    }

    private boolean isDownloadManagerEnabled(Context context) {
        int applicationEnabledSetting = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
        return (applicationEnabledSetting == 2 || applicationEnabledSetting == 3) ? false : true;
    }
}
