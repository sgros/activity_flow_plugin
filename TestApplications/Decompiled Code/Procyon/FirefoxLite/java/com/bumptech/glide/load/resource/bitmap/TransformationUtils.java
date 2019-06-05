// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import android.graphics.Shader;
import android.graphics.BitmapShader;
import android.graphics.Shader$TileMode;
import com.bumptech.glide.util.Preconditions;
import android.graphics.Rect;
import android.graphics.Bitmap$Config;
import android.graphics.RectF;
import android.util.Log;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import java.util.concurrent.locks.ReentrantLock;
import android.os.Build$VERSION;
import android.os.Build;
import java.util.Arrays;
import java.util.List;
import android.graphics.Paint;
import java.util.concurrent.locks.Lock;

public final class TransformationUtils
{
    private static final Lock BITMAP_DRAWABLE_LOCK;
    private static final Paint CIRCLE_CROP_BITMAP_PAINT;
    private static final Paint CIRCLE_CROP_SHAPE_PAINT;
    private static final Paint DEFAULT_PAINT;
    private static final List<String> MODELS_REQUIRING_BITMAP_LOCK;
    
    static {
        DEFAULT_PAINT = new Paint(6);
        CIRCLE_CROP_SHAPE_PAINT = new Paint(7);
        MODELS_REQUIRING_BITMAP_LOCK = Arrays.asList("XT1097", "XT1085");
        Lock bitmap_DRAWABLE_LOCK;
        if (TransformationUtils.MODELS_REQUIRING_BITMAP_LOCK.contains(Build.MODEL) && Build$VERSION.SDK_INT == 22) {
            bitmap_DRAWABLE_LOCK = new ReentrantLock();
        }
        else {
            bitmap_DRAWABLE_LOCK = new NoLock();
        }
        BITMAP_DRAWABLE_LOCK = bitmap_DRAWABLE_LOCK;
        (CIRCLE_CROP_BITMAP_PAINT = new Paint(7)).setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.SRC_IN));
    }
    
    private static void applyMatrix(final Bitmap bitmap, final Bitmap bitmap2, final Matrix matrix) {
        TransformationUtils.BITMAP_DRAWABLE_LOCK.lock();
        try {
            final Canvas canvas = new Canvas(bitmap2);
            canvas.drawBitmap(bitmap, matrix, TransformationUtils.DEFAULT_PAINT);
            clear(canvas);
        }
        finally {
            TransformationUtils.BITMAP_DRAWABLE_LOCK.unlock();
        }
    }
    
    public static Bitmap centerCrop(final BitmapPool bitmapPool, final Bitmap bitmap, final int n, final int n2) {
        if (bitmap.getWidth() == n && bitmap.getHeight() == n2) {
            return bitmap;
        }
        final Matrix matrix = new Matrix();
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        float n3 = 0.0f;
        float n4;
        float n5;
        if (width * n2 > height * n) {
            n4 = n2 / (float)bitmap.getHeight();
            n5 = (n - bitmap.getWidth() * n4) * 0.5f;
        }
        else {
            n4 = n / (float)bitmap.getWidth();
            n3 = (n2 - bitmap.getHeight() * n4) * 0.5f;
            n5 = 0.0f;
        }
        matrix.setScale(n4, n4);
        matrix.postTranslate((float)(int)(n5 + 0.5f), (float)(int)(n3 + 0.5f));
        final Bitmap value = bitmapPool.get(n, n2, getSafeConfig(bitmap));
        setAlpha(bitmap, value);
        applyMatrix(bitmap, value, matrix);
        return value;
    }
    
    public static Bitmap centerInside(final BitmapPool bitmapPool, final Bitmap bitmap, final int n, final int n2) {
        if (bitmap.getWidth() <= n && bitmap.getHeight() <= n2) {
            if (Log.isLoggable("TransformationUtils", 2)) {
                Log.v("TransformationUtils", "requested target size larger or equal to input, returning input");
            }
            return bitmap;
        }
        if (Log.isLoggable("TransformationUtils", 2)) {
            Log.v("TransformationUtils", "requested target size too big for input, fit centering instead");
        }
        return fitCenter(bitmapPool, bitmap, n, n2);
    }
    
    public static Bitmap circleCrop(final BitmapPool bitmapPool, final Bitmap obj, int width, int min) {
        min = Math.min(width, min);
        final float n = (float)min;
        final float n2 = n / 2.0f;
        width = obj.getWidth();
        final int height = obj.getHeight();
        final float n3 = (float)width;
        final float a = n / n3;
        final float n4 = (float)height;
        final float max = Math.max(a, n / n4);
        final float n5 = n3 * max;
        final float n6 = max * n4;
        final float n7 = (n - n5) / 2.0f;
        final float n8 = (n - n6) / 2.0f;
        final RectF rectF = new RectF(n7, n8, n5 + n7, n6 + n8);
        final Bitmap alphaSafeBitmap = getAlphaSafeBitmap(bitmapPool, obj);
        final Bitmap value = bitmapPool.get(min, min, Bitmap$Config.ARGB_8888);
        value.setHasAlpha(true);
        TransformationUtils.BITMAP_DRAWABLE_LOCK.lock();
        try {
            final Canvas canvas = new Canvas(value);
            canvas.drawCircle(n2, n2, n2, TransformationUtils.CIRCLE_CROP_SHAPE_PAINT);
            canvas.drawBitmap(alphaSafeBitmap, (Rect)null, rectF, TransformationUtils.CIRCLE_CROP_BITMAP_PAINT);
            clear(canvas);
            TransformationUtils.BITMAP_DRAWABLE_LOCK.unlock();
            if (!alphaSafeBitmap.equals(obj)) {
                bitmapPool.put(alphaSafeBitmap);
            }
            return value;
        }
        finally {
            TransformationUtils.BITMAP_DRAWABLE_LOCK.unlock();
        }
    }
    
    private static void clear(final Canvas canvas) {
        canvas.setBitmap((Bitmap)null);
    }
    
    public static Bitmap fitCenter(final BitmapPool bitmapPool, final Bitmap bitmap, final int i, final int j) {
        if (bitmap.getWidth() == i && bitmap.getHeight() == j) {
            if (Log.isLoggable("TransformationUtils", 2)) {
                Log.v("TransformationUtils", "requested target size matches input, returning input");
            }
            return bitmap;
        }
        final float min = Math.min(i / (float)bitmap.getWidth(), j / (float)bitmap.getHeight());
        final int round = Math.round(bitmap.getWidth() * min);
        final int round2 = Math.round(bitmap.getHeight() * min);
        if (bitmap.getWidth() == round && bitmap.getHeight() == round2) {
            if (Log.isLoggable("TransformationUtils", 2)) {
                Log.v("TransformationUtils", "adjusted target size matches input, returning input");
            }
            return bitmap;
        }
        final Bitmap value = bitmapPool.get((int)(bitmap.getWidth() * min), (int)(bitmap.getHeight() * min), getSafeConfig(bitmap));
        setAlpha(bitmap, value);
        if (Log.isLoggable("TransformationUtils", 2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("request: ");
            sb.append(i);
            sb.append("x");
            sb.append(j);
            Log.v("TransformationUtils", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("toFit:   ");
            sb2.append(bitmap.getWidth());
            sb2.append("x");
            sb2.append(bitmap.getHeight());
            Log.v("TransformationUtils", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("toReuse: ");
            sb3.append(value.getWidth());
            sb3.append("x");
            sb3.append(value.getHeight());
            Log.v("TransformationUtils", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("minPct:   ");
            sb4.append(min);
            Log.v("TransformationUtils", sb4.toString());
        }
        final Matrix matrix = new Matrix();
        matrix.setScale(min, min);
        applyMatrix(bitmap, value, matrix);
        return value;
    }
    
    private static Bitmap getAlphaSafeBitmap(final BitmapPool bitmapPool, final Bitmap bitmap) {
        if (Bitmap$Config.ARGB_8888.equals((Object)bitmap.getConfig())) {
            return bitmap;
        }
        final Bitmap value = bitmapPool.get(bitmap.getWidth(), bitmap.getHeight(), Bitmap$Config.ARGB_8888);
        new Canvas(value).drawBitmap(bitmap, 0.0f, 0.0f, (Paint)null);
        return value;
    }
    
    public static Lock getBitmapDrawableLock() {
        return TransformationUtils.BITMAP_DRAWABLE_LOCK;
    }
    
    public static int getExifOrientationDegrees(int n) {
        switch (n) {
            default: {
                n = 0;
                break;
            }
            case 7:
            case 8: {
                n = 270;
                break;
            }
            case 5:
            case 6: {
                n = 90;
                break;
            }
            case 3:
            case 4: {
                n = 180;
                break;
            }
        }
        return n;
    }
    
    private static Bitmap$Config getSafeConfig(final Bitmap bitmap) {
        Bitmap$Config bitmap$Config;
        if (bitmap.getConfig() != null) {
            bitmap$Config = bitmap.getConfig();
        }
        else {
            bitmap$Config = Bitmap$Config.ARGB_8888;
        }
        return bitmap$Config;
    }
    
    static void initializeMatrixForRotation(final int n, final Matrix matrix) {
        switch (n) {
            case 8: {
                matrix.setRotate(-90.0f);
                break;
            }
            case 7: {
                matrix.setRotate(-90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            }
            case 6: {
                matrix.setRotate(90.0f);
                break;
            }
            case 5: {
                matrix.setRotate(90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            }
            case 4: {
                matrix.setRotate(180.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            }
            case 3: {
                matrix.setRotate(180.0f);
                break;
            }
            case 2: {
                matrix.setScale(-1.0f, 1.0f);
                break;
            }
        }
    }
    
    public static boolean isExifOrientationRequired(final int n) {
        switch (n) {
            default: {
                return false;
            }
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                return true;
            }
        }
    }
    
    public static Bitmap rotateImageExif(final BitmapPool bitmapPool, final Bitmap bitmap, final int n) {
        if (!isExifOrientationRequired(n)) {
            return bitmap;
        }
        final Matrix matrix = new Matrix();
        initializeMatrixForRotation(n, matrix);
        final RectF rectF = new RectF(0.0f, 0.0f, (float)bitmap.getWidth(), (float)bitmap.getHeight());
        matrix.mapRect(rectF);
        final Bitmap value = bitmapPool.get(Math.round(rectF.width()), Math.round(rectF.height()), getSafeConfig(bitmap));
        matrix.postTranslate(-rectF.left, -rectF.top);
        applyMatrix(bitmap, value, matrix);
        return value;
    }
    
    public static Bitmap roundedCorners(final BitmapPool bitmapPool, final Bitmap obj, final int n, final int n2, final int n3) {
        Preconditions.checkArgument(n > 0, "width must be greater than 0.");
        Preconditions.checkArgument(n2 > 0, "height must be greater than 0.");
        Preconditions.checkArgument(n3 > 0, "roundingRadius must be greater than 0.");
        final Bitmap alphaSafeBitmap = getAlphaSafeBitmap(bitmapPool, obj);
        final Bitmap value = bitmapPool.get(n, n2, Bitmap$Config.ARGB_8888);
        value.setHasAlpha(true);
        final BitmapShader shader = new BitmapShader(alphaSafeBitmap, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader((Shader)shader);
        final RectF rectF = new RectF(0.0f, 0.0f, (float)value.getWidth(), (float)value.getHeight());
        TransformationUtils.BITMAP_DRAWABLE_LOCK.lock();
        try {
            final Canvas canvas = new Canvas(value);
            canvas.drawColor(0, PorterDuff$Mode.CLEAR);
            final float n4 = (float)n3;
            canvas.drawRoundRect(rectF, n4, n4, paint);
            clear(canvas);
            TransformationUtils.BITMAP_DRAWABLE_LOCK.unlock();
            if (!alphaSafeBitmap.equals(obj)) {
                bitmapPool.put(alphaSafeBitmap);
            }
            return value;
        }
        finally {
            TransformationUtils.BITMAP_DRAWABLE_LOCK.unlock();
        }
    }
    
    public static void setAlpha(final Bitmap bitmap, final Bitmap bitmap2) {
        bitmap2.setHasAlpha(bitmap.hasAlpha());
    }
    
    private static final class NoLock implements Lock
    {
        NoLock() {
        }
        
        @Override
        public void lock() {
        }
        
        @Override
        public void lockInterruptibly() throws InterruptedException {
        }
        
        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException("Should not be called");
        }
        
        @Override
        public boolean tryLock() {
            return true;
        }
        
        @Override
        public boolean tryLock(final long n, final TimeUnit timeUnit) throws InterruptedException {
            return true;
        }
        
        @Override
        public void unlock() {
        }
    }
}
