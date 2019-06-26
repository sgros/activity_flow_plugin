// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ui;

import android.view.View;
import android.view.TextureView;
import android.view.View$MeasureSpec;
import android.content.Context;
import android.graphics.Matrix;
import android.widget.FrameLayout;

public class AspectRatioFrameLayout extends FrameLayout
{
    private static final float MAX_ASPECT_RATIO_DEFORMATION_FRACTION = 0.01f;
    public static final int RESIZE_MODE_FILL = 3;
    public static final int RESIZE_MODE_FIT = 0;
    public static final int RESIZE_MODE_FIXED_HEIGHT = 2;
    public static final int RESIZE_MODE_FIXED_WIDTH = 1;
    public static final int RESIZE_MODE_ZOOM = 4;
    private AspectRatioListener aspectRatioListener;
    private final AspectRatioUpdateDispatcher aspectRatioUpdateDispatcher;
    private boolean drawingReady;
    private Matrix matrix;
    private int resizeMode;
    private int rotation;
    private float videoAspectRatio;
    
    public AspectRatioFrameLayout(final Context context) {
        super(context);
        this.matrix = new Matrix();
        this.resizeMode = 0;
        this.aspectRatioUpdateDispatcher = new AspectRatioUpdateDispatcher();
    }
    
    public float getAspectRatio() {
        return this.videoAspectRatio;
    }
    
    public int getResizeMode() {
        return this.resizeMode;
    }
    
    public int getVideoRotation() {
        return this.rotation;
    }
    
    public boolean isDrawingReady() {
        return this.drawingReady;
    }
    
    protected void onMeasure(int i, int n) {
        super.onMeasure(i, n);
        if (this.videoAspectRatio <= 0.0f) {
            return;
        }
        n = this.getMeasuredWidth();
        i = this.getMeasuredHeight();
        final float n2 = (float)n;
        final float n3 = (float)i;
        final float n4 = n2 / n3;
        final float a = this.videoAspectRatio / n4 - 1.0f;
        final float abs = Math.abs(a);
        final int n5 = 0;
        if (abs <= 0.01f) {
            this.aspectRatioUpdateDispatcher.scheduleUpdate(this.videoAspectRatio, n4, false);
            return;
        }
        final int resizeMode = this.resizeMode;
        Label_0227: {
            float n6 = 0.0f;
            Label_0193: {
                float n7;
                if (resizeMode != 0) {
                    if (resizeMode == 1) {
                        n6 = this.videoAspectRatio;
                        break Label_0193;
                    }
                    if (resizeMode != 2) {
                        if (resizeMode != 3) {
                            if (resizeMode != 4) {
                                break Label_0227;
                            }
                            if (a <= 0.0f) {
                                n6 = this.videoAspectRatio;
                                break Label_0193;
                            }
                            n7 = this.videoAspectRatio;
                        }
                        else {
                            if (a <= 0.0f) {
                                n6 = this.videoAspectRatio;
                                break Label_0193;
                            }
                            n7 = this.videoAspectRatio;
                        }
                    }
                    else {
                        n7 = this.videoAspectRatio;
                    }
                }
                else {
                    if (a > 0.0f) {
                        n6 = this.videoAspectRatio;
                        break Label_0193;
                    }
                    n7 = this.videoAspectRatio;
                }
                n = (int)(n3 * n7);
                break Label_0227;
            }
            i = (int)(n2 / n6);
        }
        this.aspectRatioUpdateDispatcher.scheduleUpdate(this.videoAspectRatio, n4, true);
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(n, 1073741824), View$MeasureSpec.makeMeasureSpec(i, 1073741824));
        View child;
        Matrix matrix;
        float n8;
        float n9;
        float n10;
        float n11;
        for (n = this.getChildCount(), i = n5; i < n; ++i) {
            child = this.getChildAt(i);
            if (child instanceof TextureView) {
                this.matrix.reset();
                n = this.getWidth() / 2;
                i = this.getHeight() / 2;
                matrix = this.matrix;
                n8 = (float)this.rotation;
                n9 = (float)n;
                n10 = (float)i;
                matrix.postRotate(n8, n9, n10);
                i = this.rotation;
                if (i == 90 || i == 270) {
                    n11 = this.getHeight() / (float)this.getWidth();
                    this.matrix.postScale(1.0f / n11, n11, n9, n10);
                }
                ((TextureView)child).setTransform(this.matrix);
                break;
            }
        }
    }
    
    public void setAspectRatio(final float videoAspectRatio, final int rotation) {
        if (this.videoAspectRatio != videoAspectRatio) {
            this.videoAspectRatio = videoAspectRatio;
            this.rotation = rotation;
            this.requestLayout();
        }
    }
    
    public void setAspectRatioListener(final AspectRatioListener aspectRatioListener) {
        this.aspectRatioListener = aspectRatioListener;
    }
    
    public void setDrawingReady(final boolean drawingReady) {
        if (this.drawingReady == drawingReady) {
            return;
        }
        this.drawingReady = drawingReady;
    }
    
    public void setResizeMode(final int resizeMode) {
        if (this.resizeMode != resizeMode) {
            this.resizeMode = resizeMode;
            this.requestLayout();
        }
    }
    
    public interface AspectRatioListener
    {
        void onAspectRatioUpdated(final float p0, final float p1, final boolean p2);
    }
    
    private final class AspectRatioUpdateDispatcher implements Runnable
    {
        private boolean aspectRatioMismatch;
        private boolean isScheduled;
        private float naturalAspectRatio;
        private float targetAspectRatio;
        
        @Override
        public void run() {
            this.isScheduled = false;
            if (AspectRatioFrameLayout.this.aspectRatioListener == null) {
                return;
            }
            AspectRatioFrameLayout.this.aspectRatioListener.onAspectRatioUpdated(this.targetAspectRatio, this.naturalAspectRatio, this.aspectRatioMismatch);
        }
        
        public void scheduleUpdate(final float targetAspectRatio, final float naturalAspectRatio, final boolean aspectRatioMismatch) {
            this.targetAspectRatio = targetAspectRatio;
            this.naturalAspectRatio = naturalAspectRatio;
            this.aspectRatioMismatch = aspectRatioMismatch;
            if (!this.isScheduled) {
                this.isScheduled = true;
                AspectRatioFrameLayout.this.post((Runnable)this);
            }
        }
    }
}
