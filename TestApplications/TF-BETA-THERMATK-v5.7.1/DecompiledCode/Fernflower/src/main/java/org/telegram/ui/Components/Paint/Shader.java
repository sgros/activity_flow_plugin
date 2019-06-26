package org.telegram.ui.Components.Paint;

import android.graphics.Color;
import android.opengl.GLES20;
import java.util.HashMap;
import java.util.Map;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

public class Shader {
   private int fragmentShader;
   protected int program = GLES20.glCreateProgram();
   protected Map uniformsMap = new HashMap();
   private int vertexShader;

   public Shader(String var1, String var2, String[] var3, String[] var4) {
      Shader.CompilationResult var8 = this.compileShader(35633, var1);
      int var5 = var8.status;
      byte var6 = 0;
      if (var5 == 0) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("Vertex shader compilation failed");
         }

         this.destroyShader(var8.shader, 0, this.program);
      } else {
         Shader.CompilationResult var9 = this.compileShader(35632, var2);
         if (var9.status == 0) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.e("Fragment shader compilation failed");
            }

            this.destroyShader(var8.shader, var9.shader, this.program);
         } else {
            GLES20.glAttachShader(this.program, var8.shader);
            GLES20.glAttachShader(this.program, var9.shader);

            for(var5 = 0; var5 < var3.length; ++var5) {
               GLES20.glBindAttribLocation(this.program, var5, var3[var5]);
            }

            if (this.linkProgram(this.program) == 0) {
               this.destroyShader(var8.shader, var9.shader, this.program);
            } else {
               int var7 = var4.length;

               for(var5 = var6; var5 < var7; ++var5) {
                  String var10 = var4[var5];
                  this.uniformsMap.put(var10, GLES20.glGetUniformLocation(this.program, var10));
               }

               var5 = var8.shader;
               if (var5 != 0) {
                  GLES20.glDeleteShader(var5);
               }

               var5 = var9.shader;
               if (var5 != 0) {
                  GLES20.glDeleteShader(var5);
               }

            }
         }
      }
   }

   public static void SetColorUniform(int var0, int var1) {
      GLES20.glUniform4f(var0, (float)Color.red(var1) / 255.0F, (float)Color.green(var1) / 255.0F, (float)Color.blue(var1) / 255.0F, (float)Color.alpha(var1) / 255.0F);
   }

   private Shader.CompilationResult compileShader(int var1, String var2) {
      var1 = GLES20.glCreateShader(var1);
      GLES20.glShaderSource(var1, var2);
      GLES20.glCompileShader(var1);
      int[] var3 = new int[1];
      GLES20.glGetShaderiv(var1, 35713, var3, 0);
      if (var3[0] == 0 && BuildVars.LOGS_ENABLED) {
         FileLog.e(GLES20.glGetShaderInfoLog(var1));
      }

      return new Shader.CompilationResult(var1, var3[0]);
   }

   private void destroyShader(int var1, int var2, int var3) {
      if (var1 != 0) {
         GLES20.glDeleteShader(var1);
      }

      if (var2 != 0) {
         GLES20.glDeleteShader(var2);
      }

      if (var3 != 0) {
         GLES20.glDeleteProgram(var1);
      }

   }

   private int linkProgram(int var1) {
      GLES20.glLinkProgram(var1);
      int[] var2 = new int[1];
      GLES20.glGetProgramiv(var1, 35714, var2, 0);
      if (var2[0] == 0 && BuildVars.LOGS_ENABLED) {
         FileLog.e(GLES20.glGetProgramInfoLog(var1));
      }

      return var2[0];
   }

   public void cleanResources() {
      if (this.program != 0) {
         GLES20.glDeleteProgram(this.vertexShader);
         this.program = 0;
      }

   }

   public int getUniform(String var1) {
      return (Integer)this.uniformsMap.get(var1);
   }

   private class CompilationResult {
      int shader;
      int status;

      CompilationResult(int var2, int var3) {
         this.shader = var2;
         this.status = var3;
      }
   }
}
