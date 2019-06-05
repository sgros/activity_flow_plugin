package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CombinedChartRenderer extends DataRenderer {
   protected WeakReference mChart;
   protected List mHighlightBuffer = new ArrayList();
   protected List mRenderers = new ArrayList(5);

   public CombinedChartRenderer(CombinedChart var1, ChartAnimator var2, ViewPortHandler var3) {
      super(var2, var3);
      this.mChart = new WeakReference(var1);
      this.createRenderers();
   }

   public void createRenderers() {
      this.mRenderers.clear();
      CombinedChart var1 = (CombinedChart)this.mChart.get();
      if (var1 != null) {
         CombinedChart.DrawOrder[] var2 = var1.getDrawOrder();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            CombinedChart.DrawOrder var5 = var2[var4];
            switch(var5) {
            case BAR:
               if (var1.getBarData() != null) {
                  this.mRenderers.add(new BarChartRenderer(var1, this.mAnimator, this.mViewPortHandler));
               }
               break;
            case BUBBLE:
               if (var1.getBubbleData() != null) {
                  this.mRenderers.add(new BubbleChartRenderer(var1, this.mAnimator, this.mViewPortHandler));
               }
               break;
            case LINE:
               if (var1.getLineData() != null) {
                  this.mRenderers.add(new LineChartRenderer(var1, this.mAnimator, this.mViewPortHandler));
               }
               break;
            case CANDLE:
               if (var1.getCandleData() != null) {
                  this.mRenderers.add(new CandleStickChartRenderer(var1, this.mAnimator, this.mViewPortHandler));
               }
               break;
            case SCATTER:
               if (var1.getScatterData() != null) {
                  this.mRenderers.add(new ScatterChartRenderer(var1, this.mAnimator, this.mViewPortHandler));
               }
            }
         }

      }
   }

   public void drawData(Canvas var1) {
      Iterator var2 = this.mRenderers.iterator();

      while(var2.hasNext()) {
         ((DataRenderer)var2.next()).drawData(var1);
      }

   }

   public void drawExtras(Canvas var1) {
      Iterator var2 = this.mRenderers.iterator();

      while(var2.hasNext()) {
         ((DataRenderer)var2.next()).drawExtras(var1);
      }

   }

   public void drawHighlighted(Canvas var1, Highlight[] var2) {
      Chart var3 = (Chart)this.mChart.get();
      if (var3 != null) {
         Iterator var4 = this.mRenderers.iterator();

         while(var4.hasNext()) {
            DataRenderer var5 = (DataRenderer)var4.next();
            Object var6 = null;
            if (var5 instanceof BarChartRenderer) {
               var6 = ((BarChartRenderer)var5).mChart.getBarData();
            } else if (var5 instanceof LineChartRenderer) {
               var6 = ((LineChartRenderer)var5).mChart.getLineData();
            } else if (var5 instanceof CandleStickChartRenderer) {
               var6 = ((CandleStickChartRenderer)var5).mChart.getCandleData();
            } else if (var5 instanceof ScatterChartRenderer) {
               var6 = ((ScatterChartRenderer)var5).mChart.getScatterData();
            } else if (var5 instanceof BubbleChartRenderer) {
               var6 = ((BubbleChartRenderer)var5).mChart.getBubbleData();
            }

            int var7;
            if (var6 == null) {
               var7 = -1;
            } else {
               var7 = ((CombinedData)var3.getData()).getAllData().indexOf(var6);
            }

            this.mHighlightBuffer.clear();
            int var8 = var2.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               Highlight var10 = var2[var9];
               if (var10.getDataIndex() == var7 || var10.getDataIndex() == -1) {
                  this.mHighlightBuffer.add(var10);
               }
            }

            var5.drawHighlighted(var1, (Highlight[])this.mHighlightBuffer.toArray(new Highlight[this.mHighlightBuffer.size()]));
         }

      }
   }

   public void drawValues(Canvas var1) {
      Iterator var2 = this.mRenderers.iterator();

      while(var2.hasNext()) {
         ((DataRenderer)var2.next()).drawValues(var1);
      }

   }

   public DataRenderer getSubRenderer(int var1) {
      return var1 < this.mRenderers.size() && var1 >= 0 ? (DataRenderer)this.mRenderers.get(var1) : null;
   }

   public List getSubRenderers() {
      return this.mRenderers;
   }

   public void initBuffers() {
      Iterator var1 = this.mRenderers.iterator();

      while(var1.hasNext()) {
         ((DataRenderer)var1.next()).initBuffers();
      }

   }

   public void setSubRenderers(List var1) {
      this.mRenderers = var1;
   }
}
