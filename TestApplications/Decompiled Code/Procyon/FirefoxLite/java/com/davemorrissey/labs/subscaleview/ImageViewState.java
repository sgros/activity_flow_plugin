// 
// Decompiled by Procyon v0.5.34
// 

package com.davemorrissey.labs.subscaleview;

import android.graphics.PointF;
import java.io.Serializable;

public class ImageViewState implements Serializable
{
    public static final ImageViewState ALIGN_TOP;
    private float centerX;
    private float centerY;
    private int orientation;
    private float scale;
    
    static {
        ALIGN_TOP = new ImageViewState(0.0f, new PointF(0.0f, 0.0f), 0);
    }
    
    public ImageViewState(final float scale, final PointF pointF, final int orientation) {
        this.scale = scale;
        this.centerX = pointF.x;
        this.centerY = pointF.y;
        this.orientation = orientation;
    }
    
    public PointF getCenter() {
        return new PointF(this.centerX, this.centerY);
    }
    
    public int getOrientation() {
        return this.orientation;
    }
    
    public float getScale() {
        return this.scale;
    }
}
