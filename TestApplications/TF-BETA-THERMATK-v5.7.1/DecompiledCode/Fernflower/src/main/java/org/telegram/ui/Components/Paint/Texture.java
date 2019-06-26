package org.telegram.ui.Components.Paint;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import java.nio.Buffer;
import org.telegram.ui.Components.Size;

public class Texture {
   private Bitmap bitmap;
   private int texture;

   public Texture(Bitmap var1) {
      this.bitmap = var1;
   }

   public static int generateTexture(Size var0) {
      int[] var1 = new int[1];
      GLES20.glGenTextures(1, var1, 0);
      int var2 = var1[0];
      GLES20.glBindTexture(3553, var2);
      GLES20.glTexParameteri(3553, 10242, 33071);
      GLES20.glTexParameteri(3553, 10243, 33071);
      GLES20.glTexParameteri(3553, 10240, 9729);
      GLES20.glTexParameteri(3553, 10241, 9729);
      GLES20.glTexImage2D(3553, 0, 6408, (int)var0.width, (int)var0.height, 0, 6408, 5121, (Buffer)null);
      return var2;
   }

   private boolean isPOT(int var1) {
      boolean var2;
      if ((var1 & var1 - 1) == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void cleanResources(boolean var1) {
      int var2 = this.texture;
      if (var2 != 0) {
         GLES20.glDeleteTextures(1, new int[]{var2}, 0);
         this.texture = 0;
         if (var1) {
            this.bitmap.recycle();
         }

      }
   }

   public int texture() {
      int var1 = this.texture;
      if (var1 != 0) {
         return var1;
      } else if (this.bitmap.isRecycled()) {
         return 0;
      } else {
         int[] var2 = new int[1];
         GLES20.glGenTextures(1, var2, 0);
         this.texture = var2[0];
         GLES20.glBindTexture(3553, this.texture);
         GLES20.glTexParameteri(3553, 10242, 33071);
         GLES20.glTexParameteri(3553, 10243, 33071);
         GLES20.glTexParameteri(3553, 10240, 9729);
         GLES20.glTexParameteri(3553, 10241, 9729);
         GLUtils.texImage2D(3553, 0, this.bitmap, 0);
         Utils.HasGLError();
         return this.texture;
      }
   }
}
