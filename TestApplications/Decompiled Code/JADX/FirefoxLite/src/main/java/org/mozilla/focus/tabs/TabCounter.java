package org.mozilla.focus.tabs;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import java.text.NumberFormat;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.utils.DrawableUtils;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.nightmode.themed.ThemedImageView;
import org.mozilla.rocket.nightmode.themed.ThemedRelativeLayout;
import org.mozilla.rocket.nightmode.themed.ThemedTextView;

public class TabCounter extends ThemedRelativeLayout {
    private final AnimatorSet animationSet;
    private final ThemedImageView bar;
    private final ThemedImageView box;
    private int count;
    private float currentTextRatio;
    private ColorStateList menuIconColor;
    private final ThemedTextView text;

    public TabCounter(Context context) {
        this(context, null);
    }

    public TabCounter(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TabCounter(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        int color = context.getResources().getColor(C0769R.color.colorMenuIconForeground);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0427R.styleable.TabCounter, i, 0);
        this.menuIconColor = obtainStyledAttributes.getColorStateList(0);
        obtainStyledAttributes.recycle();
        LayoutInflater.from(context).inflate(C0769R.layout.button_tab_counter, this);
        this.box = (ThemedImageView) findViewById(C0427R.C0426id.counter_box);
        this.bar = (ThemedImageView) findViewById(C0427R.C0426id.counter_bar);
        this.text = (ThemedTextView) findViewById(C0427R.C0426id.counter_text);
        this.text.setText(":)");
        this.text.setPadding(0, 0, 0, (int) TypedValue.applyDimension(1, 1.0f, context.getResources().getDisplayMetrics()));
        if (this.menuIconColor.getDefaultColor() != color) {
            tintDrawables(this.menuIconColor);
        }
        this.animationSet = createAnimatorSet();
    }

    public CharSequence getText() {
        return this.text.getText();
    }

    public void setCountWithAnimation(int i) {
        if (this.count == 0) {
            setCount(i);
        } else if (this.count != i) {
            if (this.count <= 99 || i <= 99) {
                adjustTextSize(i);
                this.text.setPadding(0, 0, 0, 0);
                this.text.setText(formatForDisplay(i));
                this.count = i;
                if (this.animationSet.isRunning()) {
                    this.animationSet.cancel();
                }
                this.animationSet.start();
                return;
            }
            this.count = i;
        }
    }

    public void setCount(int i) {
        adjustTextSize(i);
        this.text.setPadding(0, 0, 0, 0);
        this.text.setText(formatForDisplay(i));
        this.count = i;
    }

    private void tintDrawables(ColorStateList colorStateList) {
        this.box.setImageDrawable(DrawableUtils.loadAndTintDrawable(getContext(), 2131230970, colorStateList.getDefaultColor()));
        this.box.setImageTintList(colorStateList);
        this.bar.setImageDrawable(DrawableUtils.loadAndTintDrawable(getContext(), 2131230969, colorStateList.getDefaultColor()));
        this.bar.setImageTintList(colorStateList);
        this.text.setTextColor(colorStateList);
    }

    private AnimatorSet createAnimatorSet() {
        AnimatorSet animatorSet = new AnimatorSet();
        createBoxAnimatorSet(animatorSet);
        createBarAnimatorSet(animatorSet);
        createTextAnimatorSet(animatorSet);
        return animatorSet;
    }

    private void createBoxAnimatorSet(AnimatorSet animatorSet) {
        AnimatorSet animatorSet2 = animatorSet;
        ObjectAnimator duration = ObjectAnimator.ofFloat(this.box, "alpha", new float[]{1.0f, 0.0f}).setDuration(33);
        ObjectAnimator duration2 = ObjectAnimator.ofFloat(this.box, "translationY", new float[]{0.0f, -5.3f}).setDuration(50);
        ObjectAnimator duration3 = ObjectAnimator.ofFloat(this.box, "translationY", new float[]{-5.3f, -1.0f}).setDuration(116);
        ObjectAnimator duration4 = ObjectAnimator.ofFloat(this.box, "alpha", new float[]{0.01f, 1.0f}).setDuration(66);
        ObjectAnimator duration5 = ObjectAnimator.ofFloat(this.box, "translationY", new float[]{-1.0f, 2.7f}).setDuration(116);
        ObjectAnimator duration6 = ObjectAnimator.ofFloat(this.box, "translationY", new float[]{2.7f, 0.0f}).setDuration(133);
        ObjectAnimator duration7 = ObjectAnimator.ofFloat(this.box, "scaleY", new float[]{0.02f, 1.05f}).setDuration(100);
        duration7.setStartDelay(16);
        ObjectAnimator duration8 = ObjectAnimator.ofFloat(this.box, "scaleY", new float[]{1.05f, 0.99f}).setDuration(116);
        ObjectAnimator duration9 = ObjectAnimator.ofFloat(this.box, "scaleY", new float[]{0.99f, 1.0f}).setDuration(133);
        animatorSet2.play(duration).with(duration2);
        animatorSet2.play(duration2).before(duration3);
        animatorSet2.play(duration3).with(duration4);
        animatorSet2.play(duration3).before(duration5);
        animatorSet2.play(duration5).before(duration6);
        animatorSet2.play(duration2).before(duration7);
        animatorSet2.play(duration7).before(duration8);
        animatorSet2.play(duration8).before(duration9);
    }

    private void createBarAnimatorSet(AnimatorSet animatorSet) {
        Animator animator = (Animator) animatorSet.getChildAnimations().get(0);
        ObjectAnimator duration = ObjectAnimator.ofFloat(this.bar, "translationY", new float[]{0.0f, -7.0f}).setDuration(100);
        ObjectAnimator duration2 = ObjectAnimator.ofFloat(this.bar, "alpha", new float[]{1.0f, 0.0f}).setDuration(66);
        duration2.setStartDelay(48);
        ObjectAnimator duration3 = ObjectAnimator.ofFloat(this.bar, "translationY", new float[]{-7.0f, 0.0f}).setDuration(16);
        ObjectAnimator duration4 = ObjectAnimator.ofFloat(this.bar, "scaleX", new float[]{0.31f, 1.0f}).setDuration(166);
        duration4.setStartDelay(176);
        ObjectAnimator duration5 = ObjectAnimator.ofFloat(this.bar, "alpha", new float[]{0.0f, 1.0f}).setDuration(166);
        duration5.setStartDelay(176);
        animatorSet.play(animator).with(duration);
        animatorSet.play(animator).before(duration2);
        animatorSet.play(duration2).before(duration3);
        animatorSet.play(duration3).before(duration4);
        animatorSet.play(duration4).with(duration5);
    }

    private void createTextAnimatorSet(AnimatorSet animatorSet) {
        Animator animator = (Animator) animatorSet.getChildAnimations().get(0);
        ObjectAnimator duration = ObjectAnimator.ofFloat(this.text, "alpha", new float[]{1.0f, 0.0f}).setDuration(33);
        ObjectAnimator duration2 = ObjectAnimator.ofFloat(this.text, "alpha", new float[]{0.0f, 1.0f}).setDuration(66);
        duration2.setStartDelay(96);
        ObjectAnimator duration3 = ObjectAnimator.ofFloat(this.text, "translationY", new float[]{0.0f, 4.4f}).setDuration(66);
        duration3.setStartDelay(96);
        ObjectAnimator duration4 = ObjectAnimator.ofFloat(this.text, "translationY", new float[]{4.4f, 0.0f}).setDuration(66);
        animatorSet.play(animator).with(duration);
        animatorSet.play(duration).before(duration2);
        animatorSet.play(duration2).with(duration3);
        animatorSet.play(duration3).before(duration4);
    }

    private String formatForDisplay(int i) {
        return i > 99 ? "∞" : NumberFormat.getInstance().format((long) i);
    }

    private void adjustTextSize(int i) {
        final float f = (i > 99 || i < 10) ? 0.6f : 0.5f;
        if (f != this.currentTextRatio) {
            this.currentTextRatio = f;
            this.text.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    TabCounter.this.text.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = (int) (((float) TabCounter.this.box.getWidth()) * f);
                    if (width > 0) {
                        TabCounter.this.text.setTextSize(0, (float) width);
                    }
                }
            });
        }
    }

    public void setNightMode(boolean z) {
        super.setNightMode(z);
        tintDrawables(this.menuIconColor);
        this.bar.setNightMode(z);
        this.box.setNightMode(z);
        this.text.setNightMode(z);
    }
}
