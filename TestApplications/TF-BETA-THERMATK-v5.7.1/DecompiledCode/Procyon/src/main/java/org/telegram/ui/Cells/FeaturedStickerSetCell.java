// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.FileLoader;
import android.graphics.drawable.Drawable;
import android.view.View$OnClickListener;
import android.view.View$MeasureSpec;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Style;
import android.graphics.Paint$Cap;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import org.telegram.tgnet.TLRPC;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint;
import org.telegram.ui.Components.BackupImageView;
import android.animation.AnimatorSet;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout;

public class FeaturedStickerSetCell extends FrameLayout
{
    private TextView addButton;
    private int angle;
    private ImageView checkImage;
    private int currentAccount;
    private AnimatorSet currentAnimation;
    private boolean drawProgress;
    private BackupImageView imageView;
    private boolean isInstalled;
    private long lastUpdateTime;
    private boolean needDivider;
    private float progressAlpha;
    private Paint progressPaint;
    private RectF progressRect;
    private Rect rect;
    private TLRPC.StickerSetCovered stickersSet;
    private TextView textView;
    private TextView valueTextView;
    private boolean wasLayout;
    
    public FeaturedStickerSetCell(final Context context) {
        super(context);
        this.rect = new Rect();
        this.currentAccount = UserConfig.selectedAccount;
        this.progressRect = new RectF();
        (this.progressPaint = new Paint(1)).setColor(Theme.getColor("featuredStickers_buttonProgress"));
        this.progressPaint.setStrokeCap(Paint$Cap.ROUND);
        this.progressPaint.setStyle(Paint$Style.STROKE);
        this.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int gravity;
        if (isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        textView.setGravity(gravity);
        final TextView textView2 = this.textView;
        int n2;
        if (LocaleController.isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        float n3;
        if (LocaleController.isRTL) {
            n3 = 22.0f;
        }
        else {
            n3 = 71.0f;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 71.0f;
        }
        else {
            n4 = 22.0f;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n2, n3, 10.0f, n4, 0.0f));
        (this.valueTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.valueTextView.setTextSize(1, 13.0f);
        this.valueTextView.setLines(1);
        this.valueTextView.setMaxLines(1);
        this.valueTextView.setSingleLine(true);
        this.valueTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView valueTextView = this.valueTextView;
        int gravity2;
        if (LocaleController.isRTL) {
            gravity2 = 5;
        }
        else {
            gravity2 = 3;
        }
        valueTextView.setGravity(gravity2);
        final TextView valueTextView2 = this.valueTextView;
        int n5;
        if (LocaleController.isRTL) {
            n5 = 5;
        }
        else {
            n5 = 3;
        }
        float n6;
        if (LocaleController.isRTL) {
            n6 = 100.0f;
        }
        else {
            n6 = 71.0f;
        }
        float n7;
        if (LocaleController.isRTL) {
            n7 = 71.0f;
        }
        else {
            n7 = 100.0f;
        }
        this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n5, n6, 35.0f, n7, 0.0f));
        (this.imageView = new BackupImageView(context)).setAspectFit(true);
        final BackupImageView imageView = this.imageView;
        int n8;
        if (LocaleController.isRTL) {
            n8 = 5;
        }
        else {
            n8 = 3;
        }
        float n9;
        if (LocaleController.isRTL) {
            n9 = 0.0f;
        }
        else {
            n9 = 12.0f;
        }
        float n10;
        if (LocaleController.isRTL) {
            n10 = 12.0f;
        }
        else {
            n10 = 0.0f;
        }
        this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, n8 | 0x30, n9, 8.0f, n10, 0.0f));
        (this.addButton = new TextView(context) {
            protected void onDraw(final Canvas canvas) {
                super.onDraw(canvas);
                if (FeaturedStickerSetCell.this.drawProgress || (!FeaturedStickerSetCell.this.drawProgress && FeaturedStickerSetCell.this.progressAlpha != 0.0f)) {
                    FeaturedStickerSetCell.this.progressPaint.setAlpha(Math.min(255, (int)(FeaturedStickerSetCell.this.progressAlpha * 255.0f)));
                    final int n = this.getMeasuredWidth() - AndroidUtilities.dp(11.0f);
                    FeaturedStickerSetCell.this.progressRect.set((float)n, (float)AndroidUtilities.dp(3.0f), (float)(n + AndroidUtilities.dp(8.0f)), (float)AndroidUtilities.dp(11.0f));
                    canvas.drawArc(FeaturedStickerSetCell.this.progressRect, (float)FeaturedStickerSetCell.this.angle, 220.0f, false, FeaturedStickerSetCell.this.progressPaint);
                    this.invalidate((int)FeaturedStickerSetCell.this.progressRect.left - AndroidUtilities.dp(2.0f), (int)FeaturedStickerSetCell.this.progressRect.top - AndroidUtilities.dp(2.0f), (int)FeaturedStickerSetCell.this.progressRect.right + AndroidUtilities.dp(2.0f), (int)FeaturedStickerSetCell.this.progressRect.bottom + AndroidUtilities.dp(2.0f));
                    final long currentTimeMillis = System.currentTimeMillis();
                    if (Math.abs(FeaturedStickerSetCell.this.lastUpdateTime - System.currentTimeMillis()) < 1000L) {
                        final long n2 = currentTimeMillis - FeaturedStickerSetCell.this.lastUpdateTime;
                        final float n3 = 360L * n2 / 2000.0f;
                        final FeaturedStickerSetCell this$0 = FeaturedStickerSetCell.this;
                        this$0.angle += n3;
                        final FeaturedStickerSetCell this$2 = FeaturedStickerSetCell.this;
                        this$2.angle -= FeaturedStickerSetCell.this.angle / 360 * 360;
                        if (FeaturedStickerSetCell.this.drawProgress) {
                            if (FeaturedStickerSetCell.this.progressAlpha < 1.0f) {
                                final FeaturedStickerSetCell this$3 = FeaturedStickerSetCell.this;
                                this$3.progressAlpha += n2 / 200.0f;
                                if (FeaturedStickerSetCell.this.progressAlpha > 1.0f) {
                                    FeaturedStickerSetCell.this.progressAlpha = 1.0f;
                                }
                            }
                        }
                        else if (FeaturedStickerSetCell.this.progressAlpha > 0.0f) {
                            final FeaturedStickerSetCell this$4 = FeaturedStickerSetCell.this;
                            this$4.progressAlpha -= n2 / 200.0f;
                            if (FeaturedStickerSetCell.this.progressAlpha < 0.0f) {
                                FeaturedStickerSetCell.this.progressAlpha = 0.0f;
                            }
                        }
                    }
                    FeaturedStickerSetCell.this.lastUpdateTime = currentTimeMillis;
                    this.invalidate();
                }
            }
        }).setGravity(17);
        this.addButton.setTextColor(Theme.getColor("featuredStickers_buttonText"));
        this.addButton.setTextSize(1, 14.0f);
        this.addButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.addButton.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), Theme.getColor("featuredStickers_addButton"), Theme.getColor("featuredStickers_addButtonPressed")));
        this.addButton.setText((CharSequence)LocaleController.getString("Add", 2131558555).toUpperCase());
        this.addButton.setPadding(AndroidUtilities.dp(17.0f), 0, AndroidUtilities.dp(17.0f), 0);
        final TextView addButton = this.addButton;
        int n11 = n;
        if (LocaleController.isRTL) {
            n11 = 3;
        }
        float n12;
        if (LocaleController.isRTL) {
            n12 = 14.0f;
        }
        else {
            n12 = 0.0f;
        }
        float n13;
        if (LocaleController.isRTL) {
            n13 = 0.0f;
        }
        else {
            n13 = 14.0f;
        }
        this.addView((View)addButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 28.0f, n11 | 0x30, n12, 18.0f, n13, 0.0f));
        (this.checkImage = new ImageView(context)).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), PorterDuff$Mode.MULTIPLY));
        this.checkImage.setImageResource(2131165858);
        this.addView((View)this.checkImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(19, 14.0f));
    }
    
    public TLRPC.StickerSetCovered getStickerSet() {
        return this.stickersSet;
    }
    
    public boolean isInstalled() {
        return this.isInstalled;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.wasLayout = false;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine(0.0f, (float)(this.getHeight() - 1), (float)(this.getWidth() - this.getPaddingRight()), (float)(this.getHeight() - 1), Theme.dividerPaint);
        }
    }
    
    protected void onLayout(final boolean b, int n, int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        n2 = this.addButton.getLeft() + this.addButton.getMeasuredWidth() / 2 - this.checkImage.getMeasuredWidth() / 2;
        n = this.addButton.getTop() + this.addButton.getMeasuredHeight() / 2 - this.checkImage.getMeasuredHeight() / 2;
        final ImageView checkImage = this.checkImage;
        checkImage.layout(n2, n, checkImage.getMeasuredWidth() + n2, this.checkImage.getMeasuredHeight() + n);
        this.wasLayout = true;
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.needDivider ? 1 : 0), 1073741824));
        this.measureChildWithMargins((View)this.textView, n, this.addButton.getMeasuredWidth(), n2, 0);
    }
    
    public void setAddOnClickListener(final View$OnClickListener onClickListener) {
        this.addButton.setOnClickListener(onClickListener);
    }
    
    public void setDrawProgress(final boolean drawProgress) {
        this.drawProgress = drawProgress;
        this.lastUpdateTime = System.currentTimeMillis();
        this.addButton.invalidate();
    }
    
    public void setStickersSet(final TLRPC.StickerSetCovered stickersSet, final boolean needDivider, final boolean b) {
        final boolean b2 = stickersSet == this.stickersSet && this.wasLayout;
        this.needDivider = needDivider;
        this.stickersSet = stickersSet;
        this.lastUpdateTime = System.currentTimeMillis();
        this.setWillNotDraw(this.needDivider ^ true);
        final AnimatorSet currentAnimation = this.currentAnimation;
        final TLRPC.PhotoSize photoSize = null;
        if (currentAnimation != null) {
            currentAnimation.cancel();
            this.currentAnimation = null;
        }
        this.textView.setText((CharSequence)this.stickersSet.set.title);
        if (b) {
            Drawable drawable = new Drawable() {
                Paint paint = new Paint(1);
                
                public void draw(final Canvas canvas) {
                    this.paint.setColor(-12277526);
                    canvas.drawCircle((float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(5.0f), (float)AndroidUtilities.dp(3.0f), this.paint);
                }
                
                public int getIntrinsicHeight() {
                    return AndroidUtilities.dp(8.0f);
                }
                
                public int getIntrinsicWidth() {
                    return AndroidUtilities.dp(12.0f);
                }
                
                public int getOpacity() {
                    return -2;
                }
                
                public void setAlpha(final int n) {
                }
                
                public void setColorFilter(final ColorFilter colorFilter) {
                }
            };
            final TextView textView = this.textView;
            Drawable drawable2;
            if (LocaleController.isRTL) {
                drawable2 = null;
            }
            else {
                drawable2 = drawable;
            }
            if (!LocaleController.isRTL) {
                drawable = null;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable2, (Drawable)null, drawable, (Drawable)null);
        }
        else {
            this.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        this.valueTextView.setText((CharSequence)LocaleController.formatPluralString("Stickers", stickersSet.set.count));
        final TLRPC.Document cover = stickersSet.cover;
        TLRPC.PhotoSize closestPhotoSizeWithSize = photoSize;
        if (cover != null) {
            closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(cover.thumbs, 90);
        }
        if (closestPhotoSizeWithSize != null && closestPhotoSizeWithSize.location != null) {
            this.imageView.setImage(ImageLocation.getForDocument(closestPhotoSizeWithSize, stickersSet.cover), null, "webp", null, stickersSet);
        }
        else if (!stickersSet.covers.isEmpty()) {
            final TLRPC.Document document = stickersSet.covers.get(0);
            this.imageView.setImage(ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90), document), null, "webp", null, stickersSet);
        }
        else {
            this.imageView.setImage(null, null, "webp", null, stickersSet);
        }
        if (b2) {
            final boolean isInstalled = this.isInstalled;
            final boolean stickerPackInstalled = DataQuery.getInstance(this.currentAccount).isStickerPackInstalled(stickersSet.set.id);
            this.isInstalled = stickerPackInstalled;
            if (stickerPackInstalled) {
                if (!isInstalled) {
                    this.checkImage.setVisibility(0);
                    this.addButton.setClickable(false);
                    (this.currentAnimation = new AnimatorSet()).setDuration(200L);
                    this.currentAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.addButton, "alpha", new float[] { 1.0f, 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.addButton, "scaleX", new float[] { 1.0f, 0.01f }), (Animator)ObjectAnimator.ofFloat((Object)this.addButton, "scaleY", new float[] { 1.0f, 0.01f }), (Animator)ObjectAnimator.ofFloat((Object)this.checkImage, "alpha", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.checkImage, "scaleX", new float[] { 0.01f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.checkImage, "scaleY", new float[] { 0.01f, 1.0f }) });
                    this.currentAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationCancel(final Animator obj) {
                            if (FeaturedStickerSetCell.this.currentAnimation != null && FeaturedStickerSetCell.this.currentAnimation.equals(obj)) {
                                FeaturedStickerSetCell.this.currentAnimation = null;
                            }
                        }
                        
                        public void onAnimationEnd(final Animator obj) {
                            if (FeaturedStickerSetCell.this.currentAnimation != null && FeaturedStickerSetCell.this.currentAnimation.equals(obj)) {
                                FeaturedStickerSetCell.this.addButton.setVisibility(4);
                            }
                        }
                    });
                    this.currentAnimation.start();
                }
            }
            else if (isInstalled) {
                this.addButton.setVisibility(0);
                this.addButton.setClickable(true);
                (this.currentAnimation = new AnimatorSet()).setDuration(200L);
                this.currentAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.checkImage, "alpha", new float[] { 1.0f, 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.checkImage, "scaleX", new float[] { 1.0f, 0.01f }), (Animator)ObjectAnimator.ofFloat((Object)this.checkImage, "scaleY", new float[] { 1.0f, 0.01f }), (Animator)ObjectAnimator.ofFloat((Object)this.addButton, "alpha", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.addButton, "scaleX", new float[] { 0.01f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.addButton, "scaleY", new float[] { 0.01f, 1.0f }) });
                this.currentAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationCancel(final Animator obj) {
                        if (FeaturedStickerSetCell.this.currentAnimation != null && FeaturedStickerSetCell.this.currentAnimation.equals(obj)) {
                            FeaturedStickerSetCell.this.currentAnimation = null;
                        }
                    }
                    
                    public void onAnimationEnd(final Animator obj) {
                        if (FeaturedStickerSetCell.this.currentAnimation != null && FeaturedStickerSetCell.this.currentAnimation.equals(obj)) {
                            FeaturedStickerSetCell.this.checkImage.setVisibility(4);
                        }
                    }
                });
                this.currentAnimation.start();
            }
        }
        else {
            final boolean stickerPackInstalled2 = DataQuery.getInstance(this.currentAccount).isStickerPackInstalled(stickersSet.set.id);
            this.isInstalled = stickerPackInstalled2;
            if (stickerPackInstalled2) {
                this.addButton.setVisibility(4);
                this.addButton.setClickable(false);
                this.checkImage.setVisibility(0);
                this.checkImage.setScaleX(1.0f);
                this.checkImage.setScaleY(1.0f);
                this.checkImage.setAlpha(1.0f);
            }
            else {
                this.addButton.setVisibility(0);
                this.addButton.setClickable(true);
                this.checkImage.setVisibility(4);
                this.addButton.setScaleX(1.0f);
                this.addButton.setScaleY(1.0f);
                this.addButton.setAlpha(1.0f);
            }
        }
    }
}
