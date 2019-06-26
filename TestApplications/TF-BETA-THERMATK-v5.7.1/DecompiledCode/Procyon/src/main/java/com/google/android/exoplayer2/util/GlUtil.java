// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import android.annotation.TargetApi;
import java.nio.IntBuffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import android.text.TextUtils;
import android.opengl.GLU;
import android.opengl.GLES20;

public final class GlUtil
{
    private static final String TAG = "GlUtil";
    
    private GlUtil() {
    }
    
    private static void addShader(int glCreateShader, final String str, final int n) {
        glCreateShader = GLES20.glCreateShader(glCreateShader);
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        final int[] array = { 0 };
        GLES20.glGetShaderiv(glCreateShader, 35713, array, 0);
        if (array[0] != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append(GLES20.glGetShaderInfoLog(glCreateShader));
            sb.append(", source: ");
            sb.append(str);
            throwGlError(sb.toString());
        }
        GLES20.glAttachShader(n, glCreateShader);
        GLES20.glDeleteShader(glCreateShader);
        checkGlError();
    }
    
    public static void checkGlError() {
        int n = 0;
        while (true) {
            final int glGetError = GLES20.glGetError();
            if (glGetError == 0) {
                break;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("glError ");
            sb.append(GLU.gluErrorString(n));
            Log.e("GlUtil", sb.toString());
            n = glGetError;
        }
    }
    
    public static int compileProgram(final String s, final String s2) {
        final int glCreateProgram = GLES20.glCreateProgram();
        checkGlError();
        addShader(35633, s, glCreateProgram);
        addShader(35632, s2, glCreateProgram);
        GLES20.glLinkProgram(glCreateProgram);
        final int[] array = { 0 };
        GLES20.glGetProgramiv(glCreateProgram, 35714, array, 0);
        if (array[0] != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to link shader program: \n");
            sb.append(GLES20.glGetProgramInfoLog(glCreateProgram));
            throwGlError(sb.toString());
        }
        checkGlError();
        return glCreateProgram;
    }
    
    public static int compileProgram(final String[] array, final String[] array2) {
        return compileProgram(TextUtils.join((CharSequence)"\n", (Object[])array), TextUtils.join((CharSequence)"\n", (Object[])array2));
    }
    
    public static FloatBuffer createBuffer(final int n) {
        return ByteBuffer.allocateDirect(n * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }
    
    public static FloatBuffer createBuffer(final float[] src) {
        return (FloatBuffer)createBuffer(src.length).put(src).flip();
    }
    
    @TargetApi(15)
    public static int createExternalTexture() {
        final int[] array = { 0 };
        GLES20.glGenTextures(1, IntBuffer.wrap(array));
        GLES20.glBindTexture(36197, array[0]);
        GLES20.glTexParameteri(36197, 10241, 9729);
        GLES20.glTexParameteri(36197, 10240, 9729);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        checkGlError();
        return array[0];
    }
    
    private static void throwGlError(final String s) {
        Log.e("GlUtil", s);
    }
}
