package com.bumptech.glide;

import com.bumptech.glide.manager.RequestManagerRetriever.RequestManagerFactory;
import com.bumptech.glide.module.AppGlideModule;
import java.util.Set;

abstract class GeneratedAppGlideModule extends AppGlideModule {
    @Deprecated
    public abstract Set<Class<?>> getExcludedModuleClasses();

    /* Access modifiers changed, original: 0000 */
    public RequestManagerFactory getRequestManagerFactory() {
        return null;
    }

    GeneratedAppGlideModule() {
    }
}
