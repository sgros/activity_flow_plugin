package org.telegram.p004ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.palette.graphics.Palette;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.ByteArrayOutputStream;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.Emoji.EmojiDrawable;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.ImageReceiver.BitmapHolder;
import org.telegram.messenger.ImageReceiver.ImageReceiverDelegate;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.voip.EncryptionKeyEmojifier;
import org.telegram.messenger.voip.VoIPBaseService;
import org.telegram.messenger.voip.VoIPBaseService.StateListener;
import org.telegram.messenger.voip.VoIPController;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BottomSheet;
import org.telegram.p004ui.ActionBar.BottomSheet.BottomSheetCell;
import org.telegram.p004ui.ActionBar.DarkAlertDialog.Builder;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.BackupImageView;
import org.telegram.p004ui.Components.CorrectlyMeasuringTextView;
import org.telegram.p004ui.Components.CubicBezierInterpolator;
import org.telegram.p004ui.Components.IdenticonDrawable;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.voip.CallSwipeView;
import org.telegram.p004ui.Components.voip.CallSwipeView.Listener;
import org.telegram.p004ui.Components.voip.CheckableImageView;
import org.telegram.p004ui.Components.voip.DarkTheme;
import org.telegram.p004ui.Components.voip.FabBackgroundDrawable;
import org.telegram.p004ui.Components.voip.VoIPHelper;
import org.telegram.tgnet.TLRPC.TL_encryptedChat;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.VoIPActivity */
public class VoIPActivity extends Activity implements StateListener, NotificationCenterDelegate {
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
    private TextAlphaSpan[] ellSpans;
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
    private SignalBarsDrawable signalBarsDrawable;
    private CheckableImageView spkToggle;
    private TextView stateText;
    private TextView stateText2;
    private LinearLayout swipeViewsWrap;
    private Animator textChangingAnim;
    private Animator tooltipAnim;
    private Runnable tooltipHider;
    private User user;

    /* renamed from: org.telegram.ui.VoIPActivity$10 */
    class C323410 implements Runnable {
        C323410() {
        }

        public void run() {
            VoIPActivity.this.nameText.sendAccessibilityEvent(8);
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$13 */
    class C323613 implements OnClickListener {
        C323613() {
        }

        public void onClick(View view) {
            VoIPActivity voIPActivity = VoIPActivity.this;
            if (voIPActivity.emojiTooltipVisible) {
                voIPActivity.setEmojiTooltipVisible(false);
                if (VoIPActivity.this.tooltipHider != null) {
                    VoIPActivity.this.hintTextView.removeCallbacks(VoIPActivity.this.tooltipHider);
                    VoIPActivity.this.tooltipHider = null;
                }
            }
            voIPActivity = VoIPActivity.this;
            voIPActivity.setEmojiExpanded(voIPActivity.emojiExpanded ^ 1);
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$14 */
    class C323814 implements OnLongClickListener {

        /* renamed from: org.telegram.ui.VoIPActivity$14$1 */
        class C32371 implements Runnable {
            C32371() {
            }

            public void run() {
                VoIPActivity.this.tooltipHider = null;
                VoIPActivity.this.setEmojiTooltipVisible(false);
            }
        }

        C323814() {
        }

        public boolean onLongClick(View view) {
            VoIPActivity voIPActivity = VoIPActivity.this;
            if (voIPActivity.emojiExpanded) {
                return false;
            }
            if (voIPActivity.tooltipHider != null) {
                VoIPActivity.this.hintTextView.removeCallbacks(VoIPActivity.this.tooltipHider);
                VoIPActivity.this.tooltipHider = null;
            }
            voIPActivity = VoIPActivity.this;
            voIPActivity.setEmojiTooltipVisible(voIPActivity.emojiTooltipVisible ^ 1);
            voIPActivity = VoIPActivity.this;
            if (voIPActivity.emojiTooltipVisible) {
                TextView access$2500 = voIPActivity.hintTextView;
                VoIPActivity voIPActivity2 = VoIPActivity.this;
                C32371 c32371 = new C32371();
                voIPActivity2.tooltipHider = c32371;
                access$2500.postDelayed(c32371, 5000);
            }
            return true;
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$15 */
    class C324015 extends AnimatorListenerAdapter {
        private Runnable restarter = new C32391();

        /* renamed from: org.telegram.ui.VoIPActivity$15$1 */
        class C32391 implements Runnable {
            C32391() {
            }

            public void run() {
                if (!VoIPActivity.this.isFinishing()) {
                    VoIPActivity.this.ellAnimator.start();
                }
            }
        }

        C324015() {
        }

        public void onAnimationEnd(Animator animator) {
            if (!VoIPActivity.this.isFinishing()) {
                VoIPActivity.this.content.postDelayed(this.restarter, 300);
            }
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$16 */
    class C324116 implements Runnable {
        C324116() {
        }

        public void run() {
            VoIPActivity.this.finish();
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$19 */
    class C324419 implements Runnable {
        C324419() {
        }

        public void run() {
            if (!VoIPActivity.this.isFinishing() && VoIPService.getSharedInstance() != null) {
                if (VoIPActivity.this.callState == 3 || VoIPActivity.this.callState == 5) {
                    Object[] objArr;
                    String str;
                    long callDuration = VoIPService.getSharedInstance().getCallDuration() / 1000;
                    TextView access$3100 = VoIPActivity.this.durationText;
                    if (callDuration > 3600) {
                        objArr = new Object[]{Long.valueOf(callDuration / 3600), Long.valueOf((callDuration % 3600) / 60), Long.valueOf(callDuration % 60)};
                        str = "%d:%02d:%02d";
                    } else {
                        objArr = new Object[]{Long.valueOf(callDuration / 60), Long.valueOf(callDuration % 60)};
                        str = "%d:%02d";
                    }
                    access$3100.setText(String.format(str, objArr));
                    VoIPActivity.this.durationText.postDelayed(this, 500);
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$20 */
    class C324520 extends AnimatorListenerAdapter {
        C324520() {
        }

        public void onAnimationEnd(Animator animator) {
            VoIPActivity.this.swipeViewsWrap.setVisibility(8);
            VoIPActivity.this.declineBtn.setVisibility(8);
            VoIPActivity.this.accountNameText.setVisibility(8);
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$21 */
    class C324621 extends AnimatorListenerAdapter {
        C324621() {
        }

        public void onAnimationEnd(Animator animator) {
            VoIPActivity.this.swipeViewsWrap.setVisibility(8);
            VoIPActivity.this.declineBtn.setVisibility(8);
            VoIPActivity.this.acceptBtn.setVisibility(8);
            VoIPActivity.this.accountNameText.setVisibility(8);
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$22 */
    class C324722 extends AnimatorListenerAdapter {
        C324722() {
        }

        public void onAnimationEnd(Animator animator) {
            VoIPActivity.this.retryAnim = null;
            VoIPActivity.this.endBtn.setEnabled(true);
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$23 */
    class C324823 extends AnimatorListenerAdapter {
        C324823() {
        }

        public void onAnimationEnd(Animator animator) {
            VoIPActivity.this.cancelBtn.setVisibility(8);
            VoIPActivity.this.endBtn.setEnabled(true);
            VoIPActivity.this.retryAnim = null;
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$26 */
    class C325426 implements OnDismissListener {
        C325426() {
        }

        public void onDismiss(DialogInterface dialogInterface) {
            VoIPActivity.this.finish();
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$27 */
    class C325527 extends AnimatorListenerAdapter {
        C325527() {
        }

        public void onAnimationEnd(Animator animator) {
            VoIPActivity.this.textChangingAnim = null;
            VoIPActivity.this.stateText2.setVisibility(8);
            VoIPActivity voIPActivity = VoIPActivity.this;
            voIPActivity.durationText = voIPActivity.stateText;
            VoIPActivity.this.stateText.setTranslationY(0.0f);
            VoIPActivity.this.stateText.setScaleX(1.0f);
            VoIPActivity.this.stateText.setScaleY(1.0f);
            VoIPActivity.this.stateText.setAlpha(1.0f);
            VoIPActivity.this.stateText.setText(VoIPActivity.this.stateText2.getText());
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$28 */
    class C325628 extends AnimatorListenerAdapter {
        C325628() {
        }

        public void onAnimationEnd(Animator animator) {
            VoIPActivity.this.tooltipAnim = null;
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$29 */
    class C325729 extends AnimatorListenerAdapter {
        C325729() {
        }

        public void onAnimationEnd(Animator animator) {
            VoIPActivity.this.emojiAnimator = null;
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$2 */
    class C32582 implements OnClickListener {
        private int tapCount = 0;

        C32582() {
        }

        public void onClick(View view) {
            if (!BuildVars.DEBUG_VERSION) {
                int i = this.tapCount;
                if (i != 9) {
                    this.tapCount = i + 1;
                    return;
                }
            }
            VoIPActivity.this.showDebugAlert();
            this.tapCount = 0;
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$30 */
    class C326030 extends AnimatorListenerAdapter {
        C326030() {
        }

        public void onAnimationEnd(Animator animator) {
            VoIPActivity.this.emojiAnimator = null;
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$33 */
    class C326433 implements OnDismissListener {
        C326433() {
        }

        public void onDismiss(DialogInterface dialogInterface) {
            VoIPActivity.this.getWindow().setNavigationBarColor(0);
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$3 */
    class C32743 implements OnClickListener {

        /* renamed from: org.telegram.ui.VoIPActivity$3$1 */
        class C32591 implements Runnable {
            C32591() {
            }

            public void run() {
                if (VoIPService.getSharedInstance() != null || VoIPActivity.this.isFinishing()) {
                    if (VoIPService.getSharedInstance() != null) {
                        VoIPService.getSharedInstance().registerStateListener(VoIPActivity.this);
                    }
                    return;
                }
                VoIPActivity.this.endBtn.postDelayed(this, 100);
            }
        }

        C32743() {
        }

        public void onClick(View view) {
            VoIPActivity.this.endBtn.setEnabled(false);
            if (VoIPActivity.this.retrying) {
                Intent intent = new Intent(VoIPActivity.this, VoIPService.class);
                intent.putExtra("user_id", VoIPActivity.this.user.f534id);
                intent.putExtra("is_outgoing", true);
                intent.putExtra("start_incall_activity", false);
                intent.putExtra("account", VoIPActivity.this.currentAccount);
                try {
                    VoIPActivity.this.startService(intent);
                } catch (Throwable th) {
                    FileLog.m30e(th);
                }
                VoIPActivity.this.hideRetry();
                VoIPActivity.this.endBtn.postDelayed(new C32591(), 100);
                return;
            }
            if (VoIPService.getSharedInstance() != null) {
                VoIPService.getSharedInstance().hangUp();
            }
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$4 */
    class C32754 implements OnClickListener {
        C32754() {
        }

        public void onClick(View view) {
            VoIPService sharedInstance = VoIPService.getSharedInstance();
            if (sharedInstance != null) {
                sharedInstance.toggleSpeakerphoneOrShowRouteSheet(VoIPActivity.this);
            }
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$5 */
    class C32765 implements OnClickListener {
        C32765() {
        }

        public void onClick(View view) {
            if (VoIPService.getSharedInstance() == null) {
                VoIPActivity.this.finish();
                return;
            }
            int isChecked = VoIPActivity.this.micToggle.isChecked() ^ 1;
            VoIPActivity.this.micToggle.setChecked(isChecked);
            VoIPService.getSharedInstance().setMicMute(isChecked);
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$6 */
    class C32776 implements OnClickListener {
        C32776() {
        }

        public void onClick(View view) {
            if (VoIPActivity.this.isIncomingWaiting) {
                VoIPActivity.this.showMessagesSheet();
                return;
            }
            Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("com.tmessages.openchat");
            stringBuilder.append(Math.random());
            stringBuilder.append(Integer.MAX_VALUE);
            intent.setAction(stringBuilder.toString());
            intent.putExtra("currentAccount", VoIPActivity.this.currentAccount);
            intent.setFlags(32768);
            intent.putExtra("userId", VoIPActivity.this.user.f534id);
            VoIPActivity.this.startActivity(intent);
            VoIPActivity.this.finish();
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$9 */
    class C32829 implements OnClickListener {
        C32829() {
        }

        public void onClick(View view) {
            VoIPActivity.this.finish();
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$SignalBarsDrawable */
    private class SignalBarsDrawable extends Drawable {
        private int[] barHeights;
        private int offsetStart;
        private Paint paint;
        private RectF rect;

        public int getOpacity() {
            return -3;
        }

        public void setAlpha(int i) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        private SignalBarsDrawable() {
            this.barHeights = new int[]{AndroidUtilities.m26dp(3.0f), AndroidUtilities.m26dp(6.0f), AndroidUtilities.m26dp(9.0f), AndroidUtilities.m26dp(12.0f)};
            this.paint = new Paint(1);
            this.rect = new RectF();
            this.offsetStart = 6;
        }

        /* synthetic */ SignalBarsDrawable(VoIPActivity voIPActivity, C43501 c43501) {
            this();
        }

        public void draw(Canvas canvas) {
            if (VoIPActivity.this.callState == 3 || VoIPActivity.this.callState == 5) {
                this.paint.setColor(-1);
                int dp = getBounds().left + AndroidUtilities.m26dp(LocaleController.isRTL ? 0.0f : (float) this.offsetStart);
                int i = getBounds().top;
                int i2 = 0;
                while (i2 < 4) {
                    int i3 = i2 + 1;
                    this.paint.setAlpha(i3 <= VoIPActivity.this.signalBarsCount ? 242 : 102);
                    this.rect.set((float) (AndroidUtilities.m26dp((float) (i2 * 4)) + dp), (float) ((getIntrinsicHeight() + i) - this.barHeights[i2]), (float) (((AndroidUtilities.m26dp(4.0f) * i2) + dp) + AndroidUtilities.m26dp(3.0f)), (float) (getIntrinsicHeight() + i));
                    canvas.drawRoundRect(this.rect, (float) AndroidUtilities.m26dp(0.3f), (float) AndroidUtilities.m26dp(0.3f), this.paint);
                    i2 = i3;
                }
            }
        }

        public int getIntrinsicWidth() {
            return AndroidUtilities.m26dp((float) (this.offsetStart + 15));
        }

        public int getIntrinsicHeight() {
            return AndroidUtilities.m26dp(12.0f);
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$TextAlphaSpan */
    private class TextAlphaSpan extends CharacterStyle {
        private int alpha = 0;

        public int getAlpha() {
            return this.alpha;
        }

        public void setAlpha(int i) {
            this.alpha = i;
            VoIPActivity.this.stateText.invalidate();
            VoIPActivity.this.stateText2.invalidate();
        }

        public void updateDrawState(TextPaint textPaint) {
            textPaint.setAlpha(this.alpha);
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$1 */
    class C43501 implements ImageReceiverDelegate {
        C43501() {
        }

        public void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2) {
            BitmapHolder bitmapSafe = imageReceiver.getBitmapSafe();
            if (bitmapSafe != null) {
                VoIPActivity.this.updateBlurredPhotos(bitmapSafe);
            }
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$7 */
    class C43517 implements Listener {

        /* renamed from: org.telegram.ui.VoIPActivity$7$1 */
        class C32781 extends AnimatorListenerAdapter {
            C32781() {
            }

            public void onAnimationEnd(Animator animator) {
                VoIPActivity.this.currentDeclineAnim = null;
            }
        }

        /* renamed from: org.telegram.ui.VoIPActivity$7$2 */
        class C32792 extends AnimatorListenerAdapter {
            C32792() {
            }

            public void onAnimationEnd(Animator animator) {
                VoIPActivity.this.currentDeclineAnim = null;
            }
        }

        C43517() {
        }

        public void onDragComplete() {
            VoIPActivity.this.acceptSwipe.setEnabled(false);
            VoIPActivity.this.declineSwipe.setEnabled(false);
            if (VoIPService.getSharedInstance() == null) {
                VoIPActivity.this.finish();
                return;
            }
            VoIPActivity.this.didAcceptFromHere = true;
            if (VERSION.SDK_INT >= 23) {
                if (VoIPActivity.this.checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
                    VoIPActivity.this.requestPermissions(new String[]{r3}, 101);
                }
            }
            VoIPService.getSharedInstance().acceptIncomingCall();
            VoIPActivity.this.callAccepted();
        }

        public void onDragStart() {
            if (VoIPActivity.this.currentDeclineAnim != null) {
                VoIPActivity.this.currentDeclineAnim.cancel();
            }
            AnimatorSet animatorSet = new AnimatorSet();
            Animator[] animatorArr = new Animator[2];
            String str = "alpha";
            animatorArr[0] = ObjectAnimator.ofFloat(VoIPActivity.this.declineSwipe, str, new float[]{0.2f});
            animatorArr[1] = ObjectAnimator.ofFloat(VoIPActivity.this.declineBtn, str, new float[]{0.2f});
            animatorSet.playTogether(animatorArr);
            animatorSet.setDuration(200);
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            animatorSet.addListener(new C32781());
            VoIPActivity.this.currentDeclineAnim = animatorSet;
            animatorSet.start();
            VoIPActivity.this.declineSwipe.stopAnimatingArrows();
        }

        public void onDragCancel() {
            if (VoIPActivity.this.currentDeclineAnim != null) {
                VoIPActivity.this.currentDeclineAnim.cancel();
            }
            AnimatorSet animatorSet = new AnimatorSet();
            Animator[] animatorArr = new Animator[2];
            String str = "alpha";
            animatorArr[0] = ObjectAnimator.ofFloat(VoIPActivity.this.declineSwipe, str, new float[]{1.0f});
            animatorArr[1] = ObjectAnimator.ofFloat(VoIPActivity.this.declineBtn, str, new float[]{1.0f});
            animatorSet.playTogether(animatorArr);
            animatorSet.setDuration(200);
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            animatorSet.addListener(new C32792());
            VoIPActivity.this.currentDeclineAnim = animatorSet;
            animatorSet.start();
            VoIPActivity.this.declineSwipe.startAnimatingArrows();
        }
    }

    /* renamed from: org.telegram.ui.VoIPActivity$8 */
    class C43528 implements Listener {

        /* renamed from: org.telegram.ui.VoIPActivity$8$1 */
        class C32801 extends AnimatorListenerAdapter {
            C32801() {
            }

            public void onAnimationEnd(Animator animator) {
                VoIPActivity.this.currentAcceptAnim = null;
            }
        }

        /* renamed from: org.telegram.ui.VoIPActivity$8$2 */
        class C32812 extends AnimatorListenerAdapter {
            C32812() {
            }

            public void onAnimationEnd(Animator animator) {
                VoIPActivity.this.currentAcceptAnim = null;
            }
        }

        C43528() {
        }

        public void onDragComplete() {
            VoIPActivity.this.acceptSwipe.setEnabled(false);
            VoIPActivity.this.declineSwipe.setEnabled(false);
            if (VoIPService.getSharedInstance() != null) {
                VoIPService.getSharedInstance().declineIncomingCall(4, null);
            } else {
                VoIPActivity.this.finish();
            }
        }

        public void onDragStart() {
            if (VoIPActivity.this.currentAcceptAnim != null) {
                VoIPActivity.this.currentAcceptAnim.cancel();
            }
            AnimatorSet animatorSet = new AnimatorSet();
            Animator[] animatorArr = new Animator[2];
            String str = "alpha";
            animatorArr[0] = ObjectAnimator.ofFloat(VoIPActivity.this.acceptSwipe, str, new float[]{0.2f});
            animatorArr[1] = ObjectAnimator.ofFloat(VoIPActivity.this.acceptBtn, str, new float[]{0.2f});
            animatorSet.playTogether(animatorArr);
            animatorSet.setDuration(200);
            animatorSet.setInterpolator(new DecelerateInterpolator());
            animatorSet.addListener(new C32801());
            VoIPActivity.this.currentAcceptAnim = animatorSet;
            animatorSet.start();
            VoIPActivity.this.acceptSwipe.stopAnimatingArrows();
        }

        public void onDragCancel() {
            if (VoIPActivity.this.currentAcceptAnim != null) {
                VoIPActivity.this.currentAcceptAnim.cancel();
            }
            AnimatorSet animatorSet = new AnimatorSet();
            Animator[] animatorArr = new Animator[2];
            String str = "alpha";
            animatorArr[0] = ObjectAnimator.ofFloat(VoIPActivity.this.acceptSwipe, str, new float[]{1.0f});
            animatorArr[1] = ObjectAnimator.ofFloat(VoIPActivity.this.acceptBtn, str, new float[]{1.0f});
            animatorSet.playTogether(animatorArr);
            animatorSet.setDuration(200);
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            animatorSet.addListener(new C32812());
            VoIPActivity.this.currentAcceptAnim = animatorSet;
            animatorSet.start();
            VoIPActivity.this.acceptSwipe.startAnimatingArrows();
        }
    }

    private void showInviteFragment() {
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        getWindow().addFlags(524288);
        super.onCreate(bundle);
        if (VoIPService.getSharedInstance() == null) {
            finish();
            return;
        }
        this.currentAccount = VoIPService.getSharedInstance().getAccount();
        if (this.currentAccount == -1) {
            finish();
            return;
        }
        if ((getResources().getConfiguration().screenLayout & 15) < 3) {
            setRequestedOrientation(1);
        }
        View createContentView = createContentView();
        setContentView(createContentView);
        int i = VERSION.SDK_INT;
        if (i >= 21) {
            getWindow().addFlags(Integer.MIN_VALUE);
            getWindow().setStatusBarColor(0);
            getWindow().setNavigationBarColor(0);
            getWindow().getDecorView().setSystemUiVisibility(1792);
        } else if (i >= 19) {
            getWindow().addFlags(201326592);
            getWindow().getDecorView().setSystemUiVisibility(1792);
        }
        this.user = VoIPService.getSharedInstance().getUser();
        if (this.user.photo != null) {
            this.photoView.getImageReceiver().setDelegate(new C43501());
            this.photoView.setImage(ImageLocation.getForUser(this.user, true), null, new ColorDrawable(Theme.ACTION_BAR_VIDEO_EDIT_COLOR), this.user);
            this.photoView.setLayerType(2, null);
        } else {
            this.photoView.setVisibility(8);
            createContentView.setBackgroundDrawable(new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-14994098, -14328963}));
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setVolumeControlStream(0);
        this.nameText.setOnClickListener(new C32582());
        this.endBtn.setOnClickListener(new C32743());
        this.spkToggle.setOnClickListener(new C32754());
        this.micToggle.setOnClickListener(new C32765());
        this.chatBtn.setOnClickListener(new C32776());
        this.spkToggle.setChecked(((AudioManager) getSystemService(MimeTypes.BASE_TYPE_AUDIO)).isSpeakerphoneOn());
        this.micToggle.setChecked(VoIPService.getSharedInstance().isMicMute());
        onAudioSettingsChanged();
        TextView textView = this.nameText;
        User user = this.user;
        textView.setText(ContactsController.formatName(user.first_name, user.last_name));
        VoIPService.getSharedInstance().registerStateListener(this);
        this.acceptSwipe.setListener(new C43517());
        this.declineSwipe.setListener(new C43528());
        this.cancelBtn.setOnClickListener(new C32829());
        getWindow().getDecorView().setKeepScreenOn(true);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.closeInCallActivity);
        String str = "CallEmojiKeyTooltip";
        this.hintTextView.setText(LocaleController.formatString(str, C1067R.string.CallEmojiKeyTooltip, this.user.first_name));
        this.emojiExpandedText.setText(LocaleController.formatString(str, C1067R.string.CallEmojiKeyTooltip, this.user.first_name));
        if (((AccessibilityManager) getSystemService("accessibility")).isTouchExplorationEnabled()) {
            this.nameText.postDelayed(new C323410(), 500);
        }
    }

    private View createContentView() {
        C323511 c323511 = new FrameLayout(this) {
            private void setNegativeMargins(Rect rect, LayoutParams layoutParams) {
                layoutParams.topMargin = -rect.top;
                layoutParams.bottomMargin = -rect.bottom;
                layoutParams.leftMargin = -rect.left;
                layoutParams.rightMargin = -rect.right;
            }

            /* Access modifiers changed, original: protected */
            public boolean fitSystemWindows(Rect rect) {
                setNegativeMargins(rect, (LayoutParams) VoIPActivity.this.photoView.getLayoutParams());
                setNegativeMargins(rect, (LayoutParams) VoIPActivity.this.blurOverlayView1.getLayoutParams());
                setNegativeMargins(rect, (LayoutParams) VoIPActivity.this.blurOverlayView2.getLayoutParams());
                return super.fitSystemWindows(rect);
            }
        };
        c323511.setBackgroundColor(0);
        c323511.setFitsSystemWindows(true);
        c323511.setClipToPadding(false);
        C434912 c434912 = new BackupImageView(this) {
            private Drawable bottomGradient = getResources().getDrawable(C1067R.C1065drawable.gradient_bottom);
            private Paint paint = new Paint();
            private Drawable topGradient = getResources().getDrawable(C1067R.C1065drawable.gradient_top);

            /* Access modifiers changed, original: protected */
            public void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                this.paint.setColor(1275068416);
                canvas.drawRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), this.paint);
                this.topGradient.setBounds(0, 0, getWidth(), AndroidUtilities.m26dp(170.0f));
                this.topGradient.setAlpha(128);
                this.topGradient.draw(canvas);
                this.bottomGradient.setBounds(0, getHeight() - AndroidUtilities.m26dp(220.0f), getWidth(), getHeight());
                this.bottomGradient.setAlpha(178);
                this.bottomGradient.draw(canvas);
            }
        };
        this.photoView = c434912;
        c323511.addView(c434912);
        this.blurOverlayView1 = new ImageView(this);
        this.blurOverlayView1.setScaleType(ScaleType.CENTER_CROP);
        this.blurOverlayView1.setAlpha(0.0f);
        c323511.addView(this.blurOverlayView1);
        this.blurOverlayView2 = new ImageView(this);
        this.blurOverlayView2.setScaleType(ScaleType.CENTER_CROP);
        this.blurOverlayView2.setAlpha(0.0f);
        c323511.addView(this.blurOverlayView2);
        TextView textView = new TextView(this);
        textView.setTextColor(-855638017);
        textView.setText(LocaleController.getString("VoipInCallBranding", C1067R.string.VoipInCallBranding));
        Drawable mutate = getResources().getDrawable(C1067R.C1065drawable.notification).mutate();
        mutate.setAlpha(204);
        mutate.setBounds(0, 0, AndroidUtilities.m26dp(15.0f), AndroidUtilities.m26dp(15.0f));
        this.signalBarsDrawable = new SignalBarsDrawable(this, null);
        SignalBarsDrawable signalBarsDrawable = this.signalBarsDrawable;
        signalBarsDrawable.setBounds(0, 0, signalBarsDrawable.getIntrinsicWidth(), this.signalBarsDrawable.getIntrinsicHeight());
        Drawable drawable = LocaleController.isRTL ? this.signalBarsDrawable : mutate;
        if (!LocaleController.isRTL) {
            mutate = this.signalBarsDrawable;
        }
        textView.setCompoundDrawables(drawable, null, mutate, null);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setGravity(LocaleController.isRTL ? 5 : 3);
        textView.setCompoundDrawablePadding(AndroidUtilities.m26dp(5.0f));
        textView.setTextSize(1, 14.0f);
        c323511.addView(textView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 18.0f, 18.0f, 18.0f, 0.0f));
        this.brandingText = textView;
        textView = new TextView(this);
        textView.setSingleLine();
        textView.setTextColor(-1);
        textView.setTextSize(1, 40.0f);
        textView.setEllipsize(TruncateAt.END);
        textView.setGravity(LocaleController.isRTL ? 5 : 3);
        textView.setShadowLayer((float) AndroidUtilities.m26dp(3.0f), 0.0f, (float) AndroidUtilities.m26dp(0.6666667f), 1275068416);
        textView.setTypeface(Typeface.create("sans-serif-light", 0));
        this.nameText = textView;
        c323511.addView(textView, LayoutHelper.createFrame(-1, -2.0f, 51, 16.0f, 43.0f, 18.0f, 0.0f));
        textView = new TextView(this);
        textView.setTextColor(-855638017);
        textView.setSingleLine();
        textView.setEllipsize(TruncateAt.END);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setShadowLayer((float) AndroidUtilities.m26dp(3.0f), 0.0f, (float) AndroidUtilities.m26dp(0.6666667f), 1275068416);
        textView.setTextSize(1, 15.0f);
        textView.setGravity(LocaleController.isRTL ? 5 : 3);
        this.stateText = textView;
        c323511.addView(textView, LayoutHelper.createFrame(-1, -2.0f, 51, 18.0f, 98.0f, 18.0f, 0.0f));
        this.durationText = textView;
        textView = new TextView(this);
        textView.setTextColor(-855638017);
        textView.setSingleLine();
        textView.setEllipsize(TruncateAt.END);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setShadowLayer((float) AndroidUtilities.m26dp(3.0f), 0.0f, (float) AndroidUtilities.m26dp(0.6666667f), 1275068416);
        textView.setTextSize(1, 15.0f);
        textView.setGravity(LocaleController.isRTL ? 5 : 3);
        textView.setVisibility(8);
        this.stateText2 = textView;
        c323511.addView(textView, LayoutHelper.createFrame(-1, -2.0f, 51, 18.0f, 98.0f, 18.0f, 0.0f));
        this.ellSpans = new TextAlphaSpan[]{new TextAlphaSpan(), new TextAlphaSpan(), new TextAlphaSpan()};
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(0);
        c323511.addView(linearLayout, LayoutHelper.createFrame(-1, -2, 80));
        TextView textView2 = new TextView(this);
        textView2.setTextColor(-855638017);
        textView2.setSingleLine();
        textView2.setEllipsize(TruncateAt.END);
        textView2.setShadowLayer((float) AndroidUtilities.m26dp(3.0f), 0.0f, (float) AndroidUtilities.m26dp(0.6666667f), 1275068416);
        textView2.setTextSize(1, 15.0f);
        textView2.setGravity(LocaleController.isRTL ? 5 : 3);
        this.accountNameText = textView2;
        c323511.addView(textView2, LayoutHelper.createFrame(-1, -2.0f, 51, 18.0f, 120.0f, 18.0f, 0.0f));
        CheckableImageView checkableImageView = new CheckableImageView(this);
        checkableImageView.setBackgroundResource(C1067R.C1065drawable.bg_voip_icon_btn);
        mutate = getResources().getDrawable(C1067R.C1065drawable.ic_mic_off_white_24dp).mutate();
        checkableImageView.setAlpha(204);
        checkableImageView.setImageDrawable(mutate);
        checkableImageView.setScaleType(ScaleType.CENTER);
        checkableImageView.setContentDescription(LocaleController.getString("AccDescrMuteMic", C1067R.string.AccDescrMuteMic));
        FrameLayout frameLayout = new FrameLayout(this);
        this.micToggle = checkableImageView;
        frameLayout.addView(checkableImageView, LayoutHelper.createFrame(38, 38.0f, 81, 0.0f, 0.0f, 0.0f, 10.0f));
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(0, -2, 1.0f));
        ImageView imageView = new ImageView(this);
        mutate = getResources().getDrawable(C1067R.C1065drawable.ic_chat_bubble_white_24dp).mutate();
        mutate.setAlpha(204);
        imageView.setImageDrawable(mutate);
        imageView.setScaleType(ScaleType.CENTER);
        imageView.setContentDescription(LocaleController.getString("AccDescrOpenChat", C1067R.string.AccDescrOpenChat));
        frameLayout = new FrameLayout(this);
        this.chatBtn = imageView;
        frameLayout.addView(imageView, LayoutHelper.createFrame(38, 38.0f, 81, 0.0f, 0.0f, 0.0f, 10.0f));
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(0, -2, 1.0f));
        checkableImageView = new CheckableImageView(this);
        checkableImageView.setBackgroundResource(C1067R.C1065drawable.bg_voip_icon_btn);
        mutate = getResources().getDrawable(C1067R.C1065drawable.ic_volume_up_white_24dp).mutate();
        checkableImageView.setAlpha(204);
        checkableImageView.setImageDrawable(mutate);
        checkableImageView.setScaleType(ScaleType.CENTER);
        checkableImageView.setContentDescription(LocaleController.getString("VoipAudioRoutingSpeaker", C1067R.string.VoipAudioRoutingSpeaker));
        frameLayout = new FrameLayout(this);
        this.spkToggle = checkableImageView;
        frameLayout.addView(checkableImageView, LayoutHelper.createFrame(38, 38.0f, 81, 0.0f, 0.0f, 0.0f, 10.0f));
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(0, -2, 1.0f));
        this.bottomButtons = linearLayout;
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(0);
        CallSwipeView callSwipeView = new CallSwipeView(this);
        callSwipeView.setColor(-12207027);
        callSwipeView.setContentDescription(LocaleController.getString("Accept", C1067R.string.Accept));
        this.acceptSwipe = callSwipeView;
        linearLayout.addView(callSwipeView, LayoutHelper.createLinear(-1, 70, 1.0f, 4, 4, -35, 4));
        CallSwipeView callSwipeView2 = new CallSwipeView(this);
        callSwipeView2.setColor(-1696188);
        callSwipeView2.setContentDescription(LocaleController.getString("Decline", C1067R.string.Decline));
        this.declineSwipe = callSwipeView2;
        linearLayout.addView(callSwipeView2, LayoutHelper.createLinear(-1, 70, 1.0f, -35, 4, 4, 4));
        this.swipeViewsWrap = linearLayout;
        c323511.addView(linearLayout, LayoutHelper.createFrame(-1, -2.0f, 80, 20.0f, 0.0f, 20.0f, 68.0f));
        ImageView imageView2 = new ImageView(this);
        FabBackgroundDrawable fabBackgroundDrawable = new FabBackgroundDrawable();
        fabBackgroundDrawable.setColor(-12207027);
        imageView2.setBackgroundDrawable(fabBackgroundDrawable);
        imageView2.setImageResource(C1067R.C1065drawable.ic_call_end_white_36dp);
        imageView2.setScaleType(ScaleType.MATRIX);
        Matrix matrix = new Matrix();
        matrix.setTranslate((float) AndroidUtilities.m26dp(17.0f), (float) AndroidUtilities.m26dp(17.0f));
        matrix.postRotate(-135.0f, (float) AndroidUtilities.m26dp(35.0f), (float) AndroidUtilities.m26dp(35.0f));
        imageView2.setImageMatrix(matrix);
        this.acceptBtn = imageView2;
        c323511.addView(imageView2, LayoutHelper.createFrame(78, 78.0f, 83, 20.0f, 0.0f, 0.0f, 68.0f));
        ImageView imageView3 = new ImageView(this);
        FabBackgroundDrawable fabBackgroundDrawable2 = new FabBackgroundDrawable();
        fabBackgroundDrawable2.setColor(-1696188);
        imageView3.setBackgroundDrawable(fabBackgroundDrawable2);
        imageView3.setImageResource(C1067R.C1065drawable.ic_call_end_white_36dp);
        imageView3.setScaleType(ScaleType.CENTER);
        this.declineBtn = imageView3;
        c323511.addView(imageView3, LayoutHelper.createFrame(78, 78.0f, 85, 0.0f, 0.0f, 20.0f, 68.0f));
        callSwipeView.setViewToDrag(imageView2, false);
        callSwipeView2.setViewToDrag(imageView3, true);
        FrameLayout frameLayout2 = new FrameLayout(this);
        FabBackgroundDrawable fabBackgroundDrawable3 = new FabBackgroundDrawable();
        fabBackgroundDrawable3.setColor(-1696188);
        this.endBtnBg = fabBackgroundDrawable3;
        frameLayout2.setBackgroundDrawable(fabBackgroundDrawable3);
        imageView = new ImageView(this);
        imageView.setImageResource(C1067R.C1065drawable.ic_call_end_white_36dp);
        imageView.setScaleType(ScaleType.CENTER);
        this.endBtnIcon = imageView;
        frameLayout2.addView(imageView, LayoutHelper.createFrame(70, 70.0f));
        frameLayout2.setForeground(getResources().getDrawable(C1067R.C1065drawable.fab_highlight_dark));
        frameLayout2.setContentDescription(LocaleController.getString("VoipEndCall", C1067R.string.VoipEndCall));
        this.endBtn = frameLayout2;
        c323511.addView(frameLayout2, LayoutHelper.createFrame(78, 78.0f, 81, 0.0f, 0.0f, 0.0f, 68.0f));
        imageView2 = new ImageView(this);
        fabBackgroundDrawable3 = new FabBackgroundDrawable();
        fabBackgroundDrawable3.setColor(-1);
        imageView2.setBackgroundDrawable(fabBackgroundDrawable3);
        imageView2.setImageResource(C1067R.C1065drawable.edit_cancel);
        imageView2.setColorFilter(-1996488704);
        imageView2.setScaleType(ScaleType.CENTER);
        imageView2.setVisibility(8);
        imageView2.setContentDescription(LocaleController.getString("Cancel", C1067R.string.Cancel));
        this.cancelBtn = imageView2;
        c323511.addView(imageView2, LayoutHelper.createFrame(78, 78.0f, 83, 52.0f, 0.0f, 0.0f, 68.0f));
        this.emojiWrap = new LinearLayout(this);
        this.emojiWrap.setOrientation(0);
        this.emojiWrap.setClipToPadding(false);
        this.emojiWrap.setPivotX(0.0f);
        this.emojiWrap.setPivotY(0.0f);
        this.emojiWrap.setPadding(AndroidUtilities.m26dp(14.0f), AndroidUtilities.m26dp(10.0f), AndroidUtilities.m26dp(14.0f), AndroidUtilities.m26dp(10.0f));
        int i = 0;
        while (i < 4) {
            imageView = new ImageView(this);
            imageView.setScaleType(ScaleType.FIT_XY);
            this.emojiWrap.addView(imageView, LayoutHelper.createLinear(22, 22, i == 0 ? 0.0f : 4.0f, 0.0f, 0.0f, 0.0f));
            this.keyEmojiViews[i] = imageView;
            i++;
        }
        this.emojiWrap.setOnClickListener(new C323613());
        c323511.addView(this.emojiWrap, LayoutHelper.createFrame(-2, -2, (LocaleController.isRTL ? 3 : 5) | 48));
        this.emojiWrap.setOnLongClickListener(new C323814());
        this.emojiExpandedText = new TextView(this);
        this.emojiExpandedText.setTextSize(1, 16.0f);
        this.emojiExpandedText.setTextColor(-1);
        this.emojiExpandedText.setGravity(17);
        this.emojiExpandedText.setAlpha(0.0f);
        c323511.addView(this.emojiExpandedText, LayoutHelper.createFrame(-1, -2.0f, 17, 10.0f, 32.0f, 10.0f, 0.0f));
        this.hintTextView = new CorrectlyMeasuringTextView(this);
        this.hintTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.m26dp(3.0f), -231525581));
        this.hintTextView.setTextColor(Theme.getColor(Theme.key_chat_gifSaveHintText));
        this.hintTextView.setTextSize(1, 14.0f);
        this.hintTextView.setPadding(AndroidUtilities.m26dp(10.0f), AndroidUtilities.m26dp(10.0f), AndroidUtilities.m26dp(10.0f), AndroidUtilities.m26dp(10.0f));
        this.hintTextView.setGravity(17);
        this.hintTextView.setMaxWidth(AndroidUtilities.m26dp(300.0f));
        this.hintTextView.setAlpha(0.0f);
        c323511.addView(this.hintTextView, LayoutHelper.createFrame(-2, -2.0f, 53, 0.0f, 42.0f, 10.0f, 0.0f));
        int alpha = this.stateText.getPaint().getAlpha();
        this.ellAnimator = new AnimatorSet();
        AnimatorSet animatorSet = this.ellAnimator;
        Animator[] animatorArr = new Animator[6];
        int i2 = alpha;
        animatorArr[0] = createAlphaAnimator(this.ellSpans[0], 0, i2, 0, 300);
        animatorArr[1] = createAlphaAnimator(this.ellSpans[1], 0, i2, 150, 300);
        animatorArr[2] = createAlphaAnimator(this.ellSpans[2], 0, i2, 300, 300);
        int i3 = alpha;
        animatorArr[3] = createAlphaAnimator(this.ellSpans[0], i3, 0, 1000, 400);
        animatorArr[4] = createAlphaAnimator(this.ellSpans[1], i3, 0, 1000, 400);
        animatorArr[5] = createAlphaAnimator(this.ellSpans[2], i3, 0, 1000, 400);
        animatorSet.playTogether(animatorArr);
        this.ellAnimator.addListener(new C324015());
        c323511.setClipChildren(false);
        this.content = c323511;
        return c323511;
    }

    @SuppressLint({"ObjectAnimatorBinding"})
    private ObjectAnimator createAlphaAnimator(Object obj, int i, int i2, int i3, int i4) {
        ObjectAnimator ofInt = ObjectAnimator.ofInt(obj, "alpha", new int[]{i, i2});
        ofInt.setDuration((long) i4);
        ofInt.setStartDelay((long) i3);
        ofInt.setInterpolator(CubicBezierInterpolator.DEFAULT);
        return ofInt;
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeInCallActivity);
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().unregisterStateListener(this);
        }
        super.onDestroy();
    }

    public void onBackPressed() {
        if (this.emojiExpanded) {
            setEmojiExpanded(false);
            return;
        }
        if (!this.isIncomingWaiting) {
            super.onBackPressed();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().onUIForegroundStateChanged(true);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        if (this.retrying) {
            finish();
        }
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().onUIForegroundStateChanged(false);
        }
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 101) {
            if (VoIPService.getSharedInstance() == null) {
                finish();
            } else if (iArr.length > 0 && iArr[0] == 0) {
                VoIPService.getSharedInstance().acceptIncomingCall();
                callAccepted();
            } else if (shouldShowRequestPermissionRationale("android.permission.RECORD_AUDIO")) {
                this.acceptSwipe.reset();
            } else {
                VoIPService.getSharedInstance().declineIncomingCall();
                VoIPHelper.permissionDenied(this, new C324116());
            }
        }
    }

    private void updateKeyView() {
        if (VoIPService.getSharedInstance() != null) {
            new IdenticonDrawable().setColors(new int[]{16777215, -1, -1711276033, 872415231});
            TL_encryptedChat tL_encryptedChat = new TL_encryptedChat();
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byteArrayOutputStream.write(VoIPService.getSharedInstance().getEncryptionKey());
                byteArrayOutputStream.write(VoIPService.getSharedInstance().getGA());
                tL_encryptedChat.auth_key = byteArrayOutputStream.toByteArray();
            } catch (Exception unused) {
            }
            byte[] bArr = tL_encryptedChat.auth_key;
            String[] emojifyForCall = EncryptionKeyEmojifier.emojifyForCall(Utilities.computeSHA256(bArr, 0, bArr.length));
            LinearLayout linearLayout = this.emojiWrap;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LocaleController.getString("EncryptionKey", C1067R.string.EncryptionKey));
            String str = ", ";
            stringBuilder.append(str);
            stringBuilder.append(TextUtils.join(str, emojifyForCall));
            linearLayout.setContentDescription(stringBuilder.toString());
            for (int i = 0; i < 4; i++) {
                EmojiDrawable emojiDrawable = Emoji.getEmojiDrawable(emojifyForCall[i]);
                if (emojiDrawable != null) {
                    emojiDrawable.setBounds(0, 0, AndroidUtilities.m26dp(22.0f), AndroidUtilities.m26dp(22.0f));
                    this.keyEmojiViews[i].setImageDrawable(emojiDrawable);
                }
            }
        }
    }

    private CharSequence getFormattedDebugString() {
        String debugString = VoIPService.getSharedInstance().getDebugString();
        SpannableString spannableString = new SpannableString(debugString);
        int i = 0;
        do {
            int i2 = i + 1;
            int indexOf = debugString.indexOf(10, i2);
            if (indexOf == -1) {
                indexOf = debugString.length();
            }
            String substring = debugString.substring(i, indexOf);
            if (substring.contains("IN_USE")) {
                spannableString.setSpan(new ForegroundColorSpan(-16711936), i, indexOf, 0);
            } else if (substring.contains(": ")) {
                spannableString.setSpan(new ForegroundColorSpan(-1426063361), i, (substring.indexOf(58) + i) + 1, 0);
            }
            i = debugString.indexOf(10, i2);
        } while (i != -1);
        return spannableString;
    }

    private void showDebugAlert() {
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().forceRating();
            final LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(1);
            linearLayout.setBackgroundColor(-872415232);
            int dp = AndroidUtilities.m26dp(16.0f);
            int i = dp * 2;
            linearLayout.setPadding(dp, i, dp, i);
            TextView textView = new TextView(this);
            textView.setTextColor(-1);
            textView.setTextSize(1, 15.0f);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setGravity(17);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("libtgvoip v");
            stringBuilder.append(VoIPController.getVersion());
            textView.setText(stringBuilder.toString());
            linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 16.0f));
            ScrollView scrollView = new ScrollView(this);
            final TextView textView2 = new TextView(this);
            textView2.setTypeface(Typeface.MONOSPACE);
            textView2.setTextSize(1, 11.0f);
            textView2.setMaxWidth(AndroidUtilities.m26dp(350.0f));
            textView2.setTextColor(-1);
            textView2.setText(getFormattedDebugString());
            scrollView.addView(textView2);
            linearLayout.addView(scrollView, LayoutHelper.createLinear(-1, -1, 1.0f));
            textView = new TextView(this);
            textView.setBackgroundColor(-1);
            textView.setTextColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
            textView.setPadding(dp, dp, dp, dp);
            textView.setTextSize(1, 15.0f);
            textView.setText(LocaleController.getString("Close", C1067R.string.Close));
            linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, 1, 0, 16, 0, 0));
            final WindowManager windowManager = (WindowManager) getSystemService("window");
            windowManager.addView(linearLayout, new WindowManager.LayoutParams(-1, -1, 1000, 0, -3));
            textView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    windowManager.removeView(linearLayout);
                }
            });
            linearLayout.postDelayed(new Runnable() {
                public void run() {
                    if (!VoIPActivity.this.isFinishing() && VoIPService.getSharedInstance() != null) {
                        textView2.setText(VoIPActivity.this.getFormattedDebugString());
                        linearLayout.postDelayed(this, 500);
                    }
                }
            }, 500);
        }
    }

    private void startUpdatingCallDuration() {
        new C324419().run();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (!this.isIncomingWaiting || (i != 25 && i != 24)) {
            return super.onKeyDown(i, keyEvent);
        }
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().stopRinging();
        } else {
            finish();
        }
        return true;
    }

    private void callAccepted() {
        this.endBtn.setVisibility(0);
        if (VoIPService.getSharedInstance().hasEarpiece()) {
            this.spkToggle.setVisibility(0);
        } else {
            this.spkToggle.setVisibility(8);
        }
        this.bottomButtons.setVisibility(0);
        String str = "alpha";
        AnimatorSet animatorSet;
        AnimatorSet animatorSet2;
        if (this.didAcceptFromHere) {
            ObjectAnimator ofArgb;
            this.acceptBtn.setVisibility(8);
            String str2 = "color";
            if (VERSION.SDK_INT >= 21) {
                ofArgb = ObjectAnimator.ofArgb(this.endBtnBg, str2, new int[]{-12207027, -1696188});
            } else {
                ofArgb = ObjectAnimator.ofInt(this.endBtnBg, str2, new int[]{-12207027, -1696188});
                ofArgb.setEvaluator(new ArgbEvaluator());
            }
            animatorSet = new AnimatorSet();
            AnimatorSet animatorSet3 = new AnimatorSet();
            r14 = new Animator[2];
            r14[0] = ObjectAnimator.ofFloat(this.endBtnIcon, "rotation", new float[]{-135.0f, 0.0f});
            r14[1] = ofArgb;
            animatorSet3.playTogether(r14);
            animatorSet3.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            animatorSet3.setDuration(500);
            animatorSet2 = new AnimatorSet();
            Animator[] animatorArr = new Animator[3];
            animatorArr[0] = ObjectAnimator.ofFloat(this.swipeViewsWrap, str, new float[]{1.0f, 0.0f});
            animatorArr[1] = ObjectAnimator.ofFloat(this.declineBtn, str, new float[]{0.0f});
            animatorArr[2] = ObjectAnimator.ofFloat(this.accountNameText, str, new float[]{0.0f});
            animatorSet2.playTogether(animatorArr);
            animatorSet2.setInterpolator(CubicBezierInterpolator.EASE_IN);
            animatorSet2.setDuration(125);
            animatorSet.playTogether(new Animator[]{animatorSet3, animatorSet2});
            animatorSet.addListener(new C324520());
            animatorSet.start();
            return;
        }
        animatorSet2 = new AnimatorSet();
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.bottomButtons, str, new float[]{0.0f, 1.0f})});
        animatorSet.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        animatorSet.setDuration(500);
        AnimatorSet animatorSet4 = new AnimatorSet();
        r5 = new Animator[4];
        r5[1] = ObjectAnimator.ofFloat(this.declineBtn, str, new float[]{0.0f});
        r5[2] = ObjectAnimator.ofFloat(this.acceptBtn, str, new float[]{0.0f});
        r5[3] = ObjectAnimator.ofFloat(this.accountNameText, str, new float[]{0.0f});
        animatorSet4.playTogether(r5);
        animatorSet4.setInterpolator(CubicBezierInterpolator.EASE_IN);
        animatorSet4.setDuration(125);
        animatorSet2.playTogether(new Animator[]{animatorSet, animatorSet4});
        animatorSet2.addListener(new C324621());
        animatorSet2.start();
    }

    private void showRetry() {
        ObjectAnimator ofArgb;
        AnimatorSet animatorSet = this.retryAnim;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        this.endBtn.setEnabled(false);
        this.retrying = true;
        this.cancelBtn.setVisibility(0);
        this.cancelBtn.setAlpha(0.0f);
        AnimatorSet animatorSet2 = new AnimatorSet();
        String str = "color";
        if (VERSION.SDK_INT >= 21) {
            ofArgb = ObjectAnimator.ofArgb(this.endBtnBg, str, new int[]{-1696188, -12207027});
        } else {
            ofArgb = ObjectAnimator.ofInt(this.endBtnBg, str, new int[]{-1696188, -12207027});
            ofArgb.setEvaluator(new ArgbEvaluator());
        }
        Animator[] animatorArr = new Animator[4];
        animatorArr[0] = ObjectAnimator.ofFloat(this.cancelBtn, "alpha", new float[]{0.0f, 1.0f});
        animatorArr[1] = ObjectAnimator.ofFloat(this.endBtn, "translationX", new float[]{0.0f, (float) (((this.content.getWidth() / 2) - AndroidUtilities.m26dp(52.0f)) - (this.endBtn.getWidth() / 2))});
        animatorArr[2] = ofArgb;
        animatorArr[3] = ObjectAnimator.ofFloat(this.endBtnIcon, "rotation", new float[]{0.0f, -135.0f});
        animatorSet2.playTogether(animatorArr);
        animatorSet2.setStartDelay(200);
        animatorSet2.setDuration(300);
        animatorSet2.setInterpolator(CubicBezierInterpolator.DEFAULT);
        animatorSet2.addListener(new C324722());
        this.retryAnim = animatorSet2;
        animatorSet2.start();
    }

    private void hideRetry() {
        ObjectAnimator ofArgb;
        AnimatorSet animatorSet = this.retryAnim;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        this.retrying = false;
        String str = "color";
        if (VERSION.SDK_INT >= 21) {
            ofArgb = ObjectAnimator.ofArgb(this.endBtnBg, str, new int[]{-12207027, -1696188});
        } else {
            ofArgb = ObjectAnimator.ofInt(this.endBtnBg, str, new int[]{-12207027, -1696188});
            ofArgb.setEvaluator(new ArgbEvaluator());
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        Animator[] animatorArr = new Animator[4];
        animatorArr[0] = ofArgb;
        animatorArr[1] = ObjectAnimator.ofFloat(this.endBtnIcon, "rotation", new float[]{-135.0f, 0.0f});
        animatorArr[2] = ObjectAnimator.ofFloat(this.endBtn, "translationX", new float[]{0.0f});
        animatorArr[3] = ObjectAnimator.ofFloat(this.cancelBtn, "alpha", new float[]{0.0f});
        animatorSet2.playTogether(animatorArr);
        animatorSet2.setStartDelay(200);
        animatorSet2.setDuration(300);
        animatorSet2.setInterpolator(CubicBezierInterpolator.DEFAULT);
        animatorSet2.addListener(new C324823());
        this.retryAnim = animatorSet2;
        animatorSet2.start();
    }

    public void onStateChanged(final int i) {
        final int i2 = this.callState;
        this.callState = i;
        runOnUiThread(new Runnable() {

            /* renamed from: org.telegram.ui.VoIPActivity$24$1 */
            class C32491 implements Runnable {
                C32491() {
                }

                public void run() {
                    VoIPActivity.this.finish();
                }
            }

            /* renamed from: org.telegram.ui.VoIPActivity$24$2 */
            class C32502 implements Runnable {
                C32502() {
                }

                public void run() {
                    VoIPActivity.this.tooltipHider = null;
                    VoIPActivity.this.setEmojiTooltipVisible(false);
                }
            }

            /* renamed from: org.telegram.ui.VoIPActivity$24$3 */
            class C32513 implements Runnable {
                C32513() {
                }

                public void run() {
                    VoIPActivity.this.finish();
                }
            }

            public void run() {
                int i;
                boolean access$3600 = VoIPActivity.this.firstStateChange;
                String str = "VoipIncoming";
                if (VoIPActivity.this.firstStateChange) {
                    VoIPActivity.this.spkToggle.setChecked(((AudioManager) VoIPActivity.this.getSystemService(MimeTypes.BASE_TYPE_AUDIO)).isSpeakerphoneOn());
                    VoIPActivity voIPActivity = VoIPActivity.this;
                    boolean z = i == 15;
                    voIPActivity.isIncomingWaiting = z;
                    if (z) {
                        VoIPActivity.this.swipeViewsWrap.setVisibility(0);
                        VoIPActivity.this.endBtn.setVisibility(8);
                        VoIPActivity.this.acceptSwipe.startAnimatingArrows();
                        VoIPActivity.this.declineSwipe.startAnimatingArrows();
                        if (UserConfig.getActivatedAccountsCount() > 1) {
                            User currentUser = UserConfig.getInstance(VoIPActivity.this.currentAccount).getCurrentUser();
                            VoIPActivity.this.accountNameText.setText(LocaleController.formatString("VoipAnsweringAsAccount", C1067R.string.VoipAnsweringAsAccount, ContactsController.formatName(currentUser.first_name, currentUser.last_name)));
                        } else {
                            VoIPActivity.this.accountNameText.setVisibility(8);
                        }
                        VoIPActivity.this.getWindow().addFlags(2097152);
                        VoIPService sharedInstance = VoIPService.getSharedInstance();
                        if (sharedInstance != null) {
                            sharedInstance.startRingtoneAndVibration();
                        }
                        VoIPActivity.this.setTitle(LocaleController.getString(str, C1067R.string.VoipIncoming));
                    } else {
                        VoIPActivity.this.swipeViewsWrap.setVisibility(8);
                        VoIPActivity.this.acceptBtn.setVisibility(8);
                        VoIPActivity.this.declineBtn.setVisibility(8);
                        VoIPActivity.this.accountNameText.setVisibility(8);
                        VoIPActivity.this.getWindow().clearFlags(2097152);
                    }
                    if (i != 3) {
                        VoIPActivity.this.emojiWrap.setVisibility(8);
                    }
                    VoIPActivity.this.firstStateChange = false;
                }
                if (VoIPActivity.this.isIncomingWaiting) {
                    i = i;
                    if (!(i == 15 || i == 11 || i == 10)) {
                        VoIPActivity.this.isIncomingWaiting = false;
                        if (!VoIPActivity.this.didAcceptFromHere) {
                            VoIPActivity.this.callAccepted();
                        }
                    }
                }
                i = i;
                int i2;
                if (i == 15) {
                    VoIPActivity.this.setStateTextAnimated(LocaleController.getString(str, C1067R.string.VoipIncoming), false);
                    VoIPActivity.this.getWindow().addFlags(2097152);
                } else if (i == 1 || i == 2) {
                    VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipConnecting", C1067R.string.VoipConnecting), true);
                } else if (i == 12) {
                    VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipExchangingKeys", C1067R.string.VoipExchangingKeys), true);
                } else if (i == 13) {
                    VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipWaiting", C1067R.string.VoipWaiting), true);
                } else if (i == 16) {
                    VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipRinging", C1067R.string.VoipRinging), true);
                } else if (i == 14) {
                    VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipRequesting", C1067R.string.VoipRequesting), true);
                } else if (i == 10) {
                    VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipHangingUp", C1067R.string.VoipHangingUp), true);
                    VoIPActivity.this.endBtnIcon.setAlpha(0.5f);
                    VoIPActivity.this.endBtn.setEnabled(false);
                } else if (i == 11) {
                    VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipCallEnded", C1067R.string.VoipCallEnded), false);
                    VoIPActivity.this.stateText.postDelayed(new C32491(), 200);
                } else if (i == 17) {
                    VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipBusy", C1067R.string.VoipBusy), false);
                    VoIPActivity.this.showRetry();
                } else if (i == 3 || i == 5) {
                    VoIPActivity.this.setTitle(null);
                    if (!access$3600 && i == 3) {
                        String str2 = "call_emoji_tooltip_count";
                        i2 = MessagesController.getGlobalMainSettings().getInt(str2, 0);
                        if (i2 < 3) {
                            VoIPActivity.this.setEmojiTooltipVisible(true);
                            TextView access$2500 = VoIPActivity.this.hintTextView;
                            VoIPActivity voIPActivity2 = VoIPActivity.this;
                            C32502 c32502 = new C32502();
                            voIPActivity2.tooltipHider = c32502;
                            access$2500.postDelayed(c32502, 5000);
                            MessagesController.getGlobalMainSettings().edit().putInt(str2, i2 + 1).commit();
                        }
                    }
                    i2 = i2;
                    if (!(i2 == 3 || i2 == 5)) {
                        VoIPActivity.this.setStateTextAnimated("0:00", false);
                        VoIPActivity.this.startUpdatingCallDuration();
                        VoIPActivity.this.updateKeyView();
                        if (VoIPActivity.this.emojiWrap.getVisibility() != 0) {
                            VoIPActivity.this.emojiWrap.setVisibility(0);
                            VoIPActivity.this.emojiWrap.setAlpha(0.0f);
                            VoIPActivity.this.emojiWrap.animate().alpha(1.0f).setDuration(200).setInterpolator(new DecelerateInterpolator()).start();
                        }
                    }
                } else if (i == 4) {
                    VoIPActivity.this.setStateTextAnimated(LocaleController.getString("VoipFailed", C1067R.string.VoipFailed), false);
                    i2 = VoIPService.getSharedInstance() != null ? VoIPService.getSharedInstance().getLastError() : 0;
                    if (i2 == 1) {
                        VoIPActivity.this.showErrorDialog(AndroidUtilities.replaceTags(LocaleController.formatString("VoipPeerIncompatible", C1067R.string.VoipPeerIncompatible, ContactsController.formatName(VoIPActivity.this.user.first_name, VoIPActivity.this.user.last_name))));
                    } else if (i2 == -1) {
                        VoIPActivity.this.showErrorDialog(AndroidUtilities.replaceTags(LocaleController.formatString("VoipPeerOutdated", C1067R.string.VoipPeerOutdated, ContactsController.formatName(VoIPActivity.this.user.first_name, VoIPActivity.this.user.last_name))));
                    } else if (i2 == -2) {
                        VoIPActivity.this.showErrorDialog(AndroidUtilities.replaceTags(LocaleController.formatString("CallNotAvailable", C1067R.string.CallNotAvailable, ContactsController.formatName(VoIPActivity.this.user.first_name, VoIPActivity.this.user.last_name))));
                    } else if (i2 == 3) {
                        VoIPActivity.this.showErrorDialog("Error initializing audio hardware");
                    } else if (i2 == -3) {
                        VoIPActivity.this.finish();
                    } else if (i2 == -5) {
                        VoIPActivity.this.showErrorDialog(LocaleController.getString("VoipErrorUnknown", C1067R.string.VoipErrorUnknown));
                    } else {
                        VoIPActivity.this.stateText.postDelayed(new C32513(), 1000);
                    }
                }
                VoIPActivity.this.brandingText.invalidate();
            }
        });
    }

    public void onSignalBarsCountChanged(final int i) {
        runOnUiThread(new Runnable() {
            public void run() {
                VoIPActivity.this.signalBarsCount = i;
                VoIPActivity.this.brandingText.invalidate();
            }
        });
    }

    private void showErrorDialog(CharSequence charSequence) {
        AlertDialog show = new Builder(this).setTitle(LocaleController.getString("VoipFailed", C1067R.string.VoipFailed)).setMessage(charSequence).setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), null).show();
        show.setCanceledOnTouchOutside(true);
        show.setOnDismissListener(new C325426());
    }

    public void onAudioSettingsChanged() {
        VoIPBaseService sharedInstance = VoIPBaseService.getSharedInstance();
        if (sharedInstance != null) {
            this.micToggle.setChecked(sharedInstance.isMicMute());
            if (sharedInstance.hasEarpiece() || sharedInstance.isBluetoothHeadsetConnected()) {
                this.spkToggle.setVisibility(0);
                if (!sharedInstance.hasEarpiece()) {
                    this.spkToggle.setImageResource(C1067R.C1065drawable.ic_bluetooth_white_24dp);
                    this.spkToggle.setChecked(sharedInstance.isSpeakerphoneOn());
                } else if (sharedInstance.isBluetoothHeadsetConnected()) {
                    int currentAudioRoute = sharedInstance.getCurrentAudioRoute();
                    if (currentAudioRoute == 0) {
                        this.spkToggle.setImageResource(C1067R.C1065drawable.ic_phone_in_talk_white_24dp);
                    } else if (currentAudioRoute == 1) {
                        this.spkToggle.setImageResource(C1067R.C1065drawable.ic_volume_up_white_24dp);
                    } else if (currentAudioRoute == 2) {
                        this.spkToggle.setImageResource(C1067R.C1065drawable.ic_bluetooth_white_24dp);
                    }
                    this.spkToggle.setChecked(false);
                } else {
                    this.spkToggle.setImageResource(C1067R.C1065drawable.ic_volume_up_white_24dp);
                    this.spkToggle.setChecked(sharedInstance.isSpeakerphoneOn());
                }
            } else {
                this.spkToggle.setVisibility(4);
            }
        }
    }

    private void setStateTextAnimated(String str, boolean z) {
        if (!str.equals(this.lastStateText)) {
            CharSequence spannableStringBuilder;
            this.lastStateText = str;
            Animator animator = this.textChangingAnim;
            if (animator != null) {
                animator.cancel();
            }
            if (z) {
                if (!this.ellAnimator.isRunning()) {
                    this.ellAnimator.start();
                }
                spannableStringBuilder = new SpannableStringBuilder(str.toUpperCase());
                for (TextAlphaSpan alpha : this.ellSpans) {
                    alpha.setAlpha(0);
                }
                SpannableString spannableString = new SpannableString("...");
                spannableString.setSpan(this.ellSpans[0], 0, 1, 0);
                spannableString.setSpan(this.ellSpans[1], 1, 2, 0);
                spannableString.setSpan(this.ellSpans[2], 2, 3, 0);
                spannableStringBuilder.append(spannableString);
            } else {
                if (this.ellAnimator.isRunning()) {
                    this.ellAnimator.cancel();
                }
                spannableStringBuilder = str.toUpperCase();
            }
            this.stateText2.setText(spannableStringBuilder);
            this.stateText2.setVisibility(0);
            TextView textView = this.stateText;
            textView.setPivotX(LocaleController.isRTL ? (float) textView.getWidth() : 0.0f);
            textView = this.stateText;
            textView.setPivotY((float) (textView.getHeight() / 2));
            this.stateText2.setPivotX(LocaleController.isRTL ? (float) this.stateText.getWidth() : 0.0f);
            this.stateText2.setPivotY((float) (this.stateText.getHeight() / 2));
            this.durationText = this.stateText2;
            AnimatorSet animatorSet = new AnimatorSet();
            r13 = new Animator[8];
            String str2 = "alpha";
            r13[0] = ObjectAnimator.ofFloat(this.stateText2, str2, new float[]{0.0f, 1.0f});
            String str3 = "translationY";
            r13[1] = ObjectAnimator.ofFloat(this.stateText2, str3, new float[]{(float) (this.stateText.getHeight() / 2), 0.0f});
            String str4 = "scaleX";
            r13[2] = ObjectAnimator.ofFloat(this.stateText2, str4, new float[]{0.7f, 1.0f});
            String str5 = "scaleY";
            r13[3] = ObjectAnimator.ofFloat(this.stateText2, str5, new float[]{0.7f, 1.0f});
            r13[4] = ObjectAnimator.ofFloat(this.stateText, str2, new float[]{1.0f, 0.0f});
            r13[5] = ObjectAnimator.ofFloat(this.stateText, str3, new float[]{0.0f, (float) ((-this.stateText.getHeight()) / 2)});
            r13[6] = ObjectAnimator.ofFloat(this.stateText, str4, new float[]{1.0f, 0.7f});
            r13[7] = ObjectAnimator.ofFloat(this.stateText, str5, new float[]{1.0f, 0.7f});
            animatorSet.playTogether(r13);
            animatorSet.setDuration(200);
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            animatorSet.addListener(new C325527());
            this.textChangingAnim = animatorSet;
            animatorSet.start();
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.emojiDidLoad) {
            for (ImageView invalidate : this.keyEmojiViews) {
                invalidate.invalidate();
            }
        }
        if (i == NotificationCenter.closeInCallActivity) {
            finish();
        }
    }

    private void setEmojiTooltipVisible(boolean z) {
        this.emojiTooltipVisible = z;
        Animator animator = this.tooltipAnim;
        if (animator != null) {
            animator.cancel();
        }
        this.hintTextView.setVisibility(0);
        TextView textView = this.hintTextView;
        float[] fArr = new float[1];
        fArr[0] = z ? 1.0f : 0.0f;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(textView, "alpha", fArr);
        ofFloat.setDuration(300);
        ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
        ofFloat.addListener(new C325628());
        this.tooltipAnim = ofFloat;
        ofFloat.start();
    }

    private void setEmojiExpanded(boolean z) {
        boolean z2 = z;
        if (this.emojiExpanded != z2) {
            this.emojiExpanded = z2;
            AnimatorSet animatorSet = this.emojiAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            String str = "scaleY";
            String str2 = "scaleX";
            String str3 = "translationY";
            String str4 = "translationX";
            String str5 = "alpha";
            AnimatorSet animatorSet2;
            if (z2) {
                int[] iArr = new int[]{0, 0};
                int[] iArr2 = new int[]{0, 0};
                this.emojiWrap.getLocationInWindow(iArr);
                this.emojiExpandedText.getLocationInWindow(iArr2);
                Rect rect = new Rect();
                getWindow().getDecorView().getGlobalVisibleRect(rect);
                int height = ((iArr2[1] - (iArr[1] + this.emojiWrap.getHeight())) - AndroidUtilities.m26dp(32.0f)) - this.emojiWrap.getHeight();
                int width = ((rect.width() / 2) - (Math.round(((float) this.emojiWrap.getWidth()) * 2.5f) / 2)) - iArr[0];
                animatorSet2 = new AnimatorSet();
                r8 = new Animator[7];
                r8[0] = ObjectAnimator.ofFloat(this.emojiWrap, str3, new float[]{(float) height});
                r8[1] = ObjectAnimator.ofFloat(this.emojiWrap, str4, new float[]{(float) width});
                r8[2] = ObjectAnimator.ofFloat(this.emojiWrap, str2, new float[]{2.5f});
                r8[3] = ObjectAnimator.ofFloat(this.emojiWrap, str, new float[]{2.5f});
                r8[4] = ObjectAnimator.ofFloat(this.blurOverlayView1, str5, new float[]{this.blurOverlayView1.getAlpha(), 1.0f, 1.0f});
                r8[5] = ObjectAnimator.ofFloat(this.blurOverlayView2, str5, new float[]{this.blurOverlayView2.getAlpha(), this.blurOverlayView2.getAlpha(), 1.0f});
                r8[6] = ObjectAnimator.ofFloat(this.emojiExpandedText, str5, new float[]{1.0f});
                animatorSet2.playTogether(r8);
                animatorSet2.setDuration(300);
                animatorSet2.setInterpolator(CubicBezierInterpolator.DEFAULT);
                this.emojiAnimator = animatorSet2;
                animatorSet2.addListener(new C325729());
                animatorSet2.start();
            } else {
                animatorSet2 = new AnimatorSet();
                r2 = new Animator[7];
                r2[0] = ObjectAnimator.ofFloat(this.emojiWrap, str4, new float[]{0.0f});
                r2[1] = ObjectAnimator.ofFloat(this.emojiWrap, str3, new float[]{0.0f});
                r2[2] = ObjectAnimator.ofFloat(this.emojiWrap, str2, new float[]{1.0f});
                r2[3] = ObjectAnimator.ofFloat(this.emojiWrap, str, new float[]{1.0f});
                r2[4] = ObjectAnimator.ofFloat(this.blurOverlayView1, str5, new float[]{this.blurOverlayView1.getAlpha(), this.blurOverlayView1.getAlpha(), 0.0f});
                r2[5] = ObjectAnimator.ofFloat(this.blurOverlayView2, str5, new float[]{this.blurOverlayView2.getAlpha(), 0.0f, 0.0f});
                r2[6] = ObjectAnimator.ofFloat(this.emojiExpandedText, str5, new float[]{0.0f});
                animatorSet2.playTogether(r2);
                animatorSet2.setDuration(300);
                animatorSet2.setInterpolator(CubicBezierInterpolator.DEFAULT);
                this.emojiAnimator = animatorSet2;
                animatorSet2.addListener(new C326030());
                animatorSet2.start();
            }
        }
    }

    private void updateBlurredPhotos(final BitmapHolder bitmapHolder) {
        new Thread(new Runnable() {

            /* renamed from: org.telegram.ui.VoIPActivity$31$1 */
            class C32611 implements Runnable {
                C32611() {
                }

                public void run() {
                    VoIPActivity.this.blurOverlayView1.setImageBitmap(VoIPActivity.this.blurredPhoto1);
                    VoIPActivity.this.blurOverlayView2.setImageBitmap(VoIPActivity.this.blurredPhoto2);
                    bitmapHolder.release();
                }
            }

            public void run() {
                try {
                    Bitmap createBitmap = Bitmap.createBitmap(150, 150, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.drawBitmap(bitmapHolder.bitmap, null, new Rect(0, 0, 150, 150), new Paint(2));
                    Utilities.blurBitmap(createBitmap, 3, 0, createBitmap.getWidth(), createBitmap.getHeight(), createBitmap.getRowBytes());
                    Palette generate = Palette.from(bitmapHolder.bitmap).generate();
                    Paint paint = new Paint();
                    paint.setColor((generate.getDarkMutedColor(-11242343) & 16777215) | 1140850688);
                    canvas.drawColor(637534208);
                    canvas.drawRect(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), paint);
                    Bitmap createBitmap2 = Bitmap.createBitmap(50, 50, Config.ARGB_8888);
                    Canvas canvas2 = new Canvas(createBitmap2);
                    canvas2.drawBitmap(bitmapHolder.bitmap, null, new Rect(0, 0, 50, 50), new Paint(2));
                    Utilities.blurBitmap(createBitmap2, 3, 0, createBitmap2.getWidth(), createBitmap2.getHeight(), createBitmap2.getRowBytes());
                    paint.setAlpha(102);
                    canvas2.drawRect(0.0f, 0.0f, (float) canvas2.getWidth(), (float) canvas2.getHeight(), paint);
                    VoIPActivity.this.blurredPhoto1 = createBitmap;
                    VoIPActivity.this.blurredPhoto2 = createBitmap2;
                    VoIPActivity.this.runOnUiThread(new C32611());
                } catch (Throwable unused) {
                }
            }
        }).start();
    }

    private void sendTextMessage(final String str) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                SendMessagesHelper.getInstance(VoIPActivity.this.currentAccount).sendMessage(str, (long) VoIPActivity.this.user.f534id, null, null, false, null, null, null);
            }
        });
    }

    private void showMessagesSheet() {
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().stopRinging();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("mainconfig", 0);
        r4 = new String[4];
        r4[0] = sharedPreferences.getString("quick_reply_msg1", LocaleController.getString("QuickReplyDefault1", C1067R.string.QuickReplyDefault1));
        r4[1] = sharedPreferences.getString("quick_reply_msg2", LocaleController.getString("QuickReplyDefault2", C1067R.string.QuickReplyDefault2));
        r4[2] = sharedPreferences.getString("quick_reply_msg3", LocaleController.getString("QuickReplyDefault3", C1067R.string.QuickReplyDefault3));
        r4[3] = sharedPreferences.getString("quick_reply_msg4", LocaleController.getString("QuickReplyDefault4", C1067R.string.QuickReplyDefault4));
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        final BottomSheet bottomSheet = new BottomSheet(this, true, 0);
        if (VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(-13948117);
            bottomSheet.setOnDismissListener(new C326433());
        }
        C326634 c326634 = new OnClickListener() {
            public void onClick(final View view) {
                bottomSheet.dismiss();
                if (VoIPService.getSharedInstance() != null) {
                    VoIPService.getSharedInstance().declineIncomingCall(4, new Runnable() {
                        public void run() {
                            VoIPActivity.this.sendTextMessage((String) view.getTag());
                        }
                    });
                }
            }
        };
        for (CharSequence charSequence : r4) {
            BottomSheetCell bottomSheetCell = new BottomSheetCell(this, 0);
            bottomSheetCell.setTextAndIcon(charSequence, 0);
            bottomSheetCell.setTextColor(-1);
            bottomSheetCell.setTag(charSequence);
            bottomSheetCell.setOnClickListener(c326634);
            linearLayout.addView(bottomSheetCell);
        }
        FrameLayout frameLayout = new FrameLayout(this);
        final BottomSheetCell bottomSheetCell2 = new BottomSheetCell(this, 0);
        String str = "QuickReplyCustom";
        bottomSheetCell2.setTextAndIcon(LocaleController.getString(str, C1067R.string.QuickReplyCustom), 0);
        bottomSheetCell2.setTextColor(-1);
        frameLayout.addView(bottomSheetCell2);
        final FrameLayout frameLayout2 = new FrameLayout(this);
        final EditText editText = new EditText(this);
        editText.setTextSize(1, 16.0f);
        editText.setTextColor(-1);
        editText.setHintTextColor(DarkTheme.getColor(Theme.key_chat_messagePanelHint));
        editText.setBackgroundDrawable(null);
        editText.setPadding(AndroidUtilities.m26dp(16.0f), AndroidUtilities.m26dp(11.0f), AndroidUtilities.m26dp(16.0f), AndroidUtilities.m26dp(12.0f));
        editText.setHint(LocaleController.getString(str, C1067R.string.QuickReplyCustom));
        editText.setMinHeight(AndroidUtilities.m26dp(48.0f));
        editText.setGravity(80);
        editText.setMaxLines(4);
        editText.setSingleLine(false);
        editText.setInputType((editText.getInputType() | 16384) | MessagesController.UPDATE_MASK_REORDER);
        frameLayout2.addView(editText, LayoutHelper.createFrame(-1, -2.0f, LocaleController.isRTL ? 5 : 3, LocaleController.isRTL ? 48.0f : 0.0f, 0.0f, LocaleController.isRTL ? 0.0f : 48.0f, 0.0f));
        final ImageView imageView = new ImageView(this);
        imageView.setScaleType(ScaleType.CENTER);
        imageView.setImageDrawable(DarkTheme.getThemedDrawable(this, C1067R.C1065drawable.ic_send, Theme.key_chat_messagePanelSend));
        if (LocaleController.isRTL) {
            imageView.setScaleX(-0.1f);
        } else {
            imageView.setScaleX(0.1f);
        }
        imageView.setScaleY(0.1f);
        imageView.setAlpha(0.0f);
        frameLayout2.addView(imageView, LayoutHelper.createFrame(48, 48, (LocaleController.isRTL ? 3 : 5) | 80));
        imageView.setOnClickListener(new OnClickListener() {

            /* renamed from: org.telegram.ui.VoIPActivity$35$1 */
            class C32671 implements Runnable {
                C32671() {
                }

                public void run() {
                    C326835 c326835 = C326835.this;
                    VoIPActivity.this.sendTextMessage(editText.getText().toString());
                }
            }

            public void onClick(View view) {
                if (editText.length() != 0) {
                    bottomSheet.dismiss();
                    if (VoIPService.getSharedInstance() != null) {
                        VoIPService.getSharedInstance().declineIncomingCall(4, new C32671());
                    }
                }
            }
        });
        imageView.setVisibility(4);
        final ImageView imageView2 = new ImageView(this);
        imageView2.setScaleType(ScaleType.CENTER);
        imageView2.setImageDrawable(DarkTheme.getThemedDrawable(this, C1067R.C1065drawable.edit_cancel, Theme.key_chat_messagePanelIcons));
        frameLayout2.addView(imageView2, LayoutHelper.createFrame(48, 48, (LocaleController.isRTL ? 3 : 5) | 80));
        imageView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                frameLayout2.setVisibility(8);
                bottomSheetCell2.setVisibility(0);
                editText.setText("");
                ((InputMethodManager) VoIPActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            boolean prevState = false;

            /* renamed from: org.telegram.ui.VoIPActivity$37$1 */
            class C32701 implements Runnable {
                C32701() {
                }

                public void run() {
                    imageView2.setVisibility(4);
                }
            }

            /* renamed from: org.telegram.ui.VoIPActivity$37$2 */
            class C32712 implements Runnable {
                C32712() {
                }

                public void run() {
                    imageView.setVisibility(4);
                }
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                boolean z = editable.length() > 0;
                if (this.prevState != z) {
                    this.prevState = z;
                    if (z) {
                        imageView.setVisibility(0);
                        imageView.animate().alpha(1.0f).scaleX(LocaleController.isRTL ? -1.0f : 1.0f).scaleY(1.0f).setDuration(200).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                        imageView2.animate().alpha(0.0f).scaleX(0.1f).scaleY(0.1f).setInterpolator(CubicBezierInterpolator.DEFAULT).setDuration(200).withEndAction(new C32701()).start();
                        return;
                    }
                    imageView2.setVisibility(0);
                    imageView2.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(200).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                    imageView.animate().alpha(0.0f).scaleX(LocaleController.isRTL ? -0.1f : 0.1f).scaleY(0.1f).setInterpolator(CubicBezierInterpolator.DEFAULT).setDuration(200).withEndAction(new C32712()).start();
                }
            }
        });
        frameLayout2.setVisibility(8);
        frameLayout.addView(frameLayout2);
        bottomSheetCell2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                frameLayout2.setVisibility(0);
                bottomSheetCell2.setVisibility(4);
                editText.requestFocus();
                ((InputMethodManager) VoIPActivity.this.getSystemService("input_method")).showSoftInput(editText, 0);
            }
        });
        linearLayout.addView(frameLayout);
        bottomSheet.setCustomView(linearLayout);
        bottomSheet.setBackgroundColor(-13948117);
        bottomSheet.show();
    }
}
