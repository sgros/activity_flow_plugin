package org.telegram.p004ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.BackupImageView;
import org.telegram.p004ui.Components.CheckBox2;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.PhotoViewer;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;

/* renamed from: org.telegram.ui.Cells.SharedPhotoVideoCell */
public class SharedPhotoVideoCell extends FrameLayout {
    private Paint backgroundPaint = new Paint();
    private int currentAccount = UserConfig.selectedAccount;
    private SharedPhotoVideoCellDelegate delegate;
    private boolean ignoreLayout;
    private int[] indeces;
    private boolean isFirst;
    private int itemsCount;
    private MessageObject[] messageObjects;
    private PhotoVideoView[] photoVideoViews;

    /* renamed from: org.telegram.ui.Cells.SharedPhotoVideoCell$PhotoVideoView */
    private class PhotoVideoView extends FrameLayout {
        private AnimatorSet animator;
        private CheckBox2 checkBox;
        private FrameLayout container;
        private MessageObject currentMessageObject;
        private BackupImageView imageView;
        private View selector;
        private FrameLayout videoInfoContainer;
        private TextView videoTextView;

        public PhotoVideoView(Context context) {
            super(context);
            setWillNotDraw(false);
            this.container = new FrameLayout(context);
            addView(this.container, LayoutHelper.createFrame(-1, -1.0f));
            this.imageView = new BackupImageView(context);
            this.imageView.getImageReceiver().setNeedsQualityThumb(true);
            this.imageView.getImageReceiver().setShouldGenerateQualityThumb(true);
            this.container.addView(this.imageView, LayoutHelper.createFrame(-1, -1.0f));
            this.videoInfoContainer = new FrameLayout(context, SharedPhotoVideoCell.this) {
                private RectF rect = new RectF();

                /* Access modifiers changed, original: protected */
                public void onDraw(Canvas canvas) {
                    this.rect.set(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight());
                    canvas.drawRoundRect(this.rect, (float) AndroidUtilities.m26dp(4.0f), (float) AndroidUtilities.m26dp(4.0f), Theme.chat_timeBackgroundPaint);
                }
            };
            this.videoInfoContainer.setWillNotDraw(false);
            this.videoInfoContainer.setPadding(AndroidUtilities.m26dp(5.0f), 0, AndroidUtilities.m26dp(5.0f), 0);
            this.container.addView(this.videoInfoContainer, LayoutHelper.createFrame(-2, 17.0f, 83, 4.0f, 0.0f, 0.0f, 4.0f));
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(C1067R.C1065drawable.play_mini_video);
            this.videoInfoContainer.addView(imageView, LayoutHelper.createFrame(-2, -2, 19));
            this.videoTextView = new TextView(context);
            this.videoTextView.setTextColor(-1);
            this.videoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.videoTextView.setTextSize(1, 12.0f);
            this.videoTextView.setImportantForAccessibility(2);
            this.videoInfoContainer.addView(this.videoTextView, LayoutHelper.createFrame(-2, -2.0f, 19, 13.0f, -0.7f, 0.0f, 0.0f));
            this.selector = new View(context);
            this.selector.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            addView(this.selector, LayoutHelper.createFrame(-1, -1.0f));
            this.checkBox = new CheckBox2(context);
            this.checkBox.setVisibility(4);
            this.checkBox.setColor(null, Theme.key_sharedMedia_photoPlaceholder, Theme.key_checkboxCheck);
            this.checkBox.setSize(21);
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(1);
            addView(this.checkBox, LayoutHelper.createFrame(24, 24.0f, 53, 0.0f, 1.0f, 1.0f, 0.0f));
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (VERSION.SDK_INT >= 21) {
                this.selector.drawableHotspotChanged(motionEvent.getX(), motionEvent.getY());
            }
            return super.onTouchEvent(motionEvent);
        }

        public void setChecked(final boolean z, boolean z2) {
            if (this.checkBox.getVisibility() != 0) {
                this.checkBox.setVisibility(0);
            }
            this.checkBox.setChecked(z, z2);
            AnimatorSet animatorSet = this.animator;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.animator = null;
            }
            float f = 1.0f;
            if (z2) {
                this.animator = new AnimatorSet();
                AnimatorSet animatorSet2 = this.animator;
                Animator[] animatorArr = new Animator[2];
                FrameLayout frameLayout = this.container;
                float[] fArr = new float[1];
                fArr[0] = z ? 0.81f : 1.0f;
                animatorArr[0] = ObjectAnimator.ofFloat(frameLayout, "scaleX", fArr);
                frameLayout = this.container;
                fArr = new float[1];
                if (z) {
                    f = 0.81f;
                }
                fArr[0] = f;
                animatorArr[1] = ObjectAnimator.ofFloat(frameLayout, "scaleY", fArr);
                animatorSet2.playTogether(animatorArr);
                this.animator.setDuration(200);
                this.animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        if (PhotoVideoView.this.animator != null && PhotoVideoView.this.animator.equals(animator)) {
                            PhotoVideoView.this.animator = null;
                            if (!z) {
                                PhotoVideoView.this.setBackgroundColor(0);
                            }
                        }
                    }

                    public void onAnimationCancel(Animator animator) {
                        if (PhotoVideoView.this.animator != null && PhotoVideoView.this.animator.equals(animator)) {
                            PhotoVideoView.this.animator = null;
                        }
                    }
                });
                this.animator.start();
                return;
            }
            this.container.setScaleX(z ? 0.85f : 1.0f);
            FrameLayout frameLayout2 = this.container;
            if (z) {
                f = 0.85f;
            }
            frameLayout2.setScaleY(f);
        }

        public void setMessageObject(MessageObject messageObject) {
            this.currentMessageObject = messageObject;
            this.imageView.getImageReceiver().setVisible(PhotoViewer.isShowingImage(messageObject) ^ 1, false);
            PhotoSize photoSize = null;
            PhotoSize closestPhotoSizeWithSize;
            if (messageObject.isVideo()) {
                this.videoInfoContainer.setVisibility(0);
                int duration = messageObject.getDuration();
                duration -= (duration / 60) * 60;
                this.videoTextView.setText(String.format("%d:%02d", new Object[]{Integer.valueOf(r8), Integer.valueOf(duration)}));
                Document document = messageObject.getDocument();
                PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50);
                closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 320);
                if (closestPhotoSizeWithSize2 != closestPhotoSizeWithSize) {
                    photoSize = closestPhotoSizeWithSize;
                }
                if (closestPhotoSizeWithSize2 != null) {
                    this.imageView.setImage(ImageLocation.getForDocument(photoSize, document), "100_100", ImageLocation.getForDocument(closestPhotoSizeWithSize2, document), "b", ApplicationLoader.applicationContext.getResources().getDrawable(C1067R.C1065drawable.photo_placeholder_in), null, null, 0, messageObject);
                    return;
                }
                this.imageView.setImageResource(C1067R.C1065drawable.photo_placeholder_in);
                return;
            }
            MessageMedia messageMedia = messageObject.messageOwner.media;
            if (!(messageMedia instanceof TL_messageMediaPhoto) || messageMedia.photo == null || messageObject.photoThumbs.isEmpty()) {
                this.videoInfoContainer.setVisibility(4);
                this.imageView.setImageResource(C1067R.C1065drawable.photo_placeholder_in);
                return;
            }
            this.videoInfoContainer.setVisibility(4);
            PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 320);
            closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 50);
            if (messageObject.mediaExists || DownloadController.getInstance(SharedPhotoVideoCell.this.currentAccount).canDownloadMedia(messageObject)) {
                if (closestPhotoSizeWithSize3 != closestPhotoSizeWithSize) {
                    photoSize = closestPhotoSizeWithSize;
                }
                this.imageView.getImageReceiver().setImage(ImageLocation.getForObject(closestPhotoSizeWithSize3, messageObject.photoThumbsObject), "100_100", ImageLocation.getForObject(photoSize, messageObject.photoThumbsObject), "b", closestPhotoSizeWithSize3.size, null, messageObject, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 1);
                return;
            }
            this.imageView.setImage(null, null, ImageLocation.getForObject(closestPhotoSizeWithSize, messageObject.photoThumbsObject), "b", ApplicationLoader.applicationContext.getResources().getDrawable(C1067R.C1065drawable.photo_placeholder_in), null, null, 0, messageObject);
        }

        public void clearAnimation() {
            super.clearAnimation();
            AnimatorSet animatorSet = this.animator;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.animator = null;
            }
        }

        /* Access modifiers changed, original: protected */
        public void onDraw(Canvas canvas) {
            if (this.checkBox.isChecked() || !this.imageView.getImageReceiver().hasBitmapImage() || this.imageView.getImageReceiver().getCurrentAlpha() != 1.0f || PhotoViewer.isShowingImage(this.currentMessageObject)) {
                canvas.drawRect(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight(), SharedPhotoVideoCell.this.backgroundPaint);
            }
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            if (this.currentMessageObject.isVideo()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(LocaleController.getString("AttachVideo", C1067R.string.AttachVideo));
                stringBuilder.append(", ");
                stringBuilder.append(LocaleController.formatCallDuration(this.currentMessageObject.getDuration()));
                accessibilityNodeInfo.setText(stringBuilder.toString());
            } else {
                accessibilityNodeInfo.setText(LocaleController.getString("AttachPhoto", C1067R.string.AttachPhoto));
            }
            if (this.checkBox.isChecked()) {
                accessibilityNodeInfo.setCheckable(true);
                accessibilityNodeInfo.setChecked(true);
            }
        }
    }

    /* renamed from: org.telegram.ui.Cells.SharedPhotoVideoCell$SharedPhotoVideoCellDelegate */
    public interface SharedPhotoVideoCellDelegate {
        void didClickItem(SharedPhotoVideoCell sharedPhotoVideoCell, int i, MessageObject messageObject, int i2);

        boolean didLongClickItem(SharedPhotoVideoCell sharedPhotoVideoCell, int i, MessageObject messageObject, int i2);
    }

    public SharedPhotoVideoCell(Context context) {
        super(context);
        this.backgroundPaint.setColor(Theme.getColor(Theme.key_sharedMedia_photoPlaceholder));
        this.messageObjects = new MessageObject[6];
        this.photoVideoViews = new PhotoVideoView[6];
        this.indeces = new int[6];
        for (int i = 0; i < 6; i++) {
            this.photoVideoViews[i] = new PhotoVideoView(context);
            addView(this.photoVideoViews[i]);
            this.photoVideoViews[i].setVisibility(4);
            this.photoVideoViews[i].setTag(Integer.valueOf(i));
            this.photoVideoViews[i].setOnClickListener(new C2335-$$Lambda$SharedPhotoVideoCell$h2sL-iIl8gMm2fSeDVCPJaJSH54(this));
            this.photoVideoViews[i].setOnLongClickListener(new C2336-$$Lambda$SharedPhotoVideoCell$tNwom3pFU6ZmuNehI10sy_pOFqA(this));
        }
    }

    public /* synthetic */ void lambda$new$0$SharedPhotoVideoCell(View view) {
        if (this.delegate != null) {
            int intValue = ((Integer) view.getTag()).intValue();
            this.delegate.didClickItem(this, this.indeces[intValue], this.messageObjects[intValue], intValue);
        }
    }

    public /* synthetic */ boolean lambda$new$1$SharedPhotoVideoCell(View view) {
        if (this.delegate == null) {
            return false;
        }
        int intValue = ((Integer) view.getTag()).intValue();
        return this.delegate.didLongClickItem(this, this.indeces[intValue], this.messageObjects[intValue], intValue);
    }

    public void updateCheckboxColor() {
        for (int i = 0; i < 6; i++) {
            this.photoVideoViews[i].checkBox.invalidate();
        }
    }

    public void setDelegate(SharedPhotoVideoCellDelegate sharedPhotoVideoCellDelegate) {
        this.delegate = sharedPhotoVideoCellDelegate;
    }

    public void setItemsCount(int i) {
        int i2 = 0;
        while (true) {
            PhotoVideoView[] photoVideoViewArr = this.photoVideoViews;
            if (i2 < photoVideoViewArr.length) {
                photoVideoViewArr[i2].clearAnimation();
                this.photoVideoViews[i2].setVisibility(i2 < i ? 0 : 4);
                i2++;
            } else {
                this.itemsCount = i;
                return;
            }
        }
    }

    public BackupImageView getImageView(int i) {
        if (i >= this.itemsCount) {
            return null;
        }
        return this.photoVideoViews[i].imageView;
    }

    public MessageObject getMessageObject(int i) {
        if (i >= this.itemsCount) {
            return null;
        }
        return this.messageObjects[i];
    }

    public void setIsFirst(boolean z) {
        this.isFirst = z;
    }

    public void setChecked(int i, boolean z, boolean z2) {
        this.photoVideoViews[i].setChecked(z, z2);
    }

    public void setItem(int i, int i2, MessageObject messageObject) {
        this.messageObjects[i] = messageObject;
        this.indeces[i] = i2;
        if (messageObject != null) {
            this.photoVideoViews[i].setVisibility(0);
            this.photoVideoViews[i].setMessageObject(messageObject);
            return;
        }
        this.photoVideoViews[i].clearAnimation();
        this.photoVideoViews[i].setVisibility(4);
        this.messageObjects[i] = null;
    }

    public void requestLayout() {
        if (!this.ignoreLayout) {
            super.requestLayout();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        if (AndroidUtilities.isTablet()) {
            i2 = (AndroidUtilities.m26dp(490.0f) - ((this.itemsCount - 1) * AndroidUtilities.m26dp(2.0f))) / this.itemsCount;
        } else {
            i2 = (AndroidUtilities.displaySize.x - ((this.itemsCount - 1) * AndroidUtilities.m26dp(2.0f))) / this.itemsCount;
        }
        this.ignoreLayout = true;
        int i3 = 0;
        for (int i4 = 0; i4 < this.itemsCount; i4++) {
            LayoutParams layoutParams = (LayoutParams) this.photoVideoViews[i4].getLayoutParams();
            layoutParams.topMargin = this.isFirst ? 0 : AndroidUtilities.m26dp(2.0f);
            layoutParams.leftMargin = (AndroidUtilities.m26dp(2.0f) + i2) * i4;
            if (i4 != this.itemsCount - 1) {
                layoutParams.width = i2;
            } else if (AndroidUtilities.isTablet()) {
                layoutParams.width = AndroidUtilities.m26dp(490.0f) - ((this.itemsCount - 1) * (AndroidUtilities.m26dp(2.0f) + i2));
            } else {
                layoutParams.width = AndroidUtilities.displaySize.x - ((this.itemsCount - 1) * (AndroidUtilities.m26dp(2.0f) + i2));
            }
            layoutParams.height = i2;
            layoutParams.gravity = 51;
            this.photoVideoViews[i4].setLayoutParams(layoutParams);
        }
        this.ignoreLayout = false;
        if (!this.isFirst) {
            i3 = AndroidUtilities.m26dp(2.0f);
        }
        super.onMeasure(i, MeasureSpec.makeMeasureSpec(i3 + i2, 1073741824));
    }
}