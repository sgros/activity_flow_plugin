package org.telegram.p004ui.Components.Paint;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import org.telegram.p004ui.Components.Size;

/* renamed from: org.telegram.ui.Components.Paint.Texture */
public class Texture {
    private Bitmap bitmap;
    private int texture;

    private boolean isPOT(int i) {
        return (i & (i + -1)) == 0;
    }

    public Texture(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void cleanResources(boolean z) {
        if (this.texture != 0) {
            GLES20.glDeleteTextures(1, new int[]{this.texture}, 0);
            this.texture = 0;
            if (z) {
                this.bitmap.recycle();
            }
        }
    }

    public int texture() {
        int i = this.texture;
        if (i != 0) {
            return i;
        }
        if (this.bitmap.isRecycled()) {
            return 0;
        }
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        this.texture = iArr[0];
        GLES20.glBindTexture(3553, this.texture);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLUtils.texImage2D(3553, 0, this.bitmap, 0);
        Utils.HasGLError();
        return this.texture;
    }

    public static int generateTexture(Size size) {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        int i = iArr[0];
        GLES20.glBindTexture(3553, i);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexImage2D(3553, 0, 6408, (int) size.width, (int) size.height, 0, 6408, 5121, null);
        return i;
    }
}
