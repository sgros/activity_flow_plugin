// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.voip;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Canvas;
import android.animation.Animator$AnimatorListener;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import java.util.Collection;
import android.animation.ObjectAnimator;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Style;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.Paint;
import android.animation.AnimatorSet;
import android.graphics.Path;
import android.view.View;

public class CallSwipeView extends View
{
    private boolean animatingArrows;
    private Path arrow;
    private int[] arrowAlphas;
    private AnimatorSet arrowAnim;
    private Paint arrowsPaint;
    private boolean canceled;
    private boolean dragFromRight;
    private float dragStartX;
    private boolean dragging;
    private Listener listener;
    private Paint pullBgPaint;
    private RectF tmpRect;
    private View viewToDrag;
    
    public CallSwipeView(final Context context) {
        super(context);
        this.arrowAlphas = new int[] { 64, 64, 64 };
        this.dragging = false;
        this.tmpRect = new RectF();
        this.arrow = new Path();
        this.animatingArrows = false;
        this.canceled = false;
        this.init();
    }
    
    private int getDraggedViewWidth() {
        return this.getHeight();
    }
    
    private void init() {
        (this.arrowsPaint = new Paint(1)).setColor(-1);
        this.arrowsPaint.setStyle(Paint$Style.STROKE);
        this.arrowsPaint.setStrokeWidth((float)AndroidUtilities.dp(2.5f));
        this.pullBgPaint = new Paint(1);
        final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
        for (int i = 0; i < this.arrowAlphas.length; ++i) {
            final ObjectAnimator ofInt = ObjectAnimator.ofInt((Object)new ArrowAnimWrapper(i), "arrowAlpha", new int[] { 64, 255, 64 });
            ofInt.setDuration(700L);
            ofInt.setStartDelay((long)(i * 200));
            list.add(ofInt);
        }
        (this.arrowAnim = new AnimatorSet()).playTogether((Collection)list);
        this.arrowAnim.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            private Runnable restarter = new Runnable() {
                @Override
                public void run() {
                    if (CallSwipeView.this.arrowAnim != null) {
                        CallSwipeView.this.arrowAnim.start();
                    }
                }
            };
            private long startTime;
            
            public void onAnimationCancel(final Animator animator) {
                CallSwipeView.this.canceled = true;
            }
            
            public void onAnimationEnd(final Animator animator) {
                if (System.currentTimeMillis() - this.startTime < animator.getDuration() / 4L) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.w("Not repeating animation because previous loop was too fast");
                    }
                    return;
                }
                if (!CallSwipeView.this.canceled && CallSwipeView.this.animatingArrows) {
                    CallSwipeView.this.post(this.restarter);
                }
            }
            
            public void onAnimationStart(final Animator animator) {
                this.startTime = System.currentTimeMillis();
            }
        });
    }
    
    private void updateArrowPath() {
        this.arrow.reset();
        final int dp = AndroidUtilities.dp(6.0f);
        if (this.dragFromRight) {
            final Path arrow = this.arrow;
            final float n = (float)dp;
            arrow.moveTo(n, (float)(-dp));
            this.arrow.lineTo(0.0f, 0.0f);
            this.arrow.lineTo(n, n);
        }
        else {
            this.arrow.moveTo(0.0f, (float)(-dp));
            final Path arrow2 = this.arrow;
            final float n2 = (float)dp;
            arrow2.lineTo(n2, 0.0f);
            this.arrow.lineTo(0.0f, n2);
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        final AnimatorSet arrowAnim = this.arrowAnim;
        if (arrowAnim != null) {
            this.canceled = true;
            arrowAnim.cancel();
            this.arrowAnim = null;
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.viewToDrag.getTranslationX() != 0.0f) {
            if (this.dragFromRight) {
                this.tmpRect.set(this.getWidth() + this.viewToDrag.getTranslationX() - this.getDraggedViewWidth(), 0.0f, (float)this.getWidth(), (float)this.getHeight());
            }
            else {
                this.tmpRect.set(0.0f, 0.0f, this.viewToDrag.getTranslationX() + this.getDraggedViewWidth(), (float)this.getHeight());
            }
            canvas.drawRoundRect(this.tmpRect, (float)(this.getHeight() / 2), (float)(this.getHeight() / 2), this.pullBgPaint);
        }
        canvas.save();
        if (this.dragFromRight) {
            canvas.translate((float)(this.getWidth() - this.getHeight() - AndroidUtilities.dp(18.0f)), (float)(this.getHeight() / 2));
        }
        else {
            canvas.translate((float)(this.getHeight() + AndroidUtilities.dp(12.0f)), (float)(this.getHeight() / 2));
        }
        final float abs = Math.abs(this.viewToDrag.getTranslationX());
        for (int i = 0; i < 3; ++i) {
            final float n = (float)AndroidUtilities.dp((float)(i * 16));
            final float n2 = 16.0f;
            float n3 = 1.0f;
            if (abs > n) {
                n3 = 1.0f - Math.min(1.0f, Math.max(0.0f, (abs - AndroidUtilities.dp(16.0f) * i) / AndroidUtilities.dp(16.0f)));
            }
            this.arrowsPaint.setAlpha(Math.round(this.arrowAlphas[i] * n3));
            canvas.drawPath(this.arrow, this.arrowsPaint);
            float n4 = n2;
            if (this.dragFromRight) {
                n4 = -16.0f;
            }
            canvas.translate((float)AndroidUtilities.dp(n4), 0.0f);
        }
        canvas.restore();
        this.invalidate();
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(16);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (!this.isEnabled()) {
            return false;
        }
        if (motionEvent.getAction() == 0) {
            if ((!this.dragFromRight && motionEvent.getX() < this.getDraggedViewWidth()) || (this.dragFromRight && motionEvent.getX() > this.getWidth() - this.getDraggedViewWidth())) {
                this.dragging = true;
                this.dragStartX = motionEvent.getX();
                this.getParent().requestDisallowInterceptTouchEvent(true);
                this.listener.onDragStart();
                this.stopAnimatingArrows();
            }
        }
        else {
            final int action = motionEvent.getAction();
            float b = 0.0f;
            if (action == 2) {
                final View viewToDrag = this.viewToDrag;
                float a;
                if (this.dragFromRight) {
                    a = (float)(-(this.getWidth() - this.getDraggedViewWidth()));
                }
                else {
                    a = 0.0f;
                }
                final float x = motionEvent.getX();
                final float dragStartX = this.dragStartX;
                if (!this.dragFromRight) {
                    b = (float)(this.getWidth() - this.getDraggedViewWidth());
                }
                viewToDrag.setTranslationX(Math.max(a, Math.min(x - dragStartX, b)));
                this.invalidate();
            }
            else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                if (Math.abs(this.viewToDrag.getTranslationX()) >= this.getWidth() - this.getDraggedViewWidth() && motionEvent.getAction() == 1) {
                    this.listener.onDragComplete();
                }
                else {
                    this.listener.onDragCancel();
                    this.viewToDrag.animate().translationX(0.0f).setDuration(200L).start();
                    this.invalidate();
                    this.startAnimatingArrows();
                    this.dragging = false;
                }
            }
        }
        return this.dragging;
    }
    
    public boolean performAccessibilityAction(final int n, final Bundle bundle) {
        if (n == 16 && this.isEnabled()) {
            this.listener.onDragComplete();
        }
        return super.performAccessibilityAction(n, bundle);
    }
    
    public void reset() {
        if (this.arrowAnim != null) {
            if (!this.canceled) {
                this.listener.onDragCancel();
                this.viewToDrag.animate().translationX(0.0f).setDuration(200L).start();
                this.invalidate();
                this.startAnimatingArrows();
                this.dragging = false;
            }
        }
    }
    
    public void setColor(final int color) {
        this.pullBgPaint.setColor(color);
        this.pullBgPaint.setAlpha(178);
    }
    
    public void setListener(final Listener listener) {
        this.listener = listener;
    }
    
    public void setViewToDrag(final View viewToDrag, final boolean dragFromRight) {
        this.viewToDrag = viewToDrag;
        this.dragFromRight = dragFromRight;
        this.updateArrowPath();
    }
    
    public void startAnimatingArrows() {
        if (!this.animatingArrows) {
            final AnimatorSet arrowAnim = this.arrowAnim;
            if (arrowAnim != null) {
                this.animatingArrows = true;
                if (arrowAnim != null) {
                    arrowAnim.start();
                }
            }
        }
    }
    
    public void stopAnimatingArrows() {
        this.animatingArrows = false;
    }
    
    private class ArrowAnimWrapper
    {
        private int index;
        
        public ArrowAnimWrapper(final int index) {
            this.index = index;
        }
        
        public int getArrowAlpha() {
            return CallSwipeView.this.arrowAlphas[this.index];
        }
        
        public void setArrowAlpha(final int n) {
            CallSwipeView.this.arrowAlphas[this.index] = n;
        }
    }
    
    public interface Listener
    {
        void onDragCancel();
        
        void onDragComplete();
        
        void onDragStart();
    }
}
