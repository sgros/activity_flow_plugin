// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.graphics.ColorFilter;
import android.graphics.PorterDuff$Mode;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.Resources;
import android.graphics.Region;
import android.graphics.Rect;
import android.graphics.drawable.Drawable$ConstantState;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class DrawableWrapper extends Drawable
{
    private final Drawable mWrapped;
    
    public DrawableWrapper(final Drawable mWrapped) {
        this.mWrapped = mWrapped;
    }
    
    public void draw(final Canvas canvas) {
        this.mWrapped.draw(canvas);
    }
    
    public int getChangingConfigurations() {
        return this.mWrapped.getChangingConfigurations();
    }
    
    public Drawable$ConstantState getConstantState() {
        return this.mWrapped.getConstantState();
    }
    
    public Drawable getCurrent() {
        return this.mWrapped.getCurrent();
    }
    
    public int getIntrinsicHeight() {
        return this.mWrapped.getIntrinsicHeight();
    }
    
    public int getIntrinsicWidth() {
        return this.mWrapped.getIntrinsicWidth();
    }
    
    public int getMinimumHeight() {
        return this.mWrapped.getMinimumHeight();
    }
    
    public int getMinimumWidth() {
        return this.mWrapped.getMinimumWidth();
    }
    
    public int getOpacity() {
        return this.mWrapped.getOpacity();
    }
    
    public boolean getPadding(final Rect rect) {
        return this.mWrapped.getPadding(rect);
    }
    
    public int[] getState() {
        return this.mWrapped.getState();
    }
    
    public Region getTransparentRegion() {
        return this.mWrapped.getTransparentRegion();
    }
    
    public Drawable getWrappedDrawable() {
        return this.mWrapped;
    }
    
    public void inflate(final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set) throws XmlPullParserException, IOException {
        this.mWrapped.inflate(resources, xmlPullParser, set);
    }
    
    public boolean isStateful() {
        return this.mWrapped.isStateful();
    }
    
    public void jumpToCurrentState() {
        this.mWrapped.jumpToCurrentState();
    }
    
    public Drawable mutate() {
        return this.mWrapped.mutate();
    }
    
    protected void onBoundsChange(final Rect bounds) {
        this.mWrapped.setBounds(bounds);
    }
    
    protected boolean onLevelChange(final int level) {
        return this.mWrapped.setLevel(level);
    }
    
    protected boolean onStateChange(final int[] state) {
        return this.mWrapped.setState(state);
    }
    
    public void scheduleSelf(final Runnable runnable, final long n) {
        this.mWrapped.scheduleSelf(runnable, n);
    }
    
    public void setAlpha(final int alpha) {
        this.mWrapped.setAlpha(alpha);
    }
    
    public void setChangingConfigurations(final int changingConfigurations) {
        this.mWrapped.setChangingConfigurations(changingConfigurations);
    }
    
    public void setColorFilter(final int n, final PorterDuff$Mode porterDuff$Mode) {
        this.mWrapped.setColorFilter(n, porterDuff$Mode);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mWrapped.setColorFilter(colorFilter);
    }
    
    public void setFilterBitmap(final boolean filterBitmap) {
        this.mWrapped.setFilterBitmap(filterBitmap);
    }
    
    public boolean setVisible(final boolean b, final boolean b2) {
        return this.mWrapped.setVisible(b, b2);
    }
    
    public void unscheduleSelf(final Runnable runnable) {
        this.mWrapped.unscheduleSelf(runnable);
    }
}
