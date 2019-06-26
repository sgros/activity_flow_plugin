package org.telegram.messenger.video;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TextureRenderer {
   private static final int FLOAT_SIZE_BYTES = 4;
   private static final String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
   private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
   private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 20;
   private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
   private static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
   private float[] mMVPMatrix = new float[16];
   private int mProgram;
   private float[] mSTMatrix = new float[16];
   private int mTextureID = -12345;
   private FloatBuffer mTriangleVertices;
   private int maPositionHandle;
   private int maTextureHandle;
   private int muMVPMatrixHandle;
   private int muSTMatrixHandle;
   private int rotationAngle;

   public TextureRenderer(int var1) {
      this.rotationAngle = var1;
      float[] var2 = new float[]{-1.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0F, -1.0F, 0.0F, 1.0F, 0.0F, -1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F};
      this.mTriangleVertices = ByteBuffer.allocateDirect(var2.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
      this.mTriangleVertices.put(var2).position(0);
      Matrix.setIdentityM(this.mSTMatrix, 0);
   }

   private int createProgram(String var1, String var2) {
      int var3 = this.loadShader(35633, var1);
      byte var4 = 0;
      if (var3 == 0) {
         return 0;
      } else {
         int var5 = this.loadShader(35632, var2);
         if (var5 == 0) {
            return 0;
         } else {
            int var6 = GLES20.glCreateProgram();
            this.checkGlError("glCreateProgram");
            if (var6 == 0) {
               return 0;
            } else {
               GLES20.glAttachShader(var6, var3);
               this.checkGlError("glAttachShader");
               GLES20.glAttachShader(var6, var5);
               this.checkGlError("glAttachShader");
               GLES20.glLinkProgram(var6);
               int[] var7 = new int[1];
               GLES20.glGetProgramiv(var6, 35714, var7, 0);
               if (var7[0] != 1) {
                  GLES20.glDeleteProgram(var6);
                  var6 = var4;
               }

               return var6;
            }
         }
      }
   }

   private int loadShader(int var1, String var2) {
      int var3 = GLES20.glCreateShader(var1);
      StringBuilder var4 = new StringBuilder();
      var4.append("glCreateShader type=");
      var4.append(var1);
      this.checkGlError(var4.toString());
      GLES20.glShaderSource(var3, var2);
      GLES20.glCompileShader(var3);
      int[] var5 = new int[1];
      var1 = 0;
      GLES20.glGetShaderiv(var3, 35713, var5, 0);
      if (var5[0] == 0) {
         GLES20.glDeleteShader(var3);
      } else {
         var1 = var3;
      }

      return var1;
   }

   public void checkGlError(String var1) {
      int var2 = GLES20.glGetError();
      if (var2 != 0) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append(": glError ");
         var3.append(var2);
         throw new RuntimeException(var3.toString());
      }
   }

   public void drawFrame(SurfaceTexture var1, boolean var2) {
      this.checkGlError("onDrawFrame start");
      var1.getTransformMatrix(this.mSTMatrix);
      if (var2) {
         float[] var3 = this.mSTMatrix;
         var3[5] = -var3[5];
         var3[13] = 1.0F - var3[13];
      }

      GLES20.glUseProgram(this.mProgram);
      this.checkGlError("glUseProgram");
      GLES20.glActiveTexture(33984);
      GLES20.glBindTexture(36197, this.mTextureID);
      this.mTriangleVertices.position(0);
      GLES20.glVertexAttribPointer(this.maPositionHandle, 3, 5126, false, 20, this.mTriangleVertices);
      this.checkGlError("glVertexAttribPointer maPosition");
      GLES20.glEnableVertexAttribArray(this.maPositionHandle);
      this.checkGlError("glEnableVertexAttribArray maPositionHandle");
      this.mTriangleVertices.position(3);
      GLES20.glVertexAttribPointer(this.maTextureHandle, 2, 5126, false, 20, this.mTriangleVertices);
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
      int var1 = this.mProgram;
      if (var1 != 0) {
         this.maPositionHandle = GLES20.glGetAttribLocation(var1, "aPosition");
         this.checkGlError("glGetAttribLocation aPosition");
         if (this.maPositionHandle != -1) {
            this.maTextureHandle = GLES20.glGetAttribLocation(this.mProgram, "aTextureCoord");
            this.checkGlError("glGetAttribLocation aTextureCoord");
            if (this.maTextureHandle != -1) {
               this.muMVPMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uMVPMatrix");
               this.checkGlError("glGetUniformLocation uMVPMatrix");
               if (this.muMVPMatrixHandle != -1) {
                  this.muSTMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uSTMatrix");
                  this.checkGlError("glGetUniformLocation uSTMatrix");
                  if (this.muSTMatrixHandle != -1) {
                     int[] var2 = new int[1];
                     GLES20.glGenTextures(1, var2, 0);
                     this.mTextureID = var2[0];
                     GLES20.glBindTexture(36197, this.mTextureID);
                     this.checkGlError("glBindTexture mTextureID");
                     GLES20.glTexParameteri(36197, 10241, 9729);
                     GLES20.glTexParameteri(36197, 10240, 9729);
                     GLES20.glTexParameteri(36197, 10242, 33071);
                     GLES20.glTexParameteri(36197, 10243, 33071);
                     this.checkGlError("glTexParameter");
                     Matrix.setIdentityM(this.mMVPMatrix, 0);
                     var1 = this.rotationAngle;
                     if (var1 != 0) {
                        Matrix.rotateM(this.mMVPMatrix, 0, (float)var1, 0.0F, 0.0F, 1.0F);
                     }

                  } else {
                     throw new RuntimeException("Could not get attrib location for uSTMatrix");
                  }
               } else {
                  throw new RuntimeException("Could not get attrib location for uMVPMatrix");
               }
            } else {
               throw new RuntimeException("Could not get attrib location for aTextureCoord");
            }
         } else {
            throw new RuntimeException("Could not get attrib location for aPosition");
         }
      } else {
         throw new RuntimeException("failed creating program");
      }
   }
}
