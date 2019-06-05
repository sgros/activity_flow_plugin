// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.download;

import android.util.Log;
import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import android.arch.lifecycle.MutableLiveData;
import org.mozilla.focus.download.DownloadInfo;
import android.arch.lifecycle.ViewModel;

public final class DownloadInfoViewModel extends ViewModel
{
    public static final Companion Companion;
    private final SingleLiveEvent<DownloadInfo> deleteSnackbarObservable;
    private final MutableLiveData<DownloadInfoPack> downloadInfoObservable;
    private final DownloadInfoPack downloadInfoPack;
    private boolean isLastPage;
    private boolean isLoading;
    private boolean isOpening;
    private int itemCount;
    private OnProgressUpdateListener progressUpdateListener;
    private final DownloadInfoRepository repository;
    private final SingleLiveEvent<Integer> toastMessageObservable;
    private DownloadInfoRepository.OnQueryItemCompleteListener updateListener;
    
    static {
        Companion = new Companion(null);
    }
    
    public DownloadInfoViewModel(final DownloadInfoRepository repository) {
        Intrinsics.checkParameterIsNotNull(repository, "repository");
        this.repository = repository;
        this.downloadInfoObservable = new MutableLiveData<DownloadInfoPack>();
        this.toastMessageObservable = new SingleLiveEvent<Integer>();
        this.deleteSnackbarObservable = new SingleLiveEvent<DownloadInfo>();
        this.downloadInfoPack = new DownloadInfoPack(new ArrayList<DownloadInfo>(), -1, -1L);
        this.updateListener = (DownloadInfoRepository.OnQueryItemCompleteListener)new DownloadInfoViewModel$updateListener.DownloadInfoViewModel$updateListener$1(this);
    }
    
    private final long[] getRunningDownloadIds() {
        final ArrayList<DownloadInfo> list = this.downloadInfoPack.getList();
        final ArrayList<DownloadInfo> list2 = new ArrayList<DownloadInfo>();
        final Iterator<Object> iterator = list.iterator();
        int i;
        while (true) {
            final boolean hasNext = iterator.hasNext();
            i = 0;
            final int n = 0;
            if (!hasNext) {
                break;
            }
            final DownloadInfo next = iterator.next();
            final DownloadInfo downloadInfo = next;
            int n2 = 0;
            Label_0085: {
                if (downloadInfo.getStatus() != 2) {
                    n2 = n;
                    if (downloadInfo.getStatus() != 1) {
                        break Label_0085;
                    }
                }
                n2 = 1;
            }
            if (n2 == 0) {
                continue;
            }
            list2.add(next);
        }
        final ArrayList<DownloadInfo> list3 = list2;
        final ArrayList<Long> list4 = new ArrayList<Long>(CollectionsKt__IterablesKt.collectionSizeOrDefault((Iterable<?>)list3, 10));
        final Iterator<Object> iterator2 = list3.iterator();
        while (iterator2.hasNext()) {
            list4.add(iterator2.next().getDownloadId());
        }
        final ArrayList<Long> list5 = list4;
        long[] array;
        for (array = new long[list5.size()]; i < array.length; ++i) {
            final Object value = list5.get(i);
            Intrinsics.checkExpressionValueIsNotNull(value, "ids[i]");
            array[i] = ((Long)value).longValue();
        }
        return array;
    }
    
    private final boolean isDownloading() {
        final ArrayList<DownloadInfo> list = this.downloadInfoPack.getList();
        final boolean b = list instanceof Collection;
        final boolean b2 = false;
        boolean b3;
        if (b && list.isEmpty()) {
            b3 = b2;
        }
        else {
            final Iterator<Object> iterator = list.iterator();
            DownloadInfo downloadInfo;
            do {
                b3 = b2;
                if (!iterator.hasNext()) {
                    return b3;
                }
                downloadInfo = iterator.next();
            } while (downloadInfo.getStatus() != 2 && downloadInfo.getStatus() != 1);
            b3 = true;
        }
        return b3;
    }
    
    private final void updateItem(final DownloadInfo downloadInfo) {
        while (true) {
            for (int size = this.downloadInfoPack.getList().size(), i = 0; i < size; ++i) {
                final DownloadInfo value = this.downloadInfoPack.getList().get(i);
                Intrinsics.checkExpressionValueIsNotNull(value, "downloadInfoPack.list[i]");
                if (Intrinsics.areEqual(value.getRowId(), downloadInfo.getRowId())) {
                    if (i == -1) {
                        this.downloadInfoPack.getList().add(0, downloadInfo);
                    }
                    else {
                        this.downloadInfoPack.getList().remove(i);
                        this.downloadInfoPack.getList().add(i, downloadInfo);
                    }
                    this.downloadInfoPack.setNotifyType(1);
                    this.downloadInfoObservable.setValue(this.downloadInfoPack);
                    return;
                }
            }
            int i = -1;
            continue;
        }
    }
    
    private final void updateRunningItems() {
        final int length = this.getRunningDownloadIds().length;
        final int n = 0;
        if (length != 0) {
            for (int length2 = this.getRunningDownloadIds().length, i = n; i < length2; ++i) {
                this.repository.queryByDownloadId(this.getRunningDownloadIds()[i], (DownloadInfoRepository.OnQueryItemCompleteListener)new DownloadInfoViewModel$updateRunningItems.DownloadInfoViewModel$updateRunningItems$1(this));
            }
        }
    }
    
    public final void add(final DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "downloadInfo");
        while (true) {
            for (int size = this.downloadInfoPack.getList().size(), i = 0; i < size; ++i) {
                final DownloadInfo value = this.downloadInfoPack.getList().get(i);
                Intrinsics.checkExpressionValueIsNotNull(value, "downloadInfoPack.list[i]");
                final long longValue = value.getRowId();
                final Long rowId = downloadInfo.getRowId();
                Intrinsics.checkExpressionValueIsNotNull(rowId, "downloadInfo.rowId");
                if (longValue < rowId) {
                    if (i == -1) {
                        this.downloadInfoPack.getList().add(downloadInfo);
                        if (this.downloadInfoPack.getList().size() > 1) {
                            this.downloadInfoPack.setNotifyType(2);
                            this.downloadInfoPack.setIndex(this.downloadInfoPack.getList().size() - 1);
                        }
                        else {
                            this.downloadInfoPack.setNotifyType(1);
                        }
                    }
                    else {
                        this.downloadInfoPack.getList().add(i, downloadInfo);
                        this.downloadInfoPack.setNotifyType(2);
                        this.downloadInfoPack.setIndex(i);
                    }
                    this.downloadInfoObservable.setValue(this.downloadInfoPack);
                    return;
                }
            }
            int i = -1;
            continue;
        }
    }
    
    public final void cancel(final long n) {
        this.repository.queryByRowId(n, (DownloadInfoRepository.OnQueryItemCompleteListener)new DownloadInfoViewModel$cancel.DownloadInfoViewModel$cancel$1(this, n));
    }
    
    public final void confirmDelete(final DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "download");
        final URI create = URI.create(downloadInfo.getFileUri());
        Intrinsics.checkExpressionValueIsNotNull(create, "URI.create(download.fileUri)");
        final File file = new File(create.getPath());
        try {
            if (file.delete()) {
                final DownloadInfoRepository repository = this.repository;
                final Long downloadId = downloadInfo.getDownloadId();
                Intrinsics.checkExpressionValueIsNotNull(downloadId, "download.downloadId");
                repository.deleteFromDownloadManager(downloadId);
                final DownloadInfoRepository repository2 = this.repository;
                final Long rowId = downloadInfo.getRowId();
                Intrinsics.checkExpressionValueIsNotNull(rowId, "download.rowId");
                repository2.remove(rowId);
            }
            else {
                this.toastMessageObservable.setValue(2131755081);
            }
        }
        catch (Exception ex) {
            final String simpleName = this.getClass().getSimpleName();
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(ex.getMessage());
            Log.e(simpleName, sb.toString());
            this.toastMessageObservable.setValue(2131755081);
        }
    }
    
    public final void delete(final long n) {
        this.repository.queryByRowId(n, (DownloadInfoRepository.OnQueryItemCompleteListener)new DownloadInfoViewModel$delete.DownloadInfoViewModel$delete$1(this));
    }
    
    public final SingleLiveEvent<DownloadInfo> getDeleteSnackbarObservable() {
        return this.deleteSnackbarObservable;
    }
    
    public final MutableLiveData<DownloadInfoPack> getDownloadInfoObservable() {
        return this.downloadInfoObservable;
    }
    
    public final SingleLiveEvent<Integer> getToastMessageObservable() {
        return this.toastMessageObservable;
    }
    
    public final void hide(final long n) {
        for (int size = this.downloadInfoPack.getList().size(), i = 0; i < size; ++i) {
            final DownloadInfo value = this.downloadInfoPack.getList().get(i);
            Intrinsics.checkExpressionValueIsNotNull(value, "downloadInfoPack.list[i]");
            final DownloadInfo o = value;
            final Long rowId = o.getRowId();
            if (rowId != null) {
                if (n == rowId) {
                    this.downloadInfoPack.getList().remove(o);
                    this.downloadInfoPack.setNotifyType(3);
                    this.downloadInfoPack.setIndex(i);
                    this.downloadInfoObservable.setValue(this.downloadInfoPack);
                    break;
                }
            }
        }
    }
    
    public final boolean isOpening() {
        return this.isOpening;
    }
    
    public final void loadMore(final boolean b) {
        if (this.isLoading) {
            return;
        }
        this.isLoading = true;
        if (b) {
            this.isLastPage = false;
            this.isOpening = false;
            this.itemCount = 0;
            this.downloadInfoPack.getList().clear();
        }
        if (this.isLastPage) {
            this.isLoading = false;
            return;
        }
        this.repository.loadData(this.itemCount, 20, (DownloadInfoRepository.OnQueryListCompleteListener)new DownloadInfoViewModel$loadMore.DownloadInfoViewModel$loadMore$1(this));
    }
    
    public final void markAllItemsAreRead() {
        this.repository.markAllItemsAreRead();
    }
    
    public final void notifyDownloadComplete(final long n) {
        this.repository.queryByDownloadId(n, this.updateListener);
    }
    
    public final void notifyRowUpdate(final long n) {
        this.repository.queryByRowId(n, this.updateListener);
    }
    
    public final void queryDownloadProgress() {
        this.repository.queryDownloadingItems(this.getRunningDownloadIds(), (DownloadInfoRepository.OnQueryListCompleteListener)new DownloadInfoViewModel$queryDownloadProgress.DownloadInfoViewModel$queryDownloadProgress$1(this));
    }
    
    public final void registerForProgressUpdate(OnProgressUpdateListener progressUpdateListener) {
        Intrinsics.checkParameterIsNotNull(progressUpdateListener, "listener");
        this.progressUpdateListener = progressUpdateListener;
        if (this.isDownloading()) {
            progressUpdateListener = this.progressUpdateListener;
            if (progressUpdateListener != null) {
                progressUpdateListener.onStartUpdate();
            }
        }
        this.updateRunningItems();
    }
    
    public final void remove(final long n) {
        this.repository.remove(n);
        this.hide(n);
    }
    
    public final void setOpening(final boolean isOpening) {
        this.isOpening = isOpening;
    }
    
    public final void unregisterForProgressUpdate() {
        final OnProgressUpdateListener progressUpdateListener = this.progressUpdateListener;
        if (progressUpdateListener != null) {
            progressUpdateListener.onStopUpdate();
        }
        this.progressUpdateListener = null;
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
    
    public interface OnProgressUpdateListener
    {
        void onCompleteUpdate();
        
        void onStartUpdate();
        
        void onStopUpdate();
    }
}
