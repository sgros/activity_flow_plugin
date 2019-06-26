package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.Animator.AnimatorListener;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.telegram.ui.Cells.DialogCell;

public class DialogsItemAnimator extends SimpleItemAnimator {
   private static final boolean DEBUG = false;
   private static final int changeDuration = 180;
   private static final int deleteDuration = 180;
   private static TimeInterpolator sDefaultInterpolator = new DecelerateInterpolator();
   private int bottomClip;
   ArrayList mAddAnimations = new ArrayList();
   ArrayList mAdditionsList = new ArrayList();
   ArrayList mChangeAnimations = new ArrayList();
   ArrayList mChangesList = new ArrayList();
   ArrayList mMoveAnimations = new ArrayList();
   ArrayList mMovesList = new ArrayList();
   private ArrayList mPendingAdditions = new ArrayList();
   private ArrayList mPendingChanges = new ArrayList();
   private ArrayList mPendingMoves = new ArrayList();
   private ArrayList mPendingRemovals = new ArrayList();
   ArrayList mRemoveAnimations = new ArrayList();
   private DialogCell removingDialog;
   private int topClip;

   private void animateRemoveImpl(final RecyclerView.ViewHolder var1) {
      final View var2 = var1.itemView;
      this.mRemoveAnimations.add(var1);
      if (var2 instanceof DialogCell) {
         final DialogCell var3 = (DialogCell)var2;
         this.removingDialog = var3;
         if (this.topClip != Integer.MAX_VALUE) {
            int var4 = this.removingDialog.getMeasuredHeight();
            int var5 = this.topClip;
            this.bottomClip = var4 - var5;
            this.removingDialog.setTopClip(var5);
            this.removingDialog.setBottomClip(this.bottomClip);
         } else if (this.bottomClip != Integer.MAX_VALUE) {
            this.topClip = this.removingDialog.getMeasuredHeight() - this.bottomClip;
            this.removingDialog.setTopClip(this.topClip);
            this.removingDialog.setBottomClip(this.bottomClip);
         }

         if (VERSION.SDK_INT >= 21) {
            var3.setElevation(-1.0F);
            var3.setOutlineProvider((ViewOutlineProvider)null);
         }

         ObjectAnimator var6 = ObjectAnimator.ofFloat(var3, AnimationProperties.CLIP_DIALOG_CELL_PROGRESS, new float[]{1.0F}).setDuration(180L);
         var6.setInterpolator(sDefaultInterpolator);
         var6.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1x) {
               var1x.removeAllListeners();
               var3.setClipProgress(0.0F);
               if (VERSION.SDK_INT >= 21) {
                  var3.setElevation(0.0F);
               }

               DialogsItemAnimator.this.dispatchRemoveFinished(var1);
               DialogsItemAnimator.this.mRemoveAnimations.remove(var1);
               DialogsItemAnimator.this.dispatchFinishedWhenDone();
            }

            public void onAnimationStart(Animator var1x) {
               DialogsItemAnimator.this.dispatchRemoveStarting(var1);
            }
         });
         var6.start();
      } else {
         final ViewPropertyAnimator var7 = var2.animate();
         var7.setDuration(180L).alpha(0.0F).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1x) {
               var7.setListener((AnimatorListener)null);
               var2.setAlpha(1.0F);
               DialogsItemAnimator.this.dispatchRemoveFinished(var1);
               DialogsItemAnimator.this.mRemoveAnimations.remove(var1);
               DialogsItemAnimator.this.dispatchFinishedWhenDone();
            }

            public void onAnimationStart(Animator var1x) {
               DialogsItemAnimator.this.dispatchRemoveStarting(var1);
            }
         }).start();
      }

   }

   private void endChangeAnimation(List var1, RecyclerView.ViewHolder var2) {
      for(int var3 = var1.size() - 1; var3 >= 0; --var3) {
         DialogsItemAnimator.ChangeInfo var4 = (DialogsItemAnimator.ChangeInfo)var1.get(var3);
         if (this.endChangeAnimationIfNecessary(var4, var2) && var4.oldHolder == null && var4.newHolder == null) {
            var1.remove(var4);
         }
      }

   }

   private void endChangeAnimationIfNecessary(DialogsItemAnimator.ChangeInfo var1) {
      RecyclerView.ViewHolder var2 = var1.oldHolder;
      if (var2 != null) {
         this.endChangeAnimationIfNecessary(var1, var2);
      }

      var2 = var1.newHolder;
      if (var2 != null) {
         this.endChangeAnimationIfNecessary(var1, var2);
      }

   }

   private boolean endChangeAnimationIfNecessary(DialogsItemAnimator.ChangeInfo var1, RecyclerView.ViewHolder var2) {
      RecyclerView.ViewHolder var3 = var1.newHolder;
      boolean var4 = false;
      if (var3 == var2) {
         var1.newHolder = null;
      } else {
         if (var1.oldHolder != var2) {
            return false;
         }

         var1.oldHolder = null;
         var4 = true;
      }

      var2.itemView.setAlpha(1.0F);
      var2.itemView.setTranslationX(0.0F);
      var2.itemView.setTranslationY(0.0F);
      this.dispatchChangeFinished(var2, var4);
      return true;
   }

   private void resetAnimation(RecyclerView.ViewHolder var1) {
      var1.itemView.animate().setInterpolator(sDefaultInterpolator);
      this.endAnimation(var1);
   }

   public boolean animateAdd(RecyclerView.ViewHolder var1) {
      this.resetAnimation(var1);
      View var2 = var1.itemView;
      if (var2 instanceof DialogCell) {
         ((DialogCell)var2).setClipProgress(1.0F);
      } else {
         var2.setAlpha(0.0F);
      }

      this.mPendingAdditions.add(var1);
      return true;
   }

   void animateAddImpl(final RecyclerView.ViewHolder var1) {
      final View var2 = var1.itemView;
      this.mAddAnimations.add(var1);
      if (var2 instanceof DialogCell) {
         final DialogCell var4 = (DialogCell)var2;
         ObjectAnimator var3 = ObjectAnimator.ofFloat(var4, AnimationProperties.CLIP_DIALOG_CELL_PROGRESS, new float[]{0.0F}).setDuration(180L);
         var3.setInterpolator(sDefaultInterpolator);
         var3.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1x) {
               var4.setClipProgress(0.0F);
            }

            public void onAnimationEnd(Animator var1x) {
               var1x.removeAllListeners();
               DialogsItemAnimator.this.dispatchAddFinished(var1);
               DialogsItemAnimator.this.mAddAnimations.remove(var1);
               DialogsItemAnimator.this.dispatchFinishedWhenDone();
            }

            public void onAnimationStart(Animator var1x) {
               DialogsItemAnimator.this.dispatchAddStarting(var1);
            }
         });
         var3.start();
      } else {
         final ViewPropertyAnimator var5 = var2.animate();
         var5.alpha(1.0F).setDuration(180L).setListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1x) {
               var2.setAlpha(1.0F);
            }

            public void onAnimationEnd(Animator var1x) {
               var5.setListener((AnimatorListener)null);
               DialogsItemAnimator.this.dispatchAddFinished(var1);
               DialogsItemAnimator.this.mAddAnimations.remove(var1);
               DialogsItemAnimator.this.dispatchFinishedWhenDone();
            }

            public void onAnimationStart(Animator var1x) {
               DialogsItemAnimator.this.dispatchAddStarting(var1);
            }
         }).start();
      }

   }

   public boolean animateChange(RecyclerView.ViewHolder var1, RecyclerView.ViewHolder var2, int var3, int var4, int var5, int var6) {
      if (var1.itemView instanceof DialogCell) {
         this.resetAnimation(var1);
         this.resetAnimation(var2);
         var1.itemView.setAlpha(1.0F);
         var2.itemView.setAlpha(0.0F);
         var2.itemView.setTranslationX(0.0F);
         this.mPendingChanges.add(new DialogsItemAnimator.ChangeInfo(var1, var2, var3, var4, var5, var6));
         return true;
      } else {
         return false;
      }
   }

   void animateChangeImpl(final DialogsItemAnimator.ChangeInfo var1) {
      final RecyclerView.ViewHolder var2 = var1.oldHolder;
      RecyclerView.ViewHolder var3 = var1.newHolder;
      if (var2 != null && var3 != null) {
         final AnimatorSet var4 = new AnimatorSet();
         var4.setDuration(180L);
         var4.playTogether(new Animator[]{ObjectAnimator.ofFloat(var2.itemView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(var3.itemView, View.ALPHA, new float[]{1.0F})});
         this.mChangeAnimations.add(var1.oldHolder);
         this.mChangeAnimations.add(var1.newHolder);
         var4.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1x) {
               var2.itemView.setAlpha(1.0F);
               var4.removeAllListeners();
               DialogsItemAnimator.this.dispatchChangeFinished(var1.oldHolder, true);
               DialogsItemAnimator.this.mChangeAnimations.remove(var1.oldHolder);
               DialogsItemAnimator.this.dispatchFinishedWhenDone();
               DialogsItemAnimator.this.dispatchChangeFinished(var1.newHolder, false);
               DialogsItemAnimator.this.mChangeAnimations.remove(var1.newHolder);
               DialogsItemAnimator.this.dispatchFinishedWhenDone();
            }

            public void onAnimationStart(Animator var1x) {
               DialogsItemAnimator.this.dispatchChangeStarting(var1.oldHolder, true);
               DialogsItemAnimator.this.dispatchChangeStarting(var1.newHolder, false);
            }
         });
         var4.start();
      }

   }

   public boolean animateMove(RecyclerView.ViewHolder var1, int var2, int var3, int var4, int var5) {
      View var6 = var1.itemView;
      var2 += (int)var6.getTranslationX();
      var3 += (int)var1.itemView.getTranslationY();
      this.resetAnimation(var1);
      int var7 = var4 - var2;
      int var8 = var5 - var3;
      if (var7 == 0 && var8 == 0) {
         this.dispatchMoveFinished(var1);
         return false;
      } else {
         if (var7 != 0) {
            var6.setTranslationX((float)(-var7));
         }

         if (var8 != 0) {
            var6.setTranslationY((float)(-var8));
         }

         this.mPendingMoves.add(new DialogsItemAnimator.MoveInfo(var1, var2, var3, var4, var5));
         return true;
      }
   }

   void animateMoveImpl(final RecyclerView.ViewHolder var1, final int var2, int var3, final int var4, int var5) {
      final View var6 = var1.itemView;
      var2 = var4 - var2;
      var4 = var5 - var3;
      if (var2 != 0) {
         var6.animate().translationX(0.0F);
      }

      if (var4 != 0) {
         var6.animate().translationY(0.0F);
      }

      if (var3 > var5) {
         this.bottomClip = var3 - var5;
      } else {
         this.topClip = var4;
      }

      DialogCell var7 = this.removingDialog;
      if (var7 != null) {
         if (this.topClip != Integer.MAX_VALUE) {
            var5 = var7.getMeasuredHeight();
            var3 = this.topClip;
            this.bottomClip = var5 - var3;
            this.removingDialog.setTopClip(var3);
            this.removingDialog.setBottomClip(this.bottomClip);
         } else if (this.bottomClip != Integer.MAX_VALUE) {
            this.topClip = var7.getMeasuredHeight() - this.bottomClip;
            this.removingDialog.setTopClip(this.topClip);
            this.removingDialog.setBottomClip(this.bottomClip);
         }
      }

      final ViewPropertyAnimator var8 = var6.animate();
      this.mMoveAnimations.add(var1);
      var8.setDuration(180L).setListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1x) {
            if (var2 != 0) {
               var6.setTranslationX(0.0F);
            }

            if (var4 != 0) {
               var6.setTranslationY(0.0F);
            }

         }

         public void onAnimationEnd(Animator var1x) {
            var8.setListener((AnimatorListener)null);
            DialogsItemAnimator.this.dispatchMoveFinished(var1);
            DialogsItemAnimator.this.mMoveAnimations.remove(var1);
            DialogsItemAnimator.this.dispatchFinishedWhenDone();
         }

         public void onAnimationStart(Animator var1x) {
            DialogsItemAnimator.this.dispatchMoveStarting(var1);
         }
      }).start();
   }

   public boolean animateRemove(RecyclerView.ViewHolder var1) {
      this.resetAnimation(var1);
      this.mPendingRemovals.add(var1);
      return true;
   }

   public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder var1, List var2) {
      return false;
   }

   void cancelAll(List var1) {
      for(int var2 = var1.size() - 1; var2 >= 0; --var2) {
         ((RecyclerView.ViewHolder)var1.get(var2)).itemView.animate().cancel();
      }

   }

   void dispatchFinishedWhenDone() {
      if (!this.isRunning()) {
         this.dispatchAnimationsFinished();
         this.onAllAnimationsDone();
      }

   }

   public void endAnimation(RecyclerView.ViewHolder var1) {
      View var2 = var1.itemView;
      var2.animate().cancel();

      int var3;
      for(var3 = this.mPendingMoves.size() - 1; var3 >= 0; --var3) {
         if (((DialogsItemAnimator.MoveInfo)this.mPendingMoves.get(var3)).holder == var1) {
            var2.setTranslationY(0.0F);
            var2.setTranslationX(0.0F);
            this.dispatchMoveFinished(var1);
            this.mPendingMoves.remove(var3);
         }
      }

      this.endChangeAnimation(this.mPendingChanges, var1);
      if (this.mPendingRemovals.remove(var1)) {
         if (var2 instanceof DialogCell) {
            ((DialogCell)var2).setClipProgress(0.0F);
         } else {
            var2.setAlpha(1.0F);
         }

         this.dispatchRemoveFinished(var1);
      }

      if (this.mPendingAdditions.remove(var1)) {
         if (var2 instanceof DialogCell) {
            ((DialogCell)var2).setClipProgress(0.0F);
         } else {
            var2.setAlpha(1.0F);
         }

         this.dispatchAddFinished(var1);
      }

      ArrayList var4;
      for(var3 = this.mChangesList.size() - 1; var3 >= 0; --var3) {
         var4 = (ArrayList)this.mChangesList.get(var3);
         this.endChangeAnimation(var4, var1);
         if (var4.isEmpty()) {
            this.mChangesList.remove(var3);
         }
      }

      for(var3 = this.mMovesList.size() - 1; var3 >= 0; --var3) {
         var4 = (ArrayList)this.mMovesList.get(var3);

         for(int var5 = var4.size() - 1; var5 >= 0; --var5) {
            if (((DialogsItemAnimator.MoveInfo)var4.get(var5)).holder == var1) {
               var2.setTranslationY(0.0F);
               var2.setTranslationX(0.0F);
               this.dispatchMoveFinished(var1);
               var4.remove(var5);
               if (var4.isEmpty()) {
                  this.mMovesList.remove(var3);
               }
               break;
            }
         }
      }

      for(var3 = this.mAdditionsList.size() - 1; var3 >= 0; --var3) {
         var4 = (ArrayList)this.mAdditionsList.get(var3);
         if (var4.remove(var1)) {
            if (var2 instanceof DialogCell) {
               ((DialogCell)var2).setClipProgress(1.0F);
            } else {
               var2.setAlpha(1.0F);
            }

            this.dispatchAddFinished(var1);
            if (var4.isEmpty()) {
               this.mAdditionsList.remove(var3);
            }
         }
      }

      this.mRemoveAnimations.remove(var1);
      this.mAddAnimations.remove(var1);
      this.mChangeAnimations.remove(var1);
      this.mMoveAnimations.remove(var1);
      this.dispatchFinishedWhenDone();
   }

   public void endAnimations() {
      int var1;
      View var3;
      for(var1 = this.mPendingMoves.size() - 1; var1 >= 0; --var1) {
         DialogsItemAnimator.MoveInfo var2 = (DialogsItemAnimator.MoveInfo)this.mPendingMoves.get(var1);
         var3 = var2.holder.itemView;
         var3.setTranslationY(0.0F);
         var3.setTranslationX(0.0F);
         this.dispatchMoveFinished(var2.holder);
         this.mPendingMoves.remove(var1);
      }

      for(var1 = this.mPendingRemovals.size() - 1; var1 >= 0; --var1) {
         this.dispatchRemoveFinished((RecyclerView.ViewHolder)this.mPendingRemovals.get(var1));
         this.mPendingRemovals.remove(var1);
      }

      View var6;
      for(var1 = this.mPendingAdditions.size() - 1; var1 >= 0; --var1) {
         RecyclerView.ViewHolder var8 = (RecyclerView.ViewHolder)this.mPendingAdditions.get(var1);
         var6 = var8.itemView;
         if (var6 instanceof DialogCell) {
            ((DialogCell)var6).setClipProgress(0.0F);
         } else {
            var6.setAlpha(1.0F);
         }

         this.dispatchAddFinished(var8);
         this.mPendingAdditions.remove(var1);
      }

      for(var1 = this.mPendingChanges.size() - 1; var1 >= 0; --var1) {
         this.endChangeAnimationIfNecessary((DialogsItemAnimator.ChangeInfo)this.mPendingChanges.get(var1));
      }

      this.mPendingChanges.clear();
      if (this.isRunning()) {
         int var4;
         ArrayList var7;
         for(var1 = this.mMovesList.size() - 1; var1 >= 0; --var1) {
            var7 = (ArrayList)this.mMovesList.get(var1);

            for(var4 = var7.size() - 1; var4 >= 0; --var4) {
               DialogsItemAnimator.MoveInfo var5 = (DialogsItemAnimator.MoveInfo)var7.get(var4);
               var3 = var5.holder.itemView;
               var3.setTranslationY(0.0F);
               var3.setTranslationX(0.0F);
               this.dispatchMoveFinished(var5.holder);
               var7.remove(var4);
               if (var7.isEmpty()) {
                  this.mMovesList.remove(var7);
               }
            }
         }

         for(var1 = this.mAdditionsList.size() - 1; var1 >= 0; --var1) {
            ArrayList var9 = (ArrayList)this.mAdditionsList.get(var1);

            for(var4 = var9.size() - 1; var4 >= 0; --var4) {
               RecyclerView.ViewHolder var10 = (RecyclerView.ViewHolder)var9.get(var4);
               var6 = var10.itemView;
               if (var6 instanceof DialogCell) {
                  ((DialogCell)var6).setClipProgress(0.0F);
               } else {
                  var6.setAlpha(1.0F);
               }

               this.dispatchAddFinished(var10);
               var9.remove(var4);
               if (var9.isEmpty()) {
                  this.mAdditionsList.remove(var9);
               }
            }
         }

         for(var1 = this.mChangesList.size() - 1; var1 >= 0; --var1) {
            var7 = (ArrayList)this.mChangesList.get(var1);

            for(var4 = var7.size() - 1; var4 >= 0; --var4) {
               this.endChangeAnimationIfNecessary((DialogsItemAnimator.ChangeInfo)var7.get(var4));
               if (var7.isEmpty()) {
                  this.mChangesList.remove(var7);
               }
            }
         }

         this.cancelAll(this.mRemoveAnimations);
         this.cancelAll(this.mMoveAnimations);
         this.cancelAll(this.mAddAnimations);
         this.cancelAll(this.mChangeAnimations);
         this.dispatchAnimationsFinished();
      }
   }

   public boolean isRunning() {
      boolean var1;
      if (this.mPendingAdditions.isEmpty() && this.mPendingChanges.isEmpty() && this.mPendingMoves.isEmpty() && this.mPendingRemovals.isEmpty() && this.mMoveAnimations.isEmpty() && this.mRemoveAnimations.isEmpty() && this.mAddAnimations.isEmpty() && this.mChangeAnimations.isEmpty() && this.mMovesList.isEmpty() && this.mAdditionsList.isEmpty() && this.mChangesList.isEmpty()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   // $FF: synthetic method
   public void lambda$runPendingAnimations$0$DialogsItemAnimator(ArrayList var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         DialogsItemAnimator.MoveInfo var3 = (DialogsItemAnimator.MoveInfo)var2.next();
         this.animateMoveImpl(var3.holder, var3.fromX, var3.fromY, var3.toX, var3.toY);
      }

      var1.clear();
      this.mMovesList.remove(var1);
   }

   // $FF: synthetic method
   public void lambda$runPendingAnimations$1$DialogsItemAnimator(ArrayList var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.animateChangeImpl((DialogsItemAnimator.ChangeInfo)var2.next());
      }

      var1.clear();
      this.mChangesList.remove(var1);
   }

   // $FF: synthetic method
   public void lambda$runPendingAnimations$2$DialogsItemAnimator(ArrayList var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.animateAddImpl((RecyclerView.ViewHolder)var2.next());
      }

      var1.clear();
      this.mAdditionsList.remove(var1);
   }

   protected void onAllAnimationsDone() {
   }

   public void prepareForRemove() {
      this.topClip = Integer.MAX_VALUE;
      this.bottomClip = Integer.MAX_VALUE;
      this.removingDialog = null;
   }

   public void runPendingAnimations() {
      boolean var1 = this.mPendingRemovals.isEmpty();
      boolean var2 = this.mPendingMoves.isEmpty() ^ true;
      boolean var3 = this.mPendingChanges.isEmpty() ^ true;
      boolean var4 = this.mPendingAdditions.isEmpty() ^ true;
      if (var1 ^ true || var2 || var4 || var3) {
         Iterator var5 = this.mPendingRemovals.iterator();

         while(var5.hasNext()) {
            this.animateRemoveImpl((RecyclerView.ViewHolder)var5.next());
         }

         this.mPendingRemovals.clear();
         ArrayList var6;
         if (var2) {
            var6 = new ArrayList(this.mPendingMoves);
            this.mMovesList.add(var6);
            this.mPendingMoves.clear();
            (new _$$Lambda$DialogsItemAnimator$FzJ8o5Mz2rO6C7ZkKkUswV9PN8U(this, var6)).run();
         }

         if (var3) {
            var6 = new ArrayList(this.mPendingChanges);
            this.mChangesList.add(var6);
            this.mPendingChanges.clear();
            (new _$$Lambda$DialogsItemAnimator$cc5l3oNCPWmFl8oTuclZaKvqaQ8(this, var6)).run();
         }

         if (var4) {
            var6 = new ArrayList(this.mPendingAdditions);
            this.mAdditionsList.add(var6);
            this.mPendingAdditions.clear();
            (new _$$Lambda$DialogsItemAnimator$zsvBbBTBPz9JWwcIM9KIgALVodc(this, var6)).run();
         }

      }
   }

   private static class ChangeInfo {
      public int fromX;
      public int fromY;
      public RecyclerView.ViewHolder newHolder;
      public RecyclerView.ViewHolder oldHolder;
      public int toX;
      public int toY;

      private ChangeInfo(RecyclerView.ViewHolder var1, RecyclerView.ViewHolder var2) {
         this.oldHolder = var1;
         this.newHolder = var2;
      }

      ChangeInfo(RecyclerView.ViewHolder var1, RecyclerView.ViewHolder var2, int var3, int var4, int var5, int var6) {
         this(var1, var2);
         this.fromX = var3;
         this.fromY = var4;
         this.toX = var5;
         this.toY = var6;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("ChangeInfo{oldHolder=");
         var1.append(this.oldHolder);
         var1.append(", newHolder=");
         var1.append(this.newHolder);
         var1.append(", fromX=");
         var1.append(this.fromX);
         var1.append(", fromY=");
         var1.append(this.fromY);
         var1.append(", toX=");
         var1.append(this.toX);
         var1.append(", toY=");
         var1.append(this.toY);
         var1.append('}');
         return var1.toString();
      }
   }

   private static class MoveInfo {
      public int fromX;
      public int fromY;
      public RecyclerView.ViewHolder holder;
      public int toX;
      public int toY;

      MoveInfo(RecyclerView.ViewHolder var1, int var2, int var3, int var4, int var5) {
         this.holder = var1;
         this.fromX = var2;
         this.fromY = var3;
         this.toX = var4;
         this.toY = var5;
      }
   }
}
