package org.mozilla.rocket.urlinput;

import android.support.v7.util.DiffUtil;
import kotlin.jvm.internal.Intrinsics;

public final class QuickSearchDiffCallback extends DiffUtil.ItemCallback {
   public boolean areContentsTheSame(QuickSearch var1, QuickSearch var2) {
      Intrinsics.checkParameterIsNotNull(var1, "oldItem");
      Intrinsics.checkParameterIsNotNull(var2, "newItem");
      return Intrinsics.areEqual(var1, var2);
   }

   public boolean areItemsTheSame(QuickSearch var1, QuickSearch var2) {
      Intrinsics.checkParameterIsNotNull(var1, "oldItem");
      Intrinsics.checkParameterIsNotNull(var2, "newItem");
      return Intrinsics.areEqual(var1.getName(), var2.getName());
   }
}
