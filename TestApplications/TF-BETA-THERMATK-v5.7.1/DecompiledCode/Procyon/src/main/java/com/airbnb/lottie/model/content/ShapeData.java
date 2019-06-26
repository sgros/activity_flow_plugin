// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.utils.Logger;
import java.util.Collection;
import java.util.ArrayList;
import android.graphics.PointF;
import com.airbnb.lottie.model.CubicCurveData;
import java.util.List;

public class ShapeData
{
    private boolean closed;
    private final List<CubicCurveData> curves;
    private PointF initialPoint;
    
    public ShapeData() {
        this.curves = new ArrayList<CubicCurveData>();
    }
    
    public ShapeData(final PointF initialPoint, final boolean closed, final List<CubicCurveData> c) {
        this.initialPoint = initialPoint;
        this.closed = closed;
        this.curves = new ArrayList<CubicCurveData>(c);
    }
    
    private void setInitialPoint(final float n, final float n2) {
        if (this.initialPoint == null) {
            this.initialPoint = new PointF();
        }
        this.initialPoint.set(n, n2);
    }
    
    public List<CubicCurveData> getCurves() {
        return this.curves;
    }
    
    public PointF getInitialPoint() {
        return this.initialPoint;
    }
    
    public void interpolateBetween(final ShapeData shapeData, final ShapeData shapeData2, final float n) {
        if (this.initialPoint == null) {
            this.initialPoint = new PointF();
        }
        this.closed = (shapeData.isClosed() || shapeData2.isClosed());
        if (shapeData.getCurves().size() != shapeData2.getCurves().size()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Curves must have the same number of control points. Shape 1: ");
            sb.append(shapeData.getCurves().size());
            sb.append("\tShape 2: ");
            sb.append(shapeData2.getCurves().size());
            Logger.warning(sb.toString());
        }
        final int min = Math.min(shapeData.getCurves().size(), shapeData2.getCurves().size());
        if (this.curves.size() < min) {
            for (int i = this.curves.size(); i < min; ++i) {
                this.curves.add(new CubicCurveData());
            }
        }
        else if (this.curves.size() > min) {
            for (int j = this.curves.size() - 1; j >= min; --j) {
                final List<CubicCurveData> curves = this.curves;
                curves.remove(curves.size() - 1);
            }
        }
        final PointF initialPoint = shapeData.getInitialPoint();
        final PointF initialPoint2 = shapeData2.getInitialPoint();
        this.setInitialPoint(MiscUtils.lerp(initialPoint.x, initialPoint2.x, n), MiscUtils.lerp(initialPoint.y, initialPoint2.y, n));
        for (int k = this.curves.size() - 1; k >= 0; --k) {
            final CubicCurveData cubicCurveData = shapeData.getCurves().get(k);
            final CubicCurveData cubicCurveData2 = shapeData2.getCurves().get(k);
            final PointF controlPoint1 = cubicCurveData.getControlPoint1();
            final PointF controlPoint2 = cubicCurveData.getControlPoint2();
            final PointF vertex = cubicCurveData.getVertex();
            final PointF controlPoint3 = cubicCurveData2.getControlPoint1();
            final PointF controlPoint4 = cubicCurveData2.getControlPoint2();
            final PointF vertex2 = cubicCurveData2.getVertex();
            this.curves.get(k).setControlPoint1(MiscUtils.lerp(controlPoint1.x, controlPoint3.x, n), MiscUtils.lerp(controlPoint1.y, controlPoint3.y, n));
            this.curves.get(k).setControlPoint2(MiscUtils.lerp(controlPoint2.x, controlPoint4.x, n), MiscUtils.lerp(controlPoint2.y, controlPoint4.y, n));
            this.curves.get(k).setVertex(MiscUtils.lerp(vertex.x, vertex2.x, n), MiscUtils.lerp(vertex.y, vertex2.y, n));
        }
    }
    
    public boolean isClosed() {
        return this.closed;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ShapeData{numCurves=");
        sb.append(this.curves.size());
        sb.append("closed=");
        sb.append(this.closed);
        sb.append('}');
        return sb.toString();
    }
}
