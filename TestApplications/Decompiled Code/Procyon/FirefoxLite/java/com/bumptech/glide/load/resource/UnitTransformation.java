// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource;

import java.security.MessageDigest;
import com.bumptech.glide.load.engine.Resource;
import android.content.Context;
import com.bumptech.glide.load.Transformation;

public final class UnitTransformation<T> implements Transformation<T>
{
    private static final Transformation<?> TRANSFORMATION;
    
    static {
        TRANSFORMATION = new UnitTransformation<Object>();
    }
    
    private UnitTransformation() {
    }
    
    public static <T> UnitTransformation<T> get() {
        return (UnitTransformation<T>)(UnitTransformation)UnitTransformation.TRANSFORMATION;
    }
    
    @Override
    public Resource<T> transform(final Context context, final Resource<T> resource, final int n, final int n2) {
        return resource;
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
    }
}
