// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.tilesource;

import java.util.concurrent.Semaphore;

public abstract class OnlineTileSourceBase extends BitmapTileSourceBase
{
    private final String[] mBaseUrls;
    private final Semaphore mSemaphore;
    private final TileSourcePolicy mTileSourcePolicy;
    
    public OnlineTileSourceBase(final String s, final int n, final int n2, final int n3, final String s2, final String[] array) {
        this(s, n, n2, n3, s2, array, null);
    }
    
    public OnlineTileSourceBase(final String s, final int n, final int n2, final int n3, final String s2, final String[] array, final String s3) {
        this(s, n, n2, n3, s2, array, s3, new TileSourcePolicy());
    }
    
    public OnlineTileSourceBase(final String s, final int n, final int n2, final int n3, final String s2, final String[] mBaseUrls, final String s3, final TileSourcePolicy mTileSourcePolicy) {
        super(s, n, n2, n3, s2, s3);
        this.mBaseUrls = mBaseUrls;
        this.mTileSourcePolicy = mTileSourcePolicy;
        if (this.mTileSourcePolicy.getMaxConcurrent() > 0) {
            this.mSemaphore = new Semaphore(this.mTileSourcePolicy.getMaxConcurrent(), true);
        }
        else {
            this.mSemaphore = null;
        }
    }
    
    public void acquire() throws InterruptedException {
        final Semaphore mSemaphore = this.mSemaphore;
        if (mSemaphore == null) {
            return;
        }
        mSemaphore.acquire();
    }
    
    public String getBaseUrl() {
        final String[] mBaseUrls = this.mBaseUrls;
        return mBaseUrls[super.random.nextInt(mBaseUrls.length)];
    }
    
    public TileSourcePolicy getTileSourcePolicy() {
        return this.mTileSourcePolicy;
    }
    
    public abstract String getTileURLString(final long p0);
    
    public void release() {
        final Semaphore mSemaphore = this.mSemaphore;
        if (mSemaphore == null) {
            return;
        }
        mSemaphore.release();
    }
}
