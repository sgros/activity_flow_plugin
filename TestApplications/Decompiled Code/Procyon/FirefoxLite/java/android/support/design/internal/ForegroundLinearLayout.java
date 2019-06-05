// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.internal;

import android.graphics.drawable.Drawable$Callback;
import android.annotation.TargetApi;
import android.view.Gravity;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;

public class ForegroundLinearLayout extends LinearLayoutCompat
{
    private Drawable foreground;
    boolean foregroundBoundsChanged;
    private int foregroundGravity;
    protected boolean mForegroundInPadding;
    private final Rect overlayBounds;
    private final Rect selfBounds;
    
    public ForegroundLinearLayout(final Context context) {
        this(context, null);
    }
    
    public ForegroundLinearLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public ForegroundLinearLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.selfBounds = new Rect();
        this.overlayBounds = new Rect();
        this.foregroundGravity = 119;
        this.mForegroundInPadding = true;
        this.foregroundBoundsChanged = false;
        final TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, set, R.styleable.ForegroundLinearLayout, n, 0, new int[0]);
        this.foregroundGravity = obtainStyledAttributes.getInt(R.styleable.ForegroundLinearLayout_android_foregroundGravity, this.foregroundGravity);
        final Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.ForegroundLinearLayout_android_foreground);
        if (drawable != null) {
            this.setForeground(drawable);
        }
        this.mForegroundInPadding = obtainStyledAttributes.getBoolean(R.styleable.ForegroundLinearLayout_foregroundInsidePadding, true);
        obtainStyledAttributes.recycle();
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        if (this.foreground != null) {
            final Drawable foreground = this.foreground;
            if (this.foregroundBoundsChanged) {
                this.foregroundBoundsChanged = false;
                final Rect selfBounds = this.selfBounds;
                final Rect overlayBounds = this.overlayBounds;
                final int n = this.getRight() - this.getLeft();
                final int n2 = this.getBottom() - this.getTop();
                if (this.mForegroundInPadding) {
                    selfBounds.set(0, 0, n, n2);
                }
                else {
                    selfBounds.set(this.getPaddingLeft(), this.getPaddingTop(), n - this.getPaddingRight(), n2 - this.getPaddingBottom());
                }
                Gravity.apply(this.foregroundGravity, foreground.getIntrinsicWidth(), foreground.getIntrinsicHeight(), selfBounds, overlayBounds);
                foreground.setBounds(overlayBounds);
            }
            foreground.draw(canvas);
        }
    }
    
    @TargetApi(21)
    public void drawableHotspotChanged(final float n, final float n2) {
        super.drawableHotspotChanged(n, n2);
        if (this.foreground != null) {
            this.foreground.setHotspot(n, n2);
        }
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.foreground != null && this.foreground.isStateful()) {
            this.foreground.setState(this.getDrawableState());
        }
    }
    
    public Drawable getForeground() {
        return this.foreground;
    }
    
    public int getForegroundGravity() {
        return this.foregroundGravity;
    }
    
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.foreground != null) {
            this.foreground.jumpToCurrentState();
        }
    }
    
    @Override
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.foregroundBoundsChanged |= b;
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        this.foregroundBoundsChanged = true;
    }
    
    public void setForeground(final Drawable foreground) {
        if (this.foreground != foreground) {
            if (this.foreground != null) {
                this.foreground.setCallback((Drawable$Callback)null);
                this.unscheduleDrawable(this.foreground);
            }
            if ((this.foreground = foreground) != null) {
                this.setWillNotDraw(false);
                foreground.setCallback((Drawable$Callback)this);
                if (foreground.isStateful()) {
                    foreground.setState(this.getDrawableState());
                }
                if (this.foregroundGravity == 119) {
                    foreground.getPadding(new Rect());
                }
            }
            else {
                this.setWillNotDraw(true);
            }
            this.requestLayout();
            this.invalidate();
        }
    }
    
    public void setForegroundGravity(int foregroundGravity) {
        if (this.foregroundGravity != foregroundGravity) {
            int n = foregroundGravity;
            if ((0x800007 & foregroundGravity) == 0x0) {
                n = (foregroundGravity | 0x800003);
            }
            foregroundGravity = n;
            if ((n & 0x70) == 0x0) {
                foregroundGravity = (n | 0x30);
            }
            this.foregroundGravity = foregroundGravity;
            if (this.foregroundGravity == 119 && this.foreground != null) {
                this.foreground.getPadding(new Rect());
            }
            this.requestLayout();
        }
    }
    
    protected boolean verifyDrawable(final Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.foreground;
    }
}
