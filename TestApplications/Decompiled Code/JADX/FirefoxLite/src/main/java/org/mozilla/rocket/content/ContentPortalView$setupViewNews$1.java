package org.mozilla.rocket.content;

import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.rocket.content.ContentPortalView.NewsListListener;

/* compiled from: ContentPortalView.kt */
final class ContentPortalView$setupViewNews$1 implements OnClickListener {
    final /* synthetic */ ContentPortalView this$0;

    ContentPortalView$setupViewNews$1(ContentPortalView contentPortalView) {
        this.this$0 = contentPortalView;
    }

    public final void onClick(View view) {
        NewsListListener newsListListener = this.this$0.getNewsListListener();
        if (newsListListener != null) {
            newsListListener.loadMore();
        }
    }
}
