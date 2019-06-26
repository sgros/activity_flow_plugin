// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;
import android.opengl.GLES20;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.graphics.SurfaceTexture;
import android.opengl.EGLSurface;
import android.os.Handler;
import android.opengl.EGLDisplay;
import android.opengl.EGLContext;
import android.annotation.TargetApi;
import android.graphics.SurfaceTexture$OnFrameAvailableListener;

@TargetApi(17)
public final class EGLSurfaceTexture implements SurfaceTexture$OnFrameAvailableListener, Runnable
{
    private static final int[] EGL_CONFIG_ATTRIBUTES;
    private static final int EGL_PROTECTED_CONTENT_EXT = 12992;
    private static final int EGL_SURFACE_HEIGHT = 1;
    private static final int EGL_SURFACE_WIDTH = 1;
    public static final int SECURE_MODE_NONE = 0;
    public static final int SECURE_MODE_PROTECTED_PBUFFER = 2;
    public static final int SECURE_MODE_SURFACELESS_CONTEXT = 1;
    private final TextureImageListener callback;
    private EGLContext context;
    private EGLDisplay display;
    private final Handler handler;
    private EGLSurface surface;
    private SurfaceTexture texture;
    private final int[] textureIdHolder;
    
    static {
        EGL_CONFIG_ATTRIBUTES = new int[] { 12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 0, 12327, 12344, 12339, 4, 12344 };
    }
    
    public EGLSurfaceTexture(final Handler handler) {
        this(handler, null);
    }
    
    public EGLSurfaceTexture(final Handler handler, final TextureImageListener callback) {
        this.handler = handler;
        this.callback = callback;
        this.textureIdHolder = new int[1];
    }
    
    private static EGLConfig chooseEGLConfig(final EGLDisplay eglDisplay) {
        final EGLConfig[] array = { null };
        final int[] array2 = { 0 };
        final boolean eglChooseConfig = EGL14.eglChooseConfig(eglDisplay, EGLSurfaceTexture.EGL_CONFIG_ATTRIBUTES, 0, array, 0, 1, array2, 0);
        if (eglChooseConfig && array2[0] > 0 && array[0] != null) {
            return array[0];
        }
        throw new GlException(Util.formatInvariant("eglChooseConfig failed: success=%b, numConfigs[0]=%d, configs[0]=%s", eglChooseConfig, array2[0], array[0]));
    }
    
    private static EGLContext createEGLContext(final EGLDisplay eglDisplay, final EGLConfig eglConfig, final int n) {
        int[] array;
        if (n == 0) {
            final int[] array2;
            array = (array2 = new int[3]);
            array2[0] = 12440;
            array2[array2[1] = 2] = 12344;
        }
        else {
            final int[] array3;
            array = (array3 = new int[5]);
            array3[0] = 12440;
            array3[array3[1] = 2] = 12992;
            array3[3] = 1;
            array3[4] = 12344;
        }
        final EGLContext eglCreateContext = EGL14.eglCreateContext(eglDisplay, eglConfig, EGL14.EGL_NO_CONTEXT, array, 0);
        if (eglCreateContext != null) {
            return eglCreateContext;
        }
        throw new GlException("eglCreateContext failed");
    }
    
    private static EGLSurface createEGLSurface(final EGLDisplay eglDisplay, final EGLConfig eglConfig, final EGLContext eglContext, final int n) {
        EGLSurface eglSurface;
        if (n == 1) {
            eglSurface = EGL14.EGL_NO_SURFACE;
        }
        else {
            int[] array;
            if (n == 2) {
                final int[] array2;
                array = (array2 = new int[7]);
                array2[0] = 12375;
                array2[1] = 1;
                array2[2] = 12374;
                array2[3] = 1;
                array2[4] = 12992;
                array2[5] = 1;
                array2[6] = 12344;
            }
            else {
                final int[] array3;
                array = (array3 = new int[5]);
                array3[0] = 12375;
                array3[1] = 1;
                array3[2] = 12374;
                array3[3] = 1;
                array3[4] = 12344;
            }
            eglSurface = EGL14.eglCreatePbufferSurface(eglDisplay, eglConfig, array, 0);
            if (eglSurface == null) {
                throw new GlException("eglCreatePbufferSurface failed");
            }
        }
        if (EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
            return eglSurface;
        }
        throw new GlException("eglMakeCurrent failed");
    }
    
    private void dispatchOnFrameAvailable() {
        final TextureImageListener callback = this.callback;
        if (callback != null) {
            callback.onFrameAvailable();
        }
    }
    
    private static void generateTextureIds(final int[] array) {
        GLES20.glGenTextures(1, array, 0);
        GlUtil.checkGlError();
    }
    
    private static EGLDisplay getDefaultDisplay() {
        final EGLDisplay eglGetDisplay = EGL14.eglGetDisplay(0);
        if (eglGetDisplay == null) {
            throw new GlException("eglGetDisplay failed");
        }
        final int[] array = new int[2];
        if (EGL14.eglInitialize(eglGetDisplay, array, 0, array, 1)) {
            return eglGetDisplay;
        }
        throw new GlException("eglInitialize failed");
    }
    
    public SurfaceTexture getSurfaceTexture() {
        final SurfaceTexture texture = this.texture;
        Assertions.checkNotNull(texture);
        return texture;
    }
    
    public void init(final int n) {
        this.display = getDefaultDisplay();
        final EGLConfig chooseEGLConfig = chooseEGLConfig(this.display);
        this.context = createEGLContext(this.display, chooseEGLConfig, n);
        this.surface = createEGLSurface(this.display, chooseEGLConfig, this.context, n);
        generateTextureIds(this.textureIdHolder);
        (this.texture = new SurfaceTexture(this.textureIdHolder[0])).setOnFrameAvailableListener((SurfaceTexture$OnFrameAvailableListener)this);
    }
    
    public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
        this.handler.post((Runnable)this);
    }
    
    public void release() {
        this.handler.removeCallbacks((Runnable)this);
        try {
            if (this.texture != null) {
                this.texture.release();
                GLES20.glDeleteTextures(1, this.textureIdHolder, 0);
            }
        }
        finally {
            final EGLDisplay display = this.display;
            if (display != null && !display.equals((Object)EGL14.EGL_NO_DISPLAY)) {
                final EGLDisplay display2 = this.display;
                final EGLSurface egl_NO_SURFACE = EGL14.EGL_NO_SURFACE;
                EGL14.eglMakeCurrent(display2, egl_NO_SURFACE, egl_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
            }
            final EGLSurface surface = this.surface;
            if (surface != null && !surface.equals((Object)EGL14.EGL_NO_SURFACE)) {
                EGL14.eglDestroySurface(this.display, this.surface);
            }
            final EGLContext context = this.context;
            if (context != null) {
                EGL14.eglDestroyContext(this.display, context);
            }
            if (Util.SDK_INT >= 19) {
                EGL14.eglReleaseThread();
            }
            final EGLDisplay display3 = this.display;
            if (display3 != null && !display3.equals((Object)EGL14.EGL_NO_DISPLAY)) {
                EGL14.eglTerminate(this.display);
            }
            this.display = null;
            this.context = null;
            this.surface = null;
            this.texture = null;
        }
    }
    
    public void run() {
        this.dispatchOnFrameAvailable();
        final SurfaceTexture texture = this.texture;
        if (texture == null) {
            return;
        }
        try {
            texture.updateTexImage();
        }
        catch (RuntimeException ex) {}
    }
    
    public static final class GlException extends RuntimeException
    {
        private GlException(final String message) {
            super(message);
        }
    }
    
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface SecureMode {
    }
    
    public interface TextureImageListener
    {
        void onFrameAvailable();
    }
}
