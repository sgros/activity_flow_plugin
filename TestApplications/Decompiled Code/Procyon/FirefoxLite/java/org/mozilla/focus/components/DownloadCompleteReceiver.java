// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.components;

import org.mozilla.threadutils.ThreadUtils;
import java.io.File;
import android.os.Environment;
import android.net.Uri;
import android.text.TextUtils;
import org.mozilla.focus.download.DownloadInfo;
import java.util.List;
import org.mozilla.focus.download.DownloadInfoManager;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class DownloadCompleteReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        final long longExtra = intent.getLongExtra("extra_download_id", -1L);
        if (longExtra == -1L) {
            return;
        }
        DownloadInfoManager.getInstance().queryByDownloadId(longExtra, (DownloadInfoManager.AsyncQueryListener)new DownloadInfoManager.AsyncQueryListener() {
            @Override
            public void onQueryComplete(final List list) {
                if (list.size() > 0) {
                    final DownloadInfo downloadInfo = list.get(0);
                    if (downloadInfo.getStatus() == 8 && !TextUtils.isEmpty((CharSequence)downloadInfo.getFileUri())) {
                        DownloadInfoManager.getInstance().updateByRowId(downloadInfo, (DownloadInfoManager.AsyncUpdateListener)new AsyncUpdateListener() {
                            @Override
                            public void onUpdateComplete(final int n) {
                                final Uri parse = Uri.parse(downloadInfo.getFileUri());
                                if ("file".equals(parse.getScheme())) {
                                    ThreadUtils.postToBackgroundThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), new File(parse.getPath()).getName());
                                            if (file.exists() && file.canWrite()) {
                                                RelocateService.startActionMove(context, downloadInfo.getRowId(), downloadInfo.getDownloadId(), file, downloadInfo.getMimeType());
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
