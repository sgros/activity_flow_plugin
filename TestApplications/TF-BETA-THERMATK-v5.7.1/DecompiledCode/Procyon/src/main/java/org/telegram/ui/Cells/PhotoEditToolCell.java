// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.AndroidUtilities;
import android.view.View$MeasureSpec;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.text.TextUtils$TruncateAt;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.content.Context;
import android.animation.AnimatorSet;
import org.telegram.ui.Components.PhotoEditorSeekBar;
import android.widget.TextView;
import android.widget.FrameLayout;

public class PhotoEditToolCell extends FrameLayout
{
    private Runnable hideValueRunnable;
    private TextView nameTextView;
    private PhotoEditorSeekBar seekBar;
    private AnimatorSet valueAnimation;
    private TextView valueTextView;
    
    public PhotoEditToolCell(final Context context) {
        super(context);
        this.hideValueRunnable = new Runnable() {
            @Override
            public void run() {
                PhotoEditToolCell.this.valueTextView.setTag((Object)null);
                PhotoEditToolCell.this.valueAnimation = new AnimatorSet();
                PhotoEditToolCell.this.valueAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)PhotoEditToolCell.this.valueTextView, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoEditToolCell.this.nameTextView, "alpha", new float[] { 1.0f }) });
                PhotoEditToolCell.this.valueAnimation.setDuration(180L);
                PhotoEditToolCell.this.valueAnimation.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                PhotoEditToolCell.this.valueAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        if (animator.equals(PhotoEditToolCell.this.valueAnimation)) {
                            PhotoEditToolCell.this.valueAnimation = null;
                        }
                    }
                });
                PhotoEditToolCell.this.valueAnimation.start();
            }
        };
        (this.nameTextView = new TextView(context)).setGravity(5);
        this.nameTextView.setTextColor(-1);
        this.nameTextView.setTextSize(1, 12.0f);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.addView((View)this.nameTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(80, -2.0f, 19, 0.0f, 0.0f, 0.0f, 0.0f));
        (this.valueTextView = new TextView(context)).setTextColor(-9649153);
        this.valueTextView.setTextSize(1, 12.0f);
        this.valueTextView.setGravity(5);
        this.valueTextView.setSingleLine(true);
        this.addView((View)this.valueTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(80, -2.0f, 19, 0.0f, 0.0f, 0.0f, 0.0f));
        this.addView((View)(this.seekBar = new PhotoEditorSeekBar(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 40.0f, 19, 96.0f, 0.0f, 24.0f, 0.0f));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0f), 1073741824));
    }
    
    public void setIconAndTextAndValue(final String s, final float n, final int n2, final int n3) {
        final AnimatorSet valueAnimation = this.valueAnimation;
        if (valueAnimation != null) {
            valueAnimation.cancel();
            this.valueAnimation = null;
        }
        AndroidUtilities.cancelRunOnUIThread(this.hideValueRunnable);
        this.valueTextView.setTag((Object)null);
        final TextView nameTextView = this.nameTextView;
        final StringBuilder sb = new StringBuilder();
        sb.append(s.substring(0, 1).toUpperCase());
        sb.append(s.substring(1).toLowerCase());
        nameTextView.setText((CharSequence)sb.toString());
        if (n > 0.0f) {
            final TextView valueTextView = this.valueTextView;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("+");
            sb2.append((int)n);
            valueTextView.setText((CharSequence)sb2.toString());
        }
        else {
            final TextView valueTextView2 = this.valueTextView;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            sb3.append((int)n);
            valueTextView2.setText((CharSequence)sb3.toString());
        }
        this.valueTextView.setAlpha(0.0f);
        this.nameTextView.setAlpha(1.0f);
        this.seekBar.setMinMax(n2, n3);
        this.seekBar.setProgress((int)n, false);
    }
    
    public void setSeekBarDelegate(final PhotoEditorSeekBar.PhotoEditorSeekBarDelegate photoEditorSeekBarDelegate) {
        this.seekBar.setDelegate((PhotoEditorSeekBar.PhotoEditorSeekBarDelegate)new PhotoEditorSeekBar.PhotoEditorSeekBarDelegate() {
            @Override
            public void onProgressChanged(final int n, final int n2) {
                photoEditorSeekBarDelegate.onProgressChanged(n, n2);
                if (n2 > 0) {
                    final TextView access$000 = PhotoEditToolCell.this.valueTextView;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("+");
                    sb.append(n2);
                    access$000.setText((CharSequence)sb.toString());
                }
                else {
                    final TextView access$2 = PhotoEditToolCell.this.valueTextView;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("");
                    sb2.append(n2);
                    access$2.setText((CharSequence)sb2.toString());
                }
                if (PhotoEditToolCell.this.valueTextView.getTag() == null) {
                    if (PhotoEditToolCell.this.valueAnimation != null) {
                        PhotoEditToolCell.this.valueAnimation.cancel();
                    }
                    PhotoEditToolCell.this.valueTextView.setTag((Object)1);
                    PhotoEditToolCell.this.valueAnimation = new AnimatorSet();
                    PhotoEditToolCell.this.valueAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)PhotoEditToolCell.this.valueTextView, "alpha", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoEditToolCell.this.nameTextView, "alpha", new float[] { 0.0f }) });
                    PhotoEditToolCell.this.valueAnimation.setDuration(180L);
                    PhotoEditToolCell.this.valueAnimation.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                    PhotoEditToolCell.this.valueAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            AndroidUtilities.runOnUIThread(PhotoEditToolCell.this.hideValueRunnable, 1000L);
                        }
                    });
                    PhotoEditToolCell.this.valueAnimation.start();
                }
                else {
                    AndroidUtilities.cancelRunOnUIThread(PhotoEditToolCell.this.hideValueRunnable);
                    AndroidUtilities.runOnUIThread(PhotoEditToolCell.this.hideValueRunnable, 1000L);
                }
            }
        });
    }
    
    public void setTag(final Object o) {
        super.setTag(o);
        this.seekBar.setTag(o);
    }
}
