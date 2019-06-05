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
import org.mozilla.threadutils.ThreadUtils;

public class DownloadCompleteReceiver extends BroadcastReceiver {
   public void onReceive(final Context var1, Intent var2) {
      long var3 = var2.getLongExtra("extra_download_id", -1L);
      if (var3 != -1L) {
         DownloadInfoManager.getInstance().queryByDownloadId(var3, new DownloadInfoManager.AsyncQueryListener() {
            public void onQueryComplete(List var1x) {
               if (var1x.size() > 0) {
                  final DownloadInfo var2 = (DownloadInfo)var1x.get(0);
                  if (var2.getStatus() == 8 && !TextUtils.isEmpty(var2.getFileUri())) {
                     DownloadInfoManager.getInstance().updateByRowId(var2, new DownloadInfoManager.AsyncUpdateListener() {
                        public void onUpdateComplete(int var1x) {
                           final Uri var2x = Uri.parse(var2.getFileUri());
                           if ("file".equals(var2x.getScheme())) {
                              ThreadUtils.postToBackgroundThread(new Runnable() {
                                 public void run() {
                                    String var1x = (new File(var2x.getPath())).getName();
                                    File var2xx = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), var1x);
                                    if (var2xx.exists() && var2xx.canWrite()) {
                                       RelocateService.startActionMove(var1, var2.getRowId(), var2.getDownloadId(), var2xx, var2.getMimeType());
                                    }

                                 }
                              });
                           }

                        }
                     });
                  }

                  if (!var2.existInDownloadManager()) {
                     DownloadInfoManager.getInstance().delete(var2.getRowId(), (DownloadInfoManager.AsyncDeleteListener)null);
                  }
               }

            }
         });
      }
   }
}
