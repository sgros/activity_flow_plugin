// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.MessageObject;
import org.telegram.messenger.ImageReceiver;
import android.view.View$MeasureSpec;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import org.telegram.messenger.LocaleController;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import org.telegram.ui.Cells.ChatMessageCell;
import android.widget.ImageView;
import android.animation.AnimatorSet;
import android.widget.FrameLayout;

public class HintView extends FrameLayout
{
    private AnimatorSet animatorSet;
    private ImageView arrowImageView;
    private int currentType;
    private Runnable hideRunnable;
    private ImageView imageView;
    private boolean isTopArrow;
    private ChatMessageCell messageCell;
    private String overrideText;
    private TextView textView;
    
    public HintView(final Context context, final int n) {
        this(context, n, false);
    }
    
    public HintView(final Context context, int n, final boolean isTopArrow) {
        super(context);
        this.currentType = n;
        this.isTopArrow = isTopArrow;
        (this.textView = new CorrectlyMeasuringTextView(context)).setTextColor(Theme.getColor("chat_gifSaveHintText"));
        this.textView.setTextSize(1, 14.0f);
        this.textView.setMaxLines(2);
        this.textView.setMaxWidth(AndroidUtilities.dp(250.0f));
        this.textView.setGravity(51);
        this.textView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0f), Theme.getColor("chat_gifSaveHintBackground")));
        final TextView textView = this.textView;
        float n2;
        if (this.currentType == 0) {
            n2 = 54.0f;
        }
        else {
            n2 = 5.0f;
        }
        textView.setPadding(AndroidUtilities.dp(n2), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(7.0f));
        final TextView textView2 = this.textView;
        float n3;
        if (isTopArrow) {
            n3 = 6.0f;
        }
        else {
            n3 = 0.0f;
        }
        float n4;
        if (isTopArrow) {
            n4 = 0.0f;
        }
        else {
            n4 = 6.0f;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, n3, 0.0f, n4));
        if (n == 0) {
            this.textView.setText((CharSequence)LocaleController.getString("AutoplayVideoInfo", 2131558806));
            (this.imageView = new ImageView(context)).setImageResource(2131165890);
            this.imageView.setScaleType(ImageView$ScaleType.CENTER);
            this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_gifSaveHintText"), PorterDuff$Mode.MULTIPLY));
            this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(38, 34.0f, 51, 7.0f, 7.0f, 0.0f, 0.0f));
        }
        this.arrowImageView = new ImageView(context);
        final ImageView arrowImageView = this.arrowImageView;
        if (isTopArrow) {
            n = 2131165887;
        }
        else {
            n = 2131165886;
        }
        arrowImageView.setImageResource(n);
        this.arrowImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_gifSaveHintBackground"), PorterDuff$Mode.MULTIPLY));
        final ImageView arrowImageView2 = this.arrowImageView;
        if (isTopArrow) {
            n = 48;
        }
        else {
            n = 80;
        }
        this.addView((View)arrowImageView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(14, 6.0f, n | 0x3, 0.0f, 0.0f, 0.0f, 0.0f));
    }
    
    public ChatMessageCell getMessageCell() {
        return this.messageCell;
    }
    
    public void hide() {
        if (this.getTag() == null) {
            return;
        }
        this.setTag((Object)null);
        final Runnable hideRunnable = this.hideRunnable;
        if (hideRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(hideRunnable);
            this.hideRunnable = null;
        }
        final AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        (this.animatorSet = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "alpha", new float[] { 0.0f }) });
        this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                HintView.this.setVisibility(4);
                HintView.this.messageCell = null;
                HintView.this.animatorSet = null;
            }
        });
        this.animatorSet.setDuration(300L);
        this.animatorSet.start();
    }
    
    public void setOverrideText(final String s) {
        this.overrideText = s;
        this.textView.setText((CharSequence)s);
        final ChatMessageCell messageCell = this.messageCell;
        if (messageCell != null) {
            this.messageCell = null;
            this.showForMessageCell(messageCell, false);
        }
    }
    
    public boolean showForMessageCell(final ChatMessageCell messageCell, final boolean b) {
        if ((this.currentType == 0 && this.getTag() != null) || this.messageCell == messageCell) {
            return false;
        }
        final Runnable hideRunnable = this.hideRunnable;
        if (hideRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(hideRunnable);
            this.hideRunnable = null;
        }
        final int top = messageCell.getTop();
        final View view = (View)messageCell.getParent();
        int n;
        int n2;
        if (this.currentType == 0) {
            final ImageReceiver photoImage = messageCell.getPhotoImage();
            n = top + photoImage.getImageY();
            final int imageHeight = photoImage.getImageHeight();
            final int measuredHeight = view.getMeasuredHeight();
            if (n <= this.getMeasuredHeight() + AndroidUtilities.dp(10.0f) || n + imageHeight > measuredHeight + imageHeight / 4) {
                return false;
            }
            n2 = messageCell.getNoSoundIconCenterX();
        }
        else {
            final MessageObject messageObject = messageCell.getMessageObject();
            final String overrideText = this.overrideText;
            if (overrideText == null) {
                this.textView.setText((CharSequence)LocaleController.getString("HidAccount", 2131559635));
            }
            else {
                this.textView.setText((CharSequence)overrideText);
            }
            this.measure(View$MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE));
            final int n3 = n = top + AndroidUtilities.dp(22.0f);
            if (!messageObject.isOutOwner()) {
                n = n3;
                if (messageCell.isDrawNameLayout()) {
                    n = n3 + AndroidUtilities.dp(20.0f);
                }
            }
            if (!this.isTopArrow && n <= this.getMeasuredHeight() + AndroidUtilities.dp(10.0f)) {
                return false;
            }
            n2 = messageCell.getForwardNameCenterX();
        }
        final int measuredWidth = view.getMeasuredWidth();
        if (this.isTopArrow) {
            this.setTranslationY((float)AndroidUtilities.dp(44.0f));
        }
        else {
            this.setTranslationY((float)(n - this.getMeasuredHeight()));
        }
        final int n4 = messageCell.getLeft() + n2;
        int dp = AndroidUtilities.dp(19.0f);
        if (n4 > view.getMeasuredWidth() / 2) {
            final int n5 = measuredWidth - this.getMeasuredWidth() - AndroidUtilities.dp(38.0f);
            this.setTranslationX((float)n5);
            dp += n5;
        }
        else {
            this.setTranslationX(0.0f);
        }
        final float translationX = (float)(messageCell.getLeft() + n2 - dp - this.arrowImageView.getMeasuredWidth() / 2);
        this.arrowImageView.setTranslationX(translationX);
        if (n4 > view.getMeasuredWidth() / 2) {
            if (translationX < AndroidUtilities.dp(10.0f)) {
                final float n6 = translationX - AndroidUtilities.dp(10.0f);
                this.setTranslationX(this.getTranslationX() + n6);
                this.arrowImageView.setTranslationX(translationX - n6);
            }
        }
        else if (translationX > this.getMeasuredWidth() - AndroidUtilities.dp(24.0f)) {
            final float translationX2 = translationX - this.getMeasuredWidth() + AndroidUtilities.dp(24.0f);
            this.setTranslationX(translationX2);
            this.arrowImageView.setTranslationX(translationX - translationX2);
        }
        else if (translationX < AndroidUtilities.dp(10.0f)) {
            final float n7 = translationX - AndroidUtilities.dp(10.0f);
            this.setTranslationX(this.getTranslationX() + n7);
            this.arrowImageView.setTranslationX(translationX - n7);
        }
        this.messageCell = messageCell;
        final AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        this.setTag((Object)1);
        this.setVisibility(0);
        if (b) {
            (this.animatorSet = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "alpha", new float[] { 0.0f, 1.0f }) });
            this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    HintView.this.animatorSet = null;
                    final HintView this$0 = HintView.this;
                    final _$$Lambda$HintView$1$Oo_YArBkq6553J0682j2MQqGlbY $$Lambda$HintView$1$Oo_YArBkq6553J0682j2MQqGlbY = new _$$Lambda$HintView$1$Oo_YArBkq6553J0682j2MQqGlbY(this);
                    this$0.hideRunnable = $$Lambda$HintView$1$Oo_YArBkq6553J0682j2MQqGlbY;
                    long n;
                    if (HintView.this.currentType == 0) {
                        n = 10000L;
                    }
                    else {
                        n = 2000L;
                    }
                    AndroidUtilities.runOnUIThread($$Lambda$HintView$1$Oo_YArBkq6553J0682j2MQqGlbY, n);
                }
            });
            this.animatorSet.setDuration(300L);
            this.animatorSet.start();
        }
        else {
            this.setAlpha(1.0f);
        }
        return true;
    }
}
