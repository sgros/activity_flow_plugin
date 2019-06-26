package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.os.SystemClock;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;

public class UndoView extends FrameLayout {
   public static final int ACTION_ARCHIVE = 2;
   public static final int ACTION_ARCHIVE_FEW = 4;
   public static final int ACTION_ARCHIVE_FEW_HINT = 5;
   public static final int ACTION_ARCHIVE_HIDDEN = 6;
   public static final int ACTION_ARCHIVE_HINT = 3;
   public static final int ACTION_ARCHIVE_PINNED = 7;
   public static final int ACTION_CLEAR = 0;
   public static final int ACTION_DELETE = 1;
   private int currentAccount;
   private int currentAction;
   private Runnable currentActionRunnable;
   private Runnable currentCancelRunnable;
   private long currentDialogId;
   private TextView infoTextView;
   private boolean isShowed;
   private long lastUpdateTime;
   private LottieAnimationView leftImageView;
   private int prevSeconds;
   private Paint progressPaint;
   private RectF rect;
   private TextView subinfoTextView;
   private TextPaint textPaint;
   private int textWidth;
   private long timeLeft;
   private String timeLeftString;
   private LinearLayout undoButton;
   private ImageView undoImageView;
   private TextView undoTextView;

   public UndoView(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.infoTextView = new TextView(var1);
      this.infoTextView.setTextSize(1, 15.0F);
      this.infoTextView.setTextColor(Theme.getColor("undo_infoColor"));
      this.addView(this.infoTextView, LayoutHelper.createFrame(-2, -2.0F, 51, 45.0F, 13.0F, 0.0F, 0.0F));
      this.subinfoTextView = new TextView(var1);
      this.subinfoTextView.setTextSize(1, 13.0F);
      this.subinfoTextView.setTextColor(Theme.getColor("undo_infoColor"));
      this.subinfoTextView.setSingleLine(true);
      this.subinfoTextView.setEllipsize(TruncateAt.END);
      this.addView(this.subinfoTextView, LayoutHelper.createFrame(-2, -2.0F, 51, 58.0F, 27.0F, 8.0F, 0.0F));
      this.leftImageView = new LottieAnimationView(var1);
      this.leftImageView.setScaleType(ScaleType.CENTER);
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"info1", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_background") | -16777216)));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"info2", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_background") | -16777216)));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"luc12", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"luc11", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"luc10", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"luc9", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"luc8", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"luc7", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"luc6", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"luc5", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"luc4", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"luc3", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"luc2", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"luc1", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
      this.leftImageView.addValueCallback(new KeyPath(new String[]{"Oval", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
      this.addView(this.leftImageView, LayoutHelper.createFrame(54, -2.0F, 19, 3.0F, 0.0F, 0.0F, 0.0F));
      this.undoButton = new LinearLayout(var1);
      this.undoButton.setOrientation(0);
      this.addView(this.undoButton, LayoutHelper.createFrame(-2, -1.0F, 21, 0.0F, 0.0F, 19.0F, 0.0F));
      this.undoButton.setOnClickListener(new _$$Lambda$UndoView$ut_O3jMsR3UxcWCuvOqjPzYJ4Go(this));
      this.undoImageView = new ImageView(var1);
      this.undoImageView.setImageResource(2131165353);
      this.undoImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("undo_cancelColor"), Mode.MULTIPLY));
      this.undoButton.addView(this.undoImageView, LayoutHelper.createLinear(-2, -2, 19));
      this.undoTextView = new TextView(var1);
      this.undoTextView.setTextSize(1, 14.0F);
      this.undoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.undoTextView.setTextColor(Theme.getColor("undo_cancelColor"));
      this.undoTextView.setText(LocaleController.getString("Undo", 2131560934));
      this.undoButton.addView(this.undoTextView, LayoutHelper.createLinear(-2, -2, 19, 6, 0, 0, 0));
      this.rect = new RectF((float)AndroidUtilities.dp(15.0F), (float)AndroidUtilities.dp(15.0F), (float)AndroidUtilities.dp(33.0F), (float)AndroidUtilities.dp(33.0F));
      this.progressPaint = new Paint(1);
      this.progressPaint.setStyle(Style.STROKE);
      this.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.progressPaint.setStrokeCap(Cap.ROUND);
      this.progressPaint.setColor(Theme.getColor("undo_infoColor"));
      this.textPaint = new TextPaint(1);
      this.textPaint.setTextSize((float)AndroidUtilities.dp(12.0F));
      this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.textPaint.setColor(Theme.getColor("undo_infoColor"));
      this.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0F), Theme.getColor("undo_background")));
      this.setOnTouchListener(_$$Lambda$UndoView$cqEu5tq8BrTwZKHKJIY3aTuNIjk.INSTANCE);
      this.setVisibility(4);
   }

   private boolean isTooltipAction() {
      int var1 = this.currentAction;
      boolean var2;
      if (var1 != 6 && var1 != 3 && var1 != 5 && var1 != 7) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   // $FF: synthetic method
   static boolean lambda$new$1(View var0, MotionEvent var1) {
      return true;
   }

   protected boolean canUndo() {
      return true;
   }

   public void hide(boolean var1, int var2) {
      if (this.getVisibility() == 0 && this.isShowed) {
         this.isShowed = false;
         Runnable var3 = this.currentActionRunnable;
         if (var3 != null) {
            if (var1) {
               var3.run();
            }

            this.currentActionRunnable = null;
         }

         var3 = this.currentCancelRunnable;
         if (var3 != null) {
            if (!var1) {
               var3.run();
            }

            this.currentCancelRunnable = null;
         }

         int var4 = this.currentAction;
         if (var4 == 0 || var4 == 1) {
            MessagesController var9 = MessagesController.getInstance(this.currentAccount);
            long var5 = this.currentDialogId;
            boolean var7;
            if (this.currentAction == 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            var9.removeDialogAction(var5, var7, var1);
         }

         byte var11 = 52;
         if (var2 != 0) {
            AnimatorSet var10 = new AnimatorSet();
            if (var2 == 1) {
               Property var8 = View.TRANSLATION_Y;
               if (!this.isTooltipAction()) {
                  var11 = 48;
               }

               var10.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, var8, new float[]{(float)AndroidUtilities.dp((float)(var11 + 8))})});
               var10.setDuration(250L);
            } else {
               var10.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, View.SCALE_X, new float[]{0.8F}), ObjectAnimator.ofFloat(this, View.SCALE_Y, new float[]{0.8F}), ObjectAnimator.ofFloat(this, View.ALPHA, new float[]{0.0F})});
               var10.setDuration(180L);
            }

            var10.setInterpolator(new DecelerateInterpolator());
            var10.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  UndoView.this.setVisibility(4);
                  UndoView.this.setScaleX(1.0F);
                  UndoView.this.setScaleY(1.0F);
                  UndoView.this.setAlpha(1.0F);
               }
            });
            var10.start();
         } else {
            if (!this.isTooltipAction()) {
               var11 = 48;
            }

            this.setTranslationY((float)AndroidUtilities.dp((float)(var11 + 8)));
            this.setVisibility(4);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$new$0$UndoView(View var1) {
      if (this.canUndo()) {
         this.hide(false, 1);
      }
   }

   protected void onDraw(Canvas var1) {
      int var2 = this.currentAction;
      long var3;
      if (var2 == 1 || var2 == 0) {
         var3 = this.timeLeft;
         if (var3 > 0L) {
            var2 = (int)Math.ceil((double)((float)var3 / 1000.0F));
         } else {
            var2 = 0;
         }

         if (this.prevSeconds != var2) {
            this.prevSeconds = var2;
            this.timeLeftString = String.format("%d", Math.max(1, var2));
            this.textWidth = (int)Math.ceil((double)this.textPaint.measureText(this.timeLeftString));
         }

         var1.drawText(this.timeLeftString, this.rect.centerX() - (float)(this.textWidth / 2), (float)AndroidUtilities.dp(28.2F), this.textPaint);
         var1.drawArc(this.rect, -90.0F, (float)this.timeLeft / 5000.0F * -360.0F, false, this.progressPaint);
      }

      long var5 = SystemClock.uptimeMillis();
      var3 = this.lastUpdateTime;
      this.timeLeft -= var5 - var3;
      this.lastUpdateTime = var5;
      if (this.timeLeft <= 0L) {
         this.hide(true, 1);
      }

      this.invalidate();
   }

   protected void onMeasure(int var1, int var2) {
      var1 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824);
      float var3;
      if (this.isTooltipAction()) {
         var3 = 52.0F;
      } else {
         var3 = 48.0F;
      }

      super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(var3), 1073741824));
   }

   public void showWithAction(long var1, int var3, Runnable var4) {
      this.showWithAction(var1, var3, var4, (Runnable)null);
   }

   public void showWithAction(long var1, int var3, Runnable var4, Runnable var5) {
      Runnable var6 = this.currentActionRunnable;
      if (var6 != null) {
         var6.run();
      }

      this.isShowed = true;
      this.currentActionRunnable = var4;
      this.currentCancelRunnable = var5;
      this.currentDialogId = var1;
      this.currentAction = var3;
      this.timeLeft = 5000L;
      this.lastUpdateTime = SystemClock.uptimeMillis();
      boolean var8;
      LayoutParams var10;
      if (this.isTooltipAction()) {
         if (var3 == 6) {
            this.infoTextView.setText(LocaleController.getString("ArchiveHidden", 2131558643));
            this.subinfoTextView.setText(LocaleController.getString("ArchiveHiddenInfo", 2131558644));
            this.leftImageView.setAnimation(2131492869);
         } else if (var3 == 7) {
            this.infoTextView.setText(LocaleController.getString("ArchivePinned", 2131558651));
            this.subinfoTextView.setText(LocaleController.getString("ArchivePinnedInfo", 2131558652));
            this.leftImageView.setAnimation(2131492868);
         } else {
            if (var3 == 3) {
               this.infoTextView.setText(LocaleController.getString("ChatArchived", 2131559022));
            } else {
               this.infoTextView.setText(LocaleController.getString("ChatsArchived", 2131559052));
            }

            this.subinfoTextView.setText(LocaleController.getString("ChatArchivedInfo", 2131559023));
            this.leftImageView.setAnimation(2131492868);
         }

         var10 = (LayoutParams)this.infoTextView.getLayoutParams();
         var10.leftMargin = AndroidUtilities.dp(58.0F);
         var10.topMargin = AndroidUtilities.dp(6.0F);
         this.infoTextView.setTextSize(1, 14.0F);
         this.infoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.subinfoTextView.setVisibility(0);
         this.undoButton.setVisibility(8);
         this.leftImageView.setVisibility(0);
         this.leftImageView.setProgress(0.0F);
         this.leftImageView.playAnimation();
      } else {
         int var7 = this.currentAction;
         if (var7 != 2 && var7 != 4) {
            var10 = (LayoutParams)this.infoTextView.getLayoutParams();
            var10.leftMargin = AndroidUtilities.dp(45.0F);
            var10.topMargin = AndroidUtilities.dp(13.0F);
            this.infoTextView.setTextSize(1, 15.0F);
            this.undoButton.setVisibility(0);
            this.infoTextView.setTypeface(Typeface.DEFAULT);
            this.subinfoTextView.setVisibility(8);
            this.leftImageView.setVisibility(8);
            if (this.currentAction == 0) {
               this.infoTextView.setText(LocaleController.getString("HistoryClearedUndo", 2131559640));
            } else {
               var3 = (int)var1;
               if (var3 < 0) {
                  TLRPC.Chat var12 = MessagesController.getInstance(this.currentAccount).getChat(-var3);
                  if (ChatObject.isChannel(var12) && !var12.megagroup) {
                     this.infoTextView.setText(LocaleController.getString("ChannelDeletedUndo", 2131558948));
                  } else {
                     this.infoTextView.setText(LocaleController.getString("GroupDeletedUndo", 2131559602));
                  }
               } else {
                  this.infoTextView.setText(LocaleController.getString("ChatDeletedUndo", 2131559026));
               }
            }

            MessagesController var13 = MessagesController.getInstance(this.currentAccount);
            if (this.currentAction == 0) {
               var8 = true;
            } else {
               var8 = false;
            }

            var13.addDialogAction(var1, var8);
         } else {
            if (var3 == 2) {
               this.infoTextView.setText(LocaleController.getString("ChatArchived", 2131559022));
            } else {
               this.infoTextView.setText(LocaleController.getString("ChatsArchived", 2131559052));
            }

            var10 = (LayoutParams)this.infoTextView.getLayoutParams();
            var10.leftMargin = AndroidUtilities.dp(58.0F);
            var10.topMargin = AndroidUtilities.dp(13.0F);
            this.infoTextView.setTextSize(1, 15.0F);
            this.undoButton.setVisibility(0);
            this.infoTextView.setTypeface(Typeface.DEFAULT);
            this.subinfoTextView.setVisibility(8);
            this.leftImageView.setVisibility(0);
            this.leftImageView.setAnimation(2131492866);
            this.leftImageView.setProgress(0.0F);
            this.leftImageView.playAnimation();
         }
      }

      StringBuilder var11 = new StringBuilder();
      var11.append(this.infoTextView.getText());
      String var16;
      if (this.subinfoTextView.getVisibility() == 0) {
         StringBuilder var15 = new StringBuilder();
         var15.append(". ");
         var15.append(this.subinfoTextView.getText());
         var16 = var15.toString();
      } else {
         var16 = "";
      }

      var11.append(var16);
      AndroidUtilities.makeAccessibilityAnnouncement(var11.toString());
      if (this.getVisibility() != 0) {
         this.setVisibility(0);
         var8 = this.isTooltipAction();
         byte var17 = 52;
         byte var9;
         if (var8) {
            var9 = 52;
         } else {
            var9 = 48;
         }

         this.setTranslationY((float)AndroidUtilities.dp((float)(var9 + 8)));
         AnimatorSet var14 = new AnimatorSet();
         Property var18 = View.TRANSLATION_Y;
         if (this.isTooltipAction()) {
            var9 = var17;
         } else {
            var9 = 48;
         }

         var14.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, var18, new float[]{(float)AndroidUtilities.dp((float)(var9 + 8)), 0.0F})});
         var14.setInterpolator(new DecelerateInterpolator());
         var14.setDuration(180L);
         var14.start();
      }

   }
}
