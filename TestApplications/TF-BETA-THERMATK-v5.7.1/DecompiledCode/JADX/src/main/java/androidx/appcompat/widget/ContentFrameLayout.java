package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

public class ContentFrameLayout extends FrameLayout {
    private OnAttachListener mAttachListener;
    private final Rect mDecorPadding;
    private TypedValue mFixedHeightMajor;
    private TypedValue mFixedHeightMinor;
    private TypedValue mFixedWidthMajor;
    private TypedValue mFixedWidthMinor;
    private TypedValue mMinWidthMajor;
    private TypedValue mMinWidthMinor;

    public interface OnAttachListener {
        void onAttachedFromWindow();

        void onDetachedFromWindow();
    }

    public ContentFrameLayout(Context context) {
        this(context, null);
    }

    public ContentFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ContentFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDecorPadding = new Rect();
    }

    public void dispatchFitSystemWindows(Rect rect) {
        fitSystemWindows(rect);
    }

    public void setAttachListener(OnAttachListener onAttachListener) {
        this.mAttachListener = onAttachListener;
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00d8  */
    /* JADX WARNING: Removed duplicated region for block: B:59:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00b0  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:59:? A:{SYNTHETIC, RETURN} */
    public void onMeasure(int r14, int r15) {
        /*
        r13 = this;
        r0 = r13.getContext();
        r0 = r0.getResources();
        r0 = r0.getDisplayMetrics();
        r1 = r0.widthPixels;
        r2 = r0.heightPixels;
        r3 = 1;
        r4 = 0;
        if (r1 >= r2) goto L_0x0016;
    L_0x0014:
        r1 = 1;
        goto L_0x0017;
    L_0x0016:
        r1 = 0;
    L_0x0017:
        r2 = android.view.View.MeasureSpec.getMode(r14);
        r5 = android.view.View.MeasureSpec.getMode(r15);
        r6 = 6;
        r7 = 5;
        r8 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r2 != r8) goto L_0x0061;
    L_0x0027:
        if (r1 == 0) goto L_0x002c;
    L_0x0029:
        r10 = r13.mFixedWidthMinor;
        goto L_0x002e;
    L_0x002c:
        r10 = r13.mFixedWidthMajor;
    L_0x002e:
        if (r10 == 0) goto L_0x0061;
    L_0x0030:
        r11 = r10.type;
        if (r11 == 0) goto L_0x0061;
    L_0x0034:
        if (r11 != r7) goto L_0x003c;
    L_0x0036:
        r10 = r10.getDimension(r0);
    L_0x003a:
        r10 = (int) r10;
        goto L_0x0048;
    L_0x003c:
        if (r11 != r6) goto L_0x0047;
    L_0x003e:
        r11 = r0.widthPixels;
        r12 = (float) r11;
        r11 = (float) r11;
        r10 = r10.getFraction(r12, r11);
        goto L_0x003a;
    L_0x0047:
        r10 = 0;
    L_0x0048:
        if (r10 <= 0) goto L_0x0061;
    L_0x004a:
        r11 = r13.mDecorPadding;
        r12 = r11.left;
        r11 = r11.right;
        r12 = r12 + r11;
        r10 = r10 - r12;
        r14 = android.view.View.MeasureSpec.getSize(r14);
        r14 = java.lang.Math.min(r10, r14);
        r14 = android.view.View.MeasureSpec.makeMeasureSpec(r14, r9);
        r10 = r14;
        r14 = 1;
        goto L_0x0063;
    L_0x0061:
        r10 = r14;
        r14 = 0;
    L_0x0063:
        if (r5 != r8) goto L_0x009c;
    L_0x0065:
        if (r1 == 0) goto L_0x006a;
    L_0x0067:
        r5 = r13.mFixedHeightMajor;
        goto L_0x006c;
    L_0x006a:
        r5 = r13.mFixedHeightMinor;
    L_0x006c:
        if (r5 == 0) goto L_0x009c;
    L_0x006e:
        r11 = r5.type;
        if (r11 == 0) goto L_0x009c;
    L_0x0072:
        if (r11 != r7) goto L_0x007a;
    L_0x0074:
        r5 = r5.getDimension(r0);
    L_0x0078:
        r5 = (int) r5;
        goto L_0x0086;
    L_0x007a:
        if (r11 != r6) goto L_0x0085;
    L_0x007c:
        r11 = r0.heightPixels;
        r12 = (float) r11;
        r11 = (float) r11;
        r5 = r5.getFraction(r12, r11);
        goto L_0x0078;
    L_0x0085:
        r5 = 0;
    L_0x0086:
        if (r5 <= 0) goto L_0x009c;
    L_0x0088:
        r11 = r13.mDecorPadding;
        r12 = r11.top;
        r11 = r11.bottom;
        r12 = r12 + r11;
        r5 = r5 - r12;
        r15 = android.view.View.MeasureSpec.getSize(r15);
        r15 = java.lang.Math.min(r5, r15);
        r15 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r9);
    L_0x009c:
        super.onMeasure(r10, r15);
        r5 = r13.getMeasuredWidth();
        r10 = android.view.View.MeasureSpec.makeMeasureSpec(r5, r9);
        if (r14 != 0) goto L_0x00dd;
    L_0x00a9:
        if (r2 != r8) goto L_0x00dd;
    L_0x00ab:
        if (r1 == 0) goto L_0x00b0;
    L_0x00ad:
        r14 = r13.mMinWidthMinor;
        goto L_0x00b2;
    L_0x00b0:
        r14 = r13.mMinWidthMajor;
    L_0x00b2:
        if (r14 == 0) goto L_0x00dd;
    L_0x00b4:
        r1 = r14.type;
        if (r1 == 0) goto L_0x00dd;
    L_0x00b8:
        if (r1 != r7) goto L_0x00c0;
    L_0x00ba:
        r14 = r14.getDimension(r0);
    L_0x00be:
        r14 = (int) r14;
        goto L_0x00cc;
    L_0x00c0:
        if (r1 != r6) goto L_0x00cb;
    L_0x00c2:
        r0 = r0.widthPixels;
        r1 = (float) r0;
        r0 = (float) r0;
        r14 = r14.getFraction(r1, r0);
        goto L_0x00be;
    L_0x00cb:
        r14 = 0;
    L_0x00cc:
        if (r14 <= 0) goto L_0x00d6;
    L_0x00ce:
        r0 = r13.mDecorPadding;
        r1 = r0.left;
        r0 = r0.right;
        r1 = r1 + r0;
        r14 = r14 - r1;
    L_0x00d6:
        if (r5 >= r14) goto L_0x00dd;
    L_0x00d8:
        r10 = android.view.View.MeasureSpec.makeMeasureSpec(r14, r9);
        goto L_0x00de;
    L_0x00dd:
        r3 = 0;
    L_0x00de:
        if (r3 == 0) goto L_0x00e3;
    L_0x00e0:
        super.onMeasure(r10, r15);
    L_0x00e3:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ContentFrameLayout.onMeasure(int, int):void");
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

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        OnAttachListener onAttachListener = this.mAttachListener;
        if (onAttachListener != null) {
            onAttachListener.onAttachedFromWindow();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        OnAttachListener onAttachListener = this.mAttachListener;
        if (onAttachListener != null) {
            onAttachListener.onDetachedFromWindow();
        }
    }
}
