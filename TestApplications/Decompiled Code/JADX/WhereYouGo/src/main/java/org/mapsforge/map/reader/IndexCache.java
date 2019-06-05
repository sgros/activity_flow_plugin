package org.mapsforge.map.reader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import org.mapsforge.core.util.LRUCache;
import org.mapsforge.map.reader.header.SubFileParameter;

class IndexCache {
    private static final int INDEX_ENTRIES_PER_BLOCK = 128;
    private static final int SIZE_OF_INDEX_BLOCK = 640;
    private final Map<IndexCacheEntryKey, byte[]> map;
    private final RandomAccessFile randomAccessFile;

    IndexCache(RandomAccessFile randomAccessFile, int capacity) {
        this.randomAccessFile = randomAccessFile;
        this.map = new LRUCache(capacity);
    }

    /* Access modifiers changed, original: 0000 */
    public void destroy() {
        this.map.clear();
    }

    /* Access modifiers changed, original: 0000 */
    public long getIndexEntry(SubFileParameter subFileParameter, long blockNumber) throws IOException {
        if (blockNumber >= subFileParameter.numberOfBlocks) {
            throw new IOException("invalid block number: " + blockNumber);
        }
        long indexBlockNumber = blockNumber / 128;
        IndexCacheEntryKey indexCacheEntryKey = new IndexCacheEntryKey(subFileParameter, indexBlockNumber);
        byte[] indexBlock = (byte[]) this.map.get(indexCacheEntryKey);
        if (indexBlock == null) {
            long indexBlockPosition = subFileParameter.indexStartAddress + (640 * indexBlockNumber);
            int indexBlockSize = Math.min(SIZE_OF_INDEX_BLOCK, (int) (subFileParameter.indexEndAddress - indexBlockPosition));
            indexBlock = new byte[indexBlockSize];
            this.randomAccessFile.seek(indexBlockPosition);
            if (this.randomAccessFile.read(indexBlock, 0, indexBlockSize) != indexBlockSize) {
                throw new IOException("could not read index block with size: " + indexBlockSize);
            }
            this.map.put(indexCacheEntryKey, indexBlock);
        }
        return Deserializer.getFiveBytesLong(indexBlock, (int) (5 * (blockNumber % 128)));
    }
}
