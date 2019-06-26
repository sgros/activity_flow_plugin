// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.charts;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Canvas;
import com.github.mikephil.charting.highlight.PieHighlighter;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;
import java.util.List;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.RectF;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.data.PieData;

public class PieChart extends PieRadarChartBase<PieData>
{
    private float[] mAbsoluteAngles;
    private CharSequence mCenterText;
    private MPPointF mCenterTextOffset;
    private float mCenterTextRadiusPercent;
    private RectF mCircleBox;
    private float[] mDrawAngles;
    private boolean mDrawCenterText;
    private boolean mDrawEntryLabels;
    private boolean mDrawHole;
    private boolean mDrawRoundedSlices;
    private boolean mDrawSlicesUnderHole;
    private float mHoleRadiusPercent;
    protected float mMaxAngle;
    protected float mTransparentCircleRadiusPercent;
    private boolean mUsePercentValues;
    
    public PieChart(final Context context) {
        super(context);
        this.mCircleBox = new RectF();
        this.mDrawEntryLabels = true;
        this.mDrawAngles = new float[1];
        this.mAbsoluteAngles = new float[1];
        this.mDrawHole = true;
        this.mDrawSlicesUnderHole = false;
        this.mUsePercentValues = false;
        this.mDrawRoundedSlices = false;
        this.mCenterText = "";
        this.mCenterTextOffset = MPPointF.getInstance(0.0f, 0.0f);
        this.mHoleRadiusPercent = 50.0f;
        this.mTransparentCircleRadiusPercent = 55.0f;
        this.mDrawCenterText = true;
        this.mCenterTextRadiusPercent = 100.0f;
        this.mMaxAngle = 360.0f;
    }
    
    public PieChart(final Context context, final AttributeSet set) {
        super(context, set);
        this.mCircleBox = new RectF();
        this.mDrawEntryLabels = true;
        this.mDrawAngles = new float[1];
        this.mAbsoluteAngles = new float[1];
        this.mDrawHole = true;
        this.mDrawSlicesUnderHole = false;
        this.mUsePercentValues = false;
        this.mDrawRoundedSlices = false;
        this.mCenterText = "";
        this.mCenterTextOffset = MPPointF.getInstance(0.0f, 0.0f);
        this.mHoleRadiusPercent = 50.0f;
        this.mTransparentCircleRadiusPercent = 55.0f;
        this.mDrawCenterText = true;
        this.mCenterTextRadiusPercent = 100.0f;
        this.mMaxAngle = 360.0f;
    }
    
    public PieChart(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mCircleBox = new RectF();
        this.mDrawEntryLabels = true;
        this.mDrawAngles = new float[1];
        this.mAbsoluteAngles = new float[1];
        this.mDrawHole = true;
        this.mDrawSlicesUnderHole = false;
        this.mUsePercentValues = false;
        this.mDrawRoundedSlices = false;
        this.mCenterText = "";
        this.mCenterTextOffset = MPPointF.getInstance(0.0f, 0.0f);
        this.mHoleRadiusPercent = 50.0f;
        this.mTransparentCircleRadiusPercent = 55.0f;
        this.mDrawCenterText = true;
        this.mCenterTextRadiusPercent = 100.0f;
        this.mMaxAngle = 360.0f;
    }
    
    private float calcAngle(final float n) {
        return this.calcAngle(n, ((PieData)this.mData).getYValueSum());
    }
    
    private float calcAngle(final float n, final float n2) {
        return n / n2 * this.mMaxAngle;
    }
    
    private void calcAngles() {
        final int entryCount = ((PieData)this.mData).getEntryCount();
        if (this.mDrawAngles.length != entryCount) {
            this.mDrawAngles = new float[entryCount];
        }
        else {
            for (int i = 0; i < entryCount; ++i) {
                this.mDrawAngles[i] = 0.0f;
            }
        }
        if (this.mAbsoluteAngles.length != entryCount) {
            this.mAbsoluteAngles = new float[entryCount];
        }
        else {
            for (int j = 0; j < entryCount; ++j) {
                this.mAbsoluteAngles[j] = 0.0f;
            }
        }
        final float yValueSum = ((PieData)this.mData).getYValueSum();
        final List<IPieDataSet> dataSets = ((PieData)this.mData).getDataSets();
        int n;
        for (int k = n = 0; k < ((PieData)this.mData).getDataSetCount(); ++k) {
            final IPieDataSet set = dataSets.get(k);
            for (int l = 0; l < set.getEntryCount(); ++l) {
                this.mDrawAngles[n] = this.calcAngle(Math.abs(set.getEntryForIndex(l).getY()), yValueSum);
                if (n == 0) {
                    this.mAbsoluteAngles[n] = this.mDrawAngles[n];
                }
                else {
                    this.mAbsoluteAngles[n] = this.mAbsoluteAngles[n - 1] + this.mDrawAngles[n];
                }
                ++n;
            }
        }
    }
    
    @Override
    protected void calcMinMax() {
        this.calcAngles();
    }
    
    @Override
    public void calculateOffsets() {
        super.calculateOffsets();
        if (this.mData == null) {
            return;
        }
        final float n = this.getDiameter() / 2.0f;
        final MPPointF centerOffsets = this.getCenterOffsets();
        final float selectionShift = ((PieData)this.mData).getDataSet().getSelectionShift();
        this.mCircleBox.set(centerOffsets.x - n + selectionShift, centerOffsets.y - n + selectionShift, centerOffsets.x + n - selectionShift, centerOffsets.y + n - selectionShift);
        MPPointF.recycleInstance(centerOffsets);
    }
    
    public float[] getAbsoluteAngles() {
        return this.mAbsoluteAngles;
    }
    
    public MPPointF getCenterCircleBox() {
        return MPPointF.getInstance(this.mCircleBox.centerX(), this.mCircleBox.centerY());
    }
    
    public CharSequence getCenterText() {
        return this.mCenterText;
    }
    
    public MPPointF getCenterTextOffset() {
        return MPPointF.getInstance(this.mCenterTextOffset.x, this.mCenterTextOffset.y);
    }
    
    public float getCenterTextRadiusPercent() {
        return this.mCenterTextRadiusPercent;
    }
    
    public RectF getCircleBox() {
        return this.mCircleBox;
    }
    
    public int getDataSetIndexForIndex(final int n) {
        final List<IPieDataSet> dataSets = ((PieData)this.mData).getDataSets();
        for (int i = 0; i < dataSets.size(); ++i) {
            if (dataSets.get(i).getEntryForXValue((float)n, Float.NaN) != null) {
                return i;
            }
        }
        return -1;
    }
    
    public float[] getDrawAngles() {
        return this.mDrawAngles;
    }
    
    public float getHoleRadius() {
        return this.mHoleRadiusPercent;
    }
    
    @Override
    public int getIndexForAngle(float normalizedAngle) {
        normalizedAngle = Utils.getNormalizedAngle(normalizedAngle - this.getRotationAngle());
        for (int i = 0; i < this.mAbsoluteAngles.length; ++i) {
            if (this.mAbsoluteAngles[i] > normalizedAngle) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    protected float[] getMarkerPosition(final Highlight highlight) {
        final MPPointF centerCircleBox = this.getCenterCircleBox();
        final float radius = this.getRadius();
        float n = radius / 10.0f * 3.6f;
        if (this.isDrawHoleEnabled()) {
            n = (radius - radius / 100.0f * this.getHoleRadius()) / 2.0f;
        }
        final float rotationAngle = this.getRotationAngle();
        final int n2 = (int)highlight.getX();
        final float n3 = this.mDrawAngles[n2] / 2.0f;
        final double n4 = radius - n;
        final float n5 = (float)(Math.cos(Math.toRadians((this.mAbsoluteAngles[n2] + rotationAngle - n3) * this.mAnimator.getPhaseY())) * n4 + centerCircleBox.x);
        final float n6 = (float)(n4 * Math.sin(Math.toRadians((rotationAngle + this.mAbsoluteAngles[n2] - n3) * this.mAnimator.getPhaseY())) + centerCircleBox.y);
        MPPointF.recycleInstance(centerCircleBox);
        return new float[] { n5, n6 };
    }
    
    public float getMaxAngle() {
        return this.mMaxAngle;
    }
    
    @Override
    public float getRadius() {
        if (this.mCircleBox == null) {
            return 0.0f;
        }
        return Math.min(this.mCircleBox.width() / 2.0f, this.mCircleBox.height() / 2.0f);
    }
    
    @Override
    protected float getRequiredBaseOffset() {
        return 0.0f;
    }
    
    @Override
    protected float getRequiredLegendOffset() {
        return this.mLegendRenderer.getLabelPaint().getTextSize() * 2.0f;
    }
    
    public float getTransparentCircleRadius() {
        return this.mTransparentCircleRadiusPercent;
    }
    
    @Deprecated
    @Override
    public XAxis getXAxis() {
        throw new RuntimeException("PieChart has no XAxis");
    }
    
    @Override
    protected void init() {
        super.init();
        this.mRenderer = new PieChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.mXAxis = null;
        this.mHighlighter = new PieHighlighter(this);
    }
    
    public boolean isDrawCenterTextEnabled() {
        return this.mDrawCenterText;
    }
    
    public boolean isDrawEntryLabelsEnabled() {
        return this.mDrawEntryLabels;
    }
    
    public boolean isDrawHoleEnabled() {
        return this.mDrawHole;
    }
    
    public boolean isDrawRoundedSlicesEnabled() {
        return this.mDrawRoundedSlices;
    }
    
    public boolean isDrawSlicesUnderHoleEnabled() {
        return this.mDrawSlicesUnderHole;
    }
    
    public boolean isUsePercentValuesEnabled() {
        return this.mUsePercentValues;
    }
    
    public boolean needsHighlight(final int n) {
        if (!this.valuesToHighlight()) {
            return false;
        }
        for (int i = 0; i < this.mIndicesToHighlight.length; ++i) {
            if ((int)this.mIndicesToHighlight[i].getX() == n) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected void onDetachedFromWindow() {
        if (this.mRenderer != null && this.mRenderer instanceof PieChartRenderer) {
            ((PieChartRenderer)this.mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }
    
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mData == null) {
            return;
        }
        this.mRenderer.drawData(canvas);
        if (this.valuesToHighlight()) {
            this.mRenderer.drawHighlighted(canvas, this.mIndicesToHighlight);
        }
        this.mRenderer.drawExtras(canvas);
        this.mRenderer.drawValues(canvas);
        this.mLegendRenderer.renderLegend(canvas);
        this.drawDescription(canvas);
        this.drawMarkers(canvas);
    }
    
    public void setCenterText(final CharSequence mCenterText) {
        if (mCenterText == null) {
            this.mCenterText = "";
        }
        else {
            this.mCenterText = mCenterText;
        }
    }
    
    public void setCenterTextColor(final int color) {
        ((PieChartRenderer)this.mRenderer).getPaintCenterText().setColor(color);
    }
    
    public void setCenterTextOffset(final float n, final float n2) {
        this.mCenterTextOffset.x = Utils.convertDpToPixel(n);
        this.mCenterTextOffset.y = Utils.convertDpToPixel(n2);
    }
    
    public void setCenterTextRadiusPercent(final float mCenterTextRadiusPercent) {
        this.mCenterTextRadiusPercent = mCenterTextRadiusPercent;
    }
    
    public void setCenterTextSize(final float n) {
        ((PieChartRenderer)this.mRenderer).getPaintCenterText().setTextSize(Utils.convertDpToPixel(n));
    }
    
    public void setCenterTextSizePixels(final float textSize) {
        ((PieChartRenderer)this.mRenderer).getPaintCenterText().setTextSize(textSize);
    }
    
    public void setCenterTextTypeface(final Typeface typeface) {
        ((PieChartRenderer)this.mRenderer).getPaintCenterText().setTypeface(typeface);
    }
    
    public void setDrawCenterText(final boolean mDrawCenterText) {
        this.mDrawCenterText = mDrawCenterText;
    }
    
    public void setDrawEntryLabels(final boolean mDrawEntryLabels) {
        this.mDrawEntryLabels = mDrawEntryLabels;
    }
    
    public void setDrawHoleEnabled(final boolean mDrawHole) {
        this.mDrawHole = mDrawHole;
    }
    
    @Deprecated
    public void setDrawSliceText(final boolean mDrawEntryLabels) {
        this.mDrawEntryLabels = mDrawEntryLabels;
    }
    
    public void setDrawSlicesUnderHole(final boolean mDrawSlicesUnderHole) {
        this.mDrawSlicesUnderHole = mDrawSlicesUnderHole;
    }
    
    public void setEntryLabelColor(final int color) {
        ((PieChartRenderer)this.mRenderer).getPaintEntryLabels().setColor(color);
    }
    
    public void setEntryLabelTextSize(final float n) {
        ((PieChartRenderer)this.mRenderer).getPaintEntryLabels().setTextSize(Utils.convertDpToPixel(n));
    }
    
    public void setEntryLabelTypeface(final Typeface typeface) {
        ((PieChartRenderer)this.mRenderer).getPaintEntryLabels().setTypeface(typeface);
    }
    
    public void setHoleColor(final int color) {
        ((PieChartRenderer)this.mRenderer).getPaintHole().setColor(color);
    }
    
    public void setHoleRadius(final float mHoleRadiusPercent) {
        this.mHoleRadiusPercent = mHoleRadiusPercent;
    }
    
    public void setMaxAngle(float mMaxAngle) {
        float n = mMaxAngle;
        if (mMaxAngle > 360.0f) {
            n = 360.0f;
        }
        mMaxAngle = n;
        if (n < 90.0f) {
            mMaxAngle = 90.0f;
        }
        this.mMaxAngle = mMaxAngle;
    }
    
    public void setTransparentCircleAlpha(final int alpha) {
        ((PieChartRenderer)this.mRenderer).getPaintTransparentCircle().setAlpha(alpha);
    }
    
    public void setTransparentCircleColor(final int color) {
        final Paint paintTransparentCircle = ((PieChartRenderer)this.mRenderer).getPaintTransparentCircle();
        final int alpha = paintTransparentCircle.getAlpha();
        paintTransparentCircle.setColor(color);
        paintTransparentCircle.setAlpha(alpha);
    }
    
    public void setTransparentCircleRadius(final float mTransparentCircleRadiusPercent) {
        this.mTransparentCircleRadiusPercent = mTransparentCircleRadiusPercent;
    }
    
    public void setUsePercentValues(final boolean mUsePercentValues) {
        this.mUsePercentValues = mUsePercentValues;
    }
}
