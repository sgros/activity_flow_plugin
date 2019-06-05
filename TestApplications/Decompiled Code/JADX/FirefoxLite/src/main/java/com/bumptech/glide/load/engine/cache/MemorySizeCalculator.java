package com.bumptech.glide.load.engine.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;

public final class MemorySizeCalculator {
    private final int arrayPoolSize;
    private final int bitmapPoolSize;
    private final Context context;
    private final int memoryCacheSize;

    public static final class Builder {
        static final int BITMAP_POOL_TARGET_SCREENS = (VERSION.SDK_INT < 26 ? 4 : 1);
        private ActivityManager activityManager;
        private int arrayPoolSizeBytes = 4194304;
        private float bitmapPoolScreens = ((float) BITMAP_POOL_TARGET_SCREENS);
        private final Context context;
        private float lowMemoryMaxSizeMultiplier = 0.33f;
        private float maxSizeMultiplier = 0.4f;
        private float memoryCacheScreens = 2.0f;
        private ScreenDimensions screenDimensions;

        public Builder(Context context) {
            this.context = context;
            this.activityManager = (ActivityManager) context.getSystemService("activity");
            this.screenDimensions = new DisplayMetricsScreenDimensions(context.getResources().getDisplayMetrics());
            if (VERSION.SDK_INT >= 26 && MemorySizeCalculator.isLowMemoryDevice(this.activityManager)) {
                this.bitmapPoolScreens = 0.0f;
            }
        }

        public MemorySizeCalculator build() {
            return new MemorySizeCalculator(this);
        }
    }

    interface ScreenDimensions {
        int getHeightPixels();

        int getWidthPixels();
    }

    private static final class DisplayMetricsScreenDimensions implements ScreenDimensions {
        private final DisplayMetrics displayMetrics;

        public DisplayMetricsScreenDimensions(DisplayMetrics displayMetrics) {
            this.displayMetrics = displayMetrics;
        }

        public int getWidthPixels() {
            return this.displayMetrics.widthPixels;
        }

        public int getHeightPixels() {
            return this.displayMetrics.heightPixels;
        }
    }

    MemorySizeCalculator(Builder builder) {
        int access$200;
        this.context = builder.context;
        if (isLowMemoryDevice(builder.activityManager)) {
            access$200 = builder.arrayPoolSizeBytes / 2;
        } else {
            access$200 = builder.arrayPoolSizeBytes;
        }
        this.arrayPoolSize = access$200;
        access$200 = getMaxSize(builder.activityManager, builder.maxSizeMultiplier, builder.lowMemoryMaxSizeMultiplier);
        float widthPixels = (float) ((builder.screenDimensions.getWidthPixels() * builder.screenDimensions.getHeightPixels()) * 4);
        int round = Math.round(builder.bitmapPoolScreens * widthPixels);
        int round2 = Math.round(widthPixels * builder.memoryCacheScreens);
        int i = access$200 - this.arrayPoolSize;
        int i2 = round2 + round;
        if (i2 <= i) {
            this.memoryCacheSize = round2;
            this.bitmapPoolSize = round;
        } else {
            widthPixels = ((float) i) / (builder.bitmapPoolScreens + builder.memoryCacheScreens);
            this.memoryCacheSize = Math.round(builder.memoryCacheScreens * widthPixels);
            this.bitmapPoolSize = Math.round(widthPixels * builder.bitmapPoolScreens);
        }
        if (Log.isLoggable("MemorySizeCalculator", 3)) {
            String str = "MemorySizeCalculator";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Calculation complete, Calculated memory cache size: ");
            stringBuilder.append(toMb(this.memoryCacheSize));
            stringBuilder.append(", pool size: ");
            stringBuilder.append(toMb(this.bitmapPoolSize));
            stringBuilder.append(", byte array size: ");
            stringBuilder.append(toMb(this.arrayPoolSize));
            stringBuilder.append(", memory class limited? ");
            stringBuilder.append(i2 > access$200);
            stringBuilder.append(", max size: ");
            stringBuilder.append(toMb(access$200));
            stringBuilder.append(", memoryClass: ");
            stringBuilder.append(builder.activityManager.getMemoryClass());
            stringBuilder.append(", isLowMemoryDevice: ");
            stringBuilder.append(isLowMemoryDevice(builder.activityManager));
            Log.d(str, stringBuilder.toString());
        }
    }

    public int getMemoryCacheSize() {
        return this.memoryCacheSize;
    }

    public int getBitmapPoolSize() {
        return this.bitmapPoolSize;
    }

    public int getArrayPoolSizeInBytes() {
        return this.arrayPoolSize;
    }

    private static int getMaxSize(ActivityManager activityManager, float f, float f2) {
        float memoryClass = (float) ((activityManager.getMemoryClass() * 1024) * 1024);
        if (isLowMemoryDevice(activityManager)) {
            f = f2;
        }
        return Math.round(memoryClass * f);
    }

    private String toMb(int i) {
        return Formatter.formatFileSize(this.context, (long) i);
    }

    private static boolean isLowMemoryDevice(ActivityManager activityManager) {
        return VERSION.SDK_INT >= 19 ? activityManager.isLowRamDevice() : false;
    }
}
