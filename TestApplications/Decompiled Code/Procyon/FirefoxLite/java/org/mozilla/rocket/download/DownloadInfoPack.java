// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.download;

import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import java.util.ArrayList;

public final class DownloadInfoPack
{
    private long index;
    private ArrayList<DownloadInfo> list;
    private int notifyType;
    
    public DownloadInfoPack(final ArrayList<DownloadInfo> list, final int notifyType, final long index) {
        Intrinsics.checkParameterIsNotNull(list, "list");
        this.list = list;
        this.notifyType = notifyType;
        this.index = index;
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
    
    public final void setIndex(final long index) {
        this.index = index;
    }
    
    public final void setNotifyType(final int notifyType) {
        this.notifyType = notifyType;
    }
}
