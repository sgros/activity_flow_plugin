package org.mozilla.focus.widget;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class DrawableWrapper extends Drawable {
    private final Drawable mWrapped;

    public DrawableWrapper(Drawable drawable) {
        this.mWrapped = drawable;
    }

    public Drawable getWrappedDrawable() {
        return this.mWrapped;
    }

    public void draw(Canvas canvas) {
        this.mWrapped.draw(canvas);
    }

    public int getChangingConfigurations() {
        return this.mWrapped.getChangingConfigurations();
    }

    public ConstantState getConstantState() {
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

    public boolean getPadding(Rect rect) {
        return this.mWrapped.getPadding(rect);
    }

    public int[] getState() {
        return this.mWrapped.getState();
    }

    public Region getTransparentRegion() {
        return this.mWrapped.getTransparentRegion();
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        this.mWrapped.inflate(resources, xmlPullParser, attributeSet);
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

    public void setAlpha(int i) {
        this.mWrapped.setAlpha(i);
    }

    public void scheduleSelf(Runnable runnable, long j) {
        this.mWrapped.scheduleSelf(runnable, j);
    }

    public void setChangingConfigurations(int i) {
        this.mWrapped.setChangingConfigurations(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mWrapped.setColorFilter(colorFilter);
    }

    public void setColorFilter(int i, Mode mode) {
        this.mWrapped.setColorFilter(i, mode);
    }

    public void setFilterBitmap(boolean z) {
        this.mWrapped.setFilterBitmap(z);
    }

    public boolean setVisible(boolean z, boolean z2) {
        return this.mWrapped.setVisible(z, z2);
    }

    public void unscheduleSelf(Runnable runnable) {
        this.mWrapped.unscheduleSelf(runnable);
    }

    /* Access modifiers changed, original: protected */
    public void onBoundsChange(Rect rect) {
        this.mWrapped.setBounds(rect);
    }

    /* Access modifiers changed, original: protected */
    public boolean onLevelChange(int i) {
        return this.mWrapped.setLevel(i);
    }

    /* Access modifiers changed, original: protected */
    public boolean onStateChange(int[] iArr) {
        return this.mWrapped.setState(iArr);
    }
}
