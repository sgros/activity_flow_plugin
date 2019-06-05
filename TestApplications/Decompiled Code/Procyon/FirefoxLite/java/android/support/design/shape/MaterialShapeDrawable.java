// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.shape;

import android.graphics.Rect;
import android.graphics.Region$Op;
import android.graphics.ColorFilter;
import android.graphics.Canvas;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Region;
import android.graphics.PointF;
import android.graphics.Path;
import android.graphics.Paint$Style;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.support.v4.graphics.drawable.TintAwareDrawable;
import android.graphics.drawable.Drawable;

public class MaterialShapeDrawable extends Drawable implements TintAwareDrawable
{
    private int alpha;
    private final ShapePath[] cornerPaths;
    private final Matrix[] cornerTransforms;
    private final Matrix[] edgeTransforms;
    private float interpolation;
    private final Matrix matrix;
    private final Paint paint;
    private Paint$Style paintStyle;
    private final Path path;
    private final PointF pointF;
    private float scale;
    private final float[] scratch;
    private final float[] scratch2;
    private final Region scratchRegion;
    private int shadowColor;
    private int shadowElevation;
    private boolean shadowEnabled;
    private int shadowRadius;
    private final ShapePath shapePath;
    private ShapePathModel shapedViewModel;
    private float strokeWidth;
    private PorterDuffColorFilter tintFilter;
    private ColorStateList tintList;
    private PorterDuff$Mode tintMode;
    private final Region transparentRegion;
    private boolean useTintColorForShadow;
    
    private float angleOfCorner(final int n, final int n2, final int n3) {
        this.getCoordinatesOfCorner((n - 1 + 4) % 4, n2, n3, this.pointF);
        final float x = this.pointF.x;
        final float y = this.pointF.y;
        this.getCoordinatesOfCorner((n + 1) % 4, n2, n3, this.pointF);
        final float x2 = this.pointF.x;
        final float y2 = this.pointF.y;
        this.getCoordinatesOfCorner(n, n2, n3, this.pointF);
        final float x3 = this.pointF.x;
        final float y3 = this.pointF.y;
        float n5;
        final float n4 = n5 = (float)Math.atan2(y - y3, x - x3) - (float)Math.atan2(y2 - y3, x2 - x3);
        if (n4 < 0.0f) {
            n5 = (float)(n4 + 6.283185307179586);
        }
        return n5;
    }
    
    private float angleOfEdge(final int n, final int n2, final int n3) {
        this.getCoordinatesOfCorner(n, n2, n3, this.pointF);
        final float x = this.pointF.x;
        final float y = this.pointF.y;
        this.getCoordinatesOfCorner((n + 1) % 4, n2, n3, this.pointF);
        return (float)Math.atan2(this.pointF.y - y, this.pointF.x - x);
    }
    
    private void appendCornerPath(final int n, final Path path) {
        this.scratch[0] = this.cornerPaths[n].startX;
        this.scratch[1] = this.cornerPaths[n].startY;
        this.cornerTransforms[n].mapPoints(this.scratch);
        if (n == 0) {
            path.moveTo(this.scratch[0], this.scratch[1]);
        }
        else {
            path.lineTo(this.scratch[0], this.scratch[1]);
        }
        this.cornerPaths[n].applyToPath(this.cornerTransforms[n], path);
    }
    
    private void appendEdgePath(final int n, final Path path) {
        final int n2 = (n + 1) % 4;
        this.scratch[0] = this.cornerPaths[n].endX;
        this.scratch[1] = this.cornerPaths[n].endY;
        this.cornerTransforms[n].mapPoints(this.scratch);
        this.scratch2[0] = this.cornerPaths[n2].startX;
        this.scratch2[1] = this.cornerPaths[n2].startY;
        this.cornerTransforms[n2].mapPoints(this.scratch2);
        final float n3 = (float)Math.hypot(this.scratch[0] - this.scratch2[0], this.scratch[1] - this.scratch2[1]);
        this.shapePath.reset(0.0f, 0.0f);
        this.getEdgeTreatmentForIndex(n).getEdgePath(n3, this.interpolation, this.shapePath);
        this.shapePath.applyToPath(this.edgeTransforms[n], path);
    }
    
    private void getCoordinatesOfCorner(final int n, final int n2, final int n3, final PointF pointF) {
        switch (n) {
            default: {
                pointF.set(0.0f, 0.0f);
                break;
            }
            case 3: {
                pointF.set(0.0f, (float)n3);
                break;
            }
            case 2: {
                pointF.set((float)n2, (float)n3);
                break;
            }
            case 1: {
                pointF.set((float)n2, 0.0f);
                break;
            }
        }
    }
    
    private CornerTreatment getCornerTreatmentForIndex(final int n) {
        switch (n) {
            default: {
                return this.shapedViewModel.getTopLeftCorner();
            }
            case 3: {
                return this.shapedViewModel.getBottomLeftCorner();
            }
            case 2: {
                return this.shapedViewModel.getBottomRightCorner();
            }
            case 1: {
                return this.shapedViewModel.getTopRightCorner();
            }
        }
    }
    
    private EdgeTreatment getEdgeTreatmentForIndex(final int n) {
        switch (n) {
            default: {
                return this.shapedViewModel.getTopEdge();
            }
            case 3: {
                return this.shapedViewModel.getLeftEdge();
            }
            case 2: {
                return this.shapedViewModel.getBottomEdge();
            }
            case 1: {
                return this.shapedViewModel.getRightEdge();
            }
        }
    }
    
    private void getPath(final int n, final int n2, final Path path) {
        this.getPathForSize(n, n2, path);
        if (this.scale == 1.0f) {
            return;
        }
        this.matrix.reset();
        this.matrix.setScale(this.scale, this.scale, (float)(n / 2), (float)(n2 / 2));
        path.transform(this.matrix);
    }
    
    private static int modulateAlpha(final int n, final int n2) {
        return n * (n2 + (n2 >>> 7)) >>> 8;
    }
    
    private void setCornerPathAndTransform(final int n, final int n2, final int n3) {
        this.getCoordinatesOfCorner(n, n2, n3, this.pointF);
        this.getCornerTreatmentForIndex(n).getCornerPath(this.angleOfCorner(n, n2, n3), this.interpolation, this.cornerPaths[n]);
        final float angleOfEdge = this.angleOfEdge((n - 1 + 4) % 4, n2, n3);
        this.cornerTransforms[n].reset();
        this.cornerTransforms[n].setTranslate(this.pointF.x, this.pointF.y);
        this.cornerTransforms[n].preRotate((float)Math.toDegrees(angleOfEdge + 1.5707964f));
    }
    
    private void setEdgeTransform(final int n, final int n2, final int n3) {
        this.scratch[0] = this.cornerPaths[n].endX;
        this.scratch[1] = this.cornerPaths[n].endY;
        this.cornerTransforms[n].mapPoints(this.scratch);
        final float angleOfEdge = this.angleOfEdge(n, n2, n3);
        this.edgeTransforms[n].reset();
        this.edgeTransforms[n].setTranslate(this.scratch[0], this.scratch[1]);
        this.edgeTransforms[n].preRotate((float)Math.toDegrees(angleOfEdge));
    }
    
    private void updateTintFilter() {
        if (this.tintList != null && this.tintMode != null) {
            final int colorForState = this.tintList.getColorForState(this.getState(), 0);
            this.tintFilter = new PorterDuffColorFilter(colorForState, this.tintMode);
            if (this.useTintColorForShadow) {
                this.shadowColor = colorForState;
            }
            return;
        }
        this.tintFilter = null;
    }
    
    public void draw(final Canvas canvas) {
        this.paint.setColorFilter((ColorFilter)this.tintFilter);
        final int alpha = this.paint.getAlpha();
        this.paint.setAlpha(modulateAlpha(alpha, this.alpha));
        this.paint.setStrokeWidth(this.strokeWidth);
        this.paint.setStyle(this.paintStyle);
        if (this.shadowElevation > 0 && this.shadowEnabled) {
            this.paint.setShadowLayer((float)this.shadowRadius, 0.0f, (float)this.shadowElevation, this.shadowColor);
        }
        if (this.shapedViewModel != null) {
            this.getPath(canvas.getWidth(), canvas.getHeight(), this.path);
            canvas.drawPath(this.path, this.paint);
        }
        else {
            canvas.drawRect(0.0f, 0.0f, (float)canvas.getWidth(), (float)canvas.getHeight(), this.paint);
        }
        this.paint.setAlpha(alpha);
    }
    
    public int getOpacity() {
        return -3;
    }
    
    public void getPathForSize(final int n, final int n2, final Path path) {
        path.rewind();
        if (this.shapedViewModel == null) {
            return;
        }
        final int n3 = 0;
        int n4 = 0;
        int i;
        while (true) {
            i = n3;
            if (n4 >= 4) {
                break;
            }
            this.setCornerPathAndTransform(n4, n, n2);
            this.setEdgeTransform(n4, n, n2);
            ++n4;
        }
        while (i < 4) {
            this.appendCornerPath(i, path);
            this.appendEdgePath(i, path);
            ++i;
        }
        path.close();
    }
    
    public ColorStateList getTintList() {
        return this.tintList;
    }
    
    public Region getTransparentRegion() {
        final Rect bounds = this.getBounds();
        this.transparentRegion.set(bounds);
        this.getPath(bounds.width(), bounds.height(), this.path);
        this.scratchRegion.setPath(this.path, this.transparentRegion);
        this.transparentRegion.op(this.scratchRegion, Region$Op.DIFFERENCE);
        return this.transparentRegion;
    }
    
    public void setAlpha(final int alpha) {
        this.alpha = alpha;
        this.invalidateSelf();
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        this.invalidateSelf();
    }
    
    public void setInterpolation(final float interpolation) {
        this.interpolation = interpolation;
        this.invalidateSelf();
    }
    
    public void setTint(final int n) {
        this.setTintList(ColorStateList.valueOf(n));
    }
    
    public void setTintList(final ColorStateList tintList) {
        this.tintList = tintList;
        this.updateTintFilter();
        this.invalidateSelf();
    }
    
    public void setTintMode(final PorterDuff$Mode tintMode) {
        this.tintMode = tintMode;
        this.updateTintFilter();
        this.invalidateSelf();
    }
}
