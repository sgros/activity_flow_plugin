// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.urlinput;

import kotlin.jvm.internal.Intrinsics;
import org.mozilla.threadutils.ThreadUtils;
import java.util.List;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

public final class QuickSearchUtils
{
    public static final QuickSearchUtils INSTANCE;
    
    static {
        INSTANCE = new QuickSearchUtils();
    }
    
    private QuickSearchUtils() {
    }
    
    private final void loadEnginesFromAssets(final Context context, final int n, final MutableLiveData<List<QuickSearch>> mutableLiveData) {
        ThreadUtils.postToBackgroundThread((Runnable)new QuickSearchUtils$loadEnginesFromAssets.QuickSearchUtils$loadEnginesFromAssets$1(context, n, (MutableLiveData)mutableLiveData));
    }
    
    public final void loadDefaultEngines$app_focusWebkitRelease(final Context context, final MutableLiveData<List<QuickSearch>> mutableLiveData) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(mutableLiveData, "liveData");
        this.loadEnginesFromAssets(context, 2131689483, mutableLiveData);
    }
    
    public final void loadEnginesByLocale$app_focusWebkitRelease(final Context context, final MutableLiveData<List<QuickSearch>> mutableLiveData) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(mutableLiveData, "liveData");
        this.loadEnginesFromAssets(context, 2131689482, mutableLiveData);
    }
}
