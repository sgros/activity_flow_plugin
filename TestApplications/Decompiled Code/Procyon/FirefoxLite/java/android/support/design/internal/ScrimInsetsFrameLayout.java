// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.internal;

import android.graphics.drawable.Drawable$Callback;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.view.View;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;

public class ScrimInsetsFrameLayout extends FrameLayout
{
    Drawable insetForeground;
    Rect insets;
    private Rect tempRect;
    
    public ScrimInsetsFrameLayout(final Context context) {
        this(context, null);
    }
    
    public ScrimInsetsFrameLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public ScrimInsetsFrameLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.tempRect = new Rect();
        final TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, set, R.styleable.ScrimInsetsFrameLayout, n, R.style.Widget_Design_ScrimInsetsFrameLayout, new int[0]);
        this.insetForeground = obtainStyledAttributes.getDrawable(R.styleable.ScrimInsetsFrameLayout_insetForeground);
        obtainStyledAttributes.recycle();
        this.setWillNotDraw(true);
        ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
                if (ScrimInsetsFrameLayout.this.insets == null) {
                    ScrimInsetsFrameLayout.this.insets = new Rect();
                }
                ScrimInsetsFrameLayout.this.insets.set(windowInsetsCompat.getSystemWindowInsetLeft(), windowInsetsCompat.getSystemWindowInsetTop(), windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                ScrimInsetsFrameLayout.this.onInsetsChanged(windowInsetsCompat);
                ScrimInsetsFrameLayout.this.setWillNotDraw(!windowInsetsCompat.hasSystemWindowInsets() || ScrimInsetsFrameLayout.this.insetForeground == null);
                ViewCompat.postInvalidateOnAnimation((View)ScrimInsetsFrameLayout.this);
                return windowInsetsCompat.consumeSystemWindowInsets();
            }
        });
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        final int width = this.getWidth();
        final int height = this.getHeight();
        if (this.insets != null && this.insetForeground != null) {
            final int save = canvas.save();
            canvas.translate((float)this.getScrollX(), (float)this.getScrollY());
            this.tempRect.set(0, 0, width, this.insets.top);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(canvas);
            this.tempRect.set(0, height - this.insets.bottom, width, height);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(canvas);
            this.tempRect.set(0, this.insets.top, this.insets.left, height - this.insets.bottom);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(canvas);
            this.tempRect.set(width - this.insets.right, this.insets.top, width, height - this.insets.bottom);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(canvas);
            canvas.restoreToCount(save);
        }
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.insetForeground != null) {
            this.insetForeground.setCallback((Drawable$Callback)this);
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.insetForeground != null) {
            this.insetForeground.setCallback((Drawable$Callback)null);
        }
    }
    
    protected void onInsetsChanged(final WindowInsetsCompat windowInsetsCompat) {
    }
}
