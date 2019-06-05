// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.download;

import android.widget.Toast;
import org.mozilla.focus.components.RelocateService;
import java.util.List;
import android.app.DownloadManager;
import android.app.DownloadManager$Request;
import android.net.Uri;
import android.os.Environment;
import android.webkit.URLUtil;
import android.webkit.CookieManager;
import android.content.Context;
import org.mozilla.rocket.tabs.web.Download;
import android.app.Activity;
import java.lang.ref.WeakReference;
import android.os.AsyncTask;

public class EnqueueDownloadTask extends AsyncTask<Void, Void, ErrorCode>
{
    private WeakReference<Activity> activityRef;
    private Download download;
    private String refererUrl;
    
    public EnqueueDownloadTask(final Activity referent, final Download download, final String refererUrl) {
        this.activityRef = new WeakReference<Activity>(referent);
        this.download = download;
        this.refererUrl = refererUrl;
    }
    
    private boolean isDownloadManagerEnabled(final Context context) {
        final int applicationEnabledSetting = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
        return applicationEnabledSetting != 2 && applicationEnabledSetting != 3;
    }
    
    protected ErrorCode doInBackground(final Void... array) {
        final Context context = this.activityRef.get();
        if (context == null) {
            return ErrorCode.GENERAL_ERROR;
        }
        final String cookie = CookieManager.getInstance().getCookie(this.download.getUrl());
        String s;
        if (this.download.getName() != null) {
            s = this.download.getName();
        }
        else {
            s = URLUtil.guessFileName(this.download.getUrl(), this.download.getContentDisposition(), this.download.getMimeType());
        }
        final String directory_DOWNLOADS = Environment.DIRECTORY_DOWNLOADS;
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return ErrorCode.STORAGE_UNAVAILABLE;
        }
        if (!URLUtil.isNetworkUrl(this.download.getUrl())) {
            return ErrorCode.FILE_NOT_SUPPORTED;
        }
        if (!this.isDownloadManagerEnabled(context)) {
            return ErrorCode.DOWNLOAD_MANAGER_DISABLED;
        }
        final DownloadManager$Request setMimeType = new DownloadManager$Request(Uri.parse(this.download.getUrl())).addRequestHeader("User-Agent", this.download.getUserAgent()).addRequestHeader("Cookie", cookie).addRequestHeader("Referer", this.refererUrl).setDestinationInExternalPublicDir(directory_DOWNLOADS, s).setNotificationVisibility(1).setMimeType(this.download.getMimeType());
        setMimeType.allowScanningByMediaScanner();
        final Long value = ((DownloadManager)context.getSystemService("download")).enqueue(setMimeType);
        final DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setDownloadId(value);
        if (!DownloadInfoManager.getInstance().recordExists(value)) {
            DownloadInfoManager.getInstance().insert(downloadInfo, (DownloadInfoManager.AsyncInsertListener)new DownloadInfoManager.AsyncInsertListener() {
                @Override
                public void onInsertComplete(final long n) {
                    DownloadInfoManager.notifyRowUpdated(context, n);
                }
            });
        }
        else {
            DownloadInfoManager.getInstance().queryByDownloadId(value, (DownloadInfoManager.AsyncQueryListener)new DownloadInfoManager.AsyncQueryListener() {
                @Override
                public void onQueryComplete(final List list) {
                    if (!list.isEmpty()) {
                        final DownloadInfo downloadInfo = list.get(0);
                        DownloadInfoManager.getInstance().delete(downloadInfo.getRowId(), null);
                        DownloadInfoManager.getInstance().insert(downloadInfo, (DownloadInfoManager.AsyncInsertListener)new AsyncInsertListener() {
                            @Override
                            public void onInsertComplete(final long n) {
                                DownloadInfoManager.notifyRowUpdated(context, n);
                                RelocateService.broadcastRelocateFinished(context, n);
                            }
                        });
                    }
                }
            });
        }
        return ErrorCode.SUCCESS;
    }
    
    protected void onPostExecute(final ErrorCode errorCode) {
        final Activity activity = this.activityRef.get();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            switch (EnqueueDownloadTask$3.$SwitchMap$org$mozilla$focus$download$EnqueueDownloadTask$ErrorCode[errorCode.ordinal()]) {
                case 3: {
                    if (!this.download.isStartFromContextMenu()) {
                        Toast.makeText((Context)activity, 2131755107, 1).show();
                        break;
                    }
                    break;
                }
                case 2: {
                    Toast.makeText((Context)activity, 2131755106, 1).show();
                    break;
                }
                case 1: {
                    Toast.makeText((Context)activity, 2131755266, 1).show();
                    break;
                }
            }
        }
    }
    
    public enum ErrorCode
    {
        DOWNLOAD_MANAGER_DISABLED, 
        FILE_NOT_SUPPORTED, 
        GENERAL_ERROR, 
        STORAGE_UNAVAILABLE, 
        SUCCESS;
    }
}
