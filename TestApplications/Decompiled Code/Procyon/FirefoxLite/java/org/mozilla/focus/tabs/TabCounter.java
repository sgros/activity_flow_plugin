// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.tabs;

import org.mozilla.focus.utils.DrawableUtils;
import java.text.NumberFormat;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import org.mozilla.focus.R;
import android.util.AttributeSet;
import android.content.Context;
import org.mozilla.rocket.nightmode.themed.ThemedTextView;
import android.content.res.ColorStateList;
import org.mozilla.rocket.nightmode.themed.ThemedImageView;
import android.animation.AnimatorSet;
import org.mozilla.rocket.nightmode.themed.ThemedRelativeLayout;

public class TabCounter extends ThemedRelativeLayout
{
    private final AnimatorSet animationSet;
    private final ThemedImageView bar;
    private final ThemedImageView box;
    private int count;
    private float currentTextRatio;
    private ColorStateList menuIconColor;
    private final ThemedTextView text;
    
    public TabCounter(final Context context) {
        this(context, null);
    }
    
    public TabCounter(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public TabCounter(final Context context, final AttributeSet set, int n) {
        super(context, set, n);
        final int color = context.getResources().getColor(2131099720);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.TabCounter, n, 0);
        this.menuIconColor = obtainStyledAttributes.getColorStateList(0);
        obtainStyledAttributes.recycle();
        LayoutInflater.from(context).inflate(2131492919, (ViewGroup)this);
        this.box = (ThemedImageView)this.findViewById(2131296384);
        this.bar = (ThemedImageView)this.findViewById(2131296383);
        (this.text = (ThemedTextView)this.findViewById(2131296386)).setText((CharSequence)":)");
        n = (int)TypedValue.applyDimension(1, 1.0f, context.getResources().getDisplayMetrics());
        this.text.setPadding(0, 0, 0, n);
        if (this.menuIconColor.getDefaultColor() != color) {
            this.tintDrawables(this.menuIconColor);
        }
        this.animationSet = this.createAnimatorSet();
    }
    
    private void adjustTextSize(final int n) {
        float currentTextRatio;
        if (n <= 99 && n >= 10) {
            currentTextRatio = 0.5f;
        }
        else {
            currentTextRatio = 0.6f;
        }
        if (currentTextRatio != this.currentTextRatio) {
            this.currentTextRatio = currentTextRatio;
            this.text.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    TabCounter.this.text.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                    final int n = (int)(TabCounter.this.box.getWidth() * currentTextRatio);
                    if (n > 0) {
                        TabCounter.this.text.setTextSize(0, (float)n);
                    }
                }
            });
        }
    }
    
    private AnimatorSet createAnimatorSet() {
        final AnimatorSet set = new AnimatorSet();
        this.createBoxAnimatorSet(set);
        this.createBarAnimatorSet(set);
        this.createTextAnimatorSet(set);
        return set;
    }
    
    private void createBarAnimatorSet(final AnimatorSet set) {
        final Animator animator = set.getChildAnimations().get(0);
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)this.bar, "translationY", new float[] { 0.0f, -7.0f }).setDuration(100L);
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)this.bar, "alpha", new float[] { 1.0f, 0.0f }).setDuration(66L);
        setDuration2.setStartDelay(48L);
        final ObjectAnimator setDuration3 = ObjectAnimator.ofFloat((Object)this.bar, "translationY", new float[] { -7.0f, 0.0f }).setDuration(16L);
        final ObjectAnimator setDuration4 = ObjectAnimator.ofFloat((Object)this.bar, "scaleX", new float[] { 0.31f, 1.0f }).setDuration(166L);
        setDuration4.setStartDelay(176L);
        final ObjectAnimator setDuration5 = ObjectAnimator.ofFloat((Object)this.bar, "alpha", new float[] { 0.0f, 1.0f }).setDuration(166L);
        setDuration5.setStartDelay(176L);
        set.play(animator).with((Animator)setDuration);
        set.play(animator).before((Animator)setDuration2);
        set.play((Animator)setDuration2).before((Animator)setDuration3);
        set.play((Animator)setDuration3).before((Animator)setDuration4);
        set.play((Animator)setDuration4).with((Animator)setDuration5);
    }
    
    private void createBoxAnimatorSet(final AnimatorSet set) {
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)this.box, "alpha", new float[] { 1.0f, 0.0f }).setDuration(33L);
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)this.box, "translationY", new float[] { 0.0f, -5.3f }).setDuration(50L);
        final ObjectAnimator setDuration3 = ObjectAnimator.ofFloat((Object)this.box, "translationY", new float[] { -5.3f, -1.0f }).setDuration(116L);
        final ObjectAnimator setDuration4 = ObjectAnimator.ofFloat((Object)this.box, "alpha", new float[] { 0.01f, 1.0f }).setDuration(66L);
        final ObjectAnimator setDuration5 = ObjectAnimator.ofFloat((Object)this.box, "translationY", new float[] { -1.0f, 2.7f }).setDuration(116L);
        final ObjectAnimator setDuration6 = ObjectAnimator.ofFloat((Object)this.box, "translationY", new float[] { 2.7f, 0.0f }).setDuration(133L);
        final ObjectAnimator setDuration7 = ObjectAnimator.ofFloat((Object)this.box, "scaleY", new float[] { 0.02f, 1.05f }).setDuration(100L);
        setDuration7.setStartDelay(16L);
        final ObjectAnimator setDuration8 = ObjectAnimator.ofFloat((Object)this.box, "scaleY", new float[] { 1.05f, 0.99f }).setDuration(116L);
        final ObjectAnimator setDuration9 = ObjectAnimator.ofFloat((Object)this.box, "scaleY", new float[] { 0.99f, 1.0f }).setDuration(133L);
        set.play((Animator)setDuration).with((Animator)setDuration2);
        set.play((Animator)setDuration2).before((Animator)setDuration3);
        set.play((Animator)setDuration3).with((Animator)setDuration4);
        set.play((Animator)setDuration3).before((Animator)setDuration5);
        set.play((Animator)setDuration5).before((Animator)setDuration6);
        set.play((Animator)setDuration2).before((Animator)setDuration7);
        set.play((Animator)setDuration7).before((Animator)setDuration8);
        set.play((Animator)setDuration8).before((Animator)setDuration9);
    }
    
    private void createTextAnimatorSet(final AnimatorSet set) {
        final Animator animator = set.getChildAnimations().get(0);
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)this.text, "alpha", new float[] { 1.0f, 0.0f }).setDuration(33L);
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)this.text, "alpha", new float[] { 0.0f, 1.0f }).setDuration(66L);
        setDuration2.setStartDelay(96L);
        final ObjectAnimator setDuration3 = ObjectAnimator.ofFloat((Object)this.text, "translationY", new float[] { 0.0f, 4.4f }).setDuration(66L);
        setDuration3.setStartDelay(96L);
        final ObjectAnimator setDuration4 = ObjectAnimator.ofFloat((Object)this.text, "translationY", new float[] { 4.4f, 0.0f }).setDuration(66L);
        set.play(animator).with((Animator)setDuration);
        set.play((Animator)setDuration).before((Animator)setDuration2);
        set.play((Animator)setDuration2).with((Animator)setDuration3);
        set.play((Animator)setDuration3).before((Animator)setDuration4);
    }
    
    private String formatForDisplay(final int n) {
        if (n > 99) {
            return "\u221e";
        }
        return NumberFormat.getInstance().format(n);
    }
    
    private void tintDrawables(final ColorStateList textColor) {
        this.box.setImageDrawable(DrawableUtils.loadAndTintDrawable(this.getContext(), 2131230970, textColor.getDefaultColor()));
        this.box.setImageTintList(textColor);
        this.bar.setImageDrawable(DrawableUtils.loadAndTintDrawable(this.getContext(), 2131230969, textColor.getDefaultColor()));
        this.bar.setImageTintList(textColor);
        this.text.setTextColor(textColor);
    }
    
    public CharSequence getText() {
        return this.text.getText();
    }
    
    public void setCount(final int count) {
        this.adjustTextSize(count);
        this.text.setPadding(0, 0, 0, 0);
        this.text.setText((CharSequence)this.formatForDisplay(count));
        this.count = count;
    }
    
    public void setCountWithAnimation(final int count) {
        if (this.count == 0) {
            this.setCount(count);
            return;
        }
        if (this.count == count) {
            return;
        }
        if (this.count > 99 && count > 99) {
            this.count = count;
            return;
        }
        this.adjustTextSize(count);
        this.text.setPadding(0, 0, 0, 0);
        this.text.setText((CharSequence)this.formatForDisplay(count));
        this.count = count;
        if (this.animationSet.isRunning()) {
            this.animationSet.cancel();
        }
        this.animationSet.start();
    }
    
    @Override
    public void setNightMode(final boolean b) {
        super.setNightMode(b);
        this.tintDrawables(this.menuIconColor);
        this.bar.setNightMode(b);
        this.box.setNightMode(b);
        this.text.setNightMode(b);
    }
}
