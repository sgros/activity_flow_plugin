// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide;

import com.bumptech.glide.manager.RequestManagerRetriever;
import java.util.Collections;
import java.util.Set;
import android.content.Context;
import android.util.Log;
import org.mozilla.focus.glide.FocusGlideModule;

final class GeneratedAppGlideModuleImpl extends GeneratedAppGlideModule
{
    private final FocusGlideModule appGlideModule;
    
    GeneratedAppGlideModuleImpl() {
        this.appGlideModule = new FocusGlideModule();
        if (Log.isLoggable("Glide", 3)) {
            Log.d("Glide", "Discovered AppGlideModule from annotation: org.mozilla.focus.glide.FocusGlideModule");
        }
    }
    
    @Override
    public void applyOptions(final Context context, final GlideBuilder glideBuilder) {
        this.appGlideModule.applyOptions(context, glideBuilder);
    }
    
    public Set<Class<?>> getExcludedModuleClasses() {
        return Collections.emptySet();
    }
    
    @Override
    GeneratedRequestManagerFactory getRequestManagerFactory() {
        return new GeneratedRequestManagerFactory();
    }
    
    @Override
    public boolean isManifestParsingEnabled() {
        return this.appGlideModule.isManifestParsingEnabled();
    }
    
    @Override
    public void registerComponents(final Context context, final Glide glide, final Registry registry) {
        this.appGlideModule.registerComponents(context, glide, registry);
    }
}
