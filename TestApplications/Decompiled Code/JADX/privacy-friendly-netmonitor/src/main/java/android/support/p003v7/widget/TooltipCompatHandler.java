package android.support.p003v7.widget;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p000v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnHoverListener;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityManager;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v7.widget.TooltipCompatHandler */
class TooltipCompatHandler implements OnLongClickListener, OnHoverListener, OnAttachStateChangeListener {
    private static final long HOVER_HIDE_TIMEOUT_MS = 15000;
    private static final long HOVER_HIDE_TIMEOUT_SHORT_MS = 3000;
    private static final long LONG_CLICK_HIDE_TIMEOUT_MS = 2500;
    private static final String TAG = "TooltipCompatHandler";
    private static TooltipCompatHandler sActiveHandler;
    private final View mAnchor;
    private int mAnchorX;
    private int mAnchorY;
    private boolean mFromTouch;
    private final Runnable mHideRunnable = new C03872();
    private TooltipPopup mPopup;
    private final Runnable mShowRunnable = new C03861();
    private final CharSequence mTooltipText;

    /* renamed from: android.support.v7.widget.TooltipCompatHandler$1 */
    class C03861 implements Runnable {
        C03861() {
        }

        public void run() {
            TooltipCompatHandler.this.show(false);
        }
    }

    /* renamed from: android.support.v7.widget.TooltipCompatHandler$2 */
    class C03872 implements Runnable {
        C03872() {
        }

        public void run() {
            TooltipCompatHandler.this.hide();
        }
    }

    public void onViewAttachedToWindow(View view) {
    }

    public static void setTooltipText(View view, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            if (sActiveHandler != null && sActiveHandler.mAnchor == view) {
                sActiveHandler.hide();
            }
            view.setOnLongClickListener(null);
            view.setLongClickable(false);
            view.setOnHoverListener(null);
            return;
        }
        TooltipCompatHandler tooltipCompatHandler = new TooltipCompatHandler(view, charSequence);
    }

    private TooltipCompatHandler(View view, CharSequence charSequence) {
        this.mAnchor = view;
        this.mTooltipText = charSequence;
        this.mAnchor.setOnLongClickListener(this);
        this.mAnchor.setOnHoverListener(this);
    }

    public boolean onLongClick(View view) {
        this.mAnchorX = view.getWidth() / 2;
        this.mAnchorY = view.getHeight() / 2;
        show(true);
        return true;
    }

    public boolean onHover(View view, MotionEvent motionEvent) {
        if (this.mPopup != null && this.mFromTouch) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.mAnchor.getContext().getSystemService("accessibility");
        if (accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action != 7) {
            if (action == 10) {
                hide();
            }
        } else if (this.mAnchor.isEnabled() && this.mPopup == null) {
            this.mAnchorX = (int) motionEvent.getX();
            this.mAnchorY = (int) motionEvent.getY();
            this.mAnchor.removeCallbacks(this.mShowRunnable);
            this.mAnchor.postDelayed(this.mShowRunnable, (long) ViewConfiguration.getLongPressTimeout());
        }
        return false;
    }

    public void onViewDetachedFromWindow(View view) {
        hide();
    }

    private void show(boolean z) {
        if (ViewCompat.isAttachedToWindow(this.mAnchor)) {
            long j;
            if (sActiveHandler != null) {
                sActiveHandler.hide();
            }
            sActiveHandler = this;
            this.mFromTouch = z;
            this.mPopup = new TooltipPopup(this.mAnchor.getContext());
            this.mPopup.show(this.mAnchor, this.mAnchorX, this.mAnchorY, this.mFromTouch, this.mTooltipText);
            this.mAnchor.addOnAttachStateChangeListener(this);
            if (this.mFromTouch) {
                j = LONG_CLICK_HIDE_TIMEOUT_MS;
            } else if ((ViewCompat.getWindowSystemUiVisibility(this.mAnchor) & 1) == 1) {
                j = HOVER_HIDE_TIMEOUT_SHORT_MS - ((long) ViewConfiguration.getLongPressTimeout());
            } else {
                j = HOVER_HIDE_TIMEOUT_MS - ((long) ViewConfiguration.getLongPressTimeout());
            }
            this.mAnchor.removeCallbacks(this.mHideRunnable);
            this.mAnchor.postDelayed(this.mHideRunnable, j);
        }
    }

    private void hide() {
        if (sActiveHandler == this) {
            sActiveHandler = null;
            if (this.mPopup != null) {
                this.mPopup.hide();
                this.mPopup = null;
                this.mAnchor.removeOnAttachStateChangeListener(this);
            } else {
                Log.e(TAG, "sActiveHandler.mPopup == null");
            }
        }
        this.mAnchor.removeCallbacks(this.mShowRunnable);
        this.mAnchor.removeCallbacks(this.mHideRunnable);
    }
}
