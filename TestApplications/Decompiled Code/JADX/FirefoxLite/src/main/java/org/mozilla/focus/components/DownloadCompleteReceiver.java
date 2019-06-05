package org.mozilla.focus.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import java.io.File;
import java.util.List;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.focus.download.DownloadInfoManager;
import org.mozilla.focus.download.DownloadInfoManager.AsyncQueryListener;
import org.mozilla.focus.download.DownloadInfoManager.AsyncUpdateListener;
import org.mozilla.threadutils.ThreadUtils;

public class DownloadCompleteReceiver extends BroadcastReceiver {
    public void onReceive(final Context context, Intent intent) {
        long longExtra = intent.getLongExtra("extra_download_id", -1);
        if (longExtra != -1) {
            DownloadInfoManager.getInstance().queryByDownloadId(Long.valueOf(longExtra), new AsyncQueryListener() {
                public void onQueryComplete(List list) {
                    if (list.size() > 0) {
                        final DownloadInfo downloadInfo = (DownloadInfo) list.get(0);
                        if (downloadInfo.getStatus() == 8 && !TextUtils.isEmpty(downloadInfo.getFileUri())) {
                            DownloadInfoManager.getInstance().updateByRowId(downloadInfo, new AsyncUpdateListener() {
                                public void onUpdateComplete(int i) {
                                    final Uri parse = Uri.parse(downloadInfo.getFileUri());
                                    if ("file".equals(parse.getScheme())) {
                                        ThreadUtils.postToBackgroundThread(new Runnable() {
                                            public void run() {
                                                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), new File(parse.getPath()).getName());
                                                if (file.exists() && file.canWrite()) {
                                                    RelocateService.startActionMove(context, downloadInfo.getRowId().longValue(), downloadInfo.getDownloadId().longValue(), file, downloadInfo.getMimeType());
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                        if (!downloadInfo.existInDownloadManager()) {
                            DownloadInfoManager.getInstance().delete(downloadInfo.getRowId(), null);
                        }
                    }
                }
            });
        }
    }
}
