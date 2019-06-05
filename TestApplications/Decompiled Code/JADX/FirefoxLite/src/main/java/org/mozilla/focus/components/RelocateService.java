package org.mozilla.focus.components;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.support.p001v4.app.NotificationCompat.Builder;
import android.support.p001v4.app.ServiceCompat;
import android.support.p001v4.content.ContextCompat;
import android.support.p001v4.content.LocalBroadcastManager;
import android.util.Log;
import java.io.File;
import org.mozilla.focus.download.DownloadInfoManager;
import org.mozilla.focus.utils.Settings;
import org.mozilla.rocket.C0769R;

public class RelocateService extends IntentService {
    public RelocateService() {
        super("RelocateService");
    }

    public static void startActionMove(Context context, long j, long j2, File file, String str) {
        Intent intent = new Intent(context, RelocateService.class);
        intent.setAction("org.mozilla.focus.components.action.MOVE");
        intent.putExtra("org.mozilla.extra.row_id", j);
        intent.putExtra("org.mozilla.extra.download_id", j2);
        intent.putExtra("org.mozilla.extra.file_path", file.getAbsolutePath());
        intent.setType(str);
        ContextCompat.startForegroundService(context, intent);
    }

    private void startForeground() {
        String str;
        if (VERSION.SDK_INT >= 26) {
            configForegroundChannel(this);
            str = "relocation_service";
        } else {
            str = "not_used_notification_id";
        }
        startForeground(2000, new Builder(getApplicationContext(), str).build());
    }

    private static void configForegroundChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel("relocation_service", context.getString(C0769R.string.app_name), 4));
        }
    }

    private void stopForeground() {
        ServiceCompat.stopForeground(this, 1);
    }

    /* Access modifiers changed, original: protected */
    public void onHandleIntent(Intent intent) {
        if (intent != null) {
            if ("org.mozilla.focus.components.action.MOVE".equals(intent.getAction())) {
                long longExtra = intent.getLongExtra("org.mozilla.extra.download_id", -1);
                if (DownloadInfoManager.getInstance().recordExists(longExtra)) {
                    File file = new File(intent.getStringExtra("org.mozilla.extra.file_path"));
                    if (file.exists() && file.canWrite()) {
                        long longExtra2 = intent.getLongExtra("org.mozilla.extra.row_id", -1);
                        String type = intent.getType();
                        startForeground();
                        handleActionMove(longExtra2, longExtra, file, type);
                        stopForeground();
                    }
                }
            }
        }
    }

    private void handleActionMove(long j, long j2, File file, String str) {
        if (Settings.getInstance(getApplicationContext()).shouldSaveToRemovableStorage()) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                moveFile(j, j2, file, str);
            } else {
                broadcastNoPermission(j2, file, str);
            }
            return;
        }
        broadcastRelocateFinished(j);
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00f9 A:{ExcHandler: NoRemovableStorageException (r9_14 'e' org.mozilla.focus.utils.NoRemovableStorageException), Splitter:B:1:0x0009} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00be A:{SYNTHETIC, Splitter:B:28:0x00be} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:24:0x00b7, code skipped:
            r9 = e;
     */
    /* JADX WARNING: Missing block: B:25:0x00b8, code skipped:
            r3 = null;
     */
    /* JADX WARNING: Missing block: B:37:0x00d6, code skipped:
            r8 = new java.lang.StringBuilder();
            r8.append("cannot delete copied file: ");
            r8.append(r3.getAbsolutePath());
            android.util.Log.w("RelocateService", r8.toString());
     */
    /* JADX WARNING: Missing block: B:38:0x00f1, code skipped:
            r7 = move-exception;
     */
    /* JADX WARNING: Missing block: B:39:0x00f2, code skipped:
            r7.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:41:0x00f9, code skipped:
            r9 = move-exception;
     */
    /* JADX WARNING: Missing block: B:42:0x00fa, code skipped:
            broadcastRelocateFinished(r7);
     */
    /* JADX WARNING: Missing block: B:46:0x010c, code skipped:
            r0.setShowedStorageMessage(38183);
            r7 = getString(org.mozilla.rocket.C0769R.string.message_fallback_save_to_primary_external);
            broadcastUi(r7);
            android.util.Log.w("RelocateService", r7.toString());
     */
    /* JADX WARNING: Missing block: B:47:0x0122, code skipped:
            r9.printStackTrace();
     */
    private void moveFile(long r7, long r9, java.io.File r11, java.lang.String r12) {
        /*
        r6 = this;
        r0 = r6.getApplicationContext();
        r0 = org.mozilla.focus.utils.Settings.getInstance(r0);
        r1 = 0;
        r2 = org.mozilla.focus.utils.StorageUtils.getTargetDirOnRemovableStorageForDownloads(r6, r12);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b7 }
        if (r2 == 0) goto L_0x0125;
    L_0x000f:
        org.mozilla.fileutils.FileUtils.ensureDir(r2);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b7 }
        r3 = r11.getName();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b7 }
        r3 = org.mozilla.fileutils.FileUtils.getFileSlot(r2, r3);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b7 }
        r1 = r2.getUsableSpace();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r4 = r11.length();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r1 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1));
        if (r1 >= 0) goto L_0x003d;
    L_0x0026:
        r9 = 2131755264; // 0x7f100100 float:1.9141402E38 double:1.0532270413E-314;
        r9 = r6.getString(r9);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r6.broadcastUi(r9);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r10 = "RelocateService";
        r9 = r9.toString();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        android.util.Log.w(r10, r9);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r6.broadcastRelocateFinished(r7);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        return;
    L_0x003d:
        r1 = org.mozilla.fileutils.FileUtils.copy(r11, r3);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        if (r1 != 0) goto L_0x0063;
    L_0x0043:
        r9 = "RelocateService";
        r10 = "cannot copy file from %s to %s";
        r12 = 2;
        r12 = new java.lang.Object[r12];	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r1 = 0;
        r2 = r11.getPath();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r12[r1] = r2;	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r1 = 1;
        r2 = r3.getPath();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r12[r1] = r2;	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r10 = java.lang.String.format(r10, r12);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        android.util.Log.w(r9, r10);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r6.broadcastRelocateFinished(r7);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        return;
    L_0x0063:
        r1 = r11.delete();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        if (r1 == 0) goto L_0x009a;
    L_0x0069:
        r1 = org.mozilla.focus.download.DownloadInfoManager.getInstance();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r2 = r3.getAbsolutePath();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r1.replacePath(r9, r2, r12);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r9 = r0.getRemovableStorageStateOnCreate();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        if (r9 != 0) goto L_0x0125;
    L_0x007a:
        r9 = r0.getShowedStorageMessage();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r10 = 22919; // 0x5987 float:3.2116E-41 double:1.13235E-319;
        if (r9 == r10) goto L_0x0125;
    L_0x0082:
        r0.setShowedStorageMessage(r10);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r9 = 2131755265; // 0x7f100101 float:1.9141404E38 double:1.053227042E-314;
        r9 = r6.getString(r9);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r6.broadcastUi(r9);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r10 = "RelocateService";
        r9 = r9.toString();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        android.util.Log.w(r10, r9);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        goto L_0x0125;
    L_0x009a:
        r9 = new java.lang.RuntimeException;	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r10 = new java.lang.StringBuilder;	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r10.<init>();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r12 = "Cannot delete original file: ";
        r10.append(r12);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r12 = r11.getAbsolutePath();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r10.append(r12);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r10 = r10.toString();	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        r9.<init>(r10);	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
        throw r9;	 Catch:{ NoRemovableStorageException -> 0x00f9, Exception -> 0x00b5 }
    L_0x00b5:
        r9 = move-exception;
        goto L_0x00b9;
    L_0x00b7:
        r9 = move-exception;
        r3 = r1;
    L_0x00b9:
        r6.broadcastRelocateFinished(r7);
        if (r3 == 0) goto L_0x00f5;
    L_0x00be:
        r7 = r3.exists();	 Catch:{ Exception -> 0x00f1 }
        if (r7 == 0) goto L_0x00f5;
    L_0x00c4:
        r7 = r3.canWrite();	 Catch:{ Exception -> 0x00f1 }
        if (r7 == 0) goto L_0x00f5;
    L_0x00ca:
        r7 = r11.exists();	 Catch:{ Exception -> 0x00f1 }
        if (r7 == 0) goto L_0x00f5;
    L_0x00d0:
        r7 = r3.delete();	 Catch:{ Exception -> 0x00f1 }
        if (r7 == 0) goto L_0x00f5;
    L_0x00d6:
        r7 = "RelocateService";
        r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00f1 }
        r8.<init>();	 Catch:{ Exception -> 0x00f1 }
        r10 = "cannot delete copied file: ";
        r8.append(r10);	 Catch:{ Exception -> 0x00f1 }
        r10 = r3.getAbsolutePath();	 Catch:{ Exception -> 0x00f1 }
        r8.append(r10);	 Catch:{ Exception -> 0x00f1 }
        r8 = r8.toString();	 Catch:{ Exception -> 0x00f1 }
        android.util.Log.w(r7, r8);	 Catch:{ Exception -> 0x00f1 }
        goto L_0x00f5;
    L_0x00f1:
        r7 = move-exception;
        r7.printStackTrace();
    L_0x00f5:
        r9.printStackTrace();
        goto L_0x0125;
    L_0x00f9:
        r9 = move-exception;
        r6.broadcastRelocateFinished(r7);
        r7 = r0.getRemovableStorageStateOnCreate();
        if (r7 == 0) goto L_0x0122;
    L_0x0103:
        r7 = r0.getShowedStorageMessage();
        r8 = 38183; // 0x9527 float:5.3506E-41 double:1.8865E-319;
        if (r7 == r8) goto L_0x0122;
    L_0x010c:
        r0.setShowedStorageMessage(r8);
        r7 = 2131755263; // 0x7f1000ff float:1.91414E38 double:1.053227041E-314;
        r7 = r6.getString(r7);
        r6.broadcastUi(r7);
        r8 = "RelocateService";
        r7 = r7.toString();
        android.util.Log.w(r8, r7);
    L_0x0122:
        r9.printStackTrace();
    L_0x0125:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.components.RelocateService.moveFile(long, long, java.io.File, java.lang.String):void");
    }

    private void broadcastUi(CharSequence charSequence) {
        Intent intent = new Intent("org.mozilla.action.NOTIFY_UI");
        intent.addCategory("org.mozilla.category.FILE_OPERATION");
        intent.putExtra("org.mozilla.extra.message", charSequence);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastNoPermission(long j, File file, String str) {
        Intent intent = new Intent("org.mozilla.action.REQUEST_PERMISSION");
        intent.addCategory("org.mozilla.category.FILE_OPERATION");
        intent.putExtra("org.mozilla.extra.download_id", j);
        intent.putExtra("org.mozilla.extra.file_path", file.getAbsoluteFile());
        intent.setType(str);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d("RelocateService", "no permission for file relocating, send broadcast to grant permission");
    }

    private void broadcastRelocateFinished(long j) {
        broadcastRelocateFinished(this, j);
    }

    public static void broadcastRelocateFinished(Context context, long j) {
        Intent intent = new Intent("org.mozilla.action.RELOCATE_FINISH");
        intent.addCategory("org.mozilla.category.FILE_OPERATION");
        intent.putExtra("org.mozilla.extra.row_id", j);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
