package org.telegram.ui.Components.voip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

public class CallSwipeView extends View {
   private boolean animatingArrows = false;
   private Path arrow = new Path();
   private int[] arrowAlphas = new int[]{64, 64, 64};
   private AnimatorSet arrowAnim;
   private Paint arrowsPaint;
   private boolean canceled = false;
   private boolean dragFromRight;
   private float dragStartX;
   private boolean dragging = false;
   private CallSwipeView.Listener listener;
   private Paint pullBgPaint;
   private RectF tmpRect = new RectF();
   private View viewToDrag;

   public CallSwipeView(Context var1) {
      super(var1);
      this.init();
   }

   private int getDraggedViewWidth() {
      return this.getHeight();
   }

   private void init() {
      this.arrowsPaint = new Paint(1);
      this.arrowsPaint.setColor(-1);
      this.arrowsPaint.setStyle(Style.STROKE);
      this.arrowsPaint.setStrokeWidth((float)AndroidUtilities.dp(2.5F));
      this.pullBgPaint = new Paint(1);
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.arrowAlphas.length; ++var2) {
         ObjectAnimator var3 = ObjectAnimator.ofInt(new CallSwipeView.ArrowAnimWrapper(var2), "arrowAlpha", new int[]{64, 255, 64});
         var3.setDuration(700L);
         var3.setStartDelay((long)(var2 * 200));
         var1.add(var3);
      }

      this.arrowAnim = new AnimatorSet();
      this.arrowAnim.playTogether(var1);
      this.arrowAnim.addListener(new AnimatorListenerAdapter() {
         private Runnable restarter = new Runnable() {
            public void run() {
               if (CallSwipeView.this.arrowAnim != null) {
                  CallSwipeView.this.arrowAnim.start();
               }

            }
         };
         private long startTime;

         public void onAnimationCancel(Animator var1) {
            CallSwipeView.this.canceled = true;
         }

         public void onAnimationEnd(Animator var1) {
            if (System.currentTimeMillis() - this.startTime < var1.getDuration() / 4L) {
               if (BuildVars.LOGS_ENABLED) {
                  FileLog.w("Not repeating animation because previous loop was too fast");
               }

            } else {
               if (!CallSwipeView.this.canceled && CallSwipeView.this.animatingArrows) {
                  CallSwipeView.this.post(this.restarter);
               }

            }
         }

         public void onAnimationStart(Animator var1) {
            this.startTime = System.currentTimeMillis();
         }
      });
   }

   private void updateArrowPath() {
      this.arrow.reset();
      int var1 = AndroidUtilities.dp(6.0F);
      Path var2;
      float var3;
      if (this.dragFromRight) {
         var2 = this.arrow;
         var3 = (float)var1;
         var2.moveTo(var3, (float)(-var1));
         this.arrow.lineTo(0.0F, 0.0F);
         this.arrow.lineTo(var3, var3);
      } else {
         this.arrow.moveTo(0.0F, (float)(-var1));
         var2 = this.arrow;
         var3 = (float)var1;
         var2.lineTo(var3, 0.0F);
         this.arrow.lineTo(0.0F, var3);
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      AnimatorSet var1 = this.arrowAnim;
      if (var1 != null) {
         this.canceled = true;
         var1.cancel();
         this.arrowAnim = null;
      }

   }

   protected void onDraw(Canvas var1) {
      if (this.viewToDrag.getTranslationX() != 0.0F) {
         if (this.dragFromRight) {
            this.tmpRect.set((float)this.getWidth() + this.viewToDrag.getTranslationX() - (float)this.getDraggedViewWidth(), 0.0F, (float)this.getWidth(), (float)this.getHeight());
         } else {
            this.tmpRect.set(0.0F, 0.0F, this.viewToDrag.getTranslationX() + (float)this.getDraggedViewWidth(), (float)this.getHeight());
         }

         var1.drawRoundRect(this.tmpRect, (float)(this.getHeight() / 2), (float)(this.getHeight() / 2), this.pullBgPaint);
      }

      var1.save();
      if (this.dragFromRight) {
         var1.translate((float)(this.getWidth() - this.getHeight() - AndroidUtilities.dp(18.0F)), (float)(this.getHeight() / 2));
      } else {
         var1.translate((float)(this.getHeight() + AndroidUtilities.dp(12.0F)), (float)(this.getHeight() / 2));
      }

      float var2 = Math.abs(this.viewToDrag.getTranslationX());

      for(int var3 = 0; var3 < 3; ++var3) {
         float var4 = (float)AndroidUtilities.dp((float)(var3 * 16));
         float var5 = 16.0F;
         float var6 = 1.0F;
         if (var2 > var4) {
            var6 = 1.0F - Math.min(1.0F, Math.max(0.0F, (var2 - (float)(AndroidUtilities.dp(16.0F) * var3)) / (float)AndroidUtilities.dp(16.0F)));
         }

         this.arrowsPaint.setAlpha(Math.round((float)this.arrowAlphas[var3] * var6));
         var1.drawPath(this.arrow, this.arrowsPaint);
         var6 = var5;
         if (this.dragFromRight) {
            var6 = -16.0F;
         }

         var1.translate((float)AndroidUtilities.dp(var6), 0.0F);
      }

      var1.restore();
      this.invalidate();
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.addAction(16);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (!this.isEnabled()) {
         return false;
      } else {
         if (var1.getAction() == 0) {
            if (!this.dragFromRight && var1.getX() < (float)this.getDraggedViewWidth() || this.dragFromRight && var1.getX() > (float)(this.getWidth() - this.getDraggedViewWidth())) {
               this.dragging = true;
               this.dragStartX = var1.getX();
               this.getParent().requestDisallowInterceptTouchEvent(true);
               this.listener.onDragStart();
               this.stopAnimatingArrows();
            }
         } else {
            int var2 = var1.getAction();
            float var3 = 0.0F;
            if (var2 == 2) {
               View var4 = this.viewToDrag;
               float var5;
               if (this.dragFromRight) {
                  var5 = (float)(-(this.getWidth() - this.getDraggedViewWidth()));
               } else {
                  var5 = 0.0F;
               }

               float var6 = var1.getX();
               float var7 = this.dragStartX;
               if (!this.dragFromRight) {
                  var3 = (float)(this.getWidth() - this.getDraggedViewWidth());
               }

               var4.setTranslationX(Math.max(var5, Math.min(var6 - var7, var3)));
               this.invalidate();
            } else if (var1.getAction() == 1 || var1.getAction() == 3) {
               if (Math.abs(this.viewToDrag.getTranslationX()) >= (float)(this.getWidth() - this.getDraggedViewWidth()) && var1.getAction() == 1) {
                  this.listener.onDragComplete();
               } else {
                  this.listener.onDragCancel();
                  this.viewToDrag.animate().translationX(0.0F).setDuration(200L).start();
                  this.invalidate();
                  this.startAnimatingArrows();
                  this.dragging = false;
               }
            }
         }

         return this.dragging;
      }
   }

   public boolean performAccessibilityAction(int var1, Bundle var2) {
      if (var1 == 16 && this.isEnabled()) {
         this.listener.onDragComplete();
      }

      return super.performAccessibilityAction(var1, var2);
   }

   public void reset() {
      if (this.arrowAnim != null && !this.canceled) {
         this.listener.onDragCancel();
         this.viewToDrag.animate().translationX(0.0F).setDuration(200L).start();
         this.invalidate();
         this.startAnimatingArrows();
         this.dragging = false;
      }

   }

   public void setColor(int var1) {
      this.pullBgPaint.setColor(var1);
      this.pullBgPaint.setAlpha(178);
   }

   public void setListener(CallSwipeView.Listener var1) {
      this.listener = var1;
   }

   public void setViewToDrag(View var1, boolean var2) {
      this.viewToDrag = var1;
      this.dragFromRight = var2;
      this.updateArrowPath();
   }

   public void startAnimatingArrows() {
      if (!this.animatingArrows) {
         AnimatorSet var1 = this.arrowAnim;
         if (var1 != null) {
            this.animatingArrows = true;
            if (var1 != null) {
               var1.start();
            }
         }
      }

   }

   public void stopAnimatingArrows() {
      this.animatingArrows = false;
   }

   private class ArrowAnimWrapper {
      private int index;

      public ArrowAnimWrapper(int var2) {
         this.index = var2;
      }

      public int getArrowAlpha() {
         return CallSwipeView.this.arrowAlphas[this.index];
      }

      public void setArrowAlpha(int var1) {
         CallSwipeView.this.arrowAlphas[this.index] = var1;
      }
   }

   public interface Listener {
      void onDragCancel();

      void onDragComplete();

      void onDragStart();
   }
}
