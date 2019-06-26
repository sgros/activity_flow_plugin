// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.view.ViewGroup;

public abstract class BaseCell extends ViewGroup
{
    private boolean checkingForLongPress;
    private CheckForLongPress pendingCheckForLongPress;
    private CheckForTap pendingCheckForTap;
    private int pressCount;
    
    public BaseCell(final Context context) {
        super(context);
        this.checkingForLongPress = false;
        this.pendingCheckForLongPress = null;
        this.pressCount = 0;
        this.pendingCheckForTap = null;
        this.setWillNotDraw(false);
        this.setFocusable(true);
    }
    
    public static void setDrawableBounds(final Drawable drawable, final float n, final float n2) {
        setDrawableBounds(drawable, (int)n, (int)n2, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }
    
    public static void setDrawableBounds(final Drawable drawable, final int n, final int n2) {
        setDrawableBounds(drawable, n, n2, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }
    
    public static void setDrawableBounds(final Drawable drawable, final int n, final int n2, final int n3, final int n4) {
        if (drawable != null) {
            drawable.setBounds(n, n2, n3 + n, n4 + n2);
        }
    }
    
    protected void cancelCheckLongPress() {
        this.checkingForLongPress = false;
        final CheckForLongPress pendingCheckForLongPress = this.pendingCheckForLongPress;
        if (pendingCheckForLongPress != null) {
            this.removeCallbacks((Runnable)pendingCheckForLongPress);
        }
        final CheckForTap pendingCheckForTap = this.pendingCheckForTap;
        if (pendingCheckForTap != null) {
            this.removeCallbacks((Runnable)pendingCheckForTap);
        }
    }
    
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    protected void onLongPress() {
    }
    
    protected void startCheckLongPress() {
        if (this.checkingForLongPress) {
            return;
        }
        this.checkingForLongPress = true;
        if (this.pendingCheckForTap == null) {
            this.pendingCheckForTap = new CheckForTap();
        }
        this.postDelayed((Runnable)this.pendingCheckForTap, (long)ViewConfiguration.getTapTimeout());
    }
    
    class CheckForLongPress implements Runnable
    {
        public int currentPressCount;
        
        @Override
        public void run() {
            if (BaseCell.this.checkingForLongPress && BaseCell.this.getParent() != null && this.currentPressCount == BaseCell.this.pressCount) {
                BaseCell.this.checkingForLongPress = false;
                BaseCell.this.performHapticFeedback(0);
                BaseCell.this.onLongPress();
                final MotionEvent obtain = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
                BaseCell.this.onTouchEvent(obtain);
                obtain.recycle();
            }
        }
    }
    
    private final class CheckForTap implements Runnable
    {
        @Override
        public void run() {
            if (BaseCell.this.pendingCheckForLongPress == null) {
                final BaseCell this$0 = BaseCell.this;
                this$0.pendingCheckForLongPress = this$0.new CheckForLongPress();
            }
            BaseCell.this.pendingCheckForLongPress.currentPressCount = ++BaseCell.this.pressCount;
            final BaseCell this$2 = BaseCell.this;
            this$2.postDelayed((Runnable)this$2.pendingCheckForLongPress, (long)(ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout()));
        }
    }
}
