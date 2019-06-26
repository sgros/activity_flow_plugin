package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.Spannable.Factory;
import android.text.TextUtils.TruncateAt;
import android.text.style.MetricAffectingSpan;
import android.text.style.URLSpan;
import android.util.LongSparseArray;
import android.util.Property;
import android.util.SparseArray;
import android.view.DisplayCutout;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.MeasureSpec;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import androidx.annotation.Keep;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManagerFixed;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.WebFile;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnchorSpan;
import org.telegram.ui.Components.AnimatedArrowDrawable;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.ClippingImageView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.GroupedPhotosListView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LineProgressView;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.Scroller;
import org.telegram.ui.Components.SeekBar;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.StaticLayoutEx;
import org.telegram.ui.Components.TableLayout;
import org.telegram.ui.Components.TextPaintImageReceiverSpan;
import org.telegram.ui.Components.TextPaintMarkSpan;
import org.telegram.ui.Components.TextPaintSpan;
import org.telegram.ui.Components.TextPaintUrlSpan;
import org.telegram.ui.Components.TextPaintWebpageUrlSpan;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.Components.WebPlayerView;

public class ArticleViewer implements NotificationCenter.NotificationCenterDelegate, OnGestureListener, OnDoubleTapListener {
   public static final Property ARTICLE_VIEWER_INNER_TRANSLATION_X = new AnimationProperties.FloatProperty("innerTranslationX") {
      public Float get(ArticleViewer.WindowView var1) {
         return var1.getInnerTranslationX();
      }

      public void setValue(ArticleViewer.WindowView var1, float var2) {
         var1.setInnerTranslationX(var2);
      }
   };
   @SuppressLint({"StaticFieldLeak"})
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
   private static TextPaint audioTimePaint = new TextPaint(1);
   private static SparseArray authorTextPaints = new SparseArray();
   private static TextPaint channelNamePaint;
   private static Paint colorPaint;
   private static DecelerateInterpolator decelerateInterpolator;
   private static SparseArray detailsTextPaints = new SparseArray();
   private static Paint dividerPaint;
   private static Paint dotsPaint;
   private static TextPaint embedPostAuthorPaint;
   private static SparseArray embedPostCaptionTextPaints = new SparseArray();
   private static TextPaint embedPostDatePaint;
   private static SparseArray embedPostTextPaints = new SparseArray();
   private static TextPaint errorTextPaint;
   private static SparseArray footerTextPaints = new SparseArray();
   private static final int gallery_menu_openin = 3;
   private static final int gallery_menu_save = 1;
   private static final int gallery_menu_share = 2;
   private static SparseArray headerTextPaints = new SparseArray();
   private static SparseArray kickerTextPaints = new SparseArray();
   private static TextPaint listTextNumPaint;
   private static SparseArray listTextPaints = new SparseArray();
   private static TextPaint listTextPointerPaint;
   private static SparseArray mediaCaptionTextPaints = new SparseArray();
   private static SparseArray mediaCreditTextPaints = new SparseArray();
   private static SparseArray paragraphTextPaints = new SparseArray();
   private static Paint photoBackgroundPaint;
   private static SparseArray photoCaptionTextPaints = new SparseArray();
   private static SparseArray photoCreditTextPaints = new SparseArray();
   private static Paint preformattedBackgroundPaint;
   private static SparseArray preformattedTextPaints = new SparseArray();
   private static Drawable[] progressDrawables;
   private static Paint progressPaint;
   private static Paint quoteLinePaint;
   private static SparseArray quoteTextPaints = new SparseArray();
   private static TextPaint relatedArticleHeaderPaint;
   private static TextPaint relatedArticleTextPaint;
   private static SparseArray relatedArticleTextPaints = new SparseArray();
   private static Paint selectorPaint;
   private static SparseArray subheaderTextPaints = new SparseArray();
   private static SparseArray subtitleTextPaints = new SparseArray();
   private static Paint tableHalfLinePaint;
   private static Paint tableHeaderPaint;
   private static Paint tableLinePaint;
   private static Paint tableStripPaint;
   private static SparseArray tableTextPaints = new SparseArray();
   private static SparseArray titleTextPaints = new SparseArray();
   private static Paint urlPaint;
   private static Paint webpageMarkPaint;
   private static Paint webpageUrlPaint;
   private ActionBar actionBar;
   private ArticleViewer.WebpageAdapter[] adapter;
   private int anchorsOffsetMeasuredWidth;
   private float animateToScale;
   private float animateToX;
   private float animateToY;
   private ClippingImageView animatingImageView;
   private Runnable animationEndRunnable;
   private int animationInProgress;
   private long animationStartTime;
   private float animationValue;
   private float[][] animationValues = new float[2][10];
   private AspectRatioFrameLayout aspectRatioFrameLayout;
   private boolean attachedToWindow;
   private ImageView backButton;
   private BackDrawable backDrawable;
   private Paint backgroundPaint;
   private Paint blackPaint = new Paint();
   private FrameLayout bottomLayout;
   private boolean canDragDown = true;
   private boolean canZoom = true;
   private TextView captionTextView;
   private TextView captionTextViewNext;
   private ImageReceiver centerImage = new ImageReceiver();
   private boolean changingPage;
   private TLRPC.TL_pageBlockChannel channelBlock;
   private boolean checkingForLongPress = false;
   private boolean collapsed;
   private ArticleViewer.ColorCell[] colorCells = new ArticleViewer.ColorCell[3];
   private FrameLayout containerView;
   private int[] coords = new int[2];
   private Drawable copyBackgroundDrawable;
   private ArrayList createdWebViews = new ArrayList();
   private int currentAccount;
   private AnimatorSet currentActionBarAnimation;
   private AnimatedFileDrawable currentAnimation;
   private String[] currentFileNames = new String[3];
   private int currentHeaderHeight;
   private int currentIndex;
   private TLRPC.PageBlock currentMedia;
   private TLRPC.WebPage currentPage;
   private ArticleViewer.PlaceProviderObject currentPlaceObject;
   private WebPlayerView currentPlayingVideo;
   private int currentRotation;
   private ImageReceiver.BitmapHolder currentThumb;
   private View customView;
   private CustomViewCallback customViewCallback;
   private TextView deleteView;
   private boolean disableShowCheck;
   private boolean discardTap;
   private boolean dontResetZoomOnFirstLayout;
   private boolean doubleTap;
   private float dragY;
   private boolean draggingDown;
   private boolean drawBlockSelection;
   private ArticleViewer.FontCell[] fontCells = new ArticleViewer.FontCell[2];
   private final int fontSizeCount = 5;
   private AspectRatioFrameLayout fullscreenAspectRatioView;
   private TextureView fullscreenTextureView;
   private FrameLayout fullscreenVideoContainer;
   private WebPlayerView fullscreenedVideo;
   private GestureDetector gestureDetector;
   private GroupedPhotosListView groupedPhotosListView;
   boolean hasCutout;
   private Paint headerPaint = new Paint();
   private Paint headerProgressPaint = new Paint();
   private FrameLayout headerView;
   private ArticleViewer.PlaceProviderObject hideAfterAnimation;
   private AnimatorSet imageMoveAnimation;
   private ArrayList imagesArr = new ArrayList();
   private DecelerateInterpolator interpolator = new DecelerateInterpolator(1.5F);
   private boolean invalidCoords;
   private boolean isActionBarVisible = true;
   private boolean isPhotoVisible;
   private boolean isPlaying;
   private boolean isRtl;
   private boolean isVisible;
   private int lastBlockNum = 1;
   private Object lastInsets;
   private int lastReqId;
   private Drawable layerShadowDrawable;
   private LinearLayoutManager[] layoutManager;
   private ImageReceiver leftImage = new ImageReceiver();
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
   private ArrayList pagesStack = new ArrayList();
   private Activity parentActivity;
   private BaseFragment parentFragment;
   private ArticleViewer.CheckForLongPress pendingCheckForLongPress = null;
   private ArticleViewer.CheckForTap pendingCheckForTap = null;
   private Runnable photoAnimationEndRunnable;
   private int photoAnimationInProgress;
   private ArticleViewer.PhotoBackgroundDrawable photoBackgroundDrawable = new ArticleViewer.PhotoBackgroundDrawable(-16777216);
   private View photoContainerBackground;
   private ArticleViewer.FrameLayoutDrawer photoContainerView;
   private long photoTransitionAnimationStartTime;
   private float pinchCenterX;
   private float pinchCenterY;
   private float pinchStartDistance;
   private float pinchStartScale = 1.0F;
   private float pinchStartX;
   private float pinchStartY;
   private ActionBarPopupWindow.ActionBarPopupWindowLayout popupLayout;
   private Rect popupRect;
   private ActionBarPopupWindow popupWindow;
   private int pressCount = 0;
   private int pressedLayoutY;
   private TextPaintUrlSpan pressedLink;
   private ArticleViewer.DrawingText pressedLinkOwnerLayout;
   private View pressedLinkOwnerView;
   private int previewsReqId;
   private ContextProgressView progressView;
   private AnimatorSet progressViewAnimation;
   private ArticleViewer.RadialProgressView[] radialProgressViews = new ArticleViewer.RadialProgressView[3];
   private ImageReceiver rightImage = new ImageReceiver();
   private float scale = 1.0F;
   private Paint scrimPaint;
   private Scroller scroller;
   private int selectedColor = 0;
   private int selectedFont = 0;
   private int selectedFontSize = 2;
   private ActionBarMenuItem settingsButton;
   private ImageView shareButton;
   private FrameLayout shareContainer;
   private ArticleViewer.PlaceProviderObject showAfterAnimation;
   private Drawable slideDotBigDrawable;
   private Drawable slideDotDrawable;
   private Paint statusBarPaint = new Paint();
   private int switchImageAfterAnimation;
   private boolean textureUploaded;
   private SimpleTextView titleTextView;
   private long transitionAnimationStartTime;
   private float translationX;
   private float translationY;
   private Runnable updateProgressRunnable = new Runnable() {
      public void run() {
         if (ArticleViewer.this.videoPlayer != null && ArticleViewer.this.videoPlayerSeekbar != null && !ArticleViewer.this.videoPlayerSeekbar.isDragging()) {
            float var1 = (float)ArticleViewer.this.videoPlayer.getCurrentPosition() / (float)ArticleViewer.this.videoPlayer.getDuration();
            ArticleViewer.this.videoPlayerSeekbar.setProgress(var1);
            ArticleViewer.this.videoPlayerControlFrameLayout.invalidate();
            ArticleViewer.this.updateVideoPlayerTime();
         }

         if (ArticleViewer.this.isPlaying) {
            AndroidUtilities.runOnUIThread(ArticleViewer.this.updateProgressRunnable, 100L);
         }

      }
   };
   private LinkPath urlPath = new LinkPath();
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
   private LayoutParams windowLayoutParams;
   private ArticleViewer.WindowView windowView;
   private boolean zoomAnimation;
   private boolean zooming;

   // $FF: synthetic method
   static int access$1104(ArticleViewer var0) {
      int var1 = var0.pressCount + 1;
      var0.pressCount = var1;
      return var1;
   }

   // $FF: synthetic method
   static int access$13008(ArticleViewer var0) {
      int var1 = var0.lastBlockNum++;
      return var1;
   }

   private boolean addPageToStack(TLRPC.WebPage var1, String var2, int var3) {
      this.saveCurrentPagePosition();
      this.currentPage = var1;
      this.pagesStack.add(var1);
      this.updateInterfaceForCurrentPage(var3);
      return this.scrollToAnchor(var2);
   }

   private void animateTo(float var1, float var2, float var3, boolean var4) {
      this.animateTo(var1, var2, var3, var4, 250);
   }

   private void animateTo(float var1, float var2, float var3, boolean var4, int var5) {
      if (this.scale != var1 || this.translationX != var2 || this.translationY != var3) {
         this.zoomAnimation = var4;
         this.animateToScale = var1;
         this.animateToX = var2;
         this.animateToY = var3;
         this.animationStartTime = System.currentTimeMillis();
         this.imageMoveAnimation = new AnimatorSet();
         this.imageMoveAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "animationValue", new float[]{0.0F, 1.0F})});
         this.imageMoveAnimation.setInterpolator(this.interpolator);
         this.imageMoveAnimation.setDuration((long)var5);
         this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               ArticleViewer.this.imageMoveAnimation = null;
               ArticleViewer.this.photoContainerView.invalidate();
            }
         });
         this.imageMoveAnimation.start();
      }
   }

   private boolean checkAnimation() {
      int var1 = this.animationInProgress;
      boolean var2 = false;
      if (var1 != 0 && Math.abs(this.transitionAnimationStartTime - System.currentTimeMillis()) >= 500L) {
         Runnable var3 = this.animationEndRunnable;
         if (var3 != null) {
            var3.run();
            this.animationEndRunnable = null;
         }

         this.animationInProgress = 0;
      }

      if (this.animationInProgress != 0) {
         var2 = true;
      }

      return var2;
   }

   private boolean checkLayoutForLinks(MotionEvent var1, View var2, ArticleViewer.DrawingText var3, int var4, int var5) {
      if (this.pageSwitchAnimation == null && var2 != null && var3 != null) {
         boolean var10;
         boolean var11;
         boolean var37;
         label236: {
            label235: {
               StaticLayout var6 = var3.textLayout;
               int var7 = (int)var1.getX();
               int var8 = (int)var1.getY();
               int var9 = var1.getAction();
               var10 = true;
               var11 = true;
               if (var9 == 0) {
                  int var12 = var6.getLineCount();
                  var9 = 0;
                  float var13 = 2.14748365E9F;

                  float var14;
                  for(var14 = 0.0F; var9 < var12; ++var9) {
                     var14 = Math.max(var6.getLineWidth(var9), var14);
                     var13 = Math.min(var6.getLineLeft(var9), var13);
                  }

                  float var15 = (float)var7;
                  var13 += (float)var4;
                  if (var15 >= var13 && var15 <= var13 + var14 && var8 >= var5 && var8 <= var6.getHeight() + var5) {
                     this.pressedLinkOwnerLayout = var3;
                     this.pressedLinkOwnerView = var2;
                     this.pressedLayoutY = var5;
                     if (var6.getText() instanceof Spannable) {
                        label215: {
                           Exception var10000;
                           Exception var33;
                           label244: {
                              boolean var10001;
                              try {
                                 var5 = var6.getLineForVertical(var8 - var5);
                              } catch (Exception var30) {
                                 var10000 = var30;
                                 var10001 = false;
                                 break label244;
                              }

                              var13 = (float)(var7 - var4);

                              try {
                                 var4 = var6.getOffsetForHorizontal(var5, var13);
                                 var14 = var6.getLineLeft(var5);
                              } catch (Exception var29) {
                                 var10000 = var29;
                                 var10001 = false;
                                 break label244;
                              }

                              if (var14 > var13) {
                                 break label215;
                              }

                              TextPaintUrlSpan[] var16;
                              Spannable var31;
                              try {
                                 if (var14 + var6.getLineWidth(var5) < var13) {
                                    break label215;
                                 }

                                 var31 = (Spannable)var6.getText();
                                 var16 = (TextPaintUrlSpan[])var31.getSpans(var4, var4, TextPaintUrlSpan.class);
                              } catch (Exception var28) {
                                 var10000 = var28;
                                 var10001 = false;
                                 break label244;
                              }

                              if (var16 == null) {
                                 break label215;
                              }

                              try {
                                 if (var16.length <= 0) {
                                    break label215;
                                 }

                                 this.pressedLink = var16[0];
                                 var5 = var31.getSpanStart(this.pressedLink);
                                 var4 = var31.getSpanEnd(this.pressedLink);
                              } catch (Exception var27) {
                                 var10000 = var27;
                                 var10001 = false;
                                 break label244;
                              }

                              var7 = 1;

                              while(true) {
                                 try {
                                    if (var7 >= var16.length) {
                                       break;
                                    }
                                 } catch (Exception var26) {
                                    var10000 = var26;
                                    var10001 = false;
                                    break label244;
                                 }

                                 TextPaintUrlSpan var17 = var16[var7];

                                 try {
                                    var12 = var31.getSpanStart(var17);
                                    var8 = var31.getSpanEnd(var17);
                                 } catch (Exception var25) {
                                    var10000 = var25;
                                    var10001 = false;
                                    break label244;
                                 }

                                 label246: {
                                    if (var5 <= var12) {
                                       var9 = var4;
                                       if (var8 <= var4) {
                                          break label246;
                                       }
                                    }

                                    try {
                                       this.pressedLink = var17;
                                    } catch (Exception var24) {
                                       var10000 = var24;
                                       var10001 = false;
                                       break label244;
                                    }

                                    var5 = var12;
                                    var9 = var8;
                                 }

                                 ++var7;
                                 var4 = var9;
                              }

                              label251: {
                                 label178: {
                                    try {
                                       this.urlPath.setUseRoundRect(true);
                                       this.urlPath.setCurrentLayout(var6, var5, 0.0F);
                                       if (this.pressedLink.getTextPaint() != null) {
                                          var9 = this.pressedLink.getTextPaint().baselineShift;
                                          break label178;
                                       }
                                    } catch (Exception var23) {
                                       var10000 = var23;
                                       var10001 = false;
                                       break label251;
                                    }

                                    var9 = 0;
                                 }

                                 LinkPath var32;
                                 try {
                                    var32 = this.urlPath;
                                 } catch (Exception var22) {
                                    var10000 = var22;
                                    var10001 = false;
                                    break label251;
                                 }

                                 if (var9 != 0) {
                                    if (var9 > 0) {
                                       var14 = 5.0F;
                                    } else {
                                       var14 = -2.0F;
                                    }

                                    try {
                                       var9 += AndroidUtilities.dp(var14);
                                    } catch (Exception var21) {
                                       var10000 = var21;
                                       var10001 = false;
                                       break label251;
                                    }
                                 } else {
                                    var9 = 0;
                                 }

                                 try {
                                    var32.setBaselineShift(var9);
                                    var6.getSelectionPath(var5, var4, this.urlPath);
                                    var2.invalidate();
                                    break label215;
                                 } catch (Exception var20) {
                                    var10000 = var20;
                                    var10001 = false;
                                 }
                              }

                              var33 = var10000;

                              try {
                                 FileLog.e((Throwable)var33);
                                 break label215;
                              } catch (Exception var19) {
                                 var10000 = var19;
                                 var10001 = false;
                              }
                           }

                           var33 = var10000;
                           FileLog.e((Throwable)var33);
                        }
                     }
                  }
               } else if (var1.getAction() == 1) {
                  TextPaintUrlSpan var34 = this.pressedLink;
                  if (var34 != null) {
                     String var41 = var34.getUrl();
                     if (var41 != null) {
                        BottomSheet var35 = this.linkSheet;
                        if (var35 != null) {
                           var35.dismiss();
                           this.linkSheet = null;
                        }

                        String var36;
                        label148: {
                           var4 = var41.lastIndexOf(35);
                           String var38;
                           if (var4 != -1) {
                              String var40;
                              if (!TextUtils.isEmpty(this.currentPage.cached_page.url)) {
                                 var40 = this.currentPage.cached_page.url.toLowerCase();
                              } else {
                                 var40 = this.currentPage.url.toLowerCase();
                              }

                              try {
                                 var36 = URLDecoder.decode(var41.substring(var4 + 1), "UTF-8");
                              } catch (Exception var18) {
                                 var36 = "";
                              }

                              var38 = var36;
                              if (var41.toLowerCase().contains(var40)) {
                                 if (TextUtils.isEmpty(var36)) {
                                    this.layoutManager[0].scrollToPositionWithOffset(0, 0);
                                    this.checkScrollAnimated();
                                 } else {
                                    this.scrollToAnchor(var36);
                                 }

                                 var37 = true;
                                 break label148;
                              }
                           } else {
                              var38 = null;
                           }

                           var37 = false;
                           var36 = var38;
                        }

                        if (!var37) {
                           this.openWebpageUrl(this.pressedLink.getUrl(), var36);
                        }
                     }
                     break label235;
                  }
               } else if (var1.getAction() == 3) {
                  ActionBarPopupWindow var39 = this.popupWindow;
                  if (var39 == null || !var39.isShowing()) {
                     break label235;
                  }
               }

               var37 = false;
               break label236;
            }

            var37 = true;
         }

         if (var37) {
            this.removePressedLink();
         }

         if (var1.getAction() == 0) {
            this.startCheckLongPress();
         }

         if (var1.getAction() != 0 && var1.getAction() != 2) {
            this.cancelCheckLongPress();
         }

         if (var2 instanceof ArticleViewer.BlockDetailsCell) {
            if (this.pressedLink == null) {
               var11 = false;
            }

            return var11;
         } else {
            if (this.pressedLinkOwnerLayout != null) {
               var11 = var10;
            } else {
               var11 = false;
            }

            return var11;
         }
      } else {
         return false;
      }
   }

   private void checkMinMax(boolean var1) {
      float var2 = this.translationX;
      float var3 = this.translationY;
      this.updateMinMax(this.scale);
      float var4 = this.translationX;
      float var5 = this.minX;
      if (var4 < var5) {
         var2 = var5;
      } else {
         var5 = this.maxX;
         if (var4 > var5) {
            var2 = var5;
         }
      }

      var4 = this.translationY;
      var5 = this.minY;
      if (var4 < var5) {
         var3 = var5;
      } else {
         var5 = this.maxY;
         if (var4 > var5) {
            var3 = var5;
         }
      }

      this.animateTo(this.scale, var2, var3, var1);
   }

   private boolean checkPhotoAnimation() {
      int var1 = this.photoAnimationInProgress;
      boolean var2 = false;
      if (var1 != 0 && Math.abs(this.photoTransitionAnimationStartTime - System.currentTimeMillis()) >= 500L) {
         Runnable var3 = this.photoAnimationEndRunnable;
         if (var3 != null) {
            var3.run();
            this.photoAnimationEndRunnable = null;
         }

         this.photoAnimationInProgress = 0;
      }

      if (this.photoAnimationInProgress != 0) {
         var2 = true;
      }

      return var2;
   }

   private void checkProgress(int var1, boolean var2) {
      if (this.currentFileNames[var1] != null) {
         int var3 = this.currentIndex;
         boolean var4 = true;
         int var5;
         if (var1 == 1) {
            var5 = var3 + 1;
         } else {
            var5 = var3;
            if (var1 == 2) {
               var5 = var3 - 1;
            }
         }

         File var6 = this.getMediaFile(var5);
         boolean var7 = this.isMediaVideo(var5);
         if (var6 != null && var6.exists()) {
            if (var7) {
               this.radialProgressViews[var1].setBackgroundState(3, var2);
            } else {
               this.radialProgressViews[var1].setBackgroundState(-1, var2);
            }
         } else {
            if (var7) {
               if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[var1])) {
                  this.radialProgressViews[var1].setBackgroundState(2, false);
               } else {
                  this.radialProgressViews[var1].setBackgroundState(1, false);
               }
            } else {
               this.radialProgressViews[var1].setBackgroundState(0, var2);
            }

            Float var8 = ImageLoader.getInstance().getFileProgress(this.currentFileNames[var1]);
            Float var9 = var8;
            if (var8 == null) {
               var9 = 0.0F;
            }

            this.radialProgressViews[var1].setProgress(var9, false);
         }

         if (var1 == 0) {
            if (this.currentFileNames[0] != null && !var7 && this.radialProgressViews[0].backgroundState != 0) {
               var2 = var4;
            } else {
               var2 = false;
            }

            this.canZoom = var2;
         }
      } else {
         this.radialProgressViews[var1].setBackgroundState(-1, var2);
      }

   }

   private void checkScroll(int var1) {
      this.setCurrentHeaderHeight(this.currentHeaderHeight - var1);
   }

   private void checkScrollAnimated() {
      int var1 = AndroidUtilities.dp(56.0F);
      if (this.currentHeaderHeight != var1) {
         ValueAnimator var2 = ValueAnimator.ofObject(new IntEvaluator(), new Object[]{this.currentHeaderHeight, AndroidUtilities.dp(56.0F)}).setDuration(180L);
         var2.setInterpolator(new DecelerateInterpolator());
         var2.addUpdateListener(new _$$Lambda$ArticleViewer$sxYzbPn_gQQmb_6tSvBl0G3LOAw(this));
         var2.start();
      }
   }

   private ArticleViewer.DrawingText createLayoutForText(View var1, CharSequence var2, TLRPC.RichText var3, int var4, int var5, TLRPC.PageBlock var6, Alignment var7, int var8, ArticleViewer.WebpageAdapter var9) {
      Object var10 = null;
      if (var2 != null || var3 != null && !(var3 instanceof TLRPC.TL_textEmpty)) {
         int var11;
         if (var4 < 0) {
            var11 = AndroidUtilities.dp(10.0F);
         } else {
            var11 = var4;
         }

         int var12 = this.getSelectedColor();
         CharSequence var13;
         if (var2 != null) {
            var13 = var2;
         } else {
            var13 = this.getText(var1, var3, var3, var6, var11);
         }

         if (TextUtils.isEmpty(var13)) {
            return null;
         } else {
            label349: {
               var4 = this.selectedFontSize;
               if (var4 == 0) {
                  var4 = AndroidUtilities.dp(4.0F);
               } else {
                  if (var4 != 1) {
                     if (var4 == 3) {
                        var4 = AndroidUtilities.dp(2.0F);
                     } else if (var4 == 4) {
                        var4 = AndroidUtilities.dp(4.0F);
                     } else {
                        var4 = 0;
                     }
                     break label349;
                  }

                  var4 = AndroidUtilities.dp(2.0F);
               }

               var4 = -var4;
            }

            TextPaint var34;
            if (var6 instanceof TLRPC.TL_pageBlockEmbedPost && var3 == null) {
               if (((TLRPC.TL_pageBlockEmbedPost)var6).author == var2) {
                  if (embedPostAuthorPaint == null) {
                     embedPostAuthorPaint = new TextPaint(1);
                     embedPostAuthorPaint.setColor(this.getTextColor());
                  }

                  embedPostAuthorPaint.setTextSize((float)(AndroidUtilities.dp(15.0F) + var4));
                  var34 = embedPostAuthorPaint;
               } else {
                  if (embedPostDatePaint == null) {
                     embedPostDatePaint = new TextPaint(1);
                     if (var12 == 0) {
                        embedPostDatePaint.setColor(-7366752);
                     } else {
                        embedPostDatePaint.setColor(this.getGrayTextColor());
                     }
                  }

                  embedPostDatePaint.setTextSize((float)(AndroidUtilities.dp(14.0F) + var4));
                  var34 = embedPostDatePaint;
               }
            } else if (var6 instanceof TLRPC.TL_pageBlockChannel) {
               if (channelNamePaint == null) {
                  channelNamePaint = new TextPaint(1);
                  channelNamePaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
               }

               if (this.channelBlock == null) {
                  channelNamePaint.setColor(this.getTextColor());
               } else {
                  channelNamePaint.setColor(-1);
               }

               channelNamePaint.setTextSize((float)AndroidUtilities.dp(15.0F));
               var34 = channelNamePaint;
            } else if (var6 instanceof ArticleViewer.TL_pageBlockRelatedArticlesChild) {
               ArticleViewer.TL_pageBlockRelatedArticlesChild var35 = (ArticleViewer.TL_pageBlockRelatedArticlesChild)var6;
               if (var2 == ((TLRPC.TL_pageRelatedArticle)var35.parent.articles.get(var35.num)).title) {
                  if (relatedArticleHeaderPaint == null) {
                     relatedArticleHeaderPaint = new TextPaint(1);
                     relatedArticleHeaderPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                  }

                  relatedArticleHeaderPaint.setColor(this.getTextColor());
                  relatedArticleHeaderPaint.setTextSize((float)(AndroidUtilities.dp(15.0F) + var4));
                  var34 = relatedArticleHeaderPaint;
               } else {
                  if (relatedArticleTextPaint == null) {
                     relatedArticleTextPaint = new TextPaint(1);
                  }

                  relatedArticleTextPaint.setColor(this.getGrayTextColor());
                  relatedArticleTextPaint.setTextSize((float)(AndroidUtilities.dp(14.0F) + var4));
                  var34 = relatedArticleTextPaint;
               }
            } else if (this.isListItemBlock(var6) && var2 != null) {
               if (listTextPointerPaint == null) {
                  listTextPointerPaint = new TextPaint(1);
                  listTextPointerPaint.setColor(this.getTextColor());
               }

               if (listTextNumPaint == null) {
                  listTextNumPaint = new TextPaint(1);
                  listTextNumPaint.setColor(this.getTextColor());
               }

               listTextPointerPaint.setTextSize((float)(AndroidUtilities.dp(19.0F) + var4));
               listTextNumPaint.setTextSize((float)(AndroidUtilities.dp(16.0F) + var4));
               if (var6 instanceof ArticleViewer.TL_pageBlockListItem && !((ArticleViewer.TL_pageBlockListItem)var6).parent.pageBlockList.ordered) {
                  var34 = listTextPointerPaint;
               } else {
                  var34 = listTextNumPaint;
               }
            } else {
               var34 = this.getTextPaint(var3, var3, var6);
            }

            StaticLayout var40;
            if (var8 != 0) {
               if (var6 instanceof TLRPC.TL_pageBlockPullquote) {
                  var40 = StaticLayoutEx.createStaticLayout(var13, var34, var11, Alignment.ALIGN_CENTER, 1.0F, 0.0F, false, TruncateAt.END, var11, var8);
               } else {
                  var40 = StaticLayoutEx.createStaticLayout(var13, var34, var11, var7, 1.0F, (float)AndroidUtilities.dp(4.0F), false, TruncateAt.END, var11, var8);
               }
            } else {
               var2 = var13;
               if (var13.charAt(var13.length() - 1) == '\n') {
                  var2 = var13.subSequence(0, var13.length() - 1);
               }

               if (var6 instanceof TLRPC.TL_pageBlockPullquote) {
                  var40 = new StaticLayout(var2, var34, var11, Alignment.ALIGN_CENTER, 1.0F, 0.0F, false);
               } else {
                  var40 = new StaticLayout(var2, var34, var11, var7, 1.0F, (float)AndroidUtilities.dp(4.0F), false);
               }
            }

            if (var40 == null) {
               return null;
            } else {
               var2 = var40.getText();
               LinkPath var36;
               LinkPath var42;
               if (var40 != null && var2 instanceof Spanned) {
                  label377: {
                     Spanned var43 = (Spanned)var2;

                     boolean var10001;
                     label363: {
                        AnchorSpan[] var38;
                        try {
                           var38 = (AnchorSpan[])var43.getSpans(0, var43.length(), AnchorSpan.class);
                           var8 = var40.getLineCount();
                        } catch (Exception var33) {
                           var10001 = false;
                           break label363;
                        }

                        if (var38 != null) {
                           label381: {
                              try {
                                 if (var38.length <= 0) {
                                    break label381;
                                 }
                              } catch (Exception var32) {
                                 var10001 = false;
                                 break label381;
                              }

                              var4 = 0;

                              while(true) {
                                 try {
                                    if (var4 >= var38.length) {
                                       break;
                                    }
                                 } catch (Exception var31) {
                                    var10001 = false;
                                    break;
                                 }

                                 if (var8 <= 1) {
                                    try {
                                       var9.anchorsOffset.put(var38[var4].getName(), var5);
                                    } catch (Exception var30) {
                                       var10001 = false;
                                       break;
                                    }
                                 } else {
                                    try {
                                       var9.anchorsOffset.put(var38[var4].getName(), var5 + var40.getLineTop(var40.getLineForOffset(var43.getSpanStart(var38[var4]))));
                                    } catch (Exception var29) {
                                       var10001 = false;
                                       break;
                                    }
                                 }

                                 ++var4;
                              }
                           }
                        }
                     }

                     float var14;
                     LinkPath var39;
                     label296: {
                        label389: {
                           TextPaintWebpageUrlSpan[] var37;
                           try {
                              var37 = (TextPaintWebpageUrlSpan[])var43.getSpans(0, var43.length(), TextPaintWebpageUrlSpan.class);
                           } catch (Exception var28) {
                              var10001 = false;
                              break label389;
                           }

                           if (var37 == null) {
                              break label389;
                           }

                           try {
                              if (var37.length <= 0) {
                                 break label389;
                              }

                              var39 = new LinkPath(true);
                           } catch (Exception var27) {
                              var10001 = false;
                              break label389;
                           }

                           try {
                              var39.setAllowReset(false);
                           } catch (Exception var26) {
                              var10001 = false;
                              break label296;
                           }

                           var4 = 0;

                           while(true) {
                              label274: {
                                 try {
                                    if (var4 >= var37.length) {
                                       break;
                                    }

                                    var11 = var43.getSpanStart(var37[var4]);
                                    var8 = var43.getSpanEnd(var37[var4]);
                                    var39.setCurrentLayout(var40, var11, 0.0F);
                                    if (var37[var4].getTextPaint() != null) {
                                       var5 = var37[var4].getTextPaint().baselineShift;
                                       break label274;
                                    }
                                 } catch (Exception var25) {
                                    var10001 = false;
                                    break label296;
                                 }

                                 var5 = 0;
                              }

                              if (var5 != 0) {
                                 if (var5 > 0) {
                                    var14 = 5.0F;
                                 } else {
                                    var14 = -2.0F;
                                 }

                                 try {
                                    var5 += AndroidUtilities.dp(var14);
                                 } catch (Exception var24) {
                                    var10001 = false;
                                    break label296;
                                 }
                              } else {
                                 var5 = 0;
                              }

                              try {
                                 var39.setBaselineShift(var5);
                                 var40.getSelectionPath(var11, var8, var39);
                              } catch (Exception var23) {
                                 var10001 = false;
                                 break label296;
                              }

                              ++var4;
                           }

                           try {
                              var39.setAllowReset(true);
                           } catch (Exception var22) {
                              var10001 = false;
                           }
                           break label296;
                        }

                        var39 = null;
                     }

                     TextPaintMarkSpan[] var44;
                     label252: {
                        label371: {
                           try {
                              var44 = (TextPaintMarkSpan[])var43.getSpans(0, var43.length(), TextPaintMarkSpan.class);
                           } catch (Exception var21) {
                              var10001 = false;
                              break label371;
                           }

                           var42 = var39;
                           var36 = (LinkPath)var10;
                           if (var44 == null) {
                              break label377;
                           }

                           var42 = var39;
                           var36 = (LinkPath)var10;

                           try {
                              if (var44.length <= 0) {
                                 break label377;
                              }

                              var36 = new LinkPath(true);
                              break label252;
                           } catch (Exception var20) {
                              var10001 = false;
                           }
                        }

                        var42 = var39;
                        var36 = (LinkPath)var10;
                        break label377;
                     }

                     label372: {
                        try {
                           var36.setAllowReset(false);
                        } catch (Exception var19) {
                           var10001 = false;
                           break label372;
                        }

                        var4 = 0;

                        while(true) {
                           label230: {
                              try {
                                 if (var4 >= var44.length) {
                                    break;
                                 }

                                 var8 = var43.getSpanStart(var44[var4]);
                                 var11 = var43.getSpanEnd(var44[var4]);
                                 var36.setCurrentLayout(var40, var8, 0.0F);
                                 if (var44[var4].getTextPaint() != null) {
                                    var5 = var44[var4].getTextPaint().baselineShift;
                                    break label230;
                                 }
                              } catch (Exception var18) {
                                 var10001 = false;
                                 break label372;
                              }

                              var5 = 0;
                           }

                           if (var5 != 0) {
                              if (var5 > 0) {
                                 var14 = 5.0F;
                              } else {
                                 var14 = -2.0F;
                              }

                              try {
                                 var5 += AndroidUtilities.dp(var14);
                              } catch (Exception var17) {
                                 var10001 = false;
                                 break label372;
                              }
                           } else {
                              var5 = 0;
                           }

                           try {
                              var36.setBaselineShift(var5);
                              var40.getSelectionPath(var8, var11, var36);
                           } catch (Exception var16) {
                              var10001 = false;
                              break label372;
                           }

                           ++var4;
                        }

                        try {
                           var36.setAllowReset(true);
                        } catch (Exception var15) {
                           var10001 = false;
                        }
                     }

                     var42 = var39;
                  }
               } else {
                  var42 = null;
                  var36 = (LinkPath)var10;
               }

               ArticleViewer.DrawingText var41 = new ArticleViewer.DrawingText();
               var41.textLayout = var40;
               var41.textPath = var42;
               var41.markPath = var36;
               return var41;
            }
         }
      } else {
         return null;
      }
   }

   private ArticleViewer.DrawingText createLayoutForText(View var1, CharSequence var2, TLRPC.RichText var3, int var4, int var5, TLRPC.PageBlock var6, ArticleViewer.WebpageAdapter var7) {
      return this.createLayoutForText(var1, var2, var3, var4, var5, var6, Alignment.ALIGN_NORMAL, 0, var7);
   }

   private ArticleViewer.DrawingText createLayoutForText(View var1, CharSequence var2, TLRPC.RichText var3, int var4, TLRPC.PageBlock var5, Alignment var6, ArticleViewer.WebpageAdapter var7) {
      return this.createLayoutForText(var1, var2, var3, var4, 0, var5, var6, 0, var7);
   }

   private ArticleViewer.DrawingText createLayoutForText(View var1, CharSequence var2, TLRPC.RichText var3, int var4, TLRPC.PageBlock var5, ArticleViewer.WebpageAdapter var6) {
      return this.createLayoutForText(var1, var2, var3, var4, 0, var5, Alignment.ALIGN_NORMAL, 0, var6);
   }

   private void createPaint(boolean var1) {
      if (quoteLinePaint == null) {
         quoteLinePaint = new Paint();
         preformattedBackgroundPaint = new Paint();
         tableLinePaint = new Paint(1);
         tableLinePaint.setStyle(Style.STROKE);
         tableLinePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0F));
         tableHalfLinePaint = new Paint();
         tableHalfLinePaint.setStyle(Style.STROKE);
         tableHalfLinePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0F) / 2.0F);
         tableHeaderPaint = new Paint();
         tableStripPaint = new Paint();
         urlPaint = new Paint();
         webpageUrlPaint = new Paint(1);
         photoBackgroundPaint = new Paint();
         dividerPaint = new Paint();
         webpageMarkPaint = new Paint(1);
      } else if (!var1) {
         return;
      }

      int var2 = this.getSelectedColor();
      if (var2 == 0) {
         preformattedBackgroundPaint.setColor(-657156);
         webpageUrlPaint.setColor(-1313798);
         urlPaint.setColor(-2299145);
         tableHalfLinePaint.setColor(-2039584);
         tableLinePaint.setColor(-2039584);
         tableHeaderPaint.setColor(-723724);
         tableStripPaint.setColor(-526345);
         photoBackgroundPaint.setColor(-723724);
         dividerPaint.setColor(-3288619);
         webpageMarkPaint.setColor(-68676);
      } else if (var2 == 1) {
         preformattedBackgroundPaint.setColor(-1712440);
         webpageUrlPaint.setColor(-2365721);
         urlPaint.setColor(-3481882);
         tableHalfLinePaint.setColor(-3620432);
         tableLinePaint.setColor(-3620432);
         tableHeaderPaint.setColor(-1120560);
         tableStripPaint.setColor(-1120560);
         photoBackgroundPaint.setColor(-1120560);
         dividerPaint.setColor(-4080987);
         webpageMarkPaint.setColor(-1712691);
      } else if (var2 == 2) {
         preformattedBackgroundPaint.setColor(-15000805);
         webpageUrlPaint.setColor(-14536904);
         urlPaint.setColor(-14469050);
         tableHalfLinePaint.setColor(-13750738);
         tableLinePaint.setColor(-13750738);
         tableHeaderPaint.setColor(-15066598);
         tableStripPaint.setColor(-15066598);
         photoBackgroundPaint.setColor(-14935012);
         dividerPaint.setColor(-12303292);
         webpageMarkPaint.setColor(-14408668);
      }

      quoteLinePaint.setColor(this.getTextColor());
   }

   private void drawContent(Canvas var1) {
      int var2 = this.photoAnimationInProgress;
      if (var2 != 1 && (this.isPhotoVisible || var2 == 2)) {
         float var3;
         float var4;
         float var5;
         float var6;
         float var7;
         float var8;
         float var9;
         if (this.imageMoveAnimation != null) {
            if (!this.scroller.isFinished()) {
               this.scroller.abortAnimation();
            }

            var3 = this.scale;
            var4 = this.animateToScale;
            var5 = this.animationValue;
            var6 = (var4 - var3) * var5 + var3;
            var7 = this.translationX;
            var8 = (this.animateToX - var7) * var5 + var7;
            var9 = this.translationY;
            var9 += (this.animateToY - var9) * var5;
            if (var4 == 1.0F && var3 == 1.0F && var7 == 0.0F) {
               var7 = var9;
            } else {
               var7 = -1.0F;
            }

            this.photoContainerView.invalidate();
         } else {
            if (this.animationStartTime != 0L) {
               this.translationX = this.animateToX;
               this.translationY = this.animateToY;
               this.scale = this.animateToScale;
               this.animationStartTime = 0L;
               this.updateMinMax(this.scale);
               this.zoomAnimation = false;
            }

            if (!this.scroller.isFinished() && this.scroller.computeScrollOffset()) {
               if ((float)this.scroller.getStartX() < this.maxX && (float)this.scroller.getStartX() > this.minX) {
                  this.translationX = (float)this.scroller.getCurrX();
               }

               if ((float)this.scroller.getStartY() < this.maxY && (float)this.scroller.getStartY() > this.minY) {
                  this.translationY = (float)this.scroller.getCurrY();
               }

               this.photoContainerView.invalidate();
            }

            var2 = this.switchImageAfterAnimation;
            if (var2 != 0) {
               if (var2 == 1) {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$sVetLHzILrx_miSeiNhv3ir2iLY(this));
               } else if (var2 == 2) {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$oXQW1t29RA_lRZqbkS6wEfWeLck(this));
               }

               this.switchImageAfterAnimation = 0;
            }

            var6 = this.scale;
            var7 = this.translationY;
            var8 = this.translationX;
            if (!this.moving) {
               var9 = var7;
            } else {
               var9 = var7;
               var7 = -1.0F;
            }
         }

         if (this.photoAnimationInProgress != 2) {
            if (this.scale == 1.0F && var7 != -1.0F && !this.zoomAnimation) {
               var4 = (float)this.getContainerViewHeight() / 4.0F;
               this.photoBackgroundDrawable.setAlpha((int)Math.max(127.0F, (1.0F - Math.min(Math.abs(var7), var4) / var4) * 255.0F));
            } else {
               this.photoBackgroundDrawable.setAlpha(255);
            }
         }

         AspectRatioFrameLayout var10 = null;
         ImageReceiver var11 = var10;
         if (this.scale >= 1.0F) {
            var11 = var10;
            if (!this.zoomAnimation) {
               var11 = var10;
               if (!this.zooming) {
                  if (var8 > this.maxX + (float)AndroidUtilities.dp(5.0F)) {
                     var11 = this.leftImage;
                  } else if (var8 < this.minX - (float)AndroidUtilities.dp(5.0F)) {
                     var11 = this.rightImage;
                  } else {
                     this.groupedPhotosListView.setMoveProgress(0.0F);
                     var11 = var10;
                  }
               }
            }
         }

         boolean var12;
         if (var11 != null) {
            var12 = true;
         } else {
            var12 = false;
         }

         this.changingPage = var12;
         int var13;
         float var14;
         float var15;
         float var17;
         if (var11 == this.rightImage) {
            label178: {
               if (!this.zoomAnimation) {
                  var7 = this.minX;
                  if (var8 < var7) {
                     var7 = Math.min(1.0F, (var7 - var8) / (float)var1.getWidth());
                     var4 = (1.0F - var7) * 0.3F;
                     var3 = (float)(-var1.getWidth() - AndroidUtilities.dp(30.0F) / 2);
                     break label178;
                  }
               }

               var3 = var8;
               var7 = 1.0F;
               var4 = 0.0F;
            }

            if (var11.hasBitmapImage()) {
               var1.save();
               var1.translate((float)(this.getContainerViewWidth() / 2), (float)(this.getContainerViewHeight() / 2));
               var1.translate((float)(var1.getWidth() + AndroidUtilities.dp(30.0F) / 2) + var3, 0.0F);
               var5 = 1.0F - var4;
               var1.scale(var5, var5);
               var2 = var11.getBitmapWidth();
               var13 = var11.getBitmapHeight();
               var5 = (float)this.getContainerViewWidth();
               var14 = (float)var2;
               var15 = var5 / var14;
               var5 = (float)this.getContainerViewHeight();
               float var16 = (float)var13;
               var17 = var5 / var16;
               var5 = var15;
               if (var15 > var17) {
                  var5 = var17;
               }

               var13 = (int)(var14 * var5);
               var2 = (int)(var16 * var5);
               var11.setAlpha(var7);
               var11.setImageCoords(-var13 / 2, -var2 / 2, var13, var2);
               var11.draw(var1);
               var1.restore();
            }

            this.groupedPhotosListView.setMoveProgress(-var7);
            var1.save();
            var1.translate(var3, var9 / var6);
            var1.translate(((float)var1.getWidth() * (this.scale + 1.0F) + (float)AndroidUtilities.dp(30.0F)) / 2.0F, -var9 / var6);
            this.radialProgressViews[1].setScale(1.0F - var4);
            this.radialProgressViews[1].setAlpha(var7);
            this.radialProgressViews[1].onDraw(var1);
            var1.restore();
         }

         label171: {
            if (!this.zoomAnimation) {
               var7 = this.maxX;
               if (var8 > var7) {
                  var7 = Math.min(1.0F, (var8 - var7) / (float)var1.getWidth());
                  var4 = var7 * 0.3F;
                  var7 = 1.0F - var7;
                  var3 = this.maxX;
                  break label171;
               }
            }

            var3 = var8;
            var7 = 1.0F;
            var4 = 0.0F;
         }

         var10 = this.aspectRatioFrameLayout;
         boolean var25;
         if (var10 != null && var10.getVisibility() == 0) {
            var25 = true;
         } else {
            var25 = false;
         }

         if (this.centerImage.hasBitmapImage()) {
            var1.save();
            var1.translate((float)(this.getContainerViewWidth() / 2), (float)(this.getContainerViewHeight() / 2));
            var1.translate(var3, var9);
            var5 = var6 - var4;
            var1.scale(var5, var5);
            int var18 = this.centerImage.getBitmapWidth();
            int var19 = this.centerImage.getBitmapHeight();
            int var20 = var18;
            var13 = var19;
            if (var25) {
               var20 = var18;
               var13 = var19;
               if (this.textureUploaded) {
                  var20 = var18;
                  var13 = var19;
                  if (Math.abs((float)var18 / (float)var19 - (float)this.videoTextureView.getMeasuredWidth() / (float)this.videoTextureView.getMeasuredHeight()) > 0.01F) {
                     var20 = this.videoTextureView.getMeasuredWidth();
                     var13 = this.videoTextureView.getMeasuredHeight();
                  }
               }
            }

            var5 = (float)this.getContainerViewWidth();
            var17 = (float)var20;
            var5 /= var17;
            var15 = (float)this.getContainerViewHeight();
            var14 = (float)var13;
            var15 /= var14;
            if (var5 > var15) {
               var5 = var15;
            }

            var20 = (int)(var17 * var5);
            var13 = (int)(var14 * var5);
            if (!var25 || !this.textureUploaded || !this.videoCrossfadeStarted || this.videoCrossfadeAlpha != 1.0F) {
               this.centerImage.setAlpha(var7);
               this.centerImage.setImageCoords(-var20 / 2, -var13 / 2, var20, var13);
               this.centerImage.draw(var1);
            }

            if (var25) {
               if (!this.videoCrossfadeStarted && this.textureUploaded) {
                  this.videoCrossfadeStarted = true;
                  this.videoCrossfadeAlpha = 0.0F;
                  this.videoCrossfadeAlphaLastTime = System.currentTimeMillis();
               }

               var1.translate((float)(-var20 / 2), (float)(-var13 / 2));
               this.videoTextureView.setAlpha(this.videoCrossfadeAlpha * var7);
               this.aspectRatioFrameLayout.draw(var1);
               if (this.videoCrossfadeStarted && this.videoCrossfadeAlpha < 1.0F) {
                  long var21 = System.currentTimeMillis();
                  long var23 = this.videoCrossfadeAlphaLastTime;
                  this.videoCrossfadeAlphaLastTime = var21;
                  this.videoCrossfadeAlpha += (float)(var21 - var23) / 300.0F;
                  this.photoContainerView.invalidate();
                  if (this.videoCrossfadeAlpha > 1.0F) {
                     this.videoCrossfadeAlpha = 1.0F;
                  }
               }
            }

            var1.restore();
         }

         if (!var25 && this.bottomLayout.getVisibility() != 0) {
            var1.save();
            var1.translate(var3, var9 / var6);
            this.radialProgressViews[0].setScale(1.0F - var4);
            this.radialProgressViews[0].setAlpha(var7);
            this.radialProgressViews[0].onDraw(var1);
            var1.restore();
         }

         if (var11 == this.leftImage) {
            if (var11.hasBitmapImage()) {
               var1.save();
               var1.translate((float)(this.getContainerViewWidth() / 2), (float)(this.getContainerViewHeight() / 2));
               var1.translate(-((float)var1.getWidth() * (this.scale + 1.0F) + (float)AndroidUtilities.dp(30.0F)) / 2.0F + var8, 0.0F);
               var13 = var11.getBitmapWidth();
               var2 = var11.getBitmapHeight();
               var4 = (float)this.getContainerViewWidth();
               var15 = (float)var13;
               var3 = var4 / var15;
               var4 = (float)this.getContainerViewHeight();
               var17 = (float)var2;
               var5 = var4 / var17;
               var4 = var3;
               if (var3 > var5) {
                  var4 = var5;
               }

               var2 = (int)(var15 * var4);
               var13 = (int)(var17 * var4);
               var11.setAlpha(1.0F);
               var11.setImageCoords(-var2 / 2, -var13 / 2, var2, var13);
               var11.draw(var1);
               var1.restore();
            }

            this.groupedPhotosListView.setMoveProgress(1.0F - var7);
            var1.save();
            var1.translate(var8, var9 / var6);
            var1.translate(-((float)var1.getWidth() * (this.scale + 1.0F) + (float)AndroidUtilities.dp(30.0F)) / 2.0F, -var9 / var6);
            this.radialProgressViews[2].setScale(1.0F);
            this.radialProgressViews[2].setAlpha(1.0F);
            this.radialProgressViews[2].onDraw(var1);
            var1.restore();
         }
      }

   }

   private void drawLayoutLink(Canvas var1, ArticleViewer.DrawingText var2) {
      if (var1 != null && var2 != null && this.pressedLinkOwnerLayout == var2) {
         if (this.pressedLink != null) {
            var1.drawPath(this.urlPath, urlPaint);
         } else if (this.drawBlockSelection && var2 != null) {
            float var3;
            float var4;
            if (var2.getLineCount() == 1) {
               var3 = var2.getLineWidth(0);
               var4 = var2.getLineLeft(0);
            } else {
               var3 = (float)var2.getWidth();
               var4 = 0.0F;
            }

            var1.drawRect((float)(-AndroidUtilities.dp(2.0F)) + var4, 0.0F, var4 + var3 + (float)AndroidUtilities.dp(2.0F), (float)var2.getHeight(), urlPaint);
         }
      }

   }

   private TLRPC.PageBlock fixListBlock(TLRPC.PageBlock var1, TLRPC.PageBlock var2) {
      if (var1 instanceof ArticleViewer.TL_pageBlockListItem) {
         ((ArticleViewer.TL_pageBlockListItem)var1).blockItem = var2;
         return var1;
      } else if (var1 instanceof ArticleViewer.TL_pageBlockOrderedListItem) {
         ((ArticleViewer.TL_pageBlockOrderedListItem)var1).blockItem = var2;
         return var1;
      } else {
         return var2;
      }
   }

   private TLRPC.RichText getBlockCaption(TLRPC.PageBlock var1, int var2) {
      if (var2 == 2) {
         TLRPC.RichText var3 = this.getBlockCaption(var1, 0);
         TLRPC.RichText var4 = var3;
         if (var3 instanceof TLRPC.TL_textEmpty) {
            var4 = null;
         }

         var3 = this.getBlockCaption(var1, 1);
         TLRPC.RichText var15 = var3;
         if (var3 instanceof TLRPC.TL_textEmpty) {
            var15 = null;
         }

         if (var4 != null && var15 == null) {
            return var4;
         } else if (var4 == null && var15 != null) {
            return var15;
         } else if (var4 != null && var15 != null) {
            TLRPC.TL_textPlain var13 = new TLRPC.TL_textPlain();
            var13.text = " ";
            TLRPC.TL_textConcat var5 = new TLRPC.TL_textConcat();
            var5.texts.add(var4);
            var5.texts.add(var13);
            var5.texts.add(var15);
            return var5;
         } else {
            return null;
         }
      } else {
         if (var1 instanceof TLRPC.TL_pageBlockEmbedPost) {
            TLRPC.TL_pageBlockEmbedPost var6 = (TLRPC.TL_pageBlockEmbedPost)var1;
            if (var2 == 0) {
               return var6.caption.text;
            }

            if (var2 == 1) {
               return var6.caption.credit;
            }
         } else if (var1 instanceof TLRPC.TL_pageBlockSlideshow) {
            TLRPC.TL_pageBlockSlideshow var7 = (TLRPC.TL_pageBlockSlideshow)var1;
            if (var2 == 0) {
               return var7.caption.text;
            }

            if (var2 == 1) {
               return var7.caption.credit;
            }
         } else if (var1 instanceof TLRPC.TL_pageBlockPhoto) {
            TLRPC.TL_pageBlockPhoto var8 = (TLRPC.TL_pageBlockPhoto)var1;
            if (var2 == 0) {
               return var8.caption.text;
            }

            if (var2 == 1) {
               return var8.caption.credit;
            }
         } else if (var1 instanceof TLRPC.TL_pageBlockCollage) {
            TLRPC.TL_pageBlockCollage var9 = (TLRPC.TL_pageBlockCollage)var1;
            if (var2 == 0) {
               return var9.caption.text;
            }

            if (var2 == 1) {
               return var9.caption.credit;
            }
         } else if (var1 instanceof TLRPC.TL_pageBlockEmbed) {
            TLRPC.TL_pageBlockEmbed var10 = (TLRPC.TL_pageBlockEmbed)var1;
            if (var2 == 0) {
               return var10.caption.text;
            }

            if (var2 == 1) {
               return var10.caption.credit;
            }
         } else {
            if (var1 instanceof TLRPC.TL_pageBlockBlockquote) {
               return ((TLRPC.TL_pageBlockBlockquote)var1).caption;
            }

            if (var1 instanceof TLRPC.TL_pageBlockVideo) {
               TLRPC.TL_pageBlockVideo var11 = (TLRPC.TL_pageBlockVideo)var1;
               if (var2 == 0) {
                  return var11.caption.text;
               }

               if (var2 == 1) {
                  return var11.caption.credit;
               }
            } else {
               if (var1 instanceof TLRPC.TL_pageBlockPullquote) {
                  return ((TLRPC.TL_pageBlockPullquote)var1).caption;
               }

               if (var1 instanceof TLRPC.TL_pageBlockAudio) {
                  TLRPC.TL_pageBlockAudio var12 = (TLRPC.TL_pageBlockAudio)var1;
                  if (var2 == 0) {
                     return var12.caption.text;
                  }

                  if (var2 == 1) {
                     return var12.caption.credit;
                  }
               } else {
                  if (var1 instanceof TLRPC.TL_pageBlockCover) {
                     return this.getBlockCaption(((TLRPC.TL_pageBlockCover)var1).cover, var2);
                  }

                  if (var1 instanceof TLRPC.TL_pageBlockMap) {
                     TLRPC.TL_pageBlockMap var14 = (TLRPC.TL_pageBlockMap)var1;
                     if (var2 == 0) {
                        return var14.caption.text;
                     }

                     if (var2 == 1) {
                        return var14.caption.credit;
                     }
                  }
               }
            }
         }

         return null;
      }
   }

   private int getContainerViewHeight() {
      return this.photoContainerView.getHeight();
   }

   private int getContainerViewWidth() {
      return this.photoContainerView.getWidth();
   }

   private TLRPC.Document getDocumentWithId(long var1) {
      TLRPC.WebPage var3 = this.currentPage;
      if (var3 != null && var3.cached_page != null) {
         TLRPC.Document var5 = var3.document;
         if (var5 != null && var5.id == var1) {
            return var5;
         }

         for(int var4 = 0; var4 < this.currentPage.cached_page.documents.size(); ++var4) {
            var5 = (TLRPC.Document)this.currentPage.cached_page.documents.get(var4);
            if (var5.id == var1) {
               return var5;
            }
         }
      }

      return null;
   }

   private TLRPC.PhotoSize getFileLocation(TLObject var1, int[] var2) {
      TLRPC.PhotoSize var3;
      if (var1 instanceof TLRPC.Photo) {
         var3 = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Photo)var1).sizes, AndroidUtilities.getPhotoSize());
         if (var3 != null) {
            var2[0] = var3.size;
            if (var2[0] == 0) {
               var2[0] = -1;
            }

            return var3;
         }

         var2[0] = -1;
      } else if (var1 instanceof TLRPC.Document) {
         var3 = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Document)var1).thumbs, 90);
         if (var3 != null) {
            var2[0] = var3.size;
            if (var2[0] == 0) {
               var2[0] = -1;
            }

            return var3;
         }
      }

      return null;
   }

   private String getFileName(int var1) {
      TLObject var2 = this.getMedia(var1);
      Object var3 = var2;
      if (var2 instanceof TLRPC.Photo) {
         var3 = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Photo)var2).sizes, AndroidUtilities.getPhotoSize());
      }

      return FileLoader.getAttachFileName((TLObject)var3);
   }

   private int getGrayTextColor() {
      int var1 = this.getSelectedColor();
      if (var1 != 0) {
         return var1 != 1 ? -10066330 : -11711675;
      } else {
         return -8156010;
      }
   }

   private ImageReceiver getImageReceiverFromListView(ViewGroup var1, TLRPC.PageBlock var2, int[] var3) {
      int var4 = var1.getChildCount();

      for(int var5 = 0; var5 < var4; ++var5) {
         ImageReceiver var6 = this.getImageReceiverView(var1.getChildAt(var5), var2, var3);
         if (var6 != null) {
            return var6;
         }
      }

      return null;
   }

   private ImageReceiver getImageReceiverView(View var1, TLRPC.PageBlock var2, int[] var3) {
      if (var1 instanceof ArticleViewer.BlockPhotoCell) {
         ArticleViewer.BlockPhotoCell var4 = (ArticleViewer.BlockPhotoCell)var1;
         if (var4.currentBlock == var2) {
            var1.getLocationInWindow(var3);
            return var4.imageView;
         }
      } else if (var1 instanceof ArticleViewer.BlockVideoCell) {
         ArticleViewer.BlockVideoCell var8 = (ArticleViewer.BlockVideoCell)var1;
         if (var8.currentBlock == var2) {
            var1.getLocationInWindow(var3);
            return var8.imageView;
         }
      } else {
         ImageReceiver var5;
         if (var1 instanceof ArticleViewer.BlockCollageCell) {
            var5 = this.getImageReceiverFromListView(((ArticleViewer.BlockCollageCell)var1).innerListView, var2, var3);
            if (var5 != null) {
               return var5;
            }
         } else if (var1 instanceof ArticleViewer.BlockSlideshowCell) {
            var5 = this.getImageReceiverFromListView(((ArticleViewer.BlockSlideshowCell)var1).innerListView, var2, var3);
            if (var5 != null) {
               return var5;
            }
         } else if (var1 instanceof ArticleViewer.BlockListItemCell) {
            ArticleViewer.BlockListItemCell var6 = (ArticleViewer.BlockListItemCell)var1;
            if (var6.blockLayout != null) {
               var5 = this.getImageReceiverView(var6.blockLayout.itemView, var2, var3);
               if (var5 != null) {
                  return var5;
               }
            }
         } else if (var1 instanceof ArticleViewer.BlockOrderedListItemCell) {
            ArticleViewer.BlockOrderedListItemCell var7 = (ArticleViewer.BlockOrderedListItemCell)var1;
            if (var7.blockLayout != null) {
               var5 = this.getImageReceiverView(var7.blockLayout.itemView, var2, var3);
               if (var5 != null) {
                  return var5;
               }
            }
         }
      }

      return null;
   }

   public static ArticleViewer getInstance() {
      ArticleViewer var0 = Instance;
      ArticleViewer var1 = var0;
      if (var0 == null) {
         synchronized(ArticleViewer.class){}

         Throwable var10000;
         boolean var10001;
         label206: {
            try {
               var0 = Instance;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label206;
            }

            var1 = var0;
            if (var0 == null) {
               try {
                  var1 = new ArticleViewer();
                  Instance = var1;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label206;
               }
            }

            label193:
            try {
               return var1;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label193;
            }
         }

         while(true) {
            Throwable var22 = var10000;

            try {
               throw var22;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var1;
      }
   }

   private int getInstantLinkBackgroundColor() {
      int var1 = this.getSelectedColor();
      if (var1 != 0) {
         return var1 != 1 ? -14536904 : -2498337;
      } else {
         return -1707782;
      }
   }

   private View getLastNonListCell(View var1) {
      View var3;
      if (var1 instanceof ArticleViewer.BlockListItemCell) {
         ArticleViewer.BlockListItemCell var2 = (ArticleViewer.BlockListItemCell)var1;
         var3 = var1;
         if (var2.blockLayout != null) {
            return this.getLastNonListCell(var2.blockLayout.itemView);
         }
      } else {
         var3 = var1;
         if (var1 instanceof ArticleViewer.BlockOrderedListItemCell) {
            ArticleViewer.BlockOrderedListItemCell var4 = (ArticleViewer.BlockOrderedListItemCell)var1;
            var3 = var1;
            if (var4.blockLayout != null) {
               var3 = this.getLastNonListCell(var4.blockLayout.itemView);
            }
         }
      }

      return var3;
   }

   private TLRPC.PageBlock getLastNonListPageBlock(TLRPC.PageBlock var1) {
      if (var1 instanceof ArticleViewer.TL_pageBlockListItem) {
         ArticleViewer.TL_pageBlockListItem var4 = (ArticleViewer.TL_pageBlockListItem)var1;
         return var4.blockItem != null ? this.getLastNonListPageBlock(var4.blockItem) : var4.blockItem;
      } else {
         TLRPC.PageBlock var2 = var1;
         if (var1 instanceof ArticleViewer.TL_pageBlockOrderedListItem) {
            ArticleViewer.TL_pageBlockOrderedListItem var3 = (ArticleViewer.TL_pageBlockOrderedListItem)var1;
            if (var3.blockItem != null) {
               return this.getLastNonListPageBlock(var3.blockItem);
            }

            var2 = var3.blockItem;
         }

         return var2;
      }
   }

   private TLRPC.RichText getLastRichText(TLRPC.RichText var1) {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof TLRPC.TL_textFixed) {
         return this.getLastRichText(((TLRPC.TL_textFixed)var1).text);
      } else if (var1 instanceof TLRPC.TL_textItalic) {
         return this.getLastRichText(((TLRPC.TL_textItalic)var1).text);
      } else if (var1 instanceof TLRPC.TL_textBold) {
         return this.getLastRichText(((TLRPC.TL_textBold)var1).text);
      } else if (var1 instanceof TLRPC.TL_textUnderline) {
         return this.getLastRichText(((TLRPC.TL_textUnderline)var1).text);
      } else if (var1 instanceof TLRPC.TL_textStrike) {
         return this.getLastRichText(((TLRPC.TL_textStrike)var1).text);
      } else if (var1 instanceof TLRPC.TL_textEmail) {
         return this.getLastRichText(((TLRPC.TL_textEmail)var1).text);
      } else if (var1 instanceof TLRPC.TL_textUrl) {
         return this.getLastRichText(((TLRPC.TL_textUrl)var1).text);
      } else {
         TLRPC.RichText var2;
         if (var1 instanceof TLRPC.TL_textAnchor) {
            this.getLastRichText(((TLRPC.TL_textAnchor)var1).text);
            var2 = var1;
         } else {
            if (var1 instanceof TLRPC.TL_textSubscript) {
               return this.getLastRichText(((TLRPC.TL_textSubscript)var1).text);
            }

            if (var1 instanceof TLRPC.TL_textSuperscript) {
               return this.getLastRichText(((TLRPC.TL_textSuperscript)var1).text);
            }

            if (var1 instanceof TLRPC.TL_textMarked) {
               return this.getLastRichText(((TLRPC.TL_textMarked)var1).text);
            }

            var2 = var1;
            if (var1 instanceof TLRPC.TL_textPhone) {
               var1 = ((TLRPC.TL_textPhone)var1).text;

               try {
                  var2 = this.getLastRichText(var1);
               } catch (Throwable var3) {
                  throw var3;
               }
            }
         }

         return var2;
      }
   }

   private int getLinkTextColor() {
      int var1 = this.getSelectedColor();
      if (var1 != 0) {
         return var1 != 1 ? -10838585 : -13471296;
      } else {
         return -15435321;
      }
   }

   private TLObject getMedia(int var1) {
      if (!this.imagesArr.isEmpty() && var1 < this.imagesArr.size() && var1 >= 0) {
         TLRPC.PageBlock var2 = (TLRPC.PageBlock)this.imagesArr.get(var1);
         if (var2 instanceof TLRPC.TL_pageBlockPhoto) {
            return this.getPhotoWithId(((TLRPC.TL_pageBlockPhoto)var2).photo_id);
         }

         if (var2 instanceof TLRPC.TL_pageBlockVideo) {
            return this.getDocumentWithId(((TLRPC.TL_pageBlockVideo)var2).video_id);
         }
      }

      return null;
   }

   private File getMediaFile(int var1) {
      if (!this.imagesArr.isEmpty() && var1 < this.imagesArr.size() && var1 >= 0) {
         TLRPC.PageBlock var2 = (TLRPC.PageBlock)this.imagesArr.get(var1);
         if (var2 instanceof TLRPC.TL_pageBlockPhoto) {
            TLRPC.Photo var3 = this.getPhotoWithId(((TLRPC.TL_pageBlockPhoto)var2).photo_id);
            if (var3 != null) {
               TLRPC.PhotoSize var4 = FileLoader.getClosestPhotoSizeWithSize(var3.sizes, AndroidUtilities.getPhotoSize());
               if (var4 != null) {
                  return FileLoader.getPathToAttach(var4, true);
               }
            }
         } else if (var2 instanceof TLRPC.TL_pageBlockVideo) {
            TLRPC.Document var5 = this.getDocumentWithId(((TLRPC.TL_pageBlockVideo)var2).video_id);
            if (var5 != null) {
               return FileLoader.getPathToAttach(var5, true);
            }
         }
      }

      return null;
   }

   private String getMediaMime(int var1) {
      if (var1 < this.imagesArr.size() && var1 >= 0) {
         TLRPC.PageBlock var2 = (TLRPC.PageBlock)this.imagesArr.get(var1);
         if (var2 instanceof TLRPC.TL_pageBlockVideo) {
            TLRPC.Document var3 = this.getDocumentWithId(((TLRPC.TL_pageBlockVideo)var2).video_id);
            if (var3 != null) {
               return var3.mime_type;
            }
         }
      }

      return "image/jpeg";
   }

   private TLRPC.Photo getPhotoWithId(long var1) {
      TLRPC.WebPage var3 = this.currentPage;
      if (var3 != null && var3.cached_page != null) {
         TLRPC.Photo var5 = var3.photo;
         if (var5 != null && var5.id == var1) {
            return var5;
         }

         for(int var4 = 0; var4 < this.currentPage.cached_page.photos.size(); ++var4) {
            var5 = (TLRPC.Photo)this.currentPage.cached_page.photos.get(var4);
            if (var5.id == var1) {
               return var5;
            }
         }
      }

      return null;
   }

   private ArticleViewer.PlaceProviderObject getPlaceForPhoto(TLRPC.PageBlock var1) {
      ImageReceiver var2 = this.getImageReceiverFromListView(this.listView[0], var1, this.coords);
      if (var2 == null) {
         return null;
      } else {
         ArticleViewer.PlaceProviderObject var4 = new ArticleViewer.PlaceProviderObject();
         int[] var3 = this.coords;
         var4.viewX = var3[0];
         var4.viewY = var3[1];
         var4.parentView = this.listView[0];
         var4.imageReceiver = var2;
         var4.thumb = var2.getBitmapSafe();
         var4.radius = var2.getRoundRadius();
         var4.clipTopAddition = this.currentHeaderHeight;
         return var4;
      }
   }

   public static CharSequence getPlainText(TLRPC.RichText var0) {
      if (var0 == null) {
         return "";
      } else if (var0 instanceof TLRPC.TL_textFixed) {
         return getPlainText(((TLRPC.TL_textFixed)var0).text);
      } else if (var0 instanceof TLRPC.TL_textItalic) {
         return getPlainText(((TLRPC.TL_textItalic)var0).text);
      } else if (var0 instanceof TLRPC.TL_textBold) {
         return getPlainText(((TLRPC.TL_textBold)var0).text);
      } else if (var0 instanceof TLRPC.TL_textUnderline) {
         return getPlainText(((TLRPC.TL_textUnderline)var0).text);
      } else if (var0 instanceof TLRPC.TL_textStrike) {
         return getPlainText(((TLRPC.TL_textStrike)var0).text);
      } else if (var0 instanceof TLRPC.TL_textEmail) {
         return getPlainText(((TLRPC.TL_textEmail)var0).text);
      } else if (var0 instanceof TLRPC.TL_textUrl) {
         return getPlainText(((TLRPC.TL_textUrl)var0).text);
      } else if (var0 instanceof TLRPC.TL_textPlain) {
         return ((TLRPC.TL_textPlain)var0).text;
      } else if (var0 instanceof TLRPC.TL_textAnchor) {
         return getPlainText(((TLRPC.TL_textAnchor)var0).text);
      } else if (var0 instanceof TLRPC.TL_textEmpty) {
         return "";
      } else if (!(var0 instanceof TLRPC.TL_textConcat)) {
         if (var0 instanceof TLRPC.TL_textSubscript) {
            return getPlainText(((TLRPC.TL_textSubscript)var0).text);
         } else if (var0 instanceof TLRPC.TL_textSuperscript) {
            return getPlainText(((TLRPC.TL_textSuperscript)var0).text);
         } else if (var0 instanceof TLRPC.TL_textMarked) {
            return getPlainText(((TLRPC.TL_textMarked)var0).text);
         } else if (var0 instanceof TLRPC.TL_textPhone) {
            var0 = ((TLRPC.TL_textPhone)var0).text;

            try {
               CharSequence var5 = getPlainText(var0);
               return var5;
            } catch (Throwable var4) {
               throw var4;
            }
         } else {
            if (var0 instanceof TLRPC.TL_textImage) {
            }

            return "";
         }
      } else {
         StringBuilder var1 = new StringBuilder();
         int var2 = var0.texts.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            var1.append(getPlainText((TLRPC.RichText)var0.texts.get(var3)));
         }

         return var1;
      }
   }

   private int getSelectedColor() {
      int var1 = this.selectedColor;
      boolean var2 = this.nightModeEnabled;
      byte var3 = 2;
      int var4;
      if (var2 && var1 != 2) {
         if (Theme.selectedAutoNightType != 0) {
            if (Theme.isCurrentThemeNight()) {
               var4 = var3;
               return var4;
            }
         } else {
            int var5 = Calendar.getInstance().get(11);
            if (var5 >= 22) {
               var4 = var3;
               if (var5 <= 24) {
                  return var4;
               }
            }

            if (var5 >= 0 && var5 <= 6) {
               var4 = var3;
               return var4;
            }
         }
      }

      var4 = var1;
      return var4;
   }

   private CharSequence getText(View var1, TLRPC.RichText var2, TLRPC.RichText var3, TLRPC.PageBlock var4, int var5) {
      TLRPC.RichText var6 = null;
      if (var3 == null) {
         return null;
      } else if (var3 instanceof TLRPC.TL_textFixed) {
         return this.getText(var1, var2, ((TLRPC.TL_textFixed)var3).text, var4, var5);
      } else if (var3 instanceof TLRPC.TL_textItalic) {
         return this.getText(var1, var2, ((TLRPC.TL_textItalic)var3).text, var4, var5);
      } else if (var3 instanceof TLRPC.TL_textBold) {
         return this.getText(var1, var2, ((TLRPC.TL_textBold)var3).text, var4, var5);
      } else if (var3 instanceof TLRPC.TL_textUnderline) {
         return this.getText(var1, var2, ((TLRPC.TL_textUnderline)var3).text, var4, var5);
      } else if (var3 instanceof TLRPC.TL_textStrike) {
         return this.getText(var1, var2, ((TLRPC.TL_textStrike)var3).text, var4, var5);
      } else {
         TextPaint var22;
         StringBuilder var23;
         if (var3 instanceof TLRPC.TL_textEmail) {
            SpannableStringBuilder var35 = new SpannableStringBuilder(this.getText(var1, var2, ((TLRPC.TL_textEmail)var3).text, var4, var5));
            MetricAffectingSpan[] var36 = (MetricAffectingSpan[])var35.getSpans(0, var35.length(), MetricAffectingSpan.class);
            if (var35.length() != 0) {
               label146: {
                  if (var36 != null) {
                     var22 = var6;
                     if (var36.length != 0) {
                        break label146;
                     }
                  }

                  var22 = this.getTextPaint(var2, var3, var4);
               }

               var23 = new StringBuilder();
               var23.append("mailto:");
               var23.append(getUrl(var3));
               var35.setSpan(new TextPaintUrlSpan(var22, var23.toString()), 0, var35.length(), 33);
            }

            return var35;
         } else {
            MetricAffectingSpan[] var20;
            SpannableStringBuilder var30;
            if (var3 instanceof TLRPC.TL_textUrl) {
               TLRPC.TL_textUrl var33 = (TLRPC.TL_textUrl)var3;
               var30 = new SpannableStringBuilder(this.getText(var1, var2, var33.text, var4, var5));
               var20 = (MetricAffectingSpan[])var30.getSpans(0, var30.length(), MetricAffectingSpan.class);
               if (var20 != null && var20.length != 0) {
                  var22 = null;
               } else {
                  var22 = this.getTextPaint(var2, var3, var4);
               }

               Object var29;
               if (var33.webpage_id != 0L) {
                  var29 = new TextPaintWebpageUrlSpan(var22, getUrl(var3));
               } else {
                  var29 = new TextPaintUrlSpan(var22, getUrl(var3));
               }

               if (var30.length() != 0) {
                  var30.setSpan(var29, 0, var30.length(), 33);
               }

               return var30;
            } else if (var3 instanceof TLRPC.TL_textPlain) {
               return ((TLRPC.TL_textPlain)var3).text;
            } else if (var3 instanceof TLRPC.TL_textAnchor) {
               TLRPC.TL_textAnchor var25 = (TLRPC.TL_textAnchor)var3;
               SpannableStringBuilder var27 = new SpannableStringBuilder(this.getText(var1, var2, var25.text, var4, var5));
               var27.setSpan(new AnchorSpan(var25.name), 0, var27.length(), 17);
               return var27;
            } else if (var3 instanceof TLRPC.TL_textEmpty) {
               return "";
            } else {
               int var9;
               int var10;
               if (var3 instanceof TLRPC.TL_textConcat) {
                  SpannableStringBuilder var8 = new SpannableStringBuilder();
                  var9 = var3.texts.size();

                  for(var10 = 0; var10 < var9; ++var10) {
                     TLRPC.RichText var7 = (TLRPC.RichText)var3.texts.get(var10);
                     TLRPC.RichText var11 = this.getLastRichText(var7);
                     boolean var12;
                     if (var5 >= 0 && var7 instanceof TLRPC.TL_textUrl && ((TLRPC.TL_textUrl)var7).webpage_id != 0L) {
                        var12 = true;
                     } else {
                        var12 = false;
                     }

                     if (var12 && var8.length() != 0 && var8.charAt(var8.length() - 1) != '\n') {
                        var8.append(" ");
                     }

                     CharSequence var31 = this.getText(var1, var2, var7, var4, var5);
                     int var13 = this.getTextFlags(var11);
                     int var14 = var8.length();
                     var8.append(var31);
                     if (var13 != 0 && !(var31 instanceof SpannableStringBuilder)) {
                        if ((var13 & 8) == 0 && (var13 & 512) == 0) {
                           if (var14 != var8.length()) {
                              var8.setSpan(new TextPaintSpan(this.getTextPaint(var2, var11, var4)), var14, var8.length(), 33);
                           }
                        } else {
                           String var32 = getUrl(var7);
                           String var34 = var32;
                           if (var32 == null) {
                              var34 = getUrl(var2);
                           }

                           Object var37;
                           if ((var13 & 512) != 0) {
                              var37 = new TextPaintWebpageUrlSpan(this.getTextPaint(var2, var11, var4), var34);
                           } else {
                              var37 = new TextPaintUrlSpan(this.getTextPaint(var2, var11, var4), var34);
                           }

                           if (var14 != var8.length()) {
                              var8.setSpan(var37, var14, var8.length(), 33);
                           }
                        }
                     }

                     if (var12 && var10 != var9 - 1) {
                        var8.append(" ");
                     }
                  }

                  return var8;
               } else if (var3 instanceof TLRPC.TL_textSubscript) {
                  return this.getText(var1, var2, ((TLRPC.TL_textSubscript)var3).text, var4, var5);
               } else if (var3 instanceof TLRPC.TL_textSuperscript) {
                  return this.getText(var1, var2, ((TLRPC.TL_textSuperscript)var3).text, var4, var5);
               } else if (var3 instanceof TLRPC.TL_textMarked) {
                  var30 = new SpannableStringBuilder(this.getText(var1, var2, ((TLRPC.TL_textMarked)var3).text, var4, var5));
                  var20 = (MetricAffectingSpan[])var30.getSpans(0, var30.length(), MetricAffectingSpan.class);
                  if (var30.length() != 0) {
                     if (var20 != null && var20.length != 0) {
                        var22 = null;
                     } else {
                        var22 = this.getTextPaint(var2, var3, var4);
                     }

                     var30.setSpan(new TextPaintMarkSpan(var22), 0, var30.length(), 33);
                  }

                  return var30;
               } else if (var3 instanceof TLRPC.TL_textPhone) {
                  var6 = ((TLRPC.TL_textPhone)var3).text;

                  CharSequence var19;
                  try {
                     var19 = this.getText(var1, var2, var6, var4, var5);
                  } catch (Throwable var17) {
                     throw var17;
                  }

                  var30 = new SpannableStringBuilder(var19);
                  var20 = (MetricAffectingSpan[])var30.getSpans(0, var30.length(), MetricAffectingSpan.class);
                  if (var30.length() != 0) {
                     if (var20 != null && var20.length != 0) {
                        var22 = null;
                     } else {
                        var22 = this.getTextPaint(var2, var3, var4);
                     }

                     var23 = new StringBuilder();
                     var23.append("tel:");
                     var23.append(getUrl(var3));
                     var30.setSpan(new TextPaintUrlSpan(var22, var23.toString()), 0, var30.length(), 33);
                  }

                  return var30;
               } else if (var3 instanceof TLRPC.TL_textImage) {
                  TLRPC.TL_textImage var26 = (TLRPC.TL_textImage)var3;
                  TLRPC.Document var21 = this.getDocumentWithId(var26.document_id);
                  if (var21 != null) {
                     SpannableStringBuilder var24 = new SpannableStringBuilder("*");
                     var10 = AndroidUtilities.dp((float)var26.w);
                     var9 = AndroidUtilities.dp((float)var26.h);
                     var5 = Math.abs(var5);
                     if (var10 > var5) {
                        float var15 = (float)var5 / (float)var10;
                        var9 = (int)((float)var9 * var15);
                     } else {
                        var5 = var10;
                     }

                     TLRPC.WebPage var28 = this.currentPage;
                     boolean var16;
                     if (this.selectedColor == 2) {
                        var16 = true;
                     } else {
                        var16 = false;
                     }

                     var24.setSpan(new TextPaintImageReceiverSpan(var1, var21, var28, var5, var9, false, var16), 0, var24.length(), 33);
                     return var24;
                  } else {
                     return "";
                  }
               } else {
                  StringBuilder var18 = new StringBuilder();
                  var18.append("not supported ");
                  var18.append(var3);
                  return var18.toString();
               }
            }
         }
      }
   }

   private int getTextColor() {
      int var1 = this.getSelectedColor();
      return var1 != 0 && var1 != 1 ? -6710887 : -14606047;
   }

   private int getTextFlags(TLRPC.RichText var1) {
      if (var1 instanceof TLRPC.TL_textFixed) {
         return this.getTextFlags(var1.parentRichText) | 4;
      } else if (var1 instanceof TLRPC.TL_textItalic) {
         return this.getTextFlags(var1.parentRichText) | 2;
      } else if (var1 instanceof TLRPC.TL_textBold) {
         return this.getTextFlags(var1.parentRichText) | 1;
      } else if (var1 instanceof TLRPC.TL_textUnderline) {
         return this.getTextFlags(var1.parentRichText) | 16;
      } else if (var1 instanceof TLRPC.TL_textStrike) {
         return this.getTextFlags(var1.parentRichText) | 32;
      } else if (var1 instanceof TLRPC.TL_textEmail) {
         return this.getTextFlags(var1.parentRichText) | 8;
      } else if (var1 instanceof TLRPC.TL_textPhone) {
         return this.getTextFlags(var1.parentRichText) | 8;
      } else if (var1 instanceof TLRPC.TL_textUrl) {
         return ((TLRPC.TL_textUrl)var1).webpage_id != 0L ? this.getTextFlags(var1.parentRichText) | 512 : this.getTextFlags(var1.parentRichText) | 8;
      } else if (var1 instanceof TLRPC.TL_textSubscript) {
         return this.getTextFlags(var1.parentRichText) | 128;
      } else if (var1 instanceof TLRPC.TL_textSuperscript) {
         return this.getTextFlags(var1.parentRichText) | 256;
      } else if (var1 instanceof TLRPC.TL_textMarked) {
         return this.getTextFlags(var1.parentRichText) | 64;
      } else if (var1 != null) {
         var1 = var1.parentRichText;

         try {
            int var2 = this.getTextFlags(var1);
            return var2;
         } catch (Throwable var3) {
            throw var3;
         }
      } else {
         return 0;
      }
   }

   private TextPaint getTextPaint(TLRPC.RichText var1, TLRPC.RichText var2, TLRPC.PageBlock var3) {
      int var4;
      int var5;
      int var6;
      int var7;
      label277: {
         var4 = this.getTextFlags(var2);
         var5 = AndroidUtilities.dp(14.0F);
         var6 = this.selectedFontSize;
         if (var6 == 0) {
            var6 = AndroidUtilities.dp(4.0F);
         } else {
            if (var6 != 1) {
               if (var6 == 3) {
                  var7 = AndroidUtilities.dp(2.0F);
               } else if (var6 == 4) {
                  var7 = AndroidUtilities.dp(4.0F);
               } else {
                  var7 = 0;
               }
               break label277;
            }

            var6 = AndroidUtilities.dp(2.0F);
         }

         var7 = -var6;
      }

      boolean var8 = var3 instanceof TLRPC.TL_pageBlockPhoto;
      TLRPC.RichText var9 = null;
      int var10;
      SparseArray var12;
      if (var8) {
         var9 = ((TLRPC.TL_pageBlockPhoto)var3).caption.text;
         if (var9 != var2 && var9 != var1) {
            var12 = photoCreditTextPaints;
            var6 = AndroidUtilities.dp(12.0F);
         } else {
            var12 = photoCaptionTextPaints;
            var6 = AndroidUtilities.dp(14.0F);
         }

         var5 = var6;
         var6 = this.getGrayTextColor();
      } else if (var3 instanceof TLRPC.TL_pageBlockMap) {
         var9 = ((TLRPC.TL_pageBlockMap)var3).caption.text;
         if (var9 != var2 && var9 != var1) {
            var12 = photoCreditTextPaints;
            var6 = AndroidUtilities.dp(12.0F);
         } else {
            var12 = photoCaptionTextPaints;
            var6 = AndroidUtilities.dp(14.0F);
         }

         var5 = var6;
         var6 = this.getGrayTextColor();
      } else if (var3 instanceof TLRPC.TL_pageBlockTitle) {
         var12 = titleTextPaints;
         var5 = AndroidUtilities.dp(24.0F);
         var6 = this.getTextColor();
      } else if (var3 instanceof TLRPC.TL_pageBlockKicker) {
         var12 = kickerTextPaints;
         var5 = AndroidUtilities.dp(14.0F);
         var6 = this.getTextColor();
      } else if (var3 instanceof TLRPC.TL_pageBlockAuthorDate) {
         var12 = authorTextPaints;
         var5 = AndroidUtilities.dp(14.0F);
         var6 = this.getGrayTextColor();
      } else if (var3 instanceof TLRPC.TL_pageBlockFooter) {
         var12 = footerTextPaints;
         var5 = AndroidUtilities.dp(14.0F);
         var6 = this.getGrayTextColor();
      } else if (var3 instanceof TLRPC.TL_pageBlockSubtitle) {
         var12 = subtitleTextPaints;
         var5 = AndroidUtilities.dp(21.0F);
         var6 = this.getTextColor();
      } else if (var3 instanceof TLRPC.TL_pageBlockHeader) {
         var12 = headerTextPaints;
         var5 = AndroidUtilities.dp(21.0F);
         var6 = this.getTextColor();
      } else if (var3 instanceof TLRPC.TL_pageBlockSubheader) {
         var12 = subheaderTextPaints;
         var5 = AndroidUtilities.dp(18.0F);
         var6 = this.getTextColor();
      } else {
         label252: {
            if (var3 instanceof TLRPC.TL_pageBlockBlockquote) {
               TLRPC.TL_pageBlockBlockquote var13 = (TLRPC.TL_pageBlockBlockquote)var3;
               if (var13.text == var1) {
                  var12 = quoteTextPaints;
                  var5 = AndroidUtilities.dp(15.0F);
                  var6 = this.getTextColor();
                  break label252;
               }

               if (var13.caption == var1) {
                  var12 = photoCaptionTextPaints;
                  var5 = AndroidUtilities.dp(14.0F);
                  var6 = this.getGrayTextColor();
                  break label252;
               }
            } else if (var3 instanceof TLRPC.TL_pageBlockPullquote) {
               TLRPC.TL_pageBlockPullquote var14 = (TLRPC.TL_pageBlockPullquote)var3;
               if (var14.text == var1) {
                  var12 = quoteTextPaints;
                  var5 = AndroidUtilities.dp(15.0F);
                  var6 = this.getTextColor();
                  break label252;
               }

               if (var14.caption == var1) {
                  var12 = photoCaptionTextPaints;
                  var5 = AndroidUtilities.dp(14.0F);
                  var6 = this.getGrayTextColor();
                  break label252;
               }
            } else {
               if (var3 instanceof TLRPC.TL_pageBlockPreformatted) {
                  var12 = preformattedTextPaints;
                  var5 = AndroidUtilities.dp(14.0F);
                  var6 = this.getTextColor();
                  break label252;
               }

               if (var3 instanceof TLRPC.TL_pageBlockParagraph) {
                  var12 = paragraphTextPaints;
                  var5 = AndroidUtilities.dp(16.0F);
                  var6 = this.getTextColor();
                  break label252;
               }

               if (this.isListItemBlock(var3)) {
                  var12 = listTextPaints;
                  var5 = AndroidUtilities.dp(16.0F);
                  var6 = this.getTextColor();
                  break label252;
               }

               if (var3 instanceof TLRPC.TL_pageBlockEmbed) {
                  var9 = ((TLRPC.TL_pageBlockEmbed)var3).caption.text;
                  if (var9 != var2 && var9 != var1) {
                     var12 = photoCreditTextPaints;
                     var6 = AndroidUtilities.dp(12.0F);
                  } else {
                     var12 = photoCaptionTextPaints;
                     var6 = AndroidUtilities.dp(14.0F);
                  }

                  var5 = var6;
                  var6 = this.getGrayTextColor();
                  break label252;
               }

               if (var3 instanceof TLRPC.TL_pageBlockSlideshow) {
                  var9 = ((TLRPC.TL_pageBlockSlideshow)var3).caption.text;
                  if (var9 != var2 && var9 != var1) {
                     var12 = photoCreditTextPaints;
                     var6 = AndroidUtilities.dp(12.0F);
                  } else {
                     var12 = photoCaptionTextPaints;
                     var6 = AndroidUtilities.dp(14.0F);
                  }

                  var5 = var6;
                  var6 = this.getGrayTextColor();
                  break label252;
               }

               if (var3 instanceof TLRPC.TL_pageBlockCollage) {
                  var9 = ((TLRPC.TL_pageBlockCollage)var3).caption.text;
                  if (var9 != var2 && var9 != var1) {
                     var12 = photoCreditTextPaints;
                     var6 = AndroidUtilities.dp(12.0F);
                  } else {
                     var12 = photoCaptionTextPaints;
                     var6 = AndroidUtilities.dp(14.0F);
                  }

                  var5 = var6;
                  var6 = this.getGrayTextColor();
                  break label252;
               }

               if (!(var3 instanceof TLRPC.TL_pageBlockEmbedPost)) {
                  if (var3 instanceof TLRPC.TL_pageBlockVideo) {
                     if (var2 == ((TLRPC.TL_pageBlockVideo)var3).caption.text) {
                        var12 = mediaCaptionTextPaints;
                        var6 = AndroidUtilities.dp(14.0F);
                        var5 = this.getTextColor();
                     } else {
                        var12 = mediaCreditTextPaints;
                        var6 = AndroidUtilities.dp(12.0F);
                        var5 = this.getTextColor();
                     }
                  } else {
                     if (!(var3 instanceof TLRPC.TL_pageBlockAudio)) {
                        if (var3 instanceof TLRPC.TL_pageBlockRelatedArticles) {
                           var12 = relatedArticleTextPaints;
                           var5 = AndroidUtilities.dp(15.0F);
                           var6 = this.getGrayTextColor();
                        } else if (var3 instanceof TLRPC.TL_pageBlockDetails) {
                           var12 = detailsTextPaints;
                           var5 = AndroidUtilities.dp(15.0F);
                           var6 = this.getTextColor();
                        } else if (var3 instanceof TLRPC.TL_pageBlockTable) {
                           var12 = tableTextPaints;
                           var5 = AndroidUtilities.dp(15.0F);
                           var6 = this.getTextColor();
                        } else {
                           var12 = null;
                           var6 = -65536;
                        }
                        break label252;
                     }

                     if (var2 == ((TLRPC.TL_pageBlockAudio)var3).caption.text) {
                        var12 = mediaCaptionTextPaints;
                        var6 = AndroidUtilities.dp(14.0F);
                        var5 = this.getTextColor();
                     } else {
                        var12 = mediaCreditTextPaints;
                        var6 = AndroidUtilities.dp(12.0F);
                        var5 = this.getTextColor();
                     }
                  }

                  var10 = var6;
                  var6 = var5;
                  var5 = var10;
                  break label252;
               }

               TLRPC.TL_pageCaption var16 = ((TLRPC.TL_pageBlockEmbedPost)var3).caption;
               if (var2 == var16.text) {
                  var12 = photoCaptionTextPaints;
                  var5 = AndroidUtilities.dp(14.0F);
                  var6 = this.getGrayTextColor();
                  break label252;
               }

               if (var2 == var16.credit) {
                  var12 = photoCreditTextPaints;
                  var5 = AndroidUtilities.dp(12.0F);
                  var6 = this.getGrayTextColor();
                  break label252;
               }

               if (var2 != null) {
                  var12 = embedPostTextPaints;
                  var5 = AndroidUtilities.dp(14.0F);
                  var6 = this.getTextColor();
                  break label252;
               }
            }

            var6 = -65536;
            var12 = var9;
         }
      }

      int var11;
      label202: {
         var11 = var4 & 256;
         if (var11 == 0) {
            var10 = var5;
            if ((var4 & 128) == 0) {
               break label202;
            }
         }

         var10 = var5 - AndroidUtilities.dp(4.0F);
      }

      if (var12 == null) {
         if (errorTextPaint == null) {
            errorTextPaint = new TextPaint(1);
            errorTextPaint.setColor(-65536);
         }

         errorTextPaint.setTextSize((float)AndroidUtilities.dp(14.0F));
         return errorTextPaint;
      } else {
         TextPaint var17 = (TextPaint)var12.get(var4);
         TextPaint var15 = var17;
         if (var17 == null) {
            var15 = new TextPaint(1);
            if ((var4 & 4) != 0) {
               var15.setTypeface(AndroidUtilities.getTypeface("fonts/rmono.ttf"));
            } else if (var3 instanceof TLRPC.TL_pageBlockRelatedArticles) {
               var15.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            } else if (this.selectedFont != 1 && !(var3 instanceof TLRPC.TL_pageBlockTitle) && !(var3 instanceof TLRPC.TL_pageBlockKicker) && !(var3 instanceof TLRPC.TL_pageBlockHeader) && !(var3 instanceof TLRPC.TL_pageBlockSubtitle) && !(var3 instanceof TLRPC.TL_pageBlockSubheader)) {
               var5 = var4 & 1;
               if (var5 != 0 && (var4 & 2) != 0) {
                  var15.setTypeface(AndroidUtilities.getTypeface("fonts/rmediumitalic.ttf"));
               } else if (var5 != 0) {
                  var15.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
               } else if ((var4 & 2) != 0) {
                  var15.setTypeface(AndroidUtilities.getTypeface("fonts/ritalic.ttf"));
               }
            } else {
               var5 = var4 & 1;
               if (var5 != 0 && (var4 & 2) != 0) {
                  var15.setTypeface(Typeface.create("serif", 3));
               } else if (var5 != 0) {
                  var15.setTypeface(Typeface.create("serif", 1));
               } else if ((var4 & 2) != 0) {
                  var15.setTypeface(Typeface.create("serif", 2));
               } else {
                  var15.setTypeface(Typeface.create("serif", 0));
               }
            }

            if ((var4 & 32) != 0) {
               var15.setFlags(var15.getFlags() | 16);
            }

            if ((var4 & 16) != 0) {
               var15.setFlags(var15.getFlags() | 8);
            }

            if ((var4 & 8) != 0 || (var4 & 512) != 0) {
               var15.setFlags(var15.getFlags());
               var6 = this.getLinkTextColor();
            }

            if (var11 != 0) {
               var15.baselineShift -= AndroidUtilities.dp(6.0F);
            } else if ((var4 & 128) != 0) {
               var15.baselineShift += AndroidUtilities.dp(2.0F);
            }

            var15.setColor(var6);
            var12.put(var4, var15);
         }

         var15.setTextSize((float)(var10 + var7));
         return var15;
      }
   }

   public static String getUrl(TLRPC.RichText var0) {
      if (var0 instanceof TLRPC.TL_textFixed) {
         return getUrl(((TLRPC.TL_textFixed)var0).text);
      } else if (var0 instanceof TLRPC.TL_textItalic) {
         return getUrl(((TLRPC.TL_textItalic)var0).text);
      } else if (var0 instanceof TLRPC.TL_textBold) {
         return getUrl(((TLRPC.TL_textBold)var0).text);
      } else if (var0 instanceof TLRPC.TL_textUnderline) {
         return getUrl(((TLRPC.TL_textUnderline)var0).text);
      } else if (var0 instanceof TLRPC.TL_textStrike) {
         var0 = ((TLRPC.TL_textStrike)var0).text;

         try {
            String var2 = getUrl(var0);
            return var2;
         } catch (Throwable var1) {
            throw var1;
         }
      } else if (var0 instanceof TLRPC.TL_textEmail) {
         return ((TLRPC.TL_textEmail)var0).email;
      } else if (var0 instanceof TLRPC.TL_textUrl) {
         return ((TLRPC.TL_textUrl)var0).url;
      } else {
         return var0 instanceof TLRPC.TL_textPhone ? ((TLRPC.TL_textPhone)var0).phone : null;
      }
   }

   private void goToNext() {
      float var1;
      if (this.scale != 1.0F) {
         var1 = (float)((this.getContainerViewWidth() - this.centerImage.getImageWidth()) / 2) * this.scale;
      } else {
         var1 = 0.0F;
      }

      this.switchImageAfterAnimation = 1;
      this.animateTo(this.scale, this.minX - (float)this.getContainerViewWidth() - var1 - (float)(AndroidUtilities.dp(30.0F) / 2), this.translationY, false);
   }

   private void goToPrev() {
      float var1;
      if (this.scale != 1.0F) {
         var1 = (float)((this.getContainerViewWidth() - this.centerImage.getImageWidth()) / 2) * this.scale;
      } else {
         var1 = 0.0F;
      }

      this.switchImageAfterAnimation = 2;
      this.animateTo(this.scale, this.maxX + (float)this.getContainerViewWidth() + var1 + (float)(AndroidUtilities.dp(30.0F) / 2), this.translationY, false);
   }

   public static boolean hasInstance() {
      boolean var0;
      if (Instance != null) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   private boolean isListItemBlock(TLRPC.PageBlock var1) {
      boolean var2;
      if (!(var1 instanceof ArticleViewer.TL_pageBlockListItem) && !(var1 instanceof ArticleViewer.TL_pageBlockOrderedListItem)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private boolean isMediaVideo(int var1) {
      boolean var2;
      if (!this.imagesArr.isEmpty() && var1 < this.imagesArr.size() && var1 >= 0 && this.isVideoBlock((TLRPC.PageBlock)this.imagesArr.get(var1))) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private boolean isVideoBlock(TLRPC.PageBlock var1) {
      if (var1 instanceof TLRPC.TL_pageBlockVideo) {
         TLRPC.Document var2 = this.getDocumentWithId(((TLRPC.TL_pageBlockVideo)var1).video_id);
         if (var2 != null) {
            return MessageObject.isVideoDocument(var2);
         }
      }

      return false;
   }

   private void joinChannel(ArticleViewer.BlockChannelCell var1, TLRPC.Chat var2) {
      TLRPC.TL_channels_joinChannel var3 = new TLRPC.TL_channels_joinChannel();
      var3.channel = MessagesController.getInputChannel(var2);
      int var4 = UserConfig.selectedAccount;
      ConnectionsManager.getInstance(var4).sendRequest(var3, new _$$Lambda$ArticleViewer$tOg7TGz_CemIZKoQ4SmkPVL_N7w(this, var1, var4, var3, var2));
   }

   // $FF: synthetic method
   static void lambda$null$33(ArticleViewer.BlockChannelCell var0) {
      var0.setState(2, false);
   }

   // $FF: synthetic method
   static void lambda$null$34(int var0, TLRPC.Chat var1) {
      MessagesController.getInstance(var0).loadFullChat(var1.id, 0, true);
   }

   // $FF: synthetic method
   static boolean lambda$setParentActivity$12(View var0, MotionEvent var1) {
      return true;
   }

   private void loadChannel(ArticleViewer.BlockChannelCell var1, ArticleViewer.WebpageAdapter var2, TLRPC.Chat var3) {
      if (!this.loadingChannel && !TextUtils.isEmpty(var3.username)) {
         this.loadingChannel = true;
         TLRPC.TL_contacts_resolveUsername var4 = new TLRPC.TL_contacts_resolveUsername();
         var4.username = var3.username;
         int var5 = UserConfig.selectedAccount;
         ConnectionsManager.getInstance(var5).sendRequest(var4, new _$$Lambda$ArticleViewer$OTnRhboivaBmKveLaMpnewhsYJg(this, var2, var5, var1));
      }

   }

   private void onActionClick(boolean var1) {
      TLObject var2 = this.getMedia(this.currentIndex);
      if (var2 instanceof TLRPC.Document && this.currentFileNames[0] != null) {
         TLRPC.Document var3 = (TLRPC.Document)var2;
         TLRPC.PageBlock var4 = this.currentMedia;
         Object var5 = null;
         File var6 = (File)var5;
         if (var4 != null) {
            var6 = this.getMediaFile(this.currentIndex);
            if (var6 != null && !var6.exists()) {
               var6 = (File)var5;
            }
         }

         if (var6 == null) {
            if (var1) {
               if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[0])) {
                  FileLoader.getInstance(this.currentAccount).loadFile(var3, this.currentPage, 1, 1);
               } else {
                  FileLoader.getInstance(this.currentAccount).cancelLoadFile(var3);
               }
            }
         } else {
            this.preparePlayer(var6, true);
         }
      }

   }

   private void onClosed() {
      this.isVisible = false;
      this.currentPage = null;

      int var1;
      for(var1 = 0; var1 < this.listView.length; ++var1) {
         this.adapter[var1].cleanup();
      }

      try {
         this.parentActivity.getWindow().clearFlags(128);
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

      for(var1 = 0; var1 < this.createdWebViews.size(); ++var1) {
         ((ArticleViewer.BlockEmbedCell)this.createdWebViews.get(var1)).destroyWebView(false);
      }

      this.containerView.post(new _$$Lambda$ArticleViewer$7Sl00UfgZjsIK9QPR7pwpUGqEew(this));
   }

   private void onPhotoClosed(ArticleViewer.PlaceProviderObject var1) {
      this.isPhotoVisible = false;
      this.disableShowCheck = true;
      this.currentMedia = null;
      ImageReceiver.BitmapHolder var2 = this.currentThumb;
      if (var2 != null) {
         var2.release();
         this.currentThumb = null;
      }

      AnimatedFileDrawable var4 = this.currentAnimation;
      if (var4 != null) {
         var4.setSecondParentView((View)null);
         this.currentAnimation = null;
      }

      for(int var3 = 0; var3 < 3; ++var3) {
         ArticleViewer.RadialProgressView[] var5 = this.radialProgressViews;
         if (var5[var3] != null) {
            var5[var3].setBackgroundState(-1, false);
         }
      }

      this.centerImage.setImageBitmap((Bitmap)null);
      this.leftImage.setImageBitmap((Bitmap)null);
      this.rightImage.setImageBitmap((Bitmap)null);
      this.photoContainerView.post(new _$$Lambda$ArticleViewer$RbiUQ9E_b8OvcTg7QvMaKqKT5rU(this));
      this.disableShowCheck = false;
      if (var1 != null) {
         var1.imageReceiver.setVisible(true, true);
      }

      this.groupedPhotosListView.clear();
   }

   private void onPhotoShow(int var1, ArticleViewer.PlaceProviderObject var2) {
      this.currentIndex = -1;
      String[] var3 = this.currentFileNames;
      var3[0] = null;
      var3[1] = null;
      var3[2] = null;
      ImageReceiver.BitmapHolder var6 = this.currentThumb;
      if (var6 != null) {
         var6.release();
      }

      ImageReceiver.BitmapHolder var5;
      if (var2 != null) {
         var5 = var2.thumb;
      } else {
         var5 = null;
      }

      this.currentThumb = var5;
      this.menuItem.setVisibility(0);
      this.menuItem.hideSubItem(3);
      this.actionBar.setTranslationY(0.0F);
      this.captionTextView.setTag((Object)null);
      this.captionTextView.setVisibility(8);

      for(int var4 = 0; var4 < 3; ++var4) {
         ArticleViewer.RadialProgressView[] var7 = this.radialProgressViews;
         if (var7[var4] != null) {
            var7[var4].setBackgroundState(-1, false);
         }
      }

      this.setImageIndex(var1, true);
      if (this.currentMedia != null && this.isMediaVideo(this.currentIndex)) {
         this.onActionClick(false);
      }

   }

   private void onSharePressed() {
      if (this.parentActivity != null && this.currentMedia != null) {
         Exception var10000;
         label65: {
            File var1;
            boolean var10001;
            try {
               var1 = this.getMediaFile(this.currentIndex);
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label65;
            }

            int var3;
            Intent var12;
            label56: {
               if (var1 != null) {
                  try {
                     if (var1.exists()) {
                        var12 = new Intent("android.intent.action.SEND");
                        var12.setType(this.getMediaMime(this.currentIndex));
                        var3 = VERSION.SDK_INT;
                        break label56;
                     }
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label65;
                  }
               }

               try {
                  AlertDialog.Builder var2 = new AlertDialog.Builder(this.parentActivity);
                  var2.setTitle(LocaleController.getString("AppName", 2131558635));
                  var2.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                  var2.setMessage(LocaleController.getString("PleaseDownload", 2131560454));
                  this.showDialog(var2.create());
                  return;
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label65;
               }
            }

            if (var3 >= 24) {
               try {
                  var12.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this.parentActivity, "org.telegram.messenger.provider", var1));
                  var12.setFlags(1);
               } catch (Exception var8) {
                  try {
                     var12.putExtra("android.intent.extra.STREAM", Uri.fromFile(var1));
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label65;
                  }
               }
            } else {
               try {
                  var12.putExtra("android.intent.extra.STREAM", Uri.fromFile(var1));
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label65;
               }
            }

            try {
               this.parentActivity.startActivityForResult(Intent.createChooser(var12, LocaleController.getString("ShareFile", 2131560748)), 500);
               return;
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
            }
         }

         Exception var13 = var10000;
         FileLog.e((Throwable)var13);
      }

   }

   private boolean open(MessageObject var1, TLRPC.WebPage var2, String var3, boolean var4) {
      if (this.parentActivity != null && (!this.isVisible || this.collapsed) && (var1 != null || var2 != null)) {
         TLRPC.WebPage var5 = var2;
         if (var1 != null) {
            var5 = var1.messageOwner.media.webpage;
         }

         int var6;
         Exception var10000;
         boolean var10001;
         String var21;
         TLRPC.WebPage var24;
         label180: {
            if (var1 != null) {
               var5 = var1.messageOwner.media.webpage;
               var6 = 0;

               while(true) {
                  label147: {
                     if (var6 < var1.messageOwner.entities.size()) {
                        label172: {
                           TLRPC.MessageEntity var23 = (TLRPC.MessageEntity)var1.messageOwner.entities.get(var6);
                           if (!(var23 instanceof TLRPC.TL_messageEntityUrl)) {
                              break label147;
                           }

                           label173: {
                              label184: {
                                 try {
                                    var3 = var1.messageOwner.message.substring(var23.offset, var23.offset + var23.length).toLowerCase();
                                    if (!TextUtils.isEmpty(var5.cached_page.url)) {
                                       var21 = var5.cached_page.url.toLowerCase();
                                       break label184;
                                    }
                                 } catch (Exception var17) {
                                    var10000 = var17;
                                    var10001 = false;
                                    break label173;
                                 }

                                 try {
                                    var21 = var5.url.toLowerCase();
                                 } catch (Exception var16) {
                                    var10000 = var16;
                                    var10001 = false;
                                    break label173;
                                 }
                              }

                              try {
                                 if (!var3.contains(var21) && !var21.contains(var3)) {
                                    break label147;
                                 }
                              } catch (Exception var15) {
                                 var10000 = var15;
                                 var10001 = false;
                                 break label173;
                              }

                              int var7;
                              try {
                                 var7 = var3.lastIndexOf(35);
                              } catch (Exception var14) {
                                 var10000 = var14;
                                 var10001 = false;
                                 break label173;
                              }

                              if (var7 == -1) {
                                 break label172;
                              }

                              try {
                                 var21 = var3.substring(var7 + 1);
                                 break;
                              } catch (Exception var13) {
                                 var10000 = var13;
                                 var10001 = false;
                              }
                           }

                           Exception var25 = var10000;
                           FileLog.e((Throwable)var25);
                           break label147;
                        }
                     }

                     var21 = null;
                     break;
                  }

                  ++var6;
               }
            } else {
               label177: {
                  if (var3 != null) {
                     var6 = var3.lastIndexOf(35);
                     if (var6 != -1) {
                        var21 = var3.substring(var6 + 1);
                        break label177;
                     }
                  }

                  var24 = var5;
                  var21 = null;
                  break label180;
               }
            }

            var24 = var5;
         }

         this.pagesStack.clear();
         this.collapsed = false;
         this.backDrawable.setRotation(0.0F, false);
         this.containerView.setTranslationX(0.0F);
         this.containerView.setTranslationY(0.0F);
         this.listView[0].setTranslationY(0.0F);
         this.listView[0].setTranslationX(0.0F);
         this.listView[1].setTranslationX(0.0F);
         this.listView[0].setAlpha(1.0F);
         this.windowView.setInnerTranslationX(0.0F);
         this.actionBar.setVisibility(8);
         this.bottomLayout.setVisibility(8);
         this.captionTextView.setVisibility(8);
         this.captionTextViewNext.setVisibility(8);
         this.layoutManager[0].scrollToPositionWithOffset(0, 0);
         if (var4) {
            this.setCurrentHeaderHeight(AndroidUtilities.dp(56.0F));
         } else {
            this.checkScrollAnimated();
         }

         boolean var8 = this.addPageToStack(var24, var21, 0);
         if (var4) {
            if (var8 || var21 == null) {
               var21 = null;
            }

            TLRPC.TL_messages_getWebPage var9 = new TLRPC.TL_messages_getWebPage();
            var9.url = var24.url;
            TLRPC.Page var27 = var24.cached_page;
            if (!(var27 instanceof TLRPC.TL_pagePart_layer82) && !var27.part) {
               var9.hash = var24.hash;
            } else {
               var9.hash = 0;
            }

            var6 = UserConfig.selectedAccount;
            ConnectionsManager.getInstance(var6).sendRequest(var9, new _$$Lambda$ArticleViewer$z8m1_SXnv_7lkJNDb6qlQTHMjFc(this, var24, var1, var21, var6));
         }

         this.lastInsets = null;
         if (!this.isVisible) {
            label175: {
               WindowManager var19 = (WindowManager)this.parentActivity.getSystemService("window");
               if (this.attachedToWindow) {
                  try {
                     var19.removeView(this.windowView);
                  } catch (Exception var10) {
                  }
               }

               label95: {
                  try {
                     if (VERSION.SDK_INT >= 21) {
                        this.windowLayoutParams.flags = -2147417856;
                        if (VERSION.SDK_INT >= 28) {
                           this.windowLayoutParams.layoutInDisplayCutoutMode = 1;
                        }
                     }
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label95;
                  }

                  try {
                     LayoutParams var26 = this.windowLayoutParams;
                     var26.flags |= 1032;
                     this.windowView.setFocusable(false);
                     this.containerView.setFocusable(false);
                     var19.addView(this.windowView, this.windowLayoutParams);
                     break label175;
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                  }
               }

               Exception var20 = var10000;
               FileLog.e((Throwable)var20);
               return false;
            }
         } else {
            LayoutParams var18 = this.windowLayoutParams;
            var18.flags &= -17;
            ((WindowManager)this.parentActivity.getSystemService("window")).updateViewLayout(this.windowView, this.windowLayoutParams);
         }

         this.isVisible = true;
         this.animationInProgress = 1;
         this.windowView.setAlpha(0.0F);
         this.containerView.setAlpha(0.0F);
         AnimatorSet var22 = new AnimatorSet();
         var22.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.windowView, View.ALPHA, new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.containerView, View.ALPHA, new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.windowView, View.TRANSLATION_X, new float[]{(float)AndroidUtilities.dp(56.0F), 0.0F})});
         this.animationEndRunnable = new _$$Lambda$ArticleViewer$DFl_wS8sRaC6wF2T9h1iNHqI_KI(this);
         var22.setDuration(150L);
         var22.setInterpolator(this.interpolator);
         var22.addListener(new AnimatorListenerAdapter() {
            // $FF: synthetic method
            public void lambda$onAnimationEnd$0$ArticleViewer$13() {
               NotificationCenter.getInstance(ArticleViewer.this.currentAccount).setAnimationInProgress(false);
               if (ArticleViewer.this.animationEndRunnable != null) {
                  ArticleViewer.this.animationEndRunnable.run();
                  ArticleViewer.this.animationEndRunnable = null;
               }

            }

            public void onAnimationEnd(Animator var1) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$13$45D0IaozkG_I_gCXpiUTnqoe_0k(this));
            }
         });
         this.transitionAnimationStartTime = System.currentTimeMillis();
         AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$AbuoJEBmR86qlkjB83QNafOTaB8(this, var22));
         if (VERSION.SDK_INT >= 18) {
            this.containerView.setLayerType(2, (Paint)null);
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean openAllParentBlocks(ArticleViewer.TL_pageBlockDetailsChild var1) {
      TLRPC.PageBlock var6 = this.getLastNonListPageBlock(var1.parent);
      boolean var2 = var6 instanceof TLRPC.TL_pageBlockDetails;
      boolean var3 = false;
      if (var2) {
         TLRPC.TL_pageBlockDetails var7 = (TLRPC.TL_pageBlockDetails)var6;
         if (!var7.open) {
            var7.open = true;
            return true;
         } else {
            return false;
         }
      } else {
         var2 = var3;
         if (var6 instanceof ArticleViewer.TL_pageBlockDetailsChild) {
            boolean var5;
            label27: {
               var1 = (ArticleViewer.TL_pageBlockDetailsChild)var6;
               TLRPC.PageBlock var4 = this.getLastNonListPageBlock(var1.block);
               if (var4 instanceof TLRPC.TL_pageBlockDetails) {
                  TLRPC.TL_pageBlockDetails var8 = (TLRPC.TL_pageBlockDetails)var4;
                  if (!var8.open) {
                     var8.open = true;
                     var5 = true;
                     break label27;
                  }
               }

               var5 = false;
            }

            if (!this.openAllParentBlocks(var1)) {
               var2 = var3;
               if (!var5) {
                  return var2;
               }
            }

            var2 = true;
         }

         return var2;
      }
   }

   private void openPreviewsChat(TLRPC.User var1, long var2) {
      if (var1 != null && this.parentActivity != null) {
         Bundle var4 = new Bundle();
         var4.putInt("user_id", var1.id);
         StringBuilder var5 = new StringBuilder();
         var5.append("webpage");
         var5.append(var2);
         var4.putString("botUser", var5.toString());
         ((LaunchActivity)this.parentActivity).presentFragment(new ChatActivity(var4), false, true);
         this.close(false, true);
      }

   }

   private void openWebpageUrl(String var1, String var2) {
      if (this.openUrlReqId != 0) {
         ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.openUrlReqId, false);
         this.openUrlReqId = 0;
      }

      int var3 = this.lastReqId + 1;
      this.lastReqId = var3;
      this.closePhoto(false);
      this.showProgressView(true, true);
      TLRPC.TL_messages_getWebPage var4 = new TLRPC.TL_messages_getWebPage();
      var4.url = var1;
      var4.hash = 0;
      this.openUrlReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var4, new _$$Lambda$ArticleViewer$CIJ3NzkK6eZMINaSMugn6BEySDI(this, var3, var2, var4));
   }

   @SuppressLint({"NewApi"})
   private void preparePlayer(File var1, boolean var2) {
      if (this.parentActivity != null) {
         this.releasePlayer();
         if (this.videoTextureView == null) {
            this.aspectRatioFrameLayout = new AspectRatioFrameLayout(this.parentActivity);
            this.aspectRatioFrameLayout.setVisibility(4);
            this.photoContainerView.addView(this.aspectRatioFrameLayout, 0, LayoutHelper.createFrame(-1, -1, 17));
            this.videoTextureView = new TextureView(this.parentActivity);
            this.videoTextureView.setOpaque(false);
            this.aspectRatioFrameLayout.addView(this.videoTextureView, LayoutHelper.createFrame(-1, -1, 17));
         }

         this.textureUploaded = false;
         this.videoCrossfadeStarted = false;
         TextureView var3 = this.videoTextureView;
         this.videoCrossfadeAlpha = 0.0F;
         var3.setAlpha(0.0F);
         this.videoPlayButton.setImageResource(2131165479);
         if (this.videoPlayer == null) {
            this.videoPlayer = new VideoPlayer();
            this.videoPlayer.setTextureView(this.videoTextureView);
            this.videoPlayer.setDelegate(new VideoPlayer.VideoPlayerDelegate() {
               public void onError(Exception var1) {
                  FileLog.e((Throwable)var1);
               }

               public void onRenderedFirstFrame() {
                  if (!ArticleViewer.this.textureUploaded) {
                     ArticleViewer.this.textureUploaded = true;
                     ArticleViewer.this.containerView.invalidate();
                  }

               }

               public void onStateChanged(boolean var1, int var2) {
                  if (ArticleViewer.this.videoPlayer != null) {
                     if (var2 != 4 && var2 != 1) {
                        try {
                           ArticleViewer.this.parentActivity.getWindow().addFlags(128);
                        } catch (Exception var5) {
                           FileLog.e((Throwable)var5);
                        }
                     } else {
                        try {
                           ArticleViewer.this.parentActivity.getWindow().clearFlags(128);
                        } catch (Exception var4) {
                           FileLog.e((Throwable)var4);
                        }
                     }

                     if (var2 == 3 && ArticleViewer.this.aspectRatioFrameLayout.getVisibility() != 0) {
                        ArticleViewer.this.aspectRatioFrameLayout.setVisibility(0);
                     }

                     if (ArticleViewer.this.videoPlayer.isPlaying() && var2 != 4) {
                        if (!ArticleViewer.this.isPlaying) {
                           ArticleViewer.this.isPlaying = true;
                           ArticleViewer.this.videoPlayButton.setImageResource(2131165478);
                           AndroidUtilities.runOnUIThread(ArticleViewer.this.updateProgressRunnable);
                        }
                     } else if (ArticleViewer.this.isPlaying) {
                        ArticleViewer.this.isPlaying = false;
                        ArticleViewer.this.videoPlayButton.setImageResource(2131165479);
                        AndroidUtilities.cancelRunOnUIThread(ArticleViewer.this.updateProgressRunnable);
                        if (var2 == 4 && !ArticleViewer.this.videoPlayerSeekbar.isDragging()) {
                           ArticleViewer.this.videoPlayerSeekbar.setProgress(0.0F);
                           ArticleViewer.this.videoPlayerControlFrameLayout.invalidate();
                           ArticleViewer.this.videoPlayer.seekTo(0L);
                           ArticleViewer.this.videoPlayer.pause();
                        }
                     }

                     ArticleViewer.this.updateVideoPlayerTime();
                  }
               }

               public boolean onSurfaceDestroyed(SurfaceTexture var1) {
                  return false;
               }

               public void onSurfaceTextureUpdated(SurfaceTexture var1) {
               }

               public void onVideoSizeChanged(int var1, int var2, int var3, float var4) {
                  if (ArticleViewer.this.aspectRatioFrameLayout != null) {
                     int var5 = var1;
                     int var6 = var2;
                     if (var3 != 90) {
                        if (var3 == 270) {
                           var5 = var1;
                           var6 = var2;
                        } else {
                           var6 = var1;
                           var5 = var2;
                        }
                     }

                     AspectRatioFrameLayout var7 = ArticleViewer.this.aspectRatioFrameLayout;
                     if (var5 == 0) {
                        var4 = 1.0F;
                     } else {
                        var4 = (float)var6 * var4 / (float)var5;
                     }

                     var7.setAspectRatio(var4, var3);
                  }

               }
            });
            VideoPlayer var8 = this.videoPlayer;
            long var4 = 0L;
            long var6 = var4;
            if (var8 != null) {
               var6 = var8.getDuration();
               if (var6 == -9223372036854775807L) {
                  var6 = var4;
               }
            }

            var4 = var6 / 1000L;
            TextPaint var9 = this.videoPlayerTime.getPaint();
            var6 = var4 / 60L;
            var4 %= 60L;
            Math.ceil((double)var9.measureText(String.format("%02d:%02d / %02d:%02d", var6, var4, var6, var4)));
         }

         this.videoPlayer.preparePlayer(Uri.fromFile(var1), "other");
         this.bottomLayout.setVisibility(0);
         this.videoPlayer.setPlayWhenReady(var2);
      }
   }

   private boolean processTouchEvent(MotionEvent var1) {
      if (this.photoAnimationInProgress == 0 && this.animationStartTime == 0L) {
         if (var1.getPointerCount() == 1 && this.gestureDetector.onTouchEvent(var1) && this.doubleTap) {
            this.doubleTap = false;
            this.moving = false;
            this.zooming = false;
            this.checkMinMax(false);
            return true;
         }

         float var8;
         VelocityTracker var10;
         if (var1.getActionMasked() != 0 && var1.getActionMasked() != 5) {
            int var2 = var1.getActionMasked();
            float var3 = 0.0F;
            float var4 = 0.0F;
            float var6;
            float var7;
            if (var2 == 2) {
               float var5;
               if (this.canZoom && var1.getPointerCount() == 2 && !this.draggingDown && this.zooming && !this.changingPage) {
                  this.discardTap = true;
                  this.scale = (float)Math.hypot((double)(var1.getX(1) - var1.getX(0)), (double)(var1.getY(1) - var1.getY(0))) / this.pinchStartDistance * this.pinchStartScale;
                  this.translationX = this.pinchCenterX - (float)(this.getContainerViewWidth() / 2) - (this.pinchCenterX - (float)(this.getContainerViewWidth() / 2) - this.pinchStartX) * (this.scale / this.pinchStartScale);
                  var5 = this.pinchCenterY;
                  var6 = (float)(this.getContainerViewHeight() / 2);
                  var7 = this.pinchCenterY;
                  var4 = (float)(this.getContainerViewHeight() / 2);
                  var8 = this.pinchStartY;
                  var3 = this.scale;
                  this.translationY = var5 - var6 - (var7 - var4 - var8) * (var3 / this.pinchStartScale);
                  this.updateMinMax(var3);
                  this.photoContainerView.invalidate();
               } else if (var1.getPointerCount() == 1) {
                  VelocityTracker var9 = this.velocityTracker;
                  if (var9 != null) {
                     var9.addMovement(var1);
                  }

                  var7 = Math.abs(var1.getX() - this.moveStartX);
                  var8 = Math.abs(var1.getY() - this.dragY);
                  if (var7 > (float)AndroidUtilities.dp(3.0F) || var8 > (float)AndroidUtilities.dp(3.0F)) {
                     this.discardTap = true;
                  }

                  if (this.canDragDown && !this.draggingDown && this.scale == 1.0F && var8 >= (float)AndroidUtilities.dp(30.0F) && var8 / 2.0F > var7) {
                     this.draggingDown = true;
                     this.moving = false;
                     this.dragY = var1.getY();
                     if (this.isActionBarVisible) {
                        this.toggleActionBar(false, true);
                     }

                     return true;
                  }

                  if (this.draggingDown) {
                     this.translationY = var1.getY() - this.dragY;
                     this.photoContainerView.invalidate();
                  } else if (!this.invalidCoords && this.animationStartTime == 0L) {
                     var7 = this.moveStartX - var1.getX();
                     var8 = this.moveStartY - var1.getY();
                     if (this.moving || this.scale == 1.0F && Math.abs(var8) + (float)AndroidUtilities.dp(12.0F) < Math.abs(var7) || this.scale != 1.0F) {
                        if (!this.moving) {
                           this.moving = true;
                           this.canDragDown = false;
                           var7 = 0.0F;
                           var8 = 0.0F;
                        }

                        label254: {
                           this.moveStartX = var1.getX();
                           this.moveStartY = var1.getY();
                           this.updateMinMax(this.scale);
                           if (this.translationX >= this.minX || this.rightImage.hasImageSet()) {
                              var6 = var7;
                              if (this.translationX <= this.maxX) {
                                 break label254;
                              }

                              var6 = var7;
                              if (this.leftImage.hasImageSet()) {
                                 break label254;
                              }
                           }

                           var6 = var7 / 3.0F;
                        }

                        label249: {
                           var3 = this.maxY;
                           if (var3 == 0.0F) {
                              var5 = this.minY;
                              if (var5 == 0.0F) {
                                 var7 = this.translationY;
                                 if (var7 - var8 < var5) {
                                    this.translationY = var5;
                                    var8 = var4;
                                 } else if (var7 - var8 > var3) {
                                    this.translationY = var3;
                                    var8 = var4;
                                 }
                                 break label249;
                              }
                           }

                           var7 = this.translationY;
                           if (var7 < this.minY || var7 > this.maxY) {
                              var8 /= 3.0F;
                           }
                        }

                        this.translationX -= var6;
                        if (this.scale != 1.0F) {
                           this.translationY -= var8;
                        }

                        this.photoContainerView.invalidate();
                     }
                  } else {
                     this.invalidCoords = false;
                     this.moveStartX = var1.getX();
                     this.moveStartY = var1.getY();
                  }
               }
            } else if (var1.getActionMasked() == 3 || var1.getActionMasked() == 1 || var1.getActionMasked() == 6) {
               if (this.zooming) {
                  this.invalidCoords = true;
                  var8 = this.scale;
                  if (var8 < 1.0F) {
                     this.updateMinMax(1.0F);
                     this.animateTo(1.0F, 0.0F, 0.0F, true);
                  } else if (var8 > 3.0F) {
                     var8 = this.pinchCenterX - (float)(this.getContainerViewWidth() / 2) - (this.pinchCenterX - (float)(this.getContainerViewWidth() / 2) - this.pinchStartX) * (3.0F / this.pinchStartScale);
                     var7 = this.pinchCenterY - (float)(this.getContainerViewHeight() / 2) - (this.pinchCenterY - (float)(this.getContainerViewHeight() / 2) - this.pinchStartY) * (3.0F / this.pinchStartScale);
                     this.updateMinMax(3.0F);
                     var6 = this.minX;
                     if (var8 < var6) {
                        var8 = var6;
                     } else {
                        var6 = this.maxX;
                        if (var8 > var6) {
                           var8 = var6;
                        }
                     }

                     var6 = this.minY;
                     if (var7 < var6) {
                        var7 = var6;
                     } else {
                        var6 = this.maxY;
                        if (var7 > var6) {
                           var7 = var6;
                        }
                     }

                     this.animateTo(3.0F, var8, var7, true);
                  } else {
                     this.checkMinMax(true);
                  }

                  this.zooming = false;
               } else if (this.draggingDown) {
                  if (Math.abs(this.dragY - var1.getY()) > (float)this.getContainerViewHeight() / 6.0F) {
                     this.closePhoto(true);
                  } else {
                     this.animateTo(1.0F, 0.0F, 0.0F, false);
                  }

                  this.draggingDown = false;
               } else if (this.moving) {
                  var6 = this.translationX;
                  var7 = this.translationY;
                  this.updateMinMax(this.scale);
                  this.moving = false;
                  this.canDragDown = true;
                  var10 = this.velocityTracker;
                  var8 = var3;
                  if (var10 != null) {
                     var8 = var3;
                     if (this.scale == 1.0F) {
                        var10.computeCurrentVelocity(1000);
                        var8 = this.velocityTracker.getXVelocity();
                     }
                  }

                  if ((this.translationX < this.minX - (float)(this.getContainerViewWidth() / 3) || var8 < (float)(-AndroidUtilities.dp(650.0F))) && this.rightImage.hasImageSet()) {
                     this.goToNext();
                     return true;
                  }

                  if ((this.translationX > this.maxX + (float)(this.getContainerViewWidth() / 3) || var8 > (float)AndroidUtilities.dp(650.0F)) && this.leftImage.hasImageSet()) {
                     this.goToPrev();
                     return true;
                  }

                  var4 = this.translationX;
                  var8 = this.minX;
                  if (var4 >= var8) {
                     var8 = this.maxX;
                     if (var4 <= var8) {
                        var8 = var6;
                     }
                  }

                  var4 = this.translationY;
                  var6 = this.minY;
                  if (var4 < var6) {
                     var7 = var6;
                  } else {
                     var6 = this.maxY;
                     if (var4 > var6) {
                        var7 = var6;
                     }
                  }

                  this.animateTo(this.scale, var8, var7, false);
               }
            }
         } else {
            this.discardTap = false;
            if (!this.scroller.isFinished()) {
               this.scroller.abortAnimation();
            }

            if (!this.draggingDown && !this.changingPage) {
               if (this.canZoom && var1.getPointerCount() == 2) {
                  this.pinchStartDistance = (float)Math.hypot((double)(var1.getX(1) - var1.getX(0)), (double)(var1.getY(1) - var1.getY(0)));
                  this.pinchStartScale = this.scale;
                  this.pinchCenterX = (var1.getX(0) + var1.getX(1)) / 2.0F;
                  this.pinchCenterY = (var1.getY(0) + var1.getY(1)) / 2.0F;
                  this.pinchStartX = this.translationX;
                  this.pinchStartY = this.translationY;
                  this.zooming = true;
                  this.moving = false;
                  var10 = this.velocityTracker;
                  if (var10 != null) {
                     var10.clear();
                  }
               } else if (var1.getPointerCount() == 1) {
                  this.moveStartX = var1.getX();
                  var8 = var1.getY();
                  this.moveStartY = var8;
                  this.dragY = var8;
                  this.draggingDown = false;
                  this.canDragDown = true;
                  var10 = this.velocityTracker;
                  if (var10 != null) {
                     var10.clear();
                  }
               }
            }
         }
      }

      return false;
   }

   private void releasePlayer() {
      VideoPlayer var1 = this.videoPlayer;
      if (var1 != null) {
         var1.releasePlayer(true);
         this.videoPlayer = null;
      }

      try {
         this.parentActivity.getWindow().clearFlags(128);
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

      AspectRatioFrameLayout var3 = this.aspectRatioFrameLayout;
      if (var3 != null) {
         this.photoContainerView.removeView(var3);
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
      } else {
         ArrayList var1 = this.pagesStack;
         var1.remove(var1.size() - 1);
         var1 = this.pagesStack;
         this.currentPage = (TLRPC.WebPage)var1.get(var1.size() - 1);
         this.updateInterfaceForCurrentPage(-1);
         return true;
      }
   }

   private void removePressedLink() {
      if (this.pressedLink != null || this.pressedLinkOwnerView != null) {
         View var1 = this.pressedLinkOwnerView;
         this.pressedLink = null;
         this.pressedLinkOwnerLayout = null;
         this.pressedLinkOwnerView = null;
         if (var1 != null) {
            var1.invalidate();
         }

      }
   }

   private void saveCurrentPagePosition() {
      if (this.currentPage != null) {
         LinearLayoutManager[] var1 = this.layoutManager;
         boolean var2 = false;
         int var3 = var1[0].findFirstVisibleItemPosition();
         if (var3 != -1) {
            View var7 = this.layoutManager[0].findViewByPosition(var3);
            int var4;
            if (var7 != null) {
               var4 = var7.getTop();
            } else {
               var4 = 0;
            }

            Editor var5 = ApplicationLoader.applicationContext.getSharedPreferences("articles", 0).edit();
            StringBuilder var8 = new StringBuilder();
            var8.append("article");
            var8.append(this.currentPage.id);
            String var9 = var8.toString();
            var5 = var5.putInt(var9, var3);
            StringBuilder var6 = new StringBuilder();
            var6.append(var9);
            var6.append("o");
            var5 = var5.putInt(var6.toString(), var4);
            var6 = new StringBuilder();
            var6.append(var9);
            var6.append("r");
            String var11 = var6.toString();
            Point var10 = AndroidUtilities.displaySize;
            if (var10.x > var10.y) {
               var2 = true;
            }

            var5.putBoolean(var11, var2).commit();
         }

      }
   }

   private boolean scrollToAnchor(String var1) {
      boolean var2 = TextUtils.isEmpty(var1);
      Integer var3 = 0;
      if (var2) {
         return false;
      } else {
         String var4 = var1.toLowerCase();
         Integer var5 = (Integer)this.adapter[0].anchors.get(var4);
         if (var5 != null) {
            TLRPC.TL_textAnchor var9 = (TLRPC.TL_textAnchor)this.adapter[0].anchorsParent.get(var4);
            int var6;
            RecyclerView.ViewHolder var12;
            if (var9 != null) {
               TLRPC.TL_pageBlockParagraph var14 = new TLRPC.TL_pageBlockParagraph();
               var14.text = var9.text;
               var6 = this.adapter[0].getTypeForBlock(var14);
               var12 = this.adapter[0].onCreateViewHolder((ViewGroup)null, var6);
               this.adapter[0].bindBlockToHolder(var6, var12, var14, 0, 0);
               BottomSheet.Builder var17 = new BottomSheet.Builder(this.parentActivity);
               var17.setUseFullscreen(true);
               var17.setApplyTopPadding(false);
               LinearLayout var13 = new LinearLayout(this.parentActivity);
               var13.setOrientation(1);
               TextView var15 = new TextView(this.parentActivity) {
                  protected void onDraw(Canvas var1) {
                     var1.drawLine(0.0F, (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), ArticleViewer.dividerPaint);
                     super.onDraw(var1);
                  }
               };
               var15.setTextSize(1, 16.0F);
               var15.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
               var15.setText(LocaleController.getString("InstantViewReference", 2131559669));
               byte var16;
               if (this.isRtl) {
                  var16 = 5;
               } else {
                  var16 = 3;
               }

               var15.setGravity(var16 | 16);
               var15.setTextColor(this.getTextColor());
               var15.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
               var13.addView(var15, new android.widget.LinearLayout.LayoutParams(-1, AndroidUtilities.dp(48.0F) + 1));
               var13.addView(var12.itemView, LayoutHelper.createLinear(-1, -2, 0.0F, 7.0F, 0.0F, 0.0F));
               var17.setCustomView(var13);
               this.linkSheet = var17.create();
               var6 = this.selectedColor;
               if (var6 == 0) {
                  this.linkSheet.setBackgroundColor(-1);
               } else if (var6 == 1) {
                  this.linkSheet.setBackgroundColor(-659492);
               } else if (var6 == 2) {
                  this.linkSheet.setBackgroundColor(-15461356);
               }

               this.showDialog(this.linkSheet);
               return true;
            }

            if (var5 >= 0 && var5 < this.adapter[0].blocks.size()) {
               TLRPC.PageBlock var8 = (TLRPC.PageBlock)this.adapter[0].blocks.get(var5);
               TLRPC.PageBlock var10 = this.getLastNonListPageBlock(var8);
               if (var10 instanceof ArticleViewer.TL_pageBlockDetailsChild && this.openAllParentBlocks((ArticleViewer.TL_pageBlockDetailsChild)var10)) {
                  this.adapter[0].updateRows();
                  this.adapter[0].notifyDataSetChanged();
               }

               var6 = this.adapter[0].localBlocks.indexOf(var8);
               if (var6 != -1) {
                  var5 = var6;
               }

               Integer var7 = (Integer)this.adapter[0].anchorsOffset.get(var4);
               Integer var11 = var3;
               if (var7 != null) {
                  if (var7 == -1) {
                     var6 = this.adapter[0].getTypeForBlock(var8);
                     var12 = this.adapter[0].onCreateViewHolder((ViewGroup)null, var6);
                     this.adapter[0].bindBlockToHolder(var6, var12, var8, 0, 0);
                     var12.itemView.measure(MeasureSpec.makeMeasureSpec(this.listView[0].getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
                     var11 = (Integer)this.adapter[0].anchorsOffset.get(var4);
                     if (var11 == -1) {
                        var11 = var3;
                     }
                  } else {
                     var11 = var7;
                  }
               }

               this.layoutManager[0].scrollToPositionWithOffset(var5, this.currentHeaderHeight - AndroidUtilities.dp(56.0F) - var11);
               return true;
            }
         }

         return false;
      }
   }

   private void setCurrentCaption(CharSequence var1, boolean var2) {
      if (!TextUtils.isEmpty((CharSequence)var1)) {
         Theme.createChatResources((Context)null, true);
         if (!var2) {
            if (var1 instanceof Spannable) {
               Spannable var3 = (Spannable)var1;
               TextPaintUrlSpan[] var4 = (TextPaintUrlSpan[])var3.getSpans(0, ((CharSequence)var1).length(), TextPaintUrlSpan.class);
               var1 = new SpannableStringBuilder(((CharSequence)var1).toString());
               if (var4 != null && var4.length > 0) {
                  for(int var5 = 0; var5 < var4.length; ++var5) {
                     ((SpannableStringBuilder)var1).setSpan(new URLSpan(var4[var5].getUrl()) {
                        public void onClick(View var1) {
                           ArticleViewer.this.openWebpageUrl(this.getURL(), (String)null);
                        }
                     }, var3.getSpanStart(var4[var5]), var3.getSpanEnd(var4[var5]), 33);
                  }
               }
            } else {
               var1 = new SpannableStringBuilder(((CharSequence)var1).toString());
            }
         }

         CharSequence var6 = Emoji.replaceEmoji((CharSequence)var1, this.captionTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
         this.captionTextView.setTag(var6);
         this.captionTextView.setText(var6);
         this.captionTextView.setVisibility(0);
      } else {
         this.captionTextView.setTag((Object)null);
         this.captionTextView.setVisibility(8);
      }

   }

   private void setCurrentHeaderHeight(int var1) {
      int var2 = AndroidUtilities.dp(56.0F);
      int var3 = Math.max(AndroidUtilities.statusBarHeight, AndroidUtilities.dp(24.0F));
      int var4;
      if (var1 < var3) {
         var4 = var3;
      } else {
         var4 = var1;
         if (var1 > var2) {
            var4 = var2;
         }
      }

      float var5 = (float)(var2 - var3);
      this.currentHeaderHeight = var4;
      var1 = this.currentHeaderHeight;
      float var6 = (float)(var1 - var3) / var5 * 0.2F + 0.8F;
      var5 = (float)(var1 - var3) / var5;
      this.backButton.setScaleX(var6);
      this.backButton.setScaleY(var6);
      this.backButton.setTranslationY((float)((var2 - this.currentHeaderHeight) / 2));
      this.shareContainer.setScaleX(var6);
      this.shareContainer.setScaleY(var6);
      this.settingsButton.setScaleX(var6);
      this.settingsButton.setScaleY(var6);
      this.titleTextView.setScaleX(var6);
      this.titleTextView.setScaleY(var6);
      this.lineProgressView.setScaleY(var5 * 0.5F + 0.5F);
      this.shareContainer.setTranslationY((float)((var2 - this.currentHeaderHeight) / 2));
      this.settingsButton.setTranslationY((float)((var2 - this.currentHeaderHeight) / 2));
      this.titleTextView.setTranslationY((float)((var2 - this.currentHeaderHeight) / 2));
      this.headerView.setTranslationY((float)(this.currentHeaderHeight - var2));
      var1 = 0;

      while(true) {
         RecyclerListView[] var7 = this.listView;
         if (var1 >= var7.length) {
            return;
         }

         var7[var1].setTopGlowOffset(this.currentHeaderHeight);
         ++var1;
      }
   }

   private void setImageIndex(int var1, boolean var2) {
      if (this.currentIndex != var1) {
         if (!var2) {
            ImageReceiver.BitmapHolder var3 = this.currentThumb;
            if (var3 != null) {
               var3.release();
               this.currentThumb = null;
            }
         }

         this.currentFileNames[0] = this.getFileName(var1);
         this.currentFileNames[1] = this.getFileName(var1 + 1);
         this.currentFileNames[2] = this.getFileName(var1 - 1);
         int var4 = this.currentIndex;
         this.currentIndex = var1;
         boolean var10;
         if (!this.imagesArr.isEmpty()) {
            var1 = this.currentIndex;
            if (var1 < 0 || var1 >= this.imagesArr.size()) {
               this.closePhoto(false);
               return;
            }

            TLRPC.PageBlock var11 = (TLRPC.PageBlock)this.imagesArr.get(this.currentIndex);
            TLRPC.PageBlock var5 = this.currentMedia;
            if (var5 != null && var5 == var11) {
               var10 = true;
            } else {
               var10 = false;
            }

            this.currentMedia = var11;
            boolean var6 = this.isMediaVideo(this.currentIndex);
            if (var6) {
               this.menuItem.showSubItem(3);
            }

            SpannableStringBuilder var12;
            label115: {
               if (var11 instanceof TLRPC.TL_pageBlockPhoto) {
                  String var16 = ((TLRPC.TL_pageBlockPhoto)var11).url;
                  if (!TextUtils.isEmpty(var16)) {
                     var12 = new SpannableStringBuilder(var16);
                     var12.setSpan(new URLSpan(var16) {
                        public void onClick(View var1) {
                           ArticleViewer.this.openWebpageUrl(this.getURL(), (String)null);
                        }
                     }, 0, var16.length(), 34);
                     var2 = true;
                     break label115;
                  }
               }

               var12 = null;
               var2 = false;
            }

            Object var18 = var12;
            if (var12 == null) {
               TLRPC.RichText var13 = this.getBlockCaption(this.currentMedia, 2);
               var18 = this.getText((View)null, var13, var13, this.currentMedia, -AndroidUtilities.dp(100.0F));
            }

            this.setCurrentCaption((CharSequence)var18, var2);
            if (this.currentAnimation != null) {
               this.menuItem.setVisibility(8);
               this.menuItem.hideSubItem(1);
               this.actionBar.setTitle(LocaleController.getString("AttachGif", 2131558716));
            } else {
               this.menuItem.setVisibility(0);
               if (this.imagesArr.size() == 1) {
                  if (var6) {
                     this.actionBar.setTitle(LocaleController.getString("AttachVideo", 2131558733));
                  } else {
                     this.actionBar.setTitle(LocaleController.getString("AttachPhoto", 2131558727));
                  }
               } else {
                  this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.currentIndex + 1, this.imagesArr.size()));
               }

               this.menuItem.showSubItem(1);
            }

            this.groupedPhotosListView.fillList();
            var2 = var6;
         } else {
            var10 = false;
            var2 = false;
         }

         int var7 = this.listView[0].getChildCount();

         for(int var8 = 0; var8 < var7; ++var8) {
            View var14 = this.listView[0].getChildAt(var8);
            if (var14 instanceof ArticleViewer.BlockSlideshowCell) {
               ArticleViewer.BlockSlideshowCell var15 = (ArticleViewer.BlockSlideshowCell)var14;
               int var9 = var15.currentBlock.items.indexOf(this.currentMedia);
               if (var9 != -1) {
                  var15.innerListView.setCurrentItem(var9, false);
                  break;
               }
            }
         }

         ArticleViewer.PlaceProviderObject var17 = this.currentPlaceObject;
         if (var17 != null) {
            if (this.photoAnimationInProgress == 0) {
               var17.imageReceiver.setVisible(true, true);
            } else {
               this.showAfterAnimation = var17;
            }
         }

         this.currentPlaceObject = this.getPlaceForPhoto(this.currentMedia);
         var17 = this.currentPlaceObject;
         if (var17 != null) {
            if (this.photoAnimationInProgress == 0) {
               var17.imageReceiver.setVisible(false, true);
            } else {
               this.hideAfterAnimation = var17;
            }
         }

         if (!var10) {
            this.draggingDown = false;
            this.translationX = 0.0F;
            this.translationY = 0.0F;
            this.scale = 1.0F;
            this.animateToX = 0.0F;
            this.animateToY = 0.0F;
            this.animateToScale = 1.0F;
            this.animationStartTime = 0L;
            this.imageMoveAnimation = null;
            AspectRatioFrameLayout var19 = this.aspectRatioFrameLayout;
            if (var19 != null) {
               var19.setVisibility(4);
            }

            this.releasePlayer();
            this.pinchStartDistance = 0.0F;
            this.pinchStartScale = 1.0F;
            this.pinchCenterX = 0.0F;
            this.pinchCenterY = 0.0F;
            this.pinchStartX = 0.0F;
            this.pinchStartY = 0.0F;
            this.moveStartX = 0.0F;
            this.moveStartY = 0.0F;
            this.zooming = false;
            this.moving = false;
            this.doubleTap = false;
            this.invalidCoords = false;
            this.canDragDown = true;
            this.changingPage = false;
            this.switchImageAfterAnimation = 0;
            if (this.currentFileNames[0] != null && !var2 && this.radialProgressViews[0].backgroundState != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            this.canZoom = var2;
            this.updateMinMax(this.scale);
         }

         if (var4 == -1) {
            this.setImages();

            for(var1 = 0; var1 < 3; ++var1) {
               this.checkProgress(var1, false);
            }
         } else {
            this.checkProgress(0, false);
            var1 = this.currentIndex;
            ImageReceiver var20;
            ArticleViewer.RadialProgressView var21;
            ArticleViewer.RadialProgressView[] var22;
            if (var4 > var1) {
               var20 = this.rightImage;
               this.rightImage = this.centerImage;
               this.centerImage = this.leftImage;
               this.leftImage = var20;
               var22 = this.radialProgressViews;
               var21 = var22[0];
               var22[0] = var22[2];
               var22[2] = var21;
               this.setIndexToImage(this.leftImage, var1 - 1);
               this.checkProgress(1, false);
               this.checkProgress(2, false);
            } else if (var4 < var1) {
               var20 = this.leftImage;
               this.leftImage = this.centerImage;
               this.centerImage = this.rightImage;
               this.rightImage = var20;
               var22 = this.radialProgressViews;
               var21 = var22[0];
               var22[0] = var22[1];
               var22[1] = var21;
               this.setIndexToImage(this.rightImage, var1 + 1);
               this.checkProgress(1, false);
               this.checkProgress(2, false);
            }
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

   private void setIndexToImage(ImageReceiver var1, int var2) {
      var1.setOrientation(0, false);
      int[] var3 = new int[1];
      TLObject var4 = this.getMedia(var2);
      TLRPC.PhotoSize var5 = this.getFileLocation(var4, var3);
      if (var5 != null) {
         ImageReceiver.BitmapHolder var6;
         BitmapDrawable var12;
         if (var4 instanceof TLRPC.Photo) {
            TLRPC.Photo var9 = (TLRPC.Photo)var4;
            var6 = this.currentThumb;
            if (var6 == null || var1 != this.centerImage) {
               var6 = null;
            }

            if (var3[0] == 0) {
               var3[0] = -1;
            }

            TLRPC.PhotoSize var7 = FileLoader.getClosestPhotoSizeWithSize(var9.sizes, 80);
            ImageLocation var11 = ImageLocation.getForPhoto(var5, var9);
            ImageLocation var10 = ImageLocation.getForPhoto(var7, var9);
            if (var6 != null) {
               var12 = new BitmapDrawable(var6.bitmap);
            } else {
               var12 = null;
            }

            var1.setImage(var11, (String)null, var10, "b", var12, var3[0], (String)null, this.currentPage, 1);
         } else if (this.isMediaVideo(var2)) {
            if (!(var5.location instanceof TLRPC.TL_fileLocationUnavailable)) {
               var6 = this.currentThumb;
               if (var6 == null || var1 != this.centerImage) {
                  var6 = null;
               }

               ImageLocation var8 = ImageLocation.getForDocument(var5, (TLRPC.Document)var4);
               if (var6 != null) {
                  var12 = new BitmapDrawable(var6.bitmap);
               } else {
                  var12 = null;
               }

               var1.setImage((ImageLocation)null, (String)null, var8, "b", var12, 0, (String)null, this.currentPage, 1);
            } else {
               var1.setImageBitmap(this.parentActivity.getResources().getDrawable(2131165761));
            }
         } else {
            AnimatedFileDrawable var13 = this.currentAnimation;
            if (var13 != null) {
               var1.setImageBitmap((Drawable)var13);
               this.currentAnimation.setSecondParentView(this.photoContainerView);
            }
         }
      } else if (var3[0] == 0) {
         var1.setImageBitmap((Bitmap)null);
      } else {
         var1.setImageBitmap(this.parentActivity.getResources().getDrawable(2131165761));
      }

   }

   private void setMapColors(SparseArray var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         int var3 = var1.keyAt(var2);
         TextPaint var4 = (TextPaint)var1.valueAt(var2);
         if ((var3 & 8) == 0 && (var3 & 512) == 0) {
            var4.setColor(this.getTextColor());
         } else {
            var4.setColor(this.getLinkTextColor());
         }
      }

   }

   private void setScaleToFill() {
      float var1 = (float)this.centerImage.getBitmapWidth();
      float var2 = (float)this.getContainerViewWidth();
      float var3 = (float)this.centerImage.getBitmapHeight();
      float var4 = (float)this.getContainerViewHeight();
      float var5 = Math.min(var4 / var3, var2 / var1);
      var1 = (float)((int)(var1 * var5));
      var5 = (float)((int)(var3 * var5));
      this.scale = Math.max(var2 / var1, var4 / var5);
      this.updateMinMax(this.scale);
   }

   private void showCopyPopup(String var1) {
      if (this.parentActivity != null) {
         BottomSheet var2 = this.linkSheet;
         if (var2 != null) {
            var2.dismiss();
            this.linkSheet = null;
         }

         BottomSheet.Builder var3 = new BottomSheet.Builder(this.parentActivity);
         var3.setUseFullscreen(true);
         var3.setTitle(var1);
         String var4 = LocaleController.getString("Open", 2131560110);
         int var5 = 0;
         String var8 = LocaleController.getString("Copy", 2131559163);
         _$$Lambda$ArticleViewer$OR_FYCAXpGUOR5Uvrpul7uvfnQI var6 = new _$$Lambda$ArticleViewer$OR_FYCAXpGUOR5Uvrpul7uvfnQI(this, var1);
         var3.setItems(new CharSequence[]{var4, var8}, var6);
         BottomSheet var7 = var3.create();
         this.showDialog(var7);

         while(var5 < 2) {
            var7.setItemColor(var5, this.getTextColor(), this.getTextColor());
            ++var5;
         }

         var7.setTitleColor(this.getGrayTextColor());
         var5 = this.selectedColor;
         if (var5 == 0) {
            var7.setBackgroundColor(-1);
         } else if (var5 == 1) {
            var7.setBackgroundColor(-659492);
         } else if (var5 == 2) {
            var7.setBackgroundColor(-15461356);
         }

      }
   }

   private void showNightModeHint() {
      Activity var1 = this.parentActivity;
      if (var1 != null && this.nightModeHintView == null && this.nightModeEnabled) {
         this.nightModeHintView = new FrameLayout(var1);
         this.nightModeHintView.setBackgroundColor(-13421773);
         this.containerView.addView(this.nightModeHintView, LayoutHelper.createFrame(-1, -2, 83));
         ImageView var2 = new ImageView(this.parentActivity);
         var2.setScaleType(ScaleType.CENTER);
         var2.setImageResource(2131165607);
         FrameLayout var8 = this.nightModeHintView;
         boolean var3 = LocaleController.isRTL;
         byte var4 = 5;
         byte var5;
         if (var3) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var8.addView(var2, LayoutHelper.createFrame(56, 56, var5 | 16));
         TextView var10 = new TextView(this.parentActivity);
         var10.setText(LocaleController.getString("InstantViewNightMode", 2131559668));
         var10.setTextColor(-1);
         var10.setTextSize(1, 15.0F);
         var8 = this.nightModeHintView;
         if (LocaleController.isRTL) {
            var5 = var4;
         } else {
            var5 = 3;
         }

         var3 = LocaleController.isRTL;
         byte var6 = 10;
         if (var3) {
            var4 = 10;
         } else {
            var4 = 56;
         }

         float var7 = (float)var4;
         var4 = var6;
         if (LocaleController.isRTL) {
            var4 = 56;
         }

         var8.addView(var10, LayoutHelper.createFrame(-1, -1.0F, var5 | 48, var7, 11.0F, (float)var4, 12.0F));
         AnimatorSet var9 = new AnimatorSet();
         var9.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.nightModeHintView, View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(100.0F), 0.0F})});
         var9.setInterpolator(new DecelerateInterpolator(1.5F));
         var9.addListener(new AnimatorListenerAdapter() {
            // $FF: synthetic method
            public void lambda$onAnimationEnd$0$ArticleViewer$12() {
               AnimatorSet var1 = new AnimatorSet();
               var1.playTogether(new Animator[]{ObjectAnimator.ofFloat(ArticleViewer.this.nightModeHintView, View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(100.0F)})});
               var1.setInterpolator(new DecelerateInterpolator(1.5F));
               var1.setDuration(250L);
               var1.start();
            }

            public void onAnimationEnd(Animator var1) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$12$IKrF64A2pjQihanm36OwYzYcJGQ(this), 3000L);
            }
         });
         var9.setDuration(250L);
         var9.start();
      }

   }

   private void showPopup(View var1, int var2, int var3, int var4) {
      ActionBarPopupWindow var5 = this.popupWindow;
      if (var5 != null && var5.isShowing()) {
         this.popupWindow.dismiss();
      } else {
         Drawable var7;
         if (this.popupLayout == null) {
            this.popupRect = new Rect();
            this.popupLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this.parentActivity);
            this.popupLayout.setPadding(AndroidUtilities.dp(1.0F), AndroidUtilities.dp(1.0F), AndroidUtilities.dp(1.0F), AndroidUtilities.dp(1.0F));
            ActionBarPopupWindow.ActionBarPopupWindowLayout var6 = this.popupLayout;
            var7 = this.parentActivity.getResources().getDrawable(2131165578);
            this.copyBackgroundDrawable = var7;
            var6.setBackgroundDrawable(var7);
            this.popupLayout.setAnimationEnabled(false);
            this.popupLayout.setOnTouchListener(new _$$Lambda$ArticleViewer$MGSGAQzWCEU3w9PF3AH_evurh_k(this));
            this.popupLayout.setDispatchKeyEventListener(new _$$Lambda$ArticleViewer$qD5uCRo_niW9s97tmnD3kf5paxo(this));
            this.popupLayout.setShowedFromBotton(false);
            this.deleteView = new TextView(this.parentActivity);
            this.deleteView.setBackgroundDrawable(Theme.createSelectorDrawable(251658240, 2));
            this.deleteView.setGravity(16);
            this.deleteView.setPadding(AndroidUtilities.dp(20.0F), 0, AndroidUtilities.dp(20.0F), 0);
            this.deleteView.setTextSize(1, 15.0F);
            this.deleteView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.deleteView.setText(LocaleController.getString("Copy", 2131559163).toUpperCase());
            this.deleteView.setOnClickListener(new _$$Lambda$ArticleViewer$RDJJaoFjXrUN__l9em3k_Gv7twk(this));
            this.popupLayout.addView(this.deleteView, LayoutHelper.createFrame(-2, 48.0F));
            this.popupWindow = new ActionBarPopupWindow(this.popupLayout, -2, -2);
            this.popupWindow.setAnimationEnabled(false);
            this.popupWindow.setAnimationStyle(2131624111);
            this.popupWindow.setOutsideTouchable(true);
            this.popupWindow.setClippingEnabled(true);
            this.popupWindow.setInputMethodMode(2);
            this.popupWindow.setSoftInputMode(0);
            this.popupWindow.getContentView().setFocusableInTouchMode(true);
            this.popupWindow.setOnDismissListener(new _$$Lambda$ArticleViewer$IPD6DMulJZDu02GPHHVkQa_Seko(this));
         }

         if (this.selectedColor == 2) {
            this.deleteView.setTextColor(-5723992);
            var7 = this.copyBackgroundDrawable;
            if (var7 != null) {
               var7.setColorFilter(new PorterDuffColorFilter(-14408668, Mode.MULTIPLY));
            }
         } else {
            this.deleteView.setTextColor(-14606047);
            var7 = this.copyBackgroundDrawable;
            if (var7 != null) {
               var7.setColorFilter(new PorterDuffColorFilter(-1, Mode.MULTIPLY));
            }
         }

         this.popupLayout.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0F), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0F), Integer.MIN_VALUE));
         this.popupWindow.setFocusable(true);
         this.popupWindow.showAtLocation(var1, var2, var3, var4);
         this.popupWindow.startAnimation();
      }
   }

   private void showProgressView(boolean var1, final boolean var2) {
      if (var1) {
         AndroidUtilities.cancelRunOnUIThread(this.lineProgressTickRunnable);
         if (var2) {
            this.lineProgressView.setProgress(0.0F, false);
            this.lineProgressView.setProgress(0.3F, true);
            AndroidUtilities.runOnUIThread(this.lineProgressTickRunnable, 100L);
         } else {
            this.lineProgressView.setProgress(1.0F, true);
         }
      } else {
         AnimatorSet var3 = this.progressViewAnimation;
         if (var3 != null) {
            var3.cancel();
         }

         this.progressViewAnimation = new AnimatorSet();
         if (var2) {
            this.progressView.setVisibility(0);
            this.shareContainer.setEnabled(false);
            this.progressViewAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.shareButton, View.SCALE_X, new float[]{0.1F}), ObjectAnimator.ofFloat(this.shareButton, View.SCALE_Y, new float[]{0.1F}), ObjectAnimator.ofFloat(this.shareButton, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.progressView, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, View.ALPHA, new float[]{1.0F})});
         } else {
            this.shareButton.setVisibility(0);
            this.shareContainer.setEnabled(true);
            this.progressViewAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.progressView, View.SCALE_X, new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, View.SCALE_Y, new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.shareButton, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.shareButton, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.shareButton, View.ALPHA, new float[]{1.0F})});
         }

         this.progressViewAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1) {
               if (ArticleViewer.this.progressViewAnimation != null && ArticleViewer.this.progressViewAnimation.equals(var1)) {
                  ArticleViewer.this.progressViewAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1) {
               if (ArticleViewer.this.progressViewAnimation != null && ArticleViewer.this.progressViewAnimation.equals(var1)) {
                  if (!var2) {
                     ArticleViewer.this.progressView.setVisibility(4);
                  } else {
                     ArticleViewer.this.shareButton.setVisibility(4);
                  }
               }

            }
         });
         this.progressViewAnimation.setDuration(150L);
         this.progressViewAnimation.start();
      }

   }

   private void toggleActionBar(boolean var1, boolean var2) {
      if (var1) {
         this.actionBar.setVisibility(0);
         if (this.videoPlayer != null) {
            this.bottomLayout.setVisibility(0);
         }

         if (this.captionTextView.getTag() != null) {
            this.captionTextView.setVisibility(0);
         }
      }

      this.isActionBarVisible = var1;
      this.actionBar.setEnabled(var1);
      this.bottomLayout.setEnabled(var1);
      float var3 = 1.0F;
      float var7;
      if (var2) {
         ArrayList var4 = new ArrayList();
         ActionBar var5 = this.actionBar;
         Property var6 = View.ALPHA;
         if (var1) {
            var7 = 1.0F;
         } else {
            var7 = 0.0F;
         }

         var4.add(ObjectAnimator.ofFloat(var5, var6, new float[]{var7}));
         GroupedPhotosListView var11 = this.groupedPhotosListView;
         var6 = View.ALPHA;
         if (var1) {
            var7 = 1.0F;
         } else {
            var7 = 0.0F;
         }

         var4.add(ObjectAnimator.ofFloat(var11, var6, new float[]{var7}));
         FrameLayout var13 = this.bottomLayout;
         Property var12 = View.ALPHA;
         if (var1) {
            var7 = 1.0F;
         } else {
            var7 = 0.0F;
         }

         var4.add(ObjectAnimator.ofFloat(var13, var12, new float[]{var7}));
         if (this.captionTextView.getTag() != null) {
            TextView var14 = this.captionTextView;
            var12 = View.ALPHA;
            if (!var1) {
               var3 = 0.0F;
            }

            var4.add(ObjectAnimator.ofFloat(var14, var12, new float[]{var3}));
         }

         this.currentActionBarAnimation = new AnimatorSet();
         this.currentActionBarAnimation.playTogether(var4);
         if (!var1) {
            this.currentActionBarAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  if (ArticleViewer.this.currentActionBarAnimation != null && ArticleViewer.this.currentActionBarAnimation.equals(var1)) {
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
      } else {
         ActionBar var8 = this.actionBar;
         if (var1) {
            var7 = 1.0F;
         } else {
            var7 = 0.0F;
         }

         var8.setAlpha(var7);
         FrameLayout var9 = this.bottomLayout;
         if (var1) {
            var7 = 1.0F;
         } else {
            var7 = 0.0F;
         }

         var9.setAlpha(var7);
         if (this.captionTextView.getTag() != null) {
            TextView var10 = this.captionTextView;
            if (!var1) {
               var3 = 0.0F;
            }

            var10.setAlpha(var3);
         }

         if (!var1) {
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

   private void updateFontEntry(int var1, TextPaint var2, Typeface var3, Typeface var4, Typeface var5, Typeface var6) {
      int var7 = var1 & 1;
      if (var7 != 0 && (var1 & 2) != 0) {
         var2.setTypeface(var4);
      } else if (var7 != 0) {
         var2.setTypeface(var5);
      } else if ((var1 & 2) != 0) {
         var2.setTypeface(var6);
      } else if ((var1 & 4) == 0) {
         var2.setTypeface(var3);
      }

   }

   private void updateInterfaceForCurrentPage(int var1) {
      TLRPC.WebPage var2 = this.currentPage;
      if (var2 != null) {
         TLRPC.Page var3 = var2.cached_page;
         if (var3 != null) {
            this.isRtl = var3.rtl;
            this.channelBlock = null;
            SimpleTextView var4 = this.titleTextView;
            String var11 = var2.site_name;
            String var10 = var11;
            if (var11 == null) {
               var10 = "";
            }

            var4.setText(var10);
            boolean var5 = true;
            int var6;
            int var7;
            ArticleViewer.WebpageAdapter var13;
            if (var1 != 0) {
               ArticleViewer.WebpageAdapter[] var12 = this.adapter;
               var13 = var12[1];
               var12[1] = var12[0];
               var12[0] = var13;
               RecyclerListView[] var14 = this.listView;
               RecyclerListView var15 = var14[1];
               var14[1] = var14[0];
               var14[0] = var15;
               LinearLayoutManager[] var17 = this.layoutManager;
               LinearLayoutManager var18 = var17[1];
               var17[1] = var17[0];
               var17[0] = var18;
               var6 = this.containerView.indexOfChild(var14[0]);
               var7 = this.containerView.indexOfChild(this.listView[1]);
               if (var1 == 1) {
                  if (var6 < var7) {
                     this.containerView.removeView(this.listView[0]);
                     this.containerView.addView(this.listView[0], var7);
                  }
               } else if (var7 < var6) {
                  this.containerView.removeView(this.listView[0]);
                  this.containerView.addView(this.listView[0], var6);
               }

               this.pageSwitchAnimation = new AnimatorSet();
               this.listView[0].setVisibility(0);
               final byte var27;
               if (var1 == 1) {
                  var27 = 0;
               } else {
                  var27 = 1;
               }

               this.listView[var27].setBackgroundColor(this.backgroundPaint.getColor());
               if (VERSION.SDK_INT >= 18) {
                  this.listView[var27].setLayerType(2, (Paint)null);
               }

               if (var1 == 1) {
                  this.pageSwitchAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.listView[0], View.TRANSLATION_X, new float[]{(float)AndroidUtilities.dp(56.0F), 0.0F}), ObjectAnimator.ofFloat(this.listView[0], View.ALPHA, new float[]{0.0F, 1.0F})});
               } else if (var1 == -1) {
                  this.listView[0].setAlpha(1.0F);
                  this.listView[0].setTranslationX(0.0F);
                  this.pageSwitchAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.listView[1], View.TRANSLATION_X, new float[]{0.0F, (float)AndroidUtilities.dp(56.0F)}), ObjectAnimator.ofFloat(this.listView[1], View.ALPHA, new float[]{1.0F, 0.0F})});
               }

               this.pageSwitchAnimation.setDuration(150L);
               this.pageSwitchAnimation.setInterpolator(this.interpolator);
               this.pageSwitchAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     ArticleViewer.this.listView[1].setVisibility(8);
                     ArticleViewer.this.listView[var27].setBackgroundDrawable((Drawable)null);
                     if (VERSION.SDK_INT >= 18) {
                        ArticleViewer.this.listView[var27].setLayerType(0, (Paint)null);
                     }

                     ArticleViewer.this.pageSwitchAnimation = null;
                  }
               });
               this.pageSwitchAnimation.start();
            }

            this.headerView.invalidate();
            this.adapter[0].cleanup();
            int var8 = this.currentPage.cached_page.blocks.size();

            for(var7 = 0; var7 < var8; ++var7) {
               TLRPC.PageBlock var16 = (TLRPC.PageBlock)this.currentPage.cached_page.blocks.get(var7);
               if (var7 == 0) {
                  var16.first = true;
                  if (var16 instanceof TLRPC.TL_pageBlockCover) {
                     TLRPC.TL_pageBlockCover var20 = (TLRPC.TL_pageBlockCover)var16;
                     TLRPC.RichText var21 = this.getBlockCaption(var20, 0);
                     TLRPC.RichText var22 = this.getBlockCaption(var20, 1);
                     if ((var21 != null && !(var21 instanceof TLRPC.TL_textEmpty) || var22 != null && !(var22 instanceof TLRPC.TL_textEmpty)) && var8 > 1) {
                        TLRPC.PageBlock var23 = (TLRPC.PageBlock)this.currentPage.cached_page.blocks.get(1);
                        if (var23 instanceof TLRPC.TL_pageBlockChannel) {
                           this.channelBlock = (TLRPC.TL_pageBlockChannel)var23;
                        }
                     }
                  }
               } else if (var7 == 1 && this.channelBlock != null) {
                  continue;
               }

               var13 = this.adapter[0];
               if (var7 == var8 - 1) {
                  var6 = var7;
               } else {
                  var6 = 0;
               }

               var13.addBlock(var16, 0, 0, var6);
            }

            this.adapter[0].notifyDataSetChanged();
            if (this.pagesStack.size() != 1 && var1 != -1) {
               this.layoutManager[0].scrollToPositionWithOffset(0, 0);
            } else {
               SharedPreferences var19 = ApplicationLoader.applicationContext.getSharedPreferences("articles", 0);
               StringBuilder var25 = new StringBuilder();
               var25.append("article");
               var25.append(this.currentPage.id);
               var11 = var25.toString();
               var7 = var19.getInt(var11, -1);
               StringBuilder var24 = new StringBuilder();
               var24.append(var11);
               var24.append("r");
               boolean var9 = var19.getBoolean(var24.toString(), true);
               Point var26 = AndroidUtilities.displaySize;
               if (var26.x <= var26.y) {
                  var5 = false;
               }

               if (var9 == var5) {
                  var24 = new StringBuilder();
                  var24.append(var11);
                  var24.append("o");
                  var1 = var19.getInt(var24.toString(), 0) - this.listView[0].getPaddingTop();
               } else {
                  var1 = AndroidUtilities.dp(10.0F);
               }

               if (var7 != -1) {
                  this.layoutManager[0].scrollToPositionWithOffset(var7, var1);
               }
            }

            this.checkScrollAnimated();
         }
      }

   }

   private void updateMinMax(float var1) {
      int var2 = (int)((float)this.centerImage.getImageWidth() * var1 - (float)this.getContainerViewWidth()) / 2;
      int var3 = (int)((float)this.centerImage.getImageHeight() * var1 - (float)this.getContainerViewHeight()) / 2;
      if (var2 > 0) {
         this.minX = (float)(-var2);
         this.maxX = (float)var2;
      } else {
         this.maxX = 0.0F;
         this.minX = 0.0F;
      }

      if (var3 > 0) {
         this.minY = (float)(-var3);
         this.maxY = (float)var3;
      } else {
         this.maxY = 0.0F;
         this.minY = 0.0F;
      }

   }

   private void updateNightModeButton() {
      ImageView var1 = this.nightModeImageView;
      boolean var2;
      if (this.selectedColor != 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      var1.setEnabled(var2);
      var1 = this.nightModeImageView;
      float var3;
      if (this.selectedColor == 2) {
         var3 = 0.5F;
      } else {
         var3 = 1.0F;
      }

      var1.setAlpha(var3);
      var1 = this.nightModeImageView;
      int var4;
      if (this.nightModeEnabled && this.selectedColor != 2) {
         var4 = -15428119;
      } else {
         var4 = -3355444;
      }

      var1.setColorFilter(new PorterDuffColorFilter(var4, Mode.MULTIPLY));
   }

   private void updatePaintColors() {
      Context var1 = ApplicationLoader.applicationContext;
      byte var2 = 0;
      byte var3 = 0;
      int var4 = 0;
      var1.getSharedPreferences("articles", 0).edit().putInt("font_color", this.selectedColor).commit();
      int var5 = this.getSelectedColor();
      RecyclerListView[] var6;
      if (var5 == 0) {
         this.backgroundPaint.setColor(-1);

         while(true) {
            var6 = this.listView;
            if (var4 >= var6.length) {
               break;
            }

            var6[var4].setGlowColor(-657673);
            ++var4;
         }
      } else if (var5 == 1) {
         this.backgroundPaint.setColor(-659492);
         var4 = var2;

         while(true) {
            var6 = this.listView;
            if (var4 >= var6.length) {
               break;
            }

            var6[var4].setGlowColor(-659492);
            ++var4;
         }
      } else if (var5 == 2) {
         this.backgroundPaint.setColor(-15461356);
         var4 = var3;

         while(true) {
            var6 = this.listView;
            if (var4 >= var6.length) {
               break;
            }

            var6[var4].setGlowColor(-15461356);
            ++var4;
         }
      }

      TextPaint var7 = listTextPointerPaint;
      if (var7 != null) {
         var7.setColor(this.getTextColor());
      }

      var7 = listTextNumPaint;
      if (var7 != null) {
         var7.setColor(this.getTextColor());
      }

      var7 = embedPostAuthorPaint;
      if (var7 != null) {
         var7.setColor(this.getTextColor());
      }

      var7 = channelNamePaint;
      if (var7 != null) {
         if (this.channelBlock == null) {
            var7.setColor(this.getTextColor());
         } else {
            var7.setColor(-1);
         }
      }

      var7 = relatedArticleHeaderPaint;
      if (var7 != null) {
         var7.setColor(this.getTextColor());
      }

      var7 = relatedArticleTextPaint;
      if (var7 != null) {
         var7.setColor(this.getGrayTextColor());
      }

      var7 = embedPostDatePaint;
      if (var7 != null) {
         if (var5 == 0) {
            var7.setColor(-7366752);
         } else {
            var7.setColor(this.getGrayTextColor());
         }
      }

      this.createPaint(true);
      this.setMapColors(titleTextPaints);
      this.setMapColors(kickerTextPaints);
      this.setMapColors(subtitleTextPaints);
      this.setMapColors(headerTextPaints);
      this.setMapColors(subheaderTextPaints);
      this.setMapColors(quoteTextPaints);
      this.setMapColors(preformattedTextPaints);
      this.setMapColors(paragraphTextPaints);
      this.setMapColors(listTextPaints);
      this.setMapColors(embedPostTextPaints);
      this.setMapColors(mediaCaptionTextPaints);
      this.setMapColors(mediaCreditTextPaints);
      this.setMapColors(photoCaptionTextPaints);
      this.setMapColors(photoCreditTextPaints);
      this.setMapColors(authorTextPaints);
      this.setMapColors(footerTextPaints);
      this.setMapColors(embedPostCaptionTextPaints);
      this.setMapColors(relatedArticleTextPaints);
      this.setMapColors(detailsTextPaints);
      this.setMapColors(tableTextPaints);
   }

   private void updatePaintFonts() {
      Context var1 = ApplicationLoader.applicationContext;
      byte var2 = 0;
      var1.getSharedPreferences("articles", 0).edit().putInt("font_type", this.selectedFont).commit();
      Typeface var8;
      if (this.selectedFont == 0) {
         var8 = Typeface.DEFAULT;
      } else {
         var8 = Typeface.SERIF;
      }

      Typeface var3;
      if (this.selectedFont == 0) {
         var3 = AndroidUtilities.getTypeface("fonts/ritalic.ttf");
      } else {
         var3 = Typeface.create("serif", 2);
      }

      Typeface var4;
      if (this.selectedFont == 0) {
         var4 = AndroidUtilities.getTypeface("fonts/rmedium.ttf");
      } else {
         var4 = Typeface.create("serif", 1);
      }

      Typeface var5;
      if (this.selectedFont == 0) {
         var5 = AndroidUtilities.getTypeface("fonts/rmediumitalic.ttf");
      } else {
         var5 = Typeface.create("serif", 3);
      }

      int var6;
      for(var6 = 0; var6 < quoteTextPaints.size(); ++var6) {
         this.updateFontEntry(quoteTextPaints.keyAt(var6), (TextPaint)quoteTextPaints.valueAt(var6), var8, var5, var4, var3);
      }

      for(var6 = 0; var6 < preformattedTextPaints.size(); ++var6) {
         this.updateFontEntry(preformattedTextPaints.keyAt(var6), (TextPaint)preformattedTextPaints.valueAt(var6), var8, var5, var4, var3);
      }

      for(var6 = 0; var6 < paragraphTextPaints.size(); ++var6) {
         this.updateFontEntry(paragraphTextPaints.keyAt(var6), (TextPaint)paragraphTextPaints.valueAt(var6), var8, var5, var4, var3);
      }

      for(var6 = 0; var6 < listTextPaints.size(); ++var6) {
         this.updateFontEntry(listTextPaints.keyAt(var6), (TextPaint)listTextPaints.valueAt(var6), var8, var5, var4, var3);
      }

      for(var6 = 0; var6 < embedPostTextPaints.size(); ++var6) {
         this.updateFontEntry(embedPostTextPaints.keyAt(var6), (TextPaint)embedPostTextPaints.valueAt(var6), var8, var5, var4, var3);
      }

      for(var6 = 0; var6 < mediaCaptionTextPaints.size(); ++var6) {
         this.updateFontEntry(mediaCaptionTextPaints.keyAt(var6), (TextPaint)mediaCaptionTextPaints.valueAt(var6), var8, var5, var4, var3);
      }

      for(var6 = 0; var6 < mediaCreditTextPaints.size(); ++var6) {
         this.updateFontEntry(mediaCreditTextPaints.keyAt(var6), (TextPaint)mediaCreditTextPaints.valueAt(var6), var8, var5, var4, var3);
      }

      for(var6 = 0; var6 < photoCaptionTextPaints.size(); ++var6) {
         this.updateFontEntry(photoCaptionTextPaints.keyAt(var6), (TextPaint)photoCaptionTextPaints.valueAt(var6), var8, var5, var4, var3);
      }

      for(var6 = 0; var6 < photoCreditTextPaints.size(); ++var6) {
         this.updateFontEntry(photoCreditTextPaints.keyAt(var6), (TextPaint)photoCreditTextPaints.valueAt(var6), var8, var5, var4, var3);
      }

      for(var6 = 0; var6 < authorTextPaints.size(); ++var6) {
         this.updateFontEntry(authorTextPaints.keyAt(var6), (TextPaint)authorTextPaints.valueAt(var6), var8, var5, var4, var3);
      }

      for(var6 = 0; var6 < footerTextPaints.size(); ++var6) {
         this.updateFontEntry(footerTextPaints.keyAt(var6), (TextPaint)footerTextPaints.valueAt(var6), var8, var5, var4, var3);
      }

      for(var6 = 0; var6 < embedPostCaptionTextPaints.size(); ++var6) {
         this.updateFontEntry(embedPostCaptionTextPaints.keyAt(var6), (TextPaint)embedPostCaptionTextPaints.valueAt(var6), var8, var5, var4, var3);
      }

      for(var6 = 0; var6 < relatedArticleTextPaints.size(); ++var6) {
         this.updateFontEntry(relatedArticleTextPaints.keyAt(var6), (TextPaint)relatedArticleTextPaints.valueAt(var6), var8, var5, var4, var3);
      }

      var6 = 0;

      while(true) {
         int var7 = var2;
         if (var6 >= detailsTextPaints.size()) {
            while(var7 < tableTextPaints.size()) {
               this.updateFontEntry(tableTextPaints.keyAt(var7), (TextPaint)tableTextPaints.valueAt(var7), var8, var5, var4, var3);
               ++var7;
            }

            return;
         }

         this.updateFontEntry(detailsTextPaints.keyAt(var6), (TextPaint)detailsTextPaints.valueAt(var6), var8, var5, var4, var3);
         ++var6;
      }
   }

   private void updatePaintSize() {
      Context var1 = ApplicationLoader.applicationContext;
      int var2 = 0;
      var1.getSharedPreferences("articles", 0).edit().putInt("font_size", this.selectedFontSize).commit();

      while(var2 < 2) {
         this.adapter[var2].notifyDataSetChanged();
         ++var2;
      }

   }

   private void updateVideoPlayerTime() {
      VideoPlayer var1 = this.videoPlayer;
      Integer var2 = 0;
      String var7;
      if (var1 == null) {
         var7 = String.format("%02d:%02d / %02d:%02d", var2, var2, var2, var2);
      } else {
         long var3 = var1.getCurrentPosition() / 1000L;
         long var5 = this.videoPlayer.getDuration() / 1000L;
         if (var5 != -9223372036854775807L && var3 != -9223372036854775807L) {
            var7 = String.format("%02d:%02d / %02d:%02d", var3 / 60L, var3 % 60L, var5 / 60L, var5 % 60L);
         } else {
            var7 = String.format("%02d:%02d / %02d:%02d", var2, var2, var2, var2);
         }
      }

      if (!TextUtils.equals(this.videoPlayerTime.getText(), var7)) {
         this.videoPlayerTime.setText(var7);
      }

   }

   private TLRPC.PageBlock wrapInTableBlock(TLRPC.PageBlock var1, TLRPC.PageBlock var2) {
      if (var1 instanceof ArticleViewer.TL_pageBlockListItem) {
         ArticleViewer.TL_pageBlockListItem var6 = (ArticleViewer.TL_pageBlockListItem)var1;
         ArticleViewer.TL_pageBlockListItem var5 = new ArticleViewer.TL_pageBlockListItem();
         var5.parent = var6.parent;
         var5.blockItem = this.wrapInTableBlock(var6.blockItem, var2);
         return var5;
      } else if (var1 instanceof ArticleViewer.TL_pageBlockOrderedListItem) {
         ArticleViewer.TL_pageBlockOrderedListItem var3 = (ArticleViewer.TL_pageBlockOrderedListItem)var1;
         ArticleViewer.TL_pageBlockOrderedListItem var4 = new ArticleViewer.TL_pageBlockOrderedListItem();
         var4.parent = var3.parent;
         var4.blockItem = this.wrapInTableBlock(var3.blockItem, var2);
         return var4;
      } else {
         return var2;
      }
   }

   protected void cancelCheckLongPress() {
      this.checkingForLongPress = false;
      ArticleViewer.CheckForLongPress var1 = this.pendingCheckForLongPress;
      if (var1 != null) {
         this.windowView.removeCallbacks(var1);
         this.pendingCheckForLongPress = null;
      }

      ArticleViewer.CheckForTap var2 = this.pendingCheckForTap;
      if (var2 != null) {
         this.windowView.removeCallbacks(var2);
         this.pendingCheckForTap = null;
      }

   }

   public void close(boolean var1, boolean var2) {
      if (this.parentActivity != null && this.isVisible && !this.checkAnimation()) {
         if (this.fullscreenVideoContainer.getVisibility() == 0) {
            if (this.customView != null) {
               this.fullscreenVideoContainer.setVisibility(4);
               this.customViewCallback.onCustomViewHidden();
               this.fullscreenVideoContainer.removeView(this.customView);
               this.customView = null;
            } else {
               WebPlayerView var3 = this.fullscreenedVideo;
               if (var3 != null) {
                  var3.exitFullscreen();
               }
            }

            if (!var2) {
               return;
            }
         }

         if (this.isPhotoVisible) {
            this.closePhoto(var2 ^ true);
            if (!var2) {
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
         if (var1 && !var2 && this.removeLastPageFromStack()) {
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
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

         AnimatorSet var5 = new AnimatorSet();
         var5.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.windowView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.containerView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.windowView, View.TRANSLATION_X, new float[]{0.0F, (float)AndroidUtilities.dp(56.0F)})});
         this.animationInProgress = 2;
         this.animationEndRunnable = new _$$Lambda$ArticleViewer$d55YwG7qykKR_6ZPmUqu81Dn_rc(this);
         var5.setDuration(150L);
         var5.setInterpolator(this.interpolator);
         var5.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               if (ArticleViewer.this.animationEndRunnable != null) {
                  ArticleViewer.this.animationEndRunnable.run();
                  ArticleViewer.this.animationEndRunnable = null;
               }

            }
         });
         this.transitionAnimationStartTime = System.currentTimeMillis();
         if (VERSION.SDK_INT >= 18) {
            this.containerView.setLayerType(2, (Paint)null);
         }

         var5.start();
      }

   }

   public void closePhoto(boolean var1) {
      if (this.parentActivity != null && this.isPhotoVisible && !this.checkPhotoAnimation()) {
         this.releasePlayer();
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidFailedLoad);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidLoad);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileLoadProgressChanged);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needSetDayNightTheme);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
         this.isActionBarVisible = false;
         VelocityTracker var2 = this.velocityTracker;
         if (var2 != null) {
            var2.recycle();
            this.velocityTracker = null;
         }

         ArticleViewer.PlaceProviderObject var3 = this.getPlaceForPhoto(this.currentMedia);
         if (!var1) {
            this.photoContainerView.setVisibility(4);
            this.photoContainerBackground.setVisibility(4);
            this.photoAnimationInProgress = 0;
            this.onPhotoClosed(var3);
            this.photoContainerView.setScaleX(1.0F);
            this.photoContainerView.setScaleY(1.0F);
         } else {
            AnimatorSet var4;
            android.view.ViewGroup.LayoutParams var5;
            int var6;
            int var7;
            label78: {
               this.photoAnimationInProgress = 1;
               this.animatingImageView.setVisibility(0);
               this.photoContainerView.invalidate();
               var4 = new AnimatorSet();
               var5 = this.animatingImageView.getLayoutParams();
               var6 = this.centerImage.getOrientation();
               if (var3 != null) {
                  ImageReceiver var18 = var3.imageReceiver;
                  if (var18 != null) {
                     var7 = var18.getAnimatedOrientation();
                     break label78;
                  }
               }

               var7 = 0;
            }

            if (var7 != 0) {
               var6 = var7;
            }

            this.animatingImageView.setOrientation(var6);
            RectF var20;
            if (var3 != null) {
               ClippingImageView var19 = this.animatingImageView;
               if (var3.radius != 0) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               var19.setNeedRadius(var1);
               var20 = var3.imageReceiver.getDrawRegion();
               var5.width = (int)var20.width();
               var5.height = (int)var20.height();
               this.animatingImageView.setImageBitmap(var3.thumb);
            } else {
               this.animatingImageView.setNeedRadius(false);
               var5.width = this.centerImage.getImageWidth();
               var5.height = this.centerImage.getImageHeight();
               this.animatingImageView.setImageBitmap(this.centerImage.getBitmapSafe());
               var20 = null;
            }

            this.animatingImageView.setLayoutParams(var5);
            Point var8 = AndroidUtilities.displaySize;
            float var9 = (float)var8.x / (float)var5.width;
            float var10 = (float)(var8.y + AndroidUtilities.statusBarHeight) / (float)var5.height;
            if (var9 > var10) {
               var9 = var10;
            }

            var10 = (float)var5.width;
            float var11 = this.scale;
            float var12 = (float)var5.height;
            float var13 = ((float)AndroidUtilities.displaySize.x - var10 * var11 * var9) / 2.0F;
            var10 = var13;
            if (VERSION.SDK_INT >= 21) {
               Object var24 = this.lastInsets;
               var10 = var13;
               if (var24 != null) {
                  var10 = var13 + (float)((WindowInsets)var24).getSystemWindowInsetLeft();
               }
            }

            var13 = ((float)(AndroidUtilities.displaySize.y + AndroidUtilities.statusBarHeight) - var12 * var11 * var9) / 2.0F;
            this.animatingImageView.setTranslationX(var10 + this.translationX);
            this.animatingImageView.setTranslationY(var13 + this.translationY);
            this.animatingImageView.setScaleX(this.scale * var9);
            this.animatingImageView.setScaleY(this.scale * var9);
            if (var3 != null) {
               var3.imageReceiver.setVisible(false, true);
               if (var3.imageReceiver.isAspectFit()) {
                  var7 = 0;
               } else {
                  var7 = (int)Math.abs(var20.left - (float)var3.imageReceiver.getImageX());
               }

               int var14 = (int)Math.abs(var20.top - (float)var3.imageReceiver.getImageY());
               int[] var25 = new int[2];
               var3.parentView.getLocationInWindow(var25);
               int var15 = (int)((float)var25[1] - ((float)var3.viewY + var20.top) + (float)var3.clipTopAddition);
               var6 = var15;
               if (var15 < 0) {
                  var6 = 0;
               }

               var9 = (float)var3.viewY;
               var10 = var20.top;
               int var16 = (int)(var9 + var10 + (var20.bottom - var10) - (float)(var25[1] + var3.parentView.getHeight()) + (float)var3.clipBottomAddition);
               var15 = var16;
               if (var16 < 0) {
                  var15 = 0;
               }

               var6 = Math.max(var6, var14);
               var15 = Math.max(var15, var14);
               this.animationValues[0][0] = this.animatingImageView.getScaleX();
               this.animationValues[0][1] = this.animatingImageView.getScaleY();
               this.animationValues[0][2] = this.animatingImageView.getTranslationX();
               this.animationValues[0][3] = this.animatingImageView.getTranslationY();
               float[][] var26 = this.animationValues;
               var26[0][4] = 0.0F;
               var26[0][5] = 0.0F;
               var26[0][6] = 0.0F;
               var26[0][7] = 0.0F;
               var26[0][8] = 0.0F;
               var26[0][9] = 0.0F;
               float[] var28 = var26[1];
               var9 = var3.scale;
               var28[0] = var9;
               var26[1][1] = var9;
               var26[1][2] = (float)var3.viewX + var20.left * var9;
               var26[1][3] = (float)var3.viewY + var20.top * var9;
               float[] var21 = var26[1];
               var10 = (float)var7;
               var21[4] = var10 * var9;
               var26[1][5] = (float)var6 * var9;
               var26[1][6] = (float)var15 * var9;
               var26[1][7] = (float)var3.radius;
               var26[1][8] = (float)var14 * var9;
               var26[1][9] = var10 * var9;
               var4.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.animatingImageView, "animationProgress", new float[]{0.0F, 1.0F}), ObjectAnimator.ofInt(this.photoBackgroundDrawable, AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[]{0}), ObjectAnimator.ofFloat(this.actionBar, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.bottomLayout, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.captionTextView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.groupedPhotosListView, View.ALPHA, new float[]{0.0F})});
            } else {
               var7 = AndroidUtilities.displaySize.y + AndroidUtilities.statusBarHeight;
               ObjectAnimator var29 = ObjectAnimator.ofInt(this.photoBackgroundDrawable, AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[]{0});
               ObjectAnimator var22 = ObjectAnimator.ofFloat(this.animatingImageView, View.ALPHA, new float[]{0.0F});
               ClippingImageView var17 = this.animatingImageView;
               Property var27 = View.TRANSLATION_Y;
               if (this.translationY < 0.0F) {
                  var7 = -var7;
               }

               var4.playTogether(new Animator[]{var29, var22, ObjectAnimator.ofFloat(var17, var27, new float[]{(float)var7}), ObjectAnimator.ofFloat(this.actionBar, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.bottomLayout, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.captionTextView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.groupedPhotosListView, View.ALPHA, new float[]{0.0F})});
            }

            this.photoAnimationEndRunnable = new _$$Lambda$ArticleViewer$bddzRYkreqx_hk8WrDDgX9mS_Fw(this, var3);
            var4.setDuration(200L);
            var4.addListener(new AnimatorListenerAdapter() {
               // $FF: synthetic method
               public void lambda$onAnimationEnd$0$ArticleViewer$24() {
                  if (ArticleViewer.this.photoAnimationEndRunnable != null) {
                     ArticleViewer.this.photoAnimationEndRunnable.run();
                     ArticleViewer.this.photoAnimationEndRunnable = null;
                  }

               }

               public void onAnimationEnd(Animator var1) {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$24$PIC8GuTz6cB94k1nZ_KlW1gR_KY(this));
               }
            });
            this.photoTransitionAnimationStartTime = System.currentTimeMillis();
            if (VERSION.SDK_INT >= 18) {
               this.photoContainerView.setLayerType(2, (Paint)null);
            }

            var4.start();
         }

         AnimatedFileDrawable var23 = this.currentAnimation;
         if (var23 != null) {
            var23.setSecondParentView((View)null);
            this.currentAnimation = null;
            this.centerImage.setImageBitmap((Drawable)null);
         }
      }

   }

   public void collapse() {
      if (this.parentActivity != null && this.isVisible && !this.checkAnimation()) {
         if (this.fullscreenVideoContainer.getVisibility() == 0) {
            if (this.customView != null) {
               this.fullscreenVideoContainer.setVisibility(4);
               this.customViewCallback.onCustomViewHidden();
               this.fullscreenVideoContainer.removeView(this.customView);
               this.customView = null;
            } else {
               WebPlayerView var1 = this.fullscreenedVideo;
               if (var1 != null) {
                  var1.exitFullscreen();
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
         } catch (Exception var7) {
            FileLog.e((Throwable)var7);
         }

         AnimatorSet var8 = new AnimatorSet();
         FrameLayout var2 = this.containerView;
         ObjectAnimator var9 = ObjectAnimator.ofFloat(var2, View.TRANSLATION_X, new float[]{(float)(var2.getMeasuredWidth() - AndroidUtilities.dp(56.0F))});
         FrameLayout var3 = this.containerView;
         Property var4 = View.TRANSLATION_Y;
         int var5 = ActionBar.getCurrentActionBarHeight();
         int var6;
         if (VERSION.SDK_INT >= 21) {
            var6 = AndroidUtilities.statusBarHeight;
         } else {
            var6 = 0;
         }

         var8.playTogether(new Animator[]{var9, ObjectAnimator.ofFloat(var3, var4, new float[]{(float)(var5 + var6)}), ObjectAnimator.ofFloat(this.windowView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.listView[0], View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.listView[0], View.TRANSLATION_Y, new float[]{(float)(-AndroidUtilities.dp(56.0F))}), ObjectAnimator.ofFloat(this.headerView, View.TRANSLATION_Y, new float[]{0.0F}), ObjectAnimator.ofFloat(this.backButton, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.backButton, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.backButton, View.TRANSLATION_Y, new float[]{0.0F}), ObjectAnimator.ofFloat(this.shareContainer, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.shareContainer, View.TRANSLATION_Y, new float[]{0.0F}), ObjectAnimator.ofFloat(this.shareContainer, View.SCALE_Y, new float[]{1.0F})});
         this.collapsed = true;
         this.animationInProgress = 2;
         this.animationEndRunnable = new _$$Lambda$ArticleViewer$o2CofdEeGng__h9rKJjob7RhMPo(this);
         var8.setInterpolator(new DecelerateInterpolator());
         var8.setDuration(250L);
         var8.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               if (ArticleViewer.this.animationEndRunnable != null) {
                  ArticleViewer.this.animationEndRunnable.run();
                  ArticleViewer.this.animationEndRunnable = null;
               }

            }
         });
         this.transitionAnimationStartTime = System.currentTimeMillis();
         if (VERSION.SDK_INT >= 18) {
            this.containerView.setLayerType(2, (Paint)null);
         }

         this.backDrawable.setRotation(1.0F, true);
         var8.start();
      }

   }

   public void destroyArticleViewer() {
      if (this.parentActivity != null && this.windowView != null) {
         this.releasePlayer();

         try {
            if (this.windowView.getParent() != null) {
               ((WindowManager)this.parentActivity.getSystemService("window")).removeViewImmediate(this.windowView);
            }

            this.windowView = null;
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

         for(int var2 = 0; var2 < this.createdWebViews.size(); ++var2) {
            ((ArticleViewer.BlockEmbedCell)this.createdWebViews.get(var2)).destroyWebView(true);
         }

         this.createdWebViews.clear();

         try {
            this.parentActivity.getWindow().clearFlags(128);
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

         ImageReceiver.BitmapHolder var1 = this.currentThumb;
         if (var1 != null) {
            var1.release();
            this.currentThumb = null;
         }

         this.animatingImageView.setImageBitmap((ImageReceiver.BitmapHolder)null);
         this.parentActivity = null;
         this.parentFragment = null;
         Instance = null;
      }

   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      int var4 = NotificationCenter.fileDidFailedLoad;
      byte var5 = 0;
      byte var6 = 0;
      byte var10 = 0;
      String var7;
      if (var1 == var4) {
         var7 = (String)var3[0];

         for(var1 = var10; var1 < 3; ++var1) {
            String[] var11 = this.currentFileNames;
            if (var11[var1] != null && var11[var1].equals(var7)) {
               this.radialProgressViews[var1].setProgress(1.0F, true);
               this.checkProgress(var1, true);
               break;
            }
         }
      } else if (var1 == NotificationCenter.fileDidLoad) {
         String var12 = (String)var3[0];

         for(var1 = 0; var1 < 3; ++var1) {
            String[] var20 = this.currentFileNames;
            if (var20[var1] != null && var20[var1].equals(var12)) {
               this.radialProgressViews[var1].setProgress(1.0F, true);
               this.checkProgress(var1, true);
               if (var1 == 0 && this.isMediaVideo(this.currentIndex)) {
                  this.onActionClick(false);
               }
               break;
            }
         }
      } else if (var1 == NotificationCenter.FileLoadProgressChanged) {
         var7 = (String)var3[0];

         for(var1 = var5; var1 < 3; ++var1) {
            String[] var8 = this.currentFileNames;
            if (var8[var1] != null && var8[var1].equals(var7)) {
               Float var22 = (Float)var3[1];
               this.radialProgressViews[var1].setProgress(var22, true);
            }
         }
      } else if (var1 == NotificationCenter.emojiDidLoad) {
         TextView var13 = this.captionTextView;
         if (var13 != null) {
            var13.invalidate();
         }
      } else if (var1 == NotificationCenter.needSetDayNightTheme) {
         if (this.nightModeEnabled && this.selectedColor != 2 && this.adapter != null) {
            this.updatePaintColors();

            for(var1 = var6; var1 < this.listView.length; ++var1) {
               this.adapter[var1].notifyDataSetChanged();
            }
         }
      } else {
         RecyclerListView[] var15;
         View var16;
         int var17;
         if (var1 == NotificationCenter.messagePlayingDidStart) {
            MessageObject var14 = (MessageObject)var3[0];
            if (this.listView != null) {
               var1 = 0;

               while(true) {
                  var15 = this.listView;
                  if (var1 >= var15.length) {
                     break;
                  }

                  var17 = var15[var1].getChildCount();

                  for(var2 = 0; var2 < var17; ++var2) {
                     var16 = this.listView[var1].getChildAt(var2);
                     if (var16 instanceof ArticleViewer.BlockAudioCell) {
                        ((ArticleViewer.BlockAudioCell)var16).updateButtonState(true);
                     }
                  }

                  ++var1;
               }
            }
         } else if (var1 != NotificationCenter.messagePlayingDidReset && var1 != NotificationCenter.messagePlayingPlayStateChanged) {
            if (var1 == NotificationCenter.messagePlayingProgressDidChanged) {
               Integer var19 = (Integer)var3[0];
               if (this.listView != null) {
                  var1 = 0;

                  while(true) {
                     RecyclerListView[] var21 = this.listView;
                     if (var1 >= var21.length) {
                        break;
                     }

                     var17 = var21[var1].getChildCount();

                     for(var2 = 0; var2 < var17; ++var2) {
                        View var23 = this.listView[var1].getChildAt(var2);
                        if (var23 instanceof ArticleViewer.BlockAudioCell) {
                           ArticleViewer.BlockAudioCell var9 = (ArticleViewer.BlockAudioCell)var23;
                           MessageObject var24 = var9.getMessageObject();
                           if (var24 != null && var24.getId() == var19) {
                              MessageObject var25 = MediaController.getInstance().getPlayingMessageObject();
                              if (var25 != null) {
                                 var24.audioProgress = var25.audioProgress;
                                 var24.audioProgressSec = var25.audioProgressSec;
                                 var24.audioPlayerDuration = var25.audioPlayerDuration;
                                 var9.updatePlayingMessageProgress();
                              }
                              break;
                           }
                        }
                     }

                     ++var1;
                  }
               }
            }
         } else if (this.listView != null) {
            var1 = 0;

            while(true) {
               var15 = this.listView;
               if (var1 >= var15.length) {
                  break;
               }

               var17 = var15[var1].getChildCount();

               for(var2 = 0; var2 < var17; ++var2) {
                  var16 = this.listView[var1].getChildAt(var2);
                  if (var16 instanceof ArticleViewer.BlockAudioCell) {
                     ArticleViewer.BlockAudioCell var18 = (ArticleViewer.BlockAudioCell)var16;
                     if (var18.getMessageObject() != null) {
                        var18.updateButtonState(true);
                     }
                  }
               }

               ++var1;
            }
         }
      }

   }

   @Keep
   public float getAnimationValue() {
      return this.animationValue;
   }

   public boolean isShowingImage(TLRPC.PageBlock var1) {
      boolean var2;
      if (this.isPhotoVisible && !this.disableShowCheck && var1 != null && this.currentMedia == var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isVisible() {
      return this.isVisible;
   }

   // $FF: synthetic method
   public void lambda$checkScrollAnimated$21$ArticleViewer(ValueAnimator var1) {
      this.setCurrentHeaderHeight((Integer)var1.getAnimatedValue());
   }

   // $FF: synthetic method
   public void lambda$close$28$ArticleViewer() {
      FrameLayout var1 = this.containerView;
      if (var1 != null) {
         if (VERSION.SDK_INT >= 18) {
            var1.setLayerType(0, (Paint)null);
         }

         this.animationInProgress = 0;
         this.onClosed();
      }
   }

   // $FF: synthetic method
   public void lambda$closePhoto$40$ArticleViewer(ArticleViewer.PlaceProviderObject var1) {
      if (VERSION.SDK_INT >= 18) {
         this.photoContainerView.setLayerType(0, (Paint)null);
      }

      this.photoContainerView.setVisibility(4);
      this.photoContainerBackground.setVisibility(4);
      this.photoAnimationInProgress = 0;
      this.onPhotoClosed(var1);
   }

   // $FF: synthetic method
   public void lambda$collapse$26$ArticleViewer() {
      FrameLayout var1 = this.containerView;
      if (var1 != null) {
         if (VERSION.SDK_INT >= 18) {
            var1.setLayerType(0, (Paint)null);
         }

         this.animationInProgress = 0;
         ((WindowManager)this.parentActivity.getSystemService("window")).updateViewLayout(this.windowView, this.windowLayoutParams);
      }
   }

   // $FF: synthetic method
   public void lambda$drawContent$42$ArticleViewer() {
      this.setImageIndex(this.currentIndex + 1, false);
   }

   // $FF: synthetic method
   public void lambda$drawContent$43$ArticleViewer() {
      this.setImageIndex(this.currentIndex - 1, false);
   }

   // $FF: synthetic method
   public void lambda$joinChannel$35$ArticleViewer(ArticleViewer.BlockChannelCell var1, int var2, TLRPC.TL_channels_joinChannel var3, TLRPC.Chat var4, TLObject var5, TLRPC.TL_error var6) {
      if (var6 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$_hnQGBrCa7S7qlSM2IdUcfGIHl4(this, var1, var2, var6, var3));
      } else {
         TLRPC.Updates var8 = (TLRPC.Updates)var5;
         int var7 = 0;

         boolean var10;
         while(true) {
            if (var7 >= var8.updates.size()) {
               var10 = false;
               break;
            }

            TLRPC.Update var9 = (TLRPC.Update)var8.updates.get(var7);
            if (var9 instanceof TLRPC.TL_updateNewChannelMessage && ((TLRPC.TL_updateNewChannelMessage)var9).message.action instanceof TLRPC.TL_messageActionChatAddUser) {
               var10 = true;
               break;
            }

            ++var7;
         }

         MessagesController.getInstance(var2).processUpdates(var8, false);
         if (!var10) {
            MessagesController.getInstance(var2).generateJoinMessage(var4.id, true);
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$8J_5MDu2I5a8EY_PWwIvLvSitBE(var1));
         AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$Cq82CY1mMDVABDekDXqZnXYfB8o(var2, var4), 1000L);
         MessagesStorage.getInstance(var2).updateDialogsWithDeletedMessages(new ArrayList(), (ArrayList)null, true, var4.id);
      }
   }

   // $FF: synthetic method
   public void lambda$loadChannel$31$ArticleViewer(ArticleViewer.WebpageAdapter var1, int var2, ArticleViewer.BlockChannelCell var3, TLObject var4, TLRPC.TL_error var5) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$OU_4EBuNPSDwj_f4lawwZA9jcMI(this, var1, var5, var4, var2, var3));
   }

   // $FF: synthetic method
   public void lambda$null$10$ArticleViewer(int var1, long var2, TLObject var4, TLRPC.TL_error var5) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$RMBAO5TCJgoWk_O4MzZOP5_N2n4(this, var4, var1, var2));
   }

   // $FF: synthetic method
   public void lambda$null$22$ArticleViewer(TLRPC.WebPage var1, TLRPC.TL_webPage var2, MessageObject var3, String var4) {
      if (!this.pagesStack.isEmpty() && this.pagesStack.get(0) == var1 && var2.cached_page != null) {
         if (var3 != null) {
            var3.messageOwner.media.webpage = var2;
         }

         this.pagesStack.set(0, var2);
         if (this.pagesStack.size() == 1) {
            this.currentPage = var2;
            Editor var6 = ApplicationLoader.applicationContext.getSharedPreferences("articles", 0).edit();
            StringBuilder var5 = new StringBuilder();
            var5.append("article");
            var5.append(this.currentPage.id);
            var6.remove(var5.toString()).commit();
            this.updateInterfaceForCurrentPage(0);
            if (var4 != null) {
               this.scrollToAnchor(var4);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$30$ArticleViewer(ArticleViewer.WebpageAdapter var1, TLRPC.TL_error var2, TLObject var3, int var4, ArticleViewer.BlockChannelCell var5) {
      this.loadingChannel = false;
      if (this.parentFragment != null && !var1.blocks.isEmpty()) {
         if (var2 == null) {
            TLRPC.TL_contacts_resolvedPeer var6 = (TLRPC.TL_contacts_resolvedPeer)var3;
            if (!var6.chats.isEmpty()) {
               MessagesController.getInstance(var4).putUsers(var6.users, false);
               MessagesController.getInstance(var4).putChats(var6.chats, false);
               MessagesStorage.getInstance(var4).putUsersAndChats(var6.users, var6.chats, false, true);
               this.loadedChannel = (TLRPC.Chat)var6.chats.get(0);
               TLRPC.Chat var7 = this.loadedChannel;
               if (var7.left && !var7.kicked) {
                  var5.setState(0, false);
               } else {
                  var5.setState(4, false);
               }
            } else {
               var5.setState(4, false);
            }
         } else {
            var5.setState(4, false);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$32$ArticleViewer(ArticleViewer.BlockChannelCell var1, int var2, TLRPC.TL_error var3, TLRPC.TL_channels_joinChannel var4) {
      var1.setState(0, false);
      AlertsCreator.processError(var2, var3, this.parentFragment, var4, true);
   }

   // $FF: synthetic method
   public void lambda$null$5$ArticleViewer(int var1, TLObject var2, String var3, TLRPC.TL_messages_getWebPage var4) {
      if (this.openUrlReqId != 0 && var1 == this.lastReqId) {
         this.openUrlReqId = 0;
         this.showProgressView(true, false);
         if (this.isVisible) {
            if (var2 instanceof TLRPC.TL_webPage) {
               TLRPC.TL_webPage var5 = (TLRPC.TL_webPage)var2;
               if (var5.cached_page instanceof TLRPC.TL_page) {
                  this.addPageToStack(var5, var3, 1);
                  return;
               }
            }

            Browser.openUrl(this.parentActivity, (String)var4.url);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$9$ArticleViewer(TLObject var1, int var2, long var3) {
      if (this.previewsReqId != 0) {
         this.previewsReqId = 0;
         this.showProgressView(true, false);
         if (var1 != null) {
            TLRPC.TL_contacts_resolvedPeer var5 = (TLRPC.TL_contacts_resolvedPeer)var1;
            MessagesController.getInstance(var2).putUsers(var5.users, false);
            MessagesStorage.getInstance(var2).putUsersAndChats(var5.users, var5.chats, false, true);
            if (!var5.users.isEmpty()) {
               this.openPreviewsChat((TLRPC.User)var5.users.get(0), var3);
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$onClosed$29$ArticleViewer() {
      try {
         if (this.windowView.getParent() != null) {
            ((WindowManager)this.parentActivity.getSystemService("window")).removeView(this.windowView);
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   // $FF: synthetic method
   public void lambda$onPhotoClosed$41$ArticleViewer() {
      this.animatingImageView.setImageBitmap((ImageReceiver.BitmapHolder)null);
   }

   // $FF: synthetic method
   public void lambda$open$23$ArticleViewer(TLRPC.WebPage var1, MessageObject var2, String var3, int var4, TLObject var5, TLRPC.TL_error var6) {
      if (var5 instanceof TLRPC.TL_webPage) {
         TLRPC.TL_webPage var7 = (TLRPC.TL_webPage)var5;
         if (var7.cached_page == null) {
            return;
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$l3_iiPHXy4_uEQn5oag7li6c_dQ(this, var1, var7, var2, var3));
         LongSparseArray var8 = new LongSparseArray(1);
         var8.put(var7.id, var7);
         MessagesStorage.getInstance(var4).putWebPages(var8);
      }

   }

   // $FF: synthetic method
   public void lambda$open$24$ArticleViewer() {
      FrameLayout var1 = this.containerView;
      if (var1 != null && this.windowView != null) {
         if (VERSION.SDK_INT >= 18) {
            var1.setLayerType(0, (Paint)null);
         }

         this.animationInProgress = 0;
         AndroidUtilities.hideKeyboard(this.parentActivity.getCurrentFocus());
      }

   }

   // $FF: synthetic method
   public void lambda$open$25$ArticleViewer(AnimatorSet var1) {
      NotificationCenter.getInstance(this.currentAccount).setAllowedNotificationsDutingAnimation(new int[]{NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats});
      NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(true);
      var1.start();
   }

   // $FF: synthetic method
   public void lambda$openPhoto$37$ArticleViewer() {
      ArticleViewer.FrameLayoutDrawer var1 = this.photoContainerView;
      if (var1 != null) {
         if (VERSION.SDK_INT >= 18) {
            var1.setLayerType(0, (Paint)null);
         }

         this.photoAnimationInProgress = 0;
         this.photoTransitionAnimationStartTime = 0L;
         this.setImages();
         this.photoContainerView.invalidate();
         this.animatingImageView.setVisibility(8);
         ArticleViewer.PlaceProviderObject var2 = this.showAfterAnimation;
         if (var2 != null) {
            var2.imageReceiver.setVisible(true, true);
         }

         var2 = this.hideAfterAnimation;
         if (var2 != null) {
            var2.imageReceiver.setVisible(false, true);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$openPhoto$38$ArticleViewer(AnimatorSet var1) {
      NotificationCenter.getInstance(this.currentAccount).setAllowedNotificationsDutingAnimation(new int[]{NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats});
      NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(true);
      var1.start();
   }

   // $FF: synthetic method
   public void lambda$openPhoto$39$ArticleViewer(ArticleViewer.PlaceProviderObject var1) {
      this.disableShowCheck = false;
      var1.imageReceiver.setVisible(false, true);
   }

   // $FF: synthetic method
   public void lambda$openWebpageUrl$6$ArticleViewer(int var1, String var2, TLRPC.TL_messages_getWebPage var3, TLObject var4, TLRPC.TL_error var5) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$wFBfqyMKleu_xH4t_92wfKx_WCw(this, var1, var4, var2, var3));
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$11$ArticleViewer(ArticleViewer.WebpageAdapter var1, View var2, int var3) {
      if (var3 == var1.localBlocks.size() && this.currentPage != null) {
         if (this.previewsReqId != 0) {
            return;
         }

         TLObject var13 = MessagesController.getInstance(this.currentAccount).getUserOrChat("previews");
         if (var13 instanceof TLRPC.TL_user) {
            this.openPreviewsChat((TLRPC.User)var13, this.currentPage.id);
         } else {
            var3 = UserConfig.selectedAccount;
            long var4 = this.currentPage.id;
            this.showProgressView(true, true);
            TLRPC.TL_contacts_resolveUsername var14 = new TLRPC.TL_contacts_resolveUsername();
            var14.username = "previews";
            this.previewsReqId = ConnectionsManager.getInstance(var3).sendRequest(var14, new _$$Lambda$ArticleViewer$ggiuvz6vZ37WaSls81dRSwQOWdc(this, var3, var4));
         }
      } else if (var3 >= 0 && var3 < var1.localBlocks.size()) {
         TLRPC.PageBlock var6 = (TLRPC.PageBlock)var1.localBlocks.get(var3);
         TLRPC.PageBlock var7 = this.getLastNonListPageBlock(var6);
         TLRPC.PageBlock var8 = var7;
         if (var7 instanceof ArticleViewer.TL_pageBlockDetailsChild) {
            var8 = ((ArticleViewer.TL_pageBlockDetailsChild)var7).block;
         }

         if (var8 instanceof TLRPC.TL_pageBlockChannel) {
            TLRPC.TL_pageBlockChannel var11 = (TLRPC.TL_pageBlockChannel)var8;
            MessagesController.getInstance(this.currentAccount).openByUserName(var11.channel.username, this.parentFragment, 2);
            this.close(false, true);
         } else if (var8 instanceof ArticleViewer.TL_pageBlockRelatedArticlesChild) {
            ArticleViewer.TL_pageBlockRelatedArticlesChild var12 = (ArticleViewer.TL_pageBlockRelatedArticlesChild)var8;
            this.openWebpageUrl(((TLRPC.TL_pageRelatedArticle)var12.parent.articles.get(var12.num)).url, (String)null);
         } else if (var8 instanceof TLRPC.TL_pageBlockDetails) {
            View var16 = this.getLastNonListCell(var2);
            if (!(var16 instanceof ArticleViewer.BlockDetailsCell)) {
               return;
            }

            this.pressedLinkOwnerLayout = null;
            this.pressedLinkOwnerView = null;
            if (var1.blocks.indexOf(var6) < 0) {
               return;
            }

            TLRPC.TL_pageBlockDetails var15 = (TLRPC.TL_pageBlockDetails)var8;
            var15.open ^= true;
            int var9 = var1.getItemCount();
            var1.updateRows();
            var9 = Math.abs(var1.getItemCount() - var9);
            ArticleViewer.BlockDetailsCell var17 = (ArticleViewer.BlockDetailsCell)var16;
            AnimatedArrowDrawable var18 = var17.arrow;
            float var10;
            if (var15.open) {
               var10 = 0.0F;
            } else {
               var10 = 1.0F;
            }

            var18.setAnimationProgressAnimated(var10);
            var17.invalidate();
            if (var9 != 0) {
               if (var15.open) {
                  var1.notifyItemRangeInserted(var3 + 1, var9);
               } else {
                  var1.notifyItemRangeRemoved(var3 + 1, var9);
               }
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$setParentActivity$13$ArticleViewer(View var1) {
      this.close(true, true);
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$14$ArticleViewer() {
      float var1 = 0.7F - this.lineProgressView.getCurrentProgress();
      if (var1 > 0.0F) {
         if (var1 < 0.25F) {
            var1 = 0.01F;
         } else {
            var1 = 0.02F;
         }

         LineProgressView var2 = this.lineProgressView;
         var2.setProgress(var2.getCurrentProgress() + var1, true);
         AndroidUtilities.runOnUIThread(this.lineProgressTickRunnable, 100L);
      }

   }

   // $FF: synthetic method
   public void lambda$setParentActivity$15$ArticleViewer(View var1) {
      this.nightModeEnabled ^= true;
      Context var3 = ApplicationLoader.applicationContext;
      int var2 = 0;
      var3.getSharedPreferences("articles", 0).edit().putBoolean("nightModeEnabled", this.nightModeEnabled).commit();
      this.updateNightModeButton();
      this.updatePaintColors();

      while(var2 < this.listView.length) {
         this.adapter[var2].notifyDataSetChanged();
         ++var2;
      }

      if (this.nightModeEnabled) {
         this.showNightModeHint();
      }

   }

   // $FF: synthetic method
   public void lambda$setParentActivity$16$ArticleViewer(View var1) {
      int var2 = (Integer)var1.getTag();
      this.selectedColor = var2;
      byte var3 = 0;

      int var4;
      for(var4 = 0; var4 < 3; ++var4) {
         ArticleViewer.ColorCell var6 = this.colorCells[var4];
         boolean var5;
         if (var4 == var2) {
            var5 = true;
         } else {
            var5 = false;
         }

         var6.select(var5);
      }

      this.updateNightModeButton();
      this.updatePaintColors();

      for(var4 = var3; var4 < this.listView.length; ++var4) {
         this.adapter[var4].notifyDataSetChanged();
      }

   }

   // $FF: synthetic method
   public void lambda$setParentActivity$17$ArticleViewer(View var1) {
      int var2 = (Integer)var1.getTag();
      this.selectedFont = var2;
      byte var3 = 0;

      int var4;
      for(var4 = 0; var4 < 2; ++var4) {
         ArticleViewer.FontCell var6 = this.fontCells[var4];
         boolean var5;
         if (var4 == var2) {
            var5 = true;
         } else {
            var5 = false;
         }

         var6.select(var5);
      }

      this.updatePaintFonts();

      for(var4 = var3; var4 < this.listView.length; ++var4) {
         this.adapter[var4].notifyDataSetChanged();
      }

   }

   // $FF: synthetic method
   public void lambda$setParentActivity$18$ArticleViewer(View var1) {
      TLRPC.WebPage var2 = this.currentPage;
      if (var2 != null) {
         Activity var3 = this.parentActivity;
         if (var3 != null) {
            String var4 = var2.url;
            this.showDialog(new ShareAlert(var3, (ArrayList)null, var4, false, var4, true));
         }
      }

   }

   // $FF: synthetic method
   public void lambda$setParentActivity$19$ArticleViewer(float var1) {
      VideoPlayer var2 = this.videoPlayer;
      if (var2 != null) {
         var2.seekTo((long)((int)(var1 * (float)var2.getDuration())));
      }

   }

   // $FF: synthetic method
   public void lambda$setParentActivity$20$ArticleViewer(View var1) {
      VideoPlayer var2 = this.videoPlayer;
      if (var2 != null) {
         if (this.isPlaying) {
            var2.pause();
         } else {
            var2.play();
         }
      }

   }

   // $FF: synthetic method
   public WindowInsets lambda$setParentActivity$7$ArticleViewer(View var1, WindowInsets var2) {
      WindowInsets var4 = (WindowInsets)this.lastInsets;
      this.lastInsets = var2;
      if (var4 == null || !var4.toString().equals(var2.toString())) {
         this.windowView.requestLayout();
      }

      if (VERSION.SDK_INT >= 28) {
         DisplayCutout var5 = this.parentActivity.getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
         if (var5 != null) {
            List var6 = var5.getBoundingRects();
            if (var6 != null && !var6.isEmpty()) {
               boolean var3 = false;
               if (((Rect)var6.get(0)).height() != 0) {
                  var3 = true;
               }

               this.hasCutout = var3;
            }
         }
      }

      return var2.consumeSystemWindowInsets();
   }

   // $FF: synthetic method
   public boolean lambda$setParentActivity$8$ArticleViewer(View var1, int var2) {
      if (var1 instanceof ArticleViewer.BlockRelatedArticlesCell) {
         ArticleViewer.BlockRelatedArticlesCell var3 = (ArticleViewer.BlockRelatedArticlesCell)var1;
         this.showCopyPopup(((TLRPC.TL_pageRelatedArticle)var3.currentBlock.parent.articles.get(var3.currentBlock.num)).url);
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$showCopyPopup$0$ArticleViewer(String var1, DialogInterface var2, int var3) {
      Activity var4 = this.parentActivity;
      if (var4 != null) {
         if (var3 == 0) {
            Browser.openUrl(var4, (String)var1);
         } else if (var3 == 1) {
            String var5;
            if (var1.startsWith("mailto:")) {
               var5 = var1.substring(7);
            } else {
               var5 = var1;
               if (var1.startsWith("tel:")) {
                  var5 = var1.substring(4);
               }
            }

            AndroidUtilities.addToClipboard(var5);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$showDialog$36$ArticleViewer(DialogInterface var1) {
      this.visibleDialog = null;
   }

   // $FF: synthetic method
   public boolean lambda$showPopup$1$ArticleViewer(View var1, MotionEvent var2) {
      if (var2.getActionMasked() == 0) {
         ActionBarPopupWindow var3 = this.popupWindow;
         if (var3 != null && var3.isShowing()) {
            var1.getHitRect(this.popupRect);
            if (!this.popupRect.contains((int)var2.getX(), (int)var2.getY())) {
               this.popupWindow.dismiss();
            }
         }
      }

      return false;
   }

   // $FF: synthetic method
   public void lambda$showPopup$2$ArticleViewer(KeyEvent var1) {
      if (var1.getKeyCode() == 4 && var1.getRepeatCount() == 0) {
         ActionBarPopupWindow var2 = this.popupWindow;
         if (var2 != null && var2.isShowing()) {
            this.popupWindow.dismiss();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$showPopup$3$ArticleViewer(View var1) {
      ArticleViewer.DrawingText var2 = this.pressedLinkOwnerLayout;
      if (var2 != null) {
         AndroidUtilities.addToClipboard(var2.getText());
         Toast.makeText(this.parentActivity, LocaleController.getString("TextCopied", 2131560887), 0).show();
      }

      ActionBarPopupWindow var3 = this.popupWindow;
      if (var3 != null && var3.isShowing()) {
         this.popupWindow.dismiss(true);
      }

   }

   // $FF: synthetic method
   public void lambda$showPopup$4$ArticleViewer() {
      View var1 = this.pressedLinkOwnerView;
      if (var1 != null) {
         this.pressedLinkOwnerLayout = null;
         var1.invalidate();
         this.pressedLinkOwnerView = null;
      }

   }

   // $FF: synthetic method
   public void lambda$uncollapse$27$ArticleViewer() {
      FrameLayout var1 = this.containerView;
      if (var1 != null) {
         if (VERSION.SDK_INT >= 18) {
            var1.setLayerType(0, (Paint)null);
         }

         this.animationInProgress = 0;
      }
   }

   public boolean onDoubleTap(MotionEvent var1) {
      boolean var2 = this.canZoom;
      boolean var3 = false;
      boolean var4 = var3;
      if (var2) {
         if (this.scale == 1.0F) {
            var4 = var3;
            if (this.translationY != 0.0F) {
               return var4;
            }

            if (this.translationX != 0.0F) {
               var4 = var3;
               return var4;
            }
         }

         var4 = var3;
         if (this.animationStartTime == 0L) {
            if (this.photoAnimationInProgress != 0) {
               var4 = var3;
            } else {
               float var5 = this.scale;
               var4 = true;
               if (var5 == 1.0F) {
                  var5 = var1.getX() - (float)(this.getContainerViewWidth() / 2) - (var1.getX() - (float)(this.getContainerViewWidth() / 2) - this.translationX) * (3.0F / this.scale);
                  float var6 = var1.getY() - (float)(this.getContainerViewHeight() / 2) - (var1.getY() - (float)(this.getContainerViewHeight() / 2) - this.translationY) * (3.0F / this.scale);
                  this.updateMinMax(3.0F);
                  float var7 = this.minX;
                  if (var5 < var7) {
                     var5 = var7;
                  } else {
                     var7 = this.maxX;
                     if (var5 > var7) {
                        var5 = var7;
                     }
                  }

                  var7 = this.minY;
                  if (var6 < var7) {
                     var6 = var7;
                  } else {
                     var7 = this.maxY;
                     if (var6 > var7) {
                        var6 = var7;
                     }
                  }

                  this.animateTo(3.0F, var5, var6, true);
               } else {
                  this.animateTo(1.0F, 0.0F, 0.0F, true);
               }

               this.doubleTap = true;
            }
         }
      }

      return var4;
   }

   public boolean onDoubleTapEvent(MotionEvent var1) {
      return false;
   }

   public boolean onDown(MotionEvent var1) {
      return false;
   }

   public boolean onFling(MotionEvent var1, MotionEvent var2, float var3, float var4) {
      if (this.scale != 1.0F) {
         this.scroller.abortAnimation();
         this.scroller.fling(Math.round(this.translationX), Math.round(this.translationY), Math.round(var3), Math.round(var4), (int)this.minX, (int)this.maxX, (int)this.minY, (int)this.maxY);
         this.photoContainerView.postInvalidate();
      }

      return false;
   }

   public void onLongPress(MotionEvent var1) {
   }

   public void onPause() {
      if (this.currentAnimation != null) {
         this.closePhoto(false);
      }

   }

   public boolean onScroll(MotionEvent var1, MotionEvent var2, float var3, float var4) {
      return false;
   }

   public void onShowPress(MotionEvent var1) {
   }

   public boolean onSingleTapConfirmed(MotionEvent var1) {
      if (this.discardTap) {
         return false;
      } else {
         AspectRatioFrameLayout var2 = this.aspectRatioFrameLayout;
         boolean var3;
         if (var2 != null && var2.getVisibility() == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         ArticleViewer.RadialProgressView[] var6 = this.radialProgressViews;
         if (var6[0] != null && this.photoContainerView != null && !var3) {
            int var7 = var6[0].backgroundState;
            if (var7 > 0 && var7 <= 3) {
               float var4 = var1.getX();
               float var5 = var1.getY();
               if (var4 >= (float)(this.getContainerViewWidth() - AndroidUtilities.dp(100.0F)) / 2.0F && var4 <= (float)(this.getContainerViewWidth() + AndroidUtilities.dp(100.0F)) / 2.0F && var5 >= (float)(this.getContainerViewHeight() - AndroidUtilities.dp(100.0F)) / 2.0F && var5 <= (float)(this.getContainerViewHeight() + AndroidUtilities.dp(100.0F)) / 2.0F) {
                  this.onActionClick(true);
                  this.checkProgress(0, true);
                  return true;
               }
            }
         }

         this.toggleActionBar(this.isActionBarVisible ^ true, true);
         return true;
      }
   }

   public boolean onSingleTapUp(MotionEvent var1) {
      return false;
   }

   public boolean open(MessageObject var1) {
      return this.open(var1, (TLRPC.WebPage)null, (String)null, true);
   }

   public boolean open(TLRPC.TL_webPage var1, String var2) {
      return this.open((MessageObject)null, var1, var2, true);
   }

   public boolean openPhoto(TLRPC.PageBlock var1) {
      if (this.pageSwitchAnimation == null && this.parentActivity != null && !this.isPhotoVisible && !this.checkPhotoAnimation() && var1 != null) {
         ArticleViewer.PlaceProviderObject var2 = this.getPlaceForPhoto(var1);
         if (var2 == null) {
            return false;
         } else {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidFailedLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileLoadProgressChanged);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
            if (this.velocityTracker == null) {
               this.velocityTracker = VelocityTracker.obtain();
            }

            this.isPhotoVisible = true;
            this.toggleActionBar(true, false);
            this.actionBar.setAlpha(0.0F);
            this.bottomLayout.setAlpha(0.0F);
            this.captionTextView.setAlpha(0.0F);
            this.photoBackgroundDrawable.setAlpha(0);
            this.groupedPhotosListView.setAlpha(0.0F);
            this.photoContainerView.setAlpha(1.0F);
            this.disableShowCheck = true;
            this.photoAnimationInProgress = 1;
            if (var1 != null) {
               this.currentAnimation = var2.imageReceiver.getAnimation();
            }

            int var3 = this.adapter[0].photoBlocks.indexOf(var1);
            this.imagesArr.clear();
            if (var1 instanceof TLRPC.TL_pageBlockVideo && !this.isVideoBlock(var1)) {
               this.imagesArr.add(var1);
               var3 = 0;
            } else {
               this.imagesArr.addAll(this.adapter[0].photoBlocks);
            }

            this.onPhotoShow(var3, var2);
            RectF var17 = var2.imageReceiver.getDrawRegion();
            var3 = var2.imageReceiver.getOrientation();
            int var4 = var2.imageReceiver.getAnimatedOrientation();
            if (var4 != 0) {
               var3 = var4;
            }

            this.animatingImageView.setVisibility(0);
            this.animatingImageView.setRadius(var2.radius);
            this.animatingImageView.setOrientation(var3);
            ClippingImageView var5 = this.animatingImageView;
            boolean var6;
            if (var2.radius != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            var5.setNeedRadius(var6);
            this.animatingImageView.setImageBitmap(var2.thumb);
            this.animatingImageView.setAlpha(1.0F);
            this.animatingImageView.setPivotX(0.0F);
            this.animatingImageView.setPivotY(0.0F);
            this.animatingImageView.setScaleX(var2.scale);
            this.animatingImageView.setScaleY(var2.scale);
            this.animatingImageView.setTranslationX((float)var2.viewX + var17.left * var2.scale);
            this.animatingImageView.setTranslationY((float)var2.viewY + var17.top * var2.scale);
            android.view.ViewGroup.LayoutParams var21 = this.animatingImageView.getLayoutParams();
            var21.width = (int)var17.width();
            var21.height = (int)var17.height();
            this.animatingImageView.setLayoutParams(var21);
            Point var7 = AndroidUtilities.displaySize;
            float var8 = (float)var7.x / (float)var21.width;
            float var9 = (float)(var7.y + AndroidUtilities.statusBarHeight) / (float)var21.height;
            if (var8 <= var9) {
               var9 = var8;
            }

            var8 = (float)var21.width;
            float var10 = (float)var21.height;
            float var11 = ((float)AndroidUtilities.displaySize.x - var8 * var9) / 2.0F;
            var8 = var11;
            if (VERSION.SDK_INT >= 21) {
               Object var24 = this.lastInsets;
               var8 = var11;
               if (var24 != null) {
                  var8 = var11 + (float)((WindowInsets)var24).getSystemWindowInsetLeft();
               }
            }

            var11 = ((float)(AndroidUtilities.displaySize.y + AndroidUtilities.statusBarHeight) - var10 * var9) / 2.0F;
            if (var2.imageReceiver.isAspectFit()) {
               var3 = 0;
            } else {
               var3 = (int)Math.abs(var17.left - (float)var2.imageReceiver.getImageX());
            }

            int var12 = (int)Math.abs(var17.top - (float)var2.imageReceiver.getImageY());
            int[] var25 = new int[2];
            var2.parentView.getLocationInWindow(var25);
            int var13 = (int)((float)var25[1] - ((float)var2.viewY + var17.top) + (float)var2.clipTopAddition);
            var4 = var13;
            if (var13 < 0) {
               var4 = 0;
            }

            int var14 = (int)((float)var2.viewY + var17.top + (float)var21.height - (float)(var25[1] + var2.parentView.getHeight()) + (float)var2.clipBottomAddition);
            var13 = var14;
            if (var14 < 0) {
               var13 = 0;
            }

            var4 = Math.max(var4, var12);
            var13 = Math.max(var13, var12);
            this.animationValues[0][0] = this.animatingImageView.getScaleX();
            this.animationValues[0][1] = this.animatingImageView.getScaleY();
            this.animationValues[0][2] = this.animatingImageView.getTranslationX();
            this.animationValues[0][3] = this.animatingImageView.getTranslationY();
            float[][] var22 = this.animationValues;
            float[] var18 = var22[0];
            var10 = (float)var3;
            float var15 = var2.scale;
            var18[4] = var10 * var15;
            var22[0][5] = (float)var4 * var15;
            var22[0][6] = (float)var13 * var15;
            var22[0][7] = (float)this.animatingImageView.getRadius();
            float[][] var19 = this.animationValues;
            float[] var23 = var19[0];
            float var16 = (float)var12;
            var15 = var2.scale;
            var23[8] = var16 * var15;
            var19[0][9] = var10 * var15;
            var19[1][0] = var9;
            var19[1][1] = var9;
            var19[1][2] = var8;
            var19[1][3] = var11;
            var19[1][4] = 0.0F;
            var19[1][5] = 0.0F;
            var19[1][6] = 0.0F;
            var19[1][7] = 0.0F;
            var19[1][8] = 0.0F;
            var19[1][9] = 0.0F;
            this.photoContainerView.setVisibility(0);
            this.photoContainerBackground.setVisibility(0);
            this.animatingImageView.setAnimationProgress(0.0F);
            AnimatorSet var20 = new AnimatorSet();
            var20.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.animatingImageView, "animationProgress", new float[]{0.0F, 1.0F}), ObjectAnimator.ofInt(this.photoBackgroundDrawable, AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[]{0, 255}), ObjectAnimator.ofFloat(this.actionBar, View.ALPHA, new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.bottomLayout, View.ALPHA, new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.captionTextView, View.ALPHA, new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.groupedPhotosListView, View.ALPHA, new float[]{0.0F, 1.0F})});
            this.photoAnimationEndRunnable = new _$$Lambda$ArticleViewer$8pPxKBf7qST2OsNyPVRs9Feebz8(this);
            var20.setDuration(200L);
            var20.addListener(new AnimatorListenerAdapter() {
               // $FF: synthetic method
               public void lambda$onAnimationEnd$0$ArticleViewer$23() {
                  NotificationCenter.getInstance(ArticleViewer.this.currentAccount).setAnimationInProgress(false);
                  if (ArticleViewer.this.photoAnimationEndRunnable != null) {
                     ArticleViewer.this.photoAnimationEndRunnable.run();
                     ArticleViewer.this.photoAnimationEndRunnable = null;
                  }

               }

               public void onAnimationEnd(Animator var1) {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$23$VcLMsIelHoGYSebxKyhxv0wAYFw(this));
               }
            });
            this.photoTransitionAnimationStartTime = System.currentTimeMillis();
            AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$mSJxhXEaOnXrvVt6G3mknr82Hqo(this, var20));
            if (VERSION.SDK_INT >= 18) {
               this.photoContainerView.setLayerType(2, (Paint)null);
            }

            this.photoBackgroundDrawable.drawRunnable = new _$$Lambda$ArticleViewer$8Z92wELF_pGETcAKmhLAU_sdkG8(this, var2);
            return true;
         }
      } else {
         return false;
      }
   }

   @Keep
   public void setAnimationValue(float var1) {
      this.animationValue = var1;
      this.photoContainerView.invalidate();
   }

   public void setParentActivity(Activity var1, BaseFragment var2) {
      this.parentFragment = var2;
      this.currentAccount = UserConfig.selectedAccount;
      this.leftImage.setCurrentAccount(this.currentAccount);
      this.rightImage.setCurrentAccount(this.currentAccount);
      this.centerImage.setCurrentAccount(this.currentAccount);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needSetDayNightTheme);
      if (this.parentActivity == var1) {
         this.updatePaintColors();
      } else {
         this.parentActivity = var1;
         SharedPreferences var8 = ApplicationLoader.applicationContext.getSharedPreferences("articles", 0);
         this.selectedFontSize = var8.getInt("font_size", 2);
         this.selectedFont = var8.getInt("font_type", 0);
         this.selectedColor = var8.getInt("font_color", 0);
         this.nightModeEnabled = var8.getBoolean("nightModeEnabled", false);
         this.createPaint(false);
         this.backgroundPaint = new Paint();
         this.layerShadowDrawable = var1.getResources().getDrawable(2131165521);
         this.slideDotDrawable = var1.getResources().getDrawable(2131165828);
         this.slideDotBigDrawable = var1.getResources().getDrawable(2131165827);
         this.scrimPaint = new Paint();
         this.windowView = new ArticleViewer.WindowView(var1);
         this.windowView.setWillNotDraw(false);
         this.windowView.setClipChildren(true);
         this.windowView.setFocusable(false);
         this.containerView = new FrameLayout(var1) {
            protected boolean drawChild(Canvas var1, View var2, long var3) {
               if (!ArticleViewer.this.windowView.movingPage) {
                  return super.drawChild(var1, var2, var3);
               } else {
                  int var5;
                  int var6;
                  int var7;
                  int var8;
                  label31: {
                     var5 = this.getMeasuredWidth();
                     var6 = (int)ArticleViewer.this.listView[0].getTranslationX();
                     if (var2 == ArticleViewer.this.listView[1]) {
                        var7 = var6;
                     } else {
                        if (var2 == ArticleViewer.this.listView[0]) {
                           var8 = var5;
                           var7 = var6;
                           break label31;
                        }

                        var7 = var5;
                     }

                     byte var9 = 0;
                     var8 = var7;
                     var7 = var9;
                  }

                  int var13 = var1.save();
                  var1.clipRect(var7, 0, var8, this.getHeight());
                  boolean var10 = super.drawChild(var1, var2, var3);
                  var1.restoreToCount(var13);
                  if (var6 != 0) {
                     float var11;
                     if (var2 == ArticleViewer.this.listView[0]) {
                        var11 = Math.max(0.0F, Math.min((float)(var5 - var6) / (float)AndroidUtilities.dp(20.0F), 1.0F));
                        ArticleViewer.this.layerShadowDrawable.setBounds(var6 - ArticleViewer.this.layerShadowDrawable.getIntrinsicWidth(), var2.getTop(), var6, var2.getBottom());
                        ArticleViewer.this.layerShadowDrawable.setAlpha((int)(var11 * 255.0F));
                        ArticleViewer.this.layerShadowDrawable.draw(var1);
                     } else if (var2 == ArticleViewer.this.listView[1]) {
                        float var12 = Math.min(0.8F, (float)(var5 - var6) / (float)var5);
                        var11 = var12;
                        if (var12 < 0.0F) {
                           var11 = 0.0F;
                        }

                        ArticleViewer.this.scrimPaint.setColor((int)(var11 * 153.0F) << 24);
                        var1.drawRect((float)var7, 0.0F, (float)var8, (float)this.getHeight(), ArticleViewer.this.scrimPaint);
                     }
                  }

                  return var10;
               }
            }
         };
         this.windowView.addView(this.containerView, LayoutHelper.createFrame(-1, -1, 51));
         this.containerView.setFitsSystemWindows(true);
         if (VERSION.SDK_INT >= 21) {
            this.containerView.setOnApplyWindowInsetsListener(new _$$Lambda$ArticleViewer$vp7UXKOYIsKBRVoLXvi04N7sU1U(this));
         }

         this.containerView.setSystemUiVisibility(1028);
         this.photoContainerBackground = new View(var1);
         this.photoContainerBackground.setVisibility(4);
         this.photoContainerBackground.setBackgroundDrawable(this.photoBackgroundDrawable);
         this.windowView.addView(this.photoContainerBackground, LayoutHelper.createFrame(-1, -1, 51));
         this.animatingImageView = new ClippingImageView(var1);
         this.animatingImageView.setAnimationValues(this.animationValues);
         this.animatingImageView.setVisibility(8);
         this.windowView.addView(this.animatingImageView, LayoutHelper.createFrame(40, 40.0F));
         this.photoContainerView = new ArticleViewer.FrameLayoutDrawer(var1) {
            protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
               super.onLayout(var1, var2, var3, var4, var5);
               var2 = var5 - var3;
               var5 = var2 - ArticleViewer.this.captionTextView.getMeasuredHeight();
               var4 = var2 - ArticleViewer.this.groupedPhotosListView.getMeasuredHeight();
               var2 = var5;
               var3 = var4;
               if (ArticleViewer.this.bottomLayout.getVisibility() == 0) {
                  var2 = var5 - ArticleViewer.this.bottomLayout.getMeasuredHeight();
                  var3 = var4 - ArticleViewer.this.bottomLayout.getMeasuredHeight();
               }

               var4 = var2;
               if (!ArticleViewer.this.groupedPhotosListView.currentPhotos.isEmpty()) {
                  var4 = var2 - ArticleViewer.this.groupedPhotosListView.getMeasuredHeight();
               }

               ArticleViewer.this.captionTextView.layout(0, var4, ArticleViewer.this.captionTextView.getMeasuredWidth(), ArticleViewer.this.captionTextView.getMeasuredHeight() + var4);
               ArticleViewer.this.captionTextViewNext.layout(0, var4, ArticleViewer.this.captionTextViewNext.getMeasuredWidth(), ArticleViewer.this.captionTextViewNext.getMeasuredHeight() + var4);
               ArticleViewer.this.groupedPhotosListView.layout(0, var3, ArticleViewer.this.groupedPhotosListView.getMeasuredWidth(), ArticleViewer.this.groupedPhotosListView.getMeasuredHeight() + var3);
            }
         };
         this.photoContainerView.setVisibility(4);
         this.photoContainerView.setWillNotDraw(false);
         this.windowView.addView(this.photoContainerView, LayoutHelper.createFrame(-1, -1, 51));
         this.fullscreenVideoContainer = new FrameLayout(var1);
         this.fullscreenVideoContainer.setBackgroundColor(-16777216);
         this.fullscreenVideoContainer.setVisibility(4);
         this.windowView.addView(this.fullscreenVideoContainer, LayoutHelper.createFrame(-1, -1.0F));
         this.fullscreenAspectRatioView = new AspectRatioFrameLayout(var1);
         this.fullscreenAspectRatioView.setVisibility(8);
         this.fullscreenVideoContainer.addView(this.fullscreenAspectRatioView, LayoutHelper.createFrame(-1, -1, 17));
         this.fullscreenTextureView = new TextureView(var1);
         this.listView = new RecyclerListView[2];
         this.adapter = new ArticleViewer.WebpageAdapter[2];
         this.layoutManager = new LinearLayoutManager[2];
         int var3 = 0;

         while(true) {
            RecyclerListView[] var9 = this.listView;
            byte var6;
            if (var3 >= var9.length) {
               this.headerPaint.setColor(-16777216);
               this.statusBarPaint.setColor(-16777216);
               this.headerProgressPaint.setColor(-14408666);
               this.headerView = new FrameLayout(var1) {
                  protected void onDraw(Canvas var1) {
                     int var2 = this.getMeasuredWidth();
                     int var3 = this.getMeasuredHeight();
                     float var4 = (float)var2;
                     float var5 = (float)var3;
                     var1.drawRect(0.0F, 0.0F, var4, var5, ArticleViewer.this.headerPaint);
                     if (ArticleViewer.this.layoutManager != null) {
                        var2 = ArticleViewer.this.layoutManager[0].findFirstVisibleItemPosition();
                        int var6 = ArticleViewer.this.layoutManager[0].findLastVisibleItemPosition();
                        int var7 = ArticleViewer.this.layoutManager[0].getItemCount();
                        var3 = var7 - 2;
                        View var8;
                        if (var6 >= var3) {
                           var8 = ArticleViewer.this.layoutManager[0].findViewByPosition(var3);
                        } else {
                           var8 = ArticleViewer.this.layoutManager[0].findViewByPosition(var2);
                        }

                        if (var8 != null) {
                           float var9 = var4 / (float)(var7 - 1);
                           ArticleViewer.this.layoutManager[0].getChildCount();
                           var4 = (float)var8.getMeasuredHeight();
                           if (var6 >= var3) {
                              var4 = (float)(var3 - var2) * var9 * (float)(ArticleViewer.this.listView[0].getMeasuredHeight() - var8.getTop()) / var4;
                           } else {
                              var4 = (1.0F - ((float)Math.min(0, var8.getTop() - ArticleViewer.this.listView[0].getPaddingTop()) + var4) / var4) * var9;
                           }

                           var1.drawRect(0.0F, 0.0F, (float)var2 * var9 + var4, var5, ArticleViewer.this.headerProgressPaint);
                        }
                     }
                  }
               };
               this.headerView.setOnTouchListener(_$$Lambda$ArticleViewer$_HFJyvtR4MvWBLU_ptyI8bKyRKk.INSTANCE);
               this.headerView.setWillNotDraw(false);
               this.containerView.addView(this.headerView, LayoutHelper.createFrame(-1, 56.0F));
               this.backButton = new ImageView(var1);
               this.backButton.setScaleType(ScaleType.CENTER);
               this.backDrawable = new BackDrawable(false);
               this.backDrawable.setAnimationTime(200.0F);
               this.backDrawable.setColor(-5000269);
               this.backDrawable.setRotated(false);
               this.backButton.setImageDrawable(this.backDrawable);
               this.backButton.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
               this.headerView.addView(this.backButton, LayoutHelper.createFrame(54, 56.0F));
               this.backButton.setOnClickListener(new _$$Lambda$ArticleViewer$_BOkadygLKaLCM7xHgqDylRJDCI(this));
               this.backButton.setContentDescription(LocaleController.getString("AccDescrGoBack", 2131558435));
               this.titleTextView = new SimpleTextView(var1);
               this.titleTextView.setGravity(19);
               this.titleTextView.setTextSize(20);
               this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
               this.titleTextView.setTextColor(-5000269);
               this.titleTextView.setPivotX(0.0F);
               this.titleTextView.setPivotY((float)AndroidUtilities.dp(28.0F));
               this.headerView.addView(this.titleTextView, LayoutHelper.createFrame(-1, 56.0F, 51, 72.0F, 0.0F, 96.0F, 0.0F));
               this.lineProgressView = new LineProgressView(var1);
               this.lineProgressView.setProgressColor(-1);
               this.lineProgressView.setPivotX(0.0F);
               this.lineProgressView.setPivotY((float)AndroidUtilities.dp(2.0F));
               this.headerView.addView(this.lineProgressView, LayoutHelper.createFrame(-1, 2.0F, 83, 0.0F, 0.0F, 0.0F, 1.0F));
               this.lineProgressTickRunnable = new _$$Lambda$ArticleViewer$ldm2w9awvjqof32LBYx2CGXsH0c(this);
               LinearLayout var12 = new LinearLayout(this.parentActivity);
               var12.setPadding(0, AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(4.0F));
               var12.setOrientation(1);

               boolean var7;
               for(var3 = 0; var3 < 3; ++var3) {
                  this.colorCells[var3] = new ArticleViewer.ColorCell(this.parentActivity);
                  ArticleViewer.ColorCell var20;
                  if (var3 != 0) {
                     if (var3 != 1) {
                        if (var3 == 2) {
                           this.colorCells[var3].setTextAndColor(LocaleController.getString("ColorDark", 2131559123), -14474461);
                        }
                     } else {
                        this.colorCells[var3].setTextAndColor(LocaleController.getString("ColorSepia", 2131559128), -1382967);
                     }
                  } else {
                     this.nightModeImageView = new ImageView(this.parentActivity);
                     this.nightModeImageView.setScaleType(ScaleType.CENTER);
                     this.nightModeImageView.setImageResource(2131165607);
                     ImageView var19 = this.nightModeImageView;
                     int var22;
                     if (this.nightModeEnabled && this.selectedColor != 2) {
                        var22 = -15428119;
                     } else {
                        var22 = -3355444;
                     }

                     var19.setColorFilter(new PorterDuffColorFilter(var22, Mode.MULTIPLY));
                     this.nightModeImageView.setBackgroundDrawable(Theme.createSelectorDrawable(251658240));
                     var20 = this.colorCells[var3];
                     ImageView var14 = this.nightModeImageView;
                     if (LocaleController.isRTL) {
                        var6 = 3;
                     } else {
                        var6 = 5;
                     }

                     var20.addView(var14, LayoutHelper.createFrame(48, 48, var6 | 48));
                     this.nightModeImageView.setOnClickListener(new _$$Lambda$ArticleViewer$R_6rwlwqps7RWpLQ78R9g2TJAag(this));
                     this.colorCells[var3].setTextAndColor(LocaleController.getString("ColorWhite", 2131559132), -1);
                  }

                  var20 = this.colorCells[var3];
                  if (var3 == this.selectedColor) {
                     var7 = true;
                  } else {
                     var7 = false;
                  }

                  var20.select(var7);
                  this.colorCells[var3].setTag(var3);
                  this.colorCells[var3].setOnClickListener(new _$$Lambda$ArticleViewer$H1Bc9M26tuZkWwbtuUTUyFFEPMA(this));
                  var12.addView(this.colorCells[var3], LayoutHelper.createLinear(-1, 50));
               }

               this.updateNightModeButton();
               View var21 = new View(this.parentActivity);
               var21.setBackgroundColor(-2039584);
               var12.addView(var21, LayoutHelper.createLinear(-1, 1, 15.0F, 4.0F, 15.0F, 4.0F));
               var21.getLayoutParams().height = 1;

               for(var3 = 0; var3 < 2; ++var3) {
                  this.fontCells[var3] = new ArticleViewer.FontCell(this.parentActivity);
                  if (var3 != 0) {
                     if (var3 == 1) {
                        this.fontCells[var3].setTextAndTypeface("Serif", Typeface.SERIF);
                     }
                  } else {
                     this.fontCells[var3].setTextAndTypeface("Roboto", Typeface.DEFAULT);
                  }

                  ArticleViewer.FontCell var23 = this.fontCells[var3];
                  if (var3 == this.selectedFont) {
                     var7 = true;
                  } else {
                     var7 = false;
                  }

                  var23.select(var7);
                  this.fontCells[var3].setTag(var3);
                  this.fontCells[var3].setOnClickListener(new _$$Lambda$ArticleViewer$bbOV83__XjOP0aIG8YMQY9bn2Gc(this));
                  var12.addView(this.fontCells[var3], LayoutHelper.createLinear(-1, 50));
               }

               var21 = new View(this.parentActivity);
               var21.setBackgroundColor(-2039584);
               var12.addView(var21, LayoutHelper.createLinear(-1, 1, 15.0F, 4.0F, 15.0F, 4.0F));
               var21.getLayoutParams().height = 1;
               TextView var24 = new TextView(this.parentActivity);
               var24.setTextColor(-14606047);
               var24.setTextSize(1, 16.0F);
               var24.setLines(1);
               var24.setMaxLines(1);
               var24.setSingleLine(true);
               var24.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
               byte var16;
               if (LocaleController.isRTL) {
                  var16 = 5;
               } else {
                  var16 = 3;
               }

               var24.setGravity(var16 | 48);
               var24.setText(LocaleController.getString("FontSize", 2131559498));
               if (LocaleController.isRTL) {
                  var16 = 5;
               } else {
                  var16 = 3;
               }

               var12.addView(var24, LayoutHelper.createLinear(-2, -2, var16 | 48, 17, 12, 17, 0));
               var12.addView(new ArticleViewer.SizeChooseView(this.parentActivity), LayoutHelper.createLinear(-1, 38, 0.0F, 0.0F, 0.0F, 1.0F));
               this.settingsButton = new ActionBarMenuItem(this.parentActivity, (ActionBarMenu)null, 1090519039, -1);
               this.settingsButton.setPopupAnimationEnabled(false);
               this.settingsButton.setLayoutInScreen(true);
               var24 = new TextView(this.parentActivity);
               var24.setTextSize(1, 18.0F);
               var24.setText("Aa");
               var24.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
               var24.setTextColor(-5000269);
               var24.setGravity(17);
               var24.setImportantForAccessibility(2);
               this.settingsButton.addView(var24, LayoutHelper.createFrame(-1, -1.0F));
               this.settingsButton.addSubItem(var12, AndroidUtilities.dp(220.0F), -2);
               this.settingsButton.redrawPopup(-1);
               this.settingsButton.setContentDescription(LocaleController.getString("Settings", 2131560738));
               this.headerView.addView(this.settingsButton, LayoutHelper.createFrame(48, 56.0F, 53, 0.0F, 0.0F, 56.0F, 0.0F));
               this.shareContainer = new FrameLayout(var1);
               this.headerView.addView(this.shareContainer, LayoutHelper.createFrame(48, 56, 53));
               this.shareContainer.setOnClickListener(new _$$Lambda$ArticleViewer$T6B8FEUYsYpA0hIBLlSeg6NN1X0(this));
               this.shareButton = new ImageView(var1);
               this.shareButton.setScaleType(ScaleType.CENTER);
               this.shareButton.setImageResource(2131165469);
               this.shareButton.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
               this.shareButton.setContentDescription(LocaleController.getString("ShareFile", 2131560748));
               this.shareContainer.addView(this.shareButton, LayoutHelper.createFrame(48, 56.0F));
               this.progressView = new ContextProgressView(var1, 2);
               this.progressView.setVisibility(8);
               this.shareContainer.addView(this.progressView, LayoutHelper.createFrame(48, 56.0F));
               this.windowLayoutParams = new LayoutParams();
               LayoutParams var13 = this.windowLayoutParams;
               var13.height = -1;
               var13.format = -3;
               var13.width = -1;
               var13.gravity = 51;
               var13.type = 99;
               var3 = VERSION.SDK_INT;
               if (var3 >= 21) {
                  var13.flags = -2147417848;
                  if (var3 >= 28) {
                     var13.layoutInDisplayCutoutMode = 1;
                  }
               } else {
                  var13.flags = 8;
               }

               if (progressDrawables == null) {
                  progressDrawables = new Drawable[4];
                  progressDrawables[0] = this.parentActivity.getResources().getDrawable(2131165357);
                  progressDrawables[1] = this.parentActivity.getResources().getDrawable(2131165337);
                  progressDrawables[2] = this.parentActivity.getResources().getDrawable(2131165537);
                  progressDrawables[3] = this.parentActivity.getResources().getDrawable(2131165771);
               }

               this.scroller = new Scroller(var1);
               this.blackPaint.setColor(-16777216);
               this.actionBar = new ActionBar(var1);
               this.actionBar.setBackgroundColor(2130706432);
               this.actionBar.setOccupyStatusBar(false);
               this.actionBar.setTitleColor(-1);
               this.actionBar.setItemsBackgroundColor(1090519039, false);
               this.actionBar.setBackButtonImage(2131165409);
               this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, 1, 1));
               this.photoContainerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0F));
               this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
                  public boolean canOpenMenu() {
                     ArticleViewer var1 = ArticleViewer.this;
                     File var3 = var1.getMediaFile(var1.currentIndex);
                     boolean var2;
                     if (var3 != null && var3.exists()) {
                        var2 = true;
                     } else {
                        var2 = false;
                     }

                     return var2;
                  }

                  public void onItemClick(int var1) {
                     if (var1 == -1) {
                        ArticleViewer.this.closePhoto(true);
                     } else if (var1 == 1) {
                        if (VERSION.SDK_INT >= 23 && ArticleViewer.this.parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                           ArticleViewer.this.parentActivity.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
                           return;
                        }

                        ArticleViewer var2 = ArticleViewer.this;
                        File var6 = var2.getMediaFile(var2.currentIndex);
                        if (var6 != null && var6.exists()) {
                           String var3 = var6.toString();
                           Activity var4 = ArticleViewer.this.parentActivity;
                           var2 = ArticleViewer.this;
                           MediaController.saveFile(var3, var4, var2.isMediaVideo(var2.currentIndex), (String)null, (String)null);
                        } else {
                           AlertDialog.Builder var7 = new AlertDialog.Builder(ArticleViewer.this.parentActivity);
                           var7.setTitle(LocaleController.getString("AppName", 2131558635));
                           var7.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                           var7.setMessage(LocaleController.getString("PleaseDownload", 2131560454));
                           ArticleViewer.this.showDialog(var7.create());
                        }
                     } else if (var1 == 2) {
                        ArticleViewer.this.onSharePressed();
                     } else if (var1 == 3) {
                        try {
                           AndroidUtilities.openForView(ArticleViewer.this.getMedia(ArticleViewer.this.currentIndex), ArticleViewer.this.parentActivity);
                           ArticleViewer.this.closePhoto(false);
                        } catch (Exception var5) {
                           FileLog.e((Throwable)var5);
                        }
                     }

                  }
               });
               ActionBarMenu var15 = this.actionBar.createMenu();
               var15.addItem(2, 2131165818);
               this.menuItem = var15.addItem(0, 2131165416);
               this.menuItem.setLayoutInScreen(true);
               this.menuItem.addSubItem(3, 2131165649, LocaleController.getString("OpenInExternalApp", 2131560116)).setColors(-328966, -328966);
               this.menuItem.addSubItem(1, 2131165628, LocaleController.getString("SaveToGallery", 2131560630)).setColors(-328966, -328966);
               this.menuItem.redrawPopup(-115203550);
               this.bottomLayout = new FrameLayout(this.parentActivity);
               this.bottomLayout.setBackgroundColor(2130706432);
               this.photoContainerView.addView(this.bottomLayout, LayoutHelper.createFrame(-1, 48, 83));
               this.groupedPhotosListView = new GroupedPhotosListView(this.parentActivity);
               this.photoContainerView.addView(this.groupedPhotosListView, LayoutHelper.createFrame(-1, 62.0F, 83, 0.0F, 0.0F, 0.0F, 0.0F));
               this.groupedPhotosListView.setDelegate(new GroupedPhotosListView.GroupedPhotosListViewDelegate() {
                  public int getAvatarsDialogId() {
                     return 0;
                  }

                  public int getCurrentAccount() {
                     return ArticleViewer.this.currentAccount;
                  }

                  public int getCurrentIndex() {
                     return ArticleViewer.this.currentIndex;
                  }

                  public ArrayList getImagesArr() {
                     return null;
                  }

                  public ArrayList getImagesArrLocations() {
                     return null;
                  }

                  public ArrayList getPageBlockArr() {
                     return ArticleViewer.this.imagesArr;
                  }

                  public Object getParentObject() {
                     return ArticleViewer.this.currentPage;
                  }

                  public int getSlideshowMessageId() {
                     return 0;
                  }

                  public void setCurrentIndex(int var1) {
                     ArticleViewer.this.currentIndex = -1;
                     if (ArticleViewer.this.currentThumb != null) {
                        ArticleViewer.this.currentThumb.release();
                        ArticleViewer.this.currentThumb = null;
                     }

                     ArticleViewer.this.setImageIndex(var1, true);
                  }
               });
               this.captionTextViewNext = new TextView(var1);
               this.captionTextViewNext.setMaxLines(10);
               this.captionTextViewNext.setBackgroundColor(2130706432);
               this.captionTextViewNext.setMovementMethod(new PhotoViewer.LinkMovementMethodMy());
               this.captionTextViewNext.setPadding(AndroidUtilities.dp(20.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(20.0F), AndroidUtilities.dp(8.0F));
               this.captionTextViewNext.setLinkTextColor(-1);
               this.captionTextViewNext.setTextColor(-1);
               this.captionTextViewNext.setHighlightColor(872415231);
               this.captionTextViewNext.setGravity(19);
               this.captionTextViewNext.setTextSize(1, 16.0F);
               this.captionTextViewNext.setVisibility(8);
               this.photoContainerView.addView(this.captionTextViewNext, LayoutHelper.createFrame(-1, -2, 83));
               this.captionTextView = new TextView(var1);
               this.captionTextView.setMaxLines(10);
               this.captionTextView.setBackgroundColor(2130706432);
               this.captionTextView.setMovementMethod(new PhotoViewer.LinkMovementMethodMy());
               this.captionTextView.setPadding(AndroidUtilities.dp(20.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(20.0F), AndroidUtilities.dp(8.0F));
               this.captionTextView.setLinkTextColor(-1);
               this.captionTextView.setTextColor(-1);
               this.captionTextView.setHighlightColor(872415231);
               this.captionTextView.setGravity(19);
               this.captionTextView.setTextSize(1, 16.0F);
               this.captionTextView.setVisibility(8);
               this.photoContainerView.addView(this.captionTextView, LayoutHelper.createFrame(-1, -2, 83));
               this.radialProgressViews[0] = new ArticleViewer.RadialProgressView(var1, this.photoContainerView);
               this.radialProgressViews[0].setBackgroundState(0, false);
               this.radialProgressViews[1] = new ArticleViewer.RadialProgressView(var1, this.photoContainerView);
               this.radialProgressViews[1].setBackgroundState(0, false);
               this.radialProgressViews[2] = new ArticleViewer.RadialProgressView(var1, this.photoContainerView);
               this.radialProgressViews[2].setBackgroundState(0, false);
               this.videoPlayerSeekbar = new SeekBar(var1);
               this.videoPlayerSeekbar.setColors(1728053247, 1728053247, -2764585, -1, -1);
               this.videoPlayerSeekbar.setDelegate(new _$$Lambda$ArticleViewer$_GVmz26hlBkXHajDH6vxhTab9Js(this));
               this.videoPlayerControlFrameLayout = new FrameLayout(var1) {
                  protected void onDraw(Canvas var1) {
                     var1.save();
                     var1.translate((float)AndroidUtilities.dp(48.0F), 0.0F);
                     ArticleViewer.this.videoPlayerSeekbar.draw(var1);
                     var1.restore();
                  }

                  protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
                     super.onLayout(var1, var2, var3, var4, var5);
                     float var6;
                     if (ArticleViewer.this.videoPlayer != null) {
                        var6 = (float)ArticleViewer.this.videoPlayer.getCurrentPosition() / (float)ArticleViewer.this.videoPlayer.getDuration();
                     } else {
                        var6 = 0.0F;
                     }

                     ArticleViewer.this.videoPlayerSeekbar.setProgress(var6);
                  }

                  protected void onMeasure(int var1, int var2) {
                     super.onMeasure(var1, var2);
                     VideoPlayer var3 = ArticleViewer.this.videoPlayer;
                     long var4 = 0L;
                     long var6 = var4;
                     if (var3 != null) {
                        var6 = ArticleViewer.this.videoPlayer.getDuration();
                        if (var6 == -9223372036854775807L) {
                           var6 = var4;
                        }
                     }

                     var4 = var6 / 1000L;
                     TextPaint var8 = ArticleViewer.this.videoPlayerTime.getPaint();
                     var6 = var4 / 60L;
                     var4 %= 60L;
                     var1 = (int)Math.ceil((double)var8.measureText(String.format("%02d:%02d / %02d:%02d", var6, var4, var6, var4)));
                     ArticleViewer.this.videoPlayerSeekbar.setSize(this.getMeasuredWidth() - AndroidUtilities.dp(64.0F) - var1, this.getMeasuredHeight());
                  }

                  public boolean onTouchEvent(MotionEvent var1) {
                     var1.getX();
                     var1.getY();
                     if (ArticleViewer.this.videoPlayerSeekbar.onTouch(var1.getAction(), var1.getX() - (float)AndroidUtilities.dp(48.0F), var1.getY())) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                        this.invalidate();
                        return true;
                     } else {
                        return super.onTouchEvent(var1);
                     }
                  }
               };
               this.videoPlayerControlFrameLayout.setWillNotDraw(false);
               this.bottomLayout.addView(this.videoPlayerControlFrameLayout, LayoutHelper.createFrame(-1, -1, 51));
               this.videoPlayButton = new ImageView(var1);
               this.videoPlayButton.setScaleType(ScaleType.CENTER);
               this.videoPlayerControlFrameLayout.addView(this.videoPlayButton, LayoutHelper.createFrame(48, 48, 51));
               this.videoPlayButton.setOnClickListener(new _$$Lambda$ArticleViewer$lPU_BVNRUajP3efspuXk_ru9iJ0(this));
               this.videoPlayerTime = new TextView(var1);
               this.videoPlayerTime.setTextColor(-1);
               this.videoPlayerTime.setGravity(16);
               this.videoPlayerTime.setTextSize(1, 13.0F);
               this.videoPlayerControlFrameLayout.addView(this.videoPlayerTime, LayoutHelper.createFrame(-2, -1.0F, 53, 0.0F, 0.0F, 8.0F, 0.0F));
               this.gestureDetector = new GestureDetector(var1, this);
               this.gestureDetector.setOnDoubleTapListener(this);
               this.centerImage.setParentView(this.photoContainerView);
               this.centerImage.setCrossfadeAlpha((byte)2);
               this.centerImage.setInvalidateAll(true);
               this.leftImage.setParentView(this.photoContainerView);
               this.leftImage.setCrossfadeAlpha((byte)2);
               this.leftImage.setInvalidateAll(true);
               this.rightImage.setParentView(this.photoContainerView);
               this.rightImage.setCrossfadeAlpha((byte)2);
               this.rightImage.setInvalidateAll(true);
               this.updatePaintColors();
               return;
            }

            var9[var3] = new RecyclerListView(var1) {
               public boolean onInterceptTouchEvent(MotionEvent var1) {
                  if (ArticleViewer.this.pressedLinkOwnerLayout != null && ArticleViewer.this.pressedLink == null && (ArticleViewer.this.popupWindow == null || !ArticleViewer.this.popupWindow.isShowing()) && (var1.getAction() == 1 || var1.getAction() == 3)) {
                     ArticleViewer.this.pressedLink = null;
                     ArticleViewer.this.pressedLinkOwnerLayout = null;
                     ArticleViewer.this.pressedLinkOwnerView = null;
                  } else if (ArticleViewer.this.pressedLinkOwnerLayout != null && ArticleViewer.this.pressedLink != null && var1.getAction() == 1) {
                     ArticleViewer var2 = ArticleViewer.this;
                     var2.checkLayoutForLinks(var1, var2.pressedLinkOwnerView, ArticleViewer.this.pressedLinkOwnerLayout, 0, 0);
                  }

                  return super.onInterceptTouchEvent(var1);
               }

               protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
                  super.onLayout(var1, var2, var3, var4, var5);
                  var3 = this.getChildCount();

                  for(var2 = 0; var2 < var3; ++var2) {
                     View var6 = this.getChildAt(var2);
                     if (var6.getTag() instanceof Integer && (Integer)var6.getTag() == 90 && var6.getBottom() < this.getMeasuredHeight()) {
                        var2 = this.getMeasuredHeight();
                        var6.layout(0, var2 - var6.getMeasuredHeight(), var6.getMeasuredWidth(), var2);
                        break;
                     }
                  }

               }

               public boolean onTouchEvent(MotionEvent var1) {
                  if (ArticleViewer.this.pressedLinkOwnerLayout != null && ArticleViewer.this.pressedLink == null && (ArticleViewer.this.popupWindow == null || !ArticleViewer.this.popupWindow.isShowing()) && (var1.getAction() == 1 || var1.getAction() == 3)) {
                     ArticleViewer.this.pressedLink = null;
                     ArticleViewer.this.pressedLinkOwnerLayout = null;
                     ArticleViewer.this.pressedLinkOwnerView = null;
                  }

                  return super.onTouchEvent(var1);
               }

               public void setTranslationX(float var1) {
                  super.setTranslationX(var1);
                  if (ArticleViewer.this.windowView.movingPage) {
                     ArticleViewer.this.containerView.invalidate();
                     var1 /= (float)this.getMeasuredWidth();
                     ArticleViewer var2 = ArticleViewer.this;
                     var2.setCurrentHeaderHeight((int)((float)var2.windowView.startMovingHeaderHeight + (float)(AndroidUtilities.dp(56.0F) - ArticleViewer.this.windowView.startMovingHeaderHeight) * var1));
                  }

               }
            };
            ((DefaultItemAnimator)this.listView[var3].getItemAnimator()).setDelayAnimations(false);
            RecyclerListView var4 = this.listView[var3];
            LinearLayoutManager[] var5 = this.layoutManager;
            LinearLayoutManager var10 = new LinearLayoutManager(this.parentActivity, 1, false);
            var5[var3] = var10;
            var4.setLayoutManager(var10);
            ArticleViewer.WebpageAdapter[] var17 = this.adapter;
            ArticleViewer.WebpageAdapter var11 = new ArticleViewer.WebpageAdapter(this.parentActivity);
            var17[var3] = var11;
            this.listView[var3].setAdapter(var11);
            this.listView[var3].setClipToPadding(false);
            RecyclerListView var18 = this.listView[var3];
            if (var3 == 0) {
               var6 = 0;
            } else {
               var6 = 8;
            }

            var18.setVisibility(var6);
            this.listView[var3].setPadding(0, AndroidUtilities.dp(56.0F), 0, 0);
            this.listView[var3].setTopGlowOffset(AndroidUtilities.dp(56.0F));
            this.containerView.addView(this.listView[var3], LayoutHelper.createFrame(-1, -1.0F));
            this.listView[var3].setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$ArticleViewer$gN3ysqedASIoOc6B_30_HuSHg6g(this)));
            this.listView[var3].setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$ArticleViewer$D7cgjgLrXEd0Ps_6XAby7DZGrbY(this, var11)));
            this.listView[var3].setOnScrollListener(new RecyclerView.OnScrollListener() {
               public void onScrolled(RecyclerView var1, int var2, int var3) {
                  if (var1.getChildCount() != 0) {
                     ArticleViewer.this.headerView.invalidate();
                     ArticleViewer.this.checkScroll(var3);
                  }
               }
            });
            ++var3;
         }
      }
   }

   public void showDialog(Dialog var1) {
      if (this.parentActivity != null) {
         try {
            if (this.visibleDialog != null) {
               this.visibleDialog.dismiss();
               this.visibleDialog = null;
            }
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
         }

         try {
            this.visibleDialog = var1;
            this.visibleDialog.setCanceledOnTouchOutside(true);
            Dialog var3 = this.visibleDialog;
            _$$Lambda$ArticleViewer$akGDRTg_CpAhs2_Lmpycw0irNsA var2 = new _$$Lambda$ArticleViewer$akGDRTg_CpAhs2_Lmpycw0irNsA(this);
            var3.setOnDismissListener(var2);
            var1.show();
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

      }
   }

   protected void startCheckLongPress() {
      if (!this.checkingForLongPress) {
         this.checkingForLongPress = true;
         if (this.pendingCheckForTap == null) {
            this.pendingCheckForTap = new ArticleViewer.CheckForTap();
         }

         this.windowView.postDelayed(this.pendingCheckForTap, (long)ViewConfiguration.getTapTimeout());
      }
   }

   public void uncollapse() {
      if (this.parentActivity != null && this.isVisible && !this.checkAnimation()) {
         AnimatorSet var1 = new AnimatorSet();
         var1.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.containerView, View.TRANSLATION_X, new float[]{0.0F}), ObjectAnimator.ofFloat(this.containerView, View.TRANSLATION_Y, new float[]{0.0F}), ObjectAnimator.ofFloat(this.windowView, View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofFloat(this.listView[0], View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofFloat(this.listView[0], View.TRANSLATION_Y, new float[]{0.0F}), ObjectAnimator.ofFloat(this.headerView, View.TRANSLATION_Y, new float[]{0.0F}), ObjectAnimator.ofFloat(this.backButton, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.backButton, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.backButton, View.TRANSLATION_Y, new float[]{0.0F}), ObjectAnimator.ofFloat(this.shareContainer, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.shareContainer, View.TRANSLATION_Y, new float[]{0.0F}), ObjectAnimator.ofFloat(this.shareContainer, View.SCALE_Y, new float[]{1.0F})});
         this.collapsed = false;
         this.animationInProgress = 2;
         this.animationEndRunnable = new _$$Lambda$ArticleViewer$C6ZUTa2rGrDYlXYood81SR54cQg(this);
         var1.setDuration(250L);
         var1.setInterpolator(new DecelerateInterpolator());
         var1.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               if (ArticleViewer.this.animationEndRunnable != null) {
                  ArticleViewer.this.animationEndRunnable.run();
                  ArticleViewer.this.animationEndRunnable = null;
               }

            }
         });
         this.transitionAnimationStartTime = System.currentTimeMillis();
         if (VERSION.SDK_INT >= 18) {
            this.containerView.setLayerType(2, (Paint)null);
         }

         this.backDrawable.setRotation(0.0F, true);
         var1.start();
      }

   }

   private class BlockAudioCell extends View implements DownloadController.FileDownloadProgressListener {
      private int TAG;
      private int buttonPressed;
      private int buttonState;
      private int buttonX;
      private int buttonY;
      private ArticleViewer.DrawingText captionLayout;
      private ArticleViewer.DrawingText creditLayout;
      private int creditOffset;
      private TLRPC.TL_pageBlockAudio currentBlock;
      private TLRPC.Document currentDocument;
      private MessageObject currentMessageObject;
      private StaticLayout durationLayout;
      private boolean isFirst;
      private boolean isLast;
      private String lastTimeString;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private RadialProgress2 radialProgress;
      private SeekBar seekBar;
      private int seekBarX;
      private int seekBarY;
      private int textX;
      private int textY = AndroidUtilities.dp(54.0F);
      private StaticLayout titleLayout;

      public BlockAudioCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
         this.radialProgress = new RadialProgress2(this);
         this.radialProgress.setBackgroundStroke(AndroidUtilities.dp(3.0F));
         this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0F));
         this.TAG = DownloadController.getInstance(ArticleViewer.this.currentAccount).generateObserverTag();
         this.seekBar = new SeekBar(var2);
         this.seekBar.setDelegate(new _$$Lambda$ArticleViewer$BlockAudioCell$WgP383_edJ263u4ZKo_pJ9bitM4(this));
      }

      private void didPressedButton(boolean var1) {
         int var2 = this.buttonState;
         if (var2 == 0) {
            if (MediaController.getInstance().setPlaylist(this.parentAdapter.audioMessages, this.currentMessageObject, false)) {
               this.buttonState = 1;
               this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
               this.invalidate();
            }
         } else if (var2 == 1) {
            if (MediaController.getInstance().pauseMessage(this.currentMessageObject)) {
               this.buttonState = 0;
               this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
               this.invalidate();
            }
         } else if (var2 == 2) {
            this.radialProgress.setProgress(0.0F, false);
            FileLoader.getInstance(ArticleViewer.this.currentAccount).loadFile(this.currentDocument, ArticleViewer.this.currentPage, 1, 1);
            this.buttonState = 3;
            this.radialProgress.setIcon(this.getIconForCurrentState(), true, var1);
            this.invalidate();
         } else if (var2 == 3) {
            FileLoader.getInstance(ArticleViewer.this.currentAccount).cancelLoadFile(this.currentDocument);
            this.buttonState = 2;
            this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
            this.invalidate();
         }

      }

      private int getIconForCurrentState() {
         int var1 = this.buttonState;
         if (var1 == 1) {
            return 1;
         } else if (var1 == 2) {
            return 2;
         } else {
            return var1 == 3 ? 3 : 0;
         }
      }

      public MessageObject getMessageObject() {
         return this.currentMessageObject;
      }

      public int getObserverTag() {
         return this.TAG;
      }

      // $FF: synthetic method
      public void lambda$new$0$ArticleViewer$BlockAudioCell(float var1) {
         MessageObject var2 = this.currentMessageObject;
         if (var2 != null) {
            var2.audioProgress = var1;
            MediaController.getInstance().seekToProgress(this.currentMessageObject, var1);
         }
      }

      protected void onAttachedToWindow() {
         super.onAttachedToWindow();
         this.updateButtonState(false);
      }

      protected void onDetachedFromWindow() {
         super.onDetachedFromWindow();
         DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver(this);
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            this.radialProgress.setColors(ArticleViewer.this.getTextColor(), ArticleViewer.this.getTextColor(), ArticleViewer.this.getTextColor(), ArticleViewer.this.getTextColor());
            this.radialProgress.draw(var1);
            var1.save();
            var1.translate((float)this.seekBarX, (float)this.seekBarY);
            this.seekBar.draw(var1);
            var1.restore();
            if (this.durationLayout != null) {
               var1.save();
               var1.translate((float)(this.buttonX + AndroidUtilities.dp(54.0F)), (float)(this.seekBarY + AndroidUtilities.dp(6.0F)));
               this.durationLayout.draw(var1);
               var1.restore();
            }

            if (this.titleLayout != null) {
               var1.save();
               var1.translate((float)(this.buttonX + AndroidUtilities.dp(54.0F)), (float)(this.seekBarY - AndroidUtilities.dp(16.0F)));
               this.titleLayout.draw(var1);
               var1.restore();
            }

            if (this.captionLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.captionLayout.draw(var1);
               var1.restore();
            }

            if (this.creditLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)(this.textY + this.creditOffset));
               this.creditLayout.draw(var1);
               var1.restore();
            }

            if (this.currentBlock.level > 0) {
               float var2 = (float)AndroidUtilities.dp(18.0F);
               float var3 = (float)AndroidUtilities.dp(20.0F);
               int var4 = this.getMeasuredHeight();
               int var5;
               if (this.currentBlock.bottom) {
                  var5 = AndroidUtilities.dp(6.0F);
               } else {
                  var5 = 0;
               }

               var1.drawRect(var2, 0.0F, var3, (float)(var4 - var5), ArticleViewer.quoteLinePaint);
            }

         }
      }

      public void onFailedDownload(String var1, boolean var2) {
         this.updateButtonState(true);
      }

      @SuppressLint({"DrawAllocation", "NewApi"})
      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         var2 = AndroidUtilities.dp(54.0F);
         TLRPC.TL_pageBlockAudio var4 = this.currentBlock;
         if (var4 != null) {
            var1 = var4.level;
            if (var1 > 0) {
               this.textX = AndroidUtilities.dp((float)(var1 * 14)) + AndroidUtilities.dp(18.0F);
            } else {
               this.textX = AndroidUtilities.dp(18.0F);
            }

            int var5 = var3 - this.textX - AndroidUtilities.dp(18.0F);
            int var6 = AndroidUtilities.dp(44.0F);
            this.buttonX = AndroidUtilities.dp(16.0F);
            this.buttonY = AndroidUtilities.dp(5.0F);
            RadialProgress2 var11 = this.radialProgress;
            var1 = this.buttonX;
            int var7 = this.buttonY;
            var11.setProgressRect(var1, var7, var1 + var6, var7 + var6);
            ArticleViewer var12 = ArticleViewer.this;
            TLRPC.TL_pageBlockAudio var8 = this.currentBlock;
            this.captionLayout = var12.createLayoutForText(this, (CharSequence)null, var8.caption.text, var5, var8, this.parentAdapter);
            var1 = var2;
            if (this.captionLayout != null) {
               this.creditOffset = AndroidUtilities.dp(4.0F) + this.captionLayout.getHeight();
               var1 = var2 + this.creditOffset + AndroidUtilities.dp(4.0F);
            }

            var2 = var1;
            ArticleViewer var16 = ArticleViewer.this;
            TLRPC.TL_pageBlockAudio var9 = this.currentBlock;
            TLRPC.RichText var10 = var9.caption.credit;
            Alignment var13;
            if (var16.isRtl) {
               var13 = Alignment.ALIGN_RIGHT;
            } else {
               var13 = Alignment.ALIGN_NORMAL;
            }

            this.creditLayout = var16.createLayoutForText(this, (CharSequence)null, var10, var5, var9, var13, this.parentAdapter);
            var1 = var1;
            if (this.creditLayout != null) {
               var1 = var2 + AndroidUtilities.dp(4.0F) + this.creditLayout.getHeight();
            }

            var2 = var1;
            if (!this.isFirst) {
               var2 = var1;
               if (this.currentBlock.level <= 0) {
                  var2 = var1 + AndroidUtilities.dp(8.0F);
               }
            }

            String var17 = this.currentMessageObject.getMusicAuthor(false);
            String var14 = this.currentMessageObject.getMusicTitle(false);
            this.seekBarX = this.buttonX + AndroidUtilities.dp(50.0F) + var6;
            var1 = var3 - this.seekBarX - AndroidUtilities.dp(18.0F);
            if (TextUtils.isEmpty(var14) && TextUtils.isEmpty(var17)) {
               this.titleLayout = null;
               this.seekBarY = this.buttonY + (var6 - AndroidUtilities.dp(30.0F)) / 2;
            } else {
               SpannableStringBuilder var15;
               if (!TextUtils.isEmpty(var14) && !TextUtils.isEmpty(var17)) {
                  var15 = new SpannableStringBuilder(String.format("%s - %s", var17, var14));
               } else if (!TextUtils.isEmpty(var14)) {
                  var15 = new SpannableStringBuilder(var14);
               } else {
                  var15 = new SpannableStringBuilder(var17);
               }

               if (!TextUtils.isEmpty(var17)) {
                  var15.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), 0, var17.length(), 18);
               }

               this.titleLayout = new StaticLayout(TextUtils.ellipsize(var15, Theme.chat_audioTitlePaint, (float)var1, TruncateAt.END), ArticleViewer.audioTimePaint, var1, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
               this.seekBarY = this.buttonY + (var6 - AndroidUtilities.dp(30.0F)) / 2 + AndroidUtilities.dp(11.0F);
            }

            this.seekBar.setSize(var1, AndroidUtilities.dp(30.0F));
         } else {
            var2 = 1;
         }

         this.setMeasuredDimension(var3, var2);
         this.updatePlayingMessageProgress();
      }

      public void onProgressDownload(String var1, float var2) {
         this.radialProgress.setProgress(var2, true);
         if (this.buttonState != 3) {
            this.updateButtonState(true);
         }

      }

      public void onProgressUpload(String var1, float var2, boolean var3) {
      }

      public void onSuccessDownload(String var1) {
         this.radialProgress.setProgress(1.0F, true);
         this.updateButtonState(true);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         float var2 = var1.getX();
         float var3 = var1.getY();
         boolean var4 = this.seekBar.onTouch(var1.getAction(), var1.getX() - (float)this.seekBarX, var1.getY() - (float)this.seekBarY);
         boolean var5 = true;
         if (var4) {
            if (var1.getAction() == 0) {
               this.getParent().requestDisallowInterceptTouchEvent(true);
            }

            this.invalidate();
            return true;
         } else {
            if (var1.getAction() == 0) {
               label48: {
                  label47: {
                     if (this.buttonState != -1) {
                        int var6 = this.buttonX;
                        if (var2 >= (float)var6 && var2 <= (float)(var6 + AndroidUtilities.dp(48.0F))) {
                           var6 = this.buttonY;
                           if (var3 >= (float)var6 && var3 <= (float)(var6 + AndroidUtilities.dp(48.0F))) {
                              break label47;
                           }
                        }
                     }

                     if (this.buttonState != 0) {
                        break label48;
                     }
                  }

                  this.buttonPressed = 1;
                  this.invalidate();
               }
            } else if (var1.getAction() == 1) {
               if (this.buttonPressed == 1) {
                  this.buttonPressed = 0;
                  this.playSoundEffect(0);
                  this.didPressedButton(true);
                  this.invalidate();
               }
            } else if (var1.getAction() == 3) {
               this.buttonPressed = 0;
            }

            var4 = var5;
            if (this.buttonPressed == 0) {
               var4 = var5;
               if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.captionLayout, this.textX, this.textY)) {
                  var4 = var5;
                  if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.creditLayout, this.textX, this.textY + this.creditOffset)) {
                     if (super.onTouchEvent(var1)) {
                        var4 = var5;
                     } else {
                        var4 = false;
                     }
                  }
               }
            }

            return var4;
         }
      }

      public void setBlock(TLRPC.TL_pageBlockAudio var1, boolean var2, boolean var3) {
         this.currentBlock = var1;
         this.currentMessageObject = (MessageObject)this.parentAdapter.audioBlocks.get(this.currentBlock);
         this.currentDocument = this.currentMessageObject.getDocument();
         this.isFirst = var2;
         this.isLast = var3;
         this.radialProgress.setProgressColor(ArticleViewer.this.getTextColor());
         this.seekBar.setColors(ArticleViewer.this.getTextColor() & 1073741823, ArticleViewer.this.getTextColor() & 1073741823, ArticleViewer.this.getTextColor(), ArticleViewer.this.getTextColor(), ArticleViewer.this.getTextColor());
         this.updateButtonState(false);
         this.requestLayout();
      }

      public void updateButtonState(boolean var1) {
         String var2 = FileLoader.getAttachFileName(this.currentDocument);
         boolean var3 = FileLoader.getPathToAttach(this.currentDocument, true).exists();
         if (TextUtils.isEmpty(var2)) {
            this.radialProgress.setIcon(4, false, false);
         } else {
            if (var3) {
               DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver(this);
               var3 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
               if (!var3 || var3 && MediaController.getInstance().isMessagePaused()) {
                  this.buttonState = 0;
               } else {
                  this.buttonState = 1;
               }

               this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
            } else {
               DownloadController.getInstance(ArticleViewer.this.currentAccount).addLoadingFileObserver(var2, (MessageObject)null, this);
               if (!FileLoader.getInstance(ArticleViewer.this.currentAccount).isLoadingFile(var2)) {
                  this.buttonState = 2;
                  this.radialProgress.setProgress(0.0F, var1);
                  this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
               } else {
                  this.buttonState = 3;
                  Float var4 = ImageLoader.getInstance().getFileProgress(var2);
                  if (var4 != null) {
                     this.radialProgress.setProgress(var4, var1);
                  } else {
                     this.radialProgress.setProgress(0.0F, var1);
                  }

                  this.radialProgress.setIcon(this.getIconForCurrentState(), true, var1);
               }
            }

            this.updatePlayingMessageProgress();
         }
      }

      public void updatePlayingMessageProgress() {
         if (this.currentDocument != null && this.currentMessageObject != null) {
            if (!this.seekBar.isDragging()) {
               this.seekBar.setProgress(this.currentMessageObject.audioProgress);
            }

            int var1;
            if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
               var1 = this.currentMessageObject.audioProgressSec;
            } else {
               var1 = 0;

               while(true) {
                  if (var1 >= this.currentDocument.attributes.size()) {
                     var1 = 0;
                     break;
                  }

                  TLRPC.DocumentAttribute var2 = (TLRPC.DocumentAttribute)this.currentDocument.attributes.get(var1);
                  if (var2 instanceof TLRPC.TL_documentAttributeAudio) {
                     var1 = var2.duration;
                     break;
                  }

                  ++var1;
               }
            }

            String var4 = String.format("%d:%02d", var1 / 60, var1 % 60);
            String var3 = this.lastTimeString;
            if (var3 == null || var3 != null && !var3.equals(var4)) {
               this.lastTimeString = var4;
               ArticleViewer.audioTimePaint.setTextSize((float)AndroidUtilities.dp(16.0F));
               var1 = (int)Math.ceil((double)ArticleViewer.audioTimePaint.measureText(var4));
               this.durationLayout = new StaticLayout(var4, ArticleViewer.audioTimePaint, var1, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            }

            ArticleViewer.audioTimePaint.setColor(ArticleViewer.this.getTextColor());
            this.invalidate();
         }

      }
   }

   private class BlockAuthorDateCell extends View {
      private TLRPC.TL_pageBlockAuthorDate currentBlock;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ArticleViewer.DrawingText textLayout;
      private int textX;
      private int textY = AndroidUtilities.dp(8.0F);

      public BlockAuthorDateCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

         }
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setEnabled(true);
         ArticleViewer.DrawingText var2 = this.textLayout;
         if (var2 != null) {
            var1.setText(var2.getText());
         }
      }

      protected void onMeasure(int var1, int var2) {
         var2 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockAuthorDate var3 = this.currentBlock;
         var1 = 1;
         if (var3 != null) {
            ArticleViewer var4 = ArticleViewer.this;
            TLRPC.RichText var5 = var3.author;
            CharSequence var6 = var4.getText(this, var5, var5, var3, var2);
            boolean var7 = var6 instanceof Spannable;
            Spannable var8 = null;
            MetricAffectingSpan[] var9;
            if (var7) {
               var8 = (Spannable)var6;
               var9 = (MetricAffectingSpan[])var8.getSpans(0, var6.length(), MetricAffectingSpan.class);
            } else {
               var9 = null;
            }

            String var16;
            if (this.currentBlock.published_date != 0 && !TextUtils.isEmpty(var6)) {
               Object[] var10002 = new Object[2];
               long var10006 = (long)this.currentBlock.published_date;
               var10002[0] = LocaleController.getInstance().chatFullDate.format(var10006 * 1000L);
               var10002[1] = var6;
               var16 = LocaleController.formatString("ArticleDateByAuthor", 2131558705, var10002);
            } else if (!TextUtils.isEmpty(var6)) {
               var16 = LocaleController.formatString("ArticleByAuthor", 2131558704, var6);
            } else {
               long var10001 = (long)this.currentBlock.published_date;
               var16 = LocaleController.getInstance().chatFullDate.format(var10001 * 1000L);
            }

            Object var17 = var16;
            int var10;
            if (var9 != null) {
               label95: {
                  Object var20 = var16;
                  var17 = var16;

                  Exception var10000;
                  label93: {
                     boolean var21;
                     try {
                        if (var9.length <= 0) {
                           break label95;
                        }
                     } catch (Exception var15) {
                        var10000 = var15;
                        var21 = false;
                        break label93;
                     }

                     var20 = var16;

                     try {
                        var10 = TextUtils.indexOf(var16, var6);
                     } catch (Exception var14) {
                        var10000 = var14;
                        var21 = false;
                        break label93;
                     }

                     var17 = var16;
                     if (var10 == -1) {
                        break label95;
                     }

                     var20 = var16;

                     Spannable var18;
                     try {
                        var18 = Factory.getInstance().newSpannable(var16);
                     } catch (Exception var13) {
                        var10000 = var13;
                        var21 = false;
                        break label93;
                     }

                     var1 = 0;

                     while(true) {
                        var20 = var18;
                        var17 = var18;

                        try {
                           if (var1 >= var9.length) {
                              break label95;
                           }
                        } catch (Exception var12) {
                           var10000 = var12;
                           var21 = false;
                           break;
                        }

                        var20 = var18;

                        try {
                           var18.setSpan(var9[var1], var8.getSpanStart(var9[var1]) + var10, var8.getSpanEnd(var9[var1]) + var10, 33);
                        } catch (Exception var11) {
                           var10000 = var11;
                           var21 = false;
                           break;
                        }

                        ++var1;
                     }
                  }

                  Exception var19 = var10000;
                  FileLog.e((Throwable)var19);
                  var17 = var20;
               }
            }

            this.textLayout = ArticleViewer.this.createLayoutForText(this, (CharSequence)var17, (TLRPC.RichText)null, var2 - AndroidUtilities.dp(36.0F), this.currentBlock, this.parentAdapter);
            if (this.textLayout != null) {
               var10 = AndroidUtilities.dp(16.0F);
               var1 = this.textLayout.getHeight();
               if (ArticleViewer.this.isRtl) {
                  this.textX = (int)Math.floor((double)((float)var2 - this.textLayout.getLineWidth(0) - (float)AndroidUtilities.dp(16.0F)));
               } else {
                  this.textX = AndroidUtilities.dp(18.0F);
               }

               var1 = var10 + var1 + 0;
            } else {
               var1 = 0;
            }
         }

         this.setMeasuredDimension(var2, var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockAuthorDate var1) {
         this.currentBlock = var1;
         this.requestLayout();
      }
   }

   private class BlockBlockquoteCell extends View {
      private TLRPC.TL_pageBlockBlockquote currentBlock;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ArticleViewer.DrawingText textLayout;
      private ArticleViewer.DrawingText textLayout2;
      private int textX;
      private int textY = AndroidUtilities.dp(8.0F);
      private int textY2;

      public BlockBlockquoteCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

            if (this.textLayout2 != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY2);
               this.textLayout2.draw(var1);
               var1.restore();
            }

            int var2;
            if (ArticleViewer.this.isRtl) {
               var2 = this.getMeasuredWidth() - AndroidUtilities.dp(20.0F);
               var1.drawRect((float)var2, (float)AndroidUtilities.dp(6.0F), (float)(var2 + AndroidUtilities.dp(2.0F)), (float)(this.getMeasuredHeight() - AndroidUtilities.dp(6.0F)), ArticleViewer.quoteLinePaint);
            } else {
               var1.drawRect((float)AndroidUtilities.dp((float)(this.currentBlock.level * 14 + 18)), (float)AndroidUtilities.dp(6.0F), (float)AndroidUtilities.dp((float)(this.currentBlock.level * 14 + 20)), (float)(this.getMeasuredHeight() - AndroidUtilities.dp(6.0F)), ArticleViewer.quoteLinePaint);
            }

            if (this.currentBlock.level > 0) {
               float var3 = (float)AndroidUtilities.dp(18.0F);
               float var4 = (float)AndroidUtilities.dp(20.0F);
               int var5 = this.getMeasuredHeight();
               if (this.currentBlock.bottom) {
                  var2 = AndroidUtilities.dp(6.0F);
               } else {
                  var2 = 0;
               }

               var1.drawRect(var3, 0.0F, var4, (float)(var5 - var2), ArticleViewer.quoteLinePaint);
            }

         }
      }

      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         if (this.currentBlock != null) {
            var2 = var3 - AndroidUtilities.dp(50.0F);
            int var4 = this.currentBlock.level;
            var1 = var2;
            if (var4 > 0) {
               var1 = var2 - AndroidUtilities.dp((float)(var4 * 14));
            }

            ArticleViewer var5 = ArticleViewer.this;
            TLRPC.TL_pageBlockBlockquote var6 = this.currentBlock;
            this.textLayout = var5.createLayoutForText(this, (CharSequence)null, var6.text, var1, var6, this.parentAdapter);
            if (this.textLayout != null) {
               var2 = 0 + AndroidUtilities.dp(8.0F) + this.textLayout.getHeight();
            } else {
               var2 = 0;
            }

            if (this.currentBlock.level > 0) {
               if (ArticleViewer.this.isRtl) {
                  this.textX = AndroidUtilities.dp((float)(this.currentBlock.level * 14 + 14));
               } else {
                  this.textX = AndroidUtilities.dp((float)(this.currentBlock.level * 14)) + AndroidUtilities.dp(32.0F);
               }
            } else if (ArticleViewer.this.isRtl) {
               this.textX = AndroidUtilities.dp(14.0F);
            } else {
               this.textX = AndroidUtilities.dp(32.0F);
            }

            ArticleViewer var8 = ArticleViewer.this;
            TLRPC.TL_pageBlockBlockquote var7 = this.currentBlock;
            this.textLayout2 = var8.createLayoutForText(this, (CharSequence)null, var7.caption, var1, var7, this.parentAdapter);
            var1 = var2;
            if (this.textLayout2 != null) {
               this.textY2 = AndroidUtilities.dp(8.0F) + var2;
               var1 = var2 + AndroidUtilities.dp(8.0F) + this.textLayout2.getHeight();
            }

            var2 = var1;
            if (var1 != 0) {
               var2 = var1 + AndroidUtilities.dp(8.0F);
            }
         } else {
            var2 = 1;
         }

         this.setMeasuredDimension(var3, var2);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) && !ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout2, this.textX, this.textY2) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockBlockquote var1) {
         this.currentBlock = var1;
         this.requestLayout();
      }
   }

   private class BlockChannelCell extends FrameLayout {
      private Paint backgroundPaint;
      private int buttonWidth;
      private AnimatorSet currentAnimation;
      private TLRPC.TL_pageBlockChannel currentBlock;
      private int currentState;
      private int currentType;
      private ImageView imageView;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ContextProgressView progressView;
      private ArticleViewer.DrawingText textLayout;
      private TextView textView;
      private int textX = AndroidUtilities.dp(18.0F);
      private int textX2;
      private int textY = AndroidUtilities.dp(11.0F);

      public BlockChannelCell(Context var2, ArticleViewer.WebpageAdapter var3, int var4) {
         super(var2);
         this.parentAdapter = var3;
         this.setWillNotDraw(false);
         this.backgroundPaint = new Paint();
         this.currentType = var4;
         this.textView = new TextView(var2);
         this.textView.setTextSize(1, 14.0F);
         this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.textView.setText(LocaleController.getString("ChannelJoin", 2131558954));
         this.textView.setGravity(19);
         this.addView(this.textView, LayoutHelper.createFrame(-2, 39, 53));
         this.textView.setOnClickListener(new _$$Lambda$ArticleViewer$BlockChannelCell$yVpHWSEz8bUl3txryOPbfk9oO0M(this));
         this.imageView = new ImageView(var2);
         this.imageView.setImageResource(2131165524);
         this.imageView.setScaleType(ScaleType.CENTER);
         this.addView(this.imageView, LayoutHelper.createFrame(39, 39, 53));
         this.progressView = new ContextProgressView(var2, 0);
         this.addView(this.progressView, LayoutHelper.createFrame(39, 39, 53));
      }

      // $FF: synthetic method
      public void lambda$new$0$ArticleViewer$BlockChannelCell(View var1) {
         if (this.currentState == 0) {
            this.setState(1, true);
            ArticleViewer var2 = ArticleViewer.this;
            var2.joinChannel(this, var2.loadedChannel);
         }
      }

      protected void onDraw(Canvas var1) {
         int var2 = this.currentType;
         if (this.currentBlock != null) {
            var1.drawRect(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)AndroidUtilities.dp(39.0F), this.backgroundPaint);
            ArticleViewer.DrawingText var3 = this.textLayout;
            if (var3 != null && var3.getLineCount() > 0) {
               var1.save();
               if (ArticleViewer.this.isRtl) {
                  var1.translate((float)this.getMeasuredWidth() - this.textLayout.getLineWidth(0) - (float)this.textX, (float)this.textY);
               } else {
                  var1.translate((float)this.textX, (float)this.textY);
               }

               this.textLayout.draw(var1);
               var1.restore();
            }

         }
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         this.imageView.layout(this.textX2 + this.buttonWidth / 2 - AndroidUtilities.dp(19.0F), 0, this.textX2 + this.buttonWidth / 2 + AndroidUtilities.dp(20.0F), AndroidUtilities.dp(39.0F));
         this.progressView.layout(this.textX2 + this.buttonWidth / 2 - AndroidUtilities.dp(19.0F), 0, this.textX2 + this.buttonWidth / 2 + AndroidUtilities.dp(20.0F), AndroidUtilities.dp(39.0F));
         TextView var6 = this.textView;
         var2 = this.textX2;
         var6.layout(var2, 0, var6.getMeasuredWidth() + var2, this.textView.getMeasuredHeight());
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         var2 = MeasureSpec.getSize(var1);
         this.setMeasuredDimension(var2, AndroidUtilities.dp(48.0F));
         this.textView.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0F), 1073741824));
         this.buttonWidth = this.textView.getMeasuredWidth();
         this.progressView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0F), 1073741824));
         this.imageView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0F), 1073741824));
         TLRPC.TL_pageBlockChannel var3 = this.currentBlock;
         if (var3 != null) {
            this.textLayout = ArticleViewer.this.createLayoutForText(this, var3.channel.title, (TLRPC.RichText)null, var2 - AndroidUtilities.dp(52.0F) - this.buttonWidth, this.currentBlock, Alignment.ALIGN_LEFT, this.parentAdapter);
            if (ArticleViewer.this.isRtl) {
               this.textX2 = this.textX;
            } else {
               this.textX2 = this.getMeasuredWidth() - this.textX - this.buttonWidth;
            }
         }

      }

      public boolean onTouchEvent(MotionEvent var1) {
         if (this.currentType != 0) {
            return super.onTouchEvent(var1);
         } else {
            boolean var2;
            if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) && !super.onTouchEvent(var1)) {
               var2 = false;
            } else {
               var2 = true;
            }

            return var2;
         }
      }

      public void setBlock(TLRPC.TL_pageBlockChannel var1) {
         this.currentBlock = var1;
         int var2 = ArticleViewer.this.getSelectedColor();
         if (this.currentType == 0) {
            this.textView.setTextColor(-14840360);
            if (var2 == 0) {
               this.backgroundPaint.setColor(-526345);
            } else if (var2 == 1) {
               this.backgroundPaint.setColor(-1712440);
            } else if (var2 == 2) {
               this.backgroundPaint.setColor(-15000805);
            }

            this.imageView.setColorFilter(new PorterDuffColorFilter(-6710887, Mode.MULTIPLY));
         } else {
            this.textView.setTextColor(-1);
            this.backgroundPaint.setColor(2130706432);
            this.imageView.setColorFilter(new PorterDuffColorFilter(-1, Mode.MULTIPLY));
         }

         TLRPC.Chat var3 = MessagesController.getInstance(ArticleViewer.this.currentAccount).getChat(var1.channel.id);
         if (var3 != null && !var3.min) {
            ArticleViewer.this.loadedChannel = var3;
            if (var3.left && !var3.kicked) {
               this.setState(0, false);
            } else {
               this.setState(4, false);
            }
         } else {
            ArticleViewer.this.loadChannel(this, this.parentAdapter, var1.channel);
            this.setState(1, false);
         }

         this.requestLayout();
      }

      public void setState(int var1, boolean var2) {
         AnimatorSet var3 = this.currentAnimation;
         if (var3 != null) {
            var3.cancel();
         }

         this.currentState = var1;
         float var4 = 0.0F;
         float var5 = 0.0F;
         float var6 = 0.1F;
         float var9;
         if (var2) {
            this.currentAnimation = new AnimatorSet();
            var3 = this.currentAnimation;
            TextView var7 = this.textView;
            Property var8 = View.ALPHA;
            if (var1 == 0) {
               var9 = 1.0F;
            } else {
               var9 = 0.0F;
            }

            ObjectAnimator var22 = ObjectAnimator.ofFloat(var7, var8, new float[]{var9});
            var7 = this.textView;
            Property var10 = View.SCALE_X;
            if (var1 == 0) {
               var9 = 1.0F;
            } else {
               var9 = 0.1F;
            }

            ObjectAnimator var21 = ObjectAnimator.ofFloat(var7, var10, new float[]{var9});
            TextView var23 = this.textView;
            Property var11 = View.SCALE_Y;
            if (var1 == 0) {
               var9 = 1.0F;
            } else {
               var9 = 0.1F;
            }

            ObjectAnimator var24 = ObjectAnimator.ofFloat(var23, var11, new float[]{var9});
            ContextProgressView var12 = this.progressView;
            var11 = View.ALPHA;
            if (var1 == 1) {
               var9 = 1.0F;
            } else {
               var9 = 0.0F;
            }

            ObjectAnimator var25 = ObjectAnimator.ofFloat(var12, var11, new float[]{var9});
            ContextProgressView var13 = this.progressView;
            Property var26 = View.SCALE_X;
            if (var1 == 1) {
               var9 = 1.0F;
            } else {
               var9 = 0.1F;
            }

            ObjectAnimator var27 = ObjectAnimator.ofFloat(var13, var26, new float[]{var9});
            var13 = this.progressView;
            Property var14 = View.SCALE_Y;
            if (var1 == 1) {
               var9 = 1.0F;
            } else {
               var9 = 0.1F;
            }

            ObjectAnimator var28 = ObjectAnimator.ofFloat(var13, var14, new float[]{var9});
            ImageView var29 = this.imageView;
            Property var15 = View.ALPHA;
            var9 = var5;
            if (var1 == 2) {
               var9 = 1.0F;
            }

            ObjectAnimator var30 = ObjectAnimator.ofFloat(var29, var15, new float[]{var9});
            ImageView var16 = this.imageView;
            var15 = View.SCALE_X;
            if (var1 == 2) {
               var9 = 1.0F;
            } else {
               var9 = 0.1F;
            }

            ObjectAnimator var31 = ObjectAnimator.ofFloat(var16, var15, new float[]{var9});
            var16 = this.imageView;
            Property var17 = View.SCALE_Y;
            if (var1 == 2) {
               var6 = 1.0F;
            }

            var3.playTogether(new Animator[]{var22, var21, var24, var25, var27, var28, var30, var31, ObjectAnimator.ofFloat(var16, var17, new float[]{var6})});
            this.currentAnimation.setDuration(150L);
            this.currentAnimation.start();
         } else {
            TextView var18 = this.textView;
            if (var1 == 0) {
               var9 = 1.0F;
            } else {
               var9 = 0.0F;
            }

            var18.setAlpha(var9);
            var18 = this.textView;
            if (var1 == 0) {
               var9 = 1.0F;
            } else {
               var9 = 0.1F;
            }

            var18.setScaleX(var9);
            var18 = this.textView;
            if (var1 == 0) {
               var9 = 1.0F;
            } else {
               var9 = 0.1F;
            }

            var18.setScaleY(var9);
            ContextProgressView var19 = this.progressView;
            if (var1 == 1) {
               var9 = 1.0F;
            } else {
               var9 = 0.0F;
            }

            var19.setAlpha(var9);
            var19 = this.progressView;
            if (var1 == 1) {
               var9 = 1.0F;
            } else {
               var9 = 0.1F;
            }

            var19.setScaleX(var9);
            var19 = this.progressView;
            if (var1 == 1) {
               var9 = 1.0F;
            } else {
               var9 = 0.1F;
            }

            var19.setScaleY(var9);
            ImageView var20 = this.imageView;
            var9 = var4;
            if (var1 == 2) {
               var9 = 1.0F;
            }

            var20.setAlpha(var9);
            var20 = this.imageView;
            if (var1 == 2) {
               var9 = 1.0F;
            } else {
               var9 = 0.1F;
            }

            var20.setScaleX(var9);
            var20 = this.imageView;
            if (var1 == 2) {
               var6 = 1.0F;
            }

            var20.setScaleY(var6);
         }

      }
   }

   private class BlockCollageCell extends FrameLayout {
      private ArticleViewer.DrawingText captionLayout;
      private ArticleViewer.DrawingText creditLayout;
      private int creditOffset;
      private TLRPC.TL_pageBlockCollage currentBlock;
      private GridLayoutManager gridLayoutManager;
      private ArticleViewer.BlockCollageCell.GroupedMessages group = new ArticleViewer.BlockCollageCell.GroupedMessages();
      private boolean inLayout;
      private RecyclerView.Adapter innerAdapter;
      private RecyclerListView innerListView;
      private int listX;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private int textX;
      private int textY;

      public BlockCollageCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
         this.innerListView = new RecyclerListView(var2) {
            public void requestLayout() {
               if (!BlockCollageCell.this.inLayout) {
                  super.requestLayout();
               }
            }
         };
         this.innerListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            public void getItemOffsets(Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
               byte var5 = 0;
               var1.bottom = 0;
               MessageObject.GroupedMessagePosition var11;
               if (var2 instanceof ArticleViewer.BlockPhotoCell) {
                  var11 = (MessageObject.GroupedMessagePosition)BlockCollageCell.this.group.positions.get(((ArticleViewer.BlockPhotoCell)var2).currentBlock);
               } else if (var2 instanceof ArticleViewer.BlockVideoCell) {
                  var11 = (MessageObject.GroupedMessagePosition)BlockCollageCell.this.group.positions.get(((ArticleViewer.BlockVideoCell)var2).currentBlock);
               } else {
                  var11 = null;
               }

               if (var11 != null && var11.siblingHeights != null) {
                  Point var12 = AndroidUtilities.displaySize;
                  float var6 = (float)Math.max(var12.x, var12.y) * 0.5F;
                  int var7 = 0;
                  int var8 = 0;

                  while(true) {
                     float[] var13 = var11.siblingHeights;
                     if (var7 >= var13.length) {
                        int var9 = var8 + (var11.maxY - var11.minY) * AndroidUtilities.dp2(11.0F);
                        int var10 = BlockCollageCell.this.group.posArray.size();
                        var7 = var5;

                        while(true) {
                           var8 = var9;
                           if (var7 >= var10) {
                              break;
                           }

                           MessageObject.GroupedMessagePosition var14 = (MessageObject.GroupedMessagePosition)BlockCollageCell.this.group.posArray.get(var7);
                           byte var16 = var14.minY;
                           byte var15 = var11.minY;
                           if (var16 == var15 && (var14.minX != var11.minX || var14.maxX != var11.maxX || var16 != var15 || var14.maxY != var11.maxY) && var14.minY == var11.minY) {
                              var8 = var9 - ((int)Math.ceil((double)(var6 * var14.ph)) - AndroidUtilities.dp(4.0F));
                              break;
                           }

                           ++var7;
                        }

                        var1.bottom = -var8;
                        break;
                     }

                     var8 += (int)Math.ceil((double)(var13[var7] * var6));
                     ++var7;
                  }
               }

            }
         });
         this.gridLayoutManager = new GridLayoutManagerFixed(var2, 1000, 1, true) {
            protected boolean hasSiblingChild(int var1) {
               TLObject var2 = (TLObject)BlockCollageCell.this.currentBlock.items.get(BlockCollageCell.this.currentBlock.items.size() - var1 - 1);
               MessageObject.GroupedMessagePosition var8 = (MessageObject.GroupedMessagePosition)BlockCollageCell.this.group.positions.get(var2);
               if (var8.minX != var8.maxX) {
                  byte var7 = var8.minY;
                  if (var7 == var8.maxY && var7 != 0) {
                     int var3 = BlockCollageCell.this.group.posArray.size();

                     for(var1 = 0; var1 < var3; ++var1) {
                        MessageObject.GroupedMessagePosition var4 = (MessageObject.GroupedMessagePosition)BlockCollageCell.this.group.posArray.get(var1);
                        if (var4 != var8) {
                           byte var5 = var4.minY;
                           byte var6 = var8.minY;
                           if (var5 <= var6 && var4.maxY >= var6) {
                              return true;
                           }
                        }
                     }
                  }
               }

               return false;
            }

            public boolean shouldLayoutChildFromOpositeSide(View var1) {
               return false;
            }

            public boolean supportsPredictiveItemAnimations() {
               return false;
            }
         };
         this.gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            public int getSpanSize(int var1) {
               TLObject var2 = (TLObject)BlockCollageCell.this.currentBlock.items.get(BlockCollageCell.this.currentBlock.items.size() - var1 - 1);
               return ((MessageObject.GroupedMessagePosition)BlockCollageCell.this.group.positions.get(var2)).spanSize;
            }
         });
         this.innerListView.setLayoutManager(this.gridLayoutManager);
         RecyclerListView var5 = this.innerListView;
         RecyclerView.Adapter var4 = new RecyclerView.Adapter() {
            public int getItemCount() {
               return BlockCollageCell.this.currentBlock == null ? 0 : BlockCollageCell.this.currentBlock.items.size();
            }

            public int getItemViewType(int var1) {
               ArrayList var2 = BlockCollageCell.this.currentBlock.items;
               int var3 = BlockCollageCell.this.currentBlock.items.size();
               byte var4 = 1;
               if ((TLRPC.PageBlock)var2.get(var3 - var1 - 1) instanceof TLRPC.TL_pageBlockPhoto) {
                  var4 = 0;
               }

               return var4;
            }

            public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
               TLRPC.PageBlock var3 = (TLRPC.PageBlock)BlockCollageCell.this.currentBlock.items.get(BlockCollageCell.this.currentBlock.items.size() - var2 - 1);
               if (var1.getItemViewType() != 0) {
                  ArticleViewer.BlockVideoCell var4 = (ArticleViewer.BlockVideoCell)var1.itemView;
                  var4.groupPosition = (MessageObject.GroupedMessagePosition)BlockCollageCell.this.group.positions.get(var3);
                  var4.setBlock((TLRPC.TL_pageBlockVideo)var3, true, true);
               } else {
                  ArticleViewer.BlockPhotoCell var5 = (ArticleViewer.BlockPhotoCell)var1.itemView;
                  var5.groupPosition = (MessageObject.GroupedMessagePosition)BlockCollageCell.this.group.positions.get(var3);
                  var5.setBlock((TLRPC.TL_pageBlockPhoto)var3, true, true);
               }

            }

            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
               ArticleViewer.BlockCollageCell var3;
               Object var4;
               if (var2 != 0) {
                  var3 = BlockCollageCell.this;
                  var4 = ArticleViewer.this.new BlockVideoCell(var3.getContext(), BlockCollageCell.this.parentAdapter, 2);
               } else {
                  var3 = BlockCollageCell.this;
                  var4 = ArticleViewer.this.new BlockPhotoCell(var3.getContext(), BlockCollageCell.this.parentAdapter, 2);
               }

               return new RecyclerListView.Holder((View)var4);
            }
         };
         this.innerAdapter = var4;
         var5.setAdapter(var4);
         this.addView(this.innerListView, LayoutHelper.createFrame(-1, -2.0F));
         this.setWillNotDraw(false);
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.captionLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.captionLayout.draw(var1);
               var1.restore();
            }

            if (this.creditLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)(this.textY + this.creditOffset));
               this.creditLayout.draw(var1);
               var1.restore();
            }

            if (this.currentBlock.level > 0) {
               float var2 = (float)AndroidUtilities.dp(18.0F);
               float var3 = (float)AndroidUtilities.dp(20.0F);
               int var4 = this.getMeasuredHeight();
               int var5;
               if (this.currentBlock.bottom) {
                  var5 = AndroidUtilities.dp(6.0F);
               } else {
                  var5 = 0;
               }

               var1.drawRect(var2, 0.0F, var3, (float)(var4 - var5), ArticleViewer.quoteLinePaint);
            }

         }
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         this.innerListView.layout(this.listX, AndroidUtilities.dp(8.0F), this.listX + this.innerListView.getMeasuredWidth(), this.innerListView.getMeasuredHeight() + AndroidUtilities.dp(8.0F));
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         byte var9 = 1;
         this.inLayout = true;
         int var3 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockCollage var4 = this.currentBlock;
         var1 = var9;
         if (var4 != null) {
            var1 = var4.level;
            if (var1 > 0) {
               var1 = AndroidUtilities.dp((float)(var1 * 14)) + AndroidUtilities.dp(18.0F);
               this.listX = var1;
               this.textX = var1;
               var1 = var3 - (this.listX + AndroidUtilities.dp(18.0F));
               var2 = var1;
            } else {
               this.listX = 0;
               this.textX = AndroidUtilities.dp(18.0F);
               var2 = var3 - AndroidUtilities.dp(36.0F);
               var1 = var3;
            }

            this.innerListView.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
            int var5 = this.innerListView.getMeasuredHeight();
            ArticleViewer var6 = ArticleViewer.this;
            var4 = this.currentBlock;
            this.captionLayout = var6.createLayoutForText(this, (CharSequence)null, var4.caption.text, var2, var4, this.parentAdapter);
            var1 = var5;
            if (this.captionLayout != null) {
               this.textY = AndroidUtilities.dp(8.0F) + var5;
               this.creditOffset = AndroidUtilities.dp(4.0F) + this.captionLayout.getHeight();
               var1 = var5 + this.creditOffset + AndroidUtilities.dp(4.0F);
            }

            var6 = ArticleViewer.this;
            TLRPC.TL_pageBlockCollage var7 = this.currentBlock;
            TLRPC.RichText var8 = var7.caption.credit;
            Alignment var10;
            if (var6.isRtl) {
               var10 = Alignment.ALIGN_RIGHT;
            } else {
               var10 = Alignment.ALIGN_NORMAL;
            }

            this.creditLayout = var6.createLayoutForText(this, (CharSequence)null, var8, var2, var7, var10, this.parentAdapter);
            var2 = var1;
            if (this.creditLayout != null) {
               var2 = var1 + AndroidUtilities.dp(4.0F) + this.creditLayout.getHeight();
            }

            var2 += AndroidUtilities.dp(16.0F);
            var4 = this.currentBlock;
            var1 = var2;
            if (var4.level > 0) {
               var1 = var2;
               if (!var4.bottom) {
                  var1 = var2 + AndroidUtilities.dp(8.0F);
               }
            }
         }

         this.setMeasuredDimension(var3, var1);
         this.inLayout = false;
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.captionLayout, this.textX, this.textY) && !ArticleViewer.this.checkLayoutForLinks(var1, this, this.creditLayout, this.textX, this.textY + this.creditOffset) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockCollage var1) {
         if (this.currentBlock != var1) {
            this.currentBlock = var1;
            this.group.calculate();
         }

         this.innerAdapter.notifyDataSetChanged();
         int var2 = ArticleViewer.this.getSelectedColor();
         if (var2 == 0) {
            this.innerListView.setGlowColor(-657673);
         } else if (var2 == 1) {
            this.innerListView.setGlowColor(-659492);
         } else if (var2 == 2) {
            this.innerListView.setGlowColor(-15461356);
         }

         this.requestLayout();
      }

      public class GroupedMessages {
         public long groupId;
         public boolean hasSibling;
         private int maxSizeWidth = 1000;
         public ArrayList posArray = new ArrayList();
         public HashMap positions = new HashMap();

         private float multiHeight(float[] var1, int var2, int var3) {
            float var4;
            for(var4 = 0.0F; var2 < var3; ++var2) {
               var4 += var1[var2];
            }

            return (float)this.maxSizeWidth / var4;
         }

         public void calculate() {
            this.posArray.clear();
            this.positions.clear();
            int var1 = BlockCollageCell.this.currentBlock.items.size();
            if (var1 > 1) {
               StringBuilder var2 = new StringBuilder();
               this.hasSibling = false;
               int var3 = 0;
               float var4 = 1.0F;

               boolean var5;
               float var8;
               boolean var9;
               MessageObject.GroupedMessagePosition var10;
               float var12;
               for(var5 = false; var3 < var1; var5 = var9) {
                  label338: {
                     TLObject var6 = (TLObject)BlockCollageCell.this.currentBlock.items.get(var3);
                     TLRPC.PhotoSize var34;
                     if (var6 instanceof TLRPC.TL_pageBlockPhoto) {
                        TLRPC.TL_pageBlockPhoto var7 = (TLRPC.TL_pageBlockPhoto)var6;
                        TLRPC.Photo var32 = ArticleViewer.this.getPhotoWithId(var7.photo_id);
                        if (var32 == null) {
                           var8 = var4;
                           var9 = var5;
                           break label338;
                        }

                        var34 = FileLoader.getClosestPhotoSizeWithSize(var32.sizes, AndroidUtilities.getPhotoSize());
                     } else {
                        var8 = var4;
                        var9 = var5;
                        if (!(var6 instanceof TLRPC.TL_pageBlockVideo)) {
                           break label338;
                        }

                        TLRPC.TL_pageBlockVideo var35 = (TLRPC.TL_pageBlockVideo)var6;
                        TLRPC.Document var36 = ArticleViewer.this.getDocumentWithId(var35.video_id);
                        if (var36 == null) {
                           var8 = var4;
                           var9 = var5;
                           break label338;
                        }

                        var34 = FileLoader.getClosestPhotoSizeWithSize(var36.thumbs, 90);
                     }

                     var10 = new MessageObject.GroupedMessagePosition();
                     boolean var11;
                     if (var3 == var1 - 1) {
                        var11 = true;
                     } else {
                        var11 = false;
                     }

                     var10.last = var11;
                     if (var34 == null) {
                        var8 = 1.0F;
                     } else {
                        var8 = (float)var34.w / (float)var34.h;
                     }

                     var10.aspectRatio = var8;
                     var8 = var10.aspectRatio;
                     if (var8 > 1.2F) {
                        var2.append("w");
                     } else if (var8 < 0.8F) {
                        var2.append("n");
                     } else {
                        var2.append("q");
                     }

                     var12 = var10.aspectRatio;
                     var8 = var4 + var12;
                     if (var12 > 2.0F) {
                        var5 = true;
                     }

                     this.positions.put(var6, var10);
                     this.posArray.add(var10);
                     var9 = var5;
                  }

                  ++var3;
                  var4 = var8;
               }

               int var13 = AndroidUtilities.dp(120.0F);
               var8 = (float)AndroidUtilities.dp(120.0F);
               Point var37 = AndroidUtilities.displaySize;
               var3 = (int)(var8 / ((float)Math.min(var37.x, var37.y) / (float)this.maxSizeWidth));
               var12 = (float)AndroidUtilities.dp(40.0F);
               var37 = AndroidUtilities.displaySize;
               var8 = (float)Math.min(var37.x, var37.y);
               int var14 = this.maxSizeWidth;
               int var39 = (int)(var12 / (var8 / (float)var14));
               var8 = (float)var14 / 814.0F;
               var4 /= (float)var1;
               MessageObject.GroupedMessagePosition var30;
               int var31;
               MessageObject.GroupedMessagePosition var38;
               if (var5 || var1 != 2 && var1 != 3 && var1 != 4) {
                  float[] var40 = new float[this.posArray.size()];

                  for(var31 = 0; var31 < var1; ++var31) {
                     if (var4 > 1.1F) {
                        var40[var31] = Math.max(1.0F, ((MessageObject.GroupedMessagePosition)this.posArray.get(var31)).aspectRatio);
                     } else {
                        var40[var31] = Math.min(1.0F, ((MessageObject.GroupedMessagePosition)this.posArray.get(var31)).aspectRatio);
                     }

                     var40[var31] = Math.max(0.66667F, Math.min(1.7F, var40[var31]));
                  }

                  ArrayList var33 = new ArrayList();

                  for(var31 = 1; var31 < var40.length; ++var31) {
                     var39 = var40.length - var31;
                     if (var31 <= 3 && var39 <= 3) {
                        var33.add(new ArticleViewer.BlockCollageCell.GroupedMessages.MessageGroupedLayoutAttempt(var31, var39, this.multiHeight(var40, 0, var31), this.multiHeight(var40, var31, var40.length)));
                     }
                  }

                  for(var31 = 1; var31 < var40.length - 1; ++var31) {
                     for(var39 = 1; var39 < var40.length - var31; ++var39) {
                        var14 = var40.length - var31 - var39;
                        if (var31 <= 3) {
                           byte var42;
                           if (var4 < 0.85F) {
                              var42 = 4;
                           } else {
                              var42 = 3;
                           }

                           if (var39 <= var42 && var14 <= 3) {
                              var8 = this.multiHeight(var40, 0, var31);
                              var13 = var31 + var39;
                              var33.add(new ArticleViewer.BlockCollageCell.GroupedMessages.MessageGroupedLayoutAttempt(var31, var39, var14, var8, this.multiHeight(var40, var31, var13), this.multiHeight(var40, var13, var40.length)));
                           }
                        }
                     }
                  }

                  int var20;
                  int var21;
                  for(var39 = 1; var39 < var40.length - 2; ++var39) {
                     for(var31 = 1; var31 < var40.length - var39; ++var31) {
                        for(var13 = 1; var13 < var40.length - var39 - var31; ++var13) {
                           var20 = var40.length - var39 - var31 - var13;
                           if (var39 <= 3 && var31 <= 3 && var13 <= 3 && var20 <= 3) {
                              var4 = this.multiHeight(var40, 0, var39);
                              var21 = var39 + var31;
                              var8 = this.multiHeight(var40, var39, var21);
                              var14 = var21 + var13;
                              var33.add(new ArticleViewer.BlockCollageCell.GroupedMessages.MessageGroupedLayoutAttempt(var39, var31, var13, var20, var4, var8, this.multiHeight(var40, var21, var14), this.multiHeight(var40, var14, var40.length)));
                           }
                        }
                     }
                  }

                  var39 = var1;
                  float var22 = (float)(this.maxSizeWidth / 3 * 4);
                  var1 = 0;
                  ArticleViewer.BlockCollageCell.GroupedMessages.MessageGroupedLayoutAttempt var41 = null;

                  for(var12 = 0.0F; var1 < var33.size(); var12 = var8) {
                     ArticleViewer.BlockCollageCell.GroupedMessages.MessageGroupedLayoutAttempt var26 = (ArticleViewer.BlockCollageCell.GroupedMessages.MessageGroupedLayoutAttempt)var33.get(var1);
                     var31 = 0;
                     var8 = 0.0F;
                     float var23 = Float.MAX_VALUE;

                     while(true) {
                        float[] var43 = var26.heights;
                        if (var31 >= var43.length) {
                           var8 = Math.abs(var8 - var22);
                           int[] var44 = var26.lineCounts;
                           if (var44.length > 1) {
                              label341: {
                                 if (var44[0] <= var44[1] && (var44.length <= 2 || var44[1] <= var44[2])) {
                                    var44 = var26.lineCounts;
                                    if (var44.length <= 3 || var44[2] <= var44[3]) {
                                       break label341;
                                    }
                                 }

                                 var8 *= 1.2F;
                              }
                           }

                           var4 = var8;
                           if (var23 < (float)var3) {
                              var4 = var8 * 1.5F;
                           }

                           label235: {
                              if (var41 != null) {
                                 var8 = var12;
                                 if (var4 >= var12) {
                                    break label235;
                                 }
                              }

                              var41 = var26;
                              var8 = var4;
                           }

                           ++var1;
                           break;
                        }

                        var8 += var43[var31];
                        var4 = var23;
                        if (var43[var31] < var23) {
                           var4 = var43[var31];
                        }

                        ++var31;
                        var23 = var4;
                     }
                  }

                  if (var41 == null) {
                     return;
                  }

                  var13 = 0;
                  var31 = 0;

                  while(true) {
                     int[] var27 = var41.lineCounts;
                     var1 = var39;
                     if (var13 >= var27.length) {
                        break;
                     }

                     var20 = var27[var13];
                     var4 = var41.heights[var13];
                     var3 = this.maxSizeWidth;
                     var1 = var31;
                     var31 = var3;
                     var14 = 0;

                     MessageObject.GroupedMessagePosition var28;
                     for(var28 = null; var14 < var20; var31 = var21) {
                        int var24 = (int)(var40[var1] * var4);
                        var21 = var31 - var24;
                        var30 = (MessageObject.GroupedMessagePosition)this.posArray.get(var1);
                        byte var29;
                        if (var13 == 0) {
                           var29 = 4;
                        } else {
                           var29 = 0;
                        }

                        var31 = var29;
                        if (var13 == var41.lineCounts.length - 1) {
                           var31 = var29 | 8;
                        }

                        var3 = var31;
                        if (var14 == 0) {
                           var3 = var31 | 1;
                        }

                        if (var14 == var20 - 1) {
                           var31 = var3 | 2;
                           var28 = var30;
                        } else {
                           var31 = var3;
                        }

                        var30.set(var14, var14, var13, var13, var24, var4 / 814.0F, var31);
                        ++var1;
                        ++var14;
                     }

                     var28.pw += var31;
                     var28.spanSize += var31;
                     ++var13;
                     var31 = var1;
                  }
               } else if (var1 == 2) {
                  label336: {
                     var38 = (MessageObject.GroupedMessagePosition)this.posArray.get(0);
                     var30 = (MessageObject.GroupedMessagePosition)this.posArray.get(1);
                     String var25 = var2.toString();
                     if (var25.equals("ww")) {
                        double var15 = (double)var4;
                        double var17 = (double)var8;
                        Double.isNaN(var17);
                        if (var15 > var17 * 1.4D) {
                           var4 = var38.aspectRatio;
                           var8 = var30.aspectRatio;
                           if ((double)(var4 - var8) < 0.2D) {
                              var31 = this.maxSizeWidth;
                              var4 = (float)Math.round(Math.min((float)var31 / var4, Math.min((float)var31 / var8, 407.0F))) / 814.0F;
                              var38.set(0, 0, 0, 0, this.maxSizeWidth, var4, 7);
                              var30.set(0, 0, 1, 1, this.maxSizeWidth, var4, 11);
                              break label336;
                           }
                        }
                     }

                     if (!var25.equals("ww") && !var25.equals("qq")) {
                        var31 = this.maxSizeWidth;
                        var4 = (float)var31;
                        var8 = (float)var31;
                        var12 = var38.aspectRatio;
                        var14 = (int)Math.max(var4 * 0.4F, (float)Math.round(var8 / var12 / (1.0F / var12 + 1.0F / var30.aspectRatio)));
                        var13 = this.maxSizeWidth - var14;
                        var39 = var14;
                        var31 = var13;
                        if (var13 < var3) {
                           var39 = var14 - (var3 - var13);
                           var31 = var3;
                        }

                        var4 = Math.min(814.0F, (float)Math.round(Math.min((float)var31 / var38.aspectRatio, (float)var39 / var30.aspectRatio))) / 814.0F;
                        var38.set(0, 0, 0, 0, var31, var4, 13);
                        var30.set(1, 1, 0, 0, var39, var4, 14);
                     } else {
                        var31 = this.maxSizeWidth / 2;
                        var4 = (float)var31;
                        var4 = (float)Math.round(Math.min(var4 / var38.aspectRatio, Math.min(var4 / var30.aspectRatio, 814.0F))) / 814.0F;
                        var38.set(0, 0, 0, 0, var31, var4, 13);
                        var30.set(1, 1, 0, 0, var31, var4, 14);
                     }
                  }
               } else if (var1 == 3) {
                  var10 = (MessageObject.GroupedMessagePosition)this.posArray.get(0);
                  var38 = (MessageObject.GroupedMessagePosition)this.posArray.get(1);
                  var30 = (MessageObject.GroupedMessagePosition)this.posArray.get(2);
                  if (var2.charAt(0) == 'n') {
                     var4 = var38.aspectRatio;
                     var4 = Math.min(407.0F, (float)Math.round((float)this.maxSizeWidth * var4 / (var30.aspectRatio + var4)));
                     var8 = 814.0F - var4;
                     var31 = (int)Math.max((float)var3, Math.min((float)this.maxSizeWidth * 0.5F, (float)Math.round(Math.min(var30.aspectRatio * var4, var38.aspectRatio * var8))));
                     var3 = Math.round(Math.min(var10.aspectRatio * 814.0F + (float)var39, (float)(this.maxSizeWidth - var31)));
                     var10.set(0, 0, 0, 1, var3, 1.0F, 13);
                     var8 /= 814.0F;
                     var38.set(1, 1, 0, 0, var31, var8, 6);
                     var4 /= 814.0F;
                     var30.set(0, 1, 1, 1, var31, var4, 10);
                     var31 = this.maxSizeWidth;
                     var30.spanSize = var31;
                     var10.siblingHeights = new float[]{var4, var8};
                     var38.spanSize = var31 - var3;
                     var30.leftSpanOffset = var3;
                     this.hasSibling = true;
                  } else {
                     var8 = (float)Math.round(Math.min((float)this.maxSizeWidth / var10.aspectRatio, 537.24005F)) / 814.0F;
                     var10.set(0, 1, 0, 0, this.maxSizeWidth, var8, 7);
                     var31 = this.maxSizeWidth / 2;
                     var4 = (float)var31;
                     var4 = Math.min(814.0F - var8, (float)Math.round(Math.min(var4 / var38.aspectRatio, var4 / var30.aspectRatio))) / 814.0F;
                     var38.set(0, 0, 1, 1, var31, var4, 9);
                     var30.set(1, 1, 1, 1, var31, var4, 10);
                  }
               } else if (var1 == 4) {
                  MessageObject.GroupedMessagePosition var19 = (MessageObject.GroupedMessagePosition)this.posArray.get(0);
                  var10 = (MessageObject.GroupedMessagePosition)this.posArray.get(1);
                  var38 = (MessageObject.GroupedMessagePosition)this.posArray.get(2);
                  var30 = (MessageObject.GroupedMessagePosition)this.posArray.get(3);
                  if (var2.charAt(0) == 'w') {
                     var4 = (float)Math.round(Math.min((float)this.maxSizeWidth / var19.aspectRatio, 537.24005F)) / 814.0F;
                     var19.set(0, 2, 0, 0, this.maxSizeWidth, var4, 7);
                     var12 = (float)Math.round((float)this.maxSizeWidth / (var10.aspectRatio + var38.aspectRatio + var30.aspectRatio));
                     var8 = (float)var3;
                     var31 = (int)Math.max(var8, Math.min((float)this.maxSizeWidth * 0.4F, var10.aspectRatio * var12));
                     var3 = (int)Math.max(Math.max(var8, (float)this.maxSizeWidth * 0.33F), var30.aspectRatio * var12);
                     var39 = this.maxSizeWidth;
                     var4 = Math.min(814.0F - var4, var12) / 814.0F;
                     var10.set(0, 0, 1, 1, var31, var4, 9);
                     var38.set(1, 1, 1, 1, var39 - var31 - var3, var4, 8);
                     var30.set(2, 2, 1, 1, var3, var4, 10);
                  } else {
                     var31 = Math.max(var3, Math.round(814.0F / (1.0F / var10.aspectRatio + 1.0F / var38.aspectRatio + 1.0F / ((MessageObject.GroupedMessagePosition)this.posArray.get(3)).aspectRatio)));
                     var8 = (float)var13;
                     var12 = (float)var31;
                     var4 = Math.min(0.33F, Math.max(var8, var12 / var10.aspectRatio) / 814.0F);
                     var8 = Math.min(0.33F, Math.max(var8, var12 / var38.aspectRatio) / 814.0F);
                     var12 = 1.0F - var4 - var8;
                     var3 = Math.round(Math.min(814.0F * var19.aspectRatio + (float)var39, (float)(this.maxSizeWidth - var31)));
                     var19.set(0, 0, 0, 2, var3, var4 + var8 + var12, 13);
                     var10.set(1, 1, 0, 0, var31, var4, 6);
                     var38.set(0, 1, 1, 1, var31, var8, 2);
                     var38.spanSize = this.maxSizeWidth;
                     var30.set(0, 1, 2, 2, var31, var12, 10);
                     var31 = this.maxSizeWidth;
                     var30.spanSize = var31;
                     var10.spanSize = var31 - var3;
                     var38.leftSpanOffset = var3;
                     var30.leftSpanOffset = var3;
                     var19.siblingHeights = new float[]{var4, var8, var12};
                     this.hasSibling = true;
                  }
               }

               for(var31 = 0; var31 < var1; ++var31) {
                  var38 = (MessageObject.GroupedMessagePosition)this.posArray.get(var31);
                  if ((var38.flags & 1) != 0) {
                     var38.edge = true;
                  }
               }

            }
         }

         private class MessageGroupedLayoutAttempt {
            public float[] heights;
            public int[] lineCounts;

            public MessageGroupedLayoutAttempt(int var2, int var3, float var4, float var5) {
               this.lineCounts = new int[]{var2, var3};
               this.heights = new float[]{var4, var5};
            }

            public MessageGroupedLayoutAttempt(int var2, int var3, int var4, float var5, float var6, float var7) {
               this.lineCounts = new int[]{var2, var3, var4};
               this.heights = new float[]{var5, var6, var7};
            }

            public MessageGroupedLayoutAttempt(int var2, int var3, int var4, int var5, float var6, float var7, float var8, float var9) {
               this.lineCounts = new int[]{var2, var3, var4, var5};
               this.heights = new float[]{var6, var7, var8, var9};
            }
         }
      }
   }

   private class BlockDetailsBottomCell extends View {
      private RectF rect = new RectF();

      public BlockDetailsBottomCell(Context var2) {
         super(var2);
      }

      protected void onDraw(Canvas var1) {
         var1.drawLine(0.0F, 0.0F, (float)this.getMeasuredWidth(), 0.0F, ArticleViewer.dividerPaint);
      }

      protected void onMeasure(int var1, int var2) {
         this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(4.0F) + 1);
      }
   }

   private class BlockDetailsCell extends View implements Callback {
      private AnimatedArrowDrawable arrow;
      private TLRPC.TL_pageBlockDetails currentBlock;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ArticleViewer.DrawingText textLayout;
      private int textX = AndroidUtilities.dp(50.0F);
      private int textY = AndroidUtilities.dp(11.0F) + 1;

      public BlockDetailsCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
         this.arrow = new AnimatedArrowDrawable(ArticleViewer.this.getGrayTextColor(), true);
      }

      public void invalidateDrawable(Drawable var1) {
         this.invalidate();
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            var1.save();
            var1.translate((float)AndroidUtilities.dp(18.0F), (float)((this.getMeasuredHeight() - AndroidUtilities.dp(13.0F) - 1) / 2));
            this.arrow.draw(var1);
            var1.restore();
            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

            float var2 = (float)(this.getMeasuredHeight() - 1);
            var1.drawLine(0.0F, var2, (float)this.getMeasuredWidth(), var2, ArticleViewer.dividerPaint);
         }
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         var2 = AndroidUtilities.dp(39.0F);
         TLRPC.TL_pageBlockDetails var4 = this.currentBlock;
         var1 = var2;
         if (var4 != null) {
            ArticleViewer var5 = ArticleViewer.this;
            TLRPC.RichText var6 = var4.title;
            var1 = AndroidUtilities.dp(52.0F);
            TLRPC.TL_pageBlockDetails var7 = this.currentBlock;
            Alignment var8;
            if (ArticleViewer.this.isRtl) {
               var8 = Alignment.ALIGN_RIGHT;
            } else {
               var8 = Alignment.ALIGN_NORMAL;
            }

            this.textLayout = var5.createLayoutForText(this, (CharSequence)null, var6, var3 - var1, var7, var8, this.parentAdapter);
            var1 = var2;
            if (this.textLayout != null) {
               var1 = Math.max(var2, AndroidUtilities.dp(21.0F) + this.textLayout.getHeight());
               this.textY = (this.textLayout.getHeight() + AndroidUtilities.dp(21.0F) - this.textLayout.getHeight()) / 2;
            }
         }

         this.setMeasuredDimension(var3, var1 + 1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void scheduleDrawable(Drawable var1, Runnable var2, long var3) {
      }

      public void setBlock(TLRPC.TL_pageBlockDetails var1) {
         this.currentBlock = var1;
         AnimatedArrowDrawable var2 = this.arrow;
         float var3;
         if (var1.open) {
            var3 = 0.0F;
         } else {
            var3 = 1.0F;
         }

         var2.setAnimationProgress(var3);
         this.arrow.setCallback(this);
         this.requestLayout();
      }

      public void unscheduleDrawable(Drawable var1, Runnable var2) {
      }
   }

   private class BlockDividerCell extends View {
      private RectF rect = new RectF();

      public BlockDividerCell(Context var2) {
         super(var2);
      }

      protected void onDraw(Canvas var1) {
         int var2 = this.getMeasuredWidth() / 3;
         this.rect.set((float)var2, (float)AndroidUtilities.dp(8.0F), (float)(var2 * 2), (float)AndroidUtilities.dp(10.0F));
         var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(1.0F), (float)AndroidUtilities.dp(1.0F), ArticleViewer.dividerPaint);
      }

      protected void onMeasure(int var1, int var2) {
         this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(18.0F));
      }
   }

   private class BlockEmbedCell extends FrameLayout {
      private ArticleViewer.DrawingText captionLayout;
      private ArticleViewer.DrawingText creditLayout;
      private int creditOffset;
      private TLRPC.TL_pageBlockEmbed currentBlock;
      private int exactWebViewHeight;
      private int listX;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private int textX;
      private int textY;
      private WebPlayerView videoView;
      private boolean wasUserInteraction;
      private ArticleViewer.BlockEmbedCell.TouchyWebView webView;

      @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
      public BlockEmbedCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
         this.setWillNotDraw(false);
         this.videoView = new WebPlayerView(var2, false, false, new WebPlayerView.WebPlayerViewDelegate() {
            public boolean checkInlinePermissions() {
               return false;
            }

            public ViewGroup getTextureViewContainer() {
               return null;
            }

            public void onInitFailed() {
               BlockEmbedCell.this.webView.setVisibility(0);
               BlockEmbedCell.this.videoView.setVisibility(4);
               BlockEmbedCell.this.videoView.loadVideo((String)null, (TLRPC.Photo)null, (Object)null, (String)null, false);
               HashMap var1 = new HashMap();
               var1.put("Referer", "http://youtube.com");
               BlockEmbedCell.this.webView.loadUrl(BlockEmbedCell.this.currentBlock.url, var1);
            }

            public void onInlineSurfaceTextureReady() {
            }

            public void onPlayStateChanged(WebPlayerView var1, boolean var2) {
               if (var2) {
                  if (ArticleViewer.this.currentPlayingVideo != null && ArticleViewer.this.currentPlayingVideo != var1) {
                     ArticleViewer.this.currentPlayingVideo.pause();
                  }

                  ArticleViewer.this.currentPlayingVideo = var1;

                  try {
                     ArticleViewer.this.parentActivity.getWindow().addFlags(128);
                  } catch (Exception var4) {
                     FileLog.e((Throwable)var4);
                  }
               } else {
                  if (ArticleViewer.this.currentPlayingVideo == var1) {
                     ArticleViewer.this.currentPlayingVideo = null;
                  }

                  try {
                     ArticleViewer.this.parentActivity.getWindow().clearFlags(128);
                  } catch (Exception var3) {
                     FileLog.e((Throwable)var3);
                  }
               }

            }

            public void onSharePressed() {
               if (ArticleViewer.this.parentActivity != null) {
                  ArticleViewer var1 = ArticleViewer.this;
                  var1.showDialog(new ShareAlert(var1.parentActivity, (ArrayList)null, BlockEmbedCell.this.currentBlock.url, false, BlockEmbedCell.this.currentBlock.url, true));
               }
            }

            public TextureView onSwitchInlineMode(View var1, boolean var2, float var3, int var4, boolean var5) {
               return null;
            }

            public TextureView onSwitchToFullscreen(View var1, boolean var2, float var3, int var4, boolean var5) {
               if (var2) {
                  ArticleViewer.this.fullscreenAspectRatioView.addView(ArticleViewer.this.fullscreenTextureView, LayoutHelper.createFrame(-1, -1.0F));
                  ArticleViewer.this.fullscreenAspectRatioView.setVisibility(0);
                  ArticleViewer.this.fullscreenAspectRatioView.setAspectRatio(var3, var4);
                  ArticleViewer.BlockEmbedCell var6 = BlockEmbedCell.this;
                  ArticleViewer.this.fullscreenedVideo = var6.videoView;
                  ArticleViewer.this.fullscreenVideoContainer.addView(var1, LayoutHelper.createFrame(-1, -1.0F));
                  ArticleViewer.this.fullscreenVideoContainer.setVisibility(0);
               } else {
                  ArticleViewer.this.fullscreenAspectRatioView.removeView(ArticleViewer.this.fullscreenTextureView);
                  ArticleViewer.this.fullscreenedVideo = null;
                  ArticleViewer.this.fullscreenAspectRatioView.setVisibility(8);
                  ArticleViewer.this.fullscreenVideoContainer.setVisibility(4);
               }

               return ArticleViewer.this.fullscreenTextureView;
            }

            public void onVideoSizeChanged(float var1, int var2) {
               ArticleViewer.this.fullscreenAspectRatioView.setAspectRatio(var1, var2);
            }

            public void prepareToSwitchInlineMode(boolean var1, Runnable var2, float var3, boolean var4) {
            }
         });
         this.addView(this.videoView);
         ArticleViewer.this.createdWebViews.add(this);
         this.webView = new ArticleViewer.BlockEmbedCell.TouchyWebView(var2);
         this.webView.getSettings().setJavaScriptEnabled(true);
         this.webView.getSettings().setDomStorageEnabled(true);
         this.webView.getSettings().setAllowContentAccess(true);
         if (VERSION.SDK_INT >= 17) {
            this.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
            this.webView.addJavascriptInterface(new ArticleViewer.BlockEmbedCell.TelegramWebviewProxy(), "TelegramWebviewProxy");
         }

         if (VERSION.SDK_INT >= 21) {
            this.webView.getSettings().setMixedContentMode(0);
            CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
         }

         this.webView.setWebChromeClient(new WebChromeClient() {
            // $FF: synthetic method
            public void lambda$onShowCustomView$0$ArticleViewer$BlockEmbedCell$2() {
               if (ArticleViewer.this.customView != null) {
                  ArticleViewer.this.fullscreenVideoContainer.addView(ArticleViewer.this.customView, LayoutHelper.createFrame(-1, -1.0F));
                  ArticleViewer.this.fullscreenVideoContainer.setVisibility(0);
               }

            }

            public void onHideCustomView() {
               super.onHideCustomView();
               if (ArticleViewer.this.customView != null) {
                  ArticleViewer.this.fullscreenVideoContainer.setVisibility(4);
                  ArticleViewer.this.fullscreenVideoContainer.removeView(ArticleViewer.this.customView);
                  if (ArticleViewer.this.customViewCallback != null && !ArticleViewer.this.customViewCallback.getClass().getName().contains(".chromium.")) {
                     ArticleViewer.this.customViewCallback.onCustomViewHidden();
                  }

                  ArticleViewer.this.customView = null;
               }
            }

            public void onShowCustomView(View var1, int var2, CustomViewCallback var3) {
               this.onShowCustomView(var1, var3);
            }

            public void onShowCustomView(View var1, CustomViewCallback var2) {
               if (ArticleViewer.this.customView != null) {
                  var2.onCustomViewHidden();
               } else {
                  ArticleViewer.this.customView = var1;
                  ArticleViewer.this.customViewCallback = var2;
                  AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$BlockEmbedCell$2$BQ0m5DiCWR6D1N_jvzFvaMkwwdA(this), 100L);
               }
            }
         });
         this.webView.setWebViewClient(new WebViewClient() {
            public void onLoadResource(WebView var1, String var2) {
               super.onLoadResource(var1, var2);
            }

            public void onPageFinished(WebView var1, String var2) {
               super.onPageFinished(var1, var2);
            }

            public boolean shouldOverrideUrlLoading(WebView var1, String var2) {
               if (BlockEmbedCell.this.wasUserInteraction) {
                  Browser.openUrl(ArticleViewer.this.parentActivity, (String)var2);
                  return true;
               } else {
                  return false;
               }
            }
         });
         this.addView(this.webView);
      }

      public void destroyWebView(boolean var1) {
         label28: {
            Exception var10000;
            label30: {
               boolean var10001;
               try {
                  this.webView.stopLoading();
                  this.webView.loadUrl("about:blank");
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label30;
               }

               if (var1) {
                  try {
                     this.webView.destroy();
                  } catch (Exception var4) {
                     var10000 = var4;
                     var10001 = false;
                     break label30;
                  }
               }

               try {
                  this.currentBlock = null;
                  break label28;
               } catch (Exception var3) {
                  var10000 = var3;
                  var10001 = false;
               }
            }

            Exception var2 = var10000;
            FileLog.e((Throwable)var2);
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

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.captionLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.captionLayout.draw(var1);
               var1.restore();
            }

            if (this.creditLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)(this.textY + this.creditOffset));
               this.creditLayout.draw(var1);
               var1.restore();
            }

            if (this.currentBlock.level > 0) {
               float var2 = (float)AndroidUtilities.dp(18.0F);
               float var3 = (float)AndroidUtilities.dp(20.0F);
               int var4 = this.getMeasuredHeight();
               int var5;
               if (this.currentBlock.bottom) {
                  var5 = AndroidUtilities.dp(6.0F);
               } else {
                  var5 = 0;
               }

               var1.drawRect(var2, 0.0F, var3, (float)(var4 - var5), ArticleViewer.quoteLinePaint);
            }

         }
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         ArticleViewer.BlockEmbedCell.TouchyWebView var6 = this.webView;
         var2 = this.listX;
         var6.layout(var2, 0, var6.getMeasuredWidth() + var2, this.webView.getMeasuredHeight());
         if (this.videoView.getParent() == this) {
            WebPlayerView var7 = this.videoView;
            var2 = this.listX;
            var7.layout(var2, 0, var7.getMeasuredWidth() + var2, this.videoView.getMeasuredHeight());
         }

      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockEmbed var4 = this.currentBlock;
         if (var4 != null) {
            label75: {
               var1 = var4.level;
               int var5;
               int var6;
               if (var1 > 0) {
                  var1 = AndroidUtilities.dp((float)(var1 * 14)) + AndroidUtilities.dp(18.0F);
                  this.listX = var1;
                  this.textX = var1;
                  var5 = var3 - (this.listX + AndroidUtilities.dp(18.0F));
                  var6 = var5;
               } else {
                  this.listX = 0;
                  this.textX = AndroidUtilities.dp(18.0F);
                  var2 = AndroidUtilities.dp(36.0F);
                  if (!this.currentBlock.full_width) {
                     var1 = var3 - AndroidUtilities.dp(36.0F);
                     this.listX += AndroidUtilities.dp(18.0F);
                  } else {
                     var1 = var3;
                  }

                  var6 = var3 - var2;
                  var5 = var1;
               }

               var1 = this.currentBlock.w;
               float var7;
               if (var1 == 0) {
                  var7 = 1.0F;
               } else {
                  var7 = (float)var3 / (float)var1;
               }

               var1 = this.exactWebViewHeight;
               if (var1 != 0) {
                  var2 = AndroidUtilities.dp((float)var1);
               } else {
                  var4 = this.currentBlock;
                  if (var4.w == 0) {
                     var1 = AndroidUtilities.dp((float)var4.h);
                  } else {
                     var1 = var4.h;
                  }

                  var2 = (int)((float)var1 * var7);
               }

               var1 = var2;
               if (var2 == 0) {
                  var1 = AndroidUtilities.dp(10.0F);
               }

               var2 = var1;
               this.webView.measure(MeasureSpec.makeMeasureSpec(var5, 1073741824), MeasureSpec.makeMeasureSpec(var1, 1073741824));
               if (this.videoView.getParent() == this) {
                  this.videoView.measure(MeasureSpec.makeMeasureSpec(var5, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(10.0F) + var1, 1073741824));
               }

               ArticleViewer var11 = ArticleViewer.this;
               TLRPC.TL_pageBlockEmbed var8 = this.currentBlock;
               this.captionLayout = var11.createLayoutForText(this, (CharSequence)null, var8.caption.text, var6, var8, this.parentAdapter);
               var1 = var1;
               if (this.captionLayout != null) {
                  this.textY = AndroidUtilities.dp(8.0F) + var2;
                  this.creditOffset = AndroidUtilities.dp(4.0F) + this.captionLayout.getHeight();
                  var1 = var2 + this.creditOffset + AndroidUtilities.dp(4.0F);
               }

               ArticleViewer var9 = ArticleViewer.this;
               TLRPC.TL_pageBlockEmbed var10 = this.currentBlock;
               TLRPC.RichText var13 = var10.caption.credit;
               Alignment var12;
               if (var9.isRtl) {
                  var12 = Alignment.ALIGN_RIGHT;
               } else {
                  var12 = Alignment.ALIGN_NORMAL;
               }

               this.creditLayout = var9.createLayoutForText(this, (CharSequence)null, var13, var6, var10, var12, this.parentAdapter);
               var2 = var1;
               if (this.creditLayout != null) {
                  var2 = var1 + AndroidUtilities.dp(4.0F) + this.creditLayout.getHeight();
               }

               var2 += AndroidUtilities.dp(5.0F);
               var4 = this.currentBlock;
               if (var4.level > 0 && !var4.bottom) {
                  var1 = AndroidUtilities.dp(8.0F);
               } else {
                  var1 = var2;
                  if (this.currentBlock.level != 0) {
                     break label75;
                  }

                  var1 = var2;
                  if (this.captionLayout == null) {
                     break label75;
                  }

                  var1 = AndroidUtilities.dp(8.0F);
               }

               var1 += var2;
            }
         } else {
            var1 = 1;
         }

         this.setMeasuredDimension(var3, var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.captionLayout, this.textX, this.textY) && !ArticleViewer.this.checkLayoutForLinks(var1, this, this.creditLayout, this.textX, this.textY + this.creditOffset) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockEmbed var1) {
         TLRPC.TL_pageBlockEmbed var2 = this.currentBlock;
         this.currentBlock = var1;
         TLRPC.TL_pageBlockEmbed var3 = this.currentBlock;
         if (var2 != var3) {
            label66: {
               this.wasUserInteraction = false;
               if (var3.allow_scrolling) {
                  this.webView.setVerticalScrollBarEnabled(true);
                  this.webView.setHorizontalScrollBarEnabled(true);
               } else {
                  this.webView.setVerticalScrollBarEnabled(false);
                  this.webView.setHorizontalScrollBarEnabled(false);
               }

               this.exactWebViewHeight = 0;

               try {
                  this.webView.loadUrl("about:blank");
               } catch (Exception var4) {
                  FileLog.e((Throwable)var4);
               }

               Exception var10000;
               label68: {
                  boolean var10001;
                  try {
                     if (this.currentBlock.html != null) {
                        this.webView.loadDataWithBaseURL("https://telegram.org/embed", this.currentBlock.html, "text/html", "UTF-8", (String)null);
                        this.videoView.setVisibility(4);
                        this.videoView.loadVideo((String)null, (TLRPC.Photo)null, (Object)null, (String)null, false);
                        this.webView.setVisibility(0);
                        break label66;
                     }
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                     break label68;
                  }

                  TLRPC.Photo var11;
                  label58: {
                     try {
                        if (this.currentBlock.poster_photo_id != 0L) {
                           var11 = ArticleViewer.this.getPhotoWithId(this.currentBlock.poster_photo_id);
                           break label58;
                        }
                     } catch (Exception var8) {
                        var10000 = var8;
                        var10001 = false;
                        break label68;
                     }

                     var11 = null;
                  }

                  try {
                     if (this.videoView.loadVideo(var1.url, var11, ArticleViewer.this.currentPage, (String)null, false)) {
                        this.webView.setVisibility(4);
                        this.videoView.setVisibility(0);
                        this.webView.stopLoading();
                        this.webView.loadUrl("about:blank");
                        break label66;
                     }
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label68;
                  }

                  try {
                     this.webView.setVisibility(0);
                     this.videoView.setVisibility(4);
                     this.videoView.loadVideo((String)null, (TLRPC.Photo)null, (Object)null, (String)null, false);
                     HashMap var10 = new HashMap();
                     var10.put("Referer", "http://youtube.com");
                     this.webView.loadUrl(this.currentBlock.url, var10);
                     break label66;
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                  }
               }

               Exception var9 = var10000;
               FileLog.e((Throwable)var9);
            }
         }

         this.requestLayout();
      }

      private class TelegramWebviewProxy {
         private TelegramWebviewProxy() {
         }

         // $FF: synthetic method
         TelegramWebviewProxy(Object var2) {
            this();
         }

         // $FF: synthetic method
         public void lambda$postEvent$0$ArticleViewer$BlockEmbedCell$TelegramWebviewProxy(String var1, String var2) {
            if ("resize_frame".equals(var1)) {
               try {
                  JSONObject var4 = new JSONObject(var2);
                  BlockEmbedCell.this.exactWebViewHeight = Utilities.parseInt(var4.getString("height"));
                  BlockEmbedCell.this.requestLayout();
               } catch (Throwable var3) {
               }
            }

         }

         @JavascriptInterface
         public void postEvent(String var1, String var2) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ArticleViewer$BlockEmbedCell$TelegramWebviewProxy$9fT_04iuc3H2EZHCwJyjCPpU95o(this, var1, var2));
         }
      }

      public class TouchyWebView extends WebView {
         public TouchyWebView(Context var2) {
            super(var2);
            this.setFocusable(false);
         }

         public boolean onTouchEvent(MotionEvent var1) {
            BlockEmbedCell.this.wasUserInteraction = true;
            if (BlockEmbedCell.this.currentBlock != null) {
               if (BlockEmbedCell.this.currentBlock.allow_scrolling) {
                  this.requestDisallowInterceptTouchEvent(true);
               } else {
                  ArticleViewer.this.windowView.requestDisallowInterceptTouchEvent(true);
               }
            }

            return super.onTouchEvent(var1);
         }
      }
   }

   private class BlockEmbedPostCell extends View {
      private AvatarDrawable avatarDrawable;
      private ImageReceiver avatarImageView;
      private boolean avatarVisible;
      private ArticleViewer.DrawingText captionLayout;
      private ArticleViewer.DrawingText creditLayout;
      private int creditOffset;
      private TLRPC.TL_pageBlockEmbedPost currentBlock;
      private ArticleViewer.DrawingText dateLayout;
      private int dateX;
      private int lineHeight;
      private ArticleViewer.DrawingText nameLayout;
      private int nameX;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private int textX;
      private int textY;

      public BlockEmbedPostCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
         this.avatarImageView = new ImageReceiver(this);
         this.avatarImageView.setRoundRadius(AndroidUtilities.dp(20.0F));
         this.avatarImageView.setImageCoords(AndroidUtilities.dp(32.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(40.0F), AndroidUtilities.dp(40.0F));
         this.avatarDrawable = new AvatarDrawable();
      }

      protected void onDraw(Canvas var1) {
         TLRPC.TL_pageBlockEmbedPost var2 = this.currentBlock;
         if (var2 != null) {
            if (!(var2 instanceof ArticleViewer.TL_pageBlockEmbedPostCaption)) {
               if (this.avatarVisible) {
                  this.avatarImageView.draw(var1);
               }

               ArticleViewer.DrawingText var9 = this.nameLayout;
               byte var3 = 54;
               byte var4 = 0;
               byte var5;
               float var6;
               float var7;
               if (var9 != null) {
                  var1.save();
                  if (this.avatarVisible) {
                     var5 = 54;
                  } else {
                     var5 = 0;
                  }

                  var6 = (float)AndroidUtilities.dp((float)(var5 + 32));
                  if (this.dateLayout != null) {
                     var7 = 10.0F;
                  } else {
                     var7 = 19.0F;
                  }

                  var1.translate(var6, (float)AndroidUtilities.dp(var7));
                  this.nameLayout.draw(var1);
                  var1.restore();
               }

               if (this.dateLayout != null) {
                  var1.save();
                  if (this.avatarVisible) {
                     var5 = var3;
                  } else {
                     var5 = 0;
                  }

                  var1.translate((float)AndroidUtilities.dp((float)(var5 + 32)), (float)AndroidUtilities.dp(29.0F));
                  this.dateLayout.draw(var1);
                  var1.restore();
               }

               float var8 = (float)AndroidUtilities.dp(18.0F);
               var6 = (float)AndroidUtilities.dp(6.0F);
               var7 = (float)AndroidUtilities.dp(20.0F);
               int var10 = this.lineHeight;
               int var11;
               if (this.currentBlock.level != 0) {
                  var11 = var4;
               } else {
                  var11 = AndroidUtilities.dp(6.0F);
               }

               var1.drawRect(var8, var6, var7, (float)(var10 - var11), ArticleViewer.quoteLinePaint);
            }

            if (this.captionLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.captionLayout.draw(var1);
               var1.restore();
            }

            if (this.creditLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)(this.textY + this.creditOffset));
               this.creditLayout.draw(var1);
               var1.restore();
            }

         }
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockEmbedPost var4 = this.currentBlock;
         var1 = 1;
         if (var4 != null) {
            boolean var5 = var4 instanceof ArticleViewer.TL_pageBlockEmbedPostCaption;
            byte var10 = 0;
            var1 = 0;
            TLRPC.TL_pageBlockEmbedPost var6;
            ArticleViewer var7;
            ArticleViewer var12;
            Alignment var13;
            if (var5) {
               var2 = var3 - AndroidUtilities.dp(50.0F);
               var12 = ArticleViewer.this;
               var6 = this.currentBlock;
               this.captionLayout = var12.createLayoutForText(this, (CharSequence)null, var6.caption.text, var2, var6, this.parentAdapter);
               if (this.captionLayout != null) {
                  this.creditOffset = AndroidUtilities.dp(4.0F) + this.captionLayout.getHeight();
                  var1 = 0 + this.creditOffset + AndroidUtilities.dp(4.0F);
               }

               var7 = ArticleViewer.this;
               var6 = this.currentBlock;
               TLRPC.RichText var8 = var6.caption.credit;
               if (var7.isRtl) {
                  var13 = Alignment.ALIGN_RIGHT;
               } else {
                  var13 = Alignment.ALIGN_NORMAL;
               }

               this.creditLayout = var7.createLayoutForText(this, (CharSequence)null, var8, var2, var6, var13, this.parentAdapter);
               var2 = var1;
               if (this.creditLayout != null) {
                  var2 = var1 + AndroidUtilities.dp(4.0F) + this.creditLayout.getHeight();
               }

               this.textX = AndroidUtilities.dp(18.0F);
               this.textY = AndroidUtilities.dp(4.0F);
               var1 = var2;
            } else {
               if (var4.author_photo_id != 0L) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               this.avatarVisible = var5;
               if (var5) {
                  TLRPC.Photo var16 = ArticleViewer.this.getPhotoWithId(this.currentBlock.author_photo_id);
                  var5 = var16 instanceof TLRPC.TL_photo;
                  this.avatarVisible = var5;
                  if (var5) {
                     this.avatarDrawable.setInfo(0, this.currentBlock.author, (String)null, false);
                     TLRPC.PhotoSize var14 = FileLoader.getClosestPhotoSizeWithSize(var16.sizes, AndroidUtilities.dp(40.0F), true);
                     this.avatarImageView.setImage(ImageLocation.getForPhoto(var14, var16), "40_40", this.avatarDrawable, 0, (String)null, ArticleViewer.this.currentPage, 1);
                  }
               }

               ArticleViewer var17 = ArticleViewer.this;
               String var15 = this.currentBlock.author;
               byte var11;
               if (this.avatarVisible) {
                  var11 = 54;
               } else {
                  var11 = 0;
               }

               this.nameLayout = var17.createLayoutForText(this, var15, (TLRPC.RichText)null, var3 - AndroidUtilities.dp((float)(var11 + 50)), 0, this.currentBlock, Alignment.ALIGN_NORMAL, 1, this.parentAdapter);
               if (this.currentBlock.date != 0) {
                  var12 = ArticleViewer.this;
                  long var10001 = (long)this.currentBlock.date;
                  String var18 = LocaleController.getInstance().chatFullDate.format(var10001 * 1000L);
                  var11 = var10;
                  if (this.avatarVisible) {
                     var11 = 54;
                  }

                  this.dateLayout = var12.createLayoutForText(this, var18, (TLRPC.RichText)null, var3 - AndroidUtilities.dp((float)(var11 + 50)), this.currentBlock, this.parentAdapter);
               } else {
                  this.dateLayout = null;
               }

               var2 = AndroidUtilities.dp(56.0F);
               if (this.currentBlock.blocks.isEmpty()) {
                  int var9 = var3 - AndroidUtilities.dp(50.0F);
                  var12 = ArticleViewer.this;
                  var6 = this.currentBlock;
                  this.captionLayout = var12.createLayoutForText(this, (CharSequence)null, var6.caption.text, var9, var6, this.parentAdapter);
                  var1 = var2;
                  if (this.captionLayout != null) {
                     this.creditOffset = AndroidUtilities.dp(4.0F) + this.captionLayout.getHeight();
                     var1 = var2 + this.creditOffset + AndroidUtilities.dp(4.0F);
                  }

                  var2 = var1;
                  var7 = ArticleViewer.this;
                  TLRPC.TL_pageBlockEmbedPost var20 = this.currentBlock;
                  TLRPC.RichText var19 = var20.caption.credit;
                  if (var7.isRtl) {
                     var13 = Alignment.ALIGN_RIGHT;
                  } else {
                     var13 = Alignment.ALIGN_NORMAL;
                  }

                  this.creditLayout = var7.createLayoutForText(this, (CharSequence)null, var19, var9, var20, var13, this.parentAdapter);
                  var1 = var1;
                  if (this.creditLayout != null) {
                     var1 = var2 + AndroidUtilities.dp(4.0F) + this.creditLayout.getHeight();
                  }

                  this.textX = AndroidUtilities.dp(32.0F);
                  this.textY = AndroidUtilities.dp(56.0F);
               } else {
                  this.captionLayout = null;
                  this.creditLayout = null;
                  var1 = var2;
               }
            }

            this.lineHeight = var1;
         }

         this.setMeasuredDimension(var3, var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.captionLayout, this.textX, this.textY) && !ArticleViewer.this.checkLayoutForLinks(var1, this, this.creditLayout, this.textX, this.textY + this.creditOffset) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockEmbedPost var1) {
         this.currentBlock = var1;
         this.requestLayout();
      }
   }

   private class BlockFooterCell extends View {
      private TLRPC.TL_pageBlockFooter currentBlock;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ArticleViewer.DrawingText textLayout;
      private int textX = AndroidUtilities.dp(18.0F);
      private int textY = AndroidUtilities.dp(8.0F);

      public BlockFooterCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

            if (this.currentBlock.level > 0) {
               float var2 = (float)AndroidUtilities.dp(18.0F);
               float var3 = (float)AndroidUtilities.dp(20.0F);
               int var4 = this.getMeasuredHeight();
               int var5;
               if (this.currentBlock.bottom) {
                  var5 = AndroidUtilities.dp(6.0F);
               } else {
                  var5 = 0;
               }

               var1.drawRect(var2, 0.0F, var3, (float)(var4 - var5), ArticleViewer.quoteLinePaint);
            }

         }
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         var2 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockFooter var3 = this.currentBlock;
         var1 = 0;
         if (var3 != null) {
            int var4 = var3.level;
            if (var4 == 0) {
               this.textY = AndroidUtilities.dp(8.0F);
               this.textX = AndroidUtilities.dp(18.0F);
            } else {
               this.textY = 0;
               this.textX = AndroidUtilities.dp((float)(var4 * 14 + 18));
            }

            ArticleViewer var5 = ArticleViewer.this;
            TLRPC.RichText var6 = this.currentBlock.text;
            var4 = AndroidUtilities.dp(18.0F);
            int var7 = this.textX;
            TLRPC.TL_pageBlockFooter var8 = this.currentBlock;
            Alignment var9;
            if (ArticleViewer.this.isRtl) {
               var9 = Alignment.ALIGN_RIGHT;
            } else {
               var9 = Alignment.ALIGN_NORMAL;
            }

            this.textLayout = var5.createLayoutForText(this, (CharSequence)null, var6, var2 - var4 - var7, var8, var9, this.parentAdapter);
            ArticleViewer.DrawingText var10 = this.textLayout;
            if (var10 != null) {
               var4 = var10.getHeight();
               if (this.currentBlock.level > 0) {
                  var1 = AndroidUtilities.dp(8.0F);
               } else {
                  var1 = AndroidUtilities.dp(16.0F);
               }

               var1 += var4;
            }
         } else {
            var1 = 1;
         }

         this.setMeasuredDimension(var2, var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockFooter var1) {
         this.currentBlock = var1;
         this.requestLayout();
      }
   }

   private class BlockHeaderCell extends View {
      private TLRPC.TL_pageBlockHeader currentBlock;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ArticleViewer.DrawingText textLayout;
      private int textX = AndroidUtilities.dp(18.0F);
      private int textY = AndroidUtilities.dp(8.0F);

      public BlockHeaderCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

         }
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setEnabled(true);
         if (this.textLayout != null) {
            StringBuilder var2 = new StringBuilder();
            var2.append(this.textLayout.getText());
            var2.append(", ");
            var2.append(LocaleController.getString("AccDescrIVHeading", 2131558439));
            var1.setText(var2.toString());
         }
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         var2 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockHeader var3 = this.currentBlock;
         var1 = 0;
         if (var3 != null) {
            ArticleViewer var4 = ArticleViewer.this;
            TLRPC.RichText var5 = var3.text;
            int var6 = AndroidUtilities.dp(36.0F);
            TLRPC.TL_pageBlockHeader var7 = this.currentBlock;
            Alignment var8;
            if (ArticleViewer.this.isRtl) {
               var8 = Alignment.ALIGN_RIGHT;
            } else {
               var8 = Alignment.ALIGN_NORMAL;
            }

            this.textLayout = var4.createLayoutForText(this, (CharSequence)null, var5, var2 - var6, var7, var8, this.parentAdapter);
            if (this.textLayout != null) {
               var1 = 0 + AndroidUtilities.dp(16.0F) + this.textLayout.getHeight();
            }
         } else {
            var1 = 1;
         }

         this.setMeasuredDimension(var2, var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockHeader var1) {
         this.currentBlock = var1;
         this.requestLayout();
      }
   }

   private class BlockKickerCell extends View {
      private TLRPC.TL_pageBlockKicker currentBlock;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ArticleViewer.DrawingText textLayout;
      private int textX = AndroidUtilities.dp(18.0F);
      private int textY;

      public BlockKickerCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

         }
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         var2 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockKicker var3 = this.currentBlock;
         if (var3 != null) {
            ArticleViewer var4 = ArticleViewer.this;
            TLRPC.RichText var5 = var3.text;
            var1 = AndroidUtilities.dp(36.0F);
            TLRPC.TL_pageBlockKicker var6 = this.currentBlock;
            Alignment var7;
            if (ArticleViewer.this.isRtl) {
               var7 = Alignment.ALIGN_RIGHT;
            } else {
               var7 = Alignment.ALIGN_NORMAL;
            }

            this.textLayout = var4.createLayoutForText(this, (CharSequence)null, var5, var2 - var1, var6, var7, this.parentAdapter);
            ArticleViewer.DrawingText var8 = this.textLayout;
            var1 = 0;
            if (var8 != null) {
               var1 = 0 + AndroidUtilities.dp(16.0F) + this.textLayout.getHeight();
            }

            if (this.currentBlock.first) {
               var1 += AndroidUtilities.dp(8.0F);
               this.textY = AndroidUtilities.dp(16.0F);
            } else {
               this.textY = AndroidUtilities.dp(8.0F);
            }
         } else {
            var1 = 1;
         }

         this.setMeasuredDimension(var2, var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockKicker var1) {
         this.currentBlock = var1;
         this.requestLayout();
      }
   }

   private class BlockListItemCell extends ViewGroup {
      private RecyclerView.ViewHolder blockLayout;
      private int blockX;
      private int blockY;
      private ArticleViewer.TL_pageBlockListItem currentBlock;
      private int currentBlockType;
      private boolean drawDot;
      private int numOffsetY;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private boolean parentIsList;
      private ArticleViewer.DrawingText textLayout;
      private int textX;
      private int textY;
      private boolean verticalAlign;

      public BlockListItemCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
         this.setWillNotDraw(false);
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            int var2 = this.getMeasuredWidth();
            if (this.currentBlock.numLayout != null) {
               var1.save();
               boolean var3 = ArticleViewer.this.isRtl;
               byte var4 = 0;
               int var5 = 0;
               float var6;
               if (var3) {
                  var6 = (float)(var2 - AndroidUtilities.dp(15.0F) - this.currentBlock.parent.maxNumWidth - this.currentBlock.parent.level * AndroidUtilities.dp(12.0F));
                  int var8 = this.textY;
                  var2 = this.numOffsetY;
                  if (this.drawDot) {
                     var5 = AndroidUtilities.dp(1.0F);
                  }

                  var1.translate(var6, (float)(var8 + var2 - var5));
               } else {
                  var6 = (float)(AndroidUtilities.dp(15.0F) + this.currentBlock.parent.maxNumWidth - (int)Math.ceil((double)this.currentBlock.numLayout.getLineWidth(0)) + this.currentBlock.parent.level * AndroidUtilities.dp(12.0F));
                  var2 = this.textY;
                  int var7 = this.numOffsetY;
                  var5 = var4;
                  if (this.drawDot) {
                     var5 = AndroidUtilities.dp(1.0F);
                  }

                  var1.translate(var6, (float)(var2 + var7 - var5));
               }

               this.currentBlock.numLayout.draw(var1);
               var1.restore();
            }

            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

         }
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setEnabled(true);
         ArticleViewer.DrawingText var2 = this.textLayout;
         if (var2 != null) {
            var1.setText(var2.getText());
         }
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         RecyclerView.ViewHolder var6 = this.blockLayout;
         if (var6 != null) {
            View var7 = var6.itemView;
            var2 = this.blockX;
            var7.layout(var2, this.blockY, var7.getMeasuredWidth() + var2, this.blockY + this.blockLayout.itemView.getMeasuredHeight());
         }

      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         ArticleViewer.TL_pageBlockListItem var4 = this.currentBlock;
         var1 = 1;
         if (var4 != null) {
            this.textLayout = null;
            var1 = var4.index;
            byte var5 = 0;
            byte var6 = 0;
            byte var7 = 0;
            if (var1 == 0 && this.currentBlock.parent.level == 0) {
               var1 = AndroidUtilities.dp(10.0F);
            } else {
               var1 = 0;
            }

            this.textY = var1;
            this.numOffsetY = 0;
            if (this.currentBlock.parent.lastMaxNumCalcWidth != var3 || this.currentBlock.parent.lastFontSize != ArticleViewer.this.selectedFontSize) {
               this.currentBlock.parent.lastMaxNumCalcWidth = var3;
               this.currentBlock.parent.lastFontSize = ArticleViewer.this.selectedFontSize;
               this.currentBlock.parent.maxNumWidth = 0;
               var2 = this.currentBlock.parent.items.size();

               for(var1 = 0; var1 < var2; ++var1) {
                  var4 = (ArticleViewer.TL_pageBlockListItem)this.currentBlock.parent.items.get(var1);
                  if (var4.num != null) {
                     var4.numLayout = ArticleViewer.this.createLayoutForText(this, var4.num, (TLRPC.RichText)null, var3 - AndroidUtilities.dp(54.0F), this.currentBlock, this.parentAdapter);
                     this.currentBlock.parent.maxNumWidth = Math.max(this.currentBlock.parent.maxNumWidth, (int)Math.ceil((double)var4.numLayout.getLineWidth(0)));
                  }
               }

               this.currentBlock.parent.maxNumWidth = Math.max(this.currentBlock.parent.maxNumWidth, (int)Math.ceil((double)ArticleViewer.listTextNumPaint.measureText("00.")));
            }

            this.drawDot = this.currentBlock.parent.pageBlockList.ordered ^ true;
            boolean var8;
            if (!(this.getParent() instanceof ArticleViewer.BlockListItemCell) && !(this.getParent() instanceof ArticleViewer.BlockOrderedListItemCell)) {
               var8 = false;
            } else {
               var8 = true;
            }

            this.parentIsList = var8;
            if (ArticleViewer.this.isRtl) {
               this.textX = AndroidUtilities.dp(18.0F);
            } else {
               this.textX = AndroidUtilities.dp(24.0F) + this.currentBlock.parent.maxNumWidth + this.currentBlock.parent.level * AndroidUtilities.dp(12.0F);
            }

            var2 = var3 - AndroidUtilities.dp(18.0F) - this.textX;
            var1 = var2;
            if (ArticleViewer.this.isRtl) {
               var1 = var2 - (AndroidUtilities.dp(6.0F) + this.currentBlock.parent.maxNumWidth + this.currentBlock.parent.level * AndroidUtilities.dp(12.0F));
            }

            label159: {
               if (this.currentBlock.textItem != null) {
                  ArticleViewer var9 = ArticleViewer.this;
                  TLRPC.RichText var10 = this.currentBlock.textItem;
                  ArticleViewer.TL_pageBlockListItem var11 = this.currentBlock;
                  Alignment var12;
                  if (ArticleViewer.this.isRtl) {
                     var12 = Alignment.ALIGN_RIGHT;
                  } else {
                     var12 = Alignment.ALIGN_NORMAL;
                  }

                  this.textLayout = var9.createLayoutForText(this, (CharSequence)null, var10, var1, var11, var12, this.parentAdapter);
                  ArticleViewer.DrawingText var13 = this.textLayout;
                  var2 = var6;
                  if (var13 == null) {
                     break label159;
                  }

                  var2 = var6;
                  if (var13.getLineCount() <= 0) {
                     break label159;
                  }

                  if (this.currentBlock.numLayout != null && this.currentBlock.numLayout.getLineCount() > 0) {
                     var1 = this.textLayout.getLineAscent(0);
                     this.numOffsetY = this.currentBlock.numLayout.getLineAscent(0) + AndroidUtilities.dp(2.5F) - var1;
                  }

                  var1 = this.textLayout.getHeight() + AndroidUtilities.dp(8.0F);
                  var2 = var5;
               } else {
                  var2 = var6;
                  if (this.currentBlock.blockItem == null) {
                     break label159;
                  }

                  this.blockX = this.textX;
                  this.blockY = this.textY;
                  RecyclerView.ViewHolder var14 = this.blockLayout;
                  var2 = var7;
                  if (var14 != null) {
                     View var15 = var14.itemView;
                     if (var15 instanceof ArticleViewer.BlockParagraphCell) {
                        this.blockY -= AndroidUtilities.dp(8.0F);
                        if (!ArticleViewer.this.isRtl) {
                           this.blockX -= AndroidUtilities.dp(18.0F);
                        }

                        var2 = AndroidUtilities.dp(18.0F) + var1;
                        var1 = 0 - AndroidUtilities.dp(8.0F);
                     } else {
                        label152: {
                           label197: {
                              if (!(var15 instanceof ArticleViewer.BlockHeaderCell) && !(var15 instanceof ArticleViewer.BlockSubheaderCell) && !(var15 instanceof ArticleViewer.BlockTitleCell) && !(var15 instanceof ArticleViewer.BlockSubtitleCell)) {
                                 if (ArticleViewer.this.isListItemBlock(this.currentBlock.blockItem)) {
                                    this.blockX = 0;
                                    this.blockY = 0;
                                    this.textY = 0;
                                    if (this.currentBlock.index == 0 && this.currentBlock.parent.level == 0) {
                                       var1 = 0 - AndroidUtilities.dp(10.0F);
                                    } else {
                                       var1 = 0;
                                    }

                                    var1 -= AndroidUtilities.dp(8.0F);
                                    var2 = var3;
                                    break label152;
                                 }

                                 if (!(this.blockLayout.itemView instanceof ArticleViewer.BlockTableCell)) {
                                    var2 = var1;
                                    break label197;
                                 }

                                 this.blockX -= AndroidUtilities.dp(18.0F);
                                 var2 = AndroidUtilities.dp(36.0F);
                              } else {
                                 if (!ArticleViewer.this.isRtl) {
                                    this.blockX -= AndroidUtilities.dp(18.0F);
                                 }

                                 var2 = AndroidUtilities.dp(18.0F);
                              }

                              var2 += var1;
                           }

                           var1 = 0;
                        }
                     }

                     this.blockLayout.itemView.measure(MeasureSpec.makeMeasureSpec(var2, 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
                     if (this.blockLayout.itemView instanceof ArticleViewer.BlockParagraphCell && this.currentBlock.numLayout != null && this.currentBlock.numLayout.getLineCount() > 0) {
                        ArticleViewer.BlockParagraphCell var16 = (ArticleViewer.BlockParagraphCell)this.blockLayout.itemView;
                        if (var16.textLayout != null && var16.textLayout.getLineCount() > 0) {
                           var2 = var16.textLayout.getLineAscent(0);
                           this.numOffsetY = this.currentBlock.numLayout.getLineAscent(0) + AndroidUtilities.dp(2.5F) - var2;
                        }
                     }

                     if (this.currentBlock.blockItem instanceof TLRPC.TL_pageBlockDetails) {
                        this.verticalAlign = true;
                        this.blockY = 0;
                        var2 = var1;
                        if (this.currentBlock.index == 0) {
                           var2 = var1;
                           if (this.currentBlock.parent.level == 0) {
                              var2 = var1 - AndroidUtilities.dp(10.0F);
                           }
                        }

                        var2 -= AndroidUtilities.dp(8.0F);
                     } else {
                        var15 = this.blockLayout.itemView;
                        if (var15 instanceof ArticleViewer.BlockOrderedListItemCell) {
                           this.verticalAlign = ((ArticleViewer.BlockOrderedListItemCell)var15).verticalAlign;
                           var2 = var1;
                        } else {
                           var2 = var1;
                           if (var15 instanceof ArticleViewer.BlockListItemCell) {
                              this.verticalAlign = ((ArticleViewer.BlockListItemCell)var15).verticalAlign;
                              var2 = var1;
                           }
                        }
                     }

                     if (this.verticalAlign && this.currentBlock.numLayout != null) {
                        this.textY = (this.blockLayout.itemView.getMeasuredHeight() - this.currentBlock.numLayout.getHeight()) / 2 - AndroidUtilities.dp(4.0F);
                        this.drawDot = false;
                     }

                     var2 += this.blockLayout.itemView.getMeasuredHeight();
                  }

                  var1 = AndroidUtilities.dp(8.0F);
               }

               var2 += var1;
            }

            var1 = var2;
            if (this.currentBlock.parent.items.get(this.currentBlock.parent.items.size() - 1) == this.currentBlock) {
               var1 = var2 + AndroidUtilities.dp(8.0F);
            }

            var2 = var1;
            if (this.currentBlock.index == 0) {
               var2 = var1;
               if (this.currentBlock.parent.level == 0) {
                  var2 = var1 + AndroidUtilities.dp(10.0F);
               }
            }

            var1 = var2;
         }

         this.setMeasuredDimension(var3, var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         return ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) ? true : super.onTouchEvent(var1);
      }

      public void setBlock(ArticleViewer.TL_pageBlockListItem var1) {
         if (this.currentBlock != var1) {
            this.currentBlock = var1;
            RecyclerView.ViewHolder var2 = this.blockLayout;
            if (var2 != null) {
               this.removeView(var2.itemView);
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

   private class BlockMapCell extends FrameLayout {
      private ArticleViewer.DrawingText captionLayout;
      private ArticleViewer.DrawingText creditLayout;
      private int creditOffset;
      private TLRPC.TL_pageBlockMap currentBlock;
      private int currentMapProvider;
      private int currentType;
      private ImageReceiver imageView;
      private boolean isFirst;
      private boolean isLast;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private boolean photoPressed;
      private int textX;
      private int textY;

      public BlockMapCell(Context var2, ArticleViewer.WebpageAdapter var3, int var4) {
         super(var2);
         this.parentAdapter = var3;
         this.setWillNotDraw(false);
         this.imageView = new ImageReceiver(this);
         this.currentType = var4;
      }

      protected void onDraw(Canvas var1) {
         int var2 = this.currentType;
         if (this.currentBlock != null) {
            this.imageView.draw(var1);
            int var3;
            if (this.currentMapProvider == 2 && this.imageView.hasNotThumb()) {
               var2 = (int)((float)Theme.chat_redLocationIcon.getIntrinsicWidth() * 0.8F);
               var3 = (int)((float)Theme.chat_redLocationIcon.getIntrinsicHeight() * 0.8F);
               int var4 = this.imageView.getImageX() + (this.imageView.getImageWidth() - var2) / 2;
               int var5 = this.imageView.getImageY() + (this.imageView.getImageHeight() / 2 - var3);
               Theme.chat_redLocationIcon.setAlpha((int)(this.imageView.getCurrentAlpha() * 255.0F));
               Theme.chat_redLocationIcon.setBounds(var4, var5, var2 + var4, var3 + var5);
               Theme.chat_redLocationIcon.draw(var1);
            }

            this.textY = this.imageView.getImageY() + this.imageView.getImageHeight() + AndroidUtilities.dp(8.0F);
            if (this.captionLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.captionLayout.draw(var1);
               var1.restore();
            }

            if (this.creditLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)(this.textY + this.creditOffset));
               this.creditLayout.draw(var1);
               var1.restore();
            }

            if (this.currentBlock.level > 0) {
               float var6 = (float)AndroidUtilities.dp(18.0F);
               float var7 = (float)AndroidUtilities.dp(20.0F);
               var3 = this.getMeasuredHeight();
               if (this.currentBlock.bottom) {
                  var2 = AndroidUtilities.dp(6.0F);
               } else {
                  var2 = 0;
               }

               var1.drawRect(var6, 0.0F, var7, (float)(var3 - var2), ArticleViewer.quoteLinePaint);
            }

         }
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setEnabled(true);
         StringBuilder var2 = new StringBuilder(LocaleController.getString("Map", 2131559800));
         if (this.captionLayout != null) {
            var2.append(", ");
            var2.append(this.captionLayout.getText());
         }

         var1.setText(var2.toString());
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         var2 = MeasureSpec.getSize(var1);
         var1 = this.currentType;
         int var3 = 1;
         if (var1 == 1) {
            var1 = ((View)this.getParent()).getMeasuredWidth();
            var2 = ((View)this.getParent()).getMeasuredHeight();
         } else if (var1 == 2) {
            var1 = var2;
         } else {
            var1 = var2;
            var2 = 0;
         }

         TLRPC.TL_pageBlockMap var4 = this.currentBlock;
         if (var4 != null) {
            int var5;
            int var6;
            label75: {
               if (this.currentType == 0) {
                  var3 = var4.level;
                  if (var3 > 0) {
                     var3 = AndroidUtilities.dp((float)(var3 * 14)) + AndroidUtilities.dp(18.0F);
                     this.textX = var3;
                     var5 = var1 - (AndroidUtilities.dp(18.0F) + var3);
                     var6 = var5;
                     break label75;
                  }
               }

               this.textX = AndroidUtilities.dp(18.0F);
               var6 = var1 - AndroidUtilities.dp(36.0F);
               var5 = var1;
               var3 = 0;
            }

            float var7;
            int var9;
            if (this.currentType == 0) {
               var7 = (float)var5;
               var4 = this.currentBlock;
               int var8 = (int)(var7 / (float)var4.w * (float)var4.h);
               var9 = (int)((float)(Math.max(ArticleViewer.this.listView[0].getMeasuredWidth(), ArticleViewer.this.listView[0].getMeasuredHeight()) - AndroidUtilities.dp(56.0F)) * 0.9F);
               var2 = var8;
               if (var8 > var9) {
                  var7 = (float)var9;
                  var4 = this.currentBlock;
                  var5 = (int)(var7 / (float)var4.h * (float)var4.w);
                  var3 += (var1 - var3 - var5) / 2;
                  var2 = var9;
               }
            }

            ImageReceiver var19;
            label69: {
               var19 = this.imageView;
               if (!this.isFirst) {
                  var9 = this.currentType;
                  if (var9 != 1 && var9 != 2 && this.currentBlock.level <= 0) {
                     var9 = AndroidUtilities.dp(8.0F);
                     break label69;
                  }
               }

               var9 = 0;
            }

            var19.setImageCoords(var3, var9, var5, var2);
            var3 = ArticleViewer.this.currentAccount;
            TLRPC.GeoPoint var20 = this.currentBlock.geo;
            double var10 = var20.lat;
            double var12 = var20._long;
            float var14 = (float)var5;
            float var15 = AndroidUtilities.density;
            var5 = (int)(var14 / var15);
            var7 = (float)var2;
            String var21 = AndroidUtilities.formapMapUrl(var3, var10, var12, var5, (int)(var7 / var15), true, 15);
            TLRPC.GeoPoint var16 = this.currentBlock.geo;
            var15 = AndroidUtilities.density;
            WebFile var23 = WebFile.createWithGeoPoint(var16, (int)(var14 / var15), (int)(var7 / var15), 15, Math.min(2, (int)Math.ceil((double)var15)));
            this.currentMapProvider = MessagesController.getInstance(ArticleViewer.this.currentAccount).mapProvider;
            if (this.currentMapProvider == 2) {
               if (var23 != null) {
                  this.imageView.setImage(ImageLocation.getForWebFile(var23), (String)null, Theme.chat_locationDrawable[0], (String)null, ArticleViewer.this.currentPage, 0);
               }
            } else if (var21 != null) {
               this.imageView.setImage(var21, (String)null, Theme.chat_locationDrawable[0], (String)null, 0);
            }

            var3 = var2;
            if (this.currentType == 0) {
               ArticleViewer var24 = ArticleViewer.this;
               var4 = this.currentBlock;
               this.captionLayout = var24.createLayoutForText(this, (CharSequence)null, var4.caption.text, var6, var4, this.parentAdapter);
               var5 = var2;
               if (this.captionLayout != null) {
                  this.creditOffset = AndroidUtilities.dp(4.0F) + this.captionLayout.getHeight();
                  var5 = var2 + this.creditOffset + AndroidUtilities.dp(4.0F);
               }

               var24 = ArticleViewer.this;
               TLRPC.TL_pageBlockMap var17 = this.currentBlock;
               TLRPC.RichText var18 = var17.caption.credit;
               Alignment var22;
               if (var24.isRtl) {
                  var22 = Alignment.ALIGN_RIGHT;
               } else {
                  var22 = Alignment.ALIGN_NORMAL;
               }

               this.creditLayout = var24.createLayoutForText(this, (CharSequence)null, var18, var6, var17, var22, this.parentAdapter);
               var3 = var5;
               if (this.creditLayout != null) {
                  var3 = var5 + AndroidUtilities.dp(4.0F) + this.creditLayout.getHeight();
               }
            }

            var2 = var3;
            if (!this.isFirst) {
               var2 = var3;
               if (this.currentType == 0) {
                  var2 = var3;
                  if (this.currentBlock.level <= 0) {
                     var2 = var3 + AndroidUtilities.dp(8.0F);
                  }
               }
            }

            var3 = var2;
            if (this.currentType != 2) {
               var3 = var2 + AndroidUtilities.dp(8.0F);
            }
         }

         this.setMeasuredDimension(var1, var3);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         float var2 = var1.getX();
         float var3 = var1.getY();
         int var4 = var1.getAction();
         boolean var5 = false;
         if (var4 == 0 && this.imageView.isInsideImage(var2, var3)) {
            this.photoPressed = true;
         } else if (var1.getAction() == 1 && this.photoPressed) {
            this.photoPressed = false;

            try {
               double var6 = this.currentBlock.geo.lat;
               double var8 = this.currentBlock.geo._long;
               Activity var10 = ArticleViewer.this.parentActivity;
               StringBuilder var12 = new StringBuilder();
               var12.append("geo:");
               var12.append(var6);
               var12.append(",");
               var12.append(var8);
               var12.append("?q=");
               var12.append(var6);
               var12.append(",");
               var12.append(var8);
               Intent var11 = new Intent("android.intent.action.VIEW", Uri.parse(var12.toString()));
               var10.startActivity(var11);
            } catch (Exception var13) {
               FileLog.e((Throwable)var13);
            }
         } else if (var1.getAction() == 3) {
            this.photoPressed = false;
         }

         if (this.photoPressed || ArticleViewer.this.checkLayoutForLinks(var1, this, this.captionLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(var1, this, this.creditLayout, this.textX, this.textY + this.creditOffset) || super.onTouchEvent(var1)) {
            var5 = true;
         }

         return var5;
      }

      public void setBlock(TLRPC.TL_pageBlockMap var1, boolean var2, boolean var3) {
         this.currentBlock = var1;
         this.isFirst = var2;
         this.isLast = var3;
         this.requestLayout();
      }
   }

   private class BlockOrderedListItemCell extends ViewGroup {
      private RecyclerView.ViewHolder blockLayout;
      private int blockX;
      private int blockY;
      private ArticleViewer.TL_pageBlockOrderedListItem currentBlock;
      private int currentBlockType;
      private int numOffsetY;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private boolean parentIsList;
      private ArticleViewer.DrawingText textLayout;
      private int textX;
      private int textY;
      private boolean verticalAlign;

      public BlockOrderedListItemCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
         this.setWillNotDraw(false);
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            int var2 = this.getMeasuredWidth();
            if (this.currentBlock.numLayout != null) {
               var1.save();
               if (ArticleViewer.this.isRtl) {
                  var1.translate((float)(var2 - AndroidUtilities.dp(18.0F) - this.currentBlock.parent.maxNumWidth - this.currentBlock.parent.level * AndroidUtilities.dp(20.0F)), (float)(this.textY + this.numOffsetY));
               } else {
                  var1.translate((float)(AndroidUtilities.dp(18.0F) + this.currentBlock.parent.maxNumWidth - (int)Math.ceil((double)this.currentBlock.numLayout.getLineWidth(0)) + this.currentBlock.parent.level * AndroidUtilities.dp(20.0F)), (float)(this.textY + this.numOffsetY));
               }

               this.currentBlock.numLayout.draw(var1);
               var1.restore();
            }

            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

         }
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setEnabled(true);
         ArticleViewer.DrawingText var2 = this.textLayout;
         if (var2 != null) {
            var1.setText(var2.getText());
         }
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         RecyclerView.ViewHolder var6 = this.blockLayout;
         if (var6 != null) {
            View var7 = var6.itemView;
            var2 = this.blockX;
            var7.layout(var2, this.blockY, var7.getMeasuredWidth() + var2, this.blockY + this.blockLayout.itemView.getMeasuredHeight());
         }

      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         ArticleViewer.TL_pageBlockOrderedListItem var4 = this.currentBlock;
         var2 = 1;
         if (var4 != null) {
            this.textLayout = null;
            var1 = var4.index;
            byte var5 = 0;
            byte var6 = 0;
            byte var7 = 0;
            if (var1 == 0 && this.currentBlock.parent.level == 0) {
               var1 = AndroidUtilities.dp(10.0F);
            } else {
               var1 = 0;
            }

            this.textY = var1;
            this.numOffsetY = 0;
            if (this.currentBlock.parent.lastMaxNumCalcWidth != var3 || this.currentBlock.parent.lastFontSize != ArticleViewer.this.selectedFontSize) {
               this.currentBlock.parent.lastMaxNumCalcWidth = var3;
               this.currentBlock.parent.lastFontSize = ArticleViewer.this.selectedFontSize;
               this.currentBlock.parent.maxNumWidth = 0;
               var2 = this.currentBlock.parent.items.size();

               for(var1 = 0; var1 < var2; ++var1) {
                  var4 = (ArticleViewer.TL_pageBlockOrderedListItem)this.currentBlock.parent.items.get(var1);
                  if (var4.num != null) {
                     var4.numLayout = ArticleViewer.this.createLayoutForText(this, var4.num, (TLRPC.RichText)null, var3 - AndroidUtilities.dp(54.0F), this.currentBlock, this.parentAdapter);
                     this.currentBlock.parent.maxNumWidth = Math.max(this.currentBlock.parent.maxNumWidth, (int)Math.ceil((double)var4.numLayout.getLineWidth(0)));
                  }
               }

               this.currentBlock.parent.maxNumWidth = Math.max(this.currentBlock.parent.maxNumWidth, (int)Math.ceil((double)ArticleViewer.listTextNumPaint.measureText("00.")));
            }

            if (ArticleViewer.this.isRtl) {
               this.textX = AndroidUtilities.dp(18.0F);
            } else {
               this.textX = AndroidUtilities.dp(24.0F) + this.currentBlock.parent.maxNumWidth + this.currentBlock.parent.level * AndroidUtilities.dp(20.0F);
            }

            this.verticalAlign = false;
            var2 = var3 - AndroidUtilities.dp(18.0F) - this.textX;
            var1 = var2;
            if (ArticleViewer.this.isRtl) {
               var1 = var2 - (AndroidUtilities.dp(6.0F) + this.currentBlock.parent.maxNumWidth + this.currentBlock.parent.level * AndroidUtilities.dp(20.0F));
            }

            label137: {
               if (this.currentBlock.textItem != null) {
                  ArticleViewer var8 = ArticleViewer.this;
                  TLRPC.RichText var9 = this.currentBlock.textItem;
                  ArticleViewer.TL_pageBlockOrderedListItem var10 = this.currentBlock;
                  Alignment var11;
                  if (ArticleViewer.this.isRtl) {
                     var11 = Alignment.ALIGN_RIGHT;
                  } else {
                     var11 = Alignment.ALIGN_NORMAL;
                  }

                  this.textLayout = var8.createLayoutForText(this, (CharSequence)null, var9, var1, var10, var11, this.parentAdapter);
                  ArticleViewer.DrawingText var12 = this.textLayout;
                  var2 = var6;
                  if (var12 == null) {
                     break label137;
                  }

                  var2 = var6;
                  if (var12.getLineCount() <= 0) {
                     break label137;
                  }

                  if (this.currentBlock.numLayout != null && this.currentBlock.numLayout.getLineCount() > 0) {
                     var1 = this.textLayout.getLineAscent(0);
                     this.numOffsetY = this.currentBlock.numLayout.getLineAscent(0) - var1;
                  }

                  var1 = this.textLayout.getHeight() + AndroidUtilities.dp(8.0F);
                  var2 = var5;
               } else {
                  var2 = var6;
                  if (this.currentBlock.blockItem == null) {
                     break label137;
                  }

                  this.blockX = this.textX;
                  this.blockY = this.textY;
                  RecyclerView.ViewHolder var13 = this.blockLayout;
                  var2 = var7;
                  if (var13 != null) {
                     View var14 = var13.itemView;
                     if (var14 instanceof ArticleViewer.BlockParagraphCell) {
                        this.blockY -= AndroidUtilities.dp(8.0F);
                        if (!ArticleViewer.this.isRtl) {
                           this.blockX -= AndroidUtilities.dp(18.0F);
                        }

                        var2 = AndroidUtilities.dp(18.0F) + var1;
                        var1 = 0 - AndroidUtilities.dp(8.0F);
                     } else {
                        label130: {
                           label170: {
                              if (!(var14 instanceof ArticleViewer.BlockHeaderCell) && !(var14 instanceof ArticleViewer.BlockSubheaderCell) && !(var14 instanceof ArticleViewer.BlockTitleCell) && !(var14 instanceof ArticleViewer.BlockSubtitleCell)) {
                                 if (ArticleViewer.this.isListItemBlock(this.currentBlock.blockItem)) {
                                    this.blockX = 0;
                                    this.blockY = 0;
                                    this.textY = 0;
                                    var1 = 0 - AndroidUtilities.dp(8.0F);
                                    var2 = var3;
                                    break label130;
                                 }

                                 if (!(this.blockLayout.itemView instanceof ArticleViewer.BlockTableCell)) {
                                    var2 = var1;
                                    break label170;
                                 }

                                 this.blockX -= AndroidUtilities.dp(18.0F);
                                 var2 = AndroidUtilities.dp(36.0F);
                              } else {
                                 if (!ArticleViewer.this.isRtl) {
                                    this.blockX -= AndroidUtilities.dp(18.0F);
                                 }

                                 var2 = AndroidUtilities.dp(18.0F);
                              }

                              var2 += var1;
                           }

                           var1 = 0;
                        }
                     }

                     this.blockLayout.itemView.measure(MeasureSpec.makeMeasureSpec(var2, 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
                     if (this.blockLayout.itemView instanceof ArticleViewer.BlockParagraphCell && this.currentBlock.numLayout != null && this.currentBlock.numLayout.getLineCount() > 0) {
                        ArticleViewer.BlockParagraphCell var15 = (ArticleViewer.BlockParagraphCell)this.blockLayout.itemView;
                        if (var15.textLayout != null && var15.textLayout.getLineCount() > 0) {
                           var2 = var15.textLayout.getLineAscent(0);
                           this.numOffsetY = this.currentBlock.numLayout.getLineAscent(0) - var2;
                        }
                     }

                     if (this.currentBlock.blockItem instanceof TLRPC.TL_pageBlockDetails) {
                        this.verticalAlign = true;
                        this.blockY = 0;
                        var2 = var1 - AndroidUtilities.dp(8.0F);
                     } else {
                        var14 = this.blockLayout.itemView;
                        if (var14 instanceof ArticleViewer.BlockOrderedListItemCell) {
                           this.verticalAlign = ((ArticleViewer.BlockOrderedListItemCell)var14).verticalAlign;
                           var2 = var1;
                        } else {
                           var2 = var1;
                           if (var14 instanceof ArticleViewer.BlockListItemCell) {
                              this.verticalAlign = ((ArticleViewer.BlockListItemCell)var14).verticalAlign;
                              var2 = var1;
                           }
                        }
                     }

                     if (this.verticalAlign && this.currentBlock.numLayout != null) {
                        this.textY = (this.blockLayout.itemView.getMeasuredHeight() - this.currentBlock.numLayout.getHeight()) / 2;
                     }

                     var2 += this.blockLayout.itemView.getMeasuredHeight();
                  }

                  var1 = AndroidUtilities.dp(8.0F);
               }

               var2 += var1;
            }

            var1 = var2;
            if (this.currentBlock.parent.items.get(this.currentBlock.parent.items.size() - 1) == this.currentBlock) {
               var1 = var2 + AndroidUtilities.dp(8.0F);
            }

            var2 = var1;
            if (this.currentBlock.index == 0) {
               var2 = var1;
               if (this.currentBlock.parent.level == 0) {
                  var2 = var1 + AndroidUtilities.dp(10.0F);
               }
            }
         }

         this.setMeasuredDimension(var3, var2);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         return ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) ? true : super.onTouchEvent(var1);
      }

      public void setBlock(ArticleViewer.TL_pageBlockOrderedListItem var1) {
         if (this.currentBlock != var1) {
            this.currentBlock = var1;
            RecyclerView.ViewHolder var2 = this.blockLayout;
            if (var2 != null) {
               this.removeView(var2.itemView);
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

   private class BlockParagraphCell extends View {
      private TLRPC.TL_pageBlockParagraph currentBlock;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ArticleViewer.DrawingText textLayout;
      private int textX;
      private int textY;

      public BlockParagraphCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

            if (this.currentBlock.level > 0) {
               float var2 = (float)AndroidUtilities.dp(18.0F);
               float var3 = (float)AndroidUtilities.dp(20.0F);
               int var4 = this.getMeasuredHeight();
               int var5;
               if (this.currentBlock.bottom) {
                  var5 = AndroidUtilities.dp(6.0F);
               } else {
                  var5 = 0;
               }

               var1.drawRect(var2, 0.0F, var3, (float)(var4 - var5), ArticleViewer.quoteLinePaint);
            }

         }
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setEnabled(true);
         ArticleViewer.DrawingText var2 = this.textLayout;
         if (var2 != null) {
            var1.setText(var2.getText());
         }
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         var2 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockParagraph var3 = this.currentBlock;
         var1 = 0;
         if (var3 != null) {
            int var4 = var3.level;
            if (var4 == 0) {
               this.textY = AndroidUtilities.dp(8.0F);
               this.textX = AndroidUtilities.dp(18.0F);
            } else {
               this.textY = 0;
               this.textX = AndroidUtilities.dp((float)(var4 * 14 + 18));
            }

            ArticleViewer var5 = ArticleViewer.this;
            TLRPC.RichText var6 = this.currentBlock.text;
            int var7 = AndroidUtilities.dp(18.0F);
            var4 = this.textX;
            int var8 = this.textY;
            TLRPC.TL_pageBlockParagraph var9 = this.currentBlock;
            Alignment var10;
            if (ArticleViewer.this.isRtl) {
               var10 = Alignment.ALIGN_RIGHT;
            } else {
               var10 = Alignment.ALIGN_NORMAL;
            }

            this.textLayout = var5.createLayoutForText(this, (CharSequence)null, var6, var2 - var7 - var4, var8, var9, var10, 0, this.parentAdapter);
            ArticleViewer.DrawingText var11 = this.textLayout;
            if (var11 != null) {
               var4 = var11.getHeight();
               if (this.currentBlock.level > 0) {
                  var1 = AndroidUtilities.dp(8.0F);
               } else {
                  var1 = AndroidUtilities.dp(16.0F);
               }

               var1 += var4;
            }
         } else {
            var1 = 1;
         }

         this.setMeasuredDimension(var2, var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockParagraph var1) {
         this.currentBlock = var1;
         this.requestLayout();
      }
   }

   private class BlockPhotoCell extends FrameLayout implements DownloadController.FileDownloadProgressListener {
      private int TAG;
      boolean autoDownload;
      private int buttonPressed;
      private int buttonState;
      private int buttonX;
      private int buttonY;
      private boolean cancelLoading;
      private ArticleViewer.DrawingText captionLayout;
      private ArticleViewer.BlockChannelCell channelCell;
      private ArticleViewer.DrawingText creditLayout;
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
      private ArticleViewer.WebpageAdapter parentAdapter;
      private TLRPC.PageBlock parentBlock;
      private boolean photoPressed;
      private RadialProgress2 radialProgress;
      private int textX;
      private int textY;

      public BlockPhotoCell(Context var2, ArticleViewer.WebpageAdapter var3, int var4) {
         super(var2);
         this.parentAdapter = var3;
         this.setWillNotDraw(false);
         this.imageView = new ImageReceiver(this);
         this.channelCell = ArticleViewer.this.new BlockChannelCell(var2, this.parentAdapter, 1);
         this.radialProgress = new RadialProgress2(this);
         this.radialProgress.setProgressColor(-1);
         this.radialProgress.setColors(1711276032, 2130706432, -1, -2500135);
         this.TAG = DownloadController.getInstance(ArticleViewer.this.currentAccount).generateObserverTag();
         this.addView(this.channelCell, LayoutHelper.createFrame(-1, -2.0F));
         this.currentType = var4;
      }

      private void didPressedButton(boolean var1) {
         int var2 = this.buttonState;
         if (var2 == 0) {
            this.cancelLoading = false;
            this.radialProgress.setProgress(0.0F, var1);
            this.imageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.currentPhoto), this.currentFilter, ImageLocation.getForPhoto(this.currentPhotoObjectThumb, this.currentPhoto), this.currentThumbFilter, this.currentPhotoObject.size, (String)null, ArticleViewer.this.currentPage, 1);
            this.buttonState = 1;
            this.radialProgress.setIcon(this.getIconForCurrentState(), true, var1);
            this.invalidate();
         } else if (var2 == 1) {
            this.cancelLoading = true;
            this.imageView.cancelLoadImage();
            this.buttonState = 0;
            this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
            this.invalidate();
         }

      }

      private int getIconForCurrentState() {
         int var1 = this.buttonState;
         if (var1 == 0) {
            return 2;
         } else {
            return var1 == 1 ? 3 : 4;
         }
      }

      public View getChannelCell() {
         return this.channelCell;
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
         DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver(this);
      }

      protected void onDraw(Canvas var1) {
         int var2 = this.currentType;
         if (this.currentBlock != null) {
            if (!this.imageView.hasBitmapImage() || this.imageView.getCurrentAlpha() != 1.0F) {
               var1.drawRect((float)this.imageView.getImageX(), (float)this.imageView.getImageY(), (float)this.imageView.getImageX2(), (float)this.imageView.getImageY2(), ArticleViewer.photoBackgroundPaint);
            }

            this.imageView.draw(var1);
            if (this.imageView.getVisible()) {
               this.radialProgress.draw(var1);
            }

            int var3;
            if (!TextUtils.isEmpty(this.currentBlock.url)) {
               var2 = this.getMeasuredWidth() - AndroidUtilities.dp(35.0F);
               var3 = this.imageView.getImageY() + AndroidUtilities.dp(11.0F);
               this.linkDrawable.setBounds(var2, var3, AndroidUtilities.dp(24.0F) + var2, AndroidUtilities.dp(24.0F) + var3);
               this.linkDrawable.draw(var1);
            }

            this.textY = this.imageView.getImageY() + this.imageView.getImageHeight() + AndroidUtilities.dp(8.0F);
            if (this.captionLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.captionLayout.draw(var1);
               var1.restore();
            }

            if (this.creditLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)(this.textY + this.creditOffset));
               this.creditLayout.draw(var1);
               var1.restore();
            }

            if (this.currentBlock.level > 0) {
               float var4 = (float)AndroidUtilities.dp(18.0F);
               float var5 = (float)AndroidUtilities.dp(20.0F);
               var3 = this.getMeasuredHeight();
               if (this.currentBlock.bottom) {
                  var2 = AndroidUtilities.dp(6.0F);
               } else {
                  var2 = 0;
               }

               var1.drawRect(var4, 0.0F, var5, (float)(var3 - var2), ArticleViewer.quoteLinePaint);
            }

         }
      }

      public void onFailedDownload(String var1, boolean var2) {
         this.updateButtonState(false);
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setEnabled(true);
         StringBuilder var2 = new StringBuilder(LocaleController.getString("AttachPhoto", 2131558727));
         if (this.captionLayout != null) {
            var2.append(", ");
            var2.append(this.captionLayout.getText());
         }

         var1.setText(var2.toString());
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         int var3;
         int var4;
         boolean var5;
         byte var6;
         int var7;
         float var8;
         label142: {
            var3 = MeasureSpec.getSize(var1);
            var4 = this.currentType;
            var5 = false;
            var6 = 1;
            if (var4 == 1) {
               var3 = ((View)this.getParent()).getMeasuredWidth();
               var4 = ((View)this.getParent()).getMeasuredHeight();
            } else {
               if (var4 != 2) {
                  var4 = 0;
                  var7 = var3;
                  break label142;
               }

               var8 = this.groupPosition.ph;
               Point var9 = AndroidUtilities.displaySize;
               var4 = (int)Math.ceil((double)(var8 * (float)Math.max(var9.x, var9.y) * 0.5F));
            }

            var7 = var3;
         }

         TLRPC.TL_pageBlockPhoto var21 = this.currentBlock;
         var3 = var6;
         if (var21 != null) {
            int var10;
            int var11;
            int var12;
            int var20;
            label134: {
               this.currentPhoto = ArticleViewer.this.getPhotoWithId(var21.photo_id);
               var10 = AndroidUtilities.dp(48.0F);
               if (this.currentType == 0) {
                  var3 = this.currentBlock.level;
                  if (var3 > 0) {
                     var20 = AndroidUtilities.dp((float)(var3 * 14)) + AndroidUtilities.dp(18.0F);
                     this.textX = var20;
                     var11 = var7 - (AndroidUtilities.dp(18.0F) + var20);
                     var12 = var11;
                     break label134;
                  }
               }

               this.textX = AndroidUtilities.dp(18.0F);
               var12 = var7 - AndroidUtilities.dp(36.0F);
               var11 = var7;
               var20 = 0;
            }

            TLRPC.Photo var22 = this.currentPhoto;
            if (var22 != null && this.currentPhotoObject != null) {
               this.currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(var22.sizes, 40, true);
               if (this.currentPhotoObject == this.currentPhotoObjectThumb) {
                  this.currentPhotoObjectThumb = null;
               }

               int var14;
               int var15;
               label124: {
                  int var13 = this.currentType;
                  if (var13 == 0) {
                     var8 = (float)var11;
                     TLRPC.PhotoSize var23 = this.currentPhotoObject;
                     var13 = (int)(var8 / (float)var23.w * (float)var23.h);
                     if (this.parentBlock instanceof TLRPC.TL_pageBlockCover) {
                        var3 = Math.min(var13, var11);
                        var14 = var20;
                        var15 = var11;
                     } else {
                        var4 = (int)((float)(Math.max(ArticleViewer.this.listView[0].getMeasuredWidth(), ArticleViewer.this.listView[0].getMeasuredHeight()) - AndroidUtilities.dp(56.0F)) * 0.9F);
                        var3 = var13;
                        var14 = var20;
                        var15 = var11;
                        if (var13 > var4) {
                           var8 = (float)var4;
                           var23 = this.currentPhotoObject;
                           var15 = (int)(var8 / (float)var23.h * (float)var23.w);
                           var14 = var20 + (var7 - var20 - var15) / 2;
                           var3 = var4;
                        }
                     }
                  } else {
                     var3 = var4;
                     var14 = var20;
                     var15 = var11;
                     if (var13 == 2) {
                        var14 = var11;
                        if ((this.groupPosition.flags & 2) == 0) {
                           var14 = var11 - AndroidUtilities.dp(2.0F);
                        }

                        if ((this.groupPosition.flags & 8) == 0) {
                           var11 = var4 - AndroidUtilities.dp(2.0F);
                        } else {
                           var11 = var4;
                        }

                        var13 = this.groupPosition.leftSpanOffset;
                        var15 = var20;
                        var3 = var14;
                        if (var13 != 0) {
                           var15 = (int)Math.ceil((double)((float)(var13 * var7) / 1000.0F));
                           var3 = var14 - var15;
                           var15 += var20;
                        }

                        var20 = var11;
                        var14 = var15;
                        var15 = var3;
                        break label124;
                     }
                  }

                  var4 = var3;
                  var20 = var3;
               }

               ImageReceiver var25;
               label119: {
                  var25 = this.imageView;
                  if (!this.isFirst) {
                     var3 = this.currentType;
                     if (var3 != 1 && var3 != 2 && this.currentBlock.level <= 0) {
                        var3 = AndroidUtilities.dp(8.0F);
                        break label119;
                     }
                  }

                  var3 = 0;
               }

               var25.setImageCoords(var14, var3, var15, var20);
               if (this.currentType == 0) {
                  this.currentFilter = null;
               } else {
                  this.currentFilter = String.format(Locale.US, "%d_%d", var15, var20);
               }

               this.currentThumbFilter = "80_80_b";
               boolean var16;
               if ((DownloadController.getInstance(ArticleViewer.this.currentAccount).getCurrentDownloadMask() & 1) != 0) {
                  var16 = true;
               } else {
                  var16 = false;
               }

               this.autoDownload = var16;
               File var26 = FileLoader.getPathToAttach(this.currentPhotoObject, true);
               if (!this.autoDownload && !var26.exists()) {
                  this.imageView.setStrippedLocation(ImageLocation.getForPhoto(this.currentPhotoObject, this.currentPhoto));
                  this.imageView.setImage((ImageLocation)null, this.currentFilter, ImageLocation.getForPhoto(this.currentPhotoObjectThumb, this.currentPhoto), this.currentThumbFilter, this.currentPhotoObject.size, (String)null, ArticleViewer.this.currentPage, 1);
               } else {
                  this.imageView.setStrippedLocation((ImageLocation)null);
                  this.imageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.currentPhoto), this.currentFilter, ImageLocation.getForPhoto(this.currentPhotoObjectThumb, this.currentPhoto), this.currentThumbFilter, this.currentPhotoObject.size, (String)null, ArticleViewer.this.currentPage, 1);
               }

               this.buttonX = (int)((float)this.imageView.getImageX() + (float)(this.imageView.getImageWidth() - var10) / 2.0F);
               this.buttonY = (int)((float)this.imageView.getImageY() + (float)(this.imageView.getImageHeight() - var10) / 2.0F);
               RadialProgress2 var27 = this.radialProgress;
               var3 = this.buttonX;
               var20 = this.buttonY;
               var27.setProgressRect(var3, var20, var3 + var10, var10 + var20);
               var3 = var4;
            } else {
               var3 = var4;
            }

            var4 = var3;
            if (this.currentType == 0) {
               ArticleViewer var28 = ArticleViewer.this;
               TLRPC.TL_pageBlockPhoto var17 = this.currentBlock;
               this.captionLayout = var28.createLayoutForText(this, (CharSequence)null, var17.caption.text, var12, var17, this.parentAdapter);
               var20 = var3;
               if (this.captionLayout != null) {
                  this.creditOffset = AndroidUtilities.dp(4.0F) + this.captionLayout.getHeight();
                  var20 = var3 + this.creditOffset + AndroidUtilities.dp(4.0F);
               }

               ArticleViewer var18 = ArticleViewer.this;
               var17 = this.currentBlock;
               TLRPC.RichText var19 = var17.caption.credit;
               Alignment var29;
               if (var18.isRtl) {
                  var29 = Alignment.ALIGN_RIGHT;
               } else {
                  var29 = Alignment.ALIGN_NORMAL;
               }

               this.creditLayout = var18.createLayoutForText(this, (CharSequence)null, var19, var12, var17, var29, this.parentAdapter);
               var4 = var20;
               if (this.creditLayout != null) {
                  var4 = var20 + AndroidUtilities.dp(4.0F) + this.creditLayout.getHeight();
               }
            }

            var3 = var4;
            if (!this.isFirst) {
               var3 = var4;
               if (this.currentType == 0) {
                  var3 = var4;
                  if (this.currentBlock.level <= 0) {
                     var3 = var4 + AndroidUtilities.dp(8.0F);
                  }
               }
            }

            boolean var24 = var5;
            if (this.parentBlock instanceof TLRPC.TL_pageBlockCover) {
               var24 = var5;
               if (this.parentAdapter.blocks != null) {
                  var24 = var5;
                  if (this.parentAdapter.blocks.size() > 1) {
                     var24 = var5;
                     if (this.parentAdapter.blocks.get(1) instanceof TLRPC.TL_pageBlockChannel) {
                        var24 = true;
                     }
                  }
               }
            }

            var4 = var3;
            if (this.currentType != 2) {
               var4 = var3;
               if (!var24) {
                  var4 = var3 + AndroidUtilities.dp(8.0F);
               }
            }

            var3 = var4;
         }

         this.channelCell.measure(var1, var2);
         this.channelCell.setTranslationY((float)(this.imageView.getImageHeight() - AndroidUtilities.dp(39.0F)));
         this.setMeasuredDimension(var7, var3);
      }

      public void onProgressDownload(String var1, float var2) {
         this.radialProgress.setProgress(var2, true);
         if (this.buttonState != 1) {
            this.updateButtonState(true);
         }

      }

      public void onProgressUpload(String var1, float var2, boolean var3) {
      }

      public void onSuccessDownload(String var1) {
         this.radialProgress.setProgress(1.0F, true);
         this.updateButtonState(true);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         float var2 = var1.getX();
         float var3 = var1.getY();
         int var4 = this.channelCell.getVisibility();
         boolean var5 = false;
         if (var4 == 0 && var3 > this.channelCell.getTranslationY() && var3 < this.channelCell.getTranslationY() + (float)AndroidUtilities.dp(39.0F)) {
            if (ArticleViewer.this.channelBlock != null && var1.getAction() == 1) {
               MessagesController.getInstance(ArticleViewer.this.currentAccount).openByUserName(ArticleViewer.this.channelBlock.channel.username, ArticleViewer.this.parentFragment, 2);
               ArticleViewer.this.close(false, true);
            }

            return true;
         } else {
            if (var1.getAction() == 0 && this.imageView.isInsideImage(var2, var3)) {
               label62: {
                  label61: {
                     if (this.buttonState != -1) {
                        var4 = this.buttonX;
                        if (var2 >= (float)var4 && var2 <= (float)(var4 + AndroidUtilities.dp(48.0F))) {
                           var4 = this.buttonY;
                           if (var3 >= (float)var4 && var3 <= (float)(var4 + AndroidUtilities.dp(48.0F))) {
                              break label61;
                           }
                        }
                     }

                     if (this.buttonState != 0) {
                        this.photoPressed = true;
                        break label62;
                     }
                  }

                  this.buttonPressed = 1;
                  this.invalidate();
               }
            } else if (var1.getAction() == 1) {
               if (this.photoPressed) {
                  this.photoPressed = false;
                  ArticleViewer.this.openPhoto(this.currentBlock);
               } else if (this.buttonPressed == 1) {
                  this.buttonPressed = 0;
                  this.playSoundEffect(0);
                  this.didPressedButton(true);
                  this.invalidate();
               }
            } else if (var1.getAction() == 3) {
               this.photoPressed = false;
               this.buttonPressed = 0;
            }

            if (this.photoPressed || this.buttonPressed != 0 || ArticleViewer.this.checkLayoutForLinks(var1, this, this.captionLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(var1, this, this.creditLayout, this.textX, this.textY + this.creditOffset) || super.onTouchEvent(var1)) {
               var5 = true;
            }

            return var5;
         }
      }

      public void setBlock(TLRPC.TL_pageBlockPhoto var1, boolean var2, boolean var3) {
         this.parentBlock = null;
         this.currentBlock = var1;
         this.isFirst = var2;
         this.isLast = var3;
         this.channelCell.setVisibility(4);
         if (!TextUtils.isEmpty(this.currentBlock.url)) {
            this.linkDrawable = this.getResources().getDrawable(2131165496);
         }

         var1 = this.currentBlock;
         if (var1 != null) {
            TLRPC.Photo var4 = ArticleViewer.this.getPhotoWithId(var1.photo_id);
            if (var4 != null) {
               this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(var4.sizes, AndroidUtilities.getPhotoSize());
            } else {
               this.currentPhotoObject = null;
            }
         } else {
            this.currentPhotoObject = null;
         }

         this.updateButtonState(false);
         this.requestLayout();
      }

      public void setParentBlock(TLRPC.PageBlock var1) {
         this.parentBlock = var1;
         if (ArticleViewer.this.channelBlock != null && this.parentBlock instanceof TLRPC.TL_pageBlockCover) {
            this.channelCell.setBlock(ArticleViewer.this.channelBlock);
            this.channelCell.setVisibility(0);
         }

      }

      public void updateButtonState(boolean var1) {
         String var2 = FileLoader.getAttachFileName(this.currentPhotoObject);
         boolean var3 = FileLoader.getPathToAttach(this.currentPhotoObject, true).exists();
         if (TextUtils.isEmpty(var2)) {
            this.radialProgress.setIcon(4, false, false);
         } else {
            if (var3) {
               DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver(this);
               this.buttonState = -1;
               this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
               this.invalidate();
            } else {
               DownloadController.getInstance(ArticleViewer.this.currentAccount).addLoadingFileObserver(var2, (MessageObject)null, this);
               var3 = this.autoDownload;
               float var4 = 0.0F;
               if (!var3 && !FileLoader.getInstance(ArticleViewer.this.currentAccount).isLoadingFile(var2)) {
                  this.buttonState = 0;
               } else {
                  this.buttonState = 1;
                  Float var5 = ImageLoader.getInstance().getFileProgress(var2);
                  if (var5 != null) {
                     var4 = var5;
                  }
               }

               this.radialProgress.setIcon(this.getIconForCurrentState(), true, var1);
               this.radialProgress.setProgress(var4, false);
               this.invalidate();
            }

         }
      }
   }

   private class BlockPreformattedCell extends FrameLayout {
      private TLRPC.TL_pageBlockPreformatted currentBlock;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private HorizontalScrollView scrollView;
      private View textContainer;
      private ArticleViewer.DrawingText textLayout;

      public BlockPreformattedCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
         this.scrollView = new HorizontalScrollView(var2) {
            public boolean onInterceptTouchEvent(MotionEvent var1) {
               if (BlockPreformattedCell.this.textContainer.getMeasuredWidth() > this.getMeasuredWidth()) {
                  ArticleViewer.this.windowView.requestDisallowInterceptTouchEvent(true);
               }

               return super.onInterceptTouchEvent(var1);
            }

            protected void onScrollChanged(int var1, int var2, int var3, int var4) {
               super.onScrollChanged(var1, var2, var3, var4);
               if (ArticleViewer.this.pressedLinkOwnerLayout != null) {
                  ArticleViewer.this.pressedLinkOwnerLayout = null;
                  ArticleViewer.this.pressedLinkOwnerView = null;
               }

            }
         };
         this.scrollView.setPadding(0, AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(8.0F));
         this.addView(this.scrollView, LayoutHelper.createFrame(-1, -2.0F));
         this.textContainer = new View(var2) {
            protected void onDraw(Canvas var1) {
               if (BlockPreformattedCell.this.textLayout != null) {
                  var1.save();
                  BlockPreformattedCell.this.textLayout.draw(var1);
                  var1.restore();
               }

            }

            protected void onMeasure(int var1, int var2) {
               TLRPC.TL_pageBlockPreformatted var3 = BlockPreformattedCell.this.currentBlock;
               int var4 = 0;
               int var5 = 1;
               var1 = 1;
               if (var3 != null) {
                  ArticleViewer.BlockPreformattedCell var8 = BlockPreformattedCell.this;
                  var8.textLayout = ArticleViewer.this.createLayoutForText(this, (CharSequence)null, var8.currentBlock.text, AndroidUtilities.dp(5000.0F), BlockPreformattedCell.this.currentBlock, BlockPreformattedCell.this.parentAdapter);
                  if (BlockPreformattedCell.this.textLayout != null) {
                     int var6 = BlockPreformattedCell.this.textLayout.getHeight() + 0;
                     int var7 = BlockPreformattedCell.this.textLayout.getLineCount();

                     while(true) {
                        var5 = var1;
                        var2 = var6;
                        if (var4 >= var7) {
                           break;
                        }

                        var1 = Math.max((int)Math.ceil((double)BlockPreformattedCell.this.textLayout.getLineWidth(var4)), var1);
                        ++var4;
                     }
                  } else {
                     var2 = 0;
                  }
               } else {
                  var2 = 1;
               }

               this.setMeasuredDimension(var5 + AndroidUtilities.dp(32.0F), var2);
            }
         };
         android.widget.FrameLayout.LayoutParams var5 = new android.widget.FrameLayout.LayoutParams(-2, -1);
         int var4 = AndroidUtilities.dp(16.0F);
         var5.rightMargin = var4;
         var5.leftMargin = var4;
         var4 = AndroidUtilities.dp(12.0F);
         var5.bottomMargin = var4;
         var5.topMargin = var4;
         this.scrollView.addView(this.textContainer, var5);
         this.setWillNotDraw(false);
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            var1.drawRect(0.0F, (float)AndroidUtilities.dp(8.0F), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - AndroidUtilities.dp(8.0F)), ArticleViewer.preformattedBackgroundPaint);
         }
      }

      protected void onMeasure(int var1, int var2) {
         var1 = MeasureSpec.getSize(var1);
         this.scrollView.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
         this.setMeasuredDimension(var1, this.scrollView.getMeasuredHeight());
      }

      public void setBlock(TLRPC.TL_pageBlockPreformatted var1) {
         this.currentBlock = var1;
         this.scrollView.setScrollX(0);
         this.textContainer.requestLayout();
      }
   }

   private class BlockPullquoteCell extends View {
      private TLRPC.TL_pageBlockPullquote currentBlock;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ArticleViewer.DrawingText textLayout;
      private ArticleViewer.DrawingText textLayout2;
      private int textX = AndroidUtilities.dp(18.0F);
      private int textY = AndroidUtilities.dp(8.0F);
      private int textY2;

      public BlockPullquoteCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

            if (this.textLayout2 != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY2);
               this.textLayout2.draw(var1);
               var1.restore();
            }

         }
      }

      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockPullquote var4 = this.currentBlock;
         if (var4 != null) {
            this.textLayout = ArticleViewer.this.createLayoutForText(this, (CharSequence)null, var4.text, var3 - AndroidUtilities.dp(36.0F), this.currentBlock, this.parentAdapter);
            ArticleViewer.DrawingText var5 = this.textLayout;
            var2 = 0;
            if (var5 != null) {
               var2 = 0 + AndroidUtilities.dp(8.0F) + this.textLayout.getHeight();
            }

            this.textLayout2 = ArticleViewer.this.createLayoutForText(this, (CharSequence)null, this.currentBlock.caption, var3 - AndroidUtilities.dp(36.0F), this.currentBlock, this.parentAdapter);
            var1 = var2;
            if (this.textLayout2 != null) {
               this.textY2 = AndroidUtilities.dp(2.0F) + var2;
               var1 = var2 + AndroidUtilities.dp(8.0F) + this.textLayout2.getHeight();
            }

            var2 = var1;
            if (var1 != 0) {
               var2 = var1 + AndroidUtilities.dp(8.0F);
            }
         } else {
            var2 = 1;
         }

         this.setMeasuredDimension(var3, var2);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) && !ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout2, this.textX, this.textY2) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockPullquote var1) {
         this.currentBlock = var1;
         this.requestLayout();
      }
   }

   private class BlockRelatedArticlesCell extends View {
      private int additionalHeight;
      private ArticleViewer.TL_pageBlockRelatedArticlesChild currentBlock;
      private boolean divider;
      private boolean drawImage;
      private ImageReceiver imageView;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ArticleViewer.DrawingText textLayout;
      private ArticleViewer.DrawingText textLayout2;
      private int textOffset;
      private int textX = AndroidUtilities.dp(18.0F);
      private int textY = AndroidUtilities.dp(10.0F);

      public BlockRelatedArticlesCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
         this.imageView = new ImageReceiver(this);
         this.imageView.setRoundRadius(AndroidUtilities.dp(6.0F));
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.drawImage) {
               this.imageView.draw(var1);
            }

            var1.save();
            var1.translate((float)this.textX, (float)AndroidUtilities.dp(10.0F));
            ArticleViewer.DrawingText var2 = this.textLayout;
            if (var2 != null) {
               var2.draw(var1);
            }

            if (this.textLayout2 != null) {
               var1.translate(0.0F, (float)this.textOffset);
               this.textLayout2.draw(var1);
            }

            var1.restore();
            if (this.divider) {
               float var3;
               if (ArticleViewer.this.isRtl) {
                  var3 = 0.0F;
               } else {
                  var3 = (float)AndroidUtilities.dp(17.0F);
               }

               float var4 = (float)(this.getMeasuredHeight() - 1);
               int var5 = this.getMeasuredWidth();
               int var6;
               if (ArticleViewer.this.isRtl) {
                  var6 = AndroidUtilities.dp(17.0F);
               } else {
                  var6 = 0;
               }

               var1.drawLine(var3, var4, (float)(var5 - var6), (float)(this.getMeasuredHeight() - 1), ArticleViewer.dividerPaint);
            }

         }
      }

      @SuppressLint({"DrawAllocation", "NewApi"})
      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         boolean var4;
         if (this.currentBlock.num != this.currentBlock.parent.articles.size() - 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.divider = var4;
         TLRPC.TL_pageRelatedArticle var5 = (TLRPC.TL_pageRelatedArticle)this.currentBlock.parent.articles.get(this.currentBlock.num);
         this.additionalHeight = 0;
         if (ArticleViewer.this.selectedFontSize == 0) {
            this.additionalHeight = -AndroidUtilities.dp(4.0F);
         } else if (ArticleViewer.this.selectedFontSize == 1) {
            this.additionalHeight = -AndroidUtilities.dp(2.0F);
         } else if (ArticleViewer.this.selectedFontSize == 3) {
            this.additionalHeight = AndroidUtilities.dp(2.0F);
         } else if (ArticleViewer.this.selectedFontSize == 4) {
            this.additionalHeight = AndroidUtilities.dp(4.0F);
         }

         long var6 = var5.photo_id;
         TLRPC.Photo var8;
         if (var6 != 0L) {
            var8 = ArticleViewer.this.getPhotoWithId(var6);
         } else {
            var8 = null;
         }

         if (var8 != null) {
            this.drawImage = true;
            TLRPC.PhotoSize var9 = FileLoader.getClosestPhotoSizeWithSize(var8.sizes, AndroidUtilities.getPhotoSize());
            TLRPC.PhotoSize var10 = FileLoader.getClosestPhotoSizeWithSize(var8.sizes, 80, true);
            TLRPC.PhotoSize var11 = var10;
            if (var9 == var10) {
               var11 = null;
            }

            this.imageView.setImage(ImageLocation.getForPhoto(var9, var8), "64_64", ImageLocation.getForPhoto(var11, var8), "64_64_b", var9.size, (String)null, ArticleViewer.this.currentPage, 1);
         } else {
            this.drawImage = false;
         }

         int var12 = AndroidUtilities.dp(60.0F);
         var1 = var3 - AndroidUtilities.dp(36.0F);
         var2 = var1;
         if (this.drawImage) {
            var2 = AndroidUtilities.dp(44.0F);
            this.imageView.setImageCoords(var3 - var2 - AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F), var2, var2);
            var2 = var1 - (this.imageView.getImageWidth() + AndroidUtilities.dp(6.0F));
         }

         var1 = AndroidUtilities.dp(18.0F);
         String var19 = var5.title;
         if (var19 != null) {
            this.textLayout = ArticleViewer.this.createLayoutForText(this, var19, (TLRPC.RichText)null, var2, this.textY, this.currentBlock, Alignment.ALIGN_NORMAL, 3, this.parentAdapter);
         }

         ArticleViewer.DrawingText var20 = this.textLayout;
         int var14;
         int var15;
         boolean var23;
         if (var20 != null) {
            int var13 = var20.getLineCount();
            this.textOffset = this.textLayout.getHeight() + AndroidUtilities.dp(6.0F) + this.additionalHeight;
            var14 = var1 + this.textLayout.getHeight();
            var1 = 0;

            boolean var17;
            while(true) {
               if (var1 >= var13) {
                  var17 = false;
                  break;
               }

               if (this.textLayout.getLineLeft(var1) != 0.0F) {
                  var17 = true;
                  break;
               }

               ++var1;
            }

            var15 = 4 - var13;
            var23 = var17;
            var1 = var14;
         } else {
            this.textOffset = 0;
            var23 = false;
            var15 = 4;
         }

         if (var5.published_date != 0 && !TextUtils.isEmpty(var5.author)) {
            Object[] var10002 = new Object[2];
            long var10006 = (long)var5.published_date;
            var10002[0] = LocaleController.getInstance().chatFullDate.format(var10006 * 1000L);
            var10002[1] = var5.author;
            var19 = LocaleController.formatString("ArticleDateByAuthor", 2131558705, var10002);
         } else if (!TextUtils.isEmpty(var5.author)) {
            var19 = LocaleController.formatString("ArticleByAuthor", 2131558704, var5.author);
         } else if (var5.published_date != 0) {
            long var10001 = (long)var5.published_date;
            var19 = LocaleController.getInstance().chatFullDate.format(var10001 * 1000L);
         } else if (!TextUtils.isEmpty(var5.description)) {
            var19 = var5.description;
         } else {
            var19 = var5.url;
         }

         ArticleViewer var21 = ArticleViewer.this;
         var14 = this.textY;
         int var16 = this.textOffset;
         ArticleViewer.TL_pageBlockRelatedArticlesChild var18 = this.currentBlock;
         Alignment var22;
         if (!var21.isRtl && !var23) {
            var22 = Alignment.ALIGN_NORMAL;
         } else {
            var22 = Alignment.ALIGN_RIGHT;
         }

         this.textLayout2 = var21.createLayoutForText(this, var19, (TLRPC.RichText)null, var2, var16 + var14, var18, var22, var15, this.parentAdapter);
         var20 = this.textLayout2;
         var2 = var1;
         if (var20 != null) {
            var1 += var20.getHeight();
            var2 = var1;
            if (this.textLayout != null) {
               var2 = var1 + AndroidUtilities.dp(6.0F) + this.additionalHeight;
            }
         }

         this.setMeasuredDimension(var3, Math.max(var12, var2) + this.divider);
      }

      public void setBlock(ArticleViewer.TL_pageBlockRelatedArticlesChild var1) {
         this.currentBlock = var1;
         this.requestLayout();
      }
   }

   private class BlockRelatedArticlesHeaderCell extends View {
      private TLRPC.TL_pageBlockRelatedArticles currentBlock;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ArticleViewer.DrawingText textLayout;
      private int textX = AndroidUtilities.dp(18.0F);
      private int textY;

      public BlockRelatedArticlesHeaderCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

         }
      }

      protected void onMeasure(int var1, int var2) {
         var1 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockRelatedArticles var3 = this.currentBlock;
         if (var3 != null) {
            this.textLayout = ArticleViewer.this.createLayoutForText(this, (CharSequence)null, var3.title, var1 - AndroidUtilities.dp(52.0F), 0, this.currentBlock, Alignment.ALIGN_NORMAL, 1, this.parentAdapter);
            if (this.textLayout != null) {
               this.textY = AndroidUtilities.dp(6.0F) + (AndroidUtilities.dp(32.0F) - this.textLayout.getHeight()) / 2;
            }
         }

         if (this.textLayout != null) {
            this.setMeasuredDimension(var1, AndroidUtilities.dp(38.0F));
         } else {
            this.setMeasuredDimension(var1, 1);
         }

      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockRelatedArticles var1) {
         this.currentBlock = var1;
         this.requestLayout();
      }
   }

   private class BlockRelatedArticlesShadowCell extends View {
      private CombinedDrawable shadowDrawable;

      public BlockRelatedArticlesShadowCell(Context var2) {
         super(var2);
         Drawable var3 = Theme.getThemedDrawable(var2, 2131165395, -16777216);
         this.shadowDrawable = new CombinedDrawable(new ColorDrawable(-986896), var3);
         this.shadowDrawable.setFullsize(true);
         this.setBackgroundDrawable(this.shadowDrawable);
      }

      protected void onMeasure(int var1, int var2) {
         this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(12.0F));
         var1 = ArticleViewer.this.getSelectedColor();
         if (var1 == 0) {
            Theme.setCombinedDrawableColor(this.shadowDrawable, -986896, false);
         } else if (var1 == 1) {
            Theme.setCombinedDrawableColor(this.shadowDrawable, -1712440, false);
         } else if (var1 == 2) {
            Theme.setCombinedDrawableColor(this.shadowDrawable, -15000805, false);
         }

      }
   }

   private class BlockSlideshowCell extends FrameLayout {
      private ArticleViewer.DrawingText captionLayout;
      private ArticleViewer.DrawingText creditLayout;
      private int creditOffset;
      private TLRPC.TL_pageBlockSlideshow currentBlock;
      private int currentPage;
      private View dotsContainer;
      private PagerAdapter innerAdapter;
      private ViewPager innerListView;
      private float pageOffset;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private int textX = AndroidUtilities.dp(18.0F);
      private int textY;

      public BlockSlideshowCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
         if (ArticleViewer.dotsPaint == null) {
            ArticleViewer.dotsPaint = new Paint(1);
            ArticleViewer.dotsPaint.setColor(-1);
         }

         this.innerListView = new ViewPager(var2) {
            public boolean onInterceptTouchEvent(MotionEvent var1) {
               ArticleViewer.this.windowView.requestDisallowInterceptTouchEvent(true);
               return super.onInterceptTouchEvent(var1);
            }

            public boolean onTouchEvent(MotionEvent var1) {
               return super.onTouchEvent(var1);
            }
         };
         this.innerListView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int var1) {
            }

            public void onPageScrolled(int var1, float var2, int var3) {
               var2 = (float)BlockSlideshowCell.this.innerListView.getMeasuredWidth();
               if (var2 != 0.0F) {
                  ArticleViewer.BlockSlideshowCell var4 = BlockSlideshowCell.this;
                  var4.pageOffset = ((float)var1 * var2 + (float)var3 - (float)var4.currentPage * var2) / var2;
                  BlockSlideshowCell.this.dotsContainer.invalidate();
               }
            }

            public void onPageSelected(int var1) {
               BlockSlideshowCell.this.currentPage = var1;
               BlockSlideshowCell.this.dotsContainer.invalidate();
            }
         });
         ViewPager var4 = this.innerListView;
         PagerAdapter var6 = new PagerAdapter() {
            public void destroyItem(ViewGroup var1, int var2, Object var3) {
               var1.removeView(((null.ObjectContainer)var3).view);
            }

            public int getCount() {
               return BlockSlideshowCell.this.currentBlock == null ? 0 : BlockSlideshowCell.this.currentBlock.items.size();
            }

            public int getItemPosition(Object var1) {
               null.ObjectContainer var2 = (null.ObjectContainer)var1;
               return BlockSlideshowCell.this.currentBlock.items.contains(var2.block) ? -1 : -2;
            }

            public Object instantiateItem(ViewGroup var1, int var2) {
               TLRPC.PageBlock var3 = (TLRPC.PageBlock)BlockSlideshowCell.this.currentBlock.items.get(var2);
               ArticleViewer.BlockSlideshowCell var4;
               Object var6;
               if (var3 instanceof TLRPC.TL_pageBlockPhoto) {
                  var4 = BlockSlideshowCell.this;
                  var6 = ArticleViewer.this.new BlockPhotoCell(var4.getContext(), BlockSlideshowCell.this.parentAdapter, 1);
                  ((ArticleViewer.BlockPhotoCell)var6).setBlock((TLRPC.TL_pageBlockPhoto)var3, true, true);
               } else {
                  var4 = BlockSlideshowCell.this;
                  var6 = ArticleViewer.this.new BlockVideoCell(var4.getContext(), BlockSlideshowCell.this.parentAdapter, 1);
                  ((ArticleViewer.BlockVideoCell)var6).setBlock((TLRPC.TL_pageBlockVideo)var3, true, true);
               }

               var1.addView((View)var6);
               null.ObjectContainer var5 = new null.ObjectContainer();
               var5.view = (View)var6;
               var5.block = var3;
               return var5;
            }

            public boolean isViewFromObject(View var1, Object var2) {
               boolean var3;
               if (((null.ObjectContainer)var2).view == var1) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               return var3;
            }

            public void unregisterDataSetObserver(DataSetObserver var1) {
               if (var1 != null) {
                  super.unregisterDataSetObserver(var1);
               }

            }

            class ObjectContainer {
               private TLRPC.PageBlock block;
               private View view;
            }
         };
         this.innerAdapter = var6;
         var4.setAdapter(var6);
         int var5 = ArticleViewer.this.getSelectedColor();
         if (var5 == 0) {
            AndroidUtilities.setViewPagerEdgeEffectColor(this.innerListView, -657673);
         } else if (var5 == 1) {
            AndroidUtilities.setViewPagerEdgeEffectColor(this.innerListView, -659492);
         } else if (var5 == 2) {
            AndroidUtilities.setViewPagerEdgeEffectColor(this.innerListView, -15461356);
         }

         this.addView(this.innerListView);
         this.dotsContainer = new View(var2) {
            protected void onDraw(Canvas var1) {
               if (BlockSlideshowCell.this.currentBlock != null) {
                  int var2 = BlockSlideshowCell.this.innerAdapter.getCount();
                  int var3 = AndroidUtilities.dp(7.0F) * var2 + (var2 - 1) * AndroidUtilities.dp(6.0F) + AndroidUtilities.dp(4.0F);
                  int var5;
                  if (var3 < this.getMeasuredWidth()) {
                     var2 = (this.getMeasuredWidth() - var3) / 2;
                  } else {
                     var3 = AndroidUtilities.dp(4.0F);
                     int var4 = AndroidUtilities.dp(13.0F);
                     var5 = (this.getMeasuredWidth() - AndroidUtilities.dp(8.0F)) / 2 / var4;
                     int var6 = BlockSlideshowCell.this.currentPage;
                     int var7 = var2 - var5 - 1;
                     if (var6 == var7 && BlockSlideshowCell.this.pageOffset < 0.0F) {
                        var2 = var3 - ((int)(BlockSlideshowCell.this.pageOffset * (float)var4) + (var2 - var5 * 2 - 1) * var4);
                     } else {
                        label60: {
                           if (BlockSlideshowCell.this.currentPage >= var7) {
                              var2 = (var2 - var5 * 2 - 1) * var4;
                           } else if (BlockSlideshowCell.this.currentPage > var5) {
                              var2 = (int)(BlockSlideshowCell.this.pageOffset * (float)var4) + (BlockSlideshowCell.this.currentPage - var5) * var4;
                           } else {
                              if (BlockSlideshowCell.this.currentPage != var5 || BlockSlideshowCell.this.pageOffset <= 0.0F) {
                                 var2 = var3;
                                 break label60;
                              }

                              var2 = (int)(BlockSlideshowCell.this.pageOffset * (float)var4);
                           }

                           var2 = var3 - var2;
                        }
                     }
                  }

                  for(var3 = 0; var3 < BlockSlideshowCell.this.currentBlock.items.size(); ++var3) {
                     var5 = AndroidUtilities.dp(4.0F) + var2 + AndroidUtilities.dp(13.0F) * var3;
                     Drawable var8;
                     if (BlockSlideshowCell.this.currentPage == var3) {
                        var8 = ArticleViewer.this.slideDotBigDrawable;
                     } else {
                        var8 = ArticleViewer.this.slideDotDrawable;
                     }

                     var8.setBounds(var5 - AndroidUtilities.dp(5.0F), 0, var5 + AndroidUtilities.dp(5.0F), AndroidUtilities.dp(10.0F));
                     var8.draw(var1);
                  }

               }
            }
         };
         this.addView(this.dotsContainer);
         this.setWillNotDraw(false);
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.captionLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.captionLayout.draw(var1);
               var1.restore();
            }

            if (this.creditLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)(this.textY + this.creditOffset));
               this.creditLayout.draw(var1);
               var1.restore();
            }

         }
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         this.innerListView.layout(0, AndroidUtilities.dp(8.0F), this.innerListView.getMeasuredWidth(), AndroidUtilities.dp(8.0F) + this.innerListView.getMeasuredHeight());
         var2 = this.innerListView.getBottom() - AndroidUtilities.dp(23.0F);
         View var6 = this.dotsContainer;
         var6.layout(0, var2, var6.getMeasuredWidth(), this.dotsContainer.getMeasuredHeight() + var2);
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         if (this.currentBlock != null) {
            var2 = AndroidUtilities.dp(310.0F);
            this.innerListView.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var2, 1073741824));
            this.currentBlock.items.size();
            this.dotsContainer.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(10.0F), 1073741824));
            int var4 = var3 - AndroidUtilities.dp(36.0F);
            this.textY = AndroidUtilities.dp(16.0F) + var2;
            ArticleViewer var5 = ArticleViewer.this;
            TLRPC.TL_pageBlockSlideshow var6 = this.currentBlock;
            this.captionLayout = var5.createLayoutForText(this, (CharSequence)null, var6.caption.text, var4, var6, this.parentAdapter);
            var1 = var2;
            if (this.captionLayout != null) {
               this.creditOffset = AndroidUtilities.dp(4.0F) + this.captionLayout.getHeight();
               var1 = var2 + this.creditOffset + AndroidUtilities.dp(4.0F);
            }

            ArticleViewer var7 = ArticleViewer.this;
            TLRPC.TL_pageBlockSlideshow var8 = this.currentBlock;
            TLRPC.RichText var10 = var8.caption.credit;
            Alignment var9;
            if (var7.isRtl) {
               var9 = Alignment.ALIGN_RIGHT;
            } else {
               var9 = Alignment.ALIGN_NORMAL;
            }

            this.creditLayout = var7.createLayoutForText(this, (CharSequence)null, var10, var4, var8, var9, this.parentAdapter);
            var2 = var1;
            if (this.creditLayout != null) {
               var2 = var1 + AndroidUtilities.dp(4.0F) + this.creditLayout.getHeight();
            }

            var1 = var2 + AndroidUtilities.dp(16.0F);
         } else {
            var1 = 1;
         }

         this.setMeasuredDimension(var3, var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.captionLayout, this.textX, this.textY) && !ArticleViewer.this.checkLayoutForLinks(var1, this, this.creditLayout, this.textX, this.textY + this.creditOffset) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockSlideshow var1) {
         this.currentBlock = var1;
         this.innerAdapter.notifyDataSetChanged();
         this.innerListView.setCurrentItem(0, false);
         this.innerListView.forceLayout();
         this.requestLayout();
      }
   }

   private class BlockSubheaderCell extends View {
      private TLRPC.TL_pageBlockSubheader currentBlock;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ArticleViewer.DrawingText textLayout;
      private int textX = AndroidUtilities.dp(18.0F);
      private int textY = AndroidUtilities.dp(8.0F);

      public BlockSubheaderCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

         }
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setEnabled(true);
         if (this.textLayout != null) {
            StringBuilder var2 = new StringBuilder();
            var2.append(this.textLayout.getText());
            var2.append(", ");
            var2.append(LocaleController.getString("AccDescrIVHeading", 2131558439));
            var1.setText(var2.toString());
         }
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         var2 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockSubheader var3 = this.currentBlock;
         var1 = 0;
         if (var3 != null) {
            ArticleViewer var4 = ArticleViewer.this;
            TLRPC.RichText var5 = var3.text;
            int var6 = AndroidUtilities.dp(36.0F);
            TLRPC.TL_pageBlockSubheader var7 = this.currentBlock;
            Alignment var8;
            if (ArticleViewer.this.isRtl) {
               var8 = Alignment.ALIGN_RIGHT;
            } else {
               var8 = Alignment.ALIGN_NORMAL;
            }

            this.textLayout = var4.createLayoutForText(this, (CharSequence)null, var5, var2 - var6, var7, var8, this.parentAdapter);
            if (this.textLayout != null) {
               var1 = 0 + AndroidUtilities.dp(16.0F) + this.textLayout.getHeight();
            }
         } else {
            var1 = 1;
         }

         this.setMeasuredDimension(var2, var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockSubheader var1) {
         this.currentBlock = var1;
         this.requestLayout();
      }
   }

   private class BlockSubtitleCell extends View {
      private TLRPC.TL_pageBlockSubtitle currentBlock;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ArticleViewer.DrawingText textLayout;
      private int textX = AndroidUtilities.dp(18.0F);
      private int textY = AndroidUtilities.dp(8.0F);

      public BlockSubtitleCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

         }
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setEnabled(true);
         if (this.textLayout != null) {
            StringBuilder var2 = new StringBuilder();
            var2.append(this.textLayout.getText());
            var2.append(", ");
            var2.append(LocaleController.getString("AccDescrIVHeading", 2131558439));
            var1.setText(var2.toString());
         }
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         var2 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockSubtitle var3 = this.currentBlock;
         var1 = 0;
         if (var3 != null) {
            ArticleViewer var4 = ArticleViewer.this;
            TLRPC.RichText var5 = var3.text;
            int var6 = AndroidUtilities.dp(36.0F);
            TLRPC.TL_pageBlockSubtitle var7 = this.currentBlock;
            Alignment var8;
            if (ArticleViewer.this.isRtl) {
               var8 = Alignment.ALIGN_RIGHT;
            } else {
               var8 = Alignment.ALIGN_NORMAL;
            }

            this.textLayout = var4.createLayoutForText(this, (CharSequence)null, var5, var2 - var6, var7, var8, this.parentAdapter);
            if (this.textLayout != null) {
               var1 = 0 + AndroidUtilities.dp(16.0F) + this.textLayout.getHeight();
            }
         } else {
            var1 = 1;
         }

         this.setMeasuredDimension(var2, var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockSubtitle var1) {
         this.currentBlock = var1;
         this.requestLayout();
      }
   }

   private class BlockTableCell extends FrameLayout implements TableLayout.TableLayoutDelegate {
      private TLRPC.TL_pageBlockTable currentBlock;
      private boolean firstLayout;
      private boolean inLayout;
      private int listX;
      private int listY;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private HorizontalScrollView scrollView;
      private TableLayout tableLayout;
      private int textX;
      private int textY;
      private ArticleViewer.DrawingText titleLayout;

      public BlockTableCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
         this.scrollView = new HorizontalScrollView(var2) {
            public boolean onInterceptTouchEvent(MotionEvent var1) {
               if (BlockTableCell.this.tableLayout.getMeasuredWidth() > this.getMeasuredWidth() - AndroidUtilities.dp(36.0F)) {
                  ArticleViewer.this.windowView.requestDisallowInterceptTouchEvent(true);
               }

               return super.onInterceptTouchEvent(var1);
            }

            protected void onMeasure(int var1, int var2) {
               BlockTableCell.this.tableLayout.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1) - this.getPaddingLeft() - this.getPaddingRight(), 0), var2);
               this.setMeasuredDimension(MeasureSpec.getSize(var1), BlockTableCell.this.tableLayout.getMeasuredHeight());
            }

            protected void onScrollChanged(int var1, int var2, int var3, int var4) {
               super.onScrollChanged(var1, var2, var3, var4);
               if (ArticleViewer.this.pressedLinkOwnerLayout != null) {
                  ArticleViewer.this.pressedLinkOwnerLayout = null;
                  ArticleViewer.this.pressedLinkOwnerView = null;
               }

            }

            protected boolean overScrollBy(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9) {
               ArticleViewer.this.removePressedLink();
               return super.overScrollBy(var1, var2, var3, var4, var5, var6, var7, var8, var9);
            }
         };
         this.scrollView.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
         this.scrollView.setClipToPadding(false);
         this.addView(this.scrollView, LayoutHelper.createFrame(-1, -2.0F));
         this.tableLayout = new TableLayout(var2, this);
         this.tableLayout.setOrientation(0);
         this.tableLayout.setRowOrderPreserved(true);
         this.scrollView.addView(this.tableLayout, new android.widget.FrameLayout.LayoutParams(-2, -2));
         this.setWillNotDraw(false);
      }

      public ArticleViewer.DrawingText createTextLayout(TLRPC.TL_pageTableCell var1, int var2) {
         if (var1 == null) {
            return null;
         } else {
            Alignment var3;
            if (var1.align_right) {
               var3 = Alignment.ALIGN_OPPOSITE;
            } else if (var1.align_center) {
               var3 = Alignment.ALIGN_CENTER;
            } else {
               var3 = Alignment.ALIGN_NORMAL;
            }

            return ArticleViewer.this.createLayoutForText(this, (CharSequence)null, var1.text, var2, 0, this.currentBlock, var3, 0, this.parentAdapter);
         }
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

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.titleLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.titleLayout.draw(var1);
               var1.restore();
            }

            if (this.currentBlock.level > 0) {
               float var2 = (float)AndroidUtilities.dp(18.0F);
               float var3 = (float)AndroidUtilities.dp(20.0F);
               int var4 = this.getMeasuredHeight();
               int var5;
               if (this.currentBlock.bottom) {
                  var5 = AndroidUtilities.dp(6.0F);
               } else {
                  var5 = 0;
               }

               var1.drawRect(var2, 0.0F, var3, (float)(var4 - var5), ArticleViewer.quoteLinePaint);
            }

         }
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         HorizontalScrollView var6 = this.scrollView;
         var2 = this.listX;
         var6.layout(var2, this.listY, var6.getMeasuredWidth() + var2, this.listY + this.scrollView.getMeasuredHeight());
         if (this.firstLayout) {
            if (ArticleViewer.this.isRtl) {
               this.scrollView.setScrollX(this.tableLayout.getMeasuredWidth() - this.scrollView.getMeasuredWidth() + AndroidUtilities.dp(36.0F));
            } else {
               this.scrollView.setScrollX(0);
            }

            this.firstLayout = false;
         }

      }

      protected void onMeasure(int var1, int var2) {
         byte var6 = 1;
         this.inLayout = true;
         int var3 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockTable var4 = this.currentBlock;
         var1 = var6;
         if (var4 != null) {
            var1 = var4.level;
            if (var1 > 0) {
               this.listX = AndroidUtilities.dp((float)(var1 * 14));
               this.textX = this.listX + AndroidUtilities.dp(18.0F);
               var1 = this.textX;
            } else {
               this.listX = 0;
               this.textX = AndroidUtilities.dp(18.0F);
               var1 = AndroidUtilities.dp(36.0F);
            }

            ArticleViewer var5 = ArticleViewer.this;
            var4 = this.currentBlock;
            this.titleLayout = var5.createLayoutForText(this, (CharSequence)null, var4.title, var3 - var1, 0, var4, Alignment.ALIGN_CENTER, 0, this.parentAdapter);
            ArticleViewer.DrawingText var7 = this.titleLayout;
            if (var7 != null) {
               this.textY = 0;
               var1 = var7.getHeight() + AndroidUtilities.dp(8.0F) + 0;
               this.listY = var1;
            } else {
               this.listY = AndroidUtilities.dp(8.0F);
               var1 = 0;
            }

            this.scrollView.measure(MeasureSpec.makeMeasureSpec(var3 - this.listX, 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
            var2 = var1 + this.scrollView.getMeasuredHeight() + AndroidUtilities.dp(8.0F);
            var4 = this.currentBlock;
            var1 = var2;
            if (var4.level > 0) {
               var1 = var2;
               if (!var4.bottom) {
                  var1 = var2 + AndroidUtilities.dp(8.0F);
               }
            }
         }

         this.setMeasuredDimension(var3, var1);
         this.inLayout = false;
      }

      public boolean onTouchEvent(MotionEvent var1) {
         int var2 = this.tableLayout.getChildCount();
         boolean var3 = false;

         for(int var4 = 0; var4 < var2; ++var4) {
            TableLayout.Child var5 = this.tableLayout.getChildAt(var4);
            if (ArticleViewer.this.checkLayoutForLinks(var1, this, var5.textLayout, this.scrollView.getPaddingLeft() - this.scrollView.getScrollX() + this.listX + var5.getTextX(), this.listY + var5.getTextY())) {
               return true;
            }
         }

         if (ArticleViewer.this.checkLayoutForLinks(var1, this, this.titleLayout, this.textX, this.textY) || super.onTouchEvent(var1)) {
            var3 = true;
         }

         return var3;
      }

      public void setBlock(TLRPC.TL_pageBlockTable var1) {
         this.currentBlock = var1;
         int var2 = ArticleViewer.this.getSelectedColor();
         if (var2 == 0) {
            AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, -657673);
         } else if (var2 == 1) {
            AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, -659492);
         } else if (var2 == 2) {
            AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, -15461356);
         }

         this.tableLayout.removeAllChildrens();
         this.tableLayout.setDrawLines(this.currentBlock.bordered);
         this.tableLayout.setStriped(this.currentBlock.striped);
         this.tableLayout.setRtl(ArticleViewer.this.isRtl);
         int var3;
         int var4;
         int var5;
         TLRPC.TL_pageTableRow var11;
         if (!this.currentBlock.rows.isEmpty()) {
            var11 = (TLRPC.TL_pageTableRow)this.currentBlock.rows.get(0);
            var3 = var11.cells.size();
            var4 = 0;
            var2 = 0;

            while(true) {
               var5 = var2;
               if (var4 >= var3) {
                  break;
               }

               var5 = ((TLRPC.TL_pageTableCell)var11.cells.get(var4)).colspan;
               if (var5 == 0) {
                  var5 = 1;
               }

               var2 += var5;
               ++var4;
            }
         } else {
            var5 = 0;
         }

         int var6 = this.currentBlock.rows.size();

         for(var2 = 0; var2 < var6; ++var2) {
            var11 = (TLRPC.TL_pageTableRow)this.currentBlock.rows.get(var2);
            int var7 = var11.cells.size();
            var4 = 0;

            for(var3 = 0; var4 < var7; ++var4) {
               TLRPC.TL_pageTableCell var8 = (TLRPC.TL_pageTableCell)var11.cells.get(var4);
               int var9 = var8.colspan;
               if (var9 == 0) {
                  var9 = 1;
               }

               int var10 = var8.rowspan;
               if (var10 == 0) {
                  var10 = 1;
               }

               if (var8.text != null) {
                  this.tableLayout.addChild(var8, var3, var2, var9);
               } else {
                  this.tableLayout.addChild(var3, var2, var9, var10);
               }

               var3 += var9;
            }
         }

         this.tableLayout.setColumnCount(var5);
         this.firstLayout = true;
         this.requestLayout();
      }
   }

   private class BlockTitleCell extends View {
      private TLRPC.TL_pageBlockTitle currentBlock;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private ArticleViewer.DrawingText textLayout;
      private int textX = AndroidUtilities.dp(18.0F);
      private int textY;

      public BlockTitleCell(Context var2, ArticleViewer.WebpageAdapter var3) {
         super(var2);
         this.parentAdapter = var3;
      }

      protected void onDraw(Canvas var1) {
         if (this.currentBlock != null) {
            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.textLayout.draw(var1);
               var1.restore();
            }

         }
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setEnabled(true);
         if (this.textLayout != null) {
            StringBuilder var2 = new StringBuilder();
            var2.append(this.textLayout.getText());
            var2.append(", ");
            var2.append(LocaleController.getString("AccDescrIVTitle", 2131558440));
            var1.setText(var2.toString());
         }
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         var2 = MeasureSpec.getSize(var1);
         TLRPC.TL_pageBlockTitle var3 = this.currentBlock;
         if (var3 != null) {
            ArticleViewer var4 = ArticleViewer.this;
            TLRPC.RichText var5 = var3.text;
            var1 = AndroidUtilities.dp(36.0F);
            TLRPC.TL_pageBlockTitle var6 = this.currentBlock;
            Alignment var7;
            if (ArticleViewer.this.isRtl) {
               var7 = Alignment.ALIGN_RIGHT;
            } else {
               var7 = Alignment.ALIGN_NORMAL;
            }

            this.textLayout = var4.createLayoutForText(this, (CharSequence)null, var5, var2 - var1, var6, var7, this.parentAdapter);
            ArticleViewer.DrawingText var8 = this.textLayout;
            var1 = 0;
            if (var8 != null) {
               var1 = 0 + AndroidUtilities.dp(16.0F) + this.textLayout.getHeight();
            }

            if (this.currentBlock.first) {
               var1 += AndroidUtilities.dp(8.0F);
               this.textY = AndroidUtilities.dp(16.0F);
            } else {
               this.textY = AndroidUtilities.dp(8.0F);
            }
         } else {
            var1 = 1;
         }

         this.setMeasuredDimension(var2, var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!ArticleViewer.this.checkLayoutForLinks(var1, this, this.textLayout, this.textX, this.textY) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void setBlock(TLRPC.TL_pageBlockTitle var1) {
         this.currentBlock = var1;
         this.requestLayout();
      }
   }

   private class BlockVideoCell extends FrameLayout implements DownloadController.FileDownloadProgressListener {
      private int TAG;
      private boolean autoDownload;
      private int buttonPressed;
      private int buttonState;
      private int buttonX;
      private int buttonY;
      private boolean cancelLoading;
      private ArticleViewer.DrawingText captionLayout;
      private ArticleViewer.BlockChannelCell channelCell;
      private ArticleViewer.DrawingText creditLayout;
      private int creditOffset;
      private TLRPC.TL_pageBlockVideo currentBlock;
      private TLRPC.Document currentDocument;
      private int currentType;
      private MessageObject.GroupedMessagePosition groupPosition;
      private ImageReceiver imageView;
      private boolean isFirst;
      private boolean isGif;
      private boolean isLast;
      private ArticleViewer.WebpageAdapter parentAdapter;
      private TLRPC.PageBlock parentBlock;
      private boolean photoPressed;
      private RadialProgress2 radialProgress;
      private int textX;
      private int textY;

      public BlockVideoCell(Context var2, ArticleViewer.WebpageAdapter var3, int var4) {
         super(var2);
         this.parentAdapter = var3;
         this.setWillNotDraw(false);
         this.imageView = new ImageReceiver(this);
         this.imageView.setNeedsQualityThumb(true);
         this.imageView.setShouldGenerateQualityThumb(true);
         this.currentType = var4;
         this.radialProgress = new RadialProgress2(this);
         this.radialProgress.setProgressColor(-1);
         this.radialProgress.setColors(1711276032, 2130706432, -1, -2500135);
         this.TAG = DownloadController.getInstance(ArticleViewer.this.currentAccount).generateObserverTag();
         this.channelCell = ArticleViewer.this.new BlockChannelCell(var2, this.parentAdapter, 1);
         this.addView(this.channelCell, LayoutHelper.createFrame(-1, -2.0F));
      }

      private void didPressedButton(boolean var1) {
         int var2 = this.buttonState;
         if (var2 == 0) {
            this.cancelLoading = false;
            this.radialProgress.setProgress(0.0F, false);
            if (this.isGif) {
               TLRPC.PhotoSize var3 = FileLoader.getClosestPhotoSizeWithSize(this.currentDocument.thumbs, 40);
               this.imageView.setImage(ImageLocation.getForDocument(this.currentDocument), (String)null, ImageLocation.getForDocument(var3, this.currentDocument), "80_80_b", this.currentDocument.size, (String)null, ArticleViewer.this.currentPage, 1);
            } else {
               FileLoader.getInstance(ArticleViewer.this.currentAccount).loadFile(this.currentDocument, ArticleViewer.this.currentPage, 1, 1);
            }

            this.buttonState = 1;
            this.radialProgress.setIcon(this.getIconForCurrentState(), true, var1);
            this.invalidate();
         } else if (var2 == 1) {
            this.cancelLoading = true;
            if (this.isGif) {
               this.imageView.cancelLoadImage();
            } else {
               FileLoader.getInstance(ArticleViewer.this.currentAccount).cancelLoadFile(this.currentDocument);
            }

            this.buttonState = 0;
            this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
            this.invalidate();
         } else if (var2 == 2) {
            this.imageView.setAllowStartAnimation(true);
            this.imageView.startAnimation();
            this.buttonState = -1;
            this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
         } else if (var2 == 3) {
            ArticleViewer.this.openPhoto(this.currentBlock);
         }

      }

      private int getIconForCurrentState() {
         int var1 = this.buttonState;
         if (var1 == 0) {
            return 2;
         } else if (var1 == 1) {
            return 3;
         } else if (var1 == 2) {
            return 8;
         } else {
            return var1 == 3 ? 0 : 4;
         }
      }

      public View getChannelCell() {
         return this.channelCell;
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
         DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver(this);
      }

      protected void onDraw(Canvas var1) {
         int var2 = this.currentType;
         if (this.currentBlock != null) {
            if (!this.imageView.hasBitmapImage() || this.imageView.getCurrentAlpha() != 1.0F) {
               var1.drawRect(this.imageView.getDrawRegion(), ArticleViewer.photoBackgroundPaint);
            }

            this.imageView.draw(var1);
            if (this.imageView.getVisible()) {
               this.radialProgress.draw(var1);
            }

            this.textY = this.imageView.getImageY() + this.imageView.getImageHeight() + AndroidUtilities.dp(8.0F);
            if (this.captionLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)this.textY);
               this.captionLayout.draw(var1);
               var1.restore();
            }

            if (this.creditLayout != null) {
               var1.save();
               var1.translate((float)this.textX, (float)(this.textY + this.creditOffset));
               this.creditLayout.draw(var1);
               var1.restore();
            }

            if (this.currentBlock.level > 0) {
               float var3 = (float)AndroidUtilities.dp(18.0F);
               float var4 = (float)AndroidUtilities.dp(20.0F);
               int var5 = this.getMeasuredHeight();
               if (this.currentBlock.bottom) {
                  var2 = AndroidUtilities.dp(6.0F);
               } else {
                  var2 = 0;
               }

               var1.drawRect(var3, 0.0F, var4, (float)(var5 - var2), ArticleViewer.quoteLinePaint);
            }

         }
      }

      public void onFailedDownload(String var1, boolean var2) {
         this.updateButtonState(false);
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setEnabled(true);
         StringBuilder var2 = new StringBuilder(LocaleController.getString("AttachVideo", 2131558733));
         if (this.captionLayout != null) {
            var2.append(", ");
            var2.append(this.captionLayout.getText());
         }

         var1.setText(var2.toString());
      }

      @SuppressLint({"NewApi"})
      protected void onMeasure(int var1, int var2) {
         int var3;
         int var4;
         int var5;
         float var6;
         label159: {
            var3 = MeasureSpec.getSize(var1);
            var4 = this.currentType;
            if (var4 == 1) {
               var3 = ((View)this.getParent()).getMeasuredWidth();
               var4 = ((View)this.getParent()).getMeasuredHeight();
            } else {
               if (var4 != 2) {
                  var4 = 0;
                  var5 = var3;
                  break label159;
               }

               var6 = this.groupPosition.ph;
               Point var7 = AndroidUtilities.displaySize;
               var4 = (int)Math.ceil((double)(var6 * (float)Math.max(var7.x, var7.y) * 0.5F));
            }

            var5 = var3;
         }

         TLRPC.TL_pageBlockVideo var20 = this.currentBlock;
         if (var20 != null) {
            int var8;
            int var9;
            int var10;
            label151: {
               if (this.currentType == 0) {
                  var3 = var20.level;
                  if (var3 > 0) {
                     var8 = AndroidUtilities.dp((float)(var3 * 14)) + AndroidUtilities.dp(18.0F);
                     this.textX = var8;
                     var9 = var5 - (AndroidUtilities.dp(18.0F) + var8);
                     var10 = var9;
                     break label151;
                  }
               }

               this.textX = AndroidUtilities.dp(18.0F);
               var10 = var5 - AndroidUtilities.dp(36.0F);
               var9 = var5;
               var8 = 0;
            }

            if (this.currentDocument == null) {
               var3 = var4;
            } else {
               int var11;
               int var12;
               TLRPC.PhotoSize var21;
               label144: {
                  var11 = AndroidUtilities.dp(48.0F);
                  var21 = FileLoader.getClosestPhotoSizeWithSize(this.currentDocument.thumbs, 48);
                  var12 = this.currentType;
                  int var15;
                  int var16;
                  if (var12 == 0) {
                     var12 = this.currentDocument.attributes.size();
                     var3 = 0;

                     boolean var19;
                     while(true) {
                        if (var3 >= var12) {
                           boolean var26 = false;
                           var3 = var4;
                           var19 = var26;
                           break;
                        }

                        TLRPC.DocumentAttribute var13 = (TLRPC.DocumentAttribute)this.currentDocument.attributes.get(var3);
                        if (var13 instanceof TLRPC.TL_documentAttributeVideo) {
                           var3 = (int)((float)var9 / (float)var13.w * (float)var13.h);
                           var19 = true;
                           break;
                        }

                        ++var3;
                     }

                     if (var21 != null) {
                        var6 = (float)var21.w;
                     } else {
                        var6 = 100.0F;
                     }

                     float var14;
                     if (var21 != null) {
                        var14 = (float)var21.h;
                     } else {
                        var14 = 100.0F;
                     }

                     if (!var19) {
                        var3 = (int)((float)var9 / var6 * var14);
                     }

                     if (this.parentBlock instanceof TLRPC.TL_pageBlockCover) {
                        var4 = Math.min(var3, var9);
                        var15 = var8;
                     } else {
                        var12 = (int)((float)(Math.max(ArticleViewer.this.listView[0].getMeasuredWidth(), ArticleViewer.this.listView[0].getMeasuredHeight()) - AndroidUtilities.dp(56.0F)) * 0.9F);
                        var15 = var8;
                        var4 = var3;
                        if (var3 > var12) {
                           var9 = (int)((float)var12 / var14 * var6);
                           var15 = var8 + (var5 - var8 - var9) / 2;
                           var4 = var12;
                        }
                     }

                     if (var4 == 0) {
                        var3 = AndroidUtilities.dp(100.0F);
                        var16 = var9;
                     } else if (var4 < var11) {
                        var3 = var11;
                        var16 = var9;
                     } else {
                        var3 = var4;
                        var16 = var9;
                     }
                  } else {
                     var15 = var8;
                     var3 = var4;
                     var16 = var9;
                     if (var12 == 2) {
                        var12 = var9;
                        if ((this.groupPosition.flags & 2) == 0) {
                           var12 = var9 - AndroidUtilities.dp(2.0F);
                        }

                        var15 = var8;
                        var3 = var4;
                        var16 = var12;
                        if ((this.groupPosition.flags & 8) == 0) {
                           var3 = AndroidUtilities.dp(2.0F);
                           var3 = var4 - var3;
                           var4 = var4;
                           break label144;
                        }
                     }
                  }

                  var4 = var3;
                  var12 = var16;
                  var8 = var15;
               }

               ImageReceiver var27;
               label129: {
                  this.imageView.setQualityThumbDocument(this.currentDocument);
                  var27 = this.imageView;
                  if (!this.isFirst) {
                     var9 = this.currentType;
                     if (var9 != 1 && var9 != 2 && this.currentBlock.level <= 0) {
                        var9 = AndroidUtilities.dp(8.0F);
                        break label129;
                     }
                  }

                  var9 = 0;
               }

               var27.setImageCoords(var8, var9, var12, var3);
               if (this.isGif) {
                  this.autoDownload = DownloadController.getInstance(ArticleViewer.this.currentAccount).canDownloadMedia(4, this.currentDocument.size);
                  File var28 = FileLoader.getPathToAttach(this.currentDocument, true);
                  if (!this.autoDownload && !var28.exists()) {
                     this.imageView.setStrippedLocation(ImageLocation.getForDocument(this.currentDocument));
                     this.imageView.setImage((ImageLocation)null, (String)null, (ImageLocation)null, (String)null, ImageLocation.getForDocument(var21, this.currentDocument), "80_80_b", (Drawable)null, this.currentDocument.size, (String)null, ArticleViewer.this.currentPage, 1);
                  } else {
                     this.imageView.setStrippedLocation((ImageLocation)null);
                     this.imageView.setImage(ImageLocation.getForDocument(this.currentDocument), (String)null, (ImageLocation)null, (String)null, ImageLocation.getForDocument(var21, this.currentDocument), "80_80_b", (Drawable)null, this.currentDocument.size, (String)null, ArticleViewer.this.currentPage, 1);
                  }
               } else {
                  this.imageView.setStrippedLocation((ImageLocation)null);
                  this.imageView.setImage((ImageLocation)null, (String)null, ImageLocation.getForDocument(var21, this.currentDocument), "80_80_b", 0, (String)null, ArticleViewer.this.currentPage, 1);
               }

               this.imageView.setAspectFit(true);
               this.buttonX = (int)((float)this.imageView.getImageX() + (float)(this.imageView.getImageWidth() - var11) / 2.0F);
               this.buttonY = (int)((float)this.imageView.getImageY() + (float)(this.imageView.getImageHeight() - var11) / 2.0F);
               RadialProgress2 var22 = this.radialProgress;
               var3 = this.buttonX;
               var8 = this.buttonY;
               var22.setProgressRect(var3, var8, var3 + var11, var11 + var8);
               var3 = var4;
            }

            var4 = var3;
            if (this.currentType == 0) {
               ArticleViewer var23 = ArticleViewer.this;
               TLRPC.TL_pageBlockVideo var29 = this.currentBlock;
               this.captionLayout = var23.createLayoutForText(this, (CharSequence)null, var29.caption.text, var10, var29, this.parentAdapter);
               var8 = var3;
               if (this.captionLayout != null) {
                  this.creditOffset = AndroidUtilities.dp(4.0F) + this.captionLayout.getHeight();
                  var8 = var3 + this.creditOffset + AndroidUtilities.dp(4.0F);
               }

               ArticleViewer var17 = ArticleViewer.this;
               TLRPC.TL_pageBlockVideo var18 = this.currentBlock;
               TLRPC.RichText var30 = var18.caption.credit;
               Alignment var24;
               if (var17.isRtl) {
                  var24 = Alignment.ALIGN_RIGHT;
               } else {
                  var24 = Alignment.ALIGN_NORMAL;
               }

               this.creditLayout = var17.createLayoutForText(this, (CharSequence)null, var30, var10, var18, var24, this.parentAdapter);
               var4 = var8;
               if (this.creditLayout != null) {
                  var4 = var8 + AndroidUtilities.dp(4.0F) + this.creditLayout.getHeight();
               }
            }

            var3 = var4;
            if (!this.isFirst) {
               var3 = var4;
               if (this.currentType == 0) {
                  var3 = var4;
                  if (this.currentBlock.level <= 0) {
                     var3 = var4 + AndroidUtilities.dp(8.0F);
                  }
               }
            }

            boolean var25;
            label111: {
               if (this.parentBlock instanceof TLRPC.TL_pageBlockCover) {
                  var4 = this.parentAdapter.blocks.size();
                  var25 = true;
                  if (var4 > 1 && this.parentAdapter.blocks.get(1) instanceof TLRPC.TL_pageBlockChannel) {
                     break label111;
                  }
               }

               var25 = false;
            }

            var4 = var3;
            if (this.currentType != 2) {
               var4 = var3;
               if (!var25) {
                  var4 = var3 + AndroidUtilities.dp(8.0F);
               }
            }
         } else {
            var4 = 1;
         }

         this.channelCell.measure(var1, var2);
         this.channelCell.setTranslationY((float)(this.imageView.getImageHeight() - AndroidUtilities.dp(39.0F)));
         this.setMeasuredDimension(var5, var4);
      }

      public void onProgressDownload(String var1, float var2) {
         this.radialProgress.setProgress(var2, true);
         if (this.buttonState != 1) {
            this.updateButtonState(true);
         }

      }

      public void onProgressUpload(String var1, float var2, boolean var3) {
      }

      public void onSuccessDownload(String var1) {
         this.radialProgress.setProgress(1.0F, true);
         if (this.isGif) {
            this.buttonState = 2;
            this.didPressedButton(true);
         } else {
            this.updateButtonState(true);
         }

      }

      public boolean onTouchEvent(MotionEvent var1) {
         float var2 = var1.getX();
         float var3 = var1.getY();
         int var4 = this.channelCell.getVisibility();
         boolean var5 = false;
         if (var4 == 0 && var3 > this.channelCell.getTranslationY() && var3 < this.channelCell.getTranslationY() + (float)AndroidUtilities.dp(39.0F)) {
            if (ArticleViewer.this.channelBlock != null && var1.getAction() == 1) {
               MessagesController.getInstance(ArticleViewer.this.currentAccount).openByUserName(ArticleViewer.this.channelBlock.channel.username, ArticleViewer.this.parentFragment, 2);
               ArticleViewer.this.close(false, true);
            }

            return true;
         } else {
            if (var1.getAction() == 0 && this.imageView.isInsideImage(var2, var3)) {
               label62: {
                  label61: {
                     if (this.buttonState != -1) {
                        var4 = this.buttonX;
                        if (var2 >= (float)var4 && var2 <= (float)(var4 + AndroidUtilities.dp(48.0F))) {
                           var4 = this.buttonY;
                           if (var3 >= (float)var4 && var3 <= (float)(var4 + AndroidUtilities.dp(48.0F))) {
                              break label61;
                           }
                        }
                     }

                     if (this.buttonState != 0) {
                        this.photoPressed = true;
                        break label62;
                     }
                  }

                  this.buttonPressed = 1;
                  this.invalidate();
               }
            } else if (var1.getAction() == 1) {
               if (this.photoPressed) {
                  this.photoPressed = false;
                  ArticleViewer.this.openPhoto(this.currentBlock);
               } else if (this.buttonPressed == 1) {
                  this.buttonPressed = 0;
                  this.playSoundEffect(0);
                  this.didPressedButton(true);
                  this.invalidate();
               }
            } else if (var1.getAction() == 3) {
               this.photoPressed = false;
            }

            if (this.photoPressed || this.buttonPressed != 0 || ArticleViewer.this.checkLayoutForLinks(var1, this, this.captionLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(var1, this, this.creditLayout, this.textX, this.textY + this.creditOffset) || super.onTouchEvent(var1)) {
               var5 = true;
            }

            return var5;
         }
      }

      public void setBlock(TLRPC.TL_pageBlockVideo var1, boolean var2, boolean var3) {
         this.currentBlock = var1;
         this.parentBlock = null;
         this.cancelLoading = false;
         this.currentDocument = ArticleViewer.this.getDocumentWithId(this.currentBlock.video_id);
         this.isGif = MessageObject.isGifDocument(this.currentDocument);
         this.isFirst = var2;
         this.isLast = var3;
         this.channelCell.setVisibility(4);
         this.updateButtonState(false);
         this.requestLayout();
      }

      public void setParentBlock(TLRPC.PageBlock var1) {
         this.parentBlock = var1;
         if (ArticleViewer.this.channelBlock != null && this.parentBlock instanceof TLRPC.TL_pageBlockCover) {
            this.channelCell.setBlock(ArticleViewer.this.channelBlock);
            this.channelCell.setVisibility(0);
         }

      }

      public void updateButtonState(boolean var1) {
         String var2 = FileLoader.getAttachFileName(this.currentDocument);
         TLRPC.Document var3 = this.currentDocument;
         boolean var4 = true;
         boolean var5 = FileLoader.getPathToAttach(var3, true).exists();
         if (TextUtils.isEmpty(var2)) {
            this.radialProgress.setIcon(4, false, false);
         } else {
            if (var5) {
               DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver(this);
               if (!this.isGif) {
                  this.buttonState = 3;
               } else {
                  this.buttonState = -1;
               }

               this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
               this.invalidate();
            } else {
               DownloadController.getInstance(ArticleViewer.this.currentAccount).addLoadingFileObserver(var2, (MessageObject)null, this);
               var5 = FileLoader.getInstance(ArticleViewer.this.currentAccount).isLoadingFile(var2);
               float var6 = 0.0F;
               if (!var5) {
                  if (!this.cancelLoading && this.autoDownload && this.isGif) {
                     this.buttonState = 1;
                     var5 = var4;
                  } else {
                     this.buttonState = 0;
                     var5 = false;
                  }
               } else {
                  this.buttonState = 1;
                  Float var7 = ImageLoader.getInstance().getFileProgress(var2);
                  var5 = var4;
                  if (var7 != null) {
                     var6 = var7;
                     var5 = var4;
                  }
               }

               this.radialProgress.setIcon(this.getIconForCurrentState(), var5, var1);
               this.radialProgress.setProgress(var6, false);
               this.invalidate();
            }

         }
      }
   }

   class CheckForLongPress implements Runnable {
      public int currentPressCount;

      public void run() {
         if (ArticleViewer.this.checkingForLongPress && ArticleViewer.this.windowView != null) {
            ArticleViewer.this.checkingForLongPress = false;
            ArticleViewer var1;
            if (ArticleViewer.this.pressedLink != null) {
               ArticleViewer.this.windowView.performHapticFeedback(0);
               var1 = ArticleViewer.this;
               var1.showCopyPopup(var1.pressedLink.getUrl());
               ArticleViewer.this.pressedLink = null;
               ArticleViewer.this.pressedLinkOwnerLayout = null;
               if (ArticleViewer.this.pressedLinkOwnerView != null) {
                  ArticleViewer.this.pressedLinkOwnerView.invalidate();
               }
            } else if (ArticleViewer.this.pressedLinkOwnerLayout != null && ArticleViewer.this.pressedLinkOwnerView != null) {
               ArticleViewer.this.windowView.performHapticFeedback(0);
               int[] var4 = new int[2];
               ArticleViewer.this.pressedLinkOwnerView.getLocationInWindow(var4);
               int var2 = var4[1] + ArticleViewer.this.pressedLayoutY - AndroidUtilities.dp(54.0F);
               int var3 = var2;
               if (var2 < 0) {
                  var3 = 0;
               }

               ArticleViewer.this.pressedLinkOwnerView.invalidate();
               ArticleViewer.this.drawBlockSelection = true;
               var1 = ArticleViewer.this;
               var1.showPopup(var1.pressedLinkOwnerView, 48, 0, var3);
               ArticleViewer.this.listView[0].setLayoutFrozen(true);
               ArticleViewer.this.listView[0].setLayoutFrozen(false);
            }
         }

      }
   }

   private final class CheckForTap implements Runnable {
      private CheckForTap() {
      }

      // $FF: synthetic method
      CheckForTap(Object var2) {
         this();
      }

      public void run() {
         if (ArticleViewer.this.pendingCheckForLongPress == null) {
            ArticleViewer var1 = ArticleViewer.this;
            var1.pendingCheckForLongPress = var1.new CheckForLongPress();
         }

         ArticleViewer.this.pendingCheckForLongPress.currentPressCount = ArticleViewer.access$1104(ArticleViewer.this);
         if (ArticleViewer.this.windowView != null) {
            ArticleViewer.this.windowView.postDelayed(ArticleViewer.this.pendingCheckForLongPress, (long)(ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout()));
         }

      }
   }

   public class ColorCell extends FrameLayout {
      private int currentColor;
      private boolean selected;
      private TextView textView;

      public ColorCell(Context var2) {
         super(var2);
         if (ArticleViewer.colorPaint == null) {
            ArticleViewer.colorPaint = new Paint(1);
            ArticleViewer.selectorPaint = new Paint(1);
            ArticleViewer.selectorPaint.setColor(-15428119);
            ArticleViewer.selectorPaint.setStyle(Style.STROKE);
            ArticleViewer.selectorPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
         }

         this.setBackgroundDrawable(Theme.createSelectorDrawable(251658240, 2));
         this.setWillNotDraw(false);
         this.textView = new TextView(var2);
         this.textView.setTextColor(-14606047);
         this.textView.setTextSize(1, 16.0F);
         this.textView.setLines(1);
         this.textView.setMaxLines(1);
         this.textView.setSingleLine(true);
         TextView var8 = this.textView;
         boolean var3 = LocaleController.isRTL;
         byte var4 = 5;
         byte var5;
         if (var3) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var8.setGravity(var5 | 16);
         this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(1.0F));
         var8 = this.textView;
         if (LocaleController.isRTL) {
            var5 = var4;
         } else {
            var5 = 3;
         }

         var3 = LocaleController.isRTL;
         byte var6 = 17;
         if (var3) {
            var4 = 17;
         } else {
            var4 = 53;
         }

         float var7 = (float)var4;
         var4 = var6;
         if (LocaleController.isRTL) {
            var4 = 53;
         }

         this.addView(var8, LayoutHelper.createFrame(-1, -1.0F, var5 | 48, var7, 0.0F, (float)var4, 0.0F));
      }

      protected void onDraw(Canvas var1) {
         ArticleViewer.colorPaint.setColor(this.currentColor);
         int var2;
         if (!LocaleController.isRTL) {
            var2 = AndroidUtilities.dp(28.0F);
         } else {
            var2 = this.getMeasuredWidth() - AndroidUtilities.dp(28.0F);
         }

         var1.drawCircle((float)var2, (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(10.0F), ArticleViewer.colorPaint);
         if (this.selected) {
            ArticleViewer.selectorPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
            ArticleViewer.selectorPaint.setColor(-15428119);
            if (!LocaleController.isRTL) {
               var2 = AndroidUtilities.dp(28.0F);
            } else {
               var2 = this.getMeasuredWidth() - AndroidUtilities.dp(28.0F);
            }

            var1.drawCircle((float)var2, (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(10.0F), ArticleViewer.selectorPaint);
         } else if (this.currentColor == -1) {
            ArticleViewer.selectorPaint.setStrokeWidth((float)AndroidUtilities.dp(1.0F));
            ArticleViewer.selectorPaint.setColor(-4539718);
            if (!LocaleController.isRTL) {
               var2 = AndroidUtilities.dp(28.0F);
            } else {
               var2 = this.getMeasuredWidth() - AndroidUtilities.dp(28.0F);
            }

            var1.drawCircle((float)var2, (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(9.0F), ArticleViewer.selectorPaint);
         }

      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824));
      }

      public void select(boolean var1) {
         if (this.selected != var1) {
            this.selected = var1;
            this.invalidate();
         }
      }

      public void setTextAndColor(String var1, int var2) {
         this.textView.setText(var1);
         this.currentColor = var2;
         this.invalidate();
      }
   }

   public class DrawingText {
      public LinkPath markPath;
      public StaticLayout textLayout;
      public LinkPath textPath;

      public void draw(Canvas var1) {
         LinkPath var2 = this.textPath;
         if (var2 != null) {
            var1.drawPath(var2, ArticleViewer.webpageUrlPaint);
         }

         var2 = this.markPath;
         if (var2 != null) {
            var1.drawPath(var2, ArticleViewer.webpageMarkPaint);
         }

         ArticleViewer.this.drawLayoutLink(var1, this);
         this.textLayout.draw(var1);
      }

      public int getHeight() {
         return this.textLayout.getHeight();
      }

      public int getLineAscent(int var1) {
         return this.textLayout.getLineAscent(var1);
      }

      public int getLineCount() {
         return this.textLayout.getLineCount();
      }

      public float getLineLeft(int var1) {
         return this.textLayout.getLineLeft(var1);
      }

      public float getLineWidth(int var1) {
         return this.textLayout.getLineWidth(var1);
      }

      public CharSequence getText() {
         return this.textLayout.getText();
      }

      public int getWidth() {
         return this.textLayout.getWidth();
      }
   }

   public class FontCell extends FrameLayout {
      private TextView textView;
      private TextView textView2;

      public FontCell(Context var2) {
         super(var2);
         this.setBackgroundDrawable(Theme.createSelectorDrawable(251658240, 2));
         this.textView = new TextView(var2);
         this.textView.setTextColor(-14606047);
         this.textView.setTextSize(1, 16.0F);
         this.textView.setLines(1);
         this.textView.setMaxLines(1);
         this.textView.setSingleLine(true);
         TextView var9 = this.textView;
         boolean var3 = LocaleController.isRTL;
         byte var4 = 5;
         byte var5;
         if (var3) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var9.setGravity(var5 | 16);
         var9 = this.textView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var3 = LocaleController.isRTL;
         byte var6 = 53;
         byte var7;
         if (var3) {
            var7 = 17;
         } else {
            var7 = 53;
         }

         float var8 = (float)var7;
         if (LocaleController.isRTL) {
            var7 = var6;
         } else {
            var7 = 17;
         }

         this.addView(var9, LayoutHelper.createFrame(-1, -1.0F, var5 | 48, var8, 0.0F, (float)var7, 0.0F));
         this.textView2 = new TextView(var2);
         this.textView2.setTextColor(-14606047);
         this.textView2.setTextSize(1, 16.0F);
         this.textView2.setLines(1);
         this.textView2.setMaxLines(1);
         this.textView2.setSingleLine(true);
         this.textView2.setText("Aa");
         var9 = this.textView2;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var9.setGravity(var5 | 16);
         var9 = this.textView2;
         if (LocaleController.isRTL) {
            var5 = var4;
         } else {
            var5 = 3;
         }

         this.addView(var9, LayoutHelper.createFrame(-1, -1.0F, var5 | 48, 17.0F, 0.0F, 17.0F, 0.0F));
      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824));
      }

      public void select(boolean var1) {
         TextView var2 = this.textView2;
         int var3;
         if (var1) {
            var3 = -15428119;
         } else {
            var3 = -14606047;
         }

         var2.setTextColor(var3);
      }

      public void setTextAndTypeface(String var1, Typeface var2) {
         this.textView.setText(var1);
         this.textView.setTypeface(var2);
         this.textView2.setTypeface(var2);
         this.invalidate();
      }
   }

   private class FrameLayoutDrawer extends FrameLayout {
      public FrameLayoutDrawer(Context var2) {
         super(var2);
      }

      protected boolean drawChild(Canvas var1, View var2, long var3) {
         boolean var5;
         if (var2 != ArticleViewer.this.aspectRatioFrameLayout && super.drawChild(var1, var2, var3)) {
            var5 = true;
         } else {
            var5 = false;
         }

         return var5;
      }

      protected void onDraw(Canvas var1) {
         ArticleViewer.this.drawContent(var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         ArticleViewer.this.processTouchEvent(var1);
         return true;
      }
   }

   private class PhotoBackgroundDrawable extends ColorDrawable {
      private Runnable drawRunnable;

      public PhotoBackgroundDrawable(int var2) {
         super(var2);
      }

      public void draw(Canvas var1) {
         super.draw(var1);
         if (this.getAlpha() != 0) {
            Runnable var2 = this.drawRunnable;
            if (var2 != null) {
               var2.run();
               this.drawRunnable = null;
            }
         }

      }

      @Keep
      public void setAlpha(int var1) {
         if (ArticleViewer.this.parentActivity instanceof LaunchActivity) {
            DrawerLayoutContainer var2 = ((LaunchActivity)ArticleViewer.this.parentActivity).drawerLayoutContainer;
            boolean var3;
            if (ArticleViewer.this.isPhotoVisible && var1 == 255) {
               var3 = false;
            } else {
               var3 = true;
            }

            var2.setAllowDrawContent(var3);
         }

         super.setAlpha(var1);
      }
   }

   public static class PlaceProviderObject {
      public int clipBottomAddition;
      public int clipTopAddition;
      public ImageReceiver imageReceiver;
      public int index;
      public View parentView;
      public int radius;
      public float scale = 1.0F;
      public int size;
      public ImageReceiver.BitmapHolder thumb;
      public int viewX;
      public int viewY;
   }

   private class RadialProgressView {
      private float alpha = 1.0F;
      private float animatedAlphaValue = 1.0F;
      private float animatedProgressValue = 0.0F;
      private float animationProgressStart = 0.0F;
      private int backgroundState = -1;
      private float currentProgress = 0.0F;
      private long currentProgressTime = 0L;
      private long lastUpdateTime = 0L;
      private View parent;
      private int previousBackgroundState = -2;
      private RectF progressRect = new RectF();
      private float radOffset = 0.0F;
      private float scale = 1.0F;
      private int size = AndroidUtilities.dp(64.0F);

      public RadialProgressView(Context var2, View var3) {
         if (ArticleViewer.decelerateInterpolator == null) {
            ArticleViewer.decelerateInterpolator = new DecelerateInterpolator(1.5F);
            ArticleViewer.progressPaint = new Paint(1);
            ArticleViewer.progressPaint.setStyle(Style.STROKE);
            ArticleViewer.progressPaint.setStrokeCap(Cap.ROUND);
            ArticleViewer.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(3.0F));
            ArticleViewer.progressPaint.setColor(-1);
         }

         this.parent = var3;
      }

      private void updateAnimation() {
         long var1 = System.currentTimeMillis();
         long var3 = var1 - this.lastUpdateTime;
         this.lastUpdateTime = var1;
         if (this.animatedProgressValue != 1.0F) {
            this.radOffset += (float)(360L * var3) / 3000.0F;
            float var5 = this.currentProgress;
            float var6 = this.animationProgressStart;
            float var7 = var5 - var6;
            if (var7 > 0.0F) {
               this.currentProgressTime += var3;
               if (this.currentProgressTime >= 300L) {
                  this.animatedProgressValue = var5;
                  this.animationProgressStart = var5;
                  this.currentProgressTime = 0L;
               } else {
                  this.animatedProgressValue = var6 + var7 * ArticleViewer.decelerateInterpolator.getInterpolation((float)this.currentProgressTime / 300.0F);
               }
            }

            this.parent.invalidate();
         }

         if (this.animatedProgressValue >= 1.0F && this.previousBackgroundState != -2) {
            this.animatedAlphaValue -= (float)var3 / 200.0F;
            if (this.animatedAlphaValue <= 0.0F) {
               this.animatedAlphaValue = 0.0F;
               this.previousBackgroundState = -2;
            }

            this.parent.invalidate();
         }

      }

      public void onDraw(Canvas var1) {
         int var2 = (int)((float)this.size * this.scale);
         int var3 = (ArticleViewer.this.getContainerViewWidth() - var2) / 2;
         int var4 = (ArticleViewer.this.getContainerViewHeight() - var2) / 2;
         int var5 = this.previousBackgroundState;
         Drawable var6;
         if (var5 >= 0 && var5 < 4) {
            var6 = ArticleViewer.progressDrawables[this.previousBackgroundState];
            if (var6 != null) {
               var6.setAlpha((int)(this.animatedAlphaValue * 255.0F * this.alpha));
               var6.setBounds(var3, var4, var3 + var2, var4 + var2);
               var6.draw(var1);
            }
         }

         var5 = this.backgroundState;
         if (var5 >= 0 && var5 < 4) {
            var6 = ArticleViewer.progressDrawables[this.backgroundState];
            if (var6 != null) {
               if (this.previousBackgroundState != -2) {
                  var6.setAlpha((int)((1.0F - this.animatedAlphaValue) * 255.0F * this.alpha));
               } else {
                  var6.setAlpha((int)(this.alpha * 255.0F));
               }

               var6.setBounds(var3, var4, var3 + var2, var4 + var2);
               var6.draw(var1);
            }
         }

         var5 = this.backgroundState;
         if (var5 != 0 && var5 != 1) {
            var5 = this.previousBackgroundState;
            if (var5 != 0 && var5 != 1) {
               return;
            }
         }

         var5 = AndroidUtilities.dp(4.0F);
         if (this.previousBackgroundState != -2) {
            ArticleViewer.progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0F * this.alpha));
         } else {
            ArticleViewer.progressPaint.setAlpha((int)(this.alpha * 255.0F));
         }

         this.progressRect.set((float)(var3 + var5), (float)(var4 + var5), (float)(var3 + var2 - var5), (float)(var4 + var2 - var5));
         var1.drawArc(this.progressRect, this.radOffset - 90.0F, Math.max(4.0F, this.animatedProgressValue * 360.0F), false, ArticleViewer.progressPaint);
         this.updateAnimation();
      }

      public void setAlpha(float var1) {
         this.alpha = var1;
      }

      public void setBackgroundState(int var1, boolean var2) {
         label12: {
            this.lastUpdateTime = System.currentTimeMillis();
            if (var2) {
               int var3 = this.backgroundState;
               if (var3 != var1) {
                  this.previousBackgroundState = var3;
                  this.animatedAlphaValue = 1.0F;
                  break label12;
               }
            }

            this.previousBackgroundState = -2;
         }

         this.backgroundState = var1;
         this.parent.invalidate();
      }

      public void setProgress(float var1, boolean var2) {
         if (!var2) {
            this.animatedProgressValue = var1;
            this.animationProgressStart = var1;
         } else {
            this.animationProgressStart = this.animatedProgressValue;
         }

         this.currentProgress = var1;
         this.currentProgressTime = 0L;
      }

      public void setScale(float var1) {
         this.scale = var1;
      }
   }

   public class ScrollEvaluator extends IntEvaluator {
      public Integer evaluate(float var1, Integer var2, Integer var3) {
         return super.evaluate(var1, var2, var3);
      }
   }

   private class SizeChooseView extends View {
      private int circleSize;
      private int gapSize;
      private int lineSize;
      private boolean moving;
      private Paint paint = new Paint(1);
      private int sideSide;
      private boolean startMoving;
      private int startMovingQuality;
      private float startX;

      public SizeChooseView(Context var2) {
         super(var2);
      }

      protected void onDraw(Canvas var1) {
         int var2 = this.getMeasuredHeight() / 2;

         for(int var3 = 0; var3 < 5; ++var3) {
            int var4 = this.sideSide;
            int var5 = this.lineSize;
            int var6 = this.gapSize;
            int var7 = this.circleSize;
            var7 = var4 + (var5 + var6 * 2 + var7) * var3 + var7 / 2;
            if (var3 <= ArticleViewer.this.selectedFontSize) {
               this.paint.setColor(-15428119);
            } else {
               this.paint.setColor(-3355444);
            }

            float var8 = (float)var7;
            float var9 = (float)var2;
            if (var3 == ArticleViewer.this.selectedFontSize) {
               var5 = AndroidUtilities.dp(4.0F);
            } else {
               var5 = this.circleSize / 2;
            }

            var1.drawCircle(var8, var9, (float)var5, this.paint);
            if (var3 != 0) {
               var5 = var7 - this.circleSize / 2 - this.gapSize - this.lineSize;
               var1.drawRect((float)var5, (float)(var2 - AndroidUtilities.dp(1.0F)), (float)(var5 + this.lineSize), (float)(AndroidUtilities.dp(1.0F) + var2), this.paint);
            }
         }

      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(var1, var2);
         MeasureSpec.getSize(var1);
         this.circleSize = AndroidUtilities.dp(5.0F);
         this.gapSize = AndroidUtilities.dp(2.0F);
         this.sideSide = AndroidUtilities.dp(17.0F);
         this.lineSize = (this.getMeasuredWidth() - this.circleSize * 5 - this.gapSize * 2 * 4 - this.sideSide * 2) / 4;
      }

      public boolean onTouchEvent(MotionEvent var1) {
         float var2 = var1.getX();
         int var3 = var1.getAction();
         int var4 = 0;
         boolean var5 = false;
         int var6;
         int var7;
         int var8;
         if (var3 == 0) {
            this.getParent().requestDisallowInterceptTouchEvent(true);

            for(var4 = 0; var4 < 5; ++var4) {
               var6 = this.sideSide;
               var7 = this.lineSize;
               var8 = this.gapSize;
               var3 = this.circleSize;
               var3 = var6 + (var7 + var8 * 2 + var3) * var4 + var3 / 2;
               if (var2 > (float)(var3 - AndroidUtilities.dp(15.0F)) && var2 < (float)(var3 + AndroidUtilities.dp(15.0F))) {
                  if (var4 == ArticleViewer.this.selectedFontSize) {
                     var5 = true;
                  }

                  this.startMoving = var5;
                  this.startX = var2;
                  this.startMovingQuality = ArticleViewer.this.selectedFontSize;
                  break;
               }
            }
         } else if (var1.getAction() == 2) {
            if (this.startMoving) {
               if (Math.abs(this.startX - var2) >= AndroidUtilities.getPixelsInCM(0.5F, true)) {
                  this.moving = true;
                  this.startMoving = false;
               }
            } else if (this.moving) {
               while(var4 < 5) {
                  var8 = this.sideSide;
                  var3 = this.lineSize;
                  var6 = this.gapSize;
                  var7 = this.circleSize;
                  var8 = var8 + (var6 * 2 + var3 + var7) * var4 + var7 / 2;
                  var3 = var3 / 2 + var7 / 2 + var6;
                  if (var2 > (float)(var8 - var3) && var2 < (float)(var8 + var3)) {
                     if (ArticleViewer.this.selectedFontSize != var4) {
                        ArticleViewer.this.selectedFontSize = var4;
                        ArticleViewer.this.updatePaintSize();
                        this.invalidate();
                     }
                     break;
                  }

                  ++var4;
               }
            }
         } else if (var1.getAction() == 1 || var1.getAction() == 3) {
            if (!this.moving) {
               for(var4 = 0; var4 < 5; ++var4) {
                  var3 = this.sideSide;
                  var8 = this.lineSize;
                  var7 = this.gapSize;
                  var6 = this.circleSize;
                  var3 = var3 + (var8 + var7 * 2 + var6) * var4 + var6 / 2;
                  if (var2 > (float)(var3 - AndroidUtilities.dp(15.0F)) && var2 < (float)(var3 + AndroidUtilities.dp(15.0F))) {
                     if (ArticleViewer.this.selectedFontSize != var4) {
                        ArticleViewer.this.selectedFontSize = var4;
                        ArticleViewer.this.updatePaintSize();
                        this.invalidate();
                     }
                     break;
                  }
               }
            } else if (ArticleViewer.this.selectedFontSize != this.startMovingQuality) {
               ArticleViewer.this.updatePaintSize();
            }

            this.startMoving = false;
            this.moving = false;
         }

         return true;
      }
   }

   private class TL_pageBlockDetailsBottom extends TLRPC.PageBlock {
      private TLRPC.TL_pageBlockDetails parent;
   }

   private class TL_pageBlockDetailsChild extends TLRPC.PageBlock {
      private TLRPC.PageBlock block;
      private TLRPC.PageBlock parent;

      private TL_pageBlockDetailsChild() {
      }

      // $FF: synthetic method
      TL_pageBlockDetailsChild(Object var2) {
         this();
      }
   }

   private class TL_pageBlockEmbedPostCaption extends TLRPC.TL_pageBlockEmbedPost {
      private TLRPC.TL_pageBlockEmbedPost parent;

      private TL_pageBlockEmbedPostCaption() {
      }

      // $FF: synthetic method
      TL_pageBlockEmbedPostCaption(Object var2) {
         this();
      }
   }

   private class TL_pageBlockListItem extends TLRPC.PageBlock {
      private TLRPC.PageBlock blockItem;
      private int index;
      private String num;
      private ArticleViewer.DrawingText numLayout;
      private ArticleViewer.TL_pageBlockListParent parent;
      private TLRPC.RichText textItem;

      private TL_pageBlockListItem() {
         this.index = Integer.MAX_VALUE;
      }

      // $FF: synthetic method
      TL_pageBlockListItem(Object var2) {
         this();
      }
   }

   private class TL_pageBlockListParent extends TLRPC.PageBlock {
      private ArrayList items;
      private int lastFontSize;
      private int lastMaxNumCalcWidth;
      private int level;
      private int maxNumWidth;
      private TLRPC.TL_pageBlockList pageBlockList;

      private TL_pageBlockListParent() {
         this.items = new ArrayList();
      }

      // $FF: synthetic method
      TL_pageBlockListParent(Object var2) {
         this();
      }
   }

   private class TL_pageBlockOrderedListItem extends TLRPC.PageBlock {
      private TLRPC.PageBlock blockItem;
      private int index;
      private String num;
      private ArticleViewer.DrawingText numLayout;
      private ArticleViewer.TL_pageBlockOrderedListParent parent;
      private TLRPC.RichText textItem;

      private TL_pageBlockOrderedListItem() {
         this.index = Integer.MAX_VALUE;
      }

      // $FF: synthetic method
      TL_pageBlockOrderedListItem(Object var2) {
         this();
      }
   }

   private class TL_pageBlockOrderedListParent extends TLRPC.PageBlock {
      private ArrayList items;
      private int lastFontSize;
      private int lastMaxNumCalcWidth;
      private int level;
      private int maxNumWidth;
      private TLRPC.TL_pageBlockOrderedList pageBlockOrderedList;

      private TL_pageBlockOrderedListParent() {
         this.items = new ArrayList();
      }

      // $FF: synthetic method
      TL_pageBlockOrderedListParent(Object var2) {
         this();
      }
   }

   private class TL_pageBlockRelatedArticlesChild extends TLRPC.PageBlock {
      private int num;
      private TLRPC.TL_pageBlockRelatedArticles parent;

      private TL_pageBlockRelatedArticlesChild() {
      }

      // $FF: synthetic method
      TL_pageBlockRelatedArticlesChild(Object var2) {
         this();
      }
   }

   private class TL_pageBlockRelatedArticlesShadow extends TLRPC.PageBlock {
      private TLRPC.TL_pageBlockRelatedArticles parent;

      private TL_pageBlockRelatedArticlesShadow() {
      }

      // $FF: synthetic method
      TL_pageBlockRelatedArticlesShadow(Object var2) {
         this();
      }
   }

   private class WebpageAdapter extends RecyclerListView.SelectionAdapter {
      private HashMap anchors = new HashMap();
      private HashMap anchorsOffset = new HashMap();
      private HashMap anchorsParent = new HashMap();
      private HashMap audioBlocks = new HashMap();
      private ArrayList audioMessages = new ArrayList();
      private ArrayList blocks = new ArrayList();
      private Context context;
      private ArrayList localBlocks = new ArrayList();
      private ArrayList photoBlocks = new ArrayList();

      public WebpageAdapter(Context var2) {
         this.context = var2;
      }

      private void addAllMediaFromBlock(TLRPC.PageBlock var1) {
         if (var1 instanceof TLRPC.TL_pageBlockPhoto) {
            TLRPC.TL_pageBlockPhoto var2 = (TLRPC.TL_pageBlockPhoto)var1;
            TLRPC.Photo var3 = ArticleViewer.this.getPhotoWithId(var2.photo_id);
            if (var3 != null) {
               var2.thumb = FileLoader.getClosestPhotoSizeWithSize(var3.sizes, 56, true);
               var2.thumbObject = var3;
               this.photoBlocks.add(var1);
            }
         } else if (var1 instanceof TLRPC.TL_pageBlockVideo && ArticleViewer.this.isVideoBlock(var1)) {
            TLRPC.TL_pageBlockVideo var11 = (TLRPC.TL_pageBlockVideo)var1;
            TLRPC.Document var12 = ArticleViewer.this.getDocumentWithId(var11.video_id);
            if (var12 != null) {
               var11.thumb = FileLoader.getClosestPhotoSizeWithSize(var12.thumbs, 56, true);
               var11.thumbObject = var12;
               this.photoBlocks.add(var1);
            }
         } else {
            boolean var4 = var1 instanceof TLRPC.TL_pageBlockSlideshow;
            byte var5 = 0;
            int var6 = 0;
            if (var4) {
               TLRPC.TL_pageBlockSlideshow var9 = (TLRPC.TL_pageBlockSlideshow)var1;

               for(int var13 = var9.items.size(); var6 < var13; ++var6) {
                  var1 = (TLRPC.PageBlock)var9.items.get(var6);
                  var1.groupId = ArticleViewer.this.lastBlockNum;
                  this.addAllMediaFromBlock(var1);
               }

               ArticleViewer.access$13008(ArticleViewer.this);
            } else if (var1 instanceof TLRPC.TL_pageBlockCollage) {
               TLRPC.TL_pageBlockCollage var8 = (TLRPC.TL_pageBlockCollage)var1;
               int var7 = var8.items.size();

               for(var6 = var5; var6 < var7; ++var6) {
                  TLRPC.PageBlock var10 = (TLRPC.PageBlock)var8.items.get(var6);
                  var10.groupId = ArticleViewer.this.lastBlockNum;
                  this.addAllMediaFromBlock(var10);
               }

               ArticleViewer.access$13008(ArticleViewer.this);
            } else if (var1 instanceof TLRPC.TL_pageBlockCover) {
               this.addAllMediaFromBlock(((TLRPC.TL_pageBlockCover)var1).cover);
            }
         }

      }

      private void addBlock(TLRPC.PageBlock var1, int var2, int var3, int var4) {
         TLRPC.PageBlock var5 = var1;
         boolean var6 = var1 instanceof ArticleViewer.TL_pageBlockDetailsChild;
         TLRPC.PageBlock var7;
         if (var6) {
            var7 = ((ArticleViewer.TL_pageBlockDetailsChild)var1).block;
         } else {
            var7 = var1;
         }

         if (!(var7 instanceof TLRPC.TL_pageBlockList) && !(var7 instanceof TLRPC.TL_pageBlockOrderedList)) {
            this.setRichTextParents(var7);
            this.addAllMediaFromBlock(var7);
         }

         TLRPC.PageBlock var8 = ArticleViewer.this.getLastNonListPageBlock(var7);
         if (!(var8 instanceof TLRPC.TL_pageBlockUnsupported)) {
            if (var8 instanceof TLRPC.TL_pageBlockAnchor) {
               this.anchors.put(((TLRPC.TL_pageBlockAnchor)var8).name.toLowerCase(), this.blocks.size());
            } else {
               boolean var9 = var8 instanceof TLRPC.TL_pageBlockList;
               if (!var9 && !(var8 instanceof TLRPC.TL_pageBlockOrderedList)) {
                  this.blocks.add(var1);
               }

               boolean var10 = var8 instanceof TLRPC.TL_pageBlockAudio;
               byte var11 = 0;
               byte var12 = 0;
               byte var13 = 0;
               if (var10) {
                  TLRPC.TL_pageBlockAudio var22 = (TLRPC.TL_pageBlockAudio)var8;
                  TLRPC.TL_message var27 = new TLRPC.TL_message();
                  var27.out = true;
                  var2 = -Long.valueOf(var22.audio_id).hashCode();
                  var8.mid = var2;
                  var27.id = var2;
                  var27.to_id = new TLRPC.TL_peerUser();
                  TLRPC.Peer var33 = var27.to_id;
                  var2 = UserConfig.getInstance(ArticleViewer.this.currentAccount).getClientUserId();
                  var27.from_id = var2;
                  var33.user_id = var2;
                  var27.date = (int)(System.currentTimeMillis() / 1000L);
                  var27.message = "";
                  var27.media = new TLRPC.TL_messageMediaDocument();
                  var27.media.webpage = ArticleViewer.this.currentPage;
                  TLRPC.MessageMedia var35 = var27.media;
                  var35.flags |= 3;
                  var35.document = ArticleViewer.this.getDocumentWithId(var22.audio_id);
                  var27.flags |= 768;
                  MessageObject var28 = new MessageObject(UserConfig.selectedAccount, var27, false);
                  this.audioMessages.add(var28);
                  this.audioBlocks.put(var22, var28);
               } else if (var8 instanceof TLRPC.TL_pageBlockEmbedPost) {
                  TLRPC.TL_pageBlockEmbedPost var23 = (TLRPC.TL_pageBlockEmbedPost)var8;
                  if (!var23.blocks.isEmpty()) {
                     var8.level = -1;

                     for(var2 = var13; var2 < var23.blocks.size(); ++var2) {
                        var5 = (TLRPC.PageBlock)var23.blocks.get(var2);
                        if (!(var5 instanceof TLRPC.TL_pageBlockUnsupported)) {
                           if (var5 instanceof TLRPC.TL_pageBlockAnchor) {
                              TLRPC.TL_pageBlockAnchor var29 = (TLRPC.TL_pageBlockAnchor)var5;
                              this.anchors.put(var29.name.toLowerCase(), this.blocks.size());
                           } else {
                              var5.level = 1;
                              if (var2 == var23.blocks.size() - 1) {
                                 var5.bottom = true;
                              }

                              this.blocks.add(var5);
                              this.addAllMediaFromBlock(var5);
                           }
                        }
                     }

                     if (!TextUtils.isEmpty(ArticleViewer.getPlainText(var23.caption.text)) || !TextUtils.isEmpty(ArticleViewer.getPlainText(var23.caption.credit))) {
                        ArticleViewer.TL_pageBlockEmbedPostCaption var30 = ArticleViewer.this.new TL_pageBlockEmbedPostCaption();
                        var30.parent = var23;
                        var30.caption = var23.caption;
                        this.blocks.add(var30);
                     }
                  }
               } else if (var8 instanceof TLRPC.TL_pageBlockRelatedArticles) {
                  TLRPC.TL_pageBlockRelatedArticles var24 = (TLRPC.TL_pageBlockRelatedArticles)var8;
                  ArticleViewer.TL_pageBlockRelatedArticlesShadow var31 = ArticleViewer.this.new TL_pageBlockRelatedArticlesShadow();
                  var31.parent = var24;
                  ArrayList var37 = this.blocks;
                  var37.add(var37.size() - 1, var31);
                  var3 = var24.articles.size();

                  for(var2 = var11; var2 < var3; ++var2) {
                     ArticleViewer.TL_pageBlockRelatedArticlesChild var32 = ArticleViewer.this.new TL_pageBlockRelatedArticlesChild();
                     var32.parent = var24;
                     var32.num = var2;
                     this.blocks.add(var32);
                  }

                  if (var4 == 0) {
                     var31 = ArticleViewer.this.new TL_pageBlockRelatedArticlesShadow();
                     var31.parent = var24;
                     this.blocks.add(var31);
                  }
               } else {
                  int var46;
                  int var48;
                  if (var8 instanceof TLRPC.TL_pageBlockDetails) {
                     TLRPC.TL_pageBlockDetails var25 = (TLRPC.TL_pageBlockDetails)var8;
                     var48 = var25.blocks.size();

                     for(var46 = var12; var46 < var48; ++var46) {
                        ArticleViewer.TL_pageBlockDetailsChild var39 = ArticleViewer.this.new TL_pageBlockDetailsChild();
                        var39.parent = var5;
                        var39.block = (TLRPC.PageBlock)var25.blocks.get(var46);
                        this.addBlock(ArticleViewer.this.wrapInTableBlock(var5, var39), var2 + 1, var3, var4);
                     }
                  } else {
                     String var42 = " ";
                     String var14 = ".%d";
                     ArticleViewer.TL_pageBlockDetailsChild var17;
                     int var18;
                     int var47;
                     ArticleViewer.TL_pageBlockDetailsChild var53;
                     if (var9) {
                        TLRPC.TL_pageBlockList var49 = (TLRPC.TL_pageBlockList)var8;
                        ArticleViewer.TL_pageBlockListParent var15 = ArticleViewer.this.new TL_pageBlockListParent();
                        var15.pageBlockList = var49;
                        var15.level = var3;
                        var46 = var49.items.size();
                        var47 = 0;
                        String var26 = var42;

                        for(TLRPC.TL_pageBlockList var44 = var49; var47 < var46; ++var47) {
                           TLRPC.PageListItem var36 = (TLRPC.PageListItem)var44.items.get(var47);
                           ArticleViewer.TL_pageBlockListItem var16 = ArticleViewer.this.new TL_pageBlockListItem();
                           var16.index = var47;
                           var16.parent = var15;
                           if (var44.ordered) {
                              if (ArticleViewer.this.isRtl) {
                                 var16.num = String.format(".%d", var47 + 1);
                              } else {
                                 var16.num = String.format("%d.", var47 + 1);
                              }
                           } else {
                              var16.num = "•";
                           }

                           var15.items.add(var16);
                           Object var50;
                           TLRPC.TL_pageListItemBlocks var52;
                           if (var36 instanceof TLRPC.TL_pageListItemText) {
                              var16.textItem = ((TLRPC.TL_pageListItemText)var36).text;
                              var50 = var36;
                           } else {
                              var50 = var36;
                              if (var36 instanceof TLRPC.TL_pageListItemBlocks) {
                                 var52 = (TLRPC.TL_pageListItemBlocks)var36;
                                 if (!var52.blocks.isEmpty()) {
                                    var16.blockItem = (TLRPC.PageBlock)var52.blocks.get(0);
                                    var50 = var36;
                                 } else {
                                    var50 = new TLRPC.TL_pageListItemText();
                                    TLRPC.TL_textPlain var38 = new TLRPC.TL_textPlain();
                                    var38.text = var26;
                                    ((TLRPC.TL_pageListItemText)var50).text = var38;
                                 }
                              }
                           }

                           ArticleViewer.TL_pageBlockDetailsChild var40;
                           if (var6) {
                              var17 = (ArticleViewer.TL_pageBlockDetailsChild)var5;
                              var40 = ArticleViewer.this.new TL_pageBlockDetailsChild();
                              var40.parent = var17.parent;
                              var40.block = var16;
                              this.addBlock(var40, var2, var3 + 1, var4);
                           } else {
                              Object var43 = var16;
                              if (var47 == 0) {
                                 var43 = ArticleViewer.this.fixListBlock(var5, var16);
                              }

                              this.addBlock((TLRPC.PageBlock)var43, var2, var3 + 1, var4);
                           }

                           if (var50 instanceof TLRPC.TL_pageListItemBlocks) {
                              var52 = (TLRPC.TL_pageListItemBlocks)var50;
                              var18 = var52.blocks.size();

                              for(var48 = 1; var48 < var18; ++var48) {
                                 ArticleViewer.TL_pageBlockListItem var56 = ArticleViewer.this.new TL_pageBlockListItem();
                                 var56.blockItem = (TLRPC.PageBlock)var52.blocks.get(var48);
                                 var56.parent = var15;
                                 if (var6) {
                                    var40 = (ArticleViewer.TL_pageBlockDetailsChild)var5;
                                    var53 = ArticleViewer.this.new TL_pageBlockDetailsChild();
                                    var53.parent = var40.parent;
                                    var53.block = var56;
                                    this.addBlock(var53, var2, var3 + 1, var4);
                                 } else {
                                    this.addBlock(var56, var2, var3 + 1, var4);
                                 }

                                 var15.items.add(var56);
                              }
                           }
                        }
                     } else {
                        var42 = " ";
                        if (var8 instanceof TLRPC.TL_pageBlockOrderedList) {
                           TLRPC.TL_pageBlockOrderedList var45 = (TLRPC.TL_pageBlockOrderedList)var8;
                           ArticleViewer.TL_pageBlockOrderedListParent var19 = ArticleViewer.this.new TL_pageBlockOrderedListParent();
                           var19.pageBlockOrderedList = var45;
                           var19.level = var3;
                           var47 = var45.items.size();

                           for(var46 = 0; var46 < var47; var6 = var9) {
                              Object var34 = (TLRPC.PageListOrderedItem)var45.items.get(var46);
                              ArticleViewer.TL_pageBlockOrderedListItem var51 = ArticleViewer.this.new TL_pageBlockOrderedListItem();
                              var51.index = var46;
                              var51.parent = var19;
                              var19.items.add(var51);
                              StringBuilder var20;
                              TLRPC.TL_pageListOrderedItemBlocks var55;
                              if (var34 instanceof TLRPC.TL_pageListOrderedItemText) {
                                 TLRPC.TL_pageListOrderedItemText var54 = (TLRPC.TL_pageListOrderedItemText)var34;
                                 var51.textItem = var54.text;
                                 if (TextUtils.isEmpty(var54.num)) {
                                    if (ArticleViewer.this.isRtl) {
                                       var51.num = String.format(var14, var46 + 1);
                                    } else {
                                       var51.num = String.format("%d.", var46 + 1);
                                    }
                                 } else if (ArticleViewer.this.isRtl) {
                                    var20 = new StringBuilder();
                                    var20.append(".");
                                    var20.append(var54.num);
                                    var51.num = var20.toString();
                                 } else {
                                    var20 = new StringBuilder();
                                    var20.append(var54.num);
                                    var20.append(".");
                                    var51.num = var20.toString();
                                 }
                              } else if (var34 instanceof TLRPC.TL_pageListOrderedItemBlocks) {
                                 var55 = (TLRPC.TL_pageListOrderedItemBlocks)var34;
                                 if (!var55.blocks.isEmpty()) {
                                    var51.blockItem = (TLRPC.PageBlock)var55.blocks.get(0);
                                 } else {
                                    var34 = new TLRPC.TL_pageListOrderedItemText();
                                    TLRPC.TL_textPlain var58 = new TLRPC.TL_textPlain();
                                    var58.text = var42;
                                    ((TLRPC.TL_pageListOrderedItemText)var34).text = var58;
                                 }

                                 if (TextUtils.isEmpty(var55.num)) {
                                    if (ArticleViewer.this.isRtl) {
                                       var51.num = String.format(var14, var46 + 1);
                                    } else {
                                       var51.num = String.format("%d.", var46 + 1);
                                    }
                                 } else if (ArticleViewer.this.isRtl) {
                                    var20 = new StringBuilder();
                                    var20.append(".");
                                    var20.append(var55.num);
                                    var51.num = var20.toString();
                                 } else {
                                    var20 = new StringBuilder();
                                    var20.append(var55.num);
                                    var20.append(".");
                                    var51.num = var20.toString();
                                 }
                              }

                              if (var6) {
                                 var53 = (ArticleViewer.TL_pageBlockDetailsChild)var1;
                                 var17 = ArticleViewer.this.new TL_pageBlockDetailsChild();
                                 var17.parent = var53.parent;
                                 var17.block = var51;
                                 this.addBlock(var17, var2, var3 + 1, var4);
                              } else {
                                 Object var57 = var51;
                                 if (var46 == 0) {
                                    var57 = ArticleViewer.this.fixListBlock(var1, var51);
                                 }

                                 this.addBlock((TLRPC.PageBlock)var57, var2, var3 + 1, var4);
                              }

                              var9 = var6;
                              if (var34 instanceof TLRPC.TL_pageListOrderedItemBlocks) {
                                 var55 = (TLRPC.TL_pageListOrderedItemBlocks)var34;
                                 var18 = var55.blocks.size();
                                 var48 = 1;

                                 while(true) {
                                    var9 = var6;
                                    if (var48 >= var18) {
                                       break;
                                    }

                                    var51 = ArticleViewer.this.new TL_pageBlockOrderedListItem();
                                    var51.blockItem = (TLRPC.PageBlock)var55.blocks.get(var48);
                                    var51.parent = var19;
                                    if (var6) {
                                       var17 = (ArticleViewer.TL_pageBlockDetailsChild)var1;
                                       ArticleViewer.TL_pageBlockDetailsChild var41 = ArticleViewer.this.new TL_pageBlockDetailsChild();
                                       var41.parent = var17.parent;
                                       var41.block = var51;
                                       this.addBlock(var41, var2, var3 + 1, var4);
                                    } else {
                                       try {
                                          this.addBlock(var51, var2, var3 + 1, var4);
                                       } catch (Throwable var21) {
                                          throw var21;
                                       }
                                    }

                                    var19.items.add(var51);
                                    ++var48;
                                 }
                              }

                              ++var46;
                           }
                        }
                     }
                  }
               }

            }
         }
      }

      private void bindBlockToHolder(int var1, RecyclerView.ViewHolder var2, TLRPC.PageBlock var3, int var4, int var5) {
         TLRPC.PageBlock var6;
         if (var3 instanceof TLRPC.TL_pageBlockCover) {
            var6 = ((TLRPC.TL_pageBlockCover)var3).cover;
         } else if (var3 instanceof ArticleViewer.TL_pageBlockDetailsChild) {
            var6 = ((ArticleViewer.TL_pageBlockDetailsChild)var3).block;
         } else {
            var6 = var3;
         }

         if (var1 != 100) {
            boolean var7 = false;
            boolean var8 = false;
            boolean var9 = false;
            boolean var10 = false;
            boolean var11;
            switch(var1) {
            case 0:
               ((ArticleViewer.BlockParagraphCell)var2.itemView).setBlock((TLRPC.TL_pageBlockParagraph)var6);
               break;
            case 1:
               ((ArticleViewer.BlockHeaderCell)var2.itemView).setBlock((TLRPC.TL_pageBlockHeader)var6);
               break;
            case 2:
               ArticleViewer.BlockDividerCell var20 = (ArticleViewer.BlockDividerCell)var2.itemView;
               break;
            case 3:
               ((ArticleViewer.BlockEmbedCell)var2.itemView).setBlock((TLRPC.TL_pageBlockEmbed)var6);
               break;
            case 4:
               ((ArticleViewer.BlockSubtitleCell)var2.itemView).setBlock((TLRPC.TL_pageBlockSubtitle)var6);
               break;
            case 5:
               ArticleViewer.BlockVideoCell var17 = (ArticleViewer.BlockVideoCell)var2.itemView;
               TLRPC.TL_pageBlockVideo var23 = (TLRPC.TL_pageBlockVideo)var6;
               if (var4 == 0) {
                  var11 = true;
               } else {
                  var11 = false;
               }

               var10 = var9;
               if (var4 == var5 - 1) {
                  var10 = true;
               }

               var17.setBlock(var23, var11, var10);
               var17.setParentBlock(var3);
               break;
            case 6:
               ((ArticleViewer.BlockPullquoteCell)var2.itemView).setBlock((TLRPC.TL_pageBlockPullquote)var6);
               break;
            case 7:
               ((ArticleViewer.BlockBlockquoteCell)var2.itemView).setBlock((TLRPC.TL_pageBlockBlockquote)var6);
               break;
            case 8:
               ((ArticleViewer.BlockSlideshowCell)var2.itemView).setBlock((TLRPC.TL_pageBlockSlideshow)var6);
               break;
            case 9:
               ArticleViewer.BlockPhotoCell var15 = (ArticleViewer.BlockPhotoCell)var2.itemView;
               TLRPC.TL_pageBlockPhoto var22 = (TLRPC.TL_pageBlockPhoto)var6;
               if (var4 == 0) {
                  var11 = true;
               } else {
                  var11 = false;
               }

               var10 = var8;
               if (var4 == var5 - 1) {
                  var10 = true;
               }

               var15.setBlock(var22, var11, var10);
               var15.setParentBlock(var3);
               break;
            case 10:
               ((ArticleViewer.BlockAuthorDateCell)var2.itemView).setBlock((TLRPC.TL_pageBlockAuthorDate)var6);
               break;
            case 11:
               ((ArticleViewer.BlockTitleCell)var2.itemView).setBlock((TLRPC.TL_pageBlockTitle)var6);
               break;
            case 12:
               ((ArticleViewer.BlockListItemCell)var2.itemView).setBlock((ArticleViewer.TL_pageBlockListItem)var6);
               break;
            case 13:
               ((ArticleViewer.BlockFooterCell)var2.itemView).setBlock((TLRPC.TL_pageBlockFooter)var6);
               break;
            case 14:
               ((ArticleViewer.BlockPreformattedCell)var2.itemView).setBlock((TLRPC.TL_pageBlockPreformatted)var6);
               break;
            case 15:
               ((ArticleViewer.BlockSubheaderCell)var2.itemView).setBlock((TLRPC.TL_pageBlockSubheader)var6);
               break;
            case 16:
               ((ArticleViewer.BlockEmbedPostCell)var2.itemView).setBlock((TLRPC.TL_pageBlockEmbedPost)var6);
               break;
            case 17:
               ((ArticleViewer.BlockCollageCell)var2.itemView).setBlock((TLRPC.TL_pageBlockCollage)var6);
               break;
            case 18:
               ((ArticleViewer.BlockChannelCell)var2.itemView).setBlock((TLRPC.TL_pageBlockChannel)var6);
               break;
            case 19:
               ArticleViewer.BlockAudioCell var14 = (ArticleViewer.BlockAudioCell)var2.itemView;
               TLRPC.TL_pageBlockAudio var18 = (TLRPC.TL_pageBlockAudio)var6;
               if (var4 == 0) {
                  var11 = true;
               } else {
                  var11 = false;
               }

               var10 = var7;
               if (var4 == var5 - 1) {
                  var10 = true;
               }

               var14.setBlock(var18, var11, var10);
               break;
            case 20:
               ((ArticleViewer.BlockKickerCell)var2.itemView).setBlock((TLRPC.TL_pageBlockKicker)var6);
               break;
            case 21:
               ((ArticleViewer.BlockOrderedListItemCell)var2.itemView).setBlock((ArticleViewer.TL_pageBlockOrderedListItem)var6);
               break;
            case 22:
               ArticleViewer.BlockMapCell var13 = (ArticleViewer.BlockMapCell)var2.itemView;
               TLRPC.TL_pageBlockMap var16 = (TLRPC.TL_pageBlockMap)var6;
               if (var4 == 0) {
                  var11 = true;
               } else {
                  var11 = false;
               }

               if (var4 == var5 - 1) {
                  var10 = true;
               }

               var13.setBlock(var16, var11, var10);
               break;
            case 23:
               ((ArticleViewer.BlockRelatedArticlesCell)var2.itemView).setBlock((ArticleViewer.TL_pageBlockRelatedArticlesChild)var6);
               break;
            case 24:
               ((ArticleViewer.BlockDetailsCell)var2.itemView).setBlock((TLRPC.TL_pageBlockDetails)var6);
               break;
            case 25:
               ((ArticleViewer.BlockTableCell)var2.itemView).setBlock((TLRPC.TL_pageBlockTable)var6);
               break;
            case 26:
               ((ArticleViewer.BlockRelatedArticlesHeaderCell)var2.itemView).setBlock((TLRPC.TL_pageBlockRelatedArticles)var6);
               break;
            case 27:
               ArticleViewer.BlockDetailsBottomCell var12 = (ArticleViewer.BlockDetailsBottomCell)var2.itemView;
            }
         } else {
            TextView var21 = (TextView)var2.itemView;
            StringBuilder var19 = new StringBuilder();
            var19.append("unsupported block ");
            var19.append(var6);
            var21.setText(var19.toString());
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

      private int getTypeForBlock(TLRPC.PageBlock var1) {
         if (var1 instanceof TLRPC.TL_pageBlockParagraph) {
            return 0;
         } else if (var1 instanceof TLRPC.TL_pageBlockHeader) {
            return 1;
         } else if (var1 instanceof TLRPC.TL_pageBlockDivider) {
            return 2;
         } else if (var1 instanceof TLRPC.TL_pageBlockEmbed) {
            return 3;
         } else if (var1 instanceof TLRPC.TL_pageBlockSubtitle) {
            return 4;
         } else if (var1 instanceof TLRPC.TL_pageBlockVideo) {
            return 5;
         } else if (var1 instanceof TLRPC.TL_pageBlockPullquote) {
            return 6;
         } else if (var1 instanceof TLRPC.TL_pageBlockBlockquote) {
            return 7;
         } else if (var1 instanceof TLRPC.TL_pageBlockSlideshow) {
            return 8;
         } else if (var1 instanceof TLRPC.TL_pageBlockPhoto) {
            return 9;
         } else if (var1 instanceof TLRPC.TL_pageBlockAuthorDate) {
            return 10;
         } else if (var1 instanceof TLRPC.TL_pageBlockTitle) {
            return 11;
         } else if (var1 instanceof ArticleViewer.TL_pageBlockListItem) {
            return 12;
         } else if (var1 instanceof TLRPC.TL_pageBlockFooter) {
            return 13;
         } else if (var1 instanceof TLRPC.TL_pageBlockPreformatted) {
            return 14;
         } else if (var1 instanceof TLRPC.TL_pageBlockSubheader) {
            return 15;
         } else if (var1 instanceof TLRPC.TL_pageBlockEmbedPost) {
            return 16;
         } else if (var1 instanceof TLRPC.TL_pageBlockCollage) {
            return 17;
         } else if (var1 instanceof TLRPC.TL_pageBlockChannel) {
            return 18;
         } else if (var1 instanceof TLRPC.TL_pageBlockAudio) {
            return 19;
         } else if (var1 instanceof TLRPC.TL_pageBlockKicker) {
            return 20;
         } else if (var1 instanceof ArticleViewer.TL_pageBlockOrderedListItem) {
            return 21;
         } else if (var1 instanceof TLRPC.TL_pageBlockMap) {
            return 22;
         } else if (var1 instanceof ArticleViewer.TL_pageBlockRelatedArticlesChild) {
            return 23;
         } else if (var1 instanceof TLRPC.TL_pageBlockDetails) {
            return 24;
         } else if (var1 instanceof TLRPC.TL_pageBlockTable) {
            return 25;
         } else if (var1 instanceof TLRPC.TL_pageBlockRelatedArticles) {
            return 26;
         } else if (var1 instanceof ArticleViewer.TL_pageBlockDetailsBottom) {
            return 27;
         } else if (var1 instanceof ArticleViewer.TL_pageBlockRelatedArticlesShadow) {
            return 28;
         } else if (var1 instanceof ArticleViewer.TL_pageBlockDetailsChild) {
            return this.getTypeForBlock(((ArticleViewer.TL_pageBlockDetailsChild)var1).block);
         } else {
            return var1 instanceof TLRPC.TL_pageBlockCover ? this.getTypeForBlock(((TLRPC.TL_pageBlockCover)var1).cover) : 100;
         }
      }

      private boolean isBlockOpened(ArticleViewer.TL_pageBlockDetailsChild var1) {
         TLRPC.PageBlock var3 = ArticleViewer.this.getLastNonListPageBlock(var1.parent);
         if (var3 instanceof TLRPC.TL_pageBlockDetails) {
            return ((TLRPC.TL_pageBlockDetails)var3).open;
         } else if (var3 instanceof ArticleViewer.TL_pageBlockDetailsChild) {
            var1 = (ArticleViewer.TL_pageBlockDetailsChild)var3;
            TLRPC.PageBlock var2 = ArticleViewer.this.getLastNonListPageBlock(var1.block);
            return var2 instanceof TLRPC.TL_pageBlockDetails && !((TLRPC.TL_pageBlockDetails)var2).open ? false : this.isBlockOpened(var1);
         } else {
            return false;
         }
      }

      private void setRichTextParents(TLRPC.PageBlock var1) {
         if (var1 instanceof TLRPC.TL_pageBlockEmbedPost) {
            TLRPC.TL_pageBlockEmbedPost var9 = (TLRPC.TL_pageBlockEmbedPost)var1;
            this.setRichTextParents((TLRPC.RichText)null, var9.caption.text);
            this.setRichTextParents((TLRPC.RichText)null, var9.caption.credit);
         } else if (var1 instanceof TLRPC.TL_pageBlockParagraph) {
            this.setRichTextParents((TLRPC.RichText)null, ((TLRPC.TL_pageBlockParagraph)var1).text);
         } else if (var1 instanceof TLRPC.TL_pageBlockKicker) {
            this.setRichTextParents((TLRPC.RichText)null, ((TLRPC.TL_pageBlockKicker)var1).text);
         } else if (var1 instanceof TLRPC.TL_pageBlockFooter) {
            this.setRichTextParents((TLRPC.RichText)null, ((TLRPC.TL_pageBlockFooter)var1).text);
         } else if (var1 instanceof TLRPC.TL_pageBlockHeader) {
            this.setRichTextParents((TLRPC.RichText)null, ((TLRPC.TL_pageBlockHeader)var1).text);
         } else if (var1 instanceof TLRPC.TL_pageBlockPreformatted) {
            this.setRichTextParents((TLRPC.RichText)null, ((TLRPC.TL_pageBlockPreformatted)var1).text);
         } else if (var1 instanceof TLRPC.TL_pageBlockSubheader) {
            this.setRichTextParents((TLRPC.RichText)null, ((TLRPC.TL_pageBlockSubheader)var1).text);
         } else {
            boolean var2 = var1 instanceof TLRPC.TL_pageBlockSlideshow;
            byte var3 = 0;
            byte var4 = 0;
            int var5 = 0;
            int var15;
            if (var2) {
               TLRPC.TL_pageBlockSlideshow var10 = (TLRPC.TL_pageBlockSlideshow)var1;
               this.setRichTextParents((TLRPC.RichText)null, var10.caption.text);
               this.setRichTextParents((TLRPC.RichText)null, var10.caption.credit);

               for(var15 = var10.items.size(); var5 < var15; ++var5) {
                  this.setRichTextParents((TLRPC.PageBlock)var10.items.get(var5));
               }
            } else if (var1 instanceof TLRPC.TL_pageBlockPhoto) {
               TLRPC.TL_pageBlockPhoto var11 = (TLRPC.TL_pageBlockPhoto)var1;
               this.setRichTextParents((TLRPC.RichText)null, var11.caption.text);
               this.setRichTextParents((TLRPC.RichText)null, var11.caption.credit);
            } else if (var1 instanceof ArticleViewer.TL_pageBlockListItem) {
               ArticleViewer.TL_pageBlockListItem var12 = (ArticleViewer.TL_pageBlockListItem)var1;
               if (var12.textItem != null) {
                  this.setRichTextParents((TLRPC.RichText)null, var12.textItem);
               } else if (var12.blockItem != null) {
                  this.setRichTextParents(var12.blockItem);
               }
            } else if (var1 instanceof ArticleViewer.TL_pageBlockOrderedListItem) {
               ArticleViewer.TL_pageBlockOrderedListItem var13 = (ArticleViewer.TL_pageBlockOrderedListItem)var1;
               if (var13.textItem != null) {
                  this.setRichTextParents((TLRPC.RichText)null, var13.textItem);
               } else if (var13.blockItem != null) {
                  this.setRichTextParents(var13.blockItem);
               }
            } else {
               int var19;
               if (var1 instanceof TLRPC.TL_pageBlockCollage) {
                  TLRPC.TL_pageBlockCollage var14 = (TLRPC.TL_pageBlockCollage)var1;
                  this.setRichTextParents((TLRPC.RichText)null, var14.caption.text);
                  this.setRichTextParents((TLRPC.RichText)null, var14.caption.credit);
                  var19 = var14.items.size();

                  for(var5 = var3; var5 < var19; ++var5) {
                     this.setRichTextParents((TLRPC.PageBlock)var14.items.get(var5));
                  }
               } else if (var1 instanceof TLRPC.TL_pageBlockEmbed) {
                  TLRPC.TL_pageBlockEmbed var16 = (TLRPC.TL_pageBlockEmbed)var1;
                  this.setRichTextParents((TLRPC.RichText)null, var16.caption.text);
                  this.setRichTextParents((TLRPC.RichText)null, var16.caption.credit);
               } else if (var1 instanceof TLRPC.TL_pageBlockSubtitle) {
                  this.setRichTextParents((TLRPC.RichText)null, ((TLRPC.TL_pageBlockSubtitle)var1).text);
               } else if (var1 instanceof TLRPC.TL_pageBlockBlockquote) {
                  TLRPC.TL_pageBlockBlockquote var17 = (TLRPC.TL_pageBlockBlockquote)var1;
                  this.setRichTextParents((TLRPC.RichText)null, var17.text);
                  this.setRichTextParents((TLRPC.RichText)null, var17.caption);
               } else if (var1 instanceof TLRPC.TL_pageBlockDetails) {
                  TLRPC.TL_pageBlockDetails var18 = (TLRPC.TL_pageBlockDetails)var1;
                  this.setRichTextParents((TLRPC.RichText)null, var18.title);
                  var15 = var18.blocks.size();

                  for(var5 = var4; var5 < var15; ++var5) {
                     this.setRichTextParents((TLRPC.PageBlock)var18.blocks.get(var5));
                  }
               } else if (var1 instanceof TLRPC.TL_pageBlockVideo) {
                  TLRPC.TL_pageBlockVideo var20 = (TLRPC.TL_pageBlockVideo)var1;
                  this.setRichTextParents((TLRPC.RichText)null, var20.caption.text);
                  this.setRichTextParents((TLRPC.RichText)null, var20.caption.credit);
               } else if (var1 instanceof TLRPC.TL_pageBlockPullquote) {
                  TLRPC.TL_pageBlockPullquote var21 = (TLRPC.TL_pageBlockPullquote)var1;
                  this.setRichTextParents((TLRPC.RichText)null, var21.text);
                  this.setRichTextParents((TLRPC.RichText)null, var21.caption);
               } else if (var1 instanceof TLRPC.TL_pageBlockAudio) {
                  TLRPC.TL_pageBlockAudio var22 = (TLRPC.TL_pageBlockAudio)var1;
                  this.setRichTextParents((TLRPC.RichText)null, var22.caption.text);
                  this.setRichTextParents((TLRPC.RichText)null, var22.caption.credit);
               } else if (var1 instanceof TLRPC.TL_pageBlockTable) {
                  TLRPC.TL_pageBlockTable var6 = (TLRPC.TL_pageBlockTable)var1;
                  this.setRichTextParents((TLRPC.RichText)null, var6.title);
                  var19 = var6.rows.size();

                  for(var5 = 0; var5 < var19; ++var5) {
                     TLRPC.TL_pageTableRow var23 = (TLRPC.TL_pageTableRow)var6.rows.get(var5);
                     int var7 = var23.cells.size();

                     for(var15 = 0; var15 < var7; ++var15) {
                        this.setRichTextParents((TLRPC.RichText)null, ((TLRPC.TL_pageTableCell)var23.cells.get(var15)).text);
                     }
                  }
               } else if (var1 instanceof TLRPC.TL_pageBlockTitle) {
                  this.setRichTextParents((TLRPC.RichText)null, ((TLRPC.TL_pageBlockTitle)var1).text);
               } else if (var1 instanceof TLRPC.TL_pageBlockCover) {
                  var1 = ((TLRPC.TL_pageBlockCover)var1).cover;

                  try {
                     this.setRichTextParents(var1);
                  } catch (Throwable var8) {
                     throw var8;
                  }
               } else if (var1 instanceof TLRPC.TL_pageBlockAuthorDate) {
                  this.setRichTextParents((TLRPC.RichText)null, ((TLRPC.TL_pageBlockAuthorDate)var1).author);
               } else if (var1 instanceof TLRPC.TL_pageBlockMap) {
                  TLRPC.TL_pageBlockMap var24 = (TLRPC.TL_pageBlockMap)var1;
                  this.setRichTextParents((TLRPC.RichText)null, var24.caption.text);
                  this.setRichTextParents((TLRPC.RichText)null, var24.caption.credit);
               } else if (var1 instanceof TLRPC.TL_pageBlockRelatedArticles) {
                  this.setRichTextParents((TLRPC.RichText)null, ((TLRPC.TL_pageBlockRelatedArticles)var1).title);
               }
            }
         }

      }

      private void setRichTextParents(TLRPC.RichText var1, TLRPC.RichText var2) {
         if (var2 != null) {
            var2.parentRichText = var1;
            if (var2 instanceof TLRPC.TL_textFixed) {
               this.setRichTextParents(var2, ((TLRPC.TL_textFixed)var2).text);
            } else if (var2 instanceof TLRPC.TL_textItalic) {
               this.setRichTextParents(var2, ((TLRPC.TL_textItalic)var2).text);
            } else if (var2 instanceof TLRPC.TL_textBold) {
               this.setRichTextParents(var2, ((TLRPC.TL_textBold)var2).text);
            } else if (var2 instanceof TLRPC.TL_textUnderline) {
               this.setRichTextParents(var2, ((TLRPC.TL_textUnderline)var2).text);
            } else if (var2 instanceof TLRPC.TL_textStrike) {
               this.setRichTextParents(var2, ((TLRPC.TL_textStrike)var2).text);
            } else if (var2 instanceof TLRPC.TL_textEmail) {
               this.setRichTextParents(var2, ((TLRPC.TL_textEmail)var2).text);
            } else if (var2 instanceof TLRPC.TL_textPhone) {
               this.setRichTextParents(var2, ((TLRPC.TL_textPhone)var2).text);
            } else if (var2 instanceof TLRPC.TL_textUrl) {
               this.setRichTextParents(var2, ((TLRPC.TL_textUrl)var2).text);
            } else if (var2 instanceof TLRPC.TL_textConcat) {
               int var3 = var2.texts.size();

               for(int var4 = 0; var4 < var3; ++var4) {
                  this.setRichTextParents(var2, (TLRPC.RichText)var2.texts.get(var4));
               }
            } else if (var2 instanceof TLRPC.TL_textSubscript) {
               this.setRichTextParents(var2, ((TLRPC.TL_textSubscript)var2).text);
            } else if (var2 instanceof TLRPC.TL_textSuperscript) {
               this.setRichTextParents(var2, ((TLRPC.TL_textSuperscript)var2).text);
            } else if (var2 instanceof TLRPC.TL_textMarked) {
               this.setRichTextParents(var2, ((TLRPC.TL_textMarked)var2).text);
            } else if (var2 instanceof TLRPC.TL_textAnchor) {
               TLRPC.TL_textAnchor var7 = (TLRPC.TL_textAnchor)var2;
               TLRPC.RichText var5 = var7.text;

               try {
                  this.setRichTextParents(var2, var5);
               } catch (Throwable var6) {
                  throw var6;
               }

               String var8 = var7.name.toLowerCase();
               this.anchors.put(var8, this.blocks.size());
               var2 = var7.text;
               if (var2 instanceof TLRPC.TL_textPlain) {
                  if (!TextUtils.isEmpty(((TLRPC.TL_textPlain)var2).text)) {
                     this.anchorsParent.put(var8, var7);
                  }
               } else if (!(var2 instanceof TLRPC.TL_textEmpty)) {
                  this.anchorsParent.put(var8, var7);
               }

               this.anchorsOffset.put(var8, -1);
            }

         }
      }

      private void updateRows() {
         this.localBlocks.clear();
         int var1 = this.blocks.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            TLRPC.PageBlock var3 = (TLRPC.PageBlock)this.blocks.get(var2);
            TLRPC.PageBlock var4 = ArticleViewer.this.getLastNonListPageBlock(var3);
            if (!(var4 instanceof ArticleViewer.TL_pageBlockDetailsChild) || this.isBlockOpened((ArticleViewer.TL_pageBlockDetailsChild)var4)) {
               this.localBlocks.add(var3);
            }
         }

      }

      public TLRPC.PageBlock getItem(int var1) {
         return (TLRPC.PageBlock)this.localBlocks.get(var1);
      }

      public int getItemCount() {
         int var1;
         if (ArticleViewer.this.currentPage != null && ArticleViewer.this.currentPage.cached_page != null) {
            var1 = this.localBlocks.size() + 1;
         } else {
            var1 = 0;
         }

         return var1;
      }

      public int getItemViewType(int var1) {
         return var1 == this.localBlocks.size() ? 90 : this.getTypeForBlock((TLRPC.PageBlock)this.localBlocks.get(var1));
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         return var2 == 23 || var2 == 24;
      }

      public void notifyDataSetChanged() {
         this.updateRows();
         super.notifyDataSetChanged();
      }

      public void notifyItemChanged(int var1) {
         this.updateRows();
         super.notifyItemChanged(var1);
      }

      public void notifyItemChanged(int var1, Object var2) {
         this.updateRows();
         super.notifyItemChanged(var1, var2);
      }

      public void notifyItemInserted(int var1) {
         this.updateRows();
         super.notifyItemInserted(var1);
      }

      public void notifyItemMoved(int var1, int var2) {
         this.updateRows();
         super.notifyItemMoved(var1, var2);
      }

      public void notifyItemRangeChanged(int var1, int var2) {
         this.updateRows();
         super.notifyItemRangeChanged(var1, var2);
      }

      public void notifyItemRangeChanged(int var1, int var2, Object var3) {
         this.updateRows();
         super.notifyItemRangeChanged(var1, var2, var3);
      }

      public void notifyItemRangeInserted(int var1, int var2) {
         this.updateRows();
         super.notifyItemRangeInserted(var1, var2);
      }

      public void notifyItemRangeRemoved(int var1, int var2) {
         this.updateRows();
         super.notifyItemRangeRemoved(var1, var2);
      }

      public void notifyItemRemoved(int var1) {
         this.updateRows();
         super.notifyItemRemoved(var1);
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var2 < this.localBlocks.size()) {
            TLRPC.PageBlock var3 = (TLRPC.PageBlock)this.localBlocks.get(var2);
            this.bindBlockToHolder(var1.getItemViewType(), var1, var3, var2, this.localBlocks.size());
         } else if (var1.getItemViewType() == 90) {
            TextView var4 = (TextView)((ViewGroup)var1.itemView).getChildAt(0);
            var2 = ArticleViewer.this.getSelectedColor();
            if (var2 == 0) {
               var4.setTextColor(-8879475);
               var4.setBackgroundColor(-1183760);
            } else if (var2 == 1) {
               var4.setTextColor(ArticleViewer.this.getGrayTextColor());
               var4.setBackgroundColor(-1712440);
            } else if (var2 == 2) {
               var4.setTextColor(ArticleViewer.this.getGrayTextColor());
               var4.setBackgroundColor(-15000805);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var4;
         if (var2 != 90) {
            switch(var2) {
            case 0:
               var4 = ArticleViewer.this.new BlockParagraphCell(this.context, this);
               break;
            case 1:
               var4 = ArticleViewer.this.new BlockHeaderCell(this.context, this);
               break;
            case 2:
               var4 = ArticleViewer.this.new BlockDividerCell(this.context);
               break;
            case 3:
               var4 = ArticleViewer.this.new BlockEmbedCell(this.context, this);
               break;
            case 4:
               var4 = ArticleViewer.this.new BlockSubtitleCell(this.context, this);
               break;
            case 5:
               var4 = ArticleViewer.this.new BlockVideoCell(this.context, this, 0);
               break;
            case 6:
               var4 = ArticleViewer.this.new BlockPullquoteCell(this.context, this);
               break;
            case 7:
               var4 = ArticleViewer.this.new BlockBlockquoteCell(this.context, this);
               break;
            case 8:
               var4 = ArticleViewer.this.new BlockSlideshowCell(this.context, this);
               break;
            case 9:
               var4 = ArticleViewer.this.new BlockPhotoCell(this.context, this, 0);
               break;
            case 10:
               var4 = ArticleViewer.this.new BlockAuthorDateCell(this.context, this);
               break;
            case 11:
               var4 = ArticleViewer.this.new BlockTitleCell(this.context, this);
               break;
            case 12:
               var4 = ArticleViewer.this.new BlockListItemCell(this.context, this);
               break;
            case 13:
               var4 = ArticleViewer.this.new BlockFooterCell(this.context, this);
               break;
            case 14:
               var4 = ArticleViewer.this.new BlockPreformattedCell(this.context, this);
               break;
            case 15:
               var4 = ArticleViewer.this.new BlockSubheaderCell(this.context, this);
               break;
            case 16:
               var4 = ArticleViewer.this.new BlockEmbedPostCell(this.context, this);
               break;
            case 17:
               var4 = ArticleViewer.this.new BlockCollageCell(this.context, this);
               break;
            case 18:
               var4 = ArticleViewer.this.new BlockChannelCell(this.context, this, 0);
               break;
            case 19:
               var4 = ArticleViewer.this.new BlockAudioCell(this.context, this);
               break;
            case 20:
               var4 = ArticleViewer.this.new BlockKickerCell(this.context, this);
               break;
            case 21:
               var4 = ArticleViewer.this.new BlockOrderedListItemCell(this.context, this);
               break;
            case 22:
               var4 = ArticleViewer.this.new BlockMapCell(this.context, this, 0);
               break;
            case 23:
               var4 = ArticleViewer.this.new BlockRelatedArticlesCell(this.context, this);
               break;
            case 24:
               var4 = ArticleViewer.this.new BlockDetailsCell(this.context, this);
               break;
            case 25:
               var4 = ArticleViewer.this.new BlockTableCell(this.context, this);
               break;
            case 26:
               var4 = ArticleViewer.this.new BlockRelatedArticlesHeaderCell(this.context, this);
               break;
            case 27:
               var4 = ArticleViewer.this.new BlockDetailsBottomCell(this.context);
               break;
            case 28:
               var4 = ArticleViewer.this.new BlockRelatedArticlesShadowCell(this.context);
               break;
            default:
               var4 = new TextView(this.context);
               ((TextView)var4).setBackgroundColor(-65536);
               ((TextView)var4).setTextColor(-16777216);
               ((TextView)var4).setTextSize(1, 20.0F);
            }
         } else {
            var4 = new FrameLayout(this.context) {
               protected void onMeasure(int var1, int var2) {
                  super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0F), 1073741824));
               }
            };
            ((FrameLayout)var4).setTag(90);
            TextView var3 = new TextView(this.context);
            ((FrameLayout)var4).addView(var3, LayoutHelper.createFrame(-1, 34.0F, 51, 0.0F, 10.0F, 0.0F, 0.0F));
            var3.setText(LocaleController.getString("PreviewFeedback", 2131560472));
            var3.setTextSize(1, 12.0F);
            var3.setGravity(17);
         }

         ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         ((View)var4).setFocusable(true);
         return new RecyclerListView.Holder((View)var4);
      }
   }

   private class WindowView extends FrameLayout {
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

      public WindowView(Context var2) {
         super(var2);
      }

      private void prepareForMoving(MotionEvent var1) {
         this.maybeStartTracking = false;
         this.startedTracking = true;
         this.startedTrackingX = (int)var1.getX();
         if (ArticleViewer.this.pagesStack.size() > 1) {
            this.movingPage = true;
            this.startMovingHeaderHeight = ArticleViewer.this.currentHeaderHeight;
            ArticleViewer.this.listView[1].setVisibility(0);
            ArticleViewer.this.listView[1].setAlpha(1.0F);
            ArticleViewer.this.listView[1].setTranslationX(0.0F);
            ArticleViewer.this.listView[0].setBackgroundColor(ArticleViewer.this.backgroundPaint.getColor());
         } else {
            this.movingPage = false;
         }

         ArticleViewer.this.cancelCheckLongPress();
      }

      protected void dispatchDraw(Canvas var1) {
         super.dispatchDraw(var1);
         int var2 = this.bWidth;
         if (var2 != 0) {
            int var3 = this.bHeight;
            if (var3 != 0) {
               int var4 = this.bX;
               if (var4 == 0) {
                  int var5 = this.bY;
                  if (var5 == 0) {
                     var1.drawRect((float)var4, (float)var5, (float)(var4 + var2), (float)(var5 + var3), ArticleViewer.this.blackPaint);
                     return;
                  }
               }

               var1.drawRect((float)this.bX - this.getTranslationX(), (float)this.bY, (float)(this.bX + this.bWidth) - this.getTranslationX(), (float)(this.bY + this.bHeight), ArticleViewer.this.blackPaint);
            }
         }

      }

      protected boolean drawChild(Canvas var1, View var2, long var3) {
         int var5 = this.getMeasuredWidth();
         int var6 = (int)this.innerTranslationX;
         int var7 = var1.save();
         var1.clipRect(var6, 0, var5, this.getHeight());
         boolean var8 = super.drawChild(var1, var2, var3);
         var1.restoreToCount(var7);
         if (var6 != 0 && var2 == ArticleViewer.this.containerView) {
            float var9 = (float)(var5 - var6);
            float var10 = Math.min(0.8F, var9 / (float)var5);
            float var11 = var10;
            if (var10 < 0.0F) {
               var11 = 0.0F;
            }

            ArticleViewer.this.scrimPaint.setColor((int)(var11 * 153.0F) << 24);
            var1.drawRect(0.0F, 0.0F, (float)var6, (float)this.getHeight(), ArticleViewer.this.scrimPaint);
            var11 = Math.max(0.0F, Math.min(var9 / (float)AndroidUtilities.dp(20.0F), 1.0F));
            ArticleViewer.this.layerShadowDrawable.setBounds(var6 - ArticleViewer.this.layerShadowDrawable.getIntrinsicWidth(), var2.getTop(), var6, var2.getBottom());
            ArticleViewer.this.layerShadowDrawable.setAlpha((int)(var11 * 255.0F));
            ArticleViewer.this.layerShadowDrawable.draw(var1);
         }

         return var8;
      }

      @Keep
      public float getAlpha() {
         return this.alpha;
      }

      @Keep
      public float getInnerTranslationX() {
         return this.innerTranslationX;
      }

      public boolean handleTouchEvent(MotionEvent var1) {
         if (!ArticleViewer.this.isPhotoVisible && !this.closeAnimationInProgress && ArticleViewer.this.fullscreenVideoContainer.getVisibility() != 0) {
            VelocityTracker var9;
            if (var1 != null && var1.getAction() == 0 && !this.startedTracking && !this.maybeStartTracking) {
               this.startedTrackingPointerId = var1.getPointerId(0);
               this.maybeStartTracking = true;
               this.startedTrackingX = (int)var1.getX();
               this.startedTrackingY = (int)var1.getY();
               var9 = this.tracker;
               if (var9 != null) {
                  var9.clear();
               }
            } else {
               float var4;
               if (var1 != null && var1.getAction() == 2 && var1.getPointerId(0) == this.startedTrackingPointerId) {
                  if (this.tracker == null) {
                     this.tracker = VelocityTracker.obtain();
                  }

                  int var2 = Math.max(0, (int)(var1.getX() - (float)this.startedTrackingX));
                  int var3 = Math.abs((int)var1.getY() - this.startedTrackingY);
                  this.tracker.addMovement(var1);
                  if (this.maybeStartTracking && !this.startedTracking && (float)var2 >= AndroidUtilities.getPixelsInCM(0.4F, true) && Math.abs(var2) / 3 > var3) {
                     this.prepareForMoving(var1);
                  } else if (this.startedTracking) {
                     ArticleViewer.this.pressedLinkOwnerLayout = null;
                     ArticleViewer.this.pressedLinkOwnerView = null;
                     if (this.movingPage) {
                        ArticleViewer.this.listView[0].setTranslationX((float)var2);
                     } else {
                        FrameLayout var11 = ArticleViewer.this.containerView;
                        var4 = (float)var2;
                        var11.setTranslationX(var4);
                        this.setInnerTranslationX(var4);
                     }
                  }
               } else if (var1 != null && var1.getPointerId(0) == this.startedTrackingPointerId && (var1.getAction() == 3 || var1.getAction() == 1 || var1.getAction() == 6)) {
                  if (this.tracker == null) {
                     this.tracker = VelocityTracker.obtain();
                  }

                  this.tracker.computeCurrentVelocity(1000);
                  float var5 = this.tracker.getXVelocity();
                  float var6 = this.tracker.getYVelocity();
                  if (!this.startedTracking && var5 >= 3500.0F && var5 > Math.abs(var6)) {
                     this.prepareForMoving(var1);
                  }

                  if (this.startedTracking) {
                     Object var10;
                     if (this.movingPage) {
                        var10 = ArticleViewer.this.listView[0];
                     } else {
                        var10 = ArticleViewer.this.containerView;
                     }

                     var4 = ((View)var10).getX();
                     final boolean var7;
                     if (var4 >= (float)((View)var10).getMeasuredWidth() / 3.0F || var5 >= 3500.0F && var5 >= var6) {
                        var7 = false;
                     } else {
                        var7 = true;
                     }

                     AnimatorSet var8 = new AnimatorSet();
                     if (!var7) {
                        var4 = (float)((View)var10).getMeasuredWidth() - var4;
                        if (this.movingPage) {
                           var8.playTogether(new Animator[]{ObjectAnimator.ofFloat(ArticleViewer.this.listView[0], View.TRANSLATION_X, new float[]{(float)((View)var10).getMeasuredWidth()})});
                        } else {
                           var8.playTogether(new Animator[]{ObjectAnimator.ofFloat(ArticleViewer.this.containerView, View.TRANSLATION_X, new float[]{(float)((View)var10).getMeasuredWidth()}), ObjectAnimator.ofFloat(this, ArticleViewer.ARTICLE_VIEWER_INNER_TRANSLATION_X, new float[]{(float)((View)var10).getMeasuredWidth()})});
                        }
                     } else if (this.movingPage) {
                        var8.playTogether(new Animator[]{ObjectAnimator.ofFloat(ArticleViewer.this.listView[0], View.TRANSLATION_X, new float[]{0.0F})});
                     } else {
                        var8.playTogether(new Animator[]{ObjectAnimator.ofFloat(ArticleViewer.this.containerView, View.TRANSLATION_X, new float[]{0.0F}), ObjectAnimator.ofFloat(this, ArticleViewer.ARTICLE_VIEWER_INNER_TRANSLATION_X, new float[]{0.0F})});
                     }

                     var8.setDuration((long)Math.max((int)(200.0F / (float)((View)var10).getMeasuredWidth() * var4), 50));
                     var8.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator var1) {
                           if (WindowView.this.movingPage) {
                              ArticleViewer.this.listView[0].setBackgroundDrawable((Drawable)null);
                              if (!var7) {
                                 ArticleViewer.WebpageAdapter var2 = ArticleViewer.this.adapter[1];
                                 ArticleViewer.this.adapter[1] = ArticleViewer.this.adapter[0];
                                 ArticleViewer.this.adapter[0] = var2;
                                 RecyclerListView var3 = ArticleViewer.this.listView[1];
                                 ArticleViewer.this.listView[1] = ArticleViewer.this.listView[0];
                                 ArticleViewer.this.listView[0] = var3;
                                 LinearLayoutManager var4 = ArticleViewer.this.layoutManager[1];
                                 ArticleViewer.this.layoutManager[1] = ArticleViewer.this.layoutManager[0];
                                 ArticleViewer.this.layoutManager[0] = var4;
                                 ArticleViewer.this.pagesStack.remove(ArticleViewer.this.pagesStack.size() - 1);
                                 ArticleViewer var5 = ArticleViewer.this;
                                 var5.currentPage = (TLRPC.WebPage)var5.pagesStack.get(ArticleViewer.this.pagesStack.size() - 1);
                              }

                              ArticleViewer.this.listView[1].setVisibility(8);
                              ArticleViewer.this.headerView.invalidate();
                           } else if (!var7) {
                              ArticleViewer.this.saveCurrentPagePosition();
                              ArticleViewer.this.onClosed();
                           }

                           WindowView.this.movingPage = false;
                           WindowView.this.startedTracking = false;
                           WindowView.this.closeAnimationInProgress = false;
                        }
                     });
                     var8.start();
                     this.closeAnimationInProgress = true;
                  } else {
                     this.maybeStartTracking = false;
                     this.startedTracking = false;
                     this.movingPage = false;
                  }

                  var9 = this.tracker;
                  if (var9 != null) {
                     var9.recycle();
                     this.tracker = null;
                  }
               } else if (var1 == null) {
                  this.maybeStartTracking = false;
                  this.startedTracking = false;
                  this.movingPage = false;
                  var9 = this.tracker;
                  if (var9 != null) {
                     var9.recycle();
                     this.tracker = null;
                  }
               }
            }

            return this.startedTracking;
         } else {
            return false;
         }
      }

      protected void onAttachedToWindow() {
         super.onAttachedToWindow();
         ArticleViewer.this.attachedToWindow = true;
      }

      protected void onDetachedFromWindow() {
         super.onDetachedFromWindow();
         ArticleViewer.this.attachedToWindow = false;
      }

      protected void onDraw(Canvas var1) {
         var1.drawRect(this.innerTranslationX, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), ArticleViewer.this.backgroundPaint);
         if (VERSION.SDK_INT >= 21) {
            ArticleViewer var2 = ArticleViewer.this;
            if (var2.hasCutout && var2.lastInsets != null) {
               WindowInsets var3 = (WindowInsets)ArticleViewer.this.lastInsets;
               var1.drawRect(this.innerTranslationX, 0.0F, (float)this.getMeasuredWidth(), (float)var3.getSystemWindowInsetBottom(), ArticleViewer.this.statusBarPaint);
            }
         }

      }

      public boolean onInterceptTouchEvent(MotionEvent var1) {
         boolean var2;
         if (ArticleViewer.this.collapsed || !this.handleTouchEvent(var1) && !super.onInterceptTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         if (!this.selfLayout) {
            var4 -= var2;
            if (ArticleViewer.this.anchorsOffsetMeasuredWidth != var4) {
               for(var2 = 0; var2 < ArticleViewer.this.listView.length; ++var2) {
                  Iterator var6 = ArticleViewer.this.adapter[var2].anchorsOffset.entrySet().iterator();

                  while(var6.hasNext()) {
                     ((Entry)var6.next()).setValue(-1);
                  }
               }

               ArticleViewer.this.anchorsOffsetMeasuredWidth = var4;
            }

            if (VERSION.SDK_INT >= 21 && ArticleViewer.this.lastInsets != null) {
               WindowInsets var8 = (WindowInsets)ArticleViewer.this.lastInsets;
               var2 = var8.getSystemWindowInsetLeft();
               if (var8.getSystemWindowInsetRight() != 0) {
                  this.bX = var4 - this.bWidth;
                  this.bY = 0;
               } else if (var8.getSystemWindowInsetLeft() != 0) {
                  this.bX = 0;
                  this.bY = 0;
               } else {
                  this.bX = 0;
                  this.bY = var5 - var3 - this.bHeight;
               }

               if (VERSION.SDK_INT >= 28) {
                  var4 = var8.getSystemWindowInsetTop() + 0;
                  var3 = var2;
                  var2 = var4;
               } else {
                  byte var7 = 0;
                  var3 = var2;
                  var2 = var7;
               }
            } else {
               var2 = 0;
               var3 = 0;
            }

            ArticleViewer.this.containerView.layout(var3, var2, ArticleViewer.this.containerView.getMeasuredWidth() + var3, ArticleViewer.this.containerView.getMeasuredHeight() + var2);
            ArticleViewer.this.photoContainerView.layout(var3, var2, ArticleViewer.this.photoContainerView.getMeasuredWidth() + var3, ArticleViewer.this.photoContainerView.getMeasuredHeight() + var2);
            ArticleViewer.this.photoContainerBackground.layout(var3, var2, ArticleViewer.this.photoContainerBackground.getMeasuredWidth() + var3, ArticleViewer.this.photoContainerBackground.getMeasuredHeight() + var2);
            ArticleViewer.this.fullscreenVideoContainer.layout(var3, var2, ArticleViewer.this.fullscreenVideoContainer.getMeasuredWidth() + var3, ArticleViewer.this.fullscreenVideoContainer.getMeasuredHeight() + var2);
            ArticleViewer.this.animatingImageView.layout(0, 0, ArticleViewer.this.animatingImageView.getMeasuredWidth(), ArticleViewer.this.animatingImageView.getMeasuredHeight());
         }
      }

      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         var1 = MeasureSpec.getSize(var2);
         if (VERSION.SDK_INT >= 21 && ArticleViewer.this.lastInsets != null) {
            this.setMeasuredDimension(var3, var1);
            WindowInsets var4 = (WindowInsets)ArticleViewer.this.lastInsets;
            var2 = var1;
            if (AndroidUtilities.incorrectDisplaySizeFix) {
               int var5 = AndroidUtilities.displaySize.y;
               var2 = var1;
               if (var1 > var5) {
                  var2 = var5;
               }

               var2 += AndroidUtilities.statusBarHeight;
            }

            var2 -= var4.getSystemWindowInsetBottom();
            var1 = var3 - (var4.getSystemWindowInsetRight() + var4.getSystemWindowInsetLeft());
            if (var4.getSystemWindowInsetRight() != 0) {
               this.bWidth = var4.getSystemWindowInsetRight();
               this.bHeight = var2;
            } else if (var4.getSystemWindowInsetLeft() != 0) {
               this.bWidth = var4.getSystemWindowInsetLeft();
               this.bHeight = var2;
            } else {
               this.bWidth = var1;
               this.bHeight = var4.getSystemWindowInsetBottom();
            }

            var2 -= var4.getSystemWindowInsetTop();
         } else {
            this.setMeasuredDimension(var3, var1);
            var2 = var1;
            var1 = var3;
         }

         ArticleViewer.this.containerView.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(var2, 1073741824));
         ArticleViewer.this.photoContainerView.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(var2, 1073741824));
         ArticleViewer.this.photoContainerBackground.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(var2, 1073741824));
         ArticleViewer.this.fullscreenVideoContainer.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(var2, 1073741824));
         android.view.ViewGroup.LayoutParams var6 = ArticleViewer.this.animatingImageView.getLayoutParams();
         ArticleViewer.this.animatingImageView.measure(MeasureSpec.makeMeasureSpec(var6.width, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(var6.height, Integer.MIN_VALUE));
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (ArticleViewer.this.collapsed || !this.handleTouchEvent(var1) && !super.onTouchEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void requestDisallowInterceptTouchEvent(boolean var1) {
         this.handleTouchEvent((MotionEvent)null);
         super.requestDisallowInterceptTouchEvent(var1);
      }

      @Keep
      public void setAlpha(float var1) {
         Paint var2 = ArticleViewer.this.backgroundPaint;
         int var3 = (int)(255.0F * var1);
         var2.setAlpha(var3);
         ArticleViewer.this.statusBarPaint.setAlpha(var3);
         this.alpha = var1;
         if (ArticleViewer.this.parentActivity instanceof LaunchActivity) {
            DrawerLayoutContainer var5 = ((LaunchActivity)ArticleViewer.this.parentActivity).drawerLayoutContainer;
            boolean var4;
            if (ArticleViewer.this.isVisible && this.alpha == 1.0F && this.innerTranslationX == 0.0F) {
               var4 = false;
            } else {
               var4 = true;
            }

            var5.setAllowDrawContent(var4);
         }

         this.invalidate();
      }

      @Keep
      public void setInnerTranslationX(float var1) {
         this.innerTranslationX = var1;
         if (ArticleViewer.this.parentActivity instanceof LaunchActivity) {
            DrawerLayoutContainer var2 = ((LaunchActivity)ArticleViewer.this.parentActivity).drawerLayoutContainer;
            boolean var3;
            if (ArticleViewer.this.isVisible && this.alpha == 1.0F && this.innerTranslationX == 0.0F) {
               var3 = false;
            } else {
               var3 = true;
            }

            var2.setAllowDrawContent(var3);
         }

         this.invalidate();
      }
   }
}
