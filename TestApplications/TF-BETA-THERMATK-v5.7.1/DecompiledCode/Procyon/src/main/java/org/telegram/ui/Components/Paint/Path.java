// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import java.util.Collection;
import java.util.Arrays;
import java.util.Vector;

public class Path
{
    private float baseWeight;
    private Brush brush;
    private int color;
    private Vector<Point> points;
    public double remainder;
    
    public Path(final Point e) {
        (this.points = new Vector<Point>()).add(e);
    }
    
    public Path(final Point[] a) {
        (this.points = new Vector<Point>()).addAll(Arrays.asList(a));
    }
    
    public float getBaseWeight() {
        return this.baseWeight;
    }
    
    public Brush getBrush() {
        return this.brush;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public int getLength() {
        final Vector<Point> points = this.points;
        if (points == null) {
            return 0;
        }
        return points.size();
    }
    
    public Point[] getPoints() {
        final Point[] a = new Point[this.points.size()];
        this.points.toArray(a);
        return a;
    }
    
    public void setup(final int color, final float baseWeight, final Brush brush) {
        this.color = color;
        this.baseWeight = baseWeight;
        this.brush = brush;
    }
}
