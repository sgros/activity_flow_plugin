package org.mozilla.focus.components;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.ServiceCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import java.io.File;
import org.mozilla.focus.download.DownloadInfoManager;
import org.mozilla.focus.utils.Settings;

public class RelocateService extends IntentService {
   public RelocateService() {
      super("RelocateService");
   }

   private void broadcastNoPermission(long var1, File var3, String var4) {
      Intent var5 = new Intent("org.mozilla.action.REQUEST_PERMISSION");
      var5.addCategory("org.mozilla.category.FILE_OPERATION");
      var5.putExtra("org.mozilla.extra.download_id", var1);
      var5.putExtra("org.mozilla.extra.file_path", var3.getAbsoluteFile());
      var5.setType(var4);
      LocalBroadcastManager.getInstance(this).sendBroadcast(var5);
      Log.d("RelocateService", "no permission for file relocating, send broadcast to grant permission");
   }

   private void broadcastRelocateFinished(long var1) {
      broadcastRelocateFinished(this, var1);
   }

   public static void broadcastRelocateFinished(Context var0, long var1) {
      Intent var3 = new Intent("org.mozilla.action.RELOCATE_FINISH");
      var3.addCategory("org.mozilla.category.FILE_OPERATION");
      var3.putExtra("org.mozilla.extra.row_id", var1);
      LocalBroadcastManager.getInstance(var0).sendBroadcast(var3);
   }

   private void broadcastUi(CharSequence var1) {
      Intent var2 = new Intent("org.mozilla.action.NOTIFY_UI");
      var2.addCategory("org.mozilla.category.FILE_OPERATION");
      var2.putExtra("org.mozilla.extra.message", var1);
      LocalBroadcastManager.getInstance(this).sendBroadcast(var2);
   }

   private static void configForegroundChannel(Context var0) {
      NotificationManager var1 = (NotificationManager)var0.getSystemService("notification");
      if (VERSION.SDK_INT >= 26) {
         var1.createNotificationChannel(new NotificationChannel("relocation_service", var0.getString(2131755062), 4));
      }

   }

   private void handleActionMove(long var1, long var3, File var5, String var6) {
      if (!Settings.getInstance(this.getApplicationContext()).shouldSaveToRemovableStorage()) {
         this.broadcastRelocateFinished(var1);
      } else {
         if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            this.moveFile(var1, var3, var5, var6);
         } else {
            this.broadcastNoPermission(var3, var5, var6);
         }

      }
   }

   private void moveFile(long param1, long param3, File param5, String param6) {
      // $FF: Couldn't be decompiled
   }

   public static void startActionMove(Context var0, long var1, long var3, File var5, String var6) {
      Intent var7 = new Intent(var0, RelocateService.class);
      var7.setAction("org.mozilla.focus.components.action.MOVE");
      var7.putExtra("org.mozilla.extra.row_id", var1);
      var7.putExtra("org.mozilla.extra.download_id", var3);
      var7.putExtra("org.mozilla.extra.file_path", var5.getAbsolutePath());
      var7.setType(var6);
      ContextCompat.startForegroundService(var0, var7);
   }

   private void startForeground() {
      String var1;
      if (VERSION.SDK_INT >= 26) {
         configForegroundChannel(this);
         var1 = "relocation_service";
      } else {
         var1 = "not_used_notification_id";
      }

      this.startForeground(2000, (new NotificationCompat.Builder(this.getApplicationContext(), var1)).build());
   }

   private void stopForeground() {
      ServiceCompat.stopForeground(this, 1);
   }

   protected void onHandleIntent(Intent var1) {
      if (var1 != null && "org.mozilla.focus.components.action.MOVE".equals(var1.getAction())) {
         long var2 = var1.getLongExtra("org.mozilla.extra.download_id", -1L);
         if (!DownloadInfoManager.getInstance().recordExists(var2)) {
            return;
         }

         File var4 = new File(var1.getStringExtra("org.mozilla.extra.file_path"));
         if (!var4.exists() || !var4.canWrite()) {
            return;
         }

         long var5 = var1.getLongExtra("org.mozilla.extra.row_id", -1L);
         String var7 = var1.getType();
         this.startForeground();
         this.handleActionMove(var5, var2, var4, var7);
         this.stopForeground();
      }

   }
}
