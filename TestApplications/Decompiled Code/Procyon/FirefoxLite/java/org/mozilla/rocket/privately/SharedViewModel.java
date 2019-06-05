// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.privately;

import kotlin.jvm.internal.Intrinsics;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public final class SharedViewModel extends ViewModel
{
    private MutableLiveData<Boolean> isUrlInputShowing;
    private final MutableLiveData<String> url;
    
    public SharedViewModel() {
        this.isUrlInputShowing = new MutableLiveData<Boolean>();
        this.url = new MutableLiveData<String>();
    }
    
    public final LiveData<String> getUrl() {
        return this.url;
    }
    
    public final void setUrl(final String value) {
        Intrinsics.checkParameterIsNotNull(value, "newUrl");
        this.url.setValue(value);
    }
    
    public final MutableLiveData<Boolean> urlInputState() {
        return this.isUrlInputShowing;
    }
}
