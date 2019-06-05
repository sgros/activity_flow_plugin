// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.circularreveal.cardview;

import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.support.design.circularreveal.CircularRevealHelper;
import android.support.design.circularreveal.CircularRevealWidget;
import android.support.v7.widget.CardView;

public class CircularRevealCardView extends CardView implements CircularRevealWidget
{
    private final CircularRevealHelper helper;
    
    public void actualDraw(final Canvas canvas) {
        super.draw(canvas);
    }
    
    public boolean actualIsOpaque() {
        return super.isOpaque();
    }
    
    @Override
    public void buildCircularRevealCache() {
        this.helper.buildCircularRevealCache();
    }
    
    @Override
    public void destroyCircularRevealCache() {
        this.helper.destroyCircularRevealCache();
    }
    
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
    
    @Override
    public int getCircularRevealScrimColor() {
        return this.helper.getCircularRevealScrimColor();
    }
    
    @Override
    public RevealInfo getRevealInfo() {
        return this.helper.getRevealInfo();
    }
    
    public boolean isOpaque() {
        if (this.helper != null) {
            return this.helper.isOpaque();
        }
        return super.isOpaque();
    }
    
    @Override
    public void setCircularRevealOverlayDrawable(final Drawable circularRevealOverlayDrawable) {
        this.helper.setCircularRevealOverlayDrawable(circularRevealOverlayDrawable);
    }
    
    @Override
    public void setCircularRevealScrimColor(final int circularRevealScrimColor) {
        this.helper.setCircularRevealScrimColor(circularRevealScrimColor);
    }
    
    @Override
    public void setRevealInfo(final RevealInfo revealInfo) {
        this.helper.setRevealInfo(revealInfo);
    }
}
