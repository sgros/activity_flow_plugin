package org.mozilla.rocket.urlinput;

import android.arch.lifecycle.LiveData;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: QuickSearchRepository.kt */
public final class QuickSearchRepository {
    public static final Companion Companion = new Companion();
    private static volatile QuickSearchRepository INSTANCE;
    private final QuickSearchDataSource globalDataSource;
    private final QuickSearchDataSource localeDataSource;

    /* compiled from: QuickSearchRepository.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final QuickSearchRepository getInstance(QuickSearchDataSource quickSearchDataSource, QuickSearchDataSource quickSearchDataSource2) {
            Intrinsics.checkParameterIsNotNull(quickSearchDataSource, "globalDataSource");
            Intrinsics.checkParameterIsNotNull(quickSearchDataSource2, "localeDataSource");
            QuickSearchRepository access$getINSTANCE$cp = QuickSearchRepository.INSTANCE;
            if (access$getINSTANCE$cp == null) {
                synchronized (this) {
                    access$getINSTANCE$cp = QuickSearchRepository.INSTANCE;
                    if (access$getINSTANCE$cp == null) {
                        access$getINSTANCE$cp = new QuickSearchRepository(quickSearchDataSource, quickSearchDataSource2);
                        QuickSearchRepository.INSTANCE = access$getINSTANCE$cp;
                    }
                }
            }
            return access$getINSTANCE$cp;
        }
    }

    public static final QuickSearchRepository getInstance(QuickSearchDataSource quickSearchDataSource, QuickSearchDataSource quickSearchDataSource2) {
        return Companion.getInstance(quickSearchDataSource, quickSearchDataSource2);
    }

    public QuickSearchRepository(QuickSearchDataSource quickSearchDataSource, QuickSearchDataSource quickSearchDataSource2) {
        Intrinsics.checkParameterIsNotNull(quickSearchDataSource, "globalDataSource");
        Intrinsics.checkParameterIsNotNull(quickSearchDataSource2, "localeDataSource");
        this.globalDataSource = quickSearchDataSource;
        this.localeDataSource = quickSearchDataSource2;
    }

    public final LiveData<List<QuickSearch>> fetchGlobal() {
        return this.globalDataSource.fetchEngines();
    }

    public final LiveData<List<QuickSearch>> fetchLocale() {
        return this.localeDataSource.fetchEngines();
    }
}
