package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.PieDataSet;

public interface IPieDataSet extends IDataSet {
   float getSelectionShift();

   float getSliceSpace();

   int getValueLineColor();

   float getValueLinePart1Length();

   float getValueLinePart1OffsetPercentage();

   float getValueLinePart2Length();

   float getValueLineWidth();

   PieDataSet.ValuePosition getXValuePosition();

   PieDataSet.ValuePosition getYValuePosition();

   boolean isAutomaticallyDisableSliceSpacingEnabled();

   boolean isValueLineVariableLength();
}
