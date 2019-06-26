package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;

public class HintView extends FrameLayout {
   private AnimatorSet animatorSet;
   private ImageView arrowImageView;
   private int currentType;
   private Runnable hideRunnable;
   private ImageView imageView;
   private boolean isTopArrow;
   private ChatMessageCell messageCell;
   private String overrideText;
   private TextView textView;

   public HintView(Context var1, int var2) {
      this(var1, var2, false);
   }

   public HintView(Context var1, int var2, boolean var3) {
      super(var1);
      this.currentType = var2;
      this.isTopArrow = var3;
      this.textView = new CorrectlyMeasuringTextView(var1);
      this.textView.setTextColor(Theme.getColor("chat_gifSaveHintText"));
      this.textView.setTextSize(1, 14.0F);
      this.textView.setMaxLines(2);
      this.textView.setMaxWidth(AndroidUtilities.dp(250.0F));
      this.textView.setGravity(51);
      this.textView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0F), Theme.getColor("chat_gifSaveHintBackground")));
      TextView var4 = this.textView;
      float var5;
      if (this.currentType == 0) {
         var5 = 54.0F;
      } else {
         var5 = 5.0F;
      }

      var4.setPadding(AndroidUtilities.dp(var5), AndroidUtilities.dp(6.0F), AndroidUtilities.dp(5.0F), AndroidUtilities.dp(7.0F));
      var4 = this.textView;
      if (var3) {
         var5 = 6.0F;
      } else {
         var5 = 0.0F;
      }

      float var6;
      if (var3) {
         var6 = 0.0F;
      } else {
         var6 = 6.0F;
      }

      this.addView(var4, LayoutHelper.createFrame(-2, -2.0F, 51, 0.0F, var5, 0.0F, var6));
      if (var2 == 0) {
         this.textView.setText(LocaleController.getString("AutoplayVideoInfo", 2131558806));
         this.imageView = new ImageView(var1);
         this.imageView.setImageResource(2131165890);
         this.imageView.setScaleType(ScaleType.CENTER);
         this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_gifSaveHintText"), Mode.MULTIPLY));
         this.addView(this.imageView, LayoutHelper.createFrame(38, 34.0F, 51, 7.0F, 7.0F, 0.0F, 0.0F));
      }

      this.arrowImageView = new ImageView(var1);
      ImageView var7 = this.arrowImageView;
      if (var3) {
         var2 = 2131165887;
      } else {
         var2 = 2131165886;
      }

      var7.setImageResource(var2);
      this.arrowImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_gifSaveHintBackground"), Mode.MULTIPLY));
      var7 = this.arrowImageView;
      byte var8;
      if (var3) {
         var8 = 48;
      } else {
         var8 = 80;
      }

      this.addView(var7, LayoutHelper.createFrame(14, 6.0F, var8 | 3, 0.0F, 0.0F, 0.0F, 0.0F));
   }

   public ChatMessageCell getMessageCell() {
      return this.messageCell;
   }

   public void hide() {
      if (this.getTag() != null) {
         this.setTag((Object)null);
         Runnable var1 = this.hideRunnable;
         if (var1 != null) {
            AndroidUtilities.cancelRunOnUIThread(var1);
            this.hideRunnable = null;
         }

         AnimatorSet var2 = this.animatorSet;
         if (var2 != null) {
            var2.cancel();
            this.animatorSet = null;
         }

         this.animatorSet = new AnimatorSet();
         this.animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "alpha", new float[]{0.0F})});
         this.animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               HintView.this.setVisibility(4);
               HintView.this.messageCell = null;
               HintView.this.animatorSet = null;
            }
         });
         this.animatorSet.setDuration(300L);
         this.animatorSet.start();
      }
   }

   public void setOverrideText(String var1) {
      this.overrideText = var1;
      this.textView.setText(var1);
      ChatMessageCell var2 = this.messageCell;
      if (var2 != null) {
         this.messageCell = null;
         this.showForMessageCell(var2, false);
      }

   }

   public boolean showForMessageCell(ChatMessageCell var1, boolean var2) {
      if ((this.currentType != 0 || this.getTag() == null) && this.messageCell != var1) {
         Runnable var3 = this.hideRunnable;
         if (var3 != null) {
            AndroidUtilities.cancelRunOnUIThread(var3);
            this.hideRunnable = null;
         }

         int var4 = var1.getTop();
         View var13 = (View)var1.getParent();
         int var6;
         int var7;
         if (this.currentType == 0) {
            ImageReceiver var14 = var1.getPhotoImage();
            var4 += var14.getImageY();
            var6 = var14.getImageHeight();
            var7 = var13.getMeasuredHeight();
            if (var4 <= this.getMeasuredHeight() + AndroidUtilities.dp(10.0F) || var4 + var6 > var7 + var6 / 4) {
               return false;
            }

            var7 = var1.getNoSoundIconCenterX();
         } else {
            MessageObject var5 = var1.getMessageObject();
            String var8 = this.overrideText;
            if (var8 == null) {
               this.textView.setText(LocaleController.getString("HidAccount", 2131559635));
            } else {
               this.textView.setText(var8);
            }

            this.measure(MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE));
            var7 = var4 + AndroidUtilities.dp(22.0F);
            var4 = var7;
            if (!var5.isOutOwner()) {
               var4 = var7;
               if (var1.isDrawNameLayout()) {
                  var4 = var7 + AndroidUtilities.dp(20.0F);
               }
            }

            if (!this.isTopArrow && var4 <= this.getMeasuredHeight() + AndroidUtilities.dp(10.0F)) {
               return false;
            }

            var7 = var1.getForwardNameCenterX();
         }

         int var9 = var13.getMeasuredWidth();
         if (this.isTopArrow) {
            this.setTranslationY((float)AndroidUtilities.dp(44.0F));
         } else {
            this.setTranslationY((float)(var4 - this.getMeasuredHeight()));
         }

         var6 = var1.getLeft() + var7;
         var4 = AndroidUtilities.dp(19.0F);
         if (var6 > var13.getMeasuredWidth() / 2) {
            var9 = var9 - this.getMeasuredWidth() - AndroidUtilities.dp(38.0F);
            this.setTranslationX((float)var9);
            var4 += var9;
         } else {
            this.setTranslationX(0.0F);
         }

         float var10 = (float)(var1.getLeft() + var7 - var4 - this.arrowImageView.getMeasuredWidth() / 2);
         this.arrowImageView.setTranslationX(var10);
         float var11;
         if (var6 > var13.getMeasuredWidth() / 2) {
            if (var10 < (float)AndroidUtilities.dp(10.0F)) {
               var11 = var10 - (float)AndroidUtilities.dp(10.0F);
               this.setTranslationX(this.getTranslationX() + var11);
               this.arrowImageView.setTranslationX(var10 - var11);
            }
         } else if (var10 > (float)(this.getMeasuredWidth() - AndroidUtilities.dp(24.0F))) {
            var11 = var10 - (float)this.getMeasuredWidth() + (float)AndroidUtilities.dp(24.0F);
            this.setTranslationX(var11);
            this.arrowImageView.setTranslationX(var10 - var11);
         } else if (var10 < (float)AndroidUtilities.dp(10.0F)) {
            var11 = var10 - (float)AndroidUtilities.dp(10.0F);
            this.setTranslationX(this.getTranslationX() + var11);
            this.arrowImageView.setTranslationX(var10 - var11);
         }

         this.messageCell = var1;
         AnimatorSet var12 = this.animatorSet;
         if (var12 != null) {
            var12.cancel();
            this.animatorSet = null;
         }

         this.setTag(1);
         this.setVisibility(0);
         if (var2) {
            this.animatorSet = new AnimatorSet();
            this.animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "alpha", new float[]{0.0F, 1.0F})});
            this.animatorSet.addListener(new AnimatorListenerAdapter() {
               // $FF: synthetic method
               public void lambda$onAnimationEnd$0$HintView$1() {
                  HintView.this.hide();
               }

               public void onAnimationEnd(Animator var1) {
                  HintView.this.animatorSet = null;
                  HintView var5 = HintView.this;
                  _$$Lambda$HintView$1$Oo_YArBkq6553J0682j2MQqGlbY var2 = new _$$Lambda$HintView$1$Oo_YArBkq6553J0682j2MQqGlbY(this);
                  var5.hideRunnable = var2;
                  long var3;
                  if (HintView.this.currentType == 0) {
                     var3 = 10000L;
                  } else {
                     var3 = 2000L;
                  }

                  AndroidUtilities.runOnUIThread(var2, var3);
               }
            });
            this.animatorSet.setDuration(300L);
            this.animatorSet.start();
         } else {
            this.setAlpha(1.0F);
         }

         return true;
      } else {
         return false;
      }
   }
}
