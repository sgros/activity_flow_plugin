// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.cache;

import android.util.DisplayMetrics;
import android.text.format.Formatter;
import android.os.Build$VERSION;
import android.app.ActivityManager;
import android.util.Log;
import android.content.Context;

public final class MemorySizeCalculator
{
    private final int arrayPoolSize;
    private final int bitmapPoolSize;
    private final Context context;
    private final int memoryCacheSize;
    
    MemorySizeCalculator(final Builder builder) {
        this.context = builder.context;
        int access$200;
        if (isLowMemoryDevice(builder.activityManager)) {
            access$200 = builder.arrayPoolSizeBytes / 2;
        }
        else {
            access$200 = builder.arrayPoolSizeBytes;
        }
        this.arrayPoolSize = access$200;
        final int maxSize = getMaxSize(builder.activityManager, builder.maxSizeMultiplier, builder.lowMemoryMaxSizeMultiplier);
        final float n = (float)(builder.screenDimensions.getWidthPixels() * builder.screenDimensions.getHeightPixels() * 4);
        final int round = Math.round(builder.bitmapPoolScreens * n);
        final int round2 = Math.round(n * builder.memoryCacheScreens);
        final int n2 = maxSize - this.arrayPoolSize;
        final int n3 = round2 + round;
        if (n3 <= n2) {
            this.memoryCacheSize = round2;
            this.bitmapPoolSize = round;
        }
        else {
            final float n4 = n2 / (builder.bitmapPoolScreens + builder.memoryCacheScreens);
            this.memoryCacheSize = Math.round(builder.memoryCacheScreens * n4);
            this.bitmapPoolSize = Math.round(n4 * builder.bitmapPoolScreens);
        }
        if (Log.isLoggable("MemorySizeCalculator", 3)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Calculation complete, Calculated memory cache size: ");
            sb.append(this.toMb(this.memoryCacheSize));
            sb.append(", pool size: ");
            sb.append(this.toMb(this.bitmapPoolSize));
            sb.append(", byte array size: ");
            sb.append(this.toMb(this.arrayPoolSize));
            sb.append(", memory class limited? ");
            sb.append(n3 > maxSize);
            sb.append(", max size: ");
            sb.append(this.toMb(maxSize));
            sb.append(", memoryClass: ");
            sb.append(builder.activityManager.getMemoryClass());
            sb.append(", isLowMemoryDevice: ");
            sb.append(isLowMemoryDevice(builder.activityManager));
            Log.d("MemorySizeCalculator", sb.toString());
        }
    }
    
    private static int getMaxSize(final ActivityManager activityManager, float n, final float n2) {
        final int memoryClass = activityManager.getMemoryClass();
        final boolean lowMemoryDevice = isLowMemoryDevice(activityManager);
        final float n3 = (float)(memoryClass * 1024 * 1024);
        if (lowMemoryDevice) {
            n = n2;
        }
        return Math.round(n3 * n);
    }
    
    private static boolean isLowMemoryDevice(final ActivityManager activityManager) {
        return Build$VERSION.SDK_INT >= 19 && activityManager.isLowRamDevice();
    }
    
    private String toMb(final int n) {
        return Formatter.formatFileSize(this.context, (long)n);
    }
    
    public int getArrayPoolSizeInBytes() {
        return this.arrayPoolSize;
    }
    
    public int getBitmapPoolSize() {
        return this.bitmapPoolSize;
    }
    
    public int getMemoryCacheSize() {
        return this.memoryCacheSize;
    }
    
    public static final class Builder
    {
        static final int BITMAP_POOL_TARGET_SCREENS;
        private ActivityManager activityManager;
        private int arrayPoolSizeBytes;
        private float bitmapPoolScreens;
        private final Context context;
        private float lowMemoryMaxSizeMultiplier;
        private float maxSizeMultiplier;
        private float memoryCacheScreens;
        private ScreenDimensions screenDimensions;
        
        static {
            int bitmap_POOL_TARGET_SCREENS;
            if (Build$VERSION.SDK_INT < 26) {
                bitmap_POOL_TARGET_SCREENS = 4;
            }
            else {
                bitmap_POOL_TARGET_SCREENS = 1;
            }
            BITMAP_POOL_TARGET_SCREENS = bitmap_POOL_TARGET_SCREENS;
        }
        
        public Builder(final Context context) {
            this.memoryCacheScreens = 2.0f;
            this.bitmapPoolScreens = (float)Builder.BITMAP_POOL_TARGET_SCREENS;
            this.maxSizeMultiplier = 0.4f;
            this.lowMemoryMaxSizeMultiplier = 0.33f;
            this.arrayPoolSizeBytes = 4194304;
            this.context = context;
            this.activityManager = (ActivityManager)context.getSystemService("activity");
            this.screenDimensions = new DisplayMetricsScreenDimensions(context.getResources().getDisplayMetrics());
            if (Build$VERSION.SDK_INT >= 26 && isLowMemoryDevice(this.activityManager)) {
                this.bitmapPoolScreens = 0.0f;
            }
        }
        
        public MemorySizeCalculator build() {
            return new MemorySizeCalculator(this);
        }
    }
    
    private static final class DisplayMetricsScreenDimensions implements ScreenDimensions
    {
        private final DisplayMetrics displayMetrics;
        
        public DisplayMetricsScreenDimensions(final DisplayMetrics displayMetrics) {
            this.displayMetrics = displayMetrics;
        }
        
        @Override
        public int getHeightPixels() {
            return this.displayMetrics.heightPixels;
        }
        
        @Override
        public int getWidthPixels() {
            return this.displayMetrics.widthPixels;
        }
    }
    
    interface ScreenDimensions
    {
        int getHeightPixels();
        
        int getWidthPixels();
    }
}
