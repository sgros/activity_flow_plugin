// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider;

import android.util.Log;
import java.util.Iterator;
import android.graphics.BitmapFactory$Options;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build$VERSION;
import android.graphics.drawable.Drawable;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import org.osmdroid.tileprovider.modules.ConfigurablePriorityThreadFactory;
import android.graphics.Bitmap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;

public class BitmapPool
{
    private static final BitmapPool sInstance;
    private final ExecutorService mExecutor;
    private final LinkedList<Bitmap> mPool;
    
    static {
        sInstance = new BitmapPool();
    }
    
    private BitmapPool() {
        this.mPool = new LinkedList<Bitmap>();
        this.mExecutor = Executors.newFixedThreadPool(1, new ConfigurablePriorityThreadFactory(1, BitmapPool.class.getName()));
    }
    
    public static BitmapPool getInstance() {
        return BitmapPool.sInstance;
    }
    
    private void syncRecycle(final Drawable drawable) {
        if (drawable == null) {
            return;
        }
        if (Build$VERSION.SDK_INT <= 10 && drawable instanceof BitmapDrawable) {
            final Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        if (drawable instanceof ReusableBitmapDrawable) {
            this.returnDrawableToPool((ReusableBitmapDrawable)drawable);
        }
    }
    
    public void applyReusableOptions(final BitmapFactory$Options bitmapFactory$Options, final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 11) {
            bitmapFactory$Options.inBitmap = this.obtainSizedBitmapFromPool(n, n2);
            bitmapFactory$Options.inSampleSize = 1;
            bitmapFactory$Options.inMutable = true;
        }
    }
    
    public void asyncRecycle(final Drawable drawable) {
        if (drawable == null) {
            return;
        }
        this.mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                BitmapPool.this.syncRecycle(drawable);
            }
        });
    }
    
    public Bitmap obtainSizedBitmapFromPool(final int n, final int n2) {
        synchronized (this.mPool) {
            if (this.mPool.isEmpty()) {
                return null;
            }
            for (final Bitmap bitmap : this.mPool) {
                if (bitmap.isRecycled()) {
                    this.mPool.remove(bitmap);
                    return this.obtainSizedBitmapFromPool(n, n2);
                }
                if (bitmap.getWidth() == n && bitmap.getHeight() == n2) {
                    this.mPool.remove(bitmap);
                    return bitmap;
                }
            }
            return null;
        }
    }
    
    public void returnDrawableToPool(final ReusableBitmapDrawable reusableBitmapDrawable) {
        final Bitmap tryRecycle = reusableBitmapDrawable.tryRecycle();
        if (tryRecycle != null && !tryRecycle.isRecycled() && tryRecycle.isMutable() && tryRecycle.getConfig() != null) {
            synchronized (this.mPool) {
                this.mPool.addLast(tryRecycle);
                return;
            }
        }
        if (tryRecycle != null) {
            Log.d("OsmDroid", "Rejected bitmap from being added to BitmapPool.");
        }
    }
}
