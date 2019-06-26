// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Crop;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class CropState
{
    private float height;
    private Matrix matrix;
    private float minimumScale;
    private float rotation;
    private float scale;
    private float[] values;
    private float width;
    private float x;
    private float y;
    
    public CropState(final Bitmap bitmap) {
        this.width = (float)bitmap.getWidth();
        this.height = (float)bitmap.getHeight();
        this.x = 0.0f;
        this.y = 0.0f;
        this.scale = 1.0f;
        this.rotation = 0.0f;
        this.matrix = new Matrix();
        this.values = new float[9];
    }
    
    private void updateValues() {
        this.matrix.getValues(this.values);
    }
    
    public void getConcatMatrix(final Matrix matrix) {
        matrix.postConcat(this.matrix);
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public Matrix getMatrix() {
        final Matrix matrix = new Matrix();
        matrix.set(this.matrix);
        return matrix;
    }
    
    public float getRotation() {
        return this.rotation;
    }
    
    public float getScale() {
        return this.scale;
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public float getX() {
        this.updateValues();
        final float[] values = this.values;
        final Matrix matrix = this.matrix;
        return values[2];
    }
    
    public float getY() {
        this.updateValues();
        final float[] values = this.values;
        final Matrix matrix = this.matrix;
        return values[5];
    }
    
    public void reset(final CropAreaView cropAreaView) {
        this.matrix.reset();
        this.x = 0.0f;
        this.y = 0.0f;
        this.rotation = 0.0f;
        this.minimumScale = cropAreaView.getCropWidth() / this.width;
        this.scale = this.minimumScale;
        final Matrix matrix = this.matrix;
        final float scale = this.scale;
        matrix.postScale(scale, scale);
    }
    
    public void rotate(final float n, final float n2, final float n3) {
        this.rotation += n;
        this.matrix.postRotate(n, n2, n3);
    }
    
    public void scale(final float n, final float n2, final float n3) {
        this.scale *= n;
        this.matrix.postScale(n, n, n2, n3);
    }
    
    public void translate(final float n, final float n2) {
        this.x += n;
        this.y += n2;
        this.matrix.postTranslate(n, n2);
    }
}
