// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.urlinput;

import java.util.List;
import android.arch.lifecycle.LiveData;
import kotlin.jvm.internal.Intrinsics;

public final class QuickSearchRepository
{
    public static final Companion Companion;
    private static volatile QuickSearchRepository INSTANCE;
    private final QuickSearchDataSource globalDataSource;
    private final QuickSearchDataSource localeDataSource;
    
    static {
        Companion = new Companion(null);
    }
    
    public QuickSearchRepository(final QuickSearchDataSource globalDataSource, final QuickSearchDataSource localeDataSource) {
        Intrinsics.checkParameterIsNotNull(globalDataSource, "globalDataSource");
        Intrinsics.checkParameterIsNotNull(localeDataSource, "localeDataSource");
        this.globalDataSource = globalDataSource;
        this.localeDataSource = localeDataSource;
    }
    
    public static final /* synthetic */ QuickSearchRepository access$getINSTANCE$cp() {
        return QuickSearchRepository.INSTANCE;
    }
    
    public static final /* synthetic */ void access$setINSTANCE$cp(final QuickSearchRepository instance) {
        QuickSearchRepository.INSTANCE = instance;
    }
    
    public static final QuickSearchRepository getInstance(final QuickSearchDataSource quickSearchDataSource, final QuickSearchDataSource quickSearchDataSource2) {
        return QuickSearchRepository.Companion.getInstance(quickSearchDataSource, quickSearchDataSource2);
    }
    
    public final LiveData<List<QuickSearch>> fetchGlobal() {
        return this.globalDataSource.fetchEngines();
    }
    
    public final LiveData<List<QuickSearch>> fetchLocale() {
        return this.localeDataSource.fetchEngines();
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        public final QuickSearchRepository getInstance(final QuickSearchDataSource quickSearchDataSource, final QuickSearchDataSource quickSearchDataSource2) {
            Intrinsics.checkParameterIsNotNull(quickSearchDataSource, "globalDataSource");
            Intrinsics.checkParameterIsNotNull(quickSearchDataSource2, "localeDataSource");
            final QuickSearchRepository access$getINSTANCE$cp = QuickSearchRepository.access$getINSTANCE$cp();
            if (access$getINSTANCE$cp != null) {
                return access$getINSTANCE$cp;
            }
            synchronized (this) {
                final QuickSearchRepository access$getINSTANCE$cp2 = QuickSearchRepository.access$getINSTANCE$cp();
                QuickSearchRepository quickSearchRepository;
                if (access$getINSTANCE$cp2 != null) {
                    quickSearchRepository = access$getINSTANCE$cp2;
                }
                else {
                    final QuickSearchRepository quickSearchRepository2 = new QuickSearchRepository(quickSearchDataSource, quickSearchDataSource2);
                    QuickSearchRepository.access$setINSTANCE$cp(quickSearchRepository2);
                    quickSearchRepository = quickSearchRepository2;
                }
                return quickSearchRepository;
            }
        }
    }
}
