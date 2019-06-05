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

public final class GlobalDataSource implements QuickSearchDataSource
{
    public static final Companion Companion;
    @SuppressLint({ "StaticFieldLeak" })
    private static volatile GlobalDataSource INSTANCE;
    private Context context;
    
    static {
        Companion = new Companion(null);
    }
    
    public static final /* synthetic */ GlobalDataSource access$getINSTANCE$cp() {
        return GlobalDataSource.INSTANCE;
    }
    
    public static final /* synthetic */ void access$setContext$p(final GlobalDataSource globalDataSource, final Context context) {
        globalDataSource.context = context;
    }
    
    public static final /* synthetic */ void access$setINSTANCE$cp(final GlobalDataSource instance) {
        GlobalDataSource.INSTANCE = instance;
    }
    
    public static final GlobalDataSource getInstance(final Context context) {
        return GlobalDataSource.Companion.getInstance(context);
    }
    
    @Override
    public LiveData<List<QuickSearch>> fetchEngines() {
        final MutableLiveData<List<QuickSearch>> mutableLiveData = new MutableLiveData<List<QuickSearch>>();
        final QuickSearchUtils instance = QuickSearchUtils.INSTANCE;
        final Context context = this.context;
        if (context == null) {
            Intrinsics.throwUninitializedPropertyAccessException("context");
        }
        instance.loadDefaultEngines$app_focusWebkitRelease(context, mutableLiveData);
        return mutableLiveData;
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        public final GlobalDataSource getInstance(Context applicationContext) {
            Intrinsics.checkParameterIsNotNull(applicationContext, "context");
            final GlobalDataSource access$getINSTANCE$cp = GlobalDataSource.access$getINSTANCE$cp();
            if (access$getINSTANCE$cp != null) {
                return access$getINSTANCE$cp;
            }
            synchronized (this) {
                final GlobalDataSource access$getINSTANCE$cp2 = GlobalDataSource.access$getINSTANCE$cp();
                GlobalDataSource globalDataSource;
                if (access$getINSTANCE$cp2 != null) {
                    globalDataSource = access$getINSTANCE$cp2;
                }
                else {
                    final GlobalDataSource globalDataSource2 = new GlobalDataSource();
                    GlobalDataSource.access$setINSTANCE$cp(globalDataSource2);
                    applicationContext = applicationContext.getApplicationContext();
                    Intrinsics.checkExpressionValueIsNotNull(applicationContext, "context.applicationContext");
                    GlobalDataSource.access$setContext$p(globalDataSource2, applicationContext);
                    globalDataSource = globalDataSource2;
                }
                return globalDataSource;
            }
        }
    }
}
