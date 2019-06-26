// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import android.opengl.GLUtils;
import java.nio.Buffer;
import android.opengl.GLES20;
import org.telegram.ui.Components.Size;
import android.graphics.Bitmap;

public class Texture
{
    private Bitmap bitmap;
    private int texture;
    
    public Texture(final Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    
    public static int generateTexture(final Size size) {
        final int[] array = { 0 };
        GLES20.glGenTextures(1, array, 0);
        final int n = array[0];
        GLES20.glBindTexture(3553, n);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexImage2D(3553, 0, 6408, (int)size.width, (int)size.height, 0, 6408, 5121, (Buffer)null);
        return n;
    }
    
    private boolean isPOT(final int n) {
        return (n & n - 1) == 0x0;
    }
    
    public void cleanResources(final boolean b) {
        final int texture = this.texture;
        if (texture == 0) {
            return;
        }
        GLES20.glDeleteTextures(1, new int[] { texture }, 0);
        this.texture = 0;
        if (b) {
            this.bitmap.recycle();
        }
    }
    
    public int texture() {
        final int texture = this.texture;
        if (texture != 0) {
            return texture;
        }
        if (this.bitmap.isRecycled()) {
            return 0;
        }
        final int[] array = { 0 };
        GLES20.glGenTextures(1, array, 0);
        GLES20.glBindTexture(3553, this.texture = array[0]);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLUtils.texImage2D(3553, 0, this.bitmap, 0);
        Utils.HasGLError();
        return this.texture;
    }
}
