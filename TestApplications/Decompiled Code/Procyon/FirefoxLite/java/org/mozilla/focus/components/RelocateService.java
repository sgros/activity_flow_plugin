// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.components;

import android.app.Service;
import android.support.v4.app.ServiceCompat;
import android.support.v4.app.NotificationCompat;
import org.mozilla.focus.utils.NoRemovableStorageException;
import org.mozilla.focus.download.DownloadInfoManager;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.utils.StorageUtils;
import android.support.v4.content.ContextCompat;
import org.mozilla.focus.utils.Settings;
import android.app.NotificationChannel;
import android.os.Build$VERSION;
import android.app.NotificationManager;
import android.util.Log;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import java.io.Serializable;
import android.content.Intent;
import java.io.File;
import android.app.IntentService;

public class RelocateService extends IntentService
{
    public RelocateService() {
        super("RelocateService");
    }
    
    private void broadcastNoPermission(final long n, final File file, final String type) {
        final Intent intent = new Intent("org.mozilla.action.REQUEST_PERMISSION");
        intent.addCategory("org.mozilla.category.FILE_OPERATION");
        intent.putExtra("org.mozilla.extra.download_id", n);
        intent.putExtra("org.mozilla.extra.file_path", (Serializable)file.getAbsoluteFile());
        intent.setType(type);
        LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intent);
        Log.d("RelocateService", "no permission for file relocating, send broadcast to grant permission");
    }
    
    private void broadcastRelocateFinished(final long n) {
        broadcastRelocateFinished((Context)this, n);
    }
    
    public static void broadcastRelocateFinished(final Context context, final long n) {
        final Intent intent = new Intent("org.mozilla.action.RELOCATE_FINISH");
        intent.addCategory("org.mozilla.category.FILE_OPERATION");
        intent.putExtra("org.mozilla.extra.row_id", n);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    
    private void broadcastUi(final CharSequence charSequence) {
        final Intent intent = new Intent("org.mozilla.action.NOTIFY_UI");
        intent.addCategory("org.mozilla.category.FILE_OPERATION");
        intent.putExtra("org.mozilla.extra.message", charSequence);
        LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intent);
    }
    
    private static void configForegroundChannel(final Context context) {
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
        if (Build$VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel("relocation_service", (CharSequence)context.getString(2131755062), 4));
        }
    }
    
    private void handleActionMove(final long n, final long n2, final File file, final String s) {
        if (!Settings.getInstance(this.getApplicationContext()).shouldSaveToRemovableStorage()) {
            this.broadcastRelocateFinished(n);
            return;
        }
        if (ContextCompat.checkSelfPermission((Context)this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            this.moveFile(n, n2, file, s);
        }
        else {
            this.broadcastNoPermission(n2, file, s);
        }
    }
    
    private void moveFile(final long n, final long n2, final File file, String s) {
        final Settings instance = Settings.getInstance(this.getApplicationContext());
        try {
            File fileSlot;
            try {
                final File targetDirOnRemovableStorageForDownloads = StorageUtils.getTargetDirOnRemovableStorageForDownloads((Context)this, (String)s);
                if (targetDirOnRemovableStorageForDownloads == null) {
                    return;
                }
                FileUtils.ensureDir(targetDirOnRemovableStorageForDownloads);
                fileSlot = FileUtils.getFileSlot(targetDirOnRemovableStorageForDownloads, file.getName());
                try {
                    if (targetDirOnRemovableStorageForDownloads.getUsableSpace() < file.length()) {
                        s = this.getString(2131755264);
                        this.broadcastUi((CharSequence)s);
                        Log.w("RelocateService", ((CharSequence)s).toString());
                        this.broadcastRelocateFinished(n);
                        return;
                    }
                    if (!FileUtils.copy(file, fileSlot)) {
                        Log.w("RelocateService", String.format("cannot copy file from %s to %s", file.getPath(), fileSlot.getPath()));
                        this.broadcastRelocateFinished(n);
                        return;
                    }
                    if (!file.delete()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Cannot delete original file: ");
                        sb.append(file.getAbsolutePath());
                        throw new RuntimeException(sb.toString());
                    }
                    DownloadInfoManager.getInstance().replacePath(n2, fileSlot.getAbsolutePath(), (String)s);
                    if (!instance.getRemovableStorageStateOnCreate() && instance.getShowedStorageMessage() != 22919) {
                        instance.setShowedStorageMessage(22919);
                        s = this.getString(2131755265);
                        this.broadcastUi((CharSequence)s);
                        Log.w("RelocateService", ((CharSequence)s).toString());
                    }
                    return;
                }
                catch (Exception s) {}
            }
            catch (Exception s) {
                fileSlot = null;
            }
            this.broadcastRelocateFinished(n);
            if (fileSlot != null) {
                try {
                    if (fileSlot.exists() && fileSlot.canWrite() && file.exists() && fileSlot.delete()) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("cannot delete copied file: ");
                        sb2.append(fileSlot.getAbsolutePath());
                        Log.w("RelocateService", sb2.toString());
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            ((Throwable)s).printStackTrace();
        }
        catch (NoRemovableStorageException ex2) {
            this.broadcastRelocateFinished(n);
            if (instance.getRemovableStorageStateOnCreate() && instance.getShowedStorageMessage() != 38183) {
                instance.setShowedStorageMessage(38183);
                s = this.getString(2131755263);
                this.broadcastUi((CharSequence)s);
                Log.w("RelocateService", ((CharSequence)s).toString());
            }
            ex2.printStackTrace();
        }
    }
    
    public static void startActionMove(final Context context, final long n, final long n2, final File file, final String type) {
        final Intent intent = new Intent(context, (Class)RelocateService.class);
        intent.setAction("org.mozilla.focus.components.action.MOVE");
        intent.putExtra("org.mozilla.extra.row_id", n);
        intent.putExtra("org.mozilla.extra.download_id", n2);
        intent.putExtra("org.mozilla.extra.file_path", file.getAbsolutePath());
        intent.setType(type);
        ContextCompat.startForegroundService(context, intent);
    }
    
    private void startForeground() {
        String s;
        if (Build$VERSION.SDK_INT >= 26) {
            configForegroundChannel((Context)this);
            s = "relocation_service";
        }
        else {
            s = "not_used_notification_id";
        }
        this.startForeground(2000, new NotificationCompat.Builder(this.getApplicationContext(), s).build());
    }
    
    private void stopForeground() {
        ServiceCompat.stopForeground((Service)this, 1);
    }
    
    protected void onHandleIntent(final Intent intent) {
        if (intent != null && "org.mozilla.focus.components.action.MOVE".equals(intent.getAction())) {
            final long longExtra = intent.getLongExtra("org.mozilla.extra.download_id", -1L);
            if (!DownloadInfoManager.getInstance().recordExists(longExtra)) {
                return;
            }
            final File file = new File(intent.getStringExtra("org.mozilla.extra.file_path"));
            if (!file.exists() || !file.canWrite()) {
                return;
            }
            final long longExtra2 = intent.getLongExtra("org.mozilla.extra.row_id", -1L);
            final String type = intent.getType();
            this.startForeground();
            this.handleActionMove(longExtra2, longExtra, file, type);
            this.stopForeground();
        }
    }
}
