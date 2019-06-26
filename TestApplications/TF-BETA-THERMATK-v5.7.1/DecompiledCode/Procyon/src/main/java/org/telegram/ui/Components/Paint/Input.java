// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import android.view.MotionEvent;
import java.util.Vector;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Matrix;

public class Input
{
    private boolean beganDrawing;
    private boolean clearBuffer;
    private boolean hasMoved;
    private Matrix invertMatrix;
    private boolean isFirst;
    private Point lastLocation;
    private double lastRemainder;
    private Point[] points;
    private int pointsCount;
    private RenderView renderView;
    private float[] tempPoint;
    
    public Input(final RenderView renderView) {
        this.points = new Point[3];
        this.tempPoint = new float[2];
        this.renderView = renderView;
    }
    
    private void paintPath(final Path path) {
        path.setup(this.renderView.getCurrentColor(), this.renderView.getCurrentWeight(), this.renderView.getCurrentBrush());
        if (this.clearBuffer) {
            this.lastRemainder = 0.0;
        }
        path.remainder = this.lastRemainder;
        this.renderView.getPainting().paintStroke(path, this.clearBuffer, new Runnable() {
            final /* synthetic */ Input this$0;
            
            @Override
            public void run() {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        final Runnable this$1 = Runnable.this;
                        this$1.this$0.lastRemainder = path.remainder;
                        Input.this.clearBuffer = false;
                    }
                });
            }
        });
    }
    
    private void reset() {
        this.pointsCount = 0;
    }
    
    private Point smoothPoint(final Point point, final Point point2, final Point point3, final float n) {
        final float n2 = 1.0f - n;
        final double pow = Math.pow(n2, 2.0);
        final double n3 = n2 * 2.0f * n;
        final double n4 = n * n;
        final double x = point.x;
        final double x2 = point3.x;
        Double.isNaN(n3);
        final double x3 = point2.x;
        Double.isNaN(n4);
        final double y = point.y;
        final double y2 = point3.y;
        Double.isNaN(n3);
        final double y3 = point2.y;
        Double.isNaN(n4);
        return new Point(x * pow + x2 * n3 + x3 * n4, y * pow + y2 * n3 + y3 * n4, 1.0);
    }
    
    private void smoothenAndPaintPoints(final boolean b) {
        final int pointsCount = this.pointsCount;
        if (pointsCount > 2) {
            final Vector<Point> vector = new Vector<Point>();
            final Point[] points = this.points;
            final Point point = points[0];
            final Point point2 = points[1];
            final Point point3 = points[2];
            if (point3 == null || point2 == null || point == null) {
                return;
            }
            final Point multiplySum = point2.multiplySum(point, 0.5);
            final Point multiplySum2 = point3.multiplySum(point2, 0.5);
            final int n = (int)Math.min(48.0, Math.max(Math.floor(multiplySum.getDistanceTo(multiplySum2) / 1), 24.0));
            final float n2 = 1.0f / n;
            int i = 0;
            float n3 = 0.0f;
            while (i < n) {
                final Point smoothPoint = this.smoothPoint(multiplySum, multiplySum2, point2, n3);
                if (this.isFirst) {
                    smoothPoint.edge = true;
                    this.isFirst = false;
                }
                vector.add(smoothPoint);
                n3 += n2;
                ++i;
            }
            if (b) {
                multiplySum2.edge = true;
            }
            vector.add(multiplySum2);
            final Point[] a = new Point[vector.size()];
            vector.toArray(a);
            this.paintPath(new Path(a));
            final Point[] points2 = this.points;
            System.arraycopy(points2, 1, points2, 0, 2);
            if (b) {
                this.pointsCount = 0;
            }
            else {
                this.pointsCount = 2;
            }
        }
        else {
            final Point[] array = new Point[pointsCount];
            System.arraycopy(this.points, 0, array, 0, pointsCount);
            this.paintPath(new Path(array));
        }
    }
    
    public void process(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        final float x = motionEvent.getX();
        final float n = (float)this.renderView.getHeight();
        final float y = motionEvent.getY();
        final float[] tempPoint = this.tempPoint;
        tempPoint[0] = x;
        tempPoint[1] = n - y;
        this.invertMatrix.mapPoints(tempPoint);
        final float[] tempPoint2 = this.tempPoint;
        final Point point = new Point(tempPoint2[0], tempPoint2[1], 1.0);
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                if (!this.hasMoved) {
                    if (this.renderView.shouldDraw()) {
                        point.edge = true;
                        this.paintPath(new Path(point));
                    }
                    this.reset();
                }
                else if (this.pointsCount > 0) {
                    this.smoothenAndPaintPoints(true);
                }
                this.pointsCount = 0;
                this.renderView.getPainting().commitStroke(this.renderView.getCurrentColor());
                this.beganDrawing = false;
                this.renderView.onFinishedDrawing(this.hasMoved);
                return;
            }
            if (actionMasked != 2) {
                return;
            }
        }
        if (!this.beganDrawing) {
            this.beganDrawing = true;
            this.hasMoved = false;
            this.isFirst = true;
            this.lastLocation = point;
            this.points[0] = point;
            this.pointsCount = 1;
            this.clearBuffer = true;
        }
        else {
            if (point.getDistanceTo(this.lastLocation) < AndroidUtilities.dp(5.0f)) {
                return;
            }
            if (!this.hasMoved) {
                this.renderView.onBeganDrawing();
                this.hasMoved = true;
            }
            final Point[] points = this.points;
            final int pointsCount = this.pointsCount;
            points[pointsCount] = point;
            this.pointsCount = pointsCount + 1;
            if (this.pointsCount == 3) {
                this.smoothenAndPaintPoints(false);
            }
            this.lastLocation = point;
        }
    }
    
    public void setMatrix(final Matrix matrix) {
        matrix.invert(this.invertMatrix = new Matrix());
    }
}
