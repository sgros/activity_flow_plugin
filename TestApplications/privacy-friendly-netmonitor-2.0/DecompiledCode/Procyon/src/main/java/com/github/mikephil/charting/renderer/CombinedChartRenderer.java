// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.data.CombinedData;
import java.util.Iterator;
import android.graphics.Canvas;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
import java.util.ArrayList;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.highlight.Highlight;
import java.util.List;
import com.github.mikephil.charting.charts.Chart;
import java.lang.ref.WeakReference;

public class CombinedChartRenderer extends DataRenderer
{
    protected WeakReference<Chart> mChart;
    protected List<Highlight> mHighlightBuffer;
    protected List<DataRenderer> mRenderers;
    
    public CombinedChartRenderer(final CombinedChart referent, final ChartAnimator chartAnimator, final ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mRenderers = new ArrayList<DataRenderer>(5);
        this.mHighlightBuffer = new ArrayList<Highlight>();
        this.mChart = new WeakReference<Chart>(referent);
        this.createRenderers();
    }
    
    public void createRenderers() {
        this.mRenderers.clear();
        final CombinedChart combinedChart = this.mChart.get();
        if (combinedChart == null) {
            return;
        }
        final CombinedChart.DrawOrder[] drawOrder = combinedChart.getDrawOrder();
        for (int length = drawOrder.length, i = 0; i < length; ++i) {
            switch (CombinedChartRenderer$1.$SwitchMap$com$github$mikephil$charting$charts$CombinedChart$DrawOrder[drawOrder[i].ordinal()]) {
                case 5: {
                    if (combinedChart.getScatterData() != null) {
                        this.mRenderers.add(new ScatterChartRenderer(combinedChart, this.mAnimator, this.mViewPortHandler));
                        break;
                    }
                    break;
                }
                case 4: {
                    if (combinedChart.getCandleData() != null) {
                        this.mRenderers.add(new CandleStickChartRenderer(combinedChart, this.mAnimator, this.mViewPortHandler));
                        break;
                    }
                    break;
                }
                case 3: {
                    if (combinedChart.getLineData() != null) {
                        this.mRenderers.add(new LineChartRenderer(combinedChart, this.mAnimator, this.mViewPortHandler));
                        break;
                    }
                    break;
                }
                case 2: {
                    if (combinedChart.getBubbleData() != null) {
                        this.mRenderers.add(new BubbleChartRenderer(combinedChart, this.mAnimator, this.mViewPortHandler));
                        break;
                    }
                    break;
                }
                case 1: {
                    if (combinedChart.getBarData() != null) {
                        this.mRenderers.add(new BarChartRenderer(combinedChart, this.mAnimator, this.mViewPortHandler));
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void drawData(final Canvas canvas) {
        final Iterator<DataRenderer> iterator = this.mRenderers.iterator();
        while (iterator.hasNext()) {
            iterator.next().drawData(canvas);
        }
    }
    
    @Override
    public void drawExtras(final Canvas canvas) {
        final Iterator<DataRenderer> iterator = this.mRenderers.iterator();
        while (iterator.hasNext()) {
            iterator.next().drawExtras(canvas);
        }
    }
    
    @Override
    public void drawHighlighted(final Canvas canvas, final Highlight[] array) {
        final Chart<CombinedData> chart = this.mChart.get();
        if (chart == null) {
            return;
        }
        for (final DataRenderer dataRenderer : this.mRenderers) {
            Object o = null;
            if (dataRenderer instanceof BarChartRenderer) {
                o = ((BarChartRenderer)dataRenderer).mChart.getBarData();
            }
            else if (dataRenderer instanceof LineChartRenderer) {
                o = ((LineChartRenderer)dataRenderer).mChart.getLineData();
            }
            else if (dataRenderer instanceof CandleStickChartRenderer) {
                o = ((CandleStickChartRenderer)dataRenderer).mChart.getCandleData();
            }
            else if (dataRenderer instanceof ScatterChartRenderer) {
                o = ((ScatterChartRenderer)dataRenderer).mChart.getScatterData();
            }
            else if (dataRenderer instanceof BubbleChartRenderer) {
                o = ((BubbleChartRenderer)dataRenderer).mChart.getBubbleData();
            }
            int index;
            if (o == null) {
                index = -1;
            }
            else {
                index = chart.getData().getAllData().indexOf(o);
            }
            this.mHighlightBuffer.clear();
            for (final Highlight highlight : array) {
                if (highlight.getDataIndex() == index || highlight.getDataIndex() == -1) {
                    this.mHighlightBuffer.add(highlight);
                }
            }
            dataRenderer.drawHighlighted(canvas, this.mHighlightBuffer.toArray(new Highlight[this.mHighlightBuffer.size()]));
        }
    }
    
    @Override
    public void drawValues(final Canvas canvas) {
        final Iterator<DataRenderer> iterator = this.mRenderers.iterator();
        while (iterator.hasNext()) {
            iterator.next().drawValues(canvas);
        }
    }
    
    public DataRenderer getSubRenderer(final int n) {
        if (n < this.mRenderers.size() && n >= 0) {
            return this.mRenderers.get(n);
        }
        return null;
    }
    
    public List<DataRenderer> getSubRenderers() {
        return this.mRenderers;
    }
    
    @Override
    public void initBuffers() {
        final Iterator<DataRenderer> iterator = this.mRenderers.iterator();
        while (iterator.hasNext()) {
            iterator.next().initBuffers();
        }
    }
    
    public void setSubRenderers(final List<DataRenderer> mRenderers) {
        this.mRenderers = mRenderers;
    }
}
