// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import android.util.Log;
import com.github.mikephil.charting.utils.Utils;
import android.content.Context;
import com.github.mikephil.charting.utils.ColorTemplate;
import android.graphics.Color;
import java.util.ArrayList;
import com.github.mikephil.charting.formatter.DefaultFillFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import android.graphics.DashPathEffect;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class LineDataSet extends LineRadarDataSet<Entry> implements ILineDataSet
{
    private int mCircleColorHole;
    private List<Integer> mCircleColors;
    private float mCircleHoleRadius;
    private float mCircleRadius;
    private float mCubicIntensity;
    private DashPathEffect mDashPathEffect;
    private boolean mDrawCircleHole;
    private boolean mDrawCircles;
    private IFillFormatter mFillFormatter;
    private Mode mMode;
    
    public LineDataSet(final List<Entry> list, final String s) {
        super(list, s);
        this.mMode = Mode.LINEAR;
        this.mCircleColors = null;
        this.mCircleColorHole = -1;
        this.mCircleRadius = 8.0f;
        this.mCircleHoleRadius = 4.0f;
        this.mCubicIntensity = 0.2f;
        this.mDashPathEffect = null;
        this.mFillFormatter = new DefaultFillFormatter();
        this.mDrawCircles = true;
        this.mDrawCircleHole = true;
        if (this.mCircleColors == null) {
            this.mCircleColors = new ArrayList<Integer>();
        }
        this.mCircleColors.clear();
        this.mCircleColors.add(Color.rgb(140, 234, 255));
    }
    
    @Override
    public DataSet<Entry> copy() {
        final ArrayList<Entry> list = new ArrayList<Entry>();
        for (int i = 0; i < this.mValues.size(); ++i) {
            list.add(this.mValues.get(i).copy());
        }
        final LineDataSet set = new LineDataSet(list, this.getLabel());
        set.mMode = this.mMode;
        set.mColors = this.mColors;
        set.mCircleRadius = this.mCircleRadius;
        set.mCircleHoleRadius = this.mCircleHoleRadius;
        set.mCircleColors = this.mCircleColors;
        set.mDashPathEffect = this.mDashPathEffect;
        set.mDrawCircles = this.mDrawCircles;
        set.mDrawCircleHole = this.mDrawCircleHole;
        set.mHighLightColor = this.mHighLightColor;
        return set;
    }
    
    public void disableDashedLine() {
        this.mDashPathEffect = null;
    }
    
    public void enableDashedLine(final float n, final float n2, final float n3) {
        this.mDashPathEffect = new DashPathEffect(new float[] { n, n2 }, n3);
    }
    
    @Override
    public int getCircleColor(final int n) {
        return this.mCircleColors.get(n);
    }
    
    @Override
    public int getCircleColorCount() {
        return this.mCircleColors.size();
    }
    
    public List<Integer> getCircleColors() {
        return this.mCircleColors;
    }
    
    @Override
    public int getCircleHoleColor() {
        return this.mCircleColorHole;
    }
    
    @Override
    public float getCircleHoleRadius() {
        return this.mCircleHoleRadius;
    }
    
    @Override
    public float getCircleRadius() {
        return this.mCircleRadius;
    }
    
    @Deprecated
    public float getCircleSize() {
        return this.getCircleRadius();
    }
    
    @Override
    public float getCubicIntensity() {
        return this.mCubicIntensity;
    }
    
    @Override
    public DashPathEffect getDashPathEffect() {
        return this.mDashPathEffect;
    }
    
    @Override
    public IFillFormatter getFillFormatter() {
        return this.mFillFormatter;
    }
    
    @Override
    public Mode getMode() {
        return this.mMode;
    }
    
    @Override
    public boolean isDashedLineEnabled() {
        return this.mDashPathEffect != null;
    }
    
    @Override
    public boolean isDrawCircleHoleEnabled() {
        return this.mDrawCircleHole;
    }
    
    @Override
    public boolean isDrawCirclesEnabled() {
        return this.mDrawCircles;
    }
    
    @Deprecated
    @Override
    public boolean isDrawCubicEnabled() {
        return this.mMode == Mode.CUBIC_BEZIER;
    }
    
    @Deprecated
    @Override
    public boolean isDrawSteppedEnabled() {
        return this.mMode == Mode.STEPPED;
    }
    
    public void resetCircleColors() {
        if (this.mCircleColors == null) {
            this.mCircleColors = new ArrayList<Integer>();
        }
        this.mCircleColors.clear();
    }
    
    public void setCircleColor(final int i) {
        this.resetCircleColors();
        this.mCircleColors.add(i);
    }
    
    public void setCircleColorHole(final int mCircleColorHole) {
        this.mCircleColorHole = mCircleColorHole;
    }
    
    public void setCircleColors(final List<Integer> mCircleColors) {
        this.mCircleColors = mCircleColors;
    }
    
    public void setCircleColors(final int... array) {
        this.mCircleColors = ColorTemplate.createColors(array);
    }
    
    public void setCircleColors(final int[] array, final Context context) {
        List<Integer> mCircleColors;
        if ((mCircleColors = this.mCircleColors) == null) {
            mCircleColors = new ArrayList<Integer>();
        }
        mCircleColors.clear();
        for (int length = array.length, i = 0; i < length; ++i) {
            mCircleColors.add(context.getResources().getColor(array[i]));
        }
        this.mCircleColors = mCircleColors;
    }
    
    public void setCircleHoleRadius(final float n) {
        if (n >= 0.5f) {
            this.mCircleHoleRadius = Utils.convertDpToPixel(n);
        }
        else {
            Log.e("LineDataSet", "Circle radius cannot be < 0.5");
        }
    }
    
    public void setCircleRadius(final float n) {
        if (n >= 1.0f) {
            this.mCircleRadius = Utils.convertDpToPixel(n);
        }
        else {
            Log.e("LineDataSet", "Circle radius cannot be < 1");
        }
    }
    
    @Deprecated
    public void setCircleSize(final float circleRadius) {
        this.setCircleRadius(circleRadius);
    }
    
    public void setCubicIntensity(float mCubicIntensity) {
        float n = mCubicIntensity;
        if (mCubicIntensity > 1.0f) {
            n = 1.0f;
        }
        mCubicIntensity = n;
        if (n < 0.05f) {
            mCubicIntensity = 0.05f;
        }
        this.mCubicIntensity = mCubicIntensity;
    }
    
    public void setDrawCircleHole(final boolean mDrawCircleHole) {
        this.mDrawCircleHole = mDrawCircleHole;
    }
    
    public void setDrawCircles(final boolean mDrawCircles) {
        this.mDrawCircles = mDrawCircles;
    }
    
    public void setFillFormatter(final IFillFormatter mFillFormatter) {
        if (mFillFormatter == null) {
            this.mFillFormatter = new DefaultFillFormatter();
        }
        else {
            this.mFillFormatter = mFillFormatter;
        }
    }
    
    public void setMode(final Mode mMode) {
        this.mMode = mMode;
    }
    
    public enum Mode
    {
        CUBIC_BEZIER, 
        HORIZONTAL_BEZIER, 
        LINEAR, 
        STEPPED;
    }
}
