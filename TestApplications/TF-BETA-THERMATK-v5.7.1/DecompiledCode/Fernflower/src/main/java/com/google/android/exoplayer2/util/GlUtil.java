package com.google.android.exoplayer2.util;

import android.annotation.TargetApi;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.text.TextUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class GlUtil {
   private static final String TAG = "GlUtil";

   private GlUtil() {
   }

   private static void addShader(int var0, String var1, int var2) {
      var0 = GLES20.glCreateShader(var0);
      GLES20.glShaderSource(var0, var1);
      GLES20.glCompileShader(var0);
      int[] var3 = new int[]{0};
      GLES20.glGetShaderiv(var0, 35713, var3, 0);
      if (var3[0] != 1) {
         StringBuilder var4 = new StringBuilder();
         var4.append(GLES20.glGetShaderInfoLog(var0));
         var4.append(", source: ");
         var4.append(var1);
         throwGlError(var4.toString());
      }

      GLES20.glAttachShader(var2, var0);
      GLES20.glDeleteShader(var0);
      checkGlError();
   }

   public static void checkGlError() {
      int var0 = 0;

      while(true) {
         int var1 = GLES20.glGetError();
         if (var1 == 0) {
            return;
         }

         StringBuilder var2 = new StringBuilder();
         var2.append("glError ");
         var2.append(GLU.gluErrorString(var0));
         Log.e("GlUtil", var2.toString());
         var0 = var1;
      }
   }

   public static int compileProgram(String var0, String var1) {
      int var2 = GLES20.glCreateProgram();
      checkGlError();
      addShader(35633, var0, var2);
      addShader(35632, var1, var2);
      GLES20.glLinkProgram(var2);
      int[] var3 = new int[]{0};
      GLES20.glGetProgramiv(var2, 35714, var3, 0);
      if (var3[0] != 1) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Unable to link shader program: \n");
         var4.append(GLES20.glGetProgramInfoLog(var2));
         throwGlError(var4.toString());
      }

      checkGlError();
      return var2;
   }

   public static int compileProgram(String[] var0, String[] var1) {
      return compileProgram(TextUtils.join("\n", var0), TextUtils.join("\n", var1));
   }

   public static FloatBuffer createBuffer(int var0) {
      return ByteBuffer.allocateDirect(var0 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
   }

   public static FloatBuffer createBuffer(float[] var0) {
      return (FloatBuffer)createBuffer(var0.length).put(var0).flip();
   }

   @TargetApi(15)
   public static int createExternalTexture() {
      int[] var0 = new int[1];
      GLES20.glGenTextures(1, IntBuffer.wrap(var0));
      GLES20.glBindTexture(36197, var0[0]);
      GLES20.glTexParameteri(36197, 10241, 9729);
      GLES20.glTexParameteri(36197, 10240, 9729);
      GLES20.glTexParameteri(36197, 10242, 33071);
      GLES20.glTexParameteri(36197, 10243, 33071);
      checkGlError();
      return var0[0];
   }

   private static void throwGlError(String var0) {
      Log.e("GlUtil", var0);
   }
}
