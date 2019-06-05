package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.support.design.C0028R;
import android.support.design.animation.AnimationUtils;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.snackbar.ContentViewCallback;
import android.support.design.widget.SwipeDismissBehavior.OnDismissListener;
import android.support.p001v4.view.AccessibilityDelegateCompat;
import android.support.p001v4.view.OnApplyWindowInsetsListener;
import android.support.p001v4.view.ViewCompat;
import android.support.p001v4.view.WindowInsetsCompat;
import android.support.p001v4.view.accessibility.AccessibilityManagerCompat;
import android.support.p001v4.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener;
import android.support.p001v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTransientBottomBar<B extends BaseTransientBottomBar<B>> {
    private static final int[] SNACKBAR_STYLE_ATTR = new int[]{C0028R.attr.snackbarStyle};
    private static final boolean USE_OFFSET_API;
    static final Handler handler = new Handler(Looper.getMainLooper(), new C00531());
    private final AccessibilityManager accessibilityManager;
    private Behavior behavior;
    private List<BaseCallback<B>> callbacks;
    private final ContentViewCallback contentViewCallback;
    private final Context context;
    private int duration;
    final Callback managerCallback = new C00604();
    private final ViewGroup targetParent;
    protected final SnackbarBaseLayout view;

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$11 */
    class C005211 implements AnimatorUpdateListener {
        private int previousAnimatedIntValue = 0;

        C005211() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            if (BaseTransientBottomBar.USE_OFFSET_API) {
                ViewCompat.offsetTopAndBottom(BaseTransientBottomBar.this.view, intValue - this.previousAnimatedIntValue);
            } else {
                BaseTransientBottomBar.this.view.setTranslationY((float) intValue);
            }
            this.previousAnimatedIntValue = intValue;
        }
    }

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$1 */
    static class C00531 implements Callback {
        C00531() {
        }

        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    ((BaseTransientBottomBar) message.obj).showView();
                    return true;
                case 1:
                    ((BaseTransientBottomBar) message.obj).hideView(message.arg1);
                    return true;
                default:
                    return false;
            }
        }
    }

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$8 */
    class C00558 extends AnimatorListenerAdapter {
        C00558() {
        }

        public void onAnimationStart(Animator animator) {
            BaseTransientBottomBar.this.contentViewCallback.animateContentIn(70, 180);
        }

        public void onAnimationEnd(Animator animator) {
            BaseTransientBottomBar.this.onViewShown();
        }
    }

    public static abstract class BaseCallback<B> {
        public void onDismissed(B b, int i) {
        }

        public void onShown(B b) {
        }
    }

    public static class BehaviorDelegate {
        private Callback managerCallback;

        public BehaviorDelegate(SwipeDismissBehavior<?> swipeDismissBehavior) {
            swipeDismissBehavior.setStartAlphaSwipeDistance(0.1f);
            swipeDismissBehavior.setEndAlphaSwipeDistance(0.6f);
            swipeDismissBehavior.setSwipeDirection(0);
        }

        public void setBaseTransientBottomBar(BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.managerCallback = baseTransientBottomBar.managerCallback;
        }

        public boolean canSwipeDismissView(View view) {
            return view instanceof SnackbarBaseLayout;
        }

        public void onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 3) {
                switch (actionMasked) {
                    case 0:
                        if (coordinatorLayout.isPointInChildBounds(view, (int) motionEvent.getX(), (int) motionEvent.getY())) {
                            SnackbarManager.getInstance().pauseTimeout(this.managerCallback);
                            return;
                        }
                        return;
                    case 1:
                        break;
                    default:
                        return;
                }
            }
            SnackbarManager.getInstance().restoreTimeoutIfPaused(this.managerCallback);
        }
    }

    protected interface OnAttachStateChangeListener {
        void onViewAttachedToWindow(View view);

        void onViewDetachedFromWindow(View view);
    }

    protected interface OnLayoutChangeListener {
        void onLayoutChange(View view, int i, int i2, int i3, int i4);
    }

    protected static class SnackbarBaseLayout extends FrameLayout {
        private final AccessibilityManager accessibilityManager;
        private OnAttachStateChangeListener onAttachStateChangeListener;
        private OnLayoutChangeListener onLayoutChangeListener;
        private final TouchExplorationStateChangeListener touchExplorationStateChangeListener;

        /* renamed from: android.support.design.widget.BaseTransientBottomBar$SnackbarBaseLayout$1 */
        class C00571 implements TouchExplorationStateChangeListener {
            C00571() {
            }

            public void onTouchExplorationStateChanged(boolean z) {
                SnackbarBaseLayout.this.setClickableOrFocusableBasedOnAccessibility(z);
            }
        }

        protected SnackbarBaseLayout(Context context) {
            this(context, null);
        }

        protected SnackbarBaseLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0028R.styleable.SnackbarLayout);
            if (obtainStyledAttributes.hasValue(C0028R.styleable.SnackbarLayout_elevation)) {
                ViewCompat.setElevation(this, (float) obtainStyledAttributes.getDimensionPixelSize(C0028R.styleable.SnackbarLayout_elevation, 0));
            }
            obtainStyledAttributes.recycle();
            this.accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
            this.touchExplorationStateChangeListener = new C00571();
            AccessibilityManagerCompat.addTouchExplorationStateChangeListener(this.accessibilityManager, this.touchExplorationStateChangeListener);
            setClickableOrFocusableBasedOnAccessibility(this.accessibilityManager.isTouchExplorationEnabled());
        }

        private void setClickableOrFocusableBasedOnAccessibility(boolean z) {
            setClickable(z ^ 1);
            setFocusable(z);
        }

        /* Access modifiers changed, original: protected */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (this.onLayoutChangeListener != null) {
                this.onLayoutChangeListener.onLayoutChange(this, i, i2, i3, i4);
            }
        }

        /* Access modifiers changed, original: protected */
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (this.onAttachStateChangeListener != null) {
                this.onAttachStateChangeListener.onViewAttachedToWindow(this);
            }
            ViewCompat.requestApplyInsets(this);
        }

        /* Access modifiers changed, original: protected */
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (this.onAttachStateChangeListener != null) {
                this.onAttachStateChangeListener.onViewDetachedFromWindow(this);
            }
            AccessibilityManagerCompat.removeTouchExplorationStateChangeListener(this.accessibilityManager, this.touchExplorationStateChangeListener);
        }

        /* Access modifiers changed, original: 0000 */
        public void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
            this.onLayoutChangeListener = onLayoutChangeListener;
        }

        /* Access modifiers changed, original: 0000 */
        public void setOnAttachStateChangeListener(OnAttachStateChangeListener onAttachStateChangeListener) {
            this.onAttachStateChangeListener = onAttachStateChangeListener;
        }
    }

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$2 */
    class C00582 implements OnApplyWindowInsetsListener {
        C00582() {
        }

        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), windowInsetsCompat.getSystemWindowInsetBottom());
            return windowInsetsCompat;
        }
    }

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$3 */
    class C00593 extends AccessibilityDelegateCompat {
        C00593() {
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.addAction(1048576);
            accessibilityNodeInfoCompat.setDismissable(true);
        }

        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            if (i != 1048576) {
                return super.performAccessibilityAction(view, i, bundle);
            }
            BaseTransientBottomBar.this.dismiss();
            return true;
        }
    }

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$4 */
    class C00604 implements Callback {
        C00604() {
        }

        public void show() {
            BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(0, BaseTransientBottomBar.this));
        }

        public void dismiss(int i) {
            BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(1, i, 0, BaseTransientBottomBar.this));
        }
    }

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$5 */
    class C00615 implements OnDismissListener {
        C00615() {
        }

        public void onDismiss(View view) {
            view.setVisibility(8);
            BaseTransientBottomBar.this.dispatchDismiss(0);
        }

        public void onDragStateChanged(int i) {
            switch (i) {
                case 0:
                    SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.managerCallback);
                    return;
                case 1:
                case 2:
                    SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.managerCallback);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$6 */
    class C00626 implements OnAttachStateChangeListener {

        /* renamed from: android.support.design.widget.BaseTransientBottomBar$6$1 */
        class C00541 implements Runnable {
            C00541() {
            }

            public void run() {
                BaseTransientBottomBar.this.onViewHidden(3);
            }
        }

        public void onViewAttachedToWindow(View view) {
        }

        C00626() {
        }

        public void onViewDetachedFromWindow(View view) {
            if (BaseTransientBottomBar.this.isShownOrQueued()) {
                BaseTransientBottomBar.handler.post(new C00541());
            }
        }
    }

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$7 */
    class C00637 implements OnLayoutChangeListener {
        C00637() {
        }

        public void onLayoutChange(View view, int i, int i2, int i3, int i4) {
            BaseTransientBottomBar.this.view.setOnLayoutChangeListener(null);
            if (BaseTransientBottomBar.this.shouldAnimate()) {
                BaseTransientBottomBar.this.animateViewIn();
            } else {
                BaseTransientBottomBar.this.onViewShown();
            }
        }
    }

    public static class Behavior extends SwipeDismissBehavior<View> {
        private final BehaviorDelegate delegate = new BehaviorDelegate(this);

        private void setBaseTransientBottomBar(BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.delegate.setBaseTransientBottomBar(baseTransientBottomBar);
        }

        public boolean canSwipeDismissView(View view) {
            return this.delegate.canSwipeDismissView(view);
        }

        public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            this.delegate.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
            return super.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
        }
    }

    static {
        boolean z = VERSION.SDK_INT >= 16 && VERSION.SDK_INT <= 19;
        USE_OFFSET_API = z;
    }

    protected BaseTransientBottomBar(ViewGroup viewGroup, View view, ContentViewCallback contentViewCallback) {
        if (viewGroup == null) {
            throw new IllegalArgumentException("Transient bottom bar must have non-null parent");
        } else if (view == null) {
            throw new IllegalArgumentException("Transient bottom bar must have non-null content");
        } else if (contentViewCallback != null) {
            this.targetParent = viewGroup;
            this.contentViewCallback = contentViewCallback;
            this.context = viewGroup.getContext();
            ThemeEnforcement.checkAppCompatTheme(this.context);
            this.view = (SnackbarBaseLayout) LayoutInflater.from(this.context).inflate(getSnackbarBaseLayoutResId(), this.targetParent, false);
            this.view.addView(view);
            ViewCompat.setAccessibilityLiveRegion(this.view, 1);
            ViewCompat.setImportantForAccessibility(this.view, 1);
            ViewCompat.setFitsSystemWindows(this.view, true);
            ViewCompat.setOnApplyWindowInsetsListener(this.view, new C00582());
            ViewCompat.setAccessibilityDelegate(this.view, new C00593());
            this.accessibilityManager = (AccessibilityManager) this.context.getSystemService("accessibility");
        } else {
            throw new IllegalArgumentException("Transient bottom bar must have non-null callback");
        }
    }

    /* Access modifiers changed, original: protected */
    public int getSnackbarBaseLayoutResId() {
        return hasSnackbarStyleAttr() ? C0028R.layout.mtrl_layout_snackbar : C0028R.layout.design_layout_snackbar;
    }

    /* Access modifiers changed, original: protected */
    public boolean hasSnackbarStyleAttr() {
        TypedArray obtainStyledAttributes = this.context.obtainStyledAttributes(SNACKBAR_STYLE_ATTR);
        int resourceId = obtainStyledAttributes.getResourceId(0, -1);
        obtainStyledAttributes.recycle();
        if (resourceId != -1) {
            return true;
        }
        return false;
    }

    public B setDuration(int i) {
        this.duration = i;
        return this;
    }

    public int getDuration() {
        return this.duration;
    }

    public Context getContext() {
        return this.context;
    }

    public void show() {
        SnackbarManager.getInstance().show(getDuration(), this.managerCallback);
    }

    public void dismiss() {
        dispatchDismiss(3);
    }

    /* Access modifiers changed, original: protected */
    public void dispatchDismiss(int i) {
        SnackbarManager.getInstance().dismiss(this.managerCallback, i);
    }

    public B addCallback(BaseCallback<B> baseCallback) {
        if (baseCallback == null) {
            return this;
        }
        if (this.callbacks == null) {
            this.callbacks = new ArrayList();
        }
        this.callbacks.add(baseCallback);
        return this;
    }

    public boolean isShownOrQueued() {
        return SnackbarManager.getInstance().isCurrentOrNext(this.managerCallback);
    }

    /* Access modifiers changed, original: protected */
    public SwipeDismissBehavior<? extends View> getNewBehavior() {
        return new Behavior();
    }

    /* Access modifiers changed, original: final */
    public final void showView() {
        if (this.view.getParent() == null) {
            LayoutParams layoutParams = this.view.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.LayoutParams layoutParams2 = (CoordinatorLayout.LayoutParams) layoutParams;
                SwipeDismissBehavior newBehavior = this.behavior == null ? getNewBehavior() : this.behavior;
                if (newBehavior instanceof Behavior) {
                    ((Behavior) newBehavior).setBaseTransientBottomBar(this);
                }
                newBehavior.setListener(new C00615());
                layoutParams2.setBehavior(newBehavior);
                layoutParams2.insetEdge = 80;
            }
            this.targetParent.addView(this.view);
        }
        this.view.setOnAttachStateChangeListener(new C00626());
        if (!ViewCompat.isLaidOut(this.view)) {
            this.view.setOnLayoutChangeListener(new C00637());
        } else if (shouldAnimate()) {
            animateViewIn();
        } else {
            onViewShown();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void animateViewIn() {
        final int translationYBottom = getTranslationYBottom();
        if (USE_OFFSET_API) {
            ViewCompat.offsetTopAndBottom(this.view, translationYBottom);
        } else {
            this.view.setTranslationY((float) translationYBottom);
        }
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(new int[]{translationYBottom, 0});
        valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimator.setDuration(250);
        valueAnimator.addListener(new C00558());
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            private int previousAnimatedIntValue = translationYBottom;

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                if (BaseTransientBottomBar.USE_OFFSET_API) {
                    ViewCompat.offsetTopAndBottom(BaseTransientBottomBar.this.view, intValue - this.previousAnimatedIntValue);
                } else {
                    BaseTransientBottomBar.this.view.setTranslationY((float) intValue);
                }
                this.previousAnimatedIntValue = intValue;
            }
        });
        valueAnimator.start();
    }

    private void animateViewOut(final int i) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(new int[]{0, getTranslationYBottom()});
        valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimator.setDuration(250);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                BaseTransientBottomBar.this.contentViewCallback.animateContentOut(0, 180);
            }

            public void onAnimationEnd(Animator animator) {
                BaseTransientBottomBar.this.onViewHidden(i);
            }
        });
        valueAnimator.addUpdateListener(new C005211());
        valueAnimator.start();
    }

    private int getTranslationYBottom() {
        int height = this.view.getHeight();
        LayoutParams layoutParams = this.view.getLayoutParams();
        return layoutParams instanceof MarginLayoutParams ? height + ((MarginLayoutParams) layoutParams).bottomMargin : height;
    }

    /* Access modifiers changed, original: final */
    public final void hideView(int i) {
        if (shouldAnimate() && this.view.getVisibility() == 0) {
            animateViewOut(i);
        } else {
            onViewHidden(i);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onViewShown() {
        SnackbarManager.getInstance().onShown(this.managerCallback);
        if (this.callbacks != null) {
            for (int size = this.callbacks.size() - 1; size >= 0; size--) {
                ((BaseCallback) this.callbacks.get(size)).onShown(this);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onViewHidden(int i) {
        SnackbarManager.getInstance().onDismissed(this.managerCallback);
        if (this.callbacks != null) {
            for (int size = this.callbacks.size() - 1; size >= 0; size--) {
                ((BaseCallback) this.callbacks.get(size)).onDismissed(this, i);
            }
        }
        ViewParent parent = this.view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(this.view);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean shouldAnimate() {
        List enabledAccessibilityServiceList = this.accessibilityManager.getEnabledAccessibilityServiceList(1);
        if (enabledAccessibilityServiceList == null || !enabledAccessibilityServiceList.isEmpty()) {
            return false;
        }
        return true;
    }
}
