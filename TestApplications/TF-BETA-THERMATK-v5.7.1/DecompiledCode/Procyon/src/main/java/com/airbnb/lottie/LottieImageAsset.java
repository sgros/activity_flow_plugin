// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import android.graphics.Bitmap;

public class LottieImageAsset
{
    private Bitmap bitmap;
    private final String dirName;
    private final String fileName;
    private final int height;
    private final String id;
    private final int width;
    
    public LottieImageAsset(final int width, final int height, final String id, final String fileName, final String dirName) {
        this.width = width;
        this.height = height;
        this.id = id;
        this.fileName = fileName;
        this.dirName = dirName;
    }
    
    public Bitmap getBitmap() {
        return this.bitmap;
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setBitmap(final Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
