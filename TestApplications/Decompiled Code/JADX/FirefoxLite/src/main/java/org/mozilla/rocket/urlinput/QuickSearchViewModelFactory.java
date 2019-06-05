package org.mozilla.rocket.urlinput;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider.NewInstanceFactory;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: QuickSearchViewModelFactory.kt */
public final class QuickSearchViewModelFactory extends NewInstanceFactory {
    private final QuickSearchRepository repository;

    public QuickSearchViewModelFactory(QuickSearchRepository quickSearchRepository) {
        Intrinsics.checkParameterIsNotNull(quickSearchRepository, "repository");
        this.repository = quickSearchRepository;
    }

    public <T extends ViewModel> T create(Class<T> cls) {
        Intrinsics.checkParameterIsNotNull(cls, "modelClass");
        return new QuickSearchViewModel(this.repository);
    }
}
