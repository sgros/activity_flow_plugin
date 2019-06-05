// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.circularreveal;

import android.graphics.drawable.Drawable;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.widget.FrameLayout;

public class CircularRevealFrameLayout extends FrameLayout implements CircularRevealWidget
{
    private final CircularRevealHelper helper;
    
    public void actualDraw(final Canvas canvas) {
        super.draw(canvas);
    }
    
    public boolean actualIsOpaque() {
        return super.isOpaque();
    }
    
    public void buildCircularRevealCache() {
        this.helper.buildCircularRevealCache();
    }
    
    public void destroyCircularRevealCache() {
        this.helper.destroyCircularRevealCache();
    }
    
    @SuppressLint({ "MissingSuperCall" })
    public void draw(final Canvas canvas) {
        if (this.helper != null) {
            this.helper.draw(canvas);
        }
        else {
            super.draw(canvas);
        }
    }
    
    public Drawable getCircularRevealOverlayDrawable() {
        return this.helper.getCircularRevealOverlayDrawable();
    }
    
    public int getCircularRevealScrimColor() {
        return this.helper.getCircularRevealScrimColor();
    }
    
    public RevealInfo getRevealInfo() {
        return this.helper.getRevealInfo();
    }
    
    public boolean isOpaque() {
        if (this.helper != null) {
            return this.helper.isOpaque();
        }
        return super.isOpaque();
    }
    
    public void setCircularRevealOverlayDrawable(final Drawable circularRevealOverlayDrawable) {
        this.helper.setCircularRevealOverlayDrawable(circularRevealOverlayDrawable);
    }
    
    public void setCircularRevealScrimColor(final int circularRevealScrimColor) {
        this.helper.setCircularRevealScrimColor(circularRevealScrimColor);
    }
    
    public void setRevealInfo(final RevealInfo revealInfo) {
        this.helper.setRevealInfo(revealInfo);
    }
}
