// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.shape;

import android.graphics.RectF;
import android.graphics.Path;
import android.graphics.Matrix;
import java.util.List;

public class ShapePath
{
    public float endX;
    public float endY;
    private final List<PathOperation> operations;
    public float startX;
    public float startY;
    
    public void addArc(final float n, final float n2, final float n3, final float n4, final float startAngle, final float sweepAngle) {
        final PathArcOperation pathArcOperation = new PathArcOperation(n, n2, n3, n4);
        pathArcOperation.startAngle = startAngle;
        pathArcOperation.sweepAngle = sweepAngle;
        this.operations.add((PathOperation)pathArcOperation);
        final float n5 = (n3 - n) / 2.0f;
        final double n6 = startAngle + sweepAngle;
        this.endX = (n + n3) * 0.5f + n5 * (float)Math.cos(Math.toRadians(n6));
        this.endY = (n2 + n4) * 0.5f + (n4 - n2) / 2.0f * (float)Math.sin(Math.toRadians(n6));
    }
    
    public void applyToPath(final Matrix matrix, final Path path) {
        for (int size = this.operations.size(), i = 0; i < size; ++i) {
            this.operations.get(i).applyToPath(matrix, path);
        }
    }
    
    public void lineTo(final float endX, final float endY) {
        final PathLineOperation pathLineOperation = new PathLineOperation();
        pathLineOperation.x = endX;
        pathLineOperation.y = endY;
        this.operations.add((PathOperation)pathLineOperation);
        this.endX = endX;
        this.endY = endY;
    }
    
    public void reset(final float n, final float n2) {
        this.startX = n;
        this.startY = n2;
        this.endX = n;
        this.endY = n2;
        this.operations.clear();
    }
    
    public static class PathArcOperation extends PathOperation
    {
        private static final RectF rectF;
        public float bottom;
        public float left;
        public float right;
        public float startAngle;
        public float sweepAngle;
        public float top;
        
        static {
            rectF = new RectF();
        }
        
        public PathArcOperation(final float left, final float top, final float right, final float bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
        
        @Override
        public void applyToPath(final Matrix matrix, final Path path) {
            final Matrix matrix2 = this.matrix;
            matrix.invert(matrix2);
            path.transform(matrix2);
            PathArcOperation.rectF.set(this.left, this.top, this.right, this.bottom);
            path.arcTo(PathArcOperation.rectF, this.startAngle, this.sweepAngle, false);
            path.transform(matrix);
        }
    }
    
    public static class PathLineOperation extends PathOperation
    {
        private float x;
        private float y;
        
        @Override
        public void applyToPath(final Matrix matrix, final Path path) {
            final Matrix matrix2 = this.matrix;
            matrix.invert(matrix2);
            path.transform(matrix2);
            path.lineTo(this.x, this.y);
            path.transform(matrix);
        }
    }
    
    public abstract static class PathOperation
    {
        protected final Matrix matrix;
        
        public PathOperation() {
            this.matrix = new Matrix();
        }
        
        public abstract void applyToPath(final Matrix p0, final Path p1);
    }
}
