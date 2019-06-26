// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader;

import java.io.IOException;
import org.mapsforge.map.reader.header.SubFileParameter;
import org.mapsforge.core.util.LRUCache;
import java.io.RandomAccessFile;
import java.util.Map;

class IndexCache
{
    private static final int INDEX_ENTRIES_PER_BLOCK = 128;
    private static final int SIZE_OF_INDEX_BLOCK = 640;
    private final Map<IndexCacheEntryKey, byte[]> map;
    private final RandomAccessFile randomAccessFile;
    
    IndexCache(final RandomAccessFile randomAccessFile, final int n) {
        this.randomAccessFile = randomAccessFile;
        this.map = new LRUCache<IndexCacheEntryKey, byte[]>(n);
    }
    
    void destroy() {
        this.map.clear();
    }
    
    long getIndexEntry(final SubFileParameter subFileParameter, final long lng) throws IOException {
        if (lng >= subFileParameter.numberOfBlocks) {
            throw new IOException("invalid block number: " + lng);
        }
        final long n = lng / 128L;
        final IndexCacheEntryKey indexCacheEntryKey = new IndexCacheEntryKey(subFileParameter, n);
        byte[] b;
        if ((b = this.map.get(indexCacheEntryKey)) == null) {
            final long pos = subFileParameter.indexStartAddress + 640L * n;
            final int min = Math.min(640, (int)(subFileParameter.indexEndAddress - pos));
            b = new byte[min];
            this.randomAccessFile.seek(pos);
            if (this.randomAccessFile.read(b, 0, min) != min) {
                throw new IOException("could not read index block with size: " + min);
            }
            this.map.put(indexCacheEntryKey, b);
        }
        return Deserializer.getFiveBytesLong(b, (int)(5L * (lng % 128L)));
    }
}
