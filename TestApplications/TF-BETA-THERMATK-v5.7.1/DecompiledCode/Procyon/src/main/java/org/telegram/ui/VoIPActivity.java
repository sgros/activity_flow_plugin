// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.graphics.ColorFilter;
import android.graphics.RectF;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import android.annotation.TargetApi;
import org.telegram.ui.Components.voip.VoIPHelper;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.ContactsController;
import android.media.AudioManager;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import android.content.Intent;
import org.telegram.messenger.BuildVars;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable$Orientation;
import android.graphics.drawable.ColorDrawable;
import org.telegram.messenger.ImageLocation;
import android.os.Bundle;
import org.telegram.messenger.Emoji;
import android.text.TextUtils;
import org.telegram.messenger.voip.EncryptionKeyEmojifier;
import java.io.ByteArrayOutputStream;
import org.telegram.ui.Components.IdenticonDrawable;
import androidx.palette.graphics.Palette;
import org.telegram.messenger.Utilities;
import android.graphics.Bitmap$Config;
import android.content.SharedPreferences;
import android.view.ViewPropertyAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import org.telegram.ui.Components.voip.DarkTheme;
import android.widget.EditText;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.DarkAlertDialog;
import android.view.WindowManager$LayoutParams;
import android.view.WindowManager;
import android.widget.ScrollView;
import org.telegram.messenger.voip.VoIPController;
import android.text.SpannableStringBuilder;
import java.util.HashMap;
import java.util.ArrayList;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.SendMessagesHelper;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableString;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CorrectlyMeasuringTextView;
import android.view.View$OnLongClickListener;
import android.view.View$OnClickListener;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.text.TextUtils$TruncateAt;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.widget.ImageView$ScaleType;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout$LayoutParams;
import android.graphics.Rect;
import android.content.Context;
import android.annotation.SuppressLint;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import org.telegram.ui.Components.CubicBezierInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Build$VERSION;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.messenger.ImageReceiver;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.voip.CheckableImageView;
import org.telegram.ui.Components.voip.FabBackgroundDrawable;
import android.animation.AnimatorSet;
import android.animation.Animator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.ui.Components.voip.CallSwipeView;
import android.view.View;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.voip.VoIPBaseService;
import android.app.Activity;

public class VoIPActivity extends Activity implements StateListener, NotificationCenterDelegate
{
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
    private int currentAccount;
    private Animator currentDeclineAnim;
    private View declineBtn;
    private CallSwipeView declineSwipe;
    private boolean didAcceptFromHere;
    private TextView durationText;
    private AnimatorSet ellAnimator;
    private TextAlphaSpan[] ellSpans;
    private AnimatorSet emojiAnimator;
    boolean emojiExpanded;
    private TextView emojiExpandedText;
    boolean emojiTooltipVisible;
    private LinearLayout emojiWrap;
    private View endBtn;
    private FabBackgroundDrawable endBtnBg;
    private View endBtnIcon;
    private boolean firstStateChange;
    private TextView hintTextView;
    private boolean isIncomingWaiting;
    private ImageView[] keyEmojiViews;
    private boolean keyEmojiVisible;
    private String lastStateText;
    private CheckableImageView micToggle;
    private TextView nameText;
    private BackupImageView photoView;
    private AnimatorSet retryAnim;
    private boolean retrying;
    private int signalBarsCount;
    private SignalBarsDrawable signalBarsDrawable;
    private CheckableImageView spkToggle;
    private TextView stateText;
    private TextView stateText2;
    private LinearLayout swipeViewsWrap;
    private Animator textChangingAnim;
    private Animator tooltipAnim;
    private Runnable tooltipHider;
    private TLRPC.User user;
    
    public VoIPActivity() {
        this.currentAccount = -1;
        this.firstStateChange = true;
        this.didAcceptFromHere = false;
        this.keyEmojiViews = new ImageView[4];
    }
    
    private void callAccepted() {
        this.endBtn.setVisibility(0);
        if (VoIPService.getSharedInstance().hasEarpiece()) {
            this.spkToggle.setVisibility(0);
        }
        else {
            this.spkToggle.setVisibility(8);
        }
        this.bottomButtons.setVisibility(0);
        if (this.didAcceptFromHere) {
            this.acceptBtn.setVisibility(8);
            ObjectAnimator objectAnimator;
            if (Build$VERSION.SDK_INT >= 21) {
                objectAnimator = ObjectAnimator.ofArgb((Object)this.endBtnBg, "color", new int[] { -12207027, -1696188 });
            }
            else {
                objectAnimator = ObjectAnimator.ofInt((Object)this.endBtnBg, "color", new int[] { -12207027, -1696188 });
                objectAnimator.setEvaluator((TypeEvaluator)new ArgbEvaluator());
            }
            final AnimatorSet set = new AnimatorSet();
            final AnimatorSet set2 = new AnimatorSet();
            set2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.endBtnIcon, "rotation", new float[] { -135.0f, 0.0f }), (Animator)objectAnimator });
            set2.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
            set2.setDuration(500L);
            final AnimatorSet set3 = new AnimatorSet();
            set3.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.swipeViewsWrap, "alpha", new float[] { 1.0f, 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.declineBtn, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.accountNameText, "alpha", new float[] { 0.0f }) });
            set3.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_IN);
            set3.setDuration(125L);
            set.playTogether(new Animator[] { (Animator)set2, (Animator)set3 });
            set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    VoIPActivity.this.swipeViewsWrap.setVisibility(8);
                    VoIPActivity.this.declineBtn.setVisibility(8);
                    VoIPActivity.this.accountNameText.setVisibility(8);
                }
            });
            set.start();
        }
        else {
            final AnimatorSet set4 = new AnimatorSet();
            final AnimatorSet set5 = new AnimatorSet();
            set5.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.bottomButtons, "alpha", new float[] { 0.0f, 1.0f }) });
            set5.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
            set5.setDuration(500L);
            final AnimatorSet set6 = new AnimatorSet();
            set6.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.swipeViewsWrap, "alpha", new float[] { 1.0f, 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.declineBtn, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.acceptBtn, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.accountNameText, "alpha", new float[] { 0.0f }) });
            set6.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_IN);
            set6.setDuration(125L);
            set4.playTogether(new Animator[] { (Animator)set5, (Animator)set6 });
            set4.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    VoIPActivity.this.swipeViewsWrap.setVisibility(8);
                    VoIPActivity.this.declineBtn.setVisibility(8);
                    VoIPActivity.this.acceptBtn.setVisibility(8);
                    VoIPActivity.this.accountNameText.setVisibility(8);
                }
            });
            set4.start();
        }
    }
    
    @SuppressLint({ "ObjectAnimatorBinding" })
    private ObjectAnimator createAlphaAnimator(final Object o, final int n, final int n2, final int n3, final int n4) {
        final ObjectAnimator ofInt = ObjectAnimator.ofInt(o, "alpha", new int[] { n, n2 });
        ofInt.setDuration((long)n4);
        ofInt.setStartDelay((long)n3);
        ofInt.setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT);
        return ofInt;
    }
    
    private View createContentView() {
        final FrameLayout content = new FrameLayout(this) {
            private void setNegativeMargins(final Rect rect, final FrameLayout$LayoutParams frameLayout$LayoutParams) {
                frameLayout$LayoutParams.topMargin = -rect.top;
                frameLayout$LayoutParams.bottomMargin = -rect.bottom;
                frameLayout$LayoutParams.leftMargin = -rect.left;
                frameLayout$LayoutParams.rightMargin = -rect.right;
            }
            
            protected boolean fitSystemWindows(final Rect rect) {
                this.setNegativeMargins(rect, (FrameLayout$LayoutParams)VoIPActivity.this.photoView.getLayoutParams());
                this.setNegativeMargins(rect, (FrameLayout$LayoutParams)VoIPActivity.this.blurOverlayView1.getLayoutParams());
                this.setNegativeMargins(rect, (FrameLayout$LayoutParams)VoIPActivity.this.blurOverlayView2.getLayoutParams());
                return super.fitSystemWindows(rect);
            }
        };
        content.setBackgroundColor(0);
        content.setFitsSystemWindows(true);
        content.setClipToPadding(false);
        content.addView((View)(this.photoView = new BackupImageView(this) {
            private Drawable bottomGradient = this.getResources().getDrawable(2131165392);
            private Paint paint = new Paint();
            private Drawable topGradient = this.getResources().getDrawable(2131165393);
            
            @Override
            protected void onDraw(final Canvas canvas) {
                super.onDraw(canvas);
                this.paint.setColor(1275068416);
                canvas.drawRect(0.0f, 0.0f, (float)this.getWidth(), (float)this.getHeight(), this.paint);
                this.topGradient.setBounds(0, 0, this.getWidth(), AndroidUtilities.dp(170.0f));
                this.topGradient.setAlpha(128);
                this.topGradient.draw(canvas);
                this.bottomGradient.setBounds(0, this.getHeight() - AndroidUtilities.dp(220.0f), this.getWidth(), this.getHeight());
                this.bottomGradient.setAlpha(178);
                this.bottomGradient.draw(canvas);
            }
        }));
        (this.blurOverlayView1 = new ImageView((Context)this)).setScaleType(ImageView$ScaleType.CENTER_CROP);
        this.blurOverlayView1.setAlpha(0.0f);
        content.addView((View)this.blurOverlayView1);
        (this.blurOverlayView2 = new ImageView((Context)this)).setScaleType(ImageView$ScaleType.CENTER_CROP);
        this.blurOverlayView2.setAlpha(0.0f);
        content.addView((View)this.blurOverlayView2);
        final TextView brandingText = new TextView((Context)this);
        brandingText.setTextColor(-855638017);
        brandingText.setText((CharSequence)LocaleController.getString("VoipInCallBranding", 2131561071));
        Drawable drawable = this.getResources().getDrawable(2131165698).mutate();
        drawable.setAlpha(204);
        drawable.setBounds(0, 0, AndroidUtilities.dp(15.0f), AndroidUtilities.dp(15.0f));
        this.signalBarsDrawable = new SignalBarsDrawable();
        final SignalBarsDrawable signalBarsDrawable = this.signalBarsDrawable;
        signalBarsDrawable.setBounds(0, 0, signalBarsDrawable.getIntrinsicWidth(), this.signalBarsDrawable.getIntrinsicHeight());
        Drawable signalBarsDrawable2;
        if (LocaleController.isRTL) {
            signalBarsDrawable2 = this.signalBarsDrawable;
        }
        else {
            signalBarsDrawable2 = drawable;
        }
        if (!LocaleController.isRTL) {
            drawable = this.signalBarsDrawable;
        }
        brandingText.setCompoundDrawables(signalBarsDrawable2, (Drawable)null, drawable, (Drawable)null);
        brandingText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        int gravity;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        brandingText.setGravity(gravity);
        brandingText.setCompoundDrawablePadding(AndroidUtilities.dp(5.0f));
        brandingText.setTextSize(1, 14.0f);
        int n;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        content.addView((View)brandingText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n | 0x30, 18.0f, 18.0f, 18.0f, 0.0f));
        this.brandingText = brandingText;
        final TextView nameText = new TextView((Context)this);
        nameText.setSingleLine();
        nameText.setTextColor(-1);
        nameText.setTextSize(1, 40.0f);
        nameText.setEllipsize(TextUtils$TruncateAt.END);
        int gravity2;
        if (LocaleController.isRTL) {
            gravity2 = 5;
        }
        else {
            gravity2 = 3;
        }
        nameText.setGravity(gravity2);
        nameText.setShadowLayer((float)AndroidUtilities.dp(3.0f), 0.0f, (float)AndroidUtilities.dp(0.6666667f), 1275068416);
        nameText.setTypeface(Typeface.create("sans-serif-light", 0));
        content.addView((View)(this.nameText = nameText), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 16.0f, 43.0f, 18.0f, 0.0f));
        final TextView textView = new TextView((Context)this);
        textView.setTextColor(-855638017);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils$TruncateAt.END);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setShadowLayer((float)AndroidUtilities.dp(3.0f), 0.0f, (float)AndroidUtilities.dp(0.6666667f), 1275068416);
        textView.setTextSize(1, 15.0f);
        int gravity3;
        if (LocaleController.isRTL) {
            gravity3 = 5;
        }
        else {
            gravity3 = 3;
        }
        textView.setGravity(gravity3);
        content.addView((View)(this.stateText = textView), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 18.0f, 98.0f, 18.0f, 0.0f));
        this.durationText = textView;
        final TextView stateText2 = new TextView((Context)this);
        stateText2.setTextColor(-855638017);
        stateText2.setSingleLine();
        stateText2.setEllipsize(TextUtils$TruncateAt.END);
        stateText2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        stateText2.setShadowLayer((float)AndroidUtilities.dp(3.0f), 0.0f, (float)AndroidUtilities.dp(0.6666667f), 1275068416);
        stateText2.setTextSize(1, 15.0f);
        int gravity4;
        if (LocaleController.isRTL) {
            gravity4 = 5;
        }
        else {
            gravity4 = 3;
        }
        stateText2.setGravity(gravity4);
        stateText2.setVisibility(8);
        content.addView((View)(this.stateText2 = stateText2), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 18.0f, 98.0f, 18.0f, 0.0f));
        this.ellSpans = new TextAlphaSpan[] { new TextAlphaSpan(), new TextAlphaSpan(), new TextAlphaSpan() };
        final LinearLayout bottomButtons = new LinearLayout((Context)this);
        bottomButtons.setOrientation(0);
        content.addView((View)bottomButtons, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2, 80));
        final TextView accountNameText = new TextView((Context)this);
        accountNameText.setTextColor(-855638017);
        accountNameText.setSingleLine();
        accountNameText.setEllipsize(TextUtils$TruncateAt.END);
        accountNameText.setShadowLayer((float)AndroidUtilities.dp(3.0f), 0.0f, (float)AndroidUtilities.dp(0.6666667f), 1275068416);
        accountNameText.setTextSize(1, 15.0f);
        int gravity5;
        if (LocaleController.isRTL) {
            gravity5 = 5;
        }
        else {
            gravity5 = 3;
        }
        accountNameText.setGravity(gravity5);
        content.addView((View)(this.accountNameText = accountNameText), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 18.0f, 120.0f, 18.0f, 0.0f));
        final CheckableImageView micToggle = new CheckableImageView((Context)this);
        micToggle.setBackgroundResource(2131165304);
        final Drawable mutate = this.getResources().getDrawable(2131165456).mutate();
        micToggle.setAlpha(204);
        micToggle.setImageDrawable(mutate);
        micToggle.setScaleType(ImageView$ScaleType.CENTER);
        micToggle.setContentDescription((CharSequence)LocaleController.getString("AccDescrMuteMic", 2131558448));
        final FrameLayout frameLayout = new FrameLayout((Context)this);
        frameLayout.addView((View)(this.micToggle = micToggle), (ViewGroup$LayoutParams)LayoutHelper.createFrame(38, 38.0f, 81, 0.0f, 0.0f, 0.0f, 10.0f));
        bottomButtons.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -2, 1.0f));
        final ImageView chatBtn = new ImageView((Context)this);
        final Drawable mutate2 = this.getResources().getDrawable(2131165436).mutate();
        mutate2.setAlpha(204);
        chatBtn.setImageDrawable(mutate2);
        chatBtn.setScaleType(ImageView$ScaleType.CENTER);
        chatBtn.setContentDescription((CharSequence)LocaleController.getString("AccDescrOpenChat", 2131558450));
        final FrameLayout frameLayout2 = new FrameLayout((Context)this);
        frameLayout2.addView((View)(this.chatBtn = chatBtn), (ViewGroup$LayoutParams)LayoutHelper.createFrame(38, 38.0f, 81, 0.0f, 0.0f, 0.0f, 10.0f));
        bottomButtons.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -2, 1.0f));
        final CheckableImageView spkToggle = new CheckableImageView((Context)this);
        spkToggle.setBackgroundResource(2131165304);
        final Drawable mutate3 = this.getResources().getDrawable(2131165476).mutate();
        spkToggle.setAlpha(204);
        spkToggle.setImageDrawable(mutate3);
        spkToggle.setScaleType(ImageView$ScaleType.CENTER);
        spkToggle.setContentDescription((CharSequence)LocaleController.getString("VoipAudioRoutingSpeaker", 2131561060));
        final FrameLayout frameLayout3 = new FrameLayout((Context)this);
        frameLayout3.addView((View)(this.spkToggle = spkToggle), (ViewGroup$LayoutParams)LayoutHelper.createFrame(38, 38.0f, 81, 0.0f, 0.0f, 0.0f, 10.0f));
        bottomButtons.addView((View)frameLayout3, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -2, 1.0f));
        this.bottomButtons = bottomButtons;
        final LinearLayout swipeViewsWrap = new LinearLayout((Context)this);
        swipeViewsWrap.setOrientation(0);
        final CallSwipeView acceptSwipe = new CallSwipeView((Context)this);
        acceptSwipe.setColor(-12207027);
        acceptSwipe.setContentDescription((CharSequence)LocaleController.getString("Accept", 2131558484));
        swipeViewsWrap.addView((View)(this.acceptSwipe = acceptSwipe), (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 70, 1.0f, 4, 4, -35, 4));
        final CallSwipeView declineSwipe = new CallSwipeView((Context)this);
        declineSwipe.setColor(-1696188);
        declineSwipe.setContentDescription((CharSequence)LocaleController.getString("Decline", 2131559223));
        swipeViewsWrap.addView((View)(this.declineSwipe = declineSwipe), (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 70, 1.0f, -35, 4, 4, 4));
        content.addView((View)(this.swipeViewsWrap = swipeViewsWrap), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 80, 20.0f, 0.0f, 20.0f, 68.0f));
        final ImageView acceptBtn = new ImageView((Context)this);
        final FabBackgroundDrawable backgroundDrawable = new FabBackgroundDrawable();
        backgroundDrawable.setColor(-12207027);
        acceptBtn.setBackgroundDrawable((Drawable)backgroundDrawable);
        acceptBtn.setImageResource(2131165431);
        acceptBtn.setScaleType(ImageView$ScaleType.MATRIX);
        final Matrix imageMatrix = new Matrix();
        imageMatrix.setTranslate((float)AndroidUtilities.dp(17.0f), (float)AndroidUtilities.dp(17.0f));
        imageMatrix.postRotate(-135.0f, (float)AndroidUtilities.dp(35.0f), (float)AndroidUtilities.dp(35.0f));
        acceptBtn.setImageMatrix(imageMatrix);
        content.addView(this.acceptBtn = (View)acceptBtn, (ViewGroup$LayoutParams)LayoutHelper.createFrame(78, 78.0f, 83, 20.0f, 0.0f, 0.0f, 68.0f));
        final ImageView declineBtn = new ImageView((Context)this);
        final FabBackgroundDrawable backgroundDrawable2 = new FabBackgroundDrawable();
        backgroundDrawable2.setColor(-1696188);
        declineBtn.setBackgroundDrawable((Drawable)backgroundDrawable2);
        declineBtn.setImageResource(2131165431);
        declineBtn.setScaleType(ImageView$ScaleType.CENTER);
        content.addView(this.declineBtn = (View)declineBtn, (ViewGroup$LayoutParams)LayoutHelper.createFrame(78, 78.0f, 85, 0.0f, 0.0f, 20.0f, 68.0f));
        acceptSwipe.setViewToDrag((View)acceptBtn, false);
        declineSwipe.setViewToDrag((View)declineBtn, true);
        final FrameLayout endBtn = new FrameLayout((Context)this);
        final FabBackgroundDrawable endBtnBg = new FabBackgroundDrawable();
        endBtnBg.setColor(-1696188);
        endBtn.setBackgroundDrawable((Drawable)(this.endBtnBg = endBtnBg));
        final ImageView endBtnIcon = new ImageView((Context)this);
        endBtnIcon.setImageResource(2131165431);
        endBtnIcon.setScaleType(ImageView$ScaleType.CENTER);
        endBtn.addView(this.endBtnIcon = (View)endBtnIcon, (ViewGroup$LayoutParams)LayoutHelper.createFrame(70, 70.0f));
        endBtn.setForeground(this.getResources().getDrawable(2131165376));
        endBtn.setContentDescription((CharSequence)LocaleController.getString("VoipEndCall", 2131561065));
        content.addView(this.endBtn = (View)endBtn, (ViewGroup$LayoutParams)LayoutHelper.createFrame(78, 78.0f, 81, 0.0f, 0.0f, 0.0f, 68.0f));
        final ImageView cancelBtn = new ImageView((Context)this);
        final FabBackgroundDrawable backgroundDrawable3 = new FabBackgroundDrawable();
        backgroundDrawable3.setColor(-1);
        cancelBtn.setBackgroundDrawable((Drawable)backgroundDrawable3);
        cancelBtn.setImageResource(2131165375);
        cancelBtn.setColorFilter(-1996488704);
        cancelBtn.setScaleType(ImageView$ScaleType.CENTER);
        cancelBtn.setVisibility(8);
        cancelBtn.setContentDescription((CharSequence)LocaleController.getString("Cancel", 2131558891));
        content.addView(this.cancelBtn = (View)cancelBtn, (ViewGroup$LayoutParams)LayoutHelper.createFrame(78, 78.0f, 83, 52.0f, 0.0f, 0.0f, 68.0f));
        (this.emojiWrap = new LinearLayout((Context)this)).setOrientation(0);
        this.emojiWrap.setClipToPadding(false);
        this.emojiWrap.setPivotX(0.0f);
        this.emojiWrap.setPivotY(0.0f);
        this.emojiWrap.setPadding(AndroidUtilities.dp(14.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(14.0f), AndroidUtilities.dp(10.0f));
        for (int i = 0; i < 4; ++i) {
            final ImageView imageView = new ImageView((Context)this);
            imageView.setScaleType(ImageView$ScaleType.FIT_XY);
            final LinearLayout emojiWrap = this.emojiWrap;
            float n2;
            if (i == 0) {
                n2 = 0.0f;
            }
            else {
                n2 = 4.0f;
            }
            emojiWrap.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(22, 22, n2, 0.0f, 0.0f, 0.0f));
            this.keyEmojiViews[i] = imageView;
        }
        this.emojiWrap.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final VoIPActivity this$0 = VoIPActivity.this;
                if (this$0.emojiTooltipVisible) {
                    this$0.setEmojiTooltipVisible(false);
                    if (VoIPActivity.this.tooltipHider != null) {
                        VoIPActivity.this.hintTextView.removeCallbacks(VoIPActivity.this.tooltipHider);
                        VoIPActivity.this.tooltipHider = null;
                    }
                }
                final VoIPActivity this$2 = VoIPActivity.this;
                this$2.setEmojiExpanded(this$2.emojiExpanded ^ true);
            }
        });
        final LinearLayout emojiWrap2 = this.emojiWrap;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 3;
        }
        else {
            n3 = 5;
        }
        content.addView((View)emojiWrap2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, n3 | 0x30));
        this.emojiWrap.setOnLongClickListener((View$OnLongClickListener)new View$OnLongClickListener() {
            public boolean onLongClick(final View view) {
                final VoIPActivity this$0 = VoIPActivity.this;
                if (this$0.emojiExpanded) {
                    return false;
                }
                if (this$0.tooltipHider != null) {
                    VoIPActivity.this.hintTextView.removeCallbacks(VoIPActivity.this.tooltipHider);
                    VoIPActivity.this.tooltipHider = null;
                }
                final VoIPActivity this$2 = VoIPActivity.this;
                this$2.setEmojiTooltipVisible(this$2.emojiTooltipVisible ^ true);
                final VoIPActivity this$3 = VoIPActivity.this;
                if (this$3.emojiTooltipVisible) {
                    final TextView access$2500 = this$3.hintTextView;
                    final VoIPActivity this$4 = VoIPActivity.this;
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            VoIPActivity.this.tooltipHider = null;
                            VoIPActivity.this.setEmojiTooltipVisible(false);
                        }
                    };
                    this$4.tooltipHider = runnable;
                    access$2500.postDelayed((Runnable)runnable, 5000L);
                }
                return true;
            }
        });
        (this.emojiExpandedText = new TextView((Context)this)).setTextSize(1, 16.0f);
        this.emojiExpandedText.setTextColor(-1);
        this.emojiExpandedText.setGravity(17);
        this.emojiExpandedText.setAlpha(0.0f);
        content.addView((View)this.emojiExpandedText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 17, 10.0f, 32.0f, 10.0f, 0.0f));
        (this.hintTextView = new CorrectlyMeasuringTextView((Context)this)).setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0f), -231525581));
        this.hintTextView.setTextColor(Theme.getColor("chat_gifSaveHintText"));
        this.hintTextView.setTextSize(1, 14.0f);
        this.hintTextView.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f));
        this.hintTextView.setGravity(17);
        this.hintTextView.setMaxWidth(AndroidUtilities.dp(300.0f));
        this.hintTextView.setAlpha(0.0f);
        content.addView((View)this.hintTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 53, 0.0f, 42.0f, 10.0f, 0.0f));
        final int alpha = this.stateText.getPaint().getAlpha();
        (this.ellAnimator = new AnimatorSet()).playTogether(new Animator[] { (Animator)this.createAlphaAnimator(this.ellSpans[0], 0, alpha, 0, 300), (Animator)this.createAlphaAnimator(this.ellSpans[1], 0, alpha, 150, 300), (Animator)this.createAlphaAnimator(this.ellSpans[2], 0, alpha, 300, 300), (Animator)this.createAlphaAnimator(this.ellSpans[0], alpha, 0, 1000, 400), (Animator)this.createAlphaAnimator(this.ellSpans[1], alpha, 0, 1000, 400), (Animator)this.createAlphaAnimator(this.ellSpans[2], alpha, 0, 1000, 400) });
        this.ellAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            private Runnable restarter = new Runnable() {
                @Override
                public void run() {
                    if (!VoIPActivity.this.isFinishing()) {
                        VoIPActivity.this.ellAnimator.start();
                    }
                }
            };
            
            public void onAnimationEnd(final Animator animator) {
                if (!VoIPActivity.this.isFinishing()) {
                    VoIPActivity.this.content.postDelayed(this.restarter, 300L);
                }
            }
        });
        content.setClipChildren(false);
        return (View)(this.content = content);
    }
    
    private CharSequence getFormattedDebugString() {
        final String debugString = VoIPService.getSharedInstance().getDebugString();
        final SpannableString spannableString = new SpannableString((CharSequence)debugString);
        int index = 0;
        int n;
        do {
            n = index + 1;
            int endIndex;
            if ((endIndex = debugString.indexOf(10, n)) == -1) {
                endIndex = debugString.length();
            }
            final String substring = debugString.substring(index, endIndex);
            if (substring.contains("IN_USE")) {
                spannableString.setSpan((Object)new ForegroundColorSpan(-16711936), index, endIndex, 0);
            }
            else {
                if (!substring.contains(": ")) {
                    continue;
                }
                spannableString.setSpan((Object)new ForegroundColorSpan(-1426063361), index, substring.indexOf(58) + index + 1, 0);
            }
        } while ((index = debugString.indexOf(10, n)) != -1);
        return (CharSequence)spannableString;
    }
    
    private void hideRetry() {
        final AnimatorSet retryAnim = this.retryAnim;
        if (retryAnim != null) {
            retryAnim.cancel();
        }
        this.retrying = false;
        ObjectAnimator objectAnimator;
        if (Build$VERSION.SDK_INT >= 21) {
            objectAnimator = ObjectAnimator.ofArgb((Object)this.endBtnBg, "color", new int[] { -12207027, -1696188 });
        }
        else {
            objectAnimator = ObjectAnimator.ofInt((Object)this.endBtnBg, "color", new int[] { -12207027, -1696188 });
            objectAnimator.setEvaluator((TypeEvaluator)new ArgbEvaluator());
        }
        final AnimatorSet retryAnim2 = new AnimatorSet();
        retryAnim2.playTogether(new Animator[] { (Animator)objectAnimator, (Animator)ObjectAnimator.ofFloat((Object)this.endBtnIcon, "rotation", new float[] { -135.0f, 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.endBtn, "translationX", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.cancelBtn, "alpha", new float[] { 0.0f }) });
        retryAnim2.setStartDelay(200L);
        retryAnim2.setDuration(300L);
        retryAnim2.setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT);
        retryAnim2.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                VoIPActivity.this.cancelBtn.setVisibility(8);
                VoIPActivity.this.endBtn.setEnabled(true);
                VoIPActivity.this.retryAnim = null;
            }
        });
        (this.retryAnim = retryAnim2).start();
    }
    
    private void sendTextMessage(final String s) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                SendMessagesHelper.getInstance(VoIPActivity.this.currentAccount).sendMessage(s, VoIPActivity.this.user.id, null, null, false, null, null, null);
            }
        });
    }
    
    private void setEmojiExpanded(final boolean emojiExpanded) {
        if (this.emojiExpanded == emojiExpanded) {
            return;
        }
        this.emojiExpanded = emojiExpanded;
        final AnimatorSet emojiAnimator = this.emojiAnimator;
        if (emojiAnimator != null) {
            emojiAnimator.cancel();
        }
        if (emojiExpanded) {
            final int[] array2;
            final int[] array = array2 = new int[2];
            array2[1] = (array2[0] = 0);
            final int[] array4;
            final int[] array3 = array4 = new int[2];
            array4[1] = (array4[0] = 0);
            this.emojiWrap.getLocationInWindow(array);
            this.emojiExpandedText.getLocationInWindow(array3);
            final Rect rect = new Rect();
            this.getWindow().getDecorView().getGlobalVisibleRect(rect);
            final int n = array3[1];
            final int n2 = array[1];
            final int height = this.emojiWrap.getHeight();
            final int dp = AndroidUtilities.dp(32.0f);
            final int height2 = this.emojiWrap.getHeight();
            final int n3 = rect.width() / 2;
            final int n4 = Math.round(this.emojiWrap.getWidth() * 2.5f) / 2;
            final int n5 = array[0];
            final AnimatorSet emojiAnimator2 = new AnimatorSet();
            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.emojiWrap, "translationY", new float[] { (float)(n - (n2 + height) - dp - height2) });
            final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this.emojiWrap, "translationX", new float[] { (float)(n3 - n4 - n5) });
            final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)this.emojiWrap, "scaleX", new float[] { 2.5f });
            final ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat((Object)this.emojiWrap, "scaleY", new float[] { 2.5f });
            final ImageView blurOverlayView1 = this.blurOverlayView1;
            final ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat((Object)blurOverlayView1, "alpha", new float[] { blurOverlayView1.getAlpha(), 1.0f, 1.0f });
            final ImageView blurOverlayView2 = this.blurOverlayView2;
            emojiAnimator2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2, (Animator)ofFloat3, (Animator)ofFloat4, (Animator)ofFloat5, (Animator)ObjectAnimator.ofFloat((Object)blurOverlayView2, "alpha", new float[] { blurOverlayView2.getAlpha(), this.blurOverlayView2.getAlpha(), 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.emojiExpandedText, "alpha", new float[] { 1.0f }) });
            emojiAnimator2.setDuration(300L);
            emojiAnimator2.setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT);
            (this.emojiAnimator = emojiAnimator2).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    VoIPActivity.this.emojiAnimator = null;
                }
            });
            emojiAnimator2.start();
        }
        else {
            final AnimatorSet emojiAnimator3 = new AnimatorSet();
            final ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat((Object)this.emojiWrap, "translationX", new float[] { 0.0f });
            final ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat((Object)this.emojiWrap, "translationY", new float[] { 0.0f });
            final ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat((Object)this.emojiWrap, "scaleX", new float[] { 1.0f });
            final ObjectAnimator ofFloat9 = ObjectAnimator.ofFloat((Object)this.emojiWrap, "scaleY", new float[] { 1.0f });
            final ImageView blurOverlayView3 = this.blurOverlayView1;
            final ObjectAnimator ofFloat10 = ObjectAnimator.ofFloat((Object)blurOverlayView3, "alpha", new float[] { blurOverlayView3.getAlpha(), this.blurOverlayView1.getAlpha(), 0.0f });
            final ImageView blurOverlayView4 = this.blurOverlayView2;
            emojiAnimator3.playTogether(new Animator[] { (Animator)ofFloat6, (Animator)ofFloat7, (Animator)ofFloat8, (Animator)ofFloat9, (Animator)ofFloat10, (Animator)ObjectAnimator.ofFloat((Object)blurOverlayView4, "alpha", new float[] { blurOverlayView4.getAlpha(), 0.0f, 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.emojiExpandedText, "alpha", new float[] { 0.0f }) });
            emojiAnimator3.setDuration(300L);
            emojiAnimator3.setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT);
            (this.emojiAnimator = emojiAnimator3).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    VoIPActivity.this.emojiAnimator = null;
                }
            });
            emojiAnimator3.start();
        }
    }
    
    private void setEmojiTooltipVisible(final boolean emojiTooltipVisible) {
        this.emojiTooltipVisible = emojiTooltipVisible;
        final Animator tooltipAnim = this.tooltipAnim;
        if (tooltipAnim != null) {
            tooltipAnim.cancel();
        }
        this.hintTextView.setVisibility(0);
        final TextView hintTextView = this.hintTextView;
        float n;
        if (emojiTooltipVisible) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)hintTextView, "alpha", new float[] { n });
        ofFloat.setDuration(300L);
        ofFloat.setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT);
        ofFloat.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                VoIPActivity.this.tooltipAnim = null;
            }
        });
        ((ObjectAnimator)(this.tooltipAnim = (Animator)ofFloat)).start();
    }
    
    private void setStateTextAnimated(String upperCase, final boolean b) {
        if (upperCase.equals(this.lastStateText)) {
            return;
        }
        this.lastStateText = upperCase;
        final Animator textChangingAnim = this.textChangingAnim;
        if (textChangingAnim != null) {
            textChangingAnim.cancel();
        }
        if (b) {
            if (!this.ellAnimator.isRunning()) {
                this.ellAnimator.start();
            }
            upperCase = (String)new SpannableStringBuilder((CharSequence)upperCase.toUpperCase());
            final TextAlphaSpan[] ellSpans = this.ellSpans;
            for (int length = ellSpans.length, i = 0; i < length; ++i) {
                ellSpans[i].setAlpha(0);
            }
            final SpannableString spannableString = new SpannableString((CharSequence)"...");
            spannableString.setSpan((Object)this.ellSpans[0], 0, 1, 0);
            spannableString.setSpan((Object)this.ellSpans[1], 1, 2, 0);
            spannableString.setSpan((Object)this.ellSpans[2], 2, 3, 0);
            ((SpannableStringBuilder)upperCase).append((CharSequence)spannableString);
        }
        else {
            if (this.ellAnimator.isRunning()) {
                this.ellAnimator.cancel();
            }
            upperCase = upperCase.toUpperCase();
        }
        this.stateText2.setText((CharSequence)upperCase);
        this.stateText2.setVisibility(0);
        final TextView stateText = this.stateText;
        float pivotX;
        if (LocaleController.isRTL) {
            pivotX = (float)stateText.getWidth();
        }
        else {
            pivotX = 0.0f;
        }
        stateText.setPivotX(pivotX);
        final TextView stateText2 = this.stateText;
        stateText2.setPivotY((float)(stateText2.getHeight() / 2));
        final TextView stateText3 = this.stateText2;
        float pivotX2;
        if (LocaleController.isRTL) {
            pivotX2 = (float)this.stateText.getWidth();
        }
        else {
            pivotX2 = 0.0f;
        }
        stateText3.setPivotX(pivotX2);
        this.stateText2.setPivotY((float)(this.stateText.getHeight() / 2));
        this.durationText = this.stateText2;
        final AnimatorSet textChangingAnim2 = new AnimatorSet();
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.stateText2, "alpha", new float[] { 0.0f, 1.0f });
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this.stateText2, "translationY", new float[] { (float)(this.stateText.getHeight() / 2), 0.0f });
        final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)this.stateText2, "scaleX", new float[] { 0.7f, 1.0f });
        final ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat((Object)this.stateText2, "scaleY", new float[] { 0.7f, 1.0f });
        final ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat((Object)this.stateText, "alpha", new float[] { 1.0f, 0.0f });
        final TextView stateText4 = this.stateText;
        textChangingAnim2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2, (Animator)ofFloat3, (Animator)ofFloat4, (Animator)ofFloat5, (Animator)ObjectAnimator.ofFloat((Object)stateText4, "translationY", new float[] { 0.0f, (float)(-stateText4.getHeight() / 2) }), (Animator)ObjectAnimator.ofFloat((Object)this.stateText, "scaleX", new float[] { 1.0f, 0.7f }), (Animator)ObjectAnimator.ofFloat((Object)this.stateText, "scaleY", new float[] { 1.0f, 0.7f }) });
        textChangingAnim2.setDuration(200L);
        textChangingAnim2.setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT);
        textChangingAnim2.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                VoIPActivity.this.textChangingAnim = null;
                VoIPActivity.this.stateText2.setVisibility(8);
                final VoIPActivity this$0 = VoIPActivity.this;
                this$0.durationText = this$0.stateText;
                VoIPActivity.this.stateText.setTranslationY(0.0f);
                VoIPActivity.this.stateText.setScaleX(1.0f);
                VoIPActivity.this.stateText.setScaleY(1.0f);
                VoIPActivity.this.stateText.setAlpha(1.0f);
                VoIPActivity.this.stateText.setText(VoIPActivity.this.stateText2.getText());
            }
        });
        ((AnimatorSet)(this.textChangingAnim = (Animator)textChangingAnim2)).start();
    }
    
    private void showDebugAlert() {
        if (VoIPService.getSharedInstance() == null) {
            return;
        }
        VoIPService.getSharedInstance().forceRating();
        final LinearLayout linearLayout = new LinearLayout((Context)this);
        linearLayout.setOrientation(1);
        linearLayout.setBackgroundColor(-872415232);
        final int dp = AndroidUtilities.dp(16.0f);
        final int n = dp * 2;
        linearLayout.setPadding(dp, n, dp, n);
        final TextView textView = new TextView((Context)this);
        textView.setTextColor(-1);
        textView.setTextSize(1, 15.0f);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setGravity(17);
        final StringBuilder sb = new StringBuilder();
        sb.append("libtgvoip v");
        sb.append(VoIPController.getVersion());
        textView.setText((CharSequence)sb.toString());
        linearLayout.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 16.0f));
        final ScrollView scrollView = new ScrollView((Context)this);
        final TextView textView2 = new TextView((Context)this);
        textView2.setTypeface(Typeface.MONOSPACE);
        textView2.setTextSize(1, 11.0f);
        textView2.setMaxWidth(AndroidUtilities.dp(350.0f));
        textView2.setTextColor(-1);
        textView2.setText(this.getFormattedDebugString());
        scrollView.addView((View)textView2);
        linearLayout.addView((View)scrollView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -1, 1.0f));
        final TextView textView3 = new TextView((Context)this);
        textView3.setBackgroundColor(-1);
        textView3.setTextColor(-16777216);
        textView3.setPadding(dp, dp, dp, dp);
        textView3.setTextSize(1, 15.0f);
        textView3.setText((CharSequence)LocaleController.getString("Close", 2131559117));
        linearLayout.addView((View)textView3, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 1, 0, 16, 0, 0));
        final WindowManager windowManager = (WindowManager)this.getSystemService("window");
        windowManager.addView((View)linearLayout, (ViewGroup$LayoutParams)new WindowManager$LayoutParams(-1, -1, 1000, 0, -3));
        textView3.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                windowManager.removeView((View)linearLayout);
            }
        });
        linearLayout.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                if (!VoIPActivity.this.isFinishing()) {
                    if (VoIPService.getSharedInstance() != null) {
                        textView2.setText(VoIPActivity.this.getFormattedDebugString());
                        linearLayout.postDelayed((Runnable)this, 500L);
                    }
                }
            }
        }, 500L);
    }
    
    private void showErrorDialog(final CharSequence message) {
        final AlertDialog show = ((AlertDialog.Builder)new DarkAlertDialog.Builder((Context)this)).setTitle(LocaleController.getString("VoipFailed", 2131561068)).setMessage(message).setPositiveButton(LocaleController.getString("OK", 2131560097), null).show();
        show.setCanceledOnTouchOutside(true);
        show.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
            public void onDismiss(final DialogInterface dialogInterface) {
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
        final SharedPreferences sharedPreferences = this.getSharedPreferences("mainconfig", 0);
        final String[] array = { sharedPreferences.getString("quick_reply_msg1", LocaleController.getString("QuickReplyDefault1", 2131560524)), sharedPreferences.getString("quick_reply_msg2", LocaleController.getString("QuickReplyDefault2", 2131560525)), sharedPreferences.getString("quick_reply_msg3", LocaleController.getString("QuickReplyDefault3", 2131560526)), sharedPreferences.getString("quick_reply_msg4", LocaleController.getString("QuickReplyDefault4", 2131560527)) };
        final LinearLayout customView = new LinearLayout((Context)this);
        customView.setOrientation(1);
        final BottomSheet bottomSheet = new BottomSheet((Context)this, true, 0);
        if (Build$VERSION.SDK_INT >= 21) {
            this.getWindow().setNavigationBarColor(-13948117);
            bottomSheet.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
                public void onDismiss(final DialogInterface dialogInterface) {
                    VoIPActivity.this.getWindow().setNavigationBarColor(0);
                }
            });
        }
        final View$OnClickListener onClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                bottomSheet.dismiss();
                if (VoIPService.getSharedInstance() != null) {
                    VoIPService.getSharedInstance().declineIncomingCall(4, new Runnable() {
                        @Override
                        public void run() {
                            VoIPActivity.this.sendTextMessage((String)view.getTag());
                        }
                    });
                }
            }
        };
        for (final String tag : array) {
            final BottomSheet.BottomSheetCell bottomSheetCell = new BottomSheet.BottomSheetCell((Context)this, 0);
            bottomSheetCell.setTextAndIcon(tag, 0);
            bottomSheetCell.setTextColor(-1);
            bottomSheetCell.setTag((Object)tag);
            bottomSheetCell.setOnClickListener((View$OnClickListener)onClickListener);
            customView.addView((View)bottomSheetCell);
        }
        final FrameLayout frameLayout = new FrameLayout((Context)this);
        final BottomSheet.BottomSheetCell bottomSheetCell2 = new BottomSheet.BottomSheetCell((Context)this, 0);
        bottomSheetCell2.setTextAndIcon(LocaleController.getString("QuickReplyCustom", 2131560523), 0);
        bottomSheetCell2.setTextColor(-1);
        frameLayout.addView((View)bottomSheetCell2);
        final FrameLayout frameLayout2 = new FrameLayout((Context)this);
        final EditText editText = new EditText((Context)this);
        editText.setTextSize(1, 16.0f);
        editText.setTextColor(-1);
        editText.setHintTextColor(DarkTheme.getColor("chat_messagePanelHint"));
        editText.setBackgroundDrawable((Drawable)null);
        editText.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(11.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f));
        editText.setHint((CharSequence)LocaleController.getString("QuickReplyCustom", 2131560523));
        editText.setMinHeight(AndroidUtilities.dp(48.0f));
        editText.setGravity(80);
        editText.setMaxLines(4);
        editText.setSingleLine(false);
        editText.setInputType(editText.getInputType() | 0x4000 | 0x20000);
        int n;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        float n2;
        if (LocaleController.isRTL) {
            n2 = 48.0f;
        }
        else {
            n2 = 0.0f;
        }
        float n3;
        if (LocaleController.isRTL) {
            n3 = 0.0f;
        }
        else {
            n3 = 48.0f;
        }
        frameLayout2.addView((View)editText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n, n2, 0.0f, n3, 0.0f));
        final ImageView imageView = new ImageView((Context)this);
        imageView.setScaleType(ImageView$ScaleType.CENTER);
        imageView.setImageDrawable(DarkTheme.getThemedDrawable((Context)this, 2131165468, "chat_messagePanelSend"));
        if (LocaleController.isRTL) {
            imageView.setScaleX(-0.1f);
        }
        else {
            imageView.setScaleX(0.1f);
        }
        imageView.setScaleY(0.1f);
        imageView.setAlpha(0.0f);
        int n4;
        if (LocaleController.isRTL) {
            n4 = 3;
        }
        else {
            n4 = 5;
        }
        frameLayout2.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, n4 | 0x50));
        imageView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (editText.length() == 0) {
                    return;
                }
                bottomSheet.dismiss();
                if (VoIPService.getSharedInstance() != null) {
                    VoIPService.getSharedInstance().declineIncomingCall(4, new Runnable() {
                        @Override
                        public void run() {
                            final View$OnClickListener this$1 = (View$OnClickListener)View$OnClickListener.this;
                            VoIPActivity.this.sendTextMessage(editText.getText().toString());
                        }
                    });
                }
            }
        });
        imageView.setVisibility(4);
        final ImageView imageView2 = new ImageView((Context)this);
        imageView2.setScaleType(ImageView$ScaleType.CENTER);
        imageView2.setImageDrawable(DarkTheme.getThemedDrawable((Context)this, 2131165375, "chat_messagePanelIcons"));
        int n5;
        if (LocaleController.isRTL) {
            n5 = 3;
        }
        else {
            n5 = 5;
        }
        frameLayout2.addView((View)imageView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, n5 | 0x50));
        imageView2.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                frameLayout2.setVisibility(8);
                bottomSheetCell2.setVisibility(0);
                editText.setText((CharSequence)"");
                ((InputMethodManager)VoIPActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });
        editText.addTextChangedListener((TextWatcher)new TextWatcher() {
            boolean prevState = false;
            
            public void afterTextChanged(final Editable editable) {
                final boolean prevState = editable.length() > 0;
                if (this.prevState != prevState) {
                    this.prevState = prevState;
                    if (prevState) {
                        imageView.setVisibility(0);
                        final ViewPropertyAnimator alpha = imageView.animate().alpha(1.0f);
                        float n;
                        if (LocaleController.isRTL) {
                            n = -1.0f;
                        }
                        else {
                            n = 1.0f;
                        }
                        alpha.scaleX(n).scaleY(1.0f).setDuration(200L).setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT).start();
                        imageView2.animate().alpha(0.0f).scaleX(0.1f).scaleY(0.1f).setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT).setDuration(200L).withEndAction((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                imageView2.setVisibility(4);
                            }
                        }).start();
                    }
                    else {
                        imageView2.setVisibility(0);
                        imageView2.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(200L).setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT).start();
                        final ViewPropertyAnimator alpha2 = imageView.animate().alpha(0.0f);
                        float n2;
                        if (LocaleController.isRTL) {
                            n2 = -0.1f;
                        }
                        else {
                            n2 = 0.1f;
                        }
                        alpha2.scaleX(n2).scaleY(0.1f).setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT).setDuration(200L).withEndAction((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                imageView.setVisibility(4);
                            }
                        }).start();
                    }
                }
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        frameLayout2.setVisibility(8);
        frameLayout.addView((View)frameLayout2);
        bottomSheetCell2.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                frameLayout2.setVisibility(0);
                bottomSheetCell2.setVisibility(4);
                editText.requestFocus();
                ((InputMethodManager)VoIPActivity.this.getSystemService("input_method")).showSoftInput((View)editText, 0);
            }
        });
        customView.addView((View)frameLayout);
        bottomSheet.setCustomView((View)customView);
        bottomSheet.setBackgroundColor(-13948117);
        bottomSheet.show();
    }
    
    private void showRetry() {
        final AnimatorSet retryAnim = this.retryAnim;
        if (retryAnim != null) {
            retryAnim.cancel();
        }
        this.endBtn.setEnabled(false);
        this.retrying = true;
        this.cancelBtn.setVisibility(0);
        this.cancelBtn.setAlpha(0.0f);
        final AnimatorSet retryAnim2 = new AnimatorSet();
        ObjectAnimator objectAnimator;
        if (Build$VERSION.SDK_INT >= 21) {
            objectAnimator = ObjectAnimator.ofArgb((Object)this.endBtnBg, "color", new int[] { -1696188, -12207027 });
        }
        else {
            objectAnimator = ObjectAnimator.ofInt((Object)this.endBtnBg, "color", new int[] { -1696188, -12207027 });
            objectAnimator.setEvaluator((TypeEvaluator)new ArgbEvaluator());
        }
        retryAnim2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.cancelBtn, "alpha", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.endBtn, "translationX", new float[] { 0.0f, (float)(this.content.getWidth() / 2 - AndroidUtilities.dp(52.0f) - this.endBtn.getWidth() / 2) }), (Animator)objectAnimator, (Animator)ObjectAnimator.ofFloat((Object)this.endBtnIcon, "rotation", new float[] { 0.0f, -135.0f }) });
        retryAnim2.setStartDelay(200L);
        retryAnim2.setDuration(300L);
        retryAnim2.setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT);
        retryAnim2.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                VoIPActivity.this.retryAnim = null;
                VoIPActivity.this.endBtn.setEnabled(true);
            }
        });
        (this.retryAnim = retryAnim2).start();
    }
    
    private void startUpdatingCallDuration() {
        new Runnable() {
            @Override
            public void run() {
                if (!VoIPActivity.this.isFinishing()) {
                    if (VoIPService.getSharedInstance() != null) {
                        if (VoIPActivity.this.callState == 3 || VoIPActivity.this.callState == 5) {
                            final long n = VoIPService.getSharedInstance().getCallDuration() / 1000L;
                            final TextView access$3100 = VoIPActivity.this.durationText;
                            Object[] args;
                            String format;
                            if (n > 3600L) {
                                args = new Object[] { n / 3600L, n % 3600L / 60L, n % 60L };
                                format = "%d:%02d:%02d";
                            }
                            else {
                                args = new Object[] { n / 60L, n % 60L };
                                format = "%d:%02d";
                            }
                            access$3100.setText((CharSequence)String.format(format, args));
                            VoIPActivity.this.durationText.postDelayed((Runnable)this, 500L);
                        }
                    }
                }
            }
        }.run();
    }
    
    private void updateBlurredPhotos(final ImageReceiver.BitmapHolder bitmapHolder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap$Config.ARGB_8888);
                    final Canvas canvas = new Canvas(bitmap);
                    canvas.drawBitmap(bitmapHolder.bitmap, (Rect)null, new Rect(0, 0, 150, 150), new Paint(2));
                    Utilities.blurBitmap(bitmap, 3, 0, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
                    final Palette generate = Palette.from(bitmapHolder.bitmap).generate();
                    final Paint paint = new Paint();
                    paint.setColor((generate.getDarkMutedColor(-11242343) & 0xFFFFFF) | 0x44000000);
                    canvas.drawColor(637534208);
                    canvas.drawRect(0.0f, 0.0f, (float)canvas.getWidth(), (float)canvas.getHeight(), paint);
                    final Bitmap bitmap2 = Bitmap.createBitmap(50, 50, Bitmap$Config.ARGB_8888);
                    final Canvas canvas2 = new Canvas(bitmap2);
                    canvas2.drawBitmap(bitmapHolder.bitmap, (Rect)null, new Rect(0, 0, 50, 50), new Paint(2));
                    Utilities.blurBitmap(bitmap2, 3, 0, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                    paint.setAlpha(102);
                    canvas2.drawRect(0.0f, 0.0f, (float)canvas2.getWidth(), (float)canvas2.getHeight(), paint);
                    VoIPActivity.this.blurredPhoto1 = bitmap;
                    VoIPActivity.this.blurredPhoto2 = bitmap2;
                    VoIPActivity.this.runOnUiThread((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            VoIPActivity.this.blurOverlayView1.setImageBitmap(VoIPActivity.this.blurredPhoto1);
                            VoIPActivity.this.blurOverlayView2.setImageBitmap(VoIPActivity.this.blurredPhoto2);
                            bitmapHolder.release();
                        }
                    });
                }
                catch (Throwable t) {}
            }
        }).start();
    }
    
    private void updateKeyView() {
        if (VoIPService.getSharedInstance() == null) {
            return;
        }
        new IdenticonDrawable().setColors(new int[] { 16777215, -1, -1711276033, 872415231 });
        Object o = new TLRPC.TL_encryptedChat();
        while (true) {
            try {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byteArrayOutputStream.write(VoIPService.getSharedInstance().getEncryptionKey());
                byteArrayOutputStream.write(VoIPService.getSharedInstance().getGA());
                ((TLRPC.EncryptedChat)o).auth_key = byteArrayOutputStream.toByteArray();
                o = ((TLRPC.EncryptedChat)o).auth_key;
                o = EncryptionKeyEmojifier.emojifyForCall(Utilities.computeSHA256((byte[])o, 0, ((byte[])o).length));
                final LinearLayout emojiWrap = this.emojiWrap;
                final StringBuilder sb = new StringBuilder();
                sb.append(LocaleController.getString("EncryptionKey", 2131559360));
                sb.append(", ");
                sb.append(TextUtils.join((CharSequence)", ", (Object[])o));
                emojiWrap.setContentDescription((CharSequence)sb.toString());
                for (int i = 0; i < 4; ++i) {
                    final Emoji.EmojiDrawable emojiDrawable = Emoji.getEmojiDrawable(o[i]);
                    if (emojiDrawable != null) {
                        emojiDrawable.setBounds(0, 0, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(22.0f));
                        this.keyEmojiViews[i].setImageDrawable((Drawable)emojiDrawable);
                    }
                }
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    public void didReceivedNotification(final int n, int i, final Object... array) {
        if (n == NotificationCenter.emojiDidLoad) {
            final ImageView[] keyEmojiViews = this.keyEmojiViews;
            int length;
            for (length = keyEmojiViews.length, i = 0; i < length; ++i) {
                keyEmojiViews[i].invalidate();
            }
        }
        if (n == NotificationCenter.closeInCallActivity) {
            this.finish();
        }
    }
    
    public void onAudioSettingsChanged() {
        final VoIPBaseService sharedInstance = VoIPBaseService.getSharedInstance();
        if (sharedInstance == null) {
            return;
        }
        this.micToggle.setChecked(sharedInstance.isMicMute());
        if (!sharedInstance.hasEarpiece() && !sharedInstance.isBluetoothHeadsetConnected()) {
            this.spkToggle.setVisibility(4);
        }
        else {
            this.spkToggle.setVisibility(0);
            if (!sharedInstance.hasEarpiece()) {
                this.spkToggle.setImageResource(2131165428);
                this.spkToggle.setChecked(sharedInstance.isSpeakerphoneOn());
            }
            else if (sharedInstance.isBluetoothHeadsetConnected()) {
                final int currentAudioRoute = sharedInstance.getCurrentAudioRoute();
                if (currentAudioRoute != 0) {
                    if (currentAudioRoute != 1) {
                        if (currentAudioRoute == 2) {
                            this.spkToggle.setImageResource(2131165428);
                        }
                    }
                    else {
                        this.spkToggle.setImageResource(2131165476);
                    }
                }
                else {
                    this.spkToggle.setImageResource(2131165461);
                }
                this.spkToggle.setChecked(false);
            }
            else {
                this.spkToggle.setImageResource(2131165476);
                this.spkToggle.setChecked(sharedInstance.isSpeakerphoneOn());
            }
        }
    }
    
    public void onBackPressed() {
        if (this.emojiExpanded) {
            this.setEmojiExpanded(false);
            return;
        }
        if (!this.isIncomingWaiting) {
            super.onBackPressed();
        }
    }
    
    protected void onCreate(final Bundle bundle) {
        this.requestWindowFeature(1);
        this.getWindow().addFlags(524288);
        super.onCreate(bundle);
        if (VoIPService.getSharedInstance() == null) {
            this.finish();
            return;
        }
        this.currentAccount = VoIPService.getSharedInstance().getAccount();
        if (this.currentAccount == -1) {
            this.finish();
            return;
        }
        if ((this.getResources().getConfiguration().screenLayout & 0xF) < 3) {
            this.setRequestedOrientation(1);
        }
        final View contentView = this.createContentView();
        this.setContentView(contentView);
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 21) {
            this.getWindow().addFlags(Integer.MIN_VALUE);
            this.getWindow().setStatusBarColor(0);
            this.getWindow().setNavigationBarColor(0);
            this.getWindow().getDecorView().setSystemUiVisibility(1792);
        }
        else if (sdk_INT >= 19) {
            this.getWindow().addFlags(201326592);
            this.getWindow().getDecorView().setSystemUiVisibility(1792);
        }
        this.user = VoIPService.getSharedInstance().getUser();
        if (this.user.photo != null) {
            this.photoView.getImageReceiver().setDelegate((ImageReceiver.ImageReceiverDelegate)new ImageReceiver.ImageReceiverDelegate() {
                @Override
                public void didSetImage(final ImageReceiver imageReceiver, final boolean b, final boolean b2) {
                    final ImageReceiver.BitmapHolder bitmapSafe = imageReceiver.getBitmapSafe();
                    if (bitmapSafe != null) {
                        VoIPActivity.this.updateBlurredPhotos(bitmapSafe);
                    }
                }
            });
            this.photoView.setImage(ImageLocation.getForUser(this.user, true), null, (Drawable)new ColorDrawable(-16777216), this.user);
            this.photoView.setLayerType(2, (Paint)null);
        }
        else {
            this.photoView.setVisibility(8);
            contentView.setBackgroundDrawable((Drawable)new GradientDrawable(GradientDrawable$Orientation.TOP_BOTTOM, new int[] { -14994098, -14328963 }));
        }
        this.getWindow().setBackgroundDrawable((Drawable)new ColorDrawable(0));
        this.setVolumeControlStream(0);
        this.nameText.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            private int tapCount = 0;
            
            public void onClick(final View view) {
                if (!BuildVars.DEBUG_VERSION) {
                    final int tapCount = this.tapCount;
                    if (tapCount != 9) {
                        this.tapCount = tapCount + 1;
                        return;
                    }
                }
                VoIPActivity.this.showDebugAlert();
                this.tapCount = 0;
            }
        });
        this.endBtn.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                VoIPActivity.this.endBtn.setEnabled(false);
                if (VoIPActivity.this.retrying) {
                    final Intent intent = new Intent((Context)VoIPActivity.this, (Class)VoIPService.class);
                    intent.putExtra("user_id", VoIPActivity.this.user.id);
                    intent.putExtra("is_outgoing", true);
                    intent.putExtra("start_incall_activity", false);
                    intent.putExtra("account", VoIPActivity.this.currentAccount);
                    try {
                        VoIPActivity.this.startService(intent);
                    }
                    catch (Throwable t) {
                        FileLog.e(t);
                    }
                    VoIPActivity.this.hideRetry();
                    VoIPActivity.this.endBtn.postDelayed((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (VoIPService.getSharedInstance() == null && !VoIPActivity.this.isFinishing()) {
                                VoIPActivity.this.endBtn.postDelayed((Runnable)this, 100L);
                                return;
                            }
                            if (VoIPService.getSharedInstance() != null) {
                                VoIPService.getSharedInstance().registerStateListener((VoIPBaseService.StateListener)VoIPActivity.this);
                            }
                        }
                    }, 100L);
                    return;
                }
                if (VoIPService.getSharedInstance() != null) {
                    VoIPService.getSharedInstance().hangUp();
                }
            }
        });
        this.spkToggle.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final VoIPService sharedInstance = VoIPService.getSharedInstance();
                if (sharedInstance == null) {
                    return;
                }
                sharedInstance.toggleSpeakerphoneOrShowRouteSheet(VoIPActivity.this);
            }
        });
        this.micToggle.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (VoIPService.getSharedInstance() == null) {
                    VoIPActivity.this.finish();
                    return;
                }
                final boolean b = VoIPActivity.this.micToggle.isChecked() ^ true;
                VoIPActivity.this.micToggle.setChecked(b);
                VoIPService.getSharedInstance().setMicMute(b);
            }
        });
        this.chatBtn.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (VoIPActivity.this.isIncomingWaiting) {
                    VoIPActivity.this.showMessagesSheet();
                    return;
                }
                final Intent intent = new Intent(ApplicationLoader.applicationContext, (Class)LaunchActivity.class);
                final StringBuilder sb = new StringBuilder();
                sb.append("com.tmessages.openchat");
                sb.append(Math.random());
                sb.append(Integer.MAX_VALUE);
                intent.setAction(sb.toString());
                intent.putExtra("currentAccount", VoIPActivity.this.currentAccount);
                intent.setFlags(32768);
                intent.putExtra("userId", VoIPActivity.this.user.id);
                VoIPActivity.this.startActivity(intent);
                VoIPActivity.this.finish();
            }
        });
        this.spkToggle.setChecked(((AudioManager)this.getSystemService("audio")).isSpeakerphoneOn());
        this.micToggle.setChecked(VoIPService.getSharedInstance().isMicMute());
        this.onAudioSettingsChanged();
        final TextView nameText = this.nameText;
        final TLRPC.User user = this.user;
        nameText.setText((CharSequence)ContactsController.formatName(user.first_name, user.last_name));
        VoIPService.getSharedInstance().registerStateListener((VoIPBaseService.StateListener)this);
        this.acceptSwipe.setListener((CallSwipeView.Listener)new CallSwipeView.Listener() {
            @Override
            public void onDragCancel() {
                if (VoIPActivity.this.currentDeclineAnim != null) {
                    VoIPActivity.this.currentDeclineAnim.cancel();
                }
                final AnimatorSet set = new AnimatorSet();
                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)VoIPActivity.this.declineSwipe, "alpha", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)VoIPActivity.this.declineBtn, "alpha", new float[] { 1.0f }) });
                set.setDuration(200L);
                set.setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT);
                set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        VoIPActivity.this.currentDeclineAnim = null;
                    }
                });
                VoIPActivity.this.currentDeclineAnim = (Animator)set;
                set.start();
                VoIPActivity.this.declineSwipe.startAnimatingArrows();
            }
            
            @Override
            public void onDragComplete() {
                VoIPActivity.this.acceptSwipe.setEnabled(false);
                VoIPActivity.this.declineSwipe.setEnabled(false);
                if (VoIPService.getSharedInstance() == null) {
                    VoIPActivity.this.finish();
                    return;
                }
                VoIPActivity.this.didAcceptFromHere = true;
                if (Build$VERSION.SDK_INT >= 23 && VoIPActivity.this.checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
                    VoIPActivity.this.requestPermissions(new String[] { "android.permission.RECORD_AUDIO" }, 101);
                }
                else {
                    VoIPService.getSharedInstance().acceptIncomingCall();
                    VoIPActivity.this.callAccepted();
                }
            }
            
            @Override
            public void onDragStart() {
                if (VoIPActivity.this.currentDeclineAnim != null) {
                    VoIPActivity.this.currentDeclineAnim.cancel();
                }
                final AnimatorSet set = new AnimatorSet();
                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)VoIPActivity.this.declineSwipe, "alpha", new float[] { 0.2f }), (Animator)ObjectAnimator.ofFloat((Object)VoIPActivity.this.declineBtn, "alpha", new float[] { 0.2f }) });
                set.setDuration(200L);
                set.setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT);
                set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        VoIPActivity.this.currentDeclineAnim = null;
                    }
                });
                VoIPActivity.this.currentDeclineAnim = (Animator)set;
                set.start();
                VoIPActivity.this.declineSwipe.stopAnimatingArrows();
            }
        });
        this.declineSwipe.setListener((CallSwipeView.Listener)new CallSwipeView.Listener() {
            @Override
            public void onDragCancel() {
                if (VoIPActivity.this.currentAcceptAnim != null) {
                    VoIPActivity.this.currentAcceptAnim.cancel();
                }
                final AnimatorSet set = new AnimatorSet();
                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)VoIPActivity.this.acceptSwipe, "alpha", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)VoIPActivity.this.acceptBtn, "alpha", new float[] { 1.0f }) });
                set.setDuration(200L);
                set.setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT);
                set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        VoIPActivity.this.currentAcceptAnim = null;
                    }
                });
                VoIPActivity.this.currentAcceptAnim = (Animator)set;
                set.start();
                VoIPActivity.this.acceptSwipe.startAnimatingArrows();
            }
            
            @Override
            public void onDragComplete() {
                VoIPActivity.this.acceptSwipe.setEnabled(false);
                VoIPActivity.this.declineSwipe.setEnabled(false);
                if (VoIPService.getSharedInstance() != null) {
                    VoIPService.getSharedInstance().declineIncomingCall(4, null);
                }
                else {
                    VoIPActivity.this.finish();
                }
            }
            
            @Override
            public void onDragStart() {
                if (VoIPActivity.this.currentAcceptAnim != null) {
                    VoIPActivity.this.currentAcceptAnim.cancel();
                }
                final AnimatorSet set = new AnimatorSet();
                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)VoIPActivity.this.acceptSwipe, "alpha", new float[] { 0.2f }), (Animator)ObjectAnimator.ofFloat((Object)VoIPActivity.this.acceptBtn, "alpha", new float[] { 0.2f }) });
                set.setDuration(200L);
                set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        VoIPActivity.this.currentAcceptAnim = null;
                    }
                });
                VoIPActivity.this.currentAcceptAnim = (Animator)set;
                set.start();
                VoIPActivity.this.acceptSwipe.stopAnimatingArrows();
            }
        });
        this.cancelBtn.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                VoIPActivity.this.finish();
            }
        });
        this.getWindow().getDecorView().setKeepScreenOn(true);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.closeInCallActivity);
        this.hintTextView.setText((CharSequence)LocaleController.formatString("CallEmojiKeyTooltip", 2131558872, this.user.first_name));
        this.emojiExpandedText.setText((CharSequence)LocaleController.formatString("CallEmojiKeyTooltip", 2131558872, this.user.first_name));
        if (((AccessibilityManager)this.getSystemService("accessibility")).isTouchExplorationEnabled()) {
            this.nameText.postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    VoIPActivity.this.nameText.sendAccessibilityEvent(8);
                }
            }, 500L);
        }
    }
    
    protected void onDestroy() {
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeInCallActivity);
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().unregisterStateListener((VoIPBaseService.StateListener)this);
        }
        super.onDestroy();
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        if (this.isIncomingWaiting && (n == 25 || n == 24)) {
            if (VoIPService.getSharedInstance() != null) {
                VoIPService.getSharedInstance().stopRinging();
            }
            else {
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(n, keyEvent);
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
    public void onRequestPermissionsResult(final int n, final String[] array, final int[] array2) {
        if (n == 101) {
            if (VoIPService.getSharedInstance() == null) {
                this.finish();
                return;
            }
            if (array2.length > 0 && array2[0] == 0) {
                VoIPService.getSharedInstance().acceptIncomingCall();
                this.callAccepted();
            }
            else {
                if (!this.shouldShowRequestPermissionRationale("android.permission.RECORD_AUDIO")) {
                    VoIPService.getSharedInstance().declineIncomingCall();
                    VoIPHelper.permissionDenied(this, new Runnable() {
                        @Override
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
    
    public void onSignalBarsCountChanged(final int n) {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                VoIPActivity.this.signalBarsCount = n;
                VoIPActivity.this.brandingText.invalidate();
            }
        });
    }
    
    public void onStateChanged(final int callState) {
        final int callState2 = this.callState;
        this.callState = callState;
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final boolean access$3600 = VoIPActivity.this.firstStateChange;
                if (VoIPActivity.this.firstStateChange) {
                    VoIPActivity.this.spkToggle.setChecked(((AudioManager)VoIPActivity.this.getSystemService("audio")).isSpeakerphoneOn());
                    final VoIPActivity this$0 = VoIPActivity.this;
                    final boolean b = callState == 15;
                    this$0.isIncomingWaiting = b;
                    if (b) {
                        VoIPActivity.this.swipeViewsWrap.setVisibility(0);
                        VoIPActivity.this.endBtn.setVisibility(8);
                        VoIPActivity.this.acceptSwipe.startAnimatingArrows();
                        VoIPActivity.this.declineSwipe.startAnimatingArrows();
                        if (UserConfig.getActivatedAccountsCount() > 1) {
                            final TLRPC.User currentUser = UserConfig.getInstance(VoIPActivity.this.currentAccount).getCurrentUser();
                            VoIPActivity.this.accountNameText.setText((CharSequence)LocaleController.formatString("VoipAnsweringAsAccount", 2131561057, ContactsController.formatName(currentUser.first_name, currentUser.last_name)));
                        }
                        else {
                            VoIPActivity.this.accountNameText.setVisibility(8);
                        }
                        VoIPActivity.this.getWindow().addFlags(2097152);
                        final VoIPService sharedInstance = VoIPService.getSharedInstance();
                        if (sharedInstance != null) {
                            sharedInstance.startRingtoneAndVibration();
                        }
                        VoIPActivity.this.setTitle((CharSequence)LocaleController.getString("VoipIncoming", 2131561073));
                    }
                    else {
                        VoIPActivity.this.swipeViewsWrap.setVisibility(8);
                        VoIPActivity.this.acceptBtn.setVisibility(8);
                        VoIPActivity.this.declineBtn.setVisibility(8);
                        VoIPActivity.this.accountNameText.setVisibility(8);
                        VoIPActivity.this.getWindow().clearFlags(2097152);
                    }
                    if (callState != 3) {
                        VoIPActivity.this.emojiWrap.setVisibility(8);
                    }
                    VoIPActivity.this.firstStateChange = false;
                }
                if (VoIPActivity.this.isIncomingWaiting) {
                    final int val$state = callState;
                    if (val$state != 15 && val$state != 11 && val$state != 10) {
                        VoIPActivity.this.isIncomingWaiting = false;
                        if (!VoIPActivity.this.didAcceptFromHere) {
                            VoIPActivity.this.callAccepted();
                        }
                    }
                }
                final int val$state2 = callState;
                if (val$state2 == 15) {
                    VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipIncoming", 2131561073), false);
                    VoIPActivity.this.getWindow().addFlags(2097152);
                }
                else if (val$state2 != 1 && val$state2 != 2) {
                    if (val$state2 == 12) {
                        VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipExchangingKeys", 2131561067), true);
                    }
                    else if (val$state2 == 13) {
                        VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipWaiting", 2131561094), true);
                    }
                    else if (val$state2 == 16) {
                        VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipRinging", 2131561090), true);
                    }
                    else if (val$state2 == 14) {
                        VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipRequesting", 2131561089), true);
                    }
                    else if (val$state2 == 10) {
                        VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipHangingUp", 2131561070), true);
                        VoIPActivity.this.endBtnIcon.setAlpha(0.5f);
                        VoIPActivity.this.endBtn.setEnabled(false);
                    }
                    else if (val$state2 == 11) {
                        VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipCallEnded", 2131561062), false);
                        VoIPActivity.this.stateText.postDelayed((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                VoIPActivity.this.finish();
                            }
                        }, 200L);
                    }
                    else if (val$state2 == 17) {
                        VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipBusy", 2131561061), false);
                        VoIPActivity.this.showRetry();
                    }
                    else if (val$state2 != 3 && val$state2 != 5) {
                        if (val$state2 == 4) {
                            VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipFailed", 2131561068), false);
                            int lastError;
                            if (VoIPService.getSharedInstance() != null) {
                                lastError = VoIPService.getSharedInstance().getLastError();
                            }
                            else {
                                lastError = 0;
                            }
                            if (lastError == 1) {
                                final VoIPActivity this$2 = VoIPActivity.this;
                                this$2.showErrorDialog((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("VoipPeerIncompatible", 2131561084, ContactsController.formatName(this$2.user.first_name, VoIPActivity.this.user.last_name))));
                            }
                            else if (lastError == -1) {
                                final VoIPActivity this$3 = VoIPActivity.this;
                                this$3.showErrorDialog((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("VoipPeerOutdated", 2131561085, ContactsController.formatName(this$3.user.first_name, VoIPActivity.this.user.last_name))));
                            }
                            else if (lastError == -2) {
                                final VoIPActivity this$4 = VoIPActivity.this;
                                this$4.showErrorDialog((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("CallNotAvailable", 2131558880, ContactsController.formatName(this$4.user.first_name, VoIPActivity.this.user.last_name))));
                            }
                            else if (lastError == 3) {
                                VoIPActivity.this.showErrorDialog("Error initializing audio hardware");
                            }
                            else if (lastError == -3) {
                                VoIPActivity.this.finish();
                            }
                            else if (lastError == -5) {
                                VoIPActivity.this.showErrorDialog(LocaleController.getString("VoipErrorUnknown", 2131561066));
                            }
                            else {
                                VoIPActivity.this.stateText.postDelayed((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        VoIPActivity.this.finish();
                                    }
                                }, 1000L);
                            }
                        }
                    }
                    else {
                        VoIPActivity.this.setTitle((CharSequence)null);
                        if (!access$3600 && callState == 3) {
                            final int int1 = MessagesController.getGlobalMainSettings().getInt("call_emoji_tooltip_count", 0);
                            if (int1 < 3) {
                                VoIPActivity.this.setEmojiTooltipVisible(true);
                                final TextView access$3601 = VoIPActivity.this.hintTextView;
                                final VoIPActivity this$5 = VoIPActivity.this;
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        VoIPActivity.this.tooltipHider = null;
                                        VoIPActivity.this.setEmojiTooltipVisible(false);
                                    }
                                };
                                this$5.tooltipHider = runnable;
                                access$3601.postDelayed((Runnable)runnable, 5000L);
                                MessagesController.getGlobalMainSettings().edit().putInt("call_emoji_tooltip_count", int1 + 1).commit();
                            }
                        }
                        final int val$prevState = callState2;
                        if (val$prevState != 3 && val$prevState != 5) {
                            VoIPActivity.this.setStateTextAnimated("0:00", false);
                            VoIPActivity.this.startUpdatingCallDuration();
                            VoIPActivity.this.updateKeyView();
                            if (VoIPActivity.this.emojiWrap.getVisibility() != 0) {
                                VoIPActivity.this.emojiWrap.setVisibility(0);
                                VoIPActivity.this.emojiWrap.setAlpha(0.0f);
                                VoIPActivity.this.emojiWrap.animate().alpha(1.0f).setDuration(200L).setInterpolator((TimeInterpolator)new DecelerateInterpolator()).start();
                            }
                        }
                    }
                }
                else {
                    VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipConnecting", 2131561063), true);
                }
                VoIPActivity.this.brandingText.invalidate();
            }
        });
    }
    
    private class SignalBarsDrawable extends Drawable
    {
        private int[] barHeights;
        private int offsetStart;
        private Paint paint;
        private RectF rect;
        
        private SignalBarsDrawable() {
            this.barHeights = new int[] { AndroidUtilities.dp(3.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(9.0f), AndroidUtilities.dp(12.0f) };
            this.paint = new Paint(1);
            this.rect = new RectF();
            this.offsetStart = 6;
        }
        
        public void draw(final Canvas canvas) {
            if (VoIPActivity.this.callState != 3 && VoIPActivity.this.callState != 5) {
                return;
            }
            this.paint.setColor(-1);
            final int left = this.getBounds().left;
            float n;
            if (LocaleController.isRTL) {
                n = 0.0f;
            }
            else {
                n = (float)this.offsetStart;
            }
            final int n2 = left + AndroidUtilities.dp(n);
            final int top = this.getBounds().top;
            int n3;
            for (int i = 0; i < 4; i = n3) {
                final Paint paint = this.paint;
                n3 = i + 1;
                int alpha;
                if (n3 <= VoIPActivity.this.signalBarsCount) {
                    alpha = 242;
                }
                else {
                    alpha = 102;
                }
                paint.setAlpha(alpha);
                this.rect.set((float)(AndroidUtilities.dp((float)(i * 4)) + n2), (float)(this.getIntrinsicHeight() + top - this.barHeights[i]), (float)(AndroidUtilities.dp(4.0f) * i + n2 + AndroidUtilities.dp(3.0f)), (float)(this.getIntrinsicHeight() + top));
                canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(0.3f), (float)AndroidUtilities.dp(0.3f), this.paint);
            }
        }
        
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(12.0f);
        }
        
        public int getIntrinsicWidth() {
            return AndroidUtilities.dp((float)(this.offsetStart + 15));
        }
        
        public int getOpacity() {
            return -3;
        }
        
        public void setAlpha(final int n) {
        }
        
        public void setColorFilter(final ColorFilter colorFilter) {
        }
    }
    
    private class TextAlphaSpan extends CharacterStyle
    {
        private int alpha;
        
        public TextAlphaSpan() {
            this.alpha = 0;
        }
        
        public int getAlpha() {
            return this.alpha;
        }
        
        public void setAlpha(final int alpha) {
            this.alpha = alpha;
            VoIPActivity.this.stateText.invalidate();
            VoIPActivity.this.stateText2.invalidate();
        }
        
        public void updateDrawState(final TextPaint textPaint) {
            textPaint.setAlpha(this.alpha);
        }
    }
}
