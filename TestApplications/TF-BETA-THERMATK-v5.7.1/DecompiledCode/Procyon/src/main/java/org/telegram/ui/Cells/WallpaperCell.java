// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import java.io.File;
import org.telegram.messenger.MediaController;
import android.graphics.Bitmap;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.FileLoader;
import org.telegram.tgnet.TLRPC;
import android.graphics.ColorFilter;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Build$VERSION;
import android.view.MotionEvent;
import android.widget.ImageView$ScaleType;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.WallpapersListActivity;
import android.graphics.Canvas;
import android.widget.ImageView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox;
import android.animation.AnimatorSet;
import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.view.View$OnLongClickListener;
import android.view.View$OnClickListener;
import android.view.View;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import android.widget.FrameLayout;

public class WallpaperCell extends FrameLayout
{
    private Paint backgroundPaint;
    private Drawable checkDrawable;
    private Paint circlePaint;
    private int currentType;
    private Paint framePaint;
    private boolean isBottom;
    private boolean isTop;
    private int spanCount;
    private WallpaperView[] wallpaperViews;
    
    public WallpaperCell(final Context context) {
        super(context);
        this.spanCount = 3;
        this.wallpaperViews = new WallpaperView[5];
        int n = 0;
        while (true) {
            final WallpaperView[] wallpaperViews = this.wallpaperViews;
            if (n >= wallpaperViews.length) {
                break;
            }
            final WallpaperView wallpaperView = new WallpaperView(context);
            this.addView((View)(wallpaperViews[n] = wallpaperView));
            wallpaperView.setOnClickListener((View$OnClickListener)new _$$Lambda$WallpaperCell$fnKXvqmCCrXx3zL9kUDMI1LGdcU(this, wallpaperView, n));
            wallpaperView.setOnLongClickListener((View$OnLongClickListener)new _$$Lambda$WallpaperCell$KQeRkjIAB8SBAWriFhep5_TPLbU(this, wallpaperView, n));
            ++n;
        }
        (this.framePaint = new Paint()).setColor(855638016);
        this.circlePaint = new Paint(1);
        this.checkDrawable = context.getResources().getDrawable(2131165300).mutate();
        (this.backgroundPaint = new Paint()).setColor(Theme.getColor("sharedMedia_photoPlaceholder"));
    }
    
    public void invalidate() {
        super.invalidate();
        for (int i = 0; i < this.spanCount; ++i) {
            this.wallpaperViews[i].invalidate();
        }
    }
    
    protected void onLayout(final boolean b, int dp, int i, int dp2, int measuredWidth) {
        dp2 = AndroidUtilities.dp(14.0f);
        final boolean isTop = this.isTop;
        i = 0;
        if (isTop) {
            dp = AndroidUtilities.dp(14.0f);
        }
        else {
            dp = 0;
        }
        while (i < this.spanCount) {
            measuredWidth = this.wallpaperViews[i].getMeasuredWidth();
            final WallpaperView[] wallpaperViews = this.wallpaperViews;
            wallpaperViews[i].layout(dp2, dp, dp2 + measuredWidth, wallpaperViews[i].getMeasuredHeight() + dp);
            dp2 += measuredWidth + AndroidUtilities.dp(6.0f);
            ++i;
        }
    }
    
    protected void onMeasure(int dp, int n) {
        final int size = View$MeasureSpec.getSize(dp);
        final int n2 = size - AndroidUtilities.dp((float)((this.spanCount - 1) * 6 + 28));
        n = n2 / this.spanCount;
        int dp2;
        if (this.currentType == 0) {
            dp2 = AndroidUtilities.dp(180.0f);
        }
        else {
            dp2 = n;
        }
        final boolean isTop = this.isTop;
        float n3 = 14.0f;
        final int n4 = 0;
        if (isTop) {
            dp = AndroidUtilities.dp(14.0f);
        }
        else {
            dp = 0;
        }
        if (!this.isBottom) {
            n3 = 6.0f;
        }
        this.setMeasuredDimension(size, dp + dp2 + AndroidUtilities.dp(n3));
        dp = n2;
        int n5 = n4;
        while (true) {
            final int spanCount = this.spanCount;
            if (n5 >= spanCount) {
                break;
            }
            final WallpaperView wallpaperView = this.wallpaperViews[n5];
            int n6;
            if (n5 == spanCount - 1) {
                n6 = dp;
            }
            else {
                n6 = n;
            }
            wallpaperView.measure(View$MeasureSpec.makeMeasureSpec(n6, 1073741824), View$MeasureSpec.makeMeasureSpec(dp2, 1073741824));
            dp -= n;
            ++n5;
        }
    }
    
    protected void onWallpaperClick(final Object o, final int n) {
    }
    
    protected boolean onWallpaperLongClick(final Object o, final int n) {
        return false;
    }
    
    public void setChecked(final int n, final boolean b, final boolean b2) {
        this.wallpaperViews[n].setChecked(b, b2);
    }
    
    public void setParams(final int spanCount, final boolean isTop, final boolean isBottom) {
        this.spanCount = spanCount;
        this.isTop = isTop;
        this.isBottom = isBottom;
        int n = 0;
        while (true) {
            final WallpaperView[] wallpaperViews = this.wallpaperViews;
            if (n >= wallpaperViews.length) {
                break;
            }
            final WallpaperView wallpaperView = wallpaperViews[n];
            int visibility;
            if (n < spanCount) {
                visibility = 0;
            }
            else {
                visibility = 8;
            }
            wallpaperView.setVisibility(visibility);
            this.wallpaperViews[n].clearAnimation();
            ++n;
        }
    }
    
    public void setWallpaper(final int currentType, final int n, final Object o, final long n2, final Drawable drawable, final boolean b) {
        this.currentType = currentType;
        if (o == null) {
            this.wallpaperViews[n].setVisibility(8);
            this.wallpaperViews[n].clearAnimation();
        }
        else {
            this.wallpaperViews[n].setVisibility(0);
            this.wallpaperViews[n].setWallpaper(o, n2, drawable, b);
        }
    }
    
    private class WallpaperView extends FrameLayout
    {
        private AnimatorSet animator;
        private AnimatorSet animatorSet;
        private CheckBox checkBox;
        private Object currentWallpaper;
        private BackupImageView imageView;
        private ImageView imageView2;
        private boolean isSelected;
        private View selector;
        
        public WallpaperView(final Context context) {
            super(context);
            this.setWillNotDraw(false);
            this.addView((View)(this.imageView = new BackupImageView(context) {
                @Override
                protected void onDraw(final Canvas canvas) {
                    super.onDraw(canvas);
                    if (WallpaperView.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                        canvas.drawLine(1.0f, 0.0f, (float)(this.getMeasuredWidth() - 1), 0.0f, WallpaperCell.this.framePaint);
                        canvas.drawLine(0.0f, 0.0f, 0.0f, (float)this.getMeasuredHeight(), WallpaperCell.this.framePaint);
                        canvas.drawLine((float)(this.getMeasuredWidth() - 1), 0.0f, (float)(this.getMeasuredWidth() - 1), (float)this.getMeasuredHeight(), WallpaperCell.this.framePaint);
                        canvas.drawLine(1.0f, (float)(this.getMeasuredHeight() - 1), (float)(this.getMeasuredWidth() - 1), (float)(this.getMeasuredHeight() - 1), WallpaperCell.this.framePaint);
                    }
                    if (WallpaperView.this.isSelected) {
                        WallpaperCell.this.circlePaint.setColor(Theme.serviceMessageColorBackup);
                        final int n = this.getMeasuredWidth() / 2;
                        final int n2 = this.getMeasuredHeight() / 2;
                        canvas.drawCircle((float)n, (float)n2, (float)AndroidUtilities.dp(20.0f), WallpaperCell.this.circlePaint);
                        WallpaperCell.this.checkDrawable.setBounds(n - WallpaperCell.this.checkDrawable.getIntrinsicWidth() / 2, n2 - WallpaperCell.this.checkDrawable.getIntrinsicHeight() / 2, n + WallpaperCell.this.checkDrawable.getIntrinsicWidth() / 2, n2 + WallpaperCell.this.checkDrawable.getIntrinsicHeight() / 2);
                        WallpaperCell.this.checkDrawable.draw(canvas);
                    }
                }
            }), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
            (this.imageView2 = new ImageView(context)).setImageResource(2131165443);
            this.imageView2.setScaleType(ImageView$ScaleType.CENTER);
            this.addView((View)this.imageView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
            (this.selector = new View(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.addView(this.selector, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            (this.checkBox = new CheckBox(context, 2131165802)).setVisibility(4);
            this.checkBox.setColor(Theme.getColor("checkbox"), Theme.getColor("checkboxCheck"));
            this.addView((View)this.checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(22, 22.0f, 53, 0.0f, 2.0f, 2.0f, 0.0f));
        }
        
        public void clearAnimation() {
            super.clearAnimation();
            final AnimatorSet animator = this.animator;
            if (animator != null) {
                animator.cancel();
                this.animator = null;
            }
        }
        
        public void invalidate() {
            super.invalidate();
            this.imageView.invalidate();
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.checkBox.isChecked() || !this.imageView.getImageReceiver().hasBitmapImage() || this.imageView.getImageReceiver().getCurrentAlpha() != 1.0f) {
                canvas.drawRect(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), WallpaperCell.this.backgroundPaint);
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
            float scaleY = 0.8875f;
            if (b2) {
                this.animator = new AnimatorSet();
                final AnimatorSet animator2 = this.animator;
                final BackupImageView imageView = this.imageView;
                float n;
                if (b) {
                    n = 0.8875f;
                }
                else {
                    n = 1.0f;
                }
                final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)imageView, "scaleX", new float[] { n });
                final BackupImageView imageView2 = this.imageView;
                if (!b) {
                    scaleY = 1.0f;
                }
                animator2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ObjectAnimator.ofFloat((Object)imageView2, "scaleY", new float[] { scaleY }) });
                this.animator.setDuration(200L);
                this.animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationCancel(final Animator obj) {
                        if (WallpaperView.this.animator != null && WallpaperView.this.animator.equals(obj)) {
                            WallpaperView.this.animator = null;
                        }
                    }
                    
                    public void onAnimationEnd(final Animator obj) {
                        if (WallpaperView.this.animator != null && WallpaperView.this.animator.equals(obj)) {
                            WallpaperView.this.animator = null;
                            if (!b) {
                                WallpaperView.this.setBackgroundColor(0);
                            }
                        }
                    }
                });
                this.animator.start();
            }
            else {
                final BackupImageView imageView3 = this.imageView;
                float scaleX;
                if (b) {
                    scaleX = 0.8875f;
                }
                else {
                    scaleX = 1.0f;
                }
                imageView3.setScaleX(scaleX);
                final BackupImageView imageView4 = this.imageView;
                if (!b) {
                    scaleY = 1.0f;
                }
                imageView4.setScaleY(scaleY);
            }
            this.invalidate();
        }
        
        public void setWallpaper(final Object currentWallpaper, final long n, final Drawable imageDrawable, final boolean b) {
            this.currentWallpaper = currentWallpaper;
            final boolean b2 = false;
            final boolean b3 = false;
            final boolean b4 = false;
            if (currentWallpaper == null) {
                this.imageView.setVisibility(4);
                this.imageView2.setVisibility(0);
                if (b) {
                    this.imageView2.setImageDrawable(imageDrawable);
                    this.imageView2.setScaleType(ImageView$ScaleType.CENTER_CROP);
                }
                else {
                    final ImageView imageView2 = this.imageView2;
                    int backgroundColor;
                    if (n != -1L && n != 1000001L) {
                        backgroundColor = 1509949440;
                    }
                    else {
                        backgroundColor = 1514625126;
                    }
                    imageView2.setBackgroundColor(backgroundColor);
                    this.imageView2.setScaleType(ImageView$ScaleType.CENTER);
                    this.imageView2.setImageResource(2131165443);
                }
            }
            else {
                this.imageView.setVisibility(0);
                this.imageView2.setVisibility(4);
                final BackupImageView imageView3 = this.imageView;
                final TLRPC.PhotoSize photoSize = null;
                imageView3.setBackgroundDrawable((Drawable)null);
                this.imageView.getImageReceiver().setColorFilter(null);
                this.imageView.getImageReceiver().setAlpha(1.0f);
                if (currentWallpaper instanceof TLRPC.TL_wallPaper) {
                    final TLRPC.TL_wallPaper tl_wallPaper = (TLRPC.TL_wallPaper)currentWallpaper;
                    boolean isSelected = b4;
                    if (tl_wallPaper.id == n) {
                        isSelected = true;
                    }
                    this.isSelected = isSelected;
                    final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tl_wallPaper.document.thumbs, 100);
                    TLRPC.PhotoSize closestPhotoSizeWithSize2;
                    if ((closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tl_wallPaper.document.thumbs, 320)) == closestPhotoSizeWithSize) {
                        closestPhotoSizeWithSize2 = null;
                    }
                    int n2;
                    if (closestPhotoSizeWithSize2 != null) {
                        n2 = closestPhotoSizeWithSize2.size;
                    }
                    else {
                        n2 = tl_wallPaper.document.size;
                    }
                    if (tl_wallPaper.pattern) {
                        this.imageView.setBackgroundColor(tl_wallPaper.settings.background_color | 0xFF000000);
                        this.imageView.setImage(ImageLocation.getForDocument(closestPhotoSizeWithSize2, tl_wallPaper.document), "100_100", ImageLocation.getForDocument(closestPhotoSizeWithSize, tl_wallPaper.document), null, "jpg", n2, 1, tl_wallPaper);
                        this.imageView.getImageReceiver().setColorFilter((ColorFilter)new PorterDuffColorFilter(AndroidUtilities.getPatternColor(tl_wallPaper.settings.background_color), PorterDuff$Mode.SRC_IN));
                        this.imageView.getImageReceiver().setAlpha(tl_wallPaper.settings.intensity / 100.0f);
                    }
                    else if (closestPhotoSizeWithSize2 != null) {
                        this.imageView.setImage(ImageLocation.getForDocument(closestPhotoSizeWithSize2, tl_wallPaper.document), "100_100", ImageLocation.getForDocument(closestPhotoSizeWithSize, tl_wallPaper.document), "100_100_b", "jpg", n2, 1, tl_wallPaper);
                    }
                    else {
                        this.imageView.setImage(ImageLocation.getForDocument(tl_wallPaper.document), "100_100", ImageLocation.getForDocument(closestPhotoSizeWithSize, tl_wallPaper.document), "100_100_b", "jpg", n2, 1, tl_wallPaper);
                    }
                }
                else if (currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                    final WallpapersListActivity.ColorWallpaper colorWallpaper = (WallpapersListActivity.ColorWallpaper)currentWallpaper;
                    final File path = colorWallpaper.path;
                    if (path != null) {
                        this.imageView.setImage(path.getAbsolutePath(), "100_100", null);
                    }
                    else {
                        this.imageView.setImageBitmap(null);
                        this.imageView.setBackgroundColor(colorWallpaper.color | 0xFF000000);
                    }
                    boolean isSelected2 = b2;
                    if (colorWallpaper.id == n) {
                        isSelected2 = true;
                    }
                    this.isSelected = isSelected2;
                }
                else if (currentWallpaper instanceof WallpapersListActivity.FileWallpaper) {
                    final WallpapersListActivity.FileWallpaper fileWallpaper = (WallpapersListActivity.FileWallpaper)currentWallpaper;
                    boolean isSelected3 = b3;
                    if (fileWallpaper.id == n) {
                        isSelected3 = true;
                    }
                    this.isSelected = isSelected3;
                    final File originalPath = fileWallpaper.originalPath;
                    if (originalPath != null) {
                        this.imageView.setImage(originalPath.getAbsolutePath(), "100_100", null);
                    }
                    else {
                        final File path2 = fileWallpaper.path;
                        if (path2 != null) {
                            this.imageView.setImage(path2.getAbsolutePath(), "100_100", null);
                        }
                        else if (fileWallpaper.resId == -2L) {
                            this.imageView.setImageDrawable(Theme.getThemedWallpaper(true));
                        }
                        else {
                            this.imageView.setImageResource(fileWallpaper.thumbResId);
                        }
                    }
                }
                else if (currentWallpaper instanceof MediaController.SearchImage) {
                    final MediaController.SearchImage searchImage = (MediaController.SearchImage)currentWallpaper;
                    final TLRPC.Photo photo = searchImage.photo;
                    if (photo != null) {
                        final TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 100);
                        TLRPC.PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(searchImage.photo.sizes, 320);
                        if (closestPhotoSizeWithSize4 == closestPhotoSizeWithSize3) {
                            closestPhotoSizeWithSize4 = photoSize;
                        }
                        int size;
                        if (closestPhotoSizeWithSize4 != null) {
                            size = closestPhotoSizeWithSize4.size;
                        }
                        else {
                            size = 0;
                        }
                        this.imageView.setImage(ImageLocation.getForPhoto(closestPhotoSizeWithSize4, searchImage.photo), "100_100", ImageLocation.getForPhoto(closestPhotoSizeWithSize3, searchImage.photo), "100_100_b", "jpg", size, 1, searchImage);
                    }
                    else {
                        this.imageView.setImage(searchImage.thumbUrl, "100_100", null);
                    }
                }
                else {
                    this.isSelected = false;
                }
            }
        }
    }
}
