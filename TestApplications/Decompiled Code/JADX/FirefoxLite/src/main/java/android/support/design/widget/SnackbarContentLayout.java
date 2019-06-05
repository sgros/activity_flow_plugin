package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.C0028R;
import android.support.design.snackbar.ContentViewCallback;
import android.support.p001v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SnackbarContentLayout extends LinearLayout implements ContentViewCallback {
    private Button actionView;
    private int maxInlineActionWidth;
    private int maxWidth;
    private TextView messageView;

    public SnackbarContentLayout(Context context) {
        this(context, null);
    }

    public SnackbarContentLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0028R.styleable.SnackbarLayout);
        this.maxWidth = obtainStyledAttributes.getDimensionPixelSize(C0028R.styleable.SnackbarLayout_android_maxWidth, -1);
        this.maxInlineActionWidth = obtainStyledAttributes.getDimensionPixelSize(C0028R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
        obtainStyledAttributes.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.messageView = (TextView) findViewById(C0028R.C0027id.snackbar_text);
        this.actionView = (Button) findViewById(C0028R.C0027id.snackbar_action);
    }

    public TextView getMessageView() {
        return this.messageView;
    }

    public Button getActionView() {
        return this.actionView;
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:15:0x0055, code skipped:
            if (updateViewsWithinLayout(1, r0, r0 - r1) != false) goto L_0x0064;
     */
    /* JADX WARNING: Missing block: B:19:0x0060, code skipped:
            if (updateViewsWithinLayout(0, r0, r0) != false) goto L_0x0064;
     */
    public void onMeasure(int r8, int r9) {
        /*
        r7 = this;
        super.onMeasure(r8, r9);
        r0 = r7.maxWidth;
        if (r0 <= 0) goto L_0x001a;
    L_0x0007:
        r0 = r7.getMeasuredWidth();
        r1 = r7.maxWidth;
        if (r0 <= r1) goto L_0x001a;
    L_0x000f:
        r8 = r7.maxWidth;
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r8, r0);
        super.onMeasure(r8, r9);
    L_0x001a:
        r0 = r7.getResources();
        r1 = android.support.design.C0028R.dimen.design_snackbar_padding_vertical_2lines;
        r0 = r0.getDimensionPixelSize(r1);
        r1 = r7.getResources();
        r2 = android.support.design.C0028R.dimen.design_snackbar_padding_vertical;
        r1 = r1.getDimensionPixelSize(r2);
        r2 = r7.messageView;
        r2 = r2.getLayout();
        r2 = r2.getLineCount();
        r3 = 0;
        r4 = 1;
        if (r2 <= r4) goto L_0x003e;
    L_0x003c:
        r2 = 1;
        goto L_0x003f;
    L_0x003e:
        r2 = 0;
    L_0x003f:
        if (r2 == 0) goto L_0x0058;
    L_0x0041:
        r5 = r7.maxInlineActionWidth;
        if (r5 <= 0) goto L_0x0058;
    L_0x0045:
        r5 = r7.actionView;
        r5 = r5.getMeasuredWidth();
        r6 = r7.maxInlineActionWidth;
        if (r5 <= r6) goto L_0x0058;
    L_0x004f:
        r1 = r0 - r1;
        r0 = r7.updateViewsWithinLayout(r4, r0, r1);
        if (r0 == 0) goto L_0x0063;
    L_0x0057:
        goto L_0x0064;
    L_0x0058:
        if (r2 == 0) goto L_0x005b;
    L_0x005a:
        goto L_0x005c;
    L_0x005b:
        r0 = r1;
    L_0x005c:
        r0 = r7.updateViewsWithinLayout(r3, r0, r0);
        if (r0 == 0) goto L_0x0063;
    L_0x0062:
        goto L_0x0064;
    L_0x0063:
        r4 = 0;
    L_0x0064:
        if (r4 == 0) goto L_0x0069;
    L_0x0066:
        super.onMeasure(r8, r9);
    L_0x0069:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.design.widget.SnackbarContentLayout.onMeasure(int, int):void");
    }

    private boolean updateViewsWithinLayout(int i, int i2, int i3) {
        boolean z;
        if (i != getOrientation()) {
            setOrientation(i);
            z = true;
        } else {
            z = false;
        }
        if (this.messageView.getPaddingTop() == i2 && this.messageView.getPaddingBottom() == i3) {
            return z;
        }
        updateTopBottomPadding(this.messageView, i2, i3);
        return true;
    }

    private static void updateTopBottomPadding(View view, int i, int i2) {
        if (ViewCompat.isPaddingRelative(view)) {
            ViewCompat.setPaddingRelative(view, ViewCompat.getPaddingStart(view), i, ViewCompat.getPaddingEnd(view), i2);
        } else {
            view.setPadding(view.getPaddingLeft(), i, view.getPaddingRight(), i2);
        }
    }

    public void animateContentIn(int i, int i2) {
        this.messageView.setAlpha(0.0f);
        long j = (long) i2;
        long j2 = (long) i;
        this.messageView.animate().alpha(1.0f).setDuration(j).setStartDelay(j2).start();
        if (this.actionView.getVisibility() == 0) {
            this.actionView.setAlpha(0.0f);
            this.actionView.animate().alpha(1.0f).setDuration(j).setStartDelay(j2).start();
        }
    }

    public void animateContentOut(int i, int i2) {
        this.messageView.setAlpha(1.0f);
        long j = (long) i2;
        long j2 = (long) i;
        this.messageView.animate().alpha(0.0f).setDuration(j).setStartDelay(j2).start();
        if (this.actionView.getVisibility() == 0) {
            this.actionView.setAlpha(1.0f);
            this.actionView.animate().alpha(0.0f).setDuration(j).setStartDelay(j2).start();
        }
    }
}
