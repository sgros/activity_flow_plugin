package org.telegram.p004ui.Components.Paint;

import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/* renamed from: org.telegram.ui.Components.Paint.Utils */
public class Utils {
    public static void HasGLError() {
        int glGetError = GLES20.glGetError();
        if (glGetError != 0) {
            Log.d("Paint", GLUtils.getEGLErrorString(glGetError));
        }
    }

    public static void RectFIntegral(RectF rectF) {
        rectF.left = (float) ((int) Math.floor((double) rectF.left));
        rectF.top = (float) ((int) Math.floor((double) rectF.top));
        rectF.right = (float) ((int) Math.ceil((double) rectF.right));
        rectF.bottom = (float) ((int) Math.ceil((double) rectF.bottom));
    }
}
