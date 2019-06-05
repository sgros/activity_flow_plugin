package org.mozilla.focus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowInsets;
import org.mozilla.focus.C0427R;

public class ResizableKeyboardLayout extends CoordinatorLayout {
    private final int idOfViewToHide;
    private int marginBottom;
    private View viewToHide;

    public ResizableKeyboardLayout(Context context) {
        this(context, null);
    }

    public ResizableKeyboardLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ResizableKeyboardLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(attributeSet, C0427R.styleable.ResizableKeyboardLayout, 0, 0);
        try {
            this.idOfViewToHide = obtainStyledAttributes.getResourceId(0, -1);
            setOnApplyWindowInsetsListener(new C0565-$$Lambda$ResizableKeyboardLayout$H8zzoWULdCUNHnihVFXycNJ6TkA(this));
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public static /* synthetic */ WindowInsets lambda$new$0(ResizableKeyboardLayout resizableKeyboardLayout, View view, WindowInsets windowInsets) {
        int systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
        if (systemWindowInsetBottom != 0) {
            if (resizableKeyboardLayout.getPaddingBottom() != systemWindowInsetBottom) {
                resizableKeyboardLayout.setPadding(0, 0, 0, systemWindowInsetBottom);
                if (resizableKeyboardLayout.getLayoutParams() instanceof MarginLayoutParams) {
                    ((MarginLayoutParams) resizableKeyboardLayout.getLayoutParams()).bottomMargin = 0;
                }
                if (resizableKeyboardLayout.viewToHide != null) {
                    resizableKeyboardLayout.viewToHide.setVisibility(8);
                }
            }
        } else if (resizableKeyboardLayout.getPaddingBottom() != 0) {
            resizableKeyboardLayout.setPadding(0, 0, 0, 0);
            ((MarginLayoutParams) resizableKeyboardLayout.getLayoutParams()).bottomMargin = resizableKeyboardLayout.marginBottom;
            if (resizableKeyboardLayout.viewToHide != null) {
                resizableKeyboardLayout.viewToHide.setVisibility(0);
            }
        }
        return windowInsets;
    }

    public void setLayoutParams(LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
        if (layoutParams instanceof MarginLayoutParams) {
            this.marginBottom = ((MarginLayoutParams) layoutParams).bottomMargin;
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.idOfViewToHide != -1) {
            this.viewToHide = findViewById(this.idOfViewToHide);
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.viewToHide = null;
    }
}
