package org.mozilla.rocket.download;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: DownloadIndicatorViewModel.kt */
public final class DownloadIndicatorViewModel extends ViewModel {
    private final MutableLiveData<Status> downloadIndicatorObservable = new MutableLiveData();
    private final DownloadInfoRepository repository;

    /* compiled from: DownloadIndicatorViewModel.kt */
    public enum Status {
        DEFAULT,
        DOWNLOADING,
        UNREAD,
        WARNING
    }

    public DownloadIndicatorViewModel(DownloadInfoRepository downloadInfoRepository) {
        Intrinsics.checkParameterIsNotNull(downloadInfoRepository, "repository");
        this.repository = downloadInfoRepository;
    }

    public final MutableLiveData<Status> getDownloadIndicatorObservable() {
        return this.downloadIndicatorObservable;
    }

    public final void updateIndicator() {
        this.repository.queryIndicatorStatus(new DownloadIndicatorViewModel$updateIndicator$1(this));
    }
}
