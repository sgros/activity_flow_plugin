// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import android.graphics.Canvas;
import androidx.annotation.Keep;
import android.view.KeyEvent;
import android.animation.TimeInterpolator;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ScrollView;
import java.util.HashMap;
import android.widget.LinearLayout;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import org.telegram.messenger.FileLog;
import android.animation.Animator$AnimatorListener;
import android.animation.ObjectAnimator;
import org.telegram.messenger.AndroidUtilities;
import android.animation.Animator;
import android.view.View;
import android.content.Context;
import android.os.Build$VERSION;
import android.animation.AnimatorSet;
import android.view.ViewTreeObserver;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import android.view.animation.DecelerateInterpolator;
import android.view.ViewTreeObserver$OnScrollChangedListener;
import android.widget.PopupWindow;

public class ActionBarPopupWindow extends PopupWindow
{
    private static final ViewTreeObserver$OnScrollChangedListener NOP;
    private static final boolean allowAnimation;
    private static DecelerateInterpolator decelerateInterpolator;
    private static Method layoutInScreenMethod;
    private static final Field superListenerField;
    private boolean animationEnabled;
    private int dismissAnimationDuration;
    private ViewTreeObserver$OnScrollChangedListener mSuperScrollListener;
    private ViewTreeObserver mViewTreeObserver;
    private AnimatorSet windowAnimatorSet;
    
    static {
        allowAnimation = (Build$VERSION.SDK_INT >= 18);
        ActionBarPopupWindow.decelerateInterpolator = new DecelerateInterpolator();
        Field declaredField = null;
        while (true) {
            try {
                final Field field = declaredField = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
                field.setAccessible(true);
                declaredField = field;
                superListenerField = declaredField;
                NOP = (ViewTreeObserver$OnScrollChangedListener)_$$Lambda$ActionBarPopupWindow$u1KuFqdl4RQdFf_yVDBUWk_fHAc.INSTANCE;
            }
            catch (NoSuchFieldException ex) {
                continue;
            }
            break;
        }
    }
    
    public ActionBarPopupWindow() {
        this.animationEnabled = ActionBarPopupWindow.allowAnimation;
        this.dismissAnimationDuration = 150;
        this.init();
    }
    
    public ActionBarPopupWindow(final int n, final int n2) {
        super(n, n2);
        this.animationEnabled = ActionBarPopupWindow.allowAnimation;
        this.dismissAnimationDuration = 150;
        this.init();
    }
    
    public ActionBarPopupWindow(final Context context) {
        super(context);
        this.animationEnabled = ActionBarPopupWindow.allowAnimation;
        this.dismissAnimationDuration = 150;
        this.init();
    }
    
    public ActionBarPopupWindow(final View view) {
        super(view);
        this.animationEnabled = ActionBarPopupWindow.allowAnimation;
        this.dismissAnimationDuration = 150;
        this.init();
    }
    
    public ActionBarPopupWindow(final View view, final int n, final int n2) {
        super(view, n, n2);
        this.animationEnabled = ActionBarPopupWindow.allowAnimation;
        this.dismissAnimationDuration = 150;
        this.init();
    }
    
    public ActionBarPopupWindow(final View view, final int n, final int n2, final boolean b) {
        super(view, n, n2, b);
        this.animationEnabled = ActionBarPopupWindow.allowAnimation;
        this.dismissAnimationDuration = 150;
        this.init();
    }
    
    static /* synthetic */ void access$601(final ActionBarPopupWindow actionBarPopupWindow) {
        actionBarPopupWindow.dismiss();
    }
    
    private void init() {
        final Field superListenerField = ActionBarPopupWindow.superListenerField;
        if (superListenerField != null) {
            try {
                this.mSuperScrollListener = (ViewTreeObserver$OnScrollChangedListener)superListenerField.get(this);
                ActionBarPopupWindow.superListenerField.set(this, ActionBarPopupWindow.NOP);
            }
            catch (Exception ex) {
                this.mSuperScrollListener = null;
            }
        }
    }
    
    private void registerListener(final View view) {
        if (this.mSuperScrollListener != null) {
            ViewTreeObserver viewTreeObserver;
            if (view.getWindowToken() != null) {
                viewTreeObserver = view.getViewTreeObserver();
            }
            else {
                viewTreeObserver = null;
            }
            final ViewTreeObserver mViewTreeObserver = this.mViewTreeObserver;
            if (viewTreeObserver != mViewTreeObserver) {
                if (mViewTreeObserver != null && mViewTreeObserver.isAlive()) {
                    this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
                }
                if ((this.mViewTreeObserver = viewTreeObserver) != null) {
                    viewTreeObserver.addOnScrollChangedListener(this.mSuperScrollListener);
                }
            }
        }
    }
    
    private void unregisterListener() {
        if (this.mSuperScrollListener != null) {
            final ViewTreeObserver mViewTreeObserver = this.mViewTreeObserver;
            if (mViewTreeObserver != null) {
                if (mViewTreeObserver.isAlive()) {
                    this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
                }
                this.mViewTreeObserver = null;
            }
        }
    }
    
    public void dismiss() {
        this.dismiss(true);
    }
    
    public void dismiss(final boolean b) {
        this.setFocusable(false);
        if (this.animationEnabled && b) {
            final AnimatorSet windowAnimatorSet = this.windowAnimatorSet;
            if (windowAnimatorSet != null) {
                windowAnimatorSet.cancel();
            }
            final ActionBarPopupWindowLayout actionBarPopupWindowLayout = (ActionBarPopupWindowLayout)this.getContentView();
            this.windowAnimatorSet = new AnimatorSet();
            final AnimatorSet windowAnimatorSet2 = this.windowAnimatorSet;
            float n;
            if (actionBarPopupWindowLayout.showedFromBotton) {
                n = 5.0f;
            }
            else {
                n = -5.0f;
            }
            windowAnimatorSet2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)actionBarPopupWindowLayout, "translationY", new float[] { (float)AndroidUtilities.dp(n) }), (Animator)ObjectAnimator.ofFloat((Object)actionBarPopupWindowLayout, "alpha", new float[] { 0.0f }) });
            this.windowAnimatorSet.setDuration((long)this.dismissAnimationDuration);
            this.windowAnimatorSet.addListener((Animator$AnimatorListener)new Animator$AnimatorListener() {
                public void onAnimationCancel(final Animator animator) {
                    this.onAnimationEnd(animator);
                }
                
                public void onAnimationEnd(final Animator animator) {
                    ActionBarPopupWindow.this.windowAnimatorSet = null;
                    ActionBarPopupWindow.this.setFocusable(false);
                    while (true) {
                        try {
                            ActionBarPopupWindow.access$601(ActionBarPopupWindow.this);
                            ActionBarPopupWindow.this.unregisterListener();
                        }
                        catch (Exception ex) {
                            continue;
                        }
                        break;
                    }
                }
                
                public void onAnimationRepeat(final Animator animator) {
                }
                
                public void onAnimationStart(final Animator animator) {
                }
            });
            this.windowAnimatorSet.start();
            return;
        }
        while (true) {
            try {
                super.dismiss();
                this.unregisterListener();
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    public void setAnimationEnabled(final boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
    }
    
    public void setDismissAnimationDuration(final int dismissAnimationDuration) {
        this.dismissAnimationDuration = dismissAnimationDuration;
    }
    
    public void setLayoutInScreen(final boolean b) {
        try {
            if (ActionBarPopupWindow.layoutInScreenMethod == null) {
                (ActionBarPopupWindow.layoutInScreenMethod = PopupWindow.class.getDeclaredMethod("setLayoutInScreenEnabled", Boolean.TYPE)).setAccessible(true);
            }
            ActionBarPopupWindow.layoutInScreenMethod.invoke(this, true);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void showAsDropDown(final View view, final int n, final int n2) {
        try {
            super.showAsDropDown(view, n, n2);
            this.registerListener(view);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void showAtLocation(final View view, final int n, final int n2, final int n3) {
        super.showAtLocation(view, n, n2, n3);
        this.unregisterListener();
    }
    
    public void startAnimation() {
        if (this.animationEnabled) {
            if (this.windowAnimatorSet != null) {
                return;
            }
            final ActionBarPopupWindowLayout actionBarPopupWindowLayout = (ActionBarPopupWindowLayout)this.getContentView();
            actionBarPopupWindowLayout.setTranslationY(0.0f);
            actionBarPopupWindowLayout.setAlpha(1.0f);
            actionBarPopupWindowLayout.setPivotX((float)actionBarPopupWindowLayout.getMeasuredWidth());
            actionBarPopupWindowLayout.setPivotY(0.0f);
            final int itemsCount = actionBarPopupWindowLayout.getItemsCount();
            actionBarPopupWindowLayout.positions.clear();
            int i = 0;
            int j = 0;
            while (i < itemsCount) {
                final View item = actionBarPopupWindowLayout.getItemAt(i);
                if (item.getVisibility() == 0) {
                    actionBarPopupWindowLayout.positions.put(item, j);
                    item.setAlpha(0.0f);
                    ++j;
                }
                ++i;
            }
            if (actionBarPopupWindowLayout.showedFromBotton) {
                actionBarPopupWindowLayout.lastStartedChild = itemsCount - 1;
            }
            else {
                actionBarPopupWindowLayout.lastStartedChild = 0;
            }
            (this.windowAnimatorSet = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)actionBarPopupWindowLayout, "backScaleY", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofInt((Object)actionBarPopupWindowLayout, "backAlpha", new int[] { 0, 255 }) });
            this.windowAnimatorSet.setDuration((long)(j * 16 + 150));
            this.windowAnimatorSet.addListener((Animator$AnimatorListener)new Animator$AnimatorListener() {
                public void onAnimationCancel(final Animator animator) {
                    this.onAnimationEnd(animator);
                }
                
                public void onAnimationEnd(final Animator animator) {
                    ActionBarPopupWindow.this.windowAnimatorSet = null;
                }
                
                public void onAnimationRepeat(final Animator animator) {
                }
                
                public void onAnimationStart(final Animator animator) {
                }
            });
            this.windowAnimatorSet.start();
        }
    }
    
    public void update(final View view, final int n, final int n2) {
        super.update(view, n, n2);
        this.registerListener(view);
    }
    
    public void update(final View view, final int n, final int n2, final int n3, final int n4) {
        super.update(view, n, n2, n3, n4);
        this.registerListener(view);
    }
    
    public static class ActionBarPopupWindowLayout extends FrameLayout
    {
        private boolean animationEnabled;
        private int backAlpha;
        private float backScaleX;
        private float backScaleY;
        protected Drawable backgroundDrawable;
        private int lastStartedChild;
        protected LinearLayout linearLayout;
        private OnDispatchKeyEventListener mOnDispatchKeyEventListener;
        private HashMap<View, Integer> positions;
        private ScrollView scrollView;
        private boolean showedFromBotton;
        
        public ActionBarPopupWindowLayout(final Context context) {
            super(context);
            this.backScaleX = 1.0f;
            this.backScaleY = 1.0f;
            this.backAlpha = 255;
            this.lastStartedChild = 0;
            this.animationEnabled = ActionBarPopupWindow.allowAnimation;
            this.positions = new HashMap<View, Integer>();
            (this.backgroundDrawable = this.getResources().getDrawable(2131165777).mutate()).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("actionBarDefaultSubmenuBackground"), PorterDuff$Mode.MULTIPLY));
            this.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            this.setWillNotDraw(false);
            try {
                (this.scrollView = new ScrollView(context)).setVerticalScrollBarEnabled(false);
                this.addView((View)this.scrollView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f));
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
            (this.linearLayout = new LinearLayout(context)).setOrientation(1);
            final ScrollView scrollView = this.scrollView;
            if (scrollView != null) {
                scrollView.addView((View)this.linearLayout, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-2, -2));
            }
            else {
                this.addView((View)this.linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f));
            }
        }
        
        private void startChildAnimation(final View view) {
            if (this.animationEnabled) {
                final AnimatorSet set = new AnimatorSet();
                final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)view, "alpha", new float[] { 0.0f, 1.0f });
                float n;
                if (this.showedFromBotton) {
                    n = 6.0f;
                }
                else {
                    n = -6.0f;
                }
                set.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ObjectAnimator.ofFloat((Object)view, "translationY", new float[] { (float)AndroidUtilities.dp(n), 0.0f }) });
                set.setDuration(180L);
                set.setInterpolator((TimeInterpolator)ActionBarPopupWindow.decelerateInterpolator);
                set.start();
            }
        }
        
        public void addView(final View view) {
            this.linearLayout.addView(view);
        }
        
        public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
            final OnDispatchKeyEventListener mOnDispatchKeyEventListener = this.mOnDispatchKeyEventListener;
            if (mOnDispatchKeyEventListener != null) {
                mOnDispatchKeyEventListener.onDispatchKeyEvent(keyEvent);
            }
            return super.dispatchKeyEvent(keyEvent);
        }
        
        @Keep
        public int getBackAlpha() {
            return this.backAlpha;
        }
        
        public float getBackScaleX() {
            return this.backScaleX;
        }
        
        public float getBackScaleY() {
            return this.backScaleY;
        }
        
        public View getItemAt(final int n) {
            return this.linearLayout.getChildAt(n);
        }
        
        public int getItemsCount() {
            return this.linearLayout.getChildCount();
        }
        
        protected void onDraw(final Canvas canvas) {
            final Drawable backgroundDrawable = this.backgroundDrawable;
            if (backgroundDrawable != null) {
                backgroundDrawable.setAlpha(this.backAlpha);
                this.getMeasuredHeight();
                if (this.showedFromBotton) {
                    this.backgroundDrawable.setBounds(0, (int)(this.getMeasuredHeight() * (1.0f - this.backScaleY)), (int)(this.getMeasuredWidth() * this.backScaleX), this.getMeasuredHeight());
                }
                else {
                    this.backgroundDrawable.setBounds(0, 0, (int)(this.getMeasuredWidth() * this.backScaleX), (int)(this.getMeasuredHeight() * this.backScaleY));
                }
                this.backgroundDrawable.draw(canvas);
            }
        }
        
        public void removeInnerViews() {
            this.linearLayout.removeAllViews();
        }
        
        public void scrollToTop() {
            final ScrollView scrollView = this.scrollView;
            if (scrollView != null) {
                scrollView.scrollTo(0, 0);
            }
        }
        
        public void setAnimationEnabled(final boolean animationEnabled) {
            this.animationEnabled = animationEnabled;
        }
        
        @Keep
        public void setBackAlpha(final int backAlpha) {
            this.backAlpha = backAlpha;
        }
        
        @Keep
        public void setBackScaleX(final float backScaleX) {
            this.backScaleX = backScaleX;
            this.invalidate();
        }
        
        @Keep
        public void setBackScaleY(final float backScaleY) {
            this.backScaleY = backScaleY;
            if (this.animationEnabled) {
                final int itemsCount = this.getItemsCount();
                for (int i = 0; i < itemsCount; ++i) {
                    this.getItemAt(i).getVisibility();
                }
                final int n = this.getMeasuredHeight() - AndroidUtilities.dp(16.0f);
                if (this.showedFromBotton) {
                    for (int j = this.lastStartedChild; j >= 0; --j) {
                        final View item = this.getItemAt(j);
                        if (item.getVisibility() == 0) {
                            final Integer n2 = this.positions.get(item);
                            if (n2 != null && n - (n2 * AndroidUtilities.dp(48.0f) + AndroidUtilities.dp(32.0f)) > n * backScaleY) {
                                break;
                            }
                            this.lastStartedChild = j - 1;
                            this.startChildAnimation(item);
                        }
                    }
                }
                else {
                    for (int k = this.lastStartedChild; k < itemsCount; ++k) {
                        final View item2 = this.getItemAt(k);
                        if (item2.getVisibility() == 0) {
                            final Integer n3 = this.positions.get(item2);
                            if (n3 != null && (n3 + 1) * AndroidUtilities.dp(48.0f) - AndroidUtilities.dp(24.0f) > n * backScaleY) {
                                break;
                            }
                            this.lastStartedChild = k + 1;
                            this.startChildAnimation(item2);
                        }
                    }
                }
            }
            this.invalidate();
        }
        
        public void setBackgroundDrawable(final Drawable backgroundDrawable) {
            this.backgroundDrawable = backgroundDrawable;
        }
        
        public void setDispatchKeyEventListener(final OnDispatchKeyEventListener mOnDispatchKeyEventListener) {
            this.mOnDispatchKeyEventListener = mOnDispatchKeyEventListener;
        }
        
        public void setShowedFromBotton(final boolean showedFromBotton) {
            this.showedFromBotton = showedFromBotton;
        }
    }
    
    public interface OnDispatchKeyEventListener
    {
        void onDispatchKeyEvent(final KeyEvent p0);
    }
}
