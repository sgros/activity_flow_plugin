package com.github.mikephil.charting.data;

import android.util.Log;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CombinedData extends BarLineScatterCandleBubbleData {
   private BarData mBarData;
   private BubbleData mBubbleData;
   private CandleData mCandleData;
   private LineData mLineData;
   private ScatterData mScatterData;

   public void calcMinMax() {
      if (this.mDataSets == null) {
         this.mDataSets = new ArrayList();
      }

      this.mDataSets.clear();
      this.mYMax = -3.4028235E38F;
      this.mYMin = Float.MAX_VALUE;
      this.mXMax = -3.4028235E38F;
      this.mXMin = Float.MAX_VALUE;
      this.mLeftAxisMax = -3.4028235E38F;
      this.mLeftAxisMin = Float.MAX_VALUE;
      this.mRightAxisMax = -3.4028235E38F;
      this.mRightAxisMin = Float.MAX_VALUE;
      Iterator var1 = this.getAllData().iterator();

      while(var1.hasNext()) {
         ChartData var2 = (ChartData)var1.next();
         var2.calcMinMax();
         List var3 = var2.getDataSets();
         this.mDataSets.addAll(var3);
         if (var2.getYMax() > this.mYMax) {
            this.mYMax = var2.getYMax();
         }

         if (var2.getYMin() < this.mYMin) {
            this.mYMin = var2.getYMin();
         }

         if (var2.getXMax() > this.mXMax) {
            this.mXMax = var2.getXMax();
         }

         if (var2.getXMin() < this.mXMin) {
            this.mXMin = var2.getXMin();
         }

         if (var2.mLeftAxisMax > this.mLeftAxisMax) {
            this.mLeftAxisMax = var2.mLeftAxisMax;
         }

         if (var2.mLeftAxisMin < this.mLeftAxisMin) {
            this.mLeftAxisMin = var2.mLeftAxisMin;
         }

         if (var2.mRightAxisMax > this.mRightAxisMax) {
            this.mRightAxisMax = var2.mRightAxisMax;
         }

         if (var2.mRightAxisMin < this.mRightAxisMin) {
            this.mRightAxisMin = var2.mRightAxisMin;
         }
      }

   }

   public List getAllData() {
      ArrayList var1 = new ArrayList();
      if (this.mLineData != null) {
         var1.add(this.mLineData);
      }

      if (this.mBarData != null) {
         var1.add(this.mBarData);
      }

      if (this.mScatterData != null) {
         var1.add(this.mScatterData);
      }

      if (this.mCandleData != null) {
         var1.add(this.mCandleData);
      }

      if (this.mBubbleData != null) {
         var1.add(this.mBubbleData);
      }

      return var1;
   }

   public BarData getBarData() {
      return this.mBarData;
   }

   public BubbleData getBubbleData() {
      return this.mBubbleData;
   }

   public CandleData getCandleData() {
      return this.mCandleData;
   }

   public BarLineScatterCandleBubbleData getDataByIndex(int var1) {
      return (BarLineScatterCandleBubbleData)this.getAllData().get(var1);
   }

   public int getDataIndex(ChartData var1) {
      return this.getAllData().indexOf(var1);
   }

   public IBarLineScatterCandleBubbleDataSet getDataSetByHighlight(Highlight var1) {
      if (var1.getDataIndex() >= this.getAllData().size()) {
         return null;
      } else {
         BarLineScatterCandleBubbleData var2 = this.getDataByIndex(var1.getDataIndex());
         return var1.getDataSetIndex() >= var2.getDataSetCount() ? null : (IBarLineScatterCandleBubbleDataSet)var2.getDataSets().get(var1.getDataSetIndex());
      }
   }

   public Entry getEntryForHighlight(Highlight var1) {
      if (var1.getDataIndex() >= this.getAllData().size()) {
         return null;
      } else {
         BarLineScatterCandleBubbleData var2 = this.getDataByIndex(var1.getDataIndex());
         if (var1.getDataSetIndex() >= var2.getDataSetCount()) {
            return null;
         } else {
            Iterator var4 = var2.getDataSetByIndex(var1.getDataSetIndex()).getEntriesForXValue(var1.getX()).iterator();

            Entry var3;
            do {
               if (!var4.hasNext()) {
                  return null;
               }

               var3 = (Entry)var4.next();
            } while(var3.getY() != var1.getY() && !Float.isNaN(var1.getY()));

            return var3;
         }
      }
   }

   public LineData getLineData() {
      return this.mLineData;
   }

   public ScatterData getScatterData() {
      return this.mScatterData;
   }

   public void notifyDataChanged() {
      if (this.mLineData != null) {
         this.mLineData.notifyDataChanged();
      }

      if (this.mBarData != null) {
         this.mBarData.notifyDataChanged();
      }

      if (this.mCandleData != null) {
         this.mCandleData.notifyDataChanged();
      }

      if (this.mScatterData != null) {
         this.mScatterData.notifyDataChanged();
      }

      if (this.mBubbleData != null) {
         this.mBubbleData.notifyDataChanged();
      }

      this.calcMinMax();
   }

   @Deprecated
   public boolean removeDataSet(int var1) {
      Log.e("MPAndroidChart", "removeDataSet(int index) not supported for CombinedData");
      return false;
   }

   public boolean removeDataSet(IBarLineScatterCandleBubbleDataSet var1) {
      Iterator var2 = this.getAllData().iterator();
      boolean var3 = false;

      while(var2.hasNext()) {
         boolean var4 = ((ChartData)var2.next()).removeDataSet(var1);
         var3 = var4;
         if (var4) {
            var3 = var4;
            break;
         }
      }

      return var3;
   }

   @Deprecated
   public boolean removeEntry(float var1, int var2) {
      Log.e("MPAndroidChart", "removeEntry(...) not supported for CombinedData");
      return false;
   }

   @Deprecated
   public boolean removeEntry(Entry var1, int var2) {
      Log.e("MPAndroidChart", "removeEntry(...) not supported for CombinedData");
      return false;
   }

   public void setData(BarData var1) {
      this.mBarData = var1;
      this.notifyDataChanged();
   }

   public void setData(BubbleData var1) {
      this.mBubbleData = var1;
      this.notifyDataChanged();
   }

   public void setData(CandleData var1) {
      this.mCandleData = var1;
      this.notifyDataChanged();
   }

   public void setData(LineData var1) {
      this.mLineData = var1;
      this.notifyDataChanged();
   }

   public void setData(ScatterData var1) {
      this.mScatterData = var1;
      this.notifyDataChanged();
   }
}
