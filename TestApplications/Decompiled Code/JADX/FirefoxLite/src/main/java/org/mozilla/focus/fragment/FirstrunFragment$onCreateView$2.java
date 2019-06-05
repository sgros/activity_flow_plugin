package org.mozilla.focus.fragment;

import android.graphics.drawable.Drawable;
import android.support.p001v4.view.ViewPager.OnPageChangeListener;
import org.mozilla.focus.C0427R;

/* compiled from: FirstrunFragment.kt */
public final class FirstrunFragment$onCreateView$2 implements OnPageChangeListener {
    final /* synthetic */ FirstrunFragment this$0;

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    FirstrunFragment$onCreateView$2(FirstrunFragment firstrunFragment) {
        this.this$0 = firstrunFragment;
    }

    public void onPageSelected(int i) {
        Drawable drawable = FirstrunFragment.access$getBgDrawables$p(this.this$0)[i % FirstrunFragment.access$getBgDrawables$p(this.this$0).length];
        if (i % 2 == 0) {
            FirstrunFragment.access$getBgTransitionDrawable$p(this.this$0).setDrawableByLayerId(C0427R.C0426id.first_run_bg_even, drawable);
            FirstrunFragment.access$getBgTransitionDrawable$p(this.this$0).reverseTransition(400);
            return;
        }
        FirstrunFragment.access$getBgTransitionDrawable$p(this.this$0).setDrawableByLayerId(C0427R.C0426id.first_run_bg_odd, drawable);
        FirstrunFragment.access$getBgTransitionDrawable$p(this.this$0).startTransition(400);
    }
}
