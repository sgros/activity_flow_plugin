// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator;

import java.nio.Buffer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import android.graphics.Bitmap$Config;
import java.util.ArrayList;
import java.util.Map;
import java.nio.ByteBuffer;
import android.graphics.Bitmap;
import java.util.List;

public class InMemoryTileCache implements TileCache
{
    private static final float LOAD_FACTOR = 0.6f;
    private static final int TILE_SIZE_IN_BYTES = 131072;
    private final List<Bitmap> bitmapPool;
    private final ByteBuffer byteBuffer;
    private final int capacity;
    private final Map<MapGeneratorJob, Bitmap> map;
    
    public InMemoryTileCache(final int n) {
        this.capacity = getCapacity(n);
        this.bitmapPool = createBitmapPool(this.capacity + 1);
        this.map = createMap(this.capacity, this.bitmapPool);
        this.byteBuffer = ByteBuffer.allocate(131072);
    }
    
    private static List<Bitmap> createBitmapPool(final int n) {
        final ArrayList<Bitmap> list = new ArrayList<Bitmap>();
        for (int i = 0; i < n; ++i) {
            list.add(Bitmap.createBitmap(256, 256, Bitmap$Config.RGB_565));
        }
        return list;
    }
    
    private static Map<MapGeneratorJob, Bitmap> createMap(final int n, final List<Bitmap> list) {
        return new LinkedHashMap<MapGeneratorJob, Bitmap>((int)(n / 0.6f) + 2, 0.6f, true) {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected boolean removeEldestEntry(final Map.Entry<MapGeneratorJob, Bitmap> entry) {
                if (this.size() > n) {
                    this.remove(entry.getKey());
                    list.add(entry.getValue());
                }
                return false;
            }
        };
    }
    
    private static int getCapacity(final int i) {
        if (i < 0) {
            throw new IllegalArgumentException("capacity must not be negative: " + i);
        }
        return i;
    }
    
    @Override
    public boolean containsKey(final MapGeneratorJob mapGeneratorJob) {
        synchronized (this.map) {
            return this.map.containsKey(mapGeneratorJob);
        }
    }
    
    @Override
    public void destroy() {
        synchronized (this.map) {
            final Iterator<Bitmap> iterator = this.map.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().recycle();
            }
        }
        this.map.clear();
        final Iterator<Bitmap> iterator2 = this.bitmapPool.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().recycle();
        }
        this.bitmapPool.clear();
    }
    // monitorexit(map)
    
    @Override
    public Bitmap get(final MapGeneratorJob mapGeneratorJob) {
        synchronized (this.map) {
            return this.map.get(mapGeneratorJob);
        }
    }
    
    @Override
    public int getCapacity() {
        return this.capacity;
    }
    
    @Override
    public boolean isPersistent() {
        return false;
    }
    
    @Override
    public void put(final MapGeneratorJob mapGeneratorJob, final Bitmap bitmap) {
        if (this.capacity != 0) {
            synchronized (this.map) {
                if (this.bitmapPool.isEmpty()) {
                    return;
                }
            }
            final Bitmap bitmap2 = this.bitmapPool.remove(this.bitmapPool.size() - 1);
            this.byteBuffer.rewind();
            bitmap.copyPixelsToBuffer((Buffer)this.byteBuffer);
            this.byteBuffer.rewind();
            bitmap2.copyPixelsFromBuffer((Buffer)this.byteBuffer);
            final MapGeneratorJob mapGeneratorJob2;
            this.map.put(mapGeneratorJob2, bitmap2);
        }
        // monitorexit(map)
    }
    
    @Override
    public void setCapacity(final int n) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setPersistent(final boolean b) {
        throw new UnsupportedOperationException();
    }
}
