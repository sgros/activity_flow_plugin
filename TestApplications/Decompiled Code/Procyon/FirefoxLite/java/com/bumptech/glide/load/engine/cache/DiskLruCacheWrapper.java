// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.cache;

import android.util.Log;
import com.bumptech.glide.load.Key;
import java.io.IOException;
import com.bumptech.glide.disklrucache.DiskLruCache;
import java.io.File;

public class DiskLruCacheWrapper implements DiskCache
{
    private static DiskLruCacheWrapper wrapper;
    private final File directory;
    private DiskLruCache diskLruCache;
    private final int maxSize;
    private final SafeKeyGenerator safeKeyGenerator;
    private final DiskCacheWriteLocker writeLocker;
    
    protected DiskLruCacheWrapper(final File directory, final int maxSize) {
        this.writeLocker = new DiskCacheWriteLocker();
        this.directory = directory;
        this.maxSize = maxSize;
        this.safeKeyGenerator = new SafeKeyGenerator();
    }
    
    public static DiskCache get(final File file, final int n) {
        synchronized (DiskLruCacheWrapper.class) {
            if (DiskLruCacheWrapper.wrapper == null) {
                DiskLruCacheWrapper.wrapper = new DiskLruCacheWrapper(file, n);
            }
            return DiskLruCacheWrapper.wrapper;
        }
    }
    
    private DiskLruCache getDiskCache() throws IOException {
        synchronized (this) {
            if (this.diskLruCache == null) {
                this.diskLruCache = DiskLruCache.open(this.directory, 1, 1, this.maxSize);
            }
            return this.diskLruCache;
        }
    }
    
    @Override
    public File get(final Key obj) {
        final String safeKey = this.safeKeyGenerator.getSafeKey(obj);
        if (Log.isLoggable("DiskLruCacheWrapper", 2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Get: Obtained: ");
            sb.append(safeKey);
            sb.append(" for for Key: ");
            sb.append(obj);
            Log.v("DiskLruCacheWrapper", sb.toString());
        }
        final File file = null;
        File file2;
        try {
            final DiskLruCache.Value value = this.getDiskCache().get(safeKey);
            file2 = file;
            if (value != null) {
                file2 = value.getFile(0);
            }
        }
        catch (IOException ex) {
            file2 = file;
            if (Log.isLoggable("DiskLruCacheWrapper", 5)) {
                Log.w("DiskLruCacheWrapper", "Unable to get from disk cache", (Throwable)ex);
                file2 = file;
            }
        }
        return file2;
    }
    
    @Override
    public void put(Key edit, final Writer writer) {
        final String safeKey = this.safeKeyGenerator.getSafeKey(edit);
        this.writeLocker.acquire(safeKey);
        try {
            if (Log.isLoggable("DiskLruCacheWrapper", 2)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Put: Obtained: ");
                sb.append(safeKey);
                sb.append(" for for Key: ");
                sb.append(edit);
                Log.v("DiskLruCacheWrapper", sb.toString());
            }
            try {
                final DiskLruCache diskCache = this.getDiskCache();
                if (diskCache.get(safeKey) != null) {
                    return;
                }
                edit = (Key)diskCache.edit(safeKey);
                if (edit != null) {
                    try {
                        if (writer.write(((DiskLruCache.Editor)edit).getFile(0))) {
                            ((DiskLruCache.Editor)edit).commit();
                        }
                        return;
                    }
                    finally {
                        ((DiskLruCache.Editor)edit).abortUnlessCommitted();
                    }
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Had two simultaneous puts for: ");
                sb2.append(safeKey);
                throw new IllegalStateException(sb2.toString());
            }
            catch (IOException ex) {
                if (Log.isLoggable("DiskLruCacheWrapper", 5)) {
                    Log.w("DiskLruCacheWrapper", "Unable to put to disk cache", (Throwable)ex);
                }
            }
        }
        finally {
            this.writeLocker.release(safeKey);
        }
    }
}
