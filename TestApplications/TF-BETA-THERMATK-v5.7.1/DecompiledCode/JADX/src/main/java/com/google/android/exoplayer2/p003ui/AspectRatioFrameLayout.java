package com.google.android.exoplayer2.p003ui;

import android.content.Context;
import android.graphics.Matrix;
import android.widget.FrameLayout;

/* renamed from: com.google.android.exoplayer2.ui.AspectRatioFrameLayout */
public class AspectRatioFrameLayout extends FrameLayout {
    private static final float MAX_ASPECT_RATIO_DEFORMATION_FRACTION = 0.01f;
    public static final int RESIZE_MODE_FILL = 3;
    public static final int RESIZE_MODE_FIT = 0;
    public static final int RESIZE_MODE_FIXED_HEIGHT = 2;
    public static final int RESIZE_MODE_FIXED_WIDTH = 1;
    public static final int RESIZE_MODE_ZOOM = 4;
    private AspectRatioListener aspectRatioListener;
    private final AspectRatioUpdateDispatcher aspectRatioUpdateDispatcher = new AspectRatioUpdateDispatcher();
    private boolean drawingReady;
    private Matrix matrix = new Matrix();
    private int resizeMode = 0;
    private int rotation;
    private float videoAspectRatio;

    /* renamed from: com.google.android.exoplayer2.ui.AspectRatioFrameLayout$AspectRatioListener */
    public interface AspectRatioListener {
        void onAspectRatioUpdated(float f, float f2, boolean z);
    }

    /* renamed from: com.google.android.exoplayer2.ui.AspectRatioFrameLayout$AspectRatioUpdateDispatcher */
    private final class AspectRatioUpdateDispatcher implements Runnable {
        private boolean aspectRatioMismatch;
        private boolean isScheduled;
        private float naturalAspectRatio;
        private float targetAspectRatio;

        private AspectRatioUpdateDispatcher() {
        }

        public void scheduleUpdate(float f, float f2, boolean z) {
            this.targetAspectRatio = f;
            this.naturalAspectRatio = f2;
            this.aspectRatioMismatch = z;
            if (!this.isScheduled) {
                this.isScheduled = true;
                AspectRatioFrameLayout.this.post(this);
            }
        }

        public void run() {
            this.isScheduled = false;
            if (AspectRatioFrameLayout.this.aspectRatioListener != null) {
                AspectRatioFrameLayout.this.aspectRatioListener.onAspectRatioUpdated(this.targetAspectRatio, this.naturalAspectRatio, this.aspectRatioMismatch);
            }
        }
    }

    public AspectRatioFrameLayout(Context context) {
        super(context);
    }

    public void setAspectRatio(float f, int i) {
        if (this.videoAspectRatio != f) {
            this.videoAspectRatio = f;
            this.rotation = i;
            requestLayout();
        }
    }

    public void setAspectRatioListener(AspectRatioListener aspectRatioListener) {
        this.aspectRatioListener = aspectRatioListener;
    }

    public int getResizeMode() {
        return this.resizeMode;
    }

    public void setResizeMode(int i) {
        if (this.resizeMode != i) {
            this.resizeMode = i;
            requestLayout();
        }
    }

    public void setDrawingReady(boolean z) {
        if (this.drawingReady != z) {
            this.drawingReady = z;
        }
    }

    public float getAspectRatio() {
        return this.videoAspectRatio;
    }

    public int getVideoRotation() {
        return this.rotation;
    }

    public boolean isDrawingReady() {
        return this.drawingReady;
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0085  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0085  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0085  */
    public void onMeasure(int r12, int r13) {
        /*
        r11 = this;
        super.onMeasure(r12, r13);
        r12 = r11.videoAspectRatio;
        r13 = 0;
        r12 = (r12 > r13 ? 1 : (r12 == r13 ? 0 : -1));
        if (r12 > 0) goto L_0x000b;
    L_0x000a:
        return;
    L_0x000b:
        r12 = r11.getMeasuredWidth();
        r0 = r11.getMeasuredHeight();
        r1 = (float) r12;
        r2 = (float) r0;
        r3 = r1 / r2;
        r4 = r11.videoAspectRatio;
        r4 = r4 / r3;
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = r4 - r5;
        r6 = java.lang.Math.abs(r4);
        r7 = 1008981770; // 0x3c23d70a float:0.01 double:4.9850323E-315;
        r8 = 0;
        r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r6 > 0) goto L_0x0031;
    L_0x0029:
        r12 = r11.aspectRatioUpdateDispatcher;
        r13 = r11.videoAspectRatio;
        r12.scheduleUpdate(r13, r3, r8);
        return;
    L_0x0031:
        r6 = r11.resizeMode;
        r7 = 2;
        r9 = 1;
        if (r6 == 0) goto L_0x0061;
    L_0x0037:
        if (r6 == r9) goto L_0x005c;
    L_0x0039:
        if (r6 == r7) goto L_0x0056;
    L_0x003b:
        r10 = 3;
        if (r6 == r10) goto L_0x004c;
    L_0x003e:
        r10 = 4;
        if (r6 == r10) goto L_0x0042;
    L_0x0041:
        goto L_0x006b;
    L_0x0042:
        r13 = (r4 > r13 ? 1 : (r4 == r13 ? 0 : -1));
        if (r13 <= 0) goto L_0x0049;
    L_0x0046:
        r12 = r11.videoAspectRatio;
        goto L_0x0058;
    L_0x0049:
        r13 = r11.videoAspectRatio;
        goto L_0x005e;
    L_0x004c:
        r13 = (r4 > r13 ? 1 : (r4 == r13 ? 0 : -1));
        if (r13 > 0) goto L_0x0053;
    L_0x0050:
        r13 = r11.videoAspectRatio;
        goto L_0x005e;
    L_0x0053:
        r12 = r11.videoAspectRatio;
        goto L_0x0058;
    L_0x0056:
        r12 = r11.videoAspectRatio;
    L_0x0058:
        r2 = r2 * r12;
        r12 = (int) r2;
        goto L_0x006b;
    L_0x005c:
        r13 = r11.videoAspectRatio;
    L_0x005e:
        r1 = r1 / r13;
        r0 = (int) r1;
        goto L_0x006b;
    L_0x0061:
        r13 = (r4 > r13 ? 1 : (r4 == r13 ? 0 : -1));
        if (r13 <= 0) goto L_0x0068;
    L_0x0065:
        r13 = r11.videoAspectRatio;
        goto L_0x005e;
    L_0x0068:
        r12 = r11.videoAspectRatio;
        goto L_0x0058;
    L_0x006b:
        r13 = r11.aspectRatioUpdateDispatcher;
        r1 = r11.videoAspectRatio;
        r13.scheduleUpdate(r1, r3, r9);
        r13 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r12 = android.view.View.MeasureSpec.makeMeasureSpec(r12, r13);
        r13 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r13);
        super.onMeasure(r12, r13);
        r12 = r11.getChildCount();
    L_0x0083:
        if (r8 >= r12) goto L_0x00cc;
    L_0x0085:
        r13 = r11.getChildAt(r8);
        r0 = r13 instanceof android.view.TextureView;
        if (r0 == 0) goto L_0x00c9;
    L_0x008d:
        r12 = r11.matrix;
        r12.reset();
        r12 = r11.getWidth();
        r12 = r12 / r7;
        r0 = r11.getHeight();
        r0 = r0 / r7;
        r1 = r11.matrix;
        r2 = r11.rotation;
        r2 = (float) r2;
        r12 = (float) r12;
        r0 = (float) r0;
        r1.postRotate(r2, r12, r0);
        r1 = r11.rotation;
        r2 = 90;
        if (r1 == r2) goto L_0x00b0;
    L_0x00ac:
        r2 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        if (r1 != r2) goto L_0x00c1;
    L_0x00b0:
        r1 = r11.getHeight();
        r1 = (float) r1;
        r2 = r11.getWidth();
        r2 = (float) r2;
        r1 = r1 / r2;
        r2 = r11.matrix;
        r5 = r5 / r1;
        r2.postScale(r5, r1, r12, r0);
    L_0x00c1:
        r13 = (android.view.TextureView) r13;
        r12 = r11.matrix;
        r13.setTransform(r12);
        goto L_0x00cc;
    L_0x00c9:
        r8 = r8 + 1;
        goto L_0x0083;
    L_0x00cc:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.p003ui.AspectRatioFrameLayout.onMeasure(int, int):void");
    }
}
