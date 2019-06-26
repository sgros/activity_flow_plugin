package com.airbnb.lottie.model.content;

import android.graphics.PointF;
import com.airbnb.lottie.model.CubicCurveData;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.utils.MiscUtils;
import java.util.ArrayList;
import java.util.List;

public class ShapeData {
    private boolean closed;
    private final List<CubicCurveData> curves;
    private PointF initialPoint;

    public ShapeData(PointF pointF, boolean z, List<CubicCurveData> list) {
        this.initialPoint = pointF;
        this.closed = z;
        this.curves = new ArrayList(list);
    }

    public ShapeData() {
        this.curves = new ArrayList();
    }

    private void setInitialPoint(float f, float f2) {
        if (this.initialPoint == null) {
            this.initialPoint = new PointF();
        }
        this.initialPoint.set(f, f2);
    }

    public PointF getInitialPoint() {
        return this.initialPoint;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public List<CubicCurveData> getCurves() {
        return this.curves;
    }

    public void interpolateBetween(ShapeData shapeData, ShapeData shapeData2, float f) {
        if (this.initialPoint == null) {
            this.initialPoint = new PointF();
        }
        boolean z = shapeData.isClosed() || shapeData2.isClosed();
        this.closed = z;
        if (shapeData.getCurves().size() != shapeData2.getCurves().size()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Curves must have the same number of control points. Shape 1: ");
            stringBuilder.append(shapeData.getCurves().size());
            stringBuilder.append("\tShape 2: ");
            stringBuilder.append(shapeData2.getCurves().size());
            Logger.warning(stringBuilder.toString());
        }
        int min = Math.min(shapeData.getCurves().size(), shapeData2.getCurves().size());
        int size;
        if (this.curves.size() < min) {
            for (size = this.curves.size(); size < min; size++) {
                this.curves.add(new CubicCurveData());
            }
        } else if (this.curves.size() > min) {
            for (size = this.curves.size() - 1; size >= min; size--) {
                List list = this.curves;
                list.remove(list.size() - 1);
            }
        }
        PointF initialPoint = shapeData.getInitialPoint();
        PointF initialPoint2 = shapeData2.getInitialPoint();
        setInitialPoint(MiscUtils.lerp(initialPoint.x, initialPoint2.x, f), MiscUtils.lerp(initialPoint.y, initialPoint2.y, f));
        for (min = this.curves.size() - 1; min >= 0; min--) {
            CubicCurveData cubicCurveData = (CubicCurveData) shapeData.getCurves().get(min);
            CubicCurveData cubicCurveData2 = (CubicCurveData) shapeData2.getCurves().get(min);
            PointF controlPoint1 = cubicCurveData.getControlPoint1();
            PointF controlPoint2 = cubicCurveData.getControlPoint2();
            PointF vertex = cubicCurveData.getVertex();
            PointF controlPoint12 = cubicCurveData2.getControlPoint1();
            PointF controlPoint22 = cubicCurveData2.getControlPoint2();
            initialPoint2 = cubicCurveData2.getVertex();
            ((CubicCurveData) this.curves.get(min)).setControlPoint1(MiscUtils.lerp(controlPoint1.x, controlPoint12.x, f), MiscUtils.lerp(controlPoint1.y, controlPoint12.y, f));
            ((CubicCurveData) this.curves.get(min)).setControlPoint2(MiscUtils.lerp(controlPoint2.x, controlPoint22.x, f), MiscUtils.lerp(controlPoint2.y, controlPoint22.y, f));
            ((CubicCurveData) this.curves.get(min)).setVertex(MiscUtils.lerp(vertex.x, initialPoint2.x, f), MiscUtils.lerp(vertex.y, initialPoint2.y, f));
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ShapeData{numCurves=");
        stringBuilder.append(this.curves.size());
        stringBuilder.append("closed=");
        stringBuilder.append(this.closed);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
