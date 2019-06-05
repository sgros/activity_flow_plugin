package com.bumptech.glide.load.engine.bitmap_recycle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.util.Log;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LruBitmapPool implements BitmapPool {
    private static final Config DEFAULT_CONFIG = Config.ARGB_8888;
    private final Set<Config> allowedConfigs;
    private int currentSize;
    private int evictions;
    private int hits;
    private final int initialMaxSize;
    private int maxSize;
    private int misses;
    private int puts;
    private final LruPoolStrategy strategy;
    private final BitmapTracker tracker;

    private interface BitmapTracker {
        void add(Bitmap bitmap);

        void remove(Bitmap bitmap);
    }

    private static class NullBitmapTracker implements BitmapTracker {
        public void add(Bitmap bitmap) {
        }

        public void remove(Bitmap bitmap) {
        }

        NullBitmapTracker() {
        }
    }

    LruBitmapPool(int i, LruPoolStrategy lruPoolStrategy, Set<Config> set) {
        this.initialMaxSize = i;
        this.maxSize = i;
        this.strategy = lruPoolStrategy;
        this.allowedConfigs = set;
        this.tracker = new NullBitmapTracker();
    }

    public LruBitmapPool(int i) {
        this(i, getDefaultStrategy(), getDefaultAllowedConfigs());
    }

    public synchronized void put(Bitmap bitmap) {
        if (bitmap == null) {
            throw new NullPointerException("Bitmap must not be null");
        } else if (bitmap.isRecycled()) {
            throw new IllegalStateException("Cannot pool recycled bitmap");
        } else {
            StringBuilder stringBuilder;
            if (bitmap.isMutable() && this.strategy.getSize(bitmap) <= this.maxSize) {
                if (this.allowedConfigs.contains(bitmap.getConfig())) {
                    int size = this.strategy.getSize(bitmap);
                    this.strategy.put(bitmap);
                    this.tracker.add(bitmap);
                    this.puts++;
                    this.currentSize += size;
                    if (Log.isLoggable("LruBitmapPool", 2)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Put bitmap in pool=");
                        stringBuilder.append(this.strategy.logBitmap(bitmap));
                        Log.v("LruBitmapPool", stringBuilder.toString());
                    }
                    dump();
                    evict();
                    return;
                }
            }
            if (Log.isLoggable("LruBitmapPool", 2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Reject bitmap from pool, bitmap: ");
                stringBuilder.append(this.strategy.logBitmap(bitmap));
                stringBuilder.append(", is mutable: ");
                stringBuilder.append(bitmap.isMutable());
                stringBuilder.append(", is allowed config: ");
                stringBuilder.append(this.allowedConfigs.contains(bitmap.getConfig()));
                Log.v("LruBitmapPool", stringBuilder.toString());
            }
            bitmap.recycle();
        }
    }

    private void evict() {
        trimToSize(this.maxSize);
    }

    public Bitmap get(int i, int i2, Config config) {
        Bitmap dirtyOrNull = getDirtyOrNull(i, i2, config);
        if (dirtyOrNull == null) {
            return Bitmap.createBitmap(i, i2, config);
        }
        dirtyOrNull.eraseColor(0);
        return dirtyOrNull;
    }

    public Bitmap getDirty(int i, int i2, Config config) {
        Bitmap dirtyOrNull = getDirtyOrNull(i, i2, config);
        return dirtyOrNull == null ? Bitmap.createBitmap(i, i2, config) : dirtyOrNull;
    }

    @TargetApi(26)
    private static void assertNotHardwareConfig(Config config) {
        if (VERSION.SDK_INT >= 26 && config == Config.HARDWARE) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot create a mutable Bitmap with config: ");
            stringBuilder.append(config);
            stringBuilder.append(". Consider setting Downsampler#ALLOW_HARDWARE_CONFIG to false in your RequestOptions and/or in GlideBuilder.setDefaultRequestOptions");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private synchronized Bitmap getDirtyOrNull(int i, int i2, Config config) {
        Bitmap bitmap;
        StringBuilder stringBuilder;
        assertNotHardwareConfig(config);
        bitmap = this.strategy.get(i, i2, config != null ? config : DEFAULT_CONFIG);
        if (bitmap == null) {
            if (Log.isLoggable("LruBitmapPool", 3)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Missing bitmap=");
                stringBuilder.append(this.strategy.logBitmap(i, i2, config));
                Log.d("LruBitmapPool", stringBuilder.toString());
            }
            this.misses++;
        } else {
            this.hits++;
            this.currentSize -= this.strategy.getSize(bitmap);
            this.tracker.remove(bitmap);
            normalize(bitmap);
        }
        if (Log.isLoggable("LruBitmapPool", 2)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Get bitmap=");
            stringBuilder.append(this.strategy.logBitmap(i, i2, config));
            Log.v("LruBitmapPool", stringBuilder.toString());
        }
        dump();
        return bitmap;
    }

    private static void normalize(Bitmap bitmap) {
        bitmap.setHasAlpha(true);
        maybeSetPreMultiplied(bitmap);
    }

    @TargetApi(19)
    private static void maybeSetPreMultiplied(Bitmap bitmap) {
        if (VERSION.SDK_INT >= 19) {
            bitmap.setPremultiplied(true);
        }
    }

    public void clearMemory() {
        if (Log.isLoggable("LruBitmapPool", 3)) {
            Log.d("LruBitmapPool", "clearMemory");
        }
        trimToSize(0);
    }

    @SuppressLint({"InlinedApi"})
    public void trimMemory(int i) {
        if (Log.isLoggable("LruBitmapPool", 3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("trimMemory, level=");
            stringBuilder.append(i);
            Log.d("LruBitmapPool", stringBuilder.toString());
        }
        if (i >= 40) {
            clearMemory();
        } else if (i >= 20) {
            trimToSize(this.maxSize / 2);
        }
    }

    private synchronized void trimToSize(int i) {
        while (this.currentSize > i) {
            Bitmap removeLast = this.strategy.removeLast();
            if (removeLast == null) {
                if (Log.isLoggable("LruBitmapPool", 5)) {
                    Log.w("LruBitmapPool", "Size mismatch, resetting");
                    dumpUnchecked();
                }
                this.currentSize = 0;
                return;
            }
            this.tracker.remove(removeLast);
            this.currentSize -= this.strategy.getSize(removeLast);
            this.evictions++;
            if (Log.isLoggable("LruBitmapPool", 3)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Evicting bitmap=");
                stringBuilder.append(this.strategy.logBitmap(removeLast));
                Log.d("LruBitmapPool", stringBuilder.toString());
            }
            dump();
            removeLast.recycle();
        }
    }

    private void dump() {
        if (Log.isLoggable("LruBitmapPool", 2)) {
            dumpUnchecked();
        }
    }

    private void dumpUnchecked() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hits=");
        stringBuilder.append(this.hits);
        stringBuilder.append(", misses=");
        stringBuilder.append(this.misses);
        stringBuilder.append(", puts=");
        stringBuilder.append(this.puts);
        stringBuilder.append(", evictions=");
        stringBuilder.append(this.evictions);
        stringBuilder.append(", currentSize=");
        stringBuilder.append(this.currentSize);
        stringBuilder.append(", maxSize=");
        stringBuilder.append(this.maxSize);
        stringBuilder.append("\nStrategy=");
        stringBuilder.append(this.strategy);
        Log.v("LruBitmapPool", stringBuilder.toString());
    }

    private static LruPoolStrategy getDefaultStrategy() {
        if (VERSION.SDK_INT >= 19) {
            return new SizeConfigStrategy();
        }
        return new AttributeStrategy();
    }

    @TargetApi(26)
    private static Set<Config> getDefaultAllowedConfigs() {
        HashSet hashSet = new HashSet();
        hashSet.addAll(Arrays.asList(Config.values()));
        if (VERSION.SDK_INT >= 19) {
            hashSet.add(null);
        }
        if (VERSION.SDK_INT >= 26) {
            hashSet.remove(Config.HARDWARE);
        }
        return Collections.unmodifiableSet(hashSet);
    }
}
