// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.internal;

import android.view.View$MeasureSpec;
import android.view.ViewPropertyAnimator;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.content.res.TypedArray;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import android.widget.Button;
import android.support.annotation.RestrictTo;
import android.support.design.widget.BaseTransientBottomBar;
import android.widget.LinearLayout;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class SnackbarContentLayout extends LinearLayout implements ContentViewCallback
{
    private Button mActionView;
    private int mMaxInlineActionWidth;
    private int mMaxWidth;
    private TextView mMessageView;
    
    public SnackbarContentLayout(final Context context) {
        this(context, null);
    }
    
    public SnackbarContentLayout(final Context context, final AttributeSet set) {
        super(context, set);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.SnackbarLayout);
        this.mMaxWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
        this.mMaxInlineActionWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
        obtainStyledAttributes.recycle();
    }
    
    private static void updateTopBottomPadding(final View view, final int n, final int n2) {
        if (ViewCompat.isPaddingRelative(view)) {
            ViewCompat.setPaddingRelative(view, ViewCompat.getPaddingStart(view), n, ViewCompat.getPaddingEnd(view), n2);
        }
        else {
            view.setPadding(view.getPaddingLeft(), n, view.getPaddingRight(), n2);
        }
    }
    
    private boolean updateViewsWithinLayout(final int orientation, final int n, final int n2) {
        boolean b;
        if (orientation != this.getOrientation()) {
            this.setOrientation(orientation);
            b = true;
        }
        else {
            b = false;
        }
        if (this.mMessageView.getPaddingTop() != n || this.mMessageView.getPaddingBottom() != n2) {
            updateTopBottomPadding((View)this.mMessageView, n, n2);
            b = true;
        }
        return b;
    }
    
    public void animateContentIn(final int n, final int n2) {
        this.mMessageView.setAlpha(0.0f);
        final ViewPropertyAnimator alpha = this.mMessageView.animate().alpha(1.0f);
        final long n3 = n2;
        final ViewPropertyAnimator setDuration = alpha.setDuration(n3);
        final long n4 = n;
        setDuration.setStartDelay(n4).start();
        if (this.mActionView.getVisibility() == 0) {
            this.mActionView.setAlpha(0.0f);
            this.mActionView.animate().alpha(1.0f).setDuration(n3).setStartDelay(n4).start();
        }
    }
    
    public void animateContentOut(final int n, final int n2) {
        this.mMessageView.setAlpha(1.0f);
        final ViewPropertyAnimator alpha = this.mMessageView.animate().alpha(0.0f);
        final long n3 = n2;
        final ViewPropertyAnimator setDuration = alpha.setDuration(n3);
        final long n4 = n;
        setDuration.setStartDelay(n4).start();
        if (this.mActionView.getVisibility() == 0) {
            this.mActionView.setAlpha(1.0f);
            this.mActionView.animate().alpha(0.0f).setDuration(n3).setStartDelay(n4).start();
        }
    }
    
    public Button getActionView() {
        return this.mActionView;
    }
    
    public TextView getMessageView() {
        return this.mMessageView;
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mMessageView = (TextView)this.findViewById(R.id.snackbar_text);
        this.mActionView = (Button)this.findViewById(R.id.snackbar_action);
    }
    
    protected void onMeasure(int lineCount, final int n) {
        super.onMeasure(lineCount, n);
        int measureSpec = lineCount;
        if (this.mMaxWidth > 0) {
            measureSpec = lineCount;
            if (this.getMeasuredWidth() > this.mMaxWidth) {
                measureSpec = View$MeasureSpec.makeMeasureSpec(this.mMaxWidth, 1073741824);
                super.onMeasure(measureSpec, n);
            }
        }
        final int dimensionPixelSize = this.getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical_2lines);
        final int dimensionPixelSize2 = this.getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical);
        lineCount = this.mMessageView.getLayout().getLineCount();
        final int n2 = 1;
        if (lineCount > 1) {
            lineCount = 1;
        }
        else {
            lineCount = 0;
        }
        Label_0171: {
            if (lineCount != 0 && this.mMaxInlineActionWidth > 0 && this.mActionView.getMeasuredWidth() > this.mMaxInlineActionWidth) {
                if (this.updateViewsWithinLayout(1, dimensionPixelSize, dimensionPixelSize - dimensionPixelSize2)) {
                    lineCount = n2;
                    break Label_0171;
                }
            }
            else {
                if (lineCount != 0) {
                    lineCount = dimensionPixelSize;
                }
                else {
                    lineCount = dimensionPixelSize2;
                }
                if (this.updateViewsWithinLayout(0, lineCount, lineCount)) {
                    lineCount = n2;
                    break Label_0171;
                }
            }
            lineCount = 0;
        }
        if (lineCount != 0) {
            super.onMeasure(measureSpec, n);
        }
    }
}
