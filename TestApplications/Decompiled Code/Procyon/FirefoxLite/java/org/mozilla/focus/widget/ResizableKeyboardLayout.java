// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.content.res.TypedArray;
import android.view.ViewGroup$LayoutParams;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.WindowInsets;
import android.view.View$OnApplyWindowInsetsListener;
import org.mozilla.focus.R;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.support.design.widget.CoordinatorLayout;

public class ResizableKeyboardLayout extends CoordinatorLayout
{
    private final int idOfViewToHide;
    private int marginBottom;
    private View viewToHide;
    
    public ResizableKeyboardLayout(final Context context) {
        this(context, null);
    }
    
    public ResizableKeyboardLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public ResizableKeyboardLayout(Context obtainStyledAttributes, final AttributeSet set, final int n) {
        super(obtainStyledAttributes, set, n);
        obtainStyledAttributes = (Context)this.getContext().getTheme().obtainStyledAttributes(set, R.styleable.ResizableKeyboardLayout, 0, 0);
        try {
            this.idOfViewToHide = ((TypedArray)obtainStyledAttributes).getResourceId(0, -1);
            ((TypedArray)obtainStyledAttributes).recycle();
            this.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new _$$Lambda$ResizableKeyboardLayout$H8zzoWULdCUNHnihVFXycNJ6TkA(this));
        }
        finally {
            ((TypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.idOfViewToHide != -1) {
            this.viewToHide = this.findViewById(this.idOfViewToHide);
        }
    }
    
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.viewToHide = null;
    }
    
    public void setLayoutParams(final ViewGroup$LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
        if (layoutParams instanceof ViewGroup$MarginLayoutParams) {
            this.marginBottom = ((ViewGroup$MarginLayoutParams)layoutParams).bottomMargin;
        }
    }
}
