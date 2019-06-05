package android.support.p004v7.widget;

import android.support.p004v7.widget.RecyclerView.LayoutManager;
import android.support.p004v7.widget.RecyclerView.OnFlingListener;
import android.support.p004v7.widget.RecyclerView.OnScrollListener;
import android.support.p004v7.widget.RecyclerView.SmoothScroller;
import android.support.p004v7.widget.RecyclerView.SmoothScroller.Action;
import android.support.p004v7.widget.RecyclerView.SmoothScroller.ScrollVectorProvider;
import android.support.p004v7.widget.RecyclerView.State;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/* renamed from: android.support.v7.widget.SnapHelper */
public abstract class SnapHelper extends OnFlingListener {
    private Scroller mGravityScroller;
    RecyclerView mRecyclerView;
    private final OnScrollListener mScrollListener = new C06461();

    /* renamed from: android.support.v7.widget.SnapHelper$1 */
    class C06461 extends OnScrollListener {
        boolean mScrolled = false;

        C06461() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
            if (i == 0 && this.mScrolled) {
                this.mScrolled = false;
                SnapHelper.this.snapToTargetExistingView();
            }
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (i != 0 || i2 != 0) {
                this.mScrolled = true;
            }
        }
    }

    public abstract int[] calculateDistanceToFinalSnap(LayoutManager layoutManager, View view);

    public abstract View findSnapView(LayoutManager layoutManager);

    public abstract int findTargetSnapPosition(LayoutManager layoutManager, int i, int i2);

    public boolean onFling(int i, int i2) {
        LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        boolean z = false;
        if (layoutManager == null || this.mRecyclerView.getAdapter() == null) {
            return false;
        }
        int minFlingVelocity = this.mRecyclerView.getMinFlingVelocity();
        if ((Math.abs(i2) > minFlingVelocity || Math.abs(i) > minFlingVelocity) && snapFromFling(layoutManager, i, i2)) {
            z = true;
        }
        return z;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) throws IllegalStateException {
        if (this.mRecyclerView != recyclerView) {
            if (this.mRecyclerView != null) {
                destroyCallbacks();
            }
            this.mRecyclerView = recyclerView;
            if (this.mRecyclerView != null) {
                setupCallbacks();
                this.mGravityScroller = new Scroller(this.mRecyclerView.getContext(), new DecelerateInterpolator());
                snapToTargetExistingView();
            }
        }
    }

    private void setupCallbacks() throws IllegalStateException {
        if (this.mRecyclerView.getOnFlingListener() == null) {
            this.mRecyclerView.addOnScrollListener(this.mScrollListener);
            this.mRecyclerView.setOnFlingListener(this);
            return;
        }
        throw new IllegalStateException("An instance of OnFlingListener already set.");
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeOnScrollListener(this.mScrollListener);
        this.mRecyclerView.setOnFlingListener(null);
    }

    private boolean snapFromFling(LayoutManager layoutManager, int i, int i2) {
        if (!(layoutManager instanceof ScrollVectorProvider)) {
            return false;
        }
        SmoothScroller createScroller = createScroller(layoutManager);
        if (createScroller == null) {
            return false;
        }
        i = findTargetSnapPosition(layoutManager, i, i2);
        if (i == -1) {
            return false;
        }
        createScroller.setTargetPosition(i);
        layoutManager.startSmoothScroll(createScroller);
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public void snapToTargetExistingView() {
        if (this.mRecyclerView != null) {
            LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
            if (layoutManager != null) {
                View findSnapView = findSnapView(layoutManager);
                if (findSnapView != null) {
                    int[] calculateDistanceToFinalSnap = calculateDistanceToFinalSnap(layoutManager, findSnapView);
                    if (!(calculateDistanceToFinalSnap[0] == 0 && calculateDistanceToFinalSnap[1] == 0)) {
                        this.mRecyclerView.smoothScrollBy(calculateDistanceToFinalSnap[0], calculateDistanceToFinalSnap[1]);
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public SmoothScroller createScroller(LayoutManager layoutManager) {
        return createSnapScroller(layoutManager);
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public LinearSmoothScroller createSnapScroller(LayoutManager layoutManager) {
        if (layoutManager instanceof ScrollVectorProvider) {
            return new LinearSmoothScroller(this.mRecyclerView.getContext()) {
                /* Access modifiers changed, original: protected */
                public void onTargetFound(View view, State state, Action action) {
                    if (SnapHelper.this.mRecyclerView != null) {
                        int[] calculateDistanceToFinalSnap = SnapHelper.this.calculateDistanceToFinalSnap(SnapHelper.this.mRecyclerView.getLayoutManager(), view);
                        int i = calculateDistanceToFinalSnap[0];
                        int i2 = calculateDistanceToFinalSnap[1];
                        int calculateTimeForDeceleration = calculateTimeForDeceleration(Math.max(Math.abs(i), Math.abs(i2)));
                        if (calculateTimeForDeceleration > 0) {
                            action.update(i, i2, calculateTimeForDeceleration, this.mDecelerateInterpolator);
                        }
                    }
                }

                /* Access modifiers changed, original: protected */
                public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return 100.0f / ((float) displayMetrics.densityDpi);
                }
            };
        }
        return null;
    }
}
