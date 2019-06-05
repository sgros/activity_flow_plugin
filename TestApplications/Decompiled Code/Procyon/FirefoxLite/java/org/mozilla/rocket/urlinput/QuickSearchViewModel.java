// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.urlinput;

import java.util.Collection;
import android.arch.lifecycle.Observer;
import kotlin.jvm.internal.Intrinsics;
import java.util.ArrayList;
import android.arch.lifecycle.MediatorLiveData;
import java.util.List;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public final class QuickSearchViewModel extends ViewModel
{
    private final LiveData<List<QuickSearch>> liveGlobal;
    private final LiveData<List<QuickSearch>> liveLocale;
    private final MediatorLiveData<ArrayList<QuickSearch>> quickSearchObservable;
    
    public QuickSearchViewModel(final QuickSearchRepository quickSearchRepository) {
        Intrinsics.checkParameterIsNotNull(quickSearchRepository, "repository");
        this.quickSearchObservable = new MediatorLiveData<ArrayList<QuickSearch>>();
        this.liveGlobal = quickSearchRepository.fetchGlobal();
        this.liveLocale = quickSearchRepository.fetchLocale();
        this.quickSearchObservable.addSource(this.liveGlobal, new Observer<Object>() {
            public final void onChanged(final List<QuickSearch> list) {
                QuickSearchViewModel.this.mergeEngines();
            }
        });
        this.quickSearchObservable.addSource(this.liveLocale, new Observer<Object>() {
            public final void onChanged(final List<QuickSearch> list) {
                QuickSearchViewModel.this.mergeEngines();
            }
        });
    }
    
    private final void mergeEngines() {
        final ArrayList<QuickSearch> value = new ArrayList<QuickSearch>();
        final List<QuickSearch> list = this.liveGlobal.getValue();
        if (list != null) {
            value.addAll(list);
        }
        final List<QuickSearch> list2 = this.liveLocale.getValue();
        if (list2 != null) {
            value.addAll(list2);
        }
        this.quickSearchObservable.setValue(value);
    }
    
    public final MediatorLiveData<ArrayList<QuickSearch>> getQuickSearchObservable() {
        return this.quickSearchObservable;
    }
}
