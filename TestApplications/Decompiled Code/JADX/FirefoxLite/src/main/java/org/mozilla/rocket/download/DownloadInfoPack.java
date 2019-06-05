package org.mozilla.rocket.download;

import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;

/* compiled from: DownloadInfoPack.kt */
public final class DownloadInfoPack {
    private long index;
    private ArrayList<DownloadInfo> list;
    private int notifyType;

    public DownloadInfoPack(ArrayList<DownloadInfo> arrayList, int i, long j) {
        Intrinsics.checkParameterIsNotNull(arrayList, "list");
        this.list = arrayList;
        this.notifyType = i;
        this.index = j;
    }

    public final long getIndex() {
        return this.index;
    }

    public final ArrayList<DownloadInfo> getList() {
        return this.list;
    }

    public final int getNotifyType() {
        return this.notifyType;
    }

    public final void setIndex(long j) {
        this.index = j;
    }

    public final void setNotifyType(int i) {
        this.notifyType = i;
    }
}
