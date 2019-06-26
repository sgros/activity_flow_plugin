// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import androidx.annotation.Keep;
import android.app.Activity;
import android.view.accessibility.AccessibilityEvent;
import android.view.View$MeasureSpec;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import android.widget.FrameLayout$LayoutParams;
import android.view.DisplayCutout;
import android.graphics.Canvas;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.view.MotionEvent;
import android.widget.ListView;
import android.view.View;
import android.annotation.SuppressLint;
import android.view.WindowInsets;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.View$OnApplyWindowInsetsListener;
import android.os.Build$VERSION;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.view.VelocityTracker;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.animation.AnimatorSet;
import android.graphics.Paint;
import android.widget.FrameLayout;

public class DrawerLayoutContainer extends FrameLayout
{
    private static final int MIN_DRAWER_MARGIN = 64;
    private boolean allowDrawContent;
    private boolean allowOpenDrawer;
    private Paint backgroundPaint;
    private boolean beginTrackingSent;
    private int behindKeyboardColor;
    private AnimatorSet currentAnimation;
    private ViewGroup drawerLayout;
    private boolean drawerOpened;
    private float drawerPosition;
    private boolean hasCutout;
    private boolean inLayout;
    private Object lastInsets;
    private boolean maybeStartTracking;
    private int minDrawerMargin;
    private int paddingTop;
    private ActionBarLayout parentActionBarLayout;
    private Rect rect;
    private float scrimOpacity;
    private Paint scrimPaint;
    private Drawable shadowLeft;
    private boolean startedTracking;
    private int startedTrackingPointerId;
    private int startedTrackingX;
    private int startedTrackingY;
    private VelocityTracker velocityTracker;
    
    public DrawerLayoutContainer(final Context context) {
        super(context);
        this.rect = new Rect();
        this.scrimPaint = new Paint();
        this.backgroundPaint = new Paint();
        this.allowDrawContent = true;
        this.minDrawerMargin = (int)(AndroidUtilities.density * 64.0f + 0.5f);
        this.setDescendantFocusability(262144);
        this.setFocusableInTouchMode(true);
        if (Build$VERSION.SDK_INT >= 21) {
            this.setFitsSystemWindows(true);
            this.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new _$$Lambda$DrawerLayoutContainer$dOsUXLZuN_il_QGSmGuPct7OsoA(this));
            this.setSystemUiVisibility(1280);
        }
        this.shadowLeft = this.getResources().getDrawable(2131165599);
    }
    
    @SuppressLint({ "NewApi" })
    private void applyMarginInsets(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams, final Object o, int systemWindowInsetTop, final boolean b) {
        final WindowInsets windowInsets = (WindowInsets)o;
        final int n = 0;
        WindowInsets windowInsets2;
        if (systemWindowInsetTop == 3) {
            windowInsets2 = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), 0, windowInsets.getSystemWindowInsetBottom());
        }
        else {
            windowInsets2 = windowInsets;
            if (systemWindowInsetTop == 5) {
                windowInsets2 = windowInsets.replaceSystemWindowInsets(0, windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
            }
        }
        viewGroup$MarginLayoutParams.leftMargin = windowInsets2.getSystemWindowInsetLeft();
        if (b) {
            systemWindowInsetTop = n;
        }
        else {
            systemWindowInsetTop = windowInsets2.getSystemWindowInsetTop();
        }
        viewGroup$MarginLayoutParams.topMargin = systemWindowInsetTop;
        viewGroup$MarginLayoutParams.rightMargin = windowInsets2.getSystemWindowInsetRight();
        viewGroup$MarginLayoutParams.bottomMargin = windowInsets2.getSystemWindowInsetBottom();
    }
    
    @SuppressLint({ "NewApi" })
    private void dispatchChildInsets(final View view, final Object o, final int n) {
        final WindowInsets windowInsets = (WindowInsets)o;
        WindowInsets windowInsets2;
        if (n == 3) {
            windowInsets2 = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), 0, windowInsets.getSystemWindowInsetBottom());
        }
        else {
            windowInsets2 = windowInsets;
            if (n == 5) {
                windowInsets2 = windowInsets.replaceSystemWindowInsets(0, windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
            }
        }
        view.dispatchApplyWindowInsets(windowInsets2);
    }
    
    private float getScrimOpacity() {
        return this.scrimOpacity;
    }
    
    private int getTopInset(final Object o) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        int systemWindowInsetTop = 0;
        if (sdk_INT >= 21) {
            systemWindowInsetTop = systemWindowInsetTop;
            if (o != null) {
                systemWindowInsetTop = ((WindowInsets)o).getSystemWindowInsetTop();
            }
        }
        return systemWindowInsetTop;
    }
    
    private void onDrawerAnimationEnd(final boolean drawerOpened) {
        this.startedTracking = false;
        this.currentAnimation = null;
        if (!(this.drawerOpened = drawerOpened)) {
            final ViewGroup drawerLayout = this.drawerLayout;
            if (drawerLayout instanceof ListView) {
                ((ListView)drawerLayout).setSelectionFromTop(0, 0);
            }
        }
        if (Build$VERSION.SDK_INT >= 19) {
            for (int i = 0; i < this.getChildCount(); ++i) {
                final View child = this.getChildAt(i);
                if (child != this.drawerLayout) {
                    int importantForAccessibility;
                    if (drawerOpened) {
                        importantForAccessibility = 4;
                    }
                    else {
                        importantForAccessibility = 0;
                    }
                    child.setImportantForAccessibility(importantForAccessibility);
                }
            }
        }
        this.sendAccessibilityEvent(32);
    }
    
    private void prepareForDrawerOpen(final MotionEvent motionEvent) {
        this.maybeStartTracking = false;
        this.startedTracking = true;
        if (motionEvent != null) {
            this.startedTrackingX = (int)motionEvent.getX();
        }
        this.beginTrackingSent = false;
    }
    
    private void setScrimOpacity(final float scrimOpacity) {
        this.scrimOpacity = scrimOpacity;
        this.invalidate();
    }
    
    public void cancelCurrentAnimation() {
        final AnimatorSet currentAnimation = this.currentAnimation;
        if (currentAnimation != null) {
            currentAnimation.cancel();
            this.currentAnimation = null;
        }
    }
    
    public void closeDrawer(final boolean b) {
        this.cancelCurrentAnimation();
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "drawerPosition", new float[] { 0.0f }) });
        set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
        if (b) {
            set.setDuration((long)Math.max((int)(200.0f / this.drawerLayout.getMeasuredWidth() * this.drawerPosition), 50));
        }
        else {
            set.setDuration(300L);
        }
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                DrawerLayoutContainer.this.onDrawerAnimationEnd(false);
            }
        });
        set.start();
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, final long n) {
        final boolean allowDrawContent = this.allowDrawContent;
        int n2 = 0;
        if (!allowDrawContent) {
            return false;
        }
        final int height = this.getHeight();
        final boolean b = view != this.drawerLayout;
        final int width = this.getWidth();
        final int save = canvas.save();
        int n4;
        if (b) {
            final int childCount = this.getChildCount();
            int i = 0;
            int n3 = 0;
            n4 = 0;
            while (i < childCount) {
                final View child = this.getChildAt(i);
                int n5 = n4;
                if (child.getVisibility() == 0) {
                    n5 = n4;
                    if (child != this.drawerLayout) {
                        n5 = i;
                    }
                }
                int n6 = n3;
                if (child != view) {
                    n6 = n3;
                    if (child.getVisibility() == 0) {
                        n6 = n3;
                        if (child == this.drawerLayout) {
                            if (child.getHeight() < height) {
                                n6 = n3;
                            }
                            else {
                                final int n7 = child.getMeasuredWidth() + (int)child.getX();
                                if (n7 > (n6 = n3)) {
                                    n6 = n7;
                                }
                            }
                        }
                    }
                }
                ++i;
                n3 = n6;
                n4 = n5;
            }
            if (n3 != 0) {
                canvas.clipRect(n3, 0, width, this.getHeight());
            }
            n2 = n3;
        }
        else {
            n4 = 0;
        }
        final boolean drawChild = super.drawChild(canvas, view, n);
        canvas.restoreToCount(save);
        if (this.scrimOpacity > 0.0f && b) {
            if (this.indexOfChild(view) == n4) {
                this.scrimPaint.setColor((int)(this.scrimOpacity * 153.0f) << 24);
                canvas.drawRect((float)n2, 0.0f, (float)width, (float)this.getHeight(), this.scrimPaint);
            }
        }
        else if (this.shadowLeft != null) {
            final float max = Math.max(0.0f, Math.min(this.drawerPosition / AndroidUtilities.dp(20.0f), 1.0f));
            if (max != 0.0f) {
                this.shadowLeft.setBounds((int)this.drawerPosition, view.getTop(), (int)this.drawerPosition + this.shadowLeft.getIntrinsicWidth(), view.getBottom());
                this.shadowLeft.setAlpha((int)(max * 255.0f));
                this.shadowLeft.draw(canvas);
            }
        }
        return drawChild;
    }
    
    public View getDrawerLayout() {
        return (View)this.drawerLayout;
    }
    
    public float getDrawerPosition() {
        return this.drawerPosition;
    }
    
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    public boolean isDrawerOpened() {
        return this.drawerOpened;
    }
    
    public void moveDrawerByX(final float n) {
        this.setDrawerPosition(this.drawerPosition + n);
    }
    
    protected void onDraw(final Canvas canvas) {
        if (Build$VERSION.SDK_INT >= 21) {
            final Object lastInsets = this.lastInsets;
            if (lastInsets != null) {
                final WindowInsets windowInsets = (WindowInsets)lastInsets;
                final int systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
                if (systemWindowInsetBottom > 0) {
                    this.backgroundPaint.setColor(this.behindKeyboardColor);
                    canvas.drawRect(0.0f, (float)(this.getMeasuredHeight() - systemWindowInsetBottom), (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.backgroundPaint);
                }
                if (this.hasCutout) {
                    this.backgroundPaint.setColor(-16777216);
                    final int systemWindowInsetLeft = windowInsets.getSystemWindowInsetLeft();
                    if (systemWindowInsetLeft != 0) {
                        canvas.drawRect(0.0f, 0.0f, (float)systemWindowInsetLeft, (float)this.getMeasuredHeight(), this.backgroundPaint);
                    }
                    final int systemWindowInsetRight = windowInsets.getSystemWindowInsetRight();
                    if (systemWindowInsetRight != 0) {
                        canvas.drawRect((float)systemWindowInsetRight, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.backgroundPaint);
                    }
                }
            }
        }
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return this.parentActionBarLayout.checkTransitionAnimation() || this.onTouchEvent(motionEvent);
    }
    
    protected void onLayout(final boolean b, int i, int childCount, final int n, final int n2) {
        this.inLayout = true;
        View child;
        FrameLayout$LayoutParams frameLayout$LayoutParams;
        for (childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
                if (BuildVars.DEBUG_VERSION) {
                    if (this.drawerLayout != child) {
                        child.layout(frameLayout$LayoutParams.leftMargin, frameLayout$LayoutParams.topMargin + this.getPaddingTop(), frameLayout$LayoutParams.leftMargin + child.getMeasuredWidth(), frameLayout$LayoutParams.topMargin + child.getMeasuredHeight() + this.getPaddingTop());
                    }
                    else {
                        child.layout(-child.getMeasuredWidth(), frameLayout$LayoutParams.topMargin + this.getPaddingTop(), 0, frameLayout$LayoutParams.topMargin + child.getMeasuredHeight() + this.getPaddingTop());
                    }
                }
                else {
                    try {
                        if (this.drawerLayout != child) {
                            child.layout(frameLayout$LayoutParams.leftMargin, frameLayout$LayoutParams.topMargin + this.getPaddingTop(), frameLayout$LayoutParams.leftMargin + child.getMeasuredWidth(), frameLayout$LayoutParams.topMargin + child.getMeasuredHeight() + this.getPaddingTop());
                        }
                        else {
                            child.layout(-child.getMeasuredWidth(), frameLayout$LayoutParams.topMargin + this.getPaddingTop(), 0, frameLayout$LayoutParams.topMargin + child.getMeasuredHeight() + this.getPaddingTop());
                        }
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
            }
        }
        this.inLayout = false;
    }
    
    @SuppressLint({ "NewApi" })
    protected void onMeasure(final int n, final int n2) {
        final int size = View$MeasureSpec.getSize(n);
        final int size2 = View$MeasureSpec.getSize(n2);
        this.setMeasuredDimension(size, size2);
        int y = size2;
        if (Build$VERSION.SDK_INT < 21) {
            this.inLayout = true;
            if (size2 == AndroidUtilities.displaySize.y + AndroidUtilities.statusBarHeight) {
                if (this.getLayoutParams() instanceof ViewGroup$MarginLayoutParams) {
                    this.setPadding(0, AndroidUtilities.statusBarHeight, 0, 0);
                }
                y = AndroidUtilities.displaySize.y;
            }
            else {
                y = size2;
                if (this.getLayoutParams() instanceof ViewGroup$MarginLayoutParams) {
                    this.setPadding(0, 0, 0, 0);
                    y = size2;
                }
            }
            this.inLayout = false;
        }
        final boolean b = this.lastInsets != null && Build$VERSION.SDK_INT >= 21;
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
                if (b) {
                    if (child.getFitsSystemWindows()) {
                        this.dispatchChildInsets(child, this.lastInsets, frameLayout$LayoutParams.gravity);
                    }
                    else if (child.getTag() == null) {
                        this.applyMarginInsets((ViewGroup$MarginLayoutParams)frameLayout$LayoutParams, this.lastInsets, frameLayout$LayoutParams.gravity, Build$VERSION.SDK_INT >= 21);
                    }
                }
                if (this.drawerLayout != child) {
                    child.measure(View$MeasureSpec.makeMeasureSpec(size - frameLayout$LayoutParams.leftMargin - frameLayout$LayoutParams.rightMargin, 1073741824), View$MeasureSpec.makeMeasureSpec(y - frameLayout$LayoutParams.topMargin - frameLayout$LayoutParams.bottomMargin, 1073741824));
                }
                else {
                    child.setPadding(0, 0, 0, 0);
                    child.measure(FrameLayout.getChildMeasureSpec(n, this.minDrawerMargin + frameLayout$LayoutParams.leftMargin + frameLayout$LayoutParams.rightMargin, frameLayout$LayoutParams.width), FrameLayout.getChildMeasureSpec(n2, frameLayout$LayoutParams.topMargin + frameLayout$LayoutParams.bottomMargin, frameLayout$LayoutParams.height));
                }
            }
        }
    }
    
    public boolean onRequestSendAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        return (!this.drawerOpened || view == this.drawerLayout) && super.onRequestSendAccessibilityEvent(view, accessibilityEvent);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.parentActionBarLayout.checkTransitionAnimation()) {
            return false;
        }
        final boolean drawerOpened = this.drawerOpened;
        final boolean b = true;
        boolean b2 = true;
        if (drawerOpened && motionEvent != null && motionEvent.getX() > this.drawerPosition && !this.startedTracking) {
            if (motionEvent.getAction() == 1) {
                this.closeDrawer(false);
            }
            return true;
        }
        if (this.allowOpenDrawer && this.parentActionBarLayout.fragmentsStack.size() == 1) {
            if (motionEvent != null && (motionEvent.getAction() == 0 || motionEvent.getAction() == 2) && !this.startedTracking && !this.maybeStartTracking) {
                this.parentActionBarLayout.getHitRect(this.rect);
                this.startedTrackingX = (int)motionEvent.getX();
                this.startedTrackingY = (int)motionEvent.getY();
                if (this.rect.contains(this.startedTrackingX, this.startedTrackingY)) {
                    this.startedTrackingPointerId = motionEvent.getPointerId(0);
                    this.maybeStartTracking = true;
                    this.cancelCurrentAnimation();
                    final VelocityTracker velocityTracker = this.velocityTracker;
                    if (velocityTracker != null) {
                        velocityTracker.clear();
                    }
                }
            }
            else if (motionEvent != null && motionEvent.getAction() == 2 && motionEvent.getPointerId(0) == this.startedTrackingPointerId) {
                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }
                final float a = (float)(int)(motionEvent.getX() - this.startedTrackingX);
                final float n = (float)Math.abs((int)motionEvent.getY() - this.startedTrackingY);
                this.velocityTracker.addMovement(motionEvent);
                if (this.maybeStartTracking && !this.startedTracking && ((a > 0.0f && a / 3.0f > Math.abs(n) && Math.abs(a) >= AndroidUtilities.getPixelsInCM(0.2f, true)) || (this.drawerOpened && a < 0.0f && Math.abs(a) >= Math.abs(n) && Math.abs(a) >= AndroidUtilities.getPixelsInCM(0.4f, true)))) {
                    this.prepareForDrawerOpen(motionEvent);
                    this.startedTrackingX = (int)motionEvent.getX();
                    this.requestDisallowInterceptTouchEvent(true);
                }
                else if (this.startedTracking) {
                    if (!this.beginTrackingSent) {
                        if (((Activity)this.getContext()).getCurrentFocus() != null) {
                            AndroidUtilities.hideKeyboard(((Activity)this.getContext()).getCurrentFocus());
                        }
                        this.beginTrackingSent = true;
                    }
                    this.moveDrawerByX(a);
                    this.startedTrackingX = (int)motionEvent.getX();
                }
            }
            else if (motionEvent == null || (motionEvent != null && motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6))) {
                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }
                this.velocityTracker.computeCurrentVelocity(1000);
                Label_0717: {
                    if (!this.startedTracking) {
                        final float drawerPosition = this.drawerPosition;
                        if (drawerPosition == 0.0f || drawerPosition == this.drawerLayout.getMeasuredWidth()) {
                            break Label_0717;
                        }
                    }
                    final float xVelocity = this.velocityTracker.getXVelocity();
                    final float yVelocity = this.velocityTracker.getYVelocity();
                    if ((this.drawerPosition >= this.drawerLayout.getMeasuredWidth() / 2.0f || (xVelocity >= 3500.0f && Math.abs(xVelocity) >= Math.abs(yVelocity))) && (xVelocity >= 0.0f || Math.abs(xVelocity) < 3500.0f)) {
                        if (this.drawerOpened || Math.abs(xVelocity) < 3500.0f) {
                            b2 = false;
                        }
                        this.openDrawer(b2);
                    }
                    else {
                        this.closeDrawer(this.drawerOpened && Math.abs(xVelocity) >= 3500.0f && b);
                    }
                }
                this.startedTracking = false;
                this.maybeStartTracking = false;
                final VelocityTracker velocityTracker2 = this.velocityTracker;
                if (velocityTracker2 != null) {
                    velocityTracker2.recycle();
                    this.velocityTracker = null;
                }
            }
        }
        return this.startedTracking;
    }
    
    public void openDrawer(final boolean b) {
        if (!this.allowOpenDrawer) {
            return;
        }
        if (AndroidUtilities.isTablet()) {
            final ActionBarLayout parentActionBarLayout = this.parentActionBarLayout;
            if (parentActionBarLayout != null) {
                final Activity parentActivity = parentActionBarLayout.parentActivity;
                if (parentActivity != null) {
                    AndroidUtilities.hideKeyboard(parentActivity.getCurrentFocus());
                }
            }
        }
        this.cancelCurrentAnimation();
        final AnimatorSet currentAnimation = new AnimatorSet();
        currentAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "drawerPosition", new float[] { (float)this.drawerLayout.getMeasuredWidth() }) });
        currentAnimation.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
        if (b) {
            currentAnimation.setDuration((long)Math.max((int)(200.0f / this.drawerLayout.getMeasuredWidth() * (this.drawerLayout.getMeasuredWidth() - this.drawerPosition)), 50));
        }
        else {
            currentAnimation.setDuration(300L);
        }
        currentAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                DrawerLayoutContainer.this.onDrawerAnimationEnd(true);
            }
        });
        currentAnimation.start();
        this.currentAnimation = currentAnimation;
    }
    
    public void requestDisallowInterceptTouchEvent(final boolean b) {
        if (this.maybeStartTracking && !this.startedTracking) {
            this.onTouchEvent(null);
        }
        super.requestDisallowInterceptTouchEvent(b);
    }
    
    public void requestLayout() {
        if (!this.inLayout) {
            super.requestLayout();
        }
    }
    
    public void setAllowDrawContent(final boolean allowDrawContent) {
        if (this.allowDrawContent != allowDrawContent) {
            this.allowDrawContent = allowDrawContent;
            this.invalidate();
        }
    }
    
    public void setAllowOpenDrawer(final boolean allowOpenDrawer, final boolean b) {
        this.allowOpenDrawer = allowOpenDrawer;
        if (!this.allowOpenDrawer && this.drawerPosition != 0.0f) {
            if (!b) {
                this.setDrawerPosition(0.0f);
                this.onDrawerAnimationEnd(false);
            }
            else {
                this.closeDrawer(true);
            }
        }
    }
    
    public void setBehindKeyboardColor(final int behindKeyboardColor) {
        this.behindKeyboardColor = behindKeyboardColor;
        this.invalidate();
    }
    
    public void setDrawerLayout(final ViewGroup drawerLayout) {
        this.addView((View)(this.drawerLayout = drawerLayout));
        if (Build$VERSION.SDK_INT >= 21) {
            this.drawerLayout.setFitsSystemWindows(true);
        }
    }
    
    @Keep
    public void setDrawerPosition(final float drawerPosition) {
        this.drawerPosition = drawerPosition;
        if (this.drawerPosition > this.drawerLayout.getMeasuredWidth()) {
            this.drawerPosition = (float)this.drawerLayout.getMeasuredWidth();
        }
        else if (this.drawerPosition < 0.0f) {
            this.drawerPosition = 0.0f;
        }
        this.drawerLayout.setTranslationX(this.drawerPosition);
        int visibility;
        if (this.drawerPosition > 0.0f) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        if (this.drawerLayout.getVisibility() != visibility) {
            this.drawerLayout.setVisibility(visibility);
        }
        this.setScrimOpacity(this.drawerPosition / this.drawerLayout.getMeasuredWidth());
    }
    
    public void setParentActionBarLayout(final ActionBarLayout parentActionBarLayout) {
        this.parentActionBarLayout = parentActionBarLayout;
    }
}
