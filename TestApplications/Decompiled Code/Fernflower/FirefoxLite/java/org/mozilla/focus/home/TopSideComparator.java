package org.mozilla.focus.home;

import java.io.Serializable;
import java.util.Comparator;
import org.mozilla.focus.history.model.Site;

public class TopSideComparator implements Serializable, Comparator {
   public int compare(Site var1, Site var2) {
      long var4;
      int var3 = (var4 = var1.getViewCount() - var2.getViewCount()) == 0L ? 0 : (var4 < 0L ? -1 : 1);
      if (var3 > 0) {
         return -1;
      } else if (var3 < 0) {
         return 1;
      } else {
         long var5;
         var3 = (var5 = var1.getLastViewTimestamp() - var2.getLastViewTimestamp()) == 0L ? 0 : (var5 < 0L ? -1 : 1);
         if (var3 > 0) {
            return -1;
         } else {
            return var3 < 0 ? 1 : 0;
         }
      }
   }
}
