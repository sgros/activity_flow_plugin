// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.FileLoader;
import android.os.SystemClock;
import android.graphics.Canvas;
import org.telegram.messenger.ImageLocation;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.view.View$OnClickListener;
import android.widget.ImageView$ScaleType;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.graphics.Paint$Style;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.LinearLayout;
import android.graphics.Paint;
import android.widget.LinearLayout$LayoutParams;
import android.widget.HorizontalScrollView;

public class ScrollSlidingTabStrip extends HorizontalScrollView
{
    private boolean animateFromPosition;
    private int currentPosition;
    private LinearLayout$LayoutParams defaultExpandLayoutParams;
    private LinearLayout$LayoutParams defaultTabLayoutParams;
    private ScrollSlidingTabStripDelegate delegate;
    private int dividerPadding;
    private int indicatorColor;
    private int indicatorHeight;
    private long lastAnimationTime;
    private int lastScrollX;
    private float positionAnimationProgress;
    private Paint rectPaint;
    private int scrollOffset;
    private boolean shouldExpand;
    private float startAnimationPosition;
    private int tabCount;
    private int tabPadding;
    private LinearLayout tabsContainer;
    private int underlineColor;
    private int underlineHeight;
    
    public ScrollSlidingTabStrip(final Context context) {
        super(context);
        this.indicatorColor = -10066330;
        this.underlineColor = 436207616;
        this.scrollOffset = AndroidUtilities.dp(52.0f);
        this.underlineHeight = AndroidUtilities.dp(2.0f);
        this.dividerPadding = AndroidUtilities.dp(12.0f);
        this.tabPadding = AndroidUtilities.dp(24.0f);
        this.lastScrollX = 0;
        this.setFillViewport(true);
        this.setWillNotDraw(false);
        this.setHorizontalScrollBarEnabled(false);
        (this.tabsContainer = new LinearLayout(context)).setOrientation(0);
        this.tabsContainer.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
        this.addView((View)this.tabsContainer);
        (this.rectPaint = new Paint()).setAntiAlias(true);
        this.rectPaint.setStyle(Paint$Style.FILL);
        this.defaultTabLayoutParams = new LinearLayout$LayoutParams(AndroidUtilities.dp(52.0f), -1);
        this.defaultExpandLayoutParams = new LinearLayout$LayoutParams(0, -1, 1.0f);
    }
    
    private void scrollToChild(int scrollX) {
        if (this.tabCount != 0) {
            if (this.tabsContainer.getChildAt(scrollX) != null) {
                int left;
                final int n = left = this.tabsContainer.getChildAt(scrollX).getLeft();
                if (scrollX > 0) {
                    left = n - this.scrollOffset;
                }
                scrollX = this.getScrollX();
                if (left != this.lastScrollX) {
                    if (left < scrollX) {
                        this.smoothScrollTo(this.lastScrollX = left, 0);
                    }
                    else if (this.scrollOffset + left > scrollX + this.getWidth() - this.scrollOffset * 2) {
                        this.smoothScrollTo(this.lastScrollX = left - this.getWidth() + this.scrollOffset * 3, 0);
                    }
                }
            }
        }
    }
    
    public ImageView addIconTab(final Drawable imageDrawable) {
        final int n = this.tabCount++;
        final ImageView imageView = new ImageView(this.getContext());
        boolean selected = true;
        imageView.setFocusable(true);
        imageView.setImageDrawable(imageDrawable);
        imageView.setScaleType(ImageView$ScaleType.CENTER);
        imageView.setOnClickListener((View$OnClickListener)new _$$Lambda$ScrollSlidingTabStrip$_7_oyaI_rm6dAg9Sbg63YJVLh4k(this, n));
        this.tabsContainer.addView((View)imageView);
        if (n != this.currentPosition) {
            selected = false;
        }
        imageView.setSelected(selected);
        return imageView;
    }
    
    public TextView addIconTabWithCounter(final Drawable imageDrawable) {
        final int n = this.tabCount++;
        final FrameLayout frameLayout = new FrameLayout(this.getContext());
        frameLayout.setFocusable(true);
        this.tabsContainer.addView((View)frameLayout);
        final ImageView imageView = new ImageView(this.getContext());
        imageView.setImageDrawable(imageDrawable);
        imageView.setScaleType(ImageView$ScaleType.CENTER);
        frameLayout.setOnClickListener((View$OnClickListener)new _$$Lambda$ScrollSlidingTabStrip$mNTU5l0eITNwV9vJo7_FdGx3xIs(this, n));
        frameLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        frameLayout.setSelected(n == this.currentPosition);
        final TextView textView = new TextView(this.getContext());
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setTextSize(1, 12.0f);
        textView.setTextColor(Theme.getColor("chat_emojiPanelBadgeText"));
        textView.setGravity(17);
        textView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(9.0f), Theme.getColor("chat_emojiPanelBadgeBackground")));
        textView.setMinWidth(AndroidUtilities.dp(18.0f));
        textView.setPadding(AndroidUtilities.dp(5.0f), 0, AndroidUtilities.dp(5.0f), AndroidUtilities.dp(1.0f));
        frameLayout.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 18.0f, 51, 26.0f, 6.0f, 0.0f, 0.0f));
        return textView;
    }
    
    public View addStickerTab(final TLObject tag, final TLRPC.Document document, final Object o) {
        final int n = this.tabCount++;
        final FrameLayout frameLayout = new FrameLayout(this.getContext());
        frameLayout.setTag((Object)tag);
        frameLayout.setTag(2131230860, o);
        frameLayout.setTag(2131230858, (Object)document);
        frameLayout.setFocusable(true);
        frameLayout.setOnClickListener((View$OnClickListener)new _$$Lambda$ScrollSlidingTabStrip$VOvZofAy7NE0HRe6ap1qMeWlOr0(this, n));
        this.tabsContainer.addView((View)frameLayout);
        frameLayout.setSelected(n == this.currentPosition);
        final BackupImageView backupImageView = new BackupImageView(this.getContext());
        backupImageView.setAspectFit(true);
        frameLayout.addView((View)backupImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(30, 30, 17));
        return (View)frameLayout;
    }
    
    public void addStickerTab(final TLRPC.Chat info) {
        final int n = this.tabCount++;
        final FrameLayout frameLayout = new FrameLayout(this.getContext());
        frameLayout.setFocusable(true);
        frameLayout.setOnClickListener((View$OnClickListener)new _$$Lambda$ScrollSlidingTabStrip$CxqiesXBMvO9L7nEGHWQr5LTQDY(this, n));
        this.tabsContainer.addView((View)frameLayout);
        frameLayout.setSelected(n == this.currentPosition);
        final BackupImageView backupImageView = new BackupImageView(this.getContext());
        backupImageView.setRoundRadius(AndroidUtilities.dp(15.0f));
        final AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setTextSize(AndroidUtilities.dp(14.0f));
        avatarDrawable.setInfo(info);
        backupImageView.setImage(ImageLocation.getForChat(info, false), "50_50", avatarDrawable, info);
        backupImageView.setAspectFit(true);
        frameLayout.addView((View)backupImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(30, 30, 17));
    }
    
    public void cancelPositionAnimation() {
        this.animateFromPosition = false;
        this.positionAnimationProgress = 1.0f;
    }
    
    public int getCurrentPosition() {
        return this.currentPosition;
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (!this.isInEditMode()) {
            if (this.tabCount != 0) {
                final int height = this.getHeight();
                if (this.underlineHeight > 0) {
                    this.rectPaint.setColor(this.underlineColor);
                    canvas.drawRect(0.0f, (float)(height - this.underlineHeight), (float)this.tabsContainer.getWidth(), (float)height, this.rectPaint);
                }
                if (this.indicatorHeight >= 0) {
                    final View child = this.tabsContainer.getChildAt(this.currentPosition);
                    float n = 0.0f;
                    int measuredWidth;
                    if (child != null) {
                        n = (float)child.getLeft();
                        measuredWidth = child.getMeasuredWidth();
                    }
                    else {
                        measuredWidth = 0;
                    }
                    float n2 = n;
                    if (this.animateFromPosition) {
                        final long uptimeMillis = SystemClock.uptimeMillis();
                        final long lastAnimationTime = this.lastAnimationTime;
                        this.lastAnimationTime = uptimeMillis;
                        this.positionAnimationProgress += (uptimeMillis - lastAnimationTime) / 150.0f;
                        if (this.positionAnimationProgress >= 1.0f) {
                            this.positionAnimationProgress = 1.0f;
                            this.animateFromPosition = false;
                        }
                        final float startAnimationPosition = this.startAnimationPosition;
                        n2 = (n - startAnimationPosition) * CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.positionAnimationProgress) + startAnimationPosition;
                        this.invalidate();
                    }
                    this.rectPaint.setColor(this.indicatorColor);
                    final int indicatorHeight = this.indicatorHeight;
                    if (indicatorHeight == 0) {
                        canvas.drawRect(n2, 0.0f, n2 + measuredWidth, (float)height, this.rectPaint);
                    }
                    else {
                        canvas.drawRect(n2, (float)(height - indicatorHeight), n2 + measuredWidth, (float)height, this.rectPaint);
                    }
                }
            }
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.setImages();
    }
    
    public void onPageScrolled(final int currentPosition, final int n) {
        final int currentPosition2 = this.currentPosition;
        if (currentPosition2 == currentPosition) {
            return;
        }
        final View child = this.tabsContainer.getChildAt(currentPosition2);
        if (child != null) {
            this.startAnimationPosition = (float)child.getLeft();
            this.positionAnimationProgress = 0.0f;
            this.animateFromPosition = true;
            this.lastAnimationTime = SystemClock.uptimeMillis();
        }
        else {
            this.animateFromPosition = false;
        }
        this.currentPosition = currentPosition;
        if (currentPosition >= this.tabsContainer.getChildCount()) {
            return;
        }
        this.positionAnimationProgress = 0.0f;
        for (int i = 0; i < this.tabsContainer.getChildCount(); ++i) {
            this.tabsContainer.getChildAt(i).setSelected(i == currentPosition);
        }
        if (n == currentPosition && currentPosition > 1) {
            this.scrollToChild(currentPosition - 1);
        }
        else {
            this.scrollToChild(currentPosition);
        }
        this.invalidate();
    }
    
    protected void onScrollChanged(int i, int n, int n2, int min) {
        super.onScrollChanged(i, n, n2, min);
        final int dp = AndroidUtilities.dp(52.0f);
        min = n2 / dp;
        n = i / dp;
        n2 = (int)Math.ceil(this.getMeasuredWidth() / (float)dp) + 1;
        View child;
        Object tag;
        Object tag2;
        TLRPC.Document document;
        ImageLocation imageLocation;
        BackupImageView backupImageView;
        for (i = Math.max(0, Math.min(min, n)), min = Math.min(this.tabsContainer.getChildCount(), Math.max(min, n) + n2); i < min; ++i) {
            child = this.tabsContainer.getChildAt(i);
            if (child != null) {
                tag = child.getTag();
                tag2 = child.getTag(2131230860);
                document = (TLRPC.Document)child.getTag(2131230858);
                if (tag instanceof TLRPC.Document) {
                    imageLocation = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90), document);
                }
                else {
                    if (!(tag instanceof TLRPC.PhotoSize)) {
                        continue;
                    }
                    imageLocation = ImageLocation.getForSticker((TLRPC.PhotoSize)tag, document);
                }
                backupImageView = (BackupImageView)((FrameLayout)child).getChildAt(0);
                if (i >= n && i < n + n2) {
                    backupImageView.setImage(imageLocation, null, "webp", null, tag2);
                }
                else {
                    backupImageView.setImageDrawable(null);
                }
            }
        }
    }
    
    public void removeTabs() {
        this.tabsContainer.removeAllViews();
        this.tabCount = 0;
        this.currentPosition = 0;
        this.animateFromPosition = false;
    }
    
    public void selectTab(final int n) {
        if (n >= 0) {
            if (n < this.tabCount) {
                this.tabsContainer.getChildAt(n).performClick();
            }
        }
    }
    
    public void setDelegate(final ScrollSlidingTabStripDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setImages() {
        for (int dp = AndroidUtilities.dp(52.0f), i = this.getScrollX() / dp; i < Math.min(this.tabsContainer.getChildCount(), (int)Math.ceil(this.getMeasuredWidth() / (float)dp) + i + 1); ++i) {
            final View child = this.tabsContainer.getChildAt(i);
            final Object tag = child.getTag();
            final Object tag2 = child.getTag(2131230860);
            final TLRPC.Document document = (TLRPC.Document)child.getTag(2131230858);
            ImageLocation imageLocation;
            if (tag instanceof TLRPC.Document) {
                imageLocation = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90), document);
            }
            else {
                if (!(tag instanceof TLRPC.PhotoSize)) {
                    continue;
                }
                imageLocation = ImageLocation.getForSticker((TLRPC.PhotoSize)tag, document);
            }
            ((BackupImageView)((FrameLayout)child).getChildAt(0)).setImage(imageLocation, null, "webp", null, tag2);
        }
    }
    
    public void setIndicatorColor(final int indicatorColor) {
        this.indicatorColor = indicatorColor;
        this.invalidate();
    }
    
    public void setIndicatorHeight(final int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
        this.invalidate();
    }
    
    public void setShouldExpand(final boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        this.requestLayout();
    }
    
    public void setUnderlineColor(final int underlineColor) {
        this.underlineColor = underlineColor;
        this.invalidate();
    }
    
    public void setUnderlineColorResource(final int n) {
        this.underlineColor = this.getResources().getColor(n);
        this.invalidate();
    }
    
    public void setUnderlineHeight(final int underlineHeight) {
        this.underlineHeight = underlineHeight;
        this.invalidate();
    }
    
    public void updateTabStyles() {
        for (int i = 0; i < this.tabCount; ++i) {
            final View child = this.tabsContainer.getChildAt(i);
            if (this.shouldExpand) {
                child.setLayoutParams((ViewGroup$LayoutParams)this.defaultExpandLayoutParams);
            }
            else {
                child.setLayoutParams((ViewGroup$LayoutParams)this.defaultTabLayoutParams);
            }
        }
    }
    
    public interface ScrollSlidingTabStripDelegate
    {
        void onPageSelected(final int p0);
    }
}
