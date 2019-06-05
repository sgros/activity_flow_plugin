// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.bitmap_recycle;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import java.util.Collections;
import java.util.Collection;
import java.util.Arrays;
import java.util.HashSet;
import android.util.Log;
import android.annotation.TargetApi;
import android.os.Build$VERSION;
import java.util.Set;
import android.graphics.Bitmap$Config;

public class LruBitmapPool implements BitmapPool
{
    private static final Bitmap$Config DEFAULT_CONFIG;
    private final Set<Bitmap$Config> allowedConfigs;
    private int currentSize;
    private int evictions;
    private int hits;
    private final int initialMaxSize;
    private int maxSize;
    private int misses;
    private int puts;
    private final LruPoolStrategy strategy;
    private final BitmapTracker tracker;
    
    static {
        DEFAULT_CONFIG = Bitmap$Config.ARGB_8888;
    }
    
    public LruBitmapPool(final int n) {
        this(n, getDefaultStrategy(), getDefaultAllowedConfigs());
    }
    
    LruBitmapPool(final int n, final LruPoolStrategy strategy, final Set<Bitmap$Config> allowedConfigs) {
        this.initialMaxSize = n;
        this.maxSize = n;
        this.strategy = strategy;
        this.allowedConfigs = allowedConfigs;
        this.tracker = (BitmapTracker)new NullBitmapTracker();
    }
    
    @TargetApi(26)
    private static void assertNotHardwareConfig(final Bitmap$Config obj) {
        if (Build$VERSION.SDK_INT < 26) {
            return;
        }
        if (obj != Bitmap$Config.HARDWARE) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot create a mutable Bitmap with config: ");
        sb.append(obj);
        sb.append(". Consider setting Downsampler#ALLOW_HARDWARE_CONFIG to false in your RequestOptions and/or in GlideBuilder.setDefaultRequestOptions");
        throw new IllegalArgumentException(sb.toString());
    }
    
    private void dump() {
        if (Log.isLoggable("LruBitmapPool", 2)) {
            this.dumpUnchecked();
        }
    }
    
    private void dumpUnchecked() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Hits=");
        sb.append(this.hits);
        sb.append(", misses=");
        sb.append(this.misses);
        sb.append(", puts=");
        sb.append(this.puts);
        sb.append(", evictions=");
        sb.append(this.evictions);
        sb.append(", currentSize=");
        sb.append(this.currentSize);
        sb.append(", maxSize=");
        sb.append(this.maxSize);
        sb.append("\nStrategy=");
        sb.append(this.strategy);
        Log.v("LruBitmapPool", sb.toString());
    }
    
    private void evict() {
        this.trimToSize(this.maxSize);
    }
    
    @TargetApi(26)
    private static Set<Bitmap$Config> getDefaultAllowedConfigs() {
        final HashSet<Bitmap$Config> s = new HashSet<Bitmap$Config>();
        s.addAll((Collection<?>)Arrays.asList(Bitmap$Config.values()));
        if (Build$VERSION.SDK_INT >= 19) {
            s.add(null);
        }
        if (Build$VERSION.SDK_INT >= 26) {
            s.remove(Bitmap$Config.HARDWARE);
        }
        return (Set<Bitmap$Config>)Collections.unmodifiableSet((Set<?>)s);
    }
    
    private static LruPoolStrategy getDefaultStrategy() {
        LruPoolStrategy lruPoolStrategy;
        if (Build$VERSION.SDK_INT >= 19) {
            lruPoolStrategy = new SizeConfigStrategy();
        }
        else {
            lruPoolStrategy = new AttributeStrategy();
        }
        return lruPoolStrategy;
    }
    
    private Bitmap getDirtyOrNull(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        synchronized (this) {
            assertNotHardwareConfig(bitmap$Config);
            final LruPoolStrategy strategy = this.strategy;
            Bitmap$Config default_CONFIG;
            if (bitmap$Config != null) {
                default_CONFIG = bitmap$Config;
            }
            else {
                default_CONFIG = LruBitmapPool.DEFAULT_CONFIG;
            }
            final Bitmap value = strategy.get(n, n2, default_CONFIG);
            if (value == null) {
                if (Log.isLoggable("LruBitmapPool", 3)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Missing bitmap=");
                    sb.append(this.strategy.logBitmap(n, n2, bitmap$Config));
                    Log.d("LruBitmapPool", sb.toString());
                }
                ++this.misses;
            }
            else {
                ++this.hits;
                this.currentSize -= this.strategy.getSize(value);
                this.tracker.remove(value);
                normalize(value);
            }
            if (Log.isLoggable("LruBitmapPool", 2)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Get bitmap=");
                sb2.append(this.strategy.logBitmap(n, n2, bitmap$Config));
                Log.v("LruBitmapPool", sb2.toString());
            }
            this.dump();
            return value;
        }
    }
    
    @TargetApi(19)
    private static void maybeSetPreMultiplied(final Bitmap bitmap) {
        if (Build$VERSION.SDK_INT >= 19) {
            bitmap.setPremultiplied(true);
        }
    }
    
    private static void normalize(final Bitmap bitmap) {
        bitmap.setHasAlpha(true);
        maybeSetPreMultiplied(bitmap);
    }
    
    private void trimToSize(final int n) {
        synchronized (this) {
            while (this.currentSize > n) {
                final Bitmap removeLast = this.strategy.removeLast();
                if (removeLast == null) {
                    if (Log.isLoggable("LruBitmapPool", 5)) {
                        Log.w("LruBitmapPool", "Size mismatch, resetting");
                        this.dumpUnchecked();
                    }
                    this.currentSize = 0;
                    return;
                }
                this.tracker.remove(removeLast);
                this.currentSize -= this.strategy.getSize(removeLast);
                ++this.evictions;
                if (Log.isLoggable("LruBitmapPool", 3)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Evicting bitmap=");
                    sb.append(this.strategy.logBitmap(removeLast));
                    Log.d("LruBitmapPool", sb.toString());
                }
                this.dump();
                removeLast.recycle();
            }
        }
    }
    
    @Override
    public void clearMemory() {
        if (Log.isLoggable("LruBitmapPool", 3)) {
            Log.d("LruBitmapPool", "clearMemory");
        }
        this.trimToSize(0);
    }
    
    @Override
    public Bitmap get(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        final Bitmap dirtyOrNull = this.getDirtyOrNull(n, n2, bitmap$Config);
        Bitmap bitmap;
        if (dirtyOrNull != null) {
            dirtyOrNull.eraseColor(0);
            bitmap = dirtyOrNull;
        }
        else {
            bitmap = Bitmap.createBitmap(n, n2, bitmap$Config);
        }
        return bitmap;
    }
    
    @Override
    public Bitmap getDirty(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        Bitmap bitmap;
        if ((bitmap = this.getDirtyOrNull(n, n2, bitmap$Config)) == null) {
            bitmap = Bitmap.createBitmap(n, n2, bitmap$Config);
        }
        return bitmap;
    }
    
    @Override
    public void put(final Bitmap bitmap) {
        // monitorenter(this)
        Label_0281: {
            if (bitmap == null) {
                break Label_0281;
            }
            try {
                if (bitmap.isRecycled()) {
                    throw new IllegalStateException("Cannot pool recycled bitmap");
                }
                if (bitmap.isMutable() && this.strategy.getSize(bitmap) <= this.maxSize && this.allowedConfigs.contains(bitmap.getConfig())) {
                    final int size = this.strategy.getSize(bitmap);
                    this.strategy.put(bitmap);
                    this.tracker.add(bitmap);
                    ++this.puts;
                    this.currentSize += size;
                    if (Log.isLoggable("LruBitmapPool", 2)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Put bitmap in pool=");
                        sb.append(this.strategy.logBitmap(bitmap));
                        Log.v("LruBitmapPool", sb.toString());
                    }
                    this.dump();
                    this.evict();
                    return;
                }
                if (Log.isLoggable("LruBitmapPool", 2)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Reject bitmap from pool, bitmap: ");
                    sb2.append(this.strategy.logBitmap(bitmap));
                    sb2.append(", is mutable: ");
                    sb2.append(bitmap.isMutable());
                    sb2.append(", is allowed config: ");
                    sb2.append(this.allowedConfigs.contains(bitmap.getConfig()));
                    Log.v("LruBitmapPool", sb2.toString());
                }
                bitmap.recycle();
                return;
                throw new NullPointerException("Bitmap must not be null");
            }
            finally {
            }
            // monitorexit(this)
        }
    }
    
    @SuppressLint({ "InlinedApi" })
    @Override
    public void trimMemory(final int i) {
        if (Log.isLoggable("LruBitmapPool", 3)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("trimMemory, level=");
            sb.append(i);
            Log.d("LruBitmapPool", sb.toString());
        }
        if (i >= 40) {
            this.clearMemory();
        }
        else if (i >= 20) {
            this.trimToSize(this.maxSize / 2);
        }
    }
    
    private interface BitmapTracker
    {
        void add(final Bitmap p0);
        
        void remove(final Bitmap p0);
    }
    
    private static class NullBitmapTracker implements BitmapTracker
    {
        NullBitmapTracker() {
        }
        
        @Override
        public void add(final Bitmap bitmap) {
        }
        
        @Override
        public void remove(final Bitmap bitmap) {
        }
    }
}
