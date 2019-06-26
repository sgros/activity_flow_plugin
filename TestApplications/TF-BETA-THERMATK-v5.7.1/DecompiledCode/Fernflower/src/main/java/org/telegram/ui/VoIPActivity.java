package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.TextUtils.TruncateAt;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.palette.graphics.Palette;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.voip.EncryptionKeyEmojifier;
import org.telegram.messenger.voip.VoIPBaseService;
import org.telegram.messenger.voip.VoIPController;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.DarkAlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CorrectlyMeasuringTextView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.IdenticonDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.voip.CallSwipeView;
import org.telegram.ui.Components.voip.CheckableImageView;
import org.telegram.ui.Components.voip.DarkTheme;
import org.telegram.ui.Components.voip.FabBackgroundDrawable;
import org.telegram.ui.Components.voip.VoIPHelper;

public class VoIPActivity extends Activity implements VoIPBaseService.StateListener, NotificationCenter.NotificationCenterDelegate {
   private static final String TAG = "tg-voip-ui";
   private View acceptBtn;
   private CallSwipeView acceptSwipe;
   private TextView accountNameText;
   private ImageView addMemberBtn;
   private ImageView blurOverlayView1;
   private ImageView blurOverlayView2;
   private Bitmap blurredPhoto1;
   private Bitmap blurredPhoto2;
   private LinearLayout bottomButtons;
   private TextView brandingText;
   private int callState;
   private View cancelBtn;
   private ImageView chatBtn;
   private FrameLayout content;
   private Animator currentAcceptAnim;
   private int currentAccount = -1;
   private Animator currentDeclineAnim;
   private View declineBtn;
   private CallSwipeView declineSwipe;
   private boolean didAcceptFromHere = false;
   private TextView durationText;
   private AnimatorSet ellAnimator;
   private VoIPActivity.TextAlphaSpan[] ellSpans;
   private AnimatorSet emojiAnimator;
   boolean emojiExpanded;
   private TextView emojiExpandedText;
   boolean emojiTooltipVisible;
   private LinearLayout emojiWrap;
   private View endBtn;
   private FabBackgroundDrawable endBtnBg;
   private View endBtnIcon;
   private boolean firstStateChange = true;
   private TextView hintTextView;
   private boolean isIncomingWaiting;
   private ImageView[] keyEmojiViews = new ImageView[4];
   private boolean keyEmojiVisible;
   private String lastStateText;
   private CheckableImageView micToggle;
   private TextView nameText;
   private BackupImageView photoView;
   private AnimatorSet retryAnim;
   private boolean retrying;
   private int signalBarsCount;
   private VoIPActivity.SignalBarsDrawable signalBarsDrawable;
   private CheckableImageView spkToggle;
   private TextView stateText;
   private TextView stateText2;
   private LinearLayout swipeViewsWrap;
   private Animator textChangingAnim;
   private Animator tooltipAnim;
   private Runnable tooltipHider;
   private TLRPC.User user;

   private void callAccepted() {
      this.endBtn.setVisibility(0);
      if (VoIPService.getSharedInstance().hasEarpiece()) {
         this.spkToggle.setVisibility(0);
      } else {
         this.spkToggle.setVisibility(8);
      }

      this.bottomButtons.setVisibility(0);
      AnimatorSet var2;
      AnimatorSet var3;
      AnimatorSet var4;
      if (this.didAcceptFromHere) {
         this.acceptBtn.setVisibility(8);
         ObjectAnimator var1;
         if (VERSION.SDK_INT >= 21) {
            var1 = ObjectAnimator.ofArgb(this.endBtnBg, "color", new int[]{-12207027, -1696188});
         } else {
            var1 = ObjectAnimator.ofInt(this.endBtnBg, "color", new int[]{-12207027, -1696188});
            var1.setEvaluator(new ArgbEvaluator());
         }

         var2 = new AnimatorSet();
         var3 = new AnimatorSet();
         var3.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.endBtnIcon, "rotation", new float[]{-135.0F, 0.0F}), var1});
         var3.setInterpolator(CubicBezierInterpolator.EASE_OUT);
         var3.setDuration(500L);
         var4 = new AnimatorSet();
         var4.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.swipeViewsWrap, "alpha", new float[]{1.0F, 0.0F}), ObjectAnimator.ofFloat(this.declineBtn, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.accountNameText, "alpha", new float[]{0.0F})});
         var4.setInterpolator(CubicBezierInterpolator.EASE_IN);
         var4.setDuration(125L);
         var2.playTogether(new Animator[]{var3, var4});
         var2.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               VoIPActivity.this.swipeViewsWrap.setVisibility(8);
               VoIPActivity.this.declineBtn.setVisibility(8);
               VoIPActivity.this.accountNameText.setVisibility(8);
            }
         });
         var2.start();
      } else {
         var3 = new AnimatorSet();
         var4 = new AnimatorSet();
         var4.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.bottomButtons, "alpha", new float[]{0.0F, 1.0F})});
         var4.setInterpolator(CubicBezierInterpolator.EASE_OUT);
         var4.setDuration(500L);
         var2 = new AnimatorSet();
         var2.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.swipeViewsWrap, "alpha", new float[]{1.0F, 0.0F}), ObjectAnimator.ofFloat(this.declineBtn, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.acceptBtn, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.accountNameText, "alpha", new float[]{0.0F})});
         var2.setInterpolator(CubicBezierInterpolator.EASE_IN);
         var2.setDuration(125L);
         var3.playTogether(new Animator[]{var4, var2});
         var3.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               VoIPActivity.this.swipeViewsWrap.setVisibility(8);
               VoIPActivity.this.declineBtn.setVisibility(8);
               VoIPActivity.this.acceptBtn.setVisibility(8);
               VoIPActivity.this.accountNameText.setVisibility(8);
            }
         });
         var3.start();
      }

   }

   @SuppressLint({"ObjectAnimatorBinding"})
   private ObjectAnimator createAlphaAnimator(Object var1, int var2, int var3, int var4, int var5) {
      ObjectAnimator var6 = ObjectAnimator.ofInt(var1, "alpha", new int[]{var2, var3});
      var6.setDuration((long)var5);
      var6.setStartDelay((long)var4);
      var6.setInterpolator(CubicBezierInterpolator.DEFAULT);
      return var6;
   }

   private View createContentView() {
      FrameLayout var1 = new FrameLayout(this) {
         private void setNegativeMargins(Rect var1, LayoutParams var2) {
            var2.topMargin = -var1.top;
            var2.bottomMargin = -var1.bottom;
            var2.leftMargin = -var1.left;
            var2.rightMargin = -var1.right;
         }

         protected boolean fitSystemWindows(Rect var1) {
            this.setNegativeMargins(var1, (LayoutParams)VoIPActivity.this.photoView.getLayoutParams());
            this.setNegativeMargins(var1, (LayoutParams)VoIPActivity.this.blurOverlayView1.getLayoutParams());
            this.setNegativeMargins(var1, (LayoutParams)VoIPActivity.this.blurOverlayView2.getLayoutParams());
            return super.fitSystemWindows(var1);
         }
      };
      var1.setBackgroundColor(0);
      var1.setFitsSystemWindows(true);
      var1.setClipToPadding(false);
      BackupImageView var2 = new BackupImageView(this) {
         private Drawable bottomGradient = this.getResources().getDrawable(2131165392);
         private Paint paint = new Paint();
         private Drawable topGradient = this.getResources().getDrawable(2131165393);

         protected void onDraw(Canvas var1) {
            super.onDraw(var1);
            this.paint.setColor(1275068416);
            var1.drawRect(0.0F, 0.0F, (float)this.getWidth(), (float)this.getHeight(), this.paint);
            this.topGradient.setBounds(0, 0, this.getWidth(), AndroidUtilities.dp(170.0F));
            this.topGradient.setAlpha(128);
            this.topGradient.draw(var1);
            this.bottomGradient.setBounds(0, this.getHeight() - AndroidUtilities.dp(220.0F), this.getWidth(), this.getHeight());
            this.bottomGradient.setAlpha(178);
            this.bottomGradient.draw(var1);
         }
      };
      this.photoView = var2;
      var1.addView(var2);
      this.blurOverlayView1 = new ImageView(this);
      this.blurOverlayView1.setScaleType(ScaleType.CENTER_CROP);
      this.blurOverlayView1.setAlpha(0.0F);
      var1.addView(this.blurOverlayView1);
      this.blurOverlayView2 = new ImageView(this);
      this.blurOverlayView2.setScaleType(ScaleType.CENTER_CROP);
      this.blurOverlayView2.setAlpha(0.0F);
      var1.addView(this.blurOverlayView2);
      TextView var3 = new TextView(this);
      var3.setTextColor(-855638017);
      var3.setText(LocaleController.getString("VoipInCallBranding", 2131561071));
      Object var9 = this.getResources().getDrawable(2131165698).mutate();
      ((Drawable)var9).setAlpha(204);
      ((Drawable)var9).setBounds(0, 0, AndroidUtilities.dp(15.0F), AndroidUtilities.dp(15.0F));
      this.signalBarsDrawable = new VoIPActivity.SignalBarsDrawable();
      VoIPActivity.SignalBarsDrawable var4 = this.signalBarsDrawable;
      var4.setBounds(0, 0, var4.getIntrinsicWidth(), this.signalBarsDrawable.getIntrinsicHeight());
      Object var13;
      if (LocaleController.isRTL) {
         var13 = this.signalBarsDrawable;
      } else {
         var13 = var9;
      }

      if (!LocaleController.isRTL) {
         var9 = this.signalBarsDrawable;
      }

      var3.setCompoundDrawables((Drawable)var13, (Drawable)null, (Drawable)var9, (Drawable)null);
      var3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      byte var5;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var3.setGravity(var5);
      var3.setCompoundDrawablePadding(AndroidUtilities.dp(5.0F));
      var3.setTextSize(1, 14.0F);
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var1.addView(var3, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, 18.0F, 18.0F, 18.0F, 0.0F));
      this.brandingText = var3;
      TextView var10 = new TextView(this);
      var10.setSingleLine();
      var10.setTextColor(-1);
      var10.setTextSize(1, 40.0F);
      var10.setEllipsize(TruncateAt.END);
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var10.setGravity(var5);
      var10.setShadowLayer((float)AndroidUtilities.dp(3.0F), 0.0F, (float)AndroidUtilities.dp(0.6666667F), 1275068416);
      var10.setTypeface(Typeface.create("sans-serif-light", 0));
      this.nameText = var10;
      var1.addView(var10, LayoutHelper.createFrame(-1, -2.0F, 51, 16.0F, 43.0F, 18.0F, 0.0F));
      var10 = new TextView(this);
      var10.setTextColor(-855638017);
      var10.setSingleLine();
      var10.setEllipsize(TruncateAt.END);
      var10.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var10.setShadowLayer((float)AndroidUtilities.dp(3.0F), 0.0F, (float)AndroidUtilities.dp(0.6666667F), 1275068416);
      var10.setTextSize(1, 15.0F);
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var10.setGravity(var5);
      this.stateText = var10;
      var1.addView(var10, LayoutHelper.createFrame(-1, -2.0F, 51, 18.0F, 98.0F, 18.0F, 0.0F));
      this.durationText = var10;
      var10 = new TextView(this);
      var10.setTextColor(-855638017);
      var10.setSingleLine();
      var10.setEllipsize(TruncateAt.END);
      var10.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var10.setShadowLayer((float)AndroidUtilities.dp(3.0F), 0.0F, (float)AndroidUtilities.dp(0.6666667F), 1275068416);
      var10.setTextSize(1, 15.0F);
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var10.setGravity(var5);
      var10.setVisibility(8);
      this.stateText2 = var10;
      var1.addView(var10, LayoutHelper.createFrame(-1, -2.0F, 51, 18.0F, 98.0F, 18.0F, 0.0F));
      this.ellSpans = new VoIPActivity.TextAlphaSpan[]{new VoIPActivity.TextAlphaSpan(), new VoIPActivity.TextAlphaSpan(), new VoIPActivity.TextAlphaSpan()};
      LinearLayout var14 = new LinearLayout(this);
      var14.setOrientation(0);
      var1.addView(var14, LayoutHelper.createFrame(-1, -2, 80));
      TextView var17 = new TextView(this);
      var17.setTextColor(-855638017);
      var17.setSingleLine();
      var17.setEllipsize(TruncateAt.END);
      var17.setShadowLayer((float)AndroidUtilities.dp(3.0F), 0.0F, (float)AndroidUtilities.dp(0.6666667F), 1275068416);
      var17.setTextSize(1, 15.0F);
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var17.setGravity(var5);
      this.accountNameText = var17;
      var1.addView(var17, LayoutHelper.createFrame(-1, -2.0F, 51, 18.0F, 120.0F, 18.0F, 0.0F));
      CheckableImageView var20 = new CheckableImageView(this);
      var20.setBackgroundResource(2131165304);
      Drawable var11 = this.getResources().getDrawable(2131165456).mutate();
      var20.setAlpha(204);
      var20.setImageDrawable(var11);
      var20.setScaleType(ScaleType.CENTER);
      var20.setContentDescription(LocaleController.getString("AccDescrMuteMic", 2131558448));
      FrameLayout var12 = new FrameLayout(this);
      this.micToggle = var20;
      var12.addView(var20, LayoutHelper.createFrame(38, 38.0F, 81, 0.0F, 0.0F, 0.0F, 10.0F));
      var14.addView(var12, LayoutHelper.createLinear(0, -2, 1.0F));
      ImageView var23 = new ImageView(this);
      var11 = this.getResources().getDrawable(2131165436).mutate();
      var11.setAlpha(204);
      var23.setImageDrawable(var11);
      var23.setScaleType(ScaleType.CENTER);
      var23.setContentDescription(LocaleController.getString("AccDescrOpenChat", 2131558450));
      var12 = new FrameLayout(this);
      this.chatBtn = var23;
      var12.addView(var23, LayoutHelper.createFrame(38, 38.0F, 81, 0.0F, 0.0F, 0.0F, 10.0F));
      var14.addView(var12, LayoutHelper.createLinear(0, -2, 1.0F));
      var20 = new CheckableImageView(this);
      var20.setBackgroundResource(2131165304);
      var11 = this.getResources().getDrawable(2131165476).mutate();
      var20.setAlpha(204);
      var20.setImageDrawable(var11);
      var20.setScaleType(ScaleType.CENTER);
      var20.setContentDescription(LocaleController.getString("VoipAudioRoutingSpeaker", 2131561060));
      var12 = new FrameLayout(this);
      this.spkToggle = var20;
      var12.addView(var20, LayoutHelper.createFrame(38, 38.0F, 81, 0.0F, 0.0F, 0.0F, 10.0F));
      var14.addView(var12, LayoutHelper.createLinear(0, -2, 1.0F));
      this.bottomButtons = var14;
      LinearLayout var18 = new LinearLayout(this);
      var18.setOrientation(0);
      CallSwipeView var15 = new CallSwipeView(this);
      var15.setColor(-12207027);
      var15.setContentDescription(LocaleController.getString("Accept", 2131558484));
      this.acceptSwipe = var15;
      var18.addView(var15, LayoutHelper.createLinear(-1, 70, 1.0F, 4, 4, -35, 4));
      CallSwipeView var25 = new CallSwipeView(this);
      var25.setColor(-1696188);
      var25.setContentDescription(LocaleController.getString("Decline", 2131559223));
      this.declineSwipe = var25;
      var18.addView(var25, LayoutHelper.createLinear(-1, 70, 1.0F, -35, 4, 4, 4));
      this.swipeViewsWrap = var18;
      var1.addView(var18, LayoutHelper.createFrame(-1, -2.0F, 80, 20.0F, 0.0F, 20.0F, 68.0F));
      ImageView var21 = new ImageView(this);
      FabBackgroundDrawable var6 = new FabBackgroundDrawable();
      var6.setColor(-12207027);
      var21.setBackgroundDrawable(var6);
      var21.setImageResource(2131165431);
      var21.setScaleType(ScaleType.MATRIX);
      Matrix var24 = new Matrix();
      var24.setTranslate((float)AndroidUtilities.dp(17.0F), (float)AndroidUtilities.dp(17.0F));
      var24.postRotate(-135.0F, (float)AndroidUtilities.dp(35.0F), (float)AndroidUtilities.dp(35.0F));
      var21.setImageMatrix(var24);
      this.acceptBtn = var21;
      var1.addView(var21, LayoutHelper.createFrame(78, 78.0F, 83, 20.0F, 0.0F, 0.0F, 68.0F));
      ImageView var26 = new ImageView(this);
      FabBackgroundDrawable var7 = new FabBackgroundDrawable();
      var7.setColor(-1696188);
      var26.setBackgroundDrawable(var7);
      var26.setImageResource(2131165431);
      var26.setScaleType(ScaleType.CENTER);
      this.declineBtn = var26;
      var1.addView(var26, LayoutHelper.createFrame(78, 78.0F, 85, 0.0F, 0.0F, 20.0F, 68.0F));
      var15.setViewToDrag(var21, false);
      var25.setViewToDrag(var26, true);
      FrameLayout var16 = new FrameLayout(this);
      FabBackgroundDrawable var27 = new FabBackgroundDrawable();
      var27.setColor(-1696188);
      this.endBtnBg = var27;
      var16.setBackgroundDrawable(var27);
      var23 = new ImageView(this);
      var23.setImageResource(2131165431);
      var23.setScaleType(ScaleType.CENTER);
      this.endBtnIcon = var23;
      var16.addView(var23, LayoutHelper.createFrame(70, 70.0F));
      var16.setForeground(this.getResources().getDrawable(2131165376));
      var16.setContentDescription(LocaleController.getString("VoipEndCall", 2131561065));
      this.endBtn = var16;
      var1.addView(var16, LayoutHelper.createFrame(78, 78.0F, 81, 0.0F, 0.0F, 0.0F, 68.0F));
      var23 = new ImageView(this);
      FabBackgroundDrawable var19 = new FabBackgroundDrawable();
      var19.setColor(-1);
      var23.setBackgroundDrawable(var19);
      var23.setImageResource(2131165375);
      var23.setColorFilter(-1996488704);
      var23.setScaleType(ScaleType.CENTER);
      var23.setVisibility(8);
      var23.setContentDescription(LocaleController.getString("Cancel", 2131558891));
      this.cancelBtn = var23;
      var1.addView(var23, LayoutHelper.createFrame(78, 78.0F, 83, 52.0F, 0.0F, 0.0F, 68.0F));
      this.emojiWrap = new LinearLayout(this);
      this.emojiWrap.setOrientation(0);
      this.emojiWrap.setClipToPadding(false);
      this.emojiWrap.setPivotX(0.0F);
      this.emojiWrap.setPivotY(0.0F);
      this.emojiWrap.setPadding(AndroidUtilities.dp(14.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(14.0F), AndroidUtilities.dp(10.0F));

      int var29;
      for(var29 = 0; var29 < 4; ++var29) {
         ImageView var22 = new ImageView(this);
         var22.setScaleType(ScaleType.FIT_XY);
         LinearLayout var28 = this.emojiWrap;
         float var8;
         if (var29 == 0) {
            var8 = 0.0F;
         } else {
            var8 = 4.0F;
         }

         var28.addView(var22, LayoutHelper.createLinear(22, 22, var8, 0.0F, 0.0F, 0.0F));
         this.keyEmojiViews[var29] = var22;
      }

      this.emojiWrap.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            VoIPActivity var2 = VoIPActivity.this;
            if (var2.emojiTooltipVisible) {
               var2.setEmojiTooltipVisible(false);
               if (VoIPActivity.this.tooltipHider != null) {
                  VoIPActivity.this.hintTextView.removeCallbacks(VoIPActivity.this.tooltipHider);
                  VoIPActivity.this.tooltipHider = null;
               }
            }

            var2 = VoIPActivity.this;
            var2.setEmojiExpanded(var2.emojiExpanded ^ true);
         }
      });
      var14 = this.emojiWrap;
      if (LocaleController.isRTL) {
         var5 = 3;
      } else {
         var5 = 5;
      }

      var1.addView(var14, LayoutHelper.createFrame(-2, -2, var5 | 48));
      this.emojiWrap.setOnLongClickListener(new OnLongClickListener() {
         public boolean onLongClick(View var1) {
            VoIPActivity var4 = VoIPActivity.this;
            if (var4.emojiExpanded) {
               return false;
            } else {
               if (var4.tooltipHider != null) {
                  VoIPActivity.this.hintTextView.removeCallbacks(VoIPActivity.this.tooltipHider);
                  VoIPActivity.this.tooltipHider = null;
               }

               var4 = VoIPActivity.this;
               var4.setEmojiTooltipVisible(var4.emojiTooltipVisible ^ true);
               var4 = VoIPActivity.this;
               if (var4.emojiTooltipVisible) {
                  TextView var5 = var4.hintTextView;
                  VoIPActivity var2 = VoIPActivity.this;
                  Runnable var3 = new Runnable() {
                     public void run() {
                        VoIPActivity.this.tooltipHider = null;
                        VoIPActivity.this.setEmojiTooltipVisible(false);
                     }
                  };
                  var2.tooltipHider = var3;
                  var5.postDelayed(var3, 5000L);
               }

               return true;
            }
         }
      });
      this.emojiExpandedText = new TextView(this);
      this.emojiExpandedText.setTextSize(1, 16.0F);
      this.emojiExpandedText.setTextColor(-1);
      this.emojiExpandedText.setGravity(17);
      this.emojiExpandedText.setAlpha(0.0F);
      var1.addView(this.emojiExpandedText, LayoutHelper.createFrame(-1, -2.0F, 17, 10.0F, 32.0F, 10.0F, 0.0F));
      this.hintTextView = new CorrectlyMeasuringTextView(this);
      this.hintTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0F), -231525581));
      this.hintTextView.setTextColor(Theme.getColor("chat_gifSaveHintText"));
      this.hintTextView.setTextSize(1, 14.0F);
      this.hintTextView.setPadding(AndroidUtilities.dp(10.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(10.0F));
      this.hintTextView.setGravity(17);
      this.hintTextView.setMaxWidth(AndroidUtilities.dp(300.0F));
      this.hintTextView.setAlpha(0.0F);
      var1.addView(this.hintTextView, LayoutHelper.createFrame(-2, -2.0F, 53, 0.0F, 42.0F, 10.0F, 0.0F));
      var29 = this.stateText.getPaint().getAlpha();
      this.ellAnimator = new AnimatorSet();
      this.ellAnimator.playTogether(new Animator[]{this.createAlphaAnimator(this.ellSpans[0], 0, var29, 0, 300), this.createAlphaAnimator(this.ellSpans[1], 0, var29, 150, 300), this.createAlphaAnimator(this.ellSpans[2], 0, var29, 300, 300), this.createAlphaAnimator(this.ellSpans[0], var29, 0, 1000, 400), this.createAlphaAnimator(this.ellSpans[1], var29, 0, 1000, 400), this.createAlphaAnimator(this.ellSpans[2], var29, 0, 1000, 400)});
      this.ellAnimator.addListener(new AnimatorListenerAdapter() {
         private Runnable restarter = new Runnable() {
            public void run() {
               if (!VoIPActivity.this.isFinishing()) {
                  VoIPActivity.this.ellAnimator.start();
               }

            }
         };

         public void onAnimationEnd(Animator var1) {
            if (!VoIPActivity.this.isFinishing()) {
               VoIPActivity.this.content.postDelayed(this.restarter, 300L);
            }

         }
      });
      var1.setClipChildren(false);
      this.content = var1;
      return var1;
   }

   private CharSequence getFormattedDebugString() {
      String var1 = VoIPService.getSharedInstance().getDebugString();
      SpannableString var2 = new SpannableString(var1);
      int var3 = 0;

      int var6;
      do {
         int var4 = var3 + 1;
         int var5 = var1.indexOf(10, var4);
         var6 = var5;
         if (var5 == -1) {
            var6 = var1.length();
         }

         String var7 = var1.substring(var3, var6);
         if (var7.contains("IN_USE")) {
            var2.setSpan(new ForegroundColorSpan(-16711936), var3, var6, 0);
         } else if (var7.contains(": ")) {
            var2.setSpan(new ForegroundColorSpan(-1426063361), var3, var7.indexOf(58) + var3 + 1, 0);
         }

         var6 = var1.indexOf(10, var4);
         var3 = var6;
      } while(var6 != -1);

      return var2;
   }

   private void hideRetry() {
      AnimatorSet var1 = this.retryAnim;
      if (var1 != null) {
         var1.cancel();
      }

      this.retrying = false;
      ObjectAnimator var3;
      if (VERSION.SDK_INT >= 21) {
         var3 = ObjectAnimator.ofArgb(this.endBtnBg, "color", new int[]{-12207027, -1696188});
      } else {
         var3 = ObjectAnimator.ofInt(this.endBtnBg, "color", new int[]{-12207027, -1696188});
         var3.setEvaluator(new ArgbEvaluator());
      }

      AnimatorSet var2 = new AnimatorSet();
      var2.playTogether(new Animator[]{var3, ObjectAnimator.ofFloat(this.endBtnIcon, "rotation", new float[]{-135.0F, 0.0F}), ObjectAnimator.ofFloat(this.endBtn, "translationX", new float[]{0.0F}), ObjectAnimator.ofFloat(this.cancelBtn, "alpha", new float[]{0.0F})});
      var2.setStartDelay(200L);
      var2.setDuration(300L);
      var2.setInterpolator(CubicBezierInterpolator.DEFAULT);
      var2.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            VoIPActivity.this.cancelBtn.setVisibility(8);
            VoIPActivity.this.endBtn.setEnabled(true);
            VoIPActivity.this.retryAnim = null;
         }
      });
      this.retryAnim = var2;
      var2.start();
   }

   private void sendTextMessage(final String var1) {
      AndroidUtilities.runOnUIThread(new Runnable() {
         public void run() {
            SendMessagesHelper.getInstance(VoIPActivity.this.currentAccount).sendMessage(var1, (long)VoIPActivity.this.user.id, (MessageObject)null, (TLRPC.WebPage)null, false, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
         }
      });
   }

   private void setEmojiExpanded(boolean var1) {
      if (this.emojiExpanded != var1) {
         this.emojiExpanded = var1;
         AnimatorSet var2 = this.emojiAnimator;
         if (var2 != null) {
            var2.cancel();
         }

         ObjectAnimator var13;
         ImageView var15;
         ImageView var16;
         ObjectAnimator var18;
         ObjectAnimator var21;
         ObjectAnimator var23;
         if (var1) {
            int[] var3 = new int[]{0, 0};
            int[] var4 = new int[]{0, 0};
            this.emojiWrap.getLocationInWindow(var3);
            this.emojiExpandedText.getLocationInWindow(var4);
            Rect var17 = new Rect();
            this.getWindow().getDecorView().getGlobalVisibleRect(var17);
            int var5 = var4[1];
            int var6 = var3[1];
            int var7 = this.emojiWrap.getHeight();
            int var8 = AndroidUtilities.dp(32.0F);
            int var9 = this.emojiWrap.getHeight();
            int var10 = var17.width() / 2;
            int var11 = Math.round((float)this.emojiWrap.getWidth() * 2.5F) / 2;
            int var12 = var3[0];
            AnimatorSet var19 = new AnimatorSet();
            var18 = ObjectAnimator.ofFloat(this.emojiWrap, "translationY", new float[]{(float)(var5 - (var6 + var7) - var8 - var9)});
            var13 = ObjectAnimator.ofFloat(this.emojiWrap, "translationX", new float[]{(float)(var10 - var11 - var12)});
            ObjectAnimator var14 = ObjectAnimator.ofFloat(this.emojiWrap, "scaleX", new float[]{2.5F});
            var21 = ObjectAnimator.ofFloat(this.emojiWrap, "scaleY", new float[]{2.5F});
            var15 = this.blurOverlayView1;
            var23 = ObjectAnimator.ofFloat(var15, "alpha", new float[]{var15.getAlpha(), 1.0F, 1.0F});
            var16 = this.blurOverlayView2;
            var19.playTogether(new Animator[]{var18, var13, var14, var21, var23, ObjectAnimator.ofFloat(var16, "alpha", new float[]{var16.getAlpha(), this.blurOverlayView2.getAlpha(), 1.0F}), ObjectAnimator.ofFloat(this.emojiExpandedText, "alpha", new float[]{1.0F})});
            var19.setDuration(300L);
            var19.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.emojiAnimator = var19;
            var19.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  VoIPActivity.this.emojiAnimator = null;
               }
            });
            var19.start();
         } else {
            AnimatorSet var22 = new AnimatorSet();
            var21 = ObjectAnimator.ofFloat(this.emojiWrap, "translationX", new float[]{0.0F});
            var18 = ObjectAnimator.ofFloat(this.emojiWrap, "translationY", new float[]{0.0F});
            var13 = ObjectAnimator.ofFloat(this.emojiWrap, "scaleX", new float[]{1.0F});
            ObjectAnimator var20 = ObjectAnimator.ofFloat(this.emojiWrap, "scaleY", new float[]{1.0F});
            var15 = this.blurOverlayView1;
            var23 = ObjectAnimator.ofFloat(var15, "alpha", new float[]{var15.getAlpha(), this.blurOverlayView1.getAlpha(), 0.0F});
            var16 = this.blurOverlayView2;
            var22.playTogether(new Animator[]{var21, var18, var13, var20, var23, ObjectAnimator.ofFloat(var16, "alpha", new float[]{var16.getAlpha(), 0.0F, 0.0F}), ObjectAnimator.ofFloat(this.emojiExpandedText, "alpha", new float[]{0.0F})});
            var22.setDuration(300L);
            var22.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.emojiAnimator = var22;
            var22.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  VoIPActivity.this.emojiAnimator = null;
               }
            });
            var22.start();
         }

      }
   }

   private void setEmojiTooltipVisible(boolean var1) {
      this.emojiTooltipVisible = var1;
      Animator var2 = this.tooltipAnim;
      if (var2 != null) {
         var2.cancel();
      }

      this.hintTextView.setVisibility(0);
      TextView var4 = this.hintTextView;
      float var3;
      if (var1) {
         var3 = 1.0F;
      } else {
         var3 = 0.0F;
      }

      ObjectAnimator var5 = ObjectAnimator.ofFloat(var4, "alpha", new float[]{var3});
      var5.setDuration(300L);
      var5.setInterpolator(CubicBezierInterpolator.DEFAULT);
      var5.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            VoIPActivity.this.tooltipAnim = null;
         }
      });
      this.tooltipAnim = var5;
      var5.start();
   }

   private void setStateTextAnimated(String var1, boolean var2) {
      if (!var1.equals(this.lastStateText)) {
         this.lastStateText = var1;
         Animator var3 = this.textChangingAnim;
         if (var3 != null) {
            var3.cancel();
         }

         Object var12;
         if (var2) {
            if (!this.ellAnimator.isRunning()) {
               this.ellAnimator.start();
            }

            var12 = new SpannableStringBuilder(var1.toUpperCase());
            VoIPActivity.TextAlphaSpan[] var14 = this.ellSpans;
            int var4 = var14.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               var14[var5].setAlpha(0);
            }

            SpannableString var16 = new SpannableString("...");
            var16.setSpan(this.ellSpans[0], 0, 1, 0);
            var16.setSpan(this.ellSpans[1], 1, 2, 0);
            var16.setSpan(this.ellSpans[2], 2, 3, 0);
            ((SpannableStringBuilder)var12).append(var16);
         } else {
            if (this.ellAnimator.isRunning()) {
               this.ellAnimator.cancel();
            }

            var12 = var1.toUpperCase();
         }

         this.stateText2.setText((CharSequence)var12);
         this.stateText2.setVisibility(0);
         TextView var13 = this.stateText;
         float var6;
         if (LocaleController.isRTL) {
            var6 = (float)var13.getWidth();
         } else {
            var6 = 0.0F;
         }

         var13.setPivotX(var6);
         var13 = this.stateText;
         var13.setPivotY((float)(var13.getHeight() / 2));
         var13 = this.stateText2;
         if (LocaleController.isRTL) {
            var6 = (float)this.stateText.getWidth();
         } else {
            var6 = 0.0F;
         }

         var13.setPivotX(var6);
         this.stateText2.setPivotY((float)(this.stateText.getHeight() / 2));
         this.durationText = this.stateText2;
         AnimatorSet var7 = new AnimatorSet();
         ObjectAnimator var8 = ObjectAnimator.ofFloat(this.stateText2, "alpha", new float[]{0.0F, 1.0F});
         ObjectAnimator var15 = ObjectAnimator.ofFloat(this.stateText2, "translationY", new float[]{(float)(this.stateText.getHeight() / 2), 0.0F});
         ObjectAnimator var9 = ObjectAnimator.ofFloat(this.stateText2, "scaleX", new float[]{0.7F, 1.0F});
         ObjectAnimator var10 = ObjectAnimator.ofFloat(this.stateText2, "scaleY", new float[]{0.7F, 1.0F});
         ObjectAnimator var17 = ObjectAnimator.ofFloat(this.stateText, "alpha", new float[]{1.0F, 0.0F});
         TextView var11 = this.stateText;
         var7.playTogether(new Animator[]{var8, var15, var9, var10, var17, ObjectAnimator.ofFloat(var11, "translationY", new float[]{0.0F, (float)(-var11.getHeight() / 2)}), ObjectAnimator.ofFloat(this.stateText, "scaleX", new float[]{1.0F, 0.7F}), ObjectAnimator.ofFloat(this.stateText, "scaleY", new float[]{1.0F, 0.7F})});
         var7.setDuration(200L);
         var7.setInterpolator(CubicBezierInterpolator.DEFAULT);
         var7.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               VoIPActivity.this.textChangingAnim = null;
               VoIPActivity.this.stateText2.setVisibility(8);
               VoIPActivity var2 = VoIPActivity.this;
               var2.durationText = var2.stateText;
               VoIPActivity.this.stateText.setTranslationY(0.0F);
               VoIPActivity.this.stateText.setScaleX(1.0F);
               VoIPActivity.this.stateText.setScaleY(1.0F);
               VoIPActivity.this.stateText.setAlpha(1.0F);
               VoIPActivity.this.stateText.setText(VoIPActivity.this.stateText2.getText());
            }
         });
         this.textChangingAnim = var7;
         var7.start();
      }
   }

   private void showDebugAlert() {
      if (VoIPService.getSharedInstance() != null) {
         VoIPService.getSharedInstance().forceRating();
         final LinearLayout var1 = new LinearLayout(this);
         var1.setOrientation(1);
         var1.setBackgroundColor(-872415232);
         int var2 = AndroidUtilities.dp(16.0F);
         int var3 = var2 * 2;
         var1.setPadding(var2, var3, var2, var3);
         TextView var4 = new TextView(this);
         var4.setTextColor(-1);
         var4.setTextSize(1, 15.0F);
         var4.setTypeface(Typeface.DEFAULT_BOLD);
         var4.setGravity(17);
         StringBuilder var5 = new StringBuilder();
         var5.append("libtgvoip v");
         var5.append(VoIPController.getVersion());
         var4.setText(var5.toString());
         var1.addView(var4, LayoutHelper.createLinear(-1, -2, 0.0F, 0.0F, 0.0F, 16.0F));
         ScrollView var7 = new ScrollView(this);
         final TextView var9 = new TextView(this);
         var9.setTypeface(Typeface.MONOSPACE);
         var9.setTextSize(1, 11.0F);
         var9.setMaxWidth(AndroidUtilities.dp(350.0F));
         var9.setTextColor(-1);
         var9.setText(this.getFormattedDebugString());
         var7.addView(var9);
         var1.addView(var7, LayoutHelper.createLinear(-1, -1, 1.0F));
         TextView var6 = new TextView(this);
         var6.setBackgroundColor(-1);
         var6.setTextColor(-16777216);
         var6.setPadding(var2, var2, var2, var2);
         var6.setTextSize(1, 15.0F);
         var6.setText(LocaleController.getString("Close", 2131559117));
         var1.addView(var6, LayoutHelper.createLinear(-2, -2, 1, 0, 16, 0, 0));
         final WindowManager var8 = (WindowManager)this.getSystemService("window");
         var8.addView(var1, new android.view.WindowManager.LayoutParams(-1, -1, 1000, 0, -3));
         var6.setOnClickListener(new OnClickListener() {
            public void onClick(View var1x) {
               var8.removeView(var1);
            }
         });
         var1.postDelayed(new Runnable() {
            public void run() {
               if (!VoIPActivity.this.isFinishing() && VoIPService.getSharedInstance() != null) {
                  var9.setText(VoIPActivity.this.getFormattedDebugString());
                  var1.postDelayed(this, 500L);
               }

            }
         }, 500L);
      }
   }

   private void showErrorDialog(CharSequence var1) {
      AlertDialog var2 = (new DarkAlertDialog.Builder(this)).setTitle(LocaleController.getString("VoipFailed", 2131561068)).setMessage(var1).setPositiveButton(LocaleController.getString("OK", 2131560097), (android.content.DialogInterface.OnClickListener)null).show();
      var2.setCanceledOnTouchOutside(true);
      var2.setOnDismissListener(new OnDismissListener() {
         public void onDismiss(DialogInterface var1) {
            VoIPActivity.this.finish();
         }
      });
   }

   private void showInviteFragment() {
   }

   private void showMessagesSheet() {
      if (VoIPService.getSharedInstance() != null) {
         VoIPService.getSharedInstance().stopRinging();
      }

      SharedPreferences var1 = this.getSharedPreferences("mainconfig", 0);
      String[] var2 = new String[]{var1.getString("quick_reply_msg1", LocaleController.getString("QuickReplyDefault1", 2131560524)), var1.getString("quick_reply_msg2", LocaleController.getString("QuickReplyDefault2", 2131560525)), var1.getString("quick_reply_msg3", LocaleController.getString("QuickReplyDefault3", 2131560526)), var1.getString("quick_reply_msg4", LocaleController.getString("QuickReplyDefault4", 2131560527))};
      LinearLayout var3 = new LinearLayout(this);
      var3.setOrientation(1);
      final BottomSheet var13 = new BottomSheet(this, true, 0);
      if (VERSION.SDK_INT >= 21) {
         this.getWindow().setNavigationBarColor(-13948117);
         var13.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface var1) {
               VoIPActivity.this.getWindow().setNavigationBarColor(0);
            }
         });
      }

      OnClickListener var4 = new OnClickListener() {
         public void onClick(final View var1) {
            var13.dismiss();
            if (VoIPService.getSharedInstance() != null) {
               VoIPService.getSharedInstance().declineIncomingCall(4, new Runnable() {
                  public void run() {
                     VoIPActivity.this.sendTextMessage((String)var1.getTag());
                  }
               });
            }

         }
      };
      int var5 = var2.length;

      final BottomSheet.BottomSheetCell var8;
      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var2[var6];
         var8 = new BottomSheet.BottomSheetCell(this, 0);
         var8.setTextAndIcon(var7, 0);
         var8.setTextColor(-1);
         var8.setTag(var7);
         var8.setOnClickListener(var4);
         var3.addView(var8);
      }

      FrameLayout var9 = new FrameLayout(this);
      var8 = new BottomSheet.BottomSheetCell(this, 0);
      var8.setTextAndIcon(LocaleController.getString("QuickReplyCustom", 2131560523), 0);
      var8.setTextColor(-1);
      var9.addView(var8);
      final FrameLayout var17 = new FrameLayout(this);
      final EditText var10 = new EditText(this);
      var10.setTextSize(1, 16.0F);
      var10.setTextColor(-1);
      var10.setHintTextColor(DarkTheme.getColor("chat_messagePanelHint"));
      var10.setBackgroundDrawable((Drawable)null);
      var10.setPadding(AndroidUtilities.dp(16.0F), AndroidUtilities.dp(11.0F), AndroidUtilities.dp(16.0F), AndroidUtilities.dp(12.0F));
      var10.setHint(LocaleController.getString("QuickReplyCustom", 2131560523));
      var10.setMinHeight(AndroidUtilities.dp(48.0F));
      var10.setGravity(80);
      var10.setMaxLines(4);
      var10.setSingleLine(false);
      var10.setInputType(var10.getInputType() | 16384 | 131072);
      byte var16;
      if (LocaleController.isRTL) {
         var16 = 5;
      } else {
         var16 = 3;
      }

      float var11;
      if (LocaleController.isRTL) {
         var11 = 48.0F;
      } else {
         var11 = 0.0F;
      }

      float var12;
      if (LocaleController.isRTL) {
         var12 = 0.0F;
      } else {
         var12 = 48.0F;
      }

      var17.addView(var10, LayoutHelper.createFrame(-1, -2.0F, var16, var11, 0.0F, var12, 0.0F));
      final ImageView var15 = new ImageView(this);
      var15.setScaleType(ScaleType.CENTER);
      var15.setImageDrawable(DarkTheme.getThemedDrawable(this, 2131165468, "chat_messagePanelSend"));
      if (LocaleController.isRTL) {
         var15.setScaleX(-0.1F);
      } else {
         var15.setScaleX(0.1F);
      }

      var15.setScaleY(0.1F);
      var15.setAlpha(0.0F);
      if (LocaleController.isRTL) {
         var16 = 3;
      } else {
         var16 = 5;
      }

      var17.addView(var15, LayoutHelper.createFrame(48, 48, var16 | 80));
      var15.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if (var10.length() != 0) {
               var13.dismiss();
               if (VoIPService.getSharedInstance() != null) {
                  VoIPService.getSharedInstance().declineIncomingCall(4, new Runnable() {
                     public void run() {
                        <undefinedtype> var1 = <VAR_NAMELESS_ENCLOSURE>;
                        VoIPActivity.this.sendTextMessage(var10.getText().toString());
                     }
                  });
               }

            }
         }
      });
      var15.setVisibility(4);
      final ImageView var14 = new ImageView(this);
      var14.setScaleType(ScaleType.CENTER);
      var14.setImageDrawable(DarkTheme.getThemedDrawable(this, 2131165375, "chat_messagePanelIcons"));
      if (LocaleController.isRTL) {
         var16 = 3;
      } else {
         var16 = 5;
      }

      var17.addView(var14, LayoutHelper.createFrame(48, 48, var16 | 80));
      var14.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            var17.setVisibility(8);
            var8.setVisibility(0);
            var10.setText("");
            ((InputMethodManager)VoIPActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(var10.getWindowToken(), 0);
         }
      });
      var10.addTextChangedListener(new TextWatcher() {
         boolean prevState = false;

         public void afterTextChanged(Editable var1) {
            boolean var2;
            if (var1.length() > 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            if (this.prevState != var2) {
               this.prevState = var2;
               float var3;
               ViewPropertyAnimator var4;
               if (var2) {
                  var15.setVisibility(0);
                  var4 = var15.animate().alpha(1.0F);
                  if (LocaleController.isRTL) {
                     var3 = -1.0F;
                  } else {
                     var3 = 1.0F;
                  }

                  var4.scaleX(var3).scaleY(1.0F).setDuration(200L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                  var14.animate().alpha(0.0F).scaleX(0.1F).scaleY(0.1F).setInterpolator(CubicBezierInterpolator.DEFAULT).setDuration(200L).withEndAction(new Runnable() {
                     public void run() {
                        var14.setVisibility(4);
                     }
                  }).start();
               } else {
                  var14.setVisibility(0);
                  var14.animate().alpha(1.0F).scaleX(1.0F).scaleY(1.0F).setDuration(200L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                  var4 = var15.animate().alpha(0.0F);
                  if (LocaleController.isRTL) {
                     var3 = -0.1F;
                  } else {
                     var3 = 0.1F;
                  }

                  var4.scaleX(var3).scaleY(0.1F).setInterpolator(CubicBezierInterpolator.DEFAULT).setDuration(200L).withEndAction(new Runnable() {
                     public void run() {
                        var15.setVisibility(4);
                     }
                  }).start();
               }
            }

         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }
      });
      var17.setVisibility(8);
      var9.addView(var17);
      var8.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            var17.setVisibility(0);
            var8.setVisibility(4);
            var10.requestFocus();
            ((InputMethodManager)VoIPActivity.this.getSystemService("input_method")).showSoftInput(var10, 0);
         }
      });
      var3.addView(var9);
      var13.setCustomView(var3);
      var13.setBackgroundColor(-13948117);
      var13.show();
   }

   private void showRetry() {
      AnimatorSet var1 = this.retryAnim;
      if (var1 != null) {
         var1.cancel();
      }

      this.endBtn.setEnabled(false);
      this.retrying = true;
      this.cancelBtn.setVisibility(0);
      this.cancelBtn.setAlpha(0.0F);
      AnimatorSet var2 = new AnimatorSet();
      ObjectAnimator var3;
      if (VERSION.SDK_INT >= 21) {
         var3 = ObjectAnimator.ofArgb(this.endBtnBg, "color", new int[]{-1696188, -12207027});
      } else {
         var3 = ObjectAnimator.ofInt(this.endBtnBg, "color", new int[]{-1696188, -12207027});
         var3.setEvaluator(new ArgbEvaluator());
      }

      var2.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.cancelBtn, "alpha", new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.endBtn, "translationX", new float[]{0.0F, (float)(this.content.getWidth() / 2 - AndroidUtilities.dp(52.0F) - this.endBtn.getWidth() / 2)}), var3, ObjectAnimator.ofFloat(this.endBtnIcon, "rotation", new float[]{0.0F, -135.0F})});
      var2.setStartDelay(200L);
      var2.setDuration(300L);
      var2.setInterpolator(CubicBezierInterpolator.DEFAULT);
      var2.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            VoIPActivity.this.retryAnim = null;
            VoIPActivity.this.endBtn.setEnabled(true);
         }
      });
      this.retryAnim = var2;
      var2.start();
   }

   private void startUpdatingCallDuration() {
      (new Runnable() {
         public void run() {
            if (!VoIPActivity.this.isFinishing() && VoIPService.getSharedInstance() != null && (VoIPActivity.this.callState == 3 || VoIPActivity.this.callState == 5)) {
               long var1 = VoIPService.getSharedInstance().getCallDuration() / 1000L;
               TextView var3 = VoIPActivity.this.durationText;
               Object[] var4;
               String var5;
               if (var1 > 3600L) {
                  var4 = new Object[]{var1 / 3600L, var1 % 3600L / 60L, var1 % 60L};
                  var5 = "%d:%02d:%02d";
               } else {
                  var4 = new Object[]{var1 / 60L, var1 % 60L};
                  var5 = "%d:%02d";
               }

               var3.setText(String.format(var5, var4));
               VoIPActivity.this.durationText.postDelayed(this, 500L);
            }

         }
      }).run();
   }

   private void updateBlurredPhotos(final ImageReceiver.BitmapHolder var1) {
      (new Thread(new Runnable() {
         public void run() {
            try {
               Bitmap var1x = Bitmap.createBitmap(150, 150, Config.ARGB_8888);
               Canvas var2 = new Canvas(var1x);
               Bitmap var3 = var1.bitmap;
               Rect var4 = new Rect(0, 0, 150, 150);
               Paint var5 = new Paint(2);
               var2.drawBitmap(var3, (Rect)null, var4, var5);
               Utilities.blurBitmap(var1x, 3, 0, var1x.getWidth(), var1x.getHeight(), var1x.getRowBytes());
               Palette var13 = Palette.from(var1.bitmap).generate();
               Paint var11 = new Paint();
               var11.setColor(var13.getDarkMutedColor(-11242343) & 16777215 | 1140850688);
               var2.drawColor(637534208);
               var2.drawRect(0.0F, 0.0F, (float)var2.getWidth(), (float)var2.getHeight(), var11);
               Bitmap var14 = Bitmap.createBitmap(50, 50, Config.ARGB_8888);
               Canvas var6 = new Canvas(var14);
               Bitmap var10 = var1.bitmap;
               var4 = new Rect(0, 0, 50, 50);
               Paint var7 = new Paint(2);
               var6.drawBitmap(var10, (Rect)null, var4, var7);
               Utilities.blurBitmap(var14, 3, 0, var14.getWidth(), var14.getHeight(), var14.getRowBytes());
               var11.setAlpha(102);
               var6.drawRect(0.0F, 0.0F, (float)var6.getWidth(), (float)var6.getHeight(), var11);
               VoIPActivity.this.blurredPhoto1 = var1x;
               VoIPActivity.this.blurredPhoto2 = var14;
               VoIPActivity var12 = VoIPActivity.this;
               Runnable var9 = new Runnable() {
                  public void run() {
                     VoIPActivity.this.blurOverlayView1.setImageBitmap(VoIPActivity.this.blurredPhoto1);
                     VoIPActivity.this.blurOverlayView2.setImageBitmap(VoIPActivity.this.blurredPhoto2);
                     var1.release();
                  }
               };
               var12.runOnUiThread(var9);
            } catch (Throwable var8) {
            }

         }
      })).start();
   }

   private void updateKeyView() {
      if (VoIPService.getSharedInstance() != null) {
         (new IdenticonDrawable()).setColors(new int[]{16777215, -1, -1711276033, 872415231});
         TLRPC.TL_encryptedChat var1 = new TLRPC.TL_encryptedChat();

         try {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            var2.write(VoIPService.getSharedInstance().getEncryptionKey());
            var2.write(VoIPService.getSharedInstance().getGA());
            var1.auth_key = var2.toByteArray();
         } catch (Exception var5) {
         }

         byte[] var6 = var1.auth_key;
         String[] var7 = EncryptionKeyEmojifier.emojifyForCall(Utilities.computeSHA256(var6, 0, var6.length));
         LinearLayout var8 = this.emojiWrap;
         StringBuilder var3 = new StringBuilder();
         var3.append(LocaleController.getString("EncryptionKey", 2131559360));
         var3.append(", ");
         var3.append(TextUtils.join(", ", var7));
         var8.setContentDescription(var3.toString());

         for(int var4 = 0; var4 < 4; ++var4) {
            Emoji.EmojiDrawable var9 = Emoji.getEmojiDrawable(var7[var4]);
            if (var9 != null) {
               var9.setBounds(0, 0, AndroidUtilities.dp(22.0F), AndroidUtilities.dp(22.0F));
               this.keyEmojiViews[var4].setImageDrawable(var9);
            }
         }

      }
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.emojiDidLoad) {
         ImageView[] var5 = this.keyEmojiViews;
         int var4 = var5.length;

         for(var2 = 0; var2 < var4; ++var2) {
            var5[var2].invalidate();
         }
      }

      if (var1 == NotificationCenter.closeInCallActivity) {
         this.finish();
      }

   }

   public void onAudioSettingsChanged() {
      VoIPBaseService var1 = VoIPBaseService.getSharedInstance();
      if (var1 != null) {
         this.micToggle.setChecked(var1.isMicMute());
         if (!var1.hasEarpiece() && !var1.isBluetoothHeadsetConnected()) {
            this.spkToggle.setVisibility(4);
         } else {
            this.spkToggle.setVisibility(0);
            if (!var1.hasEarpiece()) {
               this.spkToggle.setImageResource(2131165428);
               this.spkToggle.setChecked(var1.isSpeakerphoneOn());
            } else if (var1.isBluetoothHeadsetConnected()) {
               int var2 = var1.getCurrentAudioRoute();
               if (var2 != 0) {
                  if (var2 != 1) {
                     if (var2 == 2) {
                        this.spkToggle.setImageResource(2131165428);
                     }
                  } else {
                     this.spkToggle.setImageResource(2131165476);
                  }
               } else {
                  this.spkToggle.setImageResource(2131165461);
               }

               this.spkToggle.setChecked(false);
            } else {
               this.spkToggle.setImageResource(2131165476);
               this.spkToggle.setChecked(var1.isSpeakerphoneOn());
            }
         }

      }
   }

   public void onBackPressed() {
      if (this.emojiExpanded) {
         this.setEmojiExpanded(false);
      } else {
         if (!this.isIncomingWaiting) {
            super.onBackPressed();
         }

      }
   }

   protected void onCreate(Bundle var1) {
      this.requestWindowFeature(1);
      this.getWindow().addFlags(524288);
      super.onCreate(var1);
      if (VoIPService.getSharedInstance() == null) {
         this.finish();
      } else {
         this.currentAccount = VoIPService.getSharedInstance().getAccount();
         if (this.currentAccount == -1) {
            this.finish();
         } else {
            if ((this.getResources().getConfiguration().screenLayout & 15) < 3) {
               this.setRequestedOrientation(1);
            }

            View var4 = this.createContentView();
            this.setContentView(var4);
            int var2 = VERSION.SDK_INT;
            if (var2 >= 21) {
               this.getWindow().addFlags(Integer.MIN_VALUE);
               this.getWindow().setStatusBarColor(0);
               this.getWindow().setNavigationBarColor(0);
               this.getWindow().getDecorView().setSystemUiVisibility(1792);
            } else if (var2 >= 19) {
               this.getWindow().addFlags(201326592);
               this.getWindow().getDecorView().setSystemUiVisibility(1792);
            }

            this.user = VoIPService.getSharedInstance().getUser();
            if (this.user.photo != null) {
               this.photoView.getImageReceiver().setDelegate(new ImageReceiver.ImageReceiverDelegate() {
                  public void didSetImage(ImageReceiver var1, boolean var2, boolean var3) {
                     ImageReceiver.BitmapHolder var4 = var1.getBitmapSafe();
                     if (var4 != null) {
                        VoIPActivity.this.updateBlurredPhotos(var4);
                     }

                  }
               });
               this.photoView.setImage((ImageLocation)ImageLocation.getForUser(this.user, true), (String)null, (Drawable)(new ColorDrawable(-16777216)), (Object)this.user);
               this.photoView.setLayerType(2, (Paint)null);
            } else {
               this.photoView.setVisibility(8);
               var4.setBackgroundDrawable(new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-14994098, -14328963}));
            }

            this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            this.setVolumeControlStream(0);
            this.nameText.setOnClickListener(new OnClickListener() {
               private int tapCount = 0;

               public void onClick(View var1) {
                  if (!BuildVars.DEBUG_VERSION) {
                     int var2 = this.tapCount;
                     if (var2 != 9) {
                        this.tapCount = var2 + 1;
                        return;
                     }
                  }

                  VoIPActivity.this.showDebugAlert();
                  this.tapCount = 0;
               }
            });
            this.endBtn.setOnClickListener(new OnClickListener() {
               public void onClick(View var1) {
                  VoIPActivity.this.endBtn.setEnabled(false);
                  if (VoIPActivity.this.retrying) {
                     Intent var3 = new Intent(VoIPActivity.this, VoIPService.class);
                     var3.putExtra("user_id", VoIPActivity.this.user.id);
                     var3.putExtra("is_outgoing", true);
                     var3.putExtra("start_incall_activity", false);
                     var3.putExtra("account", VoIPActivity.this.currentAccount);

                     try {
                        VoIPActivity.this.startService(var3);
                     } catch (Throwable var2) {
                        FileLog.e(var2);
                     }

                     VoIPActivity.this.hideRetry();
                     VoIPActivity.this.endBtn.postDelayed(new Runnable() {
                        public void run() {
                           if (VoIPService.getSharedInstance() == null && !VoIPActivity.this.isFinishing()) {
                              VoIPActivity.this.endBtn.postDelayed(this, 100L);
                           } else {
                              if (VoIPService.getSharedInstance() != null) {
                                 VoIPService.getSharedInstance().registerStateListener(VoIPActivity.this);
                              }

                           }
                        }
                     }, 100L);
                  } else {
                     if (VoIPService.getSharedInstance() != null) {
                        VoIPService.getSharedInstance().hangUp();
                     }

                  }
               }
            });
            this.spkToggle.setOnClickListener(new OnClickListener() {
               public void onClick(View var1) {
                  VoIPService var2 = VoIPService.getSharedInstance();
                  if (var2 != null) {
                     var2.toggleSpeakerphoneOrShowRouteSheet(VoIPActivity.this);
                  }
               }
            });
            this.micToggle.setOnClickListener(new OnClickListener() {
               public void onClick(View var1) {
                  if (VoIPService.getSharedInstance() == null) {
                     VoIPActivity.this.finish();
                  } else {
                     boolean var2 = VoIPActivity.this.micToggle.isChecked() ^ true;
                     VoIPActivity.this.micToggle.setChecked(var2);
                     VoIPService.getSharedInstance().setMicMute(var2);
                  }
               }
            });
            this.chatBtn.setOnClickListener(new OnClickListener() {
               public void onClick(View var1) {
                  if (VoIPActivity.this.isIncomingWaiting) {
                     VoIPActivity.this.showMessagesSheet();
                  } else {
                     Intent var2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                     StringBuilder var3 = new StringBuilder();
                     var3.append("com.tmessages.openchat");
                     var3.append(Math.random());
                     var3.append(Integer.MAX_VALUE);
                     var2.setAction(var3.toString());
                     var2.putExtra("currentAccount", VoIPActivity.this.currentAccount);
                     var2.setFlags(32768);
                     var2.putExtra("userId", VoIPActivity.this.user.id);
                     VoIPActivity.this.startActivity(var2);
                     VoIPActivity.this.finish();
                  }
               }
            });
            this.spkToggle.setChecked(((AudioManager)this.getSystemService("audio")).isSpeakerphoneOn());
            this.micToggle.setChecked(VoIPService.getSharedInstance().isMicMute());
            this.onAudioSettingsChanged();
            TextView var3 = this.nameText;
            TLRPC.User var5 = this.user;
            var3.setText(ContactsController.formatName(var5.first_name, var5.last_name));
            VoIPService.getSharedInstance().registerStateListener(this);
            this.acceptSwipe.setListener(new CallSwipeView.Listener() {
               public void onDragCancel() {
                  if (VoIPActivity.this.currentDeclineAnim != null) {
                     VoIPActivity.this.currentDeclineAnim.cancel();
                  }

                  AnimatorSet var1 = new AnimatorSet();
                  var1.playTogether(new Animator[]{ObjectAnimator.ofFloat(VoIPActivity.this.declineSwipe, "alpha", new float[]{1.0F}), ObjectAnimator.ofFloat(VoIPActivity.this.declineBtn, "alpha", new float[]{1.0F})});
                  var1.setDuration(200L);
                  var1.setInterpolator(CubicBezierInterpolator.DEFAULT);
                  var1.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1) {
                        VoIPActivity.this.currentDeclineAnim = null;
                     }
                  });
                  VoIPActivity.this.currentDeclineAnim = var1;
                  var1.start();
                  VoIPActivity.this.declineSwipe.startAnimatingArrows();
               }

               public void onDragComplete() {
                  VoIPActivity.this.acceptSwipe.setEnabled(false);
                  VoIPActivity.this.declineSwipe.setEnabled(false);
                  if (VoIPService.getSharedInstance() == null) {
                     VoIPActivity.this.finish();
                  } else {
                     VoIPActivity.this.didAcceptFromHere = true;
                     if (VERSION.SDK_INT >= 23 && VoIPActivity.this.checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
                        VoIPActivity.this.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 101);
                     } else {
                        VoIPService.getSharedInstance().acceptIncomingCall();
                        VoIPActivity.this.callAccepted();
                     }

                  }
               }

               public void onDragStart() {
                  if (VoIPActivity.this.currentDeclineAnim != null) {
                     VoIPActivity.this.currentDeclineAnim.cancel();
                  }

                  AnimatorSet var1 = new AnimatorSet();
                  var1.playTogether(new Animator[]{ObjectAnimator.ofFloat(VoIPActivity.this.declineSwipe, "alpha", new float[]{0.2F}), ObjectAnimator.ofFloat(VoIPActivity.this.declineBtn, "alpha", new float[]{0.2F})});
                  var1.setDuration(200L);
                  var1.setInterpolator(CubicBezierInterpolator.DEFAULT);
                  var1.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1) {
                        VoIPActivity.this.currentDeclineAnim = null;
                     }
                  });
                  VoIPActivity.this.currentDeclineAnim = var1;
                  var1.start();
                  VoIPActivity.this.declineSwipe.stopAnimatingArrows();
               }
            });
            this.declineSwipe.setListener(new CallSwipeView.Listener() {
               public void onDragCancel() {
                  if (VoIPActivity.this.currentAcceptAnim != null) {
                     VoIPActivity.this.currentAcceptAnim.cancel();
                  }

                  AnimatorSet var1 = new AnimatorSet();
                  var1.playTogether(new Animator[]{ObjectAnimator.ofFloat(VoIPActivity.this.acceptSwipe, "alpha", new float[]{1.0F}), ObjectAnimator.ofFloat(VoIPActivity.this.acceptBtn, "alpha", new float[]{1.0F})});
                  var1.setDuration(200L);
                  var1.setInterpolator(CubicBezierInterpolator.DEFAULT);
                  var1.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1) {
                        VoIPActivity.this.currentAcceptAnim = null;
                     }
                  });
                  VoIPActivity.this.currentAcceptAnim = var1;
                  var1.start();
                  VoIPActivity.this.acceptSwipe.startAnimatingArrows();
               }

               public void onDragComplete() {
                  VoIPActivity.this.acceptSwipe.setEnabled(false);
                  VoIPActivity.this.declineSwipe.setEnabled(false);
                  if (VoIPService.getSharedInstance() != null) {
                     VoIPService.getSharedInstance().declineIncomingCall(4, (Runnable)null);
                  } else {
                     VoIPActivity.this.finish();
                  }

               }

               public void onDragStart() {
                  if (VoIPActivity.this.currentAcceptAnim != null) {
                     VoIPActivity.this.currentAcceptAnim.cancel();
                  }

                  AnimatorSet var1 = new AnimatorSet();
                  var1.playTogether(new Animator[]{ObjectAnimator.ofFloat(VoIPActivity.this.acceptSwipe, "alpha", new float[]{0.2F}), ObjectAnimator.ofFloat(VoIPActivity.this.acceptBtn, "alpha", new float[]{0.2F})});
                  var1.setDuration(200L);
                  var1.setInterpolator(new DecelerateInterpolator());
                  var1.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1) {
                        VoIPActivity.this.currentAcceptAnim = null;
                     }
                  });
                  VoIPActivity.this.currentAcceptAnim = var1;
                  var1.start();
                  VoIPActivity.this.acceptSwipe.stopAnimatingArrows();
               }
            });
            this.cancelBtn.setOnClickListener(new OnClickListener() {
               public void onClick(View var1) {
                  VoIPActivity.this.finish();
               }
            });
            this.getWindow().getDecorView().setKeepScreenOn(true);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.closeInCallActivity);
            this.hintTextView.setText(LocaleController.formatString("CallEmojiKeyTooltip", 2131558872, this.user.first_name));
            this.emojiExpandedText.setText(LocaleController.formatString("CallEmojiKeyTooltip", 2131558872, this.user.first_name));
            if (((AccessibilityManager)this.getSystemService("accessibility")).isTouchExplorationEnabled()) {
               this.nameText.postDelayed(new Runnable() {
                  public void run() {
                     VoIPActivity.this.nameText.sendAccessibilityEvent(8);
                  }
               }, 500L);
            }

         }
      }
   }

   protected void onDestroy() {
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeInCallActivity);
      if (VoIPService.getSharedInstance() != null) {
         VoIPService.getSharedInstance().unregisterStateListener(this);
      }

      super.onDestroy();
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      if (this.isIncomingWaiting && (var1 == 25 || var1 == 24)) {
         if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().stopRinging();
         } else {
            this.finish();
         }

         return true;
      } else {
         return super.onKeyDown(var1, var2);
      }
   }

   protected void onPause() {
      super.onPause();
      if (this.retrying) {
         this.finish();
      }

      if (VoIPService.getSharedInstance() != null) {
         VoIPService.getSharedInstance().onUIForegroundStateChanged(false);
      }

   }

   @TargetApi(23)
   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
      if (var1 == 101) {
         if (VoIPService.getSharedInstance() == null) {
            this.finish();
            return;
         }

         if (var3.length > 0 && var3[0] == 0) {
            VoIPService.getSharedInstance().acceptIncomingCall();
            this.callAccepted();
         } else {
            if (!this.shouldShowRequestPermissionRationale("android.permission.RECORD_AUDIO")) {
               VoIPService.getSharedInstance().declineIncomingCall();
               VoIPHelper.permissionDenied(this, new Runnable() {
                  public void run() {
                     VoIPActivity.this.finish();
                  }
               });
               return;
            }

            this.acceptSwipe.reset();
         }
      }

   }

   protected void onResume() {
      super.onResume();
      if (VoIPService.getSharedInstance() != null) {
         VoIPService.getSharedInstance().onUIForegroundStateChanged(true);
      }

   }

   public void onSignalBarsCountChanged(final int var1) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            VoIPActivity.this.signalBarsCount = var1;
            VoIPActivity.this.brandingText.invalidate();
         }
      });
   }

   public void onStateChanged(final int var1) {
      final int var2 = this.callState;
      this.callState = var1;
      this.runOnUiThread(new Runnable() {
         public void run() {
            boolean var1x = VoIPActivity.this.firstStateChange;
            VoIPActivity var2x;
            if (VoIPActivity.this.firstStateChange) {
               VoIPActivity.this.spkToggle.setChecked(((AudioManager)VoIPActivity.this.getSystemService("audio")).isSpeakerphoneOn());
               var2x = VoIPActivity.this;
               boolean var3;
               if (var1 == 15) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               var2x.isIncomingWaiting = var3;
               if (var3) {
                  VoIPActivity.this.swipeViewsWrap.setVisibility(0);
                  VoIPActivity.this.endBtn.setVisibility(8);
                  VoIPActivity.this.acceptSwipe.startAnimatingArrows();
                  VoIPActivity.this.declineSwipe.startAnimatingArrows();
                  if (UserConfig.getActivatedAccountsCount() > 1) {
                     TLRPC.User var7 = UserConfig.getInstance(VoIPActivity.this.currentAccount).getCurrentUser();
                     VoIPActivity.this.accountNameText.setText(LocaleController.formatString("VoipAnsweringAsAccount", 2131561057, ContactsController.formatName(var7.first_name, var7.last_name)));
                  } else {
                     VoIPActivity.this.accountNameText.setVisibility(8);
                  }

                  VoIPActivity.this.getWindow().addFlags(2097152);
                  VoIPService var8 = VoIPService.getSharedInstance();
                  if (var8 != null) {
                     var8.startRingtoneAndVibration();
                  }

                  VoIPActivity.this.setTitle(LocaleController.getString("VoipIncoming", 2131561073));
               } else {
                  VoIPActivity.this.swipeViewsWrap.setVisibility(8);
                  VoIPActivity.this.acceptBtn.setVisibility(8);
                  VoIPActivity.this.declineBtn.setVisibility(8);
                  VoIPActivity.this.accountNameText.setVisibility(8);
                  VoIPActivity.this.getWindow().clearFlags(2097152);
               }

               if (var1 != 3) {
                  VoIPActivity.this.emojiWrap.setVisibility(8);
               }

               VoIPActivity.this.firstStateChange = false;
            }

            int var4;
            if (VoIPActivity.this.isIncomingWaiting) {
               var4 = var1;
               if (var4 != 15 && var4 != 11 && var4 != 10) {
                  VoIPActivity.this.isIncomingWaiting = false;
                  if (!VoIPActivity.this.didAcceptFromHere) {
                     VoIPActivity.this.callAccepted();
                  }
               }
            }

            var4 = var1;
            if (var4 == 15) {
               VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipIncoming", 2131561073), false);
               VoIPActivity.this.getWindow().addFlags(2097152);
            } else if (var4 != 1 && var4 != 2) {
               if (var4 == 12) {
                  VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipExchangingKeys", 2131561067), true);
               } else if (var4 == 13) {
                  VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipWaiting", 2131561094), true);
               } else if (var4 == 16) {
                  VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipRinging", 2131561090), true);
               } else if (var4 == 14) {
                  VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipRequesting", 2131561089), true);
               } else if (var4 == 10) {
                  VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipHangingUp", 2131561070), true);
                  VoIPActivity.this.endBtnIcon.setAlpha(0.5F);
                  VoIPActivity.this.endBtn.setEnabled(false);
               } else if (var4 == 11) {
                  VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipCallEnded", 2131561062), false);
                  VoIPActivity.this.stateText.postDelayed(new Runnable() {
                     public void run() {
                        VoIPActivity.this.finish();
                     }
                  }, 200L);
               } else if (var4 == 17) {
                  VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipBusy", 2131561061), false);
                  VoIPActivity.this.showRetry();
               } else if (var4 != 3 && var4 != 5) {
                  if (var4 == 4) {
                     VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipFailed", 2131561068), false);
                     if (VoIPService.getSharedInstance() != null) {
                        var4 = VoIPService.getSharedInstance().getLastError();
                     } else {
                        var4 = 0;
                     }

                     if (var4 == 1) {
                        var2x = VoIPActivity.this;
                        var2x.showErrorDialog(AndroidUtilities.replaceTags(LocaleController.formatString("VoipPeerIncompatible", 2131561084, ContactsController.formatName(var2x.user.first_name, VoIPActivity.this.user.last_name))));
                     } else if (var4 == -1) {
                        var2x = VoIPActivity.this;
                        var2x.showErrorDialog(AndroidUtilities.replaceTags(LocaleController.formatString("VoipPeerOutdated", 2131561085, ContactsController.formatName(var2x.user.first_name, VoIPActivity.this.user.last_name))));
                     } else if (var4 == -2) {
                        var2x = VoIPActivity.this;
                        var2x.showErrorDialog(AndroidUtilities.replaceTags(LocaleController.formatString("CallNotAvailable", 2131558880, ContactsController.formatName(var2x.user.first_name, VoIPActivity.this.user.last_name))));
                     } else if (var4 == 3) {
                        VoIPActivity.this.showErrorDialog("Error initializing audio hardware");
                     } else if (var4 == -3) {
                        VoIPActivity.this.finish();
                     } else if (var4 == -5) {
                        VoIPActivity.this.showErrorDialog(LocaleController.getString("VoipErrorUnknown", 2131561066));
                     } else {
                        VoIPActivity.this.stateText.postDelayed(new Runnable() {
                           public void run() {
                              VoIPActivity.this.finish();
                           }
                        }, 1000L);
                     }
                  }
               } else {
                  VoIPActivity.this.setTitle((CharSequence)null);
                  if (!var1x && var1 == 3) {
                     var4 = MessagesController.getGlobalMainSettings().getInt("call_emoji_tooltip_count", 0);
                     if (var4 < 3) {
                        VoIPActivity.this.setEmojiTooltipVisible(true);
                        TextView var5 = VoIPActivity.this.hintTextView;
                        VoIPActivity var6 = VoIPActivity.this;
                        Runnable var9 = new Runnable() {
                           public void run() {
                              VoIPActivity.this.tooltipHider = null;
                              VoIPActivity.this.setEmojiTooltipVisible(false);
                           }
                        };
                        var6.tooltipHider = var9;
                        var5.postDelayed(var9, 5000L);
                        MessagesController.getGlobalMainSettings().edit().putInt("call_emoji_tooltip_count", var4 + 1).commit();
                     }
                  }

                  var4 = var2;
                  if (var4 != 3 && var4 != 5) {
                     VoIPActivity.this.setStateTextAnimated("0:00", false);
                     VoIPActivity.this.startUpdatingCallDuration();
                     VoIPActivity.this.updateKeyView();
                     if (VoIPActivity.this.emojiWrap.getVisibility() != 0) {
                        VoIPActivity.this.emojiWrap.setVisibility(0);
                        VoIPActivity.this.emojiWrap.setAlpha(0.0F);
                        VoIPActivity.this.emojiWrap.animate().alpha(1.0F).setDuration(200L).setInterpolator(new DecelerateInterpolator()).start();
                     }
                  }
               }
            } else {
               VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipConnecting", 2131561063), true);
            }

            VoIPActivity.this.brandingText.invalidate();
         }
      });
   }

   private class SignalBarsDrawable extends Drawable {
      private int[] barHeights;
      private int offsetStart;
      private Paint paint;
      private RectF rect;

      private SignalBarsDrawable() {
         this.barHeights = new int[]{AndroidUtilities.dp(3.0F), AndroidUtilities.dp(6.0F), AndroidUtilities.dp(9.0F), AndroidUtilities.dp(12.0F)};
         this.paint = new Paint(1);
         this.rect = new RectF();
         this.offsetStart = 6;
      }

      // $FF: synthetic method
      SignalBarsDrawable(Object var2) {
         this();
      }

      public void draw(Canvas var1) {
         if (VoIPActivity.this.callState == 3 || VoIPActivity.this.callState == 5) {
            this.paint.setColor(-1);
            int var2 = this.getBounds().left;
            float var3;
            if (LocaleController.isRTL) {
               var3 = 0.0F;
            } else {
               var3 = (float)this.offsetStart;
            }

            int var4 = var2 + AndroidUtilities.dp(var3);
            int var5 = this.getBounds().top;

            int var7;
            for(var2 = 0; var2 < 4; var2 = var7) {
               Paint var6 = this.paint;
               var7 = var2 + 1;
               short var8;
               if (var7 <= VoIPActivity.this.signalBarsCount) {
                  var8 = 242;
               } else {
                  var8 = 102;
               }

               var6.setAlpha(var8);
               this.rect.set((float)(AndroidUtilities.dp((float)(var2 * 4)) + var4), (float)(this.getIntrinsicHeight() + var5 - this.barHeights[var2]), (float)(AndroidUtilities.dp(4.0F) * var2 + var4 + AndroidUtilities.dp(3.0F)), (float)(this.getIntrinsicHeight() + var5));
               var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(0.3F), (float)AndroidUtilities.dp(0.3F), this.paint);
            }

         }
      }

      public int getIntrinsicHeight() {
         return AndroidUtilities.dp(12.0F);
      }

      public int getIntrinsicWidth() {
         return AndroidUtilities.dp((float)(this.offsetStart + 15));
      }

      public int getOpacity() {
         return -3;
      }

      public void setAlpha(int var1) {
      }

      public void setColorFilter(ColorFilter var1) {
      }
   }

   private class TextAlphaSpan extends CharacterStyle {
      private int alpha = 0;

      public TextAlphaSpan() {
      }

      public int getAlpha() {
         return this.alpha;
      }

      public void setAlpha(int var1) {
         this.alpha = var1;
         VoIPActivity.this.stateText.invalidate();
         VoIPActivity.this.stateText2.invalidate();
      }

      public void updateDrawState(TextPaint var1) {
         var1.setAlpha(this.alpha);
      }
   }
}
