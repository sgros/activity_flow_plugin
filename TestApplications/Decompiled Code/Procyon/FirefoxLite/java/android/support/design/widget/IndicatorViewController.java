// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.content.res.ColorStateList;
import android.widget.LinearLayout$LayoutParams;
import android.support.v4.widget.Space;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.support.design.animation.AnimatorSetCompat;
import java.util.ArrayList;
import android.animation.AnimatorSet;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;
import android.text.TextUtils;
import android.support.design.animation.AnimationUtils;
import android.view.View;
import android.animation.ObjectAnimator;
import java.util.List;
import android.support.design.R;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import android.widget.FrameLayout;
import android.animation.Animator;

final class IndicatorViewController
{
    private Animator captionAnimator;
    private FrameLayout captionArea;
    private int captionDisplayed;
    private int captionToShow;
    private final float captionTranslationYPx;
    private int captionViewsAdded;
    private final Context context;
    private boolean errorEnabled;
    private CharSequence errorText;
    private int errorTextAppearance;
    private TextView errorView;
    private CharSequence helperText;
    private boolean helperTextEnabled;
    private int helperTextTextAppearance;
    private TextView helperTextView;
    private LinearLayout indicatorArea;
    private int indicatorsAdded;
    private final TextInputLayout textInputView;
    private Typeface typeface;
    
    public IndicatorViewController(final TextInputLayout textInputView) {
        this.context = textInputView.getContext();
        this.textInputView = textInputView;
        this.captionTranslationYPx = (float)this.context.getResources().getDimensionPixelSize(R.dimen.design_textinput_caption_translate_y);
    }
    
    private boolean canAdjustIndicatorPadding() {
        return this.indicatorArea != null && this.textInputView.getEditText() != null;
    }
    
    private void createCaptionAnimators(final List<Animator> list, final boolean b, final TextView textView, final int n, final int n2, final int n3) {
        if (textView != null && b) {
            if (n == n3 || n == n2) {
                list.add((Animator)this.createCaptionOpacityAnimator(textView, n3 == n));
                if (n3 == n) {
                    list.add((Animator)this.createCaptionTranslationYAnimator(textView));
                }
            }
        }
    }
    
    private ObjectAnimator createCaptionOpacityAnimator(final TextView textView, final boolean b) {
        float n;
        if (b) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)textView, View.ALPHA, new float[] { n });
        ofFloat.setDuration(167L);
        ofFloat.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        return ofFloat;
    }
    
    private ObjectAnimator createCaptionTranslationYAnimator(final TextView textView) {
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)textView, View.TRANSLATION_Y, new float[] { -this.captionTranslationYPx, 0.0f });
        ofFloat.setDuration(217L);
        ofFloat.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
        return ofFloat;
    }
    
    private TextView getCaptionViewFromDisplayState(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case 2: {
                return this.helperTextView;
            }
            case 1: {
                return this.errorView;
            }
        }
    }
    
    private boolean isCaptionStateError(final int n) {
        boolean b = true;
        if (n != 1 || this.errorView == null || TextUtils.isEmpty(this.errorText)) {
            b = false;
        }
        return b;
    }
    
    private void setCaptionViewVisibilities(final int n, final int captionDisplayed) {
        if (n == captionDisplayed) {
            return;
        }
        if (captionDisplayed != 0) {
            final TextView captionViewFromDisplayState = this.getCaptionViewFromDisplayState(captionDisplayed);
            if (captionViewFromDisplayState != null) {
                captionViewFromDisplayState.setVisibility(0);
                captionViewFromDisplayState.setAlpha(1.0f);
            }
        }
        if (n != 0) {
            final TextView captionViewFromDisplayState2 = this.getCaptionViewFromDisplayState(n);
            if (captionViewFromDisplayState2 != null) {
                captionViewFromDisplayState2.setVisibility(4);
                if (n == 1) {
                    captionViewFromDisplayState2.setText((CharSequence)null);
                }
            }
        }
        this.captionDisplayed = captionDisplayed;
    }
    
    private void setTextViewTypeface(final TextView textView, final Typeface typeface) {
        if (textView != null) {
            textView.setTypeface(typeface);
        }
    }
    
    private void setViewGroupGoneIfEmpty(final ViewGroup viewGroup, final int n) {
        if (n == 0) {
            viewGroup.setVisibility(8);
        }
    }
    
    private boolean shouldAnimateCaptionView(final TextView textView, final CharSequence charSequence) {
        return ViewCompat.isLaidOut((View)this.textInputView) && this.textInputView.isEnabled() && (this.captionToShow != this.captionDisplayed || textView == null || !TextUtils.equals(textView.getText(), charSequence));
    }
    
    private void updateCaptionViewsVisibility(final int n, final int n2, final boolean b) {
        if (b) {
            final AnimatorSet captionAnimator = new AnimatorSet();
            this.captionAnimator = (Animator)captionAnimator;
            final ArrayList<Animator> list = new ArrayList<Animator>();
            this.createCaptionAnimators(list, this.helperTextEnabled, this.helperTextView, 2, n, n2);
            this.createCaptionAnimators(list, this.errorEnabled, this.errorView, 1, n, n2);
            AnimatorSetCompat.playTogether(captionAnimator, list);
            captionAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                final /* synthetic */ TextView val$captionViewToHide = IndicatorViewController.this.getCaptionViewFromDisplayState(n);
                final /* synthetic */ TextView val$captionViewToShow = IndicatorViewController.this.getCaptionViewFromDisplayState(n2);
                
                public void onAnimationEnd(final Animator animator) {
                    IndicatorViewController.this.captionDisplayed = n2;
                    IndicatorViewController.this.captionAnimator = null;
                    if (this.val$captionViewToHide != null) {
                        this.val$captionViewToHide.setVisibility(4);
                        if (n == 1 && IndicatorViewController.this.errorView != null) {
                            IndicatorViewController.this.errorView.setText((CharSequence)null);
                        }
                    }
                }
                
                public void onAnimationStart(final Animator animator) {
                    if (this.val$captionViewToShow != null) {
                        this.val$captionViewToShow.setVisibility(0);
                    }
                }
            });
            captionAnimator.start();
        }
        else {
            this.setCaptionViewVisibilities(n, n2);
        }
        this.textInputView.updateEditTextBackground();
        this.textInputView.updateLabelState(b);
        this.textInputView.updateTextInputBoxState();
    }
    
    void addIndicator(final TextView textView, final int n) {
        if (this.indicatorArea == null && this.captionArea == null) {
            (this.indicatorArea = new LinearLayout(this.context)).setOrientation(0);
            this.textInputView.addView((View)this.indicatorArea, -1, -2);
            this.captionArea = new FrameLayout(this.context);
            this.indicatorArea.addView((View)this.captionArea, -1, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-2, -2));
            this.indicatorArea.addView((View)new Space(this.context), (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(0, 0, 1.0f));
            if (this.textInputView.getEditText() != null) {
                this.adjustIndicatorPadding();
            }
        }
        if (this.isCaptionView(n)) {
            this.captionArea.setVisibility(0);
            this.captionArea.addView((View)textView);
            ++this.captionViewsAdded;
        }
        else {
            this.indicatorArea.addView((View)textView, n);
        }
        this.indicatorArea.setVisibility(0);
        ++this.indicatorsAdded;
    }
    
    void adjustIndicatorPadding() {
        if (this.canAdjustIndicatorPadding()) {
            ViewCompat.setPaddingRelative((View)this.indicatorArea, ViewCompat.getPaddingStart((View)this.textInputView.getEditText()), 0, ViewCompat.getPaddingEnd((View)this.textInputView.getEditText()), 0);
        }
    }
    
    void cancelCaptionAnimator() {
        if (this.captionAnimator != null) {
            this.captionAnimator.cancel();
        }
    }
    
    boolean errorShouldBeShown() {
        return this.isCaptionStateError(this.captionToShow);
    }
    
    CharSequence getErrorText() {
        return this.errorText;
    }
    
    int getErrorViewCurrentTextColor() {
        int currentTextColor;
        if (this.errorView != null) {
            currentTextColor = this.errorView.getCurrentTextColor();
        }
        else {
            currentTextColor = -1;
        }
        return currentTextColor;
    }
    
    ColorStateList getErrorViewTextColors() {
        ColorStateList textColors;
        if (this.errorView != null) {
            textColors = this.errorView.getTextColors();
        }
        else {
            textColors = null;
        }
        return textColors;
    }
    
    CharSequence getHelperText() {
        return this.helperText;
    }
    
    int getHelperTextViewCurrentTextColor() {
        int currentTextColor;
        if (this.helperTextView != null) {
            currentTextColor = this.helperTextView.getCurrentTextColor();
        }
        else {
            currentTextColor = -1;
        }
        return currentTextColor;
    }
    
    void hideError() {
        this.errorText = null;
        this.cancelCaptionAnimator();
        if (this.captionDisplayed == 1) {
            if (this.helperTextEnabled && !TextUtils.isEmpty(this.helperText)) {
                this.captionToShow = 2;
            }
            else {
                this.captionToShow = 0;
            }
        }
        this.updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, this.shouldAnimateCaptionView(this.errorView, null));
    }
    
    void hideHelperText() {
        this.cancelCaptionAnimator();
        if (this.captionDisplayed == 2) {
            this.captionToShow = 0;
        }
        this.updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, this.shouldAnimateCaptionView(this.helperTextView, null));
    }
    
    boolean isCaptionView(final int n) {
        boolean b = true;
        if (n != 0) {
            b = (n == 1 && b);
        }
        return b;
    }
    
    boolean isErrorEnabled() {
        return this.errorEnabled;
    }
    
    boolean isHelperTextEnabled() {
        return this.helperTextEnabled;
    }
    
    void removeIndicator(final TextView textView, final int n) {
        if (this.indicatorArea == null) {
            return;
        }
        if (this.isCaptionView(n) && this.captionArea != null) {
            --this.captionViewsAdded;
            this.setViewGroupGoneIfEmpty((ViewGroup)this.captionArea, this.captionViewsAdded);
            this.captionArea.removeView((View)textView);
        }
        else {
            this.indicatorArea.removeView((View)textView);
        }
        --this.indicatorsAdded;
        this.setViewGroupGoneIfEmpty((ViewGroup)this.indicatorArea, this.indicatorsAdded);
    }
    
    void setErrorEnabled(final boolean errorEnabled) {
        if (this.errorEnabled == errorEnabled) {
            return;
        }
        this.cancelCaptionAnimator();
        if (errorEnabled) {
            (this.errorView = new AppCompatTextView(this.context)).setId(R.id.textinput_error);
            if (this.typeface != null) {
                this.errorView.setTypeface(this.typeface);
            }
            this.setErrorTextAppearance(this.errorTextAppearance);
            this.errorView.setVisibility(4);
            ViewCompat.setAccessibilityLiveRegion((View)this.errorView, 1);
            this.addIndicator(this.errorView, 0);
        }
        else {
            this.hideError();
            this.removeIndicator(this.errorView, 0);
            this.errorView = null;
            this.textInputView.updateEditTextBackground();
            this.textInputView.updateTextInputBoxState();
        }
        this.errorEnabled = errorEnabled;
    }
    
    void setErrorTextAppearance(final int errorTextAppearance) {
        this.errorTextAppearance = errorTextAppearance;
        if (this.errorView != null) {
            this.textInputView.setTextAppearanceCompatWithErrorFallback(this.errorView, errorTextAppearance);
        }
    }
    
    void setErrorViewTextColor(final ColorStateList textColor) {
        if (this.errorView != null) {
            this.errorView.setTextColor(textColor);
        }
    }
    
    void setHelperTextAppearance(final int helperTextTextAppearance) {
        this.helperTextTextAppearance = helperTextTextAppearance;
        if (this.helperTextView != null) {
            TextViewCompat.setTextAppearance(this.helperTextView, helperTextTextAppearance);
        }
    }
    
    void setHelperTextEnabled(final boolean helperTextEnabled) {
        if (this.helperTextEnabled == helperTextEnabled) {
            return;
        }
        this.cancelCaptionAnimator();
        if (helperTextEnabled) {
            (this.helperTextView = new AppCompatTextView(this.context)).setId(R.id.textinput_helper_text);
            if (this.typeface != null) {
                this.helperTextView.setTypeface(this.typeface);
            }
            this.helperTextView.setVisibility(4);
            ViewCompat.setAccessibilityLiveRegion((View)this.helperTextView, 1);
            this.setHelperTextAppearance(this.helperTextTextAppearance);
            this.addIndicator(this.helperTextView, 1);
        }
        else {
            this.hideHelperText();
            this.removeIndicator(this.helperTextView, 1);
            this.helperTextView = null;
            this.textInputView.updateEditTextBackground();
            this.textInputView.updateTextInputBoxState();
        }
        this.helperTextEnabled = helperTextEnabled;
    }
    
    void setHelperTextViewTextColor(final ColorStateList textColor) {
        if (this.helperTextView != null) {
            this.helperTextView.setTextColor(textColor);
        }
    }
    
    void setTypefaces(final Typeface typeface) {
        if (typeface != this.typeface) {
            this.typeface = typeface;
            this.setTextViewTypeface(this.errorView, typeface);
            this.setTextViewTypeface(this.helperTextView, typeface);
        }
    }
    
    void showError(final CharSequence charSequence) {
        this.cancelCaptionAnimator();
        this.errorText = charSequence;
        this.errorView.setText(charSequence);
        if (this.captionDisplayed != 1) {
            this.captionToShow = 1;
        }
        this.updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, this.shouldAnimateCaptionView(this.errorView, charSequence));
    }
    
    void showHelper(final CharSequence charSequence) {
        this.cancelCaptionAnimator();
        this.helperText = charSequence;
        this.helperTextView.setText(charSequence);
        if (this.captionDisplayed != 2) {
            this.captionToShow = 2;
        }
        this.updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, this.shouldAnimateCaptionView(this.helperTextView, charSequence));
    }
}
