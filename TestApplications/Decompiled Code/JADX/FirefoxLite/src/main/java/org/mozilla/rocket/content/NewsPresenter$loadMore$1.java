package org.mozilla.rocket.content;

/* compiled from: NewsPresenter.kt */
final class NewsPresenter$loadMore$1 implements Runnable {
    final /* synthetic */ NewsPresenter this$0;

    NewsPresenter$loadMore$1(NewsPresenter newsPresenter) {
        this.this$0 = newsPresenter;
    }

    public final void run() {
        this.this$0.isLoading = false;
    }
}
