package org.metalev.multitouch.controller;

import android.util.Log;
import android.view.MotionEvent;
import java.lang.reflect.Method;

public class MultiTouchController<T> {
    private static int ACTION_POINTER_INDEX_SHIFT = 8;
    private static int ACTION_POINTER_UP = 6;
    private static Method m_getHistoricalPressure;
    private static Method m_getHistoricalX;
    private static Method m_getHistoricalY;
    private static Method m_getPointerCount;
    private static Method m_getPointerId;
    private static Method m_getPressure;
    private static Method m_getX;
    private static Method m_getY;
    public static final boolean multiTouchSupported;
    private static final int[] pointerIds = new int[20];
    private static final float[] pressureVals = new float[20];
    private static final float[] xVals = new float[20];
    private static final float[] yVals = new float[20];
    private boolean handleSingleTouchEvents;
    private PointInfo mCurrPt = new PointInfo();
    private float mCurrPtAng;
    private float mCurrPtDiam;
    private float mCurrPtHeight;
    private float mCurrPtWidth;
    private float mCurrPtX;
    private float mCurrPtY;
    private PositionAndScale mCurrXform = new PositionAndScale();
    private int mMode = 0;
    private PointInfo mPrevPt = new PointInfo();
    private long mSettleEndTime;
    private long mSettleStartTime;
    MultiTouchObjectCanvas<T> objectCanvas;
    private T selectedObject = null;
    private float startAngleMinusPinchAngle;
    private float startPosX;
    private float startPosY;
    private float startScaleOverPinchDiam;
    private float startScaleXOverPinchWidth;
    private float startScaleYOverPinchHeight;

    public interface MultiTouchObjectCanvas<T> {
        T getDraggableObjectAtPoint(PointInfo pointInfo);

        void getPositionAndScale(T t, PositionAndScale positionAndScale);

        void selectObject(T t, PointInfo pointInfo);

        boolean setPositionAndScale(T t, PositionAndScale positionAndScale, PointInfo pointInfo);
    }

    public static class PointInfo {
        private int action;
        private float angle;
        private boolean angleIsCalculated;
        private float diameter;
        private boolean diameterIsCalculated;
        private float diameterSq;
        private boolean diameterSqIsCalculated;
        /* renamed from: dx */
        private float f40dx;
        /* renamed from: dy */
        private float f41dy;
        private long eventTime;
        private boolean isDown;
        private boolean isMultiTouch;
        private int numPoints;
        private int[] pointerIds = new int[20];
        private float pressureMid;
        private float[] pressures = new float[20];
        private float xMid;
        /* renamed from: xs */
        private float[] f42xs = new float[20];
        private float yMid;
        /* renamed from: ys */
        private float[] f43ys = new float[20];

        private int julery_isqrt(int i) {
            int i2 = 0;
            int i3 = 32768;
            int i4 = 15;
            while (true) {
                int i5 = i4 - 1;
                i4 = ((i2 << 1) + i3) << i4;
                if (i >= i4) {
                    i2 += i3;
                    i -= i4;
                }
                i3 >>= 1;
                if (i3 <= 0) {
                    return i2;
                }
                i4 = i5;
            }
        }

        private void set(int i, float[] fArr, float[] fArr2, float[] fArr3, int[] iArr, int i2, boolean z, long j) {
            this.eventTime = j;
            this.action = i2;
            this.numPoints = i;
            for (int i3 = 0; i3 < i; i3++) {
                this.f42xs[i3] = fArr[i3];
                this.f43ys[i3] = fArr2[i3];
                this.pressures[i3] = fArr3[i3];
                this.pointerIds[i3] = iArr[i3];
            }
            this.isDown = z;
            this.isMultiTouch = i >= 2;
            if (this.isMultiTouch) {
                this.xMid = (fArr[0] + fArr[1]) * 0.5f;
                this.yMid = (fArr2[0] + fArr2[1]) * 0.5f;
                this.pressureMid = (fArr3[0] + fArr3[1]) * 0.5f;
                this.f40dx = Math.abs(fArr[1] - fArr[0]);
                this.f41dy = Math.abs(fArr2[1] - fArr2[0]);
            } else {
                this.xMid = fArr[0];
                this.yMid = fArr2[0];
                this.pressureMid = fArr3[0];
                this.f41dy = 0.0f;
                this.f40dx = 0.0f;
            }
            this.angleIsCalculated = false;
            this.diameterIsCalculated = false;
            this.diameterSqIsCalculated = false;
        }

        public boolean isMultiTouch() {
            return this.isMultiTouch;
        }

        public float getMultiTouchWidth() {
            return this.isMultiTouch ? this.f40dx : 0.0f;
        }

        public float getMultiTouchHeight() {
            return this.isMultiTouch ? this.f41dy : 0.0f;
        }

        public float getMultiTouchDiameterSq() {
            if (!this.diameterSqIsCalculated) {
                float f;
                if (this.isMultiTouch) {
                    f = this.f40dx;
                    f *= f;
                    float f2 = this.f41dy;
                    f += f2 * f2;
                } else {
                    f = 0.0f;
                }
                this.diameterSq = f;
                this.diameterSqIsCalculated = true;
            }
            return this.diameterSq;
        }

        public float getMultiTouchDiameter() {
            if (!this.diameterIsCalculated) {
                float f = 0.0f;
                if (this.isMultiTouch) {
                    float multiTouchDiameterSq = getMultiTouchDiameterSq();
                    if (multiTouchDiameterSq != 0.0f) {
                        f = ((float) julery_isqrt((int) (multiTouchDiameterSq * 256.0f))) / 16.0f;
                    }
                    this.diameter = f;
                    multiTouchDiameterSq = this.diameter;
                    f = this.f40dx;
                    if (multiTouchDiameterSq < f) {
                        this.diameter = f;
                    }
                    multiTouchDiameterSq = this.diameter;
                    f = this.f41dy;
                    if (multiTouchDiameterSq < f) {
                        this.diameter = f;
                    }
                } else {
                    this.diameter = 0.0f;
                }
                this.diameterIsCalculated = true;
            }
            return this.diameter;
        }

        public float getMultiTouchAngle() {
            if (!this.angleIsCalculated) {
                if (this.isMultiTouch) {
                    float[] fArr = this.f43ys;
                    double d = (double) (fArr[1] - fArr[0]);
                    fArr = this.f42xs;
                    this.angle = (float) Math.atan2(d, (double) (fArr[1] - fArr[0]));
                } else {
                    this.angle = 0.0f;
                }
                this.angleIsCalculated = true;
            }
            return this.angle;
        }

        public float getX() {
            return this.xMid;
        }

        public float getY() {
            return this.yMid;
        }

        public boolean isDown() {
            return this.isDown;
        }

        public long getEventTime() {
            return this.eventTime;
        }
    }

    public static class PositionAndScale {
        private float angle;
        private float scale;
        private float scaleX;
        private float scaleY;
        private boolean updateAngle;
        private boolean updateScale;
        private boolean updateScaleXY;
        private float xOff;
        private float yOff;

        public void set(float f, float f2, boolean z, float f3, boolean z2, float f4, float f5, boolean z3, float f6) {
            this.xOff = f;
            this.yOff = f2;
            this.updateScale = z;
            f = 1.0f;
            if (f3 == 0.0f) {
                f3 = 1.0f;
            }
            this.scale = f3;
            this.updateScaleXY = z2;
            if (f4 == 0.0f) {
                f4 = 1.0f;
            }
            this.scaleX = f4;
            if (f5 != 0.0f) {
                f = f5;
            }
            this.scaleY = f;
            this.updateAngle = z3;
            this.angle = f6;
        }

        /* Access modifiers changed, original: protected */
        public void set(float f, float f2, float f3, float f4, float f5, float f6) {
            this.xOff = f;
            this.yOff = f2;
            f = 1.0f;
            if (f3 == 0.0f) {
                f3 = 1.0f;
            }
            this.scale = f3;
            if (f4 == 0.0f) {
                f4 = 1.0f;
            }
            this.scaleX = f4;
            if (f5 != 0.0f) {
                f = f5;
            }
            this.scaleY = f;
            this.angle = f6;
        }

        public float getXOff() {
            return this.xOff;
        }

        public float getYOff() {
            return this.yOff;
        }

        public float getScale() {
            return !this.updateScale ? 1.0f : this.scale;
        }
    }

    private void extractCurrPtInfo() {
        this.mCurrPtX = this.mCurrPt.getX();
        this.mCurrPtY = this.mCurrPt.getY();
        float f = 0.0f;
        this.mCurrPtDiam = Math.max(21.3f, !this.mCurrXform.updateScale ? 0.0f : this.mCurrPt.getMultiTouchDiameter());
        this.mCurrPtWidth = Math.max(30.0f, !this.mCurrXform.updateScaleXY ? 0.0f : this.mCurrPt.getMultiTouchWidth());
        this.mCurrPtHeight = Math.max(30.0f, !this.mCurrXform.updateScaleXY ? 0.0f : this.mCurrPt.getMultiTouchHeight());
        if (this.mCurrXform.updateAngle) {
            f = this.mCurrPt.getMultiTouchAngle();
        }
        this.mCurrPtAng = f;
    }

    public MultiTouchController(MultiTouchObjectCanvas<T> multiTouchObjectCanvas, boolean z) {
        this.handleSingleTouchEvents = z;
        this.objectCanvas = multiTouchObjectCanvas;
    }

    static {
        boolean z = true;
        try {
            m_getPointerCount = MotionEvent.class.getMethod("getPointerCount", new Class[0]);
            m_getPointerId = MotionEvent.class.getMethod("getPointerId", new Class[]{Integer.TYPE});
            m_getPressure = MotionEvent.class.getMethod("getPressure", new Class[]{Integer.TYPE});
            m_getHistoricalX = MotionEvent.class.getMethod("getHistoricalX", new Class[]{Integer.TYPE, Integer.TYPE});
            m_getHistoricalY = MotionEvent.class.getMethod("getHistoricalY", new Class[]{Integer.TYPE, Integer.TYPE});
            m_getHistoricalPressure = MotionEvent.class.getMethod("getHistoricalPressure", new Class[]{Integer.TYPE, Integer.TYPE});
            m_getX = MotionEvent.class.getMethod("getX", new Class[]{Integer.TYPE});
            m_getY = MotionEvent.class.getMethod("getY", new Class[]{Integer.TYPE});
        } catch (Exception e) {
            Log.e("MultiTouchController", "static initializer failed", e);
            z = false;
        }
        multiTouchSupported = z;
        if (multiTouchSupported) {
            try {
                ACTION_POINTER_UP = MotionEvent.class.getField("ACTION_POINTER_UP").getInt(null);
                ACTION_POINTER_INDEX_SHIFT = MotionEvent.class.getField("ACTION_POINTER_INDEX_SHIFT").getInt(null);
            } catch (Exception unused) {
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:66:0x0145 A:{Catch:{ Exception -> 0x0165 }} */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0140 A:{Catch:{ Exception -> 0x0165 }} */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0126 A:{Catch:{ Exception -> 0x0165 }} */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0124 A:{Catch:{ Exception -> 0x0165 }} */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x012c A:{Catch:{ Exception -> 0x0165 }} */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0140 A:{Catch:{ Exception -> 0x0165 }} */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0145 A:{Catch:{ Exception -> 0x0165 }} */
    public boolean onTouchEvent(android.view.MotionEvent r21) {
        /*
        r20 = this;
        r11 = r20;
        r0 = r21;
        r12 = 0;
        r1 = multiTouchSupported;	 Catch:{ Exception -> 0x0165 }
        r13 = 1;
        if (r1 == 0) goto L_0x001a;
    L_0x000a:
        r1 = m_getPointerCount;	 Catch:{ Exception -> 0x0165 }
        r2 = new java.lang.Object[r12];	 Catch:{ Exception -> 0x0165 }
        r1 = r1.invoke(r0, r2);	 Catch:{ Exception -> 0x0165 }
        r1 = (java.lang.Integer) r1;	 Catch:{ Exception -> 0x0165 }
        r1 = r1.intValue();	 Catch:{ Exception -> 0x0165 }
        r14 = r1;
        goto L_0x001b;
    L_0x001a:
        r14 = 1;
    L_0x001b:
        r1 = r11.mMode;	 Catch:{ Exception -> 0x0165 }
        if (r1 != 0) goto L_0x0026;
    L_0x001f:
        r1 = r11.handleSingleTouchEvents;	 Catch:{ Exception -> 0x0165 }
        if (r1 != 0) goto L_0x0026;
    L_0x0023:
        if (r14 != r13) goto L_0x0026;
    L_0x0025:
        return r12;
    L_0x0026:
        r15 = r21.getAction();	 Catch:{ Exception -> 0x0165 }
        r1 = r21.getHistorySize();	 Catch:{ Exception -> 0x0165 }
        r9 = r1 / r14;
        r10 = 0;
    L_0x0031:
        if (r10 > r9) goto L_0x0164;
    L_0x0033:
        if (r10 >= r9) goto L_0x0037;
    L_0x0035:
        r1 = 1;
        goto L_0x0038;
    L_0x0037:
        r1 = 0;
    L_0x0038:
        r2 = multiTouchSupported;	 Catch:{ Exception -> 0x0165 }
        r3 = 2;
        if (r2 == 0) goto L_0x00ed;
    L_0x003d:
        if (r14 != r13) goto L_0x0041;
    L_0x003f:
        goto L_0x00ed;
    L_0x0041:
        r2 = 20;
        r2 = java.lang.Math.min(r14, r2);	 Catch:{ Exception -> 0x0165 }
        r4 = 0;
    L_0x0048:
        if (r4 >= r2) goto L_0x011a;
    L_0x004a:
        r5 = m_getPointerId;	 Catch:{ Exception -> 0x0165 }
        r6 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x0165 }
        r7 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x0165 }
        r6[r12] = r7;	 Catch:{ Exception -> 0x0165 }
        r5 = r5.invoke(r0, r6);	 Catch:{ Exception -> 0x0165 }
        r5 = (java.lang.Integer) r5;	 Catch:{ Exception -> 0x0165 }
        r5 = r5.intValue();	 Catch:{ Exception -> 0x0165 }
        r6 = pointerIds;	 Catch:{ Exception -> 0x0165 }
        r6[r4] = r5;	 Catch:{ Exception -> 0x0165 }
        r5 = xVals;	 Catch:{ Exception -> 0x0165 }
        if (r1 == 0) goto L_0x007b;
    L_0x0066:
        r6 = m_getHistoricalX;	 Catch:{ Exception -> 0x0165 }
        r7 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x0165 }
        r8 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x0165 }
        r7[r12] = r8;	 Catch:{ Exception -> 0x0165 }
        r8 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x0165 }
        r7[r13] = r8;	 Catch:{ Exception -> 0x0165 }
    L_0x0076:
        r6 = r6.invoke(r0, r7);	 Catch:{ Exception -> 0x0165 }
        goto L_0x0086;
    L_0x007b:
        r6 = m_getX;	 Catch:{ Exception -> 0x0165 }
        r7 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x0165 }
        r8 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x0165 }
        r7[r12] = r8;	 Catch:{ Exception -> 0x0165 }
        goto L_0x0076;
    L_0x0086:
        r6 = (java.lang.Float) r6;	 Catch:{ Exception -> 0x0165 }
        r6 = r6.floatValue();	 Catch:{ Exception -> 0x0165 }
        r5[r4] = r6;	 Catch:{ Exception -> 0x0165 }
        r5 = yVals;	 Catch:{ Exception -> 0x0165 }
        if (r1 == 0) goto L_0x00a7;
    L_0x0092:
        r6 = m_getHistoricalY;	 Catch:{ Exception -> 0x0165 }
        r7 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x0165 }
        r8 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x0165 }
        r7[r12] = r8;	 Catch:{ Exception -> 0x0165 }
        r8 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x0165 }
        r7[r13] = r8;	 Catch:{ Exception -> 0x0165 }
    L_0x00a2:
        r6 = r6.invoke(r0, r7);	 Catch:{ Exception -> 0x0165 }
        goto L_0x00b2;
    L_0x00a7:
        r6 = m_getY;	 Catch:{ Exception -> 0x0165 }
        r7 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x0165 }
        r8 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x0165 }
        r7[r12] = r8;	 Catch:{ Exception -> 0x0165 }
        goto L_0x00a2;
    L_0x00b2:
        r6 = (java.lang.Float) r6;	 Catch:{ Exception -> 0x0165 }
        r6 = r6.floatValue();	 Catch:{ Exception -> 0x0165 }
        r5[r4] = r6;	 Catch:{ Exception -> 0x0165 }
        r5 = pressureVals;	 Catch:{ Exception -> 0x0165 }
        if (r1 == 0) goto L_0x00d3;
    L_0x00be:
        r6 = m_getHistoricalPressure;	 Catch:{ Exception -> 0x0165 }
        r7 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x0165 }
        r8 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x0165 }
        r7[r12] = r8;	 Catch:{ Exception -> 0x0165 }
        r8 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x0165 }
        r7[r13] = r8;	 Catch:{ Exception -> 0x0165 }
        r6 = r6.invoke(r0, r7);	 Catch:{ Exception -> 0x0165 }
        goto L_0x00e1;
    L_0x00d3:
        r6 = m_getPressure;	 Catch:{ Exception -> 0x0165 }
        r7 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x0165 }
        r8 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x0165 }
        r7[r12] = r8;	 Catch:{ Exception -> 0x0165 }
        r6 = r6.invoke(r0, r7);	 Catch:{ Exception -> 0x0165 }
    L_0x00e1:
        r6 = (java.lang.Float) r6;	 Catch:{ Exception -> 0x0165 }
        r6 = r6.floatValue();	 Catch:{ Exception -> 0x0165 }
        r5[r4] = r6;	 Catch:{ Exception -> 0x0165 }
        r4 = r4 + 1;
        goto L_0x0048;
    L_0x00ed:
        r2 = xVals;	 Catch:{ Exception -> 0x0165 }
        if (r1 == 0) goto L_0x00f6;
    L_0x00f1:
        r4 = r0.getHistoricalX(r10);	 Catch:{ Exception -> 0x0165 }
        goto L_0x00fa;
    L_0x00f6:
        r4 = r21.getX();	 Catch:{ Exception -> 0x0165 }
    L_0x00fa:
        r2[r12] = r4;	 Catch:{ Exception -> 0x0165 }
        r2 = yVals;	 Catch:{ Exception -> 0x0165 }
        if (r1 == 0) goto L_0x0105;
    L_0x0100:
        r4 = r0.getHistoricalY(r10);	 Catch:{ Exception -> 0x0165 }
        goto L_0x0109;
    L_0x0105:
        r4 = r21.getY();	 Catch:{ Exception -> 0x0165 }
    L_0x0109:
        r2[r12] = r4;	 Catch:{ Exception -> 0x0165 }
        r2 = pressureVals;	 Catch:{ Exception -> 0x0165 }
        if (r1 == 0) goto L_0x0114;
    L_0x010f:
        r4 = r0.getHistoricalPressure(r10);	 Catch:{ Exception -> 0x0165 }
        goto L_0x0118;
    L_0x0114:
        r4 = r21.getPressure();	 Catch:{ Exception -> 0x0165 }
    L_0x0118:
        r2[r12] = r4;	 Catch:{ Exception -> 0x0165 }
    L_0x011a:
        r4 = xVals;	 Catch:{ Exception -> 0x0165 }
        r5 = yVals;	 Catch:{ Exception -> 0x0165 }
        r6 = pressureVals;	 Catch:{ Exception -> 0x0165 }
        r7 = pointerIds;	 Catch:{ Exception -> 0x0165 }
        if (r1 == 0) goto L_0x0126;
    L_0x0124:
        r8 = 2;
        goto L_0x0127;
    L_0x0126:
        r8 = r15;
    L_0x0127:
        if (r1 == 0) goto L_0x012c;
    L_0x0129:
        r16 = 1;
        goto L_0x013e;
    L_0x012c:
        if (r15 == r13) goto L_0x013c;
    L_0x012e:
        r2 = ACTION_POINTER_INDEX_SHIFT;	 Catch:{ Exception -> 0x0165 }
        r2 = r13 << r2;
        r2 = r2 - r13;
        r2 = r2 & r15;
        r3 = ACTION_POINTER_UP;	 Catch:{ Exception -> 0x0165 }
        if (r2 == r3) goto L_0x013c;
    L_0x0138:
        r2 = 3;
        if (r15 == r2) goto L_0x013c;
    L_0x013b:
        goto L_0x0129;
    L_0x013c:
        r16 = 0;
    L_0x013e:
        if (r1 == 0) goto L_0x0145;
    L_0x0140:
        r1 = r0.getHistoricalEventTime(r10);	 Catch:{ Exception -> 0x0165 }
        goto L_0x0149;
    L_0x0145:
        r1 = r21.getEventTime();	 Catch:{ Exception -> 0x0165 }
    L_0x0149:
        r17 = r1;
        r1 = r20;
        r2 = r14;
        r3 = r4;
        r4 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r16;
        r16 = r9;
        r19 = r10;
        r9 = r17;
        r1.decodeTouchEvent(r2, r3, r4, r5, r6, r7, r8, r9);	 Catch:{ Exception -> 0x0165 }
        r10 = r19 + 1;
        r9 = r16;
        goto L_0x0031;
    L_0x0164:
        return r13;
    L_0x0165:
        r0 = move-exception;
        r1 = "MultiTouchController";
        r2 = "onTouchEvent() failed";
        android.util.Log.e(r1, r2, r0);
        return r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.metalev.multitouch.controller.MultiTouchController.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private void decodeTouchEvent(int i, float[] fArr, float[] fArr2, float[] fArr3, int[] iArr, int i2, boolean z, long j) {
        PointInfo pointInfo = this.mPrevPt;
        this.mPrevPt = this.mCurrPt;
        this.mCurrPt = pointInfo;
        this.mCurrPt.set(i, fArr, fArr2, fArr3, iArr, i2, z, j);
        multiTouchController();
    }

    private void anchorAtThisPositionAndScale() {
        Object obj = this.selectedObject;
        if (obj != null) {
            this.objectCanvas.getPositionAndScale(obj, this.mCurrXform);
            float access$400 = (this.mCurrXform.updateScale && this.mCurrXform.scale != 0.0f) ? this.mCurrXform.scale : 1.0f;
            float f = 1.0f / access$400;
            extractCurrPtInfo();
            this.startPosX = (this.mCurrPtX - this.mCurrXform.xOff) * f;
            this.startPosY = (this.mCurrPtY - this.mCurrXform.yOff) * f;
            this.startScaleOverPinchDiam = this.mCurrXform.scale / this.mCurrPtDiam;
            this.startScaleXOverPinchWidth = this.mCurrXform.scaleX / this.mCurrPtWidth;
            this.startScaleYOverPinchHeight = this.mCurrXform.scaleY / this.mCurrPtHeight;
            this.startAngleMinusPinchAngle = this.mCurrXform.angle - this.mCurrPtAng;
        }
    }

    private void performDragOrPinch() {
        if (this.selectedObject != null) {
            float f = 1.0f;
            if (this.mCurrXform.updateScale && this.mCurrXform.scale != 0.0f) {
                f = this.mCurrXform.scale;
            }
            extractCurrPtInfo();
            this.mCurrXform.set(this.mCurrPtX - (this.startPosX * f), this.mCurrPtY - (this.startPosY * f), this.startScaleOverPinchDiam * this.mCurrPtDiam, this.startScaleXOverPinchWidth * this.mCurrPtWidth, this.startScaleYOverPinchHeight * this.mCurrPtHeight, this.startAngleMinusPinchAngle + this.mCurrPtAng);
            this.objectCanvas.setPositionAndScale(this.selectedObject, this.mCurrXform, this.mCurrPt);
        }
    }

    public boolean isPinching() {
        return this.mMode == 2;
    }

    private void multiTouchController() {
        int i = this.mMode;
        if (i != 0) {
            MultiTouchObjectCanvas multiTouchObjectCanvas;
            if (i != 1) {
                if (i == 2) {
                    if (this.mCurrPt.isMultiTouch() && this.mCurrPt.isDown()) {
                        if (Math.abs(this.mCurrPt.getX() - this.mPrevPt.getX()) > 30.0f || Math.abs(this.mCurrPt.getY() - this.mPrevPt.getY()) > 30.0f || Math.abs(this.mCurrPt.getMultiTouchWidth() - this.mPrevPt.getMultiTouchWidth()) * 0.5f > 40.0f || Math.abs(this.mCurrPt.getMultiTouchHeight() - this.mPrevPt.getMultiTouchHeight()) * 0.5f > 40.0f) {
                            anchorAtThisPositionAndScale();
                            this.mSettleStartTime = this.mCurrPt.getEventTime();
                            this.mSettleEndTime = this.mSettleStartTime + 20;
                        } else if (this.mCurrPt.eventTime < this.mSettleEndTime) {
                            anchorAtThisPositionAndScale();
                        } else {
                            performDragOrPinch();
                        }
                    } else if (this.mCurrPt.isDown()) {
                        this.mMode = 1;
                        anchorAtThisPositionAndScale();
                        this.mSettleStartTime = this.mCurrPt.getEventTime();
                        this.mSettleEndTime = this.mSettleStartTime + 20;
                    } else {
                        this.mMode = 0;
                        multiTouchObjectCanvas = this.objectCanvas;
                        this.selectedObject = null;
                        multiTouchObjectCanvas.selectObject(null, this.mCurrPt);
                    }
                }
            } else if (!this.mCurrPt.isDown()) {
                this.mMode = 0;
                multiTouchObjectCanvas = this.objectCanvas;
                this.selectedObject = null;
                multiTouchObjectCanvas.selectObject(null, this.mCurrPt);
            } else if (this.mCurrPt.isMultiTouch()) {
                this.mMode = 2;
                anchorAtThisPositionAndScale();
                this.mSettleStartTime = this.mCurrPt.getEventTime();
                this.mSettleEndTime = this.mSettleStartTime + 20;
            } else if (this.mCurrPt.getEventTime() < this.mSettleEndTime) {
                anchorAtThisPositionAndScale();
            } else {
                performDragOrPinch();
            }
        } else if (this.mCurrPt.isDown()) {
            this.selectedObject = this.objectCanvas.getDraggableObjectAtPoint(this.mCurrPt);
            Object obj = this.selectedObject;
            if (obj != null) {
                this.mMode = 1;
                this.objectCanvas.selectObject(obj, this.mCurrPt);
                anchorAtThisPositionAndScale();
                long eventTime = this.mCurrPt.getEventTime();
                this.mSettleEndTime = eventTime;
                this.mSettleStartTime = eventTime;
            }
        }
    }
}
