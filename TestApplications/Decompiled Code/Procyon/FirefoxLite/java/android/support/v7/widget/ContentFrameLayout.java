// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import android.view.View;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.content.Context;
import android.util.TypedValue;
import android.graphics.Rect;
import android.widget.FrameLayout;

public class ContentFrameLayout extends FrameLayout
{
    private OnAttachListener mAttachListener;
    private final Rect mDecorPadding;
    private TypedValue mFixedHeightMajor;
    private TypedValue mFixedHeightMinor;
    private TypedValue mFixedWidthMajor;
    private TypedValue mFixedWidthMinor;
    private TypedValue mMinWidthMajor;
    private TypedValue mMinWidthMinor;
    
    public ContentFrameLayout(final Context context) {
        this(context, null);
    }
    
    public ContentFrameLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public ContentFrameLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mDecorPadding = new Rect();
    }
    
    public void dispatchFitSystemWindows(final Rect rect) {
        this.fitSystemWindows(rect);
    }
    
    public TypedValue getFixedHeightMajor() {
        if (this.mFixedHeightMajor == null) {
            this.mFixedHeightMajor = new TypedValue();
        }
        return this.mFixedHeightMajor;
    }
    
    public TypedValue getFixedHeightMinor() {
        if (this.mFixedHeightMinor == null) {
            this.mFixedHeightMinor = new TypedValue();
        }
        return this.mFixedHeightMinor;
    }
    
    public TypedValue getFixedWidthMajor() {
        if (this.mFixedWidthMajor == null) {
            this.mFixedWidthMajor = new TypedValue();
        }
        return this.mFixedWidthMajor;
    }
    
    public TypedValue getFixedWidthMinor() {
        if (this.mFixedWidthMinor == null) {
            this.mFixedWidthMinor = new TypedValue();
        }
        return this.mFixedWidthMinor;
    }
    
    public TypedValue getMinWidthMajor() {
        if (this.mMinWidthMajor == null) {
            this.mMinWidthMajor = new TypedValue();
        }
        return this.mMinWidthMajor;
    }
    
    public TypedValue getMinWidthMinor() {
        if (this.mMinWidthMinor == null) {
            this.mMinWidthMinor = new TypedValue();
        }
        return this.mMinWidthMinor;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mAttachListener != null) {
            this.mAttachListener.onAttachedFromWindow();
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mAttachListener != null) {
            this.mAttachListener.onDetachedFromWindow();
        }
    }
    
    protected void onMeasure(int measureSpec, int n) {
        final DisplayMetrics displayMetrics = this.getContext().getResources().getDisplayMetrics();
        final int widthPixels = displayMetrics.widthPixels;
        final int heightPixels = displayMetrics.heightPixels;
        final int n2 = 1;
        final boolean b = widthPixels < heightPixels;
        final int mode = View$MeasureSpec.getMode(measureSpec);
        final int mode2 = View$MeasureSpec.getMode(n);
        int measureSpec2 = 0;
        boolean b2 = false;
        Label_0198: {
            if (mode == Integer.MIN_VALUE) {
                TypedValue typedValue;
                if (b) {
                    typedValue = this.mFixedWidthMinor;
                }
                else {
                    typedValue = this.mFixedWidthMajor;
                }
                if (typedValue != null && typedValue.type != 0) {
                    int n3;
                    if (typedValue.type == 5) {
                        n3 = (int)typedValue.getDimension(displayMetrics);
                    }
                    else if (typedValue.type == 6) {
                        n3 = (int)typedValue.getFraction((float)displayMetrics.widthPixels, (float)displayMetrics.widthPixels);
                    }
                    else {
                        n3 = 0;
                    }
                    if (n3 > 0) {
                        measureSpec2 = View$MeasureSpec.makeMeasureSpec(Math.min(n3 - (this.mDecorPadding.left + this.mDecorPadding.right), View$MeasureSpec.getSize(measureSpec)), 1073741824);
                        b2 = true;
                        break Label_0198;
                    }
                }
            }
            b2 = false;
            measureSpec2 = measureSpec;
        }
        int measureSpec3 = n;
        if (mode2 == Integer.MIN_VALUE) {
            TypedValue typedValue2;
            if (b) {
                typedValue2 = this.mFixedHeightMajor;
            }
            else {
                typedValue2 = this.mFixedHeightMinor;
            }
            measureSpec3 = n;
            if (typedValue2 != null) {
                measureSpec3 = n;
                if (typedValue2.type != 0) {
                    if (typedValue2.type == 5) {
                        measureSpec = (int)typedValue2.getDimension(displayMetrics);
                    }
                    else if (typedValue2.type == 6) {
                        measureSpec = (int)typedValue2.getFraction((float)displayMetrics.heightPixels, (float)displayMetrics.heightPixels);
                    }
                    else {
                        measureSpec = 0;
                    }
                    measureSpec3 = n;
                    if (measureSpec > 0) {
                        measureSpec3 = View$MeasureSpec.makeMeasureSpec(Math.min(measureSpec - (this.mDecorPadding.top + this.mDecorPadding.bottom), View$MeasureSpec.getSize(n)), 1073741824);
                    }
                }
            }
        }
        super.onMeasure(measureSpec2, measureSpec3);
        final int measuredWidth = this.getMeasuredWidth();
        final int measureSpec4 = View$MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824);
        Label_0505: {
            if (!b2 && mode == Integer.MIN_VALUE) {
                TypedValue typedValue3;
                if (b) {
                    typedValue3 = this.mMinWidthMinor;
                }
                else {
                    typedValue3 = this.mMinWidthMajor;
                }
                if (typedValue3 != null && typedValue3.type != 0) {
                    if (typedValue3.type == 5) {
                        measureSpec = (int)typedValue3.getDimension(displayMetrics);
                    }
                    else if (typedValue3.type == 6) {
                        measureSpec = (int)typedValue3.getFraction((float)displayMetrics.widthPixels, (float)displayMetrics.widthPixels);
                    }
                    else {
                        measureSpec = 0;
                    }
                    n = measureSpec;
                    if (measureSpec > 0) {
                        n = measureSpec - (this.mDecorPadding.left + this.mDecorPadding.right);
                    }
                    if (measuredWidth < n) {
                        measureSpec = View$MeasureSpec.makeMeasureSpec(n, 1073741824);
                        n = n2;
                        break Label_0505;
                    }
                }
            }
            n = 0;
            measureSpec = measureSpec4;
        }
        if (n != 0) {
            super.onMeasure(measureSpec, measureSpec3);
        }
    }
    
    public void setAttachListener(final OnAttachListener mAttachListener) {
        this.mAttachListener = mAttachListener;
    }
    
    public void setDecorPadding(final int n, final int n2, final int n3, final int n4) {
        this.mDecorPadding.set(n, n2, n3, n4);
        if (ViewCompat.isLaidOut((View)this)) {
            this.requestLayout();
        }
    }
    
    public interface OnAttachListener
    {
        void onAttachedFromWindow();
        
        void onDetachedFromWindow();
    }
}
