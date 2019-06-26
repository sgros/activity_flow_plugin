package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Build.VERSION;
import android.os.PowerManager.WakeLock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.style.ImageSpan;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.Keep;
import androidx.core.os.BuildCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.camera.CameraController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.GroupStickersActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.StickersActivity;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;

public class ChatActivityEnterView extends FrameLayout implements NotificationCenter.NotificationCenterDelegate, SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate, StickersAlert.StickersAlertDelegate {
   private boolean allowGifs;
   private boolean allowShowTopView;
   private boolean allowStickers;
   private ImageView attachButton;
   private LinearLayout attachLayout;
   private ImageView audioSendButton;
   private TLRPC.TL_document audioToSend;
   private MessageObject audioToSendMessageObject;
   private String audioToSendPath;
   private AnimatorSet audioVideoButtonAnimation;
   private FrameLayout audioVideoButtonContainer;
   private ImageView botButton;
   private MessageObject botButtonsMessageObject;
   private int botCount;
   private PopupWindow botKeyboardPopup;
   private BotKeyboardView botKeyboardView;
   private MessageObject botMessageObject;
   private TLRPC.TL_replyKeyboardMarkup botReplyMarkup;
   private boolean calledRecordRunnable;
   private Drawable cameraDrawable;
   private boolean canWriteToChannel;
   private ImageView cancelBotButton;
   private boolean closeAnimationInProgress;
   private int currentAccount;
   private int currentEmojiIcon;
   private int currentPopupContentType;
   private Animator currentResizeAnimation;
   private AnimatorSet currentTopViewAnimation;
   private ChatActivityEnterView.ChatActivityEnterViewDelegate delegate;
   private boolean destroyed;
   private long dialog_id;
   private float distCanMove;
   private AnimatorSet doneButtonAnimation;
   private FrameLayout doneButtonContainer;
   private ImageView doneButtonImage;
   private ContextProgressView doneButtonProgress;
   private Paint dotPaint;
   private boolean editingCaption;
   private MessageObject editingMessageObject;
   private int editingMessageReqId;
   private ImageView[] emojiButton;
   private AnimatorSet emojiButtonAnimation;
   private int emojiPadding;
   private boolean emojiTabOpen;
   private EmojiView emojiView;
   private boolean emojiViewVisible;
   private ImageView expandStickersButton;
   private boolean forceShowSendButton;
   private boolean gifsTabOpen;
   private boolean hasBotCommands;
   private boolean hasRecordVideo;
   private boolean ignoreTextChange;
   private TLRPC.ChatFull info;
   private int innerTextChange;
   private boolean isPaused;
   private int keyboardHeight;
   private int keyboardHeightLand;
   private boolean keyboardVisible;
   private int lastSizeChangeValue1;
   private boolean lastSizeChangeValue2;
   private String lastTimeString;
   private long lastTypingSendTime;
   private long lastTypingTimeSend;
   private Drawable lockArrowDrawable;
   private Drawable lockBackgroundDrawable;
   private Drawable lockDrawable;
   private Drawable lockShadowDrawable;
   private Drawable lockTopDrawable;
   private AccessibilityDelegate mediaMessageButtonsDelegate;
   private EditTextCaption messageEditText;
   private TLRPC.WebPage messageWebPage;
   private boolean messageWebPageSearch;
   private Drawable micDrawable;
   private boolean needShowTopView;
   private ImageView notifyButton;
   private Runnable onFinishInitCameraRunnable;
   private Runnable openKeyboardRunnable;
   private int originalViewHeight;
   private Paint paint;
   private Paint paintRecord;
   private Activity parentActivity;
   private ChatActivity parentFragment;
   private Drawable pauseDrawable;
   private TLRPC.KeyboardButton pendingLocationButton;
   private MessageObject pendingMessageObject;
   private Drawable playDrawable;
   private CloseProgressDrawable2 progressDrawable;
   private Runnable recordAudioVideoRunnable;
   private boolean recordAudioVideoRunnableStarted;
   private ImageView recordCancelImage;
   private TextView recordCancelText;
   private ChatActivityEnterView.RecordCircle recordCircle;
   private Property recordCircleScale;
   private ImageView recordDeleteImageView;
   private ChatActivityEnterView.RecordDot recordDot;
   private int recordInterfaceState;
   private FrameLayout recordPanel;
   private TextView recordSendText;
   private LinearLayout recordTimeContainer;
   private TextView recordTimeText;
   private View recordedAudioBackground;
   private FrameLayout recordedAudioPanel;
   private ImageView recordedAudioPlayButton;
   private ChatActivityEnterView.SeekBarWaveformView recordedAudioSeekBar;
   private TextView recordedAudioTimeTextView;
   private boolean recordingAudioVideo;
   private RectF rect;
   private Paint redDotPaint;
   private MessageObject replyingMessageObject;
   private Property roundedTranslationYProperty;
   private AnimatorSet runningAnimation;
   private AnimatorSet runningAnimation2;
   private AnimatorSet runningAnimationAudio;
   private int runningAnimationType;
   private int searchingType;
   private SeekBarWaveform seekBarWaveform;
   private ImageView sendButton;
   private FrameLayout sendButtonContainer;
   private boolean sendByEnter;
   private Drawable sendDrawable;
   private boolean showKeyboardOnResume;
   private boolean silent;
   private SizeNotifierFrameLayout sizeNotifierLayout;
   private LinearLayout slideText;
   private float startedDraggingX;
   private AnimatedArrowDrawable stickersArrow;
   private boolean stickersDragging;
   private boolean stickersExpanded;
   private int stickersExpandedHeight;
   private Animator stickersExpansionAnim;
   private float stickersExpansionProgress;
   private boolean stickersTabOpen;
   private LinearLayout textFieldContainer;
   private View topLineView;
   private View topView;
   private boolean topViewShowed;
   private Runnable updateExpandabilityRunnable;
   private ImageView videoSendButton;
   private VideoTimelineView videoTimelineView;
   private VideoEditedInfo videoToSendMessageObject;
   private boolean waitingForKeyboardOpen;
   private WakeLock wakeLock;

   @SuppressLint({"ClickableViewAccessibility"})
   public ChatActivityEnterView(Activity var1, SizeNotifierFrameLayout var2, ChatActivity var3, boolean var4) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.mediaMessageButtonsDelegate = new AccessibilityDelegate() {
         public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfo var2) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
            var2.setClassName("android.widget.ImageButton");
            var2.setClickable(true);
            var2.setLongClickable(true);
         }
      };
      this.emojiButton = new ImageView[2];
      this.currentPopupContentType = -1;
      this.currentEmojiIcon = -1;
      this.isPaused = true;
      this.startedDraggingX = -1.0F;
      this.distCanMove = (float)AndroidUtilities.dp(80.0F);
      this.messageWebPageSearch = true;
      this.openKeyboardRunnable = new Runnable() {
         public void run() {
            if (!ChatActivityEnterView.this.destroyed && ChatActivityEnterView.this.messageEditText != null && ChatActivityEnterView.this.waitingForKeyboardOpen && !ChatActivityEnterView.this.keyboardVisible && !AndroidUtilities.usingHardwareInput && !AndroidUtilities.isInMultiwindow) {
               ChatActivityEnterView.this.messageEditText.requestFocus();
               AndroidUtilities.showKeyboard(ChatActivityEnterView.this.messageEditText);
               AndroidUtilities.cancelRunOnUIThread(ChatActivityEnterView.this.openKeyboardRunnable);
               AndroidUtilities.runOnUIThread(ChatActivityEnterView.this.openKeyboardRunnable, 100L);
            }

         }
      };
      this.updateExpandabilityRunnable = new Runnable() {
         private int lastKnownPage = -1;

         public void run() {
            if (ChatActivityEnterView.this.emojiView != null) {
               int var1 = ChatActivityEnterView.this.emojiView.getCurrentPage();
               if (var1 != this.lastKnownPage) {
                  this.lastKnownPage = var1;
                  boolean var2 = ChatActivityEnterView.this.stickersTabOpen;
                  ChatActivityEnterView var3 = ChatActivityEnterView.this;
                  byte var4 = 2;
                  boolean var5;
                  if (var1 != 1 && var1 != 2) {
                     var5 = false;
                  } else {
                     var5 = true;
                  }

                  var3.stickersTabOpen = var5;
                  boolean var6 = ChatActivityEnterView.this.emojiTabOpen;
                  var3 = ChatActivityEnterView.this;
                  if (var1 == 0) {
                     var5 = true;
                  } else {
                     var5 = false;
                  }

                  var3.emojiTabOpen = var5;
                  if (ChatActivityEnterView.this.stickersExpanded) {
                     if (!ChatActivityEnterView.this.stickersTabOpen && ChatActivityEnterView.this.searchingType == 0) {
                        if (ChatActivityEnterView.this.searchingType != 0) {
                           ChatActivityEnterView.this.searchingType = 0;
                           ChatActivityEnterView.this.emojiView.closeSearch(true);
                           ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                        }

                        ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                     } else if (ChatActivityEnterView.this.searchingType != 0) {
                        var3 = ChatActivityEnterView.this;
                        if (var1 != 0) {
                           var4 = 1;
                        }

                        var3.searchingType = var4;
                        ChatActivityEnterView.this.checkStickresExpandHeight();
                     }
                  }

                  if (var2 != ChatActivityEnterView.this.stickersTabOpen || var6 != ChatActivityEnterView.this.emojiTabOpen) {
                     ChatActivityEnterView.this.checkSendButton(true);
                  }
               }
            }

         }
      };
      this.roundedTranslationYProperty = new Property(Integer.class, "translationY") {
         public Integer get(View var1) {
            return Math.round(var1.getTranslationY());
         }

         public void set(View var1, Integer var2) {
            var1.setTranslationY((float)var2);
         }
      };
      this.recordCircleScale = new Property(Float.class, "scale") {
         public Float get(ChatActivityEnterView.RecordCircle var1) {
            return var1.getScale();
         }

         public void set(ChatActivityEnterView.RecordCircle var1, Float var2) {
            var1.setScale(var2);
         }
      };
      this.redDotPaint = new Paint(1);
      this.onFinishInitCameraRunnable = new Runnable() {
         public void run() {
            if (ChatActivityEnterView.this.delegate != null) {
               ChatActivityEnterView.this.delegate.needStartRecordVideo(0);
            }

         }
      };
      this.recordAudioVideoRunnable = new Runnable() {
         public void run() {
            if (ChatActivityEnterView.this.delegate != null && ChatActivityEnterView.this.parentActivity != null) {
               ChatActivityEnterView.this.delegate.onPreAudioVideoRecord();
               ChatActivityEnterView.this.calledRecordRunnable = true;
               ChatActivityEnterView.this.recordAudioVideoRunnableStarted = false;
               ChatActivityEnterView.this.recordCircle.setLockTranslation(10000.0F);
               ChatActivityEnterView.this.recordSendText.setAlpha(0.0F);
               ChatActivityEnterView.this.slideText.setAlpha(1.0F);
               ChatActivityEnterView.this.slideText.setTranslationY(0.0F);
               if (ChatActivityEnterView.this.videoSendButton != null && ChatActivityEnterView.this.videoSendButton.getTag() != null) {
                  if (VERSION.SDK_INT >= 23) {
                     boolean var1;
                     if (ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                        var1 = true;
                     } else {
                        var1 = false;
                     }

                     boolean var2;
                     if (ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.CAMERA") == 0) {
                        var2 = true;
                     } else {
                        var2 = false;
                     }

                     if (!var1 || !var2) {
                        byte var3;
                        if (!var1 && !var2) {
                           var3 = 2;
                        } else {
                           var3 = 1;
                        }

                        String[] var4 = new String[var3];
                        if (!var1 && !var2) {
                           var4[0] = "android.permission.RECORD_AUDIO";
                           var4[1] = "android.permission.CAMERA";
                        } else if (!var1) {
                           var4[0] = "android.permission.RECORD_AUDIO";
                        } else {
                           var4[0] = "android.permission.CAMERA";
                        }

                        ChatActivityEnterView.this.parentActivity.requestPermissions(var4, 3);
                        return;
                     }
                  }

                  if (!CameraController.getInstance().isCameraInitied()) {
                     CameraController.getInstance().initCamera(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                  } else {
                     ChatActivityEnterView.this.onFinishInitCameraRunnable.run();
                  }
               } else {
                  if (ChatActivityEnterView.this.parentFragment != null && VERSION.SDK_INT >= 23 && ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
                     ChatActivityEnterView.this.parentActivity.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 3);
                     return;
                  }

                  ChatActivityEnterView.this.delegate.needStartRecordAudio(1);
                  ChatActivityEnterView.this.startedDraggingX = -1.0F;
                  MediaController.getInstance().startRecording(ChatActivityEnterView.this.currentAccount, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject);
                  ChatActivityEnterView.this.updateRecordIntefrace();
                  ChatActivityEnterView.this.audioVideoButtonContainer.getParent().requestDisallowInterceptTouchEvent(true);
               }
            }

         }
      };
      this.paint = new Paint(1);
      this.paintRecord = new Paint(1);
      this.rect = new RectF();
      this.dotPaint = new Paint(1);
      this.dotPaint.setColor(Theme.getColor("chat_emojiPanelNewTrending"));
      this.setFocusable(true);
      this.setFocusableInTouchMode(true);
      this.setWillNotDraw(false);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStarted);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStartError);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStopped);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordProgressChanged);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.audioDidSent);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.audioRouteChanged);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
      this.parentActivity = var1;
      this.parentFragment = var3;
      this.sizeNotifierLayout = var2;
      this.sizeNotifierLayout.setDelegate(this);
      this.sendByEnter = MessagesController.getGlobalMainSettings().getBoolean("send_by_enter", false);
      this.textFieldContainer = new LinearLayout(var1);
      this.textFieldContainer.setOrientation(0);
      this.addView(this.textFieldContainer, LayoutHelper.createFrame(-1, -2.0F, 83, 0.0F, 2.0F, 0.0F, 0.0F));
      FrameLayout var12 = new FrameLayout(var1);
      this.textFieldContainer.addView(var12, LayoutHelper.createLinear(0, -2, 1.0F, 80));

      int var5;
      for(var5 = 0; var5 < 2; ++var5) {
         this.emojiButton[var5] = new ImageView(var1) {
            protected void onDraw(Canvas var1) {
               super.onDraw(var1);
               if (this.getTag() != null && ChatActivityEnterView.this.attachLayout != null && !ChatActivityEnterView.this.emojiViewVisible && !DataQuery.getInstance(ChatActivityEnterView.this.currentAccount).getUnreadStickerSets().isEmpty() && ChatActivityEnterView.this.dotPaint != null) {
                  int var2 = this.getWidth() / 2;
                  int var3 = AndroidUtilities.dp(9.0F);
                  int var4 = this.getHeight() / 2;
                  int var5 = AndroidUtilities.dp(8.0F);
                  var1.drawCircle((float)(var2 + var3), (float)(var4 - var5), (float)AndroidUtilities.dp(5.0F), ChatActivityEnterView.this.dotPaint);
               }

            }
         };
         this.emojiButton[var5].setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), Mode.MULTIPLY));
         this.emojiButton[var5].setScaleType(ScaleType.CENTER_INSIDE);
         var12.addView(this.emojiButton[var5], LayoutHelper.createFrame(48, 48.0F, 83, 3.0F, 0.0F, 0.0F, 0.0F));
         this.emojiButton[var5].setOnClickListener(new _$$Lambda$ChatActivityEnterView$jlpojNCIsF_c7hlFcVLHdouZye4(this));
         this.emojiButton[var5].setContentDescription(LocaleController.getString("AccDescrEmojiButton", 2131558431));
         if (var5 == 1) {
            this.emojiButton[var5].setVisibility(4);
            this.emojiButton[var5].setAlpha(0.0F);
            this.emojiButton[var5].setScaleX(0.1F);
            this.emojiButton[var5].setScaleY(0.1F);
         }
      }

      this.setEmojiButtonImage(false, false);
      this.messageEditText = new EditTextCaption(var1) {
         // $FF: synthetic method
         public boolean lambda$onCreateInputConnection$0$ChatActivityEnterView$9(InputContentInfoCompat var1, int var2, Bundle var3) {
            if (BuildCompat.isAtLeastNMR1() && (var2 & 1) != 0) {
               try {
                  var1.requestPermission();
               } catch (Exception var4) {
                  return false;
               }
            }

            if (var1.getDescription().hasMimeType("image/gif")) {
               SendMessagesHelper.prepareSendingDocument((String)null, (String)null, var1.getContentUri(), (String)null, "image/gif", ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, var1, (MessageObject)null);
            } else {
               SendMessagesHelper.prepareSendingPhoto((String)null, var1.getContentUri(), ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, (CharSequence)null, (ArrayList)null, (ArrayList)null, var1, 0, (MessageObject)null);
            }

            if (ChatActivityEnterView.this.delegate != null) {
               ChatActivityEnterView.this.delegate.onMessageSend((CharSequence)null);
            }

            return true;
         }

         public InputConnection onCreateInputConnection(EditorInfo var1) {
            InputConnection var2 = super.onCreateInputConnection(var1);
            EditorInfoCompat.setContentMimeTypes(var1, new String[]{"image/gif", "image/*", "image/jpg", "image/png"});
            return InputConnectionCompat.createWrapper(var2, var1, new _$$Lambda$ChatActivityEnterView$9$ETBc4NSNPoua6FyaOMaLXYYn2ak(this));
         }

         protected void onSelectionChanged(int var1, int var2) {
            super.onSelectionChanged(var1, var2);
            if (ChatActivityEnterView.this.delegate != null) {
               ChatActivityEnterView.this.delegate.onTextSelectionChanged(var1, var2);
            }

         }

         public boolean onTouchEvent(MotionEvent var1) {
            if (ChatActivityEnterView.this.isPopupShowing() && var1.getAction() == 0) {
               if (ChatActivityEnterView.this.searchingType != 0) {
                  ChatActivityEnterView.this.searchingType = 0;
                  ChatActivityEnterView.this.emojiView.closeSearch(false);
               }

               ChatActivityEnterView var2 = ChatActivityEnterView.this;
               byte var3;
               if (AndroidUtilities.usingHardwareInput) {
                  var3 = 0;
               } else {
                  var3 = 2;
               }

               var2.showPopup(var3, 0);
               ChatActivityEnterView.this.openKeyboardInternal();
            }

            try {
               boolean var4 = super.onTouchEvent(var1);
               return var4;
            } catch (Exception var5) {
               FileLog.e((Throwable)var5);
               return false;
            }
         }
      };
      this.messageEditText.setDelegate(new _$$Lambda$ChatActivityEnterView$YXzMuZPPiDr1K7R7cpN8afDWrUQ(this));
      this.updateFieldHint();
      int var6 = 268435456;
      ChatActivity var10 = this.parentFragment;
      var5 = var6;
      if (var10 != null) {
         var5 = var6;
         if (var10.getCurrentEncryptedChat() != null) {
            var5 = 285212672;
         }
      }

      this.messageEditText.setImeOptions(var5);
      EditTextCaption var11 = this.messageEditText;
      var11.setInputType(var11.getInputType() | 16384 | 131072);
      this.messageEditText.setSingleLine(false);
      this.messageEditText.setMaxLines(6);
      this.messageEditText.setTextSize(1, 18.0F);
      this.messageEditText.setGravity(80);
      this.messageEditText.setPadding(0, AndroidUtilities.dp(11.0F), 0, AndroidUtilities.dp(12.0F));
      this.messageEditText.setBackgroundDrawable((Drawable)null);
      this.messageEditText.setTextColor(Theme.getColor("chat_messagePanelText"));
      this.messageEditText.setHintColor(Theme.getColor("chat_messagePanelHint"));
      this.messageEditText.setHintTextColor(Theme.getColor("chat_messagePanelHint"));
      var11 = this.messageEditText;
      float var7;
      if (var4) {
         var7 = 50.0F;
      } else {
         var7 = 2.0F;
      }

      var12.addView(var11, LayoutHelper.createFrame(-1, -2.0F, 80, 52.0F, 0.0F, var7, 0.0F));
      this.messageEditText.setOnKeyListener(new OnKeyListener() {
         boolean ctrlPressed = false;

         public boolean onKey(View var1, int var2, KeyEvent var3) {
            boolean var4 = false;
            if (var2 == 4 && !ChatActivityEnterView.this.keyboardVisible && ChatActivityEnterView.this.isPopupShowing()) {
               if (var3.getAction() == 1) {
                  if (ChatActivityEnterView.this.currentPopupContentType == 1 && ChatActivityEnterView.this.botButtonsMessageObject != null) {
                     Editor var5 = MessagesController.getMainSettings(ChatActivityEnterView.this.currentAccount).edit();
                     StringBuilder var6 = new StringBuilder();
                     var6.append("hidekeyboard_");
                     var6.append(ChatActivityEnterView.this.dialog_id);
                     var5.putInt(var6.toString(), ChatActivityEnterView.this.botButtonsMessageObject.getId()).commit();
                  }

                  if (ChatActivityEnterView.this.searchingType != 0) {
                     ChatActivityEnterView.this.searchingType = 0;
                     ChatActivityEnterView.this.emojiView.closeSearch(true);
                     ChatActivityEnterView.this.messageEditText.requestFocus();
                  } else {
                     ChatActivityEnterView.this.showPopup(0, 0);
                  }
               }

               return true;
            } else if (var2 == 66 && (this.ctrlPressed || ChatActivityEnterView.this.sendByEnter) && var3.getAction() == 0 && ChatActivityEnterView.this.editingMessageObject == null) {
               ChatActivityEnterView.this.sendMessage();
               return true;
            } else if (var2 != 113 && var2 != 114) {
               return false;
            } else {
               if (var3.getAction() == 0) {
                  var4 = true;
               }

               this.ctrlPressed = var4;
               return true;
            }
         }
      });
      this.messageEditText.setOnEditorActionListener(new OnEditorActionListener() {
         boolean ctrlPressed = false;

         public boolean onEditorAction(TextView var1, int var2, KeyEvent var3) {
            if (var2 == 4) {
               ChatActivityEnterView.this.sendMessage();
               return true;
            } else {
               boolean var4 = false;
               if (var3 != null && var2 == 0) {
                  if ((this.ctrlPressed || ChatActivityEnterView.this.sendByEnter) && var3.getAction() == 0 && ChatActivityEnterView.this.editingMessageObject == null) {
                     ChatActivityEnterView.this.sendMessage();
                     return true;
                  }

                  if (var2 == 113 || var2 == 114) {
                     if (var3.getAction() == 0) {
                        var4 = true;
                     }

                     this.ctrlPressed = var4;
                     return true;
                  }
               }

               return false;
            }
         }
      });
      this.messageEditText.addTextChangedListener(new TextWatcher() {
         boolean processChange = false;

         public void afterTextChanged(Editable var1) {
            if (ChatActivityEnterView.this.innerTextChange == 0) {
               if (ChatActivityEnterView.this.sendByEnter && var1.length() > 0 && var1.charAt(var1.length() - 1) == '\n' && ChatActivityEnterView.this.editingMessageObject == null) {
                  ChatActivityEnterView.this.sendMessage();
               }

               if (this.processChange) {
                  ImageSpan[] var2 = (ImageSpan[])var1.getSpans(0, var1.length(), ImageSpan.class);

                  for(int var3 = 0; var3 < var2.length; ++var3) {
                     var1.removeSpan(var2[var3]);
                  }

                  Emoji.replaceEmoji(var1, ChatActivityEnterView.this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
                  this.processChange = false;
               }

            }
         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            if (ChatActivityEnterView.this.innerTextChange != 1) {
               ChatActivityEnterView.this.checkSendButton(true);
               CharSequence var5 = AndroidUtilities.getTrimmedString(var1.toString());
               if (ChatActivityEnterView.this.delegate != null && !ChatActivityEnterView.this.ignoreTextChange) {
                  if (var4 > 2 || var1 == null || var1.length() == 0) {
                     ChatActivityEnterView.this.messageWebPageSearch = true;
                  }

                  ChatActivityEnterView.ChatActivityEnterViewDelegate var6 = ChatActivityEnterView.this.delegate;
                  boolean var7;
                  if (var3 <= var4 + 1 && var4 - var3 <= 2) {
                     var7 = false;
                  } else {
                     var7 = true;
                  }

                  var6.onTextChanged(var1, var7);
               }

               if (ChatActivityEnterView.this.innerTextChange != 2 && var3 != var4 && var4 - var3 > 1) {
                  this.processChange = true;
               }

               if (ChatActivityEnterView.this.editingMessageObject == null && !ChatActivityEnterView.this.canWriteToChannel && var5.length() != 0 && ChatActivityEnterView.this.lastTypingTimeSend < System.currentTimeMillis() - 5000L && !ChatActivityEnterView.this.ignoreTextChange) {
                  var2 = ConnectionsManager.getInstance(ChatActivityEnterView.this.currentAccount).getCurrentTime();
                  TLRPC.User var8 = null;
                  if ((int)ChatActivityEnterView.this.dialog_id > 0) {
                     var8 = MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).getUser((int)ChatActivityEnterView.this.dialog_id);
                  }

                  if (var8 != null) {
                     label97: {
                        if (var8.id != UserConfig.getInstance(ChatActivityEnterView.this.currentAccount).getClientUserId()) {
                           TLRPC.UserStatus var9 = var8.status;
                           if (var9 == null || var9.expires >= var2 || MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).onlinePrivacy.containsKey(var8.id)) {
                              break label97;
                           }
                        }

                        return;
                     }
                  }

                  ChatActivityEnterView.this.lastTypingTimeSend = System.currentTimeMillis();
                  if (ChatActivityEnterView.this.delegate != null) {
                     ChatActivityEnterView.this.delegate.needSendTyping();
                  }
               }

            }
         }
      });
      ImageView var13;
      byte var22;
      if (var4) {
         this.attachLayout = new LinearLayout(var1);
         this.attachLayout.setOrientation(0);
         this.attachLayout.setEnabled(false);
         this.attachLayout.setPivotX((float)AndroidUtilities.dp(48.0F));
         var12.addView(this.attachLayout, LayoutHelper.createFrame(-2, 48, 85));
         this.botButton = new ImageView(var1);
         this.botButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), Mode.MULTIPLY));
         this.botButton.setImageResource(2131165482);
         this.botButton.setScaleType(ScaleType.CENTER);
         this.botButton.setVisibility(8);
         this.attachLayout.addView(this.botButton, LayoutHelper.createLinear(48, 48));
         this.botButton.setOnClickListener(new _$$Lambda$ChatActivityEnterView$ePEXGP4V8TnztLq_2EEcKkpts_k(this));
         this.notifyButton = new ImageView(var1);
         var13 = this.notifyButton;
         if (this.silent) {
            var5 = 2131165489;
         } else {
            var5 = 2131165490;
         }

         var13.setImageResource(var5);
         ImageView var8 = this.notifyButton;
         String var14;
         if (this.silent) {
            var5 = 2131558424;
            var14 = "AccDescrChanSilentOn";
         } else {
            var5 = 2131558423;
            var14 = "AccDescrChanSilentOff";
         }

         var8.setContentDescription(LocaleController.getString(var14, var5));
         this.notifyButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), Mode.MULTIPLY));
         this.notifyButton.setScaleType(ScaleType.CENTER);
         var13 = this.notifyButton;
         if (this.canWriteToChannel) {
            var22 = 0;
         } else {
            var22 = 8;
         }

         var13.setVisibility(var22);
         this.attachLayout.addView(this.notifyButton, LayoutHelper.createLinear(48, 48));
         this.notifyButton.setOnClickListener(new OnClickListener() {
            private Toast visibleToast;

            public void onClick(View var1) {
               ChatActivityEnterView var5 = ChatActivityEnterView.this;
               var5.silent = var5.silent ^ true;
               ImageView var6 = ChatActivityEnterView.this.notifyButton;
               int var2;
               if (ChatActivityEnterView.this.silent) {
                  var2 = 2131165489;
               } else {
                  var2 = 2131165490;
               }

               var6.setImageResource(var2);
               Editor var3 = MessagesController.getNotificationsSettings(ChatActivityEnterView.this.currentAccount).edit();
               StringBuilder var7 = new StringBuilder();
               var7.append("silent_");
               var7.append(ChatActivityEnterView.this.dialog_id);
               var3.putBoolean(var7.toString(), ChatActivityEnterView.this.silent).commit();
               NotificationsController.getInstance(ChatActivityEnterView.this.currentAccount).updateServerNotificationsSettings(ChatActivityEnterView.this.dialog_id);

               try {
                  if (this.visibleToast != null) {
                     this.visibleToast.cancel();
                  }
               } catch (Exception var4) {
                  FileLog.e((Throwable)var4);
               }

               if (ChatActivityEnterView.this.silent) {
                  this.visibleToast = Toast.makeText(ChatActivityEnterView.this.parentActivity, LocaleController.getString("ChannelNotifyMembersInfoOff", 2131558981), 0);
                  this.visibleToast.show();
               } else {
                  this.visibleToast = Toast.makeText(ChatActivityEnterView.this.parentActivity, LocaleController.getString("ChannelNotifyMembersInfoOn", 2131558982), 0);
                  this.visibleToast.show();
               }

               ImageView var9 = ChatActivityEnterView.this.notifyButton;
               String var8;
               if (ChatActivityEnterView.this.silent) {
                  var2 = 2131558424;
                  var8 = "AccDescrChanSilentOn";
               } else {
                  var2 = 2131558423;
                  var8 = "AccDescrChanSilentOff";
               }

               var9.setContentDescription(LocaleController.getString(var8, var2));
               ChatActivityEnterView.this.updateFieldHint();
            }
         });
         this.attachButton = new ImageView(var1);
         this.attachButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), Mode.MULTIPLY));
         this.attachButton.setImageResource(2131165480);
         this.attachButton.setScaleType(ScaleType.CENTER);
         this.attachLayout.addView(this.attachButton, LayoutHelper.createLinear(48, 48));
         this.attachButton.setOnClickListener(new _$$Lambda$ChatActivityEnterView$sA9S70ZL3Nng90HptN_7RpznwZo(this));
         this.attachButton.setContentDescription(LocaleController.getString("AccDescrAttachButton", 2131558413));
      }

      this.recordedAudioPanel = new FrameLayout(var1);
      FrameLayout var18 = this.recordedAudioPanel;
      if (this.audioToSend == null) {
         var22 = 8;
      } else {
         var22 = 0;
      }

      var18.setVisibility(var22);
      this.recordedAudioPanel.setBackgroundColor(Theme.getColor("chat_messagePanelBackground"));
      this.recordedAudioPanel.setFocusable(true);
      this.recordedAudioPanel.setFocusableInTouchMode(true);
      this.recordedAudioPanel.setClickable(true);
      var12.addView(this.recordedAudioPanel, LayoutHelper.createFrame(-1, 48, 80));
      this.recordDeleteImageView = new ImageView(var1);
      this.recordDeleteImageView.setScaleType(ScaleType.CENTER);
      this.recordDeleteImageView.setImageResource(2131165623);
      this.recordDeleteImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelVoiceDelete"), Mode.MULTIPLY));
      this.recordDeleteImageView.setContentDescription(LocaleController.getString("Delete", 2131559227));
      this.recordedAudioPanel.addView(this.recordDeleteImageView, LayoutHelper.createFrame(48, 48.0F));
      this.recordDeleteImageView.setOnClickListener(new _$$Lambda$ChatActivityEnterView$pQGz59T4TelJWdtUhc7Wjoj7V9g(this));
      this.videoTimelineView = new VideoTimelineView(var1);
      this.videoTimelineView.setColor(-11817481);
      this.videoTimelineView.setRoundFrames(true);
      this.videoTimelineView.setDelegate(new VideoTimelineView.VideoTimelineViewDelegate() {
         public void didStartDragging() {
            ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(1, 0.0F);
         }

         public void didStopDragging() {
            ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(0, 0.0F);
         }

         public void onLeftProgressChanged(float var1) {
            if (ChatActivityEnterView.this.videoToSendMessageObject != null) {
               ChatActivityEnterView.this.videoToSendMessageObject.startTime = (long)((float)ChatActivityEnterView.this.videoToSendMessageObject.estimatedDuration * var1);
               ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(2, var1);
            }
         }

         public void onRightProgressChanged(float var1) {
            if (ChatActivityEnterView.this.videoToSendMessageObject != null) {
               ChatActivityEnterView.this.videoToSendMessageObject.endTime = (long)((float)ChatActivityEnterView.this.videoToSendMessageObject.estimatedDuration * var1);
               ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(2, var1);
            }
         }
      });
      this.recordedAudioPanel.addView(this.videoTimelineView, LayoutHelper.createFrame(-1, 32.0F, 19, 40.0F, 0.0F, 0.0F, 0.0F));
      this.recordedAudioBackground = new View(var1);
      this.recordedAudioBackground.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0F), Theme.getColor("chat_recordedVoiceBackground")));
      this.recordedAudioPanel.addView(this.recordedAudioBackground, LayoutHelper.createFrame(-1, 36.0F, 19, 48.0F, 0.0F, 0.0F, 0.0F));
      this.recordedAudioSeekBar = new ChatActivityEnterView.SeekBarWaveformView(var1);
      this.recordedAudioPanel.addView(this.recordedAudioSeekBar, LayoutHelper.createFrame(-1, 32.0F, 19, 92.0F, 0.0F, 52.0F, 0.0F));
      this.playDrawable = Theme.createSimpleSelectorDrawable(var1, 2131165806, Theme.getColor("chat_recordedVoicePlayPause"), Theme.getColor("chat_recordedVoicePlayPausePressed"));
      this.pauseDrawable = Theme.createSimpleSelectorDrawable(var1, 2131165805, Theme.getColor("chat_recordedVoicePlayPause"), Theme.getColor("chat_recordedVoicePlayPausePressed"));
      this.recordedAudioPlayButton = new ImageView(var1);
      this.recordedAudioPlayButton.setImageDrawable(this.playDrawable);
      this.recordedAudioPlayButton.setScaleType(ScaleType.CENTER);
      this.recordedAudioPlayButton.setContentDescription(LocaleController.getString("AccActionPlay", 2131558409));
      this.recordedAudioPanel.addView(this.recordedAudioPlayButton, LayoutHelper.createFrame(48, 48.0F, 83, 48.0F, 0.0F, 0.0F, 0.0F));
      this.recordedAudioPlayButton.setOnClickListener(new _$$Lambda$ChatActivityEnterView$0PqxYA4Sm4DXenEKSdETEwDwen0(this));
      this.recordedAudioTimeTextView = new TextView(var1);
      this.recordedAudioTimeTextView.setTextColor(Theme.getColor("chat_messagePanelVoiceDuration"));
      this.recordedAudioTimeTextView.setTextSize(1, 13.0F);
      this.recordedAudioPanel.addView(this.recordedAudioTimeTextView, LayoutHelper.createFrame(-2, -2.0F, 21, 0.0F, 0.0F, 13.0F, 0.0F));
      this.recordPanel = new FrameLayout(var1);
      this.recordPanel.setVisibility(8);
      this.recordPanel.setBackgroundColor(Theme.getColor("chat_messagePanelBackground"));
      var12.addView(this.recordPanel, LayoutHelper.createFrame(-1, 48, 80));
      this.recordPanel.setOnTouchListener(_$$Lambda$ChatActivityEnterView$NTG78ehgN82Ciqu_59BpKprJO6c.INSTANCE);
      this.slideText = new LinearLayout(var1);
      this.slideText.setOrientation(0);
      this.recordPanel.addView(this.slideText, LayoutHelper.createFrame(-2, -2.0F, 17, 30.0F, 0.0F, 0.0F, 0.0F));
      this.recordCancelImage = new ImageView(var1);
      this.recordCancelImage.setImageResource(2131165829);
      this.recordCancelImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_recordVoiceCancel"), Mode.MULTIPLY));
      this.slideText.addView(this.recordCancelImage, LayoutHelper.createLinear(-2, -2, 16, 0, 1, 0, 0));
      this.recordCancelText = new TextView(var1);
      this.recordCancelText.setText(LocaleController.getString("SlideToCancel", 2131560787));
      this.recordCancelText.setTextColor(Theme.getColor("chat_recordVoiceCancel"));
      this.recordCancelText.setTextSize(1, 12.0F);
      this.slideText.addView(this.recordCancelText, LayoutHelper.createLinear(-2, -2, 16, 6, 0, 0, 0));
      this.recordSendText = new TextView(var1);
      this.recordSendText.setText(LocaleController.getString("Cancel", 2131558891).toUpperCase());
      this.recordSendText.setTextColor(Theme.getColor("chat_fieldOverlayText"));
      this.recordSendText.setTextSize(1, 16.0F);
      this.recordSendText.setGravity(17);
      this.recordSendText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.recordSendText.setAlpha(0.0F);
      this.recordSendText.setPadding(AndroidUtilities.dp(36.0F), 0, 0, 0);
      this.recordSendText.setOnClickListener(new _$$Lambda$ChatActivityEnterView$yC9IrZCKz9RgoNa9k7GyjXp1EoM(this));
      this.recordPanel.addView(this.recordSendText, LayoutHelper.createFrame(-2, -1.0F, 49, 0.0F, 0.0F, 0.0F, 0.0F));
      this.recordTimeContainer = new LinearLayout(var1);
      this.recordTimeContainer.setOrientation(0);
      this.recordTimeContainer.setPadding(AndroidUtilities.dp(13.0F), 0, 0, 0);
      this.recordTimeContainer.setBackgroundColor(Theme.getColor("chat_messagePanelBackground"));
      this.recordPanel.addView(this.recordTimeContainer, LayoutHelper.createFrame(-2, -2, 16));
      this.recordDot = new ChatActivityEnterView.RecordDot(var1);
      this.recordTimeContainer.addView(this.recordDot, LayoutHelper.createLinear(11, 11, 16, 0, 1, 0, 0));
      this.recordTimeText = new TextView(var1);
      this.recordTimeText.setTextColor(Theme.getColor("chat_recordTime"));
      this.recordTimeText.setTextSize(1, 16.0F);
      this.recordTimeContainer.addView(this.recordTimeText, LayoutHelper.createLinear(-2, -2, 16, 6, 0, 0, 0));
      this.sendButtonContainer = new FrameLayout(var1);
      this.textFieldContainer.addView(this.sendButtonContainer, LayoutHelper.createLinear(48, 48, 80));
      this.audioVideoButtonContainer = new FrameLayout(var1);
      this.audioVideoButtonContainer.setBackgroundColor(Theme.getColor("chat_messagePanelBackground"));
      this.audioVideoButtonContainer.setSoundEffectsEnabled(false);
      this.sendButtonContainer.addView(this.audioVideoButtonContainer, LayoutHelper.createFrame(48, 48.0F));
      this.audioVideoButtonContainer.setOnTouchListener(new _$$Lambda$ChatActivityEnterView$ilTU_PI6Smic3T39rzhm2HGqiUo(this));
      this.audioSendButton = new ImageView(var1);
      this.audioSendButton.setScaleType(ScaleType.CENTER_INSIDE);
      this.audioSendButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), Mode.MULTIPLY));
      this.audioSendButton.setImageResource(2131165488);
      this.audioSendButton.setPadding(0, 0, AndroidUtilities.dp(4.0F), 0);
      this.audioSendButton.setContentDescription(LocaleController.getString("AccDescrVoiceMessage", 2131558483));
      this.audioSendButton.setFocusable(true);
      this.audioSendButton.setAccessibilityDelegate(this.mediaMessageButtonsDelegate);
      this.audioVideoButtonContainer.addView(this.audioSendButton, LayoutHelper.createFrame(48, 48.0F));
      if (var4) {
         this.videoSendButton = new ImageView(var1);
         this.videoSendButton.setScaleType(ScaleType.CENTER_INSIDE);
         this.videoSendButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), Mode.MULTIPLY));
         this.videoSendButton.setImageResource(2131165494);
         this.videoSendButton.setPadding(0, 0, AndroidUtilities.dp(4.0F), 0);
         this.videoSendButton.setContentDescription(LocaleController.getString("AccDescrVideoMessage", 2131558481));
         this.videoSendButton.setFocusable(true);
         this.videoSendButton.setAccessibilityDelegate(this.mediaMessageButtonsDelegate);
         this.audioVideoButtonContainer.addView(this.videoSendButton, LayoutHelper.createFrame(48, 48.0F));
      }

      this.recordCircle = new ChatActivityEnterView.RecordCircle(var1);
      this.recordCircle.setVisibility(8);
      this.sizeNotifierLayout.addView(this.recordCircle, LayoutHelper.createFrame(124, 194.0F, 85, 0.0F, 0.0F, -36.0F, 0.0F));
      this.cancelBotButton = new ImageView(var1);
      this.cancelBotButton.setVisibility(4);
      this.cancelBotButton.setScaleType(ScaleType.CENTER_INSIDE);
      ImageView var15 = this.cancelBotButton;
      CloseProgressDrawable2 var19 = new CloseProgressDrawable2();
      this.progressDrawable = var19;
      var15.setImageDrawable(var19);
      this.cancelBotButton.setContentDescription(LocaleController.getString("Cancel", 2131558891));
      this.progressDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelCancelInlineBot"), Mode.MULTIPLY));
      this.cancelBotButton.setSoundEffectsEnabled(false);
      this.cancelBotButton.setScaleX(0.1F);
      this.cancelBotButton.setScaleY(0.1F);
      this.cancelBotButton.setAlpha(0.0F);
      this.sendButtonContainer.addView(this.cancelBotButton, LayoutHelper.createFrame(48, 48.0F));
      this.cancelBotButton.setOnClickListener(new _$$Lambda$ChatActivityEnterView$5SWSfIIIolSi29LVurkXFLuveaY(this));
      this.sendButton = new ImageView(var1);
      this.sendButton.setVisibility(4);
      this.sendButton.setScaleType(ScaleType.CENTER_INSIDE);
      this.sendButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelSend"), Mode.MULTIPLY));
      this.sendButton.setImageResource(2131165468);
      this.sendButton.setContentDescription(LocaleController.getString("Send", 2131560685));
      this.sendButton.setSoundEffectsEnabled(false);
      this.sendButton.setScaleX(0.1F);
      this.sendButton.setScaleY(0.1F);
      this.sendButton.setAlpha(0.0F);
      this.sendButtonContainer.addView(this.sendButton, LayoutHelper.createFrame(48, 48.0F));
      this.sendButton.setOnClickListener(new _$$Lambda$ChatActivityEnterView$LT79Icaff_VcrkpB6XfMaR17p9I(this));
      this.expandStickersButton = new ImageView(var1);
      this.expandStickersButton.setPadding(0, 0, AndroidUtilities.dp(4.0F), 0);
      this.expandStickersButton.setScaleType(ScaleType.CENTER);
      var13 = this.expandStickersButton;
      AnimatedArrowDrawable var16 = new AnimatedArrowDrawable(Theme.getColor("chat_messagePanelIcons"), false);
      this.stickersArrow = var16;
      var13.setImageDrawable(var16);
      this.expandStickersButton.setVisibility(8);
      this.expandStickersButton.setScaleX(0.1F);
      this.expandStickersButton.setScaleY(0.1F);
      this.expandStickersButton.setAlpha(0.0F);
      this.sendButtonContainer.addView(this.expandStickersButton, LayoutHelper.createFrame(48, 48.0F));
      this.expandStickersButton.setOnClickListener(new _$$Lambda$ChatActivityEnterView$BuL8MDh0vQ9QIU2SBmKOQL4e5dE(this));
      this.expandStickersButton.setContentDescription(LocaleController.getString("AccDescrExpandPanel", 2131558432));
      this.doneButtonContainer = new FrameLayout(var1);
      this.doneButtonContainer.setVisibility(8);
      this.textFieldContainer.addView(this.doneButtonContainer, LayoutHelper.createLinear(48, 48, 80));
      this.doneButtonContainer.setOnClickListener(new _$$Lambda$ChatActivityEnterView$k84iF5Zh9bonq8mHpikPhsUxJO4(this));
      Drawable var20 = Theme.createCircleDrawable(AndroidUtilities.dp(16.0F), Theme.getColor("chat_messagePanelSend"));
      Drawable var17 = var1.getResources().getDrawable(2131165484).mutate();
      var17.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelBackground"), Mode.MULTIPLY));
      CombinedDrawable var21 = new CombinedDrawable(var20, var17, 0, AndroidUtilities.dp(1.0F));
      var21.setCustomSize(AndroidUtilities.dp(32.0F), AndroidUtilities.dp(32.0F));
      this.doneButtonImage = new ImageView(var1);
      this.doneButtonImage.setScaleType(ScaleType.CENTER);
      this.doneButtonImage.setImageDrawable(var21);
      this.doneButtonImage.setContentDescription(LocaleController.getString("Done", 2131559299));
      this.doneButtonContainer.addView(this.doneButtonImage, LayoutHelper.createFrame(48, 48.0F));
      this.doneButtonProgress = new ContextProgressView(var1, 0);
      this.doneButtonProgress.setVisibility(4);
      this.doneButtonContainer.addView(this.doneButtonProgress, LayoutHelper.createFrame(-1, -1.0F));
      SharedPreferences var9 = MessagesController.getGlobalEmojiSettings();
      this.keyboardHeight = var9.getInt("kbd_height", AndroidUtilities.dp(200.0F));
      this.keyboardHeightLand = var9.getInt("kbd_height_land3", AndroidUtilities.dp(200.0F));
      this.setRecordVideoButtonVisible(false, false);
      this.checkSendButton(false);
      this.checkChannelRights();
   }

   private void checkSendButton(boolean var1) {
      if (this.editingMessageObject == null) {
         if (this.isPaused) {
            var1 = false;
         }

         LinearLayout var6;
         if (AndroidUtilities.getTrimmedString(this.messageEditText.getText()).length() <= 0 && !this.forceShowSendButton && this.audioToSend == null && this.videoToSendMessageObject == null) {
            AnimatorSet var7;
            ArrayList var8;
            if (this.emojiView != null && this.emojiViewVisible && (this.stickersTabOpen || this.emojiTabOpen && this.searchingType == 2) && !AndroidUtilities.isInMultiwindow) {
               if (var1) {
                  if (this.runningAnimationType == 4) {
                     return;
                  }

                  var7 = this.runningAnimation;
                  if (var7 != null) {
                     var7.cancel();
                     this.runningAnimation = null;
                  }

                  var7 = this.runningAnimation2;
                  if (var7 != null) {
                     var7.cancel();
                     this.runningAnimation2 = null;
                  }

                  var6 = this.attachLayout;
                  if (var6 != null) {
                     var6.setVisibility(0);
                     this.runningAnimation2 = new AnimatorSet();
                     this.runningAnimation2.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.attachLayout, View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofFloat(this.attachLayout, View.SCALE_X, new float[]{1.0F})});
                     this.runningAnimation2.setDuration(100L);
                     this.runningAnimation2.start();
                     this.updateFieldRight(1);
                     if (this.getVisibility() == 0) {
                        this.delegate.onAttachButtonShow();
                     }
                  }

                  this.expandStickersButton.setVisibility(0);
                  this.runningAnimation = new AnimatorSet();
                  this.runningAnimationType = 4;
                  var8 = new ArrayList();
                  var8.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_X, new float[]{1.0F}));
                  var8.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_Y, new float[]{1.0F}));
                  var8.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.ALPHA, new float[]{1.0F}));
                  if (this.cancelBotButton.getVisibility() == 0) {
                     var8.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, new float[]{0.1F}));
                     var8.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, new float[]{0.1F}));
                     var8.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, new float[]{0.0F}));
                  } else if (this.audioVideoButtonContainer.getVisibility() == 0) {
                     var8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_X, new float[]{0.1F}));
                     var8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_Y, new float[]{0.1F}));
                     var8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, new float[]{0.0F}));
                  } else {
                     var8.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, new float[]{0.1F}));
                     var8.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, new float[]{0.1F}));
                     var8.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, new float[]{0.0F}));
                  }

                  this.runningAnimation.playTogether(var8);
                  this.runningAnimation.setDuration(150L);
                  this.runningAnimation.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationCancel(Animator var1) {
                        if (ChatActivityEnterView.this.runningAnimation != null && ChatActivityEnterView.this.runningAnimation.equals(var1)) {
                           ChatActivityEnterView.this.runningAnimation = null;
                        }

                     }

                     public void onAnimationEnd(Animator var1) {
                        if (ChatActivityEnterView.this.runningAnimation != null && ChatActivityEnterView.this.runningAnimation.equals(var1)) {
                           ChatActivityEnterView.this.sendButton.setVisibility(8);
                           ChatActivityEnterView.this.cancelBotButton.setVisibility(8);
                           ChatActivityEnterView.this.audioVideoButtonContainer.setVisibility(8);
                           ChatActivityEnterView.this.expandStickersButton.setVisibility(0);
                           ChatActivityEnterView.this.runningAnimation = null;
                           ChatActivityEnterView.this.runningAnimationType = 0;
                        }

                     }
                  });
                  this.runningAnimation.start();
               } else {
                  this.sendButton.setScaleX(0.1F);
                  this.sendButton.setScaleY(0.1F);
                  this.sendButton.setAlpha(0.0F);
                  this.cancelBotButton.setScaleX(0.1F);
                  this.cancelBotButton.setScaleY(0.1F);
                  this.cancelBotButton.setAlpha(0.0F);
                  this.audioVideoButtonContainer.setScaleX(0.1F);
                  this.audioVideoButtonContainer.setScaleY(0.1F);
                  this.audioVideoButtonContainer.setAlpha(0.0F);
                  this.expandStickersButton.setScaleX(1.0F);
                  this.expandStickersButton.setScaleY(1.0F);
                  this.expandStickersButton.setAlpha(1.0F);
                  this.cancelBotButton.setVisibility(8);
                  this.sendButton.setVisibility(8);
                  this.audioVideoButtonContainer.setVisibility(8);
                  this.expandStickersButton.setVisibility(0);
                  if (this.attachLayout != null) {
                     if (this.getVisibility() == 0) {
                        this.delegate.onAttachButtonShow();
                     }

                     this.attachLayout.setVisibility(0);
                     this.updateFieldRight(1);
                  }
               }
            } else if (this.sendButton.getVisibility() == 0 || this.cancelBotButton.getVisibility() == 0 || this.expandStickersButton.getVisibility() == 0) {
               if (var1) {
                  if (this.runningAnimationType == 2) {
                     return;
                  }

                  var7 = this.runningAnimation;
                  if (var7 != null) {
                     var7.cancel();
                     this.runningAnimation = null;
                  }

                  var7 = this.runningAnimation2;
                  if (var7 != null) {
                     var7.cancel();
                     this.runningAnimation2 = null;
                  }

                  var6 = this.attachLayout;
                  if (var6 != null) {
                     var6.setVisibility(0);
                     this.runningAnimation2 = new AnimatorSet();
                     this.runningAnimation2.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.attachLayout, View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofFloat(this.attachLayout, View.SCALE_X, new float[]{1.0F})});
                     this.runningAnimation2.setDuration(100L);
                     this.runningAnimation2.start();
                     this.updateFieldRight(1);
                     if (this.getVisibility() == 0) {
                        this.delegate.onAttachButtonShow();
                     }
                  }

                  this.audioVideoButtonContainer.setVisibility(0);
                  this.runningAnimation = new AnimatorSet();
                  this.runningAnimationType = 2;
                  var8 = new ArrayList();
                  var8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_X, new float[]{1.0F}));
                  var8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_Y, new float[]{1.0F}));
                  var8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, new float[]{1.0F}));
                  if (this.cancelBotButton.getVisibility() == 0) {
                     var8.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, new float[]{0.1F}));
                     var8.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, new float[]{0.1F}));
                     var8.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, new float[]{0.0F}));
                  } else if (this.expandStickersButton.getVisibility() == 0) {
                     var8.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_X, new float[]{0.1F}));
                     var8.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_Y, new float[]{0.1F}));
                     var8.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.ALPHA, new float[]{0.0F}));
                  } else {
                     var8.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, new float[]{0.1F}));
                     var8.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, new float[]{0.1F}));
                     var8.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, new float[]{0.0F}));
                  }

                  this.runningAnimation.playTogether(var8);
                  this.runningAnimation.setDuration(150L);
                  this.runningAnimation.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationCancel(Animator var1) {
                        if (ChatActivityEnterView.this.runningAnimation != null && ChatActivityEnterView.this.runningAnimation.equals(var1)) {
                           ChatActivityEnterView.this.runningAnimation = null;
                        }

                     }

                     public void onAnimationEnd(Animator var1) {
                        if (ChatActivityEnterView.this.runningAnimation != null && ChatActivityEnterView.this.runningAnimation.equals(var1)) {
                           ChatActivityEnterView.this.sendButton.setVisibility(8);
                           ChatActivityEnterView.this.cancelBotButton.setVisibility(8);
                           ChatActivityEnterView.this.audioVideoButtonContainer.setVisibility(0);
                           ChatActivityEnterView.this.runningAnimation = null;
                           ChatActivityEnterView.this.runningAnimationType = 0;
                        }

                     }
                  });
                  this.runningAnimation.start();
               } else {
                  this.sendButton.setScaleX(0.1F);
                  this.sendButton.setScaleY(0.1F);
                  this.sendButton.setAlpha(0.0F);
                  this.cancelBotButton.setScaleX(0.1F);
                  this.cancelBotButton.setScaleY(0.1F);
                  this.cancelBotButton.setAlpha(0.0F);
                  this.expandStickersButton.setScaleX(0.1F);
                  this.expandStickersButton.setScaleY(0.1F);
                  this.expandStickersButton.setAlpha(0.0F);
                  this.audioVideoButtonContainer.setScaleX(1.0F);
                  this.audioVideoButtonContainer.setScaleY(1.0F);
                  this.audioVideoButtonContainer.setAlpha(1.0F);
                  this.cancelBotButton.setVisibility(8);
                  this.sendButton.setVisibility(8);
                  this.expandStickersButton.setVisibility(8);
                  this.audioVideoButtonContainer.setVisibility(0);
                  if (this.attachLayout != null) {
                     if (this.getVisibility() == 0) {
                        this.delegate.onAttachButtonShow();
                     }

                     this.attachLayout.setVisibility(0);
                     this.updateFieldRight(1);
                  }
               }
            }
         } else {
            final String var2 = this.messageEditText.getCaption();
            boolean var3;
            if (var2 == null || this.sendButton.getVisibility() != 0 && this.expandStickersButton.getVisibility() != 0) {
               var3 = false;
            } else {
               var3 = true;
            }

            boolean var4;
            if (var2 != null || this.cancelBotButton.getVisibility() != 0 && this.expandStickersButton.getVisibility() != 0) {
               var4 = false;
            } else {
               var4 = true;
            }

            if (this.audioVideoButtonContainer.getVisibility() == 0 || var3 || var4) {
               if (!var1) {
                  this.audioVideoButtonContainer.setScaleX(0.1F);
                  this.audioVideoButtonContainer.setScaleY(0.1F);
                  this.audioVideoButtonContainer.setAlpha(0.0F);
                  if (var2 != null) {
                     this.sendButton.setScaleX(0.1F);
                     this.sendButton.setScaleY(0.1F);
                     this.sendButton.setAlpha(0.0F);
                     this.cancelBotButton.setScaleX(1.0F);
                     this.cancelBotButton.setScaleY(1.0F);
                     this.cancelBotButton.setAlpha(1.0F);
                     this.cancelBotButton.setVisibility(0);
                     this.sendButton.setVisibility(8);
                  } else {
                     this.cancelBotButton.setScaleX(0.1F);
                     this.cancelBotButton.setScaleY(0.1F);
                     this.cancelBotButton.setAlpha(0.0F);
                     this.sendButton.setScaleX(1.0F);
                     this.sendButton.setScaleY(1.0F);
                     this.sendButton.setAlpha(1.0F);
                     this.sendButton.setVisibility(0);
                     this.cancelBotButton.setVisibility(8);
                  }

                  this.audioVideoButtonContainer.setVisibility(8);
                  var6 = this.attachLayout;
                  if (var6 != null) {
                     var6.setVisibility(8);
                     if (this.delegate != null && this.getVisibility() == 0) {
                        this.delegate.onAttachButtonHidden();
                     }

                     this.updateFieldRight(0);
                  }
               } else {
                  if (this.runningAnimationType == 1 && this.messageEditText.getCaption() == null || this.runningAnimationType == 3 && var2 != null) {
                     return;
                  }

                  AnimatorSet var5 = this.runningAnimation;
                  if (var5 != null) {
                     var5.cancel();
                     this.runningAnimation = null;
                  }

                  var5 = this.runningAnimation2;
                  if (var5 != null) {
                     var5.cancel();
                     this.runningAnimation2 = null;
                  }

                  if (this.attachLayout != null) {
                     this.runningAnimation2 = new AnimatorSet();
                     this.runningAnimation2.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.attachLayout, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.attachLayout, View.SCALE_X, new float[]{0.0F})});
                     this.runningAnimation2.setDuration(100L);
                     this.runningAnimation2.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationCancel(Animator var1) {
                           if (ChatActivityEnterView.this.runningAnimation2 != null && ChatActivityEnterView.this.runningAnimation2.equals(var1)) {
                              ChatActivityEnterView.this.runningAnimation2 = null;
                           }

                        }

                        public void onAnimationEnd(Animator var1) {
                           if (ChatActivityEnterView.this.runningAnimation2 != null && ChatActivityEnterView.this.runningAnimation2.equals(var1)) {
                              ChatActivityEnterView.this.attachLayout.setVisibility(8);
                           }

                        }
                     });
                     this.runningAnimation2.start();
                     this.updateFieldRight(0);
                     if (this.delegate != null && this.getVisibility() == 0) {
                        this.delegate.onAttachButtonHidden();
                     }
                  }

                  this.runningAnimation = new AnimatorSet();
                  ArrayList var9 = new ArrayList();
                  if (this.audioVideoButtonContainer.getVisibility() == 0) {
                     var9.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_X, new float[]{0.1F}));
                     var9.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_Y, new float[]{0.1F}));
                     var9.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, new float[]{0.0F}));
                  }

                  if (this.expandStickersButton.getVisibility() == 0) {
                     var9.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_X, new float[]{0.1F}));
                     var9.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_Y, new float[]{0.1F}));
                     var9.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.ALPHA, new float[]{0.0F}));
                  }

                  if (var3) {
                     var9.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, new float[]{0.1F}));
                     var9.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, new float[]{0.1F}));
                     var9.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, new float[]{0.0F}));
                  } else if (var4) {
                     var9.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, new float[]{0.1F}));
                     var9.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, new float[]{0.1F}));
                     var9.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, new float[]{0.0F}));
                  }

                  if (var2 != null) {
                     this.runningAnimationType = 3;
                     var9.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, new float[]{1.0F}));
                     var9.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, new float[]{1.0F}));
                     var9.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, new float[]{1.0F}));
                     this.cancelBotButton.setVisibility(0);
                  } else {
                     this.runningAnimationType = 1;
                     var9.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, new float[]{1.0F}));
                     var9.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, new float[]{1.0F}));
                     var9.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, new float[]{1.0F}));
                     this.sendButton.setVisibility(0);
                  }

                  this.runningAnimation.playTogether(var9);
                  this.runningAnimation.setDuration(150L);
                  this.runningAnimation.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationCancel(Animator var1) {
                        if (ChatActivityEnterView.this.runningAnimation != null && ChatActivityEnterView.this.runningAnimation.equals(var1)) {
                           ChatActivityEnterView.this.runningAnimation = null;
                        }

                     }

                     public void onAnimationEnd(Animator var1) {
                        if (ChatActivityEnterView.this.runningAnimation != null && ChatActivityEnterView.this.runningAnimation.equals(var1)) {
                           if (var2 != null) {
                              ChatActivityEnterView.this.cancelBotButton.setVisibility(0);
                              ChatActivityEnterView.this.sendButton.setVisibility(8);
                           } else {
                              ChatActivityEnterView.this.sendButton.setVisibility(0);
                              ChatActivityEnterView.this.cancelBotButton.setVisibility(8);
                           }

                           ChatActivityEnterView.this.audioVideoButtonContainer.setVisibility(8);
                           ChatActivityEnterView.this.expandStickersButton.setVisibility(8);
                           ChatActivityEnterView.this.runningAnimation = null;
                           ChatActivityEnterView.this.runningAnimationType = 0;
                        }

                     }
                  });
                  this.runningAnimation.start();
               }
            }
         }

      }
   }

   private void checkStickresExpandHeight() {
      android.graphics.Point var1 = AndroidUtilities.displaySize;
      int var2;
      if (var1.x > var1.y) {
         var2 = this.keyboardHeightLand;
      } else {
         var2 = this.keyboardHeight;
      }

      int var3 = this.originalViewHeight;
      int var4;
      if (VERSION.SDK_INT >= 21) {
         var4 = AndroidUtilities.statusBarHeight;
      } else {
         var4 = 0;
      }

      var3 = var3 - var4 - ActionBar.getCurrentActionBarHeight() - this.getHeight() + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
      var4 = var3;
      if (this.searchingType == 2) {
         var4 = Math.min(var3, AndroidUtilities.dp(120.0F) + var2);
      }

      var3 = this.emojiView.getLayoutParams().height;
      if (var3 != var4) {
         Animator var5 = this.stickersExpansionAnim;
         if (var5 != null) {
            var5.cancel();
            this.stickersExpansionAnim = null;
         }

         this.stickersExpandedHeight = var4;
         AnimatorSet var6;
         if (var3 > var4) {
            var6 = new AnimatorSet();
            var6.playTogether(new Animator[]{ObjectAnimator.ofInt(this, this.roundedTranslationYProperty, new int[]{-(this.stickersExpandedHeight - var2)}), ObjectAnimator.ofInt(this.emojiView, this.roundedTranslationYProperty, new int[]{-(this.stickersExpandedHeight - var2)})});
            ((ObjectAnimator)var6.getChildAnimations().get(0)).addUpdateListener(new _$$Lambda$ChatActivityEnterView$wjaxKxy8o6FENKkopnAtGTac1QI(this));
            var6.setDuration(400L);
            var6.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            var6.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  ChatActivityEnterView.this.stickersExpansionAnim = null;
                  if (ChatActivityEnterView.this.emojiView != null) {
                     ChatActivityEnterView.this.emojiView.getLayoutParams().height = ChatActivityEnterView.this.stickersExpandedHeight;
                     ChatActivityEnterView.this.emojiView.setLayerType(0, (Paint)null);
                  }

               }
            });
            this.stickersExpansionAnim = var6;
            this.emojiView.setLayerType(2, (Paint)null);
            var6.start();
         } else {
            this.emojiView.getLayoutParams().height = this.stickersExpandedHeight;
            this.sizeNotifierLayout.requestLayout();
            var3 = this.messageEditText.getSelectionStart();
            var4 = this.messageEditText.getSelectionEnd();
            EditTextCaption var7 = this.messageEditText;
            var7.setText(var7.getText());
            this.messageEditText.setSelection(var3, var4);
            var6 = new AnimatorSet();
            var6.playTogether(new Animator[]{ObjectAnimator.ofInt(this, this.roundedTranslationYProperty, new int[]{-(this.stickersExpandedHeight - var2)}), ObjectAnimator.ofInt(this.emojiView, this.roundedTranslationYProperty, new int[]{-(this.stickersExpandedHeight - var2)})});
            ((ObjectAnimator)var6.getChildAnimations().get(0)).addUpdateListener(new _$$Lambda$ChatActivityEnterView$XEk0ExTurPjgeNRTljJZaBiOh9o(this));
            var6.setDuration(400L);
            var6.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            var6.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  ChatActivityEnterView.this.stickersExpansionAnim = null;
                  ChatActivityEnterView.this.emojiView.setLayerType(0, (Paint)null);
               }
            });
            this.stickersExpansionAnim = var6;
            this.emojiView.setLayerType(2, (Paint)null);
            var6.start();
         }

      }
   }

   private void createEmojiView() {
      if (this.emojiView == null) {
         this.emojiView = new EmojiView(this.allowStickers, this.allowGifs, this.parentActivity, true, this.info);
         this.emojiView.setVisibility(8);
         this.emojiView.setDelegate(new EmojiView.EmojiViewDelegate() {
            public boolean isExpanded() {
               return ChatActivityEnterView.this.stickersExpanded;
            }

            public boolean isSearchOpened() {
               boolean var1;
               if (ChatActivityEnterView.this.searchingType != 0) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               return var1;
            }

            // $FF: synthetic method
            public void lambda$onClearEmojiRecent$0$ChatActivityEnterView$26(DialogInterface var1, int var2) {
               ChatActivityEnterView.this.emojiView.clearRecentEmoji();
            }

            public boolean onBackspace() {
               if (ChatActivityEnterView.this.messageEditText.length() == 0) {
                  return false;
               } else {
                  ChatActivityEnterView.this.messageEditText.dispatchKeyEvent(new KeyEvent(0, 67));
                  return true;
               }
            }

            public void onClearEmojiRecent() {
               if (ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.parentActivity != null) {
                  AlertDialog.Builder var1 = new AlertDialog.Builder(ChatActivityEnterView.this.parentActivity);
                  var1.setTitle(LocaleController.getString("AppName", 2131558635));
                  var1.setMessage(LocaleController.getString("ClearRecentEmoji", 2131559113));
                  var1.setPositiveButton(LocaleController.getString("ClearButton", 2131559102).toUpperCase(), new _$$Lambda$ChatActivityEnterView$26$SN56de_QEsvpP_TQQjotkzhHl_U(this));
                  var1.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (android.content.DialogInterface.OnClickListener)null);
                  ChatActivityEnterView.this.parentFragment.showDialog(var1.create());
               }

            }

            public void onEmojiSelected(String var1) {
               int var2 = ChatActivityEnterView.this.messageEditText.getSelectionEnd();
               int var3 = var2;
               if (var2 < 0) {
                  var3 = 0;
               }

               try {
                  ChatActivityEnterView.this.innerTextChange = 2;
                  CharSequence var8 = Emoji.replaceEmoji(var1, ChatActivityEnterView.this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
                  ChatActivityEnterView.this.messageEditText.setText(ChatActivityEnterView.this.messageEditText.getText().insert(var3, var8));
                  var3 += var8.length();
                  ChatActivityEnterView.this.messageEditText.setSelection(var3, var3);
               } catch (Exception var6) {
                  FileLog.e((Throwable)var6);
               } finally {
                  ChatActivityEnterView.this.innerTextChange = 0;
               }

            }

            public void onGifSelected(Object var1, Object var2) {
               if (ChatActivityEnterView.this.stickersExpanded) {
                  if (ChatActivityEnterView.this.searchingType != 0) {
                     ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                  }

                  ChatActivityEnterView.this.setStickersExpanded(false, true, false);
               }

               if (var1 instanceof TLRPC.Document) {
                  TLRPC.Document var4 = (TLRPC.Document)var1;
                  SendMessagesHelper.getInstance(ChatActivityEnterView.this.currentAccount).sendSticker(var4, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, var2);
                  DataQuery.getInstance(ChatActivityEnterView.this.currentAccount).addRecentGif(var4, (int)(System.currentTimeMillis() / 1000L));
                  if ((int)ChatActivityEnterView.this.dialog_id == 0) {
                     MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).saveGif(var2, var4);
                  }
               } else if (var1 instanceof TLRPC.BotInlineResult) {
                  TLRPC.BotInlineResult var5 = (TLRPC.BotInlineResult)var1;
                  if (var5.document != null) {
                     DataQuery.getInstance(ChatActivityEnterView.this.currentAccount).addRecentGif(var5.document, (int)(System.currentTimeMillis() / 1000L));
                     if ((int)ChatActivityEnterView.this.dialog_id == 0) {
                        MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).saveGif(var2, var5.document);
                     }
                  }

                  TLRPC.User var6 = (TLRPC.User)var2;
                  HashMap var3 = new HashMap();
                  var3.put("id", var5.id);
                  StringBuilder var7 = new StringBuilder();
                  var7.append("");
                  var7.append(var5.query_id);
                  var3.put("query_id", var7.toString());
                  SendMessagesHelper.prepareSendingBotContextResult(var5, var3, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject);
                  if (ChatActivityEnterView.this.searchingType != 0) {
                     ChatActivityEnterView.this.searchingType = 0;
                     ChatActivityEnterView.this.emojiView.closeSearch(true);
                     ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                  }
               }

               if (ChatActivityEnterView.this.delegate != null) {
                  ChatActivityEnterView.this.delegate.onMessageSend((CharSequence)null);
               }

            }

            public void onSearchOpenClose(int var1) {
               ChatActivityEnterView.this.searchingType = var1;
               ChatActivityEnterView var2 = ChatActivityEnterView.this;
               boolean var3;
               if (var1 != 0) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               var2.setStickersExpanded(var3, false, false);
               if (ChatActivityEnterView.this.emojiTabOpen && ChatActivityEnterView.this.searchingType == 2) {
                  ChatActivityEnterView.this.checkStickresExpandHeight();
               }

            }

            public void onShowStickerSet(TLRPC.StickerSet var1, TLRPC.InputStickerSet var2) {
               if (ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.parentActivity != null) {
                  if (var1 != null) {
                     var2 = new TLRPC.TL_inputStickerSetID();
                     ((TLRPC.InputStickerSet)var2).access_hash = var1.access_hash;
                     ((TLRPC.InputStickerSet)var2).id = var1.id;
                  }

                  ChatActivityEnterView.this.parentFragment.showDialog(new StickersAlert(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.parentFragment, (TLRPC.InputStickerSet)var2, (TLRPC.TL_messages_stickerSet)null, ChatActivityEnterView.this));
               }

            }

            public void onStickerSelected(TLRPC.Document var1, Object var2) {
               if (ChatActivityEnterView.this.stickersExpanded) {
                  if (ChatActivityEnterView.this.searchingType != 0) {
                     ChatActivityEnterView.this.searchingType = 0;
                     ChatActivityEnterView.this.emojiView.closeSearch(true, MessageObject.getStickerSetId(var1));
                     ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                  }

                  ChatActivityEnterView.this.setStickersExpanded(false, true, false);
               }

               ChatActivityEnterView.this.onStickerSelected(var1, var2, false);
               if ((int)ChatActivityEnterView.this.dialog_id == 0 && MessageObject.isGifDocument(var1)) {
                  MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).saveGif(var2, var1);
               }

            }

            public void onStickerSetAdd(TLRPC.StickerSetCovered var1) {
               DataQuery.getInstance(ChatActivityEnterView.this.currentAccount).removeStickersSet(ChatActivityEnterView.this.parentActivity, var1.set, 2, ChatActivityEnterView.this.parentFragment, false);
            }

            public void onStickerSetRemove(TLRPC.StickerSetCovered var1) {
               DataQuery.getInstance(ChatActivityEnterView.this.currentAccount).removeStickersSet(ChatActivityEnterView.this.parentActivity, var1.set, 0, ChatActivityEnterView.this.parentFragment, false);
            }

            public void onStickersGroupClick(int var1) {
               if (ChatActivityEnterView.this.parentFragment != null) {
                  if (AndroidUtilities.isTablet()) {
                     ChatActivityEnterView.this.hidePopup(false);
                  }

                  GroupStickersActivity var2 = new GroupStickersActivity(var1);
                  var2.setInfo(ChatActivityEnterView.this.info);
                  ChatActivityEnterView.this.parentFragment.presentFragment(var2);
               }

            }

            public void onStickersSettingsClick() {
               if (ChatActivityEnterView.this.parentFragment != null) {
                  ChatActivityEnterView.this.parentFragment.presentFragment(new StickersActivity(0));
               }

            }

            public void onTabOpened(int var1) {
               ChatActivityEnterView.ChatActivityEnterViewDelegate var2 = ChatActivityEnterView.this.delegate;
               boolean var3;
               if (var1 == 3) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               var2.onStickersTab(var3);
               ChatActivityEnterView var4 = ChatActivityEnterView.this;
               var4.post(var4.updateExpandabilityRunnable);
            }
         });
         this.emojiView.setDragListener(new EmojiView.DragListener() {
            int initialOffset;
            boolean wasExpanded;

            private boolean allowDragging() {
               boolean var1;
               if (ChatActivityEnterView.this.stickersTabOpen && (ChatActivityEnterView.this.stickersExpanded || ChatActivityEnterView.this.messageEditText.length() <= 0) && ChatActivityEnterView.this.emojiView.areThereAnyStickers()) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               return var1;
            }

            public void onDrag(int var1) {
               if (this.allowDragging()) {
                  android.graphics.Point var2 = AndroidUtilities.displaySize;
                  int var3;
                  if (var2.x > var2.y) {
                     var3 = ChatActivityEnterView.this.keyboardHeightLand;
                  } else {
                     var3 = ChatActivityEnterView.this.keyboardHeight;
                  }

                  var1 = Math.max(Math.min(var1 + this.initialOffset, 0), -(ChatActivityEnterView.this.stickersExpandedHeight - var3));
                  EmojiView var5 = ChatActivityEnterView.this.emojiView;
                  float var4 = (float)var1;
                  var5.setTranslationY(var4);
                  ChatActivityEnterView.this.setTranslationY(var4);
                  ChatActivityEnterView var6 = ChatActivityEnterView.this;
                  var6.stickersExpansionProgress = var4 / (float)(-(var6.stickersExpandedHeight - var3));
                  ChatActivityEnterView.this.sizeNotifierLayout.invalidate();
               }
            }

            public void onDragCancel() {
               if (ChatActivityEnterView.this.stickersTabOpen) {
                  ChatActivityEnterView.this.stickersDragging = false;
                  ChatActivityEnterView.this.setStickersExpanded(this.wasExpanded, true, false);
               }
            }

            public void onDragEnd(float var1) {
               if (this.allowDragging()) {
                  ChatActivityEnterView.this.stickersDragging = false;
                  if ((!this.wasExpanded || var1 < (float)AndroidUtilities.dp(200.0F)) && (this.wasExpanded || var1 > (float)AndroidUtilities.dp(-200.0F)) && (!this.wasExpanded || ChatActivityEnterView.this.stickersExpansionProgress > 0.6F) && (this.wasExpanded || ChatActivityEnterView.this.stickersExpansionProgress < 0.4F)) {
                     ChatActivityEnterView.this.setStickersExpanded(this.wasExpanded, true, true);
                  } else {
                     ChatActivityEnterView.this.setStickersExpanded(this.wasExpanded ^ true, true, true);
                  }

               }
            }

            public void onDragStart() {
               if (this.allowDragging()) {
                  if (ChatActivityEnterView.this.stickersExpansionAnim != null) {
                     ChatActivityEnterView.this.stickersExpansionAnim.cancel();
                  }

                  ChatActivityEnterView.this.stickersDragging = true;
                  this.wasExpanded = ChatActivityEnterView.this.stickersExpanded;
                  ChatActivityEnterView.this.stickersExpanded = true;
                  ChatActivityEnterView var1 = ChatActivityEnterView.this;
                  int var2 = var1.sizeNotifierLayout.getHeight();
                  int var3;
                  if (VERSION.SDK_INT >= 21) {
                     var3 = AndroidUtilities.statusBarHeight;
                  } else {
                     var3 = 0;
                  }

                  var1.stickersExpandedHeight = var2 - var3 - ActionBar.getCurrentActionBarHeight() - ChatActivityEnterView.this.getHeight() + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                  if (ChatActivityEnterView.this.searchingType == 2) {
                     var1 = ChatActivityEnterView.this;
                     var2 = var1.stickersExpandedHeight;
                     int var4 = AndroidUtilities.dp(120.0F);
                     android.graphics.Point var5 = AndroidUtilities.displaySize;
                     if (var5.x > var5.y) {
                        var3 = ChatActivityEnterView.this.keyboardHeightLand;
                     } else {
                        var3 = ChatActivityEnterView.this.keyboardHeight;
                     }

                     var1.stickersExpandedHeight = Math.min(var2, var4 + var3);
                  }

                  ChatActivityEnterView.this.emojiView.getLayoutParams().height = ChatActivityEnterView.this.stickersExpandedHeight;
                  ChatActivityEnterView.this.emojiView.setLayerType(2, (Paint)null);
                  ChatActivityEnterView.this.sizeNotifierLayout.requestLayout();
                  ChatActivityEnterView.this.sizeNotifierLayout.setForeground(ChatActivityEnterView.this.new ScrimDrawable());
                  this.initialOffset = (int)ChatActivityEnterView.this.getTranslationY();
                  if (ChatActivityEnterView.this.delegate != null) {
                     ChatActivityEnterView.this.delegate.onStickersExpandedChange();
                  }

               }
            }
         });
         this.sizeNotifierLayout.addView(this.emojiView);
         this.checkChannelRights();
      }
   }

   private void hideRecordedAudioPanel() {
      this.audioToSendPath = null;
      this.audioToSend = null;
      this.audioToSendMessageObject = null;
      this.videoToSendMessageObject = null;
      this.videoTimelineView.destroy();
      AnimatorSet var1 = new AnimatorSet();
      var1.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.recordedAudioPanel, View.ALPHA, new float[]{0.0F})});
      var1.setDuration(200L);
      var1.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            ChatActivityEnterView.this.recordedAudioPanel.setVisibility(8);
         }
      });
      var1.start();
   }

   // $FF: synthetic method
   static boolean lambda$new$6(View var0, MotionEvent var1) {
      return true;
   }

   private void onWindowSizeChanged() {
      int var1 = this.sizeNotifierLayout.getHeight();
      int var2 = var1;
      if (!this.keyboardVisible) {
         var2 = var1 - this.emojiPadding;
      }

      ChatActivityEnterView.ChatActivityEnterViewDelegate var3 = this.delegate;
      if (var3 != null) {
         var3.onWindowSizeChanged(var2);
      }

      if (this.topView != null) {
         if (var2 < AndroidUtilities.dp(72.0F) + ActionBar.getCurrentActionBarHeight()) {
            if (this.allowShowTopView) {
               this.allowShowTopView = false;
               if (this.needShowTopView) {
                  this.topView.setVisibility(8);
                  this.topLineView.setVisibility(8);
                  this.topLineView.setAlpha(0.0F);
                  this.resizeForTopView(false);
                  View var4 = this.topView;
                  var4.setTranslationY((float)var4.getLayoutParams().height);
               }
            }
         } else if (!this.allowShowTopView) {
            this.allowShowTopView = true;
            if (this.needShowTopView) {
               this.topView.setVisibility(0);
               this.topLineView.setVisibility(0);
               this.topLineView.setAlpha(1.0F);
               this.resizeForTopView(true);
               this.topView.setTranslationY(0.0F);
            }
         }
      }

   }

   private void openKeyboardInternal() {
      byte var1;
      if (!AndroidUtilities.usingHardwareInput && !this.isPaused) {
         var1 = 2;
      } else {
         var1 = 0;
      }

      this.showPopup(var1, 0);
      this.messageEditText.requestFocus();
      AndroidUtilities.showKeyboard(this.messageEditText);
      if (this.isPaused) {
         this.showKeyboardOnResume = true;
      } else if (!AndroidUtilities.usingHardwareInput && !this.keyboardVisible && !AndroidUtilities.isInMultiwindow) {
         this.waitingForKeyboardOpen = true;
         AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
         AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100L);
      }

   }

   private void resizeForTopView(boolean var1) {
      LayoutParams var2 = (LayoutParams)this.textFieldContainer.getLayoutParams();
      int var3 = AndroidUtilities.dp(2.0F);
      int var4;
      if (var1) {
         var4 = this.topView.getLayoutParams().height;
      } else {
         var4 = 0;
      }

      var2.topMargin = var3 + var4;
      this.textFieldContainer.setLayoutParams(var2);
      var3 = AndroidUtilities.dp(51.0F);
      if (var1) {
         var4 = this.topView.getLayoutParams().height;
      } else {
         var4 = 0;
      }

      this.setMinimumHeight(var3 + var4);
      if (this.stickersExpanded) {
         if (this.searchingType == 0) {
            this.setStickersExpanded(false, true, false);
         } else {
            this.checkStickresExpandHeight();
         }
      }

   }

   private void sendMessage() {
      if (this.stickersExpanded) {
         this.setStickersExpanded(false, true, false);
         if (this.searchingType != 0) {
            this.emojiView.closeSearch(false);
            this.emojiView.hideSearchKeyboard();
         }
      }

      if (this.videoToSendMessageObject != null) {
         this.delegate.needStartRecordVideo(4);
         this.hideRecordedAudioPanel();
         this.checkSendButton(true);
      } else {
         ChatActivityEnterView.ChatActivityEnterViewDelegate var1;
         if (this.audioToSend != null) {
            MessageObject var3 = MediaController.getInstance().getPlayingMessageObject();
            if (var3 != null && var3 == this.audioToSendMessageObject) {
               MediaController.getInstance().cleanupPlayer(true, true);
            }

            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(this.audioToSend, (VideoEditedInfo)null, this.audioToSendPath, this.dialog_id, this.replyingMessageObject, (String)null, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null, 0, (Object)null);
            var1 = this.delegate;
            if (var1 != null) {
               var1.onMessageSend((CharSequence)null);
            }

            this.hideRecordedAudioPanel();
            this.checkSendButton(true);
         } else {
            Editable var2 = this.messageEditText.getText();
            if (this.processSendingText(var2)) {
               this.messageEditText.setText("");
               this.lastTypingTimeSend = 0L;
               var1 = this.delegate;
               if (var1 != null) {
                  var1.onMessageSend(var2);
               }
            } else if (this.forceShowSendButton) {
               var1 = this.delegate;
               if (var1 != null) {
                  var1.onMessageSend((CharSequence)null);
               }
            }

         }
      }
   }

   private void setEmojiButtonImage(boolean var1, boolean var2) {
      boolean var3 = var2;
      if (var2) {
         var3 = var2;
         if (this.currentEmojiIcon == -1) {
            var3 = false;
         }
      }

      byte var8;
      if (var1 && this.currentPopupContentType == 0) {
         var8 = 0;
      } else {
         EmojiView var5 = this.emojiView;
         int var4;
         if (var5 == null) {
            var4 = MessagesController.getGlobalEmojiSettings().getInt("selected_page", 0);
         } else {
            var4 = var5.getCurrentPage();
         }

         if (var4 != 0 && (this.allowStickers || this.allowGifs)) {
            if (var4 == 1) {
               var8 = 2;
            } else {
               var8 = 3;
            }
         } else {
            var8 = 1;
         }
      }

      if (this.currentEmojiIcon != var8) {
         AnimatorSet var6 = this.emojiButtonAnimation;
         Integer var9 = null;
         if (var6 != null) {
            var6.cancel();
            this.emojiButtonAnimation = null;
         }

         byte var7;
         ImageView[] var10;
         if (var8 == 0) {
            var10 = this.emojiButton;
            if (var3) {
               var7 = 1;
            } else {
               var7 = 0;
            }

            var10[var7].setImageResource(2131165487);
         } else if (var8 == 1) {
            var10 = this.emojiButton;
            if (var3) {
               var7 = 1;
            } else {
               var7 = 0;
            }

            var10[var7].setImageResource(2131165492);
         } else if (var8 == 2) {
            var10 = this.emojiButton;
            if (var3) {
               var7 = 1;
            } else {
               var7 = 0;
            }

            var10[var7].setImageResource(2131165493);
         } else if (var8 == 3) {
            var10 = this.emojiButton;
            if (var3) {
               var7 = 1;
            } else {
               var7 = 0;
            }

            var10[var7].setImageResource(2131165486);
         }

         var10 = this.emojiButton;
         if (var3) {
            var7 = 1;
         } else {
            var7 = 0;
         }

         ImageView var11 = var10[var7];
         if (var8 == 2) {
            var9 = 1;
         }

         var11.setTag(var9);
         this.currentEmojiIcon = var8;
         if (var3) {
            this.emojiButton[1].setVisibility(0);
            this.emojiButtonAnimation = new AnimatorSet();
            this.emojiButtonAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_X, new float[]{0.1F}), ObjectAnimator.ofFloat(this.emojiButton[0], View.SCALE_Y, new float[]{0.1F}), ObjectAnimator.ofFloat(this.emojiButton[0], View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.emojiButton[1], View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.emojiButton[1], View.ALPHA, new float[]{1.0F})});
            this.emojiButtonAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  if (var1.equals(ChatActivityEnterView.this.emojiButtonAnimation)) {
                     ChatActivityEnterView.this.emojiButtonAnimation = null;
                     ImageView var2 = ChatActivityEnterView.this.emojiButton[1];
                     ChatActivityEnterView.this.emojiButton[1] = ChatActivityEnterView.this.emojiButton[0];
                     ChatActivityEnterView.this.emojiButton[0] = var2;
                     ChatActivityEnterView.this.emojiButton[1].setVisibility(4);
                     ChatActivityEnterView.this.emojiButton[1].setAlpha(0.0F);
                     ChatActivityEnterView.this.emojiButton[1].setScaleX(0.1F);
                     ChatActivityEnterView.this.emojiButton[1].setScaleY(0.1F);
                  }

               }
            });
            this.emojiButtonAnimation.setDuration(150L);
            this.emojiButtonAnimation.start();
         }

      }
   }

   private void setRecordVideoButtonVisible(boolean var1, boolean var2) {
      ImageView var3 = this.videoSendButton;
      if (var3 != null) {
         Integer var4;
         if (var1) {
            var4 = 1;
         } else {
            var4 = null;
         }

         var3.setTag(var4);
         AnimatorSet var19 = this.audioVideoButtonAnimation;
         if (var19 != null) {
            var19.cancel();
            this.audioVideoButtonAnimation = null;
         }

         float var5 = 0.0F;
         float var6 = 0.0F;
         float var7 = 0.1F;
         float var10;
         if (var2) {
            boolean var8;
            SharedPreferences var20;
            label92: {
               var20 = MessagesController.getGlobalMainSettings();
               if ((int)this.dialog_id < 0) {
                  TLRPC.Chat var16 = MessagesController.getInstance(this.currentAccount).getChat(-((int)this.dialog_id));
                  if (ChatObject.isChannel(var16) && !var16.megagroup) {
                     var8 = true;
                     break label92;
                  }
               }

               var8 = false;
            }

            Editor var17 = var20.edit();
            String var21;
            if (var8) {
               var21 = "currentModeVideoChannel";
            } else {
               var21 = "currentModeVideo";
            }

            var17.putBoolean(var21, var1).commit();
            this.audioVideoButtonAnimation = new AnimatorSet();
            var19 = this.audioVideoButtonAnimation;
            var3 = this.videoSendButton;
            Property var9 = View.SCALE_X;
            if (var1) {
               var10 = 1.0F;
            } else {
               var10 = 0.1F;
            }

            ObjectAnimator var18 = ObjectAnimator.ofFloat(var3, var9, new float[]{var10});
            ImageView var11 = this.videoSendButton;
            var9 = View.SCALE_Y;
            if (var1) {
               var10 = 1.0F;
            } else {
               var10 = 0.1F;
            }

            ObjectAnimator var23 = ObjectAnimator.ofFloat(var11, var9, new float[]{var10});
            var11 = this.videoSendButton;
            Property var12 = View.ALPHA;
            if (var1) {
               var10 = 1.0F;
            } else {
               var10 = 0.0F;
            }

            ObjectAnimator var24 = ObjectAnimator.ofFloat(var11, var12, new float[]{var10});
            ImageView var25 = this.audioSendButton;
            Property var13 = View.SCALE_X;
            if (var1) {
               var10 = 0.1F;
            } else {
               var10 = 1.0F;
            }

            ObjectAnimator var26 = ObjectAnimator.ofFloat(var25, var13, new float[]{var10});
            ImageView var14 = this.audioSendButton;
            var13 = View.SCALE_Y;
            if (!var1) {
               var7 = 1.0F;
            }

            ObjectAnimator var15 = ObjectAnimator.ofFloat(var14, var13, new float[]{var7});
            var14 = this.audioSendButton;
            var13 = View.ALPHA;
            if (var1) {
               var7 = var6;
            } else {
               var7 = 1.0F;
            }

            var19.playTogether(new Animator[]{var18, var23, var24, var26, var15, ObjectAnimator.ofFloat(var14, var13, new float[]{var7})});
            this.audioVideoButtonAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  if (var1.equals(ChatActivityEnterView.this.audioVideoButtonAnimation)) {
                     ChatActivityEnterView.this.audioVideoButtonAnimation = null;
                  }

                  ImageView var2;
                  if (ChatActivityEnterView.this.videoSendButton.getTag() == null) {
                     var2 = ChatActivityEnterView.this.audioSendButton;
                  } else {
                     var2 = ChatActivityEnterView.this.videoSendButton;
                  }

                  var2.sendAccessibilityEvent(8);
               }
            });
            this.audioVideoButtonAnimation.setInterpolator(new DecelerateInterpolator());
            this.audioVideoButtonAnimation.setDuration(150L);
            this.audioVideoButtonAnimation.start();
         } else {
            ImageView var22 = this.videoSendButton;
            if (var1) {
               var10 = 1.0F;
            } else {
               var10 = 0.1F;
            }

            var22.setScaleX(var10);
            var22 = this.videoSendButton;
            if (var1) {
               var10 = 1.0F;
            } else {
               var10 = 0.1F;
            }

            var22.setScaleY(var10);
            var22 = this.videoSendButton;
            if (var1) {
               var10 = 1.0F;
            } else {
               var10 = 0.0F;
            }

            var22.setAlpha(var10);
            var22 = this.audioSendButton;
            if (var1) {
               var10 = 0.1F;
            } else {
               var10 = 1.0F;
            }

            var22.setScaleX(var10);
            var22 = this.audioSendButton;
            if (!var1) {
               var7 = 1.0F;
            }

            var22.setScaleY(var7);
            var22 = this.audioSendButton;
            if (var1) {
               var7 = var5;
            } else {
               var7 = 1.0F;
            }

            var22.setAlpha(var7);
         }

      }
   }

   private void setStickersExpanded(boolean var1, boolean var2, boolean var3) {
      if (this.emojiView != null && (var3 || this.stickersExpanded != var1)) {
         this.stickersExpanded = var1;
         ChatActivityEnterView.ChatActivityEnterViewDelegate var4 = this.delegate;
         if (var4 != null) {
            var4.onStickersExpandedChange();
         }

         android.graphics.Point var8 = AndroidUtilities.displaySize;
         final int var5;
         if (var8.x > var8.y) {
            var5 = this.keyboardHeightLand;
         } else {
            var5 = this.keyboardHeight;
         }

         Animator var9 = this.stickersExpansionAnim;
         if (var9 != null) {
            var9.cancel();
            this.stickersExpansionAnim = null;
         }

         AnimatorSet var11;
         if (this.stickersExpanded) {
            this.originalViewHeight = this.sizeNotifierLayout.getHeight();
            int var6 = this.originalViewHeight;
            int var7;
            if (VERSION.SDK_INT >= 21) {
               var7 = AndroidUtilities.statusBarHeight;
            } else {
               var7 = 0;
            }

            this.stickersExpandedHeight = var6 - var7 - ActionBar.getCurrentActionBarHeight() - this.getHeight() + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
            if (this.searchingType == 2) {
               this.stickersExpandedHeight = Math.min(this.stickersExpandedHeight, AndroidUtilities.dp(120.0F) + var5);
            }

            this.emojiView.getLayoutParams().height = this.stickersExpandedHeight;
            this.sizeNotifierLayout.requestLayout();
            this.sizeNotifierLayout.setForeground(new ChatActivityEnterView.ScrimDrawable());
            var7 = this.messageEditText.getSelectionStart();
            var6 = this.messageEditText.getSelectionEnd();
            EditTextCaption var10 = this.messageEditText;
            var10.setText(var10.getText());
            this.messageEditText.setSelection(var7, var6);
            if (var2) {
               var11 = new AnimatorSet();
               var11.playTogether(new Animator[]{ObjectAnimator.ofInt(this, this.roundedTranslationYProperty, new int[]{-(this.stickersExpandedHeight - var5)}), ObjectAnimator.ofInt(this.emojiView, this.roundedTranslationYProperty, new int[]{-(this.stickersExpandedHeight - var5)}), ObjectAnimator.ofFloat(this.stickersArrow, "animationProgress", new float[]{1.0F})});
               var11.setDuration(400L);
               var11.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
               ((ObjectAnimator)var11.getChildAnimations().get(0)).addUpdateListener(new _$$Lambda$ChatActivityEnterView$4bFyOKzZkoegxIkwH3otL_R7SUo(this, var5));
               var11.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     ChatActivityEnterView.this.stickersExpansionAnim = null;
                     ChatActivityEnterView.this.emojiView.setLayerType(0, (Paint)null);
                  }
               });
               this.stickersExpansionAnim = var11;
               this.emojiView.setLayerType(2, (Paint)null);
               var11.start();
            } else {
               this.stickersExpansionProgress = 1.0F;
               this.setTranslationY((float)(-(this.stickersExpandedHeight - var5)));
               this.emojiView.setTranslationY((float)(-(this.stickersExpandedHeight - var5)));
               this.stickersArrow.setAnimationProgress(1.0F);
            }
         } else if (var2) {
            this.closeAnimationInProgress = true;
            var11 = new AnimatorSet();
            var11.playTogether(new Animator[]{ObjectAnimator.ofInt(this, this.roundedTranslationYProperty, new int[]{0}), ObjectAnimator.ofInt(this.emojiView, this.roundedTranslationYProperty, new int[]{0}), ObjectAnimator.ofFloat(this.stickersArrow, "animationProgress", new float[]{0.0F})});
            var11.setDuration(400L);
            var11.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            ((ObjectAnimator)var11.getChildAnimations().get(0)).addUpdateListener(new _$$Lambda$ChatActivityEnterView$A5TmJlGp3wUlMNBvVwJxDClmnmg(this, var5));
            var11.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  ChatActivityEnterView.this.closeAnimationInProgress = false;
                  ChatActivityEnterView.this.stickersExpansionAnim = null;
                  if (ChatActivityEnterView.this.emojiView != null) {
                     ChatActivityEnterView.this.emojiView.getLayoutParams().height = var5;
                     ChatActivityEnterView.this.emojiView.setLayerType(0, (Paint)null);
                  }

                  if (ChatActivityEnterView.this.sizeNotifierLayout != null) {
                     ChatActivityEnterView.this.sizeNotifierLayout.requestLayout();
                     ChatActivityEnterView.this.sizeNotifierLayout.setForeground((Drawable)null);
                     ChatActivityEnterView.this.sizeNotifierLayout.setWillNotDraw(false);
                  }

               }
            });
            this.stickersExpansionAnim = var11;
            this.emojiView.setLayerType(2, (Paint)null);
            var11.start();
         } else {
            this.stickersExpansionProgress = 0.0F;
            this.setTranslationY(0.0F);
            this.emojiView.setTranslationY(0.0F);
            this.emojiView.getLayoutParams().height = var5;
            this.sizeNotifierLayout.requestLayout();
            this.sizeNotifierLayout.setForeground((Drawable)null);
            this.sizeNotifierLayout.setWillNotDraw(false);
            this.stickersArrow.setAnimationProgress(0.0F);
         }

         if (var1) {
            this.expandStickersButton.setContentDescription(LocaleController.getString("AccDescrCollapsePanel", 2131558428));
         } else {
            this.expandStickersButton.setContentDescription(LocaleController.getString("AccDescrExpandPanel", 2131558432));
         }
      }

   }

   private void showPopup(int var1, int var2) {
      BotKeyboardView var7;
      if (var1 == 1) {
         if (var2 == 0 && this.emojiView == null) {
            if (this.parentActivity == null) {
               return;
            }

            this.createEmojiView();
         }

         Object var3 = null;
         if (var2 == 0) {
            this.emojiView.setVisibility(0);
            this.emojiViewVisible = true;
            var7 = this.botKeyboardView;
            if (var7 != null && var7.getVisibility() != 8) {
               this.botKeyboardView.setVisibility(8);
            }

            var3 = this.emojiView;
         } else if (var2 == 1) {
            EmojiView var8 = this.emojiView;
            if (var8 != null && var8.getVisibility() != 8) {
               this.emojiView.setVisibility(8);
               this.emojiViewVisible = false;
            }

            this.botKeyboardView.setVisibility(0);
            var3 = this.botKeyboardView;
         }

         this.currentPopupContentType = var2;
         if (this.keyboardHeight <= 0) {
            this.keyboardHeight = MessagesController.getGlobalEmojiSettings().getInt("kbd_height", AndroidUtilities.dp(200.0F));
         }

         if (this.keyboardHeightLand <= 0) {
            this.keyboardHeightLand = MessagesController.getGlobalEmojiSettings().getInt("kbd_height_land3", AndroidUtilities.dp(200.0F));
         }

         android.graphics.Point var4 = AndroidUtilities.displaySize;
         int var5;
         if (var4.x > var4.y) {
            var5 = this.keyboardHeightLand;
         } else {
            var5 = this.keyboardHeight;
         }

         int var6 = var5;
         if (var2 == 1) {
            var6 = Math.min(this.botKeyboardView.getKeyboardHeight(), var5);
         }

         BotKeyboardView var9 = this.botKeyboardView;
         if (var9 != null) {
            var9.setPanelHeight(var6);
         }

         LayoutParams var10 = (LayoutParams)((View)var3).getLayoutParams();
         var10.height = var6;
         ((View)var3).setLayoutParams(var10);
         if (!AndroidUtilities.isInMultiwindow) {
            AndroidUtilities.hideKeyboard(this.messageEditText);
         }

         SizeNotifierFrameLayout var11 = this.sizeNotifierLayout;
         if (var11 != null) {
            this.emojiPadding = var6;
            var11.requestLayout();
            this.setEmojiButtonImage(true, true);
            this.updateBotButton();
            this.onWindowSizeChanged();
         }
      } else {
         if (this.emojiButton != null) {
            this.setEmojiButtonImage(false, true);
         }

         this.currentPopupContentType = -1;
         if (this.emojiView != null) {
            this.emojiViewVisible = false;
            if (AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow) {
               this.emojiView.setVisibility(8);
            }
         }

         var7 = this.botKeyboardView;
         if (var7 != null) {
            var7.setVisibility(8);
         }

         if (this.sizeNotifierLayout != null) {
            if (var1 == 0) {
               this.emojiPadding = 0;
            }

            this.sizeNotifierLayout.requestLayout();
            this.onWindowSizeChanged();
         }

         this.updateBotButton();
      }

      if (this.stickersTabOpen || this.emojiTabOpen) {
         this.checkSendButton(true);
      }

      if (this.stickersExpanded && var1 != 1) {
         this.setStickersExpanded(false, false, false);
      }

   }

   private void updateBotButton() {
      ImageView var1 = this.botButton;
      if (var1 != null) {
         if (!this.hasBotCommands && this.botReplyMarkup == null) {
            var1.setVisibility(8);
         } else {
            if (this.botButton.getVisibility() != 0) {
               this.botButton.setVisibility(0);
            }

            if (this.botReplyMarkup != null) {
               if (this.isPopupShowing() && this.currentPopupContentType == 1) {
                  this.botButton.setImageResource(2131165487);
                  this.botButton.setContentDescription(LocaleController.getString("AccDescrShowKeyboard", 2131558473));
               } else {
                  this.botButton.setImageResource(2131165482);
                  this.botButton.setContentDescription(LocaleController.getString("AccDescrBotKeyboard", 2131558416));
               }
            } else {
               this.botButton.setImageResource(2131165481);
               this.botButton.setContentDescription(LocaleController.getString("AccDescrBotCommands", 2131558415));
            }
         }

         float var3;
         LinearLayout var4;
         label57: {
            this.updateFieldRight(2);
            var4 = this.attachLayout;
            ImageView var2 = this.botButton;
            if (var2 == null || var2.getVisibility() == 8) {
               var2 = this.notifyButton;
               if (var2 == null || var2.getVisibility() == 8) {
                  var3 = 48.0F;
                  break label57;
               }
            }

            var3 = 96.0F;
         }

         var4.setPivotX((float)AndroidUtilities.dp(var3));
      }
   }

   private void updateFieldHint() {
      int var1 = (int)this.dialog_id;
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 < 0) {
         TLRPC.Chat var4 = MessagesController.getInstance(this.currentAccount).getChat(-((int)this.dialog_id));
         var3 = var2;
         if (ChatObject.isChannel(var4)) {
            var3 = var2;
            if (!var4.megagroup) {
               var3 = true;
            }
         }
      }

      if (this.editingMessageObject != null) {
         EditTextCaption var5 = this.messageEditText;
         String var6;
         if (this.editingCaption) {
            var6 = LocaleController.getString("Caption", 2131558904);
         } else {
            var6 = LocaleController.getString("TypeMessage", 2131560921);
         }

         var5.setHintText(var6);
      } else if (var3) {
         if (this.silent) {
            this.messageEditText.setHintText(LocaleController.getString("ChannelSilentBroadcast", 2131559003));
         } else {
            this.messageEditText.setHintText(LocaleController.getString("ChannelBroadcast", 2131558936));
         }
      } else {
         this.messageEditText.setHintText(LocaleController.getString("TypeMessage", 2131560921));
      }

   }

   private void updateFieldRight(int var1) {
      EditTextCaption var2 = this.messageEditText;
      if (var2 != null && this.editingMessageObject == null) {
         LayoutParams var4 = (LayoutParams)var2.getLayoutParams();
         ImageView var3;
         if (var1 == 1) {
            label63: {
               var3 = this.botButton;
               if (var3 == null || var3.getVisibility() != 0) {
                  var3 = this.notifyButton;
                  if (var3 == null || var3.getVisibility() != 0) {
                     var4.rightMargin = AndroidUtilities.dp(50.0F);
                     break label63;
                  }
               }

               var4.rightMargin = AndroidUtilities.dp(98.0F);
            }
         } else if (var1 == 2) {
            if (var4.rightMargin != AndroidUtilities.dp(2.0F)) {
               label64: {
                  var3 = this.botButton;
                  if (var3 == null || var3.getVisibility() != 0) {
                     var3 = this.notifyButton;
                     if (var3 == null || var3.getVisibility() != 0) {
                        var4.rightMargin = AndroidUtilities.dp(50.0F);
                        break label64;
                     }
                  }

                  var4.rightMargin = AndroidUtilities.dp(98.0F);
               }
            }
         } else {
            var4.rightMargin = AndroidUtilities.dp(2.0F);
         }

         this.messageEditText.setLayoutParams(var4);
      }

   }

   private void updateRecordIntefrace() {
      boolean var1 = this.recordingAudioVideo;
      Integer var2 = 0;
      AnimatorSet var7;
      if (var1) {
         if (this.recordInterfaceState == 1) {
            return;
         }

         this.recordInterfaceState = 1;

         try {
            if (this.wakeLock == null) {
               this.wakeLock = ((PowerManager)ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(536870918, "telegram:audio_record_lock");
               this.wakeLock.acquire();
            }
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
         }

         AndroidUtilities.lockOrientation(this.parentActivity);
         ChatActivityEnterView.ChatActivityEnterViewDelegate var3 = this.delegate;
         if (var3 != null) {
            var3.needStartRecordAudio(0);
         }

         this.recordPanel.setVisibility(0);
         this.recordCircle.setVisibility(0);
         this.recordCircle.setAmplitude(0.0D);
         this.recordTimeText.setText(String.format("%02d:%02d.%02d", var2, var2, var2));
         this.recordDot.resetAlpha();
         this.lastTimeString = null;
         this.lastTypingSendTime = -1L;
         LayoutParams var6 = (LayoutParams)this.slideText.getLayoutParams();
         var6.leftMargin = AndroidUtilities.dp(30.0F);
         this.slideText.setLayoutParams(var6);
         this.slideText.setAlpha(1.0F);
         this.recordPanel.setX((float)AndroidUtilities.displaySize.x);
         var7 = this.runningAnimationAudio;
         if (var7 != null) {
            var7.cancel();
         }

         this.runningAnimationAudio = new AnimatorSet();
         this.runningAnimationAudio.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.recordPanel, View.TRANSLATION_X, new float[]{0.0F}), ObjectAnimator.ofFloat(this.recordCircle, this.recordCircleScale, new float[]{1.0F}), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, new float[]{0.0F})});
         this.runningAnimationAudio.setDuration(300L);
         this.runningAnimationAudio.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               if (ChatActivityEnterView.this.runningAnimationAudio != null && ChatActivityEnterView.this.runningAnimationAudio.equals(var1)) {
                  ChatActivityEnterView.this.recordPanel.setX(0.0F);
                  ChatActivityEnterView.this.runningAnimationAudio = null;
               }

            }
         });
         this.runningAnimationAudio.setInterpolator(new DecelerateInterpolator());
         this.runningAnimationAudio.start();
      } else {
         WakeLock var8 = this.wakeLock;
         if (var8 != null) {
            try {
               var8.release();
               this.wakeLock = null;
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }
         }

         AndroidUtilities.unlockOrientation(this.parentActivity);
         if (this.recordInterfaceState == 0) {
            return;
         }

         this.recordInterfaceState = 0;
         var7 = this.runningAnimationAudio;
         if (var7 != null) {
            var7.cancel();
         }

         this.runningAnimationAudio = new AnimatorSet();
         this.runningAnimationAudio.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.recordPanel, View.TRANSLATION_X, new float[]{(float)AndroidUtilities.displaySize.x}), ObjectAnimator.ofFloat(this.recordCircle, this.recordCircleScale, new float[]{0.0F}), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, new float[]{1.0F})});
         this.runningAnimationAudio.setDuration(300L);
         this.runningAnimationAudio.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               if (ChatActivityEnterView.this.runningAnimationAudio != null && ChatActivityEnterView.this.runningAnimationAudio.equals(var1)) {
                  LayoutParams var2 = (LayoutParams)ChatActivityEnterView.this.slideText.getLayoutParams();
                  var2.leftMargin = AndroidUtilities.dp(30.0F);
                  ChatActivityEnterView.this.slideText.setLayoutParams(var2);
                  ChatActivityEnterView.this.slideText.setAlpha(1.0F);
                  ChatActivityEnterView.this.recordPanel.setVisibility(8);
                  ChatActivityEnterView.this.recordCircle.setVisibility(8);
                  ChatActivityEnterView.this.recordCircle.setSendButtonInvisible();
                  ChatActivityEnterView.this.runningAnimationAudio = null;
               }

            }
         });
         this.runningAnimationAudio.setInterpolator(new AccelerateInterpolator());
         this.runningAnimationAudio.start();
      }

   }

   public void addEmojiToRecent(String var1) {
      this.createEmojiView();
      this.emojiView.addEmojiToRecent(var1);
   }

   public void addRecentGif(TLRPC.Document var1) {
      DataQuery.getInstance(this.currentAccount).addRecentGif(var1, (int)(System.currentTimeMillis() / 1000L));
      EmojiView var2 = this.emojiView;
      if (var2 != null) {
         var2.addRecentGif(var1);
      }

   }

   public void addStickerToRecent(TLRPC.Document var1) {
      this.createEmojiView();
      this.emojiView.addRecentSticker(var1);
   }

   public void addTopView(View var1, View var2, int var3) {
      if (var1 != null) {
         this.topLineView = var2;
         this.topLineView.setVisibility(8);
         this.topLineView.setAlpha(0.0F);
         this.addView(this.topLineView, LayoutHelper.createFrame(-1, 1.0F, 51, 0.0F, (float)(var3 + 1), 0.0F, 0.0F));
         this.topView = var1;
         this.topView.setVisibility(8);
         var1 = this.topView;
         float var4 = (float)var3;
         var1.setTranslationY(var4);
         this.addView(this.topView, 0, LayoutHelper.createFrame(-1, var4, 51, 0.0F, 2.0F, 0.0F, 0.0F));
         this.needShowTopView = false;
      }
   }

   public void cancelRecordingAudioVideo() {
      if (this.hasRecordVideo && this.videoSendButton.getTag() != null) {
         CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
         this.delegate.needStartRecordVideo(2);
      } else {
         this.delegate.needStartRecordAudio(0);
         MediaController.getInstance().stopRecording(0);
      }

      this.recordingAudioVideo = false;
      this.updateRecordIntefrace();
   }

   public void checkChannelRights() {
      ChatActivity var1 = this.parentFragment;
      if (var1 != null) {
         TLRPC.Chat var4 = var1.getCurrentChat();
         if (var4 != null) {
            FrameLayout var2 = this.audioVideoButtonContainer;
            float var3;
            if (ChatObject.canSendMedia(var4)) {
               var3 = 1.0F;
            } else {
               var3 = 0.5F;
            }

            var2.setAlpha(var3);
            EmojiView var5 = this.emojiView;
            if (var5 != null) {
               var5.setStickersBanned(ChatObject.canSendStickers(var4) ^ true, var4.id);
            }
         }

      }
   }

   public void checkRoundVideo() {
      if (!this.hasRecordVideo) {
         if (this.attachLayout != null && VERSION.SDK_INT >= 18) {
            long var1 = this.dialog_id;
            int var3 = (int)var1;
            int var4 = (int)(var1 >> 32);
            boolean var5 = true;
            if (var3 == 0 && var4 != 0) {
               if (AndroidUtilities.getPeerLayerVersion(MessagesController.getInstance(this.currentAccount).getEncryptedChat(var4).layer) >= 66) {
                  this.hasRecordVideo = true;
               }
            } else {
               this.hasRecordVideo = true;
            }

            boolean var7;
            if ((int)this.dialog_id < 0) {
               TLRPC.Chat var6 = MessagesController.getInstance(this.currentAccount).getChat(-((int)this.dialog_id));
               if (!ChatObject.isChannel(var6) || var6.megagroup) {
                  var5 = false;
               }

               var7 = var5;
               if (var5) {
                  var7 = var5;
                  if (!var6.creator) {
                     label50: {
                        TLRPC.TL_chatAdminRights var9 = var6.admin_rights;
                        if (var9 != null) {
                           var7 = var5;
                           if (var9.post_messages) {
                              break label50;
                           }
                        }

                        this.hasRecordVideo = false;
                        var7 = var5;
                     }
                  }
               }
            } else {
               var7 = false;
            }

            if (!SharedConfig.inappCamera) {
               this.hasRecordVideo = false;
            }

            if (this.hasRecordVideo) {
               if (SharedConfig.hasCameraCache) {
                  CameraController.getInstance().initCamera((Runnable)null);
               }

               SharedPreferences var8 = MessagesController.getGlobalMainSettings();
               String var10;
               if (var7) {
                  var10 = "currentModeVideoChannel";
               } else {
                  var10 = "currentModeVideo";
               }

               this.setRecordVideoButtonVisible(var8.getBoolean(var10, var7), false);
            } else {
               this.setRecordVideoButtonVisible(false, false);
            }

         } else {
            this.hasRecordVideo = false;
            this.setRecordVideoButtonVisible(false, false);
         }
      }
   }

   public void closeKeyboard() {
      AndroidUtilities.hideKeyboard(this.messageEditText);
   }

   public void didPressedBotButton(TLRPC.KeyboardButton var1, MessageObject var2, MessageObject var3) {
      if (var1 != null && var3 != null) {
         if (var1 instanceof TLRPC.TL_keyboardButton) {
            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(var1.text, this.dialog_id, var2, (TLRPC.WebPage)null, false, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
         } else if (var1 instanceof TLRPC.TL_keyboardButtonUrl) {
            this.parentFragment.showOpenUrlAlert(var1.url, true);
         } else if (var1 instanceof TLRPC.TL_keyboardButtonRequestPhone) {
            this.parentFragment.shareMyContact(var3);
         } else if (var1 instanceof TLRPC.TL_keyboardButtonRequestGeoLocation) {
            AlertDialog.Builder var6 = new AlertDialog.Builder(this.parentActivity);
            var6.setTitle(LocaleController.getString("ShareYouLocationTitle", 2131560756));
            var6.setMessage(LocaleController.getString("ShareYouLocationInfo", 2131560754));
            var6.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$ChatActivityEnterView$fmPI_spkDt8Wbdud0XG8IKtQnwY(this, var3, var1));
            var6.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (android.content.DialogInterface.OnClickListener)null);
            this.parentFragment.showDialog(var6.create());
         } else if (!(var1 instanceof TLRPC.TL_keyboardButtonCallback) && !(var1 instanceof TLRPC.TL_keyboardButtonGame) && !(var1 instanceof TLRPC.TL_keyboardButtonBuy) && !(var1 instanceof TLRPC.TL_keyboardButtonUrlAuth)) {
            if (var1 instanceof TLRPC.TL_keyboardButtonSwitchInline) {
               if (this.parentFragment.processSwitchButton((TLRPC.TL_keyboardButtonSwitchInline)var1)) {
                  return;
               }

               if (var1.same_peer) {
                  TLRPC.Message var7 = var3.messageOwner;
                  int var4 = var7.from_id;
                  int var5 = var7.via_bot_id;
                  if (var5 != 0) {
                     var4 = var5;
                  }

                  TLRPC.User var10 = MessagesController.getInstance(this.currentAccount).getUser(var4);
                  if (var10 == null) {
                     return;
                  }

                  StringBuilder var8 = new StringBuilder();
                  var8.append("@");
                  var8.append(var10.username);
                  var8.append(" ");
                  var8.append(var1.query);
                  this.setFieldText(var8.toString());
               } else {
                  Bundle var9 = new Bundle();
                  var9.putBoolean("onlySelect", true);
                  var9.putInt("dialogsType", 1);
                  DialogsActivity var11 = new DialogsActivity(var9);
                  var11.setDelegate(new _$$Lambda$ChatActivityEnterView$G5pe20Ftkscr87_dZmhZj8HOVnM(this, var3, var1));
                  this.parentFragment.presentFragment(var11);
               }
            }
         } else {
            SendMessagesHelper.getInstance(this.currentAccount).sendCallback(true, var3, var1, this.parentFragment);
         }
      }

   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.emojiDidLoad) {
         EmojiView var16 = this.emojiView;
         if (var16 != null) {
            var16.invalidateViews();
         }

         BotKeyboardView var17 = this.botKeyboardView;
         if (var17 != null) {
            var17.invalidateViews();
         }
      } else {
         int var4 = NotificationCenter.recordProgressChanged;
         byte var5 = 0;
         byte var15 = 0;
         if (var1 == var4) {
            long var6 = (Long)var3[0];
            long var8 = var6 / 1000L;
            var1 = (int)(var6 % 1000L) / 10;
            String var10 = String.format("%02d:%02d.%02d", var8 / 60L, var8 % 60L, var1);
            String var11 = this.lastTimeString;
            if (var11 == null || !var11.equals(var10)) {
               if (this.lastTypingSendTime != var8 && var8 % 5L == 0L) {
                  this.lastTypingSendTime = var8;
                  MessagesController var12 = MessagesController.getInstance(this.currentAccount);
                  var8 = this.dialog_id;
                  ImageView var31 = this.videoSendButton;
                  byte var14;
                  if (var31 != null && var31.getTag() != null) {
                     var14 = 7;
                  } else {
                     var14 = 1;
                  }

                  var12.sendTyping(var8, var14, 0);
               }

               TextView var33 = this.recordTimeText;
               if (var33 != null) {
                  var33.setText(var10);
               }
            }

            ChatActivityEnterView.RecordCircle var28 = this.recordCircle;
            if (var28 != null) {
               var28.setAmplitude((Double)var3[1]);
            }

            ImageView var18 = this.videoSendButton;
            if (var18 != null && var18.getTag() != null && var6 >= 59500L) {
               this.startedDraggingX = -1.0F;
               this.delegate.needStartRecordVideo(3);
            }
         } else if (var1 == NotificationCenter.closeChats) {
            EditTextCaption var19 = this.messageEditText;
            if (var19 != null && var19.isFocused()) {
               AndroidUtilities.hideKeyboard(this.messageEditText);
            }
         } else {
            Integer var20;
            if (var1 != NotificationCenter.recordStartError && var1 != NotificationCenter.recordStopped) {
               if (var1 == NotificationCenter.recordStarted) {
                  if (!this.recordingAudioVideo) {
                     this.recordingAudioVideo = true;
                     this.updateRecordIntefrace();
                  }
               } else if (var1 == NotificationCenter.audioDidSent) {
                  Object var29 = var3[0];
                  if (var29 instanceof VideoEditedInfo) {
                     this.videoToSendMessageObject = (VideoEditedInfo)var29;
                     this.audioToSendPath = (String)var3[1];
                     this.videoTimelineView.setVideoPath(this.audioToSendPath);
                     this.videoTimelineView.setVisibility(0);
                     this.videoTimelineView.setMinProgressDiff(1000.0F / (float)this.videoToSendMessageObject.estimatedDuration);
                     this.recordedAudioBackground.setVisibility(8);
                     this.recordedAudioTimeTextView.setVisibility(8);
                     this.recordedAudioPlayButton.setVisibility(8);
                     this.recordedAudioSeekBar.setVisibility(8);
                     this.recordedAudioPanel.setAlpha(1.0F);
                     this.recordedAudioPanel.setVisibility(0);
                     this.closeKeyboard();
                     this.hidePopup(false);
                     this.checkSendButton(false);
                  } else {
                     this.audioToSend = (TLRPC.TL_document)var3[0];
                     this.audioToSendPath = (String)var3[1];
                     if (this.audioToSend != null) {
                        if (this.recordedAudioPanel == null) {
                           return;
                        }

                        this.videoTimelineView.setVisibility(8);
                        this.recordedAudioBackground.setVisibility(0);
                        this.recordedAudioTimeTextView.setVisibility(0);
                        this.recordedAudioPlayButton.setVisibility(0);
                        this.recordedAudioSeekBar.setVisibility(0);
                        TLRPC.TL_message var21 = new TLRPC.TL_message();
                        var21.out = true;
                        var21.id = 0;
                        var21.to_id = new TLRPC.TL_peerUser();
                        TLRPC.Peer var30 = var21.to_id;
                        var1 = UserConfig.getInstance(this.currentAccount).getClientUserId();
                        var21.from_id = var1;
                        var30.user_id = var1;
                        var21.date = (int)(System.currentTimeMillis() / 1000L);
                        var21.message = "";
                        var21.attachPath = this.audioToSendPath;
                        var21.media = new TLRPC.TL_messageMediaDocument();
                        TLRPC.MessageMedia var32 = var21.media;
                        var32.flags |= 3;
                        var32.document = this.audioToSend;
                        var21.flags |= 768;
                        this.audioToSendMessageObject = new MessageObject(UserConfig.selectedAccount, var21, false);
                        this.recordedAudioPanel.setAlpha(1.0F);
                        this.recordedAudioPanel.setVisibility(0);
                        var1 = 0;

                        while(true) {
                           if (var1 >= this.audioToSend.attributes.size()) {
                              var1 = 0;
                              break;
                           }

                           TLRPC.DocumentAttribute var22 = (TLRPC.DocumentAttribute)this.audioToSend.attributes.get(var1);
                           if (var22 instanceof TLRPC.TL_documentAttributeAudio) {
                              var1 = var22.duration;
                              break;
                           }

                           ++var1;
                        }

                        for(var2 = 0; var2 < this.audioToSend.attributes.size(); ++var2) {
                           TLRPC.DocumentAttribute var34 = (TLRPC.DocumentAttribute)this.audioToSend.attributes.get(var2);
                           if (var34 instanceof TLRPC.TL_documentAttributeAudio) {
                              byte[] var23 = var34.waveform;
                              if (var23 == null || var23.length == 0) {
                                 var34.waveform = MediaController.getInstance().getWaveform(this.audioToSendPath);
                              }

                              this.recordedAudioSeekBar.setWaveform(var34.waveform);
                              break;
                           }
                        }

                        this.recordedAudioTimeTextView.setText(String.format("%d:%02d", var1 / 60, var1 % 60));
                        this.closeKeyboard();
                        this.hidePopup(false);
                        this.checkSendButton(false);
                     } else {
                        ChatActivityEnterView.ChatActivityEnterViewDelegate var24 = this.delegate;
                        if (var24 != null) {
                           var24.onMessageSend((CharSequence)null);
                        }
                     }
                  }
               } else if (var1 == NotificationCenter.audioRouteChanged) {
                  if (this.parentActivity != null) {
                     boolean var13 = (Boolean)var3[0];
                     Activity var25 = this.parentActivity;
                     if (var13) {
                        var1 = var15;
                     } else {
                        var1 = Integer.MIN_VALUE;
                     }

                     var25.setVolumeControlStream(var1);
                  }
               } else if (var1 == NotificationCenter.messagePlayingDidReset) {
                  if (this.audioToSendMessageObject != null && !MediaController.getInstance().isPlayingMessage(this.audioToSendMessageObject)) {
                     this.recordedAudioPlayButton.setImageDrawable(this.playDrawable);
                     this.recordedAudioPlayButton.setContentDescription(LocaleController.getString("AccActionPlay", 2131558409));
                     this.recordedAudioSeekBar.setProgress(0.0F);
                  }
               } else if (var1 == NotificationCenter.messagePlayingProgressDidChanged) {
                  var20 = (Integer)var3[0];
                  if (this.audioToSendMessageObject != null && MediaController.getInstance().isPlayingMessage(this.audioToSendMessageObject)) {
                     MessageObject var35 = MediaController.getInstance().getPlayingMessageObject();
                     MessageObject var26 = this.audioToSendMessageObject;
                     var26.audioProgress = var35.audioProgress;
                     var26.audioProgressSec = var35.audioProgressSec;
                     if (!this.recordedAudioSeekBar.isDragging()) {
                        this.recordedAudioSeekBar.setProgress(this.audioToSendMessageObject.audioProgress);
                     }
                  }
               } else if (var1 == NotificationCenter.featuredStickersDidLoad && this.emojiButton != null) {
                  var1 = var5;

                  while(true) {
                     ImageView[] var27 = this.emojiButton;
                     if (var1 >= var27.length) {
                        break;
                     }

                     var27[var1].invalidate();
                     ++var1;
                  }
               }
            } else {
               if (this.recordingAudioVideo) {
                  MessagesController.getInstance(this.currentAccount).sendTyping(this.dialog_id, 2, 0);
                  this.recordingAudioVideo = false;
                  this.updateRecordIntefrace();
               }

               if (var1 == NotificationCenter.recordStopped) {
                  var20 = (Integer)var3[0];
                  if (var20 == 2) {
                     this.videoTimelineView.setVisibility(0);
                     this.recordedAudioBackground.setVisibility(8);
                     this.recordedAudioTimeTextView.setVisibility(8);
                     this.recordedAudioPlayButton.setVisibility(8);
                     this.recordedAudioSeekBar.setVisibility(8);
                     this.recordedAudioPanel.setAlpha(1.0F);
                     this.recordedAudioPanel.setVisibility(0);
                  } else {
                     var20;
                  }
               }
            }
         }
      }

   }

   public void doneEditingMessage() {
      if (this.editingMessageObject != null) {
         this.delegate.onMessageEditEnd(true);
         this.showEditDoneProgress(true, true);
         CharSequence[] var1 = new CharSequence[]{this.messageEditText.getText()};
         ArrayList var2 = DataQuery.getInstance(this.currentAccount).getEntities(var1);
         this.editingMessageReqId = SendMessagesHelper.getInstance(this.currentAccount).editMessage(this.editingMessageObject, var1[0].toString(), this.messageWebPageSearch, this.parentFragment, var2, new _$$Lambda$ChatActivityEnterView$ORXTEQMKGHUriZg_5Y7AosAFGHI(this));
      }

   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      if (var2 == this.topView) {
         var1.save();
         var1.clipRect(0, 0, this.getMeasuredWidth(), var2.getLayoutParams().height + AndroidUtilities.dp(2.0F));
      }

      boolean var5 = super.drawChild(var1, var2, var3);
      if (var2 == this.topView) {
         var1.restore();
      }

      return var5;
   }

   public ImageView getAttachButton() {
      return this.attachButton;
   }

   public int getCursorPosition() {
      EditTextCaption var1 = this.messageEditText;
      return var1 == null ? 0 : var1.getSelectionStart();
   }

   public EditTextCaption getEditField() {
      return this.messageEditText;
   }

   public MessageObject getEditingMessageObject() {
      return this.editingMessageObject;
   }

   public int getEmojiPadding() {
      return this.emojiPadding;
   }

   public EmojiView getEmojiView() {
      return this.emojiView;
   }

   public CharSequence getFieldText() {
      return this.hasText() ? this.messageEditText.getText() : null;
   }

   public int getSelectionLength() {
      EditTextCaption var1 = this.messageEditText;
      if (var1 == null) {
         return 0;
      } else {
         int var2;
         int var3;
         try {
            var2 = var1.getSelectionEnd();
            var3 = this.messageEditText.getSelectionStart();
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
            return 0;
         }

         return var2 - var3;
      }
   }

   public boolean hasAudioToSend() {
      boolean var1;
      if (this.audioToSendMessageObject == null && this.videoToSendMessageObject == null) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   public boolean hasRecordVideo() {
      return this.hasRecordVideo;
   }

   public boolean hasText() {
      EditTextCaption var1 = this.messageEditText;
      boolean var2;
      if (var1 != null && var1.length() > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void hideEmojiView() {
      if (!this.emojiViewVisible) {
         EmojiView var1 = this.emojiView;
         if (var1 != null && var1.getVisibility() != 8) {
            this.emojiView.setVisibility(8);
         }
      }

   }

   public void hidePopup(boolean var1) {
      if (this.isPopupShowing()) {
         if (this.currentPopupContentType == 1 && var1 && this.botButtonsMessageObject != null) {
            Editor var2 = MessagesController.getMainSettings(this.currentAccount).edit();
            StringBuilder var3 = new StringBuilder();
            var3.append("hidekeyboard_");
            var3.append(this.dialog_id);
            var2.putInt(var3.toString(), this.botButtonsMessageObject.getId()).commit();
         }

         if (var1 && this.searchingType != 0) {
            this.searchingType = 0;
            this.emojiView.closeSearch(true);
            this.messageEditText.requestFocus();
            this.setStickersExpanded(false, true, false);
            if (this.emojiTabOpen) {
               this.checkSendButton(true);
            }
         } else {
            if (this.searchingType != 0) {
               this.searchingType = 0;
               this.emojiView.closeSearch(false);
               this.messageEditText.requestFocus();
            }

            this.showPopup(0, 0);
         }
      }

   }

   public void hideTopView(boolean var1) {
      if (this.topView != null && this.topViewShowed) {
         this.topViewShowed = false;
         this.needShowTopView = false;
         if (this.allowShowTopView) {
            AnimatorSet var2 = this.currentTopViewAnimation;
            if (var2 != null) {
               var2.cancel();
               this.currentTopViewAnimation = null;
            }

            View var4;
            if (var1) {
               this.currentTopViewAnimation = new AnimatorSet();
               AnimatorSet var3 = this.currentTopViewAnimation;
               var4 = this.topView;
               var3.playTogether(new Animator[]{ObjectAnimator.ofFloat(var4, View.TRANSLATION_Y, new float[]{(float)var4.getLayoutParams().height}), ObjectAnimator.ofFloat(this.topLineView, View.ALPHA, new float[]{0.0F})});
               this.currentTopViewAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationCancel(Animator var1) {
                     if (ChatActivityEnterView.this.currentTopViewAnimation != null && ChatActivityEnterView.this.currentTopViewAnimation.equals(var1)) {
                        ChatActivityEnterView.this.currentTopViewAnimation = null;
                     }

                  }

                  public void onAnimationEnd(Animator var1) {
                     if (ChatActivityEnterView.this.currentTopViewAnimation != null && ChatActivityEnterView.this.currentTopViewAnimation.equals(var1)) {
                        ChatActivityEnterView.this.topView.setVisibility(8);
                        ChatActivityEnterView.this.topLineView.setVisibility(8);
                        ChatActivityEnterView.this.resizeForTopView(false);
                        ChatActivityEnterView.this.currentTopViewAnimation = null;
                     }

                  }
               });
               this.currentTopViewAnimation.setDuration(200L);
               this.currentTopViewAnimation.setInterpolator(CubicBezierInterpolator.DEFAULT);
               this.currentTopViewAnimation.start();
            } else {
               this.topView.setVisibility(8);
               this.topLineView.setVisibility(8);
               this.topLineView.setAlpha(0.0F);
               this.resizeForTopView(false);
               var4 = this.topView;
               var4.setTranslationY((float)var4.getLayoutParams().height);
            }
         }
      }

   }

   public boolean isEditingCaption() {
      return this.editingCaption;
   }

   public boolean isEditingMessage() {
      boolean var1;
      if (this.editingMessageObject != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isInVideoMode() {
      boolean var1;
      if (this.videoSendButton.getTag() != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isKeyboardVisible() {
      return this.keyboardVisible;
   }

   public boolean isMessageWebPageSearchEnabled() {
      return this.messageWebPageSearch;
   }

   public boolean isPopupShowing() {
      boolean var2;
      if (!this.emojiViewVisible) {
         BotKeyboardView var1 = this.botKeyboardView;
         if (var1 == null || var1.getVisibility() != 0) {
            var2 = false;
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   public boolean isPopupView(View var1) {
      boolean var2;
      if (var1 != this.botKeyboardView && var1 != this.emojiView) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public boolean isRecordCircle(View var1) {
      boolean var2;
      if (var1 == this.recordCircle) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isRecordLocked() {
      boolean var1;
      if (this.recordingAudioVideo && this.recordCircle.isSendButtonVisible()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isRecordingAudioVideo() {
      return this.recordingAudioVideo;
   }

   public boolean isRtlText() {
      boolean var1 = false;

      int var2;
      try {
         var2 = this.messageEditText.getLayout().getParagraphDirection(0);
      } catch (Throwable var4) {
         return var1;
      }

      if (var2 == -1) {
         var1 = true;
      }

      return var1;
   }

   public boolean isSendButtonVisible() {
      boolean var1;
      if (this.sendButton.getVisibility() == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isStickersExpanded() {
      return this.stickersExpanded;
   }

   public boolean isTopViewVisible() {
      View var1 = this.topView;
      boolean var2;
      if (var1 != null && var1.getVisibility() == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   // $FF: synthetic method
   public void lambda$checkStickresExpandHeight$18$ChatActivityEnterView(ValueAnimator var1) {
      this.sizeNotifierLayout.invalidate();
   }

   // $FF: synthetic method
   public void lambda$checkStickresExpandHeight$19$ChatActivityEnterView(ValueAnimator var1) {
      this.sizeNotifierLayout.invalidate();
   }

   // $FF: synthetic method
   public void lambda$didPressedBotButton$16$ChatActivityEnterView(MessageObject var1, TLRPC.KeyboardButton var2, DialogInterface var3, int var4) {
      if (VERSION.SDK_INT >= 23 && this.parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
         this.parentActivity.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
         this.pendingMessageObject = var1;
         this.pendingLocationButton = var2;
      } else {
         SendMessagesHelper.getInstance(this.currentAccount).sendCurrentLocation(var1, var2);
      }
   }

   // $FF: synthetic method
   public void lambda$didPressedBotButton$17$ChatActivityEnterView(MessageObject var1, TLRPC.KeyboardButton var2, DialogsActivity var3, ArrayList var4, CharSequence var5, boolean var6) {
      TLRPC.Message var11 = var1.messageOwner;
      int var7 = var11.from_id;
      int var8 = var11.via_bot_id;
      if (var8 != 0) {
         var7 = var8;
      }

      TLRPC.User var12 = MessagesController.getInstance(this.currentAccount).getUser(var7);
      if (var12 == null) {
         var3.finishFragment();
      } else {
         long var9 = (Long)var4.get(0);
         DataQuery var16 = DataQuery.getInstance(this.currentAccount);
         StringBuilder var15 = new StringBuilder();
         var15.append("@");
         var15.append(var12.username);
         var15.append(" ");
         var15.append(var2.query);
         var16.saveDraft(var9, var15.toString(), (ArrayList)null, (TLRPC.Message)null, true);
         if (var9 != this.dialog_id) {
            var7 = (int)var9;
            if (var7 != 0) {
               Bundle var13 = new Bundle();
               if (var7 > 0) {
                  var13.putInt("user_id", var7);
               } else if (var7 < 0) {
                  var13.putInt("chat_id", -var7);
               }

               if (!MessagesController.getInstance(this.currentAccount).checkCanOpenChat(var13, var3)) {
                  return;
               }

               ChatActivity var14 = new ChatActivity(var13);
               if (this.parentFragment.presentFragment(var14, true)) {
                  if (!AndroidUtilities.isTablet()) {
                     this.parentFragment.removeSelfFromStack();
                  }
               } else {
                  var3.finishFragment();
               }
            } else {
               var3.finishFragment();
            }
         } else {
            var3.finishFragment();
         }

      }
   }

   // $FF: synthetic method
   public void lambda$doneEditingMessage$13$ChatActivityEnterView() {
      this.editingMessageReqId = 0;
      this.setEditingMessageObject((MessageObject)null, false);
   }

   // $FF: synthetic method
   public void lambda$new$0$ChatActivityEnterView(View var1) {
      if (this.isPopupShowing() && this.currentPopupContentType == 0) {
         if (this.searchingType != 0) {
            this.searchingType = 0;
            this.emojiView.closeSearch(false);
            this.messageEditText.requestFocus();
         }

         this.openKeyboardInternal();
      } else {
         boolean var2 = true;
         this.showPopup(1, 0);
         EmojiView var3 = this.emojiView;
         if (this.messageEditText.length() <= 0) {
            var2 = false;
         }

         var3.onOpen(var2);
      }

   }

   // $FF: synthetic method
   public void lambda$new$1$ChatActivityEnterView() {
      ChatActivityEnterView.ChatActivityEnterViewDelegate var1 = this.delegate;
      if (var1 != null) {
         var1.onTextSpansChanged(this.messageEditText.getText());
      }

   }

   // $FF: synthetic method
   public void lambda$new$10$ChatActivityEnterView(View var1) {
      this.sendMessage();
   }

   // $FF: synthetic method
   public void lambda$new$11$ChatActivityEnterView(View var1) {
      if (this.expandStickersButton.getVisibility() == 0 && this.expandStickersButton.getAlpha() == 1.0F) {
         if (this.stickersExpanded) {
            if (this.searchingType != 0) {
               this.searchingType = 0;
               this.emojiView.closeSearch(true);
               this.emojiView.hideSearchKeyboard();
               if (this.emojiTabOpen) {
                  this.checkSendButton(true);
               }
            } else if (!this.stickersDragging) {
               EmojiView var2 = this.emojiView;
               if (var2 != null) {
                  var2.showSearchField(false);
               }
            }
         } else if (!this.stickersDragging) {
            this.emojiView.showSearchField(true);
         }

         if (!this.stickersDragging) {
            this.setStickersExpanded(this.stickersExpanded ^ true, true, false);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$new$12$ChatActivityEnterView(View var1) {
      this.doneEditingMessage();
   }

   // $FF: synthetic method
   public void lambda$new$2$ChatActivityEnterView(View var1) {
      if (this.searchingType != 0) {
         this.searchingType = 0;
         this.emojiView.closeSearch(false);
         this.messageEditText.requestFocus();
      }

      if (this.botReplyMarkup != null) {
         label30: {
            if (this.isPopupShowing()) {
               int var2 = this.currentPopupContentType;
               if (var2 == 1) {
                  if (var2 == 1 && this.botButtonsMessageObject != null) {
                     Editor var6 = MessagesController.getMainSettings(this.currentAccount).edit();
                     StringBuilder var5 = new StringBuilder();
                     var5.append("hidekeyboard_");
                     var5.append(this.dialog_id);
                     var6.putInt(var5.toString(), this.botButtonsMessageObject.getId()).commit();
                  }

                  this.openKeyboardInternal();
                  break label30;
               }
            }

            this.showPopup(1, 1);
            Editor var4 = MessagesController.getMainSettings(this.currentAccount).edit();
            StringBuilder var3 = new StringBuilder();
            var3.append("hidekeyboard_");
            var3.append(this.dialog_id);
            var4.remove(var3.toString()).commit();
         }
      } else if (this.hasBotCommands) {
         this.setFieldText("/");
         this.messageEditText.requestFocus();
         this.openKeyboard();
      }

      if (this.stickersExpanded) {
         this.setStickersExpanded(false, false, false);
      }

   }

   // $FF: synthetic method
   public void lambda$new$3$ChatActivityEnterView(View var1) {
      this.delegate.didPressedAttachButton();
   }

   // $FF: synthetic method
   public void lambda$new$4$ChatActivityEnterView(View var1) {
      if (this.videoToSendMessageObject != null) {
         CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
         this.delegate.needStartRecordVideo(2);
      } else {
         MessageObject var2 = MediaController.getInstance().getPlayingMessageObject();
         if (var2 != null && var2 == this.audioToSendMessageObject) {
            MediaController.getInstance().cleanupPlayer(true, true);
         }
      }

      String var3 = this.audioToSendPath;
      if (var3 != null) {
         (new File(var3)).delete();
      }

      this.hideRecordedAudioPanel();
      this.checkSendButton(true);
   }

   // $FF: synthetic method
   public void lambda$new$5$ChatActivityEnterView(View var1) {
      if (this.audioToSend != null) {
         if (MediaController.getInstance().isPlayingMessage(this.audioToSendMessageObject) && !MediaController.getInstance().isMessagePaused()) {
            MediaController.getInstance().pauseMessage(this.audioToSendMessageObject);
            this.recordedAudioPlayButton.setImageDrawable(this.playDrawable);
            this.recordedAudioPlayButton.setContentDescription(LocaleController.getString("AccActionPlay", 2131558409));
         } else {
            this.recordedAudioPlayButton.setImageDrawable(this.pauseDrawable);
            MediaController.getInstance().playMessage(this.audioToSendMessageObject);
            this.recordedAudioPlayButton.setContentDescription(LocaleController.getString("AccActionPause", 2131558408));
         }

      }
   }

   // $FF: synthetic method
   public void lambda$new$7$ChatActivityEnterView(View var1) {
      if (this.hasRecordVideo && this.videoSendButton.getTag() != null) {
         CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
         this.delegate.needStartRecordVideo(2);
      } else {
         this.delegate.needStartRecordAudio(0);
         MediaController.getInstance().stopRecording(0);
      }

      this.recordingAudioVideo = false;
      this.updateRecordIntefrace();
   }

   // $FF: synthetic method
   public boolean lambda$new$8$ChatActivityEnterView(View var1, MotionEvent var2) {
      int var3 = var2.getAction();
      boolean var4 = false;
      if (var3 == 0) {
         if (this.recordCircle.isSendButtonVisible()) {
            if (!this.hasRecordVideo || this.calledRecordRunnable) {
               this.startedDraggingX = -1.0F;
               if (this.hasRecordVideo && this.videoSendButton.getTag() != null) {
                  this.delegate.needStartRecordVideo(1);
               } else {
                  this.delegate.needStartRecordAudio(0);
                  MediaController.getInstance().stopRecording(1);
               }

               this.recordingAudioVideo = false;
               this.updateRecordIntefrace();
            }

            return false;
         }

         ChatActivity var13 = this.parentFragment;
         if (var13 != null) {
            TLRPC.Chat var14 = var13.getCurrentChat();
            if (var14 != null && !ChatObject.canSendMedia(var14)) {
               this.delegate.needShowMediaBanHint();
               return false;
            }
         }

         if (this.hasRecordVideo) {
            this.calledRecordRunnable = false;
            this.recordAudioVideoRunnableStarted = true;
            AndroidUtilities.runOnUIThread(this.recordAudioVideoRunnable, 150L);
         } else {
            this.recordAudioVideoRunnable.run();
         }
      } else if (var2.getAction() != 1 && var2.getAction() != 3) {
         if (var2.getAction() == 2 && this.recordingAudioVideo) {
            float var6 = var2.getX();
            float var7 = var2.getY();
            if (this.recordCircle.isSendButtonVisible()) {
               return false;
            }

            if (this.recordCircle.setLockTranslation(var7) == 2) {
               AnimatorSet var10 = new AnimatorSet();
               ChatActivityEnterView.RecordCircle var11 = this.recordCircle;
               var10.playTogether(new Animator[]{ObjectAnimator.ofFloat(var11, "lockAnimatedTranslation", new float[]{var11.startTranslation}), ObjectAnimator.ofFloat(this.slideText, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.slideText, View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(20.0F)}), ObjectAnimator.ofFloat(this.recordSendText, View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofFloat(this.recordSendText, View.TRANSLATION_Y, new float[]{(float)(-AndroidUtilities.dp(20.0F)), 0.0F})});
               var10.setInterpolator(new DecelerateInterpolator());
               var10.setDuration(150L);
               var10.start();
               return false;
            }

            if (var6 < -this.distCanMove) {
               if (this.hasRecordVideo && this.videoSendButton.getTag() != null) {
                  CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
                  this.delegate.needStartRecordVideo(2);
               } else {
                  this.delegate.needStartRecordAudio(0);
                  MediaController.getInstance().stopRecording(0);
               }

               this.recordingAudioVideo = false;
               this.updateRecordIntefrace();
            }

            float var8 = var6 + this.audioVideoButtonContainer.getX();
            LayoutParams var12 = (LayoutParams)this.slideText.getLayoutParams();
            var7 = this.startedDraggingX;
            if (var7 != -1.0F) {
               var7 = var8 - var7;
               var12.leftMargin = AndroidUtilities.dp(30.0F) + (int)var7;
               this.slideText.setLayoutParams(var12);
               var6 = var7 / this.distCanMove + 1.0F;
               if (var6 > 1.0F) {
                  var7 = 1.0F;
               } else {
                  var7 = var6;
                  if (var6 < 0.0F) {
                     var7 = 0.0F;
                  }
               }

               this.slideText.setAlpha(var7);
            }

            if (var8 <= this.slideText.getX() + (float)this.slideText.getWidth() + (float)AndroidUtilities.dp(30.0F) && this.startedDraggingX == -1.0F) {
               this.startedDraggingX = var8;
               this.distCanMove = (float)(this.recordPanel.getMeasuredWidth() - this.slideText.getMeasuredWidth() - AndroidUtilities.dp(48.0F)) / 2.0F;
               var7 = this.distCanMove;
               if (var7 <= 0.0F) {
                  this.distCanMove = (float)AndroidUtilities.dp(80.0F);
               } else if (var7 > (float)AndroidUtilities.dp(80.0F)) {
                  this.distCanMove = (float)AndroidUtilities.dp(80.0F);
               }
            }

            if (var12.leftMargin > AndroidUtilities.dp(30.0F)) {
               var12.leftMargin = AndroidUtilities.dp(30.0F);
               this.slideText.setLayoutParams(var12);
               this.slideText.setAlpha(1.0F);
               this.startedDraggingX = -1.0F;
            }
         }
      } else {
         if (this.recordCircle.isSendButtonVisible() || this.recordedAudioPanel.getVisibility() == 0) {
            return false;
         }

         if (this.recordAudioVideoRunnableStarted) {
            AndroidUtilities.cancelRunOnUIThread(this.recordAudioVideoRunnable);
            ChatActivityEnterView.ChatActivityEnterViewDelegate var5 = this.delegate;
            boolean var9;
            if (this.videoSendButton.getTag() == null) {
               var9 = true;
            } else {
               var9 = false;
            }

            var5.onSwitchRecordMode(var9);
            var9 = var4;
            if (this.videoSendButton.getTag() == null) {
               var9 = true;
            }

            this.setRecordVideoButtonVisible(var9, true);
            this.performHapticFeedback(3);
            this.sendAccessibilityEvent(1);
         } else if (!this.hasRecordVideo || this.calledRecordRunnable) {
            this.startedDraggingX = -1.0F;
            if (this.hasRecordVideo && this.videoSendButton.getTag() != null) {
               CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
               this.delegate.needStartRecordVideo(1);
            } else {
               this.delegate.needStartRecordAudio(0);
               MediaController.getInstance().stopRecording(1);
            }

            this.recordingAudioVideo = false;
            this.updateRecordIntefrace();
         }
      }

      var1.onTouchEvent(var2);
      return true;
   }

   // $FF: synthetic method
   public void lambda$new$9$ChatActivityEnterView(View var1) {
      String var3 = this.messageEditText.getText().toString();
      int var2 = var3.indexOf(32);
      if (var2 != -1 && var2 != var3.length() - 1) {
         this.setFieldText(var3.substring(0, var2 + 1));
      } else {
         this.setFieldText("");
      }

   }

   // $FF: synthetic method
   public void lambda$setButtons$15$ChatActivityEnterView(TLRPC.KeyboardButton var1) {
      MessageObject var2 = this.replyingMessageObject;
      if (var2 == null) {
         if ((int)this.dialog_id < 0) {
            var2 = this.botButtonsMessageObject;
         } else {
            var2 = null;
         }
      }

      MessageObject var3 = this.replyingMessageObject;
      if (var3 == null) {
         var3 = this.botButtonsMessageObject;
      }

      this.didPressedBotButton(var1, var2, var3);
      if (this.replyingMessageObject != null) {
         this.openKeyboardInternal();
         this.setButtons(this.botMessageObject, false);
      } else if (this.botButtonsMessageObject.messageOwner.reply_markup.single_use) {
         this.openKeyboardInternal();
         Editor var6 = MessagesController.getMainSettings(this.currentAccount).edit();
         StringBuilder var4 = new StringBuilder();
         var4.append("answered_");
         var4.append(this.dialog_id);
         var6.putInt(var4.toString(), this.botButtonsMessageObject.getId()).commit();
      }

      ChatActivityEnterView.ChatActivityEnterViewDelegate var5 = this.delegate;
      if (var5 != null) {
         var5.onMessageSend((CharSequence)null);
      }

   }

   // $FF: synthetic method
   public void lambda$setFieldFocused$14$ChatActivityEnterView() {
      boolean var1 = AndroidUtilities.isTablet();
      boolean var2 = true;
      boolean var3 = var2;
      if (var1) {
         Activity var4 = this.parentActivity;
         var3 = var2;
         if (var4 instanceof LaunchActivity) {
            LaunchActivity var6 = (LaunchActivity)var4;
            var3 = var2;
            if (var6 != null) {
               ActionBarLayout var7 = var6.getLayersActionBarLayout();
               var3 = var2;
               if (var7 != null) {
                  if (var7.getVisibility() != 0) {
                     var3 = var2;
                  } else {
                     var3 = false;
                  }
               }
            }
         }
      }

      if (var3) {
         EditTextCaption var8 = this.messageEditText;
         if (var8 != null) {
            try {
               var8.requestFocus();
            } catch (Exception var5) {
               FileLog.e((Throwable)var5);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$setStickersExpanded$20$ChatActivityEnterView(int var1, ValueAnimator var2) {
      this.stickersExpansionProgress = this.getTranslationY() / (float)(-(this.stickersExpandedHeight - var1));
      this.sizeNotifierLayout.invalidate();
   }

   // $FF: synthetic method
   public void lambda$setStickersExpanded$21$ChatActivityEnterView(int var1, ValueAnimator var2) {
      this.stickersExpansionProgress = this.getTranslationY() / (float)(-(this.stickersExpandedHeight - var1));
      this.sizeNotifierLayout.invalidate();
   }

   public void onDestroy() {
      this.destroyed = true;
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStarted);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStartError);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStopped);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordProgressChanged);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioDidSent);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioRouteChanged);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
      EmojiView var1 = this.emojiView;
      if (var1 != null) {
         var1.onDestroy();
      }

      WakeLock var3 = this.wakeLock;
      if (var3 != null) {
         try {
            var3.release();
            this.wakeLock = null;
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }
      }

      SizeNotifierFrameLayout var4 = this.sizeNotifierLayout;
      if (var4 != null) {
         var4.setDelegate((SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate)null);
      }

   }

   protected void onDraw(Canvas var1) {
      View var2 = this.topView;
      int var3;
      if (var2 != null && var2.getVisibility() == 0) {
         var3 = (int)this.topView.getTranslationY();
      } else {
         var3 = 0;
      }

      int var4 = Theme.chat_composeShadowDrawable.getIntrinsicHeight() + var3;
      Theme.chat_composeShadowDrawable.setBounds(0, var3, this.getMeasuredWidth(), var4);
      Theme.chat_composeShadowDrawable.draw(var1);
      var1.drawRect(0.0F, (float)var4, (float)this.getWidth(), (float)this.getHeight(), Theme.chat_composeBackgroundPaint);
   }

   public void onEditTimeExpired() {
      this.doneButtonContainer.setVisibility(8);
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      if (this.recordingAudioVideo) {
         this.getParent().requestDisallowInterceptTouchEvent(true);
      }

      return super.onInterceptTouchEvent(var1);
   }

   public void onPause() {
      this.isPaused = true;
      this.closeKeyboard();
   }

   public void onRequestPermissionsResultFragment(int var1, String[] var2, int[] var3) {
      if (var1 == 2 && this.pendingLocationButton != null) {
         if (var3.length > 0 && var3[0] == 0) {
            SendMessagesHelper.getInstance(this.currentAccount).sendCurrentLocation(this.pendingMessageObject, this.pendingLocationButton);
         }

         this.pendingLocationButton = null;
         this.pendingMessageObject = null;
      }

   }

   public void onResume() {
      this.isPaused = false;
      if (this.showKeyboardOnResume) {
         this.showKeyboardOnResume = false;
         if (this.searchingType == 0) {
            this.messageEditText.requestFocus();
         }

         AndroidUtilities.showKeyboard(this.messageEditText);
         if (!AndroidUtilities.usingHardwareInput && !this.keyboardVisible && !AndroidUtilities.isInMultiwindow) {
            this.waitingForKeyboardOpen = true;
            AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
            AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100L);
         }
      }

   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if (var1 != var3 && this.stickersExpanded) {
         this.searchingType = 0;
         this.emojiView.closeSearch(false);
         this.setStickersExpanded(false, false, false);
      }

      this.videoTimelineView.clearFrames();
   }

   public void onSizeChanged(int var1, boolean var2) {
      int var3 = this.searchingType;
      boolean var4 = true;
      boolean var5 = true;
      if (var3 != 0) {
         this.lastSizeChangeValue1 = var1;
         this.lastSizeChangeValue2 = var2;
         if (var1 > 0) {
            var2 = var5;
         } else {
            var2 = false;
         }

         this.keyboardVisible = var2;
      } else {
         if (var1 > AndroidUtilities.dp(50.0F) && this.keyboardVisible && !AndroidUtilities.isInMultiwindow) {
            if (var2) {
               this.keyboardHeightLand = var1;
               MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height_land3", this.keyboardHeightLand).commit();
            } else {
               this.keyboardHeight = var1;
               MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height", this.keyboardHeight).commit();
            }
         }

         if (this.isPopupShowing()) {
            if (var2) {
               var3 = this.keyboardHeightLand;
            } else {
               var3 = this.keyboardHeight;
            }

            int var6 = var3;
            if (this.currentPopupContentType == 1) {
               var6 = var3;
               if (!this.botKeyboardView.isFullSize()) {
                  var6 = Math.min(this.botKeyboardView.getKeyboardHeight(), var3);
               }
            }

            Object var7 = null;
            var3 = this.currentPopupContentType;
            if (var3 == 0) {
               var7 = this.emojiView;
            } else if (var3 == 1) {
               var7 = this.botKeyboardView;
            }

            BotKeyboardView var8 = this.botKeyboardView;
            if (var8 != null) {
               var8.setPanelHeight(var6);
            }

            LayoutParams var9 = (LayoutParams)((View)var7).getLayoutParams();
            if (!this.closeAnimationInProgress && (var9.width != AndroidUtilities.displaySize.x || var9.height != var6) && !this.stickersExpanded) {
               var9.width = AndroidUtilities.displaySize.x;
               var9.height = var6;
               ((View)var7).setLayoutParams(var9);
               if (this.sizeNotifierLayout != null) {
                  this.emojiPadding = var9.height;
                  this.sizeNotifierLayout.requestLayout();
                  this.onWindowSizeChanged();
               }
            }
         }

         if (this.lastSizeChangeValue1 == var1 && this.lastSizeChangeValue2 == var2) {
            this.onWindowSizeChanged();
         } else {
            this.lastSizeChangeValue1 = var1;
            this.lastSizeChangeValue2 = var2;
            var5 = this.keyboardVisible;
            if (var1 > 0) {
               var2 = var4;
            } else {
               var2 = false;
            }

            this.keyboardVisible = var2;
            if (this.keyboardVisible && this.isPopupShowing()) {
               this.showPopup(0, this.currentPopupContentType);
            }

            if (this.emojiPadding != 0) {
               var2 = this.keyboardVisible;
               if (!var2 && var2 != var5 && !this.isPopupShowing()) {
                  this.emojiPadding = 0;
                  this.sizeNotifierLayout.requestLayout();
               }
            }

            if (this.keyboardVisible && this.waitingForKeyboardOpen) {
               this.waitingForKeyboardOpen = false;
               AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
            }

            this.onWindowSizeChanged();
         }
      }
   }

   public void onStickerSelected(TLRPC.Document var1, Object var2, boolean var3) {
      if (this.searchingType != 0) {
         this.searchingType = 0;
         this.emojiView.closeSearch(true);
         this.emojiView.hideSearchKeyboard();
      }

      this.setStickersExpanded(false, true, false);
      SendMessagesHelper.getInstance(this.currentAccount).sendSticker(var1, this.dialog_id, this.replyingMessageObject, var2);
      ChatActivityEnterView.ChatActivityEnterViewDelegate var4 = this.delegate;
      if (var4 != null) {
         var4.onMessageSend((CharSequence)null);
      }

      if (var3) {
         this.setFieldText("");
      }

      DataQuery.getInstance(this.currentAccount).addRecentSticker(0, var2, var1, (int)(System.currentTimeMillis() / 1000L), false);
   }

   public void openKeyboard() {
      AndroidUtilities.showKeyboard(this.messageEditText);
   }

   public boolean processSendingText(CharSequence var1) {
      CharSequence var2 = AndroidUtilities.getTrimmedString(var1);
      int var3 = MessagesController.getInstance(this.currentAccount).maxMessageLength;
      if (var2.length() == 0) {
         return false;
      } else {
         int var4 = (int)Math.ceil((double)((float)var2.length() / (float)var3));
         int var5 = 0;

         while(true) {
            int var6 = var5;
            if (var5 >= var4) {
               return true;
            }

            CharSequence[] var8 = new CharSequence[1];
            ++var5;
            var8[0] = var2.subSequence(var6 * var3, Math.min(var5 * var3, var2.length()));
            ArrayList var7 = DataQuery.getInstance(this.currentAccount).getEntities(var8);
            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(var8[0].toString(), this.dialog_id, this.replyingMessageObject, this.messageWebPage, this.messageWebPageSearch, var7, (TLRPC.ReplyMarkup)null, (HashMap)null);
         }
      }
   }

   public void replaceWithText(int var1, int var2, CharSequence var3, boolean var4) {
      Exception var10000;
      label30: {
         boolean var10001;
         SpannableStringBuilder var5;
         try {
            var5 = new SpannableStringBuilder(this.messageEditText.getText());
            var5.replace(var1, var2 + var1, var3);
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
            break label30;
         }

         if (var4) {
            try {
               Emoji.replaceEmoji(var5, this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label30;
            }
         }

         try {
            this.messageEditText.setText(var5);
            this.messageEditText.setSelection(var1 + var3.length());
            return;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var9 = var10000;
      FileLog.e((Throwable)var9);
   }

   public void setAllowStickersAndGifs(boolean var1, boolean var2) {
      if ((this.allowStickers != var1 || this.allowGifs != var2) && this.emojiView != null) {
         if (this.emojiViewVisible) {
            this.hidePopup(false);
         }

         this.sizeNotifierLayout.removeView(this.emojiView);
         this.emojiView = null;
      }

      this.allowStickers = var1;
      this.allowGifs = var2;
      this.setEmojiButtonImage(false, this.isPaused ^ true);
   }

   public void setBotsCount(int var1, boolean var2) {
      this.botCount = var1;
      if (this.hasBotCommands != var2) {
         this.hasBotCommands = var2;
         this.updateBotButton();
      }

   }

   public void setButtons(MessageObject var1) {
      this.setButtons(var1, true);
   }

   public void setButtons(MessageObject var1, boolean var2) {
      MessageObject var3 = this.replyingMessageObject;
      if (var3 != null && var3 == this.botButtonsMessageObject && var3 != var1) {
         this.botMessageObject = var1;
      } else {
         if (this.botButton != null) {
            var3 = this.botButtonsMessageObject;
            if ((var3 == null || var3 != var1) && (this.botButtonsMessageObject != null || var1 != null)) {
               if (this.botKeyboardView == null) {
                  this.botKeyboardView = new BotKeyboardView(this.parentActivity);
                  this.botKeyboardView.setVisibility(8);
                  this.botKeyboardView.setDelegate(new _$$Lambda$ChatActivityEnterView$b_Y8EmOjKWjLQcXw9E16k20A4eo(this));
                  this.sizeNotifierLayout.addView(this.botKeyboardView);
               }

               TLRPC.TL_replyKeyboardMarkup var8;
               label72: {
                  this.botButtonsMessageObject = var1;
                  if (var1 != null) {
                     TLRPC.ReplyMarkup var7 = var1.messageOwner.reply_markup;
                     if (var7 instanceof TLRPC.TL_replyKeyboardMarkup) {
                        var8 = (TLRPC.TL_replyKeyboardMarkup)var7;
                        break label72;
                     }
                  }

                  var8 = null;
               }

               this.botReplyMarkup = var8;
               BotKeyboardView var10 = this.botKeyboardView;
               android.graphics.Point var4 = AndroidUtilities.displaySize;
               int var5;
               if (var4.x > var4.y) {
                  var5 = this.keyboardHeightLand;
               } else {
                  var5 = this.keyboardHeight;
               }

               var10.setPanelHeight(var5);
               this.botKeyboardView.setButtons(this.botReplyMarkup);
               var8 = this.botReplyMarkup;
               boolean var6 = false;
               if (var8 != null) {
                  SharedPreferences var11 = MessagesController.getMainSettings(this.currentAccount);
                  StringBuilder var9 = new StringBuilder();
                  var9.append("hidekeyboard_");
                  var9.append(this.dialog_id);
                  boolean var12;
                  if (var11.getInt(var9.toString(), 0) == var1.getId()) {
                     var12 = true;
                  } else {
                     var12 = false;
                  }

                  label62: {
                     if (this.botButtonsMessageObject != this.replyingMessageObject && this.botReplyMarkup.single_use) {
                        var9 = new StringBuilder();
                        var9.append("answered_");
                        var9.append(this.dialog_id);
                        if (var11.getInt(var9.toString(), 0) == var1.getId()) {
                           break label62;
                        }
                     }

                     var6 = true;
                  }

                  if (var6 && !var12 && this.messageEditText.length() == 0 && !this.isPopupShowing()) {
                     this.showPopup(1, 1);
                  }
               } else if (this.isPopupShowing() && this.currentPopupContentType == 1) {
                  if (var2) {
                     this.openKeyboardInternal();
                  } else {
                     this.showPopup(0, 1);
                  }
               }

               this.updateBotButton();
            }
         }

      }
   }

   public void setCaption(String var1) {
      EditTextCaption var2 = this.messageEditText;
      if (var2 != null) {
         var2.setCaption(var1);
         this.checkSendButton(true);
      }

   }

   public void setChatInfo(TLRPC.ChatFull var1) {
      this.info = var1;
      EmojiView var2 = this.emojiView;
      if (var2 != null) {
         var2.setChatInfo(this.info);
      }

   }

   public void setCommand(MessageObject var1, String var2, boolean var3, boolean var4) {
      if (var2 != null && this.getVisibility() == 0) {
         String var5 = null;
         Object var6 = null;
         TLRPC.User var7;
         if (var3) {
            var5 = this.messageEditText.getText().toString();
            var7 = (TLRPC.User)var6;
            if (var1 != null) {
               var7 = (TLRPC.User)var6;
               if ((int)this.dialog_id < 0) {
                  var7 = MessagesController.getInstance(this.currentAccount).getUser(var1.messageOwner.from_id);
               }
            }

            StringBuilder var8;
            String var9;
            if ((this.botCount != 1 || var4) && var7 != null && var7.bot && !var2.contains("@")) {
               var8 = new StringBuilder();
               var8.append(String.format(Locale.US, "%s@%s", var2, var7.username));
               var8.append(" ");
               var8.append(var5.replaceFirst("^/[a-zA-Z@\\d_]{1,255}(\\s|$)", ""));
               var9 = var8.toString();
            } else {
               var8 = new StringBuilder();
               var8.append(var2);
               var8.append(" ");
               var8.append(var5.replaceFirst("^/[a-zA-Z@\\d_]{1,255}(\\s|$)", ""));
               var9 = var8.toString();
            }

            this.ignoreTextChange = true;
            this.messageEditText.setText(var9);
            EditTextCaption var10 = this.messageEditText;
            var10.setSelection(var10.getText().length());
            this.ignoreTextChange = false;
            ChatActivityEnterView.ChatActivityEnterViewDelegate var11 = this.delegate;
            if (var11 != null) {
               var11.onTextChanged(this.messageEditText.getText(), true);
            }

            if (!this.keyboardVisible && this.currentPopupContentType == -1) {
               this.openKeyboard();
            }
         } else {
            var7 = var5;
            if (var1 != null) {
               var7 = var5;
               if ((int)this.dialog_id < 0) {
                  var7 = MessagesController.getInstance(this.currentAccount).getUser(var1.messageOwner.from_id);
               }
            }

            if ((this.botCount != 1 || var4) && var7 != null && var7.bot && !var2.contains("@")) {
               SendMessagesHelper.getInstance(this.currentAccount).sendMessage(String.format(Locale.US, "%s@%s", var2, var7.username), this.dialog_id, this.replyingMessageObject, (TLRPC.WebPage)null, false, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
            } else {
               SendMessagesHelper.getInstance(this.currentAccount).sendMessage(var2, this.dialog_id, this.replyingMessageObject, (TLRPC.WebPage)null, false, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
            }
         }
      }

   }

   public void setDelegate(ChatActivityEnterView.ChatActivityEnterViewDelegate var1) {
      this.delegate = var1;
   }

   public void setDialogId(long var1, int var3) {
      this.dialog_id = var1;
      int var4 = this.currentAccount;
      if (var4 != var3) {
         NotificationCenter.getInstance(var4).removeObserver(this, NotificationCenter.recordStarted);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStartError);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStopped);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordProgressChanged);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioDidSent);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioRouteChanged);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
         this.currentAccount = var3;
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStarted);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStartError);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStopped);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordProgressChanged);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.audioDidSent);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.audioRouteChanged);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
      }

      if ((int)this.dialog_id < 0) {
         boolean var8;
         byte var11;
         label66: {
            TLRPC.Chat var5 = MessagesController.getInstance(this.currentAccount).getChat(-((int)this.dialog_id));
            SharedPreferences var6 = MessagesController.getNotificationsSettings(this.currentAccount);
            StringBuilder var7 = new StringBuilder();
            var7.append("silent_");
            var7.append(this.dialog_id);
            this.silent = var6.getBoolean(var7.toString(), false);
            var8 = ChatObject.isChannel(var5);
            var11 = 1;
            if (var8) {
               label64: {
                  if (!var5.creator) {
                     TLRPC.TL_chatAdminRights var14 = var5.admin_rights;
                     if (var14 == null || !var14.post_messages) {
                        break label64;
                     }
                  }

                  if (!var5.megagroup) {
                     var8 = true;
                     break label66;
                  }
               }
            }

            var8 = false;
         }

         this.canWriteToChannel = var8;
         ImageView var12 = this.notifyButton;
         byte var10;
         LinearLayout var13;
         if (var12 != null) {
            if (this.canWriteToChannel) {
               var10 = 0;
            } else {
               var10 = 8;
            }

            var12.setVisibility(var10);
            var12 = this.notifyButton;
            if (this.silent) {
               var3 = 2131165489;
            } else {
               var3 = 2131165490;
            }

            float var9;
            label78: {
               var12.setImageResource(var3);
               var13 = this.attachLayout;
               ImageView var15 = this.botButton;
               if (var15 == null || var15.getVisibility() == 8) {
                  var15 = this.notifyButton;
                  if (var15 == null || var15.getVisibility() == 8) {
                     var9 = 48.0F;
                     break label78;
                  }
               }

               var9 = 96.0F;
            }

            var13.setPivotX((float)AndroidUtilities.dp(var9));
         }

         var13 = this.attachLayout;
         if (var13 != null) {
            if (var13.getVisibility() == 0) {
               var10 = var11;
            } else {
               var10 = 0;
            }

            this.updateFieldRight(var10);
         }
      }

      this.checkRoundVideo();
      this.updateFieldHint();
   }

   public void setEditingMessageObject(MessageObject var1, boolean var2) {
      if (this.audioToSend == null && this.videoToSendMessageObject == null && this.editingMessageObject != var1) {
         if (this.editingMessageReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.editingMessageReqId, true);
            this.editingMessageReqId = 0;
         }

         this.editingMessageObject = var1;
         this.editingCaption = var2;
         if (this.editingMessageObject == null) {
            this.doneButtonContainer.setVisibility(8);
            this.messageEditText.setFilters(new InputFilter[0]);
            this.delegate.onMessageEditEnd(false);
            this.audioVideoButtonContainer.setVisibility(0);
            this.attachLayout.setVisibility(0);
            this.sendButtonContainer.setVisibility(0);
            this.attachLayout.setScaleX(1.0F);
            this.attachLayout.setAlpha(1.0F);
            this.sendButton.setScaleX(0.1F);
            this.sendButton.setScaleY(0.1F);
            this.sendButton.setAlpha(0.0F);
            this.cancelBotButton.setScaleX(0.1F);
            this.cancelBotButton.setScaleY(0.1F);
            this.cancelBotButton.setAlpha(0.0F);
            this.audioVideoButtonContainer.setScaleX(1.0F);
            this.audioVideoButtonContainer.setScaleY(1.0F);
            this.audioVideoButtonContainer.setAlpha(1.0F);
            this.sendButton.setVisibility(8);
            this.cancelBotButton.setVisibility(8);
            this.messageEditText.setText("");
            if (this.getVisibility() == 0) {
               this.delegate.onAttachButtonShow();
            }

            this.updateFieldRight(1);
         } else {
            AnimatorSet var21 = this.doneButtonAnimation;
            if (var21 != null) {
               var21.cancel();
               this.doneButtonAnimation = null;
            }

            this.doneButtonContainer.setVisibility(0);
            this.showEditDoneProgress(true, false);
            InputFilter[] var3 = new InputFilter[1];
            CharSequence var22;
            if (var2) {
               var3[0] = new LengthFilter(MessagesController.getInstance(this.currentAccount).maxCaptionLength);
               var22 = this.editingMessageObject.caption;
            } else {
               var3[0] = new LengthFilter(MessagesController.getInstance(this.currentAccount).maxMessageLength);
               var22 = this.editingMessageObject.messageText;
            }

            if (var22 != null) {
               ArrayList var4 = this.editingMessageObject.messageOwner.entities;
               DataQuery.sortEntities(var4);
               SpannableStringBuilder var23 = new SpannableStringBuilder(var22);
               Object[] var5 = var23.getSpans(0, var23.length(), Object.class);
               int var6;
               if (var5 != null && var5.length > 0) {
                  for(var6 = 0; var6 < var5.length; ++var6) {
                     var23.removeSpan(var5[var6]);
                  }
               }

               if (var4 != null) {
                  int var7 = 0;
                  int var8 = 0;

                  while(true) {
                     label165: {
                        label164: {
                           label163: {
                              Exception var10000;
                              label197: {
                                 boolean var10001;
                                 TLRPC.MessageEntity var26;
                                 try {
                                    if (var7 >= var4.size()) {
                                       break;
                                    }

                                    var26 = (TLRPC.MessageEntity)var4.get(var7);
                                    if (var26.offset + var26.length + var8 > var23.length()) {
                                       break label163;
                                    }
                                 } catch (Exception var15) {
                                    var10000 = var15;
                                    var10001 = false;
                                    break label197;
                                 }

                                 label198: {
                                    try {
                                       if (!(var26 instanceof TLRPC.TL_inputMessageEntityMentionName)) {
                                          break label198;
                                       }

                                       if (var26.offset + var26.length + var8 < var23.length() && var23.charAt(var26.offset + var26.length + var8) == ' ') {
                                          ++var26.length;
                                       }
                                    } catch (Exception var20) {
                                       var10000 = var20;
                                       var10001 = false;
                                       break label197;
                                    }

                                    try {
                                       StringBuilder var10 = new StringBuilder();
                                       var10.append("");
                                       var10.append(((TLRPC.TL_inputMessageEntityMentionName)var26).user_id.user_id);
                                       URLSpanUserMention var9 = new URLSpanUserMention(var10.toString(), 1);
                                       var23.setSpan(var9, var26.offset + var8, var26.offset + var26.length + var8, 33);
                                    } catch (Exception var11) {
                                       var10000 = var11;
                                       var10001 = false;
                                       break label197;
                                    }

                                    var6 = var8;
                                    break label165;
                                 }

                                 label200: {
                                    try {
                                       if (!(var26 instanceof TLRPC.TL_messageEntityMentionName)) {
                                          break label200;
                                       }

                                       if (var26.offset + var26.length + var8 < var23.length() && var23.charAt(var26.offset + var26.length + var8) == ' ') {
                                          ++var26.length;
                                       }
                                    } catch (Exception var19) {
                                       var10000 = var19;
                                       var10001 = false;
                                       break label197;
                                    }

                                    try {
                                       StringBuilder var27 = new StringBuilder();
                                       var27.append("");
                                       var27.append(((TLRPC.TL_messageEntityMentionName)var26).user_id);
                                       URLSpanUserMention var29 = new URLSpanUserMention(var27.toString(), 1);
                                       var23.setSpan(var29, var26.offset + var8, var26.offset + var26.length + var8, 33);
                                    } catch (Exception var12) {
                                       var10000 = var12;
                                       var10001 = false;
                                       break label197;
                                    }

                                    var6 = var8;
                                    break label165;
                                 }

                                 try {
                                    if (var26 instanceof TLRPC.TL_messageEntityCode) {
                                       var23.insert(var26.offset + var26.length + var8, "`");
                                       var23.insert(var26.offset + var8, "`");
                                       break label164;
                                    }
                                 } catch (Exception var14) {
                                    var10000 = var14;
                                    var10001 = false;
                                    break label197;
                                 }

                                 label202: {
                                    try {
                                       if (var26 instanceof TLRPC.TL_messageEntityPre) {
                                          var23.insert(var26.offset + var26.length + var8, "```");
                                          var23.insert(var26.offset + var8, "```");
                                          break label202;
                                       }
                                    } catch (Exception var18) {
                                       var10000 = var18;
                                       var10001 = false;
                                       break label197;
                                    }

                                    TypefaceSpan var28;
                                    label137: {
                                       try {
                                          if (!(var26 instanceof TLRPC.TL_messageEntityBold)) {
                                             break label137;
                                          }

                                          var28 = new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                                          var23.setSpan(var28, var26.offset + var8, var26.offset + var26.length + var8, 33);
                                       } catch (Exception var17) {
                                          var10000 = var17;
                                          var10001 = false;
                                          break label197;
                                       }

                                       var6 = var8;
                                       break label165;
                                    }

                                    label130: {
                                       try {
                                          if (!(var26 instanceof TLRPC.TL_messageEntityItalic)) {
                                             break label130;
                                          }

                                          var28 = new TypefaceSpan(AndroidUtilities.getTypeface("fonts/ritalic.ttf"));
                                          var23.setSpan(var28, var26.offset + var8, var26.offset + var26.length + var8, 33);
                                       } catch (Exception var16) {
                                          var10000 = var16;
                                          var10001 = false;
                                          break label197;
                                       }

                                       var6 = var8;
                                       break label165;
                                    }

                                    var6 = var8;

                                    try {
                                       if (!(var26 instanceof TLRPC.TL_messageEntityTextUrl)) {
                                          break label165;
                                       }

                                       URLSpanReplacement var30 = new URLSpanReplacement(var26.url);
                                       var23.setSpan(var30, var26.offset + var8, var26.offset + var26.length + var8, 33);
                                    } catch (Exception var13) {
                                       var10000 = var13;
                                       var10001 = false;
                                       break label197;
                                    }

                                    var6 = var8;
                                    break label165;
                                 }

                                 var6 = var8 + 6;
                                 break label165;
                              }

                              Exception var25 = var10000;
                              FileLog.e((Throwable)var25);
                              break;
                           }

                           var6 = var8;
                           break label165;
                        }

                        var6 = var8 + 2;
                     }

                     ++var7;
                     var8 = var6;
                  }
               }

               this.setFieldText(Emoji.replaceEmoji(new SpannableStringBuilder(var23), this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0F), false));
            } else {
               this.setFieldText("");
            }

            this.messageEditText.setFilters(var3);
            this.openKeyboard();
            LayoutParams var24 = (LayoutParams)this.messageEditText.getLayoutParams();
            var24.rightMargin = AndroidUtilities.dp(4.0F);
            this.messageEditText.setLayoutParams(var24);
            this.sendButton.setVisibility(8);
            this.cancelBotButton.setVisibility(8);
            this.audioVideoButtonContainer.setVisibility(8);
            this.attachLayout.setVisibility(8);
            this.sendButtonContainer.setVisibility(8);
         }

         this.updateFieldHint();
      }

   }

   public void setFieldFocused() {
      AccessibilityManager var1 = (AccessibilityManager)this.parentActivity.getSystemService("accessibility");
      if (this.messageEditText != null && !var1.isTouchExplorationEnabled()) {
         try {
            this.messageEditText.requestFocus();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }
      }

   }

   public void setFieldFocused(boolean var1) {
      AccessibilityManager var2 = (AccessibilityManager)this.parentActivity.getSystemService("accessibility");
      if (this.messageEditText != null && !var2.isTouchExplorationEnabled()) {
         if (var1) {
            if (this.searchingType == 0 && !this.messageEditText.isFocused()) {
               this.messageEditText.postDelayed(new _$$Lambda$ChatActivityEnterView$w7v9u3TaN_X3t9L7_8faaWf5O3M(this), 600L);
            }
         } else {
            EditTextCaption var3 = this.messageEditText;
            if (var3 != null && var3.isFocused() && !this.keyboardVisible) {
               this.messageEditText.clearFocus();
            }
         }
      }

   }

   public void setFieldText(CharSequence var1) {
      this.setFieldText(var1, true);
   }

   public void setFieldText(CharSequence var1, boolean var2) {
      EditTextCaption var3 = this.messageEditText;
      if (var3 != null) {
         this.ignoreTextChange = var2;
         var3.setText(var1);
         EditTextCaption var4 = this.messageEditText;
         var4.setSelection(var4.getText().length());
         this.ignoreTextChange = false;
         ChatActivityEnterView.ChatActivityEnterViewDelegate var5 = this.delegate;
         if (var5 != null) {
            var5.onTextChanged(this.messageEditText.getText(), true);
         }

      }
   }

   public void setForceShowSendButton(boolean var1, boolean var2) {
      this.forceShowSendButton = var1;
      this.checkSendButton(var2);
   }

   public void setOpenGifsTabFirst() {
      this.createEmojiView();
      DataQuery.getInstance(this.currentAccount).loadRecents(0, true, true, false);
      this.emojiView.switchToGifRecent();
   }

   public void setReplyingMessageObject(MessageObject var1) {
      if (var1 != null) {
         if (this.botMessageObject == null) {
            MessageObject var2 = this.botButtonsMessageObject;
            if (var2 != this.replyingMessageObject) {
               this.botMessageObject = var2;
            }
         }

         this.replyingMessageObject = var1;
         this.setButtons(this.replyingMessageObject, true);
      } else if (var1 == null && this.replyingMessageObject == this.botButtonsMessageObject) {
         this.replyingMessageObject = null;
         this.setButtons(this.botMessageObject, false);
         this.botMessageObject = null;
      } else {
         this.replyingMessageObject = var1;
      }

      MediaController.getInstance().setReplyingMessage(var1);
   }

   public void setSelection(int var1) {
      EditTextCaption var2 = this.messageEditText;
      if (var2 != null) {
         var2.setSelection(var1, var2.length());
      }
   }

   public void setWebPage(TLRPC.WebPage var1, boolean var2) {
      this.messageWebPage = var1;
      this.messageWebPageSearch = var2;
   }

   public void showContextProgress(boolean var1) {
      CloseProgressDrawable2 var2 = this.progressDrawable;
      if (var2 != null) {
         if (var1) {
            var2.startAnimation();
         } else {
            var2.stopAnimation();
         }

      }
   }

   public void showEditDoneProgress(final boolean var1, boolean var2) {
      AnimatorSet var3 = this.doneButtonAnimation;
      if (var3 != null) {
         var3.cancel();
      }

      if (!var2) {
         if (var1) {
            this.doneButtonImage.setScaleX(0.1F);
            this.doneButtonImage.setScaleY(0.1F);
            this.doneButtonImage.setAlpha(0.0F);
            this.doneButtonProgress.setScaleX(1.0F);
            this.doneButtonProgress.setScaleY(1.0F);
            this.doneButtonProgress.setAlpha(1.0F);
            this.doneButtonImage.setVisibility(4);
            this.doneButtonProgress.setVisibility(0);
            this.doneButtonContainer.setEnabled(false);
         } else {
            this.doneButtonProgress.setScaleX(0.1F);
            this.doneButtonProgress.setScaleY(0.1F);
            this.doneButtonProgress.setAlpha(0.0F);
            this.doneButtonImage.setScaleX(1.0F);
            this.doneButtonImage.setScaleY(1.0F);
            this.doneButtonImage.setAlpha(1.0F);
            this.doneButtonImage.setVisibility(0);
            this.doneButtonProgress.setVisibility(4);
            this.doneButtonContainer.setEnabled(true);
         }
      } else {
         this.doneButtonAnimation = new AnimatorSet();
         if (var1) {
            this.doneButtonProgress.setVisibility(0);
            this.doneButtonContainer.setEnabled(false);
            this.doneButtonAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.doneButtonImage, View.SCALE_X, new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneButtonImage, View.SCALE_Y, new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneButtonImage, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.doneButtonProgress, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneButtonProgress, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneButtonProgress, View.ALPHA, new float[]{1.0F})});
         } else {
            this.doneButtonImage.setVisibility(0);
            this.doneButtonContainer.setEnabled(true);
            this.doneButtonAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.doneButtonProgress, View.SCALE_X, new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneButtonProgress, View.SCALE_Y, new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneButtonProgress, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.doneButtonImage, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneButtonImage, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneButtonImage, View.ALPHA, new float[]{1.0F})});
         }

         this.doneButtonAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1x) {
               if (ChatActivityEnterView.this.doneButtonAnimation != null && ChatActivityEnterView.this.doneButtonAnimation.equals(var1x)) {
                  ChatActivityEnterView.this.doneButtonAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1x) {
               if (ChatActivityEnterView.this.doneButtonAnimation != null && ChatActivityEnterView.this.doneButtonAnimation.equals(var1x)) {
                  if (!var1) {
                     ChatActivityEnterView.this.doneButtonProgress.setVisibility(4);
                  } else {
                     ChatActivityEnterView.this.doneButtonImage.setVisibility(4);
                  }
               }

            }
         });
         this.doneButtonAnimation.setDuration(150L);
         this.doneButtonAnimation.start();
      }

   }

   public void showTopView(boolean var1, boolean var2) {
      if (this.topView != null && !this.topViewShowed && this.getVisibility() == 0) {
         this.needShowTopView = true;
         this.topViewShowed = true;
         if (this.allowShowTopView) {
            this.topView.setVisibility(0);
            this.topLineView.setVisibility(0);
            AnimatorSet var3 = this.currentTopViewAnimation;
            if (var3 != null) {
               var3.cancel();
               this.currentTopViewAnimation = null;
            }

            this.resizeForTopView(true);
            if (var1) {
               this.currentTopViewAnimation = new AnimatorSet();
               this.currentTopViewAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.topView, View.TRANSLATION_Y, new float[]{0.0F}), ObjectAnimator.ofFloat(this.topLineView, View.ALPHA, new float[]{1.0F})});
               this.currentTopViewAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     if (ChatActivityEnterView.this.currentTopViewAnimation != null && ChatActivityEnterView.this.currentTopViewAnimation.equals(var1)) {
                        ChatActivityEnterView.this.currentTopViewAnimation = null;
                     }

                  }
               });
               this.currentTopViewAnimation.setDuration(250L);
               this.currentTopViewAnimation.setInterpolator(CubicBezierInterpolator.DEFAULT);
               this.currentTopViewAnimation.start();
            } else {
               this.topView.setTranslationY(0.0F);
               this.topLineView.setAlpha(1.0F);
            }

            if (this.recordedAudioPanel.getVisibility() != 0 && (!this.forceShowSendButton || var2)) {
               this.messageEditText.requestFocus();
               this.openKeyboard();
            }
         }

      } else {
         if (this.recordedAudioPanel.getVisibility() != 0 && (!this.forceShowSendButton || var2)) {
            this.openKeyboard();
         }

      }
   }

   public interface ChatActivityEnterViewDelegate {
      void didPressedAttachButton();

      void needChangeVideoPreviewState(int var1, float var2);

      void needSendTyping();

      void needShowMediaBanHint();

      void needStartRecordAudio(int var1);

      void needStartRecordVideo(int var1);

      void onAttachButtonHidden();

      void onAttachButtonShow();

      void onMessageEditEnd(boolean var1);

      void onMessageSend(CharSequence var1);

      void onPreAudioVideoRecord();

      void onStickersExpandedChange();

      void onStickersTab(boolean var1);

      void onSwitchRecordMode(boolean var1);

      void onTextChanged(CharSequence var1, boolean var2);

      void onTextSelectionChanged(int var1, int var2);

      void onTextSpansChanged(CharSequence var1);

      void onWindowSizeChanged(int var1);
   }

   private class RecordCircle extends View {
      private float amplitude;
      private float animateAmplitudeDiff;
      private float animateToAmplitude;
      private long lastUpdateTime;
      private float lockAnimatedTranslation;
      private boolean pressed;
      private float scale;
      private boolean sendButtonVisible;
      private float startTranslation;
      private ChatActivityEnterView.RecordCircle.VirtualViewHelper virtualViewHelper;

      public RecordCircle(Context var2) {
         super(var2);
         ChatActivityEnterView.this.paint.setColor(Theme.getColor("chat_messagePanelVoiceBackground"));
         ChatActivityEnterView.this.paintRecord.setColor(Theme.getColor("chat_messagePanelVoiceShadow"));
         ChatActivityEnterView.this.lockDrawable = this.getResources().getDrawable(2131165543);
         ChatActivityEnterView.this.lockDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("key_chat_messagePanelVoiceLock"), Mode.MULTIPLY));
         ChatActivityEnterView.this.lockTopDrawable = this.getResources().getDrawable(2131165547);
         ChatActivityEnterView.this.lockTopDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("key_chat_messagePanelVoiceLock"), Mode.MULTIPLY));
         ChatActivityEnterView.this.lockArrowDrawable = this.getResources().getDrawable(2131165541);
         ChatActivityEnterView.this.lockArrowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("key_chat_messagePanelVoiceLock"), Mode.MULTIPLY));
         ChatActivityEnterView.this.lockBackgroundDrawable = this.getResources().getDrawable(2131165545);
         ChatActivityEnterView.this.lockBackgroundDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("key_chat_messagePanelVoiceLockBackground"), Mode.MULTIPLY));
         ChatActivityEnterView.this.lockShadowDrawable = this.getResources().getDrawable(2131165546);
         ChatActivityEnterView.this.lockShadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("key_chat_messagePanelVoiceLockShadow"), Mode.MULTIPLY));
         ChatActivityEnterView.this.micDrawable = this.getResources().getDrawable(2131165488).mutate();
         ChatActivityEnterView.this.micDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelVoicePressed"), Mode.MULTIPLY));
         ChatActivityEnterView.this.cameraDrawable = this.getResources().getDrawable(2131165494).mutate();
         ChatActivityEnterView.this.cameraDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelVoicePressed"), Mode.MULTIPLY));
         ChatActivityEnterView.this.sendDrawable = this.getResources().getDrawable(2131165468).mutate();
         ChatActivityEnterView.this.sendDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelVoicePressed"), Mode.MULTIPLY));
         this.virtualViewHelper = new ChatActivityEnterView.RecordCircle.VirtualViewHelper(this);
         ViewCompat.setAccessibilityDelegate(this, this.virtualViewHelper);
      }

      protected boolean dispatchHoverEvent(MotionEvent var1) {
         boolean var2;
         if (!super.dispatchHoverEvent(var1) && !this.virtualViewHelper.dispatchHoverEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public float getLockAnimatedTranslation() {
         return this.lockAnimatedTranslation;
      }

      public float getScale() {
         return this.scale;
      }

      public boolean isSendButtonVisible() {
         return this.sendButtonVisible;
      }

      protected void onDraw(Canvas var1) {
         int var2 = this.getMeasuredWidth() / 2;
         int var3 = AndroidUtilities.dp(170.0F);
         float var4 = this.lockAnimatedTranslation;
         float var5;
         if (var4 != 10000.0F) {
            var5 = (float)Math.max(0, (int)(this.startTranslation - var4));
            var4 = var5;
            if (var5 > (float)AndroidUtilities.dp(57.0F)) {
               var4 = (float)AndroidUtilities.dp(57.0F);
            }
         } else {
            var4 = 0.0F;
         }

         var3 = (int)((float)var3 - var4);
         var5 = this.scale;
         float var6;
         if (var5 <= 0.5F) {
            var5 /= 0.5F;
            var6 = var5;
         } else {
            if (var5 <= 0.75F) {
               var5 = 1.0F - (var5 - 0.5F) / 0.25F * 0.1F;
            } else {
               var5 = (var5 - 0.75F) / 0.25F * 0.1F + 0.9F;
            }

            var6 = 1.0F;
         }

         long var7 = System.currentTimeMillis();
         long var9 = this.lastUpdateTime;
         float var11 = this.animateToAmplitude;
         float var12 = this.amplitude;
         if (var11 != var12) {
            float var13 = this.animateAmplitudeDiff;
            this.amplitude = var12 + (float)(var7 - var9) * var13;
            if (var13 > 0.0F) {
               if (this.amplitude > var11) {
                  this.amplitude = var11;
               }
            } else if (this.amplitude < var11) {
               this.amplitude = var11;
            }

            this.invalidate();
         }

         this.lastUpdateTime = System.currentTimeMillis();
         if (this.amplitude != 0.0F) {
            var1.drawCircle((float)this.getMeasuredWidth() / 2.0F, (float)var3, ((float)AndroidUtilities.dp(42.0F) + (float)AndroidUtilities.dp(20.0F) * this.amplitude) * this.scale, ChatActivityEnterView.this.paintRecord);
         }

         var1.drawCircle((float)this.getMeasuredWidth() / 2.0F, (float)var3, (float)AndroidUtilities.dp(42.0F) * var5, ChatActivityEnterView.this.paint);
         Drawable var14;
         if (this.isSendButtonVisible()) {
            var14 = ChatActivityEnterView.this.sendDrawable;
         } else if (ChatActivityEnterView.this.videoSendButton != null && ChatActivityEnterView.this.videoSendButton.getTag() != null) {
            var14 = ChatActivityEnterView.this.cameraDrawable;
         } else {
            var14 = ChatActivityEnterView.this.micDrawable;
         }

         var14.setBounds(var2 - var14.getIntrinsicWidth() / 2, var3 - var14.getIntrinsicHeight() / 2, var14.getIntrinsicWidth() / 2 + var2, var3 + var14.getIntrinsicHeight() / 2);
         int var15 = (int)(var6 * 255.0F);
         var14.setAlpha(var15);
         var14.draw(var1);
         var12 = 1.0F - var4 / (float)AndroidUtilities.dp(57.0F);
         var6 = Math.max(0.0F, 1.0F - var4 / (float)AndroidUtilities.dp(57.0F) * 2.0F);
         int var16;
         int var17;
         int var18;
         int var19;
         if (this.isSendButtonVisible()) {
            var16 = AndroidUtilities.dp(31.0F);
            var17 = AndroidUtilities.dp(57.0F) + (int)((float)AndroidUtilities.dp(30.0F) * (1.0F - var5) - var4 + (float)AndroidUtilities.dp(20.0F) * var12);
            var18 = AndroidUtilities.dp(5.0F);
            var19 = AndroidUtilities.dp(11.0F) + var17;
            var3 = AndroidUtilities.dp(25.0F) + var17;
            var15 = (int)((float)var15 * (var4 / (float)AndroidUtilities.dp(57.0F)));
            ChatActivityEnterView.this.lockBackgroundDrawable.setAlpha(255);
            ChatActivityEnterView.this.lockShadowDrawable.setAlpha(255);
            ChatActivityEnterView.this.lockTopDrawable.setAlpha(var15);
            ChatActivityEnterView.this.lockDrawable.setAlpha(var15);
            ChatActivityEnterView.this.lockArrowDrawable.setAlpha((int)((float)var15 * var6));
            var18 += var17;
         } else {
            var16 = AndroidUtilities.dp(31.0F) + (int)((float)AndroidUtilities.dp(29.0F) * var12);
            var17 = AndroidUtilities.dp(57.0F) + (int)((float)AndroidUtilities.dp(30.0F) * (1.0F - var5)) - (int)var4;
            var18 = AndroidUtilities.dp(5.0F) + var17 + (int)((float)AndroidUtilities.dp(4.0F) * var12);
            var3 = AndroidUtilities.dp(11.0F);
            var19 = (int)((float)AndroidUtilities.dp(10.0F) * var12) + var3 + var17;
            var3 = AndroidUtilities.dp(25.0F) + var17 + (int)((float)AndroidUtilities.dp(16.0F) * var12);
            ChatActivityEnterView.this.lockBackgroundDrawable.setAlpha(var15);
            ChatActivityEnterView.this.lockShadowDrawable.setAlpha(var15);
            ChatActivityEnterView.this.lockTopDrawable.setAlpha(var15);
            ChatActivityEnterView.this.lockDrawable.setAlpha(var15);
            ChatActivityEnterView.this.lockArrowDrawable.setAlpha((int)((float)var15 * var6));
         }

         var14 = ChatActivityEnterView.this.lockBackgroundDrawable;
         var15 = AndroidUtilities.dp(15.0F);
         int var20 = AndroidUtilities.dp(15.0F);
         var16 += var17;
         var14.setBounds(var2 - var15, var17, var20 + var2, var16);
         ChatActivityEnterView.this.lockBackgroundDrawable.draw(var1);
         ChatActivityEnterView.this.lockShadowDrawable.setBounds(var2 - AndroidUtilities.dp(16.0F), var17 - AndroidUtilities.dp(1.0F), AndroidUtilities.dp(16.0F) + var2, var16 + AndroidUtilities.dp(1.0F));
         ChatActivityEnterView.this.lockShadowDrawable.draw(var1);
         ChatActivityEnterView.this.lockTopDrawable.setBounds(var2 - AndroidUtilities.dp(6.0F), var18, AndroidUtilities.dp(6.0F) + var2, AndroidUtilities.dp(14.0F) + var18);
         ChatActivityEnterView.this.lockTopDrawable.draw(var1);
         ChatActivityEnterView.this.lockDrawable.setBounds(var2 - AndroidUtilities.dp(7.0F), var19, AndroidUtilities.dp(7.0F) + var2, AndroidUtilities.dp(12.0F) + var19);
         ChatActivityEnterView.this.lockDrawable.draw(var1);
         ChatActivityEnterView.this.lockArrowDrawable.setBounds(var2 - AndroidUtilities.dp(7.5F), var3, AndroidUtilities.dp(7.5F) + var2, AndroidUtilities.dp(9.0F) + var3);
         ChatActivityEnterView.this.lockArrowDrawable.draw(var1);
         if (this.isSendButtonVisible()) {
            ChatActivityEnterView.this.redDotPaint.setAlpha(255);
            ChatActivityEnterView.this.rect.set((float)(var2 - AndroidUtilities.dp2(6.5F)), (float)(AndroidUtilities.dp(9.0F) + var17), (float)(var2 + AndroidUtilities.dp(6.5F)), (float)(var17 + AndroidUtilities.dp(22.0F)));
            var1.drawRoundRect(ChatActivityEnterView.this.rect, (float)AndroidUtilities.dp(1.0F), (float)AndroidUtilities.dp(1.0F), ChatActivityEnterView.this.redDotPaint);
         }

      }

      public boolean onTouchEvent(MotionEvent var1) {
         if (this.sendButtonVisible) {
            int var2 = (int)var1.getX();
            int var3 = (int)var1.getY();
            if (var1.getAction() == 0) {
               boolean var4 = ChatActivityEnterView.this.lockBackgroundDrawable.getBounds().contains(var2, var3);
               this.pressed = var4;
               return var4;
            }

            if (this.pressed) {
               if (var1.getAction() == 1 && ChatActivityEnterView.this.lockBackgroundDrawable.getBounds().contains(var2, var3)) {
                  if (ChatActivityEnterView.this.videoSendButton != null && ChatActivityEnterView.this.videoSendButton.getTag() != null) {
                     ChatActivityEnterView.this.delegate.needStartRecordVideo(3);
                  } else {
                     MediaController.getInstance().stopRecording(2);
                     ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                  }
               }

               return true;
            }
         }

         return false;
      }

      public void setAmplitude(double var1) {
         this.animateToAmplitude = (float)Math.min(100.0D, var1) / 100.0F;
         this.animateAmplitudeDiff = (this.animateToAmplitude - this.amplitude) / 150.0F;
         this.lastUpdateTime = System.currentTimeMillis();
         this.invalidate();
      }

      @Keep
      public void setLockAnimatedTranslation(float var1) {
         this.lockAnimatedTranslation = var1;
         this.invalidate();
      }

      public int setLockTranslation(float var1) {
         if (var1 == 10000.0F) {
            this.sendButtonVisible = false;
            this.lockAnimatedTranslation = -1.0F;
            this.startTranslation = -1.0F;
            this.invalidate();
            return 0;
         } else if (this.sendButtonVisible) {
            return 2;
         } else {
            if (this.lockAnimatedTranslation == -1.0F) {
               this.startTranslation = var1;
            }

            this.lockAnimatedTranslation = var1;
            this.invalidate();
            if (this.startTranslation - this.lockAnimatedTranslation >= (float)AndroidUtilities.dp(57.0F)) {
               this.sendButtonVisible = true;
               return 2;
            } else {
               return 1;
            }
         }
      }

      @Keep
      public void setScale(float var1) {
         this.scale = var1;
         this.invalidate();
      }

      public void setSendButtonInvisible() {
         this.sendButtonVisible = false;
         this.invalidate();
      }

      private class VirtualViewHelper extends ExploreByTouchHelper {
         public VirtualViewHelper(View var2) {
            super(var2);
         }

         protected int getVirtualViewAt(float var1, float var2) {
            if (RecordCircle.this.isSendButtonVisible()) {
               android.graphics.Rect var3 = ChatActivityEnterView.this.sendDrawable.getBounds();
               int var4 = (int)var1;
               int var5 = (int)var2;
               if (var3.contains(var4, var5)) {
                  return 1;
               }

               if (ChatActivityEnterView.this.lockBackgroundDrawable.getBounds().contains(var4, var5)) {
                  return 2;
               }
            }

            return -1;
         }

         protected void getVisibleVirtualViews(List var1) {
            if (RecordCircle.this.isSendButtonVisible()) {
               var1.add(1);
               var1.add(2);
            }

         }

         protected boolean onPerformActionForVirtualView(int var1, int var2, Bundle var3) {
            return true;
         }

         protected void onPopulateNodeForVirtualView(int var1, AccessibilityNodeInfoCompat var2) {
            if (var1 == 1) {
               var2.setBoundsInParent(ChatActivityEnterView.this.sendDrawable.getBounds());
               var2.setText(LocaleController.getString("Send", 2131560685));
            } else if (var1 == 2) {
               var2.setBoundsInParent(ChatActivityEnterView.this.lockBackgroundDrawable.getBounds());
               var2.setText(LocaleController.getString("Stop", 2131560820));
            }

         }
      }
   }

   private class RecordDot extends View {
      private float alpha;
      private boolean isIncr;
      private long lastUpdateTime;

      public RecordDot(Context var2) {
         super(var2);
         ChatActivityEnterView.this.redDotPaint.setColor(Theme.getColor("chat_recordedVoiceDot"));
      }

      protected void onDraw(Canvas var1) {
         ChatActivityEnterView.this.redDotPaint.setAlpha((int)(this.alpha * 255.0F));
         long var2 = System.currentTimeMillis() - this.lastUpdateTime;
         if (!this.isIncr) {
            this.alpha -= (float)var2 / 400.0F;
            if (this.alpha <= 0.0F) {
               this.alpha = 0.0F;
               this.isIncr = true;
            }
         } else {
            this.alpha += (float)var2 / 400.0F;
            if (this.alpha >= 1.0F) {
               this.alpha = 1.0F;
               this.isIncr = false;
            }
         }

         this.lastUpdateTime = System.currentTimeMillis();
         var1.drawCircle((float)AndroidUtilities.dp(5.0F), (float)AndroidUtilities.dp(5.0F), (float)AndroidUtilities.dp(5.0F), ChatActivityEnterView.this.redDotPaint);
         this.invalidate();
      }

      public void resetAlpha() {
         this.alpha = 1.0F;
         this.lastUpdateTime = System.currentTimeMillis();
         this.isIncr = false;
         this.invalidate();
      }
   }

   private class ScrimDrawable extends Drawable {
      private Paint paint = new Paint();

      public ScrimDrawable() {
         this.paint.setColor(0);
      }

      public void draw(Canvas var1) {
         if (ChatActivityEnterView.this.emojiView != null) {
            this.paint.setAlpha(Math.round(ChatActivityEnterView.this.stickersExpansionProgress * 102.0F));
            var1.drawRect(0.0F, 0.0F, (float)ChatActivityEnterView.this.getWidth(), ChatActivityEnterView.this.emojiView.getY() - (float)ChatActivityEnterView.this.getHeight() + (float)Theme.chat_composeShadowDrawable.getIntrinsicHeight(), this.paint);
         }
      }

      public int getOpacity() {
         return -2;
      }

      public void setAlpha(int var1) {
      }

      public void setColorFilter(ColorFilter var1) {
      }
   }

   private class SeekBarWaveformView extends View {
      public SeekBarWaveformView(Context var2) {
         super(var2);
         ChatActivityEnterView.this.seekBarWaveform = new SeekBarWaveform(var2);
         ChatActivityEnterView.this.seekBarWaveform.setDelegate(new _$$Lambda$ChatActivityEnterView$SeekBarWaveformView$3LQzMaku4bFncUmbMthE8hwccgc(this));
      }

      public boolean isDragging() {
         return ChatActivityEnterView.this.seekBarWaveform.isDragging();
      }

      // $FF: synthetic method
      public void lambda$new$0$ChatActivityEnterView$SeekBarWaveformView(float var1) {
         if (ChatActivityEnterView.this.audioToSendMessageObject != null) {
            ChatActivityEnterView.this.audioToSendMessageObject.audioProgress = var1;
            MediaController.getInstance().seekToProgress(ChatActivityEnterView.this.audioToSendMessageObject, var1);
         }

      }

      protected void onDraw(Canvas var1) {
         super.onDraw(var1);
         ChatActivityEnterView.this.seekBarWaveform.setColors(Theme.getColor("chat_recordedVoiceProgress"), Theme.getColor("chat_recordedVoiceProgressInner"), Theme.getColor("chat_recordedVoiceProgress"));
         ChatActivityEnterView.this.seekBarWaveform.draw(var1);
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         super.onLayout(var1, var2, var3, var4, var5);
         ChatActivityEnterView.this.seekBarWaveform.setSize(var4 - var2, var5 - var3);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2 = ChatActivityEnterView.this.seekBarWaveform.onTouch(var1.getAction(), var1.getX(), var1.getY());
         boolean var3 = true;
         if (var2) {
            if (var1.getAction() == 0) {
               ChatActivityEnterView.this.requestDisallowInterceptTouchEvent(true);
            }

            this.invalidate();
         }

         boolean var4 = var3;
         if (!var2) {
            if (super.onTouchEvent(var1)) {
               var4 = var3;
            } else {
               var4 = false;
            }
         }

         return var4;
      }

      public void setProgress(float var1) {
         ChatActivityEnterView.this.seekBarWaveform.setProgress(var1);
         this.invalidate();
      }

      public void setWaveform(byte[] var1) {
         ChatActivityEnterView.this.seekBarWaveform.setWaveform(var1);
         this.invalidate();
      }
   }
}
