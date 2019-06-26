package com.google.android.exoplayer2.source.dash.manifest;

import java.util.Collections;
import java.util.List;

public class AdaptationSet {
   public final List accessibilityDescriptors;
   public final int id;
   public final List representations;
   public final List supplementalProperties;
   public final int type;

   public AdaptationSet(int var1, int var2, List var3, List var4, List var5) {
      this.id = var1;
      this.type = var2;
      this.representations = Collections.unmodifiableList(var3);
      if (var4 == null) {
         var3 = Collections.emptyList();
      } else {
         var3 = Collections.unmodifiableList(var4);
      }

      this.accessibilityDescriptors = var3;
      if (var5 == null) {
         var3 = Collections.emptyList();
      } else {
         var3 = Collections.unmodifiableList(var5);
      }

      this.supplementalProperties = var3;
   }
}
