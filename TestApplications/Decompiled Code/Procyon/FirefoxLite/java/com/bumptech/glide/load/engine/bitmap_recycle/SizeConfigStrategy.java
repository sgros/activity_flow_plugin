// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.bitmap_recycle;

import java.util.Iterator;
import com.bumptech.glide.util.Util;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.NavigableMap;
import java.util.Map;
import android.graphics.Bitmap;
import android.graphics.Bitmap$Config;
import android.annotation.TargetApi;

@TargetApi(19)
public class SizeConfigStrategy implements LruPoolStrategy
{
    private static final Bitmap$Config[] ALPHA_8_IN_CONFIGS;
    private static final Bitmap$Config[] ARGB_4444_IN_CONFIGS;
    private static final Bitmap$Config[] ARGB_8888_IN_CONFIGS;
    private static final Bitmap$Config[] RGB_565_IN_CONFIGS;
    private final GroupedLinkedMap<Key, Bitmap> groupedMap;
    private final KeyPool keyPool;
    private final Map<Bitmap$Config, NavigableMap<Integer, Integer>> sortedSizes;
    
    static {
        ARGB_8888_IN_CONFIGS = new Bitmap$Config[] { Bitmap$Config.ARGB_8888, null };
        RGB_565_IN_CONFIGS = new Bitmap$Config[] { Bitmap$Config.RGB_565 };
        ARGB_4444_IN_CONFIGS = new Bitmap$Config[] { Bitmap$Config.ARGB_4444 };
        ALPHA_8_IN_CONFIGS = new Bitmap$Config[] { Bitmap$Config.ALPHA_8 };
    }
    
    public SizeConfigStrategy() {
        this.keyPool = new KeyPool();
        this.groupedMap = new GroupedLinkedMap<Key, Bitmap>();
        this.sortedSizes = new HashMap<Bitmap$Config, NavigableMap<Integer, Integer>>();
    }
    
    private void decrementBitmapOfSize(final Integer obj, final Bitmap bitmap) {
        final NavigableMap<Integer, Integer> sizesForConfig = this.getSizesForConfig(bitmap.getConfig());
        final Integer n = sizesForConfig.get(obj);
        if (n != null) {
            if (n == 1) {
                sizesForConfig.remove(obj);
            }
            else {
                sizesForConfig.put(obj, n - 1);
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Tried to decrement empty size, size: ");
        sb.append(obj);
        sb.append(", removed: ");
        sb.append(this.logBitmap(bitmap));
        sb.append(", this: ");
        sb.append(this);
        throw new NullPointerException(sb.toString());
    }
    
    private Key findBestKey(final int i, final Bitmap$Config bitmap$Config) {
        final Key value = this.keyPool.get(i, bitmap$Config);
        final Bitmap$Config[] inConfigs = getInConfigs(bitmap$Config);
        final int length = inConfigs.length;
        int n = 0;
        Poolable value2;
        while (true) {
            value2 = value;
            if (n >= length) {
                break;
            }
            final Bitmap$Config bitmap$Config2 = inConfigs[n];
            final Integer n2 = this.getSizesForConfig(bitmap$Config2).ceilingKey(i);
            if (n2 != null && n2 <= i * 8) {
                if (n2 == i) {
                    if (bitmap$Config2 == null) {
                        value2 = value;
                        if (bitmap$Config == null) {
                            break;
                        }
                    }
                    else {
                        value2 = value;
                        if (bitmap$Config2.equals((Object)bitmap$Config)) {
                            break;
                        }
                    }
                }
                this.keyPool.offer(value);
                value2 = this.keyPool.get(n2, bitmap$Config2);
                break;
            }
            ++n;
        }
        return (Key)value2;
    }
    
    static String getBitmapString(final int i, final Bitmap$Config obj) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(i);
        sb.append("](");
        sb.append(obj);
        sb.append(")");
        return sb.toString();
    }
    
    private static Bitmap$Config[] getInConfigs(final Bitmap$Config bitmap$Config) {
        switch (SizeConfigStrategy$1.$SwitchMap$android$graphics$Bitmap$Config[bitmap$Config.ordinal()]) {
            default: {
                return new Bitmap$Config[] { bitmap$Config };
            }
            case 4: {
                return SizeConfigStrategy.ALPHA_8_IN_CONFIGS;
            }
            case 3: {
                return SizeConfigStrategy.ARGB_4444_IN_CONFIGS;
            }
            case 2: {
                return SizeConfigStrategy.RGB_565_IN_CONFIGS;
            }
            case 1: {
                return SizeConfigStrategy.ARGB_8888_IN_CONFIGS;
            }
        }
    }
    
    private NavigableMap<Integer, Integer> getSizesForConfig(final Bitmap$Config bitmap$Config) {
        NavigableMap<Integer, Integer> navigableMap;
        if ((navigableMap = this.sortedSizes.get(bitmap$Config)) == null) {
            navigableMap = new TreeMap<Integer, Integer>();
            this.sortedSizes.put(bitmap$Config, navigableMap);
        }
        return navigableMap;
    }
    
    @Override
    public Bitmap get(final int n, final int n2, Bitmap$Config bitmap$Config) {
        final Key bestKey = this.findBestKey(Util.getBitmapByteSize(n, n2, bitmap$Config), bitmap$Config);
        final Bitmap bitmap = this.groupedMap.get(bestKey);
        if (bitmap != null) {
            this.decrementBitmapOfSize(bestKey.size, bitmap);
            if (bitmap.getConfig() != null) {
                bitmap$Config = bitmap.getConfig();
            }
            else {
                bitmap$Config = Bitmap$Config.ARGB_8888;
            }
            bitmap.reconfigure(n, n2, bitmap$Config);
        }
        return bitmap;
    }
    
    @Override
    public int getSize(final Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }
    
    @Override
    public String logBitmap(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        return getBitmapString(Util.getBitmapByteSize(n, n2, bitmap$Config), bitmap$Config);
    }
    
    @Override
    public String logBitmap(final Bitmap bitmap) {
        return getBitmapString(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
    }
    
    @Override
    public void put(final Bitmap bitmap) {
        final Key value = this.keyPool.get(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
        this.groupedMap.put(value, bitmap);
        final NavigableMap<Integer, Integer> sizesForConfig = this.getSizesForConfig(bitmap.getConfig());
        final Integer n = sizesForConfig.get(value.size);
        final int size = value.size;
        int i = 1;
        if (n != null) {
            i = 1 + n;
        }
        sizesForConfig.put(size, i);
    }
    
    @Override
    public Bitmap removeLast() {
        final Bitmap bitmap = this.groupedMap.removeLast();
        if (bitmap != null) {
            this.decrementBitmapOfSize(Util.getBitmapByteSize(bitmap), bitmap);
        }
        return bitmap;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SizeConfigStrategy{groupedMap=");
        sb.append(this.groupedMap);
        sb.append(", sortedSizes=(");
        for (final Map.Entry<Bitmap$Config, NavigableMap<Integer, Integer>> entry : this.sortedSizes.entrySet()) {
            sb.append(entry.getKey());
            sb.append('[');
            sb.append(entry.getValue());
            sb.append("], ");
        }
        if (!this.sortedSizes.isEmpty()) {
            sb.replace(sb.length() - 2, sb.length(), "");
        }
        sb.append(")}");
        return sb.toString();
    }
    
    static final class Key implements Poolable
    {
        private Bitmap$Config config;
        private final KeyPool pool;
        int size;
        
        public Key(final KeyPool pool) {
            this.pool = pool;
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof Key;
            final boolean b2 = false;
            if (b) {
                final Key key = (Key)o;
                boolean b3 = b2;
                if (this.size == key.size) {
                    b3 = b2;
                    if (Util.bothNullOrEqual(this.config, key.config)) {
                        b3 = true;
                    }
                }
                return b3;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            final int size = this.size;
            int hashCode;
            if (this.config != null) {
                hashCode = this.config.hashCode();
            }
            else {
                hashCode = 0;
            }
            return size * 31 + hashCode;
        }
        
        public void init(final int size, final Bitmap$Config config) {
            this.size = size;
            this.config = config;
        }
        
        @Override
        public void offer() {
            this.pool.offer(this);
        }
        
        @Override
        public String toString() {
            return SizeConfigStrategy.getBitmapString(this.size, this.config);
        }
    }
    
    static class KeyPool extends BaseKeyPool<Key>
    {
        @Override
        protected Key create() {
            return new Key(this);
        }
        
        public Key get(final int n, final Bitmap$Config bitmap$Config) {
            final Key key = this.get();
            key.init(n, bitmap$Config);
            return key;
        }
    }
}
