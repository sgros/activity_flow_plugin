// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.file;

import java.io.IOException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.Options;
import java.io.File;
import com.bumptech.glide.load.ResourceDecoder;

public class FileDecoder implements ResourceDecoder<File, File>
{
    @Override
    public Resource<File> decode(final File file, final int n, final int n2, final Options options) {
        return new FileResource(file);
    }
    
    @Override
    public boolean handles(final File file, final Options options) {
        return true;
    }
}
