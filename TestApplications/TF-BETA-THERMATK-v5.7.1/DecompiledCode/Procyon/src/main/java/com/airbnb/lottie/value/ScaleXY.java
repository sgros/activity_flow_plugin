// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.value;

public class ScaleXY
{
    private float scaleX;
    private float scaleY;
    
    public ScaleXY() {
        this(1.0f, 1.0f);
    }
    
    public ScaleXY(final float scaleX, final float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    public boolean equals(final float n, final float n2) {
        return this.scaleX == n && this.scaleY == n2;
    }
    
    public float getScaleX() {
        return this.scaleX;
    }
    
    public float getScaleY() {
        return this.scaleY;
    }
    
    public void set(final float scaleX, final float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getScaleX());
        sb.append("x");
        sb.append(this.getScaleY());
        return sb.toString();
    }
}
