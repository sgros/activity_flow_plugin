package org.mapsforge.map.reader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import org.mapsforge.core.util.LRUCache;
import org.mapsforge.map.reader.header.SubFileParameter;

class IndexCache {
   private static final int INDEX_ENTRIES_PER_BLOCK = 128;
   private static final int SIZE_OF_INDEX_BLOCK = 640;
   private final Map map;
   private final RandomAccessFile randomAccessFile;

   IndexCache(RandomAccessFile var1, int var2) {
      this.randomAccessFile = var1;
      this.map = new LRUCache(var2);
   }

   void destroy() {
      this.map.clear();
   }

   long getIndexEntry(SubFileParameter var1, long var2) throws IOException {
      if (var2 >= var1.numberOfBlocks) {
         throw new IOException("invalid block number: " + var2);
      } else {
         long var4 = var2 / 128L;
         IndexCacheEntryKey var6 = new IndexCacheEntryKey(var1, var4);
         byte[] var7 = (byte[])this.map.get(var6);
         byte[] var8 = var7;
         if (var7 == null) {
            var4 = var1.indexStartAddress + 640L * var4;
            int var9 = Math.min(640, (int)(var1.indexEndAddress - var4));
            var8 = new byte[var9];
            this.randomAccessFile.seek(var4);
            if (this.randomAccessFile.read(var8, 0, var9) != var9) {
               throw new IOException("could not read index block with size: " + var9);
            }

            this.map.put(var6, var8);
         }

         return Deserializer.getFiveBytesLong(var8, (int)(5L * (var2 % 128L)));
      }
   }
}
