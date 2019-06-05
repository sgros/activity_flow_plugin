// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.urlinput;

import kotlin.jvm.internal.Intrinsics;
import android.arch.lifecycle.MutableLiveData;
import java.util.List;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.annotation.SuppressLint;

public final class LocaleDataSource implements QuickSearchDataSource
{
    public static final Companion Companion;
    @SuppressLint({ "StaticFieldLeak" })
    private static volatile LocaleDataSource INSTANCE;
    private Context context;
    
    static {
        Companion = new Companion(null);
    }
    
    public static final /* synthetic */ LocaleDataSource access$getINSTANCE$cp() {
        return LocaleDataSource.INSTANCE;
    }
    
    public static final /* synthetic */ void access$setContext$p(final LocaleDataSource localeDataSource, final Context context) {
        localeDataSource.context = context;
    }
    
    public static final /* synthetic */ void access$setINSTANCE$cp(final LocaleDataSource instance) {
        LocaleDataSource.INSTANCE = instance;
    }
    
    public static final LocaleDataSource getInstance(final Context context) {
        return LocaleDataSource.Companion.getInstance(context);
    }
    
    @Override
    public LiveData<List<QuickSearch>> fetchEngines() {
        final MutableLiveData<List<QuickSearch>> mutableLiveData = new MutableLiveData<List<QuickSearch>>();
        final QuickSearchUtils instance = QuickSearchUtils.INSTANCE;
        final Context context = this.context;
        if (context == null) {
            Intrinsics.throwUninitializedPropertyAccessException("context");
        }
        instance.loadEnginesByLocale$app_focusWebkitRelease(context, mutableLiveData);
        return mutableLiveData;
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        public final LocaleDataSource getInstance(Context applicationContext) {
            Intrinsics.checkParameterIsNotNull(applicationContext, "context");
            final LocaleDataSource access$getINSTANCE$cp = LocaleDataSource.access$getINSTANCE$cp();
            if (access$getINSTANCE$cp != null) {
                return access$getINSTANCE$cp;
            }
            synchronized (this) {
                final LocaleDataSource access$getINSTANCE$cp2 = LocaleDataSource.access$getINSTANCE$cp();
                LocaleDataSource localeDataSource;
                if (access$getINSTANCE$cp2 != null) {
                    localeDataSource = access$getINSTANCE$cp2;
                }
                else {
                    final LocaleDataSource localeDataSource2 = new LocaleDataSource();
                    LocaleDataSource.access$setINSTANCE$cp(localeDataSource2);
                    applicationContext = applicationContext.getApplicationContext();
                    Intrinsics.checkExpressionValueIsNotNull(applicationContext, "context.applicationContext");
                    LocaleDataSource.access$setContext$p(localeDataSource2, applicationContext);
                    localeDataSource = localeDataSource2;
                }
                return localeDataSource;
            }
        }
    }
}
