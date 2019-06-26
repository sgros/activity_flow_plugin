// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import java.util.Iterator;
import android.graphics.Paint$Cap;
import org.telegram.ui.Components.TableLayout;
import android.database.DataSetObserver;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.PagerAdapter;
import android.graphics.drawable.ColorDrawable;
import org.telegram.ui.Components.CombinedDrawable;
import android.widget.FrameLayout$LayoutParams;
import android.widget.HorizontalScrollView;
import java.util.Locale;
import org.telegram.messenger.WebFile;
import org.telegram.ui.Components.AvatarDrawable;
import android.webkit.JavascriptInterface;
import org.telegram.messenger.Utilities;
import org.json.JSONObject;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.CookieManager;
import java.util.Map;
import android.graphics.drawable.Drawable$Callback;
import java.util.HashMap;
import androidx.recyclerview.widget.GridLayoutManagerFixed;
import androidx.recyclerview.widget.GridLayoutManager;
import android.text.Spannable$Factory;
import android.view.accessibility.AccessibilityNodeInfo;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.messenger.DownloadController;
import android.view.ViewConfiguration;
import android.content.DialogInterface$OnDismissListener;
import android.text.method.MovementMethod;
import org.telegram.ui.ActionBar.ActionBarMenu;
import androidx.recyclerview.widget.DefaultItemAnimator;
import android.view.View$OnApplyWindowInsetsListener;
import android.widget.Toast;
import android.view.KeyEvent;
import android.content.DialogInterface;
import java.util.List;
import android.view.DisplayCutout;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.AnimatedArrowDrawable;
import android.util.LongSparseArray;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.messenger.MessagesStorage;
import androidx.annotation.Keep;
import org.telegram.messenger.MediaController;
import android.graphics.RectF;
import android.view.WindowInsets;
import android.content.SharedPreferences;
import java.util.Collection;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.PopupWindow$OnDismissListener;
import android.view.View$OnClickListener;
import android.view.View$OnTouchListener;
import android.widget.ImageView$ScaleType;
import android.graphics.drawable.BitmapDrawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.Emoji;
import android.text.style.URLSpan;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View$MeasureSpec;
import android.widget.LinearLayout$LayoutParams;
import android.widget.LinearLayout;
import android.graphics.Point;
import android.content.SharedPreferences$Editor;
import org.telegram.messenger.ApplicationLoader;
import android.graphics.SurfaceTexture;
import org.telegram.ui.Components.LayoutHelper;
import android.os.Bundle;
import android.view.ViewGroup$LayoutParams;
import android.view.WindowManager;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.LocaleController;
import android.net.Uri;
import android.os.Parcelable;
import android.content.Context;
import androidx.core.content.FileProvider;
import android.os.Build$VERSION;
import android.content.Intent;
import android.graphics.Bitmap;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessageObject;
import android.graphics.Typeface;
import org.telegram.ui.Components.TextPaintImageReceiverSpan;
import org.telegram.ui.Components.TextPaintMarkSpan;
import org.telegram.ui.Components.TextPaintSpan;
import org.telegram.ui.Components.AnchorSpan;
import org.telegram.ui.Components.TextPaintWebpageUrlSpan;
import android.text.style.MetricAffectingSpan;
import android.text.SpannableStringBuilder;
import java.util.Calendar;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup;
import android.graphics.Paint$Style;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.IntEvaluator;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.FileLoader;
import android.text.StaticLayout;
import java.net.URLDecoder;
import android.text.TextUtils;
import org.telegram.messenger.FileLog;
import android.graphics.Path;
import android.text.Spannable;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import org.telegram.tgnet.TLObject;
import java.io.File;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.text.Layout$Alignment;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.AnimationProperties;
import android.view.WindowManager$LayoutParams;
import android.app.Dialog;
import org.telegram.ui.Components.SeekBar;
import org.telegram.ui.Components.VideoPlayer;
import android.view.VelocityTracker;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.Components.Scroller;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.TextPaintUrlSpan;
import android.graphics.Rect;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import android.app.Activity;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.Components.LineProgressView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.Components.GroupedPhotosListView;
import android.view.GestureDetector;
import android.view.TextureView;
import android.webkit.WebChromeClient$CustomViewCallback;
import android.view.View;
import org.telegram.ui.Components.WebPlayerView;
import org.telegram.ui.Components.AnimatedFileDrawable;
import android.animation.AnimatorSet;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.ImageReceiver;
import android.widget.TextView;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.BackDrawable;
import android.widget.ImageView;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import org.telegram.ui.Components.ClippingImageView;
import org.telegram.ui.ActionBar.ActionBar;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import android.graphics.Paint;
import android.util.SparseArray;
import android.text.TextPaint;
import android.annotation.SuppressLint;
import android.util.Property;
import android.view.GestureDetector$OnDoubleTapListener;
import android.view.GestureDetector$OnGestureListener;
import org.telegram.messenger.NotificationCenter;

public class ArticleViewer implements NotificationCenterDelegate, GestureDetector$OnGestureListener, GestureDetector$OnDoubleTapListener
{
    public static final Property<WindowView, Float> ARTICLE_VIEWER_INNER_TRANSLATION_X;
    @SuppressLint({ "StaticFieldLeak" })
    private static volatile ArticleViewer Instance;
    private static final int TEXT_FLAG_ITALIC = 2;
    private static final int TEXT_FLAG_MARKED = 64;
    private static final int TEXT_FLAG_MEDIUM = 1;
    private static final int TEXT_FLAG_MONO = 4;
    private static final int TEXT_FLAG_REGULAR = 0;
    private static final int TEXT_FLAG_STRIKE = 32;
    private static final int TEXT_FLAG_SUB = 128;
    private static final int TEXT_FLAG_SUP = 256;
    private static final int TEXT_FLAG_UNDERLINE = 16;
    private static final int TEXT_FLAG_URL = 8;
    private static final int TEXT_FLAG_WEBPAGE_URL = 512;
    private static TextPaint audioTimePaint;
    private static SparseArray<TextPaint> authorTextPaints;
    private static TextPaint channelNamePaint;
    private static Paint colorPaint;
    private static DecelerateInterpolator decelerateInterpolator;
    private static SparseArray<TextPaint> detailsTextPaints;
    private static Paint dividerPaint;
    private static Paint dotsPaint;
    private static TextPaint embedPostAuthorPaint;
    private static SparseArray<TextPaint> embedPostCaptionTextPaints;
    private static TextPaint embedPostDatePaint;
    private static SparseArray<TextPaint> embedPostTextPaints;
    private static TextPaint errorTextPaint;
    private static SparseArray<TextPaint> footerTextPaints;
    private static final int gallery_menu_openin = 3;
    private static final int gallery_menu_save = 1;
    private static final int gallery_menu_share = 2;
    private static SparseArray<TextPaint> headerTextPaints;
    private static SparseArray<TextPaint> kickerTextPaints;
    private static TextPaint listTextNumPaint;
    private static SparseArray<TextPaint> listTextPaints;
    private static TextPaint listTextPointerPaint;
    private static SparseArray<TextPaint> mediaCaptionTextPaints;
    private static SparseArray<TextPaint> mediaCreditTextPaints;
    private static SparseArray<TextPaint> paragraphTextPaints;
    private static Paint photoBackgroundPaint;
    private static SparseArray<TextPaint> photoCaptionTextPaints;
    private static SparseArray<TextPaint> photoCreditTextPaints;
    private static Paint preformattedBackgroundPaint;
    private static SparseArray<TextPaint> preformattedTextPaints;
    private static Drawable[] progressDrawables;
    private static Paint progressPaint;
    private static Paint quoteLinePaint;
    private static SparseArray<TextPaint> quoteTextPaints;
    private static TextPaint relatedArticleHeaderPaint;
    private static TextPaint relatedArticleTextPaint;
    private static SparseArray<TextPaint> relatedArticleTextPaints;
    private static Paint selectorPaint;
    private static SparseArray<TextPaint> subheaderTextPaints;
    private static SparseArray<TextPaint> subtitleTextPaints;
    private static Paint tableHalfLinePaint;
    private static Paint tableHeaderPaint;
    private static Paint tableLinePaint;
    private static Paint tableStripPaint;
    private static SparseArray<TextPaint> tableTextPaints;
    private static SparseArray<TextPaint> titleTextPaints;
    private static Paint urlPaint;
    private static Paint webpageMarkPaint;
    private static Paint webpageUrlPaint;
    private ActionBar actionBar;
    private WebpageAdapter[] adapter;
    private int anchorsOffsetMeasuredWidth;
    private float animateToScale;
    private float animateToX;
    private float animateToY;
    private ClippingImageView animatingImageView;
    private Runnable animationEndRunnable;
    private int animationInProgress;
    private long animationStartTime;
    private float animationValue;
    private float[][] animationValues;
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private boolean attachedToWindow;
    private ImageView backButton;
    private BackDrawable backDrawable;
    private Paint backgroundPaint;
    private Paint blackPaint;
    private FrameLayout bottomLayout;
    private boolean canDragDown;
    private boolean canZoom;
    private TextView captionTextView;
    private TextView captionTextViewNext;
    private ImageReceiver centerImage;
    private boolean changingPage;
    private TLRPC.TL_pageBlockChannel channelBlock;
    private boolean checkingForLongPress;
    private boolean collapsed;
    private ColorCell[] colorCells;
    private FrameLayout containerView;
    private int[] coords;
    private Drawable copyBackgroundDrawable;
    private ArrayList<BlockEmbedCell> createdWebViews;
    private int currentAccount;
    private AnimatorSet currentActionBarAnimation;
    private AnimatedFileDrawable currentAnimation;
    private String[] currentFileNames;
    private int currentHeaderHeight;
    private int currentIndex;
    private TLRPC.PageBlock currentMedia;
    private TLRPC.WebPage currentPage;
    private PlaceProviderObject currentPlaceObject;
    private WebPlayerView currentPlayingVideo;
    private int currentRotation;
    private ImageReceiver.BitmapHolder currentThumb;
    private View customView;
    private WebChromeClient$CustomViewCallback customViewCallback;
    private TextView deleteView;
    private boolean disableShowCheck;
    private boolean discardTap;
    private boolean dontResetZoomOnFirstLayout;
    private boolean doubleTap;
    private float dragY;
    private boolean draggingDown;
    private boolean drawBlockSelection;
    private FontCell[] fontCells;
    private final int fontSizeCount;
    private AspectRatioFrameLayout fullscreenAspectRatioView;
    private TextureView fullscreenTextureView;
    private FrameLayout fullscreenVideoContainer;
    private WebPlayerView fullscreenedVideo;
    private GestureDetector gestureDetector;
    private GroupedPhotosListView groupedPhotosListView;
    boolean hasCutout;
    private Paint headerPaint;
    private Paint headerProgressPaint;
    private FrameLayout headerView;
    private PlaceProviderObject hideAfterAnimation;
    private AnimatorSet imageMoveAnimation;
    private ArrayList<TLRPC.PageBlock> imagesArr;
    private DecelerateInterpolator interpolator;
    private boolean invalidCoords;
    private boolean isActionBarVisible;
    private boolean isPhotoVisible;
    private boolean isPlaying;
    private boolean isRtl;
    private boolean isVisible;
    private int lastBlockNum;
    private Object lastInsets;
    private int lastReqId;
    private Drawable layerShadowDrawable;
    private LinearLayoutManager[] layoutManager;
    private ImageReceiver leftImage;
    private Runnable lineProgressTickRunnable;
    private LineProgressView lineProgressView;
    private BottomSheet linkSheet;
    private RecyclerListView[] listView;
    private TLRPC.Chat loadedChannel;
    private boolean loadingChannel;
    private float maxX;
    private float maxY;
    private ActionBarMenuItem menuItem;
    private float minX;
    private float minY;
    private float moveStartX;
    private float moveStartY;
    private boolean moving;
    private boolean nightModeEnabled;
    private FrameLayout nightModeHintView;
    private ImageView nightModeImageView;
    private int openUrlReqId;
    private AnimatorSet pageSwitchAnimation;
    private ArrayList<TLRPC.WebPage> pagesStack;
    private Activity parentActivity;
    private BaseFragment parentFragment;
    private CheckForLongPress pendingCheckForLongPress;
    private CheckForTap pendingCheckForTap;
    private Runnable photoAnimationEndRunnable;
    private int photoAnimationInProgress;
    private PhotoBackgroundDrawable photoBackgroundDrawable;
    private View photoContainerBackground;
    private FrameLayoutDrawer photoContainerView;
    private long photoTransitionAnimationStartTime;
    private float pinchCenterX;
    private float pinchCenterY;
    private float pinchStartDistance;
    private float pinchStartScale;
    private float pinchStartX;
    private float pinchStartY;
    private ActionBarPopupWindow.ActionBarPopupWindowLayout popupLayout;
    private Rect popupRect;
    private ActionBarPopupWindow popupWindow;
    private int pressCount;
    private int pressedLayoutY;
    private TextPaintUrlSpan pressedLink;
    private DrawingText pressedLinkOwnerLayout;
    private View pressedLinkOwnerView;
    private int previewsReqId;
    private ContextProgressView progressView;
    private AnimatorSet progressViewAnimation;
    private RadialProgressView[] radialProgressViews;
    private ImageReceiver rightImage;
    private float scale;
    private Paint scrimPaint;
    private Scroller scroller;
    private int selectedColor;
    private int selectedFont;
    private int selectedFontSize;
    private ActionBarMenuItem settingsButton;
    private ImageView shareButton;
    private FrameLayout shareContainer;
    private PlaceProviderObject showAfterAnimation;
    private Drawable slideDotBigDrawable;
    private Drawable slideDotDrawable;
    private Paint statusBarPaint;
    private int switchImageAfterAnimation;
    private boolean textureUploaded;
    private SimpleTextView titleTextView;
    private long transitionAnimationStartTime;
    private float translationX;
    private float translationY;
    private Runnable updateProgressRunnable;
    private LinkPath urlPath;
    private VelocityTracker velocityTracker;
    private float videoCrossfadeAlpha;
    private long videoCrossfadeAlphaLastTime;
    private boolean videoCrossfadeStarted;
    private ImageView videoPlayButton;
    private VideoPlayer videoPlayer;
    private FrameLayout videoPlayerControlFrameLayout;
    private SeekBar videoPlayerSeekbar;
    private TextView videoPlayerTime;
    private TextureView videoTextureView;
    private Dialog visibleDialog;
    private boolean wasLayout;
    private WindowManager$LayoutParams windowLayoutParams;
    private WindowView windowView;
    private boolean zoomAnimation;
    private boolean zooming;
    
    static {
        ARTICLE_VIEWER_INNER_TRANSLATION_X = new AnimationProperties.FloatProperty<WindowView>("innerTranslationX") {
            public Float get(final WindowView windowView) {
                return windowView.getInnerTranslationX();
            }
            
            public void setValue(final WindowView windowView, final float innerTranslationX) {
                windowView.setInnerTranslationX(innerTranslationX);
            }
        };
        ArticleViewer.audioTimePaint = new TextPaint(1);
        ArticleViewer.photoCaptionTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.photoCreditTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.titleTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.kickerTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.headerTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.subtitleTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.subheaderTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.authorTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.footerTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.paragraphTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.listTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.preformattedTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.quoteTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.embedPostTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.embedPostCaptionTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.mediaCaptionTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.mediaCreditTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.relatedArticleTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.detailsTextPaints = (SparseArray<TextPaint>)new SparseArray();
        ArticleViewer.tableTextPaints = (SparseArray<TextPaint>)new SparseArray();
    }
    
    public ArticleViewer() {
        this.createdWebViews = new ArrayList<BlockEmbedCell>();
        this.lastBlockNum = 1;
        this.pagesStack = new ArrayList<TLRPC.WebPage>();
        this.headerPaint = new Paint();
        this.statusBarPaint = new Paint();
        this.headerProgressPaint = new Paint();
        this.checkingForLongPress = false;
        this.pendingCheckForLongPress = null;
        this.pressCount = 0;
        this.pendingCheckForTap = null;
        this.urlPath = new LinkPath();
        this.fontSizeCount = 5;
        this.selectedFontSize = 2;
        this.selectedColor = 0;
        this.selectedFont = 0;
        this.colorCells = new ColorCell[3];
        this.fontCells = new FontCell[2];
        this.coords = new int[2];
        this.isActionBarVisible = true;
        this.photoBackgroundDrawable = new PhotoBackgroundDrawable(-16777216);
        this.blackPaint = new Paint();
        this.radialProgressViews = new RadialProgressView[3];
        this.updateProgressRunnable = new Runnable() {
            @Override
            public void run() {
                if (ArticleViewer.this.videoPlayer != null && ArticleViewer.this.videoPlayerSeekbar != null && !ArticleViewer.this.videoPlayerSeekbar.isDragging()) {
                    ArticleViewer.this.videoPlayerSeekbar.setProgress(ArticleViewer.this.videoPlayer.getCurrentPosition() / (float)ArticleViewer.this.videoPlayer.getDuration());
                    ArticleViewer.this.videoPlayerControlFrameLayout.invalidate();
                    ArticleViewer.this.updateVideoPlayerTime();
                }
                if (ArticleViewer.this.isPlaying) {
                    AndroidUtilities.runOnUIThread(ArticleViewer.this.updateProgressRunnable, 100L);
                }
            }
        };
        this.animationValues = new float[2][10];
        this.leftImage = new ImageReceiver();
        this.centerImage = new ImageReceiver();
        this.rightImage = new ImageReceiver();
        this.currentFileNames = new String[3];
        this.scale = 1.0f;
        this.interpolator = new DecelerateInterpolator(1.5f);
        this.pinchStartScale = 1.0f;
        this.canZoom = true;
        this.canDragDown = true;
        this.imagesArr = new ArrayList<TLRPC.PageBlock>();
    }
    
    private boolean addPageToStack(final TLRPC.WebPage webPage, final String s, final int n) {
        this.saveCurrentPagePosition();
        this.currentPage = webPage;
        this.pagesStack.add(webPage);
        this.updateInterfaceForCurrentPage(n);
        return this.scrollToAnchor(s);
    }
    
    private void animateTo(final float n, final float n2, final float n3, final boolean b) {
        this.animateTo(n, n2, n3, b, 250);
    }
    
    private void animateTo(final float animateToScale, final float animateToX, final float animateToY, final boolean zoomAnimation, final int n) {
        if (this.scale == animateToScale && this.translationX == animateToX && this.translationY == animateToY) {
            return;
        }
        this.zoomAnimation = zoomAnimation;
        this.animateToScale = animateToScale;
        this.animateToX = animateToX;
        this.animateToY = animateToY;
        this.animationStartTime = System.currentTimeMillis();
        (this.imageMoveAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "animationValue", new float[] { 0.0f, 1.0f }) });
        this.imageMoveAnimation.setInterpolator((TimeInterpolator)this.interpolator);
        this.imageMoveAnimation.setDuration((long)n);
        this.imageMoveAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                ArticleViewer.this.imageMoveAnimation = null;
                ArticleViewer.this.photoContainerView.invalidate();
            }
        });
        this.imageMoveAnimation.start();
    }
    
    private boolean checkAnimation() {
        final int animationInProgress = this.animationInProgress;
        boolean b = false;
        if (animationInProgress != 0 && Math.abs(this.transitionAnimationStartTime - System.currentTimeMillis()) >= 500L) {
            final Runnable animationEndRunnable = this.animationEndRunnable;
            if (animationEndRunnable != null) {
                animationEndRunnable.run();
                this.animationEndRunnable = null;
            }
            this.animationInProgress = 0;
        }
        if (this.animationInProgress != 0) {
            b = true;
        }
        return b;
    }
    
    private boolean checkLayoutForLinks(final MotionEvent motionEvent, final View pressedLinkOwnerView, final DrawingText pressedLinkOwnerLayout, int n, int pressedLayoutY) {
        if (this.pageSwitchAnimation != null || pressedLinkOwnerView == null || pressedLinkOwnerLayout == null) {
            return false;
        }
        final StaticLayout textLayout = pressedLinkOwnerLayout.textLayout;
        final int n2 = (int)motionEvent.getX();
        final int n3 = (int)motionEvent.getY();
        final int action = motionEvent.getAction();
        final boolean b = true;
        boolean b2 = true;
        Label_0787: {
            Label_0784: {
                if (action == 0) {
                    final int lineCount = textLayout.getLineCount();
                    int i = 0;
                    float min = 2.14748365E9f;
                    float max = 0.0f;
                    while (i < lineCount) {
                        max = Math.max(textLayout.getLineWidth(i), max);
                        min = Math.min(textLayout.getLineLeft(i), min);
                        ++i;
                    }
                    final float n4 = (float)n2;
                    final float n5 = n + min;
                    if (n4 < n5 || n4 > n5 + max || n3 < pressedLayoutY || n3 > textLayout.getHeight() + pressedLayoutY) {
                        break Label_0784;
                    }
                    this.pressedLinkOwnerLayout = pressedLinkOwnerLayout;
                    this.pressedLinkOwnerView = pressedLinkOwnerView;
                    this.pressedLayoutY = pressedLayoutY;
                    if (!(textLayout.getText() instanceof Spannable)) {
                        break Label_0784;
                    }
                    try {
                        pressedLayoutY = textLayout.getLineForVertical(n3 - pressedLayoutY);
                        final float n6 = (float)(n2 - n);
                        n = textLayout.getOffsetForHorizontal(pressedLayoutY, n6);
                        final float lineLeft = textLayout.getLineLeft(pressedLayoutY);
                        if (lineLeft > n6 || lineLeft + textLayout.getLineWidth(pressedLayoutY) < n6) {
                            break Label_0784;
                        }
                        final Spannable spannable = (Spannable)textLayout.getText();
                        final TextPaintUrlSpan[] array = (TextPaintUrlSpan[])spannable.getSpans(n, n, (Class)TextPaintUrlSpan.class);
                        if (array != null && array.length > 0) {
                            this.pressedLink = array[0];
                            pressedLayoutY = spannable.getSpanStart((Object)this.pressedLink);
                            n = spannable.getSpanEnd((Object)this.pressedLink);
                            int n7;
                            for (int j = 1; j < array.length; ++j, n = n7) {
                                final TextPaintUrlSpan pressedLink = array[j];
                                final int spanStart = spannable.getSpanStart((Object)pressedLink);
                                final int spanEnd = spannable.getSpanEnd((Object)pressedLink);
                                if (pressedLayoutY > spanStart || spanEnd > (n7 = n)) {
                                    this.pressedLink = pressedLink;
                                    pressedLayoutY = spanStart;
                                    n7 = spanEnd;
                                }
                            }
                            try {
                                this.urlPath.setUseRoundRect(true);
                                this.urlPath.setCurrentLayout(textLayout, pressedLayoutY, 0.0f);
                                int baselineShift;
                                if (this.pressedLink.getTextPaint() != null) {
                                    baselineShift = this.pressedLink.getTextPaint().baselineShift;
                                }
                                else {
                                    baselineShift = 0;
                                }
                                final LinkPath urlPath = this.urlPath;
                                int baselineShift2;
                                if (baselineShift != 0) {
                                    float n8;
                                    if (baselineShift > 0) {
                                        n8 = 5.0f;
                                    }
                                    else {
                                        n8 = -2.0f;
                                    }
                                    baselineShift2 = baselineShift + AndroidUtilities.dp(n8);
                                }
                                else {
                                    baselineShift2 = 0;
                                }
                                urlPath.setBaselineShift(baselineShift2);
                                textLayout.getSelectionPath(pressedLayoutY, n, (Path)this.urlPath);
                                pressedLinkOwnerView.invalidate();
                            }
                            catch (Exception ex) {
                                FileLog.e(ex);
                            }
                        }
                        break Label_0784;
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                        break Label_0784;
                    }
                }
                if (motionEvent.getAction() == 1) {
                    final TextPaintUrlSpan pressedLink2 = this.pressedLink;
                    if (pressedLink2 == null) {
                        break Label_0784;
                    }
                    final String url = pressedLink2.getUrl();
                    if (url != null) {
                        final BottomSheet linkSheet = this.linkSheet;
                        if (linkSheet != null) {
                            linkSheet.dismiss();
                            this.linkSheet = null;
                        }
                        n = url.lastIndexOf(35);
                        String decode = null;
                        Label_0734: {
                            String s2;
                            if (n != -1) {
                                String s;
                                if (!TextUtils.isEmpty((CharSequence)this.currentPage.cached_page.url)) {
                                    s = this.currentPage.cached_page.url.toLowerCase();
                                }
                                else {
                                    s = this.currentPage.url.toLowerCase();
                                }
                                try {
                                    decode = URLDecoder.decode(url.substring(n + 1), "UTF-8");
                                }
                                catch (Exception ex3) {
                                    decode = "";
                                }
                                s2 = decode;
                                if (url.toLowerCase().contains(s)) {
                                    if (TextUtils.isEmpty((CharSequence)decode)) {
                                        this.layoutManager[0].scrollToPositionWithOffset(0, 0);
                                        this.checkScrollAnimated();
                                    }
                                    else {
                                        this.scrollToAnchor(decode);
                                    }
                                    n = 1;
                                    break Label_0734;
                                }
                            }
                            else {
                                s2 = null;
                            }
                            n = 0;
                            decode = s2;
                        }
                        if (n == 0) {
                            this.openWebpageUrl(this.pressedLink.getUrl(), decode);
                        }
                    }
                }
                else {
                    if (motionEvent.getAction() != 3) {
                        break Label_0784;
                    }
                    final ActionBarPopupWindow popupWindow = this.popupWindow;
                    if (popupWindow != null && popupWindow.isShowing()) {
                        break Label_0784;
                    }
                }
                n = 1;
                break Label_0787;
            }
            n = 0;
        }
        if (n != 0) {
            this.removePressedLink();
        }
        if (motionEvent.getAction() == 0) {
            this.startCheckLongPress();
        }
        if (motionEvent.getAction() != 0 && motionEvent.getAction() != 2) {
            this.cancelCheckLongPress();
        }
        if (pressedLinkOwnerView instanceof BlockDetailsCell) {
            if (this.pressedLink == null) {
                b2 = false;
            }
            return b2;
        }
        return this.pressedLinkOwnerLayout != null && b;
    }
    
    private void checkMinMax(final boolean b) {
        float translationX = this.translationX;
        float translationY = this.translationY;
        this.updateMinMax(this.scale);
        final float translationX2 = this.translationX;
        final float minX = this.minX;
        if (translationX2 < minX) {
            translationX = minX;
        }
        else {
            final float maxX = this.maxX;
            if (translationX2 > maxX) {
                translationX = maxX;
            }
        }
        final float translationY2 = this.translationY;
        final float minY = this.minY;
        if (translationY2 < minY) {
            translationY = minY;
        }
        else {
            final float maxY = this.maxY;
            if (translationY2 > maxY) {
                translationY = maxY;
            }
        }
        this.animateTo(this.scale, translationX, translationY, b);
    }
    
    private boolean checkPhotoAnimation() {
        final int photoAnimationInProgress = this.photoAnimationInProgress;
        boolean b = false;
        if (photoAnimationInProgress != 0 && Math.abs(this.photoTransitionAnimationStartTime - System.currentTimeMillis()) >= 500L) {
            final Runnable photoAnimationEndRunnable = this.photoAnimationEndRunnable;
            if (photoAnimationEndRunnable != null) {
                photoAnimationEndRunnable.run();
                this.photoAnimationEndRunnable = null;
            }
            this.photoAnimationInProgress = 0;
        }
        if (this.photoAnimationInProgress != 0) {
            b = true;
        }
        return b;
    }
    
    private void checkProgress(final int n, final boolean b) {
        if (this.currentFileNames[n] != null) {
            final int currentIndex = this.currentIndex;
            final boolean b2 = true;
            int n2;
            if (n == 1) {
                n2 = currentIndex + 1;
            }
            else {
                n2 = currentIndex;
                if (n == 2) {
                    n2 = currentIndex - 1;
                }
            }
            final File mediaFile = this.getMediaFile(n2);
            final boolean mediaVideo = this.isMediaVideo(n2);
            if (mediaFile != null && mediaFile.exists()) {
                if (mediaVideo) {
                    this.radialProgressViews[n].setBackgroundState(3, b);
                }
                else {
                    this.radialProgressViews[n].setBackgroundState(-1, b);
                }
            }
            else {
                if (mediaVideo) {
                    if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[n])) {
                        this.radialProgressViews[n].setBackgroundState(2, false);
                    }
                    else {
                        this.radialProgressViews[n].setBackgroundState(1, false);
                    }
                }
                else {
                    this.radialProgressViews[n].setBackgroundState(0, b);
                }
                Float n3;
                if ((n3 = ImageLoader.getInstance().getFileProgress(this.currentFileNames[n])) == null) {
                    n3 = 0.0f;
                }
                this.radialProgressViews[n].setProgress(n3, false);
            }
            if (n == 0) {
                this.canZoom = (this.currentFileNames[0] != null && !mediaVideo && this.radialProgressViews[0].backgroundState != 0 && b2);
            }
        }
        else {
            this.radialProgressViews[n].setBackgroundState(-1, b);
        }
    }
    
    private void checkScroll(final int n) {
        this.setCurrentHeaderHeight(this.currentHeaderHeight - n);
    }
    
    private void checkScrollAnimated() {
        if (this.currentHeaderHeight == AndroidUtilities.dp(56.0f)) {
            return;
        }
        final ValueAnimator setDuration = ValueAnimator.ofObject((TypeEvaluator)new IntEvaluator(), new Object[] { this.currentHeaderHeight, AndroidUtilities.dp(56.0f) }).setDuration(180L);
        setDuration.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
        setDuration.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new _$$Lambda$ArticleViewer$sxYzbPn_gQQmb_6tSvBl0G3LOAw(this));
        setDuration.start();
    }
    
    private DrawingText createLayoutForText(final View p0, final CharSequence p1, final TLRPC.RichText p2, final int p3, final int p4, final TLRPC.PageBlock p5, final Layout$Alignment p6, final int p7, final WebpageAdapter p8) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          10
        //     3: aload_2        
        //     4: ifnonnull       20
        //     7: aload_3        
        //     8: ifnull          18
        //    11: aload_3        
        //    12: instanceof      Lorg/telegram/tgnet/TLRPC$TL_textEmpty;
        //    15: ifeq            20
        //    18: aconst_null    
        //    19: areturn        
        //    20: iload           4
        //    22: ifge            36
        //    25: ldc_w           10.0
        //    28: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //    31: istore          11
        //    33: goto            40
        //    36: iload           4
        //    38: istore          11
        //    40: aload_0        
        //    41: invokespecial   org/telegram/ui/ArticleViewer.getSelectedColor:()I
        //    44: istore          12
        //    46: aload_2        
        //    47: ifnull          56
        //    50: aload_2        
        //    51: astore          13
        //    53: goto            69
        //    56: aload_0        
        //    57: aload_1        
        //    58: aload_3        
        //    59: aload_3        
        //    60: aload           6
        //    62: iload           11
        //    64: invokespecial   org/telegram/ui/ArticleViewer.getText:(Landroid/view/View;Lorg/telegram/tgnet/TLRPC$RichText;Lorg/telegram/tgnet/TLRPC$RichText;Lorg/telegram/tgnet/TLRPC$PageBlock;I)Ljava/lang/CharSequence;
        //    67: astore          13
        //    69: aload           13
        //    71: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //    74: ifeq            79
        //    77: aconst_null    
        //    78: areturn        
        //    79: aload_0        
        //    80: getfield        org/telegram/ui/ArticleViewer.selectedFontSize:I
        //    83: istore          4
        //    85: iload           4
        //    87: ifne            106
        //    90: ldc_w           4.0
        //    93: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //    96: istore          4
        //    98: iload           4
        //   100: ineg           
        //   101: istore          4
        //   103: goto            156
        //   106: iload           4
        //   108: iconst_1       
        //   109: if_icmpne       121
        //   112: fconst_2       
        //   113: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   116: istore          4
        //   118: goto            98
        //   121: iload           4
        //   123: iconst_3       
        //   124: if_icmpne       136
        //   127: fconst_2       
        //   128: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   131: istore          4
        //   133: goto            156
        //   136: iload           4
        //   138: iconst_4       
        //   139: if_icmpne       153
        //   142: ldc_w           4.0
        //   145: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   148: istore          4
        //   150: goto            156
        //   153: iconst_0       
        //   154: istore          4
        //   156: aload           6
        //   158: instanceof      Lorg/telegram/tgnet/TLRPC$TL_pageBlockEmbedPost;
        //   161: ifeq            297
        //   164: aload_3        
        //   165: ifnonnull       297
        //   168: aload           6
        //   170: checkcast       Lorg/telegram/tgnet/TLRPC$TL_pageBlockEmbedPost;
        //   173: getfield        org/telegram/tgnet/TLRPC$TL_pageBlockEmbedPost.author:Ljava/lang/String;
        //   176: aload_2        
        //   177: if_acmpne       230
        //   180: getstatic       org/telegram/ui/ArticleViewer.embedPostAuthorPaint:Landroid/text/TextPaint;
        //   183: ifnonnull       207
        //   186: new             Landroid/text/TextPaint;
        //   189: dup            
        //   190: iconst_1       
        //   191: invokespecial   android/text/TextPaint.<init>:(I)V
        //   194: putstatic       org/telegram/ui/ArticleViewer.embedPostAuthorPaint:Landroid/text/TextPaint;
        //   197: getstatic       org/telegram/ui/ArticleViewer.embedPostAuthorPaint:Landroid/text/TextPaint;
        //   200: aload_0        
        //   201: invokespecial   org/telegram/ui/ArticleViewer.getTextColor:()I
        //   204: invokevirtual   android/text/TextPaint.setColor:(I)V
        //   207: getstatic       org/telegram/ui/ArticleViewer.embedPostAuthorPaint:Landroid/text/TextPaint;
        //   210: ldc_w           15.0
        //   213: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   216: iload           4
        //   218: iadd           
        //   219: i2f            
        //   220: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //   223: getstatic       org/telegram/ui/ArticleViewer.embedPostAuthorPaint:Landroid/text/TextPaint;
        //   226: astore_1       
        //   227: goto            680
        //   230: getstatic       org/telegram/ui/ArticleViewer.embedPostDatePaint:Landroid/text/TextPaint;
        //   233: ifnonnull       274
        //   236: new             Landroid/text/TextPaint;
        //   239: dup            
        //   240: iconst_1       
        //   241: invokespecial   android/text/TextPaint.<init>:(I)V
        //   244: putstatic       org/telegram/ui/ArticleViewer.embedPostDatePaint:Landroid/text/TextPaint;
        //   247: iload           12
        //   249: ifne            264
        //   252: getstatic       org/telegram/ui/ArticleViewer.embedPostDatePaint:Landroid/text/TextPaint;
        //   255: ldc_w           -7366752
        //   258: invokevirtual   android/text/TextPaint.setColor:(I)V
        //   261: goto            274
        //   264: getstatic       org/telegram/ui/ArticleViewer.embedPostDatePaint:Landroid/text/TextPaint;
        //   267: aload_0        
        //   268: invokespecial   org/telegram/ui/ArticleViewer.getGrayTextColor:()I
        //   271: invokevirtual   android/text/TextPaint.setColor:(I)V
        //   274: getstatic       org/telegram/ui/ArticleViewer.embedPostDatePaint:Landroid/text/TextPaint;
        //   277: ldc_w           14.0
        //   280: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   283: iload           4
        //   285: iadd           
        //   286: i2f            
        //   287: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //   290: getstatic       org/telegram/ui/ArticleViewer.embedPostDatePaint:Landroid/text/TextPaint;
        //   293: astore_1       
        //   294: goto            680
        //   297: aload           6
        //   299: instanceof      Lorg/telegram/tgnet/TLRPC$TL_pageBlockChannel;
        //   302: ifeq            382
        //   305: getstatic       org/telegram/ui/ArticleViewer.channelNamePaint:Landroid/text/TextPaint;
        //   308: ifnonnull       335
        //   311: new             Landroid/text/TextPaint;
        //   314: dup            
        //   315: iconst_1       
        //   316: invokespecial   android/text/TextPaint.<init>:(I)V
        //   319: putstatic       org/telegram/ui/ArticleViewer.channelNamePaint:Landroid/text/TextPaint;
        //   322: getstatic       org/telegram/ui/ArticleViewer.channelNamePaint:Landroid/text/TextPaint;
        //   325: ldc_w           "fonts/rmedium.ttf"
        //   328: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   331: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   334: pop            
        //   335: aload_0        
        //   336: getfield        org/telegram/ui/ArticleViewer.channelBlock:Lorg/telegram/tgnet/TLRPC$TL_pageBlockChannel;
        //   339: ifnonnull       355
        //   342: getstatic       org/telegram/ui/ArticleViewer.channelNamePaint:Landroid/text/TextPaint;
        //   345: aload_0        
        //   346: invokespecial   org/telegram/ui/ArticleViewer.getTextColor:()I
        //   349: invokevirtual   android/text/TextPaint.setColor:(I)V
        //   352: goto            362
        //   355: getstatic       org/telegram/ui/ArticleViewer.channelNamePaint:Landroid/text/TextPaint;
        //   358: iconst_m1      
        //   359: invokevirtual   android/text/TextPaint.setColor:(I)V
        //   362: getstatic       org/telegram/ui/ArticleViewer.channelNamePaint:Landroid/text/TextPaint;
        //   365: ldc_w           15.0
        //   368: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   371: i2f            
        //   372: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //   375: getstatic       org/telegram/ui/ArticleViewer.channelNamePaint:Landroid/text/TextPaint;
        //   378: astore_1       
        //   379: goto            680
        //   382: aload           6
        //   384: instanceof      Lorg/telegram/ui/ArticleViewer$TL_pageBlockRelatedArticlesChild;
        //   387: ifeq            533
        //   390: aload           6
        //   392: checkcast       Lorg/telegram/ui/ArticleViewer$TL_pageBlockRelatedArticlesChild;
        //   395: astore_1       
        //   396: aload_2        
        //   397: aload_1        
        //   398: invokestatic    org/telegram/ui/ArticleViewer$TL_pageBlockRelatedArticlesChild.access$7400:(Lorg/telegram/ui/ArticleViewer$TL_pageBlockRelatedArticlesChild;)Lorg/telegram/tgnet/TLRPC$TL_pageBlockRelatedArticles;
        //   401: getfield        org/telegram/tgnet/TLRPC$TL_pageBlockRelatedArticles.articles:Ljava/util/ArrayList;
        //   404: aload_1        
        //   405: invokestatic    org/telegram/ui/ArticleViewer$TL_pageBlockRelatedArticlesChild.access$7300:(Lorg/telegram/ui/ArticleViewer$TL_pageBlockRelatedArticlesChild;)I
        //   408: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //   411: checkcast       Lorg/telegram/tgnet/TLRPC$TL_pageRelatedArticle;
        //   414: getfield        org/telegram/tgnet/TLRPC$TL_pageRelatedArticle.title:Ljava/lang/String;
        //   417: if_acmpne       483
        //   420: getstatic       org/telegram/ui/ArticleViewer.relatedArticleHeaderPaint:Landroid/text/TextPaint;
        //   423: ifnonnull       450
        //   426: new             Landroid/text/TextPaint;
        //   429: dup            
        //   430: iconst_1       
        //   431: invokespecial   android/text/TextPaint.<init>:(I)V
        //   434: putstatic       org/telegram/ui/ArticleViewer.relatedArticleHeaderPaint:Landroid/text/TextPaint;
        //   437: getstatic       org/telegram/ui/ArticleViewer.relatedArticleHeaderPaint:Landroid/text/TextPaint;
        //   440: ldc_w           "fonts/rmedium.ttf"
        //   443: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   446: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   449: pop            
        //   450: getstatic       org/telegram/ui/ArticleViewer.relatedArticleHeaderPaint:Landroid/text/TextPaint;
        //   453: aload_0        
        //   454: invokespecial   org/telegram/ui/ArticleViewer.getTextColor:()I
        //   457: invokevirtual   android/text/TextPaint.setColor:(I)V
        //   460: getstatic       org/telegram/ui/ArticleViewer.relatedArticleHeaderPaint:Landroid/text/TextPaint;
        //   463: ldc_w           15.0
        //   466: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   469: iload           4
        //   471: iadd           
        //   472: i2f            
        //   473: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //   476: getstatic       org/telegram/ui/ArticleViewer.relatedArticleHeaderPaint:Landroid/text/TextPaint;
        //   479: astore_1       
        //   480: goto            680
        //   483: getstatic       org/telegram/ui/ArticleViewer.relatedArticleTextPaint:Landroid/text/TextPaint;
        //   486: ifnonnull       500
        //   489: new             Landroid/text/TextPaint;
        //   492: dup            
        //   493: iconst_1       
        //   494: invokespecial   android/text/TextPaint.<init>:(I)V
        //   497: putstatic       org/telegram/ui/ArticleViewer.relatedArticleTextPaint:Landroid/text/TextPaint;
        //   500: getstatic       org/telegram/ui/ArticleViewer.relatedArticleTextPaint:Landroid/text/TextPaint;
        //   503: aload_0        
        //   504: invokespecial   org/telegram/ui/ArticleViewer.getGrayTextColor:()I
        //   507: invokevirtual   android/text/TextPaint.setColor:(I)V
        //   510: getstatic       org/telegram/ui/ArticleViewer.relatedArticleTextPaint:Landroid/text/TextPaint;
        //   513: ldc_w           14.0
        //   516: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   519: iload           4
        //   521: iadd           
        //   522: i2f            
        //   523: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //   526: getstatic       org/telegram/ui/ArticleViewer.relatedArticleTextPaint:Landroid/text/TextPaint;
        //   529: astore_1       
        //   530: goto            680
        //   533: aload_0        
        //   534: aload           6
        //   536: invokespecial   org/telegram/ui/ArticleViewer.isListItemBlock:(Lorg/telegram/tgnet/TLRPC$PageBlock;)Z
        //   539: ifeq            671
        //   542: aload_2        
        //   543: ifnull          671
        //   546: getstatic       org/telegram/ui/ArticleViewer.listTextPointerPaint:Landroid/text/TextPaint;
        //   549: ifnonnull       573
        //   552: new             Landroid/text/TextPaint;
        //   555: dup            
        //   556: iconst_1       
        //   557: invokespecial   android/text/TextPaint.<init>:(I)V
        //   560: putstatic       org/telegram/ui/ArticleViewer.listTextPointerPaint:Landroid/text/TextPaint;
        //   563: getstatic       org/telegram/ui/ArticleViewer.listTextPointerPaint:Landroid/text/TextPaint;
        //   566: aload_0        
        //   567: invokespecial   org/telegram/ui/ArticleViewer.getTextColor:()I
        //   570: invokevirtual   android/text/TextPaint.setColor:(I)V
        //   573: getstatic       org/telegram/ui/ArticleViewer.listTextNumPaint:Landroid/text/TextPaint;
        //   576: ifnonnull       600
        //   579: new             Landroid/text/TextPaint;
        //   582: dup            
        //   583: iconst_1       
        //   584: invokespecial   android/text/TextPaint.<init>:(I)V
        //   587: putstatic       org/telegram/ui/ArticleViewer.listTextNumPaint:Landroid/text/TextPaint;
        //   590: getstatic       org/telegram/ui/ArticleViewer.listTextNumPaint:Landroid/text/TextPaint;
        //   593: aload_0        
        //   594: invokespecial   org/telegram/ui/ArticleViewer.getTextColor:()I
        //   597: invokevirtual   android/text/TextPaint.setColor:(I)V
        //   600: getstatic       org/telegram/ui/ArticleViewer.listTextPointerPaint:Landroid/text/TextPaint;
        //   603: ldc_w           19.0
        //   606: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   609: iload           4
        //   611: iadd           
        //   612: i2f            
        //   613: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //   616: getstatic       org/telegram/ui/ArticleViewer.listTextNumPaint:Landroid/text/TextPaint;
        //   619: ldc_w           16.0
        //   622: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   625: iload           4
        //   627: iadd           
        //   628: i2f            
        //   629: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //   632: aload           6
        //   634: instanceof      Lorg/telegram/ui/ArticleViewer$TL_pageBlockListItem;
        //   637: ifeq            664
        //   640: aload           6
        //   642: checkcast       Lorg/telegram/ui/ArticleViewer$TL_pageBlockListItem;
        //   645: invokestatic    org/telegram/ui/ArticleViewer$TL_pageBlockListItem.access$5800:(Lorg/telegram/ui/ArticleViewer$TL_pageBlockListItem;)Lorg/telegram/ui/ArticleViewer$TL_pageBlockListParent;
        //   648: invokestatic    org/telegram/ui/ArticleViewer$TL_pageBlockListParent.access$7500:(Lorg/telegram/ui/ArticleViewer$TL_pageBlockListParent;)Lorg/telegram/tgnet/TLRPC$TL_pageBlockList;
        //   651: getfield        org/telegram/tgnet/TLRPC$TL_pageBlockList.ordered:Z
        //   654: ifne            664
        //   657: getstatic       org/telegram/ui/ArticleViewer.listTextPointerPaint:Landroid/text/TextPaint;
        //   660: astore_1       
        //   661: goto            680
        //   664: getstatic       org/telegram/ui/ArticleViewer.listTextNumPaint:Landroid/text/TextPaint;
        //   667: astore_1       
        //   668: goto            680
        //   671: aload_0        
        //   672: aload_3        
        //   673: aload_3        
        //   674: aload           6
        //   676: invokespecial   org/telegram/ui/ArticleViewer.getTextPaint:(Lorg/telegram/tgnet/TLRPC$RichText;Lorg/telegram/tgnet/TLRPC$RichText;Lorg/telegram/tgnet/TLRPC$PageBlock;)Landroid/text/TextPaint;
        //   679: astore_1       
        //   680: iload           8
        //   682: ifeq            748
        //   685: aload           6
        //   687: instanceof      Lorg/telegram/tgnet/TLRPC$TL_pageBlockPullquote;
        //   690: ifeq            718
        //   693: aload           13
        //   695: aload_1        
        //   696: iload           11
        //   698: getstatic       android/text/Layout$Alignment.ALIGN_CENTER:Landroid/text/Layout$Alignment;
        //   701: fconst_1       
        //   702: fconst_0       
        //   703: iconst_0       
        //   704: getstatic       android/text/TextUtils$TruncateAt.END:Landroid/text/TextUtils$TruncateAt;
        //   707: iload           11
        //   709: iload           8
        //   711: invokestatic    org/telegram/ui/Components/StaticLayoutEx.createStaticLayout:(Ljava/lang/CharSequence;Landroid/text/TextPaint;ILandroid/text/Layout$Alignment;FFZLandroid/text/TextUtils$TruncateAt;II)Landroid/text/StaticLayout;
        //   714: astore_1       
        //   715: goto            842
        //   718: aload           13
        //   720: aload_1        
        //   721: iload           11
        //   723: aload           7
        //   725: fconst_1       
        //   726: ldc_w           4.0
        //   729: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   732: i2f            
        //   733: iconst_0       
        //   734: getstatic       android/text/TextUtils$TruncateAt.END:Landroid/text/TextUtils$TruncateAt;
        //   737: iload           11
        //   739: iload           8
        //   741: invokestatic    org/telegram/ui/Components/StaticLayoutEx.createStaticLayout:(Ljava/lang/CharSequence;Landroid/text/TextPaint;ILandroid/text/Layout$Alignment;FFZLandroid/text/TextUtils$TruncateAt;II)Landroid/text/StaticLayout;
        //   744: astore_1       
        //   745: goto            842
        //   748: aload           13
        //   750: astore_2       
        //   751: aload           13
        //   753: aload           13
        //   755: invokeinterface java/lang/CharSequence.length:()I
        //   760: iconst_1       
        //   761: isub           
        //   762: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   767: bipush          10
        //   769: if_icmpne       790
        //   772: aload           13
        //   774: iconst_0       
        //   775: aload           13
        //   777: invokeinterface java/lang/CharSequence.length:()I
        //   782: iconst_1       
        //   783: isub           
        //   784: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   789: astore_2       
        //   790: aload           6
        //   792: instanceof      Lorg/telegram/tgnet/TLRPC$TL_pageBlockPullquote;
        //   795: ifeq            819
        //   798: new             Landroid/text/StaticLayout;
        //   801: dup            
        //   802: aload_2        
        //   803: aload_1        
        //   804: iload           11
        //   806: getstatic       android/text/Layout$Alignment.ALIGN_CENTER:Landroid/text/Layout$Alignment;
        //   809: fconst_1       
        //   810: fconst_0       
        //   811: iconst_0       
        //   812: invokespecial   android/text/StaticLayout.<init>:(Ljava/lang/CharSequence;Landroid/text/TextPaint;ILandroid/text/Layout$Alignment;FFZ)V
        //   815: astore_1       
        //   816: goto            842
        //   819: new             Landroid/text/StaticLayout;
        //   822: dup            
        //   823: aload_2        
        //   824: aload_1        
        //   825: iload           11
        //   827: aload           7
        //   829: fconst_1       
        //   830: ldc_w           4.0
        //   833: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   836: i2f            
        //   837: iconst_0       
        //   838: invokespecial   android/text/StaticLayout.<init>:(Ljava/lang/CharSequence;Landroid/text/TextPaint;ILandroid/text/Layout$Alignment;FFZ)V
        //   841: astore_1       
        //   842: aload_1        
        //   843: ifnonnull       848
        //   846: aconst_null    
        //   847: areturn        
        //   848: aload_1        
        //   849: invokevirtual   android/text/StaticLayout.getText:()Ljava/lang/CharSequence;
        //   852: astore_2       
        //   853: aload_1        
        //   854: ifnull          1389
        //   857: aload_2        
        //   858: instanceof      Landroid/text/Spanned;
        //   861: ifeq            1389
        //   864: aload_2        
        //   865: checkcast       Landroid/text/Spanned;
        //   868: astore          7
        //   870: aload           7
        //   872: iconst_0       
        //   873: aload           7
        //   875: invokeinterface android/text/Spanned.length:()I
        //   880: ldc_w           Lorg/telegram/ui/Components/AnchorSpan;.class
        //   883: invokeinterface android/text/Spanned.getSpans:(IILjava/lang/Class;)[Ljava/lang/Object;
        //   888: checkcast       [Lorg/telegram/ui/Components/AnchorSpan;
        //   891: astore_2       
        //   892: aload_1        
        //   893: invokevirtual   android/text/StaticLayout.getLineCount:()I
        //   896: istore          8
        //   898: aload_2        
        //   899: ifnull          994
        //   902: aload_2        
        //   903: arraylength    
        //   904: ifle            994
        //   907: iconst_0       
        //   908: istore          4
        //   910: iload           4
        //   912: aload_2        
        //   913: arraylength    
        //   914: if_icmpge       994
        //   917: iload           8
        //   919: iconst_1       
        //   920: if_icmpgt       947
        //   923: aload           9
        //   925: invokestatic    org/telegram/ui/ArticleViewer$WebpageAdapter.access$2200:(Lorg/telegram/ui/ArticleViewer$WebpageAdapter;)Ljava/util/HashMap;
        //   928: aload_2        
        //   929: iload           4
        //   931: aaload         
        //   932: invokevirtual   org/telegram/ui/Components/AnchorSpan.getName:()Ljava/lang/String;
        //   935: iload           5
        //   937: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   940: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   943: pop            
        //   944: goto            988
        //   947: aload           9
        //   949: invokestatic    org/telegram/ui/ArticleViewer$WebpageAdapter.access$2200:(Lorg/telegram/ui/ArticleViewer$WebpageAdapter;)Ljava/util/HashMap;
        //   952: aload_2        
        //   953: iload           4
        //   955: aaload         
        //   956: invokevirtual   org/telegram/ui/Components/AnchorSpan.getName:()Ljava/lang/String;
        //   959: iload           5
        //   961: aload_1        
        //   962: aload_1        
        //   963: aload           7
        //   965: aload_2        
        //   966: iload           4
        //   968: aaload         
        //   969: invokeinterface android/text/Spanned.getSpanStart:(Ljava/lang/Object;)I
        //   974: invokevirtual   android/text/StaticLayout.getLineForOffset:(I)I
        //   977: invokevirtual   android/text/StaticLayout.getLineTop:(I)I
        //   980: iadd           
        //   981: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   984: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   987: pop            
        //   988: iinc            4, 1
        //   991: goto            910
        //   994: aload           7
        //   996: iconst_0       
        //   997: aload           7
        //   999: invokeinterface android/text/Spanned.length:()I
        //  1004: ldc_w           Lorg/telegram/ui/Components/TextPaintWebpageUrlSpan;.class
        //  1007: invokeinterface android/text/Spanned.getSpans:(IILjava/lang/Class;)[Ljava/lang/Object;
        //  1012: checkcast       [Lorg/telegram/ui/Components/TextPaintWebpageUrlSpan;
        //  1015: astore_3       
        //  1016: aload_3        
        //  1017: ifnull          1179
        //  1020: aload_3        
        //  1021: arraylength    
        //  1022: ifle            1179
        //  1025: new             Lorg/telegram/ui/Components/LinkPath;
        //  1028: astore_2       
        //  1029: aload_2        
        //  1030: iconst_1       
        //  1031: invokespecial   org/telegram/ui/Components/LinkPath.<init>:(Z)V
        //  1034: aload_2        
        //  1035: iconst_0       
        //  1036: invokevirtual   org/telegram/ui/Components/LinkPath.setAllowReset:(Z)V
        //  1039: iconst_0       
        //  1040: istore          4
        //  1042: iload           4
        //  1044: aload_3        
        //  1045: arraylength    
        //  1046: if_icmpge       1171
        //  1049: aload           7
        //  1051: aload_3        
        //  1052: iload           4
        //  1054: aaload         
        //  1055: invokeinterface android/text/Spanned.getSpanStart:(Ljava/lang/Object;)I
        //  1060: istore          11
        //  1062: aload           7
        //  1064: aload_3        
        //  1065: iload           4
        //  1067: aaload         
        //  1068: invokeinterface android/text/Spanned.getSpanEnd:(Ljava/lang/Object;)I
        //  1073: istore          8
        //  1075: aload_2        
        //  1076: aload_1        
        //  1077: iload           11
        //  1079: fconst_0       
        //  1080: invokevirtual   org/telegram/ui/Components/LinkPath.setCurrentLayout:(Landroid/text/StaticLayout;IF)V
        //  1083: aload_3        
        //  1084: iload           4
        //  1086: aaload         
        //  1087: invokevirtual   org/telegram/ui/Components/TextPaintUrlSpan.getTextPaint:()Landroid/text/TextPaint;
        //  1090: ifnull          1108
        //  1093: aload_3        
        //  1094: iload           4
        //  1096: aaload         
        //  1097: invokevirtual   org/telegram/ui/Components/TextPaintUrlSpan.getTextPaint:()Landroid/text/TextPaint;
        //  1100: getfield        android/text/TextPaint.baselineShift:I
        //  1103: istore          5
        //  1105: goto            1111
        //  1108: iconst_0       
        //  1109: istore          5
        //  1111: iload           5
        //  1113: ifeq            1147
        //  1116: iload           5
        //  1118: ifle            1129
        //  1121: ldc_w           5.0
        //  1124: fstore          14
        //  1126: goto            1134
        //  1129: ldc_w           -2.0
        //  1132: fstore          14
        //  1134: iload           5
        //  1136: fload           14
        //  1138: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1141: iadd           
        //  1142: istore          5
        //  1144: goto            1150
        //  1147: iconst_0       
        //  1148: istore          5
        //  1150: aload_2        
        //  1151: iload           5
        //  1153: invokevirtual   org/telegram/ui/Components/LinkPath.setBaselineShift:(I)V
        //  1156: aload_1        
        //  1157: iload           11
        //  1159: iload           8
        //  1161: aload_2        
        //  1162: invokevirtual   android/text/StaticLayout.getSelectionPath:(IILandroid/graphics/Path;)V
        //  1165: iinc            4, 1
        //  1168: goto            1042
        //  1171: aload_2        
        //  1172: iconst_1       
        //  1173: invokevirtual   org/telegram/ui/Components/LinkPath.setAllowReset:(Z)V
        //  1176: goto            1181
        //  1179: aconst_null    
        //  1180: astore_2       
        //  1181: aload           7
        //  1183: iconst_0       
        //  1184: aload           7
        //  1186: invokeinterface android/text/Spanned.length:()I
        //  1191: ldc_w           Lorg/telegram/ui/Components/TextPaintMarkSpan;.class
        //  1194: invokeinterface android/text/Spanned.getSpans:(IILjava/lang/Class;)[Ljava/lang/Object;
        //  1199: checkcast       [Lorg/telegram/ui/Components/TextPaintMarkSpan;
        //  1202: astore          9
        //  1204: aload_2        
        //  1205: astore          6
        //  1207: aload           10
        //  1209: astore_3       
        //  1210: aload           9
        //  1212: ifnull          1395
        //  1215: aload_2        
        //  1216: astore          6
        //  1218: aload           10
        //  1220: astore_3       
        //  1221: aload           9
        //  1223: arraylength    
        //  1224: ifle            1395
        //  1227: new             Lorg/telegram/ui/Components/LinkPath;
        //  1230: astore_3       
        //  1231: aload_3        
        //  1232: iconst_1       
        //  1233: invokespecial   org/telegram/ui/Components/LinkPath.<init>:(Z)V
        //  1236: aload_3        
        //  1237: iconst_0       
        //  1238: invokevirtual   org/telegram/ui/Components/LinkPath.setAllowReset:(Z)V
        //  1241: iconst_0       
        //  1242: istore          4
        //  1244: iload           4
        //  1246: aload           9
        //  1248: arraylength    
        //  1249: if_icmpge       1378
        //  1252: aload           7
        //  1254: aload           9
        //  1256: iload           4
        //  1258: aaload         
        //  1259: invokeinterface android/text/Spanned.getSpanStart:(Ljava/lang/Object;)I
        //  1264: istore          8
        //  1266: aload           7
        //  1268: aload           9
        //  1270: iload           4
        //  1272: aaload         
        //  1273: invokeinterface android/text/Spanned.getSpanEnd:(Ljava/lang/Object;)I
        //  1278: istore          11
        //  1280: aload_3        
        //  1281: aload_1        
        //  1282: iload           8
        //  1284: fconst_0       
        //  1285: invokevirtual   org/telegram/ui/Components/LinkPath.setCurrentLayout:(Landroid/text/StaticLayout;IF)V
        //  1288: aload           9
        //  1290: iload           4
        //  1292: aaload         
        //  1293: invokevirtual   org/telegram/ui/Components/TextPaintMarkSpan.getTextPaint:()Landroid/text/TextPaint;
        //  1296: ifnull          1315
        //  1299: aload           9
        //  1301: iload           4
        //  1303: aaload         
        //  1304: invokevirtual   org/telegram/ui/Components/TextPaintMarkSpan.getTextPaint:()Landroid/text/TextPaint;
        //  1307: getfield        android/text/TextPaint.baselineShift:I
        //  1310: istore          5
        //  1312: goto            1318
        //  1315: iconst_0       
        //  1316: istore          5
        //  1318: iload           5
        //  1320: ifeq            1354
        //  1323: iload           5
        //  1325: ifle            1336
        //  1328: ldc_w           5.0
        //  1331: fstore          14
        //  1333: goto            1341
        //  1336: ldc_w           -2.0
        //  1339: fstore          14
        //  1341: iload           5
        //  1343: fload           14
        //  1345: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1348: iadd           
        //  1349: istore          5
        //  1351: goto            1357
        //  1354: iconst_0       
        //  1355: istore          5
        //  1357: aload_3        
        //  1358: iload           5
        //  1360: invokevirtual   org/telegram/ui/Components/LinkPath.setBaselineShift:(I)V
        //  1363: aload_1        
        //  1364: iload           8
        //  1366: iload           11
        //  1368: aload_3        
        //  1369: invokevirtual   android/text/StaticLayout.getSelectionPath:(IILandroid/graphics/Path;)V
        //  1372: iinc            4, 1
        //  1375: goto            1244
        //  1378: aload_3        
        //  1379: iconst_1       
        //  1380: invokevirtual   org/telegram/ui/Components/LinkPath.setAllowReset:(Z)V
        //  1383: aload_2        
        //  1384: astore          6
        //  1386: goto            1395
        //  1389: aconst_null    
        //  1390: astore          6
        //  1392: aload           10
        //  1394: astore_3       
        //  1395: new             Lorg/telegram/ui/ArticleViewer$DrawingText;
        //  1398: dup            
        //  1399: aload_0        
        //  1400: invokespecial   org/telegram/ui/ArticleViewer$DrawingText.<init>:(Lorg/telegram/ui/ArticleViewer;)V
        //  1403: astore_2       
        //  1404: aload_2        
        //  1405: aload_1        
        //  1406: putfield        org/telegram/ui/ArticleViewer$DrawingText.textLayout:Landroid/text/StaticLayout;
        //  1409: aload_2        
        //  1410: aload           6
        //  1412: putfield        org/telegram/ui/ArticleViewer$DrawingText.textPath:Lorg/telegram/ui/Components/LinkPath;
        //  1415: aload_2        
        //  1416: aload_3        
        //  1417: putfield        org/telegram/ui/ArticleViewer$DrawingText.markPath:Lorg/telegram/ui/Components/LinkPath;
        //  1420: aload_2        
        //  1421: areturn        
        //  1422: astore_2       
        //  1423: goto            994
        //  1426: astore_2       
        //  1427: goto            1179
        //  1430: astore_3       
        //  1431: goto            1181
        //  1434: astore_3       
        //  1435: aload_2        
        //  1436: astore          6
        //  1438: aload           10
        //  1440: astore_3       
        //  1441: goto            1395
        //  1444: astore          6
        //  1446: goto            1383
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  870    898    1422   1426   Ljava/lang/Exception;
        //  902    907    1422   1426   Ljava/lang/Exception;
        //  910    917    1422   1426   Ljava/lang/Exception;
        //  923    944    1422   1426   Ljava/lang/Exception;
        //  947    988    1422   1426   Ljava/lang/Exception;
        //  994    1016   1426   1430   Ljava/lang/Exception;
        //  1020   1034   1426   1430   Ljava/lang/Exception;
        //  1034   1039   1430   1434   Ljava/lang/Exception;
        //  1042   1105   1430   1434   Ljava/lang/Exception;
        //  1134   1144   1430   1434   Ljava/lang/Exception;
        //  1150   1165   1430   1434   Ljava/lang/Exception;
        //  1171   1176   1430   1434   Ljava/lang/Exception;
        //  1181   1204   1434   1444   Ljava/lang/Exception;
        //  1221   1236   1434   1444   Ljava/lang/Exception;
        //  1236   1241   1444   1449   Ljava/lang/Exception;
        //  1244   1312   1444   1449   Ljava/lang/Exception;
        //  1341   1351   1444   1449   Ljava/lang/Exception;
        //  1357   1372   1444   1449   Ljava/lang/Exception;
        //  1378   1383   1444   1449   Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 669 out-of-bounds for length 669
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private DrawingText createLayoutForText(final View view, final CharSequence charSequence, final TLRPC.RichText richText, final int n, final int n2, final TLRPC.PageBlock pageBlock, final WebpageAdapter webpageAdapter) {
        return this.createLayoutForText(view, charSequence, richText, n, n2, pageBlock, Layout$Alignment.ALIGN_NORMAL, 0, webpageAdapter);
    }
    
    private DrawingText createLayoutForText(final View view, final CharSequence charSequence, final TLRPC.RichText richText, final int n, final TLRPC.PageBlock pageBlock, final Layout$Alignment layout$Alignment, final WebpageAdapter webpageAdapter) {
        return this.createLayoutForText(view, charSequence, richText, n, 0, pageBlock, layout$Alignment, 0, webpageAdapter);
    }
    
    private DrawingText createLayoutForText(final View view, final CharSequence charSequence, final TLRPC.RichText richText, final int n, final TLRPC.PageBlock pageBlock, final WebpageAdapter webpageAdapter) {
        return this.createLayoutForText(view, charSequence, richText, n, 0, pageBlock, Layout$Alignment.ALIGN_NORMAL, 0, webpageAdapter);
    }
    
    private void createPaint(final boolean b) {
        if (ArticleViewer.quoteLinePaint == null) {
            ArticleViewer.quoteLinePaint = new Paint();
            ArticleViewer.preformattedBackgroundPaint = new Paint();
            (ArticleViewer.tableLinePaint = new Paint(1)).setStyle(Paint$Style.STROKE);
            ArticleViewer.tableLinePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0f));
            (ArticleViewer.tableHalfLinePaint = new Paint()).setStyle(Paint$Style.STROKE);
            ArticleViewer.tableHalfLinePaint.setStrokeWidth(AndroidUtilities.dp(1.0f) / 2.0f);
            ArticleViewer.tableHeaderPaint = new Paint();
            ArticleViewer.tableStripPaint = new Paint();
            ArticleViewer.urlPaint = new Paint();
            ArticleViewer.webpageUrlPaint = new Paint(1);
            ArticleViewer.photoBackgroundPaint = new Paint();
            ArticleViewer.dividerPaint = new Paint();
            ArticleViewer.webpageMarkPaint = new Paint(1);
        }
        else if (!b) {
            return;
        }
        final int selectedColor = this.getSelectedColor();
        if (selectedColor == 0) {
            ArticleViewer.preformattedBackgroundPaint.setColor(-657156);
            ArticleViewer.webpageUrlPaint.setColor(-1313798);
            ArticleViewer.urlPaint.setColor(-2299145);
            ArticleViewer.tableHalfLinePaint.setColor(-2039584);
            ArticleViewer.tableLinePaint.setColor(-2039584);
            ArticleViewer.tableHeaderPaint.setColor(-723724);
            ArticleViewer.tableStripPaint.setColor(-526345);
            ArticleViewer.photoBackgroundPaint.setColor(-723724);
            ArticleViewer.dividerPaint.setColor(-3288619);
            ArticleViewer.webpageMarkPaint.setColor(-68676);
        }
        else if (selectedColor == 1) {
            ArticleViewer.preformattedBackgroundPaint.setColor(-1712440);
            ArticleViewer.webpageUrlPaint.setColor(-2365721);
            ArticleViewer.urlPaint.setColor(-3481882);
            ArticleViewer.tableHalfLinePaint.setColor(-3620432);
            ArticleViewer.tableLinePaint.setColor(-3620432);
            ArticleViewer.tableHeaderPaint.setColor(-1120560);
            ArticleViewer.tableStripPaint.setColor(-1120560);
            ArticleViewer.photoBackgroundPaint.setColor(-1120560);
            ArticleViewer.dividerPaint.setColor(-4080987);
            ArticleViewer.webpageMarkPaint.setColor(-1712691);
        }
        else if (selectedColor == 2) {
            ArticleViewer.preformattedBackgroundPaint.setColor(-15000805);
            ArticleViewer.webpageUrlPaint.setColor(-14536904);
            ArticleViewer.urlPaint.setColor(-14469050);
            ArticleViewer.tableHalfLinePaint.setColor(-13750738);
            ArticleViewer.tableLinePaint.setColor(-13750738);
            ArticleViewer.tableHeaderPaint.setColor(-15066598);
            ArticleViewer.tableStripPaint.setColor(-15066598);
            ArticleViewer.photoBackgroundPaint.setColor(-14935012);
            ArticleViewer.dividerPaint.setColor(-12303292);
            ArticleViewer.webpageMarkPaint.setColor(-14408668);
        }
        ArticleViewer.quoteLinePaint.setColor(this.getTextColor());
    }
    
    private void drawContent(final Canvas canvas) {
        final int photoAnimationInProgress = this.photoAnimationInProgress;
        if (photoAnimationInProgress != 1) {
            if (this.isPhotoVisible || photoAnimationInProgress == 2) {
                float scale2;
                float translationX2;
                float n;
                float translationY2;
                if (this.imageMoveAnimation != null) {
                    if (!this.scroller.isFinished()) {
                        this.scroller.abortAnimation();
                    }
                    final float scale = this.scale;
                    final float animateToScale = this.animateToScale;
                    final float animationValue = this.animationValue;
                    scale2 = (animateToScale - scale) * animationValue + scale;
                    final float translationX = this.translationX;
                    translationX2 = (this.animateToX - translationX) * animationValue + translationX;
                    final float translationY = this.translationY;
                    n = translationY + (this.animateToY - translationY) * animationValue;
                    if (animateToScale == 1.0f && scale == 1.0f && translationX == 0.0f) {
                        translationY2 = n;
                    }
                    else {
                        translationY2 = -1.0f;
                    }
                    this.photoContainerView.invalidate();
                }
                else {
                    if (this.animationStartTime != 0L) {
                        this.translationX = this.animateToX;
                        this.translationY = this.animateToY;
                        this.scale = this.animateToScale;
                        this.animationStartTime = 0L;
                        this.updateMinMax(this.scale);
                        this.zoomAnimation = false;
                    }
                    if (!this.scroller.isFinished() && this.scroller.computeScrollOffset()) {
                        if (this.scroller.getStartX() < this.maxX && this.scroller.getStartX() > this.minX) {
                            this.translationX = (float)this.scroller.getCurrX();
                        }
                        if (this.scroller.getStartY() < this.maxY && this.scroller.getStartY() > this.minY) {
                            this.translationY = (float)this.scroller.getCurrY();
                        }
                        this.photoContainerView.invalidate();
                    }
                    final int switchImageAfterAnimation = this.switchImageAfterAnimation;
                    if (switchImageAfterAnimation != 0) {
                        if (switchImageAfterAnimation == 1) {
                            AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$sVetLHzILrx_miSeiNhv3ir2iLY(this));
                        }
                        else if (switchImageAfterAnimation == 2) {
                            AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$oXQW1t29RA_lRZqbkS6wEfWeLck(this));
                        }
                        this.switchImageAfterAnimation = 0;
                    }
                    scale2 = this.scale;
                    translationY2 = this.translationY;
                    translationX2 = this.translationX;
                    if (!this.moving) {
                        n = translationY2;
                    }
                    else {
                        n = translationY2;
                        translationY2 = -1.0f;
                    }
                }
                if (this.photoAnimationInProgress != 2) {
                    if (this.scale == 1.0f && translationY2 != -1.0f && !this.zoomAnimation) {
                        final float b = this.getContainerViewHeight() / 4.0f;
                        this.photoBackgroundDrawable.setAlpha((int)Math.max(127.0f, (1.0f - Math.min(Math.abs(translationY2), b) / b) * 255.0f));
                    }
                    else {
                        this.photoBackgroundDrawable.setAlpha(255);
                    }
                }
                ImageReceiver imageReceiver2;
                final ImageReceiver imageReceiver = imageReceiver2 = null;
                if (this.scale >= 1.0f) {
                    imageReceiver2 = imageReceiver;
                    if (!this.zoomAnimation) {
                        imageReceiver2 = imageReceiver;
                        if (!this.zooming) {
                            if (translationX2 > this.maxX + AndroidUtilities.dp(5.0f)) {
                                imageReceiver2 = this.leftImage;
                            }
                            else if (translationX2 < this.minX - AndroidUtilities.dp(5.0f)) {
                                imageReceiver2 = this.rightImage;
                            }
                            else {
                                this.groupedPhotosListView.setMoveProgress(0.0f);
                                imageReceiver2 = imageReceiver;
                            }
                        }
                    }
                }
                this.changingPage = (imageReceiver2 != null);
                if (imageReceiver2 == this.rightImage) {
                    float min = 0.0f;
                    float n2 = 0.0f;
                    float n3 = 0.0f;
                    Label_0716: {
                        if (!this.zoomAnimation) {
                            final float minX = this.minX;
                            if (translationX2 < minX) {
                                min = Math.min(1.0f, (minX - translationX2) / canvas.getWidth());
                                n2 = (1.0f - min) * 0.3f;
                                n3 = (float)(-canvas.getWidth() - AndroidUtilities.dp(30.0f) / 2);
                                break Label_0716;
                            }
                        }
                        n3 = translationX2;
                        min = 1.0f;
                        n2 = 0.0f;
                    }
                    if (imageReceiver2.hasBitmapImage()) {
                        canvas.save();
                        canvas.translate((float)(this.getContainerViewWidth() / 2), (float)(this.getContainerViewHeight() / 2));
                        canvas.translate(canvas.getWidth() + AndroidUtilities.dp(30.0f) / 2 + n3, 0.0f);
                        final float n4 = 1.0f - n2;
                        canvas.scale(n4, n4);
                        final int bitmapWidth = imageReceiver2.getBitmapWidth();
                        final int bitmapHeight = imageReceiver2.getBitmapHeight();
                        final float n5 = (float)this.getContainerViewWidth();
                        final float n6 = (float)bitmapWidth;
                        final float n7 = n5 / n6;
                        final float n8 = (float)this.getContainerViewHeight();
                        final float n9 = (float)bitmapHeight;
                        final float n10 = n8 / n9;
                        float n11 = n7;
                        if (n7 > n10) {
                            n11 = n10;
                        }
                        final int n12 = (int)(n6 * n11);
                        final int n13 = (int)(n9 * n11);
                        imageReceiver2.setAlpha(min);
                        imageReceiver2.setImageCoords(-n12 / 2, -n13 / 2, n12, n13);
                        imageReceiver2.draw(canvas);
                        canvas.restore();
                    }
                    this.groupedPhotosListView.setMoveProgress(-min);
                    canvas.save();
                    canvas.translate(n3, n / scale2);
                    canvas.translate((canvas.getWidth() * (this.scale + 1.0f) + AndroidUtilities.dp(30.0f)) / 2.0f, -n / scale2);
                    this.radialProgressViews[1].setScale(1.0f - n2);
                    this.radialProgressViews[1].setAlpha(min);
                    this.radialProgressViews[1].onDraw(canvas);
                    canvas.restore();
                }
                float n14 = 0.0f;
                float n15 = 0.0f;
                float maxX2 = 0.0f;
                Label_1062: {
                    if (!this.zoomAnimation) {
                        final float maxX = this.maxX;
                        if (translationX2 > maxX) {
                            final float min2 = Math.min(1.0f, (translationX2 - maxX) / canvas.getWidth());
                            n14 = min2 * 0.3f;
                            n15 = 1.0f - min2;
                            maxX2 = this.maxX;
                            break Label_1062;
                        }
                    }
                    maxX2 = translationX2;
                    n15 = 1.0f;
                    n14 = 0.0f;
                }
                final AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
                final boolean b2 = aspectRatioFrameLayout != null && aspectRatioFrameLayout.getVisibility() == 0;
                if (this.centerImage.hasBitmapImage()) {
                    canvas.save();
                    canvas.translate((float)(this.getContainerViewWidth() / 2), (float)(this.getContainerViewHeight() / 2));
                    canvas.translate(maxX2, n);
                    final float n16 = scale2 - n14;
                    canvas.scale(n16, n16);
                    final int bitmapWidth2 = this.centerImage.getBitmapWidth();
                    final int bitmapHeight2 = this.centerImage.getBitmapHeight();
                    int measuredWidth = bitmapWidth2;
                    int measuredHeight = bitmapHeight2;
                    if (b2) {
                        measuredWidth = bitmapWidth2;
                        measuredHeight = bitmapHeight2;
                        if (this.textureUploaded) {
                            measuredWidth = bitmapWidth2;
                            measuredHeight = bitmapHeight2;
                            if (Math.abs(bitmapWidth2 / (float)bitmapHeight2 - this.videoTextureView.getMeasuredWidth() / (float)this.videoTextureView.getMeasuredHeight()) > 0.01f) {
                                measuredWidth = this.videoTextureView.getMeasuredWidth();
                                measuredHeight = this.videoTextureView.getMeasuredHeight();
                            }
                        }
                    }
                    final float n17 = (float)this.getContainerViewWidth();
                    final float n18 = (float)measuredWidth;
                    float n19 = n17 / n18;
                    final float n20 = (float)this.getContainerViewHeight();
                    final float n21 = (float)measuredHeight;
                    final float n22 = n20 / n21;
                    if (n19 > n22) {
                        n19 = n22;
                    }
                    final int n23 = (int)(n18 * n19);
                    final int n24 = (int)(n21 * n19);
                    if (!b2 || !this.textureUploaded || !this.videoCrossfadeStarted || this.videoCrossfadeAlpha != 1.0f) {
                        this.centerImage.setAlpha(n15);
                        this.centerImage.setImageCoords(-n23 / 2, -n24 / 2, n23, n24);
                        this.centerImage.draw(canvas);
                    }
                    if (b2) {
                        if (!this.videoCrossfadeStarted && this.textureUploaded) {
                            this.videoCrossfadeStarted = true;
                            this.videoCrossfadeAlpha = 0.0f;
                            this.videoCrossfadeAlphaLastTime = System.currentTimeMillis();
                        }
                        canvas.translate((float)(-n23 / 2), (float)(-n24 / 2));
                        this.videoTextureView.setAlpha(this.videoCrossfadeAlpha * n15);
                        this.aspectRatioFrameLayout.draw(canvas);
                        if (this.videoCrossfadeStarted && this.videoCrossfadeAlpha < 1.0f) {
                            final long currentTimeMillis = System.currentTimeMillis();
                            final long videoCrossfadeAlphaLastTime = this.videoCrossfadeAlphaLastTime;
                            this.videoCrossfadeAlphaLastTime = currentTimeMillis;
                            this.videoCrossfadeAlpha += (currentTimeMillis - videoCrossfadeAlphaLastTime) / 300.0f;
                            this.photoContainerView.invalidate();
                            if (this.videoCrossfadeAlpha > 1.0f) {
                                this.videoCrossfadeAlpha = 1.0f;
                            }
                        }
                    }
                    canvas.restore();
                }
                if (!b2 && this.bottomLayout.getVisibility() != 0) {
                    canvas.save();
                    canvas.translate(maxX2, n / scale2);
                    this.radialProgressViews[0].setScale(1.0f - n14);
                    this.radialProgressViews[0].setAlpha(n15);
                    this.radialProgressViews[0].onDraw(canvas);
                    canvas.restore();
                }
                if (imageReceiver2 == this.leftImage) {
                    if (imageReceiver2.hasBitmapImage()) {
                        canvas.save();
                        canvas.translate((float)(this.getContainerViewWidth() / 2), (float)(this.getContainerViewHeight() / 2));
                        canvas.translate(-(canvas.getWidth() * (this.scale + 1.0f) + AndroidUtilities.dp(30.0f)) / 2.0f + translationX2, 0.0f);
                        final int bitmapWidth3 = imageReceiver2.getBitmapWidth();
                        final int bitmapHeight3 = imageReceiver2.getBitmapHeight();
                        final float n25 = (float)this.getContainerViewWidth();
                        final float n26 = (float)bitmapWidth3;
                        final float n27 = n25 / n26;
                        final float n28 = (float)this.getContainerViewHeight();
                        final float n29 = (float)bitmapHeight3;
                        final float n30 = n28 / n29;
                        float n31 = n27;
                        if (n27 > n30) {
                            n31 = n30;
                        }
                        final int n32 = (int)(n26 * n31);
                        final int n33 = (int)(n29 * n31);
                        imageReceiver2.setAlpha(1.0f);
                        imageReceiver2.setImageCoords(-n32 / 2, -n33 / 2, n32, n33);
                        imageReceiver2.draw(canvas);
                        canvas.restore();
                    }
                    this.groupedPhotosListView.setMoveProgress(1.0f - n15);
                    canvas.save();
                    canvas.translate(translationX2, n / scale2);
                    canvas.translate(-(canvas.getWidth() * (this.scale + 1.0f) + AndroidUtilities.dp(30.0f)) / 2.0f, -n / scale2);
                    this.radialProgressViews[2].setScale(1.0f);
                    this.radialProgressViews[2].setAlpha(1.0f);
                    this.radialProgressViews[2].onDraw(canvas);
                    canvas.restore();
                }
            }
        }
    }
    
    private void drawLayoutLink(final Canvas canvas, final DrawingText drawingText) {
        if (canvas != null && drawingText != null) {
            if (this.pressedLinkOwnerLayout == drawingText) {
                if (this.pressedLink != null) {
                    canvas.drawPath((Path)this.urlPath, ArticleViewer.urlPaint);
                }
                else if (this.drawBlockSelection && drawingText != null) {
                    float lineWidth;
                    float lineLeft;
                    if (drawingText.getLineCount() == 1) {
                        lineWidth = drawingText.getLineWidth(0);
                        lineLeft = drawingText.getLineLeft(0);
                    }
                    else {
                        lineWidth = (float)drawingText.getWidth();
                        lineLeft = 0.0f;
                    }
                    canvas.drawRect(-AndroidUtilities.dp(2.0f) + lineLeft, 0.0f, lineLeft + lineWidth + AndroidUtilities.dp(2.0f), (float)drawingText.getHeight(), ArticleViewer.urlPaint);
                }
            }
        }
    }
    
    private TLRPC.PageBlock fixListBlock(final TLRPC.PageBlock pageBlock, final TLRPC.PageBlock pageBlock2) {
        if (pageBlock instanceof TL_pageBlockListItem) {
            ((TL_pageBlockListItem)pageBlock).blockItem = pageBlock2;
            return pageBlock;
        }
        if (pageBlock instanceof TL_pageBlockOrderedListItem) {
            ((TL_pageBlockOrderedListItem)pageBlock).blockItem = pageBlock2;
            return pageBlock;
        }
        return pageBlock2;
    }
    
    private TLRPC.RichText getBlockCaption(final TLRPC.PageBlock pageBlock, final int n) {
        if (n != 2) {
            if (pageBlock instanceof TLRPC.TL_pageBlockEmbedPost) {
                final TLRPC.TL_pageBlockEmbedPost tl_pageBlockEmbedPost = (TLRPC.TL_pageBlockEmbedPost)pageBlock;
                if (n == 0) {
                    return tl_pageBlockEmbedPost.caption.text;
                }
                if (n == 1) {
                    return tl_pageBlockEmbedPost.caption.credit;
                }
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockSlideshow) {
                final TLRPC.TL_pageBlockSlideshow tl_pageBlockSlideshow = (TLRPC.TL_pageBlockSlideshow)pageBlock;
                if (n == 0) {
                    return tl_pageBlockSlideshow.caption.text;
                }
                if (n == 1) {
                    return tl_pageBlockSlideshow.caption.credit;
                }
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockPhoto) {
                final TLRPC.TL_pageBlockPhoto tl_pageBlockPhoto = (TLRPC.TL_pageBlockPhoto)pageBlock;
                if (n == 0) {
                    return tl_pageBlockPhoto.caption.text;
                }
                if (n == 1) {
                    return tl_pageBlockPhoto.caption.credit;
                }
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockCollage) {
                final TLRPC.TL_pageBlockCollage tl_pageBlockCollage = (TLRPC.TL_pageBlockCollage)pageBlock;
                if (n == 0) {
                    return tl_pageBlockCollage.caption.text;
                }
                if (n == 1) {
                    return tl_pageBlockCollage.caption.credit;
                }
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockEmbed) {
                final TLRPC.TL_pageBlockEmbed tl_pageBlockEmbed = (TLRPC.TL_pageBlockEmbed)pageBlock;
                if (n == 0) {
                    return tl_pageBlockEmbed.caption.text;
                }
                if (n == 1) {
                    return tl_pageBlockEmbed.caption.credit;
                }
            }
            else {
                if (pageBlock instanceof TLRPC.TL_pageBlockBlockquote) {
                    return ((TLRPC.TL_pageBlockBlockquote)pageBlock).caption;
                }
                if (pageBlock instanceof TLRPC.TL_pageBlockVideo) {
                    final TLRPC.TL_pageBlockVideo tl_pageBlockVideo = (TLRPC.TL_pageBlockVideo)pageBlock;
                    if (n == 0) {
                        return tl_pageBlockVideo.caption.text;
                    }
                    if (n == 1) {
                        return tl_pageBlockVideo.caption.credit;
                    }
                }
                else {
                    if (pageBlock instanceof TLRPC.TL_pageBlockPullquote) {
                        return ((TLRPC.TL_pageBlockPullquote)pageBlock).caption;
                    }
                    if (pageBlock instanceof TLRPC.TL_pageBlockAudio) {
                        final TLRPC.TL_pageBlockAudio tl_pageBlockAudio = (TLRPC.TL_pageBlockAudio)pageBlock;
                        if (n == 0) {
                            return tl_pageBlockAudio.caption.text;
                        }
                        if (n == 1) {
                            return tl_pageBlockAudio.caption.credit;
                        }
                    }
                    else {
                        if (pageBlock instanceof TLRPC.TL_pageBlockCover) {
                            return this.getBlockCaption(((TLRPC.TL_pageBlockCover)pageBlock).cover, n);
                        }
                        if (pageBlock instanceof TLRPC.TL_pageBlockMap) {
                            final TLRPC.TL_pageBlockMap tl_pageBlockMap = (TLRPC.TL_pageBlockMap)pageBlock;
                            if (n == 0) {
                                return tl_pageBlockMap.caption.text;
                            }
                            if (n == 1) {
                                return tl_pageBlockMap.caption.credit;
                            }
                        }
                    }
                }
            }
            return null;
        }
        TLRPC.RichText blockCaption;
        if ((blockCaption = this.getBlockCaption(pageBlock, 0)) instanceof TLRPC.TL_textEmpty) {
            blockCaption = null;
        }
        TLRPC.RichText blockCaption2;
        if ((blockCaption2 = this.getBlockCaption(pageBlock, 1)) instanceof TLRPC.TL_textEmpty) {
            blockCaption2 = null;
        }
        if (blockCaption != null && blockCaption2 == null) {
            return blockCaption;
        }
        if (blockCaption == null && blockCaption2 != null) {
            return blockCaption2;
        }
        if (blockCaption != null && blockCaption2 != null) {
            final TLRPC.TL_textPlain e = new TLRPC.TL_textPlain();
            e.text = " ";
            final TLRPC.TL_textConcat tl_textConcat = new TLRPC.TL_textConcat();
            tl_textConcat.texts.add(blockCaption);
            tl_textConcat.texts.add((TLRPC.RichText)e);
            tl_textConcat.texts.add(blockCaption2);
            return tl_textConcat;
        }
        return null;
    }
    
    private int getContainerViewHeight() {
        return this.photoContainerView.getHeight();
    }
    
    private int getContainerViewWidth() {
        return this.photoContainerView.getWidth();
    }
    
    private TLRPC.Document getDocumentWithId(final long n) {
        final TLRPC.WebPage currentPage = this.currentPage;
        if (currentPage != null) {
            if (currentPage.cached_page != null) {
                final TLRPC.Document document = currentPage.document;
                if (document != null && document.id == n) {
                    return document;
                }
                for (int i = 0; i < this.currentPage.cached_page.documents.size(); ++i) {
                    final TLRPC.Document document2 = this.currentPage.cached_page.documents.get(i);
                    if (document2.id == n) {
                        return document2;
                    }
                }
            }
        }
        return null;
    }
    
    private TLRPC.PhotoSize getFileLocation(final TLObject tlObject, final int[] array) {
        if (tlObject instanceof TLRPC.Photo) {
            final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Photo)tlObject).sizes, AndroidUtilities.getPhotoSize());
            if (closestPhotoSizeWithSize != null) {
                array[0] = closestPhotoSizeWithSize.size;
                if (array[0] == 0) {
                    array[0] = -1;
                }
                return closestPhotoSizeWithSize;
            }
            array[0] = -1;
        }
        else if (tlObject instanceof TLRPC.Document) {
            final TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Document)tlObject).thumbs, 90);
            if (closestPhotoSizeWithSize2 != null) {
                array[0] = closestPhotoSizeWithSize2.size;
                if (array[0] == 0) {
                    array[0] = -1;
                }
                return closestPhotoSizeWithSize2;
            }
        }
        return null;
    }
    
    private String getFileName(final int n) {
        TLObject tlObject2;
        final TLObject tlObject = tlObject2 = this.getMedia(n);
        if (tlObject instanceof TLRPC.Photo) {
            tlObject2 = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Photo)tlObject).sizes, AndroidUtilities.getPhotoSize());
        }
        return FileLoader.getAttachFileName(tlObject2);
    }
    
    private int getGrayTextColor() {
        final int selectedColor = this.getSelectedColor();
        if (selectedColor == 0) {
            return -8156010;
        }
        if (selectedColor != 1) {
            return -10066330;
        }
        return -11711675;
    }
    
    private ImageReceiver getImageReceiverFromListView(final ViewGroup viewGroup, final TLRPC.PageBlock pageBlock, final int[] array) {
        for (int childCount = viewGroup.getChildCount(), i = 0; i < childCount; ++i) {
            final ImageReceiver imageReceiverView = this.getImageReceiverView(viewGroup.getChildAt(i), pageBlock, array);
            if (imageReceiverView != null) {
                return imageReceiverView;
            }
        }
        return null;
    }
    
    private ImageReceiver getImageReceiverView(final View view, final TLRPC.PageBlock pageBlock, final int[] array) {
        if (view instanceof BlockPhotoCell) {
            final BlockPhotoCell blockPhotoCell = (BlockPhotoCell)view;
            if (blockPhotoCell.currentBlock == pageBlock) {
                view.getLocationInWindow(array);
                return blockPhotoCell.imageView;
            }
        }
        else if (view instanceof BlockVideoCell) {
            final BlockVideoCell blockVideoCell = (BlockVideoCell)view;
            if (blockVideoCell.currentBlock == pageBlock) {
                view.getLocationInWindow(array);
                return blockVideoCell.imageView;
            }
        }
        else if (view instanceof BlockCollageCell) {
            final ImageReceiver imageReceiverFromListView = this.getImageReceiverFromListView(((BlockCollageCell)view).innerListView, pageBlock, array);
            if (imageReceiverFromListView != null) {
                return imageReceiverFromListView;
            }
        }
        else if (view instanceof BlockSlideshowCell) {
            final ImageReceiver imageReceiverFromListView2 = this.getImageReceiverFromListView(((BlockSlideshowCell)view).innerListView, pageBlock, array);
            if (imageReceiverFromListView2 != null) {
                return imageReceiverFromListView2;
            }
        }
        else if (view instanceof BlockListItemCell) {
            final BlockListItemCell blockListItemCell = (BlockListItemCell)view;
            if (blockListItemCell.blockLayout != null) {
                final ImageReceiver imageReceiverView = this.getImageReceiverView(blockListItemCell.blockLayout.itemView, pageBlock, array);
                if (imageReceiverView != null) {
                    return imageReceiverView;
                }
            }
        }
        else if (view instanceof BlockOrderedListItemCell) {
            final BlockOrderedListItemCell blockOrderedListItemCell = (BlockOrderedListItemCell)view;
            if (blockOrderedListItemCell.blockLayout != null) {
                final ImageReceiver imageReceiverView2 = this.getImageReceiverView(blockOrderedListItemCell.blockLayout.itemView, pageBlock, array);
                if (imageReceiverView2 != null) {
                    return imageReceiverView2;
                }
            }
        }
        return null;
    }
    
    public static ArticleViewer getInstance() {
        final ArticleViewer instance;
        if ((instance = ArticleViewer.Instance) == null) {
            synchronized (ArticleViewer.class) {
                if (ArticleViewer.Instance == null) {
                    ArticleViewer.Instance = new ArticleViewer();
                }
            }
        }
        return instance;
    }
    
    private int getInstantLinkBackgroundColor() {
        final int selectedColor = this.getSelectedColor();
        if (selectedColor == 0) {
            return -1707782;
        }
        if (selectedColor != 1) {
            return -14536904;
        }
        return -2498337;
    }
    
    private View getLastNonListCell(final View view) {
        View lastNonListCell;
        if (view instanceof BlockListItemCell) {
            final BlockListItemCell blockListItemCell = (BlockListItemCell)view;
            lastNonListCell = view;
            if (blockListItemCell.blockLayout != null) {
                return this.getLastNonListCell(blockListItemCell.blockLayout.itemView);
            }
        }
        else {
            lastNonListCell = view;
            if (view instanceof BlockOrderedListItemCell) {
                final BlockOrderedListItemCell blockOrderedListItemCell = (BlockOrderedListItemCell)view;
                lastNonListCell = view;
                if (blockOrderedListItemCell.blockLayout != null) {
                    lastNonListCell = this.getLastNonListCell(blockOrderedListItemCell.blockLayout.itemView);
                }
            }
        }
        return lastNonListCell;
    }
    
    private TLRPC.PageBlock getLastNonListPageBlock(final TLRPC.PageBlock pageBlock) {
        if (!(pageBlock instanceof TL_pageBlockListItem)) {
            TLRPC.PageBlock access$5400 = pageBlock;
            if (pageBlock instanceof TL_pageBlockOrderedListItem) {
                final TL_pageBlockOrderedListItem tl_pageBlockOrderedListItem = (TL_pageBlockOrderedListItem)pageBlock;
                if (tl_pageBlockOrderedListItem.blockItem != null) {
                    return this.getLastNonListPageBlock(tl_pageBlockOrderedListItem.blockItem);
                }
                access$5400 = tl_pageBlockOrderedListItem.blockItem;
            }
            return access$5400;
        }
        final TL_pageBlockListItem tl_pageBlockListItem = (TL_pageBlockListItem)pageBlock;
        if (tl_pageBlockListItem.blockItem != null) {
            return this.getLastNonListPageBlock(tl_pageBlockListItem.blockItem);
        }
        return tl_pageBlockListItem.blockItem;
    }
    
    private TLRPC.RichText getLastRichText(TLRPC.RichText text) {
        if (text == null) {
            return null;
        }
        if (text instanceof TLRPC.TL_textFixed) {
            return this.getLastRichText(((TLRPC.TL_textFixed)text).text);
        }
        if (text instanceof TLRPC.TL_textItalic) {
            return this.getLastRichText(((TLRPC.TL_textItalic)text).text);
        }
        if (text instanceof TLRPC.TL_textBold) {
            return this.getLastRichText(((TLRPC.TL_textBold)text).text);
        }
        if (text instanceof TLRPC.TL_textUnderline) {
            return this.getLastRichText(((TLRPC.TL_textUnderline)text).text);
        }
        if (text instanceof TLRPC.TL_textStrike) {
            return this.getLastRichText(((TLRPC.TL_textStrike)text).text);
        }
        if (text instanceof TLRPC.TL_textEmail) {
            return this.getLastRichText(((TLRPC.TL_textEmail)text).text);
        }
        if (text instanceof TLRPC.TL_textUrl) {
            return this.getLastRichText(((TLRPC.TL_textUrl)text).text);
        }
        if (text instanceof TLRPC.TL_textAnchor) {
            this.getLastRichText(((TLRPC.TL_textAnchor)text).text);
            return text;
        }
        if (text instanceof TLRPC.TL_textSubscript) {
            return this.getLastRichText(((TLRPC.TL_textSubscript)text).text);
        }
        if (text instanceof TLRPC.TL_textSuperscript) {
            return this.getLastRichText(((TLRPC.TL_textSuperscript)text).text);
        }
        if (text instanceof TLRPC.TL_textMarked) {
            return this.getLastRichText(((TLRPC.TL_textMarked)text).text);
        }
        TLRPC.RichText lastRichText = text;
        if (!(text instanceof TLRPC.TL_textPhone)) {
            return lastRichText;
        }
        text = ((TLRPC.TL_textPhone)text).text;
        try {
            lastRichText = this.getLastRichText(text);
            return lastRichText;
        }
        catch (Throwable t) {
            throw t;
        }
    }
    
    private int getLinkTextColor() {
        final int selectedColor = this.getSelectedColor();
        if (selectedColor == 0) {
            return -15435321;
        }
        if (selectedColor != 1) {
            return -10838585;
        }
        return -13471296;
    }
    
    private TLObject getMedia(final int index) {
        if (!this.imagesArr.isEmpty() && index < this.imagesArr.size()) {
            if (index >= 0) {
                final TLRPC.PageBlock pageBlock = this.imagesArr.get(index);
                if (pageBlock instanceof TLRPC.TL_pageBlockPhoto) {
                    return this.getPhotoWithId(((TLRPC.TL_pageBlockPhoto)pageBlock).photo_id);
                }
                if (pageBlock instanceof TLRPC.TL_pageBlockVideo) {
                    return this.getDocumentWithId(((TLRPC.TL_pageBlockVideo)pageBlock).video_id);
                }
            }
        }
        return null;
    }
    
    private File getMediaFile(final int index) {
        if (!this.imagesArr.isEmpty() && index < this.imagesArr.size()) {
            if (index >= 0) {
                final TLRPC.PageBlock pageBlock = this.imagesArr.get(index);
                if (pageBlock instanceof TLRPC.TL_pageBlockPhoto) {
                    final TLRPC.Photo photoWithId = this.getPhotoWithId(((TLRPC.TL_pageBlockPhoto)pageBlock).photo_id);
                    if (photoWithId != null) {
                        final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photoWithId.sizes, AndroidUtilities.getPhotoSize());
                        if (closestPhotoSizeWithSize != null) {
                            return FileLoader.getPathToAttach(closestPhotoSizeWithSize, true);
                        }
                    }
                }
                else if (pageBlock instanceof TLRPC.TL_pageBlockVideo) {
                    final TLRPC.Document documentWithId = this.getDocumentWithId(((TLRPC.TL_pageBlockVideo)pageBlock).video_id);
                    if (documentWithId != null) {
                        return FileLoader.getPathToAttach(documentWithId, true);
                    }
                }
            }
        }
        return null;
    }
    
    private String getMediaMime(final int index) {
        if (index < this.imagesArr.size()) {
            if (index >= 0) {
                final TLRPC.PageBlock pageBlock = this.imagesArr.get(index);
                if (pageBlock instanceof TLRPC.TL_pageBlockVideo) {
                    final TLRPC.Document documentWithId = this.getDocumentWithId(((TLRPC.TL_pageBlockVideo)pageBlock).video_id);
                    if (documentWithId != null) {
                        return documentWithId.mime_type;
                    }
                }
            }
        }
        return "image/jpeg";
    }
    
    private TLRPC.Photo getPhotoWithId(final long n) {
        final TLRPC.WebPage currentPage = this.currentPage;
        if (currentPage != null) {
            if (currentPage.cached_page != null) {
                final TLRPC.Photo photo = currentPage.photo;
                if (photo != null && photo.id == n) {
                    return photo;
                }
                for (int i = 0; i < this.currentPage.cached_page.photos.size(); ++i) {
                    final TLRPC.Photo photo2 = this.currentPage.cached_page.photos.get(i);
                    if (photo2.id == n) {
                        return photo2;
                    }
                }
            }
        }
        return null;
    }
    
    private PlaceProviderObject getPlaceForPhoto(final TLRPC.PageBlock pageBlock) {
        final ImageReceiver imageReceiverFromListView = this.getImageReceiverFromListView(this.listView[0], pageBlock, this.coords);
        if (imageReceiverFromListView == null) {
            return null;
        }
        final PlaceProviderObject placeProviderObject = new PlaceProviderObject();
        final int[] coords = this.coords;
        placeProviderObject.viewX = coords[0];
        placeProviderObject.viewY = coords[1];
        placeProviderObject.parentView = (View)this.listView[0];
        placeProviderObject.imageReceiver = imageReceiverFromListView;
        placeProviderObject.thumb = imageReceiverFromListView.getBitmapSafe();
        placeProviderObject.radius = imageReceiverFromListView.getRoundRadius();
        placeProviderObject.clipTopAddition = this.currentHeaderHeight;
        return placeProviderObject;
    }
    
    public static CharSequence getPlainText(TLRPC.RichText text) {
        if (text == null) {
            return "";
        }
        if (text instanceof TLRPC.TL_textFixed) {
            return getPlainText(((TLRPC.TL_textFixed)text).text);
        }
        if (text instanceof TLRPC.TL_textItalic) {
            return getPlainText(((TLRPC.TL_textItalic)text).text);
        }
        if (text instanceof TLRPC.TL_textBold) {
            return getPlainText(((TLRPC.TL_textBold)text).text);
        }
        if (text instanceof TLRPC.TL_textUnderline) {
            return getPlainText(((TLRPC.TL_textUnderline)text).text);
        }
        if (text instanceof TLRPC.TL_textStrike) {
            return getPlainText(((TLRPC.TL_textStrike)text).text);
        }
        if (text instanceof TLRPC.TL_textEmail) {
            return getPlainText(((TLRPC.TL_textEmail)text).text);
        }
        if (text instanceof TLRPC.TL_textUrl) {
            return getPlainText(((TLRPC.TL_textUrl)text).text);
        }
        if (text instanceof TLRPC.TL_textPlain) {
            return ((TLRPC.TL_textPlain)text).text;
        }
        if (text instanceof TLRPC.TL_textAnchor) {
            return getPlainText(((TLRPC.TL_textAnchor)text).text);
        }
        if (text instanceof TLRPC.TL_textEmpty) {
            return "";
        }
        if (text instanceof TLRPC.TL_textConcat) {
            final StringBuilder sb = new StringBuilder();
            for (int size = text.texts.size(), i = 0; i < size; ++i) {
                sb.append(getPlainText(text.texts.get(i)));
            }
            return sb;
        }
        if (text instanceof TLRPC.TL_textSubscript) {
            return getPlainText(((TLRPC.TL_textSubscript)text).text);
        }
        if (text instanceof TLRPC.TL_textSuperscript) {
            return getPlainText(((TLRPC.TL_textSuperscript)text).text);
        }
        if (text instanceof TLRPC.TL_textMarked) {
            return getPlainText(((TLRPC.TL_textMarked)text).text);
        }
        Label_0311: {
            if (!(text instanceof TLRPC.TL_textPhone)) {
                break Label_0311;
            }
            text = ((TLRPC.TL_textPhone)text).text;
            try {
                return getPlainText(text);
                Label_0318: {
                    return "";
                }
                // iftrue(Label_0318:, !text instanceof TLRPC.TL_textImage)
                return "";
            }
            catch (Throwable t) {
                throw t;
            }
        }
    }
    
    private int getSelectedColor() {
        final int selectedColor = this.selectedColor;
        final boolean nightModeEnabled = this.nightModeEnabled;
        final int n = 2;
        if (nightModeEnabled && selectedColor != 2) {
            if (Theme.selectedAutoNightType != 0) {
                if (Theme.isCurrentThemeNight()) {
                    return n;
                }
            }
            else {
                final int value = Calendar.getInstance().get(11);
                if (value >= 22) {
                    final int n2 = n;
                    if (value <= 24) {
                        return n2;
                    }
                }
                if (value >= 0 && value <= 6) {
                    return n;
                }
            }
        }
        return selectedColor;
    }
    
    private CharSequence getText(final View view, final TLRPC.RichText richText, final TLRPC.RichText obj, final TLRPC.PageBlock pageBlock, int abs) {
        final TextPaint textPaint = null;
        if (obj == null) {
            return null;
        }
        if (obj instanceof TLRPC.TL_textFixed) {
            return this.getText(view, richText, ((TLRPC.TL_textFixed)obj).text, pageBlock, abs);
        }
        if (obj instanceof TLRPC.TL_textItalic) {
            return this.getText(view, richText, ((TLRPC.TL_textItalic)obj).text, pageBlock, abs);
        }
        if (obj instanceof TLRPC.TL_textBold) {
            return this.getText(view, richText, ((TLRPC.TL_textBold)obj).text, pageBlock, abs);
        }
        if (obj instanceof TLRPC.TL_textUnderline) {
            return this.getText(view, richText, ((TLRPC.TL_textUnderline)obj).text, pageBlock, abs);
        }
        if (obj instanceof TLRPC.TL_textStrike) {
            return this.getText(view, richText, ((TLRPC.TL_textStrike)obj).text, pageBlock, abs);
        }
        if (obj instanceof TLRPC.TL_textEmail) {
            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.getText(view, richText, ((TLRPC.TL_textEmail)obj).text, pageBlock, abs));
            final MetricAffectingSpan[] array = (MetricAffectingSpan[])spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), (Class)MetricAffectingSpan.class);
            if (spannableStringBuilder.length() != 0) {
                TextPaint textPaint2 = null;
                Label_0217: {
                    if (array != null) {
                        textPaint2 = textPaint;
                        if (array.length != 0) {
                            break Label_0217;
                        }
                    }
                    textPaint2 = this.getTextPaint(richText, obj, pageBlock);
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("mailto:");
                sb.append(getUrl(obj));
                spannableStringBuilder.setSpan((Object)new TextPaintUrlSpan(textPaint2, sb.toString()), 0, spannableStringBuilder.length(), 33);
            }
            return (CharSequence)spannableStringBuilder;
        }
        if (obj instanceof TLRPC.TL_textUrl) {
            final TLRPC.TL_textUrl tl_textUrl = (TLRPC.TL_textUrl)obj;
            final SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(this.getText(view, richText, tl_textUrl.text, pageBlock, abs));
            final MetricAffectingSpan[] array2 = (MetricAffectingSpan[])spannableStringBuilder2.getSpans(0, spannableStringBuilder2.length(), (Class)MetricAffectingSpan.class);
            TextPaint textPaint3;
            if (array2 != null && array2.length != 0) {
                textPaint3 = null;
            }
            else {
                textPaint3 = this.getTextPaint(richText, obj, pageBlock);
            }
            TextPaintUrlSpan textPaintUrlSpan;
            if (tl_textUrl.webpage_id != 0L) {
                textPaintUrlSpan = new TextPaintWebpageUrlSpan(textPaint3, getUrl(obj));
            }
            else {
                textPaintUrlSpan = new TextPaintUrlSpan(textPaint3, getUrl(obj));
            }
            if (spannableStringBuilder2.length() != 0) {
                spannableStringBuilder2.setSpan((Object)textPaintUrlSpan, 0, spannableStringBuilder2.length(), 33);
            }
            return (CharSequence)spannableStringBuilder2;
        }
        if (obj instanceof TLRPC.TL_textPlain) {
            return ((TLRPC.TL_textPlain)obj).text;
        }
        if (obj instanceof TLRPC.TL_textAnchor) {
            final TLRPC.TL_textAnchor tl_textAnchor = (TLRPC.TL_textAnchor)obj;
            final SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(this.getText(view, richText, tl_textAnchor.text, pageBlock, abs));
            spannableStringBuilder3.setSpan((Object)new AnchorSpan(tl_textAnchor.name), 0, spannableStringBuilder3.length(), 17);
            return (CharSequence)spannableStringBuilder3;
        }
        if (obj instanceof TLRPC.TL_textEmpty) {
            return "";
        }
        if (obj instanceof TLRPC.TL_textConcat) {
            final SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder();
            for (int size = obj.texts.size(), i = 0; i < size; ++i) {
                final TLRPC.RichText richText2 = obj.texts.get(i);
                final TLRPC.RichText lastRichText = this.getLastRichText(richText2);
                final boolean b = abs >= 0 && richText2 instanceof TLRPC.TL_textUrl && ((TLRPC.TL_textUrl)richText2).webpage_id != 0L;
                if (b && spannableStringBuilder4.length() != 0 && spannableStringBuilder4.charAt(spannableStringBuilder4.length() - 1) != '\n') {
                    spannableStringBuilder4.append((CharSequence)" ");
                }
                final CharSequence text = this.getText(view, richText, richText2, pageBlock, abs);
                final int textFlags = this.getTextFlags(lastRichText);
                final int length = spannableStringBuilder4.length();
                spannableStringBuilder4.append(text);
                if (textFlags != 0 && !(text instanceof SpannableStringBuilder)) {
                    if ((textFlags & 0x8) == 0x0 && (textFlags & 0x200) == 0x0) {
                        if (length != spannableStringBuilder4.length()) {
                            spannableStringBuilder4.setSpan((Object)new TextPaintSpan(this.getTextPaint(richText, lastRichText, pageBlock)), length, spannableStringBuilder4.length(), 33);
                        }
                    }
                    else {
                        String s;
                        if ((s = getUrl(richText2)) == null) {
                            s = getUrl(richText);
                        }
                        TextPaintUrlSpan textPaintUrlSpan2;
                        if ((textFlags & 0x200) != 0x0) {
                            textPaintUrlSpan2 = new TextPaintWebpageUrlSpan(this.getTextPaint(richText, lastRichText, pageBlock), s);
                        }
                        else {
                            textPaintUrlSpan2 = new TextPaintUrlSpan(this.getTextPaint(richText, lastRichText, pageBlock), s);
                        }
                        if (length != spannableStringBuilder4.length()) {
                            spannableStringBuilder4.setSpan((Object)textPaintUrlSpan2, length, spannableStringBuilder4.length(), 33);
                        }
                    }
                }
                if (b && i != size - 1) {
                    spannableStringBuilder4.append((CharSequence)" ");
                }
            }
            return (CharSequence)spannableStringBuilder4;
        }
        if (obj instanceof TLRPC.TL_textSubscript) {
            return this.getText(view, richText, ((TLRPC.TL_textSubscript)obj).text, pageBlock, abs);
        }
        if (obj instanceof TLRPC.TL_textSuperscript) {
            return this.getText(view, richText, ((TLRPC.TL_textSuperscript)obj).text, pageBlock, abs);
        }
        if (obj instanceof TLRPC.TL_textMarked) {
            final SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder(this.getText(view, richText, ((TLRPC.TL_textMarked)obj).text, pageBlock, abs));
            final MetricAffectingSpan[] array3 = (MetricAffectingSpan[])spannableStringBuilder5.getSpans(0, spannableStringBuilder5.length(), (Class)MetricAffectingSpan.class);
            if (spannableStringBuilder5.length() != 0) {
                TextPaint textPaint4;
                if (array3 != null && array3.length != 0) {
                    textPaint4 = null;
                }
                else {
                    textPaint4 = this.getTextPaint(richText, obj, pageBlock);
                }
                spannableStringBuilder5.setSpan((Object)new TextPaintMarkSpan(textPaint4), 0, spannableStringBuilder5.length(), 33);
            }
            return (CharSequence)spannableStringBuilder5;
        }
        Label_1178: {
            if (!(obj instanceof TLRPC.TL_textPhone)) {
                break Label_1178;
            }
            final TLRPC.RichText text2 = ((TLRPC.TL_textPhone)obj).text;
            try {
                final SpannableStringBuilder spannableStringBuilder6 = new SpannableStringBuilder(this.getText(view, richText, text2, pageBlock, abs));
                final MetricAffectingSpan[] array4 = (MetricAffectingSpan[])spannableStringBuilder6.getSpans(0, spannableStringBuilder6.length(), (Class)MetricAffectingSpan.class);
                if (spannableStringBuilder6.length() != 0) {
                    TextPaint textPaint5;
                    if (array4 != null && array4.length != 0) {
                        textPaint5 = null;
                    }
                    else {
                        textPaint5 = this.getTextPaint(richText, obj, pageBlock);
                    }
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("tel:");
                    sb2.append(getUrl(obj));
                    spannableStringBuilder6.setSpan((Object)new TextPaintUrlSpan(textPaint5, sb2.toString()), 0, spannableStringBuilder6.length(), 33);
                }
                return (CharSequence)spannableStringBuilder6;
                final StringBuilder sb3;
                Label_1335: {
                    sb3 = new StringBuilder();
                }
                sb3.append("not supported ");
                sb3.append(obj);
                return sb3.toString();
                // iftrue(Label_1273:, dp <= abs)
                // iftrue(Label_1331:, documentWithId == null)
                // iftrue(Label_1335:, !obj instanceof TLRPC.TL_textImage)
                SpannableStringBuilder spannableStringBuilder7 = null;
                int dp2 = 0;
                TLRPC.Document documentWithId = null;
                Label_1277: {
                    while (true) {
                        int dp;
                        while (true) {
                            spannableStringBuilder7 = new SpannableStringBuilder((CharSequence)"*");
                            final TLRPC.TL_textImage tl_textImage;
                            dp = AndroidUtilities.dp((float)tl_textImage.w);
                            dp2 = AndroidUtilities.dp((float)tl_textImage.h);
                            abs = Math.abs(abs);
                            dp2 *= (int)(abs / (float)dp);
                            break Label_1277;
                            tl_textImage = (TLRPC.TL_textImage)obj;
                            documentWithId = this.getDocumentWithId(tl_textImage.document_id);
                            continue;
                        }
                        Label_1273:
                        abs = dp;
                        break Label_1277;
                        continue;
                    }
                }
                spannableStringBuilder7.setSpan((Object)new TextPaintImageReceiverSpan(view, documentWithId, this.currentPage, abs, dp2, false, this.selectedColor == 2), 0, spannableStringBuilder7.length(), 33);
                return (CharSequence)spannableStringBuilder7;
                Label_1331:
                return "";
            }
            catch (Throwable t) {
                throw t;
            }
        }
    }
    
    private int getTextColor() {
        final int selectedColor = this.getSelectedColor();
        if (selectedColor != 0 && selectedColor != 1) {
            return -6710887;
        }
        return -14606047;
    }
    
    private int getTextFlags(TLRPC.RichText parentRichText) {
        if (parentRichText instanceof TLRPC.TL_textFixed) {
            return this.getTextFlags(parentRichText.parentRichText) | 0x4;
        }
        if (parentRichText instanceof TLRPC.TL_textItalic) {
            return this.getTextFlags(parentRichText.parentRichText) | 0x2;
        }
        if (parentRichText instanceof TLRPC.TL_textBold) {
            return this.getTextFlags(parentRichText.parentRichText) | 0x1;
        }
        if (parentRichText instanceof TLRPC.TL_textUnderline) {
            return this.getTextFlags(parentRichText.parentRichText) | 0x10;
        }
        if (parentRichText instanceof TLRPC.TL_textStrike) {
            return this.getTextFlags(parentRichText.parentRichText) | 0x20;
        }
        if (parentRichText instanceof TLRPC.TL_textEmail) {
            return this.getTextFlags(parentRichText.parentRichText) | 0x8;
        }
        if (parentRichText instanceof TLRPC.TL_textPhone) {
            return this.getTextFlags(parentRichText.parentRichText) | 0x8;
        }
        if (parentRichText instanceof TLRPC.TL_textUrl) {
            if (((TLRPC.TL_textUrl)parentRichText).webpage_id != 0L) {
                return this.getTextFlags(parentRichText.parentRichText) | 0x200;
            }
            return this.getTextFlags(parentRichText.parentRichText) | 0x8;
        }
        else {
            if (parentRichText instanceof TLRPC.TL_textSubscript) {
                return this.getTextFlags(parentRichText.parentRichText) | 0x80;
            }
            if (parentRichText instanceof TLRPC.TL_textSuperscript) {
                return this.getTextFlags(parentRichText.parentRichText) | 0x100;
            }
            if (parentRichText instanceof TLRPC.TL_textMarked) {
                return this.getTextFlags(parentRichText.parentRichText) | 0x40;
            }
            if (parentRichText == null) {
                return 0;
            }
            parentRichText = parentRichText.parentRichText;
            try {
                return this.getTextFlags(parentRichText);
            }
            catch (Throwable t) {
                throw t;
            }
        }
    }
    
    private TextPaint getTextPaint(final TLRPC.RichText richText, final TLRPC.RichText richText2, final TLRPC.PageBlock pageBlock) {
        final int textFlags = this.getTextFlags(richText2);
        int n = AndroidUtilities.dp(14.0f);
        final int selectedFontSize = this.selectedFontSize;
        int n3 = 0;
        Label_0092: {
            int n2;
            if (selectedFontSize == 0) {
                n2 = AndroidUtilities.dp(4.0f);
            }
            else if (selectedFontSize == 1) {
                n2 = AndroidUtilities.dp(2.0f);
            }
            else {
                if (selectedFontSize == 3) {
                    n3 = AndroidUtilities.dp(2.0f);
                    break Label_0092;
                }
                if (selectedFontSize == 4) {
                    n3 = AndroidUtilities.dp(4.0f);
                    break Label_0092;
                }
                n3 = 0;
                break Label_0092;
            }
            n3 = -n2;
        }
        final boolean b = pageBlock instanceof TLRPC.TL_pageBlockPhoto;
        final SparseArray sparseArray = null;
        SparseArray sparseArray2 = null;
        int color = 0;
        Label_0272: {
            if (b) {
                final TLRPC.RichText text = ((TLRPC.TL_pageBlockPhoto)pageBlock).caption.text;
                int n4;
                if (text != richText2 && text != richText) {
                    sparseArray2 = ArticleViewer.photoCreditTextPaints;
                    n4 = AndroidUtilities.dp(12.0f);
                }
                else {
                    sparseArray2 = ArticleViewer.photoCaptionTextPaints;
                    n4 = AndroidUtilities.dp(14.0f);
                }
                n = n4;
                color = this.getGrayTextColor();
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockMap) {
                final TLRPC.RichText text2 = ((TLRPC.TL_pageBlockMap)pageBlock).caption.text;
                int n5;
                if (text2 != richText2 && text2 != richText) {
                    sparseArray2 = ArticleViewer.photoCreditTextPaints;
                    n5 = AndroidUtilities.dp(12.0f);
                }
                else {
                    sparseArray2 = ArticleViewer.photoCaptionTextPaints;
                    n5 = AndroidUtilities.dp(14.0f);
                }
                n = n5;
                color = this.getGrayTextColor();
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockTitle) {
                sparseArray2 = ArticleViewer.titleTextPaints;
                n = AndroidUtilities.dp(24.0f);
                color = this.getTextColor();
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockKicker) {
                sparseArray2 = ArticleViewer.kickerTextPaints;
                n = AndroidUtilities.dp(14.0f);
                color = this.getTextColor();
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockAuthorDate) {
                sparseArray2 = ArticleViewer.authorTextPaints;
                n = AndroidUtilities.dp(14.0f);
                color = this.getGrayTextColor();
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockFooter) {
                sparseArray2 = ArticleViewer.footerTextPaints;
                n = AndroidUtilities.dp(14.0f);
                color = this.getGrayTextColor();
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockSubtitle) {
                sparseArray2 = ArticleViewer.subtitleTextPaints;
                n = AndroidUtilities.dp(21.0f);
                color = this.getTextColor();
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockHeader) {
                sparseArray2 = ArticleViewer.headerTextPaints;
                n = AndroidUtilities.dp(21.0f);
                color = this.getTextColor();
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockSubheader) {
                sparseArray2 = ArticleViewer.subheaderTextPaints;
                n = AndroidUtilities.dp(18.0f);
                color = this.getTextColor();
            }
            else {
                if (pageBlock instanceof TLRPC.TL_pageBlockBlockquote) {
                    final TLRPC.TL_pageBlockBlockquote tl_pageBlockBlockquote = (TLRPC.TL_pageBlockBlockquote)pageBlock;
                    if (tl_pageBlockBlockquote.text == richText) {
                        sparseArray2 = ArticleViewer.quoteTextPaints;
                        n = AndroidUtilities.dp(15.0f);
                        color = this.getTextColor();
                        break Label_0272;
                    }
                    if (tl_pageBlockBlockquote.caption == richText) {
                        sparseArray2 = ArticleViewer.photoCaptionTextPaints;
                        n = AndroidUtilities.dp(14.0f);
                        color = this.getGrayTextColor();
                        break Label_0272;
                    }
                }
                else if (pageBlock instanceof TLRPC.TL_pageBlockPullquote) {
                    final TLRPC.TL_pageBlockPullquote tl_pageBlockPullquote = (TLRPC.TL_pageBlockPullquote)pageBlock;
                    if (tl_pageBlockPullquote.text == richText) {
                        sparseArray2 = ArticleViewer.quoteTextPaints;
                        n = AndroidUtilities.dp(15.0f);
                        color = this.getTextColor();
                        break Label_0272;
                    }
                    if (tl_pageBlockPullquote.caption == richText) {
                        sparseArray2 = ArticleViewer.photoCaptionTextPaints;
                        n = AndroidUtilities.dp(14.0f);
                        color = this.getGrayTextColor();
                        break Label_0272;
                    }
                }
                else {
                    if (pageBlock instanceof TLRPC.TL_pageBlockPreformatted) {
                        sparseArray2 = ArticleViewer.preformattedTextPaints;
                        n = AndroidUtilities.dp(14.0f);
                        color = this.getTextColor();
                        break Label_0272;
                    }
                    if (pageBlock instanceof TLRPC.TL_pageBlockParagraph) {
                        sparseArray2 = ArticleViewer.paragraphTextPaints;
                        n = AndroidUtilities.dp(16.0f);
                        color = this.getTextColor();
                        break Label_0272;
                    }
                    if (this.isListItemBlock(pageBlock)) {
                        sparseArray2 = ArticleViewer.listTextPaints;
                        n = AndroidUtilities.dp(16.0f);
                        color = this.getTextColor();
                        break Label_0272;
                    }
                    if (pageBlock instanceof TLRPC.TL_pageBlockEmbed) {
                        final TLRPC.RichText text3 = ((TLRPC.TL_pageBlockEmbed)pageBlock).caption.text;
                        int n6;
                        if (text3 != richText2 && text3 != richText) {
                            sparseArray2 = ArticleViewer.photoCreditTextPaints;
                            n6 = AndroidUtilities.dp(12.0f);
                        }
                        else {
                            sparseArray2 = ArticleViewer.photoCaptionTextPaints;
                            n6 = AndroidUtilities.dp(14.0f);
                        }
                        n = n6;
                        color = this.getGrayTextColor();
                        break Label_0272;
                    }
                    if (pageBlock instanceof TLRPC.TL_pageBlockSlideshow) {
                        final TLRPC.RichText text4 = ((TLRPC.TL_pageBlockSlideshow)pageBlock).caption.text;
                        int n7;
                        if (text4 != richText2 && text4 != richText) {
                            sparseArray2 = ArticleViewer.photoCreditTextPaints;
                            n7 = AndroidUtilities.dp(12.0f);
                        }
                        else {
                            sparseArray2 = ArticleViewer.photoCaptionTextPaints;
                            n7 = AndroidUtilities.dp(14.0f);
                        }
                        n = n7;
                        color = this.getGrayTextColor();
                        break Label_0272;
                    }
                    if (pageBlock instanceof TLRPC.TL_pageBlockCollage) {
                        final TLRPC.RichText text5 = ((TLRPC.TL_pageBlockCollage)pageBlock).caption.text;
                        int n8;
                        if (text5 != richText2 && text5 != richText) {
                            sparseArray2 = ArticleViewer.photoCreditTextPaints;
                            n8 = AndroidUtilities.dp(12.0f);
                        }
                        else {
                            sparseArray2 = ArticleViewer.photoCaptionTextPaints;
                            n8 = AndroidUtilities.dp(14.0f);
                        }
                        n = n8;
                        color = this.getGrayTextColor();
                        break Label_0272;
                    }
                    if (!(pageBlock instanceof TLRPC.TL_pageBlockEmbedPost)) {
                        int n9;
                        int n10;
                        if (pageBlock instanceof TLRPC.TL_pageBlockVideo) {
                            if (richText2 == ((TLRPC.TL_pageBlockVideo)pageBlock).caption.text) {
                                sparseArray2 = ArticleViewer.mediaCaptionTextPaints;
                                n9 = AndroidUtilities.dp(14.0f);
                                n10 = this.getTextColor();
                            }
                            else {
                                sparseArray2 = ArticleViewer.mediaCreditTextPaints;
                                n9 = AndroidUtilities.dp(12.0f);
                                n10 = this.getTextColor();
                            }
                        }
                        else if (pageBlock instanceof TLRPC.TL_pageBlockAudio) {
                            if (richText2 == ((TLRPC.TL_pageBlockAudio)pageBlock).caption.text) {
                                sparseArray2 = ArticleViewer.mediaCaptionTextPaints;
                                n9 = AndroidUtilities.dp(14.0f);
                                n10 = this.getTextColor();
                            }
                            else {
                                sparseArray2 = ArticleViewer.mediaCreditTextPaints;
                                n9 = AndroidUtilities.dp(12.0f);
                                n10 = this.getTextColor();
                            }
                        }
                        else {
                            if (pageBlock instanceof TLRPC.TL_pageBlockRelatedArticles) {
                                sparseArray2 = ArticleViewer.relatedArticleTextPaints;
                                n = AndroidUtilities.dp(15.0f);
                                color = this.getGrayTextColor();
                                break Label_0272;
                            }
                            if (pageBlock instanceof TLRPC.TL_pageBlockDetails) {
                                sparseArray2 = ArticleViewer.detailsTextPaints;
                                n = AndroidUtilities.dp(15.0f);
                                color = this.getTextColor();
                                break Label_0272;
                            }
                            if (pageBlock instanceof TLRPC.TL_pageBlockTable) {
                                sparseArray2 = ArticleViewer.tableTextPaints;
                                n = AndroidUtilities.dp(15.0f);
                                color = this.getTextColor();
                                break Label_0272;
                            }
                            sparseArray2 = null;
                            color = -65536;
                            break Label_0272;
                        }
                        final int n11 = n9;
                        color = n10;
                        n = n11;
                        break Label_0272;
                    }
                    final TLRPC.TL_pageCaption caption = ((TLRPC.TL_pageBlockEmbedPost)pageBlock).caption;
                    if (richText2 == caption.text) {
                        sparseArray2 = ArticleViewer.photoCaptionTextPaints;
                        n = AndroidUtilities.dp(14.0f);
                        color = this.getGrayTextColor();
                        break Label_0272;
                    }
                    if (richText2 == caption.credit) {
                        sparseArray2 = ArticleViewer.photoCreditTextPaints;
                        n = AndroidUtilities.dp(12.0f);
                        color = this.getGrayTextColor();
                        break Label_0272;
                    }
                    if (richText2 != null) {
                        sparseArray2 = ArticleViewer.embedPostTextPaints;
                        n = AndroidUtilities.dp(14.0f);
                        color = this.getTextColor();
                        break Label_0272;
                    }
                }
                color = -65536;
                sparseArray2 = sparseArray;
            }
        }
        final int n12 = textFlags & 0x100;
        int n13 = 0;
        Label_1265: {
            if (n12 == 0) {
                n13 = n;
                if ((textFlags & 0x80) == 0x0) {
                    break Label_1265;
                }
            }
            n13 = n - AndroidUtilities.dp(4.0f);
        }
        if (sparseArray2 == null) {
            if (ArticleViewer.errorTextPaint == null) {
                (ArticleViewer.errorTextPaint = new TextPaint(1)).setColor(-65536);
            }
            ArticleViewer.errorTextPaint.setTextSize((float)AndroidUtilities.dp(14.0f));
            return ArticleViewer.errorTextPaint;
        }
        TextPaint textPaint;
        if ((textPaint = (TextPaint)sparseArray2.get(textFlags)) == null) {
            textPaint = new TextPaint(1);
            if ((textFlags & 0x4) != 0x0) {
                textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmono.ttf"));
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockRelatedArticles) {
                textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            }
            else if (this.selectedFont != 1 && !(pageBlock instanceof TLRPC.TL_pageBlockTitle) && !(pageBlock instanceof TLRPC.TL_pageBlockKicker) && !(pageBlock instanceof TLRPC.TL_pageBlockHeader) && !(pageBlock instanceof TLRPC.TL_pageBlockSubtitle) && !(pageBlock instanceof TLRPC.TL_pageBlockSubheader)) {
                final int n14 = textFlags & 0x1;
                if (n14 != 0 && (textFlags & 0x2) != 0x0) {
                    textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmediumitalic.ttf"));
                }
                else if (n14 != 0) {
                    textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                }
                else if ((textFlags & 0x2) != 0x0) {
                    textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/ritalic.ttf"));
                }
            }
            else {
                final int n15 = textFlags & 0x1;
                if (n15 != 0 && (textFlags & 0x2) != 0x0) {
                    textPaint.setTypeface(Typeface.create("serif", 3));
                }
                else if (n15 != 0) {
                    textPaint.setTypeface(Typeface.create("serif", 1));
                }
                else if ((textFlags & 0x2) != 0x0) {
                    textPaint.setTypeface(Typeface.create("serif", 2));
                }
                else {
                    textPaint.setTypeface(Typeface.create("serif", 0));
                }
            }
            if ((textFlags & 0x20) != 0x0) {
                textPaint.setFlags(textPaint.getFlags() | 0x10);
            }
            if ((textFlags & 0x10) != 0x0) {
                textPaint.setFlags(textPaint.getFlags() | 0x8);
            }
            if ((textFlags & 0x8) != 0x0 || (textFlags & 0x200) != 0x0) {
                textPaint.setFlags(textPaint.getFlags());
                color = this.getLinkTextColor();
            }
            if (n12 != 0) {
                textPaint.baselineShift -= AndroidUtilities.dp(6.0f);
            }
            else if ((textFlags & 0x80) != 0x0) {
                textPaint.baselineShift += AndroidUtilities.dp(2.0f);
            }
            textPaint.setColor(color);
            sparseArray2.put(textFlags, (Object)textPaint);
        }
        textPaint.setTextSize((float)(n13 + n3));
        return textPaint;
    }
    
    public static String getUrl(TLRPC.RichText text) {
        if (text instanceof TLRPC.TL_textFixed) {
            return getUrl(((TLRPC.TL_textFixed)text).text);
        }
        if (text instanceof TLRPC.TL_textItalic) {
            return getUrl(((TLRPC.TL_textItalic)text).text);
        }
        if (text instanceof TLRPC.TL_textBold) {
            return getUrl(((TLRPC.TL_textBold)text).text);
        }
        if (text instanceof TLRPC.TL_textUnderline) {
            return getUrl(((TLRPC.TL_textUnderline)text).text);
        }
        Label_0094: {
            if (!(text instanceof TLRPC.TL_textStrike)) {
                break Label_0094;
            }
            text = ((TLRPC.TL_textStrike)text).text;
            try {
                return getUrl(text);
                Label_0124: {
                    return ((TLRPC.TL_textPhone)text).phone;
                }
                // iftrue(Label_0139:, !text instanceof TLRPC.TL_textPhone)
                Label_0109: {
                    return ((TLRPC.TL_textUrl)text).url;
                }
                // iftrue(Label_0124:, !text instanceof TLRPC.TL_textUrl)
                Label_0139: {
                    return null;
                }
                // iftrue(Label_0109:, !text instanceof TLRPC.TL_textEmail)
                return ((TLRPC.TL_textEmail)text).email;
            }
            catch (Throwable t) {
                throw t;
            }
        }
    }
    
    private void goToNext() {
        float n;
        if (this.scale != 1.0f) {
            n = (this.getContainerViewWidth() - this.centerImage.getImageWidth()) / 2 * this.scale;
        }
        else {
            n = 0.0f;
        }
        this.switchImageAfterAnimation = 1;
        this.animateTo(this.scale, this.minX - this.getContainerViewWidth() - n - AndroidUtilities.dp(30.0f) / 2, this.translationY, false);
    }
    
    private void goToPrev() {
        float n;
        if (this.scale != 1.0f) {
            n = (this.getContainerViewWidth() - this.centerImage.getImageWidth()) / 2 * this.scale;
        }
        else {
            n = 0.0f;
        }
        this.switchImageAfterAnimation = 2;
        this.animateTo(this.scale, this.maxX + this.getContainerViewWidth() + n + AndroidUtilities.dp(30.0f) / 2, this.translationY, false);
    }
    
    public static boolean hasInstance() {
        return ArticleViewer.Instance != null;
    }
    
    private boolean isListItemBlock(final TLRPC.PageBlock pageBlock) {
        return pageBlock instanceof TL_pageBlockListItem || pageBlock instanceof TL_pageBlockOrderedListItem;
    }
    
    private boolean isMediaVideo(final int index) {
        return !this.imagesArr.isEmpty() && index < this.imagesArr.size() && index >= 0 && this.isVideoBlock(this.imagesArr.get(index));
    }
    
    private boolean isVideoBlock(final TLRPC.PageBlock pageBlock) {
        if (pageBlock instanceof TLRPC.TL_pageBlockVideo) {
            final TLRPC.Document documentWithId = this.getDocumentWithId(((TLRPC.TL_pageBlockVideo)pageBlock).video_id);
            if (documentWithId != null) {
                return MessageObject.isVideoDocument(documentWithId);
            }
        }
        return false;
    }
    
    private void joinChannel(final BlockChannelCell blockChannelCell, final TLRPC.Chat chat) {
        final TLRPC.TL_channels_joinChannel tl_channels_joinChannel = new TLRPC.TL_channels_joinChannel();
        tl_channels_joinChannel.channel = MessagesController.getInputChannel(chat);
        final int selectedAccount = UserConfig.selectedAccount;
        ConnectionsManager.getInstance(selectedAccount).sendRequest(tl_channels_joinChannel, new _$$Lambda$ArticleViewer$tOg7TGz_CemIZKoQ4SmkPVL_N7w(this, blockChannelCell, selectedAccount, tl_channels_joinChannel, chat));
    }
    
    private void loadChannel(final BlockChannelCell blockChannelCell, final WebpageAdapter webpageAdapter, final TLRPC.Chat chat) {
        if (!this.loadingChannel) {
            if (!TextUtils.isEmpty((CharSequence)chat.username)) {
                this.loadingChannel = true;
                final TLRPC.TL_contacts_resolveUsername tl_contacts_resolveUsername = new TLRPC.TL_contacts_resolveUsername();
                tl_contacts_resolveUsername.username = chat.username;
                final int selectedAccount = UserConfig.selectedAccount;
                ConnectionsManager.getInstance(selectedAccount).sendRequest(tl_contacts_resolveUsername, new _$$Lambda$ArticleViewer$OTnRhboivaBmKveLaMpnewhsYJg(this, webpageAdapter, selectedAccount, blockChannelCell));
            }
        }
    }
    
    private void onActionClick(final boolean b) {
        final TLObject media = this.getMedia(this.currentIndex);
        if (media instanceof TLRPC.Document) {
            if (this.currentFileNames[0] != null) {
                final TLRPC.Document document = (TLRPC.Document)media;
                final TLRPC.PageBlock currentMedia = this.currentMedia;
                File mediaFile;
                final File file = mediaFile = null;
                if (currentMedia != null) {
                    mediaFile = this.getMediaFile(this.currentIndex);
                    if (mediaFile != null && !mediaFile.exists()) {
                        mediaFile = file;
                    }
                }
                if (mediaFile == null) {
                    if (b) {
                        if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[0])) {
                            FileLoader.getInstance(this.currentAccount).loadFile(document, this.currentPage, 1, 1);
                        }
                        else {
                            FileLoader.getInstance(this.currentAccount).cancelLoadFile(document);
                        }
                    }
                }
                else {
                    this.preparePlayer(mediaFile, true);
                }
            }
        }
    }
    
    private void onClosed() {
        this.isVisible = false;
        this.currentPage = null;
        for (int i = 0; i < this.listView.length; ++i) {
            this.adapter[i].cleanup();
        }
        try {
            this.parentActivity.getWindow().clearFlags(128);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        for (int j = 0; j < this.createdWebViews.size(); ++j) {
            this.createdWebViews.get(j).destroyWebView(false);
        }
        this.containerView.post((Runnable)new _$$Lambda$ArticleViewer$7Sl00UfgZjsIK9QPR7pwpUGqEew(this));
    }
    
    private void onPhotoClosed(final PlaceProviderObject placeProviderObject) {
        this.isPhotoVisible = false;
        this.disableShowCheck = true;
        this.currentMedia = null;
        final ImageReceiver.BitmapHolder currentThumb = this.currentThumb;
        if (currentThumb != null) {
            currentThumb.release();
            this.currentThumb = null;
        }
        final AnimatedFileDrawable currentAnimation = this.currentAnimation;
        if (currentAnimation != null) {
            currentAnimation.setSecondParentView(null);
            this.currentAnimation = null;
        }
        for (int i = 0; i < 3; ++i) {
            final RadialProgressView[] radialProgressViews = this.radialProgressViews;
            if (radialProgressViews[i] != null) {
                radialProgressViews[i].setBackgroundState(-1, false);
            }
        }
        this.centerImage.setImageBitmap((Bitmap)null);
        this.leftImage.setImageBitmap((Bitmap)null);
        this.rightImage.setImageBitmap((Bitmap)null);
        this.photoContainerView.post((Runnable)new _$$Lambda$ArticleViewer$RbiUQ9E_b8OvcTg7QvMaKqKT5rU(this));
        this.disableShowCheck = false;
        if (placeProviderObject != null) {
            placeProviderObject.imageReceiver.setVisible(true, true);
        }
        this.groupedPhotosListView.clear();
    }
    
    private void onPhotoShow(final int n, final PlaceProviderObject placeProviderObject) {
        this.currentIndex = -1;
        final String[] currentFileNames = this.currentFileNames;
        currentFileNames[0] = null;
        currentFileNames[2] = (currentFileNames[1] = null);
        final ImageReceiver.BitmapHolder currentThumb = this.currentThumb;
        if (currentThumb != null) {
            currentThumb.release();
        }
        ImageReceiver.BitmapHolder thumb;
        if (placeProviderObject != null) {
            thumb = placeProviderObject.thumb;
        }
        else {
            thumb = null;
        }
        this.currentThumb = thumb;
        this.menuItem.setVisibility(0);
        this.menuItem.hideSubItem(3);
        this.actionBar.setTranslationY(0.0f);
        this.captionTextView.setTag((Object)null);
        this.captionTextView.setVisibility(8);
        for (int i = 0; i < 3; ++i) {
            final RadialProgressView[] radialProgressViews = this.radialProgressViews;
            if (radialProgressViews[i] != null) {
                radialProgressViews[i].setBackgroundState(-1, false);
            }
        }
        this.setImageIndex(n, true);
        if (this.currentMedia != null && this.isMediaVideo(this.currentIndex)) {
            this.onActionClick(false);
        }
    }
    
    private void onSharePressed() {
        if (this.parentActivity != null) {
            if (this.currentMedia != null) {
                try {
                    final File mediaFile = this.getMediaFile(this.currentIndex);
                    if (mediaFile != null && mediaFile.exists()) {
                        final Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType(this.getMediaMime(this.currentIndex));
                        if (Build$VERSION.SDK_INT >= 24) {
                            try {
                                intent.putExtra("android.intent.extra.STREAM", (Parcelable)FileProvider.getUriForFile((Context)this.parentActivity, "org.telegram.messenger.provider", mediaFile));
                                intent.setFlags(1);
                            }
                            catch (Exception ex2) {
                                intent.putExtra("android.intent.extra.STREAM", (Parcelable)Uri.fromFile(mediaFile));
                            }
                        }
                        else {
                            intent.putExtra("android.intent.extra.STREAM", (Parcelable)Uri.fromFile(mediaFile));
                        }
                        this.parentActivity.startActivityForResult(Intent.createChooser(intent, (CharSequence)LocaleController.getString("ShareFile", 2131560748)), 500);
                    }
                    else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.parentActivity);
                        builder.setTitle(LocaleController.getString("AppName", 2131558635));
                        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                        builder.setMessage(LocaleController.getString("PleaseDownload", 2131560454));
                        this.showDialog(builder.create());
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
    }
    
    private boolean open(MessageObject windowLayoutParams, final TLRPC.WebPage webPage, String lowerCase, final boolean b) {
        if (this.parentActivity == null || (this.isVisible && !this.collapsed) || (windowLayoutParams == null && webPage == null)) {
            return false;
        }
        TLRPC.WebPage webPage2 = webPage;
        if (windowLayoutParams != null) {
            webPage2 = ((MessageObject)windowLayoutParams).messageOwner.media.webpage;
        }
        String s = null;
        TLRPC.WebPage webPage3 = null;
        Label_0269: {
            Label_0258: {
                if (windowLayoutParams == null) {
                    if (lowerCase != null) {
                        final int lastIndex = lowerCase.lastIndexOf(35);
                        if (lastIndex != -1) {
                            s = lowerCase.substring(lastIndex + 1);
                            break Label_0258;
                        }
                    }
                    webPage3 = webPage2;
                    s = null;
                    break Label_0269;
                }
                webPage2 = ((MessageObject)windowLayoutParams).messageOwner.media.webpage;
                for (int i = 0; i < ((MessageObject)windowLayoutParams).messageOwner.entities.size(); ++i) {
                    final TLRPC.MessageEntity messageEntity = ((MessageObject)windowLayoutParams).messageOwner.entities.get(i);
                    if (messageEntity instanceof TLRPC.TL_messageEntityUrl) {
                        try {
                            lowerCase = ((MessageObject)windowLayoutParams).messageOwner.message.substring(messageEntity.offset, messageEntity.offset + messageEntity.length).toLowerCase();
                            String s2;
                            if (!TextUtils.isEmpty((CharSequence)webPage2.cached_page.url)) {
                                s2 = webPage2.cached_page.url.toLowerCase();
                            }
                            else {
                                s2 = webPage2.url.toLowerCase();
                            }
                            if (lowerCase.contains(s2) || s2.contains(lowerCase)) {
                                final int lastIndex2 = lowerCase.lastIndexOf(35);
                                if (lastIndex2 != -1) {
                                    s = lowerCase.substring(lastIndex2 + 1);
                                    break Label_0258;
                                }
                                break;
                            }
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                }
                s = null;
            }
            webPage3 = webPage2;
        }
        this.pagesStack.clear();
        this.collapsed = false;
        this.backDrawable.setRotation(0.0f, false);
        this.containerView.setTranslationX(0.0f);
        this.containerView.setTranslationY(0.0f);
        this.listView[0].setTranslationY(0.0f);
        this.listView[0].setTranslationX(0.0f);
        this.listView[1].setTranslationX(0.0f);
        this.listView[0].setAlpha(1.0f);
        this.windowView.setInnerTranslationX(0.0f);
        this.actionBar.setVisibility(8);
        this.bottomLayout.setVisibility(8);
        this.captionTextView.setVisibility(8);
        this.captionTextViewNext.setVisibility(8);
        this.layoutManager[0].scrollToPositionWithOffset(0, 0);
        if (b) {
            this.setCurrentHeaderHeight(AndroidUtilities.dp(56.0f));
        }
        else {
            this.checkScrollAnimated();
        }
        final boolean addPageToStack = this.addPageToStack(webPage3, s, 0);
        if (b) {
            if (addPageToStack || s == null) {
                s = null;
            }
            final TLRPC.TL_messages_getWebPage tl_messages_getWebPage = new TLRPC.TL_messages_getWebPage();
            tl_messages_getWebPage.url = webPage3.url;
            final TLRPC.Page cached_page = webPage3.cached_page;
            if (!(cached_page instanceof TLRPC.TL_pagePart_layer82) && !cached_page.part) {
                tl_messages_getWebPage.hash = webPage3.hash;
            }
            else {
                tl_messages_getWebPage.hash = 0;
            }
            final int selectedAccount = UserConfig.selectedAccount;
            ConnectionsManager.getInstance(selectedAccount).sendRequest(tl_messages_getWebPage, new _$$Lambda$ArticleViewer$z8m1_SXnv_7lkJNDb6qlQTHMjFc(this, webPage3, (MessageObject)windowLayoutParams, s, selectedAccount));
        }
        this.lastInsets = null;
        Label_0675: {
            if (this.isVisible) {
                break Label_0675;
            }
            windowLayoutParams = (Exception)this.parentActivity.getSystemService("window");
            while (true) {
                if (!this.attachedToWindow) {
                    break Label_0584;
                }
                try {
                    ((WindowManager)windowLayoutParams).removeView((View)this.windowView);
                    Label_0717: {
                        try {
                            if (Build$VERSION.SDK_INT >= 21) {
                                this.windowLayoutParams.flags = -2147417856;
                                if (Build$VERSION.SDK_INT >= 28) {
                                    this.windowLayoutParams.layoutInDisplayCutoutMode = 1;
                                }
                            }
                            final WindowManager$LayoutParams windowLayoutParams2 = this.windowLayoutParams;
                            windowLayoutParams2.flags |= 0x408;
                            this.windowView.setFocusable(false);
                            this.containerView.setFocusable(false);
                            ((WindowManager)windowLayoutParams).addView((View)this.windowView, (ViewGroup$LayoutParams)this.windowLayoutParams);
                            break Label_0717;
                        }
                        catch (Exception windowLayoutParams) {
                            FileLog.e(windowLayoutParams);
                            return false;
                        }
                        windowLayoutParams = (Exception)this.windowLayoutParams;
                        ((WindowManager$LayoutParams)windowLayoutParams).flags &= 0xFFFFFFEF;
                        ((WindowManager)this.parentActivity.getSystemService("window")).updateViewLayout((View)this.windowView, (ViewGroup$LayoutParams)this.windowLayoutParams);
                    }
                    this.isVisible = true;
                    this.animationInProgress = 1;
                    this.windowView.setAlpha(0.0f);
                    this.containerView.setAlpha(0.0f);
                    windowLayoutParams = (Exception)new AnimatorSet();
                    ((AnimatorSet)windowLayoutParams).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.windowView, View.ALPHA, new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.containerView, View.ALPHA, new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.windowView, View.TRANSLATION_X, new float[] { (float)AndroidUtilities.dp(56.0f), 0.0f }) });
                    this.animationEndRunnable = new _$$Lambda$ArticleViewer$DFl_wS8sRaC6wF2T9h1iNHqI_KI(this);
                    ((AnimatorSet)windowLayoutParams).setDuration(150L);
                    ((AnimatorSet)windowLayoutParams).setInterpolator((TimeInterpolator)this.interpolator);
                    ((AnimatorSet)windowLayoutParams).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$13$45D0IaozkG_I_gCXpiUTnqoe_0k(this));
                        }
                    });
                    this.transitionAnimationStartTime = System.currentTimeMillis();
                    AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$AbuoJEBmR86qlkjB83QNafOTaB8(this, (AnimatorSet)windowLayoutParams));
                    if (Build$VERSION.SDK_INT >= 18) {
                        this.containerView.setLayerType(2, (Paint)null);
                    }
                    return true;
                }
                catch (Exception ex2) {
                    continue;
                }
                break;
            }
        }
    }
    
    private boolean openAllParentBlocks(TL_pageBlockDetailsChild tl_pageBlockDetailsChild) {
        final TLRPC.PageBlock lastNonListPageBlock = this.getLastNonListPageBlock(tl_pageBlockDetailsChild.parent);
        final boolean b = lastNonListPageBlock instanceof TLRPC.TL_pageBlockDetails;
        final boolean b2 = false;
        if (b) {
            final TLRPC.TL_pageBlockDetails tl_pageBlockDetails = (TLRPC.TL_pageBlockDetails)lastNonListPageBlock;
            return !tl_pageBlockDetails.open && (tl_pageBlockDetails.open = true);
        }
        boolean b3 = b2;
        if (lastNonListPageBlock instanceof TL_pageBlockDetailsChild) {
            tl_pageBlockDetailsChild = (TL_pageBlockDetailsChild)lastNonListPageBlock;
            final TLRPC.PageBlock lastNonListPageBlock2 = this.getLastNonListPageBlock(tl_pageBlockDetailsChild.block);
            boolean b4 = false;
            Label_0103: {
                if (lastNonListPageBlock2 instanceof TLRPC.TL_pageBlockDetails) {
                    final TLRPC.TL_pageBlockDetails tl_pageBlockDetails2 = (TLRPC.TL_pageBlockDetails)lastNonListPageBlock2;
                    if (!tl_pageBlockDetails2.open) {
                        tl_pageBlockDetails2.open = true;
                        b4 = true;
                        break Label_0103;
                    }
                }
                b4 = false;
            }
            if (!this.openAllParentBlocks(tl_pageBlockDetailsChild)) {
                b3 = b2;
                if (!b4) {
                    return b3;
                }
            }
            b3 = true;
        }
        return b3;
    }
    
    private void openPreviewsChat(final TLRPC.User user, final long lng) {
        if (user != null) {
            if (this.parentActivity != null) {
                final Bundle bundle = new Bundle();
                bundle.putInt("user_id", user.id);
                final StringBuilder sb = new StringBuilder();
                sb.append("webpage");
                sb.append(lng);
                bundle.putString("botUser", sb.toString());
                ((LaunchActivity)this.parentActivity).presentFragment(new ChatActivity(bundle), false, true);
                this.close(false, true);
            }
        }
    }
    
    private void openWebpageUrl(final String url, final String s) {
        if (this.openUrlReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.openUrlReqId, false);
            this.openUrlReqId = 0;
        }
        final int lastReqId = this.lastReqId + 1;
        this.lastReqId = lastReqId;
        this.closePhoto(false);
        this.showProgressView(true, true);
        final TLRPC.TL_messages_getWebPage tl_messages_getWebPage = new TLRPC.TL_messages_getWebPage();
        tl_messages_getWebPage.url = url;
        tl_messages_getWebPage.hash = 0;
        this.openUrlReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getWebPage, new _$$Lambda$ArticleViewer$CIJ3NzkK6eZMINaSMugn6BEySDI(this, lastReqId, s, tl_messages_getWebPage));
    }
    
    @SuppressLint({ "NewApi" })
    private void preparePlayer(final File file, final boolean playWhenReady) {
        if (this.parentActivity == null) {
            return;
        }
        this.releasePlayer();
        if (this.videoTextureView == null) {
            (this.aspectRatioFrameLayout = new AspectRatioFrameLayout((Context)this.parentActivity)).setVisibility(4);
            this.photoContainerView.addView((View)this.aspectRatioFrameLayout, 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 17));
            (this.videoTextureView = new TextureView((Context)this.parentActivity)).setOpaque(false);
            this.aspectRatioFrameLayout.addView((View)this.videoTextureView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 17));
        }
        this.textureUploaded = false;
        this.videoCrossfadeStarted = false;
        this.videoTextureView.setAlpha(this.videoCrossfadeAlpha = 0.0f);
        this.videoPlayButton.setImageResource(2131165479);
        if (this.videoPlayer == null) {
            (this.videoPlayer = new VideoPlayer()).setTextureView(this.videoTextureView);
            this.videoPlayer.setDelegate((VideoPlayer.VideoPlayerDelegate)new VideoPlayer.VideoPlayerDelegate() {
                @Override
                public void onError(final Exception ex) {
                    FileLog.e(ex);
                }
                
                @Override
                public void onRenderedFirstFrame() {
                    if (!ArticleViewer.this.textureUploaded) {
                        ArticleViewer.this.textureUploaded = true;
                        ArticleViewer.this.containerView.invalidate();
                    }
                }
                
                @Override
                public void onStateChanged(final boolean b, final int n) {
                    if (ArticleViewer.this.videoPlayer == null) {
                        return;
                    }
                    if (n != 4 && n != 1) {
                        try {
                            ArticleViewer.this.parentActivity.getWindow().addFlags(128);
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                    else {
                        try {
                            ArticleViewer.this.parentActivity.getWindow().clearFlags(128);
                        }
                        catch (Exception ex2) {
                            FileLog.e(ex2);
                        }
                    }
                    if (n == 3 && ArticleViewer.this.aspectRatioFrameLayout.getVisibility() != 0) {
                        ArticleViewer.this.aspectRatioFrameLayout.setVisibility(0);
                    }
                    if (ArticleViewer.this.videoPlayer.isPlaying() && n != 4) {
                        if (!ArticleViewer.this.isPlaying) {
                            ArticleViewer.this.isPlaying = true;
                            ArticleViewer.this.videoPlayButton.setImageResource(2131165478);
                            AndroidUtilities.runOnUIThread(ArticleViewer.this.updateProgressRunnable);
                        }
                    }
                    else if (ArticleViewer.this.isPlaying) {
                        ArticleViewer.this.isPlaying = false;
                        ArticleViewer.this.videoPlayButton.setImageResource(2131165479);
                        AndroidUtilities.cancelRunOnUIThread(ArticleViewer.this.updateProgressRunnable);
                        if (n == 4 && !ArticleViewer.this.videoPlayerSeekbar.isDragging()) {
                            ArticleViewer.this.videoPlayerSeekbar.setProgress(0.0f);
                            ArticleViewer.this.videoPlayerControlFrameLayout.invalidate();
                            ArticleViewer.this.videoPlayer.seekTo(0L);
                            ArticleViewer.this.videoPlayer.pause();
                        }
                    }
                    ArticleViewer.this.updateVideoPlayerTime();
                }
                
                @Override
                public boolean onSurfaceDestroyed(final SurfaceTexture surfaceTexture) {
                    return false;
                }
                
                @Override
                public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
                }
                
                @Override
                public void onVideoSizeChanged(final int n, final int n2, final int n3, float n4) {
                    if (ArticleViewer.this.aspectRatioFrameLayout != null) {
                        int n5 = n;
                        int n6 = n2;
                        if (n3 != 90) {
                            if (n3 == 270) {
                                n5 = n;
                                n6 = n2;
                            }
                            else {
                                n6 = n;
                                n5 = n2;
                            }
                        }
                        final AspectRatioFrameLayout access$900 = ArticleViewer.this.aspectRatioFrameLayout;
                        if (n5 == 0) {
                            n4 = 1.0f;
                        }
                        else {
                            n4 = n6 * n4 / n5;
                        }
                        access$900.setAspectRatio(n4, n3);
                    }
                }
            });
            final VideoPlayer videoPlayer = this.videoPlayer;
            long duration;
            final long n = duration = 0L;
            if (videoPlayer != null) {
                duration = videoPlayer.getDuration();
                if (duration == -9223372036854775807L) {
                    duration = n;
                }
            }
            final long n2 = duration / 1000L;
            final TextPaint paint = this.videoPlayerTime.getPaint();
            final long n3 = n2 / 60L;
            final long n4 = n2 % 60L;
            Math.ceil(paint.measureText(String.format("%02d:%02d / %02d:%02d", n3, n4, n3, n4)));
        }
        this.videoPlayer.preparePlayer(Uri.fromFile(file), "other");
        this.bottomLayout.setVisibility(0);
        this.videoPlayer.setPlayWhenReady(playWhenReady);
    }
    
    private boolean processTouchEvent(final MotionEvent motionEvent) {
        if (this.photoAnimationInProgress == 0) {
            if (this.animationStartTime == 0L) {
                if (motionEvent.getPointerCount() == 1 && this.gestureDetector.onTouchEvent(motionEvent) && this.doubleTap) {
                    this.doubleTap = false;
                    this.moving = false;
                    this.checkMinMax(this.zooming = false);
                    return true;
                }
                if (motionEvent.getActionMasked() != 0 && motionEvent.getActionMasked() != 5) {
                    final int actionMasked = motionEvent.getActionMasked();
                    final float n = 0.0f;
                    final float n2 = 0.0f;
                    if (actionMasked == 2) {
                        if (this.canZoom && motionEvent.getPointerCount() == 2 && !this.draggingDown && this.zooming && !this.changingPage) {
                            this.discardTap = true;
                            this.scale = (float)Math.hypot(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0)) / this.pinchStartDistance * this.pinchStartScale;
                            this.translationX = this.pinchCenterX - this.getContainerViewWidth() / 2 - (this.pinchCenterX - this.getContainerViewWidth() / 2 - this.pinchStartX) * (this.scale / this.pinchStartScale);
                            final float pinchCenterY = this.pinchCenterY;
                            final float n3 = (float)(this.getContainerViewHeight() / 2);
                            final float pinchCenterY2 = this.pinchCenterY;
                            final float n4 = (float)(this.getContainerViewHeight() / 2);
                            final float pinchStartY = this.pinchStartY;
                            final float scale = this.scale;
                            this.translationY = pinchCenterY - n3 - (pinchCenterY2 - n4 - pinchStartY) * (scale / this.pinchStartScale);
                            this.updateMinMax(scale);
                            this.photoContainerView.invalidate();
                        }
                        else if (motionEvent.getPointerCount() == 1) {
                            final VelocityTracker velocityTracker = this.velocityTracker;
                            if (velocityTracker != null) {
                                velocityTracker.addMovement(motionEvent);
                            }
                            final float abs = Math.abs(motionEvent.getX() - this.moveStartX);
                            final float abs2 = Math.abs(motionEvent.getY() - this.dragY);
                            if (abs > AndroidUtilities.dp(3.0f) || abs2 > AndroidUtilities.dp(3.0f)) {
                                this.discardTap = true;
                            }
                            if (this.canDragDown && !this.draggingDown && this.scale == 1.0f && abs2 >= AndroidUtilities.dp(30.0f) && abs2 / 2.0f > abs) {
                                this.draggingDown = true;
                                this.moving = false;
                                this.dragY = motionEvent.getY();
                                if (this.isActionBarVisible) {
                                    this.toggleActionBar(false, true);
                                }
                                return true;
                            }
                            if (this.draggingDown) {
                                this.translationY = motionEvent.getY() - this.dragY;
                                this.photoContainerView.invalidate();
                            }
                            else if (!this.invalidCoords && this.animationStartTime == 0L) {
                                float a = this.moveStartX - motionEvent.getX();
                                float a2 = this.moveStartY - motionEvent.getY();
                                if (this.moving || (this.scale == 1.0f && Math.abs(a2) + AndroidUtilities.dp(12.0f) < Math.abs(a)) || this.scale != 1.0f) {
                                    if (!this.moving) {
                                        this.moving = true;
                                        this.canDragDown = false;
                                        a = 0.0f;
                                        a2 = 0.0f;
                                    }
                                    this.moveStartX = motionEvent.getX();
                                    this.moveStartY = motionEvent.getY();
                                    this.updateMinMax(this.scale);
                                    float n5 = 0.0f;
                                    Label_0693: {
                                        if (this.translationX >= this.minX || this.rightImage.hasImageSet()) {
                                            n5 = a;
                                            if (this.translationX <= this.maxX) {
                                                break Label_0693;
                                            }
                                            n5 = a;
                                            if (this.leftImage.hasImageSet()) {
                                                break Label_0693;
                                            }
                                        }
                                        n5 = a / 3.0f;
                                    }
                                    final float maxY = this.maxY;
                                    Label_0798: {
                                        if (maxY == 0.0f) {
                                            final float minY = this.minY;
                                            if (minY == 0.0f) {
                                                final float translationY = this.translationY;
                                                if (translationY - a2 < minY) {
                                                    this.translationY = minY;
                                                    a2 = n2;
                                                    break Label_0798;
                                                }
                                                if (translationY - a2 > maxY) {
                                                    this.translationY = maxY;
                                                    a2 = n2;
                                                }
                                                break Label_0798;
                                            }
                                        }
                                        final float translationY2 = this.translationY;
                                        if (translationY2 < this.minY || translationY2 > this.maxY) {
                                            a2 /= 3.0f;
                                        }
                                    }
                                    this.translationX -= n5;
                                    if (this.scale != 1.0f) {
                                        this.translationY -= a2;
                                    }
                                    this.photoContainerView.invalidate();
                                }
                            }
                            else {
                                this.invalidCoords = false;
                                this.moveStartX = motionEvent.getX();
                                this.moveStartY = motionEvent.getY();
                            }
                        }
                    }
                    else if (motionEvent.getActionMasked() == 3 || motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 6) {
                        if (this.zooming) {
                            this.invalidCoords = true;
                            final float scale2 = this.scale;
                            if (scale2 < 1.0f) {
                                this.updateMinMax(1.0f);
                                this.animateTo(1.0f, 0.0f, 0.0f, true);
                            }
                            else if (scale2 > 3.0f) {
                                float n6 = this.pinchCenterX - this.getContainerViewWidth() / 2 - (this.pinchCenterX - this.getContainerViewWidth() / 2 - this.pinchStartX) * (3.0f / this.pinchStartScale);
                                float n7 = this.pinchCenterY - this.getContainerViewHeight() / 2 - (this.pinchCenterY - this.getContainerViewHeight() / 2 - this.pinchStartY) * (3.0f / this.pinchStartScale);
                                this.updateMinMax(3.0f);
                                final float minX = this.minX;
                                if (n6 < minX) {
                                    n6 = minX;
                                }
                                else {
                                    final float maxX = this.maxX;
                                    if (n6 > maxX) {
                                        n6 = maxX;
                                    }
                                }
                                final float minY2 = this.minY;
                                if (n7 < minY2) {
                                    n7 = minY2;
                                }
                                else {
                                    final float maxY2 = this.maxY;
                                    if (n7 > maxY2) {
                                        n7 = maxY2;
                                    }
                                }
                                this.animateTo(3.0f, n6, n7, true);
                            }
                            else {
                                this.checkMinMax(true);
                            }
                            this.zooming = false;
                        }
                        else if (this.draggingDown) {
                            if (Math.abs(this.dragY - motionEvent.getY()) > this.getContainerViewHeight() / 6.0f) {
                                this.closePhoto(true);
                            }
                            else {
                                this.animateTo(1.0f, 0.0f, 0.0f, false);
                            }
                            this.draggingDown = false;
                        }
                        else if (this.moving) {
                            final float translationX = this.translationX;
                            float translationY3 = this.translationY;
                            this.updateMinMax(this.scale);
                            this.moving = false;
                            this.canDragDown = true;
                            final VelocityTracker velocityTracker2 = this.velocityTracker;
                            float xVelocity = n;
                            if (velocityTracker2 != null) {
                                xVelocity = n;
                                if (this.scale == 1.0f) {
                                    velocityTracker2.computeCurrentVelocity(1000);
                                    xVelocity = this.velocityTracker.getXVelocity();
                                }
                            }
                            if ((this.translationX < this.minX - this.getContainerViewWidth() / 3 || xVelocity < -AndroidUtilities.dp(650.0f)) && this.rightImage.hasImageSet()) {
                                this.goToNext();
                                return true;
                            }
                            if ((this.translationX > this.maxX + this.getContainerViewWidth() / 3 || xVelocity > AndroidUtilities.dp(650.0f)) && this.leftImage.hasImageSet()) {
                                this.goToPrev();
                                return true;
                            }
                            final float translationX2 = this.translationX;
                            float n8 = this.minX;
                            if (translationX2 >= n8) {
                                n8 = this.maxX;
                                if (translationX2 <= n8) {
                                    n8 = translationX;
                                }
                            }
                            final float translationY4 = this.translationY;
                            final float minY3 = this.minY;
                            if (translationY4 < minY3) {
                                translationY3 = minY3;
                            }
                            else {
                                final float maxY3 = this.maxY;
                                if (translationY4 > maxY3) {
                                    translationY3 = maxY3;
                                }
                            }
                            this.animateTo(this.scale, n8, translationY3, false);
                        }
                    }
                }
                else {
                    this.discardTap = false;
                    if (!this.scroller.isFinished()) {
                        this.scroller.abortAnimation();
                    }
                    if (!this.draggingDown && !this.changingPage) {
                        if (this.canZoom && motionEvent.getPointerCount() == 2) {
                            this.pinchStartDistance = (float)Math.hypot(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0));
                            this.pinchStartScale = this.scale;
                            this.pinchCenterX = (motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f;
                            this.pinchCenterY = (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f;
                            this.pinchStartX = this.translationX;
                            this.pinchStartY = this.translationY;
                            this.zooming = true;
                            this.moving = false;
                            final VelocityTracker velocityTracker3 = this.velocityTracker;
                            if (velocityTracker3 != null) {
                                velocityTracker3.clear();
                            }
                        }
                        else if (motionEvent.getPointerCount() == 1) {
                            this.moveStartX = motionEvent.getX();
                            final float y = motionEvent.getY();
                            this.moveStartY = y;
                            this.dragY = y;
                            this.draggingDown = false;
                            this.canDragDown = true;
                            final VelocityTracker velocityTracker4 = this.velocityTracker;
                            if (velocityTracker4 != null) {
                                velocityTracker4.clear();
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private void releasePlayer() {
        final VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.releasePlayer(true);
            this.videoPlayer = null;
        }
        try {
            this.parentActivity.getWindow().clearFlags(128);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        final AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
        if (aspectRatioFrameLayout != null) {
            this.photoContainerView.removeView((View)aspectRatioFrameLayout);
            this.aspectRatioFrameLayout = null;
        }
        if (this.videoTextureView != null) {
            this.videoTextureView = null;
        }
        if (this.isPlaying) {
            this.isPlaying = false;
            this.videoPlayButton.setImageResource(2131165479);
            AndroidUtilities.cancelRunOnUIThread(this.updateProgressRunnable);
        }
        this.bottomLayout.setVisibility(8);
    }
    
    private boolean removeLastPageFromStack() {
        if (this.pagesStack.size() < 2) {
            return false;
        }
        final ArrayList<TLRPC.WebPage> pagesStack = this.pagesStack;
        pagesStack.remove(pagesStack.size() - 1);
        final ArrayList<TLRPC.WebPage> pagesStack2 = this.pagesStack;
        this.currentPage = pagesStack2.get(pagesStack2.size() - 1);
        this.updateInterfaceForCurrentPage(-1);
        return true;
    }
    
    private void removePressedLink() {
        if (this.pressedLink == null && this.pressedLinkOwnerView == null) {
            return;
        }
        final View pressedLinkOwnerView = this.pressedLinkOwnerView;
        this.pressedLink = null;
        this.pressedLinkOwnerLayout = null;
        this.pressedLinkOwnerView = null;
        if (pressedLinkOwnerView != null) {
            pressedLinkOwnerView.invalidate();
        }
    }
    
    private void saveCurrentPagePosition() {
        if (this.currentPage == null) {
            return;
        }
        final LinearLayoutManager[] layoutManager = this.layoutManager;
        boolean b = false;
        final int firstVisibleItemPosition = layoutManager[0].findFirstVisibleItemPosition();
        if (firstVisibleItemPosition != -1) {
            final View viewByPosition = this.layoutManager[0].findViewByPosition(firstVisibleItemPosition);
            int top;
            if (viewByPosition != null) {
                top = viewByPosition.getTop();
            }
            else {
                top = 0;
            }
            final SharedPreferences$Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("articles", 0).edit();
            final StringBuilder sb = new StringBuilder();
            sb.append("article");
            sb.append(this.currentPage.id);
            final String string = sb.toString();
            final SharedPreferences$Editor putInt = edit.putInt(string, firstVisibleItemPosition);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string);
            sb2.append("o");
            final SharedPreferences$Editor putInt2 = putInt.putInt(sb2.toString(), top);
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string);
            sb3.append("r");
            final String string2 = sb3.toString();
            final Point displaySize = AndroidUtilities.displaySize;
            if (displaySize.x > displaySize.y) {
                b = true;
            }
            putInt2.putBoolean(string2, b).commit();
        }
    }
    
    private boolean scrollToAnchor(final String s) {
        final boolean empty = TextUtils.isEmpty((CharSequence)s);
        final Integer value = 0;
        if (empty) {
            return false;
        }
        final String lowerCase = s.toLowerCase();
        Integer value2 = this.adapter[0].anchors.get(lowerCase);
        if (value2 != null) {
            final TLRPC.TL_textAnchor tl_textAnchor = this.adapter[0].anchorsParent.get(lowerCase);
            if (tl_textAnchor != null) {
                final TLRPC.TL_pageBlockParagraph tl_pageBlockParagraph = new TLRPC.TL_pageBlockParagraph();
                tl_pageBlockParagraph.text = tl_textAnchor.text;
                final int access$6600 = this.adapter[0].getTypeForBlock(tl_pageBlockParagraph);
                final RecyclerView.ViewHolder onCreateViewHolder = this.adapter[0].onCreateViewHolder(null, access$6600);
                this.adapter[0].bindBlockToHolder(access$6600, onCreateViewHolder, tl_pageBlockParagraph, 0, 0);
                final BottomSheet.Builder builder = new BottomSheet.Builder((Context)this.parentActivity);
                builder.setUseFullscreen(true);
                builder.setApplyTopPadding(false);
                final LinearLayout customView = new LinearLayout((Context)this.parentActivity);
                customView.setOrientation(1);
                final TextView textView = new TextView(this.parentActivity) {
                    protected void onDraw(final Canvas canvas) {
                        canvas.drawLine(0.0f, (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), ArticleViewer.dividerPaint);
                        super.onDraw(canvas);
                    }
                };
                textView.setTextSize(1, 16.0f);
                textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                textView.setText((CharSequence)LocaleController.getString("InstantViewReference", 2131559669));
                int n;
                if (this.isRtl) {
                    n = 5;
                }
                else {
                    n = 3;
                }
                textView.setGravity(n | 0x10);
                textView.setTextColor(this.getTextColor());
                textView.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
                customView.addView((View)textView, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, AndroidUtilities.dp(48.0f) + 1));
                customView.addView(onCreateViewHolder.itemView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 0.0f, 7.0f, 0.0f, 0.0f));
                builder.setCustomView((View)customView);
                this.linkSheet = builder.create();
                final int selectedColor = this.selectedColor;
                if (selectedColor == 0) {
                    this.linkSheet.setBackgroundColor(-1);
                }
                else if (selectedColor == 1) {
                    this.linkSheet.setBackgroundColor(-659492);
                }
                else if (selectedColor == 2) {
                    this.linkSheet.setBackgroundColor(-15461356);
                }
                this.showDialog(this.linkSheet);
                return true;
            }
            if (value2 >= 0) {
                if (value2 < this.adapter[0].blocks.size()) {
                    final TLRPC.PageBlock o = this.adapter[0].blocks.get(value2);
                    final TLRPC.PageBlock lastNonListPageBlock = this.getLastNonListPageBlock(o);
                    if (lastNonListPageBlock instanceof TL_pageBlockDetailsChild && this.openAllParentBlocks((TL_pageBlockDetailsChild)lastNonListPageBlock)) {
                        this.adapter[0].updateRows();
                        this.adapter[0].notifyDataSetChanged();
                    }
                    final int index = this.adapter[0].localBlocks.indexOf(o);
                    if (index != -1) {
                        value2 = index;
                    }
                    final Integer n2 = this.adapter[0].anchorsOffset.get(lowerCase);
                    Integer n3 = value;
                    if (n2 != null) {
                        if (n2 == -1) {
                            final int access$6601 = this.adapter[0].getTypeForBlock(o);
                            final RecyclerView.ViewHolder onCreateViewHolder2 = this.adapter[0].onCreateViewHolder(null, access$6601);
                            this.adapter[0].bindBlockToHolder(access$6601, onCreateViewHolder2, o, 0, 0);
                            onCreateViewHolder2.itemView.measure(View$MeasureSpec.makeMeasureSpec(this.listView[0].getMeasuredWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
                            n3 = this.adapter[0].anchorsOffset.get(lowerCase);
                            if (n3 == -1) {
                                n3 = value;
                            }
                        }
                        else {
                            n3 = n2;
                        }
                    }
                    this.layoutManager[0].scrollToPositionWithOffset(value2, this.currentHeaderHeight - AndroidUtilities.dp(56.0f) - n3);
                    return true;
                }
            }
        }
        return false;
    }
    
    private void setCurrentCaption(CharSequence replaceEmoji, final boolean b) {
        if (!TextUtils.isEmpty(replaceEmoji)) {
            Theme.createChatResources(null, true);
            if (!b) {
                if (replaceEmoji instanceof Spannable) {
                    final Spannable spannable = (Spannable)replaceEmoji;
                    final TextPaintUrlSpan[] array = (TextPaintUrlSpan[])spannable.getSpans(0, replaceEmoji.length(), (Class)TextPaintUrlSpan.class);
                    replaceEmoji = (CharSequence)new SpannableStringBuilder((CharSequence)replaceEmoji.toString());
                    if (array != null && array.length > 0) {
                        for (int i = 0; i < array.length; ++i) {
                            ((SpannableStringBuilder)replaceEmoji).setSpan((Object)new URLSpan(array[i].getUrl()) {
                                public void onClick(final View view) {
                                    ArticleViewer.this.openWebpageUrl(this.getURL(), null);
                                }
                            }, spannable.getSpanStart((Object)array[i]), spannable.getSpanEnd((Object)array[i]), 33);
                        }
                    }
                }
                else {
                    replaceEmoji = (CharSequence)new SpannableStringBuilder((CharSequence)replaceEmoji.toString());
                }
            }
            replaceEmoji = Emoji.replaceEmoji(replaceEmoji, this.captionTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            this.captionTextView.setTag((Object)replaceEmoji);
            this.captionTextView.setText(replaceEmoji);
            this.captionTextView.setVisibility(0);
        }
        else {
            this.captionTextView.setTag((Object)null);
            this.captionTextView.setVisibility(8);
        }
    }
    
    private void setCurrentHeaderHeight(int currentHeaderHeight) {
        final int dp = AndroidUtilities.dp(56.0f);
        final int max = Math.max(AndroidUtilities.statusBarHeight, AndroidUtilities.dp(24.0f));
        int currentHeaderHeight2;
        if (currentHeaderHeight < max) {
            currentHeaderHeight2 = max;
        }
        else if ((currentHeaderHeight2 = currentHeaderHeight) > dp) {
            currentHeaderHeight2 = dp;
        }
        final float n = (float)(dp - max);
        this.currentHeaderHeight = currentHeaderHeight2;
        currentHeaderHeight = this.currentHeaderHeight;
        final float n2 = (currentHeaderHeight - max) / n * 0.2f + 0.8f;
        final float n3 = (currentHeaderHeight - max) / n;
        this.backButton.setScaleX(n2);
        this.backButton.setScaleY(n2);
        this.backButton.setTranslationY((float)((dp - this.currentHeaderHeight) / 2));
        this.shareContainer.setScaleX(n2);
        this.shareContainer.setScaleY(n2);
        this.settingsButton.setScaleX(n2);
        this.settingsButton.setScaleY(n2);
        this.titleTextView.setScaleX(n2);
        this.titleTextView.setScaleY(n2);
        this.lineProgressView.setScaleY(n3 * 0.5f + 0.5f);
        this.shareContainer.setTranslationY((float)((dp - this.currentHeaderHeight) / 2));
        this.settingsButton.setTranslationY((float)((dp - this.currentHeaderHeight) / 2));
        this.titleTextView.setTranslationY((float)((dp - this.currentHeaderHeight) / 2));
        this.headerView.setTranslationY((float)(this.currentHeaderHeight - dp));
        currentHeaderHeight = 0;
        while (true) {
            final RecyclerListView[] listView = this.listView;
            if (currentHeaderHeight >= listView.length) {
                break;
            }
            listView[currentHeaderHeight].setTopGlowOffset(this.currentHeaderHeight);
            ++currentHeaderHeight;
        }
    }
    
    private void setImageIndex(int i, final boolean b) {
        if (this.currentIndex == i) {
            return;
        }
        if (!b) {
            final ImageReceiver.BitmapHolder currentThumb = this.currentThumb;
            if (currentThumb != null) {
                currentThumb.release();
                this.currentThumb = null;
            }
        }
        this.currentFileNames[0] = this.getFileName(i);
        this.currentFileNames[1] = this.getFileName(i + 1);
        this.currentFileNames[2] = this.getFileName(i - 1);
        final int currentIndex = this.currentIndex;
        this.currentIndex = i;
        boolean b3;
        if (!this.imagesArr.isEmpty()) {
            i = this.currentIndex;
            if (i < 0 || i >= this.imagesArr.size()) {
                this.closePhoto(false);
                return;
            }
            final TLRPC.PageBlock currentMedia = this.imagesArr.get(this.currentIndex);
            final TLRPC.PageBlock currentMedia2 = this.currentMedia;
            if (currentMedia2 != null && currentMedia2 == currentMedia) {
                i = 1;
            }
            else {
                i = 0;
            }
            this.currentMedia = currentMedia;
            final boolean mediaVideo = this.isMediaVideo(this.currentIndex);
            if (mediaVideo) {
                this.menuItem.showSubItem(3);
            }
            SpannableStringBuilder spannableStringBuilder = null;
            boolean b2 = false;
            Label_0244: {
                if (currentMedia instanceof TLRPC.TL_pageBlockPhoto) {
                    final String url = ((TLRPC.TL_pageBlockPhoto)currentMedia).url;
                    if (!TextUtils.isEmpty((CharSequence)url)) {
                        spannableStringBuilder = new SpannableStringBuilder((CharSequence)url);
                        spannableStringBuilder.setSpan((Object)new URLSpan(url) {
                            public void onClick(final View view) {
                                ArticleViewer.this.openWebpageUrl(this.getURL(), null);
                            }
                        }, 0, url.length(), 34);
                        b2 = true;
                        break Label_0244;
                    }
                }
                spannableStringBuilder = null;
                b2 = false;
            }
            CharSequence text = (CharSequence)spannableStringBuilder;
            if (spannableStringBuilder == null) {
                final TLRPC.RichText blockCaption = this.getBlockCaption(this.currentMedia, 2);
                text = this.getText(null, blockCaption, blockCaption, this.currentMedia, -AndroidUtilities.dp(100.0f));
            }
            this.setCurrentCaption(text, b2);
            if (this.currentAnimation != null) {
                this.menuItem.setVisibility(8);
                this.menuItem.hideSubItem(1);
                this.actionBar.setTitle(LocaleController.getString("AttachGif", 2131558716));
            }
            else {
                this.menuItem.setVisibility(0);
                if (this.imagesArr.size() == 1) {
                    if (mediaVideo) {
                        this.actionBar.setTitle(LocaleController.getString("AttachVideo", 2131558733));
                    }
                    else {
                        this.actionBar.setTitle(LocaleController.getString("AttachPhoto", 2131558727));
                    }
                }
                else {
                    this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.currentIndex + 1, this.imagesArr.size()));
                }
                this.menuItem.showSubItem(1);
            }
            this.groupedPhotosListView.fillList();
            b3 = mediaVideo;
        }
        else {
            i = 0;
            b3 = false;
        }
        for (int childCount = this.listView[0].getChildCount(), j = 0; j < childCount; ++j) {
            final View child = this.listView[0].getChildAt(j);
            if (child instanceof BlockSlideshowCell) {
                final BlockSlideshowCell blockSlideshowCell = (BlockSlideshowCell)child;
                final int index = blockSlideshowCell.currentBlock.items.indexOf(this.currentMedia);
                if (index != -1) {
                    blockSlideshowCell.innerListView.setCurrentItem(index, false);
                    break;
                }
            }
        }
        final PlaceProviderObject currentPlaceObject = this.currentPlaceObject;
        if (currentPlaceObject != null) {
            if (this.photoAnimationInProgress == 0) {
                currentPlaceObject.imageReceiver.setVisible(true, true);
            }
            else {
                this.showAfterAnimation = currentPlaceObject;
            }
        }
        this.currentPlaceObject = this.getPlaceForPhoto(this.currentMedia);
        final PlaceProviderObject currentPlaceObject2 = this.currentPlaceObject;
        if (currentPlaceObject2 != null) {
            if (this.photoAnimationInProgress == 0) {
                currentPlaceObject2.imageReceiver.setVisible(false, true);
            }
            else {
                this.hideAfterAnimation = currentPlaceObject2;
            }
        }
        if (i == 0) {
            this.draggingDown = false;
            this.translationX = 0.0f;
            this.translationY = 0.0f;
            this.scale = 1.0f;
            this.animateToX = 0.0f;
            this.animateToY = 0.0f;
            this.animateToScale = 1.0f;
            this.animationStartTime = 0L;
            this.imageMoveAnimation = null;
            final AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
            if (aspectRatioFrameLayout != null) {
                aspectRatioFrameLayout.setVisibility(4);
            }
            this.releasePlayer();
            this.pinchStartDistance = 0.0f;
            this.pinchStartScale = 1.0f;
            this.pinchCenterX = 0.0f;
            this.pinchCenterY = 0.0f;
            this.pinchStartX = 0.0f;
            this.pinchStartY = 0.0f;
            this.moveStartX = 0.0f;
            this.moveStartY = 0.0f;
            this.zooming = false;
            this.moving = false;
            this.doubleTap = false;
            this.invalidCoords = false;
            this.canDragDown = true;
            this.changingPage = false;
            this.switchImageAfterAnimation = 0;
            this.canZoom = (this.currentFileNames[0] != null && !b3 && this.radialProgressViews[0].backgroundState != 0);
            this.updateMinMax(this.scale);
        }
        if (currentIndex == -1) {
            this.setImages();
            for (i = 0; i < 3; ++i) {
                this.checkProgress(i, false);
            }
        }
        else {
            this.checkProgress(0, false);
            i = this.currentIndex;
            if (currentIndex > i) {
                final ImageReceiver rightImage = this.rightImage;
                this.rightImage = this.centerImage;
                this.centerImage = this.leftImage;
                this.leftImage = rightImage;
                final RadialProgressView[] radialProgressViews = this.radialProgressViews;
                final RadialProgressView radialProgressView = radialProgressViews[0];
                radialProgressViews[0] = radialProgressViews[2];
                radialProgressViews[2] = radialProgressView;
                this.setIndexToImage(this.leftImage, i - 1);
                this.checkProgress(1, false);
                this.checkProgress(2, false);
            }
            else if (currentIndex < i) {
                final ImageReceiver leftImage = this.leftImage;
                this.leftImage = this.centerImage;
                this.centerImage = this.rightImage;
                this.rightImage = leftImage;
                final RadialProgressView[] radialProgressViews2 = this.radialProgressViews;
                final RadialProgressView radialProgressView2 = radialProgressViews2[0];
                radialProgressViews2[0] = radialProgressViews2[1];
                radialProgressViews2[1] = radialProgressView2;
                this.setIndexToImage(this.rightImage, i + 1);
                this.checkProgress(1, false);
                this.checkProgress(2, false);
            }
        }
    }
    
    private void setImages() {
        if (this.photoAnimationInProgress == 0) {
            this.setIndexToImage(this.centerImage, this.currentIndex);
            this.setIndexToImage(this.rightImage, this.currentIndex + 1);
            this.setIndexToImage(this.leftImage, this.currentIndex - 1);
        }
    }
    
    private void setIndexToImage(final ImageReceiver imageReceiver, final int n) {
        imageReceiver.setOrientation(0, false);
        final int[] array = { 0 };
        final TLObject media = this.getMedia(n);
        final TLRPC.PhotoSize fileLocation = this.getFileLocation(media, array);
        if (fileLocation != null) {
            if (media instanceof TLRPC.Photo) {
                final TLRPC.Photo photo = (TLRPC.Photo)media;
                ImageReceiver.BitmapHolder currentThumb = this.currentThumb;
                if (currentThumb == null || imageReceiver != this.centerImage) {
                    currentThumb = null;
                }
                if (array[0] == 0) {
                    array[0] = -1;
                }
                final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 80);
                final ImageLocation forPhoto = ImageLocation.getForPhoto(fileLocation, photo);
                final ImageLocation forPhoto2 = ImageLocation.getForPhoto(closestPhotoSizeWithSize, photo);
                Object o;
                if (currentThumb != null) {
                    o = new BitmapDrawable(currentThumb.bitmap);
                }
                else {
                    o = null;
                }
                imageReceiver.setImage(forPhoto, null, forPhoto2, "b", (Drawable)o, array[0], null, this.currentPage, 1);
            }
            else if (this.isMediaVideo(n)) {
                if (!(fileLocation.location instanceof TLRPC.TL_fileLocationUnavailable)) {
                    ImageReceiver.BitmapHolder currentThumb2 = this.currentThumb;
                    if (currentThumb2 == null || imageReceiver != this.centerImage) {
                        currentThumb2 = null;
                    }
                    final ImageLocation forDocument = ImageLocation.getForDocument(fileLocation, (TLRPC.Document)media);
                    Object o2;
                    if (currentThumb2 != null) {
                        o2 = new BitmapDrawable(currentThumb2.bitmap);
                    }
                    else {
                        o2 = null;
                    }
                    imageReceiver.setImage(null, null, forDocument, "b", (Drawable)o2, 0, null, this.currentPage, 1);
                }
                else {
                    imageReceiver.setImageBitmap(this.parentActivity.getResources().getDrawable(2131165761));
                }
            }
            else {
                final AnimatedFileDrawable currentAnimation = this.currentAnimation;
                if (currentAnimation != null) {
                    imageReceiver.setImageBitmap((Drawable)currentAnimation);
                    this.currentAnimation.setSecondParentView((View)this.photoContainerView);
                }
            }
        }
        else if (array[0] == 0) {
            imageReceiver.setImageBitmap((Bitmap)null);
        }
        else {
            imageReceiver.setImageBitmap(this.parentActivity.getResources().getDrawable(2131165761));
        }
    }
    
    private void setMapColors(final SparseArray<TextPaint> sparseArray) {
        for (int i = 0; i < sparseArray.size(); ++i) {
            final int key = sparseArray.keyAt(i);
            final TextPaint textPaint = (TextPaint)sparseArray.valueAt(i);
            if ((key & 0x8) == 0x0 && (key & 0x200) == 0x0) {
                textPaint.setColor(this.getTextColor());
            }
            else {
                textPaint.setColor(this.getLinkTextColor());
            }
        }
    }
    
    private void setScaleToFill() {
        final float n = (float)this.centerImage.getBitmapWidth();
        final float n2 = (float)this.getContainerViewWidth();
        final float n3 = (float)this.centerImage.getBitmapHeight();
        final float n4 = (float)this.getContainerViewHeight();
        final float min = Math.min(n4 / n3, n2 / n);
        this.updateMinMax(this.scale = Math.max(n2 / (int)(n * min), n4 / (int)(n3 * min)));
    }
    
    private void showCopyPopup(final String title) {
        if (this.parentActivity == null) {
            return;
        }
        final BottomSheet linkSheet = this.linkSheet;
        if (linkSheet != null) {
            linkSheet.dismiss();
            this.linkSheet = null;
        }
        final BottomSheet.Builder builder = new BottomSheet.Builder((Context)this.parentActivity);
        builder.setUseFullscreen(true);
        builder.setTitle(title);
        final String string = LocaleController.getString("Open", 2131560110);
        int i = 0;
        builder.setItems(new CharSequence[] { string, LocaleController.getString("Copy", 2131559163) }, (DialogInterface$OnClickListener)new _$$Lambda$ArticleViewer$OR_FYCAXpGUOR5Uvrpul7uvfnQI(this, title));
        final BottomSheet create = builder.create();
        this.showDialog(create);
        while (i < 2) {
            create.setItemColor(i, this.getTextColor(), this.getTextColor());
            ++i;
        }
        create.setTitleColor(this.getGrayTextColor());
        final int selectedColor = this.selectedColor;
        if (selectedColor == 0) {
            create.setBackgroundColor(-1);
        }
        else if (selectedColor == 1) {
            create.setBackgroundColor(-659492);
        }
        else if (selectedColor == 2) {
            create.setBackgroundColor(-15461356);
        }
    }
    
    private void showNightModeHint() {
        final Activity parentActivity = this.parentActivity;
        if (parentActivity != null && this.nightModeHintView == null) {
            if (this.nightModeEnabled) {
                (this.nightModeHintView = new FrameLayout((Context)parentActivity)).setBackgroundColor(-13421773);
                this.containerView.addView((View)this.nightModeHintView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2, 83));
                final ImageView imageView = new ImageView((Context)this.parentActivity);
                imageView.setScaleType(ImageView$ScaleType.CENTER);
                imageView.setImageResource(2131165607);
                final FrameLayout nightModeHintView = this.nightModeHintView;
                final boolean isRTL = LocaleController.isRTL;
                final int n = 5;
                int n2;
                if (isRTL) {
                    n2 = 5;
                }
                else {
                    n2 = 3;
                }
                nightModeHintView.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(56, 56, n2 | 0x10));
                final TextView textView = new TextView((Context)this.parentActivity);
                textView.setText((CharSequence)LocaleController.getString("InstantViewNightMode", 2131559668));
                textView.setTextColor(-1);
                textView.setTextSize(1, 15.0f);
                final FrameLayout nightModeHintView2 = this.nightModeHintView;
                int n3;
                if (LocaleController.isRTL) {
                    n3 = n;
                }
                else {
                    n3 = 3;
                }
                final boolean isRTL2 = LocaleController.isRTL;
                final int n4 = 10;
                int n5;
                if (isRTL2) {
                    n5 = 10;
                }
                else {
                    n5 = 56;
                }
                final float n6 = (float)n5;
                int n7 = n4;
                if (LocaleController.isRTL) {
                    n7 = 56;
                }
                nightModeHintView2.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, n6, 11.0f, (float)n7, 12.0f));
                final AnimatorSet set = new AnimatorSet();
                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.nightModeHintView, View.TRANSLATION_Y, new float[] { (float)AndroidUtilities.dp(100.0f), 0.0f }) });
                set.setInterpolator((TimeInterpolator)new DecelerateInterpolator(1.5f));
                set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$12$IKrF64A2pjQihanm36OwYzYcJGQ(this), 3000L);
                    }
                });
                set.setDuration(250L);
                set.start();
            }
        }
    }
    
    private void showPopup(final View view, final int n, final int n2, final int n3) {
        final ActionBarPopupWindow popupWindow = this.popupWindow;
        if (popupWindow != null && popupWindow.isShowing()) {
            this.popupWindow.dismiss();
            return;
        }
        if (this.popupLayout == null) {
            this.popupRect = new Rect();
            (this.popupLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout((Context)this.parentActivity)).setPadding(AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f));
            this.popupLayout.setBackgroundDrawable(this.copyBackgroundDrawable = this.parentActivity.getResources().getDrawable(2131165578));
            this.popupLayout.setAnimationEnabled(false);
            this.popupLayout.setOnTouchListener((View$OnTouchListener)new _$$Lambda$ArticleViewer$MGSGAQzWCEU3w9PF3AH_evurh_k(this));
            this.popupLayout.setDispatchKeyEventListener(new _$$Lambda$ArticleViewer$qD5uCRo_niW9s97tmnD3kf5paxo(this));
            this.popupLayout.setShowedFromBotton(false);
            (this.deleteView = new TextView((Context)this.parentActivity)).setBackgroundDrawable(Theme.createSelectorDrawable(251658240, 2));
            this.deleteView.setGravity(16);
            this.deleteView.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
            this.deleteView.setTextSize(1, 15.0f);
            this.deleteView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.deleteView.setText((CharSequence)LocaleController.getString("Copy", 2131559163).toUpperCase());
            this.deleteView.setOnClickListener((View$OnClickListener)new _$$Lambda$ArticleViewer$RDJJaoFjXrUN__l9em3k_Gv7twk(this));
            this.popupLayout.addView((View)this.deleteView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 48.0f));
            (this.popupWindow = new ActionBarPopupWindow((View)this.popupLayout, -2, -2)).setAnimationEnabled(false);
            this.popupWindow.setAnimationStyle(2131624111);
            this.popupWindow.setOutsideTouchable(true);
            this.popupWindow.setClippingEnabled(true);
            this.popupWindow.setInputMethodMode(2);
            this.popupWindow.setSoftInputMode(0);
            this.popupWindow.getContentView().setFocusableInTouchMode(true);
            this.popupWindow.setOnDismissListener((PopupWindow$OnDismissListener)new _$$Lambda$ArticleViewer$IPD6DMulJZDu02GPHHVkQa_Seko(this));
        }
        if (this.selectedColor == 2) {
            this.deleteView.setTextColor(-5723992);
            final Drawable copyBackgroundDrawable = this.copyBackgroundDrawable;
            if (copyBackgroundDrawable != null) {
                copyBackgroundDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(-14408668, PorterDuff$Mode.MULTIPLY));
            }
        }
        else {
            this.deleteView.setTextColor(-14606047);
            final Drawable copyBackgroundDrawable2 = this.copyBackgroundDrawable;
            if (copyBackgroundDrawable2 != null) {
                copyBackgroundDrawable2.setColorFilter((ColorFilter)new PorterDuffColorFilter(-1, PorterDuff$Mode.MULTIPLY));
            }
        }
        this.popupLayout.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        this.popupWindow.setFocusable(true);
        this.popupWindow.showAtLocation(view, n, n2, n3);
        this.popupWindow.startAnimation();
    }
    
    private void showProgressView(final boolean b, final boolean b2) {
        if (b) {
            AndroidUtilities.cancelRunOnUIThread(this.lineProgressTickRunnable);
            if (b2) {
                this.lineProgressView.setProgress(0.0f, false);
                this.lineProgressView.setProgress(0.3f, true);
                AndroidUtilities.runOnUIThread(this.lineProgressTickRunnable, 100L);
            }
            else {
                this.lineProgressView.setProgress(1.0f, true);
            }
        }
        else {
            final AnimatorSet progressViewAnimation = this.progressViewAnimation;
            if (progressViewAnimation != null) {
                progressViewAnimation.cancel();
            }
            this.progressViewAnimation = new AnimatorSet();
            if (b2) {
                this.progressView.setVisibility(0);
                this.shareContainer.setEnabled(false);
                this.progressViewAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.shareButton, View.SCALE_X, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.shareButton, View.SCALE_Y, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.shareButton, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.ALPHA, new float[] { 1.0f }) });
            }
            else {
                this.shareButton.setVisibility(0);
                this.shareContainer.setEnabled(true);
                this.progressViewAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.SCALE_X, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.SCALE_Y, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shareButton, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shareButton, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shareButton, View.ALPHA, new float[] { 1.0f }) });
            }
            this.progressViewAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator obj) {
                    if (ArticleViewer.this.progressViewAnimation != null && ArticleViewer.this.progressViewAnimation.equals(obj)) {
                        ArticleViewer.this.progressViewAnimation = null;
                    }
                }
                
                public void onAnimationEnd(final Animator obj) {
                    if (ArticleViewer.this.progressViewAnimation != null && ArticleViewer.this.progressViewAnimation.equals(obj)) {
                        if (!b2) {
                            ArticleViewer.this.progressView.setVisibility(4);
                        }
                        else {
                            ArticleViewer.this.shareButton.setVisibility(4);
                        }
                    }
                }
            });
            this.progressViewAnimation.setDuration(150L);
            this.progressViewAnimation.start();
        }
    }
    
    private void toggleActionBar(final boolean enabled, final boolean b) {
        if (enabled) {
            this.actionBar.setVisibility(0);
            if (this.videoPlayer != null) {
                this.bottomLayout.setVisibility(0);
            }
            if (this.captionTextView.getTag() != null) {
                this.captionTextView.setVisibility(0);
            }
        }
        this.isActionBarVisible = enabled;
        this.actionBar.setEnabled(enabled);
        this.bottomLayout.setEnabled(enabled);
        float alpha = 1.0f;
        if (b) {
            final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
            final ActionBar actionBar = this.actionBar;
            final Property alpha2 = View.ALPHA;
            float n;
            if (enabled) {
                n = 1.0f;
            }
            else {
                n = 0.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)actionBar, alpha2, new float[] { n }));
            final GroupedPhotosListView groupedPhotosListView = this.groupedPhotosListView;
            final Property alpha3 = View.ALPHA;
            float n2;
            if (enabled) {
                n2 = 1.0f;
            }
            else {
                n2 = 0.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)groupedPhotosListView, alpha3, new float[] { n2 }));
            final FrameLayout bottomLayout = this.bottomLayout;
            final Property alpha4 = View.ALPHA;
            float n3;
            if (enabled) {
                n3 = 1.0f;
            }
            else {
                n3 = 0.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)bottomLayout, alpha4, new float[] { n3 }));
            if (this.captionTextView.getTag() != null) {
                final TextView captionTextView = this.captionTextView;
                final Property alpha5 = View.ALPHA;
                if (!enabled) {
                    alpha = 0.0f;
                }
                list.add(ObjectAnimator.ofFloat((Object)captionTextView, alpha5, new float[] { alpha }));
            }
            (this.currentActionBarAnimation = new AnimatorSet()).playTogether((Collection)list);
            if (!enabled) {
                this.currentActionBarAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator obj) {
                        if (ArticleViewer.this.currentActionBarAnimation != null && ArticleViewer.this.currentActionBarAnimation.equals(obj)) {
                            ArticleViewer.this.actionBar.setVisibility(8);
                            if (ArticleViewer.this.videoPlayer != null) {
                                ArticleViewer.this.bottomLayout.setVisibility(8);
                            }
                            if (ArticleViewer.this.captionTextView.getTag() != null) {
                                ArticleViewer.this.captionTextView.setVisibility(8);
                            }
                            ArticleViewer.this.currentActionBarAnimation = null;
                        }
                    }
                });
            }
            this.currentActionBarAnimation.setDuration(200L);
            this.currentActionBarAnimation.start();
        }
        else {
            final ActionBar actionBar2 = this.actionBar;
            float alpha6;
            if (enabled) {
                alpha6 = 1.0f;
            }
            else {
                alpha6 = 0.0f;
            }
            actionBar2.setAlpha(alpha6);
            final FrameLayout bottomLayout2 = this.bottomLayout;
            float alpha7;
            if (enabled) {
                alpha7 = 1.0f;
            }
            else {
                alpha7 = 0.0f;
            }
            bottomLayout2.setAlpha(alpha7);
            if (this.captionTextView.getTag() != null) {
                final TextView captionTextView2 = this.captionTextView;
                if (!enabled) {
                    alpha = 0.0f;
                }
                captionTextView2.setAlpha(alpha);
            }
            if (!enabled) {
                this.actionBar.setVisibility(8);
                if (this.videoPlayer != null) {
                    this.bottomLayout.setVisibility(8);
                }
                if (this.captionTextView.getTag() != null) {
                    this.captionTextView.setVisibility(8);
                }
            }
        }
    }
    
    private void updateFontEntry(final int n, final TextPaint textPaint, final Typeface typeface, final Typeface typeface2, final Typeface typeface3, final Typeface typeface4) {
        final int n2 = n & 0x1;
        if (n2 != 0 && (n & 0x2) != 0x0) {
            textPaint.setTypeface(typeface2);
        }
        else if (n2 != 0) {
            textPaint.setTypeface(typeface3);
        }
        else if ((n & 0x2) != 0x0) {
            textPaint.setTypeface(typeface4);
        }
        else if ((n & 0x4) == 0x0) {
            textPaint.setTypeface(typeface);
        }
    }
    
    private void updateInterfaceForCurrentPage(int dp) {
        final TLRPC.WebPage currentPage = this.currentPage;
        if (currentPage != null) {
            final TLRPC.Page cached_page = currentPage.cached_page;
            if (cached_page != null) {
                this.isRtl = cached_page.rtl;
                this.channelBlock = null;
                final SimpleTextView titleTextView = this.titleTextView;
                String site_name;
                if ((site_name = currentPage.site_name) == null) {
                    site_name = "";
                }
                titleTextView.setText(site_name);
                int n = true ? 1 : 0;
                if (dp != 0) {
                    final WebpageAdapter[] adapter = this.adapter;
                    final WebpageAdapter webpageAdapter = adapter[1];
                    adapter[1] = adapter[0];
                    adapter[0] = webpageAdapter;
                    final RecyclerListView[] listView = this.listView;
                    final RecyclerListView recyclerListView = listView[1];
                    listView[1] = listView[0];
                    listView[0] = recyclerListView;
                    final LinearLayoutManager[] layoutManager = this.layoutManager;
                    final LinearLayoutManager linearLayoutManager = layoutManager[1];
                    layoutManager[1] = layoutManager[0];
                    layoutManager[0] = linearLayoutManager;
                    final int indexOfChild = this.containerView.indexOfChild((View)listView[0]);
                    final int indexOfChild2 = this.containerView.indexOfChild((View)this.listView[1]);
                    if (dp == 1) {
                        if (indexOfChild < indexOfChild2) {
                            this.containerView.removeView((View)this.listView[0]);
                            this.containerView.addView((View)this.listView[0], indexOfChild2);
                        }
                    }
                    else if (indexOfChild2 < indexOfChild) {
                        this.containerView.removeView((View)this.listView[0]);
                        this.containerView.addView((View)this.listView[0], indexOfChild);
                    }
                    this.pageSwitchAnimation = new AnimatorSet();
                    this.listView[0].setVisibility(0);
                    int n2;
                    if (dp == 1) {
                        n2 = 0;
                    }
                    else {
                        n2 = 1;
                    }
                    this.listView[n2].setBackgroundColor(this.backgroundPaint.getColor());
                    if (Build$VERSION.SDK_INT >= 18) {
                        this.listView[n2].setLayerType(2, (Paint)null);
                    }
                    if (dp == 1) {
                        this.pageSwitchAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.listView[0], View.TRANSLATION_X, new float[] { (float)AndroidUtilities.dp(56.0f), 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.listView[0], View.ALPHA, new float[] { 0.0f, 1.0f }) });
                    }
                    else if (dp == -1) {
                        this.listView[0].setAlpha(1.0f);
                        this.listView[0].setTranslationX(0.0f);
                        this.pageSwitchAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.listView[1], View.TRANSLATION_X, new float[] { 0.0f, (float)AndroidUtilities.dp(56.0f) }), (Animator)ObjectAnimator.ofFloat((Object)this.listView[1], View.ALPHA, new float[] { 1.0f, 0.0f }) });
                    }
                    this.pageSwitchAnimation.setDuration(150L);
                    this.pageSwitchAnimation.setInterpolator((TimeInterpolator)this.interpolator);
                    this.pageSwitchAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            ArticleViewer.this.listView[1].setVisibility(8);
                            ArticleViewer.this.listView[n2].setBackgroundDrawable((Drawable)null);
                            if (Build$VERSION.SDK_INT >= 18) {
                                ArticleViewer.this.listView[n2].setLayerType(0, (Paint)null);
                            }
                            ArticleViewer.this.pageSwitchAnimation = null;
                        }
                    });
                    this.pageSwitchAnimation.start();
                }
                this.headerView.invalidate();
                this.adapter[0].cleanup();
                for (int size = this.currentPage.cached_page.blocks.size(), i = 0; i < size; ++i) {
                    final TLRPC.PageBlock pageBlock = this.currentPage.cached_page.blocks.get(i);
                    if (i == 0) {
                        pageBlock.first = true;
                        if (pageBlock instanceof TLRPC.TL_pageBlockCover) {
                            final TLRPC.TL_pageBlockCover tl_pageBlockCover = (TLRPC.TL_pageBlockCover)pageBlock;
                            final TLRPC.RichText blockCaption = this.getBlockCaption(tl_pageBlockCover, 0);
                            final TLRPC.RichText blockCaption2 = this.getBlockCaption(tl_pageBlockCover, 1);
                            if (((blockCaption != null && !(blockCaption instanceof TLRPC.TL_textEmpty)) || (blockCaption2 != null && !(blockCaption2 instanceof TLRPC.TL_textEmpty))) && size > 1) {
                                final TLRPC.PageBlock pageBlock2 = this.currentPage.cached_page.blocks.get(1);
                                if (pageBlock2 instanceof TLRPC.TL_pageBlockChannel) {
                                    this.channelBlock = (TLRPC.TL_pageBlockChannel)pageBlock2;
                                }
                            }
                        }
                    }
                    else if (i == 1 && this.channelBlock != null) {
                        continue;
                    }
                    final WebpageAdapter webpageAdapter2 = this.adapter[0];
                    int n3;
                    if (i == size - 1) {
                        n3 = i;
                    }
                    else {
                        n3 = 0;
                    }
                    webpageAdapter2.addBlock(pageBlock, 0, 0, n3);
                }
                this.adapter[0].notifyDataSetChanged();
                if (this.pagesStack.size() != 1 && dp != -1) {
                    this.layoutManager[0].scrollToPositionWithOffset(0, 0);
                }
                else {
                    final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("articles", 0);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("article");
                    sb.append(this.currentPage.id);
                    final String string = sb.toString();
                    final int int1 = sharedPreferences.getInt(string, -1);
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(string);
                    sb2.append("r");
                    final boolean boolean1 = sharedPreferences.getBoolean(sb2.toString(), true);
                    final Point displaySize = AndroidUtilities.displaySize;
                    if (displaySize.x <= displaySize.y) {
                        n = (false ? 1 : 0);
                    }
                    if ((boolean1 ? 1 : 0) == n) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append(string);
                        sb3.append("o");
                        dp = sharedPreferences.getInt(sb3.toString(), 0) - this.listView[0].getPaddingTop();
                    }
                    else {
                        dp = AndroidUtilities.dp(10.0f);
                    }
                    if (int1 != -1) {
                        this.layoutManager[0].scrollToPositionWithOffset(int1, dp);
                    }
                }
                this.checkScrollAnimated();
            }
        }
    }
    
    private void updateMinMax(final float n) {
        final int n2 = (int)(this.centerImage.getImageWidth() * n - this.getContainerViewWidth()) / 2;
        final int n3 = (int)(this.centerImage.getImageHeight() * n - this.getContainerViewHeight()) / 2;
        if (n2 > 0) {
            this.minX = (float)(-n2);
            this.maxX = (float)n2;
        }
        else {
            this.maxX = 0.0f;
            this.minX = 0.0f;
        }
        if (n3 > 0) {
            this.minY = (float)(-n3);
            this.maxY = (float)n3;
        }
        else {
            this.maxY = 0.0f;
            this.minY = 0.0f;
        }
    }
    
    private void updateNightModeButton() {
        this.nightModeImageView.setEnabled(this.selectedColor != 2);
        final ImageView nightModeImageView = this.nightModeImageView;
        float alpha;
        if (this.selectedColor == 2) {
            alpha = 0.5f;
        }
        else {
            alpha = 1.0f;
        }
        nightModeImageView.setAlpha(alpha);
        final ImageView nightModeImageView2 = this.nightModeImageView;
        int n;
        if (this.nightModeEnabled && this.selectedColor != 2) {
            n = -15428119;
        }
        else {
            n = -3355444;
        }
        nightModeImageView2.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
    }
    
    private void updatePaintColors() {
        final Context applicationContext = ApplicationLoader.applicationContext;
        final int n = 0;
        final int n2 = 0;
        int n3 = 0;
        applicationContext.getSharedPreferences("articles", 0).edit().putInt("font_color", this.selectedColor).commit();
        final int selectedColor = this.getSelectedColor();
        if (selectedColor == 0) {
            this.backgroundPaint.setColor(-1);
            while (true) {
                final RecyclerListView[] listView = this.listView;
                if (n3 >= listView.length) {
                    break;
                }
                listView[n3].setGlowColor(-657673);
                ++n3;
            }
        }
        else if (selectedColor == 1) {
            this.backgroundPaint.setColor(-659492);
            int n4 = n;
            while (true) {
                final RecyclerListView[] listView2 = this.listView;
                if (n4 >= listView2.length) {
                    break;
                }
                listView2[n4].setGlowColor(-659492);
                ++n4;
            }
        }
        else if (selectedColor == 2) {
            this.backgroundPaint.setColor(-15461356);
            int n5 = n2;
            while (true) {
                final RecyclerListView[] listView3 = this.listView;
                if (n5 >= listView3.length) {
                    break;
                }
                listView3[n5].setGlowColor(-15461356);
                ++n5;
            }
        }
        final TextPaint listTextPointerPaint = ArticleViewer.listTextPointerPaint;
        if (listTextPointerPaint != null) {
            listTextPointerPaint.setColor(this.getTextColor());
        }
        final TextPaint listTextNumPaint = ArticleViewer.listTextNumPaint;
        if (listTextNumPaint != null) {
            listTextNumPaint.setColor(this.getTextColor());
        }
        final TextPaint embedPostAuthorPaint = ArticleViewer.embedPostAuthorPaint;
        if (embedPostAuthorPaint != null) {
            embedPostAuthorPaint.setColor(this.getTextColor());
        }
        final TextPaint channelNamePaint = ArticleViewer.channelNamePaint;
        if (channelNamePaint != null) {
            if (this.channelBlock == null) {
                channelNamePaint.setColor(this.getTextColor());
            }
            else {
                channelNamePaint.setColor(-1);
            }
        }
        final TextPaint relatedArticleHeaderPaint = ArticleViewer.relatedArticleHeaderPaint;
        if (relatedArticleHeaderPaint != null) {
            relatedArticleHeaderPaint.setColor(this.getTextColor());
        }
        final TextPaint relatedArticleTextPaint = ArticleViewer.relatedArticleTextPaint;
        if (relatedArticleTextPaint != null) {
            relatedArticleTextPaint.setColor(this.getGrayTextColor());
        }
        final TextPaint embedPostDatePaint = ArticleViewer.embedPostDatePaint;
        if (embedPostDatePaint != null) {
            if (selectedColor == 0) {
                embedPostDatePaint.setColor(-7366752);
            }
            else {
                embedPostDatePaint.setColor(this.getGrayTextColor());
            }
        }
        this.createPaint(true);
        this.setMapColors(ArticleViewer.titleTextPaints);
        this.setMapColors(ArticleViewer.kickerTextPaints);
        this.setMapColors(ArticleViewer.subtitleTextPaints);
        this.setMapColors(ArticleViewer.headerTextPaints);
        this.setMapColors(ArticleViewer.subheaderTextPaints);
        this.setMapColors(ArticleViewer.quoteTextPaints);
        this.setMapColors(ArticleViewer.preformattedTextPaints);
        this.setMapColors(ArticleViewer.paragraphTextPaints);
        this.setMapColors(ArticleViewer.listTextPaints);
        this.setMapColors(ArticleViewer.embedPostTextPaints);
        this.setMapColors(ArticleViewer.mediaCaptionTextPaints);
        this.setMapColors(ArticleViewer.mediaCreditTextPaints);
        this.setMapColors(ArticleViewer.photoCaptionTextPaints);
        this.setMapColors(ArticleViewer.photoCreditTextPaints);
        this.setMapColors(ArticleViewer.authorTextPaints);
        this.setMapColors(ArticleViewer.footerTextPaints);
        this.setMapColors(ArticleViewer.embedPostCaptionTextPaints);
        this.setMapColors(ArticleViewer.relatedArticleTextPaints);
        this.setMapColors(ArticleViewer.detailsTextPaints);
        this.setMapColors(ArticleViewer.tableTextPaints);
    }
    
    private void updatePaintFonts() {
        final Context applicationContext = ApplicationLoader.applicationContext;
        final int n = 0;
        applicationContext.getSharedPreferences("articles", 0).edit().putInt("font_type", this.selectedFont).commit();
        Typeface typeface;
        if (this.selectedFont == 0) {
            typeface = Typeface.DEFAULT;
        }
        else {
            typeface = Typeface.SERIF;
        }
        Typeface typeface2;
        if (this.selectedFont == 0) {
            typeface2 = AndroidUtilities.getTypeface("fonts/ritalic.ttf");
        }
        else {
            typeface2 = Typeface.create("serif", 2);
        }
        Typeface typeface3;
        if (this.selectedFont == 0) {
            typeface3 = AndroidUtilities.getTypeface("fonts/rmedium.ttf");
        }
        else {
            typeface3 = Typeface.create("serif", 1);
        }
        Typeface typeface4;
        if (this.selectedFont == 0) {
            typeface4 = AndroidUtilities.getTypeface("fonts/rmediumitalic.ttf");
        }
        else {
            typeface4 = Typeface.create("serif", 3);
        }
        for (int i = 0; i < ArticleViewer.quoteTextPaints.size(); ++i) {
            this.updateFontEntry(ArticleViewer.quoteTextPaints.keyAt(i), (TextPaint)ArticleViewer.quoteTextPaints.valueAt(i), typeface, typeface4, typeface3, typeface2);
        }
        for (int j = 0; j < ArticleViewer.preformattedTextPaints.size(); ++j) {
            this.updateFontEntry(ArticleViewer.preformattedTextPaints.keyAt(j), (TextPaint)ArticleViewer.preformattedTextPaints.valueAt(j), typeface, typeface4, typeface3, typeface2);
        }
        for (int k = 0; k < ArticleViewer.paragraphTextPaints.size(); ++k) {
            this.updateFontEntry(ArticleViewer.paragraphTextPaints.keyAt(k), (TextPaint)ArticleViewer.paragraphTextPaints.valueAt(k), typeface, typeface4, typeface3, typeface2);
        }
        for (int l = 0; l < ArticleViewer.listTextPaints.size(); ++l) {
            this.updateFontEntry(ArticleViewer.listTextPaints.keyAt(l), (TextPaint)ArticleViewer.listTextPaints.valueAt(l), typeface, typeface4, typeface3, typeface2);
        }
        for (int n2 = 0; n2 < ArticleViewer.embedPostTextPaints.size(); ++n2) {
            this.updateFontEntry(ArticleViewer.embedPostTextPaints.keyAt(n2), (TextPaint)ArticleViewer.embedPostTextPaints.valueAt(n2), typeface, typeface4, typeface3, typeface2);
        }
        for (int n3 = 0; n3 < ArticleViewer.mediaCaptionTextPaints.size(); ++n3) {
            this.updateFontEntry(ArticleViewer.mediaCaptionTextPaints.keyAt(n3), (TextPaint)ArticleViewer.mediaCaptionTextPaints.valueAt(n3), typeface, typeface4, typeface3, typeface2);
        }
        for (int n4 = 0; n4 < ArticleViewer.mediaCreditTextPaints.size(); ++n4) {
            this.updateFontEntry(ArticleViewer.mediaCreditTextPaints.keyAt(n4), (TextPaint)ArticleViewer.mediaCreditTextPaints.valueAt(n4), typeface, typeface4, typeface3, typeface2);
        }
        for (int n5 = 0; n5 < ArticleViewer.photoCaptionTextPaints.size(); ++n5) {
            this.updateFontEntry(ArticleViewer.photoCaptionTextPaints.keyAt(n5), (TextPaint)ArticleViewer.photoCaptionTextPaints.valueAt(n5), typeface, typeface4, typeface3, typeface2);
        }
        for (int n6 = 0; n6 < ArticleViewer.photoCreditTextPaints.size(); ++n6) {
            this.updateFontEntry(ArticleViewer.photoCreditTextPaints.keyAt(n6), (TextPaint)ArticleViewer.photoCreditTextPaints.valueAt(n6), typeface, typeface4, typeface3, typeface2);
        }
        for (int n7 = 0; n7 < ArticleViewer.authorTextPaints.size(); ++n7) {
            this.updateFontEntry(ArticleViewer.authorTextPaints.keyAt(n7), (TextPaint)ArticleViewer.authorTextPaints.valueAt(n7), typeface, typeface4, typeface3, typeface2);
        }
        for (int n8 = 0; n8 < ArticleViewer.footerTextPaints.size(); ++n8) {
            this.updateFontEntry(ArticleViewer.footerTextPaints.keyAt(n8), (TextPaint)ArticleViewer.footerTextPaints.valueAt(n8), typeface, typeface4, typeface3, typeface2);
        }
        for (int n9 = 0; n9 < ArticleViewer.embedPostCaptionTextPaints.size(); ++n9) {
            this.updateFontEntry(ArticleViewer.embedPostCaptionTextPaints.keyAt(n9), (TextPaint)ArticleViewer.embedPostCaptionTextPaints.valueAt(n9), typeface, typeface4, typeface3, typeface2);
        }
        for (int n10 = 0; n10 < ArticleViewer.relatedArticleTextPaints.size(); ++n10) {
            this.updateFontEntry(ArticleViewer.relatedArticleTextPaints.keyAt(n10), (TextPaint)ArticleViewer.relatedArticleTextPaints.valueAt(n10), typeface, typeface4, typeface3, typeface2);
        }
        int n11 = 0;
        int n12;
        while (true) {
            n12 = n;
            if (n11 >= ArticleViewer.detailsTextPaints.size()) {
                break;
            }
            this.updateFontEntry(ArticleViewer.detailsTextPaints.keyAt(n11), (TextPaint)ArticleViewer.detailsTextPaints.valueAt(n11), typeface, typeface4, typeface3, typeface2);
            ++n11;
        }
        while (n12 < ArticleViewer.tableTextPaints.size()) {
            this.updateFontEntry(ArticleViewer.tableTextPaints.keyAt(n12), (TextPaint)ArticleViewer.tableTextPaints.valueAt(n12), typeface, typeface4, typeface3, typeface2);
            ++n12;
        }
    }
    
    private void updatePaintSize() {
        final Context applicationContext = ApplicationLoader.applicationContext;
        int i = 0;
        applicationContext.getSharedPreferences("articles", 0).edit().putInt("font_size", this.selectedFontSize).commit();
        while (i < 2) {
            this.adapter[i].notifyDataSetChanged();
            ++i;
        }
    }
    
    private void updateVideoPlayerTime() {
        final VideoPlayer videoPlayer = this.videoPlayer;
        final Integer value = 0;
        String text;
        if (videoPlayer == null) {
            text = String.format("%02d:%02d / %02d:%02d", value, value, value, value);
        }
        else {
            final long n = videoPlayer.getCurrentPosition() / 1000L;
            final long n2 = this.videoPlayer.getDuration() / 1000L;
            if (n2 != -9223372036854775807L && n != -9223372036854775807L) {
                text = String.format("%02d:%02d / %02d:%02d", n / 60L, n % 60L, n2 / 60L, n2 % 60L);
            }
            else {
                text = String.format("%02d:%02d / %02d:%02d", value, value, value, value);
            }
        }
        if (!TextUtils.equals(this.videoPlayerTime.getText(), (CharSequence)text)) {
            this.videoPlayerTime.setText((CharSequence)text);
        }
    }
    
    private TLRPC.PageBlock wrapInTableBlock(final TLRPC.PageBlock pageBlock, final TLRPC.PageBlock pageBlock2) {
        if (pageBlock instanceof TL_pageBlockListItem) {
            final TL_pageBlockListItem tl_pageBlockListItem = (TL_pageBlockListItem)pageBlock;
            final TL_pageBlockListItem tl_pageBlockListItem2 = new TL_pageBlockListItem();
            tl_pageBlockListItem2.parent = tl_pageBlockListItem.parent;
            tl_pageBlockListItem2.blockItem = this.wrapInTableBlock(tl_pageBlockListItem.blockItem, pageBlock2);
            return tl_pageBlockListItem2;
        }
        if (pageBlock instanceof TL_pageBlockOrderedListItem) {
            final TL_pageBlockOrderedListItem tl_pageBlockOrderedListItem = (TL_pageBlockOrderedListItem)pageBlock;
            final TL_pageBlockOrderedListItem tl_pageBlockOrderedListItem2 = new TL_pageBlockOrderedListItem();
            tl_pageBlockOrderedListItem2.parent = tl_pageBlockOrderedListItem.parent;
            tl_pageBlockOrderedListItem2.blockItem = this.wrapInTableBlock(tl_pageBlockOrderedListItem.blockItem, pageBlock2);
            return tl_pageBlockOrderedListItem2;
        }
        return pageBlock2;
    }
    
    protected void cancelCheckLongPress() {
        this.checkingForLongPress = false;
        final CheckForLongPress pendingCheckForLongPress = this.pendingCheckForLongPress;
        if (pendingCheckForLongPress != null) {
            this.windowView.removeCallbacks((Runnable)pendingCheckForLongPress);
            this.pendingCheckForLongPress = null;
        }
        final CheckForTap pendingCheckForTap = this.pendingCheckForTap;
        if (pendingCheckForTap != null) {
            this.windowView.removeCallbacks((Runnable)pendingCheckForTap);
            this.pendingCheckForTap = null;
        }
    }
    
    public void close(final boolean b, final boolean b2) {
        if (this.parentActivity != null && this.isVisible) {
            if (!this.checkAnimation()) {
                if (this.fullscreenVideoContainer.getVisibility() == 0) {
                    if (this.customView != null) {
                        this.fullscreenVideoContainer.setVisibility(4);
                        this.customViewCallback.onCustomViewHidden();
                        this.fullscreenVideoContainer.removeView(this.customView);
                        this.customView = null;
                    }
                    else {
                        final WebPlayerView fullscreenedVideo = this.fullscreenedVideo;
                        if (fullscreenedVideo != null) {
                            fullscreenedVideo.exitFullscreen();
                        }
                    }
                    if (!b2) {
                        return;
                    }
                }
                if (this.isPhotoVisible) {
                    this.closePhoto(b2 ^ true);
                    if (!b2) {
                        return;
                    }
                }
                if (this.openUrlReqId != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.openUrlReqId, true);
                    this.openUrlReqId = 0;
                    this.showProgressView(true, false);
                }
                if (this.previewsReqId != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.previewsReqId, true);
                    this.previewsReqId = 0;
                    this.showProgressView(true, false);
                }
                this.saveCurrentPagePosition();
                if (b && !b2 && this.removeLastPageFromStack()) {
                    return;
                }
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStart);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needSetDayNightTheme);
                this.parentFragment = null;
                try {
                    if (this.visibleDialog != null) {
                        this.visibleDialog.dismiss();
                        this.visibleDialog = null;
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                final AnimatorSet set = new AnimatorSet();
                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.windowView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.containerView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.windowView, View.TRANSLATION_X, new float[] { 0.0f, (float)AndroidUtilities.dp(56.0f) }) });
                this.animationInProgress = 2;
                this.animationEndRunnable = new _$$Lambda$ArticleViewer$d55YwG7qykKR_6ZPmUqu81Dn_rc(this);
                set.setDuration(150L);
                set.setInterpolator((TimeInterpolator)this.interpolator);
                set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        if (ArticleViewer.this.animationEndRunnable != null) {
                            ArticleViewer.this.animationEndRunnable.run();
                            ArticleViewer.this.animationEndRunnable = null;
                        }
                    }
                });
                this.transitionAnimationStartTime = System.currentTimeMillis();
                if (Build$VERSION.SDK_INT >= 18) {
                    this.containerView.setLayerType(2, (Paint)null);
                }
                set.start();
            }
        }
    }
    
    public void closePhoto(final boolean b) {
        if (this.parentActivity != null && this.isPhotoVisible) {
            if (!this.checkPhotoAnimation()) {
                this.releasePlayer();
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidFailedLoad);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidLoad);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileLoadProgressChanged);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needSetDayNightTheme);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
                this.isActionBarVisible = false;
                final VelocityTracker velocityTracker = this.velocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    this.velocityTracker = null;
                }
                final PlaceProviderObject placeForPhoto = this.getPlaceForPhoto(this.currentMedia);
                if (b) {
                    this.photoAnimationInProgress = 1;
                    this.animatingImageView.setVisibility(0);
                    this.photoContainerView.invalidate();
                    final AnimatorSet set = new AnimatorSet();
                    final ViewGroup$LayoutParams layoutParams = this.animatingImageView.getLayoutParams();
                    int orientation = this.centerImage.getOrientation();
                    int animatedOrientation = 0;
                    Label_0198: {
                        if (placeForPhoto != null) {
                            final ImageReceiver imageReceiver = placeForPhoto.imageReceiver;
                            if (imageReceiver != null) {
                                animatedOrientation = imageReceiver.getAnimatedOrientation();
                                break Label_0198;
                            }
                        }
                        animatedOrientation = 0;
                    }
                    if (animatedOrientation != 0) {
                        orientation = animatedOrientation;
                    }
                    this.animatingImageView.setOrientation(orientation);
                    RectF drawRegion;
                    if (placeForPhoto != null) {
                        this.animatingImageView.setNeedRadius(placeForPhoto.radius != 0);
                        drawRegion = placeForPhoto.imageReceiver.getDrawRegion();
                        layoutParams.width = (int)drawRegion.width();
                        layoutParams.height = (int)drawRegion.height();
                        this.animatingImageView.setImageBitmap(placeForPhoto.thumb);
                    }
                    else {
                        this.animatingImageView.setNeedRadius(false);
                        layoutParams.width = this.centerImage.getImageWidth();
                        layoutParams.height = this.centerImage.getImageHeight();
                        this.animatingImageView.setImageBitmap(this.centerImage.getBitmapSafe());
                        drawRegion = null;
                    }
                    this.animatingImageView.setLayoutParams(layoutParams);
                    final Point displaySize = AndroidUtilities.displaySize;
                    float n = displaySize.x / (float)layoutParams.width;
                    final float n2 = (displaySize.y + AndroidUtilities.statusBarHeight) / (float)layoutParams.height;
                    if (n > n2) {
                        n = n2;
                    }
                    final float n3 = (float)layoutParams.width;
                    final float scale = this.scale;
                    final float n4 = (float)layoutParams.height;
                    float n6;
                    final float n5 = n6 = (AndroidUtilities.displaySize.x - n3 * scale * n) / 2.0f;
                    if (Build$VERSION.SDK_INT >= 21) {
                        final Object lastInsets = this.lastInsets;
                        n6 = n5;
                        if (lastInsets != null) {
                            n6 = n5 + ((WindowInsets)lastInsets).getSystemWindowInsetLeft();
                        }
                    }
                    final float n7 = (AndroidUtilities.displaySize.y + AndroidUtilities.statusBarHeight - n4 * scale * n) / 2.0f;
                    this.animatingImageView.setTranslationX(n6 + this.translationX);
                    this.animatingImageView.setTranslationY(n7 + this.translationY);
                    this.animatingImageView.setScaleX(this.scale * n);
                    this.animatingImageView.setScaleY(this.scale * n);
                    if (placeForPhoto != null) {
                        placeForPhoto.imageReceiver.setVisible(false, true);
                        int n8;
                        if (placeForPhoto.imageReceiver.isAspectFit()) {
                            n8 = 0;
                        }
                        else {
                            n8 = (int)Math.abs(drawRegion.left - placeForPhoto.imageReceiver.getImageX());
                        }
                        final int n9 = (int)Math.abs(drawRegion.top - placeForPhoto.imageReceiver.getImageY());
                        final int[] array = new int[2];
                        placeForPhoto.parentView.getLocationInWindow(array);
                        int a;
                        if ((a = (int)(array[1] - (placeForPhoto.viewY + drawRegion.top) + placeForPhoto.clipTopAddition)) < 0) {
                            a = 0;
                        }
                        final float n10 = (float)placeForPhoto.viewY;
                        final float top = drawRegion.top;
                        int a2;
                        if ((a2 = (int)(n10 + top + (drawRegion.bottom - top) - (array[1] + placeForPhoto.parentView.getHeight()) + placeForPhoto.clipBottomAddition)) < 0) {
                            a2 = 0;
                        }
                        final int max = Math.max(a, n9);
                        final int max2 = Math.max(a2, n9);
                        this.animationValues[0][0] = this.animatingImageView.getScaleX();
                        this.animationValues[0][1] = this.animatingImageView.getScaleY();
                        this.animationValues[0][2] = this.animatingImageView.getTranslationX();
                        this.animationValues[0][3] = this.animatingImageView.getTranslationY();
                        final float[][] animationValues = this.animationValues;
                        animationValues[0][4] = 0.0f;
                        animationValues[0][5] = 0.0f;
                        animationValues[0][6] = 0.0f;
                        animationValues[0][7] = 0.0f;
                        animationValues[0][8] = 0.0f;
                        animationValues[0][9] = 0.0f;
                        final float[] array2 = animationValues[1];
                        final float scale2 = placeForPhoto.scale;
                        array2[0] = scale2;
                        animationValues[1][1] = scale2;
                        animationValues[1][2] = placeForPhoto.viewX + drawRegion.left * scale2;
                        animationValues[1][3] = placeForPhoto.viewY + drawRegion.top * scale2;
                        final float[] array3 = animationValues[1];
                        final float n11 = (float)n8;
                        array3[4] = n11 * scale2;
                        animationValues[1][5] = max * scale2;
                        animationValues[1][6] = max2 * scale2;
                        animationValues[1][7] = (float)placeForPhoto.radius;
                        animationValues[1][8] = n9 * scale2;
                        animationValues[1][9] = n11 * scale2;
                        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.animatingImageView, "animationProgress", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofInt((Object)this.photoBackgroundDrawable, (Property)AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[] { 0 }), (Animator)ObjectAnimator.ofFloat((Object)this.actionBar, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.bottomLayout, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.captionTextView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.groupedPhotosListView, View.ALPHA, new float[] { 0.0f }) });
                    }
                    else {
                        int n12 = AndroidUtilities.displaySize.y + AndroidUtilities.statusBarHeight;
                        final ObjectAnimator ofInt = ObjectAnimator.ofInt((Object)this.photoBackgroundDrawable, (Property)AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[] { 0 });
                        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.animatingImageView, View.ALPHA, new float[] { 0.0f });
                        final ClippingImageView animatingImageView = this.animatingImageView;
                        final Property translation_Y = View.TRANSLATION_Y;
                        if (this.translationY < 0.0f) {
                            n12 = -n12;
                        }
                        set.playTogether(new Animator[] { (Animator)ofInt, (Animator)ofFloat, (Animator)ObjectAnimator.ofFloat((Object)animatingImageView, translation_Y, new float[] { (float)n12 }), (Animator)ObjectAnimator.ofFloat((Object)this.actionBar, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.bottomLayout, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.captionTextView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.groupedPhotosListView, View.ALPHA, new float[] { 0.0f }) });
                    }
                    this.photoAnimationEndRunnable = new _$$Lambda$ArticleViewer$bddzRYkreqx_hk8WrDDgX9mS_Fw(this, placeForPhoto);
                    set.setDuration(200L);
                    set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$24$PIC8GuTz6cB94k1nZ_KlW1gR_KY(this));
                        }
                    });
                    this.photoTransitionAnimationStartTime = System.currentTimeMillis();
                    if (Build$VERSION.SDK_INT >= 18) {
                        this.photoContainerView.setLayerType(2, (Paint)null);
                    }
                    set.start();
                }
                else {
                    this.photoContainerView.setVisibility(4);
                    this.photoContainerBackground.setVisibility(4);
                    this.photoAnimationInProgress = 0;
                    this.onPhotoClosed(placeForPhoto);
                    this.photoContainerView.setScaleX(1.0f);
                    this.photoContainerView.setScaleY(1.0f);
                }
                final AnimatedFileDrawable currentAnimation = this.currentAnimation;
                if (currentAnimation != null) {
                    currentAnimation.setSecondParentView(null);
                    this.currentAnimation = null;
                    this.centerImage.setImageBitmap((Drawable)null);
                }
            }
        }
    }
    
    public void collapse() {
        if (this.parentActivity != null && this.isVisible) {
            if (!this.checkAnimation()) {
                if (this.fullscreenVideoContainer.getVisibility() == 0) {
                    if (this.customView != null) {
                        this.fullscreenVideoContainer.setVisibility(4);
                        this.customViewCallback.onCustomViewHidden();
                        this.fullscreenVideoContainer.removeView(this.customView);
                        this.customView = null;
                    }
                    else {
                        final WebPlayerView fullscreenedVideo = this.fullscreenedVideo;
                        if (fullscreenedVideo != null) {
                            fullscreenedVideo.exitFullscreen();
                        }
                    }
                }
                if (this.isPhotoVisible) {
                    this.closePhoto(false);
                }
                try {
                    if (this.visibleDialog != null) {
                        this.visibleDialog.dismiss();
                        this.visibleDialog = null;
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                final AnimatorSet set = new AnimatorSet();
                final FrameLayout containerView = this.containerView;
                final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)containerView, View.TRANSLATION_X, new float[] { (float)(containerView.getMeasuredWidth() - AndroidUtilities.dp(56.0f)) });
                final FrameLayout containerView2 = this.containerView;
                final Property translation_Y = View.TRANSLATION_Y;
                final int currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
                int statusBarHeight;
                if (Build$VERSION.SDK_INT >= 21) {
                    statusBarHeight = AndroidUtilities.statusBarHeight;
                }
                else {
                    statusBarHeight = 0;
                }
                set.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ObjectAnimator.ofFloat((Object)containerView2, translation_Y, new float[] { (float)(currentActionBarHeight + statusBarHeight) }), (Animator)ObjectAnimator.ofFloat((Object)this.windowView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.listView[0], View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.listView[0], View.TRANSLATION_Y, new float[] { (float)(-AndroidUtilities.dp(56.0f)) }), (Animator)ObjectAnimator.ofFloat((Object)this.headerView, View.TRANSLATION_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.backButton, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.backButton, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.backButton, View.TRANSLATION_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shareContainer, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shareContainer, View.TRANSLATION_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shareContainer, View.SCALE_Y, new float[] { 1.0f }) });
                this.collapsed = true;
                this.animationInProgress = 2;
                this.animationEndRunnable = new _$$Lambda$ArticleViewer$o2CofdEeGng__h9rKJjob7RhMPo(this);
                set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                set.setDuration(250L);
                set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        if (ArticleViewer.this.animationEndRunnable != null) {
                            ArticleViewer.this.animationEndRunnable.run();
                            ArticleViewer.this.animationEndRunnable = null;
                        }
                    }
                });
                this.transitionAnimationStartTime = System.currentTimeMillis();
                if (Build$VERSION.SDK_INT >= 18) {
                    this.containerView.setLayerType(2, (Paint)null);
                }
                this.backDrawable.setRotation(1.0f, true);
                set.start();
            }
        }
    }
    
    public void destroyArticleViewer() {
        if (this.parentActivity != null) {
            if (this.windowView != null) {
                this.releasePlayer();
                try {
                    if (this.windowView.getParent() != null) {
                        ((WindowManager)this.parentActivity.getSystemService("window")).removeViewImmediate((View)this.windowView);
                    }
                    this.windowView = null;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                for (int i = 0; i < this.createdWebViews.size(); ++i) {
                    this.createdWebViews.get(i).destroyWebView(true);
                }
                this.createdWebViews.clear();
                try {
                    this.parentActivity.getWindow().clearFlags(128);
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
                final ImageReceiver.BitmapHolder currentThumb = this.currentThumb;
                if (currentThumb != null) {
                    currentThumb.release();
                    this.currentThumb = null;
                }
                this.animatingImageView.setImageBitmap(null);
                this.parentActivity = null;
                this.parentFragment = null;
                ArticleViewer.Instance = null;
            }
        }
    }
    
    @Override
    public void didReceivedNotification(int i, int j, final Object... array) {
        final int fileDidFailedLoad = NotificationCenter.fileDidFailedLoad;
        final int n = 0;
        final int n2 = 0;
        j = 0;
        if (i == fileDidFailedLoad) {
            final String anObject = (String)array[0];
            String[] currentFileNames;
            for (i = j; i < 3; ++i) {
                currentFileNames = this.currentFileNames;
                if (currentFileNames[i] != null && currentFileNames[i].equals(anObject)) {
                    this.radialProgressViews[i].setProgress(1.0f, true);
                    this.checkProgress(i, true);
                    break;
                }
            }
        }
        else if (i == NotificationCenter.fileDidLoad) {
            final String anObject2 = (String)array[0];
            i = 0;
            while (i < 3) {
                final String[] currentFileNames2 = this.currentFileNames;
                if (currentFileNames2[i] != null && currentFileNames2[i].equals(anObject2)) {
                    this.radialProgressViews[i].setProgress(1.0f, true);
                    this.checkProgress(i, true);
                    if (i == 0 && this.isMediaVideo(this.currentIndex)) {
                        this.onActionClick(false);
                        break;
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
        }
        else if (i == NotificationCenter.FileLoadProgressChanged) {
            final String anObject3 = (String)array[0];
            String[] currentFileNames3;
            for (i = n; i < 3; ++i) {
                currentFileNames3 = this.currentFileNames;
                if (currentFileNames3[i] != null && currentFileNames3[i].equals(anObject3)) {
                    this.radialProgressViews[i].setProgress((float)array[1], true);
                }
            }
        }
        else if (i == NotificationCenter.emojiDidLoad) {
            final TextView captionTextView = this.captionTextView;
            if (captionTextView != null) {
                captionTextView.invalidate();
            }
        }
        else if (i == NotificationCenter.needSetDayNightTheme) {
            if (this.nightModeEnabled && this.selectedColor != 2 && this.adapter != null) {
                this.updatePaintColors();
                for (i = n2; i < this.listView.length; ++i) {
                    this.adapter[i].notifyDataSetChanged();
                }
            }
        }
        else if (i == NotificationCenter.messagePlayingDidStart) {
            final MessageObject messageObject = (MessageObject)array[0];
            if (this.listView != null) {
                i = 0;
                while (true) {
                    final RecyclerListView[] listView = this.listView;
                    if (i >= listView.length) {
                        break;
                    }
                    int childCount;
                    View child;
                    for (childCount = listView[i].getChildCount(), j = 0; j < childCount; ++j) {
                        child = this.listView[i].getChildAt(j);
                        if (child instanceof BlockAudioCell) {
                            ((BlockAudioCell)child).updateButtonState(true);
                        }
                    }
                    ++i;
                }
            }
        }
        else if (i != NotificationCenter.messagePlayingDidReset && i != NotificationCenter.messagePlayingPlayStateChanged) {
            if (i == NotificationCenter.messagePlayingProgressDidChanged) {
                final Integer n3 = (Integer)array[0];
                if (this.listView != null) {
                    i = 0;
                    while (true) {
                        final RecyclerListView[] listView2 = this.listView;
                        if (i >= listView2.length) {
                            break;
                        }
                        int childCount2;
                        View child2;
                        BlockAudioCell blockAudioCell;
                        MessageObject messageObject2;
                        MessageObject playingMessageObject;
                        for (childCount2 = listView2[i].getChildCount(), j = 0; j < childCount2; ++j) {
                            child2 = this.listView[i].getChildAt(j);
                            if (child2 instanceof BlockAudioCell) {
                                blockAudioCell = (BlockAudioCell)child2;
                                messageObject2 = blockAudioCell.getMessageObject();
                                if (messageObject2 != null && messageObject2.getId() == n3) {
                                    playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                                    if (playingMessageObject != null) {
                                        messageObject2.audioProgress = playingMessageObject.audioProgress;
                                        messageObject2.audioProgressSec = playingMessageObject.audioProgressSec;
                                        messageObject2.audioPlayerDuration = playingMessageObject.audioPlayerDuration;
                                        blockAudioCell.updatePlayingMessageProgress();
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                        ++i;
                    }
                }
            }
        }
        else if (this.listView != null) {
            i = 0;
            while (true) {
                final RecyclerListView[] listView3 = this.listView;
                if (i >= listView3.length) {
                    break;
                }
                int childCount3;
                View child3;
                BlockAudioCell blockAudioCell2;
                for (childCount3 = listView3[i].getChildCount(), j = 0; j < childCount3; ++j) {
                    child3 = this.listView[i].getChildAt(j);
                    if (child3 instanceof BlockAudioCell) {
                        blockAudioCell2 = (BlockAudioCell)child3;
                        if (blockAudioCell2.getMessageObject() != null) {
                            blockAudioCell2.updateButtonState(true);
                        }
                    }
                }
                ++i;
            }
        }
    }
    
    @Keep
    public float getAnimationValue() {
        return this.animationValue;
    }
    
    public boolean isShowingImage(final TLRPC.PageBlock pageBlock) {
        return this.isPhotoVisible && !this.disableShowCheck && pageBlock != null && this.currentMedia == pageBlock;
    }
    
    public boolean isVisible() {
        return this.isVisible;
    }
    
    public boolean onDoubleTap(final MotionEvent motionEvent) {
        final boolean canZoom = this.canZoom;
        boolean b2;
        final boolean b = b2 = false;
        if (canZoom) {
            if (this.scale == 1.0f) {
                b2 = b;
                if (this.translationY != 0.0f) {
                    return b2;
                }
                if (this.translationX != 0.0f) {
                    b2 = b;
                    return b2;
                }
            }
            b2 = b;
            if (this.animationStartTime == 0L) {
                if (this.photoAnimationInProgress != 0) {
                    b2 = b;
                }
                else {
                    final float scale = this.scale;
                    b2 = true;
                    if (scale == 1.0f) {
                        float n = motionEvent.getX() - this.getContainerViewWidth() / 2 - (motionEvent.getX() - this.getContainerViewWidth() / 2 - this.translationX) * (3.0f / this.scale);
                        float n2 = motionEvent.getY() - this.getContainerViewHeight() / 2 - (motionEvent.getY() - this.getContainerViewHeight() / 2 - this.translationY) * (3.0f / this.scale);
                        this.updateMinMax(3.0f);
                        final float minX = this.minX;
                        if (n < minX) {
                            n = minX;
                        }
                        else {
                            final float maxX = this.maxX;
                            if (n > maxX) {
                                n = maxX;
                            }
                        }
                        final float minY = this.minY;
                        if (n2 < minY) {
                            n2 = minY;
                        }
                        else {
                            final float maxY = this.maxY;
                            if (n2 > maxY) {
                                n2 = maxY;
                            }
                        }
                        this.animateTo(3.0f, n, n2, true);
                    }
                    else {
                        this.animateTo(1.0f, 0.0f, 0.0f, true);
                    }
                    this.doubleTap = true;
                }
            }
        }
        return b2;
    }
    
    public boolean onDoubleTapEvent(final MotionEvent motionEvent) {
        return false;
    }
    
    public boolean onDown(final MotionEvent motionEvent) {
        return false;
    }
    
    public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float a, final float a2) {
        if (this.scale != 1.0f) {
            this.scroller.abortAnimation();
            this.scroller.fling(Math.round(this.translationX), Math.round(this.translationY), Math.round(a), Math.round(a2), (int)this.minX, (int)this.maxX, (int)this.minY, (int)this.maxY);
            this.photoContainerView.postInvalidate();
        }
        return false;
    }
    
    public void onLongPress(final MotionEvent motionEvent) {
    }
    
    public void onPause() {
        if (this.currentAnimation != null) {
            this.closePhoto(false);
        }
    }
    
    public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
        return false;
    }
    
    public void onShowPress(final MotionEvent motionEvent) {
    }
    
    public boolean onSingleTapConfirmed(final MotionEvent motionEvent) {
        if (this.discardTap) {
            return false;
        }
        final AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
        final boolean b = aspectRatioFrameLayout != null && aspectRatioFrameLayout.getVisibility() == 0;
        final RadialProgressView[] radialProgressViews = this.radialProgressViews;
        if (radialProgressViews[0] != null && this.photoContainerView != null && !b) {
            final int access$21900 = radialProgressViews[0].backgroundState;
            if (access$21900 > 0 && access$21900 <= 3) {
                final float x = motionEvent.getX();
                final float y = motionEvent.getY();
                if (x >= (this.getContainerViewWidth() - AndroidUtilities.dp(100.0f)) / 2.0f && x <= (this.getContainerViewWidth() + AndroidUtilities.dp(100.0f)) / 2.0f && y >= (this.getContainerViewHeight() - AndroidUtilities.dp(100.0f)) / 2.0f && y <= (this.getContainerViewHeight() + AndroidUtilities.dp(100.0f)) / 2.0f) {
                    this.onActionClick(true);
                    this.checkProgress(0, true);
                    return true;
                }
            }
        }
        this.toggleActionBar(this.isActionBarVisible ^ true, true);
        return true;
    }
    
    public boolean onSingleTapUp(final MotionEvent motionEvent) {
        return false;
    }
    
    public boolean open(final MessageObject messageObject) {
        return this.open(messageObject, null, null, true);
    }
    
    public boolean open(final TLRPC.TL_webPage tl_webPage, final String s) {
        return this.open(null, tl_webPage, s, true);
    }
    
    public boolean openPhoto(final TLRPC.PageBlock pageBlock) {
        if (this.pageSwitchAnimation != null || this.parentActivity == null || this.isPhotoVisible || this.checkPhotoAnimation() || pageBlock == null) {
            return false;
        }
        final PlaceProviderObject placeForPhoto = this.getPlaceForPhoto(pageBlock);
        if (placeForPhoto == null) {
            return false;
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidFailedLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileLoadProgressChanged);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.toggleActionBar(this.isPhotoVisible = true, false);
        this.actionBar.setAlpha(0.0f);
        this.bottomLayout.setAlpha(0.0f);
        this.captionTextView.setAlpha(0.0f);
        this.photoBackgroundDrawable.setAlpha(0);
        this.groupedPhotosListView.setAlpha(0.0f);
        this.photoContainerView.setAlpha(1.0f);
        this.disableShowCheck = true;
        this.photoAnimationInProgress = 1;
        if (pageBlock != null) {
            this.currentAnimation = placeForPhoto.imageReceiver.getAnimation();
        }
        int index = this.adapter[0].photoBlocks.indexOf(pageBlock);
        this.imagesArr.clear();
        if (pageBlock instanceof TLRPC.TL_pageBlockVideo && !this.isVideoBlock(pageBlock)) {
            this.imagesArr.add(pageBlock);
            index = 0;
        }
        else {
            this.imagesArr.addAll(this.adapter[0].photoBlocks);
        }
        this.onPhotoShow(index, placeForPhoto);
        final RectF drawRegion = placeForPhoto.imageReceiver.getDrawRegion();
        int orientation = placeForPhoto.imageReceiver.getOrientation();
        final int animatedOrientation = placeForPhoto.imageReceiver.getAnimatedOrientation();
        if (animatedOrientation != 0) {
            orientation = animatedOrientation;
        }
        this.animatingImageView.setVisibility(0);
        this.animatingImageView.setRadius(placeForPhoto.radius);
        this.animatingImageView.setOrientation(orientation);
        this.animatingImageView.setNeedRadius(placeForPhoto.radius != 0);
        this.animatingImageView.setImageBitmap(placeForPhoto.thumb);
        this.animatingImageView.setAlpha(1.0f);
        this.animatingImageView.setPivotX(0.0f);
        this.animatingImageView.setPivotY(0.0f);
        this.animatingImageView.setScaleX(placeForPhoto.scale);
        this.animatingImageView.setScaleY(placeForPhoto.scale);
        this.animatingImageView.setTranslationX(placeForPhoto.viewX + drawRegion.left * placeForPhoto.scale);
        this.animatingImageView.setTranslationY(placeForPhoto.viewY + drawRegion.top * placeForPhoto.scale);
        final ViewGroup$LayoutParams layoutParams = this.animatingImageView.getLayoutParams();
        layoutParams.width = (int)drawRegion.width();
        layoutParams.height = (int)drawRegion.height();
        this.animatingImageView.setLayoutParams(layoutParams);
        final Point displaySize = AndroidUtilities.displaySize;
        final float n = displaySize.x / (float)layoutParams.width;
        float n2 = (displaySize.y + AndroidUtilities.statusBarHeight) / (float)layoutParams.height;
        if (n <= n2) {
            n2 = n;
        }
        final float n3 = (float)layoutParams.width;
        final float n4 = (float)layoutParams.height;
        float n6;
        final float n5 = n6 = (AndroidUtilities.displaySize.x - n3 * n2) / 2.0f;
        if (Build$VERSION.SDK_INT >= 21) {
            final Object lastInsets = this.lastInsets;
            n6 = n5;
            if (lastInsets != null) {
                n6 = n5 + ((WindowInsets)lastInsets).getSystemWindowInsetLeft();
            }
        }
        final float n7 = (AndroidUtilities.displaySize.y + AndroidUtilities.statusBarHeight - n4 * n2) / 2.0f;
        int n8;
        if (placeForPhoto.imageReceiver.isAspectFit()) {
            n8 = 0;
        }
        else {
            n8 = (int)Math.abs(drawRegion.left - placeForPhoto.imageReceiver.getImageX());
        }
        final int n9 = (int)Math.abs(drawRegion.top - placeForPhoto.imageReceiver.getImageY());
        final int[] array = new int[2];
        placeForPhoto.parentView.getLocationInWindow(array);
        int a;
        if ((a = (int)(array[1] - (placeForPhoto.viewY + drawRegion.top) + placeForPhoto.clipTopAddition)) < 0) {
            a = 0;
        }
        int a2;
        if ((a2 = (int)(placeForPhoto.viewY + drawRegion.top + layoutParams.height - (array[1] + placeForPhoto.parentView.getHeight()) + placeForPhoto.clipBottomAddition)) < 0) {
            a2 = 0;
        }
        final int max = Math.max(a, n9);
        final int max2 = Math.max(a2, n9);
        this.animationValues[0][0] = this.animatingImageView.getScaleX();
        this.animationValues[0][1] = this.animatingImageView.getScaleY();
        this.animationValues[0][2] = this.animatingImageView.getTranslationX();
        this.animationValues[0][3] = this.animatingImageView.getTranslationY();
        final float[][] animationValues = this.animationValues;
        final float[] array2 = animationValues[0];
        final float n10 = (float)n8;
        final float scale = placeForPhoto.scale;
        array2[4] = n10 * scale;
        animationValues[0][5] = max * scale;
        animationValues[0][6] = max2 * scale;
        animationValues[0][7] = (float)this.animatingImageView.getRadius();
        final float[][] animationValues2 = this.animationValues;
        final float[] array3 = animationValues2[0];
        final float n11 = (float)n9;
        final float scale2 = placeForPhoto.scale;
        array3[8] = n11 * scale2;
        animationValues2[0][9] = n10 * scale2;
        animationValues2[1][0] = n2;
        animationValues2[1][1] = n2;
        animationValues2[1][2] = n6;
        animationValues2[1][3] = n7;
        animationValues2[1][4] = 0.0f;
        animationValues2[1][5] = 0.0f;
        animationValues2[1][6] = 0.0f;
        animationValues2[1][7] = 0.0f;
        animationValues2[1][8] = 0.0f;
        animationValues2[1][9] = 0.0f;
        this.photoContainerView.setVisibility(0);
        this.photoContainerBackground.setVisibility(0);
        this.animatingImageView.setAnimationProgress(0.0f);
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.animatingImageView, "animationProgress", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofInt((Object)this.photoBackgroundDrawable, (Property)AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[] { 0, 255 }), (Animator)ObjectAnimator.ofFloat((Object)this.actionBar, View.ALPHA, new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.bottomLayout, View.ALPHA, new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.captionTextView, View.ALPHA, new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.groupedPhotosListView, View.ALPHA, new float[] { 0.0f, 1.0f }) });
        this.photoAnimationEndRunnable = new _$$Lambda$ArticleViewer$8pPxKBf7qST2OsNyPVRs9Feebz8(this);
        set.setDuration(200L);
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$23$VcLMsIelHoGYSebxKyhxv0wAYFw(this));
            }
        });
        this.photoTransitionAnimationStartTime = System.currentTimeMillis();
        AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$mSJxhXEaOnXrvVt6G3mknr82Hqo(this, set));
        if (Build$VERSION.SDK_INT >= 18) {
            this.photoContainerView.setLayerType(2, (Paint)null);
        }
        this.photoBackgroundDrawable.drawRunnable = new _$$Lambda$ArticleViewer$8Z92wELF_pGETcAKmhLAU_sdkG8(this, placeForPhoto);
        return true;
    }
    
    @Keep
    public void setAnimationValue(final float animationValue) {
        this.animationValue = animationValue;
        this.photoContainerView.invalidate();
    }
    
    public void setParentActivity(final Activity parentActivity, final BaseFragment parentFragment) {
        this.parentFragment = parentFragment;
        this.currentAccount = UserConfig.selectedAccount;
        this.leftImage.setCurrentAccount(this.currentAccount);
        this.rightImage.setCurrentAccount(this.currentAccount);
        this.centerImage.setCurrentAccount(this.currentAccount);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needSetDayNightTheme);
        if (this.parentActivity == parentActivity) {
            this.updatePaintColors();
            return;
        }
        this.parentActivity = parentActivity;
        final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("articles", 0);
        this.selectedFontSize = sharedPreferences.getInt("font_size", 2);
        this.selectedFont = sharedPreferences.getInt("font_type", 0);
        this.selectedColor = sharedPreferences.getInt("font_color", 0);
        this.nightModeEnabled = sharedPreferences.getBoolean("nightModeEnabled", false);
        this.createPaint(false);
        this.backgroundPaint = new Paint();
        this.layerShadowDrawable = parentActivity.getResources().getDrawable(2131165521);
        this.slideDotDrawable = parentActivity.getResources().getDrawable(2131165828);
        this.slideDotBigDrawable = parentActivity.getResources().getDrawable(2131165827);
        this.scrimPaint = new Paint();
        (this.windowView = new WindowView((Context)parentActivity)).setWillNotDraw(false);
        this.windowView.setClipChildren(true);
        this.windowView.setFocusable(false);
        this.containerView = new FrameLayout(parentActivity) {
            protected boolean drawChild(final Canvas canvas, final View view, final long n) {
                if (ArticleViewer.this.windowView.movingPage) {
                    final int measuredWidth = this.getMeasuredWidth();
                    final int n2 = (int)ArticleViewer.this.listView[0].getTranslationX();
                    int n4 = 0;
                    int n5 = 0;
                    Label_0093: {
                        int n3;
                        if (view == ArticleViewer.this.listView[1]) {
                            n3 = n2;
                        }
                        else {
                            if (view == ArticleViewer.this.listView[0]) {
                                n4 = measuredWidth;
                                n5 = n2;
                                break Label_0093;
                            }
                            n3 = measuredWidth;
                        }
                        final int n6 = 0;
                        n4 = n3;
                        n5 = n6;
                    }
                    final int save = canvas.save();
                    canvas.clipRect(n5, 0, n4, this.getHeight());
                    final boolean drawChild = super.drawChild(canvas, view, n);
                    canvas.restoreToCount(save);
                    if (n2 != 0) {
                        if (view == ArticleViewer.this.listView[0]) {
                            final float max = Math.max(0.0f, Math.min((measuredWidth - n2) / (float)AndroidUtilities.dp(20.0f), 1.0f));
                            ArticleViewer.this.layerShadowDrawable.setBounds(n2 - ArticleViewer.this.layerShadowDrawable.getIntrinsicWidth(), view.getTop(), n2, view.getBottom());
                            ArticleViewer.this.layerShadowDrawable.setAlpha((int)(max * 255.0f));
                            ArticleViewer.this.layerShadowDrawable.draw(canvas);
                        }
                        else if (view == ArticleViewer.this.listView[1]) {
                            float min;
                            if ((min = Math.min(0.8f, (measuredWidth - n2) / (float)measuredWidth)) < 0.0f) {
                                min = 0.0f;
                            }
                            ArticleViewer.this.scrimPaint.setColor((int)(min * 153.0f) << 24);
                            canvas.drawRect((float)n5, 0.0f, (float)n4, (float)this.getHeight(), ArticleViewer.this.scrimPaint);
                        }
                    }
                    return drawChild;
                }
                return super.drawChild(canvas, view, n);
            }
        };
        this.windowView.addView((View)this.containerView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.containerView.setFitsSystemWindows(true);
        if (Build$VERSION.SDK_INT >= 21) {
            this.containerView.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new _$$Lambda$ArticleViewer$vp7UXKOYIsKBRVoLXvi04N7sU1U(this));
        }
        this.containerView.setSystemUiVisibility(1028);
        (this.photoContainerBackground = new View((Context)parentActivity)).setVisibility(4);
        this.photoContainerBackground.setBackgroundDrawable((Drawable)this.photoBackgroundDrawable);
        this.windowView.addView(this.photoContainerBackground, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        (this.animatingImageView = new ClippingImageView((Context)parentActivity)).setAnimationValues(this.animationValues);
        this.animatingImageView.setVisibility(8);
        this.windowView.addView((View)this.animatingImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f));
        (this.photoContainerView = (FrameLayoutDrawer)new FrameLayoutDrawer(parentActivity) {
            protected void onLayout(final boolean b, int n, int n2, int n3, int n4) {
                super.onLayout(b, n, n2, n3, n4);
                n = n4 - n2;
                n4 = n - ArticleViewer.this.captionTextView.getMeasuredHeight();
                n3 = n - ArticleViewer.this.groupedPhotosListView.getMeasuredHeight();
                n = n4;
                n2 = n3;
                if (ArticleViewer.this.bottomLayout.getVisibility() == 0) {
                    n = n4 - ArticleViewer.this.bottomLayout.getMeasuredHeight();
                    n2 = n3 - ArticleViewer.this.bottomLayout.getMeasuredHeight();
                }
                n3 = n;
                if (!ArticleViewer.this.groupedPhotosListView.currentPhotos.isEmpty()) {
                    n3 = n - ArticleViewer.this.groupedPhotosListView.getMeasuredHeight();
                }
                ArticleViewer.this.captionTextView.layout(0, n3, ArticleViewer.this.captionTextView.getMeasuredWidth(), ArticleViewer.this.captionTextView.getMeasuredHeight() + n3);
                ArticleViewer.this.captionTextViewNext.layout(0, n3, ArticleViewer.this.captionTextViewNext.getMeasuredWidth(), ArticleViewer.this.captionTextViewNext.getMeasuredHeight() + n3);
                ArticleViewer.this.groupedPhotosListView.layout(0, n2, ArticleViewer.this.groupedPhotosListView.getMeasuredWidth(), ArticleViewer.this.groupedPhotosListView.getMeasuredHeight() + n2);
            }
        }).setVisibility(4);
        this.photoContainerView.setWillNotDraw(false);
        this.windowView.addView((View)this.photoContainerView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        (this.fullscreenVideoContainer = new FrameLayout((Context)parentActivity)).setBackgroundColor(-16777216);
        this.fullscreenVideoContainer.setVisibility(4);
        this.windowView.addView((View)this.fullscreenVideoContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.fullscreenAspectRatioView = new AspectRatioFrameLayout((Context)parentActivity)).setVisibility(8);
        this.fullscreenVideoContainer.addView((View)this.fullscreenAspectRatioView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 17));
        this.fullscreenTextureView = new TextureView((Context)parentActivity);
        this.listView = new RecyclerListView[2];
        this.adapter = new WebpageAdapter[2];
        this.layoutManager = new LinearLayoutManager[2];
        int n = 0;
        while (true) {
            final RecyclerListView[] listView = this.listView;
            if (n >= listView.length) {
                break;
            }
            listView[n] = new RecyclerListView(parentActivity) {
                @Override
                public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                    if (ArticleViewer.this.pressedLinkOwnerLayout != null && ArticleViewer.this.pressedLink == null && (ArticleViewer.this.popupWindow == null || !ArticleViewer.this.popupWindow.isShowing()) && (motionEvent.getAction() == 1 || motionEvent.getAction() == 3)) {
                        ArticleViewer.this.pressedLink = null;
                        ArticleViewer.this.pressedLinkOwnerLayout = null;
                        ArticleViewer.this.pressedLinkOwnerView = null;
                    }
                    else if (ArticleViewer.this.pressedLinkOwnerLayout != null && ArticleViewer.this.pressedLink != null && motionEvent.getAction() == 1) {
                        final ArticleViewer this$0 = ArticleViewer.this;
                        this$0.checkLayoutForLinks(motionEvent, this$0.pressedLinkOwnerView, ArticleViewer.this.pressedLinkOwnerLayout, 0, 0);
                    }
                    return super.onInterceptTouchEvent(motionEvent);
                }
                
                @Override
                protected void onLayout(final boolean b, int i, int childCount, final int n, final int n2) {
                    super.onLayout(b, i, childCount, n, n2);
                    View child;
                    for (childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                        child = this.getChildAt(i);
                        if (child.getTag() instanceof Integer && (int)child.getTag() == 90 && child.getBottom() < this.getMeasuredHeight()) {
                            i = this.getMeasuredHeight();
                            child.layout(0, i - child.getMeasuredHeight(), child.getMeasuredWidth(), i);
                            break;
                        }
                    }
                }
                
                @Override
                public boolean onTouchEvent(final MotionEvent motionEvent) {
                    if (ArticleViewer.this.pressedLinkOwnerLayout != null && ArticleViewer.this.pressedLink == null && (ArticleViewer.this.popupWindow == null || !ArticleViewer.this.popupWindow.isShowing()) && (motionEvent.getAction() == 1 || motionEvent.getAction() == 3)) {
                        ArticleViewer.this.pressedLink = null;
                        ArticleViewer.this.pressedLinkOwnerLayout = null;
                        ArticleViewer.this.pressedLinkOwnerView = null;
                    }
                    return super.onTouchEvent(motionEvent);
                }
                
                public void setTranslationX(float translationX) {
                    super.setTranslationX(translationX);
                    if (ArticleViewer.this.windowView.movingPage) {
                        ArticleViewer.this.containerView.invalidate();
                        translationX /= this.getMeasuredWidth();
                        final ArticleViewer this$0 = ArticleViewer.this;
                        this$0.setCurrentHeaderHeight((int)(this$0.windowView.startMovingHeaderHeight + (AndroidUtilities.dp(56.0f) - ArticleViewer.this.windowView.startMovingHeaderHeight) * translationX));
                    }
                }
            };
            ((DefaultItemAnimator)this.listView[n].getItemAnimator()).setDelayAnimations(false);
            this.listView[n].setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager[n] = new LinearLayoutManager((Context)this.parentActivity, 1, false)));
            final WebpageAdapter[] adapter = this.adapter;
            final WebpageAdapter adapter2 = new WebpageAdapter((Context)this.parentActivity);
            adapter[n] = adapter2;
            this.listView[n].setAdapter(adapter2);
            this.listView[n].setClipToPadding(false);
            final RecyclerListView recyclerListView = this.listView[n];
            int visibility;
            if (n == 0) {
                visibility = 0;
            }
            else {
                visibility = 8;
            }
            recyclerListView.setVisibility(visibility);
            this.listView[n].setPadding(0, AndroidUtilities.dp(56.0f), 0, 0);
            this.listView[n].setTopGlowOffset(AndroidUtilities.dp(56.0f));
            this.containerView.addView((View)this.listView[n], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            this.listView[n].setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new _$$Lambda$ArticleViewer$gN3ysqedASIoOc6B_30_HuSHg6g(this));
            this.listView[n].setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$ArticleViewer$D7cgjgLrXEd0Ps_6XAby7DZGrbY(this, adapter2));
            this.listView[n].setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                    if (recyclerView.getChildCount() == 0) {
                        return;
                    }
                    ArticleViewer.this.headerView.invalidate();
                    ArticleViewer.this.checkScroll(n2);
                }
            });
            ++n;
        }
        this.headerPaint.setColor(-16777216);
        this.statusBarPaint.setColor(-16777216);
        this.headerProgressPaint.setColor(-14408666);
        (this.headerView = new FrameLayout(parentActivity) {
            protected void onDraw(final Canvas canvas) {
                final int measuredWidth = this.getMeasuredWidth();
                final int measuredHeight = this.getMeasuredHeight();
                final float n = (float)measuredWidth;
                final float n2 = (float)measuredHeight;
                canvas.drawRect(0.0f, 0.0f, n, n2, ArticleViewer.this.headerPaint);
                if (ArticleViewer.this.layoutManager == null) {
                    return;
                }
                final int firstVisibleItemPosition = ArticleViewer.this.layoutManager[0].findFirstVisibleItemPosition();
                final int lastVisibleItemPosition = ArticleViewer.this.layoutManager[0].findLastVisibleItemPosition();
                final int itemCount = ((RecyclerView.LayoutManager)ArticleViewer.this.layoutManager[0]).getItemCount();
                final int n3 = itemCount - 2;
                View view;
                if (lastVisibleItemPosition >= n3) {
                    view = ArticleViewer.this.layoutManager[0].findViewByPosition(n3);
                }
                else {
                    view = ArticleViewer.this.layoutManager[0].findViewByPosition(firstVisibleItemPosition);
                }
                if (view == null) {
                    return;
                }
                final float n4 = n / (itemCount - 1);
                ((RecyclerView.LayoutManager)ArticleViewer.this.layoutManager[0]).getChildCount();
                final float n5 = (float)view.getMeasuredHeight();
                float n6;
                if (lastVisibleItemPosition >= n3) {
                    n6 = (n3 - firstVisibleItemPosition) * n4 * (ArticleViewer.this.listView[0].getMeasuredHeight() - view.getTop()) / n5;
                }
                else {
                    n6 = (1.0f - (Math.min(0, view.getTop() - ArticleViewer.this.listView[0].getPaddingTop()) + n5) / n5) * n4;
                }
                canvas.drawRect(0.0f, 0.0f, firstVisibleItemPosition * n4 + n6, n2, ArticleViewer.this.headerProgressPaint);
            }
        }).setOnTouchListener((View$OnTouchListener)_$$Lambda$ArticleViewer$_HFJyvtR4MvWBLU_ptyI8bKyRKk.INSTANCE);
        this.headerView.setWillNotDraw(false);
        this.containerView.addView((View)this.headerView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 56.0f));
        (this.backButton = new ImageView((Context)parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
        (this.backDrawable = new BackDrawable(false)).setAnimationTime(200.0f);
        this.backDrawable.setColor(-5000269);
        this.backDrawable.setRotated(false);
        this.backButton.setImageDrawable((Drawable)this.backDrawable);
        this.backButton.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        this.headerView.addView((View)this.backButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(54, 56.0f));
        this.backButton.setOnClickListener((View$OnClickListener)new _$$Lambda$ArticleViewer$_BOkadygLKaLCM7xHgqDylRJDCI(this));
        this.backButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrGoBack", 2131558435));
        (this.titleTextView = new SimpleTextView((Context)parentActivity)).setGravity(19);
        this.titleTextView.setTextSize(20);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleTextView.setTextColor(-5000269);
        this.titleTextView.setPivotX(0.0f);
        this.titleTextView.setPivotY((float)AndroidUtilities.dp(28.0f));
        this.headerView.addView((View)this.titleTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 56.0f, 51, 72.0f, 0.0f, 96.0f, 0.0f));
        (this.lineProgressView = new LineProgressView((Context)parentActivity)).setProgressColor(-1);
        this.lineProgressView.setPivotX(0.0f);
        this.lineProgressView.setPivotY((float)AndroidUtilities.dp(2.0f));
        this.headerView.addView((View)this.lineProgressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 2.0f, 83, 0.0f, 0.0f, 0.0f, 1.0f));
        this.lineProgressTickRunnable = new _$$Lambda$ArticleViewer$ldm2w9awvjqof32LBYx2CGXsH0c(this);
        final LinearLayout linearLayout = new LinearLayout((Context)this.parentActivity);
        linearLayout.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
        linearLayout.setOrientation(1);
        for (int i = 0; i < 3; ++i) {
            this.colorCells[i] = new ColorCell((Context)this.parentActivity);
            if (i != 0) {
                if (i != 1) {
                    if (i == 2) {
                        this.colorCells[i].setTextAndColor(LocaleController.getString("ColorDark", 2131559123), -14474461);
                    }
                }
                else {
                    this.colorCells[i].setTextAndColor(LocaleController.getString("ColorSepia", 2131559128), -1382967);
                }
            }
            else {
                (this.nightModeImageView = new ImageView((Context)this.parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
                this.nightModeImageView.setImageResource(2131165607);
                final ImageView nightModeImageView = this.nightModeImageView;
                int n2;
                if (this.nightModeEnabled && this.selectedColor != 2) {
                    n2 = -15428119;
                }
                else {
                    n2 = -3355444;
                }
                nightModeImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(n2, PorterDuff$Mode.MULTIPLY));
                this.nightModeImageView.setBackgroundDrawable(Theme.createSelectorDrawable(251658240));
                final ColorCell colorCell = this.colorCells[i];
                final ImageView nightModeImageView2 = this.nightModeImageView;
                int n3;
                if (LocaleController.isRTL) {
                    n3 = 3;
                }
                else {
                    n3 = 5;
                }
                colorCell.addView((View)nightModeImageView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, n3 | 0x30));
                this.nightModeImageView.setOnClickListener((View$OnClickListener)new _$$Lambda$ArticleViewer$R_6rwlwqps7RWpLQ78R9g2TJAag(this));
                this.colorCells[i].setTextAndColor(LocaleController.getString("ColorWhite", 2131559132), -1);
            }
            this.colorCells[i].select(i == this.selectedColor);
            this.colorCells[i].setTag((Object)i);
            this.colorCells[i].setOnClickListener((View$OnClickListener)new _$$Lambda$ArticleViewer$H1Bc9M26tuZkWwbtuUTUyFFEPMA(this));
            linearLayout.addView((View)this.colorCells[i], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
        }
        this.updateNightModeButton();
        final View view = new View((Context)this.parentActivity);
        view.setBackgroundColor(-2039584);
        linearLayout.addView(view, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 1, 15.0f, 4.0f, 15.0f, 4.0f));
        view.getLayoutParams().height = 1;
        for (int j = 0; j < 2; ++j) {
            this.fontCells[j] = new FontCell((Context)this.parentActivity);
            if (j != 0) {
                if (j == 1) {
                    this.fontCells[j].setTextAndTypeface("Serif", Typeface.SERIF);
                }
            }
            else {
                this.fontCells[j].setTextAndTypeface("Roboto", Typeface.DEFAULT);
            }
            this.fontCells[j].select(j == this.selectedFont);
            this.fontCells[j].setTag((Object)j);
            this.fontCells[j].setOnClickListener((View$OnClickListener)new _$$Lambda$ArticleViewer$bbOV83__XjOP0aIG8YMQY9bn2Gc(this));
            linearLayout.addView((View)this.fontCells[j], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
        }
        final View view2 = new View((Context)this.parentActivity);
        view2.setBackgroundColor(-2039584);
        linearLayout.addView(view2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 1, 15.0f, 4.0f, 15.0f, 4.0f));
        view2.getLayoutParams().height = 1;
        final TextView textView = new TextView((Context)this.parentActivity);
        textView.setTextColor(-14606047);
        textView.setTextSize(1, 16.0f);
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        int n4;
        if (LocaleController.isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        textView.setGravity(n4 | 0x30);
        textView.setText((CharSequence)LocaleController.getString("FontSize", 2131559498));
        int n5;
        if (LocaleController.isRTL) {
            n5 = 5;
        }
        else {
            n5 = 3;
        }
        linearLayout.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n5 | 0x30, 17, 12, 17, 0));
        linearLayout.addView((View)new SizeChooseView((Context)this.parentActivity), (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 38, 0.0f, 0.0f, 0.0f, 1.0f));
        (this.settingsButton = new ActionBarMenuItem((Context)this.parentActivity, null, 1090519039, -1)).setPopupAnimationEnabled(false);
        this.settingsButton.setLayoutInScreen(true);
        final TextView textView2 = new TextView((Context)this.parentActivity);
        textView2.setTextSize(1, 18.0f);
        textView2.setText((CharSequence)"Aa");
        textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView2.setTextColor(-5000269);
        textView2.setGravity(17);
        textView2.setImportantForAccessibility(2);
        this.settingsButton.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.settingsButton.addSubItem((View)linearLayout, AndroidUtilities.dp(220.0f), -2);
        this.settingsButton.redrawPopup(-1);
        this.settingsButton.setContentDescription((CharSequence)LocaleController.getString("Settings", 2131560738));
        this.headerView.addView((View)this.settingsButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 56.0f, 53, 0.0f, 0.0f, 56.0f, 0.0f));
        this.shareContainer = new FrameLayout((Context)parentActivity);
        this.headerView.addView((View)this.shareContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 56, 53));
        this.shareContainer.setOnClickListener((View$OnClickListener)new _$$Lambda$ArticleViewer$T6B8FEUYsYpA0hIBLlSeg6NN1X0(this));
        (this.shareButton = new ImageView((Context)parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
        this.shareButton.setImageResource(2131165469);
        this.shareButton.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        this.shareButton.setContentDescription((CharSequence)LocaleController.getString("ShareFile", 2131560748));
        this.shareContainer.addView((View)this.shareButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 56.0f));
        (this.progressView = new ContextProgressView((Context)parentActivity, 2)).setVisibility(8);
        this.shareContainer.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 56.0f));
        this.windowLayoutParams = new WindowManager$LayoutParams();
        final WindowManager$LayoutParams windowLayoutParams = this.windowLayoutParams;
        windowLayoutParams.height = -1;
        windowLayoutParams.format = -3;
        windowLayoutParams.width = -1;
        windowLayoutParams.gravity = 51;
        windowLayoutParams.type = 99;
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 21) {
            windowLayoutParams.flags = -2147417848;
            if (sdk_INT >= 28) {
                windowLayoutParams.layoutInDisplayCutoutMode = 1;
            }
        }
        else {
            windowLayoutParams.flags = 8;
        }
        if (ArticleViewer.progressDrawables == null) {
            (ArticleViewer.progressDrawables = new Drawable[4])[0] = this.parentActivity.getResources().getDrawable(2131165357);
            ArticleViewer.progressDrawables[1] = this.parentActivity.getResources().getDrawable(2131165337);
            ArticleViewer.progressDrawables[2] = this.parentActivity.getResources().getDrawable(2131165537);
            ArticleViewer.progressDrawables[3] = this.parentActivity.getResources().getDrawable(2131165771);
        }
        this.scroller = new Scroller((Context)parentActivity);
        this.blackPaint.setColor(-16777216);
        (this.actionBar = new ActionBar((Context)parentActivity)).setBackgroundColor(2130706432);
        this.actionBar.setOccupyStatusBar(false);
        this.actionBar.setTitleColor(-1);
        this.actionBar.setItemsBackgroundColor(1090519039, false);
        this.actionBar.setBackButtonImage(2131165409);
        this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, 1, 1));
        this.photoContainerView.addView((View)this.actionBar, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
        this.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public boolean canOpenMenu() {
                final ArticleViewer this$0 = ArticleViewer.this;
                final File access$8800 = this$0.getMediaFile(this$0.currentIndex);
                return access$8800 != null && access$8800.exists();
            }
            
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ArticleViewer.this.closePhoto(true);
                }
                else if (n == 1) {
                    if (Build$VERSION.SDK_INT >= 23 && ArticleViewer.this.parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                        ArticleViewer.this.parentActivity.requestPermissions(new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" }, 4);
                        return;
                    }
                    final ArticleViewer this$0 = ArticleViewer.this;
                    final File access$8800 = this$0.getMediaFile(this$0.currentIndex);
                    if (access$8800 != null && access$8800.exists()) {
                        final String string = access$8800.toString();
                        final Activity access$8801 = ArticleViewer.this.parentActivity;
                        final ArticleViewer this$2 = ArticleViewer.this;
                        MediaController.saveFile(string, (Context)access$8801, this$2.isMediaVideo(this$2.currentIndex) ? 1 : 0, null, null);
                    }
                    else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)ArticleViewer.this.parentActivity);
                        builder.setTitle(LocaleController.getString("AppName", 2131558635));
                        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                        builder.setMessage(LocaleController.getString("PleaseDownload", 2131560454));
                        ArticleViewer.this.showDialog(builder.create());
                    }
                }
                else if (n == 2) {
                    ArticleViewer.this.onSharePressed();
                }
                else if (n == 3) {
                    try {
                        AndroidUtilities.openForView(ArticleViewer.this.getMedia(ArticleViewer.this.currentIndex), ArticleViewer.this.parentActivity);
                        ArticleViewer.this.closePhoto(false);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
            }
        });
        final ActionBarMenu menu = this.actionBar.createMenu();
        menu.addItem(2, 2131165818);
        (this.menuItem = menu.addItem(0, 2131165416)).setLayoutInScreen(true);
        this.menuItem.addSubItem(3, 2131165649, LocaleController.getString("OpenInExternalApp", 2131560116)).setColors(-328966, -328966);
        this.menuItem.addSubItem(1, 2131165628, LocaleController.getString("SaveToGallery", 2131560630)).setColors(-328966, -328966);
        this.menuItem.redrawPopup(-115203550);
        (this.bottomLayout = new FrameLayout((Context)this.parentActivity)).setBackgroundColor(2130706432);
        this.photoContainerView.addView((View)this.bottomLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        this.groupedPhotosListView = new GroupedPhotosListView((Context)this.parentActivity);
        this.photoContainerView.addView((View)this.groupedPhotosListView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 62.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
        this.groupedPhotosListView.setDelegate((GroupedPhotosListView.GroupedPhotosListViewDelegate)new GroupedPhotosListView.GroupedPhotosListViewDelegate() {
            @Override
            public int getAvatarsDialogId() {
                return 0;
            }
            
            @Override
            public int getCurrentAccount() {
                return ArticleViewer.this.currentAccount;
            }
            
            @Override
            public int getCurrentIndex() {
                return ArticleViewer.this.currentIndex;
            }
            
            @Override
            public ArrayList<MessageObject> getImagesArr() {
                return null;
            }
            
            @Override
            public ArrayList<ImageLocation> getImagesArrLocations() {
                return null;
            }
            
            @Override
            public ArrayList<TLRPC.PageBlock> getPageBlockArr() {
                return ArticleViewer.this.imagesArr;
            }
            
            @Override
            public Object getParentObject() {
                return ArticleViewer.this.currentPage;
            }
            
            @Override
            public int getSlideshowMessageId() {
                return 0;
            }
            
            @Override
            public void setCurrentIndex(final int n) {
                ArticleViewer.this.currentIndex = -1;
                if (ArticleViewer.this.currentThumb != null) {
                    ArticleViewer.this.currentThumb.release();
                    ArticleViewer.this.currentThumb = null;
                }
                ArticleViewer.this.setImageIndex(n, true);
            }
        });
        (this.captionTextViewNext = new TextView((Context)parentActivity)).setMaxLines(10);
        this.captionTextViewNext.setBackgroundColor(2130706432);
        this.captionTextViewNext.setMovementMethod((MovementMethod)new PhotoViewer.LinkMovementMethodMy());
        this.captionTextViewNext.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(8.0f));
        this.captionTextViewNext.setLinkTextColor(-1);
        this.captionTextViewNext.setTextColor(-1);
        this.captionTextViewNext.setHighlightColor(872415231);
        this.captionTextViewNext.setGravity(19);
        this.captionTextViewNext.setTextSize(1, 16.0f);
        this.captionTextViewNext.setVisibility(8);
        this.photoContainerView.addView((View)this.captionTextViewNext, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2, 83));
        (this.captionTextView = new TextView((Context)parentActivity)).setMaxLines(10);
        this.captionTextView.setBackgroundColor(2130706432);
        this.captionTextView.setMovementMethod((MovementMethod)new PhotoViewer.LinkMovementMethodMy());
        this.captionTextView.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(8.0f));
        this.captionTextView.setLinkTextColor(-1);
        this.captionTextView.setTextColor(-1);
        this.captionTextView.setHighlightColor(872415231);
        this.captionTextView.setGravity(19);
        this.captionTextView.setTextSize(1, 16.0f);
        this.captionTextView.setVisibility(8);
        this.photoContainerView.addView((View)this.captionTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2, 83));
        (this.radialProgressViews[0] = new RadialProgressView((Context)parentActivity, (View)this.photoContainerView)).setBackgroundState(0, false);
        (this.radialProgressViews[1] = new RadialProgressView((Context)parentActivity, (View)this.photoContainerView)).setBackgroundState(0, false);
        (this.radialProgressViews[2] = new RadialProgressView((Context)parentActivity, (View)this.photoContainerView)).setBackgroundState(0, false);
        (this.videoPlayerSeekbar = new SeekBar((Context)parentActivity)).setColors(1728053247, 1728053247, -2764585, -1, -1);
        this.videoPlayerSeekbar.setDelegate((SeekBar.SeekBarDelegate)new _$$Lambda$ArticleViewer$_GVmz26hlBkXHajDH6vxhTab9Js(this));
        (this.videoPlayerControlFrameLayout = new FrameLayout(parentActivity) {
            protected void onDraw(final Canvas canvas) {
                canvas.save();
                canvas.translate((float)AndroidUtilities.dp(48.0f), 0.0f);
                ArticleViewer.this.videoPlayerSeekbar.draw(canvas);
                canvas.restore();
            }
            
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                float progress;
                if (ArticleViewer.this.videoPlayer != null) {
                    progress = ArticleViewer.this.videoPlayer.getCurrentPosition() / (float)ArticleViewer.this.videoPlayer.getDuration();
                }
                else {
                    progress = 0.0f;
                }
                ArticleViewer.this.videoPlayerSeekbar.setProgress(progress);
            }
            
            protected void onMeasure(int n, final int n2) {
                super.onMeasure(n, n2);
                final VideoPlayer access$9700 = ArticleViewer.this.videoPlayer;
                long duration;
                final long n3 = duration = 0L;
                if (access$9700 != null) {
                    duration = ArticleViewer.this.videoPlayer.getDuration();
                    if (duration == -9223372036854775807L) {
                        duration = n3;
                    }
                }
                final long n4 = duration / 1000L;
                final TextPaint paint = ArticleViewer.this.videoPlayerTime.getPaint();
                final long n5 = n4 / 60L;
                final long n6 = n4 % 60L;
                n = (int)Math.ceil(paint.measureText(String.format("%02d:%02d / %02d:%02d", n5, n6, n5, n6)));
                ArticleViewer.this.videoPlayerSeekbar.setSize(this.getMeasuredWidth() - AndroidUtilities.dp(64.0f) - n, this.getMeasuredHeight());
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                motionEvent.getX();
                motionEvent.getY();
                if (ArticleViewer.this.videoPlayerSeekbar.onTouch(motionEvent.getAction(), motionEvent.getX() - AndroidUtilities.dp(48.0f), motionEvent.getY())) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                    this.invalidate();
                    return true;
                }
                return super.onTouchEvent(motionEvent);
            }
        }).setWillNotDraw(false);
        this.bottomLayout.addView((View)this.videoPlayerControlFrameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        (this.videoPlayButton = new ImageView((Context)parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
        this.videoPlayerControlFrameLayout.addView((View)this.videoPlayButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 51));
        this.videoPlayButton.setOnClickListener((View$OnClickListener)new _$$Lambda$ArticleViewer$lPU_BVNRUajP3efspuXk_ru9iJ0(this));
        (this.videoPlayerTime = new TextView((Context)parentActivity)).setTextColor(-1);
        this.videoPlayerTime.setGravity(16);
        this.videoPlayerTime.setTextSize(1, 13.0f);
        this.videoPlayerControlFrameLayout.addView((View)this.videoPlayerTime, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, 53, 0.0f, 0.0f, 8.0f, 0.0f));
        (this.gestureDetector = new GestureDetector((Context)parentActivity, (GestureDetector$OnGestureListener)this)).setOnDoubleTapListener((GestureDetector$OnDoubleTapListener)this);
        this.centerImage.setParentView((View)this.photoContainerView);
        this.centerImage.setCrossfadeAlpha((byte)2);
        this.centerImage.setInvalidateAll(true);
        this.leftImage.setParentView((View)this.photoContainerView);
        this.leftImage.setCrossfadeAlpha((byte)2);
        this.leftImage.setInvalidateAll(true);
        this.rightImage.setParentView((View)this.photoContainerView);
        this.rightImage.setCrossfadeAlpha((byte)2);
        this.rightImage.setInvalidateAll(true);
        this.updatePaintColors();
    }
    
    public void showDialog(final Dialog visibleDialog) {
        if (this.parentActivity == null) {
            return;
        }
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        try {
            (this.visibleDialog = visibleDialog).setCanceledOnTouchOutside(true);
            this.visibleDialog.setOnDismissListener((DialogInterface$OnDismissListener)new _$$Lambda$ArticleViewer$akGDRTg_CpAhs2_Lmpycw0irNsA(this));
            visibleDialog.show();
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
    }
    
    protected void startCheckLongPress() {
        if (this.checkingForLongPress) {
            return;
        }
        this.checkingForLongPress = true;
        if (this.pendingCheckForTap == null) {
            this.pendingCheckForTap = new CheckForTap();
        }
        this.windowView.postDelayed((Runnable)this.pendingCheckForTap, (long)ViewConfiguration.getTapTimeout());
    }
    
    public void uncollapse() {
        if (this.parentActivity != null && this.isVisible) {
            if (!this.checkAnimation()) {
                final AnimatorSet set = new AnimatorSet();
                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.containerView, View.TRANSLATION_X, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.containerView, View.TRANSLATION_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.windowView, View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.listView[0], View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.listView[0], View.TRANSLATION_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.headerView, View.TRANSLATION_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.backButton, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.backButton, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.backButton, View.TRANSLATION_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shareContainer, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shareContainer, View.TRANSLATION_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shareContainer, View.SCALE_Y, new float[] { 1.0f }) });
                this.collapsed = false;
                this.animationInProgress = 2;
                this.animationEndRunnable = new _$$Lambda$ArticleViewer$C6ZUTa2rGrDYlXYood81SR54cQg(this);
                set.setDuration(250L);
                set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        if (ArticleViewer.this.animationEndRunnable != null) {
                            ArticleViewer.this.animationEndRunnable.run();
                            ArticleViewer.this.animationEndRunnable = null;
                        }
                    }
                });
                this.transitionAnimationStartTime = System.currentTimeMillis();
                if (Build$VERSION.SDK_INT >= 18) {
                    this.containerView.setLayerType(2, (Paint)null);
                }
                this.backDrawable.setRotation(0.0f, true);
                set.start();
            }
        }
    }
    
    private class BlockAudioCell extends View implements FileDownloadProgressListener
    {
        private int TAG;
        private int buttonPressed;
        private int buttonState;
        private int buttonX;
        private int buttonY;
        private DrawingText captionLayout;
        private DrawingText creditLayout;
        private int creditOffset;
        private TLRPC.TL_pageBlockAudio currentBlock;
        private TLRPC.Document currentDocument;
        private MessageObject currentMessageObject;
        private StaticLayout durationLayout;
        private boolean isFirst;
        private boolean isLast;
        private String lastTimeString;
        private WebpageAdapter parentAdapter;
        private RadialProgress2 radialProgress;
        private SeekBar seekBar;
        private int seekBarX;
        private int seekBarY;
        private int textX;
        private int textY;
        private StaticLayout titleLayout;
        
        public BlockAudioCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textY = AndroidUtilities.dp(54.0f);
            this.parentAdapter = parentAdapter;
            (this.radialProgress = new RadialProgress2(this)).setBackgroundStroke(AndroidUtilities.dp(3.0f));
            this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
            this.TAG = DownloadController.getInstance(ArticleViewer.this.currentAccount).generateObserverTag();
            (this.seekBar = new SeekBar(context)).setDelegate((SeekBar.SeekBarDelegate)new _$$Lambda$ArticleViewer$BlockAudioCell$WgP383_edJ263u4ZKo_pJ9bitM4(this));
        }
        
        private void didPressedButton(final boolean b) {
            final int buttonState = this.buttonState;
            if (buttonState == 0) {
                if (MediaController.getInstance().setPlaylist(this.parentAdapter.audioMessages, this.currentMessageObject, false)) {
                    this.buttonState = 1;
                    this.radialProgress.setIcon(this.getIconForCurrentState(), false, b);
                    this.invalidate();
                }
            }
            else if (buttonState == 1) {
                if (MediaController.getInstance().pauseMessage(this.currentMessageObject)) {
                    this.buttonState = 0;
                    this.radialProgress.setIcon(this.getIconForCurrentState(), false, b);
                    this.invalidate();
                }
            }
            else if (buttonState == 2) {
                this.radialProgress.setProgress(0.0f, false);
                FileLoader.getInstance(ArticleViewer.this.currentAccount).loadFile(this.currentDocument, ArticleViewer.this.currentPage, 1, 1);
                this.buttonState = 3;
                this.radialProgress.setIcon(this.getIconForCurrentState(), true, b);
                this.invalidate();
            }
            else if (buttonState == 3) {
                FileLoader.getInstance(ArticleViewer.this.currentAccount).cancelLoadFile(this.currentDocument);
                this.buttonState = 2;
                this.radialProgress.setIcon(this.getIconForCurrentState(), false, b);
                this.invalidate();
            }
        }
        
        private int getIconForCurrentState() {
            final int buttonState = this.buttonState;
            if (buttonState == 1) {
                return 1;
            }
            if (buttonState == 2) {
                return 2;
            }
            if (buttonState == 3) {
                return 3;
            }
            return 0;
        }
        
        public MessageObject getMessageObject() {
            return this.currentMessageObject;
        }
        
        public int getObserverTag() {
            return this.TAG;
        }
        
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.updateButtonState(false);
        }
        
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            this.radialProgress.setColors(ArticleViewer.this.getTextColor(), ArticleViewer.this.getTextColor(), ArticleViewer.this.getTextColor(), ArticleViewer.this.getTextColor());
            this.radialProgress.draw(canvas);
            canvas.save();
            canvas.translate((float)this.seekBarX, (float)this.seekBarY);
            this.seekBar.draw(canvas);
            canvas.restore();
            if (this.durationLayout != null) {
                canvas.save();
                canvas.translate((float)(this.buttonX + AndroidUtilities.dp(54.0f)), (float)(this.seekBarY + AndroidUtilities.dp(6.0f)));
                this.durationLayout.draw(canvas);
                canvas.restore();
            }
            if (this.titleLayout != null) {
                canvas.save();
                canvas.translate((float)(this.buttonX + AndroidUtilities.dp(54.0f)), (float)(this.seekBarY - AndroidUtilities.dp(16.0f)));
                this.titleLayout.draw(canvas);
                canvas.restore();
            }
            if (this.captionLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.captionLayout.draw(canvas);
                canvas.restore();
            }
            if (this.creditLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)(this.textY + this.creditOffset));
                this.creditLayout.draw(canvas);
                canvas.restore();
            }
            if (this.currentBlock.level > 0) {
                final float n = (float)AndroidUtilities.dp(18.0f);
                final float n2 = (float)AndroidUtilities.dp(20.0f);
                final int measuredHeight = this.getMeasuredHeight();
                int dp;
                if (this.currentBlock.bottom) {
                    dp = AndroidUtilities.dp(6.0f);
                }
                else {
                    dp = 0;
                }
                canvas.drawRect(n, 0.0f, n2, (float)(measuredHeight - dp), ArticleViewer.quoteLinePaint);
            }
        }
        
        public void onFailedDownload(final String s, final boolean b) {
            this.updateButtonState(true);
        }
        
        @SuppressLint({ "DrawAllocation", "NewApi" })
        protected void onMeasure(int n, int dp) {
            final int size = View$MeasureSpec.getSize(n);
            dp = AndroidUtilities.dp(54.0f);
            final TLRPC.TL_pageBlockAudio currentBlock = this.currentBlock;
            if (currentBlock != null) {
                n = currentBlock.level;
                if (n > 0) {
                    this.textX = AndroidUtilities.dp((float)(n * 14)) + AndroidUtilities.dp(18.0f);
                }
                else {
                    this.textX = AndroidUtilities.dp(18.0f);
                }
                final int n2 = size - this.textX - AndroidUtilities.dp(18.0f);
                final int dp2 = AndroidUtilities.dp(44.0f);
                this.buttonX = AndroidUtilities.dp(16.0f);
                this.buttonY = AndroidUtilities.dp(5.0f);
                final RadialProgress2 radialProgress = this.radialProgress;
                n = this.buttonX;
                final int buttonY = this.buttonY;
                radialProgress.setProgressRect(n, buttonY, n + dp2, buttonY + dp2);
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.TL_pageBlockAudio currentBlock2 = this.currentBlock;
                this.captionLayout = this$0.createLayoutForText(this, null, currentBlock2.caption.text, n2, currentBlock2, this.parentAdapter);
                n = dp;
                if (this.captionLayout != null) {
                    this.creditOffset = AndroidUtilities.dp(4.0f) + this.captionLayout.getHeight();
                    n = dp + (this.creditOffset + AndroidUtilities.dp(4.0f));
                }
                dp = n;
                final ArticleViewer this$2 = ArticleViewer.this;
                final TLRPC.TL_pageBlockAudio currentBlock3 = this.currentBlock;
                final TLRPC.RichText credit = currentBlock3.caption.credit;
                Layout$Alignment layout$Alignment;
                if (this$2.isRtl) {
                    layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                }
                else {
                    layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                }
                this.creditLayout = this$2.createLayoutForText(this, null, credit, n2, currentBlock3, layout$Alignment, this.parentAdapter);
                n = dp;
                if (this.creditLayout != null) {
                    n = dp + (AndroidUtilities.dp(4.0f) + this.creditLayout.getHeight());
                }
                dp = n;
                if (!this.isFirst) {
                    dp = n;
                    if (this.currentBlock.level <= 0) {
                        dp = n + AndroidUtilities.dp(8.0f);
                    }
                }
                final String musicAuthor = this.currentMessageObject.getMusicAuthor(false);
                final String musicTitle = this.currentMessageObject.getMusicTitle(false);
                this.seekBarX = this.buttonX + AndroidUtilities.dp(50.0f) + dp2;
                n = size - this.seekBarX - AndroidUtilities.dp(18.0f);
                if (TextUtils.isEmpty((CharSequence)musicTitle) && TextUtils.isEmpty((CharSequence)musicAuthor)) {
                    this.titleLayout = null;
                    this.seekBarY = this.buttonY + (dp2 - AndroidUtilities.dp(30.0f)) / 2;
                }
                else {
                    SpannableStringBuilder spannableStringBuilder;
                    if (!TextUtils.isEmpty((CharSequence)musicTitle) && !TextUtils.isEmpty((CharSequence)musicAuthor)) {
                        spannableStringBuilder = new SpannableStringBuilder((CharSequence)String.format("%s - %s", musicAuthor, musicTitle));
                    }
                    else if (!TextUtils.isEmpty((CharSequence)musicTitle)) {
                        spannableStringBuilder = new SpannableStringBuilder((CharSequence)musicTitle);
                    }
                    else {
                        spannableStringBuilder = new SpannableStringBuilder((CharSequence)musicAuthor);
                    }
                    if (!TextUtils.isEmpty((CharSequence)musicAuthor)) {
                        spannableStringBuilder.setSpan((Object)new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), 0, musicAuthor.length(), 18);
                    }
                    this.titleLayout = new StaticLayout(TextUtils.ellipsize((CharSequence)spannableStringBuilder, Theme.chat_audioTitlePaint, (float)n, TextUtils$TruncateAt.END), ArticleViewer.audioTimePaint, n, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.seekBarY = this.buttonY + (dp2 - AndroidUtilities.dp(30.0f)) / 2 + AndroidUtilities.dp(11.0f);
                }
                this.seekBar.setSize(n, AndroidUtilities.dp(30.0f));
            }
            else {
                dp = 1;
            }
            this.setMeasuredDimension(size, dp);
            this.updatePlayingMessageProgress();
        }
        
        public void onProgressDownload(final String s, final float n) {
            this.radialProgress.setProgress(n, true);
            if (this.buttonState != 3) {
                this.updateButtonState(true);
            }
        }
        
        public void onProgressUpload(final String s, final float n, final boolean b) {
        }
        
        public void onSuccessDownload(final String s) {
            this.radialProgress.setProgress(1.0f, true);
            this.updateButtonState(true);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final float x = motionEvent.getX();
            final float y = motionEvent.getY();
            final boolean onTouch = this.seekBar.onTouch(motionEvent.getAction(), motionEvent.getX() - this.seekBarX, motionEvent.getY() - this.seekBarY);
            final boolean b = true;
            if (onTouch) {
                if (motionEvent.getAction() == 0) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
                this.invalidate();
                return true;
            }
            Label_0217: {
                if (motionEvent.getAction() == 0) {
                    Label_0154: {
                        if (this.buttonState != -1) {
                            final int buttonX = this.buttonX;
                            if (x >= buttonX && x <= buttonX + AndroidUtilities.dp(48.0f)) {
                                final int buttonY = this.buttonY;
                                if (y >= buttonY && y <= buttonY + AndroidUtilities.dp(48.0f)) {
                                    break Label_0154;
                                }
                            }
                        }
                        if (this.buttonState != 0) {
                            break Label_0217;
                        }
                    }
                    this.buttonPressed = 1;
                    this.invalidate();
                }
                else if (motionEvent.getAction() == 1) {
                    if (this.buttonPressed == 1) {
                        this.playSoundEffect(this.buttonPressed = 0);
                        this.didPressedButton(true);
                        this.invalidate();
                    }
                }
                else if (motionEvent.getAction() == 3) {
                    this.buttonPressed = 0;
                }
            }
            boolean b2 = b;
            if (this.buttonPressed == 0) {
                b2 = b;
                if (!ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.captionLayout, this.textX, this.textY)) {
                    b2 = b;
                    if (!ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.creditLayout, this.textX, this.textY + this.creditOffset)) {
                        b2 = (super.onTouchEvent(motionEvent) && b);
                    }
                }
            }
            return b2;
        }
        
        public void setBlock(final TLRPC.TL_pageBlockAudio currentBlock, final boolean isFirst, final boolean isLast) {
            this.currentBlock = currentBlock;
            this.currentMessageObject = this.parentAdapter.audioBlocks.get(this.currentBlock);
            this.currentDocument = this.currentMessageObject.getDocument();
            this.isFirst = isFirst;
            this.isLast = isLast;
            this.radialProgress.setProgressColor(ArticleViewer.this.getTextColor());
            this.seekBar.setColors(ArticleViewer.this.getTextColor() & 0x3FFFFFFF, ArticleViewer.this.getTextColor() & 0x3FFFFFFF, ArticleViewer.this.getTextColor(), ArticleViewer.this.getTextColor(), ArticleViewer.this.getTextColor());
            this.updateButtonState(false);
            this.requestLayout();
        }
        
        public void updateButtonState(final boolean b) {
            final String attachFileName = FileLoader.getAttachFileName(this.currentDocument);
            final boolean exists = FileLoader.getPathToAttach(this.currentDocument, true).exists();
            if (TextUtils.isEmpty((CharSequence)attachFileName)) {
                this.radialProgress.setIcon(4, false, false);
                return;
            }
            if (exists) {
                DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
                final boolean playingMessage = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
                if (playingMessage && (!playingMessage || !MediaController.getInstance().isMessagePaused())) {
                    this.buttonState = 1;
                }
                else {
                    this.buttonState = 0;
                }
                this.radialProgress.setIcon(this.getIconForCurrentState(), false, b);
            }
            else {
                DownloadController.getInstance(ArticleViewer.this.currentAccount).addLoadingFileObserver(attachFileName, null, (DownloadController.FileDownloadProgressListener)this);
                if (!FileLoader.getInstance(ArticleViewer.this.currentAccount).isLoadingFile(attachFileName)) {
                    this.buttonState = 2;
                    this.radialProgress.setProgress(0.0f, b);
                    this.radialProgress.setIcon(this.getIconForCurrentState(), false, b);
                }
                else {
                    this.buttonState = 3;
                    final Float fileProgress = ImageLoader.getInstance().getFileProgress(attachFileName);
                    if (fileProgress != null) {
                        this.radialProgress.setProgress(fileProgress, b);
                    }
                    else {
                        this.radialProgress.setProgress(0.0f, b);
                    }
                    this.radialProgress.setIcon(this.getIconForCurrentState(), true, b);
                }
            }
            this.updatePlayingMessageProgress();
        }
        
        public void updatePlayingMessageProgress() {
            if (this.currentDocument != null) {
                if (this.currentMessageObject != null) {
                    if (!this.seekBar.isDragging()) {
                        this.seekBar.setProgress(this.currentMessageObject.audioProgress);
                    }
                    int n = 0;
                    Label_0119: {
                        if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                            n = this.currentMessageObject.audioProgressSec;
                        }
                        else {
                            for (int i = 0; i < this.currentDocument.attributes.size(); ++i) {
                                final TLRPC.DocumentAttribute documentAttribute = this.currentDocument.attributes.get(i);
                                if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                                    n = documentAttribute.duration;
                                    break Label_0119;
                                }
                            }
                            n = 0;
                        }
                    }
                    final String format = String.format("%d:%02d", n / 60, n % 60);
                    final String lastTimeString = this.lastTimeString;
                    if (lastTimeString == null || (lastTimeString != null && !lastTimeString.equals(format))) {
                        this.lastTimeString = format;
                        ArticleViewer.audioTimePaint.setTextSize((float)AndroidUtilities.dp(16.0f));
                        this.durationLayout = new StaticLayout((CharSequence)format, ArticleViewer.audioTimePaint, (int)Math.ceil(ArticleViewer.audioTimePaint.measureText(format)), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    }
                    ArticleViewer.audioTimePaint.setColor(ArticleViewer.this.getTextColor());
                    this.invalidate();
                }
            }
        }
    }
    
    private class BlockAuthorDateCell extends View
    {
        private TLRPC.TL_pageBlockAuthorDate currentBlock;
        private WebpageAdapter parentAdapter;
        private DrawingText textLayout;
        private int textX;
        private int textY;
        
        public BlockAuthorDateCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textY = AndroidUtilities.dp(8.0f);
            this.parentAdapter = parentAdapter;
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
        
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setEnabled(true);
            final DrawingText textLayout = this.textLayout;
            if (textLayout == null) {
                return;
            }
            accessibilityNodeInfo.setText(textLayout.getText());
        }
        
        protected void onMeasure(int height, int size) {
            size = View$MeasureSpec.getSize(height);
            final TLRPC.TL_pageBlockAuthorDate currentBlock = this.currentBlock;
            height = 1;
            if (currentBlock != null) {
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.RichText author = currentBlock.author;
                final CharSequence access$19900 = this$0.getText(this, author, author, currentBlock, size);
                final boolean b = access$19900 instanceof Spannable;
                Spannable spannable = null;
                MetricAffectingSpan[] array;
                if (b) {
                    spannable = (Spannable)access$19900;
                    array = (MetricAffectingSpan[])spannable.getSpans(0, access$19900.length(), (Class)MetricAffectingSpan.class);
                }
                else {
                    array = null;
                }
                String s;
                if (this.currentBlock.published_date != 0 && !TextUtils.isEmpty(access$19900)) {
                    s = LocaleController.formatString("ArticleDateByAuthor", 2131558705, LocaleController.getInstance().chatFullDate.format(this.currentBlock.published_date * 1000L), access$19900);
                }
                else if (!TextUtils.isEmpty(access$19900)) {
                    s = LocaleController.formatString("ArticleByAuthor", 2131558704, access$19900);
                }
                else {
                    s = LocaleController.getInstance().chatFullDate.format(this.currentBlock.published_date * 1000L);
                }
                Object o = s;
                if (array != null) {
                    CharSequence charSequence = s;
                    o = s;
                    try {
                        if (array.length > 0) {
                            charSequence = s;
                            final int index = TextUtils.indexOf((CharSequence)s, access$19900);
                            o = s;
                            if (index != -1) {
                                charSequence = s;
                                final Spannable spannable2 = Spannable$Factory.getInstance().newSpannable((CharSequence)s);
                                height = 0;
                                while (true) {
                                    charSequence = (CharSequence)spannable2;
                                    o = spannable2;
                                    if (height >= array.length) {
                                        break;
                                    }
                                    charSequence = (CharSequence)spannable2;
                                    spannable2.setSpan((Object)array[height], spannable.getSpanStart((Object)array[height]) + index, spannable.getSpanEnd((Object)array[height]) + index, 33);
                                    ++height;
                                }
                            }
                        }
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                        o = charSequence;
                    }
                }
                this.textLayout = ArticleViewer.this.createLayoutForText(this, (CharSequence)o, null, size - AndroidUtilities.dp(36.0f), this.currentBlock, this.parentAdapter);
                if (this.textLayout != null) {
                    final int dp = AndroidUtilities.dp(16.0f);
                    height = this.textLayout.getHeight();
                    if (ArticleViewer.this.isRtl) {
                        this.textX = (int)Math.floor(size - this.textLayout.getLineWidth(0) - AndroidUtilities.dp(16.0f));
                    }
                    else {
                        this.textX = AndroidUtilities.dp(18.0f);
                    }
                    height = dp + height + 0;
                }
                else {
                    height = 0;
                }
            }
            this.setMeasuredDimension(size, height);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockAuthorDate currentBlock) {
            this.currentBlock = currentBlock;
            this.requestLayout();
        }
    }
    
    private class BlockBlockquoteCell extends View
    {
        private TLRPC.TL_pageBlockBlockquote currentBlock;
        private WebpageAdapter parentAdapter;
        private DrawingText textLayout;
        private DrawingText textLayout2;
        private int textX;
        private int textY;
        private int textY2;
        
        public BlockBlockquoteCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textY = AndroidUtilities.dp(8.0f);
            this.parentAdapter = parentAdapter;
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
            if (this.textLayout2 != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY2);
                this.textLayout2.draw(canvas);
                canvas.restore();
            }
            if (ArticleViewer.this.isRtl) {
                final int n = this.getMeasuredWidth() - AndroidUtilities.dp(20.0f);
                canvas.drawRect((float)n, (float)AndroidUtilities.dp(6.0f), (float)(n + AndroidUtilities.dp(2.0f)), (float)(this.getMeasuredHeight() - AndroidUtilities.dp(6.0f)), ArticleViewer.quoteLinePaint);
            }
            else {
                canvas.drawRect((float)AndroidUtilities.dp((float)(this.currentBlock.level * 14 + 18)), (float)AndroidUtilities.dp(6.0f), (float)AndroidUtilities.dp((float)(this.currentBlock.level * 14 + 20)), (float)(this.getMeasuredHeight() - AndroidUtilities.dp(6.0f)), ArticleViewer.quoteLinePaint);
            }
            if (this.currentBlock.level > 0) {
                final float n2 = (float)AndroidUtilities.dp(18.0f);
                final float n3 = (float)AndroidUtilities.dp(20.0f);
                final int measuredHeight = this.getMeasuredHeight();
                int dp;
                if (this.currentBlock.bottom) {
                    dp = AndroidUtilities.dp(6.0f);
                }
                else {
                    dp = 0;
                }
                canvas.drawRect(n2, 0.0f, n3, (float)(measuredHeight - dp), ArticleViewer.quoteLinePaint);
            }
        }
        
        protected void onMeasure(int n, int n2) {
            final int size = View$MeasureSpec.getSize(n);
            if (this.currentBlock != null) {
                n2 = size - AndroidUtilities.dp(50.0f);
                final int level = this.currentBlock.level;
                n = n2;
                if (level > 0) {
                    n = n2 - AndroidUtilities.dp((float)(level * 14));
                }
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.TL_pageBlockBlockquote currentBlock = this.currentBlock;
                this.textLayout = this$0.createLayoutForText(this, null, currentBlock.text, n, currentBlock, this.parentAdapter);
                if (this.textLayout != null) {
                    n2 = 0 + (AndroidUtilities.dp(8.0f) + this.textLayout.getHeight());
                }
                else {
                    n2 = 0;
                }
                if (this.currentBlock.level > 0) {
                    if (ArticleViewer.this.isRtl) {
                        this.textX = AndroidUtilities.dp((float)(this.currentBlock.level * 14 + 14));
                    }
                    else {
                        this.textX = AndroidUtilities.dp((float)(this.currentBlock.level * 14)) + AndroidUtilities.dp(32.0f);
                    }
                }
                else if (ArticleViewer.this.isRtl) {
                    this.textX = AndroidUtilities.dp(14.0f);
                }
                else {
                    this.textX = AndroidUtilities.dp(32.0f);
                }
                final ArticleViewer this$2 = ArticleViewer.this;
                final TLRPC.TL_pageBlockBlockquote currentBlock2 = this.currentBlock;
                this.textLayout2 = this$2.createLayoutForText(this, null, currentBlock2.caption, n, currentBlock2, this.parentAdapter);
                n = n2;
                if (this.textLayout2 != null) {
                    this.textY2 = AndroidUtilities.dp(8.0f) + n2;
                    n = n2 + (AndroidUtilities.dp(8.0f) + this.textLayout2.getHeight());
                }
                if ((n2 = n) != 0) {
                    n2 = n + AndroidUtilities.dp(8.0f);
                }
            }
            else {
                n2 = 1;
            }
            this.setMeasuredDimension(size, n2);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout2, this.textX, this.textY2) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockBlockquote currentBlock) {
            this.currentBlock = currentBlock;
            this.requestLayout();
        }
    }
    
    private class BlockChannelCell extends FrameLayout
    {
        private Paint backgroundPaint;
        private int buttonWidth;
        private AnimatorSet currentAnimation;
        private TLRPC.TL_pageBlockChannel currentBlock;
        private int currentState;
        private int currentType;
        private ImageView imageView;
        private WebpageAdapter parentAdapter;
        private ContextProgressView progressView;
        private DrawingText textLayout;
        private TextView textView;
        private int textX;
        private int textX2;
        private int textY;
        
        public BlockChannelCell(final Context context, final WebpageAdapter parentAdapter, final int currentType) {
            super(context);
            this.textX = AndroidUtilities.dp(18.0f);
            this.textY = AndroidUtilities.dp(11.0f);
            this.parentAdapter = parentAdapter;
            this.setWillNotDraw(false);
            this.backgroundPaint = new Paint();
            this.currentType = currentType;
            (this.textView = new TextView(context)).setTextSize(1, 14.0f);
            this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.textView.setText((CharSequence)LocaleController.getString("ChannelJoin", 2131558954));
            this.textView.setGravity(19);
            this.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 39, 53));
            this.textView.setOnClickListener((View$OnClickListener)new _$$Lambda$ArticleViewer$BlockChannelCell$yVpHWSEz8bUl3txryOPbfk9oO0M(this));
            (this.imageView = new ImageView(context)).setImageResource(2131165524);
            this.imageView.setScaleType(ImageView$ScaleType.CENTER);
            this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(39, 39, 53));
            this.addView((View)(this.progressView = new ContextProgressView(context, 0)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(39, 39, 53));
        }
        
        protected void onDraw(final Canvas canvas) {
            final int currentType = this.currentType;
            if (this.currentBlock == null) {
                return;
            }
            canvas.drawRect(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)AndroidUtilities.dp(39.0f), this.backgroundPaint);
            final DrawingText textLayout = this.textLayout;
            if (textLayout != null && textLayout.getLineCount() > 0) {
                canvas.save();
                if (ArticleViewer.this.isRtl) {
                    canvas.translate(this.getMeasuredWidth() - this.textLayout.getLineWidth(0) - this.textX, (float)this.textY);
                }
                else {
                    canvas.translate((float)this.textX, (float)this.textY);
                }
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
        
        protected void onLayout(final boolean b, int textX2, final int n, final int n2, final int n3) {
            this.imageView.layout(this.textX2 + this.buttonWidth / 2 - AndroidUtilities.dp(19.0f), 0, this.textX2 + this.buttonWidth / 2 + AndroidUtilities.dp(20.0f), AndroidUtilities.dp(39.0f));
            this.progressView.layout(this.textX2 + this.buttonWidth / 2 - AndroidUtilities.dp(19.0f), 0, this.textX2 + this.buttonWidth / 2 + AndroidUtilities.dp(20.0f), AndroidUtilities.dp(39.0f));
            final TextView textView = this.textView;
            textX2 = this.textX2;
            textView.layout(textX2, 0, textView.getMeasuredWidth() + textX2, this.textView.getMeasuredHeight());
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(final int n, int size) {
            size = View$MeasureSpec.getSize(n);
            this.setMeasuredDimension(size, AndroidUtilities.dp(48.0f));
            this.textView.measure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0f), 1073741824));
            this.buttonWidth = this.textView.getMeasuredWidth();
            this.progressView.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0f), 1073741824));
            this.imageView.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0f), 1073741824));
            final TLRPC.TL_pageBlockChannel currentBlock = this.currentBlock;
            if (currentBlock != null) {
                this.textLayout = ArticleViewer.this.createLayoutForText((View)this, currentBlock.channel.title, null, size - AndroidUtilities.dp(52.0f) - this.buttonWidth, this.currentBlock, Layout$Alignment.ALIGN_LEFT, this.parentAdapter);
                if (ArticleViewer.this.isRtl) {
                    this.textX2 = this.textX;
                }
                else {
                    this.textX2 = this.getMeasuredWidth() - this.textX - this.buttonWidth;
                }
            }
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            if (this.currentType != 0) {
                return super.onTouchEvent(motionEvent);
            }
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.textLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockChannel currentBlock) {
            this.currentBlock = currentBlock;
            final int access$13100 = ArticleViewer.this.getSelectedColor();
            if (this.currentType == 0) {
                this.textView.setTextColor(-14840360);
                if (access$13100 == 0) {
                    this.backgroundPaint.setColor(-526345);
                }
                else if (access$13100 == 1) {
                    this.backgroundPaint.setColor(-1712440);
                }
                else if (access$13100 == 2) {
                    this.backgroundPaint.setColor(-15000805);
                }
                this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(-6710887, PorterDuff$Mode.MULTIPLY));
            }
            else {
                this.textView.setTextColor(-1);
                this.backgroundPaint.setColor(2130706432);
                this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(-1, PorterDuff$Mode.MULTIPLY));
            }
            final TLRPC.Chat chat = MessagesController.getInstance(ArticleViewer.this.currentAccount).getChat(currentBlock.channel.id);
            if (chat != null && !chat.min) {
                ArticleViewer.this.loadedChannel = chat;
                if (chat.left && !chat.kicked) {
                    this.setState(0, false);
                }
                else {
                    this.setState(4, false);
                }
            }
            else {
                ArticleViewer.this.loadChannel(this, this.parentAdapter, currentBlock.channel);
                this.setState(1, false);
            }
            this.requestLayout();
        }
        
        public void setState(final int currentState, final boolean b) {
            final AnimatorSet currentAnimation = this.currentAnimation;
            if (currentAnimation != null) {
                currentAnimation.cancel();
            }
            this.currentState = currentState;
            final float n = 0.0f;
            final float n2 = 0.0f;
            float scaleY = 0.1f;
            if (b) {
                this.currentAnimation = new AnimatorSet();
                final AnimatorSet currentAnimation2 = this.currentAnimation;
                final TextView textView = this.textView;
                final Property alpha = View.ALPHA;
                float n3;
                if (currentState == 0) {
                    n3 = 1.0f;
                }
                else {
                    n3 = 0.0f;
                }
                final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)textView, alpha, new float[] { n3 });
                final TextView textView2 = this.textView;
                final Property scale_X = View.SCALE_X;
                float n4;
                if (currentState == 0) {
                    n4 = 1.0f;
                }
                else {
                    n4 = 0.1f;
                }
                final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)textView2, scale_X, new float[] { n4 });
                final TextView textView3 = this.textView;
                final Property scale_Y = View.SCALE_Y;
                float n5;
                if (currentState == 0) {
                    n5 = 1.0f;
                }
                else {
                    n5 = 0.1f;
                }
                final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)textView3, scale_Y, new float[] { n5 });
                final ContextProgressView progressView = this.progressView;
                final Property alpha2 = View.ALPHA;
                float n6;
                if (currentState == 1) {
                    n6 = 1.0f;
                }
                else {
                    n6 = 0.0f;
                }
                final ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat((Object)progressView, alpha2, new float[] { n6 });
                final ContextProgressView progressView2 = this.progressView;
                final Property scale_X2 = View.SCALE_X;
                float n7;
                if (currentState == 1) {
                    n7 = 1.0f;
                }
                else {
                    n7 = 0.1f;
                }
                final ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat((Object)progressView2, scale_X2, new float[] { n7 });
                final ContextProgressView progressView3 = this.progressView;
                final Property scale_Y2 = View.SCALE_Y;
                float n8;
                if (currentState == 1) {
                    n8 = 1.0f;
                }
                else {
                    n8 = 0.1f;
                }
                final ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat((Object)progressView3, scale_Y2, new float[] { n8 });
                final ImageView imageView = this.imageView;
                final Property alpha3 = View.ALPHA;
                float n9 = n2;
                if (currentState == 2) {
                    n9 = 1.0f;
                }
                final ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat((Object)imageView, alpha3, new float[] { n9 });
                final ImageView imageView2 = this.imageView;
                final Property scale_X3 = View.SCALE_X;
                float n10;
                if (currentState == 2) {
                    n10 = 1.0f;
                }
                else {
                    n10 = 0.1f;
                }
                final ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat((Object)imageView2, scale_X3, new float[] { n10 });
                final ImageView imageView3 = this.imageView;
                final Property scale_Y3 = View.SCALE_Y;
                if (currentState == 2) {
                    scaleY = 1.0f;
                }
                currentAnimation2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2, (Animator)ofFloat3, (Animator)ofFloat4, (Animator)ofFloat5, (Animator)ofFloat6, (Animator)ofFloat7, (Animator)ofFloat8, (Animator)ObjectAnimator.ofFloat((Object)imageView3, scale_Y3, new float[] { scaleY }) });
                this.currentAnimation.setDuration(150L);
                this.currentAnimation.start();
            }
            else {
                final TextView textView4 = this.textView;
                float alpha4;
                if (currentState == 0) {
                    alpha4 = 1.0f;
                }
                else {
                    alpha4 = 0.0f;
                }
                textView4.setAlpha(alpha4);
                final TextView textView5 = this.textView;
                float scaleX;
                if (currentState == 0) {
                    scaleX = 1.0f;
                }
                else {
                    scaleX = 0.1f;
                }
                textView5.setScaleX(scaleX);
                final TextView textView6 = this.textView;
                float scaleY2;
                if (currentState == 0) {
                    scaleY2 = 1.0f;
                }
                else {
                    scaleY2 = 0.1f;
                }
                textView6.setScaleY(scaleY2);
                final ContextProgressView progressView4 = this.progressView;
                float alpha5;
                if (currentState == 1) {
                    alpha5 = 1.0f;
                }
                else {
                    alpha5 = 0.0f;
                }
                progressView4.setAlpha(alpha5);
                final ContextProgressView progressView5 = this.progressView;
                float scaleX2;
                if (currentState == 1) {
                    scaleX2 = 1.0f;
                }
                else {
                    scaleX2 = 0.1f;
                }
                progressView5.setScaleX(scaleX2);
                final ContextProgressView progressView6 = this.progressView;
                float scaleY3;
                if (currentState == 1) {
                    scaleY3 = 1.0f;
                }
                else {
                    scaleY3 = 0.1f;
                }
                progressView6.setScaleY(scaleY3);
                final ImageView imageView4 = this.imageView;
                float alpha6 = n;
                if (currentState == 2) {
                    alpha6 = 1.0f;
                }
                imageView4.setAlpha(alpha6);
                final ImageView imageView5 = this.imageView;
                float scaleX3;
                if (currentState == 2) {
                    scaleX3 = 1.0f;
                }
                else {
                    scaleX3 = 0.1f;
                }
                imageView5.setScaleX(scaleX3);
                final ImageView imageView6 = this.imageView;
                if (currentState == 2) {
                    scaleY = 1.0f;
                }
                imageView6.setScaleY(scaleY);
            }
        }
    }
    
    private class BlockCollageCell extends FrameLayout
    {
        private DrawingText captionLayout;
        private DrawingText creditLayout;
        private int creditOffset;
        private TLRPC.TL_pageBlockCollage currentBlock;
        private GridLayoutManager gridLayoutManager;
        private GroupedMessages group;
        private boolean inLayout;
        private RecyclerView.Adapter innerAdapter;
        private RecyclerListView innerListView;
        private int listX;
        private WebpageAdapter parentAdapter;
        private int textX;
        private int textY;
        final /* synthetic */ ArticleViewer this$0;
        
        public BlockCollageCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.group = new GroupedMessages();
            this.parentAdapter = parentAdapter;
            (this.innerListView = new RecyclerListView(context) {
                @Override
                public void requestLayout() {
                    if (BlockCollageCell.this.inLayout) {
                        return;
                    }
                    super.requestLayout();
                }
            }).addItemDecoration((RecyclerView.ItemDecoration)new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
                    final int n = 0;
                    rect.bottom = 0;
                    MessageObject.GroupedMessagePosition groupedMessagePosition;
                    if (view instanceof BlockPhotoCell) {
                        groupedMessagePosition = BlockCollageCell.this.group.positions.get(((BlockPhotoCell)view).currentBlock);
                    }
                    else if (view instanceof BlockVideoCell) {
                        groupedMessagePosition = BlockCollageCell.this.group.positions.get(((BlockVideoCell)view).currentBlock);
                    }
                    else {
                        groupedMessagePosition = null;
                    }
                    if (groupedMessagePosition != null && groupedMessagePosition.siblingHeights != null) {
                        final Point displaySize = AndroidUtilities.displaySize;
                        final float n2 = Math.max(displaySize.x, displaySize.y) * 0.5f;
                        int n3 = 0;
                        int n4 = 0;
                        while (true) {
                            final float[] siblingHeights = groupedMessagePosition.siblingHeights;
                            if (n3 >= siblingHeights.length) {
                                break;
                            }
                            n4 += (int)Math.ceil(siblingHeights[n3] * n2);
                            ++n3;
                        }
                        final int n5 = n4 + (groupedMessagePosition.maxY - groupedMessagePosition.minY) * AndroidUtilities.dp2(11.0f);
                        final int size = BlockCollageCell.this.group.posArray.size();
                        int index = n;
                        int n6;
                        while (true) {
                            n6 = n5;
                            if (index >= size) {
                                break;
                            }
                            final MessageObject.GroupedMessagePosition groupedMessagePosition2 = BlockCollageCell.this.group.posArray.get(index);
                            final byte minY = groupedMessagePosition2.minY;
                            final byte minY2 = groupedMessagePosition.minY;
                            if (minY == minY2) {
                                if (groupedMessagePosition2.minX != groupedMessagePosition.minX || groupedMessagePosition2.maxX != groupedMessagePosition.maxX || minY != minY2 || groupedMessagePosition2.maxY != groupedMessagePosition.maxY) {
                                    if (groupedMessagePosition2.minY == groupedMessagePosition.minY) {
                                        n6 = n5 - ((int)Math.ceil(n2 * groupedMessagePosition2.ph) - AndroidUtilities.dp(4.0f));
                                        break;
                                    }
                                }
                            }
                            ++index;
                        }
                        rect.bottom = -n6;
                    }
                }
            });
            (this.gridLayoutManager = new GridLayoutManagerFixed(context, 1000, 1, true) {
                @Override
                protected boolean hasSiblingChild(int i) {
                    final MessageObject.GroupedMessagePosition groupedMessagePosition = BlockCollageCell.this.group.positions.get(BlockCollageCell.this.currentBlock.items.get(BlockCollageCell.this.currentBlock.items.size() - i - 1));
                    if (groupedMessagePosition.minX != groupedMessagePosition.maxX) {
                        i = groupedMessagePosition.minY;
                        if (i == groupedMessagePosition.maxY) {
                            if (i != 0) {
                                int size;
                                MessageObject.GroupedMessagePosition groupedMessagePosition2;
                                byte minY;
                                byte minY2;
                                for (size = BlockCollageCell.this.group.posArray.size(), i = 0; i < size; ++i) {
                                    groupedMessagePosition2 = BlockCollageCell.this.group.posArray.get(i);
                                    if (groupedMessagePosition2 != groupedMessagePosition) {
                                        minY = groupedMessagePosition2.minY;
                                        minY2 = groupedMessagePosition.minY;
                                        if (minY <= minY2 && groupedMessagePosition2.maxY >= minY2) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return false;
                }
                
                @Override
                public boolean shouldLayoutChildFromOpositeSide(final View view) {
                    return false;
                }
                
                @Override
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }
            }).setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(final int n) {
                    return BlockCollageCell.this.group.positions.get(BlockCollageCell.this.currentBlock.items.get(BlockCollageCell.this.currentBlock.items.size() - n - 1)).spanSize;
                }
            });
            this.innerListView.setLayoutManager((RecyclerView.LayoutManager)this.gridLayoutManager);
            this.innerListView.setAdapter(this.innerAdapter = new RecyclerView.Adapter() {
                @Override
                public int getItemCount() {
                    if (BlockCollageCell.this.currentBlock == null) {
                        return 0;
                    }
                    return BlockCollageCell.this.currentBlock.items.size();
                }
                
                @Override
                public int getItemViewType(final int n) {
                    final ArrayList<TLRPC.PageBlock> items = BlockCollageCell.this.currentBlock.items;
                    final int size = BlockCollageCell.this.currentBlock.items.size();
                    int n2 = 1;
                    if (items.get(size - n - 1) instanceof TLRPC.TL_pageBlockPhoto) {
                        n2 = 0;
                    }
                    return n2;
                }
                
                @Override
                public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
                    final TLRPC.PageBlock pageBlock = BlockCollageCell.this.currentBlock.items.get(BlockCollageCell.this.currentBlock.items.size() - n - 1);
                    if (viewHolder.getItemViewType() != 0) {
                        final BlockVideoCell blockVideoCell = (BlockVideoCell)viewHolder.itemView;
                        blockVideoCell.groupPosition = BlockCollageCell.this.group.positions.get(pageBlock);
                        blockVideoCell.setBlock((TLRPC.TL_pageBlockVideo)pageBlock, true, true);
                    }
                    else {
                        final BlockPhotoCell blockPhotoCell = (BlockPhotoCell)viewHolder.itemView;
                        blockPhotoCell.groupPosition = BlockCollageCell.this.group.positions.get(pageBlock);
                        blockPhotoCell.setBlock((TLRPC.TL_pageBlockPhoto)pageBlock, true, true);
                    }
                }
                
                @Override
                public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
                    DownloadController.FileDownloadProgressListener fileDownloadProgressListener;
                    if (n != 0) {
                        final BlockCollageCell this$1 = BlockCollageCell.this;
                        fileDownloadProgressListener = this$1.this$0.new BlockVideoCell(this$1.getContext(), BlockCollageCell.this.parentAdapter, 2);
                    }
                    else {
                        final BlockCollageCell this$2 = BlockCollageCell.this;
                        fileDownloadProgressListener = this$2.this$0.new BlockPhotoCell(this$2.getContext(), BlockCollageCell.this.parentAdapter, 2);
                    }
                    return new RecyclerListView.Holder((View)fileDownloadProgressListener);
                }
            });
            this.addView((View)this.innerListView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
            this.setWillNotDraw(false);
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.captionLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.captionLayout.draw(canvas);
                canvas.restore();
            }
            if (this.creditLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)(this.textY + this.creditOffset));
                this.creditLayout.draw(canvas);
                canvas.restore();
            }
            if (this.currentBlock.level > 0) {
                final float n = (float)AndroidUtilities.dp(18.0f);
                final float n2 = (float)AndroidUtilities.dp(20.0f);
                final int measuredHeight = this.getMeasuredHeight();
                int dp;
                if (this.currentBlock.bottom) {
                    dp = AndroidUtilities.dp(6.0f);
                }
                else {
                    dp = 0;
                }
                canvas.drawRect(n, 0.0f, n2, (float)(measuredHeight - dp), ArticleViewer.quoteLinePaint);
            }
        }
        
        protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
            this.innerListView.layout(this.listX, AndroidUtilities.dp(8.0f), this.listX + this.innerListView.getMeasuredWidth(), this.innerListView.getMeasuredHeight() + AndroidUtilities.dp(8.0f));
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int level, int n) {
            n = 1;
            this.inLayout = true;
            final int size = View$MeasureSpec.getSize(level);
            final TLRPC.TL_pageBlockCollage currentBlock = this.currentBlock;
            level = n;
            if (currentBlock != null) {
                level = currentBlock.level;
                if (level > 0) {
                    level = AndroidUtilities.dp((float)(level * 14)) + AndroidUtilities.dp(18.0f);
                    this.listX = level;
                    this.textX = level;
                    level = (n = size - (this.listX + AndroidUtilities.dp(18.0f)));
                }
                else {
                    this.listX = 0;
                    this.textX = AndroidUtilities.dp(18.0f);
                    n = size - AndroidUtilities.dp(36.0f);
                    level = size;
                }
                this.innerListView.measure(View$MeasureSpec.makeMeasureSpec(level, 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
                final int measuredHeight = this.innerListView.getMeasuredHeight();
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.TL_pageBlockCollage currentBlock2 = this.currentBlock;
                this.captionLayout = this$0.createLayoutForText((View)this, null, currentBlock2.caption.text, n, currentBlock2, this.parentAdapter);
                level = measuredHeight;
                if (this.captionLayout != null) {
                    this.textY = AndroidUtilities.dp(8.0f) + measuredHeight;
                    this.creditOffset = AndroidUtilities.dp(4.0f) + this.captionLayout.getHeight();
                    level = measuredHeight + (this.creditOffset + AndroidUtilities.dp(4.0f));
                }
                final ArticleViewer this$2 = ArticleViewer.this;
                final TLRPC.TL_pageBlockCollage currentBlock3 = this.currentBlock;
                final TLRPC.RichText credit = currentBlock3.caption.credit;
                Layout$Alignment layout$Alignment;
                if (this$2.isRtl) {
                    layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                }
                else {
                    layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                }
                this.creditLayout = this$2.createLayoutForText((View)this, null, credit, n, currentBlock3, layout$Alignment, this.parentAdapter);
                n = level;
                if (this.creditLayout != null) {
                    n = level + (AndroidUtilities.dp(4.0f) + this.creditLayout.getHeight());
                }
                n += AndroidUtilities.dp(16.0f);
                final TLRPC.TL_pageBlockCollage currentBlock4 = this.currentBlock;
                level = n;
                if (currentBlock4.level > 0) {
                    level = n;
                    if (!currentBlock4.bottom) {
                        level = n + AndroidUtilities.dp(8.0f);
                    }
                }
            }
            this.setMeasuredDimension(size, level);
            this.inLayout = false;
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.captionLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.creditLayout, this.textX, this.textY + this.creditOffset) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockCollage currentBlock) {
            if (this.currentBlock != currentBlock) {
                this.currentBlock = currentBlock;
                this.group.calculate();
            }
            this.innerAdapter.notifyDataSetChanged();
            final int access$13100 = ArticleViewer.this.getSelectedColor();
            if (access$13100 == 0) {
                this.innerListView.setGlowColor(-657673);
            }
            else if (access$13100 == 1) {
                this.innerListView.setGlowColor(-659492);
            }
            else if (access$13100 == 2) {
                this.innerListView.setGlowColor(-15461356);
            }
            this.requestLayout();
        }
        
        public class GroupedMessages
        {
            public long groupId;
            public boolean hasSibling;
            private int maxSizeWidth;
            public ArrayList<MessageObject.GroupedMessagePosition> posArray;
            public HashMap<TLObject, MessageObject.GroupedMessagePosition> positions;
            
            public GroupedMessages() {
                this.posArray = new ArrayList<MessageObject.GroupedMessagePosition>();
                this.positions = new HashMap<TLObject, MessageObject.GroupedMessagePosition>();
                this.maxSizeWidth = 1000;
            }
            
            private float multiHeight(final float[] array, int i, final int n) {
                float n2 = 0.0f;
                while (i < n) {
                    n2 += array[i];
                    ++i;
                }
                return this.maxSizeWidth / n2;
            }
            
            public void calculate() {
                this.posArray.clear();
                this.positions.clear();
                int size = BlockCollageCell.this.currentBlock.items.size();
                if (size <= 1) {
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                this.hasSibling = false;
                int i = 0;
                float n = 1.0f;
                int n2 = 0;
                while (i < size) {
                    final TLRPC.PageBlock key = BlockCollageCell.this.currentBlock.items.get(i);
                    float n3 = 0.0f;
                    int n4 = 0;
                    Label_0376: {
                        TLRPC.PhotoSize photoSize;
                        if (key instanceof TLRPC.TL_pageBlockPhoto) {
                            final TLRPC.Photo access$12800 = ArticleViewer.this.getPhotoWithId(((TLRPC.TL_pageBlockPhoto)key).photo_id);
                            if (access$12800 == null) {
                                n3 = n;
                                n4 = n2;
                                break Label_0376;
                            }
                            photoSize = FileLoader.getClosestPhotoSizeWithSize(access$12800.sizes, AndroidUtilities.getPhotoSize());
                        }
                        else {
                            n3 = n;
                            n4 = n2;
                            if (!(key instanceof TLRPC.TL_pageBlockVideo)) {
                                break Label_0376;
                            }
                            final TLRPC.Document access$12801 = ArticleViewer.this.getDocumentWithId(((TLRPC.TL_pageBlockVideo)key).video_id);
                            if (access$12801 == null) {
                                n3 = n;
                                n4 = n2;
                                break Label_0376;
                            }
                            photoSize = FileLoader.getClosestPhotoSizeWithSize(access$12801.thumbs, 90);
                        }
                        final MessageObject.GroupedMessagePosition groupedMessagePosition = new MessageObject.GroupedMessagePosition();
                        groupedMessagePosition.last = (i == size - 1);
                        float aspectRatio;
                        if (photoSize == null) {
                            aspectRatio = 1.0f;
                        }
                        else {
                            aspectRatio = photoSize.w / (float)photoSize.h;
                        }
                        groupedMessagePosition.aspectRatio = aspectRatio;
                        final float aspectRatio2 = groupedMessagePosition.aspectRatio;
                        if (aspectRatio2 > 1.2f) {
                            sb.append("w");
                        }
                        else if (aspectRatio2 < 0.8f) {
                            sb.append("n");
                        }
                        else {
                            sb.append("q");
                        }
                        final float aspectRatio3 = groupedMessagePosition.aspectRatio;
                        n3 = n + aspectRatio3;
                        if (aspectRatio3 > 2.0f) {
                            n2 = 1;
                        }
                        this.positions.put(key, groupedMessagePosition);
                        this.posArray.add(groupedMessagePosition);
                        n4 = n2;
                    }
                    ++i;
                    n = n3;
                    n2 = n4;
                }
                final int dp = AndroidUtilities.dp(120.0f);
                final float n5 = (float)AndroidUtilities.dp(120.0f);
                final Point displaySize = AndroidUtilities.displaySize;
                final int a = (int)(n5 / (Math.min(displaySize.x, displaySize.y) / (float)this.maxSizeWidth));
                final float n6 = (float)AndroidUtilities.dp(40.0f);
                final Point displaySize2 = AndroidUtilities.displaySize;
                final float n7 = (float)Math.min(displaySize2.x, displaySize2.y);
                final int maxSizeWidth = this.maxSizeWidth;
                final int n8 = (int)(n6 / (n7 / maxSizeWidth));
                final float n9 = maxSizeWidth / 814.0f;
                final float n10 = n / size;
                if (n2 == 0 && (size == 2 || size == 3 || size == 4)) {
                    Label_1890: {
                        if (size == 2) {
                            final MessageObject.GroupedMessagePosition groupedMessagePosition2 = this.posArray.get(0);
                            final MessageObject.GroupedMessagePosition groupedMessagePosition3 = this.posArray.get(1);
                            final String string = sb.toString();
                            if (string.equals("ww")) {
                                final double n11 = n10;
                                final double v = n9;
                                Double.isNaN(v);
                                if (n11 > v * 1.4) {
                                    final float aspectRatio4 = groupedMessagePosition2.aspectRatio;
                                    final float aspectRatio5 = groupedMessagePosition3.aspectRatio;
                                    if (aspectRatio4 - aspectRatio5 < 0.2) {
                                        final int maxSizeWidth2 = this.maxSizeWidth;
                                        final float n12 = Math.round(Math.min(maxSizeWidth2 / aspectRatio4, Math.min(maxSizeWidth2 / aspectRatio5, 407.0f))) / 814.0f;
                                        groupedMessagePosition2.set(0, 0, 0, 0, this.maxSizeWidth, n12, 7);
                                        groupedMessagePosition3.set(0, 0, 1, 1, this.maxSizeWidth, n12, 11);
                                        break Label_1890;
                                    }
                                }
                            }
                            if (!string.equals("ww") && !string.equals("qq")) {
                                final int maxSizeWidth3 = this.maxSizeWidth;
                                final float n13 = (float)maxSizeWidth3;
                                final float n14 = (float)maxSizeWidth3;
                                final float aspectRatio6 = groupedMessagePosition2.aspectRatio;
                                final int n15 = (int)Math.max(n13 * 0.4f, (float)Math.round(n14 / aspectRatio6 / (1.0f / aspectRatio6 + 1.0f / groupedMessagePosition3.aspectRatio)));
                                final int n16 = this.maxSizeWidth - n15;
                                int n17 = n15;
                                int n18;
                                if ((n18 = n16) < a) {
                                    n17 = n15 - (a - n16);
                                    n18 = a;
                                }
                                final float n19 = Math.min(814.0f, (float)Math.round(Math.min(n18 / groupedMessagePosition2.aspectRatio, n17 / groupedMessagePosition3.aspectRatio))) / 814.0f;
                                groupedMessagePosition2.set(0, 0, 0, 0, n18, n19, 13);
                                groupedMessagePosition3.set(1, 1, 0, 0, n17, n19, 14);
                            }
                            else {
                                final int n20 = this.maxSizeWidth / 2;
                                final float n21 = (float)n20;
                                final float n22 = Math.round(Math.min(n21 / groupedMessagePosition2.aspectRatio, Math.min(n21 / groupedMessagePosition3.aspectRatio, 814.0f))) / 814.0f;
                                groupedMessagePosition2.set(0, 0, 0, 0, n20, n22, 13);
                                groupedMessagePosition3.set(1, 1, 0, 0, n20, n22, 14);
                            }
                        }
                        else if (size == 3) {
                            final MessageObject.GroupedMessagePosition groupedMessagePosition4 = this.posArray.get(0);
                            final MessageObject.GroupedMessagePosition groupedMessagePosition5 = this.posArray.get(1);
                            final MessageObject.GroupedMessagePosition groupedMessagePosition6 = this.posArray.get(2);
                            if (sb.charAt(0) == 'n') {
                                final float aspectRatio7 = groupedMessagePosition5.aspectRatio;
                                final float min = Math.min(407.0f, (float)Math.round(this.maxSizeWidth * aspectRatio7 / (groupedMessagePosition6.aspectRatio + aspectRatio7)));
                                final float n23 = 814.0f - min;
                                final int n24 = (int)Math.max((float)a, Math.min(this.maxSizeWidth * 0.5f, (float)Math.round(Math.min(groupedMessagePosition6.aspectRatio * min, groupedMessagePosition5.aspectRatio * n23))));
                                final int round = Math.round(Math.min(groupedMessagePosition4.aspectRatio * 814.0f + n8, (float)(this.maxSizeWidth - n24)));
                                groupedMessagePosition4.set(0, 0, 0, 1, round, 1.0f, 13);
                                final float n25 = n23 / 814.0f;
                                groupedMessagePosition5.set(1, 1, 0, 0, n24, n25, 6);
                                final float n26 = min / 814.0f;
                                groupedMessagePosition6.set(0, 1, 1, 1, n24, n26, 10);
                                final int maxSizeWidth4 = this.maxSizeWidth;
                                groupedMessagePosition6.spanSize = maxSizeWidth4;
                                groupedMessagePosition4.siblingHeights = new float[] { n26, n25 };
                                groupedMessagePosition5.spanSize = maxSizeWidth4 - round;
                                groupedMessagePosition6.leftSpanOffset = round;
                                this.hasSibling = true;
                            }
                            else {
                                final float n27 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition4.aspectRatio, 537.24005f)) / 814.0f;
                                groupedMessagePosition4.set(0, 1, 0, 0, this.maxSizeWidth, n27, 7);
                                final int n28 = this.maxSizeWidth / 2;
                                final float n29 = (float)n28;
                                final float n30 = Math.min(814.0f - n27, (float)Math.round(Math.min(n29 / groupedMessagePosition5.aspectRatio, n29 / groupedMessagePosition6.aspectRatio))) / 814.0f;
                                groupedMessagePosition5.set(0, 0, 1, 1, n28, n30, 9);
                                groupedMessagePosition6.set(1, 1, 1, 1, n28, n30, 10);
                            }
                        }
                        else if (size == 4) {
                            final MessageObject.GroupedMessagePosition groupedMessagePosition7 = this.posArray.get(0);
                            final MessageObject.GroupedMessagePosition groupedMessagePosition8 = this.posArray.get(1);
                            final MessageObject.GroupedMessagePosition groupedMessagePosition9 = this.posArray.get(2);
                            final MessageObject.GroupedMessagePosition groupedMessagePosition10 = this.posArray.get(3);
                            if (sb.charAt(0) == 'w') {
                                final float n31 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition7.aspectRatio, 537.24005f)) / 814.0f;
                                groupedMessagePosition7.set(0, 2, 0, 0, this.maxSizeWidth, n31, 7);
                                final float b = (float)Math.round(this.maxSizeWidth / (groupedMessagePosition8.aspectRatio + groupedMessagePosition9.aspectRatio + groupedMessagePosition10.aspectRatio));
                                final float n32 = (float)a;
                                final int n33 = (int)Math.max(n32, Math.min(this.maxSizeWidth * 0.4f, groupedMessagePosition8.aspectRatio * b));
                                final int n34 = (int)Math.max(Math.max(n32, this.maxSizeWidth * 0.33f), groupedMessagePosition10.aspectRatio * b);
                                final int maxSizeWidth5 = this.maxSizeWidth;
                                final float n35 = Math.min(814.0f - n31, b) / 814.0f;
                                groupedMessagePosition8.set(0, 0, 1, 1, n33, n35, 9);
                                groupedMessagePosition9.set(1, 1, 1, 1, maxSizeWidth5 - n33 - n34, n35, 8);
                                groupedMessagePosition10.set(2, 2, 1, 1, n34, n35, 10);
                            }
                            else {
                                final int max = Math.max(a, Math.round(814.0f / (1.0f / groupedMessagePosition8.aspectRatio + 1.0f / groupedMessagePosition9.aspectRatio + 1.0f / this.posArray.get(3).aspectRatio)));
                                final float n36 = (float)dp;
                                final float n37 = (float)max;
                                final float min2 = Math.min(0.33f, Math.max(n36, n37 / groupedMessagePosition8.aspectRatio) / 814.0f);
                                final float min3 = Math.min(0.33f, Math.max(n36, n37 / groupedMessagePosition9.aspectRatio) / 814.0f);
                                final float n38 = 1.0f - min2 - min3;
                                final int round2 = Math.round(Math.min(814.0f * groupedMessagePosition7.aspectRatio + n8, (float)(this.maxSizeWidth - max)));
                                groupedMessagePosition7.set(0, 0, 0, 2, round2, min2 + min3 + n38, 13);
                                groupedMessagePosition8.set(1, 1, 0, 0, max, min2, 6);
                                groupedMessagePosition9.set(0, 1, 1, 1, max, min3, 2);
                                groupedMessagePosition9.spanSize = this.maxSizeWidth;
                                groupedMessagePosition10.set(0, 1, 2, 2, max, n38, 10);
                                final int maxSizeWidth6 = this.maxSizeWidth;
                                groupedMessagePosition10.spanSize = maxSizeWidth6;
                                groupedMessagePosition8.spanSize = maxSizeWidth6 - round2;
                                groupedMessagePosition9.leftSpanOffset = round2;
                                groupedMessagePosition10.leftSpanOffset = round2;
                                groupedMessagePosition7.siblingHeights = new float[] { min2, min3, n38 };
                                this.hasSibling = true;
                            }
                        }
                    }
                }
                else {
                    final float[] array = new float[this.posArray.size()];
                    for (int j = 0; j < size; ++j) {
                        if (n10 > 1.1f) {
                            array[j] = Math.max(1.0f, this.posArray.get(j).aspectRatio);
                        }
                        else {
                            array[j] = Math.min(1.0f, this.posArray.get(j).aspectRatio);
                        }
                        array[j] = Math.max(0.66667f, Math.min(1.7f, array[j]));
                    }
                    final ArrayList<MessageGroupedLayoutAttempt> list = new ArrayList<MessageGroupedLayoutAttempt>();
                    for (int k = 1; k < array.length; ++k) {
                        final int n39 = array.length - k;
                        if (k <= 3) {
                            if (n39 <= 3) {
                                list.add(new MessageGroupedLayoutAttempt(k, n39, this.multiHeight(array, 0, k), this.multiHeight(array, k, array.length)));
                            }
                        }
                    }
                    for (int l = 1; l < array.length - 1; ++l) {
                        for (int n40 = 1; n40 < array.length - l; ++n40) {
                            final int n41 = array.length - l - n40;
                            if (l <= 3) {
                                int n42;
                                if (n10 < 0.85f) {
                                    n42 = 4;
                                }
                                else {
                                    n42 = 3;
                                }
                                if (n40 <= n42) {
                                    if (n41 <= 3) {
                                        final float multiHeight = this.multiHeight(array, 0, l);
                                        final int n43 = l + n40;
                                        list.add(new MessageGroupedLayoutAttempt(l, n40, n41, multiHeight, this.multiHeight(array, l, n43), this.multiHeight(array, n43, array.length)));
                                    }
                                }
                            }
                        }
                    }
                    for (int n44 = 1; n44 < array.length - 2; ++n44) {
                        for (int n45 = 1; n45 < array.length - n44; ++n45) {
                            for (int n46 = 1; n46 < array.length - n44 - n45; ++n46) {
                                final int n47 = array.length - n44 - n45 - n46;
                                if (n44 <= 3 && n45 <= 3 && n46 <= 3) {
                                    if (n47 <= 3) {
                                        final float multiHeight2 = this.multiHeight(array, 0, n44);
                                        final int n48 = n44 + n45;
                                        final float multiHeight3 = this.multiHeight(array, n44, n48);
                                        final int n49 = n48 + n46;
                                        list.add(new MessageGroupedLayoutAttempt(n44, n45, n46, n47, multiHeight2, multiHeight3, this.multiHeight(array, n48, n49), this.multiHeight(array, n49, array.length)));
                                    }
                                }
                            }
                        }
                    }
                    final int n50 = size;
                    final float n51 = (float)(this.maxSizeWidth / 3 * 4);
                    int index = 0;
                    MessageGroupedLayoutAttempt messageGroupedLayoutAttempt = null;
                    float n52 = 0.0f;
                    while (index < list.size()) {
                        final MessageGroupedLayoutAttempt messageGroupedLayoutAttempt2 = list.get(index);
                        int n53 = 0;
                        float n54 = 0.0f;
                        float n55 = Float.MAX_VALUE;
                        while (true) {
                            final float[] heights = messageGroupedLayoutAttempt2.heights;
                            if (n53 >= heights.length) {
                                break;
                            }
                            n54 += heights[n53];
                            float n56 = n55;
                            if (heights[n53] < n55) {
                                n56 = heights[n53];
                            }
                            ++n53;
                            n55 = n56;
                        }
                        float abs = Math.abs(n54 - n51);
                        final int[] lineCounts = messageGroupedLayoutAttempt2.lineCounts;
                        Label_2634: {
                            if (lineCounts.length > 1) {
                                if (lineCounts[0] <= lineCounts[1]) {
                                    if (lineCounts.length <= 2 || lineCounts[1] <= lineCounts[2]) {
                                        final int[] lineCounts2 = messageGroupedLayoutAttempt2.lineCounts;
                                        if (lineCounts2.length <= 3 || lineCounts2[2] <= lineCounts2[3]) {
                                            break Label_2634;
                                        }
                                    }
                                }
                                abs *= 1.2f;
                            }
                        }
                        float n57 = abs;
                        if (n55 < a) {
                            n57 = abs * 1.5f;
                        }
                        float n58 = 0.0f;
                        Label_2678: {
                            if (messageGroupedLayoutAttempt != null) {
                                n58 = n52;
                                if (n57 >= n52) {
                                    break Label_2678;
                                }
                            }
                            messageGroupedLayoutAttempt = messageGroupedLayoutAttempt2;
                            n58 = n57;
                        }
                        ++index;
                        n52 = n58;
                    }
                    if (messageGroupedLayoutAttempt == null) {
                        return;
                    }
                    int n59 = 0;
                    int n60 = 0;
                    while (true) {
                        final int[] lineCounts3 = messageGroupedLayoutAttempt.lineCounts;
                        size = n50;
                        if (n59 >= lineCounts3.length) {
                            break;
                        }
                        final int n61 = lineCounts3[n59];
                        final float n62 = messageGroupedLayoutAttempt.heights[n59];
                        final int maxSizeWidth7 = this.maxSizeWidth;
                        int index2 = n60;
                        int n63 = maxSizeWidth7;
                        int n64 = 0;
                        MessageObject.GroupedMessagePosition groupedMessagePosition11 = null;
                        while (n64 < n61) {
                            final int n65 = (int)(array[index2] * n62);
                            final int n66 = n63 - n65;
                            final MessageObject.GroupedMessagePosition groupedMessagePosition12 = this.posArray.get(index2);
                            int n67;
                            if (n59 == 0) {
                                n67 = 4;
                            }
                            else {
                                n67 = 0;
                            }
                            int n68 = n67;
                            if (n59 == messageGroupedLayoutAttempt.lineCounts.length - 1) {
                                n68 = (n67 | 0x8);
                            }
                            int n69 = n68;
                            if (n64 == 0) {
                                n69 = (n68 | 0x1);
                            }
                            int n70;
                            if (n64 == n61 - 1) {
                                n70 = (n69 | 0x2);
                                groupedMessagePosition11 = groupedMessagePosition12;
                            }
                            else {
                                n70 = n69;
                            }
                            groupedMessagePosition12.set(n64, n64, n59, n59, n65, n62 / 814.0f, n70);
                            ++index2;
                            ++n64;
                            n63 = n66;
                        }
                        groupedMessagePosition11.pw += n63;
                        groupedMessagePosition11.spanSize += n63;
                        ++n59;
                        n60 = index2;
                    }
                }
                for (int index3 = 0; index3 < size; ++index3) {
                    final MessageObject.GroupedMessagePosition groupedMessagePosition13 = this.posArray.get(index3);
                    if ((groupedMessagePosition13.flags & 0x1) != 0x0) {
                        groupedMessagePosition13.edge = true;
                    }
                }
            }
            
            private class MessageGroupedLayoutAttempt
            {
                public float[] heights;
                public int[] lineCounts;
                
                public MessageGroupedLayoutAttempt(final int n, final int n2, final float n3, final float n4) {
                    this.lineCounts = new int[] { n, n2 };
                    this.heights = new float[] { n3, n4 };
                }
                
                public MessageGroupedLayoutAttempt(final int n, final int n2, final int n3, final float n4, final float n5, final float n6) {
                    this.lineCounts = new int[] { n, n2, n3 };
                    this.heights = new float[] { n4, n5, n6 };
                }
                
                public MessageGroupedLayoutAttempt(final int n, final int n2, final int n3, final int n4, final float n5, final float n6, final float n7, final float n8) {
                    this.lineCounts = new int[] { n, n2, n3, n4 };
                    this.heights = new float[] { n5, n6, n7, n8 };
                }
            }
        }
    }
    
    private class BlockDetailsBottomCell extends View
    {
        private RectF rect;
        
        public BlockDetailsBottomCell(final Context context) {
            super(context);
            this.rect = new RectF();
        }
        
        protected void onDraw(final Canvas canvas) {
            canvas.drawLine(0.0f, 0.0f, (float)this.getMeasuredWidth(), 0.0f, ArticleViewer.dividerPaint);
        }
        
        protected void onMeasure(final int n, final int n2) {
            this.setMeasuredDimension(View$MeasureSpec.getSize(n), AndroidUtilities.dp(4.0f) + 1);
        }
    }
    
    private class BlockDetailsCell extends View implements Drawable$Callback
    {
        private AnimatedArrowDrawable arrow;
        private TLRPC.TL_pageBlockDetails currentBlock;
        private WebpageAdapter parentAdapter;
        private DrawingText textLayout;
        private int textX;
        private int textY;
        
        public BlockDetailsCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textX = AndroidUtilities.dp(50.0f);
            this.textY = AndroidUtilities.dp(11.0f) + 1;
            this.parentAdapter = parentAdapter;
            this.arrow = new AnimatedArrowDrawable(ArticleViewer.this.getGrayTextColor(), true);
        }
        
        public void invalidateDrawable(final Drawable drawable) {
            this.invalidate();
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            canvas.save();
            canvas.translate((float)AndroidUtilities.dp(18.0f), (float)((this.getMeasuredHeight() - AndroidUtilities.dp(13.0f) - 1) / 2));
            this.arrow.draw(canvas);
            canvas.restore();
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
            final float n = (float)(this.getMeasuredHeight() - 1);
            canvas.drawLine(0.0f, n, (float)this.getMeasuredWidth(), n, ArticleViewer.dividerPaint);
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int n, int dp) {
            final int size = View$MeasureSpec.getSize(n);
            dp = AndroidUtilities.dp(39.0f);
            final TLRPC.TL_pageBlockDetails currentBlock = this.currentBlock;
            n = dp;
            if (currentBlock != null) {
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.RichText title = currentBlock.title;
                n = AndroidUtilities.dp(52.0f);
                final TLRPC.TL_pageBlockDetails currentBlock2 = this.currentBlock;
                Layout$Alignment layout$Alignment;
                if (ArticleViewer.this.isRtl) {
                    layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                }
                else {
                    layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                }
                this.textLayout = this$0.createLayoutForText(this, null, title, size - n, currentBlock2, layout$Alignment, this.parentAdapter);
                n = dp;
                if (this.textLayout != null) {
                    n = Math.max(dp, AndroidUtilities.dp(21.0f) + this.textLayout.getHeight());
                    this.textY = (this.textLayout.getHeight() + AndroidUtilities.dp(21.0f) - this.textLayout.getHeight()) / 2;
                }
            }
            this.setMeasuredDimension(size, n + 1);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent);
        }
        
        public void scheduleDrawable(final Drawable drawable, final Runnable runnable, final long n) {
        }
        
        public void setBlock(final TLRPC.TL_pageBlockDetails currentBlock) {
            this.currentBlock = currentBlock;
            final AnimatedArrowDrawable arrow = this.arrow;
            float animationProgress;
            if (currentBlock.open) {
                animationProgress = 0.0f;
            }
            else {
                animationProgress = 1.0f;
            }
            arrow.setAnimationProgress(animationProgress);
            this.arrow.setCallback((Drawable$Callback)this);
            this.requestLayout();
        }
        
        public void unscheduleDrawable(final Drawable drawable, final Runnable runnable) {
        }
    }
    
    private class BlockDividerCell extends View
    {
        private RectF rect;
        
        public BlockDividerCell(final Context context) {
            super(context);
            this.rect = new RectF();
        }
        
        protected void onDraw(final Canvas canvas) {
            final int n = this.getMeasuredWidth() / 3;
            this.rect.set((float)n, (float)AndroidUtilities.dp(8.0f), (float)(n * 2), (float)AndroidUtilities.dp(10.0f));
            canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(1.0f), (float)AndroidUtilities.dp(1.0f), ArticleViewer.dividerPaint);
        }
        
        protected void onMeasure(final int n, final int n2) {
            this.setMeasuredDimension(View$MeasureSpec.getSize(n), AndroidUtilities.dp(18.0f));
        }
    }
    
    private class BlockEmbedCell extends FrameLayout
    {
        private DrawingText captionLayout;
        private DrawingText creditLayout;
        private int creditOffset;
        private TLRPC.TL_pageBlockEmbed currentBlock;
        private int exactWebViewHeight;
        private int listX;
        private WebpageAdapter parentAdapter;
        private int textX;
        private int textY;
        final /* synthetic */ ArticleViewer this$0;
        private WebPlayerView videoView;
        private boolean wasUserInteraction;
        private TouchyWebView webView;
        
        @SuppressLint({ "SetJavaScriptEnabled", "AddJavascriptInterface" })
        public BlockEmbedCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.parentAdapter = parentAdapter;
            this.setWillNotDraw(false);
            this.addView((View)(this.videoView = new WebPlayerView(context, false, false, (WebPlayerView.WebPlayerViewDelegate)new WebPlayerView.WebPlayerViewDelegate() {
                @Override
                public boolean checkInlinePermissions() {
                    return false;
                }
                
                @Override
                public ViewGroup getTextureViewContainer() {
                    return null;
                }
                
                @Override
                public void onInitFailed() {
                    BlockEmbedCell.this.webView.setVisibility(0);
                    BlockEmbedCell.this.videoView.setVisibility(4);
                    BlockEmbedCell.this.videoView.loadVideo(null, null, null, null, false);
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("Referer", "http://youtube.com");
                    BlockEmbedCell.this.webView.loadUrl(BlockEmbedCell.this.currentBlock.url, (Map)hashMap);
                }
                
                @Override
                public void onInlineSurfaceTextureReady() {
                }
                
                @Override
                public void onPlayStateChanged(final WebPlayerView webPlayerView, final boolean b) {
                    if (b) {
                        if (ArticleViewer.this.currentPlayingVideo != null && ArticleViewer.this.currentPlayingVideo != webPlayerView) {
                            ArticleViewer.this.currentPlayingVideo.pause();
                        }
                        ArticleViewer.this.currentPlayingVideo = webPlayerView;
                        try {
                            ArticleViewer.this.parentActivity.getWindow().addFlags(128);
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                    else {
                        if (ArticleViewer.this.currentPlayingVideo == webPlayerView) {
                            ArticleViewer.this.currentPlayingVideo = null;
                        }
                        try {
                            ArticleViewer.this.parentActivity.getWindow().clearFlags(128);
                        }
                        catch (Exception ex2) {
                            FileLog.e(ex2);
                        }
                    }
                }
                
                @Override
                public void onSharePressed() {
                    if (ArticleViewer.this.parentActivity == null) {
                        return;
                    }
                    final ArticleViewer this$0 = ArticleViewer.this;
                    this$0.showDialog(new ShareAlert((Context)this$0.parentActivity, null, BlockEmbedCell.this.currentBlock.url, false, BlockEmbedCell.this.currentBlock.url, true));
                }
                
                @Override
                public TextureView onSwitchInlineMode(final View view, final boolean b, final float n, final int n2, final boolean b2) {
                    return null;
                }
                
                @Override
                public TextureView onSwitchToFullscreen(final View view, final boolean b, final float n, final int n2, final boolean b2) {
                    if (b) {
                        ArticleViewer.this.fullscreenAspectRatioView.addView((View)ArticleViewer.this.fullscreenTextureView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                        ArticleViewer.this.fullscreenAspectRatioView.setVisibility(0);
                        ArticleViewer.this.fullscreenAspectRatioView.setAspectRatio(n, n2);
                        final BlockEmbedCell this$1 = BlockEmbedCell.this;
                        this$1.this$0.fullscreenedVideo = this$1.videoView;
                        ArticleViewer.this.fullscreenVideoContainer.addView(view, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                        ArticleViewer.this.fullscreenVideoContainer.setVisibility(0);
                    }
                    else {
                        ArticleViewer.this.fullscreenAspectRatioView.removeView((View)ArticleViewer.this.fullscreenTextureView);
                        ArticleViewer.this.fullscreenedVideo = null;
                        ArticleViewer.this.fullscreenAspectRatioView.setVisibility(8);
                        ArticleViewer.this.fullscreenVideoContainer.setVisibility(4);
                    }
                    return ArticleViewer.this.fullscreenTextureView;
                }
                
                @Override
                public void onVideoSizeChanged(final float n, final int n2) {
                    ArticleViewer.this.fullscreenAspectRatioView.setAspectRatio(n, n2);
                }
                
                @Override
                public void prepareToSwitchInlineMode(final boolean b, final Runnable runnable, final float n, final boolean b2) {
                }
            })));
            ArticleViewer.this.createdWebViews.add(this);
            this.webView = new TouchyWebView(context);
            this.webView.getSettings().setJavaScriptEnabled(true);
            this.webView.getSettings().setDomStorageEnabled(true);
            this.webView.getSettings().setAllowContentAccess(true);
            if (Build$VERSION.SDK_INT >= 17) {
                this.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
                this.webView.addJavascriptInterface((Object)new TelegramWebviewProxy(), "TelegramWebviewProxy");
            }
            if (Build$VERSION.SDK_INT >= 21) {
                this.webView.getSettings().setMixedContentMode(0);
                CookieManager.getInstance().setAcceptThirdPartyCookies((WebView)this.webView, true);
            }
            this.webView.setWebChromeClient((WebChromeClient)new WebChromeClient() {
                public void onHideCustomView() {
                    super.onHideCustomView();
                    if (ArticleViewer.this.customView == null) {
                        return;
                    }
                    ArticleViewer.this.fullscreenVideoContainer.setVisibility(4);
                    ArticleViewer.this.fullscreenVideoContainer.removeView(ArticleViewer.this.customView);
                    if (ArticleViewer.this.customViewCallback != null && !ArticleViewer.this.customViewCallback.getClass().getName().contains(".chromium.")) {
                        ArticleViewer.this.customViewCallback.onCustomViewHidden();
                    }
                    ArticleViewer.this.customView = null;
                }
                
                public void onShowCustomView(final View view, final int n, final WebChromeClient$CustomViewCallback webChromeClient$CustomViewCallback) {
                    this.onShowCustomView(view, webChromeClient$CustomViewCallback);
                }
                
                public void onShowCustomView(final View view, final WebChromeClient$CustomViewCallback webChromeClient$CustomViewCallback) {
                    if (ArticleViewer.this.customView != null) {
                        webChromeClient$CustomViewCallback.onCustomViewHidden();
                        return;
                    }
                    ArticleViewer.this.customView = view;
                    ArticleViewer.this.customViewCallback = webChromeClient$CustomViewCallback;
                    AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$BlockEmbedCell$2$BQ0m5DiCWR6D1N_jvzFvaMkwwdA(this), 100L);
                }
            });
            this.webView.setWebViewClient((WebViewClient)new WebViewClient() {
                public void onLoadResource(final WebView webView, final String s) {
                    super.onLoadResource(webView, s);
                }
                
                public void onPageFinished(final WebView webView, final String s) {
                    super.onPageFinished(webView, s);
                }
                
                public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                    if (BlockEmbedCell.this.wasUserInteraction) {
                        Browser.openUrl((Context)ArticleViewer.this.parentActivity, s);
                        return true;
                    }
                    return false;
                }
            });
            this.addView((View)this.webView);
        }
        
        public void destroyWebView(final boolean b) {
            try {
                this.webView.stopLoading();
                this.webView.loadUrl("about:blank");
                if (b) {
                    this.webView.destroy();
                }
                this.currentBlock = null;
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            this.videoView.destroy();
        }
        
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
        }
        
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (!ArticleViewer.this.isVisible) {
                this.currentBlock = null;
            }
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.captionLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.captionLayout.draw(canvas);
                canvas.restore();
            }
            if (this.creditLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)(this.textY + this.creditOffset));
                this.creditLayout.draw(canvas);
                canvas.restore();
            }
            if (this.currentBlock.level > 0) {
                final float n = (float)AndroidUtilities.dp(18.0f);
                final float n2 = (float)AndroidUtilities.dp(20.0f);
                final int measuredHeight = this.getMeasuredHeight();
                int dp;
                if (this.currentBlock.bottom) {
                    dp = AndroidUtilities.dp(6.0f);
                }
                else {
                    dp = 0;
                }
                canvas.drawRect(n, 0.0f, n2, (float)(measuredHeight - dp), ArticleViewer.quoteLinePaint);
            }
        }
        
        protected void onLayout(final boolean b, int n, final int n2, final int n3, final int n4) {
            final TouchyWebView webView = this.webView;
            n = this.listX;
            webView.layout(n, 0, webView.getMeasuredWidth() + n, this.webView.getMeasuredHeight());
            if (this.videoView.getParent() == this) {
                final WebPlayerView videoView = this.videoView;
                n = this.listX;
                videoView.layout(n, 0, videoView.getMeasuredWidth() + n, this.videoView.getMeasuredHeight());
            }
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int n, int n2) {
            final int size = View$MeasureSpec.getSize(n);
            final TLRPC.TL_pageBlockEmbed currentBlock = this.currentBlock;
            Label_0561: {
                if (currentBlock != null) {
                    n = currentBlock.level;
                    int n4;
                    int n3;
                    if (n > 0) {
                        n = AndroidUtilities.dp((float)(n * 14)) + AndroidUtilities.dp(18.0f);
                        this.listX = n;
                        this.textX = n;
                        n3 = (n4 = size - (this.listX + AndroidUtilities.dp(18.0f)));
                    }
                    else {
                        this.listX = 0;
                        this.textX = AndroidUtilities.dp(18.0f);
                        n2 = AndroidUtilities.dp(36.0f);
                        if (!this.currentBlock.full_width) {
                            n = size - AndroidUtilities.dp(36.0f);
                            this.listX += AndroidUtilities.dp(18.0f);
                        }
                        else {
                            n = size;
                        }
                        n4 = size - n2;
                        n3 = n;
                    }
                    n = this.currentBlock.w;
                    float n5;
                    if (n == 0) {
                        n5 = 1.0f;
                    }
                    else {
                        n5 = size / (float)n;
                    }
                    n = this.exactWebViewHeight;
                    if (n != 0) {
                        n2 = AndroidUtilities.dp((float)n);
                    }
                    else {
                        final TLRPC.TL_pageBlockEmbed currentBlock2 = this.currentBlock;
                        if (currentBlock2.w == 0) {
                            n = AndroidUtilities.dp((float)currentBlock2.h);
                        }
                        else {
                            n = currentBlock2.h;
                        }
                        n2 = (int)(n * n5);
                    }
                    n = n2;
                    if (n2 == 0) {
                        n = AndroidUtilities.dp(10.0f);
                    }
                    n2 = n;
                    this.webView.measure(View$MeasureSpec.makeMeasureSpec(n3, 1073741824), View$MeasureSpec.makeMeasureSpec(n2, 1073741824));
                    if (this.videoView.getParent() == this) {
                        this.videoView.measure(View$MeasureSpec.makeMeasureSpec(n3, 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(10.0f) + n2, 1073741824));
                    }
                    final ArticleViewer this$0 = ArticleViewer.this;
                    final TLRPC.TL_pageBlockEmbed currentBlock3 = this.currentBlock;
                    this.captionLayout = this$0.createLayoutForText((View)this, null, currentBlock3.caption.text, n4, currentBlock3, this.parentAdapter);
                    n = n2;
                    if (this.captionLayout != null) {
                        this.textY = AndroidUtilities.dp(8.0f) + n2;
                        this.creditOffset = AndroidUtilities.dp(4.0f) + this.captionLayout.getHeight();
                        n = n2 + (this.creditOffset + AndroidUtilities.dp(4.0f));
                    }
                    final ArticleViewer this$2 = ArticleViewer.this;
                    final TLRPC.TL_pageBlockEmbed currentBlock4 = this.currentBlock;
                    final TLRPC.RichText credit = currentBlock4.caption.credit;
                    Layout$Alignment layout$Alignment;
                    if (this$2.isRtl) {
                        layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                    }
                    else {
                        layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                    }
                    this.creditLayout = this$2.createLayoutForText((View)this, null, credit, n4, currentBlock4, layout$Alignment, this.parentAdapter);
                    n2 = n;
                    if (this.creditLayout != null) {
                        n2 = n + (AndroidUtilities.dp(4.0f) + this.creditLayout.getHeight());
                    }
                    n2 += AndroidUtilities.dp(5.0f);
                    final TLRPC.TL_pageBlockEmbed currentBlock5 = this.currentBlock;
                    if (currentBlock5.level > 0 && !currentBlock5.bottom) {
                        n = AndroidUtilities.dp(8.0f);
                    }
                    else {
                        n = n2;
                        if (this.currentBlock.level != 0) {
                            break Label_0561;
                        }
                        n = n2;
                        if (this.captionLayout == null) {
                            break Label_0561;
                        }
                        n = AndroidUtilities.dp(8.0f);
                    }
                    n += n2;
                }
                else {
                    n = 1;
                }
            }
            this.setMeasuredDimension(size, n);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.captionLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.creditLayout, this.textX, this.textY + this.creditOffset) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockEmbed currentBlock) {
            final TLRPC.TL_pageBlockEmbed currentBlock2 = this.currentBlock;
            this.currentBlock = currentBlock;
            final TLRPC.TL_pageBlockEmbed currentBlock3 = this.currentBlock;
            if (currentBlock2 != currentBlock3) {
                this.wasUserInteraction = false;
                if (currentBlock3.allow_scrolling) {
                    this.webView.setVerticalScrollBarEnabled(true);
                    this.webView.setHorizontalScrollBarEnabled(true);
                }
                else {
                    this.webView.setVerticalScrollBarEnabled(false);
                    this.webView.setHorizontalScrollBarEnabled(false);
                }
                this.exactWebViewHeight = 0;
                try {
                    this.webView.loadUrl("about:blank");
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                try {
                    if (this.currentBlock.html != null) {
                        this.webView.loadDataWithBaseURL("https://telegram.org/embed", this.currentBlock.html, "text/html", "UTF-8", (String)null);
                        this.videoView.setVisibility(4);
                        this.videoView.loadVideo(null, null, null, null, false);
                        this.webView.setVisibility(0);
                    }
                    else {
                        TLRPC.Photo access$12800;
                        if (this.currentBlock.poster_photo_id != 0L) {
                            access$12800 = ArticleViewer.this.getPhotoWithId(this.currentBlock.poster_photo_id);
                        }
                        else {
                            access$12800 = null;
                        }
                        if (this.videoView.loadVideo(currentBlock.url, access$12800, ArticleViewer.this.currentPage, null, false)) {
                            this.webView.setVisibility(4);
                            this.videoView.setVisibility(0);
                            this.webView.stopLoading();
                            this.webView.loadUrl("about:blank");
                        }
                        else {
                            this.webView.setVisibility(0);
                            this.videoView.setVisibility(4);
                            this.videoView.loadVideo(null, null, null, null, false);
                            final HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("Referer", "http://youtube.com");
                            this.webView.loadUrl(this.currentBlock.url, (Map)hashMap);
                        }
                    }
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
            }
            this.requestLayout();
        }
        
        private class TelegramWebviewProxy
        {
            @JavascriptInterface
            public void postEvent(final String s, final String s2) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$BlockEmbedCell$TelegramWebviewProxy$9fT_04iuc3H2EZHCwJyjCPpU95o(this, s, s2));
            }
        }
        
        public class TouchyWebView extends WebView
        {
            public TouchyWebView(final Context context) {
                super(context);
                this.setFocusable(false);
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                BlockEmbedCell.this.wasUserInteraction = true;
                if (BlockEmbedCell.this.currentBlock != null) {
                    if (BlockEmbedCell.this.currentBlock.allow_scrolling) {
                        this.requestDisallowInterceptTouchEvent(true);
                    }
                    else {
                        ArticleViewer.this.windowView.requestDisallowInterceptTouchEvent(true);
                    }
                }
                return super.onTouchEvent(motionEvent);
            }
        }
    }
    
    private class BlockEmbedPostCell extends View
    {
        private AvatarDrawable avatarDrawable;
        private ImageReceiver avatarImageView;
        private boolean avatarVisible;
        private DrawingText captionLayout;
        private DrawingText creditLayout;
        private int creditOffset;
        private TLRPC.TL_pageBlockEmbedPost currentBlock;
        private DrawingText dateLayout;
        private int dateX;
        private int lineHeight;
        private DrawingText nameLayout;
        private int nameX;
        private WebpageAdapter parentAdapter;
        private int textX;
        private int textY;
        
        public BlockEmbedPostCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.parentAdapter = parentAdapter;
            (this.avatarImageView = new ImageReceiver(this)).setRoundRadius(AndroidUtilities.dp(20.0f));
            this.avatarImageView.setImageCoords(AndroidUtilities.dp(32.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
            this.avatarDrawable = new AvatarDrawable();
        }
        
        protected void onDraw(final Canvas canvas) {
            final TLRPC.TL_pageBlockEmbedPost currentBlock = this.currentBlock;
            if (currentBlock == null) {
                return;
            }
            if (!(currentBlock instanceof TL_pageBlockEmbedPostCaption)) {
                if (this.avatarVisible) {
                    this.avatarImageView.draw(canvas);
                }
                final DrawingText nameLayout = this.nameLayout;
                final int n = 54;
                final int n2 = 0;
                if (nameLayout != null) {
                    canvas.save();
                    int n3;
                    if (this.avatarVisible) {
                        n3 = 54;
                    }
                    else {
                        n3 = 0;
                    }
                    final float n4 = (float)AndroidUtilities.dp((float)(n3 + 32));
                    float n5;
                    if (this.dateLayout != null) {
                        n5 = 10.0f;
                    }
                    else {
                        n5 = 19.0f;
                    }
                    canvas.translate(n4, (float)AndroidUtilities.dp(n5));
                    this.nameLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.dateLayout != null) {
                    canvas.save();
                    int n6;
                    if (this.avatarVisible) {
                        n6 = n;
                    }
                    else {
                        n6 = 0;
                    }
                    canvas.translate((float)AndroidUtilities.dp((float)(n6 + 32)), (float)AndroidUtilities.dp(29.0f));
                    this.dateLayout.draw(canvas);
                    canvas.restore();
                }
                final float n7 = (float)AndroidUtilities.dp(18.0f);
                final float n8 = (float)AndroidUtilities.dp(6.0f);
                final float n9 = (float)AndroidUtilities.dp(20.0f);
                final int lineHeight = this.lineHeight;
                int dp;
                if (this.currentBlock.level != 0) {
                    dp = n2;
                }
                else {
                    dp = AndroidUtilities.dp(6.0f);
                }
                canvas.drawRect(n7, n8, n9, (float)(lineHeight - dp), ArticleViewer.quoteLinePaint);
            }
            if (this.captionLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.captionLayout.draw(canvas);
                canvas.restore();
            }
            if (this.creditLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)(this.textY + this.creditOffset));
                this.creditLayout.draw(canvas);
                canvas.restore();
            }
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int lineHeight, int dp) {
            final int size = View$MeasureSpec.getSize(lineHeight);
            final TLRPC.TL_pageBlockEmbedPost currentBlock = this.currentBlock;
            lineHeight = 1;
            if (currentBlock != null) {
                final boolean b = currentBlock instanceof TL_pageBlockEmbedPostCaption;
                dp = 0;
                lineHeight = 0;
                if (b) {
                    dp = size - AndroidUtilities.dp(50.0f);
                    final ArticleViewer this$0 = ArticleViewer.this;
                    final TLRPC.TL_pageBlockEmbedPost currentBlock2 = this.currentBlock;
                    this.captionLayout = this$0.createLayoutForText(this, null, currentBlock2.caption.text, dp, currentBlock2, this.parentAdapter);
                    if (this.captionLayout != null) {
                        this.creditOffset = AndroidUtilities.dp(4.0f) + this.captionLayout.getHeight();
                        lineHeight = 0 + (this.creditOffset + AndroidUtilities.dp(4.0f));
                    }
                    final ArticleViewer this$2 = ArticleViewer.this;
                    final TLRPC.TL_pageBlockEmbedPost currentBlock3 = this.currentBlock;
                    final TLRPC.RichText credit = currentBlock3.caption.credit;
                    Layout$Alignment layout$Alignment;
                    if (this$2.isRtl) {
                        layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                    }
                    else {
                        layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                    }
                    this.creditLayout = this$2.createLayoutForText(this, null, credit, dp, currentBlock3, layout$Alignment, this.parentAdapter);
                    dp = lineHeight;
                    if (this.creditLayout != null) {
                        dp = lineHeight + (AndroidUtilities.dp(4.0f) + this.creditLayout.getHeight());
                    }
                    this.textX = AndroidUtilities.dp(18.0f);
                    this.textY = AndroidUtilities.dp(4.0f);
                    lineHeight = dp;
                }
                else {
                    final boolean avatarVisible = currentBlock.author_photo_id != 0L;
                    this.avatarVisible = avatarVisible;
                    if (avatarVisible) {
                        final TLRPC.Photo access$12800 = ArticleViewer.this.getPhotoWithId(this.currentBlock.author_photo_id);
                        if (this.avatarVisible = (access$12800 instanceof TLRPC.TL_photo)) {
                            this.avatarDrawable.setInfo(0, this.currentBlock.author, null, false);
                            this.avatarImageView.setImage(ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(access$12800.sizes, AndroidUtilities.dp(40.0f), true), access$12800), "40_40", this.avatarDrawable, 0, null, ArticleViewer.this.currentPage, 1);
                        }
                    }
                    final ArticleViewer this$3 = ArticleViewer.this;
                    final String author = this.currentBlock.author;
                    if (this.avatarVisible) {
                        lineHeight = 54;
                    }
                    else {
                        lineHeight = 0;
                    }
                    this.nameLayout = this$3.createLayoutForText(this, author, null, size - AndroidUtilities.dp((float)(lineHeight + 50)), 0, this.currentBlock, Layout$Alignment.ALIGN_NORMAL, 1, this.parentAdapter);
                    if (this.currentBlock.date != 0) {
                        final ArticleViewer this$4 = ArticleViewer.this;
                        final String format = LocaleController.getInstance().chatFullDate.format(this.currentBlock.date * 1000L);
                        lineHeight = dp;
                        if (this.avatarVisible) {
                            lineHeight = 54;
                        }
                        this.dateLayout = this$4.createLayoutForText(this, format, null, size - AndroidUtilities.dp((float)(lineHeight + 50)), this.currentBlock, this.parentAdapter);
                    }
                    else {
                        this.dateLayout = null;
                    }
                    dp = AndroidUtilities.dp(56.0f);
                    if (this.currentBlock.blocks.isEmpty()) {
                        final int n = size - AndroidUtilities.dp(50.0f);
                        final ArticleViewer this$5 = ArticleViewer.this;
                        final TLRPC.TL_pageBlockEmbedPost currentBlock4 = this.currentBlock;
                        this.captionLayout = this$5.createLayoutForText(this, null, currentBlock4.caption.text, n, currentBlock4, this.parentAdapter);
                        lineHeight = dp;
                        if (this.captionLayout != null) {
                            this.creditOffset = AndroidUtilities.dp(4.0f) + this.captionLayout.getHeight();
                            lineHeight = dp + (this.creditOffset + AndroidUtilities.dp(4.0f));
                        }
                        dp = lineHeight;
                        final ArticleViewer this$6 = ArticleViewer.this;
                        final TLRPC.TL_pageBlockEmbedPost currentBlock5 = this.currentBlock;
                        final TLRPC.RichText credit2 = currentBlock5.caption.credit;
                        Layout$Alignment layout$Alignment2;
                        if (this$6.isRtl) {
                            layout$Alignment2 = Layout$Alignment.ALIGN_RIGHT;
                        }
                        else {
                            layout$Alignment2 = Layout$Alignment.ALIGN_NORMAL;
                        }
                        this.creditLayout = this$6.createLayoutForText(this, null, credit2, n, currentBlock5, layout$Alignment2, this.parentAdapter);
                        lineHeight = dp;
                        if (this.creditLayout != null) {
                            lineHeight = dp + (AndroidUtilities.dp(4.0f) + this.creditLayout.getHeight());
                        }
                        this.textX = AndroidUtilities.dp(32.0f);
                        this.textY = AndroidUtilities.dp(56.0f);
                    }
                    else {
                        this.captionLayout = null;
                        this.creditLayout = null;
                        lineHeight = dp;
                    }
                }
                this.lineHeight = lineHeight;
            }
            this.setMeasuredDimension(size, lineHeight);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.captionLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.creditLayout, this.textX, this.textY + this.creditOffset) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockEmbedPost currentBlock) {
            this.currentBlock = currentBlock;
            this.requestLayout();
        }
    }
    
    private class BlockFooterCell extends View
    {
        private TLRPC.TL_pageBlockFooter currentBlock;
        private WebpageAdapter parentAdapter;
        private DrawingText textLayout;
        private int textX;
        private int textY;
        
        public BlockFooterCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textX = AndroidUtilities.dp(18.0f);
            this.textY = AndroidUtilities.dp(8.0f);
            this.parentAdapter = parentAdapter;
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
            if (this.currentBlock.level > 0) {
                final float n = (float)AndroidUtilities.dp(18.0f);
                final float n2 = (float)AndroidUtilities.dp(20.0f);
                final int measuredHeight = this.getMeasuredHeight();
                int dp;
                if (this.currentBlock.bottom) {
                    dp = AndroidUtilities.dp(6.0f);
                }
                else {
                    dp = 0;
                }
                canvas.drawRect(n, 0.0f, n2, (float)(measuredHeight - dp), ArticleViewer.quoteLinePaint);
            }
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int n, int size) {
            size = View$MeasureSpec.getSize(n);
            final TLRPC.TL_pageBlockFooter currentBlock = this.currentBlock;
            n = 0;
            if (currentBlock != null) {
                final int level = currentBlock.level;
                if (level == 0) {
                    this.textY = AndroidUtilities.dp(8.0f);
                    this.textX = AndroidUtilities.dp(18.0f);
                }
                else {
                    this.textY = 0;
                    this.textX = AndroidUtilities.dp((float)(level * 14 + 18));
                }
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.RichText text = this.currentBlock.text;
                final int dp = AndroidUtilities.dp(18.0f);
                final int textX = this.textX;
                final TLRPC.TL_pageBlockFooter currentBlock2 = this.currentBlock;
                Layout$Alignment layout$Alignment;
                if (ArticleViewer.this.isRtl) {
                    layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                }
                else {
                    layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                }
                this.textLayout = this$0.createLayoutForText(this, null, text, size - dp - textX, currentBlock2, layout$Alignment, this.parentAdapter);
                final DrawingText textLayout = this.textLayout;
                if (textLayout != null) {
                    final int height = textLayout.getHeight();
                    if (this.currentBlock.level > 0) {
                        n = AndroidUtilities.dp(8.0f);
                    }
                    else {
                        n = AndroidUtilities.dp(16.0f);
                    }
                    n += height;
                }
            }
            else {
                n = 1;
            }
            this.setMeasuredDimension(size, n);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockFooter currentBlock) {
            this.currentBlock = currentBlock;
            this.requestLayout();
        }
    }
    
    private class BlockHeaderCell extends View
    {
        private TLRPC.TL_pageBlockHeader currentBlock;
        private WebpageAdapter parentAdapter;
        private DrawingText textLayout;
        private int textX;
        private int textY;
        
        public BlockHeaderCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textX = AndroidUtilities.dp(18.0f);
            this.textY = AndroidUtilities.dp(8.0f);
            this.parentAdapter = parentAdapter;
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
        
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setEnabled(true);
            if (this.textLayout == null) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append((Object)this.textLayout.getText());
            sb.append(", ");
            sb.append(LocaleController.getString("AccDescrIVHeading", 2131558439));
            accessibilityNodeInfo.setText((CharSequence)sb.toString());
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int n, int size) {
            size = View$MeasureSpec.getSize(n);
            final TLRPC.TL_pageBlockHeader currentBlock = this.currentBlock;
            n = 0;
            if (currentBlock != null) {
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.RichText text = currentBlock.text;
                final int dp = AndroidUtilities.dp(36.0f);
                final TLRPC.TL_pageBlockHeader currentBlock2 = this.currentBlock;
                Layout$Alignment layout$Alignment;
                if (ArticleViewer.this.isRtl) {
                    layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                }
                else {
                    layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                }
                this.textLayout = this$0.createLayoutForText(this, null, text, size - dp, currentBlock2, layout$Alignment, this.parentAdapter);
                if (this.textLayout != null) {
                    n = 0 + (AndroidUtilities.dp(16.0f) + this.textLayout.getHeight());
                }
            }
            else {
                n = 1;
            }
            this.setMeasuredDimension(size, n);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockHeader currentBlock) {
            this.currentBlock = currentBlock;
            this.requestLayout();
        }
    }
    
    private class BlockKickerCell extends View
    {
        private TLRPC.TL_pageBlockKicker currentBlock;
        private WebpageAdapter parentAdapter;
        private DrawingText textLayout;
        private int textX;
        private int textY;
        
        public BlockKickerCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textX = AndroidUtilities.dp(18.0f);
            this.parentAdapter = parentAdapter;
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int dp, int size) {
            size = View$MeasureSpec.getSize(dp);
            final TLRPC.TL_pageBlockKicker currentBlock = this.currentBlock;
            if (currentBlock != null) {
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.RichText text = currentBlock.text;
                dp = AndroidUtilities.dp(36.0f);
                final TLRPC.TL_pageBlockKicker currentBlock2 = this.currentBlock;
                Layout$Alignment layout$Alignment;
                if (ArticleViewer.this.isRtl) {
                    layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                }
                else {
                    layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                }
                this.textLayout = this$0.createLayoutForText(this, null, text, size - dp, currentBlock2, layout$Alignment, this.parentAdapter);
                final DrawingText textLayout = this.textLayout;
                dp = 0;
                if (textLayout != null) {
                    dp = 0 + (AndroidUtilities.dp(16.0f) + this.textLayout.getHeight());
                }
                if (this.currentBlock.first) {
                    dp += AndroidUtilities.dp(8.0f);
                    this.textY = AndroidUtilities.dp(16.0f);
                }
                else {
                    this.textY = AndroidUtilities.dp(8.0f);
                }
            }
            else {
                dp = 1;
            }
            this.setMeasuredDimension(size, dp);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockKicker currentBlock) {
            this.currentBlock = currentBlock;
            this.requestLayout();
        }
    }
    
    private class BlockListItemCell extends ViewGroup
    {
        private RecyclerView.ViewHolder blockLayout;
        private int blockX;
        private int blockY;
        private TL_pageBlockListItem currentBlock;
        private int currentBlockType;
        private boolean drawDot;
        private int numOffsetY;
        private WebpageAdapter parentAdapter;
        private boolean parentIsList;
        private DrawingText textLayout;
        private int textX;
        private int textY;
        private boolean verticalAlign;
        
        public BlockListItemCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.parentAdapter = parentAdapter;
            this.setWillNotDraw(false);
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            final int measuredWidth = this.getMeasuredWidth();
            if (this.currentBlock.numLayout != null) {
                canvas.save();
                final boolean access$11800 = ArticleViewer.this.isRtl;
                final int n = 0;
                int dp = 0;
                if (access$11800) {
                    final float n2 = (float)(measuredWidth - AndroidUtilities.dp(15.0f) - this.currentBlock.parent.maxNumWidth - this.currentBlock.parent.level * AndroidUtilities.dp(12.0f));
                    final int textY = this.textY;
                    final int numOffsetY = this.numOffsetY;
                    if (this.drawDot) {
                        dp = AndroidUtilities.dp(1.0f);
                    }
                    canvas.translate(n2, (float)(textY + numOffsetY - dp));
                }
                else {
                    final float n3 = (float)(AndroidUtilities.dp(15.0f) + this.currentBlock.parent.maxNumWidth - (int)Math.ceil(this.currentBlock.numLayout.getLineWidth(0)) + this.currentBlock.parent.level * AndroidUtilities.dp(12.0f));
                    final int textY2 = this.textY;
                    final int numOffsetY2 = this.numOffsetY;
                    int dp2 = n;
                    if (this.drawDot) {
                        dp2 = AndroidUtilities.dp(1.0f);
                    }
                    canvas.translate(n3, (float)(textY2 + numOffsetY2 - dp2));
                }
                this.currentBlock.numLayout.draw(canvas);
                canvas.restore();
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
        
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setEnabled(true);
            final DrawingText textLayout = this.textLayout;
            if (textLayout == null) {
                return;
            }
            accessibilityNodeInfo.setText(textLayout.getText());
        }
        
        protected void onLayout(final boolean b, int blockX, final int n, final int n2, final int n3) {
            final RecyclerView.ViewHolder blockLayout = this.blockLayout;
            if (blockLayout != null) {
                final View itemView = blockLayout.itemView;
                blockX = this.blockX;
                itemView.layout(blockX, this.blockY, itemView.getMeasuredWidth() + blockX, this.blockY + this.blockLayout.itemView.getMeasuredHeight());
            }
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int i, int n) {
            final int size = View$MeasureSpec.getSize(i);
            final TL_pageBlockListItem currentBlock = this.currentBlock;
            i = 1;
            if (currentBlock != null) {
                this.textLayout = null;
                i = currentBlock.index;
                final int n2 = 0;
                final int n3 = 0;
                final int n4 = 0;
                if (i == 0 && this.currentBlock.parent.level == 0) {
                    i = AndroidUtilities.dp(10.0f);
                }
                else {
                    i = 0;
                }
                this.textY = i;
                this.numOffsetY = 0;
                if (this.currentBlock.parent.lastMaxNumCalcWidth != size || this.currentBlock.parent.lastFontSize != ArticleViewer.this.selectedFontSize) {
                    this.currentBlock.parent.lastMaxNumCalcWidth = size;
                    this.currentBlock.parent.lastFontSize = ArticleViewer.this.selectedFontSize;
                    this.currentBlock.parent.maxNumWidth = 0;
                    TL_pageBlockListItem tl_pageBlockListItem;
                    for (n = this.currentBlock.parent.items.size(), i = 0; i < n; ++i) {
                        tl_pageBlockListItem = this.currentBlock.parent.items.get(i);
                        if (tl_pageBlockListItem.num != null) {
                            tl_pageBlockListItem.numLayout = ArticleViewer.this.createLayoutForText((View)this, tl_pageBlockListItem.num, null, size - AndroidUtilities.dp(54.0f), this.currentBlock, this.parentAdapter);
                            this.currentBlock.parent.maxNumWidth = Math.max(this.currentBlock.parent.maxNumWidth, (int)Math.ceil(tl_pageBlockListItem.numLayout.getLineWidth(0)));
                        }
                    }
                    this.currentBlock.parent.maxNumWidth = Math.max(this.currentBlock.parent.maxNumWidth, (int)Math.ceil(ArticleViewer.listTextNumPaint.measureText("00.")));
                }
                this.drawDot = (this.currentBlock.parent.pageBlockList.ordered ^ true);
                this.parentIsList = (this.getParent() instanceof BlockListItemCell || this.getParent() instanceof BlockOrderedListItemCell);
                if (ArticleViewer.this.isRtl) {
                    this.textX = AndroidUtilities.dp(18.0f);
                }
                else {
                    this.textX = AndroidUtilities.dp(24.0f) + this.currentBlock.parent.maxNumWidth + this.currentBlock.parent.level * AndroidUtilities.dp(12.0f);
                }
                n = (i = size - AndroidUtilities.dp(18.0f) - this.textX);
                if (ArticleViewer.this.isRtl) {
                    i = n - (AndroidUtilities.dp(6.0f) + this.currentBlock.parent.maxNumWidth + this.currentBlock.parent.level * AndroidUtilities.dp(12.0f));
                }
                Label_1326: {
                    if (this.currentBlock.textItem != null) {
                        final ArticleViewer this$0 = ArticleViewer.this;
                        final TLRPC.RichText access$10400 = this.currentBlock.textItem;
                        final TL_pageBlockListItem currentBlock2 = this.currentBlock;
                        Layout$Alignment layout$Alignment;
                        if (ArticleViewer.this.isRtl) {
                            layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                        }
                        else {
                            layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                        }
                        this.textLayout = this$0.createLayoutForText((View)this, null, access$10400, i, currentBlock2, layout$Alignment, this.parentAdapter);
                        final DrawingText textLayout = this.textLayout;
                        n = n3;
                        if (textLayout == null) {
                            break Label_1326;
                        }
                        n = n3;
                        if (textLayout.getLineCount() <= 0) {
                            break Label_1326;
                        }
                        if (this.currentBlock.numLayout != null && this.currentBlock.numLayout.getLineCount() > 0) {
                            i = this.textLayout.getLineAscent(0);
                            this.numOffsetY = this.currentBlock.numLayout.getLineAscent(0) + AndroidUtilities.dp(2.5f) - i;
                        }
                        i = this.textLayout.getHeight() + AndroidUtilities.dp(8.0f);
                        n = n2;
                    }
                    else {
                        n = n3;
                        if (this.currentBlock.blockItem == null) {
                            break Label_1326;
                        }
                        this.blockX = this.textX;
                        this.blockY = this.textY;
                        final RecyclerView.ViewHolder blockLayout = this.blockLayout;
                        n = n4;
                        if (blockLayout != null) {
                            final View itemView = blockLayout.itemView;
                            Label_0995: {
                                if (itemView instanceof BlockParagraphCell) {
                                    this.blockY -= AndroidUtilities.dp(8.0f);
                                    if (!ArticleViewer.this.isRtl) {
                                        this.blockX -= AndroidUtilities.dp(18.0f);
                                    }
                                    n = AndroidUtilities.dp(18.0f) + i;
                                    i = 0 - AndroidUtilities.dp(8.0f);
                                }
                                else {
                                    Label_0993: {
                                        if (!(itemView instanceof BlockHeaderCell) && !(itemView instanceof BlockSubheaderCell) && !(itemView instanceof BlockTitleCell) && !(itemView instanceof BlockSubtitleCell)) {
                                            if (ArticleViewer.this.isListItemBlock(this.currentBlock.blockItem)) {
                                                this.blockX = 0;
                                                this.blockY = 0;
                                                this.textY = 0;
                                                if (this.currentBlock.index == 0 && this.currentBlock.parent.level == 0) {
                                                    i = 0 - AndroidUtilities.dp(10.0f);
                                                }
                                                else {
                                                    i = 0;
                                                }
                                                i -= AndroidUtilities.dp(8.0f);
                                                n = size;
                                                break Label_0995;
                                            }
                                            if (!(this.blockLayout.itemView instanceof BlockTableCell)) {
                                                n = i;
                                                break Label_0993;
                                            }
                                            this.blockX -= AndroidUtilities.dp(18.0f);
                                            n = AndroidUtilities.dp(36.0f);
                                        }
                                        else {
                                            if (!ArticleViewer.this.isRtl) {
                                                this.blockX -= AndroidUtilities.dp(18.0f);
                                            }
                                            n = AndroidUtilities.dp(18.0f);
                                        }
                                        n += i;
                                    }
                                    i = 0;
                                }
                            }
                            this.blockLayout.itemView.measure(View$MeasureSpec.makeMeasureSpec(n, 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
                            if (this.blockLayout.itemView instanceof BlockParagraphCell && this.currentBlock.numLayout != null && this.currentBlock.numLayout.getLineCount() > 0) {
                                final BlockParagraphCell blockParagraphCell = (BlockParagraphCell)this.blockLayout.itemView;
                                if (blockParagraphCell.textLayout != null && blockParagraphCell.textLayout.getLineCount() > 0) {
                                    n = blockParagraphCell.textLayout.getLineAscent(0);
                                    this.numOffsetY = this.currentBlock.numLayout.getLineAscent(0) + AndroidUtilities.dp(2.5f) - n;
                                }
                            }
                            if (this.currentBlock.blockItem instanceof TLRPC.TL_pageBlockDetails) {
                                this.verticalAlign = true;
                                this.blockY = 0;
                                n = i;
                                if (this.currentBlock.index == 0) {
                                    n = i;
                                    if (this.currentBlock.parent.level == 0) {
                                        n = i - AndroidUtilities.dp(10.0f);
                                    }
                                }
                                n -= AndroidUtilities.dp(8.0f);
                            }
                            else {
                                final View itemView2 = this.blockLayout.itemView;
                                if (itemView2 instanceof BlockOrderedListItemCell) {
                                    this.verticalAlign = ((BlockOrderedListItemCell)itemView2).verticalAlign;
                                    n = i;
                                }
                                else {
                                    n = i;
                                    if (itemView2 instanceof BlockListItemCell) {
                                        this.verticalAlign = ((BlockListItemCell)itemView2).verticalAlign;
                                        n = i;
                                    }
                                }
                            }
                            if (this.verticalAlign && this.currentBlock.numLayout != null) {
                                this.textY = (this.blockLayout.itemView.getMeasuredHeight() - this.currentBlock.numLayout.getHeight()) / 2 - AndroidUtilities.dp(4.0f);
                                this.drawDot = false;
                            }
                            n += this.blockLayout.itemView.getMeasuredHeight();
                        }
                        i = AndroidUtilities.dp(8.0f);
                    }
                    n += i;
                }
                i = n;
                if (this.currentBlock.parent.items.get(this.currentBlock.parent.items.size() - 1) == this.currentBlock) {
                    i = n + AndroidUtilities.dp(8.0f);
                }
                n = i;
                if (this.currentBlock.index == 0) {
                    n = i;
                    if (this.currentBlock.parent.level == 0) {
                        n = i + AndroidUtilities.dp(10.0f);
                    }
                }
                i = n;
            }
            this.setMeasuredDimension(size, i);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.textLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TL_pageBlockListItem currentBlock) {
            if (this.currentBlock != currentBlock) {
                this.currentBlock = currentBlock;
                final RecyclerView.ViewHolder blockLayout = this.blockLayout;
                if (blockLayout != null) {
                    this.removeView(blockLayout.itemView);
                    this.blockLayout = null;
                }
                if (this.currentBlock.blockItem != null) {
                    this.currentBlockType = this.parentAdapter.getTypeForBlock(this.currentBlock.blockItem);
                    this.blockLayout = this.parentAdapter.onCreateViewHolder(this, this.currentBlockType);
                    this.addView(this.blockLayout.itemView);
                }
            }
            if (this.currentBlock.blockItem != null) {
                this.parentAdapter.bindBlockToHolder(this.currentBlockType, this.blockLayout, this.currentBlock.blockItem, 0, 0);
            }
            this.requestLayout();
        }
    }
    
    private class BlockMapCell extends FrameLayout
    {
        private DrawingText captionLayout;
        private DrawingText creditLayout;
        private int creditOffset;
        private TLRPC.TL_pageBlockMap currentBlock;
        private int currentMapProvider;
        private int currentType;
        private ImageReceiver imageView;
        private boolean isFirst;
        private boolean isLast;
        private WebpageAdapter parentAdapter;
        private boolean photoPressed;
        private int textX;
        private int textY;
        
        public BlockMapCell(final Context context, final WebpageAdapter parentAdapter, final int currentType) {
            super(context);
            this.parentAdapter = parentAdapter;
            this.setWillNotDraw(false);
            this.imageView = new ImageReceiver((View)this);
            this.currentType = currentType;
        }
        
        protected void onDraw(final Canvas canvas) {
            final int currentType = this.currentType;
            if (this.currentBlock == null) {
                return;
            }
            this.imageView.draw(canvas);
            if (this.currentMapProvider == 2 && this.imageView.hasNotThumb()) {
                final int n = (int)(Theme.chat_redLocationIcon.getIntrinsicWidth() * 0.8f);
                final int n2 = (int)(Theme.chat_redLocationIcon.getIntrinsicHeight() * 0.8f);
                final int n3 = this.imageView.getImageX() + (this.imageView.getImageWidth() - n) / 2;
                final int n4 = this.imageView.getImageY() + (this.imageView.getImageHeight() / 2 - n2);
                Theme.chat_redLocationIcon.setAlpha((int)(this.imageView.getCurrentAlpha() * 255.0f));
                Theme.chat_redLocationIcon.setBounds(n3, n4, n + n3, n2 + n4);
                Theme.chat_redLocationIcon.draw(canvas);
            }
            this.textY = this.imageView.getImageY() + this.imageView.getImageHeight() + AndroidUtilities.dp(8.0f);
            if (this.captionLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.captionLayout.draw(canvas);
                canvas.restore();
            }
            if (this.creditLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)(this.textY + this.creditOffset));
                this.creditLayout.draw(canvas);
                canvas.restore();
            }
            if (this.currentBlock.level > 0) {
                final float n5 = (float)AndroidUtilities.dp(18.0f);
                final float n6 = (float)AndroidUtilities.dp(20.0f);
                final int measuredHeight = this.getMeasuredHeight();
                int dp;
                if (this.currentBlock.bottom) {
                    dp = AndroidUtilities.dp(6.0f);
                }
                else {
                    dp = 0;
                }
                canvas.drawRect(n5, 0.0f, n6, (float)(measuredHeight - dp), ArticleViewer.quoteLinePaint);
            }
        }
        
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setEnabled(true);
            final StringBuilder sb = new StringBuilder(LocaleController.getString("Map", 2131559800));
            if (this.captionLayout != null) {
                sb.append(", ");
                sb.append(this.captionLayout.getText());
            }
            accessibilityNodeInfo.setText((CharSequence)sb.toString());
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int n, int n2) {
            n2 = View$MeasureSpec.getSize(n);
            n = this.currentType;
            int n3 = 1;
            if (n == 1) {
                n = ((View)this.getParent()).getMeasuredWidth();
                n2 = ((View)this.getParent()).getMeasuredHeight();
            }
            else if (n == 2) {
                n = n2;
            }
            else {
                n = n2;
                n2 = 0;
            }
            final TLRPC.TL_pageBlockMap currentBlock = this.currentBlock;
            if (currentBlock != null) {
                int textX = 0;
                int n5 = 0;
                int n4 = 0;
                Label_0145: {
                    if (this.currentType == 0) {
                        final int level = currentBlock.level;
                        if (level > 0) {
                            textX = AndroidUtilities.dp((float)(level * 14)) + AndroidUtilities.dp(18.0f);
                            this.textX = textX;
                            n4 = (n5 = n - (AndroidUtilities.dp(18.0f) + textX));
                            break Label_0145;
                        }
                    }
                    this.textX = AndroidUtilities.dp(18.0f);
                    n5 = n - AndroidUtilities.dp(36.0f);
                    n4 = n;
                    textX = 0;
                }
                if (this.currentType == 0) {
                    final float n6 = (float)n4;
                    final TLRPC.TL_pageBlockMap currentBlock2 = this.currentBlock;
                    final int n7 = (int)(n6 / currentBlock2.w * currentBlock2.h);
                    final int n8 = (int)((Math.max(ArticleViewer.this.listView[0].getMeasuredWidth(), ArticleViewer.this.listView[0].getMeasuredHeight()) - AndroidUtilities.dp(56.0f)) * 0.9f);
                    if ((n2 = n7) > n8) {
                        final float n9 = (float)n8;
                        final TLRPC.TL_pageBlockMap currentBlock3 = this.currentBlock;
                        n4 = (int)(n9 / currentBlock3.h * currentBlock3.w);
                        textX += (n - textX - n4) / 2;
                        n2 = n8;
                    }
                }
                final ImageReceiver imageView = this.imageView;
                int dp = 0;
                Label_0336: {
                    if (!this.isFirst) {
                        final int currentType = this.currentType;
                        if (currentType != 1 && currentType != 2) {
                            if (this.currentBlock.level <= 0) {
                                dp = AndroidUtilities.dp(8.0f);
                                break Label_0336;
                            }
                        }
                    }
                    dp = 0;
                }
                imageView.setImageCoords(textX, dp, n4, n2);
                final int access$9200 = ArticleViewer.this.currentAccount;
                final TLRPC.GeoPoint geo = this.currentBlock.geo;
                final double lat = geo.lat;
                final double long1 = geo._long;
                final float n10 = (float)n4;
                final float density = AndroidUtilities.density;
                final int n11 = (int)(n10 / density);
                final float n12 = (float)n2;
                final String formapMapUrl = AndroidUtilities.formapMapUrl(access$9200, lat, long1, n11, (int)(n12 / density), true, 15);
                final TLRPC.GeoPoint geo2 = this.currentBlock.geo;
                final float density2 = AndroidUtilities.density;
                final WebFile withGeoPoint = WebFile.createWithGeoPoint(geo2, (int)(n10 / density2), (int)(n12 / density2), 15, Math.min(2, (int)Math.ceil(density2)));
                this.currentMapProvider = MessagesController.getInstance(ArticleViewer.this.currentAccount).mapProvider;
                if (this.currentMapProvider == 2) {
                    if (withGeoPoint != null) {
                        this.imageView.setImage(ImageLocation.getForWebFile(withGeoPoint), null, Theme.chat_locationDrawable[0], null, ArticleViewer.this.currentPage, 0);
                    }
                }
                else if (formapMapUrl != null) {
                    this.imageView.setImage(formapMapUrl, null, Theme.chat_locationDrawable[0], null, 0);
                }
                int n13 = n2;
                if (this.currentType == 0) {
                    final ArticleViewer this$0 = ArticleViewer.this;
                    final TLRPC.TL_pageBlockMap currentBlock4 = this.currentBlock;
                    this.captionLayout = this$0.createLayoutForText((View)this, null, currentBlock4.caption.text, n5, currentBlock4, this.parentAdapter);
                    int n14 = n2;
                    if (this.captionLayout != null) {
                        this.creditOffset = AndroidUtilities.dp(4.0f) + this.captionLayout.getHeight();
                        n14 = n2 + (this.creditOffset + AndroidUtilities.dp(4.0f));
                    }
                    final ArticleViewer this$2 = ArticleViewer.this;
                    final TLRPC.TL_pageBlockMap currentBlock5 = this.currentBlock;
                    final TLRPC.RichText credit = currentBlock5.caption.credit;
                    Layout$Alignment layout$Alignment;
                    if (this$2.isRtl) {
                        layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                    }
                    else {
                        layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                    }
                    this.creditLayout = this$2.createLayoutForText((View)this, null, credit, n5, currentBlock5, layout$Alignment, this.parentAdapter);
                    n13 = n14;
                    if (this.creditLayout != null) {
                        n13 = n14 + (AndroidUtilities.dp(4.0f) + this.creditLayout.getHeight());
                    }
                }
                n2 = n13;
                if (!this.isFirst) {
                    n2 = n13;
                    if (this.currentType == 0) {
                        n2 = n13;
                        if (this.currentBlock.level <= 0) {
                            n2 = n13 + AndroidUtilities.dp(8.0f);
                        }
                    }
                }
                n3 = n2;
                if (this.currentType != 2) {
                    n3 = n2 + AndroidUtilities.dp(8.0f);
                }
            }
            this.setMeasuredDimension(n, n3);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final float x = motionEvent.getX();
            final float y = motionEvent.getY();
            final int action = motionEvent.getAction();
            boolean b = false;
            if (action == 0 && this.imageView.isInsideImage(x, y)) {
                this.photoPressed = true;
            }
            else if (motionEvent.getAction() == 1 && this.photoPressed) {
                this.photoPressed = false;
                try {
                    final double lat = this.currentBlock.geo.lat;
                    final double long1 = this.currentBlock.geo._long;
                    final Activity access$2500 = ArticleViewer.this.parentActivity;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("geo:");
                    sb.append(lat);
                    sb.append(",");
                    sb.append(long1);
                    sb.append("?q=");
                    sb.append(lat);
                    sb.append(",");
                    sb.append(long1);
                    access$2500.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            else if (motionEvent.getAction() == 3) {
                this.photoPressed = false;
            }
            if (this.photoPressed || ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.captionLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.creditLayout, this.textX, this.textY + this.creditOffset) || super.onTouchEvent(motionEvent)) {
                b = true;
            }
            return b;
        }
        
        public void setBlock(final TLRPC.TL_pageBlockMap currentBlock, final boolean isFirst, final boolean isLast) {
            this.currentBlock = currentBlock;
            this.isFirst = isFirst;
            this.isLast = isLast;
            this.requestLayout();
        }
    }
    
    private class BlockOrderedListItemCell extends ViewGroup
    {
        private RecyclerView.ViewHolder blockLayout;
        private int blockX;
        private int blockY;
        private TL_pageBlockOrderedListItem currentBlock;
        private int currentBlockType;
        private int numOffsetY;
        private WebpageAdapter parentAdapter;
        private boolean parentIsList;
        private DrawingText textLayout;
        private int textX;
        private int textY;
        private boolean verticalAlign;
        
        public BlockOrderedListItemCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.parentAdapter = parentAdapter;
            this.setWillNotDraw(false);
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            final int measuredWidth = this.getMeasuredWidth();
            if (this.currentBlock.numLayout != null) {
                canvas.save();
                if (ArticleViewer.this.isRtl) {
                    canvas.translate((float)(measuredWidth - AndroidUtilities.dp(18.0f) - this.currentBlock.parent.maxNumWidth - this.currentBlock.parent.level * AndroidUtilities.dp(20.0f)), (float)(this.textY + this.numOffsetY));
                }
                else {
                    canvas.translate((float)(AndroidUtilities.dp(18.0f) + this.currentBlock.parent.maxNumWidth - (int)Math.ceil(this.currentBlock.numLayout.getLineWidth(0)) + this.currentBlock.parent.level * AndroidUtilities.dp(20.0f)), (float)(this.textY + this.numOffsetY));
                }
                this.currentBlock.numLayout.draw(canvas);
                canvas.restore();
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
        
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setEnabled(true);
            final DrawingText textLayout = this.textLayout;
            if (textLayout == null) {
                return;
            }
            accessibilityNodeInfo.setText(textLayout.getText());
        }
        
        protected void onLayout(final boolean b, int blockX, final int n, final int n2, final int n3) {
            final RecyclerView.ViewHolder blockLayout = this.blockLayout;
            if (blockLayout != null) {
                final View itemView = blockLayout.itemView;
                blockX = this.blockX;
                itemView.layout(blockX, this.blockY, itemView.getMeasuredWidth() + blockX, this.blockY + this.blockLayout.itemView.getMeasuredHeight());
            }
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int i, int n) {
            final int size = View$MeasureSpec.getSize(i);
            final TL_pageBlockOrderedListItem currentBlock = this.currentBlock;
            n = 1;
            if (currentBlock != null) {
                this.textLayout = null;
                i = currentBlock.index;
                final int n2 = 0;
                final int n3 = 0;
                final int n4 = 0;
                if (i == 0 && this.currentBlock.parent.level == 0) {
                    i = AndroidUtilities.dp(10.0f);
                }
                else {
                    i = 0;
                }
                this.textY = i;
                this.numOffsetY = 0;
                if (this.currentBlock.parent.lastMaxNumCalcWidth != size || this.currentBlock.parent.lastFontSize != ArticleViewer.this.selectedFontSize) {
                    this.currentBlock.parent.lastMaxNumCalcWidth = size;
                    this.currentBlock.parent.lastFontSize = ArticleViewer.this.selectedFontSize;
                    this.currentBlock.parent.maxNumWidth = 0;
                    TL_pageBlockOrderedListItem tl_pageBlockOrderedListItem;
                    for (n = this.currentBlock.parent.items.size(), i = 0; i < n; ++i) {
                        tl_pageBlockOrderedListItem = this.currentBlock.parent.items.get(i);
                        if (tl_pageBlockOrderedListItem.num != null) {
                            tl_pageBlockOrderedListItem.numLayout = ArticleViewer.this.createLayoutForText((View)this, tl_pageBlockOrderedListItem.num, null, size - AndroidUtilities.dp(54.0f), this.currentBlock, this.parentAdapter);
                            this.currentBlock.parent.maxNumWidth = Math.max(this.currentBlock.parent.maxNumWidth, (int)Math.ceil(tl_pageBlockOrderedListItem.numLayout.getLineWidth(0)));
                        }
                    }
                    this.currentBlock.parent.maxNumWidth = Math.max(this.currentBlock.parent.maxNumWidth, (int)Math.ceil(ArticleViewer.listTextNumPaint.measureText("00.")));
                }
                if (ArticleViewer.this.isRtl) {
                    this.textX = AndroidUtilities.dp(18.0f);
                }
                else {
                    this.textX = AndroidUtilities.dp(24.0f) + this.currentBlock.parent.maxNumWidth + this.currentBlock.parent.level * AndroidUtilities.dp(20.0f);
                }
                this.verticalAlign = false;
                n = (i = size - AndroidUtilities.dp(18.0f) - this.textX);
                if (ArticleViewer.this.isRtl) {
                    i = n - (AndroidUtilities.dp(6.0f) + this.currentBlock.parent.maxNumWidth + this.currentBlock.parent.level * AndroidUtilities.dp(20.0f));
                }
                Label_1168: {
                    if (this.currentBlock.textItem != null) {
                        final ArticleViewer this$0 = ArticleViewer.this;
                        final TLRPC.RichText access$10500 = this.currentBlock.textItem;
                        final TL_pageBlockOrderedListItem currentBlock2 = this.currentBlock;
                        Layout$Alignment layout$Alignment;
                        if (ArticleViewer.this.isRtl) {
                            layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                        }
                        else {
                            layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                        }
                        this.textLayout = this$0.createLayoutForText((View)this, null, access$10500, i, currentBlock2, layout$Alignment, this.parentAdapter);
                        final DrawingText textLayout = this.textLayout;
                        n = n3;
                        if (textLayout == null) {
                            break Label_1168;
                        }
                        n = n3;
                        if (textLayout.getLineCount() <= 0) {
                            break Label_1168;
                        }
                        if (this.currentBlock.numLayout != null && this.currentBlock.numLayout.getLineCount() > 0) {
                            i = this.textLayout.getLineAscent(0);
                            this.numOffsetY = this.currentBlock.numLayout.getLineAscent(0) - i;
                        }
                        i = this.textLayout.getHeight() + AndroidUtilities.dp(8.0f);
                        n = n2;
                    }
                    else {
                        n = n3;
                        if (this.currentBlock.blockItem == null) {
                            break Label_1168;
                        }
                        this.blockX = this.textX;
                        this.blockY = this.textY;
                        final RecyclerView.ViewHolder blockLayout = this.blockLayout;
                        n = n4;
                        if (blockLayout != null) {
                            final View itemView = blockLayout.itemView;
                            Label_0891: {
                                if (itemView instanceof BlockParagraphCell) {
                                    this.blockY -= AndroidUtilities.dp(8.0f);
                                    if (!ArticleViewer.this.isRtl) {
                                        this.blockX -= AndroidUtilities.dp(18.0f);
                                    }
                                    n = AndroidUtilities.dp(18.0f) + i;
                                    i = 0 - AndroidUtilities.dp(8.0f);
                                }
                                else {
                                    Label_0889: {
                                        if (!(itemView instanceof BlockHeaderCell) && !(itemView instanceof BlockSubheaderCell) && !(itemView instanceof BlockTitleCell) && !(itemView instanceof BlockSubtitleCell)) {
                                            if (ArticleViewer.this.isListItemBlock(this.currentBlock.blockItem)) {
                                                this.blockX = 0;
                                                this.blockY = 0;
                                                this.textY = 0;
                                                i = 0 - AndroidUtilities.dp(8.0f);
                                                n = size;
                                                break Label_0891;
                                            }
                                            if (!(this.blockLayout.itemView instanceof BlockTableCell)) {
                                                n = i;
                                                break Label_0889;
                                            }
                                            this.blockX -= AndroidUtilities.dp(18.0f);
                                            n = AndroidUtilities.dp(36.0f);
                                        }
                                        else {
                                            if (!ArticleViewer.this.isRtl) {
                                                this.blockX -= AndroidUtilities.dp(18.0f);
                                            }
                                            n = AndroidUtilities.dp(18.0f);
                                        }
                                        n += i;
                                    }
                                    i = 0;
                                }
                            }
                            this.blockLayout.itemView.measure(View$MeasureSpec.makeMeasureSpec(n, 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
                            if (this.blockLayout.itemView instanceof BlockParagraphCell && this.currentBlock.numLayout != null && this.currentBlock.numLayout.getLineCount() > 0) {
                                final BlockParagraphCell blockParagraphCell = (BlockParagraphCell)this.blockLayout.itemView;
                                if (blockParagraphCell.textLayout != null && blockParagraphCell.textLayout.getLineCount() > 0) {
                                    n = blockParagraphCell.textLayout.getLineAscent(0);
                                    this.numOffsetY = this.currentBlock.numLayout.getLineAscent(0) - n;
                                }
                            }
                            if (this.currentBlock.blockItem instanceof TLRPC.TL_pageBlockDetails) {
                                this.verticalAlign = true;
                                this.blockY = 0;
                                n = i - AndroidUtilities.dp(8.0f);
                            }
                            else {
                                final View itemView2 = this.blockLayout.itemView;
                                if (itemView2 instanceof BlockOrderedListItemCell) {
                                    this.verticalAlign = ((BlockOrderedListItemCell)itemView2).verticalAlign;
                                    n = i;
                                }
                                else {
                                    n = i;
                                    if (itemView2 instanceof BlockListItemCell) {
                                        this.verticalAlign = ((BlockListItemCell)itemView2).verticalAlign;
                                        n = i;
                                    }
                                }
                            }
                            if (this.verticalAlign && this.currentBlock.numLayout != null) {
                                this.textY = (this.blockLayout.itemView.getMeasuredHeight() - this.currentBlock.numLayout.getHeight()) / 2;
                            }
                            n += this.blockLayout.itemView.getMeasuredHeight();
                        }
                        i = AndroidUtilities.dp(8.0f);
                    }
                    n += i;
                }
                i = n;
                if (this.currentBlock.parent.items.get(this.currentBlock.parent.items.size() - 1) == this.currentBlock) {
                    i = n + AndroidUtilities.dp(8.0f);
                }
                n = i;
                if (this.currentBlock.index == 0) {
                    n = i;
                    if (this.currentBlock.parent.level == 0) {
                        n = i + AndroidUtilities.dp(10.0f);
                    }
                }
            }
            this.setMeasuredDimension(size, n);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.textLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TL_pageBlockOrderedListItem currentBlock) {
            if (this.currentBlock != currentBlock) {
                this.currentBlock = currentBlock;
                final RecyclerView.ViewHolder blockLayout = this.blockLayout;
                if (blockLayout != null) {
                    this.removeView(blockLayout.itemView);
                    this.blockLayout = null;
                }
                if (this.currentBlock.blockItem != null) {
                    this.currentBlockType = this.parentAdapter.getTypeForBlock(this.currentBlock.blockItem);
                    this.blockLayout = this.parentAdapter.onCreateViewHolder(this, this.currentBlockType);
                    this.addView(this.blockLayout.itemView);
                }
            }
            if (this.currentBlock.blockItem != null) {
                this.parentAdapter.bindBlockToHolder(this.currentBlockType, this.blockLayout, this.currentBlock.blockItem, 0, 0);
            }
            this.requestLayout();
        }
    }
    
    private class BlockParagraphCell extends View
    {
        private TLRPC.TL_pageBlockParagraph currentBlock;
        private WebpageAdapter parentAdapter;
        private DrawingText textLayout;
        private int textX;
        private int textY;
        
        public BlockParagraphCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.parentAdapter = parentAdapter;
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
            if (this.currentBlock.level > 0) {
                final float n = (float)AndroidUtilities.dp(18.0f);
                final float n2 = (float)AndroidUtilities.dp(20.0f);
                final int measuredHeight = this.getMeasuredHeight();
                int dp;
                if (this.currentBlock.bottom) {
                    dp = AndroidUtilities.dp(6.0f);
                }
                else {
                    dp = 0;
                }
                canvas.drawRect(n, 0.0f, n2, (float)(measuredHeight - dp), ArticleViewer.quoteLinePaint);
            }
        }
        
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setEnabled(true);
            final DrawingText textLayout = this.textLayout;
            if (textLayout == null) {
                return;
            }
            accessibilityNodeInfo.setText(textLayout.getText());
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int n, int size) {
            size = View$MeasureSpec.getSize(n);
            final TLRPC.TL_pageBlockParagraph currentBlock = this.currentBlock;
            n = 0;
            if (currentBlock != null) {
                final int level = currentBlock.level;
                if (level == 0) {
                    this.textY = AndroidUtilities.dp(8.0f);
                    this.textX = AndroidUtilities.dp(18.0f);
                }
                else {
                    this.textY = 0;
                    this.textX = AndroidUtilities.dp((float)(level * 14 + 18));
                }
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.RichText text = this.currentBlock.text;
                final int dp = AndroidUtilities.dp(18.0f);
                final int textX = this.textX;
                final int textY = this.textY;
                final TLRPC.TL_pageBlockParagraph currentBlock2 = this.currentBlock;
                Layout$Alignment layout$Alignment;
                if (ArticleViewer.this.isRtl) {
                    layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                }
                else {
                    layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                }
                this.textLayout = this$0.createLayoutForText(this, null, text, size - dp - textX, textY, currentBlock2, layout$Alignment, 0, this.parentAdapter);
                final DrawingText textLayout = this.textLayout;
                if (textLayout != null) {
                    final int height = textLayout.getHeight();
                    if (this.currentBlock.level > 0) {
                        n = AndroidUtilities.dp(8.0f);
                    }
                    else {
                        n = AndroidUtilities.dp(16.0f);
                    }
                    n += height;
                }
            }
            else {
                n = 1;
            }
            this.setMeasuredDimension(size, n);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockParagraph currentBlock) {
            this.currentBlock = currentBlock;
            this.requestLayout();
        }
    }
    
    private class BlockPhotoCell extends FrameLayout implements FileDownloadProgressListener
    {
        private int TAG;
        boolean autoDownload;
        private int buttonPressed;
        private int buttonState;
        private int buttonX;
        private int buttonY;
        private boolean cancelLoading;
        private DrawingText captionLayout;
        private BlockChannelCell channelCell;
        private DrawingText creditLayout;
        private int creditOffset;
        private TLRPC.TL_pageBlockPhoto currentBlock;
        private String currentFilter;
        private TLRPC.Photo currentPhoto;
        private TLRPC.PhotoSize currentPhotoObject;
        private TLRPC.PhotoSize currentPhotoObjectThumb;
        private String currentThumbFilter;
        private int currentType;
        private MessageObject.GroupedMessagePosition groupPosition;
        private ImageReceiver imageView;
        private boolean isFirst;
        private boolean isLast;
        private Drawable linkDrawable;
        private WebpageAdapter parentAdapter;
        private TLRPC.PageBlock parentBlock;
        private boolean photoPressed;
        private RadialProgress2 radialProgress;
        private int textX;
        private int textY;
        
        public BlockPhotoCell(final Context context, final WebpageAdapter parentAdapter, final int currentType) {
            super(context);
            this.parentAdapter = parentAdapter;
            this.setWillNotDraw(false);
            this.imageView = new ImageReceiver((View)this);
            this.channelCell = new BlockChannelCell(context, this.parentAdapter, 1);
            (this.radialProgress = new RadialProgress2((View)this)).setProgressColor(-1);
            this.radialProgress.setColors(1711276032, 2130706432, -1, -2500135);
            this.TAG = DownloadController.getInstance(ArticleViewer.this.currentAccount).generateObserverTag();
            this.addView((View)this.channelCell, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
            this.currentType = currentType;
        }
        
        private void didPressedButton(final boolean b) {
            final int buttonState = this.buttonState;
            if (buttonState == 0) {
                this.cancelLoading = false;
                this.radialProgress.setProgress(0.0f, b);
                this.imageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.currentPhoto), this.currentFilter, ImageLocation.getForPhoto(this.currentPhotoObjectThumb, this.currentPhoto), this.currentThumbFilter, this.currentPhotoObject.size, null, ArticleViewer.this.currentPage, 1);
                this.buttonState = 1;
                this.radialProgress.setIcon(this.getIconForCurrentState(), true, b);
                this.invalidate();
            }
            else if (buttonState == 1) {
                this.cancelLoading = true;
                this.imageView.cancelLoadImage();
                this.buttonState = 0;
                this.radialProgress.setIcon(this.getIconForCurrentState(), false, b);
                this.invalidate();
            }
        }
        
        private int getIconForCurrentState() {
            final int buttonState = this.buttonState;
            if (buttonState == 0) {
                return 2;
            }
            if (buttonState == 1) {
                return 3;
            }
            return 4;
        }
        
        public View getChannelCell() {
            return (View)this.channelCell;
        }
        
        public int getObserverTag() {
            return this.TAG;
        }
        
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.imageView.onAttachedToWindow();
            this.updateButtonState(false);
        }
        
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.imageView.onDetachedFromWindow();
            DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
        }
        
        protected void onDraw(final Canvas canvas) {
            final int currentType = this.currentType;
            if (this.currentBlock == null) {
                return;
            }
            if (!this.imageView.hasBitmapImage() || this.imageView.getCurrentAlpha() != 1.0f) {
                canvas.drawRect((float)this.imageView.getImageX(), (float)this.imageView.getImageY(), (float)this.imageView.getImageX2(), (float)this.imageView.getImageY2(), ArticleViewer.photoBackgroundPaint);
            }
            this.imageView.draw(canvas);
            if (this.imageView.getVisible()) {
                this.radialProgress.draw(canvas);
            }
            if (!TextUtils.isEmpty((CharSequence)this.currentBlock.url)) {
                final int n = this.getMeasuredWidth() - AndroidUtilities.dp(35.0f);
                final int n2 = this.imageView.getImageY() + AndroidUtilities.dp(11.0f);
                this.linkDrawable.setBounds(n, n2, AndroidUtilities.dp(24.0f) + n, AndroidUtilities.dp(24.0f) + n2);
                this.linkDrawable.draw(canvas);
            }
            this.textY = this.imageView.getImageY() + this.imageView.getImageHeight() + AndroidUtilities.dp(8.0f);
            if (this.captionLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.captionLayout.draw(canvas);
                canvas.restore();
            }
            if (this.creditLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)(this.textY + this.creditOffset));
                this.creditLayout.draw(canvas);
                canvas.restore();
            }
            if (this.currentBlock.level > 0) {
                final float n3 = (float)AndroidUtilities.dp(18.0f);
                final float n4 = (float)AndroidUtilities.dp(20.0f);
                final int measuredHeight = this.getMeasuredHeight();
                int dp;
                if (this.currentBlock.bottom) {
                    dp = AndroidUtilities.dp(6.0f);
                }
                else {
                    dp = 0;
                }
                canvas.drawRect(n3, 0.0f, n4, (float)(measuredHeight - dp), ArticleViewer.quoteLinePaint);
            }
        }
        
        public void onFailedDownload(final String s, final boolean b) {
            this.updateButtonState(false);
        }
        
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setEnabled(true);
            final StringBuilder sb = new StringBuilder(LocaleController.getString("AttachPhoto", 2131558727));
            if (this.captionLayout != null) {
                sb.append(", ");
                sb.append(this.captionLayout.getText());
            }
            accessibilityNodeInfo.setText((CharSequence)sb.toString());
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(final int n, final int n2) {
            int n3 = View$MeasureSpec.getSize(n);
            final int currentType = this.currentType;
            final boolean b = false;
            final int n4 = 1;
            int measuredHeight = 0;
            int n5 = 0;
            Label_0109: {
                if (currentType == 1) {
                    n3 = ((View)this.getParent()).getMeasuredWidth();
                    measuredHeight = ((View)this.getParent()).getMeasuredHeight();
                }
                else {
                    if (currentType != 2) {
                        measuredHeight = 0;
                        n5 = n3;
                        break Label_0109;
                    }
                    final float ph = this.groupPosition.ph;
                    final Point displaySize = AndroidUtilities.displaySize;
                    measuredHeight = (int)Math.ceil(ph * Math.max(displaySize.x, displaySize.y) * 0.5f);
                }
                n5 = n3;
            }
            final TLRPC.TL_pageBlockPhoto currentBlock = this.currentBlock;
            int n6 = n4;
            if (currentBlock != null) {
                this.currentPhoto = ArticleViewer.this.getPhotoWithId(currentBlock.photo_id);
                final int dp = AndroidUtilities.dp(48.0f);
                int textX = 0;
                int n7 = 0;
                int b2 = 0;
                Label_0238: {
                    if (this.currentType == 0) {
                        final int level = this.currentBlock.level;
                        if (level > 0) {
                            textX = AndroidUtilities.dp((float)(level * 14)) + AndroidUtilities.dp(18.0f);
                            this.textX = textX;
                            b2 = (n7 = n5 - (AndroidUtilities.dp(18.0f) + textX));
                            break Label_0238;
                        }
                    }
                    this.textX = AndroidUtilities.dp(18.0f);
                    n7 = n5 - AndroidUtilities.dp(36.0f);
                    b2 = n5;
                    textX = 0;
                }
                final TLRPC.Photo currentPhoto = this.currentPhoto;
                int n17;
                if (currentPhoto != null && this.currentPhotoObject != null) {
                    this.currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(currentPhoto.sizes, 40, true);
                    if (this.currentPhotoObject == this.currentPhotoObjectThumb) {
                        this.currentPhotoObjectThumb = null;
                    }
                    final int currentType2 = this.currentType;
                    int n9 = 0;
                    int i = 0;
                    int j = 0;
                    Label_0610: {
                        int min;
                        if (currentType2 == 0) {
                            final float n8 = (float)b2;
                            final TLRPC.PhotoSize currentPhotoObject = this.currentPhotoObject;
                            final int a = (int)(n8 / currentPhotoObject.w * currentPhotoObject.h);
                            if (this.parentBlock instanceof TLRPC.TL_pageBlockCover) {
                                min = Math.min(a, b2);
                                n9 = textX;
                                i = b2;
                            }
                            else {
                                final int n10 = (int)((Math.max(ArticleViewer.this.listView[0].getMeasuredWidth(), ArticleViewer.this.listView[0].getMeasuredHeight()) - AndroidUtilities.dp(56.0f)) * 0.9f);
                                min = a;
                                n9 = textX;
                                i = b2;
                                if (a > n10) {
                                    final float n11 = (float)n10;
                                    final TLRPC.PhotoSize currentPhotoObject2 = this.currentPhotoObject;
                                    i = (int)(n11 / currentPhotoObject2.h * currentPhotoObject2.w);
                                    n9 = textX + (n5 - textX - i) / 2;
                                    min = n10;
                                }
                            }
                        }
                        else {
                            min = measuredHeight;
                            n9 = textX;
                            i = b2;
                            if (currentType2 == 2) {
                                int n12 = b2;
                                if ((this.groupPosition.flags & 0x2) == 0x0) {
                                    n12 = b2 - AndroidUtilities.dp(2.0f);
                                }
                                int n13;
                                if ((this.groupPosition.flags & 0x8) == 0x0) {
                                    n13 = measuredHeight - AndroidUtilities.dp(2.0f);
                                }
                                else {
                                    n13 = measuredHeight;
                                }
                                final int leftSpanOffset = this.groupPosition.leftSpanOffset;
                                int n14 = textX;
                                int n15 = n12;
                                if (leftSpanOffset != 0) {
                                    final int n16 = (int)Math.ceil(leftSpanOffset * n5 / 1000.0f);
                                    n15 = n12 - n16;
                                    n14 = textX + n16;
                                }
                                j = n13;
                                n9 = n14;
                                i = n15;
                                break Label_0610;
                            }
                        }
                        measuredHeight = min;
                        j = min;
                    }
                    final ImageReceiver imageView = this.imageView;
                    int dp2 = 0;
                    Label_0663: {
                        if (!this.isFirst) {
                            final int currentType3 = this.currentType;
                            if (currentType3 != 1 && currentType3 != 2) {
                                if (this.currentBlock.level <= 0) {
                                    dp2 = AndroidUtilities.dp(8.0f);
                                    break Label_0663;
                                }
                            }
                        }
                        dp2 = 0;
                    }
                    imageView.setImageCoords(n9, dp2, i, j);
                    if (this.currentType == 0) {
                        this.currentFilter = null;
                    }
                    else {
                        this.currentFilter = String.format(Locale.US, "%d_%d", i, j);
                    }
                    this.currentThumbFilter = "80_80_b";
                    this.autoDownload = ((DownloadController.getInstance(ArticleViewer.this.currentAccount).getCurrentDownloadMask() & 0x1) != 0x0);
                    final File pathToAttach = FileLoader.getPathToAttach(this.currentPhotoObject, true);
                    if (!this.autoDownload && !pathToAttach.exists()) {
                        this.imageView.setStrippedLocation(ImageLocation.getForPhoto(this.currentPhotoObject, this.currentPhoto));
                        this.imageView.setImage(null, this.currentFilter, ImageLocation.getForPhoto(this.currentPhotoObjectThumb, this.currentPhoto), this.currentThumbFilter, this.currentPhotoObject.size, null, ArticleViewer.this.currentPage, 1);
                    }
                    else {
                        this.imageView.setStrippedLocation(null);
                        this.imageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.currentPhoto), this.currentFilter, ImageLocation.getForPhoto(this.currentPhotoObjectThumb, this.currentPhoto), this.currentThumbFilter, this.currentPhotoObject.size, null, ArticleViewer.this.currentPage, 1);
                    }
                    this.buttonX = (int)(this.imageView.getImageX() + (this.imageView.getImageWidth() - dp) / 2.0f);
                    this.buttonY = (int)(this.imageView.getImageY() + (this.imageView.getImageHeight() - dp) / 2.0f);
                    final RadialProgress2 radialProgress = this.radialProgress;
                    final int buttonX = this.buttonX;
                    final int buttonY = this.buttonY;
                    radialProgress.setProgressRect(buttonX, buttonY, buttonX + dp, dp + buttonY);
                    n17 = measuredHeight;
                }
                else {
                    n17 = measuredHeight;
                }
                int n18 = n17;
                if (this.currentType == 0) {
                    final ArticleViewer this$0 = ArticleViewer.this;
                    final TLRPC.TL_pageBlockPhoto currentBlock2 = this.currentBlock;
                    this.captionLayout = this$0.createLayoutForText((View)this, null, currentBlock2.caption.text, n7, currentBlock2, this.parentAdapter);
                    int n19 = n17;
                    if (this.captionLayout != null) {
                        this.creditOffset = AndroidUtilities.dp(4.0f) + this.captionLayout.getHeight();
                        n19 = n17 + (this.creditOffset + AndroidUtilities.dp(4.0f));
                    }
                    final ArticleViewer this$2 = ArticleViewer.this;
                    final TLRPC.TL_pageBlockPhoto currentBlock3 = this.currentBlock;
                    final TLRPC.RichText credit = currentBlock3.caption.credit;
                    Layout$Alignment layout$Alignment;
                    if (this$2.isRtl) {
                        layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                    }
                    else {
                        layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                    }
                    this.creditLayout = this$2.createLayoutForText((View)this, null, credit, n7, currentBlock3, layout$Alignment, this.parentAdapter);
                    n18 = n19;
                    if (this.creditLayout != null) {
                        n18 = n19 + (AndroidUtilities.dp(4.0f) + this.creditLayout.getHeight());
                    }
                }
                int n20 = n18;
                if (!this.isFirst) {
                    n20 = n18;
                    if (this.currentType == 0) {
                        n20 = n18;
                        if (this.currentBlock.level <= 0) {
                            n20 = n18 + AndroidUtilities.dp(8.0f);
                        }
                    }
                }
                int n21 = b ? 1 : 0;
                if (this.parentBlock instanceof TLRPC.TL_pageBlockCover) {
                    n21 = (b ? 1 : 0);
                    if (this.parentAdapter.blocks != null) {
                        n21 = (b ? 1 : 0);
                        if (this.parentAdapter.blocks.size() > 1) {
                            n21 = (b ? 1 : 0);
                            if (this.parentAdapter.blocks.get(1) instanceof TLRPC.TL_pageBlockChannel) {
                                n21 = 1;
                            }
                        }
                    }
                }
                int n22 = n20;
                if (this.currentType != 2) {
                    n22 = n20;
                    if (n21 == 0) {
                        n22 = n20 + AndroidUtilities.dp(8.0f);
                    }
                }
                n6 = n22;
            }
            this.channelCell.measure(n, n2);
            this.channelCell.setTranslationY((float)(this.imageView.getImageHeight() - AndroidUtilities.dp(39.0f)));
            this.setMeasuredDimension(n5, n6);
        }
        
        public void onProgressDownload(final String s, final float n) {
            this.radialProgress.setProgress(n, true);
            if (this.buttonState != 1) {
                this.updateButtonState(true);
            }
        }
        
        public void onProgressUpload(final String s, final float n, final boolean b) {
        }
        
        public void onSuccessDownload(final String s) {
            this.radialProgress.setProgress(1.0f, true);
            this.updateButtonState(true);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final float x = motionEvent.getX();
            final float y = motionEvent.getY();
            final int visibility = this.channelCell.getVisibility();
            boolean b = false;
            if (visibility == 0 && y > this.channelCell.getTranslationY() && y < this.channelCell.getTranslationY() + AndroidUtilities.dp(39.0f)) {
                if (ArticleViewer.this.channelBlock != null && motionEvent.getAction() == 1) {
                    MessagesController.getInstance(ArticleViewer.this.currentAccount).openByUserName(ArticleViewer.this.channelBlock.channel.username, ArticleViewer.this.parentFragment, 2);
                    ArticleViewer.this.close(false, true);
                }
                return true;
            }
            Label_0317: {
                if (motionEvent.getAction() == 0 && this.imageView.isInsideImage(x, y)) {
                    Label_0214: {
                        if (this.buttonState != -1) {
                            final int buttonX = this.buttonX;
                            if (x >= buttonX && x <= buttonX + AndroidUtilities.dp(48.0f)) {
                                final int buttonY = this.buttonY;
                                if (y >= buttonY && y <= buttonY + AndroidUtilities.dp(48.0f)) {
                                    break Label_0214;
                                }
                            }
                        }
                        if (this.buttonState != 0) {
                            this.photoPressed = true;
                            break Label_0317;
                        }
                    }
                    this.buttonPressed = 1;
                    this.invalidate();
                }
                else if (motionEvent.getAction() == 1) {
                    if (this.photoPressed) {
                        this.photoPressed = false;
                        ArticleViewer.this.openPhoto(this.currentBlock);
                    }
                    else if (this.buttonPressed == 1) {
                        this.playSoundEffect(this.buttonPressed = 0);
                        this.didPressedButton(true);
                        this.invalidate();
                    }
                }
                else if (motionEvent.getAction() == 3) {
                    this.photoPressed = false;
                    this.buttonPressed = 0;
                }
            }
            if (this.photoPressed || this.buttonPressed != 0 || ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.captionLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.creditLayout, this.textX, this.textY + this.creditOffset) || super.onTouchEvent(motionEvent)) {
                b = true;
            }
            return b;
        }
        
        public void setBlock(TLRPC.TL_pageBlockPhoto currentBlock, final boolean isFirst, final boolean isLast) {
            this.parentBlock = null;
            this.currentBlock = currentBlock;
            this.isFirst = isFirst;
            this.isLast = isLast;
            this.channelCell.setVisibility(4);
            if (!TextUtils.isEmpty((CharSequence)this.currentBlock.url)) {
                this.linkDrawable = this.getResources().getDrawable(2131165496);
            }
            currentBlock = this.currentBlock;
            if (currentBlock != null) {
                final TLRPC.Photo access$12800 = ArticleViewer.this.getPhotoWithId(currentBlock.photo_id);
                if (access$12800 != null) {
                    this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(access$12800.sizes, AndroidUtilities.getPhotoSize());
                }
                else {
                    this.currentPhotoObject = null;
                }
            }
            else {
                this.currentPhotoObject = null;
            }
            this.updateButtonState(false);
            this.requestLayout();
        }
        
        public void setParentBlock(final TLRPC.PageBlock parentBlock) {
            this.parentBlock = parentBlock;
            if (ArticleViewer.this.channelBlock != null && this.parentBlock instanceof TLRPC.TL_pageBlockCover) {
                this.channelCell.setBlock(ArticleViewer.this.channelBlock);
                this.channelCell.setVisibility(0);
            }
        }
        
        public void updateButtonState(final boolean b) {
            final String attachFileName = FileLoader.getAttachFileName(this.currentPhotoObject);
            final boolean exists = FileLoader.getPathToAttach(this.currentPhotoObject, true).exists();
            if (TextUtils.isEmpty((CharSequence)attachFileName)) {
                this.radialProgress.setIcon(4, false, false);
                return;
            }
            if (exists) {
                DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
                this.buttonState = -1;
                this.radialProgress.setIcon(this.getIconForCurrentState(), false, b);
                this.invalidate();
            }
            else {
                DownloadController.getInstance(ArticleViewer.this.currentAccount).addLoadingFileObserver(attachFileName, null, (DownloadController.FileDownloadProgressListener)this);
                final boolean autoDownload = this.autoDownload;
                float floatValue = 0.0f;
                if (!autoDownload && !FileLoader.getInstance(ArticleViewer.this.currentAccount).isLoadingFile(attachFileName)) {
                    this.buttonState = 0;
                }
                else {
                    this.buttonState = 1;
                    final Float fileProgress = ImageLoader.getInstance().getFileProgress(attachFileName);
                    if (fileProgress != null) {
                        floatValue = fileProgress;
                    }
                }
                this.radialProgress.setIcon(this.getIconForCurrentState(), true, b);
                this.radialProgress.setProgress(floatValue, false);
                this.invalidate();
            }
        }
    }
    
    private class BlockPreformattedCell extends FrameLayout
    {
        private TLRPC.TL_pageBlockPreformatted currentBlock;
        private WebpageAdapter parentAdapter;
        private HorizontalScrollView scrollView;
        private View textContainer;
        private DrawingText textLayout;
        
        public BlockPreformattedCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.parentAdapter = parentAdapter;
            (this.scrollView = new HorizontalScrollView(context) {
                public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                    if (BlockPreformattedCell.this.textContainer.getMeasuredWidth() > this.getMeasuredWidth()) {
                        ArticleViewer.this.windowView.requestDisallowInterceptTouchEvent(true);
                    }
                    return super.onInterceptTouchEvent(motionEvent);
                }
                
                protected void onScrollChanged(final int n, final int n2, final int n3, final int n4) {
                    super.onScrollChanged(n, n2, n3, n4);
                    if (ArticleViewer.this.pressedLinkOwnerLayout != null) {
                        ArticleViewer.this.pressedLinkOwnerLayout = null;
                        ArticleViewer.this.pressedLinkOwnerView = null;
                    }
                }
            }).setPadding(0, AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f));
            this.addView((View)this.scrollView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
            this.textContainer = new View(context) {
                protected void onDraw(final Canvas canvas) {
                    if (BlockPreformattedCell.this.textLayout != null) {
                        canvas.save();
                        BlockPreformattedCell.this.textLayout.draw(canvas);
                        canvas.restore();
                    }
                }
                
                protected void onMeasure(int max, int n) {
                    final TLRPC.TL_pageBlockPreformatted access$20100 = BlockPreformattedCell.this.currentBlock;
                    int n2 = 0;
                    int n3 = 1;
                    max = 1;
                    if (access$20100 != null) {
                        final BlockPreformattedCell this$1 = BlockPreformattedCell.this;
                        this$1.textLayout = ArticleViewer.this.createLayoutForText(this, null, this$1.currentBlock.text, AndroidUtilities.dp(5000.0f), BlockPreformattedCell.this.currentBlock, BlockPreformattedCell.this.parentAdapter);
                        if (BlockPreformattedCell.this.textLayout != null) {
                            final int n4 = BlockPreformattedCell.this.textLayout.getHeight() + 0;
                            final int lineCount = BlockPreformattedCell.this.textLayout.getLineCount();
                            while (true) {
                                n3 = max;
                                n = n4;
                                if (n2 >= lineCount) {
                                    break;
                                }
                                max = Math.max((int)Math.ceil(BlockPreformattedCell.this.textLayout.getLineWidth(n2)), max);
                                ++n2;
                            }
                        }
                        else {
                            n = 0;
                        }
                    }
                    else {
                        n = 1;
                    }
                    this.setMeasuredDimension(n3 + AndroidUtilities.dp(32.0f), n);
                }
            };
            final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(-2, -1);
            final int dp = AndroidUtilities.dp(16.0f);
            frameLayout$LayoutParams.rightMargin = dp;
            frameLayout$LayoutParams.leftMargin = dp;
            final int dp2 = AndroidUtilities.dp(12.0f);
            frameLayout$LayoutParams.bottomMargin = dp2;
            frameLayout$LayoutParams.topMargin = dp2;
            this.scrollView.addView(this.textContainer, (ViewGroup$LayoutParams)frameLayout$LayoutParams);
            this.setWillNotDraw(false);
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            canvas.drawRect(0.0f, (float)AndroidUtilities.dp(8.0f), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - AndroidUtilities.dp(8.0f)), ArticleViewer.preformattedBackgroundPaint);
        }
        
        protected void onMeasure(int size, final int n) {
            size = View$MeasureSpec.getSize(size);
            this.scrollView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
            this.setMeasuredDimension(size, this.scrollView.getMeasuredHeight());
        }
        
        public void setBlock(final TLRPC.TL_pageBlockPreformatted currentBlock) {
            this.currentBlock = currentBlock;
            this.scrollView.setScrollX(0);
            this.textContainer.requestLayout();
        }
    }
    
    private class BlockPullquoteCell extends View
    {
        private TLRPC.TL_pageBlockPullquote currentBlock;
        private WebpageAdapter parentAdapter;
        private DrawingText textLayout;
        private DrawingText textLayout2;
        private int textX;
        private int textY;
        private int textY2;
        
        public BlockPullquoteCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textX = AndroidUtilities.dp(18.0f);
            this.textY = AndroidUtilities.dp(8.0f);
            this.parentAdapter = parentAdapter;
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
            if (this.textLayout2 != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY2);
                this.textLayout2.draw(canvas);
                canvas.restore();
            }
        }
        
        protected void onMeasure(int n, int n2) {
            final int size = View$MeasureSpec.getSize(n);
            final TLRPC.TL_pageBlockPullquote currentBlock = this.currentBlock;
            if (currentBlock != null) {
                this.textLayout = ArticleViewer.this.createLayoutForText(this, null, currentBlock.text, size - AndroidUtilities.dp(36.0f), this.currentBlock, this.parentAdapter);
                final DrawingText textLayout = this.textLayout;
                n2 = 0;
                if (textLayout != null) {
                    n2 = 0 + (AndroidUtilities.dp(8.0f) + this.textLayout.getHeight());
                }
                this.textLayout2 = ArticleViewer.this.createLayoutForText(this, null, this.currentBlock.caption, size - AndroidUtilities.dp(36.0f), this.currentBlock, this.parentAdapter);
                n = n2;
                if (this.textLayout2 != null) {
                    this.textY2 = AndroidUtilities.dp(2.0f) + n2;
                    n = n2 + (AndroidUtilities.dp(8.0f) + this.textLayout2.getHeight());
                }
                if ((n2 = n) != 0) {
                    n2 = n + AndroidUtilities.dp(8.0f);
                }
            }
            else {
                n2 = 1;
            }
            this.setMeasuredDimension(size, n2);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout2, this.textX, this.textY2) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockPullquote currentBlock) {
            this.currentBlock = currentBlock;
            this.requestLayout();
        }
    }
    
    private class BlockRelatedArticlesCell extends View
    {
        private int additionalHeight;
        private TL_pageBlockRelatedArticlesChild currentBlock;
        private boolean divider;
        private boolean drawImage;
        private ImageReceiver imageView;
        private WebpageAdapter parentAdapter;
        private DrawingText textLayout;
        private DrawingText textLayout2;
        private int textOffset;
        private int textX;
        private int textY;
        
        public BlockRelatedArticlesCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textX = AndroidUtilities.dp(18.0f);
            this.textY = AndroidUtilities.dp(10.0f);
            this.parentAdapter = parentAdapter;
            (this.imageView = new ImageReceiver(this)).setRoundRadius(AndroidUtilities.dp(6.0f));
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.drawImage) {
                this.imageView.draw(canvas);
            }
            canvas.save();
            canvas.translate((float)this.textX, (float)AndroidUtilities.dp(10.0f));
            final DrawingText textLayout = this.textLayout;
            if (textLayout != null) {
                textLayout.draw(canvas);
            }
            if (this.textLayout2 != null) {
                canvas.translate(0.0f, (float)this.textOffset);
                this.textLayout2.draw(canvas);
            }
            canvas.restore();
            if (this.divider) {
                float n;
                if (ArticleViewer.this.isRtl) {
                    n = 0.0f;
                }
                else {
                    n = (float)AndroidUtilities.dp(17.0f);
                }
                final float n2 = (float)(this.getMeasuredHeight() - 1);
                final int measuredWidth = this.getMeasuredWidth();
                int dp;
                if (ArticleViewer.this.isRtl) {
                    dp = AndroidUtilities.dp(17.0f);
                }
                else {
                    dp = 0;
                }
                canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), ArticleViewer.dividerPaint);
            }
        }
        
        @SuppressLint({ "DrawAllocation", "NewApi" })
        protected void onMeasure(int i, int dp) {
            final int size = View$MeasureSpec.getSize(i);
            this.divider = (this.currentBlock.num != this.currentBlock.parent.articles.size() - 1);
            final TLRPC.TL_pageRelatedArticle tl_pageRelatedArticle = this.currentBlock.parent.articles.get(this.currentBlock.num);
            this.additionalHeight = 0;
            if (ArticleViewer.this.selectedFontSize == 0) {
                this.additionalHeight = -AndroidUtilities.dp(4.0f);
            }
            else if (ArticleViewer.this.selectedFontSize == 1) {
                this.additionalHeight = -AndroidUtilities.dp(2.0f);
            }
            else if (ArticleViewer.this.selectedFontSize == 3) {
                this.additionalHeight = AndroidUtilities.dp(2.0f);
            }
            else if (ArticleViewer.this.selectedFontSize == 4) {
                this.additionalHeight = AndroidUtilities.dp(4.0f);
            }
            final long photo_id = tl_pageRelatedArticle.photo_id;
            TLRPC.Photo access$12800;
            if (photo_id != 0L) {
                access$12800 = ArticleViewer.this.getPhotoWithId(photo_id);
            }
            else {
                access$12800 = null;
            }
            if (access$12800 != null) {
                this.drawImage = true;
                final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(access$12800.sizes, AndroidUtilities.getPhotoSize());
                TLRPC.PhotoSize closestPhotoSizeWithSize2;
                if (closestPhotoSizeWithSize == (closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(access$12800.sizes, 80, true))) {
                    closestPhotoSizeWithSize2 = null;
                }
                this.imageView.setImage(ImageLocation.getForPhoto(closestPhotoSizeWithSize, access$12800), "64_64", ImageLocation.getForPhoto(closestPhotoSizeWithSize2, access$12800), "64_64_b", closestPhotoSizeWithSize.size, null, ArticleViewer.this.currentPage, 1);
            }
            else {
                this.drawImage = false;
            }
            final int dp2 = AndroidUtilities.dp(60.0f);
            i = (dp = size - AndroidUtilities.dp(36.0f));
            if (this.drawImage) {
                dp = AndroidUtilities.dp(44.0f);
                this.imageView.setImageCoords(size - dp - AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), dp, dp);
                dp = i - (this.imageView.getImageWidth() + AndroidUtilities.dp(6.0f));
            }
            i = AndroidUtilities.dp(18.0f);
            final String title = tl_pageRelatedArticle.title;
            if (title != null) {
                this.textLayout = ArticleViewer.this.createLayoutForText(this, title, null, dp, this.textY, this.currentBlock, Layout$Alignment.ALIGN_NORMAL, 3, this.parentAdapter);
            }
            final DrawingText textLayout = this.textLayout;
            int n2 = 0;
            int n3 = 0;
            Label_0524: {
                if (textLayout != null) {
                    final int lineCount = textLayout.getLineCount();
                    this.textOffset = this.textLayout.getHeight() + AndroidUtilities.dp(6.0f) + this.additionalHeight;
                    final int n = i + this.textLayout.getHeight();
                    while (true) {
                        for (i = 0; i < lineCount; ++i) {
                            if (this.textLayout.getLineLeft(i) != 0.0f) {
                                i = 1;
                                n2 = 4 - lineCount;
                                n3 = i;
                                i = n;
                                break Label_0524;
                            }
                        }
                        i = 0;
                        continue;
                    }
                }
                this.textOffset = 0;
                n3 = 0;
                n2 = 4;
            }
            String s;
            if (tl_pageRelatedArticle.published_date != 0 && !TextUtils.isEmpty((CharSequence)tl_pageRelatedArticle.author)) {
                s = LocaleController.formatString("ArticleDateByAuthor", 2131558705, LocaleController.getInstance().chatFullDate.format(tl_pageRelatedArticle.published_date * 1000L), tl_pageRelatedArticle.author);
            }
            else if (!TextUtils.isEmpty((CharSequence)tl_pageRelatedArticle.author)) {
                s = LocaleController.formatString("ArticleByAuthor", 2131558704, tl_pageRelatedArticle.author);
            }
            else if (tl_pageRelatedArticle.published_date != 0) {
                s = LocaleController.getInstance().chatFullDate.format(tl_pageRelatedArticle.published_date * 1000L);
            }
            else if (!TextUtils.isEmpty((CharSequence)tl_pageRelatedArticle.description)) {
                s = tl_pageRelatedArticle.description;
            }
            else {
                s = tl_pageRelatedArticle.url;
            }
            final ArticleViewer this$0 = ArticleViewer.this;
            final int textY = this.textY;
            final int textOffset = this.textOffset;
            final TL_pageBlockRelatedArticlesChild currentBlock = this.currentBlock;
            Layout$Alignment layout$Alignment;
            if (!this$0.isRtl && n3 == 0) {
                layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
            }
            else {
                layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
            }
            this.textLayout2 = this$0.createLayoutForText(this, s, null, dp, textOffset + textY, currentBlock, layout$Alignment, n2, this.parentAdapter);
            final DrawingText textLayout2 = this.textLayout2;
            dp = i;
            if (textLayout2 != null) {
                i = (dp = i + textLayout2.getHeight());
                if (this.textLayout != null) {
                    dp = i + (AndroidUtilities.dp(6.0f) + this.additionalHeight);
                }
            }
            this.setMeasuredDimension(size, Math.max(dp2, dp) + (this.divider ? 1 : 0));
        }
        
        public void setBlock(final TL_pageBlockRelatedArticlesChild currentBlock) {
            this.currentBlock = currentBlock;
            this.requestLayout();
        }
    }
    
    private class BlockRelatedArticlesHeaderCell extends View
    {
        private TLRPC.TL_pageBlockRelatedArticles currentBlock;
        private WebpageAdapter parentAdapter;
        private DrawingText textLayout;
        private int textX;
        private int textY;
        
        public BlockRelatedArticlesHeaderCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textX = AndroidUtilities.dp(18.0f);
            this.parentAdapter = parentAdapter;
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
        
        protected void onMeasure(int size, final int n) {
            size = View$MeasureSpec.getSize(size);
            final TLRPC.TL_pageBlockRelatedArticles currentBlock = this.currentBlock;
            if (currentBlock != null) {
                this.textLayout = ArticleViewer.this.createLayoutForText(this, null, currentBlock.title, size - AndroidUtilities.dp(52.0f), 0, this.currentBlock, Layout$Alignment.ALIGN_NORMAL, 1, this.parentAdapter);
                if (this.textLayout != null) {
                    this.textY = AndroidUtilities.dp(6.0f) + (AndroidUtilities.dp(32.0f) - this.textLayout.getHeight()) / 2;
                }
            }
            if (this.textLayout != null) {
                this.setMeasuredDimension(size, AndroidUtilities.dp(38.0f));
            }
            else {
                this.setMeasuredDimension(size, 1);
            }
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockRelatedArticles currentBlock) {
            this.currentBlock = currentBlock;
            this.requestLayout();
        }
    }
    
    private class BlockRelatedArticlesShadowCell extends View
    {
        private CombinedDrawable shadowDrawable;
        
        public BlockRelatedArticlesShadowCell(final Context context) {
            super(context);
            (this.shadowDrawable = new CombinedDrawable((Drawable)new ColorDrawable(-986896), Theme.getThemedDrawable(context, 2131165395, -16777216))).setFullsize(true);
            this.setBackgroundDrawable((Drawable)this.shadowDrawable);
        }
        
        protected void onMeasure(int access$13100, final int n) {
            this.setMeasuredDimension(View$MeasureSpec.getSize(access$13100), AndroidUtilities.dp(12.0f));
            access$13100 = ArticleViewer.this.getSelectedColor();
            if (access$13100 == 0) {
                Theme.setCombinedDrawableColor(this.shadowDrawable, -986896, false);
            }
            else if (access$13100 == 1) {
                Theme.setCombinedDrawableColor(this.shadowDrawable, -1712440, false);
            }
            else if (access$13100 == 2) {
                Theme.setCombinedDrawableColor(this.shadowDrawable, -15000805, false);
            }
        }
    }
    
    private class BlockSlideshowCell extends FrameLayout
    {
        private DrawingText captionLayout;
        private DrawingText creditLayout;
        private int creditOffset;
        private TLRPC.TL_pageBlockSlideshow currentBlock;
        private int currentPage;
        private View dotsContainer;
        private PagerAdapter innerAdapter;
        private ViewPager innerListView;
        private float pageOffset;
        private WebpageAdapter parentAdapter;
        private int textX;
        private int textY;
        final /* synthetic */ ArticleViewer this$0;
        
        public BlockSlideshowCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textX = AndroidUtilities.dp(18.0f);
            this.parentAdapter = parentAdapter;
            if (ArticleViewer.dotsPaint == null) {
                ArticleViewer.dotsPaint = new Paint(1);
                ArticleViewer.dotsPaint.setColor(-1);
            }
            (this.innerListView = new ViewPager(context) {
                @Override
                public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                    ArticleViewer.this.windowView.requestDisallowInterceptTouchEvent(true);
                    return super.onInterceptTouchEvent(motionEvent);
                }
                
                @Override
                public boolean onTouchEvent(final MotionEvent motionEvent) {
                    return super.onTouchEvent(motionEvent);
                }
            }).addOnPageChangeListener((ViewPager.OnPageChangeListener)new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrollStateChanged(final int n) {
                }
                
                @Override
                public void onPageScrolled(final int n, float n2, final int n3) {
                    n2 = (float)BlockSlideshowCell.this.innerListView.getMeasuredWidth();
                    if (n2 == 0.0f) {
                        return;
                    }
                    final BlockSlideshowCell this$1 = BlockSlideshowCell.this;
                    this$1.pageOffset = (n * n2 + n3 - this$1.currentPage * n2) / n2;
                    BlockSlideshowCell.this.dotsContainer.invalidate();
                }
                
                @Override
                public void onPageSelected(final int n) {
                    BlockSlideshowCell.this.currentPage = n;
                    BlockSlideshowCell.this.dotsContainer.invalidate();
                }
            });
            this.innerListView.setAdapter(this.innerAdapter = new PagerAdapter() {
                @Override
                public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
                    viewGroup.removeView(((ObjectContainer)o).view);
                }
                
                @Override
                public int getCount() {
                    if (BlockSlideshowCell.this.currentBlock == null) {
                        return 0;
                    }
                    return BlockSlideshowCell.this.currentBlock.items.size();
                }
                
                @Override
                public int getItemPosition(final Object o) {
                    if (BlockSlideshowCell.this.currentBlock.items.contains(((ObjectContainer)o).block)) {
                        return -1;
                    }
                    return -2;
                }
                
                @Override
                public Object instantiateItem(final ViewGroup viewGroup, final int index) {
                    final TLRPC.PageBlock pageBlock = BlockSlideshowCell.this.currentBlock.items.get(index);
                    DownloadController.FileDownloadProgressListener fileDownloadProgressListener;
                    if (pageBlock instanceof TLRPC.TL_pageBlockPhoto) {
                        final BlockSlideshowCell this$1 = BlockSlideshowCell.this;
                        fileDownloadProgressListener = this$1.this$0.new BlockPhotoCell(this$1.getContext(), BlockSlideshowCell.this.parentAdapter, 1);
                        ((BlockPhotoCell)fileDownloadProgressListener).setBlock((TLRPC.TL_pageBlockPhoto)pageBlock, true, true);
                    }
                    else {
                        final BlockSlideshowCell this$2 = BlockSlideshowCell.this;
                        fileDownloadProgressListener = this$2.this$0.new BlockVideoCell(this$2.getContext(), BlockSlideshowCell.this.parentAdapter, 1);
                        ((BlockVideoCell)fileDownloadProgressListener).setBlock((TLRPC.TL_pageBlockVideo)pageBlock, true, true);
                    }
                    viewGroup.addView((View)fileDownloadProgressListener);
                    final ObjectContainer objectContainer = new ObjectContainer();
                    objectContainer.view = (View)fileDownloadProgressListener;
                    objectContainer.block = pageBlock;
                    return objectContainer;
                }
                
                @Override
                public boolean isViewFromObject(final View view, final Object o) {
                    return ((ObjectContainer)o).view == view;
                }
                
                @Override
                public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
                    if (dataSetObserver != null) {
                        super.unregisterDataSetObserver(dataSetObserver);
                    }
                }
                
                class ObjectContainer
                {
                    private TLRPC.PageBlock block;
                    private View view;
                }
            });
            final int access$13100 = ArticleViewer.this.getSelectedColor();
            if (access$13100 == 0) {
                AndroidUtilities.setViewPagerEdgeEffectColor(this.innerListView, -657673);
            }
            else if (access$13100 == 1) {
                AndroidUtilities.setViewPagerEdgeEffectColor(this.innerListView, -659492);
            }
            else if (access$13100 == 2) {
                AndroidUtilities.setViewPagerEdgeEffectColor(this.innerListView, -15461356);
            }
            this.addView((View)this.innerListView);
            this.addView(this.dotsContainer = new View(context) {
                protected void onDraw(final Canvas canvas) {
                    if (BlockSlideshowCell.this.currentBlock == null) {
                        return;
                    }
                    final int count = BlockSlideshowCell.this.innerAdapter.getCount();
                    final int n = AndroidUtilities.dp(7.0f) * count + (count - 1) * AndroidUtilities.dp(6.0f) + AndroidUtilities.dp(4.0f);
                    int n2 = 0;
                    Label_0277: {
                        if (n < this.getMeasuredWidth()) {
                            n2 = (this.getMeasuredWidth() - n) / 2;
                        }
                        else {
                            final int dp = AndroidUtilities.dp(4.0f);
                            final int dp2 = AndroidUtilities.dp(13.0f);
                            final int n3 = (this.getMeasuredWidth() - AndroidUtilities.dp(8.0f)) / 2 / dp2;
                            final int access$17400 = BlockSlideshowCell.this.currentPage;
                            final int n4 = count - n3 - 1;
                            if (access$17400 == n4 && BlockSlideshowCell.this.pageOffset < 0.0f) {
                                n2 = dp - ((int)(BlockSlideshowCell.this.pageOffset * dp2) + (count - n3 * 2 - 1) * dp2);
                            }
                            else {
                                int n5;
                                if (BlockSlideshowCell.this.currentPage >= n4) {
                                    n5 = (count - n3 * 2 - 1) * dp2;
                                }
                                else if (BlockSlideshowCell.this.currentPage > n3) {
                                    n5 = (int)(BlockSlideshowCell.this.pageOffset * dp2) + (BlockSlideshowCell.this.currentPage - n3) * dp2;
                                }
                                else {
                                    if (BlockSlideshowCell.this.currentPage != n3 || BlockSlideshowCell.this.pageOffset <= 0.0f) {
                                        n2 = dp;
                                        break Label_0277;
                                    }
                                    n5 = (int)(BlockSlideshowCell.this.pageOffset * dp2);
                                }
                                n2 = dp - n5;
                            }
                        }
                    }
                    for (int i = 0; i < BlockSlideshowCell.this.currentBlock.items.size(); ++i) {
                        final int n6 = AndroidUtilities.dp(4.0f) + n2 + AndroidUtilities.dp(13.0f) * i;
                        Drawable drawable;
                        if (BlockSlideshowCell.this.currentPage == i) {
                            drawable = ArticleViewer.this.slideDotBigDrawable;
                        }
                        else {
                            drawable = ArticleViewer.this.slideDotDrawable;
                        }
                        drawable.setBounds(n6 - AndroidUtilities.dp(5.0f), 0, n6 + AndroidUtilities.dp(5.0f), AndroidUtilities.dp(10.0f));
                        drawable.draw(canvas);
                    }
                }
            });
            this.setWillNotDraw(false);
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.captionLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.captionLayout.draw(canvas);
                canvas.restore();
            }
            if (this.creditLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)(this.textY + this.creditOffset));
                this.creditLayout.draw(canvas);
                canvas.restore();
            }
        }
        
        protected void onLayout(final boolean b, int n, final int n2, final int n3, final int n4) {
            this.innerListView.layout(0, AndroidUtilities.dp(8.0f), this.innerListView.getMeasuredWidth(), AndroidUtilities.dp(8.0f) + this.innerListView.getMeasuredHeight());
            n = this.innerListView.getBottom() - AndroidUtilities.dp(23.0f);
            final View dotsContainer = this.dotsContainer;
            dotsContainer.layout(0, n, dotsContainer.getMeasuredWidth(), this.dotsContainer.getMeasuredHeight() + n);
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int n, int dp) {
            final int size = View$MeasureSpec.getSize(n);
            if (this.currentBlock != null) {
                dp = AndroidUtilities.dp(310.0f);
                this.innerListView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(dp, 1073741824));
                this.currentBlock.items.size();
                this.dotsContainer.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(10.0f), 1073741824));
                final int n2 = size - AndroidUtilities.dp(36.0f);
                this.textY = AndroidUtilities.dp(16.0f) + dp;
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.TL_pageBlockSlideshow currentBlock = this.currentBlock;
                this.captionLayout = this$0.createLayoutForText((View)this, null, currentBlock.caption.text, n2, currentBlock, this.parentAdapter);
                n = dp;
                if (this.captionLayout != null) {
                    this.creditOffset = AndroidUtilities.dp(4.0f) + this.captionLayout.getHeight();
                    n = dp + (this.creditOffset + AndroidUtilities.dp(4.0f));
                }
                final ArticleViewer this$2 = ArticleViewer.this;
                final TLRPC.TL_pageBlockSlideshow currentBlock2 = this.currentBlock;
                final TLRPC.RichText credit = currentBlock2.caption.credit;
                Layout$Alignment layout$Alignment;
                if (this$2.isRtl) {
                    layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                }
                else {
                    layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                }
                this.creditLayout = this$2.createLayoutForText((View)this, null, credit, n2, currentBlock2, layout$Alignment, this.parentAdapter);
                dp = n;
                if (this.creditLayout != null) {
                    dp = n + (AndroidUtilities.dp(4.0f) + this.creditLayout.getHeight());
                }
                n = dp + AndroidUtilities.dp(16.0f);
            }
            else {
                n = 1;
            }
            this.setMeasuredDimension(size, n);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.captionLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.creditLayout, this.textX, this.textY + this.creditOffset) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockSlideshow currentBlock) {
            this.currentBlock = currentBlock;
            this.innerAdapter.notifyDataSetChanged();
            this.innerListView.setCurrentItem(0, false);
            this.innerListView.forceLayout();
            this.requestLayout();
        }
    }
    
    private class BlockSubheaderCell extends View
    {
        private TLRPC.TL_pageBlockSubheader currentBlock;
        private WebpageAdapter parentAdapter;
        private DrawingText textLayout;
        private int textX;
        private int textY;
        
        public BlockSubheaderCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textX = AndroidUtilities.dp(18.0f);
            this.textY = AndroidUtilities.dp(8.0f);
            this.parentAdapter = parentAdapter;
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
        
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setEnabled(true);
            if (this.textLayout == null) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append((Object)this.textLayout.getText());
            sb.append(", ");
            sb.append(LocaleController.getString("AccDescrIVHeading", 2131558439));
            accessibilityNodeInfo.setText((CharSequence)sb.toString());
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int n, int size) {
            size = View$MeasureSpec.getSize(n);
            final TLRPC.TL_pageBlockSubheader currentBlock = this.currentBlock;
            n = 0;
            if (currentBlock != null) {
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.RichText text = currentBlock.text;
                final int dp = AndroidUtilities.dp(36.0f);
                final TLRPC.TL_pageBlockSubheader currentBlock2 = this.currentBlock;
                Layout$Alignment layout$Alignment;
                if (ArticleViewer.this.isRtl) {
                    layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                }
                else {
                    layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                }
                this.textLayout = this$0.createLayoutForText(this, null, text, size - dp, currentBlock2, layout$Alignment, this.parentAdapter);
                if (this.textLayout != null) {
                    n = 0 + (AndroidUtilities.dp(16.0f) + this.textLayout.getHeight());
                }
            }
            else {
                n = 1;
            }
            this.setMeasuredDimension(size, n);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockSubheader currentBlock) {
            this.currentBlock = currentBlock;
            this.requestLayout();
        }
    }
    
    private class BlockSubtitleCell extends View
    {
        private TLRPC.TL_pageBlockSubtitle currentBlock;
        private WebpageAdapter parentAdapter;
        private DrawingText textLayout;
        private int textX;
        private int textY;
        
        public BlockSubtitleCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textX = AndroidUtilities.dp(18.0f);
            this.textY = AndroidUtilities.dp(8.0f);
            this.parentAdapter = parentAdapter;
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
        
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setEnabled(true);
            if (this.textLayout == null) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append((Object)this.textLayout.getText());
            sb.append(", ");
            sb.append(LocaleController.getString("AccDescrIVHeading", 2131558439));
            accessibilityNodeInfo.setText((CharSequence)sb.toString());
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int n, int size) {
            size = View$MeasureSpec.getSize(n);
            final TLRPC.TL_pageBlockSubtitle currentBlock = this.currentBlock;
            n = 0;
            if (currentBlock != null) {
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.RichText text = currentBlock.text;
                final int dp = AndroidUtilities.dp(36.0f);
                final TLRPC.TL_pageBlockSubtitle currentBlock2 = this.currentBlock;
                Layout$Alignment layout$Alignment;
                if (ArticleViewer.this.isRtl) {
                    layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                }
                else {
                    layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                }
                this.textLayout = this$0.createLayoutForText(this, null, text, size - dp, currentBlock2, layout$Alignment, this.parentAdapter);
                if (this.textLayout != null) {
                    n = 0 + (AndroidUtilities.dp(16.0f) + this.textLayout.getHeight());
                }
            }
            else {
                n = 1;
            }
            this.setMeasuredDimension(size, n);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockSubtitle currentBlock) {
            this.currentBlock = currentBlock;
            this.requestLayout();
        }
    }
    
    private class BlockTableCell extends FrameLayout implements TableLayoutDelegate
    {
        private TLRPC.TL_pageBlockTable currentBlock;
        private boolean firstLayout;
        private boolean inLayout;
        private int listX;
        private int listY;
        private WebpageAdapter parentAdapter;
        private HorizontalScrollView scrollView;
        private TableLayout tableLayout;
        private int textX;
        private int textY;
        private DrawingText titleLayout;
        
        public BlockTableCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.parentAdapter = parentAdapter;
            (this.scrollView = new HorizontalScrollView(context) {
                public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                    if (BlockTableCell.this.tableLayout.getMeasuredWidth() > this.getMeasuredWidth() - AndroidUtilities.dp(36.0f)) {
                        ArticleViewer.this.windowView.requestDisallowInterceptTouchEvent(true);
                    }
                    return super.onInterceptTouchEvent(motionEvent);
                }
                
                protected void onMeasure(final int n, final int n2) {
                    BlockTableCell.this.tableLayout.measure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n) - this.getPaddingLeft() - this.getPaddingRight(), 0), n2);
                    this.setMeasuredDimension(View$MeasureSpec.getSize(n), BlockTableCell.this.tableLayout.getMeasuredHeight());
                }
                
                protected void onScrollChanged(final int n, final int n2, final int n3, final int n4) {
                    super.onScrollChanged(n, n2, n3, n4);
                    if (ArticleViewer.this.pressedLinkOwnerLayout != null) {
                        ArticleViewer.this.pressedLinkOwnerLayout = null;
                        ArticleViewer.this.pressedLinkOwnerView = null;
                    }
                }
                
                protected boolean overScrollBy(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final boolean b) {
                    ArticleViewer.this.removePressedLink();
                    return super.overScrollBy(n, n2, n3, n4, n5, n6, n7, n8, b);
                }
            }).setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            this.scrollView.setClipToPadding(false);
            this.addView((View)this.scrollView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
            (this.tableLayout = new TableLayout(context, (TableLayout.TableLayoutDelegate)this)).setOrientation(0);
            this.tableLayout.setRowOrderPreserved(true);
            this.scrollView.addView((View)this.tableLayout, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-2, -2));
            this.setWillNotDraw(false);
        }
        
        public DrawingText createTextLayout(final TLRPC.TL_pageTableCell tl_pageTableCell, final int n) {
            if (tl_pageTableCell == null) {
                return null;
            }
            Layout$Alignment layout$Alignment;
            if (tl_pageTableCell.align_right) {
                layout$Alignment = Layout$Alignment.ALIGN_OPPOSITE;
            }
            else if (tl_pageTableCell.align_center) {
                layout$Alignment = Layout$Alignment.ALIGN_CENTER;
            }
            else {
                layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
            }
            return ArticleViewer.this.createLayoutForText((View)this, null, tl_pageTableCell.text, n, 0, this.currentBlock, layout$Alignment, 0, this.parentAdapter);
        }
        
        public Paint getHalfLinePaint() {
            return ArticleViewer.tableHalfLinePaint;
        }
        
        public Paint getHeaderPaint() {
            return ArticleViewer.tableHeaderPaint;
        }
        
        public Paint getLinePaint() {
            return ArticleViewer.tableLinePaint;
        }
        
        public Paint getStripPaint() {
            return ArticleViewer.tableStripPaint;
        }
        
        public void invalidate() {
            super.invalidate();
            this.tableLayout.invalidate();
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.titleLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.titleLayout.draw(canvas);
                canvas.restore();
            }
            if (this.currentBlock.level > 0) {
                final float n = (float)AndroidUtilities.dp(18.0f);
                final float n2 = (float)AndroidUtilities.dp(20.0f);
                final int measuredHeight = this.getMeasuredHeight();
                int dp;
                if (this.currentBlock.bottom) {
                    dp = AndroidUtilities.dp(6.0f);
                }
                else {
                    dp = 0;
                }
                canvas.drawRect(n, 0.0f, n2, (float)(measuredHeight - dp), ArticleViewer.quoteLinePaint);
            }
        }
        
        protected void onLayout(final boolean b, int listX, final int n, final int n2, final int n3) {
            final HorizontalScrollView scrollView = this.scrollView;
            listX = this.listX;
            scrollView.layout(listX, this.listY, scrollView.getMeasuredWidth() + listX, this.listY + this.scrollView.getMeasuredHeight());
            if (this.firstLayout) {
                if (ArticleViewer.this.isRtl) {
                    this.scrollView.setScrollX(this.tableLayout.getMeasuredWidth() - this.scrollView.getMeasuredWidth() + AndroidUtilities.dp(36.0f));
                }
                else {
                    this.scrollView.setScrollX(0);
                }
                this.firstLayout = false;
            }
        }
        
        protected void onMeasure(int listY, int n) {
            n = 1;
            this.inLayout = true;
            final int size = View$MeasureSpec.getSize(listY);
            final TLRPC.TL_pageBlockTable currentBlock = this.currentBlock;
            listY = n;
            if (currentBlock != null) {
                listY = currentBlock.level;
                if (listY > 0) {
                    this.listX = AndroidUtilities.dp((float)(listY * 14));
                    this.textX = this.listX + AndroidUtilities.dp(18.0f);
                    listY = this.textX;
                }
                else {
                    this.listX = 0;
                    this.textX = AndroidUtilities.dp(18.0f);
                    listY = AndroidUtilities.dp(36.0f);
                }
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.TL_pageBlockTable currentBlock2 = this.currentBlock;
                this.titleLayout = this$0.createLayoutForText((View)this, null, currentBlock2.title, size - listY, 0, currentBlock2, Layout$Alignment.ALIGN_CENTER, 0, this.parentAdapter);
                final DrawingText titleLayout = this.titleLayout;
                if (titleLayout != null) {
                    this.textY = 0;
                    listY = titleLayout.getHeight() + AndroidUtilities.dp(8.0f) + 0;
                    this.listY = listY;
                }
                else {
                    this.listY = AndroidUtilities.dp(8.0f);
                    listY = 0;
                }
                this.scrollView.measure(View$MeasureSpec.makeMeasureSpec(size - this.listX, 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
                n = listY + (this.scrollView.getMeasuredHeight() + AndroidUtilities.dp(8.0f));
                final TLRPC.TL_pageBlockTable currentBlock3 = this.currentBlock;
                listY = n;
                if (currentBlock3.level > 0) {
                    listY = n;
                    if (!currentBlock3.bottom) {
                        listY = n + AndroidUtilities.dp(8.0f);
                    }
                }
            }
            this.setMeasuredDimension(size, listY);
            this.inLayout = false;
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final int childCount = this.tableLayout.getChildCount();
            boolean b = false;
            for (int i = 0; i < childCount; ++i) {
                final TableLayout.Child child = this.tableLayout.getChildAt(i);
                if (ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, child.textLayout, this.scrollView.getPaddingLeft() - this.scrollView.getScrollX() + this.listX + child.getTextX(), this.listY + child.getTextY())) {
                    return true;
                }
            }
            if (ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.titleLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent)) {
                b = true;
            }
            return b;
        }
        
        public void setBlock(final TLRPC.TL_pageBlockTable currentBlock) {
            this.currentBlock = currentBlock;
            final int access$13100 = ArticleViewer.this.getSelectedColor();
            if (access$13100 == 0) {
                AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, -657673);
            }
            else if (access$13100 == 1) {
                AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, -659492);
            }
            else if (access$13100 == 2) {
                AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, -15461356);
            }
            this.tableLayout.removeAllChildrens();
            this.tableLayout.setDrawLines(this.currentBlock.bordered);
            this.tableLayout.setStriped(this.currentBlock.striped);
            this.tableLayout.setRtl(ArticleViewer.this.isRtl);
            int columnCount;
            if (!this.currentBlock.rows.isEmpty()) {
                final TLRPC.TL_pageTableRow tl_pageTableRow = this.currentBlock.rows.get(0);
                final int size = tl_pageTableRow.cells.size();
                int index = 0;
                int n = 0;
                while (true) {
                    columnCount = n;
                    if (index >= size) {
                        break;
                    }
                    int colspan = tl_pageTableRow.cells.get(index).colspan;
                    if (colspan == 0) {
                        colspan = 1;
                    }
                    n += colspan;
                    ++index;
                }
            }
            else {
                columnCount = 0;
            }
            for (int size2 = this.currentBlock.rows.size(), i = 0; i < size2; ++i) {
                final TLRPC.TL_pageTableRow tl_pageTableRow2 = this.currentBlock.rows.get(i);
                final int size3 = tl_pageTableRow2.cells.size();
                int j = 0;
                int n2 = 0;
                while (j < size3) {
                    final TLRPC.TL_pageTableCell tl_pageTableCell = tl_pageTableRow2.cells.get(j);
                    int colspan2 = tl_pageTableCell.colspan;
                    if (colspan2 == 0) {
                        colspan2 = 1;
                    }
                    int rowspan = tl_pageTableCell.rowspan;
                    if (rowspan == 0) {
                        rowspan = 1;
                    }
                    if (tl_pageTableCell.text != null) {
                        this.tableLayout.addChild(tl_pageTableCell, n2, i, colspan2);
                    }
                    else {
                        this.tableLayout.addChild(n2, i, colspan2, rowspan);
                    }
                    n2 += colspan2;
                    ++j;
                }
            }
            this.tableLayout.setColumnCount(columnCount);
            this.firstLayout = true;
            this.requestLayout();
        }
    }
    
    private class BlockTitleCell extends View
    {
        private TLRPC.TL_pageBlockTitle currentBlock;
        private WebpageAdapter parentAdapter;
        private DrawingText textLayout;
        private int textX;
        private int textY;
        
        public BlockTitleCell(final Context context, final WebpageAdapter parentAdapter) {
            super(context);
            this.textX = AndroidUtilities.dp(18.0f);
            this.parentAdapter = parentAdapter;
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.currentBlock == null) {
                return;
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
        
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setEnabled(true);
            if (this.textLayout == null) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append((Object)this.textLayout.getText());
            sb.append(", ");
            sb.append(LocaleController.getString("AccDescrIVTitle", 2131558440));
            accessibilityNodeInfo.setText((CharSequence)sb.toString());
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(int dp, int size) {
            size = View$MeasureSpec.getSize(dp);
            final TLRPC.TL_pageBlockTitle currentBlock = this.currentBlock;
            if (currentBlock != null) {
                final ArticleViewer this$0 = ArticleViewer.this;
                final TLRPC.RichText text = currentBlock.text;
                dp = AndroidUtilities.dp(36.0f);
                final TLRPC.TL_pageBlockTitle currentBlock2 = this.currentBlock;
                Layout$Alignment layout$Alignment;
                if (ArticleViewer.this.isRtl) {
                    layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                }
                else {
                    layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                }
                this.textLayout = this$0.createLayoutForText(this, null, text, size - dp, currentBlock2, layout$Alignment, this.parentAdapter);
                final DrawingText textLayout = this.textLayout;
                dp = 0;
                if (textLayout != null) {
                    dp = 0 + (AndroidUtilities.dp(16.0f) + this.textLayout.getHeight());
                }
                if (this.currentBlock.first) {
                    dp += AndroidUtilities.dp(8.0f);
                    this.textY = AndroidUtilities.dp(16.0f);
                }
                else {
                    this.textY = AndroidUtilities.dp(8.0f);
                }
            }
            else {
                dp = 1;
            }
            this.setMeasuredDimension(size, dp);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return ArticleViewer.this.checkLayoutForLinks(motionEvent, this, this.textLayout, this.textX, this.textY) || super.onTouchEvent(motionEvent);
        }
        
        public void setBlock(final TLRPC.TL_pageBlockTitle currentBlock) {
            this.currentBlock = currentBlock;
            this.requestLayout();
        }
    }
    
    private class BlockVideoCell extends FrameLayout implements FileDownloadProgressListener
    {
        private int TAG;
        private boolean autoDownload;
        private int buttonPressed;
        private int buttonState;
        private int buttonX;
        private int buttonY;
        private boolean cancelLoading;
        private DrawingText captionLayout;
        private BlockChannelCell channelCell;
        private DrawingText creditLayout;
        private int creditOffset;
        private TLRPC.TL_pageBlockVideo currentBlock;
        private TLRPC.Document currentDocument;
        private int currentType;
        private MessageObject.GroupedMessagePosition groupPosition;
        private ImageReceiver imageView;
        private boolean isFirst;
        private boolean isGif;
        private boolean isLast;
        private WebpageAdapter parentAdapter;
        private TLRPC.PageBlock parentBlock;
        private boolean photoPressed;
        private RadialProgress2 radialProgress;
        private int textX;
        private int textY;
        
        public BlockVideoCell(final Context context, final WebpageAdapter parentAdapter, final int currentType) {
            super(context);
            this.parentAdapter = parentAdapter;
            this.setWillNotDraw(false);
            (this.imageView = new ImageReceiver((View)this)).setNeedsQualityThumb(true);
            this.imageView.setShouldGenerateQualityThumb(true);
            this.currentType = currentType;
            (this.radialProgress = new RadialProgress2((View)this)).setProgressColor(-1);
            this.radialProgress.setColors(1711276032, 2130706432, -1, -2500135);
            this.TAG = DownloadController.getInstance(ArticleViewer.this.currentAccount).generateObserverTag();
            this.addView((View)(this.channelCell = new BlockChannelCell(context, this.parentAdapter, 1)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
        }
        
        private void didPressedButton(final boolean b) {
            final int buttonState = this.buttonState;
            if (buttonState == 0) {
                this.cancelLoading = false;
                this.radialProgress.setProgress(0.0f, false);
                if (this.isGif) {
                    this.imageView.setImage(ImageLocation.getForDocument(this.currentDocument), null, ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(this.currentDocument.thumbs, 40), this.currentDocument), "80_80_b", this.currentDocument.size, null, ArticleViewer.this.currentPage, 1);
                }
                else {
                    FileLoader.getInstance(ArticleViewer.this.currentAccount).loadFile(this.currentDocument, ArticleViewer.this.currentPage, 1, 1);
                }
                this.buttonState = 1;
                this.radialProgress.setIcon(this.getIconForCurrentState(), true, b);
                this.invalidate();
            }
            else if (buttonState == 1) {
                this.cancelLoading = true;
                if (this.isGif) {
                    this.imageView.cancelLoadImage();
                }
                else {
                    FileLoader.getInstance(ArticleViewer.this.currentAccount).cancelLoadFile(this.currentDocument);
                }
                this.buttonState = 0;
                this.radialProgress.setIcon(this.getIconForCurrentState(), false, b);
                this.invalidate();
            }
            else if (buttonState == 2) {
                this.imageView.setAllowStartAnimation(true);
                this.imageView.startAnimation();
                this.buttonState = -1;
                this.radialProgress.setIcon(this.getIconForCurrentState(), false, b);
            }
            else if (buttonState == 3) {
                ArticleViewer.this.openPhoto(this.currentBlock);
            }
        }
        
        private int getIconForCurrentState() {
            final int buttonState = this.buttonState;
            if (buttonState == 0) {
                return 2;
            }
            if (buttonState == 1) {
                return 3;
            }
            if (buttonState == 2) {
                return 8;
            }
            if (buttonState == 3) {
                return 0;
            }
            return 4;
        }
        
        public View getChannelCell() {
            return (View)this.channelCell;
        }
        
        public int getObserverTag() {
            return this.TAG;
        }
        
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.imageView.onAttachedToWindow();
            this.updateButtonState(false);
        }
        
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.imageView.onDetachedFromWindow();
            DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
        }
        
        protected void onDraw(final Canvas canvas) {
            final int currentType = this.currentType;
            if (this.currentBlock == null) {
                return;
            }
            if (!this.imageView.hasBitmapImage() || this.imageView.getCurrentAlpha() != 1.0f) {
                canvas.drawRect(this.imageView.getDrawRegion(), ArticleViewer.photoBackgroundPaint);
            }
            this.imageView.draw(canvas);
            if (this.imageView.getVisible()) {
                this.radialProgress.draw(canvas);
            }
            this.textY = this.imageView.getImageY() + this.imageView.getImageHeight() + AndroidUtilities.dp(8.0f);
            if (this.captionLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)this.textY);
                this.captionLayout.draw(canvas);
                canvas.restore();
            }
            if (this.creditLayout != null) {
                canvas.save();
                canvas.translate((float)this.textX, (float)(this.textY + this.creditOffset));
                this.creditLayout.draw(canvas);
                canvas.restore();
            }
            if (this.currentBlock.level > 0) {
                final float n = (float)AndroidUtilities.dp(18.0f);
                final float n2 = (float)AndroidUtilities.dp(20.0f);
                final int measuredHeight = this.getMeasuredHeight();
                int dp;
                if (this.currentBlock.bottom) {
                    dp = AndroidUtilities.dp(6.0f);
                }
                else {
                    dp = 0;
                }
                canvas.drawRect(n, 0.0f, n2, (float)(measuredHeight - dp), ArticleViewer.quoteLinePaint);
            }
        }
        
        public void onFailedDownload(final String s, final boolean b) {
            this.updateButtonState(false);
        }
        
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setEnabled(true);
            final StringBuilder sb = new StringBuilder(LocaleController.getString("AttachVideo", 2131558733));
            if (this.captionLayout != null) {
                sb.append(", ");
                sb.append(this.captionLayout.getText());
            }
            accessibilityNodeInfo.setText((CharSequence)sb.toString());
        }
        
        @SuppressLint({ "NewApi" })
        protected void onMeasure(final int n, final int n2) {
            int n3 = View$MeasureSpec.getSize(n);
            final int currentType = this.currentType;
            int measuredHeight = 0;
            int n4 = 0;
            Label_0103: {
                if (currentType == 1) {
                    n3 = ((View)this.getParent()).getMeasuredWidth();
                    measuredHeight = ((View)this.getParent()).getMeasuredHeight();
                }
                else {
                    if (currentType != 2) {
                        measuredHeight = 0;
                        n4 = n3;
                        break Label_0103;
                    }
                    final float ph = this.groupPosition.ph;
                    final Point displaySize = AndroidUtilities.displaySize;
                    measuredHeight = (int)Math.ceil(ph * Math.max(displaySize.x, displaySize.y) * 0.5f);
                }
                n4 = n3;
            }
            final TLRPC.TL_pageBlockVideo currentBlock = this.currentBlock;
            int n20;
            if (currentBlock != null) {
                int textX = 0;
                int n5 = 0;
                int b = 0;
                Label_0203: {
                    if (this.currentType == 0) {
                        final int level = currentBlock.level;
                        if (level > 0) {
                            textX = AndroidUtilities.dp((float)(level * 14)) + AndroidUtilities.dp(18.0f);
                            this.textX = textX;
                            b = (n5 = n4 - (AndroidUtilities.dp(18.0f) + textX));
                            break Label_0203;
                        }
                    }
                    this.textX = AndroidUtilities.dp(18.0f);
                    n5 = n4 - AndroidUtilities.dp(36.0f);
                    b = n4;
                    textX = 0;
                }
                int n16;
                if (this.currentDocument != null) {
                    final int dp = AndroidUtilities.dp(48.0f);
                    final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.currentDocument.thumbs, 48);
                    final int currentType2 = this.currentType;
                    int dp2 = 0;
                    int n13 = 0;
                    int n15 = 0;
                    Label_0641: {
                        int n9 = 0;
                        int n11 = 0;
                        Label_0630: {
                            if (currentType2 == 0) {
                                final int size = this.currentDocument.attributes.size();
                                int i = 0;
                                while (true) {
                                    while (i < size) {
                                        final TLRPC.DocumentAttribute documentAttribute = this.currentDocument.attributes.get(i);
                                        if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                                            int a = (int)(b / (float)documentAttribute.w * documentAttribute.h);
                                            final int n6 = 1;
                                            float n7;
                                            if (closestPhotoSizeWithSize != null) {
                                                n7 = (float)closestPhotoSizeWithSize.w;
                                            }
                                            else {
                                                n7 = 100.0f;
                                            }
                                            float n8;
                                            if (closestPhotoSizeWithSize != null) {
                                                n8 = (float)closestPhotoSizeWithSize.h;
                                            }
                                            else {
                                                n8 = 100.0f;
                                            }
                                            if (n6 == 0) {
                                                a = (int)(b / n7 * n8);
                                            }
                                            int min;
                                            if (this.parentBlock instanceof TLRPC.TL_pageBlockCover) {
                                                min = Math.min(a, b);
                                                n9 = textX;
                                            }
                                            else {
                                                final int n10 = (int)((Math.max(ArticleViewer.this.listView[0].getMeasuredWidth(), ArticleViewer.this.listView[0].getMeasuredHeight()) - AndroidUtilities.dp(56.0f)) * 0.9f);
                                                n9 = textX;
                                                if ((min = a) > n10) {
                                                    b = (int)(n10 / n8 * n7);
                                                    n9 = textX + (n4 - textX - b) / 2;
                                                    min = n10;
                                                }
                                            }
                                            if (min == 0) {
                                                dp2 = AndroidUtilities.dp(100.0f);
                                                n11 = b;
                                                break Label_0630;
                                            }
                                            if (min < dp) {
                                                dp2 = dp;
                                                n11 = b;
                                                break Label_0630;
                                            }
                                            dp2 = min;
                                            n11 = b;
                                            break Label_0630;
                                        }
                                        else {
                                            ++i;
                                        }
                                    }
                                    final int n12 = 0;
                                    int a = measuredHeight;
                                    final int n6 = n12;
                                    continue;
                                }
                            }
                            n9 = textX;
                            dp2 = measuredHeight;
                            n11 = b;
                            if (currentType2 == 2) {
                                n13 = b;
                                if ((this.groupPosition.flags & 0x2) == 0x0) {
                                    n13 = b - AndroidUtilities.dp(2.0f);
                                }
                                n9 = textX;
                                dp2 = measuredHeight;
                                n11 = n13;
                                if ((this.groupPosition.flags & 0x8) == 0x0) {
                                    final int dp3 = AndroidUtilities.dp(2.0f);
                                    final int n14 = measuredHeight;
                                    dp2 = measuredHeight - dp3;
                                    n15 = n14;
                                    break Label_0641;
                                }
                            }
                        }
                        n15 = dp2;
                        n13 = n11;
                        textX = n9;
                    }
                    this.imageView.setQualityThumbDocument(this.currentDocument);
                    final ImageReceiver imageView = this.imageView;
                    int dp4 = 0;
                    Label_0710: {
                        if (!this.isFirst) {
                            final int currentType3 = this.currentType;
                            if (currentType3 != 1 && currentType3 != 2) {
                                if (this.currentBlock.level <= 0) {
                                    dp4 = AndroidUtilities.dp(8.0f);
                                    break Label_0710;
                                }
                            }
                        }
                        dp4 = 0;
                    }
                    imageView.setImageCoords(textX, dp4, n13, dp2);
                    if (this.isGif) {
                        this.autoDownload = DownloadController.getInstance(ArticleViewer.this.currentAccount).canDownloadMedia(4, this.currentDocument.size);
                        final File pathToAttach = FileLoader.getPathToAttach(this.currentDocument, true);
                        if (!this.autoDownload && !pathToAttach.exists()) {
                            this.imageView.setStrippedLocation(ImageLocation.getForDocument(this.currentDocument));
                            this.imageView.setImage(null, null, null, null, ImageLocation.getForDocument(closestPhotoSizeWithSize, this.currentDocument), "80_80_b", null, this.currentDocument.size, null, ArticleViewer.this.currentPage, 1);
                        }
                        else {
                            this.imageView.setStrippedLocation(null);
                            this.imageView.setImage(ImageLocation.getForDocument(this.currentDocument), null, null, null, ImageLocation.getForDocument(closestPhotoSizeWithSize, this.currentDocument), "80_80_b", null, this.currentDocument.size, null, ArticleViewer.this.currentPage, 1);
                        }
                    }
                    else {
                        this.imageView.setStrippedLocation(null);
                        this.imageView.setImage(null, null, ImageLocation.getForDocument(closestPhotoSizeWithSize, this.currentDocument), "80_80_b", 0, null, ArticleViewer.this.currentPage, 1);
                    }
                    this.imageView.setAspectFit(true);
                    this.buttonX = (int)(this.imageView.getImageX() + (this.imageView.getImageWidth() - dp) / 2.0f);
                    this.buttonY = (int)(this.imageView.getImageY() + (this.imageView.getImageHeight() - dp) / 2.0f);
                    final RadialProgress2 radialProgress = this.radialProgress;
                    final int buttonX = this.buttonX;
                    final int buttonY = this.buttonY;
                    radialProgress.setProgressRect(buttonX, buttonY, buttonX + dp, dp + buttonY);
                    n16 = n15;
                }
                else {
                    n16 = measuredHeight;
                }
                int n17 = n16;
                if (this.currentType == 0) {
                    final ArticleViewer this$0 = ArticleViewer.this;
                    final TLRPC.TL_pageBlockVideo currentBlock2 = this.currentBlock;
                    this.captionLayout = this$0.createLayoutForText((View)this, null, currentBlock2.caption.text, n5, currentBlock2, this.parentAdapter);
                    int n18 = n16;
                    if (this.captionLayout != null) {
                        this.creditOffset = AndroidUtilities.dp(4.0f) + this.captionLayout.getHeight();
                        n18 = n16 + (this.creditOffset + AndroidUtilities.dp(4.0f));
                    }
                    final ArticleViewer this$2 = ArticleViewer.this;
                    final TLRPC.TL_pageBlockVideo currentBlock3 = this.currentBlock;
                    final TLRPC.RichText credit = currentBlock3.caption.credit;
                    Layout$Alignment layout$Alignment;
                    if (this$2.isRtl) {
                        layout$Alignment = Layout$Alignment.ALIGN_RIGHT;
                    }
                    else {
                        layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
                    }
                    this.creditLayout = this$2.createLayoutForText((View)this, null, credit, n5, currentBlock3, layout$Alignment, this.parentAdapter);
                    n17 = n18;
                    if (this.creditLayout != null) {
                        n17 = n18 + (AndroidUtilities.dp(4.0f) + this.creditLayout.getHeight());
                    }
                }
                int n19 = n17;
                if (!this.isFirst) {
                    n19 = n17;
                    if (this.currentType == 0) {
                        n19 = n17;
                        if (this.currentBlock.level <= 0) {
                            n19 = n17 + AndroidUtilities.dp(8.0f);
                        }
                    }
                }
                boolean b2 = false;
                Label_1322: {
                    if (this.parentBlock instanceof TLRPC.TL_pageBlockCover) {
                        final int size2 = this.parentAdapter.blocks.size();
                        b2 = true;
                        if (size2 > 1 && this.parentAdapter.blocks.get(1) instanceof TLRPC.TL_pageBlockChannel) {
                            break Label_1322;
                        }
                    }
                    b2 = false;
                }
                n20 = n19;
                if (this.currentType != 2) {
                    n20 = n19;
                    if (!b2) {
                        n20 = n19 + AndroidUtilities.dp(8.0f);
                    }
                }
            }
            else {
                n20 = 1;
            }
            this.channelCell.measure(n, n2);
            this.channelCell.setTranslationY((float)(this.imageView.getImageHeight() - AndroidUtilities.dp(39.0f)));
            this.setMeasuredDimension(n4, n20);
        }
        
        public void onProgressDownload(final String s, final float n) {
            this.radialProgress.setProgress(n, true);
            if (this.buttonState != 1) {
                this.updateButtonState(true);
            }
        }
        
        public void onProgressUpload(final String s, final float n, final boolean b) {
        }
        
        public void onSuccessDownload(final String s) {
            this.radialProgress.setProgress(1.0f, true);
            if (this.isGif) {
                this.buttonState = 2;
                this.didPressedButton(true);
            }
            else {
                this.updateButtonState(true);
            }
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final float x = motionEvent.getX();
            final float y = motionEvent.getY();
            final int visibility = this.channelCell.getVisibility();
            boolean b = false;
            if (visibility == 0 && y > this.channelCell.getTranslationY() && y < this.channelCell.getTranslationY() + AndroidUtilities.dp(39.0f)) {
                if (ArticleViewer.this.channelBlock != null && motionEvent.getAction() == 1) {
                    MessagesController.getInstance(ArticleViewer.this.currentAccount).openByUserName(ArticleViewer.this.channelBlock.channel.username, ArticleViewer.this.parentFragment, 2);
                    ArticleViewer.this.close(false, true);
                }
                return true;
            }
            Label_0312: {
                if (motionEvent.getAction() == 0 && this.imageView.isInsideImage(x, y)) {
                    Label_0214: {
                        if (this.buttonState != -1) {
                            final int buttonX = this.buttonX;
                            if (x >= buttonX && x <= buttonX + AndroidUtilities.dp(48.0f)) {
                                final int buttonY = this.buttonY;
                                if (y >= buttonY && y <= buttonY + AndroidUtilities.dp(48.0f)) {
                                    break Label_0214;
                                }
                            }
                        }
                        if (this.buttonState != 0) {
                            this.photoPressed = true;
                            break Label_0312;
                        }
                    }
                    this.buttonPressed = 1;
                    this.invalidate();
                }
                else if (motionEvent.getAction() == 1) {
                    if (this.photoPressed) {
                        this.photoPressed = false;
                        ArticleViewer.this.openPhoto(this.currentBlock);
                    }
                    else if (this.buttonPressed == 1) {
                        this.playSoundEffect(this.buttonPressed = 0);
                        this.didPressedButton(true);
                        this.invalidate();
                    }
                }
                else if (motionEvent.getAction() == 3) {
                    this.photoPressed = false;
                }
            }
            if (this.photoPressed || this.buttonPressed != 0 || ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.captionLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(motionEvent, (View)this, this.creditLayout, this.textX, this.textY + this.creditOffset) || super.onTouchEvent(motionEvent)) {
                b = true;
            }
            return b;
        }
        
        public void setBlock(final TLRPC.TL_pageBlockVideo currentBlock, final boolean isFirst, final boolean isLast) {
            this.currentBlock = currentBlock;
            this.parentBlock = null;
            this.cancelLoading = false;
            this.currentDocument = ArticleViewer.this.getDocumentWithId(this.currentBlock.video_id);
            this.isGif = MessageObject.isGifDocument(this.currentDocument);
            this.isFirst = isFirst;
            this.isLast = isLast;
            this.channelCell.setVisibility(4);
            this.updateButtonState(false);
            this.requestLayout();
        }
        
        public void setParentBlock(final TLRPC.PageBlock parentBlock) {
            this.parentBlock = parentBlock;
            if (ArticleViewer.this.channelBlock != null && this.parentBlock instanceof TLRPC.TL_pageBlockCover) {
                this.channelCell.setBlock(ArticleViewer.this.channelBlock);
                this.channelCell.setVisibility(0);
            }
        }
        
        public void updateButtonState(final boolean b) {
            final String attachFileName = FileLoader.getAttachFileName(this.currentDocument);
            final TLRPC.Document currentDocument = this.currentDocument;
            final boolean b2 = true;
            final boolean exists = FileLoader.getPathToAttach(currentDocument, true).exists();
            if (TextUtils.isEmpty((CharSequence)attachFileName)) {
                this.radialProgress.setIcon(4, false, false);
                return;
            }
            if (exists) {
                DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
                if (!this.isGif) {
                    this.buttonState = 3;
                }
                else {
                    this.buttonState = -1;
                }
                this.radialProgress.setIcon(this.getIconForCurrentState(), false, b);
                this.invalidate();
            }
            else {
                DownloadController.getInstance(ArticleViewer.this.currentAccount).addLoadingFileObserver(attachFileName, null, (DownloadController.FileDownloadProgressListener)this);
                final boolean loadingFile = FileLoader.getInstance(ArticleViewer.this.currentAccount).isLoadingFile(attachFileName);
                float floatValue = 0.0f;
                boolean b3;
                if (!loadingFile) {
                    if (!this.cancelLoading && this.autoDownload && this.isGif) {
                        this.buttonState = 1;
                        b3 = b2;
                    }
                    else {
                        this.buttonState = 0;
                        b3 = false;
                    }
                }
                else {
                    this.buttonState = 1;
                    final Float fileProgress = ImageLoader.getInstance().getFileProgress(attachFileName);
                    b3 = b2;
                    if (fileProgress != null) {
                        floatValue = fileProgress;
                        b3 = b2;
                    }
                }
                this.radialProgress.setIcon(this.getIconForCurrentState(), b3, b);
                this.radialProgress.setProgress(floatValue, false);
                this.invalidate();
            }
        }
    }
    
    class CheckForLongPress implements Runnable
    {
        public int currentPressCount;
        
        @Override
        public void run() {
            if (ArticleViewer.this.checkingForLongPress && ArticleViewer.this.windowView != null) {
                ArticleViewer.this.checkingForLongPress = false;
                if (ArticleViewer.this.pressedLink != null) {
                    ArticleViewer.this.windowView.performHapticFeedback(0);
                    final ArticleViewer this$0 = ArticleViewer.this;
                    this$0.showCopyPopup(this$0.pressedLink.getUrl());
                    ArticleViewer.this.pressedLink = null;
                    ArticleViewer.this.pressedLinkOwnerLayout = null;
                    if (ArticleViewer.this.pressedLinkOwnerView != null) {
                        ArticleViewer.this.pressedLinkOwnerView.invalidate();
                    }
                }
                else if (ArticleViewer.this.pressedLinkOwnerLayout != null && ArticleViewer.this.pressedLinkOwnerView != null) {
                    ArticleViewer.this.windowView.performHapticFeedback(0);
                    final int[] array = new int[2];
                    ArticleViewer.this.pressedLinkOwnerView.getLocationInWindow(array);
                    int n;
                    if ((n = array[1] + ArticleViewer.this.pressedLayoutY - AndroidUtilities.dp(54.0f)) < 0) {
                        n = 0;
                    }
                    ArticleViewer.this.pressedLinkOwnerView.invalidate();
                    ArticleViewer.this.drawBlockSelection = true;
                    final ArticleViewer this$2 = ArticleViewer.this;
                    this$2.showPopup(this$2.pressedLinkOwnerView, 48, 0, n);
                    ArticleViewer.this.listView[0].setLayoutFrozen(true);
                    ArticleViewer.this.listView[0].setLayoutFrozen(false);
                }
            }
        }
    }
    
    private final class CheckForTap implements Runnable
    {
        @Override
        public void run() {
            if (ArticleViewer.this.pendingCheckForLongPress == null) {
                final ArticleViewer this$0 = ArticleViewer.this;
                this$0.pendingCheckForLongPress = this$0.new CheckForLongPress();
            }
            ArticleViewer.this.pendingCheckForLongPress.currentPressCount = ++ArticleViewer.this.pressCount;
            if (ArticleViewer.this.windowView != null) {
                ArticleViewer.this.windowView.postDelayed((Runnable)ArticleViewer.this.pendingCheckForLongPress, (long)(ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout()));
            }
        }
    }
    
    public class ColorCell extends FrameLayout
    {
        private int currentColor;
        private boolean selected;
        private TextView textView;
        
        public ColorCell(final Context context) {
            super(context);
            if (ArticleViewer.colorPaint == null) {
                ArticleViewer.colorPaint = new Paint(1);
                ArticleViewer.selectorPaint = new Paint(1);
                ArticleViewer.selectorPaint.setColor(-15428119);
                ArticleViewer.selectorPaint.setStyle(Paint$Style.STROKE);
                ArticleViewer.selectorPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
            }
            this.setBackgroundDrawable(Theme.createSelectorDrawable(251658240, 2));
            this.setWillNotDraw(false);
            (this.textView = new TextView(context)).setTextColor(-14606047);
            this.textView.setTextSize(1, 16.0f);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            final TextView textView = this.textView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 5;
            int n2;
            if (isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            textView.setGravity(n2 | 0x10);
            this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(1.0f));
            final TextView textView2 = this.textView;
            int n3;
            if (LocaleController.isRTL) {
                n3 = n;
            }
            else {
                n3 = 3;
            }
            final boolean isRTL2 = LocaleController.isRTL;
            final int n4 = 17;
            int n5;
            if (isRTL2) {
                n5 = 17;
            }
            else {
                n5 = 53;
            }
            final float n6 = (float)n5;
            int n7 = n4;
            if (LocaleController.isRTL) {
                n7 = 53;
            }
            this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, n6, 0.0f, (float)n7, 0.0f));
        }
        
        protected void onDraw(final Canvas canvas) {
            ArticleViewer.colorPaint.setColor(this.currentColor);
            int dp;
            if (!LocaleController.isRTL) {
                dp = AndroidUtilities.dp(28.0f);
            }
            else {
                dp = this.getMeasuredWidth() - AndroidUtilities.dp(28.0f);
            }
            canvas.drawCircle((float)dp, (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(10.0f), ArticleViewer.colorPaint);
            if (this.selected) {
                ArticleViewer.selectorPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
                ArticleViewer.selectorPaint.setColor(-15428119);
                int dp2;
                if (!LocaleController.isRTL) {
                    dp2 = AndroidUtilities.dp(28.0f);
                }
                else {
                    dp2 = this.getMeasuredWidth() - AndroidUtilities.dp(28.0f);
                }
                canvas.drawCircle((float)dp2, (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(10.0f), ArticleViewer.selectorPaint);
            }
            else if (this.currentColor == -1) {
                ArticleViewer.selectorPaint.setStrokeWidth((float)AndroidUtilities.dp(1.0f));
                ArticleViewer.selectorPaint.setColor(-4539718);
                int dp3;
                if (!LocaleController.isRTL) {
                    dp3 = AndroidUtilities.dp(28.0f);
                }
                else {
                    dp3 = this.getMeasuredWidth() - AndroidUtilities.dp(28.0f);
                }
                canvas.drawCircle((float)dp3, (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(9.0f), ArticleViewer.selectorPaint);
            }
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        }
        
        public void select(final boolean selected) {
            if (this.selected == selected) {
                return;
            }
            this.selected = selected;
            this.invalidate();
        }
        
        public void setTextAndColor(final String text, final int currentColor) {
            this.textView.setText((CharSequence)text);
            this.currentColor = currentColor;
            this.invalidate();
        }
    }
    
    public class DrawingText
    {
        public LinkPath markPath;
        public StaticLayout textLayout;
        public LinkPath textPath;
        
        public void draw(final Canvas canvas) {
            final LinkPath textPath = this.textPath;
            if (textPath != null) {
                canvas.drawPath((Path)textPath, ArticleViewer.webpageUrlPaint);
            }
            final LinkPath markPath = this.markPath;
            if (markPath != null) {
                canvas.drawPath((Path)markPath, ArticleViewer.webpageMarkPaint);
            }
            ArticleViewer.this.drawLayoutLink(canvas, this);
            this.textLayout.draw(canvas);
        }
        
        public int getHeight() {
            return this.textLayout.getHeight();
        }
        
        public int getLineAscent(final int n) {
            return this.textLayout.getLineAscent(n);
        }
        
        public int getLineCount() {
            return this.textLayout.getLineCount();
        }
        
        public float getLineLeft(final int n) {
            return this.textLayout.getLineLeft(n);
        }
        
        public float getLineWidth(final int n) {
            return this.textLayout.getLineWidth(n);
        }
        
        public CharSequence getText() {
            return this.textLayout.getText();
        }
        
        public int getWidth() {
            return this.textLayout.getWidth();
        }
    }
    
    public class FontCell extends FrameLayout
    {
        private TextView textView;
        private TextView textView2;
        
        public FontCell(final Context context) {
            super(context);
            this.setBackgroundDrawable(Theme.createSelectorDrawable(251658240, 2));
            (this.textView = new TextView(context)).setTextColor(-14606047);
            this.textView.setTextSize(1, 16.0f);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            final TextView textView = this.textView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 5;
            int n2;
            if (isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            textView.setGravity(n2 | 0x10);
            final TextView textView2 = this.textView;
            int n3;
            if (LocaleController.isRTL) {
                n3 = 5;
            }
            else {
                n3 = 3;
            }
            final boolean isRTL2 = LocaleController.isRTL;
            final int n4 = 53;
            int n5;
            if (isRTL2) {
                n5 = 17;
            }
            else {
                n5 = 53;
            }
            final float n6 = (float)n5;
            int n7;
            if (LocaleController.isRTL) {
                n7 = n4;
            }
            else {
                n7 = 17;
            }
            this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, n6, 0.0f, (float)n7, 0.0f));
            (this.textView2 = new TextView(context)).setTextColor(-14606047);
            this.textView2.setTextSize(1, 16.0f);
            this.textView2.setLines(1);
            this.textView2.setMaxLines(1);
            this.textView2.setSingleLine(true);
            this.textView2.setText((CharSequence)"Aa");
            final TextView textView3 = this.textView2;
            int n8;
            if (LocaleController.isRTL) {
                n8 = 5;
            }
            else {
                n8 = 3;
            }
            textView3.setGravity(n8 | 0x10);
            final TextView textView4 = this.textView2;
            int n9;
            if (LocaleController.isRTL) {
                n9 = n;
            }
            else {
                n9 = 3;
            }
            this.addView((View)textView4, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n9 | 0x30, 17.0f, 0.0f, 17.0f, 0.0f));
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        }
        
        public void select(final boolean b) {
            final TextView textView2 = this.textView2;
            int textColor;
            if (b) {
                textColor = -15428119;
            }
            else {
                textColor = -14606047;
            }
            textView2.setTextColor(textColor);
        }
        
        public void setTextAndTypeface(final String text, final Typeface typeface) {
            this.textView.setText((CharSequence)text);
            this.textView.setTypeface(typeface);
            this.textView2.setTypeface(typeface);
            this.invalidate();
        }
    }
    
    private class FrameLayoutDrawer extends FrameLayout
    {
        public FrameLayoutDrawer(final Context context) {
            super(context);
        }
        
        protected boolean drawChild(final Canvas canvas, final View view, final long n) {
            return view != ArticleViewer.this.aspectRatioFrameLayout && super.drawChild(canvas, view, n);
        }
        
        protected void onDraw(final Canvas canvas) {
            ArticleViewer.this.drawContent(canvas);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            ArticleViewer.this.processTouchEvent(motionEvent);
            return true;
        }
    }
    
    private class PhotoBackgroundDrawable extends ColorDrawable
    {
        private Runnable drawRunnable;
        
        public PhotoBackgroundDrawable(final int n) {
            super(n);
        }
        
        public void draw(final Canvas canvas) {
            super.draw(canvas);
            if (this.getAlpha() != 0) {
                final Runnable drawRunnable = this.drawRunnable;
                if (drawRunnable != null) {
                    drawRunnable.run();
                    this.drawRunnable = null;
                }
            }
        }
        
        @Keep
        public void setAlpha(final int alpha) {
            if (ArticleViewer.this.parentActivity instanceof LaunchActivity) {
                ((LaunchActivity)ArticleViewer.this.parentActivity).drawerLayoutContainer.setAllowDrawContent(!ArticleViewer.this.isPhotoVisible || alpha != 255);
            }
            super.setAlpha(alpha);
        }
    }
    
    public static class PlaceProviderObject
    {
        public int clipBottomAddition;
        public int clipTopAddition;
        public ImageReceiver imageReceiver;
        public int index;
        public View parentView;
        public int radius;
        public float scale;
        public int size;
        public ImageReceiver.BitmapHolder thumb;
        public int viewX;
        public int viewY;
        
        public PlaceProviderObject() {
            this.scale = 1.0f;
        }
    }
    
    private class RadialProgressView
    {
        private float alpha;
        private float animatedAlphaValue;
        private float animatedProgressValue;
        private float animationProgressStart;
        private int backgroundState;
        private float currentProgress;
        private long currentProgressTime;
        private long lastUpdateTime;
        private View parent;
        private int previousBackgroundState;
        private RectF progressRect;
        private float radOffset;
        private float scale;
        private int size;
        
        public RadialProgressView(final Context context, final View parent) {
            this.lastUpdateTime = 0L;
            this.radOffset = 0.0f;
            this.currentProgress = 0.0f;
            this.animationProgressStart = 0.0f;
            this.currentProgressTime = 0L;
            this.animatedProgressValue = 0.0f;
            this.progressRect = new RectF();
            this.backgroundState = -1;
            this.size = AndroidUtilities.dp(64.0f);
            this.previousBackgroundState = -2;
            this.animatedAlphaValue = 1.0f;
            this.alpha = 1.0f;
            this.scale = 1.0f;
            if (ArticleViewer.decelerateInterpolator == null) {
                ArticleViewer.decelerateInterpolator = new DecelerateInterpolator(1.5f);
                ArticleViewer.progressPaint = new Paint(1);
                ArticleViewer.progressPaint.setStyle(Paint$Style.STROKE);
                ArticleViewer.progressPaint.setStrokeCap(Paint$Cap.ROUND);
                ArticleViewer.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(3.0f));
                ArticleViewer.progressPaint.setColor(-1);
            }
            this.parent = parent;
        }
        
        private void updateAnimation() {
            final long currentTimeMillis = System.currentTimeMillis();
            final long n = currentTimeMillis - this.lastUpdateTime;
            this.lastUpdateTime = currentTimeMillis;
            if (this.animatedProgressValue != 1.0f) {
                this.radOffset += 360L * n / 3000.0f;
                final float currentProgress = this.currentProgress;
                final float animationProgressStart = this.animationProgressStart;
                final float n2 = currentProgress - animationProgressStart;
                if (n2 > 0.0f) {
                    this.currentProgressTime += n;
                    if (this.currentProgressTime >= 300L) {
                        this.animatedProgressValue = currentProgress;
                        this.animationProgressStart = currentProgress;
                        this.currentProgressTime = 0L;
                    }
                    else {
                        this.animatedProgressValue = animationProgressStart + n2 * ArticleViewer.decelerateInterpolator.getInterpolation(this.currentProgressTime / 300.0f);
                    }
                }
                this.parent.invalidate();
            }
            if (this.animatedProgressValue >= 1.0f && this.previousBackgroundState != -2) {
                this.animatedAlphaValue -= n / 200.0f;
                if (this.animatedAlphaValue <= 0.0f) {
                    this.animatedAlphaValue = 0.0f;
                    this.previousBackgroundState = -2;
                }
                this.parent.invalidate();
            }
        }
        
        public void onDraw(final Canvas canvas) {
            final int n = (int)(this.size * this.scale);
            final int n2 = (ArticleViewer.this.getContainerViewWidth() - n) / 2;
            final int n3 = (ArticleViewer.this.getContainerViewHeight() - n) / 2;
            final int previousBackgroundState = this.previousBackgroundState;
            if (previousBackgroundState >= 0 && previousBackgroundState < 4) {
                final Drawable drawable = ArticleViewer.progressDrawables[this.previousBackgroundState];
                if (drawable != null) {
                    drawable.setAlpha((int)(this.animatedAlphaValue * 255.0f * this.alpha));
                    drawable.setBounds(n2, n3, n2 + n, n3 + n);
                    drawable.draw(canvas);
                }
            }
            final int backgroundState = this.backgroundState;
            if (backgroundState >= 0 && backgroundState < 4) {
                final Drawable drawable2 = ArticleViewer.progressDrawables[this.backgroundState];
                if (drawable2 != null) {
                    if (this.previousBackgroundState != -2) {
                        drawable2.setAlpha((int)((1.0f - this.animatedAlphaValue) * 255.0f * this.alpha));
                    }
                    else {
                        drawable2.setAlpha((int)(this.alpha * 255.0f));
                    }
                    drawable2.setBounds(n2, n3, n2 + n, n3 + n);
                    drawable2.draw(canvas);
                }
            }
            final int backgroundState2 = this.backgroundState;
            if (backgroundState2 != 0 && backgroundState2 != 1) {
                final int previousBackgroundState2 = this.previousBackgroundState;
                if (previousBackgroundState2 != 0 && previousBackgroundState2 != 1) {
                    return;
                }
            }
            final int dp = AndroidUtilities.dp(4.0f);
            if (this.previousBackgroundState != -2) {
                ArticleViewer.progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0f * this.alpha));
            }
            else {
                ArticleViewer.progressPaint.setAlpha((int)(this.alpha * 255.0f));
            }
            this.progressRect.set((float)(n2 + dp), (float)(n3 + dp), (float)(n2 + n - dp), (float)(n3 + n - dp));
            canvas.drawArc(this.progressRect, this.radOffset - 90.0f, Math.max(4.0f, this.animatedProgressValue * 360.0f), false, ArticleViewer.progressPaint);
            this.updateAnimation();
        }
        
        public void setAlpha(final float alpha) {
            this.alpha = alpha;
        }
        
        public void setBackgroundState(final int backgroundState, final boolean b) {
            this.lastUpdateTime = System.currentTimeMillis();
            Label_0040: {
                if (b) {
                    final int backgroundState2 = this.backgroundState;
                    if (backgroundState2 != backgroundState) {
                        this.previousBackgroundState = backgroundState2;
                        this.animatedAlphaValue = 1.0f;
                        break Label_0040;
                    }
                }
                this.previousBackgroundState = -2;
            }
            this.backgroundState = backgroundState;
            this.parent.invalidate();
        }
        
        public void setProgress(final float currentProgress, final boolean b) {
            if (!b) {
                this.animatedProgressValue = currentProgress;
                this.animationProgressStart = currentProgress;
            }
            else {
                this.animationProgressStart = this.animatedProgressValue;
            }
            this.currentProgress = currentProgress;
            this.currentProgressTime = 0L;
        }
        
        public void setScale(final float scale) {
            this.scale = scale;
        }
    }
    
    public class ScrollEvaluator extends IntEvaluator
    {
        public Integer evaluate(final float n, final Integer n2, final Integer n3) {
            return super.evaluate(n, n2, n3);
        }
    }
    
    private class SizeChooseView extends View
    {
        private int circleSize;
        private int gapSize;
        private int lineSize;
        private boolean moving;
        private Paint paint;
        private int sideSide;
        private boolean startMoving;
        private int startMovingQuality;
        private float startX;
        
        public SizeChooseView(final Context context) {
            super(context);
            this.paint = new Paint(1);
        }
        
        protected void onDraw(final Canvas canvas) {
            final int n = this.getMeasuredHeight() / 2;
            for (int i = 0; i < 5; ++i) {
                final int sideSide = this.sideSide;
                final int lineSize = this.lineSize;
                final int gapSize = this.gapSize;
                final int circleSize = this.circleSize;
                final int n2 = sideSide + (lineSize + gapSize * 2 + circleSize) * i + circleSize / 2;
                if (i <= ArticleViewer.this.selectedFontSize) {
                    this.paint.setColor(-15428119);
                }
                else {
                    this.paint.setColor(-3355444);
                }
                final float n3 = (float)n2;
                final float n4 = (float)n;
                int dp;
                if (i == ArticleViewer.this.selectedFontSize) {
                    dp = AndroidUtilities.dp(4.0f);
                }
                else {
                    dp = this.circleSize / 2;
                }
                canvas.drawCircle(n3, n4, (float)dp, this.paint);
                if (i != 0) {
                    final int n5 = n2 - this.circleSize / 2 - this.gapSize - this.lineSize;
                    canvas.drawRect((float)n5, (float)(n - AndroidUtilities.dp(1.0f)), (float)(n5 + this.lineSize), (float)(AndroidUtilities.dp(1.0f) + n), this.paint);
                }
            }
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(n, n2);
            View$MeasureSpec.getSize(n);
            this.circleSize = AndroidUtilities.dp(5.0f);
            this.gapSize = AndroidUtilities.dp(2.0f);
            this.sideSide = AndroidUtilities.dp(17.0f);
            this.lineSize = (this.getMeasuredWidth() - this.circleSize * 5 - this.gapSize * 2 * 4 - this.sideSide * 2) / 4;
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final float x = motionEvent.getX();
            final int action = motionEvent.getAction();
            int i = 0;
            boolean startMoving = false;
            if (action == 0) {
                this.getParent().requestDisallowInterceptTouchEvent(true);
                for (int j = 0; j < 5; ++j) {
                    final int sideSide = this.sideSide;
                    final int lineSize = this.lineSize;
                    final int gapSize = this.gapSize;
                    final int circleSize = this.circleSize;
                    final int n = sideSide + (lineSize + gapSize * 2 + circleSize) * j + circleSize / 2;
                    if (x > n - AndroidUtilities.dp(15.0f) && x < n + AndroidUtilities.dp(15.0f)) {
                        if (j == ArticleViewer.this.selectedFontSize) {
                            startMoving = true;
                        }
                        this.startMoving = startMoving;
                        this.startX = x;
                        this.startMovingQuality = ArticleViewer.this.selectedFontSize;
                        break;
                    }
                }
            }
            else if (motionEvent.getAction() == 2) {
                if (this.startMoving) {
                    if (Math.abs(this.startX - x) >= AndroidUtilities.getPixelsInCM(0.5f, true)) {
                        this.moving = true;
                        this.startMoving = false;
                    }
                }
                else if (this.moving) {
                    while (i < 5) {
                        final int sideSide2 = this.sideSide;
                        final int lineSize2 = this.lineSize;
                        final int gapSize2 = this.gapSize;
                        final int circleSize2 = this.circleSize;
                        final int n2 = sideSide2 + (gapSize2 * 2 + lineSize2 + circleSize2) * i + circleSize2 / 2;
                        final int n3 = lineSize2 / 2 + circleSize2 / 2 + gapSize2;
                        if (x > n2 - n3 && x < n2 + n3) {
                            if (ArticleViewer.this.selectedFontSize != i) {
                                ArticleViewer.this.selectedFontSize = i;
                                ArticleViewer.this.updatePaintSize();
                                this.invalidate();
                                break;
                            }
                            break;
                        }
                        else {
                            ++i;
                        }
                    }
                }
            }
            else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                if (!this.moving) {
                    int k = 0;
                    while (k < 5) {
                        final int sideSide3 = this.sideSide;
                        final int lineSize3 = this.lineSize;
                        final int gapSize3 = this.gapSize;
                        final int circleSize3 = this.circleSize;
                        final int n4 = sideSide3 + (lineSize3 + gapSize3 * 2 + circleSize3) * k + circleSize3 / 2;
                        if (x > n4 - AndroidUtilities.dp(15.0f) && x < n4 + AndroidUtilities.dp(15.0f)) {
                            if (ArticleViewer.this.selectedFontSize != k) {
                                ArticleViewer.this.selectedFontSize = k;
                                ArticleViewer.this.updatePaintSize();
                                this.invalidate();
                                break;
                            }
                            break;
                        }
                        else {
                            ++k;
                        }
                    }
                }
                else if (ArticleViewer.this.selectedFontSize != this.startMovingQuality) {
                    ArticleViewer.this.updatePaintSize();
                }
                this.startMoving = false;
                this.moving = false;
            }
            return true;
        }
    }
    
    private class TL_pageBlockDetailsBottom extends PageBlock
    {
        private TL_pageBlockDetails parent;
    }
    
    private class TL_pageBlockDetailsChild extends PageBlock
    {
        private PageBlock block;
        private PageBlock parent;
    }
    
    private class TL_pageBlockEmbedPostCaption extends TL_pageBlockEmbedPost
    {
        private TL_pageBlockEmbedPost parent;
    }
    
    private class TL_pageBlockListItem extends PageBlock
    {
        private PageBlock blockItem;
        private int index;
        private String num;
        private DrawingText numLayout;
        private TL_pageBlockListParent parent;
        private RichText textItem;
        
        private TL_pageBlockListItem() {
            this.index = Integer.MAX_VALUE;
        }
    }
    
    private class TL_pageBlockListParent extends PageBlock
    {
        private ArrayList<TL_pageBlockListItem> items;
        private int lastFontSize;
        private int lastMaxNumCalcWidth;
        private int level;
        private int maxNumWidth;
        private TL_pageBlockList pageBlockList;
        
        private TL_pageBlockListParent() {
            this.items = new ArrayList<TL_pageBlockListItem>();
        }
    }
    
    private class TL_pageBlockOrderedListItem extends PageBlock
    {
        private PageBlock blockItem;
        private int index;
        private String num;
        private DrawingText numLayout;
        private TL_pageBlockOrderedListParent parent;
        private RichText textItem;
        
        private TL_pageBlockOrderedListItem() {
            this.index = Integer.MAX_VALUE;
        }
    }
    
    private class TL_pageBlockOrderedListParent extends PageBlock
    {
        private ArrayList<TL_pageBlockOrderedListItem> items;
        private int lastFontSize;
        private int lastMaxNumCalcWidth;
        private int level;
        private int maxNumWidth;
        private TL_pageBlockOrderedList pageBlockOrderedList;
        
        private TL_pageBlockOrderedListParent() {
            this.items = new ArrayList<TL_pageBlockOrderedListItem>();
        }
    }
    
    private class TL_pageBlockRelatedArticlesChild extends PageBlock
    {
        private int num;
        private TL_pageBlockRelatedArticles parent;
    }
    
    private class TL_pageBlockRelatedArticlesShadow extends PageBlock
    {
        private TL_pageBlockRelatedArticles parent;
    }
    
    private class WebpageAdapter extends SelectionAdapter
    {
        private HashMap<String, Integer> anchors;
        private HashMap<String, Integer> anchorsOffset;
        private HashMap<String, TLRPC.TL_textAnchor> anchorsParent;
        private HashMap<TLRPC.TL_pageBlockAudio, MessageObject> audioBlocks;
        private ArrayList<MessageObject> audioMessages;
        private ArrayList<TLRPC.PageBlock> blocks;
        private Context context;
        private ArrayList<TLRPC.PageBlock> localBlocks;
        private ArrayList<TLRPC.PageBlock> photoBlocks;
        
        public WebpageAdapter(final Context context) {
            this.localBlocks = new ArrayList<TLRPC.PageBlock>();
            this.blocks = new ArrayList<TLRPC.PageBlock>();
            this.photoBlocks = new ArrayList<TLRPC.PageBlock>();
            this.anchors = new HashMap<String, Integer>();
            this.anchorsOffset = new HashMap<String, Integer>();
            this.anchorsParent = new HashMap<String, TLRPC.TL_textAnchor>();
            this.audioBlocks = new HashMap<TLRPC.TL_pageBlockAudio, MessageObject>();
            this.audioMessages = new ArrayList<MessageObject>();
            this.context = context;
        }
        
        private void addAllMediaFromBlock(TLRPC.PageBlock pageBlock) {
            if (pageBlock instanceof TLRPC.TL_pageBlockPhoto) {
                final TLRPC.TL_pageBlockPhoto tl_pageBlockPhoto = (TLRPC.TL_pageBlockPhoto)pageBlock;
                final TLRPC.Photo access$12800 = ArticleViewer.this.getPhotoWithId(tl_pageBlockPhoto.photo_id);
                if (access$12800 != null) {
                    tl_pageBlockPhoto.thumb = FileLoader.getClosestPhotoSizeWithSize(access$12800.sizes, 56, true);
                    tl_pageBlockPhoto.thumbObject = access$12800;
                    this.photoBlocks.add(pageBlock);
                }
            }
            else if (pageBlock instanceof TLRPC.TL_pageBlockVideo && ArticleViewer.this.isVideoBlock(pageBlock)) {
                final TLRPC.TL_pageBlockVideo tl_pageBlockVideo = (TLRPC.TL_pageBlockVideo)pageBlock;
                final TLRPC.Document access$12801 = ArticleViewer.this.getDocumentWithId(tl_pageBlockVideo.video_id);
                if (access$12801 != null) {
                    tl_pageBlockVideo.thumb = FileLoader.getClosestPhotoSizeWithSize(access$12801.thumbs, 56, true);
                    tl_pageBlockVideo.thumbObject = access$12801;
                    this.photoBlocks.add(pageBlock);
                }
            }
            else {
                final boolean b = pageBlock instanceof TLRPC.TL_pageBlockSlideshow;
                final int n = 0;
                int i = 0;
                if (b) {
                    for (TLRPC.TL_pageBlockSlideshow tl_pageBlockSlideshow = (TLRPC.TL_pageBlockSlideshow)pageBlock; i < tl_pageBlockSlideshow.items.size(); ++i) {
                        pageBlock = tl_pageBlockSlideshow.items.get(i);
                        pageBlock.groupId = ArticleViewer.this.lastBlockNum;
                        this.addAllMediaFromBlock(pageBlock);
                    }
                    ArticleViewer.this.lastBlockNum++;
                }
                else if (pageBlock instanceof TLRPC.TL_pageBlockCollage) {
                    final TLRPC.TL_pageBlockCollage tl_pageBlockCollage = (TLRPC.TL_pageBlockCollage)pageBlock;
                    for (int size = tl_pageBlockCollage.items.size(), j = n; j < size; ++j) {
                        final TLRPC.PageBlock pageBlock2 = tl_pageBlockCollage.items.get(j);
                        pageBlock2.groupId = ArticleViewer.this.lastBlockNum;
                        this.addAllMediaFromBlock(pageBlock2);
                    }
                    ArticleViewer.this.lastBlockNum++;
                }
                else if (pageBlock instanceof TLRPC.TL_pageBlockCover) {
                    this.addAllMediaFromBlock(((TLRPC.TL_pageBlockCover)pageBlock).cover);
                }
            }
        }
        
        private void addBlock(final TLRPC.PageBlock e, int i, int size, final int n) {
            int n2 = (e instanceof TL_pageBlockDetailsChild) ? 1 : 0;
            TLRPC.PageBlock access$5600;
            if (n2 != 0) {
                access$5600 = ((TL_pageBlockDetailsChild)e).block;
            }
            else {
                access$5600 = e;
            }
            if (!(access$5600 instanceof TLRPC.TL_pageBlockList) && !(access$5600 instanceof TLRPC.TL_pageBlockOrderedList)) {
                this.setRichTextParents(access$5600);
                this.addAllMediaFromBlock(access$5600);
            }
            final TLRPC.PageBlock access$5601 = ArticleViewer.this.getLastNonListPageBlock(access$5600);
            if (access$5601 instanceof TLRPC.TL_pageBlockUnsupported) {
                return;
            }
            if (access$5601 instanceof TLRPC.TL_pageBlockAnchor) {
                this.anchors.put(((TLRPC.TL_pageBlockAnchor)access$5601).name.toLowerCase(), this.blocks.size());
                return;
            }
            final boolean b = access$5601 instanceof TLRPC.TL_pageBlockList;
            if (!b && !(access$5601 instanceof TLRPC.TL_pageBlockOrderedList)) {
                this.blocks.add(e);
            }
            final boolean b2 = access$5601 instanceof TLRPC.TL_pageBlockAudio;
            final int n3 = 0;
            final int n4 = 0;
            final int n5 = 0;
            if (b2) {
                final TLRPC.TL_pageBlockAudio key = (TLRPC.TL_pageBlockAudio)access$5601;
                final TLRPC.TL_message tl_message = new TLRPC.TL_message();
                tl_message.out = true;
                i = -Long.valueOf(key.audio_id).hashCode();
                access$5601.mid = i;
                tl_message.id = i;
                tl_message.to_id = new TLRPC.TL_peerUser();
                final TLRPC.Peer to_id = tl_message.to_id;
                i = UserConfig.getInstance(ArticleViewer.this.currentAccount).getClientUserId();
                tl_message.from_id = i;
                to_id.user_id = i;
                tl_message.date = (int)(System.currentTimeMillis() / 1000L);
                tl_message.message = "";
                tl_message.media = new TLRPC.TL_messageMediaDocument();
                tl_message.media.webpage = ArticleViewer.this.currentPage;
                final TLRPC.MessageMedia media = tl_message.media;
                media.flags |= 0x3;
                media.document = ArticleViewer.this.getDocumentWithId(key.audio_id);
                tl_message.flags |= 0x300;
                final MessageObject messageObject = new MessageObject(UserConfig.selectedAccount, tl_message, false);
                this.audioMessages.add(messageObject);
                this.audioBlocks.put(key, messageObject);
                return;
            }
            String s2;
            String text3;
            TLRPC.TL_pageBlockOrderedList list3;
            TL_pageBlockOrderedListParent tl_pageBlockOrderedListParent;
            int size5;
            int index;
            if (access$5601 instanceof TLRPC.TL_pageBlockEmbedPost) {
                final TLRPC.TL_pageBlockEmbedPost tl_pageBlockEmbedPost = (TLRPC.TL_pageBlockEmbedPost)access$5601;
                if (tl_pageBlockEmbedPost.blocks.isEmpty()) {
                    return;
                }
                access$5601.level = -1;
                TLRPC.PageBlock e2;
                for (i = n5; i < tl_pageBlockEmbedPost.blocks.size(); ++i) {
                    e2 = tl_pageBlockEmbedPost.blocks.get(i);
                    if (!(e2 instanceof TLRPC.TL_pageBlockUnsupported)) {
                        if (e2 instanceof TLRPC.TL_pageBlockAnchor) {
                            this.anchors.put(((TLRPC.TL_pageBlockAnchor)e2).name.toLowerCase(), this.blocks.size());
                        }
                        else {
                            e2.level = 1;
                            if (i == tl_pageBlockEmbedPost.blocks.size() - 1) {
                                e2.bottom = true;
                            }
                            this.blocks.add(e2);
                            this.addAllMediaFromBlock(e2);
                        }
                    }
                }
                if (!TextUtils.isEmpty(ArticleViewer.getPlainText(tl_pageBlockEmbedPost.caption.text)) || !TextUtils.isEmpty(ArticleViewer.getPlainText(tl_pageBlockEmbedPost.caption.credit))) {
                    final TL_pageBlockEmbedPostCaption e3 = new TL_pageBlockEmbedPostCaption();
                    e3.parent = tl_pageBlockEmbedPost;
                    e3.caption = tl_pageBlockEmbedPost.caption;
                    this.blocks.add(e3);
                }
                return;
            }
            else if (access$5601 instanceof TLRPC.TL_pageBlockRelatedArticles) {
                final TLRPC.TL_pageBlockRelatedArticles tl_pageBlockRelatedArticles = (TLRPC.TL_pageBlockRelatedArticles)access$5601;
                final TL_pageBlockRelatedArticlesShadow element = new TL_pageBlockRelatedArticlesShadow();
                element.parent = tl_pageBlockRelatedArticles;
                final ArrayList<TLRPC.PageBlock> blocks = this.blocks;
                blocks.add(blocks.size() - 1, element);
                TL_pageBlockRelatedArticlesChild e4;
                for (size = tl_pageBlockRelatedArticles.articles.size(), i = n3; i < size; ++i) {
                    e4 = new TL_pageBlockRelatedArticlesChild();
                    e4.parent = tl_pageBlockRelatedArticles;
                    e4.num = i;
                    this.blocks.add(e4);
                }
                if (n == 0) {
                    final TL_pageBlockRelatedArticlesShadow e5 = new TL_pageBlockRelatedArticlesShadow();
                    e5.parent = tl_pageBlockRelatedArticles;
                    this.blocks.add(e5);
                }
                return;
            }
            else {
                if (access$5601 instanceof TLRPC.TL_pageBlockDetails) {
                    final TLRPC.TL_pageBlockDetails tl_pageBlockDetails = (TLRPC.TL_pageBlockDetails)access$5601;
                    for (int size2 = tl_pageBlockDetails.blocks.size(), j = n4; j < size2; ++j) {
                        final TL_pageBlockDetailsChild tl_pageBlockDetailsChild = new TL_pageBlockDetailsChild();
                        tl_pageBlockDetailsChild.parent = e;
                        tl_pageBlockDetailsChild.block = tl_pageBlockDetails.blocks.get(j);
                        this.addBlock(ArticleViewer.this.wrapInTableBlock(e, tl_pageBlockDetailsChild), i + 1, size, n);
                    }
                    return;
                }
                final String s = " ";
                s2 = ".%d";
                if (b) {
                    final TLRPC.TL_pageBlockList list = (TLRPC.TL_pageBlockList)access$5601;
                    final TL_pageBlockListParent tl_pageBlockListParent = new TL_pageBlockListParent();
                    tl_pageBlockListParent.pageBlockList = list;
                    tl_pageBlockListParent.level = size;
                    final int size3 = list.items.size();
                    int k = 0;
                    final String text = s;
                    final TLRPC.TL_pageBlockList list2 = list;
                    while (k < size3) {
                        final TLRPC.PageListItem pageListItem = list2.items.get(k);
                        final TL_pageBlockListItem e6 = new TL_pageBlockListItem();
                        e6.index = k;
                        e6.parent = tl_pageBlockListParent;
                        if (list2.ordered) {
                            if (ArticleViewer.this.isRtl) {
                                e6.num = String.format(".%d", k + 1);
                            }
                            else {
                                e6.num = String.format("%d.", k + 1);
                            }
                        }
                        else {
                            e6.num = "\u2022";
                        }
                        tl_pageBlockListParent.items.add(e6);
                        TLRPC.PageListItem pageListItem2;
                        if (pageListItem instanceof TLRPC.TL_pageListItemText) {
                            e6.textItem = ((TLRPC.TL_pageListItemText)pageListItem).text;
                            pageListItem2 = pageListItem;
                        }
                        else {
                            pageListItem2 = pageListItem;
                            if (pageListItem instanceof TLRPC.TL_pageListItemBlocks) {
                                final TLRPC.TL_pageListItemBlocks tl_pageListItemBlocks = (TLRPC.TL_pageListItemBlocks)pageListItem;
                                if (!tl_pageListItemBlocks.blocks.isEmpty()) {
                                    e6.blockItem = tl_pageListItemBlocks.blocks.get(0);
                                    pageListItem2 = pageListItem;
                                }
                                else {
                                    pageListItem2 = new TLRPC.TL_pageListItemText();
                                    final TLRPC.TL_textPlain text2 = new TLRPC.TL_textPlain();
                                    text2.text = text;
                                    ((TLRPC.TL_pageListItemText)pageListItem2).text = text2;
                                }
                            }
                        }
                        if (n2 != 0) {
                            final TL_pageBlockDetailsChild tl_pageBlockDetailsChild2 = (TL_pageBlockDetailsChild)e;
                            final TL_pageBlockDetailsChild tl_pageBlockDetailsChild3 = new TL_pageBlockDetailsChild();
                            tl_pageBlockDetailsChild3.parent = tl_pageBlockDetailsChild2.parent;
                            tl_pageBlockDetailsChild3.block = e6;
                            this.addBlock(tl_pageBlockDetailsChild3, i, size + 1, n);
                        }
                        else {
                            TLRPC.PageBlock access$5602 = e6;
                            if (k == 0) {
                                access$5602 = ArticleViewer.this.fixListBlock(e, e6);
                            }
                            this.addBlock(access$5602, i, size + 1, n);
                        }
                        if (pageListItem2 instanceof TLRPC.TL_pageListItemBlocks) {
                            final TLRPC.TL_pageListItemBlocks tl_pageListItemBlocks2 = (TLRPC.TL_pageListItemBlocks)pageListItem2;
                            for (int size4 = tl_pageListItemBlocks2.blocks.size(), l = 1; l < size4; ++l) {
                                final TL_pageBlockListItem e7 = new TL_pageBlockListItem();
                                e7.blockItem = tl_pageListItemBlocks2.blocks.get(l);
                                e7.parent = tl_pageBlockListParent;
                                if (n2 != 0) {
                                    final TL_pageBlockDetailsChild tl_pageBlockDetailsChild4 = (TL_pageBlockDetailsChild)e;
                                    final TL_pageBlockDetailsChild tl_pageBlockDetailsChild5 = new TL_pageBlockDetailsChild();
                                    tl_pageBlockDetailsChild5.parent = tl_pageBlockDetailsChild4.parent;
                                    tl_pageBlockDetailsChild5.block = e7;
                                    this.addBlock(tl_pageBlockDetailsChild5, i, size + 1, n);
                                }
                                else {
                                    this.addBlock(e7, i, size + 1, n);
                                }
                                tl_pageBlockListParent.items.add(e7);
                            }
                        }
                        ++k;
                    }
                    return;
                }
                text3 = " ";
                if (!(access$5601 instanceof TLRPC.TL_pageBlockOrderedList)) {
                    return;
                }
                list3 = (TLRPC.TL_pageBlockOrderedList)access$5601;
                tl_pageBlockOrderedListParent = new TL_pageBlockOrderedListParent();
                tl_pageBlockOrderedListParent.pageBlockOrderedList = list3;
                tl_pageBlockOrderedListParent.level = size;
                size5 = list3.items.size();
                index = 0;
            }
        Label_2190_Outer:
            while (true) {
                if (index >= size5) {
                    return;
                }
                TLRPC.PageListOrderedItem pageListOrderedItem = list3.items.get(index);
                final TL_pageBlockOrderedListItem e8 = new TL_pageBlockOrderedListItem();
                e8.index = index;
                e8.parent = tl_pageBlockOrderedListParent;
                tl_pageBlockOrderedListParent.items.add(e8);
                if (pageListOrderedItem instanceof TLRPC.TL_pageListOrderedItemText) {
                    final TLRPC.TL_pageListOrderedItemText tl_pageListOrderedItemText = (TLRPC.TL_pageListOrderedItemText)pageListOrderedItem;
                    e8.textItem = tl_pageListOrderedItemText.text;
                    if (TextUtils.isEmpty((CharSequence)tl_pageListOrderedItemText.num)) {
                        if (ArticleViewer.this.isRtl) {
                            e8.num = String.format(s2, index + 1);
                        }
                        else {
                            e8.num = String.format("%d.", index + 1);
                        }
                    }
                    else if (ArticleViewer.this.isRtl) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(".");
                        sb.append(tl_pageListOrderedItemText.num);
                        e8.num = sb.toString();
                    }
                    else {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(tl_pageListOrderedItemText.num);
                        sb2.append(".");
                        e8.num = sb2.toString();
                    }
                }
                else if (pageListOrderedItem instanceof TLRPC.TL_pageListOrderedItemBlocks) {
                    final TLRPC.TL_pageListOrderedItemBlocks tl_pageListOrderedItemBlocks = (TLRPC.TL_pageListOrderedItemBlocks)pageListOrderedItem;
                    if (!tl_pageListOrderedItemBlocks.blocks.isEmpty()) {
                        e8.blockItem = tl_pageListOrderedItemBlocks.blocks.get(0);
                    }
                    else {
                        pageListOrderedItem = new TLRPC.TL_pageListOrderedItemText();
                        final TLRPC.TL_textPlain text4 = new TLRPC.TL_textPlain();
                        text4.text = text3;
                        ((TLRPC.TL_pageListOrderedItemText)pageListOrderedItem).text = text4;
                    }
                    if (TextUtils.isEmpty((CharSequence)tl_pageListOrderedItemBlocks.num)) {
                        if (ArticleViewer.this.isRtl) {
                            e8.num = String.format(s2, index + 1);
                        }
                        else {
                            e8.num = String.format("%d.", index + 1);
                        }
                    }
                    else if (ArticleViewer.this.isRtl) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append(".");
                        sb3.append(tl_pageListOrderedItemBlocks.num);
                        e8.num = sb3.toString();
                    }
                    else {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append(tl_pageListOrderedItemBlocks.num);
                        sb4.append(".");
                        e8.num = sb4.toString();
                    }
                }
                if (n2 != 0) {
                    final TL_pageBlockDetailsChild tl_pageBlockDetailsChild6 = (TL_pageBlockDetailsChild)e;
                    final TL_pageBlockDetailsChild tl_pageBlockDetailsChild7 = new TL_pageBlockDetailsChild();
                    tl_pageBlockDetailsChild7.parent = tl_pageBlockDetailsChild6.parent;
                    tl_pageBlockDetailsChild7.block = e8;
                    this.addBlock(tl_pageBlockDetailsChild7, i, size + 1, n);
                }
                else {
                    TLRPC.PageBlock access$5603 = e8;
                    if (index == 0) {
                        access$5603 = ArticleViewer.this.fixListBlock(e, e8);
                    }
                    this.addBlock(access$5603, i, size + 1, n);
                }
                int n6 = n2;
                Label_2330: {
                    if (!(pageListOrderedItem instanceof TLRPC.TL_pageListOrderedItemBlocks)) {
                        break Label_2330;
                    }
                    final TLRPC.TL_pageListOrderedItemBlocks tl_pageListOrderedItemBlocks2 = (TLRPC.TL_pageListOrderedItemBlocks)pageListOrderedItem;
                    final int size6 = tl_pageListOrderedItemBlocks2.blocks.size();
                    int index2 = 1;
                    while (true) {
                        n6 = n2;
                        if (index2 >= size6) {
                            break Label_2330;
                        }
                        final TL_pageBlockOrderedListItem e9 = new TL_pageBlockOrderedListItem();
                        e9.blockItem = tl_pageListOrderedItemBlocks2.blocks.get(index2);
                        e9.parent = tl_pageBlockOrderedListParent;
                        Label_2313: {
                            if (n2 != 0) {
                                final TL_pageBlockDetailsChild tl_pageBlockDetailsChild8 = (TL_pageBlockDetailsChild)e;
                                final TL_pageBlockDetailsChild tl_pageBlockDetailsChild9 = new TL_pageBlockDetailsChild();
                                tl_pageBlockDetailsChild9.parent = tl_pageBlockDetailsChild8.parent;
                                tl_pageBlockDetailsChild9.block = e9;
                                this.addBlock(tl_pageBlockDetailsChild9, i, size + 1, n);
                                break Label_2313;
                            }
                            try {
                                this.addBlock(e9, i, size + 1, n);
                                tl_pageBlockOrderedListParent.items.add(e9);
                                ++index2;
                                continue;
                                ++index;
                                n2 = n6;
                                continue Label_2190_Outer;
                            }
                            catch (Throwable t) {
                                throw t;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        
        private void bindBlockToHolder(final int n, final ViewHolder viewHolder, final TLRPC.PageBlock pageBlock, final int n2, final int n3) {
            TLRPC.PageBlock obj;
            if (pageBlock instanceof TLRPC.TL_pageBlockCover) {
                obj = ((TLRPC.TL_pageBlockCover)pageBlock).cover;
            }
            else if (pageBlock instanceof TL_pageBlockDetailsChild) {
                obj = ((TL_pageBlockDetailsChild)pageBlock).block;
            }
            else {
                obj = pageBlock;
            }
            if (n != 100) {
                final boolean b = false;
                final boolean b2 = false;
                final boolean b3 = false;
                boolean b4 = false;
                switch (n) {
                    case 27: {
                        final BlockDetailsBottomCell blockDetailsBottomCell = (BlockDetailsBottomCell)viewHolder.itemView;
                        break;
                    }
                    case 26: {
                        ((BlockRelatedArticlesHeaderCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockRelatedArticles)obj);
                        break;
                    }
                    case 25: {
                        ((BlockTableCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockTable)obj);
                        break;
                    }
                    case 24: {
                        ((BlockDetailsCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockDetails)obj);
                        break;
                    }
                    case 23: {
                        ((BlockRelatedArticlesCell)viewHolder.itemView).setBlock((TL_pageBlockRelatedArticlesChild)obj);
                        break;
                    }
                    case 22: {
                        final BlockMapCell blockMapCell = (BlockMapCell)viewHolder.itemView;
                        final TLRPC.TL_pageBlockMap tl_pageBlockMap = (TLRPC.TL_pageBlockMap)obj;
                        final boolean b5 = n2 == 0;
                        if (n2 == n3 - 1) {
                            b4 = true;
                        }
                        blockMapCell.setBlock(tl_pageBlockMap, b5, b4);
                        break;
                    }
                    case 21: {
                        ((BlockOrderedListItemCell)viewHolder.itemView).setBlock((TL_pageBlockOrderedListItem)obj);
                        break;
                    }
                    case 20: {
                        ((BlockKickerCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockKicker)obj);
                        break;
                    }
                    case 19: {
                        final BlockAudioCell blockAudioCell = (BlockAudioCell)viewHolder.itemView;
                        final TLRPC.TL_pageBlockAudio tl_pageBlockAudio = (TLRPC.TL_pageBlockAudio)obj;
                        final boolean b6 = n2 == 0;
                        boolean b7 = b;
                        if (n2 == n3 - 1) {
                            b7 = true;
                        }
                        blockAudioCell.setBlock(tl_pageBlockAudio, b6, b7);
                        break;
                    }
                    case 18: {
                        ((BlockChannelCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockChannel)obj);
                        break;
                    }
                    case 17: {
                        ((BlockCollageCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockCollage)obj);
                        break;
                    }
                    case 16: {
                        ((BlockEmbedPostCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockEmbedPost)obj);
                        break;
                    }
                    case 15: {
                        ((BlockSubheaderCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockSubheader)obj);
                        break;
                    }
                    case 14: {
                        ((BlockPreformattedCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockPreformatted)obj);
                        break;
                    }
                    case 13: {
                        ((BlockFooterCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockFooter)obj);
                        break;
                    }
                    case 12: {
                        ((BlockListItemCell)viewHolder.itemView).setBlock((TL_pageBlockListItem)obj);
                        break;
                    }
                    case 11: {
                        ((BlockTitleCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockTitle)obj);
                        break;
                    }
                    case 10: {
                        ((BlockAuthorDateCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockAuthorDate)obj);
                        break;
                    }
                    case 9: {
                        final BlockPhotoCell blockPhotoCell = (BlockPhotoCell)viewHolder.itemView;
                        final TLRPC.TL_pageBlockPhoto tl_pageBlockPhoto = (TLRPC.TL_pageBlockPhoto)obj;
                        final boolean b8 = n2 == 0;
                        boolean b9 = b2;
                        if (n2 == n3 - 1) {
                            b9 = true;
                        }
                        blockPhotoCell.setBlock(tl_pageBlockPhoto, b8, b9);
                        blockPhotoCell.setParentBlock(pageBlock);
                        break;
                    }
                    case 8: {
                        ((BlockSlideshowCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockSlideshow)obj);
                        break;
                    }
                    case 7: {
                        ((BlockBlockquoteCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockBlockquote)obj);
                        break;
                    }
                    case 6: {
                        ((BlockPullquoteCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockPullquote)obj);
                        break;
                    }
                    case 5: {
                        final BlockVideoCell blockVideoCell = (BlockVideoCell)viewHolder.itemView;
                        final TLRPC.TL_pageBlockVideo tl_pageBlockVideo = (TLRPC.TL_pageBlockVideo)obj;
                        final boolean b10 = n2 == 0;
                        boolean b11 = b3;
                        if (n2 == n3 - 1) {
                            b11 = true;
                        }
                        blockVideoCell.setBlock(tl_pageBlockVideo, b10, b11);
                        blockVideoCell.setParentBlock(pageBlock);
                        break;
                    }
                    case 4: {
                        ((BlockSubtitleCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockSubtitle)obj);
                        break;
                    }
                    case 3: {
                        ((BlockEmbedCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockEmbed)obj);
                        break;
                    }
                    case 2: {
                        final BlockDividerCell blockDividerCell = (BlockDividerCell)viewHolder.itemView;
                        break;
                    }
                    case 1: {
                        ((BlockHeaderCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockHeader)obj);
                        break;
                    }
                    case 0: {
                        ((BlockParagraphCell)viewHolder.itemView).setBlock((TLRPC.TL_pageBlockParagraph)obj);
                        break;
                    }
                }
            }
            else {
                final TextView textView = (TextView)viewHolder.itemView;
                final StringBuilder sb = new StringBuilder();
                sb.append("unsupported block ");
                sb.append(obj);
                textView.setText((CharSequence)sb.toString());
            }
        }
        
        private void cleanup() {
            this.blocks.clear();
            this.photoBlocks.clear();
            this.audioBlocks.clear();
            this.audioMessages.clear();
            this.anchors.clear();
            this.anchorsParent.clear();
            this.anchorsOffset.clear();
            this.notifyDataSetChanged();
        }
        
        private int getTypeForBlock(final TLRPC.PageBlock pageBlock) {
            if (pageBlock instanceof TLRPC.TL_pageBlockParagraph) {
                return 0;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockHeader) {
                return 1;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockDivider) {
                return 2;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockEmbed) {
                return 3;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockSubtitle) {
                return 4;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockVideo) {
                return 5;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockPullquote) {
                return 6;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockBlockquote) {
                return 7;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockSlideshow) {
                return 8;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockPhoto) {
                return 9;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockAuthorDate) {
                return 10;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockTitle) {
                return 11;
            }
            if (pageBlock instanceof TL_pageBlockListItem) {
                return 12;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockFooter) {
                return 13;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockPreformatted) {
                return 14;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockSubheader) {
                return 15;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockEmbedPost) {
                return 16;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockCollage) {
                return 17;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockChannel) {
                return 18;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockAudio) {
                return 19;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockKicker) {
                return 20;
            }
            if (pageBlock instanceof TL_pageBlockOrderedListItem) {
                return 21;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockMap) {
                return 22;
            }
            if (pageBlock instanceof TL_pageBlockRelatedArticlesChild) {
                return 23;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockDetails) {
                return 24;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockTable) {
                return 25;
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockRelatedArticles) {
                return 26;
            }
            if (pageBlock instanceof TL_pageBlockDetailsBottom) {
                return 27;
            }
            if (pageBlock instanceof TL_pageBlockRelatedArticlesShadow) {
                return 28;
            }
            if (pageBlock instanceof TL_pageBlockDetailsChild) {
                return this.getTypeForBlock(((TL_pageBlockDetailsChild)pageBlock).block);
            }
            if (pageBlock instanceof TLRPC.TL_pageBlockCover) {
                return this.getTypeForBlock(((TLRPC.TL_pageBlockCover)pageBlock).cover);
            }
            return 100;
        }
        
        private boolean isBlockOpened(TL_pageBlockDetailsChild tl_pageBlockDetailsChild) {
            final TLRPC.PageBlock access$10600 = ArticleViewer.this.getLastNonListPageBlock(tl_pageBlockDetailsChild.parent);
            if (access$10600 instanceof TLRPC.TL_pageBlockDetails) {
                return ((TLRPC.TL_pageBlockDetails)access$10600).open;
            }
            if (access$10600 instanceof TL_pageBlockDetailsChild) {
                tl_pageBlockDetailsChild = (TL_pageBlockDetailsChild)access$10600;
                final TLRPC.PageBlock access$10601 = ArticleViewer.this.getLastNonListPageBlock(tl_pageBlockDetailsChild.block);
                return (!(access$10601 instanceof TLRPC.TL_pageBlockDetails) || ((TLRPC.TL_pageBlockDetails)access$10601).open) && this.isBlockOpened(tl_pageBlockDetailsChild);
            }
            return false;
        }
        
        private void setRichTextParents(TLRPC.PageBlock cover) {
            if (cover instanceof TLRPC.TL_pageBlockEmbedPost) {
                final TLRPC.TL_pageBlockEmbedPost tl_pageBlockEmbedPost = (TLRPC.TL_pageBlockEmbedPost)cover;
                this.setRichTextParents(null, tl_pageBlockEmbedPost.caption.text);
                this.setRichTextParents(null, tl_pageBlockEmbedPost.caption.credit);
                return;
            }
            if (cover instanceof TLRPC.TL_pageBlockParagraph) {
                this.setRichTextParents(null, ((TLRPC.TL_pageBlockParagraph)cover).text);
                return;
            }
            if (cover instanceof TLRPC.TL_pageBlockKicker) {
                this.setRichTextParents(null, ((TLRPC.TL_pageBlockKicker)cover).text);
                return;
            }
            if (cover instanceof TLRPC.TL_pageBlockFooter) {
                this.setRichTextParents(null, ((TLRPC.TL_pageBlockFooter)cover).text);
                return;
            }
            if (cover instanceof TLRPC.TL_pageBlockHeader) {
                this.setRichTextParents(null, ((TLRPC.TL_pageBlockHeader)cover).text);
                return;
            }
            if (cover instanceof TLRPC.TL_pageBlockPreformatted) {
                this.setRichTextParents(null, ((TLRPC.TL_pageBlockPreformatted)cover).text);
                return;
            }
            if (cover instanceof TLRPC.TL_pageBlockSubheader) {
                this.setRichTextParents(null, ((TLRPC.TL_pageBlockSubheader)cover).text);
                return;
            }
            final boolean b = cover instanceof TLRPC.TL_pageBlockSlideshow;
            final int n = 0;
            final int n2 = 0;
            int i = 0;
            if (b) {
                final TLRPC.TL_pageBlockSlideshow tl_pageBlockSlideshow = (TLRPC.TL_pageBlockSlideshow)cover;
                this.setRichTextParents(null, tl_pageBlockSlideshow.caption.text);
                this.setRichTextParents(null, tl_pageBlockSlideshow.caption.credit);
                while (i < tl_pageBlockSlideshow.items.size()) {
                    this.setRichTextParents(tl_pageBlockSlideshow.items.get(i));
                    ++i;
                }
                return;
            }
            if (cover instanceof TLRPC.TL_pageBlockPhoto) {
                final TLRPC.TL_pageBlockPhoto tl_pageBlockPhoto = (TLRPC.TL_pageBlockPhoto)cover;
                this.setRichTextParents(null, tl_pageBlockPhoto.caption.text);
                this.setRichTextParents(null, tl_pageBlockPhoto.caption.credit);
                return;
            }
            Label_0883: {
                if (cover instanceof TL_pageBlockListItem) {
                    final TL_pageBlockListItem tl_pageBlockListItem = (TL_pageBlockListItem)cover;
                    if (tl_pageBlockListItem.textItem != null) {
                        this.setRichTextParents(null, tl_pageBlockListItem.textItem);
                        return;
                    }
                    if (tl_pageBlockListItem.blockItem != null) {
                        this.setRichTextParents(tl_pageBlockListItem.blockItem);
                    }
                    return;
                }
                else if (cover instanceof TL_pageBlockOrderedListItem) {
                    final TL_pageBlockOrderedListItem tl_pageBlockOrderedListItem = (TL_pageBlockOrderedListItem)cover;
                    if (tl_pageBlockOrderedListItem.textItem != null) {
                        this.setRichTextParents(null, tl_pageBlockOrderedListItem.textItem);
                        return;
                    }
                    if (tl_pageBlockOrderedListItem.blockItem != null) {
                        this.setRichTextParents(tl_pageBlockOrderedListItem.blockItem);
                    }
                    return;
                }
                else {
                    if (cover instanceof TLRPC.TL_pageBlockCollage) {
                        final TLRPC.TL_pageBlockCollage tl_pageBlockCollage = (TLRPC.TL_pageBlockCollage)cover;
                        this.setRichTextParents(null, tl_pageBlockCollage.caption.text);
                        this.setRichTextParents(null, tl_pageBlockCollage.caption.credit);
                        for (int size = tl_pageBlockCollage.items.size(), j = n; j < size; ++j) {
                            this.setRichTextParents(tl_pageBlockCollage.items.get(j));
                        }
                        return;
                    }
                    if (cover instanceof TLRPC.TL_pageBlockEmbed) {
                        final TLRPC.TL_pageBlockEmbed tl_pageBlockEmbed = (TLRPC.TL_pageBlockEmbed)cover;
                        this.setRichTextParents(null, tl_pageBlockEmbed.caption.text);
                        this.setRichTextParents(null, tl_pageBlockEmbed.caption.credit);
                        return;
                    }
                    if (cover instanceof TLRPC.TL_pageBlockSubtitle) {
                        this.setRichTextParents(null, ((TLRPC.TL_pageBlockSubtitle)cover).text);
                        return;
                    }
                    if (cover instanceof TLRPC.TL_pageBlockBlockquote) {
                        final TLRPC.TL_pageBlockBlockquote tl_pageBlockBlockquote = (TLRPC.TL_pageBlockBlockquote)cover;
                        this.setRichTextParents(null, tl_pageBlockBlockquote.text);
                        this.setRichTextParents(null, tl_pageBlockBlockquote.caption);
                        return;
                    }
                    if (cover instanceof TLRPC.TL_pageBlockDetails) {
                        final TLRPC.TL_pageBlockDetails tl_pageBlockDetails = (TLRPC.TL_pageBlockDetails)cover;
                        this.setRichTextParents(null, tl_pageBlockDetails.title);
                        for (int size2 = tl_pageBlockDetails.blocks.size(), k = n2; k < size2; ++k) {
                            this.setRichTextParents(tl_pageBlockDetails.blocks.get(k));
                        }
                        return;
                    }
                    if (cover instanceof TLRPC.TL_pageBlockVideo) {
                        final TLRPC.TL_pageBlockVideo tl_pageBlockVideo = (TLRPC.TL_pageBlockVideo)cover;
                        this.setRichTextParents(null, tl_pageBlockVideo.caption.text);
                        this.setRichTextParents(null, tl_pageBlockVideo.caption.credit);
                        return;
                    }
                    if (cover instanceof TLRPC.TL_pageBlockPullquote) {
                        final TLRPC.TL_pageBlockPullquote tl_pageBlockPullquote = (TLRPC.TL_pageBlockPullquote)cover;
                        this.setRichTextParents(null, tl_pageBlockPullquote.text);
                        this.setRichTextParents(null, tl_pageBlockPullquote.caption);
                        return;
                    }
                    if (cover instanceof TLRPC.TL_pageBlockAudio) {
                        final TLRPC.TL_pageBlockAudio tl_pageBlockAudio = (TLRPC.TL_pageBlockAudio)cover;
                        this.setRichTextParents(null, tl_pageBlockAudio.caption.text);
                        this.setRichTextParents(null, tl_pageBlockAudio.caption.credit);
                        return;
                    }
                    if (cover instanceof TLRPC.TL_pageBlockTable) {
                        final TLRPC.TL_pageBlockTable tl_pageBlockTable = (TLRPC.TL_pageBlockTable)cover;
                        this.setRichTextParents(null, tl_pageBlockTable.title);
                        for (int size3 = tl_pageBlockTable.rows.size(), l = 0; l < size3; ++l) {
                            final TLRPC.TL_pageTableRow tl_pageTableRow = tl_pageBlockTable.rows.get(l);
                            for (int size4 = tl_pageTableRow.cells.size(), index = 0; index < size4; ++index) {
                                this.setRichTextParents(null, tl_pageTableRow.cells.get(index).text);
                            }
                        }
                        return;
                    }
                    if (cover instanceof TLRPC.TL_pageBlockTitle) {
                        this.setRichTextParents(null, ((TLRPC.TL_pageBlockTitle)cover).text);
                        return;
                    }
                    if (!(cover instanceof TLRPC.TL_pageBlockCover)) {
                        break Label_0883;
                    }
                }
                cover = ((TLRPC.TL_pageBlockCover)cover).cover;
                try {
                    this.setRichTextParents(cover);
                    Label_0963: {
                        return;
                    }
                    // iftrue(Label_0905:, !cover instanceof TLRPC.TL_pageBlockAuthorDate)
                    // iftrue(Label_0944:, !cover instanceof TLRPC.TL_pageBlockMap)
                    while (true) {
                    Block_34:
                        while (true) {
                            final TLRPC.TL_pageBlockMap tl_pageBlockMap = (TLRPC.TL_pageBlockMap)cover;
                            this.setRichTextParents(null, tl_pageBlockMap.caption.text);
                            this.setRichTextParents(null, tl_pageBlockMap.caption.credit);
                            return;
                            break Block_34;
                            this.setRichTextParents(null, ((TLRPC.TL_pageBlockRelatedArticles)cover).title);
                            return;
                            Label_0905:
                            continue;
                        }
                        this.setRichTextParents(null, ((TLRPC.TL_pageBlockAuthorDate)cover).author);
                        return;
                        Label_0944:
                        continue;
                    }
                }
                // iftrue(Label_0963:, !cover instanceof TLRPC.TL_pageBlockRelatedArticles)
                catch (Throwable t) {
                    throw t;
                }
            }
        }
        
        private void setRichTextParents(final TLRPC.RichText parentRichText, TLRPC.RichText text) {
            if (text == null) {
                return;
            }
            text.parentRichText = parentRichText;
            if (text instanceof TLRPC.TL_textFixed) {
                this.setRichTextParents(text, ((TLRPC.TL_textFixed)text).text);
                return;
            }
            if (text instanceof TLRPC.TL_textItalic) {
                this.setRichTextParents(text, ((TLRPC.TL_textItalic)text).text);
                return;
            }
            if (text instanceof TLRPC.TL_textBold) {
                this.setRichTextParents(text, ((TLRPC.TL_textBold)text).text);
                return;
            }
            if (text instanceof TLRPC.TL_textUnderline) {
                this.setRichTextParents(text, ((TLRPC.TL_textUnderline)text).text);
                return;
            }
            if (text instanceof TLRPC.TL_textStrike) {
                this.setRichTextParents(text, ((TLRPC.TL_textStrike)text).text);
                return;
            }
            if (text instanceof TLRPC.TL_textEmail) {
                this.setRichTextParents(text, ((TLRPC.TL_textEmail)text).text);
                return;
            }
            if (text instanceof TLRPC.TL_textPhone) {
                this.setRichTextParents(text, ((TLRPC.TL_textPhone)text).text);
                return;
            }
            if (text instanceof TLRPC.TL_textUrl) {
                this.setRichTextParents(text, ((TLRPC.TL_textUrl)text).text);
                return;
            }
            if (text instanceof TLRPC.TL_textConcat) {
                for (int size = text.texts.size(), i = 0; i < size; ++i) {
                    this.setRichTextParents(text, text.texts.get(i));
                }
                return;
            }
            if (text instanceof TLRPC.TL_textSubscript) {
                this.setRichTextParents(text, ((TLRPC.TL_textSubscript)text).text);
                return;
            }
            if (text instanceof TLRPC.TL_textSuperscript) {
                this.setRichTextParents(text, ((TLRPC.TL_textSuperscript)text).text);
                return;
            }
            if (text instanceof TLRPC.TL_textMarked) {
                this.setRichTextParents(text, ((TLRPC.TL_textMarked)text).text);
                return;
            }
            if (!(text instanceof TLRPC.TL_textAnchor)) {
                return;
            }
            final TLRPC.TL_textAnchor tl_textAnchor = (TLRPC.TL_textAnchor)text;
            final TLRPC.RichText text2 = tl_textAnchor.text;
            try {
                this.setRichTextParents(text, text2);
                final String lowerCase = tl_textAnchor.name.toLowerCase();
                this.anchors.put(lowerCase, this.blocks.size());
                text = tl_textAnchor.text;
                if (text instanceof TLRPC.TL_textPlain) {
                    if (!TextUtils.isEmpty((CharSequence)((TLRPC.TL_textPlain)text).text)) {
                        this.anchorsParent.put(lowerCase, tl_textAnchor);
                    }
                }
                else if (!(text instanceof TLRPC.TL_textEmpty)) {
                    this.anchorsParent.put(lowerCase, tl_textAnchor);
                }
                this.anchorsOffset.put(lowerCase, -1);
            }
            catch (Throwable t) {
                throw t;
            }
        }
        
        private void updateRows() {
            this.localBlocks.clear();
            for (int size = this.blocks.size(), i = 0; i < size; ++i) {
                final TLRPC.PageBlock e = this.blocks.get(i);
                final TLRPC.PageBlock access$10600 = ArticleViewer.this.getLastNonListPageBlock(e);
                if (!(access$10600 instanceof TL_pageBlockDetailsChild) || this.isBlockOpened((TL_pageBlockDetailsChild)access$10600)) {
                    this.localBlocks.add(e);
                }
            }
        }
        
        public TLRPC.PageBlock getItem(final int index) {
            return this.localBlocks.get(index);
        }
        
        @Override
        public int getItemCount() {
            int n;
            if (ArticleViewer.this.currentPage != null && ArticleViewer.this.currentPage.cached_page != null) {
                n = this.localBlocks.size() + 1;
            }
            else {
                n = 0;
            }
            return n;
        }
        
        @Override
        public int getItemViewType(final int index) {
            if (index == this.localBlocks.size()) {
                return 90;
            }
            return this.getTypeForBlock(this.localBlocks.get(index));
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 23 || itemViewType == 24;
        }
        
        @Override
        public void notifyDataSetChanged() {
            this.updateRows();
            super.notifyDataSetChanged();
        }
        
        @Override
        public void notifyItemChanged(final int n) {
            this.updateRows();
            super.notifyItemChanged(n);
        }
        
        @Override
        public void notifyItemChanged(final int n, final Object o) {
            this.updateRows();
            super.notifyItemChanged(n, o);
        }
        
        @Override
        public void notifyItemInserted(final int n) {
            this.updateRows();
            super.notifyItemInserted(n);
        }
        
        @Override
        public void notifyItemMoved(final int n, final int n2) {
            this.updateRows();
            super.notifyItemMoved(n, n2);
        }
        
        @Override
        public void notifyItemRangeChanged(final int n, final int n2) {
            this.updateRows();
            super.notifyItemRangeChanged(n, n2);
        }
        
        @Override
        public void notifyItemRangeChanged(final int n, final int n2, final Object o) {
            this.updateRows();
            super.notifyItemRangeChanged(n, n2, o);
        }
        
        @Override
        public void notifyItemRangeInserted(final int n, final int n2) {
            this.updateRows();
            super.notifyItemRangeInserted(n, n2);
        }
        
        @Override
        public void notifyItemRangeRemoved(final int n, final int n2) {
            this.updateRows();
            super.notifyItemRangeRemoved(n, n2);
        }
        
        @Override
        public void notifyItemRemoved(final int n) {
            this.updateRows();
            super.notifyItemRemoved(n);
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int access$13100) {
            if (access$13100 < this.localBlocks.size()) {
                this.bindBlockToHolder(viewHolder.getItemViewType(), viewHolder, this.localBlocks.get(access$13100), access$13100, this.localBlocks.size());
            }
            else if (viewHolder.getItemViewType() == 90) {
                final TextView textView = (TextView)((ViewGroup)viewHolder.itemView).getChildAt(0);
                access$13100 = ArticleViewer.this.getSelectedColor();
                if (access$13100 == 0) {
                    textView.setTextColor(-8879475);
                    textView.setBackgroundColor(-1183760);
                }
                else if (access$13100 == 1) {
                    textView.setTextColor(ArticleViewer.this.getGrayTextColor());
                    textView.setBackgroundColor(-1712440);
                }
                else if (access$13100 == 2) {
                    textView.setTextColor(ArticleViewer.this.getGrayTextColor());
                    textView.setBackgroundColor(-15000805);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o = null;
            if (n != 90) {
                switch (n) {
                    default: {
                        o = new TextView(this.context);
                        ((TextView)o).setBackgroundColor(-65536);
                        ((TextView)o).setTextColor(-16777216);
                        ((TextView)o).setTextSize(1, 20.0f);
                        break;
                    }
                    case 28: {
                        o = new BlockRelatedArticlesShadowCell(this.context);
                        break;
                    }
                    case 27: {
                        o = new BlockDetailsBottomCell(this.context);
                        break;
                    }
                    case 26: {
                        o = new BlockRelatedArticlesHeaderCell(this.context, this);
                        break;
                    }
                    case 25: {
                        o = new BlockTableCell(this.context, this);
                        break;
                    }
                    case 24: {
                        o = new BlockDetailsCell(this.context, this);
                        break;
                    }
                    case 23: {
                        o = new BlockRelatedArticlesCell(this.context, this);
                        break;
                    }
                    case 22: {
                        o = new BlockMapCell(this.context, this, 0);
                        break;
                    }
                    case 21: {
                        o = new BlockOrderedListItemCell(this.context, this);
                        break;
                    }
                    case 20: {
                        o = new BlockKickerCell(this.context, this);
                        break;
                    }
                    case 19: {
                        o = new BlockAudioCell(this.context, this);
                        break;
                    }
                    case 18: {
                        o = new BlockChannelCell(this.context, this, 0);
                        break;
                    }
                    case 17: {
                        o = new BlockCollageCell(this.context, this);
                        break;
                    }
                    case 16: {
                        o = new BlockEmbedPostCell(this.context, this);
                        break;
                    }
                    case 15: {
                        o = new BlockSubheaderCell(this.context, this);
                        break;
                    }
                    case 14: {
                        o = new BlockPreformattedCell(this.context, this);
                        break;
                    }
                    case 13: {
                        o = new BlockFooterCell(this.context, this);
                        break;
                    }
                    case 12: {
                        o = new BlockListItemCell(this.context, this);
                        break;
                    }
                    case 11: {
                        o = new BlockTitleCell(this.context, this);
                        break;
                    }
                    case 10: {
                        o = new BlockAuthorDateCell(this.context, this);
                        break;
                    }
                    case 9: {
                        o = new BlockPhotoCell(this.context, this, 0);
                        break;
                    }
                    case 8: {
                        o = new BlockSlideshowCell(this.context, this);
                        break;
                    }
                    case 7: {
                        o = new BlockBlockquoteCell(this.context, this);
                        break;
                    }
                    case 6: {
                        o = new BlockPullquoteCell(this.context, this);
                        break;
                    }
                    case 5: {
                        o = new BlockVideoCell(this.context, this, 0);
                        break;
                    }
                    case 4: {
                        o = new BlockSubtitleCell(this.context, this);
                        break;
                    }
                    case 3: {
                        o = new BlockEmbedCell(this.context, this);
                        break;
                    }
                    case 2: {
                        o = new BlockDividerCell(this.context);
                        break;
                    }
                    case 1: {
                        o = new BlockHeaderCell(this.context, this);
                        break;
                    }
                    case 0: {
                        o = new BlockParagraphCell(this.context, this);
                        break;
                    }
                }
            }
            else {
                o = new FrameLayout(this.context) {
                    protected void onMeasure(final int n, final int n2) {
                        super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0f), 1073741824));
                    }
                };
                ((FrameLayout)o).setTag((Object)90);
                final TextView textView = new TextView(this.context);
                ((FrameLayout)o).addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 34.0f, 51, 0.0f, 10.0f, 0.0f, 0.0f));
                textView.setText((CharSequence)LocaleController.getString("PreviewFeedback", 2131560472));
                textView.setTextSize(1, 12.0f);
                textView.setGravity(17);
            }
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            ((View)o).setFocusable(true);
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    private class WindowView extends FrameLayout
    {
        private float alpha;
        private Runnable attachRunnable;
        private int bHeight;
        private int bWidth;
        private int bX;
        private int bY;
        private boolean closeAnimationInProgress;
        private float innerTranslationX;
        private boolean maybeStartTracking;
        private boolean movingPage;
        private boolean selfLayout;
        private int startMovingHeaderHeight;
        private boolean startedTracking;
        private int startedTrackingPointerId;
        private int startedTrackingX;
        private int startedTrackingY;
        private VelocityTracker tracker;
        
        public WindowView(final Context context) {
            super(context);
        }
        
        private void prepareForMoving(final MotionEvent motionEvent) {
            this.maybeStartTracking = false;
            this.startedTracking = true;
            this.startedTrackingX = (int)motionEvent.getX();
            if (ArticleViewer.this.pagesStack.size() > 1) {
                this.movingPage = true;
                this.startMovingHeaderHeight = ArticleViewer.this.currentHeaderHeight;
                ArticleViewer.this.listView[1].setVisibility(0);
                ArticleViewer.this.listView[1].setAlpha(1.0f);
                ArticleViewer.this.listView[1].setTranslationX(0.0f);
                ArticleViewer.this.listView[0].setBackgroundColor(ArticleViewer.this.backgroundPaint.getColor());
            }
            else {
                this.movingPage = false;
            }
            ArticleViewer.this.cancelCheckLongPress();
        }
        
        protected void dispatchDraw(final Canvas canvas) {
            super.dispatchDraw(canvas);
            final int bWidth = this.bWidth;
            if (bWidth != 0) {
                final int bHeight = this.bHeight;
                if (bHeight != 0) {
                    final int bx = this.bX;
                    if (bx == 0) {
                        final int by = this.bY;
                        if (by == 0) {
                            canvas.drawRect((float)bx, (float)by, (float)(bx + bWidth), (float)(by + bHeight), ArticleViewer.this.blackPaint);
                            return;
                        }
                    }
                    canvas.drawRect(this.bX - this.getTranslationX(), (float)this.bY, this.bX + this.bWidth - this.getTranslationX(), (float)(this.bY + this.bHeight), ArticleViewer.this.blackPaint);
                }
            }
        }
        
        protected boolean drawChild(final Canvas canvas, final View view, final long n) {
            final int measuredWidth = this.getMeasuredWidth();
            final int n2 = (int)this.innerTranslationX;
            final int save = canvas.save();
            canvas.clipRect(n2, 0, measuredWidth, this.getHeight());
            final boolean drawChild = super.drawChild(canvas, view, n);
            canvas.restoreToCount(save);
            if (n2 != 0 && view == ArticleViewer.this.containerView) {
                final float n3 = (float)(measuredWidth - n2);
                float min;
                if ((min = Math.min(0.8f, n3 / measuredWidth)) < 0.0f) {
                    min = 0.0f;
                }
                ArticleViewer.this.scrimPaint.setColor((int)(min * 153.0f) << 24);
                canvas.drawRect(0.0f, 0.0f, (float)n2, (float)this.getHeight(), ArticleViewer.this.scrimPaint);
                final float max = Math.max(0.0f, Math.min(n3 / AndroidUtilities.dp(20.0f), 1.0f));
                ArticleViewer.this.layerShadowDrawable.setBounds(n2 - ArticleViewer.this.layerShadowDrawable.getIntrinsicWidth(), view.getTop(), n2, view.getBottom());
                ArticleViewer.this.layerShadowDrawable.setAlpha((int)(max * 255.0f));
                ArticleViewer.this.layerShadowDrawable.draw(canvas);
            }
            return drawChild;
        }
        
        @Keep
        public float getAlpha() {
            return this.alpha;
        }
        
        @Keep
        public float getInnerTranslationX() {
            return this.innerTranslationX;
        }
        
        public boolean handleTouchEvent(final MotionEvent motionEvent) {
            if (!ArticleViewer.this.isPhotoVisible && !this.closeAnimationInProgress && ArticleViewer.this.fullscreenVideoContainer.getVisibility() != 0) {
                if (motionEvent != null && motionEvent.getAction() == 0 && !this.startedTracking && !this.maybeStartTracking) {
                    this.startedTrackingPointerId = motionEvent.getPointerId(0);
                    this.maybeStartTracking = true;
                    this.startedTrackingX = (int)motionEvent.getX();
                    this.startedTrackingY = (int)motionEvent.getY();
                    final VelocityTracker tracker = this.tracker;
                    if (tracker != null) {
                        tracker.clear();
                    }
                }
                else if (motionEvent != null && motionEvent.getAction() == 2 && motionEvent.getPointerId(0) == this.startedTrackingPointerId) {
                    if (this.tracker == null) {
                        this.tracker = VelocityTracker.obtain();
                    }
                    final int max = Math.max(0, (int)(motionEvent.getX() - this.startedTrackingX));
                    final int abs = Math.abs((int)motionEvent.getY() - this.startedTrackingY);
                    this.tracker.addMovement(motionEvent);
                    if (this.maybeStartTracking && !this.startedTracking && max >= AndroidUtilities.getPixelsInCM(0.4f, true) && Math.abs(max) / 3 > abs) {
                        this.prepareForMoving(motionEvent);
                    }
                    else if (this.startedTracking) {
                        ArticleViewer.this.pressedLinkOwnerLayout = null;
                        ArticleViewer.this.pressedLinkOwnerView = null;
                        if (this.movingPage) {
                            ArticleViewer.this.listView[0].setTranslationX((float)max);
                        }
                        else {
                            final FrameLayout access$1400 = ArticleViewer.this.containerView;
                            final float n = (float)max;
                            access$1400.setTranslationX(n);
                            this.setInnerTranslationX(n);
                        }
                    }
                }
                else if (motionEvent != null && motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6)) {
                    if (this.tracker == null) {
                        this.tracker = VelocityTracker.obtain();
                    }
                    this.tracker.computeCurrentVelocity(1000);
                    final float xVelocity = this.tracker.getXVelocity();
                    final float yVelocity = this.tracker.getYVelocity();
                    if (!this.startedTracking && xVelocity >= 3500.0f && xVelocity > Math.abs(yVelocity)) {
                        this.prepareForMoving(motionEvent);
                    }
                    if (this.startedTracking) {
                        Object access$1401;
                        if (this.movingPage) {
                            access$1401 = ArticleViewer.this.listView[0];
                        }
                        else {
                            access$1401 = ArticleViewer.this.containerView;
                        }
                        float x = ((View)access$1401).getX();
                        final boolean b = x < ((View)access$1401).getMeasuredWidth() / 3.0f && (xVelocity < 3500.0f || xVelocity < yVelocity);
                        final AnimatorSet set = new AnimatorSet();
                        if (!b) {
                            x = ((View)access$1401).getMeasuredWidth() - x;
                            if (this.movingPage) {
                                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)ArticleViewer.this.listView[0], View.TRANSLATION_X, new float[] { (float)((View)access$1401).getMeasuredWidth() }) });
                            }
                            else {
                                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)ArticleViewer.this.containerView, View.TRANSLATION_X, new float[] { (float)((View)access$1401).getMeasuredWidth() }), (Animator)ObjectAnimator.ofFloat((Object)this, (Property)ArticleViewer.ARTICLE_VIEWER_INNER_TRANSLATION_X, new float[] { (float)((View)access$1401).getMeasuredWidth() }) });
                            }
                        }
                        else if (this.movingPage) {
                            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)ArticleViewer.this.listView[0], View.TRANSLATION_X, new float[] { 0.0f }) });
                        }
                        else {
                            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)ArticleViewer.this.containerView, View.TRANSLATION_X, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this, (Property)ArticleViewer.ARTICLE_VIEWER_INNER_TRANSLATION_X, new float[] { 0.0f }) });
                        }
                        set.setDuration((long)Math.max((int)(200.0f / ((View)access$1401).getMeasuredWidth() * x), 50));
                        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator animator) {
                                if (WindowView.this.movingPage) {
                                    ArticleViewer.this.listView[0].setBackgroundDrawable((Drawable)null);
                                    if (!b) {
                                        final WebpageAdapter webpageAdapter = ArticleViewer.this.adapter[1];
                                        ArticleViewer.this.adapter[1] = ArticleViewer.this.adapter[0];
                                        ArticleViewer.this.adapter[0] = webpageAdapter;
                                        final RecyclerListView recyclerListView = ArticleViewer.this.listView[1];
                                        ArticleViewer.this.listView[1] = ArticleViewer.this.listView[0];
                                        ArticleViewer.this.listView[0] = recyclerListView;
                                        final LinearLayoutManager linearLayoutManager = ArticleViewer.this.layoutManager[1];
                                        ArticleViewer.this.layoutManager[1] = ArticleViewer.this.layoutManager[0];
                                        ArticleViewer.this.layoutManager[0] = linearLayoutManager;
                                        ArticleViewer.this.pagesStack.remove(ArticleViewer.this.pagesStack.size() - 1);
                                        final ArticleViewer this$0 = ArticleViewer.this;
                                        this$0.currentPage = (TLRPC.WebPage)this$0.pagesStack.get(ArticleViewer.this.pagesStack.size() - 1);
                                    }
                                    ArticleViewer.this.listView[1].setVisibility(8);
                                    ArticleViewer.this.headerView.invalidate();
                                }
                                else if (!b) {
                                    ArticleViewer.this.saveCurrentPagePosition();
                                    ArticleViewer.this.onClosed();
                                }
                                WindowView.this.movingPage = false;
                                WindowView.this.startedTracking = false;
                                WindowView.this.closeAnimationInProgress = false;
                            }
                        });
                        set.start();
                        this.closeAnimationInProgress = true;
                    }
                    else {
                        this.maybeStartTracking = false;
                        this.startedTracking = false;
                        this.movingPage = false;
                    }
                    final VelocityTracker tracker2 = this.tracker;
                    if (tracker2 != null) {
                        tracker2.recycle();
                        this.tracker = null;
                    }
                }
                else if (motionEvent == null) {
                    this.maybeStartTracking = false;
                    this.startedTracking = false;
                    this.movingPage = false;
                    final VelocityTracker tracker3 = this.tracker;
                    if (tracker3 != null) {
                        tracker3.recycle();
                        this.tracker = null;
                    }
                }
                return this.startedTracking;
            }
            return false;
        }
        
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            ArticleViewer.this.attachedToWindow = true;
        }
        
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            ArticleViewer.this.attachedToWindow = false;
        }
        
        protected void onDraw(final Canvas canvas) {
            canvas.drawRect(this.innerTranslationX, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), ArticleViewer.this.backgroundPaint);
            if (Build$VERSION.SDK_INT >= 21) {
                final ArticleViewer this$0 = ArticleViewer.this;
                if (this$0.hasCutout && this$0.lastInsets != null) {
                    canvas.drawRect(this.innerTranslationX, 0.0f, (float)this.getMeasuredWidth(), (float)((WindowInsets)ArticleViewer.this.lastInsets).getSystemWindowInsetBottom(), ArticleViewer.this.statusBarPaint);
                }
            }
        }
        
        public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
            return !ArticleViewer.this.collapsed && (this.handleTouchEvent(motionEvent) || super.onInterceptTouchEvent(motionEvent));
        }
        
        protected void onLayout(final boolean b, int i, int n, int n2, final int n3) {
            if (this.selfLayout) {
                return;
            }
            n2 -= i;
            if (ArticleViewer.this.anchorsOffsetMeasuredWidth != n2) {
                Iterator<Map.Entry<K, Integer>> iterator;
                for (i = 0; i < ArticleViewer.this.listView.length; ++i) {
                    iterator = ArticleViewer.this.adapter[i].anchorsOffset.entrySet().iterator();
                    while (iterator.hasNext()) {
                        iterator.next().setValue(-1);
                    }
                }
                ArticleViewer.this.anchorsOffsetMeasuredWidth = n2;
            }
            if (Build$VERSION.SDK_INT >= 21 && ArticleViewer.this.lastInsets != null) {
                final WindowInsets windowInsets = (WindowInsets)ArticleViewer.this.lastInsets;
                i = windowInsets.getSystemWindowInsetLeft();
                if (windowInsets.getSystemWindowInsetRight() != 0) {
                    this.bX = n2 - this.bWidth;
                    this.bY = 0;
                }
                else if (windowInsets.getSystemWindowInsetLeft() != 0) {
                    this.bX = 0;
                    this.bY = 0;
                }
                else {
                    this.bX = 0;
                    this.bY = n3 - n - this.bHeight;
                }
                if (Build$VERSION.SDK_INT >= 28) {
                    n2 = windowInsets.getSystemWindowInsetTop() + 0;
                    n = i;
                    i = n2;
                }
                else {
                    n2 = 0;
                    n = i;
                    i = n2;
                }
            }
            else {
                i = 0;
                n = 0;
            }
            ArticleViewer.this.containerView.layout(n, i, ArticleViewer.this.containerView.getMeasuredWidth() + n, ArticleViewer.this.containerView.getMeasuredHeight() + i);
            ArticleViewer.this.photoContainerView.layout(n, i, ArticleViewer.this.photoContainerView.getMeasuredWidth() + n, ArticleViewer.this.photoContainerView.getMeasuredHeight() + i);
            ArticleViewer.this.photoContainerBackground.layout(n, i, ArticleViewer.this.photoContainerBackground.getMeasuredWidth() + n, ArticleViewer.this.photoContainerBackground.getMeasuredHeight() + i);
            ArticleViewer.this.fullscreenVideoContainer.layout(n, i, ArticleViewer.this.fullscreenVideoContainer.getMeasuredWidth() + n, ArticleViewer.this.fullscreenVideoContainer.getMeasuredHeight() + i);
            ArticleViewer.this.animatingImageView.layout(0, 0, ArticleViewer.this.animatingImageView.getMeasuredWidth(), ArticleViewer.this.animatingImageView.getMeasuredHeight());
        }
        
        protected void onMeasure(int size, int n) {
            final int size2 = View$MeasureSpec.getSize(size);
            size = View$MeasureSpec.getSize(n);
            if (Build$VERSION.SDK_INT >= 21 && ArticleViewer.this.lastInsets != null) {
                this.setMeasuredDimension(size2, size);
                final WindowInsets windowInsets = (WindowInsets)ArticleViewer.this.lastInsets;
                n = size;
                if (AndroidUtilities.incorrectDisplaySizeFix) {
                    final int y = AndroidUtilities.displaySize.y;
                    if ((n = size) > y) {
                        n = y;
                    }
                    n += AndroidUtilities.statusBarHeight;
                }
                n -= windowInsets.getSystemWindowInsetBottom();
                size = size2 - (windowInsets.getSystemWindowInsetRight() + windowInsets.getSystemWindowInsetLeft());
                if (windowInsets.getSystemWindowInsetRight() != 0) {
                    this.bWidth = windowInsets.getSystemWindowInsetRight();
                    this.bHeight = n;
                }
                else if (windowInsets.getSystemWindowInsetLeft() != 0) {
                    this.bWidth = windowInsets.getSystemWindowInsetLeft();
                    this.bHeight = n;
                }
                else {
                    this.bWidth = size;
                    this.bHeight = windowInsets.getSystemWindowInsetBottom();
                }
                n -= windowInsets.getSystemWindowInsetTop();
            }
            else {
                this.setMeasuredDimension(size2, size);
                n = size;
                size = size2;
            }
            ArticleViewer.this.containerView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n, 1073741824));
            ArticleViewer.this.photoContainerView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n, 1073741824));
            ArticleViewer.this.photoContainerBackground.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n, 1073741824));
            ArticleViewer.this.fullscreenVideoContainer.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n, 1073741824));
            final ViewGroup$LayoutParams layoutParams = ArticleViewer.this.animatingImageView.getLayoutParams();
            ArticleViewer.this.animatingImageView.measure(View$MeasureSpec.makeMeasureSpec(layoutParams.width, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(layoutParams.height, Integer.MIN_VALUE));
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return !ArticleViewer.this.collapsed && (this.handleTouchEvent(motionEvent) || super.onTouchEvent(motionEvent));
        }
        
        public void requestDisallowInterceptTouchEvent(final boolean b) {
            this.handleTouchEvent(null);
            super.requestDisallowInterceptTouchEvent(b);
        }
        
        @Keep
        public void setAlpha(final float alpha) {
            final Paint access$3100 = ArticleViewer.this.backgroundPaint;
            final int n = (int)(255.0f * alpha);
            access$3100.setAlpha(n);
            ArticleViewer.this.statusBarPaint.setAlpha(n);
            this.alpha = alpha;
            if (ArticleViewer.this.parentActivity instanceof LaunchActivity) {
                ((LaunchActivity)ArticleViewer.this.parentActivity).drawerLayoutContainer.setAllowDrawContent(!ArticleViewer.this.isVisible || this.alpha != 1.0f || this.innerTranslationX != 0.0f);
            }
            this.invalidate();
        }
        
        @Keep
        public void setInnerTranslationX(final float innerTranslationX) {
            this.innerTranslationX = innerTranslationX;
            if (ArticleViewer.this.parentActivity instanceof LaunchActivity) {
                ((LaunchActivity)ArticleViewer.this.parentActivity).drawerLayoutContainer.setAllowDrawContent(!ArticleViewer.this.isVisible || this.alpha != 1.0f || this.innerTranslationX != 0.0f);
            }
            this.invalidate();
        }
    }
}
