// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Typeface;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.text.Layout$Alignment;
import java.util.Locale;
import java.util.Collection;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.content.Context;
import android.text.TextPaint;
import android.text.StaticLayout;
import java.util.ArrayList;
import android.animation.ObjectAnimator;
import android.view.View;

public class NumberTextView extends View
{
    private ObjectAnimator animator;
    private int currentNumber;
    private ArrayList<StaticLayout> letters;
    private ArrayList<StaticLayout> oldLetters;
    private float progress;
    private TextPaint textPaint;
    
    public NumberTextView(final Context context) {
        super(context);
        this.letters = new ArrayList<StaticLayout>();
        this.oldLetters = new ArrayList<StaticLayout>();
        this.textPaint = new TextPaint(1);
        this.progress = 0.0f;
        this.currentNumber = 1;
    }
    
    public float getProgress() {
        return this.progress;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.letters.isEmpty()) {
            return;
        }
        final float n = (float)this.letters.get(0).getHeight();
        canvas.save();
        canvas.translate((float)this.getPaddingLeft(), (this.getMeasuredHeight() - n) / 2.0f);
        for (int max = Math.max(this.letters.size(), this.oldLetters.size()), i = 0; i < max; ++i) {
            canvas.save();
            final int size = this.oldLetters.size();
            StaticLayout staticLayout = null;
            StaticLayout staticLayout2;
            if (i < size) {
                staticLayout2 = this.oldLetters.get(i);
            }
            else {
                staticLayout2 = null;
            }
            if (i < this.letters.size()) {
                staticLayout = this.letters.get(i);
            }
            final float progress = this.progress;
            if (progress > 0.0f) {
                if (staticLayout2 != null) {
                    this.textPaint.setAlpha((int)(progress * 255.0f));
                    canvas.save();
                    canvas.translate(0.0f, (this.progress - 1.0f) * n);
                    staticLayout2.draw(canvas);
                    canvas.restore();
                    if (staticLayout != null) {
                        this.textPaint.setAlpha((int)((1.0f - this.progress) * 255.0f));
                        canvas.translate(0.0f, this.progress * n);
                    }
                }
                else {
                    this.textPaint.setAlpha(255);
                }
            }
            else if (progress < 0.0f) {
                if (staticLayout2 != null) {
                    this.textPaint.setAlpha((int)(-progress * 255.0f));
                    canvas.save();
                    canvas.translate(0.0f, (this.progress + 1.0f) * n);
                    staticLayout2.draw(canvas);
                    canvas.restore();
                }
                if (staticLayout != null) {
                    if (i != max - 1 && staticLayout2 == null) {
                        this.textPaint.setAlpha(255);
                    }
                    else {
                        this.textPaint.setAlpha((int)((this.progress + 1.0f) * 255.0f));
                        canvas.translate(0.0f, this.progress * n);
                    }
                }
            }
            else if (staticLayout != null) {
                this.textPaint.setAlpha(255);
            }
            if (staticLayout != null) {
                staticLayout.draw(canvas);
            }
            canvas.restore();
            float lineWidth;
            if (staticLayout != null) {
                lineWidth = staticLayout.getLineWidth(0);
            }
            else {
                lineWidth = staticLayout2.getLineWidth(0) + AndroidUtilities.dp(1.0f);
            }
            canvas.translate(lineWidth, 0.0f);
        }
        canvas.restore();
    }
    
    public void setNumber(int i, final boolean b) {
        if (this.currentNumber == i && b) {
            return;
        }
        final ObjectAnimator animator = this.animator;
        if (animator != null) {
            animator.cancel();
            this.animator = null;
        }
        this.oldLetters.clear();
        this.oldLetters.addAll(this.letters);
        this.letters.clear();
        final String format = String.format(Locale.US, "%d", this.currentNumber);
        final String format2 = String.format(Locale.US, "%d", i);
        final boolean b2 = i > this.currentNumber;
        this.currentNumber = i;
        this.progress = 0.0f;
        int n;
        String substring;
        String substring2;
        TextPaint textPaint;
        for (i = 0; i < format2.length(); i = n) {
            n = i + 1;
            substring = format2.substring(i, n);
            if (!this.oldLetters.isEmpty() && i < format.length()) {
                substring2 = format.substring(i, n);
            }
            else {
                substring2 = null;
            }
            if (substring2 != null && substring2.equals(substring)) {
                this.letters.add(this.oldLetters.get(i));
                this.oldLetters.set(i, null);
            }
            else {
                textPaint = this.textPaint;
                this.letters.add(new StaticLayout((CharSequence)substring, textPaint, (int)Math.ceil(textPaint.measureText(substring)), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false));
            }
        }
        if (b && !this.oldLetters.isEmpty()) {
            float n2;
            if (b2) {
                n2 = -1.0f;
            }
            else {
                n2 = 1.0f;
            }
            (this.animator = ObjectAnimator.ofFloat((Object)this, "progress", new float[] { n2, 0.0f })).setDuration(150L);
            this.animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    NumberTextView.this.animator = null;
                    NumberTextView.this.oldLetters.clear();
                }
            });
            this.animator.start();
        }
        this.invalidate();
    }
    
    public void setProgress(final float progress) {
        if (this.progress == progress) {
            return;
        }
        this.progress = progress;
        this.invalidate();
    }
    
    public void setTextColor(final int color) {
        this.textPaint.setColor(color);
        this.invalidate();
    }
    
    public void setTextSize(final int n) {
        this.textPaint.setTextSize((float)AndroidUtilities.dp((float)n));
        this.oldLetters.clear();
        this.letters.clear();
        this.setNumber(this.currentNumber, false);
    }
    
    public void setTypeface(final Typeface typeface) {
        this.textPaint.setTypeface(typeface);
        this.oldLetters.clear();
        this.letters.clear();
        this.setNumber(this.currentNumber, false);
    }
}
