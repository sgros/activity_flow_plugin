// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.download;

import java.util.List;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.threadutils.ThreadUtils;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfoManager;

public final class DownloadInfoRepository
{
    public static final Companion Companion;
    private static volatile DownloadInfoRepository INSTANCE;
    
    static {
        Companion = new Companion(null);
    }
    
    public static final /* synthetic */ DownloadInfoRepository access$getINSTANCE$cp() {
        return DownloadInfoRepository.INSTANCE;
    }
    
    public static final /* synthetic */ void access$setINSTANCE$cp(final DownloadInfoRepository instance) {
        DownloadInfoRepository.INSTANCE = instance;
    }
    
    public static final DownloadInfoRepository getInstance() {
        return DownloadInfoRepository.Companion.getInstance();
    }
    
    public final void deleteFromDownloadManager(final long n) {
        final DownloadInfoManager instance = DownloadInfoManager.getInstance();
        Intrinsics.checkExpressionValueIsNotNull(instance, "DownloadInfoManager.getInstance()");
        instance.getDownloadManager().remove(new long[] { n });
    }
    
    public final void loadData(final int n, final int n2, final OnQueryListCompleteListener onQueryListCompleteListener) {
        Intrinsics.checkParameterIsNotNull(onQueryListCompleteListener, "listenerList");
        DownloadInfoManager.getInstance().query(n, n2, (DownloadInfoManager.AsyncQueryListener)new DownloadInfoRepository$loadData.DownloadInfoRepository$loadData$1(onQueryListCompleteListener));
    }
    
    public final void markAllItemsAreRead() {
        DownloadInfoManager.getInstance().markAllItemsAreRead(null);
    }
    
    public final void queryByDownloadId(final long l, final OnQueryItemCompleteListener onQueryItemCompleteListener) {
        Intrinsics.checkParameterIsNotNull(onQueryItemCompleteListener, "listenerItem");
        DownloadInfoManager.getInstance().queryByDownloadId(l, (DownloadInfoManager.AsyncQueryListener)new DownloadInfoRepository$queryByDownloadId.DownloadInfoRepository$queryByDownloadId$1(onQueryItemCompleteListener));
    }
    
    public final void queryByRowId(final long l, final OnQueryItemCompleteListener onQueryItemCompleteListener) {
        Intrinsics.checkParameterIsNotNull(onQueryItemCompleteListener, "listenerItem");
        DownloadInfoManager.getInstance().queryByRowId(l, (DownloadInfoManager.AsyncQueryListener)new DownloadInfoRepository$queryByRowId.DownloadInfoRepository$queryByRowId$1(onQueryItemCompleteListener));
    }
    
    public final void queryDownloadingItems(final long[] array, final OnQueryListCompleteListener onQueryListCompleteListener) {
        Intrinsics.checkParameterIsNotNull(array, "runningIds");
        Intrinsics.checkParameterIsNotNull(onQueryListCompleteListener, "listenerList");
        ThreadUtils.postToBackgroundThread((Runnable)new DownloadInfoRepository$queryDownloadingItems.DownloadInfoRepository$queryDownloadingItems$1(array, onQueryListCompleteListener));
    }
    
    public final void queryIndicatorStatus(final OnQueryListCompleteListener onQueryListCompleteListener) {
        Intrinsics.checkParameterIsNotNull(onQueryListCompleteListener, "listenerList");
        DownloadInfoManager.getInstance().queryDownloadingAndUnreadIds((DownloadInfoManager.AsyncQueryListener)new DownloadInfoRepository$queryIndicatorStatus.DownloadInfoRepository$queryIndicatorStatus$1(onQueryListCompleteListener));
    }
    
    public final void remove(final long l) {
        DownloadInfoManager.getInstance().delete(l, null);
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        public final DownloadInfoRepository getInstance() {
            DownloadInfoRepository downloadInfoRepository = DownloadInfoRepository.access$getINSTANCE$cp();
            if (downloadInfoRepository != null) {
                return downloadInfoRepository;
            }
            synchronized (this) {
                downloadInfoRepository = DownloadInfoRepository.access$getINSTANCE$cp();
                if (downloadInfoRepository == null) {
                    downloadInfoRepository = new DownloadInfoRepository();
                    DownloadInfoRepository.access$setINSTANCE$cp(downloadInfoRepository);
                }
                return downloadInfoRepository;
            }
        }
    }
    
    public interface OnQueryItemCompleteListener
    {
        void onComplete(final DownloadInfo p0);
    }
    
    public interface OnQueryListCompleteListener
    {
        void onComplete(final List<? extends DownloadInfo> p0);
    }
}
