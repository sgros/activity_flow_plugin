package org.mozilla.rocket.content;

import android.support.p004v7.widget.LinearLayoutManager;
import android.support.p004v7.widget.RecyclerView;
import android.support.p004v7.widget.RecyclerView.OnScrollListener;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.rocket.content.ContentPortalView.NewsListListener;

/* compiled from: ContentPortalView.kt */
public final class ContentPortalView$setupViewNews$$inlined$let$lambda$1 extends OnScrollListener {
    final /* synthetic */ LinearLayoutManager $it;
    final /* synthetic */ ContentPortalView this$0;

    ContentPortalView$setupViewNews$$inlined$let$lambda$1(LinearLayoutManager linearLayoutManager, ContentPortalView contentPortalView) {
        this.$it = linearLayoutManager;
        this.this$0 = contentPortalView;
    }

    public void onScrolled(RecyclerView recyclerView, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(recyclerView, "recyclerView");
        super.onScrolled(recyclerView, i, i2);
        if ((this.$it.getChildCount() + this.$it.findLastVisibleItemPosition()) + 10 >= this.$it.getItemCount()) {
            NewsListListener newsListListener = this.this$0.getNewsListListener();
            if (newsListListener != null) {
                newsListListener.loadMore();
            }
        }
    }
}
