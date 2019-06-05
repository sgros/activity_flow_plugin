package com.github.mikephil.charting.utils;

import com.github.mikephil.charting.data.Entry;
import java.util.Comparator;

public class EntryXComparator implements Comparator {
   public int compare(Entry var1, Entry var2) {
      float var3 = var1.getX() - var2.getX();
      if (var3 == 0.0F) {
         return 0;
      } else {
         return var3 > 0.0F ? 1 : -1;
      }
   }
}
