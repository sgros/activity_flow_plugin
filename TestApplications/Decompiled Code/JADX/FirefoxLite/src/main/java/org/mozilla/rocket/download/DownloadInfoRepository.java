package org.mozilla.rocket.download;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.focus.download.DownloadInfoManager;
import org.mozilla.threadutils.ThreadUtils;

/* compiled from: DownloadInfoRepository.kt */
public final class DownloadInfoRepository {
    public static final Companion Companion = new Companion();
    private static volatile DownloadInfoRepository INSTANCE;

    /* compiled from: DownloadInfoRepository.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final DownloadInfoRepository getInstance() {
            DownloadInfoRepository access$getINSTANCE$cp = DownloadInfoRepository.INSTANCE;
            if (access$getINSTANCE$cp == null) {
                synchronized (this) {
                    access$getINSTANCE$cp = DownloadInfoRepository.INSTANCE;
                    if (access$getINSTANCE$cp == null) {
                        access$getINSTANCE$cp = new DownloadInfoRepository();
                        DownloadInfoRepository.INSTANCE = access$getINSTANCE$cp;
                    }
                }
            }
            return access$getINSTANCE$cp;
        }
    }

    /* compiled from: DownloadInfoRepository.kt */
    public interface OnQueryItemCompleteListener {
        void onComplete(DownloadInfo downloadInfo);
    }

    /* compiled from: DownloadInfoRepository.kt */
    public interface OnQueryListCompleteListener {
        void onComplete(List<? extends DownloadInfo> list);
    }

    public static final DownloadInfoRepository getInstance() {
        return Companion.getInstance();
    }

    public final void queryIndicatorStatus(OnQueryListCompleteListener onQueryListCompleteListener) {
        Intrinsics.checkParameterIsNotNull(onQueryListCompleteListener, "listenerList");
        DownloadInfoManager.getInstance().queryDownloadingAndUnreadIds(new DownloadInfoRepository$queryIndicatorStatus$1(onQueryListCompleteListener));
    }

    public final void queryByRowId(long j, OnQueryItemCompleteListener onQueryItemCompleteListener) {
        Intrinsics.checkParameterIsNotNull(onQueryItemCompleteListener, "listenerItem");
        DownloadInfoManager.getInstance().queryByRowId(Long.valueOf(j), new DownloadInfoRepository$queryByRowId$1(onQueryItemCompleteListener));
    }

    public final void queryByDownloadId(long j, OnQueryItemCompleteListener onQueryItemCompleteListener) {
        Intrinsics.checkParameterIsNotNull(onQueryItemCompleteListener, "listenerItem");
        DownloadInfoManager.getInstance().queryByDownloadId(Long.valueOf(j), new DownloadInfoRepository$queryByDownloadId$1(onQueryItemCompleteListener));
    }

    public final void queryDownloadingItems(long[] jArr, OnQueryListCompleteListener onQueryListCompleteListener) {
        Intrinsics.checkParameterIsNotNull(jArr, "runningIds");
        Intrinsics.checkParameterIsNotNull(onQueryListCompleteListener, "listenerList");
        ThreadUtils.postToBackgroundThread((Runnable) new DownloadInfoRepository$queryDownloadingItems$1(jArr, onQueryListCompleteListener));
    }

    public final void markAllItemsAreRead() {
        DownloadInfoManager.getInstance().markAllItemsAreRead(null);
    }

    public final void loadData(int i, int i2, OnQueryListCompleteListener onQueryListCompleteListener) {
        Intrinsics.checkParameterIsNotNull(onQueryListCompleteListener, "listenerList");
        DownloadInfoManager.getInstance().query(i, i2, new DownloadInfoRepository$loadData$1(onQueryListCompleteListener));
    }

    public final void remove(long j) {
        DownloadInfoManager.getInstance().delete(Long.valueOf(j), null);
    }

    public final void deleteFromDownloadManager(long j) {
        DownloadInfoManager instance = DownloadInfoManager.getInstance();
        Intrinsics.checkExpressionValueIsNotNull(instance, "DownloadInfoManager.getInstance()");
        instance.getDownloadManager().remove(new long[]{j});
    }
}
