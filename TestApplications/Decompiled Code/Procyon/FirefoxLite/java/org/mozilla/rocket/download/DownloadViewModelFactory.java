// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.download;

import org.mozilla.focus.Inject;
import kotlin.jvm.internal.Intrinsics;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public final class DownloadViewModelFactory extends NewInstanceFactory
{
    public static final Companion Companion;
    private static volatile DownloadViewModelFactory INSTANCE;
    private final DownloadInfoRepository repository;
    
    static {
        Companion = new Companion(null);
    }
    
    private DownloadViewModelFactory(final DownloadInfoRepository repository) {
        this.repository = repository;
    }
    
    public static final /* synthetic */ DownloadViewModelFactory access$getINSTANCE$cp() {
        return DownloadViewModelFactory.INSTANCE;
    }
    
    public static final /* synthetic */ void access$setINSTANCE$cp(final DownloadViewModelFactory instance) {
        DownloadViewModelFactory.INSTANCE = instance;
    }
    
    public static final DownloadViewModelFactory getInstance() {
        return DownloadViewModelFactory.Companion.getInstance();
    }
    
    @Override
    public <T extends ViewModel> T create(final Class<T> clazz) {
        Intrinsics.checkParameterIsNotNull(clazz, "modelClass");
        if (clazz.isAssignableFrom(DownloadIndicatorViewModel.class)) {
            return (T)new DownloadIndicatorViewModel(this.repository);
        }
        if (clazz.isAssignableFrom(DownloadInfoViewModel.class)) {
            return (T)new DownloadInfoViewModel(this.repository);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown ViewModel class: ");
        sb.append(clazz.getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        public final DownloadViewModelFactory getInstance() {
            DownloadViewModelFactory downloadViewModelFactory = DownloadViewModelFactory.access$getINSTANCE$cp();
            if (downloadViewModelFactory != null) {
                return downloadViewModelFactory;
            }
            synchronized (this) {
                downloadViewModelFactory = DownloadViewModelFactory.access$getINSTANCE$cp();
                if (downloadViewModelFactory == null) {
                    downloadViewModelFactory = new(org.mozilla.rocket.download.DownloadViewModelFactory.class);
                    final DownloadInfoRepository provideDownloadInfoRepository = Inject.provideDownloadInfoRepository();
                    Intrinsics.checkExpressionValueIsNotNull(provideDownloadInfoRepository, "Inject.provideDownloadInfoRepository()");
                    new DownloadViewModelFactory(provideDownloadInfoRepository, null);
                    DownloadViewModelFactory.access$setINSTANCE$cp(downloadViewModelFactory);
                }
                return downloadViewModelFactory;
            }
        }
    }
}
