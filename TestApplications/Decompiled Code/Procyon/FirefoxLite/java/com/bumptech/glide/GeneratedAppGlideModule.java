// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide;

import com.bumptech.glide.manager.RequestManagerRetriever;
import java.util.Set;
import com.bumptech.glide.module.AppGlideModule;

abstract class GeneratedAppGlideModule extends AppGlideModule
{
    @Deprecated
    abstract Set<Class<?>> getExcludedModuleClasses();
    
    RequestManagerRetriever.RequestManagerFactory getRequestManagerFactory() {
        return null;
    }
}
