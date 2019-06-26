// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable$ConstantState;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable$Callback;
import android.graphics.drawable.Drawable;

public class CombinedDrawable extends Drawable implements Drawable$Callback
{
    private int backHeight;
    private int backWidth;
    private Drawable background;
    private boolean fullSize;
    private Drawable icon;
    private int iconHeight;
    private int iconWidth;
    private int left;
    private int offsetX;
    private int offsetY;
    private int top;
    
    public CombinedDrawable(final Drawable background, final Drawable icon) {
        this.background = background;
        this.icon = icon;
        if (icon != null) {
            icon.setCallback((Drawable$Callback)this);
        }
    }
    
    public CombinedDrawable(final Drawable background, final Drawable icon, final int left, final int top) {
        this.background = background;
        this.icon = icon;
        this.left = left;
        this.top = top;
        if (icon != null) {
            icon.setCallback((Drawable$Callback)this);
        }
    }
    
    public void draw(final Canvas canvas) {
        this.background.setBounds(this.getBounds());
        this.background.draw(canvas);
        final Drawable icon = this.icon;
        if (icon != null) {
            if (this.fullSize) {
                icon.setBounds(this.getBounds());
            }
            else if (this.iconWidth != 0) {
                final int n = this.getBounds().centerX() - this.iconWidth / 2 + this.left + this.offsetX;
                final int centerY = this.getBounds().centerY();
                final int iconHeight = this.iconHeight;
                final int n2 = centerY - iconHeight / 2 + this.top + this.offsetY;
                this.icon.setBounds(n, n2, this.iconWidth + n, iconHeight + n2);
            }
            else {
                final int n3 = this.getBounds().centerX() - this.icon.getIntrinsicWidth() / 2 + this.left;
                final int n4 = this.getBounds().centerY() - this.icon.getIntrinsicHeight() / 2 + this.top;
                final Drawable icon2 = this.icon;
                icon2.setBounds(n3, n4, icon2.getIntrinsicWidth() + n3, this.icon.getIntrinsicHeight() + n4);
            }
            this.icon.draw(canvas);
        }
    }
    
    public Drawable getBackground() {
        return this.background;
    }
    
    public Drawable$ConstantState getConstantState() {
        return this.icon.getConstantState();
    }
    
    public Drawable getIcon() {
        return this.icon;
    }
    
    public int getIntrinsicHeight() {
        int n = this.backHeight;
        if (n == 0) {
            n = this.background.getIntrinsicHeight();
        }
        return n;
    }
    
    public int getIntrinsicWidth() {
        int n = this.backWidth;
        if (n == 0) {
            n = this.background.getIntrinsicWidth();
        }
        return n;
    }
    
    public int getMinimumHeight() {
        int n = this.backHeight;
        if (n == 0) {
            n = this.background.getMinimumHeight();
        }
        return n;
    }
    
    public int getMinimumWidth() {
        int n = this.backWidth;
        if (n == 0) {
            n = this.background.getMinimumWidth();
        }
        return n;
    }
    
    public int getOpacity() {
        return this.icon.getOpacity();
    }
    
    public int[] getState() {
        return this.icon.getState();
    }
    
    public void invalidateDrawable(final Drawable drawable) {
        this.invalidateSelf();
    }
    
    public boolean isStateful() {
        return this.icon.isStateful();
    }
    
    public void jumpToCurrentState() {
        this.icon.jumpToCurrentState();
    }
    
    protected boolean onStateChange(final int[] array) {
        return true;
    }
    
    public void scheduleDrawable(final Drawable drawable, final Runnable runnable, final long n) {
        this.scheduleSelf(runnable, n);
    }
    
    public void setAlpha(final int n) {
        this.icon.setAlpha(n);
        this.background.setAlpha(n);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.icon.setColorFilter(colorFilter);
    }
    
    public void setCustomSize(final int backWidth, final int backHeight) {
        this.backWidth = backWidth;
        this.backHeight = backHeight;
    }
    
    public void setFullsize(final boolean fullSize) {
        this.fullSize = fullSize;
    }
    
    public void setIconOffset(final int offsetX, final int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
    
    public void setIconSize(final int iconWidth, final int iconHeight) {
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
    }
    
    public boolean setState(final int[] state) {
        this.icon.setState(state);
        return true;
    }
    
    public void unscheduleDrawable(final Drawable drawable, final Runnable runnable) {
        this.unscheduleSelf(runnable);
    }
}
