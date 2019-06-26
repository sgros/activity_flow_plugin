package com.github.mikephil.charting.data;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.renderer.scatter.ChevronDownShapeRenderer;
import com.github.mikephil.charting.renderer.scatter.ChevronUpShapeRenderer;
import com.github.mikephil.charting.renderer.scatter.CircleShapeRenderer;
import com.github.mikephil.charting.renderer.scatter.CrossShapeRenderer;
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer;
import com.github.mikephil.charting.renderer.scatter.SquareShapeRenderer;
import com.github.mikephil.charting.renderer.scatter.TriangleShapeRenderer;
import com.github.mikephil.charting.renderer.scatter.XShapeRenderer;
import java.util.ArrayList;
import java.util.List;

public class ScatterDataSet extends LineScatterCandleRadarDataSet implements IScatterDataSet {
   private int mScatterShapeHoleColor = 1122867;
   private float mScatterShapeHoleRadius = 0.0F;
   protected IShapeRenderer mShapeRenderer = new SquareShapeRenderer();
   private float mShapeSize = 15.0F;

   public ScatterDataSet(List var1, String var2) {
      super(var1, var2);
   }

   public static IShapeRenderer getRendererForShape(ScatterChart.ScatterShape var0) {
      switch(var0) {
      case SQUARE:
         return new SquareShapeRenderer();
      case CIRCLE:
         return new CircleShapeRenderer();
      case TRIANGLE:
         return new TriangleShapeRenderer();
      case CROSS:
         return new CrossShapeRenderer();
      case X:
         return new XShapeRenderer();
      case CHEVRON_UP:
         return new ChevronUpShapeRenderer();
      case CHEVRON_DOWN:
         return new ChevronDownShapeRenderer();
      default:
         return null;
      }
   }

   public DataSet copy() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.mValues.size(); ++var2) {
         var1.add(((Entry)this.mValues.get(var2)).copy());
      }

      ScatterDataSet var3 = new ScatterDataSet(var1, this.getLabel());
      var3.mDrawValues = this.mDrawValues;
      var3.mValueColors = this.mValueColors;
      var3.mColors = this.mColors;
      var3.mShapeSize = this.mShapeSize;
      var3.mShapeRenderer = this.mShapeRenderer;
      var3.mScatterShapeHoleRadius = this.mScatterShapeHoleRadius;
      var3.mScatterShapeHoleColor = this.mScatterShapeHoleColor;
      var3.mHighlightLineWidth = this.mHighlightLineWidth;
      var3.mHighLightColor = this.mHighLightColor;
      var3.mHighlightDashPathEffect = this.mHighlightDashPathEffect;
      return var3;
   }

   public int getScatterShapeHoleColor() {
      return this.mScatterShapeHoleColor;
   }

   public float getScatterShapeHoleRadius() {
      return this.mScatterShapeHoleRadius;
   }

   public float getScatterShapeSize() {
      return this.mShapeSize;
   }

   public IShapeRenderer getShapeRenderer() {
      return this.mShapeRenderer;
   }

   public void setScatterShape(ScatterChart.ScatterShape var1) {
      this.mShapeRenderer = getRendererForShape(var1);
   }

   public void setScatterShapeHoleColor(int var1) {
      this.mScatterShapeHoleColor = var1;
   }

   public void setScatterShapeHoleRadius(float var1) {
      this.mScatterShapeHoleRadius = var1;
   }

   public void setScatterShapeSize(float var1) {
      this.mShapeSize = var1;
   }

   public void setShapeRenderer(IShapeRenderer var1) {
      this.mShapeRenderer = var1;
   }
}
