// 
// Decompiled by Procyon v0.5.34
// 

package org.metalev.multitouch.controller;

import android.util.Log;
import android.view.MotionEvent;
import java.lang.reflect.Method;

public class MultiTouchController<T>
{
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
    private static final int[] pointerIds;
    private static final float[] pressureVals;
    private static final float[] xVals;
    private static final float[] yVals;
    private boolean handleSingleTouchEvents;
    private PointInfo mCurrPt;
    private float mCurrPtAng;
    private float mCurrPtDiam;
    private float mCurrPtHeight;
    private float mCurrPtWidth;
    private float mCurrPtX;
    private float mCurrPtY;
    private PositionAndScale mCurrXform;
    private int mMode;
    private PointInfo mPrevPt;
    private long mSettleEndTime;
    private long mSettleStartTime;
    MultiTouchObjectCanvas<T> objectCanvas;
    private T selectedObject;
    private float startAngleMinusPinchAngle;
    private float startPosX;
    private float startPosY;
    private float startScaleOverPinchDiam;
    private float startScaleXOverPinchWidth;
    private float startScaleYOverPinchHeight;
    
    static {
        boolean multiTouchSupported2 = true;
        try {
            MultiTouchController.m_getPointerCount = MotionEvent.class.getMethod("getPointerCount", (Class<?>[])new Class[0]);
            MultiTouchController.m_getPointerId = MotionEvent.class.getMethod("getPointerId", Integer.TYPE);
            MultiTouchController.m_getPressure = MotionEvent.class.getMethod("getPressure", Integer.TYPE);
            MultiTouchController.m_getHistoricalX = MotionEvent.class.getMethod("getHistoricalX", Integer.TYPE, Integer.TYPE);
            MultiTouchController.m_getHistoricalY = MotionEvent.class.getMethod("getHistoricalY", Integer.TYPE, Integer.TYPE);
            MultiTouchController.m_getHistoricalPressure = MotionEvent.class.getMethod("getHistoricalPressure", Integer.TYPE, Integer.TYPE);
            MultiTouchController.m_getX = MotionEvent.class.getMethod("getX", Integer.TYPE);
            MultiTouchController.m_getY = MotionEvent.class.getMethod("getY", Integer.TYPE);
        }
        catch (Exception ex) {
            Log.e("MultiTouchController", "static initializer failed", (Throwable)ex);
            multiTouchSupported2 = false;
        }
        multiTouchSupported = multiTouchSupported2;
        while (true) {
            if (!MultiTouchController.multiTouchSupported) {
                break Label_0227;
            }
            try {
                MultiTouchController.ACTION_POINTER_UP = MotionEvent.class.getField("ACTION_POINTER_UP").getInt(null);
                MultiTouchController.ACTION_POINTER_INDEX_SHIFT = MotionEvent.class.getField("ACTION_POINTER_INDEX_SHIFT").getInt(null);
                xVals = new float[20];
                yVals = new float[20];
                pressureVals = new float[20];
                pointerIds = new int[20];
            }
            catch (Exception ex2) {
                continue;
            }
            break;
        }
    }
    
    public MultiTouchController(final MultiTouchObjectCanvas<T> objectCanvas, final boolean handleSingleTouchEvents) {
        this.selectedObject = null;
        this.mCurrXform = new PositionAndScale();
        this.mMode = 0;
        this.mCurrPt = new PointInfo();
        this.mPrevPt = new PointInfo();
        this.handleSingleTouchEvents = handleSingleTouchEvents;
        this.objectCanvas = objectCanvas;
    }
    
    private void anchorAtThisPositionAndScale() {
        final T selectedObject = this.selectedObject;
        if (selectedObject == null) {
            return;
        }
        this.objectCanvas.getPositionAndScale(selectedObject, this.mCurrXform);
        float access$400;
        if (this.mCurrXform.updateScale && this.mCurrXform.scale != 0.0f) {
            access$400 = this.mCurrXform.scale;
        }
        else {
            access$400 = 1.0f;
        }
        final float n = 1.0f / access$400;
        this.extractCurrPtInfo();
        this.startPosX = (this.mCurrPtX - this.mCurrXform.xOff) * n;
        this.startPosY = (this.mCurrPtY - this.mCurrXform.yOff) * n;
        this.startScaleOverPinchDiam = this.mCurrXform.scale / this.mCurrPtDiam;
        this.startScaleXOverPinchWidth = this.mCurrXform.scaleX / this.mCurrPtWidth;
        this.startScaleYOverPinchHeight = this.mCurrXform.scaleY / this.mCurrPtHeight;
        this.startAngleMinusPinchAngle = this.mCurrXform.angle - this.mCurrPtAng;
    }
    
    private void decodeTouchEvent(final int n, final float[] array, final float[] array2, final float[] array3, final int[] array4, final int n2, final boolean b, final long n3) {
        final PointInfo mPrevPt = this.mPrevPt;
        this.mPrevPt = this.mCurrPt;
        (this.mCurrPt = mPrevPt).set(n, array, array2, array3, array4, n2, b, n3);
        this.multiTouchController();
    }
    
    private void extractCurrPtInfo() {
        this.mCurrPtX = this.mCurrPt.getX();
        this.mCurrPtY = this.mCurrPt.getY();
        final boolean access$000 = this.mCurrXform.updateScale;
        final float n = 0.0f;
        float multiTouchDiameter;
        if (!access$000) {
            multiTouchDiameter = 0.0f;
        }
        else {
            multiTouchDiameter = this.mCurrPt.getMultiTouchDiameter();
        }
        this.mCurrPtDiam = Math.max(21.3f, multiTouchDiameter);
        float multiTouchWidth;
        if (!this.mCurrXform.updateScaleXY) {
            multiTouchWidth = 0.0f;
        }
        else {
            multiTouchWidth = this.mCurrPt.getMultiTouchWidth();
        }
        this.mCurrPtWidth = Math.max(30.0f, multiTouchWidth);
        float multiTouchHeight;
        if (!this.mCurrXform.updateScaleXY) {
            multiTouchHeight = 0.0f;
        }
        else {
            multiTouchHeight = this.mCurrPt.getMultiTouchHeight();
        }
        this.mCurrPtHeight = Math.max(30.0f, multiTouchHeight);
        float multiTouchAngle;
        if (!this.mCurrXform.updateAngle) {
            multiTouchAngle = n;
        }
        else {
            multiTouchAngle = this.mCurrPt.getMultiTouchAngle();
        }
        this.mCurrPtAng = multiTouchAngle;
    }
    
    private void multiTouchController() {
        final int mMode = this.mMode;
        if (mMode != 0) {
            if (mMode != 1) {
                if (mMode == 2) {
                    if (this.mCurrPt.isMultiTouch() && this.mCurrPt.isDown()) {
                        if (Math.abs(this.mCurrPt.getX() - this.mPrevPt.getX()) <= 30.0f && Math.abs(this.mCurrPt.getY() - this.mPrevPt.getY()) <= 30.0f && Math.abs(this.mCurrPt.getMultiTouchWidth() - this.mPrevPt.getMultiTouchWidth()) * 0.5f <= 40.0f && Math.abs(this.mCurrPt.getMultiTouchHeight() - this.mPrevPt.getMultiTouchHeight()) * 0.5f <= 40.0f) {
                            if (this.mCurrPt.eventTime < this.mSettleEndTime) {
                                this.anchorAtThisPositionAndScale();
                            }
                            else {
                                this.performDragOrPinch();
                            }
                        }
                        else {
                            this.anchorAtThisPositionAndScale();
                            this.mSettleStartTime = this.mCurrPt.getEventTime();
                            this.mSettleEndTime = this.mSettleStartTime + 20L;
                        }
                    }
                    else if (!this.mCurrPt.isDown()) {
                        this.mMode = 0;
                        this.objectCanvas.selectObject(this.selectedObject = null, this.mCurrPt);
                    }
                    else {
                        this.mMode = 1;
                        this.anchorAtThisPositionAndScale();
                        this.mSettleStartTime = this.mCurrPt.getEventTime();
                        this.mSettleEndTime = this.mSettleStartTime + 20L;
                    }
                }
            }
            else if (!this.mCurrPt.isDown()) {
                this.mMode = 0;
                this.objectCanvas.selectObject(this.selectedObject = null, this.mCurrPt);
            }
            else if (this.mCurrPt.isMultiTouch()) {
                this.mMode = 2;
                this.anchorAtThisPositionAndScale();
                this.mSettleStartTime = this.mCurrPt.getEventTime();
                this.mSettleEndTime = this.mSettleStartTime + 20L;
            }
            else if (this.mCurrPt.getEventTime() < this.mSettleEndTime) {
                this.anchorAtThisPositionAndScale();
            }
            else {
                this.performDragOrPinch();
            }
        }
        else if (this.mCurrPt.isDown()) {
            this.selectedObject = this.objectCanvas.getDraggableObjectAtPoint(this.mCurrPt);
            final T selectedObject = this.selectedObject;
            if (selectedObject != null) {
                this.mMode = 1;
                this.objectCanvas.selectObject(selectedObject, this.mCurrPt);
                this.anchorAtThisPositionAndScale();
                final long eventTime = this.mCurrPt.getEventTime();
                this.mSettleEndTime = eventTime;
                this.mSettleStartTime = eventTime;
            }
        }
    }
    
    private void performDragOrPinch() {
        if (this.selectedObject == null) {
            return;
        }
        final boolean access$000 = this.mCurrXform.updateScale;
        float access$2 = 1.0f;
        if (access$000) {
            if (this.mCurrXform.scale != 0.0f) {
                access$2 = this.mCurrXform.scale;
            }
        }
        this.extractCurrPtInfo();
        this.mCurrXform.set(this.mCurrPtX - this.startPosX * access$2, this.mCurrPtY - this.startPosY * access$2, this.startScaleOverPinchDiam * this.mCurrPtDiam, this.startScaleXOverPinchWidth * this.mCurrPtWidth, this.startScaleYOverPinchHeight * this.mCurrPtHeight, this.startAngleMinusPinchAngle + this.mCurrPtAng);
        this.objectCanvas.setPositionAndScale(this.selectedObject, this.mCurrXform, this.mCurrPt);
    }
    
    public boolean isPinching() {
        return this.mMode == 2;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        try {
            int intValue;
            if (MultiTouchController.multiTouchSupported) {
                intValue = (int)MultiTouchController.m_getPointerCount.invoke(motionEvent, new Object[0]);
            }
            else {
                intValue = 1;
            }
            if (this.mMode == 0 && !this.handleSingleTouchEvents && intValue == 1) {
                return false;
            }
            final int action = motionEvent.getAction();
            for (int n = motionEvent.getHistorySize() / intValue, i = 0; i <= n; ++i) {
                final boolean b = i < n;
                if (MultiTouchController.multiTouchSupported && intValue != 1) {
                    for (int min = Math.min(intValue, 20), j = 0; j < min; ++j) {
                        MultiTouchController.pointerIds[j] = (int)MultiTouchController.m_getPointerId.invoke(motionEvent, j);
                        final float[] xVals = MultiTouchController.xVals;
                        Method method;
                        Object[] args;
                        if (b) {
                            method = MultiTouchController.m_getHistoricalX;
                            args = new Object[] { j, i };
                        }
                        else {
                            method = MultiTouchController.m_getX;
                            args = new Object[] { j };
                        }
                        xVals[j] = (float)method.invoke(motionEvent, args);
                        final float[] yVals = MultiTouchController.yVals;
                        Method method2;
                        Object[] args2;
                        if (b) {
                            method2 = MultiTouchController.m_getHistoricalY;
                            args2 = new Object[] { j, i };
                        }
                        else {
                            method2 = MultiTouchController.m_getY;
                            args2 = new Object[] { j };
                        }
                        yVals[j] = (float)method2.invoke(motionEvent, args2);
                        final float[] pressureVals = MultiTouchController.pressureVals;
                        Object o;
                        if (b) {
                            o = MultiTouchController.m_getHistoricalPressure.invoke(motionEvent, j, i);
                        }
                        else {
                            o = MultiTouchController.m_getPressure.invoke(motionEvent, j);
                        }
                        pressureVals[j] = (float)o;
                    }
                }
                else {
                    final float[] xVals2 = MultiTouchController.xVals;
                    float n2;
                    if (b) {
                        n2 = motionEvent.getHistoricalX(i);
                    }
                    else {
                        n2 = motionEvent.getX();
                    }
                    xVals2[0] = n2;
                    final float[] yVals2 = MultiTouchController.yVals;
                    float n3;
                    if (b) {
                        n3 = motionEvent.getHistoricalY(i);
                    }
                    else {
                        n3 = motionEvent.getY();
                    }
                    yVals2[0] = n3;
                    final float[] pressureVals2 = MultiTouchController.pressureVals;
                    float n4;
                    if (b) {
                        n4 = motionEvent.getHistoricalPressure(i);
                    }
                    else {
                        n4 = motionEvent.getPressure();
                    }
                    pressureVals2[0] = n4;
                }
                final float[] xVals3 = MultiTouchController.xVals;
                final float[] yVals3 = MultiTouchController.yVals;
                final float[] pressureVals3 = MultiTouchController.pressureVals;
                final int[] pointerIds = MultiTouchController.pointerIds;
                int n5;
                if (b) {
                    n5 = 2;
                }
                else {
                    n5 = action;
                }
                final boolean b2 = b || (action != 1 && ((1 << MultiTouchController.ACTION_POINTER_INDEX_SHIFT) - 1 & action) != MultiTouchController.ACTION_POINTER_UP && action != 3);
                long n6;
                if (b) {
                    n6 = motionEvent.getHistoricalEventTime(i);
                }
                else {
                    n6 = motionEvent.getEventTime();
                }
                this.decodeTouchEvent(intValue, xVals3, yVals3, pressureVals3, pointerIds, n5, b2, n6);
            }
            return true;
        }
        catch (Exception ex) {
            Log.e("MultiTouchController", "onTouchEvent() failed", (Throwable)ex);
            return false;
        }
    }
    
    public interface MultiTouchObjectCanvas<T>
    {
        T getDraggableObjectAtPoint(final PointInfo p0);
        
        void getPositionAndScale(final T p0, final PositionAndScale p1);
        
        void selectObject(final T p0, final PointInfo p1);
        
        boolean setPositionAndScale(final T p0, final PositionAndScale p1, final PointInfo p2);
    }
    
    public static class PointInfo
    {
        private int action;
        private float angle;
        private boolean angleIsCalculated;
        private float diameter;
        private boolean diameterIsCalculated;
        private float diameterSq;
        private boolean diameterSqIsCalculated;
        private float dx;
        private float dy;
        private long eventTime;
        private boolean isDown;
        private boolean isMultiTouch;
        private int numPoints;
        private int[] pointerIds;
        private float pressureMid;
        private float[] pressures;
        private float xMid;
        private float[] xs;
        private float yMid;
        private float[] ys;
        
        public PointInfo() {
            this.xs = new float[20];
            this.ys = new float[20];
            this.pressures = new float[20];
            this.pointerIds = new int[20];
        }
        
        private int julery_isqrt(int n) {
            int n2 = 0;
            int n3 = 32768;
            final int n4 = 15;
            int n5 = n;
            n = n4;
            int n7;
            while (true) {
                final int n6 = (n2 << 1) + n3 << n;
                n7 = n2;
                int n8 = n5;
                if (n5 >= n6) {
                    n7 = n2 + n3;
                    n8 = n5 - n6;
                }
                n3 >>= 1;
                if (n3 <= 0) {
                    break;
                }
                --n;
                n2 = n7;
                n5 = n8;
            }
            return n7;
        }
        
        private void set(final int numPoints, final float[] array, final float[] array2, final float[] array3, final int[] array4, int i, final boolean isDown, final long eventTime) {
            this.eventTime = eventTime;
            this.action = i;
            this.numPoints = numPoints;
            for (i = 0; i < numPoints; ++i) {
                this.xs[i] = array[i];
                this.ys[i] = array2[i];
                this.pressures[i] = array3[i];
                this.pointerIds[i] = array4[i];
            }
            this.isDown = isDown;
            this.isMultiTouch = (numPoints >= 2);
            if (this.isMultiTouch) {
                this.xMid = (array[0] + array[1]) * 0.5f;
                this.yMid = (array2[0] + array2[1]) * 0.5f;
                this.pressureMid = (array3[0] + array3[1]) * 0.5f;
                this.dx = Math.abs(array[1] - array[0]);
                this.dy = Math.abs(array2[1] - array2[0]);
            }
            else {
                this.xMid = array[0];
                this.yMid = array2[0];
                this.pressureMid = array3[0];
                this.dy = 0.0f;
                this.dx = 0.0f;
            }
            this.angleIsCalculated = false;
            this.diameterIsCalculated = false;
            this.diameterSqIsCalculated = false;
        }
        
        public long getEventTime() {
            return this.eventTime;
        }
        
        public float getMultiTouchAngle() {
            if (!this.angleIsCalculated) {
                if (!this.isMultiTouch) {
                    this.angle = 0.0f;
                }
                else {
                    final float[] ys = this.ys;
                    final double y = ys[1] - ys[0];
                    final float[] xs = this.xs;
                    this.angle = (float)Math.atan2(y, xs[1] - xs[0]);
                }
                this.angleIsCalculated = true;
            }
            return this.angle;
        }
        
        public float getMultiTouchDiameter() {
            if (!this.diameterIsCalculated) {
                final boolean isMultiTouch = this.isMultiTouch;
                float diameter = 0.0f;
                if (!isMultiTouch) {
                    this.diameter = 0.0f;
                }
                else {
                    final float multiTouchDiameterSq = this.getMultiTouchDiameterSq();
                    if (multiTouchDiameterSq != 0.0f) {
                        diameter = this.julery_isqrt((int)(multiTouchDiameterSq * 256.0f)) / 16.0f;
                    }
                    this.diameter = diameter;
                    final float diameter2 = this.diameter;
                    final float dx = this.dx;
                    if (diameter2 < dx) {
                        this.diameter = dx;
                    }
                    final float diameter3 = this.diameter;
                    final float dy = this.dy;
                    if (diameter3 < dy) {
                        this.diameter = dy;
                    }
                }
                this.diameterIsCalculated = true;
            }
            return this.diameter;
        }
        
        public float getMultiTouchDiameterSq() {
            if (!this.diameterSqIsCalculated) {
                float diameterSq;
                if (this.isMultiTouch) {
                    final float dx = this.dx;
                    final float dy = this.dy;
                    diameterSq = dx * dx + dy * dy;
                }
                else {
                    diameterSq = 0.0f;
                }
                this.diameterSq = diameterSq;
                this.diameterSqIsCalculated = true;
            }
            return this.diameterSq;
        }
        
        public float getMultiTouchHeight() {
            float dy;
            if (this.isMultiTouch) {
                dy = this.dy;
            }
            else {
                dy = 0.0f;
            }
            return dy;
        }
        
        public float getMultiTouchWidth() {
            float dx;
            if (this.isMultiTouch) {
                dx = this.dx;
            }
            else {
                dx = 0.0f;
            }
            return dx;
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
        
        public boolean isMultiTouch() {
            return this.isMultiTouch;
        }
    }
    
    public static class PositionAndScale
    {
        private float angle;
        private float scale;
        private float scaleX;
        private float scaleY;
        private boolean updateAngle;
        private boolean updateScale;
        private boolean updateScaleXY;
        private float xOff;
        private float yOff;
        
        public float getScale() {
            float scale;
            if (!this.updateScale) {
                scale = 1.0f;
            }
            else {
                scale = this.scale;
            }
            return scale;
        }
        
        public float getXOff() {
            return this.xOff;
        }
        
        public float getYOff() {
            return this.yOff;
        }
        
        protected void set(float scaleX, float yOff, final float n, final float n2, float scaleY, final float angle) {
            this.xOff = scaleX;
            this.yOff = yOff;
            yOff = 1.0f;
            scaleX = n;
            if (n == 0.0f) {
                scaleX = 1.0f;
            }
            this.scale = scaleX;
            scaleX = n2;
            if (n2 == 0.0f) {
                scaleX = 1.0f;
            }
            this.scaleX = scaleX;
            if (scaleY == 0.0f) {
                scaleY = yOff;
            }
            this.scaleY = scaleY;
            this.angle = angle;
        }
        
        public void set(float n, float yOff, final boolean updateScale, final float n2, final boolean updateScaleXY, final float n3, final float n4, final boolean updateAngle, final float angle) {
            this.xOff = n;
            this.yOff = yOff;
            this.updateScale = updateScale;
            yOff = 1.0f;
            n = n2;
            if (n2 == 0.0f) {
                n = 1.0f;
            }
            this.scale = n;
            this.updateScaleXY = updateScaleXY;
            n = n3;
            if (n3 == 0.0f) {
                n = 1.0f;
            }
            this.scaleX = n;
            if (n4 == 0.0f) {
                n = yOff;
            }
            else {
                n = n4;
            }
            this.scaleY = n;
            this.updateAngle = updateAngle;
            this.angle = angle;
        }
    }
}
