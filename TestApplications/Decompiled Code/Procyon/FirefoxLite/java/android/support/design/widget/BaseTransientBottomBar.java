// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.util.AttributeSet;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.widget.FrameLayout;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.content.res.TypedArray;
import java.util.ArrayList;
import android.view.ViewGroup$LayoutParams;
import android.view.ViewGroup$MarginLayoutParams;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.animation.AnimationUtils;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.support.design.internal.ThemeEnforcement;
import android.view.View;
import android.os.Message;
import android.os.Handler$Callback;
import android.os.Looper;
import android.support.design.R;
import android.os.Build$VERSION;
import android.view.ViewGroup;
import android.content.Context;
import android.support.design.snackbar.ContentViewCallback;
import java.util.List;
import android.view.accessibility.AccessibilityManager;
import android.os.Handler;

public abstract class BaseTransientBottomBar<B extends BaseTransientBottomBar<B>>
{
    private static final int[] SNACKBAR_STYLE_ATTR;
    private static final boolean USE_OFFSET_API;
    static final Handler handler;
    private final AccessibilityManager accessibilityManager;
    private Behavior behavior;
    private List<BaseCallback<B>> callbacks;
    private final ContentViewCallback contentViewCallback;
    private final Context context;
    private int duration;
    final SnackbarManager.Callback managerCallback;
    private final ViewGroup targetParent;
    protected final SnackbarBaseLayout view;
    
    static {
        USE_OFFSET_API = (Build$VERSION.SDK_INT >= 16 && Build$VERSION.SDK_INT <= 19);
        SNACKBAR_STYLE_ATTR = new int[] { R.attr.snackbarStyle };
        handler = new Handler(Looper.getMainLooper(), (Handler$Callback)new Handler$Callback() {
            public boolean handleMessage(final Message message) {
                switch (message.what) {
                    default: {
                        return false;
                    }
                    case 1: {
                        ((BaseTransientBottomBar)message.obj).hideView(message.arg1);
                        return true;
                    }
                    case 0: {
                        ((BaseTransientBottomBar)message.obj).showView();
                        return true;
                    }
                }
            }
        });
    }
    
    protected BaseTransientBottomBar(final ViewGroup targetParent, final View view, final ContentViewCallback contentViewCallback) {
        this.managerCallback = new SnackbarManager.Callback() {
            @Override
            public void dismiss(final int n) {
                BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(1, n, 0, (Object)BaseTransientBottomBar.this));
            }
            
            @Override
            public void show() {
                BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(0, (Object)BaseTransientBottomBar.this));
            }
        };
        if (targetParent == null) {
            throw new IllegalArgumentException("Transient bottom bar must have non-null parent");
        }
        if (view == null) {
            throw new IllegalArgumentException("Transient bottom bar must have non-null content");
        }
        if (contentViewCallback != null) {
            this.targetParent = targetParent;
            this.contentViewCallback = contentViewCallback;
            ThemeEnforcement.checkAppCompatTheme(this.context = targetParent.getContext());
            (this.view = (SnackbarBaseLayout)LayoutInflater.from(this.context).inflate(this.getSnackbarBaseLayoutResId(), this.targetParent, false)).addView(view);
            ViewCompat.setAccessibilityLiveRegion((View)this.view, 1);
            ViewCompat.setImportantForAccessibility((View)this.view, 1);
            ViewCompat.setFitsSystemWindows((View)this.view, true);
            ViewCompat.setOnApplyWindowInsetsListener((View)this.view, new OnApplyWindowInsetsListener() {
                @Override
                public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
                    view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                    return windowInsetsCompat;
                }
            });
            ViewCompat.setAccessibilityDelegate((View)this.view, new AccessibilityDelegateCompat() {
                @Override
                public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                    accessibilityNodeInfoCompat.addAction(1048576);
                    accessibilityNodeInfoCompat.setDismissable(true);
                }
                
                @Override
                public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
                    if (n == 1048576) {
                        BaseTransientBottomBar.this.dismiss();
                        return true;
                    }
                    return super.performAccessibilityAction(view, n, bundle);
                }
            });
            this.accessibilityManager = (AccessibilityManager)this.context.getSystemService("accessibility");
            return;
        }
        throw new IllegalArgumentException("Transient bottom bar must have non-null callback");
    }
    
    private void animateViewOut(final int n) {
        final ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(new int[] { 0, this.getTranslationYBottom() });
        valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimator.setDuration(250L);
        valueAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                BaseTransientBottomBar.this.onViewHidden(n);
            }
            
            public void onAnimationStart(final Animator animator) {
                BaseTransientBottomBar.this.contentViewCallback.animateContentOut(0, 180);
            }
        });
        valueAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            private int previousAnimatedIntValue = 0;
            
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                final int intValue = (int)valueAnimator.getAnimatedValue();
                if (BaseTransientBottomBar.USE_OFFSET_API) {
                    ViewCompat.offsetTopAndBottom((View)BaseTransientBottomBar.this.view, intValue - this.previousAnimatedIntValue);
                }
                else {
                    BaseTransientBottomBar.this.view.setTranslationY((float)intValue);
                }
                this.previousAnimatedIntValue = intValue;
            }
        });
        valueAnimator.start();
    }
    
    private int getTranslationYBottom() {
        final int height = this.view.getHeight();
        final ViewGroup$LayoutParams layoutParams = this.view.getLayoutParams();
        int n = height;
        if (layoutParams instanceof ViewGroup$MarginLayoutParams) {
            n = height + ((ViewGroup$MarginLayoutParams)layoutParams).bottomMargin;
        }
        return n;
    }
    
    public B addCallback(final BaseCallback<B> baseCallback) {
        if (baseCallback == null) {
            return (B)this;
        }
        if (this.callbacks == null) {
            this.callbacks = new ArrayList<BaseCallback<B>>();
        }
        this.callbacks.add(baseCallback);
        return (B)this;
    }
    
    void animateViewIn() {
        final int translationYBottom = this.getTranslationYBottom();
        if (BaseTransientBottomBar.USE_OFFSET_API) {
            ViewCompat.offsetTopAndBottom((View)this.view, translationYBottom);
        }
        else {
            this.view.setTranslationY((float)translationYBottom);
        }
        final ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(new int[] { translationYBottom, 0 });
        valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimator.setDuration(250L);
        valueAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                BaseTransientBottomBar.this.onViewShown();
            }
            
            public void onAnimationStart(final Animator animator) {
                BaseTransientBottomBar.this.contentViewCallback.animateContentIn(70, 180);
            }
        });
        valueAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            private int previousAnimatedIntValue = translationYBottom;
            
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                final int intValue = (int)valueAnimator.getAnimatedValue();
                if (BaseTransientBottomBar.USE_OFFSET_API) {
                    ViewCompat.offsetTopAndBottom((View)BaseTransientBottomBar.this.view, intValue - this.previousAnimatedIntValue);
                }
                else {
                    BaseTransientBottomBar.this.view.setTranslationY((float)intValue);
                }
                this.previousAnimatedIntValue = intValue;
            }
        });
        valueAnimator.start();
    }
    
    public void dismiss() {
        this.dispatchDismiss(3);
    }
    
    protected void dispatchDismiss(final int n) {
        SnackbarManager.getInstance().dismiss(this.managerCallback, n);
    }
    
    public Context getContext() {
        return this.context;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    protected SwipeDismissBehavior<? extends View> getNewBehavior() {
        return new Behavior();
    }
    
    protected int getSnackbarBaseLayoutResId() {
        int n;
        if (this.hasSnackbarStyleAttr()) {
            n = R.layout.mtrl_layout_snackbar;
        }
        else {
            n = R.layout.design_layout_snackbar;
        }
        return n;
    }
    
    protected boolean hasSnackbarStyleAttr() {
        final TypedArray obtainStyledAttributes = this.context.obtainStyledAttributes(BaseTransientBottomBar.SNACKBAR_STYLE_ATTR);
        boolean b = false;
        final int resourceId = obtainStyledAttributes.getResourceId(0, -1);
        obtainStyledAttributes.recycle();
        if (resourceId != -1) {
            b = true;
        }
        return b;
    }
    
    final void hideView(final int n) {
        if (this.shouldAnimate() && this.view.getVisibility() == 0) {
            this.animateViewOut(n);
        }
        else {
            this.onViewHidden(n);
        }
    }
    
    public boolean isShownOrQueued() {
        return SnackbarManager.getInstance().isCurrentOrNext(this.managerCallback);
    }
    
    void onViewHidden(final int n) {
        SnackbarManager.getInstance().onDismissed(this.managerCallback);
        if (this.callbacks != null) {
            for (int i = this.callbacks.size() - 1; i >= 0; --i) {
                this.callbacks.get(i).onDismissed((B)this, n);
            }
        }
        final ViewParent parent = this.view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup)parent).removeView((View)this.view);
        }
    }
    
    void onViewShown() {
        SnackbarManager.getInstance().onShown(this.managerCallback);
        if (this.callbacks != null) {
            for (int i = this.callbacks.size() - 1; i >= 0; --i) {
                this.callbacks.get(i).onShown((B)this);
            }
        }
    }
    
    public B setDuration(final int duration) {
        this.duration = duration;
        return (B)this;
    }
    
    boolean shouldAnimate() {
        final AccessibilityManager accessibilityManager = this.accessibilityManager;
        boolean b = true;
        final List enabledAccessibilityServiceList = accessibilityManager.getEnabledAccessibilityServiceList(1);
        if (enabledAccessibilityServiceList == null || !enabledAccessibilityServiceList.isEmpty()) {
            b = false;
        }
        return b;
    }
    
    public void show() {
        SnackbarManager.getInstance().show(this.getDuration(), this.managerCallback);
    }
    
    final void showView() {
        if (this.view.getParent() == null) {
            final ViewGroup$LayoutParams layoutParams = this.view.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                final CoordinatorLayout.LayoutParams layoutParams2 = (CoordinatorLayout.LayoutParams)layoutParams;
                SwipeDismissBehavior<? extends View> behavior;
                if (this.behavior == null) {
                    behavior = this.getNewBehavior();
                }
                else {
                    behavior = this.behavior;
                }
                if (behavior instanceof Behavior) {
                    ((Behavior)behavior).setBaseTransientBottomBar(this);
                }
                behavior.setListener((SwipeDismissBehavior.OnDismissListener)new SwipeDismissBehavior.OnDismissListener() {
                    @Override
                    public void onDismiss(final View view) {
                        view.setVisibility(8);
                        BaseTransientBottomBar.this.dispatchDismiss(0);
                    }
                    
                    @Override
                    public void onDragStateChanged(final int n) {
                        switch (n) {
                            case 1:
                            case 2: {
                                SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.managerCallback);
                                break;
                            }
                            case 0: {
                                SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.managerCallback);
                                break;
                            }
                        }
                    }
                });
                layoutParams2.setBehavior(behavior);
                layoutParams2.insetEdge = 80;
            }
            this.targetParent.addView((View)this.view);
        }
        this.view.setOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(final View view) {
            }
            
            @Override
            public void onViewDetachedFromWindow(final View view) {
                if (BaseTransientBottomBar.this.isShownOrQueued()) {
                    BaseTransientBottomBar.handler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            BaseTransientBottomBar.this.onViewHidden(3);
                        }
                    });
                }
            }
        });
        if (ViewCompat.isLaidOut((View)this.view)) {
            if (this.shouldAnimate()) {
                this.animateViewIn();
            }
            else {
                this.onViewShown();
            }
        }
        else {
            this.view.setOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(final View view, final int n, final int n2, final int n3, final int n4) {
                    BaseTransientBottomBar.this.view.setOnLayoutChangeListener(null);
                    if (BaseTransientBottomBar.this.shouldAnimate()) {
                        BaseTransientBottomBar.this.animateViewIn();
                    }
                    else {
                        BaseTransientBottomBar.this.onViewShown();
                    }
                }
            });
        }
    }
    
    public abstract static class BaseCallback<B>
    {
        public void onDismissed(final B b, final int n) {
        }
        
        public void onShown(final B b) {
        }
    }
    
    public static class Behavior extends SwipeDismissBehavior<View>
    {
        private final BehaviorDelegate delegate;
        
        public Behavior() {
            this.delegate = new BehaviorDelegate(this);
        }
        
        private void setBaseTransientBottomBar(final BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.delegate.setBaseTransientBottomBar(baseTransientBottomBar);
        }
        
        @Override
        public boolean canSwipeDismissView(final View view) {
            return this.delegate.canSwipeDismissView(view);
        }
        
        @Override
        public boolean onInterceptTouchEvent(final CoordinatorLayout coordinatorLayout, final View view, final MotionEvent motionEvent) {
            this.delegate.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
            return super.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
        }
    }
    
    public static class BehaviorDelegate
    {
        private SnackbarManager.Callback managerCallback;
        
        public BehaviorDelegate(final SwipeDismissBehavior<?> swipeDismissBehavior) {
            swipeDismissBehavior.setStartAlphaSwipeDistance(0.1f);
            swipeDismissBehavior.setEndAlphaSwipeDistance(0.6f);
            swipeDismissBehavior.setSwipeDirection(0);
        }
        
        public boolean canSwipeDismissView(final View view) {
            return view instanceof SnackbarBaseLayout;
        }
        
        public void onInterceptTouchEvent(final CoordinatorLayout coordinatorLayout, final View view, final MotionEvent motionEvent) {
            final int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 3) {
                switch (actionMasked) {
                    default: {
                        return;
                    }
                    case 0: {
                        if (coordinatorLayout.isPointInChildBounds(view, (int)motionEvent.getX(), (int)motionEvent.getY())) {
                            SnackbarManager.getInstance().pauseTimeout(this.managerCallback);
                        }
                        return;
                    }
                    case 1: {
                        break;
                    }
                }
            }
            SnackbarManager.getInstance().restoreTimeoutIfPaused(this.managerCallback);
        }
        
        public void setBaseTransientBottomBar(final BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.managerCallback = baseTransientBottomBar.managerCallback;
        }
    }
    
    protected interface OnAttachStateChangeListener
    {
        void onViewAttachedToWindow(final View p0);
        
        void onViewDetachedFromWindow(final View p0);
    }
    
    protected interface OnLayoutChangeListener
    {
        void onLayoutChange(final View p0, final int p1, final int p2, final int p3, final int p4);
    }
    
    protected static class SnackbarBaseLayout extends FrameLayout
    {
        private final AccessibilityManager accessibilityManager;
        private OnAttachStateChangeListener onAttachStateChangeListener;
        private OnLayoutChangeListener onLayoutChangeListener;
        private final AccessibilityManagerCompat.TouchExplorationStateChangeListener touchExplorationStateChangeListener;
        
        protected SnackbarBaseLayout(final Context context) {
            this(context, null);
        }
        
        protected SnackbarBaseLayout(final Context context, final AttributeSet set) {
            super(context, set);
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.SnackbarLayout);
            if (obtainStyledAttributes.hasValue(R.styleable.SnackbarLayout_elevation)) {
                ViewCompat.setElevation((View)this, (float)obtainStyledAttributes.getDimensionPixelSize(R.styleable.SnackbarLayout_elevation, 0));
            }
            obtainStyledAttributes.recycle();
            this.accessibilityManager = (AccessibilityManager)context.getSystemService("accessibility");
            this.touchExplorationStateChangeListener = new AccessibilityManagerCompat.TouchExplorationStateChangeListener() {
                @Override
                public void onTouchExplorationStateChanged(final boolean b) {
                    SnackbarBaseLayout.this.setClickableOrFocusableBasedOnAccessibility(b);
                }
            };
            AccessibilityManagerCompat.addTouchExplorationStateChangeListener(this.accessibilityManager, this.touchExplorationStateChangeListener);
            this.setClickableOrFocusableBasedOnAccessibility(this.accessibilityManager.isTouchExplorationEnabled());
        }
        
        private void setClickableOrFocusableBasedOnAccessibility(final boolean focusable) {
            this.setClickable(focusable ^ true);
            this.setFocusable(focusable);
        }
        
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (this.onAttachStateChangeListener != null) {
                this.onAttachStateChangeListener.onViewAttachedToWindow((View)this);
            }
            ViewCompat.requestApplyInsets((View)this);
        }
        
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (this.onAttachStateChangeListener != null) {
                this.onAttachStateChangeListener.onViewDetachedFromWindow((View)this);
            }
            AccessibilityManagerCompat.removeTouchExplorationStateChangeListener(this.accessibilityManager, this.touchExplorationStateChangeListener);
        }
        
        protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
            super.onLayout(b, n, n2, n3, n4);
            if (this.onLayoutChangeListener != null) {
                this.onLayoutChangeListener.onLayoutChange((View)this, n, n2, n3, n4);
            }
        }
        
        void setOnAttachStateChangeListener(final OnAttachStateChangeListener onAttachStateChangeListener) {
            this.onAttachStateChangeListener = onAttachStateChangeListener;
        }
        
        void setOnLayoutChangeListener(final OnLayoutChangeListener onLayoutChangeListener) {
            this.onLayoutChangeListener = onLayoutChangeListener;
        }
    }
}
