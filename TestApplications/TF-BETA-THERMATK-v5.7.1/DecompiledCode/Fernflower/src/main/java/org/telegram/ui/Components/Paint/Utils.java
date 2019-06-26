package org.telegram.ui.Components.Paint;

import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class Utils {
   public static void HasGLError() {
      int var0 = GLES20.glGetError();
      if (var0 != 0) {
         Log.d("Paint", GLUtils.getEGLErrorString(var0));
      }

   }

   public static void RectFIntegral(RectF var0) {
      var0.left = (float)((int)Math.floor((double)var0.left));
      var0.top = (float)((int)Math.floor((double)var0.top));
      var0.right = (float)((int)Math.ceil((double)var0.right));
      var0.bottom = (float)((int)Math.ceil((double)var0.bottom));
   }
}
