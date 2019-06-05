// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.urlinput;

import android.arch.lifecycle.ViewModel;
import kotlin.jvm.internal.Intrinsics;
import android.arch.lifecycle.ViewModelProvider;

public final class QuickSearchViewModelFactory extends NewInstanceFactory
{
    private final QuickSearchRepository repository;
    
    public QuickSearchViewModelFactory(final QuickSearchRepository repository) {
        Intrinsics.checkParameterIsNotNull(repository, "repository");
        this.repository = repository;
    }
    
    @Override
    public <T extends ViewModel> T create(final Class<T> clazz) {
        Intrinsics.checkParameterIsNotNull(clazz, "modelClass");
        return (T)new QuickSearchViewModel(this.repository);
    }
}
