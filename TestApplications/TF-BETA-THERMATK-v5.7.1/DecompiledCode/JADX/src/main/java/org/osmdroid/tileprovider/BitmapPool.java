package org.osmdroid.tileprovider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Log;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.osmdroid.tileprovider.modules.ConfigurablePriorityThreadFactory;

public class BitmapPool {
    private static final BitmapPool sInstance = new BitmapPool();
    private final ExecutorService mExecutor = Executors.newFixedThreadPool(1, new ConfigurablePriorityThreadFactory(1, BitmapPool.class.getName()));
    private final LinkedList<Bitmap> mPool = new LinkedList();

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:27:0x0049 in {6, 14, 21, 23, 26} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public android.graphics.Bitmap obtainSizedBitmapFromPool(int r6, int r7) {
        /*
        r5 = this;
        r0 = r5.mPool;
        monitor-enter(r0);
        r1 = r5.mPool;	 Catch:{ all -> 0x0046 }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x0046 }
        r2 = 0;	 Catch:{ all -> 0x0046 }
        if (r1 == 0) goto L_0x000e;	 Catch:{ all -> 0x0046 }
        monitor-exit(r0);	 Catch:{ all -> 0x0046 }
        return r2;	 Catch:{ all -> 0x0046 }
        r1 = r5.mPool;	 Catch:{ all -> 0x0046 }
        r1 = r1.iterator();	 Catch:{ all -> 0x0046 }
        r3 = r1.hasNext();	 Catch:{ all -> 0x0046 }
        if (r3 == 0) goto L_0x0044;	 Catch:{ all -> 0x0046 }
        r3 = r1.next();	 Catch:{ all -> 0x0046 }
        r3 = (android.graphics.Bitmap) r3;	 Catch:{ all -> 0x0046 }
        r4 = r3.isRecycled();	 Catch:{ all -> 0x0046 }
        if (r4 == 0) goto L_0x0031;	 Catch:{ all -> 0x0046 }
        r1 = r5.mPool;	 Catch:{ all -> 0x0046 }
        r1.remove(r3);	 Catch:{ all -> 0x0046 }
        r6 = r5.obtainSizedBitmapFromPool(r6, r7);	 Catch:{ all -> 0x0046 }
        monitor-exit(r0);	 Catch:{ all -> 0x0046 }
        return r6;	 Catch:{ all -> 0x0046 }
        r4 = r3.getWidth();	 Catch:{ all -> 0x0046 }
        if (r4 != r6) goto L_0x0014;	 Catch:{ all -> 0x0046 }
        r4 = r3.getHeight();	 Catch:{ all -> 0x0046 }
        if (r4 != r7) goto L_0x0014;	 Catch:{ all -> 0x0046 }
        r6 = r5.mPool;	 Catch:{ all -> 0x0046 }
        r6.remove(r3);	 Catch:{ all -> 0x0046 }
        monitor-exit(r0);	 Catch:{ all -> 0x0046 }
        return r3;	 Catch:{ all -> 0x0046 }
        monitor-exit(r0);	 Catch:{ all -> 0x0046 }
        return r2;	 Catch:{ all -> 0x0046 }
        r6 = move-exception;	 Catch:{ all -> 0x0046 }
        monitor-exit(r0);	 Catch:{ all -> 0x0046 }
        throw r6;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.BitmapPool.obtainSizedBitmapFromPool(int, int):android.graphics.Bitmap");
    }

    private BitmapPool() {
    }

    public static BitmapPool getInstance() {
        return sInstance;
    }

    public void returnDrawableToPool(ReusableBitmapDrawable reusableBitmapDrawable) {
        Bitmap tryRecycle = reusableBitmapDrawable.tryRecycle();
        if (tryRecycle != null && !tryRecycle.isRecycled() && tryRecycle.isMutable() && tryRecycle.getConfig() != null) {
            synchronized (this.mPool) {
                this.mPool.addLast(tryRecycle);
            }
        } else if (tryRecycle != null) {
            Log.d("OsmDroid", "Rejected bitmap from being added to BitmapPool.");
        }
    }

    public void applyReusableOptions(Options options, int i, int i2) {
        if (VERSION.SDK_INT >= 11) {
            options.inBitmap = obtainSizedBitmapFromPool(i, i2);
            options.inSampleSize = 1;
            options.inMutable = true;
        }
    }

    public void asyncRecycle(final Drawable drawable) {
        if (drawable != null) {
            this.mExecutor.execute(new Runnable() {
                public void run() {
                    BitmapPool.this.syncRecycle(drawable);
                }
            });
        }
    }

    private void syncRecycle(Drawable drawable) {
        if (drawable != null) {
            if (VERSION.SDK_INT <= 10 && (drawable instanceof BitmapDrawable)) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
            if (drawable instanceof ReusableBitmapDrawable) {
                returnDrawableToPool((ReusableBitmapDrawable) drawable);
            }
        }
    }
}
