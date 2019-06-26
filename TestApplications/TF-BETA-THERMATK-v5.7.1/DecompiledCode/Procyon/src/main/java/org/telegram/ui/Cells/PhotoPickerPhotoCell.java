// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import org.telegram.tgnet.TLRPC;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.MediaController;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View$MeasureSpec;
import android.widget.ImageView;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.content.Context;
import android.widget.TextView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox;
import android.animation.AnimatorSet;
import android.widget.FrameLayout;

public class PhotoPickerPhotoCell extends FrameLayout
{
    private AnimatorSet animator;
    private AnimatorSet animatorSet;
    public CheckBox checkBox;
    public FrameLayout checkFrame;
    public int itemWidth;
    public BackupImageView photoImage;
    public FrameLayout videoInfoContainer;
    public TextView videoTextView;
    private boolean zoomOnSelect;
    
    public PhotoPickerPhotoCell(final Context context, final boolean zoomOnSelect) {
        super(context);
        this.zoomOnSelect = zoomOnSelect;
        this.addView((View)(this.photoImage = new BackupImageView(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.addView((View)(this.checkFrame = new FrameLayout(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(42, 42, 53));
        (this.videoInfoContainer = new FrameLayout(context)).setBackgroundResource(2131165760);
        this.videoInfoContainer.setPadding(AndroidUtilities.dp(3.0f), 0, AndroidUtilities.dp(3.0f), 0);
        this.addView((View)this.videoInfoContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 16, 83));
        final ImageView imageView = new ImageView(context);
        imageView.setImageResource(2131165475);
        this.videoInfoContainer.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 19));
        (this.videoTextView = new TextView(context)).setTextColor(-1);
        this.videoTextView.setTextSize(1, 12.0f);
        this.videoTextView.setImportantForAccessibility(2);
        this.videoInfoContainer.addView((View)this.videoTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 19, 18.0f, -0.7f, 0.0f, 0.0f));
        this.checkBox = new CheckBox(context, 2131165355);
        final CheckBox checkBox = this.checkBox;
        int size;
        if (zoomOnSelect) {
            size = 30;
        }
        else {
            size = 26;
        }
        checkBox.setSize(size);
        this.checkBox.setCheckOffset(AndroidUtilities.dp(1.0f));
        this.checkBox.setDrawBackground(true);
        this.checkBox.setColor(-10043398, -1);
        final CheckBox checkBox2 = this.checkBox;
        int n;
        if (zoomOnSelect) {
            n = 30;
        }
        else {
            n = 26;
        }
        float n2;
        if (zoomOnSelect) {
            n2 = 30.0f;
        }
        else {
            n2 = 26.0f;
        }
        this.addView((View)checkBox2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n, n2, 53, 0.0f, 4.0f, 4.0f, 0.0f));
        this.setFocusable(true);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(this.itemWidth, 1073741824), View$MeasureSpec.makeMeasureSpec(this.itemWidth, 1073741824));
    }
    
    public void setChecked(int backgroundColor, final boolean b, final boolean b2) {
        this.checkBox.setChecked(backgroundColor, b, b2);
        final AnimatorSet animator = this.animator;
        if (animator != null) {
            animator.cancel();
            this.animator = null;
        }
        if (this.zoomOnSelect) {
            backgroundColor = -16119286;
            float scaleY = 0.85f;
            if (b2) {
                if (b) {
                    this.setBackgroundColor(-16119286);
                }
                this.animator = new AnimatorSet();
                final AnimatorSet animator2 = this.animator;
                final BackupImageView photoImage = this.photoImage;
                float n;
                if (b) {
                    n = 0.85f;
                }
                else {
                    n = 1.0f;
                }
                final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)photoImage, "scaleX", new float[] { n });
                final BackupImageView photoImage2 = this.photoImage;
                if (!b) {
                    scaleY = 1.0f;
                }
                animator2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ObjectAnimator.ofFloat((Object)photoImage2, "scaleY", new float[] { scaleY }) });
                this.animator.setDuration(200L);
                this.animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationCancel(final Animator obj) {
                        if (PhotoPickerPhotoCell.this.animator != null && PhotoPickerPhotoCell.this.animator.equals(obj)) {
                            PhotoPickerPhotoCell.this.animator = null;
                        }
                    }
                    
                    public void onAnimationEnd(final Animator obj) {
                        if (PhotoPickerPhotoCell.this.animator != null && PhotoPickerPhotoCell.this.animator.equals(obj)) {
                            PhotoPickerPhotoCell.this.animator = null;
                            if (!b) {
                                PhotoPickerPhotoCell.this.setBackgroundColor(0);
                            }
                        }
                    }
                });
                this.animator.start();
            }
            else {
                if (!b) {
                    backgroundColor = 0;
                }
                this.setBackgroundColor(backgroundColor);
                final BackupImageView photoImage3 = this.photoImage;
                float scaleX;
                if (b) {
                    scaleX = 0.85f;
                }
                else {
                    scaleX = 1.0f;
                }
                photoImage3.setScaleX(scaleX);
                final BackupImageView photoImage4 = this.photoImage;
                if (!b) {
                    scaleY = 1.0f;
                }
                photoImage4.setScaleY(scaleY);
            }
        }
    }
    
    public void setImage(final MediaController.SearchImage searchImage) {
        final Drawable drawable = this.getResources().getDrawable(2131165697);
        final TLRPC.PhotoSize thumbPhotoSize = searchImage.thumbPhotoSize;
        if (thumbPhotoSize != null) {
            this.photoImage.setImage(ImageLocation.getForPhoto(thumbPhotoSize, searchImage.photo), null, drawable, searchImage);
        }
        else {
            final TLRPC.PhotoSize photoSize = searchImage.photoSize;
            if (photoSize != null) {
                this.photoImage.setImage(ImageLocation.getForPhoto(photoSize, searchImage.photo), "80_80", drawable, searchImage);
            }
            else {
                final String thumbPath = searchImage.thumbPath;
                if (thumbPath != null) {
                    this.photoImage.setImage(thumbPath, null, drawable);
                }
                else {
                    final String thumbUrl = searchImage.thumbUrl;
                    if (thumbUrl != null && thumbUrl.length() > 0) {
                        this.photoImage.setImage(searchImage.thumbUrl, null, drawable);
                    }
                    else if (MessageObject.isDocumentHasThumb(searchImage.document)) {
                        this.photoImage.setImage(ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(searchImage.document.thumbs, 320), searchImage.document), null, drawable, searchImage);
                    }
                    else {
                        this.photoImage.setImageDrawable(drawable);
                    }
                }
            }
        }
    }
    
    public void setNum(final int num) {
        this.checkBox.setNum(num);
    }
    
    public void showCheck(final boolean b) {
        final AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        (this.animatorSet = new AnimatorSet()).setInterpolator((TimeInterpolator)new DecelerateInterpolator());
        this.animatorSet.setDuration(180L);
        final AnimatorSet animatorSet2 = this.animatorSet;
        final FrameLayout videoInfoContainer = this.videoInfoContainer;
        final float n = 1.0f;
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
                if (animator.equals(PhotoPickerPhotoCell.this.animatorSet)) {
                    PhotoPickerPhotoCell.this.animatorSet = null;
                }
            }
        });
        this.animatorSet.start();
    }
}
