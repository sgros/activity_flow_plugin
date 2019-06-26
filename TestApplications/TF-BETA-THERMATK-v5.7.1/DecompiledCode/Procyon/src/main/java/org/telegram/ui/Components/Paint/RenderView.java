// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import android.os.Looper;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.FileLog;
import android.opengl.GLUtils;
import org.telegram.messenger.BuildVars;
import android.opengl.GLES20;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGL10;
import android.view.MotionEvent;
import org.telegram.ui.Components.Size;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.view.TextureView$SurfaceTextureListener;
import android.content.Context;
import org.telegram.messenger.DispatchQueue;
import android.graphics.Bitmap;
import android.view.TextureView;

public class RenderView extends TextureView
{
    private Bitmap bitmap;
    private Brush brush;
    private int color;
    private RenderViewDelegate delegate;
    private Input input;
    private CanvasInternal internal;
    private int orientation;
    private Painting painting;
    private DispatchQueue queue;
    private boolean shuttingDown;
    private boolean transformedBitmap;
    private UndoStore undoStore;
    private float weight;
    
    public RenderView(final Context context, final Painting painting, final Bitmap bitmap, final int orientation) {
        super(context);
        this.bitmap = bitmap;
        this.orientation = orientation;
        (this.painting = painting).setRenderView(this);
        this.setSurfaceTextureListener((TextureView$SurfaceTextureListener)new TextureView$SurfaceTextureListener() {
            public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
                if (surfaceTexture != null) {
                    if (RenderView.this.internal == null) {
                        final RenderView this$0 = RenderView.this;
                        this$0.internal = this$0.new CanvasInternal(surfaceTexture);
                        RenderView.this.internal.setBufferSize(n, n2);
                        RenderView.this.updateTransform();
                        RenderView.this.internal.requestRender();
                        if (RenderView.this.painting.isPaused()) {
                            RenderView.this.painting.onResume();
                        }
                    }
                }
            }
            
            public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
                if (RenderView.this.internal == null) {
                    return true;
                }
                if (!RenderView.this.shuttingDown) {
                    RenderView.this.painting.onPause(new Runnable() {
                        @Override
                        public void run() {
                            RenderView.this.internal.shutdown();
                            RenderView.this.internal = null;
                        }
                    });
                }
                return true;
            }
            
            public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
                if (RenderView.this.internal == null) {
                    return;
                }
                RenderView.this.internal.setBufferSize(n, n2);
                RenderView.this.updateTransform();
                RenderView.this.internal.requestRender();
                RenderView.this.internal.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (RenderView.this.internal != null) {
                            RenderView.this.internal.requestRender();
                        }
                    }
                });
            }
            
            public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
            }
        });
        this.input = new Input(this);
        this.painting.setDelegate((Painting.PaintingDelegate)new Painting.PaintingDelegate() {
            @Override
            public void contentChanged(final RectF rectF) {
                if (RenderView.this.internal != null) {
                    RenderView.this.internal.scheduleRedraw();
                }
            }
            
            @Override
            public DispatchQueue requestDispatchQueue() {
                return RenderView.this.queue;
            }
            
            @Override
            public UndoStore requestUndoStore() {
                return RenderView.this.undoStore;
            }
            
            @Override
            public void strokeCommited() {
            }
        });
    }
    
    private float brushWeightForSize(final float n) {
        final float width = this.painting.getSize().width;
        return 0.00390625f * width + width * 0.043945312f * n;
    }
    
    private void updateTransform() {
        final Matrix matrix = new Matrix();
        float n;
        if (this.painting != null) {
            n = this.getWidth() / this.painting.getSize().width;
        }
        else {
            n = 1.0f;
        }
        float n2 = n;
        if (n <= 0.0f) {
            n2 = 1.0f;
        }
        final Size size = this.getPainting().getSize();
        matrix.preTranslate(this.getWidth() / 2.0f, this.getHeight() / 2.0f);
        matrix.preScale(n2, -n2);
        matrix.preTranslate(-size.width / 2.0f, -size.height / 2.0f);
        this.input.setMatrix(matrix);
        this.painting.setRenderProjection(GLMatrix.MultiplyMat4f(GLMatrix.LoadOrtho(0.0f, (float)this.internal.bufferWidth, 0.0f, (float)this.internal.bufferHeight, -1.0f, 1.0f), GLMatrix.LoadGraphicsMatrix(matrix)));
    }
    
    public Brush getCurrentBrush() {
        return this.brush;
    }
    
    public int getCurrentColor() {
        return this.color;
    }
    
    public float getCurrentWeight() {
        return this.weight;
    }
    
    public Painting getPainting() {
        return this.painting;
    }
    
    public Bitmap getResultBitmap() {
        final CanvasInternal internal = this.internal;
        Bitmap texture;
        if (internal != null) {
            texture = internal.getTexture();
        }
        else {
            texture = null;
        }
        return texture;
    }
    
    public void onBeganDrawing() {
        final RenderViewDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onBeganDrawing();
        }
    }
    
    public void onFinishedDrawing(final boolean b) {
        final RenderViewDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onFinishedDrawing(b);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() > 1) {
            return false;
        }
        final CanvasInternal internal = this.internal;
        if (internal != null && internal.initialized) {
            if (this.internal.ready) {
                this.input.process(motionEvent);
            }
        }
        return true;
    }
    
    public void performInContext(final Runnable runnable) {
        final CanvasInternal internal = this.internal;
        if (internal == null) {
            return;
        }
        internal.postRunnable(new Runnable() {
            @Override
            public void run() {
                if (RenderView.this.internal != null) {
                    if (RenderView.this.internal.initialized) {
                        RenderView.this.internal.setCurrentContext();
                        runnable.run();
                    }
                }
            }
        });
    }
    
    public void setBrush(final Brush brush) {
        this.painting.setBrush(this.brush = brush);
    }
    
    public void setBrushSize(final float n) {
        this.weight = this.brushWeightForSize(n);
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
    
    public void setDelegate(final RenderViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setQueue(final DispatchQueue queue) {
        this.queue = queue;
    }
    
    public void setUndoStore(final UndoStore undoStore) {
        this.undoStore = undoStore;
    }
    
    public boolean shouldDraw() {
        final RenderViewDelegate delegate = this.delegate;
        return delegate == null || delegate.shouldDraw();
    }
    
    public void shutdown() {
        this.shuttingDown = true;
        if (this.internal != null) {
            this.performInContext(new Runnable() {
                @Override
                public void run() {
                    RenderView.this.painting.cleanResources(RenderView.this.transformedBitmap);
                    RenderView.this.internal.shutdown();
                    RenderView.this.internal = null;
                }
            });
        }
        this.setVisibility(8);
    }
    
    private class CanvasInternal extends DispatchQueue
    {
        private final int EGL_CONTEXT_CLIENT_VERSION;
        private final int EGL_OPENGL_ES2_BIT;
        private int bufferHeight;
        private int bufferWidth;
        private Runnable drawRunnable;
        private EGL10 egl10;
        private EGLConfig eglConfig;
        private EGLContext eglContext;
        private EGLDisplay eglDisplay;
        private EGLSurface eglSurface;
        private boolean initialized;
        private long lastRenderCallTime;
        private boolean ready;
        private Runnable scheduledRunnable;
        private SurfaceTexture surfaceTexture;
        
        public CanvasInternal(final SurfaceTexture surfaceTexture) {
            super("CanvasInternal");
            this.EGL_CONTEXT_CLIENT_VERSION = 12440;
            this.EGL_OPENGL_ES2_BIT = 4;
            this.drawRunnable = new Runnable() {
                @Override
                public void run() {
                    if (CanvasInternal.this.initialized) {
                        if (!RenderView.this.shuttingDown) {
                            CanvasInternal.this.setCurrentContext();
                            GLES20.glBindFramebuffer(36160, 0);
                            GLES20.glViewport(0, 0, CanvasInternal.this.bufferWidth, CanvasInternal.this.bufferHeight);
                            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                            GLES20.glClear(16384);
                            RenderView.this.painting.render();
                            GLES20.glBlendFunc(1, 771);
                            CanvasInternal.this.egl10.eglSwapBuffers(CanvasInternal.this.eglDisplay, CanvasInternal.this.eglSurface);
                            if (!CanvasInternal.this.ready) {
                                RenderView.this.queue.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        CanvasInternal.this.ready = true;
                                    }
                                }, 200L);
                            }
                        }
                    }
                }
            };
            this.surfaceTexture = surfaceTexture;
        }
        
        private void checkBitmap() {
            final Size size = RenderView.this.painting.getSize();
            if (RenderView.this.bitmap.getWidth() != size.width || RenderView.this.bitmap.getHeight() != size.height || RenderView.this.orientation != 0) {
                float n = (float)RenderView.this.bitmap.getWidth();
                if (RenderView.this.orientation % 360 == 90 || RenderView.this.orientation % 360 == 270) {
                    n = (float)RenderView.this.bitmap.getHeight();
                }
                final float n2 = size.width / n;
                final RenderView this$0 = RenderView.this;
                this$0.bitmap = this.createBitmap(this$0.bitmap, n2);
                RenderView.this.orientation = 0;
                RenderView.this.transformedBitmap = true;
            }
        }
        
        private Bitmap createBitmap(final Bitmap bitmap, final float n) {
            final Matrix matrix = new Matrix();
            matrix.setScale(n, n);
            matrix.postRotate((float)RenderView.this.orientation);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        
        private boolean initGL() {
            this.egl10 = (EGL10)EGLContext.getEGL();
            this.eglDisplay = this.egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            final EGLDisplay eglDisplay = this.eglDisplay;
            if (eglDisplay == EGL10.EGL_NO_DISPLAY) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("eglGetDisplay failed ");
                    sb.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb.toString());
                }
                this.finish();
                return false;
            }
            if (!this.egl10.eglInitialize(eglDisplay, new int[2])) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("eglInitialize failed ");
                    sb2.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb2.toString());
                }
                this.finish();
                return false;
            }
            final int[] array = { 0 };
            final EGLConfig[] array2 = { null };
            if (!this.egl10.eglChooseConfig(this.eglDisplay, new int[] { 12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 0, 12326, 0, 12344 }, array2, 1, array)) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("eglChooseConfig failed ");
                    sb3.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb3.toString());
                }
                this.finish();
                return false;
            }
            if (array[0] <= 0) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("eglConfig not initialized");
                }
                this.finish();
                return false;
            }
            this.eglConfig = array2[0];
            this.eglContext = this.egl10.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, new int[] { 12440, 2, 12344 });
            if (this.eglContext == null) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("eglCreateContext failed ");
                    sb4.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb4.toString());
                }
                this.finish();
                return false;
            }
            final SurfaceTexture surfaceTexture = this.surfaceTexture;
            if (!(surfaceTexture instanceof SurfaceTexture)) {
                this.finish();
                return false;
            }
            this.eglSurface = this.egl10.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, (Object)surfaceTexture, (int[])null);
            final EGLSurface eglSurface = this.eglSurface;
            if (eglSurface == null || eglSurface == EGL10.EGL_NO_SURFACE) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("createWindowSurface failed ");
                    sb5.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb5.toString());
                }
                this.finish();
                return false;
            }
            if (!this.egl10.eglMakeCurrent(this.eglDisplay, eglSurface, eglSurface, this.eglContext)) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("eglMakeCurrent failed ");
                    sb6.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb6.toString());
                }
                this.finish();
                return false;
            }
            GLES20.glEnable(3042);
            GLES20.glDisable(3024);
            GLES20.glDisable(2960);
            GLES20.glDisable(2929);
            RenderView.this.painting.setupShaders();
            this.checkBitmap();
            RenderView.this.painting.setBitmap(RenderView.this.bitmap);
            Utils.HasGLError();
            return true;
        }
        
        private boolean setCurrentContext() {
            if (!this.initialized) {
                return false;
            }
            if (!this.eglContext.equals(this.egl10.eglGetCurrentContext()) || !this.eglSurface.equals(this.egl10.eglGetCurrentSurface(12377))) {
                final EGL10 egl10 = this.egl10;
                final EGLDisplay eglDisplay = this.eglDisplay;
                final EGLSurface eglSurface = this.eglSurface;
                if (!egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, this.eglContext)) {
                    return false;
                }
            }
            return true;
        }
        
        public void finish() {
            if (this.eglSurface != null) {
                final EGL10 egl10 = this.egl10;
                final EGLDisplay eglDisplay = this.eglDisplay;
                final EGLSurface egl_NO_SURFACE = EGL10.EGL_NO_SURFACE;
                egl10.eglMakeCurrent(eglDisplay, egl_NO_SURFACE, egl_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                this.egl10.eglDestroySurface(this.eglDisplay, this.eglSurface);
                this.eglSurface = null;
            }
            final EGLContext eglContext = this.eglContext;
            if (eglContext != null) {
                this.egl10.eglDestroyContext(this.eglDisplay, eglContext);
                this.eglContext = null;
            }
            final EGLDisplay eglDisplay2 = this.eglDisplay;
            if (eglDisplay2 != null) {
                this.egl10.eglTerminate(eglDisplay2);
                this.eglDisplay = null;
            }
        }
        
        public Bitmap getTexture() {
            if (!this.initialized) {
                return null;
            }
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final Bitmap[] array = { null };
            try {
                this.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        array[0] = RenderView.this.painting.getPaintingData(new RectF(0.0f, 0.0f, RenderView.this.painting.getSize().width, RenderView.this.painting.getSize().height), false).bitmap;
                        countDownLatch.countDown();
                    }
                });
                countDownLatch.await();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            return array[0];
        }
        
        public void requestRender() {
            this.postRunnable(new Runnable() {
                @Override
                public void run() {
                    CanvasInternal.this.drawRunnable.run();
                }
            });
        }
        
        @Override
        public void run() {
            if (RenderView.this.bitmap != null) {
                if (!RenderView.this.bitmap.isRecycled()) {
                    this.initialized = this.initGL();
                    super.run();
                }
            }
        }
        
        public void scheduleRedraw() {
            final Runnable scheduledRunnable = this.scheduledRunnable;
            if (scheduledRunnable != null) {
                this.cancelRunnable(scheduledRunnable);
                this.scheduledRunnable = null;
            }
            this.postRunnable(this.scheduledRunnable = new Runnable() {
                @Override
                public void run() {
                    CanvasInternal.this.scheduledRunnable = null;
                    CanvasInternal.this.drawRunnable.run();
                }
            }, 1L);
        }
        
        public void setBufferSize(final int bufferWidth, final int bufferHeight) {
            this.bufferWidth = bufferWidth;
            this.bufferHeight = bufferHeight;
        }
        
        public void shutdown() {
            this.postRunnable(new Runnable() {
                @Override
                public void run() {
                    CanvasInternal.this.finish();
                    final Looper myLooper = Looper.myLooper();
                    if (myLooper != null) {
                        myLooper.quit();
                    }
                }
            });
        }
    }
    
    public interface RenderViewDelegate
    {
        void onBeganDrawing();
        
        void onFinishedDrawing(final boolean p0);
        
        boolean shouldDraw();
    }
}
