package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaCodecInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.provider.Settings;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.TextUtils.TruncateAt;
import android.text.method.LinkMovementMethod;
import android.util.Property;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.ActionMode.Callback;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import androidx.annotation.Keep;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScrollerEnd;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.BringAppForegroundService;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.DispatchQueue;
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
import org.telegram.messenger.SecureDocument;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.WebFile;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.MentionsAdapter;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.PhotoPickerPhotoCell;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.ClippingImageView;
import org.telegram.ui.Components.GroupedPhotosListView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberPicker;
import org.telegram.ui.Components.OtherDocumentPlaceholderDrawable;
import org.telegram.ui.Components.PhotoCropView;
import org.telegram.ui.Components.PhotoFilterView;
import org.telegram.ui.Components.PhotoPaintView;
import org.telegram.ui.Components.PhotoViewerCaptionEnterView;
import org.telegram.ui.Components.PickerBottomLayoutViewer;
import org.telegram.ui.Components.PipVideoView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.Rect;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBar;
import org.telegram.ui.Components.SizeNotifierFrameLayoutPhoto;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.URLSpanUserMentionPhotoViewer;
import org.telegram.ui.Components.VideoForwardDrawable;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.Components.VideoTimelinePlayView;

public class PhotoViewer implements NotificationCenter.NotificationCenterDelegate, OnGestureListener, OnDoubleTapListener {
   @SuppressLint({"StaticFieldLeak"})
   private static volatile PhotoViewer Instance;
   private static volatile PhotoViewer PipInstance;
   public static final int SELECT_TYPE_AVATAR = 1;
   public static final int SELECT_TYPE_WALLPAPER = 3;
   private static DecelerateInterpolator decelerateInterpolator;
   private static final int gallery_menu_cancel_loading = 7;
   private static final int gallery_menu_delete = 6;
   private static final int gallery_menu_masks = 13;
   private static final int gallery_menu_openin = 11;
   private static final int gallery_menu_pip = 5;
   private static final int gallery_menu_save = 1;
   private static final int gallery_menu_send = 3;
   private static final int gallery_menu_share = 10;
   private static final int gallery_menu_showall = 2;
   private static final int gallery_menu_showinchat = 4;
   private static Drawable[] progressDrawables;
   private static Paint progressPaint;
   private ActionBar actionBar;
   private AnimatorSet actionBarAnimator;
   private Context actvityContext;
   private ActionBarMenuSubItem allMediaItem;
   private boolean allowMentions;
   private boolean allowShare;
   private float animateToScale;
   private float animateToX;
   private float animateToY;
   private ClippingImageView animatingImageView;
   private Runnable animationEndRunnable;
   private int animationInProgress;
   private long animationStartTime;
   private float animationValue;
   private float[][] animationValues = new float[2][10];
   private boolean applying;
   private AspectRatioFrameLayout aspectRatioFrameLayout;
   private boolean attachedToWindow;
   private long audioFramesSize;
   private ArrayList avatarsArr = new ArrayList();
   private int avatarsDialogId;
   private PhotoViewer.BackgroundDrawable backgroundDrawable = new PhotoViewer.BackgroundDrawable(-16777216);
   private int bitrate;
   private Paint blackPaint = new Paint();
   private FrameLayout bottomLayout;
   private boolean bottomTouchEnabled = true;
   private ImageView cameraItem;
   private boolean canDragDown = true;
   private boolean canZoom = true;
   private PhotoViewerCaptionEnterView captionEditText;
   private TextView captionTextView;
   private ImageReceiver centerImage = new ImageReceiver();
   private AnimatorSet changeModeAnimation;
   private TextureView changedTextureView;
   private boolean changingPage;
   private boolean changingTextureView;
   private CheckBox checkImageView;
   private int classGuid;
   private ImageView compressItem;
   private AnimatorSet compressItemAnimation;
   private int compressionsCount = -1;
   private PhotoViewer.FrameLayoutDrawer containerView;
   private ImageView cropItem;
   private int currentAccount;
   private AnimatedFileDrawable currentAnimation;
   private Bitmap currentBitmap;
   private TLRPC.BotInlineResult currentBotInlineResult;
   private AnimatorSet currentCaptionAnimation;
   private long currentDialogId;
   private int currentEditMode;
   private ImageLocation currentFileLocation;
   private String[] currentFileNames = new String[3];
   private int currentIndex;
   private AnimatorSet currentListViewAnimation;
   private Runnable currentLoadingVideoRunnable;
   private MessageObject currentMessageObject;
   private String currentPathObject;
   private PhotoViewer.PlaceProviderObject currentPlaceObject;
   private Uri currentPlayingVideoFile;
   private SecureDocument currentSecureDocument;
   private String currentSubtitle;
   private ImageReceiver.BitmapHolder currentThumb;
   private ImageLocation currentUserAvatarLocation = null;
   private boolean currentVideoFinishedLoading;
   private int dateOverride;
   private TextView dateTextView;
   private boolean disableShowCheck;
   private boolean discardTap;
   private boolean doneButtonPressed;
   private boolean dontResetZoomOnFirstLayout;
   private boolean doubleTap;
   private boolean doubleTapEnabled;
   private float dragY;
   private boolean draggingDown;
   private PickerBottomLayoutViewer editorDoneLayout;
   private boolean[] endReached = new boolean[]{(boolean)0, (boolean)1};
   private long endTime;
   private long estimatedDuration;
   private int estimatedSize;
   private boolean firstAnimationDelay;
   boolean fromCamera;
   private GestureDetector gestureDetector;
   private GroupedPhotosListView groupedPhotosListView;
   private PhotoViewer.PlaceProviderObject hideAfterAnimation;
   private AnimatorSet hintAnimation;
   private Runnable hintHideRunnable;
   private TextView hintTextView;
   private boolean ignoreDidSetImage;
   private AnimatorSet imageMoveAnimation;
   private ArrayList imagesArr = new ArrayList();
   private ArrayList imagesArrLocals = new ArrayList();
   private ArrayList imagesArrLocations = new ArrayList();
   private ArrayList imagesArrLocationsSizes = new ArrayList();
   private ArrayList imagesArrTemp = new ArrayList();
   private SparseArray[] imagesByIds = new SparseArray[]{new SparseArray(), new SparseArray()};
   private SparseArray[] imagesByIdsTemp = new SparseArray[]{new SparseArray(), new SparseArray()};
   private boolean inPreview;
   private VideoPlayer injectingVideoPlayer;
   private SurfaceTexture injectingVideoPlayerSurface;
   private DecelerateInterpolator interpolator = new DecelerateInterpolator(1.5F);
   private boolean invalidCoords;
   private boolean isActionBarVisible = true;
   private boolean isCurrentVideo;
   private boolean isEvent;
   private boolean isFirstLoading;
   private boolean isInline;
   private boolean isPhotosListViewVisible;
   private boolean isPlaying;
   private boolean isStreaming;
   private boolean isVisible;
   private boolean keepScreenOnFlagSet;
   private long lastBufferedPositionCheck;
   private Object lastInsets;
   private String lastTitle;
   private ImageReceiver leftImage = new ImageReceiver();
   private boolean loadInitialVideo;
   private boolean loadingMoreImages;
   private ActionBarMenuItem masksItem;
   private int maxSelectedPhotos = -1;
   private float maxX;
   private float maxY;
   private LinearLayoutManager mentionLayoutManager;
   private AnimatorSet mentionListAnimation;
   private RecyclerListView mentionListView;
   private MentionsAdapter mentionsAdapter;
   private ActionBarMenuItem menuItem;
   private long mergeDialogId;
   private float minX;
   private float minY;
   private AnimatorSet miniProgressAnimator;
   private Runnable miniProgressShowRunnable = new _$$Lambda$PhotoViewer$kxaoiG79AFKAwlWEJtsCNV1PPvc(this);
   private RadialProgressView miniProgressView;
   private float moveStartX;
   private float moveStartY;
   private boolean moving;
   private ImageView muteItem;
   private boolean muteVideo;
   private String nameOverride;
   private TextView nameTextView;
   private boolean needCaptionLayout;
   private boolean needSearchImageInArr;
   private boolean openedFullScreenVideo;
   private boolean opennedFromMedia;
   private int originalBitrate;
   private int originalHeight;
   private long originalSize;
   private int originalWidth;
   private boolean padImageForHorizontalInsets;
   private ImageView paintItem;
   private Activity parentActivity;
   private ChatAttachAlert parentAlert;
   private ChatActivity parentChatActivity;
   private PhotoCropView photoCropView;
   private PhotoFilterView photoFilterView;
   private PhotoPaintView photoPaintView;
   private PhotoViewer.PhotoProgressView[] photoProgressViews = new PhotoViewer.PhotoProgressView[3];
   private PhotoViewer.CounterView photosCounterView;
   private FrameLayout pickerView;
   private ImageView pickerViewSendButton;
   private float pinchCenterX;
   private float pinchCenterY;
   private float pinchStartDistance;
   private float pinchStartScale = 1.0F;
   private float pinchStartX;
   private float pinchStartY;
   private boolean pipAnimationInProgress;
   private boolean pipAvailable;
   private ActionBarMenuItem pipItem;
   private int[] pipPosition = new int[2];
   private PipVideoView pipVideoView;
   private PhotoViewer.PhotoViewerProvider placeProvider;
   private View playButtonAccessibilityOverlay;
   private boolean playerInjected;
   private boolean playerWasReady;
   private int previewViewEnd;
   private int previousCompression;
   private RadialProgressView progressView;
   private PhotoViewer.QualityChooseView qualityChooseView;
   private AnimatorSet qualityChooseViewAnimation;
   private PickerBottomLayoutViewer qualityPicker;
   private boolean requestingPreview;
   private TextView resetButton;
   private int resultHeight;
   private int resultWidth;
   private ImageReceiver rightImage = new ImageReceiver();
   private ImageView rotateItem;
   private int rotationValue;
   private float scale = 1.0F;
   private Scroller scroller;
   private ArrayList secureDocuments = new ArrayList();
   private float seekToProgressPending;
   private float seekToProgressPending2;
   private int selectedCompression;
   private PhotoViewer.ListAdapter selectedPhotosAdapter;
   private RecyclerListView selectedPhotosListView;
   private ActionBarMenuItem sendItem;
   private int sendPhotoType;
   private Runnable setLoadingRunnable = new Runnable() {
      public void run() {
         if (PhotoViewer.this.currentMessageObject != null) {
            FileLoader.getInstance(PhotoViewer.this.currentMessageObject.currentAccount).setLoadingVideo(PhotoViewer.this.currentMessageObject.getDocument(), true, false);
         }
      }
   };
   private ImageView shareButton;
   private int sharedMediaType;
   private PhotoViewer.PlaceProviderObject showAfterAnimation;
   private boolean skipFirstBufferingProgress;
   private int slideshowMessageId;
   private long startTime;
   private long startedPlayTime;
   private boolean streamingAlertShown;
   private SurfaceTextureListener surfaceTextureListener = new SurfaceTextureListener() {
      public void onSurfaceTextureAvailable(SurfaceTexture var1, int var2, int var3) {
      }

      public boolean onSurfaceTextureDestroyed(SurfaceTexture var1) {
         if (PhotoViewer.this.videoTextureView == null) {
            return true;
         } else if (PhotoViewer.this.changingTextureView) {
            if (PhotoViewer.this.switchingInlineMode) {
               PhotoViewer.this.waitingForFirstTextureUpload = 2;
            }

            PhotoViewer.this.videoTextureView.setSurfaceTexture(var1);
            PhotoViewer.this.videoTextureView.setVisibility(0);
            PhotoViewer.this.changingTextureView = false;
            PhotoViewer.this.containerView.invalidate();
            return false;
         } else {
            return true;
         }
      }

      public void onSurfaceTextureSizeChanged(SurfaceTexture var1, int var2, int var3) {
      }

      public void onSurfaceTextureUpdated(SurfaceTexture var1) {
         if (PhotoViewer.this.waitingForFirstTextureUpload == 1) {
            PhotoViewer.this.changedTextureView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
               // $FF: synthetic method
               public void lambda$onPreDraw$0$PhotoViewer$4$1() {
                  if (PhotoViewer.this.isInline) {
                     PhotoViewer.this.dismissInternal();
                  }

               }

               public boolean onPreDraw() {
                  PhotoViewer.this.changedTextureView.getViewTreeObserver().removeOnPreDrawListener(this);
                  if (PhotoViewer.this.textureImageView != null) {
                     PhotoViewer.this.textureImageView.setVisibility(4);
                     PhotoViewer.this.textureImageView.setImageDrawable((Drawable)null);
                     if (PhotoViewer.this.currentBitmap != null) {
                        PhotoViewer.this.currentBitmap.recycle();
                        PhotoViewer.this.currentBitmap = null;
                     }
                  }

                  AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$4$1$chreZZnt0YkItDSej6M4QHP4VU4(this));
                  PhotoViewer.this.waitingForFirstTextureUpload = 0;
                  return true;
               }
            });
            PhotoViewer.this.changedTextureView.invalidate();
         }

      }
   };
   private TextView switchCaptionTextView;
   private int switchImageAfterAnimation;
   private Runnable switchToInlineRunnable = new Runnable() {
      public void run() {
         PhotoViewer.this.switchingInlineMode = false;
         if (PhotoViewer.this.currentBitmap != null) {
            PhotoViewer.this.currentBitmap.recycle();
            PhotoViewer.this.currentBitmap = null;
         }

         PhotoViewer.this.changingTextureView = true;
         if (PhotoViewer.this.textureImageView != null) {
            try {
               PhotoViewer.this.currentBitmap = Bitmaps.createBitmap(PhotoViewer.this.videoTextureView.getWidth(), PhotoViewer.this.videoTextureView.getHeight(), Config.ARGB_8888);
               PhotoViewer.this.videoTextureView.getBitmap(PhotoViewer.this.currentBitmap);
            } catch (Throwable var5) {
               if (PhotoViewer.this.currentBitmap != null) {
                  PhotoViewer.this.currentBitmap.recycle();
                  PhotoViewer.this.currentBitmap = null;
               }

               FileLog.e(var5);
            }

            if (PhotoViewer.this.currentBitmap != null) {
               PhotoViewer.this.textureImageView.setVisibility(0);
               PhotoViewer.this.textureImageView.setImageBitmap(PhotoViewer.this.currentBitmap);
            } else {
               PhotoViewer.this.textureImageView.setImageDrawable((Drawable)null);
            }
         }

         PhotoViewer.this.isInline = true;
         PhotoViewer.this.pipVideoView = new PipVideoView();
         PhotoViewer var2 = PhotoViewer.this;
         PipVideoView var3 = var2.pipVideoView;
         Activity var1 = PhotoViewer.this.parentActivity;
         PhotoViewer var4 = PhotoViewer.this;
         var2.changedTextureView = var3.show(var1, var4, var4.aspectRatioFrameLayout.getAspectRatio(), PhotoViewer.this.aspectRatioFrameLayout.getVideoRotation());
         PhotoViewer.this.changedTextureView.setVisibility(4);
         PhotoViewer.this.aspectRatioFrameLayout.removeView(PhotoViewer.this.videoTextureView);
      }
   };
   private boolean switchingInlineMode;
   private int switchingToIndex;
   private ImageView textureImageView;
   private boolean textureUploaded;
   private ImageView timeItem;
   private int totalImagesCount;
   private int totalImagesCountMerge;
   private long transitionAnimationStartTime;
   private float translationX;
   private float translationY;
   private boolean tryStartRequestPreviewOnFinish;
   private ImageView tuneItem;
   private Runnable updateProgressRunnable = new Runnable() {
      public void run() {
         if (PhotoViewer.this.videoPlayer != null) {
            float var1;
            float var2;
            if (PhotoViewer.this.isCurrentVideo) {
               if (!PhotoViewer.this.videoTimelineView.isDragging()) {
                  var1 = (float)PhotoViewer.this.videoPlayer.getCurrentPosition() / (float)PhotoViewer.this.videoPlayer.getDuration();
                  if (!PhotoViewer.this.inPreview && PhotoViewer.this.videoTimelineView.getVisibility() == 0) {
                     if (var1 >= PhotoViewer.this.videoTimelineView.getRightProgress()) {
                        PhotoViewer.this.videoTimelineView.setProgress(0.0F);
                        PhotoViewer.this.videoPlayer.seekTo((long)((int)(PhotoViewer.this.videoTimelineView.getLeftProgress() * (float)PhotoViewer.this.videoPlayer.getDuration())));
                        if (PhotoViewer.this.muteVideo) {
                           PhotoViewer.this.videoPlayer.play();
                        } else {
                           PhotoViewer.this.videoPlayer.pause();
                        }

                        PhotoViewer.this.containerView.invalidate();
                     } else {
                        var2 = var1 - PhotoViewer.this.videoTimelineView.getLeftProgress();
                        var1 = var2;
                        if (var2 < 0.0F) {
                           var1 = 0.0F;
                        }

                        var2 = var1 / (PhotoViewer.this.videoTimelineView.getRightProgress() - PhotoViewer.this.videoTimelineView.getLeftProgress());
                        var1 = var2;
                        if (var2 > 1.0F) {
                           var1 = 1.0F;
                        }

                        PhotoViewer.this.videoTimelineView.setProgress(var1);
                     }
                  } else {
                     PhotoViewer.this.videoTimelineView.setProgress(var1);
                  }

                  PhotoViewer.this.updateVideoPlayerTime();
               }
            } else if (!PhotoViewer.this.videoPlayerSeekbar.isDragging()) {
               var2 = (float)PhotoViewer.this.videoPlayer.getCurrentPosition() / (float)PhotoViewer.this.videoPlayer.getDuration();
               if (PhotoViewer.this.currentVideoFinishedLoading) {
                  var1 = 1.0F;
               } else {
                  long var3 = SystemClock.elapsedRealtime();
                  if (Math.abs(var3 - PhotoViewer.this.lastBufferedPositionCheck) >= 500L) {
                     if (PhotoViewer.this.isStreaming) {
                        FileLoader var5 = FileLoader.getInstance(PhotoViewer.this.currentAccount);
                        if (PhotoViewer.this.seekToProgressPending != 0.0F) {
                           var1 = PhotoViewer.this.seekToProgressPending;
                        } else {
                           var1 = var2;
                        }

                        var1 = var5.getBufferedProgressFromPosition(var1, PhotoViewer.this.currentFileNames[0]);
                     } else {
                        var1 = 1.0F;
                     }

                     PhotoViewer.this.lastBufferedPositionCheck = var3;
                  } else {
                     var1 = -1.0F;
                  }
               }

               if (!PhotoViewer.this.inPreview && PhotoViewer.this.videoTimelineView.getVisibility() == 0) {
                  if (var2 >= PhotoViewer.this.videoTimelineView.getRightProgress()) {
                     PhotoViewer.this.videoPlayer.pause();
                     PhotoViewer.this.videoPlayerSeekbar.setProgress(0.0F);
                     PhotoViewer.this.videoPlayer.seekTo((long)((int)(PhotoViewer.this.videoTimelineView.getLeftProgress() * (float)PhotoViewer.this.videoPlayer.getDuration())));
                     PhotoViewer.this.containerView.invalidate();
                  } else {
                     var2 -= PhotoViewer.this.videoTimelineView.getLeftProgress();
                     var1 = var2;
                     if (var2 < 0.0F) {
                        var1 = 0.0F;
                     }

                     var2 = var1 / (PhotoViewer.this.videoTimelineView.getRightProgress() - PhotoViewer.this.videoTimelineView.getLeftProgress());
                     var1 = var2;
                     if (var2 > 1.0F) {
                        var1 = 1.0F;
                     }

                     PhotoViewer.this.videoPlayerSeekbar.setProgress(var1);
                  }
               } else {
                  if (PhotoViewer.this.seekToProgressPending == 0.0F) {
                     PhotoViewer.this.videoPlayerSeekbar.setProgress(var2);
                  }

                  if (var1 != -1.0F) {
                     PhotoViewer.this.videoPlayerSeekbar.setBufferedProgress(var1);
                     if (PhotoViewer.this.pipVideoView != null) {
                        PhotoViewer.this.pipVideoView.setBufferedProgress(var1);
                     }
                  }
               }

               PhotoViewer.this.videoPlayerControlFrameLayout.invalidate();
               PhotoViewer.this.updateVideoPlayerTime();
            }
         }

         if (PhotoViewer.this.isPlaying) {
            AndroidUtilities.runOnUIThread(PhotoViewer.this.updateProgressRunnable, 17L);
         }

      }
   };
   private VelocityTracker velocityTracker;
   private ImageView videoBackwardButton;
   private float videoCrossfadeAlpha;
   private long videoCrossfadeAlphaLastTime;
   private boolean videoCrossfadeStarted;
   private float videoDuration;
   private ImageView videoForwardButton;
   private VideoForwardDrawable videoForwardDrawable;
   private int videoFramerate;
   private long videoFramesSize;
   private boolean videoHasAudio;
   private ImageView videoPlayButton;
   private VideoPlayer videoPlayer;
   private FrameLayout videoPlayerControlFrameLayout;
   private SeekBar videoPlayerSeekbar;
   private SimpleTextView videoPlayerTime;
   private MessageObject videoPreviewMessageObject;
   private TextureView videoTextureView;
   private VideoTimelinePlayView videoTimelineView;
   private AlertDialog visibleDialog;
   private int waitingForDraw;
   private int waitingForFirstTextureUpload;
   private boolean wasLayout;
   private LayoutParams windowLayoutParams;
   private FrameLayout windowView;
   private boolean zoomAnimation;
   private boolean zooming;

   public PhotoViewer() {
      this.blackPaint.setColor(-16777216);
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
         this.imageMoveAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[]{0.0F, 1.0F})});
         this.imageMoveAnimation.setInterpolator(this.interpolator);
         this.imageMoveAnimation.setDuration((long)var5);
         this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               PhotoViewer.this.imageMoveAnimation = null;
               PhotoViewer.this.containerView.invalidate();
            }
         });
         this.imageMoveAnimation.start();
      }
   }

   private void applyCurrentEditMode() {
      int var1;
      ArrayList var4;
      MediaController.SavedFilterState var5;
      boolean var10;
      Bitmap var11;
      label114: {
         MediaController.SavedFilterState var12;
         label124: {
            var1 = this.currentEditMode;
            ArrayList var2;
            Bitmap var3;
            if (var1 == 1 || var1 == 0 && this.sendPhotoType == 1) {
               var3 = this.photoCropView.getBitmap();
               var2 = null;
            } else {
               var1 = this.currentEditMode;
               if (var1 == 2) {
                  var11 = this.photoFilterView.getBitmap();
                  var12 = this.photoFilterView.getSavedFilterState();
                  break label124;
               }

               if (var1 != 3) {
                  var11 = null;
                  var12 = null;
                  break label124;
               }

               var3 = this.photoPaintView.getBitmap();
               var2 = this.photoPaintView.getMasks();
            }

            var5 = null;
            var10 = true;
            var4 = var2;
            var11 = var3;
            break label114;
         }

         var4 = null;
         var10 = false;
         var5 = var12;
      }

      if (var11 != null) {
         TLRPC.PhotoSize var13 = ImageLoader.scaleAndSaveImage(var11, (float)AndroidUtilities.getPhotoSize(), (float)AndroidUtilities.getPhotoSize(), 80, false, 101, 101);
         if (var13 != null) {
            Object var6 = this.imagesArrLocals.get(this.currentIndex);
            int var7;
            if (var6 instanceof MediaController.PhotoEntry) {
               MediaController.PhotoEntry var15 = (MediaController.PhotoEntry)var6;
               var15.imagePath = FileLoader.getPathToAttach(var13, true).toString();
               var13 = ImageLoader.scaleAndSaveImage(var11, (float)AndroidUtilities.dp(120.0F), (float)AndroidUtilities.dp(120.0F), 70, false, 101, 101);
               if (var13 != null) {
                  var15.thumbPath = FileLoader.getPathToAttach(var13, true).toString();
               }

               if (var4 != null) {
                  var15.stickers.addAll(var4);
               }

               var7 = this.currentEditMode;
               if (var7 == 1) {
                  this.cropItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
                  var15.isCropped = true;
               } else if (var7 == 2) {
                  this.tuneItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
                  var15.isFiltered = true;
               } else if (var7 == 3) {
                  this.paintItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
                  var15.isPainted = true;
               }

               if (var5 != null) {
                  var15.savedFilterState = var5;
               } else if (var10) {
                  var15.savedFilterState = null;
               }
            } else if (var6 instanceof MediaController.SearchImage) {
               MediaController.SearchImage var16 = (MediaController.SearchImage)var6;
               var16.imagePath = FileLoader.getPathToAttach(var13, true).toString();
               var13 = ImageLoader.scaleAndSaveImage(var11, (float)AndroidUtilities.dp(120.0F), (float)AndroidUtilities.dp(120.0F), 70, false, 101, 101);
               if (var13 != null) {
                  var16.thumbPath = FileLoader.getPathToAttach(var13, true).toString();
               }

               if (var4 != null) {
                  var16.stickers.addAll(var4);
               }

               var7 = this.currentEditMode;
               if (var7 == 1) {
                  this.cropItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
                  var16.isCropped = true;
               } else if (var7 == 2) {
                  this.tuneItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
                  var16.isFiltered = true;
               } else if (var7 == 3) {
                  this.paintItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
                  var16.isPainted = true;
               }

               if (var5 != null) {
                  var16.savedFilterState = var5;
               } else if (var10) {
                  var16.savedFilterState = null;
               }
            }

            var1 = this.sendPhotoType;
            if (var1 == 0 || var1 == 4) {
               PhotoViewer.PhotoViewerProvider var14 = this.placeProvider;
               if (var14 != null) {
                  var14.updatePhotoAtIndex(this.currentIndex);
                  if (!this.placeProvider.isPhotoChecked(this.currentIndex)) {
                     this.setPhotoChecked();
                  }
               }
            }

            if (this.currentEditMode == 1) {
               float var8 = this.photoCropView.getRectSizeX() / (float)this.getContainerViewWidth();
               float var9 = this.photoCropView.getRectSizeY() / (float)this.getContainerViewHeight();
               if (var8 <= var9) {
                  var8 = var9;
               }

               this.scale = var8;
               this.translationX = this.photoCropView.getRectX() + this.photoCropView.getRectSizeX() / 2.0F - (float)(this.getContainerViewWidth() / 2);
               this.translationY = this.photoCropView.getRectY() + this.photoCropView.getRectSizeY() / 2.0F - (float)(this.getContainerViewHeight() / 2);
               this.zoomAnimation = true;
               this.applying = true;
               this.photoCropView.onDisappear();
            }

            this.centerImage.setParentView((View)null);
            this.centerImage.setOrientation(0, true);
            this.ignoreDidSetImage = true;
            this.centerImage.setImageBitmap(var11);
            this.ignoreDidSetImage = false;
            this.centerImage.setParentView(this.containerView);
            if (this.sendPhotoType == 1) {
               this.setCropBitmap();
            }
         }
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

   private void checkBufferedProgress(float var1) {
      if (this.isStreaming && this.parentActivity != null && !this.streamingAlertShown && this.videoPlayer != null) {
         MessageObject var2 = this.currentMessageObject;
         if (var2 != null) {
            TLRPC.Document var4 = var2.getDocument();
            if (var4 == null) {
               return;
            }

            if (this.currentMessageObject.getDuration() < 20) {
               return;
            }

            if (var1 < 0.9F) {
               int var3 = var4.size;
               if (((float)var3 * var1 >= 5242880.0F || var1 >= 0.5F && var3 >= 2097152) && Math.abs(SystemClock.elapsedRealtime() - this.startedPlayTime) >= 2000L) {
                  if (this.videoPlayer.getDuration() == -9223372036854775807L) {
                     Toast.makeText(this.parentActivity, LocaleController.getString("VideoDoesNotSupportStreaming", 2131561044), 1).show();
                  }

                  this.streamingAlertShown = true;
               }
            }
         }
      }

   }

   private boolean checkInlinePermissions() {
      Activity var1 = this.parentActivity;
      if (var1 == null) {
         return false;
      } else if (VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(var1)) {
         (new AlertDialog.Builder(this.parentActivity)).setTitle(LocaleController.getString("AppName", 2131558635)).setMessage(LocaleController.getString("PermissionDrawAboveOtherApps", 2131560413)).setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131560419), new _$$Lambda$PhotoViewer$3qscthC5fEqwYQ6AGYUupTSlBhs(this)).show();
         return false;
      } else {
         return true;
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

   private void checkProgress(int var1, boolean var2) {
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

      String var6 = this.currentFileNames[var1];
      boolean var7 = false;
      boolean var11;
      if (var6 == null) {
         var11 = var7;
         if (!this.imagesArrLocals.isEmpty()) {
            var11 = var7;
            if (var5 >= 0) {
               var11 = var7;
               if (var5 < this.imagesArrLocals.size()) {
                  Object var23 = this.imagesArrLocals.get(var5);
                  var11 = var7;
                  if (var23 instanceof MediaController.PhotoEntry) {
                     var11 = ((MediaController.PhotoEntry)var23).isVideo;
                  }
               }
            }
         }

         if (var11) {
            this.photoProgressViews[var1].setBackgroundState(3, var2);
         } else {
            this.photoProgressViews[var1].setBackgroundState(-1, var2);
         }
      } else {
         MessageObject var8 = this.currentMessageObject;
         File var13 = null;
         TLRPC.BotInlineResult var9 = null;
         File var10 = null;
         boolean var14;
         if (var8 != null) {
            if (var5 < 0 || var5 >= this.imagesArr.size()) {
               this.photoProgressViews[var1].setBackgroundState(-1, var2);
               return;
            }

            MessageObject var16 = (MessageObject)this.imagesArr.get(var5);
            if (this.sharedMediaType == 1 && !var16.canPreviewDocument()) {
               this.photoProgressViews[var1].setBackgroundState(-1, var2);
               return;
            }

            label199: {
               if (!TextUtils.isEmpty(var16.messageOwner.attachPath)) {
                  var10 = new File(var16.messageOwner.attachPath);
                  var13 = var10;
                  if (var10.exists()) {
                     break label199;
                  }
               }

               var13 = null;
            }

            if (var13 == null) {
               label253: {
                  TLRPC.MessageMedia var19 = var16.messageOwner.media;
                  if (var19 instanceof TLRPC.TL_messageMediaWebPage) {
                     TLRPC.WebPage var20 = var19.webpage;
                     if (var20 != null && var20.document == null) {
                        var13 = FileLoader.getPathToAttach(this.getFileLocation(var5, (int[])null), true);
                        break label253;
                     }
                  }

                  var13 = FileLoader.getPathToMessage(var16.messageOwner);
               }
            }

            if (SharedConfig.streamMedia && var16.isVideo() && var16.canStreamVideo() && (int)var16.getDialogId() != 0) {
               var14 = true;
            } else {
               var14 = false;
            }

            var11 = var16.isVideo();
         } else if (this.currentBotInlineResult != null) {
            if (var5 < 0 || var5 >= this.imagesArrLocals.size()) {
               this.photoProgressViews[var1].setBackgroundState(-1, var2);
               return;
            }

            var9 = (TLRPC.BotInlineResult)this.imagesArrLocals.get(var5);
            if (!var9.type.equals("video") && !MessageObject.isVideoDocument(var9.document)) {
               if (var9.document != null) {
                  var13 = new File(FileLoader.getDirectory(3), this.currentFileNames[var1]);
               } else {
                  var13 = var10;
                  if (var9.photo != null) {
                     var13 = new File(FileLoader.getDirectory(0), this.currentFileNames[var1]);
                  }
               }

               var11 = false;
            } else {
               TLRPC.Document var17 = var9.document;
               if (var17 != null) {
                  var13 = FileLoader.getPathToAttach(var17);
               } else if (var9.content instanceof TLRPC.TL_webDocument) {
                  var13 = FileLoader.getDirectory(4);
                  StringBuilder var18 = new StringBuilder();
                  var18.append(Utilities.MD5(var9.content.url));
                  var18.append(".");
                  var18.append(ImageLoader.getHttpUrlExtension(var9.content.url, "mp4"));
                  var13 = new File(var13, var18.toString());
               }

               var11 = true;
            }

            label174: {
               if (var13 != null) {
                  var10 = var13;
                  if (var13.exists()) {
                     break label174;
                  }
               }

               var10 = new File(FileLoader.getDirectory(4), this.currentFileNames[var1]);
            }

            var14 = false;
            var13 = var10;
         } else {
            if (this.currentFileLocation != null) {
               if (var5 < 0 || var5 >= this.imagesArrLocations.size()) {
                  this.photoProgressViews[var1].setBackgroundState(-1, var2);
                  return;
               }

               TLRPC.TL_fileLocationToBeDeprecated var15 = ((ImageLocation)this.imagesArrLocations.get(var5)).location;
               if (this.avatarsDialogId == 0 && !this.isEvent) {
                  var11 = false;
               } else {
                  var11 = true;
               }

               var13 = FileLoader.getPathToAttach(var15, var11);
            } else if (this.currentSecureDocument != null) {
               if (var5 < 0 || var5 >= this.secureDocuments.size()) {
                  this.photoProgressViews[var1].setBackgroundState(-1, var2);
                  return;
               }

               var13 = FileLoader.getPathToAttach((SecureDocument)this.secureDocuments.get(var5), true);
            } else {
               var13 = var9;
               if (this.currentPathObject != null) {
                  var10 = new File(FileLoader.getDirectory(3), this.currentFileNames[var1]);
                  var13 = var10;
                  if (!var10.exists()) {
                     var13 = new File(FileLoader.getDirectory(4), this.currentFileNames[var1]);
                  }
               }
            }

            var14 = false;
            var11 = false;
         }

         boolean var12;
         if (var13 != null && var13.exists()) {
            var12 = true;
         } else {
            var12 = false;
         }

         if (var13 == null || !var12 && !var14) {
            if (var11) {
               if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[var1])) {
                  this.photoProgressViews[var1].setBackgroundState(2, false);
               } else {
                  this.photoProgressViews[var1].setBackgroundState(1, false);
               }
            } else {
               this.photoProgressViews[var1].setBackgroundState(0, var2);
            }

            Float var21 = ImageLoader.getInstance().getFileProgress(this.currentFileNames[var1]);
            Float var22 = var21;
            if (var21 == null) {
               var22 = 0.0F;
            }

            this.photoProgressViews[var1].setProgress(var22, false);
         } else {
            if (var11) {
               this.photoProgressViews[var1].setBackgroundState(3, var2);
            } else {
               this.photoProgressViews[var1].setBackgroundState(-1, var2);
            }

            if (var1 == 0) {
               if (!var12) {
                  if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[var1])) {
                     this.menuItem.hideSubItem(7);
                  } else {
                     this.menuItem.showSubItem(7);
                  }
               } else {
                  this.menuItem.hideSubItem(7);
               }
            }
         }

         if (var1 == 0) {
            var2 = var4;
            if (this.imagesArrLocals.isEmpty()) {
               if (this.currentFileNames[0] != null && this.photoProgressViews[0].backgroundState != 0) {
                  var2 = var4;
               } else {
                  var2 = false;
               }
            }

            this.canZoom = var2;
         }
      }

   }

   private ByteArrayInputStream cleanBuffer(byte[] var1) {
      byte[] var2 = new byte[var1.length];
      int var3 = 0;
      int var4 = 0;

      while(true) {
         while(var3 < var1.length) {
            if (var1[var3] == 0 && var1[var3 + 1] == 0 && var1[var3 + 2] == 3) {
               var2[var4] = (byte)0;
               var2[var4 + 1] = (byte)0;
               var3 += 3;
               var4 += 2;
            } else {
               var2[var4] = (byte)var1[var3];
               ++var3;
               ++var4;
            }
         }

         return new ByteArrayInputStream(var2, 0, var4);
      }
   }

   private void closeCaptionEnter(boolean var1) {
      int var2 = this.currentIndex;
      if (var2 >= 0 && var2 < this.imagesArrLocals.size()) {
         Object var3 = this.imagesArrLocals.get(this.currentIndex);
         String var4 = null;
         if (var1) {
            CharSequence var5 = this.captionEditText.getFieldCharSequence();
            CharSequence[] var6 = new CharSequence[]{var5};
            ArrayList var8 = DataQuery.getInstance(this.currentAccount).getEntities(var6);
            if (var3 instanceof MediaController.PhotoEntry) {
               MediaController.PhotoEntry var7 = (MediaController.PhotoEntry)var3;
               var7.caption = var6[0];
               var7.entities = var8;
            } else if (var3 instanceof MediaController.SearchImage) {
               MediaController.SearchImage var11 = (MediaController.SearchImage)var3;
               var11.caption = var6[0];
               var11.entities = var8;
            }

            if (this.captionEditText.getFieldCharSequence().length() != 0 && !this.placeProvider.isPhotoChecked(this.currentIndex)) {
               this.setPhotoChecked();
            }

            this.setCurrentCaption((MessageObject)null, var6[0], false);
         }

         this.captionEditText.setTag((Object)null);
         String var9 = this.lastTitle;
         if (var9 != null) {
            this.actionBar.setTitle(var9);
            this.lastTitle = null;
         }

         if (this.isCurrentVideo) {
            ActionBar var10 = this.actionBar;
            if (!this.muteVideo) {
               var4 = this.currentSubtitle;
            }

            var10.setSubtitle(var4);
         }

         this.updateCaptionTextForCurrentPhoto(var3);
         if (this.captionEditText.isPopupShowing()) {
            this.captionEditText.hidePopup();
         }

         this.captionEditText.closeKeyboard();
         if (VERSION.SDK_INT >= 19) {
            this.captionEditText.setImportantForAccessibility(4);
         }
      }

   }

   private TextView createCaptionTextView() {
      TextView var1 = new TextView(this.actvityContext) {
         public boolean onTouchEvent(MotionEvent var1) {
            boolean var2;
            if (PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(var1)) {
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }
      };
      var1.setMovementMethod(new PhotoViewer.LinkMovementMethodMy());
      var1.setPadding(AndroidUtilities.dp(20.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(20.0F), AndroidUtilities.dp(8.0F));
      var1.setLinkTextColor(-1);
      var1.setTextColor(-1);
      var1.setHighlightColor(872415231);
      byte var2;
      if (LocaleController.isRTL) {
         var2 = 5;
      } else {
         var2 = 3;
      }

      var1.setGravity(var2 | 16);
      var1.setTextSize(1, 16.0F);
      var1.setVisibility(4);
      var1.setOnClickListener(new _$$Lambda$PhotoViewer$zH4Om9_yDl8bq0dJjg4CnIpYu4A(this));
      return var1;
   }

   private void createCropView() {
      if (this.photoCropView == null) {
         this.photoCropView = new PhotoCropView(this.actvityContext);
         this.photoCropView.setVisibility(8);
         int var1 = this.containerView.indexOfChild(this.pickerViewSendButton);
         this.containerView.addView(this.photoCropView, var1 - 1, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, 48.0F));
         this.photoCropView.setDelegate(new _$$Lambda$PhotoViewer$4LlSn7fGI8FnhnGA3tv2t0xhh6E(this));
      }
   }

   private void createVideoControlsInterface() {
      this.videoPlayerSeekbar = new SeekBar(this.containerView.getContext());
      this.videoPlayerSeekbar.setLineHeight(AndroidUtilities.dp(4.0F));
      this.videoPlayerSeekbar.setColors(1728053247, 1728053247, -2764585, -1, -1);
      this.videoPlayerSeekbar.setDelegate(new _$$Lambda$PhotoViewer$Wr55IT5IGNjEYLW2UzsHblapEMs(this));
      this.videoPlayerControlFrameLayout = new FrameLayout(this.containerView.getContext()) {
         protected void onDraw(Canvas var1) {
            var1.save();
            var1.translate((float)AndroidUtilities.dp(48.0F), 0.0F);
            PhotoViewer.this.videoPlayerSeekbar.draw(var1);
            var1.restore();
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            float var7;
            if (PhotoViewer.this.videoPlayer != null) {
               float var6 = (float)PhotoViewer.this.videoPlayer.getCurrentPosition() / (float)PhotoViewer.this.videoPlayer.getDuration();
               var7 = var6;
               if (!PhotoViewer.this.inPreview) {
                  var7 = var6;
                  if (PhotoViewer.this.videoTimelineView.getVisibility() == 0) {
                     var6 -= PhotoViewer.this.videoTimelineView.getLeftProgress();
                     var7 = var6;
                     if (var6 < 0.0F) {
                        var7 = 0.0F;
                     }

                     var6 = var7 / (PhotoViewer.this.videoTimelineView.getRightProgress() - PhotoViewer.this.videoTimelineView.getLeftProgress());
                     var7 = var6;
                     if (var6 > 1.0F) {
                        var7 = 1.0F;
                     }
                  }
               }
            } else {
               var7 = 0.0F;
            }

            PhotoViewer.this.videoPlayerSeekbar.setProgress(var7);
            PhotoViewer.this.videoTimelineView.setProgress(var7);
         }

         protected void onMeasure(int var1, int var2) {
            super.onMeasure(var1, var2);
            VideoPlayer var3 = PhotoViewer.this.videoPlayer;
            long var4 = 0L;
            long var6 = var4;
            if (var3 != null) {
               var6 = PhotoViewer.this.videoPlayer.getDuration();
               if (var6 == -9223372036854775807L) {
                  var6 = var4;
               }
            }

            var4 = var6 / 1000L;
            Paint var8 = PhotoViewer.this.videoPlayerTime.getPaint();
            var6 = var4 / 60L;
            var4 %= 60L;
            var1 = (int)Math.ceil((double)var8.measureText(String.format("%02d:%02d / %02d:%02d", var6, var4, var6, var4)));
            PhotoViewer.this.videoPlayerSeekbar.setSize(this.getMeasuredWidth() - AndroidUtilities.dp(64.0F) - var1, this.getMeasuredHeight());
         }

         public boolean onTouchEvent(MotionEvent var1) {
            var1.getX();
            var1.getY();
            if (PhotoViewer.this.videoPlayerSeekbar.onTouch(var1.getAction(), var1.getX() - (float)AndroidUtilities.dp(48.0F), var1.getY())) {
               this.getParent().requestDisallowInterceptTouchEvent(true);
               this.invalidate();
            }

            return true;
         }
      };
      this.videoPlayerControlFrameLayout.setWillNotDraw(false);
      this.bottomLayout.addView(this.videoPlayerControlFrameLayout, LayoutHelper.createFrame(-1, -1, 51));
      this.videoPlayButton = new ImageView(this.containerView.getContext());
      this.videoPlayButton.setScaleType(ScaleType.CENTER);
      this.videoPlayerControlFrameLayout.addView(this.videoPlayButton, LayoutHelper.createFrame(48, 48.0F, 51, 4.0F, 0.0F, 0.0F, 0.0F));
      this.videoPlayButton.setFocusable(true);
      this.videoPlayButton.setContentDescription(LocaleController.getString("AccActionPlay", 2131558409));
      this.videoPlayButton.setOnClickListener(new _$$Lambda$PhotoViewer$b2YYbmShobZSde9PkRRj_gylAtM(this));
      this.videoPlayerTime = new SimpleTextView(this.containerView.getContext());
      this.videoPlayerTime.setTextColor(-1);
      this.videoPlayerTime.setGravity(53);
      this.videoPlayerTime.setTextSize(13);
      this.videoPlayerControlFrameLayout.addView(this.videoPlayerTime, LayoutHelper.createFrame(-2, -1.0F, 53, 0.0F, 17.0F, 7.0F, 0.0F));
   }

   private void didChangedCompressionLevel(boolean var1) {
      Editor var2 = MessagesController.getGlobalMainSettings().edit();
      var2.putInt("compress_video2", this.selectedCompression);
      var2.commit();
      this.updateWidthHeightBitrateForCompression();
      this.updateVideoInfo();
      if (var1) {
         this.requestVideoPreview(1);
      }

   }

   private void dismissInternal() {
      try {
         if (this.windowView.getParent() != null) {
            ((LaunchActivity)this.parentActivity).drawerLayoutContainer.setAllowDrawContent(true);
            ((WindowManager)this.parentActivity.getSystemService("window")).removeView(this.windowView);
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   private int getAdditionX() {
      int var1 = this.currentEditMode;
      return var1 != 0 && var1 != 3 ? AndroidUtilities.dp(14.0F) : 0;
   }

   private int getAdditionY() {
      int var1 = this.currentEditMode;
      byte var2 = 0;
      int var3 = 0;
      if (var1 == 3) {
         int var4 = AndroidUtilities.dp(8.0F);
         if (VERSION.SDK_INT >= 21) {
            var3 = AndroidUtilities.statusBarHeight;
         }

         return var4 + var3;
      } else if (var1 != 0) {
         var1 = AndroidUtilities.dp(14.0F);
         var3 = var2;
         if (VERSION.SDK_INT >= 21) {
            var3 = AndroidUtilities.statusBarHeight;
         }

         return var1 + var3;
      } else {
         return 0;
      }
   }

   private int getContainerViewHeight() {
      return this.getContainerViewHeight(this.currentEditMode);
   }

   private int getContainerViewHeight(int var1) {
      int var2 = AndroidUtilities.displaySize.y;
      int var3 = var2;
      if (var1 == 0) {
         var3 = var2;
         if (VERSION.SDK_INT >= 21) {
            var3 = var2 + AndroidUtilities.statusBarHeight;
         }
      }

      if (var1 == 1) {
         var1 = AndroidUtilities.dp(144.0F);
      } else if (var1 == 2) {
         var1 = AndroidUtilities.dp(214.0F);
      } else {
         var2 = var3;
         if (var1 != 3) {
            return var2;
         }

         var1 = AndroidUtilities.dp(48.0F) + ActionBar.getCurrentActionBarHeight();
      }

      var2 = var3 - var1;
      return var2;
   }

   private int getContainerViewWidth() {
      return this.getContainerViewWidth(this.currentEditMode);
   }

   private int getContainerViewWidth(int var1) {
      int var2 = this.containerView.getWidth();
      int var3 = var2;
      if (var1 != 0) {
         var3 = var2;
         if (var1 != 3) {
            var3 = var2 - AndroidUtilities.dp(28.0F);
         }
      }

      return var3;
   }

   private VideoEditedInfo getCurrentVideoEditedInfo() {
      if (this.isCurrentVideo && this.currentPlayingVideoFile != null && this.compressionsCount != 0) {
         VideoEditedInfo var1 = new VideoEditedInfo();
         var1.startTime = this.startTime;
         var1.endTime = this.endTime;
         var1.rotationValue = this.rotationValue;
         var1.originalWidth = this.originalWidth;
         var1.originalHeight = this.originalHeight;
         var1.bitrate = this.bitrate;
         var1.originalPath = this.currentPlayingVideoFile.getPath();
         int var2 = this.estimatedSize;
         long var3;
         if (var2 != 0) {
            var3 = (long)var2;
         } else {
            var3 = 1L;
         }

         var1.estimatedSize = var3;
         var1.estimatedDuration = this.estimatedDuration;
         var1.framerate = this.videoFramerate;
         boolean var5 = this.muteVideo;
         var2 = -1;
         if (!var5 && (this.compressItem.getTag() == null || this.selectedCompression == this.compressionsCount - 1)) {
            var1.resultWidth = this.originalWidth;
            var1.resultHeight = this.originalHeight;
            if (!this.muteVideo) {
               var2 = this.originalBitrate;
            }

            var1.bitrate = var2;
            var1.muted = this.muteVideo;
         } else {
            if (this.muteVideo) {
               this.selectedCompression = 1;
               this.updateWidthHeightBitrateForCompression();
            }

            var1.resultWidth = this.resultWidth;
            var1.resultHeight = this.resultHeight;
            if (!this.muteVideo) {
               var2 = this.bitrate;
            }

            var1.bitrate = var2;
            var1.muted = this.muteVideo;
         }

         return var1;
      } else {
         return null;
      }
   }

   private TLObject getFileLocation(int var1, int[] var2) {
      if (var1 < 0) {
         return null;
      } else if (!this.secureDocuments.isEmpty()) {
         if (var1 >= this.secureDocuments.size()) {
            return null;
         } else {
            if (var2 != null) {
               var2[0] = ((SecureDocument)this.secureDocuments.get(var1)).secureFile.size;
            }

            return (TLObject)this.secureDocuments.get(var1);
         }
      } else if (!this.imagesArrLocations.isEmpty()) {
         if (var1 >= this.imagesArrLocations.size()) {
            return null;
         } else {
            if (var2 != null) {
               var2[0] = (Integer)this.imagesArrLocationsSizes.get(var1);
            }

            return ((ImageLocation)this.imagesArrLocations.get(var1)).location;
         }
      } else {
         if (!this.imagesArr.isEmpty()) {
            if (var1 >= this.imagesArr.size()) {
               return null;
            }

            MessageObject var3 = (MessageObject)this.imagesArr.get(var1);
            TLRPC.Message var4 = var3.messageOwner;
            TLRPC.PhotoSize var5;
            if (var4 instanceof TLRPC.TL_messageService) {
               TLRPC.MessageAction var6 = var4.action;
               if (var6 instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                  return var6.newUserPhoto.photo_big;
               }

               var5 = FileLoader.getClosestPhotoSizeWithSize(var3.photoThumbs, AndroidUtilities.getPhotoSize());
               if (var5 != null) {
                  if (var2 != null) {
                     var2[0] = var5.size;
                     if (var2[0] == 0) {
                        var2[0] = -1;
                     }
                  }

                  return var5;
               }

               if (var2 != null) {
                  var2[0] = -1;
               }
            } else {
               TLRPC.MessageMedia var7 = var4.media;
               if (!(var7 instanceof TLRPC.TL_messageMediaPhoto) || var7.photo == null) {
                  var7 = var3.messageOwner.media;
                  if (!(var7 instanceof TLRPC.TL_messageMediaWebPage) || var7.webpage == null) {
                     var7 = var3.messageOwner.media;
                     if (var7 instanceof TLRPC.TL_messageMediaInvoice) {
                        return ((TLRPC.TL_messageMediaInvoice)var7).photo;
                     }

                     if (var3.getDocument() != null && MessageObject.isDocumentHasThumb(var3.getDocument())) {
                        var5 = FileLoader.getClosestPhotoSizeWithSize(var3.getDocument().thumbs, 90);
                        if (var2 != null) {
                           var2[0] = var5.size;
                           if (var2[0] == 0) {
                              var2[0] = -1;
                           }
                        }

                        return var5;
                     }

                     return null;
                  }
               }

               var5 = FileLoader.getClosestPhotoSizeWithSize(var3.photoThumbs, AndroidUtilities.getPhotoSize());
               if (var5 != null) {
                  if (var2 != null) {
                     var2[0] = var5.size;
                     if (var2[0] == 0) {
                        var2[0] = -1;
                     }
                  }

                  return var5;
               }

               if (var2 != null) {
                  var2[0] = -1;
               }
            }
         }

         return null;
      }
   }

   private TLRPC.FileLocation getFileLocation(ImageLocation var1) {
      return var1 == null ? null : var1.location;
   }

   private String getFileName(int var1) {
      if (var1 < 0) {
         return null;
      } else if (!this.secureDocuments.isEmpty()) {
         if (var1 >= this.secureDocuments.size()) {
            return null;
         } else {
            SecureDocument var6 = (SecureDocument)this.secureDocuments.get(var1);
            StringBuilder var10 = new StringBuilder();
            var10.append(var6.secureFile.dc_id);
            var10.append("_");
            var10.append(var6.secureFile.id);
            var10.append(".jpg");
            return var10.toString();
         }
      } else {
         StringBuilder var2;
         if (this.imagesArrLocations.isEmpty() && this.imagesArr.isEmpty()) {
            if (!this.imagesArrLocals.isEmpty()) {
               if (var1 >= this.imagesArrLocals.size()) {
                  return null;
               }

               Object var7 = this.imagesArrLocals.get(var1);
               if (var7 instanceof MediaController.SearchImage) {
                  return ((MediaController.SearchImage)var7).getAttachName();
               }

               if (var7 instanceof TLRPC.BotInlineResult) {
                  TLRPC.BotInlineResult var8 = (TLRPC.BotInlineResult)var7;
                  TLRPC.Document var4 = var8.document;
                  if (var4 != null) {
                     return FileLoader.getAttachFileName(var4);
                  }

                  TLRPC.Photo var5 = var8.photo;
                  if (var5 != null) {
                     return FileLoader.getAttachFileName(FileLoader.getClosestPhotoSizeWithSize(var5.sizes, AndroidUtilities.getPhotoSize()));
                  }

                  if (var8.content instanceof TLRPC.TL_webDocument) {
                     var2 = new StringBuilder();
                     var2.append(Utilities.MD5(var8.content.url));
                     var2.append(".");
                     TLRPC.WebDocument var9 = var8.content;
                     var2.append(ImageLoader.getHttpUrlExtension(var9.url, FileLoader.getMimeTypePart(var9.mime_type)));
                     return var2.toString();
                  }
               }
            }
         } else {
            if (!this.imagesArrLocations.isEmpty()) {
               if (var1 >= this.imagesArrLocations.size()) {
                  return null;
               }

               ImageLocation var3 = (ImageLocation)this.imagesArrLocations.get(var1);
               if (var3 == null) {
                  return null;
               }

               var2 = new StringBuilder();
               var2.append(var3.location.volume_id);
               var2.append("_");
               var2.append(var3.location.local_id);
               var2.append(".jpg");
               return var2.toString();
            }

            if (!this.imagesArr.isEmpty()) {
               if (var1 >= this.imagesArr.size()) {
                  return null;
               }

               return FileLoader.getMessageFileName(((MessageObject)this.imagesArr.get(var1)).messageOwner);
            }
         }

         return null;
      }
   }

   private ImageLocation getImageLocation(int var1, int[] var2) {
      if (var1 < 0) {
         return null;
      } else if (!this.secureDocuments.isEmpty()) {
         if (var1 >= this.secureDocuments.size()) {
            return null;
         } else {
            if (var2 != null) {
               var2[0] = ((SecureDocument)this.secureDocuments.get(var1)).secureFile.size;
            }

            return ImageLocation.getForSecureDocument((SecureDocument)this.secureDocuments.get(var1));
         }
      } else if (!this.imagesArrLocations.isEmpty()) {
         if (var1 >= this.imagesArrLocations.size()) {
            return null;
         } else {
            if (var2 != null) {
               var2[0] = (Integer)this.imagesArrLocationsSizes.get(var1);
            }

            return (ImageLocation)this.imagesArrLocations.get(var1);
         }
      } else {
         if (!this.imagesArr.isEmpty()) {
            if (var1 >= this.imagesArr.size()) {
               return null;
            }

            MessageObject var3 = (MessageObject)this.imagesArr.get(var1);
            TLRPC.Message var4 = var3.messageOwner;
            TLRPC.PhotoSize var6;
            if (var4 instanceof TLRPC.TL_messageService) {
               if (var4.action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                  return null;
               }

               var6 = FileLoader.getClosestPhotoSizeWithSize(var3.photoThumbs, AndroidUtilities.getPhotoSize());
               if (var6 != null) {
                  if (var2 != null) {
                     var2[0] = var6.size;
                     if (var2[0] == 0) {
                        var2[0] = -1;
                     }
                  }

                  return ImageLocation.getForObject(var6, var3.photoThumbsObject);
               }

               if (var2 != null) {
                  var2[0] = -1;
               }
            } else {
               TLRPC.MessageMedia var7 = var4.media;
               if (!(var7 instanceof TLRPC.TL_messageMediaPhoto) || var7.photo == null) {
                  var7 = var3.messageOwner.media;
                  if (!(var7 instanceof TLRPC.TL_messageMediaWebPage) || var7.webpage == null) {
                     var7 = var3.messageOwner.media;
                     if (var7 instanceof TLRPC.TL_messageMediaInvoice) {
                        return ImageLocation.getForWebFile(WebFile.createWithWebDocument(((TLRPC.TL_messageMediaInvoice)var7).photo));
                     }

                     if (var3.getDocument() != null && MessageObject.isDocumentHasThumb(var3.getDocument())) {
                        TLRPC.Document var5 = var3.getDocument();
                        var6 = FileLoader.getClosestPhotoSizeWithSize(var5.thumbs, 90);
                        if (var2 != null) {
                           var2[0] = var6.size;
                           if (var2[0] == 0) {
                              var2[0] = -1;
                           }
                        }

                        return ImageLocation.getForDocument(var6, var5);
                     }

                     return null;
                  }
               }

               if (var3.isGif()) {
                  return ImageLocation.getForDocument(var3.getDocument());
               }

               var6 = FileLoader.getClosestPhotoSizeWithSize(var3.photoThumbs, AndroidUtilities.getPhotoSize());
               if (var6 != null) {
                  if (var2 != null) {
                     var2[0] = var6.size;
                     if (var2[0] == 0) {
                        var2[0] = -1;
                     }
                  }

                  return ImageLocation.getForObject(var6, var3.photoThumbsObject);
               }

               if (var2 != null) {
                  var2[0] = -1;
               }
            }
         }

         return null;
      }
   }

   public static PhotoViewer getInstance() {
      PhotoViewer var0 = Instance;
      PhotoViewer var1 = var0;
      if (var0 == null) {
         synchronized(PhotoViewer.class){}

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
                  var1 = new PhotoViewer();
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

   private int getLeftInset() {
      Object var1 = this.lastInsets;
      return var1 != null && VERSION.SDK_INT >= 21 ? ((WindowInsets)var1).getSystemWindowInsetLeft() : 0;
   }

   public static PhotoViewer getPipInstance() {
      return PipInstance;
   }

   private int getRightInset() {
      Object var1 = this.lastInsets;
      return var1 != null && VERSION.SDK_INT >= 21 ? ((WindowInsets)var1).getSystemWindowInsetRight() : 0;
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

   private void hideHint() {
      this.hintAnimation = new AnimatorSet();
      this.hintAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.hintTextView, View.ALPHA, new float[]{0.0F})});
      this.hintAnimation.addListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1) {
            if (var1.equals(PhotoViewer.this.hintAnimation)) {
               PhotoViewer.this.hintHideRunnable = null;
               PhotoViewer.this.hintHideRunnable = null;
            }

         }

         public void onAnimationEnd(Animator var1) {
            if (var1.equals(PhotoViewer.this.hintAnimation)) {
               PhotoViewer.this.hintAnimation = null;
               PhotoViewer.this.hintHideRunnable = null;
               if (PhotoViewer.this.hintTextView != null) {
                  PhotoViewer.this.hintTextView.setVisibility(8);
               }
            }

         }
      });
      this.hintAnimation.setDuration(300L);
      this.hintAnimation.start();
   }

   private void initCropView() {
      if (this.sendPhotoType == 1) {
         this.photoCropView.setBitmap((Bitmap)null, 0, false, false);
         this.photoCropView.onAppear();
         this.photoCropView.setVisibility(0);
         this.photoCropView.setAlpha(1.0F);
         this.photoCropView.onAppeared();
         this.padImageForHorizontalInsets = true;
      }
   }

   public static boolean isPlayingMessage(MessageObject var0) {
      boolean var1;
      if (Instance != null && !Instance.pipAnimationInProgress && Instance.isVisible && var0 != null && Instance.currentMessageObject != null && Instance.currentMessageObject.getId() == var0.getId() && Instance.currentMessageObject.getDialogId() == var0.getDialogId()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isPlayingMessageInPip(MessageObject var0) {
      boolean var1;
      if (PipInstance != null && var0 != null && PipInstance.currentMessageObject != null && PipInstance.currentMessageObject.getId() == var0.getId() && PipInstance.currentMessageObject.getDialogId() == var0.getDialogId()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isShowingImage(String var0) {
      PhotoViewer var1 = Instance;
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 != null) {
         var3 = var2;
         if (Instance.isVisible) {
            var3 = var2;
            if (!Instance.disableShowCheck) {
               var3 = var2;
               if (var0 != null) {
                  var3 = var2;
                  if (var0.equals(Instance.currentPathObject)) {
                     var3 = true;
                  }
               }
            }
         }
      }

      return var3;
   }

   public static boolean isShowingImage(MessageObject var0) {
      boolean var1;
      if (Instance != null && !Instance.pipAnimationInProgress && Instance.isVisible && !Instance.disableShowCheck && var0 != null && Instance.currentMessageObject != null && Instance.currentMessageObject.getId() == var0.getId() && Instance.currentMessageObject.getDialogId() == var0.getDialogId()) {
         var1 = true;
      } else {
         var1 = false;
      }

      boolean var2 = var1;
      if (!var1) {
         var2 = var1;
         if (PipInstance != null) {
            if (PipInstance.isVisible && !PipInstance.disableShowCheck && var0 != null && PipInstance.currentMessageObject != null && PipInstance.currentMessageObject.getId() == var0.getId() && PipInstance.currentMessageObject.getDialogId() == var0.getDialogId()) {
               var2 = true;
            } else {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public static boolean isShowingImage(TLRPC.BotInlineResult var0) {
      PhotoViewer var1 = Instance;
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 != null) {
         var3 = var2;
         if (Instance.isVisible) {
            var3 = var2;
            if (!Instance.disableShowCheck) {
               var3 = var2;
               if (var0 != null) {
                  var3 = var2;
                  if (Instance.currentBotInlineResult != null) {
                     var3 = var2;
                     if (var0.id == Instance.currentBotInlineResult.id) {
                        var3 = true;
                     }
                  }
               }
            }
         }
      }

      return var3;
   }

   public static boolean isShowingImage(TLRPC.FileLocation var0) {
      PhotoViewer var1 = Instance;
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 != null) {
         var3 = var2;
         if (Instance.isVisible) {
            var3 = var2;
            if (!Instance.disableShowCheck) {
               var3 = var2;
               if (var0 != null) {
                  var3 = var2;
                  if (Instance.currentFileLocation != null) {
                     var3 = var2;
                     if (var0.local_id == Instance.currentFileLocation.location.local_id) {
                        var3 = var2;
                        if (var0.volume_id == Instance.currentFileLocation.location.volume_id) {
                           var3 = var2;
                           if (var0.dc_id == Instance.currentFileLocation.dc_id) {
                              var3 = true;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var3;
   }

   // $FF: synthetic method
   public static void lambda$YkU8h9C09cYhYx6kik_kbQ5mA8s(PhotoViewer var0) {
      var0.hideHint();
   }

   // $FF: synthetic method
   static boolean lambda$null$13(View var0, MotionEvent var1) {
      return true;
   }

   // $FF: synthetic method
   static boolean lambda$null$14(View var0, MotionEvent var1) {
      return true;
   }

   // $FF: synthetic method
   static String lambda$null$15(int var0) {
      if (var0 == 0) {
         return LocaleController.getString("ShortMessageLifetimeForever", 2131560776);
      } else {
         return var0 >= 1 && var0 < 21 ? LocaleController.formatTTLString(var0) : LocaleController.formatTTLString((var0 - 16) * 5);
      }
   }

   // $FF: synthetic method
   static void lambda$null$17(BottomSheet var0, View var1) {
      var0.dismiss();
   }

   private void onActionClick(boolean var1) {
      if ((this.currentMessageObject != null || this.currentBotInlineResult != null) && this.currentFileNames[0] != null) {
         File var3;
         Object var5;
         Object var17;
         label132: {
            label131: {
               this.isStreaming = false;
               MessageObject var2 = this.currentMessageObject;
               File var11;
               TLRPC.Document var12;
               StringBuilder var18;
               if (var2 != null) {
                  label128: {
                     String var10 = var2.messageOwner.attachPath;
                     if (var10 != null && var10.length() != 0) {
                        var3 = new File(this.currentMessageObject.messageOwner.attachPath);
                        var11 = var3;
                        if (var3.exists()) {
                           break label128;
                        }
                     }

                     var11 = null;
                  }

                  var3 = var11;
                  if (var11 == null) {
                     var11 = FileLoader.getPathToMessage(this.currentMessageObject.messageOwner);
                     var3 = var11;
                     if (!var11.exists()) {
                        if (SharedConfig.streamMedia && (int)this.currentMessageObject.getDialogId() != 0 && this.currentMessageObject.isVideo() && this.currentMessageObject.canStreamVideo()) {
                           label106: {
                              label105: {
                                 label142: {
                                    StringBuilder var13;
                                    byte[] var14;
                                    boolean var10001;
                                    label102: {
                                       try {
                                          int var4 = FileLoader.getInstance(this.currentMessageObject.currentAccount).getFileReference(this.currentMessageObject);
                                          FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
                                          var12 = this.currentMessageObject.getDocument();
                                          var13 = new StringBuilder();
                                          var13.append("?account=");
                                          var13.append(this.currentMessageObject.currentAccount);
                                          var13.append("&id=");
                                          var13.append(var12.id);
                                          var13.append("&hash=");
                                          var13.append(var12.access_hash);
                                          var13.append("&dc=");
                                          var13.append(var12.dc_id);
                                          var13.append("&size=");
                                          var13.append(var12.size);
                                          var13.append("&mime=");
                                          var13.append(URLEncoder.encode(var12.mime_type, "UTF-8"));
                                          var13.append("&rid=");
                                          var13.append(var4);
                                          var13.append("&name=");
                                          var13.append(URLEncoder.encode(FileLoader.getDocumentFileName(var12), "UTF-8"));
                                          var13.append("&reference=");
                                          if (var12.file_reference != null) {
                                             var14 = var12.file_reference;
                                             break label102;
                                          }
                                       } catch (Exception var9) {
                                          var10001 = false;
                                          break label142;
                                       }

                                       try {
                                          var14 = new byte[0];
                                       } catch (Exception var8) {
                                          var10001 = false;
                                          break label142;
                                       }
                                    }

                                    try {
                                       var13.append(Utilities.bytesToHex(var14));
                                       String var15 = var13.toString();
                                       var18 = new StringBuilder();
                                       var18.append("tg://");
                                       var18.append(this.currentMessageObject.getFileName());
                                       var18.append(var15);
                                       var17 = Uri.parse(var18.toString());
                                       break label105;
                                    } catch (Exception var7) {
                                       var10001 = false;
                                    }
                                 }

                                 var17 = null;
                                 break label106;
                              }

                              try {
                                 this.isStreaming = true;
                                 this.checkProgress(0, false);
                              } catch (Exception var6) {
                              }
                           }

                           var3 = null;
                           break label132;
                        }
                        break label131;
                     }
                  }

                  var11 = var3;
               } else {
                  TLRPC.BotInlineResult var16 = this.currentBotInlineResult;
                  if (var16 == null) {
                     break label131;
                  }

                  var12 = var16.document;
                  if (var12 != null) {
                     var3 = FileLoader.getPathToAttach(var12);
                     var11 = var3;
                     if (!var3.exists()) {
                        break label131;
                     }
                  } else {
                     if (!(var16.content instanceof TLRPC.TL_webDocument)) {
                        break label131;
                     }

                     var3 = FileLoader.getDirectory(4);
                     var18 = new StringBuilder();
                     var18.append(Utilities.MD5(this.currentBotInlineResult.content.url));
                     var18.append(".");
                     var18.append(ImageLoader.getHttpUrlExtension(this.currentBotInlineResult.content.url, "mp4"));
                     var3 = new File(var3, var18.toString());
                     var11 = var3;
                     if (!var3.exists()) {
                        break label131;
                     }
                  }
               }

               var5 = null;
               var3 = var11;
               var17 = var5;
               break label132;
            }

            var3 = null;
            var17 = var3;
         }

         var5 = var17;
         if (var3 != null) {
            var5 = var17;
            if (var17 == null) {
               var5 = Uri.fromFile(var3);
            }
         }

         if (var5 == null) {
            if (var1) {
               if (this.currentMessageObject != null) {
                  if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[0])) {
                     FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
                  } else {
                     FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.getDocument());
                  }
               } else {
                  TLRPC.BotInlineResult var19 = this.currentBotInlineResult;
                  if (var19 != null) {
                     if (var19.document != null) {
                        if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[0])) {
                           FileLoader.getInstance(this.currentAccount).loadFile(this.currentBotInlineResult.document, this.currentMessageObject, 1, 0);
                        } else {
                           FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentBotInlineResult.document);
                        }
                     } else if (var19.content instanceof TLRPC.TL_webDocument) {
                        if (!ImageLoader.getInstance().isLoadingHttpFile(this.currentBotInlineResult.content.url)) {
                           ImageLoader.getInstance().loadHttpFile(this.currentBotInlineResult.content.url, "mp4", this.currentAccount);
                        } else {
                           ImageLoader.getInstance().cancelLoadHttpFile(this.currentBotInlineResult.content.url);
                        }
                     }
                  }
               }

               Drawable var20 = this.centerImage.getStaticThumb();
               if (var20 instanceof OtherDocumentPlaceholderDrawable) {
                  ((OtherDocumentPlaceholderDrawable)var20).checkFileExist();
               }
            }
         } else {
            if (this.sharedMediaType == 1 && !this.currentMessageObject.canPreviewDocument()) {
               AndroidUtilities.openDocument(this.currentMessageObject, this.parentActivity, (BaseFragment)null);
               return;
            }

            this.preparePlayer((Uri)var5, true, false);
         }

      }
   }

   @SuppressLint({"NewApi", "DrawAllocation"})
   private void onDraw(Canvas var1) {
      int var2 = this.animationInProgress;
      if (var2 != 1 && (this.isVisible || var2 == 2 || this.pipAnimationInProgress)) {
         if (this.padImageForHorizontalInsets) {
            var1.save();
            var1.translate((float)(this.getLeftInset() / 2 - this.getRightInset() / 2), 0.0F);
         }

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
               var4 = var9;
            } else {
               var4 = -1.0F;
            }

            this.containerView.invalidate();
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

               this.containerView.invalidate();
            }

            var2 = this.switchImageAfterAnimation;
            if (var2 != 0) {
               this.openedFullScreenVideo = false;
               if (var2 == 1) {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$gYyANufSpmThxLWs9CMz0i1DuDE(this));
               } else if (var2 == 2) {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$C7KcRvYbXzUEDYfyS8Y8Do1EYMA(this));
               }

               this.switchImageAfterAnimation = 0;
            }

            var6 = this.scale;
            var4 = this.translationY;
            var8 = this.translationX;
            if (!this.moving) {
               var9 = var4;
            } else {
               var9 = var4;
               var4 = -1.0F;
            }
         }

         if (this.animationInProgress != 2 && !this.pipAnimationInProgress && !this.isInline) {
            if (this.currentEditMode == 0 && this.sendPhotoType != 1 && this.scale == 1.0F && var4 != -1.0F && !this.zoomAnimation) {
               var3 = (float)this.getContainerViewHeight() / 4.0F;
               this.backgroundDrawable.setAlpha((int)Math.max(127.0F, (1.0F - Math.min(Math.abs(var4), var3) / var3) * 255.0F));
            } else {
               this.backgroundDrawable.setAlpha(255);
            }
         }

         ImageReceiver var10;
         if (this.currentEditMode == 0 && this.sendPhotoType != 1) {
            label307: {
               if (this.scale >= 1.0F && !this.zoomAnimation && !this.zooming) {
                  if (var8 > this.maxX + (float)AndroidUtilities.dp(5.0F)) {
                     var10 = this.leftImage;
                     break label307;
                  }

                  if (var8 < this.minX - (float)AndroidUtilities.dp(5.0F)) {
                     var10 = this.rightImage;
                     break label307;
                  }

                  this.groupedPhotosListView.setMoveProgress(0.0F);
               }

               var10 = null;
            }

            boolean var11;
            if (var10 != null) {
               var11 = true;
            } else {
               var11 = false;
            }

            this.changingPage = var11;
         } else {
            var10 = null;
         }

         int var12;
         float var13;
         float var14;
         float var15;
         if (var10 == this.rightImage) {
            label294: {
               if (!this.zoomAnimation) {
                  var4 = this.minX;
                  if (var8 < var4) {
                     var4 = Math.min(1.0F, (var4 - var8) / (float)this.getContainerViewWidth());
                     var7 = (1.0F - var4) * 0.3F;
                     var3 = (float)(-this.getContainerViewWidth() - AndroidUtilities.dp(30.0F) / 2);
                     break label294;
                  }
               }

               var3 = var8;
               var4 = 1.0F;
               var7 = 0.0F;
            }

            if (var10.hasBitmapImage()) {
               var1.save();
               var1.translate((float)(this.getContainerViewWidth() / 2), (float)(this.getContainerViewHeight() / 2));
               var1.translate((float)(this.getContainerViewWidth() + AndroidUtilities.dp(30.0F) / 2) + var3, 0.0F);
               var5 = 1.0F - var7;
               var1.scale(var5, var5);
               var12 = var10.getBitmapWidth();
               var2 = var10.getBitmapHeight();
               var5 = (float)this.getContainerViewWidth();
               var13 = (float)var12;
               var5 /= var13;
               var14 = (float)this.getContainerViewHeight();
               var15 = (float)var2;
               var14 /= var15;
               if (var5 > var14) {
                  var5 = var14;
               }

               var12 = (int)(var13 * var5);
               var2 = (int)(var15 * var5);
               var10.setAlpha(var4);
               var10.setImageCoords(-var12 / 2, -var2 / 2, var12, var2);
               var10.draw(var1);
               var1.restore();
            }

            this.groupedPhotosListView.setMoveProgress(-var4);
            var1.save();
            var1.translate(var3, var9 / var6);
            var1.translate(((float)this.getContainerViewWidth() * (this.scale + 1.0F) + (float)AndroidUtilities.dp(30.0F)) / 2.0F, -var9 / var6);
            this.photoProgressViews[1].setScale(1.0F - var7);
            this.photoProgressViews[1].setAlpha(var4);
            this.photoProgressViews[1].onDraw(var1);
            var1.restore();
         }

         label287: {
            if (!this.zoomAnimation) {
               var4 = this.maxX;
               if (var8 > var4 && this.currentEditMode == 0 && this.sendPhotoType != 1) {
                  var4 = Math.min(1.0F, (var8 - var4) / (float)this.getContainerViewWidth());
                  var3 = 0.3F * var4;
                  var4 = 1.0F - var4;
                  var7 = this.maxX;
                  break label287;
               }
            }

            var7 = var8;
            var4 = 1.0F;
            var3 = 0.0F;
         }

         AspectRatioFrameLayout var16 = this.aspectRatioFrameLayout;
         boolean var25;
         if (var16 != null && var16.getVisibility() == 0) {
            var25 = true;
         } else {
            var25 = false;
         }

         int var17;
         if (this.centerImage.hasBitmapImage() || var25 && this.textureUploaded) {
            var1.save();
            var1.translate((float)(this.getContainerViewWidth() / 2 + this.getAdditionX()), (float)(this.getContainerViewHeight() / 2 + this.getAdditionY()));
            var1.translate(var7, var9);
            var5 = var6 - var3;
            var1.scale(var5, var5);
            if (var25 && this.textureUploaded) {
               var12 = this.videoTextureView.getMeasuredWidth();
               var17 = this.videoTextureView.getMeasuredHeight();
            } else {
               var12 = this.centerImage.getBitmapWidth();
               var17 = this.centerImage.getBitmapHeight();
            }

            var5 = (float)this.getContainerViewWidth();
            float var18 = (float)var12;
            var14 = var5 / var18;
            var5 = (float)this.getContainerViewHeight();
            var15 = (float)var17;
            var13 = var5 / var15;
            var5 = var14;
            if (var14 > var13) {
               var5 = var13;
            }

            var12 = (int)(var18 * var5);
            var17 = (int)(var5 * var15);
            if (!var25 || !this.textureUploaded || !this.videoCrossfadeStarted || this.videoCrossfadeAlpha != 1.0F) {
               this.centerImage.setAlpha(var4);
               this.centerImage.setImageCoords(-var12 / 2, -var17 / 2, var12, var17);
               this.centerImage.draw(var1);
            }

            if (var25) {
               var13 = (float)var1.getWidth() / var18;
               var14 = (float)var1.getHeight() / var15;
               var5 = var13;
               if (var13 > var14) {
                  var5 = var14;
               }

               var17 = (int)(var15 * var5);
               if (!this.videoCrossfadeStarted && this.textureUploaded) {
                  this.videoCrossfadeStarted = true;
                  this.videoCrossfadeAlpha = 0.0F;
                  this.videoCrossfadeAlphaLastTime = System.currentTimeMillis();
               }

               var1.translate((float)(-var12 / 2), (float)(-var17 / 2));
               this.videoTextureView.setAlpha(this.videoCrossfadeAlpha * var4);
               this.aspectRatioFrameLayout.draw(var1);
               if (this.videoCrossfadeStarted && this.videoCrossfadeAlpha < 1.0F) {
                  long var19 = System.currentTimeMillis();
                  long var21 = this.videoCrossfadeAlphaLastTime;
                  this.videoCrossfadeAlphaLastTime = var19;
                  var13 = this.videoCrossfadeAlpha;
                  var14 = (float)(var19 - var21);
                  if (this.playerInjected) {
                     var5 = 100.0F;
                  } else {
                     var5 = 200.0F;
                  }

                  this.videoCrossfadeAlpha = var13 + var14 / var5;
                  this.containerView.invalidate();
                  if (this.videoCrossfadeAlpha > 1.0F) {
                     this.videoCrossfadeAlpha = 1.0F;
                  }
               }
            }

            var1.restore();
         }

         boolean var27;
         label254: {
            if (this.isCurrentVideo) {
               if (this.progressView.getVisibility() != 0) {
                  VideoPlayer var28 = this.videoPlayer;
                  if (var28 == null || !var28.isPlaying()) {
                     var27 = true;
                     break label254;
                  }
               }
            } else {
               if (!var25 && this.videoPlayerControlFrameLayout.getVisibility() != 0) {
                  var25 = true;
               } else {
                  var25 = false;
               }

               var27 = var25;
               if (!var25) {
                  break label254;
               }

               AnimatedFileDrawable var29 = this.currentAnimation;
               var27 = var25;
               if (var29 == null) {
                  break label254;
               }

               var27 = var25;
               if (var29.isLoadingStream()) {
                  break label254;
               }
            }

            var27 = false;
         }

         if (var27) {
            var1.save();
            var1.translate(var7, var9 / var6);
            this.photoProgressViews[0].setScale(1.0F - var3);
            this.photoProgressViews[0].setAlpha(var4);
            this.photoProgressViews[0].onDraw(var1);
            var1.restore();
         }

         if (!this.pipAnimationInProgress && (this.miniProgressView.getVisibility() == 0 || this.miniProgressAnimator != null)) {
            var1.save();
            var1.translate((float)this.miniProgressView.getLeft() + var7, (float)this.miniProgressView.getTop() + var9 / var6);
            this.miniProgressView.draw(var1);
            var1.restore();
         }

         if (var10 == this.leftImage) {
            if (var10.hasBitmapImage()) {
               var1.save();
               var1.translate((float)(this.getContainerViewWidth() / 2), (float)(this.getContainerViewHeight() / 2));
               var1.translate(-((float)this.getContainerViewWidth() * (this.scale + 1.0F) + (float)AndroidUtilities.dp(30.0F)) / 2.0F + var8, 0.0F);
               var2 = var10.getBitmapWidth();
               var12 = var10.getBitmapHeight();
               var8 = (float)this.getContainerViewWidth();
               var14 = (float)var2;
               var3 = var8 / var14;
               var8 = (float)this.getContainerViewHeight();
               var13 = (float)var12;
               var7 = var8 / var13;
               var8 = var3;
               if (var3 > var7) {
                  var8 = var7;
               }

               var2 = (int)(var14 * var8);
               var12 = (int)(var13 * var8);
               var10.setAlpha(1.0F);
               var10.setImageCoords(-var2 / 2, -var12 / 2, var2, var12);
               var10.draw(var1);
               var1.restore();
            }

            this.groupedPhotosListView.setMoveProgress(1.0F - var4);
            var1.save();
            var1.translate(var8, var9 / var6);
            var1.translate(-((float)this.getContainerViewWidth() * (this.scale + 1.0F) + (float)AndroidUtilities.dp(30.0F)) / 2.0F, -var9 / var6);
            this.photoProgressViews[2].setScale(1.0F);
            this.photoProgressViews[2].setAlpha(1.0F);
            this.photoProgressViews[2].onDraw(var1);
            var1.restore();
         }

         var2 = this.waitingForDraw;
         if (var2 != 0) {
            this.waitingForDraw = var2 - 1;
            if (this.waitingForDraw != 0) {
               this.containerView.invalidate();
            } else {
               if (this.textureImageView != null) {
                  try {
                     this.currentBitmap = Bitmaps.createBitmap(this.videoTextureView.getWidth(), this.videoTextureView.getHeight(), Config.ARGB_8888);
                     this.changedTextureView.getBitmap(this.currentBitmap);
                  } catch (Throwable var24) {
                     Bitmap var30 = this.currentBitmap;
                     if (var30 != null) {
                        var30.recycle();
                        this.currentBitmap = null;
                     }

                     FileLog.e(var24);
                  }

                  if (this.currentBitmap != null) {
                     this.textureImageView.setVisibility(0);
                     this.textureImageView.setImageBitmap(this.currentBitmap);
                  } else {
                     this.textureImageView.setImageDrawable((Drawable)null);
                  }
               }

               this.pipVideoView.close();
               this.pipVideoView = null;
            }
         }

         if (this.padImageForHorizontalInsets) {
            var1.restore();
         }

         if (this.aspectRatioFrameLayout != null && this.videoForwardDrawable.isAnimating()) {
            var2 = (int)((float)this.aspectRatioFrameLayout.getMeasuredHeight() * (this.scale - 1.0F)) / 2;
            VideoForwardDrawable var26 = this.videoForwardDrawable;
            var17 = this.aspectRatioFrameLayout.getLeft();
            var12 = this.aspectRatioFrameLayout.getTop();
            int var23 = (int)(var9 / var6);
            var26.setBounds(var17, var12 - var2 + var23, this.aspectRatioFrameLayout.getRight(), this.aspectRatioFrameLayout.getBottom() + var2 + var23);
            this.videoForwardDrawable.draw(var1);
         }
      }

   }

   private void onPhotoClosed(PhotoViewer.PlaceProviderObject var1) {
      this.isVisible = false;
      this.disableShowCheck = true;
      this.currentMessageObject = null;
      this.currentBotInlineResult = null;
      this.currentFileLocation = null;
      this.currentSecureDocument = null;
      this.currentPathObject = null;
      FrameLayout var2 = this.videoPlayerControlFrameLayout;
      if (var2 != null) {
         var2.setVisibility(8);
         this.dateTextView.setVisibility(0);
         this.nameTextView.setVisibility(0);
      }

      this.sendPhotoType = 0;
      ImageReceiver.BitmapHolder var4 = this.currentThumb;
      if (var4 != null) {
         var4.release();
         this.currentThumb = null;
      }

      this.parentAlert = null;
      AnimatedFileDrawable var5 = this.currentAnimation;
      if (var5 != null) {
         var5.setSecondParentView((View)null);
         this.currentAnimation = null;
      }

      for(int var3 = 0; var3 < 3; ++var3) {
         PhotoViewer.PhotoProgressView[] var6 = this.photoProgressViews;
         if (var6[var3] != null) {
            var6[var3].setBackgroundState(-1, false);
         }
      }

      this.requestVideoPreview(0);
      VideoTimelinePlayView var7 = this.videoTimelineView;
      if (var7 != null) {
         var7.destroy();
      }

      this.centerImage.setImageBitmap((Bitmap)null);
      this.leftImage.setImageBitmap((Bitmap)null);
      this.rightImage.setImageBitmap((Bitmap)null);
      this.containerView.post(new _$$Lambda$PhotoViewer$ckQHNq394VAfTJ1gr02N4PrzmKY(this));
      PhotoViewer.PhotoViewerProvider var8 = this.placeProvider;
      if (var8 != null) {
         var8.willHidePhotoViewer();
      }

      this.groupedPhotosListView.clear();
      this.placeProvider = null;
      this.selectedPhotosAdapter.notifyDataSetChanged();
      this.disableShowCheck = false;
      if (var1 != null) {
         var1.imageReceiver.setVisible(true, true);
      }

   }

   private void onPhotoShow(MessageObject var1, TLRPC.FileLocation var2, ArrayList var3, ArrayList var4, ArrayList var5, int var6, PhotoViewer.PlaceProviderObject var7) {
      this.classGuid = ConnectionsManager.generateClassGuid();
      this.currentMessageObject = null;
      this.currentFileLocation = null;
      this.currentSecureDocument = null;
      this.currentPathObject = null;
      this.fromCamera = false;
      this.currentBotInlineResult = null;
      this.currentIndex = -1;
      String[] var8 = this.currentFileNames;
      var8[0] = null;
      Integer var9 = 1;
      var8[1] = null;
      var8[2] = null;
      this.avatarsDialogId = 0;
      this.totalImagesCount = 0;
      this.totalImagesCountMerge = 0;
      this.currentEditMode = 0;
      this.isFirstLoading = true;
      this.needSearchImageInArr = false;
      this.loadingMoreImages = false;
      boolean[] var37 = this.endReached;
      var37[0] = false;
      boolean var10;
      if (this.mergeDialogId == 0L) {
         var10 = true;
      } else {
         var10 = false;
      }

      var37[1] = var10;
      this.opennedFromMedia = false;
      this.needCaptionLayout = false;
      this.containerView.setTag(var9);
      this.isCurrentVideo = false;
      this.imagesArr.clear();
      this.imagesArrLocations.clear();
      this.imagesArrLocationsSizes.clear();
      this.avatarsArr.clear();
      this.secureDocuments.clear();
      this.imagesArrLocals.clear();

      int var11;
      for(var11 = 0; var11 < 2; ++var11) {
         this.imagesByIds[var11].clear();
         this.imagesByIdsTemp[var11].clear();
      }

      this.imagesArrTemp.clear();
      this.currentUserAvatarLocation = null;
      this.containerView.setPadding(0, 0, 0, 0);
      ImageReceiver.BitmapHolder var38 = this.currentThumb;
      if (var38 != null) {
         var38.release();
      }

      if (var7 != null) {
         var38 = var7.thumb;
      } else {
         var38 = null;
      }

      this.currentThumb = var38;
      if (var7 != null && var7.isEvent) {
         var10 = true;
      } else {
         var10 = false;
      }

      this.isEvent = var10;
      this.sharedMediaType = 0;
      this.allMediaItem.setText(LocaleController.getString("ShowAllMedia", 2131560778));
      this.menuItem.setVisibility(0);
      ActionBarMenuItem var39 = this.sendItem;
      byte var12 = 8;
      var39.setVisibility(8);
      this.pipItem.setVisibility(8);
      this.cameraItem.setVisibility(8);
      this.cameraItem.setTag((Object)null);
      this.bottomLayout.setVisibility(0);
      this.bottomLayout.setTag(var9);
      this.bottomLayout.setTranslationY(0.0F);
      this.captionTextView.setTranslationY(0.0F);
      this.shareButton.setVisibility(8);
      PhotoViewer.QualityChooseView var40 = this.qualityChooseView;
      if (var40 != null) {
         var40.setVisibility(4);
         this.qualityPicker.setVisibility(4);
         this.qualityChooseView.setTag((Object)null);
      }

      AnimatorSet var41 = this.qualityChooseViewAnimation;
      if (var41 != null) {
         var41.cancel();
         this.qualityChooseViewAnimation = null;
      }

      this.setDoubleTapEnabled(true);
      this.allowShare = false;
      this.slideshowMessageId = 0;
      this.nameOverride = null;
      this.dateOverride = 0;
      this.menuItem.hideSubItem(2);
      this.menuItem.hideSubItem(4);
      this.menuItem.hideSubItem(10);
      this.menuItem.hideSubItem(11);
      this.actionBar.setTranslationY(0.0F);
      this.checkImageView.setAlpha(1.0F);
      this.checkImageView.setVisibility(8);
      this.actionBar.setTitleRightMargin(0);
      this.photosCounterView.setAlpha(1.0F);
      this.photosCounterView.setVisibility(8);
      this.pickerView.setVisibility(8);
      this.pickerViewSendButton.setVisibility(8);
      this.pickerViewSendButton.setTranslationY(0.0F);
      this.pickerView.setAlpha(1.0F);
      this.pickerViewSendButton.setAlpha(1.0F);
      this.pickerView.setTranslationY(0.0F);
      this.paintItem.setVisibility(8);
      this.cropItem.setVisibility(8);
      this.tuneItem.setVisibility(8);
      this.timeItem.setVisibility(8);
      this.rotateItem.setVisibility(8);
      this.videoTimelineView.setVisibility(8);
      this.compressItem.setVisibility(8);
      this.captionEditText.setVisibility(8);
      this.mentionListView.setVisibility(8);
      this.muteItem.setVisibility(8);
      this.actionBar.setSubtitle((CharSequence)null);
      this.masksItem.setVisibility(8);
      this.muteVideo = false;
      this.muteItem.setImageResource(2131165912);
      this.editorDoneLayout.setVisibility(8);
      this.captionTextView.setTag((Object)null);
      this.captionTextView.setVisibility(4);
      PhotoCropView var42 = this.photoCropView;
      if (var42 != null) {
         var42.setVisibility(8);
      }

      PhotoFilterView var43 = this.photoFilterView;
      if (var43 != null) {
         var43.setVisibility(8);
      }

      for(var11 = 0; var11 < 3; ++var11) {
         PhotoViewer.PhotoProgressView[] var44 = this.photoProgressViews;
         if (var44[var11] != null) {
            var44[var11].setBackgroundState(-1, false);
         }
      }

      MessageObject var14;
      boolean var47;
      if (var1 != null && var3 == null) {
         TLRPC.MessageMedia var18 = var1.messageOwner.media;
         if (var18 instanceof TLRPC.TL_messageMediaWebPage) {
            TLRPC.WebPage var20 = var18.webpage;
            if (var20 != null) {
               String var21 = var20.site_name;
               if (var21 != null) {
                  var21 = var21.toLowerCase();
                  if (var21.equals("instagram") || var21.equals("twitter") || "telegram_album".equals(var20.type)) {
                     if (!TextUtils.isEmpty(var20.author)) {
                        this.nameOverride = var20.author;
                     }

                     if (var20.cached_page instanceof TLRPC.TL_page) {
                        for(var11 = 0; var11 < var20.cached_page.blocks.size(); ++var11) {
                           TLRPC.PageBlock var23 = (TLRPC.PageBlock)var20.cached_page.blocks.get(var11);
                           if (var23 instanceof TLRPC.TL_pageBlockAuthorDate) {
                              this.dateOverride = ((TLRPC.TL_pageBlockAuthorDate)var23).published_date;
                              break;
                           }
                        }
                     }

                     ArrayList var22 = var1.getWebPagePhotos((ArrayList)null, (ArrayList)null);
                     if (!var22.isEmpty()) {
                        this.slideshowMessageId = var1.getId();
                        this.needSearchImageInArr = false;
                        this.imagesArr.addAll(var22);
                        this.totalImagesCount = this.imagesArr.size();
                        int var46 = this.imagesArr.indexOf(var1);
                        var11 = var46;
                        if (var46 < 0) {
                           var11 = 0;
                        }

                        this.setImageIndex(var11, true);
                     }
                  }
               }
            }
         }

         if (var1.canPreviewDocument()) {
            this.sharedMediaType = 1;
            this.allMediaItem.setText(LocaleController.getString("ShowAllFiles", 2131560777));
         }

         if (this.slideshowMessageId == 0) {
            this.imagesArr.add(var1);
            if (this.currentAnimation == null && var1.eventId == 0L) {
               TLRPC.Message var25 = var1.messageOwner;
               var18 = var25.media;
               if (!(var18 instanceof TLRPC.TL_messageMediaInvoice) && !(var18 instanceof TLRPC.TL_messageMediaWebPage)) {
                  TLRPC.MessageAction var26 = var25.action;
                  if (var26 == null || var26 instanceof TLRPC.TL_messageActionEmpty) {
                     this.needSearchImageInArr = true;
                     this.imagesByIds[0].put(var1.getId(), var1);
                     this.menuItem.showSubItem(2);
                     this.sendItem.setVisibility(0);
                  }
               }
            } else {
               this.needSearchImageInArr = false;
            }

            this.setImageIndex(0, true);
         }
      } else if (var4 != null) {
         this.secureDocuments.addAll(var4);
         this.setImageIndex(var6, true);
      } else {
         ImageView var15;
         byte var45;
         if (var2 != null) {
            this.avatarsDialogId = var7.dialogId;
            ImageLocation var13;
            if (this.avatarsDialogId > 0) {
               var13 = ImageLocation.getForUser(MessagesController.getInstance(this.currentAccount).getUser(this.avatarsDialogId), true);
            } else {
               var13 = ImageLocation.getForChat(MessagesController.getInstance(this.currentAccount).getChat(-this.avatarsDialogId), true);
            }

            if (var13 == null) {
               this.closePhoto(false, false);
               return;
            }

            this.imagesArrLocations.add(var13);
            this.currentUserAvatarLocation = var13;
            this.imagesArrLocationsSizes.add(var7.size);
            this.avatarsArr.add(new TLRPC.TL_photoEmpty());
            var15 = this.shareButton;
            var45 = var12;
            if (this.videoPlayerControlFrameLayout.getVisibility() != 0) {
               var45 = 0;
            }

            var15.setVisibility(var45);
            this.allowShare = true;
            this.menuItem.hideSubItem(2);
            if (this.shareButton.getVisibility() == 0) {
               this.menuItem.hideSubItem(10);
            } else {
               this.menuItem.showSubItem(10);
            }

            this.setImageIndex(0, true);
         } else if (var3 != null) {
            this.opennedFromMedia = true;
            this.menuItem.showSubItem(4);
            this.sendItem.setVisibility(0);
            this.imagesArr.addAll(var3);

            for(var11 = 0; var11 < this.imagesArr.size(); ++var11) {
               var14 = (MessageObject)this.imagesArr.get(var11);
               SparseArray[] var16 = this.imagesByIds;
               if (var14.getDialogId() == this.currentDialogId) {
                  var12 = 0;
               } else {
                  var12 = 1;
               }

               var16[var12].put(var14.getId(), var14);
            }

            if (((MessageObject)this.imagesArr.get(var6)).canPreviewDocument()) {
               this.sharedMediaType = 1;
               this.allMediaItem.setText(LocaleController.getString("ShowAllFiles", 2131560777));
            }

            this.setImageIndex(var6, true);
         } else if (var5 != null) {
            var11 = this.sendPhotoType;
            if (var11 == 0 || var11 == 4 || (var11 == 2 || var11 == 5) && var5.size() > 1) {
               this.checkImageView.setVisibility(0);
               this.photosCounterView.setVisibility(0);
               this.actionBar.setTitleRightMargin(AndroidUtilities.dp(100.0F));
            }

            var11 = this.sendPhotoType;
            if ((var11 == 2 || var11 == 5) && this.placeProvider.canCaptureMorePhotos()) {
               this.cameraItem.setVisibility(0);
               this.cameraItem.setTag(var9);
            }

            label415: {
               this.menuItem.setVisibility(8);
               this.imagesArrLocals.addAll(var5);
               Object var19 = this.imagesArrLocals.get(var6);
               if (var19 instanceof MediaController.PhotoEntry) {
                  if (((MediaController.PhotoEntry)var19).isVideo) {
                     this.cropItem.setVisibility(8);
                     this.rotateItem.setVisibility(8);
                     this.bottomLayout.setVisibility(0);
                     this.bottomLayout.setTag(var9);
                     this.bottomLayout.setTranslationY((float)(-AndroidUtilities.dp(48.0F)));
                  } else {
                     var15 = this.cropItem;
                     if (this.sendPhotoType != 1) {
                        var45 = 0;
                     } else {
                        var45 = 8;
                     }

                     var15.setVisibility(var45);
                     var15 = this.rotateItem;
                     if (this.sendPhotoType != 1) {
                        var45 = 8;
                     } else {
                        var45 = 0;
                     }

                     var15.setVisibility(var45);
                  }
               } else {
                  label416: {
                     if (var19 instanceof TLRPC.BotInlineResult) {
                        this.cropItem.setVisibility(8);
                        this.rotateItem.setVisibility(8);
                     } else {
                        ImageView var17 = this.cropItem;
                        if (var19 instanceof MediaController.SearchImage && ((MediaController.SearchImage)var19).type == 0) {
                           var45 = 0;
                        } else {
                           var45 = 8;
                        }

                        var17.setVisibility(var45);
                        this.rotateItem.setVisibility(8);
                        if (this.cropItem.getVisibility() == 0) {
                           break label416;
                        }
                     }

                     var47 = false;
                     break label415;
                  }
               }

               var47 = true;
            }

            ChatActivity var24 = this.parentChatActivity;
            if (var24 != null) {
               TLRPC.EncryptedChat var27 = var24.currentEncryptedChat;
               if (var27 == null || AndroidUtilities.getPeerLayerVersion(var27.layer) >= 46) {
                  this.mentionsAdapter.setChatInfo(this.parentChatActivity.chatInfo);
                  MentionsAdapter var29 = this.mentionsAdapter;
                  if (this.parentChatActivity.currentChat != null) {
                     var10 = true;
                  } else {
                     var10 = false;
                  }

                  label286: {
                     var29.setNeedUsernames(var10);
                     this.mentionsAdapter.setNeedBotContext(false);
                     if (var47) {
                        PhotoViewer.PhotoViewerProvider var31 = this.placeProvider;
                        if (var31 == null || var31 != null && var31.allowCaption()) {
                           var10 = true;
                           break label286;
                        }
                     }

                     var10 = false;
                  }

                  this.needCaptionLayout = var10;
                  PhotoViewerCaptionEnterView var33 = this.captionEditText;
                  if (this.needCaptionLayout) {
                     var45 = 0;
                  } else {
                     var45 = 8;
                  }

                  var33.setVisibility(var45);
                  if (this.needCaptionLayout) {
                     this.captionEditText.onCreate();
                  }
               }
            }

            this.pickerView.setVisibility(0);
            this.pickerViewSendButton.setVisibility(0);
            this.pickerViewSendButton.setTranslationY(0.0F);
            this.pickerViewSendButton.setAlpha(1.0F);
            this.bottomLayout.setVisibility(8);
            this.bottomLayout.setTag((Object)null);
            this.containerView.setTag((Object)null);
            this.setImageIndex(var6, true);
            var11 = this.sendPhotoType;
            if (var11 == 1) {
               this.paintItem.setVisibility(0);
               this.tuneItem.setVisibility(0);
            } else if (var11 != 4 && var11 != 5) {
               this.paintItem.setVisibility(this.cropItem.getVisibility());
               this.tuneItem.setVisibility(this.cropItem.getVisibility());
            } else {
               this.paintItem.setVisibility(8);
               this.tuneItem.setVisibility(8);
            }

            this.updateSelectedCount();
         }
      }

      TLRPC.User var34 = null;
      if (this.currentAnimation == null && !this.isEvent) {
         if (this.currentDialogId != 0L && this.totalImagesCount == 0) {
            DataQuery.getInstance(this.currentAccount).getMediaCount(this.currentDialogId, this.sharedMediaType, this.classGuid, true);
            if (this.mergeDialogId != 0L) {
               DataQuery.getInstance(this.currentAccount).getMediaCount(this.mergeDialogId, this.sharedMediaType, this.classGuid, true);
            }
         } else if (this.avatarsDialogId != 0) {
            MessagesController.getInstance(this.currentAccount).loadDialogPhotos(this.avatarsDialogId, 80, 0L, true, this.classGuid);
         }
      }

      var14 = this.currentMessageObject;
      if (var14 == null || !var14.isVideo()) {
         TLRPC.BotInlineResult var30 = this.currentBotInlineResult;
         if (var30 == null || !var30.type.equals("video") && !MessageObject.isVideoDocument(this.currentBotInlineResult.document)) {
            if (!this.imagesArrLocals.isEmpty()) {
               Object var32 = this.imagesArrLocals.get(var6);
               ChatActivity var28 = this.parentChatActivity;
               if (var28 != null) {
                  var34 = var28.getCurrentUser();
               }

               var28 = this.parentChatActivity;
               boolean var35;
               if (var28 != null && !var28.isSecretChat() && var34 != null && !var34.bot && !this.parentChatActivity.isEditingMessageMedia()) {
                  var35 = true;
               } else {
                  var35 = false;
               }

               if (var32 instanceof MediaController.PhotoEntry) {
                  MediaController.PhotoEntry var36 = (MediaController.PhotoEntry)var32;
                  var47 = var35;
                  if (var36.isVideo) {
                     this.preparePlayer(Uri.fromFile(new File(var36.path)), false, false);
                     var47 = var35;
                  }
               } else {
                  var47 = var35;
                  if (var35) {
                     var47 = var35;
                     if (var32 instanceof MediaController.SearchImage) {
                        if (((MediaController.SearchImage)var32).type == 0) {
                           var47 = true;
                        } else {
                           var47 = false;
                        }
                     }
                  }
               }

               if (var47) {
                  this.timeItem.setVisibility(0);
                  return;
               }
            }

            return;
         }
      }

      this.onActionClick(false);
   }

   private void onSharePressed() {
      if (this.parentActivity != null && this.allowShare) {
         Exception var10000;
         label157: {
            MessageObject var1;
            boolean var10001;
            try {
               var1 = this.currentMessageObject;
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label157;
            }

            boolean var2 = false;
            File var3 = null;
            File var4 = null;
            boolean var5;
            if (var1 != null) {
               try {
                  var2 = this.currentMessageObject.isVideo();
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label157;
               }

               var3 = var4;

               label131: {
                  try {
                     if (TextUtils.isEmpty(this.currentMessageObject.messageOwner.attachPath)) {
                        break label131;
                     }

                     var3 = new File(this.currentMessageObject.messageOwner.attachPath);
                     if (var3.exists()) {
                        break label131;
                     }
                  } catch (Exception var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label157;
                  }

                  var3 = var4;
               }

               var5 = var2;
               var4 = var3;
               if (var3 == null) {
                  try {
                     var4 = FileLoader.getPathToMessage(this.currentMessageObject.messageOwner);
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label157;
                  }

                  var5 = var2;
               }
            } else {
               label159: {
                  var5 = var2;
                  var4 = var3;

                  TLRPC.TL_fileLocationToBeDeprecated var24;
                  label145: {
                     label144: {
                        try {
                           if (this.currentFileLocation == null) {
                              break label159;
                           }

                           var24 = this.currentFileLocation.location;
                           if (this.avatarsDialogId == 0 && !this.isEvent) {
                              break label144;
                           }
                        } catch (Exception var21) {
                           var10000 = var21;
                           var10001 = false;
                           break label157;
                        }

                        var5 = true;
                        break label145;
                     }

                     var5 = false;
                  }

                  try {
                     var4 = FileLoader.getPathToAttach(var24, var5);
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label157;
                  }

                  var5 = var2;
               }
            }

            Intent var23;
            label116: {
               try {
                  if (var4.exists()) {
                     var23 = new Intent("android.intent.action.SEND");
                     break label116;
                  }
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label157;
               }

               try {
                  this.showDownloadAlert();
                  return;
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label157;
               }
            }

            if (var5) {
               try {
                  var23.setType("video/mp4");
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label157;
               }
            } else {
               label162: {
                  try {
                     if (this.currentMessageObject != null) {
                        var23.setType(this.currentMessageObject.getMimeType());
                        break label162;
                     }
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label157;
                  }

                  try {
                     var23.setType("image/jpeg");
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label157;
                  }
               }
            }

            int var6;
            try {
               var6 = VERSION.SDK_INT;
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label157;
            }

            if (var6 >= 24) {
               try {
                  var23.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this.parentActivity, "org.telegram.messenger.provider", var4));
                  var23.setFlags(1);
               } catch (Exception var10) {
                  try {
                     var23.putExtra("android.intent.extra.STREAM", Uri.fromFile(var4));
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label157;
                  }
               }
            } else {
               try {
                  var23.putExtra("android.intent.extra.STREAM", Uri.fromFile(var4));
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label157;
               }
            }

            try {
               this.parentActivity.startActivityForResult(Intent.createChooser(var23, LocaleController.getString("ShareFile", 2131560748)), 500);
               return;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
            }
         }

         Exception var25 = var10000;
         FileLog.e((Throwable)var25);
      }

   }

   private boolean onTouchEvent(MotionEvent var1) {
      if (this.animationInProgress == 0 && this.animationStartTime == 0L) {
         int var2 = this.currentEditMode;
         if (var2 == 2) {
            this.photoFilterView.onTouch(var1);
            return true;
         } else {
            if (var2 != 1 && this.sendPhotoType != 1) {
               if (!this.captionEditText.isPopupShowing() && !this.captionEditText.isKeyboardVisible()) {
                  if (this.currentEditMode == 0 && this.sendPhotoType != 1 && var1.getPointerCount() == 1 && this.gestureDetector.onTouchEvent(var1) && this.doubleTap) {
                     this.doubleTap = false;
                     this.moving = false;
                     this.zooming = false;
                     this.checkMinMax(false);
                     return true;
                  }

                  float var5;
                  VelocityTracker var10;
                  if (var1.getActionMasked() != 0 && var1.getActionMasked() != 5) {
                     var2 = var1.getActionMasked();
                     float var3 = 0.0F;
                     float var4 = 0.0F;
                     float var6;
                     float var8;
                     if (var2 == 2) {
                        float var7;
                        if (this.canZoom && var1.getPointerCount() == 2 && !this.draggingDown && this.zooming && !this.changingPage) {
                           this.discardTap = true;
                           this.scale = (float)Math.hypot((double)(var1.getX(1) - var1.getX(0)), (double)(var1.getY(1) - var1.getY(0))) / this.pinchStartDistance * this.pinchStartScale;
                           this.translationX = this.pinchCenterX - (float)(this.getContainerViewWidth() / 2) - (this.pinchCenterX - (float)(this.getContainerViewWidth() / 2) - this.pinchStartX) * (this.scale / this.pinchStartScale);
                           var5 = this.pinchCenterY;
                           var6 = (float)(this.getContainerViewHeight() / 2);
                           var7 = this.pinchCenterY;
                           var3 = (float)(this.getContainerViewHeight() / 2);
                           var8 = this.pinchStartY;
                           var4 = this.scale;
                           this.translationY = var5 - var6 - (var7 - var3 - var8) * (var4 / this.pinchStartScale);
                           this.updateMinMax(var4);
                           this.containerView.invalidate();
                        } else if (var1.getPointerCount() == 1) {
                           VelocityTracker var9 = this.velocityTracker;
                           if (var9 != null) {
                              var9.addMovement(var1);
                           }

                           var5 = Math.abs(var1.getX() - this.moveStartX);
                           var6 = Math.abs(var1.getY() - this.dragY);
                           if (var5 > (float)AndroidUtilities.dp(3.0F) || var6 > (float)AndroidUtilities.dp(3.0F)) {
                              this.discardTap = true;
                              PhotoViewer.QualityChooseView var11 = this.qualityChooseView;
                              if (var11 != null && var11.getVisibility() == 0) {
                                 return true;
                              }
                           }

                           if (this.placeProvider.canScrollAway() && this.currentEditMode == 0 && this.sendPhotoType != 1 && this.canDragDown && !this.draggingDown && this.scale == 1.0F && var6 >= (float)AndroidUtilities.dp(30.0F) && var6 / 2.0F > var5) {
                              this.draggingDown = true;
                              this.moving = false;
                              this.dragY = var1.getY();
                              if (this.isActionBarVisible && this.containerView.getTag() != null) {
                                 this.toggleActionBar(false, true);
                              } else if (this.pickerView.getVisibility() == 0) {
                                 this.toggleActionBar(false, true);
                                 this.togglePhotosListView(false, true);
                                 this.toggleCheckImageView(false);
                              }

                              return true;
                           }

                           if (this.draggingDown) {
                              this.translationY = var1.getY() - this.dragY;
                              this.containerView.invalidate();
                           } else if (!this.invalidCoords && this.animationStartTime == 0L) {
                              var6 = this.moveStartX - var1.getX();
                              var5 = this.moveStartY - var1.getY();
                              if (this.moving || this.currentEditMode != 0 || this.scale == 1.0F && Math.abs(var5) + (float)AndroidUtilities.dp(12.0F) < Math.abs(var6) || this.scale != 1.0F) {
                                 if (!this.moving) {
                                    this.moving = true;
                                    this.canDragDown = false;
                                    var6 = 0.0F;
                                    var5 = 0.0F;
                                 }

                                 label334: {
                                    this.moveStartX = var1.getX();
                                    this.moveStartY = var1.getY();
                                    this.updateMinMax(this.scale);
                                    if (this.translationX >= this.minX || this.currentEditMode == 0 && this.rightImage.hasImageSet()) {
                                       var8 = var6;
                                       if (this.translationX <= this.maxX) {
                                          break label334;
                                       }

                                       if (this.currentEditMode == 0) {
                                          var8 = var6;
                                          if (this.leftImage.hasImageSet()) {
                                             break label334;
                                          }
                                       }
                                    }

                                    var8 = var6 / 3.0F;
                                 }

                                 label319: {
                                    var3 = this.maxY;
                                    if (var3 == 0.0F) {
                                       var7 = this.minY;
                                       if (var7 == 0.0F && this.currentEditMode == 0 && this.sendPhotoType != 1) {
                                          var6 = this.translationY;
                                          if (var6 - var5 < var7) {
                                             this.translationY = var7;
                                             var5 = var4;
                                          } else if (var6 - var5 > var3) {
                                             this.translationY = var3;
                                             var5 = var4;
                                          }
                                          break label319;
                                       }
                                    }

                                    var6 = this.translationY;
                                    if (var6 < this.minY || var6 > this.maxY) {
                                       var5 /= 3.0F;
                                    }
                                 }

                                 this.translationX -= var8;
                                 if (this.scale != 1.0F || this.currentEditMode != 0) {
                                    this.translationY -= var5;
                                 }

                                 this.containerView.invalidate();
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
                           var5 = this.scale;
                           if (var5 < 1.0F) {
                              this.updateMinMax(1.0F);
                              this.animateTo(1.0F, 0.0F, 0.0F, true);
                           } else if (var5 > 3.0F) {
                              var5 = this.pinchCenterX - (float)(this.getContainerViewWidth() / 2) - (this.pinchCenterX - (float)(this.getContainerViewWidth() / 2) - this.pinchStartX) * (3.0F / this.pinchStartScale);
                              var6 = this.pinchCenterY - (float)(this.getContainerViewHeight() / 2) - (this.pinchCenterY - (float)(this.getContainerViewHeight() / 2) - this.pinchStartY) * (3.0F / this.pinchStartScale);
                              this.updateMinMax(3.0F);
                              var8 = this.minX;
                              if (var5 < var8) {
                                 var5 = var8;
                              } else {
                                 var8 = this.maxX;
                                 if (var5 > var8) {
                                    var5 = var8;
                                 }
                              }

                              var8 = this.minY;
                              if (var6 < var8) {
                                 var6 = var8;
                              } else {
                                 var8 = this.maxY;
                                 if (var6 > var8) {
                                    var6 = var8;
                                 }
                              }

                              this.animateTo(3.0F, var5, var6, true);
                           } else {
                              this.checkMinMax(true);
                           }

                           this.zooming = false;
                        } else if (this.draggingDown) {
                           if (Math.abs(this.dragY - var1.getY()) > (float)this.getContainerViewHeight() / 6.0F) {
                              this.closePhoto(true, false);
                           } else {
                              if (this.pickerView.getVisibility() == 0) {
                                 this.toggleActionBar(true, true);
                                 this.toggleCheckImageView(true);
                              }

                              this.animateTo(1.0F, 0.0F, 0.0F, false);
                           }

                           this.draggingDown = false;
                        } else if (this.moving) {
                           var8 = this.translationX;
                           var6 = this.translationY;
                           this.updateMinMax(this.scale);
                           this.moving = false;
                           this.canDragDown = true;
                           var10 = this.velocityTracker;
                           var5 = var3;
                           if (var10 != null) {
                              var5 = var3;
                              if (this.scale == 1.0F) {
                                 var10.computeCurrentVelocity(1000);
                                 var5 = this.velocityTracker.getXVelocity();
                              }
                           }

                           if (this.currentEditMode == 0 && this.sendPhotoType != 1) {
                              if ((this.translationX < this.minX - (float)(this.getContainerViewWidth() / 3) || var5 < (float)(-AndroidUtilities.dp(650.0F))) && this.rightImage.hasImageSet()) {
                                 this.goToNext();
                                 return true;
                              }

                              if ((this.translationX > this.maxX + (float)(this.getContainerViewWidth() / 3) || var5 > (float)AndroidUtilities.dp(650.0F)) && this.leftImage.hasImageSet()) {
                                 this.goToPrev();
                                 return true;
                              }
                           }

                           var4 = this.translationX;
                           var5 = this.minX;
                           if (var4 >= var5) {
                              var5 = this.maxX;
                              if (var4 <= var5) {
                                 var5 = var8;
                              }
                           }

                           var4 = this.translationY;
                           var8 = this.minY;
                           if (var4 < var8) {
                              var6 = var8;
                           } else {
                              var8 = this.maxY;
                              if (var4 > var8) {
                                 var6 = var8;
                              }
                           }

                           this.animateTo(this.scale, var5, var6, false);
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
                           var5 = var1.getY();
                           this.moveStartY = var5;
                           this.dragY = var5;
                           this.draggingDown = false;
                           this.canDragDown = true;
                           var10 = this.velocityTracker;
                           if (var10 != null) {
                              var10.clear();
                           }
                        }
                     }
                  }

                  return false;
               }

               if (var1.getAction() == 1) {
                  this.closeCaptionEnter(true);
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private void openCaptionEnter() {
      if (this.imageMoveAnimation == null && this.changeModeAnimation == null && this.currentEditMode == 0) {
         int var1 = this.sendPhotoType;
         if (var1 != 1 && var1 != 3) {
            this.selectedPhotosListView.setVisibility(8);
            this.selectedPhotosListView.setEnabled(false);
            this.selectedPhotosListView.setAlpha(0.0F);
            this.selectedPhotosListView.setTranslationY((float)(-AndroidUtilities.dp(10.0F)));
            this.photosCounterView.setRotationX(0.0F);
            this.isPhotosListViewVisible = false;
            this.captionEditText.setTag(1);
            this.captionEditText.openKeyboard();
            this.captionEditText.setImportantForAccessibility(0);
            this.lastTitle = this.actionBar.getTitle();
            if (this.isCurrentVideo) {
               ActionBar var2 = this.actionBar;
               String var3;
               if (this.muteVideo) {
                  var1 = 2131559589;
                  var3 = "GifCaption";
               } else {
                  var1 = 2131561043;
                  var3 = "VideoCaption";
               }

               var2.setTitle(LocaleController.getString(var3, var1));
               this.actionBar.setSubtitle((CharSequence)null);
            } else {
               this.actionBar.setTitle(LocaleController.getString("PhotoCaption", 2131560434));
            }
         }
      }

   }

   private void preparePlayer(Uri var1, boolean var2, boolean var3) {
      if (!var3) {
         this.currentPlayingVideoFile = var1;
      }

      if (this.parentActivity != null) {
         byte var4 = 0;
         this.streamingAlertShown = false;
         this.startedPlayTime = SystemClock.elapsedRealtime();
         this.currentVideoFinishedLoading = false;
         this.lastBufferedPositionCheck = 0L;
         boolean var5 = true;
         this.firstAnimationDelay = true;
         this.inPreview = var3;
         this.releasePlayer(false);
         if (this.videoTextureView == null) {
            this.aspectRatioFrameLayout = new AspectRatioFrameLayout(this.parentActivity) {
               protected void onMeasure(int var1, int var2) {
                  super.onMeasure(var1, var2);
                  if (PhotoViewer.this.textureImageView != null) {
                     android.view.ViewGroup.LayoutParams var3 = PhotoViewer.this.textureImageView.getLayoutParams();
                     var3.width = this.getMeasuredWidth();
                     var3.height = this.getMeasuredHeight();
                  }

               }
            };
            this.aspectRatioFrameLayout.setVisibility(4);
            this.containerView.addView(this.aspectRatioFrameLayout, 0, LayoutHelper.createFrame(-1, -1, 17));
            this.videoTextureView = new TextureView(this.parentActivity);
            SurfaceTexture var6 = this.injectingVideoPlayerSurface;
            if (var6 != null) {
               this.videoTextureView.setSurfaceTexture(var6);
               this.textureUploaded = true;
               this.injectingVideoPlayerSurface = null;
            }

            this.videoTextureView.setPivotX(0.0F);
            this.videoTextureView.setPivotY(0.0F);
            this.videoTextureView.setOpaque(false);
            this.aspectRatioFrameLayout.addView(this.videoTextureView, LayoutHelper.createFrame(-1, -1, 17));
         }

         if (VERSION.SDK_INT >= 21 && this.textureImageView == null) {
            this.textureImageView = new ImageView(this.parentActivity);
            this.textureImageView.setBackgroundColor(-65536);
            this.textureImageView.setPivotX(0.0F);
            this.textureImageView.setPivotY(0.0F);
            this.textureImageView.setVisibility(4);
            this.containerView.addView(this.textureImageView);
         }

         this.textureUploaded = false;
         this.videoCrossfadeStarted = false;
         TextureView var9 = this.videoTextureView;
         this.videoCrossfadeAlpha = 0.0F;
         var9.setAlpha(0.0F);
         this.videoPlayButton.setImageResource(2131165479);
         this.playerWasReady = false;
         if (this.videoPlayer == null) {
            VideoPlayer var11 = this.injectingVideoPlayer;
            if (var11 != null) {
               this.videoPlayer = var11;
               this.injectingVideoPlayer = null;
               this.playerInjected = true;
               this.updatePlayerState(this.videoPlayer.getPlayWhenReady(), this.videoPlayer.getPlaybackState());
               var5 = false;
            } else {
               this.videoPlayer = new VideoPlayer();
            }

            this.videoPlayer.setTextureView(this.videoTextureView);
            this.videoPlayer.setDelegate(new VideoPlayer.VideoPlayerDelegate() {
               // $FF: synthetic method
               public void lambda$onError$0$PhotoViewer$26(DialogInterface var1, int var2) {
                  try {
                     AndroidUtilities.openForView(PhotoViewer.this.currentMessageObject, PhotoViewer.this.parentActivity);
                     PhotoViewer.this.closePhoto(false, false);
                  } catch (Exception var3) {
                     FileLog.e((Throwable)var3);
                  }

               }

               public void onError(Exception var1) {
                  FileLog.e((Throwable)var1);
                  if (PhotoViewer.this.menuItem.isSubItemVisible(11)) {
                     AlertDialog.Builder var2 = new AlertDialog.Builder(PhotoViewer.this.parentActivity);
                     var2.setTitle(LocaleController.getString("AppName", 2131558635));
                     var2.setMessage(LocaleController.getString("CantPlayVideo", 2131558903));
                     var2.setPositiveButton(LocaleController.getString("Open", 2131560110), new _$$Lambda$PhotoViewer$26$aI6m9nXQqJHr0j_z6Zhi3c59_Vo(this));
                     var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                     PhotoViewer.this.showAlertDialog(var2);
                  }
               }

               public void onRenderedFirstFrame() {
                  if (!PhotoViewer.this.textureUploaded) {
                     PhotoViewer.this.textureUploaded = true;
                     PhotoViewer.this.containerView.invalidate();
                  }

               }

               public void onStateChanged(boolean var1, int var2) {
                  PhotoViewer.this.updatePlayerState(var1, var2);
               }

               public boolean onSurfaceDestroyed(SurfaceTexture var1) {
                  if (PhotoViewer.this.changingTextureView) {
                     PhotoViewer.this.changingTextureView = false;
                     if (PhotoViewer.this.isInline) {
                        if (PhotoViewer.this.isInline) {
                           PhotoViewer.this.waitingForFirstTextureUpload = 1;
                        }

                        PhotoViewer.this.changedTextureView.setSurfaceTexture(var1);
                        PhotoViewer.this.changedTextureView.setSurfaceTextureListener(PhotoViewer.this.surfaceTextureListener);
                        PhotoViewer.this.changedTextureView.setVisibility(0);
                        return true;
                     }
                  }

                  return false;
               }

               public void onSurfaceTextureUpdated(SurfaceTexture var1) {
                  if (PhotoViewer.this.waitingForFirstTextureUpload == 2) {
                     if (PhotoViewer.this.textureImageView != null) {
                        PhotoViewer.this.textureImageView.setVisibility(4);
                        PhotoViewer.this.textureImageView.setImageDrawable((Drawable)null);
                        if (PhotoViewer.this.currentBitmap != null) {
                           PhotoViewer.this.currentBitmap.recycle();
                           PhotoViewer.this.currentBitmap = null;
                        }
                     }

                     PhotoViewer.this.switchingInlineMode = false;
                     if (VERSION.SDK_INT >= 21) {
                        PhotoViewer.this.aspectRatioFrameLayout.getLocationInWindow(PhotoViewer.this.pipPosition);
                        int[] var2 = PhotoViewer.this.pipPosition;
                        var2[1] = (int)((float)var2[1] - PhotoViewer.this.containerView.getTranslationY());
                        PhotoViewer.this.textureImageView.setTranslationX(PhotoViewer.this.textureImageView.getTranslationX() + (float)PhotoViewer.this.getLeftInset());
                        PhotoViewer.this.videoTextureView.setTranslationX(PhotoViewer.this.videoTextureView.getTranslationX() + (float)PhotoViewer.this.getLeftInset() - PhotoViewer.this.aspectRatioFrameLayout.getX());
                        AnimatorSet var3 = new AnimatorSet();
                        var3.playTogether(new Animator[]{ObjectAnimator.ofFloat(PhotoViewer.this.textureImageView, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.textureImageView, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.textureImageView, View.TRANSLATION_X, new float[]{(float)PhotoViewer.this.pipPosition[0]}), ObjectAnimator.ofFloat(PhotoViewer.this.textureImageView, View.TRANSLATION_Y, new float[]{(float)PhotoViewer.this.pipPosition[1]}), ObjectAnimator.ofFloat(PhotoViewer.this.videoTextureView, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.videoTextureView, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.videoTextureView, View.TRANSLATION_X, new float[]{(float)PhotoViewer.this.pipPosition[0] - PhotoViewer.this.aspectRatioFrameLayout.getX()}), ObjectAnimator.ofFloat(PhotoViewer.this.videoTextureView, View.TRANSLATION_Y, new float[]{(float)PhotoViewer.this.pipPosition[1] - PhotoViewer.this.aspectRatioFrameLayout.getY()}), ObjectAnimator.ofInt(PhotoViewer.this.backgroundDrawable, AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[]{255}), ObjectAnimator.ofFloat(PhotoViewer.this.actionBar, View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.bottomLayout, View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.captionTextView, View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.groupedPhotosListView, View.ALPHA, new float[]{1.0F})});
                        var3.setInterpolator(new DecelerateInterpolator());
                        var3.setDuration(250L);
                        var3.addListener(new AnimatorListenerAdapter() {
                           public void onAnimationEnd(Animator var1) {
                              PhotoViewer.this.pipAnimationInProgress = false;
                           }
                        });
                        var3.start();
                     }

                     PhotoViewer.this.waitingForFirstTextureUpload = 0;
                  }

               }

               public void onVideoSizeChanged(int var1, int var2, int var3, float var4) {
                  if (PhotoViewer.this.aspectRatioFrameLayout != null) {
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

                     AspectRatioFrameLayout var7 = PhotoViewer.this.aspectRatioFrameLayout;
                     if (var5 == 0) {
                        var4 = 1.0F;
                     } else {
                        var4 = (float)var6 * var4 / (float)var5;
                     }

                     var7.setAspectRatio(var4, var3);
                  }

               }
            });
         } else {
            var5 = false;
         }

         if (var5) {
            this.seekToProgressPending = this.seekToProgressPending2;
            this.videoPlayer.preparePlayer(var1, "other");
            this.videoPlayerSeekbar.setProgress(0.0F);
            this.videoTimelineView.setProgress(0.0F);
            this.videoPlayerSeekbar.setBufferedProgress(0.0F);
            this.videoPlayer.setPlayWhenReady(var2);
         }

         TLRPC.BotInlineResult var7 = this.currentBotInlineResult;
         if (var7 == null || !var7.type.equals("video") && !MessageObject.isVideoDocument(this.currentBotInlineResult.document)) {
            this.bottomLayout.setPadding(0, 0, 0, 0);
         } else {
            this.bottomLayout.setVisibility(0);
            this.bottomLayout.setPadding(0, 0, AndroidUtilities.dp(84.0F), 0);
            this.pickerView.setVisibility(8);
         }

         FrameLayout var8 = this.videoPlayerControlFrameLayout;
         byte var10 = var4;
         if (this.isCurrentVideo) {
            var10 = 8;
         }

         var8.setVisibility(var10);
         this.dateTextView.setVisibility(8);
         this.nameTextView.setVisibility(8);
         if (this.allowShare) {
            this.shareButton.setVisibility(8);
            this.menuItem.showSubItem(10);
         }

         this.inPreview = var3;
         this.updateAccessibilityOverlayVisibility();
      }
   }

   private void processOpenVideo(final String var1, boolean var2) {
      if (this.currentLoadingVideoRunnable != null) {
         Utilities.globalQueue.cancelRunnable(this.currentLoadingVideoRunnable);
         this.currentLoadingVideoRunnable = null;
      }

      this.videoPreviewMessageObject = null;
      this.setCompressItemEnabled(false, true);
      this.muteVideo = var2;
      this.videoTimelineView.setVideoPath(var1);
      this.compressionsCount = -1;
      this.rotationValue = 0;
      this.videoFramerate = 25;
      this.originalSize = (new File(var1)).length();
      DispatchQueue var3 = Utilities.globalQueue;
      Runnable var4 = new Runnable() {
         // $FF: synthetic method
         public void lambda$run$0$PhotoViewer$43(int[] var1x) {
            if (PhotoViewer.this.parentActivity != null) {
               PhotoViewer var2 = PhotoViewer.this;
               boolean var3;
               if (var1x[0] != 0) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               var2.videoHasAudio = var3;
               PhotoViewer.this.audioFramesSize = (long)var1x[5];
               PhotoViewer.this.videoFramesSize = (long)var1x[6];
               PhotoViewer.this.videoDuration = (float)var1x[4];
               var2 = PhotoViewer.this;
               int var4 = var1x[3];
               var2.bitrate = var4;
               var2.originalBitrate = var4;
               PhotoViewer.this.videoFramerate = var1x[7];
               if (PhotoViewer.this.bitrate > 900000) {
                  PhotoViewer.this.bitrate = 900000;
               }

               if (PhotoViewer.this.videoHasAudio) {
                  PhotoViewer.this.rotationValue = var1x[8];
                  var2 = PhotoViewer.this;
                  var4 = var1x[1];
                  var2.originalWidth = var4;
                  var2.resultWidth = var4;
                  var2 = PhotoViewer.this;
                  var4 = var1x[2];
                  var2.originalHeight = var4;
                  var2.resultHeight = var4;
                  SharedPreferences var13 = MessagesController.getGlobalMainSettings();
                  PhotoViewer.this.selectedCompression = var13.getInt("compress_video2", 1);
                  if (PhotoViewer.this.originalWidth <= 1280 && PhotoViewer.this.originalHeight <= 1280) {
                     if (PhotoViewer.this.originalWidth <= 854 && PhotoViewer.this.originalHeight <= 854) {
                        if (PhotoViewer.this.originalWidth <= 640 && PhotoViewer.this.originalHeight <= 640) {
                           if (PhotoViewer.this.originalWidth <= 480 && PhotoViewer.this.originalHeight <= 480) {
                              PhotoViewer.this.compressionsCount = 1;
                           } else {
                              PhotoViewer.this.compressionsCount = 2;
                           }
                        } else {
                           PhotoViewer.this.compressionsCount = 3;
                        }
                     } else {
                        PhotoViewer.this.compressionsCount = 4;
                     }
                  } else {
                     PhotoViewer.this.compressionsCount = 5;
                  }

                  PhotoViewer.this.updateWidthHeightBitrateForCompression();
                  PhotoViewer var14 = PhotoViewer.this;
                  if (var14.compressionsCount > 1) {
                     var3 = true;
                  } else {
                     var3 = false;
                  }

                  var14.setCompressItemEnabled(var3, true);
                  if (BuildVars.LOGS_ENABLED) {
                     StringBuilder var15 = new StringBuilder();
                     var15.append("compressionsCount = ");
                     var15.append(PhotoViewer.this.compressionsCount);
                     var15.append(" w = ");
                     var15.append(PhotoViewer.this.originalWidth);
                     var15.append(" h = ");
                     var15.append(PhotoViewer.this.originalHeight);
                     FileLog.d(var15.toString());
                  }

                  if (VERSION.SDK_INT < 18 && PhotoViewer.this.compressItem.getTag() != null) {
                     label173: {
                        Exception var10000;
                        label140: {
                           boolean var10001;
                           MediaCodecInfo var18;
                           try {
                              var18 = MediaController.selectCodec("video/avc");
                           } catch (Exception var12) {
                              var10000 = var12;
                              var10001 = false;
                              break label140;
                           }

                           if (var18 == null) {
                              label106: {
                                 try {
                                    if (BuildVars.LOGS_ENABLED) {
                                       FileLog.d("no codec info for video/avc");
                                    }
                                 } catch (Exception var6) {
                                    var10000 = var6;
                                    var10001 = false;
                                    break label106;
                                 }

                                 try {
                                    PhotoViewer.this.setCompressItemEnabled(false, true);
                                    break label173;
                                 } catch (Exception var5) {
                                    var10000 = var5;
                                    var10001 = false;
                                 }
                              }
                           } else {
                              label167: {
                                 String var16;
                                 label181: {
                                    label133:
                                    try {
                                       var16 = var18.getName();
                                       if (!var16.equals("OMX.google.h264.encoder") && !var16.equals("OMX.ST.VFM.H264Enc") && !var16.equals("OMX.Exynos.avc.enc") && !var16.equals("OMX.MARVELL.VIDEO.HW.CODA7542ENCODER") && !var16.equals("OMX.MARVELL.VIDEO.H264ENCODER") && !var16.equals("OMX.k3.video.encoder.avc") && !var16.equals("OMX.TI.DUCATI1.VIDEO.H264E")) {
                                          break label133;
                                       }
                                       break label181;
                                    } catch (Exception var11) {
                                       var10000 = var11;
                                       var10001 = false;
                                       break label167;
                                    }

                                    try {
                                       if (MediaController.selectColorFormat(var18, "video/avc") != 0) {
                                          break label173;
                                       }

                                       if (BuildVars.LOGS_ENABLED) {
                                          FileLog.d("no color format for video/avc");
                                       }
                                    } catch (Exception var8) {
                                       var10000 = var8;
                                       var10001 = false;
                                       break label167;
                                    }

                                    try {
                                       PhotoViewer.this.setCompressItemEnabled(false, true);
                                       break label173;
                                    } catch (Exception var7) {
                                       var10000 = var7;
                                       var10001 = false;
                                       break label167;
                                    }
                                 }

                                 try {
                                    if (BuildVars.LOGS_ENABLED) {
                                       StringBuilder var19 = new StringBuilder();
                                       var19.append("unsupported encoder = ");
                                       var19.append(var16);
                                       FileLog.d(var19.toString());
                                    }
                                 } catch (Exception var10) {
                                    var10000 = var10;
                                    var10001 = false;
                                    break label167;
                                 }

                                 try {
                                    PhotoViewer.this.setCompressItemEnabled(false, true);
                                    break label173;
                                 } catch (Exception var9) {
                                    var10000 = var9;
                                    var10001 = false;
                                 }
                              }
                           }
                        }

                        Exception var17 = var10000;
                        PhotoViewer.this.setCompressItemEnabled(false, true);
                        FileLog.e((Throwable)var17);
                     }
                  }

                  PhotoViewer.this.qualityChooseView.invalidate();
               } else {
                  PhotoViewer.this.compressionsCount = 0;
               }

               PhotoViewer.this.updateVideoInfo();
               PhotoViewer.this.updateMuteButton();
            }
         }

         public void run() {
            if (PhotoViewer.this.currentLoadingVideoRunnable == this) {
               int[] var1x = new int[9];
               AnimatedFileDrawable.getVideoInfo(var1, var1x);
               if (PhotoViewer.this.currentLoadingVideoRunnable == this) {
                  PhotoViewer.this.currentLoadingVideoRunnable = null;
                  AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$43$_nOxRPCy18s7poWP44IsgujSQ7Q(this, var1x));
               }
            }
         }
      };
      this.currentLoadingVideoRunnable = var4;
      var3.postRunnable(var4);
   }

   private void redraw(int var1) {
      if (var1 < 6) {
         PhotoViewer.FrameLayoutDrawer var2 = this.containerView;
         if (var2 != null) {
            var2.invalidate();
            AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$PaqzhTFF8kO_z23DxN4SVmAhhn4(this, var1), 100L);
         }
      }

   }

   private void releasePlayer(boolean var1) {
      if (this.videoPlayer != null) {
         AndroidUtilities.cancelRunOnUIThread(this.setLoadingRunnable);
         this.videoPlayer.releasePlayer(true);
         this.videoPlayer = null;
         this.updateAccessibilityOverlayVisibility();
      }

      this.toggleMiniProgress(false, false);
      this.pipAvailable = false;
      this.playerInjected = false;
      if (this.pipItem.isEnabled()) {
         this.pipItem.setEnabled(false);
         this.pipItem.setAlpha(0.5F);
      }

      if (this.keepScreenOnFlagSet) {
         try {
            this.parentActivity.getWindow().clearFlags(128);
            this.keepScreenOnFlagSet = false;
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }
      }

      AspectRatioFrameLayout var2 = this.aspectRatioFrameLayout;
      if (var2 != null) {
         try {
            this.containerView.removeView(var2);
         } catch (Throwable var3) {
         }

         this.aspectRatioFrameLayout = null;
      }

      if (this.videoTextureView != null) {
         this.videoTextureView = null;
      }

      if (this.isPlaying) {
         this.isPlaying = false;
         if (!var1) {
            this.videoPlayButton.setImageResource(2131165479);
         }

         AndroidUtilities.cancelRunOnUIThread(this.updateProgressRunnable);
      }

      if (!var1 && !this.inPreview && !this.requestingPreview) {
         this.videoPlayerControlFrameLayout.setVisibility(8);
         this.dateTextView.setVisibility(0);
         this.nameTextView.setVisibility(0);
         if (this.allowShare) {
            this.shareButton.setVisibility(0);
            this.menuItem.hideSubItem(10);
         }
      }

   }

   private void removeObservers() {
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidFailedLoad);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidLoad);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileLoadProgressChanged);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mediaCountDidLoad);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mediaDidLoad);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogPhotosLoaded);
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.filePreparingFailed);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileNewChunkAvailable);
      ConnectionsManager.getInstance(this.currentAccount).cancelRequestsForGuid(this.classGuid);
   }

   private void requestVideoPreview(int var1) {
      if (this.videoPreviewMessageObject != null) {
         MediaController.getInstance().cancelVideoConvert(this.videoPreviewMessageObject);
      }

      boolean var2;
      if (this.requestingPreview && !this.tryStartRequestPreviewOnFinish) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.requestingPreview = false;
      this.loadInitialVideo = false;
      this.progressView.setVisibility(4);
      if (var1 == 1) {
         if (this.selectedCompression == this.compressionsCount - 1) {
            this.tryStartRequestPreviewOnFinish = false;
            if (!var2) {
               this.preparePlayer(this.currentPlayingVideoFile, false, false);
            } else {
               this.progressView.setVisibility(0);
               this.loadInitialVideo = true;
            }
         } else {
            this.requestingPreview = true;
            this.releasePlayer(false);
            VideoEditedInfo var10;
            if (this.videoPreviewMessageObject == null) {
               TLRPC.TL_message var3 = new TLRPC.TL_message();
               var3.id = 0;
               var3.message = "";
               var3.media = new TLRPC.TL_messageMediaEmpty();
               var3.action = new TLRPC.TL_messageActionEmpty();
               this.videoPreviewMessageObject = new MessageObject(UserConfig.selectedAccount, var3, false);
               this.videoPreviewMessageObject.messageOwner.attachPath = (new File(FileLoader.getDirectory(4), "video_preview.mp4")).getAbsolutePath();
               this.videoPreviewMessageObject.videoEditedInfo = new VideoEditedInfo();
               var10 = this.videoPreviewMessageObject.videoEditedInfo;
               var10.rotationValue = this.rotationValue;
               var10.originalWidth = this.originalWidth;
               var10.originalHeight = this.originalHeight;
               var10.framerate = this.videoFramerate;
               var10.originalPath = this.currentPlayingVideoFile.getPath();
            }

            var10 = this.videoPreviewMessageObject.videoEditedInfo;
            long var4 = this.startTime;
            var10.startTime = var4;
            long var6 = this.endTime;
            var10.endTime = var6;
            long var8 = var4;
            if (var4 == -1L) {
               var8 = 0L;
            }

            var4 = var6;
            if (var6 == -1L) {
               var4 = (long)(this.videoDuration * 1000.0F);
            }

            if (var4 - var8 > 5000000L) {
               this.videoPreviewMessageObject.videoEditedInfo.endTime = var8 + 5000000L;
            }

            var10 = this.videoPreviewMessageObject.videoEditedInfo;
            var10.bitrate = this.bitrate;
            var10.resultWidth = this.resultWidth;
            var10.resultHeight = this.resultHeight;
            if (!MediaController.getInstance().scheduleVideoConvert(this.videoPreviewMessageObject, true)) {
               this.tryStartRequestPreviewOnFinish = true;
            }

            this.requestingPreview = true;
            this.progressView.setVisibility(0);
         }
      } else {
         this.tryStartRequestPreviewOnFinish = false;
         if (var1 == 2) {
            this.preparePlayer(this.currentPlayingVideoFile, false, false);
         }
      }

      this.containerView.invalidate();
   }

   private void setCompressItemEnabled(boolean var1, boolean var2) {
      ImageView var3 = this.compressItem;
      if (var3 != null) {
         if ((!var1 || var3.getTag() == null) && (var1 || this.compressItem.getTag() != null)) {
            ImageView var4 = this.compressItem;
            Integer var7;
            if (var1) {
               var7 = 1;
            } else {
               var7 = null;
            }

            var4.setTag(var7);
            this.compressItem.setEnabled(var1);
            this.compressItem.setClickable(var1);
            AnimatorSet var8 = this.compressItemAnimation;
            if (var8 != null) {
               var8.cancel();
               this.compressItemAnimation = null;
            }

            float var5 = 1.0F;
            if (var2) {
               this.compressItemAnimation = new AnimatorSet();
               AnimatorSet var6 = this.compressItemAnimation;
               var3 = this.compressItem;
               Property var9 = View.ALPHA;
               if (!var1) {
                  var5 = 0.5F;
               }

               var6.playTogether(new Animator[]{ObjectAnimator.ofFloat(var3, var9, new float[]{var5})});
               this.compressItemAnimation.setDuration(180L);
               this.compressItemAnimation.setInterpolator(decelerateInterpolator);
               this.compressItemAnimation.start();
            } else {
               var3 = this.compressItem;
               if (!var1) {
                  var5 = 0.5F;
               }

               var3.setAlpha(var5);
            }

         }
      }
   }

   private void setCropBitmap() {
      if (this.sendPhotoType == 1) {
         Bitmap var1 = this.centerImage.getBitmap();
         int var2 = this.centerImage.getOrientation();
         Bitmap var3 = var1;
         if (var1 == null) {
            var3 = this.animatingImageView.getBitmap();
            var2 = this.animatingImageView.getOrientation();
         }

         if (var3 != null) {
            this.photoCropView.setBitmap(var3, var2, false, false);
            if (this.currentEditMode == 0) {
               this.setCropTranslations(false);
            }
         }

      }
   }

   private void setCropTranslations(boolean var1) {
      if (this.sendPhotoType == 1) {
         int var2 = this.centerImage.getBitmapWidth();
         int var3 = this.centerImage.getBitmapHeight();
         if (var2 != 0 && var3 != 0) {
            float var4 = (float)this.getContainerViewWidth();
            float var5 = (float)var2;
            float var6 = var4 / var5;
            var4 = (float)this.getContainerViewHeight();
            float var7 = (float)var3;
            float var8 = var4 / var7;
            var4 = var6;
            if (var6 > var8) {
               var4 = var8;
            }

            var8 = (float)Math.min(this.getContainerViewWidth(1), this.getContainerViewHeight(1));
            var6 = var8 / var5;
            var8 /= var7;
            if (var6 <= var8) {
               var6 = var8;
            }

            if (var1) {
               this.animationStartTime = System.currentTimeMillis();
               this.animateToX = (float)(this.getLeftInset() / 2 - this.getRightInset() / 2);
               var2 = this.currentEditMode;
               if (var2 == 2) {
                  this.animateToY = (float)(AndroidUtilities.dp(92.0F) - AndroidUtilities.dp(56.0F));
               } else if (var2 == 3) {
                  this.animateToY = (float)(AndroidUtilities.dp(44.0F) - AndroidUtilities.dp(56.0F));
               }

               this.animateToScale = var6 / var4;
               this.zoomAnimation = true;
            } else {
               this.animationStartTime = 0L;
               this.translationX = (float)(this.getLeftInset() / 2 - this.getRightInset() / 2);
               var3 = -AndroidUtilities.dp(56.0F);
               if (VERSION.SDK_INT >= 21) {
                  var2 = AndroidUtilities.statusBarHeight / 2;
               } else {
                  var2 = 0;
               }

               this.translationY = (float)(var3 + var2);
               this.scale = var6 / var4;
               this.updateMinMax(this.scale);
            }
         }

      }
   }

   private void setCurrentCaption(MessageObject var1, CharSequence var2, boolean var3) {
      if (this.needCaptionLayout) {
         if (this.captionTextView.getParent() != this.pickerView) {
            this.captionTextView.setBackgroundDrawable((Drawable)null);
            this.containerView.removeView(this.captionTextView);
            this.pickerView.addView(this.captionTextView, LayoutHelper.createFrame(-1, -2.0F, 83, 0.0F, 0.0F, 76.0F, 48.0F));
         }
      } else if (this.captionTextView.getParent() != this.containerView) {
         this.captionTextView.setBackgroundColor(2130706432);
         this.pickerView.removeView(this.captionTextView);
         this.containerView.addView(this.captionTextView, LayoutHelper.createFrame(-1, -2.0F, 83, 0.0F, 0.0F, 0.0F, 48.0F));
      }

      if (this.isCurrentVideo) {
         this.captionTextView.setMaxLines(1);
         this.captionTextView.setSingleLine(true);
      } else {
         this.captionTextView.setSingleLine(false);
         this.captionTextView.setMaxLines(10);
      }

      boolean var4;
      if (this.captionTextView.getTag() != null) {
         var4 = true;
      } else {
         var4 = false;
      }

      if (!TextUtils.isEmpty(var2)) {
         Theme.createChatResources((Context)null, true);
         CharSequence var7;
         if (var1 != null && !var1.messageOwner.entities.isEmpty()) {
            SpannableString var8 = SpannableString.valueOf(var2.toString());
            var1.addEntitiesToText(var8, true, false);
            var7 = Emoji.replaceEmoji(var8, this.captionTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
         } else {
            var7 = Emoji.replaceEmoji(new SpannableStringBuilder(var2), this.captionTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
         }

         this.captionTextView.setTag(var7);
         AnimatorSet var9 = this.currentCaptionAnimation;
         if (var9 != null) {
            var9.cancel();
            this.currentCaptionAnimation = null;
         }

         try {
            this.captionTextView.setText(var7);
         } catch (Exception var6) {
            FileLog.e((Throwable)var6);
         }

         this.captionTextView.setScrollY(0);
         this.captionTextView.setTextColor(-1);
         boolean var5;
         if (!this.isActionBarVisible || this.bottomLayout.getVisibility() != 0 && this.pickerView.getVisibility() != 0) {
            var5 = false;
         } else {
            var5 = true;
         }

         if (var5) {
            this.captionTextView.setVisibility(0);
            if (var3 && !var4) {
               this.currentCaptionAnimation = new AnimatorSet();
               this.currentCaptionAnimation.setDuration(200L);
               this.currentCaptionAnimation.setInterpolator(decelerateInterpolator);
               this.currentCaptionAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     if (var1.equals(PhotoViewer.this.currentCaptionAnimation)) {
                        PhotoViewer.this.currentCaptionAnimation = null;
                     }

                  }
               });
               this.currentCaptionAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.captionTextView, View.ALPHA, new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.captionTextView, View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(5.0F), 0.0F})});
               this.currentCaptionAnimation.start();
            } else {
               this.captionTextView.setAlpha(1.0F);
            }
         } else if (this.captionTextView.getVisibility() == 0) {
            this.captionTextView.setVisibility(4);
            this.captionTextView.setAlpha(0.0F);
         }
      } else if (this.needCaptionLayout) {
         this.captionTextView.setText(LocaleController.getString("AddCaption", 2131558566));
         this.captionTextView.setTag("empty");
         this.captionTextView.setVisibility(0);
         this.captionTextView.setTextColor(-1291845633);
      } else {
         this.captionTextView.setTextColor(-1);
         this.captionTextView.setTag((Object)null);
         if (var3 && var4) {
            this.currentCaptionAnimation = new AnimatorSet();
            this.currentCaptionAnimation.setDuration(200L);
            this.currentCaptionAnimation.setInterpolator(decelerateInterpolator);
            this.currentCaptionAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationCancel(Animator var1) {
                  if (var1.equals(PhotoViewer.this.currentCaptionAnimation)) {
                     PhotoViewer.this.currentCaptionAnimation = null;
                  }

               }

               public void onAnimationEnd(Animator var1) {
                  if (var1.equals(PhotoViewer.this.currentCaptionAnimation)) {
                     PhotoViewer.this.captionTextView.setVisibility(4);
                     PhotoViewer.this.currentCaptionAnimation = null;
                  }

               }
            });
            this.currentCaptionAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.captionTextView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.captionTextView, View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(5.0F)})});
            this.currentCaptionAnimation.start();
         } else {
            this.captionTextView.setVisibility(4);
         }
      }

   }

   private void setDoubleTapEnabled(boolean var1) {
      this.doubleTapEnabled = var1;
      GestureDetector var2 = this.gestureDetector;
      PhotoViewer var3;
      if (var1) {
         var3 = this;
      } else {
         var3 = null;
      }

      var2.setOnDoubleTapListener(var3);
   }

   private void setImageIndex(int var1, boolean var2) {
      if (this.currentIndex != var1 && this.placeProvider != null) {
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
         this.placeProvider.willSwitchFromPhoto(this.currentMessageObject, this.getFileLocation(this.currentFileLocation), this.currentIndex);
         int var4 = this.currentIndex;
         this.currentIndex = var1;
         this.setIsAboutToSwitchToIndex(this.currentIndex, var2);
         boolean var6;
         boolean var8;
         Uri var10;
         if (!this.imagesArr.isEmpty()) {
            var1 = this.currentIndex;
            if (var1 < 0 || var1 >= this.imagesArr.size()) {
               this.closePhoto(false, false);
               return;
            }

            MessageObject var5;
            label164: {
               var5 = (MessageObject)this.imagesArr.get(this.currentIndex);
               if (var2) {
                  MessageObject var9 = this.currentMessageObject;
                  if (var9 != null && var9.getId() == var5.getId()) {
                     var8 = true;
                     break label164;
                  }
               }

               var8 = false;
            }

            this.currentMessageObject = var5;
            var2 = var5.isVideo();
            if (this.sharedMediaType == 1) {
               var6 = var5.canPreviewDocument();
               this.canZoom = var6;
               if (var6) {
                  this.menuItem.showSubItem(1);
                  this.setDoubleTapEnabled(true);
               } else {
                  this.menuItem.hideSubItem(1);
                  this.setDoubleTapEnabled(false);
               }
            }

            var10 = null;
         } else {
            label197: {
               label196: {
                  label195: {
                     if (!this.secureDocuments.isEmpty()) {
                        if (var1 < 0 || var1 >= this.secureDocuments.size()) {
                           this.closePhoto(false, false);
                           return;
                        }

                        this.currentSecureDocument = (SecureDocument)this.secureDocuments.get(var1);
                     } else {
                        if (!this.imagesArrLocations.isEmpty()) {
                           if (var1 < 0 || var1 >= this.imagesArrLocations.size()) {
                              this.closePhoto(false, false);
                              return;
                           }

                           boolean var24;
                           label181: {
                              ImageLocation var20 = this.currentFileLocation;
                              ImageLocation var15 = (ImageLocation)this.imagesArrLocations.get(var1);
                              if (var2 && var20 != null && var15 != null) {
                                 TLRPC.TL_fileLocationToBeDeprecated var21 = var20.location;
                                 int var7 = var21.local_id;
                                 TLRPC.TL_fileLocationToBeDeprecated var17 = var15.location;
                                 if (var7 == var17.local_id && var21.volume_id == var17.volume_id) {
                                    var24 = true;
                                    break label181;
                                 }
                              }

                              var24 = false;
                           }

                           this.currentFileLocation = (ImageLocation)this.imagesArrLocations.get(var1);
                           var8 = var24;
                           break label195;
                        }

                        if (!this.imagesArrLocals.isEmpty()) {
                           if (var1 < 0 || var1 >= this.imagesArrLocals.size()) {
                              this.closePhoto(false, false);
                              return;
                           }

                           Object var11 = this.imagesArrLocals.get(var1);
                           if (var11 instanceof TLRPC.BotInlineResult) {
                              TLRPC.BotInlineResult var13 = (TLRPC.BotInlineResult)var11;
                              this.currentBotInlineResult = var13;
                              TLRPC.Document var16 = var13.document;
                              if (var16 != null) {
                                 this.currentPathObject = FileLoader.getPathToAttach(var16).getAbsolutePath();
                                 var2 = MessageObject.isVideoDocument(var13.document);
                              } else {
                                 label215: {
                                    TLRPC.Photo var18 = var13.photo;
                                    if (var18 != null) {
                                       this.currentPathObject = FileLoader.getPathToAttach(FileLoader.getClosestPhotoSizeWithSize(var18.sizes, AndroidUtilities.getPhotoSize())).getAbsolutePath();
                                    } else {
                                       TLRPC.WebDocument var19 = var13.content;
                                       if (var19 instanceof TLRPC.TL_webDocument) {
                                          this.currentPathObject = var19.url;
                                          var2 = var13.type.equals("video");
                                          break label215;
                                       }
                                    }

                                    var2 = false;
                                 }
                              }

                              var10 = null;
                              break label196;
                           }

                           if (var11 instanceof MediaController.PhotoEntry) {
                              MediaController.PhotoEntry var12 = (MediaController.PhotoEntry)var11;
                              String var14 = var12.path;
                              this.currentPathObject = var14;
                              var2 = var12.isVideo;
                              var10 = Uri.fromFile(new File(var14));
                              break label196;
                           }

                           if (var11 instanceof MediaController.SearchImage) {
                              this.currentPathObject = ((MediaController.SearchImage)var11).getPathToAttach();
                           }
                        }
                     }

                     var8 = false;
                  }

                  var10 = null;
                  var2 = false;
                  break label197;
               }

               var8 = false;
            }
         }

         PhotoViewer.PlaceProviderObject var23 = this.currentPlaceObject;
         if (var23 != null) {
            if (this.animationInProgress == 0) {
               var23.imageReceiver.setVisible(true, true);
            } else {
               this.showAfterAnimation = var23;
            }
         }

         this.currentPlaceObject = this.placeProvider.getPlaceForPhoto(this.currentMessageObject, this.getFileLocation(this.currentFileLocation), this.currentIndex, false);
         var23 = this.currentPlaceObject;
         if (var23 != null) {
            if (this.animationInProgress == 0) {
               var23.imageReceiver.setVisible(false, true);
            } else {
               this.hideAfterAnimation = var23;
            }
         }

         if (!var8) {
            this.draggingDown = false;
            this.translationX = 0.0F;
            this.translationY = 0.0F;
            this.scale = 1.0F;
            this.animateToX = 0.0F;
            this.animateToY = 0.0F;
            this.animateToScale = 1.0F;
            this.animationStartTime = 0L;
            this.imageMoveAnimation = null;
            this.changeModeAnimation = null;
            AspectRatioFrameLayout var26 = this.aspectRatioFrameLayout;
            if (var26 != null) {
               var26.setVisibility(4);
            }

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
            if (this.sharedMediaType != 1) {
               if (!this.imagesArrLocals.isEmpty() || this.currentFileNames[0] != null && this.photoProgressViews[0].backgroundState != 0) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               this.canZoom = var6;
            }

            this.updateMinMax(this.scale);
            this.releasePlayer(false);
         }

         if (var2 && var10 != null) {
            this.isStreaming = false;
            this.preparePlayer(var10, false, false);
         }

         if (var4 == -1) {
            this.setImages();

            for(var1 = 0; var1 < 3; ++var1) {
               this.checkProgress(var1, false);
            }
         } else {
            this.checkProgress(0, false);
            var1 = this.currentIndex;
            ImageReceiver var22;
            if (var4 > var1) {
               var22 = this.rightImage;
               this.rightImage = this.centerImage;
               this.centerImage = this.leftImage;
               this.leftImage = var22;
               PhotoViewer.PhotoProgressView[] var25 = this.photoProgressViews;
               PhotoViewer.PhotoProgressView var28 = var25[0];
               var25[0] = var25[2];
               var25[2] = var28;
               this.setIndexToImage(this.leftImage, var1 - 1);
               this.checkProgress(1, false);
               this.checkProgress(2, false);
            } else if (var4 < var1) {
               var22 = this.leftImage;
               this.leftImage = this.centerImage;
               this.centerImage = this.rightImage;
               this.rightImage = var22;
               PhotoViewer.PhotoProgressView[] var29 = this.photoProgressViews;
               PhotoViewer.PhotoProgressView var27 = var29[0];
               var29[0] = var29[1];
               var29[1] = var27;
               this.setIndexToImage(this.rightImage, var1 + 1);
               this.checkProgress(1, false);
               this.checkProgress(2, false);
            }
         }
      }

   }

   private void setImages() {
      if (this.animationInProgress == 0) {
         this.setIndexToImage(this.centerImage, this.currentIndex);
         this.setIndexToImage(this.rightImage, this.currentIndex + 1);
         this.setIndexToImage(this.leftImage, this.currentIndex - 1);
      }

   }

   private void setIndexToImage(ImageReceiver var1, int var2) {
      var1.setOrientation(0, false);
      boolean var3 = this.secureDocuments.isEmpty();
      BitmapDrawable var4 = null;
      MediaController.SearchImage var5 = null;
      ImageReceiver.BitmapHolder var7;
      ImageReceiver.BitmapHolder var8;
      if (!var3) {
         if (var2 >= 0 && var2 < this.secureDocuments.size()) {
            this.secureDocuments.get(var2);
            AndroidUtilities.getPhotoSize();
            float var6 = AndroidUtilities.density;
            var7 = this.currentThumb;
            if (var7 == null || var1 != this.centerImage) {
               var7 = null;
            }

            var8 = var7;
            if (var7 == null) {
               var8 = this.placeProvider.getThumbForPhoto((MessageObject)null, (TLRPC.FileLocation)null, var2);
            }

            SecureDocument var21 = (SecureDocument)this.secureDocuments.get(var2);
            var2 = var21.secureFile.size;
            ImageLocation var16 = ImageLocation.getForSecureDocument(var21);
            BitmapDrawable var22 = var5;
            if (var8 != null) {
               var22 = new BitmapDrawable(var8.bitmap);
            }

            var1.setImage(var16, "d", (ImageLocation)null, (String)null, var22, var2, (String)null, (Object)null, 0);
         }
      } else {
         Object var17;
         TLRPC.PhotoSize var24;
         ImageLocation var25;
         Object var29;
         BitmapDrawable var38;
         if (!this.imagesArrLocals.isEmpty()) {
            if (var2 >= 0 && var2 < this.imagesArrLocals.size()) {
               Object var9 = this.imagesArrLocals.get(var2);
               int var10 = (int)((float)AndroidUtilities.getPhotoSize() / AndroidUtilities.density);
               var7 = this.currentThumb;
               if (var7 == null || var1 != this.centerImage) {
                  var7 = null;
               }

               ImageReceiver.BitmapHolder var11 = var7;
               if (var7 == null) {
                  var11 = this.placeProvider.getThumbForPhoto((MessageObject)null, (TLRPC.FileLocation)null, var2);
               }

               var3 = var9 instanceof MediaController.PhotoEntry;
               String var26 = "d";
               Object var12;
               Object var13;
               String var14;
               byte var15;
               Object var18;
               if (var3) {
                  MediaController.PhotoEntry var31 = (MediaController.PhotoEntry)var9;
                  var3 = var31.isVideo;
                  String var32;
                  if (!var3) {
                     var26 = var31.imagePath;
                     if (var26 == null) {
                        var1.setOrientation(var31.orientation, false);
                        var26 = var31.path;
                     }

                     var32 = String.format(Locale.US, "%d_%d", var10, var10);
                  } else {
                     var26 = var31.thumbPath;
                     if (var26 == null) {
                        StringBuilder var46 = new StringBuilder();
                        var46.append("vthumb://");
                        var46.append(var31.imageId);
                        var46.append(":");
                        var46.append(var31.path);
                        var26 = var46.toString();
                     }

                     var32 = null;
                  }

                  String var40 = var32;
                  var24 = null;
                  var13 = var24;
                  var14 = var26;
                  var2 = 0;
                  var15 = 0;
                  var18 = var24;
                  var17 = var24;
                  var29 = var40;
                  var12 = var14;
               } else if (var9 instanceof TLRPC.BotInlineResult) {
                  label318: {
                     label317: {
                        label316: {
                           label369: {
                              TLRPC.BotInlineResult var19 = (TLRPC.BotInlineResult)var9;
                              if (!var19.type.equals("video") && !MessageObject.isVideoDocument(var19.document)) {
                                 if (var19.type.equals("gif")) {
                                    var17 = var19.document;
                                    if (var17 != null) {
                                       var2 = ((TLRPC.Document)var17).size;
                                       var18 = null;
                                       var12 = "d";
                                       break label316;
                                    }
                                 }

                                 TLRPC.Photo var28 = var19.photo;
                                 if (var28 != null) {
                                    var24 = FileLoader.getClosestPhotoSizeWithSize(var28.sizes, AndroidUtilities.getPhotoSize());
                                    var29 = var19.photo;
                                    var2 = var24.size;
                                    var12 = String.format(Locale.US, "%d_%d", var10, var10);
                                    var18 = null;
                                    var17 = var18;
                                    break label318;
                                 }

                                 if (var19.content instanceof TLRPC.TL_webDocument) {
                                    if (!var19.type.equals("gif")) {
                                       var26 = String.format(Locale.US, "%d_%d", var10, var10);
                                    }

                                    var18 = WebFile.createWithWebDocument(var19.content);
                                    var17 = null;
                                    var2 = 0;
                                    var12 = var26;
                                    break label316;
                                 }
                              } else {
                                 TLRPC.Document var35 = var19.document;
                                 if (var35 != null) {
                                    var24 = FileLoader.getClosestPhotoSizeWithSize(var35.thumbs, 90);
                                    var29 = var19.document;
                                    var18 = null;
                                    var17 = var18;
                                    var12 = var18;
                                    break label317;
                                 }

                                 TLRPC.WebDocument var36 = var19.thumb;
                                 if (var36 instanceof TLRPC.TL_webDocument) {
                                    var18 = WebFile.createWithWebDocument(var36);
                                    break label369;
                                 }
                              }

                              var18 = null;
                           }

                           var24 = null;
                           var17 = null;
                           var29 = var17;
                           var12 = var17;
                           break label317;
                        }

                        var29 = null;
                        var24 = null;
                        break label318;
                     }

                     var2 = 0;
                  }

                  var3 = false;
                  var15 = 1;
                  var14 = null;
                  var13 = var29;
                  var29 = var12;
                  var12 = var14;
               } else if (!(var9 instanceof MediaController.SearchImage)) {
                  var24 = null;
                  var18 = var24;
                  var17 = var24;
                  var13 = var24;
                  var29 = var24;
                  var12 = var24;
                  var3 = false;
                  var2 = 0;
                  var15 = 0;
               } else {
                  var5 = (MediaController.SearchImage)var9;
                  var24 = var5.photoSize;
                  if (var24 != null) {
                     var13 = var5.photo;
                     var2 = var24.size;
                     var29 = null;
                     var17 = var29;
                  } else {
                     label324: {
                        var29 = var5.imagePath;
                        if (var29 != null) {
                           var2 = 0;
                        } else {
                           var17 = var5.document;
                           if (var17 != null) {
                              var2 = ((TLRPC.Document)var17).size;
                              var24 = null;
                              var13 = var24;
                              var29 = var24;
                              break label324;
                           }

                           var29 = var5.imageUrl;
                           var2 = var5.size;
                        }

                        var13 = null;
                        var24 = null;
                        var17 = null;
                     }
                  }

                  var3 = false;
                  var15 = 1;
                  var12 = var29;
                  var29 = "d";
                  var18 = null;
               }

               if (var17 != null) {
                  TLRPC.PhotoSize var49 = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Document)var17).thumbs, 90);
                  var25 = ImageLocation.getForDocument((TLRPC.Document)var17);
                  ImageLocation var50;
                  if (var11 == null) {
                     var50 = ImageLocation.getForDocument(var49, (TLRPC.Document)var17);
                  } else {
                     var50 = null;
                  }

                  String var23 = String.format(Locale.US, "%d_%d", var10, var10);
                  if (var11 != null) {
                     var38 = new BitmapDrawable(var11.bitmap);
                  } else {
                     var38 = null;
                  }

                  var1.setImage(var25, "d", var50, var23, var38, var2, (String)null, var9, var15);
               } else if (var24 != null) {
                  var25 = ImageLocation.getForObject(var24, (TLObject)var13);
                  if (var11 != null) {
                     var38 = new BitmapDrawable(var11.bitmap);
                  } else {
                     var38 = null;
                  }

                  var1.setImage(var25, (String)var29, var38, var2, (String)null, var9, var15);
               } else {
                  Activity var44;
                  Object var45;
                  if (var18 != null) {
                     var25 = ImageLocation.getForWebFile((WebFile)var18);
                     if (var11 != null) {
                        var45 = new BitmapDrawable(var11.bitmap);
                     } else {
                        label361: {
                           if (var3) {
                              var44 = this.parentActivity;
                              if (var44 != null) {
                                 var45 = var44.getResources().getDrawable(2131165697);
                                 break label361;
                              }
                           }

                           var45 = null;
                        }
                     }

                     var1.setImage(var25, (String)var29, (Drawable)var45, (String)null, var9, var15);
                  } else {
                     if (var11 != null) {
                        var45 = new BitmapDrawable(var11.bitmap);
                     } else {
                        label362: {
                           if (var3) {
                              var44 = this.parentActivity;
                              if (var44 != null) {
                                 var45 = var44.getResources().getDrawable(2131165697);
                                 break label362;
                              }
                           }

                           var45 = null;
                        }
                     }

                     var1.setImage((String)var12, (String)var29, (Drawable)var45, (String)null, var2);
                  }
               }
            } else {
               var1.setImageBitmap((Bitmap)null);
            }
         } else {
            if (!this.imagesArr.isEmpty() && var2 >= 0 && var2 < this.imagesArr.size()) {
               var29 = (MessageObject)this.imagesArr.get(var2);
               var1.setShouldGenerateQualityThumb(true);
            } else {
               var29 = null;
            }

            if (var29 != null) {
               TLRPC.PhotoSize var33;
               if (((MessageObject)var29).isVideo()) {
                  var1.setNeedsQualityThumb(true);
                  ArrayList var53 = ((MessageObject)var29).photoThumbs;
                  if (var53 != null && !var53.isEmpty()) {
                     var8 = this.currentThumb;
                     if (var8 == null || var1 != this.centerImage) {
                        var8 = null;
                     }

                     var33 = FileLoader.getClosestPhotoSizeWithSize(((MessageObject)var29).photoThumbs, 100);
                     if (var8 == null) {
                        var25 = ImageLocation.getForObject(var33, ((MessageObject)var29).photoThumbsObject);
                     } else {
                        var25 = null;
                     }

                     if (var8 != null) {
                        var38 = new BitmapDrawable(var8.bitmap);
                     } else {
                        var38 = null;
                     }

                     var1.setImage((ImageLocation)null, (String)null, var25, "b", var38, 0, (String)null, var29, 1);
                  } else {
                     var1.setImageBitmap(this.parentActivity.getResources().getDrawable(2131165761));
                  }

                  return;
               }

               AnimatedFileDrawable var51 = this.currentAnimation;
               if (var51 != null) {
                  var1.setImageBitmap((Drawable)var51);
                  this.currentAnimation.setSecondParentView(this.containerView);
                  return;
               }

               if (this.sharedMediaType == 1) {
                  if (((MessageObject)var29).canPreviewDocument()) {
                     TLRPC.Document var47 = ((MessageObject)var29).getDocument();
                     var1.setNeedsQualityThumb(true);
                     var8 = this.currentThumb;
                     if (var8 == null || var1 != this.centerImage) {
                        var8 = null;
                     }

                     if (var29 != null) {
                        var33 = FileLoader.getClosestPhotoSizeWithSize(((MessageObject)var29).photoThumbs, 100);
                     } else {
                        var33 = null;
                     }

                     var2 = (int)(4096.0F / AndroidUtilities.density);
                     ImageLocation var42 = ImageLocation.getForDocument(var47);
                     String var37 = String.format(Locale.US, "%d_%d", var2, var2);
                     if (var8 == null) {
                        var25 = ImageLocation.getForDocument(var33, var47);
                     } else {
                        var25 = null;
                     }

                     if (var8 != null) {
                        var4 = new BitmapDrawable(var8.bitmap);
                     }

                     var1.setImage(var42, var37, var25, "b", var4, var47.size, (String)null, var29, 0);
                  } else {
                     var1.setImageBitmap((Drawable)(new OtherDocumentPlaceholderDrawable(this.parentActivity, this.containerView, (MessageObject)var29)));
                  }

                  return;
               }
            }

            int[] var41 = new int[1];
            ImageLocation var34 = this.getImageLocation(var2, var41);
            TLObject var39 = this.getFileLocation(var2, var41);
            if (var34 != null) {
               var1.setNeedsQualityThumb(true);
               ImageReceiver.BitmapHolder var27 = this.currentThumb;
               if (var27 == null || var1 != this.centerImage) {
                  var27 = null;
               }

               if (var41[0] == 0) {
                  var41[0] = -1;
               }

               if (var29 != null) {
                  var24 = FileLoader.getClosestPhotoSizeWithSize(((MessageObject)var29).photoThumbs, 100);
                  var17 = ((MessageObject)var29).photoThumbsObject;
               } else {
                  var24 = null;
                  var17 = var24;
               }

               TLRPC.PhotoSize var43 = var24;
               if (var24 != null) {
                  var43 = var24;
                  if (var24 == var39) {
                     var43 = null;
                  }
               }

               byte var20;
               if ((var29 == null || !((MessageObject)var29).isWebpage()) && this.avatarsDialogId == 0 && !this.isEvent) {
                  var20 = 0;
               } else {
                  var20 = 1;
               }

               if (var29 == null) {
                  int var48 = this.avatarsDialogId;
                  if (var48 != 0) {
                     if (var48 > 0) {
                        var29 = MessagesController.getInstance(this.currentAccount).getUser(this.avatarsDialogId);
                     } else {
                        var29 = MessagesController.getInstance(this.currentAccount).getChat(-this.avatarsDialogId);
                     }
                  } else {
                     var29 = null;
                  }
               }

               ImageLocation var52;
               if (var27 == null) {
                  var52 = ImageLocation.getForObject(var43, (TLObject)var17);
               } else {
                  var52 = null;
               }

               BitmapDrawable var30;
               if (var27 != null) {
                  var30 = new BitmapDrawable(var27.bitmap);
               } else {
                  var30 = null;
               }

               var1.setImage(var34, (String)null, var52, "b", var30, var41[0], (String)null, var29, var20);
            } else {
               var1.setNeedsQualityThumb(true);
               if (var41[0] == 0) {
                  var1.setImageBitmap((Bitmap)null);
               } else {
                  var1.setImageBitmap(this.parentActivity.getResources().getDrawable(2131165761));
               }
            }
         }
      }

   }

   private void setIsAboutToSwitchToIndex(int var1, boolean var2) {
      if (var2 || this.switchingToIndex != var1) {
         this.switchingToIndex = var1;
         String var3 = this.getFileName(var1);
         boolean var4 = this.imagesArr.isEmpty();
         Object var5 = null;
         byte var6 = 8;
         MessageObject var7;
         boolean var8;
         int var13;
         boolean var18;
         byte var19;
         Object var23;
         String var25;
         TLRPC.Chat var30;
         ImageView var36;
         if (!var4) {
            var1 = this.switchingToIndex;
            if (var1 < 0 || var1 >= this.imagesArr.size()) {
               return;
            }

            var7 = (MessageObject)this.imagesArr.get(this.switchingToIndex);
            var4 = var7.isVideo();
            var8 = var7.isInvoice();
            long var11;
            if (var8) {
               this.masksItem.setVisibility(8);
               this.menuItem.hideSubItem(6);
               this.menuItem.hideSubItem(11);
               var23 = var7.messageOwner.media.description;
               this.allowShare = false;
               this.bottomLayout.setTranslationY((float)AndroidUtilities.dp(48.0F));
               this.captionTextView.setTranslationY((float)AndroidUtilities.dp(48.0F));
            } else {
               ActionBarMenuItem var32 = this.masksItem;
               if (var7.hasPhotoStickers() && (int)var7.getDialogId() != 0) {
                  var19 = 0;
               } else {
                  var19 = 8;
               }

               var32.setVisibility(var19);
               if (var7.canDeleteMessage((TLRPC.Chat)null) && this.slideshowMessageId == 0) {
                  this.menuItem.showSubItem(6);
               } else {
                  this.menuItem.hideSubItem(6);
               }

               if (var4) {
                  this.menuItem.showSubItem(11);
                  if (this.pipItem.getVisibility() != 0) {
                     this.pipItem.setVisibility(0);
                  }

                  if (!this.pipAvailable) {
                     this.pipItem.setEnabled(false);
                     this.pipItem.setAlpha(0.5F);
                  }
               } else {
                  this.menuItem.hideSubItem(11);
                  if (this.pipItem.getVisibility() != 8) {
                     this.pipItem.setVisibility(8);
                  }
               }

               var25 = this.nameOverride;
               if (var25 != null) {
                  this.nameTextView.setText(var25);
               } else if (var7.isFromUser()) {
                  TLRPC.User var34 = MessagesController.getInstance(this.currentAccount).getUser(var7.messageOwner.from_id);
                  if (var34 != null) {
                     this.nameTextView.setText(UserObject.getUserName(var34));
                  } else {
                     this.nameTextView.setText("");
                  }
               } else {
                  var30 = MessagesController.getInstance(this.currentAccount).getChat(var7.messageOwner.to_id.channel_id);
                  TLRPC.Chat var35 = var30;
                  if (ChatObject.isChannel(var30)) {
                     var35 = var30;
                     if (var30.megagroup) {
                        var35 = var30;
                        if (var7.isForwardedChannelPost()) {
                           var35 = MessagesController.getInstance(this.currentAccount).getChat(var7.messageOwner.fwd_from.channel_id);
                        }
                     }
                  }

                  if (var35 != null) {
                     this.nameTextView.setText(var35.title);
                  } else {
                     this.nameTextView.setText("");
                  }
               }

               var1 = this.dateOverride;
               if (var1 == 0) {
                  var1 = var7.messageOwner.date;
               }

               var11 = (long)var1 * 1000L;
               var25 = LocaleController.formatString("formatDateAtTime", 2131561210, LocaleController.getInstance().formatterYear.format(new Date(var11)), LocaleController.getInstance().formatterDay.format(new Date(var11)));
               if (var3 != null && var4) {
                  this.dateTextView.setText(String.format("%s (%s)", var25, AndroidUtilities.formatFileSize((long)var7.getDocument().size)));
               } else {
                  this.dateTextView.setText(var25);
               }

               var23 = var7.caption;
            }

            if (this.currentAnimation != null) {
               this.menuItem.hideSubItem(1);
               this.menuItem.hideSubItem(10);
               if (!var7.canDeleteMessage((TLRPC.Chat)null)) {
                  this.menuItem.setVisibility(8);
               }

               this.allowShare = true;
               this.shareButton.setVisibility(0);
               this.actionBar.setTitle(LocaleController.getString("AttachGif", 2131558716));
            } else {
               if (this.totalImagesCount + this.totalImagesCountMerge != 0 && !this.needSearchImageInArr) {
                  boolean var14;
                  DataQuery var38;
                  if (this.opennedFromMedia) {
                     if (this.imagesArr.size() < this.totalImagesCount + this.totalImagesCountMerge && !this.loadingMoreImages && this.switchingToIndex > this.imagesArr.size() - 5) {
                        ArrayList var37;
                        if (this.imagesArr.isEmpty()) {
                           var1 = 0;
                        } else {
                           var37 = this.imagesArr;
                           var1 = ((MessageObject)var37.get(var37.size() - 1)).getId();
                        }

                        if (this.endReached[0] && this.mergeDialogId != 0L) {
                           label371: {
                              if (!this.imagesArr.isEmpty()) {
                                 var37 = this.imagesArr;
                                 if (((MessageObject)var37.get(var37.size() - 1)).getDialogId() != this.mergeDialogId) {
                                    var18 = true;
                                    var13 = 0;
                                    break label371;
                                 }
                              }

                              var13 = var1;
                              var18 = true;
                           }
                        } else {
                           var14 = false;
                           var13 = var1;
                           var18 = var14;
                        }

                        var38 = DataQuery.getInstance(this.currentAccount);
                        if (!var18) {
                           var11 = this.currentDialogId;
                        } else {
                           var11 = this.mergeDialogId;
                        }

                        var38.loadMedia(var11, 80, var13, this.sharedMediaType, 1, this.classGuid);
                        this.loadingMoreImages = true;
                     }

                     this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.switchingToIndex + 1, this.totalImagesCount + this.totalImagesCountMerge));
                  } else {
                     if (this.imagesArr.size() < this.totalImagesCount + this.totalImagesCountMerge && !this.loadingMoreImages && this.switchingToIndex < 5) {
                        if (this.imagesArr.isEmpty()) {
                           var1 = 0;
                        } else {
                           var1 = ((MessageObject)this.imagesArr.get(0)).getId();
                        }

                        if (this.endReached[0] && this.mergeDialogId != 0L) {
                           if (!this.imagesArr.isEmpty() && ((MessageObject)this.imagesArr.get(0)).getDialogId() != this.mergeDialogId) {
                              var18 = true;
                              var13 = 0;
                           } else {
                              var13 = var1;
                              var18 = true;
                           }
                        } else {
                           var14 = false;
                           var13 = var1;
                           var18 = var14;
                        }

                        var38 = DataQuery.getInstance(this.currentAccount);
                        if (!var18) {
                           var11 = this.currentDialogId;
                        } else {
                           var11 = this.mergeDialogId;
                        }

                        var38.loadMedia(var11, 80, var13, this.sharedMediaType, 1, this.classGuid);
                        this.loadingMoreImages = true;
                     }

                     this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.totalImagesCount + this.totalImagesCountMerge - this.imagesArr.size() + this.switchingToIndex + 1, this.totalImagesCount + this.totalImagesCountMerge));
                  }
               } else if (this.slideshowMessageId == 0 && var7.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) {
                  if (var7.canPreviewDocument()) {
                     this.actionBar.setTitle(LocaleController.getString("AttachDocument", 2131558714));
                  } else if (var7.isVideo()) {
                     this.actionBar.setTitle(LocaleController.getString("AttachVideo", 2131558733));
                  } else {
                     this.actionBar.setTitle(LocaleController.getString("AttachPhoto", 2131558727));
                  }
               } else if (var8) {
                  this.actionBar.setTitle(var7.messageOwner.media.title);
               } else if (var7.isVideo()) {
                  this.actionBar.setTitle(LocaleController.getString("AttachVideo", 2131558733));
               } else if (var7.getDocument() != null) {
                  this.actionBar.setTitle(LocaleController.getString("AttachDocument", 2131558714));
               }

               if ((int)this.currentDialogId == 0) {
                  this.sendItem.setVisibility(8);
               }

               var1 = var7.messageOwner.ttl;
               if (var1 != 0 && var1 < 3600) {
                  this.allowShare = false;
                  this.menuItem.hideSubItem(1);
                  this.shareButton.setVisibility(8);
                  this.menuItem.hideSubItem(10);
               } else {
                  this.allowShare = true;
                  this.menuItem.showSubItem(1);
                  var36 = this.shareButton;
                  if (this.videoPlayerControlFrameLayout.getVisibility() != 0) {
                     var6 = 0;
                  }

                  var36.setVisibility(var6);
                  if (this.shareButton.getVisibility() == 0) {
                     this.menuItem.hideSubItem(10);
                  } else {
                     this.menuItem.showSubItem(10);
                  }
               }
            }

            this.groupedPhotosListView.fillList();
         } else {
            label463: {
               if (!this.secureDocuments.isEmpty()) {
                  this.allowShare = false;
                  this.menuItem.hideSubItem(1);
                  this.nameTextView.setText("");
                  this.dateTextView.setText("");
                  this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.switchingToIndex + 1, this.secureDocuments.size()));
               } else {
                  ImageView var9;
                  if (!this.imagesArrLocations.isEmpty()) {
                     if (var1 < 0 || var1 >= this.imagesArrLocations.size()) {
                        return;
                     }

                     this.nameTextView.setText("");
                     this.dateTextView.setText("");
                     if (this.avatarsDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId() && !this.avatarsArr.isEmpty()) {
                        this.menuItem.showSubItem(6);
                     } else {
                        this.menuItem.hideSubItem(6);
                     }

                     if (this.isEvent) {
                        this.actionBar.setTitle(LocaleController.getString("AttachPhoto", 2131558727));
                     } else {
                        this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.switchingToIndex + 1, this.imagesArrLocations.size()));
                     }

                     this.menuItem.showSubItem(1);
                     this.allowShare = true;
                     var9 = this.shareButton;
                     if (this.videoPlayerControlFrameLayout.getVisibility() != 0) {
                        var6 = 0;
                     }

                     var9.setVisibility(var6);
                     if (this.shareButton.getVisibility() == 0) {
                        this.menuItem.hideSubItem(10);
                     } else {
                        this.menuItem.showSubItem(10);
                     }

                     this.groupedPhotosListView.fillList();
                  } else if (!this.imagesArrLocals.isEmpty()) {
                     if (var1 < 0 || var1 >= this.imagesArrLocals.size()) {
                        return;
                     }

                     boolean var15;
                     boolean var17;
                     Object var21;
                     label331: {
                        var21 = this.imagesArrLocals.get(var1);
                        if (var21 instanceof TLRPC.BotInlineResult) {
                           TLRPC.BotInlineResult var24 = (TLRPC.BotInlineResult)var21;
                           this.currentBotInlineResult = var24;
                           TLRPC.Document var10 = var24.document;
                           if (var10 != null) {
                              var4 = MessageObject.isVideoDocument(var10);
                           } else if (var24.content instanceof TLRPC.TL_webDocument) {
                              var4 = var24.type.equals("video");
                           } else {
                              var4 = false;
                           }

                           var15 = var4;
                        } else {
                           var15 = var21 instanceof MediaController.PhotoEntry;
                           MediaController.SearchImage var26;
                           MediaController.PhotoEntry var27;
                           if (var15) {
                              var27 = (MediaController.PhotoEntry)var21;
                              var25 = var27.path;
                              var4 = var27.isVideo;
                              var18 = false;
                           } else {
                              label464: {
                                 if (var21 instanceof MediaController.SearchImage) {
                                    var26 = (MediaController.SearchImage)var21;
                                    var25 = var26.getPathToAttach();
                                    if (var26.type == 1) {
                                       var4 = false;
                                       var18 = true;
                                       break label464;
                                    }
                                 } else {
                                    var25 = null;
                                 }

                                 var4 = false;
                                 var18 = false;
                              }
                           }

                           if (var4) {
                              label311: {
                                 this.muteItem.setVisibility(0);
                                 this.compressItem.setVisibility(0);
                                 this.isCurrentVideo = true;
                                 this.updateAccessibilityOverlayVisibility();
                                 if (var15) {
                                    VideoEditedInfo var28 = ((MediaController.PhotoEntry)var21).editedInfo;
                                    if (var28 != null && var28.muted) {
                                       var8 = true;
                                       break label311;
                                    }
                                 }

                                 var8 = false;
                              }

                              this.processOpenVideo(var25, var8);
                              this.videoTimelineView.setVisibility(0);
                              this.paintItem.setVisibility(8);
                              this.cropItem.setVisibility(8);
                              this.tuneItem.setVisibility(8);
                              this.rotateItem.setVisibility(8);
                           } else {
                              this.videoTimelineView.setVisibility(8);
                              this.muteItem.setVisibility(8);
                              this.isCurrentVideo = false;
                              this.updateAccessibilityOverlayVisibility();
                              this.compressItem.setVisibility(8);
                              if (var18) {
                                 this.paintItem.setVisibility(8);
                                 this.cropItem.setVisibility(8);
                                 this.rotateItem.setVisibility(8);
                                 this.tuneItem.setVisibility(8);
                              } else {
                                 var1 = this.sendPhotoType;
                                 if (var1 != 4 && var1 != 5) {
                                    this.paintItem.setVisibility(0);
                                    this.tuneItem.setVisibility(0);
                                 } else {
                                    this.paintItem.setVisibility(8);
                                    this.tuneItem.setVisibility(8);
                                 }

                                 var9 = this.cropItem;
                                 if (this.sendPhotoType != 1) {
                                    var19 = 0;
                                 } else {
                                    var19 = 8;
                                 }

                                 var9.setVisibility(var19);
                                 var9 = this.rotateItem;
                                 if (this.sendPhotoType != 1) {
                                    var19 = 8;
                                 } else {
                                    var19 = 0;
                                 }

                                 var9.setVisibility(var19);
                              }

                              this.actionBar.setSubtitle((CharSequence)null);
                           }

                           if (var15) {
                              var27 = (MediaController.PhotoEntry)var21;
                              if (var27.bucketId == 0 && var27.dateTaken == 0L && this.imagesArrLocals.size() == 1) {
                                 var8 = true;
                              } else {
                                 var8 = false;
                              }

                              this.fromCamera = var8;
                              var23 = var27.caption;
                              var1 = var27.ttl;
                              var15 = var27.isFiltered;
                              var17 = var27.isPainted;
                              var8 = var27.isCropped;
                              break label331;
                           }

                           var15 = var4;
                           if (var21 instanceof MediaController.SearchImage) {
                              var26 = (MediaController.SearchImage)var21;
                              var23 = var26.caption;
                              var1 = var26.ttl;
                              var15 = var26.isFiltered;
                              var17 = var26.isPainted;
                              var8 = var26.isCropped;
                              break label331;
                           }
                        }

                        var23 = null;
                        var8 = false;
                        var1 = 0;
                        boolean var16 = false;
                        var17 = false;
                        var4 = var15;
                        var15 = var16;
                     }

                     if (this.bottomLayout.getVisibility() != 8) {
                        this.bottomLayout.setVisibility(8);
                     }

                     this.bottomLayout.setTag((Object)null);
                     if (this.fromCamera) {
                        if (var4) {
                           this.actionBar.setTitle(LocaleController.getString("AttachVideo", 2131558733));
                        } else {
                           this.actionBar.setTitle(LocaleController.getString("AttachPhoto", 2131558727));
                        }
                     } else {
                        this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.switchingToIndex + 1, this.imagesArrLocals.size()));
                     }

                     ChatActivity var29 = this.parentChatActivity;
                     if (var29 != null) {
                        var30 = var29.getCurrentChat();
                        if (var30 != null) {
                           this.actionBar.setTitle(var30.title);
                        } else {
                           TLRPC.User var31 = this.parentChatActivity.getCurrentUser();
                           if (var31 != null) {
                              this.actionBar.setTitle(ContactsController.formatName(var31.first_name, var31.last_name));
                           }
                        }
                     }

                     var13 = this.sendPhotoType;
                     if (var13 == 0 || var13 == 4 || (var13 == 2 || var13 == 5) && this.imagesArrLocals.size() > 1) {
                        this.checkImageView.setChecked(this.placeProvider.isPhotoChecked(this.switchingToIndex), false);
                     }

                     this.updateCaptionTextForCurrentPhoto(var21);
                     PorterDuffColorFilter var22 = new PorterDuffColorFilter(-12734994, Mode.MULTIPLY);
                     ImageView var20 = this.timeItem;
                     PorterDuffColorFilter var33;
                     if (var1 != 0) {
                        var33 = var22;
                     } else {
                        var33 = null;
                     }

                     var20.setColorFilter(var33);
                     var20 = this.paintItem;
                     if (var17) {
                        var33 = var22;
                     } else {
                        var33 = null;
                     }

                     var20.setColorFilter(var33);
                     var20 = this.cropItem;
                     if (var8) {
                        var33 = var22;
                     } else {
                        var33 = null;
                     }

                     var20.setColorFilter(var33);
                     var36 = this.tuneItem;
                     if (!var15) {
                        var22 = null;
                     }

                     var36.setColorFilter(var22);
                     var7 = (MessageObject)var5;
                     break label463;
                  }
               }

               var23 = null;
               var7 = (MessageObject)var5;
            }
         }

         this.setCurrentCaption(var7, (CharSequence)var23, var2 ^ true);
      }
   }

   private void setPhotoChecked() {
      PhotoViewer.PhotoViewerProvider var1 = this.placeProvider;
      if (var1 != null) {
         if (var1.getSelectedPhotos() != null && this.maxSelectedPhotos > 0 && this.placeProvider.getSelectedPhotos().size() > this.maxSelectedPhotos && !this.placeProvider.isPhotoChecked(this.currentIndex)) {
            return;
         }

         int var2 = this.placeProvider.setPhotoChecked(this.currentIndex, this.getCurrentVideoEditedInfo());
         boolean var3 = this.placeProvider.isPhotoChecked(this.currentIndex);
         this.checkImageView.setChecked(var3, true);
         if (var2 >= 0) {
            int var4 = var2;
            if (this.placeProvider.allowGroupPhotos()) {
               var4 = var2 + 1;
            }

            if (var3) {
               this.selectedPhotosAdapter.notifyItemInserted(var4);
               this.selectedPhotosListView.smoothScrollToPosition(var4);
            } else {
               this.selectedPhotosAdapter.notifyItemRemoved(var4);
            }
         }

         this.updateSelectedCount();
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

   private void showDownloadAlert() {
      AlertDialog.Builder var1 = new AlertDialog.Builder(this.parentActivity);
      var1.setTitle(LocaleController.getString("AppName", 2131558635));
      var1.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
      MessageObject var2 = this.currentMessageObject;
      boolean var3 = false;
      boolean var4 = var3;
      if (var2 != null) {
         var4 = var3;
         if (var2.isVideo()) {
            var4 = var3;
            if (FileLoader.getInstance(this.currentMessageObject.currentAccount).isLoadingFile(this.currentFileNames[0])) {
               var4 = true;
            }
         }
      }

      if (var4) {
         var1.setMessage(LocaleController.getString("PleaseStreamDownload", 2131560460));
      } else {
         var1.setMessage(LocaleController.getString("PleaseDownload", 2131560454));
      }

      this.showAlertDialog(var1);
   }

   private void showHint(boolean var1, boolean var2) {
      if (this.containerView != null && (!var1 || this.hintTextView != null)) {
         if (this.hintTextView == null) {
            this.hintTextView = new TextView(this.containerView.getContext());
            this.hintTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0F), -871296751));
            this.hintTextView.setTextColor(-1);
            this.hintTextView.setTextSize(1, 14.0F);
            this.hintTextView.setPadding(AndroidUtilities.dp(8.0F), AndroidUtilities.dp(7.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(7.0F));
            this.hintTextView.setGravity(16);
            this.hintTextView.setAlpha(0.0F);
            this.containerView.addView(this.hintTextView, LayoutHelper.createFrame(-2, -2.0F, 51, 5.0F, 0.0F, 5.0F, 3.0F));
         }

         if (var1) {
            AnimatorSet var9 = this.hintAnimation;
            if (var9 != null) {
               var9.cancel();
               this.hintAnimation = null;
            }

            AndroidUtilities.cancelRunOnUIThread(this.hintHideRunnable);
            this.hintHideRunnable = null;
            this.hideHint();
            return;
         }

         TextView var4 = this.hintTextView;
         String var3;
         int var5;
         if (var2) {
            var5 = 2131559612;
            var3 = "GroupPhotosHelp";
         } else {
            var5 = 2131560786;
            var3 = "SinglePhotosHelp";
         }

         var4.setText(LocaleController.getString(var3, var5));
         Runnable var6 = this.hintHideRunnable;
         if (var6 != null) {
            AnimatorSet var7 = this.hintAnimation;
            if (var7 == null) {
               AndroidUtilities.cancelRunOnUIThread(var6);
               _$$Lambda$PhotoViewer$YkU8h9C09cYhYx6kik_kbQ5mA8s var8 = new _$$Lambda$PhotoViewer$YkU8h9C09cYhYx6kik_kbQ5mA8s(this);
               this.hintHideRunnable = var8;
               AndroidUtilities.runOnUIThread(var8, 2000L);
               return;
            }

            var7.cancel();
            this.hintAnimation = null;
         } else if (this.hintAnimation != null) {
            return;
         }

         this.hintTextView.setVisibility(0);
         this.hintAnimation = new AnimatorSet();
         this.hintAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.hintTextView, View.ALPHA, new float[]{1.0F})});
         this.hintAnimation.addListener(new AnimatorListenerAdapter() {
            // $FF: synthetic method
            public void lambda$onAnimationEnd$0$PhotoViewer$41() {
               PhotoViewer.this.hideHint();
            }

            public void onAnimationCancel(Animator var1) {
               if (var1.equals(PhotoViewer.this.hintAnimation)) {
                  PhotoViewer.this.hintAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1) {
               if (var1.equals(PhotoViewer.this.hintAnimation)) {
                  PhotoViewer.this.hintAnimation = null;
                  PhotoViewer var3 = PhotoViewer.this;
                  _$$Lambda$PhotoViewer$41$zVPp9kWNm7HNZtSbgwtWUuDr3b8 var2 = new _$$Lambda$PhotoViewer$41$zVPp9kWNm7HNZtSbgwtWUuDr3b8(this);
                  var3.hintHideRunnable = var2;
                  AndroidUtilities.runOnUIThread(var2, 2000L);
               }

            }
         });
         this.hintAnimation.setDuration(300L);
         this.hintAnimation.start();
      }

   }

   private void showQualityView(final boolean var1) {
      if (var1) {
         this.previousCompression = this.selectedCompression;
      }

      AnimatorSet var2 = this.qualityChooseViewAnimation;
      if (var2 != null) {
         var2.cancel();
      }

      this.qualityChooseViewAnimation = new AnimatorSet();
      if (var1) {
         this.qualityChooseView.setTag(1);
         this.qualityChooseViewAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.pickerView, View.TRANSLATION_Y, new float[]{0.0F, (float)AndroidUtilities.dp(152.0F)}), ObjectAnimator.ofFloat(this.pickerViewSendButton, View.TRANSLATION_Y, new float[]{0.0F, (float)AndroidUtilities.dp(152.0F)}), ObjectAnimator.ofFloat(this.bottomLayout, View.TRANSLATION_Y, new float[]{(float)(-AndroidUtilities.dp(48.0F)), (float)AndroidUtilities.dp(104.0F)})});
      } else {
         this.qualityChooseView.setTag((Object)null);
         this.qualityChooseViewAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.qualityChooseView, View.TRANSLATION_Y, new float[]{0.0F, (float)AndroidUtilities.dp(166.0F)}), ObjectAnimator.ofFloat(this.qualityPicker, View.TRANSLATION_Y, new float[]{0.0F, (float)AndroidUtilities.dp(166.0F)}), ObjectAnimator.ofFloat(this.bottomLayout, View.TRANSLATION_Y, new float[]{(float)(-AndroidUtilities.dp(48.0F)), (float)AndroidUtilities.dp(118.0F)})});
      }

      this.qualityChooseViewAnimation.addListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1x) {
            PhotoViewer.this.qualityChooseViewAnimation = null;
         }

         public void onAnimationEnd(Animator var1x) {
            if (var1x.equals(PhotoViewer.this.qualityChooseViewAnimation)) {
               PhotoViewer.this.qualityChooseViewAnimation = new AnimatorSet();
               if (var1) {
                  PhotoViewer.this.qualityChooseView.setVisibility(0);
                  PhotoViewer.this.qualityPicker.setVisibility(0);
                  PhotoViewer.this.qualityChooseViewAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(PhotoViewer.this.qualityChooseView, View.TRANSLATION_Y, new float[]{0.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.qualityPicker, View.TRANSLATION_Y, new float[]{0.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.bottomLayout, View.TRANSLATION_Y, new float[]{(float)(-AndroidUtilities.dp(48.0F))})});
               } else {
                  PhotoViewer.this.qualityChooseView.setVisibility(4);
                  PhotoViewer.this.qualityPicker.setVisibility(4);
                  PhotoViewer.this.qualityChooseViewAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(PhotoViewer.this.pickerView, View.TRANSLATION_Y, new float[]{0.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.pickerViewSendButton, View.TRANSLATION_Y, new float[]{0.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.bottomLayout, View.TRANSLATION_Y, new float[]{(float)(-AndroidUtilities.dp(48.0F))})});
               }

               PhotoViewer.this.qualityChooseViewAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1x) {
                     if (var1x.equals(PhotoViewer.this.qualityChooseViewAnimation)) {
                        PhotoViewer.this.qualityChooseViewAnimation = null;
                     }

                  }
               });
               PhotoViewer.this.qualityChooseViewAnimation.setDuration(200L);
               PhotoViewer.this.qualityChooseViewAnimation.setInterpolator(new AccelerateInterpolator());
               PhotoViewer.this.qualityChooseViewAnimation.start();
            }
         }
      });
      this.qualityChooseViewAnimation.setDuration(200L);
      this.qualityChooseViewAnimation.setInterpolator(new DecelerateInterpolator());
      this.qualityChooseViewAnimation.start();
   }

   private void switchToEditMode(final int var1) {
      if (this.currentEditMode != var1 && this.centerImage.getBitmap() != null && this.changeModeAnimation == null && this.imageMoveAnimation == null && this.photoProgressViews[0].backgroundState == -1 && this.captionEditText.getTag() == null) {
         int var3;
         ArrayList var9;
         if (var1 == 0) {
            if (this.centerImage.getBitmap() != null) {
               int var2 = this.centerImage.getBitmapWidth();
               var3 = this.centerImage.getBitmapHeight();
               float var4 = (float)this.getContainerViewWidth();
               float var5 = (float)var2;
               var4 /= var5;
               float var6 = (float)this.getContainerViewHeight();
               float var7 = (float)var3;
               float var8 = var6 / var7;
               var6 = (float)this.getContainerViewWidth(0) / var5;
               var7 = (float)this.getContainerViewHeight(0) / var7;
               var5 = var4;
               if (var4 > var8) {
                  var5 = var8;
               }

               if (var6 > var7) {
                  var4 = var7;
               } else {
                  var4 = var6;
               }

               if (this.sendPhotoType == 1) {
                  this.setCropTranslations(true);
               } else {
                  this.animateToScale = var4 / var5;
                  this.animateToX = 0.0F;
                  this.translationX = (float)(this.getLeftInset() / 2 - this.getRightInset() / 2);
                  var3 = this.currentEditMode;
                  if (var3 == 1) {
                     this.animateToY = (float)AndroidUtilities.dp(58.0F);
                  } else if (var3 == 2) {
                     this.animateToY = (float)AndroidUtilities.dp(92.0F);
                  } else if (var3 == 3) {
                     this.animateToY = (float)AndroidUtilities.dp(44.0F);
                  }

                  if (VERSION.SDK_INT >= 21) {
                     this.animateToY -= (float)(AndroidUtilities.statusBarHeight / 2);
                  }

                  this.animationStartTime = System.currentTimeMillis();
                  this.zoomAnimation = true;
               }
            }

            this.padImageForHorizontalInsets = false;
            this.imageMoveAnimation = new AnimatorSet();
            var9 = new ArrayList(4);
            var3 = this.currentEditMode;
            if (var3 == 1) {
               var9.add(ObjectAnimator.ofFloat(this.editorDoneLayout, View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(48.0F)}));
               var9.add(ObjectAnimator.ofFloat(this, AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[]{0.0F, 1.0F}));
               var9.add(ObjectAnimator.ofFloat(this.photoCropView, View.ALPHA, new float[]{0.0F}));
            } else if (var3 == 2) {
               this.photoFilterView.shutdown();
               var9.add(ObjectAnimator.ofFloat(this.photoFilterView.getToolsView(), View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(186.0F)}));
               var9.add(ObjectAnimator.ofFloat(this, AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[]{0.0F, 1.0F}));
            } else if (var3 == 3) {
               this.photoPaintView.shutdown();
               var9.add(ObjectAnimator.ofFloat(this.photoPaintView.getToolsView(), View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(126.0F)}));
               var9.add(ObjectAnimator.ofFloat(this.photoPaintView.getColorPicker(), View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(126.0F)}));
               var9.add(ObjectAnimator.ofFloat(this, AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[]{0.0F, 1.0F}));
            }

            this.imageMoveAnimation.playTogether(var9);
            this.imageMoveAnimation.setDuration(200L);
            this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1x) {
                  if (PhotoViewer.this.currentEditMode == 1) {
                     PhotoViewer.this.editorDoneLayout.setVisibility(8);
                     PhotoViewer.this.photoCropView.setVisibility(8);
                  } else if (PhotoViewer.this.currentEditMode == 2) {
                     PhotoViewer.this.containerView.removeView(PhotoViewer.this.photoFilterView);
                     PhotoViewer.this.photoFilterView = null;
                  } else if (PhotoViewer.this.currentEditMode == 3) {
                     PhotoViewer.this.containerView.removeView(PhotoViewer.this.photoPaintView);
                     PhotoViewer.this.photoPaintView = null;
                  }

                  PhotoViewer.this.imageMoveAnimation = null;
                  PhotoViewer.this.currentEditMode = var1;
                  PhotoViewer.this.applying = false;
                  if (PhotoViewer.this.sendPhotoType != 1) {
                     PhotoViewer.this.animateToScale = 1.0F;
                     PhotoViewer.this.animateToX = 0.0F;
                     PhotoViewer.this.animateToY = 0.0F;
                     PhotoViewer.this.scale = 1.0F;
                  }

                  PhotoViewer var3 = PhotoViewer.this;
                  var3.updateMinMax(var3.scale);
                  PhotoViewer.this.containerView.invalidate();
                  AnimatorSet var4 = new AnimatorSet();
                  ArrayList var2 = new ArrayList();
                  var2.add(ObjectAnimator.ofFloat(PhotoViewer.this.pickerView, View.TRANSLATION_Y, new float[]{0.0F}));
                  var2.add(ObjectAnimator.ofFloat(PhotoViewer.this.pickerViewSendButton, View.TRANSLATION_Y, new float[]{0.0F}));
                  if (PhotoViewer.this.sendPhotoType != 1) {
                     var2.add(ObjectAnimator.ofFloat(PhotoViewer.this.actionBar, View.TRANSLATION_Y, new float[]{0.0F}));
                  }

                  if (PhotoViewer.this.needCaptionLayout) {
                     var2.add(ObjectAnimator.ofFloat(PhotoViewer.this.captionTextView, View.TRANSLATION_Y, new float[]{0.0F}));
                  }

                  if (PhotoViewer.this.sendPhotoType != 0 && PhotoViewer.this.sendPhotoType != 4) {
                     if (PhotoViewer.this.sendPhotoType == 1) {
                        var2.add(ObjectAnimator.ofFloat(PhotoViewer.this.photoCropView, View.ALPHA, new float[]{1.0F}));
                     }
                  } else {
                     var2.add(ObjectAnimator.ofFloat(PhotoViewer.this.checkImageView, View.ALPHA, new float[]{1.0F}));
                     var2.add(ObjectAnimator.ofFloat(PhotoViewer.this.photosCounterView, View.ALPHA, new float[]{1.0F}));
                  }

                  if (PhotoViewer.this.cameraItem.getTag() != null) {
                     PhotoViewer.this.cameraItem.setVisibility(0);
                     var2.add(ObjectAnimator.ofFloat(PhotoViewer.this.cameraItem, View.ALPHA, new float[]{1.0F}));
                  }

                  var4.playTogether(var2);
                  var4.setDuration(200L);
                  var4.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationStart(Animator var1x) {
                        PhotoViewer.this.pickerView.setVisibility(0);
                        PhotoViewer.this.pickerViewSendButton.setVisibility(0);
                        PhotoViewer.this.actionBar.setVisibility(0);
                        if (PhotoViewer.this.needCaptionLayout) {
                           TextView var3 = PhotoViewer.this.captionTextView;
                           byte var2;
                           if (PhotoViewer.this.captionTextView.getTag() != null) {
                              var2 = 0;
                           } else {
                              var2 = 4;
                           }

                           var3.setVisibility(var2);
                        }

                        if (PhotoViewer.this.sendPhotoType == 0 || PhotoViewer.this.sendPhotoType == 4 || (PhotoViewer.this.sendPhotoType == 2 || PhotoViewer.this.sendPhotoType == 5) && PhotoViewer.this.imagesArrLocals.size() > 1) {
                           PhotoViewer.this.checkImageView.setVisibility(0);
                           PhotoViewer.this.photosCounterView.setVisibility(0);
                        } else if (PhotoViewer.this.sendPhotoType == 1) {
                           PhotoViewer.this.setCropTranslations(false);
                        }

                     }
                  });
                  var4.start();
               }
            });
            this.imageMoveAnimation.start();
         } else {
            ActionBar var10;
            if (var1 == 1) {
               this.createCropView();
               this.photoCropView.onAppear();
               this.editorDoneLayout.doneButton.setText(LocaleController.getString("Crop", 2131559175));
               this.editorDoneLayout.doneButton.setTextColor(-11420173);
               this.changeModeAnimation = new AnimatorSet();
               var9 = new ArrayList();
               var9.add(ObjectAnimator.ofFloat(this.pickerView, View.TRANSLATION_Y, new float[]{0.0F, (float)AndroidUtilities.dp(96.0F)}));
               var9.add(ObjectAnimator.ofFloat(this.pickerViewSendButton, View.TRANSLATION_Y, new float[]{0.0F, (float)AndroidUtilities.dp(96.0F)}));
               var10 = this.actionBar;
               var9.add(ObjectAnimator.ofFloat(var10, View.TRANSLATION_Y, new float[]{0.0F, (float)(-var10.getHeight())}));
               if (this.needCaptionLayout) {
                  var9.add(ObjectAnimator.ofFloat(this.captionTextView, View.TRANSLATION_Y, new float[]{0.0F, (float)AndroidUtilities.dp(96.0F)}));
               }

               var3 = this.sendPhotoType;
               if (var3 == 0 || var3 == 4) {
                  var9.add(ObjectAnimator.ofFloat(this.checkImageView, View.ALPHA, new float[]{1.0F, 0.0F}));
                  var9.add(ObjectAnimator.ofFloat(this.photosCounterView, View.ALPHA, new float[]{1.0F, 0.0F}));
               }

               if (this.selectedPhotosListView.getVisibility() == 0) {
                  var9.add(ObjectAnimator.ofFloat(this.selectedPhotosListView, View.ALPHA, new float[]{1.0F, 0.0F}));
               }

               if (this.cameraItem.getTag() != null) {
                  var9.add(ObjectAnimator.ofFloat(this.cameraItem, View.ALPHA, new float[]{1.0F, 0.0F}));
               }

               this.changeModeAnimation.playTogether(var9);
               this.changeModeAnimation.setDuration(200L);
               this.changeModeAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1x) {
                     PhotoViewer.this.changeModeAnimation = null;
                     PhotoViewer.this.pickerView.setVisibility(8);
                     PhotoViewer.this.pickerViewSendButton.setVisibility(8);
                     PhotoViewer.this.cameraItem.setVisibility(8);
                     PhotoViewer.this.selectedPhotosListView.setVisibility(8);
                     PhotoViewer.this.selectedPhotosListView.setAlpha(0.0F);
                     PhotoViewer.this.selectedPhotosListView.setTranslationY((float)(-AndroidUtilities.dp(10.0F)));
                     PhotoViewer.this.photosCounterView.setRotationX(0.0F);
                     PhotoViewer.this.selectedPhotosListView.setEnabled(false);
                     PhotoViewer.this.isPhotosListViewVisible = false;
                     if (PhotoViewer.this.needCaptionLayout) {
                        PhotoViewer.this.captionTextView.setVisibility(4);
                     }

                     if (PhotoViewer.this.sendPhotoType == 0 || PhotoViewer.this.sendPhotoType == 4 || (PhotoViewer.this.sendPhotoType == 2 || PhotoViewer.this.sendPhotoType == 5) && PhotoViewer.this.imagesArrLocals.size() > 1) {
                        PhotoViewer.this.checkImageView.setVisibility(8);
                        PhotoViewer.this.photosCounterView.setVisibility(8);
                     }

                     Bitmap var2 = PhotoViewer.this.centerImage.getBitmap();
                     if (var2 != null) {
                        PhotoCropView var13 = PhotoViewer.this.photoCropView;
                        int var3 = PhotoViewer.this.centerImage.getOrientation();
                        boolean var4;
                        if (PhotoViewer.this.sendPhotoType != 1) {
                           var4 = true;
                        } else {
                           var4 = false;
                        }

                        var13.setBitmap(var2, var3, var4, false);
                        PhotoViewer.this.photoCropView.onDisappear();
                        int var5 = PhotoViewer.this.centerImage.getBitmapWidth();
                        var3 = PhotoViewer.this.centerImage.getBitmapHeight();
                        float var6 = (float)PhotoViewer.this.getContainerViewWidth();
                        float var7 = (float)var5;
                        var6 /= var7;
                        float var8 = (float)PhotoViewer.this.getContainerViewHeight();
                        float var9 = (float)var3;
                        float var10 = var8 / var9;
                        float var11 = (float)PhotoViewer.this.getContainerViewWidth(1) / var7;
                        float var12 = (float)PhotoViewer.this.getContainerViewHeight(1) / var9;
                        var8 = var6;
                        if (var6 > var10) {
                           var8 = var10;
                        }

                        var6 = var11;
                        if (var11 > var12) {
                           var6 = var12;
                        }

                        if (PhotoViewer.this.sendPhotoType == 1) {
                           var11 = (float)Math.min(PhotoViewer.this.getContainerViewWidth(1), PhotoViewer.this.getContainerViewHeight(1));
                           var6 = var11 / var7;
                           var11 /= var9;
                           if (var6 <= var11) {
                              var6 = var11;
                           }
                        }

                        PhotoViewer.this.animateToScale = var6 / var8;
                        PhotoViewer var14 = PhotoViewer.this;
                        var14.animateToX = (float)(var14.getLeftInset() / 2 - PhotoViewer.this.getRightInset() / 2);
                        var14 = PhotoViewer.this;
                        var5 = -AndroidUtilities.dp(56.0F);
                        if (VERSION.SDK_INT >= 21) {
                           var3 = AndroidUtilities.statusBarHeight / 2;
                        } else {
                           var3 = 0;
                        }

                        var14.animateToY = (float)(var5 + var3);
                        PhotoViewer.this.animationStartTime = System.currentTimeMillis();
                        PhotoViewer.this.zoomAnimation = true;
                     }

                     PhotoViewer.this.imageMoveAnimation = new AnimatorSet();
                     PhotoViewer.this.imageMoveAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(PhotoViewer.this.editorDoneLayout, View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(48.0F), 0.0F}), ObjectAnimator.ofFloat(PhotoViewer.this, AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.photoCropView, View.ALPHA, new float[]{0.0F, 1.0F})});
                     PhotoViewer.this.imageMoveAnimation.setDuration(200L);
                     PhotoViewer.this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator var1x) {
                           PhotoViewer.this.photoCropView.onAppeared();
                           PhotoViewer.this.imageMoveAnimation = null;
                           <undefinedtype> var2 = <VAR_NAMELESS_ENCLOSURE>;
                           PhotoViewer.this.currentEditMode = var1;
                           PhotoViewer.this.animateToScale = 1.0F;
                           PhotoViewer.this.animateToX = 0.0F;
                           PhotoViewer.this.animateToY = 0.0F;
                           PhotoViewer.this.scale = 1.0F;
                           PhotoViewer var3 = PhotoViewer.this;
                           var3.updateMinMax(var3.scale);
                           PhotoViewer.this.padImageForHorizontalInsets = true;
                           PhotoViewer.this.containerView.invalidate();
                        }

                        public void onAnimationStart(Animator var1x) {
                           PhotoViewer.this.editorDoneLayout.setVisibility(0);
                           PhotoViewer.this.photoCropView.setVisibility(0);
                        }
                     });
                     PhotoViewer.this.imageMoveAnimation.start();
                  }
               });
               this.changeModeAnimation.start();
            } else if (var1 == 2) {
               if (this.photoFilterView == null) {
                  MediaController.SavedFilterState var15;
                  String var19;
                  label148: {
                     String var14;
                     label147: {
                        boolean var11 = this.imagesArrLocals.isEmpty();
                        var15 = null;
                        MediaController.SavedFilterState var13 = null;
                        if (!var11) {
                           Object var12 = this.imagesArrLocals.get(this.currentIndex);
                           if (var12 instanceof MediaController.PhotoEntry) {
                              MediaController.PhotoEntry var21 = (MediaController.PhotoEntry)var12;
                              String var17;
                              if (var21.imagePath == null) {
                                 var17 = var21.path;
                                 var13 = var21.savedFilterState;
                              } else {
                                 var17 = null;
                              }

                              var3 = var21.orientation;
                              var19 = var17;
                              var15 = var13;
                              break label148;
                           }

                           if (var12 instanceof MediaController.SearchImage) {
                              MediaController.SearchImage var16 = (MediaController.SearchImage)var12;
                              var15 = var16.savedFilterState;
                              var14 = var16.imageUrl;
                              break label147;
                           }
                        }

                        var14 = null;
                     }

                     var3 = 0;
                     var19 = var14;
                  }

                  Bitmap var18;
                  if (var15 == null) {
                     var18 = this.centerImage.getBitmap();
                     var3 = this.centerImage.getOrientation();
                  } else {
                     var18 = BitmapFactory.decodeFile(var19);
                  }

                  this.photoFilterView = new PhotoFilterView(this.parentActivity, var18, var3, var15);
                  this.containerView.addView(this.photoFilterView, LayoutHelper.createFrame(-1, -1.0F));
                  this.photoFilterView.getDoneTextView().setOnClickListener(new _$$Lambda$PhotoViewer$_kiq87m6DEbP6Rtuq9Z0xaIwbfE(this));
                  this.photoFilterView.getCancelTextView().setOnClickListener(new _$$Lambda$PhotoViewer$2VWEpko1xopXpDQMDqF1erZanWU(this));
                  this.photoFilterView.getToolsView().setTranslationY((float)AndroidUtilities.dp(186.0F));
               }

               this.changeModeAnimation = new AnimatorSet();
               ArrayList var20 = new ArrayList();
               var20.add(ObjectAnimator.ofFloat(this.pickerView, View.TRANSLATION_Y, new float[]{0.0F, (float)AndroidUtilities.dp(96.0F)}));
               var20.add(ObjectAnimator.ofFloat(this.pickerViewSendButton, View.TRANSLATION_Y, new float[]{0.0F, (float)AndroidUtilities.dp(96.0F)}));
               ActionBar var22 = this.actionBar;
               var20.add(ObjectAnimator.ofFloat(var22, View.TRANSLATION_Y, new float[]{0.0F, (float)(-var22.getHeight())}));
               var3 = this.sendPhotoType;
               if (var3 != 0 && var3 != 4) {
                  if (var3 == 1) {
                     var20.add(ObjectAnimator.ofFloat(this.photoCropView, View.ALPHA, new float[]{1.0F, 0.0F}));
                  }
               } else {
                  var20.add(ObjectAnimator.ofFloat(this.checkImageView, View.ALPHA, new float[]{1.0F, 0.0F}));
                  var20.add(ObjectAnimator.ofFloat(this.photosCounterView, View.ALPHA, new float[]{1.0F, 0.0F}));
               }

               if (this.selectedPhotosListView.getVisibility() == 0) {
                  var20.add(ObjectAnimator.ofFloat(this.selectedPhotosListView, View.ALPHA, new float[]{1.0F, 0.0F}));
               }

               if (this.cameraItem.getTag() != null) {
                  var20.add(ObjectAnimator.ofFloat(this.cameraItem, View.ALPHA, new float[]{1.0F, 0.0F}));
               }

               this.changeModeAnimation.playTogether(var20);
               this.changeModeAnimation.setDuration(200L);
               this.changeModeAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1x) {
                     PhotoViewer.this.changeModeAnimation = null;
                     PhotoViewer.this.pickerView.setVisibility(8);
                     PhotoViewer.this.pickerViewSendButton.setVisibility(8);
                     PhotoViewer.this.actionBar.setVisibility(8);
                     PhotoViewer.this.cameraItem.setVisibility(8);
                     PhotoViewer.this.selectedPhotosListView.setVisibility(8);
                     PhotoViewer.this.selectedPhotosListView.setAlpha(0.0F);
                     PhotoViewer.this.selectedPhotosListView.setTranslationY((float)(-AndroidUtilities.dp(10.0F)));
                     PhotoViewer.this.photosCounterView.setRotationX(0.0F);
                     PhotoViewer.this.selectedPhotosListView.setEnabled(false);
                     PhotoViewer.this.isPhotosListViewVisible = false;
                     if (PhotoViewer.this.needCaptionLayout) {
                        PhotoViewer.this.captionTextView.setVisibility(4);
                     }

                     if (PhotoViewer.this.sendPhotoType == 0 || PhotoViewer.this.sendPhotoType == 4 || (PhotoViewer.this.sendPhotoType == 2 || PhotoViewer.this.sendPhotoType == 5) && PhotoViewer.this.imagesArrLocals.size() > 1) {
                        PhotoViewer.this.checkImageView.setVisibility(8);
                        PhotoViewer.this.photosCounterView.setVisibility(8);
                     }

                     if (PhotoViewer.this.centerImage.getBitmap() != null) {
                        int var2 = PhotoViewer.this.centerImage.getBitmapWidth();
                        int var3 = PhotoViewer.this.centerImage.getBitmapHeight();
                        float var4 = (float)PhotoViewer.this.getContainerViewWidth();
                        float var5 = (float)var2;
                        float var6 = var4 / var5;
                        float var7 = (float)PhotoViewer.this.getContainerViewHeight();
                        var4 = (float)var3;
                        var7 /= var4;
                        var5 = (float)PhotoViewer.this.getContainerViewWidth(2) / var5;
                        float var8 = (float)PhotoViewer.this.getContainerViewHeight(2) / var4;
                        var4 = var6;
                        if (var6 > var7) {
                           var4 = var7;
                        }

                        if (var5 > var8) {
                           var6 = var8;
                        } else {
                           var6 = var5;
                        }

                        PhotoViewer.this.animateToScale = var6 / var4;
                        PhotoViewer var9 = PhotoViewer.this;
                        var9.animateToX = (float)(var9.getLeftInset() / 2 - PhotoViewer.this.getRightInset() / 2);
                        var9 = PhotoViewer.this;
                        var3 = -AndroidUtilities.dp(92.0F);
                        if (VERSION.SDK_INT >= 21) {
                           var2 = AndroidUtilities.statusBarHeight / 2;
                        } else {
                           var2 = 0;
                        }

                        var9.animateToY = (float)(var3 + var2);
                        PhotoViewer.this.animationStartTime = System.currentTimeMillis();
                        PhotoViewer.this.zoomAnimation = true;
                     }

                     PhotoViewer.this.imageMoveAnimation = new AnimatorSet();
                     PhotoViewer.this.imageMoveAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(PhotoViewer.this, AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.photoFilterView.getToolsView(), View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(186.0F), 0.0F})});
                     PhotoViewer.this.imageMoveAnimation.setDuration(200L);
                     PhotoViewer.this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator var1x) {
                           PhotoViewer.this.photoFilterView.init();
                           PhotoViewer.this.imageMoveAnimation = null;
                           <undefinedtype> var2 = <VAR_NAMELESS_ENCLOSURE>;
                           PhotoViewer.this.currentEditMode = var1;
                           PhotoViewer.this.animateToScale = 1.0F;
                           PhotoViewer.this.animateToX = 0.0F;
                           PhotoViewer.this.animateToY = 0.0F;
                           PhotoViewer.this.scale = 1.0F;
                           PhotoViewer var3 = PhotoViewer.this;
                           var3.updateMinMax(var3.scale);
                           PhotoViewer.this.padImageForHorizontalInsets = true;
                           PhotoViewer.this.containerView.invalidate();
                           if (PhotoViewer.this.sendPhotoType == 1) {
                              PhotoViewer.this.photoCropView.reset();
                           }

                        }

                        public void onAnimationStart(Animator var1x) {
                        }
                     });
                     PhotoViewer.this.imageMoveAnimation.start();
                  }
               });
               this.changeModeAnimation.start();
            } else if (var1 == 3) {
               if (this.photoPaintView == null) {
                  this.photoPaintView = new PhotoPaintView(this.parentActivity, this.centerImage.getBitmap(), this.centerImage.getOrientation());
                  this.containerView.addView(this.photoPaintView, LayoutHelper.createFrame(-1, -1.0F));
                  this.photoPaintView.getDoneTextView().setOnClickListener(new _$$Lambda$PhotoViewer$iPzcqyK9klw8fE_zuZB7xfys4J8(this));
                  this.photoPaintView.getCancelTextView().setOnClickListener(new _$$Lambda$PhotoViewer$Ws0W6J_E4CCAikNDkwUaS7ufRkg(this));
                  this.photoPaintView.getColorPicker().setTranslationY((float)AndroidUtilities.dp(126.0F));
                  this.photoPaintView.getToolsView().setTranslationY((float)AndroidUtilities.dp(126.0F));
               }

               this.changeModeAnimation = new AnimatorSet();
               var9 = new ArrayList();
               var9.add(ObjectAnimator.ofFloat(this.pickerView, View.TRANSLATION_Y, new float[]{0.0F, (float)AndroidUtilities.dp(96.0F)}));
               var9.add(ObjectAnimator.ofFloat(this.pickerViewSendButton, View.TRANSLATION_Y, new float[]{0.0F, (float)AndroidUtilities.dp(96.0F)}));
               var10 = this.actionBar;
               var9.add(ObjectAnimator.ofFloat(var10, View.TRANSLATION_Y, new float[]{0.0F, (float)(-var10.getHeight())}));
               if (this.needCaptionLayout) {
                  var9.add(ObjectAnimator.ofFloat(this.captionTextView, View.TRANSLATION_Y, new float[]{0.0F, (float)AndroidUtilities.dp(96.0F)}));
               }

               var3 = this.sendPhotoType;
               if (var3 != 0 && var3 != 4) {
                  if (var3 == 1) {
                     var9.add(ObjectAnimator.ofFloat(this.photoCropView, View.ALPHA, new float[]{1.0F, 0.0F}));
                  }
               } else {
                  var9.add(ObjectAnimator.ofFloat(this.checkImageView, View.ALPHA, new float[]{1.0F, 0.0F}));
                  var9.add(ObjectAnimator.ofFloat(this.photosCounterView, View.ALPHA, new float[]{1.0F, 0.0F}));
               }

               if (this.selectedPhotosListView.getVisibility() == 0) {
                  var9.add(ObjectAnimator.ofFloat(this.selectedPhotosListView, View.ALPHA, new float[]{1.0F, 0.0F}));
               }

               if (this.cameraItem.getTag() != null) {
                  var9.add(ObjectAnimator.ofFloat(this.cameraItem, View.ALPHA, new float[]{1.0F, 0.0F}));
               }

               this.changeModeAnimation.playTogether(var9);
               this.changeModeAnimation.setDuration(200L);
               this.changeModeAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1x) {
                     PhotoViewer.this.changeModeAnimation = null;
                     PhotoViewer.this.pickerView.setVisibility(8);
                     PhotoViewer.this.pickerViewSendButton.setVisibility(8);
                     PhotoViewer.this.cameraItem.setVisibility(8);
                     PhotoViewer.this.selectedPhotosListView.setVisibility(8);
                     PhotoViewer.this.selectedPhotosListView.setAlpha(0.0F);
                     PhotoViewer.this.selectedPhotosListView.setTranslationY((float)(-AndroidUtilities.dp(10.0F)));
                     PhotoViewer.this.photosCounterView.setRotationX(0.0F);
                     PhotoViewer.this.selectedPhotosListView.setEnabled(false);
                     PhotoViewer.this.isPhotosListViewVisible = false;
                     if (PhotoViewer.this.needCaptionLayout) {
                        PhotoViewer.this.captionTextView.setVisibility(4);
                     }

                     if (PhotoViewer.this.sendPhotoType == 0 || PhotoViewer.this.sendPhotoType == 4 || (PhotoViewer.this.sendPhotoType == 2 || PhotoViewer.this.sendPhotoType == 5) && PhotoViewer.this.imagesArrLocals.size() > 1) {
                        PhotoViewer.this.checkImageView.setVisibility(8);
                        PhotoViewer.this.photosCounterView.setVisibility(8);
                     }

                     if (PhotoViewer.this.centerImage.getBitmap() != null) {
                        int var2 = PhotoViewer.this.centerImage.getBitmapWidth();
                        int var3 = PhotoViewer.this.centerImage.getBitmapHeight();
                        float var4 = (float)PhotoViewer.this.getContainerViewWidth();
                        float var5 = (float)var2;
                        var4 /= var5;
                        float var6 = (float)PhotoViewer.this.getContainerViewHeight();
                        float var7 = (float)var3;
                        float var8 = var6 / var7;
                        var6 = (float)PhotoViewer.this.getContainerViewWidth(3) / var5;
                        var7 = (float)PhotoViewer.this.getContainerViewHeight(3) / var7;
                        var5 = var4;
                        if (var4 > var8) {
                           var5 = var8;
                        }

                        if (var6 > var7) {
                           var4 = var7;
                        } else {
                           var4 = var6;
                        }

                        PhotoViewer.this.animateToScale = var4 / var5;
                        PhotoViewer var9 = PhotoViewer.this;
                        var9.animateToX = (float)(var9.getLeftInset() / 2 - PhotoViewer.this.getRightInset() / 2);
                        var9 = PhotoViewer.this;
                        var3 = -AndroidUtilities.dp(44.0F);
                        if (VERSION.SDK_INT >= 21) {
                           var2 = AndroidUtilities.statusBarHeight / 2;
                        } else {
                           var2 = 0;
                        }

                        var9.animateToY = (float)(var3 + var2);
                        PhotoViewer.this.animationStartTime = System.currentTimeMillis();
                        PhotoViewer.this.zoomAnimation = true;
                     }

                     PhotoViewer.this.imageMoveAnimation = new AnimatorSet();
                     PhotoViewer.this.imageMoveAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(PhotoViewer.this, AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.photoPaintView.getColorPicker(), View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(126.0F), 0.0F}), ObjectAnimator.ofFloat(PhotoViewer.this.photoPaintView.getToolsView(), View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(126.0F), 0.0F})});
                     PhotoViewer.this.imageMoveAnimation.setDuration(200L);
                     PhotoViewer.this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator var1x) {
                           PhotoViewer.this.photoPaintView.init();
                           PhotoViewer.this.imageMoveAnimation = null;
                           <undefinedtype> var2 = <VAR_NAMELESS_ENCLOSURE>;
                           PhotoViewer.this.currentEditMode = var1;
                           PhotoViewer.this.animateToScale = 1.0F;
                           PhotoViewer.this.animateToX = 0.0F;
                           PhotoViewer.this.animateToY = 0.0F;
                           PhotoViewer.this.scale = 1.0F;
                           PhotoViewer var3 = PhotoViewer.this;
                           var3.updateMinMax(var3.scale);
                           PhotoViewer.this.padImageForHorizontalInsets = true;
                           PhotoViewer.this.containerView.invalidate();
                           if (PhotoViewer.this.sendPhotoType == 1) {
                              PhotoViewer.this.photoCropView.reset();
                           }

                        }

                        public void onAnimationStart(Animator var1x) {
                        }
                     });
                     PhotoViewer.this.imageMoveAnimation.start();
                  }
               });
               this.changeModeAnimation.start();
            }
         }
      }

   }

   private void switchToPip() {
      if (this.videoPlayer != null && this.textureUploaded && this.checkInlinePermissions() && !this.changingTextureView && !this.switchingInlineMode && !this.isInline) {
         if (PipInstance != null) {
            PipInstance.destroyPhotoViewer();
         }

         this.openedFullScreenVideo = false;
         PipInstance = Instance;
         Instance = null;
         this.switchingInlineMode = true;
         this.isVisible = false;
         PhotoViewer.PlaceProviderObject var1 = this.currentPlaceObject;
         if (var1 != null) {
            var1.imageReceiver.setVisible(true, true);
            AnimatedFileDrawable var7 = this.currentPlaceObject.imageReceiver.getAnimation();
            if (var7 != null) {
               Bitmap var2 = var7.getAnimatedBitmap();
               if (var2 != null) {
                  try {
                     Bitmap var3 = this.videoTextureView.getBitmap(var2.getWidth(), var2.getHeight());
                     Canvas var4 = new Canvas(var2);
                     var4.drawBitmap(var3, 0.0F, 0.0F, (Paint)null);
                     var3.recycle();
                  } catch (Throwable var6) {
                     FileLog.e(var6);
                  }
               }

               var7.seekTo(this.videoPlayer.getCurrentPosition(), true);
               this.currentPlaceObject.imageReceiver.setAllowStartAnimation(true);
               this.currentPlaceObject.imageReceiver.startAnimation();
            }
         }

         if (VERSION.SDK_INT >= 21) {
            this.pipAnimationInProgress = true;
            Rect var8 = PipVideoView.getPipRect(this.aspectRatioFrameLayout.getAspectRatio());
            float var5 = var8.width / (float)this.videoTextureView.getWidth();
            var8.y += (float)AndroidUtilities.statusBarHeight;
            AnimatorSet var9 = new AnimatorSet();
            var9.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.textureImageView, View.SCALE_X, new float[]{var5}), ObjectAnimator.ofFloat(this.textureImageView, View.SCALE_Y, new float[]{var5}), ObjectAnimator.ofFloat(this.textureImageView, View.TRANSLATION_X, new float[]{var8.x}), ObjectAnimator.ofFloat(this.textureImageView, View.TRANSLATION_Y, new float[]{var8.y}), ObjectAnimator.ofFloat(this.videoTextureView, View.SCALE_X, new float[]{var5}), ObjectAnimator.ofFloat(this.videoTextureView, View.SCALE_Y, new float[]{var5}), ObjectAnimator.ofFloat(this.videoTextureView, View.TRANSLATION_X, new float[]{var8.x - this.aspectRatioFrameLayout.getX() + (float)this.getLeftInset()}), ObjectAnimator.ofFloat(this.videoTextureView, View.TRANSLATION_Y, new float[]{var8.y - this.aspectRatioFrameLayout.getY()}), ObjectAnimator.ofInt(this.backgroundDrawable, AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[]{0}), ObjectAnimator.ofFloat(this.actionBar, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.bottomLayout, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.captionTextView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.groupedPhotosListView, View.ALPHA, new float[]{0.0F})});
            var9.setInterpolator(new DecelerateInterpolator());
            var9.setDuration(250L);
            var9.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  PhotoViewer.this.pipAnimationInProgress = false;
                  PhotoViewer.this.switchToInlineRunnable.run();
               }
            });
            var9.start();
         } else {
            this.switchToInlineRunnable.run();
            this.dismissInternal();
         }
      }

   }

   private void toggleActionBar(final boolean var1, boolean var2) {
      AnimatorSet var3 = this.actionBarAnimator;
      if (var3 != null) {
         var3.cancel();
      }

      if (var1) {
         this.actionBar.setVisibility(0);
         if (this.bottomLayout.getTag() != null) {
            this.bottomLayout.setVisibility(0);
         }

         if (this.captionTextView.getTag() != null) {
            this.captionTextView.setVisibility(0);
         }
      }

      this.isActionBarVisible = var1;
      if (VERSION.SDK_INT >= 21 && this.sendPhotoType != 1) {
         short var4;
         if (this.containerView.getPaddingLeft() <= 0 && this.containerView.getPaddingRight() <= 0) {
            var4 = 0;
         } else {
            var4 = 4098;
         }

         int var13 = 4 | var4;
         PhotoViewer.FrameLayoutDrawer var9;
         if (var1) {
            var9 = this.containerView;
            var9.setSystemUiVisibility(~var13 & var9.getSystemUiVisibility());
         } else {
            var9 = this.containerView;
            var9.setSystemUiVisibility(var13 | var9.getSystemUiVisibility());
         }
      }

      float var5 = 1.0F;
      float var8;
      if (var2) {
         ArrayList var10 = new ArrayList();
         ActionBar var6 = this.actionBar;
         Property var7 = View.ALPHA;
         if (var1) {
            var8 = 1.0F;
         } else {
            var8 = 0.0F;
         }

         var10.add(ObjectAnimator.ofFloat(var6, var7, new float[]{var8}));
         FrameLayout var18 = this.bottomLayout;
         Property var16;
         if (var18 != null) {
            var16 = View.ALPHA;
            if (var1) {
               var8 = 1.0F;
            } else {
               var8 = 0.0F;
            }

            var10.add(ObjectAnimator.ofFloat(var18, var16, new float[]{var8}));
         }

         GroupedPhotosListView var19 = this.groupedPhotosListView;
         var16 = View.ALPHA;
         if (var1) {
            var8 = 1.0F;
         } else {
            var8 = 0.0F;
         }

         var10.add(ObjectAnimator.ofFloat(var19, var16, new float[]{var8}));
         if (this.captionTextView.getTag() != null) {
            TextView var17 = this.captionTextView;
            var7 = View.ALPHA;
            if (!var1) {
               var5 = 0.0F;
            }

            var10.add(ObjectAnimator.ofFloat(var17, var7, new float[]{var5}));
         }

         this.actionBarAnimator = new AnimatorSet();
         this.actionBarAnimator.playTogether(var10);
         this.actionBarAnimator.setDuration(200L);
         this.actionBarAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1x) {
               if (var1x.equals(PhotoViewer.this.actionBarAnimator)) {
                  PhotoViewer.this.actionBarAnimator = null;
               }

            }

            public void onAnimationEnd(Animator var1x) {
               if (var1x.equals(PhotoViewer.this.actionBarAnimator)) {
                  if (!var1) {
                     PhotoViewer.this.actionBar.setVisibility(4);
                     if (PhotoViewer.this.bottomLayout.getTag() != null) {
                        PhotoViewer.this.bottomLayout.setVisibility(4);
                     }

                     if (PhotoViewer.this.captionTextView.getTag() != null) {
                        PhotoViewer.this.captionTextView.setVisibility(4);
                     }
                  }

                  PhotoViewer.this.actionBarAnimator = null;
               }

            }
         });
         this.actionBarAnimator.start();
      } else {
         ActionBar var11 = this.actionBar;
         if (var1) {
            var8 = 1.0F;
         } else {
            var8 = 0.0F;
         }

         var11.setAlpha(var8);
         FrameLayout var12 = this.bottomLayout;
         if (var1) {
            var8 = 1.0F;
         } else {
            var8 = 0.0F;
         }

         var12.setAlpha(var8);
         GroupedPhotosListView var14 = this.groupedPhotosListView;
         if (var1) {
            var8 = 1.0F;
         } else {
            var8 = 0.0F;
         }

         var14.setAlpha(var8);
         TextView var15 = this.captionTextView;
         if (!var1) {
            var5 = 0.0F;
         }

         var15.setAlpha(var5);
      }

   }

   private void toggleCheckImageView(boolean var1) {
      AnimatorSet var2 = new AnimatorSet();
      ArrayList var3 = new ArrayList();
      FrameLayout var4 = this.pickerView;
      Property var5 = View.ALPHA;
      float var6 = 1.0F;
      float var7;
      if (var1) {
         var7 = 1.0F;
      } else {
         var7 = 0.0F;
      }

      var3.add(ObjectAnimator.ofFloat(var4, var5, new float[]{var7}));
      ImageView var11 = this.pickerViewSendButton;
      Property var9 = View.ALPHA;
      if (var1) {
         var7 = 1.0F;
      } else {
         var7 = 0.0F;
      }

      var3.add(ObjectAnimator.ofFloat(var11, var9, new float[]{var7}));
      if (this.needCaptionLayout) {
         TextView var10 = this.captionTextView;
         var5 = View.ALPHA;
         if (var1) {
            var7 = 1.0F;
         } else {
            var7 = 0.0F;
         }

         var3.add(ObjectAnimator.ofFloat(var10, var5, new float[]{var7}));
      }

      int var8 = this.sendPhotoType;
      if (var8 == 0 || var8 == 4) {
         CheckBox var13 = this.checkImageView;
         var9 = View.ALPHA;
         if (var1) {
            var7 = 1.0F;
         } else {
            var7 = 0.0F;
         }

         var3.add(ObjectAnimator.ofFloat(var13, var9, new float[]{var7}));
         PhotoViewer.CounterView var12 = this.photosCounterView;
         var5 = View.ALPHA;
         if (var1) {
            var7 = var6;
         } else {
            var7 = 0.0F;
         }

         var3.add(ObjectAnimator.ofFloat(var12, var5, new float[]{var7}));
      }

      var2.playTogether(var3);
      var2.setDuration(200L);
      var2.start();
   }

   private void toggleMiniProgress(boolean var1, boolean var2) {
      byte var3 = 0;
      AnimatorSet var4;
      if (var2) {
         this.toggleMiniProgressInternal(var1);
         if (var1) {
            var4 = this.miniProgressAnimator;
            if (var4 != null) {
               var4.cancel();
               this.miniProgressAnimator = null;
            }

            AndroidUtilities.cancelRunOnUIThread(this.miniProgressShowRunnable);
            if (this.firstAnimationDelay) {
               this.firstAnimationDelay = false;
               this.toggleMiniProgressInternal(true);
            } else {
               AndroidUtilities.runOnUIThread(this.miniProgressShowRunnable, 500L);
            }
         } else {
            AndroidUtilities.cancelRunOnUIThread(this.miniProgressShowRunnable);
            var4 = this.miniProgressAnimator;
            if (var4 != null) {
               var4.cancel();
               this.toggleMiniProgressInternal(false);
            }
         }
      } else {
         var4 = this.miniProgressAnimator;
         if (var4 != null) {
            var4.cancel();
            this.miniProgressAnimator = null;
         }

         RadialProgressView var6 = this.miniProgressView;
         float var5;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.0F;
         }

         var6.setAlpha(var5);
         var6 = this.miniProgressView;
         if (!var1) {
            var3 = 4;
         }

         var6.setVisibility(var3);
      }

   }

   private void toggleMiniProgressInternal(final boolean var1) {
      if (var1) {
         this.miniProgressView.setVisibility(0);
      }

      this.miniProgressAnimator = new AnimatorSet();
      AnimatorSet var2 = this.miniProgressAnimator;
      RadialProgressView var3 = this.miniProgressView;
      Property var4 = View.ALPHA;
      float var5;
      if (var1) {
         var5 = 1.0F;
      } else {
         var5 = 0.0F;
      }

      var2.playTogether(new Animator[]{ObjectAnimator.ofFloat(var3, var4, new float[]{var5})});
      this.miniProgressAnimator.setDuration(200L);
      this.miniProgressAnimator.addListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1x) {
            if (var1x.equals(PhotoViewer.this.miniProgressAnimator)) {
               PhotoViewer.this.miniProgressAnimator = null;
            }

         }

         public void onAnimationEnd(Animator var1x) {
            if (var1x.equals(PhotoViewer.this.miniProgressAnimator)) {
               if (!var1) {
                  PhotoViewer.this.miniProgressView.setVisibility(4);
               }

               PhotoViewer.this.miniProgressAnimator = null;
            }

         }
      });
      this.miniProgressAnimator.start();
   }

   private void togglePhotosListView(boolean var1, boolean var2) {
      if (var1 != this.isPhotosListViewVisible) {
         if (var1) {
            this.selectedPhotosListView.setVisibility(0);
         }

         this.isPhotosListViewVisible = var1;
         this.selectedPhotosListView.setEnabled(var1);
         float var3 = 1.0F;
         float var7;
         if (var2) {
            ArrayList var4 = new ArrayList();
            RecyclerListView var5 = this.selectedPhotosListView;
            Property var6 = View.ALPHA;
            if (var1) {
               var7 = 1.0F;
            } else {
               var7 = 0.0F;
            }

            var4.add(ObjectAnimator.ofFloat(var5, var6, new float[]{var7}));
            var5 = this.selectedPhotosListView;
            var6 = View.TRANSLATION_Y;
            if (var1) {
               var7 = 0.0F;
            } else {
               var7 = (float)(-AndroidUtilities.dp(10.0F));
            }

            var4.add(ObjectAnimator.ofFloat(var5, var6, new float[]{var7}));
            PhotoViewer.CounterView var11 = this.photosCounterView;
            Property var10 = View.ROTATION_X;
            if (!var1) {
               var3 = 0.0F;
            }

            var4.add(ObjectAnimator.ofFloat(var11, var10, new float[]{var3}));
            this.currentListViewAnimation = new AnimatorSet();
            this.currentListViewAnimation.playTogether(var4);
            if (!var1) {
               this.currentListViewAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     if (PhotoViewer.this.currentListViewAnimation != null && PhotoViewer.this.currentListViewAnimation.equals(var1)) {
                        PhotoViewer.this.selectedPhotosListView.setVisibility(8);
                        PhotoViewer.this.currentListViewAnimation = null;
                     }

                  }
               });
            }

            this.currentListViewAnimation.setDuration(200L);
            this.currentListViewAnimation.start();
         } else {
            RecyclerListView var8 = this.selectedPhotosListView;
            if (var1) {
               var7 = 1.0F;
            } else {
               var7 = 0.0F;
            }

            var8.setAlpha(var7);
            var8 = this.selectedPhotosListView;
            if (var1) {
               var7 = 0.0F;
            } else {
               var7 = (float)(-AndroidUtilities.dp(10.0F));
            }

            var8.setTranslationY(var7);
            PhotoViewer.CounterView var9 = this.photosCounterView;
            if (!var1) {
               var3 = 0.0F;
            }

            var9.setRotationX(var3);
            if (!var1) {
               this.selectedPhotosListView.setVisibility(8);
            }
         }

      }
   }

   private void updateAccessibilityOverlayVisibility() {
      if (this.playButtonAccessibilityOverlay != null) {
         if (this.isCurrentVideo) {
            VideoPlayer var1 = this.videoPlayer;
            if (var1 == null || !var1.isPlaying()) {
               this.playButtonAccessibilityOverlay.setVisibility(0);
               return;
            }
         }

         this.playButtonAccessibilityOverlay.setVisibility(4);
      }
   }

   private void updateCaptionTextForCurrentPhoto(Object var1) {
      CharSequence var2;
      if (var1 instanceof MediaController.PhotoEntry) {
         var2 = ((MediaController.PhotoEntry)var1).caption;
      } else if (!(var1 instanceof TLRPC.BotInlineResult) && var1 instanceof MediaController.SearchImage) {
         var2 = ((MediaController.SearchImage)var1).caption;
      } else {
         var2 = null;
      }

      if (TextUtils.isEmpty(var2)) {
         this.captionEditText.setFieldText("");
      } else {
         this.captionEditText.setFieldText(var2);
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

   private void updatePlayerState(boolean var1, int var2) {
      if (this.videoPlayer != null) {
         if (this.isStreaming) {
            if (var2 == 2 && this.skipFirstBufferingProgress) {
               if (var1) {
                  this.skipFirstBufferingProgress = false;
               }
            } else {
               boolean var3;
               if (this.seekToProgressPending == 0.0F && var2 != 2) {
                  var3 = false;
               } else {
                  var3 = true;
               }

               this.toggleMiniProgress(var3, true);
            }
         }

         if (var1 && var2 != 4 && var2 != 1) {
            try {
               this.parentActivity.getWindow().addFlags(128);
               this.keepScreenOnFlagSet = true;
            } catch (Exception var7) {
               FileLog.e((Throwable)var7);
            }
         } else {
            try {
               this.parentActivity.getWindow().clearFlags(128);
               this.keepScreenOnFlagSet = false;
            } catch (Exception var6) {
               FileLog.e((Throwable)var6);
            }
         }

         MessageObject var4;
         if (this.seekToProgressPending != 0.0F && (var2 == 3 || var2 == 1)) {
            int var5 = (int)((float)this.videoPlayer.getDuration() * this.seekToProgressPending);
            this.videoPlayer.seekTo((long)var5);
            this.seekToProgressPending = 0.0F;
            var4 = this.currentMessageObject;
            if (var4 != null && !FileLoader.getInstance(var4.currentAccount).isLoadingVideoAny(this.currentMessageObject.getDocument())) {
               this.skipFirstBufferingProgress = true;
            }
         }

         if (var2 == 3) {
            if (this.aspectRatioFrameLayout.getVisibility() != 0) {
               this.aspectRatioFrameLayout.setVisibility(0);
            }

            if (!this.pipItem.isEnabled()) {
               this.pipAvailable = true;
               this.pipItem.setEnabled(true);
               this.pipItem.setAlpha(1.0F);
            }

            this.playerWasReady = true;
            var4 = this.currentMessageObject;
            if (var4 != null && var4.isVideo()) {
               AndroidUtilities.cancelRunOnUIThread(this.setLoadingRunnable);
               FileLoader.getInstance(this.currentMessageObject.currentAccount).removeLoadingVideo(this.currentMessageObject.getDocument(), true, false);
            }
         } else if (var2 == 2 && var1) {
            var4 = this.currentMessageObject;
            if (var4 != null && var4.isVideo()) {
               if (this.playerWasReady) {
                  this.setLoadingRunnable.run();
               } else {
                  AndroidUtilities.runOnUIThread(this.setLoadingRunnable, 1000L);
               }
            }
         }

         PipVideoView var8;
         if (this.videoPlayer.isPlaying() && var2 != 4) {
            if (!this.isPlaying) {
               this.isPlaying = true;
               this.videoPlayButton.setImageResource(2131165478);
               AndroidUtilities.runOnUIThread(this.updateProgressRunnable);
            }
         } else if (this.isPlaying) {
            this.isPlaying = false;
            this.videoPlayButton.setImageResource(2131165479);
            AndroidUtilities.cancelRunOnUIThread(this.updateProgressRunnable);
            if (var2 == 4) {
               if (this.isCurrentVideo) {
                  if (!this.videoTimelineView.isDragging()) {
                     this.videoTimelineView.setProgress(0.0F);
                     if (!this.inPreview && this.videoTimelineView.getVisibility() == 0) {
                        this.videoPlayer.seekTo((long)((int)(this.videoTimelineView.getLeftProgress() * (float)this.videoPlayer.getDuration())));
                     } else {
                        this.videoPlayer.seekTo(0L);
                     }

                     this.videoPlayer.pause();
                     this.containerView.invalidate();
                  }
               } else {
                  if (!this.isActionBarVisible) {
                     this.toggleActionBar(true, true);
                  }

                  if (!this.videoPlayerSeekbar.isDragging()) {
                     this.videoPlayerSeekbar.setProgress(0.0F);
                     this.videoPlayerControlFrameLayout.invalidate();
                     if (!this.inPreview && this.videoTimelineView.getVisibility() == 0) {
                        this.videoPlayer.seekTo((long)((int)(this.videoTimelineView.getLeftProgress() * (float)this.videoPlayer.getDuration())));
                     } else {
                        this.videoPlayer.seekTo(0L);
                     }

                     this.videoPlayer.pause();
                  }
               }

               var8 = this.pipVideoView;
               if (var8 != null) {
                  var8.onVideoCompleted();
               }
            }
         }

         var8 = this.pipVideoView;
         if (var8 != null) {
            var8.updatePlayButton();
         }

         this.updateVideoPlayerTime();
      }
   }

   private void updateSelectedCount() {
      PhotoViewer.PhotoViewerProvider var1 = this.placeProvider;
      if (var1 != null) {
         int var2 = var1.getSelectedCount();
         this.photosCounterView.setCount(var2);
         if (var2 == 0) {
            this.togglePhotosListView(false, true);
         }

      }
   }

   private void updateVideoInfo() {
      ActionBar var1 = this.actionBar;
      if (var1 != null) {
         int var2 = this.compressionsCount;
         String var3 = null;
         if (var2 == 0) {
            var1.setSubtitle((CharSequence)null);
         } else {
            var2 = this.selectedCompression;
            if (var2 == 0) {
               this.compressItem.setImageResource(2131165896);
            } else if (var2 == 1) {
               this.compressItem.setImageResource(2131165897);
            } else if (var2 == 2) {
               this.compressItem.setImageResource(2131165898);
            } else if (var2 == 3) {
               this.compressItem.setImageResource(2131165899);
            } else if (var2 == 4) {
               this.compressItem.setImageResource(2131165895);
            }

            ImageView var4 = this.compressItem;
            StringBuilder var9 = new StringBuilder();
            var9.append(LocaleController.getString("AccDescrVideoQuality", 2131558482));
            var9.append(", ");
            var2 = Math.max(0, this.selectedCompression);
            var9.append((new String[]{"240", "360", "480", "720", "1080"})[var2]);
            var4.setContentDescription(var9.toString());
            this.estimatedDuration = (long)Math.ceil((double)((this.videoTimelineView.getRightProgress() - this.videoTimelineView.getLeftProgress()) * this.videoDuration));
            int var5;
            if (this.compressItem.getTag() != null && this.selectedCompression != this.compressionsCount - 1) {
               var2 = this.rotationValue;
               if (var2 != 90 && var2 != 270) {
                  var2 = this.resultWidth;
               } else {
                  var2 = this.resultHeight;
               }

               var5 = this.rotationValue;
               if (var5 != 90 && var5 != 270) {
                  var5 = this.resultHeight;
               } else {
                  var5 = this.resultWidth;
               }

               this.estimatedSize = (int)((float)(this.audioFramesSize + this.videoFramesSize) * ((float)this.estimatedDuration / this.videoDuration));
               int var6 = this.estimatedSize;
               this.estimatedSize = var6 + var6 / '耀' * 16;
            } else {
               var2 = this.rotationValue;
               if (var2 != 90 && var2 != 270) {
                  var2 = this.originalWidth;
               } else {
                  var2 = this.originalHeight;
               }

               var5 = this.rotationValue;
               if (var5 != 90 && var5 != 270) {
                  var5 = this.originalHeight;
               } else {
                  var5 = this.originalWidth;
               }

               this.estimatedSize = (int)((float)this.originalSize * ((float)this.estimatedDuration / this.videoDuration));
            }

            if (this.videoTimelineView.getLeftProgress() == 0.0F) {
               this.startTime = -1L;
            } else {
               this.startTime = (long)(this.videoTimelineView.getLeftProgress() * this.videoDuration) * 1000L;
            }

            if (this.videoTimelineView.getRightProgress() == 1.0F) {
               this.endTime = -1L;
            } else {
               this.endTime = (long)(this.videoTimelineView.getRightProgress() * this.videoDuration) * 1000L;
            }

            String var10 = String.format("%dx%d", var2, var5);
            long var7 = this.estimatedDuration;
            var2 = (int)(var7 / 1000L / 60L);
            this.currentSubtitle = String.format("%s, %s", var10, String.format("%d:%02d, ~%s", var2, (int)Math.ceil((double)(var7 / 1000L)) - var2 * 60, AndroidUtilities.formatFileSize((long)this.estimatedSize)));
            var1 = this.actionBar;
            if (!this.muteVideo) {
               var3 = this.currentSubtitle;
            }

            var1.setSubtitle(var3);
         }
      }
   }

   private void updateVideoPlayerTime() {
      VideoPlayer var1 = this.videoPlayer;
      Integer var2 = 0;
      String var11;
      if (var1 == null) {
         var11 = String.format("%02d:%02d / %02d:%02d", var2, var2, var2, var2);
      } else {
         long var3 = var1.getCurrentPosition();
         long var5 = 0L;
         long var7 = var3;
         if (var3 < 0L) {
            var7 = 0L;
         }

         var3 = this.videoPlayer.getDuration();
         if (var3 >= 0L) {
            var5 = var3;
         }

         if (var5 != -9223372036854775807L && var7 != -9223372036854775807L) {
            var3 = var7;
            long var9 = var5;
            if (!this.inPreview) {
               var3 = var7;
               var9 = var5;
               if (this.videoTimelineView.getVisibility() == 0) {
                  var5 = (long)((float)var5 * (this.videoTimelineView.getRightProgress() - this.videoTimelineView.getLeftProgress()));
                  var7 = (long)((float)var7 - this.videoTimelineView.getLeftProgress() * (float)var5);
                  var3 = var7;
                  var9 = var5;
                  if (var7 > var5) {
                     var3 = var5;
                     var9 = var5;
                  }
               }
            }

            var7 = var3 / 1000L;
            var3 = var9 / 1000L;
            var11 = String.format("%02d:%02d / %02d:%02d", var7 / 60L, var7 % 60L, var3 / 60L, var3 % 60L);
         } else {
            var11 = String.format("%02d:%02d / %02d:%02d", var2, var2, var2, var2);
         }
      }

      this.videoPlayerTime.setText(var11);
   }

   private void updateWidthHeightBitrateForCompression() {
      int var1 = this.compressionsCount;
      if (var1 > 0) {
         if (this.selectedCompression >= var1) {
            this.selectedCompression = var1 - 1;
         }

         var1 = this.selectedCompression;
         if (var1 != this.compressionsCount - 1) {
            float var2;
            if (var1 != 0) {
               if (var1 != 1) {
                  if (var1 != 2) {
                     var1 = 2500000;
                     var2 = 1280.0F;
                  } else {
                     var2 = 854.0F;
                     var1 = 1100000;
                  }
               } else {
                  var2 = 640.0F;
                  var1 = 900000;
               }
            } else {
               var2 = 426.0F;
               var1 = 400000;
            }

            int var3 = this.originalWidth;
            int var4 = this.originalHeight;
            float var5;
            if (var3 > var4) {
               var5 = (float)var3;
            } else {
               var5 = (float)var4;
            }

            var2 /= var5;
            this.resultWidth = Math.round((float)this.originalWidth * var2 / 2.0F) * 2;
            this.resultHeight = Math.round((float)this.originalHeight * var2 / 2.0F) * 2;
            if (this.bitrate != 0) {
               this.bitrate = Math.min(var1, (int)((float)this.originalBitrate / var2));
               this.videoFramesSize = (long)((float)(this.bitrate / 8) * this.videoDuration / 1000.0F);
            }
         }

      }
   }

   public void closePhoto(boolean var1, boolean var2) {
      byte var3 = 3;
      int var4;
      if (!var2) {
         var4 = this.currentEditMode;
         if (var4 != 0) {
            if (var4 == 3) {
               PhotoPaintView var35 = this.photoPaintView;
               if (var35 != null) {
                  var35.maybeShowDismissalAlert(this, this.parentActivity, new _$$Lambda$PhotoViewer$KYtFxmNpM70wviBQal0FRLyYs_s(this));
                  return;
               }
            }

            this.switchToEditMode(0);
            return;
         }
      }

      PhotoViewer.QualityChooseView var5 = this.qualityChooseView;
      if (var5 != null && var5.getTag() != null) {
         this.qualityPicker.cancelButton.callOnClick();
      } else {
         this.openedFullScreenVideo = false;

         try {
            if (this.visibleDialog != null) {
               this.visibleDialog.dismiss();
               this.visibleDialog = null;
            }
         } catch (Exception var21) {
            FileLog.e((Throwable)var21);
         }

         if (VERSION.SDK_INT >= 21 && this.actionBar != null) {
            var4 = this.containerView.getSystemUiVisibility() & 4102;
            if (var4 != 0) {
               PhotoViewer.FrameLayoutDrawer var22 = this.containerView;
               var22.setSystemUiVisibility(~var4 & var22.getSystemUiVisibility());
            }
         }

         var4 = this.currentEditMode;
         if (var4 != 0) {
            if (var4 == 2) {
               this.photoFilterView.shutdown();
               this.containerView.removeView(this.photoFilterView);
               this.photoFilterView = null;
            } else if (var4 == 1) {
               this.editorDoneLayout.setVisibility(8);
               this.photoCropView.setVisibility(8);
            } else if (var4 == 3) {
               this.photoPaintView.shutdown();
               this.containerView.removeView(this.photoPaintView);
               this.photoPaintView = null;
            }

            this.currentEditMode = 0;
         } else if (this.sendPhotoType == 1) {
            this.photoCropView.setVisibility(8);
         }

         if (this.parentActivity != null && (this.isInline || this.isVisible) && !this.checkAnimation() && this.placeProvider != null) {
            if (this.captionEditText.hideActionMode() && !var2) {
               return;
            }

            PhotoViewer.PlaceProviderObject var6 = this.placeProvider.getPlaceForPhoto(this.currentMessageObject, this.getFileLocation(this.currentFileLocation), this.currentIndex, true);
            AnimatedFileDrawable var23;
            if (this.videoPlayer != null && var6 != null) {
               var23 = var6.imageReceiver.getAnimation();
               if (var23 != null) {
                  if (this.textureUploaded) {
                     Bitmap var7 = var23.getAnimatedBitmap();
                     if (var7 != null) {
                        try {
                           Bitmap var8 = this.videoTextureView.getBitmap(var7.getWidth(), var7.getHeight());
                           Canvas var9 = new Canvas(var7);
                           var9.drawBitmap(var8, 0.0F, 0.0F, (Paint)null);
                           var8.recycle();
                        } catch (Throwable var20) {
                           FileLog.e(var20);
                        }
                     }
                  }

                  var23.seekTo(this.videoPlayer.getCurrentPosition(), FileLoader.getInstance(this.currentMessageObject.currentAccount).isLoadingVideo(this.currentMessageObject.getDocument(), true) ^ true);
                  var6.imageReceiver.setAllowStartAnimation(true);
                  var6.imageReceiver.startAnimation();
               }
            }

            this.releasePlayer(true);
            this.captionEditText.onDestroy();
            this.parentChatActivity = null;
            this.removeObservers();
            this.isActionBarVisible = false;
            VelocityTracker var24 = this.velocityTracker;
            if (var24 != null) {
               var24.recycle();
               this.velocityTracker = null;
            }

            if (this.isInline) {
               this.isInline = false;
               this.animationInProgress = 0;
               this.onPhotoClosed(var6);
               this.containerView.setScaleX(1.0F);
               this.containerView.setScaleY(1.0F);
            } else {
               if (var1) {
                  this.animationInProgress = 1;
                  this.animatingImageView.setVisibility(0);
                  this.containerView.invalidate();
                  AnimatorSet var31 = new AnimatorSet();
                  android.view.ViewGroup.LayoutParams var33 = this.animatingImageView.getLayoutParams();
                  int var10;
                  ClippingImageView var25;
                  RectF var26;
                  if (var6 != null) {
                     var25 = this.animatingImageView;
                     if (var6.radius != 0) {
                        var1 = true;
                     } else {
                        var1 = false;
                     }

                     var25.setNeedRadius(var1);
                     var26 = var6.imageReceiver.getDrawRegion();
                     var33.width = (int)var26.width();
                     var33.height = (int)var26.height();
                     var4 = var6.imageReceiver.getOrientation();
                     var10 = var6.imageReceiver.getAnimatedOrientation();
                     if (var10 != 0) {
                        var4 = var10;
                     }

                     this.animatingImageView.setOrientation(var4);
                     this.animatingImageView.setImageBitmap(var6.thumb);
                  } else {
                     this.animatingImageView.setNeedRadius(false);
                     var33.width = this.centerImage.getImageWidth();
                     var33.height = this.centerImage.getImageHeight();
                     this.animatingImageView.setOrientation(this.centerImage.getOrientation());
                     this.animatingImageView.setImageBitmap(this.centerImage.getBitmapSafe());
                     var26 = null;
                  }

                  this.animatingImageView.setLayoutParams(var33);
                  float var11 = (float)this.windowView.getMeasuredWidth() / (float)var33.width;
                  var10 = AndroidUtilities.displaySize.y;
                  if (VERSION.SDK_INT >= 21) {
                     var4 = AndroidUtilities.statusBarHeight;
                  } else {
                     var4 = 0;
                  }

                  float var12 = (float)(var10 + var4) / (float)var33.height;
                  float var13 = var11;
                  if (var11 > var12) {
                     var13 = var12;
                  }

                  var12 = (float)var33.width;
                  var11 = this.scale;
                  float var14 = (float)var33.height;
                  var12 = ((float)this.windowView.getMeasuredWidth() - var12 * var11 * var13) / 2.0F;
                  var10 = AndroidUtilities.displaySize.y;
                  if (VERSION.SDK_INT >= 21) {
                     var4 = AndroidUtilities.statusBarHeight;
                  } else {
                     var4 = 0;
                  }

                  var11 = ((float)(var10 + var4) - var14 * var11 * var13) / 2.0F;
                  this.animatingImageView.setTranslationX(var12 + this.translationX);
                  this.animatingImageView.setTranslationY(var11 + this.translationY);
                  this.animatingImageView.setScaleX(this.scale * var13);
                  this.animatingImageView.setScaleY(this.scale * var13);
                  if (var6 != null) {
                     var6.imageReceiver.setVisible(false, true);
                     int var15 = (int)Math.abs(var26.left - (float)var6.imageReceiver.getImageX());
                     int var16 = (int)Math.abs(var26.top - (float)var6.imageReceiver.getImageY());
                     int[] var36 = new int[2];
                     var6.parentView.getLocationInWindow(var36);
                     var10 = var36[1];
                     if (VERSION.SDK_INT >= 21) {
                        var4 = 0;
                     } else {
                        var4 = AndroidUtilities.statusBarHeight;
                     }

                     var10 = (int)((float)(var10 - var4) - ((float)var6.viewY + var26.top) + (float)var6.clipTopAddition);
                     var4 = var10;
                     if (var10 < 0) {
                        var4 = 0;
                     }

                     var12 = (float)var6.viewY;
                     var13 = var26.top;
                     var11 = var26.bottom;
                     int var17 = var36[1];
                     int var18 = var6.parentView.getHeight();
                     if (VERSION.SDK_INT >= 21) {
                        var10 = 0;
                     } else {
                        var10 = AndroidUtilities.statusBarHeight;
                     }

                     var17 = (int)(var12 + var13 + (var11 - var13) - (float)(var17 + var18 - var10) + (float)var6.clipBottomAddition);
                     var10 = var17;
                     if (var17 < 0) {
                        var10 = 0;
                     }

                     var4 = Math.max(var4, var16);
                     var10 = Math.max(var10, var16);
                     this.animationValues[0][0] = this.animatingImageView.getScaleX();
                     this.animationValues[0][1] = this.animatingImageView.getScaleY();
                     this.animationValues[0][2] = this.animatingImageView.getTranslationX();
                     this.animationValues[0][3] = this.animatingImageView.getTranslationY();
                     float[][] var37 = this.animationValues;
                     var37[0][4] = 0.0F;
                     var37[0][5] = 0.0F;
                     var37[0][6] = 0.0F;
                     var37[0][7] = 0.0F;
                     var37[0][8] = 0.0F;
                     var37[0][9] = 0.0F;
                     float[] var27 = var37[1];
                     var11 = var6.scale;
                     var27[0] = var11;
                     var37[1][1] = var11;
                     var37[1][2] = (float)var6.viewX + var26.left * var11;
                     var37[1][3] = (float)var6.viewY + var26.top * var11;
                     float[] var29 = var37[1];
                     var13 = (float)var15;
                     var29[4] = var13 * var11;
                     var37[1][5] = (float)var4 * var11;
                     var37[1][6] = (float)var10 * var11;
                     var37[1][7] = (float)var6.radius;
                     var37[1][8] = (float)var16 * var11;
                     var37[1][9] = var13 * var11;
                     byte var39 = var3;
                     if (this.sendPhotoType == 1) {
                        var39 = 4;
                     }

                     ArrayList var30 = new ArrayList(var39);
                     var30.add(ObjectAnimator.ofFloat(this.animatingImageView, AnimationProperties.CLIPPING_IMAGE_VIEW_PROGRESS, new float[]{0.0F, 1.0F}));
                     var30.add(ObjectAnimator.ofInt(this.backgroundDrawable, AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[]{0}));
                     var30.add(ObjectAnimator.ofFloat(this.containerView, View.ALPHA, new float[]{0.0F}));
                     if (this.sendPhotoType == 1) {
                        var30.add(ObjectAnimator.ofFloat(this.photoCropView, View.ALPHA, new float[]{0.0F}));
                     }

                     var31.playTogether(var30);
                  } else {
                     var10 = AndroidUtilities.displaySize.y;
                     if (VERSION.SDK_INT >= 21) {
                        var4 = AndroidUtilities.statusBarHeight;
                     } else {
                        var4 = 0;
                     }

                     var4 += var10;
                     ObjectAnimator var28 = ObjectAnimator.ofInt(this.backgroundDrawable, AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[]{0});
                     ObjectAnimator var19 = ObjectAnimator.ofFloat(this.animatingImageView, View.ALPHA, new float[]{0.0F});
                     var25 = this.animatingImageView;
                     Property var38 = View.TRANSLATION_Y;
                     if (this.translationY < 0.0F) {
                        var4 = -var4;
                     }

                     var31.playTogether(new Animator[]{var28, var19, ObjectAnimator.ofFloat(var25, var38, new float[]{(float)var4}), ObjectAnimator.ofFloat(this.containerView, View.ALPHA, new float[]{0.0F})});
                  }

                  this.animationEndRunnable = new _$$Lambda$PhotoViewer$SQuc4TyZ0dWDiWuazMyrUv4byaU(this, var6);
                  var31.setDuration(200L);
                  var31.addListener(new AnimatorListenerAdapter() {
                     // $FF: synthetic method
                     public void lambda$onAnimationEnd$0$PhotoViewer$37() {
                        if (PhotoViewer.this.animationEndRunnable != null) {
                           PhotoViewer.this.animationEndRunnable.run();
                           PhotoViewer.this.animationEndRunnable = null;
                        }

                     }

                     public void onAnimationEnd(Animator var1) {
                        AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$37$riPaWbwqB97e1xvF_frnJnVZJ9M(this));
                     }
                  });
                  this.transitionAnimationStartTime = System.currentTimeMillis();
                  if (VERSION.SDK_INT >= 18) {
                     this.containerView.setLayerType(2, (Paint)null);
                  }

                  var31.start();
               } else {
                  AnimatorSet var32 = new AnimatorSet();
                  var32.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.containerView, View.SCALE_X, new float[]{0.9F}), ObjectAnimator.ofFloat(this.containerView, View.SCALE_Y, new float[]{0.9F}), ObjectAnimator.ofInt(this.backgroundDrawable, AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[]{0}), ObjectAnimator.ofFloat(this.containerView, View.ALPHA, new float[]{0.0F})});
                  this.animationInProgress = 2;
                  this.animationEndRunnable = new _$$Lambda$PhotoViewer$mbE9dgLgiv_6p2JdgFOuRdnxess(this, var6);
                  var32.setDuration(200L);
                  var32.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1) {
                        if (PhotoViewer.this.animationEndRunnable != null) {
                           PhotoViewer.this.animationEndRunnable.run();
                           PhotoViewer.this.animationEndRunnable = null;
                        }

                     }
                  });
                  this.transitionAnimationStartTime = System.currentTimeMillis();
                  if (VERSION.SDK_INT >= 18) {
                     this.containerView.setLayerType(2, (Paint)null);
                  }

                  var32.start();
               }

               var23 = this.currentAnimation;
               if (var23 != null) {
                  var23.setSecondParentView((View)null);
                  this.currentAnimation = null;
                  this.centerImage.setImageBitmap((Drawable)null);
               }

               PhotoViewer.PhotoViewerProvider var34 = this.placeProvider;
               if (var34 != null && !var34.canScrollAway()) {
                  this.placeProvider.cancelButtonPressed();
               }
            }
         }

      }
   }

   public void destroyPhotoViewer() {
      if (this.parentActivity != null && this.windowView != null) {
         PipVideoView var1 = this.pipVideoView;
         if (var1 != null) {
            var1.close();
            this.pipVideoView = null;
         }

         this.removeObservers();
         this.releasePlayer(false);

         try {
            if (this.windowView.getParent() != null) {
               ((WindowManager)this.parentActivity.getSystemService("window")).removeViewImmediate(this.windowView);
            }

            this.windowView = null;
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

         ImageReceiver.BitmapHolder var3 = this.currentThumb;
         if (var3 != null) {
            var3.release();
            this.currentThumb = null;
         }

         this.animatingImageView.setImageBitmap((ImageReceiver.BitmapHolder)null);
         PhotoViewerCaptionEnterView var4 = this.captionEditText;
         if (var4 != null) {
            var4.onDestroy();
         }

         if (this == PipInstance) {
            PipInstance = null;
         } else {
            Instance = null;
         }
      }

   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      int var4 = NotificationCenter.fileDidFailedLoad;
      byte var21 = 0;
      String[] var33;
      String var36;
      if (var1 == var4) {
         var36 = (String)var3[0];

         for(var1 = var21; var1 < 3; ++var1) {
            var33 = this.currentFileNames;
            if (var33[var1] != null && var33[var1].equals(var36)) {
               this.photoProgressViews[var1].setProgress(1.0F, true);
               this.checkProgress(var1, true);
               break;
            }
         }
      } else {
         MessageObject var22;
         if (var1 == NotificationCenter.fileDidLoad) {
            var36 = (String)var3[0];

            for(var1 = 0; var1 < 3; ++var1) {
               var33 = this.currentFileNames;
               if (var33[var1] != null && var33[var1].equals(var36)) {
                  this.photoProgressViews[var1].setProgress(1.0F, true);
                  this.checkProgress(var1, true);
                  if (this.videoPlayer == null && var1 == 0) {
                     label490: {
                        var22 = this.currentMessageObject;
                        if (var22 == null || !var22.isVideo()) {
                           TLRPC.BotInlineResult var41 = this.currentBotInlineResult;
                           if (var41 == null || !var41.type.equals("video") && !MessageObject.isVideoDocument(this.currentBotInlineResult.document)) {
                              break label490;
                           }
                        }

                        this.onActionClick(false);
                     }
                  }

                  if (var1 == 0 && this.videoPlayer != null) {
                     this.currentVideoFinishedLoading = true;
                  }
                  break;
               }
            }
         } else {
            long var9;
            String var27;
            if (var1 == NotificationCenter.FileLoadProgressChanged) {
               var27 = (String)var3[0];
               var1 = 0;

               while(true) {
                  float var6 = 1.0F;
                  if (var1 >= 3) {
                     break;
                  }

                  String[] var35 = this.currentFileNames;
                  if (var35[var1] != null && var35[var1].equals(var27)) {
                     Float var40 = (Float)var3[1];
                     this.photoProgressViews[var1].setProgress(var40, true);
                     if (var1 == 0 && this.videoPlayer != null && this.videoPlayerSeekbar != null) {
                        if (!this.currentVideoFinishedLoading) {
                           var9 = SystemClock.elapsedRealtime();
                           if (Math.abs(var9 - this.lastBufferedPositionCheck) >= 500L) {
                              float var11 = this.seekToProgressPending;
                              var6 = var11;
                              if (var11 == 0.0F) {
                                 long var12 = this.videoPlayer.getDuration();
                                 long var14 = this.videoPlayer.getCurrentPosition();
                                 if (var12 >= 0L && var12 != -9223372036854775807L && var14 >= 0L) {
                                    var6 = (float)var14 / (float)var12;
                                 } else {
                                    var6 = 0.0F;
                                 }
                              }

                              if (this.isStreaming) {
                                 var6 = FileLoader.getInstance(this.currentAccount).getBufferedProgressFromPosition(var6, this.currentFileNames[0]);
                              } else {
                                 var6 = 1.0F;
                              }

                              this.lastBufferedPositionCheck = var9;
                           } else {
                              var6 = -1.0F;
                           }
                        }

                        if (var6 != -1.0F) {
                           this.videoPlayerSeekbar.setBufferedProgress(var6);
                           PipVideoView var37 = this.pipVideoView;
                           if (var37 != null) {
                              var37.setBufferedProgress(var6);
                           }

                           this.videoPlayerControlFrameLayout.invalidate();
                        }

                        this.checkBufferedProgress(var40);
                     }
                  }

                  ++var1;
               }
            } else {
               ArrayList var5;
               int var17;
               if (var1 == NotificationCenter.dialogPhotosLoaded) {
                  var2 = (Integer)var3[3];
                  var1 = (Integer)var3[0];
                  if (this.avatarsDialogId == var1 && this.classGuid == var2) {
                     boolean var16 = (Boolean)var3[2];
                     var5 = (ArrayList)var3[4];
                     if (var5.isEmpty()) {
                        return;
                     }

                     this.imagesArrLocations.clear();
                     this.imagesArrLocationsSizes.clear();
                     this.avatarsArr.clear();
                     var4 = 0;

                     for(var1 = -1; var4 < var5.size(); var1 = var17) {
                        TLRPC.Photo var28 = (TLRPC.Photo)var5.get(var4);
                        var17 = var1;
                        if (var28 != null) {
                           var17 = var1;
                           if (!(var28 instanceof TLRPC.TL_photoEmpty)) {
                              ArrayList var31 = var28.sizes;
                              if (var31 == null) {
                                 var17 = var1;
                              } else {
                                 TLRPC.PhotoSize var34 = FileLoader.getClosestPhotoSizeWithSize(var31, 640);
                                 var17 = var1;
                                 if (var34 != null) {
                                    var2 = var1;
                                    if (var1 == -1) {
                                       var2 = var1;
                                       if (this.currentFileLocation != null) {
                                          var17 = 0;

                                          while(true) {
                                             var2 = var1;
                                             if (var17 >= var28.sizes.size()) {
                                                break;
                                             }

                                             TLRPC.FileLocation var18 = ((TLRPC.PhotoSize)var28.sizes.get(var17)).location;
                                             var2 = var18.local_id;
                                             TLRPC.TL_fileLocationToBeDeprecated var8 = this.currentFileLocation.location;
                                             if (var2 == var8.local_id && var18.volume_id == var8.volume_id) {
                                                var2 = this.imagesArrLocations.size();
                                                break;
                                             }

                                             ++var17;
                                          }
                                       }
                                    }

                                    var1 = var28.dc_id;
                                    if (var1 != 0) {
                                       TLRPC.FileLocation var38 = var34.location;
                                       var38.dc_id = var1;
                                       var38.file_reference = var28.file_reference;
                                    }

                                    ImageLocation var39 = ImageLocation.getForPhoto(var34, var28);
                                    var17 = var2;
                                    if (var39 != null) {
                                       this.imagesArrLocations.add(var39);
                                       this.imagesArrLocationsSizes.add(var34.size);
                                       this.avatarsArr.add(var28);
                                       var17 = var2;
                                    }
                                 }
                              }
                           }
                        }

                        ++var4;
                     }

                     if (!this.avatarsArr.isEmpty()) {
                        this.menuItem.showSubItem(6);
                     } else {
                        this.menuItem.hideSubItem(6);
                     }

                     this.needSearchImageInArr = false;
                     this.currentIndex = -1;
                     if (var1 != -1) {
                        this.setImageIndex(var1, true);
                     } else {
                        var1 = this.avatarsDialogId;
                        TLRPC.Chat var29 = null;
                        TLRPC.User var30;
                        if (var1 > 0) {
                           var30 = MessagesController.getInstance(this.currentAccount).getUser(this.avatarsDialogId);
                        } else {
                           var29 = MessagesController.getInstance(this.currentAccount).getChat(-this.avatarsDialogId);
                           var30 = null;
                        }

                        if (var30 != null || var29 != null) {
                           ImageLocation var32;
                           if (var30 != null) {
                              var32 = ImageLocation.getForUser(var30, true);
                           } else {
                              var32 = ImageLocation.getForChat(var29, true);
                           }

                           if (var32 != null) {
                              this.imagesArrLocations.add(0, var32);
                              this.avatarsArr.add(0, new TLRPC.TL_photoEmpty());
                              this.imagesArrLocationsSizes.add(0, 0);
                              this.setImageIndex(0, true);
                           }
                        }
                     }

                     if (var16) {
                        MessagesController.getInstance(this.currentAccount).loadDialogPhotos(this.avatarsDialogId, 80, 0L, false, this.classGuid);
                     }
                  }
               } else if (var1 == NotificationCenter.mediaCountDidLoad) {
                  var9 = (Long)var3[0];
                  if (var9 == this.currentDialogId || var9 == this.mergeDialogId) {
                     if (var9 == this.currentDialogId) {
                        this.totalImagesCount = (Integer)var3[1];
                     } else if (var9 == this.mergeDialogId) {
                        this.totalImagesCountMerge = (Integer)var3[1];
                     }

                     if (this.needSearchImageInArr && this.isFirstLoading) {
                        this.isFirstLoading = false;
                        this.loadingMoreImages = true;
                        DataQuery.getInstance(this.currentAccount).loadMedia(this.currentDialogId, 80, 0, this.sharedMediaType, 1, this.classGuid);
                     } else if (!this.imagesArr.isEmpty()) {
                        if (this.opennedFromMedia) {
                           this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.currentIndex + 1, this.totalImagesCount + this.totalImagesCountMerge));
                        } else {
                           this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.totalImagesCount + this.totalImagesCountMerge - this.imagesArr.size() + this.currentIndex + 1, this.totalImagesCount + this.totalImagesCountMerge));
                        }
                     }
                  }
               } else if (var1 != NotificationCenter.mediaDidLoad) {
                  if (var1 == NotificationCenter.emojiDidLoad) {
                     TextView var25 = this.captionTextView;
                     if (var25 != null) {
                        var25.invalidate();
                     }
                  } else if (var1 == NotificationCenter.filePreparingFailed) {
                     var22 = (MessageObject)var3[0];
                     if (this.loadInitialVideo) {
                        this.loadInitialVideo = false;
                        this.progressView.setVisibility(4);
                        this.preparePlayer(this.currentPlayingVideoFile, false, false);
                     } else if (this.tryStartRequestPreviewOnFinish) {
                        this.releasePlayer(false);
                        this.tryStartRequestPreviewOnFinish = MediaController.getInstance().scheduleVideoConvert(this.videoPreviewMessageObject, true) ^ true;
                     } else if (var22 == this.videoPreviewMessageObject) {
                        this.requestingPreview = false;
                        this.progressView.setVisibility(4);
                     }
                  } else if (var1 == NotificationCenter.fileNewChunkAvailable && (MessageObject)var3[0] == this.videoPreviewMessageObject) {
                     var27 = (String)var3[1];
                     if ((Long)var3[3] != 0L) {
                        this.requestingPreview = false;
                        this.progressView.setVisibility(4);
                        this.preparePlayer(Uri.fromFile(new File(var27)), false, true);
                     }
                  }
               } else {
                  var9 = (Long)var3[0];
                  var1 = (Integer)var3[3];
                  if ((var9 == this.currentDialogId || var9 == this.mergeDialogId) && var1 == this.classGuid) {
                     this.loadingMoreImages = false;
                     if (var9 == this.currentDialogId) {
                        var21 = 0;
                     } else {
                        var21 = 1;
                     }

                     var5 = (ArrayList)var3[2];
                     this.endReached[var21] = (Boolean)var3[5];
                     if (!this.needSearchImageInArr) {
                        Iterator var26 = var5.iterator();
                        var1 = 0;

                        while(var26.hasNext()) {
                           var22 = (MessageObject)var26.next();
                           if (this.imagesByIds[var21].indexOfKey(var22.getId()) < 0) {
                              ++var1;
                              if (this.opennedFromMedia) {
                                 this.imagesArr.add(var22);
                              } else {
                                 this.imagesArr.add(0, var22);
                              }

                              this.imagesByIds[var21].put(var22.getId(), var22);
                           }
                        }

                        if (this.opennedFromMedia) {
                           if (var1 == 0) {
                              this.totalImagesCount = this.imagesArr.size();
                              this.totalImagesCountMerge = 0;
                           }
                        } else if (var1 != 0) {
                           var2 = this.currentIndex;
                           this.currentIndex = -1;
                           this.setImageIndex(var2 + var1, true);
                        } else {
                           this.totalImagesCount = this.imagesArr.size();
                           this.totalImagesCountMerge = 0;
                        }
                     } else {
                        if (var5.isEmpty() && (var21 != 0 || this.mergeDialogId == 0L)) {
                           this.needSearchImageInArr = false;
                           return;
                        }

                        MessageObject var7 = (MessageObject)this.imagesArr.get(this.currentIndex);
                        int var19 = 0;
                        var17 = 0;

                        int var20;
                        for(var1 = -1; var19 < var5.size(); var1 = var20) {
                           var22 = (MessageObject)var5.get(var19);
                           var4 = var17;
                           var20 = var1;
                           if (this.imagesByIdsTemp[var21].indexOfKey(var22.getId()) < 0) {
                              this.imagesByIdsTemp[var21].put(var22.getId(), var22);
                              if (this.opennedFromMedia) {
                                 this.imagesArrTemp.add(var22);
                                 if (var22.getId() == var7.getId()) {
                                    var1 = var17;
                                 }

                                 var4 = var17 + 1;
                                 var20 = var1;
                              } else {
                                 ++var17;
                                 this.imagesArrTemp.add(0, var22);
                                 var4 = var17;
                                 var20 = var1;
                                 if (var22.getId() == var7.getId()) {
                                    var20 = var5.size() - var17;
                                    var4 = var17;
                                 }
                              }
                           }

                           ++var19;
                           var17 = var4;
                        }

                        if (var17 == 0 && (var21 != 0 || this.mergeDialogId == 0L)) {
                           this.totalImagesCount = this.imagesArr.size();
                           this.totalImagesCountMerge = 0;
                        }

                        if (var1 != -1) {
                           this.imagesArr.clear();
                           this.imagesArr.addAll(this.imagesArrTemp);

                           for(var2 = 0; var2 < 2; ++var2) {
                              this.imagesByIds[var2] = this.imagesByIdsTemp[var2].clone();
                              this.imagesByIdsTemp[var2].clear();
                           }

                           this.imagesArrTemp.clear();
                           this.needSearchImageInArr = false;
                           this.currentIndex = -1;
                           var2 = var1;
                           if (var1 >= this.imagesArr.size()) {
                              var2 = this.imagesArr.size() - 1;
                           }

                           this.setImageIndex(var2, true);
                        } else {
                           label336: {
                              label335: {
                                 label334: {
                                    if (this.opennedFromMedia) {
                                       ArrayList var23;
                                       if (this.imagesArrTemp.isEmpty()) {
                                          var1 = 0;
                                       } else {
                                          var23 = this.imagesArrTemp;
                                          var1 = ((MessageObject)var23.get(var23.size() - 1)).getId();
                                       }

                                       var4 = var1;
                                       if (var21 == 0) {
                                          var4 = var1;
                                          if (this.endReached[var21]) {
                                             var4 = var1;
                                             if (this.mergeDialogId != 0L) {
                                                var2 = var1;
                                                if (!this.imagesArrTemp.isEmpty()) {
                                                   var23 = this.imagesArrTemp;
                                                   var2 = var1;
                                                   if (((MessageObject)var23.get(var23.size() - 1)).getDialogId() != this.mergeDialogId) {
                                                      break label335;
                                                   }
                                                }
                                                break label334;
                                             }
                                          }
                                       }
                                    } else {
                                       if (this.imagesArrTemp.isEmpty()) {
                                          var1 = 0;
                                       } else {
                                          var1 = ((MessageObject)this.imagesArrTemp.get(0)).getId();
                                       }

                                       var4 = var1;
                                       if (var21 == 0) {
                                          var4 = var1;
                                          if (this.endReached[var21]) {
                                             var4 = var1;
                                             if (this.mergeDialogId != 0L) {
                                                var2 = var1;
                                                if (!this.imagesArrTemp.isEmpty()) {
                                                   var2 = var1;
                                                   if (((MessageObject)this.imagesArrTemp.get(0)).getDialogId() != this.mergeDialogId) {
                                                      break label335;
                                                   }
                                                }
                                                break label334;
                                             }
                                          }
                                       }
                                    }

                                    var1 = var4;
                                    break label336;
                                 }

                                 var1 = var2;
                                 var21 = 1;
                                 break label336;
                              }

                              var21 = 1;
                              var1 = 0;
                           }

                           if (!this.endReached[var21]) {
                              this.loadingMoreImages = true;
                              DataQuery var24;
                              if (this.opennedFromMedia) {
                                 var24 = DataQuery.getInstance(this.currentAccount);
                                 if (var21 == 0) {
                                    var9 = this.currentDialogId;
                                 } else {
                                    var9 = this.mergeDialogId;
                                 }

                                 var24.loadMedia(var9, 80, var1, this.sharedMediaType, 1, this.classGuid);
                              } else {
                                 var24 = DataQuery.getInstance(this.currentAccount);
                                 if (var21 == 0) {
                                    var9 = this.currentDialogId;
                                 } else {
                                    var9 = this.mergeDialogId;
                                 }

                                 var24.loadMedia(var9, 80, var1, this.sharedMediaType, 1, this.classGuid);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public void exitFromPip() {
      if (this.isInline) {
         if (Instance != null) {
            Instance.closePhoto(false, true);
         }

         Instance = PipInstance;
         PipInstance = null;
         this.switchingInlineMode = true;
         Bitmap var1 = this.currentBitmap;
         if (var1 != null) {
            var1.recycle();
            this.currentBitmap = null;
         }

         this.changingTextureView = true;
         this.isInline = false;
         this.videoTextureView.setVisibility(4);
         this.aspectRatioFrameLayout.addView(this.videoTextureView);
         if (ApplicationLoader.mainInterfacePaused) {
            try {
               Activity var2 = this.parentActivity;
               Intent var6 = new Intent(ApplicationLoader.applicationContext, BringAppForegroundService.class);
               var2.startService(var6);
            } catch (Throwable var5) {
               FileLog.e(var5);
            }
         }

         if (VERSION.SDK_INT >= 21) {
            this.pipAnimationInProgress = true;
            Rect var7 = PipVideoView.getPipRect(this.aspectRatioFrameLayout.getAspectRatio());
            float var3 = var7.width / (float)this.textureImageView.getLayoutParams().width;
            var7.y += (float)AndroidUtilities.statusBarHeight;
            this.textureImageView.setScaleX(var3);
            this.textureImageView.setScaleY(var3);
            this.textureImageView.setTranslationX(var7.x);
            this.textureImageView.setTranslationY(var7.y);
            this.videoTextureView.setScaleX(var3);
            this.videoTextureView.setScaleY(var3);
            this.videoTextureView.setTranslationX(var7.x - this.aspectRatioFrameLayout.getX());
            this.videoTextureView.setTranslationY(var7.y - this.aspectRatioFrameLayout.getY());
         } else {
            this.pipVideoView.close();
            this.pipVideoView = null;
         }

         try {
            this.isVisible = true;
            ((WindowManager)this.parentActivity.getSystemService("window")).addView(this.windowView, this.windowLayoutParams);
            if (this.currentPlaceObject != null) {
               this.currentPlaceObject.imageReceiver.setVisible(false, false);
            }
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

         if (VERSION.SDK_INT >= 21) {
            this.waitingForDraw = 4;
         }

      }
   }

   @Keep
   public float getAnimationValue() {
      return this.animationValue;
   }

   public int getSelectiongLength() {
      PhotoViewerCaptionEnterView var1 = this.captionEditText;
      int var2;
      if (var1 != null) {
         var2 = var1.getSelectionLength();
      } else {
         var2 = 0;
      }

      return var2;
   }

   public VideoPlayer getVideoPlayer() {
      return this.videoPlayer;
   }

   public void injectVideoPlayer(VideoPlayer var1) {
      this.injectingVideoPlayer = var1;
   }

   public void injectVideoPlayerSurface(SurfaceTexture var1) {
      this.injectingVideoPlayerSurface = var1;
   }

   public void injectVideoPlayerToMediaController() {
      if (this.videoPlayer.isPlaying()) {
         MediaController.getInstance().injectVideoPlayer(this.videoPlayer, this.currentMessageObject);
         this.videoPlayer = null;
         this.updateAccessibilityOverlayVisibility();
      }

   }

   public boolean isInjectingVideoPlayer() {
      boolean var1;
      if (this.injectingVideoPlayer != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isMuteVideo() {
      return this.muteVideo;
   }

   public boolean isOpenedFullScreenVideo() {
      return this.openedFullScreenVideo;
   }

   public boolean isVisible() {
      boolean var1;
      if (this.isVisible && this.placeProvider != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   // $FF: synthetic method
   public void lambda$checkInlinePermissions$29$PhotoViewer(DialogInterface var1, int var2) {
      Activity var3 = this.parentActivity;
      if (var3 != null) {
         try {
            StringBuilder var4 = new StringBuilder();
            var4.append("package:");
            var4.append(this.parentActivity.getPackageName());
            Intent var6 = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse(var4.toString()));
            var3.startActivity(var6);
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$closePhoto$41$PhotoViewer() {
      this.switchToEditMode(0);
   }

   // $FF: synthetic method
   public void lambda$closePhoto$42$PhotoViewer(PhotoViewer.PlaceProviderObject var1) {
      if (VERSION.SDK_INT >= 18) {
         this.containerView.setLayerType(0, (Paint)null);
      }

      this.animationInProgress = 0;
      this.onPhotoClosed(var1);
   }

   // $FF: synthetic method
   public void lambda$closePhoto$43$PhotoViewer(PhotoViewer.PlaceProviderObject var1) {
      PhotoViewer.FrameLayoutDrawer var2 = this.containerView;
      if (var2 != null) {
         if (VERSION.SDK_INT >= 18) {
            var2.setLayerType(0, (Paint)null);
         }

         this.animationInProgress = 0;
         this.onPhotoClosed(var1);
         this.containerView.setScaleX(1.0F);
         this.containerView.setScaleY(1.0F);
      }
   }

   // $FF: synthetic method
   public void lambda$createCaptionTextView$30$PhotoViewer(View var1) {
      if (this.needCaptionLayout) {
         this.openCaptionEnter();
      }
   }

   // $FF: synthetic method
   public void lambda$createCropView$34$PhotoViewer(boolean var1) {
      TextView var2 = this.resetButton;
      byte var3;
      if (var1) {
         var3 = 8;
      } else {
         var3 = 0;
      }

      var2.setVisibility(var3);
   }

   // $FF: synthetic method
   public void lambda$createVideoControlsInterface$31$PhotoViewer(float var1) {
      if (this.videoPlayer != null) {
         float var2 = var1;
         if (!this.inPreview) {
            var2 = var1;
            if (this.videoTimelineView.getVisibility() == 0) {
               var2 = this.videoTimelineView.getLeftProgress() + (this.videoTimelineView.getRightProgress() - this.videoTimelineView.getLeftProgress()) * var1;
            }
         }

         long var3 = this.videoPlayer.getDuration();
         if (var3 == -9223372036854775807L) {
            this.seekToProgressPending = var2;
         } else {
            this.videoPlayer.seekTo((long)((int)(var2 * (float)var3)));
         }
      }

   }

   // $FF: synthetic method
   public void lambda$createVideoControlsInterface$32$PhotoViewer(View var1) {
      VideoPlayer var2 = this.videoPlayer;
      if (var2 != null) {
         if (this.isPlaying) {
            var2.pause();
         } else {
            if (this.isCurrentVideo) {
               if (Math.abs(this.videoTimelineView.getProgress() - 1.0F) < 0.01F || this.videoPlayer.getCurrentPosition() == this.videoPlayer.getDuration()) {
                  this.videoPlayer.seekTo(0L);
               }
            } else if (Math.abs(this.videoPlayerSeekbar.getProgress() - 1.0F) < 0.01F || this.videoPlayer.getCurrentPosition() == this.videoPlayer.getDuration()) {
               this.videoPlayer.seekTo(0L);
            }

            this.videoPlayer.play();
         }

         this.containerView.invalidate();
      }
   }

   // $FF: synthetic method
   public void lambda$new$0$PhotoViewer() {
      this.toggleMiniProgressInternal(true);
   }

   // $FF: synthetic method
   public void lambda$null$16$PhotoViewer(NumberPicker var1, BottomSheet var2, View var3) {
      int var4 = var1.getValue();
      Editor var5 = MessagesController.getGlobalMainSettings().edit();
      var5.putInt("self_destruct", var4);
      var5.commit();
      var2.dismiss();
      if (var4 < 0 || var4 >= 21) {
         var4 = (var4 - 16) * 5;
      }

      Object var6 = this.imagesArrLocals.get(this.currentIndex);
      if (var6 instanceof MediaController.PhotoEntry) {
         ((MediaController.PhotoEntry)var6).ttl = var4;
      } else if (var6 instanceof MediaController.SearchImage) {
         ((MediaController.SearchImage)var6).ttl = var4;
      }

      ImageView var8 = this.timeItem;
      PorterDuffColorFilter var7;
      if (var4 != 0) {
         var7 = new PorterDuffColorFilter(-12734994, Mode.MULTIPLY);
      } else {
         var7 = null;
      }

      var8.setColorFilter(var7);
      if (!this.checkImageView.isChecked()) {
         this.checkImageView.callOnClick();
      }

   }

   // $FF: synthetic method
   public void lambda$null$27$PhotoViewer(DialogInterface var1, int var2) {
      this.mentionsAdapter.clearRecentHashtags();
   }

   // $FF: synthetic method
   public void lambda$null$36$PhotoViewer(DialogInterface var1, int var2) {
      this.switchToEditMode(0);
   }

   // $FF: synthetic method
   public void lambda$null$39$PhotoViewer() {
      this.switchToEditMode(0);
   }

   // $FF: synthetic method
   public void lambda$onDraw$46$PhotoViewer() {
      this.setImageIndex(this.currentIndex + 1, false);
   }

   // $FF: synthetic method
   public void lambda$onDraw$47$PhotoViewer() {
      this.setImageIndex(this.currentIndex - 1, false);
   }

   // $FF: synthetic method
   public void lambda$onPhotoClosed$44$PhotoViewer() {
      this.animatingImageView.setImageBitmap((ImageReceiver.BitmapHolder)null);

      try {
         if (this.windowView.getParent() != null) {
            ((WindowManager)this.parentActivity.getSystemService("window")).removeView(this.windowView);
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   // $FF: synthetic method
   public void lambda$redraw$45$PhotoViewer(int var1) {
      this.redraw(var1 + 1);
   }

   // $FF: synthetic method
   public WindowInsets lambda$setParentActivity$1$PhotoViewer(View var1, WindowInsets var2) {
      WindowInsets var3 = (WindowInsets)this.lastInsets;
      this.lastInsets = var2;
      if (var3 == null || !var3.toString().equals(var2.toString())) {
         if (this.animationInProgress == 1) {
            ClippingImageView var4 = this.animatingImageView;
            var4.setTranslationX(var4.getTranslationX() - (float)this.getLeftInset());
            this.animationValues[0][2] = this.animatingImageView.getTranslationX();
         }

         this.windowView.requestLayout();
      }

      this.containerView.setPadding(var2.getSystemWindowInsetLeft(), 0, var2.getSystemWindowInsetRight(), 0);
      return var2.consumeSystemWindowInsets();
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$10$PhotoViewer(View var1) {
      if (this.captionEditText.getTag() == null) {
         this.muteVideo ^= true;
         this.updateMuteButton();
         this.updateVideoInfo();
         if (this.muteVideo && !this.checkImageView.isChecked()) {
            this.checkImageView.callOnClick();
         } else {
            Object var2 = this.imagesArrLocals.get(this.currentIndex);
            if (var2 instanceof MediaController.PhotoEntry) {
               ((MediaController.PhotoEntry)var2).editedInfo = this.getCurrentVideoEditedInfo();
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$11$PhotoViewer(View var1) {
      if (this.placeProvider != null && this.captionEditText.getTag() == null) {
         this.placeProvider.needAddMorePhotos();
         this.closePhoto(true, false);
      }

   }

   // $FF: synthetic method
   public void lambda$setParentActivity$12$PhotoViewer(View var1) {
      if (this.captionEditText.getTag() == null) {
         this.switchToEditMode(2);
      }
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$18$PhotoViewer(View var1) {
      if (this.parentActivity != null && this.captionEditText.getTag() == null) {
         BottomSheet.Builder var2 = new BottomSheet.Builder(this.parentActivity);
         var2.setUseHardwareLayer(false);
         LinearLayout var3 = new LinearLayout(this.parentActivity);
         var3.setOrientation(1);
         var2.setCustomView(var3);
         TextView var6 = new TextView(this.parentActivity);
         var6.setLines(1);
         var6.setSingleLine(true);
         var6.setText(LocaleController.getString("MessageLifetime", 2131559846));
         var6.setTextColor(-1);
         var6.setTextSize(1, 16.0F);
         var6.setEllipsize(TruncateAt.MIDDLE);
         var6.setPadding(AndroidUtilities.dp(21.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(21.0F), AndroidUtilities.dp(4.0F));
         var6.setGravity(16);
         var3.addView(var6, LayoutHelper.createFrame(-1, -2.0F));
         var6.setOnTouchListener(_$$Lambda$PhotoViewer$Fa9LvP0G4vIQvImlUbz8lm7_OVc.INSTANCE);
         TextView var4 = new TextView(this.parentActivity);
         int var5;
         String var7;
         if (this.isCurrentVideo) {
            var5 = 2131559851;
            var7 = "MessageLifetimeVideo";
         } else {
            var5 = 2131559849;
            var7 = "MessageLifetimePhoto";
         }

         var4.setText(LocaleController.getString(var7, var5));
         var4.setTextColor(-8355712);
         var4.setTextSize(1, 14.0F);
         var4.setEllipsize(TruncateAt.MIDDLE);
         var4.setPadding(AndroidUtilities.dp(21.0F), 0, AndroidUtilities.dp(21.0F), AndroidUtilities.dp(8.0F));
         var4.setGravity(16);
         var3.addView(var4, LayoutHelper.createFrame(-1, -2.0F));
         var4.setOnTouchListener(_$$Lambda$PhotoViewer$yXvdI0LzaOnMgp9nlpi13Yy9i9g.INSTANCE);
         BottomSheet var9 = var2.create();
         NumberPicker var12 = new NumberPicker(this.parentActivity);
         var12.setMinValue(0);
         var12.setMaxValue(28);
         Object var8 = this.imagesArrLocals.get(this.currentIndex);
         if (var8 instanceof MediaController.PhotoEntry) {
            var5 = ((MediaController.PhotoEntry)var8).ttl;
         } else if (var8 instanceof MediaController.SearchImage) {
            var5 = ((MediaController.SearchImage)var8).ttl;
         } else {
            var5 = 0;
         }

         if (var5 == 0) {
            var12.setValue(MessagesController.getGlobalMainSettings().getInt("self_destruct", 7));
         } else if (var5 >= 0 && var5 < 21) {
            var12.setValue(var5);
         } else {
            var12.setValue(var5 / 5 + 21 - 5);
         }

         var12.setTextColor(-1);
         var12.setSelectorColor(-11711155);
         var12.setFormatter(_$$Lambda$PhotoViewer$7xRrdNHIBWSRxpUDkiQj1xCx9mM.INSTANCE);
         var3.addView(var12, LayoutHelper.createLinear(-1, -2));
         PhotoViewer$13 var10 = new PhotoViewer$13(this, this.parentActivity);
         var10.setPadding(AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F));
         var3.addView(var10, LayoutHelper.createLinear(-1, 52));
         TextView var11 = new TextView(this.parentActivity);
         var11.setMinWidth(AndroidUtilities.dp(64.0F));
         var11.setTag(-1);
         var11.setTextSize(1, 14.0F);
         var11.setTextColor(-11944718);
         var11.setGravity(17);
         var11.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         var11.setText(LocaleController.getString("Done", 2131559299).toUpperCase());
         var11.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(-11944718));
         var11.setPadding(AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp(10.0F), 0);
         var10.addView(var11, LayoutHelper.createFrame(-2, 36, 53));
         var11.setOnClickListener(new _$$Lambda$PhotoViewer$26GEXmzp746KJTav_ZQPy3lxX3s(this, var12, var9));
         var11 = new TextView(this.parentActivity);
         var11.setMinWidth(AndroidUtilities.dp(64.0F));
         var11.setTag(-2);
         var11.setTextSize(1, 14.0F);
         var11.setTextColor(-11944718);
         var11.setGravity(17);
         var11.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         var11.setText(LocaleController.getString("Cancel", 2131558891).toUpperCase());
         var11.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(-11944718));
         var11.setPadding(AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp(10.0F), 0);
         var10.addView(var11, LayoutHelper.createFrame(-2, 36, 53));
         var11.setOnClickListener(new _$$Lambda$PhotoViewer$b3BwHxjBmMPlPrpxL3EOAigJ9HU(var9));
         var9.show();
         var9.setBackgroundColor(-16777216);
      }

   }

   // $FF: synthetic method
   public void lambda$setParentActivity$19$PhotoViewer(View var1) {
      this.switchToEditMode(0);
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$2$PhotoViewer(View var1) {
      this.onSharePressed();
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$20$PhotoViewer(View var1) {
      if (this.currentEditMode != 1 || this.photoCropView.isReady()) {
         this.applyCurrentEditMode();
         this.switchToEditMode(0);
      }
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$21$PhotoViewer(View var1) {
      this.photoCropView.reset();
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$22$PhotoViewer(ImageReceiver var1, boolean var2, boolean var3) {
      if (var1 == this.centerImage && var2 && !var3 && (this.currentEditMode == 1 || this.sendPhotoType == 1) && this.photoCropView != null) {
         Bitmap var4 = var1.getBitmap();
         if (var4 != null) {
            PhotoCropView var5 = this.photoCropView;
            int var6 = var1.getOrientation();
            if (this.sendPhotoType != 1) {
               var3 = true;
            } else {
               var3 = false;
            }

            var5.setBitmap(var4, var6, var3, true);
         }
      }

      if (var1 == this.centerImage && var2) {
         PhotoViewer.PhotoViewerProvider var7 = this.placeProvider;
         if (var7 != null && var7.scaleToFill() && !this.ignoreDidSetImage) {
            if (!this.wasLayout) {
               this.dontResetZoomOnFirstLayout = true;
            } else {
               this.setScaleToFill();
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$setParentActivity$23$PhotoViewer(View var1) {
      if (this.captionEditText.getTag() == null) {
         this.setPhotoChecked();
      }
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$24$PhotoViewer(View var1) {
      if (this.captionEditText.getTag() == null) {
         PhotoViewer.PhotoViewerProvider var2 = this.placeProvider;
         if (var2 != null && var2.getSelectedPhotosOrder() != null && !this.placeProvider.getSelectedPhotosOrder().isEmpty()) {
            this.togglePhotosListView(this.isPhotosListViewVisible ^ true, true);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$setParentActivity$25$PhotoViewer(View var1, int var2) {
      if (var2 == 0 && this.placeProvider.allowGroupPhotos()) {
         boolean var3 = SharedConfig.groupPhotosEnabled;
         SharedConfig.toggleGroupPhotosEnabled();
         this.placeProvider.toggleGroupPhotosEnabled();
         ImageView var4 = (ImageView)var1;
         PorterDuffColorFilter var5;
         if (!var3) {
            var5 = new PorterDuffColorFilter(-10043398, Mode.MULTIPLY);
         } else {
            var5 = null;
         }

         var4.setColorFilter(var5);
         String var6;
         if (SharedConfig.groupPhotosEnabled) {
            var2 = 2131559612;
            var6 = "GroupPhotosHelp";
         } else {
            var2 = 2131560786;
            var6 = "SinglePhotosHelp";
         }

         var4.setContentDescription(LocaleController.getString(var6, var2));
         this.showHint(false, var3 ^ true);
      } else {
         this.ignoreDidSetImage = true;
         var2 = this.imagesArrLocals.indexOf(var1.getTag());
         if (var2 >= 0) {
            this.currentIndex = -1;
            this.setImageIndex(var2, true);
         }

         this.ignoreDidSetImage = false;
      }

   }

   // $FF: synthetic method
   public void lambda$setParentActivity$26$PhotoViewer(View var1, int var2) {
      Object var6 = this.mentionsAdapter.getItem(var2);
      int var3 = this.mentionsAdapter.getResultStartPosition();
      var2 = this.mentionsAdapter.getResultLength();
      StringBuilder var10;
      if (var6 instanceof TLRPC.User) {
         TLRPC.User var7 = (TLRPC.User)var6;
         StringBuilder var5;
         if (var7.username != null) {
            PhotoViewerCaptionEnterView var4 = this.captionEditText;
            var5 = new StringBuilder();
            var5.append("@");
            var5.append(var7.username);
            var5.append(" ");
            var4.replaceWithText(var3, var2, var5.toString(), false);
         } else {
            String var9 = UserObject.getFirstName(var7);
            var5 = new StringBuilder();
            var5.append(var9);
            var5.append(" ");
            SpannableString var11 = new SpannableString(var5.toString());
            var10 = new StringBuilder();
            var10.append("");
            var10.append(var7.id);
            var11.setSpan(new URLSpanUserMentionPhotoViewer(var10.toString(), true), 0, var11.length(), 33);
            this.captionEditText.replaceWithText(var3, var2, var11, false);
         }
      } else if (var6 instanceof String) {
         PhotoViewerCaptionEnterView var12 = this.captionEditText;
         var10 = new StringBuilder();
         var10.append(var6);
         var10.append(" ");
         var12.replaceWithText(var3, var2, var10.toString(), false);
      } else if (var6 instanceof DataQuery.KeywordResult) {
         String var8 = ((DataQuery.KeywordResult)var6).emoji;
         this.captionEditText.addEmojiToRecent(var8);
         this.captionEditText.replaceWithText(var3, var2, var8, true);
      }

   }

   // $FF: synthetic method
   public boolean lambda$setParentActivity$28$PhotoViewer(View var1, int var2) {
      if (this.mentionsAdapter.getItem(var2) instanceof String) {
         AlertDialog.Builder var3 = new AlertDialog.Builder(this.parentActivity);
         var3.setTitle(LocaleController.getString("AppName", 2131558635));
         var3.setMessage(LocaleController.getString("ClearSearch", 2131559114));
         var3.setPositiveButton(LocaleController.getString("ClearButton", 2131559102).toUpperCase(), new _$$Lambda$PhotoViewer$gsWZpdB3_4pFKnDWFxWdNysXyg0(this));
         var3.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showAlertDialog(var3);
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$3$PhotoViewer(View var1) {
      this.selectedCompression = this.previousCompression;
      this.didChangedCompressionLevel(false);
      this.showQualityView(false);
      this.requestVideoPreview(2);
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$4$PhotoViewer(View var1) {
      this.showQualityView(false);
      this.requestVideoPreview(2);
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$5$PhotoViewer(View var1) {
      if (this.captionEditText.getTag() == null) {
         if (this.sendPhotoType == 1) {
            this.applyCurrentEditMode();
         }

         if (this.placeProvider != null && !this.doneButtonPressed) {
            VideoEditedInfo var2 = this.getCurrentVideoEditedInfo();
            this.placeProvider.sendButtonPressed(this.currentIndex, var2);
            this.doneButtonPressed = true;
            this.closePhoto(false, false);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$6$PhotoViewer(View var1) {
      if (this.captionEditText.getTag() == null) {
         this.switchToEditMode(1);
      }
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$7$PhotoViewer(View var1) {
      PhotoCropView var2 = this.photoCropView;
      if (var2 != null) {
         var2.rotate();
      }
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$8$PhotoViewer(View var1) {
      if (this.captionEditText.getTag() == null) {
         this.switchToEditMode(3);
      }
   }

   // $FF: synthetic method
   public void lambda$setParentActivity$9$PhotoViewer(View var1) {
      if (this.captionEditText.getTag() == null) {
         this.showQualityView(true);
         this.requestVideoPreview(1);
      }
   }

   // $FF: synthetic method
   public void lambda$showAlertDialog$33$PhotoViewer(DialogInterface var1) {
      this.visibleDialog = null;
   }

   // $FF: synthetic method
   public void lambda$switchToEditMode$35$PhotoViewer(View var1) {
      this.applyCurrentEditMode();
      this.switchToEditMode(0);
   }

   // $FF: synthetic method
   public void lambda$switchToEditMode$37$PhotoViewer(View var1) {
      if (this.photoFilterView.hasChanges()) {
         Activity var2 = this.parentActivity;
         if (var2 == null) {
            return;
         }

         AlertDialog.Builder var3 = new AlertDialog.Builder(var2);
         var3.setMessage(LocaleController.getString("DiscardChanges", 2131559273));
         var3.setTitle(LocaleController.getString("AppName", 2131558635));
         var3.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PhotoViewer$FGSVMb30vu6rC6W9VRlBHbytWQc(this));
         var3.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showAlertDialog(var3);
      } else {
         this.switchToEditMode(0);
      }

   }

   // $FF: synthetic method
   public void lambda$switchToEditMode$38$PhotoViewer(View var1) {
      this.applyCurrentEditMode();
      this.switchToEditMode(0);
   }

   // $FF: synthetic method
   public void lambda$switchToEditMode$40$PhotoViewer(View var1) {
      this.photoPaintView.maybeShowDismissalAlert(this, this.parentActivity, new _$$Lambda$PhotoViewer$NZvIqbihVu7AhVHkylD9XeLLrzY(this));
   }

   public void onConfigurationChanged(Configuration var1) {
      PipVideoView var2 = this.pipVideoView;
      if (var2 != null) {
         var2.onConfigurationChanged();
      }

   }

   public boolean onDoubleTap(MotionEvent var1) {
      VideoPlayer var2 = this.videoPlayer;
      boolean var3 = false;
      long var4 = 0L;
      float var11;
      if (var2 != null && this.videoPlayerControlFrameLayout.getVisibility() == 0) {
         long var6 = this.videoPlayer.getCurrentPosition();
         long var8 = this.videoPlayer.getDuration();
         if (var8 >= 0L && var6 >= 0L && var8 != -9223372036854775807L && var6 != -9223372036854775807L) {
            int var10 = this.getContainerViewWidth();
            var11 = var1.getX();
            var10 /= 3;
            long var12;
            if (var11 >= (float)(var10 * 2)) {
               var12 = 10000L + var6;
            } else if (var11 < (float)var10) {
               var12 = var6 - 10000L;
            } else {
               var12 = var6;
            }

            if (var6 != var12) {
               if (var12 > var8) {
                  var12 = var8;
               } else if (var12 < 0L) {
                  var12 = var4;
               }

               VideoForwardDrawable var16 = this.videoForwardDrawable;
               if (var11 < (float)var10) {
                  var3 = true;
               }

               var16.setLeftSide(var3);
               this.videoPlayer.seekTo(var12);
               this.containerView.invalidate();
               this.videoPlayerSeekbar.setProgress((float)var12 / (float)var8);
               this.videoPlayerControlFrameLayout.invalidate();
               return true;
            }
         }
      }

      if (this.canZoom && (this.scale != 1.0F || this.translationY == 0.0F && this.translationX == 0.0F) && this.animationStartTime == 0L && this.animationInProgress == 0) {
         if (this.scale == 1.0F) {
            var11 = var1.getX() - (float)(this.getContainerViewWidth() / 2) - (var1.getX() - (float)(this.getContainerViewWidth() / 2) - this.translationX) * (3.0F / this.scale);
            float var14 = var1.getY() - (float)(this.getContainerViewHeight() / 2) - (var1.getY() - (float)(this.getContainerViewHeight() / 2) - this.translationY) * (3.0F / this.scale);
            this.updateMinMax(3.0F);
            float var15 = this.minX;
            if (var11 < var15) {
               var11 = var15;
            } else {
               var15 = this.maxX;
               if (var11 > var15) {
                  var11 = var15;
               }
            }

            var15 = this.minY;
            if (var14 < var15) {
               var14 = var15;
            } else {
               var15 = this.maxY;
               if (var14 > var15) {
                  var14 = var15;
               }
            }

            this.animateTo(3.0F, var11, var14, true);
         } else {
            this.animateTo(1.0F, 0.0F, 0.0F, true);
         }

         this.doubleTap = true;
         return true;
      } else {
         return false;
      }
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
         this.containerView.postInvalidate();
      }

      return false;
   }

   public void onLongPress(MotionEvent var1) {
   }

   public void onPause() {
      if (this.currentAnimation != null) {
         this.closePhoto(false, false);
      } else {
         if (this.lastTitle != null) {
            this.closeCaptionEnter(true);
         }

      }
   }

   public void onResume() {
      this.redraw(0);
      VideoPlayer var1 = this.videoPlayer;
      if (var1 != null) {
         var1.seekTo(var1.getCurrentPosition() + 1L);
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
         float var4;
         float var5;
         int var9;
         if (this.containerView.getTag() != null) {
            AspectRatioFrameLayout var2 = this.aspectRatioFrameLayout;
            boolean var3;
            if (var2 != null && var2.getVisibility() == 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            label85: {
               var4 = var1.getX();
               var5 = var1.getY();
               if (this.sharedMediaType == 1) {
                  MessageObject var6 = this.currentMessageObject;
                  if (var6 != null) {
                     if (!var6.canPreviewDocument()) {
                        var4 = (float)(this.getContainerViewHeight() - AndroidUtilities.dp(360.0F)) / 2.0F;
                        if (var5 >= var4 && var5 <= var4 + (float)AndroidUtilities.dp(360.0F)) {
                           this.onActionClick(true);
                           return true;
                        }
                     }
                     break label85;
                  }
               }

               PhotoViewer.PhotoProgressView[] var7 = this.photoProgressViews;
               if (var7[0] != null && this.containerView != null && !var3) {
                  var9 = var7[0].backgroundState;
                  if (var9 > 0 && var9 <= 3 && var4 >= (float)(this.getContainerViewWidth() - AndroidUtilities.dp(100.0F)) / 2.0F && var4 <= (float)(this.getContainerViewWidth() + AndroidUtilities.dp(100.0F)) / 2.0F && var5 >= (float)(this.getContainerViewHeight() - AndroidUtilities.dp(100.0F)) / 2.0F && var5 <= (float)(this.getContainerViewHeight() + AndroidUtilities.dp(100.0F)) / 2.0F) {
                     this.onActionClick(true);
                     this.checkProgress(0, true);
                     return true;
                  }
               }
            }

            this.toggleActionBar(this.isActionBarVisible ^ true, true);
         } else {
            var9 = this.sendPhotoType;
            if (var9 != 0 && var9 != 4) {
               TLRPC.BotInlineResult var8 = this.currentBotInlineResult;
               if (var8 != null && (var8.type.equals("video") || MessageObject.isVideoDocument(this.currentBotInlineResult.document))) {
                  var9 = this.photoProgressViews[0].backgroundState;
                  if (var9 > 0 && var9 <= 3) {
                     var4 = var1.getX();
                     var5 = var1.getY();
                     if (var4 >= (float)(this.getContainerViewWidth() - AndroidUtilities.dp(100.0F)) / 2.0F && var4 <= (float)(this.getContainerViewWidth() + AndroidUtilities.dp(100.0F)) / 2.0F && var5 >= (float)(this.getContainerViewHeight() - AndroidUtilities.dp(100.0F)) / 2.0F && var5 <= (float)(this.getContainerViewHeight() + AndroidUtilities.dp(100.0F)) / 2.0F) {
                        this.onActionClick(true);
                        this.checkProgress(0, true);
                        return true;
                     }
                  }
               } else if (this.sendPhotoType == 2 && this.isCurrentVideo) {
                  this.videoPlayButton.callOnClick();
               }
            } else if (this.isCurrentVideo) {
               this.videoPlayButton.callOnClick();
            } else {
               this.checkImageView.performClick();
            }
         }

         return true;
      }
   }

   public boolean onSingleTapUp(MotionEvent var1) {
      return !this.canZoom && !this.doubleTapEnabled ? this.onSingleTapConfirmed(var1) : false;
   }

   public boolean openPhoto(ArrayList var1, int var2, long var3, long var5, PhotoViewer.PhotoViewerProvider var7) {
      return this.openPhoto((MessageObject)var1.get(var2), (TLRPC.FileLocation)null, var1, (ArrayList)null, (ArrayList)null, var2, var7, (ChatActivity)null, var3, var5, true);
   }

   public boolean openPhoto(ArrayList var1, int var2, PhotoViewer.PhotoViewerProvider var3) {
      return this.openPhoto((MessageObject)null, (TLRPC.FileLocation)null, (ArrayList)null, var1, (ArrayList)null, var2, var3, (ChatActivity)null, 0L, 0L, true);
   }

   public boolean openPhoto(MessageObject var1, long var2, long var4, PhotoViewer.PhotoViewerProvider var6) {
      return this.openPhoto(var1, (TLRPC.FileLocation)null, (ArrayList)null, (ArrayList)null, (ArrayList)null, 0, var6, (ChatActivity)null, var2, var4, true);
   }

   public boolean openPhoto(MessageObject var1, long var2, long var4, PhotoViewer.PhotoViewerProvider var6, boolean var7) {
      return this.openPhoto(var1, (TLRPC.FileLocation)null, (ArrayList)null, (ArrayList)null, (ArrayList)null, 0, var6, (ChatActivity)null, var2, var4, var7);
   }

   public boolean openPhoto(MessageObject var1, TLRPC.FileLocation var2, ArrayList var3, ArrayList var4, final ArrayList var5, int var6, PhotoViewer.PhotoViewerProvider var7, ChatActivity var8, long var9, long var11, boolean var13) {
      if (this.parentActivity != null && !this.isVisible && (var7 != null || !this.checkAnimation()) && (var1 != null || var2 != null || var3 != null || var5 != null || var4 != null)) {
         final PhotoViewer.PlaceProviderObject var14 = var7.getPlaceForPhoto(var1, var2, var6, true);
         if (var14 == null && var5 == null) {
            return false;
         }

         this.lastInsets = null;
         WindowManager var15 = (WindowManager)this.parentActivity.getSystemService("window");
         if (this.attachedToWindow) {
            try {
               var15.removeView(this.windowView);
            } catch (Exception var17) {
            }
         }

         Exception var10000;
         label150: {
            boolean var10001;
            label126: {
               try {
                  this.windowLayoutParams.type = 99;
                  if (VERSION.SDK_INT >= 21) {
                     this.windowLayoutParams.flags = -2147286784;
                     break label126;
                  }
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label150;
               }

               try {
                  this.windowLayoutParams.flags = 131072;
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label150;
               }
            }

            try {
               this.windowLayoutParams.softInputMode = 272;
               this.windowView.setFocusable(false);
               this.containerView.setFocusable(false);
               var15.addView(this.windowView, this.windowLayoutParams);
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label150;
            }

            this.doneButtonPressed = false;
            this.parentChatActivity = var8;
            this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, 1, 1));
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidFailedLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileLoadProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.mediaCountDidLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.mediaDidLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.dialogPhotosLoaded);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.filePreparingFailed);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileNewChunkAvailable);
            this.placeProvider = var7;
            this.mergeDialogId = var11;
            this.currentDialogId = var9;
            this.selectedPhotosAdapter.notifyDataSetChanged();
            if (this.velocityTracker == null) {
               this.velocityTracker = VelocityTracker.obtain();
            }

            this.isVisible = true;
            this.togglePhotosListView(false, false);
            this.openedFullScreenVideo = var13 ^ true;
            if (this.openedFullScreenVideo) {
               this.toggleActionBar(false, false);
            } else if (this.sendPhotoType == 1) {
               this.createCropView();
               this.toggleActionBar(false, false);
            } else {
               this.toggleActionBar(true, false);
            }

            this.seekToProgressPending2 = 0.0F;
            this.skipFirstBufferingProgress = false;
            this.playerInjected = false;
            if (var14 != null) {
               this.disableShowCheck = true;
               this.animationInProgress = 1;
               if (var1 != null) {
                  this.currentAnimation = var14.imageReceiver.getAnimation();
                  if (this.currentAnimation != null) {
                     if (var1.isVideo()) {
                        var14.imageReceiver.setAllowStartAnimation(false);
                        var14.imageReceiver.stopAnimation();
                        if (MediaController.getInstance().isPlayingMessage(var1)) {
                           this.seekToProgressPending2 = var1.audioProgress;
                        }

                        if (this.injectingVideoPlayer != null || FileLoader.getInstance(var1.currentAccount).isLoadingVideo(var1.getDocument(), true) || !this.currentAnimation.hasBitmap() && FileLoader.getInstance(var1.currentAccount).isLoadingVideo(var1.getDocument(), false)) {
                           var13 = false;
                        } else {
                           var13 = true;
                        }

                        this.skipFirstBufferingProgress = var13;
                        this.currentAnimation = null;
                     } else if (var1.getWebPagePhotos((ArrayList)null, (ArrayList)null).size() > 1) {
                        this.currentAnimation = null;
                     }
                  }
               }

               this.onPhotoShow(var1, var2, var3, var4, var5, var6, var14);
               if (this.sendPhotoType == 1) {
                  this.photoCropView.setVisibility(0);
                  this.photoCropView.setAlpha(0.0F);
                  this.photoCropView.setFreeform(false);
               }

               this.windowView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                  // $FF: synthetic method
                  public void lambda$onPreDraw$0$PhotoViewer$36(ArrayList var1) {
                     if (PhotoViewer.this.containerView != null && PhotoViewer.this.windowView != null) {
                        if (VERSION.SDK_INT >= 18) {
                           PhotoViewer.this.containerView.setLayerType(0, (Paint)null);
                        }

                        PhotoViewer.this.animationInProgress = 0;
                        PhotoViewer.this.transitionAnimationStartTime = 0L;
                        PhotoViewer.this.setImages();
                        PhotoViewer.this.setCropBitmap();
                        if (PhotoViewer.this.sendPhotoType == 1) {
                           PhotoViewer.this.photoCropView.showBackView();
                        }

                        PhotoViewer.this.containerView.invalidate();
                        PhotoViewer.this.animatingImageView.setVisibility(8);
                        if (PhotoViewer.this.showAfterAnimation != null) {
                           PhotoViewer.this.showAfterAnimation.imageReceiver.setVisible(true, true);
                        }

                        if (PhotoViewer.this.hideAfterAnimation != null) {
                           PhotoViewer.this.hideAfterAnimation.imageReceiver.setVisible(false, true);
                        }

                        if (var1 != null && PhotoViewer.this.sendPhotoType != 3) {
                           if (VERSION.SDK_INT >= 21) {
                              PhotoViewer.this.windowLayoutParams.flags = -2147417856;
                           } else {
                              PhotoViewer.this.windowLayoutParams.flags = 0;
                           }

                           PhotoViewer.this.windowLayoutParams.softInputMode = 272;
                           ((WindowManager)PhotoViewer.this.parentActivity.getSystemService("window")).updateViewLayout(PhotoViewer.this.windowView, PhotoViewer.this.windowLayoutParams);
                           PhotoViewer.this.windowView.setFocusable(true);
                           PhotoViewer.this.containerView.setFocusable(true);
                        }
                     }

                  }

                  // $FF: synthetic method
                  public void lambda$onPreDraw$1$PhotoViewer$36(AnimatorSet var1) {
                     NotificationCenter.getInstance(PhotoViewer.this.currentAccount).setAllowedNotificationsDutingAnimation(new int[]{NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats, NotificationCenter.mediaCountDidLoad, NotificationCenter.mediaDidLoad, NotificationCenter.dialogPhotosLoaded});
                     NotificationCenter.getInstance(PhotoViewer.this.currentAccount).setAnimationInProgress(true);
                     var1.start();
                  }

                  // $FF: synthetic method
                  public void lambda$onPreDraw$2$PhotoViewer$36(PhotoViewer.PlaceProviderObject var1) {
                     PhotoViewer.this.disableShowCheck = false;
                     var1.imageReceiver.setVisible(false, true);
                  }

                  public boolean onPreDraw() {
                     PhotoViewer.this.windowView.getViewTreeObserver().removeOnPreDrawListener(this);
                     RectF var1 = var14.imageReceiver.getDrawRegion();
                     int var2 = var14.imageReceiver.getOrientation();
                     int var3 = var14.imageReceiver.getAnimatedOrientation();
                     if (var3 != 0) {
                        var2 = var3;
                     }

                     PhotoViewer.this.animatingImageView.setVisibility(0);
                     PhotoViewer.this.animatingImageView.setRadius(var14.radius);
                     PhotoViewer.this.animatingImageView.setOrientation(var2);
                     ClippingImageView var4 = PhotoViewer.this.animatingImageView;
                     boolean var5x;
                     if (var14.radius != 0) {
                        var5x = true;
                     } else {
                        var5x = false;
                     }

                     var4.setNeedRadius(var5x);
                     PhotoViewer.this.animatingImageView.setImageBitmap(var14.thumb);
                     PhotoViewer.this.initCropView();
                     if (PhotoViewer.this.sendPhotoType == 1) {
                        PhotoViewer.this.photoCropView.hideBackView();
                        PhotoViewer.this.photoCropView.setAspectRatio(1.0F);
                     }

                     PhotoViewer.this.animatingImageView.setAlpha(1.0F);
                     PhotoViewer.this.animatingImageView.setPivotX(0.0F);
                     PhotoViewer.this.animatingImageView.setPivotY(0.0F);
                     PhotoViewer.this.animatingImageView.setScaleX(var14.scale);
                     PhotoViewer.this.animatingImageView.setScaleY(var14.scale);
                     ClippingImageView var6 = PhotoViewer.this.animatingImageView;
                     PhotoViewer.PlaceProviderObject var20 = var14;
                     var6.setTranslationX((float)var20.viewX + var1.left * var20.scale);
                     var6 = PhotoViewer.this.animatingImageView;
                     var20 = var14;
                     var6.setTranslationY((float)var20.viewY + var1.top * var20.scale);
                     android.view.ViewGroup.LayoutParams var23 = PhotoViewer.this.animatingImageView.getLayoutParams();
                     var23.width = (int)var1.width();
                     var23.height = (int)var1.height();
                     PhotoViewer.this.animatingImageView.setLayoutParams(var23);
                     float var7;
                     float var8;
                     float var9;
                     float var10;
                     float var11;
                     if (PhotoViewer.this.sendPhotoType == 1) {
                        if (VERSION.SDK_INT >= 21) {
                           var2 = AndroidUtilities.statusBarHeight;
                        } else {
                           var2 = 0;
                        }

                        var7 = (float)var2;
                        var8 = (float)PhotoViewer.this.photoCropView.getMeasuredHeight() - (float)AndroidUtilities.dp(64.0F) - var7;
                        var9 = Math.min((float)PhotoViewer.this.photoCropView.getMeasuredWidth(), var8);
                        var10 = (float)(AndroidUtilities.dp(16.0F) * 2);
                        var11 = (float)PhotoViewer.this.photoCropView.getMeasuredWidth() / 2.0F;
                        var8 = var7 + var8 / 2.0F;
                        var9 = (var9 - var10) / 2.0F;
                        var10 = var8 - var9;
                        var11 = (var11 + var9 - (var11 - var9)) / (float)var23.width;
                        var8 = var8 + var9 - var10;
                        var9 = Math.max(var11, var8 / (float)var23.height);
                        var8 = var10 + (var8 - (float)var23.height * var9) / 2.0F;
                        var11 = ((float)(PhotoViewer.this.windowView.getMeasuredWidth() - PhotoViewer.this.getLeftInset() - PhotoViewer.this.getRightInset()) - (float)var23.width * var9) / 2.0F + (float)PhotoViewer.this.getLeftInset();
                     } else {
                        var11 = (float)PhotoViewer.this.windowView.getMeasuredWidth() / (float)var23.width;
                        var3 = AndroidUtilities.displaySize.y;
                        if (VERSION.SDK_INT >= 21) {
                           var2 = AndroidUtilities.statusBarHeight;
                        } else {
                           var2 = 0;
                        }

                        var8 = (float)(var3 + var2) / (float)var23.height;
                        if (var11 > var8) {
                           var11 = var8;
                        }

                        var3 = AndroidUtilities.displaySize.y;
                        if (VERSION.SDK_INT >= 21) {
                           var2 = AndroidUtilities.statusBarHeight;
                        } else {
                           var2 = 0;
                        }

                        var8 = ((float)(var3 + var2) - (float)var23.height * var11) / 2.0F;
                        var10 = ((float)PhotoViewer.this.windowView.getMeasuredWidth() - (float)var23.width * var11) / 2.0F;
                        var9 = var11;
                        var11 = var10;
                     }

                     int var12 = (int)Math.abs(var1.left - (float)var14.imageReceiver.getImageX());
                     int var13 = (int)Math.abs(var1.top - (float)var14.imageReceiver.getImageY());
                     int[] var21 = new int[2];
                     var14.parentView.getLocationInWindow(var21);
                     var3 = var21[1];
                     if (VERSION.SDK_INT >= 21) {
                        var2 = 0;
                     } else {
                        var2 = AndroidUtilities.statusBarHeight;
                     }

                     var10 = (float)(var3 - var2);
                     PhotoViewer.PlaceProviderObject var14x = var14;
                     var3 = (int)(var10 - ((float)var14x.viewY + var1.top) + (float)var14x.clipTopAddition);
                     var2 = var3;
                     if (var3 < 0) {
                        var2 = 0;
                     }

                     var14x = var14;
                     var7 = (float)var14x.viewY;
                     float var15 = var1.top;
                     var10 = (float)var23.height;
                     int var16 = var21[1];
                     int var17 = var14x.parentView.getHeight();
                     if (VERSION.SDK_INT >= 21) {
                        var3 = 0;
                     } else {
                        var3 = AndroidUtilities.statusBarHeight;
                     }

                     var16 = (int)(var7 + var15 + var10 - (float)(var16 + var17 - var3) + (float)var14.clipBottomAddition);
                     var3 = var16;
                     if (var16 < 0) {
                        var3 = 0;
                     }

                     var16 = Math.max(var2, var13);
                     var3 = Math.max(var3, var13);
                     PhotoViewer.this.animationValues[0][0] = PhotoViewer.this.animatingImageView.getScaleX();
                     PhotoViewer.this.animationValues[0][1] = PhotoViewer.this.animatingImageView.getScaleY();
                     PhotoViewer.this.animationValues[0][2] = PhotoViewer.this.animatingImageView.getTranslationX();
                     float[] var18 = PhotoViewer.this.animationValues[0];
                     var10 = PhotoViewer.this.animatingImageView.getTranslationY();
                     byte var24 = 3;
                     var18[3] = var10;
                     var18 = PhotoViewer.this.animationValues[0];
                     var10 = (float)var12;
                     var18[4] = var14.scale * var10;
                     PhotoViewer.this.animationValues[0][5] = (float)var16 * var14.scale;
                     PhotoViewer.this.animationValues[0][6] = (float)var3 * var14.scale;
                     PhotoViewer.this.animationValues[0][7] = (float)PhotoViewer.this.animatingImageView.getRadius();
                     PhotoViewer.this.animationValues[0][8] = (float)var13 * var14.scale;
                     PhotoViewer.this.animationValues[0][9] = var10 * var14.scale;
                     PhotoViewer.this.animationValues[1][0] = var9;
                     PhotoViewer.this.animationValues[1][1] = var9;
                     PhotoViewer.this.animationValues[1][2] = var11;
                     PhotoViewer.this.animationValues[1][3] = var8;
                     PhotoViewer.this.animationValues[1][4] = 0.0F;
                     PhotoViewer.this.animationValues[1][5] = 0.0F;
                     PhotoViewer.this.animationValues[1][6] = 0.0F;
                     PhotoViewer.this.animationValues[1][7] = 0.0F;
                     PhotoViewer.this.animationValues[1][8] = 0.0F;
                     PhotoViewer.this.animationValues[1][9] = 0.0F;
                     PhotoViewer.this.animatingImageView.setAnimationProgress(0.0F);
                     PhotoViewer.this.backgroundDrawable.setAlpha(0);
                     PhotoViewer.this.containerView.setAlpha(0.0F);
                     PhotoViewer.this.animationEndRunnable = new _$$Lambda$PhotoViewer$36$6TF1l_RAT6OwuDg5xrfLlfrxKX0(this, var5);
                     if (!PhotoViewer.this.openedFullScreenVideo) {
                        AnimatorSet var22 = new AnimatorSet();
                        if (PhotoViewer.this.sendPhotoType == 1) {
                           var24 = 4;
                        }

                        ArrayList var19 = new ArrayList(var24);
                        var19.add(ObjectAnimator.ofFloat(PhotoViewer.this.animatingImageView, AnimationProperties.CLIPPING_IMAGE_VIEW_PROGRESS, new float[]{0.0F, 1.0F}));
                        var19.add(ObjectAnimator.ofInt(PhotoViewer.this.backgroundDrawable, AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[]{0, 255}));
                        var19.add(ObjectAnimator.ofFloat(PhotoViewer.this.containerView, View.ALPHA, new float[]{0.0F, 1.0F}));
                        if (PhotoViewer.this.sendPhotoType == 1) {
                           var19.add(ObjectAnimator.ofFloat(PhotoViewer.this.photoCropView, View.ALPHA, new float[]{0.0F, 1.0F}));
                        }

                        var22.playTogether(var19);
                        var22.setDuration(200L);
                        var22.addListener(new AnimatorListenerAdapter() {
                           // $FF: synthetic method
                           public void lambda$onAnimationEnd$0$PhotoViewer$36$1() {
                              NotificationCenter.getInstance(PhotoViewer.this.currentAccount).setAnimationInProgress(false);
                              if (PhotoViewer.this.animationEndRunnable != null) {
                                 PhotoViewer.this.animationEndRunnable.run();
                                 PhotoViewer.this.animationEndRunnable = null;
                              }

                           }

                           public void onAnimationEnd(Animator var1) {
                              AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$36$1$z2Mdnj_EJcKOldib3qThdRwgaKk(this));
                           }
                        });
                        if (VERSION.SDK_INT >= 18) {
                           PhotoViewer.this.containerView.setLayerType(2, (Paint)null);
                        }

                        PhotoViewer.this.transitionAnimationStartTime = System.currentTimeMillis();
                        AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$36$THb2N7uCapRChPdPXPsoefivhPs(this, var22));
                     } else {
                        if (PhotoViewer.this.animationEndRunnable != null) {
                           PhotoViewer.this.animationEndRunnable.run();
                           PhotoViewer.this.animationEndRunnable = null;
                        }

                        PhotoViewer.this.containerView.setAlpha(1.0F);
                        PhotoViewer.this.backgroundDrawable.setAlpha(255);
                        PhotoViewer.this.animatingImageView.setAnimationProgress(1.0F);
                        if (PhotoViewer.this.sendPhotoType == 1) {
                           PhotoViewer.this.photoCropView.setAlpha(1.0F);
                        }
                     }

                     PhotoViewer.this.backgroundDrawable.drawRunnable = new _$$Lambda$PhotoViewer$36$21zfYLrtYOpgxSCyhiSDh2RoAK4(this, var14);
                     return true;
                  }
               });
            } else {
               if (var5 != null && this.sendPhotoType != 3) {
                  if (VERSION.SDK_INT >= 21) {
                     this.windowLayoutParams.flags = -2147417856;
                  } else {
                     this.windowLayoutParams.flags = 0;
                  }

                  LayoutParams var24 = this.windowLayoutParams;
                  var24.softInputMode = 272;
                  var15.updateViewLayout(this.windowView, var24);
                  this.windowView.setFocusable(true);
                  this.containerView.setFocusable(true);
               }

               this.backgroundDrawable.setAlpha(255);
               this.containerView.setAlpha(1.0F);
               this.onPhotoShow(var1, var2, var3, var4, var5, var6, var14);
               this.initCropView();
               this.setCropBitmap();
            }

            AccessibilityManager var23 = (AccessibilityManager)this.parentActivity.getSystemService("accessibility");
            if (var23.isTouchExplorationEnabled()) {
               AccessibilityEvent var22 = AccessibilityEvent.obtain();
               var22.setEventType(16384);
               var22.getText().add(LocaleController.getString("AccDescrPhotoViewer", 2131558457));
               var23.sendAccessibilityEvent(var22);
            }

            return true;
         }

         Exception var21 = var10000;
         FileLog.e((Throwable)var21);
      }

      return false;
   }

   public boolean openPhoto(TLRPC.FileLocation var1, PhotoViewer.PhotoViewerProvider var2) {
      return this.openPhoto((MessageObject)null, var1, (ArrayList)null, (ArrayList)null, (ArrayList)null, 0, var2, (ChatActivity)null, 0L, 0L, true);
   }

   public boolean openPhotoForSelect(ArrayList var1, int var2, int var3, PhotoViewer.PhotoViewerProvider var4, ChatActivity var5) {
      this.sendPhotoType = var3;
      ImageView var6 = this.pickerViewSendButton;
      if (var6 != null) {
         android.widget.FrameLayout.LayoutParams var7 = (android.widget.FrameLayout.LayoutParams)var6.getLayoutParams();
         var3 = this.sendPhotoType;
         if (var3 != 4 && var3 != 5) {
            if (var3 != 1 && var3 != 3) {
               this.pickerViewSendButton.setImageResource(2131165468);
               this.pickerViewSendButton.setPadding(AndroidUtilities.dp(4.0F), 0, 0, 0);
               var7.bottomMargin = AndroidUtilities.dp(14.0F);
            } else {
               this.pickerViewSendButton.setImageResource(2131165384);
               this.pickerViewSendButton.setPadding(0, AndroidUtilities.dp(1.0F), 0, 0);
               var7.bottomMargin = AndroidUtilities.dp(19.0F);
            }
         } else {
            this.pickerViewSendButton.setImageResource(2131165468);
            this.pickerViewSendButton.setPadding(AndroidUtilities.dp(4.0F), 0, 0, 0);
            var7.bottomMargin = AndroidUtilities.dp(19.0F);
         }

         this.pickerViewSendButton.setLayoutParams(var7);
      }

      return this.openPhoto((MessageObject)null, (TLRPC.FileLocation)null, (ArrayList)null, (ArrayList)null, var1, var2, var4, var5, 0L, 0L, true);
   }

   @Keep
   public void setAnimationValue(float var1) {
      this.animationValue = var1;
      this.containerView.invalidate();
   }

   public void setMaxSelectedPhotos(int var1) {
      this.maxSelectedPhotos = var1;
   }

   public void setParentActivity(Activity var1) {
      this.currentAccount = UserConfig.selectedAccount;
      this.centerImage.setCurrentAccount(this.currentAccount);
      this.leftImage.setCurrentAccount(this.currentAccount);
      this.rightImage.setCurrentAccount(this.currentAccount);
      if (this.parentActivity != var1) {
         this.parentActivity = var1;
         this.actvityContext = new ContextThemeWrapper(this.parentActivity, 2131624206);
         if (progressDrawables == null) {
            progressDrawables = new Drawable[4];
            progressDrawables[0] = this.parentActivity.getResources().getDrawable(2131165357);
            progressDrawables[1] = this.parentActivity.getResources().getDrawable(2131165337);
            progressDrawables[2] = this.parentActivity.getResources().getDrawable(2131165537);
            progressDrawables[3] = this.parentActivity.getResources().getDrawable(2131165771);
         }

         this.scroller = new Scroller(var1);
         this.windowView = new FrameLayout(var1) {
            private Runnable attachRunnable;

            public boolean dispatchKeyEventPreIme(KeyEvent var1) {
               if (var1 != null && var1.getKeyCode() == 4 && var1.getAction() == 1) {
                  if (!PhotoViewer.this.captionEditText.isPopupShowing() && !PhotoViewer.this.captionEditText.isKeyboardVisible()) {
                     PhotoViewer.getInstance().closePhoto(true, false);
                     return true;
                  } else {
                     PhotoViewer.this.closeCaptionEnter(false);
                     return false;
                  }
               } else {
                  return super.dispatchKeyEventPreIme(var1);
               }
            }

            protected boolean drawChild(Canvas var1, View var2, long var3) {
               boolean var5;
               try {
                  var5 = super.drawChild(var1, var2, var3);
               } catch (Throwable var7) {
                  var5 = false;
               }

               if (VERSION.SDK_INT >= 21 && var2 == PhotoViewer.this.animatingImageView && PhotoViewer.this.lastInsets != null) {
                  WindowInsets var8 = (WindowInsets)PhotoViewer.this.lastInsets;
                  var1.drawRect(0.0F, (float)this.getMeasuredHeight(), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() + var8.getSystemWindowInsetBottom()), PhotoViewer.this.blackPaint);
               }

               return var5;
            }

            // $FF: synthetic method
            public void lambda$onLayout$0$PhotoViewer$5() {
               android.widget.FrameLayout.LayoutParams var1 = (android.widget.FrameLayout.LayoutParams)PhotoViewer.this.checkImageView.getLayoutParams();
               ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
               int var2 = (ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(40.0F)) / 2;
               int var3 = VERSION.SDK_INT;
               byte var4 = 0;
               if (var3 >= 21) {
                  var3 = AndroidUtilities.statusBarHeight;
               } else {
                  var3 = 0;
               }

               var1.topMargin = var2 + var3;
               PhotoViewer.this.checkImageView.setLayoutParams(var1);
               var1 = (android.widget.FrameLayout.LayoutParams)PhotoViewer.this.photosCounterView.getLayoutParams();
               var2 = (ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(40.0F)) / 2;
               var3 = var4;
               if (VERSION.SDK_INT >= 21) {
                  var3 = AndroidUtilities.statusBarHeight;
               }

               var1.topMargin = var2 + var3;
               PhotoViewer.this.photosCounterView.setLayoutParams(var1);
            }

            protected void onAttachedToWindow() {
               super.onAttachedToWindow();
               PhotoViewer.this.attachedToWindow = true;
            }

            protected void onDetachedFromWindow() {
               super.onDetachedFromWindow();
               PhotoViewer.this.attachedToWindow = false;
               PhotoViewer.this.wasLayout = false;
            }

            public boolean onInterceptTouchEvent(MotionEvent var1) {
               boolean var2;
               if (PhotoViewer.this.isVisible && super.onInterceptTouchEvent(var1)) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               return var2;
            }

            protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
               if (VERSION.SDK_INT >= 21) {
                  PhotoViewer.this.lastInsets;
               }

               PhotoViewer.this.animatingImageView.layout(0, 0, PhotoViewer.this.animatingImageView.getMeasuredWidth() + 0, PhotoViewer.this.animatingImageView.getMeasuredHeight());
               PhotoViewer.this.containerView.layout(0, 0, PhotoViewer.this.containerView.getMeasuredWidth() + 0, PhotoViewer.this.containerView.getMeasuredHeight());
               PhotoViewer.this.wasLayout = true;
               if (var1) {
                  if (!PhotoViewer.this.dontResetZoomOnFirstLayout) {
                     PhotoViewer.this.scale = 1.0F;
                     PhotoViewer.this.translationX = 0.0F;
                     PhotoViewer.this.translationY = 0.0F;
                     PhotoViewer var6 = PhotoViewer.this;
                     var6.updateMinMax(var6.scale);
                  }

                  if (PhotoViewer.this.checkImageView != null) {
                     PhotoViewer.this.checkImageView.post(new _$$Lambda$PhotoViewer$5$gQgnGW1vt8iiuLxByIijlXWDJsU(this));
                  }
               }

               if (PhotoViewer.this.dontResetZoomOnFirstLayout) {
                  PhotoViewer.this.setScaleToFill();
                  PhotoViewer.this.dontResetZoomOnFirstLayout = false;
               }

            }

            protected void onMeasure(int var1, int var2) {
               int var3 = MeasureSpec.getSize(var1);
               var1 = MeasureSpec.getSize(var2);
               int var5;
               if (VERSION.SDK_INT >= 21 && PhotoViewer.this.lastInsets != null) {
                  WindowInsets var4 = (WindowInsets)PhotoViewer.this.lastInsets;
                  var2 = var1;
                  if (AndroidUtilities.incorrectDisplaySizeFix) {
                     var5 = AndroidUtilities.displaySize.y;
                     var2 = var1;
                     if (var1 > var5) {
                        var2 = var5;
                     }

                     var2 += AndroidUtilities.statusBarHeight;
                  }

                  var2 -= var4.getSystemWindowInsetBottom();
               } else {
                  var5 = AndroidUtilities.displaySize.y;
                  var2 = var1;
                  if (var1 > var5) {
                     var2 = var5;
                  }
               }

               this.setMeasuredDimension(var3, var2);
               android.view.ViewGroup.LayoutParams var6 = PhotoViewer.this.animatingImageView.getLayoutParams();
               PhotoViewer.this.animatingImageView.measure(MeasureSpec.makeMeasureSpec(var6.width, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(var6.height, Integer.MIN_VALUE));
               PhotoViewer.this.containerView.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var2, 1073741824));
            }

            public boolean onTouchEvent(MotionEvent var1) {
               boolean var2;
               if (PhotoViewer.this.isVisible && PhotoViewer.this.onTouchEvent(var1)) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               return var2;
            }

            public ActionMode startActionModeForChild(View var1, Callback var2, int var3) {
               if (VERSION.SDK_INT >= 23) {
                  View var4 = PhotoViewer.this.parentActivity.findViewById(16908290);
                  if (var4 instanceof ViewGroup) {
                     try {
                        ActionMode var6 = ((ViewGroup)var4).startActionModeForChild(var1, var2, var3);
                        return var6;
                     } catch (Throwable var5) {
                        FileLog.e(var5);
                     }
                  }
               }

               return super.startActionModeForChild(var1, var2, var3);
            }
         };
         this.windowView.setBackgroundDrawable(this.backgroundDrawable);
         this.windowView.setClipChildren(true);
         this.windowView.setFocusable(false);
         this.animatingImageView = new ClippingImageView(var1);
         this.animatingImageView.setAnimationValues(this.animationValues);
         this.windowView.addView(this.animatingImageView, LayoutHelper.createFrame(40, 40.0F));
         this.containerView = new PhotoViewer.FrameLayoutDrawer(var1);
         this.containerView.setFocusable(false);
         this.windowView.addView(this.containerView, LayoutHelper.createFrame(-1, -1, 51));
         if (VERSION.SDK_INT >= 21) {
            this.containerView.setFitsSystemWindows(true);
            this.containerView.setOnApplyWindowInsetsListener(new _$$Lambda$PhotoViewer$dljDNjeNI7WCE_iobU6HEh1zPNQ(this));
            this.containerView.setSystemUiVisibility(1792);
         }

         this.windowLayoutParams = new LayoutParams();
         LayoutParams var2 = this.windowLayoutParams;
         var2.height = -1;
         var2.format = -3;
         var2.width = -1;
         var2.gravity = 51;
         var2.type = 99;
         if (VERSION.SDK_INT >= 28) {
            var2.layoutInDisplayCutoutMode = 1;
         }

         if (VERSION.SDK_INT >= 21) {
            this.windowLayoutParams.flags = -2147286784;
         } else {
            this.windowLayoutParams.flags = 131072;
         }

         this.actionBar = new ActionBar(var1) {
            public void setAlpha(float var1) {
               super.setAlpha(var1);
               PhotoViewer.this.containerView.invalidate();
            }
         };
         this.actionBar.setTitleColor(-1);
         this.actionBar.setSubtitleColor(-1);
         this.actionBar.setBackgroundColor(2130706432);
         ActionBar var7 = this.actionBar;
         boolean var3;
         if (VERSION.SDK_INT >= 21) {
            var3 = true;
         } else {
            var3 = false;
         }

         var7.setOccupyStatusBar(var3);
         this.actionBar.setItemsBackgroundColor(1090519039, false);
         this.actionBar.setBackButtonImage(2131165409);
         this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, 1, 1));
         this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0F));
         this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            // $FF: synthetic method
            static void lambda$onItemClick$1(boolean[] var0, View var1) {
               CheckBoxCell var2 = (CheckBoxCell)var1;
               var0[0] ^= true;
               var2.setChecked(var0[0], true);
            }

            public boolean canOpenMenu() {
               if (PhotoViewer.this.currentMessageObject != null) {
                  return FileLoader.getPathToMessage(PhotoViewer.this.currentMessageObject.messageOwner).exists();
               } else {
                  ImageLocation var1 = PhotoViewer.this.currentFileLocation;
                  boolean var2 = false;
                  if (var1 == null) {
                     return false;
                  } else {
                     PhotoViewer var3 = PhotoViewer.this;
                     TLRPC.FileLocation var4 = var3.getFileLocation(var3.currentFileLocation);
                     if (PhotoViewer.this.avatarsDialogId != 0 || PhotoViewer.this.isEvent) {
                        var2 = true;
                     }

                     return FileLoader.getPathToAttach(var4, var2).exists();
                  }
               }
            }

            // $FF: synthetic method
            public void lambda$onItemClick$0$PhotoViewer$7(ArrayList var1, DialogsActivity var2, ArrayList var3, CharSequence var4, boolean var5) {
               int var7 = var3.size();
               byte var8 = 0;
               int var9 = var8;
               long var10;
               if (var7 <= 1) {
                  var9 = var8;
                  if ((Long)var3.get(0) != (long)UserConfig.getInstance(PhotoViewer.this.currentAccount).getClientUserId()) {
                     if (var4 == null) {
                        var10 = (Long)var3.get(0);
                        var9 = (int)var10;
                        int var14 = (int)(var10 >> 32);
                        Bundle var12 = new Bundle();
                        var12.putBoolean("scrollToTopOnResume", true);
                        if (var9 != 0) {
                           if (var9 > 0) {
                              var12.putInt("user_id", var9);
                           } else if (var9 < 0) {
                              var12.putInt("chat_id", -var9);
                           }
                        } else {
                           var12.putInt("enc_id", var14);
                        }

                        NotificationCenter.getInstance(PhotoViewer.this.currentAccount).postNotificationName(NotificationCenter.closeChats);
                        ChatActivity var13 = new ChatActivity(var12);
                        if (((LaunchActivity)PhotoViewer.this.parentActivity).presentFragment(var13, true, false)) {
                           var13.showFieldPanelForForward(true, var1);
                        } else {
                           var2.finishFragment();
                        }

                        return;
                     }

                     var9 = var8;
                  }
               }

               while(var9 < var3.size()) {
                  var10 = (Long)var3.get(var9);
                  if (var4 != null) {
                     SendMessagesHelper.getInstance(PhotoViewer.this.currentAccount).sendMessage(var4.toString(), var10, (MessageObject)null, (TLRPC.WebPage)null, true, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
                  }

                  SendMessagesHelper.getInstance(PhotoViewer.this.currentAccount).sendMessage(var1, var10);
                  ++var9;
               }

               var2.finishFragment();
            }

            // $FF: synthetic method
            public void lambda$onItemClick$2$PhotoViewer$7(boolean[] var1, DialogInterface var2, int var3) {
               if (!PhotoViewer.this.imagesArr.isEmpty()) {
                  if (PhotoViewer.this.currentIndex < 0 || PhotoViewer.this.currentIndex >= PhotoViewer.this.imagesArr.size()) {
                     return;
                  }

                  MessageObject var4 = (MessageObject)PhotoViewer.this.imagesArr.get(PhotoViewer.this.currentIndex);
                  if (var4.isSent()) {
                     PhotoViewer.this.closePhoto(false, false);
                     ArrayList var5 = new ArrayList();
                     if (PhotoViewer.this.slideshowMessageId != 0) {
                        var5.add(PhotoViewer.this.slideshowMessageId);
                     } else {
                        var5.add(var4.getId());
                     }

                     ArrayList var12;
                     Object var15;
                     if ((int)var4.getDialogId() == 0 && var4.messageOwner.random_id != 0L) {
                        var12 = new ArrayList();
                        var12.add(var4.messageOwner.random_id);
                        var15 = MessagesController.getInstance(PhotoViewer.this.currentAccount).getEncryptedChat((int)(var4.getDialogId() >> 32));
                     } else {
                        var12 = null;
                        var15 = var12;
                     }

                     MessagesController.getInstance(PhotoViewer.this.currentAccount).deleteMessages(var5, var12, (TLRPC.EncryptedChat)var15, var4.messageOwner.to_id.channel_id, var1[0]);
                  }
               } else {
                  int var7;
                  if (!PhotoViewer.this.avatarsArr.isEmpty()) {
                     if (PhotoViewer.this.currentIndex < 0 || PhotoViewer.this.currentIndex >= PhotoViewer.this.avatarsArr.size()) {
                        return;
                     }

                     TLRPC.Photo var9 = (TLRPC.Photo)PhotoViewer.this.avatarsArr.get(PhotoViewer.this.currentIndex);
                     ImageLocation var6 = (ImageLocation)PhotoViewer.this.imagesArrLocations.get(PhotoViewer.this.currentIndex);
                     TLRPC.Photo var8 = var9;
                     if (var9 instanceof TLRPC.TL_photoEmpty) {
                        var8 = null;
                     }

                     boolean var13;
                     label86: {
                        label115: {
                           if (PhotoViewer.this.currentUserAvatarLocation != null) {
                              if (var8 != null) {
                                 Iterator var10 = var8.sizes.iterator();

                                 while(var10.hasNext()) {
                                    TLRPC.PhotoSize var14 = (TLRPC.PhotoSize)var10.next();
                                    if (var14.location.local_id == PhotoViewer.this.currentUserAvatarLocation.location.local_id && var14.location.volume_id == PhotoViewer.this.currentUserAvatarLocation.location.volume_id) {
                                       break label115;
                                    }
                                 }
                              } else if (var6.location.local_id == PhotoViewer.this.currentUserAvatarLocation.location.local_id && var6.location.volume_id == PhotoViewer.this.currentUserAvatarLocation.location.volume_id) {
                                 break label115;
                              }
                           }

                           var13 = false;
                           break label86;
                        }

                        var13 = true;
                     }

                     if (var13) {
                        MessagesController.getInstance(PhotoViewer.this.currentAccount).deleteUserPhoto((TLRPC.InputPhoto)null);
                        PhotoViewer.this.closePhoto(false, false);
                     } else if (var8 != null) {
                        TLRPC.TL_inputPhoto var11 = new TLRPC.TL_inputPhoto();
                        var11.id = var8.id;
                        var11.access_hash = var8.access_hash;
                        var11.file_reference = var8.file_reference;
                        if (var11.file_reference == null) {
                           var11.file_reference = new byte[0];
                        }

                        MessagesController.getInstance(PhotoViewer.this.currentAccount).deleteUserPhoto(var11);
                        MessagesStorage.getInstance(PhotoViewer.this.currentAccount).clearUserPhoto(PhotoViewer.this.avatarsDialogId, var8.id);
                        PhotoViewer.this.imagesArrLocations.remove(PhotoViewer.this.currentIndex);
                        PhotoViewer.this.imagesArrLocationsSizes.remove(PhotoViewer.this.currentIndex);
                        PhotoViewer.this.avatarsArr.remove(PhotoViewer.this.currentIndex);
                        if (PhotoViewer.this.imagesArrLocations.isEmpty()) {
                           PhotoViewer.this.closePhoto(false, false);
                        } else {
                           var7 = PhotoViewer.this.currentIndex;
                           var3 = var7;
                           if (var7 >= PhotoViewer.this.avatarsArr.size()) {
                              var3 = PhotoViewer.this.avatarsArr.size() - 1;
                           }

                           PhotoViewer.this.currentIndex = -1;
                           PhotoViewer.this.setImageIndex(var3, true);
                        }
                     }
                  } else if (!PhotoViewer.this.secureDocuments.isEmpty()) {
                     if (PhotoViewer.this.placeProvider == null) {
                        return;
                     }

                     PhotoViewer.this.secureDocuments.remove(PhotoViewer.this.currentIndex);
                     PhotoViewer.this.placeProvider.deleteImageAtIndex(PhotoViewer.this.currentIndex);
                     if (PhotoViewer.this.secureDocuments.isEmpty()) {
                        PhotoViewer.this.closePhoto(false, false);
                     } else {
                        var7 = PhotoViewer.this.currentIndex;
                        var3 = var7;
                        if (var7 >= PhotoViewer.this.secureDocuments.size()) {
                           var3 = PhotoViewer.this.secureDocuments.size() - 1;
                        }

                        PhotoViewer.this.currentIndex = -1;
                        PhotoViewer.this.setImageIndex(var3, true);
                     }
                  }
               }

            }

            public void onItemClick(int var1) {
               byte var2 = 1;
               if (var1 != -1) {
                  boolean var4;
                  if (var1 == 1) {
                     if (VERSION.SDK_INT >= 23 && PhotoViewer.this.parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                        PhotoViewer.this.parentActivity.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
                        return;
                     }

                     File var19;
                     if (PhotoViewer.this.currentMessageObject != null) {
                        if (PhotoViewer.this.currentMessageObject.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage && PhotoViewer.this.currentMessageObject.messageOwner.media.webpage != null && PhotoViewer.this.currentMessageObject.messageOwner.media.webpage.document == null) {
                           PhotoViewer var22 = PhotoViewer.this;
                           var19 = FileLoader.getPathToAttach(var22.getFileLocation(var22.currentIndex, (int[])null), true);
                        } else {
                           var19 = FileLoader.getPathToMessage(PhotoViewer.this.currentMessageObject.messageOwner);
                        }
                     } else if (PhotoViewer.this.currentFileLocation == null) {
                        var19 = null;
                     } else {
                        TLRPC.TL_fileLocationToBeDeprecated var17 = PhotoViewer.this.currentFileLocation.location;
                        if (PhotoViewer.this.avatarsDialogId == 0 && !PhotoViewer.this.isEvent) {
                           var4 = false;
                        } else {
                           var4 = true;
                        }

                        var19 = FileLoader.getPathToAttach(var17, var4);
                     }

                     if (var19 != null && var19.exists()) {
                        String var21 = var19.toString();
                        Activity var23 = PhotoViewer.this.parentActivity;
                        byte var16;
                        if (PhotoViewer.this.currentMessageObject != null && PhotoViewer.this.currentMessageObject.isVideo()) {
                           var16 = var2;
                        } else {
                           var16 = 0;
                        }

                        MediaController.saveFile(var21, var23, var16, (String)null, (String)null);
                     } else {
                        PhotoViewer.this.showDownloadAlert();
                     }
                  } else {
                     Bundle var13;
                     if (var1 == 2) {
                        if (PhotoViewer.this.currentDialogId != 0L) {
                           PhotoViewer.this.disableShowCheck = true;
                           var13 = new Bundle();
                           var13.putLong("dialog_id", PhotoViewer.this.currentDialogId);
                           var1 = PhotoViewer.this.sharedMediaType;
                           MediaActivity var15 = new MediaActivity(var13, new int[]{-1, -1, -1, -1, -1}, (MediaActivity.SharedMediaData[])null, var1);
                           if (PhotoViewer.this.parentChatActivity != null) {
                              var15.setChatInfo(PhotoViewer.this.parentChatActivity.getCurrentChatInfo());
                           }

                           PhotoViewer.this.closePhoto(false, false);
                           ((LaunchActivity)PhotoViewer.this.parentActivity).presentFragment(var15, false, true);
                        }
                     } else {
                        TLRPC.Chat var5;
                        int var11;
                        if (var1 == 4) {
                           if (PhotoViewer.this.currentMessageObject == null) {
                              return;
                           }

                           var13 = new Bundle();
                           var11 = (int)PhotoViewer.this.currentDialogId;
                           var1 = (int)(PhotoViewer.this.currentDialogId >> 32);
                           if (var11 != 0) {
                              if (var1 == 1) {
                                 var13.putInt("chat_id", var11);
                              } else if (var11 > 0) {
                                 var13.putInt("user_id", var11);
                              } else if (var11 < 0) {
                                 var5 = MessagesController.getInstance(PhotoViewer.this.currentAccount).getChat(-var11);
                                 var1 = var11;
                                 if (var5 != null) {
                                    var1 = var11;
                                    if (var5.migrated_to != null) {
                                       var13.putInt("migrated_to", var11);
                                       var1 = -var5.migrated_to.channel_id;
                                    }
                                 }

                                 var13.putInt("chat_id", -var1);
                              }
                           } else {
                              var13.putInt("enc_id", var1);
                           }

                           var13.putInt("message_id", PhotoViewer.this.currentMessageObject.getId());
                           NotificationCenter.getInstance(PhotoViewer.this.currentAccount).postNotificationName(NotificationCenter.closeChats);
                           LaunchActivity var20 = (LaunchActivity)PhotoViewer.this.parentActivity;
                           if (var20.getMainFragmentsCount() <= 1 && !AndroidUtilities.isTablet()) {
                              var4 = false;
                           } else {
                              var4 = true;
                           }

                           var20.presentFragment(new ChatActivity(var13), var4, true);
                           PhotoViewer.this.currentMessageObject = null;
                           PhotoViewer.this.closePhoto(false, false);
                        } else if (var1 == 3) {
                           if (PhotoViewer.this.currentMessageObject == null || PhotoViewer.this.parentActivity == null) {
                              return;
                           }

                           ((LaunchActivity)PhotoViewer.this.parentActivity).switchToAccount(PhotoViewer.this.currentMessageObject.currentAccount, true);
                           var13 = new Bundle();
                           var13.putBoolean("onlySelect", true);
                           var13.putInt("dialogsType", 3);
                           DialogsActivity var18 = new DialogsActivity(var13);
                           ArrayList var14 = new ArrayList();
                           var14.add(PhotoViewer.this.currentMessageObject);
                           var18.setDelegate(new _$$Lambda$PhotoViewer$7$MBP2lcO9C4pgMJ4ntN8Td94R7bI(this, var14));
                           ((LaunchActivity)PhotoViewer.this.parentActivity).presentFragment(var18, false, true);
                           PhotoViewer.this.closePhoto(false, false);
                        } else if (var1 == 6) {
                           if (PhotoViewer.this.parentActivity == null || PhotoViewer.this.placeProvider == null) {
                              return;
                           }

                           AlertDialog.Builder var6 = new AlertDialog.Builder(PhotoViewer.this.parentActivity);
                           String var3 = PhotoViewer.this.placeProvider.getDeleteMessageString();
                           if (var3 != null) {
                              var6.setMessage(var3);
                           } else if (PhotoViewer.this.currentMessageObject != null && PhotoViewer.this.currentMessageObject.isVideo()) {
                              var6.setMessage(LocaleController.formatString("AreYouSureDeleteVideo", 2131558693));
                           } else if (PhotoViewer.this.currentMessageObject != null && PhotoViewer.this.currentMessageObject.isGif()) {
                              var6.setMessage(LocaleController.formatString("AreYouSureDeleteGIF", 2131558684));
                           } else {
                              var6.setMessage(LocaleController.formatString("AreYouSureDeletePhoto", 2131558685));
                           }

                           var6.setTitle(LocaleController.getString("AppName", 2131558635));
                           boolean[] var7 = new boolean[1];
                           if (PhotoViewer.this.currentMessageObject != null) {
                              var1 = (int)PhotoViewer.this.currentMessageObject.getDialogId();
                              if (var1 != 0) {
                                 TLRPC.User var12;
                                 if (var1 > 0) {
                                    var12 = MessagesController.getInstance(PhotoViewer.this.currentAccount).getUser(var1);
                                    var5 = null;
                                 } else {
                                    var5 = MessagesController.getInstance(PhotoViewer.this.currentAccount).getChat(-var1);
                                    var12 = null;
                                 }

                                 if (var12 != null || !ChatObject.isChannel(var5)) {
                                    var11 = ConnectionsManager.getInstance(PhotoViewer.this.currentAccount).getCurrentTime();
                                    if (var12 != null) {
                                       var1 = MessagesController.getInstance(PhotoViewer.this.currentAccount).revokeTimePmLimit;
                                    } else {
                                       var1 = MessagesController.getInstance(PhotoViewer.this.currentAccount).revokeTimeLimit;
                                    }

                                    if ((var12 != null && var12.id != UserConfig.getInstance(PhotoViewer.this.currentAccount).getClientUserId() || var5 != null) && (PhotoViewer.this.currentMessageObject.messageOwner.action == null || PhotoViewer.this.currentMessageObject.messageOwner.action instanceof TLRPC.TL_messageActionEmpty) && PhotoViewer.this.currentMessageObject.isOut() && var11 - PhotoViewer.this.currentMessageObject.messageOwner.date <= var1) {
                                       FrameLayout var8 = new FrameLayout(PhotoViewer.this.parentActivity);
                                       CheckBoxCell var9 = new CheckBoxCell(PhotoViewer.this.parentActivity, 1);
                                       var9.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                                       if (var5 != null) {
                                          var9.setText(LocaleController.getString("DeleteForAll", 2131559243), "", false, false);
                                       } else {
                                          var9.setText(LocaleController.formatString("DeleteForUser", 2131559244, UserObject.getFirstName(var12)), "", false, false);
                                       }

                                       if (LocaleController.isRTL) {
                                          var1 = AndroidUtilities.dp(16.0F);
                                       } else {
                                          var1 = AndroidUtilities.dp(8.0F);
                                       }

                                       if (LocaleController.isRTL) {
                                          var11 = AndroidUtilities.dp(8.0F);
                                       } else {
                                          var11 = AndroidUtilities.dp(16.0F);
                                       }

                                       var9.setPadding(var1, 0, var11, 0);
                                       var8.addView(var9, LayoutHelper.createFrame(-1, 48.0F, 51, 0.0F, 0.0F, 0.0F, 0.0F));
                                       var9.setOnClickListener(new _$$Lambda$PhotoViewer$7$PnoO3dhBjKU061H6V2Ybr5NYtAo(var7));
                                       var6.setView(var8);
                                    }
                                 }
                              }
                           }

                           var6.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PhotoViewer$7$It5atnO7wprrf4dhdkFyC0mozx4(this, var7));
                           var6.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                           PhotoViewer.this.showAlertDialog(var6);
                        } else if (var1 == 10) {
                           PhotoViewer.this.onSharePressed();
                        } else if (var1 == 11) {
                           try {
                              AndroidUtilities.openForView(PhotoViewer.this.currentMessageObject, PhotoViewer.this.parentActivity);
                              PhotoViewer.this.closePhoto(false, false);
                           } catch (Exception var10) {
                              FileLog.e((Throwable)var10);
                           }
                        } else if (var1 == 13) {
                           if (PhotoViewer.this.parentActivity == null || PhotoViewer.this.currentMessageObject == null || PhotoViewer.this.currentMessageObject.messageOwner.media == null || PhotoViewer.this.currentMessageObject.messageOwner.media.photo == null) {
                              return;
                           }

                           (new StickersAlert(PhotoViewer.this.parentActivity, PhotoViewer.this.currentMessageObject, PhotoViewer.this.currentMessageObject.messageOwner.media.photo)).show();
                        } else if (var1 == 5) {
                           if (PhotoViewer.this.pipItem.getAlpha() != 1.0F) {
                              return;
                           }

                           PhotoViewer.this.switchToPip();
                        } else if (var1 == 7) {
                           if (PhotoViewer.this.currentMessageObject == null) {
                              return;
                           }

                           FileLoader.getInstance(PhotoViewer.this.currentAccount).cancelLoadFile(PhotoViewer.this.currentMessageObject.getDocument());
                           PhotoViewer.this.releasePlayer(false);
                           PhotoViewer.this.bottomLayout.setTag(1);
                           PhotoViewer.this.bottomLayout.setVisibility(0);
                        }
                     }
                  }
               } else {
                  if (PhotoViewer.this.needCaptionLayout && (PhotoViewer.this.captionEditText.isPopupShowing() || PhotoViewer.this.captionEditText.isKeyboardVisible())) {
                     PhotoViewer.this.closeCaptionEnter(false);
                     return;
                  }

                  PhotoViewer.this.closePhoto(true, false);
               }

            }
         });
         ActionBarMenu var8 = this.actionBar.createMenu();
         this.masksItem = var8.addItem(13, 2131165645);
         this.pipItem = var8.addItem(5, 2131165445);
         this.sendItem = var8.addItem(3, 2131165627);
         this.menuItem = var8.addItem(0, 2131165416);
         this.menuItem.addSubItem(11, 2131165649, LocaleController.getString("OpenInExternalApp", 2131560116)).setColors(-328966, -328966);
         this.menuItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131558443));
         this.allMediaItem = this.menuItem.addSubItem(2, 2131165646, LocaleController.getString("ShowAllMedia", 2131560778));
         this.allMediaItem.setColors(-328966, -328966);
         this.menuItem.addSubItem(4, 2131165647, LocaleController.getString("ShowInChat", 2131560780)).setColors(-328966, -328966);
         this.menuItem.addSubItem(10, 2131165671, LocaleController.getString("ShareFile", 2131560748)).setColors(-328966, -328966);
         this.menuItem.addSubItem(1, 2131165628, LocaleController.getString("SaveToGallery", 2131560630)).setColors(-328966, -328966);
         this.menuItem.addSubItem(6, 2131165623, LocaleController.getString("Delete", 2131559227)).setColors(-328966, -328966);
         this.menuItem.addSubItem(7, 2131165617, LocaleController.getString("StopDownload", 2131560822)).setColors(-328966, -328966);
         this.menuItem.redrawPopup(-115203550);
         this.sendItem.setContentDescription(LocaleController.getString("Forward", 2131559504));
         this.bottomLayout = new FrameLayout(this.actvityContext);
         this.bottomLayout.setBackgroundColor(2130706432);
         this.containerView.addView(this.bottomLayout, LayoutHelper.createFrame(-1, 48, 83));
         this.groupedPhotosListView = new GroupedPhotosListView(this.actvityContext);
         this.containerView.addView(this.groupedPhotosListView, LayoutHelper.createFrame(-1, 62.0F, 83, 0.0F, 0.0F, 0.0F, 48.0F));
         this.groupedPhotosListView.setDelegate(new GroupedPhotosListView.GroupedPhotosListViewDelegate() {
            public int getAvatarsDialogId() {
               return PhotoViewer.this.avatarsDialogId;
            }

            public int getCurrentAccount() {
               return PhotoViewer.this.currentAccount;
            }

            public int getCurrentIndex() {
               return PhotoViewer.this.currentIndex;
            }

            public ArrayList getImagesArr() {
               return PhotoViewer.this.imagesArr;
            }

            public ArrayList getImagesArrLocations() {
               return PhotoViewer.this.imagesArrLocations;
            }

            public ArrayList getPageBlockArr() {
               return null;
            }

            public Object getParentObject() {
               return null;
            }

            public int getSlideshowMessageId() {
               return PhotoViewer.this.slideshowMessageId;
            }

            public void setCurrentIndex(int var1) {
               PhotoViewer.this.currentIndex = -1;
               if (PhotoViewer.this.currentThumb != null) {
                  PhotoViewer.this.currentThumb.release();
                  PhotoViewer.this.currentThumb = null;
               }

               PhotoViewer.this.setImageIndex(var1, true);
            }
         });
         this.captionTextView = this.createCaptionTextView();
         this.switchCaptionTextView = this.createCaptionTextView();

         int var4;
         for(var4 = 0; var4 < 3; ++var4) {
            this.photoProgressViews[var4] = new PhotoViewer.PhotoProgressView(this.containerView.getContext(), this.containerView);
            this.photoProgressViews[var4].setBackgroundState(0, false);
         }

         this.miniProgressView = new RadialProgressView(this.actvityContext) {
            public void invalidate() {
               super.invalidate();
               if (PhotoViewer.this.containerView != null) {
                  PhotoViewer.this.containerView.invalidate();
               }

            }

            public void setAlpha(float var1) {
               super.setAlpha(var1);
               if (PhotoViewer.this.containerView != null) {
                  PhotoViewer.this.containerView.invalidate();
               }

            }
         };
         this.miniProgressView.setUseSelfAlpha(true);
         this.miniProgressView.setProgressColor(-1);
         this.miniProgressView.setSize(AndroidUtilities.dp(54.0F));
         this.miniProgressView.setBackgroundResource(2131165357);
         this.miniProgressView.setVisibility(4);
         this.miniProgressView.setAlpha(0.0F);
         this.containerView.addView(this.miniProgressView, LayoutHelper.createFrame(64, 64, 17));
         this.shareButton = new ImageView(this.containerView.getContext());
         this.shareButton.setImageResource(2131165818);
         this.shareButton.setScaleType(ScaleType.CENTER);
         this.shareButton.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
         this.bottomLayout.addView(this.shareButton, LayoutHelper.createFrame(50, -1, 53));
         this.shareButton.setOnClickListener(new _$$Lambda$PhotoViewer$jnWuEkM1mcOIlgJQ2IKC_gla8zE(this));
         this.shareButton.setContentDescription(LocaleController.getString("ShareFile", 2131560748));
         this.nameTextView = new TextView(this.containerView.getContext());
         this.nameTextView.setTextSize(1, 14.0F);
         this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.nameTextView.setSingleLine(true);
         this.nameTextView.setMaxLines(1);
         this.nameTextView.setEllipsize(TruncateAt.END);
         this.nameTextView.setTextColor(-1);
         this.nameTextView.setGravity(3);
         this.bottomLayout.addView(this.nameTextView, LayoutHelper.createFrame(-1, -2.0F, 51, 16.0F, 5.0F, 60.0F, 0.0F));
         this.dateTextView = new TextView(this.containerView.getContext());
         this.dateTextView.setTextSize(1, 13.0F);
         this.dateTextView.setSingleLine(true);
         this.dateTextView.setMaxLines(1);
         this.dateTextView.setEllipsize(TruncateAt.END);
         this.dateTextView.setTextColor(-1);
         this.dateTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.dateTextView.setGravity(3);
         this.bottomLayout.addView(this.dateTextView, LayoutHelper.createFrame(-1, -2.0F, 51, 16.0F, 25.0F, 50.0F, 0.0F));
         this.createVideoControlsInterface();
         this.progressView = new RadialProgressView(this.parentActivity);
         this.progressView.setProgressColor(-1);
         this.progressView.setBackgroundResource(2131165357);
         this.progressView.setVisibility(4);
         this.containerView.addView(this.progressView, LayoutHelper.createFrame(54, 54, 17));
         this.qualityPicker = new PickerBottomLayoutViewer(this.parentActivity);
         this.qualityPicker.setBackgroundColor(2130706432);
         this.qualityPicker.updateSelectedCount(0, false);
         this.qualityPicker.setTranslationY((float)AndroidUtilities.dp(120.0F));
         this.qualityPicker.doneButton.setText(LocaleController.getString("Done", 2131559299).toUpperCase());
         this.containerView.addView(this.qualityPicker, LayoutHelper.createFrame(-1, 48, 83));
         this.qualityPicker.cancelButton.setOnClickListener(new _$$Lambda$PhotoViewer$Ix7Bv26ggFYqzRhGVPWK6_2sNXM(this));
         this.qualityPicker.doneButton.setOnClickListener(new _$$Lambda$PhotoViewer$39sOKx3t1IYK0_BSSSDOFzpR_Xk(this));
         this.videoForwardDrawable = new VideoForwardDrawable();
         this.videoForwardDrawable.setDelegate(new VideoForwardDrawable.VideoForwardDrawableDelegate() {
            public void invalidate() {
               PhotoViewer.this.containerView.invalidate();
            }

            public void onAnimationEnd() {
            }
         });
         this.qualityChooseView = new PhotoViewer.QualityChooseView(this.parentActivity);
         this.qualityChooseView.setTranslationY((float)AndroidUtilities.dp(120.0F));
         this.qualityChooseView.setVisibility(4);
         this.qualityChooseView.setBackgroundColor(2130706432);
         this.containerView.addView(this.qualityChooseView, LayoutHelper.createFrame(-1, 70.0F, 83, 0.0F, 0.0F, 0.0F, 48.0F));
         this.pickerView = new FrameLayout(this.actvityContext) {
            public boolean dispatchTouchEvent(MotionEvent var1) {
               boolean var2;
               if (PhotoViewer.this.bottomTouchEnabled && super.dispatchTouchEvent(var1)) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               return var2;
            }

            public boolean onInterceptTouchEvent(MotionEvent var1) {
               boolean var2;
               if (PhotoViewer.this.bottomTouchEnabled && super.onInterceptTouchEvent(var1)) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               return var2;
            }

            public boolean onTouchEvent(MotionEvent var1) {
               boolean var2;
               if (PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(var1)) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               return var2;
            }
         };
         this.pickerView.setBackgroundColor(2130706432);
         this.containerView.addView(this.pickerView, LayoutHelper.createFrame(-1, -2, 83));
         this.videoTimelineView = new VideoTimelinePlayView(this.parentActivity);
         this.videoTimelineView.setDelegate(new VideoTimelinePlayView.VideoTimelineViewDelegate() {
            public void didStartDragging() {
            }

            public void didStopDragging() {
            }

            public void onLeftProgressChanged(float var1) {
               if (PhotoViewer.this.videoPlayer != null) {
                  if (PhotoViewer.this.videoPlayer.isPlaying()) {
                     PhotoViewer.this.videoPlayer.pause();
                     PhotoViewer.this.containerView.invalidate();
                  }

                  PhotoViewer.this.videoPlayer.seekTo((long)((int)(PhotoViewer.this.videoDuration * var1)));
                  PhotoViewer.this.videoPlayerSeekbar.setProgress(0.0F);
                  PhotoViewer.this.videoTimelineView.setProgress(0.0F);
                  PhotoViewer.this.updateVideoInfo();
               }
            }

            public void onPlayProgressChanged(float var1) {
               if (PhotoViewer.this.videoPlayer != null) {
                  PhotoViewer.this.videoPlayer.seekTo((long)((int)(PhotoViewer.this.videoDuration * var1)));
               }
            }

            public void onRightProgressChanged(float var1) {
               if (PhotoViewer.this.videoPlayer != null) {
                  if (PhotoViewer.this.videoPlayer.isPlaying()) {
                     PhotoViewer.this.videoPlayer.pause();
                     PhotoViewer.this.containerView.invalidate();
                  }

                  PhotoViewer.this.videoPlayer.seekTo((long)((int)(PhotoViewer.this.videoDuration * var1)));
                  PhotoViewer.this.videoPlayerSeekbar.setProgress(0.0F);
                  PhotoViewer.this.videoTimelineView.setProgress(0.0F);
                  PhotoViewer.this.updateVideoInfo();
               }
            }
         });
         this.pickerView.addView(this.videoTimelineView, LayoutHelper.createFrame(-1, 58.0F, 51, 0.0F, 8.0F, 0.0F, 88.0F));
         this.pickerViewSendButton = new ImageView(this.parentActivity);
         this.pickerViewSendButton.setScaleType(ScaleType.CENTER);
         Drawable var9 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0F), -10043398, -10043398);
         this.pickerViewSendButton.setBackgroundDrawable(var9);
         this.pickerViewSendButton.setColorFilter(new PorterDuffColorFilter(-1, Mode.MULTIPLY));
         this.pickerViewSendButton.setPadding(AndroidUtilities.dp(4.0F), 0, 0, 0);
         this.pickerViewSendButton.setImageResource(2131165468);
         this.containerView.addView(this.pickerViewSendButton, LayoutHelper.createFrame(56, 56.0F, 85, 0.0F, 0.0F, 14.0F, 14.0F));
         this.pickerViewSendButton.setContentDescription(LocaleController.getString("Send", 2131560685));
         this.pickerViewSendButton.setOnClickListener(new _$$Lambda$PhotoViewer$Ygm2nkEIz52xt8vsvpsgSvg6BwY(this));
         LinearLayout var5 = new LinearLayout(this.parentActivity);
         var5.setOrientation(0);
         this.pickerView.addView(var5, LayoutHelper.createFrame(-2, 48.0F, 81, 0.0F, 0.0F, 34.0F, 0.0F));
         this.cropItem = new ImageView(this.parentActivity);
         this.cropItem.setScaleType(ScaleType.CENTER);
         this.cropItem.setImageResource(2131165742);
         this.cropItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
         var5.addView(this.cropItem, LayoutHelper.createLinear(70, 48));
         this.cropItem.setOnClickListener(new _$$Lambda$PhotoViewer$eMxgxyAI6SdVhY11OBq7nV_Jqtc(this));
         this.cropItem.setContentDescription(LocaleController.getString("CropImage", 2131559176));
         this.rotateItem = new ImageView(this.parentActivity);
         this.rotateItem.setScaleType(ScaleType.CENTER);
         this.rotateItem.setImageResource(2131165885);
         this.rotateItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
         var5.addView(this.rotateItem, LayoutHelper.createLinear(70, 48));
         this.rotateItem.setOnClickListener(new _$$Lambda$PhotoViewer$S84XUzehocAn_E9EWBFOsOwSJTw(this));
         this.rotateItem.setContentDescription(LocaleController.getString("AccDescrRotate", 2131558466));
         this.paintItem = new ImageView(this.parentActivity);
         this.paintItem.setScaleType(ScaleType.CENTER);
         this.paintItem.setImageResource(2131165745);
         this.paintItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
         var5.addView(this.paintItem, LayoutHelper.createLinear(70, 48));
         this.paintItem.setOnClickListener(new _$$Lambda$PhotoViewer$Nm0i__eT2GlY_xQALMRc26gc1G0(this));
         this.paintItem.setContentDescription(LocaleController.getString("AccDescrPhotoEditor", 2131558456));
         this.compressItem = new ImageView(this.parentActivity);
         this.compressItem.setTag(1);
         this.compressItem.setScaleType(ScaleType.CENTER);
         this.compressItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
         this.selectedCompression = MessagesController.getGlobalMainSettings().getInt("compress_video2", 1);
         var4 = this.selectedCompression;
         if (var4 <= 0) {
            this.compressItem.setImageResource(2131165896);
         } else if (var4 == 1) {
            this.compressItem.setImageResource(2131165897);
         } else if (var4 == 2) {
            this.compressItem.setImageResource(2131165898);
         } else if (var4 == 3) {
            this.compressItem.setImageResource(2131165899);
         } else if (var4 == 4) {
            this.compressItem.setImageResource(2131165895);
         }

         var5.addView(this.compressItem, LayoutHelper.createLinear(70, 48));
         this.compressItem.setOnClickListener(new _$$Lambda$PhotoViewer$ZNbglMdvMBz7fic2b_SNO94Evbk(this));
         ImageView var10 = this.compressItem;
         StringBuilder var11 = new StringBuilder();
         var11.append(LocaleController.getString("AccDescrVideoQuality", 2131558482));
         var11.append(", ");
         var4 = Math.max(0, this.selectedCompression);
         var11.append((new String[]{"240", "360", "480", "720", "1080"})[var4]);
         var10.setContentDescription(var11.toString());
         this.muteItem = new ImageView(this.parentActivity);
         this.muteItem.setScaleType(ScaleType.CENTER);
         this.muteItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
         var5.addView(this.muteItem, LayoutHelper.createLinear(70, 48));
         this.muteItem.setOnClickListener(new _$$Lambda$PhotoViewer$MIXMx0jfsMSq1oOpVJ4spUPP5Ss(this));
         this.cameraItem = new ImageView(this.parentActivity);
         this.cameraItem.setScaleType(ScaleType.CENTER);
         this.cameraItem.setImageResource(2131165741);
         this.cameraItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
         this.cameraItem.setContentDescription(LocaleController.getString("AccDescrTakeMorePics", 2131558479));
         this.containerView.addView(this.cameraItem, LayoutHelper.createFrame(48, 48.0F, 85, 0.0F, 0.0F, 16.0F, 0.0F));
         this.cameraItem.setOnClickListener(new _$$Lambda$PhotoViewer$FkVTKWzor1OYhcy_PdaAovVJgkQ(this));
         this.tuneItem = new ImageView(this.parentActivity);
         this.tuneItem.setScaleType(ScaleType.CENTER);
         this.tuneItem.setImageResource(2131165751);
         this.tuneItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
         var5.addView(this.tuneItem, LayoutHelper.createLinear(70, 48));
         this.tuneItem.setOnClickListener(new _$$Lambda$PhotoViewer$BtjFUBdRHmVM1bQF4oFTkkfFVOY(this));
         this.tuneItem.setContentDescription(LocaleController.getString("AccDescrPhotoAdjust", 2131558455));
         this.timeItem = new ImageView(this.parentActivity);
         this.timeItem.setScaleType(ScaleType.CENTER);
         this.timeItem.setImageResource(2131165750);
         this.timeItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
         this.timeItem.setContentDescription(LocaleController.getString("SetTimer", 2131560737));
         var5.addView(this.timeItem, LayoutHelper.createLinear(70, 48));
         this.timeItem.setOnClickListener(new _$$Lambda$PhotoViewer$5tqvSuoNJkycbaIOeX7zem0z4z4(this));
         this.editorDoneLayout = new PickerBottomLayoutViewer(this.actvityContext);
         this.editorDoneLayout.setBackgroundColor(2130706432);
         this.editorDoneLayout.updateSelectedCount(0, false);
         this.editorDoneLayout.setVisibility(8);
         this.containerView.addView(this.editorDoneLayout, LayoutHelper.createFrame(-1, 48, 83));
         this.editorDoneLayout.cancelButton.setOnClickListener(new _$$Lambda$PhotoViewer$jVRNS_vT_efmfMNpUJWPuD_BH_s(this));
         this.editorDoneLayout.doneButton.setOnClickListener(new _$$Lambda$PhotoViewer$lzLoax3UVNdU6xIvKXGhOoY4Vc8(this));
         this.resetButton = new TextView(this.actvityContext);
         this.resetButton.setVisibility(8);
         this.resetButton.setTextSize(1, 14.0F);
         this.resetButton.setTextColor(-1);
         this.resetButton.setGravity(17);
         this.resetButton.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
         this.resetButton.setPadding(AndroidUtilities.dp(20.0F), 0, AndroidUtilities.dp(20.0F), 0);
         this.resetButton.setText(LocaleController.getString("Reset", 2131559178).toUpperCase());
         this.resetButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.editorDoneLayout.addView(this.resetButton, LayoutHelper.createFrame(-2, -1, 49));
         this.resetButton.setOnClickListener(new _$$Lambda$PhotoViewer$jYKD2cxo5_0tGvpCTvsWOo0KirI(this));
         this.gestureDetector = new GestureDetector(this.containerView.getContext(), this);
         this.setDoubleTapEnabled(true);
         _$$Lambda$PhotoViewer$8d9Z5wt8PBRYWSSOdeX0wadZFXk var12 = new _$$Lambda$PhotoViewer$8d9Z5wt8PBRYWSSOdeX0wadZFXk(this);
         this.centerImage.setParentView(this.containerView);
         this.centerImage.setCrossfadeAlpha((byte)2);
         this.centerImage.setInvalidateAll(true);
         this.centerImage.setDelegate(var12);
         this.leftImage.setParentView(this.containerView);
         this.leftImage.setCrossfadeAlpha((byte)2);
         this.leftImage.setInvalidateAll(true);
         this.leftImage.setDelegate(var12);
         this.rightImage.setParentView(this.containerView);
         this.rightImage.setCrossfadeAlpha((byte)2);
         this.rightImage.setInvalidateAll(true);
         this.rightImage.setDelegate(var12);
         var4 = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
         this.checkImageView = new CheckBox(this.containerView.getContext(), 2131165814) {
            public boolean onTouchEvent(MotionEvent var1) {
               boolean var2;
               if (PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(var1)) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               return var2;
            }
         };
         this.checkImageView.setDrawBackground(true);
         this.checkImageView.setHasBorder(true);
         this.checkImageView.setSize(40);
         this.checkImageView.setCheckOffset(AndroidUtilities.dp(1.0F));
         this.checkImageView.setColor(-10043398, -1);
         this.checkImageView.setVisibility(8);
         PhotoViewer.FrameLayoutDrawer var13 = this.containerView;
         CheckBox var14 = this.checkImageView;
         float var6;
         if (var4 != 3 && var4 != 1) {
            var6 = 68.0F;
         } else {
            var6 = 58.0F;
         }

         var13.addView(var14, LayoutHelper.createFrame(40, 40.0F, 53, 0.0F, var6, 10.0F, 0.0F));
         android.widget.FrameLayout.LayoutParams var16;
         if (VERSION.SDK_INT >= 21) {
            var16 = (android.widget.FrameLayout.LayoutParams)this.checkImageView.getLayoutParams();
            var16.topMargin += AndroidUtilities.statusBarHeight;
         }

         this.checkImageView.setOnClickListener(new _$$Lambda$PhotoViewer$MNTsOUmDycPlm_15mLZjYpUg6Ec(this));
         this.photosCounterView = new PhotoViewer.CounterView(this.parentActivity);
         PhotoViewer.FrameLayoutDrawer var18 = this.containerView;
         PhotoViewer.CounterView var15 = this.photosCounterView;
         if (var4 != 3 && var4 != 1) {
            var6 = 68.0F;
         } else {
            var6 = 58.0F;
         }

         var18.addView(var15, LayoutHelper.createFrame(40, 40.0F, 53, 0.0F, var6, 66.0F, 0.0F));
         if (VERSION.SDK_INT >= 21) {
            var16 = (android.widget.FrameLayout.LayoutParams)this.photosCounterView.getLayoutParams();
            var16.topMargin += AndroidUtilities.statusBarHeight;
         }

         this.photosCounterView.setOnClickListener(new _$$Lambda$PhotoViewer$v61rlDfXP7XMVPYKHGswQHaXc_E(this));
         this.selectedPhotosListView = new RecyclerListView(this.parentActivity);
         this.selectedPhotosListView.setVisibility(8);
         this.selectedPhotosListView.setAlpha(0.0F);
         this.selectedPhotosListView.setTranslationY((float)(-AndroidUtilities.dp(10.0F)));
         this.selectedPhotosListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            public void getItemOffsets(android.graphics.Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
               int var5 = var3.getChildAdapterPosition(var2);
               if (var2 instanceof PhotoPickerPhotoCell && var5 == 0) {
                  var1.left = AndroidUtilities.dp(3.0F);
               } else {
                  var1.left = 0;
               }

               var1.right = AndroidUtilities.dp(3.0F);
            }
         });
         ((DefaultItemAnimator)this.selectedPhotosListView.getItemAnimator()).setDelayAnimations(false);
         this.selectedPhotosListView.setBackgroundColor(2130706432);
         this.selectedPhotosListView.setPadding(0, AndroidUtilities.dp(3.0F), 0, AndroidUtilities.dp(3.0F));
         this.selectedPhotosListView.setLayoutManager(new LinearLayoutManager(this.parentActivity, 0, false) {
            public void smoothScrollToPosition(RecyclerView var1, RecyclerView.State var2, int var3) {
               LinearSmoothScrollerEnd var4 = new LinearSmoothScrollerEnd(var1.getContext());
               var4.setTargetPosition(var3);
               this.startSmoothScroll(var4);
            }
         });
         RecyclerListView var17 = this.selectedPhotosListView;
         PhotoViewer.ListAdapter var19 = new PhotoViewer.ListAdapter(this.parentActivity);
         this.selectedPhotosAdapter = var19;
         var17.setAdapter(var19);
         this.containerView.addView(this.selectedPhotosListView, LayoutHelper.createFrame(-1, 88, 51));
         this.selectedPhotosListView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$PhotoViewer$_FS1GaFBb1N0jAMDctH7XmfNRmw(this)));
         this.captionEditText = new PhotoViewerCaptionEnterView(this.actvityContext, this.containerView, this.windowView) {
            public boolean dispatchTouchEvent(MotionEvent var1) {
               boolean var2 = false;
               boolean var3 = var2;

               boolean var4;
               try {
                  if (PhotoViewer.this.bottomTouchEnabled) {
                     return var3;
                  }

                  var4 = super.dispatchTouchEvent(var1);
               } catch (Exception var5) {
                  FileLog.e((Throwable)var5);
                  return false;
               }

               var3 = var2;
               if (var4) {
                  var3 = true;
               }

               return var3;
            }

            public boolean onInterceptTouchEvent(MotionEvent var1) {
               boolean var2 = false;
               boolean var3 = var2;

               boolean var4;
               try {
                  if (PhotoViewer.this.bottomTouchEnabled) {
                     return var3;
                  }

                  var4 = super.onInterceptTouchEvent(var1);
               } catch (Exception var5) {
                  FileLog.e((Throwable)var5);
                  return false;
               }

               var3 = var2;
               if (var4) {
                  var3 = true;
               }

               return var3;
            }

            public boolean onTouchEvent(MotionEvent var1) {
               boolean var2;
               if (!PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(var1)) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               return var2;
            }
         };
         this.captionEditText.setDelegate(new PhotoViewerCaptionEnterView.PhotoViewerCaptionEnterViewDelegate() {
            public void onCaptionEnter() {
               PhotoViewer.this.closeCaptionEnter(true);
            }

            public void onTextChanged(CharSequence var1) {
               if (PhotoViewer.this.mentionsAdapter != null && PhotoViewer.this.captionEditText != null && PhotoViewer.this.parentChatActivity != null && var1 != null) {
                  PhotoViewer.this.mentionsAdapter.searchUsernameOrHashtag(var1.toString(), PhotoViewer.this.captionEditText.getCursorPosition(), PhotoViewer.this.parentChatActivity.messages, false);
               }

            }

            public void onWindowSizeChanged(int var1) {
               int var2 = Math.min(3, PhotoViewer.this.mentionsAdapter.getItemCount());
               byte var3;
               if (PhotoViewer.this.mentionsAdapter.getItemCount() > 3) {
                  var3 = 18;
               } else {
                  var3 = 0;
               }

               int var4 = AndroidUtilities.dp((float)(var2 * 36 + var3));
               if (var1 - ActionBar.getCurrentActionBarHeight() * 2 < var4) {
                  PhotoViewer.this.allowMentions = false;
                  if (PhotoViewer.this.mentionListView != null && PhotoViewer.this.mentionListView.getVisibility() == 0) {
                     PhotoViewer.this.mentionListView.setVisibility(4);
                  }
               } else {
                  PhotoViewer.this.allowMentions = true;
                  if (PhotoViewer.this.mentionListView != null && PhotoViewer.this.mentionListView.getVisibility() == 4) {
                     PhotoViewer.this.mentionListView.setVisibility(0);
                  }
               }

            }
         });
         if (VERSION.SDK_INT >= 19) {
            this.captionEditText.setImportantForAccessibility(4);
         }

         this.containerView.addView(this.captionEditText, LayoutHelper.createFrame(-1, -2, 83));
         this.mentionListView = new RecyclerListView(this.actvityContext) {
            public boolean dispatchTouchEvent(MotionEvent var1) {
               boolean var2;
               if (!PhotoViewer.this.bottomTouchEnabled && super.dispatchTouchEvent(var1)) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               return var2;
            }

            public boolean onInterceptTouchEvent(MotionEvent var1) {
               boolean var2;
               if (!PhotoViewer.this.bottomTouchEnabled && super.onInterceptTouchEvent(var1)) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               return var2;
            }

            public boolean onTouchEvent(MotionEvent var1) {
               boolean var2;
               if (!PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(var1)) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               return var2;
            }
         };
         this.mentionListView.setTag(5);
         this.mentionLayoutManager = new LinearLayoutManager(this.actvityContext) {
            public boolean supportsPredictiveItemAnimations() {
               return false;
            }
         };
         this.mentionLayoutManager.setOrientation(1);
         this.mentionListView.setLayoutManager(this.mentionLayoutManager);
         this.mentionListView.setBackgroundColor(2130706432);
         this.mentionListView.setVisibility(8);
         this.mentionListView.setClipToPadding(true);
         this.mentionListView.setOverScrollMode(2);
         this.containerView.addView(this.mentionListView, LayoutHelper.createFrame(-1, 110, 83));
         var17 = this.mentionListView;
         MentionsAdapter var20 = new MentionsAdapter(this.actvityContext, true, 0L, new MentionsAdapter.MentionsAdapterDelegate() {
            public void needChangePanelVisibility(boolean var1) {
               if (var1) {
                  android.widget.FrameLayout.LayoutParams var2 = (android.widget.FrameLayout.LayoutParams)PhotoViewer.this.mentionListView.getLayoutParams();
                  int var3 = Math.min(3, PhotoViewer.this.mentionsAdapter.getItemCount());
                  byte var4;
                  if (PhotoViewer.this.mentionsAdapter.getItemCount() > 3) {
                     var4 = 18;
                  } else {
                     var4 = 0;
                  }

                  float var5 = (float)(var3 * 36 + var4);
                  var2.height = AndroidUtilities.dp(var5);
                  var2.topMargin = -AndroidUtilities.dp(var5);
                  PhotoViewer.this.mentionListView.setLayoutParams(var2);
                  if (PhotoViewer.this.mentionListAnimation != null) {
                     PhotoViewer.this.mentionListAnimation.cancel();
                     PhotoViewer.this.mentionListAnimation = null;
                  }

                  if (PhotoViewer.this.mentionListView.getVisibility() == 0) {
                     PhotoViewer.this.mentionListView.setAlpha(1.0F);
                     return;
                  }

                  PhotoViewer.this.mentionLayoutManager.scrollToPositionWithOffset(0, 10000);
                  if (PhotoViewer.this.allowMentions) {
                     PhotoViewer.this.mentionListView.setVisibility(0);
                     PhotoViewer.this.mentionListAnimation = new AnimatorSet();
                     PhotoViewer.this.mentionListAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(PhotoViewer.this.mentionListView, View.ALPHA, new float[]{0.0F, 1.0F})});
                     PhotoViewer.this.mentionListAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator var1) {
                           if (PhotoViewer.this.mentionListAnimation != null && PhotoViewer.this.mentionListAnimation.equals(var1)) {
                              PhotoViewer.this.mentionListAnimation = null;
                           }

                        }
                     });
                     PhotoViewer.this.mentionListAnimation.setDuration(200L);
                     PhotoViewer.this.mentionListAnimation.start();
                  } else {
                     PhotoViewer.this.mentionListView.setAlpha(1.0F);
                     PhotoViewer.this.mentionListView.setVisibility(4);
                  }
               } else {
                  if (PhotoViewer.this.mentionListAnimation != null) {
                     PhotoViewer.this.mentionListAnimation.cancel();
                     PhotoViewer.this.mentionListAnimation = null;
                  }

                  if (PhotoViewer.this.mentionListView.getVisibility() == 8) {
                     return;
                  }

                  if (PhotoViewer.this.allowMentions) {
                     PhotoViewer.this.mentionListAnimation = new AnimatorSet();
                     PhotoViewer.this.mentionListAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(PhotoViewer.this.mentionListView, View.ALPHA, new float[]{0.0F})});
                     PhotoViewer.this.mentionListAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator var1) {
                           if (PhotoViewer.this.mentionListAnimation != null && PhotoViewer.this.mentionListAnimation.equals(var1)) {
                              PhotoViewer.this.mentionListView.setVisibility(8);
                              PhotoViewer.this.mentionListAnimation = null;
                           }

                        }
                     });
                     PhotoViewer.this.mentionListAnimation.setDuration(200L);
                     PhotoViewer.this.mentionListAnimation.start();
                  } else {
                     PhotoViewer.this.mentionListView.setVisibility(8);
                  }
               }

            }

            public void onContextClick(TLRPC.BotInlineResult var1) {
            }

            public void onContextSearch(boolean var1) {
            }
         });
         this.mentionsAdapter = var20;
         var17.setAdapter(var20);
         this.mentionListView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$PhotoViewer$JG3xW1rvxoBoP1ShppmfpfTlhQo(this)));
         this.mentionListView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$PhotoViewer$vduFoUyF328i_ViN04AaYIiTGtk(this)));
         if (((AccessibilityManager)this.actvityContext.getSystemService("accessibility")).isEnabled()) {
            this.playButtonAccessibilityOverlay = new View(this.actvityContext);
            this.playButtonAccessibilityOverlay.setContentDescription(LocaleController.getString("AccActionPlay", 2131558409));
            this.playButtonAccessibilityOverlay.setFocusable(true);
            this.containerView.addView(this.playButtonAccessibilityOverlay, LayoutHelper.createFrame(64, 64, 17));
         }

      }
   }

   public void setParentAlert(ChatAttachAlert var1) {
      this.parentAlert = var1;
   }

   public void setParentChatActivity(ChatActivity var1) {
      this.parentChatActivity = var1;
   }

   public void showAlertDialog(AlertDialog.Builder var1) {
      if (this.parentActivity != null) {
         try {
            if (this.visibleDialog != null) {
               this.visibleDialog.dismiss();
               this.visibleDialog = null;
            }
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

         try {
            this.visibleDialog = var1.show();
            this.visibleDialog.setCanceledOnTouchOutside(true);
            AlertDialog var2 = this.visibleDialog;
            _$$Lambda$PhotoViewer$6qHPQWdf_DnEW0pxgH_u4b0yivA var5 = new _$$Lambda$PhotoViewer$6qHPQWdf_DnEW0pxgH_u4b0yivA(this);
            var2.setOnDismissListener(var5);
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

      }
   }

   public void updateMuteButton() {
      VideoPlayer var1 = this.videoPlayer;
      if (var1 != null) {
         var1.setMute(this.muteVideo);
      }

      if (!this.videoHasAudio) {
         this.muteItem.setEnabled(false);
         this.muteItem.setClickable(false);
         this.muteItem.setAlpha(0.5F);
      } else {
         this.muteItem.setEnabled(true);
         this.muteItem.setClickable(true);
         this.muteItem.setAlpha(1.0F);
         if (this.muteVideo) {
            this.actionBar.setSubtitle((CharSequence)null);
            this.muteItem.setImageResource(2131165911);
            this.muteItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
            if (this.compressItem.getTag() != null) {
               this.compressItem.setClickable(false);
               this.compressItem.setAlpha(0.5F);
               this.compressItem.setEnabled(false);
            }

            this.videoTimelineView.setMaxProgressDiff(30000.0F / this.videoDuration);
            this.muteItem.setContentDescription(LocaleController.getString("NoSound", 2131559952));
         } else {
            this.muteItem.setColorFilter((ColorFilter)null);
            this.actionBar.setSubtitle(this.currentSubtitle);
            this.muteItem.setImageResource(2131165912);
            this.muteItem.setContentDescription(LocaleController.getString("Sound", 2131560800));
            if (this.compressItem.getTag() != null) {
               this.compressItem.setClickable(true);
               this.compressItem.setAlpha(1.0F);
               this.compressItem.setEnabled(true);
            }

            this.videoTimelineView.setMaxProgressDiff(1.0F);
         }
      }

   }

   private class BackgroundDrawable extends ColorDrawable {
      private boolean allowDrawContent;
      private Runnable drawRunnable;

      public BackgroundDrawable(int var2) {
         super(var2);
      }

      public void draw(Canvas var1) {
         super.draw(var1);
         if (this.getAlpha() != 0) {
            Runnable var2 = this.drawRunnable;
            if (var2 != null) {
               AndroidUtilities.runOnUIThread(var2);
               this.drawRunnable = null;
            }
         }

      }

      // $FF: synthetic method
      public void lambda$setAlpha$0$PhotoViewer$BackgroundDrawable() {
         if (PhotoViewer.this.parentAlert != null) {
            PhotoViewer.this.parentAlert.setAllowDrawContent(this.allowDrawContent);
         }

      }

      @Keep
      public void setAlpha(int var1) {
         if (PhotoViewer.this.parentActivity instanceof LaunchActivity) {
            boolean var2;
            if (PhotoViewer.this.isVisible && var1 == 255) {
               var2 = false;
            } else {
               var2 = true;
            }

            this.allowDrawContent = var2;
            ((LaunchActivity)PhotoViewer.this.parentActivity).drawerLayoutContainer.setAllowDrawContent(this.allowDrawContent);
            if (PhotoViewer.this.parentAlert != null) {
               if (!this.allowDrawContent) {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$BackgroundDrawable$pDidiNsUGVGYtvuU76vLRyxH30U(this), 50L);
               } else if (PhotoViewer.this.parentAlert != null) {
                  PhotoViewer.this.parentAlert.setAllowDrawContent(this.allowDrawContent);
               }
            }
         }

         super.setAlpha(var1);
      }
   }

   private class CounterView extends View {
      private int currentCount = 0;
      private int height;
      private Paint paint;
      private RectF rect;
      private float rotation;
      private StaticLayout staticLayout;
      private TextPaint textPaint = new TextPaint(1);
      private int width;

      public CounterView(Context var2) {
         super(var2);
         this.textPaint.setTextSize((float)AndroidUtilities.dp(18.0F));
         this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.textPaint.setColor(-1);
         this.paint = new Paint(1);
         this.paint.setColor(-1);
         this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
         this.paint.setStyle(Style.STROKE);
         this.paint.setStrokeJoin(Join.ROUND);
         this.rect = new RectF();
         this.setCount(0);
      }

      public float getRotationX() {
         return this.rotation;
      }

      protected void onDraw(Canvas var1) {
         int var2 = this.getMeasuredHeight() / 2;
         this.paint.setAlpha(255);
         this.rect.set((float)AndroidUtilities.dp(1.0F), (float)(var2 - AndroidUtilities.dp(14.0F)), (float)(this.getMeasuredWidth() - AndroidUtilities.dp(1.0F)), (float)(var2 + AndroidUtilities.dp(14.0F)));
         var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(15.0F), (float)AndroidUtilities.dp(15.0F), this.paint);
         if (this.staticLayout != null) {
            this.textPaint.setAlpha((int)((1.0F - this.rotation) * 255.0F));
            var1.save();
            var1.translate((float)((this.getMeasuredWidth() - this.width) / 2), (float)((this.getMeasuredHeight() - this.height) / 2) + AndroidUtilities.dpf2(0.2F) + this.rotation * (float)AndroidUtilities.dp(5.0F));
            this.staticLayout.draw(var1);
            var1.restore();
            this.paint.setAlpha((int)(this.rotation * 255.0F));
            int var3 = (int)this.rect.centerX();
            var2 = (int)((float)((int)this.rect.centerY()) - ((float)AndroidUtilities.dp(5.0F) * (1.0F - this.rotation) + (float)AndroidUtilities.dp(3.0F)));
            var1.drawLine((float)(AndroidUtilities.dp(0.5F) + var3), (float)(var2 - AndroidUtilities.dp(0.5F)), (float)(var3 - AndroidUtilities.dp(6.0F)), (float)(AndroidUtilities.dp(6.0F) + var2), this.paint);
            var1.drawLine((float)(var3 - AndroidUtilities.dp(0.5F)), (float)(var2 - AndroidUtilities.dp(0.5F)), (float)(var3 + AndroidUtilities.dp(6.0F)), (float)(var2 + AndroidUtilities.dp(6.0F)), this.paint);
         }

      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(Math.max(this.width + AndroidUtilities.dp(20.0F), AndroidUtilities.dp(30.0F)), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0F), 1073741824));
      }

      public void setCount(int var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("");
         var2.append(Math.max(1, var1));
         this.staticLayout = new StaticLayout(var2.toString(), this.textPaint, AndroidUtilities.dp(100.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         this.width = (int)Math.ceil((double)this.staticLayout.getLineWidth(0));
         this.height = this.staticLayout.getLineBottom(0);
         AnimatorSet var4 = new AnimatorSet();
         if (var1 == 0) {
            var4.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, View.SCALE_X, new float[]{0.0F}), ObjectAnimator.ofFloat(this, View.SCALE_Y, new float[]{0.0F}), ObjectAnimator.ofInt(this.paint, AnimationProperties.PAINT_ALPHA, new int[]{0}), ObjectAnimator.ofInt(this.textPaint, AnimationProperties.PAINT_ALPHA, new int[]{0})});
            var4.setInterpolator(new DecelerateInterpolator());
         } else {
            int var3 = this.currentCount;
            if (var3 == 0) {
               var4.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, View.SCALE_X, new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this, View.SCALE_Y, new float[]{0.0F, 1.0F}), ObjectAnimator.ofInt(this.paint, AnimationProperties.PAINT_ALPHA, new int[]{0, 255}), ObjectAnimator.ofInt(this.textPaint, AnimationProperties.PAINT_ALPHA, new int[]{0, 255})});
               var4.setInterpolator(new DecelerateInterpolator());
            } else if (var1 < var3) {
               var4.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, View.SCALE_X, new float[]{1.1F, 1.0F}), ObjectAnimator.ofFloat(this, View.SCALE_Y, new float[]{1.1F, 1.0F})});
               var4.setInterpolator(new OvershootInterpolator());
            } else {
               var4.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, View.SCALE_X, new float[]{0.9F, 1.0F}), ObjectAnimator.ofFloat(this, View.SCALE_Y, new float[]{0.9F, 1.0F})});
               var4.setInterpolator(new OvershootInterpolator());
            }
         }

         var4.setDuration(180L);
         var4.start();
         this.requestLayout();
         this.currentCount = var1;
      }

      @Keep
      public void setRotationX(float var1) {
         this.rotation = var1;
         this.invalidate();
      }

      @Keep
      public void setScaleX(float var1) {
         super.setScaleX(var1);
         this.invalidate();
      }
   }

   public static class EmptyPhotoViewerProvider implements PhotoViewer.PhotoViewerProvider {
      public boolean allowCaption() {
         return true;
      }

      public boolean allowGroupPhotos() {
         return true;
      }

      public boolean canCaptureMorePhotos() {
         return true;
      }

      public boolean canScrollAway() {
         return true;
      }

      public boolean cancelButtonPressed() {
         return true;
      }

      public void deleteImageAtIndex(int var1) {
      }

      public String getDeleteMessageString() {
         return null;
      }

      public int getPhotoIndex(int var1) {
         return -1;
      }

      public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3, boolean var4) {
         return null;
      }

      public int getSelectedCount() {
         return 0;
      }

      public HashMap getSelectedPhotos() {
         return null;
      }

      public ArrayList getSelectedPhotosOrder() {
         return null;
      }

      public ImageReceiver.BitmapHolder getThumbForPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3) {
         return null;
      }

      public boolean isPhotoChecked(int var1) {
         return false;
      }

      public void needAddMorePhotos() {
      }

      public boolean scaleToFill() {
         return false;
      }

      public void sendButtonPressed(int var1, VideoEditedInfo var2) {
      }

      public int setPhotoChecked(int var1, VideoEditedInfo var2) {
         return -1;
      }

      public int setPhotoUnchecked(Object var1) {
         return -1;
      }

      public void toggleGroupPhotosEnabled() {
      }

      public void updatePhotoAtIndex(int var1) {
      }

      public void willHidePhotoViewer() {
      }

      public void willSwitchFromPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3) {
      }
   }

   private class FrameLayoutDrawer extends SizeNotifierFrameLayoutPhoto {
      private Paint paint = new Paint();

      public FrameLayoutDrawer(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
         this.paint.setColor(855638016);
      }

      protected boolean drawChild(Canvas var1, View var2, long var3) {
         RecyclerListView var5 = PhotoViewer.this.mentionListView;
         boolean var6 = true;
         if (var2 != var5 && var2 != PhotoViewer.this.captionEditText) {
            if (var2 == PhotoViewer.this.cameraItem || var2 == PhotoViewer.this.pickerView || var2 == PhotoViewer.this.pickerViewSendButton || var2 == PhotoViewer.this.captionTextView || PhotoViewer.this.muteItem.getVisibility() == 0 && var2 == PhotoViewer.this.bottomLayout) {
               int var7;
               if (this.getKeyboardHeight() <= AndroidUtilities.dp(20.0F) && !AndroidUtilities.isInMultiwindow) {
                  var7 = PhotoViewer.this.captionEditText.getEmojiPadding();
               } else {
                  var7 = 0;
               }

               if (PhotoViewer.this.captionEditText.isPopupShowing() || AndroidUtilities.usingHardwareInput && PhotoViewer.this.captionEditText.getTag() != null || this.getKeyboardHeight() > 0 || var7 != 0) {
                  PhotoViewer.this.bottomTouchEnabled = false;
                  return false;
               }

               PhotoViewer.this.bottomTouchEnabled = true;
            } else if (var2 != PhotoViewer.this.checkImageView && var2 != PhotoViewer.this.photosCounterView) {
               if (var2 == PhotoViewer.this.miniProgressView) {
                  return false;
               }
            } else {
               if (PhotoViewer.this.captionEditText.getTag() != null) {
                  PhotoViewer.this.bottomTouchEnabled = false;
                  return false;
               }

               PhotoViewer.this.bottomTouchEnabled = true;
            }
         } else if (!PhotoViewer.this.captionEditText.isPopupShowing() && PhotoViewer.this.captionEditText.getEmojiPadding() == 0 && (AndroidUtilities.usingHardwareInput && PhotoViewer.this.captionEditText.getTag() == null || this.getKeyboardHeight() == 0)) {
            return false;
         }

         label58: {
            boolean var8;
            try {
               if (var2 == PhotoViewer.this.aspectRatioFrameLayout) {
                  break label58;
               }

               var8 = super.drawChild(var1, var2, var3);
            } catch (Throwable var9) {
               return var6;
            }

            if (var8) {
               return var6;
            }
         }

         var6 = false;
         return var6;
      }

      protected void onDraw(Canvas var1) {
         PhotoViewer.this.onDraw(var1);
         if (VERSION.SDK_INT >= 21 && AndroidUtilities.statusBarHeight != 0 && PhotoViewer.this.actionBar != null) {
            this.paint.setAlpha((int)(PhotoViewer.this.actionBar.getAlpha() * 255.0F * 0.2F));
            var1.drawRect(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)AndroidUtilities.statusBarHeight, this.paint);
            this.paint.setAlpha((int)(PhotoViewer.this.actionBar.getAlpha() * 255.0F * 0.498F));
            if (this.getPaddingRight() > 0) {
               var1.drawRect((float)(this.getMeasuredWidth() - this.getPaddingRight()), 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.paint);
            }

            if (this.getPaddingLeft() > 0) {
               var1.drawRect(0.0F, 0.0F, (float)this.getPaddingLeft(), (float)this.getMeasuredHeight(), this.paint);
            }
         }

      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         int var6 = this.getChildCount();
         int var7 = this.getKeyboardHeight();
         int var8 = AndroidUtilities.dp(20.0F);
         int var9 = 0;
         int var10;
         if (var7 <= var8 && !AndroidUtilities.isInMultiwindow) {
            var10 = PhotoViewer.this.captionEditText.getEmojiPadding();
         } else {
            var10 = 0;
         }

         for(; var9 < var6; ++var9) {
            View var11 = this.getChildAt(var9);
            if (var11.getVisibility() != 8) {
               int var12;
               if (var11 == PhotoViewer.this.aspectRatioFrameLayout) {
                  var12 = var2;
                  var8 = var4;
                  var7 = var5;
               } else {
                  var12 = var2 + this.getPaddingLeft();
                  var8 = var4 - this.getPaddingRight();
                  var7 = var5 - this.getPaddingBottom();
               }

               android.widget.FrameLayout.LayoutParams var13 = (android.widget.FrameLayout.LayoutParams)var11.getLayoutParams();
               int var14 = var11.getMeasuredWidth();
               int var15 = var11.getMeasuredHeight();
               int var16 = var13.gravity;
               int var17 = var16;
               if (var16 == -1) {
                  var17 = 51;
               }

               label107: {
                  var16 = var17 & 112;
                  var17 = var17 & 7 & 7;
                  if (var17 != 1) {
                     if (var17 != 5) {
                        var17 = var13.leftMargin;
                        break label107;
                     }

                     var8 = var8 - var12 - var14;
                     var17 = var13.rightMargin;
                  } else {
                     var8 = (var8 - var12 - var14) / 2 + var13.leftMargin;
                     var17 = var13.rightMargin;
                  }

                  var17 = var8 - var17;
               }

               label101: {
                  if (var16 != 16) {
                     if (var16 == 48) {
                        var7 = var13.topMargin;
                        break label101;
                     }

                     if (var16 != 80) {
                        var7 = var13.topMargin;
                        break label101;
                     }

                     var8 = var7 - var10 - var3 - var15;
                     var7 = var13.bottomMargin;
                  } else {
                     var8 = (var7 - var10 - var3 - var15) / 2 + var13.topMargin;
                     var7 = var13.bottomMargin;
                  }

                  var7 = var8 - var7;
               }

               label93: {
                  label124: {
                     if (var11 == PhotoViewer.this.mentionListView) {
                        var8 = PhotoViewer.this.captionEditText.getMeasuredHeight();
                     } else {
                        if (PhotoViewer.this.captionEditText.isPopupView(var11)) {
                           if (!AndroidUtilities.isInMultiwindow) {
                              var8 = PhotoViewer.this.captionEditText.getBottom();
                              break label93;
                           }

                           var8 = PhotoViewer.this.captionEditText.getTop() - var11.getMeasuredHeight();
                           var7 = AndroidUtilities.dp(1.0F);
                           break label124;
                        }

                        if (var11 == PhotoViewer.this.selectedPhotosListView) {
                           var8 = PhotoViewer.this.actionBar.getMeasuredHeight();
                           break label93;
                        }

                        if (var11 != PhotoViewer.this.captionTextView && var11 != PhotoViewer.this.switchCaptionTextView) {
                           if (PhotoViewer.this.hintTextView != null && var11 == PhotoViewer.this.hintTextView) {
                              var8 = PhotoViewer.this.selectedPhotosListView.getBottom();
                              var7 = AndroidUtilities.dp(3.0F);
                              break label124;
                           }

                           var8 = var7;
                           if (var11 != PhotoViewer.this.cameraItem) {
                              break label93;
                           }

                           var7 = PhotoViewer.this.pickerView.getTop();
                           float var18;
                           if (PhotoViewer.this.sendPhotoType != 4 && PhotoViewer.this.sendPhotoType != 5) {
                              var18 = 15.0F;
                           } else {
                              var18 = 40.0F;
                           }

                           var7 -= AndroidUtilities.dp(var18);
                           var8 = PhotoViewer.this.cameraItem.getMeasuredHeight();
                        } else {
                           var8 = var7;
                           if (PhotoViewer.this.groupedPhotosListView.currentPhotos.isEmpty()) {
                              break label93;
                           }

                           var8 = PhotoViewer.this.groupedPhotosListView.getMeasuredHeight();
                        }
                     }

                     var8 = var7 - var8;
                     break label93;
                  }

                  var8 += var7;
               }

               var11.layout(var17 + var12, var8, var17 + var14 + var12, var15 + var8);
            }
         }

         this.notifyHeightChanged();
      }

      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         int var4 = MeasureSpec.getSize(var2);
         this.setMeasuredDimension(var3, var4);
         this.measureChildWithMargins(PhotoViewer.this.captionEditText, var1, 0, var2, 0);
         int var5 = PhotoViewer.this.captionEditText.getMeasuredHeight();
         int var6 = var3 - (this.getPaddingRight() + this.getPaddingLeft());
         int var7 = var4 - this.getPaddingBottom();
         int var8 = this.getChildCount();

         for(var4 = 0; var4 < var8; ++var4) {
            View var9 = this.getChildAt(var4);
            if (var9.getVisibility() != 8 && var9 != PhotoViewer.this.captionEditText) {
               if (var9 == PhotoViewer.this.aspectRatioFrameLayout) {
                  int var10 = AndroidUtilities.displaySize.y;
                  if (VERSION.SDK_INT >= 21) {
                     var3 = AndroidUtilities.statusBarHeight;
                  } else {
                     var3 = 0;
                  }

                  var9.measure(var1, MeasureSpec.makeMeasureSpec(var10 + var3, 1073741824));
               } else if (PhotoViewer.this.captionEditText.isPopupView(var9)) {
                  if (AndroidUtilities.isInMultiwindow) {
                     if (AndroidUtilities.isTablet()) {
                        var9.measure(MeasureSpec.makeMeasureSpec(var6, 1073741824), MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(320.0F), var7 - var5 - AndroidUtilities.statusBarHeight), 1073741824));
                     } else {
                        var9.measure(MeasureSpec.makeMeasureSpec(var6, 1073741824), MeasureSpec.makeMeasureSpec(var7 - var5 - AndroidUtilities.statusBarHeight, 1073741824));
                     }
                  } else {
                     var9.measure(MeasureSpec.makeMeasureSpec(var6, 1073741824), MeasureSpec.makeMeasureSpec(var9.getLayoutParams().height, 1073741824));
                  }
               } else {
                  this.measureChildWithMargins(var9, var1, 0, var2, 0);
               }
            }
         }

      }
   }

   public static class LinkMovementMethodMy extends LinkMovementMethod {
      public boolean onTouchEvent(TextView var1, Spannable var2, MotionEvent var3) {
         try {
            boolean var4 = super.onTouchEvent(var1, var2, var3);
            if (var3.getAction() == 1 || var3.getAction() == 3) {
               Selection.removeSelection(var2);
            }

            return var4;
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
            return false;
         }
      }
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         if (PhotoViewer.this.placeProvider != null && PhotoViewer.this.placeProvider.getSelectedPhotosOrder() != null) {
            return PhotoViewer.this.placeProvider.allowGroupPhotos() ? PhotoViewer.this.placeProvider.getSelectedPhotosOrder().size() + 1 : PhotoViewer.this.placeProvider.getSelectedPhotosOrder().size();
         } else {
            return 0;
         }
      }

      public int getItemViewType(int var1) {
         return var1 == 0 && PhotoViewer.this.placeProvider.allowGroupPhotos() ? 1 : 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return true;
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$0$PhotoViewer$ListAdapter(View var1) {
         Object var4 = ((View)var1.getParent()).getTag();
         int var2 = PhotoViewer.this.imagesArrLocals.indexOf(var4);
         int var3;
         if (var2 >= 0) {
            var3 = PhotoViewer.this.placeProvider.setPhotoChecked(var2, PhotoViewer.this.getCurrentVideoEditedInfo());
            PhotoViewer.this.placeProvider.isPhotoChecked(var2);
            if (var2 == PhotoViewer.this.currentIndex) {
               PhotoViewer.this.checkImageView.setChecked(-1, false, true);
            }

            if (var3 >= 0) {
               var2 = var3;
               if (PhotoViewer.this.placeProvider.allowGroupPhotos()) {
                  var2 = var3 + 1;
               }

               PhotoViewer.this.selectedPhotosAdapter.notifyItemRemoved(var2);
            }

            PhotoViewer.this.updateSelectedCount();
         } else {
            var3 = PhotoViewer.this.placeProvider.setPhotoUnchecked(var4);
            if (var3 >= 0) {
               var2 = var3;
               if (PhotoViewer.this.placeProvider.allowGroupPhotos()) {
                  var2 = var3 + 1;
               }

               PhotoViewer.this.selectedPhotosAdapter.notifyItemRemoved(var2);
               PhotoViewer.this.updateSelectedCount();
            }
         }

      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         BackupImageView var4 = null;
         if (var3 != 0) {
            if (var3 == 1) {
               ImageView var5 = (ImageView)var1.itemView;
               PorterDuffColorFilter var7 = var4;
               if (SharedConfig.groupPhotosEnabled) {
                  var7 = new PorterDuffColorFilter(-10043398, Mode.MULTIPLY);
               }

               var5.setColorFilter(var7);
               String var8;
               if (SharedConfig.groupPhotosEnabled) {
                  var2 = 2131559612;
                  var8 = "GroupPhotosHelp";
               } else {
                  var2 = 2131560786;
                  var8 = "SinglePhotosHelp";
               }

               var5.setContentDescription(LocaleController.getString(var8, var2));
            }
         } else {
            PhotoPickerPhotoCell var9 = (PhotoPickerPhotoCell)var1.itemView;
            var9.itemWidth = AndroidUtilities.dp(82.0F);
            var4 = var9.photoImage;
            var4.setOrientation(0, true);
            ArrayList var11 = PhotoViewer.this.placeProvider.getSelectedPhotosOrder();
            var3 = var2;
            if (PhotoViewer.this.placeProvider.allowGroupPhotos()) {
               var3 = var2 - 1;
            }

            Object var12 = PhotoViewer.this.placeProvider.getSelectedPhotos().get(var11.get(var3));
            if (var12 instanceof MediaController.PhotoEntry) {
               MediaController.PhotoEntry var13 = (MediaController.PhotoEntry)var12;
               var9.setTag(var13);
               var9.videoInfoContainer.setVisibility(4);
               String var6 = var13.thumbPath;
               if (var6 != null) {
                  var4.setImage(var6, (String)null, this.mContext.getResources().getDrawable(2131165697));
               } else if (var13.path != null) {
                  var4.setOrientation(var13.orientation, true);
                  StringBuilder var14;
                  if (var13.isVideo) {
                     var9.videoInfoContainer.setVisibility(0);
                     var3 = var13.duration;
                     var2 = var3 / 60;
                     var9.videoTextView.setText(String.format("%d:%02d", var2, var3 - var2 * 60));
                     var14 = new StringBuilder();
                     var14.append("vthumb://");
                     var14.append(var13.imageId);
                     var14.append(":");
                     var14.append(var13.path);
                     var4.setImage(var14.toString(), (String)null, this.mContext.getResources().getDrawable(2131165697));
                  } else {
                     var14 = new StringBuilder();
                     var14.append("thumb://");
                     var14.append(var13.imageId);
                     var14.append(":");
                     var14.append(var13.path);
                     var4.setImage(var14.toString(), (String)null, this.mContext.getResources().getDrawable(2131165697));
                  }
               } else {
                  var4.setImageResource(2131165697);
               }

               var9.setChecked(-1, true, false);
               var9.checkBox.setVisibility(0);
            } else if (var12 instanceof MediaController.SearchImage) {
               MediaController.SearchImage var10 = (MediaController.SearchImage)var12;
               var9.setTag(var10);
               var9.setImage(var10);
               var9.videoInfoContainer.setVisibility(4);
               var9.setChecked(-1, true, false);
               var9.checkBox.setVisibility(0);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            var3 = new ImageView(this.mContext) {
               protected void onMeasure(int var1, int var2) {
                  super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(66.0F), 1073741824), MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var2), 1073741824));
               }
            };
            ((ImageView)var3).setScaleType(ScaleType.CENTER);
            ((ImageView)var3).setImageResource(2131165757);
            ((ImageView)var3).setFocusable(true);
         } else {
            var3 = new PhotoPickerPhotoCell(this.mContext, false);
            ((PhotoPickerPhotoCell)var3).checkFrame.setOnClickListener(new _$$Lambda$PhotoViewer$ListAdapter$9zkDzdvMmAtv_zjywq_3chr7CXo(this));
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }

   private class PhotoProgressView {
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

      public PhotoProgressView(Context var2, View var3) {
         if (PhotoViewer.decelerateInterpolator == null) {
            PhotoViewer.decelerateInterpolator = new DecelerateInterpolator(1.5F);
            PhotoViewer.progressPaint = new Paint(1);
            PhotoViewer.progressPaint.setStyle(Style.STROKE);
            PhotoViewer.progressPaint.setStrokeCap(Cap.ROUND);
            PhotoViewer.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(3.0F));
            PhotoViewer.progressPaint.setColor(-1);
         }

         this.parent = var3;
      }

      private void updateAnimation() {
         long var1 = System.currentTimeMillis();
         long var3 = var1 - this.lastUpdateTime;
         long var5 = var3;
         if (var3 > 18L) {
            var5 = 18L;
         }

         this.lastUpdateTime = var1;
         if (this.animatedProgressValue != 1.0F) {
            this.radOffset += (float)(360L * var5) / 3000.0F;
            float var7 = this.currentProgress;
            float var8 = this.animationProgressStart;
            float var9 = var7 - var8;
            if (var9 > 0.0F) {
               this.currentProgressTime += var5;
               if (this.currentProgressTime >= 300L) {
                  this.animatedProgressValue = var7;
                  this.animationProgressStart = var7;
                  this.currentProgressTime = 0L;
               } else {
                  this.animatedProgressValue = var8 + var9 * PhotoViewer.decelerateInterpolator.getInterpolation((float)this.currentProgressTime / 300.0F);
               }
            }

            this.parent.invalidate();
         }

         if (this.animatedProgressValue >= 1.0F && this.previousBackgroundState != -2) {
            this.animatedAlphaValue -= (float)var5 / 200.0F;
            if (this.animatedAlphaValue <= 0.0F) {
               this.animatedAlphaValue = 0.0F;
               this.previousBackgroundState = -2;
            }

            this.parent.invalidate();
         }

      }

      public void onDraw(Canvas var1) {
         int var2 = (int)((float)this.size * this.scale);
         int var3 = (PhotoViewer.this.getContainerViewWidth() - var2) / 2;
         int var4 = (PhotoViewer.this.getContainerViewHeight() - var2) / 2;
         int var5 = this.previousBackgroundState;
         Drawable var6;
         if (var5 >= 0 && var5 < 4) {
            var6 = PhotoViewer.progressDrawables[this.previousBackgroundState];
            if (var6 != null) {
               var6.setAlpha((int)(this.animatedAlphaValue * 255.0F * this.alpha));
               var6.setBounds(var3, var4, var3 + var2, var4 + var2);
               var6.draw(var1);
            }
         }

         var5 = this.backgroundState;
         if (var5 >= 0 && var5 < 4) {
            var6 = PhotoViewer.progressDrawables[this.backgroundState];
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
            PhotoViewer.progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0F * this.alpha));
         } else {
            PhotoViewer.progressPaint.setAlpha((int)(this.alpha * 255.0F));
         }

         this.progressRect.set((float)(var3 + var5), (float)(var4 + var5), (float)(var3 + var2 - var5), (float)(var4 + var2 - var5));
         var1.drawArc(this.progressRect, this.radOffset - 90.0F, Math.max(4.0F, this.animatedProgressValue * 360.0F), false, PhotoViewer.progressPaint);
         this.updateAnimation();
      }

      public void setAlpha(float var1) {
         this.alpha = var1;
      }

      public void setBackgroundState(int var1, boolean var2) {
         if (this.backgroundState != var1 || !var2) {
            label17: {
               this.lastUpdateTime = System.currentTimeMillis();
               if (var2) {
                  int var3 = this.backgroundState;
                  if (var3 != var1) {
                     this.previousBackgroundState = var3;
                     this.animatedAlphaValue = 1.0F;
                     break label17;
                  }
               }

               this.previousBackgroundState = -2;
            }

            this.backgroundState = var1;
            this.parent.invalidate();
         }
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

   public interface PhotoViewerProvider {
      boolean allowCaption();

      boolean allowGroupPhotos();

      boolean canCaptureMorePhotos();

      boolean canScrollAway();

      boolean cancelButtonPressed();

      void deleteImageAtIndex(int var1);

      String getDeleteMessageString();

      int getPhotoIndex(int var1);

      PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3, boolean var4);

      int getSelectedCount();

      HashMap getSelectedPhotos();

      ArrayList getSelectedPhotosOrder();

      ImageReceiver.BitmapHolder getThumbForPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3);

      boolean isPhotoChecked(int var1);

      void needAddMorePhotos();

      boolean scaleToFill();

      void sendButtonPressed(int var1, VideoEditedInfo var2);

      int setPhotoChecked(int var1, VideoEditedInfo var2);

      int setPhotoUnchecked(Object var1);

      void toggleGroupPhotosEnabled();

      void updatePhotoAtIndex(int var1);

      void willHidePhotoViewer();

      void willSwitchFromPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3);
   }

   public static class PlaceProviderObject {
      public int clipBottomAddition;
      public int clipTopAddition;
      public int dialogId;
      public ImageReceiver imageReceiver;
      public int index;
      public boolean isEvent;
      public View parentView;
      public int radius;
      public float scale = 1.0F;
      public int size;
      public ImageReceiver.BitmapHolder thumb;
      public int viewX;
      public int viewY;
   }

   private class QualityChooseView extends View {
      private int circleSize;
      private int gapSize;
      private int lineSize;
      private boolean moving;
      private Paint paint = new Paint(1);
      private int sideSide;
      private boolean startMoving;
      private int startMovingQuality;
      private float startX;
      private TextPaint textPaint = new TextPaint(1);

      public QualityChooseView(Context var2) {
         super(var2);
         this.textPaint.setTextSize((float)AndroidUtilities.dp(12.0F));
         this.textPaint.setColor(-3289651);
      }

      protected void onDraw(Canvas var1) {
         if (PhotoViewer.this.compressionsCount != 1) {
            this.lineSize = (this.getMeasuredWidth() - this.circleSize * PhotoViewer.this.compressionsCount - this.gapSize * 8 - this.sideSide * 2) / (PhotoViewer.this.compressionsCount - 1);
         } else {
            this.lineSize = this.getMeasuredWidth() - this.circleSize * PhotoViewer.this.compressionsCount - this.gapSize * 8 - this.sideSide * 2;
         }

         int var2 = this.getMeasuredHeight() / 2 + AndroidUtilities.dp(6.0F);

         for(int var3 = 0; var3 < PhotoViewer.this.compressionsCount; ++var3) {
            int var4 = this.sideSide;
            int var5 = this.lineSize;
            int var6 = this.gapSize;
            int var7 = this.circleSize;
            var6 = var4 + (var5 + var6 * 2 + var7) * var3 + var7 / 2;
            if (var3 <= PhotoViewer.this.selectedCompression) {
               this.paint.setColor(-11292945);
            } else {
               this.paint.setColor(1728053247);
            }

            String var12;
            if (var3 == PhotoViewer.this.compressionsCount - 1) {
               StringBuilder var8 = new StringBuilder();
               var8.append(Math.min(PhotoViewer.this.originalWidth, PhotoViewer.this.originalHeight));
               var8.append("p");
               var12 = var8.toString();
            } else if (var3 == 0) {
               var12 = "240p";
            } else if (var3 == 1) {
               var12 = "360p";
            } else if (var3 == 2) {
               var12 = "480p";
            } else {
               var12 = "720p";
            }

            float var9 = this.textPaint.measureText(var12);
            float var10 = (float)var6;
            float var11 = (float)var2;
            if (var3 == PhotoViewer.this.selectedCompression) {
               var4 = AndroidUtilities.dp(8.0F);
            } else {
               var4 = this.circleSize / 2;
            }

            var1.drawCircle(var10, var11, (float)var4, this.paint);
            var1.drawText(var12, var10 - var9 / 2.0F, (float)(var2 - AndroidUtilities.dp(16.0F)), this.textPaint);
            if (var3 != 0) {
               var4 = var6 - this.circleSize / 2 - this.gapSize - this.lineSize;
               var1.drawRect((float)var4, (float)(var2 - AndroidUtilities.dp(1.0F)), (float)(var4 + this.lineSize), (float)(AndroidUtilities.dp(2.0F) + var2), this.paint);
            }
         }

      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(var1, var2);
         this.circleSize = AndroidUtilities.dp(12.0F);
         this.gapSize = AndroidUtilities.dp(2.0F);
         this.sideSide = AndroidUtilities.dp(18.0F);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         float var2 = var1.getX();
         int var3 = var1.getAction();
         boolean var4 = false;
         int var5;
         int var6;
         int var7;
         int var8;
         if (var3 == 0) {
            this.getParent().requestDisallowInterceptTouchEvent(true);

            for(var3 = 0; var3 < PhotoViewer.this.compressionsCount; ++var3) {
               var5 = this.sideSide;
               var6 = this.lineSize;
               var7 = this.gapSize;
               var8 = this.circleSize;
               var8 = var5 + (var6 + var7 * 2 + var8) * var3 + var8 / 2;
               if (var2 > (float)(var8 - AndroidUtilities.dp(15.0F)) && var2 < (float)(var8 + AndroidUtilities.dp(15.0F))) {
                  if (var3 == PhotoViewer.this.selectedCompression) {
                     var4 = true;
                  }

                  this.startMoving = var4;
                  this.startX = var2;
                  this.startMovingQuality = PhotoViewer.this.selectedCompression;
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
               for(var3 = 0; var3 < PhotoViewer.this.compressionsCount; ++var3) {
                  var7 = this.sideSide;
                  var8 = this.lineSize;
                  var5 = this.gapSize;
                  var6 = this.circleSize;
                  var7 = var7 + (var5 * 2 + var8 + var6) * var3 + var6 / 2;
                  var8 = var8 / 2 + var6 / 2 + var5;
                  if (var2 > (float)(var7 - var8) && var2 < (float)(var7 + var8)) {
                     if (PhotoViewer.this.selectedCompression != var3) {
                        PhotoViewer.this.selectedCompression = var3;
                        PhotoViewer.this.didChangedCompressionLevel(false);
                        this.invalidate();
                     }
                     break;
                  }
               }
            }
         } else if (var1.getAction() == 1 || var1.getAction() == 3) {
            if (!this.moving) {
               for(var3 = 0; var3 < PhotoViewer.this.compressionsCount; ++var3) {
                  var5 = this.sideSide;
                  var7 = this.lineSize;
                  var8 = this.gapSize;
                  var6 = this.circleSize;
                  var8 = var5 + (var7 + var8 * 2 + var6) * var3 + var6 / 2;
                  if (var2 > (float)(var8 - AndroidUtilities.dp(15.0F)) && var2 < (float)(var8 + AndroidUtilities.dp(15.0F))) {
                     if (PhotoViewer.this.selectedCompression != var3) {
                        PhotoViewer.this.selectedCompression = var3;
                        PhotoViewer.this.didChangedCompressionLevel(true);
                        this.invalidate();
                     }
                     break;
                  }
               }
            } else if (PhotoViewer.this.selectedCompression != this.startMovingQuality) {
               PhotoViewer.this.requestVideoPreview(1);
            }

            this.startMoving = false;
            this.moving = false;
         }

         return true;
      }
   }
}
