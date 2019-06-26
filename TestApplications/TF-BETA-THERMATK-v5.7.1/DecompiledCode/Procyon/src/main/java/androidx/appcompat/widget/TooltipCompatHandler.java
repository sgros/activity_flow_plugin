// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.widget;

import androidx.core.view.ViewCompat;
import android.view.accessibility.AccessibilityManager;
import android.util.Log;
import android.view.MotionEvent;
import android.text.TextUtils;
import androidx.core.view.ViewConfigurationCompat;
import android.view.ViewConfiguration;
import android.view.View;
import android.view.View$OnAttachStateChangeListener;
import android.view.View$OnHoverListener;
import android.view.View$OnLongClickListener;

class TooltipCompatHandler implements View$OnLongClickListener, View$OnHoverListener, View$OnAttachStateChangeListener
{
    private static TooltipCompatHandler sActiveHandler;
    private static TooltipCompatHandler sPendingHandler;
    private final View mAnchor;
    private int mAnchorX;
    private int mAnchorY;
    private boolean mFromTouch;
    private final Runnable mHideRunnable;
    private final int mHoverSlop;
    private TooltipPopup mPopup;
    private final Runnable mShowRunnable;
    private final CharSequence mTooltipText;
    
    private TooltipCompatHandler(final View mAnchor, final CharSequence mTooltipText) {
        this.mShowRunnable = new Runnable() {
            @Override
            public void run() {
                TooltipCompatHandler.this.show(false);
            }
        };
        this.mHideRunnable = new Runnable() {
            @Override
            public void run() {
                TooltipCompatHandler.this.hide();
            }
        };
        this.mAnchor = mAnchor;
        this.mTooltipText = mTooltipText;
        this.mHoverSlop = ViewConfigurationCompat.getScaledHoverSlop(ViewConfiguration.get(this.mAnchor.getContext()));
        this.clearAnchorPos();
        this.mAnchor.setOnLongClickListener((View$OnLongClickListener)this);
        this.mAnchor.setOnHoverListener((View$OnHoverListener)this);
    }
    
    private void cancelPendingShow() {
        this.mAnchor.removeCallbacks(this.mShowRunnable);
    }
    
    private void clearAnchorPos() {
        this.mAnchorX = Integer.MAX_VALUE;
        this.mAnchorY = Integer.MAX_VALUE;
    }
    
    private void scheduleShow() {
        this.mAnchor.postDelayed(this.mShowRunnable, (long)ViewConfiguration.getLongPressTimeout());
    }
    
    private static void setPendingHandler(TooltipCompatHandler sPendingHandler) {
        final TooltipCompatHandler sPendingHandler2 = TooltipCompatHandler.sPendingHandler;
        if (sPendingHandler2 != null) {
            sPendingHandler2.cancelPendingShow();
        }
        TooltipCompatHandler.sPendingHandler = sPendingHandler;
        sPendingHandler = TooltipCompatHandler.sPendingHandler;
        if (sPendingHandler != null) {
            sPendingHandler.scheduleShow();
        }
    }
    
    public static void setTooltipText(final View view, final CharSequence charSequence) {
        final TooltipCompatHandler sPendingHandler = TooltipCompatHandler.sPendingHandler;
        if (sPendingHandler != null && sPendingHandler.mAnchor == view) {
            setPendingHandler(null);
        }
        if (TextUtils.isEmpty(charSequence)) {
            final TooltipCompatHandler sActiveHandler = TooltipCompatHandler.sActiveHandler;
            if (sActiveHandler != null && sActiveHandler.mAnchor == view) {
                sActiveHandler.hide();
            }
            view.setOnLongClickListener((View$OnLongClickListener)null);
            view.setLongClickable(false);
            view.setOnHoverListener((View$OnHoverListener)null);
        }
        else {
            new TooltipCompatHandler(view, charSequence);
        }
    }
    
    private boolean updateAnchorPos(final MotionEvent motionEvent) {
        final int mAnchorX = (int)motionEvent.getX();
        final int mAnchorY = (int)motionEvent.getY();
        if (Math.abs(mAnchorX - this.mAnchorX) <= this.mHoverSlop && Math.abs(mAnchorY - this.mAnchorY) <= this.mHoverSlop) {
            return false;
        }
        this.mAnchorX = mAnchorX;
        this.mAnchorY = mAnchorY;
        return true;
    }
    
    void hide() {
        if (TooltipCompatHandler.sActiveHandler == this) {
            TooltipCompatHandler.sActiveHandler = null;
            final TooltipPopup mPopup = this.mPopup;
            if (mPopup != null) {
                mPopup.hide();
                this.mPopup = null;
                this.clearAnchorPos();
                this.mAnchor.removeOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
            }
            else {
                Log.e("TooltipCompatHandler", "sActiveHandler.mPopup == null");
            }
        }
        if (TooltipCompatHandler.sPendingHandler == this) {
            setPendingHandler(null);
        }
        this.mAnchor.removeCallbacks(this.mHideRunnable);
    }
    
    public boolean onHover(final View view, final MotionEvent motionEvent) {
        if (this.mPopup != null && this.mFromTouch) {
            return false;
        }
        final AccessibilityManager accessibilityManager = (AccessibilityManager)this.mAnchor.getContext().getSystemService("accessibility");
        if (accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled()) {
            return false;
        }
        final int action = motionEvent.getAction();
        if (action != 7) {
            if (action == 10) {
                this.clearAnchorPos();
                this.hide();
            }
        }
        else if (this.mAnchor.isEnabled() && this.mPopup == null && this.updateAnchorPos(motionEvent)) {
            setPendingHandler(this);
        }
        return false;
    }
    
    public boolean onLongClick(final View view) {
        this.mAnchorX = view.getWidth() / 2;
        this.mAnchorY = view.getHeight() / 2;
        this.show(true);
        return true;
    }
    
    public void onViewAttachedToWindow(final View view) {
    }
    
    public void onViewDetachedFromWindow(final View view) {
        this.hide();
    }
    
    void show(final boolean mFromTouch) {
        if (!ViewCompat.isAttachedToWindow(this.mAnchor)) {
            return;
        }
        setPendingHandler(null);
        final TooltipCompatHandler sActiveHandler = TooltipCompatHandler.sActiveHandler;
        if (sActiveHandler != null) {
            sActiveHandler.hide();
        }
        TooltipCompatHandler.sActiveHandler = this;
        this.mFromTouch = mFromTouch;
        (this.mPopup = new TooltipPopup(this.mAnchor.getContext())).show(this.mAnchor, this.mAnchorX, this.mAnchorY, this.mFromTouch, this.mTooltipText);
        this.mAnchor.addOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
        long n;
        if (this.mFromTouch) {
            n = 2500L;
        }
        else {
            long n2;
            int n3;
            if ((ViewCompat.getWindowSystemUiVisibility(this.mAnchor) & 0x1) == 0x1) {
                n2 = 3000L;
                n3 = ViewConfiguration.getLongPressTimeout();
            }
            else {
                n2 = 15000L;
                n3 = ViewConfiguration.getLongPressTimeout();
            }
            n = n2 - n3;
        }
        this.mAnchor.removeCallbacks(this.mHideRunnable);
        this.mAnchor.postDelayed(this.mHideRunnable, n);
    }
}
