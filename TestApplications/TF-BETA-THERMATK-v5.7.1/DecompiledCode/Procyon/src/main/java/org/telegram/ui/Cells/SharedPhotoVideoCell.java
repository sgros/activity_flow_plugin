// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.DownloadController;
import org.telegram.tgnet.TLRPC;
import android.graphics.Bitmap;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.FileLoader;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Build$VERSION;
import android.view.MotionEvent;
import org.telegram.messenger.LocaleController;
import android.view.accessibility.AccessibilityNodeInfo;
import org.telegram.ui.PhotoViewer;
import android.widget.ImageView;
import android.graphics.Canvas;
import android.graphics.RectF;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.TextView;
import org.telegram.ui.Components.CheckBox2;
import android.animation.AnimatorSet;
import android.view.View$MeasureSpec;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.BackupImageView;
import android.view.View$OnLongClickListener;
import android.view.View$OnClickListener;
import android.view.View;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import org.telegram.messenger.MessageObject;
import android.graphics.Paint;
import android.widget.FrameLayout;

public class SharedPhotoVideoCell extends FrameLayout
{
    private Paint backgroundPaint;
    private int currentAccount;
    private SharedPhotoVideoCellDelegate delegate;
    private boolean ignoreLayout;
    private int[] indeces;
    private boolean isFirst;
    private int itemsCount;
    private MessageObject[] messageObjects;
    private PhotoVideoView[] photoVideoViews;
    
    public SharedPhotoVideoCell(final Context context) {
        super(context);
        this.backgroundPaint = new Paint();
        this.currentAccount = UserConfig.selectedAccount;
        this.backgroundPaint.setColor(Theme.getColor("sharedMedia_photoPlaceholder"));
        this.messageObjects = new MessageObject[6];
        this.photoVideoViews = new PhotoVideoView[6];
        this.indeces = new int[6];
        for (int i = 0; i < 6; ++i) {
            this.addView((View)(this.photoVideoViews[i] = new PhotoVideoView(context)));
            this.photoVideoViews[i].setVisibility(4);
            this.photoVideoViews[i].setTag((Object)i);
            this.photoVideoViews[i].setOnClickListener((View$OnClickListener)new _$$Lambda$SharedPhotoVideoCell$h2sL_iIl8gMm2fSeDVCPJaJSH54(this));
            this.photoVideoViews[i].setOnLongClickListener((View$OnLongClickListener)new _$$Lambda$SharedPhotoVideoCell$tNwom3pFU6ZmuNehI10sy_pOFqA(this));
        }
    }
    
    public BackupImageView getImageView(final int n) {
        if (n >= this.itemsCount) {
            return null;
        }
        return this.photoVideoViews[n].imageView;
    }
    
    public MessageObject getMessageObject(final int n) {
        if (n >= this.itemsCount) {
            return null;
        }
        return this.messageObjects[n];
    }
    
    protected void onMeasure(final int n, int n2) {
        if (AndroidUtilities.isTablet()) {
            n2 = (AndroidUtilities.dp(490.0f) - (this.itemsCount - 1) * AndroidUtilities.dp(2.0f)) / this.itemsCount;
        }
        else {
            n2 = (AndroidUtilities.displaySize.x - (this.itemsCount - 1) * AndroidUtilities.dp(2.0f)) / this.itemsCount;
        }
        this.ignoreLayout = true;
        final int n3 = 0;
        for (int i = 0; i < this.itemsCount; ++i) {
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.photoVideoViews[i].getLayoutParams();
            int dp;
            if (this.isFirst) {
                dp = 0;
            }
            else {
                dp = AndroidUtilities.dp(2.0f);
            }
            layoutParams.topMargin = dp;
            layoutParams.leftMargin = (AndroidUtilities.dp(2.0f) + n2) * i;
            if (i == this.itemsCount - 1) {
                if (AndroidUtilities.isTablet()) {
                    layoutParams.width = AndroidUtilities.dp(490.0f) - (this.itemsCount - 1) * (AndroidUtilities.dp(2.0f) + n2);
                }
                else {
                    layoutParams.width = AndroidUtilities.displaySize.x - (this.itemsCount - 1) * (AndroidUtilities.dp(2.0f) + n2);
                }
            }
            else {
                layoutParams.width = n2;
            }
            layoutParams.height = n2;
            layoutParams.gravity = 51;
            this.photoVideoViews[i].setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
        this.ignoreLayout = false;
        int dp2;
        if (this.isFirst) {
            dp2 = n3;
        }
        else {
            dp2 = AndroidUtilities.dp(2.0f);
        }
        super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(dp2 + n2, 1073741824));
    }
    
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }
    
    public void setChecked(final int n, final boolean b, final boolean b2) {
        this.photoVideoViews[n].setChecked(b, b2);
    }
    
    public void setDelegate(final SharedPhotoVideoCellDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setIsFirst(final boolean isFirst) {
        this.isFirst = isFirst;
    }
    
    public void setItem(final int n, final int n2, final MessageObject messageObject) {
        this.messageObjects[n] = messageObject;
        this.indeces[n] = n2;
        if (messageObject != null) {
            this.photoVideoViews[n].setVisibility(0);
            this.photoVideoViews[n].setMessageObject(messageObject);
        }
        else {
            this.photoVideoViews[n].clearAnimation();
            this.photoVideoViews[n].setVisibility(4);
            this.messageObjects[n] = null;
        }
    }
    
    public void setItemsCount(final int itemsCount) {
        int n = 0;
        while (true) {
            final PhotoVideoView[] photoVideoViews = this.photoVideoViews;
            if (n >= photoVideoViews.length) {
                break;
            }
            photoVideoViews[n].clearAnimation();
            final PhotoVideoView photoVideoView = this.photoVideoViews[n];
            int visibility;
            if (n < itemsCount) {
                visibility = 0;
            }
            else {
                visibility = 4;
            }
            photoVideoView.setVisibility(visibility);
            ++n;
        }
        this.itemsCount = itemsCount;
    }
    
    public void updateCheckboxColor() {
        for (int i = 0; i < 6; ++i) {
            this.photoVideoViews[i].checkBox.invalidate();
        }
    }
    
    private class PhotoVideoView extends FrameLayout
    {
        private AnimatorSet animator;
        private CheckBox2 checkBox;
        private FrameLayout container;
        private MessageObject currentMessageObject;
        private BackupImageView imageView;
        private View selector;
        private FrameLayout videoInfoContainer;
        private TextView videoTextView;
        
        public PhotoVideoView(final Context context) {
            super(context);
            this.setWillNotDraw(false);
            this.addView((View)(this.container = new FrameLayout(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            this.imageView = new BackupImageView(context);
            this.imageView.getImageReceiver().setNeedsQualityThumb(true);
            this.imageView.getImageReceiver().setShouldGenerateQualityThumb(true);
            this.container.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            (this.videoInfoContainer = new FrameLayout(context) {
                private RectF rect = new RectF();
                
                protected void onDraw(final Canvas canvas) {
                    this.rect.set(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
                    canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(4.0f), Theme.chat_timeBackgroundPaint);
                }
            }).setWillNotDraw(false);
            this.videoInfoContainer.setPadding(AndroidUtilities.dp(5.0f), 0, AndroidUtilities.dp(5.0f), 0);
            this.container.addView((View)this.videoInfoContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 17.0f, 83, 4.0f, 0.0f, 0.0f, 4.0f));
            final ImageView imageView = new ImageView(context);
            imageView.setImageResource(2131165772);
            this.videoInfoContainer.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 19));
            (this.videoTextView = new TextView(context)).setTextColor(-1);
            this.videoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.videoTextView.setTextSize(1, 12.0f);
            this.videoTextView.setImportantForAccessibility(2);
            this.videoInfoContainer.addView((View)this.videoTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 19, 13.0f, -0.7f, 0.0f, 0.0f));
            (this.selector = new View(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.addView(this.selector, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            (this.checkBox = new CheckBox2(context)).setVisibility(4);
            this.checkBox.setColor(null, "sharedMedia_photoPlaceholder", "checkboxCheck");
            this.checkBox.setSize(21);
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(1);
            this.addView((View)this.checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(24, 24.0f, 53, 0.0f, 1.0f, 1.0f, 0.0f));
        }
        
        public void clearAnimation() {
            super.clearAnimation();
            final AnimatorSet animator = this.animator;
            if (animator != null) {
                animator.cancel();
                this.animator = null;
            }
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.checkBox.isChecked() || !this.imageView.getImageReceiver().hasBitmapImage() || this.imageView.getImageReceiver().getCurrentAlpha() != 1.0f || PhotoViewer.isShowingImage(this.currentMessageObject)) {
                canvas.drawRect(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), SharedPhotoVideoCell.this.backgroundPaint);
            }
        }
        
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            if (this.currentMessageObject.isVideo()) {
                final StringBuilder sb = new StringBuilder();
                sb.append(LocaleController.getString("AttachVideo", 2131558733));
                sb.append(", ");
                sb.append(LocaleController.formatCallDuration(this.currentMessageObject.getDuration()));
                accessibilityNodeInfo.setText((CharSequence)sb.toString());
            }
            else {
                accessibilityNodeInfo.setText((CharSequence)LocaleController.getString("AttachPhoto", 2131558727));
            }
            if (this.checkBox.isChecked()) {
                accessibilityNodeInfo.setCheckable(true);
                accessibilityNodeInfo.setChecked(true);
            }
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            if (Build$VERSION.SDK_INT >= 21) {
                this.selector.drawableHotspotChanged(motionEvent.getX(), motionEvent.getY());
            }
            return super.onTouchEvent(motionEvent);
        }
        
        public void setChecked(final boolean b, final boolean b2) {
            if (this.checkBox.getVisibility() != 0) {
                this.checkBox.setVisibility(0);
            }
            this.checkBox.setChecked(b, b2);
            final AnimatorSet animator = this.animator;
            if (animator != null) {
                animator.cancel();
                this.animator = null;
            }
            float scaleY = 1.0f;
            if (b2) {
                this.animator = new AnimatorSet();
                final AnimatorSet animator2 = this.animator;
                final FrameLayout container = this.container;
                float n;
                if (b) {
                    n = 0.81f;
                }
                else {
                    n = 1.0f;
                }
                final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)container, "scaleX", new float[] { n });
                final FrameLayout container2 = this.container;
                if (b) {
                    scaleY = 0.81f;
                }
                animator2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ObjectAnimator.ofFloat((Object)container2, "scaleY", new float[] { scaleY }) });
                this.animator.setDuration(200L);
                this.animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationCancel(final Animator obj) {
                        if (PhotoVideoView.this.animator != null && PhotoVideoView.this.animator.equals(obj)) {
                            PhotoVideoView.this.animator = null;
                        }
                    }
                    
                    public void onAnimationEnd(final Animator obj) {
                        if (PhotoVideoView.this.animator != null && PhotoVideoView.this.animator.equals(obj)) {
                            PhotoVideoView.this.animator = null;
                            if (!b) {
                                PhotoVideoView.this.setBackgroundColor(0);
                            }
                        }
                    }
                });
                this.animator.start();
            }
            else {
                final FrameLayout container3 = this.container;
                float scaleX;
                if (b) {
                    scaleX = 0.85f;
                }
                else {
                    scaleX = 1.0f;
                }
                container3.setScaleX(scaleX);
                final FrameLayout container4 = this.container;
                if (b) {
                    scaleY = 0.85f;
                }
                container4.setScaleY(scaleY);
            }
        }
        
        public void setMessageObject(final MessageObject currentMessageObject) {
            this.currentMessageObject = currentMessageObject;
            this.imageView.getImageReceiver().setVisible(PhotoViewer.isShowingImage(currentMessageObject) ^ true, false);
            final boolean video = currentMessageObject.isVideo();
            final TLRPC.PhotoSize photoSize = null;
            TLRPC.PhotoSize photoSize2 = null;
            if (video) {
                this.videoInfoContainer.setVisibility(0);
                final int duration = currentMessageObject.getDuration();
                final int i = duration / 60;
                this.videoTextView.setText((CharSequence)String.format("%d:%02d", i, duration - i * 60));
                final TLRPC.Document document = currentMessageObject.getDocument();
                final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50);
                final TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 320);
                if (closestPhotoSizeWithSize != closestPhotoSizeWithSize2) {
                    photoSize2 = closestPhotoSizeWithSize2;
                }
                if (closestPhotoSizeWithSize != null) {
                    this.imageView.setImage(ImageLocation.getForDocument(photoSize2, document), "100_100", ImageLocation.getForDocument(closestPhotoSizeWithSize, document), "b", ApplicationLoader.applicationContext.getResources().getDrawable(2131165748), null, null, 0, currentMessageObject);
                }
                else {
                    this.imageView.setImageResource(2131165748);
                }
            }
            else {
                final TLRPC.MessageMedia media = currentMessageObject.messageOwner.media;
                if (media instanceof TLRPC.TL_messageMediaPhoto && media.photo != null && !currentMessageObject.photoThumbs.isEmpty()) {
                    this.videoInfoContainer.setVisibility(4);
                    final TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(currentMessageObject.photoThumbs, 320);
                    TLRPC.PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(currentMessageObject.photoThumbs, 50);
                    if (!currentMessageObject.mediaExists && !DownloadController.getInstance(SharedPhotoVideoCell.this.currentAccount).canDownloadMedia(currentMessageObject)) {
                        this.imageView.setImage(null, null, ImageLocation.getForObject(closestPhotoSizeWithSize4, currentMessageObject.photoThumbsObject), "b", ApplicationLoader.applicationContext.getResources().getDrawable(2131165748), null, null, 0, currentMessageObject);
                    }
                    else {
                        if (closestPhotoSizeWithSize3 == closestPhotoSizeWithSize4) {
                            closestPhotoSizeWithSize4 = photoSize;
                        }
                        final ImageReceiver imageReceiver = this.imageView.getImageReceiver();
                        final ImageLocation forObject = ImageLocation.getForObject(closestPhotoSizeWithSize3, currentMessageObject.photoThumbsObject);
                        final ImageLocation forObject2 = ImageLocation.getForObject(closestPhotoSizeWithSize4, currentMessageObject.photoThumbsObject);
                        final int size = closestPhotoSizeWithSize3.size;
                        int n;
                        if (currentMessageObject.shouldEncryptPhotoOrVideo()) {
                            n = 2;
                        }
                        else {
                            n = 1;
                        }
                        imageReceiver.setImage(forObject, "100_100", forObject2, "b", size, null, currentMessageObject, n);
                    }
                }
                else {
                    this.videoInfoContainer.setVisibility(4);
                    this.imageView.setImageResource(2131165748);
                }
            }
        }
    }
    
    public interface SharedPhotoVideoCellDelegate
    {
        void didClickItem(final SharedPhotoVideoCell p0, final int p1, final MessageObject p2, final int p3);
        
        boolean didLongClickItem(final SharedPhotoVideoCell p0, final int p1, final MessageObject p2, final int p3);
    }
}
