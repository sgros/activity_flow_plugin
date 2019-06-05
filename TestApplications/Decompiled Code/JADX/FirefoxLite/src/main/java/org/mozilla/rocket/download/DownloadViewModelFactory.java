package org.mozilla.rocket.download;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider.NewInstanceFactory;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.Inject;

/* compiled from: DownloadViewModelFactory.kt */
public final class DownloadViewModelFactory extends NewInstanceFactory {
    public static final Companion Companion = new Companion();
    private static volatile DownloadViewModelFactory INSTANCE;
    private final DownloadInfoRepository repository;

    /* compiled from: DownloadViewModelFactory.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final DownloadViewModelFactory getInstance() {
            DownloadViewModelFactory access$getINSTANCE$cp = DownloadViewModelFactory.INSTANCE;
            if (access$getINSTANCE$cp == null) {
                synchronized (this) {
                    access$getINSTANCE$cp = DownloadViewModelFactory.INSTANCE;
                    if (access$getINSTANCE$cp == null) {
                        DownloadInfoRepository provideDownloadInfoRepository = Inject.provideDownloadInfoRepository();
                        Intrinsics.checkExpressionValueIsNotNull(provideDownloadInfoRepository, "Inject.provideDownloadInfoRepository()");
                        access$getINSTANCE$cp = new DownloadViewModelFactory(provideDownloadInfoRepository, null);
                        DownloadViewModelFactory.INSTANCE = access$getINSTANCE$cp;
                    }
                }
            }
            return access$getINSTANCE$cp;
        }
    }

    public static final DownloadViewModelFactory getInstance() {
        return Companion.getInstance();
    }

    private DownloadViewModelFactory(DownloadInfoRepository downloadInfoRepository) {
        this.repository = downloadInfoRepository;
    }

    public /* synthetic */ DownloadViewModelFactory(DownloadInfoRepository downloadInfoRepository, DefaultConstructorMarker defaultConstructorMarker) {
        this(downloadInfoRepository);
    }

    public <T extends ViewModel> T create(Class<T> cls) {
        Intrinsics.checkParameterIsNotNull(cls, "modelClass");
        if (cls.isAssignableFrom(DownloadIndicatorViewModel.class)) {
            return new DownloadIndicatorViewModel(this.repository);
        }
        if (cls.isAssignableFrom(DownloadInfoViewModel.class)) {
            return new DownloadInfoViewModel(this.repository);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown ViewModel class: ");
        stringBuilder.append(cls.getName());
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
