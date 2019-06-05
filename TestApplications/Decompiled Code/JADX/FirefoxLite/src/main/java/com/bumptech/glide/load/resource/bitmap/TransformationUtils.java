package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class TransformationUtils {
    private static final Lock BITMAP_DRAWABLE_LOCK;
    private static final Paint CIRCLE_CROP_BITMAP_PAINT = new Paint(7);
    private static final Paint CIRCLE_CROP_SHAPE_PAINT = new Paint(7);
    private static final Paint DEFAULT_PAINT = new Paint(6);
    private static final List<String> MODELS_REQUIRING_BITMAP_LOCK = Arrays.asList(new String[]{"XT1097", "XT1085"});

    private static final class NoLock implements Lock {
        public void lock() {
        }

        public void lockInterruptibly() throws InterruptedException {
        }

        public boolean tryLock() {
            return true;
        }

        public boolean tryLock(long j, TimeUnit timeUnit) throws InterruptedException {
            return true;
        }

        public void unlock() {
        }

        NoLock() {
        }

        public Condition newCondition() {
            throw new UnsupportedOperationException("Should not be called");
        }
    }

    public static int getExifOrientationDegrees(int i) {
        switch (i) {
            case 3:
            case 4:
                return 180;
            case 5:
            case 6:
                return 90;
            case 7:
            case 8:
                return 270;
            default:
                return 0;
        }
    }

    public static boolean isExifOrientationRequired(int i) {
        switch (i) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return true;
            default:
                return false;
        }
    }

    static {
        Lock reentrantLock = (MODELS_REQUIRING_BITMAP_LOCK.contains(Build.MODEL) && VERSION.SDK_INT == 22) ? new ReentrantLock() : new NoLock();
        BITMAP_DRAWABLE_LOCK = reentrantLock;
        CIRCLE_CROP_BITMAP_PAINT.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    }

    public static Lock getBitmapDrawableLock() {
        return BITMAP_DRAWABLE_LOCK;
    }

    public static Bitmap centerCrop(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        if (bitmap.getWidth() == i && bitmap.getHeight() == i2) {
            return bitmap;
        }
        float height;
        float width;
        Matrix matrix = new Matrix();
        float f = 0.0f;
        if (bitmap.getWidth() * i2 > bitmap.getHeight() * i) {
            height = ((float) i2) / ((float) bitmap.getHeight());
            width = (((float) i) - (((float) bitmap.getWidth()) * height)) * 0.5f;
        } else {
            height = ((float) i) / ((float) bitmap.getWidth());
            f = (((float) i2) - (((float) bitmap.getHeight()) * height)) * 0.5f;
            width = 0.0f;
        }
        matrix.setScale(height, height);
        matrix.postTranslate((float) ((int) (width + 0.5f)), (float) ((int) (f + 0.5f)));
        Bitmap bitmap2 = bitmapPool.get(i, i2, getSafeConfig(bitmap));
        setAlpha(bitmap, bitmap2);
        applyMatrix(bitmap, bitmap2, matrix);
        return bitmap2;
    }

    public static Bitmap fitCenter(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        if (bitmap.getWidth() == i && bitmap.getHeight() == i2) {
            if (Log.isLoggable("TransformationUtils", 2)) {
                Log.v("TransformationUtils", "requested target size matches input, returning input");
            }
            return bitmap;
        }
        float min = Math.min(((float) i) / ((float) bitmap.getWidth()), ((float) i2) / ((float) bitmap.getHeight()));
        int round = Math.round(((float) bitmap.getWidth()) * min);
        int round2 = Math.round(((float) bitmap.getHeight()) * min);
        if (bitmap.getWidth() == round && bitmap.getHeight() == round2) {
            if (Log.isLoggable("TransformationUtils", 2)) {
                Log.v("TransformationUtils", "adjusted target size matches input, returning input");
            }
            return bitmap;
        }
        Bitmap bitmap2 = bitmapPool.get((int) (((float) bitmap.getWidth()) * min), (int) (((float) bitmap.getHeight()) * min), getSafeConfig(bitmap));
        setAlpha(bitmap, bitmap2);
        if (Log.isLoggable("TransformationUtils", 2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("request: ");
            stringBuilder.append(i);
            stringBuilder.append("x");
            stringBuilder.append(i2);
            Log.v("TransformationUtils", stringBuilder.toString());
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("toFit:   ");
            stringBuilder2.append(bitmap.getWidth());
            stringBuilder2.append("x");
            stringBuilder2.append(bitmap.getHeight());
            Log.v("TransformationUtils", stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("toReuse: ");
            stringBuilder2.append(bitmap2.getWidth());
            stringBuilder2.append("x");
            stringBuilder2.append(bitmap2.getHeight());
            Log.v("TransformationUtils", stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("minPct:   ");
            stringBuilder2.append(min);
            Log.v("TransformationUtils", stringBuilder2.toString());
        }
        Matrix matrix = new Matrix();
        matrix.setScale(min, min);
        applyMatrix(bitmap, bitmap2, matrix);
        return bitmap2;
    }

    public static Bitmap centerInside(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        if (bitmap.getWidth() > i || bitmap.getHeight() > i2) {
            if (Log.isLoggable("TransformationUtils", 2)) {
                Log.v("TransformationUtils", "requested target size too big for input, fit centering instead");
            }
            return fitCenter(bitmapPool, bitmap, i, i2);
        }
        if (Log.isLoggable("TransformationUtils", 2)) {
            Log.v("TransformationUtils", "requested target size larger or equal to input, returning input");
        }
        return bitmap;
    }

    public static void setAlpha(Bitmap bitmap, Bitmap bitmap2) {
        bitmap2.setHasAlpha(bitmap.hasAlpha());
    }

    public static Bitmap rotateImageExif(BitmapPool bitmapPool, Bitmap bitmap, int i) {
        if (!isExifOrientationRequired(i)) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        initializeMatrixForRotation(i, matrix);
        RectF rectF = new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight());
        matrix.mapRect(rectF);
        Bitmap bitmap2 = bitmapPool.get(Math.round(rectF.width()), Math.round(rectF.height()), getSafeConfig(bitmap));
        matrix.postTranslate(-rectF.left, -rectF.top);
        applyMatrix(bitmap, bitmap2, matrix);
        return bitmap2;
    }

    public static Bitmap circleCrop(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        i = Math.min(i, i2);
        float f = (float) i;
        float f2 = f / 2.0f;
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        float max = Math.max(f / width, f / height);
        width *= max;
        max *= height;
        height = (f - width) / 2.0f;
        f = (f - max) / 2.0f;
        RectF rectF = new RectF(height, f, width + height, max + f);
        Bitmap alphaSafeBitmap = getAlphaSafeBitmap(bitmapPool, bitmap);
        Bitmap bitmap2 = bitmapPool.get(i, i, Config.ARGB_8888);
        bitmap2.setHasAlpha(true);
        BITMAP_DRAWABLE_LOCK.lock();
        Object bitmap3;
        try {
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawCircle(f2, f2, f2, CIRCLE_CROP_SHAPE_PAINT);
            canvas.drawBitmap(alphaSafeBitmap, null, rectF, CIRCLE_CROP_BITMAP_PAINT);
            clear(canvas);
            if (!alphaSafeBitmap.equals(bitmap3)) {
                bitmapPool.put(alphaSafeBitmap);
            }
            return bitmap2;
        } finally {
            bitmap3 = BITMAP_DRAWABLE_LOCK;
            bitmap3.unlock();
        }
    }

    private static Bitmap getAlphaSafeBitmap(BitmapPool bitmapPool, Bitmap bitmap) {
        if (Config.ARGB_8888.equals(bitmap.getConfig())) {
            return bitmap;
        }
        Bitmap bitmap2 = bitmapPool.get(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        new Canvas(bitmap2).drawBitmap(bitmap, 0.0f, 0.0f, null);
        return bitmap2;
    }

    public static Bitmap roundedCorners(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2, int i3) {
        Preconditions.checkArgument(i > 0, "width must be greater than 0.");
        Preconditions.checkArgument(i2 > 0, "height must be greater than 0.");
        Preconditions.checkArgument(i3 > 0, "roundingRadius must be greater than 0.");
        Bitmap alphaSafeBitmap = getAlphaSafeBitmap(bitmapPool, bitmap);
        Bitmap bitmap2 = bitmapPool.get(i, i2, Config.ARGB_8888);
        bitmap2.setHasAlpha(true);
        BitmapShader bitmapShader = new BitmapShader(alphaSafeBitmap, TileMode.CLAMP, TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);
        RectF rectF = new RectF(0.0f, 0.0f, (float) bitmap2.getWidth(), (float) bitmap2.getHeight());
        BITMAP_DRAWABLE_LOCK.lock();
        Object bitmap3;
        try {
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawColor(0, Mode.CLEAR);
            float f = (float) i3;
            canvas.drawRoundRect(rectF, f, f, paint);
            clear(canvas);
            if (!alphaSafeBitmap.equals(bitmap3)) {
                bitmapPool.put(alphaSafeBitmap);
            }
            return bitmap2;
        } finally {
            bitmap3 = BITMAP_DRAWABLE_LOCK;
            bitmap3.unlock();
        }
    }

    private static void clear(Canvas canvas) {
        canvas.setBitmap(null);
    }

    private static Config getSafeConfig(Bitmap bitmap) {
        return bitmap.getConfig() != null ? bitmap.getConfig() : Config.ARGB_8888;
    }

    private static void applyMatrix(Bitmap bitmap, Bitmap bitmap2, Matrix matrix) {
        BITMAP_DRAWABLE_LOCK.lock();
        try {
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawBitmap(bitmap, matrix, DEFAULT_PAINT);
            clear(canvas);
        } finally {
            BITMAP_DRAWABLE_LOCK.unlock();
        }
    }

    static void initializeMatrixForRotation(int i, Matrix matrix) {
        switch (i) {
            case 2:
                matrix.setScale(-1.0f, 1.0f);
                return;
            case 3:
                matrix.setRotate(180.0f);
                return;
            case 4:
                matrix.setRotate(180.0f);
                matrix.postScale(-1.0f, 1.0f);
                return;
            case 5:
                matrix.setRotate(90.0f);
                matrix.postScale(-1.0f, 1.0f);
                return;
            case 6:
                matrix.setRotate(90.0f);
                return;
            case 7:
                matrix.setRotate(-90.0f);
                matrix.postScale(-1.0f, 1.0f);
                return;
            case 8:
                matrix.setRotate(-90.0f);
                return;
            default:
                return;
        }
    }
}
