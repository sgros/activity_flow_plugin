package org.mozilla.rocket.privately;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SharedViewModel.kt */
public final class SharedViewModel extends ViewModel {
    private MutableLiveData<Boolean> isUrlInputShowing = new MutableLiveData();
    private final MutableLiveData<String> url = new MutableLiveData();

    public final MutableLiveData<Boolean> urlInputState() {
        return this.isUrlInputShowing;
    }

    public final void setUrl(String str) {
        Intrinsics.checkParameterIsNotNull(str, "newUrl");
        this.url.setValue(str);
    }

    public final LiveData<String> getUrl() {
        return this.url;
    }
}
