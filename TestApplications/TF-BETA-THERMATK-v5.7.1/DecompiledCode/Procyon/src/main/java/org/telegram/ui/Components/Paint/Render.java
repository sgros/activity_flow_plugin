// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import android.graphics.PointF;
import java.nio.FloatBuffer;
import java.nio.Buffer;
import android.opengl.GLES20;
import android.graphics.Matrix;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import android.graphics.RectF;

public class Render
{
    private static RectF Draw(final RenderState renderState) {
        final RectF rectF = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        final int count = renderState.getCount();
        if (count == 0) {
            return rectF;
        }
        final int n = count - 1;
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect((count * 4 + n * 2) * 20);
        allocateDirect.order(ByteOrder.nativeOrder());
        final FloatBuffer floatBuffer = allocateDirect.asFloatBuffer();
        floatBuffer.position(0);
        renderState.setPosition(0);
        int i = 0;
        int n2 = 0;
        while (i < count) {
            final float read = renderState.read();
            final float read2 = renderState.read();
            final float read3 = renderState.read();
            final float read4 = renderState.read();
            final float read5 = renderState.read();
            final RectF rectF2 = new RectF(read - read3, read2 - read3, read + read3, read2 + read3);
            final float[] array = new float[8];
            final float left = rectF2.left;
            array[0] = left;
            final float top = rectF2.top;
            array[1] = top;
            final float right = rectF2.right;
            array[2] = right;
            array[3] = top;
            array[4] = left;
            final float bottom = rectF2.bottom;
            array[5] = bottom;
            array[6] = right;
            array[7] = bottom;
            final float centerX = rectF2.centerX();
            final float centerY = rectF2.centerY();
            final Matrix matrix = new Matrix();
            matrix.setRotate((float)Math.toDegrees(read4), centerX, centerY);
            matrix.mapPoints(array);
            matrix.mapRect(rectF2);
            Utils.RectFIntegral(rectF2);
            rectF.union(rectF2);
            if (n2 != 0) {
                floatBuffer.put(array[0]);
                floatBuffer.put(array[1]);
                floatBuffer.put(0.0f);
                floatBuffer.put(0.0f);
                floatBuffer.put(read5);
                ++n2;
            }
            floatBuffer.put(array[0]);
            floatBuffer.put(array[1]);
            floatBuffer.put(0.0f);
            floatBuffer.put(0.0f);
            floatBuffer.put(read5);
            floatBuffer.put(array[2]);
            floatBuffer.put(array[3]);
            floatBuffer.put(1.0f);
            floatBuffer.put(0.0f);
            floatBuffer.put(read5);
            floatBuffer.put(array[4]);
            floatBuffer.put(array[5]);
            floatBuffer.put(0.0f);
            floatBuffer.put(1.0f);
            floatBuffer.put(read5);
            floatBuffer.put(array[6]);
            floatBuffer.put(array[7]);
            floatBuffer.put(1.0f);
            floatBuffer.put(1.0f);
            floatBuffer.put(read5);
            final int n3 = n2 = n2 + 1 + 1 + 1 + 1;
            if (i != n) {
                floatBuffer.put(array[6]);
                floatBuffer.put(array[7]);
                floatBuffer.put(1.0f);
                floatBuffer.put(1.0f);
                floatBuffer.put(read5);
                n2 = n3 + 1;
            }
            ++i;
        }
        floatBuffer.position(0);
        GLES20.glVertexAttribPointer(0, 2, 5126, false, 20, (Buffer)floatBuffer.slice());
        GLES20.glEnableVertexAttribArray(0);
        floatBuffer.position(2);
        GLES20.glVertexAttribPointer(1, 2, 5126, true, 20, (Buffer)floatBuffer.slice());
        GLES20.glEnableVertexAttribArray(1);
        floatBuffer.position(4);
        GLES20.glVertexAttribPointer(2, 1, 5126, true, 20, (Buffer)floatBuffer.slice());
        GLES20.glEnableVertexAttribArray(2);
        GLES20.glDrawArrays(5, 0, n2);
        return rectF;
    }
    
    private static void PaintSegment(Point point, final Point point2, final RenderState renderState) {
        final double v = point.getDistanceTo(point2);
        final Point substract = point2.substract(point);
        Point multiplyByScalar = new Point(1.0, 1.0, 0.0);
        float angle;
        if (Math.abs(renderState.angle) > 0.0f) {
            angle = renderState.angle;
        }
        else {
            angle = (float)Math.atan2(substract.y, substract.x);
        }
        final float n = renderState.baseWeight * renderState.scale;
        final double n2 = Math.max(1.0f, renderState.spacing * n);
        if (v > 0.0) {
            Double.isNaN(v);
            multiplyByScalar = substract.multiplyByScalar(1.0 / v);
        }
        final float min = Math.min(1.0f, renderState.alpha * 1.15f);
        int edge = point.edge ? 1 : 0;
        final boolean edge2 = point2.edge;
        final double remainder = renderState.remainder;
        Double.isNaN(v);
        Double.isNaN(n2);
        final int n3 = (int)Math.ceil((v - remainder) / n2);
        final int count = renderState.getCount();
        renderState.appendValuesCount(n3);
        renderState.setPosition(count);
        point = point.add(multiplyByScalar.multiplyByScalar(renderState.remainder));
        double remainder2 = renderState.remainder;
        boolean addPoint = true;
        while (remainder2 <= v) {
            float alpha;
            if (edge != 0) {
                alpha = min;
            }
            else {
                alpha = renderState.alpha;
            }
            addPoint = renderState.addPoint(point.toPointF(), n, angle, alpha, -1);
            if (!addPoint) {
                break;
            }
            point = point.add(multiplyByScalar.multiplyByScalar(n2));
            edge = 0;
            Double.isNaN(n2);
            remainder2 += n2;
        }
        if (addPoint && edge2) {
            renderState.appendValuesCount(1);
            renderState.addPoint(point2.toPointF(), n, angle, min, -1);
        }
        Double.isNaN(v);
        renderState.remainder = remainder2 - v;
    }
    
    private static void PaintStamp(final Point point, final RenderState renderState) {
        final float baseWeight = renderState.baseWeight;
        final float scale = renderState.scale;
        final PointF pointF = point.toPointF();
        float angle;
        if (Math.abs(renderState.angle) > 0.0f) {
            angle = renderState.angle;
        }
        else {
            angle = 0.0f;
        }
        final float alpha = renderState.alpha;
        renderState.prepare();
        renderState.appendValuesCount(1);
        renderState.addPoint(pointF, baseWeight * scale, angle, alpha, 0);
    }
    
    public static RectF RenderPath(final Path path, final RenderState renderState) {
        renderState.baseWeight = path.getBaseWeight();
        renderState.spacing = path.getBrush().getSpacing();
        renderState.alpha = path.getBrush().getAlpha();
        renderState.angle = path.getBrush().getAngle();
        renderState.scale = path.getBrush().getScale();
        final int length = path.getLength();
        if (length == 0) {
            return null;
        }
        int i = 0;
        if (length == 1) {
            PaintStamp(path.getPoints()[0], renderState);
        }
        else {
            final Point[] points = path.getPoints();
            renderState.prepare();
            while (i < points.length - 1) {
                final Point point = points[i];
                ++i;
                PaintSegment(point, points[i], renderState);
            }
        }
        path.remainder = renderState.remainder;
        return Draw(renderState);
    }
}
