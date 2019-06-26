package org.mapsforge.android.maps.mapgenerator;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class InMemoryTileCache implements TileCache {
    private static final float LOAD_FACTOR = 0.6f;
    private static final int TILE_SIZE_IN_BYTES = 131072;
    private final List<Bitmap> bitmapPool = createBitmapPool(this.capacity + 1);
    private final ByteBuffer byteBuffer = ByteBuffer.allocate(131072);
    private final int capacity;
    private final Map<MapGeneratorJob, Bitmap> map = createMap(this.capacity, this.bitmapPool);

    private static List<Bitmap> createBitmapPool(int poolSize) {
        List<Bitmap> bitmaps = new ArrayList();
        for (int i = 0; i < poolSize; i++) {
            bitmaps.add(Bitmap.createBitmap(256, 256, Config.RGB_565));
        }
        return bitmaps;
    }

    private static Map<MapGeneratorJob, Bitmap> createMap(int mapCapacity, List<Bitmap> bitmapPool) {
        final int i = mapCapacity;
        final List<Bitmap> list = bitmapPool;
        return new LinkedHashMap<MapGeneratorJob, Bitmap>(((int) (((float) mapCapacity) / LOAD_FACTOR)) + 2, LOAD_FACTOR, true) {
            private static final long serialVersionUID = 1;

            /* Access modifiers changed, original: protected */
            public boolean removeEldestEntry(Entry<MapGeneratorJob, Bitmap> eldestEntry) {
                if (size() > i) {
                    remove(eldestEntry.getKey());
                    list.add(eldestEntry.getValue());
                }
                return false;
            }
        };
    }

    private static int getCapacity(int capacity) {
        if (capacity >= 0) {
            return capacity;
        }
        throw new IllegalArgumentException("capacity must not be negative: " + capacity);
    }

    public InMemoryTileCache(int capacity) {
        this.capacity = getCapacity(capacity);
    }

    public boolean containsKey(MapGeneratorJob mapGeneratorJob) {
        boolean containsKey;
        synchronized (this.map) {
            containsKey = this.map.containsKey(mapGeneratorJob);
        }
        return containsKey;
    }

    public void destroy() {
        synchronized (this.map) {
            for (Bitmap bitmap : this.map.values()) {
                bitmap.recycle();
            }
            this.map.clear();
            for (Bitmap bitmap2 : this.bitmapPool) {
                bitmap2.recycle();
            }
            this.bitmapPool.clear();
        }
    }

    public Bitmap get(MapGeneratorJob mapGeneratorJob) {
        Bitmap bitmap;
        synchronized (this.map) {
            bitmap = (Bitmap) this.map.get(mapGeneratorJob);
        }
        return bitmap;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean isPersistent() {
        return false;
    }

    public void put(MapGeneratorJob mapGeneratorJob, Bitmap bitmap) {
        if (this.capacity != 0) {
            synchronized (this.map) {
                if (this.bitmapPool.isEmpty()) {
                    return;
                }
                Bitmap pooledBitmap = (Bitmap) this.bitmapPool.remove(this.bitmapPool.size() - 1);
                this.byteBuffer.rewind();
                bitmap.copyPixelsToBuffer(this.byteBuffer);
                this.byteBuffer.rewind();
                pooledBitmap.copyPixelsFromBuffer(this.byteBuffer);
                this.map.put(mapGeneratorJob, pooledBitmap);
            }
        }
    }

    public void setCapacity(int capacity) {
        throw new UnsupportedOperationException();
    }

    public void setPersistent(boolean persistent) {
        throw new UnsupportedOperationException();
    }
}
