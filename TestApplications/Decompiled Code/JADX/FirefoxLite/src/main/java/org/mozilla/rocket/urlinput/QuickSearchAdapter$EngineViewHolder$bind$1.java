package org.mozilla.rocket.urlinput;

import android.view.View;
import android.view.View.OnClickListener;
import kotlin.jvm.functions.Function1;

/* compiled from: QuickSearchAdapter.kt */
final class QuickSearchAdapter$EngineViewHolder$bind$1 implements OnClickListener {
    final /* synthetic */ Function1 $clickListener;
    final /* synthetic */ QuickSearch $item;

    QuickSearchAdapter$EngineViewHolder$bind$1(Function1 function1, QuickSearch quickSearch) {
        this.$clickListener = function1;
        this.$item = quickSearch;
    }

    public final void onClick(View view) {
        this.$clickListener.invoke(this.$item);
    }
}
