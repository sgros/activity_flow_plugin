package org.mozilla.rocket.urlinput;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.rocket.C0769R;
import org.mozilla.threadutils.ThreadUtils;

/* compiled from: QuickSearchUtils.kt */
public final class QuickSearchUtils {
    public static final QuickSearchUtils INSTANCE = new QuickSearchUtils();

    private QuickSearchUtils() {
    }

    public final void loadDefaultEngines$app_focusWebkitRelease(Context context, MutableLiveData<List<QuickSearch>> mutableLiveData) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(mutableLiveData, "liveData");
        loadEnginesFromAssets(context, C0769R.raw.quick_search_engines_common, mutableLiveData);
    }

    public final void loadEnginesByLocale$app_focusWebkitRelease(Context context, MutableLiveData<List<QuickSearch>> mutableLiveData) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(mutableLiveData, "liveData");
        loadEnginesFromAssets(context, C0769R.raw.quick_search_engines, mutableLiveData);
    }

    private final void loadEnginesFromAssets(Context context, int i, MutableLiveData<List<QuickSearch>> mutableLiveData) {
        ThreadUtils.postToBackgroundThread((Runnable) new QuickSearchUtils$loadEnginesFromAssets$1(context, i, mutableLiveData));
    }
}
