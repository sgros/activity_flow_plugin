// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import java.io.File;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.engine.cache.DiskCache;

class DataCacheWriter<DataType> implements Writer
{
    private final DataType data;
    private final Encoder<DataType> encoder;
    private final Options options;
    
    DataCacheWriter(final Encoder<DataType> encoder, final DataType data, final Options options) {
        this.encoder = encoder;
        this.data = data;
        this.options = options;
    }
    
    @Override
    public boolean write(final File file) {
        return this.encoder.encode(this.data, file, this.options);
    }
}
