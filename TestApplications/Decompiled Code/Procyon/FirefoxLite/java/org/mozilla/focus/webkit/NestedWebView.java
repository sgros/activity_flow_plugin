// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit;

import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingChild;
import android.webkit.WebView;

public class NestedWebView extends WebView implements NestedScrollingChild
{
    private NestedScrollingChildHelper mChildHelper;
    private int mLastY;
    private int mNestedOffsetY;
    private final int[] mScrollConsumed;
    private final int[] mScrollOffset;
    
    public NestedWebView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mChildHelper = new NestedScrollingChildHelper((View)this);
        this.setNestedScrollingEnabled(true);
    }
    
    public boolean dispatchNestedFling(final float n, final float n2, final boolean b) {
        return this.mChildHelper.dispatchNestedFling(n, n2, b);
    }
    
    public boolean dispatchNestedPreFling(final float n, final float n2) {
        return this.mChildHelper.dispatchNestedPreFling(n, n2);
    }
    
    public boolean dispatchNestedPreScroll(final int n, final int n2, final int[] array, final int[] array2) {
        return this.mChildHelper.dispatchNestedPreScroll(n, n2, array, array2);
    }
    
    public boolean dispatchNestedScroll(final int n, final int n2, final int n3, final int n4, final int[] array) {
        return this.mChildHelper.dispatchNestedScroll(n, n2, n3, n4, array);
    }
    
    public boolean hasNestedScrollingParent() {
        return this.mChildHelper.hasNestedScrollingParent();
    }
    
    public boolean isNestedScrollingEnabled() {
        return this.mChildHelper.isNestedScrollingEnabled();
    }
    
    public boolean onTouchEvent(MotionEvent obtain) {
        obtain = MotionEvent.obtain(obtain);
        final int actionMasked = MotionEventCompat.getActionMasked(obtain);
        boolean b = false;
        if (actionMasked == 0) {
            this.mNestedOffsetY = 0;
        }
        if (obtain.getPointerCount() > 1) {
            return super.onTouchEvent(obtain);
        }
        final int mLastY = (int)obtain.getY();
        obtain.offsetLocation(0.0f, (float)this.mNestedOffsetY);
        switch (actionMasked) {
            case 2: {
                int n2;
                final int n = n2 = this.mLastY - mLastY;
                if (this.dispatchNestedPreScroll(0, n, this.mScrollConsumed, this.mScrollOffset)) {
                    n2 = n - this.mScrollConsumed[1];
                    this.mLastY = mLastY - this.mScrollOffset[1];
                    obtain.offsetLocation(0.0f, (float)(-this.mScrollOffset[1]));
                    this.mNestedOffsetY += this.mScrollOffset[1];
                }
                b = super.onTouchEvent(obtain);
                if (this.dispatchNestedScroll(0, this.mScrollOffset[1], 0, n2, this.mScrollOffset)) {
                    obtain.offsetLocation(0.0f, (float)this.mScrollOffset[1]);
                    this.mNestedOffsetY += this.mScrollOffset[1];
                    this.mLastY -= this.mScrollOffset[1];
                    b = b;
                    break;
                }
                break;
            }
            case 1:
            case 3: {
                b = super.onTouchEvent(obtain);
                this.stopNestedScroll();
                break;
            }
            case 0: {
                b = super.onTouchEvent(obtain);
                this.mLastY = mLastY;
                this.startNestedScroll(2);
                break;
            }
        }
        return b;
    }
    
    public void setNestedScrollingEnabled(final boolean nestedScrollingEnabled) {
        this.mChildHelper.setNestedScrollingEnabled(nestedScrollingEnabled);
    }
    
    public boolean startNestedScroll(final int n) {
        return this.mChildHelper.startNestedScroll(n);
    }
    
    public void stopNestedScroll() {
        this.mChildHelper.stopNestedScroll();
    }
}
