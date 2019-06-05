// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import java.nio.ByteBuffer;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.Options;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import com.bumptech.glide.util.LogTime;
import android.graphics.Bitmap$Config;
import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import android.os.Build$VERSION;
import java.io.InputStream;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.EnumSet;
import java.io.IOException;
import android.graphics.Bitmap;
import java.util.Collections;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import android.util.DisplayMetrics;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.ImageHeaderParser;
import android.graphics.BitmapFactory$Options;
import java.util.Queue;
import java.util.Set;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Option;

public final class Downsampler
{
    public static final Option<Boolean> ALLOW_HARDWARE_CONFIG;
    public static final Option<DecodeFormat> DECODE_FORMAT;
    public static final Option<DownsampleStrategy> DOWNSAMPLE_STRATEGY;
    private static final DecodeCallbacks EMPTY_CALLBACKS;
    public static final Option<Boolean> FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS;
    private static final Set<String> NO_DOWNSAMPLE_PRE_N_MIME_TYPES;
    private static final Queue<BitmapFactory$Options> OPTIONS_QUEUE;
    private static final Set<ImageHeaderParser.ImageType> TYPES_THAT_USE_POOL_PRE_KITKAT;
    private final BitmapPool bitmapPool;
    private final ArrayPool byteArrayPool;
    private final DisplayMetrics displayMetrics;
    private final HardwareConfigState hardwareConfigState;
    private final List<ImageHeaderParser> parsers;
    
    static {
        DECODE_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeFormat", DecodeFormat.DEFAULT);
        DOWNSAMPLE_STRATEGY = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DownsampleStrategy", DownsampleStrategy.AT_LEAST);
        FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.FixBitmapSize", false);
        ALLOW_HARDWARE_CONFIG = Option.memory("com.bumtpech.glide.load.resource.bitmap.Downsampler.AllowHardwareDecode", (Boolean)null);
        NO_DOWNSAMPLE_PRE_N_MIME_TYPES = Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(Arrays.asList("image/vnd.wap.wbmp", "image/x-ico")));
        EMPTY_CALLBACKS = (DecodeCallbacks)new DecodeCallbacks() {
            @Override
            public void onDecodeComplete(final BitmapPool bitmapPool, final Bitmap bitmap) throws IOException {
            }
            
            @Override
            public void onObtainBounds() {
            }
        };
        TYPES_THAT_USE_POOL_PRE_KITKAT = Collections.unmodifiableSet((Set<? extends ImageHeaderParser.ImageType>)EnumSet.of(ImageHeaderParser.ImageType.JPEG, ImageHeaderParser.ImageType.PNG_A, ImageHeaderParser.ImageType.PNG));
        OPTIONS_QUEUE = Util.createQueue(0);
    }
    
    public Downsampler(final List<ImageHeaderParser> parsers, final DisplayMetrics displayMetrics, final BitmapPool bitmapPool, final ArrayPool arrayPool) {
        this.hardwareConfigState = HardwareConfigState.getInstance();
        this.parsers = parsers;
        this.displayMetrics = Preconditions.checkNotNull(displayMetrics);
        this.bitmapPool = Preconditions.checkNotNull(bitmapPool);
        this.byteArrayPool = Preconditions.checkNotNull(arrayPool);
    }
    
    private static int adjustTargetDensityForError(final double n) {
        final int round = round(1.0E9 * n);
        return round(n / (round / 1.0E9f) * round);
    }
    
    private void calculateConfig(final InputStream inputStream, final DecodeFormat obj, final boolean b, final boolean b2, final BitmapFactory$Options bitmapFactory$Options, final int n, final int n2) throws IOException {
        if (this.hardwareConfigState.setHardwareConfigIfAllowed(n, n2, bitmapFactory$Options, obj, b, b2)) {
            return;
        }
        if (obj != DecodeFormat.PREFER_ARGB_8888 && obj != DecodeFormat.PREFER_ARGB_8888_DISALLOW_HARDWARE && Build$VERSION.SDK_INT != 16) {
            boolean hasAlpha;
            try {
                hasAlpha = ImageHeaderParserUtils.getType(this.parsers, inputStream, this.byteArrayPool).hasAlpha();
            }
            catch (IOException ex) {
                if (Log.isLoggable("Downsampler", 3)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Cannot determine whether the image has alpha or not from header, format ");
                    sb.append(obj);
                    Log.d("Downsampler", sb.toString(), (Throwable)ex);
                }
                hasAlpha = false;
            }
            Bitmap$Config inPreferredConfig;
            if (hasAlpha) {
                inPreferredConfig = Bitmap$Config.ARGB_8888;
            }
            else {
                inPreferredConfig = Bitmap$Config.RGB_565;
            }
            bitmapFactory$Options.inPreferredConfig = inPreferredConfig;
            if (bitmapFactory$Options.inPreferredConfig == Bitmap$Config.RGB_565 || bitmapFactory$Options.inPreferredConfig == Bitmap$Config.ARGB_4444 || bitmapFactory$Options.inPreferredConfig == Bitmap$Config.ALPHA_8) {
                bitmapFactory$Options.inDither = true;
            }
            return;
        }
        bitmapFactory$Options.inPreferredConfig = Bitmap$Config.ARGB_8888;
    }
    
    static void calculateScaling(final ImageHeaderParser.ImageType imageType, final InputStream inputStream, final DecodeCallbacks decodeCallbacks, final BitmapPool bitmapPool, final DownsampleStrategy obj, int n, final int n2, final int n3, final int n4, final int n5, final BitmapFactory$Options bitmapFactory$Options) throws IOException {
        if (n2 <= 0 || n3 <= 0) {
            return;
        }
        float n6;
        if (n != 90 && n != 270) {
            n6 = obj.getScaleFactor(n2, n3, n4, n5);
        }
        else {
            n6 = obj.getScaleFactor(n3, n2, n4, n5);
        }
        if (n6 <= 0.0f) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot scale with factor: ");
            sb.append(n6);
            sb.append(" from: ");
            sb.append(obj);
            sb.append(", source: [");
            sb.append(n2);
            sb.append("x");
            sb.append(n3);
            sb.append("], target: [");
            sb.append(n4);
            sb.append("x");
            sb.append(n5);
            sb.append("]");
            throw new IllegalArgumentException(sb.toString());
        }
        final DownsampleStrategy.SampleSizeRounding sampleSizeRounding = obj.getSampleSizeRounding(n2, n3, n4, n5);
        if (sampleSizeRounding != null) {
            final float n7 = (float)n2;
            n = round(n6 * n7);
            final float n8 = (float)n3;
            final int round = round(n6 * n8);
            n = n2 / n;
            final int n9 = n3 / round;
            if (sampleSizeRounding == DownsampleStrategy.SampleSizeRounding.MEMORY) {
                n = Math.max(n, n9);
            }
            else {
                n = Math.min(n, n9);
            }
            int max;
            if (Build$VERSION.SDK_INT <= 23 && Downsampler.NO_DOWNSAMPLE_PRE_N_MIME_TYPES.contains(bitmapFactory$Options.outMimeType)) {
                max = 1;
            }
            else {
                max = Math.max(1, Integer.highestOneBit(n));
                if (sampleSizeRounding == DownsampleStrategy.SampleSizeRounding.MEMORY && max < 1.0f / n6) {
                    max <<= 1;
                }
            }
            bitmapFactory$Options.inSampleSize = max;
            int round2;
            if (imageType == ImageHeaderParser.ImageType.JPEG) {
                final float n10 = (float)Math.min(max, 8);
                final int n11 = (int)Math.ceil(n7 / n10);
                final int n12 = (int)Math.ceil(n8 / n10);
                final int n13 = max / 8;
                n = n12;
                round2 = n11;
                if (n13 > 0) {
                    round2 = n11 / n13;
                    n = n12 / n13;
                }
            }
            else if (imageType != ImageHeaderParser.ImageType.PNG && imageType != ImageHeaderParser.ImageType.PNG_A) {
                if (imageType != ImageHeaderParser.ImageType.WEBP && imageType != ImageHeaderParser.ImageType.WEBP_A) {
                    if (n2 % max == 0 && n3 % max == 0) {
                        round2 = n2 / max;
                        n = n3 / max;
                    }
                    else {
                        final int[] dimensions = getDimensions(inputStream, bitmapFactory$Options, decodeCallbacks, bitmapPool);
                        round2 = dimensions[0];
                        n = dimensions[1];
                    }
                }
                else if (Build$VERSION.SDK_INT >= 24) {
                    final float n14 = (float)max;
                    round2 = Math.round(n7 / n14);
                    n = Math.round(n8 / n14);
                }
                else {
                    final float n15 = (float)max;
                    round2 = (int)Math.floor(n7 / n15);
                    n = (int)Math.floor(n8 / n15);
                }
            }
            else {
                final float n16 = (float)max;
                round2 = (int)Math.floor(n7 / n16);
                n = (int)Math.floor(n8 / n16);
            }
            final double d = obj.getScaleFactor(round2, n, n4, n5);
            if (Build$VERSION.SDK_INT >= 19) {
                bitmapFactory$Options.inTargetDensity = adjustTargetDensityForError(d);
                bitmapFactory$Options.inDensity = 1000000000;
            }
            if (isScaling(bitmapFactory$Options)) {
                bitmapFactory$Options.inScaled = true;
            }
            else {
                bitmapFactory$Options.inTargetDensity = 0;
                bitmapFactory$Options.inDensity = 0;
            }
            if (Log.isLoggable("Downsampler", 2)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Calculate scaling, source: [");
                sb2.append(n2);
                sb2.append("x");
                sb2.append(n3);
                sb2.append("], target: [");
                sb2.append(n4);
                sb2.append("x");
                sb2.append(n5);
                sb2.append("], power of two scaled: [");
                sb2.append(round2);
                sb2.append("x");
                sb2.append(n);
                sb2.append("], exact scale factor: ");
                sb2.append(n6);
                sb2.append(", power of 2 sample size: ");
                sb2.append(max);
                sb2.append(", adjusted scale factor: ");
                sb2.append(d);
                sb2.append(", target density: ");
                sb2.append(bitmapFactory$Options.inTargetDensity);
                sb2.append(", density: ");
                sb2.append(bitmapFactory$Options.inDensity);
                Log.v("Downsampler", sb2.toString());
            }
            return;
        }
        throw new IllegalArgumentException("Cannot round with null rounding");
    }
    
    private Bitmap decodeFromWrappedStreams(final InputStream inputStream, final BitmapFactory$Options bitmapFactory$Options, final DownsampleStrategy downsampleStrategy, final DecodeFormat decodeFormat, boolean b, final int n, final int n2, final boolean b2, final DecodeCallbacks decodeCallbacks) throws IOException {
        final long logTime = LogTime.getLogTime();
        final int[] dimensions = getDimensions(inputStream, bitmapFactory$Options, decodeCallbacks, this.bitmapPool);
        boolean b3 = false;
        final int i = dimensions[0];
        final int j = dimensions[1];
        final String outMimeType = bitmapFactory$Options.outMimeType;
        if (i == -1 || j == -1) {
            b = false;
        }
        final int orientation = ImageHeaderParserUtils.getOrientation(this.parsers, inputStream, this.byteArrayPool);
        final int exifOrientationDegrees = TransformationUtils.getExifOrientationDegrees(orientation);
        final boolean exifOrientationRequired = TransformationUtils.isExifOrientationRequired(orientation);
        int n3;
        if (n == Integer.MIN_VALUE) {
            n3 = i;
        }
        else {
            n3 = n;
        }
        int n4 = n2;
        if (n4 == Integer.MIN_VALUE) {
            n4 = j;
        }
        final ImageHeaderParser.ImageType type = ImageHeaderParserUtils.getType(this.parsers, inputStream, this.byteArrayPool);
        calculateScaling(type, inputStream, decodeCallbacks, this.bitmapPool, downsampleStrategy, exifOrientationDegrees, i, j, n3, n4, bitmapFactory$Options);
        this.calculateConfig(inputStream, decodeFormat, b, exifOrientationRequired, bitmapFactory$Options, n3, n4);
        if (Build$VERSION.SDK_INT >= 19) {
            b3 = true;
        }
        if (bitmapFactory$Options.inSampleSize == 1 || b3) {
            if (this.shouldUsePool(type)) {
                if (!b2 || !b3) {
                    float f;
                    if (isScaling(bitmapFactory$Options)) {
                        f = bitmapFactory$Options.inTargetDensity / (float)bitmapFactory$Options.inDensity;
                    }
                    else {
                        f = 1.0f;
                    }
                    final int inSampleSize = bitmapFactory$Options.inSampleSize;
                    final float n5 = (float)i;
                    final float n6 = (float)inSampleSize;
                    final int n7 = (int)Math.ceil(n5 / n6);
                    final int n8 = (int)Math.ceil(j / n6);
                    final int round = Math.round(n7 * f);
                    final int round2 = Math.round(n8 * f);
                    n3 = round;
                    n4 = round2;
                    if (Log.isLoggable("Downsampler", 2)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Calculated target [");
                        sb.append(round);
                        sb.append("x");
                        sb.append(round2);
                        sb.append("] for source [");
                        sb.append(i);
                        sb.append("x");
                        sb.append(j);
                        sb.append("], sampleSize: ");
                        sb.append(inSampleSize);
                        sb.append(", targetDensity: ");
                        sb.append(bitmapFactory$Options.inTargetDensity);
                        sb.append(", density: ");
                        sb.append(bitmapFactory$Options.inDensity);
                        sb.append(", density multiplier: ");
                        sb.append(f);
                        Log.v("Downsampler", sb.toString());
                        n4 = round2;
                        n3 = round;
                    }
                }
                if (n3 > 0 && n4 > 0) {
                    setInBitmap(bitmapFactory$Options, this.bitmapPool, n3, n4);
                }
            }
        }
        final Bitmap decodeStream = decodeStream(inputStream, bitmapFactory$Options, decodeCallbacks, this.bitmapPool);
        decodeCallbacks.onDecodeComplete(this.bitmapPool, decodeStream);
        if (Log.isLoggable("Downsampler", 2)) {
            logDecode(i, j, outMimeType, bitmapFactory$Options, decodeStream, n, n2, logTime);
        }
        Bitmap rotateImageExif = null;
        if (decodeStream != null) {
            decodeStream.setDensity(this.displayMetrics.densityDpi);
            final Bitmap obj = rotateImageExif = TransformationUtils.rotateImageExif(this.bitmapPool, decodeStream, orientation);
            if (!decodeStream.equals(obj)) {
                this.bitmapPool.put(decodeStream);
                rotateImageExif = obj;
            }
        }
        return rotateImageExif;
    }
    
    private static Bitmap decodeStream(final InputStream inputStream, final BitmapFactory$Options bitmapFactory$Options, final DecodeCallbacks decodeCallbacks, final BitmapPool bitmapPool) throws IOException {
        if (bitmapFactory$Options.inJustDecodeBounds) {
            inputStream.mark(10485760);
        }
        else {
            decodeCallbacks.onObtainBounds();
        }
        final int outWidth = bitmapFactory$Options.outWidth;
        final int outHeight = bitmapFactory$Options.outHeight;
        final String outMimeType = bitmapFactory$Options.outMimeType;
        TransformationUtils.getBitmapDrawableLock().lock();
        try {
            try {
                final Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, (Rect)null, bitmapFactory$Options);
                TransformationUtils.getBitmapDrawableLock().unlock();
                if (bitmapFactory$Options.inJustDecodeBounds) {
                    inputStream.reset();
                }
                return decodeStream;
            }
            finally {}
        }
        catch (IllegalArgumentException ex) {
            final IOException ioExceptionForInBitmapAssertion = newIoExceptionForInBitmapAssertion(ex, outWidth, outHeight, outMimeType, bitmapFactory$Options);
            if (Log.isLoggable("Downsampler", 3)) {
                Log.d("Downsampler", "Failed to decode with inBitmap, trying again without Bitmap re-use", (Throwable)ioExceptionForInBitmapAssertion);
            }
            if (bitmapFactory$Options.inBitmap != null) {
                try {
                    inputStream.reset();
                    bitmapPool.put(bitmapFactory$Options.inBitmap);
                    bitmapFactory$Options.inBitmap = null;
                    final Bitmap decodeStream2 = decodeStream(inputStream, bitmapFactory$Options, decodeCallbacks, bitmapPool);
                    TransformationUtils.getBitmapDrawableLock().unlock();
                    return decodeStream2;
                }
                catch (IOException ex2) {
                    throw ioExceptionForInBitmapAssertion;
                }
            }
            throw ioExceptionForInBitmapAssertion;
        }
        TransformationUtils.getBitmapDrawableLock().unlock();
    }
    
    @TargetApi(19)
    private static String getBitmapString(final Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        String string;
        if (Build$VERSION.SDK_INT >= 19) {
            final StringBuilder sb = new StringBuilder();
            sb.append(" (");
            sb.append(bitmap.getAllocationByteCount());
            sb.append(")");
            string = sb.toString();
        }
        else {
            string = "";
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("[");
        sb2.append(bitmap.getWidth());
        sb2.append("x");
        sb2.append(bitmap.getHeight());
        sb2.append("] ");
        sb2.append(bitmap.getConfig());
        sb2.append(string);
        return sb2.toString();
    }
    
    private static BitmapFactory$Options getDefaultOptions() {
        synchronized (Downsampler.class) {
            Object options_QUEUE = Downsampler.OPTIONS_QUEUE;
            synchronized (options_QUEUE) {
                final BitmapFactory$Options bitmapFactory$Options = Downsampler.OPTIONS_QUEUE.poll();
                // monitorexit(options_QUEUE)
                options_QUEUE = bitmapFactory$Options;
                if (bitmapFactory$Options == null) {
                    options_QUEUE = new BitmapFactory$Options();
                    resetOptions((BitmapFactory$Options)options_QUEUE);
                }
                return (BitmapFactory$Options)options_QUEUE;
            }
        }
    }
    
    private static int[] getDimensions(final InputStream inputStream, final BitmapFactory$Options bitmapFactory$Options, final DecodeCallbacks decodeCallbacks, final BitmapPool bitmapPool) throws IOException {
        bitmapFactory$Options.inJustDecodeBounds = true;
        decodeStream(inputStream, bitmapFactory$Options, decodeCallbacks, bitmapPool);
        bitmapFactory$Options.inJustDecodeBounds = false;
        return new int[] { bitmapFactory$Options.outWidth, bitmapFactory$Options.outHeight };
    }
    
    private static String getInBitmapString(final BitmapFactory$Options bitmapFactory$Options) {
        return getBitmapString(bitmapFactory$Options.inBitmap);
    }
    
    private static boolean isScaling(final BitmapFactory$Options bitmapFactory$Options) {
        return bitmapFactory$Options.inTargetDensity > 0 && bitmapFactory$Options.inDensity > 0 && bitmapFactory$Options.inTargetDensity != bitmapFactory$Options.inDensity;
    }
    
    private static void logDecode(final int i, final int j, final String str, final BitmapFactory$Options bitmapFactory$Options, final Bitmap bitmap, final int k, final int l, final long n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Decoded ");
        sb.append(getBitmapString(bitmap));
        sb.append(" from [");
        sb.append(i);
        sb.append("x");
        sb.append(j);
        sb.append("] ");
        sb.append(str);
        sb.append(" with inBitmap ");
        sb.append(getInBitmapString(bitmapFactory$Options));
        sb.append(" for [");
        sb.append(k);
        sb.append("x");
        sb.append(l);
        sb.append("], sample size: ");
        sb.append(bitmapFactory$Options.inSampleSize);
        sb.append(", density: ");
        sb.append(bitmapFactory$Options.inDensity);
        sb.append(", target density: ");
        sb.append(bitmapFactory$Options.inTargetDensity);
        sb.append(", thread: ");
        sb.append(Thread.currentThread().getName());
        sb.append(", duration: ");
        sb.append(LogTime.getElapsedMillis(n));
        Log.v("Downsampler", sb.toString());
    }
    
    private static IOException newIoExceptionForInBitmapAssertion(final IllegalArgumentException cause, final int i, final int j, final String str, final BitmapFactory$Options bitmapFactory$Options) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Exception decoding bitmap, outWidth: ");
        sb.append(i);
        sb.append(", outHeight: ");
        sb.append(j);
        sb.append(", outMimeType: ");
        sb.append(str);
        sb.append(", inBitmap: ");
        sb.append(getInBitmapString(bitmapFactory$Options));
        return new IOException(sb.toString(), cause);
    }
    
    private static void releaseOptions(final BitmapFactory$Options bitmapFactory$Options) {
        resetOptions(bitmapFactory$Options);
        synchronized (Downsampler.OPTIONS_QUEUE) {
            Downsampler.OPTIONS_QUEUE.offer(bitmapFactory$Options);
        }
    }
    
    private static void resetOptions(final BitmapFactory$Options bitmapFactory$Options) {
        bitmapFactory$Options.inTempStorage = null;
        bitmapFactory$Options.inDither = false;
        bitmapFactory$Options.inScaled = false;
        bitmapFactory$Options.inSampleSize = 1;
        bitmapFactory$Options.inPreferredConfig = null;
        bitmapFactory$Options.inJustDecodeBounds = false;
        bitmapFactory$Options.inDensity = 0;
        bitmapFactory$Options.inTargetDensity = 0;
        bitmapFactory$Options.outWidth = 0;
        bitmapFactory$Options.outHeight = 0;
        bitmapFactory$Options.outMimeType = null;
        bitmapFactory$Options.inBitmap = null;
        bitmapFactory$Options.inMutable = true;
    }
    
    private static int round(final double n) {
        return (int)(n + 0.5);
    }
    
    @TargetApi(26)
    private static void setInBitmap(final BitmapFactory$Options bitmapFactory$Options, final BitmapPool bitmapPool, final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 26 && bitmapFactory$Options.inPreferredConfig == Bitmap$Config.HARDWARE) {
            return;
        }
        bitmapFactory$Options.inBitmap = bitmapPool.getDirty(n, n2, bitmapFactory$Options.inPreferredConfig);
    }
    
    private boolean shouldUsePool(final ImageHeaderParser.ImageType imageType) throws IOException {
        return Build$VERSION.SDK_INT >= 19 || Downsampler.TYPES_THAT_USE_POOL_PRE_KITKAT.contains(imageType);
    }
    
    public Resource<Bitmap> decode(final InputStream inputStream, final int n, final int n2, final Options options) throws IOException {
        return this.decode(inputStream, n, n2, options, Downsampler.EMPTY_CALLBACKS);
    }
    
    public Resource<Bitmap> decode(final InputStream inputStream, final int n, final int n2, final Options options, final DecodeCallbacks decodeCallbacks) throws IOException {
        Preconditions.checkArgument(inputStream.markSupported(), "You must provide an InputStream that supports mark()");
        final byte[] inTempStorage = this.byteArrayPool.get(65536, byte[].class);
        final BitmapFactory$Options defaultOptions = getDefaultOptions();
        defaultOptions.inTempStorage = inTempStorage;
        final DecodeFormat decodeFormat = options.get(Downsampler.DECODE_FORMAT);
        final DownsampleStrategy downsampleStrategy = options.get(Downsampler.DOWNSAMPLE_STRATEGY);
        final boolean booleanValue = options.get(Downsampler.FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS);
        boolean b = options.get(Downsampler.ALLOW_HARDWARE_CONFIG) != null && options.get(Downsampler.ALLOW_HARDWARE_CONFIG);
        if (decodeFormat == DecodeFormat.PREFER_ARGB_8888_DISALLOW_HARDWARE) {
            b = false;
        }
        try {
            return BitmapResource.obtain(this.decodeFromWrappedStreams(inputStream, defaultOptions, downsampleStrategy, decodeFormat, b, n, n2, booleanValue, decodeCallbacks), this.bitmapPool);
        }
        finally {
            releaseOptions(defaultOptions);
            this.byteArrayPool.put(inTempStorage, byte[].class);
        }
    }
    
    public boolean handles(final InputStream inputStream) {
        return true;
    }
    
    public boolean handles(final ByteBuffer byteBuffer) {
        return true;
    }
    
    public interface DecodeCallbacks
    {
        void onDecodeComplete(final BitmapPool p0, final Bitmap p1) throws IOException;
        
        void onObtainBounds();
    }
}
