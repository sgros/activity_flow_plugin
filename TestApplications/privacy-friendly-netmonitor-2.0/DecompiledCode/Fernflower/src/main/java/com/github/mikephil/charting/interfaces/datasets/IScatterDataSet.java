package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.renderer.scatter.IShapeRenderer;

public interface IScatterDataSet extends ILineScatterCandleRadarDataSet {
   int getScatterShapeHoleColor();

   float getScatterShapeHoleRadius();

   float getScatterShapeSize();

   IShapeRenderer getShapeRenderer();
}
