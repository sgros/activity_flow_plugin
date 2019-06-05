package org.mozilla.rocket.urlinput;

import android.support.p004v7.util.DiffUtil.ItemCallback;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: QuickSearchDiffCallback.kt */
public final class QuickSearchDiffCallback extends ItemCallback<QuickSearch> {
    public boolean areItemsTheSame(QuickSearch quickSearch, QuickSearch quickSearch2) {
        Intrinsics.checkParameterIsNotNull(quickSearch, "oldItem");
        Intrinsics.checkParameterIsNotNull(quickSearch2, "newItem");
        return Intrinsics.areEqual(quickSearch.getName(), quickSearch2.getName());
    }

    public boolean areContentsTheSame(QuickSearch quickSearch, QuickSearch quickSearch2) {
        Intrinsics.checkParameterIsNotNull(quickSearch, "oldItem");
        Intrinsics.checkParameterIsNotNull(quickSearch2, "newItem");
        return Intrinsics.areEqual(quickSearch, quickSearch2);
    }
}
