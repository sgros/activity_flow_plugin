package com.github.mikephil.charting.listener;

import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;

public interface OnDrawListener {
   void onDrawFinished(DataSet var1);

   void onEntryAdded(Entry var1);

   void onEntryMoved(Entry var1);
}
