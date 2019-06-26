// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import org.telegram.ui.PhotoViewer;
import android.view.View$OnClickListener;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction;
import android.os.Build$VERSION;
import org.telegram.messenger.LocaleController;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.content.Context;
import android.widget.TextView;
import org.telegram.messenger.MediaController;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox;
import android.animation.AnimatorSet;
import android.graphics.Rect;
import android.widget.FrameLayout;

public class PhotoAttachPhotoCell extends FrameLayout
{
    private static Rect rect;
    private AnimatorSet animatorSet;
    private CheckBox checkBox;
    private FrameLayout checkFrame;
    private PhotoAttachPhotoCellDelegate delegate;
    private BackupImageView imageView;
    private boolean isLast;
    private boolean isVertical;
    private boolean needCheckShow;
    private MediaController.PhotoEntry photoEntry;
    private boolean pressed;
    private FrameLayout videoInfoContainer;
    private TextView videoTextView;
    
    static {
        PhotoAttachPhotoCell.rect = new Rect();
    }
    
    public PhotoAttachPhotoCell(final Context context) {
        super(context);
        this.addView((View)(this.imageView = new BackupImageView(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(80, 80.0f));
        this.addView((View)(this.checkFrame = new FrameLayout(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(42, 42.0f, 51, 38.0f, 0.0f, 0.0f, 0.0f));
        (this.videoInfoContainer = new FrameLayout(context)).setBackgroundResource(2131165760);
        this.videoInfoContainer.setPadding(AndroidUtilities.dp(3.0f), 0, AndroidUtilities.dp(3.0f), 0);
        this.addView((View)this.videoInfoContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(80, 16, 83));
        final ImageView imageView = new ImageView(context);
        imageView.setImageResource(2131165475);
        this.videoInfoContainer.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 19));
        (this.videoTextView = new TextView(context)).setTextColor(-1);
        this.videoTextView.setTextSize(1, 12.0f);
        this.videoTextView.setImportantForAccessibility(2);
        this.videoInfoContainer.addView((View)this.videoTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 19, 18.0f, -0.7f, 0.0f, 0.0f));
        (this.checkBox = new CheckBox(context, 2131165355)).setSize(30);
        this.checkBox.setCheckOffset(AndroidUtilities.dp(1.0f));
        this.checkBox.setDrawBackground(true);
        this.checkBox.setColor(-12793105, -1);
        this.addView((View)this.checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(30, 30.0f, 51, 46.0f, 4.0f, 0.0f, 0.0f));
        this.checkBox.setVisibility(0);
        this.setFocusable(true);
    }
    
    public void callDelegate() {
        this.delegate.onCheckClick(this);
    }
    
    public CheckBox getCheckBox() {
        return this.checkBox;
    }
    
    public FrameLayout getCheckFrame() {
        return this.checkFrame;
    }
    
    public BackupImageView getImageView() {
        return this.imageView;
    }
    
    public MediaController.PhotoEntry getPhotoEntry() {
        return this.photoEntry;
    }
    
    public View getVideoInfoContainer() {
        return (View)this.videoInfoContainer;
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setEnabled(true);
        if (this.photoEntry.isVideo) {
            final StringBuilder sb = new StringBuilder();
            sb.append(LocaleController.getString("AttachVideo", 2131558733));
            sb.append(", ");
            sb.append(LocaleController.formatCallDuration(this.photoEntry.duration));
            accessibilityNodeInfo.setText((CharSequence)sb.toString());
        }
        else {
            accessibilityNodeInfo.setText((CharSequence)LocaleController.getString("AttachPhoto", 2131558727));
        }
        if (this.checkBox.isChecked()) {
            accessibilityNodeInfo.setSelected(true);
        }
        if (Build$VERSION.SDK_INT >= 21) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo$AccessibilityAction(2131230727, (CharSequence)LocaleController.getString("Open", 2131560110)));
        }
    }
    
    protected void onMeasure(int n, int measureSpec) {
        final boolean isVertical = this.isVertical;
        measureSpec = 0;
        n = 0;
        if (isVertical) {
            measureSpec = View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0f), 1073741824);
            if (!this.isLast) {
                n = 6;
            }
            super.onMeasure(measureSpec, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)(n + 80)), 1073741824));
        }
        else {
            if (this.isLast) {
                n = measureSpec;
            }
            else {
                n = 6;
            }
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)(n + 80)), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0f), 1073741824));
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.checkFrame.getHitRect(PhotoAttachPhotoCell.rect);
        final int action = motionEvent.getAction();
        int n = 1;
        Label_0167: {
            if (action == 0) {
                if (PhotoAttachPhotoCell.rect.contains((int)motionEvent.getX(), (int)motionEvent.getY())) {
                    this.pressed = true;
                    this.invalidate();
                    break Label_0167;
                }
            }
            else if (this.pressed) {
                if (motionEvent.getAction() == 1) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                    this.pressed = false;
                    this.playSoundEffect(0);
                    this.sendAccessibilityEvent(1);
                    this.delegate.onCheckClick(this);
                    this.invalidate();
                }
                else if (motionEvent.getAction() == 3) {
                    this.pressed = false;
                    this.invalidate();
                }
                else if (motionEvent.getAction() == 2 && !PhotoAttachPhotoCell.rect.contains((int)motionEvent.getX(), (int)motionEvent.getY())) {
                    this.pressed = false;
                    this.invalidate();
                }
            }
            n = 0;
        }
        boolean onTouchEvent = n != 0;
        if (n == 0) {
            onTouchEvent = super.onTouchEvent(motionEvent);
        }
        return onTouchEvent;
    }
    
    public boolean performAccessibilityAction(final int n, final Bundle bundle) {
        if (n == 2131230727) {
            final View view = (View)this.getParent();
            view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, (float)this.getLeft(), (float)(this.getTop() + this.getHeight() - 1), 0));
            view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, (float)this.getLeft(), (float)(this.getTop() + this.getHeight() - 1), 0));
        }
        return super.performAccessibilityAction(n, bundle);
    }
    
    public void setChecked(final int n, final boolean b, final boolean b2) {
        this.checkBox.setChecked(n, b, b2);
    }
    
    public void setDelegate(final PhotoAttachPhotoCellDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setIsVertical(final boolean isVertical) {
        this.isVertical = isVertical;
    }
    
    public void setNum(final int num) {
        this.checkBox.setNum(num);
    }
    
    public void setOnCheckClickLisnener(final View$OnClickListener onClickListener) {
        this.checkFrame.setOnClickListener(onClickListener);
    }
    
    public void setPhotoEntry(MediaController.PhotoEntry photoEntry, final boolean b, final boolean isLast) {
        final int n = 0;
        this.pressed = false;
        this.photoEntry = photoEntry;
        this.isLast = isLast;
        if (this.photoEntry.isVideo) {
            this.imageView.setOrientation(0, true);
            this.videoInfoContainer.setVisibility(0);
            final int duration = this.photoEntry.duration;
            final int i = duration / 60;
            this.videoTextView.setText((CharSequence)String.format("%d:%02d", i, duration - i * 60));
        }
        else {
            this.videoInfoContainer.setVisibility(4);
        }
        photoEntry = this.photoEntry;
        final String thumbPath = photoEntry.thumbPath;
        if (thumbPath != null) {
            this.imageView.setImage(thumbPath, null, this.getResources().getDrawable(2131165697));
        }
        else if (photoEntry.path != null) {
            if (photoEntry.isVideo) {
                final BackupImageView imageView = this.imageView;
                final StringBuilder sb = new StringBuilder();
                sb.append("vthumb://");
                sb.append(this.photoEntry.imageId);
                sb.append(":");
                sb.append(this.photoEntry.path);
                imageView.setImage(sb.toString(), null, this.getResources().getDrawable(2131165697));
            }
            else {
                this.imageView.setOrientation(photoEntry.orientation, true);
                final BackupImageView imageView2 = this.imageView;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("thumb://");
                sb2.append(this.photoEntry.imageId);
                sb2.append(":");
                sb2.append(this.photoEntry.path);
                imageView2.setImage(sb2.toString(), null, this.getResources().getDrawable(2131165697));
            }
        }
        else {
            this.imageView.setImageResource(2131165697);
        }
        int n2 = n;
        if (b) {
            n2 = n;
            if (PhotoViewer.isShowingImage(this.photoEntry.path)) {
                n2 = 1;
            }
        }
        this.imageView.getImageReceiver().setVisible((boolean)((n2 ^ 0x1) != 0x0), true);
        final CheckBox checkBox = this.checkBox;
        final float n3 = 0.0f;
        float alpha;
        if (n2 != 0) {
            alpha = 0.0f;
        }
        else {
            alpha = 1.0f;
        }
        checkBox.setAlpha(alpha);
        final FrameLayout videoInfoContainer = this.videoInfoContainer;
        float alpha2;
        if (n2 != 0) {
            alpha2 = n3;
        }
        else {
            alpha2 = 1.0f;
        }
        videoInfoContainer.setAlpha(alpha2);
        this.requestLayout();
    }
    
    public void showCheck(final boolean b) {
        final float n = 1.0f;
        if ((b && this.checkBox.getAlpha() == 1.0f) || (!b && this.checkBox.getAlpha() == 0.0f)) {
            return;
        }
        final AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        (this.animatorSet = new AnimatorSet()).setInterpolator((TimeInterpolator)new DecelerateInterpolator());
        this.animatorSet.setDuration(180L);
        final AnimatorSet animatorSet2 = this.animatorSet;
        final FrameLayout videoInfoContainer = this.videoInfoContainer;
        float n2;
        if (b) {
            n2 = 1.0f;
        }
        else {
            n2 = 0.0f;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)videoInfoContainer, "alpha", new float[] { n2 });
        final CheckBox checkBox = this.checkBox;
        float n3;
        if (b) {
            n3 = n;
        }
        else {
            n3 = 0.0f;
        }
        animatorSet2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ObjectAnimator.ofFloat((Object)checkBox, "alpha", new float[] { n3 }) });
        this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (animator.equals(PhotoAttachPhotoCell.this.animatorSet)) {
                    PhotoAttachPhotoCell.this.animatorSet = null;
                }
            }
        });
        this.animatorSet.start();
    }
    
    public void showImage() {
        this.imageView.getImageReceiver().setVisible(true, true);
    }
    
    public interface PhotoAttachPhotoCellDelegate
    {
        void onCheckClick(final PhotoAttachPhotoCell p0);
    }
}
