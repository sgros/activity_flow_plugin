// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.video;

import android.opengl.EGLExt;
import android.opengl.EGLConfig;
import android.opengl.EGL14;
import android.view.Surface;
import android.opengl.EGLSurface;
import android.opengl.EGLDisplay;
import android.opengl.EGLContext;
import android.annotation.TargetApi;

@TargetApi(17)
public class InputSurface
{
    private static final int EGL_OPENGL_ES2_BIT = 4;
    private static final int EGL_RECORDABLE_ANDROID = 12610;
    private EGLContext mEGLContext;
    private EGLDisplay mEGLDisplay;
    private EGLSurface mEGLSurface;
    private Surface mSurface;
    
    public InputSurface(final Surface mSurface) {
        if (mSurface != null) {
            this.mSurface = mSurface;
            this.eglSetup();
            return;
        }
        throw new NullPointerException();
    }
    
    private void checkEglError(final String s) {
        boolean b = false;
        while (EGL14.eglGetError() != 12288) {
            b = true;
        }
        if (!b) {
            return;
        }
        throw new RuntimeException("EGL error encountered (see log)");
    }
    
    private void eglSetup() {
        this.mEGLDisplay = EGL14.eglGetDisplay(0);
        final EGLDisplay meglDisplay = this.mEGLDisplay;
        if (meglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("unable to get EGL14 display");
        }
        final int[] array = new int[2];
        if (!EGL14.eglInitialize(meglDisplay, array, 0, array, 1)) {
            this.mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL14");
        }
        final EGLConfig[] array2 = { null };
        if (!EGL14.eglChooseConfig(this.mEGLDisplay, new int[] { 12324, 8, 12323, 8, 12322, 8, 12352, 4, 12610, 1, 12344 }, 0, array2, 0, array2.length, new int[] { 0 }, 0)) {
            throw new RuntimeException("unable to find RGB888+recordable ES2 EGL config");
        }
        this.mEGLContext = EGL14.eglCreateContext(this.mEGLDisplay, array2[0], EGL14.EGL_NO_CONTEXT, new int[] { 12440, 2, 12344 }, 0);
        this.checkEglError("eglCreateContext");
        if (this.mEGLContext == null) {
            throw new RuntimeException("null context");
        }
        this.mEGLSurface = EGL14.eglCreateWindowSurface(this.mEGLDisplay, array2[0], (Object)this.mSurface, new int[] { 12344 }, 0);
        this.checkEglError("eglCreateWindowSurface");
        if (this.mEGLSurface != null) {
            return;
        }
        throw new RuntimeException("surface was null");
    }
    
    public Surface getSurface() {
        return this.mSurface;
    }
    
    public void makeCurrent() {
        final EGLDisplay meglDisplay = this.mEGLDisplay;
        final EGLSurface meglSurface = this.mEGLSurface;
        if (EGL14.eglMakeCurrent(meglDisplay, meglSurface, meglSurface, this.mEGLContext)) {
            return;
        }
        throw new RuntimeException("eglMakeCurrent failed");
    }
    
    public void release() {
        if (EGL14.eglGetCurrentContext().equals((Object)this.mEGLContext)) {
            final EGLDisplay meglDisplay = this.mEGLDisplay;
            final EGLSurface egl_NO_SURFACE = EGL14.EGL_NO_SURFACE;
            EGL14.eglMakeCurrent(meglDisplay, egl_NO_SURFACE, egl_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        }
        EGL14.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
        EGL14.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
        this.mSurface.release();
        this.mEGLDisplay = null;
        this.mEGLContext = null;
        this.mEGLSurface = null;
        this.mSurface = null;
    }
    
    @TargetApi(18)
    public void setPresentationTime(final long n) {
        EGLExt.eglPresentationTimeANDROID(this.mEGLDisplay, this.mEGLSurface, n);
    }
    
    public boolean swapBuffers() {
        return EGL14.eglSwapBuffers(this.mEGLDisplay, this.mEGLSurface);
    }
}
