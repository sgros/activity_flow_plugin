// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.ChatObject;
import android.graphics.Typeface;
import android.widget.FrameLayout$LayoutParams;
import android.view.View$MeasureSpec;
import android.os.SystemClock;
import android.graphics.Canvas;
import android.util.Property;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.AnimatorSet;
import org.telegram.messenger.MessagesController;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.graphics.Paint$Cap;
import android.graphics.Paint$Style;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.view.View$OnClickListener;
import android.graphics.ColorFilter;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import android.widget.ImageView$ScaleType;
import android.text.TextUtils$TruncateAt;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.text.TextPaint;
import android.graphics.RectF;
import android.graphics.Paint;
import com.airbnb.lottie.LottieAnimationView;
import android.widget.TextView;
import android.widget.FrameLayout;

public class UndoView extends FrameLayout
{
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
    
    public UndoView(final Context context) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        (this.infoTextView = new TextView(context)).setTextSize(1, 15.0f);
        this.infoTextView.setTextColor(Theme.getColor("undo_infoColor"));
        this.addView((View)this.infoTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 45.0f, 13.0f, 0.0f, 0.0f));
        (this.subinfoTextView = new TextView(context)).setTextSize(1, 13.0f);
        this.subinfoTextView.setTextColor(Theme.getColor("undo_infoColor"));
        this.subinfoTextView.setSingleLine(true);
        this.subinfoTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.addView((View)this.subinfoTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 58.0f, 27.0f, 8.0f, 0.0f));
        (this.leftImageView = new LottieAnimationView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "info1", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_background") | 0xFF000000)));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "info2", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_background") | 0xFF000000)));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "luc12", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "luc11", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "luc10", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "luc9", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "luc8", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "luc7", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "luc6", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "luc5", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "luc4", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "luc3", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "luc2", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "luc1", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
        this.leftImageView.addValueCallback(new KeyPath(new String[] { "Oval", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("undo_infoColor"))));
        this.addView((View)this.leftImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(54, -2.0f, 19, 3.0f, 0.0f, 0.0f, 0.0f));
        (this.undoButton = new LinearLayout(context)).setOrientation(0);
        this.addView((View)this.undoButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, 21, 0.0f, 0.0f, 19.0f, 0.0f));
        this.undoButton.setOnClickListener((View$OnClickListener)new _$$Lambda$UndoView$ut_O3jMsR3UxcWCuvOqjPzYJ4Go(this));
        (this.undoImageView = new ImageView(context)).setImageResource(2131165353);
        this.undoImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("undo_cancelColor"), PorterDuff$Mode.MULTIPLY));
        this.undoButton.addView((View)this.undoImageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 19));
        (this.undoTextView = new TextView(context)).setTextSize(1, 14.0f);
        this.undoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.undoTextView.setTextColor(Theme.getColor("undo_cancelColor"));
        this.undoTextView.setText((CharSequence)LocaleController.getString("Undo", 2131560934));
        this.undoButton.addView((View)this.undoTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 19, 6, 0, 0, 0));
        this.rect = new RectF((float)AndroidUtilities.dp(15.0f), (float)AndroidUtilities.dp(15.0f), (float)AndroidUtilities.dp(33.0f), (float)AndroidUtilities.dp(33.0f));
        (this.progressPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
        this.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.progressPaint.setStrokeCap(Paint$Cap.ROUND);
        this.progressPaint.setColor(Theme.getColor("undo_infoColor"));
        (this.textPaint = new TextPaint(1)).setTextSize((float)AndroidUtilities.dp(12.0f));
        this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textPaint.setColor(Theme.getColor("undo_infoColor"));
        this.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("undo_background")));
        this.setOnTouchListener((View$OnTouchListener)_$$Lambda$UndoView$cqEu5tq8BrTwZKHKJIY3aTuNIjk.INSTANCE);
        this.setVisibility(4);
    }
    
    private boolean isTooltipAction() {
        final int currentAction = this.currentAction;
        return currentAction == 6 || currentAction == 3 || currentAction == 5 || currentAction == 7;
    }
    
    protected boolean canUndo() {
        return true;
    }
    
    public void hide(final boolean b, final int n) {
        if (this.getVisibility() == 0) {
            if (this.isShowed) {
                this.isShowed = false;
                final Runnable currentActionRunnable = this.currentActionRunnable;
                if (currentActionRunnable != null) {
                    if (b) {
                        currentActionRunnable.run();
                    }
                    this.currentActionRunnable = null;
                }
                final Runnable currentCancelRunnable = this.currentCancelRunnable;
                if (currentCancelRunnable != null) {
                    if (!b) {
                        currentCancelRunnable.run();
                    }
                    this.currentCancelRunnable = null;
                }
                final int currentAction = this.currentAction;
                if (currentAction == 0 || currentAction == 1) {
                    MessagesController.getInstance(this.currentAccount).removeDialogAction(this.currentDialogId, this.currentAction == 0, b);
                }
                int n2 = 52;
                if (n != 0) {
                    final AnimatorSet set = new AnimatorSet();
                    if (n == 1) {
                        final Property translation_Y = View.TRANSLATION_Y;
                        if (!this.isTooltipAction()) {
                            n2 = 48;
                        }
                        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, translation_Y, new float[] { (float)AndroidUtilities.dp((float)(n2 + 8)) }) });
                        set.setDuration(250L);
                    }
                    else {
                        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, View.SCALE_X, new float[] { 0.8f }), (Animator)ObjectAnimator.ofFloat((Object)this, View.SCALE_Y, new float[] { 0.8f }), (Animator)ObjectAnimator.ofFloat((Object)this, View.ALPHA, new float[] { 0.0f }) });
                        set.setDuration(180L);
                    }
                    set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                    set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            UndoView.this.setVisibility(4);
                            UndoView.this.setScaleX(1.0f);
                            UndoView.this.setScaleY(1.0f);
                            UndoView.this.setAlpha(1.0f);
                        }
                    });
                    set.start();
                }
                else {
                    if (!this.isTooltipAction()) {
                        n2 = 48;
                    }
                    this.setTranslationY((float)AndroidUtilities.dp((float)(n2 + 8)));
                    this.setVisibility(4);
                }
            }
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        final int currentAction = this.currentAction;
        if (currentAction == 1 || currentAction == 0) {
            final long timeLeft = this.timeLeft;
            int n;
            if (timeLeft > 0L) {
                n = (int)Math.ceil(timeLeft / 1000.0f);
            }
            else {
                n = 0;
            }
            if (this.prevSeconds != n) {
                this.prevSeconds = n;
                this.timeLeftString = String.format("%d", Math.max(1, n));
                this.textWidth = (int)Math.ceil(this.textPaint.measureText(this.timeLeftString));
            }
            canvas.drawText(this.timeLeftString, this.rect.centerX() - this.textWidth / 2, (float)AndroidUtilities.dp(28.2f), (Paint)this.textPaint);
            canvas.drawArc(this.rect, -90.0f, this.timeLeft / 5000.0f * -360.0f, false, this.progressPaint);
        }
        final long uptimeMillis = SystemClock.uptimeMillis();
        this.timeLeft -= uptimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = uptimeMillis;
        if (this.timeLeft <= 0L) {
            this.hide(true, 1);
        }
        this.invalidate();
    }
    
    protected void onMeasure(int measureSpec, final int n) {
        measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(measureSpec), 1073741824);
        float n2;
        if (this.isTooltipAction()) {
            n2 = 52.0f;
        }
        else {
            n2 = 48.0f;
        }
        super.onMeasure(measureSpec, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(n2), 1073741824));
    }
    
    public void showWithAction(final long n, final int n2, final Runnable runnable) {
        this.showWithAction(n, n2, runnable, null);
    }
    
    public void showWithAction(final long currentDialogId, int currentAction, final Runnable currentActionRunnable, final Runnable currentCancelRunnable) {
        final Runnable currentActionRunnable2 = this.currentActionRunnable;
        if (currentActionRunnable2 != null) {
            currentActionRunnable2.run();
        }
        this.isShowed = true;
        this.currentActionRunnable = currentActionRunnable;
        this.currentCancelRunnable = currentCancelRunnable;
        this.currentDialogId = currentDialogId;
        this.currentAction = currentAction;
        this.timeLeft = 5000L;
        this.lastUpdateTime = SystemClock.uptimeMillis();
        if (this.isTooltipAction()) {
            if (currentAction == 6) {
                this.infoTextView.setText((CharSequence)LocaleController.getString("ArchiveHidden", 2131558643));
                this.subinfoTextView.setText((CharSequence)LocaleController.getString("ArchiveHiddenInfo", 2131558644));
                this.leftImageView.setAnimation(2131492869);
            }
            else if (currentAction == 7) {
                this.infoTextView.setText((CharSequence)LocaleController.getString("ArchivePinned", 2131558651));
                this.subinfoTextView.setText((CharSequence)LocaleController.getString("ArchivePinnedInfo", 2131558652));
                this.leftImageView.setAnimation(2131492868);
            }
            else {
                if (currentAction == 3) {
                    this.infoTextView.setText((CharSequence)LocaleController.getString("ChatArchived", 2131559022));
                }
                else {
                    this.infoTextView.setText((CharSequence)LocaleController.getString("ChatsArchived", 2131559052));
                }
                this.subinfoTextView.setText((CharSequence)LocaleController.getString("ChatArchivedInfo", 2131559023));
                this.leftImageView.setAnimation(2131492868);
            }
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.infoTextView.getLayoutParams();
            frameLayout$LayoutParams.leftMargin = AndroidUtilities.dp(58.0f);
            frameLayout$LayoutParams.topMargin = AndroidUtilities.dp(6.0f);
            this.infoTextView.setTextSize(1, 14.0f);
            this.infoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.subinfoTextView.setVisibility(0);
            this.undoButton.setVisibility(8);
            this.leftImageView.setVisibility(0);
            this.leftImageView.setProgress(0.0f);
            this.leftImageView.playAnimation();
        }
        else {
            final int currentAction2 = this.currentAction;
            if (currentAction2 != 2 && currentAction2 != 4) {
                final FrameLayout$LayoutParams frameLayout$LayoutParams2 = (FrameLayout$LayoutParams)this.infoTextView.getLayoutParams();
                frameLayout$LayoutParams2.leftMargin = AndroidUtilities.dp(45.0f);
                frameLayout$LayoutParams2.topMargin = AndroidUtilities.dp(13.0f);
                this.infoTextView.setTextSize(1, 15.0f);
                this.undoButton.setVisibility(0);
                this.infoTextView.setTypeface(Typeface.DEFAULT);
                this.subinfoTextView.setVisibility(8);
                this.leftImageView.setVisibility(8);
                if (this.currentAction == 0) {
                    this.infoTextView.setText((CharSequence)LocaleController.getString("HistoryClearedUndo", 2131559640));
                }
                else {
                    currentAction = (int)currentDialogId;
                    if (currentAction < 0) {
                        final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-currentAction);
                        if (ChatObject.isChannel(chat) && !chat.megagroup) {
                            this.infoTextView.setText((CharSequence)LocaleController.getString("ChannelDeletedUndo", 2131558948));
                        }
                        else {
                            this.infoTextView.setText((CharSequence)LocaleController.getString("GroupDeletedUndo", 2131559602));
                        }
                    }
                    else {
                        this.infoTextView.setText((CharSequence)LocaleController.getString("ChatDeletedUndo", 2131559026));
                    }
                }
                MessagesController.getInstance(this.currentAccount).addDialogAction(currentDialogId, this.currentAction == 0);
            }
            else {
                if (currentAction == 2) {
                    this.infoTextView.setText((CharSequence)LocaleController.getString("ChatArchived", 2131559022));
                }
                else {
                    this.infoTextView.setText((CharSequence)LocaleController.getString("ChatsArchived", 2131559052));
                }
                final FrameLayout$LayoutParams frameLayout$LayoutParams3 = (FrameLayout$LayoutParams)this.infoTextView.getLayoutParams();
                frameLayout$LayoutParams3.leftMargin = AndroidUtilities.dp(58.0f);
                frameLayout$LayoutParams3.topMargin = AndroidUtilities.dp(13.0f);
                this.infoTextView.setTextSize(1, 15.0f);
                this.undoButton.setVisibility(0);
                this.infoTextView.setTypeface(Typeface.DEFAULT);
                this.subinfoTextView.setVisibility(8);
                this.leftImageView.setVisibility(0);
                this.leftImageView.setAnimation(2131492866);
                this.leftImageView.setProgress(0.0f);
                this.leftImageView.playAnimation();
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append((Object)this.infoTextView.getText());
        String string;
        if (this.subinfoTextView.getVisibility() == 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(". ");
            sb2.append((Object)this.subinfoTextView.getText());
            string = sb2.toString();
        }
        else {
            string = "";
        }
        sb.append(string);
        AndroidUtilities.makeAccessibilityAnnouncement(sb.toString());
        if (this.getVisibility() != 0) {
            this.setVisibility(0);
            final boolean tooltipAction = this.isTooltipAction();
            final int n = 52;
            if (tooltipAction) {
                currentAction = 52;
            }
            else {
                currentAction = 48;
            }
            this.setTranslationY((float)AndroidUtilities.dp((float)(currentAction + 8)));
            final AnimatorSet set = new AnimatorSet();
            final Property translation_Y = View.TRANSLATION_Y;
            if (this.isTooltipAction()) {
                currentAction = n;
            }
            else {
                currentAction = 48;
            }
            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, translation_Y, new float[] { (float)AndroidUtilities.dp((float)(currentAction + 8)), 0.0f }) });
            set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
            set.setDuration(180L);
            set.start();
        }
    }
}
