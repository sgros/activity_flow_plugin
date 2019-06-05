package com.airbnb.lottie;

import android.support.v4.util.ArraySet;
import android.support.v4.util.Pair;
import com.airbnb.lottie.utils.MeanCalculator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PerformanceTracker {
   private boolean enabled = false;
   private final Comparator floatComparator = new Comparator() {
      public int compare(Pair var1, Pair var2) {
         float var3 = (Float)var1.second;
         float var4 = (Float)var2.second;
         if (var4 > var3) {
            return 1;
         } else {
            return var3 > var4 ? -1 : 0;
         }
      }
   };
   private final Set frameListeners = new ArraySet();
   private final Map layerRenderTimes = new HashMap();

   public void recordRenderTime(String var1, float var2) {
      if (this.enabled) {
         MeanCalculator var3 = (MeanCalculator)this.layerRenderTimes.get(var1);
         MeanCalculator var4 = var3;
         if (var3 == null) {
            var4 = new MeanCalculator();
            this.layerRenderTimes.put(var1, var4);
         }

         var4.add(var2);
         if (var1.equals("__container")) {
            Iterator var5 = this.frameListeners.iterator();

            while(var5.hasNext()) {
               ((PerformanceTracker.FrameListener)var5.next()).onFrameRendered(var2);
            }
         }

      }
   }

   void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public interface FrameListener {
      void onFrameRendered(float var1);
   }
}
