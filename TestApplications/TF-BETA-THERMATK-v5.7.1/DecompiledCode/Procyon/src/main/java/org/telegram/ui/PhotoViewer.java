// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.graphics.Paint$Cap;
import org.telegram.ui.Components.BackupImageView;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.animation.OvershootInterpolator;
import android.text.Layout$Alignment;
import android.graphics.Paint$Join;
import android.graphics.Paint$Style;
import android.text.TextPaint;
import android.text.StaticLayout;
import android.graphics.drawable.ColorDrawable;
import android.content.DialogInterface$OnDismissListener;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.Components.SizeNotifierFrameLayoutPhoto;
import androidx.recyclerview.widget.LinearSmoothScrollerEnd;
import androidx.recyclerview.widget.DefaultItemAnimator;
import org.telegram.ui.Cells.PhotoPickerPhotoCell;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.messenger.MessagesStorage;
import java.util.HashMap;
import org.telegram.messenger.SendMessagesHelper;
import android.os.Bundle;
import org.telegram.ui.Cells.CheckBoxCell;
import android.view.View$OnApplyWindowInsetsListener;
import android.view.ViewGroup;
import android.view.ActionMode;
import android.view.ActionMode$Callback;
import android.view.View$MeasureSpec;
import android.view.KeyEvent;
import android.view.ContextThemeWrapper;
import android.widget.FrameLayout$LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.content.res.Configuration;
import org.telegram.ui.Components.URLSpanUserMentionPhotoViewer;
import android.view.View$OnTouchListener;
import android.text.TextUtils$TruncateAt;
import android.widget.LinearLayout;
import org.telegram.ui.Components.NumberPicker;
import androidx.annotation.Keep;
import org.telegram.messenger.BringAppForegroundService;
import org.telegram.messenger.ApplicationLoader;
import java.util.Iterator;
import android.graphics.RectF;
import org.telegram.ui.Components.Rect;
import android.graphics.BitmapFactory;
import android.view.animation.AccelerateInterpolator;
import org.telegram.messenger.ContactsController;
import java.util.Date;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.UserObject;
import java.util.Locale;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableStringBuilder;
import org.telegram.messenger.Emoji;
import android.text.SpannableString;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.UserConfig;
import android.media.MediaCodecInfo;
import org.telegram.messenger.BuildVars;
import android.content.DialogInterface;
import android.os.Parcelable;
import androidx.core.content.FileProvider;
import android.content.Intent;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.OtherDocumentPlaceholderDrawable;
import java.net.URLEncoder;
import org.telegram.ui.ActionBar.BottomSheet;
import android.view.WindowInsets;
import org.telegram.messenger.WebFile;
import android.view.WindowManager;
import android.content.SharedPreferences$Editor;
import org.telegram.messenger.MessagesController;
import android.widget.ImageView$ScaleType;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.view.View$OnClickListener;
import android.text.method.MovementMethod;
import org.telegram.messenger.DataQuery;
import java.io.ByteArrayInputStream;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.SharedConfig;
import java.io.File;
import android.text.TextUtils;
import android.content.DialogInterface$OnClickListener;
import android.provider.Settings;
import android.os.Build$VERSION;
import android.widget.Toast;
import org.telegram.messenger.LocaleController;
import java.util.Collection;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.ImageLoader;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.util.Property;
import android.animation.ObjectAnimator;
import org.telegram.ui.Components.AnimationProperties;
import android.animation.Animator;
import org.telegram.tgnet.TLObject;
import android.view.MotionEvent;
import android.graphics.Canvas;
import org.telegram.messenger.VideoEditedInfo;
import android.view.ViewTreeObserver$OnPreDrawListener;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Bitmaps;
import android.graphics.Bitmap$Config;
import org.telegram.messenger.AndroidUtilities;
import android.os.SystemClock;
import org.telegram.messenger.FileLoader;
import android.view.WindowManager$LayoutParams;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.VideoTimelinePlayView;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.Components.SeekBar;
import org.telegram.ui.Components.VideoForwardDrawable;
import android.view.VelocityTracker;
import android.view.TextureView$SurfaceTextureListener;
import android.widget.Scroller;
import android.view.View;
import org.telegram.ui.Components.PipVideoView;
import org.telegram.ui.Components.PhotoPaintView;
import org.telegram.ui.Components.PhotoFilterView;
import org.telegram.ui.Components.PhotoCropView;
import org.telegram.ui.Components.ChatAttachAlert;
import android.app.Activity;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Adapters.MentionsAdapter;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import android.graphics.SurfaceTexture;
import org.telegram.ui.Components.VideoPlayer;
import android.util.SparseArray;
import org.telegram.ui.Components.GroupedPhotosListView;
import android.view.GestureDetector;
import org.telegram.ui.Components.PickerBottomLayoutViewer;
import org.telegram.messenger.SecureDocument;
import android.net.Uri;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.ImageLocation;
import android.graphics.Bitmap;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.CheckBox;
import android.view.TextureView;
import org.telegram.messenger.ImageReceiver;
import android.widget.TextView;
import org.telegram.ui.Components.PhotoViewerCaptionEnterView;
import android.widget.ImageView;
import android.widget.FrameLayout;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import org.telegram.ui.Components.ClippingImageView;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import android.content.Context;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.ActionBar;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import android.annotation.SuppressLint;
import android.view.GestureDetector$OnDoubleTapListener;
import android.view.GestureDetector$OnGestureListener;
import org.telegram.messenger.NotificationCenter;

public class PhotoViewer implements NotificationCenterDelegate, GestureDetector$OnGestureListener, GestureDetector$OnDoubleTapListener
{
    @SuppressLint({ "StaticFieldLeak" })
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
    private float[][] animationValues;
    private boolean applying;
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private boolean attachedToWindow;
    private long audioFramesSize;
    private ArrayList<TLRPC.Photo> avatarsArr;
    private int avatarsDialogId;
    private BackgroundDrawable backgroundDrawable;
    private int bitrate;
    private Paint blackPaint;
    private FrameLayout bottomLayout;
    private boolean bottomTouchEnabled;
    private ImageView cameraItem;
    private boolean canDragDown;
    private boolean canZoom;
    private PhotoViewerCaptionEnterView captionEditText;
    private TextView captionTextView;
    private ImageReceiver centerImage;
    private AnimatorSet changeModeAnimation;
    private TextureView changedTextureView;
    private boolean changingPage;
    private boolean changingTextureView;
    private CheckBox checkImageView;
    private int classGuid;
    private ImageView compressItem;
    private AnimatorSet compressItemAnimation;
    private int compressionsCount;
    private FrameLayoutDrawer containerView;
    private ImageView cropItem;
    private int currentAccount;
    private AnimatedFileDrawable currentAnimation;
    private Bitmap currentBitmap;
    private TLRPC.BotInlineResult currentBotInlineResult;
    private AnimatorSet currentCaptionAnimation;
    private long currentDialogId;
    private int currentEditMode;
    private ImageLocation currentFileLocation;
    private String[] currentFileNames;
    private int currentIndex;
    private AnimatorSet currentListViewAnimation;
    private Runnable currentLoadingVideoRunnable;
    private MessageObject currentMessageObject;
    private String currentPathObject;
    private PlaceProviderObject currentPlaceObject;
    private Uri currentPlayingVideoFile;
    private SecureDocument currentSecureDocument;
    private String currentSubtitle;
    private ImageReceiver.BitmapHolder currentThumb;
    private ImageLocation currentUserAvatarLocation;
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
    private boolean[] endReached;
    private long endTime;
    private long estimatedDuration;
    private int estimatedSize;
    private boolean firstAnimationDelay;
    boolean fromCamera;
    private GestureDetector gestureDetector;
    private GroupedPhotosListView groupedPhotosListView;
    private PlaceProviderObject hideAfterAnimation;
    private AnimatorSet hintAnimation;
    private Runnable hintHideRunnable;
    private TextView hintTextView;
    private boolean ignoreDidSetImage;
    private AnimatorSet imageMoveAnimation;
    private ArrayList<MessageObject> imagesArr;
    private ArrayList<Object> imagesArrLocals;
    private ArrayList<ImageLocation> imagesArrLocations;
    private ArrayList<Integer> imagesArrLocationsSizes;
    private ArrayList<MessageObject> imagesArrTemp;
    private SparseArray<MessageObject>[] imagesByIds;
    private SparseArray<MessageObject>[] imagesByIdsTemp;
    private boolean inPreview;
    private VideoPlayer injectingVideoPlayer;
    private SurfaceTexture injectingVideoPlayerSurface;
    private DecelerateInterpolator interpolator;
    private boolean invalidCoords;
    private boolean isActionBarVisible;
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
    private ImageReceiver leftImage;
    private boolean loadInitialVideo;
    private boolean loadingMoreImages;
    private ActionBarMenuItem masksItem;
    private int maxSelectedPhotos;
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
    private Runnable miniProgressShowRunnable;
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
    private PhotoProgressView[] photoProgressViews;
    private CounterView photosCounterView;
    private FrameLayout pickerView;
    private ImageView pickerViewSendButton;
    private float pinchCenterX;
    private float pinchCenterY;
    private float pinchStartDistance;
    private float pinchStartScale;
    private float pinchStartX;
    private float pinchStartY;
    private boolean pipAnimationInProgress;
    private boolean pipAvailable;
    private ActionBarMenuItem pipItem;
    private int[] pipPosition;
    private PipVideoView pipVideoView;
    private PhotoViewerProvider placeProvider;
    private View playButtonAccessibilityOverlay;
    private boolean playerInjected;
    private boolean playerWasReady;
    private int previewViewEnd;
    private int previousCompression;
    private RadialProgressView progressView;
    private QualityChooseView qualityChooseView;
    private AnimatorSet qualityChooseViewAnimation;
    private PickerBottomLayoutViewer qualityPicker;
    private boolean requestingPreview;
    private TextView resetButton;
    private int resultHeight;
    private int resultWidth;
    private ImageReceiver rightImage;
    private ImageView rotateItem;
    private int rotationValue;
    private float scale;
    private Scroller scroller;
    private ArrayList<SecureDocument> secureDocuments;
    private float seekToProgressPending;
    private float seekToProgressPending2;
    private int selectedCompression;
    private ListAdapter selectedPhotosAdapter;
    private RecyclerListView selectedPhotosListView;
    private ActionBarMenuItem sendItem;
    private int sendPhotoType;
    private Runnable setLoadingRunnable;
    private ImageView shareButton;
    private int sharedMediaType;
    private PlaceProviderObject showAfterAnimation;
    private boolean skipFirstBufferingProgress;
    private int slideshowMessageId;
    private long startTime;
    private long startedPlayTime;
    private boolean streamingAlertShown;
    private TextureView$SurfaceTextureListener surfaceTextureListener;
    private TextView switchCaptionTextView;
    private int switchImageAfterAnimation;
    private Runnable switchToInlineRunnable;
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
    private Runnable updateProgressRunnable;
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
    private WindowManager$LayoutParams windowLayoutParams;
    private FrameLayout windowView;
    private boolean zoomAnimation;
    private boolean zooming;
    
    public PhotoViewer() {
        this.maxSelectedPhotos = -1;
        this.miniProgressShowRunnable = new _$$Lambda$PhotoViewer$kxaoiG79AFKAwlWEJtsCNV1PPvc(this);
        this.isActionBarVisible = true;
        this.backgroundDrawable = new BackgroundDrawable(-16777216);
        this.blackPaint = new Paint();
        this.photoProgressViews = new PhotoProgressView[3];
        this.setLoadingRunnable = new Runnable() {
            @Override
            public void run() {
                if (PhotoViewer.this.currentMessageObject == null) {
                    return;
                }
                FileLoader.getInstance(PhotoViewer.this.currentMessageObject.currentAccount).setLoadingVideo(PhotoViewer.this.currentMessageObject.getDocument(), true, false);
            }
        };
        this.pipPosition = new int[2];
        this.updateProgressRunnable = new Runnable() {
            @Override
            public void run() {
                if (PhotoViewer.this.videoPlayer != null) {
                    if (PhotoViewer.this.isCurrentVideo) {
                        if (!PhotoViewer.this.videoTimelineView.isDragging()) {
                            final float progress = PhotoViewer.this.videoPlayer.getCurrentPosition() / (float)PhotoViewer.this.videoPlayer.getDuration();
                            if (!PhotoViewer.this.inPreview && PhotoViewer.this.videoTimelineView.getVisibility() == 0) {
                                if (progress >= PhotoViewer.this.videoTimelineView.getRightProgress()) {
                                    PhotoViewer.this.videoTimelineView.setProgress(0.0f);
                                    PhotoViewer.this.videoPlayer.seekTo((int)(PhotoViewer.this.videoTimelineView.getLeftProgress() * PhotoViewer.this.videoPlayer.getDuration()));
                                    if (PhotoViewer.this.muteVideo) {
                                        PhotoViewer.this.videoPlayer.play();
                                    }
                                    else {
                                        PhotoViewer.this.videoPlayer.pause();
                                    }
                                    PhotoViewer.this.containerView.invalidate();
                                }
                                else {
                                    float n;
                                    if ((n = progress - PhotoViewer.this.videoTimelineView.getLeftProgress()) < 0.0f) {
                                        n = 0.0f;
                                    }
                                    float progress2;
                                    if ((progress2 = n / (PhotoViewer.this.videoTimelineView.getRightProgress() - PhotoViewer.this.videoTimelineView.getLeftProgress())) > 1.0f) {
                                        progress2 = 1.0f;
                                    }
                                    PhotoViewer.this.videoTimelineView.setProgress(progress2);
                                }
                            }
                            else {
                                PhotoViewer.this.videoTimelineView.setProgress(progress);
                            }
                            PhotoViewer.this.updateVideoPlayerTime();
                        }
                    }
                    else if (!PhotoViewer.this.videoPlayerSeekbar.isDragging()) {
                        final float progress3 = PhotoViewer.this.videoPlayer.getCurrentPosition() / (float)PhotoViewer.this.videoPlayer.getDuration();
                        float bufferedProgressFromPosition;
                        if (PhotoViewer.this.currentVideoFinishedLoading) {
                            bufferedProgressFromPosition = 1.0f;
                        }
                        else {
                            final long elapsedRealtime = SystemClock.elapsedRealtime();
                            if (Math.abs(elapsedRealtime - PhotoViewer.this.lastBufferedPositionCheck) >= 500L) {
                                if (PhotoViewer.this.isStreaming) {
                                    final FileLoader instance = FileLoader.getInstance(PhotoViewer.this.currentAccount);
                                    float access$1200;
                                    if (PhotoViewer.this.seekToProgressPending != 0.0f) {
                                        access$1200 = PhotoViewer.this.seekToProgressPending;
                                    }
                                    else {
                                        access$1200 = progress3;
                                    }
                                    bufferedProgressFromPosition = instance.getBufferedProgressFromPosition(access$1200, PhotoViewer.this.currentFileNames[0]);
                                }
                                else {
                                    bufferedProgressFromPosition = 1.0f;
                                }
                                PhotoViewer.this.lastBufferedPositionCheck = elapsedRealtime;
                            }
                            else {
                                bufferedProgressFromPosition = -1.0f;
                            }
                        }
                        if (!PhotoViewer.this.inPreview && PhotoViewer.this.videoTimelineView.getVisibility() == 0) {
                            if (progress3 >= PhotoViewer.this.videoTimelineView.getRightProgress()) {
                                PhotoViewer.this.videoPlayer.pause();
                                PhotoViewer.this.videoPlayerSeekbar.setProgress(0.0f);
                                PhotoViewer.this.videoPlayer.seekTo((int)(PhotoViewer.this.videoTimelineView.getLeftProgress() * PhotoViewer.this.videoPlayer.getDuration()));
                                PhotoViewer.this.containerView.invalidate();
                            }
                            else {
                                float n2;
                                if ((n2 = progress3 - PhotoViewer.this.videoTimelineView.getLeftProgress()) < 0.0f) {
                                    n2 = 0.0f;
                                }
                                float progress4;
                                if ((progress4 = n2 / (PhotoViewer.this.videoTimelineView.getRightProgress() - PhotoViewer.this.videoTimelineView.getLeftProgress())) > 1.0f) {
                                    progress4 = 1.0f;
                                }
                                PhotoViewer.this.videoPlayerSeekbar.setProgress(progress4);
                            }
                        }
                        else {
                            if (PhotoViewer.this.seekToProgressPending == 0.0f) {
                                PhotoViewer.this.videoPlayerSeekbar.setProgress(progress3);
                            }
                            if (bufferedProgressFromPosition != -1.0f) {
                                PhotoViewer.this.videoPlayerSeekbar.setBufferedProgress(bufferedProgressFromPosition);
                                if (PhotoViewer.this.pipVideoView != null) {
                                    PhotoViewer.this.pipVideoView.setBufferedProgress(bufferedProgressFromPosition);
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
        this.switchToInlineRunnable = new Runnable() {
            @Override
            public void run() {
                PhotoViewer.this.switchingInlineMode = false;
                if (PhotoViewer.this.currentBitmap != null) {
                    PhotoViewer.this.currentBitmap.recycle();
                    PhotoViewer.this.currentBitmap = null;
                }
                PhotoViewer.this.changingTextureView = true;
                if (PhotoViewer.this.textureImageView != null) {
                    try {
                        PhotoViewer.this.currentBitmap = Bitmaps.createBitmap(PhotoViewer.this.videoTextureView.getWidth(), PhotoViewer.this.videoTextureView.getHeight(), Bitmap$Config.ARGB_8888);
                        PhotoViewer.this.videoTextureView.getBitmap(PhotoViewer.this.currentBitmap);
                    }
                    catch (Throwable t) {
                        if (PhotoViewer.this.currentBitmap != null) {
                            PhotoViewer.this.currentBitmap.recycle();
                            PhotoViewer.this.currentBitmap = null;
                        }
                        FileLog.e(t);
                    }
                    if (PhotoViewer.this.currentBitmap != null) {
                        PhotoViewer.this.textureImageView.setVisibility(0);
                        PhotoViewer.this.textureImageView.setImageBitmap(PhotoViewer.this.currentBitmap);
                    }
                    else {
                        PhotoViewer.this.textureImageView.setImageDrawable((Drawable)null);
                    }
                }
                PhotoViewer.this.isInline = true;
                PhotoViewer.this.pipVideoView = new PipVideoView();
                final PhotoViewer this$0 = PhotoViewer.this;
                final PipVideoView access$1500 = this$0.pipVideoView;
                final Activity access$1501 = PhotoViewer.this.parentActivity;
                final PhotoViewer this$2 = PhotoViewer.this;
                this$0.changedTextureView = access$1500.show(access$1501, this$2, this$2.aspectRatioFrameLayout.getAspectRatio(), PhotoViewer.this.aspectRatioFrameLayout.getVideoRotation());
                PhotoViewer.this.changedTextureView.setVisibility(4);
                PhotoViewer.this.aspectRatioFrameLayout.removeView((View)PhotoViewer.this.videoTextureView);
            }
        };
        this.surfaceTextureListener = (TextureView$SurfaceTextureListener)new TextureView$SurfaceTextureListener() {
            public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
            }
            
            public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
                if (PhotoViewer.this.videoTextureView == null) {
                    return true;
                }
                if (PhotoViewer.this.changingTextureView) {
                    if (PhotoViewer.this.switchingInlineMode) {
                        PhotoViewer.this.waitingForFirstTextureUpload = 2;
                    }
                    PhotoViewer.this.videoTextureView.setSurfaceTexture(surfaceTexture);
                    PhotoViewer.this.videoTextureView.setVisibility(0);
                    PhotoViewer.this.changingTextureView = false;
                    PhotoViewer.this.containerView.invalidate();
                    return false;
                }
                return true;
            }
            
            public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
            }
            
            public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
                if (PhotoViewer.this.waitingForFirstTextureUpload == 1) {
                    PhotoViewer.this.changedTextureView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                        public boolean onPreDraw() {
                            PhotoViewer.this.changedTextureView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
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
        this.animationValues = new float[2][10];
        this.leftImage = new ImageReceiver();
        this.centerImage = new ImageReceiver();
        this.rightImage = new ImageReceiver();
        this.currentFileNames = new String[3];
        this.endReached = new boolean[] { false, true };
        this.scale = 1.0f;
        this.interpolator = new DecelerateInterpolator(1.5f);
        this.pinchStartScale = 1.0f;
        this.canZoom = true;
        this.canDragDown = true;
        this.bottomTouchEnabled = true;
        this.imagesArrTemp = new ArrayList<MessageObject>();
        this.imagesByIdsTemp = (SparseArray<MessageObject>[])new SparseArray[] { new SparseArray(), new SparseArray() };
        this.imagesArr = new ArrayList<MessageObject>();
        this.imagesByIds = (SparseArray<MessageObject>[])new SparseArray[] { new SparseArray(), new SparseArray() };
        this.imagesArrLocations = new ArrayList<ImageLocation>();
        this.secureDocuments = new ArrayList<SecureDocument>();
        this.avatarsArr = new ArrayList<TLRPC.Photo>();
        this.imagesArrLocationsSizes = new ArrayList<Integer>();
        this.imagesArrLocals = new ArrayList<Object>();
        this.currentUserAvatarLocation = null;
        this.compressionsCount = -1;
        this.blackPaint.setColor(-16777216);
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
        (this.imageMoveAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, (Property)AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[] { 0.0f, 1.0f }) });
        this.imageMoveAnimation.setInterpolator((TimeInterpolator)this.interpolator);
        this.imageMoveAnimation.setDuration((long)n);
        this.imageMoveAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                PhotoViewer.this.imageMoveAnimation = null;
                PhotoViewer.this.containerView.invalidate();
            }
        });
        this.imageMoveAnimation.start();
    }
    
    private void applyCurrentEditMode() {
        final int currentEditMode = this.currentEditMode;
        Bitmap bitmap = null;
        Collection<? extends TLRPC.InputDocument> collection = null;
        boolean b = false;
        MediaController.SavedFilterState savedFilterState2 = null;
        Label_0113: {
            Bitmap bitmap2 = null;
            ArrayList<? extends TLRPC.InputDocument> masks = null;
            Label_0103: {
                if (currentEditMode != 1 && (currentEditMode != 0 || this.sendPhotoType != 1)) {
                    final int currentEditMode2 = this.currentEditMode;
                    MediaController.SavedFilterState savedFilterState;
                    if (currentEditMode2 == 2) {
                        bitmap = this.photoFilterView.getBitmap();
                        savedFilterState = this.photoFilterView.getSavedFilterState();
                    }
                    else {
                        if (currentEditMode2 == 3) {
                            bitmap2 = this.photoPaintView.getBitmap();
                            masks = this.photoPaintView.getMasks();
                            break Label_0103;
                        }
                        bitmap = null;
                        savedFilterState = null;
                    }
                    collection = null;
                    b = false;
                    savedFilterState2 = savedFilterState;
                    break Label_0113;
                }
                bitmap2 = this.photoCropView.getBitmap();
                masks = null;
            }
            savedFilterState2 = null;
            b = true;
            collection = masks;
            bitmap = bitmap2;
        }
        if (bitmap != null) {
            final TLRPC.PhotoSize scaleAndSaveImage = ImageLoader.scaleAndSaveImage(bitmap, (float)AndroidUtilities.getPhotoSize(), (float)AndroidUtilities.getPhotoSize(), 80, false, 101, 101);
            if (scaleAndSaveImage != null) {
                final MediaController.SearchImage value = this.imagesArrLocals.get(this.currentIndex);
                if (value instanceof MediaController.PhotoEntry) {
                    final MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry)value;
                    photoEntry.imagePath = FileLoader.getPathToAttach(scaleAndSaveImage, true).toString();
                    final TLRPC.PhotoSize scaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(bitmap, (float)AndroidUtilities.dp(120.0f), (float)AndroidUtilities.dp(120.0f), 70, false, 101, 101);
                    if (scaleAndSaveImage2 != null) {
                        photoEntry.thumbPath = FileLoader.getPathToAttach(scaleAndSaveImage2, true).toString();
                    }
                    if (collection != null) {
                        photoEntry.stickers.addAll(collection);
                    }
                    final int currentEditMode3 = this.currentEditMode;
                    if (currentEditMode3 == 1) {
                        this.cropItem.setColorFilter((ColorFilter)new PorterDuffColorFilter(-12734994, PorterDuff$Mode.MULTIPLY));
                        photoEntry.isCropped = true;
                    }
                    else if (currentEditMode3 == 2) {
                        this.tuneItem.setColorFilter((ColorFilter)new PorterDuffColorFilter(-12734994, PorterDuff$Mode.MULTIPLY));
                        photoEntry.isFiltered = true;
                    }
                    else if (currentEditMode3 == 3) {
                        this.paintItem.setColorFilter((ColorFilter)new PorterDuffColorFilter(-12734994, PorterDuff$Mode.MULTIPLY));
                        photoEntry.isPainted = true;
                    }
                    if (savedFilterState2 != null) {
                        photoEntry.savedFilterState = savedFilterState2;
                    }
                    else if (b) {
                        photoEntry.savedFilterState = null;
                    }
                }
                else if (value instanceof MediaController.SearchImage) {
                    final MediaController.SearchImage searchImage = value;
                    searchImage.imagePath = FileLoader.getPathToAttach(scaleAndSaveImage, true).toString();
                    final TLRPC.PhotoSize scaleAndSaveImage3 = ImageLoader.scaleAndSaveImage(bitmap, (float)AndroidUtilities.dp(120.0f), (float)AndroidUtilities.dp(120.0f), 70, false, 101, 101);
                    if (scaleAndSaveImage3 != null) {
                        searchImage.thumbPath = FileLoader.getPathToAttach(scaleAndSaveImage3, true).toString();
                    }
                    if (collection != null) {
                        searchImage.stickers.addAll(collection);
                    }
                    final int currentEditMode4 = this.currentEditMode;
                    if (currentEditMode4 == 1) {
                        this.cropItem.setColorFilter((ColorFilter)new PorterDuffColorFilter(-12734994, PorterDuff$Mode.MULTIPLY));
                        searchImage.isCropped = true;
                    }
                    else if (currentEditMode4 == 2) {
                        this.tuneItem.setColorFilter((ColorFilter)new PorterDuffColorFilter(-12734994, PorterDuff$Mode.MULTIPLY));
                        searchImage.isFiltered = true;
                    }
                    else if (currentEditMode4 == 3) {
                        this.paintItem.setColorFilter((ColorFilter)new PorterDuffColorFilter(-12734994, PorterDuff$Mode.MULTIPLY));
                        searchImage.isPainted = true;
                    }
                    if (savedFilterState2 != null) {
                        searchImage.savedFilterState = savedFilterState2;
                    }
                    else if (b) {
                        searchImage.savedFilterState = null;
                    }
                }
                final int sendPhotoType = this.sendPhotoType;
                if (sendPhotoType == 0 || sendPhotoType == 4) {
                    final PhotoViewerProvider placeProvider = this.placeProvider;
                    if (placeProvider != null) {
                        placeProvider.updatePhotoAtIndex(this.currentIndex);
                        if (!this.placeProvider.isPhotoChecked(this.currentIndex)) {
                            this.setPhotoChecked();
                        }
                    }
                }
                if (this.currentEditMode == 1) {
                    float scale = this.photoCropView.getRectSizeX() / this.getContainerViewWidth();
                    final float n = this.photoCropView.getRectSizeY() / this.getContainerViewHeight();
                    if (scale <= n) {
                        scale = n;
                    }
                    this.scale = scale;
                    this.translationX = this.photoCropView.getRectX() + this.photoCropView.getRectSizeX() / 2.0f - this.getContainerViewWidth() / 2;
                    this.translationY = this.photoCropView.getRectY() + this.photoCropView.getRectSizeY() / 2.0f - this.getContainerViewHeight() / 2;
                    this.zoomAnimation = true;
                    this.applying = true;
                    this.photoCropView.onDisappear();
                }
                this.centerImage.setParentView(null);
                this.centerImage.setOrientation(0, true);
                this.ignoreDidSetImage = true;
                this.centerImage.setImageBitmap(bitmap);
                this.ignoreDidSetImage = false;
                this.centerImage.setParentView((View)this.containerView);
                if (this.sendPhotoType == 1) {
                    this.setCropBitmap();
                }
            }
        }
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
    
    private void checkBufferedProgress(final float n) {
        if (this.isStreaming && this.parentActivity != null && !this.streamingAlertShown && this.videoPlayer != null) {
            final MessageObject currentMessageObject = this.currentMessageObject;
            if (currentMessageObject != null) {
                final TLRPC.Document document = currentMessageObject.getDocument();
                if (document == null) {
                    return;
                }
                if (this.currentMessageObject.getDuration() < 20) {
                    return;
                }
                if (n < 0.9f) {
                    final int size = document.size;
                    if ((size * n >= 5242880.0f || (n >= 0.5f && size >= 2097152)) && Math.abs(SystemClock.elapsedRealtime() - this.startedPlayTime) >= 2000L) {
                        if (this.videoPlayer.getDuration() == -9223372036854775807L) {
                            Toast.makeText((Context)this.parentActivity, (CharSequence)LocaleController.getString("VideoDoesNotSupportStreaming", 2131561044), 1).show();
                        }
                        this.streamingAlertShown = true;
                    }
                }
            }
        }
    }
    
    private boolean checkInlinePermissions() {
        final Activity parentActivity = this.parentActivity;
        if (parentActivity == null) {
            return false;
        }
        if (Build$VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays((Context)parentActivity)) {
            new AlertDialog.Builder((Context)this.parentActivity).setTitle(LocaleController.getString("AppName", 2131558635)).setMessage(LocaleController.getString("PermissionDrawAboveOtherApps", 2131560413)).setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131560419), (DialogInterface$OnClickListener)new _$$Lambda$PhotoViewer$3qscthC5fEqwYQ6AGYUupTSlBhs(this)).show();
            return false;
        }
        return true;
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
    
    private void checkProgress(final int n, final boolean b) {
        final int currentIndex = this.currentIndex;
        final boolean b2 = true;
        int index;
        if (n == 1) {
            index = currentIndex + 1;
        }
        else {
            index = currentIndex;
            if (n == 2) {
                index = currentIndex - 1;
            }
        }
        final String s = this.currentFileNames[n];
        final boolean b3 = false;
        if (s != null) {
            final MessageObject currentMessageObject = this.currentMessageObject;
            File pathToAttach = null;
            final File file = null;
            final File file2 = null;
            File file3;
            boolean b4;
            boolean video;
            if (currentMessageObject != null) {
                if (index < 0 || index >= this.imagesArr.size()) {
                    this.photoProgressViews[n].setBackgroundState(-1, b);
                    return;
                }
                final MessageObject messageObject = this.imagesArr.get(index);
                if (this.sharedMediaType == 1 && !messageObject.canPreviewDocument()) {
                    this.photoProgressViews[n].setBackgroundState(-1, b);
                    return;
                }
                if (TextUtils.isEmpty((CharSequence)messageObject.messageOwner.attachPath) || !(file3 = new File(messageObject.messageOwner.attachPath)).exists()) {
                    file3 = null;
                }
                if (file3 == null) {
                    final TLRPC.MessageMedia media = messageObject.messageOwner.media;
                    Label_0247: {
                        if (media instanceof TLRPC.TL_messageMediaWebPage) {
                            final TLRPC.WebPage webpage = media.webpage;
                            if (webpage != null && webpage.document == null) {
                                file3 = FileLoader.getPathToAttach(this.getFileLocation(index, null), true);
                                break Label_0247;
                            }
                        }
                        file3 = FileLoader.getPathToMessage(messageObject.messageOwner);
                    }
                }
                b4 = (SharedConfig.streamMedia && messageObject.isVideo() && messageObject.canStreamVideo() && (int)messageObject.getDialogId() != 0);
                video = messageObject.isVideo();
            }
            else if (this.currentBotInlineResult != null) {
                if (index < 0 || index >= this.imagesArrLocals.size()) {
                    this.photoProgressViews[n].setBackgroundState(-1, b);
                    return;
                }
                final TLRPC.BotInlineResult botInlineResult = this.imagesArrLocals.get(index);
                if (!botInlineResult.type.equals("video") && !MessageObject.isVideoDocument(botInlineResult.document)) {
                    if (botInlineResult.document != null) {
                        pathToAttach = new File(FileLoader.getDirectory(3), this.currentFileNames[n]);
                    }
                    else {
                        pathToAttach = file2;
                        if (botInlineResult.photo != null) {
                            pathToAttach = new File(FileLoader.getDirectory(0), this.currentFileNames[n]);
                        }
                    }
                    video = false;
                }
                else {
                    final TLRPC.Document document = botInlineResult.document;
                    if (document != null) {
                        pathToAttach = FileLoader.getPathToAttach(document);
                    }
                    else if (botInlineResult.content instanceof TLRPC.TL_webDocument) {
                        final File directory = FileLoader.getDirectory(4);
                        final StringBuilder sb = new StringBuilder();
                        sb.append(Utilities.MD5(botInlineResult.content.url));
                        sb.append(".");
                        sb.append(ImageLoader.getHttpUrlExtension(botInlineResult.content.url, "mp4"));
                        pathToAttach = new File(directory, sb.toString());
                    }
                    video = true;
                }
                File file4 = null;
                Label_0597: {
                    if (pathToAttach != null) {
                        file4 = pathToAttach;
                        if (pathToAttach.exists()) {
                            break Label_0597;
                        }
                    }
                    file4 = new File(FileLoader.getDirectory(4), this.currentFileNames[n]);
                }
                b4 = false;
                file3 = file4;
            }
            else {
                if (this.currentFileLocation != null) {
                    if (index < 0 || index >= this.imagesArrLocations.size()) {
                        this.photoProgressViews[n].setBackgroundState(-1, b);
                        return;
                    }
                    file3 = FileLoader.getPathToAttach(this.imagesArrLocations.get(index).location, this.avatarsDialogId != 0 || this.isEvent);
                }
                else if (this.currentSecureDocument != null) {
                    if (index < 0 || index >= this.secureDocuments.size()) {
                        this.photoProgressViews[n].setBackgroundState(-1, b);
                        return;
                    }
                    file3 = FileLoader.getPathToAttach(this.secureDocuments.get(index), true);
                }
                else {
                    file3 = file;
                    if (this.currentPathObject != null && !(file3 = new File(FileLoader.getDirectory(3), this.currentFileNames[n])).exists()) {
                        file3 = new File(FileLoader.getDirectory(4), this.currentFileNames[n]);
                    }
                }
                b4 = false;
                video = false;
            }
            final boolean b5 = file3 != null && file3.exists();
            if (file3 != null && (b5 || b4)) {
                if (video) {
                    this.photoProgressViews[n].setBackgroundState(3, b);
                }
                else {
                    this.photoProgressViews[n].setBackgroundState(-1, b);
                }
                if (n == 0) {
                    if (!b5) {
                        if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[n])) {
                            this.menuItem.hideSubItem(7);
                        }
                        else {
                            this.menuItem.showSubItem(7);
                        }
                    }
                    else {
                        this.menuItem.hideSubItem(7);
                    }
                }
            }
            else {
                if (video) {
                    if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[n])) {
                        this.photoProgressViews[n].setBackgroundState(2, false);
                    }
                    else {
                        this.photoProgressViews[n].setBackgroundState(1, false);
                    }
                }
                else {
                    this.photoProgressViews[n].setBackgroundState(0, b);
                }
                Float n2;
                if ((n2 = ImageLoader.getInstance().getFileProgress(this.currentFileNames[n])) == null) {
                    n2 = 0.0f;
                }
                this.photoProgressViews[n].setProgress(n2, false);
            }
            if (n == 0) {
                boolean canZoom = b2;
                if (this.imagesArrLocals.isEmpty()) {
                    canZoom = (this.currentFileNames[0] != null && this.photoProgressViews[0].backgroundState != 0 && b2);
                }
                this.canZoom = canZoom;
            }
        }
        else {
            int isVideo = b3 ? 1 : 0;
            if (!this.imagesArrLocals.isEmpty()) {
                isVideo = (b3 ? 1 : 0);
                if (index >= 0) {
                    isVideo = (b3 ? 1 : 0);
                    if (index < this.imagesArrLocals.size()) {
                        final Object value = this.imagesArrLocals.get(index);
                        isVideo = (b3 ? 1 : 0);
                        if (value instanceof MediaController.PhotoEntry) {
                            isVideo = (((MediaController.PhotoEntry)value).isVideo ? 1 : 0);
                        }
                    }
                }
            }
            if (isVideo != 0) {
                this.photoProgressViews[n].setBackgroundState(3, b);
            }
            else {
                this.photoProgressViews[n].setBackgroundState(-1, b);
            }
        }
    }
    
    private ByteArrayInputStream cleanBuffer(final byte[] array) {
        final byte[] buf = new byte[array.length];
        int i = 0;
        int length = 0;
        while (i < array.length) {
            if (array[i] == 0 && array[i + 1] == 0 && array[i + 2] == 3) {
                buf[length] = 0;
                buf[length + 1] = 0;
                i += 3;
                length += 2;
            }
            else {
                buf[length] = array[i];
                ++i;
                ++length;
            }
        }
        return new ByteArrayInputStream(buf, 0, length);
    }
    
    private void closeCaptionEnter(final boolean b) {
        final int currentIndex = this.currentIndex;
        if (currentIndex >= 0) {
            if (currentIndex < this.imagesArrLocals.size()) {
                final MediaController.SearchImage value = this.imagesArrLocals.get(this.currentIndex);
                CharSequence currentSubtitle = null;
                if (b) {
                    final CharSequence[] array = { this.captionEditText.getFieldCharSequence() };
                    final ArrayList<TLRPC.MessageEntity> entities = DataQuery.getInstance(this.currentAccount).getEntities(array);
                    if (value instanceof MediaController.PhotoEntry) {
                        final MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry)value;
                        photoEntry.caption = array[0];
                        photoEntry.entities = entities;
                    }
                    else if (value instanceof MediaController.SearchImage) {
                        final MediaController.SearchImage searchImage = value;
                        searchImage.caption = array[0];
                        searchImage.entities = entities;
                    }
                    if (this.captionEditText.getFieldCharSequence().length() != 0 && !this.placeProvider.isPhotoChecked(this.currentIndex)) {
                        this.setPhotoChecked();
                    }
                    this.setCurrentCaption(null, array[0], false);
                }
                this.captionEditText.setTag((Object)null);
                final String lastTitle = this.lastTitle;
                if (lastTitle != null) {
                    this.actionBar.setTitle(lastTitle);
                    this.lastTitle = null;
                }
                if (this.isCurrentVideo) {
                    final ActionBar actionBar = this.actionBar;
                    if (!this.muteVideo) {
                        currentSubtitle = this.currentSubtitle;
                    }
                    actionBar.setSubtitle(currentSubtitle);
                }
                this.updateCaptionTextForCurrentPhoto(value);
                if (this.captionEditText.isPopupShowing()) {
                    this.captionEditText.hidePopup();
                }
                this.captionEditText.closeKeyboard();
                if (Build$VERSION.SDK_INT >= 19) {
                    this.captionEditText.setImportantForAccessibility(4);
                }
            }
        }
    }
    
    private TextView createCaptionTextView() {
        final TextView textView = new TextView(this.actvityContext) {
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                return PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(motionEvent);
            }
        };
        textView.setMovementMethod((MovementMethod)new LinkMovementMethodMy());
        textView.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(8.0f));
        textView.setLinkTextColor(-1);
        textView.setTextColor(-1);
        textView.setHighlightColor(872415231);
        int n;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        textView.setGravity(n | 0x10);
        textView.setTextSize(1, 16.0f);
        textView.setVisibility(4);
        textView.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$zH4Om9_yDl8bq0dJjg4CnIpYu4A(this));
        return textView;
    }
    
    private void createCropView() {
        if (this.photoCropView != null) {
            return;
        }
        (this.photoCropView = new PhotoCropView(this.actvityContext)).setVisibility(8);
        this.containerView.addView((View)this.photoCropView, this.containerView.indexOfChild((View)this.pickerViewSendButton) - 1, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
        this.photoCropView.setDelegate((PhotoCropView.PhotoCropViewDelegate)new _$$Lambda$PhotoViewer$4LlSn7fGI8FnhnGA3tv2t0xhh6E(this));
    }
    
    private void createVideoControlsInterface() {
        (this.videoPlayerSeekbar = new SeekBar(this.containerView.getContext())).setLineHeight(AndroidUtilities.dp(4.0f));
        this.videoPlayerSeekbar.setColors(1728053247, 1728053247, -2764585, -1, -1);
        this.videoPlayerSeekbar.setDelegate((SeekBar.SeekBarDelegate)new _$$Lambda$PhotoViewer$Wr55IT5IGNjEYLW2UzsHblapEMs(this));
        (this.videoPlayerControlFrameLayout = new FrameLayout(this.containerView.getContext()) {
            protected void onDraw(final Canvas canvas) {
                canvas.save();
                canvas.translate((float)AndroidUtilities.dp(48.0f), 0.0f);
                PhotoViewer.this.videoPlayerSeekbar.draw(canvas);
                canvas.restore();
            }
            
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                float n6;
                if (PhotoViewer.this.videoPlayer != null) {
                    final float n5 = n6 = PhotoViewer.this.videoPlayer.getCurrentPosition() / (float)PhotoViewer.this.videoPlayer.getDuration();
                    if (!PhotoViewer.this.inPreview) {
                        n6 = n5;
                        if (PhotoViewer.this.videoTimelineView.getVisibility() == 0) {
                            float n7;
                            if ((n7 = n5 - PhotoViewer.this.videoTimelineView.getLeftProgress()) < 0.0f) {
                                n7 = 0.0f;
                            }
                            if ((n6 = n7 / (PhotoViewer.this.videoTimelineView.getRightProgress() - PhotoViewer.this.videoTimelineView.getLeftProgress())) > 1.0f) {
                                n6 = 1.0f;
                            }
                        }
                    }
                }
                else {
                    n6 = 0.0f;
                }
                PhotoViewer.this.videoPlayerSeekbar.setProgress(n6);
                PhotoViewer.this.videoTimelineView.setProgress(n6);
            }
            
            protected void onMeasure(int n, final int n2) {
                super.onMeasure(n, n2);
                final VideoPlayer access$100 = PhotoViewer.this.videoPlayer;
                long duration;
                final long n3 = duration = 0L;
                if (access$100 != null) {
                    duration = PhotoViewer.this.videoPlayer.getDuration();
                    if (duration == -9223372036854775807L) {
                        duration = n3;
                    }
                }
                final long n4 = duration / 1000L;
                final Paint paint = PhotoViewer.this.videoPlayerTime.getPaint();
                final long n5 = n4 / 60L;
                final long n6 = n4 % 60L;
                n = (int)Math.ceil(paint.measureText(String.format("%02d:%02d / %02d:%02d", n5, n6, n5, n6)));
                PhotoViewer.this.videoPlayerSeekbar.setSize(this.getMeasuredWidth() - AndroidUtilities.dp(64.0f) - n, this.getMeasuredHeight());
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                motionEvent.getX();
                motionEvent.getY();
                if (PhotoViewer.this.videoPlayerSeekbar.onTouch(motionEvent.getAction(), motionEvent.getX() - AndroidUtilities.dp(48.0f), motionEvent.getY())) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                    this.invalidate();
                }
                return true;
            }
        }).setWillNotDraw(false);
        this.bottomLayout.addView((View)this.videoPlayerControlFrameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        (this.videoPlayButton = new ImageView(this.containerView.getContext())).setScaleType(ImageView$ScaleType.CENTER);
        this.videoPlayerControlFrameLayout.addView((View)this.videoPlayButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, 51, 4.0f, 0.0f, 0.0f, 0.0f));
        this.videoPlayButton.setFocusable(true);
        this.videoPlayButton.setContentDescription((CharSequence)LocaleController.getString("AccActionPlay", 2131558409));
        this.videoPlayButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$b2YYbmShobZSde9PkRRj_gylAtM(this));
        (this.videoPlayerTime = new SimpleTextView(this.containerView.getContext())).setTextColor(-1);
        this.videoPlayerTime.setGravity(53);
        this.videoPlayerTime.setTextSize(13);
        this.videoPlayerControlFrameLayout.addView((View)this.videoPlayerTime, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, 53, 0.0f, 17.0f, 7.0f, 0.0f));
    }
    
    private void didChangedCompressionLevel(final boolean b) {
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("compress_video2", this.selectedCompression);
        edit.commit();
        this.updateWidthHeightBitrateForCompression();
        this.updateVideoInfo();
        if (b) {
            this.requestVideoPreview(1);
        }
    }
    
    private void dismissInternal() {
        try {
            if (this.windowView.getParent() != null) {
                ((LaunchActivity)this.parentActivity).drawerLayoutContainer.setAllowDrawContent(true);
                ((WindowManager)this.parentActivity.getSystemService("window")).removeView((View)this.windowView);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private int getAdditionX() {
        final int currentEditMode = this.currentEditMode;
        if (currentEditMode != 0 && currentEditMode != 3) {
            return AndroidUtilities.dp(14.0f);
        }
        return 0;
    }
    
    private int getAdditionY() {
        final int currentEditMode = this.currentEditMode;
        final int n = 0;
        int statusBarHeight = 0;
        if (currentEditMode == 3) {
            final int dp = AndroidUtilities.dp(8.0f);
            if (Build$VERSION.SDK_INT >= 21) {
                statusBarHeight = AndroidUtilities.statusBarHeight;
            }
            return dp + statusBarHeight;
        }
        if (currentEditMode != 0) {
            final int dp2 = AndroidUtilities.dp(14.0f);
            int statusBarHeight2 = n;
            if (Build$VERSION.SDK_INT >= 21) {
                statusBarHeight2 = AndroidUtilities.statusBarHeight;
            }
            return dp2 + statusBarHeight2;
        }
        return 0;
    }
    
    private int getContainerViewHeight() {
        return this.getContainerViewHeight(this.currentEditMode);
    }
    
    private int getContainerViewHeight(int n) {
        int y;
        final int n2 = y = AndroidUtilities.displaySize.y;
        if (n == 0) {
            y = n2;
            if (Build$VERSION.SDK_INT >= 21) {
                y = n2 + AndroidUtilities.statusBarHeight;
            }
        }
        if (n == 1) {
            n = AndroidUtilities.dp(144.0f);
        }
        else if (n == 2) {
            n = AndroidUtilities.dp(214.0f);
        }
        else {
            final int n3 = y;
            if (n != 3) {
                return n3;
            }
            n = AndroidUtilities.dp(48.0f) + ActionBar.getCurrentActionBarHeight();
        }
        return y - n;
    }
    
    private int getContainerViewWidth() {
        return this.getContainerViewWidth(this.currentEditMode);
    }
    
    private int getContainerViewWidth(final int n) {
        int width;
        final int n2 = width = this.containerView.getWidth();
        if (n != 0) {
            width = n2;
            if (n != 3) {
                width = n2 - AndroidUtilities.dp(28.0f);
            }
        }
        return width;
    }
    
    private VideoEditedInfo getCurrentVideoEditedInfo() {
        if (this.isCurrentVideo && this.currentPlayingVideoFile != null && this.compressionsCount != 0) {
            final VideoEditedInfo videoEditedInfo = new VideoEditedInfo();
            videoEditedInfo.startTime = this.startTime;
            videoEditedInfo.endTime = this.endTime;
            videoEditedInfo.rotationValue = this.rotationValue;
            videoEditedInfo.originalWidth = this.originalWidth;
            videoEditedInfo.originalHeight = this.originalHeight;
            videoEditedInfo.bitrate = this.bitrate;
            videoEditedInfo.originalPath = this.currentPlayingVideoFile.getPath();
            final int estimatedSize = this.estimatedSize;
            long estimatedSize2;
            if (estimatedSize != 0) {
                estimatedSize2 = estimatedSize;
            }
            else {
                estimatedSize2 = 1L;
            }
            videoEditedInfo.estimatedSize = estimatedSize2;
            videoEditedInfo.estimatedDuration = this.estimatedDuration;
            videoEditedInfo.framerate = this.videoFramerate;
            final boolean muteVideo = this.muteVideo;
            int n = -1;
            if (!muteVideo && (this.compressItem.getTag() == null || this.selectedCompression == this.compressionsCount - 1)) {
                videoEditedInfo.resultWidth = this.originalWidth;
                videoEditedInfo.resultHeight = this.originalHeight;
                if (!this.muteVideo) {
                    n = this.originalBitrate;
                }
                videoEditedInfo.bitrate = n;
                videoEditedInfo.muted = this.muteVideo;
            }
            else {
                if (this.muteVideo) {
                    this.selectedCompression = 1;
                    this.updateWidthHeightBitrateForCompression();
                }
                videoEditedInfo.resultWidth = this.resultWidth;
                videoEditedInfo.resultHeight = this.resultHeight;
                if (!this.muteVideo) {
                    n = this.bitrate;
                }
                videoEditedInfo.bitrate = n;
                videoEditedInfo.muted = this.muteVideo;
            }
            return videoEditedInfo;
        }
        return null;
    }
    
    private TLObject getFileLocation(final int index, final int[] array) {
        if (index < 0) {
            return null;
        }
        if (!this.secureDocuments.isEmpty()) {
            if (index >= this.secureDocuments.size()) {
                return null;
            }
            if (array != null) {
                array[0] = this.secureDocuments.get(index).secureFile.size;
            }
            return this.secureDocuments.get(index);
        }
        else {
            if (this.imagesArrLocations.isEmpty()) {
                if (!this.imagesArr.isEmpty()) {
                    if (index >= this.imagesArr.size()) {
                        return null;
                    }
                    final MessageObject messageObject = this.imagesArr.get(index);
                    final TLRPC.Message messageOwner = messageObject.messageOwner;
                    if (messageOwner instanceof TLRPC.TL_messageService) {
                        final TLRPC.MessageAction action = messageOwner.action;
                        if (action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                            return action.newUserPhoto.photo_big;
                        }
                        final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                        if (closestPhotoSizeWithSize != null) {
                            if (array != null) {
                                array[0] = closestPhotoSizeWithSize.size;
                                if (array[0] == 0) {
                                    array[0] = -1;
                                }
                            }
                            return closestPhotoSizeWithSize;
                        }
                        if (array != null) {
                            array[0] = -1;
                        }
                    }
                    else {
                        final TLRPC.MessageMedia media = messageOwner.media;
                        if (!(media instanceof TLRPC.TL_messageMediaPhoto) || media.photo == null) {
                            final TLRPC.MessageMedia media2 = messageObject.messageOwner.media;
                            if (!(media2 instanceof TLRPC.TL_messageMediaWebPage) || media2.webpage == null) {
                                final TLRPC.MessageMedia media3 = messageObject.messageOwner.media;
                                if (media3 instanceof TLRPC.TL_messageMediaInvoice) {
                                    return ((TLRPC.TL_messageMediaInvoice)media3).photo;
                                }
                                if (messageObject.getDocument() != null && MessageObject.isDocumentHasThumb(messageObject.getDocument())) {
                                    final TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject.getDocument().thumbs, 90);
                                    if (array != null) {
                                        array[0] = closestPhotoSizeWithSize2.size;
                                        if (array[0] == 0) {
                                            array[0] = -1;
                                        }
                                    }
                                    return closestPhotoSizeWithSize2;
                                }
                                return null;
                            }
                        }
                        final TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                        if (closestPhotoSizeWithSize3 != null) {
                            if (array != null) {
                                array[0] = closestPhotoSizeWithSize3.size;
                                if (array[0] == 0) {
                                    array[0] = -1;
                                }
                            }
                            return closestPhotoSizeWithSize3;
                        }
                        if (array != null) {
                            array[0] = -1;
                        }
                    }
                }
                return null;
            }
            if (index >= this.imagesArrLocations.size()) {
                return null;
            }
            if (array != null) {
                array[0] = this.imagesArrLocationsSizes.get(index);
            }
            return this.imagesArrLocations.get(index).location;
        }
    }
    
    private TLRPC.FileLocation getFileLocation(final ImageLocation imageLocation) {
        if (imageLocation == null) {
            return null;
        }
        return imageLocation.location;
    }
    
    private String getFileName(final int n) {
        if (n < 0) {
            return null;
        }
        if (this.secureDocuments.isEmpty()) {
            if (this.imagesArrLocations.isEmpty() && this.imagesArr.isEmpty()) {
                if (!this.imagesArrLocals.isEmpty()) {
                    if (n >= this.imagesArrLocals.size()) {
                        return null;
                    }
                    final TLRPC.BotInlineResult value = this.imagesArrLocals.get(n);
                    if (value instanceof MediaController.SearchImage) {
                        return ((MediaController.SearchImage)value).getAttachName();
                    }
                    if (value instanceof TLRPC.BotInlineResult) {
                        final TLRPC.BotInlineResult botInlineResult = value;
                        final TLRPC.Document document = botInlineResult.document;
                        if (document != null) {
                            return FileLoader.getAttachFileName(document);
                        }
                        final TLRPC.Photo photo = botInlineResult.photo;
                        if (photo != null) {
                            return FileLoader.getAttachFileName(FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize()));
                        }
                        if (botInlineResult.content instanceof TLRPC.TL_webDocument) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append(Utilities.MD5(botInlineResult.content.url));
                            sb.append(".");
                            final TLRPC.WebDocument content = botInlineResult.content;
                            sb.append(ImageLoader.getHttpUrlExtension(content.url, FileLoader.getMimeTypePart(content.mime_type)));
                            return sb.toString();
                        }
                    }
                }
            }
            else if (!this.imagesArrLocations.isEmpty()) {
                if (n >= this.imagesArrLocations.size()) {
                    return null;
                }
                final ImageLocation imageLocation = this.imagesArrLocations.get(n);
                if (imageLocation == null) {
                    return null;
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(imageLocation.location.volume_id);
                sb2.append("_");
                sb2.append(imageLocation.location.local_id);
                sb2.append(".jpg");
                return sb2.toString();
            }
            else if (!this.imagesArr.isEmpty()) {
                if (n >= this.imagesArr.size()) {
                    return null;
                }
                return FileLoader.getMessageFileName(this.imagesArr.get(n).messageOwner);
            }
            return null;
        }
        if (n >= this.secureDocuments.size()) {
            return null;
        }
        final SecureDocument secureDocument = this.secureDocuments.get(n);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(secureDocument.secureFile.dc_id);
        sb3.append("_");
        sb3.append(secureDocument.secureFile.id);
        sb3.append(".jpg");
        return sb3.toString();
    }
    
    private ImageLocation getImageLocation(final int index, final int[] array) {
        if (index < 0) {
            return null;
        }
        if (!this.secureDocuments.isEmpty()) {
            if (index >= this.secureDocuments.size()) {
                return null;
            }
            if (array != null) {
                array[0] = this.secureDocuments.get(index).secureFile.size;
            }
            return ImageLocation.getForSecureDocument(this.secureDocuments.get(index));
        }
        else {
            if (this.imagesArrLocations.isEmpty()) {
                if (!this.imagesArr.isEmpty()) {
                    if (index >= this.imagesArr.size()) {
                        return null;
                    }
                    final MessageObject messageObject = this.imagesArr.get(index);
                    final TLRPC.Message messageOwner = messageObject.messageOwner;
                    if (messageOwner instanceof TLRPC.TL_messageService) {
                        if (messageOwner.action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                            return null;
                        }
                        final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                        if (closestPhotoSizeWithSize != null) {
                            if (array != null) {
                                array[0] = closestPhotoSizeWithSize.size;
                                if (array[0] == 0) {
                                    array[0] = -1;
                                }
                            }
                            return ImageLocation.getForObject(closestPhotoSizeWithSize, messageObject.photoThumbsObject);
                        }
                        if (array != null) {
                            array[0] = -1;
                        }
                    }
                    else {
                        final TLRPC.MessageMedia media = messageOwner.media;
                        if (!(media instanceof TLRPC.TL_messageMediaPhoto) || media.photo == null) {
                            final TLRPC.MessageMedia media2 = messageObject.messageOwner.media;
                            if (!(media2 instanceof TLRPC.TL_messageMediaWebPage) || media2.webpage == null) {
                                final TLRPC.MessageMedia media3 = messageObject.messageOwner.media;
                                if (media3 instanceof TLRPC.TL_messageMediaInvoice) {
                                    return ImageLocation.getForWebFile(WebFile.createWithWebDocument(((TLRPC.TL_messageMediaInvoice)media3).photo));
                                }
                                if (messageObject.getDocument() != null && MessageObject.isDocumentHasThumb(messageObject.getDocument())) {
                                    final TLRPC.Document document = messageObject.getDocument();
                                    final TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
                                    if (array != null) {
                                        array[0] = closestPhotoSizeWithSize2.size;
                                        if (array[0] == 0) {
                                            array[0] = -1;
                                        }
                                    }
                                    return ImageLocation.getForDocument(closestPhotoSizeWithSize2, document);
                                }
                                return null;
                            }
                        }
                        if (messageObject.isGif()) {
                            return ImageLocation.getForDocument(messageObject.getDocument());
                        }
                        final TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                        if (closestPhotoSizeWithSize3 != null) {
                            if (array != null) {
                                array[0] = closestPhotoSizeWithSize3.size;
                                if (array[0] == 0) {
                                    array[0] = -1;
                                }
                            }
                            return ImageLocation.getForObject(closestPhotoSizeWithSize3, messageObject.photoThumbsObject);
                        }
                        if (array != null) {
                            array[0] = -1;
                        }
                    }
                }
                return null;
            }
            if (index >= this.imagesArrLocations.size()) {
                return null;
            }
            if (array != null) {
                array[0] = this.imagesArrLocationsSizes.get(index);
            }
            return this.imagesArrLocations.get(index);
        }
    }
    
    public static PhotoViewer getInstance() {
        final PhotoViewer instance;
        if ((instance = PhotoViewer.Instance) == null) {
            synchronized (PhotoViewer.class) {
                if (PhotoViewer.Instance == null) {
                    PhotoViewer.Instance = new PhotoViewer();
                }
            }
        }
        return instance;
    }
    
    private int getLeftInset() {
        final Object lastInsets = this.lastInsets;
        if (lastInsets != null && Build$VERSION.SDK_INT >= 21) {
            return ((WindowInsets)lastInsets).getSystemWindowInsetLeft();
        }
        return 0;
    }
    
    public static PhotoViewer getPipInstance() {
        return PhotoViewer.PipInstance;
    }
    
    private int getRightInset() {
        final Object lastInsets = this.lastInsets;
        if (lastInsets != null && Build$VERSION.SDK_INT >= 21) {
            return ((WindowInsets)lastInsets).getSystemWindowInsetRight();
        }
        return 0;
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
        return PhotoViewer.Instance != null;
    }
    
    private void hideHint() {
        (this.hintAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.hintTextView, View.ALPHA, new float[] { 0.0f }) });
        this.hintAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                if (animator.equals(PhotoViewer.this.hintAnimation)) {
                    PhotoViewer.this.hintHideRunnable = null;
                    PhotoViewer.this.hintHideRunnable = null;
                }
            }
            
            public void onAnimationEnd(final Animator animator) {
                if (animator.equals(PhotoViewer.this.hintAnimation)) {
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
        if (this.sendPhotoType != 1) {
            return;
        }
        this.photoCropView.setBitmap(null, 0, false, false);
        this.photoCropView.onAppear();
        this.photoCropView.setVisibility(0);
        this.photoCropView.setAlpha(1.0f);
        this.photoCropView.onAppeared();
        this.padImageForHorizontalInsets = true;
    }
    
    public static boolean isPlayingMessage(final MessageObject messageObject) {
        return PhotoViewer.Instance != null && !PhotoViewer.Instance.pipAnimationInProgress && PhotoViewer.Instance.isVisible && messageObject != null && PhotoViewer.Instance.currentMessageObject != null && PhotoViewer.Instance.currentMessageObject.getId() == messageObject.getId() && PhotoViewer.Instance.currentMessageObject.getDialogId() == messageObject.getDialogId();
    }
    
    public static boolean isPlayingMessageInPip(final MessageObject messageObject) {
        return PhotoViewer.PipInstance != null && messageObject != null && PhotoViewer.PipInstance.currentMessageObject != null && PhotoViewer.PipInstance.currentMessageObject.getId() == messageObject.getId() && PhotoViewer.PipInstance.currentMessageObject.getDialogId() == messageObject.getDialogId();
    }
    
    public static boolean isShowingImage(final String s) {
        final PhotoViewer instance = PhotoViewer.Instance;
        boolean b2;
        final boolean b = b2 = false;
        if (instance != null) {
            b2 = b;
            if (PhotoViewer.Instance.isVisible) {
                b2 = b;
                if (!PhotoViewer.Instance.disableShowCheck) {
                    b2 = b;
                    if (s != null) {
                        b2 = b;
                        if (s.equals(PhotoViewer.Instance.currentPathObject)) {
                            b2 = true;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    public static boolean isShowingImage(final MessageObject messageObject) {
        boolean b2;
        final boolean b = b2 = (PhotoViewer.Instance != null && !PhotoViewer.Instance.pipAnimationInProgress && PhotoViewer.Instance.isVisible && !PhotoViewer.Instance.disableShowCheck && messageObject != null && PhotoViewer.Instance.currentMessageObject != null && PhotoViewer.Instance.currentMessageObject.getId() == messageObject.getId() && PhotoViewer.Instance.currentMessageObject.getDialogId() == messageObject.getDialogId());
        if (!b) {
            b2 = b;
            if (PhotoViewer.PipInstance != null) {
                b2 = (PhotoViewer.PipInstance.isVisible && !PhotoViewer.PipInstance.disableShowCheck && messageObject != null && PhotoViewer.PipInstance.currentMessageObject != null && PhotoViewer.PipInstance.currentMessageObject.getId() == messageObject.getId() && PhotoViewer.PipInstance.currentMessageObject.getDialogId() == messageObject.getDialogId());
            }
        }
        return b2;
    }
    
    public static boolean isShowingImage(final TLRPC.BotInlineResult botInlineResult) {
        final PhotoViewer instance = PhotoViewer.Instance;
        boolean b2;
        final boolean b = b2 = false;
        if (instance != null) {
            b2 = b;
            if (PhotoViewer.Instance.isVisible) {
                b2 = b;
                if (!PhotoViewer.Instance.disableShowCheck) {
                    b2 = b;
                    if (botInlineResult != null) {
                        b2 = b;
                        if (PhotoViewer.Instance.currentBotInlineResult != null) {
                            b2 = b;
                            if (botInlineResult.id == PhotoViewer.Instance.currentBotInlineResult.id) {
                                b2 = true;
                            }
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    public static boolean isShowingImage(final TLRPC.FileLocation fileLocation) {
        final PhotoViewer instance = PhotoViewer.Instance;
        boolean b2;
        final boolean b = b2 = false;
        if (instance != null) {
            b2 = b;
            if (PhotoViewer.Instance.isVisible) {
                b2 = b;
                if (!PhotoViewer.Instance.disableShowCheck) {
                    b2 = b;
                    if (fileLocation != null) {
                        b2 = b;
                        if (PhotoViewer.Instance.currentFileLocation != null) {
                            b2 = b;
                            if (fileLocation.local_id == PhotoViewer.Instance.currentFileLocation.location.local_id) {
                                b2 = b;
                                if (fileLocation.volume_id == PhotoViewer.Instance.currentFileLocation.location.volume_id) {
                                    b2 = b;
                                    if (fileLocation.dc_id == PhotoViewer.Instance.currentFileLocation.dc_id) {
                                        b2 = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    private void onActionClick(final boolean b) {
        if ((this.currentMessageObject == null && this.currentBotInlineResult == null) || this.currentFileNames[0] == null) {
            return;
        }
        this.isStreaming = false;
        Object o = this.currentMessageObject;
        Label_0474: {
            if (o == null) {
                break Label_0474;
            }
            final String attachPath = ((MessageObject)o).messageOwner.attachPath;
            if (attachPath == null || attachPath.length() == 0 || !((File)(o = new File(this.currentMessageObject.messageOwner.attachPath))).exists()) {
                o = null;
            }
            Label_0461: {
                MessageObject pathToMessage;
                if ((pathToMessage = (MessageObject)o) != null) {
                    break Label_0461;
                }
                o = (pathToMessage = (MessageObject)FileLoader.getPathToMessage(this.currentMessageObject.messageOwner));
                if (((File)o).exists()) {
                    break Label_0461;
                }
            Label_0456_Outer:
                while (true) {
                    if (!SharedConfig.streamMedia || (int)this.currentMessageObject.getDialogId() == 0 || !this.currentMessageObject.isVideo() || !this.currentMessageObject.canStreamVideo()) {
                        break Block_19;
                    }
                    while (true) {
                        try {
                            final int fileReference = FileLoader.getInstance(this.currentMessageObject.currentAccount).getFileReference(this.currentMessageObject);
                            FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
                            final TLRPC.Document document = this.currentMessageObject.getDocument();
                            final StringBuilder sb = new StringBuilder();
                            sb.append("?account=");
                            sb.append(this.currentMessageObject.currentAccount);
                            sb.append("&id=");
                            sb.append(document.id);
                            sb.append("&hash=");
                            sb.append(document.access_hash);
                            sb.append("&dc=");
                            sb.append(document.dc_id);
                            sb.append("&size=");
                            sb.append(document.size);
                            sb.append("&mime=");
                            sb.append(URLEncoder.encode(document.mime_type, "UTF-8"));
                            sb.append("&rid=");
                            sb.append(fileReference);
                            sb.append("&name=");
                            sb.append(URLEncoder.encode(FileLoader.getDocumentFileName(document), "UTF-8"));
                            sb.append("&reference=");
                            byte[] file_reference;
                            if (document.file_reference != null) {
                                file_reference = document.file_reference;
                            }
                            else {
                                file_reference = new byte[0];
                            }
                            sb.append(Utilities.bytesToHex(file_reference));
                            final String string = sb.toString();
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("tg://");
                            sb2.append(this.currentMessageObject.getFileName());
                            sb2.append(string);
                            o = Uri.parse(sb2.toString());
                            final PhotoViewer photoViewer = this;
                            final boolean b2 = true;
                            photoViewer.isStreaming = b2;
                            final PhotoViewer photoViewer2 = this;
                            final int n = 0;
                            final boolean b3 = false;
                            photoViewer2.checkProgress(n, b3);
                            break Label_0456;
                        }
                        catch (Exception o) {
                            o = null;
                        }
                        try {
                            final PhotoViewer photoViewer = this;
                            final boolean b2 = true;
                            photoViewer.isStreaming = b2;
                            final PhotoViewer photoViewer2 = this;
                            final int n = 0;
                            final boolean b3 = false;
                            photoViewer2.checkProgress(n, b3);
                            Object o2 = null;
                            Object fromFile;
                            TLRPC.BotInlineResult currentBotInlineResult;
                            File directory;
                            Uri uri;
                            Block_20_Outer:Block_18_Outer:
                            while (true) {
                                fromFile = o;
                                if (o2 != null && (fromFile = o) == null) {
                                    fromFile = Uri.fromFile((File)o2);
                                }
                                if (fromFile == null) {
                                    if (b) {
                                        if (this.currentMessageObject != null) {
                                            if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[0])) {
                                                FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
                                            }
                                            else {
                                                FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.getDocument());
                                            }
                                        }
                                        else {
                                            o = this.currentBotInlineResult;
                                            if (o != null) {
                                                if (((TLRPC.BotInlineResult)o).document != null) {
                                                    if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[0])) {
                                                        FileLoader.getInstance(this.currentAccount).loadFile(this.currentBotInlineResult.document, this.currentMessageObject, 1, 0);
                                                    }
                                                    else {
                                                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentBotInlineResult.document);
                                                    }
                                                }
                                                else if (((TLRPC.BotInlineResult)o).content instanceof TLRPC.TL_webDocument) {
                                                    if (!ImageLoader.getInstance().isLoadingHttpFile(this.currentBotInlineResult.content.url)) {
                                                        ImageLoader.getInstance().loadHttpFile(this.currentBotInlineResult.content.url, "mp4", this.currentAccount);
                                                    }
                                                    else {
                                                        ImageLoader.getInstance().cancelLoadHttpFile(this.currentBotInlineResult.content.url);
                                                    }
                                                }
                                            }
                                        }
                                        o = this.centerImage.getStaticThumb();
                                        if (o instanceof OtherDocumentPlaceholderDrawable) {
                                            ((OtherDocumentPlaceholderDrawable)o).checkFileExist();
                                        }
                                    }
                                }
                                else {
                                    if (this.sharedMediaType == 1 && !this.currentMessageObject.canPreviewDocument()) {
                                        AndroidUtilities.openDocument(this.currentMessageObject, this.parentActivity, null);
                                        return;
                                    }
                                    this.preparePlayer((Uri)fromFile, true, false);
                                }
                                return;
                                currentBotInlineResult = this.currentBotInlineResult;
                                Label_0463: {
                                    while (true) {
                                        Block_17: {
                                            break Block_17;
                                            while (true) {
                                                directory = FileLoader.getDirectory(4);
                                                o = new StringBuilder();
                                                ((StringBuilder)o).append(Utilities.MD5(this.currentBotInlineResult.content.url));
                                                ((StringBuilder)o).append(".");
                                                ((StringBuilder)o).append(ImageLoader.getHttpUrlExtension(this.currentBotInlineResult.content.url, "mp4"));
                                                break Block_19;
                                                o = pathToMessage;
                                                break Label_0463;
                                                o2 = (o = null);
                                                continue Block_20_Outer;
                                                continue Label_0456_Outer;
                                                Label_0509: {
                                                    continue Block_18_Outer;
                                                }
                                            }
                                        }
                                        o = currentBotInlineResult.document;
                                        continue;
                                    }
                                }
                                uri = null;
                                o2 = o;
                                o = uri;
                                continue;
                            }
                        }
                        // iftrue(Block_19:, currentBotInlineResult == null)
                        // iftrue(Label_0463:, o = new File(directory, o.toString()).exists())
                        // iftrue(Label_0463:, o = FileLoader.getPathToAttach((TLObject)o).exists())
                        // iftrue(Block_19:, !currentBotInlineResult.content instanceof TLRPC.TL_webDocument)
                        // iftrue(Label_0509:, o == null)
                        catch (Exception ex) {
                            continue;
                        }
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @SuppressLint({ "NewApi", "DrawAllocation" })
    private void onDraw(final Canvas canvas) {
        final int animationInProgress = this.animationInProgress;
        if (animationInProgress != 1) {
            if (this.isVisible || animationInProgress == 2 || this.pipAnimationInProgress) {
                if (this.padImageForHorizontalInsets) {
                    canvas.save();
                    canvas.translate((float)(this.getLeftInset() / 2 - this.getRightInset() / 2), 0.0f);
                }
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
                    this.containerView.invalidate();
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
                        this.containerView.invalidate();
                    }
                    final int switchImageAfterAnimation = this.switchImageAfterAnimation;
                    if (switchImageAfterAnimation != 0) {
                        this.openedFullScreenVideo = false;
                        if (switchImageAfterAnimation == 1) {
                            AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$gYyANufSpmThxLWs9CMz0i1DuDE(this));
                        }
                        else if (switchImageAfterAnimation == 2) {
                            AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$C7KcRvYbXzUEDYfyS8Y8Do1EYMA(this));
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
                if (this.animationInProgress != 2 && !this.pipAnimationInProgress && !this.isInline) {
                    if (this.currentEditMode == 0 && this.sendPhotoType != 1 && this.scale == 1.0f && translationY2 != -1.0f && !this.zoomAnimation) {
                        final float b = this.getContainerViewHeight() / 4.0f;
                        this.backgroundDrawable.setAlpha((int)Math.max(127.0f, (1.0f - Math.min(Math.abs(translationY2), b) / b) * 255.0f));
                    }
                    else {
                        this.backgroundDrawable.setAlpha(255);
                    }
                }
                ImageReceiver imageReceiver = null;
                if (this.currentEditMode == 0 && this.sendPhotoType != 1) {
                    Label_0679: {
                        if (this.scale >= 1.0f && !this.zoomAnimation && !this.zooming) {
                            if (translationX2 > this.maxX + AndroidUtilities.dp(5.0f)) {
                                imageReceiver = this.leftImage;
                                break Label_0679;
                            }
                            if (translationX2 < this.minX - AndroidUtilities.dp(5.0f)) {
                                imageReceiver = this.rightImage;
                                break Label_0679;
                            }
                            this.groupedPhotosListView.setMoveProgress(0.0f);
                        }
                        imageReceiver = null;
                    }
                    this.changingPage = (imageReceiver != null);
                }
                else {
                    imageReceiver = null;
                }
                if (imageReceiver == this.rightImage) {
                    float min = 0.0f;
                    float n2 = 0.0f;
                    float n3 = 0.0f;
                    Label_0790: {
                        if (!this.zoomAnimation) {
                            final float minX = this.minX;
                            if (translationX2 < minX) {
                                min = Math.min(1.0f, (minX - translationX2) / this.getContainerViewWidth());
                                n2 = (1.0f - min) * 0.3f;
                                n3 = (float)(-this.getContainerViewWidth() - AndroidUtilities.dp(30.0f) / 2);
                                break Label_0790;
                            }
                        }
                        n3 = translationX2;
                        min = 1.0f;
                        n2 = 0.0f;
                    }
                    if (imageReceiver.hasBitmapImage()) {
                        canvas.save();
                        canvas.translate((float)(this.getContainerViewWidth() / 2), (float)(this.getContainerViewHeight() / 2));
                        canvas.translate(this.getContainerViewWidth() + AndroidUtilities.dp(30.0f) / 2 + n3, 0.0f);
                        final float n4 = 1.0f - n2;
                        canvas.scale(n4, n4);
                        final int bitmapWidth = imageReceiver.getBitmapWidth();
                        final int bitmapHeight = imageReceiver.getBitmapHeight();
                        final float n5 = (float)this.getContainerViewWidth();
                        final float n6 = (float)bitmapWidth;
                        float n7 = n5 / n6;
                        final float n8 = (float)this.getContainerViewHeight();
                        final float n9 = (float)bitmapHeight;
                        final float n10 = n8 / n9;
                        if (n7 > n10) {
                            n7 = n10;
                        }
                        final int n11 = (int)(n6 * n7);
                        final int n12 = (int)(n9 * n7);
                        imageReceiver.setAlpha(min);
                        imageReceiver.setImageCoords(-n11 / 2, -n12 / 2, n11, n12);
                        imageReceiver.draw(canvas);
                        canvas.restore();
                    }
                    this.groupedPhotosListView.setMoveProgress(-min);
                    canvas.save();
                    canvas.translate(n3, n / scale2);
                    canvas.translate((this.getContainerViewWidth() * (this.scale + 1.0f) + AndroidUtilities.dp(30.0f)) / 2.0f, -n / scale2);
                    this.photoProgressViews[1].setScale(1.0f - n2);
                    this.photoProgressViews[1].setAlpha(min);
                    this.photoProgressViews[1].onDraw(canvas);
                    canvas.restore();
                }
                float n13 = 0.0f;
                float n14 = 0.0f;
                float maxX2 = 0.0f;
                Label_1150: {
                    if (!this.zoomAnimation) {
                        final float maxX = this.maxX;
                        if (translationX2 > maxX && this.currentEditMode == 0 && this.sendPhotoType != 1) {
                            final float min2 = Math.min(1.0f, (translationX2 - maxX) / this.getContainerViewWidth());
                            n13 = 0.3f * min2;
                            n14 = 1.0f - min2;
                            maxX2 = this.maxX;
                            break Label_1150;
                        }
                    }
                    maxX2 = translationX2;
                    n14 = 1.0f;
                    n13 = 0.0f;
                }
                final AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
                final boolean b2 = aspectRatioFrameLayout != null && aspectRatioFrameLayout.getVisibility() == 0;
                if (this.centerImage.hasBitmapImage() || (b2 && this.textureUploaded)) {
                    canvas.save();
                    canvas.translate((float)(this.getContainerViewWidth() / 2 + this.getAdditionX()), (float)(this.getContainerViewHeight() / 2 + this.getAdditionY()));
                    canvas.translate(maxX2, n);
                    final float n15 = scale2 - n13;
                    canvas.scale(n15, n15);
                    int n16;
                    int n17;
                    if (b2 && this.textureUploaded) {
                        n16 = this.videoTextureView.getMeasuredWidth();
                        n17 = this.videoTextureView.getMeasuredHeight();
                    }
                    else {
                        n16 = this.centerImage.getBitmapWidth();
                        n17 = this.centerImage.getBitmapHeight();
                    }
                    final float n18 = (float)this.getContainerViewWidth();
                    final float n19 = (float)n16;
                    final float n20 = n18 / n19;
                    final float n21 = (float)this.getContainerViewHeight();
                    final float n22 = (float)n17;
                    final float n23 = n21 / n22;
                    float n24 = n20;
                    if (n20 > n23) {
                        n24 = n23;
                    }
                    final int n25 = (int)(n19 * n24);
                    final int n26 = (int)(n24 * n22);
                    if (!b2 || !this.textureUploaded || !this.videoCrossfadeStarted || this.videoCrossfadeAlpha != 1.0f) {
                        this.centerImage.setAlpha(n14);
                        this.centerImage.setImageCoords(-n25 / 2, -n26 / 2, n25, n26);
                        this.centerImage.draw(canvas);
                    }
                    if (b2) {
                        final float n27 = canvas.getWidth() / n19;
                        final float n28 = canvas.getHeight() / n22;
                        float n29 = n27;
                        if (n27 > n28) {
                            n29 = n28;
                        }
                        final int n30 = (int)(n22 * n29);
                        if (!this.videoCrossfadeStarted && this.textureUploaded) {
                            this.videoCrossfadeStarted = true;
                            this.videoCrossfadeAlpha = 0.0f;
                            this.videoCrossfadeAlphaLastTime = System.currentTimeMillis();
                        }
                        canvas.translate((float)(-n25 / 2), (float)(-n30 / 2));
                        this.videoTextureView.setAlpha(this.videoCrossfadeAlpha * n14);
                        this.aspectRatioFrameLayout.draw(canvas);
                        if (this.videoCrossfadeStarted && this.videoCrossfadeAlpha < 1.0f) {
                            final long currentTimeMillis = System.currentTimeMillis();
                            final long videoCrossfadeAlphaLastTime = this.videoCrossfadeAlphaLastTime;
                            this.videoCrossfadeAlphaLastTime = currentTimeMillis;
                            final float videoCrossfadeAlpha = this.videoCrossfadeAlpha;
                            final float n31 = (float)(currentTimeMillis - videoCrossfadeAlphaLastTime);
                            float n32;
                            if (this.playerInjected) {
                                n32 = 100.0f;
                            }
                            else {
                                n32 = 200.0f;
                            }
                            this.videoCrossfadeAlpha = videoCrossfadeAlpha + n31 / n32;
                            this.containerView.invalidate();
                            if (this.videoCrossfadeAlpha > 1.0f) {
                                this.videoCrossfadeAlpha = 1.0f;
                            }
                        }
                    }
                    canvas.restore();
                }
                final float n33 = translationX2;
                int n34 = 0;
                Label_1773: {
                    if (this.isCurrentVideo) {
                        if (this.progressView.getVisibility() != 0) {
                            final VideoPlayer videoPlayer = this.videoPlayer;
                            if (videoPlayer == null || !videoPlayer.isPlaying()) {
                                n34 = 1;
                                break Label_1773;
                            }
                        }
                    }
                    else {
                        final boolean b3 = (n34 = ((!b2 && this.videoPlayerControlFrameLayout.getVisibility() != 0) ? 1 : 0)) != 0;
                        if (!b3) {
                            break Label_1773;
                        }
                        final AnimatedFileDrawable currentAnimation = this.currentAnimation;
                        n34 = (b3 ? 1 : 0);
                        if (currentAnimation == null) {
                            break Label_1773;
                        }
                        n34 = (b3 ? 1 : 0);
                        if (currentAnimation.isLoadingStream()) {
                            break Label_1773;
                        }
                    }
                    n34 = 0;
                }
                if (n34 != 0) {
                    canvas.save();
                    canvas.translate(maxX2, n / scale2);
                    this.photoProgressViews[0].setScale(1.0f - n13);
                    this.photoProgressViews[0].setAlpha(n14);
                    this.photoProgressViews[0].onDraw(canvas);
                    canvas.restore();
                }
                if (!this.pipAnimationInProgress && (this.miniProgressView.getVisibility() == 0 || this.miniProgressAnimator != null)) {
                    canvas.save();
                    canvas.translate(this.miniProgressView.getLeft() + maxX2, this.miniProgressView.getTop() + n / scale2);
                    this.miniProgressView.draw(canvas);
                    canvas.restore();
                }
                if (imageReceiver == this.leftImage) {
                    if (imageReceiver.hasBitmapImage()) {
                        canvas.save();
                        canvas.translate((float)(this.getContainerViewWidth() / 2), (float)(this.getContainerViewHeight() / 2));
                        canvas.translate(-(this.getContainerViewWidth() * (this.scale + 1.0f) + AndroidUtilities.dp(30.0f)) / 2.0f + n33, 0.0f);
                        final int bitmapWidth2 = imageReceiver.getBitmapWidth();
                        final int bitmapHeight2 = imageReceiver.getBitmapHeight();
                        final float n35 = (float)this.getContainerViewWidth();
                        final float n36 = (float)bitmapWidth2;
                        final float n37 = n35 / n36;
                        final float n38 = (float)this.getContainerViewHeight();
                        final float n39 = (float)bitmapHeight2;
                        final float n40 = n38 / n39;
                        float n41 = n37;
                        if (n37 > n40) {
                            n41 = n40;
                        }
                        final int n42 = (int)(n36 * n41);
                        final int n43 = (int)(n39 * n41);
                        imageReceiver.setAlpha(1.0f);
                        imageReceiver.setImageCoords(-n42 / 2, -n43 / 2, n42, n43);
                        imageReceiver.draw(canvas);
                        canvas.restore();
                    }
                    this.groupedPhotosListView.setMoveProgress(1.0f - n14);
                    canvas.save();
                    canvas.translate(n33, n / scale2);
                    canvas.translate(-(this.getContainerViewWidth() * (this.scale + 1.0f) + AndroidUtilities.dp(30.0f)) / 2.0f, -n / scale2);
                    this.photoProgressViews[2].setScale(1.0f);
                    this.photoProgressViews[2].setAlpha(1.0f);
                    this.photoProgressViews[2].onDraw(canvas);
                    canvas.restore();
                }
                final int waitingForDraw = this.waitingForDraw;
                if (waitingForDraw != 0) {
                    this.waitingForDraw = waitingForDraw - 1;
                    if (this.waitingForDraw == 0) {
                        if (this.textureImageView != null) {
                            try {
                                this.currentBitmap = Bitmaps.createBitmap(this.videoTextureView.getWidth(), this.videoTextureView.getHeight(), Bitmap$Config.ARGB_8888);
                                this.changedTextureView.getBitmap(this.currentBitmap);
                            }
                            catch (Throwable t) {
                                final Bitmap currentBitmap = this.currentBitmap;
                                if (currentBitmap != null) {
                                    currentBitmap.recycle();
                                    this.currentBitmap = null;
                                }
                                FileLog.e(t);
                            }
                            if (this.currentBitmap != null) {
                                this.textureImageView.setVisibility(0);
                                this.textureImageView.setImageBitmap(this.currentBitmap);
                            }
                            else {
                                this.textureImageView.setImageDrawable((Drawable)null);
                            }
                        }
                        this.pipVideoView.close();
                        this.pipVideoView = null;
                    }
                    else {
                        this.containerView.invalidate();
                    }
                }
                if (this.padImageForHorizontalInsets) {
                    canvas.restore();
                }
                if (this.aspectRatioFrameLayout != null && this.videoForwardDrawable.isAnimating()) {
                    final int n44 = (int)(this.aspectRatioFrameLayout.getMeasuredHeight() * (this.scale - 1.0f)) / 2;
                    final VideoForwardDrawable videoForwardDrawable = this.videoForwardDrawable;
                    final int left = this.aspectRatioFrameLayout.getLeft();
                    final int top = this.aspectRatioFrameLayout.getTop();
                    final int n45 = (int)(n / scale2);
                    videoForwardDrawable.setBounds(left, top - n44 + n45, this.aspectRatioFrameLayout.getRight(), this.aspectRatioFrameLayout.getBottom() + n44 + n45);
                    this.videoForwardDrawable.draw(canvas);
                }
            }
        }
    }
    
    private void onPhotoClosed(final PlaceProviderObject placeProviderObject) {
        this.isVisible = false;
        this.disableShowCheck = true;
        this.currentMessageObject = null;
        this.currentBotInlineResult = null;
        this.currentFileLocation = null;
        this.currentSecureDocument = null;
        this.currentPathObject = null;
        final FrameLayout videoPlayerControlFrameLayout = this.videoPlayerControlFrameLayout;
        if (videoPlayerControlFrameLayout != null) {
            videoPlayerControlFrameLayout.setVisibility(8);
            this.dateTextView.setVisibility(0);
            this.nameTextView.setVisibility(0);
        }
        this.sendPhotoType = 0;
        final ImageReceiver.BitmapHolder currentThumb = this.currentThumb;
        if (currentThumb != null) {
            currentThumb.release();
            this.currentThumb = null;
        }
        this.parentAlert = null;
        final AnimatedFileDrawable currentAnimation = this.currentAnimation;
        if (currentAnimation != null) {
            currentAnimation.setSecondParentView(null);
            this.currentAnimation = null;
        }
        for (int i = 0; i < 3; ++i) {
            final PhotoProgressView[] photoProgressViews = this.photoProgressViews;
            if (photoProgressViews[i] != null) {
                photoProgressViews[i].setBackgroundState(-1, false);
            }
        }
        this.requestVideoPreview(0);
        final VideoTimelinePlayView videoTimelineView = this.videoTimelineView;
        if (videoTimelineView != null) {
            videoTimelineView.destroy();
        }
        this.centerImage.setImageBitmap((Bitmap)null);
        this.leftImage.setImageBitmap((Bitmap)null);
        this.rightImage.setImageBitmap((Bitmap)null);
        this.containerView.post((Runnable)new _$$Lambda$PhotoViewer$ckQHNq394VAfTJ1gr02N4PrzmKY(this));
        final PhotoViewerProvider placeProvider = this.placeProvider;
        if (placeProvider != null) {
            placeProvider.willHidePhotoViewer();
        }
        this.groupedPhotosListView.clear();
        this.placeProvider = null;
        this.selectedPhotosAdapter.notifyDataSetChanged();
        this.disableShowCheck = false;
        if (placeProviderObject != null) {
            placeProviderObject.imageReceiver.setVisible(true, true);
        }
    }
    
    private void onPhotoShow(final MessageObject messageObject, final TLRPC.FileLocation fileLocation, final ArrayList<MessageObject> c, final ArrayList<SecureDocument> c2, final ArrayList<Object> c3, int index, final PlaceProviderObject placeProviderObject) {
        this.classGuid = ConnectionsManager.generateClassGuid();
        this.currentMessageObject = null;
        this.currentFileLocation = null;
        this.currentSecureDocument = null;
        this.currentPathObject = null;
        this.fromCamera = false;
        this.currentBotInlineResult = null;
        this.currentIndex = -1;
        final String[] currentFileNames = this.currentFileNames;
        currentFileNames[0] = null;
        final Integer value = 1;
        currentFileNames[2] = (currentFileNames[1] = null);
        this.avatarsDialogId = 0;
        this.totalImagesCount = 0;
        this.totalImagesCountMerge = 0;
        this.currentEditMode = 0;
        this.isFirstLoading = true;
        this.needSearchImageInArr = false;
        this.loadingMoreImages = false;
        final boolean[] endReached = this.endReached;
        endReached[0] = false;
        endReached[1] = (this.mergeDialogId == 0L);
        this.opennedFromMedia = false;
        this.needCaptionLayout = false;
        this.containerView.setTag((Object)value);
        this.isCurrentVideo = false;
        this.imagesArr.clear();
        this.imagesArrLocations.clear();
        this.imagesArrLocationsSizes.clear();
        this.avatarsArr.clear();
        this.secureDocuments.clear();
        this.imagesArrLocals.clear();
        for (int i = 0; i < 2; ++i) {
            this.imagesByIds[i].clear();
            this.imagesByIdsTemp[i].clear();
        }
        this.imagesArrTemp.clear();
        this.currentUserAvatarLocation = null;
        this.containerView.setPadding(0, 0, 0, 0);
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
        this.isEvent = (placeProviderObject != null && placeProviderObject.isEvent);
        this.sharedMediaType = 0;
        this.allMediaItem.setText(LocaleController.getString("ShowAllMedia", 2131560778));
        this.menuItem.setVisibility(0);
        final ActionBarMenuItem sendItem = this.sendItem;
        final int n = 8;
        sendItem.setVisibility(8);
        this.pipItem.setVisibility(8);
        this.cameraItem.setVisibility(8);
        this.cameraItem.setTag((Object)null);
        this.bottomLayout.setVisibility(0);
        this.bottomLayout.setTag((Object)value);
        this.bottomLayout.setTranslationY(0.0f);
        this.captionTextView.setTranslationY(0.0f);
        this.shareButton.setVisibility(8);
        final QualityChooseView qualityChooseView = this.qualityChooseView;
        if (qualityChooseView != null) {
            qualityChooseView.setVisibility(4);
            this.qualityPicker.setVisibility(4);
            this.qualityChooseView.setTag((Object)null);
        }
        final AnimatorSet qualityChooseViewAnimation = this.qualityChooseViewAnimation;
        if (qualityChooseViewAnimation != null) {
            qualityChooseViewAnimation.cancel();
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
        this.actionBar.setTranslationY(0.0f);
        this.checkImageView.setAlpha(1.0f);
        this.checkImageView.setVisibility(8);
        this.actionBar.setTitleRightMargin(0);
        this.photosCounterView.setAlpha(1.0f);
        this.photosCounterView.setVisibility(8);
        this.pickerView.setVisibility(8);
        this.pickerViewSendButton.setVisibility(8);
        this.pickerViewSendButton.setTranslationY(0.0f);
        this.pickerView.setAlpha(1.0f);
        this.pickerViewSendButton.setAlpha(1.0f);
        this.pickerView.setTranslationY(0.0f);
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
        this.actionBar.setSubtitle(null);
        this.masksItem.setVisibility(8);
        this.muteVideo = false;
        this.muteItem.setImageResource(2131165912);
        this.editorDoneLayout.setVisibility(8);
        this.captionTextView.setTag((Object)null);
        this.captionTextView.setVisibility(4);
        final PhotoCropView photoCropView = this.photoCropView;
        if (photoCropView != null) {
            photoCropView.setVisibility(8);
        }
        final PhotoFilterView photoFilterView = this.photoFilterView;
        if (photoFilterView != null) {
            photoFilterView.setVisibility(8);
        }
        for (int j = 0; j < 3; ++j) {
            final PhotoProgressView[] photoProgressViews = this.photoProgressViews;
            if (photoProgressViews[j] != null) {
                photoProgressViews[j].setBackgroundState(-1, false);
            }
        }
        if (messageObject != null && c == null) {
            final TLRPC.MessageMedia media = messageObject.messageOwner.media;
            if (media instanceof TLRPC.TL_messageMediaWebPage) {
                final TLRPC.WebPage webpage = media.webpage;
                if (webpage != null) {
                    final String site_name = webpage.site_name;
                    if (site_name != null) {
                        final String lowerCase = site_name.toLowerCase();
                        if (lowerCase.equals("instagram") || lowerCase.equals("twitter") || "telegram_album".equals(webpage.type)) {
                            if (!TextUtils.isEmpty((CharSequence)webpage.author)) {
                                this.nameOverride = webpage.author;
                            }
                            if (webpage.cached_page instanceof TLRPC.TL_page) {
                                for (int k = 0; k < webpage.cached_page.blocks.size(); ++k) {
                                    final TLRPC.PageBlock pageBlock = webpage.cached_page.blocks.get(k);
                                    if (pageBlock instanceof TLRPC.TL_pageBlockAuthorDate) {
                                        this.dateOverride = ((TLRPC.TL_pageBlockAuthorDate)pageBlock).published_date;
                                        break;
                                    }
                                }
                            }
                            final ArrayList<MessageObject> webPagePhotos = messageObject.getWebPagePhotos(null, null);
                            if (!webPagePhotos.isEmpty()) {
                                this.slideshowMessageId = messageObject.getId();
                                this.needSearchImageInArr = false;
                                this.imagesArr.addAll(webPagePhotos);
                                this.totalImagesCount = this.imagesArr.size();
                                int index2;
                                if ((index2 = this.imagesArr.indexOf(messageObject)) < 0) {
                                    index2 = 0;
                                }
                                this.setImageIndex(index2, true);
                            }
                        }
                    }
                }
            }
            if (messageObject.canPreviewDocument()) {
                this.sharedMediaType = 1;
                this.allMediaItem.setText(LocaleController.getString("ShowAllFiles", 2131560777));
            }
            if (this.slideshowMessageId == 0) {
                this.imagesArr.add(messageObject);
                if (this.currentAnimation == null && messageObject.eventId == 0L) {
                    final TLRPC.Message messageOwner = messageObject.messageOwner;
                    final TLRPC.MessageMedia media2 = messageOwner.media;
                    if (!(media2 instanceof TLRPC.TL_messageMediaInvoice) && !(media2 instanceof TLRPC.TL_messageMediaWebPage)) {
                        final TLRPC.MessageAction action = messageOwner.action;
                        if (action == null || action instanceof TLRPC.TL_messageActionEmpty) {
                            this.needSearchImageInArr = true;
                            this.imagesByIds[0].put(messageObject.getId(), (Object)messageObject);
                            this.menuItem.showSubItem(2);
                            this.sendItem.setVisibility(0);
                        }
                    }
                }
                else {
                    this.needSearchImageInArr = false;
                }
                this.setImageIndex(0, true);
            }
        }
        else if (c2 != null) {
            this.secureDocuments.addAll(c2);
            this.setImageIndex(index, true);
        }
        else if (fileLocation != null) {
            this.avatarsDialogId = placeProviderObject.dialogId;
            ImageLocation imageLocation;
            if (this.avatarsDialogId > 0) {
                imageLocation = ImageLocation.getForUser(MessagesController.getInstance(this.currentAccount).getUser(this.avatarsDialogId), true);
            }
            else {
                imageLocation = ImageLocation.getForChat(MessagesController.getInstance(this.currentAccount).getChat(-this.avatarsDialogId), true);
            }
            if (imageLocation == null) {
                this.closePhoto(false, false);
                return;
            }
            this.imagesArrLocations.add(imageLocation);
            this.currentUserAvatarLocation = imageLocation;
            this.imagesArrLocationsSizes.add(placeProviderObject.size);
            this.avatarsArr.add(new TLRPC.TL_photoEmpty());
            final ImageView shareButton = this.shareButton;
            int visibility = n;
            if (this.videoPlayerControlFrameLayout.getVisibility() != 0) {
                visibility = 0;
            }
            shareButton.setVisibility(visibility);
            this.allowShare = true;
            this.menuItem.hideSubItem(2);
            if (this.shareButton.getVisibility() == 0) {
                this.menuItem.hideSubItem(10);
            }
            else {
                this.menuItem.showSubItem(10);
            }
            this.setImageIndex(0, true);
        }
        else if (c != null) {
            this.opennedFromMedia = true;
            this.menuItem.showSubItem(4);
            this.sendItem.setVisibility(0);
            this.imagesArr.addAll(c);
            for (int l = 0; l < this.imagesArr.size(); ++l) {
                final MessageObject messageObject2 = this.imagesArr.get(l);
                final SparseArray<MessageObject>[] imagesByIds = this.imagesByIds;
                int n2;
                if (messageObject2.getDialogId() == this.currentDialogId) {
                    n2 = 0;
                }
                else {
                    n2 = 1;
                }
                imagesByIds[n2].put(messageObject2.getId(), (Object)messageObject2);
            }
            if (this.imagesArr.get(index).canPreviewDocument()) {
                this.sharedMediaType = 1;
                this.allMediaItem.setText(LocaleController.getString("ShowAllFiles", 2131560777));
            }
            this.setImageIndex(index, true);
        }
        else if (c3 != null) {
            final int sendPhotoType = this.sendPhotoType;
            if (sendPhotoType == 0 || sendPhotoType == 4 || ((sendPhotoType == 2 || sendPhotoType == 5) && c3.size() > 1)) {
                this.checkImageView.setVisibility(0);
                this.photosCounterView.setVisibility(0);
                this.actionBar.setTitleRightMargin(AndroidUtilities.dp(100.0f));
            }
            final int sendPhotoType2 = this.sendPhotoType;
            if ((sendPhotoType2 == 2 || sendPhotoType2 == 5) && this.placeProvider.canCaptureMorePhotos()) {
                this.cameraItem.setVisibility(0);
                this.cameraItem.setTag((Object)value);
            }
            this.menuItem.setVisibility(8);
            this.imagesArrLocals.addAll(c3);
            final MediaController.PhotoEntry value2 = this.imagesArrLocals.get(index);
            boolean b = false;
            Label_2040: {
                Label_1943: {
                    if (!(value2 instanceof MediaController.PhotoEntry)) {
                        if (value2 instanceof TLRPC.BotInlineResult) {
                            this.cropItem.setVisibility(8);
                            this.rotateItem.setVisibility(8);
                        }
                        else {
                            final ImageView cropItem = this.cropItem;
                            int visibility2;
                            if (value2 instanceof MediaController.SearchImage && ((MediaController.SearchImage)value2).type == 0) {
                                visibility2 = 0;
                            }
                            else {
                                visibility2 = 8;
                            }
                            cropItem.setVisibility(visibility2);
                            this.rotateItem.setVisibility(8);
                            if (this.cropItem.getVisibility() == 0) {
                                break Label_1943;
                            }
                        }
                        b = false;
                        break Label_2040;
                    }
                    if (value2.isVideo) {
                        this.cropItem.setVisibility(8);
                        this.rotateItem.setVisibility(8);
                        this.bottomLayout.setVisibility(0);
                        this.bottomLayout.setTag((Object)value);
                        this.bottomLayout.setTranslationY((float)(-AndroidUtilities.dp(48.0f)));
                    }
                    else {
                        final ImageView cropItem2 = this.cropItem;
                        int visibility3;
                        if (this.sendPhotoType != 1) {
                            visibility3 = 0;
                        }
                        else {
                            visibility3 = 8;
                        }
                        cropItem2.setVisibility(visibility3);
                        final ImageView rotateItem = this.rotateItem;
                        int visibility4;
                        if (this.sendPhotoType != 1) {
                            visibility4 = 8;
                        }
                        else {
                            visibility4 = 0;
                        }
                        rotateItem.setVisibility(visibility4);
                    }
                }
                b = true;
            }
            final ChatActivity parentChatActivity = this.parentChatActivity;
            if (parentChatActivity != null) {
                final TLRPC.EncryptedChat currentEncryptedChat = parentChatActivity.currentEncryptedChat;
                if (currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(currentEncryptedChat.layer) >= 46) {
                    this.mentionsAdapter.setChatInfo(this.parentChatActivity.chatInfo);
                    this.mentionsAdapter.setNeedUsernames(this.parentChatActivity.currentChat != null);
                    this.mentionsAdapter.setNeedBotContext(false);
                    boolean needCaptionLayout = false;
                    Label_2158: {
                        if (b) {
                            final PhotoViewerProvider placeProvider = this.placeProvider;
                            if (placeProvider == null || (placeProvider != null && placeProvider.allowCaption())) {
                                needCaptionLayout = true;
                                break Label_2158;
                            }
                        }
                        needCaptionLayout = false;
                    }
                    this.needCaptionLayout = needCaptionLayout;
                    final PhotoViewerCaptionEnterView captionEditText = this.captionEditText;
                    int visibility5;
                    if (this.needCaptionLayout) {
                        visibility5 = 0;
                    }
                    else {
                        visibility5 = 8;
                    }
                    captionEditText.setVisibility(visibility5);
                    if (this.needCaptionLayout) {
                        this.captionEditText.onCreate();
                    }
                }
            }
            this.pickerView.setVisibility(0);
            this.pickerViewSendButton.setVisibility(0);
            this.pickerViewSendButton.setTranslationY(0.0f);
            this.pickerViewSendButton.setAlpha(1.0f);
            this.bottomLayout.setVisibility(8);
            this.bottomLayout.setTag((Object)null);
            this.containerView.setTag((Object)null);
            this.setImageIndex(index, true);
            final int sendPhotoType3 = this.sendPhotoType;
            if (sendPhotoType3 == 1) {
                this.paintItem.setVisibility(0);
                this.tuneItem.setVisibility(0);
            }
            else if (sendPhotoType3 != 4 && sendPhotoType3 != 5) {
                this.paintItem.setVisibility(this.cropItem.getVisibility());
                this.tuneItem.setVisibility(this.cropItem.getVisibility());
            }
            else {
                this.paintItem.setVisibility(8);
                this.tuneItem.setVisibility(8);
            }
            this.updateSelectedCount();
        }
        TLRPC.User currentUser = null;
        if (this.currentAnimation == null && !this.isEvent) {
            if (this.currentDialogId != 0L && this.totalImagesCount == 0) {
                DataQuery.getInstance(this.currentAccount).getMediaCount(this.currentDialogId, this.sharedMediaType, this.classGuid, true);
                if (this.mergeDialogId != 0L) {
                    DataQuery.getInstance(this.currentAccount).getMediaCount(this.mergeDialogId, this.sharedMediaType, this.classGuid, true);
                }
            }
            else if (this.avatarsDialogId != 0) {
                MessagesController.getInstance(this.currentAccount).loadDialogPhotos(this.avatarsDialogId, 80, 0L, true, this.classGuid);
            }
        }
        final MessageObject currentMessageObject = this.currentMessageObject;
        Label_2507: {
            if (currentMessageObject == null || !currentMessageObject.isVideo()) {
                final TLRPC.BotInlineResult currentBotInlineResult = this.currentBotInlineResult;
                if (currentBotInlineResult != null) {
                    if (currentBotInlineResult.type.equals("video")) {
                        break Label_2507;
                    }
                    if (MessageObject.isVideoDocument(this.currentBotInlineResult.document)) {
                        break Label_2507;
                    }
                }
                if (this.imagesArrLocals.isEmpty()) {
                    return;
                }
                final MediaController.SearchImage value3 = this.imagesArrLocals.get(index);
                final ChatActivity parentChatActivity2 = this.parentChatActivity;
                if (parentChatActivity2 != null) {
                    currentUser = parentChatActivity2.getCurrentUser();
                }
                final ChatActivity parentChatActivity3 = this.parentChatActivity;
                if (parentChatActivity3 != null && !parentChatActivity3.isSecretChat() && currentUser != null && !currentUser.bot && !this.parentChatActivity.isEditingMessageMedia()) {
                    index = 1;
                }
                else {
                    index = 0;
                }
                int n3;
                if (value3 instanceof MediaController.PhotoEntry) {
                    final MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry)value3;
                    n3 = index;
                    if (photoEntry.isVideo) {
                        this.preparePlayer(Uri.fromFile(new File(photoEntry.path)), false, false);
                        n3 = index;
                    }
                }
                else if ((n3 = index) != 0) {
                    n3 = index;
                    if (value3 instanceof MediaController.SearchImage) {
                        if (value3.type == 0) {
                            n3 = 1;
                        }
                        else {
                            n3 = 0;
                        }
                    }
                }
                if (n3 != 0) {
                    this.timeItem.setVisibility(0);
                }
                return;
            }
        }
        this.onActionClick(false);
    }
    
    private void onSharePressed() {
        if (this.parentActivity != null) {
            if (this.allowShare) {
                try {
                    final MessageObject currentMessageObject = this.currentMessageObject;
                    final boolean b = false;
                    final File file = null;
                    final File file2 = null;
                    int n;
                    File file4;
                    if (currentMessageObject != null) {
                        final boolean video = this.currentMessageObject.isVideo();
                        File file3 = file2;
                        if (!TextUtils.isEmpty((CharSequence)this.currentMessageObject.messageOwner.attachPath)) {
                            file3 = new File(this.currentMessageObject.messageOwner.attachPath);
                            if (!file3.exists()) {
                                file3 = file2;
                            }
                        }
                        n = (video ? 1 : 0);
                        if ((file4 = file3) == null) {
                            file4 = FileLoader.getPathToMessage(this.currentMessageObject.messageOwner);
                            n = (video ? 1 : 0);
                        }
                    }
                    else {
                        n = (b ? 1 : 0);
                        file4 = file;
                        if (this.currentFileLocation != null) {
                            file4 = FileLoader.getPathToAttach(this.currentFileLocation.location, this.avatarsDialogId != 0 || this.isEvent);
                            n = (b ? 1 : 0);
                        }
                    }
                    if (file4.exists()) {
                        final Intent intent = new Intent("android.intent.action.SEND");
                        if (n != 0) {
                            intent.setType("video/mp4");
                        }
                        else if (this.currentMessageObject != null) {
                            intent.setType(this.currentMessageObject.getMimeType());
                        }
                        else {
                            intent.setType("image/jpeg");
                        }
                        if (Build$VERSION.SDK_INT >= 24) {
                            try {
                                intent.putExtra("android.intent.extra.STREAM", (Parcelable)FileProvider.getUriForFile((Context)this.parentActivity, "org.telegram.messenger.provider", file4));
                                intent.setFlags(1);
                            }
                            catch (Exception ex2) {
                                intent.putExtra("android.intent.extra.STREAM", (Parcelable)Uri.fromFile(file4));
                            }
                        }
                        else {
                            intent.putExtra("android.intent.extra.STREAM", (Parcelable)Uri.fromFile(file4));
                        }
                        this.parentActivity.startActivityForResult(Intent.createChooser(intent, (CharSequence)LocaleController.getString("ShareFile", 2131560748)), 500);
                    }
                    else {
                        this.showDownloadAlert();
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
    }
    
    private boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.animationInProgress != 0 || this.animationStartTime != 0L) {
            return false;
        }
        final int currentEditMode = this.currentEditMode;
        if (currentEditMode == 2) {
            this.photoFilterView.onTouch(motionEvent);
            return true;
        }
        if (currentEditMode != 1) {
            if (this.sendPhotoType != 1) {
                if (!this.captionEditText.isPopupShowing() && !this.captionEditText.isKeyboardVisible()) {
                    if (this.currentEditMode == 0 && this.sendPhotoType != 1 && motionEvent.getPointerCount() == 1 && this.gestureDetector.onTouchEvent(motionEvent) && this.doubleTap) {
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
                                this.containerView.invalidate();
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
                                    final QualityChooseView qualityChooseView = this.qualityChooseView;
                                    if (qualityChooseView != null && qualityChooseView.getVisibility() == 0) {
                                        return true;
                                    }
                                }
                                if (this.placeProvider.canScrollAway() && this.currentEditMode == 0 && this.sendPhotoType != 1 && this.canDragDown && !this.draggingDown && this.scale == 1.0f && abs2 >= AndroidUtilities.dp(30.0f) && abs2 / 2.0f > abs) {
                                    this.draggingDown = true;
                                    this.moving = false;
                                    this.dragY = motionEvent.getY();
                                    if (this.isActionBarVisible && this.containerView.getTag() != null) {
                                        this.toggleActionBar(false, true);
                                    }
                                    else if (this.pickerView.getVisibility() == 0) {
                                        this.toggleActionBar(false, true);
                                        this.togglePhotosListView(false, true);
                                        this.toggleCheckImageView(false);
                                    }
                                    return true;
                                }
                                if (this.draggingDown) {
                                    this.translationY = motionEvent.getY() - this.dragY;
                                    this.containerView.invalidate();
                                }
                                else if (!this.invalidCoords && this.animationStartTime == 0L) {
                                    float a = this.moveStartX - motionEvent.getX();
                                    float a2 = this.moveStartY - motionEvent.getY();
                                    if (this.moving || this.currentEditMode != 0 || (this.scale == 1.0f && Math.abs(a2) + AndroidUtilities.dp(12.0f) < Math.abs(a)) || this.scale != 1.0f) {
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
                                        Label_0877: {
                                            if (this.translationX >= this.minX || (this.currentEditMode == 0 && this.rightImage.hasImageSet())) {
                                                n5 = a;
                                                if (this.translationX <= this.maxX) {
                                                    break Label_0877;
                                                }
                                                if (this.currentEditMode == 0) {
                                                    n5 = a;
                                                    if (this.leftImage.hasImageSet()) {
                                                        break Label_0877;
                                                    }
                                                }
                                            }
                                            n5 = a / 3.0f;
                                        }
                                        final float maxY = this.maxY;
                                        Label_0997: {
                                            if (maxY == 0.0f) {
                                                final float minY = this.minY;
                                                if (minY == 0.0f && this.currentEditMode == 0 && this.sendPhotoType != 1) {
                                                    final float translationY = this.translationY;
                                                    if (translationY - a2 < minY) {
                                                        this.translationY = minY;
                                                        a2 = n2;
                                                        break Label_0997;
                                                    }
                                                    if (translationY - a2 > maxY) {
                                                        this.translationY = maxY;
                                                        a2 = n2;
                                                    }
                                                    break Label_0997;
                                                }
                                            }
                                            final float translationY2 = this.translationY;
                                            if (translationY2 < this.minY || translationY2 > this.maxY) {
                                                a2 /= 3.0f;
                                            }
                                        }
                                        this.translationX -= n5;
                                        if (this.scale != 1.0f || this.currentEditMode != 0) {
                                            this.translationY -= a2;
                                        }
                                        this.containerView.invalidate();
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
                                    this.closePhoto(true, false);
                                }
                                else {
                                    if (this.pickerView.getVisibility() == 0) {
                                        this.toggleActionBar(true, true);
                                        this.toggleCheckImageView(true);
                                    }
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
                                if (this.currentEditMode == 0 && this.sendPhotoType != 1) {
                                    if ((this.translationX < this.minX - this.getContainerViewWidth() / 3 || xVelocity < -AndroidUtilities.dp(650.0f)) && this.rightImage.hasImageSet()) {
                                        this.goToNext();
                                        return true;
                                    }
                                    if ((this.translationX > this.maxX + this.getContainerViewWidth() / 3 || xVelocity > AndroidUtilities.dp(650.0f)) && this.leftImage.hasImageSet()) {
                                        this.goToPrev();
                                        return true;
                                    }
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
                    return false;
                }
                else if (motionEvent.getAction() == 1) {
                    this.closeCaptionEnter(true);
                }
            }
        }
        return true;
    }
    
    private void openCaptionEnter() {
        if (this.imageMoveAnimation == null && this.changeModeAnimation == null && this.currentEditMode == 0) {
            final int sendPhotoType = this.sendPhotoType;
            if (sendPhotoType != 1) {
                if (sendPhotoType != 3) {
                    this.selectedPhotosListView.setVisibility(8);
                    this.selectedPhotosListView.setEnabled(false);
                    this.selectedPhotosListView.setAlpha(0.0f);
                    this.selectedPhotosListView.setTranslationY((float)(-AndroidUtilities.dp(10.0f)));
                    this.photosCounterView.setRotationX(0.0f);
                    this.isPhotosListViewVisible = false;
                    this.captionEditText.setTag((Object)1);
                    this.captionEditText.openKeyboard();
                    this.captionEditText.setImportantForAccessibility(0);
                    this.lastTitle = this.actionBar.getTitle();
                    if (this.isCurrentVideo) {
                        final ActionBar actionBar = this.actionBar;
                        int n;
                        String s;
                        if (this.muteVideo) {
                            n = 2131559589;
                            s = "GifCaption";
                        }
                        else {
                            n = 2131561043;
                            s = "VideoCaption";
                        }
                        actionBar.setTitle(LocaleController.getString(s, n));
                        this.actionBar.setSubtitle(null);
                    }
                    else {
                        this.actionBar.setTitle(LocaleController.getString("PhotoCaption", 2131560434));
                    }
                }
            }
        }
    }
    
    private void preparePlayer(final Uri currentPlayingVideoFile, final boolean playWhenReady, final boolean b) {
        if (!b) {
            this.currentPlayingVideoFile = currentPlayingVideoFile;
        }
        if (this.parentActivity == null) {
            return;
        }
        final int n = 0;
        this.streamingAlertShown = false;
        this.startedPlayTime = SystemClock.elapsedRealtime();
        this.currentVideoFinishedLoading = false;
        this.lastBufferedPositionCheck = 0L;
        boolean b2 = true;
        this.firstAnimationDelay = true;
        this.inPreview = b;
        this.releasePlayer(false);
        if (this.videoTextureView == null) {
            (this.aspectRatioFrameLayout = new AspectRatioFrameLayout(this.parentActivity) {
                @Override
                protected void onMeasure(final int n, final int n2) {
                    super.onMeasure(n, n2);
                    if (PhotoViewer.this.textureImageView != null) {
                        final ViewGroup$LayoutParams layoutParams = PhotoViewer.this.textureImageView.getLayoutParams();
                        layoutParams.width = this.getMeasuredWidth();
                        layoutParams.height = this.getMeasuredHeight();
                    }
                }
            }).setVisibility(4);
            this.containerView.addView((View)this.aspectRatioFrameLayout, 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 17));
            this.videoTextureView = new TextureView((Context)this.parentActivity);
            final SurfaceTexture injectingVideoPlayerSurface = this.injectingVideoPlayerSurface;
            if (injectingVideoPlayerSurface != null) {
                this.videoTextureView.setSurfaceTexture(injectingVideoPlayerSurface);
                this.textureUploaded = true;
                this.injectingVideoPlayerSurface = null;
            }
            this.videoTextureView.setPivotX(0.0f);
            this.videoTextureView.setPivotY(0.0f);
            this.videoTextureView.setOpaque(false);
            this.aspectRatioFrameLayout.addView((View)this.videoTextureView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 17));
        }
        if (Build$VERSION.SDK_INT >= 21 && this.textureImageView == null) {
            (this.textureImageView = new ImageView((Context)this.parentActivity)).setBackgroundColor(-65536);
            this.textureImageView.setPivotX(0.0f);
            this.textureImageView.setPivotY(0.0f);
            this.textureImageView.setVisibility(4);
            this.containerView.addView((View)this.textureImageView);
        }
        this.textureUploaded = false;
        this.videoCrossfadeStarted = false;
        this.videoTextureView.setAlpha(this.videoCrossfadeAlpha = 0.0f);
        this.videoPlayButton.setImageResource(2131165479);
        this.playerWasReady = false;
        if (this.videoPlayer == null) {
            final VideoPlayer injectingVideoPlayer = this.injectingVideoPlayer;
            if (injectingVideoPlayer != null) {
                this.videoPlayer = injectingVideoPlayer;
                this.injectingVideoPlayer = null;
                this.playerInjected = true;
                this.updatePlayerState(this.videoPlayer.getPlayWhenReady(), this.videoPlayer.getPlaybackState());
                b2 = false;
            }
            else {
                this.videoPlayer = new VideoPlayer();
            }
            this.videoPlayer.setTextureView(this.videoTextureView);
            this.videoPlayer.setDelegate((VideoPlayer.VideoPlayerDelegate)new VideoPlayer.VideoPlayerDelegate() {
                @Override
                public void onError(final Exception ex) {
                    FileLog.e(ex);
                    if (!PhotoViewer.this.menuItem.isSubItemVisible(11)) {
                        return;
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)PhotoViewer.this.parentActivity);
                    builder.setTitle(LocaleController.getString("AppName", 2131558635));
                    builder.setMessage(LocaleController.getString("CantPlayVideo", 2131558903));
                    builder.setPositiveButton(LocaleController.getString("Open", 2131560110), (DialogInterface$OnClickListener)new _$$Lambda$PhotoViewer$26$aI6m9nXQqJHr0j_z6Zhi3c59_Vo(this));
                    builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                    PhotoViewer.this.showAlertDialog(builder);
                }
                
                @Override
                public void onRenderedFirstFrame() {
                    if (!PhotoViewer.this.textureUploaded) {
                        PhotoViewer.this.textureUploaded = true;
                        PhotoViewer.this.containerView.invalidate();
                    }
                }
                
                @Override
                public void onStateChanged(final boolean b, final int n) {
                    PhotoViewer.this.updatePlayerState(b, n);
                }
                
                @Override
                public boolean onSurfaceDestroyed(final SurfaceTexture surfaceTexture) {
                    if (PhotoViewer.this.changingTextureView) {
                        PhotoViewer.this.changingTextureView = false;
                        if (PhotoViewer.this.isInline) {
                            if (PhotoViewer.this.isInline) {
                                PhotoViewer.this.waitingForFirstTextureUpload = 1;
                            }
                            PhotoViewer.this.changedTextureView.setSurfaceTexture(surfaceTexture);
                            PhotoViewer.this.changedTextureView.setSurfaceTextureListener(PhotoViewer.this.surfaceTextureListener);
                            PhotoViewer.this.changedTextureView.setVisibility(0);
                            return true;
                        }
                    }
                    return false;
                }
                
                @Override
                public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
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
                        if (Build$VERSION.SDK_INT >= 21) {
                            PhotoViewer.this.aspectRatioFrameLayout.getLocationInWindow(PhotoViewer.this.pipPosition);
                            final int[] access$10800 = PhotoViewer.this.pipPosition;
                            access$10800[1] -= (int)PhotoViewer.this.containerView.getTranslationY();
                            PhotoViewer.this.textureImageView.setTranslationX(PhotoViewer.this.textureImageView.getTranslationX() + PhotoViewer.this.getLeftInset());
                            PhotoViewer.this.videoTextureView.setTranslationX(PhotoViewer.this.videoTextureView.getTranslationX() + PhotoViewer.this.getLeftInset() - PhotoViewer.this.aspectRatioFrameLayout.getX());
                            final AnimatorSet set = new AnimatorSet();
                            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.textureImageView, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.textureImageView, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.textureImageView, View.TRANSLATION_X, new float[] { (float)PhotoViewer.this.pipPosition[0] }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.textureImageView, View.TRANSLATION_Y, new float[] { (float)PhotoViewer.this.pipPosition[1] }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.videoTextureView, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.videoTextureView, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.videoTextureView, View.TRANSLATION_X, new float[] { PhotoViewer.this.pipPosition[0] - PhotoViewer.this.aspectRatioFrameLayout.getX() }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.videoTextureView, View.TRANSLATION_Y, new float[] { PhotoViewer.this.pipPosition[1] - PhotoViewer.this.aspectRatioFrameLayout.getY() }), (Animator)ObjectAnimator.ofInt((Object)PhotoViewer.this.backgroundDrawable, (Property)AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[] { 255 }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.actionBar, View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.bottomLayout, View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.captionTextView, View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.groupedPhotosListView, View.ALPHA, new float[] { 1.0f }) });
                            set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                            set.setDuration(250L);
                            set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                                public void onAnimationEnd(final Animator animator) {
                                    PhotoViewer.this.pipAnimationInProgress = false;
                                }
                            });
                            set.start();
                        }
                        PhotoViewer.this.waitingForFirstTextureUpload = 0;
                    }
                }
                
                @Override
                public void onVideoSizeChanged(final int n, final int n2, final int n3, float n4) {
                    if (PhotoViewer.this.aspectRatioFrameLayout != null) {
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
                        final AspectRatioFrameLayout access$2700 = PhotoViewer.this.aspectRatioFrameLayout;
                        if (n5 == 0) {
                            n4 = 1.0f;
                        }
                        else {
                            n4 = n6 * n4 / n5;
                        }
                        access$2700.setAspectRatio(n4, n3);
                    }
                }
            });
        }
        else {
            b2 = false;
        }
        if (b2) {
            this.seekToProgressPending = this.seekToProgressPending2;
            this.videoPlayer.preparePlayer(currentPlayingVideoFile, "other");
            this.videoPlayerSeekbar.setProgress(0.0f);
            this.videoTimelineView.setProgress(0.0f);
            this.videoPlayerSeekbar.setBufferedProgress(0.0f);
            this.videoPlayer.setPlayWhenReady(playWhenReady);
        }
        final TLRPC.BotInlineResult currentBotInlineResult = this.currentBotInlineResult;
        if (currentBotInlineResult != null && (currentBotInlineResult.type.equals("video") || MessageObject.isVideoDocument(this.currentBotInlineResult.document))) {
            this.bottomLayout.setVisibility(0);
            this.bottomLayout.setPadding(0, 0, AndroidUtilities.dp(84.0f), 0);
            this.pickerView.setVisibility(8);
        }
        else {
            this.bottomLayout.setPadding(0, 0, 0, 0);
        }
        final FrameLayout videoPlayerControlFrameLayout = this.videoPlayerControlFrameLayout;
        int visibility = n;
        if (this.isCurrentVideo) {
            visibility = 8;
        }
        videoPlayerControlFrameLayout.setVisibility(visibility);
        this.dateTextView.setVisibility(8);
        this.nameTextView.setVisibility(8);
        if (this.allowShare) {
            this.shareButton.setVisibility(8);
            this.menuItem.showSubItem(10);
        }
        this.inPreview = b;
        this.updateAccessibilityOverlayVisibility();
    }
    
    private void processOpenVideo(final String s, final boolean muteVideo) {
        if (this.currentLoadingVideoRunnable != null) {
            Utilities.globalQueue.cancelRunnable(this.currentLoadingVideoRunnable);
            this.currentLoadingVideoRunnable = null;
        }
        this.videoPreviewMessageObject = null;
        this.setCompressItemEnabled(false, true);
        this.muteVideo = muteVideo;
        this.videoTimelineView.setVideoPath(s);
        this.compressionsCount = -1;
        this.rotationValue = 0;
        this.videoFramerate = 25;
        this.originalSize = new File(s).length();
        Utilities.globalQueue.postRunnable(this.currentLoadingVideoRunnable = new Runnable() {
            @Override
            public void run() {
                if (PhotoViewer.this.currentLoadingVideoRunnable != this) {
                    return;
                }
                final int[] array = new int[9];
                AnimatedFileDrawable.getVideoInfo(s, array);
                if (PhotoViewer.this.currentLoadingVideoRunnable != this) {
                    return;
                }
                PhotoViewer.this.currentLoadingVideoRunnable = null;
                AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$43$_nOxRPCy18s7poWP44IsgujSQ7Q(this, array));
            }
        });
    }
    
    private void redraw(final int n) {
        if (n < 6) {
            final FrameLayoutDrawer containerView = this.containerView;
            if (containerView != null) {
                containerView.invalidate();
                AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$PaqzhTFF8kO_z23DxN4SVmAhhn4(this, n), 100L);
            }
        }
    }
    
    private void releasePlayer(final boolean b) {
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
            this.pipItem.setAlpha(0.5f);
        }
        if (this.keepScreenOnFlagSet) {
            try {
                this.parentActivity.getWindow().clearFlags(128);
                this.keepScreenOnFlagSet = false;
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        final AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
        Label_0130: {
            if (aspectRatioFrameLayout == null) {
                break Label_0130;
            }
            while (true) {
                try {
                    this.containerView.removeView((View)aspectRatioFrameLayout);
                    this.aspectRatioFrameLayout = null;
                    if (this.videoTextureView != null) {
                        this.videoTextureView = null;
                    }
                    if (this.isPlaying) {
                        this.isPlaying = false;
                        if (!b) {
                            this.videoPlayButton.setImageResource(2131165479);
                        }
                        AndroidUtilities.cancelRunOnUIThread(this.updateProgressRunnable);
                    }
                    if (!b && !this.inPreview && !this.requestingPreview) {
                        this.videoPlayerControlFrameLayout.setVisibility(8);
                        this.dateTextView.setVisibility(0);
                        this.nameTextView.setVisibility(0);
                        if (this.allowShare) {
                            this.shareButton.setVisibility(0);
                            this.menuItem.hideSubItem(10);
                        }
                    }
                }
                catch (Throwable t) {
                    continue;
                }
                break;
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
    
    private void requestVideoPreview(final int n) {
        if (this.videoPreviewMessageObject != null) {
            MediaController.getInstance().cancelVideoConvert(this.videoPreviewMessageObject);
        }
        final boolean b = this.requestingPreview && !this.tryStartRequestPreviewOnFinish;
        this.requestingPreview = false;
        this.loadInitialVideo = false;
        this.progressView.setVisibility(4);
        if (n == 1) {
            if (this.selectedCompression == this.compressionsCount - 1) {
                this.tryStartRequestPreviewOnFinish = false;
                if (!b) {
                    this.preparePlayer(this.currentPlayingVideoFile, false, false);
                }
                else {
                    this.progressView.setVisibility(0);
                    this.loadInitialVideo = true;
                }
            }
            else {
                this.requestingPreview = true;
                this.releasePlayer(false);
                if (this.videoPreviewMessageObject == null) {
                    final TLRPC.TL_message tl_message = new TLRPC.TL_message();
                    tl_message.id = 0;
                    tl_message.message = "";
                    tl_message.media = new TLRPC.TL_messageMediaEmpty();
                    tl_message.action = new TLRPC.TL_messageActionEmpty();
                    this.videoPreviewMessageObject = new MessageObject(UserConfig.selectedAccount, tl_message, false);
                    this.videoPreviewMessageObject.messageOwner.attachPath = new File(FileLoader.getDirectory(4), "video_preview.mp4").getAbsolutePath();
                    this.videoPreviewMessageObject.videoEditedInfo = new VideoEditedInfo();
                    final VideoEditedInfo videoEditedInfo = this.videoPreviewMessageObject.videoEditedInfo;
                    videoEditedInfo.rotationValue = this.rotationValue;
                    videoEditedInfo.originalWidth = this.originalWidth;
                    videoEditedInfo.originalHeight = this.originalHeight;
                    videoEditedInfo.framerate = this.videoFramerate;
                    videoEditedInfo.originalPath = this.currentPlayingVideoFile.getPath();
                }
                final VideoEditedInfo videoEditedInfo2 = this.videoPreviewMessageObject.videoEditedInfo;
                final long startTime = this.startTime;
                videoEditedInfo2.startTime = startTime;
                final long endTime = this.endTime;
                videoEditedInfo2.endTime = endTime;
                long n2 = startTime;
                if (startTime == -1L) {
                    n2 = 0L;
                }
                long n3 = endTime;
                if (endTime == -1L) {
                    n3 = (long)(this.videoDuration * 1000.0f);
                }
                if (n3 - n2 > 5000000L) {
                    this.videoPreviewMessageObject.videoEditedInfo.endTime = n2 + 5000000L;
                }
                final VideoEditedInfo videoEditedInfo3 = this.videoPreviewMessageObject.videoEditedInfo;
                videoEditedInfo3.bitrate = this.bitrate;
                videoEditedInfo3.resultWidth = this.resultWidth;
                videoEditedInfo3.resultHeight = this.resultHeight;
                if (!MediaController.getInstance().scheduleVideoConvert(this.videoPreviewMessageObject, true)) {
                    this.tryStartRequestPreviewOnFinish = true;
                }
                this.requestingPreview = true;
                this.progressView.setVisibility(0);
            }
        }
        else {
            this.tryStartRequestPreviewOnFinish = false;
            if (n == 2) {
                this.preparePlayer(this.currentPlayingVideoFile, false, false);
            }
        }
        this.containerView.invalidate();
    }
    
    private void setCompressItemEnabled(final boolean b, final boolean b2) {
        final ImageView compressItem = this.compressItem;
        if (compressItem == null) {
            return;
        }
        if ((b && compressItem.getTag() != null) || (!b && this.compressItem.getTag() == null)) {
            return;
        }
        final ImageView compressItem2 = this.compressItem;
        Integer value;
        if (b) {
            value = 1;
        }
        else {
            value = null;
        }
        compressItem2.setTag((Object)value);
        this.compressItem.setEnabled(b);
        this.compressItem.setClickable(b);
        final AnimatorSet compressItemAnimation = this.compressItemAnimation;
        if (compressItemAnimation != null) {
            compressItemAnimation.cancel();
            this.compressItemAnimation = null;
        }
        float alpha = 1.0f;
        if (b2) {
            this.compressItemAnimation = new AnimatorSet();
            final AnimatorSet compressItemAnimation2 = this.compressItemAnimation;
            final ImageView compressItem3 = this.compressItem;
            final Property alpha2 = View.ALPHA;
            if (!b) {
                alpha = 0.5f;
            }
            compressItemAnimation2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)compressItem3, alpha2, new float[] { alpha }) });
            this.compressItemAnimation.setDuration(180L);
            this.compressItemAnimation.setInterpolator((TimeInterpolator)PhotoViewer.decelerateInterpolator);
            this.compressItemAnimation.start();
        }
        else {
            final ImageView compressItem4 = this.compressItem;
            if (!b) {
                alpha = 0.5f;
            }
            compressItem4.setAlpha(alpha);
        }
    }
    
    private void setCropBitmap() {
        if (this.sendPhotoType != 1) {
            return;
        }
        final Bitmap bitmap = this.centerImage.getBitmap();
        int n = this.centerImage.getOrientation();
        Bitmap bitmap2;
        if ((bitmap2 = bitmap) == null) {
            bitmap2 = this.animatingImageView.getBitmap();
            n = this.animatingImageView.getOrientation();
        }
        if (bitmap2 != null) {
            this.photoCropView.setBitmap(bitmap2, n, false, false);
            if (this.currentEditMode == 0) {
                this.setCropTranslations(false);
            }
        }
    }
    
    private void setCropTranslations(final boolean b) {
        if (this.sendPhotoType != 1) {
            return;
        }
        final int bitmapWidth = this.centerImage.getBitmapWidth();
        final int bitmapHeight = this.centerImage.getBitmapHeight();
        if (bitmapWidth != 0) {
            if (bitmapHeight != 0) {
                final float n = (float)this.getContainerViewWidth();
                final float n2 = (float)bitmapWidth;
                final float n3 = n / n2;
                final float n4 = (float)this.getContainerViewHeight();
                final float n5 = (float)bitmapHeight;
                final float n6 = n4 / n5;
                float n7 = n3;
                if (n3 > n6) {
                    n7 = n6;
                }
                final float n8 = (float)Math.min(this.getContainerViewWidth(1), this.getContainerViewHeight(1));
                float n9 = n8 / n2;
                final float n10 = n8 / n5;
                if (n9 <= n10) {
                    n9 = n10;
                }
                if (b) {
                    this.animationStartTime = System.currentTimeMillis();
                    this.animateToX = (float)(this.getLeftInset() / 2 - this.getRightInset() / 2);
                    final int currentEditMode = this.currentEditMode;
                    if (currentEditMode == 2) {
                        this.animateToY = (float)(AndroidUtilities.dp(92.0f) - AndroidUtilities.dp(56.0f));
                    }
                    else if (currentEditMode == 3) {
                        this.animateToY = (float)(AndroidUtilities.dp(44.0f) - AndroidUtilities.dp(56.0f));
                    }
                    this.animateToScale = n9 / n7;
                    this.zoomAnimation = true;
                }
                else {
                    this.animationStartTime = 0L;
                    this.translationX = (float)(this.getLeftInset() / 2 - this.getRightInset() / 2);
                    final int n11 = -AndroidUtilities.dp(56.0f);
                    int n12;
                    if (Build$VERSION.SDK_INT >= 21) {
                        n12 = AndroidUtilities.statusBarHeight / 2;
                    }
                    else {
                        n12 = 0;
                    }
                    this.translationY = (float)(n11 + n12);
                    this.updateMinMax(this.scale = n9 / n7);
                }
            }
        }
    }
    
    private void setCurrentCaption(final MessageObject messageObject, final CharSequence charSequence, final boolean b) {
        if (this.needCaptionLayout) {
            if (this.captionTextView.getParent() != this.pickerView) {
                this.captionTextView.setBackgroundDrawable((Drawable)null);
                this.containerView.removeView((View)this.captionTextView);
                this.pickerView.addView((View)this.captionTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 0.0f, 76.0f, 48.0f));
            }
        }
        else if (this.captionTextView.getParent() != this.containerView) {
            this.captionTextView.setBackgroundColor(2130706432);
            this.pickerView.removeView((View)this.captionTextView);
            this.containerView.addView((View)this.captionTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        }
        if (this.isCurrentVideo) {
            this.captionTextView.setMaxLines(1);
            this.captionTextView.setSingleLine(true);
        }
        else {
            this.captionTextView.setSingleLine(false);
            this.captionTextView.setMaxLines(10);
        }
        final boolean b2 = this.captionTextView.getTag() != null;
        if (!TextUtils.isEmpty(charSequence)) {
            Theme.createChatResources(null, true);
            CharSequence charSequence2;
            if (messageObject != null && !messageObject.messageOwner.entities.isEmpty()) {
                final SpannableString value = SpannableString.valueOf((CharSequence)charSequence.toString());
                messageObject.addEntitiesToText((CharSequence)value, true, false);
                charSequence2 = Emoji.replaceEmoji((CharSequence)value, this.captionTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            }
            else {
                charSequence2 = Emoji.replaceEmoji((CharSequence)new SpannableStringBuilder(charSequence), this.captionTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            }
            this.captionTextView.setTag((Object)charSequence2);
            final AnimatorSet currentCaptionAnimation = this.currentCaptionAnimation;
            if (currentCaptionAnimation != null) {
                currentCaptionAnimation.cancel();
                this.currentCaptionAnimation = null;
            }
            try {
                this.captionTextView.setText(charSequence2);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            this.captionTextView.setScrollY(0);
            this.captionTextView.setTextColor(-1);
            if (this.isActionBarVisible && (this.bottomLayout.getVisibility() == 0 || this.pickerView.getVisibility() == 0)) {
                this.captionTextView.setVisibility(0);
                if (b && !b2) {
                    (this.currentCaptionAnimation = new AnimatorSet()).setDuration(200L);
                    this.currentCaptionAnimation.setInterpolator((TimeInterpolator)PhotoViewer.decelerateInterpolator);
                    this.currentCaptionAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            if (animator.equals(PhotoViewer.this.currentCaptionAnimation)) {
                                PhotoViewer.this.currentCaptionAnimation = null;
                            }
                        }
                    });
                    this.currentCaptionAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.captionTextView, View.ALPHA, new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.captionTextView, View.TRANSLATION_Y, new float[] { (float)AndroidUtilities.dp(5.0f), 0.0f }) });
                    this.currentCaptionAnimation.start();
                }
                else {
                    this.captionTextView.setAlpha(1.0f);
                }
            }
            else if (this.captionTextView.getVisibility() == 0) {
                this.captionTextView.setVisibility(4);
                this.captionTextView.setAlpha(0.0f);
            }
        }
        else if (this.needCaptionLayout) {
            this.captionTextView.setText((CharSequence)LocaleController.getString("AddCaption", 2131558566));
            this.captionTextView.setTag((Object)"empty");
            this.captionTextView.setVisibility(0);
            this.captionTextView.setTextColor(-1291845633);
        }
        else {
            this.captionTextView.setTextColor(-1);
            this.captionTextView.setTag((Object)null);
            if (b && b2) {
                (this.currentCaptionAnimation = new AnimatorSet()).setDuration(200L);
                this.currentCaptionAnimation.setInterpolator((TimeInterpolator)PhotoViewer.decelerateInterpolator);
                this.currentCaptionAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationCancel(final Animator animator) {
                        if (animator.equals(PhotoViewer.this.currentCaptionAnimation)) {
                            PhotoViewer.this.currentCaptionAnimation = null;
                        }
                    }
                    
                    public void onAnimationEnd(final Animator animator) {
                        if (animator.equals(PhotoViewer.this.currentCaptionAnimation)) {
                            PhotoViewer.this.captionTextView.setVisibility(4);
                            PhotoViewer.this.currentCaptionAnimation = null;
                        }
                    }
                });
                this.currentCaptionAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.captionTextView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.captionTextView, View.TRANSLATION_Y, new float[] { (float)AndroidUtilities.dp(5.0f) }) });
                this.currentCaptionAnimation.start();
            }
            else {
                this.captionTextView.setVisibility(4);
            }
        }
    }
    
    private void setDoubleTapEnabled(final boolean doubleTapEnabled) {
        this.doubleTapEnabled = doubleTapEnabled;
        final GestureDetector gestureDetector = this.gestureDetector;
        Object onDoubleTapListener;
        if (doubleTapEnabled) {
            onDoubleTapListener = this;
        }
        else {
            onDoubleTapListener = null;
        }
        gestureDetector.setOnDoubleTapListener((GestureDetector$OnDoubleTapListener)onDoubleTapListener);
    }
    
    private void setImageIndex(int i, final boolean b) {
        if (this.currentIndex != i) {
            if (this.placeProvider != null) {
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
                this.placeProvider.willSwitchFromPhoto(this.currentMessageObject, this.getFileLocation(this.currentFileLocation), this.currentIndex);
                final int currentIndex = this.currentIndex;
                this.setIsAboutToSwitchToIndex(this.currentIndex = i, b);
                boolean b2 = false;
                Uri fromFile = null;
                Label_0720: {
                    if (!this.imagesArr.isEmpty()) {
                        i = this.currentIndex;
                        if (i < 0 || i >= this.imagesArr.size()) {
                            this.closePhoto(false, false);
                            return;
                        }
                        final MessageObject currentMessageObject = this.imagesArr.get(this.currentIndex);
                        Label_0203: {
                            if (b) {
                                final MessageObject currentMessageObject2 = this.currentMessageObject;
                                if (currentMessageObject2 != null && currentMessageObject2.getId() == currentMessageObject.getId()) {
                                    i = 1;
                                    break Label_0203;
                                }
                            }
                            i = 0;
                        }
                        this.currentMessageObject = currentMessageObject;
                        b2 = currentMessageObject.isVideo();
                        if (this.sharedMediaType == 1) {
                            final boolean canPreviewDocument = currentMessageObject.canPreviewDocument();
                            this.canZoom = canPreviewDocument;
                            if (canPreviewDocument) {
                                this.menuItem.showSubItem(1);
                                this.setDoubleTapEnabled(true);
                            }
                            else {
                                this.menuItem.hideSubItem(1);
                                this.setDoubleTapEnabled(false);
                            }
                        }
                        fromFile = null;
                    }
                    else {
                        Label_0716: {
                            Label_0714: {
                                if (!this.secureDocuments.isEmpty()) {
                                    if (i < 0 || i >= this.secureDocuments.size()) {
                                        this.closePhoto(false, false);
                                        return;
                                    }
                                    this.currentSecureDocument = this.secureDocuments.get(i);
                                }
                                else if (!this.imagesArrLocations.isEmpty()) {
                                    if (i >= 0 && i < this.imagesArrLocations.size()) {
                                        final ImageLocation currentFileLocation = this.currentFileLocation;
                                        final ImageLocation imageLocation = this.imagesArrLocations.get(i);
                                        int n = 0;
                                        Label_0444: {
                                            if (b && currentFileLocation != null && imageLocation != null) {
                                                final TLRPC.TL_fileLocationToBeDeprecated location = currentFileLocation.location;
                                                final int local_id = location.local_id;
                                                final TLRPC.TL_fileLocationToBeDeprecated location2 = imageLocation.location;
                                                if (local_id == location2.local_id && location.volume_id == location2.volume_id) {
                                                    n = 1;
                                                    break Label_0444;
                                                }
                                            }
                                            n = 0;
                                        }
                                        this.currentFileLocation = this.imagesArrLocations.get(i);
                                        i = n;
                                        break Label_0716;
                                    }
                                    this.closePhoto(false, false);
                                    return;
                                }
                                else if (!this.imagesArrLocals.isEmpty()) {
                                    if (i >= 0 && i < this.imagesArrLocals.size()) {
                                        final MediaController.SearchImage value = this.imagesArrLocals.get(i);
                                        if (value instanceof TLRPC.BotInlineResult) {
                                            final TLRPC.BotInlineResult currentBotInlineResult = (TLRPC.BotInlineResult)value;
                                            this.currentBotInlineResult = currentBotInlineResult;
                                            final TLRPC.Document document = currentBotInlineResult.document;
                                            Label_0634: {
                                                if (document != null) {
                                                    this.currentPathObject = FileLoader.getPathToAttach(document).getAbsolutePath();
                                                    b2 = MessageObject.isVideoDocument(currentBotInlineResult.document);
                                                }
                                                else {
                                                    final TLRPC.Photo photo = currentBotInlineResult.photo;
                                                    if (photo != null) {
                                                        this.currentPathObject = FileLoader.getPathToAttach(FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize())).getAbsolutePath();
                                                    }
                                                    else {
                                                        final TLRPC.WebDocument content = currentBotInlineResult.content;
                                                        if (content instanceof TLRPC.TL_webDocument) {
                                                            this.currentPathObject = content.url;
                                                            b2 = currentBotInlineResult.type.equals("video");
                                                            break Label_0634;
                                                        }
                                                    }
                                                    b2 = false;
                                                }
                                            }
                                            fromFile = null;
                                        }
                                        else if (value instanceof MediaController.PhotoEntry) {
                                            final MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry)value;
                                            final String path = photoEntry.path;
                                            this.currentPathObject = path;
                                            b2 = photoEntry.isVideo;
                                            fromFile = Uri.fromFile(new File(path));
                                        }
                                        else {
                                            if (value instanceof MediaController.SearchImage) {
                                                this.currentPathObject = ((MediaController.SearchImage)value).getPathToAttach();
                                            }
                                            break Label_0714;
                                        }
                                        i = 0;
                                        break Label_0720;
                                    }
                                    this.closePhoto(false, false);
                                    return;
                                }
                            }
                            i = 0;
                        }
                        fromFile = null;
                        b2 = false;
                    }
                }
                final PlaceProviderObject currentPlaceObject = this.currentPlaceObject;
                if (currentPlaceObject != null) {
                    if (this.animationInProgress == 0) {
                        currentPlaceObject.imageReceiver.setVisible(true, true);
                    }
                    else {
                        this.showAfterAnimation = currentPlaceObject;
                    }
                }
                this.currentPlaceObject = this.placeProvider.getPlaceForPhoto(this.currentMessageObject, this.getFileLocation(this.currentFileLocation), this.currentIndex, false);
                final PlaceProviderObject currentPlaceObject2 = this.currentPlaceObject;
                if (currentPlaceObject2 != null) {
                    if (this.animationInProgress == 0) {
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
                    this.changeModeAnimation = null;
                    final AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
                    if (aspectRatioFrameLayout != null) {
                        aspectRatioFrameLayout.setVisibility(4);
                    }
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
                    if (this.sharedMediaType != 1) {
                        this.canZoom = (!this.imagesArrLocals.isEmpty() || (this.currentFileNames[0] != null && this.photoProgressViews[0].backgroundState != 0));
                    }
                    this.updateMinMax(this.scale);
                    this.releasePlayer(false);
                }
                if (b2 && fromFile != null) {
                    this.preparePlayer(fromFile, this.isStreaming = false, false);
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
                        final PhotoProgressView[] photoProgressViews = this.photoProgressViews;
                        final PhotoProgressView photoProgressView = photoProgressViews[0];
                        photoProgressViews[0] = photoProgressViews[2];
                        photoProgressViews[2] = photoProgressView;
                        this.setIndexToImage(this.leftImage, i - 1);
                        this.checkProgress(1, false);
                        this.checkProgress(2, false);
                    }
                    else if (currentIndex < i) {
                        final ImageReceiver leftImage = this.leftImage;
                        this.leftImage = this.centerImage;
                        this.centerImage = this.rightImage;
                        this.rightImage = leftImage;
                        final PhotoProgressView[] photoProgressViews2 = this.photoProgressViews;
                        final PhotoProgressView photoProgressView2 = photoProgressViews2[0];
                        photoProgressViews2[0] = photoProgressViews2[1];
                        photoProgressViews2[1] = photoProgressView2;
                        this.setIndexToImage(this.rightImage, i + 1);
                        this.checkProgress(1, false);
                        this.checkProgress(2, false);
                    }
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
    
    private void setIndexToImage(final ImageReceiver imageReceiver, int n) {
        imageReceiver.setOrientation(0, false);
        final boolean empty = this.secureDocuments.isEmpty();
        Object o = null;
        final Drawable drawable = null;
        if (!empty) {
            if (n >= 0 && n < this.secureDocuments.size()) {
                this.secureDocuments.get(n);
                AndroidUtilities.getPhotoSize();
                final float density = AndroidUtilities.density;
                ImageReceiver.BitmapHolder currentThumb = this.currentThumb;
                if (currentThumb == null || imageReceiver != this.centerImage) {
                    currentThumb = null;
                }
                ImageReceiver.BitmapHolder thumbForPhoto = currentThumb;
                if (currentThumb == null) {
                    thumbForPhoto = this.placeProvider.getThumbForPhoto(null, null, n);
                }
                final SecureDocument secureDocument = this.secureDocuments.get(n);
                n = secureDocument.secureFile.size;
                final ImageLocation forSecureDocument = ImageLocation.getForSecureDocument(secureDocument);
                Object o2 = drawable;
                if (thumbForPhoto != null) {
                    o2 = new BitmapDrawable(thumbForPhoto.bitmap);
                }
                imageReceiver.setImage(forSecureDocument, "d", null, null, (Drawable)o2, n, null, null, 0);
            }
        }
        else if (!this.imagesArrLocals.isEmpty()) {
            if (n >= 0 && n < this.imagesArrLocals.size()) {
                final MediaController.SearchImage value = this.imagesArrLocals.get(n);
                final int n2 = (int)(AndroidUtilities.getPhotoSize() / AndroidUtilities.density);
                ImageReceiver.BitmapHolder currentThumb2 = this.currentThumb;
                if (currentThumb2 == null || imageReceiver != this.centerImage) {
                    currentThumb2 = null;
                }
                ImageReceiver.BitmapHolder thumbForPhoto2 = currentThumb2;
                if (currentThumb2 == null) {
                    thumbForPhoto2 = this.placeProvider.getThumbForPhoto(null, null, n);
                }
                final boolean b = value instanceof MediaController.PhotoEntry;
                String format = "d";
                int isVideo;
                Object photo;
                Object o3 = null;
                int n3;
                Object o5 = null;
                Object o4 = null;
                Object o6;
                Object o7;
                if (b) {
                    final MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry)value;
                    isVideo = (photoEntry.isVideo ? 1 : 0);
                    String s;
                    String format2;
                    if (isVideo == 0) {
                        s = photoEntry.imagePath;
                        if (s == null) {
                            imageReceiver.setOrientation(photoEntry.orientation, false);
                            s = photoEntry.path;
                        }
                        format2 = String.format(Locale.US, "%d_%d", n2, n2);
                    }
                    else {
                        s = photoEntry.thumbPath;
                        if (s == null) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("vthumb://");
                            sb.append(photoEntry.imageId);
                            sb.append(":");
                            sb.append(photoEntry.path);
                            s = sb.toString();
                        }
                        format2 = null;
                    }
                    final String s2 = format2;
                    o3 = (photo = null);
                    final String s3 = s;
                    n = 0;
                    n3 = 0;
                    o4 = (o5 = photo);
                    o6 = s2;
                    o7 = s3;
                }
                else if (value instanceof TLRPC.BotInlineResult) {
                    final TLRPC.BotInlineResult botInlineResult = (TLRPC.BotInlineResult)value;
                    String format3 = null;
                    Object o8 = null;
                    Label_0823: {
                        Label_0821: {
                            Label_0807: {
                                if (!botInlineResult.type.equals("video") && !MessageObject.isVideoDocument(botInlineResult.document)) {
                                    while (true) {
                                        Label_0574: {
                                            if (!botInlineResult.type.equals("gif")) {
                                                break Label_0574;
                                            }
                                            o5 = botInlineResult.document;
                                            if (o5 == null) {
                                                break Label_0574;
                                            }
                                            n = ((TLRPC.Document)o5).size;
                                            o4 = null;
                                            format3 = "d";
                                            o8 = null;
                                            o3 = null;
                                            break Label_0823;
                                        }
                                        final TLRPC.Photo photo2 = botInlineResult.photo;
                                        if (photo2 != null) {
                                            o3 = FileLoader.getClosestPhotoSizeWithSize(photo2.sizes, AndroidUtilities.getPhotoSize());
                                            o8 = botInlineResult.photo;
                                            n = ((TLRPC.PhotoSize)o3).size;
                                            format3 = String.format(Locale.US, "%d_%d", n2, n2);
                                            o4 = (o5 = null);
                                            break Label_0823;
                                        }
                                        if (botInlineResult.content instanceof TLRPC.TL_webDocument) {
                                            if (!botInlineResult.type.equals("gif")) {
                                                format = String.format(Locale.US, "%d_%d", n2, n2);
                                            }
                                            o4 = WebFile.createWithWebDocument(botInlineResult.content);
                                            o5 = null;
                                            n = 0;
                                            format3 = format;
                                            continue;
                                        }
                                        break;
                                    }
                                }
                                else {
                                    final TLRPC.Document document = botInlineResult.document;
                                    if (document != null) {
                                        o3 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
                                        o8 = botInlineResult.document;
                                        o4 = null;
                                        o5 = (format3 = (String)o4);
                                        break Label_0821;
                                    }
                                    final TLRPC.WebDocument thumb = botInlineResult.thumb;
                                    if (thumb instanceof TLRPC.TL_webDocument) {
                                        o4 = WebFile.createWithWebDocument(thumb);
                                        break Label_0807;
                                    }
                                }
                                o4 = null;
                            }
                            o3 = null;
                            o5 = null;
                            o8 = (format3 = (String)o5);
                        }
                        n = 0;
                    }
                    isVideo = 0;
                    n3 = 1;
                    final String s4 = null;
                    photo = o8;
                    o6 = format3;
                    o7 = s4;
                }
                else if (value instanceof MediaController.SearchImage) {
                    final MediaController.SearchImage searchImage = value;
                    o3 = searchImage.photoSize;
                    Object o9 = null;
                    Label_0970: {
                        if (o3 != null) {
                            photo = searchImage.photo;
                            n = ((TLRPC.PhotoSize)o3).size;
                            o9 = (o5 = null);
                        }
                        else {
                            o9 = searchImage.imagePath;
                            if (o9 != null) {
                                n = 0;
                            }
                            else {
                                o5 = searchImage.document;
                                if (o5 != null) {
                                    n = ((TLRPC.Document)o5).size;
                                    o3 = null;
                                    photo = (o9 = o3);
                                    break Label_0970;
                                }
                                o9 = searchImage.imageUrl;
                                n = searchImage.size;
                            }
                            photo = null;
                            o3 = null;
                            o5 = null;
                        }
                    }
                    isVideo = 0;
                    n3 = 1;
                    o7 = o9;
                    o6 = "d";
                    o4 = null;
                }
                else {
                    o3 = (o4 = null);
                    o5 = (photo = o4);
                    o6 = (o7 = photo);
                    isVideo = 0;
                    n = 0;
                    n3 = 0;
                }
                if (o5 != null) {
                    final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Document)o5).thumbs, 90);
                    final ImageLocation forDocument = ImageLocation.getForDocument((TLRPC.Document)o5);
                    ImageLocation forDocument2;
                    if (thumbForPhoto2 == null) {
                        forDocument2 = ImageLocation.getForDocument(closestPhotoSizeWithSize, (TLRPC.Document)o5);
                    }
                    else {
                        forDocument2 = null;
                    }
                    final String format4 = String.format(Locale.US, "%d_%d", n2, n2);
                    Object o10;
                    if (thumbForPhoto2 != null) {
                        o10 = new BitmapDrawable(thumbForPhoto2.bitmap);
                    }
                    else {
                        o10 = null;
                    }
                    imageReceiver.setImage(forDocument, "d", forDocument2, format4, (Drawable)o10, n, null, value, n3);
                }
                else if (o3 != null) {
                    final ImageLocation forObject = ImageLocation.getForObject((TLRPC.PhotoSize)o3, (TLObject)photo);
                    Object o11;
                    if (thumbForPhoto2 != null) {
                        o11 = new BitmapDrawable(thumbForPhoto2.bitmap);
                    }
                    else {
                        o11 = null;
                    }
                    imageReceiver.setImage(forObject, (String)o6, (Drawable)o11, n, null, value, n3);
                }
                else if (o4 != null) {
                    final ImageLocation forWebFile = ImageLocation.getForWebFile((WebFile)o4);
                    Object drawable2 = null;
                    Label_1233: {
                        if (thumbForPhoto2 != null) {
                            drawable2 = new BitmapDrawable(thumbForPhoto2.bitmap);
                        }
                        else {
                            if (isVideo != 0) {
                                final Activity parentActivity = this.parentActivity;
                                if (parentActivity != null) {
                                    drawable2 = parentActivity.getResources().getDrawable(2131165697);
                                    break Label_1233;
                                }
                            }
                            drawable2 = null;
                        }
                    }
                    imageReceiver.setImage(forWebFile, (String)o6, (Drawable)drawable2, null, value, n3);
                }
                else {
                    Object drawable3 = null;
                    Label_1307: {
                        if (thumbForPhoto2 != null) {
                            drawable3 = new BitmapDrawable(thumbForPhoto2.bitmap);
                        }
                        else {
                            if (isVideo != 0) {
                                final Activity parentActivity2 = this.parentActivity;
                                if (parentActivity2 != null) {
                                    drawable3 = parentActivity2.getResources().getDrawable(2131165697);
                                    break Label_1307;
                                }
                            }
                            drawable3 = null;
                        }
                    }
                    imageReceiver.setImage((String)o7, (String)o6, (Drawable)drawable3, null, n);
                }
            }
            else {
                imageReceiver.setImageBitmap((Bitmap)null);
            }
        }
        else {
            Object o12;
            if (!this.imagesArr.isEmpty() && n >= 0 && n < this.imagesArr.size()) {
                o12 = this.imagesArr.get(n);
                imageReceiver.setShouldGenerateQualityThumb(true);
            }
            else {
                o12 = null;
            }
            if (o12 != null) {
                if (((MessageObject)o12).isVideo()) {
                    imageReceiver.setNeedsQualityThumb(true);
                    final ArrayList<TLRPC.PhotoSize> photoThumbs = ((MessageObject)o12).photoThumbs;
                    if (photoThumbs != null && !photoThumbs.isEmpty()) {
                        ImageReceiver.BitmapHolder currentThumb3 = this.currentThumb;
                        if (currentThumb3 == null || imageReceiver != this.centerImage) {
                            currentThumb3 = null;
                        }
                        final TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(((MessageObject)o12).photoThumbs, 100);
                        ImageLocation forObject2;
                        if (currentThumb3 == null) {
                            forObject2 = ImageLocation.getForObject(closestPhotoSizeWithSize2, ((MessageObject)o12).photoThumbsObject);
                        }
                        else {
                            forObject2 = null;
                        }
                        Object o13;
                        if (currentThumb3 != null) {
                            o13 = new BitmapDrawable(currentThumb3.bitmap);
                        }
                        else {
                            o13 = null;
                        }
                        imageReceiver.setImage(null, null, forObject2, "b", (Drawable)o13, 0, null, o12, 1);
                    }
                    else {
                        imageReceiver.setImageBitmap(this.parentActivity.getResources().getDrawable(2131165761));
                    }
                    return;
                }
                final AnimatedFileDrawable currentAnimation = this.currentAnimation;
                if (currentAnimation != null) {
                    imageReceiver.setImageBitmap((Drawable)currentAnimation);
                    this.currentAnimation.setSecondParentView((View)this.containerView);
                    return;
                }
                if (this.sharedMediaType == 1) {
                    if (((MessageObject)o12).canPreviewDocument()) {
                        final TLRPC.Document document2 = ((MessageObject)o12).getDocument();
                        imageReceiver.setNeedsQualityThumb(true);
                        ImageReceiver.BitmapHolder currentThumb4 = this.currentThumb;
                        if (currentThumb4 == null || imageReceiver != this.centerImage) {
                            currentThumb4 = null;
                        }
                        TLRPC.PhotoSize closestPhotoSizeWithSize3;
                        if (o12 != null) {
                            closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(((MessageObject)o12).photoThumbs, 100);
                        }
                        else {
                            closestPhotoSizeWithSize3 = null;
                        }
                        n = (int)(4096.0f / AndroidUtilities.density);
                        final ImageLocation forDocument3 = ImageLocation.getForDocument(document2);
                        final String format5 = String.format(Locale.US, "%d_%d", n, n);
                        ImageLocation forDocument4;
                        if (currentThumb4 == null) {
                            forDocument4 = ImageLocation.getForDocument(closestPhotoSizeWithSize3, document2);
                        }
                        else {
                            forDocument4 = null;
                        }
                        if (currentThumb4 != null) {
                            o = new BitmapDrawable(currentThumb4.bitmap);
                        }
                        imageReceiver.setImage(forDocument3, format5, forDocument4, "b", (Drawable)o, document2.size, null, o12, 0);
                    }
                    else {
                        imageReceiver.setImageBitmap(new OtherDocumentPlaceholderDrawable((Context)this.parentActivity, (View)this.containerView, (MessageObject)o12));
                    }
                    return;
                }
            }
            final int[] array = { 0 };
            final ImageLocation imageLocation = this.getImageLocation(n, array);
            final TLObject fileLocation = this.getFileLocation(n, array);
            if (imageLocation != null) {
                imageReceiver.setNeedsQualityThumb(true);
                ImageReceiver.BitmapHolder currentThumb5 = this.currentThumb;
                if (currentThumb5 == null || imageReceiver != this.centerImage) {
                    currentThumb5 = null;
                }
                if (array[0] == 0) {
                    array[0] = -1;
                }
                TLObject closestPhotoSizeWithSize4;
                TLObject photoThumbsObject;
                if (o12 != null) {
                    closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(((MessageObject)o12).photoThumbs, 100);
                    photoThumbsObject = ((MessageObject)o12).photoThumbsObject;
                }
                else {
                    closestPhotoSizeWithSize4 = (photoThumbsObject = null);
                }
                TLObject tlObject = closestPhotoSizeWithSize4;
                if (closestPhotoSizeWithSize4 != null && (tlObject = closestPhotoSizeWithSize4) == fileLocation) {
                    tlObject = null;
                }
                if ((o12 == null || !((MessageObject)o12).isWebpage()) && this.avatarsDialogId == 0 && !this.isEvent) {
                    n = 0;
                }
                else {
                    n = 1;
                }
                if (o12 == null) {
                    final int avatarsDialogId = this.avatarsDialogId;
                    if (avatarsDialogId != 0) {
                        if (avatarsDialogId > 0) {
                            o12 = MessagesController.getInstance(this.currentAccount).getUser(this.avatarsDialogId);
                        }
                        else {
                            o12 = MessagesController.getInstance(this.currentAccount).getChat(-this.avatarsDialogId);
                        }
                    }
                    else {
                        o12 = null;
                    }
                }
                ImageLocation forObject3;
                if (currentThumb5 == null) {
                    forObject3 = ImageLocation.getForObject((TLRPC.PhotoSize)tlObject, photoThumbsObject);
                }
                else {
                    forObject3 = null;
                }
                Object o14;
                if (currentThumb5 != null) {
                    o14 = new BitmapDrawable(currentThumb5.bitmap);
                }
                else {
                    o14 = null;
                }
                imageReceiver.setImage(imageLocation, null, forObject3, "b", (Drawable)o14, array[0], null, o12, n);
            }
            else {
                imageReceiver.setNeedsQualityThumb(true);
                if (array[0] == 0) {
                    imageReceiver.setImageBitmap((Bitmap)null);
                }
                else {
                    imageReceiver.setImageBitmap(this.parentActivity.getResources().getDrawable(2131165761));
                }
            }
        }
    }
    
    private void setIsAboutToSwitchToIndex(int visibility, final boolean b) {
        if (!b && this.switchingToIndex == visibility) {
            return;
        }
        this.switchingToIndex = visibility;
        final String fileName = this.getFileName(visibility);
        final boolean empty = this.imagesArr.isEmpty();
        final MessageObject messageObject = null;
        int n = 8;
        MessageObject messageObject2 = null;
        CharSequence charSequence = null;
        Label_3082: {
            if (!empty) {
                visibility = this.switchingToIndex;
                if (visibility < 0 || visibility >= this.imagesArr.size()) {
                    return;
                }
                messageObject2 = this.imagesArr.get(this.switchingToIndex);
                final boolean video = messageObject2.isVideo();
                final boolean invoice = messageObject2.isInvoice();
                if (invoice) {
                    this.masksItem.setVisibility(8);
                    this.menuItem.hideSubItem(6);
                    this.menuItem.hideSubItem(11);
                    charSequence = messageObject2.messageOwner.media.description;
                    this.allowShare = false;
                    this.bottomLayout.setTranslationY((float)AndroidUtilities.dp(48.0f));
                    this.captionTextView.setTranslationY((float)AndroidUtilities.dp(48.0f));
                }
                else {
                    final ActionBarMenuItem masksItem = this.masksItem;
                    if (messageObject2.hasPhotoStickers() && (int)messageObject2.getDialogId() != 0) {
                        visibility = 0;
                    }
                    else {
                        visibility = 8;
                    }
                    masksItem.setVisibility(visibility);
                    if (messageObject2.canDeleteMessage(null) && this.slideshowMessageId == 0) {
                        this.menuItem.showSubItem(6);
                    }
                    else {
                        this.menuItem.hideSubItem(6);
                    }
                    if (video) {
                        this.menuItem.showSubItem(11);
                        if (this.pipItem.getVisibility() != 0) {
                            this.pipItem.setVisibility(0);
                        }
                        if (!this.pipAvailable) {
                            this.pipItem.setEnabled(false);
                            this.pipItem.setAlpha(0.5f);
                        }
                    }
                    else {
                        this.menuItem.hideSubItem(11);
                        if (this.pipItem.getVisibility() != 8) {
                            this.pipItem.setVisibility(8);
                        }
                    }
                    final String nameOverride = this.nameOverride;
                    if (nameOverride != null) {
                        this.nameTextView.setText((CharSequence)nameOverride);
                    }
                    else if (messageObject2.isFromUser()) {
                        final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(messageObject2.messageOwner.from_id);
                        if (user != null) {
                            this.nameTextView.setText((CharSequence)UserObject.getUserName(user));
                        }
                        else {
                            this.nameTextView.setText((CharSequence)"");
                        }
                    }
                    else {
                        TLRPC.Chat chat2;
                        final TLRPC.Chat chat = chat2 = MessagesController.getInstance(this.currentAccount).getChat(messageObject2.messageOwner.to_id.channel_id);
                        if (ChatObject.isChannel(chat)) {
                            chat2 = chat;
                            if (chat.megagroup) {
                                chat2 = chat;
                                if (messageObject2.isForwardedChannelPost()) {
                                    chat2 = MessagesController.getInstance(this.currentAccount).getChat(messageObject2.messageOwner.fwd_from.channel_id);
                                }
                            }
                        }
                        if (chat2 != null) {
                            this.nameTextView.setText((CharSequence)chat2.title);
                        }
                        else {
                            this.nameTextView.setText((CharSequence)"");
                        }
                    }
                    visibility = this.dateOverride;
                    if (visibility == 0) {
                        visibility = messageObject2.messageOwner.date;
                    }
                    final long n2 = visibility * 1000L;
                    final String formatString = LocaleController.formatString("formatDateAtTime", 2131561210, LocaleController.getInstance().formatterYear.format(new Date(n2)), LocaleController.getInstance().formatterDay.format(new Date(n2)));
                    if (fileName != null && video) {
                        this.dateTextView.setText((CharSequence)String.format("%s (%s)", formatString, AndroidUtilities.formatFileSize(messageObject2.getDocument().size)));
                    }
                    else {
                        this.dateTextView.setText((CharSequence)formatString);
                    }
                    charSequence = messageObject2.caption;
                }
                if (this.currentAnimation != null) {
                    this.menuItem.hideSubItem(1);
                    this.menuItem.hideSubItem(10);
                    if (!messageObject2.canDeleteMessage(null)) {
                        this.menuItem.setVisibility(8);
                    }
                    this.allowShare = true;
                    this.shareButton.setVisibility(0);
                    this.actionBar.setTitle(LocaleController.getString("AttachGif", 2131558716));
                }
                else {
                    if (this.totalImagesCount + this.totalImagesCountMerge != 0 && !this.needSearchImageInArr) {
                        if (this.opennedFromMedia) {
                            if (this.imagesArr.size() < this.totalImagesCount + this.totalImagesCountMerge && !this.loadingMoreImages && this.switchingToIndex > this.imagesArr.size() - 5) {
                                if (this.imagesArr.isEmpty()) {
                                    visibility = 0;
                                }
                                else {
                                    final ArrayList<MessageObject> imagesArr = this.imagesArr;
                                    visibility = imagesArr.get(imagesArr.size() - 1).getId();
                                }
                                int n3 = 0;
                                Label_0966: {
                                    if (this.endReached[0] && this.mergeDialogId != 0L) {
                                        if (!this.imagesArr.isEmpty()) {
                                            final ArrayList<MessageObject> imagesArr2 = this.imagesArr;
                                            if (imagesArr2.get(imagesArr2.size() - 1).getDialogId() != this.mergeDialogId) {
                                                visibility = 1;
                                                n3 = 0;
                                                break Label_0966;
                                            }
                                        }
                                        n3 = visibility;
                                        visibility = 1;
                                    }
                                    else {
                                        final int n4 = 0;
                                        n3 = visibility;
                                        visibility = n4;
                                    }
                                }
                                final DataQuery instance = DataQuery.getInstance(this.currentAccount);
                                long n5;
                                if (visibility == 0) {
                                    n5 = this.currentDialogId;
                                }
                                else {
                                    n5 = this.mergeDialogId;
                                }
                                instance.loadMedia(n5, 80, n3, this.sharedMediaType, 1, this.classGuid);
                                this.loadingMoreImages = true;
                            }
                            this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.switchingToIndex + 1, this.totalImagesCount + this.totalImagesCountMerge));
                        }
                        else {
                            if (this.imagesArr.size() < this.totalImagesCount + this.totalImagesCountMerge && !this.loadingMoreImages && this.switchingToIndex < 5) {
                                if (this.imagesArr.isEmpty()) {
                                    visibility = 0;
                                }
                                else {
                                    visibility = this.imagesArr.get(0).getId();
                                }
                                int n6;
                                if (this.endReached[0] && this.mergeDialogId != 0L) {
                                    if (!this.imagesArr.isEmpty() && this.imagesArr.get(0).getDialogId() != this.mergeDialogId) {
                                        visibility = 1;
                                        n6 = 0;
                                    }
                                    else {
                                        n6 = visibility;
                                        visibility = 1;
                                    }
                                }
                                else {
                                    final int n7 = 0;
                                    n6 = visibility;
                                    visibility = n7;
                                }
                                final DataQuery instance2 = DataQuery.getInstance(this.currentAccount);
                                long n8;
                                if (visibility == 0) {
                                    n8 = this.currentDialogId;
                                }
                                else {
                                    n8 = this.mergeDialogId;
                                }
                                instance2.loadMedia(n8, 80, n6, this.sharedMediaType, 1, this.classGuid);
                                this.loadingMoreImages = true;
                            }
                            this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.totalImagesCount + this.totalImagesCountMerge - this.imagesArr.size() + this.switchingToIndex + 1, this.totalImagesCount + this.totalImagesCountMerge));
                        }
                    }
                    else if (this.slideshowMessageId == 0 && messageObject2.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) {
                        if (messageObject2.canPreviewDocument()) {
                            this.actionBar.setTitle(LocaleController.getString("AttachDocument", 2131558714));
                        }
                        else if (messageObject2.isVideo()) {
                            this.actionBar.setTitle(LocaleController.getString("AttachVideo", 2131558733));
                        }
                        else {
                            this.actionBar.setTitle(LocaleController.getString("AttachPhoto", 2131558727));
                        }
                    }
                    else if (invoice) {
                        this.actionBar.setTitle(messageObject2.messageOwner.media.title);
                    }
                    else if (messageObject2.isVideo()) {
                        this.actionBar.setTitle(LocaleController.getString("AttachVideo", 2131558733));
                    }
                    else if (messageObject2.getDocument() != null) {
                        this.actionBar.setTitle(LocaleController.getString("AttachDocument", 2131558714));
                    }
                    if ((int)this.currentDialogId == 0) {
                        this.sendItem.setVisibility(8);
                    }
                    visibility = messageObject2.messageOwner.ttl;
                    if (visibility != 0 && visibility < 3600) {
                        this.allowShare = false;
                        this.menuItem.hideSubItem(1);
                        this.shareButton.setVisibility(8);
                        this.menuItem.hideSubItem(10);
                    }
                    else {
                        this.allowShare = true;
                        this.menuItem.showSubItem(1);
                        final ImageView shareButton = this.shareButton;
                        if (this.videoPlayerControlFrameLayout.getVisibility() != 0) {
                            n = 0;
                        }
                        shareButton.setVisibility(n);
                        if (this.shareButton.getVisibility() == 0) {
                            this.menuItem.hideSubItem(10);
                        }
                        else {
                            this.menuItem.showSubItem(10);
                        }
                    }
                }
                this.groupedPhotosListView.fillList();
            }
            else {
                if (!this.secureDocuments.isEmpty()) {
                    this.allowShare = false;
                    this.menuItem.hideSubItem(1);
                    this.nameTextView.setText((CharSequence)"");
                    this.dateTextView.setText((CharSequence)"");
                    this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.switchingToIndex + 1, this.secureDocuments.size()));
                }
                else if (!this.imagesArrLocations.isEmpty()) {
                    if (visibility < 0 || visibility >= this.imagesArrLocations.size()) {
                        return;
                    }
                    this.nameTextView.setText((CharSequence)"");
                    this.dateTextView.setText((CharSequence)"");
                    if (this.avatarsDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId() && !this.avatarsArr.isEmpty()) {
                        this.menuItem.showSubItem(6);
                    }
                    else {
                        this.menuItem.hideSubItem(6);
                    }
                    if (this.isEvent) {
                        this.actionBar.setTitle(LocaleController.getString("AttachPhoto", 2131558727));
                    }
                    else {
                        this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.switchingToIndex + 1, this.imagesArrLocations.size()));
                    }
                    this.menuItem.showSubItem(1);
                    this.allowShare = true;
                    final ImageView shareButton2 = this.shareButton;
                    if (this.videoPlayerControlFrameLayout.getVisibility() != 0) {
                        n = 0;
                    }
                    shareButton2.setVisibility(n);
                    if (this.shareButton.getVisibility() == 0) {
                        this.menuItem.hideSubItem(10);
                    }
                    else {
                        this.menuItem.showSubItem(10);
                    }
                    this.groupedPhotosListView.fillList();
                }
                else if (!this.imagesArrLocals.isEmpty()) {
                    if (visibility >= 0 && visibility < this.imagesArrLocals.size()) {
                        final MediaController.SearchImage value = this.imagesArrLocals.get(visibility);
                        int isVideo = 0;
                        int n10 = 0;
                        boolean b4 = false;
                        boolean b5 = false;
                        Label_2688: {
                            int n9;
                            if (value instanceof TLRPC.BotInlineResult) {
                                final TLRPC.BotInlineResult currentBotInlineResult = (TLRPC.BotInlineResult)value;
                                this.currentBotInlineResult = currentBotInlineResult;
                                final TLRPC.Document document = currentBotInlineResult.document;
                                boolean videoDocument;
                                if (document != null) {
                                    videoDocument = MessageObject.isVideoDocument(document);
                                }
                                else {
                                    videoDocument = (currentBotInlineResult.content instanceof TLRPC.TL_webDocument && currentBotInlineResult.type.equals("video"));
                                }
                                n9 = (videoDocument ? 1 : 0);
                            }
                            else {
                                final boolean b2 = value instanceof MediaController.PhotoEntry;
                                String s = null;
                                Label_2222: {
                                    if (b2) {
                                        final MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry)value;
                                        s = photoEntry.path;
                                        isVideo = (photoEntry.isVideo ? 1 : 0);
                                        visibility = 0;
                                    }
                                    else {
                                        if (value instanceof MediaController.SearchImage) {
                                            final MediaController.SearchImage searchImage = value;
                                            s = searchImage.getPathToAttach();
                                            if (searchImage.type == 1) {
                                                isVideo = 0;
                                                visibility = 1;
                                                break Label_2222;
                                            }
                                        }
                                        else {
                                            s = null;
                                        }
                                        isVideo = 0;
                                        visibility = 0;
                                    }
                                }
                                if (isVideo != 0) {
                                    this.muteItem.setVisibility(0);
                                    this.compressItem.setVisibility(0);
                                    this.isCurrentVideo = true;
                                    this.updateAccessibilityOverlayVisibility();
                                    boolean b3 = false;
                                    Label_2289: {
                                        if (b2) {
                                            final VideoEditedInfo editedInfo = ((MediaController.PhotoEntry)value).editedInfo;
                                            if (editedInfo != null && editedInfo.muted) {
                                                b3 = true;
                                                break Label_2289;
                                            }
                                        }
                                        b3 = false;
                                    }
                                    this.processOpenVideo(s, b3);
                                    this.videoTimelineView.setVisibility(0);
                                    this.paintItem.setVisibility(8);
                                    this.cropItem.setVisibility(8);
                                    this.tuneItem.setVisibility(8);
                                    this.rotateItem.setVisibility(8);
                                }
                                else {
                                    this.videoTimelineView.setVisibility(8);
                                    this.muteItem.setVisibility(8);
                                    this.isCurrentVideo = false;
                                    this.updateAccessibilityOverlayVisibility();
                                    this.compressItem.setVisibility(8);
                                    if (visibility != 0) {
                                        this.paintItem.setVisibility(8);
                                        this.cropItem.setVisibility(8);
                                        this.rotateItem.setVisibility(8);
                                        this.tuneItem.setVisibility(8);
                                    }
                                    else {
                                        visibility = this.sendPhotoType;
                                        if (visibility != 4 && visibility != 5) {
                                            this.paintItem.setVisibility(0);
                                            this.tuneItem.setVisibility(0);
                                        }
                                        else {
                                            this.paintItem.setVisibility(8);
                                            this.tuneItem.setVisibility(8);
                                        }
                                        final ImageView cropItem = this.cropItem;
                                        if (this.sendPhotoType != 1) {
                                            visibility = 0;
                                        }
                                        else {
                                            visibility = 8;
                                        }
                                        cropItem.setVisibility(visibility);
                                        final ImageView rotateItem = this.rotateItem;
                                        if (this.sendPhotoType != 1) {
                                            visibility = 8;
                                        }
                                        else {
                                            visibility = 0;
                                        }
                                        rotateItem.setVisibility(visibility);
                                    }
                                    this.actionBar.setSubtitle(null);
                                }
                                if (b2) {
                                    final MediaController.PhotoEntry photoEntry2 = (MediaController.PhotoEntry)value;
                                    this.fromCamera = (photoEntry2.bucketId == 0 && photoEntry2.dateTaken == 0L && this.imagesArrLocals.size() == 1);
                                    charSequence = photoEntry2.caption;
                                    visibility = photoEntry2.ttl;
                                    n10 = (photoEntry2.isFiltered ? 1 : 0);
                                    b4 = photoEntry2.isPainted;
                                    b5 = photoEntry2.isCropped;
                                    break Label_2688;
                                }
                                n9 = isVideo;
                                if (value instanceof MediaController.SearchImage) {
                                    final MediaController.SearchImage searchImage2 = value;
                                    charSequence = searchImage2.caption;
                                    visibility = searchImage2.ttl;
                                    n10 = (searchImage2.isFiltered ? 1 : 0);
                                    b4 = searchImage2.isPainted;
                                    b5 = searchImage2.isCropped;
                                    break Label_2688;
                                }
                            }
                            charSequence = null;
                            b5 = false;
                            visibility = 0;
                            final int n11 = 0;
                            b4 = false;
                            isVideo = n9;
                            n10 = n11;
                        }
                        if (this.bottomLayout.getVisibility() != 8) {
                            this.bottomLayout.setVisibility(8);
                        }
                        this.bottomLayout.setTag((Object)null);
                        if (this.fromCamera) {
                            if (isVideo != 0) {
                                this.actionBar.setTitle(LocaleController.getString("AttachVideo", 2131558733));
                            }
                            else {
                                this.actionBar.setTitle(LocaleController.getString("AttachPhoto", 2131558727));
                            }
                        }
                        else {
                            this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.switchingToIndex + 1, this.imagesArrLocals.size()));
                        }
                        final ChatActivity parentChatActivity = this.parentChatActivity;
                        if (parentChatActivity != null) {
                            final TLRPC.Chat currentChat = parentChatActivity.getCurrentChat();
                            if (currentChat != null) {
                                this.actionBar.setTitle(currentChat.title);
                            }
                            else {
                                final TLRPC.User currentUser = this.parentChatActivity.getCurrentUser();
                                if (currentUser != null) {
                                    this.actionBar.setTitle(ContactsController.formatName(currentUser.first_name, currentUser.last_name));
                                }
                            }
                        }
                        final int sendPhotoType = this.sendPhotoType;
                        if (sendPhotoType == 0 || sendPhotoType == 4 || ((sendPhotoType == 2 || sendPhotoType == 5) && this.imagesArrLocals.size() > 1)) {
                            this.checkImageView.setChecked(this.placeProvider.isPhotoChecked(this.switchingToIndex), false);
                        }
                        this.updateCaptionTextForCurrentPhoto(value);
                        Object colorFilter = new PorterDuffColorFilter(-12734994, PorterDuff$Mode.MULTIPLY);
                        final ImageView timeItem = this.timeItem;
                        ColorFilter colorFilter2;
                        if (visibility != 0) {
                            colorFilter2 = (ColorFilter)colorFilter;
                        }
                        else {
                            colorFilter2 = null;
                        }
                        timeItem.setColorFilter(colorFilter2);
                        final ImageView paintItem = this.paintItem;
                        ColorFilter colorFilter3;
                        if (b4) {
                            colorFilter3 = (ColorFilter)colorFilter;
                        }
                        else {
                            colorFilter3 = null;
                        }
                        paintItem.setColorFilter(colorFilter3);
                        final ImageView cropItem2 = this.cropItem;
                        ColorFilter colorFilter4;
                        if (b5) {
                            colorFilter4 = (ColorFilter)colorFilter;
                        }
                        else {
                            colorFilter4 = null;
                        }
                        cropItem2.setColorFilter(colorFilter4);
                        final ImageView tuneItem = this.tuneItem;
                        if (n10 == 0) {
                            colorFilter = null;
                        }
                        tuneItem.setColorFilter((ColorFilter)colorFilter);
                        messageObject2 = messageObject;
                        break Label_3082;
                    }
                    return;
                }
                charSequence = null;
                messageObject2 = messageObject;
            }
        }
        this.setCurrentCaption(messageObject2, charSequence, b ^ true);
    }
    
    private void setPhotoChecked() {
        final PhotoViewerProvider placeProvider = this.placeProvider;
        if (placeProvider != null) {
            if (placeProvider.getSelectedPhotos() != null && this.maxSelectedPhotos > 0 && this.placeProvider.getSelectedPhotos().size() > this.maxSelectedPhotos && !this.placeProvider.isPhotoChecked(this.currentIndex)) {
                return;
            }
            final int setPhotoChecked = this.placeProvider.setPhotoChecked(this.currentIndex, this.getCurrentVideoEditedInfo());
            final boolean photoChecked = this.placeProvider.isPhotoChecked(this.currentIndex);
            this.checkImageView.setChecked(photoChecked, true);
            if (setPhotoChecked >= 0) {
                int n = setPhotoChecked;
                if (this.placeProvider.allowGroupPhotos()) {
                    n = setPhotoChecked + 1;
                }
                if (photoChecked) {
                    this.selectedPhotosAdapter.notifyItemInserted(n);
                    this.selectedPhotosListView.smoothScrollToPosition(n);
                }
                else {
                    this.selectedPhotosAdapter.notifyItemRemoved(n);
                }
            }
            this.updateSelectedCount();
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
    
    private void showDownloadAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.parentActivity);
        builder.setTitle(LocaleController.getString("AppName", 2131558635));
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
        final MessageObject currentMessageObject = this.currentMessageObject;
        int n2;
        final int n = n2 = 0;
        if (currentMessageObject != null) {
            n2 = n;
            if (currentMessageObject.isVideo()) {
                n2 = n;
                if (FileLoader.getInstance(this.currentMessageObject.currentAccount).isLoadingFile(this.currentFileNames[0])) {
                    n2 = 1;
                }
            }
        }
        if (n2 != 0) {
            builder.setMessage(LocaleController.getString("PleaseStreamDownload", 2131560460));
        }
        else {
            builder.setMessage(LocaleController.getString("PleaseDownload", 2131560454));
        }
        this.showAlertDialog(builder);
    }
    
    private void showHint(final boolean b, final boolean b2) {
        if (this.containerView != null) {
            if (!b || this.hintTextView != null) {
                if (this.hintTextView == null) {
                    (this.hintTextView = new TextView(this.containerView.getContext())).setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0f), -871296751));
                    this.hintTextView.setTextColor(-1);
                    this.hintTextView.setTextSize(1, 14.0f);
                    this.hintTextView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f));
                    this.hintTextView.setGravity(16);
                    this.hintTextView.setAlpha(0.0f);
                    this.containerView.addView((View)this.hintTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 5.0f, 0.0f, 5.0f, 3.0f));
                }
                if (b) {
                    final AnimatorSet hintAnimation = this.hintAnimation;
                    if (hintAnimation != null) {
                        hintAnimation.cancel();
                        this.hintAnimation = null;
                    }
                    AndroidUtilities.cancelRunOnUIThread(this.hintHideRunnable);
                    this.hintHideRunnable = null;
                    this.hideHint();
                    return;
                }
                final TextView hintTextView = this.hintTextView;
                int n;
                String s;
                if (b2) {
                    n = 2131559612;
                    s = "GroupPhotosHelp";
                }
                else {
                    n = 2131560786;
                    s = "SinglePhotosHelp";
                }
                hintTextView.setText((CharSequence)LocaleController.getString(s, n));
                final Runnable hintHideRunnable = this.hintHideRunnable;
                if (hintHideRunnable != null) {
                    final AnimatorSet hintAnimation2 = this.hintAnimation;
                    if (hintAnimation2 == null) {
                        AndroidUtilities.cancelRunOnUIThread(hintHideRunnable);
                        AndroidUtilities.runOnUIThread(this.hintHideRunnable = new _$$Lambda$PhotoViewer$YkU8h9C09cYhYx6kik_kbQ5mA8s(this), 2000L);
                        return;
                    }
                    hintAnimation2.cancel();
                    this.hintAnimation = null;
                }
                else if (this.hintAnimation != null) {
                    return;
                }
                this.hintTextView.setVisibility(0);
                (this.hintAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.hintTextView, View.ALPHA, new float[] { 1.0f }) });
                this.hintAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationCancel(final Animator animator) {
                        if (animator.equals(PhotoViewer.this.hintAnimation)) {
                            PhotoViewer.this.hintAnimation = null;
                        }
                    }
                    
                    public void onAnimationEnd(final Animator animator) {
                        if (animator.equals(PhotoViewer.this.hintAnimation)) {
                            PhotoViewer.this.hintAnimation = null;
                            final PhotoViewer this$0 = PhotoViewer.this;
                            final _$$Lambda$PhotoViewer$41$zVPp9kWNm7HNZtSbgwtWUuDr3b8 $$Lambda$PhotoViewer$41$zVPp9kWNm7HNZtSbgwtWUuDr3b8 = new _$$Lambda$PhotoViewer$41$zVPp9kWNm7HNZtSbgwtWUuDr3b8(this);
                            this$0.hintHideRunnable = $$Lambda$PhotoViewer$41$zVPp9kWNm7HNZtSbgwtWUuDr3b8;
                            AndroidUtilities.runOnUIThread($$Lambda$PhotoViewer$41$zVPp9kWNm7HNZtSbgwtWUuDr3b8, 2000L);
                        }
                    }
                });
                this.hintAnimation.setDuration(300L);
                this.hintAnimation.start();
            }
        }
    }
    
    private void showQualityView(final boolean b) {
        if (b) {
            this.previousCompression = this.selectedCompression;
        }
        final AnimatorSet qualityChooseViewAnimation = this.qualityChooseViewAnimation;
        if (qualityChooseViewAnimation != null) {
            qualityChooseViewAnimation.cancel();
        }
        this.qualityChooseViewAnimation = new AnimatorSet();
        if (b) {
            this.qualityChooseView.setTag((Object)1);
            this.qualityChooseViewAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.pickerView, View.TRANSLATION_Y, new float[] { 0.0f, (float)AndroidUtilities.dp(152.0f) }), (Animator)ObjectAnimator.ofFloat((Object)this.pickerViewSendButton, View.TRANSLATION_Y, new float[] { 0.0f, (float)AndroidUtilities.dp(152.0f) }), (Animator)ObjectAnimator.ofFloat((Object)this.bottomLayout, View.TRANSLATION_Y, new float[] { (float)(-AndroidUtilities.dp(48.0f)), (float)AndroidUtilities.dp(104.0f) }) });
        }
        else {
            this.qualityChooseView.setTag((Object)null);
            this.qualityChooseViewAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.qualityChooseView, View.TRANSLATION_Y, new float[] { 0.0f, (float)AndroidUtilities.dp(166.0f) }), (Animator)ObjectAnimator.ofFloat((Object)this.qualityPicker, View.TRANSLATION_Y, new float[] { 0.0f, (float)AndroidUtilities.dp(166.0f) }), (Animator)ObjectAnimator.ofFloat((Object)this.bottomLayout, View.TRANSLATION_Y, new float[] { (float)(-AndroidUtilities.dp(48.0f)), (float)AndroidUtilities.dp(118.0f) }) });
        }
        this.qualityChooseViewAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                PhotoViewer.this.qualityChooseViewAnimation = null;
            }
            
            public void onAnimationEnd(final Animator animator) {
                if (!animator.equals(PhotoViewer.this.qualityChooseViewAnimation)) {
                    return;
                }
                PhotoViewer.this.qualityChooseViewAnimation = new AnimatorSet();
                if (b) {
                    PhotoViewer.this.qualityChooseView.setVisibility(0);
                    PhotoViewer.this.qualityPicker.setVisibility(0);
                    PhotoViewer.this.qualityChooseViewAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.qualityChooseView, View.TRANSLATION_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.qualityPicker, View.TRANSLATION_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.bottomLayout, View.TRANSLATION_Y, new float[] { (float)(-AndroidUtilities.dp(48.0f)) }) });
                }
                else {
                    PhotoViewer.this.qualityChooseView.setVisibility(4);
                    PhotoViewer.this.qualityPicker.setVisibility(4);
                    PhotoViewer.this.qualityChooseViewAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.pickerView, View.TRANSLATION_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.pickerViewSendButton, View.TRANSLATION_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.bottomLayout, View.TRANSLATION_Y, new float[] { (float)(-AndroidUtilities.dp(48.0f)) }) });
                }
                PhotoViewer.this.qualityChooseViewAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        if (animator.equals(PhotoViewer.this.qualityChooseViewAnimation)) {
                            PhotoViewer.this.qualityChooseViewAnimation = null;
                        }
                    }
                });
                PhotoViewer.this.qualityChooseViewAnimation.setDuration(200L);
                PhotoViewer.this.qualityChooseViewAnimation.setInterpolator((TimeInterpolator)new AccelerateInterpolator());
                PhotoViewer.this.qualityChooseViewAnimation.start();
            }
        });
        this.qualityChooseViewAnimation.setDuration(200L);
        this.qualityChooseViewAnimation.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
        this.qualityChooseViewAnimation.start();
    }
    
    private void switchToEditMode(final int n) {
        if (this.currentEditMode != n && this.centerImage.getBitmap() != null && this.changeModeAnimation == null && this.imageMoveAnimation == null && this.photoProgressViews[0].backgroundState == -1) {
            if (this.captionEditText.getTag() == null) {
                if (n == 0) {
                    if (this.centerImage.getBitmap() != null) {
                        final int bitmapWidth = this.centerImage.getBitmapWidth();
                        final int bitmapHeight = this.centerImage.getBitmapHeight();
                        final float n2 = (float)this.getContainerViewWidth();
                        final float n3 = (float)bitmapWidth;
                        final float n4 = n2 / n3;
                        final float n5 = (float)this.getContainerViewHeight();
                        final float n6 = (float)bitmapHeight;
                        final float n7 = n5 / n6;
                        final float n8 = this.getContainerViewWidth(0) / n3;
                        final float n9 = this.getContainerViewHeight(0) / n6;
                        float n10 = n4;
                        if (n4 > n7) {
                            n10 = n7;
                        }
                        float n11;
                        if (n8 > n9) {
                            n11 = n9;
                        }
                        else {
                            n11 = n8;
                        }
                        if (this.sendPhotoType == 1) {
                            this.setCropTranslations(true);
                        }
                        else {
                            this.animateToScale = n11 / n10;
                            this.animateToX = 0.0f;
                            this.translationX = (float)(this.getLeftInset() / 2 - this.getRightInset() / 2);
                            final int currentEditMode = this.currentEditMode;
                            if (currentEditMode == 1) {
                                this.animateToY = (float)AndroidUtilities.dp(58.0f);
                            }
                            else if (currentEditMode == 2) {
                                this.animateToY = (float)AndroidUtilities.dp(92.0f);
                            }
                            else if (currentEditMode == 3) {
                                this.animateToY = (float)AndroidUtilities.dp(44.0f);
                            }
                            if (Build$VERSION.SDK_INT >= 21) {
                                this.animateToY -= AndroidUtilities.statusBarHeight / 2;
                            }
                            this.animationStartTime = System.currentTimeMillis();
                            this.zoomAnimation = true;
                        }
                    }
                    this.padImageForHorizontalInsets = false;
                    this.imageMoveAnimation = new AnimatorSet();
                    final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>(4);
                    final int currentEditMode2 = this.currentEditMode;
                    if (currentEditMode2 == 1) {
                        list.add(ObjectAnimator.ofFloat((Object)this.editorDoneLayout, View.TRANSLATION_Y, new float[] { (float)AndroidUtilities.dp(48.0f) }));
                        list.add(ObjectAnimator.ofFloat((Object)this, (Property)AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[] { 0.0f, 1.0f }));
                        list.add(ObjectAnimator.ofFloat((Object)this.photoCropView, View.ALPHA, new float[] { 0.0f }));
                    }
                    else if (currentEditMode2 == 2) {
                        this.photoFilterView.shutdown();
                        list.add(ObjectAnimator.ofFloat((Object)this.photoFilterView.getToolsView(), View.TRANSLATION_Y, new float[] { (float)AndroidUtilities.dp(186.0f) }));
                        list.add(ObjectAnimator.ofFloat((Object)this, (Property)AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[] { 0.0f, 1.0f }));
                    }
                    else if (currentEditMode2 == 3) {
                        this.photoPaintView.shutdown();
                        list.add(ObjectAnimator.ofFloat((Object)this.photoPaintView.getToolsView(), View.TRANSLATION_Y, new float[] { (float)AndroidUtilities.dp(126.0f) }));
                        list.add(ObjectAnimator.ofFloat((Object)this.photoPaintView.getColorPicker(), View.TRANSLATION_Y, new float[] { (float)AndroidUtilities.dp(126.0f) }));
                        list.add(ObjectAnimator.ofFloat((Object)this, (Property)AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[] { 0.0f, 1.0f }));
                    }
                    this.imageMoveAnimation.playTogether((Collection)list);
                    this.imageMoveAnimation.setDuration(200L);
                    this.imageMoveAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            if (PhotoViewer.this.currentEditMode == 1) {
                                PhotoViewer.this.editorDoneLayout.setVisibility(8);
                                PhotoViewer.this.photoCropView.setVisibility(8);
                            }
                            else if (PhotoViewer.this.currentEditMode == 2) {
                                PhotoViewer.this.containerView.removeView((View)PhotoViewer.this.photoFilterView);
                                PhotoViewer.this.photoFilterView = null;
                            }
                            else if (PhotoViewer.this.currentEditMode == 3) {
                                PhotoViewer.this.containerView.removeView((View)PhotoViewer.this.photoPaintView);
                                PhotoViewer.this.photoPaintView = null;
                            }
                            PhotoViewer.this.imageMoveAnimation = null;
                            PhotoViewer.this.currentEditMode = n;
                            PhotoViewer.this.applying = false;
                            if (PhotoViewer.this.sendPhotoType != 1) {
                                PhotoViewer.this.animateToScale = 1.0f;
                                PhotoViewer.this.animateToX = 0.0f;
                                PhotoViewer.this.animateToY = 0.0f;
                                PhotoViewer.this.scale = 1.0f;
                            }
                            final PhotoViewer this$0 = PhotoViewer.this;
                            this$0.updateMinMax(this$0.scale);
                            PhotoViewer.this.containerView.invalidate();
                            final AnimatorSet set = new AnimatorSet();
                            final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
                            list.add(ObjectAnimator.ofFloat((Object)PhotoViewer.this.pickerView, View.TRANSLATION_Y, new float[] { 0.0f }));
                            list.add(ObjectAnimator.ofFloat((Object)PhotoViewer.this.pickerViewSendButton, View.TRANSLATION_Y, new float[] { 0.0f }));
                            if (PhotoViewer.this.sendPhotoType != 1) {
                                list.add(ObjectAnimator.ofFloat((Object)PhotoViewer.this.actionBar, View.TRANSLATION_Y, new float[] { 0.0f }));
                            }
                            if (PhotoViewer.this.needCaptionLayout) {
                                list.add(ObjectAnimator.ofFloat((Object)PhotoViewer.this.captionTextView, View.TRANSLATION_Y, new float[] { 0.0f }));
                            }
                            if (PhotoViewer.this.sendPhotoType != 0 && PhotoViewer.this.sendPhotoType != 4) {
                                if (PhotoViewer.this.sendPhotoType == 1) {
                                    list.add(ObjectAnimator.ofFloat((Object)PhotoViewer.this.photoCropView, View.ALPHA, new float[] { 1.0f }));
                                }
                            }
                            else {
                                list.add(ObjectAnimator.ofFloat((Object)PhotoViewer.this.checkImageView, View.ALPHA, new float[] { 1.0f }));
                                list.add(ObjectAnimator.ofFloat((Object)PhotoViewer.this.photosCounterView, View.ALPHA, new float[] { 1.0f }));
                            }
                            if (PhotoViewer.this.cameraItem.getTag() != null) {
                                PhotoViewer.this.cameraItem.setVisibility(0);
                                list.add(ObjectAnimator.ofFloat((Object)PhotoViewer.this.cameraItem, View.ALPHA, new float[] { 1.0f }));
                            }
                            set.playTogether((Collection)list);
                            set.setDuration(200L);
                            set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                                public void onAnimationStart(final Animator animator) {
                                    PhotoViewer.this.pickerView.setVisibility(0);
                                    PhotoViewer.this.pickerViewSendButton.setVisibility(0);
                                    PhotoViewer.this.actionBar.setVisibility(0);
                                    if (PhotoViewer.this.needCaptionLayout) {
                                        final TextView access$4100 = PhotoViewer.this.captionTextView;
                                        int visibility;
                                        if (PhotoViewer.this.captionTextView.getTag() != null) {
                                            visibility = 0;
                                        }
                                        else {
                                            visibility = 4;
                                        }
                                        access$4100.setVisibility(visibility);
                                    }
                                    if (PhotoViewer.this.sendPhotoType != 0 && PhotoViewer.this.sendPhotoType != 4 && ((PhotoViewer.this.sendPhotoType != 2 && PhotoViewer.this.sendPhotoType != 5) || PhotoViewer.this.imagesArrLocals.size() <= 1)) {
                                        if (PhotoViewer.this.sendPhotoType == 1) {
                                            PhotoViewer.this.setCropTranslations(false);
                                        }
                                    }
                                    else {
                                        PhotoViewer.this.checkImageView.setVisibility(0);
                                        PhotoViewer.this.photosCounterView.setVisibility(0);
                                    }
                                }
                            });
                            set.start();
                        }
                    });
                    this.imageMoveAnimation.start();
                }
                else if (n == 1) {
                    this.createCropView();
                    this.photoCropView.onAppear();
                    this.editorDoneLayout.doneButton.setText((CharSequence)LocaleController.getString("Crop", 2131559175));
                    this.editorDoneLayout.doneButton.setTextColor(-11420173);
                    this.changeModeAnimation = new AnimatorSet();
                    final ArrayList<ObjectAnimator> list2 = new ArrayList<ObjectAnimator>();
                    list2.add(ObjectAnimator.ofFloat((Object)this.pickerView, View.TRANSLATION_Y, new float[] { 0.0f, (float)AndroidUtilities.dp(96.0f) }));
                    list2.add(ObjectAnimator.ofFloat((Object)this.pickerViewSendButton, View.TRANSLATION_Y, new float[] { 0.0f, (float)AndroidUtilities.dp(96.0f) }));
                    final ActionBar actionBar = this.actionBar;
                    list2.add(ObjectAnimator.ofFloat((Object)actionBar, View.TRANSLATION_Y, new float[] { 0.0f, (float)(-actionBar.getHeight()) }));
                    if (this.needCaptionLayout) {
                        list2.add(ObjectAnimator.ofFloat((Object)this.captionTextView, View.TRANSLATION_Y, new float[] { 0.0f, (float)AndroidUtilities.dp(96.0f) }));
                    }
                    final int sendPhotoType = this.sendPhotoType;
                    if (sendPhotoType == 0 || sendPhotoType == 4) {
                        list2.add(ObjectAnimator.ofFloat((Object)this.checkImageView, View.ALPHA, new float[] { 1.0f, 0.0f }));
                        list2.add(ObjectAnimator.ofFloat((Object)this.photosCounterView, View.ALPHA, new float[] { 1.0f, 0.0f }));
                    }
                    if (this.selectedPhotosListView.getVisibility() == 0) {
                        list2.add(ObjectAnimator.ofFloat((Object)this.selectedPhotosListView, View.ALPHA, new float[] { 1.0f, 0.0f }));
                    }
                    if (this.cameraItem.getTag() != null) {
                        list2.add(ObjectAnimator.ofFloat((Object)this.cameraItem, View.ALPHA, new float[] { 1.0f, 0.0f }));
                    }
                    this.changeModeAnimation.playTogether((Collection)list2);
                    this.changeModeAnimation.setDuration(200L);
                    this.changeModeAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        final /* synthetic */ PhotoViewer this$0;
                        
                        public void onAnimationEnd(final Animator animator) {
                            PhotoViewer.this.changeModeAnimation = null;
                            PhotoViewer.this.pickerView.setVisibility(8);
                            PhotoViewer.this.pickerViewSendButton.setVisibility(8);
                            PhotoViewer.this.cameraItem.setVisibility(8);
                            PhotoViewer.this.selectedPhotosListView.setVisibility(8);
                            PhotoViewer.this.selectedPhotosListView.setAlpha(0.0f);
                            PhotoViewer.this.selectedPhotosListView.setTranslationY((float)(-AndroidUtilities.dp(10.0f)));
                            PhotoViewer.this.photosCounterView.setRotationX(0.0f);
                            PhotoViewer.this.selectedPhotosListView.setEnabled(false);
                            PhotoViewer.this.isPhotosListViewVisible = false;
                            if (PhotoViewer.this.needCaptionLayout) {
                                PhotoViewer.this.captionTextView.setVisibility(4);
                            }
                            if (PhotoViewer.this.sendPhotoType == 0 || PhotoViewer.this.sendPhotoType == 4 || ((PhotoViewer.this.sendPhotoType == 2 || PhotoViewer.this.sendPhotoType == 5) && PhotoViewer.this.imagesArrLocals.size() > 1)) {
                                PhotoViewer.this.checkImageView.setVisibility(8);
                                PhotoViewer.this.photosCounterView.setVisibility(8);
                            }
                            final Bitmap bitmap = PhotoViewer.this.centerImage.getBitmap();
                            if (bitmap != null) {
                                PhotoViewer.this.photoCropView.setBitmap(bitmap, PhotoViewer.this.centerImage.getOrientation(), PhotoViewer.this.sendPhotoType != 1, false);
                                PhotoViewer.this.photoCropView.onDisappear();
                                final int bitmapWidth = PhotoViewer.this.centerImage.getBitmapWidth();
                                final int bitmapHeight = PhotoViewer.this.centerImage.getBitmapHeight();
                                final float n = (float)PhotoViewer.this.getContainerViewWidth();
                                final float n2 = (float)bitmapWidth;
                                final float n3 = n / n2;
                                final float n4 = (float)PhotoViewer.this.getContainerViewHeight();
                                final float n5 = (float)bitmapHeight;
                                final float n6 = n4 / n5;
                                final float n7 = PhotoViewer.this.getContainerViewWidth(1) / n2;
                                final float n8 = PhotoViewer.this.getContainerViewHeight(1) / n5;
                                float n9 = n3;
                                if (n3 > n6) {
                                    n9 = n6;
                                }
                                float n10 = n7;
                                if (n7 > n8) {
                                    n10 = n8;
                                }
                                if (PhotoViewer.this.sendPhotoType == 1) {
                                    final float n11 = (float)Math.min(PhotoViewer.this.getContainerViewWidth(1), PhotoViewer.this.getContainerViewHeight(1));
                                    n10 = n11 / n2;
                                    final float n12 = n11 / n5;
                                    if (n10 <= n12) {
                                        n10 = n12;
                                    }
                                }
                                PhotoViewer.this.animateToScale = n10 / n9;
                                final PhotoViewer this$0 = PhotoViewer.this;
                                this$0.animateToX = (float)(this$0.getLeftInset() / 2 - PhotoViewer.this.getRightInset() / 2);
                                final PhotoViewer this$2 = PhotoViewer.this;
                                final int n13 = -AndroidUtilities.dp(56.0f);
                                int n14;
                                if (Build$VERSION.SDK_INT >= 21) {
                                    n14 = AndroidUtilities.statusBarHeight / 2;
                                }
                                else {
                                    n14 = 0;
                                }
                                this$2.animateToY = (float)(n13 + n14);
                                PhotoViewer.this.animationStartTime = System.currentTimeMillis();
                                PhotoViewer.this.zoomAnimation = true;
                            }
                            PhotoViewer.this.imageMoveAnimation = new AnimatorSet();
                            PhotoViewer.this.imageMoveAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.editorDoneLayout, View.TRANSLATION_Y, new float[] { (float)AndroidUtilities.dp(48.0f), 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this, (Property)AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.photoCropView, View.ALPHA, new float[] { 0.0f, 1.0f }) });
                            PhotoViewer.this.imageMoveAnimation.setDuration(200L);
                            PhotoViewer.this.imageMoveAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                                public void onAnimationEnd(final Animator animator) {
                                    PhotoViewer.this.photoCropView.onAppeared();
                                    PhotoViewer.this.imageMoveAnimation = null;
                                    final AnimatorListenerAdapter this$1 = AnimatorListenerAdapter.this;
                                    this$1.this$0.currentEditMode = n;
                                    PhotoViewer.this.animateToScale = 1.0f;
                                    PhotoViewer.this.animateToX = 0.0f;
                                    PhotoViewer.this.animateToY = 0.0f;
                                    PhotoViewer.this.scale = 1.0f;
                                    final PhotoViewer this$2 = PhotoViewer.this;
                                    this$2.updateMinMax(this$2.scale);
                                    PhotoViewer.this.padImageForHorizontalInsets = true;
                                    PhotoViewer.this.containerView.invalidate();
                                }
                                
                                public void onAnimationStart(final Animator animator) {
                                    PhotoViewer.this.editorDoneLayout.setVisibility(0);
                                    PhotoViewer.this.photoCropView.setVisibility(0);
                                }
                            });
                            PhotoViewer.this.imageMoveAnimation.start();
                        }
                    });
                    this.changeModeAnimation.start();
                }
                else if (n == 2) {
                    if (this.photoFilterView == null) {
                        final boolean empty = this.imagesArrLocals.isEmpty();
                        MediaController.SavedFilterState savedFilterState = null;
                        MediaController.SavedFilterState savedFilterState2 = null;
                        int n12 = 0;
                        String s = null;
                        Label_1200: {
                            String imageUrl = null;
                            Label_1194: {
                                if (!empty) {
                                    final MediaController.SearchImage value = this.imagesArrLocals.get(this.currentIndex);
                                    if (value instanceof MediaController.PhotoEntry) {
                                        final MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry)value;
                                        String path;
                                        if (photoEntry.imagePath == null) {
                                            path = photoEntry.path;
                                            savedFilterState2 = photoEntry.savedFilterState;
                                        }
                                        else {
                                            path = null;
                                        }
                                        n12 = photoEntry.orientation;
                                        s = path;
                                        savedFilterState = savedFilterState2;
                                        break Label_1200;
                                    }
                                    if (value instanceof MediaController.SearchImage) {
                                        final MediaController.SearchImage searchImage = value;
                                        savedFilterState = searchImage.savedFilterState;
                                        imageUrl = searchImage.imageUrl;
                                        break Label_1194;
                                    }
                                }
                                imageUrl = null;
                            }
                            n12 = 0;
                            s = imageUrl;
                        }
                        Bitmap bitmap;
                        if (savedFilterState == null) {
                            bitmap = this.centerImage.getBitmap();
                            n12 = this.centerImage.getOrientation();
                        }
                        else {
                            bitmap = BitmapFactory.decodeFile(s);
                        }
                        this.photoFilterView = new PhotoFilterView((Context)this.parentActivity, bitmap, n12, savedFilterState);
                        this.containerView.addView((View)this.photoFilterView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                        this.photoFilterView.getDoneTextView().setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$_kiq87m6DEbP6Rtuq9Z0xaIwbfE(this));
                        this.photoFilterView.getCancelTextView().setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$2VWEpko1xopXpDQMDqF1erZanWU(this));
                        this.photoFilterView.getToolsView().setTranslationY((float)AndroidUtilities.dp(186.0f));
                    }
                    this.changeModeAnimation = new AnimatorSet();
                    final ArrayList<ObjectAnimator> list3 = new ArrayList<ObjectAnimator>();
                    list3.add(ObjectAnimator.ofFloat((Object)this.pickerView, View.TRANSLATION_Y, new float[] { 0.0f, (float)AndroidUtilities.dp(96.0f) }));
                    list3.add(ObjectAnimator.ofFloat((Object)this.pickerViewSendButton, View.TRANSLATION_Y, new float[] { 0.0f, (float)AndroidUtilities.dp(96.0f) }));
                    final ActionBar actionBar2 = this.actionBar;
                    list3.add(ObjectAnimator.ofFloat((Object)actionBar2, View.TRANSLATION_Y, new float[] { 0.0f, (float)(-actionBar2.getHeight()) }));
                    final int sendPhotoType2 = this.sendPhotoType;
                    if (sendPhotoType2 != 0 && sendPhotoType2 != 4) {
                        if (sendPhotoType2 == 1) {
                            list3.add(ObjectAnimator.ofFloat((Object)this.photoCropView, View.ALPHA, new float[] { 1.0f, 0.0f }));
                        }
                    }
                    else {
                        list3.add(ObjectAnimator.ofFloat((Object)this.checkImageView, View.ALPHA, new float[] { 1.0f, 0.0f }));
                        list3.add(ObjectAnimator.ofFloat((Object)this.photosCounterView, View.ALPHA, new float[] { 1.0f, 0.0f }));
                    }
                    if (this.selectedPhotosListView.getVisibility() == 0) {
                        list3.add(ObjectAnimator.ofFloat((Object)this.selectedPhotosListView, View.ALPHA, new float[] { 1.0f, 0.0f }));
                    }
                    if (this.cameraItem.getTag() != null) {
                        list3.add(ObjectAnimator.ofFloat((Object)this.cameraItem, View.ALPHA, new float[] { 1.0f, 0.0f }));
                    }
                    this.changeModeAnimation.playTogether((Collection)list3);
                    this.changeModeAnimation.setDuration(200L);
                    this.changeModeAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        final /* synthetic */ PhotoViewer this$0;
                        
                        public void onAnimationEnd(final Animator animator) {
                            PhotoViewer.this.changeModeAnimation = null;
                            PhotoViewer.this.pickerView.setVisibility(8);
                            PhotoViewer.this.pickerViewSendButton.setVisibility(8);
                            PhotoViewer.this.actionBar.setVisibility(8);
                            PhotoViewer.this.cameraItem.setVisibility(8);
                            PhotoViewer.this.selectedPhotosListView.setVisibility(8);
                            PhotoViewer.this.selectedPhotosListView.setAlpha(0.0f);
                            PhotoViewer.this.selectedPhotosListView.setTranslationY((float)(-AndroidUtilities.dp(10.0f)));
                            PhotoViewer.this.photosCounterView.setRotationX(0.0f);
                            PhotoViewer.this.selectedPhotosListView.setEnabled(false);
                            PhotoViewer.this.isPhotosListViewVisible = false;
                            if (PhotoViewer.this.needCaptionLayout) {
                                PhotoViewer.this.captionTextView.setVisibility(4);
                            }
                            if (PhotoViewer.this.sendPhotoType == 0 || PhotoViewer.this.sendPhotoType == 4 || ((PhotoViewer.this.sendPhotoType == 2 || PhotoViewer.this.sendPhotoType == 5) && PhotoViewer.this.imagesArrLocals.size() > 1)) {
                                PhotoViewer.this.checkImageView.setVisibility(8);
                                PhotoViewer.this.photosCounterView.setVisibility(8);
                            }
                            if (PhotoViewer.this.centerImage.getBitmap() != null) {
                                final int bitmapWidth = PhotoViewer.this.centerImage.getBitmapWidth();
                                final int bitmapHeight = PhotoViewer.this.centerImage.getBitmapHeight();
                                final float n = (float)PhotoViewer.this.getContainerViewWidth();
                                final float n2 = (float)bitmapWidth;
                                final float n3 = n / n2;
                                final float n4 = (float)PhotoViewer.this.getContainerViewHeight();
                                final float n5 = (float)bitmapHeight;
                                final float n6 = n4 / n5;
                                final float n7 = PhotoViewer.this.getContainerViewWidth(2) / n2;
                                final float n8 = PhotoViewer.this.getContainerViewHeight(2) / n5;
                                float n9 = n3;
                                if (n3 > n6) {
                                    n9 = n6;
                                }
                                float n10;
                                if (n7 > n8) {
                                    n10 = n8;
                                }
                                else {
                                    n10 = n7;
                                }
                                PhotoViewer.this.animateToScale = n10 / n9;
                                final PhotoViewer this$0 = PhotoViewer.this;
                                this$0.animateToX = (float)(this$0.getLeftInset() / 2 - PhotoViewer.this.getRightInset() / 2);
                                final PhotoViewer this$2 = PhotoViewer.this;
                                final int n11 = -AndroidUtilities.dp(92.0f);
                                int n12;
                                if (Build$VERSION.SDK_INT >= 21) {
                                    n12 = AndroidUtilities.statusBarHeight / 2;
                                }
                                else {
                                    n12 = 0;
                                }
                                this$2.animateToY = (float)(n11 + n12);
                                PhotoViewer.this.animationStartTime = System.currentTimeMillis();
                                PhotoViewer.this.zoomAnimation = true;
                            }
                            PhotoViewer.this.imageMoveAnimation = new AnimatorSet();
                            PhotoViewer.this.imageMoveAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this, (Property)AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.photoFilterView.getToolsView(), View.TRANSLATION_Y, new float[] { (float)AndroidUtilities.dp(186.0f), 0.0f }) });
                            PhotoViewer.this.imageMoveAnimation.setDuration(200L);
                            PhotoViewer.this.imageMoveAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                                public void onAnimationEnd(final Animator animator) {
                                    PhotoViewer.this.photoFilterView.init();
                                    PhotoViewer.this.imageMoveAnimation = null;
                                    final AnimatorListenerAdapter this$1 = AnimatorListenerAdapter.this;
                                    this$1.this$0.currentEditMode = n;
                                    PhotoViewer.this.animateToScale = 1.0f;
                                    PhotoViewer.this.animateToX = 0.0f;
                                    PhotoViewer.this.animateToY = 0.0f;
                                    PhotoViewer.this.scale = 1.0f;
                                    final PhotoViewer this$2 = PhotoViewer.this;
                                    this$2.updateMinMax(this$2.scale);
                                    PhotoViewer.this.padImageForHorizontalInsets = true;
                                    PhotoViewer.this.containerView.invalidate();
                                    if (PhotoViewer.this.sendPhotoType == 1) {
                                        PhotoViewer.this.photoCropView.reset();
                                    }
                                }
                                
                                public void onAnimationStart(final Animator animator) {
                                }
                            });
                            PhotoViewer.this.imageMoveAnimation.start();
                        }
                    });
                    this.changeModeAnimation.start();
                }
                else if (n == 3) {
                    if (this.photoPaintView == null) {
                        this.photoPaintView = new PhotoPaintView((Context)this.parentActivity, this.centerImage.getBitmap(), this.centerImage.getOrientation());
                        this.containerView.addView((View)this.photoPaintView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                        this.photoPaintView.getDoneTextView().setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$iPzcqyK9klw8fE_zuZB7xfys4J8(this));
                        this.photoPaintView.getCancelTextView().setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$Ws0W6J_E4CCAikNDkwUaS7ufRkg(this));
                        this.photoPaintView.getColorPicker().setTranslationY((float)AndroidUtilities.dp(126.0f));
                        this.photoPaintView.getToolsView().setTranslationY((float)AndroidUtilities.dp(126.0f));
                    }
                    this.changeModeAnimation = new AnimatorSet();
                    final ArrayList<ObjectAnimator> list4 = new ArrayList<ObjectAnimator>();
                    list4.add(ObjectAnimator.ofFloat((Object)this.pickerView, View.TRANSLATION_Y, new float[] { 0.0f, (float)AndroidUtilities.dp(96.0f) }));
                    list4.add(ObjectAnimator.ofFloat((Object)this.pickerViewSendButton, View.TRANSLATION_Y, new float[] { 0.0f, (float)AndroidUtilities.dp(96.0f) }));
                    final ActionBar actionBar3 = this.actionBar;
                    list4.add(ObjectAnimator.ofFloat((Object)actionBar3, View.TRANSLATION_Y, new float[] { 0.0f, (float)(-actionBar3.getHeight()) }));
                    if (this.needCaptionLayout) {
                        list4.add(ObjectAnimator.ofFloat((Object)this.captionTextView, View.TRANSLATION_Y, new float[] { 0.0f, (float)AndroidUtilities.dp(96.0f) }));
                    }
                    final int sendPhotoType3 = this.sendPhotoType;
                    if (sendPhotoType3 != 0 && sendPhotoType3 != 4) {
                        if (sendPhotoType3 == 1) {
                            list4.add(ObjectAnimator.ofFloat((Object)this.photoCropView, View.ALPHA, new float[] { 1.0f, 0.0f }));
                        }
                    }
                    else {
                        list4.add(ObjectAnimator.ofFloat((Object)this.checkImageView, View.ALPHA, new float[] { 1.0f, 0.0f }));
                        list4.add(ObjectAnimator.ofFloat((Object)this.photosCounterView, View.ALPHA, new float[] { 1.0f, 0.0f }));
                    }
                    if (this.selectedPhotosListView.getVisibility() == 0) {
                        list4.add(ObjectAnimator.ofFloat((Object)this.selectedPhotosListView, View.ALPHA, new float[] { 1.0f, 0.0f }));
                    }
                    if (this.cameraItem.getTag() != null) {
                        list4.add(ObjectAnimator.ofFloat((Object)this.cameraItem, View.ALPHA, new float[] { 1.0f, 0.0f }));
                    }
                    this.changeModeAnimation.playTogether((Collection)list4);
                    this.changeModeAnimation.setDuration(200L);
                    this.changeModeAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        final /* synthetic */ PhotoViewer this$0;
                        
                        public void onAnimationEnd(final Animator animator) {
                            PhotoViewer.this.changeModeAnimation = null;
                            PhotoViewer.this.pickerView.setVisibility(8);
                            PhotoViewer.this.pickerViewSendButton.setVisibility(8);
                            PhotoViewer.this.cameraItem.setVisibility(8);
                            PhotoViewer.this.selectedPhotosListView.setVisibility(8);
                            PhotoViewer.this.selectedPhotosListView.setAlpha(0.0f);
                            PhotoViewer.this.selectedPhotosListView.setTranslationY((float)(-AndroidUtilities.dp(10.0f)));
                            PhotoViewer.this.photosCounterView.setRotationX(0.0f);
                            PhotoViewer.this.selectedPhotosListView.setEnabled(false);
                            PhotoViewer.this.isPhotosListViewVisible = false;
                            if (PhotoViewer.this.needCaptionLayout) {
                                PhotoViewer.this.captionTextView.setVisibility(4);
                            }
                            if (PhotoViewer.this.sendPhotoType == 0 || PhotoViewer.this.sendPhotoType == 4 || ((PhotoViewer.this.sendPhotoType == 2 || PhotoViewer.this.sendPhotoType == 5) && PhotoViewer.this.imagesArrLocals.size() > 1)) {
                                PhotoViewer.this.checkImageView.setVisibility(8);
                                PhotoViewer.this.photosCounterView.setVisibility(8);
                            }
                            if (PhotoViewer.this.centerImage.getBitmap() != null) {
                                final int bitmapWidth = PhotoViewer.this.centerImage.getBitmapWidth();
                                final int bitmapHeight = PhotoViewer.this.centerImage.getBitmapHeight();
                                final float n = (float)PhotoViewer.this.getContainerViewWidth();
                                final float n2 = (float)bitmapWidth;
                                final float n3 = n / n2;
                                final float n4 = (float)PhotoViewer.this.getContainerViewHeight();
                                final float n5 = (float)bitmapHeight;
                                final float n6 = n4 / n5;
                                final float n7 = PhotoViewer.this.getContainerViewWidth(3) / n2;
                                final float n8 = PhotoViewer.this.getContainerViewHeight(3) / n5;
                                float n9 = n3;
                                if (n3 > n6) {
                                    n9 = n6;
                                }
                                float n10;
                                if (n7 > n8) {
                                    n10 = n8;
                                }
                                else {
                                    n10 = n7;
                                }
                                PhotoViewer.this.animateToScale = n10 / n9;
                                final PhotoViewer this$0 = PhotoViewer.this;
                                this$0.animateToX = (float)(this$0.getLeftInset() / 2 - PhotoViewer.this.getRightInset() / 2);
                                final PhotoViewer this$2 = PhotoViewer.this;
                                final int n11 = -AndroidUtilities.dp(44.0f);
                                int n12;
                                if (Build$VERSION.SDK_INT >= 21) {
                                    n12 = AndroidUtilities.statusBarHeight / 2;
                                }
                                else {
                                    n12 = 0;
                                }
                                this$2.animateToY = (float)(n11 + n12);
                                PhotoViewer.this.animationStartTime = System.currentTimeMillis();
                                PhotoViewer.this.zoomAnimation = true;
                            }
                            PhotoViewer.this.imageMoveAnimation = new AnimatorSet();
                            PhotoViewer.this.imageMoveAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this, (Property)AnimationProperties.PHOTO_VIEWER_ANIMATION_VALUE, new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.photoPaintView.getColorPicker(), View.TRANSLATION_Y, new float[] { (float)AndroidUtilities.dp(126.0f), 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.photoPaintView.getToolsView(), View.TRANSLATION_Y, new float[] { (float)AndroidUtilities.dp(126.0f), 0.0f }) });
                            PhotoViewer.this.imageMoveAnimation.setDuration(200L);
                            PhotoViewer.this.imageMoveAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                                public void onAnimationEnd(final Animator animator) {
                                    PhotoViewer.this.photoPaintView.init();
                                    PhotoViewer.this.imageMoveAnimation = null;
                                    final AnimatorListenerAdapter this$1 = AnimatorListenerAdapter.this;
                                    this$1.this$0.currentEditMode = n;
                                    PhotoViewer.this.animateToScale = 1.0f;
                                    PhotoViewer.this.animateToX = 0.0f;
                                    PhotoViewer.this.animateToY = 0.0f;
                                    PhotoViewer.this.scale = 1.0f;
                                    final PhotoViewer this$2 = PhotoViewer.this;
                                    this$2.updateMinMax(this$2.scale);
                                    PhotoViewer.this.padImageForHorizontalInsets = true;
                                    PhotoViewer.this.containerView.invalidate();
                                    if (PhotoViewer.this.sendPhotoType == 1) {
                                        PhotoViewer.this.photoCropView.reset();
                                    }
                                }
                                
                                public void onAnimationStart(final Animator animator) {
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
        if (this.videoPlayer != null && this.textureUploaded && this.checkInlinePermissions() && !this.changingTextureView && !this.switchingInlineMode) {
            if (!this.isInline) {
                if (PhotoViewer.PipInstance != null) {
                    PhotoViewer.PipInstance.destroyPhotoViewer();
                }
                this.openedFullScreenVideo = false;
                PhotoViewer.PipInstance = PhotoViewer.Instance;
                PhotoViewer.Instance = null;
                this.switchingInlineMode = true;
                this.isVisible = false;
                final PlaceProviderObject currentPlaceObject = this.currentPlaceObject;
                if (currentPlaceObject != null) {
                    currentPlaceObject.imageReceiver.setVisible(true, true);
                    final AnimatedFileDrawable animation = this.currentPlaceObject.imageReceiver.getAnimation();
                    if (animation != null) {
                        final Bitmap animatedBitmap = animation.getAnimatedBitmap();
                        if (animatedBitmap != null) {
                            try {
                                final Bitmap bitmap = this.videoTextureView.getBitmap(animatedBitmap.getWidth(), animatedBitmap.getHeight());
                                new Canvas(animatedBitmap).drawBitmap(bitmap, 0.0f, 0.0f, (Paint)null);
                                bitmap.recycle();
                            }
                            catch (Throwable t) {
                                FileLog.e(t);
                            }
                        }
                        animation.seekTo(this.videoPlayer.getCurrentPosition(), true);
                        this.currentPlaceObject.imageReceiver.setAllowStartAnimation(true);
                        this.currentPlaceObject.imageReceiver.startAnimation();
                    }
                }
                if (Build$VERSION.SDK_INT >= 21) {
                    this.pipAnimationInProgress = true;
                    final Rect pipRect = PipVideoView.getPipRect(this.aspectRatioFrameLayout.getAspectRatio());
                    final float n = pipRect.width / this.videoTextureView.getWidth();
                    pipRect.y += AndroidUtilities.statusBarHeight;
                    final AnimatorSet set = new AnimatorSet();
                    set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.textureImageView, View.SCALE_X, new float[] { n }), (Animator)ObjectAnimator.ofFloat((Object)this.textureImageView, View.SCALE_Y, new float[] { n }), (Animator)ObjectAnimator.ofFloat((Object)this.textureImageView, View.TRANSLATION_X, new float[] { pipRect.x }), (Animator)ObjectAnimator.ofFloat((Object)this.textureImageView, View.TRANSLATION_Y, new float[] { pipRect.y }), (Animator)ObjectAnimator.ofFloat((Object)this.videoTextureView, View.SCALE_X, new float[] { n }), (Animator)ObjectAnimator.ofFloat((Object)this.videoTextureView, View.SCALE_Y, new float[] { n }), (Animator)ObjectAnimator.ofFloat((Object)this.videoTextureView, View.TRANSLATION_X, new float[] { pipRect.x - this.aspectRatioFrameLayout.getX() + this.getLeftInset() }), (Animator)ObjectAnimator.ofFloat((Object)this.videoTextureView, View.TRANSLATION_Y, new float[] { pipRect.y - this.aspectRatioFrameLayout.getY() }), (Animator)ObjectAnimator.ofInt((Object)this.backgroundDrawable, (Property)AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[] { 0 }), (Animator)ObjectAnimator.ofFloat((Object)this.actionBar, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.bottomLayout, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.captionTextView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.groupedPhotosListView, View.ALPHA, new float[] { 0.0f }) });
                    set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                    set.setDuration(250L);
                    set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            PhotoViewer.this.pipAnimationInProgress = false;
                            PhotoViewer.this.switchToInlineRunnable.run();
                        }
                    });
                    set.start();
                }
                else {
                    this.switchToInlineRunnable.run();
                    this.dismissInternal();
                }
            }
        }
    }
    
    private void toggleActionBar(final boolean isActionBarVisible, final boolean b) {
        final AnimatorSet actionBarAnimator = this.actionBarAnimator;
        if (actionBarAnimator != null) {
            actionBarAnimator.cancel();
        }
        if (isActionBarVisible) {
            this.actionBar.setVisibility(0);
            if (this.bottomLayout.getTag() != null) {
                this.bottomLayout.setVisibility(0);
            }
            if (this.captionTextView.getTag() != null) {
                this.captionTextView.setVisibility(0);
            }
        }
        this.isActionBarVisible = isActionBarVisible;
        if (Build$VERSION.SDK_INT >= 21 && this.sendPhotoType != 1) {
            int n;
            if (this.containerView.getPaddingLeft() <= 0 && this.containerView.getPaddingRight() <= 0) {
                n = 0;
            }
            else {
                n = 4098;
            }
            final int n2 = 0x4 | n;
            if (isActionBarVisible) {
                final FrameLayoutDrawer containerView = this.containerView;
                containerView.setSystemUiVisibility(~n2 & containerView.getSystemUiVisibility());
            }
            else {
                final FrameLayoutDrawer containerView2 = this.containerView;
                containerView2.setSystemUiVisibility(n2 | containerView2.getSystemUiVisibility());
            }
        }
        float alpha = 1.0f;
        if (b) {
            final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
            final ActionBar actionBar = this.actionBar;
            final Property alpha2 = View.ALPHA;
            float n3;
            if (isActionBarVisible) {
                n3 = 1.0f;
            }
            else {
                n3 = 0.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)actionBar, alpha2, new float[] { n3 }));
            final FrameLayout bottomLayout = this.bottomLayout;
            if (bottomLayout != null) {
                final Property alpha3 = View.ALPHA;
                float n4;
                if (isActionBarVisible) {
                    n4 = 1.0f;
                }
                else {
                    n4 = 0.0f;
                }
                list.add(ObjectAnimator.ofFloat((Object)bottomLayout, alpha3, new float[] { n4 }));
            }
            final GroupedPhotosListView groupedPhotosListView = this.groupedPhotosListView;
            final Property alpha4 = View.ALPHA;
            float n5;
            if (isActionBarVisible) {
                n5 = 1.0f;
            }
            else {
                n5 = 0.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)groupedPhotosListView, alpha4, new float[] { n5 }));
            if (this.captionTextView.getTag() != null) {
                final TextView captionTextView = this.captionTextView;
                final Property alpha5 = View.ALPHA;
                if (!isActionBarVisible) {
                    alpha = 0.0f;
                }
                list.add(ObjectAnimator.ofFloat((Object)captionTextView, alpha5, new float[] { alpha }));
            }
            (this.actionBarAnimator = new AnimatorSet()).playTogether((Collection)list);
            this.actionBarAnimator.setDuration(200L);
            this.actionBarAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator animator) {
                    if (animator.equals(PhotoViewer.this.actionBarAnimator)) {
                        PhotoViewer.this.actionBarAnimator = null;
                    }
                }
                
                public void onAnimationEnd(final Animator animator) {
                    if (animator.equals(PhotoViewer.this.actionBarAnimator)) {
                        if (!isActionBarVisible) {
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
        }
        else {
            final ActionBar actionBar2 = this.actionBar;
            float alpha6;
            if (isActionBarVisible) {
                alpha6 = 1.0f;
            }
            else {
                alpha6 = 0.0f;
            }
            actionBar2.setAlpha(alpha6);
            final FrameLayout bottomLayout2 = this.bottomLayout;
            float alpha7;
            if (isActionBarVisible) {
                alpha7 = 1.0f;
            }
            else {
                alpha7 = 0.0f;
            }
            bottomLayout2.setAlpha(alpha7);
            final GroupedPhotosListView groupedPhotosListView2 = this.groupedPhotosListView;
            float alpha8;
            if (isActionBarVisible) {
                alpha8 = 1.0f;
            }
            else {
                alpha8 = 0.0f;
            }
            groupedPhotosListView2.setAlpha(alpha8);
            final TextView captionTextView2 = this.captionTextView;
            if (!isActionBarVisible) {
                alpha = 0.0f;
            }
            captionTextView2.setAlpha(alpha);
        }
    }
    
    private void toggleCheckImageView(final boolean b) {
        final AnimatorSet set = new AnimatorSet();
        final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
        final FrameLayout pickerView = this.pickerView;
        final Property alpha = View.ALPHA;
        final float n = 1.0f;
        float n2;
        if (b) {
            n2 = 1.0f;
        }
        else {
            n2 = 0.0f;
        }
        list.add(ObjectAnimator.ofFloat((Object)pickerView, alpha, new float[] { n2 }));
        final ImageView pickerViewSendButton = this.pickerViewSendButton;
        final Property alpha2 = View.ALPHA;
        float n3;
        if (b) {
            n3 = 1.0f;
        }
        else {
            n3 = 0.0f;
        }
        list.add(ObjectAnimator.ofFloat((Object)pickerViewSendButton, alpha2, new float[] { n3 }));
        if (this.needCaptionLayout) {
            final TextView captionTextView = this.captionTextView;
            final Property alpha3 = View.ALPHA;
            float n4;
            if (b) {
                n4 = 1.0f;
            }
            else {
                n4 = 0.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)captionTextView, alpha3, new float[] { n4 }));
        }
        final int sendPhotoType = this.sendPhotoType;
        if (sendPhotoType == 0 || sendPhotoType == 4) {
            final CheckBox checkImageView = this.checkImageView;
            final Property alpha4 = View.ALPHA;
            float n5;
            if (b) {
                n5 = 1.0f;
            }
            else {
                n5 = 0.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)checkImageView, alpha4, new float[] { n5 }));
            final CounterView photosCounterView = this.photosCounterView;
            final Property alpha5 = View.ALPHA;
            float n6;
            if (b) {
                n6 = n;
            }
            else {
                n6 = 0.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)photosCounterView, alpha5, new float[] { n6 }));
        }
        set.playTogether((Collection)list);
        set.setDuration(200L);
        set.start();
    }
    
    private void toggleMiniProgress(final boolean b, final boolean b2) {
        int visibility = 0;
        if (b2) {
            this.toggleMiniProgressInternal(b);
            if (b) {
                final AnimatorSet miniProgressAnimator = this.miniProgressAnimator;
                if (miniProgressAnimator != null) {
                    miniProgressAnimator.cancel();
                    this.miniProgressAnimator = null;
                }
                AndroidUtilities.cancelRunOnUIThread(this.miniProgressShowRunnable);
                if (this.firstAnimationDelay) {
                    this.firstAnimationDelay = false;
                    this.toggleMiniProgressInternal(true);
                }
                else {
                    AndroidUtilities.runOnUIThread(this.miniProgressShowRunnable, 500L);
                }
            }
            else {
                AndroidUtilities.cancelRunOnUIThread(this.miniProgressShowRunnable);
                final AnimatorSet miniProgressAnimator2 = this.miniProgressAnimator;
                if (miniProgressAnimator2 != null) {
                    miniProgressAnimator2.cancel();
                    this.toggleMiniProgressInternal(false);
                }
            }
        }
        else {
            final AnimatorSet miniProgressAnimator3 = this.miniProgressAnimator;
            if (miniProgressAnimator3 != null) {
                miniProgressAnimator3.cancel();
                this.miniProgressAnimator = null;
            }
            final RadialProgressView miniProgressView = this.miniProgressView;
            float alpha;
            if (b) {
                alpha = 1.0f;
            }
            else {
                alpha = 0.0f;
            }
            miniProgressView.setAlpha(alpha);
            final RadialProgressView miniProgressView2 = this.miniProgressView;
            if (!b) {
                visibility = 4;
            }
            miniProgressView2.setVisibility(visibility);
        }
    }
    
    private void toggleMiniProgressInternal(final boolean b) {
        if (b) {
            this.miniProgressView.setVisibility(0);
        }
        this.miniProgressAnimator = new AnimatorSet();
        final AnimatorSet miniProgressAnimator = this.miniProgressAnimator;
        final RadialProgressView miniProgressView = this.miniProgressView;
        final Property alpha = View.ALPHA;
        float n;
        if (b) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        miniProgressAnimator.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)miniProgressView, alpha, new float[] { n }) });
        this.miniProgressAnimator.setDuration(200L);
        this.miniProgressAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                if (animator.equals(PhotoViewer.this.miniProgressAnimator)) {
                    PhotoViewer.this.miniProgressAnimator = null;
                }
            }
            
            public void onAnimationEnd(final Animator animator) {
                if (animator.equals(PhotoViewer.this.miniProgressAnimator)) {
                    if (!b) {
                        PhotoViewer.this.miniProgressView.setVisibility(4);
                    }
                    PhotoViewer.this.miniProgressAnimator = null;
                }
            }
        });
        this.miniProgressAnimator.start();
    }
    
    private void togglePhotosListView(final boolean b, final boolean b2) {
        if (b == this.isPhotosListViewVisible) {
            return;
        }
        if (b) {
            this.selectedPhotosListView.setVisibility(0);
        }
        this.isPhotosListViewVisible = b;
        this.selectedPhotosListView.setEnabled(b);
        float rotationX = 1.0f;
        if (b2) {
            final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
            final RecyclerListView selectedPhotosListView = this.selectedPhotosListView;
            final Property alpha = View.ALPHA;
            float n;
            if (b) {
                n = 1.0f;
            }
            else {
                n = 0.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)selectedPhotosListView, alpha, new float[] { n }));
            final RecyclerListView selectedPhotosListView2 = this.selectedPhotosListView;
            final Property translation_Y = View.TRANSLATION_Y;
            float n2;
            if (b) {
                n2 = 0.0f;
            }
            else {
                n2 = (float)(-AndroidUtilities.dp(10.0f));
            }
            list.add(ObjectAnimator.ofFloat((Object)selectedPhotosListView2, translation_Y, new float[] { n2 }));
            final CounterView photosCounterView = this.photosCounterView;
            final Property rotation_X = View.ROTATION_X;
            if (!b) {
                rotationX = 0.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)photosCounterView, rotation_X, new float[] { rotationX }));
            (this.currentListViewAnimation = new AnimatorSet()).playTogether((Collection)list);
            if (!b) {
                this.currentListViewAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator obj) {
                        if (PhotoViewer.this.currentListViewAnimation != null && PhotoViewer.this.currentListViewAnimation.equals(obj)) {
                            PhotoViewer.this.selectedPhotosListView.setVisibility(8);
                            PhotoViewer.this.currentListViewAnimation = null;
                        }
                    }
                });
            }
            this.currentListViewAnimation.setDuration(200L);
            this.currentListViewAnimation.start();
        }
        else {
            final RecyclerListView selectedPhotosListView3 = this.selectedPhotosListView;
            float alpha2;
            if (b) {
                alpha2 = 1.0f;
            }
            else {
                alpha2 = 0.0f;
            }
            selectedPhotosListView3.setAlpha(alpha2);
            final RecyclerListView selectedPhotosListView4 = this.selectedPhotosListView;
            float translationY;
            if (b) {
                translationY = 0.0f;
            }
            else {
                translationY = (float)(-AndroidUtilities.dp(10.0f));
            }
            selectedPhotosListView4.setTranslationY(translationY);
            final CounterView photosCounterView2 = this.photosCounterView;
            if (!b) {
                rotationX = 0.0f;
            }
            photosCounterView2.setRotationX(rotationX);
            if (!b) {
                this.selectedPhotosListView.setVisibility(8);
            }
        }
    }
    
    private void updateAccessibilityOverlayVisibility() {
        if (this.playButtonAccessibilityOverlay == null) {
            return;
        }
        if (this.isCurrentVideo) {
            final VideoPlayer videoPlayer = this.videoPlayer;
            if (videoPlayer == null || !videoPlayer.isPlaying()) {
                this.playButtonAccessibilityOverlay.setVisibility(0);
                return;
            }
        }
        this.playButtonAccessibilityOverlay.setVisibility(4);
    }
    
    private void updateCaptionTextForCurrentPhoto(final Object o) {
        CharSequence fieldText = null;
        Label_0048: {
            if (o instanceof MediaController.PhotoEntry) {
                fieldText = ((MediaController.PhotoEntry)o).caption;
            }
            else {
                if (!(o instanceof TLRPC.BotInlineResult)) {
                    if (o instanceof MediaController.SearchImage) {
                        fieldText = ((MediaController.SearchImage)o).caption;
                        break Label_0048;
                    }
                }
                fieldText = null;
            }
        }
        if (TextUtils.isEmpty(fieldText)) {
            this.captionEditText.setFieldText("");
        }
        else {
            this.captionEditText.setFieldText(fieldText);
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
    
    private void updatePlayerState(final boolean b, final int n) {
        if (this.videoPlayer == null) {
            return;
        }
        if (this.isStreaming) {
            if (n == 2 && this.skipFirstBufferingProgress) {
                if (b) {
                    this.skipFirstBufferingProgress = false;
                }
            }
            else {
                this.toggleMiniProgress(this.seekToProgressPending != 0.0f || n == 2, true);
            }
        }
        if (b && n != 4 && n != 1) {
            try {
                this.parentActivity.getWindow().addFlags(128);
                this.keepScreenOnFlagSet = true;
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        else {
            try {
                this.parentActivity.getWindow().clearFlags(128);
                this.keepScreenOnFlagSet = false;
            }
            catch (Exception ex2) {
                FileLog.e(ex2);
            }
        }
        if (this.seekToProgressPending != 0.0f && (n == 3 || n == 1)) {
            this.videoPlayer.seekTo((int)(this.videoPlayer.getDuration() * this.seekToProgressPending));
            this.seekToProgressPending = 0.0f;
            final MessageObject currentMessageObject = this.currentMessageObject;
            if (currentMessageObject != null && !FileLoader.getInstance(currentMessageObject.currentAccount).isLoadingVideoAny(this.currentMessageObject.getDocument())) {
                this.skipFirstBufferingProgress = true;
            }
        }
        if (n == 3) {
            if (this.aspectRatioFrameLayout.getVisibility() != 0) {
                this.aspectRatioFrameLayout.setVisibility(0);
            }
            if (!this.pipItem.isEnabled()) {
                this.pipAvailable = true;
                this.pipItem.setEnabled(true);
                this.pipItem.setAlpha(1.0f);
            }
            this.playerWasReady = true;
            final MessageObject currentMessageObject2 = this.currentMessageObject;
            if (currentMessageObject2 != null && currentMessageObject2.isVideo()) {
                AndroidUtilities.cancelRunOnUIThread(this.setLoadingRunnable);
                FileLoader.getInstance(this.currentMessageObject.currentAccount).removeLoadingVideo(this.currentMessageObject.getDocument(), true, false);
            }
        }
        else if (n == 2 && b) {
            final MessageObject currentMessageObject3 = this.currentMessageObject;
            if (currentMessageObject3 != null && currentMessageObject3.isVideo()) {
                if (this.playerWasReady) {
                    this.setLoadingRunnable.run();
                }
                else {
                    AndroidUtilities.runOnUIThread(this.setLoadingRunnable, 1000L);
                }
            }
        }
        if (this.videoPlayer.isPlaying() && n != 4) {
            if (!this.isPlaying) {
                this.isPlaying = true;
                this.videoPlayButton.setImageResource(2131165478);
                AndroidUtilities.runOnUIThread(this.updateProgressRunnable);
            }
        }
        else if (this.isPlaying) {
            this.isPlaying = false;
            this.videoPlayButton.setImageResource(2131165479);
            AndroidUtilities.cancelRunOnUIThread(this.updateProgressRunnable);
            if (n == 4) {
                if (this.isCurrentVideo) {
                    if (!this.videoTimelineView.isDragging()) {
                        this.videoTimelineView.setProgress(0.0f);
                        if (!this.inPreview && this.videoTimelineView.getVisibility() == 0) {
                            this.videoPlayer.seekTo((int)(this.videoTimelineView.getLeftProgress() * this.videoPlayer.getDuration()));
                        }
                        else {
                            this.videoPlayer.seekTo(0L);
                        }
                        this.videoPlayer.pause();
                        this.containerView.invalidate();
                    }
                }
                else {
                    if (!this.isActionBarVisible) {
                        this.toggleActionBar(true, true);
                    }
                    if (!this.videoPlayerSeekbar.isDragging()) {
                        this.videoPlayerSeekbar.setProgress(0.0f);
                        this.videoPlayerControlFrameLayout.invalidate();
                        if (!this.inPreview && this.videoTimelineView.getVisibility() == 0) {
                            this.videoPlayer.seekTo((int)(this.videoTimelineView.getLeftProgress() * this.videoPlayer.getDuration()));
                        }
                        else {
                            this.videoPlayer.seekTo(0L);
                        }
                        this.videoPlayer.pause();
                    }
                }
                final PipVideoView pipVideoView = this.pipVideoView;
                if (pipVideoView != null) {
                    pipVideoView.onVideoCompleted();
                }
            }
        }
        final PipVideoView pipVideoView2 = this.pipVideoView;
        if (pipVideoView2 != null) {
            pipVideoView2.updatePlayButton();
        }
        this.updateVideoPlayerTime();
    }
    
    private void updateSelectedCount() {
        final PhotoViewerProvider placeProvider = this.placeProvider;
        if (placeProvider == null) {
            return;
        }
        final int selectedCount = placeProvider.getSelectedCount();
        this.photosCounterView.setCount(selectedCount);
        if (selectedCount == 0) {
            this.togglePhotosListView(false, true);
        }
    }
    
    private void updateVideoInfo() {
        final ActionBar actionBar = this.actionBar;
        if (actionBar == null) {
            return;
        }
        final int compressionsCount = this.compressionsCount;
        CharSequence currentSubtitle = null;
        if (compressionsCount == 0) {
            actionBar.setSubtitle(null);
            return;
        }
        final int selectedCompression = this.selectedCompression;
        if (selectedCompression == 0) {
            this.compressItem.setImageResource(2131165896);
        }
        else if (selectedCompression == 1) {
            this.compressItem.setImageResource(2131165897);
        }
        else if (selectedCompression == 2) {
            this.compressItem.setImageResource(2131165898);
        }
        else if (selectedCompression == 3) {
            this.compressItem.setImageResource(2131165899);
        }
        else if (selectedCompression == 4) {
            this.compressItem.setImageResource(2131165895);
        }
        final ImageView compressItem = this.compressItem;
        final StringBuilder sb = new StringBuilder();
        sb.append(LocaleController.getString("AccDescrVideoQuality", 2131558482));
        sb.append(", ");
        sb.append((new String[] { "240", "360", "480", "720", "1080" })[Math.max(0, this.selectedCompression)]);
        compressItem.setContentDescription((CharSequence)sb.toString());
        this.estimatedDuration = (long)Math.ceil((this.videoTimelineView.getRightProgress() - this.videoTimelineView.getLeftProgress()) * this.videoDuration);
        int i;
        int j;
        if (this.compressItem.getTag() != null && this.selectedCompression != this.compressionsCount - 1) {
            final int rotationValue = this.rotationValue;
            if (rotationValue != 90 && rotationValue != 270) {
                i = this.resultWidth;
            }
            else {
                i = this.resultHeight;
            }
            final int rotationValue2 = this.rotationValue;
            if (rotationValue2 != 90 && rotationValue2 != 270) {
                j = this.resultHeight;
            }
            else {
                j = this.resultWidth;
            }
            this.estimatedSize = (int)((this.audioFramesSize + this.videoFramesSize) * (this.estimatedDuration / this.videoDuration));
            final int estimatedSize = this.estimatedSize;
            this.estimatedSize = estimatedSize + estimatedSize / 32768 * 16;
        }
        else {
            final int rotationValue3 = this.rotationValue;
            if (rotationValue3 != 90 && rotationValue3 != 270) {
                i = this.originalWidth;
            }
            else {
                i = this.originalHeight;
            }
            final int rotationValue4 = this.rotationValue;
            if (rotationValue4 != 90 && rotationValue4 != 270) {
                j = this.originalHeight;
            }
            else {
                j = this.originalWidth;
            }
            this.estimatedSize = (int)(this.originalSize * (this.estimatedDuration / this.videoDuration));
        }
        if (this.videoTimelineView.getLeftProgress() == 0.0f) {
            this.startTime = -1L;
        }
        else {
            this.startTime = (long)(this.videoTimelineView.getLeftProgress() * this.videoDuration) * 1000L;
        }
        if (this.videoTimelineView.getRightProgress() == 1.0f) {
            this.endTime = -1L;
        }
        else {
            this.endTime = (long)(this.videoTimelineView.getRightProgress() * this.videoDuration) * 1000L;
        }
        final String format = String.format("%dx%d", i, j);
        final long estimatedDuration = this.estimatedDuration;
        final int k = (int)(estimatedDuration / 1000L / 60L);
        this.currentSubtitle = String.format("%s, %s", format, String.format("%d:%02d, ~%s", k, (int)Math.ceil((double)(estimatedDuration / 1000L)) - k * 60, AndroidUtilities.formatFileSize(this.estimatedSize)));
        final ActionBar actionBar2 = this.actionBar;
        if (!this.muteVideo) {
            currentSubtitle = this.currentSubtitle;
        }
        actionBar2.setSubtitle(currentSubtitle);
    }
    
    private void updateVideoPlayerTime() {
        final VideoPlayer videoPlayer = this.videoPlayer;
        final Integer value = 0;
        String text;
        if (videoPlayer == null) {
            text = String.format("%02d:%02d / %02d:%02d", value, value, value, value);
        }
        else {
            final long currentPosition = videoPlayer.getCurrentPosition();
            long n = 0L;
            long n2 = currentPosition;
            if (currentPosition < 0L) {
                n2 = 0L;
            }
            final long duration = this.videoPlayer.getDuration();
            if (duration >= 0L) {
                n = duration;
            }
            if (n != -9223372036854775807L && n2 != -9223372036854775807L) {
                long n3 = n2;
                long n4 = n;
                if (!this.inPreview) {
                    n3 = n2;
                    n4 = n;
                    if (this.videoTimelineView.getVisibility() == 0) {
                        final long n5 = (long)(n * (this.videoTimelineView.getRightProgress() - this.videoTimelineView.getLeftProgress()));
                        n3 = (long)(n2 - this.videoTimelineView.getLeftProgress() * n5);
                        n4 = n5;
                        if (n3 > n5) {
                            n3 = n5;
                            n4 = n5;
                        }
                    }
                }
                final long n6 = n3 / 1000L;
                final long n7 = n4 / 1000L;
                text = String.format("%02d:%02d / %02d:%02d", n6 / 60L, n6 % 60L, n7 / 60L, n7 % 60L);
            }
            else {
                text = String.format("%02d:%02d / %02d:%02d", value, value, value, value);
            }
        }
        this.videoPlayerTime.setText(text);
    }
    
    private void updateWidthHeightBitrateForCompression() {
        final int compressionsCount = this.compressionsCount;
        if (compressionsCount <= 0) {
            return;
        }
        if (this.selectedCompression >= compressionsCount) {
            this.selectedCompression = compressionsCount - 1;
        }
        final int selectedCompression = this.selectedCompression;
        if (selectedCompression != this.compressionsCount - 1) {
            int a;
            float n;
            if (selectedCompression != 0) {
                if (selectedCompression != 1) {
                    if (selectedCompression != 2) {
                        a = 2500000;
                        n = 1280.0f;
                    }
                    else {
                        n = 854.0f;
                        a = 1100000;
                    }
                }
                else {
                    n = 640.0f;
                    a = 900000;
                }
            }
            else {
                n = 426.0f;
                a = 400000;
            }
            final int originalWidth = this.originalWidth;
            final int originalHeight = this.originalHeight;
            float n2;
            if (originalWidth > originalHeight) {
                n2 = (float)originalWidth;
            }
            else {
                n2 = (float)originalHeight;
            }
            final float n3 = n / n2;
            this.resultWidth = Math.round(this.originalWidth * n3 / 2.0f) * 2;
            this.resultHeight = Math.round(this.originalHeight * n3 / 2.0f) * 2;
            if (this.bitrate != 0) {
                this.bitrate = Math.min(a, (int)(this.originalBitrate / n3));
                this.videoFramesSize = (long)(this.bitrate / 8 * this.videoDuration / 1000.0f);
            }
        }
    }
    
    public void closePhoto(final boolean b, final boolean b2) {
        final int n = 3;
        if (!b2) {
            final int currentEditMode = this.currentEditMode;
            if (currentEditMode != 0) {
                if (currentEditMode == 3) {
                    final PhotoPaintView photoPaintView = this.photoPaintView;
                    if (photoPaintView != null) {
                        photoPaintView.maybeShowDismissalAlert(this, this.parentActivity, new _$$Lambda$PhotoViewer$KYtFxmNpM70wviBQal0FRLyYs_s(this));
                        return;
                    }
                }
                this.switchToEditMode(0);
                return;
            }
        }
        final QualityChooseView qualityChooseView = this.qualityChooseView;
        if (qualityChooseView != null && qualityChooseView.getTag() != null) {
            this.qualityPicker.cancelButton.callOnClick();
            return;
        }
        this.openedFullScreenVideo = false;
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        if (Build$VERSION.SDK_INT >= 21 && this.actionBar != null) {
            final int n2 = this.containerView.getSystemUiVisibility() & 0x1006;
            if (n2 != 0) {
                final FrameLayoutDrawer containerView = this.containerView;
                containerView.setSystemUiVisibility(~n2 & containerView.getSystemUiVisibility());
            }
        }
        final int currentEditMode2 = this.currentEditMode;
        if (currentEditMode2 != 0) {
            if (currentEditMode2 == 2) {
                this.photoFilterView.shutdown();
                this.containerView.removeView((View)this.photoFilterView);
                this.photoFilterView = null;
            }
            else if (currentEditMode2 == 1) {
                this.editorDoneLayout.setVisibility(8);
                this.photoCropView.setVisibility(8);
            }
            else if (currentEditMode2 == 3) {
                this.photoPaintView.shutdown();
                this.containerView.removeView((View)this.photoPaintView);
                this.photoPaintView = null;
            }
            this.currentEditMode = 0;
        }
        else if (this.sendPhotoType == 1) {
            this.photoCropView.setVisibility(8);
        }
        if (this.parentActivity != null && (this.isInline || this.isVisible) && !this.checkAnimation()) {
            if (this.placeProvider != null) {
                if (this.captionEditText.hideActionMode() && !b2) {
                    return;
                }
                final PlaceProviderObject placeForPhoto = this.placeProvider.getPlaceForPhoto(this.currentMessageObject, this.getFileLocation(this.currentFileLocation), this.currentIndex, true);
                if (this.videoPlayer != null && placeForPhoto != null) {
                    final AnimatedFileDrawable animation = placeForPhoto.imageReceiver.getAnimation();
                    if (animation != null) {
                        if (this.textureUploaded) {
                            final Bitmap animatedBitmap = animation.getAnimatedBitmap();
                            if (animatedBitmap != null) {
                                try {
                                    final Bitmap bitmap = this.videoTextureView.getBitmap(animatedBitmap.getWidth(), animatedBitmap.getHeight());
                                    new Canvas(animatedBitmap).drawBitmap(bitmap, 0.0f, 0.0f, (Paint)null);
                                    bitmap.recycle();
                                }
                                catch (Throwable t) {
                                    FileLog.e(t);
                                }
                            }
                        }
                        animation.seekTo(this.videoPlayer.getCurrentPosition(), FileLoader.getInstance(this.currentMessageObject.currentAccount).isLoadingVideo(this.currentMessageObject.getDocument(), true) ^ true);
                        placeForPhoto.imageReceiver.setAllowStartAnimation(true);
                        placeForPhoto.imageReceiver.startAnimation();
                    }
                }
                this.releasePlayer(true);
                this.captionEditText.onDestroy();
                this.parentChatActivity = null;
                this.removeObservers();
                this.isActionBarVisible = false;
                final VelocityTracker velocityTracker = this.velocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    this.velocityTracker = null;
                }
                if (this.isInline) {
                    this.isInline = false;
                    this.animationInProgress = 0;
                    this.onPhotoClosed(placeForPhoto);
                    this.containerView.setScaleX(1.0f);
                    this.containerView.setScaleY(1.0f);
                }
                else {
                    if (b) {
                        this.animationInProgress = 1;
                        this.animatingImageView.setVisibility(0);
                        this.containerView.invalidate();
                        final AnimatorSet set = new AnimatorSet();
                        final ViewGroup$LayoutParams layoutParams = this.animatingImageView.getLayoutParams();
                        RectF drawRegion;
                        if (placeForPhoto != null) {
                            this.animatingImageView.setNeedRadius(placeForPhoto.radius != 0);
                            drawRegion = placeForPhoto.imageReceiver.getDrawRegion();
                            layoutParams.width = (int)drawRegion.width();
                            layoutParams.height = (int)drawRegion.height();
                            int orientation = placeForPhoto.imageReceiver.getOrientation();
                            final int animatedOrientation = placeForPhoto.imageReceiver.getAnimatedOrientation();
                            if (animatedOrientation != 0) {
                                orientation = animatedOrientation;
                            }
                            this.animatingImageView.setOrientation(orientation);
                            this.animatingImageView.setImageBitmap(placeForPhoto.thumb);
                        }
                        else {
                            this.animatingImageView.setNeedRadius(false);
                            layoutParams.width = this.centerImage.getImageWidth();
                            layoutParams.height = this.centerImage.getImageHeight();
                            this.animatingImageView.setOrientation(this.centerImage.getOrientation());
                            this.animatingImageView.setImageBitmap(this.centerImage.getBitmapSafe());
                            drawRegion = null;
                        }
                        this.animatingImageView.setLayoutParams(layoutParams);
                        final float n3 = this.windowView.getMeasuredWidth() / (float)layoutParams.width;
                        final int y = AndroidUtilities.displaySize.y;
                        int statusBarHeight;
                        if (Build$VERSION.SDK_INT >= 21) {
                            statusBarHeight = AndroidUtilities.statusBarHeight;
                        }
                        else {
                            statusBarHeight = 0;
                        }
                        final float n4 = (y + statusBarHeight) / (float)layoutParams.height;
                        float n5 = n3;
                        if (n3 > n4) {
                            n5 = n4;
                        }
                        final float n6 = (float)layoutParams.width;
                        final float scale = this.scale;
                        final float n7 = (float)layoutParams.height;
                        final float n8 = (this.windowView.getMeasuredWidth() - n6 * scale * n5) / 2.0f;
                        final int y2 = AndroidUtilities.displaySize.y;
                        int statusBarHeight2;
                        if (Build$VERSION.SDK_INT >= 21) {
                            statusBarHeight2 = AndroidUtilities.statusBarHeight;
                        }
                        else {
                            statusBarHeight2 = 0;
                        }
                        final float n9 = (y2 + statusBarHeight2 - n7 * scale * n5) / 2.0f;
                        this.animatingImageView.setTranslationX(n8 + this.translationX);
                        this.animatingImageView.setTranslationY(n9 + this.translationY);
                        this.animatingImageView.setScaleX(this.scale * n5);
                        this.animatingImageView.setScaleY(this.scale * n5);
                        if (placeForPhoto != null) {
                            placeForPhoto.imageReceiver.setVisible(false, true);
                            final int n10 = (int)Math.abs(drawRegion.left - placeForPhoto.imageReceiver.getImageX());
                            final int n11 = (int)Math.abs(drawRegion.top - placeForPhoto.imageReceiver.getImageY());
                            final int[] array = new int[2];
                            placeForPhoto.parentView.getLocationInWindow(array);
                            final int n12 = array[1];
                            int statusBarHeight3;
                            if (Build$VERSION.SDK_INT >= 21) {
                                statusBarHeight3 = 0;
                            }
                            else {
                                statusBarHeight3 = AndroidUtilities.statusBarHeight;
                            }
                            int a;
                            if ((a = (int)(n12 - statusBarHeight3 - (placeForPhoto.viewY + drawRegion.top) + placeForPhoto.clipTopAddition)) < 0) {
                                a = 0;
                            }
                            final float n13 = (float)placeForPhoto.viewY;
                            final float top = drawRegion.top;
                            final float bottom = drawRegion.bottom;
                            final int n14 = array[1];
                            final int height = placeForPhoto.parentView.getHeight();
                            int statusBarHeight4;
                            if (Build$VERSION.SDK_INT >= 21) {
                                statusBarHeight4 = 0;
                            }
                            else {
                                statusBarHeight4 = AndroidUtilities.statusBarHeight;
                            }
                            int a2;
                            if ((a2 = (int)(n13 + top + (bottom - top) - (n14 + height - statusBarHeight4) + placeForPhoto.clipBottomAddition)) < 0) {
                                a2 = 0;
                            }
                            final int max = Math.max(a, n11);
                            final int max2 = Math.max(a2, n11);
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
                            final float n15 = (float)n10;
                            array3[4] = n15 * scale2;
                            animationValues[1][5] = max * scale2;
                            animationValues[1][6] = max2 * scale2;
                            animationValues[1][7] = (float)placeForPhoto.radius;
                            animationValues[1][8] = n11 * scale2;
                            animationValues[1][9] = n15 * scale2;
                            int initialCapacity = n;
                            if (this.sendPhotoType == 1) {
                                initialCapacity = 4;
                            }
                            final ArrayList list = new ArrayList<ObjectAnimator>(initialCapacity);
                            list.add(ObjectAnimator.ofFloat((Object)this.animatingImageView, (Property)AnimationProperties.CLIPPING_IMAGE_VIEW_PROGRESS, new float[] { 0.0f, 1.0f }));
                            list.add(ObjectAnimator.ofInt((Object)this.backgroundDrawable, (Property)AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[] { 0 }));
                            list.add(ObjectAnimator.ofFloat((Object)this.containerView, View.ALPHA, new float[] { 0.0f }));
                            if (this.sendPhotoType == 1) {
                                list.add(ObjectAnimator.ofFloat((Object)this.photoCropView, View.ALPHA, new float[] { 0.0f }));
                            }
                            set.playTogether((Collection)list);
                        }
                        else {
                            final int y3 = AndroidUtilities.displaySize.y;
                            int statusBarHeight5;
                            if (Build$VERSION.SDK_INT >= 21) {
                                statusBarHeight5 = AndroidUtilities.statusBarHeight;
                            }
                            else {
                                statusBarHeight5 = 0;
                            }
                            int n16 = y3 + statusBarHeight5;
                            final ObjectAnimator ofInt = ObjectAnimator.ofInt((Object)this.backgroundDrawable, (Property)AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[] { 0 });
                            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.animatingImageView, View.ALPHA, new float[] { 0.0f });
                            final ClippingImageView animatingImageView = this.animatingImageView;
                            final Property translation_Y = View.TRANSLATION_Y;
                            if (this.translationY < 0.0f) {
                                n16 = -n16;
                            }
                            set.playTogether(new Animator[] { (Animator)ofInt, (Animator)ofFloat, (Animator)ObjectAnimator.ofFloat((Object)animatingImageView, translation_Y, new float[] { (float)n16 }), (Animator)ObjectAnimator.ofFloat((Object)this.containerView, View.ALPHA, new float[] { 0.0f }) });
                        }
                        this.animationEndRunnable = new _$$Lambda$PhotoViewer$SQuc4TyZ0dWDiWuazMyrUv4byaU(this, placeForPhoto);
                        set.setDuration(200L);
                        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator animator) {
                                AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$37$riPaWbwqB97e1xvF_frnJnVZJ9M(this));
                            }
                        });
                        this.transitionAnimationStartTime = System.currentTimeMillis();
                        if (Build$VERSION.SDK_INT >= 18) {
                            this.containerView.setLayerType(2, (Paint)null);
                        }
                        set.start();
                    }
                    else {
                        final AnimatorSet set2 = new AnimatorSet();
                        set2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.containerView, View.SCALE_X, new float[] { 0.9f }), (Animator)ObjectAnimator.ofFloat((Object)this.containerView, View.SCALE_Y, new float[] { 0.9f }), (Animator)ObjectAnimator.ofInt((Object)this.backgroundDrawable, (Property)AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[] { 0 }), (Animator)ObjectAnimator.ofFloat((Object)this.containerView, View.ALPHA, new float[] { 0.0f }) });
                        this.animationInProgress = 2;
                        this.animationEndRunnable = new _$$Lambda$PhotoViewer$mbE9dgLgiv_6p2JdgFOuRdnxess(this, placeForPhoto);
                        set2.setDuration(200L);
                        set2.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator animator) {
                                if (PhotoViewer.this.animationEndRunnable != null) {
                                    PhotoViewer.this.animationEndRunnable.run();
                                    PhotoViewer.this.animationEndRunnable = null;
                                }
                            }
                        });
                        this.transitionAnimationStartTime = System.currentTimeMillis();
                        if (Build$VERSION.SDK_INT >= 18) {
                            this.containerView.setLayerType(2, (Paint)null);
                        }
                        set2.start();
                    }
                    final AnimatedFileDrawable currentAnimation = this.currentAnimation;
                    if (currentAnimation != null) {
                        currentAnimation.setSecondParentView(null);
                        this.currentAnimation = null;
                        this.centerImage.setImageBitmap((Drawable)null);
                    }
                    final PhotoViewerProvider placeProvider = this.placeProvider;
                    if (placeProvider != null && !placeProvider.canScrollAway()) {
                        this.placeProvider.cancelButtonPressed();
                    }
                }
            }
        }
    }
    
    public void destroyPhotoViewer() {
        if (this.parentActivity != null) {
            if (this.windowView != null) {
                final PipVideoView pipVideoView = this.pipVideoView;
                if (pipVideoView != null) {
                    pipVideoView.close();
                    this.pipVideoView = null;
                }
                this.removeObservers();
                this.releasePlayer(false);
                try {
                    if (this.windowView.getParent() != null) {
                        ((WindowManager)this.parentActivity.getSystemService("window")).removeViewImmediate((View)this.windowView);
                    }
                    this.windowView = null;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                final ImageReceiver.BitmapHolder currentThumb = this.currentThumb;
                if (currentThumb != null) {
                    currentThumb.release();
                    this.currentThumb = null;
                }
                this.animatingImageView.setImageBitmap(null);
                final PhotoViewerCaptionEnterView captionEditText = this.captionEditText;
                if (captionEditText != null) {
                    captionEditText.onDestroy();
                }
                if (this == PhotoViewer.PipInstance) {
                    PhotoViewer.PipInstance = null;
                }
                else {
                    PhotoViewer.Instance = null;
                }
            }
        }
    }
    
    @Override
    public void didReceivedNotification(int i, int j, final Object... array) {
        final int fileDidFailedLoad = NotificationCenter.fileDidFailedLoad;
        j = 0;
        if (i == fileDidFailedLoad) {
            final String anObject = (String)array[0];
            String[] currentFileNames;
            for (i = j; i < 3; ++i) {
                currentFileNames = this.currentFileNames;
                if (currentFileNames[i] != null && currentFileNames[i].equals(anObject)) {
                    this.photoProgressViews[i].setProgress(1.0f, true);
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
                    this.photoProgressViews[i].setProgress(1.0f, true);
                    this.checkProgress(i, true);
                    Label_0206: {
                        if (this.videoPlayer == null && i == 0) {
                            final MessageObject currentMessageObject = this.currentMessageObject;
                            if (currentMessageObject == null || !currentMessageObject.isVideo()) {
                                final TLRPC.BotInlineResult currentBotInlineResult = this.currentBotInlineResult;
                                if (currentBotInlineResult == null || (!currentBotInlineResult.type.equals("video") && !MessageObject.isVideoDocument(this.currentBotInlineResult.document))) {
                                    break Label_0206;
                                }
                            }
                            this.onActionClick(false);
                        }
                    }
                    if (i == 0 && this.videoPlayer != null) {
                        this.currentVideoFinishedLoading = true;
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
            i = 0;
            while (true) {
                float bufferedProgressFromPosition = 1.0f;
                if (i >= 3) {
                    break;
                }
                final String[] currentFileNames3 = this.currentFileNames;
                if (currentFileNames3[i] != null && currentFileNames3[i].equals(anObject3)) {
                    final Float n = (Float)array[1];
                    this.photoProgressViews[i].setProgress(n, true);
                    if (i == 0 && this.videoPlayer != null && this.videoPlayerSeekbar != null) {
                        if (!this.currentVideoFinishedLoading) {
                            final long elapsedRealtime = SystemClock.elapsedRealtime();
                            if (Math.abs(elapsedRealtime - this.lastBufferedPositionCheck) >= 500L) {
                                float seekToProgressPending;
                                if ((seekToProgressPending = this.seekToProgressPending) == 0.0f) {
                                    final long duration = this.videoPlayer.getDuration();
                                    final long currentPosition = this.videoPlayer.getCurrentPosition();
                                    if (duration >= 0L && duration != -9223372036854775807L && currentPosition >= 0L) {
                                        seekToProgressPending = currentPosition / (float)duration;
                                    }
                                    else {
                                        seekToProgressPending = 0.0f;
                                    }
                                }
                                if (this.isStreaming) {
                                    bufferedProgressFromPosition = FileLoader.getInstance(this.currentAccount).getBufferedProgressFromPosition(seekToProgressPending, this.currentFileNames[0]);
                                }
                                else {
                                    bufferedProgressFromPosition = 1.0f;
                                }
                                this.lastBufferedPositionCheck = elapsedRealtime;
                            }
                            else {
                                bufferedProgressFromPosition = -1.0f;
                            }
                        }
                        if (bufferedProgressFromPosition != -1.0f) {
                            this.videoPlayerSeekbar.setBufferedProgress(bufferedProgressFromPosition);
                            final PipVideoView pipVideoView = this.pipVideoView;
                            if (pipVideoView != null) {
                                pipVideoView.setBufferedProgress(bufferedProgressFromPosition);
                            }
                            this.videoPlayerControlFrameLayout.invalidate();
                        }
                        this.checkBufferedProgress(n);
                    }
                }
                ++i;
            }
        }
        else if (i == NotificationCenter.dialogPhotosLoaded) {
            j = (int)array[3];
            i = (int)array[0];
            if (this.avatarsDialogId == i && this.classGuid == j) {
                final boolean booleanValue = (boolean)array[2];
                final ArrayList list = (ArrayList)array[4];
                if (list.isEmpty()) {
                    return;
                }
                this.imagesArrLocations.clear();
                this.imagesArrLocationsSizes.clear();
                this.avatarsArr.clear();
                int k = 0;
                i = -1;
                while (k < list.size()) {
                    final TLRPC.Photo e = list.get(k);
                    int n2 = i;
                    if (e != null) {
                        n2 = i;
                        if (!(e instanceof TLRPC.TL_photoEmpty)) {
                            final ArrayList<TLRPC.PhotoSize> sizes = e.sizes;
                            if (sizes == null) {
                                n2 = i;
                            }
                            else {
                                final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(sizes, 640);
                                n2 = i;
                                if (closestPhotoSizeWithSize != null) {
                                    if ((j = i) == -1) {
                                        j = i;
                                        if (this.currentFileLocation != null) {
                                            int index = 0;
                                            while (true) {
                                                j = i;
                                                if (index >= e.sizes.size()) {
                                                    break;
                                                }
                                                final TLRPC.FileLocation location = e.sizes.get(index).location;
                                                j = location.local_id;
                                                final TLRPC.TL_fileLocationToBeDeprecated location2 = this.currentFileLocation.location;
                                                if (j == location2.local_id && location.volume_id == location2.volume_id) {
                                                    j = this.imagesArrLocations.size();
                                                    break;
                                                }
                                                ++index;
                                            }
                                        }
                                    }
                                    i = e.dc_id;
                                    if (i != 0) {
                                        final TLRPC.FileLocation location3 = closestPhotoSizeWithSize.location;
                                        location3.dc_id = i;
                                        location3.file_reference = e.file_reference;
                                    }
                                    final ImageLocation forPhoto = ImageLocation.getForPhoto(closestPhotoSizeWithSize, e);
                                    n2 = j;
                                    if (forPhoto != null) {
                                        this.imagesArrLocations.add(forPhoto);
                                        this.imagesArrLocationsSizes.add(closestPhotoSizeWithSize.size);
                                        this.avatarsArr.add(e);
                                        n2 = j;
                                    }
                                }
                            }
                        }
                    }
                    ++k;
                    i = n2;
                }
                if (!this.avatarsArr.isEmpty()) {
                    this.menuItem.showSubItem(6);
                }
                else {
                    this.menuItem.hideSubItem(6);
                }
                this.needSearchImageInArr = false;
                this.currentIndex = -1;
                if (i != -1) {
                    this.setImageIndex(i, true);
                }
                else {
                    i = this.avatarsDialogId;
                    TLRPC.Chat chat = null;
                    TLRPC.User user;
                    if (i > 0) {
                        user = MessagesController.getInstance(this.currentAccount).getUser(this.avatarsDialogId);
                    }
                    else {
                        chat = MessagesController.getInstance(this.currentAccount).getChat(-this.avatarsDialogId);
                        user = null;
                    }
                    if (user != null || chat != null) {
                        ImageLocation element;
                        if (user != null) {
                            element = ImageLocation.getForUser(user, true);
                        }
                        else {
                            element = ImageLocation.getForChat(chat, true);
                        }
                        if (element != null) {
                            this.imagesArrLocations.add(0, element);
                            this.avatarsArr.add(0, new TLRPC.TL_photoEmpty());
                            this.imagesArrLocationsSizes.add(0, 0);
                            this.setImageIndex(0, true);
                        }
                    }
                }
                if (booleanValue) {
                    MessagesController.getInstance(this.currentAccount).loadDialogPhotos(this.avatarsDialogId, 80, 0L, false, this.classGuid);
                }
            }
        }
        else if (i == NotificationCenter.mediaCountDidLoad) {
            final long longValue = (long)array[0];
            if (longValue == this.currentDialogId || longValue == this.mergeDialogId) {
                if (longValue == this.currentDialogId) {
                    this.totalImagesCount = (int)array[1];
                }
                else if (longValue == this.mergeDialogId) {
                    this.totalImagesCountMerge = (int)array[1];
                }
                if (this.needSearchImageInArr && this.isFirstLoading) {
                    this.isFirstLoading = false;
                    this.loadingMoreImages = true;
                    DataQuery.getInstance(this.currentAccount).loadMedia(this.currentDialogId, 80, 0, this.sharedMediaType, 1, this.classGuid);
                }
                else if (!this.imagesArr.isEmpty()) {
                    if (this.opennedFromMedia) {
                        this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.currentIndex + 1, this.totalImagesCount + this.totalImagesCountMerge));
                    }
                    else {
                        this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, this.totalImagesCount + this.totalImagesCountMerge - this.imagesArr.size() + this.currentIndex + 1, this.totalImagesCount + this.totalImagesCountMerge));
                    }
                }
            }
        }
        else if (i == NotificationCenter.mediaDidLoad) {
            final long longValue2 = (long)array[0];
            i = (int)array[3];
            if ((longValue2 == this.currentDialogId || longValue2 == this.mergeDialogId) && i == this.classGuid) {
                this.loadingMoreImages = false;
                if (longValue2 == this.currentDialogId) {
                    j = 0;
                }
                else {
                    j = 1;
                }
                final ArrayList list2 = (ArrayList)array[2];
                this.endReached[j] = (boolean)array[5];
                if (this.needSearchImageInArr) {
                    if (list2.isEmpty() && (j != 0 || this.mergeDialogId == 0L)) {
                        this.needSearchImageInArr = false;
                        return;
                    }
                    final MessageObject messageObject = this.imagesArr.get(this.currentIndex);
                    int l = 0;
                    int n3 = 0;
                    i = -1;
                    while (l < list2.size()) {
                        final MessageObject messageObject2 = list2.get(l);
                        int n4 = n3;
                        int n5 = i;
                        if (this.imagesByIdsTemp[j].indexOfKey(messageObject2.getId()) < 0) {
                            this.imagesByIdsTemp[j].put(messageObject2.getId(), (Object)messageObject2);
                            if (this.opennedFromMedia) {
                                this.imagesArrTemp.add(messageObject2);
                                if (messageObject2.getId() == messageObject.getId()) {
                                    i = n3;
                                }
                                n4 = n3 + 1;
                                n5 = i;
                            }
                            else {
                                ++n3;
                                this.imagesArrTemp.add(0, messageObject2);
                                n4 = n3;
                                n5 = i;
                                if (messageObject2.getId() == messageObject.getId()) {
                                    n5 = list2.size() - n3;
                                    n4 = n3;
                                }
                            }
                        }
                        ++l;
                        n3 = n4;
                        i = n5;
                    }
                    if (n3 == 0 && (j != 0 || this.mergeDialogId == 0L)) {
                        this.totalImagesCount = this.imagesArr.size();
                        this.totalImagesCountMerge = 0;
                    }
                    if (i != -1) {
                        this.imagesArr.clear();
                        this.imagesArr.addAll(this.imagesArrTemp);
                        for (j = 0; j < 2; ++j) {
                            this.imagesByIds[j] = (SparseArray<MessageObject>)this.imagesByIdsTemp[j].clone();
                            this.imagesByIdsTemp[j].clear();
                        }
                        this.imagesArrTemp.clear();
                        this.needSearchImageInArr = false;
                        this.currentIndex = -1;
                        if ((j = i) >= this.imagesArr.size()) {
                            j = this.imagesArr.size() - 1;
                        }
                        this.setImageIndex(j, true);
                    }
                    else {
                        Label_2088: {
                            int n6 = 0;
                            Label_2085: {
                                Label_2078: {
                                    if (this.opennedFromMedia) {
                                        if (this.imagesArrTemp.isEmpty()) {
                                            i = 0;
                                        }
                                        else {
                                            final ArrayList<MessageObject> imagesArrTemp = this.imagesArrTemp;
                                            i = imagesArrTemp.get(imagesArrTemp.size() - 1).getId();
                                        }
                                        n6 = i;
                                        if (j != 0) {
                                            break Label_2085;
                                        }
                                        n6 = i;
                                        if (!this.endReached[j]) {
                                            break Label_2085;
                                        }
                                        n6 = i;
                                        if (this.mergeDialogId == 0L) {
                                            break Label_2085;
                                        }
                                        j = i;
                                        if (this.imagesArrTemp.isEmpty()) {
                                            break Label_2078;
                                        }
                                        final ArrayList<MessageObject> imagesArrTemp2 = this.imagesArrTemp;
                                        j = i;
                                        if (imagesArrTemp2.get(imagesArrTemp2.size() - 1).getDialogId() == this.mergeDialogId) {
                                            break Label_2078;
                                        }
                                    }
                                    else {
                                        if (this.imagesArrTemp.isEmpty()) {
                                            i = 0;
                                        }
                                        else {
                                            i = this.imagesArrTemp.get(0).getId();
                                        }
                                        n6 = i;
                                        if (j != 0) {
                                            break Label_2085;
                                        }
                                        n6 = i;
                                        if (!this.endReached[j]) {
                                            break Label_2085;
                                        }
                                        n6 = i;
                                        if (this.mergeDialogId == 0L) {
                                            break Label_2085;
                                        }
                                        j = i;
                                        if (this.imagesArrTemp.isEmpty()) {
                                            break Label_2078;
                                        }
                                        j = i;
                                        if (this.imagesArrTemp.get(0).getDialogId() == this.mergeDialogId) {
                                            break Label_2078;
                                        }
                                    }
                                    j = 1;
                                    i = 0;
                                    break Label_2088;
                                }
                                i = j;
                                j = 1;
                                break Label_2088;
                            }
                            i = n6;
                        }
                        if (!this.endReached[j]) {
                            this.loadingMoreImages = true;
                            if (this.opennedFromMedia) {
                                final DataQuery instance = DataQuery.getInstance(this.currentAccount);
                                long n7;
                                if (j == 0) {
                                    n7 = this.currentDialogId;
                                }
                                else {
                                    n7 = this.mergeDialogId;
                                }
                                instance.loadMedia(n7, 80, i, this.sharedMediaType, 1, this.classGuid);
                            }
                            else {
                                final DataQuery instance2 = DataQuery.getInstance(this.currentAccount);
                                long n8;
                                if (j == 0) {
                                    n8 = this.currentDialogId;
                                }
                                else {
                                    n8 = this.mergeDialogId;
                                }
                                instance2.loadMedia(n8, 80, i, this.sharedMediaType, 1, this.classGuid);
                            }
                        }
                    }
                }
                else {
                    final Iterator<MessageObject> iterator = list2.iterator();
                    i = 0;
                    while (iterator.hasNext()) {
                        final MessageObject messageObject3 = iterator.next();
                        if (this.imagesByIds[j].indexOfKey(messageObject3.getId()) < 0) {
                            ++i;
                            if (this.opennedFromMedia) {
                                this.imagesArr.add(messageObject3);
                            }
                            else {
                                this.imagesArr.add(0, messageObject3);
                            }
                            this.imagesByIds[j].put(messageObject3.getId(), (Object)messageObject3);
                        }
                    }
                    if (this.opennedFromMedia) {
                        if (i == 0) {
                            this.totalImagesCount = this.imagesArr.size();
                            this.totalImagesCountMerge = 0;
                        }
                    }
                    else if (i != 0) {
                        j = this.currentIndex;
                        this.currentIndex = -1;
                        this.setImageIndex(j + i, true);
                    }
                    else {
                        this.totalImagesCount = this.imagesArr.size();
                        this.totalImagesCountMerge = 0;
                    }
                }
            }
        }
        else if (i == NotificationCenter.emojiDidLoad) {
            final TextView captionTextView = this.captionTextView;
            if (captionTextView != null) {
                captionTextView.invalidate();
            }
        }
        else if (i == NotificationCenter.filePreparingFailed) {
            final MessageObject messageObject4 = (MessageObject)array[0];
            if (this.loadInitialVideo) {
                this.loadInitialVideo = false;
                this.progressView.setVisibility(4);
                this.preparePlayer(this.currentPlayingVideoFile, false, false);
            }
            else if (this.tryStartRequestPreviewOnFinish) {
                this.releasePlayer(false);
                this.tryStartRequestPreviewOnFinish = (MediaController.getInstance().scheduleVideoConvert(this.videoPreviewMessageObject, true) ^ true);
            }
            else if (messageObject4 == this.videoPreviewMessageObject) {
                this.requestingPreview = false;
                this.progressView.setVisibility(4);
            }
        }
        else if (i == NotificationCenter.fileNewChunkAvailable && array[0] == this.videoPreviewMessageObject) {
            final String pathname = (String)array[1];
            if ((long)array[3] != 0L) {
                this.requestingPreview = false;
                this.progressView.setVisibility(4);
                this.preparePlayer(Uri.fromFile(new File(pathname)), false, true);
            }
        }
    }
    
    public void exitFromPip() {
        if (!this.isInline) {
            return;
        }
        if (PhotoViewer.Instance != null) {
            PhotoViewer.Instance.closePhoto(false, true);
        }
        PhotoViewer.Instance = PhotoViewer.PipInstance;
        PhotoViewer.PipInstance = null;
        this.switchingInlineMode = true;
        final Bitmap currentBitmap = this.currentBitmap;
        if (currentBitmap != null) {
            currentBitmap.recycle();
            this.currentBitmap = null;
        }
        this.changingTextureView = true;
        this.isInline = false;
        this.videoTextureView.setVisibility(4);
        this.aspectRatioFrameLayout.addView((View)this.videoTextureView);
        if (ApplicationLoader.mainInterfacePaused) {
            try {
                this.parentActivity.startService(new Intent(ApplicationLoader.applicationContext, (Class)BringAppForegroundService.class));
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.pipAnimationInProgress = true;
            final Rect pipRect = PipVideoView.getPipRect(this.aspectRatioFrameLayout.getAspectRatio());
            final float n = pipRect.width / this.textureImageView.getLayoutParams().width;
            pipRect.y += AndroidUtilities.statusBarHeight;
            this.textureImageView.setScaleX(n);
            this.textureImageView.setScaleY(n);
            this.textureImageView.setTranslationX(pipRect.x);
            this.textureImageView.setTranslationY(pipRect.y);
            this.videoTextureView.setScaleX(n);
            this.videoTextureView.setScaleY(n);
            this.videoTextureView.setTranslationX(pipRect.x - this.aspectRatioFrameLayout.getX());
            this.videoTextureView.setTranslationY(pipRect.y - this.aspectRatioFrameLayout.getY());
        }
        else {
            this.pipVideoView.close();
            this.pipVideoView = null;
        }
        try {
            this.isVisible = true;
            ((WindowManager)this.parentActivity.getSystemService("window")).addView((View)this.windowView, (ViewGroup$LayoutParams)this.windowLayoutParams);
            if (this.currentPlaceObject != null) {
                this.currentPlaceObject.imageReceiver.setVisible(false, false);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.waitingForDraw = 4;
        }
    }
    
    @Keep
    public float getAnimationValue() {
        return this.animationValue;
    }
    
    public int getSelectiongLength() {
        final PhotoViewerCaptionEnterView captionEditText = this.captionEditText;
        int selectionLength;
        if (captionEditText != null) {
            selectionLength = captionEditText.getSelectionLength();
        }
        else {
            selectionLength = 0;
        }
        return selectionLength;
    }
    
    public VideoPlayer getVideoPlayer() {
        return this.videoPlayer;
    }
    
    public void injectVideoPlayer(final VideoPlayer injectingVideoPlayer) {
        this.injectingVideoPlayer = injectingVideoPlayer;
    }
    
    public void injectVideoPlayerSurface(final SurfaceTexture injectingVideoPlayerSurface) {
        this.injectingVideoPlayerSurface = injectingVideoPlayerSurface;
    }
    
    public void injectVideoPlayerToMediaController() {
        if (this.videoPlayer.isPlaying()) {
            MediaController.getInstance().injectVideoPlayer(this.videoPlayer, this.currentMessageObject);
            this.videoPlayer = null;
            this.updateAccessibilityOverlayVisibility();
        }
    }
    
    public boolean isInjectingVideoPlayer() {
        return this.injectingVideoPlayer != null;
    }
    
    public boolean isMuteVideo() {
        return this.muteVideo;
    }
    
    public boolean isOpenedFullScreenVideo() {
        return this.openedFullScreenVideo;
    }
    
    public boolean isVisible() {
        return this.isVisible && this.placeProvider != null;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        final PipVideoView pipVideoView = this.pipVideoView;
        if (pipVideoView != null) {
            pipVideoView.onConfigurationChanged();
        }
    }
    
    public boolean onDoubleTap(final MotionEvent motionEvent) {
        final VideoPlayer videoPlayer = this.videoPlayer;
        boolean leftSide = false;
        final long n = 0L;
        if (videoPlayer != null && this.videoPlayerControlFrameLayout.getVisibility() == 0) {
            final long currentPosition = this.videoPlayer.getCurrentPosition();
            final long duration = this.videoPlayer.getDuration();
            if (duration >= 0L && currentPosition >= 0L && duration != -9223372036854775807L && currentPosition != -9223372036854775807L) {
                final int containerViewWidth = this.getContainerViewWidth();
                final float x = motionEvent.getX();
                final int n2 = containerViewWidth / 3;
                long n3;
                if (x >= n2 * 2) {
                    n3 = 10000L + currentPosition;
                }
                else if (x < n2) {
                    n3 = currentPosition - 10000L;
                }
                else {
                    n3 = currentPosition;
                }
                if (currentPosition != n3) {
                    if (n3 > duration) {
                        n3 = duration;
                    }
                    else if (n3 < 0L) {
                        n3 = n;
                    }
                    final VideoForwardDrawable videoForwardDrawable = this.videoForwardDrawable;
                    if (x < n2) {
                        leftSide = true;
                    }
                    videoForwardDrawable.setLeftSide(leftSide);
                    this.videoPlayer.seekTo(n3);
                    this.containerView.invalidate();
                    this.videoPlayerSeekbar.setProgress(n3 / (float)duration);
                    this.videoPlayerControlFrameLayout.invalidate();
                    return true;
                }
            }
        }
        if (this.canZoom) {
            if (this.scale == 1.0f) {
                if (this.translationY != 0.0f) {
                    return false;
                }
                if (this.translationX != 0.0f) {
                    return false;
                }
            }
            if (this.animationStartTime == 0L) {
                if (this.animationInProgress == 0) {
                    if (this.scale == 1.0f) {
                        float n4 = motionEvent.getX() - this.getContainerViewWidth() / 2 - (motionEvent.getX() - this.getContainerViewWidth() / 2 - this.translationX) * (3.0f / this.scale);
                        float n5 = motionEvent.getY() - this.getContainerViewHeight() / 2 - (motionEvent.getY() - this.getContainerViewHeight() / 2 - this.translationY) * (3.0f / this.scale);
                        this.updateMinMax(3.0f);
                        final float minX = this.minX;
                        if (n4 < minX) {
                            n4 = minX;
                        }
                        else {
                            final float maxX = this.maxX;
                            if (n4 > maxX) {
                                n4 = maxX;
                            }
                        }
                        final float minY = this.minY;
                        if (n5 < minY) {
                            n5 = minY;
                        }
                        else {
                            final float maxY = this.maxY;
                            if (n5 > maxY) {
                                n5 = maxY;
                            }
                        }
                        this.animateTo(3.0f, n4, n5, true);
                    }
                    else {
                        this.animateTo(1.0f, 0.0f, 0.0f, true);
                    }
                    return this.doubleTap = true;
                }
            }
        }
        return false;
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
            this.containerView.postInvalidate();
        }
        return false;
    }
    
    public void onLongPress(final MotionEvent motionEvent) {
    }
    
    public void onPause() {
        if (this.currentAnimation != null) {
            this.closePhoto(false, false);
            return;
        }
        if (this.lastTitle != null) {
            this.closeCaptionEnter(true);
        }
    }
    
    public void onResume() {
        this.redraw(0);
        final VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.seekTo(videoPlayer.getCurrentPosition() + 1L);
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
        if (this.containerView.getTag() != null) {
            final AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
            final boolean b = aspectRatioFrameLayout != null && aspectRatioFrameLayout.getVisibility() == 0;
            final float x = motionEvent.getX();
            final float y = motionEvent.getY();
            Label_0256: {
                if (this.sharedMediaType == 1) {
                    final MessageObject currentMessageObject = this.currentMessageObject;
                    if (currentMessageObject != null) {
                        if (currentMessageObject.canPreviewDocument()) {
                            break Label_0256;
                        }
                        final float n = (this.getContainerViewHeight() - AndroidUtilities.dp(360.0f)) / 2.0f;
                        if (y >= n && y <= n + AndroidUtilities.dp(360.0f)) {
                            this.onActionClick(true);
                            return true;
                        }
                        break Label_0256;
                    }
                }
                final PhotoProgressView[] photoProgressViews = this.photoProgressViews;
                if (photoProgressViews[0] != null && this.containerView != null && !b) {
                    final int access$11100 = photoProgressViews[0].backgroundState;
                    if (access$11100 > 0 && access$11100 <= 3 && x >= (this.getContainerViewWidth() - AndroidUtilities.dp(100.0f)) / 2.0f && x <= (this.getContainerViewWidth() + AndroidUtilities.dp(100.0f)) / 2.0f && y >= (this.getContainerViewHeight() - AndroidUtilities.dp(100.0f)) / 2.0f && y <= (this.getContainerViewHeight() + AndroidUtilities.dp(100.0f)) / 2.0f) {
                        this.onActionClick(true);
                        this.checkProgress(0, true);
                        return true;
                    }
                }
            }
            this.toggleActionBar(this.isActionBarVisible ^ true, true);
        }
        else {
            final int sendPhotoType = this.sendPhotoType;
            if (sendPhotoType != 0 && sendPhotoType != 4) {
                final TLRPC.BotInlineResult currentBotInlineResult = this.currentBotInlineResult;
                if (currentBotInlineResult != null && (currentBotInlineResult.type.equals("video") || MessageObject.isVideoDocument(this.currentBotInlineResult.document))) {
                    final int access$11101 = this.photoProgressViews[0].backgroundState;
                    if (access$11101 > 0 && access$11101 <= 3) {
                        final float x2 = motionEvent.getX();
                        final float y2 = motionEvent.getY();
                        if (x2 >= (this.getContainerViewWidth() - AndroidUtilities.dp(100.0f)) / 2.0f && x2 <= (this.getContainerViewWidth() + AndroidUtilities.dp(100.0f)) / 2.0f && y2 >= (this.getContainerViewHeight() - AndroidUtilities.dp(100.0f)) / 2.0f && y2 <= (this.getContainerViewHeight() + AndroidUtilities.dp(100.0f)) / 2.0f) {
                            this.onActionClick(true);
                            this.checkProgress(0, true);
                            return true;
                        }
                    }
                }
                else if (this.sendPhotoType == 2 && this.isCurrentVideo) {
                    this.videoPlayButton.callOnClick();
                }
            }
            else if (this.isCurrentVideo) {
                this.videoPlayButton.callOnClick();
            }
            else {
                this.checkImageView.performClick();
            }
        }
        return true;
    }
    
    public boolean onSingleTapUp(final MotionEvent motionEvent) {
        return !this.canZoom && !this.doubleTapEnabled && this.onSingleTapConfirmed(motionEvent);
    }
    
    public boolean openPhoto(final ArrayList<MessageObject> list, final int index, final long n, final long n2, final PhotoViewerProvider photoViewerProvider) {
        return this.openPhoto(list.get(index), null, list, null, null, index, photoViewerProvider, null, n, n2, true);
    }
    
    public boolean openPhoto(final ArrayList<SecureDocument> list, final int n, final PhotoViewerProvider photoViewerProvider) {
        return this.openPhoto(null, null, null, list, null, n, photoViewerProvider, null, 0L, 0L, true);
    }
    
    public boolean openPhoto(final MessageObject messageObject, final long n, final long n2, final PhotoViewerProvider photoViewerProvider) {
        return this.openPhoto(messageObject, null, null, null, null, 0, photoViewerProvider, null, n, n2, true);
    }
    
    public boolean openPhoto(final MessageObject messageObject, final long n, final long n2, final PhotoViewerProvider photoViewerProvider, final boolean b) {
        return this.openPhoto(messageObject, null, null, null, null, 0, photoViewerProvider, null, n, n2, b);
    }
    
    public boolean openPhoto(final MessageObject ex, TLRPC.FileLocation fileLocation, final ArrayList<MessageObject> list, final ArrayList<SecureDocument> list2, final ArrayList<Object> list3, final int n, PhotoViewerProvider windowLayoutParams, final ChatActivity parentChatActivity, final long currentDialogId, final long mergeDialogId, boolean skipFirstBufferingProgress) {
        if (this.parentActivity == null || this.isVisible || (windowLayoutParams == null && this.checkAnimation())) {
            return false;
        }
        if (ex == null && fileLocation == null && list == null && list3 == null && list2 == null) {
            return false;
        }
        final PlaceProviderObject placeForPhoto = windowLayoutParams.getPlaceForPhoto((MessageObject)ex, fileLocation, n, true);
        if (placeForPhoto == null && list3 == null) {
            return false;
        }
        this.lastInsets = null;
        final WindowManager windowManager = (WindowManager)this.parentActivity.getSystemService("window");
        while (true) {
            if (!this.attachedToWindow) {
                break Label_0115;
            }
            try {
                windowManager.removeView((View)this.windowView);
                try {
                    this.windowLayoutParams.type = 99;
                    if (Build$VERSION.SDK_INT >= 21) {
                        this.windowLayoutParams.flags = -2147286784;
                    }
                    else {
                        this.windowLayoutParams.flags = 131072;
                    }
                    this.windowLayoutParams.softInputMode = 272;
                    this.windowView.setFocusable(false);
                    this.containerView.setFocusable(false);
                    windowManager.addView((View)this.windowView, (ViewGroup$LayoutParams)this.windowLayoutParams);
                    this.doneButtonPressed = false;
                    this.parentChatActivity = parentChatActivity;
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
                    this.placeProvider = windowLayoutParams;
                    this.mergeDialogId = mergeDialogId;
                    this.currentDialogId = currentDialogId;
                    this.selectedPhotosAdapter.notifyDataSetChanged();
                    if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                    }
                    this.isVisible = true;
                    this.togglePhotosListView(false, false);
                    this.openedFullScreenVideo = (skipFirstBufferingProgress ^ true);
                    if (this.openedFullScreenVideo) {
                        this.toggleActionBar(false, false);
                    }
                    else if (this.sendPhotoType == 1) {
                        this.createCropView();
                        this.toggleActionBar(false, false);
                    }
                    else {
                        this.toggleActionBar(true, false);
                    }
                    this.seekToProgressPending2 = 0.0f;
                    this.skipFirstBufferingProgress = false;
                    this.playerInjected = false;
                    if (placeForPhoto != null) {
                        this.disableShowCheck = true;
                        this.animationInProgress = 1;
                        if (ex != null) {
                            this.currentAnimation = placeForPhoto.imageReceiver.getAnimation();
                            if (this.currentAnimation != null) {
                                if (((MessageObject)ex).isVideo()) {
                                    placeForPhoto.imageReceiver.setAllowStartAnimation(false);
                                    placeForPhoto.imageReceiver.stopAnimation();
                                    if (MediaController.getInstance().isPlayingMessage((MessageObject)ex)) {
                                        this.seekToProgressPending2 = ((MessageObject)ex).audioProgress;
                                    }
                                    if (this.injectingVideoPlayer == null && !FileLoader.getInstance(((MessageObject)ex).currentAccount).isLoadingVideo(((MessageObject)ex).getDocument(), true) && (this.currentAnimation.hasBitmap() || !FileLoader.getInstance(((MessageObject)ex).currentAccount).isLoadingVideo(((MessageObject)ex).getDocument(), false))) {
                                        skipFirstBufferingProgress = true;
                                    }
                                    else {
                                        skipFirstBufferingProgress = false;
                                    }
                                    this.skipFirstBufferingProgress = skipFirstBufferingProgress;
                                    this.currentAnimation = null;
                                }
                                else if (((MessageObject)ex).getWebPagePhotos(null, null).size() > 1) {
                                    this.currentAnimation = null;
                                }
                            }
                        }
                        this.onPhotoShow((MessageObject)ex, fileLocation, list, list2, list3, n, placeForPhoto);
                        if (this.sendPhotoType == 1) {
                            this.photoCropView.setVisibility(0);
                            this.photoCropView.setAlpha(0.0f);
                            this.photoCropView.setFreeform(false);
                        }
                        this.windowView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                            public boolean onPreDraw() {
                                PhotoViewer.this.windowView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                                final RectF drawRegion = placeForPhoto.imageReceiver.getDrawRegion();
                                int orientation = placeForPhoto.imageReceiver.getOrientation();
                                final int animatedOrientation = placeForPhoto.imageReceiver.getAnimatedOrientation();
                                if (animatedOrientation != 0) {
                                    orientation = animatedOrientation;
                                }
                                PhotoViewer.this.animatingImageView.setVisibility(0);
                                PhotoViewer.this.animatingImageView.setRadius(placeForPhoto.radius);
                                PhotoViewer.this.animatingImageView.setOrientation(orientation);
                                PhotoViewer.this.animatingImageView.setNeedRadius(placeForPhoto.radius != 0);
                                PhotoViewer.this.animatingImageView.setImageBitmap(placeForPhoto.thumb);
                                PhotoViewer.this.initCropView();
                                if (PhotoViewer.this.sendPhotoType == 1) {
                                    PhotoViewer.this.photoCropView.hideBackView();
                                    PhotoViewer.this.photoCropView.setAspectRatio(1.0f);
                                }
                                PhotoViewer.this.animatingImageView.setAlpha(1.0f);
                                PhotoViewer.this.animatingImageView.setPivotX(0.0f);
                                PhotoViewer.this.animatingImageView.setPivotY(0.0f);
                                PhotoViewer.this.animatingImageView.setScaleX(placeForPhoto.scale);
                                PhotoViewer.this.animatingImageView.setScaleY(placeForPhoto.scale);
                                final ClippingImageView access$5700 = PhotoViewer.this.animatingImageView;
                                final PlaceProviderObject val$object = placeForPhoto;
                                access$5700.setTranslationX(val$object.viewX + drawRegion.left * val$object.scale);
                                final ClippingImageView access$5701 = PhotoViewer.this.animatingImageView;
                                final PlaceProviderObject val$object2 = placeForPhoto;
                                access$5701.setTranslationY(val$object2.viewY + drawRegion.top * val$object2.scale);
                                final ViewGroup$LayoutParams layoutParams = PhotoViewer.this.animatingImageView.getLayoutParams();
                                layoutParams.width = (int)drawRegion.width();
                                layoutParams.height = (int)drawRegion.height();
                                PhotoViewer.this.animatingImageView.setLayoutParams(layoutParams);
                                float max;
                                float n8;
                                float n9;
                                if (PhotoViewer.this.sendPhotoType == 1) {
                                    int statusBarHeight;
                                    if (Build$VERSION.SDK_INT >= 21) {
                                        statusBarHeight = AndroidUtilities.statusBarHeight;
                                    }
                                    else {
                                        statusBarHeight = 0;
                                    }
                                    final float n = (float)statusBarHeight;
                                    final float b = PhotoViewer.this.photoCropView.getMeasuredHeight() - (float)AndroidUtilities.dp(64.0f) - n;
                                    final float min = Math.min((float)PhotoViewer.this.photoCropView.getMeasuredWidth(), b);
                                    final float n2 = (float)(AndroidUtilities.dp(16.0f) * 2);
                                    final float n3 = PhotoViewer.this.photoCropView.getMeasuredWidth() / 2.0f;
                                    final float n4 = n + b / 2.0f;
                                    final float n5 = (min - n2) / 2.0f;
                                    final float n6 = n4 - n5;
                                    final float a = (n3 + n5 - (n3 - n5)) / layoutParams.width;
                                    final float n7 = n4 + n5 - n6;
                                    max = Math.max(a, n7 / layoutParams.height);
                                    n8 = n6 + (n7 - layoutParams.height * max) / 2.0f;
                                    n9 = (PhotoViewer.this.windowView.getMeasuredWidth() - PhotoViewer.this.getLeftInset() - PhotoViewer.this.getRightInset() - layoutParams.width * max) / 2.0f + PhotoViewer.this.getLeftInset();
                                }
                                else {
                                    float n10 = PhotoViewer.this.windowView.getMeasuredWidth() / (float)layoutParams.width;
                                    final int y = AndroidUtilities.displaySize.y;
                                    int statusBarHeight2;
                                    if (Build$VERSION.SDK_INT >= 21) {
                                        statusBarHeight2 = AndroidUtilities.statusBarHeight;
                                    }
                                    else {
                                        statusBarHeight2 = 0;
                                    }
                                    final float n11 = (y + statusBarHeight2) / (float)layoutParams.height;
                                    if (n10 > n11) {
                                        n10 = n11;
                                    }
                                    final int y2 = AndroidUtilities.displaySize.y;
                                    int statusBarHeight3;
                                    if (Build$VERSION.SDK_INT >= 21) {
                                        statusBarHeight3 = AndroidUtilities.statusBarHeight;
                                    }
                                    else {
                                        statusBarHeight3 = 0;
                                    }
                                    n8 = (y2 + statusBarHeight3 - layoutParams.height * n10) / 2.0f;
                                    final float n12 = (PhotoViewer.this.windowView.getMeasuredWidth() - layoutParams.width * n10) / 2.0f;
                                    max = n10;
                                    n9 = n12;
                                }
                                final int n13 = (int)Math.abs(drawRegion.left - placeForPhoto.imageReceiver.getImageX());
                                final int n14 = (int)Math.abs(drawRegion.top - placeForPhoto.imageReceiver.getImageY());
                                final int[] array = new int[2];
                                placeForPhoto.parentView.getLocationInWindow(array);
                                final int n15 = array[1];
                                int statusBarHeight4;
                                if (Build$VERSION.SDK_INT >= 21) {
                                    statusBarHeight4 = 0;
                                }
                                else {
                                    statusBarHeight4 = AndroidUtilities.statusBarHeight;
                                }
                                final float n16 = (float)(n15 - statusBarHeight4);
                                final PlaceProviderObject val$object3 = placeForPhoto;
                                int a2;
                                if ((a2 = (int)(n16 - (val$object3.viewY + drawRegion.top) + val$object3.clipTopAddition)) < 0) {
                                    a2 = 0;
                                }
                                final PlaceProviderObject val$object4 = placeForPhoto;
                                final float n17 = (float)val$object4.viewY;
                                final float top = drawRegion.top;
                                final float n18 = (float)layoutParams.height;
                                final int n19 = array[1];
                                final int height = val$object4.parentView.getHeight();
                                int statusBarHeight5;
                                if (Build$VERSION.SDK_INT >= 21) {
                                    statusBarHeight5 = 0;
                                }
                                else {
                                    statusBarHeight5 = AndroidUtilities.statusBarHeight;
                                }
                                int a3;
                                if ((a3 = (int)(n17 + top + n18 - (n19 + height - statusBarHeight5) + placeForPhoto.clipBottomAddition)) < 0) {
                                    a3 = 0;
                                }
                                final int max2 = Math.max(a2, n14);
                                final int max3 = Math.max(a3, n14);
                                PhotoViewer.this.animationValues[0][0] = PhotoViewer.this.animatingImageView.getScaleX();
                                PhotoViewer.this.animationValues[0][1] = PhotoViewer.this.animatingImageView.getScaleY();
                                PhotoViewer.this.animationValues[0][2] = PhotoViewer.this.animatingImageView.getTranslationX();
                                final float[] array2 = PhotoViewer.this.animationValues[0];
                                final float translationY = PhotoViewer.this.animatingImageView.getTranslationY();
                                int initialCapacity = 3;
                                array2[3] = translationY;
                                final float[] array3 = PhotoViewer.this.animationValues[0];
                                final float n20 = (float)n13;
                                array3[4] = placeForPhoto.scale * n20;
                                PhotoViewer.this.animationValues[0][5] = max2 * placeForPhoto.scale;
                                PhotoViewer.this.animationValues[0][6] = max3 * placeForPhoto.scale;
                                PhotoViewer.this.animationValues[0][7] = (float)PhotoViewer.this.animatingImageView.getRadius();
                                PhotoViewer.this.animationValues[0][8] = n14 * placeForPhoto.scale;
                                PhotoViewer.this.animationValues[0][9] = n20 * placeForPhoto.scale;
                                PhotoViewer.this.animationValues[1][0] = max;
                                PhotoViewer.this.animationValues[1][1] = max;
                                PhotoViewer.this.animationValues[1][2] = n9;
                                PhotoViewer.this.animationValues[1][3] = n8;
                                PhotoViewer.this.animationValues[1][4] = 0.0f;
                                PhotoViewer.this.animationValues[1][5] = 0.0f;
                                PhotoViewer.this.animationValues[1][6] = 0.0f;
                                PhotoViewer.this.animationValues[1][7] = 0.0f;
                                PhotoViewer.this.animationValues[1][8] = 0.0f;
                                PhotoViewer.this.animationValues[1][9] = 0.0f;
                                PhotoViewer.this.animatingImageView.setAnimationProgress(0.0f);
                                PhotoViewer.this.backgroundDrawable.setAlpha(0);
                                PhotoViewer.this.containerView.setAlpha(0.0f);
                                PhotoViewer.this.animationEndRunnable = new _$$Lambda$PhotoViewer$36$6TF1l_RAT6OwuDg5xrfLlfrxKX0(this, list3);
                                if (!PhotoViewer.this.openedFullScreenVideo) {
                                    final AnimatorSet set = new AnimatorSet();
                                    if (PhotoViewer.this.sendPhotoType == 1) {
                                        initialCapacity = 4;
                                    }
                                    final ArrayList list = new ArrayList<ObjectAnimator>(initialCapacity);
                                    list.add(ObjectAnimator.ofFloat((Object)PhotoViewer.this.animatingImageView, (Property)AnimationProperties.CLIPPING_IMAGE_VIEW_PROGRESS, new float[] { 0.0f, 1.0f }));
                                    list.add(ObjectAnimator.ofInt((Object)PhotoViewer.this.backgroundDrawable, (Property)AnimationProperties.COLOR_DRAWABLE_ALPHA, new int[] { 0, 255 }));
                                    list.add(ObjectAnimator.ofFloat((Object)PhotoViewer.this.containerView, View.ALPHA, new float[] { 0.0f, 1.0f }));
                                    if (PhotoViewer.this.sendPhotoType == 1) {
                                        list.add(ObjectAnimator.ofFloat((Object)PhotoViewer.this.photoCropView, View.ALPHA, new float[] { 0.0f, 1.0f }));
                                    }
                                    set.playTogether((Collection)list);
                                    set.setDuration(200L);
                                    set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                                        public void onAnimationEnd(final Animator animator) {
                                            AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$36$1$z2Mdnj_EJcKOldib3qThdRwgaKk(this));
                                        }
                                    });
                                    if (Build$VERSION.SDK_INT >= 18) {
                                        PhotoViewer.this.containerView.setLayerType(2, (Paint)null);
                                    }
                                    PhotoViewer.this.transitionAnimationStartTime = System.currentTimeMillis();
                                    AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$36$THb2N7uCapRChPdPXPsoefivhPs(this, set));
                                }
                                else {
                                    if (PhotoViewer.this.animationEndRunnable != null) {
                                        PhotoViewer.this.animationEndRunnable.run();
                                        PhotoViewer.this.animationEndRunnable = null;
                                    }
                                    PhotoViewer.this.containerView.setAlpha(1.0f);
                                    PhotoViewer.this.backgroundDrawable.setAlpha(255);
                                    PhotoViewer.this.animatingImageView.setAnimationProgress(1.0f);
                                    if (PhotoViewer.this.sendPhotoType == 1) {
                                        PhotoViewer.this.photoCropView.setAlpha(1.0f);
                                    }
                                }
                                PhotoViewer.this.backgroundDrawable.drawRunnable = new _$$Lambda$PhotoViewer$36$21zfYLrtYOpgxSCyhiSDh2RoAK4(this, placeForPhoto);
                                return true;
                            }
                        });
                    }
                    else {
                        if (list3 != null && this.sendPhotoType != 3) {
                            if (Build$VERSION.SDK_INT >= 21) {
                                this.windowLayoutParams.flags = -2147417856;
                            }
                            else {
                                this.windowLayoutParams.flags = 0;
                            }
                            windowLayoutParams = (PhotoViewerProvider)this.windowLayoutParams;
                            ((WindowManager$LayoutParams)windowLayoutParams).softInputMode = 272;
                            windowManager.updateViewLayout((View)this.windowView, (ViewGroup$LayoutParams)windowLayoutParams);
                            this.windowView.setFocusable(true);
                            this.containerView.setFocusable(true);
                        }
                        this.backgroundDrawable.setAlpha(255);
                        this.containerView.setAlpha(1.0f);
                        this.onPhotoShow((MessageObject)ex, fileLocation, list, list2, list3, n, placeForPhoto);
                        this.initCropView();
                        this.setCropBitmap();
                    }
                    fileLocation = (TLRPC.FileLocation)this.parentActivity.getSystemService("accessibility");
                    if (((AccessibilityManager)fileLocation).isTouchExplorationEnabled()) {
                        final AccessibilityEvent obtain = AccessibilityEvent.obtain();
                        obtain.setEventType(16384);
                        obtain.getText().add(LocaleController.getString("AccDescrPhotoViewer", 2131558457));
                        ((AccessibilityManager)fileLocation).sendAccessibilityEvent(obtain);
                    }
                    return true;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                return false;
            }
            catch (Exception ex2) {
                continue;
            }
            break;
        }
    }
    
    public boolean openPhoto(final TLRPC.FileLocation fileLocation, final PhotoViewerProvider photoViewerProvider) {
        return this.openPhoto(null, fileLocation, null, null, null, 0, photoViewerProvider, null, 0L, 0L, true);
    }
    
    public boolean openPhotoForSelect(final ArrayList<Object> list, final int n, int sendPhotoType, final PhotoViewerProvider photoViewerProvider, final ChatActivity chatActivity) {
        this.sendPhotoType = sendPhotoType;
        final ImageView pickerViewSendButton = this.pickerViewSendButton;
        if (pickerViewSendButton != null) {
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)pickerViewSendButton.getLayoutParams();
            sendPhotoType = this.sendPhotoType;
            if (sendPhotoType != 4 && sendPhotoType != 5) {
                if (sendPhotoType != 1 && sendPhotoType != 3) {
                    this.pickerViewSendButton.setImageResource(2131165468);
                    this.pickerViewSendButton.setPadding(AndroidUtilities.dp(4.0f), 0, 0, 0);
                    layoutParams.bottomMargin = AndroidUtilities.dp(14.0f);
                }
                else {
                    this.pickerViewSendButton.setImageResource(2131165384);
                    this.pickerViewSendButton.setPadding(0, AndroidUtilities.dp(1.0f), 0, 0);
                    layoutParams.bottomMargin = AndroidUtilities.dp(19.0f);
                }
            }
            else {
                this.pickerViewSendButton.setImageResource(2131165468);
                this.pickerViewSendButton.setPadding(AndroidUtilities.dp(4.0f), 0, 0, 0);
                layoutParams.bottomMargin = AndroidUtilities.dp(19.0f);
            }
            this.pickerViewSendButton.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
        return this.openPhoto(null, null, null, null, list, n, photoViewerProvider, chatActivity, 0L, 0L, true);
    }
    
    @Keep
    public void setAnimationValue(final float animationValue) {
        this.animationValue = animationValue;
        this.containerView.invalidate();
    }
    
    public void setMaxSelectedPhotos(final int maxSelectedPhotos) {
        this.maxSelectedPhotos = maxSelectedPhotos;
    }
    
    public void setParentActivity(final Activity parentActivity) {
        this.currentAccount = UserConfig.selectedAccount;
        this.centerImage.setCurrentAccount(this.currentAccount);
        this.leftImage.setCurrentAccount(this.currentAccount);
        this.rightImage.setCurrentAccount(this.currentAccount);
        if (this.parentActivity == parentActivity) {
            return;
        }
        this.parentActivity = parentActivity;
        this.actvityContext = (Context)new ContextThemeWrapper((Context)this.parentActivity, 2131624206);
        if (PhotoViewer.progressDrawables == null) {
            (PhotoViewer.progressDrawables = new Drawable[4])[0] = this.parentActivity.getResources().getDrawable(2131165357);
            PhotoViewer.progressDrawables[1] = this.parentActivity.getResources().getDrawable(2131165337);
            PhotoViewer.progressDrawables[2] = this.parentActivity.getResources().getDrawable(2131165537);
            PhotoViewer.progressDrawables[3] = this.parentActivity.getResources().getDrawable(2131165771);
        }
        this.scroller = new Scroller((Context)parentActivity);
        (this.windowView = new FrameLayout(parentActivity) {
            private Runnable attachRunnable;
            
            public boolean dispatchKeyEventPreIme(final KeyEvent keyEvent) {
                if (keyEvent == null || keyEvent.getKeyCode() != 4 || keyEvent.getAction() != 1) {
                    return super.dispatchKeyEventPreIme(keyEvent);
                }
                if (!PhotoViewer.this.captionEditText.isPopupShowing() && !PhotoViewer.this.captionEditText.isKeyboardVisible()) {
                    PhotoViewer.getInstance().closePhoto(true, false);
                    return true;
                }
                PhotoViewer.this.closeCaptionEnter(false);
                return false;
            }
            
            protected boolean drawChild(final Canvas canvas, final View view, final long n) {
                boolean drawChild;
                try {
                    drawChild = super.drawChild(canvas, view, n);
                }
                catch (Throwable t) {
                    drawChild = false;
                }
                if (Build$VERSION.SDK_INT >= 21 && view == PhotoViewer.this.animatingImageView && PhotoViewer.this.lastInsets != null) {
                    canvas.drawRect(0.0f, (float)this.getMeasuredHeight(), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() + ((WindowInsets)PhotoViewer.this.lastInsets).getSystemWindowInsetBottom()), PhotoViewer.this.blackPaint);
                }
                return drawChild;
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
            
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                return PhotoViewer.this.isVisible && super.onInterceptTouchEvent(motionEvent);
            }
            
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                if (Build$VERSION.SDK_INT >= 21) {
                    PhotoViewer.this.lastInsets;
                }
                PhotoViewer.this.animatingImageView.layout(0, 0, PhotoViewer.this.animatingImageView.getMeasuredWidth() + 0, PhotoViewer.this.animatingImageView.getMeasuredHeight());
                PhotoViewer.this.containerView.layout(0, 0, PhotoViewer.this.containerView.getMeasuredWidth() + 0, PhotoViewer.this.containerView.getMeasuredHeight());
                PhotoViewer.this.wasLayout = true;
                if (b) {
                    if (!PhotoViewer.this.dontResetZoomOnFirstLayout) {
                        PhotoViewer.this.scale = 1.0f;
                        PhotoViewer.this.translationX = 0.0f;
                        PhotoViewer.this.translationY = 0.0f;
                        final PhotoViewer this$0 = PhotoViewer.this;
                        this$0.updateMinMax(this$0.scale);
                    }
                    if (PhotoViewer.this.checkImageView != null) {
                        PhotoViewer.this.checkImageView.post((Runnable)new _$$Lambda$PhotoViewer$5$gQgnGW1vt8iiuLxByIijlXWDJsU(this));
                    }
                }
                if (PhotoViewer.this.dontResetZoomOnFirstLayout) {
                    PhotoViewer.this.setScaleToFill();
                    PhotoViewer.this.dontResetZoomOnFirstLayout = false;
                }
            }
            
            protected void onMeasure(int size, int n) {
                final int size2 = View$MeasureSpec.getSize(size);
                size = View$MeasureSpec.getSize(n);
                if (Build$VERSION.SDK_INT >= 21 && PhotoViewer.this.lastInsets != null) {
                    final WindowInsets windowInsets = (WindowInsets)PhotoViewer.this.lastInsets;
                    n = size;
                    if (AndroidUtilities.incorrectDisplaySizeFix) {
                        final int y = AndroidUtilities.displaySize.y;
                        if ((n = size) > y) {
                            n = y;
                        }
                        n += AndroidUtilities.statusBarHeight;
                    }
                    n -= windowInsets.getSystemWindowInsetBottom();
                }
                else {
                    final int y2 = AndroidUtilities.displaySize.y;
                    if ((n = size) > y2) {
                        n = y2;
                    }
                }
                this.setMeasuredDimension(size2, n);
                final ViewGroup$LayoutParams layoutParams = PhotoViewer.this.animatingImageView.getLayoutParams();
                PhotoViewer.this.animatingImageView.measure(View$MeasureSpec.makeMeasureSpec(layoutParams.width, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(layoutParams.height, Integer.MIN_VALUE));
                PhotoViewer.this.containerView.measure(View$MeasureSpec.makeMeasureSpec(size2, 1073741824), View$MeasureSpec.makeMeasureSpec(n, 1073741824));
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                return PhotoViewer.this.isVisible && PhotoViewer.this.onTouchEvent(motionEvent);
            }
            
            public ActionMode startActionModeForChild(final View view, final ActionMode$Callback actionMode$Callback, final int n) {
                if (Build$VERSION.SDK_INT >= 23) {
                    final View viewById = PhotoViewer.this.parentActivity.findViewById(16908290);
                    if (viewById instanceof ViewGroup) {
                        try {
                            return ((ViewGroup)viewById).startActionModeForChild(view, actionMode$Callback, n);
                        }
                        catch (Throwable t) {
                            FileLog.e(t);
                        }
                    }
                }
                return super.startActionModeForChild(view, actionMode$Callback, n);
            }
        }).setBackgroundDrawable((Drawable)this.backgroundDrawable);
        this.windowView.setClipChildren(true);
        this.windowView.setFocusable(false);
        (this.animatingImageView = new ClippingImageView((Context)parentActivity)).setAnimationValues(this.animationValues);
        this.windowView.addView((View)this.animatingImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f));
        (this.containerView = new FrameLayoutDrawer((Context)parentActivity)).setFocusable(false);
        this.windowView.addView((View)this.containerView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        if (Build$VERSION.SDK_INT >= 21) {
            this.containerView.setFitsSystemWindows(true);
            this.containerView.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new _$$Lambda$PhotoViewer$dljDNjeNI7WCE_iobU6HEh1zPNQ(this));
            this.containerView.setSystemUiVisibility(1792);
        }
        this.windowLayoutParams = new WindowManager$LayoutParams();
        final WindowManager$LayoutParams windowLayoutParams = this.windowLayoutParams;
        windowLayoutParams.height = -1;
        windowLayoutParams.format = -3;
        windowLayoutParams.width = -1;
        windowLayoutParams.gravity = 51;
        windowLayoutParams.type = 99;
        if (Build$VERSION.SDK_INT >= 28) {
            windowLayoutParams.layoutInDisplayCutoutMode = 1;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.windowLayoutParams.flags = -2147286784;
        }
        else {
            this.windowLayoutParams.flags = 131072;
        }
        (this.actionBar = new ActionBar(parentActivity) {
            public void setAlpha(final float alpha) {
                super.setAlpha(alpha);
                PhotoViewer.this.containerView.invalidate();
            }
        }).setTitleColor(-1);
        this.actionBar.setSubtitleColor(-1);
        this.actionBar.setBackgroundColor(2130706432);
        this.actionBar.setOccupyStatusBar(Build$VERSION.SDK_INT >= 21);
        this.actionBar.setItemsBackgroundColor(1090519039, false);
        this.actionBar.setBackButtonImage(2131165409);
        this.actionBar.setTitle(LocaleController.formatString("Of", 2131560099, 1, 1));
        this.containerView.addView((View)this.actionBar, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
        this.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public boolean canOpenMenu() {
                if (PhotoViewer.this.currentMessageObject != null) {
                    return FileLoader.getPathToMessage(PhotoViewer.this.currentMessageObject.messageOwner).exists();
                }
                final ImageLocation access$7200 = PhotoViewer.this.currentFileLocation;
                boolean b = false;
                if (access$7200 != null) {
                    final PhotoViewer this$0 = PhotoViewer.this;
                    final TLRPC.FileLocation access$7201 = this$0.getFileLocation(this$0.currentFileLocation);
                    if (PhotoViewer.this.avatarsDialogId != 0 || PhotoViewer.this.isEvent) {
                        b = true;
                    }
                    return FileLoader.getPathToAttach(access$7201, b).exists();
                }
                return false;
            }
            
            @Override
            public void onItemClick(int i) {
                final int n = 1;
                if (i == -1) {
                    if (PhotoViewer.this.needCaptionLayout && (PhotoViewer.this.captionEditText.isPopupShowing() || PhotoViewer.this.captionEditText.isKeyboardVisible())) {
                        PhotoViewer.this.closeCaptionEnter(false);
                        return;
                    }
                    PhotoViewer.this.closePhoto(true, false);
                }
                else if (i == 1) {
                    if (Build$VERSION.SDK_INT >= 23 && PhotoViewer.this.parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                        PhotoViewer.this.parentActivity.requestPermissions(new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" }, 4);
                        return;
                    }
                    File file;
                    if (PhotoViewer.this.currentMessageObject != null) {
                        if (PhotoViewer.this.currentMessageObject.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage && PhotoViewer.this.currentMessageObject.messageOwner.media.webpage != null && PhotoViewer.this.currentMessageObject.messageOwner.media.webpage.document == null) {
                            final PhotoViewer this$0 = PhotoViewer.this;
                            file = FileLoader.getPathToAttach(this$0.getFileLocation(this$0.currentIndex, null), true);
                        }
                        else {
                            file = FileLoader.getPathToMessage(PhotoViewer.this.currentMessageObject.messageOwner);
                        }
                    }
                    else if (PhotoViewer.this.currentFileLocation != null) {
                        file = FileLoader.getPathToAttach(PhotoViewer.this.currentFileLocation.location, PhotoViewer.this.avatarsDialogId != 0 || PhotoViewer.this.isEvent);
                    }
                    else {
                        file = null;
                    }
                    if (file != null && file.exists()) {
                        final String string = file.toString();
                        final Activity access$2600 = PhotoViewer.this.parentActivity;
                        if (PhotoViewer.this.currentMessageObject != null && PhotoViewer.this.currentMessageObject.isVideo()) {
                            i = n;
                        }
                        else {
                            i = 0;
                        }
                        MediaController.saveFile(string, (Context)access$2600, i, null, null);
                    }
                    else {
                        PhotoViewer.this.showDownloadAlert();
                    }
                }
                else if (i == 2) {
                    if (PhotoViewer.this.currentDialogId != 0L) {
                        PhotoViewer.this.disableShowCheck = true;
                        final Bundle bundle = new Bundle();
                        bundle.putLong("dialog_id", PhotoViewer.this.currentDialogId);
                        i = PhotoViewer.this.sharedMediaType;
                        final MediaActivity mediaActivity = new MediaActivity(bundle, new int[] { -1, -1, -1, -1, -1 }, null, i);
                        if (PhotoViewer.this.parentChatActivity != null) {
                            mediaActivity.setChatInfo(PhotoViewer.this.parentChatActivity.getCurrentChatInfo());
                        }
                        PhotoViewer.this.closePhoto(false, false);
                        ((LaunchActivity)PhotoViewer.this.parentActivity).presentFragment(mediaActivity, false, true);
                    }
                }
                else if (i == 4) {
                    if (PhotoViewer.this.currentMessageObject == null) {
                        return;
                    }
                    final Bundle bundle2 = new Bundle();
                    final int n2 = (int)PhotoViewer.this.currentDialogId;
                    i = (int)(PhotoViewer.this.currentDialogId >> 32);
                    if (n2 != 0) {
                        if (i == 1) {
                            bundle2.putInt("chat_id", n2);
                        }
                        else if (n2 > 0) {
                            bundle2.putInt("user_id", n2);
                        }
                        else if (n2 < 0) {
                            final TLRPC.Chat chat = MessagesController.getInstance(PhotoViewer.this.currentAccount).getChat(-n2);
                            i = n2;
                            if (chat != null) {
                                i = n2;
                                if (chat.migrated_to != null) {
                                    bundle2.putInt("migrated_to", n2);
                                    i = -chat.migrated_to.channel_id;
                                }
                            }
                            bundle2.putInt("chat_id", -i);
                        }
                    }
                    else {
                        bundle2.putInt("enc_id", i);
                    }
                    bundle2.putInt("message_id", PhotoViewer.this.currentMessageObject.getId());
                    NotificationCenter.getInstance(PhotoViewer.this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                    final LaunchActivity launchActivity = (LaunchActivity)PhotoViewer.this.parentActivity;
                    launchActivity.presentFragment(new ChatActivity(bundle2), launchActivity.getMainFragmentsCount() > 1 || AndroidUtilities.isTablet(), true);
                    PhotoViewer.this.currentMessageObject = null;
                    PhotoViewer.this.closePhoto(false, false);
                }
                else if (i == 3) {
                    if (PhotoViewer.this.currentMessageObject == null || PhotoViewer.this.parentActivity == null) {
                        return;
                    }
                    ((LaunchActivity)PhotoViewer.this.parentActivity).switchToAccount(PhotoViewer.this.currentMessageObject.currentAccount, true);
                    final Bundle bundle3 = new Bundle();
                    bundle3.putBoolean("onlySelect", true);
                    bundle3.putInt("dialogsType", 3);
                    final DialogsActivity dialogsActivity = new DialogsActivity(bundle3);
                    final ArrayList<MessageObject> list = new ArrayList<MessageObject>();
                    list.add(PhotoViewer.this.currentMessageObject);
                    dialogsActivity.setDelegate((DialogsActivity.DialogsActivityDelegate)new _$$Lambda$PhotoViewer$7$MBP2lcO9C4pgMJ4ntN8Td94R7bI(this, list));
                    ((LaunchActivity)PhotoViewer.this.parentActivity).presentFragment(dialogsActivity, false, true);
                    PhotoViewer.this.closePhoto(false, false);
                }
                else if (i == 6) {
                    if (PhotoViewer.this.parentActivity == null || PhotoViewer.this.placeProvider == null) {
                        return;
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)PhotoViewer.this.parentActivity);
                    final String deleteMessageString = PhotoViewer.this.placeProvider.getDeleteMessageString();
                    if (deleteMessageString != null) {
                        builder.setMessage(deleteMessageString);
                    }
                    else if (PhotoViewer.this.currentMessageObject != null && PhotoViewer.this.currentMessageObject.isVideo()) {
                        builder.setMessage(LocaleController.formatString("AreYouSureDeleteVideo", 2131558693, new Object[0]));
                    }
                    else if (PhotoViewer.this.currentMessageObject != null && PhotoViewer.this.currentMessageObject.isGif()) {
                        builder.setMessage(LocaleController.formatString("AreYouSureDeleteGIF", 2131558684, new Object[0]));
                    }
                    else {
                        builder.setMessage(LocaleController.formatString("AreYouSureDeletePhoto", 2131558685, new Object[0]));
                    }
                    builder.setTitle(LocaleController.getString("AppName", 2131558635));
                    final boolean[] array = { false };
                    if (PhotoViewer.this.currentMessageObject != null) {
                        i = (int)PhotoViewer.this.currentMessageObject.getDialogId();
                        if (i != 0) {
                            TLRPC.User user;
                            TLRPC.Chat chat2;
                            if (i > 0) {
                                user = MessagesController.getInstance(PhotoViewer.this.currentAccount).getUser(i);
                                chat2 = null;
                            }
                            else {
                                chat2 = MessagesController.getInstance(PhotoViewer.this.currentAccount).getChat(-i);
                                user = null;
                            }
                            if (user != null || !ChatObject.isChannel(chat2)) {
                                final int currentTime = ConnectionsManager.getInstance(PhotoViewer.this.currentAccount).getCurrentTime();
                                if (user != null) {
                                    i = MessagesController.getInstance(PhotoViewer.this.currentAccount).revokeTimePmLimit;
                                }
                                else {
                                    i = MessagesController.getInstance(PhotoViewer.this.currentAccount).revokeTimeLimit;
                                }
                                if (((user != null && user.id != UserConfig.getInstance(PhotoViewer.this.currentAccount).getClientUserId()) || chat2 != null) && (PhotoViewer.this.currentMessageObject.messageOwner.action == null || PhotoViewer.this.currentMessageObject.messageOwner.action instanceof TLRPC.TL_messageActionEmpty) && PhotoViewer.this.currentMessageObject.isOut() && currentTime - PhotoViewer.this.currentMessageObject.messageOwner.date <= i) {
                                    final FrameLayout view = new FrameLayout((Context)PhotoViewer.this.parentActivity);
                                    final CheckBoxCell checkBoxCell = new CheckBoxCell((Context)PhotoViewer.this.parentActivity, 1);
                                    checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                                    if (chat2 != null) {
                                        checkBoxCell.setText(LocaleController.getString("DeleteForAll", 2131559243), "", false, false);
                                    }
                                    else {
                                        checkBoxCell.setText(LocaleController.formatString("DeleteForUser", 2131559244, UserObject.getFirstName(user)), "", false, false);
                                    }
                                    if (LocaleController.isRTL) {
                                        i = AndroidUtilities.dp(16.0f);
                                    }
                                    else {
                                        i = AndroidUtilities.dp(8.0f);
                                    }
                                    int n3;
                                    if (LocaleController.isRTL) {
                                        n3 = AndroidUtilities.dp(8.0f);
                                    }
                                    else {
                                        n3 = AndroidUtilities.dp(16.0f);
                                    }
                                    checkBoxCell.setPadding(i, 0, n3, 0);
                                    view.addView((View)checkBoxCell, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                                    checkBoxCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$7$PnoO3dhBjKU061H6V2Ybr5NYtAo(array));
                                    builder.setView((View)view);
                                }
                            }
                        }
                    }
                    builder.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$PhotoViewer$7$It5atnO7wprrf4dhdkFyC0mozx4(this, array));
                    builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                    PhotoViewer.this.showAlertDialog(builder);
                }
                else if (i == 10) {
                    PhotoViewer.this.onSharePressed();
                }
                else if (i == 11) {
                    try {
                        AndroidUtilities.openForView(PhotoViewer.this.currentMessageObject, PhotoViewer.this.parentActivity);
                        PhotoViewer.this.closePhoto(false, false);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
                else if (i == 13) {
                    if (PhotoViewer.this.parentActivity == null || PhotoViewer.this.currentMessageObject == null || PhotoViewer.this.currentMessageObject.messageOwner.media == null || PhotoViewer.this.currentMessageObject.messageOwner.media.photo == null) {
                        return;
                    }
                    new StickersAlert((Context)PhotoViewer.this.parentActivity, PhotoViewer.this.currentMessageObject, PhotoViewer.this.currentMessageObject.messageOwner.media.photo).show();
                }
                else if (i == 5) {
                    if (PhotoViewer.this.pipItem.getAlpha() != 1.0f) {
                        return;
                    }
                    PhotoViewer.this.switchToPip();
                }
                else if (i == 7) {
                    if (PhotoViewer.this.currentMessageObject == null) {
                        return;
                    }
                    FileLoader.getInstance(PhotoViewer.this.currentAccount).cancelLoadFile(PhotoViewer.this.currentMessageObject.getDocument());
                    PhotoViewer.this.releasePlayer(false);
                    PhotoViewer.this.bottomLayout.setTag((Object)1);
                    PhotoViewer.this.bottomLayout.setVisibility(0);
                }
            }
        });
        final ActionBarMenu menu = this.actionBar.createMenu();
        this.masksItem = menu.addItem(13, 2131165645);
        this.pipItem = menu.addItem(5, 2131165445);
        this.sendItem = menu.addItem(3, 2131165627);
        this.menuItem = menu.addItem(0, 2131165416);
        this.menuItem.addSubItem(11, 2131165649, LocaleController.getString("OpenInExternalApp", 2131560116)).setColors(-328966, -328966);
        this.menuItem.setContentDescription((CharSequence)LocaleController.getString("AccDescrMoreOptions", 2131558443));
        (this.allMediaItem = this.menuItem.addSubItem(2, 2131165646, LocaleController.getString("ShowAllMedia", 2131560778))).setColors(-328966, -328966);
        this.menuItem.addSubItem(4, 2131165647, LocaleController.getString("ShowInChat", 2131560780)).setColors(-328966, -328966);
        this.menuItem.addSubItem(10, 2131165671, LocaleController.getString("ShareFile", 2131560748)).setColors(-328966, -328966);
        this.menuItem.addSubItem(1, 2131165628, LocaleController.getString("SaveToGallery", 2131560630)).setColors(-328966, -328966);
        this.menuItem.addSubItem(6, 2131165623, LocaleController.getString("Delete", 2131559227)).setColors(-328966, -328966);
        this.menuItem.addSubItem(7, 2131165617, LocaleController.getString("StopDownload", 2131560822)).setColors(-328966, -328966);
        this.menuItem.redrawPopup(-115203550);
        this.sendItem.setContentDescription((CharSequence)LocaleController.getString("Forward", 2131559504));
        (this.bottomLayout = new FrameLayout(this.actvityContext)).setBackgroundColor(2130706432);
        this.containerView.addView((View)this.bottomLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        this.groupedPhotosListView = new GroupedPhotosListView(this.actvityContext);
        this.containerView.addView((View)this.groupedPhotosListView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 62.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        this.groupedPhotosListView.setDelegate((GroupedPhotosListView.GroupedPhotosListViewDelegate)new GroupedPhotosListView.GroupedPhotosListViewDelegate() {
            @Override
            public int getAvatarsDialogId() {
                return PhotoViewer.this.avatarsDialogId;
            }
            
            @Override
            public int getCurrentAccount() {
                return PhotoViewer.this.currentAccount;
            }
            
            @Override
            public int getCurrentIndex() {
                return PhotoViewer.this.currentIndex;
            }
            
            @Override
            public ArrayList<MessageObject> getImagesArr() {
                return PhotoViewer.this.imagesArr;
            }
            
            @Override
            public ArrayList<ImageLocation> getImagesArrLocations() {
                return PhotoViewer.this.imagesArrLocations;
            }
            
            @Override
            public ArrayList<TLRPC.PageBlock> getPageBlockArr() {
                return null;
            }
            
            @Override
            public Object getParentObject() {
                return null;
            }
            
            @Override
            public int getSlideshowMessageId() {
                return PhotoViewer.this.slideshowMessageId;
            }
            
            @Override
            public void setCurrentIndex(final int n) {
                PhotoViewer.this.currentIndex = -1;
                if (PhotoViewer.this.currentThumb != null) {
                    PhotoViewer.this.currentThumb.release();
                    PhotoViewer.this.currentThumb = null;
                }
                PhotoViewer.this.setImageIndex(n, true);
            }
        });
        this.captionTextView = this.createCaptionTextView();
        this.switchCaptionTextView = this.createCaptionTextView();
        for (int i = 0; i < 3; ++i) {
            (this.photoProgressViews[i] = new PhotoProgressView(this.containerView.getContext(), (View)this.containerView)).setBackgroundState(0, false);
        }
        (this.miniProgressView = new RadialProgressView(this.actvityContext) {
            public void invalidate() {
                super.invalidate();
                if (PhotoViewer.this.containerView != null) {
                    PhotoViewer.this.containerView.invalidate();
                }
            }
            
            @Override
            public void setAlpha(final float alpha) {
                super.setAlpha(alpha);
                if (PhotoViewer.this.containerView != null) {
                    PhotoViewer.this.containerView.invalidate();
                }
            }
        }).setUseSelfAlpha(true);
        this.miniProgressView.setProgressColor(-1);
        this.miniProgressView.setSize(AndroidUtilities.dp(54.0f));
        this.miniProgressView.setBackgroundResource(2131165357);
        this.miniProgressView.setVisibility(4);
        this.miniProgressView.setAlpha(0.0f);
        this.containerView.addView((View)this.miniProgressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64, 17));
        (this.shareButton = new ImageView(this.containerView.getContext())).setImageResource(2131165818);
        this.shareButton.setScaleType(ImageView$ScaleType.CENTER);
        this.shareButton.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        this.bottomLayout.addView((View)this.shareButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(50, -1, 53));
        this.shareButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$jnWuEkM1mcOIlgJQ2IKC_gla8zE(this));
        this.shareButton.setContentDescription((CharSequence)LocaleController.getString("ShareFile", 2131560748));
        (this.nameTextView = new TextView(this.containerView.getContext())).setTextSize(1, 14.0f);
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.nameTextView.setTextColor(-1);
        this.nameTextView.setGravity(3);
        this.bottomLayout.addView((View)this.nameTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 16.0f, 5.0f, 60.0f, 0.0f));
        (this.dateTextView = new TextView(this.containerView.getContext())).setTextSize(1, 13.0f);
        this.dateTextView.setSingleLine(true);
        this.dateTextView.setMaxLines(1);
        this.dateTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.dateTextView.setTextColor(-1);
        this.dateTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.dateTextView.setGravity(3);
        this.bottomLayout.addView((View)this.dateTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 16.0f, 25.0f, 50.0f, 0.0f));
        this.createVideoControlsInterface();
        (this.progressView = new RadialProgressView((Context)this.parentActivity)).setProgressColor(-1);
        this.progressView.setBackgroundResource(2131165357);
        this.progressView.setVisibility(4);
        this.containerView.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(54, 54, 17));
        (this.qualityPicker = new PickerBottomLayoutViewer((Context)this.parentActivity)).setBackgroundColor(2130706432);
        this.qualityPicker.updateSelectedCount(0, false);
        this.qualityPicker.setTranslationY((float)AndroidUtilities.dp(120.0f));
        this.qualityPicker.doneButton.setText((CharSequence)LocaleController.getString("Done", 2131559299).toUpperCase());
        this.containerView.addView((View)this.qualityPicker, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        this.qualityPicker.cancelButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$Ix7Bv26ggFYqzRhGVPWK6_2sNXM(this));
        this.qualityPicker.doneButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$39sOKx3t1IYK0_BSSSDOFzpR_Xk(this));
        (this.videoForwardDrawable = new VideoForwardDrawable()).setDelegate((VideoForwardDrawable.VideoForwardDrawableDelegate)new VideoForwardDrawable.VideoForwardDrawableDelegate() {
            @Override
            public void invalidate() {
                PhotoViewer.this.containerView.invalidate();
            }
            
            @Override
            public void onAnimationEnd() {
            }
        });
        (this.qualityChooseView = new QualityChooseView((Context)this.parentActivity)).setTranslationY((float)AndroidUtilities.dp(120.0f));
        this.qualityChooseView.setVisibility(4);
        this.qualityChooseView.setBackgroundColor(2130706432);
        this.containerView.addView((View)this.qualityChooseView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 70.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        (this.pickerView = new FrameLayout(this.actvityContext) {
            public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
                return PhotoViewer.this.bottomTouchEnabled && super.dispatchTouchEvent(motionEvent);
            }
            
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                return PhotoViewer.this.bottomTouchEnabled && super.onInterceptTouchEvent(motionEvent);
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                return PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(motionEvent);
            }
        }).setBackgroundColor(2130706432);
        this.containerView.addView((View)this.pickerView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2, 83));
        (this.videoTimelineView = new VideoTimelinePlayView((Context)this.parentActivity)).setDelegate((VideoTimelinePlayView.VideoTimelineViewDelegate)new VideoTimelinePlayView.VideoTimelineViewDelegate() {
            @Override
            public void didStartDragging() {
            }
            
            @Override
            public void didStopDragging() {
            }
            
            @Override
            public void onLeftProgressChanged(final float n) {
                if (PhotoViewer.this.videoPlayer == null) {
                    return;
                }
                if (PhotoViewer.this.videoPlayer.isPlaying()) {
                    PhotoViewer.this.videoPlayer.pause();
                    PhotoViewer.this.containerView.invalidate();
                }
                PhotoViewer.this.videoPlayer.seekTo((int)(PhotoViewer.this.videoDuration * n));
                PhotoViewer.this.videoPlayerSeekbar.setProgress(0.0f);
                PhotoViewer.this.videoTimelineView.setProgress(0.0f);
                PhotoViewer.this.updateVideoInfo();
            }
            
            @Override
            public void onPlayProgressChanged(final float n) {
                if (PhotoViewer.this.videoPlayer == null) {
                    return;
                }
                PhotoViewer.this.videoPlayer.seekTo((int)(PhotoViewer.this.videoDuration * n));
            }
            
            @Override
            public void onRightProgressChanged(final float n) {
                if (PhotoViewer.this.videoPlayer == null) {
                    return;
                }
                if (PhotoViewer.this.videoPlayer.isPlaying()) {
                    PhotoViewer.this.videoPlayer.pause();
                    PhotoViewer.this.containerView.invalidate();
                }
                PhotoViewer.this.videoPlayer.seekTo((int)(PhotoViewer.this.videoDuration * n));
                PhotoViewer.this.videoPlayerSeekbar.setProgress(0.0f);
                PhotoViewer.this.videoTimelineView.setProgress(0.0f);
                PhotoViewer.this.updateVideoInfo();
            }
        });
        this.pickerView.addView((View)this.videoTimelineView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 58.0f, 51, 0.0f, 8.0f, 0.0f, 88.0f));
        (this.pickerViewSendButton = new ImageView((Context)this.parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
        this.pickerViewSendButton.setBackgroundDrawable(Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), -10043398, -10043398));
        this.pickerViewSendButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(-1, PorterDuff$Mode.MULTIPLY));
        this.pickerViewSendButton.setPadding(AndroidUtilities.dp(4.0f), 0, 0, 0);
        this.pickerViewSendButton.setImageResource(2131165468);
        this.containerView.addView((View)this.pickerViewSendButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(56, 56.0f, 85, 0.0f, 0.0f, 14.0f, 14.0f));
        this.pickerViewSendButton.setContentDescription((CharSequence)LocaleController.getString("Send", 2131560685));
        this.pickerViewSendButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$Ygm2nkEIz52xt8vsvpsgSvg6BwY(this));
        final LinearLayout linearLayout = new LinearLayout((Context)this.parentActivity);
        linearLayout.setOrientation(0);
        this.pickerView.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 48.0f, 81, 0.0f, 0.0f, 34.0f, 0.0f));
        (this.cropItem = new ImageView((Context)this.parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
        this.cropItem.setImageResource(2131165742);
        this.cropItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        linearLayout.addView((View)this.cropItem, (ViewGroup$LayoutParams)LayoutHelper.createLinear(70, 48));
        this.cropItem.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$eMxgxyAI6SdVhY11OBq7nV_Jqtc(this));
        this.cropItem.setContentDescription((CharSequence)LocaleController.getString("CropImage", 2131559176));
        (this.rotateItem = new ImageView((Context)this.parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
        this.rotateItem.setImageResource(2131165885);
        this.rotateItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        linearLayout.addView((View)this.rotateItem, (ViewGroup$LayoutParams)LayoutHelper.createLinear(70, 48));
        this.rotateItem.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$S84XUzehocAn_E9EWBFOsOwSJTw(this));
        this.rotateItem.setContentDescription((CharSequence)LocaleController.getString("AccDescrRotate", 2131558466));
        (this.paintItem = new ImageView((Context)this.parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
        this.paintItem.setImageResource(2131165745);
        this.paintItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        linearLayout.addView((View)this.paintItem, (ViewGroup$LayoutParams)LayoutHelper.createLinear(70, 48));
        this.paintItem.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$Nm0i__eT2GlY_xQALMRc26gc1G0(this));
        this.paintItem.setContentDescription((CharSequence)LocaleController.getString("AccDescrPhotoEditor", 2131558456));
        (this.compressItem = new ImageView((Context)this.parentActivity)).setTag((Object)1);
        this.compressItem.setScaleType(ImageView$ScaleType.CENTER);
        this.compressItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        this.selectedCompression = MessagesController.getGlobalMainSettings().getInt("compress_video2", 1);
        final int selectedCompression = this.selectedCompression;
        if (selectedCompression <= 0) {
            this.compressItem.setImageResource(2131165896);
        }
        else if (selectedCompression == 1) {
            this.compressItem.setImageResource(2131165897);
        }
        else if (selectedCompression == 2) {
            this.compressItem.setImageResource(2131165898);
        }
        else if (selectedCompression == 3) {
            this.compressItem.setImageResource(2131165899);
        }
        else if (selectedCompression == 4) {
            this.compressItem.setImageResource(2131165895);
        }
        linearLayout.addView((View)this.compressItem, (ViewGroup$LayoutParams)LayoutHelper.createLinear(70, 48));
        this.compressItem.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$ZNbglMdvMBz7fic2b_SNO94Evbk(this));
        final ImageView compressItem = this.compressItem;
        final StringBuilder sb = new StringBuilder();
        sb.append(LocaleController.getString("AccDescrVideoQuality", 2131558482));
        sb.append(", ");
        sb.append((new String[] { "240", "360", "480", "720", "1080" })[Math.max(0, this.selectedCompression)]);
        compressItem.setContentDescription((CharSequence)sb.toString());
        (this.muteItem = new ImageView((Context)this.parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
        this.muteItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        linearLayout.addView((View)this.muteItem, (ViewGroup$LayoutParams)LayoutHelper.createLinear(70, 48));
        this.muteItem.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$MIXMx0jfsMSq1oOpVJ4spUPP5Ss(this));
        (this.cameraItem = new ImageView((Context)this.parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
        this.cameraItem.setImageResource(2131165741);
        this.cameraItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        this.cameraItem.setContentDescription((CharSequence)LocaleController.getString("AccDescrTakeMorePics", 2131558479));
        this.containerView.addView((View)this.cameraItem, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, 85, 0.0f, 0.0f, 16.0f, 0.0f));
        this.cameraItem.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$FkVTKWzor1OYhcy_PdaAovVJgkQ(this));
        (this.tuneItem = new ImageView((Context)this.parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
        this.tuneItem.setImageResource(2131165751);
        this.tuneItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        linearLayout.addView((View)this.tuneItem, (ViewGroup$LayoutParams)LayoutHelper.createLinear(70, 48));
        this.tuneItem.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$BtjFUBdRHmVM1bQF4oFTkkfFVOY(this));
        this.tuneItem.setContentDescription((CharSequence)LocaleController.getString("AccDescrPhotoAdjust", 2131558455));
        (this.timeItem = new ImageView((Context)this.parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
        this.timeItem.setImageResource(2131165750);
        this.timeItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        this.timeItem.setContentDescription((CharSequence)LocaleController.getString("SetTimer", 2131560737));
        linearLayout.addView((View)this.timeItem, (ViewGroup$LayoutParams)LayoutHelper.createLinear(70, 48));
        this.timeItem.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$5tqvSuoNJkycbaIOeX7zem0z4z4(this));
        (this.editorDoneLayout = new PickerBottomLayoutViewer(this.actvityContext)).setBackgroundColor(2130706432);
        this.editorDoneLayout.updateSelectedCount(0, false);
        this.editorDoneLayout.setVisibility(8);
        this.containerView.addView((View)this.editorDoneLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        this.editorDoneLayout.cancelButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$jVRNS_vT_efmfMNpUJWPuD_BH_s(this));
        this.editorDoneLayout.doneButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$lzLoax3UVNdU6xIvKXGhOoY4Vc8(this));
        (this.resetButton = new TextView(this.actvityContext)).setVisibility(8);
        this.resetButton.setTextSize(1, 14.0f);
        this.resetButton.setTextColor(-1);
        this.resetButton.setGravity(17);
        this.resetButton.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
        this.resetButton.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.resetButton.setText((CharSequence)LocaleController.getString("Reset", 2131559178).toUpperCase());
        this.resetButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.editorDoneLayout.addView((View)this.resetButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 49));
        this.resetButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$jYKD2cxo5_0tGvpCTvsWOo0KirI(this));
        this.gestureDetector = new GestureDetector(this.containerView.getContext(), (GestureDetector$OnGestureListener)this);
        this.setDoubleTapEnabled(true);
        final _$$Lambda$PhotoViewer$8d9Z5wt8PBRYWSSOdeX0wadZFXk delegate = new _$$Lambda$PhotoViewer$8d9Z5wt8PBRYWSSOdeX0wadZFXk(this);
        this.centerImage.setParentView((View)this.containerView);
        this.centerImage.setCrossfadeAlpha((byte)2);
        this.centerImage.setInvalidateAll(true);
        this.centerImage.setDelegate((ImageReceiver.ImageReceiverDelegate)delegate);
        this.leftImage.setParentView((View)this.containerView);
        this.leftImage.setCrossfadeAlpha((byte)2);
        this.leftImage.setInvalidateAll(true);
        this.leftImage.setDelegate((ImageReceiver.ImageReceiverDelegate)delegate);
        this.rightImage.setParentView((View)this.containerView);
        this.rightImage.setCrossfadeAlpha((byte)2);
        this.rightImage.setInvalidateAll(true);
        this.rightImage.setDelegate((ImageReceiver.ImageReceiverDelegate)delegate);
        final int rotation = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
        (this.checkImageView = new CheckBox(this.containerView.getContext(), 2131165814) {
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                return PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(motionEvent);
            }
        }).setDrawBackground(true);
        this.checkImageView.setHasBorder(true);
        this.checkImageView.setSize(40);
        this.checkImageView.setCheckOffset(AndroidUtilities.dp(1.0f));
        this.checkImageView.setColor(-10043398, -1);
        this.checkImageView.setVisibility(8);
        final FrameLayoutDrawer containerView = this.containerView;
        final CheckBox checkImageView = this.checkImageView;
        float n;
        if (rotation != 3 && rotation != 1) {
            n = 68.0f;
        }
        else {
            n = 58.0f;
        }
        containerView.addView((View)checkImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, 53, 0.0f, n, 10.0f, 0.0f));
        if (Build$VERSION.SDK_INT >= 21) {
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.checkImageView.getLayoutParams();
            frameLayout$LayoutParams.topMargin += AndroidUtilities.statusBarHeight;
        }
        this.checkImageView.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$MNTsOUmDycPlm_15mLZjYpUg6Ec(this));
        this.photosCounterView = new CounterView((Context)this.parentActivity);
        final FrameLayoutDrawer containerView2 = this.containerView;
        final CounterView photosCounterView = this.photosCounterView;
        float n2;
        if (rotation != 3 && rotation != 1) {
            n2 = 68.0f;
        }
        else {
            n2 = 58.0f;
        }
        containerView2.addView((View)photosCounterView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, 53, 0.0f, n2, 66.0f, 0.0f));
        if (Build$VERSION.SDK_INT >= 21) {
            final FrameLayout$LayoutParams frameLayout$LayoutParams2 = (FrameLayout$LayoutParams)this.photosCounterView.getLayoutParams();
            frameLayout$LayoutParams2.topMargin += AndroidUtilities.statusBarHeight;
        }
        this.photosCounterView.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$v61rlDfXP7XMVPYKHGswQHaXc_E(this));
        (this.selectedPhotosListView = new RecyclerListView((Context)this.parentActivity)).setVisibility(8);
        this.selectedPhotosListView.setAlpha(0.0f);
        this.selectedPhotosListView.setTranslationY((float)(-AndroidUtilities.dp(10.0f)));
        this.selectedPhotosListView.addItemDecoration((RecyclerView.ItemDecoration)new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(final android.graphics.Rect rect, final View view, final RecyclerView recyclerView, final State state) {
                final int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                if (view instanceof PhotoPickerPhotoCell && childAdapterPosition == 0) {
                    rect.left = AndroidUtilities.dp(3.0f);
                }
                else {
                    rect.left = 0;
                }
                rect.right = AndroidUtilities.dp(3.0f);
            }
        });
        ((DefaultItemAnimator)this.selectedPhotosListView.getItemAnimator()).setDelayAnimations(false);
        this.selectedPhotosListView.setBackgroundColor(2130706432);
        this.selectedPhotosListView.setPadding(0, AndroidUtilities.dp(3.0f), 0, AndroidUtilities.dp(3.0f));
        this.selectedPhotosListView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(this.parentActivity, 0, false) {
            @Override
            public void smoothScrollToPosition(final RecyclerView recyclerView, final State state, final int targetPosition) {
                final LinearSmoothScrollerEnd linearSmoothScrollerEnd = new LinearSmoothScrollerEnd(recyclerView.getContext());
                ((RecyclerView.SmoothScroller)linearSmoothScrollerEnd).setTargetPosition(targetPosition);
                ((RecyclerView.LayoutManager)this).startSmoothScroll(linearSmoothScrollerEnd);
            }
        });
        this.selectedPhotosListView.setAdapter(this.selectedPhotosAdapter = new ListAdapter((Context)this.parentActivity));
        this.containerView.addView((View)this.selectedPhotosListView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 88, 51));
        this.selectedPhotosListView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$PhotoViewer$_FS1GaFBb1N0jAMDctH7XmfNRmw(this));
        (this.captionEditText = new PhotoViewerCaptionEnterView(this.actvityContext, this.containerView, this.windowView) {
            public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
                boolean b = false;
                try {
                    if (!PhotoViewer.this.bottomTouchEnabled) {
                        final boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
                        b = b;
                        if (dispatchTouchEvent) {
                            b = true;
                        }
                    }
                    return b;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                    return false;
                }
            }
            
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                boolean b = false;
                try {
                    if (!PhotoViewer.this.bottomTouchEnabled) {
                        final boolean onInterceptTouchEvent = super.onInterceptTouchEvent(motionEvent);
                        b = b;
                        if (onInterceptTouchEvent) {
                            b = true;
                        }
                    }
                    return b;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                    return false;
                }
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                return !PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(motionEvent);
            }
        }).setDelegate((PhotoViewerCaptionEnterView.PhotoViewerCaptionEnterViewDelegate)new PhotoViewerCaptionEnterView.PhotoViewerCaptionEnterViewDelegate() {
            @Override
            public void onCaptionEnter() {
                PhotoViewer.this.closeCaptionEnter(true);
            }
            
            @Override
            public void onTextChanged(final CharSequence charSequence) {
                if (PhotoViewer.this.mentionsAdapter != null && PhotoViewer.this.captionEditText != null && PhotoViewer.this.parentChatActivity != null && charSequence != null) {
                    PhotoViewer.this.mentionsAdapter.searchUsernameOrHashtag(charSequence.toString(), PhotoViewer.this.captionEditText.getCursorPosition(), PhotoViewer.this.parentChatActivity.messages, false);
                }
            }
            
            @Override
            public void onWindowSizeChanged(final int n) {
                final int min = Math.min(3, PhotoViewer.this.mentionsAdapter.getItemCount());
                int n2;
                if (PhotoViewer.this.mentionsAdapter.getItemCount() > 3) {
                    n2 = 18;
                }
                else {
                    n2 = 0;
                }
                if (n - ActionBar.getCurrentActionBarHeight() * 2 < AndroidUtilities.dp((float)(min * 36 + n2))) {
                    PhotoViewer.this.allowMentions = false;
                    if (PhotoViewer.this.mentionListView != null && PhotoViewer.this.mentionListView.getVisibility() == 0) {
                        PhotoViewer.this.mentionListView.setVisibility(4);
                    }
                }
                else {
                    PhotoViewer.this.allowMentions = true;
                    if (PhotoViewer.this.mentionListView != null && PhotoViewer.this.mentionListView.getVisibility() == 4) {
                        PhotoViewer.this.mentionListView.setVisibility(0);
                    }
                }
            }
        });
        if (Build$VERSION.SDK_INT >= 19) {
            this.captionEditText.setImportantForAccessibility(4);
        }
        this.containerView.addView((View)this.captionEditText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2, 83));
        (this.mentionListView = new RecyclerListView(this.actvityContext) {
            public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
                return !PhotoViewer.this.bottomTouchEnabled && super.dispatchTouchEvent(motionEvent);
            }
            
            @Override
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                return !PhotoViewer.this.bottomTouchEnabled && super.onInterceptTouchEvent(motionEvent);
            }
            
            @Override
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                return !PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(motionEvent);
            }
        }).setTag((Object)5);
        (this.mentionLayoutManager = new LinearLayoutManager(this.actvityContext) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        }).setOrientation(1);
        this.mentionListView.setLayoutManager((RecyclerView.LayoutManager)this.mentionLayoutManager);
        this.mentionListView.setBackgroundColor(2130706432);
        this.mentionListView.setVisibility(8);
        this.mentionListView.setClipToPadding(true);
        this.mentionListView.setOverScrollMode(2);
        this.containerView.addView((View)this.mentionListView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 110, 83));
        this.mentionListView.setAdapter(this.mentionsAdapter = new MentionsAdapter(this.actvityContext, true, 0L, (MentionsAdapter.MentionsAdapterDelegate)new MentionsAdapter.MentionsAdapterDelegate() {
            @Override
            public void needChangePanelVisibility(final boolean b) {
                if (b) {
                    final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)PhotoViewer.this.mentionListView.getLayoutParams();
                    final int min = Math.min(3, PhotoViewer.this.mentionsAdapter.getItemCount());
                    int n;
                    if (PhotoViewer.this.mentionsAdapter.getItemCount() > 3) {
                        n = 18;
                    }
                    else {
                        n = 0;
                    }
                    final float n2 = (float)(min * 36 + n);
                    layoutParams.height = AndroidUtilities.dp(n2);
                    layoutParams.topMargin = -AndroidUtilities.dp(n2);
                    PhotoViewer.this.mentionListView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    if (PhotoViewer.this.mentionListAnimation != null) {
                        PhotoViewer.this.mentionListAnimation.cancel();
                        PhotoViewer.this.mentionListAnimation = null;
                    }
                    if (PhotoViewer.this.mentionListView.getVisibility() == 0) {
                        PhotoViewer.this.mentionListView.setAlpha(1.0f);
                        return;
                    }
                    PhotoViewer.this.mentionLayoutManager.scrollToPositionWithOffset(0, 10000);
                    if (PhotoViewer.this.allowMentions) {
                        PhotoViewer.this.mentionListView.setVisibility(0);
                        PhotoViewer.this.mentionListAnimation = new AnimatorSet();
                        PhotoViewer.this.mentionListAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.mentionListView, View.ALPHA, new float[] { 0.0f, 1.0f }) });
                        PhotoViewer.this.mentionListAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator obj) {
                                if (PhotoViewer.this.mentionListAnimation != null && PhotoViewer.this.mentionListAnimation.equals(obj)) {
                                    PhotoViewer.this.mentionListAnimation = null;
                                }
                            }
                        });
                        PhotoViewer.this.mentionListAnimation.setDuration(200L);
                        PhotoViewer.this.mentionListAnimation.start();
                    }
                    else {
                        PhotoViewer.this.mentionListView.setAlpha(1.0f);
                        PhotoViewer.this.mentionListView.setVisibility(4);
                    }
                }
                else {
                    if (PhotoViewer.this.mentionListAnimation != null) {
                        PhotoViewer.this.mentionListAnimation.cancel();
                        PhotoViewer.this.mentionListAnimation = null;
                    }
                    if (PhotoViewer.this.mentionListView.getVisibility() == 8) {
                        return;
                    }
                    if (PhotoViewer.this.allowMentions) {
                        PhotoViewer.this.mentionListAnimation = new AnimatorSet();
                        PhotoViewer.this.mentionListAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)PhotoViewer.this.mentionListView, View.ALPHA, new float[] { 0.0f }) });
                        PhotoViewer.this.mentionListAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator obj) {
                                if (PhotoViewer.this.mentionListAnimation != null && PhotoViewer.this.mentionListAnimation.equals(obj)) {
                                    PhotoViewer.this.mentionListView.setVisibility(8);
                                    PhotoViewer.this.mentionListAnimation = null;
                                }
                            }
                        });
                        PhotoViewer.this.mentionListAnimation.setDuration(200L);
                        PhotoViewer.this.mentionListAnimation.start();
                    }
                    else {
                        PhotoViewer.this.mentionListView.setVisibility(8);
                    }
                }
            }
            
            @Override
            public void onContextClick(final TLRPC.BotInlineResult botInlineResult) {
            }
            
            @Override
            public void onContextSearch(final boolean b) {
            }
        }));
        this.mentionListView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$PhotoViewer$JG3xW1rvxoBoP1ShppmfpfTlhQo(this));
        this.mentionListView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new _$$Lambda$PhotoViewer$vduFoUyF328i_ViN04AaYIiTGtk(this));
        if (((AccessibilityManager)this.actvityContext.getSystemService("accessibility")).isEnabled()) {
            (this.playButtonAccessibilityOverlay = new View(this.actvityContext)).setContentDescription((CharSequence)LocaleController.getString("AccActionPlay", 2131558409));
            this.playButtonAccessibilityOverlay.setFocusable(true);
            this.containerView.addView(this.playButtonAccessibilityOverlay, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64, 17));
        }
    }
    
    public void setParentAlert(final ChatAttachAlert parentAlert) {
        this.parentAlert = parentAlert;
    }
    
    public void setParentChatActivity(final ChatActivity parentChatActivity) {
        this.parentChatActivity = parentChatActivity;
    }
    
    public void showAlertDialog(final AlertDialog.Builder builder) {
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
            (this.visibleDialog = builder.show()).setCanceledOnTouchOutside(true);
            this.visibleDialog.setOnDismissListener((DialogInterface$OnDismissListener)new _$$Lambda$PhotoViewer$6qHPQWdf_DnEW0pxgH_u4b0yivA(this));
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
    }
    
    public void updateMuteButton() {
        final VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.setMute(this.muteVideo);
        }
        if (!this.videoHasAudio) {
            this.muteItem.setEnabled(false);
            this.muteItem.setClickable(false);
            this.muteItem.setAlpha(0.5f);
        }
        else {
            this.muteItem.setEnabled(true);
            this.muteItem.setClickable(true);
            this.muteItem.setAlpha(1.0f);
            if (this.muteVideo) {
                this.actionBar.setSubtitle(null);
                this.muteItem.setImageResource(2131165911);
                this.muteItem.setColorFilter((ColorFilter)new PorterDuffColorFilter(-12734994, PorterDuff$Mode.MULTIPLY));
                if (this.compressItem.getTag() != null) {
                    this.compressItem.setClickable(false);
                    this.compressItem.setAlpha(0.5f);
                    this.compressItem.setEnabled(false);
                }
                this.videoTimelineView.setMaxProgressDiff(30000.0f / this.videoDuration);
                this.muteItem.setContentDescription((CharSequence)LocaleController.getString("NoSound", 2131559952));
            }
            else {
                this.muteItem.setColorFilter((ColorFilter)null);
                this.actionBar.setSubtitle(this.currentSubtitle);
                this.muteItem.setImageResource(2131165912);
                this.muteItem.setContentDescription((CharSequence)LocaleController.getString("Sound", 2131560800));
                if (this.compressItem.getTag() != null) {
                    this.compressItem.setClickable(true);
                    this.compressItem.setAlpha(1.0f);
                    this.compressItem.setEnabled(true);
                }
                this.videoTimelineView.setMaxProgressDiff(1.0f);
            }
        }
    }
    
    private class BackgroundDrawable extends ColorDrawable
    {
        private boolean allowDrawContent;
        private Runnable drawRunnable;
        
        public BackgroundDrawable(final int n) {
            super(n);
        }
        
        public void draw(final Canvas canvas) {
            super.draw(canvas);
            if (this.getAlpha() != 0) {
                final Runnable drawRunnable = this.drawRunnable;
                if (drawRunnable != null) {
                    AndroidUtilities.runOnUIThread(drawRunnable);
                    this.drawRunnable = null;
                }
            }
        }
        
        @Keep
        public void setAlpha(final int alpha) {
            if (PhotoViewer.this.parentActivity instanceof LaunchActivity) {
                this.allowDrawContent = (!PhotoViewer.this.isVisible || alpha != 255);
                ((LaunchActivity)PhotoViewer.this.parentActivity).drawerLayoutContainer.setAllowDrawContent(this.allowDrawContent);
                if (PhotoViewer.this.parentAlert != null) {
                    if (!this.allowDrawContent) {
                        AndroidUtilities.runOnUIThread(new _$$Lambda$PhotoViewer$BackgroundDrawable$pDidiNsUGVGYtvuU76vLRyxH30U(this), 50L);
                    }
                    else if (PhotoViewer.this.parentAlert != null) {
                        PhotoViewer.this.parentAlert.setAllowDrawContent(this.allowDrawContent);
                    }
                }
            }
            super.setAlpha(alpha);
        }
    }
    
    private class CounterView extends View
    {
        private int currentCount;
        private int height;
        private Paint paint;
        private RectF rect;
        private float rotation;
        private StaticLayout staticLayout;
        private TextPaint textPaint;
        private int width;
        
        public CounterView(final Context context) {
            super(context);
            this.currentCount = 0;
            (this.textPaint = new TextPaint(1)).setTextSize((float)AndroidUtilities.dp(18.0f));
            this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.textPaint.setColor(-1);
            (this.paint = new Paint(1)).setColor(-1);
            this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
            this.paint.setStyle(Paint$Style.STROKE);
            this.paint.setStrokeJoin(Paint$Join.ROUND);
            this.rect = new RectF();
            this.setCount(0);
        }
        
        public float getRotationX() {
            return this.rotation;
        }
        
        protected void onDraw(final Canvas canvas) {
            final int n = this.getMeasuredHeight() / 2;
            this.paint.setAlpha(255);
            this.rect.set((float)AndroidUtilities.dp(1.0f), (float)(n - AndroidUtilities.dp(14.0f)), (float)(this.getMeasuredWidth() - AndroidUtilities.dp(1.0f)), (float)(n + AndroidUtilities.dp(14.0f)));
            canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(15.0f), (float)AndroidUtilities.dp(15.0f), this.paint);
            if (this.staticLayout != null) {
                this.textPaint.setAlpha((int)((1.0f - this.rotation) * 255.0f));
                canvas.save();
                canvas.translate((float)((this.getMeasuredWidth() - this.width) / 2), (this.getMeasuredHeight() - this.height) / 2 + AndroidUtilities.dpf2(0.2f) + this.rotation * AndroidUtilities.dp(5.0f));
                this.staticLayout.draw(canvas);
                canvas.restore();
                this.paint.setAlpha((int)(this.rotation * 255.0f));
                final int n2 = (int)this.rect.centerX();
                final int n3 = (int)((int)this.rect.centerY() - (AndroidUtilities.dp(5.0f) * (1.0f - this.rotation) + AndroidUtilities.dp(3.0f)));
                canvas.drawLine((float)(AndroidUtilities.dp(0.5f) + n2), (float)(n3 - AndroidUtilities.dp(0.5f)), (float)(n2 - AndroidUtilities.dp(6.0f)), (float)(AndroidUtilities.dp(6.0f) + n3), this.paint);
                canvas.drawLine((float)(n2 - AndroidUtilities.dp(0.5f)), (float)(n3 - AndroidUtilities.dp(0.5f)), (float)(n2 + AndroidUtilities.dp(6.0f)), (float)(n3 + AndroidUtilities.dp(6.0f)), this.paint);
            }
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(Math.max(this.width + AndroidUtilities.dp(20.0f), AndroidUtilities.dp(30.0f)), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0f), 1073741824));
        }
        
        public void setCount(final int n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(Math.max(1, n));
            this.staticLayout = new StaticLayout((CharSequence)sb.toString(), this.textPaint, AndroidUtilities.dp(100.0f), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.width = (int)Math.ceil(this.staticLayout.getLineWidth(0));
            this.height = this.staticLayout.getLineBottom(0);
            final AnimatorSet set = new AnimatorSet();
            if (n == 0) {
                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, View.SCALE_X, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this, View.SCALE_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofInt((Object)this.paint, (Property)AnimationProperties.PAINT_ALPHA, new int[] { 0 }), (Animator)ObjectAnimator.ofInt((Object)this.textPaint, (Property)AnimationProperties.PAINT_ALPHA, new int[] { 0 }) });
                set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
            }
            else {
                final int currentCount = this.currentCount;
                if (currentCount == 0) {
                    set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, View.SCALE_X, new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this, View.SCALE_Y, new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofInt((Object)this.paint, (Property)AnimationProperties.PAINT_ALPHA, new int[] { 0, 255 }), (Animator)ObjectAnimator.ofInt((Object)this.textPaint, (Property)AnimationProperties.PAINT_ALPHA, new int[] { 0, 255 }) });
                    set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                }
                else if (n < currentCount) {
                    set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, View.SCALE_X, new float[] { 1.1f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this, View.SCALE_Y, new float[] { 1.1f, 1.0f }) });
                    set.setInterpolator((TimeInterpolator)new OvershootInterpolator());
                }
                else {
                    set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, View.SCALE_X, new float[] { 0.9f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this, View.SCALE_Y, new float[] { 0.9f, 1.0f }) });
                    set.setInterpolator((TimeInterpolator)new OvershootInterpolator());
                }
            }
            set.setDuration(180L);
            set.start();
            this.requestLayout();
            this.currentCount = n;
        }
        
        @Keep
        public void setRotationX(final float rotation) {
            this.rotation = rotation;
            this.invalidate();
        }
        
        @Keep
        public void setScaleX(final float scaleX) {
            super.setScaleX(scaleX);
            this.invalidate();
        }
    }
    
    public static class EmptyPhotoViewerProvider implements PhotoViewerProvider
    {
        @Override
        public boolean allowCaption() {
            return true;
        }
        
        @Override
        public boolean allowGroupPhotos() {
            return true;
        }
        
        @Override
        public boolean canCaptureMorePhotos() {
            return true;
        }
        
        @Override
        public boolean canScrollAway() {
            return true;
        }
        
        @Override
        public boolean cancelButtonPressed() {
            return true;
        }
        
        @Override
        public void deleteImageAtIndex(final int n) {
        }
        
        @Override
        public String getDeleteMessageString() {
            return null;
        }
        
        @Override
        public int getPhotoIndex(final int n) {
            return -1;
        }
        
        @Override
        public PlaceProviderObject getPlaceForPhoto(final MessageObject messageObject, final TLRPC.FileLocation fileLocation, final int n, final boolean b) {
            return null;
        }
        
        @Override
        public int getSelectedCount() {
            return 0;
        }
        
        @Override
        public HashMap<Object, Object> getSelectedPhotos() {
            return null;
        }
        
        @Override
        public ArrayList<Object> getSelectedPhotosOrder() {
            return null;
        }
        
        @Override
        public ImageReceiver.BitmapHolder getThumbForPhoto(final MessageObject messageObject, final TLRPC.FileLocation fileLocation, final int n) {
            return null;
        }
        
        @Override
        public boolean isPhotoChecked(final int n) {
            return false;
        }
        
        @Override
        public void needAddMorePhotos() {
        }
        
        @Override
        public boolean scaleToFill() {
            return false;
        }
        
        @Override
        public void sendButtonPressed(final int n, final VideoEditedInfo videoEditedInfo) {
        }
        
        @Override
        public int setPhotoChecked(final int n, final VideoEditedInfo videoEditedInfo) {
            return -1;
        }
        
        @Override
        public int setPhotoUnchecked(final Object o) {
            return -1;
        }
        
        @Override
        public void toggleGroupPhotosEnabled() {
        }
        
        @Override
        public void updatePhotoAtIndex(final int n) {
        }
        
        @Override
        public void willHidePhotoViewer() {
        }
        
        @Override
        public void willSwitchFromPhoto(final MessageObject messageObject, final TLRPC.FileLocation fileLocation, final int n) {
        }
    }
    
    private class FrameLayoutDrawer extends SizeNotifierFrameLayoutPhoto
    {
        private Paint paint;
        
        public FrameLayoutDrawer(final Context context) {
            super(context);
            this.paint = new Paint();
            this.setWillNotDraw(false);
            this.paint.setColor(855638016);
        }
        
        protected boolean drawChild(final Canvas canvas, final View view, final long n) {
            final RecyclerListView access$3800 = PhotoViewer.this.mentionListView;
            boolean b = true;
            if (view != access$3800 && view != PhotoViewer.this.captionEditText) {
                if (view != PhotoViewer.this.cameraItem && view != PhotoViewer.this.pickerView && view != PhotoViewer.this.pickerViewSendButton && view != PhotoViewer.this.captionTextView && (PhotoViewer.this.muteItem.getVisibility() != 0 || view != PhotoViewer.this.bottomLayout)) {
                    if (view != PhotoViewer.this.checkImageView && view != PhotoViewer.this.photosCounterView) {
                        if (view == PhotoViewer.this.miniProgressView) {
                            return false;
                        }
                    }
                    else {
                        if (PhotoViewer.this.captionEditText.getTag() != null) {
                            PhotoViewer.this.bottomTouchEnabled = false;
                            return false;
                        }
                        PhotoViewer.this.bottomTouchEnabled = true;
                    }
                }
                else {
                    int emojiPadding;
                    if (this.getKeyboardHeight() <= AndroidUtilities.dp(20.0f) && !AndroidUtilities.isInMultiwindow) {
                        emojiPadding = PhotoViewer.this.captionEditText.getEmojiPadding();
                    }
                    else {
                        emojiPadding = 0;
                    }
                    if (PhotoViewer.this.captionEditText.isPopupShowing() || (AndroidUtilities.usingHardwareInput && PhotoViewer.this.captionEditText.getTag() != null) || this.getKeyboardHeight() > 0 || emojiPadding != 0) {
                        PhotoViewer.this.bottomTouchEnabled = false;
                        return false;
                    }
                    PhotoViewer.this.bottomTouchEnabled = true;
                }
            }
            else if (!PhotoViewer.this.captionEditText.isPopupShowing() && PhotoViewer.this.captionEditText.getEmojiPadding() == 0 && ((AndroidUtilities.usingHardwareInput && PhotoViewer.this.captionEditText.getTag() == null) || this.getKeyboardHeight() == 0)) {
                return false;
            }
            try {
                if (view == PhotoViewer.this.aspectRatioFrameLayout || !super.drawChild(canvas, view, n)) {
                    b = false;
                }
                return b;
            }
            catch (Throwable t) {
                return b;
            }
        }
        
        protected void onDraw(final Canvas canvas) {
            PhotoViewer.this.onDraw(canvas);
            if (Build$VERSION.SDK_INT >= 21 && AndroidUtilities.statusBarHeight != 0 && PhotoViewer.this.actionBar != null) {
                this.paint.setAlpha((int)(PhotoViewer.this.actionBar.getAlpha() * 255.0f * 0.2f));
                canvas.drawRect(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)AndroidUtilities.statusBarHeight, this.paint);
                this.paint.setAlpha((int)(PhotoViewer.this.actionBar.getAlpha() * 255.0f * 0.498f));
                if (this.getPaddingRight() > 0) {
                    canvas.drawRect((float)(this.getMeasuredWidth() - this.getPaddingRight()), 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.paint);
                }
                if (this.getPaddingLeft() > 0) {
                    canvas.drawRect(0.0f, 0.0f, (float)this.getPaddingLeft(), (float)this.getMeasuredHeight(), this.paint);
                }
            }
        }
        
        @Override
        protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
            final int childCount = this.getChildCount();
            final int keyboardHeight = this.getKeyboardHeight();
            final int dp = AndroidUtilities.dp(20.0f);
            int i = 0;
            int emojiPadding;
            if (keyboardHeight <= dp && !AndroidUtilities.isInMultiwindow) {
                emojiPadding = PhotoViewer.this.captionEditText.getEmojiPadding();
            }
            else {
                emojiPadding = 0;
            }
            while (i < childCount) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() != 8) {
                    int n5;
                    int n6;
                    int n7;
                    if (child == PhotoViewer.this.aspectRatioFrameLayout) {
                        n5 = n;
                        n6 = n3;
                        n7 = n4;
                    }
                    else {
                        n5 = n + this.getPaddingLeft();
                        n6 = n3 - this.getPaddingRight();
                        n7 = n4 - this.getPaddingBottom();
                    }
                    final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
                    final int measuredWidth = child.getMeasuredWidth();
                    final int measuredHeight = child.getMeasuredHeight();
                    int gravity;
                    if ((gravity = frameLayout$LayoutParams.gravity) == -1) {
                        gravity = 51;
                    }
                    final int n8 = gravity & 0x70;
                    final int n9 = gravity & 0x7 & 0x7;
                    int leftMargin = 0;
                    Label_0269: {
                        int n10;
                        int n11;
                        if (n9 != 1) {
                            if (n9 != 5) {
                                leftMargin = frameLayout$LayoutParams.leftMargin;
                                break Label_0269;
                            }
                            n10 = n6 - n5 - measuredWidth;
                            n11 = frameLayout$LayoutParams.rightMargin;
                        }
                        else {
                            n10 = (n6 - n5 - measuredWidth) / 2 + frameLayout$LayoutParams.leftMargin;
                            n11 = frameLayout$LayoutParams.rightMargin;
                        }
                        leftMargin = n10 - n11;
                    }
                    int n12 = 0;
                    Label_0366: {
                        int n13;
                        int n14;
                        if (n8 != 16) {
                            if (n8 == 48) {
                                n12 = frameLayout$LayoutParams.topMargin;
                                break Label_0366;
                            }
                            if (n8 != 80) {
                                n12 = frameLayout$LayoutParams.topMargin;
                                break Label_0366;
                            }
                            n13 = n7 - emojiPadding - n2 - measuredHeight;
                            n14 = frameLayout$LayoutParams.bottomMargin;
                        }
                        else {
                            n13 = (n7 - emojiPadding - n2 - measuredHeight) / 2 + frameLayout$LayoutParams.topMargin;
                            n14 = frameLayout$LayoutParams.bottomMargin;
                        }
                        n12 = n13 - n14;
                    }
                    int n15 = 0;
                    Label_0692: {
                        int n18 = 0;
                        Label_0390: {
                            if (child != PhotoViewer.this.mentionListView) {
                                int bottom;
                                int n16;
                                if (PhotoViewer.this.captionEditText.isPopupView(child)) {
                                    if (!AndroidUtilities.isInMultiwindow) {
                                        n15 = PhotoViewer.this.captionEditText.getBottom();
                                        break Label_0692;
                                    }
                                    bottom = PhotoViewer.this.captionEditText.getTop() - child.getMeasuredHeight();
                                    n16 = AndroidUtilities.dp(1.0f);
                                }
                                else {
                                    if (child == PhotoViewer.this.selectedPhotosListView) {
                                        n15 = PhotoViewer.this.actionBar.getMeasuredHeight();
                                        break Label_0692;
                                    }
                                    if (child != PhotoViewer.this.captionTextView && child != PhotoViewer.this.switchCaptionTextView) {
                                        if (PhotoViewer.this.hintTextView != null && child == PhotoViewer.this.hintTextView) {
                                            bottom = PhotoViewer.this.selectedPhotosListView.getBottom();
                                            n16 = AndroidUtilities.dp(3.0f);
                                        }
                                        else {
                                            n15 = n12;
                                            if (child == PhotoViewer.this.cameraItem) {
                                                final int top = PhotoViewer.this.pickerView.getTop();
                                                float n17;
                                                if (PhotoViewer.this.sendPhotoType != 4 && PhotoViewer.this.sendPhotoType != 5) {
                                                    n17 = 15.0f;
                                                }
                                                else {
                                                    n17 = 40.0f;
                                                }
                                                n12 = top - AndroidUtilities.dp(n17);
                                                n18 = PhotoViewer.this.cameraItem.getMeasuredHeight();
                                                break Label_0390;
                                            }
                                            break Label_0692;
                                        }
                                    }
                                    else {
                                        n15 = n12;
                                        if (!PhotoViewer.this.groupedPhotosListView.currentPhotos.isEmpty()) {
                                            n18 = PhotoViewer.this.groupedPhotosListView.getMeasuredHeight();
                                            break Label_0390;
                                        }
                                        break Label_0692;
                                    }
                                }
                                n15 = bottom + n16;
                                break Label_0692;
                            }
                            n18 = PhotoViewer.this.captionEditText.getMeasuredHeight();
                        }
                        n15 = n12 - n18;
                    }
                    child.layout(leftMargin + n5, n15, leftMargin + measuredWidth + n5, measuredHeight + n15);
                }
                ++i;
            }
            this.notifyHeightChanged();
        }
        
        protected void onMeasure(final int n, final int n2) {
            final int size = View$MeasureSpec.getSize(n);
            final int size2 = View$MeasureSpec.getSize(n2);
            this.setMeasuredDimension(size, size2);
            this.measureChildWithMargins((View)PhotoViewer.this.captionEditText, n, 0, n2, 0);
            final int measuredHeight = PhotoViewer.this.captionEditText.getMeasuredHeight();
            final int n3 = size - (this.getPaddingRight() + this.getPaddingLeft());
            final int n4 = size2 - this.getPaddingBottom();
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() != 8) {
                    if (child != PhotoViewer.this.captionEditText) {
                        if (child == PhotoViewer.this.aspectRatioFrameLayout) {
                            final int y = AndroidUtilities.displaySize.y;
                            int statusBarHeight;
                            if (Build$VERSION.SDK_INT >= 21) {
                                statusBarHeight = AndroidUtilities.statusBarHeight;
                            }
                            else {
                                statusBarHeight = 0;
                            }
                            child.measure(n, View$MeasureSpec.makeMeasureSpec(y + statusBarHeight, 1073741824));
                        }
                        else if (PhotoViewer.this.captionEditText.isPopupView(child)) {
                            if (AndroidUtilities.isInMultiwindow) {
                                if (AndroidUtilities.isTablet()) {
                                    child.measure(View$MeasureSpec.makeMeasureSpec(n3, 1073741824), View$MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(320.0f), n4 - measuredHeight - AndroidUtilities.statusBarHeight), 1073741824));
                                }
                                else {
                                    child.measure(View$MeasureSpec.makeMeasureSpec(n3, 1073741824), View$MeasureSpec.makeMeasureSpec(n4 - measuredHeight - AndroidUtilities.statusBarHeight, 1073741824));
                                }
                            }
                            else {
                                child.measure(View$MeasureSpec.makeMeasureSpec(n3, 1073741824), View$MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, 1073741824));
                            }
                        }
                        else {
                            this.measureChildWithMargins(child, n, 0, n2, 0);
                        }
                    }
                }
            }
        }
    }
    
    public static class LinkMovementMethodMy extends LinkMovementMethod
    {
        public boolean onTouchEvent(final TextView textView, final Spannable spannable, final MotionEvent motionEvent) {
            try {
                final boolean onTouchEvent = super.onTouchEvent(textView, spannable, motionEvent);
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    Selection.removeSelection(spannable);
                }
                return onTouchEvent;
            }
            catch (Exception ex) {
                FileLog.e(ex);
                return false;
            }
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            if (PhotoViewer.this.placeProvider == null || PhotoViewer.this.placeProvider.getSelectedPhotosOrder() == null) {
                return 0;
            }
            if (PhotoViewer.this.placeProvider.allowGroupPhotos()) {
                return PhotoViewer.this.placeProvider.getSelectedPhotosOrder().size() + 1;
            }
            return PhotoViewer.this.placeProvider.getSelectedPhotosOrder().size();
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == 0 && PhotoViewer.this.placeProvider.allowGroupPhotos()) {
                return 1;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return true;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            final int itemViewType = viewHolder.getItemViewType();
            final ColorFilter colorFilter = null;
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    final ImageView imageView = (ImageView)viewHolder.itemView;
                    Object colorFilter2 = colorFilter;
                    if (SharedConfig.groupPhotosEnabled) {
                        colorFilter2 = new PorterDuffColorFilter(-10043398, PorterDuff$Mode.MULTIPLY);
                    }
                    imageView.setColorFilter((ColorFilter)colorFilter2);
                    String s;
                    if (SharedConfig.groupPhotosEnabled) {
                        i = 2131559612;
                        s = "GroupPhotosHelp";
                    }
                    else {
                        i = 2131560786;
                        s = "SinglePhotosHelp";
                    }
                    imageView.setContentDescription((CharSequence)LocaleController.getString(s, i));
                }
            }
            else {
                final PhotoPickerPhotoCell photoPickerPhotoCell = (PhotoPickerPhotoCell)viewHolder.itemView;
                photoPickerPhotoCell.itemWidth = AndroidUtilities.dp(82.0f);
                final BackupImageView photoImage = photoPickerPhotoCell.photoImage;
                photoImage.setOrientation(0, true);
                final ArrayList<Object> selectedPhotosOrder = PhotoViewer.this.placeProvider.getSelectedPhotosOrder();
                int index = i;
                if (PhotoViewer.this.placeProvider.allowGroupPhotos()) {
                    index = i - 1;
                }
                final Object value = PhotoViewer.this.placeProvider.getSelectedPhotos().get(selectedPhotosOrder.get(index));
                if (value instanceof MediaController.PhotoEntry) {
                    final MediaController.PhotoEntry tag = (MediaController.PhotoEntry)value;
                    photoPickerPhotoCell.setTag((Object)tag);
                    photoPickerPhotoCell.videoInfoContainer.setVisibility(4);
                    final String thumbPath = tag.thumbPath;
                    if (thumbPath != null) {
                        photoImage.setImage(thumbPath, null, this.mContext.getResources().getDrawable(2131165697));
                    }
                    else if (tag.path != null) {
                        photoImage.setOrientation(tag.orientation, true);
                        if (tag.isVideo) {
                            photoPickerPhotoCell.videoInfoContainer.setVisibility(0);
                            final int duration = tag.duration;
                            i = duration / 60;
                            photoPickerPhotoCell.videoTextView.setText((CharSequence)String.format("%d:%02d", i, duration - i * 60));
                            final StringBuilder sb = new StringBuilder();
                            sb.append("vthumb://");
                            sb.append(tag.imageId);
                            sb.append(":");
                            sb.append(tag.path);
                            photoImage.setImage(sb.toString(), null, this.mContext.getResources().getDrawable(2131165697));
                        }
                        else {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("thumb://");
                            sb2.append(tag.imageId);
                            sb2.append(":");
                            sb2.append(tag.path);
                            photoImage.setImage(sb2.toString(), null, this.mContext.getResources().getDrawable(2131165697));
                        }
                    }
                    else {
                        photoImage.setImageResource(2131165697);
                    }
                    photoPickerPhotoCell.setChecked(-1, true, false);
                    photoPickerPhotoCell.checkBox.setVisibility(0);
                }
                else if (value instanceof MediaController.SearchImage) {
                    final MediaController.SearchImage searchImage = (MediaController.SearchImage)value;
                    photoPickerPhotoCell.setTag((Object)searchImage);
                    photoPickerPhotoCell.setImage(searchImage);
                    photoPickerPhotoCell.videoInfoContainer.setVisibility(4);
                    photoPickerPhotoCell.setChecked(-1, true, false);
                    photoPickerPhotoCell.checkBox.setVisibility(0);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                o = new ImageView(this.mContext) {
                    protected void onMeasure(final int n, final int n2) {
                        super.onMeasure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(66.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n2), 1073741824));
                    }
                };
                ((ImageView)o).setScaleType(ImageView$ScaleType.CENTER);
                ((ImageView)o).setImageResource(2131165757);
                ((ImageView)o).setFocusable(true);
            }
            else {
                o = new PhotoPickerPhotoCell(this.mContext, false);
                ((PhotoPickerPhotoCell)o).checkFrame.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoViewer$ListAdapter$9zkDzdvMmAtv_zjywq_3chr7CXo(this));
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    private class PhotoProgressView
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
        
        public PhotoProgressView(final Context context, final View parent) {
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
            if (PhotoViewer.decelerateInterpolator == null) {
                PhotoViewer.decelerateInterpolator = new DecelerateInterpolator(1.5f);
                PhotoViewer.progressPaint = new Paint(1);
                PhotoViewer.progressPaint.setStyle(Paint$Style.STROKE);
                PhotoViewer.progressPaint.setStrokeCap(Paint$Cap.ROUND);
                PhotoViewer.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(3.0f));
                PhotoViewer.progressPaint.setColor(-1);
            }
            this.parent = parent;
        }
        
        private void updateAnimation() {
            final long currentTimeMillis = System.currentTimeMillis();
            long n;
            if ((n = currentTimeMillis - this.lastUpdateTime) > 18L) {
                n = 18L;
            }
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
                        this.animatedProgressValue = animationProgressStart + n2 * PhotoViewer.decelerateInterpolator.getInterpolation(this.currentProgressTime / 300.0f);
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
            final int n2 = (PhotoViewer.this.getContainerViewWidth() - n) / 2;
            final int n3 = (PhotoViewer.this.getContainerViewHeight() - n) / 2;
            final int previousBackgroundState = this.previousBackgroundState;
            if (previousBackgroundState >= 0 && previousBackgroundState < 4) {
                final Drawable drawable = PhotoViewer.progressDrawables[this.previousBackgroundState];
                if (drawable != null) {
                    drawable.setAlpha((int)(this.animatedAlphaValue * 255.0f * this.alpha));
                    drawable.setBounds(n2, n3, n2 + n, n3 + n);
                    drawable.draw(canvas);
                }
            }
            final int backgroundState = this.backgroundState;
            if (backgroundState >= 0 && backgroundState < 4) {
                final Drawable drawable2 = PhotoViewer.progressDrawables[this.backgroundState];
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
                PhotoViewer.progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0f * this.alpha));
            }
            else {
                PhotoViewer.progressPaint.setAlpha((int)(this.alpha * 255.0f));
            }
            this.progressRect.set((float)(n2 + dp), (float)(n3 + dp), (float)(n2 + n - dp), (float)(n3 + n - dp));
            canvas.drawArc(this.progressRect, this.radOffset - 90.0f, Math.max(4.0f, this.animatedProgressValue * 360.0f), false, PhotoViewer.progressPaint);
            this.updateAnimation();
        }
        
        public void setAlpha(final float alpha) {
            this.alpha = alpha;
        }
        
        public void setBackgroundState(final int backgroundState, final boolean b) {
            if (this.backgroundState == backgroundState && b) {
                return;
            }
            this.lastUpdateTime = System.currentTimeMillis();
            Label_0053: {
                if (b) {
                    final int backgroundState2 = this.backgroundState;
                    if (backgroundState2 != backgroundState) {
                        this.previousBackgroundState = backgroundState2;
                        this.animatedAlphaValue = 1.0f;
                        break Label_0053;
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
    
    public interface PhotoViewerProvider
    {
        boolean allowCaption();
        
        boolean allowGroupPhotos();
        
        boolean canCaptureMorePhotos();
        
        boolean canScrollAway();
        
        boolean cancelButtonPressed();
        
        void deleteImageAtIndex(final int p0);
        
        String getDeleteMessageString();
        
        int getPhotoIndex(final int p0);
        
        PlaceProviderObject getPlaceForPhoto(final MessageObject p0, final TLRPC.FileLocation p1, final int p2, final boolean p3);
        
        int getSelectedCount();
        
        HashMap<Object, Object> getSelectedPhotos();
        
        ArrayList<Object> getSelectedPhotosOrder();
        
        ImageReceiver.BitmapHolder getThumbForPhoto(final MessageObject p0, final TLRPC.FileLocation p1, final int p2);
        
        boolean isPhotoChecked(final int p0);
        
        void needAddMorePhotos();
        
        boolean scaleToFill();
        
        void sendButtonPressed(final int p0, final VideoEditedInfo p1);
        
        int setPhotoChecked(final int p0, final VideoEditedInfo p1);
        
        int setPhotoUnchecked(final Object p0);
        
        void toggleGroupPhotosEnabled();
        
        void updatePhotoAtIndex(final int p0);
        
        void willHidePhotoViewer();
        
        void willSwitchFromPhoto(final MessageObject p0, final TLRPC.FileLocation p1, final int p2);
    }
    
    public static class PlaceProviderObject
    {
        public int clipBottomAddition;
        public int clipTopAddition;
        public int dialogId;
        public ImageReceiver imageReceiver;
        public int index;
        public boolean isEvent;
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
    
    private class QualityChooseView extends View
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
        private TextPaint textPaint;
        
        public QualityChooseView(final Context context) {
            super(context);
            this.paint = new Paint(1);
            (this.textPaint = new TextPaint(1)).setTextSize((float)AndroidUtilities.dp(12.0f));
            this.textPaint.setColor(-3289651);
        }
        
        protected void onDraw(final Canvas canvas) {
            if (PhotoViewer.this.compressionsCount != 1) {
                this.lineSize = (this.getMeasuredWidth() - this.circleSize * PhotoViewer.this.compressionsCount - this.gapSize * 8 - this.sideSide * 2) / (PhotoViewer.this.compressionsCount - 1);
            }
            else {
                this.lineSize = this.getMeasuredWidth() - this.circleSize * PhotoViewer.this.compressionsCount - this.gapSize * 8 - this.sideSide * 2;
            }
            final int n = this.getMeasuredHeight() / 2 + AndroidUtilities.dp(6.0f);
            for (int i = 0; i < PhotoViewer.this.compressionsCount; ++i) {
                final int sideSide = this.sideSide;
                final int lineSize = this.lineSize;
                final int gapSize = this.gapSize;
                final int circleSize = this.circleSize;
                final int n2 = sideSide + (lineSize + gapSize * 2 + circleSize) * i + circleSize / 2;
                if (i <= PhotoViewer.this.selectedCompression) {
                    this.paint.setColor(-11292945);
                }
                else {
                    this.paint.setColor(1728053247);
                }
                String string;
                if (i == PhotoViewer.this.compressionsCount - 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(Math.min(PhotoViewer.this.originalWidth, PhotoViewer.this.originalHeight));
                    sb.append("p");
                    string = sb.toString();
                }
                else if (i == 0) {
                    string = "240p";
                }
                else if (i == 1) {
                    string = "360p";
                }
                else if (i == 2) {
                    string = "480p";
                }
                else {
                    string = "720p";
                }
                final float measureText = this.textPaint.measureText(string);
                final float n3 = (float)n2;
                final float n4 = (float)n;
                int dp;
                if (i == PhotoViewer.this.selectedCompression) {
                    dp = AndroidUtilities.dp(8.0f);
                }
                else {
                    dp = this.circleSize / 2;
                }
                canvas.drawCircle(n3, n4, (float)dp, this.paint);
                canvas.drawText(string, n3 - measureText / 2.0f, (float)(n - AndroidUtilities.dp(16.0f)), (Paint)this.textPaint);
                if (i != 0) {
                    final int n5 = n2 - this.circleSize / 2 - this.gapSize - this.lineSize;
                    canvas.drawRect((float)n5, (float)(n - AndroidUtilities.dp(1.0f)), (float)(n5 + this.lineSize), (float)(AndroidUtilities.dp(2.0f) + n), this.paint);
                }
            }
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(n, n2);
            this.circleSize = AndroidUtilities.dp(12.0f);
            this.gapSize = AndroidUtilities.dp(2.0f);
            this.sideSide = AndroidUtilities.dp(18.0f);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final float x = motionEvent.getX();
            final int action = motionEvent.getAction();
            boolean startMoving = false;
            if (action == 0) {
                this.getParent().requestDisallowInterceptTouchEvent(true);
                for (int i = 0; i < PhotoViewer.this.compressionsCount; ++i) {
                    final int sideSide = this.sideSide;
                    final int lineSize = this.lineSize;
                    final int gapSize = this.gapSize;
                    final int circleSize = this.circleSize;
                    final int n = sideSide + (lineSize + gapSize * 2 + circleSize) * i + circleSize / 2;
                    if (x > n - AndroidUtilities.dp(15.0f) && x < n + AndroidUtilities.dp(15.0f)) {
                        if (i == PhotoViewer.this.selectedCompression) {
                            startMoving = true;
                        }
                        this.startMoving = startMoving;
                        this.startX = x;
                        this.startMovingQuality = PhotoViewer.this.selectedCompression;
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
                    int j = 0;
                    while (j < PhotoViewer.this.compressionsCount) {
                        final int sideSide2 = this.sideSide;
                        final int lineSize2 = this.lineSize;
                        final int gapSize2 = this.gapSize;
                        final int circleSize2 = this.circleSize;
                        final int n2 = sideSide2 + (gapSize2 * 2 + lineSize2 + circleSize2) * j + circleSize2 / 2;
                        final int n3 = lineSize2 / 2 + circleSize2 / 2 + gapSize2;
                        if (x > n2 - n3 && x < n2 + n3) {
                            if (PhotoViewer.this.selectedCompression != j) {
                                PhotoViewer.this.selectedCompression = j;
                                PhotoViewer.this.didChangedCompressionLevel(false);
                                this.invalidate();
                                break;
                            }
                            break;
                        }
                        else {
                            ++j;
                        }
                    }
                }
            }
            else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                if (!this.moving) {
                    int k = 0;
                    while (k < PhotoViewer.this.compressionsCount) {
                        final int sideSide3 = this.sideSide;
                        final int lineSize3 = this.lineSize;
                        final int gapSize3 = this.gapSize;
                        final int circleSize3 = this.circleSize;
                        final int n4 = sideSide3 + (lineSize3 + gapSize3 * 2 + circleSize3) * k + circleSize3 / 2;
                        if (x > n4 - AndroidUtilities.dp(15.0f) && x < n4 + AndroidUtilities.dp(15.0f)) {
                            if (PhotoViewer.this.selectedCompression != k) {
                                PhotoViewer.this.selectedCompression = k;
                                PhotoViewer.this.didChangedCompressionLevel(true);
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
                else if (PhotoViewer.this.selectedCompression != this.startMovingQuality) {
                    PhotoViewer.this.requestVideoPreview(1);
                }
                this.startMoving = false;
                this.moving = false;
            }
            return true;
        }
    }
}
