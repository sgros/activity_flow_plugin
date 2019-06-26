// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.camera;

import android.hardware.Camera;
import android.graphics.Canvas;
import java.util.concurrent.CountDownLatch;
import android.graphics.SurfaceTexture;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.graphics.RectF;
import android.graphics.Rect;
import android.graphics.Paint$Style;
import org.telegram.messenger.AndroidUtilities;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.view.TextureView;
import android.graphics.Matrix;
import android.view.animation.DecelerateInterpolator;
import android.graphics.Paint;
import android.annotation.SuppressLint;
import android.view.TextureView$SurfaceTextureListener;
import android.widget.FrameLayout;

@SuppressLint({ "NewApi" })
public class CameraView extends FrameLayout implements TextureView$SurfaceTextureListener
{
    private CameraSession cameraSession;
    private int clipLeft;
    private int clipTop;
    private int cx;
    private int cy;
    private CameraViewDelegate delegate;
    private int focusAreaSize;
    private float focusProgress;
    private boolean initialFrontface;
    private boolean initied;
    private float innerAlpha;
    private Paint innerPaint;
    private DecelerateInterpolator interpolator;
    private boolean isFrontface;
    private long lastDrawTime;
    private Matrix matrix;
    private boolean mirror;
    private float outerAlpha;
    private Paint outerPaint;
    private Size previewSize;
    private TextureView textureView;
    private Matrix txform;
    
    public CameraView(final Context context, final boolean b) {
        super(context, (AttributeSet)null);
        this.txform = new Matrix();
        this.matrix = new Matrix();
        this.focusProgress = 1.0f;
        this.outerPaint = new Paint(1);
        this.innerPaint = new Paint(1);
        this.interpolator = new DecelerateInterpolator();
        this.isFrontface = b;
        this.initialFrontface = b;
        (this.textureView = new TextureView(context)).setSurfaceTextureListener((TextureView$SurfaceTextureListener)this);
        this.addView((View)this.textureView);
        this.focusAreaSize = AndroidUtilities.dp(96.0f);
        this.outerPaint.setColor(-1);
        this.outerPaint.setStyle(Paint$Style.STROKE);
        this.outerPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.innerPaint.setColor(Integer.MAX_VALUE);
    }
    
    private void adjustAspectRatio(final int n, final int n2, final int n3) {
        this.txform.reset();
        final int width = this.getWidth();
        final int height = this.getHeight();
        final float n4 = (float)(width / 2);
        final float n5 = (float)(height / 2);
        float n6;
        if (n3 != 0 && n3 != 2) {
            n6 = Math.max((this.clipTop + height) / (float)n2, (this.clipLeft + width) / (float)n);
        }
        else {
            n6 = Math.max((this.clipTop + height) / (float)n, (this.clipLeft + width) / (float)n2);
        }
        final float n7 = (float)n;
        final float n8 = (float)n2;
        final float n9 = (float)width;
        final float n10 = n8 * n6 / n9;
        final float n11 = (float)height;
        this.txform.postScale(n10, n7 * n6 / n11, n4, n5);
        if (1 != n3 && 3 != n3) {
            if (2 == n3) {
                this.txform.postRotate(180.0f, n4, n5);
            }
        }
        else {
            this.txform.postRotate((float)((n3 - 2) * 90), n4, n5);
        }
        if (this.mirror) {
            this.txform.postScale(-1.0f, 1.0f, n4, n5);
        }
        if (this.clipTop != 0 || this.clipLeft != 0) {
            this.txform.postTranslate((float)(-this.clipLeft / 2), (float)(-this.clipTop / 2));
        }
        this.textureView.setTransform(this.txform);
        final Matrix matrix = new Matrix();
        matrix.postRotate((float)this.cameraSession.getDisplayOrientation());
        matrix.postScale(n9 / 2000.0f, n11 / 2000.0f);
        matrix.postTranslate(n9 / 2.0f, n11 / 2.0f);
        matrix.invert(this.matrix);
    }
    
    private Rect calculateTapArea(final float n, final float n2, final float n3) {
        final int intValue = (this.focusAreaSize * n3).intValue();
        final int n4 = (int)n;
        final int n5 = intValue / 2;
        final int clamp = this.clamp(n4 - n5, 0, this.getWidth() - intValue);
        final int clamp2 = this.clamp((int)n2 - n5, 0, this.getHeight() - intValue);
        final RectF rectF = new RectF((float)clamp, (float)clamp2, (float)(clamp + intValue), (float)(clamp2 + intValue));
        this.matrix.mapRect(rectF);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }
    
    private void checkPreviewMatrix() {
        final Size previewSize = this.previewSize;
        if (previewSize == null) {
            return;
        }
        this.adjustAspectRatio(previewSize.getWidth(), this.previewSize.getHeight(), ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getRotation());
    }
    
    private int clamp(final int n, final int n2, final int n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }
    
    private void initCamera() {
        final ArrayList<CameraInfo> cameras = CameraController.getInstance().getCameras();
        if (cameras == null) {
            return;
        }
        int i = 0;
    Label_0077:
        while (true) {
            while (i < cameras.size()) {
                final CameraInfo cameraInfo = cameras.get(i);
                if (this.isFrontface) {
                    final CameraInfo cameraInfo2 = cameraInfo;
                    if (cameraInfo.frontCamera != 0) {
                        break Label_0077;
                    }
                }
                if (this.isFrontface || cameraInfo.frontCamera != 0) {
                    ++i;
                    continue;
                }
                final CameraInfo cameraInfo2 = cameraInfo;
                if (cameraInfo2 == null) {
                    return;
                }
                final Point displaySize = AndroidUtilities.displaySize;
                final float n = (float)Math.max(displaySize.x, displaySize.y);
                final Point displaySize2 = AndroidUtilities.displaySize;
                final float n2 = n / Math.min(displaySize2.x, displaySize2.y);
                Size size;
                int n3;
                int n4;
                if (this.initialFrontface) {
                    size = new Size(16, 9);
                    n3 = 480;
                    n4 = 270;
                }
                else {
                    int n5;
                    if (Math.abs(n2 - 1.3333334f) < 0.1f) {
                        size = new Size(4, 3);
                        n5 = 960;
                    }
                    else {
                        size = new Size(16, 9);
                        n5 = 720;
                    }
                    final int n6 = 1280;
                    n4 = n5;
                    n3 = n6;
                }
                if (this.textureView.getWidth() > 0 && this.textureView.getHeight() > 0) {
                    final Point displaySize3 = AndroidUtilities.displaySize;
                    final int min = Math.min(displaySize3.x, displaySize3.y);
                    this.previewSize = CameraController.chooseOptimalSize(cameraInfo2.getPreviewSizes(), min, size.getHeight() * min / size.getWidth(), size);
                }
                final Size chooseOptimalSize = CameraController.chooseOptimalSize(cameraInfo2.getPictureSizes(), n3, n4, size);
                Size chooseOptimalSize2 = null;
                Label_0402: {
                    if (chooseOptimalSize.getWidth() >= 1280 && chooseOptimalSize.getHeight() >= 1280) {
                        Size size2;
                        if (Math.abs(n2 - 1.3333334f) < 0.1f) {
                            size2 = new Size(3, 4);
                        }
                        else {
                            size2 = new Size(9, 16);
                        }
                        final Size size3 = chooseOptimalSize2 = CameraController.chooseOptimalSize(cameraInfo2.getPictureSizes(), n4, n3, size2);
                        if (size3.getWidth() < 1280) {
                            break Label_0402;
                        }
                        if (size3.getHeight() < 1280) {
                            chooseOptimalSize2 = size3;
                            break Label_0402;
                        }
                    }
                    chooseOptimalSize2 = chooseOptimalSize;
                }
                final SurfaceTexture surfaceTexture = this.textureView.getSurfaceTexture();
                final Size previewSize = this.previewSize;
                if (previewSize != null && surfaceTexture != null) {
                    surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), this.previewSize.getHeight());
                    this.cameraSession = new CameraSession(cameraInfo2, this.previewSize, chooseOptimalSize2, 256);
                    CameraController.getInstance().open(this.cameraSession, surfaceTexture, new Runnable() {
                        @Override
                        public void run() {
                            if (CameraView.this.cameraSession != null) {
                                CameraView.this.cameraSession.setInitied();
                            }
                            CameraView.this.checkPreviewMatrix();
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            if (CameraView.this.delegate != null) {
                                CameraView.this.delegate.onCameraCreated(CameraView.this.cameraSession.cameraInfo.camera);
                            }
                        }
                    });
                }
                return;
            }
            final CameraInfo cameraInfo2 = null;
            continue Label_0077;
        }
    }
    
    public void destroy(final boolean b, final Runnable runnable) {
        final CameraSession cameraSession = this.cameraSession;
        if (cameraSession != null) {
            cameraSession.destroy();
            final CameraController instance = CameraController.getInstance();
            final CameraSession cameraSession2 = this.cameraSession;
            CountDownLatch countDownLatch;
            if (!b) {
                countDownLatch = new CountDownLatch(1);
            }
            else {
                countDownLatch = null;
            }
            instance.close(cameraSession2, countDownLatch, runnable);
        }
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, long n) {
        final boolean drawChild = super.drawChild(canvas, view, n);
        if (this.focusProgress != 1.0f || this.innerAlpha != 0.0f || this.outerAlpha != 0.0f) {
            final int dp = AndroidUtilities.dp(30.0f);
            final long currentTimeMillis = System.currentTimeMillis();
            final long n2 = currentTimeMillis - this.lastDrawTime;
            Label_0081: {
                if (n2 >= 0L) {
                    n = n2;
                    if (n2 <= 17L) {
                        break Label_0081;
                    }
                }
                n = 17L;
            }
            this.lastDrawTime = currentTimeMillis;
            this.outerPaint.setAlpha((int)(this.interpolator.getInterpolation(this.outerAlpha) * 255.0f));
            this.innerPaint.setAlpha((int)(this.interpolator.getInterpolation(this.innerAlpha) * 127.0f));
            final float interpolation = this.interpolator.getInterpolation(this.focusProgress);
            final float n3 = (float)this.cx;
            final float n4 = (float)this.cy;
            final float n5 = (float)dp;
            canvas.drawCircle(n3, n4, (1.0f - interpolation) * n5 + n5, this.outerPaint);
            canvas.drawCircle((float)this.cx, (float)this.cy, n5 * interpolation, this.innerPaint);
            final float focusProgress = this.focusProgress;
            if (focusProgress < 1.0f) {
                this.focusProgress = focusProgress + n / 200.0f;
                if (this.focusProgress > 1.0f) {
                    this.focusProgress = 1.0f;
                }
                this.invalidate();
            }
            else {
                final float innerAlpha = this.innerAlpha;
                if (innerAlpha != 0.0f) {
                    this.innerAlpha = innerAlpha - n / 150.0f;
                    if (this.innerAlpha < 0.0f) {
                        this.innerAlpha = 0.0f;
                    }
                    this.invalidate();
                }
                else {
                    final float outerAlpha = this.outerAlpha;
                    if (outerAlpha != 0.0f) {
                        this.outerAlpha = outerAlpha - n / 150.0f;
                        if (this.outerAlpha < 0.0f) {
                            this.outerAlpha = 0.0f;
                        }
                        this.invalidate();
                    }
                }
            }
        }
        return drawChild;
    }
    
    public void focusToPoint(final int cx, final int cy) {
        final float n = (float)cx;
        final float n2 = (float)cy;
        final Rect calculateTapArea = this.calculateTapArea(n, n2, 1.0f);
        final Rect calculateTapArea2 = this.calculateTapArea(n, n2, 1.5f);
        final CameraSession cameraSession = this.cameraSession;
        if (cameraSession != null) {
            cameraSession.focusToRect(calculateTapArea, calculateTapArea2);
        }
        this.focusProgress = 0.0f;
        this.innerAlpha = 1.0f;
        this.outerAlpha = 1.0f;
        this.cx = cx;
        this.cy = cy;
        this.lastDrawTime = System.currentTimeMillis();
        this.invalidate();
    }
    
    public CameraSession getCameraSession() {
        return this.cameraSession;
    }
    
    public Size getPreviewSize() {
        return this.previewSize;
    }
    
    public boolean hasFrontFaceCamera() {
        final ArrayList<CameraInfo> cameras = CameraController.getInstance().getCameras();
        for (int i = 0; i < cameras.size(); ++i) {
            if (cameras.get(i).frontCamera != 0) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isFrontface() {
        return this.isFrontface;
    }
    
    public boolean isInitied() {
        return this.initied;
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.checkPreviewMatrix();
    }
    
    public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
        this.initCamera();
    }
    
    public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
        if (this.cameraSession != null) {
            CameraController.getInstance().close(this.cameraSession, null, null);
        }
        return false;
    }
    
    public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
        this.checkPreviewMatrix();
    }
    
    public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
        if (!this.initied) {
            final CameraSession cameraSession = this.cameraSession;
            if (cameraSession != null && cameraSession.isInitied()) {
                final CameraViewDelegate delegate = this.delegate;
                if (delegate != null) {
                    delegate.onCameraInit();
                }
                this.initied = true;
            }
        }
    }
    
    public void setClipLeft(final int clipLeft) {
        this.clipLeft = clipLeft;
    }
    
    public void setClipTop(final int clipTop) {
        this.clipTop = clipTop;
    }
    
    public void setDelegate(final CameraViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setMirror(final boolean mirror) {
        this.mirror = mirror;
    }
    
    public void switchCamera() {
        if (this.cameraSession != null) {
            CameraController.getInstance().close(this.cameraSession, null, null);
            this.cameraSession = null;
        }
        this.initied = false;
        this.isFrontface ^= true;
        this.initCamera();
    }
    
    public interface CameraViewDelegate
    {
        void onCameraCreated(final Camera p0);
        
        void onCameraInit();
    }
}
