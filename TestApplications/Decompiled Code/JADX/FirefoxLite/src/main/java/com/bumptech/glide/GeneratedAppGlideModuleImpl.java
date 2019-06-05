package com.bumptech.glide;

import android.content.Context;
import android.util.Log;
import java.util.Collections;
import java.util.Set;
import org.mozilla.focus.glide.FocusGlideModule;

final class GeneratedAppGlideModuleImpl extends GeneratedAppGlideModule {
    private final FocusGlideModule appGlideModule = new FocusGlideModule();

    GeneratedAppGlideModuleImpl() {
        if (Log.isLoggable("Glide", 3)) {
            Log.d("Glide", "Discovered AppGlideModule from annotation: org.mozilla.focus.glide.FocusGlideModule");
        }
    }

    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        this.appGlideModule.applyOptions(context, glideBuilder);
    }

    public void registerComponents(Context context, Glide glide, Registry registry) {
        this.appGlideModule.registerComponents(context, glide, registry);
    }

    public boolean isManifestParsingEnabled() {
        return this.appGlideModule.isManifestParsingEnabled();
    }

    public Set<Class<?>> getExcludedModuleClasses() {
        return Collections.emptySet();
    }

    /* Access modifiers changed, original: 0000 */
    public GeneratedRequestManagerFactory getRequestManagerFactory() {
        return new GeneratedRequestManagerFactory();
    }
}
