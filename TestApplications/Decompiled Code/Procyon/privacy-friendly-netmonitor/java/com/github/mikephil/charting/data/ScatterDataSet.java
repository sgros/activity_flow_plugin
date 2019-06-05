// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import java.util.ArrayList;
import com.github.mikephil.charting.renderer.scatter.CircleShapeRenderer;
import com.github.mikephil.charting.renderer.scatter.TriangleShapeRenderer;
import com.github.mikephil.charting.renderer.scatter.CrossShapeRenderer;
import com.github.mikephil.charting.renderer.scatter.XShapeRenderer;
import com.github.mikephil.charting.renderer.scatter.ChevronUpShapeRenderer;
import com.github.mikephil.charting.renderer.scatter.ChevronDownShapeRenderer;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.renderer.scatter.SquareShapeRenderer;
import java.util.List;
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

public class ScatterDataSet extends LineScatterCandleRadarDataSet<Entry> implements IScatterDataSet
{
    private int mScatterShapeHoleColor;
    private float mScatterShapeHoleRadius;
    protected IShapeRenderer mShapeRenderer;
    private float mShapeSize;
    
    public ScatterDataSet(final List<Entry> list, final String s) {
        super(list, s);
        this.mShapeSize = 15.0f;
        this.mShapeRenderer = new SquareShapeRenderer();
        this.mScatterShapeHoleRadius = 0.0f;
        this.mScatterShapeHoleColor = 1122867;
    }
    
    public static IShapeRenderer getRendererForShape(final ScatterChart.ScatterShape scatterShape) {
        switch (ScatterDataSet$1.$SwitchMap$com$github$mikephil$charting$charts$ScatterChart$ScatterShape[scatterShape.ordinal()]) {
            default: {
                return null;
            }
            case 7: {
                return new ChevronDownShapeRenderer();
            }
            case 6: {
                return new ChevronUpShapeRenderer();
            }
            case 5: {
                return new XShapeRenderer();
            }
            case 4: {
                return new CrossShapeRenderer();
            }
            case 3: {
                return new TriangleShapeRenderer();
            }
            case 2: {
                return new CircleShapeRenderer();
            }
            case 1: {
                return new SquareShapeRenderer();
            }
        }
    }
    
    @Override
    public DataSet<Entry> copy() {
        final ArrayList<Entry> list = new ArrayList<Entry>();
        for (int i = 0; i < this.mValues.size(); ++i) {
            list.add(this.mValues.get(i).copy());
        }
        final ScatterDataSet set = new ScatterDataSet(list, this.getLabel());
        set.mDrawValues = this.mDrawValues;
        set.mValueColors = this.mValueColors;
        set.mColors = this.mColors;
        set.mShapeSize = this.mShapeSize;
        set.mShapeRenderer = this.mShapeRenderer;
        set.mScatterShapeHoleRadius = this.mScatterShapeHoleRadius;
        set.mScatterShapeHoleColor = this.mScatterShapeHoleColor;
        set.mHighlightLineWidth = this.mHighlightLineWidth;
        set.mHighLightColor = this.mHighLightColor;
        set.mHighlightDashPathEffect = this.mHighlightDashPathEffect;
        return set;
    }
    
    @Override
    public int getScatterShapeHoleColor() {
        return this.mScatterShapeHoleColor;
    }
    
    @Override
    public float getScatterShapeHoleRadius() {
        return this.mScatterShapeHoleRadius;
    }
    
    @Override
    public float getScatterShapeSize() {
        return this.mShapeSize;
    }
    
    @Override
    public IShapeRenderer getShapeRenderer() {
        return this.mShapeRenderer;
    }
    
    public void setScatterShape(final ScatterChart.ScatterShape scatterShape) {
        this.mShapeRenderer = getRendererForShape(scatterShape);
    }
    
    public void setScatterShapeHoleColor(final int mScatterShapeHoleColor) {
        this.mScatterShapeHoleColor = mScatterShapeHoleColor;
    }
    
    public void setScatterShapeHoleRadius(final float mScatterShapeHoleRadius) {
        this.mScatterShapeHoleRadius = mScatterShapeHoleRadius;
    }
    
    public void setScatterShapeSize(final float mShapeSize) {
        this.mShapeSize = mShapeSize;
    }
    
    public void setShapeRenderer(final IShapeRenderer mShapeRenderer) {
        this.mShapeRenderer = mShapeRenderer;
    }
}
