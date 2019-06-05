package org.mozilla.rocket.urlinput;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: GlobalDataSource.kt */
public final class GlobalDataSource implements QuickSearchDataSource {
    public static final Companion Companion = new Companion();
    @SuppressLint({"StaticFieldLeak"})
    private static volatile GlobalDataSource INSTANCE;
    private Context context;

    /* compiled from: GlobalDataSource.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final GlobalDataSource getInstance(Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            GlobalDataSource access$getINSTANCE$cp = GlobalDataSource.INSTANCE;
            if (access$getINSTANCE$cp == null) {
                synchronized (this) {
                    access$getINSTANCE$cp = GlobalDataSource.INSTANCE;
                    if (access$getINSTANCE$cp == null) {
                        access$getINSTANCE$cp = new GlobalDataSource();
                        GlobalDataSource.INSTANCE = access$getINSTANCE$cp;
                        context = context.getApplicationContext();
                        Intrinsics.checkExpressionValueIsNotNull(context, "context.applicationContext");
                        access$getINSTANCE$cp.context = context;
                    }
                }
            }
            return access$getINSTANCE$cp;
        }
    }

    public static final GlobalDataSource getInstance(Context context) {
        return Companion.getInstance(context);
    }

    public LiveData<List<QuickSearch>> fetchEngines() {
        MutableLiveData mutableLiveData = new MutableLiveData();
        QuickSearchUtils quickSearchUtils = QuickSearchUtils.INSTANCE;
        Context context = this.context;
        if (context == null) {
            Intrinsics.throwUninitializedPropertyAccessException("context");
        }
        quickSearchUtils.loadDefaultEngines$app_focusWebkitRelease(context, mutableLiveData);
        return mutableLiveData;
    }
}
