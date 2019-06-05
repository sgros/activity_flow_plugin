package org.mozilla.rocket.download;

import org.mozilla.rocket.download.DownloadInfoRepository.OnQueryListCompleteListener;

/* compiled from: DownloadInfoRepository.kt */
final class DownloadInfoRepository$queryDownloadingItems$1 implements Runnable {
    final /* synthetic */ OnQueryListCompleteListener $listenerList;
    final /* synthetic */ long[] $runningIds;

    DownloadInfoRepository$queryDownloadingItems$1(long[] jArr, OnQueryListCompleteListener onQueryListCompleteListener) {
        this.$runningIds = jArr;
        this.$listenerList = onQueryListCompleteListener;
    }

    /* JADX WARNING: Missing block: B:16:0x007e, code skipped:
            kotlin.p005io.CloseableKt.closeFinally(r0, r1);
     */
    public final void run() {
        /*
        r11 = this;
        r0 = new android.app.DownloadManager$Query;
        r0.<init>();
        r1 = r11.$runningIds;
        r2 = r1.length;
        r1 = java.util.Arrays.copyOf(r1, r2);
        r0.setFilterById(r1);
        r1 = 2;
        r0.setFilterByStatus(r1);
        r1 = org.mozilla.focus.download.DownloadInfoManager.getInstance();
        r2 = "DownloadInfoManager.getInstance()";
        kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r1, r2);
        r1 = r1.getDownloadManager();
        r0 = r1.query(r0);
        r0 = (java.io.Closeable) r0;
        r1 = 0;
        r1 = (java.lang.Throwable) r1;
        r2 = r0;
        r2 = (android.database.Cursor) r2;	 Catch:{ Throwable -> 0x007c }
        if (r2 == 0) goto L_0x0074;
    L_0x002e:
        r3 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x007c }
        r3.<init>();	 Catch:{ Throwable -> 0x007c }
    L_0x0033:
        r4 = r2.moveToNext();	 Catch:{ Throwable -> 0x007c }
        if (r4 == 0) goto L_0x006d;
    L_0x0039:
        r4 = "_id";
        r4 = r2.getColumnIndex(r4);	 Catch:{ Throwable -> 0x007c }
        r4 = r2.getLong(r4);	 Catch:{ Throwable -> 0x007c }
        r6 = "total_size";
        r6 = r2.getColumnIndex(r6);	 Catch:{ Throwable -> 0x007c }
        r6 = r2.getDouble(r6);	 Catch:{ Throwable -> 0x007c }
        r8 = "bytes_so_far";
        r8 = r2.getColumnIndex(r8);	 Catch:{ Throwable -> 0x007c }
        r8 = r2.getDouble(r8);	 Catch:{ Throwable -> 0x007c }
        r10 = new org.mozilla.focus.download.DownloadInfo;	 Catch:{ Throwable -> 0x007c }
        r10.<init>();	 Catch:{ Throwable -> 0x007c }
        r4 = java.lang.Long.valueOf(r4);	 Catch:{ Throwable -> 0x007c }
        r10.setDownloadId(r4);	 Catch:{ Throwable -> 0x007c }
        r10.setSizeTotal(r6);	 Catch:{ Throwable -> 0x007c }
        r10.setSizeSoFar(r8);	 Catch:{ Throwable -> 0x007c }
        r3.add(r10);	 Catch:{ Throwable -> 0x007c }
        goto L_0x0033;
    L_0x006d:
        r2 = r11.$listenerList;	 Catch:{ Throwable -> 0x007c }
        r3 = (java.util.List) r3;	 Catch:{ Throwable -> 0x007c }
        r2.onComplete(r3);	 Catch:{ Throwable -> 0x007c }
    L_0x0074:
        r2 = kotlin.Unit.INSTANCE;	 Catch:{ Throwable -> 0x007c }
        kotlin.p005io.CloseableKt.closeFinally(r0, r1);
        return;
    L_0x007a:
        r2 = move-exception;
        goto L_0x007e;
    L_0x007c:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x007a }
    L_0x007e:
        kotlin.p005io.CloseableKt.closeFinally(r0, r1);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.rocket.download.DownloadInfoRepository$queryDownloadingItems$1.run():void");
    }
}
