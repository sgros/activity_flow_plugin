// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data.mediastore;

import java.io.File;

class FileService
{
    public boolean exists(final File file) {
        return file.exists();
    }
    
    public File get(final String pathname) {
        return new File(pathname);
    }
    
    public long length(final File file) {
        return file.length();
    }
}
