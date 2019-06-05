// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;

public class UnitTranscoder<Z> implements ResourceTranscoder<Z, Z>
{
    private static final UnitTranscoder<?> UNIT_TRANSCODER;
    
    static {
        UNIT_TRANSCODER = new UnitTranscoder<Object>();
    }
    
    public static <Z> ResourceTranscoder<Z, Z> get() {
        return (ResourceTranscoder<Z, Z>)UnitTranscoder.UNIT_TRANSCODER;
    }
    
    @Override
    public Resource<Z> transcode(final Resource<Z> resource, final Options options) {
        return resource;
    }
}
