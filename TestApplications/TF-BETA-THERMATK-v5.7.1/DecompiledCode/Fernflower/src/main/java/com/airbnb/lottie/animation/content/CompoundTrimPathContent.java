package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import com.airbnb.lottie.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class CompoundTrimPathContent {
   private List contents = new ArrayList();

   void addTrimPath(TrimPathContent var1) {
      this.contents.add(var1);
   }

   public void apply(Path var1) {
      for(int var2 = this.contents.size() - 1; var2 >= 0; --var2) {
         Utils.applyTrimPathIfNeeded(var1, (TrimPathContent)this.contents.get(var2));
      }

   }
}
