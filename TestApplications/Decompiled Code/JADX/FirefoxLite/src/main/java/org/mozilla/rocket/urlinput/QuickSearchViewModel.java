package org.mozilla.rocket.urlinput;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: QuickSearchViewModel.kt */
public final class QuickSearchViewModel extends ViewModel {
    private final LiveData<List<QuickSearch>> liveGlobal;
    private final LiveData<List<QuickSearch>> liveLocale;
    private final MediatorLiveData<ArrayList<QuickSearch>> quickSearchObservable = new MediatorLiveData();

    /* compiled from: QuickSearchViewModel.kt */
    /* renamed from: org.mozilla.rocket.urlinput.QuickSearchViewModel$1 */
    static final class C07551<T> implements Observer<S> {
        final /* synthetic */ QuickSearchViewModel this$0;

        C07551(QuickSearchViewModel quickSearchViewModel) {
            this.this$0 = quickSearchViewModel;
        }

        public final void onChanged(List<QuickSearch> list) {
            this.this$0.mergeEngines();
        }
    }

    /* compiled from: QuickSearchViewModel.kt */
    /* renamed from: org.mozilla.rocket.urlinput.QuickSearchViewModel$2 */
    static final class C07562<T> implements Observer<S> {
        final /* synthetic */ QuickSearchViewModel this$0;

        C07562(QuickSearchViewModel quickSearchViewModel) {
            this.this$0 = quickSearchViewModel;
        }

        public final void onChanged(List<QuickSearch> list) {
            this.this$0.mergeEngines();
        }
    }

    public QuickSearchViewModel(QuickSearchRepository quickSearchRepository) {
        Intrinsics.checkParameterIsNotNull(quickSearchRepository, "repository");
        this.liveGlobal = quickSearchRepository.fetchGlobal();
        this.liveLocale = quickSearchRepository.fetchLocale();
        this.quickSearchObservable.addSource(this.liveGlobal, new C07551(this));
        this.quickSearchObservable.addSource(this.liveLocale, new C07562(this));
    }

    public final MediatorLiveData<ArrayList<QuickSearch>> getQuickSearchObservable() {
        return this.quickSearchObservable;
    }

    private final void mergeEngines() {
        ArrayList arrayList = new ArrayList();
        List list = (List) this.liveGlobal.getValue();
        if (list != null) {
            arrayList.addAll(list);
        }
        list = (List) this.liveLocale.getValue();
        if (list != null) {
            arrayList.addAll(list);
        }
        this.quickSearchObservable.setValue(arrayList);
    }
}
