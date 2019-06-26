// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.video;

import java.nio.Buffer;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class TextureRenderer
{
    private static final int FLOAT_SIZE_BYTES = 4;
    private static final String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 20;
    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
    private static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
    private float[] mMVPMatrix;
    private int mProgram;
    private float[] mSTMatrix;
    private int mTextureID;
    private FloatBuffer mTriangleVertices;
    private int maPositionHandle;
    private int maTextureHandle;
    private int muMVPMatrixHandle;
    private int muSTMatrixHandle;
    private int rotationAngle;
    
    public TextureRenderer(final int rotationAngle) {
        this.mMVPMatrix = new float[16];
        this.mSTMatrix = new float[16];
        this.mTextureID = -12345;
        this.rotationAngle = rotationAngle;
        final float[] array;
        final float[] src = array = new float[20];
        array[1] = (array[0] = -1.0f);
        array[2] = 0.0f;
        array[4] = (array[3] = 0.0f);
        array[5] = 1.0f;
        array[6] = -1.0f;
        array[7] = 0.0f;
        array[8] = 1.0f;
        array[9] = 0.0f;
        array[10] = -1.0f;
        array[11] = 1.0f;
        array[13] = (array[12] = 0.0f);
        array[14] = 1.0f;
        array[16] = (array[15] = 1.0f);
        array[17] = 0.0f;
        array[19] = (array[18] = 1.0f);
        this.mTriangleVertices = ByteBuffer.allocateDirect(src.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTriangleVertices.put(src).position(0);
        Matrix.setIdentityM(this.mSTMatrix, 0);
    }
    
    private int createProgram(final String s, final String s2) {
        final int loadShader = this.loadShader(35633, s);
        final int n = 0;
        if (loadShader == 0) {
            return 0;
        }
        final int loadShader2 = this.loadShader(35632, s2);
        if (loadShader2 == 0) {
            return 0;
        }
        int glCreateProgram = GLES20.glCreateProgram();
        this.checkGlError("glCreateProgram");
        if (glCreateProgram == 0) {
            return 0;
        }
        GLES20.glAttachShader(glCreateProgram, loadShader);
        this.checkGlError("glAttachShader");
        GLES20.glAttachShader(glCreateProgram, loadShader2);
        this.checkGlError("glAttachShader");
        GLES20.glLinkProgram(glCreateProgram);
        final int[] array = { 0 };
        GLES20.glGetProgramiv(glCreateProgram, 35714, array, 0);
        if (array[0] != 1) {
            GLES20.glDeleteProgram(glCreateProgram);
            glCreateProgram = n;
        }
        return glCreateProgram;
    }
    
    private int loadShader(int i, final String s) {
        final int glCreateShader = GLES20.glCreateShader(i);
        final StringBuilder sb = new StringBuilder();
        sb.append("glCreateShader type=");
        sb.append(i);
        this.checkGlError(sb.toString());
        GLES20.glShaderSource(glCreateShader, s);
        GLES20.glCompileShader(glCreateShader);
        final int[] array = { 0 };
        i = 0;
        GLES20.glGetShaderiv(glCreateShader, 35713, array, 0);
        if (array[0] == 0) {
            GLES20.glDeleteShader(glCreateShader);
        }
        else {
            i = glCreateShader;
        }
        return i;
    }
    
    public void checkGlError(final String str) {
        final int glGetError = GLES20.glGetError();
        if (glGetError == 0) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(": glError ");
        sb.append(glGetError);
        throw new RuntimeException(sb.toString());
    }
    
    public void drawFrame(final SurfaceTexture surfaceTexture, final boolean b) {
        this.checkGlError("onDrawFrame start");
        surfaceTexture.getTransformMatrix(this.mSTMatrix);
        if (b) {
            final float[] mstMatrix = this.mSTMatrix;
            mstMatrix[5] = -mstMatrix[5];
            mstMatrix[13] = 1.0f - mstMatrix[13];
        }
        GLES20.glUseProgram(this.mProgram);
        this.checkGlError("glUseProgram");
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, this.mTextureID);
        this.mTriangleVertices.position(0);
        GLES20.glVertexAttribPointer(this.maPositionHandle, 3, 5126, false, 20, (Buffer)this.mTriangleVertices);
        this.checkGlError("glVertexAttribPointer maPosition");
        GLES20.glEnableVertexAttribArray(this.maPositionHandle);
        this.checkGlError("glEnableVertexAttribArray maPositionHandle");
        this.mTriangleVertices.position(3);
        GLES20.glVertexAttribPointer(this.maTextureHandle, 2, 5126, false, 20, (Buffer)this.mTriangleVertices);
        this.checkGlError("glVertexAttribPointer maTextureHandle");
        GLES20.glEnableVertexAttribArray(this.maTextureHandle);
        this.checkGlError("glEnableVertexAttribArray maTextureHandle");
        GLES20.glUniformMatrix4fv(this.muSTMatrixHandle, 1, false, this.mSTMatrix, 0);
        GLES20.glUniformMatrix4fv(this.muMVPMatrixHandle, 1, false, this.mMVPMatrix, 0);
        GLES20.glDrawArrays(5, 0, 4);
        this.checkGlError("glDrawArrays");
        GLES20.glFinish();
    }
    
    public int getTextureId() {
        return this.mTextureID;
    }
    
    public void surfaceCreated() {
        this.mProgram = this.createProgram("uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n", "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n");
        final int mProgram = this.mProgram;
        if (mProgram == 0) {
            throw new RuntimeException("failed creating program");
        }
        this.maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        this.checkGlError("glGetAttribLocation aPosition");
        if (this.maPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
        this.maTextureHandle = GLES20.glGetAttribLocation(this.mProgram, "aTextureCoord");
        this.checkGlError("glGetAttribLocation aTextureCoord");
        if (this.maTextureHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }
        this.muMVPMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uMVPMatrix");
        this.checkGlError("glGetUniformLocation uMVPMatrix");
        if (this.muMVPMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uMVPMatrix");
        }
        this.muSTMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uSTMatrix");
        this.checkGlError("glGetUniformLocation uSTMatrix");
        if (this.muSTMatrixHandle != -1) {
            final int[] array = { 0 };
            GLES20.glGenTextures(1, array, 0);
            GLES20.glBindTexture(36197, this.mTextureID = array[0]);
            this.checkGlError("glBindTexture mTextureID");
            GLES20.glTexParameteri(36197, 10241, 9729);
            GLES20.glTexParameteri(36197, 10240, 9729);
            GLES20.glTexParameteri(36197, 10242, 33071);
            GLES20.glTexParameteri(36197, 10243, 33071);
            this.checkGlError("glTexParameter");
            Matrix.setIdentityM(this.mMVPMatrix, 0);
            final int rotationAngle = this.rotationAngle;
            if (rotationAngle != 0) {
                Matrix.rotateM(this.mMVPMatrix, 0, (float)rotationAngle, 0.0f, 0.0f, 1.0f);
            }
            return;
        }
        throw new RuntimeException("Could not get attrib location for uSTMatrix");
    }
}
