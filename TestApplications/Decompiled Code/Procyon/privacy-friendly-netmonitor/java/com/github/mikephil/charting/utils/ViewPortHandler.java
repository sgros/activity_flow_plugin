// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.utils;

import android.view.View;
import android.graphics.RectF;
import android.graphics.Matrix;

public class ViewPortHandler
{
    protected Matrix mCenterViewPortMatrixBuffer;
    protected float mChartHeight;
    protected float mChartWidth;
    protected RectF mContentRect;
    protected final Matrix mMatrixTouch;
    private float mMaxScaleX;
    private float mMaxScaleY;
    private float mMinScaleX;
    private float mMinScaleY;
    private float mScaleX;
    private float mScaleY;
    private float mTransOffsetX;
    private float mTransOffsetY;
    private float mTransX;
    private float mTransY;
    protected final float[] matrixBuffer;
    protected float[] valsBufferForFitScreen;
    
    public ViewPortHandler() {
        this.mMatrixTouch = new Matrix();
        this.mContentRect = new RectF();
        this.mChartWidth = 0.0f;
        this.mChartHeight = 0.0f;
        this.mMinScaleY = 1.0f;
        this.mMaxScaleY = Float.MAX_VALUE;
        this.mMinScaleX = 1.0f;
        this.mMaxScaleX = Float.MAX_VALUE;
        this.mScaleX = 1.0f;
        this.mScaleY = 1.0f;
        this.mTransX = 0.0f;
        this.mTransY = 0.0f;
        this.mTransOffsetX = 0.0f;
        this.mTransOffsetY = 0.0f;
        this.valsBufferForFitScreen = new float[9];
        this.mCenterViewPortMatrixBuffer = new Matrix();
        this.matrixBuffer = new float[9];
    }
    
    public boolean canZoomInMoreX() {
        return this.mScaleX < this.mMaxScaleX;
    }
    
    public boolean canZoomInMoreY() {
        return this.mScaleY < this.mMaxScaleY;
    }
    
    public boolean canZoomOutMoreX() {
        return this.mScaleX > this.mMinScaleX;
    }
    
    public boolean canZoomOutMoreY() {
        return this.mScaleY > this.mMinScaleY;
    }
    
    public void centerViewPort(final float[] array, final View view) {
        final Matrix mCenterViewPortMatrixBuffer = this.mCenterViewPortMatrixBuffer;
        mCenterViewPortMatrixBuffer.reset();
        mCenterViewPortMatrixBuffer.set(this.mMatrixTouch);
        mCenterViewPortMatrixBuffer.postTranslate(-(array[0] - this.offsetLeft()), -(array[1] - this.offsetTop()));
        this.refresh(mCenterViewPortMatrixBuffer, view, true);
    }
    
    public float contentBottom() {
        return this.mContentRect.bottom;
    }
    
    public float contentHeight() {
        return this.mContentRect.height();
    }
    
    public float contentLeft() {
        return this.mContentRect.left;
    }
    
    public float contentRight() {
        return this.mContentRect.right;
    }
    
    public float contentTop() {
        return this.mContentRect.top;
    }
    
    public float contentWidth() {
        return this.mContentRect.width();
    }
    
    public Matrix fitScreen() {
        final Matrix matrix = new Matrix();
        this.fitScreen(matrix);
        return matrix;
    }
    
    public void fitScreen(final Matrix matrix) {
        this.mMinScaleX = 1.0f;
        this.mMinScaleY = 1.0f;
        matrix.set(this.mMatrixTouch);
        final float[] valsBufferForFitScreen = this.valsBufferForFitScreen;
        for (int i = 0; i < 9; ++i) {
            valsBufferForFitScreen[i] = 0.0f;
        }
        matrix.getValues(valsBufferForFitScreen);
        valsBufferForFitScreen[5] = (valsBufferForFitScreen[2] = 0.0f);
        valsBufferForFitScreen[4] = (valsBufferForFitScreen[0] = 1.0f);
        matrix.setValues(valsBufferForFitScreen);
    }
    
    public float getChartHeight() {
        return this.mChartHeight;
    }
    
    public float getChartWidth() {
        return this.mChartWidth;
    }
    
    public MPPointF getContentCenter() {
        return MPPointF.getInstance(this.mContentRect.centerX(), this.mContentRect.centerY());
    }
    
    public RectF getContentRect() {
        return this.mContentRect;
    }
    
    public Matrix getMatrixTouch() {
        return this.mMatrixTouch;
    }
    
    public float getMaxScaleX() {
        return this.mMaxScaleX;
    }
    
    public float getMaxScaleY() {
        return this.mMaxScaleY;
    }
    
    public float getMinScaleX() {
        return this.mMinScaleX;
    }
    
    public float getMinScaleY() {
        return this.mMinScaleY;
    }
    
    public float getScaleX() {
        return this.mScaleX;
    }
    
    public float getScaleY() {
        return this.mScaleY;
    }
    
    public float getSmallestContentExtension() {
        return Math.min(this.mContentRect.width(), this.mContentRect.height());
    }
    
    public float getTransX() {
        return this.mTransX;
    }
    
    public float getTransY() {
        return this.mTransY;
    }
    
    public boolean hasChartDimens() {
        return this.mChartHeight > 0.0f && this.mChartWidth > 0.0f;
    }
    
    public boolean hasNoDragOffset() {
        return this.mTransOffsetX <= 0.0f && this.mTransOffsetY <= 0.0f;
    }
    
    public boolean isFullyZoomedOut() {
        return this.isFullyZoomedOutX() && this.isFullyZoomedOutY();
    }
    
    public boolean isFullyZoomedOutX() {
        return this.mScaleX <= this.mMinScaleX && this.mMinScaleX <= 1.0f;
    }
    
    public boolean isFullyZoomedOutY() {
        return this.mScaleY <= this.mMinScaleY && this.mMinScaleY <= 1.0f;
    }
    
    public boolean isInBounds(final float n, final float n2) {
        return this.isInBoundsX(n) && this.isInBoundsY(n2);
    }
    
    public boolean isInBoundsBottom(float n) {
        n = (int)(n * 100.0f) / 100.0f;
        return this.mContentRect.bottom >= n;
    }
    
    public boolean isInBoundsLeft(final float n) {
        return this.mContentRect.left <= n + 1.0f;
    }
    
    public boolean isInBoundsRight(float n) {
        n = (int)(n * 100.0f) / 100.0f;
        return this.mContentRect.right >= n - 1.0f;
    }
    
    public boolean isInBoundsTop(final float n) {
        return this.mContentRect.top <= n;
    }
    
    public boolean isInBoundsX(final float n) {
        return this.isInBoundsLeft(n) && this.isInBoundsRight(n);
    }
    
    public boolean isInBoundsY(final float n) {
        return this.isInBoundsTop(n) && this.isInBoundsBottom(n);
    }
    
    public void limitTransAndScale(final Matrix matrix, final RectF rectF) {
        matrix.getValues(this.matrixBuffer);
        final float a = this.matrixBuffer[2];
        final float b = this.matrixBuffer[0];
        final float a2 = this.matrixBuffer[5];
        final float b2 = this.matrixBuffer[4];
        this.mScaleX = Math.min(Math.max(this.mMinScaleX, b), this.mMaxScaleX);
        this.mScaleY = Math.min(Math.max(this.mMinScaleY, b2), this.mMaxScaleY);
        float width = 0.0f;
        float height;
        if (rectF != null) {
            width = rectF.width();
            height = rectF.height();
        }
        else {
            height = 0.0f;
        }
        this.mTransX = Math.min(Math.max(a, -width * (this.mScaleX - 1.0f) - this.mTransOffsetX), this.mTransOffsetX);
        this.mTransY = Math.max(Math.min(a2, height * (this.mScaleY - 1.0f) + this.mTransOffsetY), -this.mTransOffsetY);
        this.matrixBuffer[2] = this.mTransX;
        this.matrixBuffer[0] = this.mScaleX;
        this.matrixBuffer[5] = this.mTransY;
        this.matrixBuffer[4] = this.mScaleY;
        matrix.setValues(this.matrixBuffer);
    }
    
    public float offsetBottom() {
        return this.mChartHeight - this.mContentRect.bottom;
    }
    
    public float offsetLeft() {
        return this.mContentRect.left;
    }
    
    public float offsetRight() {
        return this.mChartWidth - this.mContentRect.right;
    }
    
    public float offsetTop() {
        return this.mContentRect.top;
    }
    
    public Matrix refresh(final Matrix matrix, final View view, final boolean b) {
        this.mMatrixTouch.set(matrix);
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
        if (b) {
            view.invalidate();
        }
        matrix.set(this.mMatrixTouch);
        return matrix;
    }
    
    public void resetZoom(final Matrix matrix) {
        matrix.reset();
        matrix.set(this.mMatrixTouch);
        matrix.postScale(1.0f, 1.0f, 0.0f, 0.0f);
    }
    
    public void restrainViewPort(final float n, final float n2, final float n3, final float n4) {
        this.mContentRect.set(n, n2, this.mChartWidth - n3, this.mChartHeight - n4);
    }
    
    public void setChartDimens(final float mChartWidth, final float mChartHeight) {
        final float offsetLeft = this.offsetLeft();
        final float offsetTop = this.offsetTop();
        final float offsetRight = this.offsetRight();
        final float offsetBottom = this.offsetBottom();
        this.mChartHeight = mChartHeight;
        this.mChartWidth = mChartWidth;
        this.restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom);
    }
    
    public void setDragOffsetX(final float n) {
        this.mTransOffsetX = Utils.convertDpToPixel(n);
    }
    
    public void setDragOffsetY(final float n) {
        this.mTransOffsetY = Utils.convertDpToPixel(n);
    }
    
    public void setMaximumScaleX(final float n) {
        float mMaxScaleX = n;
        if (n == 0.0f) {
            mMaxScaleX = Float.MAX_VALUE;
        }
        this.mMaxScaleX = mMaxScaleX;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }
    
    public void setMaximumScaleY(final float n) {
        float mMaxScaleY = n;
        if (n == 0.0f) {
            mMaxScaleY = Float.MAX_VALUE;
        }
        this.mMaxScaleY = mMaxScaleY;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }
    
    public void setMinMaxScaleX(float mMaxScaleX, final float n) {
        float mMinScaleX = mMaxScaleX;
        if (mMaxScaleX < 1.0f) {
            mMinScaleX = 1.0f;
        }
        mMaxScaleX = n;
        if (n == 0.0f) {
            mMaxScaleX = Float.MAX_VALUE;
        }
        this.mMinScaleX = mMinScaleX;
        this.mMaxScaleX = mMaxScaleX;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }
    
    public void setMinMaxScaleY(float mMaxScaleY, final float n) {
        float mMinScaleY = mMaxScaleY;
        if (mMaxScaleY < 1.0f) {
            mMinScaleY = 1.0f;
        }
        mMaxScaleY = n;
        if (n == 0.0f) {
            mMaxScaleY = Float.MAX_VALUE;
        }
        this.mMinScaleY = mMinScaleY;
        this.mMaxScaleY = mMaxScaleY;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }
    
    public void setMinimumScaleX(final float n) {
        float mMinScaleX = n;
        if (n < 1.0f) {
            mMinScaleX = 1.0f;
        }
        this.mMinScaleX = mMinScaleX;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }
    
    public void setMinimumScaleY(final float n) {
        float mMinScaleY = n;
        if (n < 1.0f) {
            mMinScaleY = 1.0f;
        }
        this.mMinScaleY = mMinScaleY;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }
    
    public Matrix setZoom(final float n, final float n2) {
        final Matrix matrix = new Matrix();
        this.setZoom(n, n2, matrix);
        return matrix;
    }
    
    public Matrix setZoom(final float n, final float n2, final float n3, final float n4) {
        final Matrix matrix = new Matrix();
        matrix.set(this.mMatrixTouch);
        matrix.setScale(n, n2, n3, n4);
        return matrix;
    }
    
    public void setZoom(final float n, final float n2, final Matrix matrix) {
        matrix.reset();
        matrix.set(this.mMatrixTouch);
        matrix.setScale(n, n2);
    }
    
    public Matrix translate(final float[] array) {
        final Matrix matrix = new Matrix();
        this.translate(array, matrix);
        return matrix;
    }
    
    public void translate(final float[] array, final Matrix matrix) {
        matrix.reset();
        matrix.set(this.mMatrixTouch);
        matrix.postTranslate(-(array[0] - this.offsetLeft()), -(array[1] - this.offsetTop()));
    }
    
    public Matrix zoom(final float n, final float n2) {
        final Matrix matrix = new Matrix();
        this.zoom(n, n2, matrix);
        return matrix;
    }
    
    public Matrix zoom(final float n, final float n2, final float n3, final float n4) {
        final Matrix matrix = new Matrix();
        this.zoom(n, n2, n3, n4, matrix);
        return matrix;
    }
    
    public void zoom(final float n, final float n2, final float n3, final float n4, final Matrix matrix) {
        matrix.reset();
        matrix.set(this.mMatrixTouch);
        matrix.postScale(n, n2, n3, n4);
    }
    
    public void zoom(final float n, final float n2, final Matrix matrix) {
        matrix.reset();
        matrix.set(this.mMatrixTouch);
        matrix.postScale(n, n2);
    }
    
    public Matrix zoomIn(final float n, final float n2) {
        final Matrix matrix = new Matrix();
        this.zoomIn(n, n2, matrix);
        return matrix;
    }
    
    public void zoomIn(final float n, final float n2, final Matrix matrix) {
        matrix.reset();
        matrix.set(this.mMatrixTouch);
        matrix.postScale(1.4f, 1.4f, n, n2);
    }
    
    public Matrix zoomOut(final float n, final float n2) {
        final Matrix matrix = new Matrix();
        this.zoomOut(n, n2, matrix);
        return matrix;
    }
    
    public void zoomOut(final float n, final float n2, final Matrix matrix) {
        matrix.reset();
        matrix.set(this.mMatrixTouch);
        matrix.postScale(0.7f, 0.7f, n, n2);
    }
}
