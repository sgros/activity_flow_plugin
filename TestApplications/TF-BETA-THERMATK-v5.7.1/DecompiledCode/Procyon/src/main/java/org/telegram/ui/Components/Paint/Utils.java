// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import android.graphics.RectF;
import android.util.Log;
import android.opengl.GLUtils;
import android.opengl.GLES20;

public class Utils
{
    public static void HasGLError() {
        final int glGetError = GLES20.glGetError();
        if (glGetError != 0) {
            Log.d("Paint", GLUtils.getEGLErrorString(glGetError));
        }
    }
    
    public static void RectFIntegral(final RectF rectF) {
        rectF.left = (float)(int)Math.floor(rectF.left);
        rectF.top = (float)(int)Math.floor(rectF.top);
        rectF.right = (float)(int)Math.ceil(rectF.right);
        rectF.bottom = (float)(int)Math.ceil(rectF.bottom);
    }
}
