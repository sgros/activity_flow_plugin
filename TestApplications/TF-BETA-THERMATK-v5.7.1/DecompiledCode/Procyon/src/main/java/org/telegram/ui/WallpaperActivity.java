// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.ui.Cells.BaseCell;
import org.telegram.ui.Cells.ChatActionCell$ChatActionCellDelegate$_CC;
import android.text.style.CharacterStyle;
import org.telegram.ui.Cells.ChatMessageCell$ChatMessageCellDelegate$_CC;
import android.view.ViewGroup;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.messenger.UserConfig;
import android.view.KeyEvent;
import android.graphics.Shader;
import android.graphics.ComposeShader;
import android.graphics.RadialGradient;
import android.graphics.Shader$TileMode;
import android.graphics.SweepGradient;
import android.widget.TextView$OnEditorActionListener;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.InputFilter$LengthFilter;
import android.text.InputFilter;
import android.widget.LinearLayout;
import android.graphics.LinearGradient;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.view.View$MeasureSpec;
import org.telegram.ui.Components.AnimationProperties;
import android.graphics.RectF;
import android.graphics.Point;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Paint$Cap;
import android.graphics.Paint$Style;
import android.content.SharedPreferences$Editor;
import android.graphics.Bitmap$Config;
import org.telegram.messenger.FileLog;
import java.io.OutputStream;
import android.graphics.Bitmap$CompressFormat;
import java.io.FileOutputStream;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.FrameLayout$LayoutParams;
import android.view.View$OnClickListener;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.graphics.Canvas;
import android.app.Dialog;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.ActionBar;
import android.content.Context;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.ImageLoader;
import android.text.TextUtils;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.Utilities;
import android.util.Property;
import java.util.Collection;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.FileLoader;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import org.telegram.ui.Components.CubicBezierInterpolator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.animation.Animator;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import org.telegram.ui.Components.RadialProgress2;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import org.telegram.ui.Components.WallpaperParallaxEffect;
import android.animation.AnimatorSet;
import org.telegram.tgnet.TLRPC;
import java.io.File;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBarView;
import org.telegram.ui.Cells.HeaderCell;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.graphics.Bitmap;
import android.graphics.PorterDuff$Mode;
import android.graphics.Paint;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.DownloadController;
import org.telegram.ui.ActionBar.BaseFragment;

public class WallpaperActivity extends BaseFragment implements FileDownloadProgressListener, NotificationCenterDelegate
{
    private static final int share_item = 1;
    private int TAG;
    private int backgroundColor;
    private BackupImageView backgroundImage;
    private Paint backgroundPaint;
    private PorterDuff$Mode blendMode;
    private Bitmap blurredBitmap;
    private FrameLayout bottomOverlayChat;
    private TextView bottomOverlayChatText;
    private FrameLayout buttonsContainer;
    private CheckBoxView[] checkBoxView;
    private Paint checkPaint;
    private ColorPicker colorPicker;
    private float currentIntensity;
    private Object currentWallpaper;
    private Bitmap currentWallpaperBitmap;
    private WallpaperActivityDelegate delegate;
    private Paint eraserPaint;
    private String imageFilter;
    private HeaderCell intensityCell;
    private SeekBarView intensitySeekBar;
    private boolean isBlurred;
    private boolean isMotion;
    private RecyclerListView listView;
    private String loadingFile;
    private File loadingFileObject;
    private TLRPC.PhotoSize loadingSize;
    private int maxWallpaperSize;
    private AnimatorSet motionAnimation;
    private WallpaperParallaxEffect parallaxEffect;
    private float parallaxScale;
    private int patternColor;
    private FrameLayout[] patternLayout;
    private ArrayList<Object> patterns;
    private PatternsAdapter patternsAdapter;
    private FrameLayout[] patternsButtonsContainer;
    private TextView[] patternsCancelButton;
    private LinearLayoutManager patternsLayoutManager;
    private RecyclerListView patternsListView;
    private TextView[] patternsSaveButton;
    private int previousBackgroundColor;
    private float previousIntensity;
    private TLRPC.TL_wallPaper previousSelectedPattern;
    private boolean progressVisible;
    private RadialProgress2 radialProgress;
    private TLRPC.TL_wallPaper selectedPattern;
    private TextPaint textPaint;
    private Drawable themedWallpaper;
    private boolean viewCreated;
    
    public WallpaperActivity(Object currentWallpaper, final Bitmap currentWallpaperBitmap) {
        this.patternLayout = new FrameLayout[3];
        this.patternsCancelButton = new TextView[2];
        this.patternsSaveButton = new TextView[2];
        this.patternsButtonsContainer = new FrameLayout[2];
        this.currentIntensity = 0.4f;
        this.blendMode = PorterDuff$Mode.SRC_IN;
        this.parallaxScale = 1.0f;
        this.loadingFile = null;
        this.loadingFileObject = null;
        this.loadingSize = null;
        this.imageFilter = "640_360";
        this.maxWallpaperSize = 1920;
        this.currentWallpaper = currentWallpaper;
        this.currentWallpaperBitmap = currentWallpaperBitmap;
        currentWallpaper = this.currentWallpaper;
        if (currentWallpaper instanceof TLRPC.TL_wallPaper) {
            final TLRPC.TL_wallPaper tl_wallPaper = (TLRPC.TL_wallPaper)currentWallpaper;
        }
        else if (currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
            final WallpapersListActivity.ColorWallpaper colorWallpaper = (WallpapersListActivity.ColorWallpaper)currentWallpaper;
            this.isMotion = colorWallpaper.motion;
            this.selectedPattern = colorWallpaper.pattern;
            if (this.selectedPattern != null) {
                this.currentIntensity = colorWallpaper.intensity;
            }
        }
    }
    
    private void animateMotionChange() {
        final AnimatorSet motionAnimation = this.motionAnimation;
        if (motionAnimation != null) {
            motionAnimation.cancel();
        }
        this.motionAnimation = new AnimatorSet();
        if (this.isMotion) {
            this.motionAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.backgroundImage, View.SCALE_X, new float[] { this.parallaxScale }), (Animator)ObjectAnimator.ofFloat((Object)this.backgroundImage, View.SCALE_Y, new float[] { this.parallaxScale }) });
        }
        else {
            this.motionAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.backgroundImage, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.backgroundImage, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.backgroundImage, View.TRANSLATION_X, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.backgroundImage, View.TRANSLATION_Y, new float[] { 0.0f }) });
        }
        this.motionAnimation.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
        this.motionAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                WallpaperActivity.this.motionAnimation = null;
            }
        });
        this.motionAnimation.start();
    }
    
    private void setBackgroundColor(int i) {
        this.backgroundColor = i;
        this.backgroundImage.setBackgroundColor(this.backgroundColor);
        final CheckBoxView[] checkBoxView = this.checkBoxView;
        i = 0;
        if (checkBoxView[0] != null) {
            checkBoxView[0].invalidate();
        }
        this.patternColor = AndroidUtilities.getPatternColor(this.backgroundColor);
        final int patternColor = this.patternColor;
        Theme.applyChatServiceMessageColor(new int[] { patternColor, patternColor, patternColor, patternColor });
        final BackupImageView backgroundImage = this.backgroundImage;
        if (backgroundImage != null) {
            backgroundImage.getImageReceiver().setColorFilter((ColorFilter)new PorterDuffColorFilter(this.patternColor, this.blendMode));
            this.backgroundImage.getImageReceiver().setAlpha(this.currentIntensity);
            this.backgroundImage.invalidate();
        }
        final RecyclerListView listView = this.listView;
        if (listView != null) {
            listView.invalidateViews();
        }
        final FrameLayout buttonsContainer = this.buttonsContainer;
        if (buttonsContainer != null) {
            while (i < buttonsContainer.getChildCount()) {
                this.buttonsContainer.getChildAt(i).invalidate();
                ++i;
            }
        }
        final RadialProgress2 radialProgress = this.radialProgress;
        if (radialProgress != null) {
            radialProgress.setColors("chat_serviceBackground", "chat_serviceBackground", "chat_serviceText", "chat_serviceText");
        }
    }
    
    private void setCurrentImage(final boolean b) {
        final Object currentWallpaper = this.currentWallpaper;
        final boolean b2 = currentWallpaper instanceof TLRPC.TL_wallPaper;
        TLRPC.PhotoSize closestPhotoSizeWithSize = null;
        if (b2) {
            final TLRPC.TL_wallPaper tl_wallPaper = (TLRPC.TL_wallPaper)currentWallpaper;
            if (b) {
                closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tl_wallPaper.document.thumbs, 100);
            }
            this.backgroundImage.setImage(ImageLocation.getForDocument(tl_wallPaper.document), this.imageFilter, ImageLocation.getForDocument(closestPhotoSizeWithSize, tl_wallPaper.document), "100_100_b", "jpg", tl_wallPaper.document.size, 1, tl_wallPaper);
        }
        else if (currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
            this.setBackgroundColor(((WallpapersListActivity.ColorWallpaper)currentWallpaper).color);
            final TLRPC.TL_wallPaper selectedPattern = this.selectedPattern;
            if (selectedPattern != null) {
                final BackupImageView backgroundImage = this.backgroundImage;
                final ImageLocation forDocument = ImageLocation.getForDocument(selectedPattern.document);
                final String imageFilter = this.imageFilter;
                final TLRPC.TL_wallPaper selectedPattern2 = this.selectedPattern;
                backgroundImage.setImage(forDocument, imageFilter, null, null, "jpg", selectedPattern2.document.size, 1, selectedPattern2);
            }
        }
        else if (currentWallpaper instanceof WallpapersListActivity.FileWallpaper) {
            final Bitmap currentWallpaperBitmap = this.currentWallpaperBitmap;
            if (currentWallpaperBitmap != null) {
                this.backgroundImage.setImageBitmap(currentWallpaperBitmap);
            }
            else {
                final WallpapersListActivity.FileWallpaper fileWallpaper = (WallpapersListActivity.FileWallpaper)currentWallpaper;
                final File originalPath = fileWallpaper.originalPath;
                if (originalPath != null) {
                    this.backgroundImage.setImage(originalPath.getAbsolutePath(), this.imageFilter, null);
                }
                else {
                    final File path = fileWallpaper.path;
                    if (path != null) {
                        this.backgroundImage.setImage(path.getAbsolutePath(), this.imageFilter, null);
                    }
                    else {
                        final int resId = fileWallpaper.resId;
                        if (resId == -2L) {
                            this.backgroundImage.setImageDrawable(Theme.getThemedWallpaper(false));
                        }
                        else if (resId != 0) {
                            this.backgroundImage.setImageResource(resId);
                        }
                    }
                }
            }
        }
        else if (currentWallpaper instanceof MediaController.SearchImage) {
            final MediaController.SearchImage searchImage = (MediaController.SearchImage)currentWallpaper;
            final TLRPC.Photo photo = searchImage.photo;
            if (photo != null) {
                final TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 100);
                TLRPC.PhotoSize closestPhotoSizeWithSize3;
                if ((closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(searchImage.photo.sizes, this.maxWallpaperSize, true)) == closestPhotoSizeWithSize2) {
                    closestPhotoSizeWithSize3 = null;
                }
                int size;
                if (closestPhotoSizeWithSize3 != null) {
                    size = closestPhotoSizeWithSize3.size;
                }
                else {
                    size = 0;
                }
                this.backgroundImage.setImage(ImageLocation.getForPhoto(closestPhotoSizeWithSize3, searchImage.photo), this.imageFilter, ImageLocation.getForPhoto(closestPhotoSizeWithSize2, searchImage.photo), "100_100_b", "jpg", size, 1, searchImage);
            }
            else {
                this.backgroundImage.setImage(searchImage.imageUrl, this.imageFilter, searchImage.thumbUrl, "100_100_b");
            }
        }
    }
    
    private void showPatternsView(final int n, final boolean b) {
        final boolean b2 = b && n == 1 && this.selectedPattern != null;
        if (b) {
            if (n == 0) {
                final int backgroundColor = this.backgroundColor;
                this.previousBackgroundColor = backgroundColor;
                this.colorPicker.setColor(backgroundColor);
            }
            else {
                this.previousSelectedPattern = this.selectedPattern;
                this.previousIntensity = this.currentIntensity;
                this.patternsAdapter.notifyDataSetChanged();
                final ArrayList<Object> patterns = this.patterns;
                if (patterns != null) {
                    final TLRPC.TL_wallPaper selectedPattern = this.selectedPattern;
                    int n2;
                    if (selectedPattern == null) {
                        n2 = 0;
                    }
                    else {
                        n2 = patterns.indexOf(selectedPattern) + 1;
                    }
                    this.patternsLayoutManager.scrollToPositionWithOffset(n2, (this.patternsListView.getMeasuredWidth() - AndroidUtilities.dp(100.0f) - AndroidUtilities.dp(12.0f)) / 2);
                }
            }
        }
        final CheckBoxView[] checkBoxView = this.checkBoxView;
        int n3;
        if (b2) {
            n3 = 2;
        }
        else {
            n3 = 0;
        }
        checkBoxView[n3].setVisibility(0);
        final AnimatorSet set = new AnimatorSet();
        final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
        int n4;
        if (n == 0) {
            n4 = 1;
        }
        else {
            n4 = 0;
        }
        final float n5 = 1.0f;
        if (b) {
            this.patternLayout[n].setVisibility(0);
            list.add(ObjectAnimator.ofFloat((Object)this.listView, View.TRANSLATION_Y, new float[] { (float)(-this.patternLayout[n].getMeasuredHeight() + AndroidUtilities.dp(48.0f)) }));
            list.add(ObjectAnimator.ofFloat((Object)this.buttonsContainer, View.TRANSLATION_Y, new float[] { (float)(-this.patternLayout[n].getMeasuredHeight() + AndroidUtilities.dp(48.0f)) }));
            final CheckBoxView checkBoxView2 = this.checkBoxView[2];
            final Property alpha = View.ALPHA;
            float n6;
            if (b2) {
                n6 = 1.0f;
            }
            else {
                n6 = 0.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)checkBoxView2, alpha, new float[] { n6 }));
            final CheckBoxView checkBoxView3 = this.checkBoxView[0];
            final Property alpha2 = View.ALPHA;
            float n7 = n5;
            if (b2) {
                n7 = 0.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)checkBoxView3, alpha2, new float[] { n7 }));
            list.add(ObjectAnimator.ofFloat((Object)this.backgroundImage, View.ALPHA, new float[] { 0.0f }));
            if (this.patternLayout[n4].getVisibility() == 0) {
                list.add(ObjectAnimator.ofFloat((Object)this.patternLayout[n4], View.ALPHA, new float[] { 0.0f }));
                list.add(ObjectAnimator.ofFloat((Object)this.patternLayout[n], View.ALPHA, new float[] { 0.0f, 1.0f }));
                this.patternLayout[n].setTranslationY(0.0f);
            }
            else {
                final FrameLayout[] patternLayout = this.patternLayout;
                list.add(ObjectAnimator.ofFloat((Object)patternLayout[n], View.TRANSLATION_Y, new float[] { (float)patternLayout[n].getMeasuredHeight(), 0.0f }));
            }
        }
        else {
            list.add(ObjectAnimator.ofFloat((Object)this.listView, View.TRANSLATION_Y, new float[] { 0.0f }));
            list.add(ObjectAnimator.ofFloat((Object)this.buttonsContainer, View.TRANSLATION_Y, new float[] { 0.0f }));
            final FrameLayout[] patternLayout2 = this.patternLayout;
            list.add(ObjectAnimator.ofFloat((Object)patternLayout2[n], View.TRANSLATION_Y, new float[] { (float)patternLayout2[n].getMeasuredHeight() }));
            list.add(ObjectAnimator.ofFloat((Object)this.checkBoxView[0], View.ALPHA, new float[] { 1.0f }));
            list.add(ObjectAnimator.ofFloat((Object)this.checkBoxView[2], View.ALPHA, new float[] { 0.0f }));
            list.add(ObjectAnimator.ofFloat((Object)this.backgroundImage, View.ALPHA, new float[] { 1.0f }));
        }
        set.playTogether((Collection)list);
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (b && WallpaperActivity.this.patternLayout[n4].getVisibility() == 0) {
                    WallpaperActivity.this.patternLayout[n4].setAlpha(1.0f);
                    WallpaperActivity.this.patternLayout[n4].setVisibility(4);
                }
                else if (!b) {
                    WallpaperActivity.this.patternLayout[n].setVisibility(4);
                }
                final CheckBoxView[] access$2400 = WallpaperActivity.this.checkBoxView;
                int n;
                if (b2) {
                    n = 0;
                }
                else {
                    n = 2;
                }
                access$2400[n].setVisibility(4);
            }
        });
        set.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
        set.setDuration(200L);
        set.start();
    }
    
    private void updateBlurred() {
        if (this.isBlurred && this.blurredBitmap == null) {
            final Bitmap currentWallpaperBitmap = this.currentWallpaperBitmap;
            if (currentWallpaperBitmap != null) {
                this.blurredBitmap = Utilities.blurWallpaper(currentWallpaperBitmap);
            }
            else {
                final ImageReceiver imageReceiver = this.backgroundImage.getImageReceiver();
                if (imageReceiver.hasNotThumb() || imageReceiver.hasStaticThumb()) {
                    this.blurredBitmap = Utilities.blurWallpaper(imageReceiver.getBitmap());
                }
            }
        }
        if (this.isBlurred) {
            final Bitmap blurredBitmap = this.blurredBitmap;
            if (blurredBitmap != null) {
                this.backgroundImage.setImageBitmap(blurredBitmap);
            }
        }
        else {
            this.setCurrentImage(false);
        }
    }
    
    private void updateButtonState(final RadialProgress2 radialProgress2, final Object o, final FileDownloadProgressListener fileDownloadProgressListener, final boolean b, final boolean b2) {
        Object o2;
        if (fileDownloadProgressListener == this) {
            o2 = this.selectedPattern;
            if (o2 == null) {
                o2 = this.currentWallpaper;
            }
        }
        else {
            o2 = o;
        }
        final boolean b3 = o2 instanceof TLRPC.TL_wallPaper;
        int n = 4;
        if (!b3 && !(o2 instanceof MediaController.SearchImage)) {
            if (fileDownloadProgressListener != this) {
                n = 6;
            }
            radialProgress2.setIcon(n, b, b2);
        }
        else {
            boolean b4 = b2;
            if (o == null && (b4 = b2)) {
                b4 = b2;
                if (!this.progressVisible) {
                    b4 = false;
                }
            }
            String attachFileName;
            File pathToAttach;
            int n2;
            if (b3) {
                final TLRPC.TL_wallPaper tl_wallPaper = (TLRPC.TL_wallPaper)o2;
                attachFileName = FileLoader.getAttachFileName(tl_wallPaper.document);
                if (TextUtils.isEmpty((CharSequence)attachFileName)) {
                    return;
                }
                pathToAttach = FileLoader.getPathToAttach(tl_wallPaper.document, true);
                n2 = tl_wallPaper.document.size;
            }
            else {
                final MediaController.SearchImage searchImage = (MediaController.SearchImage)o2;
                final TLRPC.Photo photo = searchImage.photo;
                File file;
                String s;
                if (photo != null) {
                    final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, this.maxWallpaperSize, true);
                    file = FileLoader.getPathToAttach(closestPhotoSizeWithSize, true);
                    s = FileLoader.getAttachFileName(closestPhotoSizeWithSize);
                    n2 = closestPhotoSizeWithSize.size;
                }
                else {
                    file = ImageLoader.getHttpFilePath(searchImage.imageUrl, "jpg");
                    s = file.getName();
                    n2 = searchImage.size;
                }
                pathToAttach = file;
                if (TextUtils.isEmpty((CharSequence)(attachFileName = s))) {
                    return;
                }
            }
            final boolean exists = pathToAttach.exists();
            if (exists) {
                DownloadController.getInstance(super.currentAccount).removeLoadingFileObserver(fileDownloadProgressListener);
                radialProgress2.setProgress(1.0f, b4);
                if (o != null) {
                    n = 6;
                }
                radialProgress2.setIcon(n, b, b4);
                if (o == null) {
                    this.backgroundImage.invalidate();
                    if (n2 != 0) {
                        super.actionBar.setSubtitle(AndroidUtilities.formatFileSize(n2));
                    }
                    else {
                        super.actionBar.setSubtitle(null);
                    }
                }
            }
            else {
                DownloadController.getInstance(super.currentAccount).addLoadingFileObserver(attachFileName, null, fileDownloadProgressListener);
                FileLoader.getInstance(super.currentAccount).isLoadingFile(attachFileName);
                final Float fileProgress = ImageLoader.getInstance().getFileProgress(attachFileName);
                if (fileProgress != null) {
                    radialProgress2.setProgress(fileProgress, b4);
                }
                else {
                    radialProgress2.setProgress(0.0f, b4);
                }
                radialProgress2.setIcon(10, b, b4);
                if (o == null) {
                    super.actionBar.setSubtitle(LocaleController.getString("LoadingFullImage", 2131559769));
                    this.backgroundImage.invalidate();
                }
            }
            if (o == null) {
                final TLRPC.TL_wallPaper selectedPattern = this.selectedPattern;
                final float n3 = 0.5f;
                if (selectedPattern == null) {
                    final FrameLayout buttonsContainer = this.buttonsContainer;
                    float alpha;
                    if (exists) {
                        alpha = 1.0f;
                    }
                    else {
                        alpha = 0.5f;
                    }
                    buttonsContainer.setAlpha(alpha);
                }
                this.bottomOverlayChat.setEnabled(exists);
                final TextView bottomOverlayChatText = this.bottomOverlayChatText;
                float alpha2 = n3;
                if (exists) {
                    alpha2 = 1.0f;
                }
                bottomOverlayChatText.setAlpha(alpha2);
            }
        }
    }
    
    private void updateMotionButton() {
        final CheckBoxView[] checkBoxView = this.checkBoxView;
        int n;
        if (this.selectedPattern != null) {
            n = 2;
        }
        else {
            n = 0;
        }
        checkBoxView[n].setVisibility(0);
        final AnimatorSet set = new AnimatorSet();
        final CheckBoxView checkBoxView2 = this.checkBoxView[2];
        final Property alpha = View.ALPHA;
        final TLRPC.TL_wallPaper selectedPattern = this.selectedPattern;
        final float n2 = 1.0f;
        float n3;
        if (selectedPattern != null) {
            n3 = 1.0f;
        }
        else {
            n3 = 0.0f;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)checkBoxView2, alpha, new float[] { n3 });
        final CheckBoxView checkBoxView3 = this.checkBoxView[0];
        final Property alpha2 = View.ALPHA;
        float n4 = n2;
        if (this.selectedPattern != null) {
            n4 = 0.0f;
        }
        set.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ObjectAnimator.ofFloat((Object)checkBoxView3, alpha2, new float[] { n4 }) });
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                final CheckBoxView[] access$2400 = WallpaperActivity.this.checkBoxView;
                int n;
                if (WallpaperActivity.this.selectedPattern != null) {
                    n = 0;
                }
                else {
                    n = 2;
                }
                access$2400[n].setVisibility(4);
            }
        });
        set.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
        set.setDuration(200L);
        set.start();
    }
    
    private void updateSelectedPattern(final boolean b) {
        for (int childCount = this.patternsListView.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.patternsListView.getChildAt(i);
            if (child instanceof PatternCell) {
                ((PatternCell)child).updateSelected(b);
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("BackgroundPreview", 2131558821));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    WallpaperActivity.this.finishFragment();
                }
                else if (n == 1) {
                    if (WallpaperActivity.this.getParentActivity() == null) {
                        return;
                    }
                    final StringBuilder sb = new StringBuilder();
                    if (WallpaperActivity.this.isBlurred) {
                        sb.append("blur");
                    }
                    if (WallpaperActivity.this.isMotion) {
                        if (sb.length() > 0) {
                            sb.append("+");
                        }
                        sb.append("motion");
                    }
                    String s;
                    if (WallpaperActivity.this.currentWallpaper instanceof TLRPC.TL_wallPaper) {
                        final TLRPC.TL_wallPaper tl_wallPaper = (TLRPC.TL_wallPaper)WallpaperActivity.this.currentWallpaper;
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("https://");
                        sb2.append(MessagesController.getInstance(WallpaperActivity.this.currentAccount).linkPrefix);
                        sb2.append("/bg/");
                        sb2.append(tl_wallPaper.slug);
                        final String str = s = sb2.toString();
                        if (sb.length() > 0) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append(str);
                            sb3.append("?mode=");
                            sb3.append(sb.toString());
                            s = sb3.toString();
                        }
                    }
                    else {
                        if (!(WallpaperActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                            return;
                        }
                        final WallpapersListActivity.ColorWallpaper colorWallpaper = (WallpapersListActivity.ColorWallpaper)WallpaperActivity.this.currentWallpaper;
                        final String lowerCase = String.format("%02x%02x%02x", (byte)(WallpaperActivity.this.backgroundColor >> 16) & 0xFF, (byte)(WallpaperActivity.this.backgroundColor >> 8) & 0xFF, (byte)(WallpaperActivity.this.backgroundColor & 0xFF)).toLowerCase();
                        if (WallpaperActivity.this.selectedPattern != null) {
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append("https://");
                            sb4.append(MessagesController.getInstance(WallpaperActivity.this.currentAccount).linkPrefix);
                            sb4.append("/bg/");
                            sb4.append(WallpaperActivity.this.selectedPattern.slug);
                            sb4.append("?intensity=");
                            sb4.append((int)(WallpaperActivity.this.currentIntensity * 100.0f));
                            sb4.append("&bg_color=");
                            sb4.append(lowerCase);
                            final String str2 = s = sb4.toString();
                            if (sb.length() > 0) {
                                final StringBuilder sb5 = new StringBuilder();
                                sb5.append(str2);
                                sb5.append("&mode=");
                                sb5.append(sb.toString());
                                s = sb5.toString();
                            }
                        }
                        else {
                            final StringBuilder sb6 = new StringBuilder();
                            sb6.append("https://");
                            sb6.append(MessagesController.getInstance(WallpaperActivity.this.currentAccount).linkPrefix);
                            sb6.append("/bg/");
                            sb6.append(lowerCase);
                            s = sb6.toString();
                        }
                    }
                    final WallpaperActivity this$0 = WallpaperActivity.this;
                    this$0.showDialog(new ShareAlert((Context)this$0.getParentActivity(), null, s, false, s, false));
                }
            }
        });
        final Object currentWallpaper = this.currentWallpaper;
        if (currentWallpaper instanceof WallpapersListActivity.ColorWallpaper || currentWallpaper instanceof TLRPC.TL_wallPaper) {
            super.actionBar.createMenu().addItem(1, 2131165470);
        }
        final FrameLayout fragmentView = new FrameLayout(context);
        super.fragmentView = (View)fragmentView;
        super.hasOwnBackground = true;
        this.backgroundImage = new BackupImageView(context) {
            @Override
            protected void onDraw(final Canvas canvas) {
                super.onDraw(canvas);
                if (WallpaperActivity.this.progressVisible && WallpaperActivity.this.radialProgress != null) {
                    WallpaperActivity.this.radialProgress.draw(canvas);
                }
            }
            
            protected void onMeasure(int n, int n2) {
                super.onMeasure(n, n2);
                final WallpaperActivity this$0 = WallpaperActivity.this;
                this$0.parallaxScale = this$0.parallaxEffect.getScale(this.getMeasuredWidth(), this.getMeasuredHeight());
                if (WallpaperActivity.this.isMotion) {
                    this.setScaleX(WallpaperActivity.this.parallaxScale);
                    this.setScaleY(WallpaperActivity.this.parallaxScale);
                }
                if (WallpaperActivity.this.radialProgress != null) {
                    final int dp = AndroidUtilities.dp(44.0f);
                    n = (this.getMeasuredWidth() - dp) / 2;
                    n2 = (this.getMeasuredHeight() - dp) / 2;
                    WallpaperActivity.this.radialProgress.setProgressRect(n, n2, n + dp, dp + n2);
                }
                WallpaperActivity.this.progressVisible = (this.getMeasuredWidth() <= this.getMeasuredHeight());
            }
            
            public void setAlpha(final float overrideAlpha) {
                WallpaperActivity.this.radialProgress.setOverrideAlpha(overrideAlpha);
            }
        };
        int n2 = 0;
        int n3 = 0;
        int n6 = 0;
        Label_0198: {
            int n4;
            if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                final int n = 3;
                if (this.patterns != null) {
                    n2 = 0;
                }
                else {
                    n2 = 2;
                }
                n3 = n;
                n4 = n2;
                if (this.patterns == null) {
                    if (this.selectedPattern == null) {
                        final int n5 = 0;
                        n3 = n;
                        n6 = n5;
                        break Label_0198;
                    }
                    n3 = n;
                    n4 = n2;
                }
            }
            else {
                n3 = 2;
                n4 = 0;
            }
            n6 = 1;
            n2 = n4;
        }
        fragmentView.addView((View)this.backgroundImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
        this.backgroundImage.getImageReceiver().setDelegate((ImageReceiver.ImageReceiverDelegate)new _$$Lambda$WallpaperActivity$DLrQAr4_n4zrBNqOkPDVZ2ql9yg(this));
        (this.radialProgress = new RadialProgress2(this.backgroundImage)).setColors("chat_serviceBackground", "chat_serviceBackground", "chat_serviceText", "chat_serviceText");
        (this.listView = new RecyclerListView(context)).setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, true));
        this.listView.setOverScrollMode(2);
        this.listView.setAdapter(new ListAdapter(context));
        final RecyclerListView listView = this.listView;
        float n7;
        if (n6 != 0) {
            n7 = 64.0f;
        }
        else {
            n7 = 4.0f;
        }
        listView.setPadding(0, 0, 0, AndroidUtilities.dp(n7));
        fragmentView.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
        (this.bottomOverlayChat = new FrameLayout(context) {
            public void onDraw(final Canvas canvas) {
                final int intrinsicHeight = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                Theme.chat_composeShadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), intrinsicHeight);
                Theme.chat_composeShadowDrawable.draw(canvas);
                canvas.drawRect(0.0f, (float)intrinsicHeight, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
            }
        }).setWillNotDraw(false);
        this.bottomOverlayChat.setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
        fragmentView.addView((View)this.bottomOverlayChat, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 51, 80));
        this.bottomOverlayChat.setOnClickListener((View$OnClickListener)new _$$Lambda$WallpaperActivity$1kBuywjigfsWiHQkwiP2IyEMOss(this));
        (this.bottomOverlayChatText = new TextView(context)).setTextSize(1, 15.0f);
        this.bottomOverlayChatText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.bottomOverlayChatText.setTextColor(Theme.getColor("chat_fieldOverlayText"));
        this.bottomOverlayChatText.setText((CharSequence)LocaleController.getString("SetBackground", 2131560732));
        this.bottomOverlayChat.addView((View)this.bottomOverlayChatText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 17));
        fragmentView.addView((View)(this.buttonsContainer = new FrameLayout(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 32.0f, 81, 0.0f, 0.0f, 0.0f, 66.0f));
        final String[] array = new String[n3];
        final int[] array2 = new int[n3];
        this.checkBoxView = new CheckBoxView[n3];
        if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
            array[0] = LocaleController.getString("BackgroundColor", 2131558812);
            array[1] = LocaleController.getString("BackgroundPattern", 2131558820);
            array[2] = LocaleController.getString("BackgroundMotion", 2131558819);
        }
        else {
            array[0] = LocaleController.getString("BackgroundBlurred", 2131558810);
            array[1] = LocaleController.getString("BackgroundMotion", 2131558819);
        }
        int i = 0;
        int max = 0;
        while (i < array.length) {
            array2[i] = (int)Math.ceil(this.textPaint.measureText(array[i]));
            max = Math.max(max, array2[i]);
            ++i;
        }
        for (int j = n2; j < n3; ++j) {
            (this.checkBoxView[j] = new CheckBoxView(context, !(this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) || j != 0)).setText(array[j], array2[j], max);
            if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                if (j == 1) {
                    this.checkBoxView[j].setChecked(this.selectedPattern != null, false);
                }
                else if (j == 2) {
                    this.checkBoxView[j].setChecked(this.isMotion, false);
                }
            }
            else {
                final CheckBoxView checkBoxView = this.checkBoxView[j];
                boolean b;
                if (j == 0) {
                    b = this.isBlurred;
                }
                else {
                    b = this.isMotion;
                }
                checkBoxView.setChecked(b, false);
            }
            final int n8 = AndroidUtilities.dp(56.0f) + max;
            final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(-2, n8);
            frameLayout$LayoutParams.gravity = 51;
            int leftMargin;
            if (j == 1) {
                leftMargin = n8 + AndroidUtilities.dp(9.0f);
            }
            else {
                leftMargin = 0;
            }
            frameLayout$LayoutParams.leftMargin = leftMargin;
            this.buttonsContainer.addView((View)this.checkBoxView[j], (ViewGroup$LayoutParams)frameLayout$LayoutParams);
            final CheckBoxView[] checkBoxView2 = this.checkBoxView;
            checkBoxView2[j].setOnClickListener((View$OnClickListener)new _$$Lambda$WallpaperActivity$1_TCmIiY1WoqCaRaze9MxJF8Hq4(this, j, checkBoxView2[j]));
            if (n2 == 0 && j == 2) {
                this.checkBoxView[j].setAlpha(0.0f);
                this.checkBoxView[j].setVisibility(4);
            }
        }
        if (n6 == 0) {
            this.buttonsContainer.setVisibility(8);
        }
        (this.parallaxEffect = new WallpaperParallaxEffect(context)).setCallback((WallpaperParallaxEffect.Callback)new _$$Lambda$WallpaperActivity$PNT8mBRP7gH3N8qVf5B9a9cTbAY(this));
        if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
            this.isBlurred = false;
            for (int k = 0; k < 2; ++k) {
                (this.patternLayout[k] = new FrameLayout(context) {
                    public void onDraw(final Canvas canvas) {
                        final int intrinsicHeight = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                        Theme.chat_composeShadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), intrinsicHeight);
                        Theme.chat_composeShadowDrawable.draw(canvas);
                        canvas.drawRect(0.0f, (float)intrinsicHeight, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
                    }
                }).setVisibility(4);
                this.patternLayout[k].setWillNotDraw(false);
                final FrameLayout frameLayout = this.patternLayout[k];
                int n9;
                if (k == 0) {
                    n9 = 390;
                }
                else {
                    n9 = 242;
                }
                fragmentView.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, n9, 83));
                (this.patternsButtonsContainer[k] = new FrameLayout(context) {
                    public void onDraw(final Canvas canvas) {
                        final int intrinsicHeight = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                        Theme.chat_composeShadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), intrinsicHeight);
                        Theme.chat_composeShadowDrawable.draw(canvas);
                        canvas.drawRect(0.0f, (float)intrinsicHeight, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
                    }
                }).setWillNotDraw(false);
                this.patternsButtonsContainer[k].setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
                this.patternLayout[k].addView((View)this.patternsButtonsContainer[k], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 51, 80));
                (this.patternsCancelButton[k] = new TextView(context)).setTextSize(1, 15.0f);
                this.patternsCancelButton[k].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                this.patternsCancelButton[k].setTextColor(Theme.getColor("chat_fieldOverlayText"));
                this.patternsCancelButton[k].setText((CharSequence)LocaleController.getString("Cancel", 2131558891).toUpperCase());
                this.patternsCancelButton[k].setGravity(17);
                this.patternsCancelButton[k].setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), 0);
                this.patternsCancelButton[k].setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 0));
                this.patternsButtonsContainer[k].addView((View)this.patternsCancelButton[k], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 51));
                this.patternsCancelButton[k].setOnClickListener((View$OnClickListener)new _$$Lambda$WallpaperActivity$25btyyl2ojPzi3umZx_yVj0JTsE(this, k));
                (this.patternsSaveButton[k] = new TextView(context)).setTextSize(1, 15.0f);
                this.patternsSaveButton[k].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                this.patternsSaveButton[k].setTextColor(Theme.getColor("chat_fieldOverlayText"));
                this.patternsSaveButton[k].setText((CharSequence)LocaleController.getString("Save", 2131560626).toUpperCase());
                this.patternsSaveButton[k].setGravity(17);
                this.patternsSaveButton[k].setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), 0);
                this.patternsSaveButton[k].setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 0));
                this.patternsButtonsContainer[k].addView((View)this.patternsSaveButton[k], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 53));
                this.patternsSaveButton[k].setOnClickListener((View$OnClickListener)new _$$Lambda$WallpaperActivity$rqsIbe4XDaEpSOIXXh5TVdi7z9A(this, k));
                if (k == 1) {
                    (this.patternsListView = new RecyclerListView(context) {
                        @Override
                        public boolean onTouchEvent(final MotionEvent motionEvent) {
                            if (motionEvent.getAction() == 0) {
                                this.getParent().requestDisallowInterceptTouchEvent(true);
                            }
                            return super.onTouchEvent(motionEvent);
                        }
                    }).setLayoutManager((RecyclerView.LayoutManager)(this.patternsLayoutManager = new LinearLayoutManager(context, 0, false)));
                    this.patternsListView.setAdapter(this.patternsAdapter = new PatternsAdapter(context));
                    this.patternsListView.addItemDecoration((RecyclerView.ItemDecoration)new RecyclerView.ItemDecoration() {
                        @Override
                        public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
                            final int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                            rect.left = AndroidUtilities.dp(12.0f);
                            rect.top = 0;
                            rect.bottom = 0;
                            if (childAdapterPosition == state.getItemCount() - 1) {
                                rect.right = AndroidUtilities.dp(12.0f);
                            }
                        }
                    });
                    this.patternLayout[k].addView((View)this.patternsListView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 100.0f, 51, 0.0f, 14.0f, 0.0f, 0.0f));
                    this.patternsListView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$WallpaperActivity$yS3bJtjNmGQ30kYj94XwI29Szdc(this));
                    (this.intensityCell = new HeaderCell(context)).setText(LocaleController.getString("BackgroundIntensity", 2131558818));
                    this.patternLayout[k].addView((View)this.intensityCell, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 113.0f, 0.0f, 0.0f));
                    (this.intensitySeekBar = new SeekBarView(context) {
                        @Override
                        public boolean onTouchEvent(final MotionEvent motionEvent) {
                            if (motionEvent.getAction() == 0) {
                                this.getParent().requestDisallowInterceptTouchEvent(true);
                            }
                            return super.onTouchEvent(motionEvent);
                        }
                    }).setProgress(this.currentIntensity);
                    this.intensitySeekBar.setReportChanges(true);
                    this.intensitySeekBar.setDelegate((SeekBarView.SeekBarViewDelegate)new _$$Lambda$WallpaperActivity$JwZ2LKq5VeRI4R5G1vGS7VNqDIs(this));
                    this.patternLayout[k].addView((View)this.intensitySeekBar, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 30.0f, 51, 9.0f, 153.0f, 9.0f, 0.0f));
                }
                else {
                    this.colorPicker = new ColorPicker(context);
                    this.patternLayout[k].addView((View)this.colorPicker, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 1, 0.0f, 0.0f, 0.0f, 48.0f));
                }
            }
        }
        this.setCurrentImage(true);
        this.updateButtonState(this.radialProgress, null, this, false, false);
        if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
            super.fragmentView.setBackgroundColor(-16777216);
        }
        if (!(this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
            this.backgroundImage.getImageReceiver().setCrossfadeWithOldImage(true);
            this.backgroundImage.getImageReceiver().setForceCrossfade(true);
        }
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.wallpapersNeedReload) {
            final Object currentWallpaper = this.currentWallpaper;
            if (currentWallpaper instanceof WallpapersListActivity.FileWallpaper) {
                final WallpapersListActivity.FileWallpaper fileWallpaper = (WallpapersListActivity.FileWallpaper)currentWallpaper;
                if (fileWallpaper.id == -1L) {
                    fileWallpaper.id = (long)array[0];
                }
            }
        }
    }
    
    @Override
    public int getObserverTag() {
        return this.TAG;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final ArrayList<ThemeDescription> list = new ArrayList<ThemeDescription>();
        list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        list.add(new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        list.add(new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        int n = 0;
        while (true) {
            final FrameLayout[] patternLayout = this.patternLayout;
            if (n >= patternLayout.length) {
                break;
            }
            list.add(new ThemeDescription((View)patternLayout[n], 0, null, null, new Drawable[] { Theme.chat_composeShadowDrawable }, null, "chat_messagePanelShadow"));
            list.add(new ThemeDescription((View)this.patternLayout[n], 0, null, Theme.chat_composeBackgroundPaint, null, null, "chat_messagePanelBackground"));
            ++n;
        }
        int n2 = 0;
        while (true) {
            final FrameLayout[] patternsButtonsContainer = this.patternsButtonsContainer;
            if (n2 >= patternsButtonsContainer.length) {
                break;
            }
            list.add(new ThemeDescription((View)patternsButtonsContainer[n2], 0, null, null, new Drawable[] { Theme.chat_composeShadowDrawable }, null, "chat_messagePanelShadow"));
            list.add(new ThemeDescription((View)this.patternsButtonsContainer[n2], 0, null, Theme.chat_composeBackgroundPaint, null, null, "chat_messagePanelBackground"));
            ++n2;
        }
        list.add(new ThemeDescription((View)this.bottomOverlayChat, 0, null, null, new Drawable[] { Theme.chat_composeShadowDrawable }, null, "chat_messagePanelShadow"));
        list.add(new ThemeDescription((View)this.bottomOverlayChat, 0, null, Theme.chat_composeBackgroundPaint, null, null, "chat_messagePanelBackground"));
        list.add(new ThemeDescription((View)this.bottomOverlayChatText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "chat_fieldOverlayText"));
        int n3 = 0;
        while (true) {
            final TextView[] patternsSaveButton = this.patternsSaveButton;
            if (n3 >= patternsSaveButton.length) {
                break;
            }
            list.add(new ThemeDescription((View)patternsSaveButton[n3], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "chat_fieldOverlayText"));
            ++n3;
        }
        int n4 = 0;
        while (true) {
            final TextView[] patternsSaveButton2 = this.patternsSaveButton;
            if (n4 >= patternsSaveButton2.length) {
                break;
            }
            list.add(new ThemeDescription((View)patternsSaveButton2[n4], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "chat_fieldOverlayText"));
            ++n4;
        }
        if (this.colorPicker != null) {
            for (int i = 0; i < this.colorPicker.colorEditText.length; ++i) {
                list.add(new ThemeDescription((View)this.colorPicker.colorEditText[i], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)this.colorPicker.colorEditText[i], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
                list.add(new ThemeDescription((View)this.colorPicker.colorEditText[i], ThemeDescription.FLAG_HINTTEXTCOLOR | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "windowBackgroundWhiteBlueHeader"));
                list.add(new ThemeDescription((View)this.colorPicker.colorEditText[i], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
                list.add(new ThemeDescription((View)this.colorPicker.colorEditText[i], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                list.add(new ThemeDescription((View)this.colorPicker.colorEditText[i], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "windowBackgroundWhiteRedText3"));
            }
        }
        list.add(new ThemeDescription((View)this.intensitySeekBar, 0, new Class[] { SeekBarView.class }, new String[] { "innerPaint1" }, null, null, null, "player_progressBackground"));
        list.add(new ThemeDescription((View)this.intensitySeekBar, 0, new Class[] { SeekBarView.class }, new String[] { "outerPaint1" }, null, null, null, "player_progress"));
        list.add(new ThemeDescription((View)this.intensityCell, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable }, null, "chat_inBubble"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable }, null, "chat_inBubbleSelected"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgInShadowDrawable, Theme.chat_msgInMediaShadowDrawable }, null, "chat_inBubbleShadow"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable }, null, "chat_outBubble"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable }, null, "chat_outBubbleSelected"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutShadowDrawable, Theme.chat_msgOutMediaShadowDrawable }, null, "chat_outBubbleShadow"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_messageTextIn"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_messageTextOut"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutCheckDrawable, Theme.chat_msgOutHalfCheckDrawable }, null, "chat_outSentCheck"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgOutCheckSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable }, null, "chat_outSentCheckSelected"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, new Drawable[] { Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable }, null, "chat_mediaSentCheck"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inReplyLine"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outReplyLine"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inReplyNameText"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outReplyNameText"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inReplyMessageText"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outReplyMessageText"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inReplyMediaMessageSelectedText"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outReplyMediaMessageSelectedText"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inTimeText"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outTimeText"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_inTimeSelectedText"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ChatMessageCell.class }, null, null, null, "chat_outTimeSelectedText"));
        return list.toArray(new ThemeDescription[0]);
    }
    
    @Override
    public void onFailedDownload(final String s, final boolean b) {
        this.updateButtonState(this.radialProgress, null, this, true, b);
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        final StringBuilder sb = new StringBuilder();
        sb.append((int)(1080.0f / AndroidUtilities.density));
        sb.append("_");
        sb.append((int)(1920.0f / AndroidUtilities.density));
        sb.append("_f");
        this.imageFilter = sb.toString();
        final Point displaySize = AndroidUtilities.displaySize;
        this.maxWallpaperSize = Math.min(1920, Math.max(displaySize.x, displaySize.y));
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.wallpapersNeedReload);
        this.TAG = DownloadController.getInstance(super.currentAccount).generateObserverTag();
        (this.textPaint = new TextPaint(1)).setColor(-1);
        this.textPaint.setTextSize((float)AndroidUtilities.dp(14.0f));
        this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        (this.checkPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
        this.checkPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.checkPaint.setColor(0);
        this.checkPaint.setStrokeCap(Paint$Cap.ROUND);
        this.checkPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
        (this.eraserPaint = new Paint(1)).setColor(0);
        this.eraserPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
        this.backgroundPaint = new Paint(1);
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        final Bitmap blurredBitmap = this.blurredBitmap;
        if (blurredBitmap != null) {
            blurredBitmap.recycle();
            this.blurredBitmap = null;
        }
        Theme.applyChatServiceMessageColor();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.wallpapersNeedReload);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (this.isMotion) {
            this.parallaxEffect.setEnabled(false);
        }
    }
    
    @Override
    public void onProgressDownload(final String s, final float n) {
        this.radialProgress.setProgress(n, this.progressVisible);
        if (this.radialProgress.getIcon() != 10) {
            this.updateButtonState(this.radialProgress, null, this, false, true);
        }
    }
    
    @Override
    public void onProgressUpload(final String s, final float n, final boolean b) {
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (this.isMotion) {
            this.parallaxEffect.setEnabled(true);
        }
    }
    
    @Override
    public void onSuccessDownload(final String s) {
        this.radialProgress.setProgress(1.0f, this.progressVisible);
        this.updateButtonState(this.radialProgress, null, this, false, true);
    }
    
    public void setDelegate(final WallpaperActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setInitialModes(final boolean isBlurred, final boolean isMotion) {
        this.isBlurred = isBlurred;
        this.isMotion = isMotion;
    }
    
    public void setPatterns(final ArrayList<Object> patterns) {
        this.patterns = patterns;
        final Object currentWallpaper = this.currentWallpaper;
        if (currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
            final WallpapersListActivity.ColorWallpaper colorWallpaper = (WallpapersListActivity.ColorWallpaper)currentWallpaper;
            if (colorWallpaper.patternId != 0L) {
                for (int i = 0; i < this.patterns.size(); ++i) {
                    final TLRPC.TL_wallPaper selectedPattern = this.patterns.get(i);
                    if (selectedPattern.id == colorWallpaper.patternId) {
                        this.selectedPattern = selectedPattern;
                        break;
                    }
                }
                this.currentIntensity = colorWallpaper.intensity;
            }
        }
    }
    
    private class CheckBoxView extends View
    {
        private static final float progressBounceDiff = 0.2f;
        public final Property<CheckBoxView, Float> PROGRESS_PROPERTY;
        private ObjectAnimator checkAnimator;
        private String currentText;
        private int currentTextSize;
        private Bitmap drawBitmap;
        private Canvas drawCanvas;
        private boolean isChecked;
        private int maxTextSize;
        private float progress;
        private RectF rect;
        
        public CheckBoxView(final Context context, final boolean b) {
            super(context);
            this.PROGRESS_PROPERTY = (Property<CheckBoxView, Float>)new AnimationProperties.FloatProperty<CheckBoxView>("progress") {
                public Float get(final CheckBoxView checkBoxView) {
                    return CheckBoxView.this.progress;
                }
                
                public void setValue(final CheckBoxView checkBoxView, final float n) {
                    CheckBoxView.this.progress = n;
                    CheckBoxView.this.invalidate();
                }
            };
            this.rect = new RectF();
            if (b) {
                this.drawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f), Bitmap$Config.ARGB_4444);
                this.drawCanvas = new Canvas(this.drawBitmap);
            }
        }
        
        private void animateToCheckedState(final boolean b) {
            final Property<CheckBoxView, Float> progress_PROPERTY = this.PROGRESS_PROPERTY;
            float n;
            if (b) {
                n = 1.0f;
            }
            else {
                n = 0.0f;
            }
            (this.checkAnimator = ObjectAnimator.ofFloat((Object)this, (Property)progress_PROPERTY, new float[] { n })).setDuration(300L);
            this.checkAnimator.start();
        }
        
        private void cancelCheckAnimator() {
            final ObjectAnimator checkAnimator = this.checkAnimator;
            if (checkAnimator != null) {
                checkAnimator.cancel();
            }
        }
        
        private void setProgress(final float progress) {
            if (this.progress == progress) {
                return;
            }
            this.progress = progress;
            this.invalidate();
        }
        
        public boolean isChecked() {
            return this.isChecked;
        }
        
        protected void onDraw(final Canvas canvas) {
            this.rect.set(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
            canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(4.0f), Theme.chat_actionBackgroundPaint);
            final int n = (this.getMeasuredWidth() - this.currentTextSize - AndroidUtilities.dp(28.0f)) / 2;
            canvas.drawText(this.currentText, (float)(AndroidUtilities.dp(28.0f) + n), (float)AndroidUtilities.dp(21.0f), (Paint)WallpaperActivity.this.textPaint);
            canvas.save();
            canvas.translate((float)n, (float)AndroidUtilities.dp(7.0f));
            if (this.drawBitmap != null) {
                final float progress = this.progress;
                float n3;
                float n2;
                if (progress <= 0.5f) {
                    n2 = (n3 = progress / 0.5f);
                }
                else {
                    n2 = 2.0f - progress / 0.5f;
                    n3 = 1.0f;
                }
                final float n4 = AndroidUtilities.dp(1.0f) * n2;
                this.rect.set(n4, n4, AndroidUtilities.dp(18.0f) - n4, AndroidUtilities.dp(18.0f) - n4);
                this.drawBitmap.eraseColor(0);
                WallpaperActivity.this.backgroundPaint.setColor(-1);
                final Canvas drawCanvas = this.drawCanvas;
                final RectF rect = this.rect;
                drawCanvas.drawRoundRect(rect, rect.width() / 2.0f, this.rect.height() / 2.0f, WallpaperActivity.this.backgroundPaint);
                if (n3 != 1.0f) {
                    final float min = Math.min((float)AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f) * n3 + n4);
                    this.rect.set(AndroidUtilities.dp(2.0f) + min, AndroidUtilities.dp(2.0f) + min, AndroidUtilities.dp(16.0f) - min, AndroidUtilities.dp(16.0f) - min);
                    final Canvas drawCanvas2 = this.drawCanvas;
                    final RectF rect2 = this.rect;
                    drawCanvas2.drawRoundRect(rect2, rect2.width() / 2.0f, this.rect.height() / 2.0f, WallpaperActivity.this.eraserPaint);
                }
                if (this.progress > 0.5f) {
                    final float n5 = (float)AndroidUtilities.dp(7.3f);
                    final float n6 = (float)AndroidUtilities.dp(2.5f);
                    final float n7 = 1.0f - n2;
                    this.drawCanvas.drawLine((float)AndroidUtilities.dp(7.3f), (float)AndroidUtilities.dp(13.0f), (float)(int)(n5 - n6 * n7), (float)(int)(AndroidUtilities.dp(13.0f) - AndroidUtilities.dp(2.5f) * n7), WallpaperActivity.this.checkPaint);
                    this.drawCanvas.drawLine((float)AndroidUtilities.dp(7.3f), (float)AndroidUtilities.dp(13.0f), (float)(int)(AndroidUtilities.dp(7.3f) + AndroidUtilities.dp(6.0f) * n7), (float)(int)(AndroidUtilities.dp(13.0f) - AndroidUtilities.dp(6.0f) * n7), WallpaperActivity.this.checkPaint);
                }
                canvas.drawBitmap(this.drawBitmap, 0.0f, 0.0f, (Paint)null);
            }
            else {
                WallpaperActivity.this.backgroundPaint.setColor(WallpaperActivity.this.backgroundColor);
                this.rect.set(0.0f, 0.0f, (float)AndroidUtilities.dp(18.0f), (float)AndroidUtilities.dp(18.0f));
                final RectF rect3 = this.rect;
                canvas.drawRoundRect(rect3, rect3.width() / 2.0f, this.rect.height() / 2.0f, WallpaperActivity.this.backgroundPaint);
            }
            canvas.restore();
        }
        
        protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
            super.onLayout(b, n, n2, n3, n4);
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(this.maxTextSize + AndroidUtilities.dp(56.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
        }
        
        public void setChecked(final boolean isChecked, final boolean b) {
            if (isChecked == this.isChecked) {
                return;
            }
            this.isChecked = isChecked;
            if (b) {
                this.animateToCheckedState(isChecked);
            }
            else {
                this.cancelCheckAnimator();
                float progress;
                if (isChecked) {
                    progress = 1.0f;
                }
                else {
                    progress = 0.0f;
                }
                this.progress = progress;
                this.invalidate();
            }
        }
        
        public void setText(final String currentText, final int currentTextSize, final int maxTextSize) {
            this.currentText = currentText;
            this.currentTextSize = currentTextSize;
            this.maxTextSize = maxTextSize;
        }
    }
    
    private class ColorPicker extends FrameLayout
    {
        private int centerX;
        private int centerY;
        private Drawable circleDrawable;
        private Paint circlePaint;
        private boolean circlePressed;
        private EditTextBoldCursor[] colorEditText;
        private LinearGradient colorGradient;
        private float[] colorHSV;
        private boolean colorPressed;
        private Bitmap colorWheelBitmap;
        private Paint colorWheelPaint;
        private int colorWheelRadius;
        private float[] hsvTemp;
        boolean ignoreTextChange;
        private LinearLayout linearLayout;
        private int lx;
        private int ly;
        private final int paramValueSliderWidth;
        private Paint valueSliderPaint;
        
        public ColorPicker(final Context context) {
            super(context);
            this.paramValueSliderWidth = AndroidUtilities.dp(20.0f);
            this.colorEditText = new EditTextBoldCursor[2];
            this.colorHSV = new float[] { 0.0f, 0.0f, 1.0f };
            this.hsvTemp = new float[3];
            this.setWillNotDraw(false);
            this.circlePaint = new Paint(1);
            this.circleDrawable = context.getResources().getDrawable(2131165520).mutate();
            (this.colorWheelPaint = new Paint()).setAntiAlias(true);
            this.colorWheelPaint.setDither(true);
            (this.valueSliderPaint = new Paint()).setAntiAlias(true);
            this.valueSliderPaint.setDither(true);
            (this.linearLayout = new LinearLayout(context)).setOrientation(0);
            this.addView((View)this.linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 64.0f, 51, 12.0f, 14.0f, 21.0f, 0.0f));
            for (int i = 0; i < 2; ++i) {
                (this.colorEditText[i] = new EditTextBoldCursor(context)).setTextSize(1, 18.0f);
                this.colorEditText[i].setHintColor(Theme.getColor("windowBackgroundWhiteHintText"));
                this.colorEditText[i].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.colorEditText[i].setBackgroundDrawable((Drawable)null);
                this.colorEditText[i].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.colorEditText[i].setCursorSize(AndroidUtilities.dp(20.0f));
                this.colorEditText[i].setCursorWidth(1.5f);
                this.colorEditText[i].setSingleLine(true);
                this.colorEditText[i].setGravity(19);
                this.colorEditText[i].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
                this.colorEditText[i].setTransformHintToHeader(true);
                this.colorEditText[i].setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
                this.colorEditText[i].setPadding(0, 0, 0, 0);
                if (i == 0) {
                    this.colorEditText[i].setInputType(1);
                    this.colorEditText[i].setHintText(LocaleController.getString("BackgroundHexColorCode", 2131558817));
                }
                else {
                    this.colorEditText[i].setInputType(2);
                    this.colorEditText[i].setHintText(LocaleController.getString("BackgroundBrightness", 2131558811));
                }
                this.colorEditText[i].setImeOptions(268435462);
                int n;
                if (i == 0) {
                    n = 7;
                }
                else {
                    n = 3;
                }
                this.colorEditText[i].setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(n) });
                final LinearLayout linearLayout = this.linearLayout;
                final EditTextBoldCursor editTextBoldCursor = this.colorEditText[i];
                float n2;
                if (i == 0) {
                    n2 = 0.67f;
                }
                else {
                    n2 = 0.31f;
                }
                int n3;
                if (i != 1) {
                    n3 = 23;
                }
                else {
                    n3 = 0;
                }
                linearLayout.addView((View)editTextBoldCursor, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -1, n2, 0, 0, n3, 0));
                this.colorEditText[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                    public void afterTextChanged(final Editable editable) {
                        final ColorPicker this$1 = ColorPicker.this;
                        if (this$1.ignoreTextChange) {
                            return;
                        }
                        this$1.ignoreTextChange = true;
                        if (i == 0) {
                            int n;
                            for (int i = 0; i < editable.length(); i = n + 1) {
                                final char char1 = editable.charAt(i);
                                if (char1 >= '0') {
                                    n = i;
                                    if (char1 <= '9') {
                                        continue;
                                    }
                                }
                                if (char1 >= 'a') {
                                    n = i;
                                    if (char1 <= 'f') {
                                        continue;
                                    }
                                }
                                if (char1 >= 'A') {
                                    n = i;
                                    if (char1 <= 'F') {
                                        continue;
                                    }
                                }
                                if (char1 != '#' || (n = i) != 0) {
                                    editable.replace(i, i + 1, (CharSequence)"");
                                    n = i - 1;
                                }
                            }
                            if (editable.length() == 0) {
                                editable.append((CharSequence)"#");
                            }
                            else if (editable.charAt(0) != '#') {
                                editable.insert(0, (CharSequence)"#");
                            }
                            try {
                                ColorPicker.this.setColor(Integer.parseInt(editable.toString().substring(1), 16) | 0xFF000000);
                            }
                            catch (Exception ex) {
                                ColorPicker.this.setColor(-1);
                            }
                            final ColorPicker this$2 = ColorPicker.this;
                            WallpaperActivity.this.setBackgroundColor(this$2.getColor());
                            final EditTextBoldCursor editTextBoldCursor = ColorPicker.this.colorEditText[1];
                            final StringBuilder sb = new StringBuilder();
                            sb.append("");
                            sb.append((int)(ColorPicker.this.colorHSV[2] * 255.0f));
                            editTextBoldCursor.setText((CharSequence)sb.toString());
                        }
                        else {
                            final int intValue = Utilities.parseInt(editable.toString());
                            int j;
                            if (intValue > 255 || (j = intValue) < 0) {
                                if (intValue > 255) {
                                    j = 255;
                                }
                                else {
                                    j = 0;
                                }
                                final int length = editable.length();
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("");
                                sb2.append(j);
                                editable.replace(0, length, (CharSequence)sb2.toString());
                            }
                            ColorPicker.this.colorHSV[2] = j / 255.0f;
                            final ColorPicker this$3 = ColorPicker.this;
                            WallpaperActivity.this.setBackgroundColor(this$3.getColor());
                            ColorPicker.this.colorEditText[0].setText((CharSequence)String.format("#%02x%02x%02x", (byte)Color.red(WallpaperActivity.this.backgroundColor), (byte)Color.green(WallpaperActivity.this.backgroundColor), (byte)Color.blue(WallpaperActivity.this.backgroundColor)));
                        }
                        ColorPicker.this.invalidate();
                        ColorPicker.this.ignoreTextChange = false;
                    }
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                });
                this.colorEditText[i].setOnEditorActionListener((TextView$OnEditorActionListener)_$$Lambda$WallpaperActivity$ColorPicker$DX_Z3G7ZN8lj8LNj53R64xuAIvg.INSTANCE);
            }
        }
        
        private Bitmap createColorWheelBitmap(final int n, final int n2) {
            final Bitmap bitmap = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
            final int[] array = new int[13];
            final float[] array3;
            final float[] array2 = array3 = new float[3];
            array3[0] = 0.0f;
            array3[2] = (array3[1] = 1.0f);
            for (int i = 0; i < array.length; ++i) {
                array2[0] = (float)((i * 30 + 180) % 360);
                array[i] = Color.HSVToColor(array2);
            }
            array[12] = array[0];
            final float n3 = (float)(n / 2);
            final float n4 = (float)(n2 / 2);
            this.colorWheelPaint.setShader((Shader)new ComposeShader((Shader)new SweepGradient(n3, n4, array, (float[])null), (Shader)new RadialGradient(n3, n4, (float)this.colorWheelRadius, -1, 16777215, Shader$TileMode.CLAMP), PorterDuff$Mode.SRC_OVER));
            new Canvas(bitmap).drawCircle(n3, n4, (float)this.colorWheelRadius, this.colorWheelPaint);
            return bitmap;
        }
        
        private void drawPointerArrow(final Canvas canvas, final int n, final int n2, final int color) {
            final int dp = AndroidUtilities.dp(13.0f);
            this.circleDrawable.setBounds(n - dp, n2 - dp, n + dp, dp + n2);
            this.circleDrawable.draw(canvas);
            this.circlePaint.setColor(-1);
            final float n3 = (float)n;
            final float n4 = (float)n2;
            canvas.drawCircle(n3, n4, (float)AndroidUtilities.dp(11.0f), this.circlePaint);
            this.circlePaint.setColor(color);
            canvas.drawCircle(n3, n4, (float)AndroidUtilities.dp(9.0f), this.circlePaint);
        }
        
        public int getColor() {
            return (Color.HSVToColor(this.colorHSV) & 0xFFFFFF) | 0xFF000000;
        }
        
        protected void onDraw(final Canvas canvas) {
            this.centerX = this.getWidth() / 2 - this.paramValueSliderWidth * 2 + AndroidUtilities.dp(11.0f);
            this.centerY = this.getHeight() / 2 + AndroidUtilities.dp(34.0f);
            final Bitmap colorWheelBitmap = this.colorWheelBitmap;
            final int centerX = this.centerX;
            final int colorWheelRadius = this.colorWheelRadius;
            canvas.drawBitmap(colorWheelBitmap, (float)(centerX - colorWheelRadius), (float)(this.centerY - colorWheelRadius), (Paint)null);
            final double n = (float)Math.toRadians(this.colorHSV[0]);
            final double n2 = -Math.cos(n);
            final double v = this.colorHSV[1];
            Double.isNaN(v);
            final double v2 = this.colorWheelRadius;
            Double.isNaN(v2);
            final int n3 = (int)(n2 * v * v2);
            final int centerX2 = this.centerX;
            final double n4 = -Math.sin(n);
            final float[] colorHSV = this.colorHSV;
            final double v3 = colorHSV[1];
            Double.isNaN(v3);
            final double v4 = this.colorWheelRadius;
            Double.isNaN(v4);
            final int n5 = (int)(n4 * v3 * v4);
            final int centerY = this.centerY;
            final float[] hsvTemp = this.hsvTemp;
            hsvTemp[0] = colorHSV[0];
            hsvTemp[1] = colorHSV[1];
            hsvTemp[2] = 1.0f;
            this.drawPointerArrow(canvas, n3 + centerX2, n5 + centerY, Color.HSVToColor(hsvTemp));
            final int centerX3 = this.centerX;
            final int colorWheelRadius2 = this.colorWheelRadius;
            this.lx = centerX3 + colorWheelRadius2 + this.paramValueSliderWidth * 2;
            this.ly = this.centerY - colorWheelRadius2;
            final int dp = AndroidUtilities.dp(9.0f);
            final int n6 = this.colorWheelRadius * 2;
            if (this.colorGradient == null) {
                final int lx = this.lx;
                final float n7 = (float)lx;
                final int ly = this.ly;
                this.colorGradient = new LinearGradient(n7, (float)ly, (float)(lx + dp), (float)(ly + n6), new int[] { -16777216, Color.HSVToColor(this.hsvTemp) }, (float[])null, Shader$TileMode.CLAMP);
            }
            this.valueSliderPaint.setShader((Shader)this.colorGradient);
            final int lx2 = this.lx;
            final float n8 = (float)lx2;
            final int ly2 = this.ly;
            canvas.drawRect(n8, (float)ly2, (float)(lx2 + dp), (float)(ly2 + n6), this.valueSliderPaint);
            final int lx3 = this.lx;
            final int n9 = dp / 2;
            final float n10 = (float)this.ly;
            final float[] colorHSV2 = this.colorHSV;
            this.drawPointerArrow(canvas, lx3 + n9, (int)(n10 + colorHSV2[2] * n6), Color.HSVToColor(colorHSV2));
        }
        
        protected void onMeasure(int min, final int n) {
            final int size = View$MeasureSpec.getSize(min);
            min = Math.min(size, View$MeasureSpec.getSize(n));
            this.measureChild((View)this.linearLayout, View$MeasureSpec.makeMeasureSpec(size - AndroidUtilities.dp(42.0f), 1073741824), n);
            this.setMeasuredDimension(min, min);
        }
        
        protected void onSizeChanged(int colorWheelRadius, final int n, final int n2, final int n3) {
            if (this.colorWheelRadius != AndroidUtilities.dp(120.0f)) {
                this.colorWheelRadius = AndroidUtilities.dp(120.0f);
                colorWheelRadius = this.colorWheelRadius;
                this.colorWheelBitmap = this.createColorWheelBitmap(colorWheelRadius * 2, colorWheelRadius * 2);
                this.colorGradient = null;
            }
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final int action = motionEvent.getAction();
            Label_0038: {
                if (action != 0) {
                    if (action != 1) {
                        if (action == 2) {
                            break Label_0038;
                        }
                    }
                    else {
                        this.colorPressed = false;
                        this.circlePressed = false;
                    }
                    return super.onTouchEvent(motionEvent);
                }
            }
            final int n = (int)motionEvent.getX();
            final int n2 = (int)motionEvent.getY();
            final int n3 = n - this.centerX;
            final int n4 = n2 - this.centerY;
            final double sqrt = Math.sqrt(n3 * n3 + n4 * n4);
            if (this.circlePressed || (!this.colorPressed && sqrt <= this.colorWheelRadius)) {
                final int colorWheelRadius = this.colorWheelRadius;
                double n5 = sqrt;
                if (sqrt > colorWheelRadius) {
                    n5 = colorWheelRadius;
                }
                if (!this.circlePressed) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
                this.circlePressed = true;
                this.colorHSV[0] = (float)(Math.toDegrees(Math.atan2(n4, n3)) + 180.0);
                final float[] colorHSV = this.colorHSV;
                final double v = this.colorWheelRadius;
                Double.isNaN(v);
                colorHSV[1] = Math.max(0.0f, Math.min(1.0f, (float)(n5 / v)));
                this.colorGradient = null;
            }
            Label_0350: {
                if (!this.colorPressed) {
                    if (this.circlePressed) {
                        break Label_0350;
                    }
                    final int lx = this.lx;
                    if (n < lx || n > lx + this.paramValueSliderWidth) {
                        break Label_0350;
                    }
                    final int ly = this.ly;
                    if (n2 < ly || n2 > ly + this.colorWheelRadius * 2) {
                        break Label_0350;
                    }
                }
                final float n6 = (n2 - this.ly) / (this.colorWheelRadius * 2.0f);
                float n7;
                if (n6 < 0.0f) {
                    n7 = 0.0f;
                }
                else {
                    n7 = n6;
                    if (n6 > 1.0f) {
                        n7 = 1.0f;
                    }
                }
                this.colorHSV[2] = n7;
                if (!this.colorPressed) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
                this.colorPressed = true;
            }
            if (this.colorPressed || this.circlePressed) {
                WallpaperActivity.this.setBackgroundColor(this.getColor());
                if (!this.ignoreTextChange) {
                    final int red = Color.red(WallpaperActivity.this.backgroundColor);
                    final int green = Color.green(WallpaperActivity.this.backgroundColor);
                    final int blue = Color.blue(WallpaperActivity.this.backgroundColor);
                    this.ignoreTextChange = true;
                    this.colorEditText[0].setText((CharSequence)String.format("#%02x%02x%02x", (byte)red, (byte)green, (byte)blue));
                    final EditTextBoldCursor editTextBoldCursor = this.colorEditText[1];
                    final StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append((int)(this.colorHSV[2] * 255.0f));
                    editTextBoldCursor.setText((CharSequence)sb.toString());
                    for (int i = 0; i < 2; ++i) {
                        final EditTextBoldCursor[] colorEditText = this.colorEditText;
                        colorEditText[i].setSelection(colorEditText[i].length());
                    }
                    this.ignoreTextChange = false;
                }
                this.invalidate();
            }
            return true;
        }
        
        public void setColor(int i) {
            if (!this.ignoreTextChange) {
                this.ignoreTextChange = true;
                final int red = Color.red(i);
                final int green = Color.green(i);
                final int blue = Color.blue(i);
                Color.colorToHSV(i, this.colorHSV);
                this.colorEditText[0].setText((CharSequence)String.format("#%02x%02x%02x", (byte)red, (byte)green, (byte)blue));
                final EditTextBoldCursor editTextBoldCursor = this.colorEditText[1];
                final StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append((int)(this.colorHSV[2] * 255.0f));
                editTextBoldCursor.setText((CharSequence)sb.toString());
                EditTextBoldCursor[] colorEditText;
                for (i = 0; i < 2; ++i) {
                    colorEditText = this.colorEditText;
                    colorEditText[i].setSelection(colorEditText[i].length());
                }
                this.ignoreTextChange = false;
            }
            else {
                Color.colorToHSV(i, this.colorHSV);
            }
            this.colorGradient = null;
            this.invalidate();
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        private ArrayList<MessageObject> messages;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
            this.messages = new ArrayList<MessageObject>();
            final int date = (int)(System.currentTimeMillis() / 1000L) - 3600;
            final TLRPC.TL_message tl_message = new TLRPC.TL_message();
            if (WallpaperActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                tl_message.message = LocaleController.getString("BackgroundColorSinglePreviewLine2", 2131558816);
            }
            else {
                tl_message.message = LocaleController.getString("BackgroundPreviewLine2", 2131558823);
            }
            final int n = date + 60;
            tl_message.date = n;
            tl_message.dialog_id = 1L;
            tl_message.flags = 259;
            tl_message.from_id = UserConfig.getInstance(WallpaperActivity.this.currentAccount).getClientUserId();
            tl_message.id = 1;
            tl_message.media = new TLRPC.TL_messageMediaEmpty();
            tl_message.out = true;
            tl_message.to_id = new TLRPC.TL_peerUser();
            tl_message.to_id.user_id = 0;
            final MessageObject e = new MessageObject(WallpaperActivity.this.currentAccount, tl_message, true);
            e.eventId = 1L;
            e.resetLayout();
            this.messages.add(e);
            final TLRPC.TL_message tl_message2 = new TLRPC.TL_message();
            if (WallpaperActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                tl_message2.message = LocaleController.getString("BackgroundColorSinglePreviewLine1", 2131558815);
            }
            else {
                tl_message2.message = LocaleController.getString("BackgroundPreviewLine1", 2131558822);
            }
            tl_message2.date = n;
            tl_message2.dialog_id = 1L;
            tl_message2.flags = 265;
            tl_message2.from_id = 0;
            tl_message2.id = 1;
            tl_message2.reply_to_msg_id = 5;
            tl_message2.media = new TLRPC.TL_messageMediaEmpty();
            tl_message2.out = false;
            tl_message2.to_id = new TLRPC.TL_peerUser();
            tl_message2.to_id.user_id = UserConfig.getInstance(WallpaperActivity.this.currentAccount).getClientUserId();
            final MessageObject e2 = new MessageObject(WallpaperActivity.this.currentAccount, tl_message2, true);
            e2.eventId = 1L;
            e2.resetLayout();
            this.messages.add(e2);
            final TLRPC.TL_message tl_message3 = new TLRPC.TL_message();
            tl_message3.message = LocaleController.formatDateChat(date);
            tl_message3.id = 0;
            tl_message3.date = date;
            final MessageObject e3 = new MessageObject(WallpaperActivity.this.currentAccount, tl_message3, false);
            e3.type = 10;
            e3.contentType = 1;
            e3.isDateObject = true;
            this.messages.add(e3);
        }
        
        @Override
        public int getItemCount() {
            return this.messages.size();
        }
        
        @Override
        public int getItemViewType(final int index) {
            if (index >= 0 && index < this.messages.size()) {
                return this.messages.get(index).contentType;
            }
            return 4;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int itemViewType) {
            final MessageObject messageObject = this.messages.get(itemViewType);
            final View itemView = viewHolder.itemView;
            if (itemView instanceof ChatMessageCell) {
                final ChatMessageCell chatMessageCell = (ChatMessageCell)itemView;
                final boolean b = false;
                chatMessageCell.isChat = false;
                final int index = itemViewType - 1;
                final int itemViewType2 = this.getItemViewType(index);
                final int index2 = itemViewType + 1;
                itemViewType = this.getItemViewType(index2);
                boolean b2 = false;
                Label_0149: {
                    if (!(messageObject.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) && itemViewType2 == viewHolder.getItemViewType()) {
                        final MessageObject messageObject2 = this.messages.get(index);
                        if (messageObject2.isOutOwner() == messageObject.isOutOwner() && Math.abs(messageObject2.messageOwner.date - messageObject.messageOwner.date) <= 300) {
                            b2 = true;
                            break Label_0149;
                        }
                    }
                    b2 = false;
                }
                boolean b3 = b;
                if (itemViewType == viewHolder.getItemViewType()) {
                    final MessageObject messageObject3 = this.messages.get(index2);
                    b3 = b;
                    if (!(messageObject3.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup)) {
                        b3 = b;
                        if (messageObject3.isOutOwner() == messageObject.isOutOwner()) {
                            b3 = b;
                            if (Math.abs(messageObject3.messageOwner.date - messageObject.messageOwner.date) <= 300) {
                                b3 = true;
                            }
                        }
                    }
                }
                chatMessageCell.setFullyDraw(true);
                chatMessageCell.setMessageObject(messageObject, null, b2, b3);
            }
            else if (itemView instanceof ChatActionCell) {
                final ChatActionCell chatActionCell = (ChatActionCell)itemView;
                chatActionCell.setMessageObject(messageObject);
                chatActionCell.setAlpha(1.0f);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            BaseCell baseCell;
            if (n == 0) {
                baseCell = new ChatMessageCell(this.mContext);
                ((ChatMessageCell)baseCell).setDelegate((ChatMessageCell.ChatMessageCellDelegate)new ChatMessageCell.ChatMessageCellDelegate() {});
            }
            else if (n == 1) {
                baseCell = new ChatActionCell(this.mContext);
                ((ChatActionCell)baseCell).setDelegate((ChatActionCell.ChatActionCellDelegate)new ChatActionCell.ChatActionCellDelegate() {});
            }
            else {
                baseCell = null;
            }
            ((View)baseCell).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)baseCell);
        }
    }
    
    private class PatternCell extends BackupImageView implements FileDownloadProgressListener
    {
        private int TAG;
        private TLRPC.TL_wallPaper currentPattern;
        private RadialProgress2 radialProgress;
        private RectF rect;
        private boolean wasSelected;
        
        public PatternCell(final Context context) {
            super(context);
            this.rect = new RectF();
            this.setRoundRadius(AndroidUtilities.dp(6.0f));
            (this.radialProgress = new RadialProgress2(this)).setProgressRect(AndroidUtilities.dp(30.0f), AndroidUtilities.dp(30.0f), AndroidUtilities.dp(70.0f), AndroidUtilities.dp(70.0f));
            this.TAG = DownloadController.getInstance(WallpaperActivity.this.currentAccount).generateObserverTag();
        }
        
        private void setPattern(final TLRPC.TL_wallPaper currentPattern) {
            this.currentPattern = currentPattern;
            if (currentPattern != null) {
                this.setImage(ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(currentPattern.document.thumbs, 100), currentPattern.document), "100_100", null, null, "jpg", 0, 1, currentPattern);
            }
            else {
                this.setImageDrawable(null);
            }
            this.updateSelected(false);
        }
        
        @Override
        public int getObserverTag() {
            return this.TAG;
        }
        
        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.updateSelected(false);
        }
        
        @Override
        protected void onDraw(final Canvas canvas) {
            this.getImageReceiver().setAlpha(0.8f);
            WallpaperActivity.this.backgroundPaint.setColor(WallpaperActivity.this.backgroundColor);
            this.rect.set(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
            canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(6.0f), (float)AndroidUtilities.dp(6.0f), WallpaperActivity.this.backgroundPaint);
            super.onDraw(canvas);
            this.radialProgress.setColors(WallpaperActivity.this.patternColor, WallpaperActivity.this.patternColor, -1, -1);
            this.radialProgress.draw(canvas);
        }
        
        @Override
        public void onFailedDownload(final String s, final boolean b) {
            WallpaperActivity.this.updateButtonState(this.radialProgress, this.currentPattern, this, true, b);
        }
        
        protected void onMeasure(final int n, final int n2) {
            this.setMeasuredDimension(AndroidUtilities.dp(100.0f), AndroidUtilities.dp(100.0f));
        }
        
        @Override
        public void onProgressDownload(final String s, final float n) {
            this.radialProgress.setProgress(n, WallpaperActivity.this.progressVisible);
            if (this.radialProgress.getIcon() != 10) {
                WallpaperActivity.this.updateButtonState(this.radialProgress, this.currentPattern, this, false, true);
            }
        }
        
        @Override
        public void onProgressUpload(final String s, final float n, final boolean b) {
        }
        
        @Override
        public void onSuccessDownload(final String s) {
            this.radialProgress.setProgress(1.0f, WallpaperActivity.this.progressVisible);
            WallpaperActivity.this.updateButtonState(this.radialProgress, this.currentPattern, this, false, true);
        }
        
        public void updateSelected(final boolean b) {
            boolean b2 = false;
            Label_0061: {
                Label_0054: {
                    if (this.currentPattern != null || WallpaperActivity.this.selectedPattern != null) {
                        if (WallpaperActivity.this.selectedPattern != null) {
                            final TLRPC.TL_wallPaper currentPattern = this.currentPattern;
                            if (currentPattern != null && currentPattern.id == WallpaperActivity.this.selectedPattern.id) {
                                break Label_0054;
                            }
                        }
                        b2 = false;
                        break Label_0061;
                    }
                }
                b2 = true;
            }
            if (b2) {
                final WallpaperActivity this$0 = WallpaperActivity.this;
                this$0.updateButtonState(this.radialProgress, this$0.selectedPattern, this, false, b);
            }
            else {
                this.radialProgress.setIcon(4, false, b);
            }
            this.invalidate();
        }
    }
    
    private class PatternsAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public PatternsAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            int size;
            if (WallpaperActivity.this.patterns != null) {
                size = WallpaperActivity.this.patterns.size();
            }
            else {
                size = 0;
            }
            return size + 1;
        }
        
        @Override
        public int getItemViewType(final int n) {
            return super.getItemViewType(n);
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final PatternCell patternCell = (PatternCell)viewHolder.itemView;
            if (n == 0) {
                patternCell.setPattern(null);
            }
            else {
                patternCell.setPattern(WallpaperActivity.this.patterns.get(n - 1));
            }
            patternCell.getImageReceiver().setColorFilter((ColorFilter)new PorterDuffColorFilter(WallpaperActivity.this.patternColor, WallpaperActivity.this.blendMode));
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            return new RecyclerListView.Holder(new PatternCell(this.mContext));
        }
    }
    
    public interface WallpaperActivityDelegate
    {
        void didSetNewBackground();
    }
}
