// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.animation;

import android.graphics.Matrix;
import android.widget.ImageView;
import android.util.Property;

public class ImageMatrixProperty extends Property<ImageView, Matrix>
{
    private final Matrix matrix;
    
    public ImageMatrixProperty() {
        super((Class)Matrix.class, "imageMatrixProperty");
        this.matrix = new Matrix();
    }
    
    public Matrix get(final ImageView imageView) {
        this.matrix.set(imageView.getImageMatrix());
        return this.matrix;
    }
    
    public void set(final ImageView imageView, final Matrix imageMatrix) {
        imageView.setImageMatrix(imageMatrix);
    }
}
