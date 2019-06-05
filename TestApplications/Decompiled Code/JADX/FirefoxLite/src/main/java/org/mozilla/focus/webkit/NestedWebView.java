package org.mozilla.focus.webkit;

import android.content.Context;
import android.support.p001v4.view.MotionEventCompat;
import android.support.p001v4.view.NestedScrollingChild;
import android.support.p001v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class NestedWebView extends WebView implements NestedScrollingChild {
    private NestedScrollingChildHelper mChildHelper = new NestedScrollingChildHelper(this);
    private int mLastY;
    private int mNestedOffsetY;
    private final int[] mScrollConsumed = new int[2];
    private final int[] mScrollOffset = new int[2];

    public NestedWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setNestedScrollingEnabled(true);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        motionEvent = MotionEvent.obtain(motionEvent);
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        boolean z = false;
        if (actionMasked == 0) {
            this.mNestedOffsetY = 0;
        }
        if (motionEvent.getPointerCount() > 1) {
            return super.onTouchEvent(motionEvent);
        }
        int y = (int) motionEvent.getY();
        motionEvent.offsetLocation(0.0f, (float) this.mNestedOffsetY);
        switch (actionMasked) {
            case 0:
                z = super.onTouchEvent(motionEvent);
                this.mLastY = y;
                startNestedScroll(2);
                break;
            case 1:
            case 3:
                z = super.onTouchEvent(motionEvent);
                stopNestedScroll();
                break;
            case 2:
                actionMasked = this.mLastY - y;
                if (dispatchNestedPreScroll(0, actionMasked, this.mScrollConsumed, this.mScrollOffset)) {
                    actionMasked -= this.mScrollConsumed[1];
                    this.mLastY = y - this.mScrollOffset[1];
                    motionEvent.offsetLocation(0.0f, (float) (-this.mScrollOffset[1]));
                    this.mNestedOffsetY += this.mScrollOffset[1];
                }
                int i = actionMasked;
                z = super.onTouchEvent(motionEvent);
                if (dispatchNestedScroll(0, this.mScrollOffset[1], 0, i, this.mScrollOffset)) {
                    motionEvent.offsetLocation(0.0f, (float) this.mScrollOffset[1]);
                    this.mNestedOffsetY += this.mScrollOffset[1];
                    this.mLastY -= this.mScrollOffset[1];
                    break;
                }
                break;
        }
        return z;
    }

    public void setNestedScrollingEnabled(boolean z) {
        this.mChildHelper.setNestedScrollingEnabled(z);
    }

    public boolean isNestedScrollingEnabled() {
        return this.mChildHelper.isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int i) {
        return this.mChildHelper.startNestedScroll(i);
    }

    public void stopNestedScroll() {
        this.mChildHelper.stopNestedScroll();
    }

    public boolean hasNestedScrollingParent() {
        return this.mChildHelper.hasNestedScrollingParent();
    }

    public boolean dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr) {
        return this.mChildHelper.dispatchNestedScroll(i, i2, i3, i4, iArr);
    }

    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2) {
        return this.mChildHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2);
    }

    public boolean dispatchNestedFling(float f, float f2, boolean z) {
        return this.mChildHelper.dispatchNestedFling(f, f2, z);
    }

    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.mChildHelper.dispatchNestedPreFling(f, f2);
    }
}
