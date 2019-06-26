package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.annotation.Keep;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.camera.CameraController;
import org.telegram.messenger.camera.CameraView;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.PhotoAttachCameraCell;
import org.telegram.ui.Cells.PhotoAttachPhotoCell;
import org.telegram.ui.Cells.ShadowSectionCell;

public class ChatAttachAlert extends BottomSheet implements NotificationCenter.NotificationCenterDelegate, BottomSheet.BottomSheetDelegateInterface {
   private static ArrayList cameraPhotos = new ArrayList();
   private static int lastImageId = -1;
   private static boolean mediaFromExternalCamera;
   private static HashMap selectedPhotos = new HashMap();
   private static ArrayList selectedPhotosOrder = new ArrayList();
   private ChatAttachAlert.ListAdapter adapter;
   private int[] animateCameraValues;
   private ArrayList attachButtons = new ArrayList();
   private LinearLayoutManager attachPhotoLayoutManager;
   private RecyclerListView attachPhotoRecyclerView;
   private ViewGroup attachView;
   private BaseFragment baseFragment;
   private boolean buttonPressed;
   private boolean cameraAnimationInProgress;
   private ChatAttachAlert.PhotoAttachAdapter cameraAttachAdapter;
   private FrameLayout cameraIcon;
   private ImageView cameraImageView;
   private boolean cameraInitied;
   private float cameraOpenProgress;
   private boolean cameraOpened;
   private FrameLayout cameraPanel;
   private LinearLayoutManager cameraPhotoLayoutManager;
   private RecyclerListView cameraPhotoRecyclerView;
   private boolean cameraPhotoRecyclerViewIgnoreLayout;
   private CameraView cameraView;
   private int[] cameraViewLocation;
   private int cameraViewOffsetX;
   private int cameraViewOffsetY;
   private boolean cancelTakingPhotos;
   private Paint ciclePaint = new Paint(1);
   private TextView counterTextView;
   private int currentAccount;
   private AnimatorSet currentHintAnimation;
   private DecelerateInterpolator decelerateInterpolator;
   private ChatAttachAlert.ChatAttachViewDelegate delegate;
   private boolean deviceHasGoodCamera;
   private boolean dragging;
   private MessageObject editingMessageObject;
   private boolean flashAnimationInProgress;
   private ImageView[] flashModeButton;
   private Runnable hideHintRunnable;
   private boolean hintShowed;
   private TextView hintTextView;
   private boolean ignoreLayout;
   private ArrayList innerAnimators = new ArrayList();
   private DecelerateInterpolator interpolator;
   private float lastY;
   private LinearLayoutManager layoutManager;
   private View lineView;
   private RecyclerListView listView;
   private boolean loading;
   private int maxSelectedPhotos;
   private boolean maybeStartDraging;
   private CorrectlyMeasuringTextView mediaBanTooltip;
   private boolean mediaCaptured;
   private boolean mediaEnabled;
   private boolean openWithFrontFaceCamera;
   private boolean paused;
   private ChatAttachAlert.PhotoAttachAdapter photoAttachAdapter;
   private PhotoViewer.PhotoViewerProvider photoViewerProvider;
   private boolean pressed;
   private EmptyTextProgressView progressView;
   private TextView recordTime;
   private boolean requestingPermissions;
   private boolean revealAnimationInProgress;
   private float revealRadius;
   private int revealX;
   private int revealY;
   private int scrollOffsetY;
   private ChatAttachAlert.AttachButton sendDocumentsButton;
   private ChatAttachAlert.AttachButton sendPhotosButton;
   private Drawable shadowDrawable;
   private ShutterButton shutterButton;
   private ImageView switchCameraButton;
   private boolean takingPhoto;
   private boolean useRevealAnimation;
   private Runnable videoRecordRunnable;
   private int videoRecordTime;
   private int[] viewPosition;
   private View[] views = new View[20];

   public ChatAttachAlert(Context var1, final BaseFragment var2) {
      super(var1, false, 0);
      this.currentAccount = UserConfig.selectedAccount;
      this.mediaEnabled = true;
      this.flashModeButton = new ImageView[2];
      this.cameraViewLocation = new int[2];
      this.viewPosition = new int[2];
      this.animateCameraValues = new int[5];
      this.interpolator = new DecelerateInterpolator(1.5F);
      this.maxSelectedPhotos = -1;
      this.decelerateInterpolator = new DecelerateInterpolator();
      this.loading = true;
      this.photoViewerProvider = new ChatAttachAlert.BasePhotoProvider() {
         public boolean cancelButtonPressed() {
            return false;
         }

         public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3, boolean var4) {
            PhotoAttachPhotoCell var5 = ChatAttachAlert.this.getCellForIndex(var3);
            if (var5 != null) {
               int[] var6 = new int[2];
               var5.getImageView().getLocationInWindow(var6);
               if (VERSION.SDK_INT < 26) {
                  var6[0] -= ChatAttachAlert.this.getLeftInset();
               }

               PhotoViewer.PlaceProviderObject var7 = new PhotoViewer.PlaceProviderObject();
               var7.viewX = var6[0];
               var7.viewY = var6[1];
               var7.parentView = ChatAttachAlert.this.attachPhotoRecyclerView;
               var7.imageReceiver = var5.getImageView().getImageReceiver();
               var7.thumb = var7.imageReceiver.getBitmapSafe();
               var7.scale = var5.getImageView().getScaleX();
               var5.showCheck(false);
               return var7;
            } else {
               return null;
            }
         }

         public ImageReceiver.BitmapHolder getThumbForPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3) {
            PhotoAttachPhotoCell var4 = ChatAttachAlert.this.getCellForIndex(var3);
            return var4 != null ? var4.getImageView().getImageReceiver().getBitmapSafe() : null;
         }

         public void sendButtonPressed(int var1, VideoEditedInfo var2) {
            MediaController.PhotoEntry var3 = ChatAttachAlert.this.getPhotoEntryAtPosition(var1);
            if (var3 != null) {
               var3.editedInfo = var2;
            }

            if (ChatAttachAlert.selectedPhotos.isEmpty() && var3 != null) {
               ChatAttachAlert.this.addToSelectedPhotos(var3, -1);
            }

            ChatAttachAlert.this.delegate.didPressedButton(7);
         }

         public void updatePhotoAtIndex(int var1) {
            PhotoAttachPhotoCell var2 = ChatAttachAlert.this.getCellForIndex(var1);
            if (var2 != null) {
               var2.getImageView().setOrientation(0, true);
               MediaController.PhotoEntry var3 = ChatAttachAlert.this.getPhotoEntryAtPosition(var1);
               if (var3 == null) {
                  return;
               }

               if (var3.thumbPath != null) {
                  var2.getImageView().setImage(var3.thumbPath, (String)null, var2.getContext().getResources().getDrawable(2131165697));
               } else if (var3.path != null) {
                  var2.getImageView().setOrientation(var3.orientation, true);
                  if (var3.isVideo) {
                     BackupImageView var4 = var2.getImageView();
                     StringBuilder var5 = new StringBuilder();
                     var5.append("vthumb://");
                     var5.append(var3.imageId);
                     var5.append(":");
                     var5.append(var3.path);
                     var4.setImage(var5.toString(), (String)null, var2.getContext().getResources().getDrawable(2131165697));
                  } else {
                     BackupImageView var7 = var2.getImageView();
                     StringBuilder var6 = new StringBuilder();
                     var6.append("thumb://");
                     var6.append(var3.imageId);
                     var6.append(":");
                     var6.append(var3.path);
                     var7.setImage(var6.toString(), (String)null, var2.getContext().getResources().getDrawable(2131165697));
                  }
               } else {
                  var2.getImageView().setImageResource(2131165697);
               }
            }

         }

         public void willHidePhotoViewer() {
            int var1 = ChatAttachAlert.this.attachPhotoRecyclerView.getChildCount();

            for(int var2 = 0; var2 < var1; ++var2) {
               View var3 = ChatAttachAlert.this.attachPhotoRecyclerView.getChildAt(var2);
               if (var3 instanceof PhotoAttachPhotoCell) {
                  ((PhotoAttachPhotoCell)var3).showCheck(true);
               }
            }

         }

         public void willSwitchFromPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3) {
            PhotoAttachPhotoCell var4 = ChatAttachAlert.this.getCellForIndex(var3);
            if (var4 != null) {
               var4.showCheck(true);
            }

         }
      };
      this.baseFragment = var2;
      this.ciclePaint.setColor(Theme.getColor("dialogBackground"));
      this.setDelegate(this);
      this.setUseRevealAnimation(true);
      this.checkCamera(false);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.albumsDidLoad);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.reloadInlineHints);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.cameraInitied);
      this.shadowDrawable = var1.getResources().getDrawable(2131165823).mutate();
      Theme.setDrawableColor(this.shadowDrawable, Theme.getColor("dialogBackground"));
      RecyclerListView var3 = new RecyclerListView(var1) {
         private int lastHeight;
         private int lastWidth;

         public void onDraw(Canvas var1) {
            if (ChatAttachAlert.this.useRevealAnimation && VERSION.SDK_INT <= 19) {
               var1.save();
               var1.clipRect(ChatAttachAlert.access$3300(ChatAttachAlert.this), ChatAttachAlert.this.scrollOffsetY, this.getMeasuredWidth() - ChatAttachAlert.access$3400(ChatAttachAlert.this), this.getMeasuredHeight());
               if (ChatAttachAlert.this.revealAnimationInProgress) {
                  var1.drawCircle((float)ChatAttachAlert.this.revealX, (float)ChatAttachAlert.this.revealY, ChatAttachAlert.this.revealRadius, ChatAttachAlert.this.ciclePaint);
               } else {
                  var1.drawRect((float)ChatAttachAlert.access$4000(ChatAttachAlert.this), (float)ChatAttachAlert.this.scrollOffsetY, (float)(this.getMeasuredWidth() - ChatAttachAlert.access$4100(ChatAttachAlert.this)), (float)this.getMeasuredHeight(), ChatAttachAlert.this.ciclePaint);
               }

               var1.restore();
            } else {
               ChatAttachAlert.this.shadowDrawable.setBounds(0, ChatAttachAlert.this.scrollOffsetY - ChatAttachAlert.access$4200(ChatAttachAlert.this), this.getMeasuredWidth(), this.getMeasuredHeight());
               ChatAttachAlert.this.shadowDrawable.draw(var1);
            }

         }

         public boolean onInterceptTouchEvent(MotionEvent var1) {
            if (ChatAttachAlert.this.cameraAnimationInProgress) {
               return true;
            } else if (ChatAttachAlert.this.cameraOpened) {
               return ChatAttachAlert.this.processTouchEvent(var1);
            } else if (var1.getAction() == 0 && ChatAttachAlert.this.scrollOffsetY != 0 && var1.getY() < (float)ChatAttachAlert.this.scrollOffsetY) {
               ChatAttachAlert.this.dismiss();
               return true;
            } else {
               return super.onInterceptTouchEvent(var1);
            }
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            int var6;
            int var9;
            int var10;
            label25: {
               var6 = var5 - var3;
               if (ChatAttachAlert.this.listView.getChildCount() > 0) {
                  View var7 = ChatAttachAlert.this.listView.getChildAt(ChatAttachAlert.this.listView.getChildCount() - 1);
                  RecyclerListView.Holder var8 = (RecyclerListView.Holder)ChatAttachAlert.this.listView.findContainingViewHolder(var7);
                  if (var8 != null) {
                     var9 = var8.getAdapterPosition();
                     var10 = var7.getTop();
                     break label25;
                  }
               }

               var10 = 0;
               var9 = -1;
            }

            label20: {
               if (var9 >= 0) {
                  int var11 = this.lastHeight;
                  if (var6 - var11 != 0) {
                     var10 = var10 + var6 - var11 - this.getPaddingTop();
                     break label20;
                  }
               }

               var10 = 0;
               var9 = -1;
            }

            super.onLayout(var1, var2, var3, var4, var5);
            if (var9 != -1) {
               ChatAttachAlert.this.ignoreLayout = true;
               ChatAttachAlert.this.layoutManager.scrollToPositionWithOffset(var9, var10);
               super.onLayout(false, var2, var3, var4, var5);
               ChatAttachAlert.this.ignoreLayout = false;
            }

            this.lastHeight = var6;
            this.lastWidth = var4 - var2;
            ChatAttachAlert.this.updateLayout();
            ChatAttachAlert.this.checkCameraViewPosition();
         }

         protected void onMeasure(int var1, int var2) {
            var2 = MeasureSpec.getSize(var2);
            int var3 = var2;
            if (VERSION.SDK_INT >= 21) {
               var3 = var2 - AndroidUtilities.statusBarHeight;
            }

            short var7;
            if (ChatAttachAlert.this.baseFragment instanceof ChatActivity) {
               var7 = 298;
            } else {
               var7 = 203;
            }

            int var4 = ChatAttachAlert.access$2400(ChatAttachAlert.this);
            float var5 = (float)var7;
            int var6 = AndroidUtilities.dp(var5);
            if (DataQuery.getInstance(ChatAttachAlert.this.currentAccount).inlineBots.isEmpty()) {
               var2 = 0;
            } else {
               var2 = (int)Math.ceil((double)((float)DataQuery.getInstance(ChatAttachAlert.this.currentAccount).inlineBots.size() / 4.0F)) * AndroidUtilities.dp(100.0F) + AndroidUtilities.dp(12.0F);
            }

            var6 = var4 + var6 + var2;
            if (var6 == AndroidUtilities.dp(var5)) {
               var4 = 0;
            } else {
               var4 = Math.max(0, var3 - AndroidUtilities.dp(var5));
            }

            var2 = var4;
            if (var4 != 0) {
               var2 = var4;
               if (var6 < var3) {
                  var2 = var4 - (var3 - var6);
               }
            }

            var4 = var2;
            if (var2 == 0) {
               var4 = ChatAttachAlert.access$2500(ChatAttachAlert.this);
            }

            if (this.getPaddingTop() != var4) {
               ChatAttachAlert.this.ignoreLayout = true;
               this.setPadding(ChatAttachAlert.access$2600(ChatAttachAlert.this), var4, ChatAttachAlert.access$2700(ChatAttachAlert.this), 0);
               ChatAttachAlert.this.ignoreLayout = false;
            }

            super.onMeasure(var1, MeasureSpec.makeMeasureSpec(Math.min(var6, var3), 1073741824));
         }

         public boolean onTouchEvent(MotionEvent var1) {
            boolean var2 = ChatAttachAlert.this.cameraAnimationInProgress;
            boolean var3 = true;
            if (var2) {
               return true;
            } else if (ChatAttachAlert.this.cameraOpened) {
               return ChatAttachAlert.this.processTouchEvent(var1);
            } else {
               if (ChatAttachAlert.this.isDismissed() || !super.onTouchEvent(var1)) {
                  var3 = false;
               }

               return var3;
            }
         }

         public void requestLayout() {
            if (!ChatAttachAlert.this.ignoreLayout) {
               super.requestLayout();
            }
         }

         public void setTranslationY(float var1) {
            super.setTranslationY(var1);
            ChatAttachAlert.this.checkCameraViewPosition();
         }
      };
      this.listView = var3;
      super.containerView = var3;
      var3 = this.listView;
      super.nestedScrollChild = var3;
      var3.setWillNotDraw(false);
      this.listView.setClipToPadding(false);
      RecyclerListView var4 = this.listView;
      LinearLayoutManager var14 = new LinearLayoutManager(this.getContext());
      this.layoutManager = var14;
      var4.setLayoutManager(var14);
      this.layoutManager.setOrientation(1);
      var3 = this.listView;
      ChatAttachAlert.ListAdapter var15 = new ChatAttachAlert.ListAdapter(var1);
      this.adapter = var15;
      var3.setAdapter(var15);
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setEnabled(true);
      this.listView.setGlowColor(Theme.getColor("dialogScrollGlow"));
      this.listView.addItemDecoration(new RecyclerView.ItemDecoration() {
         public void getItemOffsets(android.graphics.Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
            var1.left = 0;
            var1.right = 0;
            var1.top = 0;
            var1.bottom = 0;
         }
      });
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrolled(RecyclerView var1, int var2, int var3) {
            if (ChatAttachAlert.this.listView.getChildCount() > 0) {
               if (ChatAttachAlert.this.hintShowed && ChatAttachAlert.this.layoutManager.findLastVisibleItemPosition() > 1) {
                  ChatAttachAlert.this.hideHint();
                  ChatAttachAlert.this.hintShowed = false;
                  MessagesController.getGlobalMainSettings().edit().putBoolean("bothint", true).commit();
               }

               ChatAttachAlert.this.updateLayout();
               ChatAttachAlert.this.checkCameraViewPosition();
            }
         }
      });
      ViewGroup var16 = super.containerView;
      int var5 = super.backgroundPaddingLeft;
      var16.setPadding(var5, 0, var5, 0);
      super.containerView.setImportantForAccessibility(2);
      this.attachView = new FrameLayout(var1) {
         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            var4 -= var2;
            var5 -= var3;
            var3 = AndroidUtilities.dp(8.0F);
            RecyclerListView var6 = ChatAttachAlert.this.attachPhotoRecyclerView;
            int var7 = ChatAttachAlert.this.attachPhotoRecyclerView.getMeasuredHeight();
            var2 = 0;
            var6.layout(0, var3, var4, var7 + var3);
            ChatAttachAlert.this.progressView.layout(0, var3, var4, ChatAttachAlert.this.progressView.getMeasuredHeight() + var3);
            ChatAttachAlert.this.lineView.layout(0, AndroidUtilities.dp(96.0F), var4, AndroidUtilities.dp(96.0F) + ChatAttachAlert.this.lineView.getMeasuredHeight());
            ChatAttachAlert.this.hintTextView.layout(var4 - ChatAttachAlert.this.hintTextView.getMeasuredWidth() - AndroidUtilities.dp(5.0F), var5 - ChatAttachAlert.this.hintTextView.getMeasuredHeight() - AndroidUtilities.dp(5.0F), var4 - AndroidUtilities.dp(5.0F), var5 - AndroidUtilities.dp(5.0F));
            var5 = (var4 - ChatAttachAlert.this.mediaBanTooltip.getMeasuredWidth()) / 2;
            var3 += (ChatAttachAlert.this.attachPhotoRecyclerView.getMeasuredHeight() - ChatAttachAlert.this.mediaBanTooltip.getMeasuredHeight()) / 2;
            ChatAttachAlert.this.mediaBanTooltip.layout(var5, var3, ChatAttachAlert.this.mediaBanTooltip.getMeasuredWidth() + var5, ChatAttachAlert.this.mediaBanTooltip.getMeasuredHeight() + var3);
            var4 = (var4 - AndroidUtilities.dp(360.0F)) / 3;

            for(var3 = 0; var2 < 8; ++var2) {
               if (ChatAttachAlert.this.views[var2] != null) {
                  var5 = AndroidUtilities.dp((float)(var3 / 4 * 97 + 105));
                  var7 = AndroidUtilities.dp(10.0F) + var3 % 4 * (AndroidUtilities.dp(85.0F) + var4);
                  ChatAttachAlert.this.views[var2].layout(var7, var5, ChatAttachAlert.this.views[var2].getMeasuredWidth() + var7, ChatAttachAlert.this.views[var2].getMeasuredHeight() + var5);
                  ++var3;
               }
            }

         }

         protected void onMeasure(int var1, int var2) {
            if (ChatAttachAlert.this.baseFragment instanceof ChatActivity) {
               super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(298.0F), 1073741824));
            } else {
               super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(203.0F), 1073741824));
            }

         }
      };
      View[] var17 = this.views;
      var3 = new RecyclerListView(var1);
      this.attachPhotoRecyclerView = var3;
      var17[8] = var3;
      this.attachPhotoRecyclerView.setVerticalScrollBarEnabled(true);
      var4 = this.attachPhotoRecyclerView;
      ChatAttachAlert.PhotoAttachAdapter var18 = new ChatAttachAlert.PhotoAttachAdapter(var1, true);
      this.photoAttachAdapter = var18;
      var4.setAdapter(var18);
      this.attachPhotoRecyclerView.setClipToPadding(false);
      this.attachPhotoRecyclerView.setPadding(AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(8.0F), 0);
      this.attachPhotoRecyclerView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.attachPhotoRecyclerView.setLayoutAnimation((LayoutAnimationController)null);
      this.attachPhotoRecyclerView.setOverScrollMode(2);
      this.attachView.addView(this.attachPhotoRecyclerView, LayoutHelper.createFrame(-1, 80.0F));
      this.attachPhotoLayoutManager = new LinearLayoutManager(var1) {
         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.attachPhotoLayoutManager.setOrientation(0);
      this.attachPhotoRecyclerView.setLayoutManager(this.attachPhotoLayoutManager);
      this.attachPhotoRecyclerView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$ChatAttachAlert$gSlgqD7A9IVmfNd3sjxwTiZ76jE(this)));
      this.attachPhotoRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrolled(RecyclerView var1, int var2, int var3) {
            ChatAttachAlert.this.checkCameraViewPosition();
         }
      });
      var17 = this.views;
      CorrectlyMeasuringTextView var19 = new CorrectlyMeasuringTextView(var1);
      this.mediaBanTooltip = var19;
      var17[11] = var19;
      this.mediaBanTooltip.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0F), Theme.getColor("chat_attachMediaBanBackground")));
      this.mediaBanTooltip.setTextColor(Theme.getColor("chat_attachMediaBanText"));
      this.mediaBanTooltip.setPadding(AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F));
      this.mediaBanTooltip.setGravity(16);
      this.mediaBanTooltip.setTextSize(1, 14.0F);
      this.mediaBanTooltip.setVisibility(4);
      this.attachView.addView(this.mediaBanTooltip, LayoutHelper.createFrame(-2, -2.0F, 51, 14.0F, 0.0F, 14.0F, 0.0F));
      var17 = this.views;
      EmptyTextProgressView var20 = new EmptyTextProgressView(var1);
      this.progressView = var20;
      var17[9] = var20;
      if (VERSION.SDK_INT >= 23 && this.getContext().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
         this.progressView.setText(LocaleController.getString("PermissionStorage", 2131560420));
         this.progressView.setTextSize(16);
      } else {
         this.progressView.setText(LocaleController.getString("NoPhotos", 2131559937));
         this.progressView.setTextSize(20);
      }

      this.attachView.addView(this.progressView, LayoutHelper.createFrame(-1, 80.0F));
      this.attachPhotoRecyclerView.setEmptyView(this.progressView);
      View[] var21 = this.views;
      View var22 = new View(this.getContext()) {
         public boolean hasOverlappingRendering() {
            return false;
         }
      };
      this.lineView = var22;
      var21[10] = var22;
      this.lineView.setBackgroundColor(Theme.getColor("dialogGrayLine"));
      this.attachView.addView(this.lineView, new LayoutParams(-1, 1, 51));
      String var6 = LocaleController.getString("ChatCamera", 2131559025);
      String var7 = LocaleController.getString("ChatGallery", 2131559028);
      String var8 = LocaleController.getString("ChatVideo", 2131559044);
      String var23 = LocaleController.getString("AttachMusic", 2131558726);
      String var9 = LocaleController.getString("ChatDocument", 2131559027);
      String var24 = LocaleController.getString("AttachContact", 2131558711);
      String var10 = LocaleController.getString("ChatLocation", 2131559042);

      for(var5 = 0; var5 < 8; ++var5) {
         if (this.baseFragment instanceof ChatActivity || var5 != 2 && var5 != 3 && var5 != 5 && var5 != 6) {
            ChatAttachAlert.AttachButton var11 = new ChatAttachAlert.AttachButton(var1);
            this.attachButtons.add(var11);
            var11.setTextAndIcon((new CharSequence[]{var6, var7, var8, var23, var9, var24, var10, ""})[var5], Theme.chat_attachButtonDrawables[var5]);
            this.attachView.addView(var11, LayoutHelper.createFrame(85, 91, 51));
            var11.setTag(var5);
            this.views[var5] = var11;
            if (var5 == 7) {
               this.sendPhotosButton = var11;
               this.sendPhotosButton.imageView.setPadding(0, AndroidUtilities.dp(4.0F), 0, 0);
            } else if (var5 == 4) {
               this.sendDocumentsButton = var11;
            }

            var11.setOnClickListener(new _$$Lambda$ChatAttachAlert$0NyE8I5jQF6_Q8wSGheAU5ZO3eo(this, var2));
         }
      }

      this.hintTextView = new TextView(var1);
      this.hintTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0F), Theme.getColor("chat_gifSaveHintBackground")));
      this.hintTextView.setTextColor(Theme.getColor("chat_gifSaveHintText"));
      this.hintTextView.setTextSize(1, 14.0F);
      this.hintTextView.setPadding(AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp(10.0F), 0);
      this.hintTextView.setText(LocaleController.getString("AttachBotsHelp", 2131558710));
      this.hintTextView.setGravity(16);
      this.hintTextView.setVisibility(4);
      this.hintTextView.setCompoundDrawablesWithIntrinsicBounds(2131165807, 0, 0, 0);
      this.hintTextView.setCompoundDrawablePadding(AndroidUtilities.dp(8.0F));
      this.attachView.addView(this.hintTextView, LayoutHelper.createFrame(-2, 32.0F, 85, 5.0F, 0.0F, 5.0F, 5.0F));
      if (this.loading) {
         this.progressView.showProgress();
      } else {
         this.progressView.showTextView();
      }

      this.recordTime = new TextView(var1);
      this.recordTime.setBackgroundResource(2131165871);
      this.recordTime.getBackground().setColorFilter(new PorterDuffColorFilter(1711276032, Mode.MULTIPLY));
      this.recordTime.setTextSize(1, 15.0F);
      this.recordTime.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.recordTime.setAlpha(0.0F);
      this.recordTime.setTextColor(-1);
      this.recordTime.setPadding(AndroidUtilities.dp(10.0F), AndroidUtilities.dp(5.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(5.0F));
      super.container.addView(this.recordTime, LayoutHelper.createFrame(-2, -2.0F, 49, 0.0F, 16.0F, 0.0F, 0.0F));
      this.cameraPanel = new FrameLayout(var1) {
         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            var3 = this.getMeasuredWidth() / 2;
            var2 = this.getMeasuredHeight() / 2;
            ChatAttachAlert.this.shutterButton.layout(var3 - ChatAttachAlert.this.shutterButton.getMeasuredWidth() / 2, var2 - ChatAttachAlert.this.shutterButton.getMeasuredHeight() / 2, ChatAttachAlert.this.shutterButton.getMeasuredWidth() / 2 + var3, ChatAttachAlert.this.shutterButton.getMeasuredHeight() / 2 + var2);
            if (this.getMeasuredWidth() == AndroidUtilities.dp(100.0F)) {
               var4 = this.getMeasuredWidth() / 2;
               var3 = var2 / 2;
               var5 = var2 + var3 + AndroidUtilities.dp(17.0F);
               var3 -= AndroidUtilities.dp(17.0F);
               var2 = var4;
            } else {
               var2 = var3 / 2;
               var4 = var3 + var2 + AndroidUtilities.dp(17.0F);
               var5 = AndroidUtilities.dp(17.0F);
               var3 = this.getMeasuredHeight() / 2;
               var2 -= var5;
               var5 = var3;
            }

            ChatAttachAlert.this.switchCameraButton.layout(var4 - ChatAttachAlert.this.switchCameraButton.getMeasuredWidth() / 2, var5 - ChatAttachAlert.this.switchCameraButton.getMeasuredHeight() / 2, var4 + ChatAttachAlert.this.switchCameraButton.getMeasuredWidth() / 2, var5 + ChatAttachAlert.this.switchCameraButton.getMeasuredHeight() / 2);

            for(var4 = 0; var4 < 2; ++var4) {
               ChatAttachAlert.this.flashModeButton[var4].layout(var2 - ChatAttachAlert.this.flashModeButton[var4].getMeasuredWidth() / 2, var3 - ChatAttachAlert.this.flashModeButton[var4].getMeasuredHeight() / 2, ChatAttachAlert.this.flashModeButton[var4].getMeasuredWidth() / 2 + var2, ChatAttachAlert.this.flashModeButton[var4].getMeasuredHeight() / 2 + var3);
            }

         }
      };
      this.cameraPanel.setVisibility(8);
      this.cameraPanel.setAlpha(0.0F);
      super.container.addView(this.cameraPanel, LayoutHelper.createFrame(-1, 100, 83));
      this.counterTextView = new TextView(var1);
      this.counterTextView.setBackgroundResource(2131165759);
      this.counterTextView.setVisibility(8);
      this.counterTextView.setTextColor(-1);
      this.counterTextView.setGravity(17);
      this.counterTextView.setPivotX(0.0F);
      this.counterTextView.setPivotY(0.0F);
      this.counterTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.counterTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 2131165756, 0);
      this.counterTextView.setCompoundDrawablePadding(AndroidUtilities.dp(4.0F));
      this.counterTextView.setPadding(AndroidUtilities.dp(16.0F), 0, AndroidUtilities.dp(16.0F), 0);
      super.container.addView(this.counterTextView, LayoutHelper.createFrame(-2, 38.0F, 51, 0.0F, 0.0F, 0.0F, 116.0F));
      this.counterTextView.setOnClickListener(new _$$Lambda$ChatAttachAlert$70irb8b3j6E6JPtp7E_VZic7gT0(this));
      this.shutterButton = new ShutterButton(var1);
      this.cameraPanel.addView(this.shutterButton, LayoutHelper.createFrame(84, 84, 17));
      this.shutterButton.setDelegate(new ShutterButton.ShutterButtonDelegate() {
         private File outputFile;

         // $FF: synthetic method
         public void lambda$shutterLongPressed$0$ChatAttachAlert$10() {
            if (ChatAttachAlert.this.videoRecordRunnable != null) {
               ChatAttachAlert.access$6008(ChatAttachAlert.this);
               ChatAttachAlert.this.recordTime.setText(String.format("%02d:%02d", ChatAttachAlert.this.videoRecordTime / 60, ChatAttachAlert.this.videoRecordTime % 60));
               AndroidUtilities.runOnUIThread(ChatAttachAlert.this.videoRecordRunnable, 1000L);
            }
         }

         // $FF: synthetic method
         public void lambda$shutterLongPressed$1$ChatAttachAlert$10(String var1, long var2x) {
            if (this.outputFile != null && ChatAttachAlert.this.baseFragment != null) {
               ChatAttachAlert.mediaFromExternalCamera = false;
               MediaController.PhotoEntry var4 = new MediaController.PhotoEntry(0, ChatAttachAlert.access$6410(), 0L, this.outputFile.getAbsolutePath(), 0, true);
               var4.duration = (int)var2x;
               var4.thumbPath = var1;
               ChatAttachAlert.this.openPhotoViewer(var4, false, false);
            }

         }

         // $FF: synthetic method
         public void lambda$shutterLongPressed$2$ChatAttachAlert$10() {
            AndroidUtilities.runOnUIThread(ChatAttachAlert.this.videoRecordRunnable, 1000L);
         }

         // $FF: synthetic method
         public void lambda$shutterReleased$3$ChatAttachAlert$10(File var1, boolean var2x) {
            ChatAttachAlert.this.takingPhoto = false;
            if (var1 != null && ChatAttachAlert.this.baseFragment != null) {
               short var4;
               label26: {
                  int var7;
                  try {
                     ExifInterface var3 = new ExifInterface(var1.getAbsolutePath());
                     var7 = var3.getAttributeInt("Orientation", 1);
                  } catch (Exception var5) {
                     FileLog.e((Throwable)var5);
                     var4 = 0;
                     break label26;
                  }

                  if (var7 != 3) {
                     if (var7 != 6) {
                        if (var7 != 8) {
                           var4 = 0;
                        } else {
                           var4 = 270;
                        }
                     } else {
                        var4 = 90;
                     }
                  } else {
                     var4 = 180;
                  }
               }

               ChatAttachAlert.mediaFromExternalCamera = false;
               MediaController.PhotoEntry var6 = new MediaController.PhotoEntry(0, ChatAttachAlert.access$6410(), 0L, var1.getAbsolutePath(), var4, false);
               var6.canDeleteAfter = true;
               ChatAttachAlert.this.openPhotoViewer(var6, var2x, false);
            }

         }

         public void shutterCancel() {
            if (!ChatAttachAlert.this.mediaCaptured) {
               File var1 = this.outputFile;
               if (var1 != null) {
                  var1.delete();
                  this.outputFile = null;
               }

               ChatAttachAlert.this.resetRecordState();
               CameraController.getInstance().stopVideoRecording(ChatAttachAlert.this.cameraView.getCameraSession(), true);
            }
         }

         public boolean shutterLongPressed() {
            boolean var1 = ChatAttachAlert.this.baseFragment instanceof ChatActivity;
            Integer var2x = 0;
            if (var1 && !ChatAttachAlert.this.mediaCaptured && !ChatAttachAlert.this.takingPhoto && ChatAttachAlert.this.baseFragment != null && ChatAttachAlert.this.baseFragment.getParentActivity() != null && ChatAttachAlert.this.cameraView != null) {
               if (VERSION.SDK_INT >= 23 && ChatAttachAlert.this.baseFragment.getParentActivity().checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
                  ChatAttachAlert.this.requestingPermissions = true;
                  ChatAttachAlert.this.baseFragment.getParentActivity().requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 21);
                  return false;
               } else {
                  for(int var3 = 0; var3 < 2; ++var3) {
                     ChatAttachAlert.this.flashModeButton[var3].setAlpha(0.0F);
                  }

                  ChatAttachAlert.this.switchCameraButton.setAlpha(0.0F);
                  if (ChatAttachAlert.this.baseFragment instanceof ChatActivity && ((ChatActivity)ChatAttachAlert.this.baseFragment).isSecretChat()) {
                     var1 = true;
                  } else {
                     var1 = false;
                  }

                  this.outputFile = AndroidUtilities.generateVideoPath(var1);
                  ChatAttachAlert.this.recordTime.setAlpha(1.0F);
                  ChatAttachAlert.this.recordTime.setText(String.format("%02d:%02d", var2x, var2x));
                  ChatAttachAlert.this.videoRecordTime = 0;
                  ChatAttachAlert.this.videoRecordRunnable = new _$$Lambda$ChatAttachAlert$10$ZeL0eTEJEk_Sg8NguIkwCCYWIW0(this);
                  AndroidUtilities.lockOrientation(var2.getParentActivity());
                  CameraController.getInstance().recordVideo(ChatAttachAlert.this.cameraView.getCameraSession(), this.outputFile, new _$$Lambda$ChatAttachAlert$10$uU9sWMugjD7x3FTSol0pBdhggvw(this), new _$$Lambda$ChatAttachAlert$10$yg9RMHiEdmJNgfGykAnz6vx1P_Q(this));
                  ChatAttachAlert.this.shutterButton.setState(ShutterButton.State.RECORDING, true);
                  return true;
               }
            } else {
               return false;
            }
         }

         public void shutterReleased() {
            if (!ChatAttachAlert.this.takingPhoto && ChatAttachAlert.this.cameraView != null && !ChatAttachAlert.this.mediaCaptured && ChatAttachAlert.this.cameraView.getCameraSession() != null) {
               ChatAttachAlert var1 = ChatAttachAlert.this;
               boolean var2x = true;
               var1.mediaCaptured = true;
               if (ChatAttachAlert.this.shutterButton.getState() == ShutterButton.State.RECORDING) {
                  ChatAttachAlert.this.resetRecordState();
                  CameraController.getInstance().stopVideoRecording(ChatAttachAlert.this.cameraView.getCameraSession(), false);
                  ChatAttachAlert.this.shutterButton.setState(ShutterButton.State.DEFAULT, true);
                  return;
               }

               if (!(ChatAttachAlert.this.baseFragment instanceof ChatActivity) || !((ChatActivity)ChatAttachAlert.this.baseFragment).isSecretChat()) {
                  var2x = false;
               }

               File var3 = AndroidUtilities.generatePicturePath(var2x);
               var2x = ChatAttachAlert.this.cameraView.getCameraSession().isSameTakePictureOrientation();
               ChatAttachAlert.this.cameraView.getCameraSession().setFlipFront(var2 instanceof ChatActivity);
               ChatAttachAlert.this.takingPhoto = CameraController.getInstance().takePicture(var3, ChatAttachAlert.this.cameraView.getCameraSession(), new _$$Lambda$ChatAttachAlert$10$spC8SeqMVNyiHnBN2HSRZFkemxQ(this, var3, var2x));
            }

         }
      });
      this.shutterButton.setFocusable(true);
      this.shutterButton.setContentDescription(LocaleController.getString("AccDescrShutter", 2131558474));
      this.switchCameraButton = new ImageView(var1);
      this.switchCameraButton.setScaleType(ScaleType.CENTER);
      this.cameraPanel.addView(this.switchCameraButton, LayoutHelper.createFrame(48, 48, 21));
      this.switchCameraButton.setOnClickListener(new _$$Lambda$ChatAttachAlert$ZqUzbu7OWYZNWU7ujZFdDlGIODQ(this));
      this.switchCameraButton.setContentDescription(LocaleController.getString("AccDescrSwitchCamera", 2131558478));

      for(var5 = 0; var5 < 2; ++var5) {
         this.flashModeButton[var5] = new ImageView(var1);
         this.flashModeButton[var5].setScaleType(ScaleType.CENTER);
         this.flashModeButton[var5].setVisibility(4);
         this.cameraPanel.addView(this.flashModeButton[var5], LayoutHelper.createFrame(48, 48, 51));
         this.flashModeButton[var5].setOnClickListener(new _$$Lambda$ChatAttachAlert$N7eKeWXf4VQLw62DCiXcluGuuq0(this));
         ImageView var25 = this.flashModeButton[var5];
         StringBuilder var12 = new StringBuilder();
         var12.append("flash mode ");
         var12.append(var5);
         var25.setContentDescription(var12.toString());
      }

      this.cameraPhotoRecyclerView = new RecyclerListView(var1) {
         public void requestLayout() {
            if (!ChatAttachAlert.this.cameraPhotoRecyclerViewIgnoreLayout) {
               super.requestLayout();
            }
         }
      };
      this.cameraPhotoRecyclerView.setVerticalScrollBarEnabled(true);
      var3 = this.cameraPhotoRecyclerView;
      ChatAttachAlert.PhotoAttachAdapter var13 = new ChatAttachAlert.PhotoAttachAdapter(var1, false);
      this.cameraAttachAdapter = var13;
      var3.setAdapter(var13);
      this.cameraPhotoRecyclerView.setClipToPadding(false);
      this.cameraPhotoRecyclerView.setPadding(AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(8.0F), 0);
      this.cameraPhotoRecyclerView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.cameraPhotoRecyclerView.setLayoutAnimation((LayoutAnimationController)null);
      this.cameraPhotoRecyclerView.setOverScrollMode(2);
      this.cameraPhotoRecyclerView.setVisibility(4);
      this.cameraPhotoRecyclerView.setAlpha(0.0F);
      super.container.addView(this.cameraPhotoRecyclerView, LayoutHelper.createFrame(-1, 80.0F));
      this.cameraPhotoLayoutManager = new LinearLayoutManager(var1, 0, false) {
         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.cameraPhotoRecyclerView.setLayoutManager(this.cameraPhotoLayoutManager);
      this.cameraPhotoRecyclerView.setOnItemClickListener((RecyclerListView.OnItemClickListener)_$$Lambda$ChatAttachAlert$GA9KqfMlT5_LL6qJw22nnzE_Xs8.INSTANCE);
   }

   // $FF: synthetic method
   static int access$2400(ChatAttachAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2500(ChatAttachAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2600(ChatAttachAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$2700(ChatAttachAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$3300(ChatAttachAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$3400(ChatAttachAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$4000(ChatAttachAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$4100(ChatAttachAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$4200(ChatAttachAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$6008(ChatAttachAlert var0) {
      int var1 = var0.videoRecordTime++;
      return var1;
   }

   // $FF: synthetic method
   static int access$6410() {
      int var0 = lastImageId--;
      return var0;
   }

   // $FF: synthetic method
   static AnimatorSet access$8600(ChatAttachAlert var0) {
      return var0.currentSheetAnimation;
   }

   // $FF: synthetic method
   static AnimatorSet access$8700(ChatAttachAlert var0) {
      return var0.currentSheetAnimation;
   }

   // $FF: synthetic method
   static AnimatorSet access$8802(ChatAttachAlert var0, AnimatorSet var1) {
      var0.currentSheetAnimation = var1;
      return var1;
   }

   // $FF: synthetic method
   static ViewGroup access$9000(ChatAttachAlert var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static ViewGroup access$9100(ChatAttachAlert var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static AnimatorSet access$9200(ChatAttachAlert var0) {
      return var0.currentSheetAnimation;
   }

   // $FF: synthetic method
   static AnimatorSet access$9302(ChatAttachAlert var0, AnimatorSet var1) {
      var0.currentSheetAnimation = var1;
      return var1;
   }

   // $FF: synthetic method
   static boolean access$9502(ChatAttachAlert var0, boolean var1) {
      var0.flashAnimationInProgress = var1;
      return var1;
   }

   private int addToSelectedPhotos(MediaController.PhotoEntry var1, int var2) {
      Integer var3 = var1.imageId;
      if (selectedPhotos.containsKey(var3)) {
         selectedPhotos.remove(var3);
         int var4 = selectedPhotosOrder.indexOf(var3);
         if (var4 >= 0) {
            selectedPhotosOrder.remove(var4);
         }

         this.updatePhotosCounter();
         this.updateCheckedPhotoIndices();
         if (var2 >= 0) {
            var1.reset();
            this.photoViewerProvider.updatePhotoAtIndex(var2);
         }

         return var4;
      } else {
         selectedPhotos.put(var3, var1);
         selectedPhotosOrder.add(var3);
         this.updatePhotosCounter();
         return -1;
      }
   }

   private void applyCameraViewPosition() {
      CameraView var1 = this.cameraView;
      if (var1 != null) {
         if (!this.cameraOpened) {
            var1.setTranslationX((float)this.cameraViewLocation[0]);
            this.cameraView.setTranslationY((float)this.cameraViewLocation[1]);
         }

         this.cameraIcon.setTranslationX((float)this.cameraViewLocation[0]);
         this.cameraIcon.setTranslationY((float)this.cameraViewLocation[1]);
         int var2 = AndroidUtilities.dp(80.0F) - this.cameraViewOffsetX;
         int var3 = AndroidUtilities.dp(80.0F) - this.cameraViewOffsetY;
         LayoutParams var4;
         if (!this.cameraOpened) {
            this.cameraView.setClipLeft(this.cameraViewOffsetX);
            this.cameraView.setClipTop(this.cameraViewOffsetY);
            var4 = (LayoutParams)this.cameraView.getLayoutParams();
            if (var4.height != var3 || var4.width != var2) {
               var4.width = var2;
               var4.height = var3;
               this.cameraView.setLayoutParams(var4);
               AndroidUtilities.runOnUIThread(new _$$Lambda$ChatAttachAlert$9zWNRomA9shsKBOcxwcrmIWHw2o(this, var4));
            }
         }

         var4 = (LayoutParams)this.cameraIcon.getLayoutParams();
         if (var4.height != var3 || var4.width != var2) {
            var4.width = var2;
            var4.height = var3;
            this.cameraIcon.setLayoutParams(var4);
            AndroidUtilities.runOnUIThread(new _$$Lambda$ChatAttachAlert$7zXlVZ2DjXYZ5PDcPaHgdzTaY_Q(this, var4));
         }
      }

   }

   private void checkCameraViewPosition() {
      if (this.deviceHasGoodCamera) {
         int var1 = this.attachPhotoRecyclerView.getChildCount();
         int var2 = 0;

         while(true) {
            if (var2 < var1) {
               View var3 = this.attachPhotoRecyclerView.getChildAt(var2);
               if (!(var3 instanceof PhotoAttachCameraCell)) {
                  ++var2;
                  continue;
               }

               if (VERSION.SDK_INT < 19 || var3.isAttachedToWindow()) {
                  var3.getLocationInWindow(this.cameraViewLocation);
                  int[] var5 = this.cameraViewLocation;
                  var5[0] -= this.getLeftInset();
                  float var4 = this.listView.getX() + (float)super.backgroundPaddingLeft - (float)this.getLeftInset();
                  var5 = this.cameraViewLocation;
                  if ((float)var5[0] < var4) {
                     this.cameraViewOffsetX = (int)(var4 - (float)var5[0]);
                     if (this.cameraViewOffsetX >= AndroidUtilities.dp(80.0F)) {
                        this.cameraViewOffsetX = 0;
                        this.cameraViewLocation[0] = AndroidUtilities.dp(-150.0F);
                        this.cameraViewLocation[1] = 0;
                     } else {
                        var5 = this.cameraViewLocation;
                        var5[0] += this.cameraViewOffsetX;
                     }
                  } else {
                     this.cameraViewOffsetX = 0;
                  }

                  label34: {
                     if (VERSION.SDK_INT >= 21) {
                        var5 = this.cameraViewLocation;
                        var2 = var5[1];
                        var1 = AndroidUtilities.statusBarHeight;
                        if (var2 < var1) {
                           this.cameraViewOffsetY = var1 - var5[1];
                           if (this.cameraViewOffsetY >= AndroidUtilities.dp(80.0F)) {
                              this.cameraViewOffsetY = 0;
                              this.cameraViewLocation[0] = AndroidUtilities.dp(-150.0F);
                              this.cameraViewLocation[1] = 0;
                           } else {
                              var5 = this.cameraViewLocation;
                              var5[1] += this.cameraViewOffsetY;
                           }
                           break label34;
                        }
                     }

                     this.cameraViewOffsetY = 0;
                  }

                  this.applyCameraViewPosition();
                  return;
               }
            }

            this.cameraViewOffsetX = 0;
            this.cameraViewOffsetY = 0;
            this.cameraViewLocation[0] = AndroidUtilities.dp(-150.0F);
            this.cameraViewLocation[1] = 0;
            this.applyCameraViewPosition();
            return;
         }
      }
   }

   private void clearSelectedPhotos() {
      boolean var1 = selectedPhotos.isEmpty();
      byte var2 = 0;
      boolean var4;
      if (!var1) {
         Iterator var3 = selectedPhotos.entrySet().iterator();

         while(var3.hasNext()) {
            ((MediaController.PhotoEntry)((Entry)var3.next()).getValue()).reset();
         }

         selectedPhotos.clear();
         selectedPhotosOrder.clear();
         this.updatePhotosButton();
         var4 = true;
      } else {
         var4 = false;
      }

      if (!cameraPhotos.isEmpty()) {
         int var5 = cameraPhotos.size();

         for(int var8 = var2; var8 < var5; ++var8) {
            MediaController.PhotoEntry var6 = (MediaController.PhotoEntry)cameraPhotos.get(var8);
            (new File(var6.path)).delete();
            String var7 = var6.imagePath;
            if (var7 != null) {
               (new File(var7)).delete();
            }

            var7 = var6.thumbPath;
            if (var7 != null) {
               (new File(var7)).delete();
            }
         }

         cameraPhotos.clear();
         var4 = true;
      }

      if (var4) {
         this.photoAttachAdapter.notifyDataSetChanged();
         this.cameraAttachAdapter.notifyDataSetChanged();
      }

   }

   private ArrayList getAllPhotosArray() {
      MediaController.AlbumEntry var1;
      if (this.baseFragment instanceof ChatActivity) {
         var1 = MediaController.allMediaAlbumEntry;
      } else {
         var1 = MediaController.allPhotosAlbumEntry;
      }

      ArrayList var3;
      if (var1 != null) {
         if (!cameraPhotos.isEmpty()) {
            ArrayList var2 = new ArrayList(var1.photos.size() + cameraPhotos.size());
            var2.addAll(cameraPhotos);
            var2.addAll(var1.photos);
            var3 = var2;
         } else {
            var3 = var1.photos;
         }
      } else if (!cameraPhotos.isEmpty()) {
         var3 = cameraPhotos;
      } else {
         var3 = new ArrayList(0);
      }

      return var3;
   }

   private PhotoAttachPhotoCell getCellForIndex(int var1) {
      int var2 = this.attachPhotoRecyclerView.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         View var4 = this.attachPhotoRecyclerView.getChildAt(var3);
         if (var4 instanceof PhotoAttachPhotoCell) {
            PhotoAttachPhotoCell var5 = (PhotoAttachPhotoCell)var4;
            if ((Integer)var5.getImageView().getTag() == var1) {
               return var5;
            }
         }
      }

      return null;
   }

   private MediaController.PhotoEntry getPhotoEntryAtPosition(int var1) {
      if (var1 < 0) {
         return null;
      } else {
         int var2 = cameraPhotos.size();
         if (var1 < var2) {
            return (MediaController.PhotoEntry)cameraPhotos.get(var1);
         } else {
            var1 -= var2;
            MediaController.AlbumEntry var3;
            if (this.baseFragment instanceof ChatActivity) {
               var3 = MediaController.allMediaAlbumEntry;
            } else {
               var3 = MediaController.allPhotosAlbumEntry;
            }

            return var1 < var3.photos.size() ? (MediaController.PhotoEntry)var3.photos.get(var1) : null;
         }
      }
   }

   private void hideHint() {
      Runnable var1 = this.hideHintRunnable;
      if (var1 != null) {
         AndroidUtilities.cancelRunOnUIThread(var1);
         this.hideHintRunnable = null;
      }

      if (this.hintTextView != null) {
         this.currentHintAnimation = new AnimatorSet();
         this.currentHintAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.hintTextView, "alpha", new float[]{0.0F})});
         this.currentHintAnimation.setInterpolator(this.decelerateInterpolator);
         this.currentHintAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1) {
               if (ChatAttachAlert.this.currentHintAnimation != null && ChatAttachAlert.this.currentHintAnimation.equals(var1)) {
                  ChatAttachAlert.this.currentHintAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1) {
               if (ChatAttachAlert.this.currentHintAnimation != null && ChatAttachAlert.this.currentHintAnimation.equals(var1)) {
                  ChatAttachAlert.this.currentHintAnimation = null;
                  if (ChatAttachAlert.this.hintTextView != null) {
                     ChatAttachAlert.this.hintTextView.setVisibility(4);
                  }
               }

            }
         });
         this.currentHintAnimation.setDuration(300L);
         this.currentHintAnimation.start();
      }
   }

   // $FF: synthetic method
   static void lambda$new$5(View var0, int var1) {
      if (var0 instanceof PhotoAttachPhotoCell) {
         ((PhotoAttachPhotoCell)var0).callDelegate();
      }

   }

   private void onRevealAnimationEnd(boolean var1) {
      NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(false);
      this.revealAnimationInProgress = false;
      MediaController.AlbumEntry var2;
      if (this.baseFragment instanceof ChatActivity) {
         var2 = MediaController.allMediaAlbumEntry;
      } else {
         var2 = MediaController.allPhotosAlbumEntry;
      }

      if (var1 && VERSION.SDK_INT <= 19 && var2 == null) {
         MediaController.loadGalleryPhotosAlbums(0);
      }

      if (var1) {
         this.checkCamera(true);
         this.showHint();
         AndroidUtilities.makeAccessibilityAnnouncement(LocaleController.getString("AccDescrAttachButton", 2131558413));
      }

   }

   private void openCamera(boolean var1) {
      if (this.cameraView != null) {
         boolean var2 = cameraPhotos.isEmpty();
         int var3 = 0;
         if (var2) {
            this.counterTextView.setVisibility(4);
            this.cameraPhotoRecyclerView.setVisibility(8);
         } else {
            this.counterTextView.setVisibility(0);
            this.cameraPhotoRecyclerView.setVisibility(0);
         }

         this.cameraPanel.setVisibility(0);
         this.cameraPanel.setTag((Object)null);
         int[] var4 = this.animateCameraValues;
         var4[0] = 0;
         var4[1] = AndroidUtilities.dp(80.0F) - this.cameraViewOffsetX;
         this.animateCameraValues[2] = AndroidUtilities.dp(80.0F) - this.cameraViewOffsetY;
         if (var1) {
            this.cameraAnimationInProgress = true;
            ArrayList var6 = new ArrayList();
            var6.add(ObjectAnimator.ofFloat(this, "cameraOpenProgress", new float[]{0.0F, 1.0F}));
            var6.add(ObjectAnimator.ofFloat(this.cameraPanel, View.ALPHA, new float[]{1.0F}));
            var6.add(ObjectAnimator.ofFloat(this.counterTextView, View.ALPHA, new float[]{1.0F}));
            var6.add(ObjectAnimator.ofFloat(this.cameraPhotoRecyclerView, View.ALPHA, new float[]{1.0F}));

            for(var3 = 0; var3 < 2; ++var3) {
               if (this.flashModeButton[var3].getVisibility() == 0) {
                  var6.add(ObjectAnimator.ofFloat(this.flashModeButton[var3], View.ALPHA, new float[]{1.0F}));
                  break;
               }
            }

            AnimatorSet var5 = new AnimatorSet();
            var5.playTogether(var6);
            var5.setDuration(200L);
            var5.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  ChatAttachAlert.this.cameraAnimationInProgress = false;
                  if (ChatAttachAlert.this.cameraOpened) {
                     ChatAttachAlert.this.delegate.onCameraOpened();
                  }

               }
            });
            var5.start();
         } else {
            this.setCameraOpenProgress(1.0F);
            this.cameraPanel.setAlpha(1.0F);
            this.counterTextView.setAlpha(1.0F);
            this.cameraPhotoRecyclerView.setAlpha(1.0F);

            while(var3 < 2) {
               if (this.flashModeButton[var3].getVisibility() == 0) {
                  this.flashModeButton[var3].setAlpha(1.0F);
                  break;
               }

               ++var3;
            }

            this.delegate.onCameraOpened();
         }

         if (VERSION.SDK_INT >= 21) {
            this.cameraView.setSystemUiVisibility(1028);
         }

         this.cameraOpened = true;
         this.cameraView.setImportantForAccessibility(2);
         if (VERSION.SDK_INT >= 19) {
            this.attachPhotoRecyclerView.setImportantForAccessibility(4);
            Iterator var7 = this.attachButtons.iterator();

            while(var7.hasNext()) {
               ((ChatAttachAlert.AttachButton)var7.next()).setImportantForAccessibility(4);
            }
         }

      }
   }

   private void openPhotoViewer(MediaController.PhotoEntry var1, final boolean var2, boolean var3) {
      if (var1 != null) {
         cameraPhotos.add(var1);
         selectedPhotos.put(var1.imageId, var1);
         selectedPhotosOrder.add(var1.imageId);
         this.updatePhotosButton();
         this.photoAttachAdapter.notifyDataSetChanged();
         this.cameraAttachAdapter.notifyDataSetChanged();
      }

      if (var1 != null && !var3 && cameraPhotos.size() > 1) {
         this.updatePhotosCounter();
         if (this.cameraView != null) {
            CameraController.getInstance().startPreview(this.cameraView.getCameraSession());
         }

         this.mediaCaptured = false;
      } else if (!cameraPhotos.isEmpty()) {
         this.cancelTakingPhotos = true;
         PhotoViewer.getInstance().setParentActivity(this.baseFragment.getParentActivity());
         PhotoViewer.getInstance().setParentAlert(this);
         PhotoViewer.getInstance().setMaxSelectedPhotos(this.maxSelectedPhotos);
         BaseFragment var5 = this.baseFragment;
         byte var4;
         ChatActivity var6;
         if (var5 instanceof ChatActivity) {
            var6 = (ChatActivity)var5;
            var4 = 2;
         } else {
            var6 = null;
            var4 = 5;
         }

         PhotoViewer.getInstance().openPhotoForSelect(this.getAllPhotosArray(), cameraPhotos.size() - 1, var4, new ChatAttachAlert.BasePhotoProvider() {
            public boolean canCaptureMorePhotos() {
               int var1 = ChatAttachAlert.this.maxSelectedPhotos;
               boolean var2x = true;
               if (var1 == 1) {
                  var2x = false;
               }

               return var2x;
            }

            public boolean canScrollAway() {
               return false;
            }

            public boolean cancelButtonPressed() {
               if (ChatAttachAlert.this.cameraOpened && ChatAttachAlert.this.cameraView != null) {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$ChatAttachAlert$15$XovEDj9DHAwUNoLPMWuRb54fRhw(this), 1000L);
                  CameraController.getInstance().startPreview(ChatAttachAlert.this.cameraView.getCameraSession());
               }

               if (ChatAttachAlert.this.cancelTakingPhotos && ChatAttachAlert.cameraPhotos.size() == 1) {
                  int var1 = 0;

                  for(int var2x = ChatAttachAlert.cameraPhotos.size(); var1 < var2x; ++var1) {
                     MediaController.PhotoEntry var3 = (MediaController.PhotoEntry)ChatAttachAlert.cameraPhotos.get(var1);
                     (new File(var3.path)).delete();
                     String var4 = var3.imagePath;
                     if (var4 != null) {
                        (new File(var4)).delete();
                     }

                     String var5 = var3.thumbPath;
                     if (var5 != null) {
                        (new File(var5)).delete();
                     }
                  }

                  ChatAttachAlert.cameraPhotos.clear();
                  ChatAttachAlert.selectedPhotosOrder.clear();
                  ChatAttachAlert.selectedPhotos.clear();
                  ChatAttachAlert.this.counterTextView.setVisibility(4);
                  ChatAttachAlert.this.cameraPhotoRecyclerView.setVisibility(8);
                  ChatAttachAlert.this.photoAttachAdapter.notifyDataSetChanged();
                  ChatAttachAlert.this.cameraAttachAdapter.notifyDataSetChanged();
                  ChatAttachAlert.this.updatePhotosButton();
               }

               return true;
            }

            public ImageReceiver.BitmapHolder getThumbForPhoto(MessageObject var1, TLRPC.FileLocation var2x, int var3) {
               return null;
            }

            // $FF: synthetic method
            public void lambda$cancelButtonPressed$0$ChatAttachAlert$15() {
               if (ChatAttachAlert.this.cameraView != null && !ChatAttachAlert.this.isDismissed() && VERSION.SDK_INT >= 21) {
                  ChatAttachAlert.this.cameraView.setSystemUiVisibility(1028);
               }

            }

            public void needAddMorePhotos() {
               ChatAttachAlert.this.cancelTakingPhotos = false;
               if (ChatAttachAlert.mediaFromExternalCamera) {
                  ChatAttachAlert.this.delegate.didPressedButton(0);
               } else {
                  if (!ChatAttachAlert.this.cameraOpened) {
                     ChatAttachAlert.this.openCamera(false);
                  }

                  ChatAttachAlert.this.counterTextView.setVisibility(0);
                  ChatAttachAlert.this.cameraPhotoRecyclerView.setVisibility(0);
                  ChatAttachAlert.this.counterTextView.setAlpha(1.0F);
                  ChatAttachAlert.this.updatePhotosCounter();
               }
            }

            public boolean scaleToFill() {
               BaseFragment var1 = ChatAttachAlert.this.baseFragment;
               boolean var2x = false;
               boolean var3 = var2x;
               if (var1 != null) {
                  if (ChatAttachAlert.this.baseFragment.getParentActivity() == null) {
                     var3 = var2x;
                  } else {
                     int var4 = System.getInt(ChatAttachAlert.this.baseFragment.getParentActivity().getContentResolver(), "accelerometer_rotation", 0);
                     if (!var2) {
                        var3 = var2x;
                        if (var4 != 1) {
                           return var3;
                        }
                     }

                     var3 = true;
                  }
               }

               return var3;
            }

            public void sendButtonPressed(int var1, VideoEditedInfo var2x) {
               if (!ChatAttachAlert.cameraPhotos.isEmpty() && ChatAttachAlert.this.baseFragment != null) {
                  if (var2x != null && var1 >= 0 && var1 < ChatAttachAlert.cameraPhotos.size()) {
                     ((MediaController.PhotoEntry)ChatAttachAlert.cameraPhotos.get(var1)).editedInfo = var2x;
                  }

                  if (!(ChatAttachAlert.this.baseFragment instanceof ChatActivity) || !((ChatActivity)ChatAttachAlert.this.baseFragment).isSecretChat()) {
                     int var3 = ChatAttachAlert.cameraPhotos.size();

                     for(var1 = 0; var1 < var3; ++var1) {
                        AndroidUtilities.addMediaToGallery(((MediaController.PhotoEntry)ChatAttachAlert.cameraPhotos.get(var1)).path);
                     }
                  }

                  ChatAttachAlert.this.delegate.didPressedButton(8);
                  ChatAttachAlert.cameraPhotos.clear();
                  ChatAttachAlert.selectedPhotosOrder.clear();
                  ChatAttachAlert.selectedPhotos.clear();
                  ChatAttachAlert.this.photoAttachAdapter.notifyDataSetChanged();
                  ChatAttachAlert.this.cameraAttachAdapter.notifyDataSetChanged();
                  ChatAttachAlert.this.closeCamera(false);
                  ChatAttachAlert.this.dismiss();
               }

            }

            public void willHidePhotoViewer() {
               ChatAttachAlert var1 = ChatAttachAlert.this;
               int var2x = 0;
               var1.mediaCaptured = false;

               for(int var3 = ChatAttachAlert.this.attachPhotoRecyclerView.getChildCount(); var2x < var3; ++var2x) {
                  View var4 = ChatAttachAlert.this.attachPhotoRecyclerView.getChildAt(var2x);
                  if (var4 instanceof PhotoAttachPhotoCell) {
                     PhotoAttachPhotoCell var5 = (PhotoAttachPhotoCell)var4;
                     var5.showImage();
                     var5.showCheck(true);
                  }
               }

            }
         }, var6);
      }
   }

   private boolean processTouchEvent(MotionEvent var1) {
      if (var1 == null) {
         return false;
      } else {
         if ((this.pressed || var1.getActionMasked() != 0) && var1.getActionMasked() != 5) {
            if (this.pressed) {
               float var2;
               float var3;
               CameraView var7;
               AnimatorSet var8;
               if (var1.getActionMasked() == 2) {
                  var2 = var1.getY();
                  var3 = var2 - this.lastY;
                  if (this.maybeStartDraging) {
                     if (Math.abs(var3) > AndroidUtilities.getPixelsInCM(0.4F, false)) {
                        this.maybeStartDraging = false;
                        this.dragging = true;
                     }
                  } else if (this.dragging) {
                     var7 = this.cameraView;
                     if (var7 != null) {
                        var7.setTranslationY(var7.getTranslationY() + var3);
                        this.lastY = var2;
                        if (this.cameraPanel.getTag() == null) {
                           this.cameraPanel.setTag(1);
                           var8 = new AnimatorSet();
                           var8.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.cameraPanel, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.counterTextView, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.flashModeButton[0], "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.flashModeButton[1], "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.cameraPhotoRecyclerView, "alpha", new float[]{0.0F})});
                           var8.setDuration(200L);
                           var8.start();
                        }
                     }
                  }
               } else if (var1.getActionMasked() == 3 || var1.getActionMasked() == 1 || var1.getActionMasked() == 6) {
                  this.pressed = false;
                  if (this.dragging) {
                     this.dragging = false;
                     var7 = this.cameraView;
                     if (var7 != null) {
                        if (Math.abs(var7.getTranslationY()) > (float)this.cameraView.getMeasuredHeight() / 6.0F) {
                           this.closeCamera(true);
                        } else {
                           var8 = new AnimatorSet();
                           var8.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.cameraView, "translationY", new float[]{0.0F}), ObjectAnimator.ofFloat(this.cameraPanel, "alpha", new float[]{1.0F}), ObjectAnimator.ofFloat(this.counterTextView, "alpha", new float[]{1.0F}), ObjectAnimator.ofFloat(this.flashModeButton[0], "alpha", new float[]{1.0F}), ObjectAnimator.ofFloat(this.flashModeButton[1], "alpha", new float[]{1.0F}), ObjectAnimator.ofFloat(this.cameraPhotoRecyclerView, "alpha", new float[]{1.0F})});
                           var8.setDuration(250L);
                           var8.setInterpolator(this.interpolator);
                           var8.start();
                           this.cameraPanel.setTag((Object)null);
                        }
                     }
                  } else {
                     CameraView var4 = this.cameraView;
                     if (var4 != null) {
                        var4.getLocationOnScreen(this.viewPosition);
                        float var5 = var1.getRawX();
                        float var6 = (float)this.viewPosition[0];
                        var3 = var1.getRawY();
                        var2 = (float)this.viewPosition[1];
                        this.cameraView.focusToPoint((int)(var5 - var6), (int)(var3 - var2));
                     }
                  }
               }
            }
         } else if (!this.takingPhoto) {
            this.pressed = true;
            this.maybeStartDraging = true;
            this.lastY = var1.getY();
         }

         return true;
      }
   }

   private void resetRecordState() {
      if (this.baseFragment != null) {
         for(int var1 = 0; var1 < 2; ++var1) {
            this.flashModeButton[var1].setAlpha(1.0F);
         }

         this.switchCameraButton.setAlpha(1.0F);
         this.recordTime.setAlpha(0.0F);
         AndroidUtilities.cancelRunOnUIThread(this.videoRecordRunnable);
         this.videoRecordRunnable = null;
         AndroidUtilities.unlockOrientation(this.baseFragment.getParentActivity());
      }
   }

   private void setCameraFlashModeIcon(ImageView var1, String var2) {
      byte var4;
      label34: {
         int var3 = var2.hashCode();
         if (var3 != 3551) {
            if (var3 != 109935) {
               if (var3 == 3005871 && var2.equals("auto")) {
                  var4 = 2;
                  break label34;
               }
            } else if (var2.equals("off")) {
               var4 = 0;
               break label34;
            }
         } else if (var2.equals("on")) {
            var4 = 1;
            break label34;
         }

         var4 = -1;
      }

      if (var4 != 0) {
         if (var4 != 1) {
            if (var4 == 2) {
               var1.setImageResource(2131165380);
               var1.setContentDescription(LocaleController.getString("AccDescrCameraFlashAuto", 2131558417));
            }
         } else {
            var1.setImageResource(2131165382);
            var1.setContentDescription(LocaleController.getString("AccDescrCameraFlashOn", 2131558419));
         }
      } else {
         var1.setImageResource(2131165381);
         var1.setContentDescription(LocaleController.getString("AccDescrCameraFlashOff", 2131558418));
      }

   }

   private void setUseRevealAnimation(boolean var1) {
      if (!var1 || var1 && VERSION.SDK_INT >= 18 && !AndroidUtilities.isTablet() && AndroidUtilities.shouldEnableAnimation() && this.baseFragment instanceof ChatActivity) {
         this.useRevealAnimation = var1;
      }

   }

   private void showHint() {
      if (this.editingMessageObject == null && this.baseFragment instanceof ChatActivity && !DataQuery.getInstance(this.currentAccount).inlineBots.isEmpty()) {
         if (MessagesController.getGlobalMainSettings().getBoolean("bothint", false)) {
            return;
         }

         this.hintShowed = true;
         this.hintTextView.setVisibility(0);
         this.currentHintAnimation = new AnimatorSet();
         this.currentHintAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.hintTextView, "alpha", new float[]{0.0F, 1.0F})});
         this.currentHintAnimation.setInterpolator(this.decelerateInterpolator);
         this.currentHintAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1) {
               if (ChatAttachAlert.this.currentHintAnimation != null && ChatAttachAlert.this.currentHintAnimation.equals(var1)) {
                  ChatAttachAlert.this.currentHintAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1) {
               if (ChatAttachAlert.this.currentHintAnimation != null && ChatAttachAlert.this.currentHintAnimation.equals(var1)) {
                  ChatAttachAlert.this.currentHintAnimation = null;
                  ChatAttachAlert var3 = ChatAttachAlert.this;
                  Runnable var2 = new Runnable() {
                     public void run() {
                        if (ChatAttachAlert.this.hideHintRunnable == this) {
                           ChatAttachAlert.this.hideHintRunnable = null;
                           ChatAttachAlert.this.hideHint();
                        }
                     }
                  };
                  var3.hideHintRunnable = var2;
                  AndroidUtilities.runOnUIThread(var2, 2000L);
               }

            }
         });
         this.currentHintAnimation.setDuration(300L);
         this.currentHintAnimation.start();
      }

   }

   @SuppressLint({"NewApi"})
   private void startRevealAnimation(final boolean var1) {
      super.containerView.setTranslationY(0.0F);
      final AnimatorSet var2 = new AnimatorSet();
      View var3 = this.delegate.getRevealView();
      float var5;
      if (var3.getVisibility() == 0 && ((ViewGroup)var3.getParent()).getVisibility() == 0) {
         int[] var20 = new int[2];
         var3.getLocationInWindow(var20);
         if (VERSION.SDK_INT <= 19) {
            var5 = (float)(AndroidUtilities.displaySize.y - super.containerView.getMeasuredHeight() - AndroidUtilities.statusBarHeight);
         } else {
            var5 = super.containerView.getY();
         }

         this.revealX = var20[0] + var3.getMeasuredWidth() / 2;
         this.revealY = (int)((float)(var20[1] + var3.getMeasuredHeight() / 2) - var5);
         if (VERSION.SDK_INT <= 19) {
            this.revealY -= AndroidUtilities.statusBarHeight;
         }
      } else {
         android.graphics.Point var4 = AndroidUtilities.displaySize;
         this.revealX = var4.x / 2 + super.backgroundPaddingLeft;
         this.revealY = (int)((float)var4.y - super.containerView.getY());
      }

      int[][] var21 = new int[][]{{0, 0}, {0, AndroidUtilities.dp(304.0F)}, {super.containerView.getMeasuredWidth(), 0}, {super.containerView.getMeasuredWidth(), AndroidUtilities.dp(304.0F)}};
      int var6 = this.revealY - this.scrollOffsetY + super.backgroundPaddingTop;
      int var7 = 0;

      int var8;
      int var9;
      for(var8 = 0; var7 < 4; ++var7) {
         var9 = this.revealX;
         var8 = Math.max(var8, (int)Math.ceil(Math.sqrt((double)((var9 - var21[var7][0]) * (var9 - var21[var7][0]) + (var6 - var21[var7][1]) * (var6 - var21[var7][1])))));
      }

      if (this.revealX <= super.containerView.getMeasuredWidth()) {
         var7 = this.revealX;
      } else {
         var7 = super.containerView.getMeasuredWidth();
      }

      ArrayList var23 = new ArrayList(3);
      if (var1) {
         var5 = 0.0F;
      } else {
         var5 = (float)var8;
      }

      float var10;
      if (var1) {
         var10 = (float)var8;
      } else {
         var10 = 0.0F;
      }

      var23.add(ObjectAnimator.ofFloat(this, "revealRadius", new float[]{var5, var10}));
      ColorDrawable var17 = super.backDrawable;
      byte var26;
      if (var1) {
         var26 = 51;
      } else {
         var26 = 0;
      }

      var23.add(ObjectAnimator.ofInt(var17, "alpha", new int[]{var26}));
      ViewGroup var18;
      if (VERSION.SDK_INT >= 21) {
         label115: {
            Exception var10000;
            label132: {
               boolean var10001;
               try {
                  var18 = super.containerView;
                  var6 = this.revealY;
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label132;
               }

               if (var1) {
                  var5 = 0.0F;
               } else {
                  var5 = (float)var8;
               }

               if (var1) {
                  var10 = (float)var8;
               } else {
                  var10 = 0.0F;
               }

               try {
                  var23.add(ViewAnimationUtils.createCircularReveal(var18, var7, var6, var5, var10));
                  break label115;
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
               }
            }

            Exception var19 = var10000;
            FileLog.e((Throwable)var19);
         }

         var2.setDuration(320L);
      } else if (!var1) {
         var2.setDuration(200L);
         var18 = super.containerView;
         if (this.revealX <= var18.getMeasuredWidth()) {
            var8 = this.revealX;
         } else {
            var8 = super.containerView.getMeasuredWidth();
         }

         var18.setPivotX((float)var8);
         super.containerView.setPivotY((float)this.revealY);
         var23.add(ObjectAnimator.ofFloat(super.containerView, View.SCALE_X, new float[]{0.0F}));
         var23.add(ObjectAnimator.ofFloat(super.containerView, View.SCALE_Y, new float[]{0.0F}));
         var23.add(ObjectAnimator.ofFloat(super.containerView, View.ALPHA, new float[]{0.0F}));
      } else {
         var2.setDuration(250L);
         super.containerView.setScaleX(1.0F);
         super.containerView.setScaleY(1.0F);
         super.containerView.setAlpha(1.0F);
         if (VERSION.SDK_INT <= 19) {
            var2.setStartDelay(20L);
         }
      }

      var2.playTogether(var23);
      var2.addListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1x) {
            if (ChatAttachAlert.access$9200(ChatAttachAlert.this) != null && var2.equals(var1x)) {
               ChatAttachAlert.access$9302(ChatAttachAlert.this, (AnimatorSet)null);
            }

         }

         public void onAnimationEnd(Animator var1x) {
            if (ChatAttachAlert.access$8600(ChatAttachAlert.this) != null && ChatAttachAlert.access$8700(ChatAttachAlert.this).equals(var1x)) {
               ChatAttachAlert.access$8802(ChatAttachAlert.this, (AnimatorSet)null);
               ChatAttachAlert.this.onRevealAnimationEnd(var1);
               ChatAttachAlert.access$9000(ChatAttachAlert.this).invalidate();
               ChatAttachAlert.access$9100(ChatAttachAlert.this).setLayerType(0, (Paint)null);
               if (!var1) {
                  try {
                     ChatAttachAlert.this.dismissInternal();
                  } catch (Exception var2x) {
                     FileLog.e((Throwable)var2x);
                  }
               }
            }

         }
      });
      final AnimatorSet var24 = var2;
      if (var1) {
         this.innerAnimators.clear();
         NotificationCenter.getInstance(this.currentAccount).setAllowedNotificationsDutingAnimation(new int[]{NotificationCenter.dialogsNeedReload});
         NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(true);
         this.revealAnimationInProgress = true;
         byte var27;
         if (VERSION.SDK_INT <= 19) {
            var27 = 12;
         } else {
            var27 = 8;
         }

         var7 = 0;

         while(true) {
            var24 = var2;
            if (var7 >= var27) {
               break;
            }

            View[] var25 = this.views;
            if (var25[var7] != null) {
               if (VERSION.SDK_INT <= 19) {
                  if (var7 < 8) {
                     var25[var7].setScaleX(0.1F);
                     this.views[var7].setScaleY(0.1F);
                  }

                  this.views[var7].setAlpha(0.0F);
               } else {
                  var25[var7].setScaleX(0.7F);
                  this.views[var7].setScaleY(0.7F);
               }

               ChatAttachAlert.InnerAnimator var22 = new ChatAttachAlert.InnerAnimator();
               var6 = this.views[var7].getLeft() + this.views[var7].getMeasuredWidth() / 2;
               var9 = this.views[var7].getTop() + this.attachView.getTop() + this.views[var7].getMeasuredHeight() / 2;
               int var11 = this.revealX;
               int var12 = this.revealY;
               var10 = (float)Math.sqrt((double)((var11 - var6) * (var11 - var6) + (var12 - var9) * (var12 - var9)));
               var5 = (float)(this.revealX - var6) / var10;
               float var13 = (float)(this.revealY - var9) / var10;
               var25 = this.views;
               var25[var7].setPivotX((float)(var25[var7].getMeasuredWidth() / 2) + var5 * (float)AndroidUtilities.dp(20.0F));
               var25 = this.views;
               var25[var7].setPivotY((float)(var25[var7].getMeasuredHeight() / 2) + var13 * (float)AndroidUtilities.dp(20.0F));
               var22.startRadius = var10 - (float)AndroidUtilities.dp(81.0F);
               this.views[var7].setTag(2131558635, 1);
               ArrayList var14 = new ArrayList();
               if (var7 < 8) {
                  var14.add(ObjectAnimator.ofFloat(this.views[var7], View.SCALE_X, new float[]{0.7F, 1.05F}));
                  var14.add(ObjectAnimator.ofFloat(this.views[var7], View.SCALE_Y, new float[]{0.7F, 1.05F}));
                  var24 = new AnimatorSet();
                  var24.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.views[var7], View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.views[var7], View.SCALE_Y, new float[]{1.0F})});
                  var24.setDuration(100L);
                  var24.setInterpolator(CubicBezierInterpolator.EASE_OUT);
               } else {
                  var24 = null;
               }

               if (VERSION.SDK_INT <= 19) {
                  var14.add(ObjectAnimator.ofFloat(this.views[var7], View.ALPHA, new float[]{1.0F}));
               }

               var22.animatorSet = new AnimatorSet();
               var22.animatorSet.playTogether(var14);
               var22.animatorSet.setDuration(150L);
               var22.animatorSet.setInterpolator(CubicBezierInterpolator.EASE_OUT);
               var22.animatorSet.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     AnimatorSet var2 = var24;
                     if (var2 != null) {
                        var2.start();
                     }

                  }
               });
               this.innerAnimators.add(var22);
            }

            ++var7;
         }
      }

      super.currentSheetAnimation = var24;
      var24.start();
   }

   private void updateCheckedPhotoIndices() {
      if (this.baseFragment instanceof ChatActivity) {
         int var1 = this.attachPhotoRecyclerView.getChildCount();
         byte var2 = 0;

         int var3;
         View var4;
         for(var3 = 0; var3 < var1; ++var3) {
            var4 = this.attachPhotoRecyclerView.getChildAt(var3);
            if (var4 instanceof PhotoAttachPhotoCell) {
               PhotoAttachPhotoCell var5 = (PhotoAttachPhotoCell)var4;
               MediaController.PhotoEntry var6 = this.getPhotoEntryAtPosition((Integer)var5.getTag());
               if (var6 != null) {
                  var5.setNum(selectedPhotosOrder.indexOf(var6.imageId));
               }
            }
         }

         var1 = this.cameraPhotoRecyclerView.getChildCount();

         for(var3 = var2; var3 < var1; ++var3) {
            var4 = this.cameraPhotoRecyclerView.getChildAt(var3);
            if (var4 instanceof PhotoAttachPhotoCell) {
               PhotoAttachPhotoCell var7 = (PhotoAttachPhotoCell)var4;
               MediaController.PhotoEntry var8 = this.getPhotoEntryAtPosition((Integer)var7.getTag());
               if (var8 != null) {
                  var7.setNum(selectedPhotosOrder.indexOf(var8.imageId));
               }
            }
         }

      }
   }

   @SuppressLint({"NewApi"})
   private void updateLayout() {
      int var2;
      RecyclerListView var4;
      if (this.listView.getChildCount() <= 0) {
         var4 = this.listView;
         var2 = var4.getPaddingTop();
         this.scrollOffsetY = var2;
         var4.setTopGlowOffset(var2);
         this.listView.invalidate();
      } else {
         View var3 = this.listView.getChildAt(0);
         RecyclerListView.Holder var1 = (RecyclerListView.Holder)this.listView.findContainingViewHolder(var3);
         var2 = var3.getTop();
         if (var2 < 0 || var1 == null || var1.getAdapterPosition() != 0) {
            var2 = 0;
         }

         if (this.scrollOffsetY != var2) {
            var4 = this.listView;
            this.scrollOffsetY = var2;
            var4.setTopGlowOffset(var2);
            this.listView.invalidate();
         }

      }
   }

   private void updatePhotosCounter() {
      if (this.counterTextView != null) {
         boolean var1 = false;
         Iterator var2 = selectedPhotos.entrySet().iterator();

         boolean var3;
         while(true) {
            var3 = var1;
            if (!var2.hasNext()) {
               break;
            }

            if (((MediaController.PhotoEntry)((Entry)var2.next()).getValue()).isVideo) {
               var3 = true;
               break;
            }
         }

         if (var3) {
            this.counterTextView.setText(LocaleController.formatPluralString("Media", selectedPhotos.size()).toUpperCase());
         } else {
            this.counterTextView.setText(LocaleController.formatPluralString("Photos", selectedPhotos.size()).toUpperCase());
         }

      }
   }

   private void updatePollMusicButton() {
      if (this.baseFragment instanceof ChatActivity) {
         if (this.attachButtons.isEmpty()) {
            return;
         }

         MessageObject var1 = this.editingMessageObject;
         boolean var2 = false;
         boolean var3;
         if (var1 != null) {
            var3 = var2;
         } else {
            TLRPC.Chat var7 = ((ChatActivity)this.baseFragment).getCurrentChat();
            var3 = var2;
            if (var7 != null) {
               var3 = var2;
               if (ChatObject.canSendPolls(var7)) {
                  var3 = true;
               }
            }
         }

         String var8;
         int var9;
         if (var3) {
            var9 = 2131560467;
            var8 = "Poll";
         } else {
            var9 = 2131558726;
            var8 = "AttachMusic";
         }

         var8 = LocaleController.getString(var8, var9);
         ArrayList var4 = this.attachButtons;
         byte var5 = 3;
         ChatAttachAlert.AttachButton var11 = (ChatAttachAlert.AttachButton)var4.get(3);
         byte var10;
         if (var3) {
            var10 = 9;
         } else {
            var10 = 3;
         }

         var11.setTag(Integer.valueOf(var10));
         Drawable[] var6 = Theme.chat_attachButtonDrawables;
         var10 = var5;
         if (var3) {
            var10 = 9;
         }

         var11.setTextAndIcon(var8, var6[var10]);
      }

   }

   public boolean canDismiss() {
      return true;
   }

   protected boolean canDismissWithSwipe() {
      return false;
   }

   protected boolean canDismissWithTouchOutside() {
      return this.cameraOpened ^ true;
   }

   public void checkCamera(boolean var1) {
      BaseFragment var2 = this.baseFragment;
      if (var2 != null) {
         boolean var3 = this.deviceHasGoodCamera;
         if (!SharedConfig.inappCamera) {
            this.deviceHasGoodCamera = false;
         } else if (VERSION.SDK_INT >= 23) {
            if (var2.getParentActivity().checkSelfPermission("android.permission.CAMERA") != 0) {
               if (var1) {
                  try {
                     this.baseFragment.getParentActivity().requestPermissions(new String[]{"android.permission.CAMERA"}, 17);
                  } catch (Exception var4) {
                  }
               }

               this.deviceHasGoodCamera = false;
            } else {
               if (var1 || SharedConfig.hasCameraCache) {
                  CameraController.getInstance().initCamera((Runnable)null);
               }

               this.deviceHasGoodCamera = CameraController.getInstance().isCameraInitied();
            }
         } else {
            if (var1 || SharedConfig.hasCameraCache) {
               CameraController.getInstance().initCamera((Runnable)null);
            }

            this.deviceHasGoodCamera = CameraController.getInstance().isCameraInitied();
         }

         if (var3 != this.deviceHasGoodCamera) {
            ChatAttachAlert.PhotoAttachAdapter var5 = this.photoAttachAdapter;
            if (var5 != null) {
               var5.notifyDataSetChanged();
            }
         }

         if (this.isShowing() && this.deviceHasGoodCamera && this.baseFragment != null && super.backDrawable.getAlpha() != 0 && !this.revealAnimationInProgress && !this.cameraOpened) {
            this.showCamera();
         }

      }
   }

   public void checkColors() {
      int var1 = this.attachButtons.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         ((ChatAttachAlert.AttachButton)this.attachButtons.get(var2)).textView.setTextColor(Theme.getColor("dialogTextGray2"));
      }

      this.lineView.setBackgroundColor(Theme.getColor("dialogGrayLine"));
      TextView var3 = this.hintTextView;
      if (var3 != null) {
         Theme.setDrawableColor(var3.getBackground(), Theme.getColor("chat_gifSaveHintBackground"));
         this.hintTextView.setTextColor(Theme.getColor("chat_gifSaveHintText"));
      }

      CorrectlyMeasuringTextView var4 = this.mediaBanTooltip;
      if (var4 != null) {
         Theme.setDrawableColor(var4.getBackground(), Theme.getColor("chat_attachMediaBanBackground"));
         this.mediaBanTooltip.setTextColor(Theme.getColor("chat_attachMediaBanText"));
      }

      RecyclerListView var5 = this.listView;
      RecyclerView.ViewHolder var6;
      if (var5 != null) {
         var5.setGlowColor(Theme.getColor("dialogScrollGlow"));
         var6 = this.listView.findViewHolderForAdapterPosition(1);
         if (var6 != null) {
            var6.itemView.setBackgroundColor(Theme.getColor("dialogBackgroundGray"));
         } else {
            this.adapter.notifyItemChanged(1);
         }
      }

      Paint var7 = this.ciclePaint;
      if (var7 != null) {
         var7.setColor(Theme.getColor("dialogBackground"));
      }

      Theme.setDrawableColor(this.shadowDrawable, Theme.getColor("dialogBackground"));
      ImageView var8 = this.cameraImageView;
      if (var8 != null) {
         var8.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogCameraIcon"), Mode.MULTIPLY));
      }

      var5 = this.attachPhotoRecyclerView;
      if (var5 != null) {
         var6 = var5.findViewHolderForAdapterPosition(0);
         if (var6 != null) {
            View var9 = var6.itemView;
            if (var9 instanceof PhotoAttachCameraCell) {
               ((PhotoAttachCameraCell)var9).getImageView().setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogCameraIcon"), Mode.MULTIPLY));
            }
         }
      }

      super.containerView.invalidate();
   }

   public void closeCamera(boolean var1) {
      if (!this.takingPhoto && this.cameraView != null) {
         this.animateCameraValues[1] = AndroidUtilities.dp(80.0F) - this.cameraViewOffsetX;
         this.animateCameraValues[2] = AndroidUtilities.dp(80.0F) - this.cameraViewOffsetY;
         int var4;
         if (var1) {
            LayoutParams var2 = (LayoutParams)this.cameraView.getLayoutParams();
            int[] var3 = this.animateCameraValues;
            var4 = (int)this.cameraView.getTranslationY();
            var2.topMargin = var4;
            var3[0] = var4;
            this.cameraView.setLayoutParams(var2);
            this.cameraView.setTranslationY(0.0F);
            this.cameraAnimationInProgress = true;
            ArrayList var7 = new ArrayList();
            var7.add(ObjectAnimator.ofFloat(this, "cameraOpenProgress", new float[]{0.0F}));
            var7.add(ObjectAnimator.ofFloat(this.cameraPanel, "alpha", new float[]{0.0F}));
            var7.add(ObjectAnimator.ofFloat(this.counterTextView, "alpha", new float[]{0.0F}));
            var7.add(ObjectAnimator.ofFloat(this.cameraPhotoRecyclerView, "alpha", new float[]{0.0F}));

            for(var4 = 0; var4 < 2; ++var4) {
               if (this.flashModeButton[var4].getVisibility() == 0) {
                  var7.add(ObjectAnimator.ofFloat(this.flashModeButton[var4], "alpha", new float[]{0.0F}));
                  break;
               }
            }

            AnimatorSet var5 = new AnimatorSet();
            var5.playTogether(var7);
            var5.setDuration(200L);
            var5.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  ChatAttachAlert.this.cameraAnimationInProgress = false;
                  ChatAttachAlert.this.cameraOpened = false;
                  if (ChatAttachAlert.this.cameraPanel != null) {
                     ChatAttachAlert.this.cameraPanel.setVisibility(8);
                  }

                  if (ChatAttachAlert.this.cameraPhotoRecyclerView != null) {
                     ChatAttachAlert.this.cameraPhotoRecyclerView.setVisibility(8);
                  }

                  if (VERSION.SDK_INT >= 21 && ChatAttachAlert.this.cameraView != null) {
                     ChatAttachAlert.this.cameraView.setSystemUiVisibility(1024);
                  }

               }
            });
            var5.start();
         } else {
            this.animateCameraValues[0] = 0;
            this.setCameraOpenProgress(0.0F);
            this.cameraPanel.setAlpha(0.0F);
            this.cameraPhotoRecyclerView.setAlpha(0.0F);
            this.counterTextView.setAlpha(0.0F);
            this.cameraPanel.setVisibility(8);
            this.cameraPhotoRecyclerView.setVisibility(8);

            for(var4 = 0; var4 < 2; ++var4) {
               if (this.flashModeButton[var4].getVisibility() == 0) {
                  this.flashModeButton[var4].setAlpha(0.0F);
                  break;
               }
            }

            this.cameraOpened = false;
            if (VERSION.SDK_INT >= 21) {
               this.cameraView.setSystemUiVisibility(1024);
            }
         }

         this.cameraView.setImportantForAccessibility(0);
         if (VERSION.SDK_INT >= 19) {
            this.attachPhotoRecyclerView.setImportantForAccessibility(0);
            Iterator var6 = this.attachButtons.iterator();

            while(var6.hasNext()) {
               ((ChatAttachAlert.AttachButton)var6.next()).setImportantForAccessibility(0);
            }
         }
      }

   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      int var4 = NotificationCenter.albumsDidLoad;
      byte var6 = 0;
      if (var1 == var4) {
         if (this.photoAttachAdapter != null) {
            this.loading = false;
            this.progressView.showTextView();
            this.photoAttachAdapter.notifyDataSetChanged();
            this.cameraAttachAdapter.notifyDataSetChanged();
            if (!selectedPhotosOrder.isEmpty()) {
               MediaController.AlbumEntry var7;
               if (this.baseFragment instanceof ChatActivity) {
                  var7 = MediaController.allMediaAlbumEntry;
               } else {
                  var7 = MediaController.allPhotosAlbumEntry;
               }

               if (var7 != null) {
                  var4 = selectedPhotosOrder.size();

                  for(var1 = var6; var1 < var4; ++var1) {
                     var2 = (Integer)selectedPhotosOrder.get(var1);
                     MediaController.PhotoEntry var5 = (MediaController.PhotoEntry)var7.photosByIds.get(var2);
                     if (var5 != null) {
                        selectedPhotos.put(var2, var5);
                     }
                  }
               }
            }
         }
      } else if (var1 == NotificationCenter.reloadInlineHints) {
         ChatAttachAlert.ListAdapter var8 = this.adapter;
         if (var8 != null) {
            var8.notifyDataSetChanged();
         }
      } else if (var1 == NotificationCenter.cameraInitied) {
         this.checkCamera(false);
      }

   }

   public void dismiss() {
      if (!this.cameraAnimationInProgress) {
         if (this.cameraOpened) {
            this.closeCamera(true);
         } else {
            this.hideCamera(true);
            super.dismiss();
         }
      }
   }

   public void dismissInternal() {
      ViewGroup var1 = super.containerView;
      if (var1 != null) {
         var1.setVisibility(4);
      }

      super.dismissInternal();
   }

   public void dismissWithButtonClick(int var1) {
      super.dismissWithButtonClick(var1);
      boolean var2;
      if (var1 != 0 && var1 != 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.hideCamera(var2);
   }

   @Keep
   public float getCameraOpenProgress() {
      return this.cameraOpenProgress;
   }

   public MessageObject getEditingMessageObject() {
      return this.editingMessageObject;
   }

   @Keep
   protected float getRevealRadius() {
      return this.revealRadius;
   }

   public HashMap getSelectedPhotos() {
      return selectedPhotos;
   }

   public ArrayList getSelectedPhotosOrder() {
      return selectedPhotosOrder;
   }

   public void hideCamera(boolean var1) {
      if (this.deviceHasGoodCamera) {
         CameraView var2 = this.cameraView;
         if (var2 != null) {
            var2.destroy(var1, (Runnable)null);
            super.container.removeView(this.cameraView);
            super.container.removeView(this.cameraIcon);
            this.cameraView = null;
            this.cameraIcon = null;
            int var3 = this.attachPhotoRecyclerView.getChildCount();

            for(int var4 = 0; var4 < var3; ++var4) {
               View var5 = this.attachPhotoRecyclerView.getChildAt(var4);
               if (var5 instanceof PhotoAttachCameraCell) {
                  var5.setVisibility(0);
                  return;
               }
            }
         }
      }

   }

   public void init() {
      MediaController.AlbumEntry var1;
      if (this.baseFragment instanceof ChatActivity) {
         var1 = MediaController.allMediaAlbumEntry;
      } else {
         var1 = MediaController.allPhotosAlbumEntry;
      }

      if (var1 != null) {
         for(int var2 = 0; var2 < Math.min(100, var1.photos.size()); ++var2) {
            ((MediaController.PhotoEntry)var1.photos.get(var2)).reset();
         }
      }

      AnimatorSet var3 = this.currentHintAnimation;
      if (var3 != null) {
         var3.cancel();
         this.currentHintAnimation = null;
      }

      this.hintTextView.setAlpha(0.0F);
      this.hintTextView.setVisibility(4);
      this.attachPhotoLayoutManager.scrollToPositionWithOffset(0, 1000000);
      this.cameraPhotoLayoutManager.scrollToPositionWithOffset(0, 1000000);
      this.clearSelectedPhotos();
      this.layoutManager.scrollToPositionWithOffset(0, 1000000);
      this.updatePhotosButton();
   }

   // $FF: synthetic method
   public void lambda$applyCameraViewPosition$6$ChatAttachAlert(LayoutParams var1) {
      CameraView var2 = this.cameraView;
      if (var2 != null) {
         var2.setLayoutParams(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$applyCameraViewPosition$7$ChatAttachAlert(LayoutParams var1) {
      FrameLayout var2 = this.cameraIcon;
      if (var2 != null) {
         var2.setLayoutParams(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$new$0$ChatAttachAlert(View var1, int var2) {
      BaseFragment var5 = this.baseFragment;
      if (var5 != null && var5.getParentActivity() != null) {
         if (this.deviceHasGoodCamera && var2 == 0) {
            this.openCamera(true);
         } else {
            int var3 = var2;
            if (this.deviceHasGoodCamera) {
               var3 = var2 - 1;
            }

            ArrayList var4 = this.getAllPhotosArray();
            if (var3 >= 0 && var3 < var4.size()) {
               PhotoViewer.getInstance().setParentActivity(this.baseFragment.getParentActivity());
               PhotoViewer.getInstance().setParentAlert(this);
               PhotoViewer.getInstance().setMaxSelectedPhotos(this.maxSelectedPhotos);
               var5 = this.baseFragment;
               ChatActivity var6;
               byte var7;
               if (var5 instanceof ChatActivity) {
                  var6 = (ChatActivity)var5;
                  var7 = 0;
               } else {
                  var6 = null;
                  var7 = 4;
               }

               PhotoViewer.getInstance().openPhotoForSelect(var4, var3, var7, this.photoViewerProvider, var6);
               AndroidUtilities.hideKeyboard(this.baseFragment.getFragmentView().findFocus());
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$new$1$ChatAttachAlert(BaseFragment var1, View var2) {
      if (!this.buttonPressed) {
         Integer var3 = (Integer)var2.getTag();
         if (this.deviceHasGoodCamera && var3 == 0 && this.baseFragment instanceof ChatActivity && ((ChatActivity)var1).isSecretChat()) {
            this.openCamera(true);
         } else {
            this.buttonPressed = true;
            this.delegate.didPressedButton(var3);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$new$2$ChatAttachAlert(View var1) {
      if (this.cameraView != null) {
         this.openPhotoViewer((MediaController.PhotoEntry)null, false, false);
         CameraController.getInstance().stopPreview(this.cameraView.getCameraSession());
      }
   }

   // $FF: synthetic method
   public void lambda$new$3$ChatAttachAlert(View var1) {
      if (!this.takingPhoto) {
         CameraView var2 = this.cameraView;
         if (var2 != null && var2.isInitied()) {
            this.cameraInitied = false;
            this.cameraView.switchCamera();
            ObjectAnimator var3 = ObjectAnimator.ofFloat(this.switchCameraButton, "scaleX", new float[]{0.0F}).setDuration(100L);
            var3.addListener(new ChatAttachAlert$11(this));
            var3.start();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$new$4$ChatAttachAlert(View var1) {
      if (!this.flashAnimationInProgress) {
         CameraView var2 = this.cameraView;
         if (var2 != null && var2.isInitied() && this.cameraOpened) {
            String var4 = this.cameraView.getCameraSession().getCurrentFlashMode();
            String var3 = this.cameraView.getCameraSession().getNextFlashMode();
            if (var4.equals(var3)) {
               return;
            }

            this.cameraView.getCameraSession().setCurrentFlashMode(var3);
            this.flashAnimationInProgress = true;
            ImageView[] var5 = this.flashModeButton;
            ImageView var6;
            if (var5[0] == var1) {
               var6 = var5[1];
            } else {
               var6 = var5[0];
            }

            var6.setVisibility(0);
            this.setCameraFlashModeIcon(var6, var3);
            AnimatorSet var7 = new AnimatorSet();
            var7.playTogether(new Animator[]{ObjectAnimator.ofFloat(var1, "translationY", new float[]{0.0F, (float)AndroidUtilities.dp(48.0F)}), ObjectAnimator.ofFloat(var6, "translationY", new float[]{(float)(-AndroidUtilities.dp(48.0F)), 0.0F}), ObjectAnimator.ofFloat(var1, "alpha", new float[]{1.0F, 0.0F}), ObjectAnimator.ofFloat(var6, "alpha", new float[]{0.0F, 1.0F})});
            var7.setDuration(200L);
            var7.addListener(new ChatAttachAlert$12(this, var1, var6));
            var7.start();
         }
      }

   }

   public void loadGalleryPhotos() {
      MediaController.AlbumEntry var1;
      if (this.baseFragment instanceof ChatActivity) {
         var1 = MediaController.allMediaAlbumEntry;
      } else {
         var1 = MediaController.allPhotosAlbumEntry;
      }

      if (var1 == null && VERSION.SDK_INT >= 21) {
         MediaController.loadGalleryPhotosAlbums(0);
      }

   }

   public void onActivityResultFragment(int param1, Intent param2, String param3) {
      // $FF: Couldn't be decompiled
   }

   protected boolean onContainerTouchEvent(MotionEvent var1) {
      boolean var2;
      if (this.cameraOpened && this.processTouchEvent(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   protected boolean onCustomCloseAnimation() {
      if (this.useRevealAnimation) {
         this.setUseRevealAnimation(true);
      }

      if (this.useRevealAnimation) {
         super.backDrawable.setAlpha(51);
         this.startRevealAnimation(false);
         return true;
      } else {
         return false;
      }
   }

   protected boolean onCustomLayout(View var1, int var2, int var3, int var4, int var5) {
      int var6 = var4 - var2;
      int var7 = var5 - var3;
      boolean var10;
      if (var6 < var7) {
         var10 = true;
      } else {
         var10 = false;
      }

      if (var1 == this.cameraPanel) {
         if (var10) {
            if (this.cameraPhotoRecyclerView.getVisibility() == 0) {
               this.cameraPanel.layout(0, var5 - AndroidUtilities.dp(196.0F), var6, var5 - AndroidUtilities.dp(96.0F));
            } else {
               this.cameraPanel.layout(0, var5 - AndroidUtilities.dp(100.0F), var6, var5);
            }
         } else if (this.cameraPhotoRecyclerView.getVisibility() == 0) {
            this.cameraPanel.layout(var4 - AndroidUtilities.dp(196.0F), 0, var4 - AndroidUtilities.dp(96.0F), var7);
         } else {
            this.cameraPanel.layout(var4 - AndroidUtilities.dp(100.0F), 0, var4, var7);
         }

         return true;
      } else {
         TextView var8 = this.counterTextView;
         if (var1 == var8) {
            if (var10) {
               var4 = (var6 - var8.getMeasuredWidth()) / 2;
               var5 -= AndroidUtilities.dp(154.0F);
               this.counterTextView.setRotation(0.0F);
               var2 = var4;
               var3 = var5;
               if (this.cameraPhotoRecyclerView.getVisibility() == 0) {
                  var3 = var5 - AndroidUtilities.dp(96.0F);
                  var2 = var4;
               }
            } else {
               var5 = var4 - AndroidUtilities.dp(154.0F);
               var4 = var7 / 2 + this.counterTextView.getMeasuredWidth() / 2;
               this.counterTextView.setRotation(-90.0F);
               var2 = var5;
               var3 = var4;
               if (this.cameraPhotoRecyclerView.getVisibility() == 0) {
                  var2 = var5 - AndroidUtilities.dp(96.0F);
                  var3 = var4;
               }
            }

            TextView var9 = this.counterTextView;
            var9.layout(var2, var3, var9.getMeasuredWidth() + var2, this.counterTextView.getMeasuredHeight() + var3);
            return true;
         } else if (var1 == this.cameraPhotoRecyclerView) {
            if (var10) {
               var2 = var7 - AndroidUtilities.dp(88.0F);
               var1.layout(0, var2, var1.getMeasuredWidth(), var1.getMeasuredHeight() + var2);
            } else {
               var2 = var2 + var6 - AndroidUtilities.dp(88.0F);
               var1.layout(var2, 0, var1.getMeasuredWidth() + var2, var1.getMeasuredHeight());
            }

            return true;
         } else {
            return false;
         }
      }
   }

   protected boolean onCustomMeasure(View var1, int var2, int var3) {
      boolean var4;
      if (var2 < var3) {
         var4 = true;
      } else {
         var4 = false;
      }

      CameraView var5 = this.cameraView;
      if (var1 == var5) {
         if (this.cameraOpened && !this.cameraAnimationInProgress) {
            var5.measure(MeasureSpec.makeMeasureSpec(var2, 1073741824), MeasureSpec.makeMeasureSpec(var3, 1073741824));
            return true;
         }
      } else {
         FrameLayout var6 = this.cameraPanel;
         if (var1 == var6) {
            if (var4) {
               var6.measure(MeasureSpec.makeMeasureSpec(var2, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0F), 1073741824));
            } else {
               var6.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0F), 1073741824), MeasureSpec.makeMeasureSpec(var3, 1073741824));
            }

            return true;
         }

         RecyclerListView var7 = this.cameraPhotoRecyclerView;
         if (var1 == var7) {
            this.cameraPhotoRecyclerViewIgnoreLayout = true;
            if (var4) {
               var7.measure(MeasureSpec.makeMeasureSpec(var2, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0F), 1073741824));
               if (this.cameraPhotoLayoutManager.getOrientation() != 0) {
                  this.cameraPhotoRecyclerView.setPadding(AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(8.0F), 0);
                  this.cameraPhotoLayoutManager.setOrientation(0);
                  this.cameraAttachAdapter.notifyDataSetChanged();
               }
            } else {
               var7.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0F), 1073741824), MeasureSpec.makeMeasureSpec(var3, 1073741824));
               if (this.cameraPhotoLayoutManager.getOrientation() != 1) {
                  this.cameraPhotoRecyclerView.setPadding(0, AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(8.0F));
                  this.cameraPhotoLayoutManager.setOrientation(1);
                  this.cameraAttachAdapter.notifyDataSetChanged();
               }
            }

            this.cameraPhotoRecyclerViewIgnoreLayout = false;
            return true;
         }
      }

      return false;
   }

   protected boolean onCustomOpenAnimation() {
      if (this.useRevealAnimation) {
         this.setUseRevealAnimation(true);
      }

      if (this.baseFragment instanceof ChatActivity) {
         this.updatePollMusicButton();
         TLRPC.Chat var1 = ((ChatActivity)this.baseFragment).getCurrentChat();
         if (var1 != null) {
            this.mediaEnabled = ChatObject.canSendMedia(var1);
            int var2 = 0;

            while(true) {
               float var3 = 1.0F;
               float var6;
               if (var2 >= 5) {
                  RecyclerListView var13 = this.attachPhotoRecyclerView;
                  if (this.mediaEnabled) {
                     var6 = 1.0F;
                  } else {
                     var6 = 0.2F;
                  }

                  var13.setAlpha(var6);
                  this.attachPhotoRecyclerView.setEnabled(this.mediaEnabled);
                  if (!this.mediaEnabled) {
                     if (ChatObject.isActionBannedByDefault(var1, 7)) {
                        this.mediaBanTooltip.setText(LocaleController.getString("GlobalAttachMediaRestricted", 2131559592));
                     } else if (AndroidUtilities.isBannedForever(var1.banned_rights)) {
                        this.mediaBanTooltip.setText(LocaleController.formatString("AttachMediaRestrictedForever", 2131558725));
                     } else {
                        this.mediaBanTooltip.setText(LocaleController.formatString("AttachMediaRestricted", 2131558724, LocaleController.formatDateForBan((long)var1.banned_rights.until_date)));
                     }
                  }

                  CorrectlyMeasuringTextView var8 = this.mediaBanTooltip;
                  byte var11;
                  if (this.mediaEnabled) {
                     var11 = 4;
                  } else {
                     var11 = 0;
                  }

                  var8.setVisibility(var11);
                  CameraView var9 = this.cameraView;
                  if (var9 != null) {
                     if (this.mediaEnabled) {
                        var6 = 1.0F;
                     } else {
                        var6 = 0.2F;
                     }

                     var9.setAlpha(var6);
                     this.cameraView.setEnabled(this.mediaEnabled);
                  }

                  FrameLayout var10 = this.cameraIcon;
                  if (var10 != null) {
                     if (!this.mediaEnabled) {
                        var3 = 0.2F;
                     }

                     var10.setAlpha(var3);
                     this.cameraIcon.setEnabled(this.mediaEnabled);
                  }
                  break;
               }

               label96: {
                  if (var2 > 2) {
                     MessageObject var4 = this.editingMessageObject;
                     if (var4 != null && var4.hasValidGroupId()) {
                        ((ChatAttachAlert.AttachButton)this.attachButtons.get(var2)).setEnabled(false);
                        ((ChatAttachAlert.AttachButton)this.attachButtons.get(var2)).setAlpha(0.2F);
                        break label96;
                     }
                  }

                  ChatAttachAlert.AttachButton var5 = (ChatAttachAlert.AttachButton)this.attachButtons.get(var2);
                  Integer var12 = (Integer)var5.getTag();
                  var6 = var3;
                  if (!this.mediaEnabled) {
                     if (var12 == 9) {
                        var6 = var3;
                     } else {
                        var6 = 0.2F;
                     }
                  }

                  var5.setAlpha(var6);
                  boolean var7;
                  if (!this.mediaEnabled && var12 != 9) {
                     var7 = false;
                  } else {
                     var7 = true;
                  }

                  var5.setEnabled(var7);
               }

               ++var2;
            }
         }
      }

      if (this.useRevealAnimation) {
         this.startRevealAnimation(true);
         return true;
      } else {
         return false;
      }
   }

   public void onDestroy() {
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.albumsDidLoad);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.reloadInlineHints);
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.cameraInitied);
      this.baseFragment = null;
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      if (!this.cameraOpened || var1 != 24 && var1 != 25) {
         return super.onKeyDown(var1, var2);
      } else {
         this.shutterButton.getDelegate().shutterReleased();
         return true;
      }
   }

   public void onOpenAnimationEnd() {
      this.onRevealAnimationEnd(true);
   }

   public void onOpenAnimationStart() {
   }

   public void onPause() {
      ShutterButton var1 = this.shutterButton;
      if (var1 != null) {
         if (!this.requestingPermissions) {
            if (this.cameraView != null && var1.getState() == ShutterButton.State.RECORDING) {
               this.resetRecordState();
               CameraController.getInstance().stopVideoRecording(this.cameraView.getCameraSession(), false);
               this.shutterButton.setState(ShutterButton.State.DEFAULT, true);
            }

            if (this.cameraOpened) {
               this.closeCamera(false);
            }

            this.hideCamera(true);
         } else {
            if (this.cameraView != null && var1.getState() == ShutterButton.State.RECORDING) {
               this.shutterButton.setState(ShutterButton.State.DEFAULT, true);
            }

            this.requestingPermissions = false;
         }

         this.paused = true;
      }
   }

   public void onResume() {
      this.paused = false;
      if (this.isShowing() && !this.isDismissed()) {
         this.checkCamera(false);
      }

   }

   public void setAllowDrawContent(boolean var1) {
      super.setAllowDrawContent(var1);
      this.checkCameraViewPosition();
   }

   @Keep
   public void setCameraOpenProgress(float var1) {
      if (this.cameraView != null) {
         this.cameraOpenProgress = var1;
         int[] var2 = this.animateCameraValues;
         float var3 = (float)var2[1];
         float var4 = (float)var2[2];
         android.graphics.Point var10 = AndroidUtilities.displaySize;
         boolean var5;
         if (var10.x < var10.y) {
            var5 = true;
         } else {
            var5 = false;
         }

         float var6;
         int var12;
         if (var5) {
            var6 = (float)(super.container.getWidth() - this.getLeftInset() - this.getRightInset());
            var12 = super.container.getHeight();
         } else {
            var6 = (float)(super.container.getWidth() - this.getLeftInset() - this.getRightInset());
            var12 = super.container.getHeight();
         }

         float var7 = (float)var12;
         if (var1 == 0.0F) {
            this.cameraView.setClipLeft(this.cameraViewOffsetX);
            this.cameraView.setClipTop(this.cameraViewOffsetY);
            this.cameraView.setTranslationX((float)this.cameraViewLocation[0]);
            this.cameraView.setTranslationY((float)this.cameraViewLocation[1]);
            this.cameraIcon.setTranslationX((float)this.cameraViewLocation[0]);
            this.cameraIcon.setTranslationY((float)this.cameraViewLocation[1]);
         } else if (this.cameraView.getTranslationX() != 0.0F || this.cameraView.getTranslationY() != 0.0F) {
            this.cameraView.setTranslationX(0.0F);
            this.cameraView.setTranslationY(0.0F);
         }

         LayoutParams var11 = (LayoutParams)this.cameraView.getLayoutParams();
         var11.width = (int)(var3 + (var6 - var3) * var1);
         var11.height = (int)(var4 + (var7 - var4) * var1);
         if (var1 != 0.0F) {
            CameraView var8 = this.cameraView;
            var3 = (float)this.cameraViewOffsetX;
            var6 = 1.0F - var1;
            var8.setClipLeft((int)(var3 * var6));
            this.cameraView.setClipTop((int)((float)this.cameraViewOffsetY * var6));
            int[] var13 = this.cameraViewLocation;
            var11.leftMargin = (int)((float)var13[0] * var6);
            int[] var9 = this.animateCameraValues;
            var11.topMargin = (int)((float)var9[0] + (float)(var13[1] - var9[0]) * var6);
         } else {
            var11.leftMargin = 0;
            var11.topMargin = 0;
         }

         this.cameraView.setLayoutParams(var11);
         if (var1 <= 0.5F) {
            this.cameraIcon.setAlpha(1.0F - var1 / 0.5F);
         } else {
            this.cameraIcon.setAlpha(0.0F);
         }

      }
   }

   public void setDelegate(ChatAttachAlert.ChatAttachViewDelegate var1) {
      this.delegate = var1;
   }

   public void setEditingMessageObject(MessageObject var1) {
      if (this.editingMessageObject != var1) {
         this.editingMessageObject = var1;
         if (this.editingMessageObject != null) {
            this.maxSelectedPhotos = 1;
         } else {
            this.maxSelectedPhotos = -1;
         }

         this.adapter.notifyDataSetChanged();

         for(int var2 = 0; var2 < 4; ++var2) {
            ChatAttachAlert.AttachButton var3;
            boolean var4;
            label39: {
               label38: {
                  var3 = (ChatAttachAlert.AttachButton)this.attachButtons.get(var2 + 3);
                  if (var2 < 2) {
                     var1 = this.editingMessageObject;
                     if (var1 != null && var1.hasValidGroupId()) {
                        break label38;
                     }
                  } else if (this.editingMessageObject != null) {
                     break label38;
                  }

                  var4 = true;
                  break label39;
               }

               var4 = false;
            }

            var3.setEnabled(var4);
            float var5;
            if (var4) {
               var5 = 1.0F;
            } else {
               var5 = 0.2F;
            }

            var3.setAlpha(var5);
         }

         this.updatePollMusicButton();
      }
   }

   public void setMaxSelectedPhotos(int var1) {
      this.maxSelectedPhotos = var1;
   }

   public void setOpenWithFrontFaceCamera(boolean var1) {
      this.openWithFrontFaceCamera = var1;
   }

   @SuppressLint({"NewApi"})
   @Keep
   protected void setRevealRadius(float var1) {
      this.revealRadius = var1;
      if (VERSION.SDK_INT <= 19) {
         this.listView.invalidate();
      }

      if (!this.isDismissed()) {
         for(int var2 = 0; var2 < this.innerAnimators.size(); ++var2) {
            ChatAttachAlert.InnerAnimator var3 = (ChatAttachAlert.InnerAnimator)this.innerAnimators.get(var2);
            if (var3.startRadius <= var1) {
               var3.animatorSet.start();
               this.innerAnimators.remove(var2);
               --var2;
            }
         }
      }

   }

   public void show() {
      super.show();
      this.buttonPressed = false;
   }

   public void showCamera() {
      if (!this.paused && this.mediaEnabled) {
         if (this.cameraView == null) {
            this.cameraView = new CameraView(this.baseFragment.getParentActivity(), this.openWithFrontFaceCamera);
            this.cameraView.setFocusable(true);
            this.cameraView.setContentDescription(LocaleController.getString("AccDescrInstantCamera", 2131558441));
            super.container.addView(this.cameraView, 1, LayoutHelper.createFrame(80, 80.0F));
            this.cameraView.setDelegate(new CameraView.CameraViewDelegate() {
               public void onCameraCreated(Camera var1) {
               }

               public void onCameraInit() {
                  int var1 = ChatAttachAlert.this.attachPhotoRecyclerView.getChildCount();
                  byte var2 = 0;

                  int var3;
                  for(var3 = 0; var3 < var1; ++var3) {
                     View var4 = ChatAttachAlert.this.attachPhotoRecyclerView.getChildAt(var3);
                     if (var4 instanceof PhotoAttachCameraCell) {
                        var4.setVisibility(4);
                        break;
                     }
                  }

                  ImageView var8;
                  if (ChatAttachAlert.this.cameraView.getCameraSession().getCurrentFlashMode().equals(ChatAttachAlert.this.cameraView.getCameraSession().getNextFlashMode())) {
                     for(var3 = 0; var3 < 2; ++var3) {
                        ChatAttachAlert.this.flashModeButton[var3].setVisibility(4);
                        ChatAttachAlert.this.flashModeButton[var3].setAlpha(0.0F);
                        ChatAttachAlert.this.flashModeButton[var3].setTranslationY(0.0F);
                     }
                  } else {
                     ChatAttachAlert var7 = ChatAttachAlert.this;
                     var7.setCameraFlashModeIcon(var7.flashModeButton[0], ChatAttachAlert.this.cameraView.getCameraSession().getCurrentFlashMode());

                     for(var3 = 0; var3 < 2; ++var3) {
                        var8 = ChatAttachAlert.this.flashModeButton[var3];
                        byte var6;
                        if (var3 == 0) {
                           var6 = 0;
                        } else {
                           var6 = 4;
                        }

                        var8.setVisibility(var6);
                        var8 = ChatAttachAlert.this.flashModeButton[var3];
                        float var5;
                        if (var3 == 0 && ChatAttachAlert.this.cameraOpened) {
                           var5 = 1.0F;
                        } else {
                           var5 = 0.0F;
                        }

                        var8.setAlpha(var5);
                        ChatAttachAlert.this.flashModeButton[var3].setTranslationY(0.0F);
                     }
                  }

                  var8 = ChatAttachAlert.this.switchCameraButton;
                  if (ChatAttachAlert.this.cameraView.isFrontface()) {
                     var3 = 2131165335;
                  } else {
                     var3 = 2131165336;
                  }

                  var8.setImageResource(var3);
                  var8 = ChatAttachAlert.this.switchCameraButton;
                  byte var9;
                  if (ChatAttachAlert.this.cameraView.hasFrontFaceCamera()) {
                     var9 = var2;
                  } else {
                     var9 = 4;
                  }

                  var8.setVisibility(var9);
               }
            });
            if (this.cameraIcon == null) {
               this.cameraIcon = new FrameLayout(this.baseFragment.getParentActivity());
               this.cameraImageView = new ImageView(this.baseFragment.getParentActivity());
               this.cameraImageView.setScaleType(ScaleType.CENTER);
               this.cameraImageView.setImageResource(2131165495);
               this.cameraImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogCameraIcon"), Mode.MULTIPLY));
               this.cameraIcon.addView(this.cameraImageView, LayoutHelper.createFrame(80, 80, 85));
            }

            super.container.addView(this.cameraIcon, 2, LayoutHelper.createFrame(80, 80.0F));
            CameraView var1 = this.cameraView;
            boolean var2 = this.mediaEnabled;
            float var3 = 1.0F;
            float var4;
            if (var2) {
               var4 = 1.0F;
            } else {
               var4 = 0.2F;
            }

            var1.setAlpha(var4);
            this.cameraView.setEnabled(this.mediaEnabled);
            FrameLayout var5 = this.cameraIcon;
            if (this.mediaEnabled) {
               var4 = var3;
            } else {
               var4 = 0.2F;
            }

            var5.setAlpha(var4);
            this.cameraIcon.setEnabled(this.mediaEnabled);
         }

         this.cameraView.setTranslationX((float)this.cameraViewLocation[0]);
         this.cameraView.setTranslationY((float)this.cameraViewLocation[1]);
         this.cameraIcon.setTranslationX((float)this.cameraViewLocation[0]);
         this.cameraIcon.setTranslationY((float)this.cameraViewLocation[1]);
      }

   }

   public void updatePhotosButton() {
      int var1 = selectedPhotos.size();
      if (var1 == 0) {
         this.sendPhotosButton.imageView.setBackgroundDrawable(Theme.chat_attachButtonDrawables[7]);
         this.sendPhotosButton.textView.setText("");
         this.sendPhotosButton.textView.setContentDescription(LocaleController.getString("Close", 2131559117));
         if (this.baseFragment instanceof ChatActivity) {
            this.sendDocumentsButton.textView.setText(LocaleController.getString("ChatDocument", 2131559027));
         }
      } else {
         this.sendPhotosButton.imageView.setBackgroundDrawable(Theme.chat_attachButtonDrawables[8]);
         this.sendPhotosButton.textView.setContentDescription((CharSequence)null);
         if (this.baseFragment instanceof ChatActivity) {
            this.sendPhotosButton.textView.setText(LocaleController.formatString("SendItems", 2131560694, String.format("(%d)", var1)));
            MessageObject var2 = this.editingMessageObject;
            if (var2 == null || !var2.hasValidGroupId()) {
               TextView var3 = this.sendDocumentsButton.textView;
               String var4;
               if (var1 == 1) {
                  var1 = 2131560686;
                  var4 = "SendAsFile";
               } else {
                  var1 = 2131560687;
                  var4 = "SendAsFiles";
               }

               var3.setText(LocaleController.getString(var4, var1));
            }
         } else {
            this.sendPhotosButton.textView.setText(LocaleController.formatString("UploadItems", 2131560965, String.format("(%d)", var1)));
         }
      }

      if (VERSION.SDK_INT >= 23 && this.getContext().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
         this.progressView.setText(LocaleController.getString("PermissionStorage", 2131560420));
         this.progressView.setTextSize(16);
      } else {
         this.progressView.setText(LocaleController.getString("NoPhotos", 2131559937));
         this.progressView.setTextSize(20);
      }

   }

   private class AttachBotButton extends FrameLayout {
      private AvatarDrawable avatarDrawable = new AvatarDrawable();
      private boolean checkingForLongPress = false;
      private TLRPC.User currentUser;
      private BackupImageView imageView;
      private TextView nameTextView;
      private ChatAttachAlert.AttachBotButton.CheckForLongPress pendingCheckForLongPress = null;
      private ChatAttachAlert.AttachBotButton.CheckForTap pendingCheckForTap = null;
      private int pressCount = 0;
      private boolean pressed;

      public AttachBotButton(Context var2) {
         super(var2);
         this.imageView = new BackupImageView(var2);
         this.imageView.setRoundRadius(AndroidUtilities.dp(27.0F));
         this.addView(this.imageView, LayoutHelper.createFrame(54, 54.0F, 49, 0.0F, 7.0F, 0.0F, 0.0F));
         this.nameTextView = new TextView(var2);
         this.nameTextView.setTextSize(1, 12.0F);
         this.nameTextView.setMaxLines(2);
         this.nameTextView.setGravity(49);
         this.nameTextView.setLines(2);
         this.nameTextView.setEllipsize(TruncateAt.END);
         this.addView(this.nameTextView, LayoutHelper.createFrame(-1, -2.0F, 51, 6.0F, 65.0F, 6.0F, 0.0F));
      }

      // $FF: synthetic method
      static int access$1304(ChatAttachAlert.AttachBotButton var0) {
         int var1 = var0.pressCount + 1;
         var0.pressCount = var1;
         return var1;
      }

      private void onLongPress() {
         if (ChatAttachAlert.this.baseFragment != null && this.currentUser != null) {
            AlertDialog.Builder var1 = new AlertDialog.Builder(this.getContext());
            var1.setTitle(LocaleController.getString("AppName", 2131558635));
            TLRPC.User var2 = this.currentUser;
            var1.setMessage(LocaleController.formatString("ChatHintsDelete", 2131559030, ContactsController.formatName(var2.first_name, var2.last_name)));
            var1.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$ChatAttachAlert$AttachBotButton$gsYaOE0UOZF51zKXNIpWWEDBs1k(this));
            var1.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            var1.show();
         }

      }

      protected void cancelCheckLongPress() {
         this.checkingForLongPress = false;
         ChatAttachAlert.AttachBotButton.CheckForLongPress var1 = this.pendingCheckForLongPress;
         if (var1 != null) {
            this.removeCallbacks(var1);
         }

         ChatAttachAlert.AttachBotButton.CheckForTap var2 = this.pendingCheckForTap;
         if (var2 != null) {
            this.removeCallbacks(var2);
         }

      }

      // $FF: synthetic method
      public void lambda$onLongPress$0$ChatAttachAlert$AttachBotButton(DialogInterface var1, int var2) {
         DataQuery.getInstance(ChatAttachAlert.this.currentAccount).removeInline(this.currentUser.id);
      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(85.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0F), 1073741824));
      }

      public boolean onTouchEvent(MotionEvent var1) {
         int var2 = var1.getAction();
         boolean var3 = true;
         if (var2 == 0) {
            this.pressed = true;
            this.invalidate();
         } else {
            if (this.pressed) {
               if (var1.getAction() == 1) {
                  this.getParent().requestDisallowInterceptTouchEvent(true);
                  this.pressed = false;
                  this.playSoundEffect(0);
                  ChatAttachAlert.this.delegate.didSelectBot(MessagesController.getInstance(ChatAttachAlert.this.currentAccount).getUser(((TLRPC.TL_topPeer)DataQuery.getInstance(ChatAttachAlert.this.currentAccount).inlineBots.get((Integer)this.getTag())).peer.user_id));
                  ChatAttachAlert.this.setUseRevealAnimation(false);
                  ChatAttachAlert.this.dismiss();
                  ChatAttachAlert.this.setUseRevealAnimation(true);
                  this.invalidate();
               } else if (var1.getAction() == 3) {
                  this.pressed = false;
                  this.invalidate();
               }
            }

            var3 = false;
         }

         boolean var4;
         if (!var3) {
            var4 = super.onTouchEvent(var1);
         } else {
            var4 = var3;
            if (var1.getAction() == 0) {
               this.startCheckLongPress();
               var4 = var3;
            }
         }

         if (var1.getAction() != 0 && var1.getAction() != 2) {
            this.cancelCheckLongPress();
         }

         return var4;
      }

      public void setUser(TLRPC.User var1) {
         if (var1 != null) {
            this.nameTextView.setTextColor(Theme.getColor("dialogTextGray2"));
            this.currentUser = var1;
            this.nameTextView.setText(ContactsController.formatName(var1.first_name, var1.last_name));
            this.avatarDrawable.setInfo(var1);
            this.imageView.setImage((ImageLocation)ImageLocation.getForUser(var1, false), "50_50", (Drawable)this.avatarDrawable, (Object)var1);
            this.requestLayout();
         }
      }

      protected void startCheckLongPress() {
         if (!this.checkingForLongPress) {
            this.checkingForLongPress = true;
            if (this.pendingCheckForTap == null) {
               this.pendingCheckForTap = new ChatAttachAlert.AttachBotButton.CheckForTap();
            }

            this.postDelayed(this.pendingCheckForTap, (long)ViewConfiguration.getTapTimeout());
         }
      }

      class CheckForLongPress implements Runnable {
         public int currentPressCount;

         public void run() {
            if (AttachBotButton.this.checkingForLongPress && AttachBotButton.this.getParent() != null && this.currentPressCount == AttachBotButton.this.pressCount) {
               AttachBotButton.this.checkingForLongPress = false;
               AttachBotButton.this.performHapticFeedback(0);
               AttachBotButton.this.onLongPress();
               MotionEvent var1 = MotionEvent.obtain(0L, 0L, 3, 0.0F, 0.0F, 0);
               AttachBotButton.this.onTouchEvent(var1);
               var1.recycle();
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
            ChatAttachAlert.AttachBotButton var1;
            if (AttachBotButton.this.pendingCheckForLongPress == null) {
               var1 = AttachBotButton.this;
               var1.pendingCheckForLongPress = var1.new CheckForLongPress();
            }

            AttachBotButton.this.pendingCheckForLongPress.currentPressCount = ChatAttachAlert.AttachBotButton.access$1304(AttachBotButton.this);
            var1 = AttachBotButton.this;
            var1.postDelayed(var1.pendingCheckForLongPress, (long)(ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout()));
         }
      }
   }

   private class AttachButton extends FrameLayout {
      private ImageView imageView;
      private TextView textView;

      public AttachButton(Context var2) {
         super(var2);
         this.imageView = new ImageView(var2);
         this.imageView.setScaleType(ScaleType.CENTER);
         this.addView(this.imageView, LayoutHelper.createFrame(54, 54.0F, 49, 0.0F, 5.0F, 0.0F, 0.0F));
         this.textView = new TextView(var2);
         this.textView.setMaxLines(2);
         this.textView.setGravity(1);
         this.textView.setEllipsize(TruncateAt.END);
         this.textView.setTextColor(Theme.getColor("dialogTextGray2"));
         this.textView.setTextSize(1, 12.0F);
         this.textView.setLineSpacing((float)(-AndroidUtilities.dp(2.0F)), 1.0F);
         this.addView(this.textView, LayoutHelper.createFrame(-1, -2.0F, 51, 0.0F, 64.0F, 0.0F, 0.0F));
      }

      public boolean hasOverlappingRendering() {
         return false;
      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(85.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(92.0F), 1073741824));
      }

      public void setTextAndIcon(CharSequence var1, Drawable var2) {
         this.textView.setText(var1);
         this.imageView.setBackgroundDrawable(var2);
      }
   }

   private class BasePhotoProvider extends PhotoViewer.EmptyPhotoViewerProvider {
      private BasePhotoProvider() {
      }

      // $FF: synthetic method
      BasePhotoProvider(Object var2) {
         this();
      }

      public boolean allowGroupPhotos() {
         return ChatAttachAlert.this.delegate.allowGroupPhotos();
      }

      public int getPhotoIndex(int var1) {
         MediaController.PhotoEntry var2 = ChatAttachAlert.this.getPhotoEntryAtPosition(var1);
         return var2 == null ? -1 : ChatAttachAlert.selectedPhotosOrder.indexOf(var2.imageId);
      }

      public int getSelectedCount() {
         return ChatAttachAlert.selectedPhotos.size();
      }

      public HashMap getSelectedPhotos() {
         return ChatAttachAlert.selectedPhotos;
      }

      public ArrayList getSelectedPhotosOrder() {
         return ChatAttachAlert.selectedPhotosOrder;
      }

      public boolean isPhotoChecked(int var1) {
         MediaController.PhotoEntry var2 = ChatAttachAlert.this.getPhotoEntryAtPosition(var1);
         boolean var3;
         if (var2 != null && ChatAttachAlert.selectedPhotos.containsKey(var2.imageId)) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public int setPhotoChecked(int var1, VideoEditedInfo var2) {
         if (ChatAttachAlert.this.maxSelectedPhotos >= 0 && ChatAttachAlert.selectedPhotos.size() >= ChatAttachAlert.this.maxSelectedPhotos && !this.isPhotoChecked(var1)) {
            return -1;
         } else {
            MediaController.PhotoEntry var3 = ChatAttachAlert.this.getPhotoEntryAtPosition(var1);
            if (var3 == null) {
               return -1;
            } else {
               int var4 = ChatAttachAlert.this.addToSelectedPhotos(var3, -1);
               boolean var5;
               if (var4 == -1) {
                  var4 = ChatAttachAlert.selectedPhotosOrder.indexOf(var3.imageId);
                  var5 = true;
               } else {
                  var3.editedInfo = null;
                  var5 = false;
               }

               var3.editedInfo = var2;
               int var6 = ChatAttachAlert.this.attachPhotoRecyclerView.getChildCount();

               int var7;
               View var8;
               for(var7 = 0; var7 < var6; ++var7) {
                  var8 = ChatAttachAlert.this.attachPhotoRecyclerView.getChildAt(var7);
                  if (var8 instanceof PhotoAttachPhotoCell && (Integer)var8.getTag() == var1) {
                     if (ChatAttachAlert.this.baseFragment instanceof ChatActivity && ChatAttachAlert.this.maxSelectedPhotos < 0) {
                        ((PhotoAttachPhotoCell)var8).setChecked(var4, var5, false);
                     } else {
                        ((PhotoAttachPhotoCell)var8).setChecked(-1, var5, false);
                     }
                     break;
                  }
               }

               var6 = ChatAttachAlert.this.cameraPhotoRecyclerView.getChildCount();

               for(var7 = 0; var7 < var6; ++var7) {
                  var8 = ChatAttachAlert.this.cameraPhotoRecyclerView.getChildAt(var7);
                  if (var8 instanceof PhotoAttachPhotoCell && (Integer)var8.getTag() == var1) {
                     if (ChatAttachAlert.this.baseFragment instanceof ChatActivity && ChatAttachAlert.this.maxSelectedPhotos < 0) {
                        ((PhotoAttachPhotoCell)var8).setChecked(var4, var5, false);
                     } else {
                        ((PhotoAttachPhotoCell)var8).setChecked(-1, var5, false);
                     }
                     break;
                  }
               }

               ChatAttachAlert.this.updatePhotosButton();
               return var4;
            }
         }
      }
   }

   public interface ChatAttachViewDelegate {
      boolean allowGroupPhotos();

      void didPressedButton(int var1);

      void didSelectBot(TLRPC.User var1);

      View getRevealView();

      void onCameraOpened();
   }

   private class InnerAnimator {
      private AnimatorSet animatorSet;
      private float startRadius;

      private InnerAnimator() {
      }

      // $FF: synthetic method
      InnerAnimator(Object var2) {
         this();
      }
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         if (ChatAttachAlert.this.editingMessageObject == null && ChatAttachAlert.this.baseFragment instanceof ChatActivity) {
            int var1;
            if (!DataQuery.getInstance(ChatAttachAlert.this.currentAccount).inlineBots.isEmpty()) {
               var1 = (int)Math.ceil((double)((float)DataQuery.getInstance(ChatAttachAlert.this.currentAccount).inlineBots.size() / 4.0F)) + 1;
            } else {
               var1 = 0;
            }

            return var1 + 1;
         } else {
            return 1;
         }
      }

      public int getItemViewType(int var1) {
         if (var1 != 0) {
            return var1 != 1 ? 2 : 1;
         } else {
            return 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var2 == 1) {
            var1.itemView.setBackgroundColor(Theme.getColor("dialogBackgroundGray"));
         } else if (var2 > 1) {
            FrameLayout var6 = (FrameLayout)var1.itemView;

            for(int var3 = 0; var3 < 4; ++var3) {
               ChatAttachAlert.AttachBotButton var4 = (ChatAttachAlert.AttachBotButton)var6.getChildAt(var3);
               int var5 = (var2 - 2) * 4 + var3;
               if (var5 >= DataQuery.getInstance(ChatAttachAlert.this.currentAccount).inlineBots.size()) {
                  var4.setVisibility(4);
               } else {
                  var4.setVisibility(0);
                  var4.setTag(var5);
                  var4.setUser(MessagesController.getInstance(ChatAttachAlert.this.currentAccount).getUser(((TLRPC.TL_topPeer)DataQuery.getInstance(ChatAttachAlert.this.currentAccount).inlineBots.get(var5)).peer.user_id));
               }
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               var3 = new FrameLayout(this.mContext) {
                  protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
                     var3 = (var4 - var2 - AndroidUtilities.dp(360.0F)) / 3;

                     for(var2 = 0; var2 < 4; ++var2) {
                        var4 = AndroidUtilities.dp(10.0F) + var2 % 4 * (AndroidUtilities.dp(85.0F) + var3);
                        View var6 = this.getChildAt(var2);
                        var6.layout(var4, 0, var6.getMeasuredWidth() + var4, var6.getMeasuredHeight());
                     }

                  }
               };

               for(var2 = 0; var2 < 4; ++var2) {
                  ((FrameLayout)var3).addView(ChatAttachAlert.this.new AttachBotButton(this.mContext));
               }

               ((FrameLayout)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(100.0F)));
            } else {
               var3 = new FrameLayout(this.mContext);
               ((FrameLayout)var3).addView(new ShadowSectionCell(this.mContext), LayoutHelper.createFrame(-1, -1.0F));
            }
         } else {
            var3 = ChatAttachAlert.this.attachView;
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }

   private class PhotoAttachAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;
      private boolean needCamera;
      private ArrayList viewsCache = new ArrayList(8);

      public PhotoAttachAdapter(Context var2, boolean var3) {
         this.mContext = var2;
         this.needCamera = var3;

         for(int var4 = 0; var4 < 8; ++var4) {
            this.viewsCache.add(this.createHolder());
         }

      }

      public RecyclerListView.Holder createHolder() {
         PhotoAttachPhotoCell var1 = new PhotoAttachPhotoCell(this.mContext);
         var1.setDelegate(new _$$Lambda$ChatAttachAlert$PhotoAttachAdapter$K_1guAmOmM0zF5Rks69_v23fQoI(this));
         return new RecyclerListView.Holder(var1);
      }

      public int getItemCount() {
         byte var1;
         if (this.needCamera && ChatAttachAlert.this.deviceHasGoodCamera) {
            var1 = 1;
         } else {
            var1 = 0;
         }

         int var2 = var1 + ChatAttachAlert.cameraPhotos.size();
         MediaController.AlbumEntry var3;
         if (ChatAttachAlert.this.baseFragment instanceof ChatActivity) {
            var3 = MediaController.allMediaAlbumEntry;
         } else {
            var3 = MediaController.allPhotosAlbumEntry;
         }

         int var4 = var2;
         if (var3 != null) {
            var4 = var2 + var3.photos.size();
         }

         return var4;
      }

      public int getItemViewType(int var1) {
         return this.needCamera && ChatAttachAlert.this.deviceHasGoodCamera && var1 == 0 ? 1 : 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      // $FF: synthetic method
      public void lambda$createHolder$0$ChatAttachAlert$PhotoAttachAdapter(PhotoAttachPhotoCell var1) {
         if (ChatAttachAlert.this.mediaEnabled) {
            int var2 = (Integer)var1.getTag();
            MediaController.PhotoEntry var3 = var1.getPhotoEntry();
            boolean var4 = ChatAttachAlert.selectedPhotos.containsKey(var3.imageId) ^ true;
            if (!var4 || ChatAttachAlert.this.maxSelectedPhotos < 0 || ChatAttachAlert.selectedPhotos.size() < ChatAttachAlert.this.maxSelectedPhotos) {
               int var5;
               if (var4) {
                  var5 = ChatAttachAlert.selectedPhotosOrder.size();
               } else {
                  var5 = -1;
               }

               if (ChatAttachAlert.this.baseFragment instanceof ChatActivity && ChatAttachAlert.this.maxSelectedPhotos < 0) {
                  var1.setChecked(var5, var4, true);
               } else {
                  var1.setChecked(-1, var4, true);
               }

               ChatAttachAlert.this.addToSelectedPhotos(var3, var2);
               if (this == ChatAttachAlert.this.cameraAttachAdapter) {
                  var5 = var2;
                  if (ChatAttachAlert.this.photoAttachAdapter.needCamera) {
                     var5 = var2;
                     if (ChatAttachAlert.this.deviceHasGoodCamera) {
                        var5 = var2 + 1;
                     }
                  }

                  ChatAttachAlert.this.photoAttachAdapter.notifyItemChanged(var5);
               } else {
                  ChatAttachAlert.this.cameraAttachAdapter.notifyItemChanged(var2);
               }

               ChatAttachAlert.this.updatePhotosButton();
            }
         }
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         boolean var3 = this.needCamera;
         boolean var4 = false;
         if (var3 && ChatAttachAlert.this.deviceHasGoodCamera && var2 == 0) {
            if (this.needCamera && ChatAttachAlert.this.deviceHasGoodCamera && var2 == 0) {
               if (ChatAttachAlert.this.cameraView != null && ChatAttachAlert.this.cameraView.isInitied()) {
                  var1.itemView.setVisibility(4);
               } else {
                  var1.itemView.setVisibility(0);
               }
            }
         } else {
            int var5 = var2;
            if (this.needCamera) {
               var5 = var2;
               if (ChatAttachAlert.this.deviceHasGoodCamera) {
                  var5 = var2 - 1;
               }
            }

            PhotoAttachPhotoCell var6 = (PhotoAttachPhotoCell)var1.itemView;
            MediaController.PhotoEntry var8 = ChatAttachAlert.this.getPhotoEntryAtPosition(var5);
            boolean var7 = this.needCamera;
            if (var5 == this.getItemCount() - 1) {
               var3 = true;
            } else {
               var3 = false;
            }

            var6.setPhotoEntry(var8, var7, var3);
            if (ChatAttachAlert.this.baseFragment instanceof ChatActivity && ChatAttachAlert.this.maxSelectedPhotos < 0) {
               var6.setChecked(ChatAttachAlert.selectedPhotosOrder.indexOf(var8.imageId), ChatAttachAlert.selectedPhotos.containsKey(var8.imageId), false);
            } else {
               var6.setChecked(-1, ChatAttachAlert.selectedPhotos.containsKey(var8.imageId), false);
            }

            var6.getImageView().setTag(var5);
            var6.setTag(var5);
            var3 = var4;
            if (this == ChatAttachAlert.this.cameraAttachAdapter) {
               var3 = var4;
               if (ChatAttachAlert.this.cameraPhotoLayoutManager.getOrientation() == 1) {
                  var3 = true;
               }
            }

            var6.setIsVertical(var3);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         RecyclerListView.Holder var3;
         if (var2 != 1) {
            if (!this.viewsCache.isEmpty()) {
               var3 = (RecyclerListView.Holder)this.viewsCache.get(0);
               this.viewsCache.remove(0);
            } else {
               var3 = this.createHolder();
            }
         } else {
            var3 = new RecyclerListView.Holder(new PhotoAttachCameraCell(this.mContext));
         }

         return var3;
      }
   }
}
