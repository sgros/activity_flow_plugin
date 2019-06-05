// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.module;

import com.bumptech.glide.GlideBuilder;
import android.content.Context;

public abstract class AppGlideModule extends LibraryGlideModule implements AppliesOptions
{
    @Override
    public void applyOptions(final Context context, final GlideBuilder glideBuilder) {
    }
    
    public boolean isManifestParsingEnabled() {
        return true;
    }
}
