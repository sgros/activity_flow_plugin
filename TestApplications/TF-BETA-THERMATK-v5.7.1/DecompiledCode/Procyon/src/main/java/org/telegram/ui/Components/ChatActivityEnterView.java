// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.List;
import android.graphics.Rect;
import androidx.customview.widget.ExploreByTouchHelper;
import androidx.annotation.Keep;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import android.view.accessibility.AccessibilityManager;
import android.text.InputFilter$LengthFilter;
import android.text.InputFilter;
import java.util.Locale;
import android.text.SpannableStringBuilder;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.LaunchActivity;
import java.io.File;
import android.animation.ValueAnimator;
import org.telegram.ui.DialogsActivity;
import org.telegram.messenger.SharedConfig;
import android.view.animation.AccelerateInterpolator;
import org.telegram.messenger.ApplicationLoader;
import android.os.PowerManager;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.ChatObject;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.ui.StickersActivity;
import org.telegram.ui.GroupStickersActivity;
import org.telegram.ui.ActionBar.BaseFragment;
import java.util.HashMap;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import org.telegram.ui.ActionBar.ActionBar;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import java.util.Collection;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.View$OnTouchListener;
import org.telegram.messenger.NotificationsController;
import android.widget.Toast;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.Emoji;
import android.text.style.ImageSpan;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView$OnEditorActionListener;
import android.content.SharedPreferences$Editor;
import android.view.KeyEvent;
import android.view.View$OnKeyListener;
import org.telegram.messenger.FileLog;
import android.view.MotionEvent;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.EditorInfo;
import java.util.ArrayList;
import org.telegram.messenger.SendMessagesHelper;
import androidx.core.os.BuildCompat;
import android.os.Bundle;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import org.telegram.messenger.LocaleController;
import android.view.View$OnClickListener;
import android.widget.ImageView$ScaleType;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.messenger.DataQuery;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.camera.CameraController;
import android.os.Build$VERSION;
import org.telegram.messenger.AndroidUtilities;
import android.view.accessibility.AccessibilityNodeInfo;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.os.PowerManager$WakeLock;
import org.telegram.messenger.VideoEditedInfo;
import android.graphics.RectF;
import android.view.View;
import android.util.Property;
import android.widget.TextView;
import org.telegram.ui.ChatActivity;
import android.app.Activity;
import android.view.View$AccessibilityDelegate;
import android.graphics.Paint;
import android.animation.Animator;
import android.graphics.drawable.Drawable;
import android.widget.PopupWindow;
import android.animation.AnimatorSet;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;
import android.widget.LinearLayout;
import android.widget.ImageView;
import org.telegram.messenger.NotificationCenter;
import android.widget.FrameLayout;

public class ChatActivityEnterView extends FrameLayout implements NotificationCenterDelegate, SizeNotifierFrameLayoutDelegate, StickersAlertDelegate
{
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
    private ChatActivityEnterViewDelegate delegate;
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
    private View$AccessibilityDelegate mediaMessageButtonsDelegate;
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
    private RecordCircle recordCircle;
    private Property<RecordCircle, Float> recordCircleScale;
    private ImageView recordDeleteImageView;
    private RecordDot recordDot;
    private int recordInterfaceState;
    private FrameLayout recordPanel;
    private TextView recordSendText;
    private LinearLayout recordTimeContainer;
    private TextView recordTimeText;
    private View recordedAudioBackground;
    private FrameLayout recordedAudioPanel;
    private ImageView recordedAudioPlayButton;
    private SeekBarWaveformView recordedAudioSeekBar;
    private TextView recordedAudioTimeTextView;
    private boolean recordingAudioVideo;
    private RectF rect;
    private Paint redDotPaint;
    private MessageObject replyingMessageObject;
    private Property<View, Integer> roundedTranslationYProperty;
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
    private PowerManager$WakeLock wakeLock;
    
    @SuppressLint({ "ClickableViewAccessibility" })
    public ChatActivityEnterView(final Activity parentActivity, final SizeNotifierFrameLayout sizeNotifierLayout, final ChatActivity parentFragment, final boolean b) {
        super((Context)parentActivity);
        this.currentAccount = UserConfig.selectedAccount;
        this.mediaMessageButtonsDelegate = new View$AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setClassName((CharSequence)"android.widget.ImageButton");
                accessibilityNodeInfo.setClickable(true);
                accessibilityNodeInfo.setLongClickable(true);
            }
        };
        this.emojiButton = new ImageView[2];
        this.currentPopupContentType = -1;
        this.currentEmojiIcon = -1;
        this.isPaused = true;
        this.startedDraggingX = -1.0f;
        this.distCanMove = (float)AndroidUtilities.dp(80.0f);
        this.messageWebPageSearch = true;
        this.openKeyboardRunnable = new Runnable() {
            @Override
            public void run() {
                if (!ChatActivityEnterView.this.destroyed && ChatActivityEnterView.this.messageEditText != null && ChatActivityEnterView.this.waitingForKeyboardOpen && !ChatActivityEnterView.this.keyboardVisible && !AndroidUtilities.usingHardwareInput && !AndroidUtilities.isInMultiwindow) {
                    ChatActivityEnterView.this.messageEditText.requestFocus();
                    AndroidUtilities.showKeyboard((View)ChatActivityEnterView.this.messageEditText);
                    AndroidUtilities.cancelRunOnUIThread(ChatActivityEnterView.this.openKeyboardRunnable);
                    AndroidUtilities.runOnUIThread(ChatActivityEnterView.this.openKeyboardRunnable, 100L);
                }
            }
        };
        this.updateExpandabilityRunnable = new Runnable() {
            private int lastKnownPage = -1;
            
            @Override
            public void run() {
                if (ChatActivityEnterView.this.emojiView != null) {
                    final int currentPage = ChatActivityEnterView.this.emojiView.getCurrentPage();
                    if (currentPage != this.lastKnownPage) {
                        this.lastKnownPage = currentPage;
                        final boolean access$800 = ChatActivityEnterView.this.stickersTabOpen;
                        final ChatActivityEnterView this$0 = ChatActivityEnterView.this;
                        int n = 2;
                        this$0.stickersTabOpen = (currentPage == 1 || currentPage == 2);
                        final boolean access$801 = ChatActivityEnterView.this.emojiTabOpen;
                        ChatActivityEnterView.this.emojiTabOpen = (currentPage == 0);
                        if (ChatActivityEnterView.this.stickersExpanded) {
                            if (!ChatActivityEnterView.this.stickersTabOpen && ChatActivityEnterView.this.searchingType == 0) {
                                if (ChatActivityEnterView.this.searchingType != 0) {
                                    ChatActivityEnterView.this.searchingType = 0;
                                    ChatActivityEnterView.this.emojiView.closeSearch(true);
                                    ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                                }
                                ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                            }
                            else if (ChatActivityEnterView.this.searchingType != 0) {
                                final ChatActivityEnterView this$2 = ChatActivityEnterView.this;
                                if (currentPage != 0) {
                                    n = 1;
                                }
                                this$2.searchingType = n;
                                ChatActivityEnterView.this.checkStickresExpandHeight();
                            }
                        }
                        if (access$800 != ChatActivityEnterView.this.stickersTabOpen || access$801 != ChatActivityEnterView.this.emojiTabOpen) {
                            ChatActivityEnterView.this.checkSendButton(true);
                        }
                    }
                }
            }
        };
        this.roundedTranslationYProperty = new Property<View, Integer>((Class)Integer.class, "translationY") {
            public Integer get(final View view) {
                return Math.round(view.getTranslationY());
            }
            
            public void set(final View view, final Integer n) {
                view.setTranslationY((float)n);
            }
        };
        this.recordCircleScale = new Property<RecordCircle, Float>((Class)Float.class, "scale") {
            public Float get(final RecordCircle recordCircle) {
                return recordCircle.getScale();
            }
            
            public void set(final RecordCircle recordCircle, final Float n) {
                recordCircle.setScale(n);
            }
        };
        this.redDotPaint = new Paint(1);
        this.onFinishInitCameraRunnable = new Runnable() {
            @Override
            public void run() {
                if (ChatActivityEnterView.this.delegate != null) {
                    ChatActivityEnterView.this.delegate.needStartRecordVideo(0);
                }
            }
        };
        this.recordAudioVideoRunnable = new Runnable() {
            @Override
            public void run() {
                if (ChatActivityEnterView.this.delegate != null) {
                    if (ChatActivityEnterView.this.parentActivity != null) {
                        ChatActivityEnterView.this.delegate.onPreAudioVideoRecord();
                        ChatActivityEnterView.this.calledRecordRunnable = true;
                        ChatActivityEnterView.this.recordAudioVideoRunnableStarted = false;
                        ChatActivityEnterView.this.recordCircle.setLockTranslation(10000.0f);
                        ChatActivityEnterView.this.recordSendText.setAlpha(0.0f);
                        ChatActivityEnterView.this.slideText.setAlpha(1.0f);
                        ChatActivityEnterView.this.slideText.setTranslationY(0.0f);
                        if (ChatActivityEnterView.this.videoSendButton != null && ChatActivityEnterView.this.videoSendButton.getTag() != null) {
                            if (Build$VERSION.SDK_INT >= 23) {
                                final boolean b = ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.RECORD_AUDIO") == 0;
                                final boolean b2 = ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.CAMERA") == 0;
                                if (!b || !b2) {
                                    int n;
                                    if (!b && !b2) {
                                        n = 2;
                                    }
                                    else {
                                        n = 1;
                                    }
                                    final String[] array = new String[n];
                                    if (!b && !b2) {
                                        array[0] = "android.permission.RECORD_AUDIO";
                                        array[1] = "android.permission.CAMERA";
                                    }
                                    else if (!b) {
                                        array[0] = "android.permission.RECORD_AUDIO";
                                    }
                                    else {
                                        array[0] = "android.permission.CAMERA";
                                    }
                                    ChatActivityEnterView.this.parentActivity.requestPermissions(array, 3);
                                    return;
                                }
                            }
                            if (!CameraController.getInstance().isCameraInitied()) {
                                CameraController.getInstance().initCamera(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                            }
                            else {
                                ChatActivityEnterView.this.onFinishInitCameraRunnable.run();
                            }
                        }
                        else {
                            if (ChatActivityEnterView.this.parentFragment != null && Build$VERSION.SDK_INT >= 23 && ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
                                ChatActivityEnterView.this.parentActivity.requestPermissions(new String[] { "android.permission.RECORD_AUDIO" }, 3);
                                return;
                            }
                            ChatActivityEnterView.this.delegate.needStartRecordAudio(1);
                            ChatActivityEnterView.this.startedDraggingX = -1.0f;
                            MediaController.getInstance().startRecording(ChatActivityEnterView.this.currentAccount, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject);
                            ChatActivityEnterView.this.updateRecordIntefrace();
                            ChatActivityEnterView.this.audioVideoButtonContainer.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }
            }
        };
        this.paint = new Paint(1);
        this.paintRecord = new Paint(1);
        this.rect = new RectF();
        (this.dotPaint = new Paint(1)).setColor(Theme.getColor("chat_emojiPanelNewTrending"));
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
        this.parentActivity = parentActivity;
        this.parentFragment = parentFragment;
        (this.sizeNotifierLayout = sizeNotifierLayout).setDelegate((SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate)this);
        this.sendByEnter = MessagesController.getGlobalMainSettings().getBoolean("send_by_enter", false);
        (this.textFieldContainer = new LinearLayout((Context)parentActivity)).setOrientation(0);
        this.addView((View)this.textFieldContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 2.0f, 0.0f, 0.0f));
        final FrameLayout frameLayout = new FrameLayout((Context)parentActivity);
        this.textFieldContainer.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -2, 1.0f, 80));
        for (int i = 0; i < 2; ++i) {
            (this.emojiButton[i] = new ImageView(parentActivity) {
                protected void onDraw(final Canvas canvas) {
                    super.onDraw(canvas);
                    if (this.getTag() != null && ChatActivityEnterView.this.attachLayout != null && !ChatActivityEnterView.this.emojiViewVisible && !DataQuery.getInstance(ChatActivityEnterView.this.currentAccount).getUnreadStickerSets().isEmpty() && ChatActivityEnterView.this.dotPaint != null) {
                        canvas.drawCircle((float)(this.getWidth() / 2 + AndroidUtilities.dp(9.0f)), (float)(this.getHeight() / 2 - AndroidUtilities.dp(8.0f)), (float)AndroidUtilities.dp(5.0f), ChatActivityEnterView.this.dotPaint);
                    }
                }
            }).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), PorterDuff$Mode.MULTIPLY));
            this.emojiButton[i].setScaleType(ImageView$ScaleType.CENTER_INSIDE);
            frameLayout.addView((View)this.emojiButton[i], (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, 83, 3.0f, 0.0f, 0.0f, 0.0f));
            this.emojiButton[i].setOnClickListener((View$OnClickListener)new _$$Lambda$ChatActivityEnterView$jlpojNCIsF_c7hlFcVLHdouZye4(this));
            this.emojiButton[i].setContentDescription((CharSequence)LocaleController.getString("AccDescrEmojiButton", 2131558431));
            if (i == 1) {
                this.emojiButton[i].setVisibility(4);
                this.emojiButton[i].setAlpha(0.0f);
                this.emojiButton[i].setScaleX(0.1f);
                this.emojiButton[i].setScaleY(0.1f);
            }
        }
        this.setEmojiButtonImage(false, false);
        (this.messageEditText = new EditTextCaption(parentActivity) {
            public InputConnection onCreateInputConnection(final EditorInfo editorInfo) {
                final InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
                EditorInfoCompat.setContentMimeTypes(editorInfo, new String[] { "image/gif", "image/*", "image/jpg", "image/png" });
                return InputConnectionCompat.createWrapper(onCreateInputConnection, editorInfo, (InputConnectionCompat.OnCommitContentListener)new _$$Lambda$ChatActivityEnterView$9$ETBc4NSNPoua6FyaOMaLXYYn2ak(this));
            }
            
            protected void onSelectionChanged(final int n, final int n2) {
                super.onSelectionChanged(n, n2);
                if (ChatActivityEnterView.this.delegate != null) {
                    ChatActivityEnterView.this.delegate.onTextSelectionChanged(n, n2);
                }
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (ChatActivityEnterView.this.isPopupShowing() && motionEvent.getAction() == 0) {
                    if (ChatActivityEnterView.this.searchingType != 0) {
                        ChatActivityEnterView.this.searchingType = 0;
                        ChatActivityEnterView.this.emojiView.closeSearch(false);
                    }
                    final ChatActivityEnterView this$0 = ChatActivityEnterView.this;
                    int n;
                    if (AndroidUtilities.usingHardwareInput) {
                        n = 0;
                    }
                    else {
                        n = 2;
                    }
                    this$0.showPopup(n, 0);
                    ChatActivityEnterView.this.openKeyboardInternal();
                }
                try {
                    return super.onTouchEvent(motionEvent);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                    return false;
                }
            }
        }).setDelegate((EditTextCaption.EditTextCaptionDelegate)new _$$Lambda$ChatActivityEnterView$YXzMuZPPiDr1K7R7cpN8afDWrUQ(this));
        this.updateFieldHint();
        final int n = 268435456;
        final ChatActivity parentFragment2 = this.parentFragment;
        int imeOptions = n;
        if (parentFragment2 != null) {
            imeOptions = n;
            if (parentFragment2.getCurrentEncryptedChat() != null) {
                imeOptions = 285212672;
            }
        }
        this.messageEditText.setImeOptions(imeOptions);
        final EditTextCaption messageEditText = this.messageEditText;
        messageEditText.setInputType(messageEditText.getInputType() | 0x4000 | 0x20000);
        this.messageEditText.setSingleLine(false);
        this.messageEditText.setMaxLines(6);
        this.messageEditText.setTextSize(1, 18.0f);
        this.messageEditText.setGravity(80);
        this.messageEditText.setPadding(0, AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(12.0f));
        this.messageEditText.setBackgroundDrawable((Drawable)null);
        this.messageEditText.setTextColor(Theme.getColor("chat_messagePanelText"));
        this.messageEditText.setHintColor(Theme.getColor("chat_messagePanelHint"));
        this.messageEditText.setHintTextColor(Theme.getColor("chat_messagePanelHint"));
        final EditTextCaption messageEditText2 = this.messageEditText;
        float n2;
        if (b) {
            n2 = 50.0f;
        }
        else {
            n2 = 2.0f;
        }
        frameLayout.addView((View)messageEditText2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 80, 52.0f, 0.0f, n2, 0.0f));
        this.messageEditText.setOnKeyListener((View$OnKeyListener)new View$OnKeyListener() {
            boolean ctrlPressed = false;
            
            public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
                boolean ctrlPressed = false;
                if (n == 4 && !ChatActivityEnterView.this.keyboardVisible && ChatActivityEnterView.this.isPopupShowing()) {
                    if (keyEvent.getAction() == 1) {
                        if (ChatActivityEnterView.this.currentPopupContentType == 1 && ChatActivityEnterView.this.botButtonsMessageObject != null) {
                            final SharedPreferences$Editor edit = MessagesController.getMainSettings(ChatActivityEnterView.this.currentAccount).edit();
                            final StringBuilder sb = new StringBuilder();
                            sb.append("hidekeyboard_");
                            sb.append(ChatActivityEnterView.this.dialog_id);
                            edit.putInt(sb.toString(), ChatActivityEnterView.this.botButtonsMessageObject.getId()).commit();
                        }
                        if (ChatActivityEnterView.this.searchingType != 0) {
                            ChatActivityEnterView.this.searchingType = 0;
                            ChatActivityEnterView.this.emojiView.closeSearch(true);
                            ChatActivityEnterView.this.messageEditText.requestFocus();
                        }
                        else {
                            ChatActivityEnterView.this.showPopup(0, 0);
                        }
                    }
                    return true;
                }
                if (n == 66 && (this.ctrlPressed || ChatActivityEnterView.this.sendByEnter) && keyEvent.getAction() == 0 && ChatActivityEnterView.this.editingMessageObject == null) {
                    ChatActivityEnterView.this.sendMessage();
                    return true;
                }
                if (n != 113 && n != 114) {
                    return false;
                }
                if (keyEvent.getAction() == 0) {
                    ctrlPressed = true;
                }
                this.ctrlPressed = ctrlPressed;
                return true;
            }
        });
        this.messageEditText.setOnEditorActionListener((TextView$OnEditorActionListener)new TextView$OnEditorActionListener() {
            boolean ctrlPressed = false;
            
            public boolean onEditorAction(final TextView textView, final int n, final KeyEvent keyEvent) {
                if (n == 4) {
                    ChatActivityEnterView.this.sendMessage();
                    return true;
                }
                boolean ctrlPressed = false;
                if (keyEvent != null && n == 0) {
                    if ((this.ctrlPressed || ChatActivityEnterView.this.sendByEnter) && keyEvent.getAction() == 0 && ChatActivityEnterView.this.editingMessageObject == null) {
                        ChatActivityEnterView.this.sendMessage();
                        return true;
                    }
                    if (n == 113 || n == 114) {
                        if (keyEvent.getAction() == 0) {
                            ctrlPressed = true;
                        }
                        this.ctrlPressed = ctrlPressed;
                        return true;
                    }
                }
                return false;
            }
        });
        this.messageEditText.addTextChangedListener((TextWatcher)new TextWatcher() {
            boolean processChange = false;
            
            public void afterTextChanged(final Editable editable) {
                if (ChatActivityEnterView.this.innerTextChange != 0) {
                    return;
                }
                if (ChatActivityEnterView.this.sendByEnter && editable.length() > 0 && editable.charAt(editable.length() - 1) == '\n' && ChatActivityEnterView.this.editingMessageObject == null) {
                    ChatActivityEnterView.this.sendMessage();
                }
                if (this.processChange) {
                    final ImageSpan[] array = (ImageSpan[])editable.getSpans(0, editable.length(), (Class)ImageSpan.class);
                    for (int i = 0; i < array.length; ++i) {
                        editable.removeSpan((Object)array[i]);
                    }
                    Emoji.replaceEmoji((CharSequence)editable, ChatActivityEnterView.this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    this.processChange = false;
                }
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, int currentTime, final int n, final int n2) {
                if (ChatActivityEnterView.this.innerTextChange == 1) {
                    return;
                }
                ChatActivityEnterView.this.checkSendButton(true);
                final CharSequence trimmedString = AndroidUtilities.getTrimmedString(charSequence.toString());
                if (ChatActivityEnterView.this.delegate != null && !ChatActivityEnterView.this.ignoreTextChange) {
                    if (n2 > 2 || charSequence == null || charSequence.length() == 0) {
                        ChatActivityEnterView.this.messageWebPageSearch = true;
                    }
                    ChatActivityEnterView.this.delegate.onTextChanged(charSequence, n > n2 + 1 || n2 - n > 2);
                }
                if (ChatActivityEnterView.this.innerTextChange != 2 && n != n2 && n2 - n > 1) {
                    this.processChange = true;
                }
                if (ChatActivityEnterView.this.editingMessageObject == null && !ChatActivityEnterView.this.canWriteToChannel && trimmedString.length() != 0 && ChatActivityEnterView.this.lastTypingTimeSend < System.currentTimeMillis() - 5000L && !ChatActivityEnterView.this.ignoreTextChange) {
                    currentTime = ConnectionsManager.getInstance(ChatActivityEnterView.this.currentAccount).getCurrentTime();
                    TLRPC.User user = null;
                    if ((int)ChatActivityEnterView.this.dialog_id > 0) {
                        user = MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).getUser((int)ChatActivityEnterView.this.dialog_id);
                    }
                    Label_0337: {
                        if (user != null) {
                            if (user.id != UserConfig.getInstance(ChatActivityEnterView.this.currentAccount).getClientUserId()) {
                                final TLRPC.UserStatus status = user.status;
                                if (status == null || status.expires >= currentTime || MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).onlinePrivacy.containsKey(user.id)) {
                                    break Label_0337;
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
        });
        if (b) {
            (this.attachLayout = new LinearLayout((Context)parentActivity)).setOrientation(0);
            this.attachLayout.setEnabled(false);
            this.attachLayout.setPivotX((float)AndroidUtilities.dp(48.0f));
            frameLayout.addView((View)this.attachLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 48, 85));
            (this.botButton = new ImageView((Context)parentActivity)).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), PorterDuff$Mode.MULTIPLY));
            this.botButton.setImageResource(2131165482);
            this.botButton.setScaleType(ImageView$ScaleType.CENTER);
            this.botButton.setVisibility(8);
            this.attachLayout.addView((View)this.botButton, (ViewGroup$LayoutParams)LayoutHelper.createLinear(48, 48));
            this.botButton.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatActivityEnterView$ePEXGP4V8TnztLq_2EEcKkpts_k(this));
            this.notifyButton = new ImageView((Context)parentActivity);
            final ImageView notifyButton = this.notifyButton;
            int imageResource;
            if (this.silent) {
                imageResource = 2131165489;
            }
            else {
                imageResource = 2131165490;
            }
            notifyButton.setImageResource(imageResource);
            final ImageView notifyButton2 = this.notifyButton;
            int n3;
            String s;
            if (this.silent) {
                n3 = 2131558424;
                s = "AccDescrChanSilentOn";
            }
            else {
                n3 = 2131558423;
                s = "AccDescrChanSilentOff";
            }
            notifyButton2.setContentDescription((CharSequence)LocaleController.getString(s, n3));
            this.notifyButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), PorterDuff$Mode.MULTIPLY));
            this.notifyButton.setScaleType(ImageView$ScaleType.CENTER);
            final ImageView notifyButton3 = this.notifyButton;
            int visibility;
            if (this.canWriteToChannel) {
                visibility = 0;
            }
            else {
                visibility = 8;
            }
            notifyButton3.setVisibility(visibility);
            this.attachLayout.addView((View)this.notifyButton, (ViewGroup$LayoutParams)LayoutHelper.createLinear(48, 48));
            this.notifyButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                private Toast visibleToast;
                
                public void onClick(final View view) {
                    final ChatActivityEnterView this$0 = ChatActivityEnterView.this;
                    this$0.silent ^= true;
                    final ImageView access$5900 = ChatActivityEnterView.this.notifyButton;
                    int imageResource;
                    if (ChatActivityEnterView.this.silent) {
                        imageResource = 2131165489;
                    }
                    else {
                        imageResource = 2131165490;
                    }
                    access$5900.setImageResource(imageResource);
                    final SharedPreferences$Editor edit = MessagesController.getNotificationsSettings(ChatActivityEnterView.this.currentAccount).edit();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("silent_");
                    sb.append(ChatActivityEnterView.this.dialog_id);
                    edit.putBoolean(sb.toString(), ChatActivityEnterView.this.silent).commit();
                    NotificationsController.getInstance(ChatActivityEnterView.this.currentAccount).updateServerNotificationsSettings(ChatActivityEnterView.this.dialog_id);
                    try {
                        if (this.visibleToast != null) {
                            this.visibleToast.cancel();
                        }
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    if (ChatActivityEnterView.this.silent) {
                        (this.visibleToast = Toast.makeText((Context)ChatActivityEnterView.this.parentActivity, (CharSequence)LocaleController.getString("ChannelNotifyMembersInfoOff", 2131558981), 0)).show();
                    }
                    else {
                        (this.visibleToast = Toast.makeText((Context)ChatActivityEnterView.this.parentActivity, (CharSequence)LocaleController.getString("ChannelNotifyMembersInfoOn", 2131558982), 0)).show();
                    }
                    final ImageView access$5901 = ChatActivityEnterView.this.notifyButton;
                    int n;
                    String s;
                    if (ChatActivityEnterView.this.silent) {
                        n = 2131558424;
                        s = "AccDescrChanSilentOn";
                    }
                    else {
                        n = 2131558423;
                        s = "AccDescrChanSilentOff";
                    }
                    access$5901.setContentDescription((CharSequence)LocaleController.getString(s, n));
                    ChatActivityEnterView.this.updateFieldHint();
                }
            });
            (this.attachButton = new ImageView((Context)parentActivity)).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), PorterDuff$Mode.MULTIPLY));
            this.attachButton.setImageResource(2131165480);
            this.attachButton.setScaleType(ImageView$ScaleType.CENTER);
            this.attachLayout.addView((View)this.attachButton, (ViewGroup$LayoutParams)LayoutHelper.createLinear(48, 48));
            this.attachButton.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatActivityEnterView$sA9S70ZL3Nng90HptN_7RpznwZo(this));
            this.attachButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrAttachButton", 2131558413));
        }
        this.recordedAudioPanel = new FrameLayout((Context)parentActivity);
        final FrameLayout recordedAudioPanel = this.recordedAudioPanel;
        int visibility2;
        if (this.audioToSend == null) {
            visibility2 = 8;
        }
        else {
            visibility2 = 0;
        }
        recordedAudioPanel.setVisibility(visibility2);
        this.recordedAudioPanel.setBackgroundColor(Theme.getColor("chat_messagePanelBackground"));
        this.recordedAudioPanel.setFocusable(true);
        this.recordedAudioPanel.setFocusableInTouchMode(true);
        this.recordedAudioPanel.setClickable(true);
        frameLayout.addView((View)this.recordedAudioPanel, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 80));
        (this.recordDeleteImageView = new ImageView((Context)parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
        this.recordDeleteImageView.setImageResource(2131165623);
        this.recordDeleteImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelVoiceDelete"), PorterDuff$Mode.MULTIPLY));
        this.recordDeleteImageView.setContentDescription((CharSequence)LocaleController.getString("Delete", 2131559227));
        this.recordedAudioPanel.addView((View)this.recordDeleteImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f));
        this.recordDeleteImageView.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatActivityEnterView$pQGz59T4TelJWdtUhc7Wjoj7V9g(this));
        (this.videoTimelineView = new VideoTimelineView((Context)parentActivity)).setColor(-11817481);
        this.videoTimelineView.setRoundFrames(true);
        this.videoTimelineView.setDelegate((VideoTimelineView.VideoTimelineViewDelegate)new VideoTimelineView.VideoTimelineViewDelegate() {
            @Override
            public void didStartDragging() {
                ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(1, 0.0f);
            }
            
            @Override
            public void didStopDragging() {
                ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(0, 0.0f);
            }
            
            @Override
            public void onLeftProgressChanged(final float n) {
                if (ChatActivityEnterView.this.videoToSendMessageObject == null) {
                    return;
                }
                ChatActivityEnterView.this.videoToSendMessageObject.startTime = (long)(ChatActivityEnterView.this.videoToSendMessageObject.estimatedDuration * n);
                ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(2, n);
            }
            
            @Override
            public void onRightProgressChanged(final float n) {
                if (ChatActivityEnterView.this.videoToSendMessageObject == null) {
                    return;
                }
                ChatActivityEnterView.this.videoToSendMessageObject.endTime = (long)(ChatActivityEnterView.this.videoToSendMessageObject.estimatedDuration * n);
                ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(2, n);
            }
        });
        this.recordedAudioPanel.addView((View)this.videoTimelineView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 32.0f, 19, 40.0f, 0.0f, 0.0f, 0.0f));
        (this.recordedAudioBackground = new View((Context)parentActivity)).setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), Theme.getColor("chat_recordedVoiceBackground")));
        this.recordedAudioPanel.addView(this.recordedAudioBackground, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f, 19, 48.0f, 0.0f, 0.0f, 0.0f));
        this.recordedAudioSeekBar = new SeekBarWaveformView((Context)parentActivity);
        this.recordedAudioPanel.addView((View)this.recordedAudioSeekBar, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 32.0f, 19, 92.0f, 0.0f, 52.0f, 0.0f));
        this.playDrawable = Theme.createSimpleSelectorDrawable((Context)parentActivity, 2131165806, Theme.getColor("chat_recordedVoicePlayPause"), Theme.getColor("chat_recordedVoicePlayPausePressed"));
        this.pauseDrawable = Theme.createSimpleSelectorDrawable((Context)parentActivity, 2131165805, Theme.getColor("chat_recordedVoicePlayPause"), Theme.getColor("chat_recordedVoicePlayPausePressed"));
        (this.recordedAudioPlayButton = new ImageView((Context)parentActivity)).setImageDrawable(this.playDrawable);
        this.recordedAudioPlayButton.setScaleType(ImageView$ScaleType.CENTER);
        this.recordedAudioPlayButton.setContentDescription((CharSequence)LocaleController.getString("AccActionPlay", 2131558409));
        this.recordedAudioPanel.addView((View)this.recordedAudioPlayButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, 83, 48.0f, 0.0f, 0.0f, 0.0f));
        this.recordedAudioPlayButton.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatActivityEnterView$0PqxYA4Sm4DXenEKSdETEwDwen0(this));
        (this.recordedAudioTimeTextView = new TextView((Context)parentActivity)).setTextColor(Theme.getColor("chat_messagePanelVoiceDuration"));
        this.recordedAudioTimeTextView.setTextSize(1, 13.0f);
        this.recordedAudioPanel.addView((View)this.recordedAudioTimeTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 13.0f, 0.0f));
        (this.recordPanel = new FrameLayout((Context)parentActivity)).setVisibility(8);
        this.recordPanel.setBackgroundColor(Theme.getColor("chat_messagePanelBackground"));
        frameLayout.addView((View)this.recordPanel, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 80));
        this.recordPanel.setOnTouchListener((View$OnTouchListener)_$$Lambda$ChatActivityEnterView$NTG78ehgN82Ciqu_59BpKprJO6c.INSTANCE);
        (this.slideText = new LinearLayout((Context)parentActivity)).setOrientation(0);
        this.recordPanel.addView((View)this.slideText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 17, 30.0f, 0.0f, 0.0f, 0.0f));
        (this.recordCancelImage = new ImageView((Context)parentActivity)).setImageResource(2131165829);
        this.recordCancelImage.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_recordVoiceCancel"), PorterDuff$Mode.MULTIPLY));
        this.slideText.addView((View)this.recordCancelImage, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 16, 0, 1, 0, 0));
        (this.recordCancelText = new TextView((Context)parentActivity)).setText((CharSequence)LocaleController.getString("SlideToCancel", 2131560787));
        this.recordCancelText.setTextColor(Theme.getColor("chat_recordVoiceCancel"));
        this.recordCancelText.setTextSize(1, 12.0f);
        this.slideText.addView((View)this.recordCancelText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 16, 6, 0, 0, 0));
        (this.recordSendText = new TextView((Context)parentActivity)).setText((CharSequence)LocaleController.getString("Cancel", 2131558891).toUpperCase());
        this.recordSendText.setTextColor(Theme.getColor("chat_fieldOverlayText"));
        this.recordSendText.setTextSize(1, 16.0f);
        this.recordSendText.setGravity(17);
        this.recordSendText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.recordSendText.setAlpha(0.0f);
        this.recordSendText.setPadding(AndroidUtilities.dp(36.0f), 0, 0, 0);
        this.recordSendText.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatActivityEnterView$yC9IrZCKz9RgoNa9k7GyjXp1EoM(this));
        this.recordPanel.addView((View)this.recordSendText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, 49, 0.0f, 0.0f, 0.0f, 0.0f));
        (this.recordTimeContainer = new LinearLayout((Context)parentActivity)).setOrientation(0);
        this.recordTimeContainer.setPadding(AndroidUtilities.dp(13.0f), 0, 0, 0);
        this.recordTimeContainer.setBackgroundColor(Theme.getColor("chat_messagePanelBackground"));
        this.recordPanel.addView((View)this.recordTimeContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 16));
        this.recordDot = new RecordDot((Context)parentActivity);
        this.recordTimeContainer.addView((View)this.recordDot, (ViewGroup$LayoutParams)LayoutHelper.createLinear(11, 11, 16, 0, 1, 0, 0));
        (this.recordTimeText = new TextView((Context)parentActivity)).setTextColor(Theme.getColor("chat_recordTime"));
        this.recordTimeText.setTextSize(1, 16.0f);
        this.recordTimeContainer.addView((View)this.recordTimeText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 16, 6, 0, 0, 0));
        this.sendButtonContainer = new FrameLayout((Context)parentActivity);
        this.textFieldContainer.addView((View)this.sendButtonContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(48, 48, 80));
        (this.audioVideoButtonContainer = new FrameLayout((Context)parentActivity)).setBackgroundColor(Theme.getColor("chat_messagePanelBackground"));
        this.audioVideoButtonContainer.setSoundEffectsEnabled(false);
        this.sendButtonContainer.addView((View)this.audioVideoButtonContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f));
        this.audioVideoButtonContainer.setOnTouchListener((View$OnTouchListener)new _$$Lambda$ChatActivityEnterView$ilTU_PI6Smic3T39rzhm2HGqiUo(this));
        (this.audioSendButton = new ImageView((Context)parentActivity)).setScaleType(ImageView$ScaleType.CENTER_INSIDE);
        this.audioSendButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), PorterDuff$Mode.MULTIPLY));
        this.audioSendButton.setImageResource(2131165488);
        this.audioSendButton.setPadding(0, 0, AndroidUtilities.dp(4.0f), 0);
        this.audioSendButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrVoiceMessage", 2131558483));
        this.audioSendButton.setFocusable(true);
        this.audioSendButton.setAccessibilityDelegate(this.mediaMessageButtonsDelegate);
        this.audioVideoButtonContainer.addView((View)this.audioSendButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f));
        if (b) {
            (this.videoSendButton = new ImageView((Context)parentActivity)).setScaleType(ImageView$ScaleType.CENTER_INSIDE);
            this.videoSendButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), PorterDuff$Mode.MULTIPLY));
            this.videoSendButton.setImageResource(2131165494);
            this.videoSendButton.setPadding(0, 0, AndroidUtilities.dp(4.0f), 0);
            this.videoSendButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrVideoMessage", 2131558481));
            this.videoSendButton.setFocusable(true);
            this.videoSendButton.setAccessibilityDelegate(this.mediaMessageButtonsDelegate);
            this.audioVideoButtonContainer.addView((View)this.videoSendButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f));
        }
        (this.recordCircle = new RecordCircle((Context)parentActivity)).setVisibility(8);
        this.sizeNotifierLayout.addView((View)this.recordCircle, (ViewGroup$LayoutParams)LayoutHelper.createFrame(124, 194.0f, 85, 0.0f, 0.0f, -36.0f, 0.0f));
        (this.cancelBotButton = new ImageView((Context)parentActivity)).setVisibility(4);
        this.cancelBotButton.setScaleType(ImageView$ScaleType.CENTER_INSIDE);
        this.cancelBotButton.setImageDrawable((Drawable)(this.progressDrawable = new CloseProgressDrawable2()));
        this.cancelBotButton.setContentDescription((CharSequence)LocaleController.getString("Cancel", 2131558891));
        this.progressDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelCancelInlineBot"), PorterDuff$Mode.MULTIPLY));
        this.cancelBotButton.setSoundEffectsEnabled(false);
        this.cancelBotButton.setScaleX(0.1f);
        this.cancelBotButton.setScaleY(0.1f);
        this.cancelBotButton.setAlpha(0.0f);
        this.sendButtonContainer.addView((View)this.cancelBotButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f));
        this.cancelBotButton.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatActivityEnterView$5SWSfIIIolSi29LVurkXFLuveaY(this));
        (this.sendButton = new ImageView((Context)parentActivity)).setVisibility(4);
        this.sendButton.setScaleType(ImageView$ScaleType.CENTER_INSIDE);
        this.sendButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelSend"), PorterDuff$Mode.MULTIPLY));
        this.sendButton.setImageResource(2131165468);
        this.sendButton.setContentDescription((CharSequence)LocaleController.getString("Send", 2131560685));
        this.sendButton.setSoundEffectsEnabled(false);
        this.sendButton.setScaleX(0.1f);
        this.sendButton.setScaleY(0.1f);
        this.sendButton.setAlpha(0.0f);
        this.sendButtonContainer.addView((View)this.sendButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f));
        this.sendButton.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatActivityEnterView$LT79Icaff_VcrkpB6XfMaR17p9I(this));
        (this.expandStickersButton = new ImageView((Context)parentActivity)).setPadding(0, 0, AndroidUtilities.dp(4.0f), 0);
        this.expandStickersButton.setScaleType(ImageView$ScaleType.CENTER);
        this.expandStickersButton.setImageDrawable((Drawable)(this.stickersArrow = new AnimatedArrowDrawable(Theme.getColor("chat_messagePanelIcons"), false)));
        this.expandStickersButton.setVisibility(8);
        this.expandStickersButton.setScaleX(0.1f);
        this.expandStickersButton.setScaleY(0.1f);
        this.expandStickersButton.setAlpha(0.0f);
        this.sendButtonContainer.addView((View)this.expandStickersButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f));
        this.expandStickersButton.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatActivityEnterView$BuL8MDh0vQ9QIU2SBmKOQL4e5dE(this));
        this.expandStickersButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrExpandPanel", 2131558432));
        (this.doneButtonContainer = new FrameLayout((Context)parentActivity)).setVisibility(8);
        this.textFieldContainer.addView((View)this.doneButtonContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(48, 48, 80));
        this.doneButtonContainer.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatActivityEnterView$k84iF5Zh9bonq8mHpikPhsUxJO4(this));
        final Drawable circleDrawable = Theme.createCircleDrawable(AndroidUtilities.dp(16.0f), Theme.getColor("chat_messagePanelSend"));
        final Drawable mutate = parentActivity.getResources().getDrawable(2131165484).mutate();
        mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelBackground"), PorterDuff$Mode.MULTIPLY));
        final CombinedDrawable imageDrawable = new CombinedDrawable(circleDrawable, mutate, 0, AndroidUtilities.dp(1.0f));
        imageDrawable.setCustomSize(AndroidUtilities.dp(32.0f), AndroidUtilities.dp(32.0f));
        (this.doneButtonImage = new ImageView((Context)parentActivity)).setScaleType(ImageView$ScaleType.CENTER);
        this.doneButtonImage.setImageDrawable((Drawable)imageDrawable);
        this.doneButtonImage.setContentDescription((CharSequence)LocaleController.getString("Done", 2131559299));
        this.doneButtonContainer.addView((View)this.doneButtonImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f));
        (this.doneButtonProgress = new ContextProgressView((Context)parentActivity, 0)).setVisibility(4);
        this.doneButtonContainer.addView((View)this.doneButtonProgress, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        final SharedPreferences globalEmojiSettings = MessagesController.getGlobalEmojiSettings();
        this.keyboardHeight = globalEmojiSettings.getInt("kbd_height", AndroidUtilities.dp(200.0f));
        this.keyboardHeightLand = globalEmojiSettings.getInt("kbd_height_land3", AndroidUtilities.dp(200.0f));
        this.setRecordVideoButtonVisible(false, false);
        this.checkSendButton(false);
        this.checkChannelRights();
    }
    
    private void checkSendButton(boolean b) {
        if (this.editingMessageObject != null) {
            return;
        }
        if (this.isPaused) {
            b = false;
        }
        if (AndroidUtilities.getTrimmedString((CharSequence)this.messageEditText.getText()).length() <= 0 && !this.forceShowSendButton && this.audioToSend == null && this.videoToSendMessageObject == null) {
            if (this.emojiView != null && this.emojiViewVisible && (this.stickersTabOpen || (this.emojiTabOpen && this.searchingType == 2)) && !AndroidUtilities.isInMultiwindow) {
                if (b) {
                    if (this.runningAnimationType == 4) {
                        return;
                    }
                    final AnimatorSet runningAnimation = this.runningAnimation;
                    if (runningAnimation != null) {
                        runningAnimation.cancel();
                        this.runningAnimation = null;
                    }
                    final AnimatorSet runningAnimation2 = this.runningAnimation2;
                    if (runningAnimation2 != null) {
                        runningAnimation2.cancel();
                        this.runningAnimation2 = null;
                    }
                    final LinearLayout attachLayout = this.attachLayout;
                    if (attachLayout != null) {
                        attachLayout.setVisibility(0);
                        (this.runningAnimation2 = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.attachLayout, View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.attachLayout, View.SCALE_X, new float[] { 1.0f }) });
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
                    final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
                    list.add(ObjectAnimator.ofFloat((Object)this.expandStickersButton, View.SCALE_X, new float[] { 1.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)this.expandStickersButton, View.SCALE_Y, new float[] { 1.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)this.expandStickersButton, View.ALPHA, new float[] { 1.0f }));
                    if (this.cancelBotButton.getVisibility() == 0) {
                        list.add(ObjectAnimator.ofFloat((Object)this.cancelBotButton, View.SCALE_X, new float[] { 0.1f }));
                        list.add(ObjectAnimator.ofFloat((Object)this.cancelBotButton, View.SCALE_Y, new float[] { 0.1f }));
                        list.add(ObjectAnimator.ofFloat((Object)this.cancelBotButton, View.ALPHA, new float[] { 0.0f }));
                    }
                    else if (this.audioVideoButtonContainer.getVisibility() == 0) {
                        list.add(ObjectAnimator.ofFloat((Object)this.audioVideoButtonContainer, View.SCALE_X, new float[] { 0.1f }));
                        list.add(ObjectAnimator.ofFloat((Object)this.audioVideoButtonContainer, View.SCALE_Y, new float[] { 0.1f }));
                        list.add(ObjectAnimator.ofFloat((Object)this.audioVideoButtonContainer, View.ALPHA, new float[] { 0.0f }));
                    }
                    else {
                        list.add(ObjectAnimator.ofFloat((Object)this.sendButton, View.SCALE_X, new float[] { 0.1f }));
                        list.add(ObjectAnimator.ofFloat((Object)this.sendButton, View.SCALE_Y, new float[] { 0.1f }));
                        list.add(ObjectAnimator.ofFloat((Object)this.sendButton, View.ALPHA, new float[] { 0.0f }));
                    }
                    this.runningAnimation.playTogether((Collection)list);
                    this.runningAnimation.setDuration(150L);
                    this.runningAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationCancel(final Animator obj) {
                            if (ChatActivityEnterView.this.runningAnimation != null && ChatActivityEnterView.this.runningAnimation.equals(obj)) {
                                ChatActivityEnterView.this.runningAnimation = null;
                            }
                        }
                        
                        public void onAnimationEnd(final Animator obj) {
                            if (ChatActivityEnterView.this.runningAnimation != null && ChatActivityEnterView.this.runningAnimation.equals(obj)) {
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
                }
                else {
                    this.sendButton.setScaleX(0.1f);
                    this.sendButton.setScaleY(0.1f);
                    this.sendButton.setAlpha(0.0f);
                    this.cancelBotButton.setScaleX(0.1f);
                    this.cancelBotButton.setScaleY(0.1f);
                    this.cancelBotButton.setAlpha(0.0f);
                    this.audioVideoButtonContainer.setScaleX(0.1f);
                    this.audioVideoButtonContainer.setScaleY(0.1f);
                    this.audioVideoButtonContainer.setAlpha(0.0f);
                    this.expandStickersButton.setScaleX(1.0f);
                    this.expandStickersButton.setScaleY(1.0f);
                    this.expandStickersButton.setAlpha(1.0f);
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
            }
            else if (this.sendButton.getVisibility() == 0 || this.cancelBotButton.getVisibility() == 0 || this.expandStickersButton.getVisibility() == 0) {
                if (b) {
                    if (this.runningAnimationType == 2) {
                        return;
                    }
                    final AnimatorSet runningAnimation3 = this.runningAnimation;
                    if (runningAnimation3 != null) {
                        runningAnimation3.cancel();
                        this.runningAnimation = null;
                    }
                    final AnimatorSet runningAnimation4 = this.runningAnimation2;
                    if (runningAnimation4 != null) {
                        runningAnimation4.cancel();
                        this.runningAnimation2 = null;
                    }
                    final LinearLayout attachLayout2 = this.attachLayout;
                    if (attachLayout2 != null) {
                        attachLayout2.setVisibility(0);
                        (this.runningAnimation2 = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.attachLayout, View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.attachLayout, View.SCALE_X, new float[] { 1.0f }) });
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
                    final ArrayList<ObjectAnimator> list2 = new ArrayList<ObjectAnimator>();
                    list2.add(ObjectAnimator.ofFloat((Object)this.audioVideoButtonContainer, View.SCALE_X, new float[] { 1.0f }));
                    list2.add(ObjectAnimator.ofFloat((Object)this.audioVideoButtonContainer, View.SCALE_Y, new float[] { 1.0f }));
                    list2.add(ObjectAnimator.ofFloat((Object)this.audioVideoButtonContainer, View.ALPHA, new float[] { 1.0f }));
                    if (this.cancelBotButton.getVisibility() == 0) {
                        list2.add(ObjectAnimator.ofFloat((Object)this.cancelBotButton, View.SCALE_X, new float[] { 0.1f }));
                        list2.add(ObjectAnimator.ofFloat((Object)this.cancelBotButton, View.SCALE_Y, new float[] { 0.1f }));
                        list2.add(ObjectAnimator.ofFloat((Object)this.cancelBotButton, View.ALPHA, new float[] { 0.0f }));
                    }
                    else if (this.expandStickersButton.getVisibility() == 0) {
                        list2.add(ObjectAnimator.ofFloat((Object)this.expandStickersButton, View.SCALE_X, new float[] { 0.1f }));
                        list2.add(ObjectAnimator.ofFloat((Object)this.expandStickersButton, View.SCALE_Y, new float[] { 0.1f }));
                        list2.add(ObjectAnimator.ofFloat((Object)this.expandStickersButton, View.ALPHA, new float[] { 0.0f }));
                    }
                    else {
                        list2.add(ObjectAnimator.ofFloat((Object)this.sendButton, View.SCALE_X, new float[] { 0.1f }));
                        list2.add(ObjectAnimator.ofFloat((Object)this.sendButton, View.SCALE_Y, new float[] { 0.1f }));
                        list2.add(ObjectAnimator.ofFloat((Object)this.sendButton, View.ALPHA, new float[] { 0.0f }));
                    }
                    this.runningAnimation.playTogether((Collection)list2);
                    this.runningAnimation.setDuration(150L);
                    this.runningAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationCancel(final Animator obj) {
                            if (ChatActivityEnterView.this.runningAnimation != null && ChatActivityEnterView.this.runningAnimation.equals(obj)) {
                                ChatActivityEnterView.this.runningAnimation = null;
                            }
                        }
                        
                        public void onAnimationEnd(final Animator obj) {
                            if (ChatActivityEnterView.this.runningAnimation != null && ChatActivityEnterView.this.runningAnimation.equals(obj)) {
                                ChatActivityEnterView.this.sendButton.setVisibility(8);
                                ChatActivityEnterView.this.cancelBotButton.setVisibility(8);
                                ChatActivityEnterView.this.audioVideoButtonContainer.setVisibility(0);
                                ChatActivityEnterView.this.runningAnimation = null;
                                ChatActivityEnterView.this.runningAnimationType = 0;
                            }
                        }
                    });
                    this.runningAnimation.start();
                }
                else {
                    this.sendButton.setScaleX(0.1f);
                    this.sendButton.setScaleY(0.1f);
                    this.sendButton.setAlpha(0.0f);
                    this.cancelBotButton.setScaleX(0.1f);
                    this.cancelBotButton.setScaleY(0.1f);
                    this.cancelBotButton.setAlpha(0.0f);
                    this.expandStickersButton.setScaleX(0.1f);
                    this.expandStickersButton.setScaleY(0.1f);
                    this.expandStickersButton.setAlpha(0.0f);
                    this.audioVideoButtonContainer.setScaleX(1.0f);
                    this.audioVideoButtonContainer.setScaleY(1.0f);
                    this.audioVideoButtonContainer.setAlpha(1.0f);
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
        }
        else {
            final String caption = this.messageEditText.getCaption();
            final boolean b2 = caption != null && (this.sendButton.getVisibility() == 0 || this.expandStickersButton.getVisibility() == 0);
            final boolean b3 = caption == null && (this.cancelBotButton.getVisibility() == 0 || this.expandStickersButton.getVisibility() == 0);
            if (this.audioVideoButtonContainer.getVisibility() == 0 || b2 || b3) {
                if (b) {
                    if ((this.runningAnimationType == 1 && this.messageEditText.getCaption() == null) || (this.runningAnimationType == 3 && caption != null)) {
                        return;
                    }
                    final AnimatorSet runningAnimation5 = this.runningAnimation;
                    if (runningAnimation5 != null) {
                        runningAnimation5.cancel();
                        this.runningAnimation = null;
                    }
                    final AnimatorSet runningAnimation6 = this.runningAnimation2;
                    if (runningAnimation6 != null) {
                        runningAnimation6.cancel();
                        this.runningAnimation2 = null;
                    }
                    if (this.attachLayout != null) {
                        (this.runningAnimation2 = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.attachLayout, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.attachLayout, View.SCALE_X, new float[] { 0.0f }) });
                        this.runningAnimation2.setDuration(100L);
                        this.runningAnimation2.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationCancel(final Animator obj) {
                                if (ChatActivityEnterView.this.runningAnimation2 != null && ChatActivityEnterView.this.runningAnimation2.equals(obj)) {
                                    ChatActivityEnterView.this.runningAnimation2 = null;
                                }
                            }
                            
                            public void onAnimationEnd(final Animator obj) {
                                if (ChatActivityEnterView.this.runningAnimation2 != null && ChatActivityEnterView.this.runningAnimation2.equals(obj)) {
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
                    final ArrayList<ObjectAnimator> list3 = new ArrayList<ObjectAnimator>();
                    if (this.audioVideoButtonContainer.getVisibility() == 0) {
                        list3.add(ObjectAnimator.ofFloat((Object)this.audioVideoButtonContainer, View.SCALE_X, new float[] { 0.1f }));
                        list3.add(ObjectAnimator.ofFloat((Object)this.audioVideoButtonContainer, View.SCALE_Y, new float[] { 0.1f }));
                        list3.add(ObjectAnimator.ofFloat((Object)this.audioVideoButtonContainer, View.ALPHA, new float[] { 0.0f }));
                    }
                    if (this.expandStickersButton.getVisibility() == 0) {
                        list3.add(ObjectAnimator.ofFloat((Object)this.expandStickersButton, View.SCALE_X, new float[] { 0.1f }));
                        list3.add(ObjectAnimator.ofFloat((Object)this.expandStickersButton, View.SCALE_Y, new float[] { 0.1f }));
                        list3.add(ObjectAnimator.ofFloat((Object)this.expandStickersButton, View.ALPHA, new float[] { 0.0f }));
                    }
                    if (b2) {
                        list3.add(ObjectAnimator.ofFloat((Object)this.sendButton, View.SCALE_X, new float[] { 0.1f }));
                        list3.add(ObjectAnimator.ofFloat((Object)this.sendButton, View.SCALE_Y, new float[] { 0.1f }));
                        list3.add(ObjectAnimator.ofFloat((Object)this.sendButton, View.ALPHA, new float[] { 0.0f }));
                    }
                    else if (b3) {
                        list3.add(ObjectAnimator.ofFloat((Object)this.cancelBotButton, View.SCALE_X, new float[] { 0.1f }));
                        list3.add(ObjectAnimator.ofFloat((Object)this.cancelBotButton, View.SCALE_Y, new float[] { 0.1f }));
                        list3.add(ObjectAnimator.ofFloat((Object)this.cancelBotButton, View.ALPHA, new float[] { 0.0f }));
                    }
                    if (caption != null) {
                        this.runningAnimationType = 3;
                        list3.add(ObjectAnimator.ofFloat((Object)this.cancelBotButton, View.SCALE_X, new float[] { 1.0f }));
                        list3.add(ObjectAnimator.ofFloat((Object)this.cancelBotButton, View.SCALE_Y, new float[] { 1.0f }));
                        list3.add(ObjectAnimator.ofFloat((Object)this.cancelBotButton, View.ALPHA, new float[] { 1.0f }));
                        this.cancelBotButton.setVisibility(0);
                    }
                    else {
                        this.runningAnimationType = 1;
                        list3.add(ObjectAnimator.ofFloat((Object)this.sendButton, View.SCALE_X, new float[] { 1.0f }));
                        list3.add(ObjectAnimator.ofFloat((Object)this.sendButton, View.SCALE_Y, new float[] { 1.0f }));
                        list3.add(ObjectAnimator.ofFloat((Object)this.sendButton, View.ALPHA, new float[] { 1.0f }));
                        this.sendButton.setVisibility(0);
                    }
                    this.runningAnimation.playTogether((Collection)list3);
                    this.runningAnimation.setDuration(150L);
                    this.runningAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationCancel(final Animator obj) {
                            if (ChatActivityEnterView.this.runningAnimation != null && ChatActivityEnterView.this.runningAnimation.equals(obj)) {
                                ChatActivityEnterView.this.runningAnimation = null;
                            }
                        }
                        
                        public void onAnimationEnd(final Animator obj) {
                            if (ChatActivityEnterView.this.runningAnimation != null && ChatActivityEnterView.this.runningAnimation.equals(obj)) {
                                if (caption != null) {
                                    ChatActivityEnterView.this.cancelBotButton.setVisibility(0);
                                    ChatActivityEnterView.this.sendButton.setVisibility(8);
                                }
                                else {
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
                else {
                    this.audioVideoButtonContainer.setScaleX(0.1f);
                    this.audioVideoButtonContainer.setScaleY(0.1f);
                    this.audioVideoButtonContainer.setAlpha(0.0f);
                    if (caption != null) {
                        this.sendButton.setScaleX(0.1f);
                        this.sendButton.setScaleY(0.1f);
                        this.sendButton.setAlpha(0.0f);
                        this.cancelBotButton.setScaleX(1.0f);
                        this.cancelBotButton.setScaleY(1.0f);
                        this.cancelBotButton.setAlpha(1.0f);
                        this.cancelBotButton.setVisibility(0);
                        this.sendButton.setVisibility(8);
                    }
                    else {
                        this.cancelBotButton.setScaleX(0.1f);
                        this.cancelBotButton.setScaleY(0.1f);
                        this.cancelBotButton.setAlpha(0.0f);
                        this.sendButton.setScaleX(1.0f);
                        this.sendButton.setScaleY(1.0f);
                        this.sendButton.setAlpha(1.0f);
                        this.sendButton.setVisibility(0);
                        this.cancelBotButton.setVisibility(8);
                    }
                    this.audioVideoButtonContainer.setVisibility(8);
                    final LinearLayout attachLayout3 = this.attachLayout;
                    if (attachLayout3 != null) {
                        attachLayout3.setVisibility(8);
                        if (this.delegate != null && this.getVisibility() == 0) {
                            this.delegate.onAttachButtonHidden();
                        }
                        this.updateFieldRight(0);
                    }
                }
            }
        }
    }
    
    private void checkStickresExpandHeight() {
        final Point displaySize = AndroidUtilities.displaySize;
        int n;
        if (displaySize.x > displaySize.y) {
            n = this.keyboardHeightLand;
        }
        else {
            n = this.keyboardHeight;
        }
        final int originalViewHeight = this.originalViewHeight;
        int statusBarHeight;
        if (Build$VERSION.SDK_INT >= 21) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        int min;
        final int a = min = originalViewHeight - statusBarHeight - ActionBar.getCurrentActionBarHeight() - this.getHeight() + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
        if (this.searchingType == 2) {
            min = Math.min(a, AndroidUtilities.dp(120.0f) + n);
        }
        final int height = this.emojiView.getLayoutParams().height;
        if (height == min) {
            return;
        }
        final Animator stickersExpansionAnim = this.stickersExpansionAnim;
        if (stickersExpansionAnim != null) {
            stickersExpansionAnim.cancel();
            this.stickersExpansionAnim = null;
        }
        if (height > (this.stickersExpandedHeight = min)) {
            final AnimatorSet stickersExpansionAnim2 = new AnimatorSet();
            stickersExpansionAnim2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofInt((Object)this, (Property)this.roundedTranslationYProperty, new int[] { -(this.stickersExpandedHeight - n) }), (Animator)ObjectAnimator.ofInt((Object)this.emojiView, (Property)this.roundedTranslationYProperty, new int[] { -(this.stickersExpandedHeight - n) }) });
            ((ObjectAnimator)stickersExpansionAnim2.getChildAnimations().get(0)).addUpdateListener((ValueAnimator$AnimatorUpdateListener)new _$$Lambda$ChatActivityEnterView$wjaxKxy8o6FENKkopnAtGTac1QI(this));
            stickersExpansionAnim2.setDuration(400L);
            stickersExpansionAnim2.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT_QUINT);
            stickersExpansionAnim2.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    ChatActivityEnterView.this.stickersExpansionAnim = null;
                    if (ChatActivityEnterView.this.emojiView != null) {
                        ChatActivityEnterView.this.emojiView.getLayoutParams().height = ChatActivityEnterView.this.stickersExpandedHeight;
                        ChatActivityEnterView.this.emojiView.setLayerType(0, (Paint)null);
                    }
                }
            });
            this.stickersExpansionAnim = (Animator)stickersExpansionAnim2;
            this.emojiView.setLayerType(2, (Paint)null);
            stickersExpansionAnim2.start();
        }
        else {
            this.emojiView.getLayoutParams().height = this.stickersExpandedHeight;
            this.sizeNotifierLayout.requestLayout();
            final int selectionStart = this.messageEditText.getSelectionStart();
            final int selectionEnd = this.messageEditText.getSelectionEnd();
            final EditTextCaption messageEditText = this.messageEditText;
            messageEditText.setText((CharSequence)messageEditText.getText());
            this.messageEditText.setSelection(selectionStart, selectionEnd);
            final AnimatorSet stickersExpansionAnim3 = new AnimatorSet();
            stickersExpansionAnim3.playTogether(new Animator[] { (Animator)ObjectAnimator.ofInt((Object)this, (Property)this.roundedTranslationYProperty, new int[] { -(this.stickersExpandedHeight - n) }), (Animator)ObjectAnimator.ofInt((Object)this.emojiView, (Property)this.roundedTranslationYProperty, new int[] { -(this.stickersExpandedHeight - n) }) });
            ((ObjectAnimator)stickersExpansionAnim3.getChildAnimations().get(0)).addUpdateListener((ValueAnimator$AnimatorUpdateListener)new _$$Lambda$ChatActivityEnterView$XEk0ExTurPjgeNRTljJZaBiOh9o(this));
            stickersExpansionAnim3.setDuration(400L);
            stickersExpansionAnim3.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT_QUINT);
            stickersExpansionAnim3.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    ChatActivityEnterView.this.stickersExpansionAnim = null;
                    ChatActivityEnterView.this.emojiView.setLayerType(0, (Paint)null);
                }
            });
            this.stickersExpansionAnim = (Animator)stickersExpansionAnim3;
            this.emojiView.setLayerType(2, (Paint)null);
            stickersExpansionAnim3.start();
        }
    }
    
    private void createEmojiView() {
        if (this.emojiView != null) {
            return;
        }
        (this.emojiView = new EmojiView(this.allowStickers, this.allowGifs, (Context)this.parentActivity, true, this.info)).setVisibility(8);
        this.emojiView.setDelegate((EmojiView.EmojiViewDelegate)new EmojiView.EmojiViewDelegate() {
            @Override
            public boolean isExpanded() {
                return ChatActivityEnterView.this.stickersExpanded;
            }
            
            @Override
            public boolean isSearchOpened() {
                return ChatActivityEnterView.this.searchingType != 0;
            }
            
            @Override
            public boolean onBackspace() {
                if (ChatActivityEnterView.this.messageEditText.length() == 0) {
                    return false;
                }
                ChatActivityEnterView.this.messageEditText.dispatchKeyEvent(new KeyEvent(0, 67));
                return true;
            }
            
            @Override
            public void onClearEmojiRecent() {
                if (ChatActivityEnterView.this.parentFragment != null) {
                    if (ChatActivityEnterView.this.parentActivity != null) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)ChatActivityEnterView.this.parentActivity);
                        builder.setTitle(LocaleController.getString("AppName", 2131558635));
                        builder.setMessage(LocaleController.getString("ClearRecentEmoji", 2131559113));
                        builder.setPositiveButton(LocaleController.getString("ClearButton", 2131559102).toUpperCase(), (DialogInterface$OnClickListener)new _$$Lambda$ChatActivityEnterView$26$SN56de_QEsvpP_TQQjotkzhHl_U(this));
                        builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                        ChatActivityEnterView.this.parentFragment.showDialog(builder.create());
                    }
                }
            }
            
            @Override
            public void onEmojiSelected(final String s) {
                Label_0019: {
                    int selectionEnd;
                    if ((selectionEnd = ChatActivityEnterView.this.messageEditText.getSelectionEnd()) >= 0) {
                        break Label_0019;
                    }
                    selectionEnd = 0;
                    try {
                        try {
                            ChatActivityEnterView.this.innerTextChange = 2;
                            final CharSequence replaceEmoji = Emoji.replaceEmoji(s, ChatActivityEnterView.this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                            ChatActivityEnterView.this.messageEditText.setText((CharSequence)ChatActivityEnterView.this.messageEditText.getText().insert(selectionEnd, replaceEmoji));
                            final int n = selectionEnd + replaceEmoji.length();
                            ChatActivityEnterView.this.messageEditText.setSelection(n, n);
                        }
                        finally {}
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
                ChatActivityEnterView.this.innerTextChange = 0;
                return;
                ChatActivityEnterView.this.innerTextChange = 0;
            }
            
            @Override
            public void onGifSelected(final Object o, final Object o2) {
                if (ChatActivityEnterView.this.stickersExpanded) {
                    if (ChatActivityEnterView.this.searchingType != 0) {
                        ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                    }
                    ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                }
                if (o instanceof TLRPC.Document) {
                    final TLRPC.Document document = (TLRPC.Document)o;
                    SendMessagesHelper.getInstance(ChatActivityEnterView.this.currentAccount).sendSticker(document, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, o2);
                    DataQuery.getInstance(ChatActivityEnterView.this.currentAccount).addRecentGif(document, (int)(System.currentTimeMillis() / 1000L));
                    if ((int)ChatActivityEnterView.this.dialog_id == 0) {
                        MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).saveGif(o2, document);
                    }
                }
                else if (o instanceof TLRPC.BotInlineResult) {
                    final TLRPC.BotInlineResult botInlineResult = (TLRPC.BotInlineResult)o;
                    if (botInlineResult.document != null) {
                        DataQuery.getInstance(ChatActivityEnterView.this.currentAccount).addRecentGif(botInlineResult.document, (int)(System.currentTimeMillis() / 1000L));
                        if ((int)ChatActivityEnterView.this.dialog_id == 0) {
                            MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).saveGif(o2, botInlineResult.document);
                        }
                    }
                    final TLRPC.User user = (TLRPC.User)o2;
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("id", botInlineResult.id);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(botInlineResult.query_id);
                    hashMap.put("query_id", sb.toString());
                    SendMessagesHelper.prepareSendingBotContextResult(botInlineResult, hashMap, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject);
                    if (ChatActivityEnterView.this.searchingType != 0) {
                        ChatActivityEnterView.this.searchingType = 0;
                        ChatActivityEnterView.this.emojiView.closeSearch(true);
                        ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                    }
                }
                if (ChatActivityEnterView.this.delegate != null) {
                    ChatActivityEnterView.this.delegate.onMessageSend(null);
                }
            }
            
            @Override
            public void onSearchOpenClose(final int n) {
                ChatActivityEnterView.this.searchingType = n;
                ChatActivityEnterView.this.setStickersExpanded(n != 0, false, false);
                if (ChatActivityEnterView.this.emojiTabOpen && ChatActivityEnterView.this.searchingType == 2) {
                    ChatActivityEnterView.this.checkStickresExpandHeight();
                }
            }
            
            @Override
            public void onShowStickerSet(final TLRPC.StickerSet set, TLRPC.InputStickerSet set2) {
                if (ChatActivityEnterView.this.parentFragment != null) {
                    if (ChatActivityEnterView.this.parentActivity != null) {
                        if (set != null) {
                            set2 = new TLRPC.TL_inputStickerSetID();
                            set2.access_hash = set.access_hash;
                            set2.id = set.id;
                        }
                        ChatActivityEnterView.this.parentFragment.showDialog(new StickersAlert((Context)ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.parentFragment, set2, null, (StickersAlert.StickersAlertDelegate)ChatActivityEnterView.this));
                    }
                }
            }
            
            @Override
            public void onStickerSelected(final TLRPC.Document document, final Object o) {
                if (ChatActivityEnterView.this.stickersExpanded) {
                    if (ChatActivityEnterView.this.searchingType != 0) {
                        ChatActivityEnterView.this.searchingType = 0;
                        ChatActivityEnterView.this.emojiView.closeSearch(true, MessageObject.getStickerSetId(document));
                        ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                    }
                    ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                }
                ChatActivityEnterView.this.onStickerSelected(document, o, false);
                if ((int)ChatActivityEnterView.this.dialog_id == 0 && MessageObject.isGifDocument(document)) {
                    MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).saveGif(o, document);
                }
            }
            
            @Override
            public void onStickerSetAdd(final TLRPC.StickerSetCovered stickerSetCovered) {
                DataQuery.getInstance(ChatActivityEnterView.this.currentAccount).removeStickersSet((Context)ChatActivityEnterView.this.parentActivity, stickerSetCovered.set, 2, ChatActivityEnterView.this.parentFragment, false);
            }
            
            @Override
            public void onStickerSetRemove(final TLRPC.StickerSetCovered stickerSetCovered) {
                DataQuery.getInstance(ChatActivityEnterView.this.currentAccount).removeStickersSet((Context)ChatActivityEnterView.this.parentActivity, stickerSetCovered.set, 0, ChatActivityEnterView.this.parentFragment, false);
            }
            
            @Override
            public void onStickersGroupClick(final int n) {
                if (ChatActivityEnterView.this.parentFragment != null) {
                    if (AndroidUtilities.isTablet()) {
                        ChatActivityEnterView.this.hidePopup(false);
                    }
                    final GroupStickersActivity groupStickersActivity = new GroupStickersActivity(n);
                    groupStickersActivity.setInfo(ChatActivityEnterView.this.info);
                    ChatActivityEnterView.this.parentFragment.presentFragment(groupStickersActivity);
                }
            }
            
            @Override
            public void onStickersSettingsClick() {
                if (ChatActivityEnterView.this.parentFragment != null) {
                    ChatActivityEnterView.this.parentFragment.presentFragment(new StickersActivity(0));
                }
            }
            
            @Override
            public void onTabOpened(final int n) {
                ChatActivityEnterView.this.delegate.onStickersTab(n == 3);
                final ChatActivityEnterView this$0 = ChatActivityEnterView.this;
                this$0.post(this$0.updateExpandabilityRunnable);
            }
        });
        this.emojiView.setDragListener((EmojiView.DragListener)new EmojiView.DragListener() {
            int initialOffset;
            boolean wasExpanded;
            
            private boolean allowDragging() {
                return ChatActivityEnterView.this.stickersTabOpen && (ChatActivityEnterView.this.stickersExpanded || ChatActivityEnterView.this.messageEditText.length() <= 0) && ChatActivityEnterView.this.emojiView.areThereAnyStickers();
            }
            
            @Override
            public void onDrag(int max) {
                if (!this.allowDragging()) {
                    return;
                }
                final Point displaySize = AndroidUtilities.displaySize;
                int n;
                if (displaySize.x > displaySize.y) {
                    n = ChatActivityEnterView.this.keyboardHeightLand;
                }
                else {
                    n = ChatActivityEnterView.this.keyboardHeight;
                }
                max = Math.max(Math.min(max + this.initialOffset, 0), -(ChatActivityEnterView.this.stickersExpandedHeight - n));
                final EmojiView access$700 = ChatActivityEnterView.this.emojiView;
                final float n2 = (float)max;
                access$700.setTranslationY(n2);
                ChatActivityEnterView.this.setTranslationY(n2);
                final ChatActivityEnterView this$0 = ChatActivityEnterView.this;
                this$0.stickersExpansionProgress = n2 / -(this$0.stickersExpandedHeight - n);
                ChatActivityEnterView.this.sizeNotifierLayout.invalidate();
            }
            
            @Override
            public void onDragCancel() {
                if (!ChatActivityEnterView.this.stickersTabOpen) {
                    return;
                }
                ChatActivityEnterView.this.stickersDragging = false;
                ChatActivityEnterView.this.setStickersExpanded(this.wasExpanded, true, false);
            }
            
            @Override
            public void onDragEnd(final float n) {
                if (!this.allowDragging()) {
                    return;
                }
                ChatActivityEnterView.this.stickersDragging = false;
                if ((this.wasExpanded && n >= AndroidUtilities.dp(200.0f)) || (!this.wasExpanded && n <= AndroidUtilities.dp(-200.0f)) || (this.wasExpanded && ChatActivityEnterView.this.stickersExpansionProgress <= 0.6f) || (!this.wasExpanded && ChatActivityEnterView.this.stickersExpansionProgress >= 0.4f)) {
                    ChatActivityEnterView.this.setStickersExpanded(this.wasExpanded ^ true, true, true);
                }
                else {
                    ChatActivityEnterView.this.setStickersExpanded(this.wasExpanded, true, true);
                }
            }
            
            @Override
            public void onDragStart() {
                if (!this.allowDragging()) {
                    return;
                }
                if (ChatActivityEnterView.this.stickersExpansionAnim != null) {
                    ChatActivityEnterView.this.stickersExpansionAnim.cancel();
                }
                ChatActivityEnterView.this.stickersDragging = true;
                this.wasExpanded = ChatActivityEnterView.this.stickersExpanded;
                ChatActivityEnterView.this.stickersExpanded = true;
                final ChatActivityEnterView this$0 = ChatActivityEnterView.this;
                final int height = this$0.sizeNotifierLayout.getHeight();
                int statusBarHeight;
                if (Build$VERSION.SDK_INT >= 21) {
                    statusBarHeight = AndroidUtilities.statusBarHeight;
                }
                else {
                    statusBarHeight = 0;
                }
                this$0.stickersExpandedHeight = height - statusBarHeight - ActionBar.getCurrentActionBarHeight() - ChatActivityEnterView.this.getHeight() + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                if (ChatActivityEnterView.this.searchingType == 2) {
                    final ChatActivityEnterView this$2 = ChatActivityEnterView.this;
                    final int access$8400 = this$2.stickersExpandedHeight;
                    final int dp = AndroidUtilities.dp(120.0f);
                    final Point displaySize = AndroidUtilities.displaySize;
                    int n;
                    if (displaySize.x > displaySize.y) {
                        n = ChatActivityEnterView.this.keyboardHeightLand;
                    }
                    else {
                        n = ChatActivityEnterView.this.keyboardHeight;
                    }
                    this$2.stickersExpandedHeight = Math.min(access$8400, dp + n);
                }
                ChatActivityEnterView.this.emojiView.getLayoutParams().height = ChatActivityEnterView.this.stickersExpandedHeight;
                ChatActivityEnterView.this.emojiView.setLayerType(2, (Paint)null);
                ChatActivityEnterView.this.sizeNotifierLayout.requestLayout();
                ChatActivityEnterView.this.sizeNotifierLayout.setForeground((Drawable)new ScrimDrawable());
                this.initialOffset = (int)ChatActivityEnterView.this.getTranslationY();
                if (ChatActivityEnterView.this.delegate != null) {
                    ChatActivityEnterView.this.delegate.onStickersExpandedChange();
                }
            }
        });
        this.sizeNotifierLayout.addView((View)this.emojiView);
        this.checkChannelRights();
    }
    
    private void hideRecordedAudioPanel() {
        this.audioToSendPath = null;
        this.audioToSend = null;
        this.audioToSendMessageObject = null;
        this.videoToSendMessageObject = null;
        this.videoTimelineView.destroy();
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.recordedAudioPanel, View.ALPHA, new float[] { 0.0f }) });
        set.setDuration(200L);
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                ChatActivityEnterView.this.recordedAudioPanel.setVisibility(8);
            }
        });
        set.start();
    }
    
    private void onWindowSizeChanged() {
        int height;
        final int n = height = this.sizeNotifierLayout.getHeight();
        if (!this.keyboardVisible) {
            height = n - this.emojiPadding;
        }
        final ChatActivityEnterViewDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onWindowSizeChanged(height);
        }
        if (this.topView != null) {
            if (height < AndroidUtilities.dp(72.0f) + ActionBar.getCurrentActionBarHeight()) {
                if (this.allowShowTopView) {
                    this.allowShowTopView = false;
                    if (this.needShowTopView) {
                        this.topView.setVisibility(8);
                        this.topLineView.setVisibility(8);
                        this.topLineView.setAlpha(0.0f);
                        this.resizeForTopView(false);
                        final View topView = this.topView;
                        topView.setTranslationY((float)topView.getLayoutParams().height);
                    }
                }
            }
            else if (!this.allowShowTopView) {
                this.allowShowTopView = true;
                if (this.needShowTopView) {
                    this.topView.setVisibility(0);
                    this.topLineView.setVisibility(0);
                    this.topLineView.setAlpha(1.0f);
                    this.resizeForTopView(true);
                    this.topView.setTranslationY(0.0f);
                }
            }
        }
    }
    
    private void openKeyboardInternal() {
        int n;
        if (!AndroidUtilities.usingHardwareInput && !this.isPaused) {
            n = 2;
        }
        else {
            n = 0;
        }
        this.showPopup(n, 0);
        this.messageEditText.requestFocus();
        AndroidUtilities.showKeyboard((View)this.messageEditText);
        if (this.isPaused) {
            this.showKeyboardOnResume = true;
        }
        else if (!AndroidUtilities.usingHardwareInput && !this.keyboardVisible && !AndroidUtilities.isInMultiwindow) {
            this.waitingForKeyboardOpen = true;
            AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
            AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100L);
        }
    }
    
    private void resizeForTopView(final boolean b) {
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.textFieldContainer.getLayoutParams();
        final int dp = AndroidUtilities.dp(2.0f);
        int height;
        if (b) {
            height = this.topView.getLayoutParams().height;
        }
        else {
            height = 0;
        }
        layoutParams.topMargin = dp + height;
        this.textFieldContainer.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        final int dp2 = AndroidUtilities.dp(51.0f);
        int height2;
        if (b) {
            height2 = this.topView.getLayoutParams().height;
        }
        else {
            height2 = 0;
        }
        this.setMinimumHeight(dp2 + height2);
        if (this.stickersExpanded) {
            if (this.searchingType == 0) {
                this.setStickersExpanded(false, true, false);
            }
            else {
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
            return;
        }
        if (this.audioToSend != null) {
            final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null && playingMessageObject == this.audioToSendMessageObject) {
                MediaController.getInstance().cleanupPlayer(true, true);
            }
            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(this.audioToSend, null, this.audioToSendPath, this.dialog_id, this.replyingMessageObject, null, null, null, null, 0, null);
            final ChatActivityEnterViewDelegate delegate = this.delegate;
            if (delegate != null) {
                delegate.onMessageSend(null);
            }
            this.hideRecordedAudioPanel();
            this.checkSendButton(true);
            return;
        }
        final Editable text = this.messageEditText.getText();
        if (this.processSendingText((CharSequence)text)) {
            this.messageEditText.setText((CharSequence)"");
            this.lastTypingTimeSend = 0L;
            final ChatActivityEnterViewDelegate delegate2 = this.delegate;
            if (delegate2 != null) {
                delegate2.onMessageSend((CharSequence)text);
            }
        }
        else if (this.forceShowSendButton) {
            final ChatActivityEnterViewDelegate delegate3 = this.delegate;
            if (delegate3 != null) {
                delegate3.onMessageSend(null);
            }
        }
    }
    
    private void setEmojiButtonImage(final boolean b, final boolean b2) {
        boolean b3 = b2;
        if (b2) {
            b3 = b2;
            if (this.currentEmojiIcon == -1) {
                b3 = false;
            }
        }
        int currentEmojiIcon;
        if (b && this.currentPopupContentType == 0) {
            currentEmojiIcon = 0;
        }
        else {
            final EmojiView emojiView = this.emojiView;
            int n;
            if (emojiView == null) {
                n = MessagesController.getGlobalEmojiSettings().getInt("selected_page", 0);
            }
            else {
                n = emojiView.getCurrentPage();
            }
            if (n != 0 && (this.allowStickers || this.allowGifs)) {
                if (n == 1) {
                    currentEmojiIcon = 2;
                }
                else {
                    currentEmojiIcon = 3;
                }
            }
            else {
                currentEmojiIcon = 1;
            }
        }
        if (this.currentEmojiIcon == currentEmojiIcon) {
            return;
        }
        final AnimatorSet emojiButtonAnimation = this.emojiButtonAnimation;
        Object value = null;
        if (emojiButtonAnimation != null) {
            emojiButtonAnimation.cancel();
            this.emojiButtonAnimation = null;
        }
        if (currentEmojiIcon == 0) {
            final ImageView[] emojiButton = this.emojiButton;
            int n2;
            if (b3) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            emojiButton[n2].setImageResource(2131165487);
        }
        else if (currentEmojiIcon == 1) {
            final ImageView[] emojiButton2 = this.emojiButton;
            int n3;
            if (b3) {
                n3 = 1;
            }
            else {
                n3 = 0;
            }
            emojiButton2[n3].setImageResource(2131165492);
        }
        else if (currentEmojiIcon == 2) {
            final ImageView[] emojiButton3 = this.emojiButton;
            int n4;
            if (b3) {
                n4 = 1;
            }
            else {
                n4 = 0;
            }
            emojiButton3[n4].setImageResource(2131165493);
        }
        else if (currentEmojiIcon == 3) {
            final ImageView[] emojiButton4 = this.emojiButton;
            int n5;
            if (b3) {
                n5 = 1;
            }
            else {
                n5 = 0;
            }
            emojiButton4[n5].setImageResource(2131165486);
        }
        final ImageView[] emojiButton5 = this.emojiButton;
        int n6;
        if (b3) {
            n6 = 1;
        }
        else {
            n6 = 0;
        }
        final ImageView imageView = emojiButton5[n6];
        if (currentEmojiIcon == 2) {
            value = 1;
        }
        imageView.setTag(value);
        this.currentEmojiIcon = currentEmojiIcon;
        if (b3) {
            this.emojiButton[1].setVisibility(0);
            (this.emojiButtonAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.emojiButton[0], View.SCALE_X, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.emojiButton[0], View.SCALE_Y, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.emojiButton[0], View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.emojiButton[1], View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.emojiButton[1], View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.emojiButton[1], View.ALPHA, new float[] { 1.0f }) });
            this.emojiButtonAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    if (animator.equals(ChatActivityEnterView.this.emojiButtonAnimation)) {
                        ChatActivityEnterView.this.emojiButtonAnimation = null;
                        final ImageView imageView = ChatActivityEnterView.this.emojiButton[1];
                        ChatActivityEnterView.this.emojiButton[1] = ChatActivityEnterView.this.emojiButton[0];
                        ChatActivityEnterView.this.emojiButton[0] = imageView;
                        ChatActivityEnterView.this.emojiButton[1].setVisibility(4);
                        ChatActivityEnterView.this.emojiButton[1].setAlpha(0.0f);
                        ChatActivityEnterView.this.emojiButton[1].setScaleX(0.1f);
                        ChatActivityEnterView.this.emojiButton[1].setScaleY(0.1f);
                    }
                }
            });
            this.emojiButtonAnimation.setDuration(150L);
            this.emojiButtonAnimation.start();
        }
    }
    
    private void setRecordVideoButtonVisible(final boolean b, final boolean b2) {
        final ImageView videoSendButton = this.videoSendButton;
        if (videoSendButton == null) {
            return;
        }
        Integer value;
        if (b) {
            value = 1;
        }
        else {
            value = null;
        }
        videoSendButton.setTag((Object)value);
        final AnimatorSet audioVideoButtonAnimation = this.audioVideoButtonAnimation;
        if (audioVideoButtonAnimation != null) {
            audioVideoButtonAnimation.cancel();
            this.audioVideoButtonAnimation = null;
        }
        final float n = 0.0f;
        final float n2 = 0.0f;
        float scaleY = 0.1f;
        if (b2) {
            final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            boolean b3 = false;
            Label_0124: {
                if ((int)this.dialog_id < 0) {
                    final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-(int)this.dialog_id);
                    if (ChatObject.isChannel(chat) && !chat.megagroup) {
                        b3 = true;
                        break Label_0124;
                    }
                }
                b3 = false;
            }
            final SharedPreferences$Editor edit = globalMainSettings.edit();
            String s;
            if (b3) {
                s = "currentModeVideoChannel";
            }
            else {
                s = "currentModeVideo";
            }
            edit.putBoolean(s, b).commit();
            this.audioVideoButtonAnimation = new AnimatorSet();
            final AnimatorSet audioVideoButtonAnimation2 = this.audioVideoButtonAnimation;
            final ImageView videoSendButton2 = this.videoSendButton;
            final Property scale_X = View.SCALE_X;
            float n3;
            if (b) {
                n3 = 1.0f;
            }
            else {
                n3 = 0.1f;
            }
            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)videoSendButton2, scale_X, new float[] { n3 });
            final ImageView videoSendButton3 = this.videoSendButton;
            final Property scale_Y = View.SCALE_Y;
            float n4;
            if (b) {
                n4 = 1.0f;
            }
            else {
                n4 = 0.1f;
            }
            final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)videoSendButton3, scale_Y, new float[] { n4 });
            final ImageView videoSendButton4 = this.videoSendButton;
            final Property alpha = View.ALPHA;
            float n5;
            if (b) {
                n5 = 1.0f;
            }
            else {
                n5 = 0.0f;
            }
            final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)videoSendButton4, alpha, new float[] { n5 });
            final ImageView audioSendButton = this.audioSendButton;
            final Property scale_X2 = View.SCALE_X;
            float n6;
            if (b) {
                n6 = 0.1f;
            }
            else {
                n6 = 1.0f;
            }
            final ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat((Object)audioSendButton, scale_X2, new float[] { n6 });
            final ImageView audioSendButton2 = this.audioSendButton;
            final Property scale_Y2 = View.SCALE_Y;
            if (!b) {
                scaleY = 1.0f;
            }
            final ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat((Object)audioSendButton2, scale_Y2, new float[] { scaleY });
            final ImageView audioSendButton3 = this.audioSendButton;
            final Property alpha2 = View.ALPHA;
            float n7;
            if (b) {
                n7 = n2;
            }
            else {
                n7 = 1.0f;
            }
            audioVideoButtonAnimation2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2, (Animator)ofFloat3, (Animator)ofFloat4, (Animator)ofFloat5, (Animator)ObjectAnimator.ofFloat((Object)audioSendButton3, alpha2, new float[] { n7 }) });
            this.audioVideoButtonAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    if (animator.equals(ChatActivityEnterView.this.audioVideoButtonAnimation)) {
                        ChatActivityEnterView.this.audioVideoButtonAnimation = null;
                    }
                    ImageView imageView;
                    if (ChatActivityEnterView.this.videoSendButton.getTag() == null) {
                        imageView = ChatActivityEnterView.this.audioSendButton;
                    }
                    else {
                        imageView = ChatActivityEnterView.this.videoSendButton;
                    }
                    imageView.sendAccessibilityEvent(8);
                }
            });
            this.audioVideoButtonAnimation.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
            this.audioVideoButtonAnimation.setDuration(150L);
            this.audioVideoButtonAnimation.start();
        }
        else {
            final ImageView videoSendButton5 = this.videoSendButton;
            float scaleX;
            if (b) {
                scaleX = 1.0f;
            }
            else {
                scaleX = 0.1f;
            }
            videoSendButton5.setScaleX(scaleX);
            final ImageView videoSendButton6 = this.videoSendButton;
            float scaleY2;
            if (b) {
                scaleY2 = 1.0f;
            }
            else {
                scaleY2 = 0.1f;
            }
            videoSendButton6.setScaleY(scaleY2);
            final ImageView videoSendButton7 = this.videoSendButton;
            float alpha3;
            if (b) {
                alpha3 = 1.0f;
            }
            else {
                alpha3 = 0.0f;
            }
            videoSendButton7.setAlpha(alpha3);
            final ImageView audioSendButton4 = this.audioSendButton;
            float scaleX2;
            if (b) {
                scaleX2 = 0.1f;
            }
            else {
                scaleX2 = 1.0f;
            }
            audioSendButton4.setScaleX(scaleX2);
            final ImageView audioSendButton5 = this.audioSendButton;
            if (!b) {
                scaleY = 1.0f;
            }
            audioSendButton5.setScaleY(scaleY);
            final ImageView audioSendButton6 = this.audioSendButton;
            float alpha4;
            if (b) {
                alpha4 = n;
            }
            else {
                alpha4 = 1.0f;
            }
            audioSendButton6.setAlpha(alpha4);
        }
    }
    
    private void setStickersExpanded(final boolean stickersExpanded, final boolean b, final boolean b2) {
        if (this.emojiView != null) {
            if (b2 || this.stickersExpanded != stickersExpanded) {
                this.stickersExpanded = stickersExpanded;
                final ChatActivityEnterViewDelegate delegate = this.delegate;
                if (delegate != null) {
                    delegate.onStickersExpandedChange();
                }
                final Point displaySize = AndroidUtilities.displaySize;
                int height;
                if (displaySize.x > displaySize.y) {
                    height = this.keyboardHeightLand;
                }
                else {
                    height = this.keyboardHeight;
                }
                final Animator stickersExpansionAnim = this.stickersExpansionAnim;
                if (stickersExpansionAnim != null) {
                    stickersExpansionAnim.cancel();
                    this.stickersExpansionAnim = null;
                }
                if (this.stickersExpanded) {
                    this.originalViewHeight = this.sizeNotifierLayout.getHeight();
                    final int originalViewHeight = this.originalViewHeight;
                    int statusBarHeight;
                    if (Build$VERSION.SDK_INT >= 21) {
                        statusBarHeight = AndroidUtilities.statusBarHeight;
                    }
                    else {
                        statusBarHeight = 0;
                    }
                    this.stickersExpandedHeight = originalViewHeight - statusBarHeight - ActionBar.getCurrentActionBarHeight() - this.getHeight() + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                    if (this.searchingType == 2) {
                        this.stickersExpandedHeight = Math.min(this.stickersExpandedHeight, AndroidUtilities.dp(120.0f) + height);
                    }
                    this.emojiView.getLayoutParams().height = this.stickersExpandedHeight;
                    this.sizeNotifierLayout.requestLayout();
                    this.sizeNotifierLayout.setForeground((Drawable)new ScrimDrawable());
                    final int selectionStart = this.messageEditText.getSelectionStart();
                    final int selectionEnd = this.messageEditText.getSelectionEnd();
                    final EditTextCaption messageEditText = this.messageEditText;
                    messageEditText.setText((CharSequence)messageEditText.getText());
                    this.messageEditText.setSelection(selectionStart, selectionEnd);
                    if (b) {
                        final AnimatorSet stickersExpansionAnim2 = new AnimatorSet();
                        stickersExpansionAnim2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofInt((Object)this, (Property)this.roundedTranslationYProperty, new int[] { -(this.stickersExpandedHeight - height) }), (Animator)ObjectAnimator.ofInt((Object)this.emojiView, (Property)this.roundedTranslationYProperty, new int[] { -(this.stickersExpandedHeight - height) }), (Animator)ObjectAnimator.ofFloat((Object)this.stickersArrow, "animationProgress", new float[] { 1.0f }) });
                        stickersExpansionAnim2.setDuration(400L);
                        stickersExpansionAnim2.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT_QUINT);
                        ((ObjectAnimator)stickersExpansionAnim2.getChildAnimations().get(0)).addUpdateListener((ValueAnimator$AnimatorUpdateListener)new _$$Lambda$ChatActivityEnterView$4bFyOKzZkoegxIkwH3otL_R7SUo(this, height));
                        stickersExpansionAnim2.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator animator) {
                                ChatActivityEnterView.this.stickersExpansionAnim = null;
                                ChatActivityEnterView.this.emojiView.setLayerType(0, (Paint)null);
                            }
                        });
                        this.stickersExpansionAnim = (Animator)stickersExpansionAnim2;
                        this.emojiView.setLayerType(2, (Paint)null);
                        stickersExpansionAnim2.start();
                    }
                    else {
                        this.stickersExpansionProgress = 1.0f;
                        this.setTranslationY((float)(-(this.stickersExpandedHeight - height)));
                        this.emojiView.setTranslationY((float)(-(this.stickersExpandedHeight - height)));
                        this.stickersArrow.setAnimationProgress(1.0f);
                    }
                }
                else if (b) {
                    this.closeAnimationInProgress = true;
                    final AnimatorSet stickersExpansionAnim3 = new AnimatorSet();
                    stickersExpansionAnim3.playTogether(new Animator[] { (Animator)ObjectAnimator.ofInt((Object)this, (Property)this.roundedTranslationYProperty, new int[] { 0 }), (Animator)ObjectAnimator.ofInt((Object)this.emojiView, (Property)this.roundedTranslationYProperty, new int[] { 0 }), (Animator)ObjectAnimator.ofFloat((Object)this.stickersArrow, "animationProgress", new float[] { 0.0f }) });
                    stickersExpansionAnim3.setDuration(400L);
                    stickersExpansionAnim3.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT_QUINT);
                    ((ObjectAnimator)stickersExpansionAnim3.getChildAnimations().get(0)).addUpdateListener((ValueAnimator$AnimatorUpdateListener)new _$$Lambda$ChatActivityEnterView$A5TmJlGp3wUlMNBvVwJxDClmnmg(this, height));
                    stickersExpansionAnim3.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            ChatActivityEnterView.this.closeAnimationInProgress = false;
                            ChatActivityEnterView.this.stickersExpansionAnim = null;
                            if (ChatActivityEnterView.this.emojiView != null) {
                                ChatActivityEnterView.this.emojiView.getLayoutParams().height = height;
                                ChatActivityEnterView.this.emojiView.setLayerType(0, (Paint)null);
                            }
                            if (ChatActivityEnterView.this.sizeNotifierLayout != null) {
                                ChatActivityEnterView.this.sizeNotifierLayout.requestLayout();
                                ChatActivityEnterView.this.sizeNotifierLayout.setForeground((Drawable)null);
                                ChatActivityEnterView.this.sizeNotifierLayout.setWillNotDraw(false);
                            }
                        }
                    });
                    this.stickersExpansionAnim = (Animator)stickersExpansionAnim3;
                    this.emojiView.setLayerType(2, (Paint)null);
                    stickersExpansionAnim3.start();
                }
                else {
                    this.setTranslationY(this.stickersExpansionProgress = 0.0f);
                    this.emojiView.setTranslationY(0.0f);
                    this.emojiView.getLayoutParams().height = height;
                    this.sizeNotifierLayout.requestLayout();
                    this.sizeNotifierLayout.setForeground((Drawable)null);
                    this.sizeNotifierLayout.setWillNotDraw(false);
                    this.stickersArrow.setAnimationProgress(0.0f);
                }
                if (stickersExpanded) {
                    this.expandStickersButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrCollapsePanel", 2131558428));
                }
                else {
                    this.expandStickersButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrExpandPanel", 2131558432));
                }
            }
        }
    }
    
    private void showPopup(final int n, final int currentPopupContentType) {
        if (n == 1) {
            if (currentPopupContentType == 0 && this.emojiView == null) {
                if (this.parentActivity == null) {
                    return;
                }
                this.createEmojiView();
            }
            Object o = null;
            if (currentPopupContentType == 0) {
                this.emojiView.setVisibility(0);
                this.emojiViewVisible = true;
                final BotKeyboardView botKeyboardView = this.botKeyboardView;
                if (botKeyboardView != null && botKeyboardView.getVisibility() != 8) {
                    this.botKeyboardView.setVisibility(8);
                }
                o = this.emojiView;
            }
            else if (currentPopupContentType == 1) {
                final EmojiView emojiView = this.emojiView;
                if (emojiView != null && emojiView.getVisibility() != 8) {
                    this.emojiView.setVisibility(8);
                    this.emojiViewVisible = false;
                }
                this.botKeyboardView.setVisibility(0);
                o = this.botKeyboardView;
            }
            this.currentPopupContentType = currentPopupContentType;
            if (this.keyboardHeight <= 0) {
                this.keyboardHeight = MessagesController.getGlobalEmojiSettings().getInt("kbd_height", AndroidUtilities.dp(200.0f));
            }
            if (this.keyboardHeightLand <= 0) {
                this.keyboardHeightLand = MessagesController.getGlobalEmojiSettings().getInt("kbd_height_land3", AndroidUtilities.dp(200.0f));
            }
            final Point displaySize = AndroidUtilities.displaySize;
            int b;
            if (displaySize.x > displaySize.y) {
                b = this.keyboardHeightLand;
            }
            else {
                b = this.keyboardHeight;
            }
            int min = b;
            if (currentPopupContentType == 1) {
                min = Math.min(this.botKeyboardView.getKeyboardHeight(), b);
            }
            final BotKeyboardView botKeyboardView2 = this.botKeyboardView;
            if (botKeyboardView2 != null) {
                botKeyboardView2.setPanelHeight(min);
            }
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)((View)o).getLayoutParams();
            layoutParams.height = min;
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            if (!AndroidUtilities.isInMultiwindow) {
                AndroidUtilities.hideKeyboard((View)this.messageEditText);
            }
            final SizeNotifierFrameLayout sizeNotifierLayout = this.sizeNotifierLayout;
            if (sizeNotifierLayout != null) {
                this.emojiPadding = min;
                sizeNotifierLayout.requestLayout();
                this.setEmojiButtonImage(true, true);
                this.updateBotButton();
                this.onWindowSizeChanged();
            }
        }
        else {
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
            final BotKeyboardView botKeyboardView3 = this.botKeyboardView;
            if (botKeyboardView3 != null) {
                botKeyboardView3.setVisibility(8);
            }
            if (this.sizeNotifierLayout != null) {
                if (n == 0) {
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
        if (this.stickersExpanded && n != 1) {
            this.setStickersExpanded(false, false, false);
        }
    }
    
    private void updateBotButton() {
        final ImageView botButton = this.botButton;
        if (botButton == null) {
            return;
        }
        if (!this.hasBotCommands && this.botReplyMarkup == null) {
            botButton.setVisibility(8);
        }
        else {
            if (this.botButton.getVisibility() != 0) {
                this.botButton.setVisibility(0);
            }
            if (this.botReplyMarkup != null) {
                if (this.isPopupShowing() && this.currentPopupContentType == 1) {
                    this.botButton.setImageResource(2131165487);
                    this.botButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrShowKeyboard", 2131558473));
                }
                else {
                    this.botButton.setImageResource(2131165482);
                    this.botButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrBotKeyboard", 2131558416));
                }
            }
            else {
                this.botButton.setImageResource(2131165481);
                this.botButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrBotCommands", 2131558415));
            }
        }
        this.updateFieldRight(2);
        final LinearLayout attachLayout = this.attachLayout;
        final ImageView botButton2 = this.botButton;
        float n = 0.0f;
        Label_0220: {
            if (botButton2 == null || botButton2.getVisibility() == 8) {
                final ImageView notifyButton = this.notifyButton;
                if (notifyButton == null || notifyButton.getVisibility() == 8) {
                    n = 48.0f;
                    break Label_0220;
                }
            }
            n = 96.0f;
        }
        attachLayout.setPivotX((float)AndroidUtilities.dp(n));
    }
    
    private void updateFieldHint() {
        final int n = (int)this.dialog_id;
        int n3;
        final int n2 = n3 = 0;
        if (n < 0) {
            final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-(int)this.dialog_id);
            n3 = n2;
            if (ChatObject.isChannel(chat)) {
                n3 = n2;
                if (!chat.megagroup) {
                    n3 = 1;
                }
            }
        }
        if (this.editingMessageObject != null) {
            final EditTextCaption messageEditText = this.messageEditText;
            String hintText;
            if (this.editingCaption) {
                hintText = LocaleController.getString("Caption", 2131558904);
            }
            else {
                hintText = LocaleController.getString("TypeMessage", 2131560921);
            }
            messageEditText.setHintText(hintText);
        }
        else if (n3 != 0) {
            if (this.silent) {
                this.messageEditText.setHintText(LocaleController.getString("ChannelSilentBroadcast", 2131559003));
            }
            else {
                this.messageEditText.setHintText(LocaleController.getString("ChannelBroadcast", 2131558936));
            }
        }
        else {
            this.messageEditText.setHintText(LocaleController.getString("TypeMessage", 2131560921));
        }
    }
    
    private void updateFieldRight(final int n) {
        final EditTextCaption messageEditText = this.messageEditText;
        if (messageEditText != null) {
            if (this.editingMessageObject == null) {
                final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)messageEditText.getLayoutParams();
                Label_0172: {
                    if (n == 1) {
                        final ImageView botButton = this.botButton;
                        if (botButton == null || botButton.getVisibility() != 0) {
                            final ImageView notifyButton = this.notifyButton;
                            if (notifyButton == null || notifyButton.getVisibility() != 0) {
                                layoutParams.rightMargin = AndroidUtilities.dp(50.0f);
                                break Label_0172;
                            }
                        }
                        layoutParams.rightMargin = AndroidUtilities.dp(98.0f);
                    }
                    else if (n == 2) {
                        if (layoutParams.rightMargin != AndroidUtilities.dp(2.0f)) {
                            final ImageView botButton2 = this.botButton;
                            if (botButton2 == null || botButton2.getVisibility() != 0) {
                                final ImageView notifyButton2 = this.notifyButton;
                                if (notifyButton2 == null || notifyButton2.getVisibility() != 0) {
                                    layoutParams.rightMargin = AndroidUtilities.dp(50.0f);
                                    break Label_0172;
                                }
                            }
                            layoutParams.rightMargin = AndroidUtilities.dp(98.0f);
                        }
                    }
                    else {
                        layoutParams.rightMargin = AndroidUtilities.dp(2.0f);
                    }
                }
                this.messageEditText.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            }
        }
    }
    
    private void updateRecordIntefrace() {
        final boolean recordingAudioVideo = this.recordingAudioVideo;
        final Integer value = 0;
        if (recordingAudioVideo) {
            if (this.recordInterfaceState == 1) {
                return;
            }
            this.recordInterfaceState = 1;
            try {
                if (this.wakeLock == null) {
                    (this.wakeLock = ((PowerManager)ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(536870918, "telegram:audio_record_lock")).acquire();
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            AndroidUtilities.lockOrientation(this.parentActivity);
            final ChatActivityEnterViewDelegate delegate = this.delegate;
            if (delegate != null) {
                delegate.needStartRecordAudio(0);
            }
            this.recordPanel.setVisibility(0);
            this.recordCircle.setVisibility(0);
            this.recordCircle.setAmplitude(0.0);
            this.recordTimeText.setText((CharSequence)String.format("%02d:%02d.%02d", value, value, value));
            this.recordDot.resetAlpha();
            this.lastTimeString = null;
            this.lastTypingSendTime = -1L;
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.slideText.getLayoutParams();
            layoutParams.leftMargin = AndroidUtilities.dp(30.0f);
            this.slideText.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            this.slideText.setAlpha(1.0f);
            this.recordPanel.setX((float)AndroidUtilities.displaySize.x);
            final AnimatorSet runningAnimationAudio = this.runningAnimationAudio;
            if (runningAnimationAudio != null) {
                runningAnimationAudio.cancel();
            }
            (this.runningAnimationAudio = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.recordPanel, View.TRANSLATION_X, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.recordCircle, (Property)this.recordCircleScale, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.audioVideoButtonContainer, View.ALPHA, new float[] { 0.0f }) });
            this.runningAnimationAudio.setDuration(300L);
            this.runningAnimationAudio.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator obj) {
                    if (ChatActivityEnterView.this.runningAnimationAudio != null && ChatActivityEnterView.this.runningAnimationAudio.equals(obj)) {
                        ChatActivityEnterView.this.recordPanel.setX(0.0f);
                        ChatActivityEnterView.this.runningAnimationAudio = null;
                    }
                }
            });
            this.runningAnimationAudio.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
            this.runningAnimationAudio.start();
        }
        else {
            final PowerManager$WakeLock wakeLock = this.wakeLock;
            if (wakeLock != null) {
                try {
                    wakeLock.release();
                    this.wakeLock = null;
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
            }
            AndroidUtilities.unlockOrientation(this.parentActivity);
            if (this.recordInterfaceState == 0) {
                return;
            }
            this.recordInterfaceState = 0;
            final AnimatorSet runningAnimationAudio2 = this.runningAnimationAudio;
            if (runningAnimationAudio2 != null) {
                runningAnimationAudio2.cancel();
            }
            (this.runningAnimationAudio = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.recordPanel, View.TRANSLATION_X, new float[] { (float)AndroidUtilities.displaySize.x }), (Animator)ObjectAnimator.ofFloat((Object)this.recordCircle, (Property)this.recordCircleScale, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.audioVideoButtonContainer, View.ALPHA, new float[] { 1.0f }) });
            this.runningAnimationAudio.setDuration(300L);
            this.runningAnimationAudio.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator obj) {
                    if (ChatActivityEnterView.this.runningAnimationAudio != null && ChatActivityEnterView.this.runningAnimationAudio.equals(obj)) {
                        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)ChatActivityEnterView.this.slideText.getLayoutParams();
                        layoutParams.leftMargin = AndroidUtilities.dp(30.0f);
                        ChatActivityEnterView.this.slideText.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                        ChatActivityEnterView.this.slideText.setAlpha(1.0f);
                        ChatActivityEnterView.this.recordPanel.setVisibility(8);
                        ChatActivityEnterView.this.recordCircle.setVisibility(8);
                        ChatActivityEnterView.this.recordCircle.setSendButtonInvisible();
                        ChatActivityEnterView.this.runningAnimationAudio = null;
                    }
                }
            });
            this.runningAnimationAudio.setInterpolator((TimeInterpolator)new AccelerateInterpolator());
            this.runningAnimationAudio.start();
        }
    }
    
    public void addEmojiToRecent(final String s) {
        this.createEmojiView();
        this.emojiView.addEmojiToRecent(s);
    }
    
    public void addRecentGif(final TLRPC.Document document) {
        DataQuery.getInstance(this.currentAccount).addRecentGif(document, (int)(System.currentTimeMillis() / 1000L));
        final EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.addRecentGif(document);
        }
    }
    
    public void addStickerToRecent(final TLRPC.Document document) {
        this.createEmojiView();
        this.emojiView.addRecentSticker(document);
    }
    
    public void addTopView(View topView, final View topLineView, final int n) {
        if (topView == null) {
            return;
        }
        (this.topLineView = topLineView).setVisibility(8);
        this.topLineView.setAlpha(0.0f);
        this.addView(this.topLineView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 1.0f, 51, 0.0f, (float)(n + 1), 0.0f, 0.0f));
        (this.topView = topView).setVisibility(8);
        topView = this.topView;
        final float translationY = (float)n;
        topView.setTranslationY(translationY);
        this.addView(this.topView, 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, translationY, 51, 0.0f, 2.0f, 0.0f, 0.0f));
        this.needShowTopView = false;
    }
    
    public void cancelRecordingAudioVideo() {
        if (this.hasRecordVideo && this.videoSendButton.getTag() != null) {
            CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
            this.delegate.needStartRecordVideo(2);
        }
        else {
            this.delegate.needStartRecordAudio(0);
            MediaController.getInstance().stopRecording(0);
        }
        this.recordingAudioVideo = false;
        this.updateRecordIntefrace();
    }
    
    public void checkChannelRights() {
        final ChatActivity parentFragment = this.parentFragment;
        if (parentFragment == null) {
            return;
        }
        final TLRPC.Chat currentChat = parentFragment.getCurrentChat();
        if (currentChat != null) {
            final FrameLayout audioVideoButtonContainer = this.audioVideoButtonContainer;
            float alpha;
            if (ChatObject.canSendMedia(currentChat)) {
                alpha = 1.0f;
            }
            else {
                alpha = 0.5f;
            }
            audioVideoButtonContainer.setAlpha(alpha);
            final EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.setStickersBanned(ChatObject.canSendStickers(currentChat) ^ true, currentChat.id);
            }
        }
    }
    
    public void checkRoundVideo() {
        if (this.hasRecordVideo) {
            return;
        }
        if (this.attachLayout != null && Build$VERSION.SDK_INT >= 18) {
            final long dialog_id = this.dialog_id;
            final int n = (int)dialog_id;
            final int i = (int)(dialog_id >> 32);
            int n2 = 1;
            if (n == 0 && i != 0) {
                if (AndroidUtilities.getPeerLayerVersion(MessagesController.getInstance(this.currentAccount).getEncryptedChat(i).layer) >= 66) {
                    this.hasRecordVideo = true;
                }
            }
            else {
                this.hasRecordVideo = true;
            }
            int n3 = 0;
            Label_0203: {
                if ((int)this.dialog_id < 0) {
                    final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-(int)this.dialog_id);
                    if (!ChatObject.isChannel(chat) || chat.megagroup) {
                        n2 = 0;
                    }
                    n3 = n2;
                    if (n2 != 0) {
                        n3 = n2;
                        if (!chat.creator) {
                            final TLRPC.TL_chatAdminRights admin_rights = chat.admin_rights;
                            if (admin_rights != null) {
                                n3 = n2;
                                if (admin_rights.post_messages) {
                                    break Label_0203;
                                }
                            }
                            this.hasRecordVideo = false;
                            n3 = n2;
                        }
                    }
                }
                else {
                    n3 = 0;
                }
            }
            if (!SharedConfig.inappCamera) {
                this.hasRecordVideo = false;
            }
            if (this.hasRecordVideo) {
                if (SharedConfig.hasCameraCache) {
                    CameraController.getInstance().initCamera(null);
                }
                final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                String s;
                if (n3 != 0) {
                    s = "currentModeVideoChannel";
                }
                else {
                    s = "currentModeVideo";
                }
                this.setRecordVideoButtonVisible(globalMainSettings.getBoolean(s, (boolean)(n3 != 0)), false);
            }
            else {
                this.setRecordVideoButtonVisible(false, false);
            }
            return;
        }
        this.setRecordVideoButtonVisible(this.hasRecordVideo = false, false);
    }
    
    public void closeKeyboard() {
        AndroidUtilities.hideKeyboard((View)this.messageEditText);
    }
    
    public void didPressedBotButton(final TLRPC.KeyboardButton keyboardButton, final MessageObject messageObject, final MessageObject messageObject2) {
        if (keyboardButton != null) {
            if (messageObject2 != null) {
                if (keyboardButton instanceof TLRPC.TL_keyboardButton) {
                    SendMessagesHelper.getInstance(this.currentAccount).sendMessage(keyboardButton.text, this.dialog_id, messageObject, null, false, null, null, null);
                }
                else if (keyboardButton instanceof TLRPC.TL_keyboardButtonUrl) {
                    this.parentFragment.showOpenUrlAlert(keyboardButton.url, true);
                }
                else if (keyboardButton instanceof TLRPC.TL_keyboardButtonRequestPhone) {
                    this.parentFragment.shareMyContact(messageObject2);
                }
                else if (keyboardButton instanceof TLRPC.TL_keyboardButtonRequestGeoLocation) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.parentActivity);
                    builder.setTitle(LocaleController.getString("ShareYouLocationTitle", 2131560756));
                    builder.setMessage(LocaleController.getString("ShareYouLocationInfo", 2131560754));
                    builder.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$ChatActivityEnterView$fmPI_spkDt8Wbdud0XG8IKtQnwY(this, messageObject2, keyboardButton));
                    builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                    this.parentFragment.showDialog(builder.create());
                }
                else if (!(keyboardButton instanceof TLRPC.TL_keyboardButtonCallback) && !(keyboardButton instanceof TLRPC.TL_keyboardButtonGame) && !(keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) && !(keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth)) {
                    if (keyboardButton instanceof TLRPC.TL_keyboardButtonSwitchInline) {
                        if (this.parentFragment.processSwitchButton((TLRPC.TL_keyboardButtonSwitchInline)keyboardButton)) {
                            return;
                        }
                        if (keyboardButton.same_peer) {
                            final TLRPC.Message messageOwner = messageObject2.messageOwner;
                            int from_id = messageOwner.from_id;
                            final int via_bot_id = messageOwner.via_bot_id;
                            if (via_bot_id != 0) {
                                from_id = via_bot_id;
                            }
                            final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(from_id);
                            if (user == null) {
                                return;
                            }
                            final StringBuilder sb = new StringBuilder();
                            sb.append("@");
                            sb.append(user.username);
                            sb.append(" ");
                            sb.append(keyboardButton.query);
                            this.setFieldText(sb.toString());
                        }
                        else {
                            final Bundle bundle = new Bundle();
                            bundle.putBoolean("onlySelect", true);
                            bundle.putInt("dialogsType", 1);
                            final DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                            dialogsActivity.setDelegate((DialogsActivity.DialogsActivityDelegate)new _$$Lambda$ChatActivityEnterView$G5pe20Ftkscr87_dZmhZj8HOVnM(this, messageObject2, keyboardButton));
                            this.parentFragment.presentFragment(dialogsActivity);
                        }
                    }
                }
                else {
                    SendMessagesHelper.getInstance(this.currentAccount).sendCallback(true, messageObject2, keyboardButton, this.parentFragment);
                }
            }
        }
    }
    
    public void didReceivedNotification(int i, int j, final Object... array) {
        if (i == NotificationCenter.emojiDidLoad) {
            final EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.invalidateViews();
            }
            final BotKeyboardView botKeyboardView = this.botKeyboardView;
            if (botKeyboardView != null) {
                botKeyboardView.invalidateViews();
            }
        }
        else {
            final int recordProgressChanged = NotificationCenter.recordProgressChanged;
            final int n = 0;
            j = 0;
            if (i == recordProgressChanged) {
                final long longValue = (long)array[0];
                final long lastTypingSendTime = longValue / 1000L;
                i = (int)(longValue % 1000L) / 10;
                final String format = String.format("%02d:%02d.%02d", lastTypingSendTime / 60L, lastTypingSendTime % 60L, i);
                final String lastTimeString = this.lastTimeString;
                if (lastTimeString == null || !lastTimeString.equals(format)) {
                    if (this.lastTypingSendTime != lastTypingSendTime && lastTypingSendTime % 5L == 0L) {
                        this.lastTypingSendTime = lastTypingSendTime;
                        final MessagesController instance = MessagesController.getInstance(this.currentAccount);
                        final long dialog_id = this.dialog_id;
                        final ImageView videoSendButton = this.videoSendButton;
                        if (videoSendButton != null && videoSendButton.getTag() != null) {
                            i = 7;
                        }
                        else {
                            i = 1;
                        }
                        instance.sendTyping(dialog_id, i, 0);
                    }
                    final TextView recordTimeText = this.recordTimeText;
                    if (recordTimeText != null) {
                        recordTimeText.setText((CharSequence)format);
                    }
                }
                final RecordCircle recordCircle = this.recordCircle;
                if (recordCircle != null) {
                    recordCircle.setAmplitude((double)array[1]);
                }
                final ImageView videoSendButton2 = this.videoSendButton;
                if (videoSendButton2 != null && videoSendButton2.getTag() != null && longValue >= 59500L) {
                    this.startedDraggingX = -1.0f;
                    this.delegate.needStartRecordVideo(3);
                }
            }
            else if (i == NotificationCenter.closeChats) {
                final EditTextCaption messageEditText = this.messageEditText;
                if (messageEditText != null && messageEditText.isFocused()) {
                    AndroidUtilities.hideKeyboard((View)this.messageEditText);
                }
            }
            else if (i != NotificationCenter.recordStartError && i != NotificationCenter.recordStopped) {
                if (i == NotificationCenter.recordStarted) {
                    if (!this.recordingAudioVideo) {
                        this.recordingAudioVideo = true;
                        this.updateRecordIntefrace();
                    }
                }
                else if (i == NotificationCenter.audioDidSent) {
                    final Object o = array[0];
                    if (o instanceof VideoEditedInfo) {
                        this.videoToSendMessageObject = (VideoEditedInfo)o;
                        this.audioToSendPath = (String)array[1];
                        this.videoTimelineView.setVideoPath(this.audioToSendPath);
                        this.videoTimelineView.setVisibility(0);
                        this.videoTimelineView.setMinProgressDiff(1000.0f / this.videoToSendMessageObject.estimatedDuration);
                        this.recordedAudioBackground.setVisibility(8);
                        this.recordedAudioTimeTextView.setVisibility(8);
                        this.recordedAudioPlayButton.setVisibility(8);
                        this.recordedAudioSeekBar.setVisibility(8);
                        this.recordedAudioPanel.setAlpha(1.0f);
                        this.recordedAudioPanel.setVisibility(0);
                        this.closeKeyboard();
                        this.hidePopup(false);
                        this.checkSendButton(false);
                    }
                    else {
                        this.audioToSend = (TLRPC.TL_document)array[0];
                        this.audioToSendPath = (String)array[1];
                        if (this.audioToSend != null) {
                            if (this.recordedAudioPanel == null) {
                                return;
                            }
                            this.videoTimelineView.setVisibility(8);
                            this.recordedAudioBackground.setVisibility(0);
                            this.recordedAudioTimeTextView.setVisibility(0);
                            this.recordedAudioPlayButton.setVisibility(0);
                            this.recordedAudioSeekBar.setVisibility(0);
                            final TLRPC.TL_message tl_message = new TLRPC.TL_message();
                            tl_message.out = true;
                            tl_message.id = 0;
                            tl_message.to_id = new TLRPC.TL_peerUser();
                            final TLRPC.Peer to_id = tl_message.to_id;
                            i = UserConfig.getInstance(this.currentAccount).getClientUserId();
                            tl_message.from_id = i;
                            to_id.user_id = i;
                            tl_message.date = (int)(System.currentTimeMillis() / 1000L);
                            tl_message.message = "";
                            tl_message.attachPath = this.audioToSendPath;
                            tl_message.media = new TLRPC.TL_messageMediaDocument();
                            final TLRPC.MessageMedia media = tl_message.media;
                            media.flags |= 0x3;
                            media.document = this.audioToSend;
                            tl_message.flags |= 0x300;
                            this.audioToSendMessageObject = new MessageObject(UserConfig.selectedAccount, tl_message, false);
                            this.recordedAudioPanel.setAlpha(1.0f);
                            this.recordedAudioPanel.setVisibility(0);
                            while (true) {
                                TLRPC.DocumentAttribute documentAttribute;
                                TLRPC.DocumentAttribute documentAttribute2;
                                byte[] waveform;
                                for (i = 0; i < this.audioToSend.attributes.size(); ++i) {
                                    documentAttribute = this.audioToSend.attributes.get(i);
                                    if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                                        i = documentAttribute.duration;
                                        for (j = 0; j < this.audioToSend.attributes.size(); ++j) {
                                            documentAttribute2 = this.audioToSend.attributes.get(j);
                                            if (documentAttribute2 instanceof TLRPC.TL_documentAttributeAudio) {
                                                waveform = documentAttribute2.waveform;
                                                if (waveform == null || waveform.length == 0) {
                                                    documentAttribute2.waveform = MediaController.getInstance().getWaveform(this.audioToSendPath);
                                                }
                                                this.recordedAudioSeekBar.setWaveform(documentAttribute2.waveform);
                                                break;
                                            }
                                        }
                                        this.recordedAudioTimeTextView.setText((CharSequence)String.format("%d:%02d", i / 60, i % 60));
                                        this.closeKeyboard();
                                        this.hidePopup(false);
                                        this.checkSendButton(false);
                                        return;
                                    }
                                }
                                i = 0;
                                continue;
                            }
                        }
                        else {
                            final ChatActivityEnterViewDelegate delegate = this.delegate;
                            if (delegate != null) {
                                delegate.onMessageSend(null);
                            }
                        }
                    }
                }
                else if (i == NotificationCenter.audioRouteChanged) {
                    if (this.parentActivity != null) {
                        final boolean booleanValue = (boolean)array[0];
                        final Activity parentActivity = this.parentActivity;
                        if (booleanValue) {
                            i = j;
                        }
                        else {
                            i = Integer.MIN_VALUE;
                        }
                        parentActivity.setVolumeControlStream(i);
                    }
                }
                else if (i == NotificationCenter.messagePlayingDidReset) {
                    if (this.audioToSendMessageObject != null && !MediaController.getInstance().isPlayingMessage(this.audioToSendMessageObject)) {
                        this.recordedAudioPlayButton.setImageDrawable(this.playDrawable);
                        this.recordedAudioPlayButton.setContentDescription((CharSequence)LocaleController.getString("AccActionPlay", 2131558409));
                        this.recordedAudioSeekBar.setProgress(0.0f);
                    }
                }
                else if (i == NotificationCenter.messagePlayingProgressDidChanged) {
                    final Integer n2 = (Integer)array[0];
                    if (this.audioToSendMessageObject != null && MediaController.getInstance().isPlayingMessage(this.audioToSendMessageObject)) {
                        final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                        final MessageObject audioToSendMessageObject = this.audioToSendMessageObject;
                        audioToSendMessageObject.audioProgress = playingMessageObject.audioProgress;
                        audioToSendMessageObject.audioProgressSec = playingMessageObject.audioProgressSec;
                        if (!this.recordedAudioSeekBar.isDragging()) {
                            this.recordedAudioSeekBar.setProgress(this.audioToSendMessageObject.audioProgress);
                        }
                    }
                }
                else if (i == NotificationCenter.featuredStickersDidLoad && this.emojiButton != null) {
                    i = n;
                    while (true) {
                        final ImageView[] emojiButton = this.emojiButton;
                        if (i >= emojiButton.length) {
                            break;
                        }
                        emojiButton[i].invalidate();
                        ++i;
                    }
                }
            }
            else {
                if (this.recordingAudioVideo) {
                    MessagesController.getInstance(this.currentAccount).sendTyping(this.dialog_id, 2, 0);
                    this.recordingAudioVideo = false;
                    this.updateRecordIntefrace();
                }
                if (i == NotificationCenter.recordStopped) {
                    final Integer n3 = (Integer)array[0];
                    if (n3 == 2) {
                        this.videoTimelineView.setVisibility(0);
                        this.recordedAudioBackground.setVisibility(8);
                        this.recordedAudioTimeTextView.setVisibility(8);
                        this.recordedAudioPlayButton.setVisibility(8);
                        this.recordedAudioSeekBar.setVisibility(8);
                        this.recordedAudioPanel.setAlpha(1.0f);
                        this.recordedAudioPanel.setVisibility(0);
                    }
                    else {
                        n3;
                    }
                }
            }
        }
    }
    
    public void doneEditingMessage() {
        if (this.editingMessageObject != null) {
            this.delegate.onMessageEditEnd(true);
            this.showEditDoneProgress(true, true);
            final CharSequence[] array = { (CharSequence)this.messageEditText.getText() };
            this.editingMessageReqId = SendMessagesHelper.getInstance(this.currentAccount).editMessage(this.editingMessageObject, array[0].toString(), this.messageWebPageSearch, this.parentFragment, DataQuery.getInstance(this.currentAccount).getEntities(array), new _$$Lambda$ChatActivityEnterView$ORXTEQMKGHUriZg_5Y7AosAFGHI(this));
        }
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, final long n) {
        if (view == this.topView) {
            canvas.save();
            canvas.clipRect(0, 0, this.getMeasuredWidth(), view.getLayoutParams().height + AndroidUtilities.dp(2.0f));
        }
        final boolean drawChild = super.drawChild(canvas, view, n);
        if (view == this.topView) {
            canvas.restore();
        }
        return drawChild;
    }
    
    public ImageView getAttachButton() {
        return this.attachButton;
    }
    
    public int getCursorPosition() {
        final EditTextCaption messageEditText = this.messageEditText;
        if (messageEditText == null) {
            return 0;
        }
        return messageEditText.getSelectionStart();
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
        if (this.hasText()) {
            return (CharSequence)this.messageEditText.getText();
        }
        return null;
    }
    
    public int getSelectionLength() {
        final EditTextCaption messageEditText = this.messageEditText;
        if (messageEditText == null) {
            return 0;
        }
        try {
            return messageEditText.getSelectionEnd() - this.messageEditText.getSelectionStart();
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return 0;
        }
    }
    
    public boolean hasAudioToSend() {
        return this.audioToSendMessageObject != null || this.videoToSendMessageObject != null;
    }
    
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    public boolean hasRecordVideo() {
        return this.hasRecordVideo;
    }
    
    public boolean hasText() {
        final EditTextCaption messageEditText = this.messageEditText;
        return messageEditText != null && messageEditText.length() > 0;
    }
    
    public void hideEmojiView() {
        if (!this.emojiViewVisible) {
            final EmojiView emojiView = this.emojiView;
            if (emojiView != null && emojiView.getVisibility() != 8) {
                this.emojiView.setVisibility(8);
            }
        }
    }
    
    public void hidePopup(final boolean b) {
        if (this.isPopupShowing()) {
            if (this.currentPopupContentType == 1 && b && this.botButtonsMessageObject != null) {
                final SharedPreferences$Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
                final StringBuilder sb = new StringBuilder();
                sb.append("hidekeyboard_");
                sb.append(this.dialog_id);
                edit.putInt(sb.toString(), this.botButtonsMessageObject.getId()).commit();
            }
            if (b && this.searchingType != 0) {
                this.searchingType = 0;
                this.emojiView.closeSearch(true);
                this.messageEditText.requestFocus();
                this.setStickersExpanded(false, true, false);
                if (this.emojiTabOpen) {
                    this.checkSendButton(true);
                }
            }
            else {
                if (this.searchingType != 0) {
                    this.searchingType = 0;
                    this.emojiView.closeSearch(false);
                    this.messageEditText.requestFocus();
                }
                this.showPopup(0, 0);
            }
        }
    }
    
    public void hideTopView(final boolean b) {
        if (this.topView != null) {
            if (this.topViewShowed) {
                this.topViewShowed = false;
                this.needShowTopView = false;
                if (this.allowShowTopView) {
                    final AnimatorSet currentTopViewAnimation = this.currentTopViewAnimation;
                    if (currentTopViewAnimation != null) {
                        currentTopViewAnimation.cancel();
                        this.currentTopViewAnimation = null;
                    }
                    if (b) {
                        this.currentTopViewAnimation = new AnimatorSet();
                        final AnimatorSet currentTopViewAnimation2 = this.currentTopViewAnimation;
                        final View topView = this.topView;
                        currentTopViewAnimation2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)topView, View.TRANSLATION_Y, new float[] { (float)topView.getLayoutParams().height }), (Animator)ObjectAnimator.ofFloat((Object)this.topLineView, View.ALPHA, new float[] { 0.0f }) });
                        this.currentTopViewAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationCancel(final Animator obj) {
                                if (ChatActivityEnterView.this.currentTopViewAnimation != null && ChatActivityEnterView.this.currentTopViewAnimation.equals(obj)) {
                                    ChatActivityEnterView.this.currentTopViewAnimation = null;
                                }
                            }
                            
                            public void onAnimationEnd(final Animator obj) {
                                if (ChatActivityEnterView.this.currentTopViewAnimation != null && ChatActivityEnterView.this.currentTopViewAnimation.equals(obj)) {
                                    ChatActivityEnterView.this.topView.setVisibility(8);
                                    ChatActivityEnterView.this.topLineView.setVisibility(8);
                                    ChatActivityEnterView.this.resizeForTopView(false);
                                    ChatActivityEnterView.this.currentTopViewAnimation = null;
                                }
                            }
                        });
                        this.currentTopViewAnimation.setDuration(200L);
                        this.currentTopViewAnimation.setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT);
                        this.currentTopViewAnimation.start();
                    }
                    else {
                        this.topView.setVisibility(8);
                        this.topLineView.setVisibility(8);
                        this.topLineView.setAlpha(0.0f);
                        this.resizeForTopView(false);
                        final View topView2 = this.topView;
                        topView2.setTranslationY((float)topView2.getLayoutParams().height);
                    }
                }
            }
        }
    }
    
    public boolean isEditingCaption() {
        return this.editingCaption;
    }
    
    public boolean isEditingMessage() {
        return this.editingMessageObject != null;
    }
    
    public boolean isInVideoMode() {
        return this.videoSendButton.getTag() != null;
    }
    
    public boolean isKeyboardVisible() {
        return this.keyboardVisible;
    }
    
    public boolean isMessageWebPageSearchEnabled() {
        return this.messageWebPageSearch;
    }
    
    public boolean isPopupShowing() {
        if (!this.emojiViewVisible) {
            final BotKeyboardView botKeyboardView = this.botKeyboardView;
            if (botKeyboardView == null || botKeyboardView.getVisibility() != 0) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isPopupView(final View view) {
        return view == this.botKeyboardView || view == this.emojiView;
    }
    
    public boolean isRecordCircle(final View view) {
        return view == this.recordCircle;
    }
    
    public boolean isRecordLocked() {
        return this.recordingAudioVideo && this.recordCircle.isSendButtonVisible();
    }
    
    public boolean isRecordingAudioVideo() {
        return this.recordingAudioVideo;
    }
    
    public boolean isRtlText() {
        boolean b = false;
        try {
            if (this.messageEditText.getLayout().getParagraphDirection(0) == -1) {
                b = true;
            }
            return b;
        }
        catch (Throwable t) {
            return b;
        }
    }
    
    public boolean isSendButtonVisible() {
        return this.sendButton.getVisibility() == 0;
    }
    
    public boolean isStickersExpanded() {
        return this.stickersExpanded;
    }
    
    public boolean isTopViewVisible() {
        final View topView = this.topView;
        return topView != null && topView.getVisibility() == 0;
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
        final EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.onDestroy();
        }
        final PowerManager$WakeLock wakeLock = this.wakeLock;
        if (wakeLock != null) {
            try {
                wakeLock.release();
                this.wakeLock = null;
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        final SizeNotifierFrameLayout sizeNotifierLayout = this.sizeNotifierLayout;
        if (sizeNotifierLayout != null) {
            sizeNotifierLayout.setDelegate(null);
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        final View topView = this.topView;
        int n;
        if (topView != null && topView.getVisibility() == 0) {
            n = (int)this.topView.getTranslationY();
        }
        else {
            n = 0;
        }
        final int n2 = Theme.chat_composeShadowDrawable.getIntrinsicHeight() + n;
        Theme.chat_composeShadowDrawable.setBounds(0, n, this.getMeasuredWidth(), n2);
        Theme.chat_composeShadowDrawable.draw(canvas);
        canvas.drawRect(0.0f, (float)n2, (float)this.getWidth(), (float)this.getHeight(), Theme.chat_composeBackgroundPaint);
    }
    
    public void onEditTimeExpired() {
        this.doneButtonContainer.setVisibility(8);
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        if (this.recordingAudioVideo) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }
    
    public void onPause() {
        this.isPaused = true;
        this.closeKeyboard();
    }
    
    public void onRequestPermissionsResultFragment(final int n, final String[] array, final int[] array2) {
        if (n == 2 && this.pendingLocationButton != null) {
            if (array2.length > 0 && array2[0] == 0) {
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
            AndroidUtilities.showKeyboard((View)this.messageEditText);
            if (!AndroidUtilities.usingHardwareInput && !this.keyboardVisible && !AndroidUtilities.isInMultiwindow) {
                this.waitingForKeyboardOpen = true;
                AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
                AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100L);
            }
        }
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3 && this.stickersExpanded) {
            this.searchingType = 0;
            this.emojiView.closeSearch(false);
            this.setStickersExpanded(false, false, false);
        }
        this.videoTimelineView.clearFrames();
    }
    
    public void onSizeChanged(final int n, final boolean b) {
        final int searchingType = this.searchingType;
        final boolean b2 = true;
        final boolean b3 = true;
        if (searchingType != 0) {
            this.lastSizeChangeValue1 = n;
            this.lastSizeChangeValue2 = b;
            this.keyboardVisible = (n > 0 && b3);
            return;
        }
        if (n > AndroidUtilities.dp(50.0f) && this.keyboardVisible && !AndroidUtilities.isInMultiwindow) {
            if (b) {
                this.keyboardHeightLand = n;
                MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height_land3", this.keyboardHeightLand).commit();
            }
            else {
                this.keyboardHeight = n;
                MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height", this.keyboardHeight).commit();
            }
        }
        if (this.isPopupShowing()) {
            int b4;
            if (b) {
                b4 = this.keyboardHeightLand;
            }
            else {
                b4 = this.keyboardHeight;
            }
            int min = b4;
            if (this.currentPopupContentType == 1) {
                min = b4;
                if (!this.botKeyboardView.isFullSize()) {
                    min = Math.min(this.botKeyboardView.getKeyboardHeight(), b4);
                }
            }
            Object o = null;
            final int currentPopupContentType = this.currentPopupContentType;
            if (currentPopupContentType == 0) {
                o = this.emojiView;
            }
            else if (currentPopupContentType == 1) {
                o = this.botKeyboardView;
            }
            final BotKeyboardView botKeyboardView = this.botKeyboardView;
            if (botKeyboardView != null) {
                botKeyboardView.setPanelHeight(min);
            }
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)((View)o).getLayoutParams();
            if (!this.closeAnimationInProgress && (layoutParams.width != AndroidUtilities.displaySize.x || layoutParams.height != min) && !this.stickersExpanded) {
                layoutParams.width = AndroidUtilities.displaySize.x;
                layoutParams.height = min;
                ((View)o).setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                if (this.sizeNotifierLayout != null) {
                    this.emojiPadding = layoutParams.height;
                    this.sizeNotifierLayout.requestLayout();
                    this.onWindowSizeChanged();
                }
            }
        }
        if (this.lastSizeChangeValue1 == n && this.lastSizeChangeValue2 == b) {
            this.onWindowSizeChanged();
            return;
        }
        this.lastSizeChangeValue1 = n;
        this.lastSizeChangeValue2 = b;
        final boolean keyboardVisible = this.keyboardVisible;
        this.keyboardVisible = (n > 0 && b2);
        if (this.keyboardVisible && this.isPopupShowing()) {
            this.showPopup(0, this.currentPopupContentType);
        }
        if (this.emojiPadding != 0) {
            final boolean keyboardVisible2 = this.keyboardVisible;
            if (!keyboardVisible2 && keyboardVisible2 != keyboardVisible && !this.isPopupShowing()) {
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
    
    public void onStickerSelected(final TLRPC.Document document, final Object o, final boolean b) {
        if (this.searchingType != 0) {
            this.searchingType = 0;
            this.emojiView.closeSearch(true);
            this.emojiView.hideSearchKeyboard();
        }
        this.setStickersExpanded(false, true, false);
        SendMessagesHelper.getInstance(this.currentAccount).sendSticker(document, this.dialog_id, this.replyingMessageObject, o);
        final ChatActivityEnterViewDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onMessageSend(null);
        }
        if (b) {
            this.setFieldText("");
        }
        DataQuery.getInstance(this.currentAccount).addRecentSticker(0, o, document, (int)(System.currentTimeMillis() / 1000L), false);
    }
    
    public void openKeyboard() {
        AndroidUtilities.showKeyboard((View)this.messageEditText);
    }
    
    public boolean processSendingText(final CharSequence charSequence) {
        final CharSequence trimmedString = AndroidUtilities.getTrimmedString(charSequence);
        final int maxMessageLength = MessagesController.getInstance(this.currentAccount).maxMessageLength;
        if (trimmedString.length() != 0) {
            final int n = (int)Math.ceil(trimmedString.length() / (float)maxMessageLength);
            int n2 = 0;
            while (true) {
                final int n3 = n2;
                if (n3 >= n) {
                    break;
                }
                final CharSequence[] array = { null };
                n2 = n3 + 1;
                array[0] = trimmedString.subSequence(n3 * maxMessageLength, Math.min(n2 * maxMessageLength, trimmedString.length()));
                SendMessagesHelper.getInstance(this.currentAccount).sendMessage(array[0].toString(), this.dialog_id, this.replyingMessageObject, this.messageWebPage, this.messageWebPageSearch, DataQuery.getInstance(this.currentAccount).getEntities(array), null, null);
            }
            return true;
        }
        return false;
    }
    
    public void replaceWithText(final int n, final int n2, final CharSequence charSequence, final boolean b) {
        try {
            final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)this.messageEditText.getText());
            text.replace(n, n2 + n, charSequence);
            if (b) {
                Emoji.replaceEmoji((CharSequence)text, this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            }
            this.messageEditText.setText((CharSequence)text);
            this.messageEditText.setSelection(n + charSequence.length());
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void setAllowStickersAndGifs(final boolean allowStickers, final boolean allowGifs) {
        if ((this.allowStickers != allowStickers || this.allowGifs != allowGifs) && this.emojiView != null) {
            if (this.emojiViewVisible) {
                this.hidePopup(false);
            }
            this.sizeNotifierLayout.removeView((View)this.emojiView);
            this.emojiView = null;
        }
        this.allowStickers = allowStickers;
        this.allowGifs = allowGifs;
        this.setEmojiButtonImage(false, this.isPaused ^ true);
    }
    
    public void setBotsCount(final int botCount, final boolean hasBotCommands) {
        this.botCount = botCount;
        if (this.hasBotCommands != hasBotCommands) {
            this.hasBotCommands = hasBotCommands;
            this.updateBotButton();
        }
    }
    
    public void setButtons(final MessageObject messageObject) {
        this.setButtons(messageObject, true);
    }
    
    public void setButtons(final MessageObject messageObject, final boolean b) {
        final MessageObject replyingMessageObject = this.replyingMessageObject;
        if (replyingMessageObject != null && replyingMessageObject == this.botButtonsMessageObject && replyingMessageObject != messageObject) {
            this.botMessageObject = messageObject;
            return;
        }
        if (this.botButton != null) {
            final MessageObject botButtonsMessageObject = this.botButtonsMessageObject;
            if (botButtonsMessageObject == null || botButtonsMessageObject != messageObject) {
                if (this.botButtonsMessageObject != null || messageObject != null) {
                    if (this.botKeyboardView == null) {
                        (this.botKeyboardView = new BotKeyboardView((Context)this.parentActivity)).setVisibility(8);
                        this.botKeyboardView.setDelegate((BotKeyboardView.BotKeyboardViewDelegate)new _$$Lambda$ChatActivityEnterView$b_Y8EmOjKWjLQcXw9E16k20A4eo(this));
                        this.sizeNotifierLayout.addView((View)this.botKeyboardView);
                    }
                    TLRPC.TL_replyKeyboardMarkup botReplyMarkup = null;
                    Label_0154: {
                        if ((this.botButtonsMessageObject = messageObject) != null) {
                            final TLRPC.ReplyMarkup reply_markup = messageObject.messageOwner.reply_markup;
                            if (reply_markup instanceof TLRPC.TL_replyKeyboardMarkup) {
                                botReplyMarkup = (TLRPC.TL_replyKeyboardMarkup)reply_markup;
                                break Label_0154;
                            }
                        }
                        botReplyMarkup = null;
                    }
                    this.botReplyMarkup = botReplyMarkup;
                    final BotKeyboardView botKeyboardView = this.botKeyboardView;
                    final Point displaySize = AndroidUtilities.displaySize;
                    int panelHeight;
                    if (displaySize.x > displaySize.y) {
                        panelHeight = this.keyboardHeightLand;
                    }
                    else {
                        panelHeight = this.keyboardHeight;
                    }
                    botKeyboardView.setPanelHeight(panelHeight);
                    this.botKeyboardView.setButtons(this.botReplyMarkup);
                    final TLRPC.TL_replyKeyboardMarkup botReplyMarkup2 = this.botReplyMarkup;
                    boolean b2 = false;
                    if (botReplyMarkup2 != null) {
                        final SharedPreferences mainSettings = MessagesController.getMainSettings(this.currentAccount);
                        final StringBuilder sb = new StringBuilder();
                        sb.append("hidekeyboard_");
                        sb.append(this.dialog_id);
                        final boolean b3 = mainSettings.getInt(sb.toString(), 0) == messageObject.getId();
                        Label_0364: {
                            if (this.botButtonsMessageObject != this.replyingMessageObject && this.botReplyMarkup.single_use) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("answered_");
                                sb2.append(this.dialog_id);
                                if (mainSettings.getInt(sb2.toString(), 0) == messageObject.getId()) {
                                    break Label_0364;
                                }
                            }
                            b2 = true;
                        }
                        if (b2 && !b3 && this.messageEditText.length() == 0 && !this.isPopupShowing()) {
                            this.showPopup(1, 1);
                        }
                    }
                    else if (this.isPopupShowing() && this.currentPopupContentType == 1) {
                        if (b) {
                            this.openKeyboardInternal();
                        }
                        else {
                            this.showPopup(0, 1);
                        }
                    }
                    this.updateBotButton();
                }
            }
        }
    }
    
    public void setCaption(final String caption) {
        final EditTextCaption messageEditText = this.messageEditText;
        if (messageEditText != null) {
            messageEditText.setCaption(caption);
            this.checkSendButton(true);
        }
    }
    
    public void setChatInfo(final TLRPC.ChatFull info) {
        this.info = info;
        final EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.setChatInfo(this.info);
        }
    }
    
    public void setCommand(final MessageObject messageObject, final String str, final boolean b, final boolean b2) {
        if (str != null) {
            if (this.getVisibility() == 0) {
                final TLRPC.User user = null;
                final TLRPC.User user2 = null;
                if (b) {
                    final String string = this.messageEditText.getText().toString();
                    TLRPC.User user3 = user2;
                    if (messageObject != null) {
                        user3 = user2;
                        if ((int)this.dialog_id < 0) {
                            user3 = MessagesController.getInstance(this.currentAccount).getUser(messageObject.messageOwner.from_id);
                        }
                    }
                    String text;
                    if ((this.botCount != 1 || b2) && user3 != null && user3.bot && !str.contains("@")) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(String.format(Locale.US, "%s@%s", str, user3.username));
                        sb.append(" ");
                        sb.append(string.replaceFirst("^/[a-zA-Z@\\d_]{1,255}(\\s|$)", ""));
                        text = sb.toString();
                    }
                    else {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append(" ");
                        sb2.append(string.replaceFirst("^/[a-zA-Z@\\d_]{1,255}(\\s|$)", ""));
                        text = sb2.toString();
                    }
                    this.ignoreTextChange = true;
                    this.messageEditText.setText((CharSequence)text);
                    final EditTextCaption messageEditText = this.messageEditText;
                    messageEditText.setSelection(messageEditText.getText().length());
                    this.ignoreTextChange = false;
                    final ChatActivityEnterViewDelegate delegate = this.delegate;
                    if (delegate != null) {
                        delegate.onTextChanged((CharSequence)this.messageEditText.getText(), true);
                    }
                    if (!this.keyboardVisible && this.currentPopupContentType == -1) {
                        this.openKeyboard();
                    }
                }
                else {
                    TLRPC.User user4 = user;
                    if (messageObject != null) {
                        user4 = user;
                        if ((int)this.dialog_id < 0) {
                            user4 = MessagesController.getInstance(this.currentAccount).getUser(messageObject.messageOwner.from_id);
                        }
                    }
                    if ((this.botCount != 1 || b2) && user4 != null && user4.bot && !str.contains("@")) {
                        SendMessagesHelper.getInstance(this.currentAccount).sendMessage(String.format(Locale.US, "%s@%s", str, user4.username), this.dialog_id, this.replyingMessageObject, null, false, null, null, null);
                    }
                    else {
                        SendMessagesHelper.getInstance(this.currentAccount).sendMessage(str, this.dialog_id, this.replyingMessageObject, null, false, null, null, null);
                    }
                }
            }
        }
    }
    
    public void setDelegate(final ChatActivityEnterViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setDialogId(final long dialog_id, int imageResource) {
        this.dialog_id = dialog_id;
        final int currentAccount = this.currentAccount;
        if (currentAccount != imageResource) {
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.recordStarted);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStartError);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStopped);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioDidSent);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioRouteChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
            this.currentAccount = imageResource;
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
            final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-(int)this.dialog_id);
            final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
            final StringBuilder sb = new StringBuilder();
            sb.append("silent_");
            sb.append(this.dialog_id);
            this.silent = notificationsSettings.getBoolean(sb.toString(), false);
            final boolean channel = ChatObject.isChannel(chat);
            final int n = 1;
            boolean canWriteToChannel = false;
            Label_0443: {
                Label_0440: {
                    if (channel) {
                        if (!chat.creator) {
                            final TLRPC.TL_chatAdminRights admin_rights = chat.admin_rights;
                            if (admin_rights == null || !admin_rights.post_messages) {
                                break Label_0440;
                            }
                        }
                        if (!chat.megagroup) {
                            canWriteToChannel = true;
                            break Label_0443;
                        }
                    }
                }
                canWriteToChannel = false;
            }
            this.canWriteToChannel = canWriteToChannel;
            final ImageView notifyButton = this.notifyButton;
            if (notifyButton != null) {
                if (this.canWriteToChannel) {
                    imageResource = 0;
                }
                else {
                    imageResource = 8;
                }
                notifyButton.setVisibility(imageResource);
                final ImageView notifyButton2 = this.notifyButton;
                if (this.silent) {
                    imageResource = 2131165489;
                }
                else {
                    imageResource = 2131165490;
                }
                notifyButton2.setImageResource(imageResource);
                final LinearLayout attachLayout = this.attachLayout;
                final ImageView botButton = this.botButton;
                float n2 = 0.0f;
                Label_0575: {
                    if (botButton == null || botButton.getVisibility() == 8) {
                        final ImageView notifyButton3 = this.notifyButton;
                        if (notifyButton3 == null || notifyButton3.getVisibility() == 8) {
                            n2 = 48.0f;
                            break Label_0575;
                        }
                    }
                    n2 = 96.0f;
                }
                attachLayout.setPivotX((float)AndroidUtilities.dp(n2));
            }
            final LinearLayout attachLayout2 = this.attachLayout;
            if (attachLayout2 != null) {
                if (attachLayout2.getVisibility() == 0) {
                    imageResource = n;
                }
                else {
                    imageResource = 0;
                }
                this.updateFieldRight(imageResource);
            }
        }
        this.checkRoundVideo();
        this.updateFieldHint();
    }
    
    public void setEditingMessageObject(MessageObject editingMessageObject, final boolean editingCaption) {
        if (this.audioToSend == null && this.videoToSendMessageObject == null) {
            if (this.editingMessageObject != editingMessageObject) {
                if (this.editingMessageReqId != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.editingMessageReqId, true);
                    this.editingMessageReqId = 0;
                }
                this.editingMessageObject = editingMessageObject;
                this.editingCaption = editingCaption;
                if (this.editingMessageObject != null) {
                    final AnimatorSet doneButtonAnimation = this.doneButtonAnimation;
                    if (doneButtonAnimation != null) {
                        doneButtonAnimation.cancel();
                        this.doneButtonAnimation = null;
                    }
                    this.doneButtonContainer.setVisibility(0);
                    this.showEditDoneProgress(true, false);
                    final InputFilter[] filters = { null };
                    CharSequence charSequence;
                    if (editingCaption) {
                        filters[0] = (InputFilter)new InputFilter$LengthFilter(MessagesController.getInstance(this.currentAccount).maxCaptionLength);
                        charSequence = this.editingMessageObject.caption;
                    }
                    else {
                        filters[0] = (InputFilter)new InputFilter$LengthFilter(MessagesController.getInstance(this.currentAccount).maxMessageLength);
                        charSequence = this.editingMessageObject.messageText;
                    }
                    if (charSequence != null) {
                        final ArrayList<TLRPC.MessageEntity> entities = this.editingMessageObject.messageOwner.entities;
                        DataQuery.sortEntities(entities);
                        editingMessageObject = (MessageObject)new SpannableStringBuilder(charSequence);
                        final Object[] spans = ((SpannableStringBuilder)editingMessageObject).getSpans(0, ((SpannableStringBuilder)editingMessageObject).length(), (Class)Object.class);
                        if (spans != null && spans.length > 0) {
                            for (int i = 0; i < spans.length; ++i) {
                                ((SpannableStringBuilder)editingMessageObject).removeSpan(spans[i]);
                            }
                        }
                        if (entities != null) {
                            int j = 0;
                            int n = 0;
                            try {
                                while (j < entities.size()) {
                                    final TLRPC.MessageEntity messageEntity = entities.get(j);
                                    int n2;
                                    if (messageEntity.offset + messageEntity.length + n > ((SpannableStringBuilder)editingMessageObject).length()) {
                                        n2 = n;
                                    }
                                    else if (messageEntity instanceof TLRPC.TL_inputMessageEntityMentionName) {
                                        if (messageEntity.offset + messageEntity.length + n < ((SpannableStringBuilder)editingMessageObject).length() && ((SpannableStringBuilder)editingMessageObject).charAt(messageEntity.offset + messageEntity.length + n) == ' ') {
                                            ++messageEntity.length;
                                        }
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append("");
                                        sb.append(((TLRPC.TL_inputMessageEntityMentionName)messageEntity).user_id.user_id);
                                        ((SpannableStringBuilder)editingMessageObject).setSpan((Object)new URLSpanUserMention(sb.toString(), 1), messageEntity.offset + n, messageEntity.offset + messageEntity.length + n, 33);
                                        n2 = n;
                                    }
                                    else if (messageEntity instanceof TLRPC.TL_messageEntityMentionName) {
                                        if (messageEntity.offset + messageEntity.length + n < ((SpannableStringBuilder)editingMessageObject).length() && ((SpannableStringBuilder)editingMessageObject).charAt(messageEntity.offset + messageEntity.length + n) == ' ') {
                                            ++messageEntity.length;
                                        }
                                        final StringBuilder sb2 = new StringBuilder();
                                        sb2.append("");
                                        sb2.append(((TLRPC.TL_messageEntityMentionName)messageEntity).user_id);
                                        ((SpannableStringBuilder)editingMessageObject).setSpan((Object)new URLSpanUserMention(sb2.toString(), 1), messageEntity.offset + n, messageEntity.offset + messageEntity.length + n, 33);
                                        n2 = n;
                                    }
                                    else if (messageEntity instanceof TLRPC.TL_messageEntityCode) {
                                        ((SpannableStringBuilder)editingMessageObject).insert(messageEntity.offset + messageEntity.length + n, (CharSequence)"`");
                                        ((SpannableStringBuilder)editingMessageObject).insert(messageEntity.offset + n, (CharSequence)"`");
                                        n2 = n + 2;
                                    }
                                    else if (messageEntity instanceof TLRPC.TL_messageEntityPre) {
                                        ((SpannableStringBuilder)editingMessageObject).insert(messageEntity.offset + messageEntity.length + n, (CharSequence)"```");
                                        ((SpannableStringBuilder)editingMessageObject).insert(messageEntity.offset + n, (CharSequence)"```");
                                        n2 = n + 6;
                                    }
                                    else if (messageEntity instanceof TLRPC.TL_messageEntityBold) {
                                        ((SpannableStringBuilder)editingMessageObject).setSpan((Object)new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), messageEntity.offset + n, messageEntity.offset + messageEntity.length + n, 33);
                                        n2 = n;
                                    }
                                    else if (messageEntity instanceof TLRPC.TL_messageEntityItalic) {
                                        ((SpannableStringBuilder)editingMessageObject).setSpan((Object)new TypefaceSpan(AndroidUtilities.getTypeface("fonts/ritalic.ttf")), messageEntity.offset + n, messageEntity.offset + messageEntity.length + n, 33);
                                        n2 = n;
                                    }
                                    else {
                                        n2 = n;
                                        if (messageEntity instanceof TLRPC.TL_messageEntityTextUrl) {
                                            ((SpannableStringBuilder)editingMessageObject).setSpan((Object)new URLSpanReplacement(messageEntity.url), messageEntity.offset + n, messageEntity.offset + messageEntity.length + n, 33);
                                            n2 = n;
                                        }
                                    }
                                    ++j;
                                    n = n2;
                                }
                            }
                            catch (Exception ex) {
                                FileLog.e(ex);
                            }
                        }
                        this.setFieldText(Emoji.replaceEmoji((CharSequence)new SpannableStringBuilder((CharSequence)editingMessageObject), this.messageEditText.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false));
                    }
                    else {
                        this.setFieldText("");
                    }
                    this.messageEditText.setFilters(filters);
                    this.openKeyboard();
                    final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.messageEditText.getLayoutParams();
                    layoutParams.rightMargin = AndroidUtilities.dp(4.0f);
                    this.messageEditText.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    this.sendButton.setVisibility(8);
                    this.cancelBotButton.setVisibility(8);
                    this.audioVideoButtonContainer.setVisibility(8);
                    this.attachLayout.setVisibility(8);
                    this.sendButtonContainer.setVisibility(8);
                }
                else {
                    this.doneButtonContainer.setVisibility(8);
                    this.messageEditText.setFilters(new InputFilter[0]);
                    this.delegate.onMessageEditEnd(false);
                    this.audioVideoButtonContainer.setVisibility(0);
                    this.attachLayout.setVisibility(0);
                    this.sendButtonContainer.setVisibility(0);
                    this.attachLayout.setScaleX(1.0f);
                    this.attachLayout.setAlpha(1.0f);
                    this.sendButton.setScaleX(0.1f);
                    this.sendButton.setScaleY(0.1f);
                    this.sendButton.setAlpha(0.0f);
                    this.cancelBotButton.setScaleX(0.1f);
                    this.cancelBotButton.setScaleY(0.1f);
                    this.cancelBotButton.setAlpha(0.0f);
                    this.audioVideoButtonContainer.setScaleX(1.0f);
                    this.audioVideoButtonContainer.setScaleY(1.0f);
                    this.audioVideoButtonContainer.setAlpha(1.0f);
                    this.sendButton.setVisibility(8);
                    this.cancelBotButton.setVisibility(8);
                    this.messageEditText.setText((CharSequence)"");
                    if (this.getVisibility() == 0) {
                        this.delegate.onAttachButtonShow();
                    }
                    this.updateFieldRight(1);
                }
                this.updateFieldHint();
            }
        }
    }
    
    public void setFieldFocused() {
        final AccessibilityManager accessibilityManager = (AccessibilityManager)this.parentActivity.getSystemService("accessibility");
        if (this.messageEditText != null && !accessibilityManager.isTouchExplorationEnabled()) {
            try {
                this.messageEditText.requestFocus();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
    }
    
    public void setFieldFocused(final boolean b) {
        final AccessibilityManager accessibilityManager = (AccessibilityManager)this.parentActivity.getSystemService("accessibility");
        if (this.messageEditText != null) {
            if (!accessibilityManager.isTouchExplorationEnabled()) {
                if (b) {
                    if (this.searchingType == 0 && !this.messageEditText.isFocused()) {
                        this.messageEditText.postDelayed((Runnable)new _$$Lambda$ChatActivityEnterView$w7v9u3TaN_X3t9L7_8faaWf5O3M(this), 600L);
                    }
                }
                else {
                    final EditTextCaption messageEditText = this.messageEditText;
                    if (messageEditText != null && messageEditText.isFocused() && !this.keyboardVisible) {
                        this.messageEditText.clearFocus();
                    }
                }
            }
        }
    }
    
    public void setFieldText(final CharSequence charSequence) {
        this.setFieldText(charSequence, true);
    }
    
    public void setFieldText(final CharSequence text, final boolean ignoreTextChange) {
        final EditTextCaption messageEditText = this.messageEditText;
        if (messageEditText == null) {
            return;
        }
        this.ignoreTextChange = ignoreTextChange;
        messageEditText.setText(text);
        final EditTextCaption messageEditText2 = this.messageEditText;
        messageEditText2.setSelection(messageEditText2.getText().length());
        this.ignoreTextChange = false;
        final ChatActivityEnterViewDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onTextChanged((CharSequence)this.messageEditText.getText(), true);
        }
    }
    
    public void setForceShowSendButton(final boolean forceShowSendButton, final boolean b) {
        this.forceShowSendButton = forceShowSendButton;
        this.checkSendButton(b);
    }
    
    public void setOpenGifsTabFirst() {
        this.createEmojiView();
        DataQuery.getInstance(this.currentAccount).loadRecents(0, true, true, false);
        this.emojiView.switchToGifRecent();
    }
    
    public void setReplyingMessageObject(final MessageObject replyingMessage) {
        if (replyingMessage != null) {
            if (this.botMessageObject == null) {
                final MessageObject botButtonsMessageObject = this.botButtonsMessageObject;
                if (botButtonsMessageObject != this.replyingMessageObject) {
                    this.botMessageObject = botButtonsMessageObject;
                }
            }
            this.setButtons(this.replyingMessageObject = replyingMessage, true);
        }
        else if (replyingMessage == null && this.replyingMessageObject == this.botButtonsMessageObject) {
            this.replyingMessageObject = null;
            this.setButtons(this.botMessageObject, false);
            this.botMessageObject = null;
        }
        else {
            this.replyingMessageObject = replyingMessage;
        }
        MediaController.getInstance().setReplyingMessage(replyingMessage);
    }
    
    public void setSelection(final int n) {
        final EditTextCaption messageEditText = this.messageEditText;
        if (messageEditText == null) {
            return;
        }
        messageEditText.setSelection(n, messageEditText.length());
    }
    
    public void setWebPage(final TLRPC.WebPage messageWebPage, final boolean messageWebPageSearch) {
        this.messageWebPage = messageWebPage;
        this.messageWebPageSearch = messageWebPageSearch;
    }
    
    public void showContextProgress(final boolean b) {
        final CloseProgressDrawable2 progressDrawable = this.progressDrawable;
        if (progressDrawable == null) {
            return;
        }
        if (b) {
            progressDrawable.startAnimation();
        }
        else {
            progressDrawable.stopAnimation();
        }
    }
    
    public void showEditDoneProgress(final boolean b, final boolean b2) {
        final AnimatorSet doneButtonAnimation = this.doneButtonAnimation;
        if (doneButtonAnimation != null) {
            doneButtonAnimation.cancel();
        }
        if (!b2) {
            if (b) {
                this.doneButtonImage.setScaleX(0.1f);
                this.doneButtonImage.setScaleY(0.1f);
                this.doneButtonImage.setAlpha(0.0f);
                this.doneButtonProgress.setScaleX(1.0f);
                this.doneButtonProgress.setScaleY(1.0f);
                this.doneButtonProgress.setAlpha(1.0f);
                this.doneButtonImage.setVisibility(4);
                this.doneButtonProgress.setVisibility(0);
                this.doneButtonContainer.setEnabled(false);
            }
            else {
                this.doneButtonProgress.setScaleX(0.1f);
                this.doneButtonProgress.setScaleY(0.1f);
                this.doneButtonProgress.setAlpha(0.0f);
                this.doneButtonImage.setScaleX(1.0f);
                this.doneButtonImage.setScaleY(1.0f);
                this.doneButtonImage.setAlpha(1.0f);
                this.doneButtonImage.setVisibility(0);
                this.doneButtonProgress.setVisibility(4);
                this.doneButtonContainer.setEnabled(true);
            }
        }
        else {
            this.doneButtonAnimation = new AnimatorSet();
            if (b) {
                this.doneButtonProgress.setVisibility(0);
                this.doneButtonContainer.setEnabled(false);
                this.doneButtonAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.doneButtonImage, View.SCALE_X, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneButtonImage, View.SCALE_Y, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneButtonImage, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneButtonProgress, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneButtonProgress, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneButtonProgress, View.ALPHA, new float[] { 1.0f }) });
            }
            else {
                this.doneButtonImage.setVisibility(0);
                this.doneButtonContainer.setEnabled(true);
                this.doneButtonAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.doneButtonProgress, View.SCALE_X, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneButtonProgress, View.SCALE_Y, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneButtonProgress, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneButtonImage, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneButtonImage, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneButtonImage, View.ALPHA, new float[] { 1.0f }) });
            }
            this.doneButtonAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator obj) {
                    if (ChatActivityEnterView.this.doneButtonAnimation != null && ChatActivityEnterView.this.doneButtonAnimation.equals(obj)) {
                        ChatActivityEnterView.this.doneButtonAnimation = null;
                    }
                }
                
                public void onAnimationEnd(final Animator obj) {
                    if (ChatActivityEnterView.this.doneButtonAnimation != null && ChatActivityEnterView.this.doneButtonAnimation.equals(obj)) {
                        if (!b) {
                            ChatActivityEnterView.this.doneButtonProgress.setVisibility(4);
                        }
                        else {
                            ChatActivityEnterView.this.doneButtonImage.setVisibility(4);
                        }
                    }
                }
            });
            this.doneButtonAnimation.setDuration(150L);
            this.doneButtonAnimation.start();
        }
    }
    
    public void showTopView(final boolean b, final boolean b2) {
        if (this.topView != null && !this.topViewShowed && this.getVisibility() == 0) {
            this.needShowTopView = true;
            this.topViewShowed = true;
            if (this.allowShowTopView) {
                this.topView.setVisibility(0);
                this.topLineView.setVisibility(0);
                final AnimatorSet currentTopViewAnimation = this.currentTopViewAnimation;
                if (currentTopViewAnimation != null) {
                    currentTopViewAnimation.cancel();
                    this.currentTopViewAnimation = null;
                }
                this.resizeForTopView(true);
                if (b) {
                    (this.currentTopViewAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.topView, View.TRANSLATION_Y, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.topLineView, View.ALPHA, new float[] { 1.0f }) });
                    this.currentTopViewAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator obj) {
                            if (ChatActivityEnterView.this.currentTopViewAnimation != null && ChatActivityEnterView.this.currentTopViewAnimation.equals(obj)) {
                                ChatActivityEnterView.this.currentTopViewAnimation = null;
                            }
                        }
                    });
                    this.currentTopViewAnimation.setDuration(250L);
                    this.currentTopViewAnimation.setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT);
                    this.currentTopViewAnimation.start();
                }
                else {
                    this.topView.setTranslationY(0.0f);
                    this.topLineView.setAlpha(1.0f);
                }
                if (this.recordedAudioPanel.getVisibility() != 0 && (!this.forceShowSendButton || b2)) {
                    this.messageEditText.requestFocus();
                    this.openKeyboard();
                }
            }
            return;
        }
        if (this.recordedAudioPanel.getVisibility() != 0 && (!this.forceShowSendButton || b2)) {
            this.openKeyboard();
        }
    }
    
    public interface ChatActivityEnterViewDelegate
    {
        void didPressedAttachButton();
        
        void needChangeVideoPreviewState(final int p0, final float p1);
        
        void needSendTyping();
        
        void needShowMediaBanHint();
        
        void needStartRecordAudio(final int p0);
        
        void needStartRecordVideo(final int p0);
        
        void onAttachButtonHidden();
        
        void onAttachButtonShow();
        
        void onMessageEditEnd(final boolean p0);
        
        void onMessageSend(final CharSequence p0);
        
        void onPreAudioVideoRecord();
        
        void onStickersExpandedChange();
        
        void onStickersTab(final boolean p0);
        
        void onSwitchRecordMode(final boolean p0);
        
        void onTextChanged(final CharSequence p0, final boolean p1);
        
        void onTextSelectionChanged(final int p0, final int p1);
        
        void onTextSpansChanged(final CharSequence p0);
        
        void onWindowSizeChanged(final int p0);
    }
    
    private class RecordCircle extends View
    {
        private float amplitude;
        private float animateAmplitudeDiff;
        private float animateToAmplitude;
        private long lastUpdateTime;
        private float lockAnimatedTranslation;
        private boolean pressed;
        private float scale;
        private boolean sendButtonVisible;
        private float startTranslation;
        private VirtualViewHelper virtualViewHelper;
        
        public RecordCircle(final Context context) {
            super(context);
            ChatActivityEnterView.this.paint.setColor(Theme.getColor("chat_messagePanelVoiceBackground"));
            ChatActivityEnterView.this.paintRecord.setColor(Theme.getColor("chat_messagePanelVoiceShadow"));
            ChatActivityEnterView.this.lockDrawable = this.getResources().getDrawable(2131165543);
            ChatActivityEnterView.this.lockDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("key_chat_messagePanelVoiceLock"), PorterDuff$Mode.MULTIPLY));
            ChatActivityEnterView.this.lockTopDrawable = this.getResources().getDrawable(2131165547);
            ChatActivityEnterView.this.lockTopDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("key_chat_messagePanelVoiceLock"), PorterDuff$Mode.MULTIPLY));
            ChatActivityEnterView.this.lockArrowDrawable = this.getResources().getDrawable(2131165541);
            ChatActivityEnterView.this.lockArrowDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("key_chat_messagePanelVoiceLock"), PorterDuff$Mode.MULTIPLY));
            ChatActivityEnterView.this.lockBackgroundDrawable = this.getResources().getDrawable(2131165545);
            ChatActivityEnterView.this.lockBackgroundDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("key_chat_messagePanelVoiceLockBackground"), PorterDuff$Mode.MULTIPLY));
            ChatActivityEnterView.this.lockShadowDrawable = this.getResources().getDrawable(2131165546);
            ChatActivityEnterView.this.lockShadowDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("key_chat_messagePanelVoiceLockShadow"), PorterDuff$Mode.MULTIPLY));
            ChatActivityEnterView.this.micDrawable = this.getResources().getDrawable(2131165488).mutate();
            ChatActivityEnterView.this.micDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelVoicePressed"), PorterDuff$Mode.MULTIPLY));
            ChatActivityEnterView.this.cameraDrawable = this.getResources().getDrawable(2131165494).mutate();
            ChatActivityEnterView.this.cameraDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelVoicePressed"), PorterDuff$Mode.MULTIPLY));
            ChatActivityEnterView.this.sendDrawable = this.getResources().getDrawable(2131165468).mutate();
            ChatActivityEnterView.this.sendDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelVoicePressed"), PorterDuff$Mode.MULTIPLY));
            ViewCompat.setAccessibilityDelegate(this, this.virtualViewHelper = new VirtualViewHelper(this));
        }
        
        protected boolean dispatchHoverEvent(final MotionEvent motionEvent) {
            return super.dispatchHoverEvent(motionEvent) || this.virtualViewHelper.dispatchHoverEvent(motionEvent);
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
        
        protected void onDraw(final Canvas canvas) {
            final int n = this.getMeasuredWidth() / 2;
            final int dp = AndroidUtilities.dp(170.0f);
            final float lockAnimatedTranslation = this.lockAnimatedTranslation;
            float n2;
            if (lockAnimatedTranslation != 10000.0f) {
                if ((n2 = (float)Math.max(0, (int)(this.startTranslation - lockAnimatedTranslation))) > AndroidUtilities.dp(57.0f)) {
                    n2 = (float)AndroidUtilities.dp(57.0f);
                }
            }
            else {
                n2 = 0.0f;
            }
            final int n3 = (int)(dp - n2);
            final float scale = this.scale;
            float n5;
            float n4;
            if (scale <= 0.5f) {
                n4 = (n5 = scale / 0.5f);
            }
            else {
                if (scale <= 0.75f) {
                    n4 = 1.0f - (scale - 0.5f) / 0.25f * 0.1f;
                }
                else {
                    n4 = (scale - 0.75f) / 0.25f * 0.1f + 0.9f;
                }
                n5 = 1.0f;
            }
            final long currentTimeMillis = System.currentTimeMillis();
            final long lastUpdateTime = this.lastUpdateTime;
            final float animateToAmplitude = this.animateToAmplitude;
            final float amplitude = this.amplitude;
            if (animateToAmplitude != amplitude) {
                final float animateAmplitudeDiff = this.animateAmplitudeDiff;
                this.amplitude = amplitude + (currentTimeMillis - lastUpdateTime) * animateAmplitudeDiff;
                if (animateAmplitudeDiff > 0.0f) {
                    if (this.amplitude > animateToAmplitude) {
                        this.amplitude = animateToAmplitude;
                    }
                }
                else if (this.amplitude < animateToAmplitude) {
                    this.amplitude = animateToAmplitude;
                }
                this.invalidate();
            }
            this.lastUpdateTime = System.currentTimeMillis();
            if (this.amplitude != 0.0f) {
                canvas.drawCircle(this.getMeasuredWidth() / 2.0f, (float)n3, (AndroidUtilities.dp(42.0f) + AndroidUtilities.dp(20.0f) * this.amplitude) * this.scale, ChatActivityEnterView.this.paintRecord);
            }
            canvas.drawCircle(this.getMeasuredWidth() / 2.0f, (float)n3, AndroidUtilities.dp(42.0f) * n4, ChatActivityEnterView.this.paint);
            Drawable drawable;
            if (this.isSendButtonVisible()) {
                drawable = ChatActivityEnterView.this.sendDrawable;
            }
            else if (ChatActivityEnterView.this.videoSendButton != null && ChatActivityEnterView.this.videoSendButton.getTag() != null) {
                drawable = ChatActivityEnterView.this.cameraDrawable;
            }
            else {
                drawable = ChatActivityEnterView.this.micDrawable;
            }
            drawable.setBounds(n - drawable.getIntrinsicWidth() / 2, n3 - drawable.getIntrinsicHeight() / 2, drawable.getIntrinsicWidth() / 2 + n, n3 + drawable.getIntrinsicHeight() / 2);
            final int alpha = (int)(n5 * 255.0f);
            drawable.setAlpha(alpha);
            drawable.draw(canvas);
            final float n6 = 1.0f - n2 / AndroidUtilities.dp(57.0f);
            final float max = Math.max(0.0f, 1.0f - n2 / AndroidUtilities.dp(57.0f) * 2.0f);
            int dp2;
            int n7;
            int n8;
            int n9;
            int n11;
            if (this.isSendButtonVisible()) {
                dp2 = AndroidUtilities.dp(31.0f);
                n7 = AndroidUtilities.dp(57.0f) + (int)(AndroidUtilities.dp(30.0f) * (1.0f - n4) - n2 + AndroidUtilities.dp(20.0f) * n6);
                final int dp3 = AndroidUtilities.dp(5.0f);
                n8 = AndroidUtilities.dp(11.0f) + n7;
                n9 = AndroidUtilities.dp(25.0f) + n7;
                final int n10 = (int)(alpha * (n2 / AndroidUtilities.dp(57.0f)));
                ChatActivityEnterView.this.lockBackgroundDrawable.setAlpha(255);
                ChatActivityEnterView.this.lockShadowDrawable.setAlpha(255);
                ChatActivityEnterView.this.lockTopDrawable.setAlpha(n10);
                ChatActivityEnterView.this.lockDrawable.setAlpha(n10);
                ChatActivityEnterView.this.lockArrowDrawable.setAlpha((int)(n10 * max));
                n11 = dp3 + n7;
            }
            else {
                dp2 = AndroidUtilities.dp(31.0f) + (int)(AndroidUtilities.dp(29.0f) * n6);
                n7 = AndroidUtilities.dp(57.0f) + (int)(AndroidUtilities.dp(30.0f) * (1.0f - n4)) - (int)n2;
                n11 = AndroidUtilities.dp(5.0f) + n7 + (int)(AndroidUtilities.dp(4.0f) * n6);
                n8 = (int)(AndroidUtilities.dp(10.0f) * n6) + (AndroidUtilities.dp(11.0f) + n7);
                n9 = AndroidUtilities.dp(25.0f) + n7 + (int)(AndroidUtilities.dp(16.0f) * n6);
                ChatActivityEnterView.this.lockBackgroundDrawable.setAlpha(alpha);
                ChatActivityEnterView.this.lockShadowDrawable.setAlpha(alpha);
                ChatActivityEnterView.this.lockTopDrawable.setAlpha(alpha);
                ChatActivityEnterView.this.lockDrawable.setAlpha(alpha);
                ChatActivityEnterView.this.lockArrowDrawable.setAlpha((int)(alpha * max));
            }
            final Drawable access$3700 = ChatActivityEnterView.this.lockBackgroundDrawable;
            final int dp4 = AndroidUtilities.dp(15.0f);
            final int dp5 = AndroidUtilities.dp(15.0f);
            final int n12 = dp2 + n7;
            access$3700.setBounds(n - dp4, n7, dp5 + n, n12);
            ChatActivityEnterView.this.lockBackgroundDrawable.draw(canvas);
            ChatActivityEnterView.this.lockShadowDrawable.setBounds(n - AndroidUtilities.dp(16.0f), n7 - AndroidUtilities.dp(1.0f), AndroidUtilities.dp(16.0f) + n, n12 + AndroidUtilities.dp(1.0f));
            ChatActivityEnterView.this.lockShadowDrawable.draw(canvas);
            ChatActivityEnterView.this.lockTopDrawable.setBounds(n - AndroidUtilities.dp(6.0f), n11, AndroidUtilities.dp(6.0f) + n, AndroidUtilities.dp(14.0f) + n11);
            ChatActivityEnterView.this.lockTopDrawable.draw(canvas);
            ChatActivityEnterView.this.lockDrawable.setBounds(n - AndroidUtilities.dp(7.0f), n8, AndroidUtilities.dp(7.0f) + n, AndroidUtilities.dp(12.0f) + n8);
            ChatActivityEnterView.this.lockDrawable.draw(canvas);
            ChatActivityEnterView.this.lockArrowDrawable.setBounds(n - AndroidUtilities.dp(7.5f), n9, AndroidUtilities.dp(7.5f) + n, AndroidUtilities.dp(9.0f) + n9);
            ChatActivityEnterView.this.lockArrowDrawable.draw(canvas);
            if (this.isSendButtonVisible()) {
                ChatActivityEnterView.this.redDotPaint.setAlpha(255);
                ChatActivityEnterView.this.rect.set((float)(n - AndroidUtilities.dp2(6.5f)), (float)(AndroidUtilities.dp(9.0f) + n7), (float)(n + AndroidUtilities.dp(6.5f)), (float)(n7 + AndroidUtilities.dp(22.0f)));
                canvas.drawRoundRect(ChatActivityEnterView.this.rect, (float)AndroidUtilities.dp(1.0f), (float)AndroidUtilities.dp(1.0f), ChatActivityEnterView.this.redDotPaint);
            }
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            if (this.sendButtonVisible) {
                final int n = (int)motionEvent.getX();
                final int n2 = (int)motionEvent.getY();
                if (motionEvent.getAction() == 0) {
                    return this.pressed = ChatActivityEnterView.this.lockBackgroundDrawable.getBounds().contains(n, n2);
                }
                if (this.pressed) {
                    if (motionEvent.getAction() == 1 && ChatActivityEnterView.this.lockBackgroundDrawable.getBounds().contains(n, n2)) {
                        if (ChatActivityEnterView.this.videoSendButton != null && ChatActivityEnterView.this.videoSendButton.getTag() != null) {
                            ChatActivityEnterView.this.delegate.needStartRecordVideo(3);
                        }
                        else {
                            MediaController.getInstance().stopRecording(2);
                            ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        
        public void setAmplitude(final double b) {
            this.animateToAmplitude = (float)Math.min(100.0, b) / 100.0f;
            this.animateAmplitudeDiff = (this.animateToAmplitude - this.amplitude) / 150.0f;
            this.lastUpdateTime = System.currentTimeMillis();
            this.invalidate();
        }
        
        @Keep
        public void setLockAnimatedTranslation(final float lockAnimatedTranslation) {
            this.lockAnimatedTranslation = lockAnimatedTranslation;
            this.invalidate();
        }
        
        public int setLockTranslation(final float n) {
            if (n == 10000.0f) {
                this.sendButtonVisible = false;
                this.lockAnimatedTranslation = -1.0f;
                this.startTranslation = -1.0f;
                this.invalidate();
                return 0;
            }
            if (this.sendButtonVisible) {
                return 2;
            }
            if (this.lockAnimatedTranslation == -1.0f) {
                this.startTranslation = n;
            }
            this.lockAnimatedTranslation = n;
            this.invalidate();
            if (this.startTranslation - this.lockAnimatedTranslation >= AndroidUtilities.dp(57.0f)) {
                this.sendButtonVisible = true;
                return 2;
            }
            return 1;
        }
        
        @Keep
        public void setScale(final float scale) {
            this.scale = scale;
            this.invalidate();
        }
        
        public void setSendButtonInvisible() {
            this.sendButtonVisible = false;
            this.invalidate();
        }
        
        private class VirtualViewHelper extends ExploreByTouchHelper
        {
            public VirtualViewHelper(final View view) {
                super(view);
            }
            
            @Override
            protected int getVirtualViewAt(final float n, final float n2) {
                if (RecordCircle.this.isSendButtonVisible()) {
                    final Rect bounds = ChatActivityEnterView.this.sendDrawable.getBounds();
                    final int n3 = (int)n;
                    final int n4 = (int)n2;
                    if (bounds.contains(n3, n4)) {
                        return 1;
                    }
                    if (ChatActivityEnterView.this.lockBackgroundDrawable.getBounds().contains(n3, n4)) {
                        return 2;
                    }
                }
                return -1;
            }
            
            @Override
            protected void getVisibleVirtualViews(final List<Integer> list) {
                if (RecordCircle.this.isSendButtonVisible()) {
                    list.add(1);
                    list.add(2);
                }
            }
            
            @Override
            protected boolean onPerformActionForVirtualView(final int n, final int n2, final Bundle bundle) {
                return true;
            }
            
            @Override
            protected void onPopulateNodeForVirtualView(final int n, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                if (n == 1) {
                    accessibilityNodeInfoCompat.setBoundsInParent(ChatActivityEnterView.this.sendDrawable.getBounds());
                    accessibilityNodeInfoCompat.setText(LocaleController.getString("Send", 2131560685));
                }
                else if (n == 2) {
                    accessibilityNodeInfoCompat.setBoundsInParent(ChatActivityEnterView.this.lockBackgroundDrawable.getBounds());
                    accessibilityNodeInfoCompat.setText(LocaleController.getString("Stop", 2131560820));
                }
            }
        }
    }
    
    private class RecordDot extends View
    {
        private float alpha;
        private boolean isIncr;
        private long lastUpdateTime;
        
        public RecordDot(final Context context) {
            super(context);
            ChatActivityEnterView.this.redDotPaint.setColor(Theme.getColor("chat_recordedVoiceDot"));
        }
        
        protected void onDraw(final Canvas canvas) {
            ChatActivityEnterView.this.redDotPaint.setAlpha((int)(this.alpha * 255.0f));
            final long n = System.currentTimeMillis() - this.lastUpdateTime;
            if (!this.isIncr) {
                this.alpha -= n / 400.0f;
                if (this.alpha <= 0.0f) {
                    this.alpha = 0.0f;
                    this.isIncr = true;
                }
            }
            else {
                this.alpha += n / 400.0f;
                if (this.alpha >= 1.0f) {
                    this.alpha = 1.0f;
                    this.isIncr = false;
                }
            }
            this.lastUpdateTime = System.currentTimeMillis();
            canvas.drawCircle((float)AndroidUtilities.dp(5.0f), (float)AndroidUtilities.dp(5.0f), (float)AndroidUtilities.dp(5.0f), ChatActivityEnterView.this.redDotPaint);
            this.invalidate();
        }
        
        public void resetAlpha() {
            this.alpha = 1.0f;
            this.lastUpdateTime = System.currentTimeMillis();
            this.isIncr = false;
            this.invalidate();
        }
    }
    
    private class ScrimDrawable extends Drawable
    {
        private Paint paint;
        
        public ScrimDrawable() {
            (this.paint = new Paint()).setColor(0);
        }
        
        public void draw(final Canvas canvas) {
            if (ChatActivityEnterView.this.emojiView == null) {
                return;
            }
            this.paint.setAlpha(Math.round(ChatActivityEnterView.this.stickersExpansionProgress * 102.0f));
            canvas.drawRect(0.0f, 0.0f, (float)ChatActivityEnterView.this.getWidth(), ChatActivityEnterView.this.emojiView.getY() - ChatActivityEnterView.this.getHeight() + Theme.chat_composeShadowDrawable.getIntrinsicHeight(), this.paint);
        }
        
        public int getOpacity() {
            return -2;
        }
        
        public void setAlpha(final int n) {
        }
        
        public void setColorFilter(final ColorFilter colorFilter) {
        }
    }
    
    private class SeekBarWaveformView extends View
    {
        public SeekBarWaveformView(final Context context) {
            super(context);
            ChatActivityEnterView.this.seekBarWaveform = new SeekBarWaveform(context);
            ChatActivityEnterView.this.seekBarWaveform.setDelegate(new _$$Lambda$ChatActivityEnterView$SeekBarWaveformView$3LQzMaku4bFncUmbMthE8hwccgc(this));
        }
        
        public boolean isDragging() {
            return ChatActivityEnterView.this.seekBarWaveform.isDragging();
        }
        
        protected void onDraw(final Canvas canvas) {
            super.onDraw(canvas);
            ChatActivityEnterView.this.seekBarWaveform.setColors(Theme.getColor("chat_recordedVoiceProgress"), Theme.getColor("chat_recordedVoiceProgressInner"), Theme.getColor("chat_recordedVoiceProgress"));
            ChatActivityEnterView.this.seekBarWaveform.draw(canvas);
        }
        
        protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
            super.onLayout(b, n, n2, n3, n4);
            ChatActivityEnterView.this.seekBarWaveform.setSize(n3 - n, n4 - n2);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final boolean onTouch = ChatActivityEnterView.this.seekBarWaveform.onTouch(motionEvent.getAction(), motionEvent.getX(), motionEvent.getY());
            final boolean b = true;
            if (onTouch) {
                if (motionEvent.getAction() == 0) {
                    ChatActivityEnterView.this.requestDisallowInterceptTouchEvent(true);
                }
                this.invalidate();
            }
            boolean b2 = b;
            if (!onTouch) {
                b2 = (super.onTouchEvent(motionEvent) && b);
            }
            return b2;
        }
        
        public void setProgress(final float progress) {
            ChatActivityEnterView.this.seekBarWaveform.setProgress(progress);
            this.invalidate();
        }
        
        public void setWaveform(final byte[] waveform) {
            ChatActivityEnterView.this.seekBarWaveform.setWaveform(waveform);
            this.invalidate();
        }
    }
}
