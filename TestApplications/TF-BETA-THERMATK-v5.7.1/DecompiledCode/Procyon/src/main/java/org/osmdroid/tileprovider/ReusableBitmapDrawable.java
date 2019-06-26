// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider;

import android.graphics.Bitmap;

public class ReusableBitmapDrawable extends ExpirableBitmapDrawable
{
    private boolean mBitmapRecycled;
    private int mUsageRefCount;
    
    public ReusableBitmapDrawable(final Bitmap bitmap) {
        super(bitmap);
        this.mBitmapRecycled = false;
        this.mUsageRefCount = 0;
    }
    
    public void beginUsingDrawable() {
        synchronized (this) {
            ++this.mUsageRefCount;
        }
    }
    
    public void finishUsingDrawable() {
        synchronized (this) {
            --this.mUsageRefCount;
            if (this.mUsageRefCount >= 0) {
                return;
            }
            throw new IllegalStateException("Unbalanced endUsingDrawable() called.");
        }
    }
    
    public boolean isBitmapValid() {
        synchronized (this) {
            return !this.mBitmapRecycled;
        }
    }
    
    public Bitmap tryRecycle() {
        synchronized (this) {
            if (this.mUsageRefCount == 0) {
                this.mBitmapRecycled = true;
                return this.getBitmap();
            }
            return null;
        }
    }
}
