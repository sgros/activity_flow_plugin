// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.urlinput;

import kotlin.jvm.internal.Intrinsics;
import android.support.v7.util.DiffUtil;

public final class QuickSearchDiffCallback extends ItemCallback<QuickSearch>
{
    public boolean areContentsTheSame(final QuickSearch quickSearch, final QuickSearch quickSearch2) {
        Intrinsics.checkParameterIsNotNull(quickSearch, "oldItem");
        Intrinsics.checkParameterIsNotNull(quickSearch2, "newItem");
        return Intrinsics.areEqual(quickSearch, quickSearch2);
    }
    
    public boolean areItemsTheSame(final QuickSearch quickSearch, final QuickSearch quickSearch2) {
        Intrinsics.checkParameterIsNotNull(quickSearch, "oldItem");
        Intrinsics.checkParameterIsNotNull(quickSearch2, "newItem");
        return Intrinsics.areEqual(quickSearch.getName(), quickSearch2.getName());
    }
}
