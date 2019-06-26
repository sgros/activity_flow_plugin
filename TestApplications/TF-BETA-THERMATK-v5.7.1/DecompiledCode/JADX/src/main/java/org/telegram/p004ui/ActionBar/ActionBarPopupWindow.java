package org.telegram.p004ui.ActionBar;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import androidx.annotation.Keep;
import com.google.android.exoplayer2.util.NalUnitUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLog;
import org.telegram.p004ui.Components.LayoutHelper;

/* renamed from: org.telegram.ui.ActionBar.ActionBarPopupWindow */
public class ActionBarPopupWindow extends PopupWindow {
    private static final OnScrollChangedListener NOP = C2165-$$Lambda$ActionBarPopupWindow$u1KuFqdl4RQdFf-yVDBUWk_fHAc.INSTANCE;
    private static final boolean allowAnimation = (VERSION.SDK_INT >= 18);
    private static DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    private static Method layoutInScreenMethod;
    private static final Field superListenerField;
    private boolean animationEnabled = allowAnimation;
    private int dismissAnimationDuration = 150;
    private OnScrollChangedListener mSuperScrollListener;
    private ViewTreeObserver mViewTreeObserver;
    private AnimatorSet windowAnimatorSet;

    /* renamed from: org.telegram.ui.ActionBar.ActionBarPopupWindow$1 */
    class C22051 implements AnimatorListener {
        public void onAnimationRepeat(Animator animator) {
        }

        public void onAnimationStart(Animator animator) {
        }

        C22051() {
        }

        public void onAnimationEnd(Animator animator) {
            ActionBarPopupWindow.this.windowAnimatorSet = null;
        }

        public void onAnimationCancel(Animator animator) {
            onAnimationEnd(animator);
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarPopupWindow$2 */
    class C22062 implements AnimatorListener {
        public void onAnimationRepeat(Animator animator) {
        }

        public void onAnimationStart(Animator animator) {
        }

        C22062() {
        }

        public void onAnimationEnd(Animator animator) {
            ActionBarPopupWindow.this.windowAnimatorSet = null;
            ActionBarPopupWindow.this.setFocusable(false);
            try {
                super.dismiss();
            } catch (Exception unused) {
            }
            ActionBarPopupWindow.this.unregisterListener();
        }

        public void onAnimationCancel(Animator animator) {
            onAnimationEnd(animator);
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarPopupWindow$ActionBarPopupWindowLayout */
    public static class ActionBarPopupWindowLayout extends FrameLayout {
        private boolean animationEnabled = ActionBarPopupWindow.allowAnimation;
        private int backAlpha = NalUnitUtil.EXTENDED_SAR;
        private float backScaleX = 1.0f;
        private float backScaleY = 1.0f;
        protected Drawable backgroundDrawable = getResources().getDrawable(C1067R.C1065drawable.popup_fixed_alert2).mutate();
        private int lastStartedChild = 0;
        protected LinearLayout linearLayout;
        private OnDispatchKeyEventListener mOnDispatchKeyEventListener;
        private HashMap<View, Integer> positions = new HashMap();
        private ScrollView scrollView;
        private boolean showedFromBotton;

        public ActionBarPopupWindowLayout(Context context) {
            super(context);
            this.backgroundDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground), Mode.MULTIPLY));
            setPadding(AndroidUtilities.m26dp(8.0f), AndroidUtilities.m26dp(8.0f), AndroidUtilities.m26dp(8.0f), AndroidUtilities.m26dp(8.0f));
            setWillNotDraw(false);
            try {
                this.scrollView = new ScrollView(context);
                this.scrollView.setVerticalScrollBarEnabled(false);
                addView(this.scrollView, LayoutHelper.createFrame(-2, -2.0f));
            } catch (Throwable th) {
                FileLog.m30e(th);
            }
            this.linearLayout = new LinearLayout(context);
            this.linearLayout.setOrientation(1);
            ScrollView scrollView = this.scrollView;
            if (scrollView != null) {
                scrollView.addView(this.linearLayout, new LayoutParams(-2, -2));
            } else {
                addView(this.linearLayout, LayoutHelper.createFrame(-2, -2.0f));
            }
        }

        public void setShowedFromBotton(boolean z) {
            this.showedFromBotton = z;
        }

        public void setDispatchKeyEventListener(OnDispatchKeyEventListener onDispatchKeyEventListener) {
            this.mOnDispatchKeyEventListener = onDispatchKeyEventListener;
        }

        @Keep
        public void setBackAlpha(int i) {
            this.backAlpha = i;
        }

        @Keep
        public int getBackAlpha() {
            return this.backAlpha;
        }

        @Keep
        public void setBackScaleX(float f) {
            this.backScaleX = f;
            invalidate();
        }

        @Keep
        public void setBackScaleY(float f) {
            this.backScaleY = f;
            if (this.animationEnabled) {
                int i;
                int visibility;
                int itemsCount = getItemsCount();
                for (i = 0; i < itemsCount; i++) {
                    visibility = getItemAt(i).getVisibility();
                }
                i = getMeasuredHeight() - AndroidUtilities.m26dp(16.0f);
                if (this.showedFromBotton) {
                    for (itemsCount = this.lastStartedChild; itemsCount >= 0; itemsCount--) {
                        View itemAt = getItemAt(itemsCount);
                        if (itemAt.getVisibility() == 0) {
                            Integer num = (Integer) this.positions.get(itemAt);
                            if (num != null && ((float) (i - ((num.intValue() * AndroidUtilities.m26dp(48.0f)) + AndroidUtilities.m26dp(32.0f)))) > ((float) i) * f) {
                                break;
                            }
                            this.lastStartedChild = itemsCount - 1;
                            startChildAnimation(itemAt);
                        }
                    }
                } else {
                    for (visibility = this.lastStartedChild; visibility < itemsCount; visibility++) {
                        View itemAt2 = getItemAt(visibility);
                        if (itemAt2.getVisibility() == 0) {
                            Integer num2 = (Integer) this.positions.get(itemAt2);
                            if (num2 != null && ((float) (((num2.intValue() + 1) * AndroidUtilities.m26dp(48.0f)) - AndroidUtilities.m26dp(24.0f))) > ((float) i) * f) {
                                break;
                            }
                            this.lastStartedChild = visibility + 1;
                            startChildAnimation(itemAt2);
                        }
                    }
                }
            }
            invalidate();
        }

        public void setBackgroundDrawable(Drawable drawable) {
            this.backgroundDrawable = drawable;
        }

        private void startChildAnimation(View view) {
            if (this.animationEnabled) {
                AnimatorSet animatorSet = new AnimatorSet();
                r2 = new Animator[2];
                r2[0] = ObjectAnimator.ofFloat(view, "alpha", new float[]{0.0f, 1.0f});
                float[] fArr = new float[2];
                fArr[0] = (float) AndroidUtilities.m26dp(this.showedFromBotton ? 6.0f : -6.0f);
                fArr[1] = 0.0f;
                r2[1] = ObjectAnimator.ofFloat(view, "translationY", fArr);
                animatorSet.playTogether(r2);
                animatorSet.setDuration(180);
                animatorSet.setInterpolator(ActionBarPopupWindow.decelerateInterpolator);
                animatorSet.start();
            }
        }

        public void setAnimationEnabled(boolean z) {
            this.animationEnabled = z;
        }

        public void addView(View view) {
            this.linearLayout.addView(view);
        }

        public void removeInnerViews() {
            this.linearLayout.removeAllViews();
        }

        public float getBackScaleX() {
            return this.backScaleX;
        }

        public float getBackScaleY() {
            return this.backScaleY;
        }

        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            OnDispatchKeyEventListener onDispatchKeyEventListener = this.mOnDispatchKeyEventListener;
            if (onDispatchKeyEventListener != null) {
                onDispatchKeyEventListener.onDispatchKeyEvent(keyEvent);
            }
            return super.dispatchKeyEvent(keyEvent);
        }

        /* Access modifiers changed, original: protected */
        public void onDraw(Canvas canvas) {
            Drawable drawable = this.backgroundDrawable;
            if (drawable != null) {
                drawable.setAlpha(this.backAlpha);
                getMeasuredHeight();
                if (this.showedFromBotton) {
                    this.backgroundDrawable.setBounds(0, (int) (((float) getMeasuredHeight()) * (1.0f - this.backScaleY)), (int) (((float) getMeasuredWidth()) * this.backScaleX), getMeasuredHeight());
                } else {
                    this.backgroundDrawable.setBounds(0, 0, (int) (((float) getMeasuredWidth()) * this.backScaleX), (int) (((float) getMeasuredHeight()) * this.backScaleY));
                }
                this.backgroundDrawable.draw(canvas);
            }
        }

        public int getItemsCount() {
            return this.linearLayout.getChildCount();
        }

        public View getItemAt(int i) {
            return this.linearLayout.getChildAt(i);
        }

        public void scrollToTop() {
            ScrollView scrollView = this.scrollView;
            if (scrollView != null) {
                scrollView.scrollTo(0, 0);
            }
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarPopupWindow$OnDispatchKeyEventListener */
    public interface OnDispatchKeyEventListener {
        void onDispatchKeyEvent(KeyEvent keyEvent);
    }

    static /* synthetic */ void lambda$static$0() {
    }

    static {
        Field field = null;
        try {
            field = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
            field.setAccessible(true);
        } catch (NoSuchFieldException unused) {
        }
        superListenerField = field;
    }

    public ActionBarPopupWindow() {
        init();
    }

    public ActionBarPopupWindow(Context context) {
        super(context);
        init();
    }

    public ActionBarPopupWindow(int i, int i2) {
        super(i, i2);
        init();
    }

    public ActionBarPopupWindow(View view) {
        super(view);
        init();
    }

    public ActionBarPopupWindow(View view, int i, int i2, boolean z) {
        super(view, i, i2, z);
        init();
    }

    public ActionBarPopupWindow(View view, int i, int i2) {
        super(view, i, i2);
        init();
    }

    public void setAnimationEnabled(boolean z) {
        this.animationEnabled = z;
    }

    public void setLayoutInScreen(boolean z) {
        try {
            if (layoutInScreenMethod == null) {
                layoutInScreenMethod = PopupWindow.class.getDeclaredMethod("setLayoutInScreenEnabled", new Class[]{Boolean.TYPE});
                layoutInScreenMethod.setAccessible(true);
            }
            layoutInScreenMethod.invoke(this, new Object[]{Boolean.valueOf(true)});
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    private void init() {
        Field field = superListenerField;
        if (field != null) {
            try {
                this.mSuperScrollListener = (OnScrollChangedListener) field.get(this);
                superListenerField.set(this, NOP);
            } catch (Exception unused) {
                this.mSuperScrollListener = null;
            }
        }
    }

    public void setDismissAnimationDuration(int i) {
        this.dismissAnimationDuration = i;
    }

    private void unregisterListener() {
        if (this.mSuperScrollListener != null) {
            ViewTreeObserver viewTreeObserver = this.mViewTreeObserver;
            if (viewTreeObserver != null) {
                if (viewTreeObserver.isAlive()) {
                    this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
                }
                this.mViewTreeObserver = null;
            }
        }
    }

    private void registerListener(View view) {
        if (this.mSuperScrollListener != null) {
            ViewTreeObserver viewTreeObserver = view.getWindowToken() != null ? view.getViewTreeObserver() : null;
            ViewTreeObserver viewTreeObserver2 = this.mViewTreeObserver;
            if (viewTreeObserver != viewTreeObserver2) {
                if (viewTreeObserver2 != null && viewTreeObserver2.isAlive()) {
                    this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
                }
                this.mViewTreeObserver = viewTreeObserver;
                if (viewTreeObserver != null) {
                    viewTreeObserver.addOnScrollChangedListener(this.mSuperScrollListener);
                }
            }
        }
    }

    public void showAsDropDown(View view, int i, int i2) {
        try {
            super.showAsDropDown(view, i, i2);
            registerListener(view);
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public void startAnimation() {
        if (this.animationEnabled && this.windowAnimatorSet == null) {
            ActionBarPopupWindowLayout actionBarPopupWindowLayout = (ActionBarPopupWindowLayout) getContentView();
            actionBarPopupWindowLayout.setTranslationY(0.0f);
            actionBarPopupWindowLayout.setAlpha(1.0f);
            actionBarPopupWindowLayout.setPivotX((float) actionBarPopupWindowLayout.getMeasuredWidth());
            actionBarPopupWindowLayout.setPivotY(0.0f);
            int itemsCount = actionBarPopupWindowLayout.getItemsCount();
            actionBarPopupWindowLayout.positions.clear();
            int i = 0;
            for (int i2 = 0; i2 < itemsCount; i2++) {
                View itemAt = actionBarPopupWindowLayout.getItemAt(i2);
                if (itemAt.getVisibility() == 0) {
                    actionBarPopupWindowLayout.positions.put(itemAt, Integer.valueOf(i));
                    itemAt.setAlpha(0.0f);
                    i++;
                }
            }
            if (actionBarPopupWindowLayout.showedFromBotton) {
                actionBarPopupWindowLayout.lastStartedChild = itemsCount - 1;
            } else {
                actionBarPopupWindowLayout.lastStartedChild = 0;
            }
            this.windowAnimatorSet = new AnimatorSet();
            AnimatorSet animatorSet = this.windowAnimatorSet;
            r6 = new Animator[2];
            r6[0] = ObjectAnimator.ofFloat(actionBarPopupWindowLayout, "backScaleY", new float[]{0.0f, 1.0f});
            r6[1] = ObjectAnimator.ofInt(actionBarPopupWindowLayout, "backAlpha", new int[]{0, NalUnitUtil.EXTENDED_SAR});
            animatorSet.playTogether(r6);
            this.windowAnimatorSet.setDuration((long) ((i * 16) + 150));
            this.windowAnimatorSet.addListener(new C22051());
            this.windowAnimatorSet.start();
        }
    }

    public void update(View view, int i, int i2, int i3, int i4) {
        super.update(view, i, i2, i3, i4);
        registerListener(view);
    }

    public void update(View view, int i, int i2) {
        super.update(view, i, i2);
        registerListener(view);
    }

    public void showAtLocation(View view, int i, int i2, int i3) {
        super.showAtLocation(view, i, i2, i3);
        unregisterListener();
    }

    public void dismiss() {
        dismiss(true);
    }

    public void dismiss(boolean z) {
        setFocusable(false);
        if (this.animationEnabled && z) {
            AnimatorSet animatorSet = this.windowAnimatorSet;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            ActionBarPopupWindowLayout actionBarPopupWindowLayout = (ActionBarPopupWindowLayout) getContentView();
            this.windowAnimatorSet = new AnimatorSet();
            AnimatorSet animatorSet2 = this.windowAnimatorSet;
            Animator[] animatorArr = new Animator[2];
            float[] fArr = new float[1];
            fArr[0] = (float) AndroidUtilities.m26dp(actionBarPopupWindowLayout.showedFromBotton ? 5.0f : -5.0f);
            animatorArr[0] = ObjectAnimator.ofFloat(actionBarPopupWindowLayout, "translationY", fArr);
            animatorArr[1] = ObjectAnimator.ofFloat(actionBarPopupWindowLayout, "alpha", new float[]{0.0f});
            animatorSet2.playTogether(animatorArr);
            this.windowAnimatorSet.setDuration((long) this.dismissAnimationDuration);
            this.windowAnimatorSet.addListener(new C22062());
            this.windowAnimatorSet.start();
            return;
        }
        try {
            super.dismiss();
        } catch (Exception unused) {
        }
        unregisterListener();
    }
}
