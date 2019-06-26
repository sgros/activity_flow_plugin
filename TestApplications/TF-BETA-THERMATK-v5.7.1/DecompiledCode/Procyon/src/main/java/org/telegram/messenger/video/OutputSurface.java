// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.video;

import java.nio.Buffer;
import android.opengl.GLES20;
import javax.microedition.khronos.egl.EGLConfig;
import java.nio.ByteOrder;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import java.nio.ByteBuffer;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGL10;
import android.graphics.SurfaceTexture$OnFrameAvailableListener;

public class OutputSurface implements SurfaceTexture$OnFrameAvailableListener
{
    private static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    private static final int EGL_OPENGL_ES2_BIT = 4;
    private EGL10 mEGL;
    private EGLContext mEGLContext;
    private EGLDisplay mEGLDisplay;
    private EGLSurface mEGLSurface;
    private boolean mFrameAvailable;
    private final Object mFrameSyncObject;
    private int mHeight;
    private ByteBuffer mPixelBuf;
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    private TextureRenderer mTextureRender;
    private int mWidth;
    private int rotateRender;
    
    public OutputSurface() {
        this.mEGLDisplay = null;
        this.mEGLContext = null;
        this.mEGLSurface = null;
        this.mFrameSyncObject = new Object();
        this.rotateRender = 0;
        this.setup();
    }
    
    public OutputSurface(final int mWidth, final int mHeight, final int rotateRender) {
        this.mEGLDisplay = null;
        this.mEGLContext = null;
        this.mEGLSurface = null;
        this.mFrameSyncObject = new Object();
        this.rotateRender = 0;
        if (mWidth > 0 && mHeight > 0) {
            this.mWidth = mWidth;
            this.mHeight = mHeight;
            this.rotateRender = rotateRender;
            (this.mPixelBuf = ByteBuffer.allocateDirect(this.mWidth * this.mHeight * 4)).order(ByteOrder.LITTLE_ENDIAN);
            this.eglSetup(mWidth, mHeight);
            this.makeCurrent();
            this.setup();
            return;
        }
        throw new IllegalArgumentException();
    }
    
    private void checkEglError(final String s) {
        if (this.mEGL.eglGetError() == 12288) {
            return;
        }
        throw new RuntimeException("EGL error encountered (see log)");
    }
    
    private void eglSetup(final int n, final int n2) {
        this.mEGL = (EGL10)EGLContext.getEGL();
        this.mEGLDisplay = this.mEGL.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        final EGLDisplay meglDisplay = this.mEGLDisplay;
        if (meglDisplay == EGL10.EGL_NO_DISPLAY) {
            throw new RuntimeException("unable to get EGL10 display");
        }
        if (!this.mEGL.eglInitialize(meglDisplay, (int[])null)) {
            this.mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL10");
        }
        final EGLConfig[] array = { null };
        if (!this.mEGL.eglChooseConfig(this.mEGLDisplay, new int[] { 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12339, 1, 12352, 4, 12344 }, array, array.length, new int[] { 0 })) {
            throw new RuntimeException("unable to find RGB888+pbuffer EGL config");
        }
        this.mEGLContext = this.mEGL.eglCreateContext(this.mEGLDisplay, array[0], EGL10.EGL_NO_CONTEXT, new int[] { 12440, 2, 12344 });
        this.checkEglError("eglCreateContext");
        if (this.mEGLContext == null) {
            throw new RuntimeException("null context");
        }
        this.mEGLSurface = this.mEGL.eglCreatePbufferSurface(this.mEGLDisplay, array[0], new int[] { 12375, n, 12374, n2, 12344 });
        this.checkEglError("eglCreatePbufferSurface");
        if (this.mEGLSurface != null) {
            return;
        }
        throw new RuntimeException("surface was null");
    }
    
    private void setup() {
        (this.mTextureRender = new TextureRenderer(this.rotateRender)).surfaceCreated();
        (this.mSurfaceTexture = new SurfaceTexture(this.mTextureRender.getTextureId())).setOnFrameAvailableListener((SurfaceTexture$OnFrameAvailableListener)this);
        this.mSurface = new Surface(this.mSurfaceTexture);
    }
    
    public void awaitNewImage() {
        synchronized (this.mFrameSyncObject) {
            while (!this.mFrameAvailable) {
                try {
                    this.mFrameSyncObject.wait(2500L);
                    if (this.mFrameAvailable) {
                        continue;
                    }
                    throw new RuntimeException("Surface frame wait timed out");
                }
                catch (InterruptedException cause) {
                    throw new RuntimeException(cause);
                }
                break;
            }
            this.mFrameAvailable = false;
            // monitorexit(this.mFrameSyncObject)
            this.mTextureRender.checkGlError("before updateTexImage");
            this.mSurfaceTexture.updateTexImage();
        }
    }
    
    public void drawImage(final boolean b) {
        this.mTextureRender.drawFrame(this.mSurfaceTexture, b);
    }
    
    public ByteBuffer getFrame() {
        this.mPixelBuf.rewind();
        GLES20.glReadPixels(0, 0, this.mWidth, this.mHeight, 6408, 5121, (Buffer)this.mPixelBuf);
        return this.mPixelBuf;
    }
    
    public Surface getSurface() {
        return this.mSurface;
    }
    
    public void makeCurrent() {
        if (this.mEGL == null) {
            throw new RuntimeException("not configured for makeCurrent");
        }
        this.checkEglError("before makeCurrent");
        final EGL10 megl = this.mEGL;
        final EGLDisplay meglDisplay = this.mEGLDisplay;
        final EGLSurface meglSurface = this.mEGLSurface;
        if (megl.eglMakeCurrent(meglDisplay, meglSurface, meglSurface, this.mEGLContext)) {
            return;
        }
        throw new RuntimeException("eglMakeCurrent failed");
    }
    
    public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
        synchronized (this.mFrameSyncObject) {
            if (!this.mFrameAvailable) {
                this.mFrameAvailable = true;
                this.mFrameSyncObject.notifyAll();
                return;
            }
            throw new RuntimeException("mFrameAvailable already set, frame could be dropped");
        }
    }
    
    public void release() {
        final EGL10 megl = this.mEGL;
        if (megl != null) {
            if (megl.eglGetCurrentContext().equals(this.mEGLContext)) {
                final EGL10 megl2 = this.mEGL;
                final EGLDisplay meglDisplay = this.mEGLDisplay;
                final EGLSurface egl_NO_SURFACE = EGL10.EGL_NO_SURFACE;
                megl2.eglMakeCurrent(meglDisplay, egl_NO_SURFACE, egl_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            }
            this.mEGL.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
            this.mEGL.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
        }
        this.mSurface.release();
        this.mEGLDisplay = null;
        this.mEGLContext = null;
        this.mEGLSurface = null;
        this.mEGL = null;
        this.mTextureRender = null;
        this.mSurface = null;
        this.mSurfaceTexture = null;
    }
}
