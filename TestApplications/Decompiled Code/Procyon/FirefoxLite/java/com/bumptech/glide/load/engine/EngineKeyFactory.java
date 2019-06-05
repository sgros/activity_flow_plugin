// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import java.util.Map;
import com.bumptech.glide.load.Key;

class EngineKeyFactory
{
    public EngineKey buildKey(final Object o, final Key key, final int n, final int n2, final Map<Class<?>, Transformation<?>> map, final Class<?> clazz, final Class<?> clazz2, final Options options) {
        return new EngineKey(o, key, n, n2, map, clazz, clazz2, options);
    }
}
