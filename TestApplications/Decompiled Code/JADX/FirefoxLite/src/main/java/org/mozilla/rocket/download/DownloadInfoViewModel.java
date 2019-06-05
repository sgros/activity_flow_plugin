package org.mozilla.rocket.download;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.download.DownloadInfoRepository.OnQueryItemCompleteListener;

/* compiled from: DownloadInfoViewModel.kt */
public final class DownloadInfoViewModel extends ViewModel {
    public static final Companion Companion = new Companion();
    private final SingleLiveEvent<DownloadInfo> deleteSnackbarObservable = new SingleLiveEvent();
    private final MutableLiveData<DownloadInfoPack> downloadInfoObservable = new MutableLiveData();
    private final DownloadInfoPack downloadInfoPack = new DownloadInfoPack(new ArrayList(), -1, -1);
    private boolean isLastPage;
    private boolean isLoading;
    private boolean isOpening;
    private int itemCount;
    private OnProgressUpdateListener progressUpdateListener;
    private final DownloadInfoRepository repository;
    private final SingleLiveEvent<Integer> toastMessageObservable = new SingleLiveEvent();
    private OnQueryItemCompleteListener updateListener = new DownloadInfoViewModel$updateListener$1(this);

    /* compiled from: DownloadInfoViewModel.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: DownloadInfoViewModel.kt */
    public interface OnProgressUpdateListener {
        void onCompleteUpdate();

        void onStartUpdate();

        void onStopUpdate();
    }

    public DownloadInfoViewModel(DownloadInfoRepository downloadInfoRepository) {
        Intrinsics.checkParameterIsNotNull(downloadInfoRepository, "repository");
        this.repository = downloadInfoRepository;
    }

    public final MutableLiveData<DownloadInfoPack> getDownloadInfoObservable() {
        return this.downloadInfoObservable;
    }

    public final SingleLiveEvent<Integer> getToastMessageObservable() {
        return this.toastMessageObservable;
    }

    public final SingleLiveEvent<DownloadInfo> getDeleteSnackbarObservable() {
        return this.deleteSnackbarObservable;
    }

    public final boolean isOpening() {
        return this.isOpening;
    }

    public final void setOpening(boolean z) {
        this.isOpening = z;
    }

    private final long[] getRunningDownloadIds() {
        int i;
        Collection arrayList = new ArrayList();
        Iterator it = this.downloadInfoPack.getList().iterator();
        while (true) {
            i = 0;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            DownloadInfo downloadInfo = (DownloadInfo) next;
            if (downloadInfo.getStatus() == 2 || downloadInfo.getStatus() == 1) {
                i = 1;
            }
            if (i != 0) {
                arrayList.add(next);
            }
        }
        Iterable<DownloadInfo> iterable = (List) arrayList;
        Collection arrayList2 = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (DownloadInfo downloadId : iterable) {
            arrayList2.add(downloadId.getDownloadId());
        }
        List list = (List) arrayList2;
        long[] jArr = new long[list.size()];
        int length = jArr.length;
        while (i < length) {
            Object obj = list.get(i);
            Intrinsics.checkExpressionValueIsNotNull(obj, "ids[i]");
            jArr[i] = ((Number) obj).longValue();
            i++;
        }
        return jArr;
    }

    private final boolean isDownloading() {
        Iterable<DownloadInfo> list = this.downloadInfoPack.getList();
        if ((list instanceof Collection) && ((Collection) list).isEmpty()) {
            return false;
        }
        for (DownloadInfo downloadInfo : list) {
            Object obj;
            if (downloadInfo.getStatus() == 2 || downloadInfo.getStatus() == 1) {
                obj = 1;
                continue;
            } else {
                obj = null;
                continue;
            }
            if (obj != null) {
                return true;
            }
        }
        return false;
    }

    private final void updateItem(DownloadInfo downloadInfo) {
        int size = this.downloadInfoPack.getList().size();
        int i = 0;
        while (i < size) {
            Object obj = this.downloadInfoPack.getList().get(i);
            Intrinsics.checkExpressionValueIsNotNull(obj, "downloadInfoPack.list[i]");
            if (Intrinsics.areEqual(((DownloadInfo) obj).getRowId(), downloadInfo.getRowId())) {
                break;
            }
            i++;
        }
        i = -1;
        if (i == -1) {
            this.downloadInfoPack.getList().add(0, downloadInfo);
        } else {
            this.downloadInfoPack.getList().remove(i);
            this.downloadInfoPack.getList().add(i, downloadInfo);
        }
        this.downloadInfoPack.setNotifyType(1);
        this.downloadInfoObservable.setValue(this.downloadInfoPack);
    }

    public final void loadMore(boolean z) {
        if (!this.isLoading) {
            this.isLoading = true;
            if (z) {
                this.isLastPage = false;
                this.isOpening = false;
                this.itemCount = 0;
                this.downloadInfoPack.getList().clear();
            }
            if (this.isLastPage) {
                this.isLoading = false;
            } else {
                this.repository.loadData(this.itemCount, 20, new DownloadInfoViewModel$loadMore$1(this));
            }
        }
    }

    public final void cancel(long j) {
        this.repository.queryByRowId(j, new DownloadInfoViewModel$cancel$1(this, j));
    }

    public final void remove(long j) {
        this.repository.remove(j);
        hide(j);
    }

    public final void delete(long j) {
        this.repository.queryByRowId(j, new DownloadInfoViewModel$delete$1(this));
    }

    public final void confirmDelete(DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "download");
        URI create = URI.create(downloadInfo.getFileUri());
        Intrinsics.checkExpressionValueIsNotNull(create, "URI.create(download.fileUri)");
        try {
            if (new File(create.getPath()).delete()) {
                DownloadInfoRepository downloadInfoRepository = this.repository;
                Long downloadId = downloadInfo.getDownloadId();
                Intrinsics.checkExpressionValueIsNotNull(downloadId, "download.downloadId");
                downloadInfoRepository.deleteFromDownloadManager(downloadId.longValue());
                downloadInfoRepository = this.repository;
                Long rowId = downloadInfo.getRowId();
                Intrinsics.checkExpressionValueIsNotNull(rowId, "download.rowId");
                downloadInfoRepository.remove(rowId.longValue());
                return;
            }
            this.toastMessageObservable.setValue(Integer.valueOf(C0769R.string.cannot_delete_the_file));
        } catch (Exception e) {
            String simpleName = getClass().getSimpleName();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(e.getMessage());
            Log.e(simpleName, stringBuilder.toString());
            this.toastMessageObservable.setValue(Integer.valueOf(C0769R.string.cannot_delete_the_file));
        }
    }

    public final void add(DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "downloadInfo");
        int size = this.downloadInfoPack.getList().size();
        int i = 0;
        while (i < size) {
            Object obj = this.downloadInfoPack.getList().get(i);
            Intrinsics.checkExpressionValueIsNotNull(obj, "downloadInfoPack.list[i]");
            long longValue = ((DownloadInfo) obj).getRowId().longValue();
            Long rowId = downloadInfo.getRowId();
            Intrinsics.checkExpressionValueIsNotNull(rowId, "downloadInfo.rowId");
            if (longValue < rowId.longValue()) {
                break;
            }
            i++;
        }
        i = -1;
        if (i == -1) {
            this.downloadInfoPack.getList().add(downloadInfo);
            if (this.downloadInfoPack.getList().size() > 1) {
                this.downloadInfoPack.setNotifyType(2);
                this.downloadInfoPack.setIndex((long) (this.downloadInfoPack.getList().size() - 1));
            } else {
                this.downloadInfoPack.setNotifyType(1);
            }
        } else {
            this.downloadInfoPack.getList().add(i, downloadInfo);
            this.downloadInfoPack.setNotifyType(2);
            this.downloadInfoPack.setIndex((long) i);
        }
        this.downloadInfoObservable.setValue(this.downloadInfoPack);
    }

    public final void hide(long j) {
        int size = this.downloadInfoPack.getList().size();
        for (int i = 0; i < size; i++) {
            Object obj = this.downloadInfoPack.getList().get(i);
            Intrinsics.checkExpressionValueIsNotNull(obj, "downloadInfoPack.list[i]");
            DownloadInfo downloadInfo = (DownloadInfo) obj;
            Long rowId = downloadInfo.getRowId();
            if (rowId != null && j == rowId.longValue()) {
                this.downloadInfoPack.getList().remove(downloadInfo);
                this.downloadInfoPack.setNotifyType(3);
                this.downloadInfoPack.setIndex((long) i);
                this.downloadInfoObservable.setValue(this.downloadInfoPack);
                return;
            }
        }
    }

    private final void updateRunningItems() {
        if ((getRunningDownloadIds().length == 0 ? 1 : null) == null) {
            for (long queryByDownloadId : getRunningDownloadIds()) {
                this.repository.queryByDownloadId(queryByDownloadId, new DownloadInfoViewModel$updateRunningItems$1(this));
            }
        }
    }

    public final void notifyDownloadComplete(long j) {
        this.repository.queryByDownloadId(j, this.updateListener);
    }

    public final void notifyRowUpdate(long j) {
        this.repository.queryByRowId(j, this.updateListener);
    }

    public final void queryDownloadProgress() {
        this.repository.queryDownloadingItems(getRunningDownloadIds(), new DownloadInfoViewModel$queryDownloadProgress$1(this));
    }

    public final void markAllItemsAreRead() {
        this.repository.markAllItemsAreRead();
    }

    public final void registerForProgressUpdate(OnProgressUpdateListener onProgressUpdateListener) {
        Intrinsics.checkParameterIsNotNull(onProgressUpdateListener, "listener");
        this.progressUpdateListener = onProgressUpdateListener;
        if (isDownloading()) {
            onProgressUpdateListener = this.progressUpdateListener;
            if (onProgressUpdateListener != null) {
                onProgressUpdateListener.onStartUpdate();
            }
        }
        updateRunningItems();
    }

    public final void unregisterForProgressUpdate() {
        OnProgressUpdateListener onProgressUpdateListener = this.progressUpdateListener;
        if (onProgressUpdateListener != null) {
            onProgressUpdateListener.onStopUpdate();
        }
        this.progressUpdateListener = (OnProgressUpdateListener) null;
    }
}
