package org.mozilla.rocket.urlinput;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: LocaleDataSource.kt */
public final class LocaleDataSource implements QuickSearchDataSource {
    public static final Companion Companion = new Companion();
    @SuppressLint({"StaticFieldLeak"})
    private static volatile LocaleDataSource INSTANCE;
    private Context context;

    /* compiled from: LocaleDataSource.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final LocaleDataSource getInstance(Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            LocaleDataSource access$getINSTANCE$cp = LocaleDataSource.INSTANCE;
            if (access$getINSTANCE$cp == null) {
                synchronized (this) {
                    access$getINSTANCE$cp = LocaleDataSource.INSTANCE;
                    if (access$getINSTANCE$cp == null) {
                        access$getINSTANCE$cp = new LocaleDataSource();
                        LocaleDataSource.INSTANCE = access$getINSTANCE$cp;
                        context = context.getApplicationContext();
                        Intrinsics.checkExpressionValueIsNotNull(context, "context.applicationContext");
                        access$getINSTANCE$cp.context = context;
                    }
                }
            }
            return access$getINSTANCE$cp;
        }
    }

    public static final LocaleDataSource getInstance(Context context) {
        return Companion.getInstance(context);
    }

    public LiveData<List<QuickSearch>> fetchEngines() {
        MutableLiveData mutableLiveData = new MutableLiveData();
        QuickSearchUtils quickSearchUtils = QuickSearchUtils.INSTANCE;
        Context context = this.context;
        if (context == null) {
            Intrinsics.throwUninitializedPropertyAccessException("context");
        }
        quickSearchUtils.loadEnginesByLocale$app_focusWebkitRelease(context, mutableLiveData);
        return mutableLiveData;
    }
}
