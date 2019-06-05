// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.download;

import kotlin.jvm.internal.Intrinsics;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public final class DownloadIndicatorViewModel extends ViewModel
{
    private final MutableLiveData<Status> downloadIndicatorObservable;
    private final DownloadInfoRepository repository;
    
    public DownloadIndicatorViewModel(final DownloadInfoRepository repository) {
        Intrinsics.checkParameterIsNotNull(repository, "repository");
        this.repository = repository;
        this.downloadIndicatorObservable = new MutableLiveData<Status>();
    }
    
    public final MutableLiveData<Status> getDownloadIndicatorObservable() {
        return this.downloadIndicatorObservable;
    }
    
    public final void updateIndicator() {
        this.repository.queryIndicatorStatus((DownloadInfoRepository.OnQueryListCompleteListener)new DownloadIndicatorViewModel$updateIndicator.DownloadIndicatorViewModel$updateIndicator$1(this));
    }
    
    public enum Status
    {
        DEFAULT, 
        DOWNLOADING, 
        UNREAD, 
        WARNING;
    }
}
