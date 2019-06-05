package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class PieDataSet extends DataSet implements IPieDataSet {
   private boolean mAutomaticallyDisableSliceSpacing;
   private float mShift = 18.0F;
   private float mSliceSpace = 0.0F;
   private int mValueLineColor;
   private float mValueLinePart1Length;
   private float mValueLinePart1OffsetPercentage;
   private float mValueLinePart2Length;
   private boolean mValueLineVariableLength;
   private float mValueLineWidth;
   private PieDataSet.ValuePosition mXValuePosition;
   private PieDataSet.ValuePosition mYValuePosition;

   public PieDataSet(List var1, String var2) {
      super(var1, var2);
      this.mXValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE;
      this.mYValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE;
      this.mValueLineColor = -16777216;
      this.mValueLineWidth = 1.0F;
      this.mValueLinePart1OffsetPercentage = 75.0F;
      this.mValueLinePart1Length = 0.3F;
      this.mValueLinePart2Length = 0.4F;
      this.mValueLineVariableLength = true;
   }

   protected void calcMinMax(PieEntry var1) {
      if (var1 != null) {
         this.calcMinMaxY(var1);
      }
   }

   public DataSet copy() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.mValues.size(); ++var2) {
         var1.add(((PieEntry)this.mValues.get(var2)).copy());
      }

      PieDataSet var3 = new PieDataSet(var1, this.getLabel());
      var3.mColors = this.mColors;
      var3.mSliceSpace = this.mSliceSpace;
      var3.mShift = this.mShift;
      return var3;
   }

   public float getSelectionShift() {
      return this.mShift;
   }

   public float getSliceSpace() {
      return this.mSliceSpace;
   }

   public int getValueLineColor() {
      return this.mValueLineColor;
   }

   public float getValueLinePart1Length() {
      return this.mValueLinePart1Length;
   }

   public float getValueLinePart1OffsetPercentage() {
      return this.mValueLinePart1OffsetPercentage;
   }

   public float getValueLinePart2Length() {
      return this.mValueLinePart2Length;
   }

   public float getValueLineWidth() {
      return this.mValueLineWidth;
   }

   public PieDataSet.ValuePosition getXValuePosition() {
      return this.mXValuePosition;
   }

   public PieDataSet.ValuePosition getYValuePosition() {
      return this.mYValuePosition;
   }

   public boolean isAutomaticallyDisableSliceSpacingEnabled() {
      return this.mAutomaticallyDisableSliceSpacing;
   }

   public boolean isValueLineVariableLength() {
      return this.mValueLineVariableLength;
   }

   public void setAutomaticallyDisableSliceSpacing(boolean var1) {
      this.mAutomaticallyDisableSliceSpacing = var1;
   }

   public void setSelectionShift(float var1) {
      this.mShift = Utils.convertDpToPixel(var1);
   }

   public void setSliceSpace(float var1) {
      float var2 = var1;
      if (var1 > 20.0F) {
         var2 = 20.0F;
      }

      var1 = var2;
      if (var2 < 0.0F) {
         var1 = 0.0F;
      }

      this.mSliceSpace = Utils.convertDpToPixel(var1);
   }

   public void setValueLineColor(int var1) {
      this.mValueLineColor = var1;
   }

   public void setValueLinePart1Length(float var1) {
      this.mValueLinePart1Length = var1;
   }

   public void setValueLinePart1OffsetPercentage(float var1) {
      this.mValueLinePart1OffsetPercentage = var1;
   }

   public void setValueLinePart2Length(float var1) {
      this.mValueLinePart2Length = var1;
   }

   public void setValueLineVariableLength(boolean var1) {
      this.mValueLineVariableLength = var1;
   }

   public void setValueLineWidth(float var1) {
      this.mValueLineWidth = var1;
   }

   public void setXValuePosition(PieDataSet.ValuePosition var1) {
      this.mXValuePosition = var1;
   }

   public void setYValuePosition(PieDataSet.ValuePosition var1) {
      this.mYValuePosition = var1;
   }

   public static enum ValuePosition {
      INSIDE_SLICE,
      OUTSIDE_SLICE;
   }
}
