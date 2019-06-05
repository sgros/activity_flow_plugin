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
import org.mozilla.rocket.tabs.web.Download;

public class EnqueueDownloadTask extends AsyncTask {
   private WeakReference activityRef;
   private Download download;
   private String refererUrl;

   public EnqueueDownloadTask(Activity var1, Download var2, String var3) {
      this.activityRef = new WeakReference(var1);
      this.download = var2;
      this.refererUrl = var3;
   }

   private boolean isDownloadManagerEnabled(Context var1) {
      int var2 = var1.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
      boolean var3;
      if (var2 != 2 && var2 != 3) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   protected EnqueueDownloadTask.ErrorCode doInBackground(Void... var1) {
      final Context var2 = (Context)this.activityRef.get();
      if (var2 == null) {
         return EnqueueDownloadTask.ErrorCode.GENERAL_ERROR;
      } else {
         String var3 = CookieManager.getInstance().getCookie(this.download.getUrl());
         String var5;
         if (this.download.getName() != null) {
            var5 = this.download.getName();
         } else {
            var5 = URLUtil.guessFileName(this.download.getUrl(), this.download.getContentDisposition(), this.download.getMimeType());
         }

         String var4 = Environment.DIRECTORY_DOWNLOADS;
         if (!"mounted".equals(Environment.getExternalStorageState())) {
            return EnqueueDownloadTask.ErrorCode.STORAGE_UNAVAILABLE;
         } else if (!URLUtil.isNetworkUrl(this.download.getUrl())) {
            return EnqueueDownloadTask.ErrorCode.FILE_NOT_SUPPORTED;
         } else if (!this.isDownloadManagerEnabled(var2)) {
            return EnqueueDownloadTask.ErrorCode.DOWNLOAD_MANAGER_DISABLED;
         } else {
            Request var6 = (new Request(Uri.parse(this.download.getUrl()))).addRequestHeader("User-Agent", this.download.getUserAgent()).addRequestHeader("Cookie", var3).addRequestHeader("Referer", this.refererUrl).setDestinationInExternalPublicDir(var4, var5).setNotificationVisibility(1).setMimeType(this.download.getMimeType());
            var6.allowScanningByMediaScanner();
            Long var7 = ((DownloadManager)var2.getSystemService("download")).enqueue(var6);
            DownloadInfo var8 = new DownloadInfo();
            var8.setDownloadId(var7);
            if (!DownloadInfoManager.getInstance().recordExists(var7)) {
               DownloadInfoManager.getInstance().insert(var8, new DownloadInfoManager.AsyncInsertListener() {
                  public void onInsertComplete(long var1) {
                     DownloadInfoManager.notifyRowUpdated(var2, var1);
                  }
               });
            } else {
               DownloadInfoManager.getInstance().queryByDownloadId(var7, new DownloadInfoManager.AsyncQueryListener() {
                  public void onQueryComplete(List var1) {
                     if (!var1.isEmpty()) {
                        DownloadInfo var2x = (DownloadInfo)var1.get(0);
                        DownloadInfoManager.getInstance().delete(var2x.getRowId(), (DownloadInfoManager.AsyncDeleteListener)null);
                        DownloadInfoManager.getInstance().insert(var2x, new DownloadInfoManager.AsyncInsertListener() {
                           public void onInsertComplete(long var1) {
                              DownloadInfoManager.notifyRowUpdated(var2, var1);
                              RelocateService.broadcastRelocateFinished(var2, var1);
                           }
                        });
                     }

                  }
               });
            }

            return EnqueueDownloadTask.ErrorCode.SUCCESS;
         }
      }
   }

   protected void onPostExecute(EnqueueDownloadTask.ErrorCode var1) {
      Activity var2 = (Activity)this.activityRef.get();
      if (var2 != null && !var2.isFinishing() && !var2.isDestroyed()) {
         switch(var1) {
         case STORAGE_UNAVAILABLE:
            Toast.makeText(var2, 2131755266, 1).show();
            break;
         case FILE_NOT_SUPPORTED:
            Toast.makeText(var2, 2131755106, 1).show();
            break;
         case SUCCESS:
            if (!this.download.isStartFromContextMenu()) {
               Toast.makeText(var2, 2131755107, 1).show();
            }
         }

      }
   }

   public static enum ErrorCode {
      DOWNLOAD_MANAGER_DISABLED,
      FILE_NOT_SUPPORTED,
      GENERAL_ERROR,
      STORAGE_UNAVAILABLE,
      SUCCESS;
   }
}
