// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model;

import android.graphics.PointF;

public class CubicCurveData
{
    private final PointF controlPoint1;
    private final PointF controlPoint2;
    private final PointF vertex;
    
    public CubicCurveData() {
        this.controlPoint1 = new PointF();
        this.controlPoint2 = new PointF();
        this.vertex = new PointF();
    }
    
    public CubicCurveData(final PointF controlPoint1, final PointF controlPoint2, final PointF vertex) {
        this.controlPoint1 = controlPoint1;
        this.controlPoint2 = controlPoint2;
        this.vertex = vertex;
    }
    
    public PointF getControlPoint1() {
        return this.controlPoint1;
    }
    
    public PointF getControlPoint2() {
        return this.controlPoint2;
    }
    
    public PointF getVertex() {
        return this.vertex;
    }
    
    public void setControlPoint1(final float n, final float n2) {
        this.controlPoint1.set(n, n2);
    }
    
    public void setControlPoint2(final float n, final float n2) {
        this.controlPoint2.set(n, n2);
    }
    
    public void setVertex(final float n, final float n2) {
        this.vertex.set(n, n2);
    }
}
