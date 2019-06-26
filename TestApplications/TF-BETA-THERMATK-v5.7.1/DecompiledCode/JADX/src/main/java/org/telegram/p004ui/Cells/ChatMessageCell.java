package org.telegram.p004ui.Cells;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.SparseArray;
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.NotificationUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.DownloadController.FileDownloadProgressListener;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.ImageReceiver.ImageReceiverDelegate;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessageObject.GroupedMessagePosition;
import org.telegram.messenger.MessageObject.GroupedMessages;
import org.telegram.messenger.MessageObject.TextLayoutBlock;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.WebFile;
import org.telegram.messenger.browser.Browser;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.AnimatedFileDrawable;
import org.telegram.p004ui.Components.AvatarDrawable;
import org.telegram.p004ui.Components.CheckBoxBase;
import org.telegram.p004ui.Components.CubicBezierInterpolator;
import org.telegram.p004ui.Components.LinkPath;
import org.telegram.p004ui.Components.RadialProgress2;
import org.telegram.p004ui.Components.RoundVideoPlayingDrawable;
import org.telegram.p004ui.Components.SeekBar;
import org.telegram.p004ui.Components.SeekBar.SeekBarDelegate;
import org.telegram.p004ui.Components.SeekBarWaveform;
import org.telegram.p004ui.Components.StaticLayoutEx;
import org.telegram.p004ui.Components.URLSpanMono;
import org.telegram.p004ui.Components.URLSpanNoUnderline;
import org.telegram.p004ui.PhotoViewer;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.GeoPoint;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.MessageFwdHeader;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.Peer;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC.TL_messageMediaGame;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
import org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC.TL_photoStrippedSize;
import org.telegram.tgnet.TLRPC.TL_poll;
import org.telegram.tgnet.TLRPC.TL_pollAnswer;
import org.telegram.tgnet.TLRPC.TL_pollAnswerVoters;
import org.telegram.tgnet.TLRPC.TL_user;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.WebPage;

/* renamed from: org.telegram.ui.Cells.ChatMessageCell */
public class ChatMessageCell extends BaseCell implements SeekBarDelegate, ImageReceiverDelegate, FileDownloadProgressListener {
    private static final int DOCUMENT_ATTACH_TYPE_AUDIO = 3;
    private static final int DOCUMENT_ATTACH_TYPE_DOCUMENT = 1;
    private static final int DOCUMENT_ATTACH_TYPE_GIF = 2;
    private static final int DOCUMENT_ATTACH_TYPE_MUSIC = 5;
    private static final int DOCUMENT_ATTACH_TYPE_NONE = 0;
    private static final int DOCUMENT_ATTACH_TYPE_ROUND = 7;
    private static final int DOCUMENT_ATTACH_TYPE_STICKER = 6;
    private static final int DOCUMENT_ATTACH_TYPE_VIDEO = 4;
    private static final int DOCUMENT_ATTACH_TYPE_WALLPAPER = 8;
    private int TAG;
    private SparseArray<Rect> accessibilityVirtualViewBounds = new SparseArray();
    private int addedCaptionHeight;
    private boolean addedForTest;
    private StaticLayout adminLayout;
    private boolean allowAssistant;
    private boolean animatePollAnswer;
    private boolean animatePollAnswerAlpha;
    private int animatingDrawVideoImageButton;
    private float animatingDrawVideoImageButtonProgress;
    private int animatingNoSound;
    private boolean animatingNoSoundPlaying;
    private float animatingNoSoundProgress;
    private boolean attachedToWindow;
    private StaticLayout authorLayout;
    private int authorX;
    private boolean autoPlayingMedia;
    private int availableTimeWidth;
    private AvatarDrawable avatarDrawable;
    private ImageReceiver avatarImage = new ImageReceiver();
    private boolean avatarPressed;
    private int backgroundDrawableLeft;
    private int backgroundDrawableRight;
    private int backgroundWidth = 100;
    private ArrayList<BotButton> botButtons = new ArrayList();
    private HashMap<String, BotButton> botButtonsByData = new HashMap();
    private HashMap<String, BotButton> botButtonsByPosition = new HashMap();
    private String botButtonsLayout;
    private boolean bottomNearToSet;
    private int buttonPressed;
    private int buttonState;
    private int buttonX;
    private int buttonY;
    private boolean canStreamVideo;
    private boolean cancelLoading;
    private int captionHeight;
    private StaticLayout captionLayout;
    private int captionOffsetX;
    private int captionWidth;
    private int captionX;
    private int captionY;
    private CheckBoxBase checkBox;
    private boolean checkBoxAnimationInProgress;
    private float checkBoxAnimationProgress;
    private int checkBoxTranslation;
    private boolean checkBoxVisible;
    private boolean checkOnlyButtonPressed;
    private AvatarDrawable contactAvatarDrawable;
    private float controlsAlpha = 1.0f;
    private int currentAccount = UserConfig.selectedAccount;
    private Drawable currentBackgroundDrawable;
    private CharSequence currentCaption;
    private Chat currentChat;
    private int currentFocusedVirtualView = -1;
    private Chat currentForwardChannel;
    private String currentForwardName;
    private String currentForwardNameString;
    private User currentForwardUser;
    private int currentMapProvider;
    private MessageObject currentMessageObject;
    private GroupedMessages currentMessagesGroup;
    private String currentNameString;
    private FileLocation currentPhoto;
    private String currentPhotoFilter;
    private String currentPhotoFilterThumb;
    private PhotoSize currentPhotoObject;
    private PhotoSize currentPhotoObjectThumb;
    private GroupedMessagePosition currentPosition;
    private PhotoSize currentReplyPhoto;
    private String currentTimeString;
    private String currentUrl;
    private User currentUser;
    private User currentViaBotUser;
    private String currentViewsString;
    private WebFile currentWebFile;
    private ChatMessageCellDelegate delegate;
    private RectF deleteProgressRect = new RectF();
    private StaticLayout descriptionLayout;
    private int descriptionX;
    private int descriptionY;
    private boolean disallowLongPress;
    private StaticLayout docTitleLayout;
    private int docTitleOffsetX;
    private int docTitleWidth;
    private Document documentAttach;
    private int documentAttachType;
    private boolean drawBackground = true;
    private boolean drawForwardedName;
    private boolean drawImageButton;
    private boolean drawInstantView;
    private int drawInstantViewType;
    private boolean drawJoinChannelView;
    private boolean drawJoinGroupView;
    private boolean drawName;
    private boolean drawNameLayout;
    private boolean drawPhotoCheckBox;
    private boolean drawPhotoImage;
    private boolean drawPinnedBottom;
    private boolean drawPinnedTop;
    private boolean drawRadialCheckBackground;
    private boolean drawSelectionBackground;
    private boolean drawShareButton;
    private boolean drawTime = true;
    private boolean drawVideoImageButton;
    private boolean drawVideoSize;
    private boolean drwaShareGoIcon;
    private StaticLayout durationLayout;
    private int durationWidth;
    private boolean firstCircleLength;
    private int firstVisibleBlockNum;
    private boolean forceNotDrawTime;
    private boolean forwardBotPressed;
    private boolean forwardName;
    private int forwardNameCenterX;
    private float[] forwardNameOffsetX = new float[2];
    private boolean forwardNamePressed;
    private int forwardNameX;
    private int forwardNameY;
    private StaticLayout[] forwardedNameLayout = new StaticLayout[2];
    private int forwardedNameWidth;
    private boolean fullyDraw;
    private boolean gamePreviewPressed;
    private boolean groupPhotoInvisible;
    private GroupedMessages groupedMessagesToSet;
    private boolean hasEmbed;
    private boolean hasGamePreview;
    private boolean hasInvoicePreview;
    private boolean hasLinkPreview;
    private int hasMiniProgress;
    private boolean hasNewLineForTime;
    private boolean hasOldCaptionPreview;
    private int highlightProgress;
    private int imageBackgroundColor;
    private int imageBackgroundSideColor;
    private int imageBackgroundSideWidth;
    private boolean imagePressed;
    private boolean inLayout;
    private StaticLayout infoLayout;
    private int infoWidth;
    private int infoX;
    private boolean instantButtonPressed;
    private RectF instantButtonRect = new RectF();
    private boolean instantPressed;
    private int instantTextLeftX;
    private int instantTextX;
    private StaticLayout instantViewLayout;
    private int instantWidth;
    private Runnable invalidateRunnable = new C23461();
    private boolean invalidatesParent;
    private boolean isAvatarVisible;
    public boolean isChat;
    private boolean isCheckPressed = true;
    private boolean isHighlighted;
    private boolean isHighlightedAnimated;
    public boolean isMegagroup;
    private boolean isPressed;
    private boolean isSmallImage;
    private int keyboardHeight;
    private long lastAnimationTime;
    private long lastCheckBoxAnimationTime;
    private long lastControlsAlphaChangeTime;
    private int lastDeleteDate;
    private int lastHeight;
    private long lastHighlightProgressTime;
    private TL_poll lastPoll;
    private ArrayList<TL_pollAnswerVoters> lastPollResults;
    private int lastPollResultsVoters;
    private int lastSendState;
    private int lastTime;
    private float lastTouchX;
    private float lastTouchY;
    private int lastViewsCount;
    private int lastVisibleBlockNum;
    private int layoutHeight;
    private int layoutWidth;
    private int linkBlockNum;
    private int linkPreviewHeight;
    private boolean linkPreviewPressed;
    private int linkSelectionBlockNum;
    private boolean locationExpired;
    private ImageReceiver locationImageReceiver;
    private boolean mediaBackground;
    private int mediaOffsetY;
    private boolean mediaWasInvisible;
    private MessageObject messageObjectToSet;
    private int miniButtonPressed;
    private int miniButtonState;
    private StaticLayout nameLayout;
    private float nameOffsetX;
    private int nameWidth;
    private float nameX;
    private float nameY;
    private int namesOffset;
    private boolean needNewVisiblePart;
    private boolean needReplyImage;
    private int noSoundCenterX;
    private boolean otherPressed;
    private int otherX;
    private int otherY;
    private StaticLayout performerLayout;
    private int performerX;
    private CheckBoxBase photoCheckBox;
    private ImageReceiver photoImage;
    private boolean photoNotSet;
    private TLObject photoParentObject;
    private StaticLayout photosCountLayout;
    private int photosCountWidth;
    private boolean pinnedBottom;
    private boolean pinnedTop;
    private float pollAnimationProgress;
    private float pollAnimationProgressTime;
    private ArrayList<PollButton> pollButtons = new ArrayList();
    private boolean pollClosed;
    private boolean pollUnvoteInProgress;
    private boolean pollVoteInProgress;
    private int pollVoteInProgressNum;
    private boolean pollVoted;
    private int pressedBotButton;
    private CharacterStyle pressedLink;
    private int pressedLinkType;
    private int[] pressedState = new int[]{16842910, 16842919};
    private int pressedVoteButton;
    private RadialProgress2 radialProgress;
    private RectF rect = new RectF();
    private ImageReceiver replyImageReceiver;
    private StaticLayout replyNameLayout;
    private float replyNameOffset;
    private int replyNameWidth;
    private boolean replyPressed;
    private int replyStartX;
    private int replyStartY;
    private StaticLayout replyTextLayout;
    private float replyTextOffset;
    private int replyTextWidth;
    private RoundVideoPlayingDrawable roundVideoPlayingDrawable;
    private boolean scheduledInvalidate;
    private Rect scrollRect = new Rect();
    private SeekBar seekBar;
    private SeekBarWaveform seekBarWaveform;
    private int seekBarX;
    private int seekBarY;
    private Drawable selectorDrawable;
    private boolean sharePressed;
    private int shareStartX;
    private int shareStartY;
    private StaticLayout siteNameLayout;
    private boolean siteNameRtl;
    private int siteNameWidth;
    private StaticLayout songLayout;
    private int songX;
    private int substractBackgroundHeight;
    private int textX;
    private int textY;
    private float timeAlpha = 1.0f;
    private int timeAudioX;
    private StaticLayout timeLayout;
    private int timeTextWidth;
    private boolean timeWasInvisible;
    private int timeWidth;
    private int timeWidthAudio;
    private int timeX;
    private StaticLayout titleLayout;
    private int titleX;
    private boolean topNearToSet;
    private long totalChangeTime;
    private int totalHeight;
    private int totalVisibleBlocksCount;
    private int unmovedTextX;
    private ArrayList<LinkPath> urlPath = new ArrayList();
    private ArrayList<LinkPath> urlPathCache = new ArrayList();
    private ArrayList<LinkPath> urlPathSelection = new ArrayList();
    private boolean useSeekBarWaweform;
    private int viaNameWidth;
    private int viaWidth;
    private int videoButtonPressed;
    private int videoButtonX;
    private int videoButtonY;
    private StaticLayout videoInfoLayout;
    private RadialProgress2 videoRadialProgress;
    private StaticLayout viewsLayout;
    private int viewsTextWidth;
    private float voteCurrentCircleLength;
    private float voteCurrentProgressTime;
    private long voteLastUpdateTime;
    private float voteRadOffset;
    private boolean voteRisingCircleLength;
    private boolean wasLayout;
    private boolean wasSending;
    private int widthBeforeNewTimeLine;
    private int widthForButtons;

    /* renamed from: org.telegram.ui.Cells.ChatMessageCell$1 */
    class C23461 implements Runnable {
        C23461() {
        }

        public void run() {
            ChatMessageCell.this.checkLocationExpired();
            if (ChatMessageCell.this.locationExpired) {
                ChatMessageCell.this.invalidate();
                ChatMessageCell.this.scheduledInvalidate = false;
                return;
            }
            ChatMessageCell chatMessageCell = ChatMessageCell.this;
            chatMessageCell.invalidate(((int) chatMessageCell.rect.left) - 5, ((int) ChatMessageCell.this.rect.top) - 5, ((int) ChatMessageCell.this.rect.right) + 5, ((int) ChatMessageCell.this.rect.bottom) + 5);
            if (ChatMessageCell.this.scheduledInvalidate) {
                AndroidUtilities.runOnUIThread(ChatMessageCell.this.invalidateRunnable, 1000);
            }
        }
    }

    /* renamed from: org.telegram.ui.Cells.ChatMessageCell$BotButton */
    private class BotButton {
        private int angle;
        private KeyboardButton button;
        private int height;
        private long lastUpdateTime;
        private float progressAlpha;
        private StaticLayout title;
        private int width;
        /* renamed from: x */
        private int f575x;
        /* renamed from: y */
        private int f576y;

        private BotButton() {
        }

        /* synthetic */ BotButton(ChatMessageCell chatMessageCell, C23461 c23461) {
            this();
        }
    }

    /* renamed from: org.telegram.ui.Cells.ChatMessageCell$ChatMessageCellDelegate */
    public interface ChatMessageCellDelegate {

        /* renamed from: org.telegram.ui.Cells.ChatMessageCell$ChatMessageCellDelegate$-CC */
        public final /* synthetic */ class C2348-CC {
            public static boolean $default$canPerformActions(ChatMessageCellDelegate chatMessageCellDelegate) {
                return false;
            }

            public static void $default$didLongPress(ChatMessageCellDelegate chatMessageCellDelegate, ChatMessageCell chatMessageCell, float f, float f2) {
            }

            public static void $default$didPressBotButton(ChatMessageCellDelegate chatMessageCellDelegate, ChatMessageCell chatMessageCell, KeyboardButton keyboardButton) {
            }

            public static void $default$didPressCancelSendButton(ChatMessageCellDelegate chatMessageCellDelegate, ChatMessageCell chatMessageCell) {
            }

            public static void $default$didPressChannelAvatar(ChatMessageCellDelegate chatMessageCellDelegate, ChatMessageCell chatMessageCell, Chat chat, int i, float f, float f2) {
            }

            public static void $default$didPressHiddenForward(ChatMessageCellDelegate chatMessageCellDelegate, ChatMessageCell chatMessageCell) {
            }

            public static void $default$didPressImage(ChatMessageCellDelegate chatMessageCellDelegate, ChatMessageCell chatMessageCell, float f, float f2) {
            }

            public static void $default$didPressInstantButton(ChatMessageCellDelegate chatMessageCellDelegate, ChatMessageCell chatMessageCell, int i) {
            }

            public static void $default$didPressOther(ChatMessageCellDelegate chatMessageCellDelegate, ChatMessageCell chatMessageCell, float f, float f2) {
            }

            public static void $default$didPressReplyMessage(ChatMessageCellDelegate chatMessageCellDelegate, ChatMessageCell chatMessageCell, int i) {
            }

            public static void $default$didPressShare(ChatMessageCellDelegate chatMessageCellDelegate, ChatMessageCell chatMessageCell) {
            }

            public static void $default$didPressUrl(ChatMessageCellDelegate chatMessageCellDelegate, MessageObject messageObject, CharacterStyle characterStyle, boolean z) {
            }

            public static void $default$didPressUserAvatar(ChatMessageCellDelegate chatMessageCellDelegate, ChatMessageCell chatMessageCell, User user, float f, float f2) {
            }

            public static void $default$didPressViaBot(ChatMessageCellDelegate chatMessageCellDelegate, ChatMessageCell chatMessageCell, String str) {
            }

            public static void $default$didPressVoteButton(ChatMessageCellDelegate chatMessageCellDelegate, ChatMessageCell chatMessageCell, TL_pollAnswer tL_pollAnswer) {
            }

            public static void $default$didStartVideoStream(ChatMessageCellDelegate chatMessageCellDelegate, MessageObject messageObject) {
            }

            public static boolean $default$isChatAdminCell(ChatMessageCellDelegate chatMessageCellDelegate, int i) {
                return false;
            }

            public static void $default$needOpenWebView(ChatMessageCellDelegate chatMessageCellDelegate, String str, String str2, String str3, String str4, int i, int i2) {
            }

            public static boolean $default$needPlayMessage(ChatMessageCellDelegate chatMessageCellDelegate, MessageObject messageObject) {
                return false;
            }

            public static void $default$videoTimerReached(ChatMessageCellDelegate chatMessageCellDelegate) {
            }
        }

        boolean canPerformActions();

        void didLongPress(ChatMessageCell chatMessageCell, float f, float f2);

        void didPressBotButton(ChatMessageCell chatMessageCell, KeyboardButton keyboardButton);

        void didPressCancelSendButton(ChatMessageCell chatMessageCell);

        void didPressChannelAvatar(ChatMessageCell chatMessageCell, Chat chat, int i, float f, float f2);

        void didPressHiddenForward(ChatMessageCell chatMessageCell);

        void didPressImage(ChatMessageCell chatMessageCell, float f, float f2);

        void didPressInstantButton(ChatMessageCell chatMessageCell, int i);

        void didPressOther(ChatMessageCell chatMessageCell, float f, float f2);

        void didPressReplyMessage(ChatMessageCell chatMessageCell, int i);

        void didPressShare(ChatMessageCell chatMessageCell);

        void didPressUrl(MessageObject messageObject, CharacterStyle characterStyle, boolean z);

        void didPressUserAvatar(ChatMessageCell chatMessageCell, User user, float f, float f2);

        void didPressViaBot(ChatMessageCell chatMessageCell, String str);

        void didPressVoteButton(ChatMessageCell chatMessageCell, TL_pollAnswer tL_pollAnswer);

        void didStartVideoStream(MessageObject messageObject);

        boolean isChatAdminCell(int i);

        void needOpenWebView(String str, String str2, String str3, String str4, int i, int i2);

        boolean needPlayMessage(MessageObject messageObject);

        void videoTimerReached();
    }

    /* renamed from: org.telegram.ui.Cells.ChatMessageCell$MessageAccessibilityNodeProvider */
    private class MessageAccessibilityNodeProvider extends AccessibilityNodeProvider {
        private final int BOT_BUTTONS_START;
        private final int INSTANT_VIEW;
        private final int LINK_IDS_START;
        private final int POLL_BUTTONS_START;
        private final int REPLY;
        private final int SHARE;
        private Path linkPath;
        private Rect rect;
        private RectF rectF;

        private MessageAccessibilityNodeProvider() {
            this.LINK_IDS_START = 2000;
            this.BOT_BUTTONS_START = 1000;
            this.POLL_BUTTONS_START = 500;
            this.INSTANT_VIEW = 499;
            this.SHARE = 498;
            this.REPLY = 497;
            this.linkPath = new Path();
            this.rectF = new RectF();
            this.rect = new Rect();
        }

        /* synthetic */ MessageAccessibilityNodeProvider(ChatMessageCell chatMessageCell, C23461 c23461) {
            this();
        }

        public AccessibilityNodeInfo createAccessibilityNodeInfo(int i) {
            int[] iArr = new int[]{0, 0};
            ChatMessageCell.this.getLocationOnScreen(iArr);
            CharSequence charSequence = null;
            String str = ", ";
            int i2 = 0;
            String stringBuilder;
            int i3;
            if (i == -1) {
                int access$3100;
                AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain(ChatMessageCell.this);
                ChatMessageCell.this.onInitializeAccessibilityNodeInfo(obtain);
                StringBuilder stringBuilder2 = new StringBuilder();
                ChatMessageCell chatMessageCell = ChatMessageCell.this;
                if (!(!chatMessageCell.isChat || chatMessageCell.currentUser == null || ChatMessageCell.this.currentMessageObject.isOut())) {
                    stringBuilder2.append(UserObject.getUserName(ChatMessageCell.this.currentUser));
                    stringBuilder2.append(10);
                }
                if (!TextUtils.isEmpty(ChatMessageCell.this.currentMessageObject.messageText)) {
                    stringBuilder2.append(ChatMessageCell.this.currentMessageObject.messageText);
                }
                String str2 = "\n";
                if (ChatMessageCell.this.currentMessageObject.isMusic()) {
                    stringBuilder2.append(str2);
                    stringBuilder2.append(LocaleController.formatString("AccDescrMusicInfo", C1067R.string.AccDescrMusicInfo, ChatMessageCell.this.currentMessageObject.getMusicAuthor(), ChatMessageCell.this.currentMessageObject.getMusicTitle()));
                } else if (ChatMessageCell.this.currentMessageObject.isVoice() || ChatMessageCell.this.currentMessageObject.isRoundVideo()) {
                    stringBuilder2.append(str);
                    stringBuilder2.append(LocaleController.formatCallDuration(ChatMessageCell.this.currentMessageObject.getDuration()));
                    if (ChatMessageCell.this.currentMessageObject.isContentUnread()) {
                        stringBuilder2.append(str);
                        stringBuilder2.append(LocaleController.getString("AccDescrMsgNotPlayed", C1067R.string.AccDescrMsgNotPlayed));
                    }
                }
                if (ChatMessageCell.this.lastPoll != null) {
                    stringBuilder2.append(str);
                    stringBuilder2.append(ChatMessageCell.this.lastPoll.question);
                    stringBuilder2.append(str);
                    stringBuilder2.append(LocaleController.getString("AnonymousPoll", C1067R.string.AnonymousPoll));
                }
                if (!(ChatMessageCell.this.currentMessageObject.messageOwner.media == null || TextUtils.isEmpty(ChatMessageCell.this.currentMessageObject.caption))) {
                    stringBuilder2.append(str2);
                    stringBuilder2.append(ChatMessageCell.this.currentMessageObject.caption);
                }
                stringBuilder2.append(str2);
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(LocaleController.getString("TodayAt", C1067R.string.TodayAt));
                stringBuilder3.append(" ");
                stringBuilder3.append(ChatMessageCell.this.currentTimeString);
                stringBuilder = stringBuilder3.toString();
                if (ChatMessageCell.this.currentMessageObject.isOut()) {
                    int i4;
                    stringBuilder2.append(LocaleController.formatString("AccDescrSentDate", C1067R.string.AccDescrSentDate, stringBuilder));
                    stringBuilder2.append(str);
                    if (ChatMessageCell.this.currentMessageObject.isUnread()) {
                        i4 = C1067R.string.AccDescrMsgUnread;
                        stringBuilder = "AccDescrMsgUnread";
                    } else {
                        i4 = C1067R.string.AccDescrMsgRead;
                        stringBuilder = "AccDescrMsgRead";
                    }
                    stringBuilder2.append(LocaleController.getString(stringBuilder, i4));
                } else {
                    stringBuilder2.append(LocaleController.formatString("AccDescrReceivedDate", C1067R.string.AccDescrReceivedDate, stringBuilder));
                }
                obtain.setContentDescription(stringBuilder2.toString());
                obtain.setEnabled(true);
                if (VERSION.SDK_INT >= 19) {
                    CollectionItemInfo collectionItemInfo = obtain.getCollectionItemInfo();
                    if (collectionItemInfo != null) {
                        obtain.setCollectionItemInfo(CollectionItemInfo.obtain(collectionItemInfo.getRowIndex(), 1, 0, 1, false));
                    }
                }
                if (VERSION.SDK_INT >= 21) {
                    obtain.addAction(new AccessibilityAction(C1067R.C1066id.acc_action_msg_options, LocaleController.getString("AccActionMessageOptions", C1067R.string.AccActionMessageOptions)));
                    access$3100 = ChatMessageCell.this.getIconForCurrentState();
                    if (access$3100 == 0) {
                        charSequence = LocaleController.getString("AccActionPlay", C1067R.string.AccActionPlay);
                    } else if (access$3100 == 1) {
                        charSequence = LocaleController.getString("AccActionPause", C1067R.string.AccActionPause);
                    } else if (access$3100 == 2) {
                        charSequence = LocaleController.getString("AccActionDownload", C1067R.string.AccActionDownload);
                    } else if (access$3100 == 3) {
                        charSequence = LocaleController.getString("AccActionCancelDownload", C1067R.string.AccActionCancelDownload);
                    } else if (access$3100 == 5) {
                        charSequence = LocaleController.getString("AccActionOpenFile", C1067R.string.AccActionOpenFile);
                    } else if (ChatMessageCell.this.currentMessageObject.type == 16) {
                        charSequence = LocaleController.getString("CallAgain", C1067R.string.CallAgain);
                    }
                    obtain.addAction(new AccessibilityAction(16, charSequence));
                    obtain.addAction(new AccessibilityAction(32, LocaleController.getString("AccActionEnterSelectionMode", C1067R.string.AccActionEnterSelectionMode)));
                    if (ChatMessageCell.this.getMiniIconForCurrentState() == 2) {
                        obtain.addAction(new AccessibilityAction(C1067R.C1066id.acc_action_small_button, LocaleController.getString("AccActionDownload", C1067R.string.AccActionDownload)));
                    }
                } else {
                    obtain.addAction(16);
                    obtain.addAction(32);
                }
                if (ChatMessageCell.this.currentMessageObject.messageText instanceof Spannable) {
                    Spannable spannable = (Spannable) ChatMessageCell.this.currentMessageObject.messageText;
                    i3 = 0;
                    for (CharacterStyle characterStyle : (CharacterStyle[]) spannable.getSpans(0, spannable.length(), ClickableSpan.class)) {
                        obtain.addChild(ChatMessageCell.this, i3 + 2000);
                        i3++;
                    }
                }
                Iterator it = ChatMessageCell.this.botButtons.iterator();
                access$3100 = 0;
                while (it.hasNext()) {
                    BotButton botButton = (BotButton) it.next();
                    obtain.addChild(ChatMessageCell.this, access$3100 + 1000);
                    access$3100++;
                }
                it = ChatMessageCell.this.pollButtons.iterator();
                while (it.hasNext()) {
                    PollButton pollButton = (PollButton) it.next();
                    obtain.addChild(ChatMessageCell.this, i2 + 500);
                    i2++;
                }
                if (ChatMessageCell.this.drawInstantView) {
                    obtain.addChild(ChatMessageCell.this, 499);
                }
                if (ChatMessageCell.this.drawShareButton) {
                    obtain.addChild(ChatMessageCell.this, 498);
                }
                if (ChatMessageCell.this.replyNameLayout != null) {
                    obtain.addChild(ChatMessageCell.this, 497);
                }
                if (ChatMessageCell.this.drawSelectionBackground || ChatMessageCell.this.getBackground() != null) {
                    obtain.setSelected(true);
                }
                return obtain;
            }
            AccessibilityNodeInfo obtain2 = AccessibilityNodeInfo.obtain();
            obtain2.setSource(ChatMessageCell.this, i);
            obtain2.setParent(ChatMessageCell.this);
            obtain2.setPackageName(ChatMessageCell.this.getContext().getPackageName());
            int spanEnd;
            if (i >= 2000) {
                Spannable spannable2 = (Spannable) ChatMessageCell.this.currentMessageObject.messageText;
                ClickableSpan linkById = getLinkById(i);
                if (linkById == null) {
                    return null;
                }
                i3 = spannable2.getSpanStart(linkById);
                spanEnd = spannable2.getSpanEnd(linkById);
                obtain2.setText(spannable2.subSequence(i3, spanEnd).toString());
                Iterator it2 = ChatMessageCell.this.currentMessageObject.textLayoutBlocks.iterator();
                while (it2.hasNext()) {
                    TextLayoutBlock textLayoutBlock = (TextLayoutBlock) it2.next();
                    int length = textLayoutBlock.textLayout.getText().length();
                    int i5 = textLayoutBlock.charactersOffset;
                    if (i5 <= i3 && length + i5 >= spanEnd) {
                        textLayoutBlock.textLayout.getSelectionPath(i3 - i5, spanEnd - i5, this.linkPath);
                        this.linkPath.computeBounds(this.rectF, true);
                        Rect rect = this.rect;
                        RectF rectF = this.rectF;
                        rect.set((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                        this.rect.offset(0, (int) textLayoutBlock.textYOffset);
                        this.rect.offset(ChatMessageCell.this.textX, ChatMessageCell.this.textY);
                        obtain2.setBoundsInParent(this.rect);
                        if (ChatMessageCell.this.accessibilityVirtualViewBounds.get(i) == null) {
                            ChatMessageCell.this.accessibilityVirtualViewBounds.put(i, new Rect(this.rect));
                        }
                        this.rect.offset(iArr[0], iArr[1]);
                        obtain2.setBoundsInScreen(this.rect);
                        obtain2.setClassName("android.widget.TextView");
                        obtain2.setEnabled(true);
                        obtain2.setClickable(true);
                        obtain2.setLongClickable(true);
                        obtain2.addAction(16);
                        obtain2.addAction(32);
                    }
                }
                obtain2.setClassName("android.widget.TextView");
                obtain2.setEnabled(true);
                obtain2.setClickable(true);
                obtain2.setLongClickable(true);
                obtain2.addAction(16);
                obtain2.addAction(32);
            } else {
                stringBuilder = "android.widget.Button";
                int i6;
                if (i >= 1000) {
                    i6 = i + NotificationUtil.IMPORTANCE_UNSPECIFIED;
                    if (i6 >= ChatMessageCell.this.botButtons.size()) {
                        return null;
                    }
                    BotButton botButton2 = (BotButton) ChatMessageCell.this.botButtons.get(i6);
                    obtain2.setText(botButton2.title.getText());
                    obtain2.setClassName(stringBuilder);
                    obtain2.setEnabled(true);
                    obtain2.setClickable(true);
                    obtain2.addAction(16);
                    this.rect.set(botButton2.f575x, botButton2.f576y, botButton2.f575x + botButton2.width, botButton2.f576y + botButton2.height);
                    if (ChatMessageCell.this.currentMessageObject.isOutOwner()) {
                        i6 = (ChatMessageCell.this.getMeasuredWidth() - ChatMessageCell.this.widthForButtons) - AndroidUtilities.m26dp(10.0f);
                    } else {
                        i6 = ChatMessageCell.this.backgroundDrawableLeft + AndroidUtilities.m26dp(ChatMessageCell.this.mediaBackground ? 1.0f : 7.0f);
                    }
                    this.rect.offset(i6, ChatMessageCell.this.layoutHeight);
                    obtain2.setBoundsInParent(this.rect);
                    if (ChatMessageCell.this.accessibilityVirtualViewBounds.get(i) == null) {
                        ChatMessageCell.this.accessibilityVirtualViewBounds.put(i, new Rect(this.rect));
                    }
                    this.rect.offset(iArr[0], iArr[1]);
                    obtain2.setBoundsInScreen(this.rect);
                } else if (i >= 500) {
                    i6 = i - 500;
                    if (i6 >= ChatMessageCell.this.pollButtons.size()) {
                        return null;
                    }
                    PollButton pollButton2 = (PollButton) ChatMessageCell.this.pollButtons.get(i6);
                    obtain2.setText(pollButton2.title.getText());
                    if (ChatMessageCell.this.pollVoted) {
                        StringBuilder stringBuilder4 = new StringBuilder();
                        stringBuilder4.append(obtain2.getText());
                        stringBuilder4.append(str);
                        stringBuilder4.append(pollButton2.percent);
                        stringBuilder4.append("%");
                        obtain2.setText(stringBuilder4.toString());
                    } else {
                        obtain2.setClassName(stringBuilder);
                    }
                    obtain2.setEnabled(true);
                    obtain2.addAction(16);
                    this.rect.set(pollButton2.f577x, pollButton2.f578y, pollButton2.f577x + (ChatMessageCell.this.backgroundWidth - AndroidUtilities.m26dp(76.0f)), pollButton2.f578y + pollButton2.height);
                    obtain2.setBoundsInParent(this.rect);
                    if (ChatMessageCell.this.accessibilityVirtualViewBounds.get(i) == null) {
                        ChatMessageCell.this.accessibilityVirtualViewBounds.put(i, new Rect(this.rect));
                    }
                    this.rect.offset(iArr[0], iArr[1]);
                    obtain2.setBoundsInScreen(this.rect);
                    obtain2.setClickable(true);
                } else if (i == 499) {
                    obtain2.setClassName(stringBuilder);
                    obtain2.setEnabled(true);
                    if (ChatMessageCell.this.instantViewLayout != null) {
                        obtain2.setText(ChatMessageCell.this.instantViewLayout.getText());
                    }
                    obtain2.addAction(16);
                    i6 = ChatMessageCell.this.photoImage.getImageX();
                    spanEnd = ChatMessageCell.this.getMeasuredHeight() - AndroidUtilities.m26dp(64.0f);
                    if (ChatMessageCell.this.currentMessageObject.isOutOwner()) {
                        i3 = (ChatMessageCell.this.getMeasuredWidth() - ChatMessageCell.this.widthForButtons) - AndroidUtilities.m26dp(10.0f);
                    } else {
                        i3 = AndroidUtilities.m26dp(ChatMessageCell.this.mediaBackground ? 1.0f : 7.0f) + ChatMessageCell.this.backgroundDrawableLeft;
                    }
                    this.rect.set(i6 + i3, spanEnd, (i6 + ChatMessageCell.this.instantWidth) + i3, AndroidUtilities.m26dp(38.0f) + spanEnd);
                    obtain2.setBoundsInParent(this.rect);
                    if (ChatMessageCell.this.accessibilityVirtualViewBounds.get(i) == null || !((Rect) ChatMessageCell.this.accessibilityVirtualViewBounds.get(i)).equals(this.rect)) {
                        ChatMessageCell.this.accessibilityVirtualViewBounds.put(i, new Rect(this.rect));
                    }
                    this.rect.offset(iArr[0], iArr[1]);
                    obtain2.setBoundsInScreen(this.rect);
                    obtain2.setClickable(true);
                } else if (i == 498) {
                    obtain2.setClassName("android.widget.ImageButton");
                    obtain2.setEnabled(true);
                    ChatMessageCell chatMessageCell2 = ChatMessageCell.this;
                    if (chatMessageCell2.isOpenChatByShare(chatMessageCell2.currentMessageObject)) {
                        obtain2.setContentDescription(LocaleController.getString("AccDescrOpenChat", C1067R.string.AccDescrOpenChat));
                    } else {
                        obtain2.setContentDescription(LocaleController.getString("ShareFile", C1067R.string.ShareFile));
                    }
                    obtain2.addAction(16);
                    this.rect.set(ChatMessageCell.this.shareStartX, ChatMessageCell.this.shareStartY, ChatMessageCell.this.shareStartX + AndroidUtilities.m26dp(40.0f), ChatMessageCell.this.shareStartY + AndroidUtilities.m26dp(32.0f));
                    obtain2.setBoundsInParent(this.rect);
                    if (ChatMessageCell.this.accessibilityVirtualViewBounds.get(i) == null || !((Rect) ChatMessageCell.this.accessibilityVirtualViewBounds.get(i)).equals(this.rect)) {
                        ChatMessageCell.this.accessibilityVirtualViewBounds.put(i, new Rect(this.rect));
                    }
                    this.rect.offset(iArr[0], iArr[1]);
                    obtain2.setBoundsInScreen(this.rect);
                    obtain2.setClickable(true);
                } else if (i == 497) {
                    obtain2.setEnabled(true);
                    StringBuilder stringBuilder5 = new StringBuilder();
                    stringBuilder5.append(LocaleController.getString("Reply", C1067R.string.Reply));
                    stringBuilder5.append(str);
                    if (ChatMessageCell.this.replyNameLayout != null) {
                        stringBuilder5.append(ChatMessageCell.this.replyNameLayout.getText());
                        stringBuilder5.append(str);
                    }
                    if (ChatMessageCell.this.replyTextLayout != null) {
                        stringBuilder5.append(ChatMessageCell.this.replyTextLayout.getText());
                    }
                    obtain2.setContentDescription(stringBuilder5.toString());
                    obtain2.addAction(16);
                    this.rect.set(ChatMessageCell.this.replyStartX, ChatMessageCell.this.replyStartY, ChatMessageCell.this.replyStartX + Math.max(ChatMessageCell.this.replyNameWidth, ChatMessageCell.this.replyTextWidth), ChatMessageCell.this.replyStartY + AndroidUtilities.m26dp(35.0f));
                    obtain2.setBoundsInParent(this.rect);
                    if (ChatMessageCell.this.accessibilityVirtualViewBounds.get(i) == null || !((Rect) ChatMessageCell.this.accessibilityVirtualViewBounds.get(i)).equals(this.rect)) {
                        ChatMessageCell.this.accessibilityVirtualViewBounds.put(i, new Rect(this.rect));
                    }
                    this.rect.offset(iArr[0], iArr[1]);
                    obtain2.setBoundsInScreen(this.rect);
                    obtain2.setClickable(true);
                }
            }
            obtain2.setFocusable(true);
            obtain2.setVisibleToUser(true);
            return obtain2;
        }

        public boolean performAction(int i, int i2, Bundle bundle) {
            ClickableSpan linkById;
            if (i == -1) {
                ChatMessageCell.this.performAccessibilityAction(i2, bundle);
            } else if (i2 == 64) {
                ChatMessageCell.this.sendAccessibilityEventForVirtualView(i, 32768);
            } else if (i2 == 16) {
                ChatMessageCellDelegate access$6000;
                ChatMessageCell chatMessageCell;
                if (i >= 2000) {
                    linkById = getLinkById(i);
                    if (linkById != null) {
                        ChatMessageCell.this.delegate.didPressUrl(ChatMessageCell.this.currentMessageObject, linkById, false);
                        ChatMessageCell.this.sendAccessibilityEventForVirtualView(i, 1);
                    }
                } else if (i >= 1000) {
                    i2 = i + NotificationUtil.IMPORTANCE_UNSPECIFIED;
                    if (i2 >= ChatMessageCell.this.botButtons.size()) {
                        return false;
                    }
                    BotButton botButton = (BotButton) ChatMessageCell.this.botButtons.get(i2);
                    if (ChatMessageCell.this.delegate != null) {
                        ChatMessageCell.this.delegate.didPressBotButton(ChatMessageCell.this, botButton.button);
                    }
                    ChatMessageCell.this.sendAccessibilityEventForVirtualView(i, 1);
                } else if (i >= 500) {
                    i2 = i - 500;
                    if (i2 >= ChatMessageCell.this.pollButtons.size()) {
                        return false;
                    }
                    PollButton pollButton = (PollButton) ChatMessageCell.this.pollButtons.get(i2);
                    if (ChatMessageCell.this.delegate != null) {
                        ChatMessageCell.this.delegate.didPressVoteButton(ChatMessageCell.this, pollButton.answer);
                    }
                    ChatMessageCell.this.sendAccessibilityEventForVirtualView(i, 1);
                } else if (i == 499) {
                    if (ChatMessageCell.this.delegate != null) {
                        access$6000 = ChatMessageCell.this.delegate;
                        chatMessageCell = ChatMessageCell.this;
                        access$6000.didPressInstantButton(chatMessageCell, chatMessageCell.drawInstantViewType);
                    }
                } else if (i == 498) {
                    if (ChatMessageCell.this.delegate != null) {
                        ChatMessageCell.this.delegate.didPressShare(ChatMessageCell.this);
                    }
                } else if (i == 497 && ChatMessageCell.this.delegate != null) {
                    access$6000 = ChatMessageCell.this.delegate;
                    chatMessageCell = ChatMessageCell.this;
                    access$6000.didPressReplyMessage(chatMessageCell, chatMessageCell.currentMessageObject.messageOwner.reply_to_msg_id);
                }
            } else if (i2 == 32) {
                linkById = getLinkById(i);
                if (linkById != null) {
                    ChatMessageCell.this.delegate.didPressUrl(ChatMessageCell.this.currentMessageObject, linkById, true);
                    ChatMessageCell.this.sendAccessibilityEventForVirtualView(i, 2);
                }
            }
            return true;
        }

        private ClickableSpan getLinkById(int i) {
            i -= 2000;
            if (!(ChatMessageCell.this.currentMessageObject.messageText instanceof Spannable)) {
                return null;
            }
            Spannable spannable = (Spannable) ChatMessageCell.this.currentMessageObject.messageText;
            ClickableSpan[] clickableSpanArr = (ClickableSpan[]) spannable.getSpans(0, spannable.length(), ClickableSpan.class);
            if (clickableSpanArr.length <= i) {
                return null;
            }
            return clickableSpanArr[i];
        }
    }

    /* renamed from: org.telegram.ui.Cells.ChatMessageCell$PollButton */
    private class PollButton {
        private TL_pollAnswer answer;
        private float decimal;
        private int height;
        private int percent;
        private float percentProgress;
        private int prevPercent;
        private float prevPercentProgress;
        private StaticLayout title;
        /* renamed from: x */
        private int f577x;
        /* renamed from: y */
        private int f578y;

        private PollButton() {
        }

        /* synthetic */ PollButton(ChatMessageCell chatMessageCell, C23461 c23461) {
            this();
        }
    }

    private boolean intersect(float f, float f2, float f3, float f4) {
        boolean z = true;
        if (f <= f3) {
            if (f2 < f3) {
                z = false;
            }
            return z;
        }
        if (f > f4) {
            z = false;
        }
        return z;
    }

    public ChatMessageCell(Context context) {
        super(context);
        this.avatarImage.setRoundRadius(AndroidUtilities.m26dp(21.0f));
        this.avatarDrawable = new AvatarDrawable();
        this.replyImageReceiver = new ImageReceiver(this);
        this.locationImageReceiver = new ImageReceiver(this);
        this.locationImageReceiver.setRoundRadius(AndroidUtilities.m26dp(26.1f));
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        this.contactAvatarDrawable = new AvatarDrawable();
        this.photoImage = new ImageReceiver(this);
        this.photoImage.setDelegate(this);
        this.radialProgress = new RadialProgress2(this);
        this.videoRadialProgress = new RadialProgress2(this);
        this.videoRadialProgress.setDrawBackground(false);
        this.videoRadialProgress.setCircleRadius(AndroidUtilities.m26dp(15.0f));
        this.seekBar = new SeekBar(context);
        this.seekBar.setDelegate(this);
        this.seekBarWaveform = new SeekBarWaveform(context);
        this.seekBarWaveform.setDelegate(this);
        this.seekBarWaveform.setParentView(this);
        this.roundVideoPlayingDrawable = new RoundVideoPlayingDrawable(this);
    }

    private void resetPressedLink(int i) {
        if (this.pressedLink == null) {
            return;
        }
        if (this.pressedLinkType == i || i == -1) {
            resetUrlPaths(false);
            this.pressedLink = null;
            this.pressedLinkType = -1;
            invalidate();
        }
    }

    private void resetUrlPaths(boolean z) {
        if (z) {
            if (!this.urlPathSelection.isEmpty()) {
                this.urlPathCache.addAll(this.urlPathSelection);
                this.urlPathSelection.clear();
            }
        } else if (!this.urlPath.isEmpty()) {
            this.urlPathCache.addAll(this.urlPath);
            this.urlPath.clear();
        }
    }

    private LinkPath obtainNewUrlPath(boolean z) {
        LinkPath linkPath;
        if (this.urlPathCache.isEmpty()) {
            linkPath = new LinkPath();
        } else {
            linkPath = (LinkPath) this.urlPathCache.get(0);
            this.urlPathCache.remove(0);
        }
        linkPath.reset();
        if (z) {
            this.urlPathSelection.add(linkPath);
        } else {
            this.urlPath.add(linkPath);
        }
        return linkPath;
    }

    /* JADX WARNING: Removed duplicated region for block: B:56:0x00e9 A:{Catch:{ Exception -> 0x01ef }} */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00d6 A:{Catch:{ Exception -> 0x01ef }} */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00e9 A:{Catch:{ Exception -> 0x01ef }} */
    private boolean checkTextBlockMotionEvent(android.view.MotionEvent r15) {
        /*
        r14 = this;
        r0 = r14.currentMessageObject;
        r1 = r0.type;
        r2 = 0;
        if (r1 != 0) goto L_0x01f7;
    L_0x0007:
        r0 = r0.textLayoutBlocks;
        if (r0 == 0) goto L_0x01f7;
    L_0x000b:
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x01f7;
    L_0x0011:
        r0 = r14.currentMessageObject;
        r0 = r0.messageText;
        r0 = r0 instanceof android.text.Spannable;
        if (r0 != 0) goto L_0x001b;
    L_0x0019:
        goto L_0x01f7;
    L_0x001b:
        r0 = r15.getAction();
        r1 = 1;
        if (r0 == 0) goto L_0x002c;
    L_0x0022:
        r0 = r15.getAction();
        if (r0 != r1) goto L_0x01f7;
    L_0x0028:
        r0 = r14.pressedLinkType;
        if (r0 != r1) goto L_0x01f7;
    L_0x002c:
        r0 = r15.getX();
        r0 = (int) r0;
        r3 = r15.getY();
        r3 = (int) r3;
        r4 = r14.textX;
        if (r0 < r4) goto L_0x01f4;
    L_0x003a:
        r5 = r14.textY;
        if (r3 < r5) goto L_0x01f4;
    L_0x003e:
        r6 = r14.currentMessageObject;
        r7 = r6.textWidth;
        r4 = r4 + r7;
        if (r0 > r4) goto L_0x01f4;
    L_0x0045:
        r4 = r6.textHeight;
        r4 = r4 + r5;
        if (r3 > r4) goto L_0x01f4;
    L_0x004a:
        r3 = r3 - r5;
        r4 = 0;
        r5 = 0;
    L_0x004d:
        r6 = r14.currentMessageObject;
        r6 = r6.textLayoutBlocks;
        r6 = r6.size();
        if (r4 >= r6) goto L_0x006f;
    L_0x0057:
        r6 = r14.currentMessageObject;
        r6 = r6.textLayoutBlocks;
        r6 = r6.get(r4);
        r6 = (org.telegram.messenger.MessageObject.TextLayoutBlock) r6;
        r6 = r6.textYOffset;
        r7 = (float) r3;
        r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r6 <= 0) goto L_0x0069;
    L_0x0068:
        goto L_0x006f;
    L_0x0069:
        r5 = r4 + 1;
        r13 = r5;
        r5 = r4;
        r4 = r13;
        goto L_0x004d;
    L_0x006f:
        r4 = r14.currentMessageObject;	 Catch:{ Exception -> 0x01ef }
        r4 = r4.textLayoutBlocks;	 Catch:{ Exception -> 0x01ef }
        r4 = r4.get(r5);	 Catch:{ Exception -> 0x01ef }
        r4 = (org.telegram.messenger.MessageObject.TextLayoutBlock) r4;	 Catch:{ Exception -> 0x01ef }
        r0 = (float) r0;	 Catch:{ Exception -> 0x01ef }
        r6 = r14.textX;	 Catch:{ Exception -> 0x01ef }
        r6 = (float) r6;	 Catch:{ Exception -> 0x01ef }
        r7 = r4.isRtl();	 Catch:{ Exception -> 0x01ef }
        r8 = 0;
        if (r7 == 0) goto L_0x0089;
    L_0x0084:
        r7 = r14.currentMessageObject;	 Catch:{ Exception -> 0x01ef }
        r7 = r7.textXOffset;	 Catch:{ Exception -> 0x01ef }
        goto L_0x008a;
    L_0x0089:
        r7 = 0;
    L_0x008a:
        r6 = r6 - r7;
        r0 = r0 - r6;
        r0 = (int) r0;	 Catch:{ Exception -> 0x01ef }
        r3 = (float) r3;	 Catch:{ Exception -> 0x01ef }
        r6 = r4.textYOffset;	 Catch:{ Exception -> 0x01ef }
        r3 = r3 - r6;
        r3 = (int) r3;	 Catch:{ Exception -> 0x01ef }
        r6 = r4.textLayout;	 Catch:{ Exception -> 0x01ef }
        r3 = r6.getLineForVertical(r3);	 Catch:{ Exception -> 0x01ef }
        r6 = r4.textLayout;	 Catch:{ Exception -> 0x01ef }
        r0 = (float) r0;	 Catch:{ Exception -> 0x01ef }
        r6 = r6.getOffsetForHorizontal(r3, r0);	 Catch:{ Exception -> 0x01ef }
        r7 = r4.textLayout;	 Catch:{ Exception -> 0x01ef }
        r7 = r7.getLineLeft(r3);	 Catch:{ Exception -> 0x01ef }
        r9 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1));
        if (r9 > 0) goto L_0x01f7;
    L_0x00a9:
        r9 = r4.textLayout;	 Catch:{ Exception -> 0x01ef }
        r3 = r9.getLineWidth(r3);	 Catch:{ Exception -> 0x01ef }
        r7 = r7 + r3;
        r0 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1));
        if (r0 < 0) goto L_0x01f7;
    L_0x00b4:
        r0 = r14.currentMessageObject;	 Catch:{ Exception -> 0x01ef }
        r0 = r0.messageText;	 Catch:{ Exception -> 0x01ef }
        r0 = (android.text.Spannable) r0;	 Catch:{ Exception -> 0x01ef }
        r3 = android.text.style.ClickableSpan.class;
        r3 = r0.getSpans(r6, r6, r3);	 Catch:{ Exception -> 0x01ef }
        r3 = (android.text.style.CharacterStyle[]) r3;	 Catch:{ Exception -> 0x01ef }
        if (r3 == 0) goto L_0x00ca;
    L_0x00c4:
        r7 = r3.length;	 Catch:{ Exception -> 0x01ef }
        if (r7 != 0) goto L_0x00c8;
    L_0x00c7:
        goto L_0x00ca;
    L_0x00c8:
        r6 = 0;
        goto L_0x00d3;
    L_0x00ca:
        r3 = org.telegram.p004ui.Components.URLSpanMono.class;
        r3 = r0.getSpans(r6, r6, r3);	 Catch:{ Exception -> 0x01ef }
        r3 = (android.text.style.CharacterStyle[]) r3;	 Catch:{ Exception -> 0x01ef }
        r6 = 1;
    L_0x00d3:
        r7 = r3.length;	 Catch:{ Exception -> 0x01ef }
        if (r7 == 0) goto L_0x00e6;
    L_0x00d6:
        r7 = r3.length;	 Catch:{ Exception -> 0x01ef }
        if (r7 == 0) goto L_0x00e4;
    L_0x00d9:
        r7 = r3[r2];	 Catch:{ Exception -> 0x01ef }
        r7 = r7 instanceof org.telegram.p004ui.Components.URLSpanBotCommand;	 Catch:{ Exception -> 0x01ef }
        if (r7 == 0) goto L_0x00e4;
    L_0x00df:
        r7 = org.telegram.p004ui.Components.URLSpanBotCommand.enabled;	 Catch:{ Exception -> 0x01ef }
        if (r7 != 0) goto L_0x00e4;
    L_0x00e3:
        goto L_0x00e6;
    L_0x00e4:
        r7 = 0;
        goto L_0x00e7;
    L_0x00e6:
        r7 = 1;
    L_0x00e7:
        if (r7 != 0) goto L_0x01f7;
    L_0x00e9:
        r15 = r15.getAction();	 Catch:{ Exception -> 0x01ef }
        if (r15 != 0) goto L_0x01dc;
    L_0x00ef:
        r15 = r3[r2];	 Catch:{ Exception -> 0x01ef }
        r14.pressedLink = r15;	 Catch:{ Exception -> 0x01ef }
        r14.linkBlockNum = r5;	 Catch:{ Exception -> 0x01ef }
        r14.pressedLinkType = r1;	 Catch:{ Exception -> 0x01ef }
        r14.resetUrlPaths(r2);	 Catch:{ Exception -> 0x01ef }
        r15 = r14.obtainNewUrlPath(r2);	 Catch:{ Exception -> 0x01d4 }
        r3 = r14.pressedLink;	 Catch:{ Exception -> 0x01d4 }
        r3 = r0.getSpanStart(r3);	 Catch:{ Exception -> 0x01d4 }
        r7 = r14.pressedLink;	 Catch:{ Exception -> 0x01d4 }
        r7 = r0.getSpanEnd(r7);	 Catch:{ Exception -> 0x01d4 }
        r9 = r4.textLayout;	 Catch:{ Exception -> 0x01d4 }
        r15.setCurrentLayout(r9, r3, r8);	 Catch:{ Exception -> 0x01d4 }
        r8 = r4.textLayout;	 Catch:{ Exception -> 0x01d4 }
        r8.getSelectionPath(r3, r7, r15);	 Catch:{ Exception -> 0x01d4 }
        r15 = r4.charactersEnd;	 Catch:{ Exception -> 0x01d4 }
        if (r7 < r15) goto L_0x0171;
    L_0x0118:
        r15 = r5 + 1;
    L_0x011a:
        r8 = r14.currentMessageObject;	 Catch:{ Exception -> 0x01d4 }
        r8 = r8.textLayoutBlocks;	 Catch:{ Exception -> 0x01d4 }
        r8 = r8.size();	 Catch:{ Exception -> 0x01d4 }
        if (r15 >= r8) goto L_0x0171;
    L_0x0124:
        r8 = r14.currentMessageObject;	 Catch:{ Exception -> 0x01d4 }
        r8 = r8.textLayoutBlocks;	 Catch:{ Exception -> 0x01d4 }
        r8 = r8.get(r15);	 Catch:{ Exception -> 0x01d4 }
        r8 = (org.telegram.messenger.MessageObject.TextLayoutBlock) r8;	 Catch:{ Exception -> 0x01d4 }
        if (r6 == 0) goto L_0x013d;
    L_0x0130:
        r9 = r8.charactersOffset;	 Catch:{ Exception -> 0x01d4 }
        r10 = r8.charactersOffset;	 Catch:{ Exception -> 0x01d4 }
        r11 = org.telegram.p004ui.Components.URLSpanMono.class;
        r9 = r0.getSpans(r9, r10, r11);	 Catch:{ Exception -> 0x01d4 }
        r9 = (android.text.style.CharacterStyle[]) r9;	 Catch:{ Exception -> 0x01d4 }
        goto L_0x0149;
    L_0x013d:
        r9 = r8.charactersOffset;	 Catch:{ Exception -> 0x01d4 }
        r10 = r8.charactersOffset;	 Catch:{ Exception -> 0x01d4 }
        r11 = android.text.style.ClickableSpan.class;
        r9 = r0.getSpans(r9, r10, r11);	 Catch:{ Exception -> 0x01d4 }
        r9 = (android.text.style.CharacterStyle[]) r9;	 Catch:{ Exception -> 0x01d4 }
    L_0x0149:
        if (r9 == 0) goto L_0x0171;
    L_0x014b:
        r10 = r9.length;	 Catch:{ Exception -> 0x01d4 }
        if (r10 == 0) goto L_0x0171;
    L_0x014e:
        r9 = r9[r2];	 Catch:{ Exception -> 0x01d4 }
        r10 = r14.pressedLink;	 Catch:{ Exception -> 0x01d4 }
        if (r9 == r10) goto L_0x0155;
    L_0x0154:
        goto L_0x0171;
    L_0x0155:
        r9 = r14.obtainNewUrlPath(r2);	 Catch:{ Exception -> 0x01d4 }
        r10 = r8.textLayout;	 Catch:{ Exception -> 0x01d4 }
        r11 = r8.textYOffset;	 Catch:{ Exception -> 0x01d4 }
        r12 = r4.textYOffset;	 Catch:{ Exception -> 0x01d4 }
        r11 = r11 - r12;
        r9.setCurrentLayout(r10, r2, r11);	 Catch:{ Exception -> 0x01d4 }
        r10 = r8.textLayout;	 Catch:{ Exception -> 0x01d4 }
        r10.getSelectionPath(r2, r7, r9);	 Catch:{ Exception -> 0x01d4 }
        r8 = r8.charactersEnd;	 Catch:{ Exception -> 0x01d4 }
        r8 = r8 - r1;
        if (r7 >= r8) goto L_0x016e;
    L_0x016d:
        goto L_0x0171;
    L_0x016e:
        r15 = r15 + 1;
        goto L_0x011a;
    L_0x0171:
        r15 = r4.charactersOffset;	 Catch:{ Exception -> 0x01d4 }
        if (r3 > r15) goto L_0x01d8;
    L_0x0175:
        r5 = r5 - r1;
        r15 = 0;
    L_0x0177:
        if (r5 < 0) goto L_0x01d8;
    L_0x0179:
        r3 = r14.currentMessageObject;	 Catch:{ Exception -> 0x01d4 }
        r3 = r3.textLayoutBlocks;	 Catch:{ Exception -> 0x01d4 }
        r3 = r3.get(r5);	 Catch:{ Exception -> 0x01d4 }
        r3 = (org.telegram.messenger.MessageObject.TextLayoutBlock) r3;	 Catch:{ Exception -> 0x01d4 }
        if (r6 == 0) goto L_0x0194;
    L_0x0185:
        r4 = r3.charactersEnd;	 Catch:{ Exception -> 0x01d4 }
        r4 = r4 - r1;
        r7 = r3.charactersEnd;	 Catch:{ Exception -> 0x01d4 }
        r7 = r7 - r1;
        r8 = org.telegram.p004ui.Components.URLSpanMono.class;
        r4 = r0.getSpans(r4, r7, r8);	 Catch:{ Exception -> 0x01d4 }
        r4 = (android.text.style.CharacterStyle[]) r4;	 Catch:{ Exception -> 0x01d4 }
        goto L_0x01a2;
    L_0x0194:
        r4 = r3.charactersEnd;	 Catch:{ Exception -> 0x01d4 }
        r4 = r4 - r1;
        r7 = r3.charactersEnd;	 Catch:{ Exception -> 0x01d4 }
        r7 = r7 - r1;
        r8 = android.text.style.ClickableSpan.class;
        r4 = r0.getSpans(r4, r7, r8);	 Catch:{ Exception -> 0x01d4 }
        r4 = (android.text.style.CharacterStyle[]) r4;	 Catch:{ Exception -> 0x01d4 }
    L_0x01a2:
        if (r4 == 0) goto L_0x01d8;
    L_0x01a4:
        r7 = r4.length;	 Catch:{ Exception -> 0x01d4 }
        if (r7 == 0) goto L_0x01d8;
    L_0x01a7:
        r4 = r4[r2];	 Catch:{ Exception -> 0x01d4 }
        r7 = r14.pressedLink;	 Catch:{ Exception -> 0x01d4 }
        if (r4 == r7) goto L_0x01ae;
    L_0x01ad:
        goto L_0x01d8;
    L_0x01ae:
        r4 = r14.obtainNewUrlPath(r2);	 Catch:{ Exception -> 0x01d4 }
        r7 = r14.pressedLink;	 Catch:{ Exception -> 0x01d4 }
        r7 = r0.getSpanStart(r7);	 Catch:{ Exception -> 0x01d4 }
        r8 = r3.height;	 Catch:{ Exception -> 0x01d4 }
        r15 = r15 - r8;
        r8 = r3.textLayout;	 Catch:{ Exception -> 0x01d4 }
        r9 = (float) r15;	 Catch:{ Exception -> 0x01d4 }
        r4.setCurrentLayout(r8, r7, r9);	 Catch:{ Exception -> 0x01d4 }
        r8 = r3.textLayout;	 Catch:{ Exception -> 0x01d4 }
        r9 = r14.pressedLink;	 Catch:{ Exception -> 0x01d4 }
        r9 = r0.getSpanEnd(r9);	 Catch:{ Exception -> 0x01d4 }
        r8.getSelectionPath(r7, r9, r4);	 Catch:{ Exception -> 0x01d4 }
        r3 = r3.charactersOffset;	 Catch:{ Exception -> 0x01d4 }
        if (r7 <= r3) goto L_0x01d1;
    L_0x01d0:
        goto L_0x01d8;
    L_0x01d1:
        r5 = r5 + -1;
        goto L_0x0177;
    L_0x01d4:
        r15 = move-exception;
        org.telegram.messenger.FileLog.m30e(r15);	 Catch:{ Exception -> 0x01ef }
    L_0x01d8:
        r14.invalidate();	 Catch:{ Exception -> 0x01ef }
        return r1;
    L_0x01dc:
        r15 = r3[r2];	 Catch:{ Exception -> 0x01ef }
        r0 = r14.pressedLink;	 Catch:{ Exception -> 0x01ef }
        if (r15 != r0) goto L_0x01f7;
    L_0x01e2:
        r15 = r14.delegate;	 Catch:{ Exception -> 0x01ef }
        r0 = r14.currentMessageObject;	 Catch:{ Exception -> 0x01ef }
        r3 = r14.pressedLink;	 Catch:{ Exception -> 0x01ef }
        r15.didPressUrl(r0, r3, r2);	 Catch:{ Exception -> 0x01ef }
        r14.resetPressedLink(r1);	 Catch:{ Exception -> 0x01ef }
        return r1;
    L_0x01ef:
        r15 = move-exception;
        org.telegram.messenger.FileLog.m30e(r15);
        goto L_0x01f7;
    L_0x01f4:
        r14.resetPressedLink(r1);
    L_0x01f7:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.checkTextBlockMotionEvent(android.view.MotionEvent):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:44:0x009c A:{Catch:{ Exception -> 0x00dc }} */
    private boolean checkCaptionMotionEvent(android.view.MotionEvent r8) {
        /*
        r7 = this;
        r0 = r7.currentCaption;
        r0 = r0 instanceof android.text.Spannable;
        r1 = 0;
        if (r0 == 0) goto L_0x00f5;
    L_0x0007:
        r0 = r7.captionLayout;
        if (r0 != 0) goto L_0x000d;
    L_0x000b:
        goto L_0x00f5;
    L_0x000d:
        r0 = r8.getAction();
        r2 = 1;
        if (r0 == 0) goto L_0x0022;
    L_0x0014:
        r0 = r7.linkPreviewPressed;
        if (r0 != 0) goto L_0x001c;
    L_0x0018:
        r0 = r7.pressedLink;
        if (r0 == 0) goto L_0x00f5;
    L_0x001c:
        r0 = r8.getAction();
        if (r0 != r2) goto L_0x00f5;
    L_0x0022:
        r0 = r8.getX();
        r0 = (int) r0;
        r3 = r8.getY();
        r3 = (int) r3;
        r4 = r7.captionX;
        r5 = 3;
        if (r0 < r4) goto L_0x00f2;
    L_0x0031:
        r6 = r7.captionWidth;
        r4 = r4 + r6;
        if (r0 > r4) goto L_0x00f2;
    L_0x0036:
        r4 = r7.captionY;
        if (r3 < r4) goto L_0x00f2;
    L_0x003a:
        r6 = r7.captionHeight;
        r4 = r4 + r6;
        if (r3 > r4) goto L_0x00f2;
    L_0x003f:
        r8 = r8.getAction();
        if (r8 != 0) goto L_0x00e1;
    L_0x0045:
        r8 = r7.captionX;	 Catch:{ Exception -> 0x00dc }
        r0 = r0 - r8;
        r8 = r7.captionY;	 Catch:{ Exception -> 0x00dc }
        r3 = r3 - r8;
        r8 = r7.captionLayout;	 Catch:{ Exception -> 0x00dc }
        r8 = r8.getLineForVertical(r3);	 Catch:{ Exception -> 0x00dc }
        r3 = r7.captionLayout;	 Catch:{ Exception -> 0x00dc }
        r0 = (float) r0;	 Catch:{ Exception -> 0x00dc }
        r3 = r3.getOffsetForHorizontal(r8, r0);	 Catch:{ Exception -> 0x00dc }
        r4 = r7.captionLayout;	 Catch:{ Exception -> 0x00dc }
        r4 = r4.getLineLeft(r8);	 Catch:{ Exception -> 0x00dc }
        r6 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r6 > 0) goto L_0x00f5;
    L_0x0062:
        r6 = r7.captionLayout;	 Catch:{ Exception -> 0x00dc }
        r8 = r6.getLineWidth(r8);	 Catch:{ Exception -> 0x00dc }
        r4 = r4 + r8;
        r8 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r8 < 0) goto L_0x00f5;
    L_0x006d:
        r8 = r7.currentCaption;	 Catch:{ Exception -> 0x00dc }
        r8 = (android.text.Spannable) r8;	 Catch:{ Exception -> 0x00dc }
        r0 = android.text.style.ClickableSpan.class;
        r0 = r8.getSpans(r3, r3, r0);	 Catch:{ Exception -> 0x00dc }
        r0 = (android.text.style.CharacterStyle[]) r0;	 Catch:{ Exception -> 0x00dc }
        if (r0 == 0) goto L_0x007e;
    L_0x007b:
        r4 = r0.length;	 Catch:{ Exception -> 0x00dc }
        if (r4 != 0) goto L_0x0086;
    L_0x007e:
        r0 = org.telegram.p004ui.Components.URLSpanMono.class;
        r0 = r8.getSpans(r3, r3, r0);	 Catch:{ Exception -> 0x00dc }
        r0 = (android.text.style.CharacterStyle[]) r0;	 Catch:{ Exception -> 0x00dc }
    L_0x0086:
        r3 = r0.length;	 Catch:{ Exception -> 0x00dc }
        if (r3 == 0) goto L_0x0099;
    L_0x0089:
        r3 = r0.length;	 Catch:{ Exception -> 0x00dc }
        if (r3 == 0) goto L_0x0097;
    L_0x008c:
        r3 = r0[r1];	 Catch:{ Exception -> 0x00dc }
        r3 = r3 instanceof org.telegram.p004ui.Components.URLSpanBotCommand;	 Catch:{ Exception -> 0x00dc }
        if (r3 == 0) goto L_0x0097;
    L_0x0092:
        r3 = org.telegram.p004ui.Components.URLSpanBotCommand.enabled;	 Catch:{ Exception -> 0x00dc }
        if (r3 != 0) goto L_0x0097;
    L_0x0096:
        goto L_0x0099;
    L_0x0097:
        r3 = 0;
        goto L_0x009a;
    L_0x0099:
        r3 = 1;
    L_0x009a:
        if (r3 != 0) goto L_0x00f5;
    L_0x009c:
        r0 = r0[r1];	 Catch:{ Exception -> 0x00dc }
        r7.pressedLink = r0;	 Catch:{ Exception -> 0x00dc }
        r7.pressedLinkType = r5;	 Catch:{ Exception -> 0x00dc }
        r7.resetUrlPaths(r1);	 Catch:{ Exception -> 0x00dc }
        r0 = r7.obtainNewUrlPath(r1);	 Catch:{ Exception -> 0x00c1 }
        r3 = r7.pressedLink;	 Catch:{ Exception -> 0x00c1 }
        r3 = r8.getSpanStart(r3);	 Catch:{ Exception -> 0x00c1 }
        r4 = r7.captionLayout;	 Catch:{ Exception -> 0x00c1 }
        r5 = 0;
        r0.setCurrentLayout(r4, r3, r5);	 Catch:{ Exception -> 0x00c1 }
        r4 = r7.captionLayout;	 Catch:{ Exception -> 0x00c1 }
        r5 = r7.pressedLink;	 Catch:{ Exception -> 0x00c1 }
        r8 = r8.getSpanEnd(r5);	 Catch:{ Exception -> 0x00c1 }
        r4.getSelectionPath(r3, r8, r0);	 Catch:{ Exception -> 0x00c1 }
        goto L_0x00c5;
    L_0x00c1:
        r8 = move-exception;
        org.telegram.messenger.FileLog.m30e(r8);	 Catch:{ Exception -> 0x00dc }
    L_0x00c5:
        r8 = r7.currentMessagesGroup;	 Catch:{ Exception -> 0x00dc }
        if (r8 == 0) goto L_0x00d8;
    L_0x00c9:
        r8 = r7.getParent();	 Catch:{ Exception -> 0x00dc }
        if (r8 == 0) goto L_0x00d8;
    L_0x00cf:
        r8 = r7.getParent();	 Catch:{ Exception -> 0x00dc }
        r8 = (android.view.ViewGroup) r8;	 Catch:{ Exception -> 0x00dc }
        r8.invalidate();	 Catch:{ Exception -> 0x00dc }
    L_0x00d8:
        r7.invalidate();	 Catch:{ Exception -> 0x00dc }
        return r2;
    L_0x00dc:
        r8 = move-exception;
        org.telegram.messenger.FileLog.m30e(r8);
        goto L_0x00f5;
    L_0x00e1:
        r8 = r7.pressedLinkType;
        if (r8 != r5) goto L_0x00f5;
    L_0x00e5:
        r8 = r7.delegate;
        r0 = r7.currentMessageObject;
        r3 = r7.pressedLink;
        r8.didPressUrl(r0, r3, r1);
        r7.resetPressedLink(r5);
        return r2;
    L_0x00f2:
        r7.resetPressedLink(r5);
    L_0x00f5:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.checkCaptionMotionEvent(android.view.MotionEvent):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x00bb A:{Catch:{ Exception -> 0x00ec }} */
    private boolean checkGameMotionEvent(android.view.MotionEvent r8) {
        /*
        r7 = this;
        r0 = r7.hasGamePreview;
        r1 = 0;
        if (r0 != 0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        r0 = r8.getX();
        r0 = (int) r0;
        r2 = r8.getY();
        r2 = (int) r2;
        r3 = r8.getAction();
        r4 = 2;
        r5 = 1;
        if (r3 != 0) goto L_0x00f2;
    L_0x0018:
        r8 = r7.drawPhotoImage;
        if (r8 == 0) goto L_0x004c;
    L_0x001c:
        r8 = r7.drawImageButton;
        if (r8 == 0) goto L_0x004c;
    L_0x0020:
        r8 = r7.buttonState;
        r3 = -1;
        if (r8 == r3) goto L_0x004c;
    L_0x0025:
        r8 = r7.buttonX;
        if (r0 < r8) goto L_0x004c;
    L_0x0029:
        r3 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r8 = r8 + r6;
        if (r0 > r8) goto L_0x004c;
    L_0x0032:
        r8 = r7.buttonY;
        if (r2 < r8) goto L_0x004c;
    L_0x0036:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r8 = r8 + r3;
        if (r2 > r8) goto L_0x004c;
    L_0x003d:
        r8 = r7.radialProgress;
        r8 = r8.getIcon();
        r3 = 4;
        if (r8 == r3) goto L_0x004c;
    L_0x0046:
        r7.buttonPressed = r5;
        r7.invalidate();
        return r5;
    L_0x004c:
        r8 = r7.drawPhotoImage;
        if (r8 == 0) goto L_0x005d;
    L_0x0050:
        r8 = r7.photoImage;
        r3 = (float) r0;
        r6 = (float) r2;
        r8 = r8.isInsideImage(r3, r6);
        if (r8 == 0) goto L_0x005d;
    L_0x005a:
        r7.gamePreviewPressed = r5;
        return r5;
    L_0x005d:
        r8 = r7.descriptionLayout;
        if (r8 == 0) goto L_0x0170;
    L_0x0061:
        r8 = r7.descriptionY;
        if (r2 < r8) goto L_0x0170;
    L_0x0065:
        r8 = r7.unmovedTextX;	 Catch:{ Exception -> 0x00ec }
        r3 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);	 Catch:{ Exception -> 0x00ec }
        r8 = r8 + r3;
        r3 = r7.descriptionX;	 Catch:{ Exception -> 0x00ec }
        r8 = r8 + r3;
        r0 = r0 - r8;
        r8 = r7.descriptionY;	 Catch:{ Exception -> 0x00ec }
        r2 = r2 - r8;
        r8 = r7.descriptionLayout;	 Catch:{ Exception -> 0x00ec }
        r8 = r8.getLineForVertical(r2);	 Catch:{ Exception -> 0x00ec }
        r2 = r7.descriptionLayout;	 Catch:{ Exception -> 0x00ec }
        r0 = (float) r0;	 Catch:{ Exception -> 0x00ec }
        r2 = r2.getOffsetForHorizontal(r8, r0);	 Catch:{ Exception -> 0x00ec }
        r3 = r7.descriptionLayout;	 Catch:{ Exception -> 0x00ec }
        r3 = r3.getLineLeft(r8);	 Catch:{ Exception -> 0x00ec }
        r6 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1));
        if (r6 > 0) goto L_0x0170;
    L_0x008c:
        r6 = r7.descriptionLayout;	 Catch:{ Exception -> 0x00ec }
        r8 = r6.getLineWidth(r8);	 Catch:{ Exception -> 0x00ec }
        r3 = r3 + r8;
        r8 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1));
        if (r8 < 0) goto L_0x0170;
    L_0x0097:
        r8 = r7.currentMessageObject;	 Catch:{ Exception -> 0x00ec }
        r8 = r8.linkDescription;	 Catch:{ Exception -> 0x00ec }
        r8 = (android.text.Spannable) r8;	 Catch:{ Exception -> 0x00ec }
        r0 = android.text.style.ClickableSpan.class;
        r0 = r8.getSpans(r2, r2, r0);	 Catch:{ Exception -> 0x00ec }
        r0 = (android.text.style.ClickableSpan[]) r0;	 Catch:{ Exception -> 0x00ec }
        r2 = r0.length;	 Catch:{ Exception -> 0x00ec }
        if (r2 == 0) goto L_0x00b8;
    L_0x00a8:
        r2 = r0.length;	 Catch:{ Exception -> 0x00ec }
        if (r2 == 0) goto L_0x00b6;
    L_0x00ab:
        r2 = r0[r1];	 Catch:{ Exception -> 0x00ec }
        r2 = r2 instanceof org.telegram.p004ui.Components.URLSpanBotCommand;	 Catch:{ Exception -> 0x00ec }
        if (r2 == 0) goto L_0x00b6;
    L_0x00b1:
        r2 = org.telegram.p004ui.Components.URLSpanBotCommand.enabled;	 Catch:{ Exception -> 0x00ec }
        if (r2 != 0) goto L_0x00b6;
    L_0x00b5:
        goto L_0x00b8;
    L_0x00b6:
        r2 = 0;
        goto L_0x00b9;
    L_0x00b8:
        r2 = 1;
    L_0x00b9:
        if (r2 != 0) goto L_0x0170;
    L_0x00bb:
        r0 = r0[r1];	 Catch:{ Exception -> 0x00ec }
        r7.pressedLink = r0;	 Catch:{ Exception -> 0x00ec }
        r0 = -10;
        r7.linkBlockNum = r0;	 Catch:{ Exception -> 0x00ec }
        r7.pressedLinkType = r4;	 Catch:{ Exception -> 0x00ec }
        r7.resetUrlPaths(r1);	 Catch:{ Exception -> 0x00ec }
        r0 = r7.obtainNewUrlPath(r1);	 Catch:{ Exception -> 0x00e4 }
        r2 = r7.pressedLink;	 Catch:{ Exception -> 0x00e4 }
        r2 = r8.getSpanStart(r2);	 Catch:{ Exception -> 0x00e4 }
        r3 = r7.descriptionLayout;	 Catch:{ Exception -> 0x00e4 }
        r4 = 0;
        r0.setCurrentLayout(r3, r2, r4);	 Catch:{ Exception -> 0x00e4 }
        r3 = r7.descriptionLayout;	 Catch:{ Exception -> 0x00e4 }
        r4 = r7.pressedLink;	 Catch:{ Exception -> 0x00e4 }
        r8 = r8.getSpanEnd(r4);	 Catch:{ Exception -> 0x00e4 }
        r3.getSelectionPath(r2, r8, r0);	 Catch:{ Exception -> 0x00e4 }
        goto L_0x00e8;
    L_0x00e4:
        r8 = move-exception;
        org.telegram.messenger.FileLog.m30e(r8);	 Catch:{ Exception -> 0x00ec }
    L_0x00e8:
        r7.invalidate();	 Catch:{ Exception -> 0x00ec }
        return r5;
    L_0x00ec:
        r8 = move-exception;
        org.telegram.messenger.FileLog.m30e(r8);
        goto L_0x0170;
    L_0x00f2:
        r8 = r8.getAction();
        if (r8 != r5) goto L_0x0170;
    L_0x00f8:
        r8 = r7.pressedLinkType;
        if (r8 == r4) goto L_0x0109;
    L_0x00fc:
        r8 = r7.gamePreviewPressed;
        if (r8 != 0) goto L_0x0109;
    L_0x0100:
        r8 = r7.buttonPressed;
        if (r8 == 0) goto L_0x0105;
    L_0x0104:
        goto L_0x0109;
    L_0x0105:
        r7.resetPressedLink(r4);
        goto L_0x0170;
    L_0x0109:
        r8 = r7.buttonPressed;
        if (r8 == 0) goto L_0x0119;
    L_0x010d:
        r7.buttonPressed = r1;
        r7.playSoundEffect(r1);
        r7.didPressButton(r5, r1);
        r7.invalidate();
        goto L_0x0170;
    L_0x0119:
        r8 = r7.pressedLink;
        if (r8 == 0) goto L_0x013e;
    L_0x011d:
        r0 = r8 instanceof android.text.style.URLSpan;
        if (r0 == 0) goto L_0x0131;
    L_0x0121:
        r8 = r7.getContext();
        r0 = r7.pressedLink;
        r0 = (android.text.style.URLSpan) r0;
        r0 = r0.getURL();
        org.telegram.messenger.browser.Browser.openUrl(r8, r0);
        goto L_0x013a;
    L_0x0131:
        r0 = r8 instanceof android.text.style.ClickableSpan;
        if (r0 == 0) goto L_0x013a;
    L_0x0135:
        r8 = (android.text.style.ClickableSpan) r8;
        r8.onClick(r7);
    L_0x013a:
        r7.resetPressedLink(r4);
        goto L_0x0170;
    L_0x013e:
        r7.gamePreviewPressed = r1;
        r8 = 0;
    L_0x0141:
        r0 = r7.botButtons;
        r0 = r0.size();
        if (r8 >= r0) goto L_0x016c;
    L_0x0149:
        r0 = r7.botButtons;
        r0 = r0.get(r8);
        r0 = (org.telegram.p004ui.Cells.ChatMessageCell.BotButton) r0;
        r2 = r0.button;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonGame;
        if (r2 == 0) goto L_0x0169;
    L_0x0159:
        r7.playSoundEffect(r1);
        r8 = r7.delegate;
        r0 = r0.button;
        r8.didPressBotButton(r7, r0);
        r7.invalidate();
        goto L_0x016c;
    L_0x0169:
        r8 = r8 + 1;
        goto L_0x0141;
    L_0x016c:
        r7.resetPressedLink(r4);
        return r5;
    L_0x0170:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.checkGameMotionEvent(android.view.MotionEvent):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00b5 A:{Catch:{ Exception -> 0x00e6 }} */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0117  */
    /* JADX WARNING: Missing block: B:101:0x0185, code skipped:
            if (r1.radialProgress.getIcon() != 4) goto L_0x0187;
     */
    private boolean checkLinkPreviewMotionEvent(android.view.MotionEvent r17) {
        /*
        r16 = this;
        r1 = r16;
        r0 = r1.currentMessageObject;
        r0 = r0.type;
        r2 = 0;
        if (r0 != 0) goto L_0x0371;
    L_0x0009:
        r0 = r1.hasLinkPreview;
        if (r0 != 0) goto L_0x000f;
    L_0x000d:
        goto L_0x0371;
    L_0x000f:
        r0 = r17.getX();
        r3 = (int) r0;
        r0 = r17.getY();
        r4 = (int) r0;
        r0 = r1.unmovedTextX;
        if (r3 < r0) goto L_0x0371;
    L_0x001d:
        r5 = r1.backgroundWidth;
        r0 = r0 + r5;
        if (r3 > r0) goto L_0x0371;
    L_0x0022:
        r0 = r1.textY;
        r5 = r1.currentMessageObject;
        r5 = r5.textHeight;
        r6 = r0 + r5;
        if (r4 < r6) goto L_0x0371;
    L_0x002c:
        r0 = r0 + r5;
        r5 = r1.linkPreviewHeight;
        r0 = r0 + r5;
        r5 = r1.drawInstantView;
        if (r5 == 0) goto L_0x0037;
    L_0x0034:
        r5 = 46;
        goto L_0x0038;
    L_0x0037:
        r5 = 0;
    L_0x0038:
        r5 = r5 + 8;
        r5 = (float) r5;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r0 = r0 + r5;
        if (r4 > r0) goto L_0x0371;
    L_0x0042:
        r0 = r17.getAction();
        r5 = 21;
        r6 = -1;
        r7 = 2;
        r8 = 1;
        if (r0 != 0) goto L_0x01f5;
    L_0x004d:
        r0 = r1.descriptionLayout;
        if (r0 == 0) goto L_0x00ea;
    L_0x0051:
        r0 = r1.descriptionY;
        if (r4 < r0) goto L_0x00ea;
    L_0x0055:
        r0 = r1.unmovedTextX;	 Catch:{ Exception -> 0x00e6 }
        r9 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);	 Catch:{ Exception -> 0x00e6 }
        r0 = r0 + r9;
        r9 = r1.descriptionX;	 Catch:{ Exception -> 0x00e6 }
        r0 = r0 + r9;
        r0 = r3 - r0;
        r9 = r1.descriptionY;	 Catch:{ Exception -> 0x00e6 }
        r9 = r4 - r9;
        r10 = r1.descriptionLayout;	 Catch:{ Exception -> 0x00e6 }
        r10 = r10.getHeight();	 Catch:{ Exception -> 0x00e6 }
        if (r9 > r10) goto L_0x00ea;
    L_0x006f:
        r10 = r1.descriptionLayout;	 Catch:{ Exception -> 0x00e6 }
        r9 = r10.getLineForVertical(r9);	 Catch:{ Exception -> 0x00e6 }
        r10 = r1.descriptionLayout;	 Catch:{ Exception -> 0x00e6 }
        r0 = (float) r0;	 Catch:{ Exception -> 0x00e6 }
        r10 = r10.getOffsetForHorizontal(r9, r0);	 Catch:{ Exception -> 0x00e6 }
        r11 = r1.descriptionLayout;	 Catch:{ Exception -> 0x00e6 }
        r11 = r11.getLineLeft(r9);	 Catch:{ Exception -> 0x00e6 }
        r12 = (r11 > r0 ? 1 : (r11 == r0 ? 0 : -1));
        if (r12 > 0) goto L_0x00ea;
    L_0x0086:
        r12 = r1.descriptionLayout;	 Catch:{ Exception -> 0x00e6 }
        r9 = r12.getLineWidth(r9);	 Catch:{ Exception -> 0x00e6 }
        r11 = r11 + r9;
        r0 = (r11 > r0 ? 1 : (r11 == r0 ? 0 : -1));
        if (r0 < 0) goto L_0x00ea;
    L_0x0091:
        r0 = r1.currentMessageObject;	 Catch:{ Exception -> 0x00e6 }
        r0 = r0.linkDescription;	 Catch:{ Exception -> 0x00e6 }
        r0 = (android.text.Spannable) r0;	 Catch:{ Exception -> 0x00e6 }
        r9 = android.text.style.ClickableSpan.class;
        r9 = r0.getSpans(r10, r10, r9);	 Catch:{ Exception -> 0x00e6 }
        r9 = (android.text.style.ClickableSpan[]) r9;	 Catch:{ Exception -> 0x00e6 }
        r10 = r9.length;	 Catch:{ Exception -> 0x00e6 }
        if (r10 == 0) goto L_0x00b2;
    L_0x00a2:
        r10 = r9.length;	 Catch:{ Exception -> 0x00e6 }
        if (r10 == 0) goto L_0x00b0;
    L_0x00a5:
        r10 = r9[r2];	 Catch:{ Exception -> 0x00e6 }
        r10 = r10 instanceof org.telegram.p004ui.Components.URLSpanBotCommand;	 Catch:{ Exception -> 0x00e6 }
        if (r10 == 0) goto L_0x00b0;
    L_0x00ab:
        r10 = org.telegram.p004ui.Components.URLSpanBotCommand.enabled;	 Catch:{ Exception -> 0x00e6 }
        if (r10 != 0) goto L_0x00b0;
    L_0x00af:
        goto L_0x00b2;
    L_0x00b0:
        r10 = 0;
        goto L_0x00b3;
    L_0x00b2:
        r10 = 1;
    L_0x00b3:
        if (r10 != 0) goto L_0x00ea;
    L_0x00b5:
        r9 = r9[r2];	 Catch:{ Exception -> 0x00e6 }
        r1.pressedLink = r9;	 Catch:{ Exception -> 0x00e6 }
        r9 = -10;
        r1.linkBlockNum = r9;	 Catch:{ Exception -> 0x00e6 }
        r1.pressedLinkType = r7;	 Catch:{ Exception -> 0x00e6 }
        r1.resetUrlPaths(r2);	 Catch:{ Exception -> 0x00e6 }
        r9 = r1.obtainNewUrlPath(r2);	 Catch:{ Exception -> 0x00de }
        r10 = r1.pressedLink;	 Catch:{ Exception -> 0x00de }
        r10 = r0.getSpanStart(r10);	 Catch:{ Exception -> 0x00de }
        r11 = r1.descriptionLayout;	 Catch:{ Exception -> 0x00de }
        r12 = 0;
        r9.setCurrentLayout(r11, r10, r12);	 Catch:{ Exception -> 0x00de }
        r11 = r1.descriptionLayout;	 Catch:{ Exception -> 0x00de }
        r12 = r1.pressedLink;	 Catch:{ Exception -> 0x00de }
        r0 = r0.getSpanEnd(r12);	 Catch:{ Exception -> 0x00de }
        r11.getSelectionPath(r10, r0, r9);	 Catch:{ Exception -> 0x00de }
        goto L_0x00e2;
    L_0x00de:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);	 Catch:{ Exception -> 0x00e6 }
    L_0x00e2:
        r16.invalidate();	 Catch:{ Exception -> 0x00e6 }
        return r8;
    L_0x00e6:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x00ea:
        r0 = r1.pressedLink;
        if (r0 != 0) goto L_0x0371;
    L_0x00ee:
        r0 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r10 = r1.miniButtonState;
        if (r10 < 0) goto L_0x0114;
    L_0x00f8:
        r10 = 1104674816; // 0x41d80000 float:27.0 double:5.457818764E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r11 = r1.buttonX;
        r12 = r11 + r10;
        if (r3 < r12) goto L_0x0114;
    L_0x0104:
        r11 = r11 + r10;
        r11 = r11 + r9;
        if (r3 > r11) goto L_0x0114;
    L_0x0108:
        r11 = r1.buttonY;
        r12 = r11 + r10;
        if (r4 < r12) goto L_0x0114;
    L_0x010e:
        r11 = r11 + r10;
        r11 = r11 + r9;
        if (r4 > r11) goto L_0x0114;
    L_0x0112:
        r9 = 1;
        goto L_0x0115;
    L_0x0114:
        r9 = 0;
    L_0x0115:
        if (r9 == 0) goto L_0x011d;
    L_0x0117:
        r1.miniButtonPressed = r8;
        r16.invalidate();
        return r8;
    L_0x011d:
        r9 = r1.drawVideoImageButton;
        if (r9 == 0) goto L_0x014e;
    L_0x0121:
        r9 = r1.buttonState;
        if (r9 == r6) goto L_0x014e;
    L_0x0125:
        r9 = r1.videoButtonX;
        if (r3 < r9) goto L_0x014e;
    L_0x0129:
        r10 = 1107820544; // 0x42080000 float:34.0 double:5.473360725E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r9 = r9 + r10;
        r10 = r1.infoWidth;
        r11 = r1.docTitleWidth;
        r10 = java.lang.Math.max(r10, r11);
        r9 = r9 + r10;
        if (r3 > r9) goto L_0x014e;
    L_0x013b:
        r9 = r1.videoButtonY;
        if (r4 < r9) goto L_0x014e;
    L_0x013f:
        r10 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r9 = r9 + r10;
        if (r4 > r9) goto L_0x014e;
    L_0x0148:
        r1.videoButtonPressed = r8;
        r16.invalidate();
        return r8;
    L_0x014e:
        r9 = r1.drawPhotoImage;
        if (r9 == 0) goto L_0x018d;
    L_0x0152:
        r9 = r1.drawImageButton;
        if (r9 == 0) goto L_0x018d;
    L_0x0156:
        r9 = r1.buttonState;
        if (r9 == r6) goto L_0x018d;
    L_0x015a:
        r9 = r1.checkOnlyButtonPressed;
        if (r9 != 0) goto L_0x0168;
    L_0x015e:
        r9 = r1.photoImage;
        r10 = (float) r3;
        r11 = (float) r4;
        r9 = r9.isInsideImage(r10, r11);
        if (r9 != 0) goto L_0x0187;
    L_0x0168:
        r9 = r1.buttonX;
        if (r3 < r9) goto L_0x018d;
    L_0x016c:
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r9 = r9 + r10;
        if (r3 > r9) goto L_0x018d;
    L_0x0173:
        r9 = r1.buttonY;
        if (r4 < r9) goto L_0x018d;
    L_0x0177:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r9 = r9 + r0;
        if (r4 > r9) goto L_0x018d;
    L_0x017e:
        r0 = r1.radialProgress;
        r0 = r0.getIcon();
        r9 = 4;
        if (r0 == r9) goto L_0x018d;
    L_0x0187:
        r1.buttonPressed = r8;
        r16.invalidate();
        return r8;
    L_0x018d:
        r0 = r1.drawInstantView;
        if (r0 == 0) goto L_0x01b9;
    L_0x0191:
        r1.instantPressed = r8;
        r0 = android.os.Build.VERSION.SDK_INT;
        if (r0 < r5) goto L_0x01b5;
    L_0x0197:
        r0 = r1.selectorDrawable;
        if (r0 == 0) goto L_0x01b5;
    L_0x019b:
        r0 = r0.getBounds();
        r0 = r0.contains(r3, r4);
        if (r0 == 0) goto L_0x01b5;
    L_0x01a5:
        r0 = r1.selectorDrawable;
        r2 = r1.pressedState;
        r0.setState(r2);
        r0 = r1.selectorDrawable;
        r2 = (float) r3;
        r3 = (float) r4;
        r0.setHotspot(r2, r3);
        r1.instantButtonPressed = r8;
    L_0x01b5:
        r16.invalidate();
        return r8;
    L_0x01b9:
        r0 = r1.documentAttachType;
        if (r0 == r8) goto L_0x0371;
    L_0x01bd:
        r0 = r1.drawPhotoImage;
        if (r0 == 0) goto L_0x0371;
    L_0x01c1:
        r0 = r1.photoImage;
        r3 = (float) r3;
        r4 = (float) r4;
        r0 = r0.isInsideImage(r3, r4);
        if (r0 == 0) goto L_0x0371;
    L_0x01cb:
        r1.linkPreviewPressed = r8;
        r0 = r1.currentMessageObject;
        r0 = r0.messageOwner;
        r0 = r0.media;
        r0 = r0.webpage;
        r3 = r1.documentAttachType;
        if (r3 != r7) goto L_0x01f4;
    L_0x01d9:
        r3 = r1.buttonState;
        if (r3 != r6) goto L_0x01f4;
    L_0x01dd:
        r3 = org.telegram.messenger.SharedConfig.autoplayGifs;
        if (r3 == 0) goto L_0x01f4;
    L_0x01e1:
        r3 = r1.photoImage;
        r3 = r3.getAnimation();
        if (r3 == 0) goto L_0x01f1;
    L_0x01e9:
        r0 = r0.embed_url;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x01f4;
    L_0x01f1:
        r1.linkPreviewPressed = r2;
        return r2;
    L_0x01f4:
        return r8;
    L_0x01f5:
        r0 = r17.getAction();
        if (r0 != r8) goto L_0x035a;
    L_0x01fb:
        r0 = r1.instantPressed;
        if (r0 == 0) goto L_0x0221;
    L_0x01ff:
        r0 = r1.delegate;
        if (r0 == 0) goto L_0x0208;
    L_0x0203:
        r3 = r1.drawInstantViewType;
        r0.didPressInstantButton(r1, r3);
    L_0x0208:
        r1.playSoundEffect(r2);
        r0 = android.os.Build.VERSION.SDK_INT;
        if (r0 < r5) goto L_0x0218;
    L_0x020f:
        r0 = r1.selectorDrawable;
        if (r0 == 0) goto L_0x0218;
    L_0x0213:
        r3 = android.util.StateSet.NOTHING;
        r0.setState(r3);
    L_0x0218:
        r1.instantButtonPressed = r2;
        r1.instantPressed = r2;
        r16.invalidate();
        goto L_0x0371;
    L_0x0221:
        r0 = r1.pressedLinkType;
        if (r0 == r7) goto L_0x023b;
    L_0x0225:
        r0 = r1.buttonPressed;
        if (r0 != 0) goto L_0x023b;
    L_0x0229:
        r0 = r1.miniButtonPressed;
        if (r0 != 0) goto L_0x023b;
    L_0x022d:
        r0 = r1.videoButtonPressed;
        if (r0 != 0) goto L_0x023b;
    L_0x0231:
        r0 = r1.linkPreviewPressed;
        if (r0 == 0) goto L_0x0236;
    L_0x0235:
        goto L_0x023b;
    L_0x0236:
        r1.resetPressedLink(r7);
        goto L_0x0371;
    L_0x023b:
        r0 = r1.videoButtonPressed;
        if (r0 != r8) goto L_0x024c;
    L_0x023f:
        r1.videoButtonPressed = r2;
        r1.playSoundEffect(r2);
        r1.didPressButton(r8, r8);
        r16.invalidate();
        goto L_0x0371;
    L_0x024c:
        r0 = r1.buttonPressed;
        if (r0 == 0) goto L_0x0265;
    L_0x0250:
        r1.buttonPressed = r2;
        r1.playSoundEffect(r2);
        r0 = r1.drawVideoImageButton;
        if (r0 == 0) goto L_0x025d;
    L_0x0259:
        r16.didClickedImage();
        goto L_0x0260;
    L_0x025d:
        r1.didPressButton(r8, r2);
    L_0x0260:
        r16.invalidate();
        goto L_0x0371;
    L_0x0265:
        r0 = r1.miniButtonPressed;
        if (r0 == 0) goto L_0x0276;
    L_0x0269:
        r1.miniButtonPressed = r2;
        r1.playSoundEffect(r2);
        r1.didPressMiniButton(r8);
        r16.invalidate();
        goto L_0x0371;
    L_0x0276:
        r0 = r1.pressedLink;
        if (r0 == 0) goto L_0x029c;
    L_0x027a:
        r3 = r0 instanceof android.text.style.URLSpan;
        if (r3 == 0) goto L_0x028e;
    L_0x027e:
        r0 = r16.getContext();
        r3 = r1.pressedLink;
        r3 = (android.text.style.URLSpan) r3;
        r3 = r3.getURL();
        org.telegram.messenger.browser.Browser.openUrl(r0, r3);
        goto L_0x0297;
    L_0x028e:
        r3 = r0 instanceof android.text.style.ClickableSpan;
        if (r3 == 0) goto L_0x0297;
    L_0x0292:
        r0 = (android.text.style.ClickableSpan) r0;
        r0.onClick(r1);
    L_0x0297:
        r1.resetPressedLink(r7);
        goto L_0x0371;
    L_0x029c:
        r0 = r1.documentAttachType;
        r3 = 7;
        if (r0 != r3) goto L_0x02cc;
    L_0x02a1:
        r0 = org.telegram.messenger.MediaController.getInstance();
        r2 = r1.currentMessageObject;
        r0 = r0.isPlayingMessage(r2);
        if (r0 == 0) goto L_0x02c3;
    L_0x02ad:
        r0 = org.telegram.messenger.MediaController.getInstance();
        r0 = r0.isMessagePaused();
        if (r0 == 0) goto L_0x02b8;
    L_0x02b7:
        goto L_0x02c3;
    L_0x02b8:
        r0 = org.telegram.messenger.MediaController.getInstance();
        r2 = r1.currentMessageObject;
        r0.lambda$startAudioAgain$5$MediaController(r2);
        goto L_0x0356;
    L_0x02c3:
        r0 = r1.delegate;
        r2 = r1.currentMessageObject;
        r0.needPlayMessage(r2);
        goto L_0x0356;
    L_0x02cc:
        if (r0 != r7) goto L_0x0312;
    L_0x02ce:
        r0 = r1.drawImageButton;
        if (r0 == 0) goto L_0x0312;
    L_0x02d2:
        r0 = r1.buttonState;
        if (r0 != r6) goto L_0x0307;
    L_0x02d6:
        r0 = org.telegram.messenger.SharedConfig.autoplayGifs;
        if (r0 == 0) goto L_0x02e5;
    L_0x02da:
        r0 = r1.delegate;
        r2 = r1.lastTouchX;
        r3 = r1.lastTouchY;
        r0.didPressImage(r1, r2, r3);
        goto L_0x0356;
    L_0x02e5:
        r1.buttonState = r7;
        r0 = r1.currentMessageObject;
        r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0.gifState = r3;
        r0 = r1.photoImage;
        r0.setAllowStartAnimation(r2);
        r0 = r1.photoImage;
        r0.stopAnimation();
        r0 = r1.radialProgress;
        r3 = r16.getIconForCurrentState();
        r0.setIcon(r3, r2, r8);
        r16.invalidate();
        r1.playSoundEffect(r2);
        goto L_0x0356;
    L_0x0307:
        if (r0 == r7) goto L_0x030b;
    L_0x0309:
        if (r0 != 0) goto L_0x0356;
    L_0x030b:
        r1.didPressButton(r8, r2);
        r1.playSoundEffect(r2);
        goto L_0x0356;
    L_0x0312:
        r0 = r1.currentMessageObject;
        r0 = r0.messageOwner;
        r0 = r0.media;
        r0 = r0.webpage;
        if (r0 == 0) goto L_0x0336;
    L_0x031c:
        r3 = r0.embed_url;
        r3 = android.text.TextUtils.isEmpty(r3);
        if (r3 != 0) goto L_0x0336;
    L_0x0324:
        r9 = r1.delegate;
        r10 = r0.embed_url;
        r11 = r0.site_name;
        r12 = r0.title;
        r13 = r0.url;
        r14 = r0.embed_width;
        r15 = r0.embed_height;
        r9.needOpenWebView(r10, r11, r12, r13, r14, r15);
        goto L_0x0356;
    L_0x0336:
        r3 = r1.buttonState;
        if (r3 == r6) goto L_0x034a;
    L_0x033a:
        r4 = 3;
        if (r3 != r4) goto L_0x033e;
    L_0x033d:
        goto L_0x034a;
    L_0x033e:
        if (r0 == 0) goto L_0x0356;
    L_0x0340:
        r2 = r16.getContext();
        r0 = r0.url;
        org.telegram.messenger.browser.Browser.openUrl(r2, r0);
        goto L_0x0356;
    L_0x034a:
        r0 = r1.delegate;
        r3 = r1.lastTouchX;
        r4 = r1.lastTouchY;
        r0.didPressImage(r1, r3, r4);
        r1.playSoundEffect(r2);
    L_0x0356:
        r1.resetPressedLink(r7);
        return r8;
    L_0x035a:
        r0 = r17.getAction();
        if (r0 != r7) goto L_0x0371;
    L_0x0360:
        r0 = r1.instantButtonPressed;
        if (r0 == 0) goto L_0x0371;
    L_0x0364:
        r0 = android.os.Build.VERSION.SDK_INT;
        if (r0 < r5) goto L_0x0371;
    L_0x0368:
        r0 = r1.selectorDrawable;
        if (r0 == 0) goto L_0x0371;
    L_0x036c:
        r3 = (float) r3;
        r4 = (float) r4;
        r0.setHotspot(r3, r4);
    L_0x0371:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.checkLinkPreviewMotionEvent(android.view.MotionEvent):boolean");
    }

    private boolean checkPollButtonMotionEvent(MotionEvent motionEvent) {
        if (this.currentMessageObject.eventId != 0 || this.pollVoted || this.pollClosed || this.pollVoteInProgress || this.pollUnvoteInProgress || this.pollButtons.isEmpty()) {
            return false;
        }
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject.type != 17 || !messageObject.isSent()) {
            return false;
        }
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int i;
        Drawable drawable;
        if (motionEvent.getAction() == 0) {
            this.pressedVoteButton = -1;
            i = 0;
            while (i < this.pollButtons.size()) {
                PollButton pollButton = (PollButton) this.pollButtons.get(i);
                int access$600 = (pollButton.f578y + this.namesOffset) - AndroidUtilities.m26dp(13.0f);
                if (x < pollButton.f577x || x > (pollButton.f577x + this.backgroundWidth) - AndroidUtilities.m26dp(31.0f) || y < access$600 || y > (pollButton.height + access$600) + AndroidUtilities.m26dp(26.0f)) {
                    i++;
                } else {
                    this.pressedVoteButton = i;
                    if (VERSION.SDK_INT >= 21) {
                        drawable = this.selectorDrawable;
                        if (drawable != null) {
                            drawable.setBounds(pollButton.f577x - AndroidUtilities.m26dp(9.0f), access$600, (pollButton.f577x + this.backgroundWidth) - AndroidUtilities.m26dp(22.0f), (pollButton.height + access$600) + AndroidUtilities.m26dp(26.0f));
                            this.selectorDrawable.setState(this.pressedState);
                            this.selectorDrawable.setHotspot((float) x, (float) y);
                        }
                    }
                    invalidate();
                    return true;
                }
            }
            return false;
        } else if (motionEvent.getAction() == 1) {
            if (this.pressedVoteButton == -1) {
                return false;
            }
            playSoundEffect(0);
            if (VERSION.SDK_INT >= 21) {
                drawable = this.selectorDrawable;
                if (drawable != null) {
                    drawable.setState(StateSet.NOTHING);
                }
            }
            i = this.pressedVoteButton;
            this.pollVoteInProgressNum = i;
            this.pollVoteInProgress = true;
            this.voteCurrentProgressTime = 0.0f;
            this.firstCircleLength = true;
            this.voteCurrentCircleLength = 360.0f;
            this.voteRisingCircleLength = false;
            this.delegate.didPressVoteButton(this, ((PollButton) this.pollButtons.get(i)).answer);
            this.pressedVoteButton = -1;
            invalidate();
            return false;
        } else if (motionEvent.getAction() != 2 || this.pressedVoteButton == -1 || VERSION.SDK_INT < 21) {
            return false;
        } else {
            drawable = this.selectorDrawable;
            if (drawable == null) {
                return false;
            }
            drawable.setHotspot((float) x, (float) y);
            return false;
        }
    }

    private boolean checkInstantButtonMotionEvent(MotionEvent motionEvent) {
        if (this.drawInstantView && this.currentMessageObject.type != 0) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            Drawable drawable;
            if (motionEvent.getAction() == 0) {
                if (this.drawInstantView) {
                    float f = (float) x;
                    float f2 = (float) y;
                    if (this.instantButtonRect.contains(f, f2)) {
                        this.instantPressed = true;
                        if (VERSION.SDK_INT >= 21) {
                            drawable = this.selectorDrawable;
                            if (drawable != null && drawable.getBounds().contains(x, y)) {
                                this.selectorDrawable.setState(this.pressedState);
                                this.selectorDrawable.setHotspot(f, f2);
                                this.instantButtonPressed = true;
                            }
                        }
                        invalidate();
                        return true;
                    }
                }
            } else if (motionEvent.getAction() == 1) {
                if (this.instantPressed) {
                    ChatMessageCellDelegate chatMessageCellDelegate = this.delegate;
                    if (chatMessageCellDelegate != null) {
                        chatMessageCellDelegate.didPressInstantButton(this, this.drawInstantViewType);
                    }
                    playSoundEffect(0);
                    if (VERSION.SDK_INT >= 21) {
                        drawable = this.selectorDrawable;
                        if (drawable != null) {
                            drawable.setState(StateSet.NOTHING);
                        }
                    }
                    this.instantButtonPressed = false;
                    this.instantPressed = false;
                    invalidate();
                }
            } else if (motionEvent.getAction() == 2 && this.instantButtonPressed && VERSION.SDK_INT >= 21) {
                drawable = this.selectorDrawable;
                if (drawable != null) {
                    drawable.setHotspot((float) x, (float) y);
                }
            }
        }
        return false;
    }

    /* JADX WARNING: Missing block: B:16:0x0026, code skipped:
            if (r4 != 8) goto L_0x0032;
     */
    private boolean checkOtherButtonMotionEvent(android.view.MotionEvent r7) {
        /*
        r6 = this;
        r0 = r6.currentMessageObject;
        r0 = r0.type;
        r1 = 16;
        r2 = 0;
        r3 = 1;
        if (r0 != r1) goto L_0x000c;
    L_0x000a:
        r0 = 1;
        goto L_0x000d;
    L_0x000c:
        r0 = 0;
    L_0x000d:
        if (r0 != 0) goto L_0x0033;
    L_0x000f:
        r0 = r6.documentAttachType;
        if (r0 == r3) goto L_0x0028;
    L_0x0013:
        r4 = r6.currentMessageObject;
        r4 = r4.type;
        r5 = 12;
        if (r4 == r5) goto L_0x0028;
    L_0x001b:
        r5 = 5;
        if (r0 == r5) goto L_0x0028;
    L_0x001e:
        r5 = 4;
        if (r0 == r5) goto L_0x0028;
    L_0x0021:
        r5 = 2;
        if (r0 == r5) goto L_0x0028;
    L_0x0024:
        r0 = 8;
        if (r4 != r0) goto L_0x0032;
    L_0x0028:
        r0 = r6.hasGamePreview;
        if (r0 != 0) goto L_0x0032;
    L_0x002c:
        r0 = r6.hasInvoicePreview;
        if (r0 != 0) goto L_0x0032;
    L_0x0030:
        r0 = 1;
        goto L_0x0033;
    L_0x0032:
        r0 = 0;
    L_0x0033:
        if (r0 != 0) goto L_0x0036;
    L_0x0035:
        return r2;
    L_0x0036:
        r0 = r7.getX();
        r0 = (int) r0;
        r4 = r7.getY();
        r4 = (int) r4;
        r5 = r7.getAction();
        if (r5 != 0) goto L_0x00a5;
    L_0x0046:
        r7 = r6.currentMessageObject;
        r7 = r7.type;
        if (r7 != r1) goto L_0x0075;
    L_0x004c:
        r7 = r6.otherX;
        if (r0 < r7) goto L_0x00c3;
    L_0x0050:
        r1 = 1131085824; // 0x436b0000 float:235.0 double:5.58830648E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r7 = r7 + r1;
        if (r0 > r7) goto L_0x00c3;
    L_0x0059:
        r7 = r6.otherY;
        r0 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r7 = r7 - r0;
        if (r4 < r7) goto L_0x00c3;
    L_0x0064:
        r7 = r6.otherY;
        r0 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r7 = r7 + r0;
        if (r4 > r7) goto L_0x00c3;
    L_0x006f:
        r6.otherPressed = r3;
        r6.invalidate();
        goto L_0x00c4;
    L_0x0075:
        r7 = r6.otherX;
        r1 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r7 = r7 - r5;
        if (r0 < r7) goto L_0x00c3;
    L_0x0080:
        r7 = r6.otherX;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r7 = r7 + r1;
        if (r0 > r7) goto L_0x00c3;
    L_0x0089:
        r7 = r6.otherY;
        r0 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r7 = r7 - r0;
        if (r4 < r7) goto L_0x00c3;
    L_0x0094:
        r7 = r6.otherY;
        r0 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r7 = r7 + r0;
        if (r4 > r7) goto L_0x00c3;
    L_0x009f:
        r6.otherPressed = r3;
        r6.invalidate();
        goto L_0x00c4;
    L_0x00a5:
        r7 = r7.getAction();
        if (r7 != r3) goto L_0x00c3;
    L_0x00ab:
        r7 = r6.otherPressed;
        if (r7 == 0) goto L_0x00c3;
    L_0x00af:
        r6.otherPressed = r2;
        r6.playSoundEffect(r2);
        r7 = r6.delegate;
        r0 = r6.otherX;
        r0 = (float) r0;
        r1 = r6.otherY;
        r1 = (float) r1;
        r7.didPressOther(r6, r0, r1);
        r6.invalidate();
        goto L_0x00c4;
    L_0x00c3:
        r3 = 0;
    L_0x00c4:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.checkOtherButtonMotionEvent(android.view.MotionEvent):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0144  */
    private boolean checkPhotoImageMotionEvent(android.view.MotionEvent r9) {
        /*
        r8 = this;
        r0 = r8.drawPhotoImage;
        r1 = 0;
        r2 = 1;
        if (r0 != 0) goto L_0x000b;
    L_0x0006:
        r0 = r8.documentAttachType;
        if (r0 == r2) goto L_0x000b;
    L_0x000a:
        return r1;
    L_0x000b:
        r0 = r9.getX();
        r0 = (int) r0;
        r3 = r9.getY();
        r3 = (int) r3;
        r4 = r9.getAction();
        r5 = -1;
        if (r4 != 0) goto L_0x016e;
    L_0x001c:
        r9 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r4 = r8.miniButtonState;
        if (r4 < 0) goto L_0x0042;
    L_0x0026:
        r4 = 1104674816; // 0x41d80000 float:27.0 double:5.457818764E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = r8.buttonX;
        r7 = r6 + r4;
        if (r0 < r7) goto L_0x0042;
    L_0x0032:
        r6 = r6 + r4;
        r6 = r6 + r9;
        if (r0 > r6) goto L_0x0042;
    L_0x0036:
        r6 = r8.buttonY;
        r7 = r6 + r4;
        if (r3 < r7) goto L_0x0042;
    L_0x003c:
        r6 = r6 + r4;
        r6 = r6 + r9;
        if (r3 > r6) goto L_0x0042;
    L_0x0040:
        r4 = 1;
        goto L_0x0043;
    L_0x0042:
        r4 = 0;
    L_0x0043:
        if (r4 == 0) goto L_0x004c;
    L_0x0045:
        r8.miniButtonPressed = r2;
        r8.invalidate();
        goto L_0x0140;
    L_0x004c:
        r4 = r8.buttonState;
        if (r4 == r5) goto L_0x006e;
    L_0x0050:
        r4 = r8.radialProgress;
        r4 = r4.getIcon();
        r6 = 4;
        if (r4 == r6) goto L_0x006e;
    L_0x0059:
        r4 = r8.buttonX;
        if (r0 < r4) goto L_0x006e;
    L_0x005d:
        r4 = r4 + r9;
        if (r0 > r4) goto L_0x006e;
    L_0x0060:
        r4 = r8.buttonY;
        if (r3 < r4) goto L_0x006e;
    L_0x0064:
        r4 = r4 + r9;
        if (r3 > r4) goto L_0x006e;
    L_0x0067:
        r8.buttonPressed = r2;
        r8.invalidate();
        goto L_0x0140;
    L_0x006e:
        r9 = r8.drawVideoImageButton;
        if (r9 == 0) goto L_0x00a0;
    L_0x0072:
        r9 = r8.buttonState;
        if (r9 == r5) goto L_0x00a0;
    L_0x0076:
        r9 = r8.videoButtonX;
        if (r0 < r9) goto L_0x00a0;
    L_0x007a:
        r4 = 1107820544; // 0x42080000 float:34.0 double:5.473360725E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r9 = r9 + r4;
        r4 = r8.infoWidth;
        r6 = r8.docTitleWidth;
        r4 = java.lang.Math.max(r4, r6);
        r9 = r9 + r4;
        if (r0 > r9) goto L_0x00a0;
    L_0x008c:
        r9 = r8.videoButtonY;
        if (r3 < r9) goto L_0x00a0;
    L_0x0090:
        r4 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r9 = r9 + r4;
        if (r3 > r9) goto L_0x00a0;
    L_0x0099:
        r8.videoButtonPressed = r2;
        r8.invalidate();
        goto L_0x0140;
    L_0x00a0:
        r9 = r8.documentAttachType;
        if (r9 != r2) goto L_0x00d8;
    L_0x00a4:
        r9 = r8.photoImage;
        r9 = r9.getImageX();
        if (r0 < r9) goto L_0x00e9;
    L_0x00ac:
        r9 = r8.photoImage;
        r9 = r9.getImageX();
        r4 = r8.backgroundWidth;
        r9 = r9 + r4;
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r9 = r9 - r4;
        if (r0 > r9) goto L_0x00e9;
    L_0x00be:
        r9 = r8.photoImage;
        r9 = r9.getImageY();
        if (r3 < r9) goto L_0x00e9;
    L_0x00c6:
        r9 = r8.photoImage;
        r9 = r9.getImageY();
        r0 = r8.photoImage;
        r0 = r0.getImageHeight();
        r9 = r9 + r0;
        if (r3 > r9) goto L_0x00e9;
    L_0x00d5:
        r8.imagePressed = r2;
        goto L_0x0140;
    L_0x00d8:
        r9 = r8.currentMessageObject;
        r9 = r9.isAnyKindOfSticker();
        if (r9 == 0) goto L_0x00eb;
    L_0x00e0:
        r9 = r8.currentMessageObject;
        r9 = r9.getInputStickerSet();
        if (r9 == 0) goto L_0x00e9;
    L_0x00e8:
        goto L_0x00eb;
    L_0x00e9:
        r2 = 0;
        goto L_0x0140;
    L_0x00eb:
        r9 = r8.photoImage;
        r9 = r9.getImageX();
        if (r0 < r9) goto L_0x011c;
    L_0x00f3:
        r9 = r8.photoImage;
        r9 = r9.getImageX();
        r4 = r8.photoImage;
        r4 = r4.getImageWidth();
        r9 = r9 + r4;
        if (r0 > r9) goto L_0x011c;
    L_0x0102:
        r9 = r8.photoImage;
        r9 = r9.getImageY();
        if (r3 < r9) goto L_0x011c;
    L_0x010a:
        r9 = r8.photoImage;
        r9 = r9.getImageY();
        r0 = r8.photoImage;
        r0 = r0.getImageHeight();
        r9 = r9 + r0;
        if (r3 > r9) goto L_0x011c;
    L_0x0119:
        r8.imagePressed = r2;
        goto L_0x011d;
    L_0x011c:
        r2 = 0;
    L_0x011d:
        r9 = r8.currentMessageObject;
        r9 = r9.type;
        r0 = 12;
        if (r9 != r0) goto L_0x0140;
    L_0x0125:
        r9 = r8.currentAccount;
        r9 = org.telegram.messenger.MessagesController.getInstance(r9);
        r0 = r8.currentMessageObject;
        r0 = r0.messageOwner;
        r0 = r0.media;
        r0 = r0.user_id;
        r0 = java.lang.Integer.valueOf(r0);
        r9 = r9.getUser(r0);
        if (r9 != 0) goto L_0x0140;
    L_0x013d:
        r8.imagePressed = r1;
        goto L_0x00e9;
    L_0x0140:
        r9 = r8.imagePressed;
        if (r9 == 0) goto L_0x016c;
    L_0x0144:
        r9 = r8.currentMessageObject;
        r9 = r9.isSendError();
        if (r9 == 0) goto L_0x0150;
    L_0x014c:
        r8.imagePressed = r1;
        goto L_0x01d3;
    L_0x0150:
        r9 = r8.currentMessageObject;
        r9 = r9.type;
        r0 = 8;
        if (r9 != r0) goto L_0x016c;
    L_0x0158:
        r9 = r8.buttonState;
        if (r9 != r5) goto L_0x016c;
    L_0x015c:
        r9 = org.telegram.messenger.SharedConfig.autoplayGifs;
        if (r9 == 0) goto L_0x016c;
    L_0x0160:
        r9 = r8.photoImage;
        r9 = r9.getAnimation();
        if (r9 != 0) goto L_0x016c;
    L_0x0168:
        r8.imagePressed = r1;
        goto L_0x01d3;
    L_0x016c:
        r1 = r2;
        goto L_0x01d3;
    L_0x016e:
        r9 = r9.getAction();
        if (r9 != r2) goto L_0x01d3;
    L_0x0174:
        r9 = r8.videoButtonPressed;
        if (r9 != r2) goto L_0x0184;
    L_0x0178:
        r8.videoButtonPressed = r1;
        r8.playSoundEffect(r1);
        r8.didPressButton(r2, r2);
        r8.invalidate();
        goto L_0x01d3;
    L_0x0184:
        r9 = r8.buttonPressed;
        if (r9 != r2) goto L_0x019c;
    L_0x0188:
        r8.buttonPressed = r1;
        r8.playSoundEffect(r1);
        r9 = r8.drawVideoImageButton;
        if (r9 == 0) goto L_0x0195;
    L_0x0191:
        r8.didClickedImage();
        goto L_0x0198;
    L_0x0195:
        r8.didPressButton(r2, r1);
    L_0x0198:
        r8.invalidate();
        goto L_0x01d3;
    L_0x019c:
        r9 = r8.miniButtonPressed;
        if (r9 != r2) goto L_0x01ac;
    L_0x01a0:
        r8.miniButtonPressed = r1;
        r8.playSoundEffect(r1);
        r8.didPressMiniButton(r2);
        r8.invalidate();
        goto L_0x01d3;
    L_0x01ac:
        r9 = r8.imagePressed;
        if (r9 == 0) goto L_0x01d3;
    L_0x01b0:
        r8.imagePressed = r1;
        r9 = r8.buttonState;
        if (r9 == r5) goto L_0x01ca;
    L_0x01b6:
        r0 = 2;
        if (r9 == r0) goto L_0x01ca;
    L_0x01b9:
        r0 = 3;
        if (r9 == r0) goto L_0x01ca;
    L_0x01bc:
        r0 = r8.drawVideoImageButton;
        if (r0 == 0) goto L_0x01c1;
    L_0x01c0:
        goto L_0x01ca;
    L_0x01c1:
        if (r9 != 0) goto L_0x01d0;
    L_0x01c3:
        r8.playSoundEffect(r1);
        r8.didPressButton(r2, r1);
        goto L_0x01d0;
    L_0x01ca:
        r8.playSoundEffect(r1);
        r8.didClickedImage();
    L_0x01d0:
        r8.invalidate();
    L_0x01d3:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.checkPhotoImageMotionEvent(android.view.MotionEvent):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:75:0x011b  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x010b A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b0  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x010b A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x011b  */
    /* JADX WARNING: Missing block: B:49:0x00c5, code skipped:
            if (r3 <= (r0 + r6)) goto L_0x00c7;
     */
    /* JADX WARNING: Missing block: B:65:0x0101, code skipped:
            if (r3 <= (r0 + r6)) goto L_0x00c7;
     */
    private boolean checkAudioMotionEvent(android.view.MotionEvent r13) {
        /*
        r12 = this;
        r0 = r12.documentAttachType;
        r1 = 3;
        r2 = 0;
        if (r0 == r1) goto L_0x000a;
    L_0x0006:
        r3 = 5;
        if (r0 == r3) goto L_0x000a;
    L_0x0009:
        return r2;
    L_0x000a:
        r0 = r13.getX();
        r0 = (int) r0;
        r3 = r13.getY();
        r3 = (int) r3;
        r4 = r12.useSeekBarWaweform;
        if (r4 == 0) goto L_0x003b;
    L_0x0018:
        r4 = r12.seekBarWaveform;
        r5 = r13.getAction();
        r6 = r13.getX();
        r7 = r12.seekBarX;
        r7 = (float) r7;
        r6 = r6 - r7;
        r7 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r7 = (float) r7;
        r6 = r6 - r7;
        r7 = r13.getY();
        r8 = r12.seekBarY;
        r8 = (float) r8;
        r7 = r7 - r8;
        r4 = r4.onTouch(r5, r6, r7);
        goto L_0x0055;
    L_0x003b:
        r4 = r12.seekBar;
        r5 = r13.getAction();
        r6 = r13.getX();
        r7 = r12.seekBarX;
        r7 = (float) r7;
        r6 = r6 - r7;
        r7 = r13.getY();
        r8 = r12.seekBarY;
        r8 = (float) r8;
        r7 = r7 - r8;
        r4 = r4.onTouch(r5, r6, r7);
    L_0x0055:
        r5 = 1;
        if (r4 == 0) goto L_0x0086;
    L_0x0058:
        r0 = r12.useSeekBarWaweform;
        if (r0 != 0) goto L_0x006a;
    L_0x005c:
        r0 = r13.getAction();
        if (r0 != 0) goto L_0x006a;
    L_0x0062:
        r13 = r12.getParent();
        r13.requestDisallowInterceptTouchEvent(r5);
        goto L_0x007f;
    L_0x006a:
        r0 = r12.useSeekBarWaweform;
        if (r0 == 0) goto L_0x007f;
    L_0x006e:
        r0 = r12.seekBarWaveform;
        r0 = r0.isStartDraging();
        if (r0 != 0) goto L_0x007f;
    L_0x0076:
        r13 = r13.getAction();
        if (r13 != r5) goto L_0x007f;
    L_0x007c:
        r12.didPressButton(r5, r2);
    L_0x007f:
        r12.disallowLongPress = r5;
        r12.invalidate();
        goto L_0x017a;
    L_0x0086:
        r6 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r7 = r12.miniButtonState;
        if (r7 < 0) goto L_0x00ac;
    L_0x0090:
        r7 = 1104674816; // 0x41d80000 float:27.0 double:5.457818764E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r8 = r12.buttonX;
        r9 = r8 + r7;
        if (r0 < r9) goto L_0x00ac;
    L_0x009c:
        r8 = r8 + r7;
        r8 = r8 + r6;
        if (r0 > r8) goto L_0x00ac;
    L_0x00a0:
        r8 = r12.buttonY;
        r9 = r8 + r7;
        if (r3 < r9) goto L_0x00ac;
    L_0x00a6:
        r8 = r8 + r7;
        r8 = r8 + r6;
        if (r3 > r8) goto L_0x00ac;
    L_0x00aa:
        r7 = 1;
        goto L_0x00ad;
    L_0x00ac:
        r7 = 0;
    L_0x00ad:
        r8 = 2;
        if (r7 != 0) goto L_0x0104;
    L_0x00b0:
        r9 = r12.buttonState;
        if (r9 == 0) goto L_0x00c9;
    L_0x00b4:
        if (r9 == r5) goto L_0x00c9;
    L_0x00b6:
        if (r9 != r8) goto L_0x00b9;
    L_0x00b8:
        goto L_0x00c9;
    L_0x00b9:
        r9 = r12.buttonX;
        if (r0 < r9) goto L_0x0104;
    L_0x00bd:
        r9 = r9 + r6;
        if (r0 > r9) goto L_0x0104;
    L_0x00c0:
        r0 = r12.buttonY;
        if (r3 < r0) goto L_0x0104;
    L_0x00c4:
        r0 = r0 + r6;
        if (r3 > r0) goto L_0x0104;
    L_0x00c7:
        r0 = 1;
        goto L_0x0105;
    L_0x00c9:
        r9 = r12.buttonX;
        r10 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r9 = r9 - r11;
        if (r0 < r9) goto L_0x0104;
    L_0x00d4:
        r9 = r12.buttonX;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r9 = r9 - r10;
        r10 = r12.backgroundWidth;
        r9 = r9 + r10;
        if (r0 > r9) goto L_0x0104;
    L_0x00e0:
        r0 = r12.drawInstantView;
        if (r0 == 0) goto L_0x00e7;
    L_0x00e4:
        r0 = r12.buttonY;
        goto L_0x00ec;
    L_0x00e7:
        r0 = r12.namesOffset;
        r9 = r12.mediaOffsetY;
        r0 = r0 + r9;
    L_0x00ec:
        if (r3 < r0) goto L_0x0104;
    L_0x00ee:
        r0 = r12.drawInstantView;
        if (r0 == 0) goto L_0x00f5;
    L_0x00f2:
        r0 = r12.buttonY;
        goto L_0x0100;
    L_0x00f5:
        r0 = r12.namesOffset;
        r6 = r12.mediaOffsetY;
        r0 = r0 + r6;
        r6 = 1118044160; // 0x42a40000 float:82.0 double:5.5238721E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
    L_0x0100:
        r0 = r0 + r6;
        if (r3 > r0) goto L_0x0104;
    L_0x0103:
        goto L_0x00c7;
    L_0x0104:
        r0 = 0;
    L_0x0105:
        r3 = r13.getAction();
        if (r3 != 0) goto L_0x011b;
    L_0x010b:
        if (r0 != 0) goto L_0x010f;
    L_0x010d:
        if (r7 == 0) goto L_0x017a;
    L_0x010f:
        if (r0 == 0) goto L_0x0114;
    L_0x0111:
        r12.buttonPressed = r5;
        goto L_0x0116;
    L_0x0114:
        r12.miniButtonPressed = r5;
    L_0x0116:
        r12.invalidate();
        r4 = 1;
        goto L_0x017a;
    L_0x011b:
        r3 = r12.buttonPressed;
        if (r3 == 0) goto L_0x014b;
    L_0x011f:
        r3 = r13.getAction();
        if (r3 != r5) goto L_0x0131;
    L_0x0125:
        r12.buttonPressed = r2;
        r12.playSoundEffect(r2);
        r12.didPressButton(r5, r2);
        r12.invalidate();
        goto L_0x017a;
    L_0x0131:
        r3 = r13.getAction();
        if (r3 != r1) goto L_0x013d;
    L_0x0137:
        r12.buttonPressed = r2;
        r12.invalidate();
        goto L_0x017a;
    L_0x013d:
        r13 = r13.getAction();
        if (r13 != r8) goto L_0x017a;
    L_0x0143:
        if (r0 != 0) goto L_0x017a;
    L_0x0145:
        r12.buttonPressed = r2;
        r12.invalidate();
        goto L_0x017a;
    L_0x014b:
        r0 = r12.miniButtonPressed;
        if (r0 == 0) goto L_0x017a;
    L_0x014f:
        r0 = r13.getAction();
        if (r0 != r5) goto L_0x0161;
    L_0x0155:
        r12.miniButtonPressed = r2;
        r12.playSoundEffect(r2);
        r12.didPressMiniButton(r5);
        r12.invalidate();
        goto L_0x017a;
    L_0x0161:
        r0 = r13.getAction();
        if (r0 != r1) goto L_0x016d;
    L_0x0167:
        r12.miniButtonPressed = r2;
        r12.invalidate();
        goto L_0x017a;
    L_0x016d:
        r13 = r13.getAction();
        if (r13 != r8) goto L_0x017a;
    L_0x0173:
        if (r7 != 0) goto L_0x017a;
    L_0x0175:
        r12.miniButtonPressed = r2;
        r12.invalidate();
    L_0x017a:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.checkAudioMotionEvent(android.view.MotionEvent):boolean");
    }

    private boolean checkBotButtonMotionEvent(MotionEvent motionEvent) {
        if (this.botButtons.isEmpty() || this.currentMessageObject.eventId != 0) {
            return false;
        }
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (motionEvent.getAction() == 0) {
            int measuredWidth;
            if (this.currentMessageObject.isOutOwner()) {
                measuredWidth = (getMeasuredWidth() - this.widthForButtons) - AndroidUtilities.m26dp(10.0f);
            } else {
                measuredWidth = this.backgroundDrawableLeft + AndroidUtilities.m26dp(this.mediaBackground ? 1.0f : 7.0f);
            }
            int i = 0;
            while (i < this.botButtons.size()) {
                BotButton botButton = (BotButton) this.botButtons.get(i);
                int access$1000 = (botButton.f576y + this.layoutHeight) - AndroidUtilities.m26dp(2.0f);
                if (x < botButton.f575x + measuredWidth || x > (botButton.f575x + measuredWidth) + botButton.width || y < access$1000 || y > access$1000 + botButton.height) {
                    i++;
                } else {
                    this.pressedBotButton = i;
                    invalidate();
                    return true;
                }
            }
            return false;
        } else if (motionEvent.getAction() != 1 || this.pressedBotButton == -1) {
            return false;
        } else {
            playSoundEffect(0);
            this.delegate.didPressBotButton(this, ((BotButton) this.botButtons.get(this.pressedBotButton)).button);
            this.pressedBotButton = -1;
            invalidate();
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:116:0x01d0  */
    /* JADX WARNING: Missing block: B:172:0x02b0, code skipped:
            if (r4 <= ((float) (r1 + org.telegram.messenger.AndroidUtilities.m26dp(32.0f)))) goto L_0x03fc;
     */
    /* JADX WARNING: Missing block: B:201:0x0313, code skipped:
            if (r4 <= ((float) (r1 + org.telegram.messenger.AndroidUtilities.m26dp(32.0f)))) goto L_0x03fc;
     */
    /* JADX WARNING: Missing block: B:235:0x03aa, code skipped:
            if (r4 <= ((float) (r1 + org.telegram.messenger.AndroidUtilities.m26dp(35.0f)))) goto L_0x03fc;
     */
    /* JADX WARNING: Missing block: B:256:0x03f5, code skipped:
            if (r4 <= ((float) (r1 + org.telegram.messenger.AndroidUtilities.m26dp(32.0f)))) goto L_0x03f9;
     */
    public boolean onTouchEvent(android.view.MotionEvent r14) {
        /*
        r13 = this;
        r0 = r13.currentMessageObject;
        if (r0 == 0) goto L_0x03fd;
    L_0x0004:
        r0 = r13.delegate;
        r0 = r0.canPerformActions();
        if (r0 != 0) goto L_0x000e;
    L_0x000c:
        goto L_0x03fd;
    L_0x000e:
        r0 = 0;
        r13.disallowLongPress = r0;
        r1 = r14.getX();
        r13.lastTouchX = r1;
        r1 = r14.getY();
        r13.lastTouchX = r1;
        r1 = r13.checkTextBlockMotionEvent(r14);
        if (r1 != 0) goto L_0x0027;
    L_0x0023:
        r1 = r13.checkOtherButtonMotionEvent(r14);
    L_0x0027:
        if (r1 != 0) goto L_0x002d;
    L_0x0029:
        r1 = r13.checkCaptionMotionEvent(r14);
    L_0x002d:
        if (r1 != 0) goto L_0x0033;
    L_0x002f:
        r1 = r13.checkAudioMotionEvent(r14);
    L_0x0033:
        if (r1 != 0) goto L_0x0039;
    L_0x0035:
        r1 = r13.checkLinkPreviewMotionEvent(r14);
    L_0x0039:
        if (r1 != 0) goto L_0x003f;
    L_0x003b:
        r1 = r13.checkInstantButtonMotionEvent(r14);
    L_0x003f:
        if (r1 != 0) goto L_0x0045;
    L_0x0041:
        r1 = r13.checkGameMotionEvent(r14);
    L_0x0045:
        if (r1 != 0) goto L_0x004b;
    L_0x0047:
        r1 = r13.checkPhotoImageMotionEvent(r14);
    L_0x004b:
        if (r1 != 0) goto L_0x0051;
    L_0x004d:
        r1 = r13.checkBotButtonMotionEvent(r14);
    L_0x0051:
        if (r1 != 0) goto L_0x0057;
    L_0x0053:
        r1 = r13.checkPollButtonMotionEvent(r14);
    L_0x0057:
        r2 = r14.getAction();
        r3 = 3;
        if (r2 != r3) goto L_0x0089;
    L_0x005e:
        r13.buttonPressed = r0;
        r13.miniButtonPressed = r0;
        r1 = -1;
        r13.pressedBotButton = r1;
        r13.pressedVoteButton = r1;
        r13.linkPreviewPressed = r0;
        r13.otherPressed = r0;
        r13.sharePressed = r0;
        r13.imagePressed = r0;
        r13.gamePreviewPressed = r0;
        r13.instantButtonPressed = r0;
        r13.instantPressed = r0;
        r2 = android.os.Build.VERSION.SDK_INT;
        r4 = 21;
        if (r2 < r4) goto L_0x0084;
    L_0x007b:
        r2 = r13.selectorDrawable;
        if (r2 == 0) goto L_0x0084;
    L_0x007f:
        r4 = android.util.StateSet.NOTHING;
        r2.setState(r4);
    L_0x0084:
        r13.resetPressedLink(r1);
        r6 = 0;
        goto L_0x008a;
    L_0x0089:
        r6 = r1;
    L_0x008a:
        r13.updateRadialProgressBackground();
        r1 = r13.disallowLongPress;
        if (r1 != 0) goto L_0x009c;
    L_0x0091:
        if (r6 == 0) goto L_0x009c;
    L_0x0093:
        r1 = r14.getAction();
        if (r1 != 0) goto L_0x009c;
    L_0x0099:
        r13.startCheckLongPress();
    L_0x009c:
        r1 = r14.getAction();
        r2 = 2;
        if (r1 == 0) goto L_0x00ac;
    L_0x00a3:
        r1 = r14.getAction();
        if (r1 == r2) goto L_0x00ac;
    L_0x00a9:
        r13.cancelCheckLongPress();
    L_0x00ac:
        if (r6 != 0) goto L_0x03fc;
    L_0x00ae:
        r1 = r14.getX();
        r4 = r14.getY();
        r5 = r14.getAction();
        r7 = 1108082688; // 0x420c0000 float:35.0 double:5.47465589E-315;
        r8 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r9 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
        r10 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r11 = 1107296256; // 0x42000000 float:32.0 double:5.4707704E-315;
        r12 = 1;
        if (r5 != 0) goto L_0x01d5;
    L_0x00c7:
        r2 = r13.delegate;
        if (r2 == 0) goto L_0x00d1;
    L_0x00cb:
        r2 = r2.canPerformActions();
        if (r2 == 0) goto L_0x03fc;
    L_0x00d1:
        r2 = r13.isAvatarVisible;
        if (r2 == 0) goto L_0x00e8;
    L_0x00d5:
        r2 = r13.avatarImage;
        r3 = r13.getTop();
        r3 = (float) r3;
        r3 = r3 + r4;
        r2 = r2.isInsideImage(r1, r3);
        if (r2 == 0) goto L_0x00e8;
    L_0x00e3:
        r13.avatarPressed = r12;
    L_0x00e5:
        r6 = 1;
        goto L_0x01ce;
    L_0x00e8:
        r2 = r13.drawForwardedName;
        if (r2 == 0) goto L_0x012b;
    L_0x00ec:
        r2 = r13.forwardedNameLayout;
        r0 = r2[r0];
        if (r0 == 0) goto L_0x012b;
    L_0x00f2:
        r0 = r13.forwardNameX;
        r2 = (float) r0;
        r2 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r2 < 0) goto L_0x012b;
    L_0x00f9:
        r2 = r13.forwardedNameWidth;
        r0 = r0 + r2;
        r0 = (float) r0;
        r0 = (r1 > r0 ? 1 : (r1 == r0 ? 0 : -1));
        if (r0 > 0) goto L_0x012b;
    L_0x0101:
        r0 = r13.forwardNameY;
        r2 = (float) r0;
        r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1));
        if (r2 < 0) goto L_0x012b;
    L_0x0108:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r0 = r0 + r2;
        r0 = (float) r0;
        r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r0 > 0) goto L_0x012b;
    L_0x0112:
        r0 = r13.viaWidth;
        if (r0 == 0) goto L_0x0128;
    L_0x0116:
        r0 = r13.forwardNameX;
        r2 = r13.viaNameWidth;
        r0 = r0 + r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r0 = r0 + r2;
        r0 = (float) r0;
        r0 = (r1 > r0 ? 1 : (r1 == r0 ? 0 : -1));
        if (r0 < 0) goto L_0x0128;
    L_0x0125:
        r13.forwardBotPressed = r12;
        goto L_0x00e5;
    L_0x0128:
        r13.forwardNamePressed = r12;
        goto L_0x00e5;
    L_0x012b:
        r0 = r13.drawNameLayout;
        if (r0 == 0) goto L_0x0164;
    L_0x012f:
        r0 = r13.nameLayout;
        if (r0 == 0) goto L_0x0164;
    L_0x0133:
        r0 = r13.viaWidth;
        if (r0 == 0) goto L_0x0164;
    L_0x0137:
        r2 = r13.nameX;
        r3 = r13.viaNameWidth;
        r5 = (float) r3;
        r5 = r5 + r2;
        r5 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1));
        if (r5 < 0) goto L_0x0164;
    L_0x0141:
        r3 = (float) r3;
        r2 = r2 + r3;
        r0 = (float) r0;
        r2 = r2 + r0;
        r0 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r0 > 0) goto L_0x0164;
    L_0x0149:
        r0 = r13.nameY;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r2 = (float) r2;
        r0 = r0 - r2;
        r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r0 < 0) goto L_0x0164;
    L_0x0155:
        r0 = r13.nameY;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r2 = (float) r2;
        r0 = r0 + r2;
        r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r0 > 0) goto L_0x0164;
    L_0x0161:
        r13.forwardBotPressed = r12;
        goto L_0x00e5;
    L_0x0164:
        r0 = r13.drawShareButton;
        if (r0 == 0) goto L_0x0191;
    L_0x0168:
        r0 = r13.shareStartX;
        r2 = (float) r0;
        r2 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r2 < 0) goto L_0x0191;
    L_0x016f:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r0 = r0 + r2;
        r0 = (float) r0;
        r0 = (r1 > r0 ? 1 : (r1 == r0 ? 0 : -1));
        if (r0 > 0) goto L_0x0191;
    L_0x0179:
        r0 = r13.shareStartY;
        r2 = (float) r0;
        r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1));
        if (r2 < 0) goto L_0x0191;
    L_0x0180:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r0 = r0 + r2;
        r0 = (float) r0;
        r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r0 > 0) goto L_0x0191;
    L_0x018a:
        r13.sharePressed = r12;
        r13.invalidate();
        goto L_0x00e5;
    L_0x0191:
        r0 = r13.replyNameLayout;
        if (r0 == 0) goto L_0x01ce;
    L_0x0195:
        r0 = r13.currentMessageObject;
        r0 = r0.shouldDrawWithoutBackground();
        if (r0 == 0) goto L_0x01a8;
    L_0x019d:
        r0 = r13.replyStartX;
        r2 = r13.replyNameWidth;
        r3 = r13.replyTextWidth;
        r2 = java.lang.Math.max(r2, r3);
        goto L_0x01ac;
    L_0x01a8:
        r0 = r13.replyStartX;
        r2 = r13.backgroundDrawableRight;
    L_0x01ac:
        r0 = r0 + r2;
        r2 = r13.replyStartX;
        r2 = (float) r2;
        r2 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r2 < 0) goto L_0x01ce;
    L_0x01b4:
        r0 = (float) r0;
        r0 = (r1 > r0 ? 1 : (r1 == r0 ? 0 : -1));
        if (r0 > 0) goto L_0x01ce;
    L_0x01b9:
        r0 = r13.replyStartY;
        r1 = (float) r0;
        r1 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1));
        if (r1 < 0) goto L_0x01ce;
    L_0x01c0:
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r0 = r0 + r1;
        r0 = (float) r0;
        r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r0 > 0) goto L_0x01ce;
    L_0x01ca:
        r13.replyPressed = r12;
        goto L_0x00e5;
    L_0x01ce:
        if (r6 == 0) goto L_0x03fc;
    L_0x01d0:
        r13.startCheckLongPress();
        goto L_0x03fc;
    L_0x01d5:
        r5 = r14.getAction();
        if (r5 == r2) goto L_0x01de;
    L_0x01db:
        r13.cancelCheckLongPress();
    L_0x01de:
        r5 = r13.avatarPressed;
        if (r5 == 0) goto L_0x0243;
    L_0x01e2:
        r5 = r14.getAction();
        if (r5 != r12) goto L_0x021d;
    L_0x01e8:
        r13.avatarPressed = r0;
        r13.playSoundEffect(r0);
        r0 = r13.delegate;
        if (r0 == 0) goto L_0x03fc;
    L_0x01f1:
        r1 = r13.currentUser;
        if (r1 == 0) goto L_0x0207;
    L_0x01f5:
        r2 = r1.f534id;
        if (r2 != 0) goto L_0x01fe;
    L_0x01f9:
        r0.didPressHiddenForward(r13);
        goto L_0x03fc;
    L_0x01fe:
        r2 = r13.lastTouchX;
        r3 = r13.lastTouchY;
        r0.didPressUserAvatar(r13, r1, r2, r3);
        goto L_0x03fc;
    L_0x0207:
        r2 = r13.currentChat;
        if (r2 == 0) goto L_0x03fc;
    L_0x020b:
        r1 = r13.currentMessageObject;
        r1 = r1.messageOwner;
        r1 = r1.fwd_from;
        r3 = r1.channel_post;
        r4 = r13.lastTouchX;
        r5 = r13.lastTouchY;
        r1 = r13;
        r0.didPressChannelAvatar(r1, r2, r3, r4, r5);
        goto L_0x03fc;
    L_0x021d:
        r5 = r14.getAction();
        if (r5 != r3) goto L_0x0227;
    L_0x0223:
        r13.avatarPressed = r0;
        goto L_0x03fc;
    L_0x0227:
        r3 = r14.getAction();
        if (r3 != r2) goto L_0x03fc;
    L_0x022d:
        r2 = r13.isAvatarVisible;
        if (r2 == 0) goto L_0x03fc;
    L_0x0231:
        r2 = r13.avatarImage;
        r3 = r13.getTop();
        r3 = (float) r3;
        r4 = r4 + r3;
        r1 = r2.isInsideImage(r1, r4);
        if (r1 != 0) goto L_0x03fc;
    L_0x023f:
        r13.avatarPressed = r0;
        goto L_0x03fc;
    L_0x0243:
        r5 = r13.forwardNamePressed;
        if (r5 == 0) goto L_0x02b6;
    L_0x0247:
        r5 = r14.getAction();
        if (r5 != r12) goto L_0x0282;
    L_0x024d:
        r13.forwardNamePressed = r0;
        r13.playSoundEffect(r0);
        r0 = r13.delegate;
        if (r0 == 0) goto L_0x03fc;
    L_0x0256:
        r2 = r13.currentForwardChannel;
        if (r2 == 0) goto L_0x026c;
    L_0x025a:
        r1 = r13.currentMessageObject;
        r1 = r1.messageOwner;
        r1 = r1.fwd_from;
        r3 = r1.channel_post;
        r4 = r13.lastTouchX;
        r5 = r13.lastTouchY;
        r1 = r13;
        r0.didPressChannelAvatar(r1, r2, r3, r4, r5);
        goto L_0x03fc;
    L_0x026c:
        r1 = r13.currentForwardUser;
        if (r1 == 0) goto L_0x0279;
    L_0x0270:
        r2 = r13.lastTouchX;
        r3 = r13.lastTouchY;
        r0.didPressUserAvatar(r13, r1, r2, r3);
        goto L_0x03fc;
    L_0x0279:
        r1 = r13.currentForwardName;
        if (r1 == 0) goto L_0x03fc;
    L_0x027d:
        r0.didPressHiddenForward(r13);
        goto L_0x03fc;
    L_0x0282:
        r5 = r14.getAction();
        if (r5 != r3) goto L_0x028c;
    L_0x0288:
        r13.forwardNamePressed = r0;
        goto L_0x03fc;
    L_0x028c:
        r3 = r14.getAction();
        if (r3 != r2) goto L_0x03fc;
    L_0x0292:
        r2 = r13.forwardNameX;
        r3 = (float) r2;
        r3 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r3 < 0) goto L_0x02b2;
    L_0x0299:
        r3 = r13.forwardedNameWidth;
        r2 = r2 + r3;
        r2 = (float) r2;
        r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r1 > 0) goto L_0x02b2;
    L_0x02a1:
        r1 = r13.forwardNameY;
        r2 = (float) r1;
        r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1));
        if (r2 < 0) goto L_0x02b2;
    L_0x02a8:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r1 = r1 + r2;
        r1 = (float) r1;
        r1 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1));
        if (r1 <= 0) goto L_0x03fc;
    L_0x02b2:
        r13.forwardNamePressed = r0;
        goto L_0x03fc;
    L_0x02b6:
        r5 = r13.forwardBotPressed;
        if (r5 == 0) goto L_0x0349;
    L_0x02ba:
        r5 = r14.getAction();
        if (r5 != r12) goto L_0x02db;
    L_0x02c0:
        r13.forwardBotPressed = r0;
        r13.playSoundEffect(r0);
        r0 = r13.delegate;
        if (r0 == 0) goto L_0x03fc;
    L_0x02c9:
        r1 = r13.currentViaBotUser;
        if (r1 == 0) goto L_0x02d0;
    L_0x02cd:
        r1 = r1.username;
        goto L_0x02d6;
    L_0x02d0:
        r1 = r13.currentMessageObject;
        r1 = r1.messageOwner;
        r1 = r1.via_bot_name;
    L_0x02d6:
        r0.didPressViaBot(r13, r1);
        goto L_0x03fc;
    L_0x02db:
        r5 = r14.getAction();
        if (r5 != r3) goto L_0x02e5;
    L_0x02e1:
        r13.forwardBotPressed = r0;
        goto L_0x03fc;
    L_0x02e5:
        r3 = r14.getAction();
        if (r3 != r2) goto L_0x03fc;
    L_0x02eb:
        r2 = r13.drawForwardedName;
        if (r2 == 0) goto L_0x0319;
    L_0x02ef:
        r2 = r13.forwardedNameLayout;
        r2 = r2[r0];
        if (r2 == 0) goto L_0x0319;
    L_0x02f5:
        r2 = r13.forwardNameX;
        r3 = (float) r2;
        r3 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r3 < 0) goto L_0x0315;
    L_0x02fc:
        r3 = r13.forwardedNameWidth;
        r2 = r2 + r3;
        r2 = (float) r2;
        r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r1 > 0) goto L_0x0315;
    L_0x0304:
        r1 = r13.forwardNameY;
        r2 = (float) r1;
        r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1));
        if (r2 < 0) goto L_0x0315;
    L_0x030b:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r1 = r1 + r2;
        r1 = (float) r1;
        r1 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1));
        if (r1 <= 0) goto L_0x03fc;
    L_0x0315:
        r13.forwardBotPressed = r0;
        goto L_0x03fc;
    L_0x0319:
        r2 = r13.nameX;
        r3 = r13.viaNameWidth;
        r5 = (float) r3;
        r5 = r5 + r2;
        r5 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1));
        if (r5 < 0) goto L_0x0345;
    L_0x0323:
        r3 = (float) r3;
        r2 = r2 + r3;
        r3 = r13.viaWidth;
        r3 = (float) r3;
        r2 = r2 + r3;
        r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r1 > 0) goto L_0x0345;
    L_0x032d:
        r1 = r13.nameY;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r2 = (float) r2;
        r1 = r1 - r2;
        r1 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1));
        if (r1 < 0) goto L_0x0345;
    L_0x0339:
        r1 = r13.nameY;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r2 = (float) r2;
        r1 = r1 + r2;
        r1 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1));
        if (r1 <= 0) goto L_0x03fc;
    L_0x0345:
        r13.forwardBotPressed = r0;
        goto L_0x03fc;
    L_0x0349:
        r5 = r13.replyPressed;
        if (r5 == 0) goto L_0x03af;
    L_0x034d:
        r5 = r14.getAction();
        if (r5 != r12) goto L_0x0367;
    L_0x0353:
        r13.replyPressed = r0;
        r13.playSoundEffect(r0);
        r0 = r13.delegate;
        if (r0 == 0) goto L_0x03fc;
    L_0x035c:
        r1 = r13.currentMessageObject;
        r1 = r1.messageOwner;
        r1 = r1.reply_to_msg_id;
        r0.didPressReplyMessage(r13, r1);
        goto L_0x03fc;
    L_0x0367:
        r5 = r14.getAction();
        if (r5 != r3) goto L_0x0371;
    L_0x036d:
        r13.replyPressed = r0;
        goto L_0x03fc;
    L_0x0371:
        r3 = r14.getAction();
        if (r3 != r2) goto L_0x03fc;
    L_0x0377:
        r2 = r13.currentMessageObject;
        r2 = r2.shouldDrawWithoutBackground();
        if (r2 == 0) goto L_0x038a;
    L_0x037f:
        r2 = r13.replyStartX;
        r3 = r13.replyNameWidth;
        r5 = r13.replyTextWidth;
        r3 = java.lang.Math.max(r3, r5);
        goto L_0x038e;
    L_0x038a:
        r2 = r13.replyStartX;
        r3 = r13.backgroundDrawableRight;
    L_0x038e:
        r2 = r2 + r3;
        r3 = r13.replyStartX;
        r3 = (float) r3;
        r3 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r3 < 0) goto L_0x03ac;
    L_0x0396:
        r2 = (float) r2;
        r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r1 > 0) goto L_0x03ac;
    L_0x039b:
        r1 = r13.replyStartY;
        r2 = (float) r1;
        r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1));
        if (r2 < 0) goto L_0x03ac;
    L_0x03a2:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r1 = r1 + r2;
        r1 = (float) r1;
        r1 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1));
        if (r1 <= 0) goto L_0x03fc;
    L_0x03ac:
        r13.replyPressed = r0;
        goto L_0x03fc;
    L_0x03af:
        r5 = r13.sharePressed;
        if (r5 == 0) goto L_0x03fc;
    L_0x03b3:
        r5 = r14.getAction();
        if (r5 != r12) goto L_0x03c6;
    L_0x03b9:
        r13.sharePressed = r0;
        r13.playSoundEffect(r0);
        r0 = r13.delegate;
        if (r0 == 0) goto L_0x03f9;
    L_0x03c2:
        r0.didPressShare(r13);
        goto L_0x03f9;
    L_0x03c6:
        r5 = r14.getAction();
        if (r5 != r3) goto L_0x03cf;
    L_0x03cc:
        r13.sharePressed = r0;
        goto L_0x03f9;
    L_0x03cf:
        r3 = r14.getAction();
        if (r3 != r2) goto L_0x03f9;
    L_0x03d5:
        r2 = r13.shareStartX;
        r3 = (float) r2;
        r3 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r3 < 0) goto L_0x03f7;
    L_0x03dc:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r2 = r2 + r3;
        r2 = (float) r2;
        r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r1 > 0) goto L_0x03f7;
    L_0x03e6:
        r1 = r13.shareStartY;
        r2 = (float) r1;
        r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1));
        if (r2 < 0) goto L_0x03f7;
    L_0x03ed:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r1 = r1 + r2;
        r1 = (float) r1;
        r1 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1));
        if (r1 <= 0) goto L_0x03f9;
    L_0x03f7:
        r13.sharePressed = r0;
    L_0x03f9:
        r13.invalidate();
    L_0x03fc:
        return r6;
    L_0x03fd:
        r0 = super.onTouchEvent(r14);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void updatePlayingMessageProgress() {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            int i;
            int i2;
            int i3;
            if (this.documentAttachType != 4) {
                String str = "%02d:%02d";
                String format;
                if (messageObject.isRoundVideo()) {
                    Document document = this.currentMessageObject.getDocument();
                    for (i = 0; i < document.attributes.size(); i++) {
                        DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(i);
                        if (documentAttribute instanceof TL_documentAttributeVideo) {
                            i2 = documentAttribute.duration;
                            break;
                        }
                    }
                    i2 = 0;
                    if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                        i2 = Math.max(0, i2 - this.currentMessageObject.audioProgressSec);
                    }
                    if (this.lastTime != i2) {
                        this.lastTime = i2;
                        format = String.format(str, new Object[]{Integer.valueOf(i2 / 60), Integer.valueOf(i2 % 60)});
                        this.timeWidthAudio = (int) Math.ceil((double) Theme.chat_timePaint.measureText(format));
                        this.durationLayout = new StaticLayout(format, Theme.chat_timePaint, this.timeWidthAudio, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        invalidate();
                    }
                } else if (this.documentAttach != null) {
                    if (this.useSeekBarWaweform) {
                        if (!this.seekBarWaveform.isDragging()) {
                            this.seekBarWaveform.setProgress(this.currentMessageObject.audioProgress);
                        }
                    } else if (!this.seekBar.isDragging()) {
                        this.seekBar.setProgress(this.currentMessageObject.audioProgress);
                        this.seekBar.setBufferedProgress(this.currentMessageObject.bufferedProgress);
                    }
                    if (this.documentAttachType == 3) {
                        if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                            i2 = this.currentMessageObject.audioProgressSec;
                        } else {
                            for (i2 = 0; i2 < this.documentAttach.attributes.size(); i2++) {
                                DocumentAttribute documentAttribute2 = (DocumentAttribute) this.documentAttach.attributes.get(i2);
                                if (documentAttribute2 instanceof TL_documentAttributeAudio) {
                                    i2 = documentAttribute2.duration;
                                    break;
                                }
                            }
                            i2 = 0;
                        }
                        if (this.lastTime != i2) {
                            this.lastTime = i2;
                            format = String.format(str, new Object[]{Integer.valueOf(i2 / 60), Integer.valueOf(i2 % 60)});
                            this.timeWidthAudio = (int) Math.ceil((double) Theme.chat_audioTimePaint.measureText(format));
                            this.durationLayout = new StaticLayout(format, Theme.chat_audioTimePaint, this.timeWidthAudio, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        }
                    } else {
                        i2 = this.currentMessageObject.getDuration();
                        i3 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject) ? this.currentMessageObject.audioProgressSec : 0;
                        if (this.lastTime != i3) {
                            String format2;
                            this.lastTime = i3;
                            if (i2 == 0) {
                                format2 = String.format("%d:%02d / -:--", new Object[]{Integer.valueOf(i3 / 60), Integer.valueOf(i3 % 60)});
                            } else {
                                format2 = String.format("%d:%02d / %d:%02d", new Object[]{Integer.valueOf(i3 / 60), Integer.valueOf(i3 % 60), Integer.valueOf(i2 / 60), Integer.valueOf(i2 % 60)});
                            }
                            String str2 = format2;
                            this.durationLayout = new StaticLayout(str2, Theme.chat_audioTimePaint, (int) Math.ceil((double) Theme.chat_audioTimePaint.measureText(str2)), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        }
                    }
                    invalidate();
                }
            } else if (!PhotoViewer.isPlayingMessage(messageObject) && !MediaController.getInstance().isGoingToShowMessageObject(this.currentMessageObject)) {
                AnimatedFileDrawable animation = this.photoImage.getAnimation();
                if (animation != null) {
                    i = animation.getDurationMs() / 1000;
                    this.currentMessageObject.audioPlayerDuration = i;
                    MessageObject messageObject2 = this.currentMessageObject;
                    Message message = messageObject2.messageOwner;
                    if (message.ttl > 0 && message.destroyTime == 0 && !messageObject2.needDrawBluredPreview() && this.currentMessageObject.isVideo() && animation.hasBitmap()) {
                        this.delegate.didStartVideoStream(this.currentMessageObject);
                    }
                } else {
                    i = 0;
                }
                if (i == 0) {
                    i = this.currentMessageObject.getDuration();
                }
                if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                    float f = (float) i;
                    i = (int) (f - (this.currentMessageObject.audioProgress * f));
                } else if (animation != null) {
                    if (i != 0) {
                        i -= animation.getCurrentProgressMs() / 1000;
                    }
                    if (this.delegate != null && animation.getCurrentProgressMs() >= 3000) {
                        this.delegate.videoTimerReached();
                    }
                }
                i3 = i - ((i / 60) * 60);
                if (this.lastTime != i) {
                    String format3 = String.format("%d:%02d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3)});
                    this.infoWidth = (int) Math.ceil((double) Theme.chat_infoPaint.measureText(format3));
                    this.infoLayout = new StaticLayout(format3, Theme.chat_infoPaint, this.infoWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.lastTime = i;
                }
            }
        }
    }

    public void setFullyDraw(boolean z) {
        this.fullyDraw = z;
    }

    public void setVisiblePart(int i, int i2) {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && messageObject.textLayoutBlocks != null) {
            i -= this.textY;
            int i3 = 0;
            int i4 = 0;
            while (i3 < this.currentMessageObject.textLayoutBlocks.size() && ((TextLayoutBlock) this.currentMessageObject.textLayoutBlocks.get(i3)).textYOffset <= ((float) i)) {
                i4 = i3;
                i3++;
            }
            int i5 = -1;
            int i6 = -1;
            int i7 = 0;
            for (i4 = 
/*
Method generation error in method: org.telegram.ui.Cells.ChatMessageCell.setVisiblePart(int, int):void, dex: classes.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r2_1 'i4' int) = (r2_0 'i4' int), (r2_3 'i4' int) binds: {(r2_0 'i4' int)=B:4:0x000a, (r2_3 'i4' int)=B:9:0x002c} in method: org.telegram.ui.Cells.ChatMessageCell.setVisiblePart(int, int):void, dex: classes.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:228)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:185)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:120)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:183)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:321)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:259)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:221)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:111)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:77)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:10)
	at jadx.core.ProcessClass.process(ProcessClass.java:38)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:539)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:511)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:222)
	... 22 more

*/

    public static StaticLayout generateStaticLayout(CharSequence charSequence, TextPaint textPaint, int i, int i2, int i3, int i4) {
        CharSequence charSequence2 = charSequence;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
        StaticLayout staticLayout = new StaticLayout(charSequence, textPaint, i2, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        int i5 = i;
        int i6 = i3;
        int i7 = 0;
        int i8 = 0;
        while (i7 < i6) {
            staticLayout.getLineDirections(i7);
            if (staticLayout.getLineLeft(i7) != 0.0f || staticLayout.isRtlCharAt(staticLayout.getLineStart(i7)) || staticLayout.isRtlCharAt(staticLayout.getLineEnd(i7))) {
                i5 = i2;
            }
            int lineEnd = staticLayout.getLineEnd(i7);
            if (lineEnd != charSequence.length()) {
                lineEnd = (lineEnd - 1) + i8;
                String str = "\n";
                if (spannableStringBuilder.charAt(lineEnd) == ' ') {
                    spannableStringBuilder.replace(lineEnd, lineEnd + 1, str);
                } else if (spannableStringBuilder.charAt(lineEnd) != 10) {
                    spannableStringBuilder.insert(lineEnd, str);
                    i8++;
                }
                if (i7 == staticLayout.getLineCount() - 1 || i7 == i4 - 1) {
                    break;
                }
                i7++;
            } else {
                break;
            }
        }
        int i9 = i5;
        return StaticLayoutEx.createStaticLayout(spannableStringBuilder, textPaint, i9, Alignment.ALIGN_NORMAL, 1.0f, (float) AndroidUtilities.m26dp(1.0f), false, TruncateAt.END, i9, i4, true);
    }

    private void didClickedImage() {
        MessageObject messageObject = this.currentMessageObject;
        int i;
        if (messageObject.type == 1 || messageObject.isAnyKindOfSticker()) {
            i = this.buttonState;
            if (i == -1) {
                this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
                return;
            } else if (i == 0) {
                didPressButton(true, false);
                return;
            } else {
                return;
            }
        }
        messageObject = this.currentMessageObject;
        int i2 = messageObject.type;
        if (i2 == 12) {
            this.delegate.didPressUserAvatar(this, MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.currentMessageObject.messageOwner.media.user_id)), this.lastTouchX, this.lastTouchY);
        } else if (i2 == 5) {
            if (this.buttonState != -1) {
                didPressButton(true, false);
            } else if (!MediaController.getInstance().isPlayingMessage(this.currentMessageObject) || MediaController.getInstance().isMessagePaused()) {
                this.delegate.needPlayMessage(this.currentMessageObject);
            } else {
                MediaController.getInstance().lambda$startAudioAgain$5$MediaController(this.currentMessageObject);
            }
        } else if (i2 == 8) {
            i = this.buttonState;
            if (i == -1 || (i == 1 && this.canStreamVideo && this.autoPlayingMedia)) {
                this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
                return;
            }
            i = this.buttonState;
            if (i == 2 || i == 0) {
                didPressButton(true, false);
            }
        } else {
            int i3 = this.documentAttachType;
            if (i3 == 4) {
                if (this.buttonState == -1 || (this.drawVideoImageButton && (this.autoPlayingMedia || (SharedConfig.streamMedia && this.canStreamVideo)))) {
                    this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
                } else if (this.drawVideoImageButton) {
                    didPressButton(true, true);
                } else {
                    i = this.buttonState;
                    if (i == 0 || i == 3) {
                        didPressButton(true, false);
                    }
                }
            } else if (i2 == 4) {
                this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
            } else if (i3 == 1) {
                if (this.buttonState == -1) {
                    this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
                }
            } else if (i3 == 2) {
                if (this.buttonState == -1) {
                    WebPage webPage = messageObject.messageOwner.media.webpage;
                    if (webPage != null) {
                        String str = webPage.embed_url;
                        if (str == null || str.length() == 0) {
                            Browser.openUrl(getContext(), webPage.url);
                        } else {
                            this.delegate.needOpenWebView(webPage.embed_url, webPage.site_name, webPage.description, webPage.url, webPage.embed_width, webPage.embed_height);
                        }
                    }
                }
            } else if (this.hasInvoicePreview && this.buttonState == -1) {
                this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
            }
        }
    }

    private void updateSecretTimeText(MessageObject messageObject) {
        if (messageObject != null && messageObject.needDrawBluredPreview()) {
            String secretTimeString = messageObject.getSecretTimeString();
            if (secretTimeString != null) {
                this.infoWidth = (int) Math.ceil((double) Theme.chat_infoPaint.measureText(secretTimeString));
                this.infoLayout = new StaticLayout(TextUtils.ellipsize(secretTimeString, Theme.chat_infoPaint, (float) this.infoWidth, TruncateAt.END), Theme.chat_infoPaint, this.infoWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                invalidate();
            }
        }
    }

    private boolean isPhotoDataChanged(MessageObject messageObject) {
        MessageObject messageObject2 = messageObject;
        int i = messageObject2.type;
        boolean z = false;
        if (!(i == 0 || i == 14)) {
            if (i != 4) {
                PhotoSize photoSize = this.currentPhotoObject;
                if (photoSize == null || (photoSize.location instanceof TL_fileLocationUnavailable)) {
                    i = messageObject2.type;
                    if (i == 1 || i == 5 || i == 3 || i == 8 || messageObject.isAnyKindOfSticker()) {
                        z = true;
                    }
                } else {
                    messageObject2 = this.currentMessageObject;
                    if (messageObject2 == null || !this.photoNotSet) {
                        return false;
                    }
                    return FileLoader.getPathToMessage(messageObject2.messageOwner).exists();
                }
            } else if (this.currentUrl == null) {
                return true;
            } else {
                String formapMapUrl;
                MessageMedia messageMedia = messageObject2.messageOwner.media;
                GeoPoint geoPoint = messageMedia.geo;
                double d = geoPoint.lat;
                double d2 = geoPoint._long;
                int dp;
                float f;
                float f2;
                int i2;
                if (messageMedia instanceof TL_messageMediaGeoLive) {
                    dp = this.backgroundWidth - AndroidUtilities.m26dp(21.0f);
                    i = AndroidUtilities.m26dp(195.0f);
                    double d3 = (double) 268435456;
                    Double.isNaN(d3);
                    double d4 = d3 / 3.141592653589793d;
                    d = (d * 3.141592653589793d) / 180.0d;
                    double log = (Math.log((Math.sin(d) + 1.0d) / (1.0d - Math.sin(d))) * d4) / 2.0d;
                    Double.isNaN(d3);
                    log = (double) (Math.round(d3 - log) - ((long) (AndroidUtilities.m26dp(10.3f) << 6)));
                    Double.isNaN(log);
                    Double.isNaN(d3);
                    double atan = ((1.5707963267948966d - (Math.atan(Math.exp((log - d3) / d4)) * 2.0d)) * 180.0d) / 3.141592653589793d;
                    int i3 = this.currentAccount;
                    f = (float) dp;
                    f2 = AndroidUtilities.density;
                    formapMapUrl = AndroidUtilities.formapMapUrl(i3, atan, d2, (int) (f / f2), (int) (((float) i) / f2), false, 15);
                } else if (TextUtils.isEmpty(messageMedia.title)) {
                    dp = this.backgroundWidth - AndroidUtilities.m26dp(12.0f);
                    i = AndroidUtilities.m26dp(195.0f);
                    i2 = this.currentAccount;
                    f = (float) dp;
                    f2 = AndroidUtilities.density;
                    formapMapUrl = AndroidUtilities.formapMapUrl(i2, d, d2, (int) (f / f2), (int) (((float) i) / f2), true, 15);
                } else {
                    dp = this.backgroundWidth - AndroidUtilities.m26dp(21.0f);
                    i = AndroidUtilities.m26dp(195.0f);
                    i2 = this.currentAccount;
                    f = (float) dp;
                    f2 = AndroidUtilities.density;
                    formapMapUrl = AndroidUtilities.formapMapUrl(i2, d, d2, (int) (f / f2), (int) (((float) i) / f2), true, 15);
                }
                return formapMapUrl.equals(this.currentUrl) ^ 1;
            }
        }
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:58:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00ab A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x00c7  */
    /* JADX WARNING: Missing block: B:61:0x00a3, code skipped:
            if (r8.currentMessageObject.replyMessageObject.isAnyKindOfSticker() == false) goto L_0x00a7;
     */
    private boolean isUserDataChanged() {
        /*
        r8 = this;
        r0 = r8.currentMessageObject;
        r1 = 1;
        if (r0 == 0) goto L_0x0016;
    L_0x0005:
        r2 = r8.hasLinkPreview;
        if (r2 != 0) goto L_0x0016;
    L_0x0009:
        r0 = r0.messageOwner;
        r0 = r0.media;
        if (r0 == 0) goto L_0x0016;
    L_0x000f:
        r0 = r0.webpage;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_webPage;
        if (r0 == 0) goto L_0x0016;
    L_0x0015:
        return r1;
    L_0x0016:
        r0 = r8.currentMessageObject;
        r2 = 0;
        if (r0 == 0) goto L_0x0113;
    L_0x001b:
        r0 = r8.currentUser;
        if (r0 != 0) goto L_0x0025;
    L_0x001f:
        r0 = r8.currentChat;
        if (r0 != 0) goto L_0x0025;
    L_0x0023:
        goto L_0x0113;
    L_0x0025:
        r0 = r8.lastSendState;
        r3 = r8.currentMessageObject;
        r3 = r3.messageOwner;
        r4 = r3.send_state;
        if (r0 == r4) goto L_0x0030;
    L_0x002f:
        return r1;
    L_0x0030:
        r0 = r8.lastDeleteDate;
        r4 = r3.destroyTime;
        if (r0 == r4) goto L_0x0037;
    L_0x0036:
        return r1;
    L_0x0037:
        r0 = r8.lastViewsCount;
        r3 = r3.views;
        if (r0 == r3) goto L_0x003e;
    L_0x003d:
        return r1;
    L_0x003e:
        r8.updateCurrentUserAndChat();
        r0 = r8.isAvatarVisible;
        r3 = 0;
        if (r0 == 0) goto L_0x005c;
    L_0x0046:
        r0 = r8.currentUser;
        if (r0 == 0) goto L_0x0051;
    L_0x004a:
        r0 = r0.photo;
        if (r0 == 0) goto L_0x0051;
    L_0x004e:
        r0 = r0.photo_small;
        goto L_0x005d;
    L_0x0051:
        r0 = r8.currentChat;
        if (r0 == 0) goto L_0x005c;
    L_0x0055:
        r0 = r0.photo;
        if (r0 == 0) goto L_0x005c;
    L_0x0059:
        r0 = r0.photo_small;
        goto L_0x005d;
    L_0x005c:
        r0 = r3;
    L_0x005d:
        r4 = r8.replyTextLayout;
        if (r4 != 0) goto L_0x0068;
    L_0x0061:
        r4 = r8.currentMessageObject;
        r4 = r4.replyMessageObject;
        if (r4 == 0) goto L_0x0068;
    L_0x0067:
        return r1;
    L_0x0068:
        r4 = r8.currentPhoto;
        if (r4 != 0) goto L_0x006e;
    L_0x006c:
        if (r0 != 0) goto L_0x0088;
    L_0x006e:
        r4 = r8.currentPhoto;
        if (r4 == 0) goto L_0x0074;
    L_0x0072:
        if (r0 == 0) goto L_0x0088;
    L_0x0074:
        r4 = r8.currentPhoto;
        if (r4 == 0) goto L_0x0089;
    L_0x0078:
        if (r0 == 0) goto L_0x0089;
    L_0x007a:
        r5 = r4.local_id;
        r6 = r0.local_id;
        if (r5 != r6) goto L_0x0088;
    L_0x0080:
        r4 = r4.volume_id;
        r6 = r0.volume_id;
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 == 0) goto L_0x0089;
    L_0x0088:
        return r1;
    L_0x0089:
        r0 = r8.replyNameLayout;
        if (r0 == 0) goto L_0x00a6;
    L_0x008d:
        r0 = r8.currentMessageObject;
        r0 = r0.replyMessageObject;
        r0 = r0.photoThumbs;
        r4 = 40;
        r0 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r0, r4);
        if (r0 == 0) goto L_0x00a6;
    L_0x009b:
        r4 = r8.currentMessageObject;
        r4 = r4.replyMessageObject;
        r4 = r4.isAnyKindOfSticker();
        if (r4 != 0) goto L_0x00a6;
    L_0x00a5:
        goto L_0x00a7;
    L_0x00a6:
        r0 = r3;
    L_0x00a7:
        r4 = r8.currentReplyPhoto;
        if (r4 != 0) goto L_0x00ae;
    L_0x00ab:
        if (r0 == 0) goto L_0x00ae;
    L_0x00ad:
        return r1;
    L_0x00ae:
        r0 = r8.drawName;
        if (r0 == 0) goto L_0x00cd;
    L_0x00b2:
        r0 = r8.isChat;
        if (r0 == 0) goto L_0x00cd;
    L_0x00b6:
        r0 = r8.currentMessageObject;
        r0 = r0.isOutOwner();
        if (r0 != 0) goto L_0x00cd;
    L_0x00be:
        r0 = r8.currentUser;
        if (r0 == 0) goto L_0x00c7;
    L_0x00c2:
        r3 = org.telegram.messenger.UserObject.getUserName(r0);
        goto L_0x00cd;
    L_0x00c7:
        r0 = r8.currentChat;
        if (r0 == 0) goto L_0x00cd;
    L_0x00cb:
        r3 = r0.title;
    L_0x00cd:
        r0 = r8.currentNameString;
        if (r0 != 0) goto L_0x00d3;
    L_0x00d1:
        if (r3 != 0) goto L_0x00e5;
    L_0x00d3:
        r0 = r8.currentNameString;
        if (r0 == 0) goto L_0x00d9;
    L_0x00d7:
        if (r3 == 0) goto L_0x00e5;
    L_0x00d9:
        r0 = r8.currentNameString;
        if (r0 == 0) goto L_0x00e6;
    L_0x00dd:
        if (r3 == 0) goto L_0x00e6;
    L_0x00df:
        r0 = r0.equals(r3);
        if (r0 != 0) goto L_0x00e6;
    L_0x00e5:
        return r1;
    L_0x00e6:
        r0 = r8.drawForwardedName;
        if (r0 == 0) goto L_0x0113;
    L_0x00ea:
        r0 = r8.currentMessageObject;
        r0 = r0.needDrawForwarded();
        if (r0 == 0) goto L_0x0113;
    L_0x00f2:
        r0 = r8.currentMessageObject;
        r0 = r0.getForwardedName();
        r3 = r8.currentForwardNameString;
        if (r3 != 0) goto L_0x00fe;
    L_0x00fc:
        if (r0 != 0) goto L_0x0112;
    L_0x00fe:
        r3 = r8.currentForwardNameString;
        if (r3 == 0) goto L_0x0104;
    L_0x0102:
        if (r0 == 0) goto L_0x0112;
    L_0x0104:
        r3 = r8.currentForwardNameString;
        if (r3 == 0) goto L_0x0111;
    L_0x0108:
        if (r0 == 0) goto L_0x0111;
    L_0x010a:
        r0 = r3.equals(r0);
        if (r0 != 0) goto L_0x0111;
    L_0x0110:
        goto L_0x0112;
    L_0x0111:
        r1 = 0;
    L_0x0112:
        return r1;
    L_0x0113:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.isUserDataChanged():boolean");
    }

    public ImageReceiver getPhotoImage() {
        return this.photoImage;
    }

    public int getNoSoundIconCenterX() {
        return this.noSoundCenterX;
    }

    public int getForwardNameCenterX() {
        User user = this.currentUser;
        if (user == null || user.f534id != 0) {
            return this.forwardNameX + this.forwardNameCenterX;
        }
        return (int) this.avatarImage.getCenterX();
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CheckBoxBase checkBoxBase = this.checkBox;
        if (checkBoxBase != null) {
            checkBoxBase.onDetachedFromWindow();
        }
        checkBoxBase = this.photoCheckBox;
        if (checkBoxBase != null) {
            checkBoxBase.onDetachedFromWindow();
        }
        this.attachedToWindow = false;
        this.radialProgress.onDetachedFromWindow();
        this.videoRadialProgress.onDetachedFromWindow();
        this.avatarImage.onDetachedFromWindow();
        this.replyImageReceiver.onDetachedFromWindow();
        this.locationImageReceiver.onDetachedFromWindow();
        this.photoImage.onDetachedFromWindow();
        if (!(!this.addedForTest || this.currentUrl == null || this.currentWebFile == null)) {
            ImageLoader.getInstance().removeTestWebFile(this.currentUrl);
            this.addedForTest = false;
        }
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MessageObject messageObject = this.messageObjectToSet;
        if (messageObject != null) {
            setMessageContent(messageObject, this.groupedMessagesToSet, this.bottomNearToSet, this.topNearToSet);
            this.messageObjectToSet = null;
            this.groupedMessagesToSet = null;
        }
        CheckBoxBase checkBoxBase = this.checkBox;
        if (checkBoxBase != null) {
            checkBoxBase.onAttachedToWindow();
        }
        checkBoxBase = this.photoCheckBox;
        if (checkBoxBase != null) {
            checkBoxBase.onAttachedToWindow();
        }
        this.attachedToWindow = true;
        float f = 0.0f;
        setTranslationX(0.0f);
        this.radialProgress.onAttachedToWindow();
        this.videoRadialProgress.onAttachedToWindow();
        this.avatarImage.onAttachedToWindow();
        this.avatarImage.setParentView((View) getParent());
        this.replyImageReceiver.onAttachedToWindow();
        this.locationImageReceiver.onAttachedToWindow();
        if (!this.photoImage.onAttachedToWindow()) {
            updateButtonState(false, false, false);
        } else if (this.drawPhotoImage) {
            updateButtonState(false, false, false);
        }
        MessageObject messageObject2 = this.currentMessageObject;
        if (messageObject2 != null && (messageObject2.isRoundVideo() || this.currentMessageObject.isVideo())) {
            checkVideoPlayback(true);
        }
        if (this.documentAttachType == 4 && this.autoPlayingMedia) {
            this.animatingNoSoundPlaying = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
            if (!this.animatingNoSoundPlaying) {
                f = 1.0f;
            }
            this.animatingNoSoundProgress = f;
            this.animatingNoSound = 0;
            return;
        }
        this.animatingNoSoundPlaying = false;
        this.animatingNoSoundProgress = 0.0f;
        int i = this.documentAttachType;
        if ((i == 4 || i == 2) && this.drawVideoSize) {
            f = 1.0f;
        }
        this.animatingDrawVideoImageButtonProgress = f;
    }

    /* JADX WARNING: Removed duplicated region for block: B:383:0x0793  */
    /* JADX WARNING: Removed duplicated region for block: B:378:0x0787  */
    /* JADX WARNING: Removed duplicated region for block: B:386:0x0798  */
    /* JADX WARNING: Removed duplicated region for block: B:608:0x0bfc A:{Catch:{ Exception -> 0x0c1c }} */
    /* JADX WARNING: Removed duplicated region for block: B:907:0x126b  */
    /* JADX WARNING: Removed duplicated region for block: B:910:0x1282  */
    /* JADX WARNING: Removed duplicated region for block: B:996:0x15b3  */
    /* JADX WARNING: Removed duplicated region for block: B:995:0x15b1  */
    /* JADX WARNING: Removed duplicated region for block: B:1006:0x1625  */
    /* JADX WARNING: Removed duplicated region for block: B:850:0x1190  */
    /* JADX WARNING: Removed duplicated region for block: B:854:0x11ab  */
    /* JADX WARNING: Removed duplicated region for block: B:853:0x11a4  */
    /* JADX WARNING: Removed duplicated region for block: B:874:0x11ed  */
    /* JADX WARNING: Removed duplicated region for block: B:873:0x11e6  */
    /* JADX WARNING: Removed duplicated region for block: B:880:0x1203  */
    /* JADX WARNING: Removed duplicated region for block: B:877:0x11f9  */
    /* JADX WARNING: Removed duplicated region for block: B:883:0x120a  */
    /* JADX WARNING: Removed duplicated region for block: B:919:0x12d5  */
    /* JADX WARNING: Removed duplicated region for block: B:915:0x12a4  */
    /* JADX WARNING: Removed duplicated region for block: B:930:0x1365  */
    /* JADX WARNING: Removed duplicated region for block: B:928:0x133e  */
    /* JADX WARNING: Removed duplicated region for block: B:1000:0x15d9  */
    /* JADX WARNING: Removed duplicated region for block: B:1006:0x1625  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:812:0x1109  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:412:0x080e  */
    /* JADX WARNING: Removed duplicated region for block: B:416:0x081f  */
    /* JADX WARNING: Removed duplicated region for block: B:415:0x081b  */
    /* JADX WARNING: Removed duplicated region for block: B:445:0x08b3  */
    /* JADX WARNING: Removed duplicated region for block: B:422:0x0838 A:{SYNTHETIC, Splitter:B:422:0x0838} */
    /* JADX WARNING: Removed duplicated region for block: B:521:0x0a23  */
    /* JADX WARNING: Removed duplicated region for block: B:447:0x08bb  */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:305:0x0624  */
    /* JADX WARNING: Removed duplicated region for block: B:318:0x0677  */
    /* JADX WARNING: Removed duplicated region for block: B:336:0x06e3  */
    /* JADX WARNING: Removed duplicated region for block: B:328:0x06bb  */
    /* JADX WARNING: Removed duplicated region for block: B:346:0x070f  */
    /* JADX WARNING: Removed duplicated region for block: B:397:0x07b4  */
    /* JADX WARNING: Removed duplicated region for block: B:349:0x071a  */
    /* JADX WARNING: Removed duplicated region for block: B:412:0x080e  */
    /* JADX WARNING: Removed duplicated region for block: B:415:0x081b  */
    /* JADX WARNING: Removed duplicated region for block: B:416:0x081f  */
    /* JADX WARNING: Removed duplicated region for block: B:422:0x0838 A:{SYNTHETIC, Splitter:B:422:0x0838} */
    /* JADX WARNING: Removed duplicated region for block: B:445:0x08b3  */
    /* JADX WARNING: Removed duplicated region for block: B:447:0x08bb  */
    /* JADX WARNING: Removed duplicated region for block: B:521:0x0a23  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x0a34 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:297:0x05ca  */
    /* JADX WARNING: Removed duplicated region for block: B:305:0x0624  */
    /* JADX WARNING: Removed duplicated region for block: B:312:0x0657  */
    /* JADX WARNING: Removed duplicated region for block: B:318:0x0677  */
    /* JADX WARNING: Removed duplicated region for block: B:321:0x069e  */
    /* JADX WARNING: Removed duplicated region for block: B:328:0x06bb  */
    /* JADX WARNING: Removed duplicated region for block: B:336:0x06e3  */
    /* JADX WARNING: Removed duplicated region for block: B:346:0x070f  */
    /* JADX WARNING: Removed duplicated region for block: B:349:0x071a  */
    /* JADX WARNING: Removed duplicated region for block: B:397:0x07b4  */
    /* JADX WARNING: Removed duplicated region for block: B:412:0x080e  */
    /* JADX WARNING: Removed duplicated region for block: B:416:0x081f  */
    /* JADX WARNING: Removed duplicated region for block: B:415:0x081b  */
    /* JADX WARNING: Removed duplicated region for block: B:445:0x08b3  */
    /* JADX WARNING: Removed duplicated region for block: B:422:0x0838 A:{SYNTHETIC, Splitter:B:422:0x0838} */
    /* JADX WARNING: Removed duplicated region for block: B:521:0x0a23  */
    /* JADX WARNING: Removed duplicated region for block: B:447:0x08bb  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x0a34 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:1139:0x1a8b  */
    /* JADX WARNING: Removed duplicated region for block: B:1143:0x1a9d  */
    /* JADX WARNING: Removed duplicated region for block: B:1142:0x1a98  */
    /* JADX WARNING: Removed duplicated region for block: B:1311:0x1f1b  */
    /* JADX WARNING: Removed duplicated region for block: B:1301:0x1ef5  */
    /* JADX WARNING: Removed duplicated region for block: B:1300:0x1ef0  */
    /* JADX WARNING: Removed duplicated region for block: B:1311:0x1f1b  */
    /* JADX WARNING: Removed duplicated region for block: B:1319:0x1f54  */
    /* JADX WARNING: Removed duplicated region for block: B:1311:0x1f1b  */
    /* JADX WARNING: Removed duplicated region for block: B:1319:0x1f54  */
    /* JADX WARNING: Removed duplicated region for block: B:1581:0x272b  */
    /* JADX WARNING: Removed duplicated region for block: B:1573:0x2719  */
    /* JADX WARNING: Removed duplicated region for block: B:1587:0x2757  */
    /* JADX WARNING: Removed duplicated region for block: B:1586:0x2741  */
    /* JADX WARNING: Removed duplicated region for block: B:1976:0x2f4c  */
    /* JADX WARNING: Removed duplicated region for block: B:1825:0x2c30  */
    /* JADX WARNING: Removed duplicated region for block: B:2032:0x30a7  */
    /* JADX WARNING: Removed duplicated region for block: B:1993:0x2fa0 A:{SYNTHETIC, Splitter:B:1993:0x2fa0} */
    /* JADX WARNING: Removed duplicated region for block: B:2047:0x310d  */
    /* JADX WARNING: Removed duplicated region for block: B:2054:0x3145  */
    /* JADX WARNING: Removed duplicated region for block: B:2053:0x311a  */
    /* JADX WARNING: Removed duplicated region for block: B:2099:0x31c8  */
    /* JADX WARNING: Removed duplicated region for block: B:2078:0x3189  */
    /* JADX WARNING: Removed duplicated region for block: B:2103:0x321d  */
    /* JADX WARNING: Removed duplicated region for block: B:2102:0x31ce  */
    /* JADX WARNING: Removed duplicated region for block: B:1801:0x2baa  */
    /* JADX WARNING: Removed duplicated region for block: B:1794:0x2b68  */
    /* JADX WARNING: Removed duplicated region for block: B:1804:0x2bb8  */
    /* JADX WARNING: Removed duplicated region for block: B:1808:0x2be8  */
    /* JADX WARNING: Removed duplicated region for block: B:1807:0x2be5  */
    /* JADX WARNING: Removed duplicated region for block: B:1811:0x2bf3  */
    /* JADX WARNING: Removed duplicated region for block: B:1816:0x2c0a  */
    /* JADX WARNING: Removed duplicated region for block: B:1814:0x2bfa  */
    /* JADX WARNING: Removed duplicated region for block: B:1825:0x2c30  */
    /* JADX WARNING: Removed duplicated region for block: B:1976:0x2f4c  */
    /* JADX WARNING: Removed duplicated region for block: B:1993:0x2fa0 A:{SYNTHETIC, Splitter:B:1993:0x2fa0} */
    /* JADX WARNING: Removed duplicated region for block: B:2032:0x30a7  */
    /* JADX WARNING: Removed duplicated region for block: B:2034:0x30ac  */
    /* JADX WARNING: Removed duplicated region for block: B:2047:0x310d  */
    /* JADX WARNING: Removed duplicated region for block: B:2053:0x311a  */
    /* JADX WARNING: Removed duplicated region for block: B:2054:0x3145  */
    /* JADX WARNING: Removed duplicated region for block: B:2057:0x315f  */
    /* JADX WARNING: Removed duplicated region for block: B:2065:0x316e A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:2078:0x3189  */
    /* JADX WARNING: Removed duplicated region for block: B:2099:0x31c8  */
    /* JADX WARNING: Removed duplicated region for block: B:2102:0x31ce  */
    /* JADX WARNING: Removed duplicated region for block: B:2103:0x321d  */
    /* JADX WARNING: Removed duplicated region for block: B:1743:0x2a9e  */
    /* JADX WARNING: Removed duplicated region for block: B:1742:0x2a9b  */
    /* JADX WARNING: Removed duplicated region for block: B:1753:0x2ab4  */
    /* JADX WARNING: Removed duplicated region for block: B:1768:0x2aeb  */
    /* JADX WARNING: Removed duplicated region for block: B:1794:0x2b68  */
    /* JADX WARNING: Removed duplicated region for block: B:1801:0x2baa  */
    /* JADX WARNING: Removed duplicated region for block: B:1804:0x2bb8  */
    /* JADX WARNING: Removed duplicated region for block: B:1807:0x2be5  */
    /* JADX WARNING: Removed duplicated region for block: B:1808:0x2be8  */
    /* JADX WARNING: Removed duplicated region for block: B:1811:0x2bf3  */
    /* JADX WARNING: Removed duplicated region for block: B:1814:0x2bfa  */
    /* JADX WARNING: Removed duplicated region for block: B:1816:0x2c0a  */
    /* JADX WARNING: Removed duplicated region for block: B:1976:0x2f4c  */
    /* JADX WARNING: Removed duplicated region for block: B:1825:0x2c30  */
    /* JADX WARNING: Removed duplicated region for block: B:2032:0x30a7  */
    /* JADX WARNING: Removed duplicated region for block: B:1993:0x2fa0 A:{SYNTHETIC, Splitter:B:1993:0x2fa0} */
    /* JADX WARNING: Removed duplicated region for block: B:2034:0x30ac  */
    /* JADX WARNING: Removed duplicated region for block: B:2047:0x310d  */
    /* JADX WARNING: Removed duplicated region for block: B:2054:0x3145  */
    /* JADX WARNING: Removed duplicated region for block: B:2053:0x311a  */
    /* JADX WARNING: Removed duplicated region for block: B:2057:0x315f  */
    /* JADX WARNING: Removed duplicated region for block: B:2065:0x316e A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:2099:0x31c8  */
    /* JADX WARNING: Removed duplicated region for block: B:2078:0x3189  */
    /* JADX WARNING: Removed duplicated region for block: B:2103:0x321d  */
    /* JADX WARNING: Removed duplicated region for block: B:2102:0x31ce  */
    /* JADX WARNING: Removed duplicated region for block: B:1702:0x2a08  */
    /* JADX WARNING: Removed duplicated region for block: B:1701:0x2a03  */
    /* JADX WARNING: Removed duplicated region for block: B:1732:0x2a7f  */
    /* JADX WARNING: Removed duplicated region for block: B:1742:0x2a9b  */
    /* JADX WARNING: Removed duplicated region for block: B:1743:0x2a9e  */
    /* JADX WARNING: Removed duplicated region for block: B:1753:0x2ab4  */
    /* JADX WARNING: Removed duplicated region for block: B:1768:0x2aeb  */
    /* JADX WARNING: Removed duplicated region for block: B:1801:0x2baa  */
    /* JADX WARNING: Removed duplicated region for block: B:1794:0x2b68  */
    /* JADX WARNING: Removed duplicated region for block: B:1804:0x2bb8  */
    /* JADX WARNING: Removed duplicated region for block: B:1808:0x2be8  */
    /* JADX WARNING: Removed duplicated region for block: B:1807:0x2be5  */
    /* JADX WARNING: Removed duplicated region for block: B:1811:0x2bf3  */
    /* JADX WARNING: Removed duplicated region for block: B:1816:0x2c0a  */
    /* JADX WARNING: Removed duplicated region for block: B:1814:0x2bfa  */
    /* JADX WARNING: Removed duplicated region for block: B:1825:0x2c30  */
    /* JADX WARNING: Removed duplicated region for block: B:1976:0x2f4c  */
    /* JADX WARNING: Removed duplicated region for block: B:1993:0x2fa0 A:{SYNTHETIC, Splitter:B:1993:0x2fa0} */
    /* JADX WARNING: Removed duplicated region for block: B:2032:0x30a7  */
    /* JADX WARNING: Removed duplicated region for block: B:2034:0x30ac  */
    /* JADX WARNING: Removed duplicated region for block: B:2047:0x310d  */
    /* JADX WARNING: Removed duplicated region for block: B:2053:0x311a  */
    /* JADX WARNING: Removed duplicated region for block: B:2054:0x3145  */
    /* JADX WARNING: Removed duplicated region for block: B:2057:0x315f  */
    /* JADX WARNING: Removed duplicated region for block: B:2065:0x316e A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:2078:0x3189  */
    /* JADX WARNING: Removed duplicated region for block: B:2099:0x31c8  */
    /* JADX WARNING: Removed duplicated region for block: B:2102:0x31ce  */
    /* JADX WARNING: Removed duplicated region for block: B:2103:0x321d  */
    /* JADX WARNING: Removed duplicated region for block: B:1679:0x29a4  */
    /* JADX WARNING: Removed duplicated region for block: B:1667:0x2973  */
    /* JADX WARNING: Removed duplicated region for block: B:1690:0x29d6  */
    /* JADX WARNING: Removed duplicated region for block: B:1689:0x29c8  */
    /* JADX WARNING: Removed duplicated region for block: B:1701:0x2a03  */
    /* JADX WARNING: Removed duplicated region for block: B:1702:0x2a08  */
    /* JADX WARNING: Removed duplicated region for block: B:1732:0x2a7f  */
    /* JADX WARNING: Removed duplicated region for block: B:1743:0x2a9e  */
    /* JADX WARNING: Removed duplicated region for block: B:1742:0x2a9b  */
    /* JADX WARNING: Removed duplicated region for block: B:1753:0x2ab4  */
    /* JADX WARNING: Removed duplicated region for block: B:1768:0x2aeb  */
    /* JADX WARNING: Removed duplicated region for block: B:1794:0x2b68  */
    /* JADX WARNING: Removed duplicated region for block: B:1801:0x2baa  */
    /* JADX WARNING: Removed duplicated region for block: B:1804:0x2bb8  */
    /* JADX WARNING: Removed duplicated region for block: B:1807:0x2be5  */
    /* JADX WARNING: Removed duplicated region for block: B:1808:0x2be8  */
    /* JADX WARNING: Removed duplicated region for block: B:1811:0x2bf3  */
    /* JADX WARNING: Removed duplicated region for block: B:1814:0x2bfa  */
    /* JADX WARNING: Removed duplicated region for block: B:1816:0x2c0a  */
    /* JADX WARNING: Removed duplicated region for block: B:1976:0x2f4c  */
    /* JADX WARNING: Removed duplicated region for block: B:1825:0x2c30  */
    /* JADX WARNING: Removed duplicated region for block: B:2032:0x30a7  */
    /* JADX WARNING: Removed duplicated region for block: B:1993:0x2fa0 A:{SYNTHETIC, Splitter:B:1993:0x2fa0} */
    /* JADX WARNING: Removed duplicated region for block: B:2034:0x30ac  */
    /* JADX WARNING: Removed duplicated region for block: B:2047:0x310d  */
    /* JADX WARNING: Removed duplicated region for block: B:2054:0x3145  */
    /* JADX WARNING: Removed duplicated region for block: B:2053:0x311a  */
    /* JADX WARNING: Removed duplicated region for block: B:2057:0x315f  */
    /* JADX WARNING: Removed duplicated region for block: B:2065:0x316e A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:2099:0x31c8  */
    /* JADX WARNING: Removed duplicated region for block: B:2078:0x3189  */
    /* JADX WARNING: Removed duplicated region for block: B:2103:0x321d  */
    /* JADX WARNING: Removed duplicated region for block: B:2102:0x31ce  */
    /* JADX WARNING: Removed duplicated region for block: B:2350:0x3832  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2215:0x34bd  */
    /* JADX WARNING: Removed duplicated region for block: B:2208:0x3490  */
    /* JADX WARNING: Removed duplicated region for block: B:2218:0x34c2  */
    /* JADX WARNING: Removed duplicated region for block: B:2225:0x34f5  */
    /* JADX WARNING: Removed duplicated region for block: B:2221:0x34cf  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2195:0x3447  */
    /* JADX WARNING: Removed duplicated region for block: B:2208:0x3490  */
    /* JADX WARNING: Removed duplicated region for block: B:2215:0x34bd  */
    /* JADX WARNING: Removed duplicated region for block: B:2218:0x34c2  */
    /* JADX WARNING: Removed duplicated region for block: B:2221:0x34cf  */
    /* JADX WARNING: Removed duplicated region for block: B:2225:0x34f5  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:1365:0x204b  */
    /* JADX WARNING: Removed duplicated region for block: B:1364:0x2045  */
    /* JADX WARNING: Removed duplicated region for block: B:1470:0x228d  */
    /* JADX WARNING: Removed duplicated region for block: B:1381:0x2089  */
    /* JADX WARNING: Removed duplicated region for block: B:2195:0x3447  */
    /* JADX WARNING: Removed duplicated region for block: B:2215:0x34bd  */
    /* JADX WARNING: Removed duplicated region for block: B:2208:0x3490  */
    /* JADX WARNING: Removed duplicated region for block: B:2218:0x34c2  */
    /* JADX WARNING: Removed duplicated region for block: B:2225:0x34f5  */
    /* JADX WARNING: Removed duplicated region for block: B:2221:0x34cf  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x01dc  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0204  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x02b6  */
    /* JADX WARNING: Removed duplicated region for block: B:1035:0x1762  */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x02c7  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x01dc  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0204  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x02b6  */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x02c7  */
    /* JADX WARNING: Removed duplicated region for block: B:1035:0x1762  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x01dc  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0204  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x02b6  */
    /* JADX WARNING: Removed duplicated region for block: B:1035:0x1762  */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x02c7  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x0a34 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x0a34 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x0a34 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:2034:0x30ac  */
    /* JADX WARNING: Removed duplicated region for block: B:2047:0x310d  */
    /* JADX WARNING: Removed duplicated region for block: B:2053:0x311a  */
    /* JADX WARNING: Removed duplicated region for block: B:2054:0x3145  */
    /* JADX WARNING: Removed duplicated region for block: B:2057:0x315f  */
    /* JADX WARNING: Removed duplicated region for block: B:2065:0x316e A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:2078:0x3189  */
    /* JADX WARNING: Removed duplicated region for block: B:2099:0x31c8  */
    /* JADX WARNING: Removed duplicated region for block: B:2102:0x31ce  */
    /* JADX WARNING: Removed duplicated region for block: B:2103:0x321d  */
    /* JADX WARNING: Removed duplicated region for block: B:2195:0x3447  */
    /* JADX WARNING: Removed duplicated region for block: B:2208:0x3490  */
    /* JADX WARNING: Removed duplicated region for block: B:2215:0x34bd  */
    /* JADX WARNING: Removed duplicated region for block: B:2218:0x34c2  */
    /* JADX WARNING: Removed duplicated region for block: B:2221:0x34cf  */
    /* JADX WARNING: Removed duplicated region for block: B:2225:0x34f5  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:2293:0x36d0 A:{Catch:{ Exception -> 0x373d }} */
    /* JADX WARNING: Removed duplicated region for block: B:2297:0x371b A:{Catch:{ Exception -> 0x373d }} */
    /* JADX WARNING: Removed duplicated region for block: B:2308:0x374e  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x0a34 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:447:0x08bb  */
    /* JADX WARNING: Removed duplicated region for block: B:521:0x0a23  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x0a34 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:1428:0x21a0  */
    /* JADX WARNING: Removed duplicated region for block: B:1421:0x216d  */
    /* JADX WARNING: Removed duplicated region for block: B:1431:0x21a5  */
    /* JADX WARNING: Removed duplicated region for block: B:1440:0x21d4  */
    /* JADX WARNING: Removed duplicated region for block: B:1447:0x2202  */
    /* JADX WARNING: Removed duplicated region for block: B:1451:0x221d  */
    /* JADX WARNING: Removed duplicated region for block: B:1450:0x2210  */
    /* JADX WARNING: Removed duplicated region for block: B:1462:0x2256  */
    /* JADX WARNING: Removed duplicated region for block: B:2195:0x3447  */
    /* JADX WARNING: Removed duplicated region for block: B:2215:0x34bd  */
    /* JADX WARNING: Removed duplicated region for block: B:2208:0x3490  */
    /* JADX WARNING: Removed duplicated region for block: B:2218:0x34c2  */
    /* JADX WARNING: Removed duplicated region for block: B:2225:0x34f5  */
    /* JADX WARNING: Removed duplicated region for block: B:2221:0x34cf  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:1421:0x216d  */
    /* JADX WARNING: Removed duplicated region for block: B:1428:0x21a0  */
    /* JADX WARNING: Removed duplicated region for block: B:1431:0x21a5  */
    /* JADX WARNING: Removed duplicated region for block: B:1440:0x21d4  */
    /* JADX WARNING: Removed duplicated region for block: B:1447:0x2202  */
    /* JADX WARNING: Removed duplicated region for block: B:1450:0x2210  */
    /* JADX WARNING: Removed duplicated region for block: B:1451:0x221d  */
    /* JADX WARNING: Removed duplicated region for block: B:1462:0x2256  */
    /* JADX WARNING: Removed duplicated region for block: B:2195:0x3447  */
    /* JADX WARNING: Removed duplicated region for block: B:2208:0x3490  */
    /* JADX WARNING: Removed duplicated region for block: B:2215:0x34bd  */
    /* JADX WARNING: Removed duplicated region for block: B:2218:0x34c2  */
    /* JADX WARNING: Removed duplicated region for block: B:2221:0x34cf  */
    /* JADX WARNING: Removed duplicated region for block: B:2225:0x34f5  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:2034:0x30ac  */
    /* JADX WARNING: Removed duplicated region for block: B:2047:0x310d  */
    /* JADX WARNING: Removed duplicated region for block: B:2054:0x3145  */
    /* JADX WARNING: Removed duplicated region for block: B:2053:0x311a  */
    /* JADX WARNING: Removed duplicated region for block: B:2057:0x315f  */
    /* JADX WARNING: Removed duplicated region for block: B:2065:0x316e A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:2099:0x31c8  */
    /* JADX WARNING: Removed duplicated region for block: B:2078:0x3189  */
    /* JADX WARNING: Removed duplicated region for block: B:2103:0x321d  */
    /* JADX WARNING: Removed duplicated region for block: B:2102:0x31ce  */
    /* JADX WARNING: Removed duplicated region for block: B:2195:0x3447  */
    /* JADX WARNING: Removed duplicated region for block: B:2215:0x34bd  */
    /* JADX WARNING: Removed duplicated region for block: B:2208:0x3490  */
    /* JADX WARNING: Removed duplicated region for block: B:2218:0x34c2  */
    /* JADX WARNING: Removed duplicated region for block: B:2225:0x34f5  */
    /* JADX WARNING: Removed duplicated region for block: B:2221:0x34cf  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x0a34 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:521:0x0a23  */
    /* JADX WARNING: Removed duplicated region for block: B:447:0x08bb  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x0a34 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:447:0x08bb  */
    /* JADX WARNING: Removed duplicated region for block: B:521:0x0a23  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x0a34 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x0a34 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:521:0x0a23  */
    /* JADX WARNING: Removed duplicated region for block: B:447:0x08bb  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x0a34 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x0a34 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:549:0x0add  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x0c25  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:797:0x10bf  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0c40  */
    /* JADX WARNING: Removed duplicated region for block: B:823:0x112a  */
    /* JADX WARNING: Removed duplicated region for block: B:2238:0x3539  */
    /* JADX WARNING: Removed duplicated region for block: B:2249:0x3599 A:{SYNTHETIC, Splitter:B:2249:0x3599} */
    /* JADX WARNING: Removed duplicated region for block: B:2266:0x3625  */
    /* JADX WARNING: Removed duplicated region for block: B:2273:0x3646  */
    /* JADX WARNING: Removed duplicated region for block: B:2314:0x3770  */
    /* JADX WARNING: Removed duplicated region for block: B:2317:0x3781  */
    /* JADX WARNING: Removed duplicated region for block: B:2378:0x3983  */
    /* JADX WARNING: Removed duplicated region for block: B:2383:0x3995  */
    /* JADX WARNING: Removed duplicated region for block: B:2384:0x399f  */
    /* JADX WARNING: Removed duplicated region for block: B:2400:0x39d4  */
    /* JADX WARNING: Removed duplicated region for block: B:2403:0x39df  */
    /* JADX WARNING: Removed duplicated region for block: B:2410:0x3a11  */
    /* JADX WARNING: Removed duplicated region for block: B:2413:0x3a1c  */
    /* JADX WARNING: Missing block: B:845:0x1185, code skipped:
            if (r5 != 8) goto L_0x1189;
     */
    /* JADX WARNING: Missing block: B:1754:0x2abc, code skipped:
            if ("m".equals(r4.type) == false) goto L_0x2abe;
     */
    /* JADX WARNING: Missing block: B:2050:0x3112, code skipped:
            if (r2 != 5) goto L_0x315a;
     */
    private void setMessageContent(org.telegram.messenger.MessageObject r60, org.telegram.messenger.MessageObject.GroupedMessages r61, boolean r62, boolean r63) {
        /*
        r59 = this;
        r1 = r59;
        r14 = r60;
        r0 = r61;
        r2 = r62;
        r3 = r63;
        r4 = r60.checkLayout();
        r15 = 0;
        if (r4 != 0) goto L_0x001d;
    L_0x0011:
        r4 = r1.currentPosition;
        if (r4 == 0) goto L_0x001f;
    L_0x0015:
        r4 = r1.lastHeight;
        r5 = org.telegram.messenger.AndroidUtilities.displaySize;
        r5 = r5.y;
        if (r4 == r5) goto L_0x001f;
    L_0x001d:
        r1.currentMessageObject = r15;
    L_0x001f:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.y;
        r1.lastHeight = r4;
        r4 = r1.currentMessageObject;
        r13 = 1;
        r12 = 0;
        if (r4 == 0) goto L_0x0039;
    L_0x002b:
        r4 = r4.getId();
        r5 = r60.getId();
        if (r4 == r5) goto L_0x0036;
    L_0x0035:
        goto L_0x0039;
    L_0x0036:
        r16 = 0;
        goto L_0x003b;
    L_0x0039:
        r16 = 1;
    L_0x003b:
        r4 = r1.currentMessageObject;
        if (r4 != r14) goto L_0x0046;
    L_0x003f:
        r4 = r14.forceUpdate;
        if (r4 == 0) goto L_0x0044;
    L_0x0043:
        goto L_0x0046;
    L_0x0044:
        r4 = 0;
        goto L_0x0047;
    L_0x0046:
        r4 = 1;
    L_0x0047:
        r5 = r1.currentMessageObject;
        r11 = 3;
        if (r5 == 0) goto L_0x0060;
    L_0x004c:
        r5 = r5.getId();
        r6 = r60.getId();
        if (r5 != r6) goto L_0x0060;
    L_0x0056:
        r5 = r1.lastSendState;
        if (r5 != r11) goto L_0x0060;
    L_0x005a:
        r5 = r60.isSent();
        if (r5 != 0) goto L_0x006e;
    L_0x0060:
        r5 = r1.currentMessageObject;
        if (r5 != r14) goto L_0x0071;
    L_0x0064:
        r5 = r59.isUserDataChanged();
        if (r5 != 0) goto L_0x006e;
    L_0x006a:
        r5 = r1.photoNotSet;
        if (r5 == 0) goto L_0x0071;
    L_0x006e:
        r17 = 1;
        goto L_0x0073;
    L_0x0071:
        r17 = 0;
    L_0x0073:
        r5 = r1.currentMessagesGroup;
        if (r0 == r5) goto L_0x0079;
    L_0x0077:
        r5 = 1;
        goto L_0x007a;
    L_0x0079:
        r5 = 0;
    L_0x007a:
        r10 = 0;
        if (r4 != 0) goto L_0x00cf;
    L_0x007d:
        r6 = r14.type;
        r7 = 17;
        if (r6 != r7) goto L_0x00cf;
    L_0x0083:
        r6 = r14.messageOwner;
        r6 = r6.media;
        r7 = r6 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPoll;
        if (r7 == 0) goto L_0x0096;
    L_0x008b:
        r6 = (org.telegram.tgnet.TLRPC.TL_messageMediaPoll) r6;
        r7 = r6.results;
        r8 = r7.results;
        r6 = r6.poll;
        r7 = r7.total_voters;
        goto L_0x0099;
    L_0x0096:
        r6 = r15;
        r8 = r6;
        r7 = 0;
    L_0x0099:
        if (r8 == 0) goto L_0x00a5;
    L_0x009b:
        r9 = r1.lastPollResults;
        if (r9 == 0) goto L_0x00a5;
    L_0x009f:
        r9 = r1.lastPollResultsVoters;
        if (r7 == r9) goto L_0x00a5;
    L_0x00a3:
        r7 = 1;
        goto L_0x00a6;
    L_0x00a5:
        r7 = 0;
    L_0x00a6:
        if (r7 != 0) goto L_0x00ad;
    L_0x00a8:
        r9 = r1.lastPollResults;
        if (r8 == r9) goto L_0x00ad;
    L_0x00ac:
        r7 = 1;
    L_0x00ad:
        if (r7 != 0) goto L_0x00ba;
    L_0x00af:
        r8 = r1.lastPoll;
        if (r8 == r6) goto L_0x00ba;
    L_0x00b3:
        r8 = r8.closed;
        r6 = r6.closed;
        if (r8 == r6) goto L_0x00ba;
    L_0x00b9:
        r7 = 1;
    L_0x00ba:
        if (r7 == 0) goto L_0x00d0;
    L_0x00bc:
        r6 = r1.attachedToWindow;
        if (r6 == 0) goto L_0x00d0;
    L_0x00c0:
        r1.pollAnimationProgressTime = r10;
        r6 = r1.pollVoted;
        if (r6 == 0) goto L_0x00d0;
    L_0x00c6:
        r6 = r60.isVoted();
        if (r6 != 0) goto L_0x00d0;
    L_0x00cc:
        r1.pollUnvoteInProgress = r13;
        goto L_0x00d0;
    L_0x00cf:
        r7 = 0;
    L_0x00d0:
        if (r5 != 0) goto L_0x00f1;
    L_0x00d2:
        if (r0 == 0) goto L_0x00f1;
    L_0x00d4:
        r5 = r0.messages;
        r5 = r5.size();
        if (r5 <= r13) goto L_0x00e9;
    L_0x00dc:
        r5 = r1.currentMessagesGroup;
        r5 = r5.positions;
        r6 = r1.currentMessageObject;
        r5 = r5.get(r6);
        r5 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r5;
        goto L_0x00ea;
    L_0x00e9:
        r5 = r15;
    L_0x00ea:
        r6 = r1.currentPosition;
        if (r5 == r6) goto L_0x00f0;
    L_0x00ee:
        r5 = 1;
        goto L_0x00f1;
    L_0x00f0:
        r5 = 0;
    L_0x00f1:
        r9 = 2;
        if (r4 != 0) goto L_0x010c;
    L_0x00f4:
        if (r17 != 0) goto L_0x010c;
    L_0x00f6:
        if (r5 != 0) goto L_0x010c;
    L_0x00f8:
        if (r7 != 0) goto L_0x010c;
    L_0x00fa:
        r5 = r59.isPhotoDataChanged(r60);
        if (r5 != 0) goto L_0x010c;
    L_0x0100:
        r5 = r1.pinnedBottom;
        if (r5 != r2) goto L_0x010c;
    L_0x0104:
        r5 = r1.pinnedTop;
        if (r5 == r3) goto L_0x0109;
    L_0x0108:
        goto L_0x010c;
    L_0x0109:
        r15 = 0;
        goto L_0x3a17;
    L_0x010c:
        r1.pinnedBottom = r2;
        r1.pinnedTop = r3;
        r1.currentMessageObject = r14;
        r1.currentMessagesGroup = r0;
        r0 = -2;
        r1.lastTime = r0;
        r1.isHighlightedAnimated = r12;
        r8 = -1;
        r1.widthBeforeNewTimeLine = r8;
        r0 = r1.currentMessagesGroup;
        if (r0 == 0) goto L_0x013d;
    L_0x0120:
        r0 = r0.posArray;
        r0 = r0.size();
        if (r0 <= r13) goto L_0x013d;
    L_0x0128:
        r0 = r1.currentMessagesGroup;
        r0 = r0.positions;
        r2 = r1.currentMessageObject;
        r0 = r0.get(r2);
        r0 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r0;
        r1.currentPosition = r0;
        r0 = r1.currentPosition;
        if (r0 != 0) goto L_0x0141;
    L_0x013a:
        r1.currentMessagesGroup = r15;
        goto L_0x0141;
    L_0x013d:
        r1.currentMessagesGroup = r15;
        r1.currentPosition = r15;
    L_0x0141:
        r0 = r1.pinnedTop;
        if (r0 == 0) goto L_0x0151;
    L_0x0145:
        r0 = r1.currentPosition;
        if (r0 == 0) goto L_0x014f;
    L_0x0149:
        r0 = r0.flags;
        r0 = r0 & 4;
        if (r0 == 0) goto L_0x0151;
    L_0x014f:
        r0 = 1;
        goto L_0x0152;
    L_0x0151:
        r0 = 0;
    L_0x0152:
        r1.drawPinnedTop = r0;
        r0 = r1.pinnedBottom;
        r6 = 8;
        if (r0 == 0) goto L_0x0165;
    L_0x015a:
        r0 = r1.currentPosition;
        if (r0 == 0) goto L_0x0163;
    L_0x015e:
        r0 = r0.flags;
        r0 = r0 & r6;
        if (r0 == 0) goto L_0x0165;
    L_0x0163:
        r0 = 1;
        goto L_0x0166;
    L_0x0165:
        r0 = 0;
    L_0x0166:
        r1.drawPinnedBottom = r0;
        r0 = r1.photoImage;
        r0.setCrossfadeWithOldImage(r12);
        r0 = r14.messageOwner;
        r2 = r0.send_state;
        r1.lastSendState = r2;
        r2 = r0.destroyTime;
        r1.lastDeleteDate = r2;
        r0 = r0.views;
        r1.lastViewsCount = r0;
        r1.isPressed = r12;
        r1.gamePreviewPressed = r12;
        r1.sharePressed = r12;
        r1.isCheckPressed = r13;
        r1.hasNewLineForTime = r12;
        r0 = r1.isChat;
        if (r0 == 0) goto L_0x019f;
    L_0x0189:
        r0 = r60.isOutOwner();
        if (r0 != 0) goto L_0x019f;
    L_0x018f:
        r0 = r60.needDrawAvatar();
        if (r0 == 0) goto L_0x019f;
    L_0x0195:
        r0 = r1.currentPosition;
        if (r0 == 0) goto L_0x019d;
    L_0x0199:
        r0 = r0.edge;
        if (r0 == 0) goto L_0x019f;
    L_0x019d:
        r0 = 1;
        goto L_0x01a0;
    L_0x019f:
        r0 = 0;
    L_0x01a0:
        r1.isAvatarVisible = r0;
        r1.wasLayout = r12;
        r1.drwaShareGoIcon = r12;
        r1.groupPhotoInvisible = r12;
        r1.animatingDrawVideoImageButton = r12;
        r1.drawVideoSize = r12;
        r1.canStreamVideo = r12;
        r1.animatingNoSound = r12;
        r0 = r59.checkNeedDrawShareButton(r60);
        r1.drawShareButton = r0;
        r1.replyNameLayout = r15;
        r1.adminLayout = r15;
        r1.checkOnlyButtonPressed = r12;
        r1.replyTextLayout = r15;
        r1.hasEmbed = r12;
        r1.autoPlayingMedia = r12;
        r1.replyNameWidth = r12;
        r1.replyTextWidth = r12;
        r1.viaWidth = r12;
        r1.viaNameWidth = r12;
        r1.addedCaptionHeight = r12;
        r1.currentReplyPhoto = r15;
        r1.currentUser = r15;
        r1.currentChat = r15;
        r1.currentViaBotUser = r15;
        r1.instantViewLayout = r15;
        r1.drawNameLayout = r12;
        r0 = r1.scheduledInvalidate;
        if (r0 == 0) goto L_0x01e3;
    L_0x01dc:
        r0 = r1.invalidateRunnable;
        org.telegram.messenger.AndroidUtilities.cancelRunOnUIThread(r0);
        r1.scheduledInvalidate = r12;
    L_0x01e3:
        r1.resetPressedLink(r8);
        r14.forceUpdate = r12;
        r1.drawPhotoImage = r12;
        r1.drawPhotoCheckBox = r12;
        r1.hasLinkPreview = r12;
        r1.hasOldCaptionPreview = r12;
        r1.hasGamePreview = r12;
        r1.hasInvoicePreview = r12;
        r1.instantButtonPressed = r12;
        r1.instantPressed = r12;
        if (r7 != 0) goto L_0x020e;
    L_0x01fa:
        r0 = android.os.Build.VERSION.SDK_INT;
        r2 = 21;
        if (r0 < r2) goto L_0x020e;
    L_0x0200:
        r0 = r1.selectorDrawable;
        if (r0 == 0) goto L_0x020e;
    L_0x0204:
        r0.setVisible(r12, r12);
        r0 = r1.selectorDrawable;
        r2 = android.util.StateSet.NOTHING;
        r0.setState(r2);
    L_0x020e:
        r1.linkPreviewPressed = r12;
        r1.buttonPressed = r12;
        r1.miniButtonPressed = r12;
        r1.pressedBotButton = r8;
        r1.pressedVoteButton = r8;
        r1.linkPreviewHeight = r12;
        r1.mediaOffsetY = r12;
        r1.documentAttachType = r12;
        r1.documentAttach = r15;
        r1.descriptionLayout = r15;
        r1.titleLayout = r15;
        r1.videoInfoLayout = r15;
        r1.photosCountLayout = r15;
        r1.siteNameLayout = r15;
        r1.authorLayout = r15;
        r1.captionLayout = r15;
        r1.captionOffsetX = r12;
        r1.currentCaption = r15;
        r1.docTitleLayout = r15;
        r1.drawImageButton = r12;
        r1.drawVideoImageButton = r12;
        r1.currentPhotoObject = r15;
        r1.photoParentObject = r15;
        r1.currentPhotoObjectThumb = r15;
        r1.currentPhotoFilter = r15;
        r1.infoLayout = r15;
        r1.cancelLoading = r12;
        r1.buttonState = r8;
        r1.miniButtonState = r8;
        r1.hasMiniProgress = r12;
        r0 = r1.addedForTest;
        if (r0 == 0) goto L_0x025f;
    L_0x024e:
        r0 = r1.currentUrl;
        if (r0 == 0) goto L_0x025f;
    L_0x0252:
        r0 = r1.currentWebFile;
        if (r0 == 0) goto L_0x025f;
    L_0x0256:
        r0 = org.telegram.messenger.ImageLoader.getInstance();
        r2 = r1.currentUrl;
        r0.removeTestWebFile(r2);
    L_0x025f:
        r1.addedForTest = r12;
        r1.currentUrl = r15;
        r1.currentWebFile = r15;
        r1.photoNotSet = r12;
        r1.drawBackground = r13;
        r1.drawName = r12;
        r1.useSeekBarWaweform = r12;
        r1.drawInstantView = r12;
        r1.drawInstantViewType = r12;
        r1.drawForwardedName = r12;
        r0 = r1.photoImage;
        r0.setSideClip(r10);
        r1.imageBackgroundColor = r12;
        r1.imageBackgroundSideColor = r12;
        r1.mediaBackground = r12;
        r0 = r1.photoImage;
        r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0.setAlpha(r7);
        if (r4 != 0) goto L_0x0289;
    L_0x0287:
        if (r17 == 0) goto L_0x028e;
    L_0x0289:
        r0 = r1.pollButtons;
        r0.clear();
    L_0x028e:
        r1.availableTimeWidth = r12;
        r0 = r1.photoImage;
        r0.setForceLoading(r12);
        r0 = r1.photoImage;
        r0.setNeedsQualityThumb(r12);
        r0 = r1.photoImage;
        r0.setShouldGenerateQualityThumb(r12);
        r0 = r1.photoImage;
        r0.setAllowDecodeSingleFrame(r12);
        r0 = r1.photoImage;
        r2 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0.setRoundRadius(r2);
        r0 = r1.photoImage;
        r0.setColorFilter(r15);
        if (r4 == 0) goto L_0x02bc;
    L_0x02b6:
        r1.firstVisibleBlockNum = r12;
        r1.lastVisibleBlockNum = r12;
        r1.needNewVisiblePart = r13;
    L_0x02bc:
        r0 = r14.type;
        r18 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r2 = 6;
        r19 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r20 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r0 != 0) goto L_0x1762;
    L_0x02c7:
        r1.drawForwardedName = r13;
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x030c;
    L_0x02cf:
        r0 = r1.isChat;
        if (r0 == 0) goto L_0x02ed;
    L_0x02d3:
        r0 = r60.isOutOwner();
        if (r0 != 0) goto L_0x02ed;
    L_0x02d9:
        r0 = r60.needDrawAvatar();
        if (r0 == 0) goto L_0x02ed;
    L_0x02df:
        r0 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r4 = 1123287040; // 0x42f40000 float:122.0 double:5.54977537E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r0 = r0 - r4;
        r1.drawName = r13;
        goto L_0x0354;
    L_0x02ed:
        r0 = r14.messageOwner;
        r0 = r0.to_id;
        r0 = r0.channel_id;
        if (r0 == 0) goto L_0x02fd;
    L_0x02f5:
        r0 = r60.isOutOwner();
        if (r0 != 0) goto L_0x02fd;
    L_0x02fb:
        r0 = 1;
        goto L_0x02fe;
    L_0x02fd:
        r0 = 0;
    L_0x02fe:
        r1.drawName = r0;
        r0 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r4 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r0 = r0 - r4;
        goto L_0x0354;
    L_0x030c:
        r0 = r1.isChat;
        if (r0 == 0) goto L_0x0330;
    L_0x0310:
        r0 = r60.isOutOwner();
        if (r0 != 0) goto L_0x0330;
    L_0x0316:
        r0 = r60.needDrawAvatar();
        if (r0 == 0) goto L_0x0330;
    L_0x031c:
        r0 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r0.x;
        r0 = r0.y;
        r0 = java.lang.Math.min(r4, r0);
        r4 = 1123287040; // 0x42f40000 float:122.0 double:5.54977537E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r0 = r0 - r4;
        r1.drawName = r13;
        goto L_0x0354;
    L_0x0330:
        r0 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r0.x;
        r0 = r0.y;
        r0 = java.lang.Math.min(r4, r0);
        r4 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r0 = r0 - r4;
        r4 = r14.messageOwner;
        r4 = r4.to_id;
        r4 = r4.channel_id;
        if (r4 == 0) goto L_0x0351;
    L_0x0349:
        r4 = r60.isOutOwner();
        if (r4 != 0) goto L_0x0351;
    L_0x034f:
        r4 = 1;
        goto L_0x0352;
    L_0x0351:
        r4 = 0;
    L_0x0352:
        r1.drawName = r4;
    L_0x0354:
        r4 = r0;
        r1.availableTimeWidth = r4;
        r0 = r60.isRoundVideo();
        if (r0 == 0) goto L_0x0389;
    L_0x035d:
        r0 = r1.availableTimeWidth;
        r6 = (double) r0;
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_audioTimePaint;
        r5 = "00:00";
        r0 = r0.measureText(r5);
        r5 = r4;
        r3 = (double) r0;
        r3 = java.lang.Math.ceil(r3);
        r0 = r60.isOutOwner();
        if (r0 == 0) goto L_0x0376;
    L_0x0374:
        r0 = 0;
        goto L_0x037c;
    L_0x0376:
        r0 = 1115684864; // 0x42800000 float:64.0 double:5.51221563E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
    L_0x037c:
        r10 = (double) r0;
        java.lang.Double.isNaN(r10);
        r3 = r3 + r10;
        java.lang.Double.isNaN(r6);
        r6 = r6 - r3;
        r0 = (int) r6;
        r1.availableTimeWidth = r0;
        goto L_0x038a;
    L_0x0389:
        r5 = r4;
    L_0x038a:
        r59.measureTime(r60);
        r0 = r1.timeWidth;
        r3 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 + r3;
        r3 = r60.isOutOwner();
        if (r3 == 0) goto L_0x03a3;
    L_0x039c:
        r3 = 1101266944; // 0x41a40000 float:20.5 double:5.44098164E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 + r3;
    L_0x03a3:
        r11 = r0;
        r0 = r14.messageOwner;
        r0 = r0.media;
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r3 == 0) goto L_0x03b4;
    L_0x03ac:
        r0 = r0.game;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_game;
        if (r0 == 0) goto L_0x03b4;
    L_0x03b2:
        r0 = 1;
        goto L_0x03b5;
    L_0x03b4:
        r0 = 0;
    L_0x03b5:
        r1.hasGamePreview = r0;
        r0 = r14.messageOwner;
        r0 = r0.media;
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
        r1.hasInvoicePreview = r3;
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r3 == 0) goto L_0x03cb;
    L_0x03c3:
        r0 = r0.webpage;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_webPage;
        if (r0 == 0) goto L_0x03cb;
    L_0x03c9:
        r0 = 1;
        goto L_0x03cc;
    L_0x03cb:
        r0 = 0;
    L_0x03cc:
        r1.hasLinkPreview = r0;
        r0 = r1.hasLinkPreview;
        if (r0 == 0) goto L_0x03de;
    L_0x03d2:
        r0 = r14.messageOwner;
        r0 = r0.media;
        r0 = r0.webpage;
        r0 = r0.cached_page;
        if (r0 == 0) goto L_0x03de;
    L_0x03dc:
        r0 = 1;
        goto L_0x03df;
    L_0x03de:
        r0 = 0;
    L_0x03df:
        r1.drawInstantView = r0;
        r0 = r1.hasLinkPreview;
        if (r0 == 0) goto L_0x03fb;
    L_0x03e5:
        r0 = r14.messageOwner;
        r0 = r0.media;
        r0 = r0.webpage;
        r0 = r0.embed_url;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x03fb;
    L_0x03f3:
        r0 = r60.isGif();
        if (r0 != 0) goto L_0x03fb;
    L_0x03f9:
        r0 = 1;
        goto L_0x03fc;
    L_0x03fb:
        r0 = 0;
    L_0x03fc:
        r1.hasEmbed = r0;
        r0 = r1.hasLinkPreview;
        if (r0 == 0) goto L_0x040b;
    L_0x0402:
        r0 = r14.messageOwner;
        r0 = r0.media;
        r0 = r0.webpage;
        r0 = r0.site_name;
        goto L_0x040c;
    L_0x040b:
        r0 = r15;
    L_0x040c:
        r3 = r1.hasLinkPreview;
        if (r3 == 0) goto L_0x0419;
    L_0x0410:
        r3 = r14.messageOwner;
        r3 = r3.media;
        r3 = r3.webpage;
        r3 = r3.type;
        goto L_0x041a;
    L_0x0419:
        r3 = r15;
    L_0x041a:
        r4 = r1.drawInstantView;
        if (r4 != 0) goto L_0x0506;
    L_0x041e:
        r0 = "telegram_channel";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x042c;
    L_0x0426:
        r1.drawInstantView = r13;
        r1.drawInstantViewType = r13;
        goto L_0x05b4;
    L_0x042c:
        r0 = "telegram_megagroup";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x043a;
    L_0x0434:
        r1.drawInstantView = r13;
        r1.drawInstantViewType = r9;
        goto L_0x05b4;
    L_0x043a:
        r0 = "telegram_message";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x0449;
    L_0x0442:
        r1.drawInstantView = r13;
        r3 = 3;
        r1.drawInstantViewType = r3;
        goto L_0x05b4;
    L_0x0449:
        r0 = "telegram_background";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x05b4;
    L_0x0451:
        r1.drawInstantView = r13;
        r1.drawInstantViewType = r2;
        r0 = r14.messageOwner;	 Catch:{ Exception -> 0x05b4 }
        r0 = r0.media;	 Catch:{ Exception -> 0x05b4 }
        r0 = r0.webpage;	 Catch:{ Exception -> 0x05b4 }
        r0 = r0.url;	 Catch:{ Exception -> 0x05b4 }
        r0 = android.net.Uri.parse(r0);	 Catch:{ Exception -> 0x05b4 }
        r3 = "intensity";
        r3 = r0.getQueryParameter(r3);	 Catch:{ Exception -> 0x05b4 }
        r3 = org.telegram.messenger.Utilities.parseInt(r3);	 Catch:{ Exception -> 0x05b4 }
        r3 = r3.intValue();	 Catch:{ Exception -> 0x05b4 }
        r4 = "bg_color";
        r4 = r0.getQueryParameter(r4);	 Catch:{ Exception -> 0x05b4 }
        r6 = android.text.TextUtils.isEmpty(r4);	 Catch:{ Exception -> 0x05b4 }
        if (r6 == 0) goto L_0x0491;
    L_0x047b:
        r6 = r60.getDocument();	 Catch:{ Exception -> 0x05b4 }
        if (r6 == 0) goto L_0x048d;
    L_0x0481:
        r7 = "image/png";
        r6 = r6.mime_type;	 Catch:{ Exception -> 0x05b4 }
        r6 = r7.equals(r6);	 Catch:{ Exception -> 0x05b4 }
        if (r6 == 0) goto L_0x048d;
    L_0x048b:
        r4 = "ffffff";
    L_0x048d:
        if (r3 != 0) goto L_0x0491;
    L_0x048f:
        r3 = 50;
    L_0x0491:
        if (r4 == 0) goto L_0x04c3;
    L_0x0493:
        r0 = 16;
        r0 = java.lang.Integer.parseInt(r4, r0);	 Catch:{ Exception -> 0x05b4 }
        r4 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r0 = r0 | r4;
        r1.imageBackgroundColor = r0;	 Catch:{ Exception -> 0x05b4 }
        r0 = r1.imageBackgroundColor;	 Catch:{ Exception -> 0x05b4 }
        r0 = org.telegram.messenger.AndroidUtilities.getPatternSideColor(r0);	 Catch:{ Exception -> 0x05b4 }
        r1.imageBackgroundSideColor = r0;	 Catch:{ Exception -> 0x05b4 }
        r0 = r1.photoImage;	 Catch:{ Exception -> 0x05b4 }
        r4 = new android.graphics.PorterDuffColorFilter;	 Catch:{ Exception -> 0x05b4 }
        r6 = r1.imageBackgroundColor;	 Catch:{ Exception -> 0x05b4 }
        r6 = org.telegram.messenger.AndroidUtilities.getPatternColor(r6);	 Catch:{ Exception -> 0x05b4 }
        r7 = android.graphics.PorterDuff.Mode.SRC_IN;	 Catch:{ Exception -> 0x05b4 }
        r4.<init>(r6, r7);	 Catch:{ Exception -> 0x05b4 }
        r0.setColorFilter(r4);	 Catch:{ Exception -> 0x05b4 }
        r0 = r1.photoImage;	 Catch:{ Exception -> 0x05b4 }
        r3 = (float) r3;	 Catch:{ Exception -> 0x05b4 }
        r4 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r3 = r3 / r4;
        r0.setAlpha(r3);	 Catch:{ Exception -> 0x05b4 }
        goto L_0x05b4;
    L_0x04c3:
        r0 = r0.getLastPathSegment();	 Catch:{ Exception -> 0x05b4 }
        if (r0 == 0) goto L_0x05b4;
    L_0x04c9:
        r3 = r0.length();	 Catch:{ Exception -> 0x05b4 }
        if (r3 != r2) goto L_0x05b4;
    L_0x04cf:
        r3 = 16;
        r0 = java.lang.Integer.parseInt(r0, r3);	 Catch:{ Exception -> 0x05b4 }
        r3 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r0 = r0 | r3;
        r1.imageBackgroundColor = r0;	 Catch:{ Exception -> 0x05b4 }
        r0 = new org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;	 Catch:{ Exception -> 0x05b4 }
        r0.<init>();	 Catch:{ Exception -> 0x05b4 }
        r1.currentPhotoObject = r0;	 Catch:{ Exception -> 0x05b4 }
        r0 = r1.currentPhotoObject;	 Catch:{ Exception -> 0x05b4 }
        r3 = "s";
        r0.type = r3;	 Catch:{ Exception -> 0x05b4 }
        r0 = r1.currentPhotoObject;	 Catch:{ Exception -> 0x05b4 }
        r3 = 1127481344; // 0x43340000 float:180.0 double:5.570497984E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);	 Catch:{ Exception -> 0x05b4 }
        r0.f465w = r3;	 Catch:{ Exception -> 0x05b4 }
        r0 = r1.currentPhotoObject;	 Catch:{ Exception -> 0x05b4 }
        r3 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);	 Catch:{ Exception -> 0x05b4 }
        r0.f464h = r3;	 Catch:{ Exception -> 0x05b4 }
        r0 = r1.currentPhotoObject;	 Catch:{ Exception -> 0x05b4 }
        r3 = new org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;	 Catch:{ Exception -> 0x05b4 }
        r3.<init>();	 Catch:{ Exception -> 0x05b4 }
        r0.location = r3;	 Catch:{ Exception -> 0x05b4 }
        goto L_0x05b4;
    L_0x0506:
        if (r0 == 0) goto L_0x05b4;
    L_0x0508:
        r0 = r0.toLowerCase();
        r4 = "instagram";
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x0524;
    L_0x0514:
        r4 = "twitter";
        r0 = r0.equals(r4);
        if (r0 != 0) goto L_0x0524;
    L_0x051c:
        r0 = "telegram_album";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x05b4;
    L_0x0524:
        r0 = r14.messageOwner;
        r0 = r0.media;
        r0 = r0.webpage;
        r3 = r0.cached_page;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_page;
        if (r3 == 0) goto L_0x05b4;
    L_0x0530:
        r3 = r0.photo;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_photo;
        if (r3 != 0) goto L_0x053e;
    L_0x0536:
        r0 = r0.document;
        r0 = org.telegram.messenger.MessageObject.isVideoDocument(r0);
        if (r0 == 0) goto L_0x05b4;
    L_0x053e:
        r1.drawInstantView = r12;
        r0 = r14.messageOwner;
        r0 = r0.media;
        r0 = r0.webpage;
        r0 = r0.cached_page;
        r0 = r0.blocks;
        r3 = 0;
        r4 = 1;
    L_0x054c:
        r6 = r0.size();
        if (r3 >= r6) goto L_0x0574;
    L_0x0552:
        r6 = r0.get(r3);
        r6 = (org.telegram.tgnet.TLRPC.PageBlock) r6;
        r7 = r6 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockSlideshow;
        if (r7 == 0) goto L_0x0565;
    L_0x055c:
        r6 = (org.telegram.tgnet.TLRPC.TL_pageBlockSlideshow) r6;
        r4 = r6.items;
        r4 = r4.size();
        goto L_0x0571;
    L_0x0565:
        r7 = r6 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockCollage;
        if (r7 == 0) goto L_0x0571;
    L_0x0569:
        r6 = (org.telegram.tgnet.TLRPC.TL_pageBlockCollage) r6;
        r4 = r6.items;
        r4 = r4.size();
    L_0x0571:
        r3 = r3 + 1;
        goto L_0x054c;
    L_0x0574:
        r0 = 2131560099; // 0x7f0d06a3 float:1.874556E38 double:1.053130617E-314;
        r3 = new java.lang.Object[r9];
        r6 = java.lang.Integer.valueOf(r13);
        r3[r12] = r6;
        r4 = java.lang.Integer.valueOf(r4);
        r3[r13] = r4;
        r4 = "Of";
        r0 = org.telegram.messenger.LocaleController.formatString(r4, r0, r3);
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_durationPaint;
        r3 = r3.measureText(r0);
        r3 = (double) r3;
        r3 = java.lang.Math.ceil(r3);
        r3 = (int) r3;
        r1.photosCountWidth = r3;
        r3 = new android.text.StaticLayout;
        r25 = org.telegram.p004ui.ActionBar.Theme.chat_durationPaint;
        r4 = r1.photosCountWidth;
        r27 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r28 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r29 = 0;
        r30 = 0;
        r23 = r3;
        r24 = r0;
        r26 = r4;
        r23.<init>(r24, r25, r26, r27, r28, r29, r30);
        r1.photosCountLayout = r3;
        r0 = 1;
        goto L_0x05b5;
    L_0x05b4:
        r0 = 0;
    L_0x05b5:
        r1.backgroundWidth = r5;
        r3 = r1.hasLinkPreview;
        if (r3 != 0) goto L_0x05f1;
    L_0x05bb:
        r3 = r1.hasGamePreview;
        if (r3 != 0) goto L_0x05f1;
    L_0x05bf:
        r3 = r1.hasInvoicePreview;
        if (r3 != 0) goto L_0x05f1;
    L_0x05c3:
        r3 = r14.lastLineWidth;
        r4 = r5 - r3;
        if (r4 >= r11) goto L_0x05ca;
    L_0x05c9:
        goto L_0x05f1;
    L_0x05ca:
        r4 = r1.backgroundWidth;
        r3 = r4 - r3;
        if (r3 < 0) goto L_0x05de;
    L_0x05d0:
        if (r3 > r11) goto L_0x05de;
    L_0x05d2:
        r4 = r4 + r11;
        r4 = r4 - r3;
        r3 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r4 = r4 + r3;
        r1.backgroundWidth = r4;
        goto L_0x0613;
    L_0x05de:
        r3 = r1.backgroundWidth;
        r4 = r14.lastLineWidth;
        r4 = r4 + r11;
        r3 = java.lang.Math.max(r3, r4);
        r4 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 + r4;
        r1.backgroundWidth = r3;
        goto L_0x0613;
    L_0x05f1:
        r3 = r1.backgroundWidth;
        r4 = r14.lastLineWidth;
        r3 = java.lang.Math.max(r3, r4);
        r4 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 + r4;
        r1.backgroundWidth = r3;
        r3 = r1.backgroundWidth;
        r4 = r1.timeWidth;
        r6 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r4 = r4 + r6;
        r3 = java.lang.Math.max(r3, r4);
        r1.backgroundWidth = r3;
    L_0x0613:
        r3 = r1.backgroundWidth;
        r4 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 - r4;
        r1.availableTimeWidth = r3;
        r3 = r60.isRoundVideo();
        if (r3 == 0) goto L_0x064e;
    L_0x0624:
        r3 = r1.availableTimeWidth;
        r3 = (double) r3;
        r6 = org.telegram.p004ui.ActionBar.Theme.chat_audioTimePaint;
        r7 = "00:00";
        r6 = r6.measureText(r7);
        r6 = (double) r6;
        r6 = java.lang.Math.ceil(r6);
        r10 = r60.isOutOwner();
        if (r10 == 0) goto L_0x063c;
    L_0x063a:
        r10 = 0;
        goto L_0x0642;
    L_0x063c:
        r10 = 1115684864; // 0x42800000 float:64.0 double:5.51221563E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
    L_0x0642:
        r8 = (double) r10;
        java.lang.Double.isNaN(r8);
        r6 = r6 + r8;
        java.lang.Double.isNaN(r3);
        r3 = r3 - r6;
        r3 = (int) r3;
        r1.availableTimeWidth = r3;
    L_0x064e:
        r59.setMessageObjectInternal(r60);
        r3 = r14.textWidth;
        r4 = r1.hasGamePreview;
        if (r4 != 0) goto L_0x065e;
    L_0x0657:
        r4 = r1.hasInvoicePreview;
        if (r4 == 0) goto L_0x065c;
    L_0x065b:
        goto L_0x065e;
    L_0x065c:
        r4 = 0;
        goto L_0x0662;
    L_0x065e:
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
    L_0x0662:
        r3 = r3 + r4;
        r1.backgroundWidth = r3;
        r3 = r14.textHeight;
        r4 = 1100742656; // 0x419c0000 float:19.5 double:5.43839131E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 + r4;
        r4 = r1.namesOffset;
        r3 = r3 + r4;
        r1.totalHeight = r3;
        r3 = r1.drawPinnedTop;
        if (r3 == 0) goto L_0x0680;
    L_0x0677:
        r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r4 = r4 - r6;
        r1.namesOffset = r4;
    L_0x0680:
        r3 = r1.backgroundWidth;
        r4 = r1.nameWidth;
        r3 = java.lang.Math.max(r3, r4);
        r4 = r1.forwardedNameWidth;
        r3 = java.lang.Math.max(r3, r4);
        r4 = r1.replyNameWidth;
        r3 = java.lang.Math.max(r3, r4);
        r4 = r1.replyTextWidth;
        r3 = java.lang.Math.max(r3, r4);
        r4 = r1.hasLinkPreview;
        if (r4 != 0) goto L_0x06b5;
    L_0x069e:
        r4 = r1.hasGamePreview;
        if (r4 != 0) goto L_0x06b5;
    L_0x06a2:
        r4 = r1.hasInvoicePreview;
        if (r4 == 0) goto L_0x06a7;
    L_0x06a6:
        goto L_0x06b5;
    L_0x06a7:
        r0 = r1.photoImage;
        r0.setImageBitmap(r15);
        r1.calcBackgroundWidth(r5, r11, r3);
        r13 = 0;
        r15 = 1;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x1ff4;
    L_0x06b5:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x06e3;
    L_0x06bb:
        r4 = r1.isChat;
        if (r4 == 0) goto L_0x06d8;
    L_0x06bf:
        r4 = r60.needDrawAvatar();
        if (r4 == 0) goto L_0x06d8;
    L_0x06c5:
        r4 = r1.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 != 0) goto L_0x06d8;
    L_0x06cd:
        r4 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r6 = 1124335616; // 0x43040000 float:132.0 double:5.554956023E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        goto L_0x070a;
    L_0x06d8:
        r4 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r6 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        goto L_0x070a;
    L_0x06e3:
        r4 = r1.isChat;
        if (r4 == 0) goto L_0x0700;
    L_0x06e7:
        r4 = r60.needDrawAvatar();
        if (r4 == 0) goto L_0x0700;
    L_0x06ed:
        r4 = r1.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 != 0) goto L_0x0700;
    L_0x06f5:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.x;
        r6 = 1124335616; // 0x43040000 float:132.0 double:5.554956023E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        goto L_0x070a;
    L_0x0700:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.x;
        r6 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
    L_0x070a:
        r4 = r4 - r6;
        r6 = r1.drawShareButton;
        if (r6 == 0) goto L_0x0716;
    L_0x070f:
        r6 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r4 = r4 - r6;
    L_0x0716:
        r6 = r1.hasLinkPreview;
        if (r6 == 0) goto L_0x07b4;
    L_0x071a:
        r6 = r14.messageOwner;
        r6 = r6.media;
        r6 = r6.webpage;
        r6 = (org.telegram.tgnet.TLRPC.TL_webPage) r6;
        r7 = r6.site_name;
        r8 = r1.drawInstantViewType;
        if (r8 == r2) goto L_0x072b;
    L_0x0728:
        r8 = r6.title;
        goto L_0x072c;
    L_0x072b:
        r8 = r15;
    L_0x072c:
        r9 = r1.drawInstantViewType;
        if (r9 == r2) goto L_0x0733;
    L_0x0730:
        r9 = r6.author;
        goto L_0x0734;
    L_0x0733:
        r9 = r15;
    L_0x0734:
        r10 = r1.drawInstantViewType;
        if (r10 == r2) goto L_0x073b;
    L_0x0738:
        r10 = r6.description;
        goto L_0x073c;
    L_0x073b:
        r10 = r15;
    L_0x073c:
        r15 = r6.photo;
        r13 = r6.document;
        r2 = r6.type;
        r6 = r6.duration;
        if (r7 == 0) goto L_0x0765;
    L_0x0746:
        if (r15 == 0) goto L_0x0765;
    L_0x0748:
        r12 = r7.toLowerCase();
        r29 = r4;
        r4 = "instagram";
        r4 = r12.equals(r4);
        if (r4 == 0) goto L_0x0767;
    L_0x0756:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.y;
        r12 = 3;
        r4 = r4 / r12;
        r12 = r1.currentMessageObject;
        r12 = r12.textWidth;
        r4 = java.lang.Math.max(r4, r12);
        goto L_0x0769;
    L_0x0765:
        r29 = r4;
    L_0x0767:
        r4 = r29;
    L_0x0769:
        r12 = "app";
        r12 = r12.equals(r2);
        if (r12 != 0) goto L_0x0784;
    L_0x0771:
        r12 = "profile";
        r12 = r12.equals(r2);
        if (r12 != 0) goto L_0x0784;
    L_0x0779:
        r12 = "article";
        r12 = r12.equals(r2);
        if (r12 == 0) goto L_0x0782;
    L_0x0781:
        goto L_0x0784;
    L_0x0782:
        r12 = 0;
        goto L_0x0785;
    L_0x0784:
        r12 = 1;
    L_0x0785:
        if (r0 != 0) goto L_0x0793;
    L_0x0787:
        r29 = r4;
        r4 = r1.drawInstantView;
        if (r4 != 0) goto L_0x0795;
    L_0x078d:
        if (r13 != 0) goto L_0x0795;
    L_0x078f:
        if (r12 == 0) goto L_0x0795;
    L_0x0791:
        r4 = 1;
        goto L_0x0796;
    L_0x0793:
        r29 = r4;
    L_0x0795:
        r4 = 0;
    L_0x0796:
        if (r0 != 0) goto L_0x07ac;
    L_0x0798:
        r0 = r1.drawInstantView;
        if (r0 != 0) goto L_0x07ac;
    L_0x079c:
        if (r13 != 0) goto L_0x07ac;
    L_0x079e:
        if (r10 == 0) goto L_0x07ac;
    L_0x07a0:
        if (r2 == 0) goto L_0x07ac;
    L_0x07a2:
        if (r12 == 0) goto L_0x07ac;
    L_0x07a4:
        r0 = r1.currentMessageObject;
        r0 = r0.photoThumbs;
        if (r0 == 0) goto L_0x07ac;
    L_0x07aa:
        r0 = 1;
        goto L_0x07ad;
    L_0x07ac:
        r0 = 0;
    L_0x07ad:
        r1.isSmallImage = r0;
        r12 = r4;
        r40 = r6;
        r6 = r2;
        goto L_0x0808;
    L_0x07b4:
        r29 = r4;
        r0 = r1.hasInvoicePreview;
        if (r0 == 0) goto L_0x07e1;
    L_0x07ba:
        r0 = r14.messageOwner;
        r0 = r0.media;
        r2 = r0;
        r2 = (org.telegram.tgnet.TLRPC.TL_messageMediaInvoice) r2;
        r7 = r0.title;
        r0 = r2.photo;
        r2 = r0 instanceof org.telegram.tgnet.TLRPC.TL_webDocument;
        if (r2 == 0) goto L_0x07d0;
    L_0x07c9:
        r0 = org.telegram.messenger.WebFile.createWithWebDocument(r0);
        r15 = r0;
        r2 = 0;
        goto L_0x07d2;
    L_0x07d0:
        r2 = 0;
        r15 = 0;
    L_0x07d2:
        r1.isSmallImage = r2;
        r2 = "invoice";
        r6 = r2;
        r2 = r15;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r12 = 0;
        r13 = 0;
        r15 = 0;
        r40 = 0;
        goto L_0x0809;
    L_0x07e1:
        r0 = r14.messageOwner;
        r0 = r0.media;
        r0 = r0.game;
        r7 = r0.title;
        r2 = r14.messageText;
        r2 = android.text.TextUtils.isEmpty(r2);
        if (r2 == 0) goto L_0x07f5;
    L_0x07f1:
        r2 = r0.description;
        r15 = r2;
        goto L_0x07f6;
    L_0x07f5:
        r15 = 0;
    L_0x07f6:
        r2 = r0.photo;
        r0 = r0.document;
        r4 = 0;
        r1.isSmallImage = r4;
        r4 = "game";
        r13 = r0;
        r6 = r4;
        r10 = r15;
        r8 = 0;
        r9 = 0;
        r12 = 0;
        r40 = 0;
        r15 = r2;
    L_0x0808:
        r2 = 0;
    L_0x0809:
        r0 = r1.drawInstantViewType;
        r4 = 6;
        if (r0 != r4) goto L_0x0817;
    L_0x080e:
        r0 = 2131559024; // 0x7f0d0270 float:1.874338E38 double:1.053130086E-314;
        r4 = "ChatBackground";
        r7 = org.telegram.messenger.LocaleController.getString(r4, r0);
    L_0x0817:
        r0 = r1.hasInvoicePreview;
        if (r0 == 0) goto L_0x081f;
    L_0x081b:
        r41 = r2;
        r4 = 0;
        goto L_0x0826;
    L_0x081f:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r4 = r0;
        r41 = r2;
    L_0x0826:
        r2 = r29 - r4;
        r0 = r1.currentMessageObject;
        r29 = r6;
        r6 = r0.photoThumbs;
        if (r6 != 0) goto L_0x0836;
    L_0x0830:
        if (r15 == 0) goto L_0x0836;
    L_0x0832:
        r6 = 1;
        r0.generateThumbs(r6);
    L_0x0836:
        if (r7 == 0) goto L_0x08b3;
    L_0x0838:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x08a8 }
        r0 = r0.measureText(r7);	 Catch:{ Exception -> 0x08a8 }
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r0 + r6;
        r42 = r5;
        r5 = (double) r0;
        r5 = java.lang.Math.ceil(r5);	 Catch:{ Exception -> 0x08a6 }
        r0 = (int) r5;	 Catch:{ Exception -> 0x08a6 }
        r5 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x08a6 }
        r32 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x08a6 }
        r33 = java.lang.Math.min(r0, r2);	 Catch:{ Exception -> 0x08a6 }
        r34 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x08a6 }
        r35 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r36 = 0;
        r37 = 0;
        r30 = r5;
        r31 = r7;
        r30.<init>(r31, r32, r33, r34, r35, r36, r37);	 Catch:{ Exception -> 0x08a6 }
        r1.siteNameLayout = r5;	 Catch:{ Exception -> 0x08a6 }
        r0 = r1.siteNameLayout;	 Catch:{ Exception -> 0x08a6 }
        r5 = 0;
        r0 = r0.getLineLeft(r5);	 Catch:{ Exception -> 0x08a6 }
        r22 = 0;
        r0 = (r0 > r22 ? 1 : (r0 == r22 ? 0 : -1));
        if (r0 == 0) goto L_0x0871;
    L_0x086f:
        r0 = 1;
        goto L_0x0872;
    L_0x0871:
        r0 = 0;
    L_0x0872:
        r1.siteNameRtl = r0;	 Catch:{ Exception -> 0x08a4 }
        r0 = r1.siteNameLayout;	 Catch:{ Exception -> 0x08a4 }
        r5 = r1.siteNameLayout;	 Catch:{ Exception -> 0x08a4 }
        r5 = r5.getLineCount();	 Catch:{ Exception -> 0x08a4 }
        r6 = 1;
        r5 = r5 - r6;
        r0 = r0.getLineBottom(r5);	 Catch:{ Exception -> 0x08a4 }
        r5 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x08a4 }
        r5 = r5 + r0;
        r1.linkPreviewHeight = r5;	 Catch:{ Exception -> 0x08a4 }
        r5 = r1.totalHeight;	 Catch:{ Exception -> 0x08a4 }
        r5 = r5 + r0;
        r1.totalHeight = r5;	 Catch:{ Exception -> 0x08a4 }
        r5 = 0;
        r6 = r0 + 0;
        r0 = r1.siteNameLayout;	 Catch:{ Exception -> 0x08a2 }
        r0 = r0.getWidth();	 Catch:{ Exception -> 0x08a2 }
        r1.siteNameWidth = r0;	 Catch:{ Exception -> 0x08a2 }
        r0 = r0 + r4;
        r3 = java.lang.Math.max(r3, r0);	 Catch:{ Exception -> 0x08a2 }
        r0 = java.lang.Math.max(r5, r0);	 Catch:{ Exception -> 0x08a2 }
        r5 = r0;
        goto L_0x08b9;
    L_0x08a2:
        r0 = move-exception;
        goto L_0x08ae;
    L_0x08a4:
        r0 = move-exception;
        goto L_0x08ad;
    L_0x08a6:
        r0 = move-exception;
        goto L_0x08ab;
    L_0x08a8:
        r0 = move-exception;
        r42 = r5;
    L_0x08ab:
        r22 = 0;
    L_0x08ad:
        r6 = 0;
    L_0x08ae:
        org.telegram.messenger.FileLog.m30e(r0);
        r5 = 0;
        goto L_0x08b9;
    L_0x08b3:
        r42 = r5;
        r22 = 0;
        r5 = 0;
        r6 = 0;
    L_0x08b9:
        if (r8 == 0) goto L_0x0a23;
    L_0x08bb:
        r0 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r1.titleX = r0;	 Catch:{ Exception -> 0x09f6 }
        r0 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x09f6 }
        if (r0 == 0) goto L_0x08eb;
    L_0x08c4:
        r0 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x08d9 }
        r30 = org.telegram.messenger.AndroidUtilities.m26dp(r20);	 Catch:{ Exception -> 0x08d9 }
        r0 = r0 + r30;
        r1.linkPreviewHeight = r0;	 Catch:{ Exception -> 0x08d9 }
        r0 = r1.totalHeight;	 Catch:{ Exception -> 0x08d9 }
        r30 = org.telegram.messenger.AndroidUtilities.m26dp(r20);	 Catch:{ Exception -> 0x08d9 }
        r0 = r0 + r30;
        r1.totalHeight = r0;	 Catch:{ Exception -> 0x08d9 }
        goto L_0x08eb;
    L_0x08d9:
        r0 = move-exception;
        r38 = r6;
        r39 = r11;
        r44 = r13;
        r43 = r15;
        r30 = 3;
        r31 = 0;
        r6 = r5;
        r15 = r12;
        r5 = r3;
        goto L_0x0a0c;
    L_0x08eb:
        r0 = r1.isSmallImage;	 Catch:{ Exception -> 0x09f6 }
        if (r0 == 0) goto L_0x091a;
    L_0x08ef:
        if (r10 != 0) goto L_0x08f2;
    L_0x08f1:
        goto L_0x091a;
    L_0x08f2:
        r31 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x08d9 }
        r0 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);	 Catch:{ Exception -> 0x08d9 }
        r33 = r2 - r0;
        r35 = 4;
        r34 = 3;
        r30 = r8;
        r32 = r2;
        r0 = org.telegram.p004ui.Cells.ChatMessageCell.generateStaticLayout(r30, r31, r32, r33, r34, r35);	 Catch:{ Exception -> 0x08d9 }
        r1.titleLayout = r0;	 Catch:{ Exception -> 0x08d9 }
        r0 = r1.titleLayout;	 Catch:{ Exception -> 0x08d9 }
        r0 = r0.getLineCount();	 Catch:{ Exception -> 0x08d9 }
        r21 = 3;
        r0 = 3 - r0;
        r30 = r0;
        r31 = r3;
        r0 = 3;
        goto L_0x0940;
    L_0x091a:
        r31 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x09f6 }
        r33 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x09f6 }
        r34 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r30);	 Catch:{ Exception -> 0x09f6 }
        r0 = (float) r0;	 Catch:{ Exception -> 0x09f6 }
        r36 = 0;
        r37 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x09f6 }
        r39 = 4;
        r30 = r8;
        r32 = r2;
        r35 = r0;
        r38 = r2;
        r0 = org.telegram.p004ui.Components.StaticLayoutEx.createStaticLayout(r30, r31, r32, r33, r34, r35, r36, r37, r38, r39);	 Catch:{ Exception -> 0x09f6 }
        r1.titleLayout = r0;	 Catch:{ Exception -> 0x09f6 }
        r31 = r3;
        r0 = 0;
        r30 = 3;
    L_0x0940:
        r3 = r1.titleLayout;	 Catch:{ Exception -> 0x09e5 }
        r32 = r5;
        r5 = r1.titleLayout;	 Catch:{ Exception -> 0x09e3 }
        r5 = r5.getLineCount();	 Catch:{ Exception -> 0x09e3 }
        r26 = 1;
        r5 = r5 + -1;
        r3 = r3.getLineBottom(r5);	 Catch:{ Exception -> 0x09e3 }
        r5 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x09e3 }
        r5 = r5 + r3;
        r1.linkPreviewHeight = r5;	 Catch:{ Exception -> 0x09e3 }
        r5 = r1.totalHeight;	 Catch:{ Exception -> 0x09e3 }
        r5 = r5 + r3;
        r1.totalHeight = r5;	 Catch:{ Exception -> 0x09e3 }
        r38 = r6;
        r39 = r11;
        r5 = r31;
        r6 = r32;
        r3 = 0;
        r31 = 0;
    L_0x0967:
        r11 = r1.titleLayout;	 Catch:{ Exception -> 0x09dc }
        r11 = r11.getLineCount();	 Catch:{ Exception -> 0x09dc }
        if (r3 >= r11) goto L_0x09d6;
    L_0x096f:
        r11 = r1.titleLayout;	 Catch:{ Exception -> 0x09dc }
        r11 = r11.getLineLeft(r3);	 Catch:{ Exception -> 0x09dc }
        r11 = (int) r11;
        if (r11 == 0) goto L_0x097d;
    L_0x0978:
        r43 = r15;
        r31 = 1;
        goto L_0x097f;
    L_0x097d:
        r43 = r15;
    L_0x097f:
        r15 = r1.titleX;	 Catch:{ Exception -> 0x09d1 }
        r44 = r13;
        r13 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        if (r15 != r13) goto L_0x098c;
    L_0x0988:
        r13 = -r11;
        r1.titleX = r13;	 Catch:{ Exception -> 0x09cf }
        goto L_0x0995;
    L_0x098c:
        r13 = r1.titleX;	 Catch:{ Exception -> 0x09cf }
        r15 = -r11;
        r13 = java.lang.Math.max(r13, r15);	 Catch:{ Exception -> 0x09cf }
        r1.titleX = r13;	 Catch:{ Exception -> 0x09cf }
    L_0x0995:
        if (r11 == 0) goto L_0x09a0;
    L_0x0997:
        r13 = r1.titleLayout;	 Catch:{ Exception -> 0x09cf }
        r13 = r13.getWidth();	 Catch:{ Exception -> 0x09cf }
        r13 = r13 - r11;
        r15 = r12;
        goto L_0x09ad;
    L_0x09a0:
        r13 = r1.titleLayout;	 Catch:{ Exception -> 0x09cf }
        r13 = r13.getLineWidth(r3);	 Catch:{ Exception -> 0x09cf }
        r15 = r12;
        r12 = (double) r13;
        r12 = java.lang.Math.ceil(r12);	 Catch:{ Exception -> 0x09cd }
        r13 = (int) r12;	 Catch:{ Exception -> 0x09cd }
    L_0x09ad:
        if (r3 < r0) goto L_0x09b5;
    L_0x09af:
        if (r11 == 0) goto L_0x09bc;
    L_0x09b1:
        r11 = r1.isSmallImage;	 Catch:{ Exception -> 0x09cd }
        if (r11 == 0) goto L_0x09bc;
    L_0x09b5:
        r11 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);	 Catch:{ Exception -> 0x09cd }
        r13 = r13 + r11;
    L_0x09bc:
        r13 = r13 + r4;
        r5 = java.lang.Math.max(r5, r13);	 Catch:{ Exception -> 0x09cd }
        r6 = java.lang.Math.max(r6, r13);	 Catch:{ Exception -> 0x09cd }
        r3 = r3 + 1;
        r12 = r15;
        r15 = r43;
        r13 = r44;
        goto L_0x0967;
    L_0x09cd:
        r0 = move-exception;
        goto L_0x0a0c;
    L_0x09cf:
        r0 = move-exception;
        goto L_0x09e1;
    L_0x09d1:
        r0 = move-exception;
        r15 = r12;
        r44 = r13;
        goto L_0x0a0c;
    L_0x09d6:
        r44 = r13;
        r43 = r15;
        r15 = r12;
        goto L_0x0a0f;
    L_0x09dc:
        r0 = move-exception;
        r44 = r13;
        r43 = r15;
    L_0x09e1:
        r15 = r12;
        goto L_0x0a0c;
    L_0x09e3:
        r0 = move-exception;
        goto L_0x09e8;
    L_0x09e5:
        r0 = move-exception;
        r32 = r5;
    L_0x09e8:
        r38 = r6;
        r39 = r11;
        r44 = r13;
        r43 = r15;
        r15 = r12;
        r5 = r31;
        r6 = r32;
        goto L_0x0a0a;
    L_0x09f6:
        r0 = move-exception;
        r31 = r3;
        r32 = r5;
        r38 = r6;
        r39 = r11;
        r44 = r13;
        r43 = r15;
        r15 = r12;
        r5 = r31;
        r6 = r32;
        r30 = 3;
    L_0x0a0a:
        r31 = 0;
    L_0x0a0c:
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0a0f:
        r3 = r5;
        r5 = r6;
        r11 = r30;
        r12 = r31;
        if (r12 == 0) goto L_0x0a32;
    L_0x0a17:
        r0 = r1.isSmallImage;
        if (r0 == 0) goto L_0x0a32;
    L_0x0a1b:
        r0 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r2 = r2 - r0;
        goto L_0x0a32;
    L_0x0a23:
        r31 = r3;
        r32 = r5;
        r38 = r6;
        r39 = r11;
        r44 = r13;
        r43 = r15;
        r15 = r12;
        r11 = 3;
        r12 = 0;
    L_0x0a32:
        if (r9 == 0) goto L_0x0ada;
    L_0x0a34:
        if (r8 != 0) goto L_0x0ada;
    L_0x0a36:
        r0 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x0ad4 }
        if (r0 == 0) goto L_0x0a4c;
    L_0x0a3a:
        r0 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x0ad4 }
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r20);	 Catch:{ Exception -> 0x0ad4 }
        r0 = r0 + r6;
        r1.linkPreviewHeight = r0;	 Catch:{ Exception -> 0x0ad4 }
        r0 = r1.totalHeight;	 Catch:{ Exception -> 0x0ad4 }
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r20);	 Catch:{ Exception -> 0x0ad4 }
        r0 = r0 + r6;
        r1.totalHeight = r0;	 Catch:{ Exception -> 0x0ad4 }
    L_0x0a4c:
        r6 = 3;
        if (r11 != r6) goto L_0x0a6d;
    L_0x0a4f:
        r0 = r1.isSmallImage;	 Catch:{ Exception -> 0x0ad4 }
        if (r0 == 0) goto L_0x0a55;
    L_0x0a53:
        if (r10 != 0) goto L_0x0a6d;
    L_0x0a55:
        r0 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0ad4 }
        r32 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x0ad4 }
        r34 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0ad4 }
        r35 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r36 = 0;
        r37 = 0;
        r30 = r0;
        r31 = r9;
        r33 = r2;
        r30.<init>(r31, r32, r33, r34, r35, r36, r37);	 Catch:{ Exception -> 0x0ad4 }
        r1.authorLayout = r0;	 Catch:{ Exception -> 0x0ad4 }
        goto L_0x0a8c;
    L_0x0a6d:
        r31 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x0ad4 }
        r0 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);	 Catch:{ Exception -> 0x0ad4 }
        r33 = r2 - r0;
        r35 = 1;
        r30 = r9;
        r32 = r2;
        r34 = r11;
        r0 = org.telegram.p004ui.Cells.ChatMessageCell.generateStaticLayout(r30, r31, r32, r33, r34, r35);	 Catch:{ Exception -> 0x0ad4 }
        r1.authorLayout = r0;	 Catch:{ Exception -> 0x0ad4 }
        r0 = r1.authorLayout;	 Catch:{ Exception -> 0x0ad4 }
        r0 = r0.getLineCount();	 Catch:{ Exception -> 0x0ad4 }
        r11 = r11 - r0;
    L_0x0a8c:
        r0 = r1.authorLayout;	 Catch:{ Exception -> 0x0ad4 }
        r6 = r1.authorLayout;	 Catch:{ Exception -> 0x0ad4 }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x0ad4 }
        r8 = 1;
        r6 = r6 - r8;
        r0 = r0.getLineBottom(r6);	 Catch:{ Exception -> 0x0ad4 }
        r6 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x0ad4 }
        r6 = r6 + r0;
        r1.linkPreviewHeight = r6;	 Catch:{ Exception -> 0x0ad4 }
        r6 = r1.totalHeight;	 Catch:{ Exception -> 0x0ad4 }
        r6 = r6 + r0;
        r1.totalHeight = r6;	 Catch:{ Exception -> 0x0ad4 }
        r0 = r1.authorLayout;	 Catch:{ Exception -> 0x0ad4 }
        r6 = 0;
        r0 = r0.getLineLeft(r6);	 Catch:{ Exception -> 0x0ad4 }
        r0 = (int) r0;	 Catch:{ Exception -> 0x0ad4 }
        r6 = -r0;
        r1.authorX = r6;	 Catch:{ Exception -> 0x0ad4 }
        if (r0 == 0) goto L_0x0aba;
    L_0x0ab1:
        r6 = r1.authorLayout;	 Catch:{ Exception -> 0x0ad4 }
        r6 = r6.getWidth();	 Catch:{ Exception -> 0x0ad4 }
        r6 = r6 - r0;
        r8 = 1;
        goto L_0x0ac8;
    L_0x0aba:
        r0 = r1.authorLayout;	 Catch:{ Exception -> 0x0ad4 }
        r6 = 0;
        r0 = r0.getLineWidth(r6);	 Catch:{ Exception -> 0x0ad4 }
        r8 = (double) r0;	 Catch:{ Exception -> 0x0ad4 }
        r8 = java.lang.Math.ceil(r8);	 Catch:{ Exception -> 0x0ad4 }
        r6 = (int) r8;
        r8 = 0;
    L_0x0ac8:
        r6 = r6 + r4;
        r3 = java.lang.Math.max(r3, r6);	 Catch:{ Exception -> 0x0ad2 }
        r5 = java.lang.Math.max(r5, r6);	 Catch:{ Exception -> 0x0ad2 }
        goto L_0x0adb;
    L_0x0ad2:
        r0 = move-exception;
        goto L_0x0ad6;
    L_0x0ad4:
        r0 = move-exception;
        r8 = 0;
    L_0x0ad6:
        org.telegram.messenger.FileLog.m30e(r0);
        goto L_0x0adb;
    L_0x0ada:
        r8 = 0;
    L_0x0adb:
        if (r10 == 0) goto L_0x0c23;
    L_0x0add:
        r6 = 0;
        r1.descriptionX = r6;	 Catch:{ Exception -> 0x0c1f }
        r0 = r1.currentMessageObject;	 Catch:{ Exception -> 0x0c1f }
        r0.generateLinkDescription();	 Catch:{ Exception -> 0x0c1f }
        r0 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x0c1f }
        if (r0 == 0) goto L_0x0afb;
    L_0x0ae9:
        r0 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x0c1f }
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r20);	 Catch:{ Exception -> 0x0c1f }
        r0 = r0 + r6;
        r1.linkPreviewHeight = r0;	 Catch:{ Exception -> 0x0c1f }
        r0 = r1.totalHeight;	 Catch:{ Exception -> 0x0c1f }
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r20);	 Catch:{ Exception -> 0x0c1f }
        r0 = r0 + r6;
        r1.totalHeight = r0;	 Catch:{ Exception -> 0x0c1f }
    L_0x0afb:
        if (r7 == 0) goto L_0x0b0b;
    L_0x0afd:
        r0 = r7.toLowerCase();	 Catch:{ Exception -> 0x0c1f }
        r6 = "twitter";
        r0 = r0.equals(r6);	 Catch:{ Exception -> 0x0c1f }
        if (r0 == 0) goto L_0x0b0b;
    L_0x0b09:
        r0 = 1;
        goto L_0x0b0c;
    L_0x0b0b:
        r0 = 0;
    L_0x0b0c:
        r6 = 3;
        if (r11 != r6) goto L_0x0b3f;
    L_0x0b0f:
        r6 = r1.isSmallImage;	 Catch:{ Exception -> 0x0c1f }
        if (r6 != 0) goto L_0x0b3f;
    L_0x0b13:
        r6 = r14.linkDescription;	 Catch:{ Exception -> 0x0c1f }
        r46 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;	 Catch:{ Exception -> 0x0c1f }
        r48 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0c1f }
        r49 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r9 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r9);	 Catch:{ Exception -> 0x0c1f }
        r9 = (float) r10;	 Catch:{ Exception -> 0x0c1f }
        r51 = 0;
        r52 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x0c1f }
        if (r0 == 0) goto L_0x0b2d;
    L_0x0b28:
        r0 = 100;
        r54 = 100;
        goto L_0x0b2f;
    L_0x0b2d:
        r54 = 6;
    L_0x0b2f:
        r45 = r6;
        r47 = r2;
        r50 = r9;
        r53 = r2;
        r0 = org.telegram.p004ui.Components.StaticLayoutEx.createStaticLayout(r45, r46, r47, r48, r49, r50, r51, r52, r53, r54);	 Catch:{ Exception -> 0x0c1f }
        r1.descriptionLayout = r0;	 Catch:{ Exception -> 0x0c1f }
        r11 = 0;
        goto L_0x0b60;
    L_0x0b3f:
        r6 = r14.linkDescription;	 Catch:{ Exception -> 0x0c1f }
        r31 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;	 Catch:{ Exception -> 0x0c1f }
        r9 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);	 Catch:{ Exception -> 0x0c1f }
        r33 = r2 - r9;
        if (r0 == 0) goto L_0x0b52;
    L_0x0b4d:
        r0 = 100;
        r35 = 100;
        goto L_0x0b54;
    L_0x0b52:
        r35 = 6;
    L_0x0b54:
        r30 = r6;
        r32 = r2;
        r34 = r11;
        r0 = org.telegram.p004ui.Cells.ChatMessageCell.generateStaticLayout(r30, r31, r32, r33, r34, r35);	 Catch:{ Exception -> 0x0c1f }
        r1.descriptionLayout = r0;	 Catch:{ Exception -> 0x0c1f }
    L_0x0b60:
        r0 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0c1f }
        r6 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0c1f }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x0c1f }
        r9 = 1;
        r6 = r6 - r9;
        r0 = r0.getLineBottom(r6);	 Catch:{ Exception -> 0x0c1f }
        r6 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x0c1f }
        r6 = r6 + r0;
        r1.linkPreviewHeight = r6;	 Catch:{ Exception -> 0x0c1f }
        r6 = r1.totalHeight;	 Catch:{ Exception -> 0x0c1f }
        r6 = r6 + r0;
        r1.totalHeight = r6;	 Catch:{ Exception -> 0x0c1f }
        r0 = 0;
        r13 = 0;
    L_0x0b7a:
        r6 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0c1f }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x0c1f }
        if (r0 >= r6) goto L_0x0ba5;
    L_0x0b82:
        r6 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0c1f }
        r6 = r6.getLineLeft(r0);	 Catch:{ Exception -> 0x0c1f }
        r9 = (double) r6;	 Catch:{ Exception -> 0x0c1f }
        r9 = java.lang.Math.ceil(r9);	 Catch:{ Exception -> 0x0c1f }
        r6 = (int) r9;	 Catch:{ Exception -> 0x0c1f }
        if (r6 == 0) goto L_0x0ba2;
    L_0x0b90:
        r9 = r1.descriptionX;	 Catch:{ Exception -> 0x0c1f }
        if (r9 != 0) goto L_0x0b98;
    L_0x0b94:
        r6 = -r6;
        r1.descriptionX = r6;	 Catch:{ Exception -> 0x0c1f }
        goto L_0x0ba1;
    L_0x0b98:
        r9 = r1.descriptionX;	 Catch:{ Exception -> 0x0c1f }
        r6 = -r6;
        r6 = java.lang.Math.max(r9, r6);	 Catch:{ Exception -> 0x0c1f }
        r1.descriptionX = r6;	 Catch:{ Exception -> 0x0c1f }
    L_0x0ba1:
        r13 = 1;
    L_0x0ba2:
        r0 = r0 + 1;
        goto L_0x0b7a;
    L_0x0ba5:
        r0 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0c1f }
        r0 = r0.getWidth();	 Catch:{ Exception -> 0x0c1f }
        r6 = r5;
        r5 = r3;
        r3 = 0;
    L_0x0bae:
        r9 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0c1c }
        r9 = r9.getLineCount();	 Catch:{ Exception -> 0x0c1c }
        if (r3 >= r9) goto L_0x0c1a;
    L_0x0bb6:
        r9 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0c1c }
        r9 = r9.getLineLeft(r3);	 Catch:{ Exception -> 0x0c1c }
        r9 = (double) r9;	 Catch:{ Exception -> 0x0c1c }
        r9 = java.lang.Math.ceil(r9);	 Catch:{ Exception -> 0x0c1c }
        r9 = (int) r9;	 Catch:{ Exception -> 0x0c1c }
        if (r9 != 0) goto L_0x0bcb;
    L_0x0bc4:
        r10 = r1.descriptionX;	 Catch:{ Exception -> 0x0c1c }
        if (r10 == 0) goto L_0x0bcb;
    L_0x0bc8:
        r10 = 0;
        r1.descriptionX = r10;	 Catch:{ Exception -> 0x0c1c }
    L_0x0bcb:
        if (r9 == 0) goto L_0x0bd2;
    L_0x0bcd:
        r10 = r0 - r9;
    L_0x0bcf:
        r30 = r13;
        goto L_0x0be8;
    L_0x0bd2:
        if (r13 == 0) goto L_0x0bd6;
    L_0x0bd4:
        r10 = r0;
        goto L_0x0bcf;
    L_0x0bd6:
        r10 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0c1c }
        r10 = r10.getLineWidth(r3);	 Catch:{ Exception -> 0x0c1c }
        r30 = r13;
        r13 = (double) r10;	 Catch:{ Exception -> 0x0c1c }
        r13 = java.lang.Math.ceil(r13);	 Catch:{ Exception -> 0x0c1c }
        r10 = (int) r13;	 Catch:{ Exception -> 0x0c1c }
        r10 = java.lang.Math.min(r10, r0);	 Catch:{ Exception -> 0x0c1c }
    L_0x0be8:
        if (r3 < r11) goto L_0x0bf2;
    L_0x0bea:
        if (r11 == 0) goto L_0x0bf9;
    L_0x0bec:
        if (r9 == 0) goto L_0x0bf9;
    L_0x0bee:
        r9 = r1.isSmallImage;	 Catch:{ Exception -> 0x0c1c }
        if (r9 == 0) goto L_0x0bf9;
    L_0x0bf2:
        r9 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);	 Catch:{ Exception -> 0x0c1c }
        r10 = r10 + r9;
    L_0x0bf9:
        r10 = r10 + r4;
        if (r6 >= r10) goto L_0x0c0f;
    L_0x0bfc:
        if (r12 == 0) goto L_0x0c05;
    L_0x0bfe:
        r9 = r1.titleX;	 Catch:{ Exception -> 0x0c1c }
        r13 = r10 - r6;
        r9 = r9 + r13;
        r1.titleX = r9;	 Catch:{ Exception -> 0x0c1c }
    L_0x0c05:
        if (r8 == 0) goto L_0x0c0e;
    L_0x0c07:
        r9 = r1.authorX;	 Catch:{ Exception -> 0x0c1c }
        r6 = r10 - r6;
        r9 = r9 + r6;
        r1.authorX = r9;	 Catch:{ Exception -> 0x0c1c }
    L_0x0c0e:
        r6 = r10;
    L_0x0c0f:
        r5 = java.lang.Math.max(r5, r10);	 Catch:{ Exception -> 0x0c1c }
        r3 = r3 + 1;
        r14 = r60;
        r13 = r30;
        goto L_0x0bae;
    L_0x0c1a:
        r3 = r5;
        goto L_0x0c23;
    L_0x0c1c:
        r0 = move-exception;
        r3 = r5;
        goto L_0x0c20;
    L_0x0c1f:
        r0 = move-exception;
    L_0x0c20:
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0c23:
        if (r15 == 0) goto L_0x0c36;
    L_0x0c25:
        r0 = r1.descriptionLayout;
        if (r0 == 0) goto L_0x0c32;
    L_0x0c29:
        if (r0 == 0) goto L_0x0c36;
    L_0x0c2b:
        r0 = r0.getLineCount();
        r5 = 1;
        if (r0 != r5) goto L_0x0c36;
    L_0x0c32:
        r5 = 0;
        r1.isSmallImage = r5;
        r15 = 0;
    L_0x0c36:
        if (r15 == 0) goto L_0x0c3e;
    L_0x0c38:
        r0 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
    L_0x0c3e:
        if (r44 == 0) goto L_0x10bf;
    L_0x0c40:
        r0 = org.telegram.messenger.MessageObject.isRoundVideoDocument(r44);
        if (r0 == 0) goto L_0x0c67;
    L_0x0c46:
        r13 = r44;
        r0 = r13.thumbs;
        r5 = 90;
        r0 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r0, r5);
        r1.currentPhotoObject = r0;
        r1.photoParentObject = r13;
        r1.documentAttach = r13;
        r0 = 7;
        r1.documentAttachType = r0;
        r14 = r60;
    L_0x0c5b:
        r8 = r29;
        r11 = r39;
        r9 = r41;
        r6 = r42;
        r5 = r43;
        goto L_0x1124;
    L_0x0c67:
        r13 = r44;
        r0 = org.telegram.messenger.MessageObject.isGifDocument(r13);
        if (r0 == 0) goto L_0x0cee;
    L_0x0c6f:
        r0 = r60.isGame();
        if (r0 != 0) goto L_0x0c80;
    L_0x0c75:
        r0 = org.telegram.messenger.SharedConfig.autoplayGifs;
        if (r0 != 0) goto L_0x0c80;
    L_0x0c79:
        r14 = r60;
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r14.gifState = r8;
        goto L_0x0c84;
    L_0x0c80:
        r14 = r60;
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x0c84:
        r0 = r1.photoImage;
        r5 = r14.gifState;
        r5 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1));
        if (r5 == 0) goto L_0x0c8e;
    L_0x0c8c:
        r5 = 1;
        goto L_0x0c8f;
    L_0x0c8e:
        r5 = 0;
    L_0x0c8f:
        r0.setAllowStartAnimation(r5);
        r0 = r13.thumbs;
        r5 = 90;
        r0 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r0, r5);
        r1.currentPhotoObject = r0;
        r1.photoParentObject = r13;
        r0 = r1.currentPhotoObject;
        if (r0 == 0) goto L_0x0ce7;
    L_0x0ca2:
        r5 = r0.f465w;
        if (r5 == 0) goto L_0x0caa;
    L_0x0ca6:
        r0 = r0.f464h;
        if (r0 != 0) goto L_0x0ce7;
    L_0x0caa:
        r0 = 0;
    L_0x0cab:
        r5 = r13.attributes;
        r5 = r5.size();
        if (r0 >= r5) goto L_0x0cd1;
    L_0x0cb3:
        r5 = r13.attributes;
        r5 = r5.get(r0);
        r5 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r5;
        r6 = r5 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
        if (r6 != 0) goto L_0x0cc7;
    L_0x0cbf:
        r6 = r5 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
        if (r6 == 0) goto L_0x0cc4;
    L_0x0cc3:
        goto L_0x0cc7;
    L_0x0cc4:
        r0 = r0 + 1;
        goto L_0x0cab;
    L_0x0cc7:
        r0 = r1.currentPhotoObject;
        r6 = r5.f444w;
        r0.f465w = r6;
        r5 = r5.f443h;
        r0.f464h = r5;
    L_0x0cd1:
        r0 = r1.currentPhotoObject;
        r5 = r0.f465w;
        if (r5 == 0) goto L_0x0cdb;
    L_0x0cd7:
        r0 = r0.f464h;
        if (r0 != 0) goto L_0x0ce7;
    L_0x0cdb:
        r0 = r1.currentPhotoObject;
        r5 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r0.f464h = r5;
        r0.f465w = r5;
    L_0x0ce7:
        r1.documentAttach = r13;
        r5 = 2;
        r1.documentAttachType = r5;
        goto L_0x0c5b;
    L_0x0cee:
        r14 = r60;
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = org.telegram.messenger.MessageObject.isVideoDocument(r13);
        if (r0 == 0) goto L_0x0dc0;
    L_0x0cf8:
        if (r43 == 0) goto L_0x0d16;
    L_0x0cfa:
        r5 = r43;
        r0 = r5.sizes;
        r6 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        r9 = 1;
        r0 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r0, r6, r9);
        r1.currentPhotoObject = r0;
        r0 = r5.sizes;
        r6 = 40;
        r0 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r0, r6);
        r1.currentPhotoObjectThumb = r0;
        r1.photoParentObject = r5;
        goto L_0x0d18;
    L_0x0d16:
        r5 = r43;
    L_0x0d18:
        r0 = r1.currentPhotoObject;
        if (r0 != 0) goto L_0x0d32;
    L_0x0d1c:
        r0 = r13.thumbs;
        r6 = 320; // 0x140 float:4.48E-43 double:1.58E-321;
        r0 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r0, r6);
        r1.currentPhotoObject = r0;
        r0 = r13.thumbs;
        r6 = 40;
        r0 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r0, r6);
        r1.currentPhotoObjectThumb = r0;
        r1.photoParentObject = r13;
    L_0x0d32:
        r0 = r1.currentPhotoObject;
        r6 = r1.currentPhotoObjectThumb;
        if (r0 != r6) goto L_0x0d3b;
    L_0x0d38:
        r6 = 0;
        r1.currentPhotoObjectThumb = r6;
    L_0x0d3b:
        r0 = r1.currentPhotoObject;
        if (r0 != 0) goto L_0x0d53;
    L_0x0d3f:
        r0 = new org.telegram.tgnet.TLRPC$TL_photoSize;
        r0.<init>();
        r1.currentPhotoObject = r0;
        r0 = r1.currentPhotoObject;
        r6 = "s";
        r0.type = r6;
        r6 = new org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
        r6.<init>();
        r0.location = r6;
    L_0x0d53:
        r0 = r1.currentPhotoObject;
        if (r0 == 0) goto L_0x0dbb;
    L_0x0d57:
        r6 = r0.f465w;
        if (r6 == 0) goto L_0x0d63;
    L_0x0d5b:
        r6 = r0.f464h;
        if (r6 == 0) goto L_0x0d63;
    L_0x0d5f:
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_photoStrippedSize;
        if (r0 == 0) goto L_0x0dbb;
    L_0x0d63:
        r0 = 0;
    L_0x0d64:
        r6 = r13.attributes;
        r6 = r6.size();
        if (r0 >= r6) goto L_0x0da5;
    L_0x0d6c:
        r6 = r13.attributes;
        r6 = r6.get(r0);
        r6 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r6;
        r9 = r6 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
        if (r9 == 0) goto L_0x0da2;
    L_0x0d78:
        r0 = r1.currentPhotoObject;
        r9 = r0 instanceof org.telegram.tgnet.TLRPC.TL_photoStrippedSize;
        if (r9 == 0) goto L_0x0d99;
    L_0x0d7e:
        r0 = r6.f444w;
        r0 = java.lang.Math.max(r0, r0);
        r0 = (float) r0;
        r9 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r0 = r0 / r9;
        r9 = r1.currentPhotoObject;
        r10 = r6.f444w;
        r10 = (float) r10;
        r10 = r10 / r0;
        r10 = (int) r10;
        r9.f465w = r10;
        r6 = r6.f443h;
        r6 = (float) r6;
        r6 = r6 / r0;
        r0 = (int) r6;
        r9.f464h = r0;
        goto L_0x0da5;
    L_0x0d99:
        r9 = r6.f444w;
        r0.f465w = r9;
        r6 = r6.f443h;
        r0.f464h = r6;
        goto L_0x0da5;
    L_0x0da2:
        r0 = r0 + 1;
        goto L_0x0d64;
    L_0x0da5:
        r0 = r1.currentPhotoObject;
        r6 = r0.f465w;
        if (r6 == 0) goto L_0x0daf;
    L_0x0dab:
        r0 = r0.f464h;
        if (r0 != 0) goto L_0x0dbb;
    L_0x0daf:
        r0 = r1.currentPhotoObject;
        r6 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r0.f464h = r6;
        r0.f465w = r6;
    L_0x0dbb:
        r6 = 0;
        r1.createDocumentLayout(r6, r14);
        goto L_0x0e1e;
    L_0x0dc0:
        r5 = r43;
        r0 = org.telegram.messenger.MessageObject.isStickerDocument(r13);
        if (r0 == 0) goto L_0x0e28;
    L_0x0dc8:
        r0 = r13.thumbs;
        r6 = 90;
        r0 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r0, r6);
        r1.currentPhotoObject = r0;
        r1.photoParentObject = r13;
        r0 = r1.currentPhotoObject;
        if (r0 == 0) goto L_0x0e19;
    L_0x0dd8:
        r6 = r0.f465w;
        if (r6 == 0) goto L_0x0de0;
    L_0x0ddc:
        r0 = r0.f464h;
        if (r0 != 0) goto L_0x0e19;
    L_0x0de0:
        r0 = 0;
    L_0x0de1:
        r6 = r13.attributes;
        r6 = r6.size();
        if (r0 >= r6) goto L_0x0e03;
    L_0x0de9:
        r6 = r13.attributes;
        r6 = r6.get(r0);
        r6 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r6;
        r9 = r6 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
        if (r9 == 0) goto L_0x0e00;
    L_0x0df5:
        r0 = r1.currentPhotoObject;
        r9 = r6.f444w;
        r0.f465w = r9;
        r6 = r6.f443h;
        r0.f464h = r6;
        goto L_0x0e03;
    L_0x0e00:
        r0 = r0 + 1;
        goto L_0x0de1;
    L_0x0e03:
        r0 = r1.currentPhotoObject;
        r6 = r0.f465w;
        if (r6 == 0) goto L_0x0e0d;
    L_0x0e09:
        r0 = r0.f464h;
        if (r0 != 0) goto L_0x0e19;
    L_0x0e0d:
        r0 = r1.currentPhotoObject;
        r6 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r0.f464h = r6;
        r0.f465w = r6;
    L_0x0e19:
        r1.documentAttach = r13;
        r6 = 6;
        r1.documentAttachType = r6;
    L_0x0e1e:
        r8 = r29;
        r11 = r39;
        r9 = r41;
        r6 = r42;
        goto L_0x1124;
    L_0x0e28:
        r6 = 6;
        r0 = r1.drawInstantViewType;
        if (r0 != r6) goto L_0x0eb6;
    L_0x0e2d:
        r0 = r13.thumbs;
        r6 = 320; // 0x140 float:4.48E-43 double:1.58E-321;
        r0 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r0, r6);
        r1.currentPhotoObject = r0;
        r1.photoParentObject = r13;
        r0 = r1.currentPhotoObject;
        if (r0 == 0) goto L_0x0e7e;
    L_0x0e3d:
        r6 = r0.f465w;
        if (r6 == 0) goto L_0x0e45;
    L_0x0e41:
        r0 = r0.f464h;
        if (r0 != 0) goto L_0x0e7e;
    L_0x0e45:
        r0 = 0;
    L_0x0e46:
        r6 = r13.attributes;
        r6 = r6.size();
        if (r0 >= r6) goto L_0x0e68;
    L_0x0e4e:
        r6 = r13.attributes;
        r6 = r6.get(r0);
        r6 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r6;
        r9 = r6 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
        if (r9 == 0) goto L_0x0e65;
    L_0x0e5a:
        r0 = r1.currentPhotoObject;
        r9 = r6.f444w;
        r0.f465w = r9;
        r6 = r6.f443h;
        r0.f464h = r6;
        goto L_0x0e68;
    L_0x0e65:
        r0 = r0 + 1;
        goto L_0x0e46;
    L_0x0e68:
        r0 = r1.currentPhotoObject;
        r6 = r0.f465w;
        if (r6 == 0) goto L_0x0e72;
    L_0x0e6e:
        r0 = r0.f464h;
        if (r0 != 0) goto L_0x0e7e;
    L_0x0e72:
        r0 = r1.currentPhotoObject;
        r6 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r0.f464h = r6;
        r0.f465w = r6;
    L_0x0e7e:
        r1.documentAttach = r13;
        r6 = 8;
        r1.documentAttachType = r6;
        r0 = r1.documentAttach;
        r0 = r0.size;
        r9 = (long) r0;
        r0 = org.telegram.messenger.AndroidUtilities.formatFileSize(r9);
        r6 = org.telegram.p004ui.ActionBar.Theme.chat_durationPaint;
        r6 = r6.measureText(r0);
        r9 = (double) r6;
        r9 = java.lang.Math.ceil(r9);
        r6 = (int) r9;
        r1.durationWidth = r6;
        r6 = new android.text.StaticLayout;
        r32 = org.telegram.p004ui.ActionBar.Theme.chat_durationPaint;
        r9 = r1.durationWidth;
        r34 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r35 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r36 = 0;
        r37 = 0;
        r30 = r6;
        r31 = r0;
        r33 = r9;
        r30.<init>(r31, r32, r33, r34, r35, r36, r37);
        r1.videoInfoLayout = r6;
        goto L_0x0e1e;
    L_0x0eb6:
        r11 = r39;
        r6 = r42;
        r1.calcBackgroundWidth(r6, r11, r3);
        r0 = r1.backgroundWidth;
        r9 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r9 = r9 + r6;
        if (r0 >= r9) goto L_0x0ed1;
    L_0x0ec8:
        r0 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r0 = r0 + r6;
        r1.backgroundWidth = r0;
    L_0x0ed1:
        r0 = org.telegram.messenger.MessageObject.isVoiceDocument(r13);
        if (r0 == 0) goto L_0x0f7f;
    L_0x0ed7:
        r0 = r1.backgroundWidth;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r0 = r0 - r9;
        r1.createDocumentLayout(r0, r14);
        r0 = r1.currentMessageObject;
        r0 = r0.textHeight;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r0 = r0 + r9;
        r9 = r1.linkPreviewHeight;
        r0 = r0 + r9;
        r1.mediaOffsetY = r0;
        r0 = r1.totalHeight;
        r9 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r0 = r0 + r9;
        r1.totalHeight = r0;
        r0 = r1.linkPreviewHeight;
        r9 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r0 = r0 + r9;
        r1.linkPreviewHeight = r0;
        r0 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r0 = r6 - r0;
        r6 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r6 == 0) goto L_0x0f47;
    L_0x0f13:
        r6 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r9 = r1.isChat;
        if (r9 == 0) goto L_0x0f2a;
    L_0x0f1b:
        r9 = r60.needDrawAvatar();
        if (r9 == 0) goto L_0x0f2a;
    L_0x0f21:
        r9 = r60.isOutOwner();
        if (r9 != 0) goto L_0x0f2a;
    L_0x0f27:
        r10 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        goto L_0x0f2b;
    L_0x0f2a:
        r10 = 0;
    L_0x0f2b:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r6 = r6 - r9;
        r9 = 1130102784; // 0x435c0000 float:220.0 double:5.58344962E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r6 = java.lang.Math.min(r6, r9);
        r9 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r6 = r6 - r9;
        r6 = r6 + r4;
        r3 = java.lang.Math.max(r3, r6);
        goto L_0x0f7a;
    L_0x0f47:
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.x;
        r9 = r1.isChat;
        if (r9 == 0) goto L_0x0f5e;
    L_0x0f4f:
        r9 = r60.needDrawAvatar();
        if (r9 == 0) goto L_0x0f5e;
    L_0x0f55:
        r9 = r60.isOutOwner();
        if (r9 != 0) goto L_0x0f5e;
    L_0x0f5b:
        r10 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        goto L_0x0f5f;
    L_0x0f5e:
        r10 = 0;
    L_0x0f5f:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r6 = r6 - r9;
        r9 = 1130102784; // 0x435c0000 float:220.0 double:5.58344962E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r6 = java.lang.Math.min(r6, r9);
        r9 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r6 = r6 - r9;
        r6 = r6 + r4;
        r3 = java.lang.Math.max(r3, r6);
    L_0x0f7a:
        r1.calcBackgroundWidth(r0, r11, r3);
        goto L_0x100e;
    L_0x0f7f:
        r0 = org.telegram.messenger.MessageObject.isMusicDocument(r13);
        if (r0 == 0) goto L_0x1014;
    L_0x0f85:
        r0 = r1.backgroundWidth;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r0 = r0 - r9;
        r0 = r1.createDocumentLayout(r0, r14);
        r9 = r1.currentMessageObject;
        r9 = r9.textHeight;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r9 = r9 + r10;
        r10 = r1.linkPreviewHeight;
        r9 = r9 + r10;
        r1.mediaOffsetY = r9;
        r9 = r1.totalHeight;
        r10 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r9 = r9 + r10;
        r1.totalHeight = r9;
        r9 = r1.linkPreviewHeight;
        r10 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r9 = r9 + r10;
        r1.linkPreviewHeight = r9;
        r9 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r6 = r6 - r9;
        r0 = r0 + r4;
        r9 = 1119617024; // 0x42bc0000 float:94.0 double:5.53164308E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r0 = r0 + r9;
        r0 = java.lang.Math.max(r3, r0);
        r3 = r1.songLayout;
        if (r3 == 0) goto L_0x0fe8;
    L_0x0fcb:
        r3 = r3.getLineCount();
        if (r3 <= 0) goto L_0x0fe8;
    L_0x0fd1:
        r0 = (float) r0;
        r3 = r1.songLayout;
        r9 = 0;
        r3 = r3.getLineWidth(r9);
        r9 = (float) r4;
        r3 = r3 + r9;
        r9 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r9 = (float) r9;
        r3 = r3 + r9;
        r0 = java.lang.Math.max(r0, r3);
        r0 = (int) r0;
    L_0x0fe8:
        r3 = r1.performerLayout;
        if (r3 == 0) goto L_0x1009;
    L_0x0fec:
        r3 = r3.getLineCount();
        if (r3 <= 0) goto L_0x1009;
    L_0x0ff2:
        r0 = (float) r0;
        r3 = r1.performerLayout;
        r9 = 0;
        r3 = r3.getLineWidth(r9);
        r9 = (float) r4;
        r3 = r3 + r9;
        r9 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r9 = (float) r9;
        r3 = r3 + r9;
        r0 = java.lang.Math.max(r0, r3);
        r0 = (int) r0;
    L_0x1009:
        r3 = r0;
        r1.calcBackgroundWidth(r6, r11, r3);
        r0 = r6;
    L_0x100e:
        r8 = r29;
        r9 = r41;
        goto L_0x1125;
    L_0x1014:
        r0 = r1.backgroundWidth;
        r9 = 1126694912; // 0x43280000 float:168.0 double:5.566612494E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r0 = r0 - r9;
        r1.createDocumentLayout(r0, r14);
        r9 = 1;
        r1.drawImageButton = r9;
        r0 = r1.drawPhotoImage;
        if (r0 == 0) goto L_0x1055;
    L_0x1027:
        r0 = r1.totalHeight;
        r9 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r0 = r0 + r9;
        r1.totalHeight = r0;
        r0 = r1.linkPreviewHeight;
        r9 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r0 = r0 + r9;
        r1.linkPreviewHeight = r0;
        r0 = r1.photoImage;
        r9 = r1.totalHeight;
        r10 = r1.namesOffset;
        r9 = r9 + r10;
        r10 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r12 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r8 = 0;
        r0.setImageCoords(r8, r9, r10, r12);
        goto L_0x10bb;
    L_0x1055:
        r0 = r1.currentMessageObject;
        r0 = r0.textHeight;
        r8 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r0 = r0 + r8;
        r8 = r1.linkPreviewHeight;
        r0 = r0 + r8;
        r1.mediaOffsetY = r0;
        r0 = r1.photoImage;
        r8 = r1.totalHeight;
        r9 = r1.namesOffset;
        r8 = r8 + r9;
        r9 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r8 = r8 - r9;
        r9 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r10 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r12 = 0;
        r0.setImageCoords(r12, r8, r9, r10);
        r0 = r1.totalHeight;
        r8 = 1115684864; // 0x42800000 float:64.0 double:5.51221563E-315;
        r8 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r0 = r0 + r8;
        r1.totalHeight = r0;
        r0 = r1.linkPreviewHeight;
        r8 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r0 = r0 + r9;
        r1.linkPreviewHeight = r0;
        r0 = r1.docTitleLayout;
        if (r0 == 0) goto L_0x10bb;
    L_0x109b:
        r0 = r0.getLineCount();
        r8 = 1;
        if (r0 <= r8) goto L_0x10bb;
    L_0x10a2:
        r0 = r1.docTitleLayout;
        r0 = r0.getLineCount();
        r0 = r0 - r8;
        r8 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r8 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r0 = r0 * r8;
        r8 = r1.totalHeight;
        r8 = r8 + r0;
        r1.totalHeight = r8;
        r8 = r1.linkPreviewHeight;
        r8 = r8 + r0;
        r1.linkPreviewHeight = r8;
    L_0x10bb:
        r8 = r29;
        goto L_0x1122;
    L_0x10bf:
        r14 = r60;
        r11 = r39;
        r6 = r42;
        r5 = r43;
        r13 = r44;
        if (r5 == 0) goto L_0x110d;
    L_0x10cb:
        if (r29 == 0) goto L_0x10d9;
    L_0x10cd:
        r0 = "photo";
        r8 = r29;
        r0 = r8.equals(r0);
        if (r0 == 0) goto L_0x10db;
    L_0x10d7:
        r0 = 1;
        goto L_0x10dc;
    L_0x10d9:
        r8 = r29;
    L_0x10db:
        r0 = 0;
    L_0x10dc:
        r9 = r14.photoThumbs;
        if (r0 != 0) goto L_0x10e5;
    L_0x10e0:
        if (r15 != 0) goto L_0x10e3;
    L_0x10e2:
        goto L_0x10e5;
    L_0x10e3:
        r10 = r2;
        goto L_0x10e9;
    L_0x10e5:
        r10 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
    L_0x10e9:
        r12 = r0 ^ 1;
        r9 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r9, r10, r12);
        r1.currentPhotoObject = r9;
        r9 = r14.photoThumbsObject;
        r1.photoParentObject = r9;
        r9 = 1;
        r0 = r0 ^ r9;
        r1.checkOnlyButtonPressed = r0;
        r0 = r14.photoThumbs;
        r9 = 40;
        r0 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r0, r9);
        r1.currentPhotoObjectThumb = r0;
        r0 = r1.currentPhotoObjectThumb;
        r9 = r1.currentPhotoObject;
        if (r0 != r9) goto L_0x1122;
    L_0x1109:
        r9 = 0;
        r1.currentPhotoObjectThumb = r9;
        goto L_0x1122;
    L_0x110d:
        r8 = r29;
        if (r41 == 0) goto L_0x1122;
    L_0x1111:
        r9 = r41;
        r0 = r9.mime_type;
        r10 = "image/";
        r0 = r0.startsWith(r10);
        if (r0 != 0) goto L_0x111e;
    L_0x111d:
        r9 = 0;
    L_0x111e:
        r10 = 0;
        r1.drawImageButton = r10;
        goto L_0x1124;
    L_0x1122:
        r9 = r41;
    L_0x1124:
        r0 = r6;
    L_0x1125:
        r6 = r1.documentAttachType;
        r10 = 5;
        if (r6 == r10) goto L_0x1759;
    L_0x112a:
        r10 = 3;
        if (r6 == r10) goto L_0x1759;
    L_0x112d:
        r10 = 1;
        if (r6 == r10) goto L_0x1759;
    L_0x1130:
        r6 = r1.currentPhotoObject;
        if (r6 != 0) goto L_0x115b;
    L_0x1134:
        if (r9 == 0) goto L_0x1137;
    L_0x1136:
        goto L_0x115b;
    L_0x1137:
        r2 = r1.photoImage;
        r4 = 0;
        r2.setImageBitmap(r4);
        r2 = r1.linkPreviewHeight;
        r4 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r2 = r2 - r4;
        r1.linkPreviewHeight = r2;
        r2 = r1.totalHeight;
        r4 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r2 = r2 + r4;
        r1.totalHeight = r2;
        r56 = r11;
        r13 = 0;
        r15 = 1;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x1654;
    L_0x115b:
        if (r5 == 0) goto L_0x115f;
    L_0x115d:
        if (r15 == 0) goto L_0x1187;
    L_0x115f:
        if (r8 == 0) goto L_0x1189;
    L_0x1161:
        r5 = "photo";
        r5 = r8.equals(r5);
        if (r5 != 0) goto L_0x1187;
    L_0x1169:
        r5 = "document";
        r5 = r8.equals(r5);
        if (r5 == 0) goto L_0x1176;
    L_0x1171:
        r5 = r1.documentAttachType;
        r6 = 6;
        if (r5 != r6) goto L_0x1187;
    L_0x1176:
        r5 = "gif";
        r5 = r8.equals(r5);
        if (r5 != 0) goto L_0x1187;
    L_0x117e:
        r5 = r1.documentAttachType;
        r6 = 4;
        if (r5 == r6) goto L_0x1187;
    L_0x1183:
        r6 = 8;
        if (r5 != r6) goto L_0x1189;
    L_0x1187:
        r5 = 1;
        goto L_0x118a;
    L_0x1189:
        r5 = 0;
    L_0x118a:
        r1.drawImageButton = r5;
        r5 = r1.linkPreviewHeight;
        if (r5 == 0) goto L_0x11a0;
    L_0x1190:
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r5 = r5 + r6;
        r1.linkPreviewHeight = r5;
        r5 = r1.totalHeight;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r5 = r5 + r6;
        r1.totalHeight = r5;
    L_0x11a0:
        r5 = r1.imageBackgroundSideColor;
        if (r5 == 0) goto L_0x11ab;
    L_0x11a4:
        r2 = 1129316352; // 0x43500000 float:208.0 double:5.57956413E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        goto L_0x11e2;
    L_0x11ab:
        r5 = r1.currentPhotoObject;
        r6 = r5 instanceof org.telegram.tgnet.TLRPC.TL_photoSizeEmpty;
        if (r6 == 0) goto L_0x11b7;
    L_0x11b1:
        r5 = r5.f465w;
        if (r5 == 0) goto L_0x11b7;
    L_0x11b5:
        r2 = r5;
        goto L_0x11e2;
    L_0x11b7:
        r5 = r1.documentAttachType;
        r6 = 6;
        if (r5 == r6) goto L_0x11cd;
    L_0x11bc:
        r6 = 8;
        if (r5 != r6) goto L_0x11c1;
    L_0x11c0:
        goto L_0x11cd;
    L_0x11c1:
        r6 = 7;
        if (r5 != r6) goto L_0x11e2;
    L_0x11c4:
        r2 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r5 = r1.photoImage;
        r6 = 1;
        r5.setAllowDecodeSingleFrame(r6);
        goto L_0x11e2;
    L_0x11cd:
        r2 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r2 == 0) goto L_0x11d8;
    L_0x11d3:
        r2 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        goto L_0x11dc;
    L_0x11d8:
        r2 = org.telegram.messenger.AndroidUtilities.displaySize;
        r2 = r2.x;
    L_0x11dc:
        r2 = (float) r2;
        r5 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r2 = r2 * r5;
        r2 = (int) r2;
    L_0x11e2:
        r5 = r1.hasInvoicePreview;
        if (r5 == 0) goto L_0x11ed;
    L_0x11e6:
        r5 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        goto L_0x11ee;
    L_0x11ed:
        r12 = 0;
    L_0x11ee:
        r5 = r2 - r12;
        r5 = r5 + r4;
        r29 = java.lang.Math.max(r3, r5);
        r3 = r1.currentPhotoObject;
        if (r3 == 0) goto L_0x1203;
    L_0x11f9:
        r10 = -1;
        r3.size = r10;
        r3 = r1.currentPhotoObjectThumb;
        if (r3 == 0) goto L_0x1206;
    L_0x1200:
        r3.size = r10;
        goto L_0x1206;
    L_0x1203:
        r10 = -1;
        r9.size = r10;
    L_0x1206:
        r3 = r1.imageBackgroundSideColor;
        if (r3 == 0) goto L_0x1214;
    L_0x120a:
        r3 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = r29 - r3;
        r1.imageBackgroundSideWidth = r3;
    L_0x1214:
        if (r15 != 0) goto L_0x129e;
    L_0x1216:
        r3 = r1.documentAttachType;
        r4 = 7;
        if (r3 != r4) goto L_0x121d;
    L_0x121b:
        goto L_0x129e;
    L_0x121d:
        r3 = r1.hasGamePreview;
        if (r3 != 0) goto L_0x1289;
    L_0x1221:
        r3 = r1.hasInvoicePreview;
        if (r3 == 0) goto L_0x1226;
    L_0x1225:
        goto L_0x1289;
    L_0x1226:
        r3 = r1.currentPhotoObject;
        r4 = r3.f465w;
        r3 = r3.f464h;
        r4 = (float) r4;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r2 = r2 - r5;
        r2 = (float) r2;
        r2 = r4 / r2;
        r4 = r4 / r2;
        r4 = (int) r4;
        r3 = (float) r3;
        r3 = r3 / r2;
        r2 = (int) r3;
        if (r7 == 0) goto L_0x125c;
    L_0x123c:
        if (r7 == 0) goto L_0x124f;
    L_0x123e:
        r3 = r7.toLowerCase();
        r5 = "instagram";
        r3 = r3.equals(r5);
        if (r3 != 0) goto L_0x124f;
    L_0x124a:
        r3 = r1.documentAttachType;
        if (r3 != 0) goto L_0x124f;
    L_0x124e:
        goto L_0x125c;
    L_0x124f:
        r3 = org.telegram.messenger.AndroidUtilities.displaySize;
        r3 = r3.y;
        r5 = r3 / 2;
        if (r2 <= r5) goto L_0x125a;
    L_0x1257:
        r5 = 2;
        r2 = r3 / 2;
    L_0x125a:
        r12 = 3;
        goto L_0x1267;
    L_0x125c:
        r3 = org.telegram.messenger.AndroidUtilities.displaySize;
        r3 = r3.y;
        r5 = r3 / 3;
        if (r2 <= r5) goto L_0x125a;
    L_0x1264:
        r12 = 3;
        r2 = r3 / 3;
    L_0x1267:
        r3 = r1.imageBackgroundSideColor;
        if (r3 == 0) goto L_0x127a;
    L_0x126b:
        r2 = (float) r2;
        r3 = 1126170624; // 0x43200000 float:160.0 double:5.564022167E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = (float) r3;
        r3 = r2 / r3;
        r4 = (float) r4;
        r4 = r4 / r3;
        r4 = (int) r4;
        r2 = r2 / r3;
        r2 = (int) r2;
    L_0x127a:
        r3 = 1114636288; // 0x42700000 float:60.0 double:5.507034975E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        if (r2 >= r3) goto L_0x12a0;
    L_0x1282:
        r2 = 1114636288; // 0x42700000 float:60.0 double:5.507034975E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        goto L_0x12a0;
    L_0x1289:
        r12 = 3;
        r3 = 640; // 0x280 float:8.97E-43 double:3.16E-321;
        r4 = 360; // 0x168 float:5.04E-43 double:1.78E-321;
        r3 = (float) r3;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r2 = r2 - r5;
        r2 = (float) r2;
        r2 = r3 / r2;
        r3 = r3 / r2;
        r3 = (int) r3;
        r4 = (float) r4;
        r4 = r4 / r2;
        r2 = (int) r4;
        r4 = r3;
        goto L_0x12a0;
    L_0x129e:
        r12 = 3;
        r4 = r2;
    L_0x12a0:
        r3 = r1.isSmallImage;
        if (r3 == 0) goto L_0x12d5;
    L_0x12a4:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r5 = r5 + r38;
        r6 = r1.linkPreviewHeight;
        if (r5 <= r6) goto L_0x12cb;
    L_0x12b0:
        r5 = r1.totalHeight;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r6 = r6 + r38;
        r7 = r1.linkPreviewHeight;
        r6 = r6 - r7;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r6 = r6 + r7;
        r5 = r5 + r6;
        r1.totalHeight = r5;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = r3 + r38;
        r1.linkPreviewHeight = r3;
    L_0x12cb:
        r3 = r1.linkPreviewHeight;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r3 = r3 - r5;
        r1.linkPreviewHeight = r3;
        goto L_0x12e6;
    L_0x12d5:
        r3 = r1.totalHeight;
        r5 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r5 = r5 + r2;
        r3 = r3 + r5;
        r1.totalHeight = r3;
        r3 = r1.linkPreviewHeight;
        r3 = r3 + r2;
        r1.linkPreviewHeight = r3;
    L_0x12e6:
        r3 = r1.documentAttachType;
        r5 = 8;
        if (r3 != r5) goto L_0x1303;
    L_0x12ec:
        r3 = r1.imageBackgroundSideColor;
        if (r3 != 0) goto L_0x1303;
    L_0x12f0:
        r3 = r1.photoImage;
        r5 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r5 = r29 - r5;
        r5 = java.lang.Math.max(r5, r4);
        r15 = 0;
        r3.setImageCoords(r15, r15, r5, r2);
        goto L_0x1309;
    L_0x1303:
        r15 = 0;
        r3 = r1.photoImage;
        r3.setImageCoords(r15, r15, r4, r2);
    L_0x1309:
        r3 = java.util.Locale.US;
        r7 = 2;
        r5 = new java.lang.Object[r7];
        r6 = java.lang.Integer.valueOf(r4);
        r5[r15] = r6;
        r6 = java.lang.Integer.valueOf(r2);
        r21 = 1;
        r5[r21] = r6;
        r6 = "%d_%d";
        r3 = java.lang.String.format(r3, r6, r5);
        r1.currentPhotoFilter = r3;
        r3 = java.util.Locale.US;
        r5 = new java.lang.Object[r7];
        r6 = java.lang.Integer.valueOf(r4);
        r5[r15] = r6;
        r6 = java.lang.Integer.valueOf(r2);
        r5[r21] = r6;
        r6 = "%d_%d_b";
        r3 = java.lang.String.format(r3, r6, r5);
        r1.currentPhotoFilterThumb = r3;
        if (r9 == 0) goto L_0x1365;
    L_0x133e:
        r2 = r1.photoImage;
        r3 = org.telegram.messenger.ImageLocation.getForWebFile(r9);
        r4 = r1.currentPhotoFilter;
        r5 = 0;
        r6 = 0;
        r9 = r9.size;
        r13 = 0;
        r21 = 1;
        r23 = 2;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r7 = r9;
        r10 = r8;
        r9 = -1;
        r8 = r13;
        r13 = -1;
        r15 = 2;
        r9 = r60;
        r55 = r10;
        r10 = r21;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10);
    L_0x1360:
        r56 = r11;
        r15 = 1;
        goto L_0x15d3;
    L_0x1365:
        r55 = r8;
        r15 = 2;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = r1.documentAttachType;
        r5 = 8;
        if (r3 != r5) goto L_0x13a7;
    L_0x1370:
        r2 = r14.mediaExists;
        if (r2 == 0) goto L_0x1391;
    L_0x1374:
        r2 = r1.photoImage;
        r3 = r1.documentAttach;
        r3 = org.telegram.messenger.ImageLocation.getForDocument(r3);
        r4 = r1.currentPhotoFilter;
        r5 = r1.currentPhotoObject;
        r5 = org.telegram.messenger.ImageLocation.getForDocument(r5, r13);
        r7 = 0;
        r13 = 1;
        r6 = "b1";
        r8 = "jpg";
        r9 = r60;
        r10 = r13;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x1360;
    L_0x1391:
        r2 = r1.photoImage;
        r3 = 0;
        r4 = 0;
        r5 = r1.currentPhotoObject;
        r5 = org.telegram.messenger.ImageLocation.getForDocument(r5, r13);
        r7 = 0;
        r10 = 1;
        r6 = "b1";
        r8 = "jpg";
        r9 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x1360;
    L_0x13a7:
        r5 = 6;
        if (r3 != r5) goto L_0x13cb;
    L_0x13aa:
        r2 = r1.photoImage;
        r3 = r1.documentAttach;
        r3 = org.telegram.messenger.ImageLocation.getForDocument(r3);
        r4 = r1.currentPhotoFilter;
        r5 = r1.currentPhotoObject;
        r6 = r1.documentAttach;
        r5 = org.telegram.messenger.ImageLocation.getForDocument(r5, r6);
        r6 = r1.documentAttach;
        r7 = r6.size;
        r10 = 1;
        r6 = "b1";
        r8 = "webp";
        r9 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x1360;
    L_0x13cb:
        r5 = 4;
        if (r3 != r5) goto L_0x1492;
    L_0x13ce:
        r2 = r1.photoImage;
        r3 = 1;
        r2.setNeedsQualityThumb(r3);
        r2 = r1.photoImage;
        r2.setShouldGenerateQualityThumb(r3);
        r2 = org.telegram.messenger.SharedConfig.autoplayVideo;
        if (r2 == 0) goto L_0x143f;
    L_0x13dd:
        r2 = r1.currentMessageObject;
        r2 = r2.mediaExists;
        if (r2 != 0) goto L_0x13f7;
    L_0x13e3:
        r2 = r60.canStreamVideo();
        if (r2 == 0) goto L_0x143f;
    L_0x13e9:
        r2 = r1.currentAccount;
        r2 = org.telegram.messenger.DownloadController.getInstance(r2);
        r3 = r1.currentMessageObject;
        r2 = r2.canDownloadMedia(r3);
        if (r2 == 0) goto L_0x143f;
    L_0x13f7:
        r2 = r1.photoImage;
        r13 = 1;
        r2.setAllowDecodeSingleFrame(r13);
        r2 = r1.photoImage;
        r2.setAllowStartAnimation(r13);
        r2 = r1.photoImage;
        r2.startAnimation();
        r2 = r1.photoImage;
        r3 = r1.documentAttach;
        r3 = org.telegram.messenger.ImageLocation.getForDocument(r3);
        r4 = r1.currentPhotoObject;
        r5 = r1.documentAttach;
        r5 = org.telegram.messenger.ImageLocation.getForDocument(r4, r5);
        r6 = r1.currentPhotoFilter;
        r4 = r1.currentPhotoObjectThumb;
        r7 = r1.documentAttach;
        r7 = org.telegram.messenger.ImageLocation.getForDocument(r4, r7);
        r8 = r1.currentPhotoFilterThumb;
        r9 = 0;
        r4 = r1.documentAttach;
        r10 = r4.size;
        r21 = 0;
        r22 = 0;
        r4 = "g";
        r12 = r11;
        r11 = r21;
        r56 = r12;
        r12 = r60;
        r15 = 1;
        r13 = r22;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);
        r1.autoPlayingMedia = r15;
        goto L_0x15d3;
    L_0x143f:
        r56 = r11;
        r15 = 1;
        r2 = r1.currentPhotoObjectThumb;
        if (r2 == 0) goto L_0x1466;
    L_0x1446:
        r2 = r1.photoImage;
        r3 = r1.currentPhotoObject;
        r4 = r1.documentAttach;
        r3 = org.telegram.messenger.ImageLocation.getForDocument(r3, r4);
        r4 = r1.currentPhotoFilter;
        r5 = r1.currentPhotoObjectThumb;
        r6 = r1.documentAttach;
        r5 = org.telegram.messenger.ImageLocation.getForDocument(r5, r6);
        r6 = r1.currentPhotoFilterThumb;
        r7 = 0;
        r8 = 0;
        r10 = 0;
        r9 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x15d3;
    L_0x1466:
        r2 = r1.photoImage;
        r3 = 0;
        r4 = 0;
        r5 = r1.currentPhotoObject;
        r6 = r1.documentAttach;
        r5 = org.telegram.messenger.ImageLocation.getForDocument(r5, r6);
        r6 = r1.currentPhotoObject;
        r7 = r6 instanceof org.telegram.tgnet.TLRPC.TL_photoStrippedSize;
        if (r7 != 0) goto L_0x1486;
    L_0x1478:
        r6 = r6.type;
        r7 = "s";
        r6 = r7.equals(r6);
        if (r6 == 0) goto L_0x1483;
    L_0x1482:
        goto L_0x1486;
    L_0x1483:
        r6 = r1.currentPhotoFilter;
        goto L_0x1488;
    L_0x1486:
        r6 = r1.currentPhotoFilterThumb;
    L_0x1488:
        r7 = 0;
        r8 = 0;
        r10 = 0;
        r9 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x15d3;
    L_0x1492:
        r56 = r11;
        r5 = 2;
        r15 = 1;
        if (r3 == r5) goto L_0x152c;
    L_0x1498:
        r5 = 7;
        if (r3 != r5) goto L_0x149d;
    L_0x149b:
        goto L_0x152c;
    L_0x149d:
        r3 = r14.mediaExists;
        r5 = r1.currentPhotoObject;
        r5 = org.telegram.messenger.FileLoader.getAttachFileName(r5);
        r6 = r1.hasGamePreview;
        if (r6 != 0) goto L_0x1509;
    L_0x14a9:
        if (r3 != 0) goto L_0x1509;
    L_0x14ab:
        r3 = r1.currentAccount;
        r3 = org.telegram.messenger.DownloadController.getInstance(r3);
        r6 = r1.currentMessageObject;
        r3 = r3.canDownloadMedia(r6);
        if (r3 != 0) goto L_0x1509;
    L_0x14b9:
        r3 = r1.currentAccount;
        r3 = org.telegram.messenger.FileLoader.getInstance(r3);
        r3 = r3.isLoadingFile(r5);
        if (r3 == 0) goto L_0x14c6;
    L_0x14c5:
        goto L_0x1509;
    L_0x14c6:
        r1.photoNotSet = r15;
        r3 = r1.currentPhotoObjectThumb;
        if (r3 == 0) goto L_0x1500;
    L_0x14cc:
        r5 = r1.photoImage;
        r6 = 0;
        r7 = 0;
        r8 = r1.photoParentObject;
        r8 = org.telegram.messenger.ImageLocation.getForObject(r3, r8);
        r3 = java.util.Locale.US;
        r9 = 2;
        r10 = new java.lang.Object[r9];
        r4 = java.lang.Integer.valueOf(r4);
        r12 = 0;
        r10[r12] = r4;
        r2 = java.lang.Integer.valueOf(r2);
        r10[r15] = r2;
        r2 = "%d_%d_b";
        r9 = java.lang.String.format(r3, r2, r10);
        r10 = 0;
        r11 = 0;
        r13 = 0;
        r2 = r5;
        r3 = r6;
        r4 = r7;
        r5 = r8;
        r6 = r9;
        r7 = r10;
        r8 = r11;
        r9 = r60;
        r10 = r13;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x15d3;
    L_0x1500:
        r12 = 0;
        r2 = r1.photoImage;
        r3 = 0;
        r2.setImageBitmap(r3);
        goto L_0x15d3;
    L_0x1509:
        r12 = 0;
        r1.photoNotSet = r12;
        r2 = r1.photoImage;
        r3 = r1.currentPhotoObject;
        r4 = r1.photoParentObject;
        r3 = org.telegram.messenger.ImageLocation.getForObject(r3, r4);
        r4 = r1.currentPhotoFilter;
        r5 = r1.currentPhotoObjectThumb;
        r6 = r1.photoParentObject;
        r5 = org.telegram.messenger.ImageLocation.getForObject(r5, r6);
        r6 = r1.currentPhotoFilterThumb;
        r7 = 0;
        r8 = 0;
        r10 = 0;
        r9 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x15d3;
    L_0x152c:
        r12 = 0;
        r2 = r1.photoImage;
        r2.setAllowDecodeSingleFrame(r15);
        org.telegram.messenger.FileLoader.getAttachFileName(r13);
        r2 = org.telegram.messenger.MessageObject.isRoundVideoDocument(r13);
        if (r2 == 0) goto L_0x1551;
    L_0x153b:
        r2 = r1.photoImage;
        r3 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r4 = 2;
        r3 = r3 / r4;
        r2.setRoundRadius(r3);
        r2 = r1.currentAccount;
        r2 = org.telegram.messenger.DownloadController.getInstance(r2);
        r3 = r1.currentMessageObject;
        r2 = r2.canDownloadMedia(r3);
        goto L_0x1565;
    L_0x1551:
        r2 = org.telegram.messenger.MessageObject.isGifDocument(r13);
        if (r2 == 0) goto L_0x1564;
    L_0x1557:
        r2 = r1.currentAccount;
        r2 = org.telegram.messenger.DownloadController.getInstance(r2);
        r3 = r1.currentMessageObject;
        r2 = r2.canDownloadMedia(r3);
        goto L_0x1565;
    L_0x1564:
        r2 = 0;
    L_0x1565:
        r3 = r1.currentPhotoObject;
        r4 = r3 instanceof org.telegram.tgnet.TLRPC.TL_photoStrippedSize;
        if (r4 != 0) goto L_0x1579;
    L_0x156b:
        r3 = r3.type;
        r4 = "s";
        r3 = r4.equals(r3);
        if (r3 == 0) goto L_0x1576;
    L_0x1575:
        goto L_0x1579;
    L_0x1576:
        r3 = r1.currentPhotoFilter;
        goto L_0x157b;
    L_0x1579:
        r3 = r1.currentPhotoFilterThumb;
    L_0x157b:
        r35 = r3;
        r3 = r14.mediaExists;
        if (r3 != 0) goto L_0x15a2;
    L_0x1581:
        if (r2 == 0) goto L_0x1584;
    L_0x1583:
        goto L_0x15a2;
    L_0x1584:
        r2 = r1.photoImage;
        r32 = 0;
        r33 = 0;
        r3 = r1.currentPhotoObject;
        r4 = r1.documentAttach;
        r34 = org.telegram.messenger.ImageLocation.getForDocument(r3, r4);
        r36 = 0;
        r37 = 0;
        r3 = r1.currentMessageObject;
        r39 = 0;
        r31 = r2;
        r38 = r3;
        r31.setImage(r32, r33, r34, r35, r36, r37, r38, r39);
        goto L_0x15d3;
    L_0x15a2:
        r1.autoPlayingMedia = r15;
        r2 = r1.photoImage;
        r3 = org.telegram.messenger.ImageLocation.getForDocument(r13);
        r4 = r13.size;
        r5 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        if (r4 >= r5) goto L_0x15b3;
    L_0x15b1:
        r4 = 0;
        goto L_0x15b5;
    L_0x15b3:
        r4 = "g";
    L_0x15b5:
        r5 = r1.currentPhotoObject;
        r6 = r1.documentAttach;
        r5 = org.telegram.messenger.ImageLocation.getForDocument(r5, r6);
        r6 = r1.currentPhotoObjectThumb;
        r7 = r1.documentAttach;
        r7 = org.telegram.messenger.ImageLocation.getForDocument(r6, r7);
        r8 = r1.currentPhotoFilterThumb;
        r9 = 0;
        r10 = r13.size;
        r11 = 0;
        r13 = 0;
        r6 = r35;
        r12 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);
    L_0x15d3:
        r1.drawPhotoImage = r15;
        r2 = r55;
        if (r2 == 0) goto L_0x1620;
    L_0x15d9:
        r3 = "video";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x1620;
    L_0x15e1:
        if (r40 == 0) goto L_0x1620;
    L_0x15e3:
        r2 = r40 / 60;
        r3 = r2 * 60;
        r40 = r40 - r3;
        r3 = 2;
        r4 = new java.lang.Object[r3];
        r2 = java.lang.Integer.valueOf(r2);
        r13 = 0;
        r4[r13] = r2;
        r2 = java.lang.Integer.valueOf(r40);
        r4[r15] = r2;
        r2 = "%d:%02d";
        r6 = java.lang.String.format(r2, r4);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_durationPaint;
        r2 = r2.measureText(r6);
        r2 = (double) r2;
        r2 = java.lang.Math.ceil(r2);
        r2 = (int) r2;
        r1.durationWidth = r2;
        r2 = new android.text.StaticLayout;
        r7 = org.telegram.p004ui.ActionBar.Theme.chat_durationPaint;
        r8 = r1.durationWidth;
        r9 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r11 = 0;
        r12 = 0;
        r5 = r2;
        r5.<init>(r6, r7, r8, r9, r10, r11, r12);
        r1.videoInfoLayout = r2;
        goto L_0x1652;
    L_0x1620:
        r13 = 0;
        r2 = r1.hasGamePreview;
        if (r2 == 0) goto L_0x1652;
    L_0x1625:
        r2 = 2131558715; // 0x7f0d013b float:1.8742754E38 double:1.053129933E-314;
        r3 = "AttachGame";
        r2 = org.telegram.messenger.LocaleController.getString(r3, r2);
        r4 = r2.toUpperCase();
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_gamePaint;
        r2 = r2.measureText(r4);
        r2 = (double) r2;
        r2 = java.lang.Math.ceil(r2);
        r2 = (int) r2;
        r1.durationWidth = r2;
        r2 = new android.text.StaticLayout;
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_gamePaint;
        r6 = r1.durationWidth;
        r7 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r9 = 0;
        r10 = 0;
        r3 = r2;
        r3.<init>(r4, r5, r6, r7, r8, r9, r10);
        r1.videoInfoLayout = r2;
    L_0x1652:
        r3 = r29;
    L_0x1654:
        r2 = r1.hasInvoicePreview;
        if (r2 == 0) goto L_0x1734;
    L_0x1658:
        r2 = r14.messageOwner;
        r2 = r2.media;
        r4 = r2.flags;
        r4 = r4 & 4;
        if (r4 == 0) goto L_0x1670;
    L_0x1662:
        r2 = 2131560388; // 0x7f0d07c4 float:1.8746147E38 double:1.0531307597E-314;
        r4 = "PaymentReceipt";
        r2 = org.telegram.messenger.LocaleController.getString(r4, r2);
        r2 = r2.toUpperCase();
        goto L_0x168f;
    L_0x1670:
        r2 = r2.test;
        if (r2 == 0) goto L_0x1682;
    L_0x1674:
        r2 = 2131560406; // 0x7f0d07d6 float:1.8746183E38 double:1.0531307686E-314;
        r4 = "PaymentTestInvoice";
        r2 = org.telegram.messenger.LocaleController.getString(r4, r2);
        r2 = r2.toUpperCase();
        goto L_0x168f;
    L_0x1682:
        r2 = 2131560375; // 0x7f0d07b7 float:1.874612E38 double:1.0531307533E-314;
        r4 = "PaymentInvoice";
        r2 = org.telegram.messenger.LocaleController.getString(r4, r2);
        r2 = r2.toUpperCase();
    L_0x168f:
        r4 = org.telegram.messenger.LocaleController.getInstance();
        r5 = r14.messageOwner;
        r5 = r5.media;
        r6 = r5.total_amount;
        r5 = r5.currency;
        r4 = r4.formatCurrencyString(r6, r5);
        r6 = new android.text.SpannableStringBuilder;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5.append(r4);
        r7 = " ";
        r5.append(r7);
        r5.append(r2);
        r2 = r5.toString();
        r6.<init>(r2);
        r2 = new org.telegram.ui.Components.TypefaceSpan;
        r5 = "fonts/rmedium.ttf";
        r5 = org.telegram.messenger.AndroidUtilities.getTypeface(r5);
        r2.<init>(r5);
        r4 = r4.length();
        r5 = 33;
        r6.setSpan(r2, r13, r4, r5);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_shipmentPaint;
        r4 = r6.length();
        r2 = r2.measureText(r6, r13, r4);
        r4 = (double) r2;
        r4 = java.lang.Math.ceil(r4);
        r2 = (int) r4;
        r1.durationWidth = r2;
        r2 = new android.text.StaticLayout;
        r7 = org.telegram.p004ui.ActionBar.Theme.chat_shipmentPaint;
        r4 = r1.durationWidth;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r8 = r4 + r5;
        r9 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r11 = 0;
        r12 = 0;
        r5 = r2;
        r5.<init>(r6, r7, r8, r9, r10, r11, r12);
        r1.videoInfoLayout = r2;
        r2 = r1.drawPhotoImage;
        if (r2 != 0) goto L_0x1734;
    L_0x16fa:
        r2 = r1.totalHeight;
        r4 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r2 = r2 + r4;
        r1.totalHeight = r2;
        r2 = r1.timeWidth;
        r4 = r60.isOutOwner();
        if (r4 == 0) goto L_0x1710;
    L_0x170d:
        r12 = 20;
        goto L_0x1711;
    L_0x1710:
        r12 = 0;
    L_0x1711:
        r12 = r12 + 14;
        r4 = (float) r12;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r2 = r2 + r4;
        r4 = r1.durationWidth;
        r5 = r4 + r2;
        if (r5 <= r0) goto L_0x172f;
    L_0x171f:
        r3 = java.lang.Math.max(r4, r3);
        r2 = r1.totalHeight;
        r4 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r2 = r2 + r4;
        r1.totalHeight = r2;
        goto L_0x1734;
    L_0x172f:
        r4 = r4 + r2;
        r3 = java.lang.Math.max(r4, r3);
    L_0x1734:
        r2 = r1.hasGamePreview;
        if (r2 == 0) goto L_0x1753;
    L_0x1738:
        r2 = r14.textHeight;
        if (r2 == 0) goto L_0x1753;
    L_0x173c:
        r4 = r1.linkPreviewHeight;
        r5 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r2 = r2 + r5;
        r4 = r4 + r2;
        r1.linkPreviewHeight = r4;
        r2 = r1.totalHeight;
        r4 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r2 = r2 + r4;
        r1.totalHeight = r2;
    L_0x1753:
        r2 = r56;
        r1.calcBackgroundWidth(r0, r2, r3);
        goto L_0x175d;
    L_0x1759:
        r13 = 0;
        r15 = 1;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x175d:
        r59.createInstantViewButton();
        goto L_0x1ff4;
    L_0x1762:
        r13 = 0;
        r15 = 1;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = 16;
        r3 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
        if (r0 != r2) goto L_0x18be;
    L_0x176c:
        r1.drawName = r13;
        r1.drawForwardedName = r13;
        r1.drawPhotoImage = r13;
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x17a1;
    L_0x1778:
        r0 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x178d;
    L_0x1780:
        r2 = r60.needDrawAvatar();
        if (r2 == 0) goto L_0x178d;
    L_0x1786:
        r2 = r60.isOutOwner();
        if (r2 != 0) goto L_0x178d;
    L_0x178c:
        goto L_0x178f;
    L_0x178d:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x178f:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r2;
        r2 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = java.lang.Math.min(r0, r2);
        r1.backgroundWidth = r0;
        goto L_0x17c9;
    L_0x17a1:
        r0 = org.telegram.messenger.AndroidUtilities.displaySize;
        r0 = r0.x;
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x17b6;
    L_0x17a9:
        r2 = r60.needDrawAvatar();
        if (r2 == 0) goto L_0x17b6;
    L_0x17af:
        r2 = r60.isOutOwner();
        if (r2 != 0) goto L_0x17b6;
    L_0x17b5:
        goto L_0x17b8;
    L_0x17b6:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x17b8:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r2;
        r2 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = java.lang.Math.min(r0, r2);
        r1.backgroundWidth = r0;
    L_0x17c9:
        r0 = r1.backgroundWidth;
        r2 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        r1.availableTimeWidth = r0;
        r0 = r59.getMaxNameWidth();
        r2 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        if (r0 >= 0) goto L_0x17e5;
    L_0x17e1:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
    L_0x17e5:
        r2 = org.telegram.messenger.LocaleController.getInstance();
        r2 = r2.formatterDay;
        r3 = r14.messageOwner;
        r3 = r3.date;
        r3 = (long) r3;
        r5 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r3 = r3 * r5;
        r2 = r2.format(r3);
        r3 = r14.messageOwner;
        r3 = r3.action;
        r3 = (org.telegram.tgnet.TLRPC.TL_messageActionPhoneCall) r3;
        r4 = r3.reason;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonMissed;
        r5 = r60.isOutOwner();
        if (r5 == 0) goto L_0x181e;
    L_0x1808:
        if (r4 == 0) goto L_0x1814;
    L_0x180a:
        r4 = 2131558877; // 0x7f0d01dd float:1.8743082E38 double:1.053130013E-314;
        r5 = "CallMessageOutgoingMissed";
        r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
        goto L_0x1843;
    L_0x1814:
        r4 = 2131558876; // 0x7f0d01dc float:1.874308E38 double:1.0531300127E-314;
        r5 = "CallMessageOutgoing";
        r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
        goto L_0x1843;
    L_0x181e:
        if (r4 == 0) goto L_0x182a;
    L_0x1820:
        r4 = 2131558875; // 0x7f0d01db float:1.8743078E38 double:1.053130012E-314;
        r5 = "CallMessageIncomingMissed";
        r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
        goto L_0x1843;
    L_0x182a:
        r4 = r3.reason;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonBusy;
        if (r4 == 0) goto L_0x183a;
    L_0x1830:
        r4 = 2131558874; // 0x7f0d01da float:1.8743076E38 double:1.0531300117E-314;
        r5 = "CallMessageIncomingDeclined";
        r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
        goto L_0x1843;
    L_0x183a:
        r4 = 2131558873; // 0x7f0d01d9 float:1.8743074E38 double:1.053130011E-314;
        r5 = "CallMessageIncoming";
        r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
    L_0x1843:
        r5 = r3.duration;
        if (r5 <= 0) goto L_0x1861;
    L_0x1847:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5.append(r2);
        r2 = ", ";
        r5.append(r2);
        r2 = r3.duration;
        r2 = org.telegram.messenger.LocaleController.formatCallDuration(r2);
        r5.append(r2);
        r2 = r5.toString();
    L_0x1861:
        r3 = new android.text.StaticLayout;
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_audioTitlePaint;
        r12 = (float) r0;
        r6 = android.text.TextUtils.TruncateAt.END;
        r6 = android.text.TextUtils.ellipsize(r4, r5, r12, r6);
        r7 = org.telegram.p004ui.ActionBar.Theme.chat_audioTitlePaint;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r8 = r0 + r4;
        r9 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r11 = 0;
        r4 = 0;
        r5 = r3;
        r15 = r12;
        r12 = r4;
        r5.<init>(r6, r7, r8, r9, r10, r11, r12);
        r1.titleLayout = r3;
        r3 = new android.text.StaticLayout;
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_contactPhonePaint;
        r5 = android.text.TextUtils.TruncateAt.END;
        r32 = android.text.TextUtils.ellipsize(r2, r4, r15, r5);
        r33 = org.telegram.p004ui.ActionBar.Theme.chat_contactPhonePaint;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r34 = r0 + r2;
        r35 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r36 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r37 = 0;
        r38 = 0;
        r31 = r3;
        r31.<init>(r32, r33, r34, r35, r36, r37, r38);
        r1.docTitleLayout = r3;
        r59.setMessageObjectInternal(r60);
        r0 = 1115815936; // 0x42820000 float:65.0 double:5.51286321E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r2 = r1.namesOffset;
        r0 = r0 + r2;
        r1.totalHeight = r0;
        r0 = r1.drawPinnedTop;
        if (r0 == 0) goto L_0x1ff4;
    L_0x18b5:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r30);
        r2 = r2 - r0;
        r1.namesOffset = r2;
        goto L_0x1ff4;
    L_0x18be:
        r2 = 12;
        if (r0 != r2) goto L_0x1ad0;
    L_0x18c2:
        r1.drawName = r13;
        r2 = 1;
        r1.drawForwardedName = r2;
        r1.drawPhotoImage = r2;
        r0 = r1.photoImage;
        r2 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0.setRoundRadius(r2);
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x1903;
    L_0x18da:
        r0 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x18ef;
    L_0x18e2:
        r2 = r60.needDrawAvatar();
        if (r2 == 0) goto L_0x18ef;
    L_0x18e8:
        r2 = r60.isOutOwner();
        if (r2 != 0) goto L_0x18ef;
    L_0x18ee:
        goto L_0x18f1;
    L_0x18ef:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x18f1:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r2;
        r2 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = java.lang.Math.min(r0, r2);
        r1.backgroundWidth = r0;
        goto L_0x192b;
    L_0x1903:
        r0 = org.telegram.messenger.AndroidUtilities.displaySize;
        r0 = r0.x;
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x1918;
    L_0x190b:
        r2 = r60.needDrawAvatar();
        if (r2 == 0) goto L_0x1918;
    L_0x1911:
        r2 = r60.isOutOwner();
        if (r2 != 0) goto L_0x1918;
    L_0x1917:
        goto L_0x191a;
    L_0x1918:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x191a:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r2;
        r2 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = java.lang.Math.min(r0, r2);
        r1.backgroundWidth = r0;
    L_0x192b:
        r0 = r1.backgroundWidth;
        r2 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        r1.availableTimeWidth = r0;
        r0 = r14.messageOwner;
        r0 = r0.media;
        r0 = r0.user_id;
        r2 = r1.currentAccount;
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);
        r0 = java.lang.Integer.valueOf(r0);
        r0 = r2.getUser(r0);
        r2 = r59.getMaxNameWidth();
        r3 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r3;
        if (r2 >= 0) goto L_0x195b;
    L_0x1957:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
    L_0x195b:
        r9 = r2;
        if (r0 == 0) goto L_0x1963;
    L_0x195e:
        r2 = r1.contactAvatarDrawable;
        r2.setInfo(r0);
    L_0x1963:
        r2 = r1.photoImage;
        r3 = org.telegram.messenger.ImageLocation.getForUser(r0, r13);
        if (r0 == 0) goto L_0x196f;
    L_0x196b:
        r4 = r1.contactAvatarDrawable;
    L_0x196d:
        r5 = r4;
        goto L_0x1978;
    L_0x196f:
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_contactDrawable;
        r5 = r60.isOutOwner();
        r4 = r4[r5];
        goto L_0x196d;
    L_0x1978:
        r6 = 0;
        r8 = 0;
        r4 = "50_50";
        r7 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8);
        r2 = r14.vCardData;
        r2 = android.text.TextUtils.isEmpty(r2);
        if (r2 != 0) goto L_0x1994;
    L_0x1989:
        r0 = r14.vCardData;
        r2 = 1;
        r1.drawInstantView = r2;
        r2 = 5;
        r1.drawInstantViewType = r2;
    L_0x1991:
        r32 = r0;
        goto L_0x19d9;
    L_0x1994:
        if (r0 == 0) goto L_0x19ba;
    L_0x1996:
        r2 = r0.phone;
        r2 = android.text.TextUtils.isEmpty(r2);
        if (r2 != 0) goto L_0x19ba;
    L_0x199e:
        r2 = org.telegram.PhoneFormat.C0278PhoneFormat.getInstance();
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "+";
        r3.append(r4);
        r0 = r0.phone;
        r3.append(r0);
        r0 = r3.toString();
        r0 = r2.format(r0);
        goto L_0x1991;
    L_0x19ba:
        r0 = r14.messageOwner;
        r0 = r0.media;
        r0 = r0.phone_number;
        r2 = android.text.TextUtils.isEmpty(r0);
        if (r2 != 0) goto L_0x19cf;
    L_0x19c6:
        r2 = org.telegram.PhoneFormat.C0278PhoneFormat.getInstance();
        r0 = r2.format(r0);
        goto L_0x1991;
    L_0x19cf:
        r0 = 2131560096; // 0x7f0d06a0 float:1.8745555E38 double:1.0531306155E-314;
        r2 = "NumberUnknown";
        r0 = org.telegram.messenger.LocaleController.getString(r2, r0);
        goto L_0x1991;
    L_0x19d9:
        r0 = r14.messageOwner;
        r0 = r0.media;
        r2 = r0.first_name;
        r0 = r0.last_name;
        r0 = org.telegram.messenger.ContactsController.formatName(r2, r0);
        r2 = 10;
        r3 = 32;
        r0 = r0.replace(r2, r3);
        r2 = r0.length();
        if (r2 != 0) goto L_0x19fd;
    L_0x19f3:
        r0 = r14.messageOwner;
        r0 = r0.media;
        r0 = r0.phone_number;
        if (r0 != 0) goto L_0x19fd;
    L_0x19fb:
        r0 = "";
    L_0x19fd:
        r2 = new android.text.StaticLayout;
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_contactNamePaint;
        r4 = (float) r9;
        r5 = android.text.TextUtils.TruncateAt.END;
        r34 = android.text.TextUtils.ellipsize(r0, r3, r4, r5);
        r35 = org.telegram.p004ui.ActionBar.Theme.chat_contactNamePaint;
        r0 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r36 = r9 + r0;
        r37 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r38 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r39 = 0;
        r40 = 0;
        r33 = r2;
        r33.<init>(r34, r35, r36, r37, r38, r39, r40);
        r1.titleLayout = r2;
        r0 = new android.text.StaticLayout;
        r33 = org.telegram.p004ui.ActionBar.Theme.chat_contactPhonePaint;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r34 = r9 + r2;
        r35 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r36 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r30);
        r2 = (float) r2;
        r38 = 0;
        r31 = r0;
        r37 = r2;
        r31.<init>(r32, r33, r34, r35, r36, r37, r38);
        r1.docTitleLayout = r0;
        r59.setMessageObjectInternal(r60);
        r0 = r1.drawForwardedName;
        if (r0 == 0) goto L_0x1a60;
    L_0x1a46:
        r0 = r60.needDrawForwarded();
        if (r0 == 0) goto L_0x1a60;
    L_0x1a4c:
        r0 = r1.currentPosition;
        if (r0 == 0) goto L_0x1a54;
    L_0x1a50:
        r0 = r0.minY;
        if (r0 != 0) goto L_0x1a60;
    L_0x1a54:
        r0 = r1.namesOffset;
        r2 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 + r2;
        r1.namesOffset = r0;
        goto L_0x1a75;
    L_0x1a60:
        r0 = r1.drawNameLayout;
        if (r0 == 0) goto L_0x1a75;
    L_0x1a64:
        r0 = r14.messageOwner;
        r0 = r0.reply_to_msg_id;
        if (r0 != 0) goto L_0x1a75;
    L_0x1a6a:
        r0 = r1.namesOffset;
        r2 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 + r2;
        r1.namesOffset = r0;
    L_0x1a75:
        r0 = 1113325568; // 0x425c0000 float:55.0 double:5.50055916E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r2 = r1.namesOffset;
        r0 = r0 + r2;
        r2 = r1.docTitleLayout;
        r2 = r2.getHeight();
        r0 = r0 + r2;
        r1.totalHeight = r0;
        r0 = r1.drawPinnedTop;
        if (r0 == 0) goto L_0x1a94;
    L_0x1a8b:
        r0 = r1.namesOffset;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r30);
        r0 = r0 - r2;
        r1.namesOffset = r0;
    L_0x1a94:
        r0 = r1.drawInstantView;
        if (r0 == 0) goto L_0x1a9d;
    L_0x1a98:
        r59.createInstantViewButton();
        goto L_0x1ff4;
    L_0x1a9d:
        r0 = r1.docTitleLayout;
        r0 = r0.getLineCount();
        if (r0 <= 0) goto L_0x1ff4;
    L_0x1aa5:
        r0 = r1.backgroundWidth;
        r2 = 1121714176; // 0x42dc0000 float:110.0 double:5.54200439E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        r2 = r1.docTitleLayout;
        r3 = r2.getLineCount();
        r4 = 1;
        r3 = r3 - r4;
        r2 = r2.getLineWidth(r3);
        r2 = (double) r2;
        r2 = java.lang.Math.ceil(r2);
        r2 = (int) r2;
        r0 = r0 - r2;
        r2 = r1.timeWidth;
        if (r0 >= r2) goto L_0x1ff4;
    L_0x1ac5:
        r0 = r1.totalHeight;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r0 = r0 + r2;
        r1.totalHeight = r0;
        goto L_0x1ff4;
    L_0x1ad0:
        r2 = 2;
        if (r0 != r2) goto L_0x1b4d;
    L_0x1ad3:
        r2 = 1;
        r1.drawForwardedName = r2;
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x1b05;
    L_0x1adc:
        r0 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x1af1;
    L_0x1ae4:
        r2 = r60.needDrawAvatar();
        if (r2 == 0) goto L_0x1af1;
    L_0x1aea:
        r2 = r60.isOutOwner();
        if (r2 != 0) goto L_0x1af1;
    L_0x1af0:
        goto L_0x1af3;
    L_0x1af1:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x1af3:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r2;
        r2 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = java.lang.Math.min(r0, r2);
        r1.backgroundWidth = r0;
        goto L_0x1b2d;
    L_0x1b05:
        r0 = org.telegram.messenger.AndroidUtilities.displaySize;
        r0 = r0.x;
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x1b1a;
    L_0x1b0d:
        r2 = r60.needDrawAvatar();
        if (r2 == 0) goto L_0x1b1a;
    L_0x1b13:
        r2 = r60.isOutOwner();
        if (r2 != 0) goto L_0x1b1a;
    L_0x1b19:
        goto L_0x1b1c;
    L_0x1b1a:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x1b1c:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r2;
        r2 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = java.lang.Math.min(r0, r2);
        r1.backgroundWidth = r0;
    L_0x1b2d:
        r0 = r1.backgroundWidth;
        r1.createDocumentLayout(r0, r14);
        r59.setMessageObjectInternal(r60);
        r0 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r2 = r1.namesOffset;
        r0 = r0 + r2;
        r1.totalHeight = r0;
        r0 = r1.drawPinnedTop;
        if (r0 == 0) goto L_0x1ff4;
    L_0x1b44:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r30);
        r2 = r2 - r0;
        r1.namesOffset = r2;
        goto L_0x1ff4;
    L_0x1b4d:
        r2 = 14;
        if (r0 != r2) goto L_0x1bc8;
    L_0x1b51:
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x1b80;
    L_0x1b57:
        r0 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x1b6c;
    L_0x1b5f:
        r2 = r60.needDrawAvatar();
        if (r2 == 0) goto L_0x1b6c;
    L_0x1b65:
        r2 = r60.isOutOwner();
        if (r2 != 0) goto L_0x1b6c;
    L_0x1b6b:
        goto L_0x1b6e;
    L_0x1b6c:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x1b6e:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r2;
        r2 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = java.lang.Math.min(r0, r2);
        r1.backgroundWidth = r0;
        goto L_0x1ba8;
    L_0x1b80:
        r0 = org.telegram.messenger.AndroidUtilities.displaySize;
        r0 = r0.x;
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x1b95;
    L_0x1b88:
        r2 = r60.needDrawAvatar();
        if (r2 == 0) goto L_0x1b95;
    L_0x1b8e:
        r2 = r60.isOutOwner();
        if (r2 != 0) goto L_0x1b95;
    L_0x1b94:
        goto L_0x1b97;
    L_0x1b95:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x1b97:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r2;
        r2 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = java.lang.Math.min(r0, r2);
        r1.backgroundWidth = r0;
    L_0x1ba8:
        r0 = r1.backgroundWidth;
        r1.createDocumentLayout(r0, r14);
        r59.setMessageObjectInternal(r60);
        r0 = 1118044160; // 0x42a40000 float:82.0 double:5.5238721E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r2 = r1.namesOffset;
        r0 = r0 + r2;
        r1.totalHeight = r0;
        r0 = r1.drawPinnedTop;
        if (r0 == 0) goto L_0x1ff4;
    L_0x1bbf:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r30);
        r2 = r2 - r0;
        r1.namesOffset = r2;
        goto L_0x1ff4;
    L_0x1bc8:
        r2 = 17;
        if (r0 != r2) goto L_0x1ffc;
    L_0x1bcc:
        r59.createSelectorDrawable();
        r2 = 1;
        r1.drawName = r2;
        r1.drawForwardedName = r2;
        r1.drawPhotoImage = r13;
        r0 = 1140457472; // 0x43fa0000 float:500.0 double:5.634608575E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r2 = r60.getMaxMessageTextWidth();
        r0 = java.lang.Math.min(r0, r2);
        r1.availableTimeWidth = r0;
        r2 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = r2 + r0;
        r1.backgroundWidth = r2;
        r2 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r1.availableTimeWidth = r2;
        r59.measureTime(r60);
        r2 = r14.messageOwner;
        r2 = r2.media;
        r2 = (org.telegram.tgnet.TLRPC.TL_messageMediaPoll) r2;
        r3 = r2.poll;
        r3 = r3.closed;
        r1.pollClosed = r3;
        r3 = r60.isVoted();
        r1.pollVoted = r3;
        r3 = new android.text.StaticLayout;
        r4 = r2.poll;
        r4 = r4.question;
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_audioTitlePaint;
        r5 = r5.getFontMetricsInt();
        r6 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = org.telegram.messenger.Emoji.replaceEmoji(r4, r5, r6, r13);
        r6 = org.telegram.p004ui.ActionBar.Theme.chat_audioTitlePaint;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r7 = r0 + r4;
        r8 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r9 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r10 = 0;
        r11 = 0;
        r4 = r3;
        r4.<init>(r5, r6, r7, r8, r9, r10, r11);
        r1.titleLayout = r3;
        r3 = r1.titleLayout;
        if (r3 == 0) goto L_0x1c51;
    L_0x1c3a:
        r3 = r3.getLineCount();
        r4 = 0;
    L_0x1c3f:
        if (r4 >= r3) goto L_0x1c51;
    L_0x1c41:
        r5 = r1.titleLayout;
        r5 = r5.getLineLeft(r4);
        r15 = 0;
        r5 = (r5 > r15 ? 1 : (r5 == r15 ? 0 : -1));
        if (r5 == 0) goto L_0x1c4e;
    L_0x1c4c:
        r3 = 1;
        goto L_0x1c53;
    L_0x1c4e:
        r4 = r4 + 1;
        goto L_0x1c3f;
    L_0x1c51:
        r15 = 0;
        r3 = 0;
    L_0x1c53:
        r12 = new android.text.StaticLayout;
        r4 = r2.poll;
        r4 = r4.closed;
        if (r4 == 0) goto L_0x1c61;
    L_0x1c5b:
        r4 = 2131559490; // 0x7f0d0442 float:1.8744326E38 double:1.053130316E-314;
        r5 = "FinalResults";
        goto L_0x1c66;
    L_0x1c61:
        r4 = 2131558633; // 0x7f0d00e9 float:1.8742587E38 double:1.0531298927E-314;
        r5 = "AnonymousPoll";
    L_0x1c66:
        r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_timePaint;
        r11 = (float) r0;
        r6 = android.text.TextUtils.TruncateAt.END;
        r5 = android.text.TextUtils.ellipsize(r4, r5, r11, r6);
        r6 = org.telegram.p004ui.ActionBar.Theme.chat_timePaint;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r7 = r0 + r4;
        r8 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r9 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r10 = 0;
        r21 = 0;
        r4 = r12;
        r22 = r11;
        r11 = r21;
        r4.<init>(r5, r6, r7, r8, r9, r10, r11);
        r1.docTitleLayout = r12;
        r4 = r1.docTitleLayout;
        if (r4 == 0) goto L_0x1cc2;
    L_0x1c90:
        r4 = r4.getLineCount();
        if (r4 <= 0) goto L_0x1cc2;
    L_0x1c96:
        if (r3 == 0) goto L_0x1cad;
    L_0x1c98:
        r4 = org.telegram.messenger.LocaleController.isRTL;
        if (r4 != 0) goto L_0x1cad;
    L_0x1c9c:
        r3 = r1.docTitleLayout;
        r3 = r3.getLineWidth(r13);
        r11 = r22 - r3;
        r3 = (double) r11;
        r3 = java.lang.Math.ceil(r3);
        r3 = (int) r3;
        r1.docTitleOffsetX = r3;
        goto L_0x1cc2;
    L_0x1cad:
        if (r3 != 0) goto L_0x1cc2;
    L_0x1caf:
        r3 = org.telegram.messenger.LocaleController.isRTL;
        if (r3 == 0) goto L_0x1cc2;
    L_0x1cb3:
        r3 = r1.docTitleLayout;
        r3 = r3.getLineLeft(r13);
        r3 = (double) r3;
        r3 = java.lang.Math.ceil(r3);
        r3 = (int) r3;
        r3 = -r3;
        r1.docTitleOffsetX = r3;
    L_0x1cc2:
        r3 = r1.timeWidth;
        r3 = r0 - r3;
        r4 = r60.isOutOwner();
        if (r4 == 0) goto L_0x1ccf;
    L_0x1ccc:
        r4 = 1105199104; // 0x41e00000 float:28.0 double:5.46040909E-315;
        goto L_0x1cd1;
    L_0x1ccf:
        r4 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
    L_0x1cd1:
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r8 = r3 - r4;
        r3 = new android.text.StaticLayout;
        r4 = r2.results;
        r4 = r4.total_voters;
        if (r4 != 0) goto L_0x1ce9;
    L_0x1cdf:
        r4 = 2131559956; // 0x7f0d0614 float:1.874527E38 double:1.0531305463E-314;
        r5 = "NoVotes";
        r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
        goto L_0x1cef;
    L_0x1ce9:
        r5 = "Vote";
        r4 = org.telegram.messenger.LocaleController.formatPluralString(r5, r4);
    L_0x1cef:
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_livePaint;
        r6 = (float) r8;
        r7 = android.text.TextUtils.TruncateAt.END;
        r6 = android.text.TextUtils.ellipsize(r4, r5, r6, r7);
        r7 = org.telegram.p004ui.ActionBar.Theme.chat_livePaint;
        r9 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r11 = 0;
        r12 = 0;
        r5 = r3;
        r5.<init>(r6, r7, r8, r9, r10, r11, r12);
        r1.infoLayout = r3;
        r3 = r1.infoLayout;
        if (r3 == 0) goto L_0x1d19;
    L_0x1d0a:
        r3 = r3.getLineCount();
        if (r3 <= 0) goto L_0x1d19;
    L_0x1d10:
        r3 = r1.infoLayout;
        r3 = r3.getLineLeft(r13);
        r3 = -r3;
        r3 = (double) r3;
        goto L_0x1d1b;
    L_0x1d19:
        r3 = 0;
    L_0x1d1b:
        r3 = java.lang.Math.ceil(r3);
        r3 = (int) r3;
        r1.infoX = r3;
        r3 = r2.poll;
        r1.lastPoll = r3;
        r3 = r2.results;
        r4 = r3.results;
        r1.lastPollResults = r4;
        r3 = r3.total_voters;
        r1.lastPollResultsVoters = r3;
        r3 = r1.animatePollAnswer;
        if (r3 != 0) goto L_0x1d3e;
    L_0x1d34:
        r3 = r1.pollVoteInProgress;
        if (r3 == 0) goto L_0x1d3e;
    L_0x1d38:
        r3 = 2;
        r12 = 3;
        r1.performHapticFeedback(r12, r3);
        goto L_0x1d3f;
    L_0x1d3e:
        r12 = 3;
    L_0x1d3f:
        r3 = r1.attachedToWindow;
        if (r3 == 0) goto L_0x1d4d;
    L_0x1d43:
        r3 = r1.pollVoteInProgress;
        if (r3 != 0) goto L_0x1d4b;
    L_0x1d47:
        r3 = r1.pollUnvoteInProgress;
        if (r3 == 0) goto L_0x1d4d;
    L_0x1d4b:
        r3 = 1;
        goto L_0x1d4e;
    L_0x1d4d:
        r3 = 0;
    L_0x1d4e:
        r1.animatePollAnswer = r3;
        r1.animatePollAnswerAlpha = r3;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r4 = r1.pollButtons;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x1dd1;
    L_0x1d5f:
        r4 = new java.util.ArrayList;
        r5 = r1.pollButtons;
        r4.<init>(r5);
        r5 = r1.pollButtons;
        r5.clear();
        r5 = r1.animatePollAnswer;
        if (r5 != 0) goto L_0x1d80;
    L_0x1d6f:
        r5 = r1.attachedToWindow;
        if (r5 == 0) goto L_0x1d7d;
    L_0x1d73:
        r5 = r1.pollVoted;
        if (r5 != 0) goto L_0x1d7b;
    L_0x1d77:
        r5 = r1.pollClosed;
        if (r5 == 0) goto L_0x1d7d;
    L_0x1d7b:
        r5 = 1;
        goto L_0x1d7e;
    L_0x1d7d:
        r5 = 0;
    L_0x1d7e:
        r1.animatePollAnswer = r5;
    L_0x1d80:
        r5 = r1.pollAnimationProgress;
        r6 = (r5 > r15 ? 1 : (r5 == r15 ? 0 : -1));
        if (r6 <= 0) goto L_0x1dce;
    L_0x1d86:
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r5 = (r5 > r11 ? 1 : (r5 == r11 ? 0 : -1));
        if (r5 >= 0) goto L_0x1dd4;
    L_0x1d8c:
        r5 = r4.size();
        r6 = 0;
    L_0x1d91:
        if (r6 >= r5) goto L_0x1dd4;
    L_0x1d93:
        r7 = r4.get(r6);
        r7 = (org.telegram.p004ui.Cells.ChatMessageCell.PollButton) r7;
        r8 = r7.prevPercent;
        r8 = (float) r8;
        r9 = r7.percent;
        r10 = r7.prevPercent;
        r9 = r9 - r10;
        r9 = (float) r9;
        r10 = r1.pollAnimationProgress;
        r9 = r9 * r10;
        r8 = r8 + r9;
        r8 = (double) r8;
        r8 = java.lang.Math.ceil(r8);
        r8 = (int) r8;
        r7.percent = r8;
        r8 = r7.prevPercentProgress;
        r9 = r7.percentProgress;
        r10 = r7.prevPercentProgress;
        r9 = r9 - r10;
        r10 = r1.pollAnimationProgress;
        r9 = r9 * r10;
        r8 = r8 + r9;
        r7.percentProgress = r8;
        r6 = r6 + 1;
        goto L_0x1d91;
    L_0x1dce:
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x1dd4;
    L_0x1dd1:
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = 0;
    L_0x1dd4:
        r5 = r1.animatePollAnswer;
        if (r5 == 0) goto L_0x1dda;
    L_0x1dd8:
        r5 = 0;
        goto L_0x1ddc;
    L_0x1dda:
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x1ddc:
        r1.pollAnimationProgress = r5;
        r5 = r1.animatePollAnswerAlpha;
        if (r5 != 0) goto L_0x1df4;
    L_0x1de2:
        r1.pollVoteInProgress = r13;
        r10 = -1;
        r1.pollVoteInProgressNum = r10;
        r5 = r1.currentAccount;
        r5 = org.telegram.messenger.SendMessagesHelper.getInstance(r5);
        r6 = r1.currentMessageObject;
        r5 = r5.isSendingVote(r6);
        goto L_0x1df6;
    L_0x1df4:
        r10 = -1;
        r5 = 0;
    L_0x1df6:
        r6 = r1.titleLayout;
        if (r6 == 0) goto L_0x1dff;
    L_0x1dfa:
        r6 = r6.getHeight();
        goto L_0x1e00;
    L_0x1dff:
        r6 = 0;
    L_0x1e00:
        r7 = 100;
        r8 = r2.poll;
        r8 = r8.answers;
        r8 = r8.size();
        r11 = r5;
        r7 = r6;
        r5 = 0;
        r6 = 0;
        r9 = 100;
        r10 = 0;
        r12 = 0;
    L_0x1e12:
        if (r5 >= r8) goto L_0x1f77;
    L_0x1e14:
        r15 = new org.telegram.ui.Cells.ChatMessageCell$PollButton;
        r13 = 0;
        r15.<init>(r1, r13);
        r13 = r2.poll;
        r13 = r13.answers;
        r13 = r13.get(r5);
        r13 = (org.telegram.tgnet.TLRPC.TL_pollAnswer) r13;
        r15.answer = r13;
        r13 = new android.text.StaticLayout;
        r61 = r8;
        r8 = r15.answer;
        r8 = r8.text;
        r21 = org.telegram.p004ui.ActionBar.Theme.chat_audioPerformerPaint;
        r14 = r21.getFontMetricsInt();
        r21 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r62 = r6;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r21 = r5;
        r5 = 0;
        r32 = org.telegram.messenger.Emoji.replaceEmoji(r8, r14, r6, r5);
        r33 = org.telegram.p004ui.ActionBar.Theme.chat_audioPerformerPaint;
        r5 = 1107558400; // 0x42040000 float:33.0 double:5.47206556E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r34 = r0 - r5;
        r35 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r36 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r37 = 0;
        r38 = 0;
        r31 = r13;
        r31.<init>(r32, r33, r34, r35, r36, r37, r38);
        r15.title = r13;
        r5 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r5 = r5 + r7;
        r15.f578y = r5;
        r5 = r15.title;
        r5 = r5.getHeight();
        r15.height = r5;
        r5 = r1.pollButtons;
        r5.add(r15);
        r3.add(r15);
        r5 = r15.height;
        r6 = 1104150528; // 0x41d00000 float:26.0 double:5.455228437E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 + r6;
        r7 = r7 + r5;
        r5 = r2.results;
        r5 = r5.results;
        r5 = r5.isEmpty();
        if (r5 != 0) goto L_0x1f17;
    L_0x1e93:
        r5 = r2.results;
        r5 = r5.results;
        r5 = r5.size();
        r6 = 0;
    L_0x1e9c:
        if (r6 >= r5) goto L_0x1f17;
    L_0x1e9e:
        r8 = r2.results;
        r8 = r8.results;
        r8 = r8.get(r6);
        r8 = (org.telegram.tgnet.TLRPC.TL_pollAnswerVoters) r8;
        r13 = r15.answer;
        r13 = r13.option;
        r14 = r8.option;
        r13 = java.util.Arrays.equals(r13, r14);
        if (r13 == 0) goto L_0x1f14;
    L_0x1eb6:
        r5 = r1.pollVoted;
        if (r5 != 0) goto L_0x1ebe;
    L_0x1eba:
        r5 = r1.pollClosed;
        if (r5 == 0) goto L_0x1ee6;
    L_0x1ebe:
        r5 = r2.results;
        r5 = r5.total_voters;
        if (r5 <= 0) goto L_0x1ee6;
    L_0x1ec4:
        r6 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r8 = r8.voters;
        r8 = (float) r8;
        r5 = (float) r5;
        r8 = r8 / r5;
        r8 = r8 * r6;
        r15.decimal = r8;
        r5 = r15.decimal;
        r5 = (int) r5;
        r15.percent = r5;
        r5 = r15.decimal;
        r6 = r15.percent;
        r6 = (float) r6;
        r5 = r5 - r6;
        r15.decimal = r5;
        goto L_0x1eee;
    L_0x1ee6:
        r5 = 0;
        r15.percent = r5;
        r5 = 0;
        r15.decimal = r5;
    L_0x1eee:
        if (r10 != 0) goto L_0x1ef5;
    L_0x1ef0:
        r10 = r15.percent;
        goto L_0x1f03;
    L_0x1ef5:
        r5 = r15.percent;
        if (r5 == 0) goto L_0x1f03;
    L_0x1efb:
        r5 = r15.percent;
        if (r10 == r5) goto L_0x1f03;
    L_0x1f01:
        r6 = 1;
        goto L_0x1f05;
    L_0x1f03:
        r6 = r62;
    L_0x1f05:
        r5 = r15.percent;
        r9 = r9 - r5;
        r5 = r15.percent;
        r5 = java.lang.Math.max(r5, r12);
        r12 = r5;
        goto L_0x1f19;
    L_0x1f14:
        r6 = r6 + 1;
        goto L_0x1e9c;
    L_0x1f17:
        r6 = r62;
    L_0x1f19:
        if (r4 == 0) goto L_0x1f50;
    L_0x1f1b:
        r5 = r4.size();
        r8 = 0;
    L_0x1f20:
        if (r8 >= r5) goto L_0x1f50;
    L_0x1f22:
        r13 = r4.get(r8);
        r13 = (org.telegram.p004ui.Cells.ChatMessageCell.PollButton) r13;
        r14 = r15.answer;
        r14 = r14.option;
        r63 = r0;
        r0 = r13.answer;
        r0 = r0.option;
        r0 = java.util.Arrays.equals(r14, r0);
        if (r0 == 0) goto L_0x1f4b;
    L_0x1f3c:
        r0 = r13.percent;
        r15.prevPercent = r0;
        r0 = r13.percentProgress;
        r15.prevPercentProgress = r0;
        goto L_0x1f52;
    L_0x1f4b:
        r8 = r8 + 1;
        r0 = r63;
        goto L_0x1f20;
    L_0x1f50:
        r63 = r0;
    L_0x1f52:
        if (r11 == 0) goto L_0x1f69;
    L_0x1f54:
        r0 = r15.answer;
        r0 = r0.option;
        r0 = java.util.Arrays.equals(r0, r11);
        if (r0 == 0) goto L_0x1f69;
    L_0x1f60:
        r13 = r21;
        r1.pollVoteInProgressNum = r13;
        r5 = 1;
        r1.pollVoteInProgress = r5;
        r11 = 0;
        goto L_0x1f6b;
    L_0x1f69:
        r13 = r21;
    L_0x1f6b:
        r5 = r13 + 1;
        r14 = r60;
        r8 = r61;
        r0 = r63;
        r13 = 0;
        r15 = 0;
        goto L_0x1e12;
    L_0x1f77:
        r62 = r6;
        if (r62 == 0) goto L_0x1f9f;
    L_0x1f7b:
        if (r9 == 0) goto L_0x1f9f;
    L_0x1f7d:
        r0 = org.telegram.p004ui.Cells.C2331-$$Lambda$ChatMessageCell$hzMG4njhE1StYhHOT542pSi6Cf0.INSTANCE;
        java.util.Collections.sort(r3, r0);
        r0 = r3.size();
        r0 = java.lang.Math.min(r9, r0);
        r2 = 0;
    L_0x1f8b:
        if (r2 >= r0) goto L_0x1f9f;
    L_0x1f8d:
        r4 = r3.get(r2);
        r4 = (org.telegram.p004ui.Cells.ChatMessageCell.PollButton) r4;
        r5 = r4.percent;
        r6 = 1;
        r5 = r5 + r6;
        r4.percent = r5;
        r2 = r2 + 1;
        goto L_0x1f8b;
    L_0x1f9f:
        r0 = r1.backgroundWidth;
        r2 = 1117257728; // 0x42980000 float:76.0 double:5.51998661E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        r2 = r1.pollButtons;
        r2 = r2.size();
        r3 = 0;
    L_0x1faf:
        if (r3 >= r2) goto L_0x1fd8;
    L_0x1fb1:
        r4 = r1.pollButtons;
        r4 = r4.get(r3);
        r4 = (org.telegram.p004ui.Cells.ChatMessageCell.PollButton) r4;
        r5 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r5 = (float) r5;
        r6 = (float) r0;
        r5 = r5 / r6;
        if (r12 == 0) goto L_0x1fcd;
    L_0x1fc4:
        r6 = r4.percent;
        r6 = (float) r6;
        r8 = (float) r12;
        r10 = r6 / r8;
        goto L_0x1fce;
    L_0x1fcd:
        r10 = 0;
    L_0x1fce:
        r5 = java.lang.Math.max(r5, r10);
        r4.percentProgress = r5;
        r3 = r3 + 1;
        goto L_0x1faf;
    L_0x1fd8:
        r59.setMessageObjectInternal(r60);
        r0 = 1116864512; // 0x42920000 float:73.0 double:5.518043864E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r2 = r1.namesOffset;
        r0 = r0 + r2;
        r0 = r0 + r7;
        r1.totalHeight = r0;
        r0 = r1.drawPinnedTop;
        if (r0 == 0) goto L_0x1ff4;
    L_0x1feb:
        r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r0;
        r1.namesOffset = r2;
    L_0x1ff4:
        r14 = r60;
        r9 = 0;
        r15 = 0;
        r29 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x3527;
    L_0x1ffc:
        r0 = r14.messageOwner;
        r0 = r0.fwd_from;
        if (r0 == 0) goto L_0x200a;
    L_0x2002:
        r0 = r60.isAnyKindOfSticker();
        if (r0 != 0) goto L_0x200a;
    L_0x2008:
        r0 = 1;
        goto L_0x200b;
    L_0x200a:
        r0 = 0;
    L_0x200b:
        r1.drawForwardedName = r0;
        r0 = r14.type;
        r2 = 9;
        if (r0 == r2) goto L_0x2015;
    L_0x2013:
        r0 = 1;
        goto L_0x2016;
    L_0x2015:
        r0 = 0;
    L_0x2016:
        r1.mediaBackground = r0;
        r2 = 1;
        r1.drawImageButton = r2;
        r1.drawPhotoImage = r2;
        r0 = r14.gifState;
        r0 = (r0 > r20 ? 1 : (r0 == r20 ? 0 : -1));
        if (r0 == 0) goto L_0x2035;
    L_0x2023:
        r0 = org.telegram.messenger.SharedConfig.autoplayGifs;
        if (r0 != 0) goto L_0x2035;
    L_0x2027:
        r0 = r14.type;
        r6 = 8;
        if (r0 == r6) goto L_0x2030;
    L_0x202d:
        r2 = 5;
        if (r0 != r2) goto L_0x2037;
    L_0x2030:
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r14.gifState = r11;
        goto L_0x2039;
    L_0x2035:
        r6 = 8;
    L_0x2037:
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x2039:
        r0 = r1.photoImage;
        r2 = 1;
        r0.setAllowDecodeSingleFrame(r2);
        r0 = r60.isVideo();
        if (r0 == 0) goto L_0x204b;
    L_0x2045:
        r0 = r1.photoImage;
        r0.setAllowStartAnimation(r2);
        goto L_0x207a;
    L_0x204b:
        r0 = r60.isRoundVideo();
        if (r0 == 0) goto L_0x206b;
    L_0x2051:
        r0 = org.telegram.messenger.MediaController.getInstance();
        r0 = r0.getPlayingMessageObject();
        r2 = r1.photoImage;
        if (r0 == 0) goto L_0x2066;
    L_0x205d:
        r0 = r0.isRoundVideo();
        if (r0 != 0) goto L_0x2064;
    L_0x2063:
        goto L_0x2066;
    L_0x2064:
        r0 = 0;
        goto L_0x2067;
    L_0x2066:
        r0 = 1;
    L_0x2067:
        r2.setAllowStartAnimation(r0);
        goto L_0x207a;
    L_0x206b:
        r0 = r1.photoImage;
        r2 = r14.gifState;
        r4 = 0;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 != 0) goto L_0x2076;
    L_0x2074:
        r2 = 1;
        goto L_0x2077;
    L_0x2076:
        r2 = 0;
    L_0x2077:
        r0.setAllowStartAnimation(r2);
    L_0x207a:
        r0 = r1.photoImage;
        r2 = r60.needDrawBluredPreview();
        r0.setForcePreview(r2);
        r0 = r14.type;
        r2 = 9;
        if (r0 != r2) goto L_0x228d;
    L_0x2089:
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x20b8;
    L_0x208f:
        r0 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x20a4;
    L_0x2097:
        r2 = r60.needDrawAvatar();
        if (r2 == 0) goto L_0x20a4;
    L_0x209d:
        r2 = r60.isOutOwner();
        if (r2 != 0) goto L_0x20a4;
    L_0x20a3:
        goto L_0x20a6;
    L_0x20a4:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x20a6:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r2;
        r2 = 1133903872; // 0x43960000 float:300.0 double:5.60222949E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = java.lang.Math.min(r0, r2);
        r1.backgroundWidth = r0;
        goto L_0x20e0;
    L_0x20b8:
        r0 = org.telegram.messenger.AndroidUtilities.displaySize;
        r0 = r0.x;
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x20cd;
    L_0x20c0:
        r2 = r60.needDrawAvatar();
        if (r2 == 0) goto L_0x20cd;
    L_0x20c6:
        r2 = r60.isOutOwner();
        if (r2 != 0) goto L_0x20cd;
    L_0x20cc:
        goto L_0x20cf;
    L_0x20cd:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x20cf:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r2;
        r2 = 1133903872; // 0x43960000 float:300.0 double:5.60222949E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = java.lang.Math.min(r0, r2);
        r1.backgroundWidth = r0;
    L_0x20e0:
        r0 = r59.checkNeedDrawShareButton(r60);
        if (r0 == 0) goto L_0x20f1;
    L_0x20e6:
        r0 = r1.backgroundWidth;
        r2 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        r1.backgroundWidth = r0;
    L_0x20f1:
        r0 = r1.backgroundWidth;
        r2 = 1124728832; // 0x430a0000 float:138.0 double:5.55689877E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = r0 - r2;
        r1.createDocumentLayout(r2, r14);
        r0 = r14.caption;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x2168;
    L_0x2106:
        r0 = r14.caption;	 Catch:{ Exception -> 0x2162 }
        r1.currentCaption = r0;	 Catch:{ Exception -> 0x2162 }
        r0 = r1.backgroundWidth;	 Catch:{ Exception -> 0x2162 }
        r3 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);	 Catch:{ Exception -> 0x2162 }
        r0 = r0 - r3;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r19);	 Catch:{ Exception -> 0x2162 }
        r3 = r0 - r3;
        r0 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x215f }
        r4 = 24;
        if (r0 < r4) goto L_0x2144;
    L_0x211f:
        r0 = r14.caption;	 Catch:{ Exception -> 0x215f }
        r4 = r14.caption;	 Catch:{ Exception -> 0x215f }
        r4 = r4.length();	 Catch:{ Exception -> 0x215f }
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x215f }
        r7 = 0;
        r0 = android.text.StaticLayout.Builder.obtain(r0, r7, r4, r5, r3);	 Catch:{ Exception -> 0x215f }
        r4 = 1;
        r0 = r0.setBreakStrategy(r4);	 Catch:{ Exception -> 0x215f }
        r0 = r0.setHyphenationFrequency(r7);	 Catch:{ Exception -> 0x215f }
        r4 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x215f }
        r0 = r0.setAlignment(r4);	 Catch:{ Exception -> 0x215f }
        r0 = r0.build();	 Catch:{ Exception -> 0x215f }
        r1.captionLayout = r0;	 Catch:{ Exception -> 0x215f }
        goto L_0x215d;
    L_0x2144:
        r0 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x215f }
        r4 = r14.caption;	 Catch:{ Exception -> 0x215f }
        r29 = org.telegram.p004ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x215f }
        r31 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x215f }
        r32 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r33 = 0;
        r34 = 0;
        r27 = r0;
        r28 = r4;
        r30 = r3;
        r27.<init>(r28, r29, r30, r31, r32, r33, r34);	 Catch:{ Exception -> 0x215f }
        r1.captionLayout = r0;	 Catch:{ Exception -> 0x215f }
    L_0x215d:
        r12 = r3;
        goto L_0x2169;
    L_0x215f:
        r0 = move-exception;
        r12 = r3;
        goto L_0x2164;
    L_0x2162:
        r0 = move-exception;
        r12 = 0;
    L_0x2164:
        org.telegram.messenger.FileLog.m30e(r0);
        goto L_0x2169;
    L_0x2168:
        r12 = 0;
    L_0x2169:
        r0 = r1.docTitleLayout;
        if (r0 == 0) goto L_0x21a0;
    L_0x216d:
        r0 = r0.getLineCount();
        r3 = 0;
        r4 = 0;
    L_0x2173:
        if (r3 >= r0) goto L_0x21a1;
    L_0x2175:
        r5 = r1.docTitleLayout;
        r5 = r5.getLineWidth(r3);
        r7 = r1.docTitleLayout;
        r7 = r7.getLineLeft(r3);
        r5 = r5 + r7;
        r7 = (double) r5;
        r7 = java.lang.Math.ceil(r7);
        r5 = (int) r7;
        r7 = r1.drawPhotoImage;
        if (r7 == 0) goto L_0x218f;
    L_0x218c:
        r7 = 52;
        goto L_0x2191;
    L_0x218f:
        r7 = 22;
    L_0x2191:
        r7 = r7 + 86;
        r7 = (float) r7;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r5 = r5 + r7;
        r4 = java.lang.Math.max(r4, r5);
        r3 = r3 + 1;
        goto L_0x2173;
    L_0x21a0:
        r4 = 0;
    L_0x21a1:
        r0 = r1.infoLayout;
        if (r0 == 0) goto L_0x21d0;
    L_0x21a5:
        r0 = r0.getLineCount();
        r3 = 0;
    L_0x21aa:
        if (r3 >= r0) goto L_0x21d0;
    L_0x21ac:
        r5 = r1.infoLayout;
        r5 = r5.getLineWidth(r3);
        r7 = (double) r5;
        r7 = java.lang.Math.ceil(r7);
        r5 = (int) r7;
        r7 = r1.drawPhotoImage;
        if (r7 == 0) goto L_0x21bf;
    L_0x21bc:
        r7 = 52;
        goto L_0x21c1;
    L_0x21bf:
        r7 = 22;
    L_0x21c1:
        r7 = r7 + 86;
        r7 = (float) r7;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r5 = r5 + r7;
        r4 = java.lang.Math.max(r4, r5);
        r3 = r3 + 1;
        goto L_0x21aa;
    L_0x21d0:
        r0 = r1.captionLayout;
        if (r0 == 0) goto L_0x2200;
    L_0x21d4:
        r0 = r0.getLineCount();
        r3 = 0;
    L_0x21d9:
        if (r3 >= r0) goto L_0x2200;
    L_0x21db:
        r5 = (float) r12;
        r7 = r1.captionLayout;
        r7 = r7.getLineWidth(r3);
        r8 = r1.captionLayout;
        r8 = r8.getLineLeft(r3);
        r7 = r7 + r8;
        r5 = java.lang.Math.min(r5, r7);
        r7 = (double) r5;
        r7 = java.lang.Math.ceil(r7);
        r5 = (int) r7;
        r7 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r5 = r5 + r7;
        if (r5 <= r4) goto L_0x21fd;
    L_0x21fc:
        r4 = r5;
    L_0x21fd:
        r3 = r3 + 1;
        goto L_0x21d9;
    L_0x2200:
        if (r4 <= 0) goto L_0x220c;
    L_0x2202:
        r1.backgroundWidth = r4;
        r0 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r2 = r4 - r0;
    L_0x220c:
        r0 = r1.drawPhotoImage;
        if (r0 == 0) goto L_0x221d;
    L_0x2210:
        r0 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r3 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        goto L_0x2244;
    L_0x221d:
        r0 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r3 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r4 = r1.docTitleLayout;
        if (r4 == 0) goto L_0x2244;
    L_0x222d:
        r4 = r4.getLineCount();
        r5 = 1;
        if (r4 <= r5) goto L_0x2244;
    L_0x2234:
        r4 = r1.docTitleLayout;
        r4 = r4.getLineCount();
        r4 = r4 - r5;
        r5 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r4 = r4 * r5;
        r3 = r3 + r4;
    L_0x2244:
        r1.availableTimeWidth = r2;
        r2 = r1.drawPhotoImage;
        if (r2 != 0) goto L_0x228a;
    L_0x224a:
        r2 = r14.caption;
        r2 = android.text.TextUtils.isEmpty(r2);
        if (r2 == 0) goto L_0x228a;
    L_0x2252:
        r2 = r1.infoLayout;
        if (r2 == 0) goto L_0x228a;
    L_0x2256:
        r2 = r2.getLineCount();
        r59.measureTime(r60);
        r4 = r1.backgroundWidth;
        r5 = 1123287040; // 0x42f40000 float:122.0 double:5.54977537E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r4 = r4 - r5;
        r5 = r1.infoLayout;
        r7 = 0;
        r5 = r5.getLineWidth(r7);
        r7 = (double) r5;
        r7 = java.lang.Math.ceil(r7);
        r5 = (int) r7;
        r4 = r4 - r5;
        r5 = r1.timeWidth;
        if (r4 >= r5) goto L_0x2280;
    L_0x2278:
        r2 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
    L_0x227e:
        r3 = r3 + r2;
        goto L_0x228a;
    L_0x2280:
        r4 = 1;
        if (r2 != r4) goto L_0x228a;
    L_0x2283:
        r2 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        goto L_0x227e;
    L_0x228a:
        r12 = r3;
        goto L_0x28f9;
    L_0x228d:
        r2 = 4;
        if (r0 != r2) goto L_0x27ad;
    L_0x2290:
        r0 = r14.messageOwner;
        r0 = r0.media;
        r2 = r0.geo;
        r4 = r2.lat;
        r7 = r2._long;
        r9 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r9 == 0) goto L_0x24db;
    L_0x229e:
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x22ce;
    L_0x22a4:
        r0 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r9 = r1.isChat;
        if (r9 == 0) goto L_0x22b9;
    L_0x22ac:
        r9 = r60.needDrawAvatar();
        if (r9 == 0) goto L_0x22b9;
    L_0x22b2:
        r9 = r60.isOutOwner();
        if (r9 != 0) goto L_0x22b9;
    L_0x22b8:
        goto L_0x22bb;
    L_0x22b9:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x22bb:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r3 = 1133543424; // 0x43908000 float:289.0 double:5.60044864E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = java.lang.Math.min(r0, r3);
        r1.backgroundWidth = r0;
        goto L_0x22f7;
    L_0x22ce:
        r0 = org.telegram.messenger.AndroidUtilities.displaySize;
        r0 = r0.x;
        r9 = r1.isChat;
        if (r9 == 0) goto L_0x22e3;
    L_0x22d6:
        r9 = r60.needDrawAvatar();
        if (r9 == 0) goto L_0x22e3;
    L_0x22dc:
        r9 = r60.isOutOwner();
        if (r9 != 0) goto L_0x22e3;
    L_0x22e2:
        goto L_0x22e5;
    L_0x22e3:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x22e5:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r3 = 1133543424; // 0x43908000 float:289.0 double:5.60044864E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = java.lang.Math.min(r0, r3);
        r1.backgroundWidth = r0;
    L_0x22f7:
        r0 = r1.backgroundWidth;
        r3 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r1.backgroundWidth = r0;
        r0 = r59.checkNeedDrawShareButton(r60);
        if (r0 == 0) goto L_0x2313;
    L_0x2308:
        r0 = r1.backgroundWidth;
        r3 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r1.backgroundWidth = r0;
    L_0x2313:
        r0 = r1.backgroundWidth;
        r3 = 1108606976; // 0x42140000 float:37.0 double:5.477246216E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r1.availableTimeWidth = r0;
        r3 = 1113063424; // 0x42580000 float:54.0 double:5.499263994E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r3 = r1.backgroundWidth;
        r9 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r3 = r3 - r9;
        r9 = 1128464384; // 0x43430000 float:195.0 double:5.575354847E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r10 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r12 = (double) r10;
        r21 = 4614256656552045848; // 0x400921fb54442d18 float:3.37028055E12 double:3.141592653589793;
        java.lang.Double.isNaN(r12);
        r21 = r12 / r21;
        r28 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r30 = 4614256656552045848; // 0x400921fb54442d18 float:3.37028055E12 double:3.141592653589793;
        r4 = r4 * r30;
        r30 = 4640537203540230144; // 0x4066800000000000 float:0.0 double:180.0;
        r4 = r4 / r30;
        r30 = java.lang.Math.sin(r4);
        r30 = r30 + r28;
        r4 = java.lang.Math.sin(r4);
        r28 = r28 - r4;
        r30 = r30 / r28;
        r4 = java.lang.Math.log(r30);
        r4 = r4 * r21;
        r28 = 4611686018427387904; // 0x4000000000000000 float:0.0 double:2.0;
        r4 = r4 / r28;
        java.lang.Double.isNaN(r12);
        r4 = r12 - r4;
        r4 = java.lang.Math.round(r4);
        r10 = 1092930765; // 0x4124cccd float:10.3 double:5.399795443E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r15 = 6;
        r10 = r10 << r15;
        r37 = r7;
        r6 = (long) r10;
        r4 = r4 - r6;
        r4 = (double) r4;
        r6 = 4609753056924675352; // 0x3ff921fb54442d18 float:3.37028055E12 double:1.5707963267948966;
        r27 = 4611686018427387904; // 0x4000000000000000 float:0.0 double:2.0;
        java.lang.Double.isNaN(r4);
        java.lang.Double.isNaN(r12);
        r4 = r4 - r12;
        r4 = r4 / r21;
        r4 = java.lang.Math.exp(r4);
        r4 = java.lang.Math.atan(r4);
        r4 = r4 * r27;
        r6 = r6 - r4;
        r4 = 4640537203540230144; // 0x4066800000000000 float:0.0 double:180.0;
        r6 = r6 * r4;
        r4 = 4614256656552045848; // 0x400921fb54442d18 float:3.37028055E12 double:3.141592653589793;
        r4 = r6 / r4;
        r6 = r1.currentAccount;
        r7 = (float) r3;
        r8 = org.telegram.messenger.AndroidUtilities.density;
        r10 = r7 / r8;
        r10 = (int) r10;
        r12 = (float) r9;
        r8 = r12 / r8;
        r8 = (int) r8;
        r35 = 0;
        r36 = 15;
        r28 = r6;
        r29 = r4;
        r31 = r37;
        r33 = r10;
        r34 = r8;
        r6 = org.telegram.messenger.AndroidUtilities.formapMapUrl(r28, r29, r31, r33, r34, r35, r36);
        r1.currentUrl = r6;
        r6 = r12;
        r11 = r2.access_hash;
        r2 = org.telegram.messenger.AndroidUtilities.density;
        r7 = r7 / r2;
        r7 = (int) r7;
        r6 = r6 / r2;
        r6 = (int) r6;
        r63 = r3;
        r2 = (double) r2;
        r2 = java.lang.Math.ceil(r2);
        r2 = (int) r2;
        r3 = 2;
        r2 = java.lang.Math.min(r3, r2);
        r28 = r4;
        r30 = r37;
        r32 = r11;
        r34 = r7;
        r35 = r6;
        r37 = r2;
        r2 = org.telegram.messenger.WebFile.createWithGeoPoint(r28, r30, r32, r34, r35, r36, r37);
        r1.currentWebFile = r2;
        r2 = r59.isCurrentLocationTimeExpired(r60);
        r1.locationExpired = r2;
        if (r2 != 0) goto L_0x2412;
    L_0x23f9:
        r2 = r1.photoImage;
        r3 = 1;
        r2.setCrossfadeWithOldImage(r3);
        r2 = 0;
        r1.mediaBackground = r2;
        r2 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = r1.invalidateRunnable;
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r2, r4);
        r1.scheduledInvalidate = r3;
        goto L_0x241e;
    L_0x2412:
        r2 = r1.backgroundWidth;
        r3 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r3;
        r1.backgroundWidth = r2;
        r12 = 0;
    L_0x241e:
        r2 = new android.text.StaticLayout;
        r3 = 2131558721; // 0x7f0d0141 float:1.8742766E38 double:1.053129936E-314;
        r4 = "AttachLiveLocation";
        r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_locationTitlePaint;
        r5 = (float) r0;
        r6 = android.text.TextUtils.TruncateAt.END;
        r29 = android.text.TextUtils.ellipsize(r3, r4, r5, r6);
        r30 = org.telegram.p004ui.ActionBar.Theme.chat_locationTitlePaint;
        r32 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r33 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r34 = 0;
        r35 = 0;
        r28 = r2;
        r31 = r0;
        r28.<init>(r29, r30, r31, r32, r33, r34, r35);
        r1.docTitleLayout = r2;
        r59.updateCurrentUserAndChat();
        r2 = r1.currentUser;
        if (r2 == 0) goto L_0x246e;
    L_0x244c:
        r3 = r1.contactAvatarDrawable;
        r3.setInfo(r2);
        r2 = r1.locationImageReceiver;
        r3 = r1.currentUser;
        r4 = 0;
        r28 = org.telegram.messenger.ImageLocation.getForUser(r3, r4);
        r3 = r1.contactAvatarDrawable;
        r31 = 0;
        r4 = r1.currentUser;
        r33 = 0;
        r29 = "50_50";
        r27 = r2;
        r30 = r3;
        r32 = r4;
        r27.setImage(r28, r29, r30, r31, r32, r33);
        goto L_0x24b3;
    L_0x246e:
        r2 = r1.currentChat;
        if (r2 == 0) goto L_0x249e;
    L_0x2472:
        r2 = r2.photo;
        if (r2 == 0) goto L_0x247a;
    L_0x2476:
        r2 = r2.photo_small;
        r1.currentPhoto = r2;
    L_0x247a:
        r2 = r1.contactAvatarDrawable;
        r3 = r1.currentChat;
        r2.setInfo(r3);
        r2 = r1.locationImageReceiver;
        r3 = r1.currentChat;
        r4 = 0;
        r28 = org.telegram.messenger.ImageLocation.getForChat(r3, r4);
        r3 = r1.contactAvatarDrawable;
        r31 = 0;
        r4 = r1.currentChat;
        r33 = 0;
        r29 = "50_50";
        r27 = r2;
        r30 = r3;
        r32 = r4;
        r27.setImage(r28, r29, r30, r31, r32, r33);
        goto L_0x24b3;
    L_0x249e:
        r2 = r1.locationImageReceiver;
        r35 = 0;
        r36 = 0;
        r3 = r1.contactAvatarDrawable;
        r38 = 0;
        r39 = 0;
        r40 = 0;
        r34 = r2;
        r37 = r3;
        r34.setImage(r35, r36, r37, r38, r39, r40);
    L_0x24b3:
        r2 = new android.text.StaticLayout;
        r3 = r14.messageOwner;
        r4 = r3.edit_date;
        if (r4 == 0) goto L_0x24bd;
    L_0x24bb:
        r3 = (long) r4;
        goto L_0x24c0;
    L_0x24bd:
        r3 = r3.date;
        r3 = (long) r3;
    L_0x24c0:
        r29 = org.telegram.messenger.LocaleController.formatLocationUpdateDate(r3);
        r30 = org.telegram.p004ui.ActionBar.Theme.chat_locationAddressPaint;
        r32 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r33 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r34 = 0;
        r35 = 0;
        r28 = r2;
        r31 = r0;
        r28.<init>(r29, r30, r31, r32, r33, r34, r35);
        r1.infoLayout = r2;
        r0 = r63;
        goto L_0x2645;
    L_0x24db:
        r37 = r7;
        r0 = r0.title;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x2648;
    L_0x24e5:
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x2515;
    L_0x24eb:
        r0 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r6 = r1.isChat;
        if (r6 == 0) goto L_0x2500;
    L_0x24f3:
        r6 = r60.needDrawAvatar();
        if (r6 == 0) goto L_0x2500;
    L_0x24f9:
        r6 = r60.isOutOwner();
        if (r6 != 0) goto L_0x2500;
    L_0x24ff:
        goto L_0x2502;
    L_0x2500:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x2502:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r3 = 1133543424; // 0x43908000 float:289.0 double:5.60044864E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = java.lang.Math.min(r0, r3);
        r1.backgroundWidth = r0;
        goto L_0x253e;
    L_0x2515:
        r0 = org.telegram.messenger.AndroidUtilities.displaySize;
        r0 = r0.x;
        r6 = r1.isChat;
        if (r6 == 0) goto L_0x252a;
    L_0x251d:
        r6 = r60.needDrawAvatar();
        if (r6 == 0) goto L_0x252a;
    L_0x2523:
        r6 = r60.isOutOwner();
        if (r6 != 0) goto L_0x252a;
    L_0x2529:
        goto L_0x252c;
    L_0x252a:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x252c:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r3 = 1133543424; // 0x43908000 float:289.0 double:5.60044864E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = java.lang.Math.min(r0, r3);
        r1.backgroundWidth = r0;
    L_0x253e:
        r0 = r1.backgroundWidth;
        r3 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r1.backgroundWidth = r0;
        r0 = r59.checkNeedDrawShareButton(r60);
        if (r0 == 0) goto L_0x255a;
    L_0x254f:
        r0 = r1.backgroundWidth;
        r3 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r1.backgroundWidth = r0;
    L_0x255a:
        r0 = r1.backgroundWidth;
        r3 = 1107820544; // 0x42080000 float:34.0 double:5.473360725E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r1.availableTimeWidth = r0;
        r3 = r1.backgroundWidth;
        r6 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r3 = r3 - r6;
        r6 = 1128464384; // 0x43430000 float:195.0 double:5.575354847E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r7 = 0;
        r1.mediaBackground = r7;
        r7 = r1.currentAccount;
        r8 = (float) r3;
        r9 = org.telegram.messenger.AndroidUtilities.density;
        r10 = r8 / r9;
        r10 = (int) r10;
        r11 = (float) r6;
        r9 = r11 / r9;
        r9 = (int) r9;
        r35 = 1;
        r36 = 15;
        r28 = r7;
        r29 = r4;
        r31 = r37;
        r33 = r10;
        r34 = r9;
        r4 = org.telegram.messenger.AndroidUtilities.formapMapUrl(r28, r29, r31, r33, r34, r35, r36);
        r1.currentUrl = r4;
        r4 = org.telegram.messenger.AndroidUtilities.density;
        r8 = r8 / r4;
        r5 = (int) r8;
        r11 = r11 / r4;
        r7 = (int) r11;
        r8 = 15;
        r9 = (double) r4;
        r9 = java.lang.Math.ceil(r9);
        r4 = (int) r9;
        r9 = 2;
        r4 = java.lang.Math.min(r9, r4);
        r2 = org.telegram.messenger.WebFile.createWithGeoPoint(r2, r5, r7, r8, r4);
        r1.currentWebFile = r2;
        r2 = r14.messageOwner;
        r2 = r2.media;
        r2 = r2.title;
        r28 = org.telegram.p004ui.ActionBar.Theme.chat_locationTitlePaint;
        r30 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r31 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r32 = 0;
        r33 = 0;
        r34 = android.text.TextUtils.TruncateAt.END;
        r36 = 1;
        r27 = r2;
        r29 = r0;
        r35 = r0;
        r2 = org.telegram.p004ui.Components.StaticLayoutEx.createStaticLayout(r27, r28, r29, r30, r31, r32, r33, r34, r35, r36);
        r1.docTitleLayout = r2;
        r9 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r4 = 0;
        r12 = r4 + r2;
        r2 = r1.docTitleLayout;
        r2.getLineCount();
        r2 = r14.messageOwner;
        r2 = r2.media;
        r2 = r2.address;
        r2 = android.text.TextUtils.isEmpty(r2);
        if (r2 != 0) goto L_0x2640;
    L_0x25ea:
        r2 = r14.messageOwner;
        r2 = r2.media;
        r2 = r2.address;
        r28 = org.telegram.p004ui.ActionBar.Theme.chat_locationAddressPaint;
        r30 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r31 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r32 = 0;
        r33 = 0;
        r34 = android.text.TextUtils.TruncateAt.END;
        r36 = 1;
        r27 = r2;
        r29 = r0;
        r35 = r0;
        r0 = org.telegram.p004ui.Components.StaticLayoutEx.createStaticLayout(r27, r28, r29, r30, r31, r32, r33, r34, r35, r36);
        r1.infoLayout = r0;
        r59.measureTime(r60);
        r0 = r1.backgroundWidth;
        r2 = r1.infoLayout;
        r4 = 0;
        r2 = r2.getLineWidth(r4);
        r4 = (double) r2;
        r4 = java.lang.Math.ceil(r4);
        r2 = (int) r4;
        r0 = r0 - r2;
        r2 = 1103101952; // 0x41c00000 float:24.0 double:5.450047783E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        r2 = r1.timeWidth;
        r4 = r60.isOutOwner();
        if (r4 == 0) goto L_0x262f;
    L_0x262c:
        r4 = 20;
        goto L_0x2630;
    L_0x262f:
        r4 = 0;
    L_0x2630:
        r4 = r4 + 20;
        r4 = (float) r4;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r2 = r2 + r4;
        if (r0 >= r2) goto L_0x2643;
    L_0x263a:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r12 = r12 + r0;
        goto L_0x2643;
    L_0x2640:
        r2 = 0;
        r1.infoLayout = r2;
    L_0x2643:
        r0 = r3;
        r9 = r6;
    L_0x2645:
        r8 = 2;
        goto L_0x2712;
    L_0x2648:
        r9 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x267a;
    L_0x2650:
        r0 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r6 = r1.isChat;
        if (r6 == 0) goto L_0x2665;
    L_0x2658:
        r6 = r60.needDrawAvatar();
        if (r6 == 0) goto L_0x2665;
    L_0x265e:
        r6 = r60.isOutOwner();
        if (r6 != 0) goto L_0x2665;
    L_0x2664:
        goto L_0x2667;
    L_0x2665:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x2667:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r3 = 1133543424; // 0x43908000 float:289.0 double:5.60044864E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = java.lang.Math.min(r0, r3);
        r1.backgroundWidth = r0;
        goto L_0x26a3;
    L_0x267a:
        r0 = org.telegram.messenger.AndroidUtilities.displaySize;
        r0 = r0.x;
        r6 = r1.isChat;
        if (r6 == 0) goto L_0x268f;
    L_0x2682:
        r6 = r60.needDrawAvatar();
        if (r6 == 0) goto L_0x268f;
    L_0x2688:
        r6 = r60.isOutOwner();
        if (r6 != 0) goto L_0x268f;
    L_0x268e:
        goto L_0x2691;
    L_0x268f:
        r3 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
    L_0x2691:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r3 = 1133543424; // 0x43908000 float:289.0 double:5.60044864E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = java.lang.Math.min(r0, r3);
        r1.backgroundWidth = r0;
    L_0x26a3:
        r0 = r1.backgroundWidth;
        r3 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r1.backgroundWidth = r0;
        r0 = r59.checkNeedDrawShareButton(r60);
        if (r0 == 0) goto L_0x26bf;
    L_0x26b4:
        r0 = r1.backgroundWidth;
        r3 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r1.backgroundWidth = r0;
    L_0x26bf:
        r0 = r1.backgroundWidth;
        r3 = 1107820544; // 0x42080000 float:34.0 double:5.473360725E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r1.availableTimeWidth = r0;
        r0 = r1.backgroundWidth;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r0 = r0 - r3;
        r3 = 1128464384; // 0x43430000 float:195.0 double:5.575354847E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r6 = r1.currentAccount;
        r7 = (float) r0;
        r8 = org.telegram.messenger.AndroidUtilities.density;
        r9 = r7 / r8;
        r9 = (int) r9;
        r10 = (float) r3;
        r8 = r10 / r8;
        r8 = (int) r8;
        r35 = 1;
        r36 = 15;
        r28 = r6;
        r29 = r4;
        r31 = r37;
        r33 = r9;
        r34 = r8;
        r4 = org.telegram.messenger.AndroidUtilities.formapMapUrl(r28, r29, r31, r33, r34, r35, r36);
        r1.currentUrl = r4;
        r4 = org.telegram.messenger.AndroidUtilities.density;
        r7 = r7 / r4;
        r5 = (int) r7;
        r10 = r10 / r4;
        r6 = (int) r10;
        r7 = 15;
        r8 = (double) r4;
        r8 = java.lang.Math.ceil(r8);
        r4 = (int) r8;
        r8 = 2;
        r4 = java.lang.Math.min(r8, r4);
        r2 = org.telegram.messenger.WebFile.createWithGeoPoint(r2, r5, r6, r7, r4);
        r1.currentWebFile = r2;
        r9 = r3;
        r12 = 0;
    L_0x2712:
        r2 = r60.getDialogId();
        r3 = (int) r2;
        if (r3 != 0) goto L_0x272b;
    L_0x2719:
        r2 = org.telegram.messenger.SharedConfig.mapPreviewType;
        if (r2 != 0) goto L_0x2721;
    L_0x271d:
        r1.currentMapProvider = r8;
    L_0x271f:
        r10 = -1;
        goto L_0x273d;
    L_0x2721:
        r3 = 1;
        if (r2 != r3) goto L_0x2727;
    L_0x2724:
        r1.currentMapProvider = r3;
        goto L_0x271f;
    L_0x2727:
        r10 = -1;
        r1.currentMapProvider = r10;
        goto L_0x273d;
    L_0x272b:
        r10 = -1;
        r2 = r14.currentAccount;
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);
        r2 = r2.mapProvider;
        r1.currentMapProvider = r2;
        r2 = r1.currentMapProvider;
        if (r2 == r10) goto L_0x273d;
    L_0x273a:
        r2 = 2;
        r1.currentMapProvider = r2;
    L_0x273d:
        r2 = r1.currentMapProvider;
        if (r2 != r10) goto L_0x2757;
    L_0x2741:
        r2 = r1.photoImage;
        r3 = 0;
        r4 = 0;
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_locationDrawable;
        r6 = r60.isOutOwner();
        r5 = r5[r6];
        r6 = 0;
        r8 = 0;
        r15 = 8;
        r7 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8);
        goto L_0x27a4;
    L_0x2757:
        r3 = 2;
        r15 = 8;
        if (r2 != r3) goto L_0x277c;
    L_0x275c:
        r2 = r1.currentWebFile;
        if (r2 == 0) goto L_0x27a4;
    L_0x2760:
        r3 = r1.photoImage;
        r4 = org.telegram.messenger.ImageLocation.getForWebFile(r2);
        r5 = 0;
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_locationDrawable;
        r6 = r60.isOutOwner();
        r6 = r2[r6];
        r7 = 0;
        r8 = 0;
        r2 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8);
        goto L_0x27a4;
    L_0x277c:
        r3 = 3;
        if (r2 == r3) goto L_0x2782;
    L_0x277f:
        r3 = 4;
        if (r2 != r3) goto L_0x2790;
    L_0x2782:
        r2 = org.telegram.messenger.ImageLoader.getInstance();
        r3 = r1.currentUrl;
        r4 = r1.currentWebFile;
        r2.addTestWebFile(r3, r4);
        r2 = 1;
        r1.addedForTest = r2;
    L_0x2790:
        r4 = r1.currentUrl;
        if (r4 == 0) goto L_0x27a4;
    L_0x2794:
        r3 = r1.photoImage;
        r5 = 0;
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_locationDrawable;
        r6 = r60.isOutOwner();
        r6 = r2[r6];
        r7 = 0;
        r8 = 0;
        r3.setImage(r4, r5, r6, r7, r8);
    L_0x27a4:
        r57 = r12;
        r2 = 0;
        r15 = 0;
        r29 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r12 = r9;
        goto L_0x342d;
    L_0x27ad:
        r10 = -1;
        r15 = 8;
        r0 = r60.isAnyKindOfSticker();
        if (r0 == 0) goto L_0x2901;
    L_0x27b6:
        r2 = 0;
        r1.drawBackground = r2;
        r0 = r14.type;
        r2 = 13;
        if (r0 != r2) goto L_0x27c1;
    L_0x27bf:
        r0 = 1;
        goto L_0x27c2;
    L_0x27c1:
        r0 = 0;
    L_0x27c2:
        r2 = 0;
        r3 = 0;
        r4 = 0;
    L_0x27c5:
        r5 = r14.messageOwner;
        r5 = r5.media;
        r5 = r5.document;
        r5 = r5.attributes;
        r5 = r5.size();
        if (r2 >= r5) goto L_0x27ec;
    L_0x27d3:
        r5 = r14.messageOwner;
        r5 = r5.media;
        r5 = r5.document;
        r5 = r5.attributes;
        r5 = r5.get(r2);
        r5 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r5;
        r6 = r5 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
        if (r6 == 0) goto L_0x27e9;
    L_0x27e5:
        r3 = r5.f444w;
        r4 = r5.f443h;
    L_0x27e9:
        r2 = r2 + 1;
        goto L_0x27c5;
    L_0x27ec:
        if (r3 != 0) goto L_0x27fa;
    L_0x27ee:
        if (r4 != 0) goto L_0x27fa;
    L_0x27f0:
        r2 = r60.isAnimatedSticker();
        if (r2 == 0) goto L_0x27fa;
    L_0x27f6:
        r3 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r4 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
    L_0x27fa:
        r2 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r2 == 0) goto L_0x2809;
    L_0x2800:
        r2 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r2 = (float) r2;
        r5 = 1053609165; // 0x3ecccccd float:0.4 double:5.205520926E-315;
        goto L_0x2816;
    L_0x2809:
        r2 = org.telegram.messenger.AndroidUtilities.displaySize;
        r5 = r2.x;
        r2 = r2.y;
        r2 = java.lang.Math.min(r5, r2);
        r2 = (float) r2;
        r5 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
    L_0x2816:
        r2 = r2 * r5;
        if (r3 != 0) goto L_0x2822;
    L_0x281a:
        r4 = (int) r2;
        r3 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = r3 + r4;
    L_0x2822:
        r4 = (float) r4;
        r3 = (float) r3;
        r3 = r2 / r3;
        r4 = r4 * r3;
        r3 = (int) r4;
        r4 = (int) r2;
        r5 = (float) r3;
        r6 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1));
        if (r6 <= 0) goto L_0x2837;
    L_0x282f:
        r3 = (float) r4;
        r2 = r2 / r5;
        r3 = r3 * r2;
        r2 = (int) r3;
        r11 = r2;
        r12 = r4;
        goto L_0x2839;
    L_0x2837:
        r12 = r3;
        r11 = r4;
    L_0x2839:
        r2 = 6;
        r1.documentAttachType = r2;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = r11 - r2;
        r1.availableTimeWidth = r2;
        r2 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = r2 + r11;
        r1.backgroundWidth = r2;
        r2 = r14.photoThumbs;
        r3 = 40;
        r2 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r2, r3);
        r1.currentPhotoObjectThumb = r2;
        r2 = r14.photoThumbsObject;
        r1.photoParentObject = r2;
        r2 = r14.attachPathExists;
        if (r2 == 0) goto L_0x28a6;
    L_0x2861:
        r2 = r1.photoImage;
        r3 = r14.messageOwner;
        r3 = r3.attachPath;
        r3 = org.telegram.messenger.ImageLocation.getForPath(r3);
        r4 = java.util.Locale.US;
        r5 = 2;
        r6 = new java.lang.Object[r5];
        r5 = java.lang.Integer.valueOf(r11);
        r7 = 0;
        r6[r7] = r5;
        r5 = java.lang.Integer.valueOf(r12);
        r7 = 1;
        r6[r7] = r5;
        r5 = "%d_%d";
        r4 = java.lang.String.format(r4, r5, r6);
        r5 = r1.currentPhotoObjectThumb;
        r6 = r1.photoParentObject;
        r5 = org.telegram.messenger.ImageLocation.getForObject(r5, r6);
        r6 = r14.messageOwner;
        r6 = r6.media;
        r6 = r6.document;
        r7 = r6.size;
        if (r0 == 0) goto L_0x289a;
    L_0x2896:
        r0 = "webp";
        r8 = r0;
        goto L_0x289b;
    L_0x289a:
        r8 = 0;
    L_0x289b:
        r0 = 1;
        r6 = "b1";
        r9 = r60;
        r13 = -1;
        r10 = r0;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x28f8;
    L_0x28a6:
        r13 = -1;
        r2 = r14.messageOwner;
        r2 = r2.media;
        r2 = r2.document;
        r3 = r2.f441id;
        r5 = 0;
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 == 0) goto L_0x28f8;
    L_0x28b5:
        r3 = r1.photoImage;
        r4 = org.telegram.messenger.ImageLocation.getForDocument(r2);
        r2 = java.util.Locale.US;
        r5 = 2;
        r6 = new java.lang.Object[r5];
        r5 = java.lang.Integer.valueOf(r11);
        r7 = 0;
        r6[r7] = r5;
        r5 = java.lang.Integer.valueOf(r12);
        r7 = 1;
        r6[r7] = r5;
        r5 = "%d_%d";
        r5 = java.lang.String.format(r2, r5, r6);
        r2 = r1.currentPhotoObjectThumb;
        r6 = r1.photoParentObject;
        r6 = org.telegram.messenger.ImageLocation.getForObject(r2, r6);
        r2 = r14.messageOwner;
        r2 = r2.media;
        r2 = r2.document;
        r7 = r2.size;
        if (r0 == 0) goto L_0x28ea;
    L_0x28e6:
        r0 = "webp";
        r8 = r0;
        goto L_0x28eb;
    L_0x28ea:
        r8 = 0;
    L_0x28eb:
        r10 = 1;
        r0 = "b1";
        r2 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r6;
        r6 = r0;
        r9 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10);
    L_0x28f8:
        r0 = r11;
    L_0x28f9:
        r2 = 0;
        r15 = 0;
        r29 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r57 = 0;
        goto L_0x342d;
    L_0x2901:
        r13 = -1;
        r0 = r14.photoThumbs;
        r2 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        r0 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r0, r2);
        r1.currentPhotoObject = r0;
        r0 = r14.photoThumbsObject;
        r1.photoParentObject = r0;
        r0 = r14.type;
        r2 = 5;
        if (r0 != r2) goto L_0x2924;
    L_0x2917:
        r0 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r2 = r60.getDocument();
        r1.documentAttach = r2;
        r2 = 7;
        r1.documentAttachType = r2;
    L_0x2922:
        r2 = 0;
        goto L_0x296a;
    L_0x2924:
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x2936;
    L_0x292a:
        r0 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
    L_0x292e:
        r0 = (float) r0;
        r2 = 1060320051; // 0x3f333333 float:0.7 double:5.23867711E-315;
        r0 = r0 * r2;
        r0 = (int) r0;
        goto L_0x2922;
    L_0x2936:
        r0 = r1.currentPhotoObject;
        if (r0 == 0) goto L_0x295f;
    L_0x293a:
        r0 = r14.type;
        r2 = 1;
        if (r0 == r2) goto L_0x2944;
    L_0x293f:
        r2 = 3;
        if (r0 == r2) goto L_0x2944;
    L_0x2942:
        if (r0 != r15) goto L_0x295f;
    L_0x2944:
        r0 = r1.currentPhotoObject;
        r2 = r0.f465w;
        r0 = r0.f464h;
        if (r2 < r0) goto L_0x295f;
    L_0x294c:
        r0 = org.telegram.messenger.AndroidUtilities.displaySize;
        r2 = r0.x;
        r0 = r0.y;
        r0 = java.lang.Math.min(r2, r0);
        r2 = 1115684864; // 0x42800000 float:64.0 double:5.51221563E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        r2 = 1;
        goto L_0x296a;
    L_0x295f:
        r0 = org.telegram.messenger.AndroidUtilities.displaySize;
        r2 = r0.x;
        r0 = r0.y;
        r0 = java.lang.Math.min(r2, r0);
        goto L_0x292e;
    L_0x296a:
        r3 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = r3 + r0;
        if (r2 != 0) goto L_0x29a4;
    L_0x2973:
        r2 = r14.type;
        r4 = 5;
        if (r2 == r4) goto L_0x298e;
    L_0x2978:
        r2 = r59.checkNeedDrawShareButton(r60);
        if (r2 == 0) goto L_0x298e;
    L_0x297e:
        r2 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = r0 - r2;
        r4 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r0 = r0 - r4;
        goto L_0x298f;
    L_0x298e:
        r2 = r0;
    L_0x298f:
        r4 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        if (r0 <= r4) goto L_0x2999;
    L_0x2995:
        r0 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
    L_0x2999:
        r4 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        if (r3 <= r4) goto L_0x29c3;
    L_0x299f:
        r3 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        goto L_0x29c3;
    L_0x29a4:
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x29c2;
    L_0x29a8:
        r2 = r60.needDrawAvatar();
        if (r2 == 0) goto L_0x29c2;
    L_0x29ae:
        r2 = r60.isOutOwner();
        if (r2 != 0) goto L_0x29c2;
    L_0x29b4:
        r2 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = r0 - r2;
        r58 = r2;
        r2 = r0;
        r0 = r58;
        goto L_0x29c3;
    L_0x29c2:
        r2 = r0;
    L_0x29c3:
        r4 = r14.type;
        r5 = 1;
        if (r4 != r5) goto L_0x29d6;
    L_0x29c8:
        r59.updateSecretTimeText(r60);
        r4 = r14.photoThumbs;
        r5 = 40;
        r4 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r4, r5);
        r1.currentPhotoObjectThumb = r4;
        goto L_0x29ea;
    L_0x29d6:
        r5 = 3;
        if (r4 == r5) goto L_0x29ec;
    L_0x29d9:
        if (r4 != r15) goto L_0x29dc;
    L_0x29db:
        goto L_0x29ec;
    L_0x29dc:
        r5 = 5;
        if (r4 != r5) goto L_0x29ea;
    L_0x29df:
        r4 = r14.photoThumbs;
        r5 = 40;
        r4 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r4, r5);
        r1.currentPhotoObjectThumb = r4;
        goto L_0x29fd;
    L_0x29ea:
        r4 = 0;
        goto L_0x29fe;
    L_0x29ec:
        r4 = 0;
        r1.createDocumentLayout(r4, r14);
        r4 = r14.photoThumbs;
        r5 = 40;
        r4 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r4, r5);
        r1.currentPhotoObjectThumb = r4;
        r59.updateSecretTimeText(r60);
    L_0x29fd:
        r4 = 1;
    L_0x29fe:
        r5 = r14.type;
        r6 = 5;
        if (r5 != r6) goto L_0x2a08;
    L_0x2a03:
        r5 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r8 = r5;
        goto L_0x2a7b;
    L_0x2a08:
        r5 = r1.currentPhotoObject;
        if (r5 == 0) goto L_0x2a0d;
    L_0x2a0c:
        goto L_0x2a0f;
    L_0x2a0d:
        r5 = r1.currentPhotoObjectThumb;
    L_0x2a0f:
        if (r5 == 0) goto L_0x2a16;
    L_0x2a11:
        r12 = r5.f465w;
        r5 = r5.f464h;
        goto L_0x2a3e;
    L_0x2a16:
        r5 = r1.documentAttach;
        if (r5 == 0) goto L_0x2a3c;
    L_0x2a1a:
        r5 = r5.attributes;
        r5 = r5.size();
        r6 = 0;
        r7 = 0;
        r12 = 0;
    L_0x2a23:
        if (r6 >= r5) goto L_0x2a3a;
    L_0x2a25:
        r8 = r1.documentAttach;
        r8 = r8.attributes;
        r8 = r8.get(r6);
        r8 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r8;
        r9 = r8 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
        if (r9 == 0) goto L_0x2a37;
    L_0x2a33:
        r12 = r8.f444w;
        r7 = r8.f443h;
    L_0x2a37:
        r6 = r6 + 1;
        goto L_0x2a23;
    L_0x2a3a:
        r5 = r7;
        goto L_0x2a3e;
    L_0x2a3c:
        r5 = 0;
        r12 = 0;
    L_0x2a3e:
        r6 = (float) r12;
        r7 = (float) r0;
        r8 = r6 / r7;
        r9 = r6 / r8;
        r9 = (int) r9;
        r5 = (float) r5;
        r8 = r5 / r8;
        r8 = (int) r8;
        if (r9 != 0) goto L_0x2a51;
    L_0x2a4b:
        r9 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
    L_0x2a51:
        if (r8 != 0) goto L_0x2a59;
    L_0x2a53:
        r8 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r8 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
    L_0x2a59:
        if (r8 <= r3) goto L_0x2a63;
    L_0x2a5b:
        r5 = (float) r8;
        r6 = (float) r3;
        r5 = r5 / r6;
        r6 = (float) r9;
        r6 = r6 / r5;
        r5 = (int) r6;
        r8 = r3;
        goto L_0x2a7b;
    L_0x2a63:
        r10 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        if (r8 >= r10) goto L_0x2a7a;
    L_0x2a6b:
        r8 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r8 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r10 = (float) r8;
        r5 = r5 / r10;
        r6 = r6 / r5;
        r5 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r5 >= 0) goto L_0x2a7a;
    L_0x2a78:
        r5 = (int) r6;
        goto L_0x2a7b;
    L_0x2a7a:
        r5 = r9;
    L_0x2a7b:
        r6 = r1.currentPhotoObject;
        if (r6 == 0) goto L_0x2a8d;
    L_0x2a7f:
        r6 = r6.type;
        r7 = "s";
        r6 = r7.equals(r6);
        if (r6 == 0) goto L_0x2a8d;
    L_0x2a89:
        r6 = 0;
        r1.currentPhotoObject = r6;
        goto L_0x2a8e;
    L_0x2a8d:
        r6 = 0;
    L_0x2a8e:
        r7 = r1.currentPhotoObject;
        if (r7 == 0) goto L_0x2aa0;
    L_0x2a92:
        r9 = r1.currentPhotoObjectThumb;
        if (r7 != r9) goto L_0x2aa0;
    L_0x2a96:
        r7 = r14.type;
        r9 = 1;
        if (r7 != r9) goto L_0x2a9e;
    L_0x2a9b:
        r1.currentPhotoObjectThumb = r6;
        goto L_0x2aa0;
    L_0x2a9e:
        r1.currentPhotoObject = r6;
    L_0x2aa0:
        if (r4 == 0) goto L_0x2ac9;
    L_0x2aa2:
        r4 = r60.needDrawBluredPreview();
        if (r4 != 0) goto L_0x2ac9;
    L_0x2aa8:
        r4 = r1.currentPhotoObject;
        if (r4 == 0) goto L_0x2ab0;
    L_0x2aac:
        r6 = r1.currentPhotoObjectThumb;
        if (r4 != r6) goto L_0x2ac9;
    L_0x2ab0:
        r4 = r1.currentPhotoObjectThumb;
        if (r4 == 0) goto L_0x2abe;
    L_0x2ab4:
        r4 = r4.type;
        r6 = "m";
        r4 = r6.equals(r4);
        if (r4 != 0) goto L_0x2ac9;
    L_0x2abe:
        r4 = r1.photoImage;
        r6 = 1;
        r4.setNeedsQualityThumb(r6);
        r4 = r1.photoImage;
        r4.setShouldGenerateQualityThumb(r6);
    L_0x2ac9:
        r4 = r1.currentMessagesGroup;
        if (r4 != 0) goto L_0x2ad4;
    L_0x2acd:
        r4 = r14.caption;
        if (r4 == 0) goto L_0x2ad4;
    L_0x2ad1:
        r4 = 0;
        r1.mediaBackground = r4;
    L_0x2ad4:
        if (r5 == 0) goto L_0x2ad8;
    L_0x2ad6:
        if (r8 != 0) goto L_0x2b3e;
    L_0x2ad8:
        r4 = r14.type;
        if (r4 != r15) goto L_0x2b3e;
    L_0x2adc:
        r4 = 0;
    L_0x2add:
        r6 = r14.messageOwner;
        r6 = r6.media;
        r6 = r6.document;
        r6 = r6.attributes;
        r6 = r6.size();
        if (r4 >= r6) goto L_0x2b3e;
    L_0x2aeb:
        r6 = r14.messageOwner;
        r6 = r6.media;
        r6 = r6.document;
        r6 = r6.attributes;
        r6 = r6.get(r4);
        r6 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r6;
        r7 = r6 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
        if (r7 != 0) goto L_0x2b05;
    L_0x2afd:
        r7 = r6 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
        if (r7 == 0) goto L_0x2b02;
    L_0x2b01:
        goto L_0x2b05;
    L_0x2b02:
        r4 = r4 + 1;
        goto L_0x2add;
    L_0x2b05:
        r4 = r6.f444w;
        r5 = (float) r4;
        r0 = (float) r0;
        r5 = r5 / r0;
        r4 = (float) r4;
        r4 = r4 / r5;
        r4 = (int) r4;
        r7 = r6.f443h;
        r7 = (float) r7;
        r7 = r7 / r5;
        r5 = (int) r7;
        if (r5 <= r3) goto L_0x2b1b;
    L_0x2b14:
        r0 = (float) r5;
        r5 = (float) r3;
        r0 = r0 / r5;
        r4 = (float) r4;
        r4 = r4 / r0;
        r5 = (int) r4;
        goto L_0x2b3f;
    L_0x2b1b:
        r3 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        if (r5 >= r3) goto L_0x2b3b;
    L_0x2b23:
        r3 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r5 = r6.f443h;
        r5 = (float) r5;
        r7 = (float) r3;
        r5 = r5 / r7;
        r6 = r6.f444w;
        r7 = (float) r6;
        r7 = r7 / r5;
        r0 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1));
        if (r0 >= 0) goto L_0x2b3c;
    L_0x2b36:
        r0 = (float) r6;
        r0 = r0 / r5;
        r0 = (int) r0;
        r5 = r0;
        goto L_0x2b3f;
    L_0x2b3b:
        r3 = r5;
    L_0x2b3c:
        r5 = r4;
        goto L_0x2b3f;
    L_0x2b3e:
        r3 = r8;
    L_0x2b3f:
        if (r5 == 0) goto L_0x2b43;
    L_0x2b41:
        if (r3 != 0) goto L_0x2b4a;
    L_0x2b43:
        r0 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r3 = r5;
    L_0x2b4a:
        r0 = r14.type;
        r4 = 3;
        if (r0 != r4) goto L_0x2b64;
    L_0x2b4f:
        r0 = r1.infoWidth;
        r4 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r0 = r0 + r4;
        if (r5 >= r0) goto L_0x2b64;
    L_0x2b5a:
        r0 = r1.infoWidth;
        r4 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r5 = r0 + r4;
    L_0x2b64:
        r0 = r1.currentMessagesGroup;
        if (r0 == 0) goto L_0x2baa;
    L_0x2b68:
        r0 = r59.getGroupPhotosWidth();
        r2 = 0;
        r4 = 0;
    L_0x2b6e:
        r6 = r1.currentMessagesGroup;
        r6 = r6.posArray;
        r6 = r6.size();
        if (r2 >= r6) goto L_0x2ba0;
    L_0x2b78:
        r6 = r1.currentMessagesGroup;
        r6 = r6.posArray;
        r6 = r6.get(r2);
        r6 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r6;
        r7 = r6.minY;
        if (r7 != 0) goto L_0x2ba0;
    L_0x2b86:
        r7 = (double) r4;
        r4 = r6.f57pw;
        r6 = r6.leftSpanOffset;
        r4 = r4 + r6;
        r4 = (float) r4;
        r6 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r4 = r4 / r6;
        r6 = (float) r0;
        r4 = r4 * r6;
        r9 = (double) r4;
        r9 = java.lang.Math.ceil(r9);
        java.lang.Double.isNaN(r7);
        r7 = r7 + r9;
        r4 = (int) r7;
        r2 = r2 + 1;
        goto L_0x2b6e;
    L_0x2ba0:
        r0 = 1108082688; // 0x420c0000 float:35.0 double:5.47465589E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r4 = r4 - r0;
        r1.availableTimeWidth = r4;
        goto L_0x2bb3;
    L_0x2baa:
        r0 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r2 = r2 - r0;
        r1.availableTimeWidth = r2;
    L_0x2bb3:
        r0 = r14.type;
        r2 = 5;
        if (r0 != r2) goto L_0x2bda;
    L_0x2bb8:
        r0 = r1.availableTimeWidth;
        r6 = (double) r0;
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_audioTimePaint;
        r2 = "00:00";
        r0 = r0.measureText(r2);
        r8 = (double) r0;
        r8 = java.lang.Math.ceil(r8);
        r0 = 1104150528; // 0x41d00000 float:26.0 double:5.455228437E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r10 = (double) r0;
        java.lang.Double.isNaN(r10);
        r8 = r8 + r10;
        java.lang.Double.isNaN(r6);
        r6 = r6 - r8;
        r0 = (int) r6;
        r1.availableTimeWidth = r0;
    L_0x2bda:
        r59.measureTime(r60);
        r0 = r1.timeWidth;
        r2 = r60.isOutOwner();
        if (r2 == 0) goto L_0x2be8;
    L_0x2be5:
        r12 = 20;
        goto L_0x2be9;
    L_0x2be8:
        r12 = 0;
    L_0x2be9:
        r12 = r12 + 14;
        r2 = (float) r12;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 + r2;
        if (r5 >= r0) goto L_0x2bf4;
    L_0x2bf3:
        r5 = r0;
    L_0x2bf4:
        r2 = r60.isRoundVideo();
        if (r2 == 0) goto L_0x2c0a;
    L_0x2bfa:
        r3 = java.lang.Math.min(r5, r3);
        r2 = 0;
        r1.drawBackground = r2;
        r2 = r1.photoImage;
        r4 = r3 / 2;
        r2.setRoundRadius(r4);
    L_0x2c08:
        r5 = r3;
        goto L_0x2c2c;
    L_0x2c0a:
        r2 = r60.needDrawBluredPreview();
        if (r2 == 0) goto L_0x2c2c;
    L_0x2c10:
        r2 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r2 == 0) goto L_0x2c1b;
    L_0x2c16:
        r2 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        goto L_0x2c25;
    L_0x2c1b:
        r2 = org.telegram.messenger.AndroidUtilities.displaySize;
        r3 = r2.x;
        r2 = r2.y;
        r2 = java.lang.Math.min(r3, r2);
    L_0x2c25:
        r2 = (float) r2;
        r3 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r2 = r2 * r3;
        r3 = (int) r2;
        goto L_0x2c08;
    L_0x2c2c:
        r2 = r1.currentMessagesGroup;
        if (r2 == 0) goto L_0x2f4c;
    L_0x2c30:
        r2 = org.telegram.messenger.AndroidUtilities.displaySize;
        r3 = r2.x;
        r2 = r2.y;
        r2 = java.lang.Math.max(r3, r2);
        r2 = (float) r2;
        r3 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r2 = r2 * r3;
        r3 = r59.getGroupPhotosWidth();
        r4 = r1.currentPosition;
        r4 = r4.f57pw;
        r4 = (float) r4;
        r5 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r4 = r4 / r5;
        r3 = (float) r3;
        r4 = r4 * r3;
        r4 = (double) r4;
        r4 = java.lang.Math.ceil(r4);
        r4 = (int) r4;
        r5 = r1.currentPosition;
        r5 = r5.minY;
        if (r5 == 0) goto L_0x2cf0;
    L_0x2c5a:
        r5 = r60.isOutOwner();
        if (r5 == 0) goto L_0x2c68;
    L_0x2c60:
        r5 = r1.currentPosition;
        r5 = r5.flags;
        r6 = 1;
        r5 = r5 & r6;
        if (r5 != 0) goto L_0x2c76;
    L_0x2c68:
        r5 = r60.isOutOwner();
        if (r5 != 0) goto L_0x2cf0;
    L_0x2c6e:
        r5 = r1.currentPosition;
        r5 = r5.flags;
        r6 = 2;
        r5 = r5 & r6;
        if (r5 == 0) goto L_0x2cf0;
    L_0x2c76:
        r5 = 0;
        r6 = 0;
        r7 = 0;
    L_0x2c79:
        r8 = r1.currentMessagesGroup;
        r8 = r8.posArray;
        r8 = r8.size();
        if (r5 >= r8) goto L_0x2cee;
    L_0x2c83:
        r8 = r1.currentMessagesGroup;
        r8 = r8.posArray;
        r8 = r8.get(r5);
        r8 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r8;
        r9 = r8.minY;
        if (r9 != 0) goto L_0x2cb8;
    L_0x2c91:
        r9 = (double) r6;
        r6 = r8.f57pw;
        r6 = (float) r6;
        r11 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r6 = r6 / r11;
        r6 = r6 * r3;
        r11 = (double) r6;
        r11 = java.lang.Math.ceil(r11);
        r6 = r8.leftSpanOffset;
        if (r6 == 0) goto L_0x2caf;
    L_0x2ca3:
        r6 = (float) r6;
        r8 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r6 = r6 / r8;
        r6 = r6 * r3;
        r13 = (double) r6;
        r13 = java.lang.Math.ceil(r13);
        goto L_0x2cb1;
    L_0x2caf:
        r13 = 0;
    L_0x2cb1:
        r11 = r11 + r13;
        java.lang.Double.isNaN(r9);
        r9 = r9 + r11;
        r6 = (int) r9;
        goto L_0x2ce8;
    L_0x2cb8:
        r10 = r1.currentPosition;
        r10 = r10.minY;
        if (r9 != r10) goto L_0x2ce5;
    L_0x2cbe:
        r9 = (double) r7;
        r7 = r8.f57pw;
        r7 = (float) r7;
        r11 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r7 = r7 / r11;
        r7 = r7 * r3;
        r11 = (double) r7;
        r11 = java.lang.Math.ceil(r11);
        r7 = r8.leftSpanOffset;
        if (r7 == 0) goto L_0x2cdc;
    L_0x2cd0:
        r7 = (float) r7;
        r8 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r7 = r7 / r8;
        r7 = r7 * r3;
        r7 = (double) r7;
        r7 = java.lang.Math.ceil(r7);
        goto L_0x2cde;
    L_0x2cdc:
        r7 = 0;
    L_0x2cde:
        r11 = r11 + r7;
        java.lang.Double.isNaN(r9);
        r9 = r9 + r11;
        r7 = (int) r9;
        goto L_0x2ce8;
    L_0x2ce5:
        if (r9 <= r10) goto L_0x2ce8;
    L_0x2ce7:
        goto L_0x2cee;
    L_0x2ce8:
        r5 = r5 + 1;
        r14 = r60;
        r13 = -1;
        goto L_0x2c79;
    L_0x2cee:
        r6 = r6 - r7;
        r4 = r4 + r6;
    L_0x2cf0:
        r5 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r4 = r4 - r5;
        r5 = r1.isAvatarVisible;
        if (r5 == 0) goto L_0x2d02;
    L_0x2cfb:
        r5 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r4 = r4 - r5;
    L_0x2d02:
        r5 = r1.currentPosition;
        r6 = r5.siblingHeights;
        if (r6 == 0) goto L_0x2d32;
    L_0x2d08:
        r5 = 0;
        r6 = 0;
    L_0x2d0a:
        r7 = r1.currentPosition;
        r8 = r7.siblingHeights;
        r9 = r8.length;
        if (r5 >= r9) goto L_0x2d1f;
    L_0x2d11:
        r7 = r8[r5];
        r7 = r7 * r2;
        r7 = (double) r7;
        r7 = java.lang.Math.ceil(r7);
        r7 = (int) r7;
        r6 = r6 + r7;
        r5 = r5 + 1;
        goto L_0x2d0a;
    L_0x2d1f:
        r2 = r7.maxY;
        r5 = r7.minY;
        r2 = r2 - r5;
        r5 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r7 = org.telegram.messenger.AndroidUtilities.density;
        r7 = r7 * r5;
        r5 = java.lang.Math.round(r7);
        r2 = r2 * r5;
        r6 = r6 + r2;
        goto L_0x2d3c;
    L_0x2d32:
        r5 = r5.f56ph;
        r2 = r2 * r5;
        r5 = (double) r2;
        r5 = java.lang.Math.ceil(r5);
        r6 = (int) r5;
    L_0x2d3c:
        r1.backgroundWidth = r4;
        r2 = r1.currentPosition;
        r2 = r2.flags;
        r5 = r2 & 2;
        if (r5 == 0) goto L_0x2d50;
    L_0x2d46:
        r5 = 1;
        r2 = r2 & r5;
        if (r2 == 0) goto L_0x2d50;
    L_0x2d4a:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
    L_0x2d4e:
        r4 = r4 - r2;
        goto L_0x2d77;
    L_0x2d50:
        r2 = r1.currentPosition;
        r2 = r2.flags;
        r5 = r2 & 2;
        if (r5 != 0) goto L_0x2d63;
    L_0x2d58:
        r5 = 1;
        r2 = r2 & r5;
        if (r2 != 0) goto L_0x2d63;
    L_0x2d5c:
        r2 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        goto L_0x2d4e;
    L_0x2d63:
        r2 = r1.currentPosition;
        r2 = r2.flags;
        r5 = 2;
        r2 = r2 & r5;
        if (r2 == 0) goto L_0x2d70;
    L_0x2d6b:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        goto L_0x2d4e;
    L_0x2d70:
        r2 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        goto L_0x2d4e;
    L_0x2d77:
        r2 = r1.currentPosition;
        r2 = r2.edge;
        if (r2 != 0) goto L_0x2d84;
    L_0x2d7d:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r2 = r2 + r4;
        r5 = r2;
        goto L_0x2d85;
    L_0x2d84:
        r5 = r4;
    L_0x2d85:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r2 = r5 - r2;
        r7 = 0;
        r12 = r7 + r2;
        r2 = r1.currentPosition;
        r2 = r2.flags;
        r7 = r2 & 8;
        if (r7 != 0) goto L_0x2da8;
    L_0x2d96:
        r7 = r1.currentMessagesGroup;
        r7 = r7.hasSibling;
        if (r7 == 0) goto L_0x2da1;
    L_0x2d9c:
        r2 = r2 & 4;
        if (r2 != 0) goto L_0x2da1;
    L_0x2da0:
        goto L_0x2da8;
    L_0x2da1:
        r21 = r5;
        r63 = r6;
        r5 = r4;
        goto L_0x2f43;
    L_0x2da8:
        r2 = r1.currentPosition;
        r2 = r1.getAdditionalWidthForPosition(r2);
        r12 = r12 + r2;
        r2 = r1.currentMessagesGroup;
        r2 = r2.messages;
        r2 = r2.size();
        r7 = r4;
        r4 = 0;
    L_0x2db9:
        if (r4 >= r2) goto L_0x2f3c;
    L_0x2dbb:
        r8 = r1.currentMessagesGroup;
        r8 = r8.messages;
        r8 = r8.get(r4);
        r8 = (org.telegram.messenger.MessageObject) r8;
        r9 = r1.currentMessagesGroup;
        r9 = r9.posArray;
        r9 = r9.get(r4);
        r9 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r9;
        r10 = r1.currentPosition;
        if (r9 == r10) goto L_0x2f1c;
    L_0x2dd3:
        r10 = r9.flags;
        r10 = r10 & r15;
        if (r10 == 0) goto L_0x2f1c;
    L_0x2dd8:
        r7 = r9.f57pw;
        r7 = (float) r7;
        r10 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r7 = r7 / r10;
        r7 = r7 * r3;
        r10 = (double) r7;
        r10 = java.lang.Math.ceil(r10);
        r7 = (int) r10;
        r10 = r9.minY;
        if (r10 == 0) goto L_0x2e95;
    L_0x2dea:
        r10 = r60.isOutOwner();
        if (r10 == 0) goto L_0x2df6;
    L_0x2df0:
        r10 = r9.flags;
        r11 = 1;
        r10 = r10 & r11;
        if (r10 != 0) goto L_0x2e02;
    L_0x2df6:
        r10 = r60.isOutOwner();
        if (r10 != 0) goto L_0x2e95;
    L_0x2dfc:
        r10 = r9.flags;
        r11 = 2;
        r10 = r10 & r11;
        if (r10 == 0) goto L_0x2e95;
    L_0x2e02:
        r10 = 0;
        r11 = 0;
        r13 = 0;
    L_0x2e05:
        r14 = r1.currentMessagesGroup;
        r14 = r14.posArray;
        r14 = r14.size();
        if (r10 >= r14) goto L_0x2e8c;
    L_0x2e0f:
        r14 = r1.currentMessagesGroup;
        r14 = r14.posArray;
        r14 = r14.get(r10);
        r14 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r14;
        r15 = r14.minY;
        if (r15 != 0) goto L_0x2e4b;
    L_0x2e1d:
        r21 = r5;
        r63 = r6;
        r5 = (double) r11;
        r11 = r14.f57pw;
        r11 = (float) r11;
        r15 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r11 = r11 / r15;
        r11 = r11 * r3;
        r22 = r12;
        r11 = (double) r11;
        r11 = java.lang.Math.ceil(r11);
        r14 = r14.leftSpanOffset;
        if (r14 == 0) goto L_0x2e41;
    L_0x2e35:
        r14 = (float) r14;
        r15 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r14 = r14 / r15;
        r14 = r14 * r3;
        r14 = (double) r14;
        r14 = java.lang.Math.ceil(r14);
        goto L_0x2e43;
    L_0x2e41:
        r14 = 0;
    L_0x2e43:
        r11 = r11 + r14;
        java.lang.Double.isNaN(r5);
        r5 = r5 + r11;
        r5 = (int) r5;
        r11 = r5;
        goto L_0x2e80;
    L_0x2e4b:
        r21 = r5;
        r63 = r6;
        r22 = r12;
        r5 = r9.minY;
        if (r15 != r5) goto L_0x2e7d;
    L_0x2e55:
        r5 = (double) r13;
        r12 = r14.f57pw;
        r12 = (float) r12;
        r13 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r12 = r12 / r13;
        r12 = r12 * r3;
        r12 = (double) r12;
        r12 = java.lang.Math.ceil(r12);
        r14 = r14.leftSpanOffset;
        if (r14 == 0) goto L_0x2e73;
    L_0x2e67:
        r14 = (float) r14;
        r15 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r14 = r14 / r15;
        r14 = r14 * r3;
        r14 = (double) r14;
        r14 = java.lang.Math.ceil(r14);
        goto L_0x2e75;
    L_0x2e73:
        r14 = 0;
    L_0x2e75:
        r12 = r12 + r14;
        java.lang.Double.isNaN(r5);
        r5 = r5 + r12;
        r5 = (int) r5;
        r13 = r5;
        goto L_0x2e80;
    L_0x2e7d:
        if (r15 <= r5) goto L_0x2e80;
    L_0x2e7f:
        goto L_0x2e92;
    L_0x2e80:
        r10 = r10 + 1;
        r6 = r63;
        r5 = r21;
        r12 = r22;
        r15 = 8;
        goto L_0x2e05;
    L_0x2e8c:
        r21 = r5;
        r63 = r6;
        r22 = r12;
    L_0x2e92:
        r11 = r11 - r13;
        r7 = r7 + r11;
        goto L_0x2e9b;
    L_0x2e95:
        r21 = r5;
        r63 = r6;
        r22 = r12;
    L_0x2e9b:
        r5 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r7 = r7 - r5;
        r5 = r9.flags;
        r6 = r5 & 2;
        if (r6 == 0) goto L_0x2eb2;
    L_0x2ea8:
        r5 = r5 & 1;
        if (r5 == 0) goto L_0x2eb2;
    L_0x2eac:
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
    L_0x2eb0:
        r7 = r7 - r5;
        goto L_0x2ed5;
    L_0x2eb2:
        r5 = r9.flags;
        r6 = r5 & 2;
        if (r6 != 0) goto L_0x2ec3;
    L_0x2eb8:
        r5 = r5 & 1;
        if (r5 != 0) goto L_0x2ec3;
    L_0x2ebc:
        r5 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        goto L_0x2eb0;
    L_0x2ec3:
        r5 = r9.flags;
        r6 = 2;
        r5 = r5 & r6;
        if (r5 == 0) goto L_0x2ece;
    L_0x2ec9:
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        goto L_0x2eb0;
    L_0x2ece:
        r5 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        goto L_0x2eb0;
    L_0x2ed5:
        r5 = r1.isChat;
        if (r5 == 0) goto L_0x2ef2;
    L_0x2ed9:
        r5 = r8.isOutOwner();
        if (r5 != 0) goto L_0x2ef2;
    L_0x2edf:
        r5 = r8.needDrawAvatar();
        if (r5 == 0) goto L_0x2ef2;
    L_0x2ee5:
        if (r9 == 0) goto L_0x2eeb;
    L_0x2ee7:
        r5 = r9.edge;
        if (r5 == 0) goto L_0x2ef2;
    L_0x2eeb:
        r5 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r7 = r7 - r5;
    L_0x2ef2:
        r5 = r1.getAdditionalWidthForPosition(r9);
        r7 = r7 + r5;
        r5 = r9.edge;
        if (r5 != 0) goto L_0x2f00;
    L_0x2efb:
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r7 = r7 + r5;
    L_0x2f00:
        r12 = r22 + r7;
        r5 = r9.minX;
        r6 = r1.currentPosition;
        r6 = r6.minX;
        if (r5 < r6) goto L_0x2f16;
    L_0x2f0a:
        r5 = r1.currentMessagesGroup;
        r5 = r5.hasSibling;
        if (r5 == 0) goto L_0x2f24;
    L_0x2f10:
        r5 = r9.minY;
        r6 = r9.maxY;
        if (r5 == r6) goto L_0x2f24;
    L_0x2f16:
        r5 = r1.captionOffsetX;
        r5 = r5 - r7;
        r1.captionOffsetX = r5;
        goto L_0x2f24;
    L_0x2f1c:
        r21 = r5;
        r63 = r6;
        r22 = r12;
        r12 = r22;
    L_0x2f24:
        r5 = r8.caption;
        if (r5 == 0) goto L_0x2f32;
    L_0x2f28:
        r6 = r1.currentCaption;
        if (r6 == 0) goto L_0x2f30;
    L_0x2f2c:
        r6 = 0;
        r1.currentCaption = r6;
        goto L_0x2f42;
    L_0x2f30:
        r1.currentCaption = r5;
    L_0x2f32:
        r4 = r4 + 1;
        r6 = r63;
        r5 = r21;
        r15 = 8;
        goto L_0x2db9;
    L_0x2f3c:
        r21 = r5;
        r63 = r6;
        r22 = r12;
    L_0x2f42:
        r5 = r7;
    L_0x2f43:
        r14 = r60;
        r15 = r63;
        r2 = r5;
        r5 = r21;
        r3 = 0;
        goto L_0x2f9c;
    L_0x2f4c:
        r2 = r14.caption;
        r1.currentCaption = r2;
        r2 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r2 == 0) goto L_0x2f5b;
    L_0x2f56:
        r2 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        goto L_0x2f65;
    L_0x2f5b:
        r2 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r2.x;
        r2 = r2.y;
        r2 = java.lang.Math.min(r4, r2);
    L_0x2f65:
        r2 = (float) r2;
        r4 = 1059481190; // 0x3f266666 float:0.65 double:5.234532584E-315;
        r2 = r2 * r4;
        r2 = (int) r2;
        r4 = r60.needDrawBluredPreview();
        if (r4 != 0) goto L_0x2f7b;
    L_0x2f72:
        r4 = r1.currentCaption;
        if (r4 == 0) goto L_0x2f7b;
    L_0x2f76:
        if (r5 >= r2) goto L_0x2f7b;
    L_0x2f78:
        r12 = r2;
        r2 = 1;
        goto L_0x2f83;
    L_0x2f7b:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r2 = r5 - r2;
        r12 = r2;
        r2 = 0;
    L_0x2f83:
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r4 = r4 + r5;
        r1.backgroundWidth = r4;
        r4 = r1.mediaBackground;
        if (r4 != 0) goto L_0x2f99;
    L_0x2f8e:
        r4 = r1.backgroundWidth;
        r6 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r4 = r4 + r6;
        r1.backgroundWidth = r4;
    L_0x2f99:
        r15 = r3;
        r3 = r2;
        r2 = r5;
    L_0x2f9c:
        r4 = r1.currentCaption;
        if (r4 == 0) goto L_0x30a7;
    L_0x2fa0:
        r6 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x309e }
        r7 = 24;
        if (r6 < r7) goto L_0x2fc7;
    L_0x2fa6:
        r6 = r4.length();	 Catch:{ Exception -> 0x309e }
        r7 = org.telegram.p004ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x309e }
        r8 = 0;
        r4 = android.text.StaticLayout.Builder.obtain(r4, r8, r6, r7, r12);	 Catch:{ Exception -> 0x309e }
        r6 = 1;
        r4 = r4.setBreakStrategy(r6);	 Catch:{ Exception -> 0x309e }
        r4 = r4.setHyphenationFrequency(r8);	 Catch:{ Exception -> 0x309e }
        r6 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x309e }
        r4 = r4.setAlignment(r6);	 Catch:{ Exception -> 0x309e }
        r4 = r4.build();	 Catch:{ Exception -> 0x309e }
        r1.captionLayout = r4;	 Catch:{ Exception -> 0x309e }
        goto L_0x2fde;
    L_0x2fc7:
        r6 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x309e }
        r29 = org.telegram.p004ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x309e }
        r31 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x309e }
        r32 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r33 = 0;
        r34 = 0;
        r27 = r6;
        r28 = r4;
        r30 = r12;
        r27.<init>(r28, r29, r30, r31, r32, r33, r34);	 Catch:{ Exception -> 0x309e }
        r1.captionLayout = r6;	 Catch:{ Exception -> 0x309e }
    L_0x2fde:
        r4 = r1.captionLayout;	 Catch:{ Exception -> 0x309e }
        r4 = r4.getLineCount();	 Catch:{ Exception -> 0x309e }
        if (r4 <= 0) goto L_0x3098;
    L_0x2fe6:
        if (r3 == 0) goto L_0x301b;
    L_0x2fe8:
        r6 = 0;
        r1.captionWidth = r6;	 Catch:{ Exception -> 0x309e }
        r6 = 0;
    L_0x2fec:
        if (r6 >= r4) goto L_0x3014;
    L_0x2fee:
        r7 = r1.captionWidth;	 Catch:{ Exception -> 0x309e }
        r7 = (double) r7;	 Catch:{ Exception -> 0x309e }
        r9 = r1.captionLayout;	 Catch:{ Exception -> 0x309e }
        r9 = r9.getLineWidth(r6);	 Catch:{ Exception -> 0x309e }
        r9 = (double) r9;	 Catch:{ Exception -> 0x309e }
        r9 = java.lang.Math.ceil(r9);	 Catch:{ Exception -> 0x309e }
        r7 = java.lang.Math.max(r7, r9);	 Catch:{ Exception -> 0x309e }
        r7 = (int) r7;	 Catch:{ Exception -> 0x309e }
        r1.captionWidth = r7;	 Catch:{ Exception -> 0x309e }
        r7 = r1.captionLayout;	 Catch:{ Exception -> 0x309e }
        r7 = r7.getLineLeft(r6);	 Catch:{ Exception -> 0x309e }
        r8 = 0;
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
        if (r7 == 0) goto L_0x3011;
    L_0x300e:
        r1.captionWidth = r12;	 Catch:{ Exception -> 0x309e }
        goto L_0x3014;
    L_0x3011:
        r6 = r6 + 1;
        goto L_0x2fec;
    L_0x3014:
        r4 = r1.captionWidth;	 Catch:{ Exception -> 0x309e }
        if (r4 <= r12) goto L_0x301d;
    L_0x3018:
        r1.captionWidth = r12;	 Catch:{ Exception -> 0x309e }
        goto L_0x301d;
    L_0x301b:
        r1.captionWidth = r12;	 Catch:{ Exception -> 0x309e }
    L_0x301d:
        r4 = r1.captionLayout;	 Catch:{ Exception -> 0x309e }
        r4 = r4.getHeight();	 Catch:{ Exception -> 0x309e }
        r1.captionHeight = r4;	 Catch:{ Exception -> 0x309e }
        r4 = r1.captionHeight;	 Catch:{ Exception -> 0x309e }
        r6 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);	 Catch:{ Exception -> 0x309e }
        r4 = r4 + r6;
        r1.addedCaptionHeight = r4;	 Catch:{ Exception -> 0x309e }
        r4 = r1.currentPosition;	 Catch:{ Exception -> 0x309e }
        if (r4 == 0) goto L_0x3042;
    L_0x3034:
        r4 = r1.currentPosition;	 Catch:{ Exception -> 0x309e }
        r4 = r4.flags;	 Catch:{ Exception -> 0x309e }
        r6 = 8;
        r4 = r4 & r6;
        if (r4 == 0) goto L_0x303e;
    L_0x303d:
        goto L_0x3042;
    L_0x303e:
        r4 = 0;
        r1.captionLayout = r4;	 Catch:{ Exception -> 0x309e }
        goto L_0x3098;
    L_0x3042:
        r4 = r1.addedCaptionHeight;	 Catch:{ Exception -> 0x309e }
        r6 = 0;
        r12 = r6 + r4;
        r4 = r1.captionWidth;	 Catch:{ Exception -> 0x3096 }
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r19);	 Catch:{ Exception -> 0x3096 }
        r6 = r5 - r6;
        r4 = java.lang.Math.max(r4, r6);	 Catch:{ Exception -> 0x3096 }
        r6 = r1.captionLayout;	 Catch:{ Exception -> 0x3096 }
        r7 = r1.captionLayout;	 Catch:{ Exception -> 0x3096 }
        r7 = r7.getLineCount();	 Catch:{ Exception -> 0x3096 }
        r8 = 1;
        r7 = r7 - r8;
        r6 = r6.getLineWidth(r7);	 Catch:{ Exception -> 0x3096 }
        r7 = r1.captionLayout;	 Catch:{ Exception -> 0x3096 }
        r9 = r1.captionLayout;	 Catch:{ Exception -> 0x3096 }
        r9 = r9.getLineCount();	 Catch:{ Exception -> 0x3096 }
        r9 = r9 - r8;
        r7 = r7.getLineLeft(r9);	 Catch:{ Exception -> 0x3096 }
        r6 = r6 + r7;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r20);	 Catch:{ Exception -> 0x3096 }
        r4 = r4 + r7;
        r4 = (float) r4;	 Catch:{ Exception -> 0x3096 }
        r4 = r4 - r6;
        r0 = (float) r0;	 Catch:{ Exception -> 0x3096 }
        r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r0 >= 0) goto L_0x308f;
    L_0x307b:
        r0 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);	 Catch:{ Exception -> 0x3096 }
        r12 = r12 + r0;
        r0 = r1.addedCaptionHeight;	 Catch:{ Exception -> 0x3096 }
        r4 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);	 Catch:{ Exception -> 0x3096 }
        r0 = r0 + r4;
        r1.addedCaptionHeight = r0;	 Catch:{ Exception -> 0x3096 }
        r0 = 1;
        goto L_0x3090;
    L_0x308f:
        r0 = 0;
    L_0x3090:
        r58 = r12;
        r12 = r0;
        r0 = r58;
        goto L_0x309a;
    L_0x3096:
        r0 = move-exception;
        goto L_0x30a0;
    L_0x3098:
        r0 = 0;
        r12 = 0;
    L_0x309a:
        r21 = r0;
        r0 = r12;
        goto L_0x30aa;
    L_0x309e:
        r0 = move-exception;
        r12 = 0;
    L_0x30a0:
        org.telegram.messenger.FileLog.m30e(r0);
        r21 = r12;
        r0 = 0;
        goto L_0x30aa;
    L_0x30a7:
        r0 = 0;
        r21 = 0;
    L_0x30aa:
        if (r3 == 0) goto L_0x30d5;
    L_0x30ac:
        r3 = r1.captionWidth;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r3 = r3 + r4;
        if (r5 >= r3) goto L_0x30d5;
    L_0x30b5:
        r3 = r1.captionWidth;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r3 = r3 + r4;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r4 = r4 + r3;
        r1.backgroundWidth = r4;
        r4 = r1.mediaBackground;
        if (r4 != 0) goto L_0x30d2;
    L_0x30c7:
        r4 = r1.backgroundWidth;
        r5 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r4 = r4 + r5;
        r1.backgroundWidth = r4;
    L_0x30d2:
        r22 = r3;
        goto L_0x30d7;
    L_0x30d5:
        r22 = r5;
    L_0x30d7:
        r3 = java.util.Locale.US;
        r4 = 2;
        r5 = new java.lang.Object[r4];
        r2 = (float) r2;
        r4 = org.telegram.messenger.AndroidUtilities.density;
        r2 = r2 / r4;
        r2 = (int) r2;
        r2 = java.lang.Integer.valueOf(r2);
        r13 = 0;
        r5[r13] = r2;
        r2 = (float) r15;
        r4 = org.telegram.messenger.AndroidUtilities.density;
        r2 = r2 / r4;
        r2 = (int) r2;
        r2 = java.lang.Integer.valueOf(r2);
        r4 = 1;
        r5[r4] = r2;
        r2 = "%d_%d";
        r2 = java.lang.String.format(r3, r2, r5);
        r1.currentPhotoFilterThumb = r2;
        r1.currentPhotoFilter = r2;
        r2 = r14.photoThumbs;
        if (r2 == 0) goto L_0x3108;
    L_0x3102:
        r2 = r2.size();
        if (r2 > r4) goto L_0x3114;
    L_0x3108:
        r2 = r14.type;
        r3 = 3;
        if (r2 == r3) goto L_0x3114;
    L_0x310d:
        r3 = 8;
        if (r2 == r3) goto L_0x3114;
    L_0x3111:
        r3 = 5;
        if (r2 != r3) goto L_0x315a;
    L_0x3114:
        r2 = r60.needDrawBluredPreview();
        if (r2 == 0) goto L_0x3145;
    L_0x311a:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = r1.currentPhotoFilter;
        r2.append(r3);
        r3 = "_b2";
        r2.append(r3);
        r2 = r2.toString();
        r1.currentPhotoFilter = r2;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = r1.currentPhotoFilterThumb;
        r2.append(r3);
        r3 = "_b2";
        r2.append(r3);
        r2 = r2.toString();
        r1.currentPhotoFilterThumb = r2;
        goto L_0x315a;
    L_0x3145:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = r1.currentPhotoFilterThumb;
        r2.append(r3);
        r3 = "_b";
        r2.append(r3);
        r2 = r2.toString();
        r1.currentPhotoFilterThumb = r2;
    L_0x315a:
        r2 = r14.type;
        r3 = 3;
        if (r2 == r3) goto L_0x3169;
    L_0x315f:
        r3 = 8;
        if (r2 == r3) goto L_0x3169;
    L_0x3163:
        r3 = 5;
        if (r2 != r3) goto L_0x3167;
    L_0x3166:
        goto L_0x3169;
    L_0x3167:
        r2 = 0;
        goto L_0x316a;
    L_0x3169:
        r2 = 1;
    L_0x316a:
        r3 = r1.currentPhotoObject;
        if (r3 == 0) goto L_0x3178;
    L_0x316e:
        if (r2 != 0) goto L_0x3178;
    L_0x3170:
        r4 = r3.size;
        if (r4 != 0) goto L_0x3178;
    L_0x3174:
        r12 = -1;
        r3.size = r12;
        goto L_0x3179;
    L_0x3178:
        r12 = -1;
    L_0x3179:
        r3 = r1.currentPhotoObjectThumb;
        if (r3 == 0) goto L_0x3185;
    L_0x317d:
        if (r2 != 0) goto L_0x3185;
    L_0x317f:
        r4 = r3.size;
        if (r4 != 0) goto L_0x3185;
    L_0x3183:
        r3.size = r12;
    L_0x3185:
        r3 = org.telegram.messenger.SharedConfig.autoplayVideo;
        if (r3 == 0) goto L_0x31c8;
    L_0x3189:
        r3 = r14.type;
        r11 = 3;
        if (r3 != r11) goto L_0x31c6;
    L_0x318e:
        r3 = r60.needDrawBluredPreview();
        if (r3 != 0) goto L_0x31c6;
    L_0x3194:
        r3 = r1.currentMessageObject;
        r3 = r3.mediaExists;
        if (r3 != 0) goto L_0x31ae;
    L_0x319a:
        r3 = r60.canStreamVideo();
        if (r3 == 0) goto L_0x31c6;
    L_0x31a0:
        r3 = r1.currentAccount;
        r3 = org.telegram.messenger.DownloadController.getInstance(r3);
        r4 = r1.currentMessageObject;
        r3 = r3.canDownloadMedia(r4);
        if (r3 == 0) goto L_0x31c6;
    L_0x31ae:
        r3 = r1.currentPosition;
        if (r3 == 0) goto L_0x31c2;
    L_0x31b2:
        r3 = r3.flags;
        r4 = r3 & 1;
        if (r4 == 0) goto L_0x31be;
    L_0x31b8:
        r4 = 2;
        r3 = r3 & r4;
        if (r3 == 0) goto L_0x31be;
    L_0x31bc:
        r3 = 1;
        goto L_0x31bf;
    L_0x31be:
        r3 = 0;
    L_0x31bf:
        r1.autoPlayingMedia = r3;
        goto L_0x31c6;
    L_0x31c2:
        r3 = 1;
        r1.autoPlayingMedia = r3;
        goto L_0x31ca;
    L_0x31c6:
        r3 = 1;
        goto L_0x31ca;
    L_0x31c8:
        r3 = 1;
        r11 = 3;
    L_0x31ca:
        r4 = r1.autoPlayingMedia;
        if (r4 == 0) goto L_0x321d;
    L_0x31ce:
        r2 = r1.photoImage;
        r2.setAllowStartAnimation(r3);
        r2 = r1.photoImage;
        r2.startAnimation();
        r2 = r14.messageOwner;
        r2 = r2.media;
        r2 = r2.document;
        r3 = r1.photoImage;
        r4 = org.telegram.messenger.ImageLocation.getForDocument(r2);
        r5 = r1.currentPhotoObject;
        r6 = r1.photoParentObject;
        r5 = org.telegram.messenger.ImageLocation.getForObject(r5, r6);
        r6 = r1.currentPhotoFilter;
        r7 = r1.currentPhotoObjectThumb;
        r7 = org.telegram.messenger.ImageLocation.getForDocument(r7, r2);
        r8 = r1.currentPhotoFilterThumb;
        r9 = 0;
        r2 = r14.messageOwner;
        r2 = r2.media;
        r2 = r2.document;
        r10 = r2.size;
        r24 = 0;
        r27 = 0;
        r28 = "g";
        r2 = r3;
        r3 = r4;
        r4 = r28;
        r28 = 3;
        r29 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r11 = r24;
        r24 = -1;
        r12 = r60;
        r28 = r15;
        r15 = 0;
        r13 = r27;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);
        goto L_0x3426;
    L_0x321d:
        r28 = r15;
        r15 = 0;
        r29 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = r14.type;
        r4 = 1;
        if (r3 != r4) goto L_0x32d7;
    L_0x3227:
        r3 = r14.useCustomPhoto;
        if (r3 == 0) goto L_0x323d;
    L_0x322b:
        r2 = r1.photoImage;
        r3 = r59.getResources();
        r4 = 2131165874; // 0x7f0702b2 float:1.7945977E38 double:1.052935844E-314;
        r3 = r3.getDrawable(r4);
        r2.setImageBitmap(r3);
        goto L_0x3426;
    L_0x323d:
        r3 = r1.currentPhotoObject;
        if (r3 == 0) goto L_0x32cf;
    L_0x3241:
        r3 = org.telegram.messenger.FileLoader.getAttachFileName(r3);
        r4 = r14.mediaExists;
        if (r4 == 0) goto L_0x3254;
    L_0x3249:
        r4 = r1.currentAccount;
        r4 = org.telegram.messenger.DownloadController.getInstance(r4);
        r4.removeLoadingFileObserver(r1);
        r4 = 1;
        goto L_0x3255;
    L_0x3254:
        r4 = 0;
    L_0x3255:
        if (r4 != 0) goto L_0x329f;
    L_0x3257:
        r4 = r1.currentAccount;
        r4 = org.telegram.messenger.DownloadController.getInstance(r4);
        r5 = r1.currentMessageObject;
        r4 = r4.canDownloadMedia(r5);
        if (r4 != 0) goto L_0x329f;
    L_0x3265:
        r4 = r1.currentAccount;
        r4 = org.telegram.messenger.FileLoader.getInstance(r4);
        r3 = r4.isLoadingFile(r3);
        if (r3 == 0) goto L_0x3272;
    L_0x3271:
        goto L_0x329f;
    L_0x3272:
        r3 = 1;
        r1.photoNotSet = r3;
        r2 = r1.currentPhotoObjectThumb;
        if (r2 == 0) goto L_0x3297;
    L_0x3279:
        r3 = r1.photoImage;
        r4 = 0;
        r5 = 0;
        r6 = r1.photoParentObject;
        r6 = org.telegram.messenger.ImageLocation.getForObject(r2, r6);
        r7 = r1.currentPhotoFilterThumb;
        r8 = 0;
        r9 = 0;
        r10 = r1.currentMessageObject;
        r2 = r10.shouldEncryptPhotoOrVideo();
        if (r2 == 0) goto L_0x3291;
    L_0x328f:
        r11 = 2;
        goto L_0x3292;
    L_0x3291:
        r11 = 0;
    L_0x3292:
        r3.setImage(r4, r5, r6, r7, r8, r9, r10, r11);
        goto L_0x3426;
    L_0x3297:
        r2 = r1.photoImage;
        r3 = 0;
        r2.setImageBitmap(r3);
        goto L_0x3426;
    L_0x329f:
        r4 = r1.photoImage;
        r3 = r1.currentPhotoObject;
        r5 = r1.photoParentObject;
        r5 = org.telegram.messenger.ImageLocation.getForObject(r3, r5);
        r6 = r1.currentPhotoFilter;
        r3 = r1.currentPhotoObjectThumb;
        r7 = r1.photoParentObject;
        r7 = org.telegram.messenger.ImageLocation.getForObject(r3, r7);
        r8 = r1.currentPhotoFilterThumb;
        if (r2 == 0) goto L_0x32b9;
    L_0x32b7:
        r9 = 0;
        goto L_0x32be;
    L_0x32b9:
        r2 = r1.currentPhotoObject;
        r12 = r2.size;
        r9 = r12;
    L_0x32be:
        r10 = 0;
        r11 = r1.currentMessageObject;
        r2 = r11.shouldEncryptPhotoOrVideo();
        if (r2 == 0) goto L_0x32c9;
    L_0x32c7:
        r12 = 2;
        goto L_0x32ca;
    L_0x32c9:
        r12 = 0;
    L_0x32ca:
        r4.setImage(r5, r6, r7, r8, r9, r10, r11, r12);
        goto L_0x3426;
    L_0x32cf:
        r2 = r1.photoImage;
        r3 = 0;
        r2.setImageBitmap(r3);
        goto L_0x3426;
    L_0x32d7:
        r2 = 8;
        if (r3 == r2) goto L_0x3309;
    L_0x32db:
        r2 = 5;
        if (r3 != r2) goto L_0x32df;
    L_0x32de:
        goto L_0x3309;
    L_0x32df:
        r2 = r1.photoImage;
        r3 = r1.currentPhotoObject;
        r4 = r1.photoParentObject;
        r3 = org.telegram.messenger.ImageLocation.getForObject(r3, r4);
        r4 = r1.currentPhotoFilter;
        r5 = r1.currentPhotoObjectThumb;
        r6 = r1.photoParentObject;
        r5 = org.telegram.messenger.ImageLocation.getForObject(r5, r6);
        r6 = r1.currentPhotoFilterThumb;
        r7 = 0;
        r8 = 0;
        r9 = r1.currentMessageObject;
        r9 = r9.shouldEncryptPhotoOrVideo();
        if (r9 == 0) goto L_0x3301;
    L_0x32ff:
        r10 = 2;
        goto L_0x3302;
    L_0x3301:
        r10 = 0;
    L_0x3302:
        r9 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x3426;
    L_0x3309:
        r2 = r14.messageOwner;
        r2 = r2.media;
        r2 = r2.document;
        r2 = org.telegram.messenger.FileLoader.getAttachFileName(r2);
        r3 = r14.attachPathExists;
        if (r3 == 0) goto L_0x3322;
    L_0x3317:
        r3 = r1.currentAccount;
        r3 = org.telegram.messenger.DownloadController.getInstance(r3);
        r3.removeLoadingFileObserver(r1);
        r3 = 1;
        goto L_0x3329;
    L_0x3322:
        r3 = r14.mediaExists;
        if (r3 == 0) goto L_0x3328;
    L_0x3326:
        r3 = 2;
        goto L_0x3329;
    L_0x3328:
        r3 = 0;
    L_0x3329:
        r4 = r14.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r4 = org.telegram.messenger.MessageObject.isGifDocument(r4);
        if (r4 != 0) goto L_0x333d;
    L_0x3335:
        r4 = r14.type;
        r5 = 5;
        if (r4 != r5) goto L_0x333b;
    L_0x333a:
        goto L_0x333d;
    L_0x333b:
        r12 = 0;
        goto L_0x3349;
    L_0x333d:
        r4 = r1.currentAccount;
        r4 = org.telegram.messenger.DownloadController.getInstance(r4);
        r5 = r1.currentMessageObject;
        r12 = r4.canDownloadMedia(r5);
    L_0x3349:
        r4 = r60.isSending();
        if (r4 != 0) goto L_0x3408;
    L_0x334f:
        r4 = r60.isEditing();
        if (r4 != 0) goto L_0x3408;
    L_0x3355:
        if (r3 != 0) goto L_0x3365;
    L_0x3357:
        r4 = r1.currentAccount;
        r4 = org.telegram.messenger.FileLoader.getInstance(r4);
        r2 = r4.isLoadingFile(r2);
        if (r2 != 0) goto L_0x3365;
    L_0x3363:
        if (r12 == 0) goto L_0x3408;
    L_0x3365:
        r2 = 1;
        if (r3 == r2) goto L_0x33ae;
    L_0x3368:
        r4 = r60.needDrawBluredPreview();
        if (r4 != 0) goto L_0x33ae;
    L_0x336e:
        if (r3 != 0) goto L_0x3378;
    L_0x3370:
        r4 = r60.canStreamVideo();
        if (r4 == 0) goto L_0x33ae;
    L_0x3376:
        if (r12 == 0) goto L_0x33ae;
    L_0x3378:
        r1.autoPlayingMedia = r2;
        r2 = r1.photoImage;
        r3 = r14.messageOwner;
        r3 = r3.media;
        r3 = r3.document;
        r3 = org.telegram.messenger.ImageLocation.getForDocument(r3);
        r4 = r1.currentPhotoObject;
        r5 = r1.photoParentObject;
        r5 = org.telegram.messenger.ImageLocation.getForObject(r4, r5);
        r6 = r1.currentPhotoFilter;
        r4 = r1.currentPhotoObjectThumb;
        r7 = r1.photoParentObject;
        r7 = org.telegram.messenger.ImageLocation.getForObject(r4, r7);
        r8 = r1.currentPhotoFilterThumb;
        r9 = 0;
        r4 = r14.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r10 = r4.size;
        r11 = 0;
        r13 = 0;
        r4 = "g";
        r12 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);
        goto L_0x3426;
    L_0x33ae:
        if (r3 != r2) goto L_0x33d6;
    L_0x33b0:
        r2 = r1.photoImage;
        r3 = r60.isSendError();
        if (r3 == 0) goto L_0x33ba;
    L_0x33b8:
        r3 = 0;
        goto L_0x33be;
    L_0x33ba:
        r3 = r14.messageOwner;
        r3 = r3.attachPath;
    L_0x33be:
        r3 = org.telegram.messenger.ImageLocation.getForPath(r3);
        r4 = 0;
        r5 = r1.currentPhotoObjectThumb;
        r6 = r1.photoParentObject;
        r5 = org.telegram.messenger.ImageLocation.getForObject(r5, r6);
        r6 = r1.currentPhotoFilterThumb;
        r7 = 0;
        r8 = 0;
        r10 = 0;
        r9 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x3426;
    L_0x33d6:
        r2 = r1.photoImage;
        r3 = r14.messageOwner;
        r3 = r3.media;
        r3 = r3.document;
        r3 = org.telegram.messenger.ImageLocation.getForDocument(r3);
        r4 = 0;
        r5 = r1.currentPhotoObject;
        r6 = r1.photoParentObject;
        r5 = org.telegram.messenger.ImageLocation.getForObject(r5, r6);
        r6 = r1.currentPhotoFilter;
        r7 = r1.currentPhotoObjectThumb;
        r8 = r1.photoParentObject;
        r7 = org.telegram.messenger.ImageLocation.getForObject(r7, r8);
        r8 = r1.currentPhotoFilterThumb;
        r9 = 0;
        r10 = r14.messageOwner;
        r10 = r10.media;
        r10 = r10.document;
        r10 = r10.size;
        r11 = 0;
        r13 = 0;
        r12 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);
        goto L_0x3426;
    L_0x3408:
        r2 = r1.photoImage;
        r3 = r1.currentPhotoObject;
        r4 = r1.photoParentObject;
        r3 = org.telegram.messenger.ImageLocation.getForObject(r3, r4);
        r4 = r1.currentPhotoFilter;
        r5 = r1.currentPhotoObjectThumb;
        r6 = r1.photoParentObject;
        r5 = org.telegram.messenger.ImageLocation.getForObject(r5, r6);
        r6 = r1.currentPhotoFilterThumb;
        r7 = 0;
        r8 = 0;
        r10 = 0;
        r9 = r60;
        r2.setImage(r3, r4, r5, r6, r7, r8, r9, r10);
    L_0x3426:
        r2 = r0;
        r57 = r21;
        r0 = r22;
        r12 = r28;
    L_0x342d:
        r59.setMessageObjectInternal(r60);
        r3 = r1.drawForwardedName;
        if (r3 == 0) goto L_0x3453;
    L_0x3434:
        r3 = r60.needDrawForwarded();
        if (r3 == 0) goto L_0x3453;
    L_0x343a:
        r3 = r1.currentPosition;
        if (r3 == 0) goto L_0x3442;
    L_0x343e:
        r3 = r3.minY;
        if (r3 != 0) goto L_0x3453;
    L_0x3442:
        r3 = r14.type;
        r4 = 5;
        if (r3 == r4) goto L_0x3468;
    L_0x3447:
        r3 = r1.namesOffset;
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 + r4;
        r1.namesOffset = r3;
        goto L_0x3468;
    L_0x3453:
        r3 = r1.drawNameLayout;
        if (r3 == 0) goto L_0x3468;
    L_0x3457:
        r3 = r14.messageOwner;
        r3 = r3.reply_to_msg_id;
        if (r3 != 0) goto L_0x3468;
    L_0x345d:
        r3 = r1.namesOffset;
        r4 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 + r4;
        r1.namesOffset = r3;
    L_0x3468:
        r3 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = r3 + r12;
        r4 = r1.namesOffset;
        r3 = r3 + r4;
        r3 = r3 + r57;
        r1.totalHeight = r3;
        r3 = r1.currentPosition;
        if (r3 == 0) goto L_0x348c;
    L_0x347a:
        r3 = r3.flags;
        r4 = 8;
        r3 = r3 & r4;
        if (r3 != 0) goto L_0x348c;
    L_0x3481:
        r3 = r1.totalHeight;
        r4 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 - r4;
        r1.totalHeight = r3;
    L_0x348c:
        r3 = r1.currentPosition;
        if (r3 == 0) goto L_0x34bd;
    L_0x3490:
        r3 = r1.getAdditionalWidthForPosition(r3);
        r0 = r0 + r3;
        r3 = r1.currentPosition;
        r3 = r3.flags;
        r3 = r3 & 4;
        if (r3 != 0) goto L_0x34ad;
    L_0x349d:
        r3 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r12 = r12 + r3;
        r3 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = 0 - r3;
        goto L_0x34ae;
    L_0x34ad:
        r3 = 0;
    L_0x34ae:
        r4 = r1.currentPosition;
        r4 = r4.flags;
        r5 = 8;
        r4 = r4 & r5;
        if (r4 != 0) goto L_0x34be;
    L_0x34b7:
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r29);
        r12 = r12 + r4;
        goto L_0x34be;
    L_0x34bd:
        r3 = 0;
    L_0x34be:
        r4 = r1.drawPinnedTop;
        if (r4 == 0) goto L_0x34cb;
    L_0x34c2:
        r4 = r1.namesOffset;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r29);
        r4 = r4 - r5;
        r1.namesOffset = r4;
    L_0x34cb:
        r4 = r1.currentPosition;
        if (r4 == 0) goto L_0x34f5;
    L_0x34cf:
        r4 = r1.namesOffset;
        if (r4 <= 0) goto L_0x34e3;
    L_0x34d3:
        r4 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r5 = r1.totalHeight;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r5 = r5 - r6;
        r1.totalHeight = r5;
        goto L_0x351a;
    L_0x34e3:
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r5 = r1.totalHeight;
        r6 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 - r6;
        r1.totalHeight = r5;
        goto L_0x351a;
    L_0x34f5:
        r4 = r1.namesOffset;
        if (r4 <= 0) goto L_0x3509;
    L_0x34f9:
        r4 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r5 = r1.totalHeight;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r5 = r5 - r6;
        r1.totalHeight = r5;
        goto L_0x351a;
    L_0x3509:
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r5 = r1.totalHeight;
        r6 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 - r6;
        r1.totalHeight = r5;
    L_0x351a:
        r5 = r1.photoImage;
        r6 = r1.namesOffset;
        r4 = r4 + r6;
        r4 = r4 + r3;
        r5.setImageCoords(r15, r4, r0, r12);
        r59.invalidate();
        r9 = r2;
    L_0x3527:
        r0 = r1.currentPosition;
        if (r0 != 0) goto L_0x3621;
    L_0x352b:
        r0 = r60.isAnyKindOfSticker();
        if (r0 != 0) goto L_0x3621;
    L_0x3531:
        r0 = r1.addedCaptionHeight;
        if (r0 != 0) goto L_0x3621;
    L_0x3535:
        r0 = r1.captionLayout;
        if (r0 != 0) goto L_0x3595;
    L_0x3539:
        r0 = r14.caption;
        if (r0 == 0) goto L_0x3595;
    L_0x353d:
        r1.currentCaption = r0;	 Catch:{ Exception -> 0x3591 }
        r0 = r1.backgroundWidth;	 Catch:{ Exception -> 0x3591 }
        r2 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);	 Catch:{ Exception -> 0x3591 }
        r0 = r0 - r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r19);	 Catch:{ Exception -> 0x3591 }
        r0 = r0 - r2;
        r2 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x3591 }
        r3 = 24;
        if (r2 < r3) goto L_0x3577;
    L_0x3553:
        r2 = r14.caption;	 Catch:{ Exception -> 0x3591 }
        r3 = r14.caption;	 Catch:{ Exception -> 0x3591 }
        r3 = r3.length();	 Catch:{ Exception -> 0x3591 }
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x3591 }
        r0 = android.text.StaticLayout.Builder.obtain(r2, r15, r3, r4, r0);	 Catch:{ Exception -> 0x3591 }
        r2 = 1;
        r0 = r0.setBreakStrategy(r2);	 Catch:{ Exception -> 0x3591 }
        r0 = r0.setHyphenationFrequency(r15);	 Catch:{ Exception -> 0x3591 }
        r2 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x3591 }
        r0 = r0.setAlignment(r2);	 Catch:{ Exception -> 0x3591 }
        r0 = r0.build();	 Catch:{ Exception -> 0x3591 }
        r1.captionLayout = r0;	 Catch:{ Exception -> 0x3591 }
        goto L_0x3595;
    L_0x3577:
        r2 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x3591 }
        r3 = r14.caption;	 Catch:{ Exception -> 0x3591 }
        r32 = org.telegram.p004ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x3591 }
        r34 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x3591 }
        r35 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r36 = 0;
        r37 = 0;
        r30 = r2;
        r31 = r3;
        r33 = r0;
        r30.<init>(r31, r32, r33, r34, r35, r36, r37);	 Catch:{ Exception -> 0x3591 }
        r1.captionLayout = r2;	 Catch:{ Exception -> 0x3591 }
        goto L_0x3595;
    L_0x3591:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x3595:
        r0 = r1.captionLayout;
        if (r0 == 0) goto L_0x3621;
    L_0x3599:
        r0 = r1.backgroundWidth;	 Catch:{ Exception -> 0x361d }
        r2 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);	 Catch:{ Exception -> 0x361d }
        r0 = r0 - r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r19);	 Catch:{ Exception -> 0x361d }
        r2 = r0 - r2;
        r3 = r1.captionLayout;	 Catch:{ Exception -> 0x361d }
        if (r3 == 0) goto L_0x3621;
    L_0x35ac:
        r3 = r1.captionLayout;	 Catch:{ Exception -> 0x361d }
        r3 = r3.getLineCount();	 Catch:{ Exception -> 0x361d }
        if (r3 <= 0) goto L_0x3621;
    L_0x35b4:
        r1.captionWidth = r2;	 Catch:{ Exception -> 0x361d }
        r2 = r1.timeWidth;	 Catch:{ Exception -> 0x361d }
        r3 = r60.isOutOwner();	 Catch:{ Exception -> 0x361d }
        if (r3 == 0) goto L_0x35c5;
    L_0x35be:
        r3 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r3);	 Catch:{ Exception -> 0x361d }
        goto L_0x35c6;
    L_0x35c5:
        r12 = 0;
    L_0x35c6:
        r2 = r2 + r12;
        r3 = r1.captionLayout;	 Catch:{ Exception -> 0x361d }
        r3 = r3.getHeight();	 Catch:{ Exception -> 0x361d }
        r1.captionHeight = r3;	 Catch:{ Exception -> 0x361d }
        r3 = r1.totalHeight;	 Catch:{ Exception -> 0x361d }
        r4 = r1.captionHeight;	 Catch:{ Exception -> 0x361d }
        r5 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);	 Catch:{ Exception -> 0x361d }
        r4 = r4 + r5;
        r3 = r3 + r4;
        r1.totalHeight = r3;	 Catch:{ Exception -> 0x361d }
        r3 = r1.captionLayout;	 Catch:{ Exception -> 0x361d }
        r4 = r1.captionLayout;	 Catch:{ Exception -> 0x361d }
        r4 = r4.getLineCount();	 Catch:{ Exception -> 0x361d }
        r5 = 1;
        r4 = r4 - r5;
        r3 = r3.getLineWidth(r4);	 Catch:{ Exception -> 0x361d }
        r4 = r1.captionLayout;	 Catch:{ Exception -> 0x361d }
        r6 = r1.captionLayout;	 Catch:{ Exception -> 0x361d }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x361d }
        r6 = r6 - r5;
        r4 = r4.getLineLeft(r6);	 Catch:{ Exception -> 0x361d }
        r3 = r3 + r4;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r18);	 Catch:{ Exception -> 0x361d }
        r0 = r0 - r4;
        r0 = (float) r0;	 Catch:{ Exception -> 0x361d }
        r0 = r0 - r3;
        r2 = (float) r2;	 Catch:{ Exception -> 0x361d }
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 >= 0) goto L_0x3621;
    L_0x3605:
        r0 = r1.totalHeight;	 Catch:{ Exception -> 0x361d }
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);	 Catch:{ Exception -> 0x361d }
        r0 = r0 + r2;
        r1.totalHeight = r0;	 Catch:{ Exception -> 0x361d }
        r0 = r1.captionHeight;	 Catch:{ Exception -> 0x361d }
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);	 Catch:{ Exception -> 0x361d }
        r0 = r0 + r2;
        r1.captionHeight = r0;	 Catch:{ Exception -> 0x361d }
        r9 = 2;
        goto L_0x3621;
    L_0x361d:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x3621:
        r0 = r1.captionLayout;
        if (r0 != 0) goto L_0x363c;
    L_0x3625:
        r0 = r1.widthBeforeNewTimeLine;
        r2 = -1;
        if (r0 == r2) goto L_0x363c;
    L_0x362a:
        r2 = r1.availableTimeWidth;
        r2 = r2 - r0;
        r0 = r1.timeWidth;
        if (r2 >= r0) goto L_0x363c;
    L_0x3631:
        r0 = r1.totalHeight;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 + r2;
        r1.totalHeight = r0;
    L_0x363c:
        r0 = r1.currentMessageObject;
        r2 = r0.eventId;
        r4 = 0;
        r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r6 == 0) goto L_0x3768;
    L_0x3646:
        r0 = r0.isMediaEmpty();
        if (r0 != 0) goto L_0x3768;
    L_0x364c:
        r0 = r1.currentMessageObject;
        r0 = r0.messageOwner;
        r0 = r0.media;
        r0 = r0.webpage;
        if (r0 == 0) goto L_0x3768;
    L_0x3656:
        r0 = r1.backgroundWidth;
        r2 = 1109655552; // 0x42240000 float:41.0 double:5.48242687E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = r0 - r2;
        r3 = 1;
        r1.hasOldCaptionPreview = r3;
        r1.linkPreviewHeight = r15;
        r0 = r1.currentMessageObject;
        r0 = r0.messageOwner;
        r0 = r0.media;
        r3 = r0.webpage;
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x36c5 }
        r4 = r3.site_name;	 Catch:{ Exception -> 0x36c5 }
        r0 = r0.measureText(r4);	 Catch:{ Exception -> 0x36c5 }
        r0 = r0 + r29;
        r4 = (double) r0;	 Catch:{ Exception -> 0x36c5 }
        r4 = java.lang.Math.ceil(r4);	 Catch:{ Exception -> 0x36c5 }
        r0 = (int) r4;	 Catch:{ Exception -> 0x36c5 }
        r1.siteNameWidth = r0;	 Catch:{ Exception -> 0x36c5 }
        r4 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x36c5 }
        r5 = r3.site_name;	 Catch:{ Exception -> 0x36c5 }
        r32 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x36c5 }
        r33 = java.lang.Math.min(r0, r2);	 Catch:{ Exception -> 0x36c5 }
        r34 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x36c5 }
        r35 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r36 = 0;
        r37 = 0;
        r30 = r4;
        r31 = r5;
        r30.<init>(r31, r32, r33, r34, r35, r36, r37);	 Catch:{ Exception -> 0x36c5 }
        r1.siteNameLayout = r4;	 Catch:{ Exception -> 0x36c5 }
        r0 = r1.siteNameLayout;	 Catch:{ Exception -> 0x36c5 }
        r0 = r0.getLineLeft(r15);	 Catch:{ Exception -> 0x36c5 }
        r5 = 0;
        r0 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1));
        if (r0 == 0) goto L_0x36a7;
    L_0x36a5:
        r0 = 1;
        goto L_0x36a8;
    L_0x36a7:
        r0 = 0;
    L_0x36a8:
        r1.siteNameRtl = r0;	 Catch:{ Exception -> 0x36c3 }
        r0 = r1.siteNameLayout;	 Catch:{ Exception -> 0x36c3 }
        r4 = r1.siteNameLayout;	 Catch:{ Exception -> 0x36c3 }
        r4 = r4.getLineCount();	 Catch:{ Exception -> 0x36c3 }
        r6 = 1;
        r4 = r4 - r6;
        r0 = r0.getLineBottom(r4);	 Catch:{ Exception -> 0x36c3 }
        r4 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x36c3 }
        r4 = r4 + r0;
        r1.linkPreviewHeight = r4;	 Catch:{ Exception -> 0x36c3 }
        r4 = r1.totalHeight;	 Catch:{ Exception -> 0x36c3 }
        r4 = r4 + r0;
        r1.totalHeight = r4;	 Catch:{ Exception -> 0x36c3 }
        goto L_0x36ca;
    L_0x36c3:
        r0 = move-exception;
        goto L_0x36c7;
    L_0x36c5:
        r0 = move-exception;
        r5 = 0;
    L_0x36c7:
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x36ca:
        r1.descriptionX = r15;	 Catch:{ Exception -> 0x373d }
        r0 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x373d }
        if (r0 == 0) goto L_0x36d9;
    L_0x36d0:
        r0 = r1.totalHeight;	 Catch:{ Exception -> 0x373d }
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r20);	 Catch:{ Exception -> 0x373d }
        r0 = r0 + r4;
        r1.totalHeight = r0;	 Catch:{ Exception -> 0x373d }
    L_0x36d9:
        r0 = r3.description;	 Catch:{ Exception -> 0x373d }
        r31 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;	 Catch:{ Exception -> 0x373d }
        r33 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x373d }
        r34 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r29);	 Catch:{ Exception -> 0x373d }
        r3 = (float) r3;	 Catch:{ Exception -> 0x373d }
        r36 = 0;
        r37 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x373d }
        r39 = 6;
        r30 = r0;
        r32 = r2;
        r35 = r3;
        r38 = r2;
        r0 = org.telegram.p004ui.Components.StaticLayoutEx.createStaticLayout(r30, r31, r32, r33, r34, r35, r36, r37, r38, r39);	 Catch:{ Exception -> 0x373d }
        r1.descriptionLayout = r0;	 Catch:{ Exception -> 0x373d }
        r0 = r1.descriptionLayout;	 Catch:{ Exception -> 0x373d }
        r2 = r1.descriptionLayout;	 Catch:{ Exception -> 0x373d }
        r2 = r2.getLineCount();	 Catch:{ Exception -> 0x373d }
        r3 = 1;
        r2 = r2 - r3;
        r0 = r0.getLineBottom(r2);	 Catch:{ Exception -> 0x373d }
        r2 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x373d }
        r2 = r2 + r0;
        r1.linkPreviewHeight = r2;	 Catch:{ Exception -> 0x373d }
        r2 = r1.totalHeight;	 Catch:{ Exception -> 0x373d }
        r2 = r2 + r0;
        r1.totalHeight = r2;	 Catch:{ Exception -> 0x373d }
        r0 = 0;
    L_0x3713:
        r2 = r1.descriptionLayout;	 Catch:{ Exception -> 0x373d }
        r2 = r2.getLineCount();	 Catch:{ Exception -> 0x373d }
        if (r0 >= r2) goto L_0x3741;
    L_0x371b:
        r2 = r1.descriptionLayout;	 Catch:{ Exception -> 0x373d }
        r2 = r2.getLineLeft(r0);	 Catch:{ Exception -> 0x373d }
        r2 = (double) r2;	 Catch:{ Exception -> 0x373d }
        r2 = java.lang.Math.ceil(r2);	 Catch:{ Exception -> 0x373d }
        r2 = (int) r2;	 Catch:{ Exception -> 0x373d }
        if (r2 == 0) goto L_0x373a;
    L_0x3729:
        r3 = r1.descriptionX;	 Catch:{ Exception -> 0x373d }
        if (r3 != 0) goto L_0x3731;
    L_0x372d:
        r2 = -r2;
        r1.descriptionX = r2;	 Catch:{ Exception -> 0x373d }
        goto L_0x373a;
    L_0x3731:
        r3 = r1.descriptionX;	 Catch:{ Exception -> 0x373d }
        r2 = -r2;
        r2 = java.lang.Math.max(r3, r2);	 Catch:{ Exception -> 0x373d }
        r1.descriptionX = r2;	 Catch:{ Exception -> 0x373d }
    L_0x373a:
        r0 = r0 + 1;
        goto L_0x3713;
    L_0x373d:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x3741:
        r0 = r1.totalHeight;
        r2 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 + r2;
        r1.totalHeight = r0;
        if (r9 == 0) goto L_0x3769;
    L_0x374e:
        r0 = r1.totalHeight;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        r1.totalHeight = r0;
        r2 = 2;
        if (r9 != r2) goto L_0x3769;
    L_0x375c:
        r0 = r1.captionHeight;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        r1.captionHeight = r0;
        goto L_0x3769;
    L_0x3768:
        r5 = 0;
    L_0x3769:
        r0 = r1.botButtons;
        r0.clear();
        if (r16 == 0) goto L_0x377d;
    L_0x3770:
        r0 = r1.botButtonsByData;
        r0.clear();
        r0 = r1.botButtonsByPosition;
        r0.clear();
        r2 = 0;
        r1.botButtonsLayout = r2;
    L_0x377d:
        r0 = r1.currentPosition;
        if (r0 != 0) goto L_0x397b;
    L_0x3781:
        r0 = r14.messageOwner;
        r0 = r0.reply_markup;
        r2 = r0 instanceof org.telegram.tgnet.TLRPC.TL_replyInlineMarkup;
        if (r2 == 0) goto L_0x397b;
    L_0x3789:
        r0 = r0.rows;
        r0 = r0.size();
        r2 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = r2 * r0;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r29);
        r2 = r2 + r3;
        r1.keyboardHeight = r2;
        r1.substractBackgroundHeight = r2;
        r2 = r1.backgroundWidth;
        r3 = r1.mediaBackground;
        if (r3 == 0) goto L_0x37a7;
    L_0x37a6:
        goto L_0x37ab;
    L_0x37a7:
        r10 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r5 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
    L_0x37ab:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r2 = r2 - r3;
        r1.widthForButtons = r2;
        r2 = r14.wantedBotKeyboardWidth;
        r3 = r1.widthForButtons;
        if (r2 <= r3) goto L_0x37fd;
    L_0x37b8:
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x37cb;
    L_0x37bc:
        r2 = r60.needDrawAvatar();
        if (r2 == 0) goto L_0x37cb;
    L_0x37c2:
        r2 = r60.isOutOwner();
        if (r2 != 0) goto L_0x37cb;
    L_0x37c8:
        r2 = 1115160576; // 0x42780000 float:62.0 double:5.5096253E-315;
        goto L_0x37cd;
    L_0x37cb:
        r2 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
    L_0x37cd:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = -r2;
        r3 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r3 == 0) goto L_0x37dd;
    L_0x37d8:
        r3 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        goto L_0x37ee;
    L_0x37dd:
        r3 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r3.x;
        r3 = r3.y;
        r3 = java.lang.Math.min(r4, r3);
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 - r4;
    L_0x37ee:
        r2 = r2 + r3;
        r3 = r1.backgroundWidth;
        r4 = r14.wantedBotKeyboardWidth;
        r2 = java.lang.Math.min(r4, r2);
        r2 = java.lang.Math.max(r3, r2);
        r1.widthForButtons = r2;
    L_0x37fd:
        r2 = new java.util.HashMap;
        r3 = r1.botButtonsByData;
        r2.<init>(r3);
        r3 = r14.botButtonsLayout;
        if (r3 == 0) goto L_0x381e;
    L_0x3808:
        r4 = r1.botButtonsLayout;
        if (r4 == 0) goto L_0x381e;
    L_0x380c:
        r3 = r3.toString();
        r3 = r4.equals(r3);
        if (r3 == 0) goto L_0x381e;
    L_0x3816:
        r3 = new java.util.HashMap;
        r4 = r1.botButtonsByPosition;
        r3.<init>(r4);
        goto L_0x3829;
    L_0x381e:
        r3 = r14.botButtonsLayout;
        if (r3 == 0) goto L_0x3828;
    L_0x3822:
        r3 = r3.toString();
        r1.botButtonsLayout = r3;
    L_0x3828:
        r3 = 0;
    L_0x3829:
        r4 = r1.botButtonsByData;
        r4.clear();
        r4 = 0;
        r5 = 0;
    L_0x3830:
        if (r4 >= r0) goto L_0x3978;
    L_0x3832:
        r6 = r14.messageOwner;
        r6 = r6.reply_markup;
        r6 = r6.rows;
        r6 = r6.get(r4);
        r6 = (org.telegram.tgnet.TLRPC.TL_keyboardButtonRow) r6;
        r7 = r6.buttons;
        r7 = r7.size();
        if (r7 != 0) goto L_0x3848;
    L_0x3846:
        goto L_0x3974;
    L_0x3848:
        r8 = r1.widthForButtons;
        r9 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r10 = r7 + -1;
        r9 = r9 * r10;
        r8 = r8 - r9;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r8 = r8 - r9;
        r8 = r8 / r7;
        r7 = r5;
        r5 = 0;
    L_0x385d:
        r9 = r6.buttons;
        r9 = r9.size();
        if (r5 >= r9) goto L_0x3973;
    L_0x3865:
        r9 = new org.telegram.ui.Cells.ChatMessageCell$BotButton;
        r10 = 0;
        r9.<init>(r1, r10);
        r10 = r6.buttons;
        r10 = r10.get(r5);
        r10 = (org.telegram.tgnet.TLRPC.KeyboardButton) r10;
        r9.button = r10;
        r10 = r9.button;
        r10 = r10.data;
        r10 = org.telegram.messenger.Utilities.bytesToHex(r10);
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r11.append(r4);
        r12 = "";
        r11.append(r12);
        r11.append(r5);
        r11 = r11.toString();
        if (r3 == 0) goto L_0x389d;
    L_0x3896:
        r12 = r3.get(r11);
        r12 = (org.telegram.p004ui.Cells.ChatMessageCell.BotButton) r12;
        goto L_0x38a3;
    L_0x389d:
        r12 = r2.get(r10);
        r12 = (org.telegram.p004ui.Cells.ChatMessageCell.BotButton) r12;
    L_0x38a3:
        if (r12 == 0) goto L_0x38bb;
    L_0x38a5:
        r13 = r12.progressAlpha;
        r9.progressAlpha = r13;
        r13 = r12.angle;
        r9.angle = r13;
        r12 = r12.lastUpdateTime;
        r9.lastUpdateTime = r12;
        goto L_0x38c2;
    L_0x38bb:
        r12 = java.lang.System.currentTimeMillis();
        r9.lastUpdateTime = r12;
    L_0x38c2:
        r12 = r1.botButtonsByData;
        r12.put(r10, r9);
        r10 = r1.botButtonsByPosition;
        r10.put(r11, r9);
        r10 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r10 = r10 + r8;
        r10 = r10 * r5;
        r9.f575x = r10;
        r10 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r10 = r10 * r4;
        r11 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r10 = r10 + r11;
        r9.f576y = r10;
        r9.width = r8;
        r10 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r9.height = r10;
        r10 = r9.button;
        r10 = r10 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonBuy;
        if (r10 == 0) goto L_0x3912;
    L_0x38fe:
        r10 = r14.messageOwner;
        r10 = r10.media;
        r10 = r10.flags;
        r10 = r10 & 4;
        if (r10 == 0) goto L_0x3912;
    L_0x3908:
        r10 = 2131560388; // 0x7f0d07c4 float:1.8746147E38 double:1.0531307597E-314;
        r11 = "PaymentReceipt";
        r10 = org.telegram.messenger.LocaleController.getString(r11, r10);
        goto L_0x3937;
    L_0x3912:
        r10 = r9.button;
        r10 = r10.text;
        r11 = org.telegram.p004ui.ActionBar.Theme.chat_botButtonPaint;
        r11 = r11.getFontMetricsInt();
        r12 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r10 = org.telegram.messenger.Emoji.replaceEmoji(r10, r11, r12, r15);
        r11 = org.telegram.p004ui.ActionBar.Theme.chat_botButtonPaint;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r12 = r8 - r12;
        r12 = (float) r12;
        r13 = android.text.TextUtils.TruncateAt.END;
        r10 = android.text.TextUtils.ellipsize(r10, r11, r12, r13);
    L_0x3937:
        r31 = r10;
        r10 = new android.text.StaticLayout;
        r32 = org.telegram.p004ui.ActionBar.Theme.chat_botButtonPaint;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r33 = r8 - r11;
        r34 = android.text.Layout.Alignment.ALIGN_CENTER;
        r35 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r36 = 0;
        r37 = 0;
        r30 = r10;
        r30.<init>(r31, r32, r33, r34, r35, r36, r37);
        r9.title = r10;
        r10 = r1.botButtons;
        r10.add(r9);
        r10 = r6.buttons;
        r10 = r10.size();
        r11 = 1;
        r10 = r10 - r11;
        if (r5 != r10) goto L_0x396f;
    L_0x3962:
        r10 = r9.f575x;
        r9 = r9.width;
        r10 = r10 + r9;
        r7 = java.lang.Math.max(r7, r10);
    L_0x396f:
        r5 = r5 + 1;
        goto L_0x385d;
    L_0x3973:
        r5 = r7;
    L_0x3974:
        r4 = r4 + 1;
        goto L_0x3830;
    L_0x3978:
        r1.widthForButtons = r5;
        goto L_0x397f;
    L_0x397b:
        r1.substractBackgroundHeight = r15;
        r1.keyboardHeight = r15;
    L_0x397f:
        r0 = r1.drawPinnedBottom;
        if (r0 == 0) goto L_0x3991;
    L_0x3983:
        r0 = r1.drawPinnedTop;
        if (r0 == 0) goto L_0x3991;
    L_0x3987:
        r0 = r1.totalHeight;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r0 = r0 - r2;
        r1.totalHeight = r0;
        goto L_0x39b8;
    L_0x3991:
        r0 = r1.drawPinnedBottom;
        if (r0 == 0) goto L_0x399f;
    L_0x3995:
        r0 = r1.totalHeight;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r29);
        r0 = r0 - r2;
        r1.totalHeight = r0;
        goto L_0x39b8;
    L_0x399f:
        r0 = r1.drawPinnedTop;
        if (r0 == 0) goto L_0x39b8;
    L_0x39a3:
        r0 = r1.pinnedBottom;
        if (r0 == 0) goto L_0x39b8;
    L_0x39a7:
        r0 = r1.currentPosition;
        if (r0 == 0) goto L_0x39b8;
    L_0x39ab:
        r0 = r0.siblingHeights;
        if (r0 != 0) goto L_0x39b8;
    L_0x39af:
        r0 = r1.totalHeight;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r29);
        r0 = r0 - r2;
        r1.totalHeight = r0;
    L_0x39b8:
        r0 = r60.isAnyKindOfSticker();
        if (r0 == 0) goto L_0x39d0;
    L_0x39be:
        r0 = r1.totalHeight;
        r2 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        if (r0 >= r2) goto L_0x39d0;
    L_0x39c8:
        r0 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r1.totalHeight = r0;
    L_0x39d0:
        r0 = r1.drawPhotoImage;
        if (r0 != 0) goto L_0x39da;
    L_0x39d4:
        r0 = r1.photoImage;
        r2 = 0;
        r0.setImageBitmap(r2);
    L_0x39da:
        r0 = r1.documentAttachType;
        r2 = 5;
        if (r0 != r2) goto L_0x3a11;
    L_0x39df:
        r0 = r1.documentAttach;
        r0 = org.telegram.messenger.MessageObject.isDocumentHasThumb(r0);
        if (r0 == 0) goto L_0x39f9;
    L_0x39e7:
        r0 = r1.documentAttach;
        r0 = r0.thumbs;
        r2 = 90;
        r0 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r0, r2);
        r2 = r1.radialProgress;
        r3 = r1.documentAttach;
        r2.setImageOverlay(r0, r3, r14);
        goto L_0x3a17;
    L_0x39f9:
        r2 = 1;
        r0 = r14.getArtworkUrl(r2);
        r2 = android.text.TextUtils.isEmpty(r0);
        if (r2 != 0) goto L_0x3a0a;
    L_0x3a04:
        r2 = r1.radialProgress;
        r2.setImageOverlay(r0);
        goto L_0x3a17;
    L_0x3a0a:
        r0 = r1.radialProgress;
        r2 = 0;
        r0.setImageOverlay(r2, r2, r2);
        goto L_0x3a17;
    L_0x3a11:
        r2 = 0;
        r0 = r1.radialProgress;
        r0.setImageOverlay(r2, r2, r2);
    L_0x3a17:
        r59.updateWaveform();
        if (r17 == 0) goto L_0x3a22;
    L_0x3a1c:
        r0 = r14.cancelEditing;
        if (r0 != 0) goto L_0x3a22;
    L_0x3a20:
        r0 = 1;
        goto L_0x3a23;
    L_0x3a22:
        r0 = 0;
    L_0x3a23:
        r2 = 1;
        r1.updateButtonState(r15, r0, r2);
        r0 = r1.buttonState;
        r2 = 2;
        if (r0 != r2) goto L_0x3a57;
    L_0x3a2c:
        r0 = r1.documentAttachType;
        r2 = 3;
        if (r0 != r2) goto L_0x3a57;
    L_0x3a31:
        r0 = r1.currentAccount;
        r0 = org.telegram.messenger.DownloadController.getInstance(r0);
        r0 = r0.canDownloadMedia(r14);
        if (r0 == 0) goto L_0x3a57;
    L_0x3a3d:
        r0 = r1.currentAccount;
        r0 = org.telegram.messenger.FileLoader.getInstance(r0);
        r2 = r1.documentAttach;
        r3 = r1.currentMessageObject;
        r4 = 1;
        r0.loadFile(r2, r3, r4, r15);
        r0 = 4;
        r1.buttonState = r0;
        r0 = r1.radialProgress;
        r2 = r59.getIconForCurrentState();
        r0.setIcon(r2, r15, r15);
    L_0x3a57:
        r0 = r1.accessibilityVirtualViewBounds;
        r0.clear();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.setMessageContent(org.telegram.messenger.MessageObject, org.telegram.messenger.MessageObject$GroupedMessages, boolean, boolean):void");
    }

    static /* synthetic */ int lambda$setMessageContent$0(PollButton pollButton, PollButton pollButton2) {
        if (pollButton.decimal > pollButton2.decimal) {
            return -1;
        }
        return pollButton.decimal < pollButton2.decimal ? 1 : 0;
    }

    public void checkVideoPlayback(boolean z) {
        if (!this.currentMessageObject.isVideo()) {
            if (z) {
                MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                z = playingMessageObject == null || !playingMessageObject.isRoundVideo();
            }
            this.photoImage.setAllowStartAnimation(z);
            if (z) {
                this.photoImage.startAnimation();
            } else {
                this.photoImage.stopAnimation();
            }
        } else if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
            this.photoImage.setAllowStartAnimation(false);
            this.photoImage.stopAnimation();
        } else {
            this.photoImage.setAllowStartAnimation(true);
            this.photoImage.startAnimation();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLongPress() {
        CharacterStyle characterStyle = this.pressedLink;
        if (characterStyle instanceof URLSpanMono) {
            this.delegate.didPressUrl(this.currentMessageObject, characterStyle, true);
            return;
        }
        if (characterStyle instanceof URLSpanNoUnderline) {
            if (((URLSpanNoUnderline) characterStyle).getURL().startsWith("/")) {
                this.delegate.didPressUrl(this.currentMessageObject, this.pressedLink, true);
                return;
            }
        } else if (characterStyle instanceof URLSpan) {
            this.delegate.didPressUrl(this.currentMessageObject, characterStyle, true);
            return;
        }
        resetPressedLink(-1);
        if (!(this.buttonPressed == 0 && this.miniButtonPressed == 0 && this.videoButtonPressed == 0 && this.pressedBotButton == -1)) {
            this.buttonPressed = 0;
            this.miniButtonPressed = 0;
            this.videoButtonPressed = 0;
            this.pressedBotButton = -1;
            invalidate();
        }
        this.linkPreviewPressed = false;
        this.otherPressed = false;
        this.sharePressed = false;
        this.imagePressed = false;
        this.gamePreviewPressed = false;
        if (this.instantPressed) {
            this.instantButtonPressed = false;
            this.instantPressed = false;
            if (VERSION.SDK_INT >= 21) {
                Drawable drawable = this.selectorDrawable;
                if (drawable != null) {
                    drawable.setState(StateSet.NOTHING);
                }
            }
            invalidate();
        }
        if (this.pressedVoteButton != -1) {
            this.pressedVoteButton = -1;
            if (VERSION.SDK_INT >= 21) {
                Drawable drawable2 = this.selectorDrawable;
                if (drawable2 != null) {
                    drawable2.setState(StateSet.NOTHING);
                }
            }
            invalidate();
        }
        ChatMessageCellDelegate chatMessageCellDelegate = this.delegate;
        if (chatMessageCellDelegate != null) {
            chatMessageCellDelegate.didLongPress(this, this.lastTouchX, this.lastTouchY);
        }
    }

    public void setCheckPressed(boolean z, boolean z2) {
        this.isCheckPressed = z;
        this.isPressed = z2;
        updateRadialProgressBackground();
        if (this.useSeekBarWaweform) {
            this.seekBarWaveform.setSelected(isDrawSelectionBackground());
        } else {
            this.seekBar.setSelected(isDrawSelectionBackground());
        }
        invalidate();
    }

    public void setInvalidatesParent(boolean z) {
        this.invalidatesParent = z;
    }

    public void invalidate() {
        super.invalidate();
        if (this.invalidatesParent && getParent() != null) {
            View view = (View) getParent();
            if (view.getParent() != null) {
                ((View) view.getParent()).invalidate();
            }
        }
    }

    public void setHighlightedAnimated() {
        this.isHighlightedAnimated = true;
        this.highlightProgress = 1000;
        this.lastHighlightProgressTime = System.currentTimeMillis();
        invalidate();
        if (getParent() != null) {
            ((View) getParent()).invalidate();
        }
    }

    public boolean isHighlighted() {
        return this.isHighlighted;
    }

    public void setHighlighted(boolean z) {
        if (this.isHighlighted != z) {
            this.isHighlighted = z;
            if (this.isHighlighted) {
                this.isHighlightedAnimated = false;
                this.highlightProgress = 0;
            } else {
                this.lastHighlightProgressTime = System.currentTimeMillis();
                this.isHighlightedAnimated = true;
                this.highlightProgress = 300;
            }
            updateRadialProgressBackground();
            if (this.useSeekBarWaweform) {
                this.seekBarWaveform.setSelected(isDrawSelectionBackground());
            } else {
                this.seekBar.setSelected(isDrawSelectionBackground());
            }
            invalidate();
            if (getParent() != null) {
                ((View) getParent()).invalidate();
            }
        }
    }

    public void setPressed(boolean z) {
        super.setPressed(z);
        updateRadialProgressBackground();
        if (this.useSeekBarWaweform) {
            this.seekBarWaveform.setSelected(isDrawSelectionBackground());
        } else {
            this.seekBar.setSelected(isDrawSelectionBackground());
        }
        invalidate();
    }

    private void updateRadialProgressBackground() {
        if (!this.drawRadialCheckBackground) {
            boolean z = true;
            Object obj = ((this.isHighlighted || this.isPressed || isPressed()) && !(this.drawPhotoImage && this.photoImage.hasBitmapImage())) ? 1 : null;
            RadialProgress2 radialProgress2 = this.radialProgress;
            boolean z2 = (obj == null && this.buttonPressed == 0) ? false : true;
            radialProgress2.setPressed(z2, false);
            if (this.hasMiniProgress != 0) {
                radialProgress2 = this.radialProgress;
                z2 = (obj == null && this.miniButtonPressed == 0) ? false : true;
                radialProgress2.setPressed(z2, true);
            }
            radialProgress2 = this.videoRadialProgress;
            if (obj == null && this.videoButtonPressed == 0) {
                z = false;
            }
            radialProgress2.setPressed(z, false);
        }
    }

    public void onSeekBarDrag(float f) {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            messageObject.audioProgress = f;
            MediaController.getInstance().seekToProgress(this.currentMessageObject, f);
        }
    }

    private void updateWaveform() {
        if (this.currentMessageObject != null && this.documentAttachType == 3) {
            boolean z = false;
            for (int i = 0; i < this.documentAttach.attributes.size(); i++) {
                DocumentAttribute documentAttribute = (DocumentAttribute) this.documentAttach.attributes.get(i);
                if (documentAttribute instanceof TL_documentAttributeAudio) {
                    byte[] bArr = documentAttribute.waveform;
                    if (bArr == null || bArr.length == 0) {
                        MediaController.getInstance().generateWaveform(this.currentMessageObject);
                    }
                    if (documentAttribute.waveform != null) {
                        z = true;
                    }
                    this.useSeekBarWaweform = z;
                    this.seekBarWaveform.setWaveform(documentAttribute.waveform);
                    return;
                }
            }
        }
    }

    private int createDocumentLayout(int i, MessageObject messageObject) {
        int i2 = i;
        MessageObject messageObject2 = messageObject;
        if (messageObject2.type == 0) {
            this.documentAttach = messageObject2.messageOwner.media.webpage.document;
        } else {
            this.documentAttach = messageObject2.messageOwner.media.document;
        }
        Document document = this.documentAttach;
        int i3 = 0;
        if (document == null) {
            return 0;
        }
        int i4;
        int dp;
        if (MessageObject.isVoiceDocument(document)) {
            this.documentAttachType = 3;
            for (i4 = 0; i4 < this.documentAttach.attributes.size(); i4++) {
                DocumentAttribute documentAttribute = (DocumentAttribute) this.documentAttach.attributes.get(i4);
                if (documentAttribute instanceof TL_documentAttributeAudio) {
                    i4 = documentAttribute.duration;
                    break;
                }
            }
            i4 = 0;
            this.widthBeforeNewTimeLine = (i2 - AndroidUtilities.m26dp(94.0f)) - ((int) Math.ceil((double) Theme.chat_audioTimePaint.measureText("00:00")));
            this.availableTimeWidth = i2 - AndroidUtilities.m26dp(18.0f);
            measureTime(messageObject2);
            dp = AndroidUtilities.m26dp(174.0f) + this.timeWidth;
            if (!this.hasLinkPreview) {
                this.backgroundWidth = Math.min(i2, dp + (i4 * AndroidUtilities.m26dp(10.0f)));
            }
            this.seekBarWaveform.setMessageObject(messageObject2);
            return 0;
        } else if (MessageObject.isMusicDocument(this.documentAttach)) {
            this.documentAttachType = 5;
            i2 -= AndroidUtilities.m26dp(86.0f);
            if (i2 < 0) {
                i2 = AndroidUtilities.m26dp(100.0f);
            }
            this.songLayout = new StaticLayout(TextUtils.ellipsize(messageObject.getMusicTitle().replace(10, ' '), Theme.chat_audioTitlePaint, (float) (i2 - AndroidUtilities.m26dp(12.0f)), TruncateAt.END), Theme.chat_audioTitlePaint, i2, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (this.songLayout.getLineCount() > 0) {
                this.songX = -((int) Math.ceil((double) this.songLayout.getLineLeft(0)));
            }
            this.performerLayout = new StaticLayout(TextUtils.ellipsize(messageObject.getMusicAuthor().replace(10, ' '), Theme.chat_audioPerformerPaint, (float) i2, TruncateAt.END), Theme.chat_audioPerformerPaint, i2, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (this.performerLayout.getLineCount() > 0) {
                this.performerX = -((int) Math.ceil((double) this.performerLayout.getLineLeft(0)));
            }
            for (i2 = 0; i2 < this.documentAttach.attributes.size(); i2++) {
                DocumentAttribute documentAttribute2 = (DocumentAttribute) this.documentAttach.attributes.get(i2);
                if (documentAttribute2 instanceof TL_documentAttributeAudio) {
                    i2 = documentAttribute2.duration;
                    break;
                }
            }
            i2 = 0;
            TextPaint textPaint = Theme.chat_audioTimePaint;
            r4 = new Object[4];
            dp = i2 / 60;
            r4[0] = Integer.valueOf(dp);
            i2 %= 60;
            r4[1] = Integer.valueOf(i2);
            r4[2] = Integer.valueOf(dp);
            r4[3] = Integer.valueOf(i2);
            i2 = (int) Math.ceil((double) textPaint.measureText(String.format("%d:%02d / %d:%02d", r4)));
            this.widthBeforeNewTimeLine = (this.backgroundWidth - AndroidUtilities.m26dp(86.0f)) - i2;
            this.availableTimeWidth = this.backgroundWidth - AndroidUtilities.m26dp(28.0f);
            return i2;
        } else {
            String str = "%s";
            String format;
            if (MessageObject.isVideoDocument(this.documentAttach)) {
                this.documentAttachType = 4;
                if (!messageObject.needDrawBluredPreview()) {
                    updatePlayingMessageProgress();
                    format = String.format(str, new Object[]{AndroidUtilities.formatFileSize((long) this.documentAttach.size)});
                    this.docTitleWidth = (int) Math.ceil((double) Theme.chat_infoPaint.measureText(format));
                    this.docTitleLayout = new StaticLayout(format, Theme.chat_infoPaint, this.docTitleWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                }
                return 0;
            } else if (MessageObject.isGifDocument(this.documentAttach)) {
                this.documentAttachType = 2;
                if (!messageObject.needDrawBluredPreview()) {
                    String string = LocaleController.getString("AttachGif", C1067R.string.AttachGif);
                    this.infoWidth = (int) Math.ceil((double) Theme.chat_infoPaint.measureText(string));
                    this.infoLayout = new StaticLayout(string, Theme.chat_infoPaint, this.infoWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    format = String.format(str, new Object[]{AndroidUtilities.formatFileSize((long) this.documentAttach.size)});
                    this.docTitleWidth = (int) Math.ceil((double) Theme.chat_infoPaint.measureText(format));
                    this.docTitleLayout = new StaticLayout(format, Theme.chat_infoPaint, this.docTitleWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                }
                return 0;
            } else {
                int i5;
                String str2 = this.documentAttach.mime_type;
                boolean z = (str2 != null && str2.toLowerCase().startsWith("image/")) || MessageObject.isDocumentHasThumb(this.documentAttach);
                this.drawPhotoImage = z;
                if (!this.drawPhotoImage) {
                    i2 += AndroidUtilities.m26dp(30.0f);
                }
                this.documentAttachType = 1;
                str2 = FileLoader.getDocumentFileName(this.documentAttach);
                if (str2 == null || str2.length() == 0) {
                    str2 = LocaleController.getString("AttachDocument", C1067R.string.AttachDocument);
                }
                this.docTitleLayout = StaticLayoutEx.createStaticLayout(str2, Theme.chat_docNamePaint, i2, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TruncateAt.MIDDLE, i2, 2, false);
                this.docTitleOffsetX = Integer.MIN_VALUE;
                StaticLayout staticLayout = this.docTitleLayout;
                if (staticLayout == null || staticLayout.getLineCount() <= 0) {
                    this.docTitleOffsetX = 0;
                    i5 = i2;
                } else {
                    i4 = 0;
                    while (i3 < this.docTitleLayout.getLineCount()) {
                        i4 = Math.max(i4, (int) Math.ceil((double) this.docTitleLayout.getLineWidth(i3)));
                        this.docTitleOffsetX = Math.max(this.docTitleOffsetX, (int) Math.ceil((double) (-this.docTitleLayout.getLineLeft(i3))));
                        i3++;
                    }
                    i5 = Math.min(i2, i4);
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(AndroidUtilities.formatFileSize((long) this.documentAttach.size));
                stringBuilder.append(" ");
                stringBuilder.append(FileLoader.getDocumentExtension(this.documentAttach));
                str2 = stringBuilder.toString();
                this.infoWidth = Math.min(i2 - AndroidUtilities.m26dp(30.0f), (int) Math.ceil((double) Theme.chat_infoPaint.measureText(str2)));
                CharSequence ellipsize = TextUtils.ellipsize(str2, Theme.chat_infoPaint, (float) this.infoWidth, TruncateAt.END);
                try {
                    if (this.infoWidth < 0) {
                        this.infoWidth = AndroidUtilities.m26dp(10.0f);
                    }
                    this.infoLayout = new StaticLayout(ellipsize, Theme.chat_infoPaint, this.infoWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                } catch (Exception e) {
                    FileLog.m30e(e);
                }
                if (this.drawPhotoImage) {
                    this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(messageObject2.photoThumbs, 320);
                    this.currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(messageObject2.photoThumbs, 40);
                    if ((DownloadController.getInstance(this.currentAccount).getAutodownloadMask() & 1) == 0) {
                        this.currentPhotoObject = null;
                    }
                    PhotoSize photoSize = this.currentPhotoObject;
                    if (photoSize == null || photoSize == this.currentPhotoObjectThumb) {
                        this.currentPhotoObject = null;
                        this.photoImage.setNeedsQualityThumb(true);
                        this.photoImage.setShouldGenerateQualityThumb(true);
                    }
                    this.currentPhotoFilter = "86_86_b";
                    this.photoImage.setImage(ImageLocation.getForObject(this.currentPhotoObject, messageObject2.photoThumbsObject), "86_86", ImageLocation.getForObject(this.currentPhotoObjectThumb, messageObject2.photoThumbsObject), this.currentPhotoFilter, 0, null, messageObject, 1);
                }
                return i5;
            }
        }
    }

    private void calcBackgroundWidth(int i, int i2, int i3) {
        if (!(this.hasLinkPreview || this.hasOldCaptionPreview || this.hasGamePreview || this.hasInvoicePreview)) {
            MessageObject messageObject = this.currentMessageObject;
            int i4 = messageObject.lastLineWidth;
            if (i - i4 >= i2 && !messageObject.hasRtl) {
                i = i3 - i4;
                if (i < 0 || i > i2) {
                    this.backgroundWidth = Math.max(i3, this.currentMessageObject.lastLineWidth + i2) + AndroidUtilities.m26dp(31.0f);
                    return;
                } else {
                    this.backgroundWidth = ((i3 + i2) - i) + AndroidUtilities.m26dp(31.0f);
                    return;
                }
            }
        }
        this.totalHeight += AndroidUtilities.m26dp(14.0f);
        this.hasNewLineForTime = true;
        this.backgroundWidth = Math.max(i3, this.currentMessageObject.lastLineWidth) + AndroidUtilities.m26dp(31.0f);
        this.backgroundWidth = Math.max(this.backgroundWidth, (this.currentMessageObject.isOutOwner() ? this.timeWidth + AndroidUtilities.m26dp(17.0f) : this.timeWidth) + AndroidUtilities.m26dp(31.0f));
    }

    public void setHighlightedText(String str) {
        MessageObject messageObject = this.messageObjectToSet;
        if (messageObject == null) {
            messageObject = this.currentMessageObject;
        }
        if (messageObject == null || TextUtils.isEmpty(str)) {
            if (!this.urlPathSelection.isEmpty()) {
                this.linkSelectionBlockNum = -1;
                resetUrlPaths(true);
                invalidate();
            }
            return;
        }
        String toLowerCase = str.toLowerCase();
        String toLowerCase2 = messageObject.messageOwner.message.toLowerCase();
        String str2 = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\n";
        int length = toLowerCase2.length();
        int i = 0;
        int i2 = -1;
        int i3 = -1;
        while (i < length) {
            int min = Math.min(toLowerCase.length(), length - i);
            int i4 = 0;
            int i5 = 0;
            while (i4 < min) {
                Object obj = toLowerCase2.charAt(i + i4) == toLowerCase.charAt(i4) ? 1 : null;
                if (obj != null) {
                    if (i5 != 0 || i == 0 || str2.indexOf(toLowerCase2.charAt(i - 1)) >= 0) {
                        i5++;
                    } else {
                        obj = null;
                    }
                }
                if (obj == null || i4 == min - 1) {
                    if (i5 > 0 && i5 > i3) {
                        i2 = i;
                        i3 = i5;
                    }
                    i++;
                } else {
                    i4++;
                }
            }
            i++;
        }
        if (i2 == -1) {
            if (!this.urlPathSelection.isEmpty()) {
                this.linkSelectionBlockNum = -1;
                resetUrlPaths(true);
                invalidate();
            }
            return;
        }
        int i6 = i2 + i3;
        int length2 = toLowerCase2.length();
        while (i6 < length2 && str2.indexOf(toLowerCase2.charAt(i6)) < 0) {
            i3++;
            i6++;
        }
        i6 = i2 + i3;
        if (this.captionLayout != null && !TextUtils.isEmpty(messageObject.caption)) {
            resetUrlPaths(true);
            try {
                LinkPath obtainNewUrlPath = obtainNewUrlPath(true);
                obtainNewUrlPath.setCurrentLayout(this.captionLayout, i2, 0.0f);
                this.captionLayout.getSelectionPath(i2, i6, obtainNewUrlPath);
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            invalidate();
        } else if (messageObject.textLayoutBlocks != null) {
            length2 = 0;
            while (length2 < messageObject.textLayoutBlocks.size()) {
                TextLayoutBlock textLayoutBlock = (TextLayoutBlock) messageObject.textLayoutBlocks.get(length2);
                length = textLayoutBlock.charactersOffset;
                if (i2 < length || i2 >= length + textLayoutBlock.textLayout.getText().length()) {
                    length2++;
                } else {
                    this.linkSelectionBlockNum = length2;
                    resetUrlPaths(true);
                    try {
                        LinkPath obtainNewUrlPath2 = obtainNewUrlPath(true);
                        obtainNewUrlPath2.setCurrentLayout(textLayoutBlock.textLayout, i2, 0.0f);
                        textLayoutBlock.textLayout.getSelectionPath(i2, i6 - textLayoutBlock.charactersOffset, obtainNewUrlPath2);
                        if (i6 >= textLayoutBlock.charactersOffset + i3) {
                            for (length2++; length2 < messageObject.textLayoutBlocks.size(); length2++) {
                                TextLayoutBlock textLayoutBlock2 = (TextLayoutBlock) messageObject.textLayoutBlocks.get(length2);
                                length = textLayoutBlock2.textLayout.getText().length();
                                LinkPath obtainNewUrlPath3 = obtainNewUrlPath(true);
                                obtainNewUrlPath3.setCurrentLayout(textLayoutBlock2.textLayout, 0, (float) textLayoutBlock2.height);
                                textLayoutBlock2.textLayout.getSelectionPath(0, i6 - textLayoutBlock2.charactersOffset, obtainNewUrlPath3);
                                if (i6 < (textLayoutBlock.charactersOffset + length) - 1) {
                                    break;
                                }
                            }
                        }
                    } catch (Exception e2) {
                        FileLog.m30e(e2);
                    }
                    invalidate();
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.selectorDrawable;
    }

    private boolean isCurrentLocationTimeExpired(MessageObject messageObject) {
        boolean z = true;
        if (this.currentMessageObject.messageOwner.media.period % 60 == 0) {
            if (Math.abs(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - messageObject.messageOwner.date) <= messageObject.messageOwner.media.period) {
                z = false;
            }
            return z;
        }
        if (Math.abs(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - messageObject.messageOwner.date) <= messageObject.messageOwner.media.period - 5) {
            z = false;
        }
        return z;
    }

    private void checkLocationExpired() {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            boolean isCurrentLocationTimeExpired = isCurrentLocationTimeExpired(messageObject);
            if (isCurrentLocationTimeExpired != this.locationExpired) {
                this.locationExpired = isCurrentLocationTimeExpired;
                if (this.locationExpired) {
                    messageObject = this.currentMessageObject;
                    this.currentMessageObject = null;
                    setMessageObject(messageObject, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
                } else {
                    AndroidUtilities.runOnUIThread(this.invalidateRunnable, 1000);
                    this.scheduledInvalidate = true;
                    int dp = this.backgroundWidth - AndroidUtilities.m26dp(91.0f);
                    this.docTitleLayout = new StaticLayout(TextUtils.ellipsize(LocaleController.getString("AttachLiveLocation", C1067R.string.AttachLiveLocation), Theme.chat_locationTitlePaint, (float) dp, TruncateAt.END), Theme.chat_locationTitlePaint, dp, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                }
            }
        }
    }

    public void setMessageObject(MessageObject messageObject, GroupedMessages groupedMessages, boolean z, boolean z2) {
        if (this.attachedToWindow) {
            setMessageContent(messageObject, groupedMessages, z, z2);
            return;
        }
        this.messageObjectToSet = messageObject;
        this.groupedMessagesToSet = groupedMessages;
        this.bottomNearToSet = z;
        this.topNearToSet = z2;
    }

    private int getAdditionalWidthForPosition(GroupedMessagePosition groupedMessagePosition) {
        int i = 0;
        if (groupedMessagePosition == null) {
            return 0;
        }
        if ((groupedMessagePosition.flags & 2) == 0) {
            i = 0 + AndroidUtilities.m26dp(4.0f);
        }
        return (groupedMessagePosition.flags & 1) == 0 ? i + AndroidUtilities.m26dp(4.0f) : i;
    }

    private void createSelectorDrawable() {
        if (VERSION.SDK_INT >= 21) {
            Drawable drawable = this.selectorDrawable;
            String str = Theme.key_chat_outPreviewInstantText;
            String str2 = Theme.key_chat_inPreviewInstantText;
            if (drawable == null) {
                final Paint paint = new Paint(1);
                paint.setColor(-1);
                C23472 c23472 = new Drawable() {
                    RectF rect = new RectF();

                    public int getOpacity() {
                        return -1;
                    }

                    public void setAlpha(int i) {
                    }

                    public void setColorFilter(ColorFilter colorFilter) {
                    }

                    public void draw(Canvas canvas) {
                        Rect bounds = getBounds();
                        this.rect.set((float) bounds.left, (float) bounds.top, (float) bounds.right, (float) bounds.bottom);
                        canvas.drawRoundRect(this.rect, (float) AndroidUtilities.m26dp(6.0f), (float) AndroidUtilities.m26dp(6.0f), paint);
                    }
                };
                int[][] iArr = new int[][]{StateSet.WILD_CARD};
                int[] iArr2 = new int[1];
                if (!this.currentMessageObject.isOutOwner()) {
                    str = str2;
                }
                iArr2[0] = 1610612735 & Theme.getColor(str);
                this.selectorDrawable = new RippleDrawable(new ColorStateList(iArr, iArr2), null, c23472);
                this.selectorDrawable.setCallback(this);
            } else {
                if (!this.currentMessageObject.isOutOwner()) {
                    str = str2;
                }
                Theme.setSelectorDrawableColor(drawable, 1610612735 & Theme.getColor(str), true);
            }
            this.selectorDrawable.setVisible(true, false);
        }
    }

    private void createInstantViewButton() {
        if (VERSION.SDK_INT >= 21 && this.drawInstantView) {
            createSelectorDrawable();
        }
        if (this.drawInstantView && this.instantViewLayout == null) {
            CharSequence string;
            this.instantWidth = AndroidUtilities.m26dp(33.0f);
            int i = this.drawInstantViewType;
            if (i == 1) {
                string = LocaleController.getString("OpenChannel", C1067R.string.OpenChannel);
            } else if (i == 2) {
                string = LocaleController.getString("OpenGroup", C1067R.string.OpenGroup);
            } else if (i == 3) {
                string = LocaleController.getString("OpenMessage", C1067R.string.OpenMessage);
            } else if (i == 5) {
                string = LocaleController.getString("ViewContact", C1067R.string.ViewContact);
            } else if (i == 6) {
                string = LocaleController.getString("OpenBackground", C1067R.string.OpenBackground);
            } else {
                string = LocaleController.getString("InstantView", C1067R.string.InstantView);
            }
            int dp = this.backgroundWidth - AndroidUtilities.m26dp(75.0f);
            this.instantViewLayout = new StaticLayout(TextUtils.ellipsize(string, Theme.chat_instantViewPaint, (float) dp, TruncateAt.END), Theme.chat_instantViewPaint, dp + AndroidUtilities.m26dp(2.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.instantWidth = this.backgroundWidth - AndroidUtilities.m26dp(34.0f);
            this.totalHeight += AndroidUtilities.m26dp(46.0f);
            if (this.currentMessageObject.type == 12) {
                this.totalHeight += AndroidUtilities.m26dp(14.0f);
            }
            StaticLayout staticLayout = this.instantViewLayout;
            if (staticLayout != null && staticLayout.getLineCount() > 0) {
                double d = (double) this.instantWidth;
                double ceil = Math.ceil((double) this.instantViewLayout.getLineWidth(0));
                Double.isNaN(d);
                this.instantTextX = (((int) (d - ceil)) / 2) + (this.drawInstantViewType == 0 ? AndroidUtilities.m26dp(8.0f) : 0);
                this.instantTextLeftX = (int) this.instantViewLayout.getLineLeft(0);
                this.instantTextX += -this.instantTextLeftX;
            }
        }
    }

    public void requestLayout() {
        if (!this.inLayout) {
            super.requestLayout();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && (messageObject.checkLayout() || this.lastHeight != AndroidUtilities.displaySize.y)) {
            this.inLayout = true;
            messageObject = this.currentMessageObject;
            this.currentMessageObject = null;
            setMessageObject(messageObject, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
            this.inLayout = false;
        }
        setMeasuredDimension(MeasureSpec.getSize(i), this.totalHeight + this.keyboardHeight);
    }

    public void forceResetMessageObject() {
        MessageObject messageObject = this.messageObjectToSet;
        if (messageObject == null) {
            messageObject = this.currentMessageObject;
        }
        this.currentMessageObject = null;
        setMessageObject(messageObject, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
    }

    private int getGroupPhotosWidth() {
        if (AndroidUtilities.isInMultiwindow || !AndroidUtilities.isTablet() || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
            return AndroidUtilities.displaySize.x;
        }
        int i = (AndroidUtilities.displaySize.x / 100) * 35;
        if (i < AndroidUtilities.m26dp(320.0f)) {
            i = AndroidUtilities.m26dp(320.0f);
        }
        return AndroidUtilities.displaySize.x - i;
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x0461  */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x0489  */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x0461  */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x0489  */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x0461  */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x0489  */
    @android.annotation.SuppressLint({"DrawAllocation"})
    public void onLayout(boolean r11, int r12, int r13, int r14, int r15) {
        /*
        r10 = this;
        r12 = r10.currentMessageObject;
        if (r12 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r12 = 1;
        r13 = 0;
        r14 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r15 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        if (r11 != 0) goto L_0x0011;
    L_0x000d:
        r11 = r10.wasLayout;
        if (r11 != 0) goto L_0x0109;
    L_0x0011:
        r11 = r10.getMeasuredWidth();
        r10.layoutWidth = r11;
        r11 = r10.getMeasuredHeight();
        r0 = r10.substractBackgroundHeight;
        r11 = r11 - r0;
        r10.layoutHeight = r11;
        r11 = r10.timeTextWidth;
        if (r11 >= 0) goto L_0x002a;
    L_0x0024:
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r10.timeTextWidth = r11;
    L_0x002a:
        r11 = new android.text.StaticLayout;
        r1 = r10.currentTimeString;
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_timePaint;
        r0 = r10.timeTextWidth;
        r3 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = r3 + r0;
        r4 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 0;
        r7 = 0;
        r0 = r11;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);
        r10.timeLayout = r11;
        r11 = r10.mediaBackground;
        r0 = 1109917696; // 0x42280000 float:42.0 double:5.483722033E-315;
        if (r11 != 0) goto L_0x007c;
    L_0x004b:
        r11 = r10.currentMessageObject;
        r11 = r11.isOutOwner();
        if (r11 != 0) goto L_0x006d;
    L_0x0053:
        r11 = r10.backgroundWidth;
        r1 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r11 = r11 - r1;
        r1 = r10.timeWidth;
        r11 = r11 - r1;
        r1 = r10.isAvatarVisible;
        if (r1 == 0) goto L_0x0068;
    L_0x0063:
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        goto L_0x0069;
    L_0x0068:
        r1 = 0;
    L_0x0069:
        r11 = r11 + r1;
        r10.timeX = r11;
        goto L_0x00c8;
    L_0x006d:
        r11 = r10.layoutWidth;
        r1 = r10.timeWidth;
        r11 = r11 - r1;
        r1 = 1109000192; // 0x421a0000 float:38.5 double:5.47918896E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r11 = r11 - r1;
        r10.timeX = r11;
        goto L_0x00c8;
    L_0x007c:
        r11 = r10.currentMessageObject;
        r11 = r11.isOutOwner();
        if (r11 != 0) goto L_0x00bc;
    L_0x0084:
        r11 = r10.backgroundWidth;
        r1 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r11 = r11 - r1;
        r1 = r10.timeWidth;
        r11 = r11 - r1;
        r1 = r10.isAvatarVisible;
        if (r1 == 0) goto L_0x0099;
    L_0x0094:
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        goto L_0x009a;
    L_0x0099:
        r1 = 0;
    L_0x009a:
        r11 = r11 + r1;
        r10.timeX = r11;
        r11 = r10.currentPosition;
        if (r11 == 0) goto L_0x00c8;
    L_0x00a1:
        r11 = r11.leftSpanOffset;
        if (r11 == 0) goto L_0x00c8;
    L_0x00a5:
        r1 = r10.timeX;
        r11 = (float) r11;
        r2 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r11 = r11 / r2;
        r2 = r10.getGroupPhotosWidth();
        r2 = (float) r2;
        r11 = r11 * r2;
        r2 = (double) r11;
        r2 = java.lang.Math.ceil(r2);
        r11 = (int) r2;
        r1 = r1 + r11;
        r10.timeX = r1;
        goto L_0x00c8;
    L_0x00bc:
        r11 = r10.layoutWidth;
        r1 = r10.timeWidth;
        r11 = r11 - r1;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r11 = r11 - r1;
        r10.timeX = r11;
    L_0x00c8:
        r11 = r10.currentMessageObject;
        r11 = r11.messageOwner;
        r11 = r11.flags;
        r11 = r11 & 1024;
        if (r11 == 0) goto L_0x00e7;
    L_0x00d2:
        r11 = new android.text.StaticLayout;
        r2 = r10.currentViewsString;
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_timePaint;
        r4 = r10.viewsTextWidth;
        r5 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r7 = 0;
        r8 = 0;
        r1 = r11;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8);
        r10.viewsLayout = r11;
        goto L_0x00ea;
    L_0x00e7:
        r11 = 0;
        r10.viewsLayout = r11;
    L_0x00ea:
        r11 = r10.isAvatarVisible;
        if (r11 == 0) goto L_0x0107;
    L_0x00ee:
        r11 = r10.avatarImage;
        r1 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r2 = r10.avatarImage;
        r2 = r2.getImageY();
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r11.setImageCoords(r1, r2, r3, r0);
    L_0x0107:
        r10.wasLayout = r12;
    L_0x0109:
        r11 = r10.currentMessageObject;
        r11 = r11.type;
        if (r11 != 0) goto L_0x0118;
    L_0x010f:
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r0 = r10.namesOffset;
        r11 = r11 + r0;
        r10.textY = r11;
    L_0x0118:
        r11 = r10.currentMessageObject;
        r11 = r11.isRoundVideo();
        if (r11 == 0) goto L_0x0123;
    L_0x0120:
        r10.updatePlayingMessageProgress();
    L_0x0123:
        r11 = r10.documentAttachType;
        r0 = 3;
        r1 = 1117257728; // 0x42980000 float:76.0 double:5.51998661E-315;
        r2 = 1116078080; // 0x42860000 float:67.0 double:5.514158374E-315;
        r3 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r4 = 10;
        r5 = 1116602368; // 0x428e0000 float:71.0 double:5.5167487E-315;
        r6 = 1102577664; // 0x41b80000 float:23.0 double:5.447457457E-315;
        r7 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r8 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r9 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        if (r11 != r0) goto L_0x0225;
    L_0x013a:
        r11 = r10.currentMessageObject;
        r11 = r11.isOutOwner();
        if (r11 == 0) goto L_0x0169;
    L_0x0142:
        r11 = r10.layoutWidth;
        r12 = r10.backgroundWidth;
        r11 = r11 - r12;
        r12 = 1113849856; // 0x42640000 float:57.0 double:5.503149485E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r11 = r11 + r12;
        r10.seekBarX = r11;
        r11 = r10.layoutWidth;
        r12 = r10.backgroundWidth;
        r11 = r11 - r12;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r11 = r11 + r12;
        r10.buttonX = r11;
        r11 = r10.layoutWidth;
        r12 = r10.backgroundWidth;
        r11 = r11 - r12;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r11 = r11 + r12;
        r10.timeAudioX = r11;
        goto L_0x01a0;
    L_0x0169:
        r11 = r10.isChat;
        if (r11 == 0) goto L_0x018c;
    L_0x016d:
        r11 = r10.currentMessageObject;
        r11 = r11.needDrawAvatar();
        if (r11 == 0) goto L_0x018c;
    L_0x0175:
        r11 = 1122238464; // 0x42e40000 float:114.0 double:5.544594715E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r10.seekBarX = r11;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r10.buttonX = r11;
        r11 = 1123549184; // 0x42f80000 float:124.0 double:5.55107053E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r10.timeAudioX = r11;
        goto L_0x01a0;
    L_0x018c:
        r11 = 1115947008; // 0x42840000 float:66.0 double:5.51351079E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r10.seekBarX = r11;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r10.buttonX = r11;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r10.timeAudioX = r11;
    L_0x01a0:
        r11 = r10.hasLinkPreview;
        if (r11 == 0) goto L_0x01bf;
    L_0x01a4:
        r11 = r10.seekBarX;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r11 = r11 + r12;
        r10.seekBarX = r11;
        r11 = r10.buttonX;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r11 = r11 + r12;
        r10.buttonX = r11;
        r11 = r10.timeAudioX;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r11 = r11 + r12;
        r10.timeAudioX = r11;
    L_0x01bf:
        r11 = r10.seekBarWaveform;
        r12 = r10.backgroundWidth;
        r14 = r10.hasLinkPreview;
        if (r14 == 0) goto L_0x01ca;
    L_0x01c7:
        r14 = 10;
        goto L_0x01cb;
    L_0x01ca:
        r14 = 0;
    L_0x01cb:
        r14 = r14 + 92;
        r14 = (float) r14;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r12 = r12 - r14;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r11.setSize(r12, r14);
        r11 = r10.seekBar;
        r12 = r10.backgroundWidth;
        r14 = r10.hasLinkPreview;
        if (r14 == 0) goto L_0x01e4;
    L_0x01e2:
        r13 = 10;
    L_0x01e4:
        r13 = r13 + 72;
        r13 = (float) r13;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r12 = r12 - r13;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r11.setSize(r12, r13);
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r12 = r10.namesOffset;
        r11 = r11 + r12;
        r12 = r10.mediaOffsetY;
        r11 = r11 + r12;
        r10.seekBarY = r11;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r12 = r10.namesOffset;
        r11 = r11 + r12;
        r12 = r10.mediaOffsetY;
        r11 = r11 + r12;
        r10.buttonY = r11;
        r11 = r10.radialProgress;
        r12 = r10.buttonX;
        r13 = r10.buttonY;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r14 = r14 + r12;
        r15 = r10.buttonY;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r15 = r15 + r0;
        r11.setProgressRect(r12, r13, r14, r15);
        r10.updatePlayingMessageProgress();
        goto L_0x054c;
    L_0x0225:
        r0 = 5;
        if (r11 != r0) goto L_0x02fa;
    L_0x0228:
        r11 = r10.currentMessageObject;
        r11 = r11.isOutOwner();
        if (r11 == 0) goto L_0x0257;
    L_0x0230:
        r11 = r10.layoutWidth;
        r12 = r10.backgroundWidth;
        r11 = r11 - r12;
        r12 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r11 = r11 + r12;
        r10.seekBarX = r11;
        r11 = r10.layoutWidth;
        r12 = r10.backgroundWidth;
        r11 = r11 - r12;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r11 = r11 + r12;
        r10.buttonX = r11;
        r11 = r10.layoutWidth;
        r12 = r10.backgroundWidth;
        r11 = r11 - r12;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r11 = r11 + r12;
        r10.timeAudioX = r11;
        goto L_0x028e;
    L_0x0257:
        r11 = r10.isChat;
        if (r11 == 0) goto L_0x027a;
    L_0x025b:
        r11 = r10.currentMessageObject;
        r11 = r11.needDrawAvatar();
        if (r11 == 0) goto L_0x027a;
    L_0x0263:
        r11 = 1122107392; // 0x42e20000 float:113.0 double:5.543947133E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r10.seekBarX = r11;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r10.buttonX = r11;
        r11 = 1123549184; // 0x42f80000 float:124.0 double:5.55107053E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r10.timeAudioX = r11;
        goto L_0x028e;
    L_0x027a:
        r11 = 1115815936; // 0x42820000 float:65.0 double:5.51286321E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r10.seekBarX = r11;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r10.buttonX = r11;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r10.timeAudioX = r11;
    L_0x028e:
        r11 = r10.hasLinkPreview;
        if (r11 == 0) goto L_0x02ad;
    L_0x0292:
        r11 = r10.seekBarX;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r11 = r11 + r12;
        r10.seekBarX = r11;
        r11 = r10.buttonX;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r11 = r11 + r12;
        r10.buttonX = r11;
        r11 = r10.timeAudioX;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r11 = r11 + r12;
        r10.timeAudioX = r11;
    L_0x02ad:
        r11 = r10.seekBar;
        r12 = r10.backgroundWidth;
        r14 = r10.hasLinkPreview;
        if (r14 == 0) goto L_0x02b7;
    L_0x02b5:
        r13 = 10;
    L_0x02b7:
        r13 = r13 + 65;
        r13 = (float) r13;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r12 = r12 - r13;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r11.setSize(r12, r13);
        r11 = 1105723392; // 0x41e80000 float:29.0 double:5.46299942E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r12 = r10.namesOffset;
        r11 = r11 + r12;
        r12 = r10.mediaOffsetY;
        r11 = r11 + r12;
        r10.seekBarY = r11;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r12 = r10.namesOffset;
        r11 = r11 + r12;
        r12 = r10.mediaOffsetY;
        r11 = r11 + r12;
        r10.buttonY = r11;
        r11 = r10.radialProgress;
        r12 = r10.buttonX;
        r13 = r10.buttonY;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r14 = r14 + r12;
        r15 = r10.buttonY;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r15 = r15 + r0;
        r11.setProgressRect(r12, r13, r14, r15);
        r10.updatePlayingMessageProgress();
        goto L_0x054c;
    L_0x02fa:
        if (r11 != r12) goto L_0x037d;
    L_0x02fc:
        r11 = r10.drawPhotoImage;
        if (r11 != 0) goto L_0x037d;
    L_0x0300:
        r11 = r10.currentMessageObject;
        r11 = r11.isOutOwner();
        if (r11 == 0) goto L_0x0315;
    L_0x0308:
        r11 = r10.layoutWidth;
        r12 = r10.backgroundWidth;
        r11 = r11 - r12;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r11 = r11 + r12;
        r10.buttonX = r11;
        goto L_0x032e;
    L_0x0315:
        r11 = r10.isChat;
        if (r11 == 0) goto L_0x0328;
    L_0x0319:
        r11 = r10.currentMessageObject;
        r11 = r11.needDrawAvatar();
        if (r11 == 0) goto L_0x0328;
    L_0x0321:
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r10.buttonX = r11;
        goto L_0x032e;
    L_0x0328:
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r10.buttonX = r11;
    L_0x032e:
        r11 = r10.hasLinkPreview;
        if (r11 == 0) goto L_0x033b;
    L_0x0332:
        r11 = r10.buttonX;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r11 = r11 + r12;
        r10.buttonX = r11;
    L_0x033b:
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r12 = r10.namesOffset;
        r11 = r11 + r12;
        r12 = r10.mediaOffsetY;
        r11 = r11 + r12;
        r10.buttonY = r11;
        r11 = r10.radialProgress;
        r12 = r10.buttonX;
        r13 = r10.buttonY;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r14 = r14 + r12;
        r0 = r10.buttonY;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r0 = r0 + r1;
        r11.setProgressRect(r12, r13, r14, r0);
        r11 = r10.photoImage;
        r12 = r10.buttonX;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r12 = r12 - r13;
        r13 = r10.buttonY;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r13 = r13 - r14;
        r14 = r10.photoImage;
        r14 = r14.getImageWidth();
        r15 = r10.photoImage;
        r15 = r15.getImageHeight();
        r11.setImageCoords(r12, r13, r14, r15);
        goto L_0x054c;
    L_0x037d:
        r11 = r10.currentMessageObject;
        r13 = r11.type;
        r0 = 12;
        if (r13 != r0) goto L_0x03c3;
    L_0x0385:
        r11 = r11.isOutOwner();
        if (r11 == 0) goto L_0x0396;
    L_0x038b:
        r11 = r10.layoutWidth;
        r12 = r10.backgroundWidth;
        r11 = r11 - r12;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r11 = r11 + r12;
        goto L_0x03ad;
    L_0x0396:
        r11 = r10.isChat;
        if (r11 == 0) goto L_0x03a9;
    L_0x039a:
        r11 = r10.currentMessageObject;
        r11 = r11.needDrawAvatar();
        if (r11 == 0) goto L_0x03a9;
    L_0x03a2:
        r11 = 1116733440; // 0x42900000 float:72.0 double:5.517396283E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        goto L_0x03ad;
    L_0x03a9:
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
    L_0x03ad:
        r12 = r10.photoImage;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r14 = r10.namesOffset;
        r13 = r13 + r14;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r12.setImageCoords(r11, r13, r14, r15);
        goto L_0x054c;
    L_0x03c3:
        if (r13 != 0) goto L_0x0413;
    L_0x03c5:
        r11 = r10.hasLinkPreview;
        if (r11 != 0) goto L_0x03d1;
    L_0x03c9:
        r11 = r10.hasGamePreview;
        if (r11 != 0) goto L_0x03d1;
    L_0x03cd:
        r11 = r10.hasInvoicePreview;
        if (r11 == 0) goto L_0x0413;
    L_0x03d1:
        r11 = r10.hasGamePreview;
        if (r11 == 0) goto L_0x03dd;
    L_0x03d5:
        r11 = r10.unmovedTextX;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r11 = r11 - r13;
        goto L_0x03f3;
    L_0x03dd:
        r11 = r10.hasInvoicePreview;
        if (r11 == 0) goto L_0x03ea;
    L_0x03e1:
        r11 = r10.unmovedTextX;
        r13 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        goto L_0x03f2;
    L_0x03ea:
        r11 = r10.unmovedTextX;
        r13 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
    L_0x03f2:
        r11 = r11 + r13;
    L_0x03f3:
        r13 = r10.isSmallImage;
        if (r13 == 0) goto L_0x0401;
    L_0x03f7:
        r13 = r10.backgroundWidth;
        r11 = r11 + r13;
        r13 = 1117913088; // 0x42a20000 float:81.0 double:5.52322452E-315;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        goto L_0x042a;
    L_0x0401:
        r13 = r10.hasInvoicePreview;
        if (r13 == 0) goto L_0x040e;
    L_0x0405:
        r13 = 1086953882; // 0x40c9999a float:6.3 double:5.370265717E-315;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r13 = -r13;
        goto L_0x0437;
    L_0x040e:
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        goto L_0x0437;
    L_0x0413:
        r11 = r10.currentMessageObject;
        r11 = r11.isOutOwner();
        if (r11 == 0) goto L_0x0439;
    L_0x041b:
        r11 = r10.mediaBackground;
        if (r11 == 0) goto L_0x042c;
    L_0x041f:
        r11 = r10.layoutWidth;
        r13 = r10.backgroundWidth;
        r11 = r11 - r13;
        r13 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
    L_0x042a:
        r11 = r11 - r13;
        goto L_0x045b;
    L_0x042c:
        r11 = r10.layoutWidth;
        r13 = r10.backgroundWidth;
        r11 = r11 - r13;
        r13 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
    L_0x0437:
        r11 = r11 + r13;
        goto L_0x045b;
    L_0x0439:
        r11 = r10.isChat;
        if (r11 == 0) goto L_0x0448;
    L_0x043d:
        r11 = r10.isAvatarVisible;
        if (r11 == 0) goto L_0x0448;
    L_0x0441:
        r11 = 1115422720; // 0x427c0000 float:63.0 double:5.510920465E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        goto L_0x044e;
    L_0x0448:
        r11 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
    L_0x044e:
        r13 = r10.currentPosition;
        if (r13 == 0) goto L_0x045b;
    L_0x0452:
        r13 = r13.edge;
        if (r13 != 0) goto L_0x045b;
    L_0x0456:
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        goto L_0x042a;
    L_0x045b:
        r13 = r10.currentPosition;
        r15 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r13 == 0) goto L_0x0483;
    L_0x0461:
        r13 = r13.flags;
        r12 = r12 & r13;
        if (r12 != 0) goto L_0x046b;
    L_0x0466:
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r11 = r11 - r12;
    L_0x046b:
        r12 = r10.currentPosition;
        r12 = r12.leftSpanOffset;
        if (r12 == 0) goto L_0x0483;
    L_0x0471:
        r12 = (float) r12;
        r13 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r12 = r12 / r13;
        r13 = r10.getGroupPhotosWidth();
        r13 = (float) r13;
        r12 = r12 * r13;
        r12 = (double) r12;
        r12 = java.lang.Math.ceil(r12);
        r12 = (int) r12;
        r11 = r11 + r12;
    L_0x0483:
        r12 = r10.currentMessageObject;
        r12 = r12.type;
        if (r12 == 0) goto L_0x048e;
    L_0x0489:
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r11 = r11 - r12;
    L_0x048e:
        r12 = r10.photoImage;
        r13 = r12.getImageY();
        r0 = r10.photoImage;
        r0 = r0.getImageWidth();
        r1 = r10.photoImage;
        r1 = r1.getImageHeight();
        r12.setImageCoords(r11, r13, r0, r1);
        r11 = (float) r11;
        r12 = r10.photoImage;
        r12 = r12.getImageWidth();
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r12 = r12 - r13;
        r12 = (float) r12;
        r12 = r12 / r15;
        r11 = r11 + r12;
        r11 = (int) r11;
        r10.buttonX = r11;
        r11 = r10.photoImage;
        r11 = r11.getImageY();
        r12 = r10.photoImage;
        r12 = r12.getImageHeight();
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r12 = r12 - r13;
        r12 = r12 / 2;
        r11 = r11 + r12;
        r10.buttonY = r11;
        r11 = r10.radialProgress;
        r12 = r10.buttonX;
        r13 = r10.buttonY;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r15 = r15 + r12;
        r0 = r10.buttonY;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r0 = r0 + r14;
        r11.setProgressRect(r12, r13, r15, r0);
        r11 = r10.deleteProgressRect;
        r12 = r10.buttonX;
        r13 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r12 = r12 + r13;
        r12 = (float) r12;
        r13 = r10.buttonY;
        r14 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r13 = r13 + r14;
        r13 = (float) r13;
        r14 = r10.buttonX;
        r15 = 1110179840; // 0x422c0000 float:43.0 double:5.485017196E-315;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r14 = r14 + r15;
        r14 = (float) r14;
        r15 = r10.buttonY;
        r0 = 1110179840; // 0x422c0000 float:43.0 double:5.485017196E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r15 = r15 + r0;
        r15 = (float) r15;
        r11.set(r12, r13, r14, r15);
        r11 = r10.documentAttachType;
        r12 = 4;
        if (r11 == r12) goto L_0x0515;
    L_0x0512:
        r12 = 2;
        if (r11 != r12) goto L_0x054c;
    L_0x0515:
        r11 = r10.photoImage;
        r11 = r11.getImageX();
        r12 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r11 = r11 + r12;
        r10.videoButtonX = r11;
        r11 = r10.photoImage;
        r11 = r11.getImageY();
        r12 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r11 = r11 + r12;
        r10.videoButtonY = r11;
        r11 = r10.videoRadialProgress;
        r12 = r10.videoButtonX;
        r13 = r10.videoButtonY;
        r14 = 1103101952; // 0x41c00000 float:24.0 double:5.450047783E-315;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r14 = r14 + r12;
        r15 = r10.videoButtonY;
        r0 = 1103101952; // 0x41c00000 float:24.0 double:5.450047783E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r15 = r15 + r0;
        r11.setProgressRect(r12, r13, r14, r15);
    L_0x054c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.onLayout(boolean, int, int, int, int):void");
    }

    public boolean needDelayRoundProgressDraw() {
        int i = this.documentAttachType;
        return (i == 7 || i == 4) && this.currentMessageObject.type != 5 && MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
    }

    public void drawRoundProgress(Canvas canvas) {
        this.rect.set(((float) this.photoImage.getImageX()) + AndroidUtilities.dpf2(1.5f), ((float) this.photoImage.getImageY()) + AndroidUtilities.dpf2(1.5f), ((float) this.photoImage.getImageX2()) - AndroidUtilities.dpf2(1.5f), ((float) this.photoImage.getImageY2()) - AndroidUtilities.dpf2(1.5f));
        canvas.drawArc(this.rect, -90.0f, this.currentMessageObject.audioProgress * 360.0f, false, Theme.chat_radialProgressPaint);
    }

    private void updatePollAnimations() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - this.voteLastUpdateTime;
        if (j > 17) {
            j = 17;
        }
        this.voteLastUpdateTime = currentTimeMillis;
        if (this.pollVoteInProgress) {
            this.voteRadOffset += ((float) (360 * j)) / 2000.0f;
            float f = this.voteRadOffset;
            int i = 360;
            this.voteRadOffset = f - ((float) (((int) (f / 360.0f)) * 360));
            this.voteCurrentProgressTime += (float) j;
            if (this.voteCurrentProgressTime >= 500.0f) {
                this.voteCurrentProgressTime = 500.0f;
            }
            if (this.voteRisingCircleLength) {
                this.voteCurrentCircleLength = (AndroidUtilities.accelerateInterpolator.getInterpolation(this.voteCurrentProgressTime / 500.0f) * 266.0f) + 4.0f;
            } else {
                if (!this.firstCircleLength) {
                    i = 270;
                }
                this.voteCurrentCircleLength = 4.0f - (((float) i) * (1.0f - AndroidUtilities.decelerateInterpolator.getInterpolation(this.voteCurrentProgressTime / 500.0f)));
            }
            if (this.voteCurrentProgressTime == 500.0f) {
                if (this.voteRisingCircleLength) {
                    this.voteRadOffset += 270.0f;
                    this.voteCurrentCircleLength = -266.0f;
                }
                this.voteRisingCircleLength ^= 1;
                if (this.firstCircleLength) {
                    this.firstCircleLength = false;
                }
                this.voteCurrentProgressTime = 0.0f;
            }
            invalidate();
        }
        if (this.animatePollAnswer) {
            this.pollAnimationProgressTime += (float) j;
            if (this.pollAnimationProgressTime >= 300.0f) {
                this.pollAnimationProgressTime = 300.0f;
            }
            this.pollAnimationProgress = AndroidUtilities.decelerateInterpolator.getInterpolation(this.pollAnimationProgressTime / 300.0f);
            if (this.pollAnimationProgress >= 1.0f) {
                this.pollAnimationProgress = 1.0f;
                this.animatePollAnswer = false;
                this.animatePollAnswerAlpha = false;
                this.pollVoteInProgress = false;
                this.pollUnvoteInProgress = false;
            }
            invalidate();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:369:0x0abc  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x0fb7  */
    /* JADX WARNING: Removed duplicated region for block: B:559:0x0fb4  */
    private void drawContent(android.graphics.Canvas r29) {
        /*
        r28 = this;
        r1 = r28;
        r8 = r29;
        r0 = r1.needNewVisiblePart;
        r9 = 0;
        if (r0 == 0) goto L_0x0020;
    L_0x0009:
        r0 = r1.currentMessageObject;
        r0 = r0.type;
        if (r0 != 0) goto L_0x0020;
    L_0x000f:
        r0 = r1.scrollRect;
        r1.getLocalVisibleRect(r0);
        r0 = r1.scrollRect;
        r2 = r0.top;
        r0 = r0.bottom;
        r0 = r0 - r2;
        r1.setVisiblePart(r2, r0);
        r1.needNewVisiblePart = r9;
    L_0x0020:
        r0 = r1.currentMessagesGroup;
        r10 = 1;
        if (r0 == 0) goto L_0x0027;
    L_0x0025:
        r0 = 1;
        goto L_0x0028;
    L_0x0027:
        r0 = 0;
    L_0x0028:
        r1.forceNotDrawTime = r0;
        r0 = r1.photoImage;
        r2 = r1.currentMessageObject;
        r2 = org.telegram.p004ui.PhotoViewer.isShowingImage(r2);
        if (r2 != 0) goto L_0x0042;
    L_0x0034:
        r2 = org.telegram.p004ui.SecretMediaViewer.getInstance();
        r3 = r1.currentMessageObject;
        r2 = r2.isShowingImage(r3);
        if (r2 != 0) goto L_0x0042;
    L_0x0040:
        r2 = 1;
        goto L_0x0043;
    L_0x0042:
        r2 = 0;
    L_0x0043:
        r0.setVisible(r2, r9);
        r0 = r1.photoImage;
        r0 = r0.getVisible();
        r11 = 2;
        r12 = 0;
        r13 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        if (r0 != 0) goto L_0x0066;
    L_0x0052:
        r1.mediaWasInvisible = r10;
        r1.timeWasInvisible = r10;
        r0 = r1.animatingNoSound;
        if (r0 != r10) goto L_0x005f;
    L_0x005a:
        r1.animatingNoSoundProgress = r12;
        r1.animatingNoSound = r9;
        goto L_0x008f;
    L_0x005f:
        if (r0 != r11) goto L_0x008f;
    L_0x0061:
        r1.animatingNoSoundProgress = r13;
        r1.animatingNoSound = r9;
        goto L_0x008f;
    L_0x0066:
        r0 = r1.groupPhotoInvisible;
        if (r0 == 0) goto L_0x006d;
    L_0x006a:
        r1.timeWasInvisible = r10;
        goto L_0x008f;
    L_0x006d:
        r0 = r1.mediaWasInvisible;
        if (r0 != 0) goto L_0x0075;
    L_0x0071:
        r0 = r1.timeWasInvisible;
        if (r0 == 0) goto L_0x008f;
    L_0x0075:
        r0 = r1.mediaWasInvisible;
        if (r0 == 0) goto L_0x007d;
    L_0x0079:
        r1.controlsAlpha = r12;
        r1.mediaWasInvisible = r9;
    L_0x007d:
        r0 = r1.timeWasInvisible;
        if (r0 == 0) goto L_0x0085;
    L_0x0081:
        r1.timeAlpha = r12;
        r1.timeWasInvisible = r9;
    L_0x0085:
        r2 = java.lang.System.currentTimeMillis();
        r1.lastControlsAlphaChangeTime = r2;
        r2 = 0;
        r1.totalChangeTime = r2;
    L_0x008f:
        r0 = r1.radialProgress;
        r2 = "chat_mediaProgress";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setProgressColor(r2);
        r0 = r1.videoRadialProgress;
        r2 = "chat_mediaProgress";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setProgressColor(r2);
        r0 = r1.currentMessageObject;
        r2 = r0.type;
        r14 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r16 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r7 = 4;
        r17 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r18 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r19 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r20 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r21 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r22 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        if (r2 != 0) goto L_0x0934;
    L_0x00bc:
        r0 = r0.isOutOwner();
        if (r0 == 0) goto L_0x00d2;
    L_0x00c2:
        r0 = r1.currentBackgroundDrawable;
        r0 = r0.getBounds();
        r0 = r0.left;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r0 = r0 + r2;
        r1.textX = r0;
        goto L_0x00ee;
    L_0x00d2:
        r0 = r1.currentBackgroundDrawable;
        r0 = r0.getBounds();
        r0 = r0.left;
        r2 = r1.mediaBackground;
        if (r2 != 0) goto L_0x00e5;
    L_0x00de:
        r2 = r1.drawPinnedBottom;
        if (r2 == 0) goto L_0x00e5;
    L_0x00e2:
        r2 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        goto L_0x00e7;
    L_0x00e5:
        r2 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
    L_0x00e7:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 + r2;
        r1.textX = r0;
    L_0x00ee:
        r0 = r1.hasGamePreview;
        if (r0 == 0) goto L_0x0117;
    L_0x00f2:
        r0 = r1.textX;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r0 = r0 + r2;
        r1.textX = r0;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r2 = r1.namesOffset;
        r0 = r0 + r2;
        r1.textY = r0;
        r0 = r1.siteNameLayout;
        if (r0 == 0) goto L_0x0140;
    L_0x0108:
        r2 = r1.textY;
        r3 = r0.getLineCount();
        r3 = r3 - r10;
        r0 = r0.getLineBottom(r3);
        r2 = r2 + r0;
        r1.textY = r2;
        goto L_0x0140;
    L_0x0117:
        r0 = r1.hasInvoicePreview;
        if (r0 == 0) goto L_0x0137;
    L_0x011b:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r2 = r1.namesOffset;
        r0 = r0 + r2;
        r1.textY = r0;
        r0 = r1.siteNameLayout;
        if (r0 == 0) goto L_0x0140;
    L_0x0128:
        r2 = r1.textY;
        r3 = r0.getLineCount();
        r3 = r3 - r10;
        r0 = r0.getLineBottom(r3);
        r2 = r2 + r0;
        r1.textY = r2;
        goto L_0x0140;
    L_0x0137:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r2 = r1.namesOffset;
        r0 = r0 + r2;
        r1.textY = r0;
    L_0x0140:
        r0 = r1.textX;
        r1.unmovedTextX = r0;
        r0 = r1.currentMessageObject;
        r0 = r0.textXOffset;
        r0 = (r0 > r12 ? 1 : (r0 == r12 ? 0 : -1));
        if (r0 == 0) goto L_0x017d;
    L_0x014c:
        r0 = r1.replyNameLayout;
        if (r0 == 0) goto L_0x017d;
    L_0x0150:
        r0 = r1.backgroundWidth;
        r2 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        r2 = r1.currentMessageObject;
        r3 = r2.textWidth;
        r0 = r0 - r3;
        r3 = r1.hasNewLineForTime;
        if (r3 != 0) goto L_0x0176;
    L_0x0162:
        r3 = r1.timeWidth;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x016d;
    L_0x016a:
        r2 = 20;
        goto L_0x016e;
    L_0x016d:
        r2 = 0;
    L_0x016e:
        r2 = r2 + r7;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = r3 + r2;
        r0 = r0 - r3;
    L_0x0176:
        if (r0 <= 0) goto L_0x017d;
    L_0x0178:
        r2 = r1.textX;
        r2 = r2 + r0;
        r1.textX = r2;
    L_0x017d:
        r0 = r1.currentMessageObject;
        r0 = r0.textLayoutBlocks;
        if (r0 == 0) goto L_0x0231;
    L_0x0183:
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x0231;
    L_0x0189:
        r0 = r1.fullyDraw;
        if (r0 == 0) goto L_0x0199;
    L_0x018d:
        r1.firstVisibleBlockNum = r9;
        r0 = r1.currentMessageObject;
        r0 = r0.textLayoutBlocks;
        r0 = r0.size();
        r1.lastVisibleBlockNum = r0;
    L_0x0199:
        r0 = r1.firstVisibleBlockNum;
        if (r0 < 0) goto L_0x0231;
    L_0x019d:
        r2 = r0;
    L_0x019e:
        r0 = r1.lastVisibleBlockNum;
        if (r2 > r0) goto L_0x0231;
    L_0x01a2:
        r0 = r1.currentMessageObject;
        r0 = r0.textLayoutBlocks;
        r0 = r0.size();
        if (r2 < r0) goto L_0x01ae;
    L_0x01ac:
        goto L_0x0231;
    L_0x01ae:
        r0 = r1.currentMessageObject;
        r0 = r0.textLayoutBlocks;
        r0 = r0.get(r2);
        r0 = (org.telegram.messenger.MessageObject.TextLayoutBlock) r0;
        r29.save();
        r3 = r1.textX;
        r4 = r0.isRtl();
        if (r4 == 0) goto L_0x01ce;
    L_0x01c3:
        r4 = r1.currentMessageObject;
        r4 = r4.textXOffset;
        r4 = (double) r4;
        r4 = java.lang.Math.ceil(r4);
        r4 = (int) r4;
        goto L_0x01cf;
    L_0x01ce:
        r4 = 0;
    L_0x01cf:
        r3 = r3 - r4;
        r3 = (float) r3;
        r4 = r1.textY;
        r4 = (float) r4;
        r5 = r0.textYOffset;
        r4 = r4 + r5;
        r8.translate(r3, r4);
        r3 = r1.pressedLink;
        if (r3 == 0) goto L_0x01fb;
    L_0x01de:
        r3 = r1.linkBlockNum;
        if (r2 != r3) goto L_0x01fb;
    L_0x01e2:
        r3 = 0;
    L_0x01e3:
        r4 = r1.urlPath;
        r4 = r4.size();
        if (r3 >= r4) goto L_0x01fb;
    L_0x01eb:
        r4 = r1.urlPath;
        r4 = r4.get(r3);
        r4 = (android.graphics.Path) r4;
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_urlPaint;
        r8.drawPath(r4, r5);
        r3 = r3 + 1;
        goto L_0x01e3;
    L_0x01fb:
        r3 = r1.linkSelectionBlockNum;
        if (r2 != r3) goto L_0x0220;
    L_0x01ff:
        r3 = r1.urlPathSelection;
        r3 = r3.isEmpty();
        if (r3 != 0) goto L_0x0220;
    L_0x0207:
        r3 = 0;
    L_0x0208:
        r4 = r1.urlPathSelection;
        r4 = r4.size();
        if (r3 >= r4) goto L_0x0220;
    L_0x0210:
        r4 = r1.urlPathSelection;
        r4 = r4.get(r3);
        r4 = (android.graphics.Path) r4;
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_textSearchSelectionPaint;
        r8.drawPath(r4, r5);
        r3 = r3 + 1;
        goto L_0x0208;
    L_0x0220:
        r0 = r0.textLayout;	 Catch:{ Exception -> 0x0226 }
        r0.draw(r8);	 Catch:{ Exception -> 0x0226 }
        goto L_0x022a;
    L_0x0226:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x022a:
        r29.restore();
        r2 = r2 + 1;
        goto L_0x019e;
    L_0x0231:
        r0 = r1.hasLinkPreview;
        if (r0 != 0) goto L_0x023d;
    L_0x0235:
        r0 = r1.hasGamePreview;
        if (r0 != 0) goto L_0x023d;
    L_0x0239:
        r0 = r1.hasInvoicePreview;
        if (r0 == 0) goto L_0x0930;
    L_0x023d:
        r0 = r1.hasGamePreview;
        if (r0 == 0) goto L_0x0251;
    L_0x0241:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r2 = r1.namesOffset;
        r0 = r0 + r2;
        r2 = r1.unmovedTextX;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r2 = r2 - r3;
    L_0x024f:
        r6 = r2;
        goto L_0x0277;
    L_0x0251:
        r0 = r1.hasInvoicePreview;
        if (r0 == 0) goto L_0x0263;
    L_0x0255:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r2 = r1.namesOffset;
        r0 = r0 + r2;
        r2 = r1.unmovedTextX;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        goto L_0x0275;
    L_0x0263:
        r0 = r1.textY;
        r2 = r1.currentMessageObject;
        r2 = r2.textHeight;
        r0 = r0 + r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r0 = r0 + r2;
        r2 = r1.unmovedTextX;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
    L_0x0275:
        r2 = r2 + r3;
        goto L_0x024f;
    L_0x0277:
        r2 = r1.hasInvoicePreview;
        if (r2 != 0) goto L_0x02b9;
    L_0x027b:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_replyLinePaint;
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x0288;
    L_0x0285:
        r3 = "chat_outPreviewLine";
        goto L_0x028a;
    L_0x0288:
        r3 = "chat_inPreviewLine";
    L_0x028a:
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r3 = (float) r6;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r2 = r0 - r2;
        r4 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r2 = r2 + r6;
        r5 = (float) r2;
        r2 = r1.linkPreviewHeight;
        r2 = r2 + r0;
        r23 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r2 = r2 + r23;
        r2 = (float) r2;
        r23 = org.telegram.p004ui.ActionBar.Theme.chat_replyLinePaint;
        r24 = r2;
        r2 = r29;
        r9 = r6;
        r6 = r24;
        r15 = 4;
        r7 = r23;
        r2.drawRect(r3, r4, r5, r6, r7);
        goto L_0x02bb;
    L_0x02b9:
        r9 = r6;
        r15 = 4;
    L_0x02bb:
        r2 = r1.siteNameLayout;
        if (r2 == 0) goto L_0x0315;
    L_0x02bf:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x02cc;
    L_0x02c9:
        r3 = "chat_outSiteNameText";
        goto L_0x02ce;
    L_0x02cc:
        r3 = "chat_inSiteNameText";
    L_0x02ce:
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r29.save();
        r2 = r1.siteNameRtl;
        if (r2 == 0) goto L_0x02e9;
    L_0x02dc:
        r2 = r1.backgroundWidth;
        r3 = r1.siteNameWidth;
        r2 = r2 - r3;
        r3 = 1107296256; // 0x42000000 float:32.0 double:5.4707704E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r3;
        goto L_0x02f3;
    L_0x02e9:
        r2 = r1.hasInvoicePreview;
        if (r2 == 0) goto L_0x02ef;
    L_0x02ed:
        r2 = 0;
        goto L_0x02f3;
    L_0x02ef:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
    L_0x02f3:
        r6 = r9 + r2;
        r2 = (float) r6;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r3 = r0 - r3;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.siteNameLayout;
        r2.draw(r8);
        r29.restore();
        r2 = r1.siteNameLayout;
        r3 = r2.getLineCount();
        r3 = r3 - r10;
        r2 = r2.getLineBottom(r3);
        r2 = r2 + r0;
        goto L_0x0316;
    L_0x0315:
        r2 = r0;
    L_0x0316:
        r3 = r1.hasGamePreview;
        if (r3 != 0) goto L_0x031e;
    L_0x031a:
        r3 = r1.hasInvoicePreview;
        if (r3 == 0) goto L_0x0334;
    L_0x031e:
        r3 = r1.currentMessageObject;
        r3 = r3.textHeight;
        if (r3 == 0) goto L_0x0334;
    L_0x0324:
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r3 = r3 + r4;
        r0 = r0 + r3;
        r3 = r1.currentMessageObject;
        r3 = r3.textHeight;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r3 = r3 + r4;
        r2 = r2 + r3;
    L_0x0334:
        r3 = r1.drawPhotoImage;
        if (r3 == 0) goto L_0x033c;
    L_0x0338:
        r3 = r1.drawInstantView;
        if (r3 != 0) goto L_0x0345;
    L_0x033c:
        r3 = r1.drawInstantViewType;
        r4 = 6;
        if (r3 != r4) goto L_0x0488;
    L_0x0341:
        r3 = r1.imageBackgroundColor;
        if (r3 == 0) goto L_0x0488;
    L_0x0345:
        if (r2 == r0) goto L_0x034c;
    L_0x0347:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r2 = r2 + r3;
    L_0x034c:
        r7 = r2;
        r2 = r1.imageBackgroundSideColor;
        if (r2 == 0) goto L_0x03a2;
    L_0x0351:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r6 = r9 + r2;
        r2 = r1.photoImage;
        r3 = r1.imageBackgroundSideWidth;
        r4 = r2.getImageWidth();
        r3 = r3 - r4;
        r3 = r3 / r11;
        r3 = r3 + r6;
        r4 = r1.photoImage;
        r4 = r4.getImageWidth();
        r5 = r1.photoImage;
        r5 = r5.getImageHeight();
        r2.setImageCoords(r3, r7, r4, r5);
        r2 = r1.rect;
        r3 = (float) r6;
        r4 = r1.photoImage;
        r4 = r4.getImageY();
        r4 = (float) r4;
        r5 = r1.imageBackgroundSideWidth;
        r6 = r6 + r5;
        r5 = (float) r6;
        r6 = r1.photoImage;
        r6 = r6.getImageY2();
        r6 = (float) r6;
        r2.set(r3, r4, r5, r6);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r3 = r1.imageBackgroundSideColor;
        r2.setColor(r3);
        r2 = r1.rect;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r3 = (float) r3;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r4 = (float) r4;
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r8.drawRoundRect(r2, r3, r4, r5);
        goto L_0x03b9;
    L_0x03a2:
        r2 = r1.photoImage;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r6 = r9 + r3;
        r3 = r1.photoImage;
        r3 = r3.getImageWidth();
        r4 = r1.photoImage;
        r4 = r4.getImageHeight();
        r2.setImageCoords(r6, r7, r3, r4);
    L_0x03b9:
        r2 = r1.imageBackgroundColor;
        if (r2 == 0) goto L_0x0423;
    L_0x03bd:
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r3.setColor(r2);
        r2 = r1.rect;
        r3 = r1.photoImage;
        r3 = r3.getImageX();
        r3 = (float) r3;
        r4 = r1.photoImage;
        r4 = r4.getImageY();
        r4 = (float) r4;
        r5 = r1.photoImage;
        r5 = r5.getImageX2();
        r5 = (float) r5;
        r6 = r1.photoImage;
        r6 = r6.getImageY2();
        r6 = (float) r6;
        r2.set(r3, r4, r5, r6);
        r2 = r1.imageBackgroundSideColor;
        if (r2 == 0) goto L_0x040f;
    L_0x03e7:
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r3 = (float) r2;
        r2 = r1.photoImage;
        r2 = r2.getImageY();
        r4 = (float) r2;
        r2 = r1.photoImage;
        r2 = r2.getImageX2();
        r5 = (float) r2;
        r2 = r1.photoImage;
        r2 = r2.getImageY2();
        r6 = (float) r2;
        r23 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r2 = r29;
        r25 = r7;
        r7 = r23;
        r2.drawRect(r3, r4, r5, r6, r7);
        goto L_0x0425;
    L_0x040f:
        r25 = r7;
        r2 = r1.rect;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r3 = (float) r3;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r4 = (float) r4;
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r8.drawRoundRect(r2, r3, r4, r5);
        goto L_0x0425;
    L_0x0423:
        r25 = r7;
    L_0x0425:
        r2 = r1.drawPhotoImage;
        if (r2 == 0) goto L_0x0474;
    L_0x0429:
        r2 = r1.drawInstantView;
        if (r2 == 0) goto L_0x0474;
    L_0x042d:
        r2 = r1.drawImageButton;
        if (r2 == 0) goto L_0x046d;
    L_0x0431:
        r2 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = r1.photoImage;
        r3 = r3.getImageX();
        r3 = (float) r3;
        r4 = r1.photoImage;
        r4 = r4.getImageWidth();
        r4 = r4 - r2;
        r4 = (float) r4;
        r4 = r4 / r21;
        r3 = r3 + r4;
        r3 = (int) r3;
        r1.buttonX = r3;
        r3 = r1.photoImage;
        r3 = r3.getImageY();
        r3 = (float) r3;
        r4 = r1.photoImage;
        r4 = r4.getImageHeight();
        r4 = r4 - r2;
        r4 = (float) r4;
        r4 = r4 / r21;
        r3 = r3 + r4;
        r3 = (int) r3;
        r1.buttonY = r3;
        r3 = r1.radialProgress;
        r4 = r1.buttonX;
        r5 = r1.buttonY;
        r6 = r4 + r2;
        r2 = r2 + r5;
        r3.setProgressRect(r4, r5, r6, r2);
    L_0x046d:
        r2 = r1.photoImage;
        r2 = r2.draw(r8);
        goto L_0x0475;
    L_0x0474:
        r2 = 0;
    L_0x0475:
        r3 = r1.photoImage;
        r3 = r3.getImageHeight();
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r3 = r3 + r4;
        r3 = r25 + r3;
        r27 = r3;
        r3 = r2;
        r2 = r27;
        goto L_0x0489;
    L_0x0488:
        r3 = 0;
    L_0x0489:
        r4 = r1.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 == 0) goto L_0x04a8;
    L_0x0491:
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;
        r5 = "chat_messageTextOut";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r4.setColor(r5);
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;
        r5 = "chat_messageTextOut";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r4.setColor(r5);
        goto L_0x04be;
    L_0x04a8:
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;
        r5 = "chat_messageTextIn";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r4.setColor(r5);
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;
        r5 = "chat_messageTextIn";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r4.setColor(r5);
    L_0x04be:
        r4 = r1.titleLayout;
        if (r4 == 0) goto L_0x04fb;
    L_0x04c2:
        if (r2 == r0) goto L_0x04c9;
    L_0x04c4:
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r2 = r2 + r4;
    L_0x04c9:
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r4 = r2 - r4;
        r29.save();
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r6 = r9 + r5;
        r5 = r1.titleX;
        r6 = r6 + r5;
        r5 = (float) r6;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r6 = r2 - r6;
        r6 = (float) r6;
        r8.translate(r5, r6);
        r5 = r1.titleLayout;
        r5.draw(r8);
        r29.restore();
        r5 = r1.titleLayout;
        r6 = r5.getLineCount();
        r6 = r6 - r10;
        r5 = r5.getLineBottom(r6);
        r2 = r2 + r5;
        goto L_0x04fc;
    L_0x04fb:
        r4 = 0;
    L_0x04fc:
        r5 = r1.authorLayout;
        if (r5 == 0) goto L_0x053a;
    L_0x0500:
        if (r2 == r0) goto L_0x0507;
    L_0x0502:
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r2 = r2 + r5;
    L_0x0507:
        if (r4 != 0) goto L_0x050f;
    L_0x0509:
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r4 = r2 - r4;
    L_0x050f:
        r29.save();
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r6 = r9 + r5;
        r5 = r1.authorX;
        r6 = r6 + r5;
        r5 = (float) r6;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r6 = r2 - r6;
        r6 = (float) r6;
        r8.translate(r5, r6);
        r5 = r1.authorLayout;
        r5.draw(r8);
        r29.restore();
        r5 = r1.authorLayout;
        r6 = r5.getLineCount();
        r6 = r6 - r10;
        r5 = r5.getLineBottom(r6);
        r2 = r2 + r5;
    L_0x053a:
        r5 = r1.descriptionLayout;
        if (r5 == 0) goto L_0x05a5;
    L_0x053e:
        if (r2 == r0) goto L_0x0545;
    L_0x0540:
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r2 = r2 + r5;
    L_0x0545:
        if (r4 != 0) goto L_0x054d;
    L_0x0547:
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r4 = r2 - r4;
    L_0x054d:
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r5 = r2 - r5;
        r1.descriptionY = r5;
        r29.save();
        r5 = r1.hasInvoicePreview;
        if (r5 == 0) goto L_0x055e;
    L_0x055c:
        r5 = 0;
        goto L_0x0562;
    L_0x055e:
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
    L_0x0562:
        r6 = r9 + r5;
        r5 = r1.descriptionX;
        r6 = r6 + r5;
        r5 = (float) r6;
        r6 = r1.descriptionY;
        r6 = (float) r6;
        r8.translate(r5, r6);
        r5 = r1.pressedLink;
        if (r5 == 0) goto L_0x0591;
    L_0x0572:
        r5 = r1.linkBlockNum;
        r6 = -10;
        if (r5 != r6) goto L_0x0591;
    L_0x0578:
        r5 = 0;
    L_0x0579:
        r6 = r1.urlPath;
        r6 = r6.size();
        if (r5 >= r6) goto L_0x0591;
    L_0x0581:
        r6 = r1.urlPath;
        r6 = r6.get(r5);
        r6 = (android.graphics.Path) r6;
        r7 = org.telegram.p004ui.ActionBar.Theme.chat_urlPaint;
        r8.drawPath(r6, r7);
        r5 = r5 + 1;
        goto L_0x0579;
    L_0x0591:
        r5 = r1.descriptionLayout;
        r5.draw(r8);
        r29.restore();
        r5 = r1.descriptionLayout;
        r6 = r5.getLineCount();
        r6 = r6 - r10;
        r5 = r5.getLineBottom(r6);
        r2 = r2 + r5;
    L_0x05a5:
        r5 = r1.drawPhotoImage;
        if (r5 == 0) goto L_0x0661;
    L_0x05a9:
        r5 = r1.drawInstantView;
        if (r5 != 0) goto L_0x0661;
    L_0x05ad:
        if (r2 == r0) goto L_0x05b4;
    L_0x05af:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r2 = r2 + r3;
    L_0x05b4:
        r3 = r1.isSmallImage;
        if (r3 == 0) goto L_0x05d5;
    L_0x05b8:
        r3 = r1.photoImage;
        r5 = r1.backgroundWidth;
        r6 = r9 + r5;
        r5 = 1117913088; // 0x42a20000 float:81.0 double:5.52322452E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r6 = r6 - r5;
        r5 = r1.photoImage;
        r5 = r5.getImageWidth();
        r7 = r1.photoImage;
        r7 = r7.getImageHeight();
        r3.setImageCoords(r6, r4, r5, r7);
        goto L_0x0639;
    L_0x05d5:
        r3 = r1.photoImage;
        r4 = r1.hasInvoicePreview;
        if (r4 == 0) goto L_0x05e4;
    L_0x05db:
        r4 = 1086953882; // 0x40c9999a float:6.3 double:5.370265717E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r4 = -r4;
        goto L_0x05e8;
    L_0x05e4:
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
    L_0x05e8:
        r6 = r9 + r4;
        r4 = r1.photoImage;
        r4 = r4.getImageWidth();
        r5 = r1.photoImage;
        r5 = r5.getImageHeight();
        r3.setImageCoords(r6, r2, r4, r5);
        r3 = r1.drawImageButton;
        if (r3 == 0) goto L_0x0639;
    L_0x05fd:
        r3 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r4 = r1.photoImage;
        r4 = r4.getImageX();
        r4 = (float) r4;
        r5 = r1.photoImage;
        r5 = r5.getImageWidth();
        r5 = r5 - r3;
        r5 = (float) r5;
        r5 = r5 / r21;
        r4 = r4 + r5;
        r4 = (int) r4;
        r1.buttonX = r4;
        r4 = r1.photoImage;
        r4 = r4.getImageY();
        r4 = (float) r4;
        r5 = r1.photoImage;
        r5 = r5.getImageHeight();
        r5 = r5 - r3;
        r5 = (float) r5;
        r5 = r5 / r21;
        r4 = r4 + r5;
        r4 = (int) r4;
        r1.buttonY = r4;
        r4 = r1.radialProgress;
        r5 = r1.buttonX;
        r6 = r1.buttonY;
        r7 = r5 + r3;
        r3 = r3 + r6;
        r4.setProgressRect(r5, r6, r7, r3);
    L_0x0639:
        r3 = r1.currentMessageObject;
        r3 = r3.isRoundVideo();
        if (r3 == 0) goto L_0x065b;
    L_0x0641:
        r3 = org.telegram.messenger.MediaController.getInstance();
        r4 = r1.currentMessageObject;
        r3 = r3.isPlayingMessage(r4);
        if (r3 == 0) goto L_0x065b;
    L_0x064d:
        r3 = org.telegram.messenger.MediaController.getInstance();
        r3 = r3.isVideoDrawingReady();
        if (r3 == 0) goto L_0x065b;
    L_0x0657:
        r1.drawTime = r10;
        r3 = 1;
        goto L_0x0661;
    L_0x065b:
        r3 = r1.photoImage;
        r3 = r3.draw(r8);
    L_0x0661:
        r4 = r1.documentAttachType;
        if (r4 != r15) goto L_0x0699;
    L_0x0665:
        r4 = r1.photoImage;
        r4 = r4.getImageX();
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r4 = r4 + r5;
        r1.videoButtonX = r4;
        r4 = r1.photoImage;
        r4 = r4.getImageY();
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r4 = r4 + r5;
        r1.videoButtonY = r4;
        r4 = r1.videoRadialProgress;
        r5 = r1.videoButtonX;
        r6 = r1.videoButtonY;
        r7 = 1103101952; // 0x41c00000 float:24.0 double:5.450047783E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r7 = r7 + r5;
        r15 = r1.videoButtonY;
        r25 = 1103101952; // 0x41c00000 float:24.0 double:5.450047783E-315;
        r25 = org.telegram.messenger.AndroidUtilities.m26dp(r25);
        r15 = r15 + r25;
        r4.setProgressRect(r5, r6, r7, r15);
    L_0x0699:
        r4 = r1.photosCountLayout;
        if (r4 == 0) goto L_0x0740;
    L_0x069d:
        r4 = r1.photoImage;
        r4 = r4.getVisible();
        if (r4 == 0) goto L_0x0740;
    L_0x06a5:
        r4 = r1.photoImage;
        r4 = r4.getImageX();
        r5 = r1.photoImage;
        r5 = r5.getImageWidth();
        r4 = r4 + r5;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r4 = r4 - r5;
        r5 = r1.photosCountWidth;
        r4 = r4 - r5;
        r5 = r1.photoImage;
        r5 = r5.getImageY();
        r6 = r1.photoImage;
        r6 = r6.getImageHeight();
        r5 = r5 + r6;
        r6 = 1100480512; // 0x41980000 float:19.0 double:5.43709615E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 - r6;
        r6 = r1.rect;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r7 = r4 - r7;
        r7 = (float) r7;
        r15 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r15 = r5 - r15;
        r15 = (float) r15;
        r14 = r1.photosCountWidth;
        r14 = r14 + r4;
        r26 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r14 = r14 + r26;
        r14 = (float) r14;
        r26 = 1097334784; // 0x41680000 float:14.5 double:5.42155419E-315;
        r26 = org.telegram.messenger.AndroidUtilities.m26dp(r26);
        r12 = r5 + r26;
        r12 = (float) r12;
        r6.set(r7, r15, r14, r12);
        r6 = org.telegram.p004ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r6 = r6.getAlpha();
        r7 = org.telegram.p004ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r12 = (float) r6;
        r14 = r1.controlsAlpha;
        r12 = r12 * r14;
        r12 = (int) r12;
        r7.setAlpha(r12);
        r7 = org.telegram.p004ui.ActionBar.Theme.chat_durationPaint;
        r12 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r14 = r1.controlsAlpha;
        r14 = r14 * r12;
        r12 = (int) r14;
        r7.setAlpha(r12);
        r7 = r1.rect;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r12 = (float) r12;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r14 = (float) r14;
        r15 = org.telegram.p004ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r8.drawRoundRect(r7, r12, r14, r15);
        r7 = org.telegram.p004ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r7.setAlpha(r6);
        r29.save();
        r4 = (float) r4;
        r5 = (float) r5;
        r8.translate(r4, r5);
        r4 = r1.photosCountLayout;
        r4.draw(r8);
        r29.restore();
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_durationPaint;
        r5 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r4.setAlpha(r5);
    L_0x0740:
        r4 = r1.videoInfoLayout;
        if (r4 == 0) goto L_0x0867;
    L_0x0744:
        r4 = r1.drawPhotoImage;
        if (r4 == 0) goto L_0x0750;
    L_0x0748:
        r4 = r1.photoImage;
        r4 = r4.getVisible();
        if (r4 == 0) goto L_0x0867;
    L_0x0750:
        r4 = r1.imageBackgroundSideColor;
        if (r4 != 0) goto L_0x0867;
    L_0x0754:
        r4 = r1.hasGamePreview;
        if (r4 != 0) goto L_0x07c5;
    L_0x0758:
        r4 = r1.hasInvoicePreview;
        if (r4 != 0) goto L_0x07c5;
    L_0x075c:
        r4 = r1.documentAttachType;
        r5 = 8;
        if (r4 != r5) goto L_0x0763;
    L_0x0762:
        goto L_0x07c5;
    L_0x0763:
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r4 = r1.photoImage;
        r4 = r4.getImageWidth();
        r2 = r2 + r4;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r2 = r2 - r4;
        r4 = r1.durationWidth;
        r6 = r2 - r4;
        r2 = r1.photoImage;
        r2 = r2.getImageY();
        r4 = r1.photoImage;
        r4 = r4.getImageHeight();
        r2 = r2 + r4;
        r4 = 1100480512; // 0x41980000 float:19.0 double:5.43709615E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r2 = r2 - r4;
        r4 = r1.rect;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r5 = r6 - r5;
        r5 = (float) r5;
        r7 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r7 = r2 - r7;
        r7 = (float) r7;
        r12 = r1.durationWidth;
        r12 = r12 + r6;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r12 = r12 + r14;
        r12 = (float) r12;
        r14 = 1097334784; // 0x41680000 float:14.5 double:5.42155419E-315;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r14 = r14 + r2;
        r14 = (float) r14;
        r4.set(r5, r7, r12, r14);
        r4 = r1.rect;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r5 = (float) r5;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r7 = (float) r7;
        r12 = org.telegram.p004ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r8.drawRoundRect(r4, r5, r7, r12);
        goto L_0x0824;
    L_0x07c5:
        r4 = r1.drawPhotoImage;
        if (r4 == 0) goto L_0x0823;
    L_0x07c9:
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r4 = 1091043328; // 0x41080000 float:8.5 double:5.390470265E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = r2 + r4;
        r2 = r1.photoImage;
        r2 = r2.getImageY();
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r2 = r2 + r4;
        r4 = r1.documentAttachType;
        r5 = 8;
        if (r4 != r5) goto L_0x07eb;
    L_0x07e8:
        r4 = 1097334784; // 0x41680000 float:14.5 double:5.42155419E-315;
        goto L_0x07ed;
    L_0x07eb:
        r4 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
    L_0x07ed:
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r5 = r1.rect;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r7 = r6 - r7;
        r7 = (float) r7;
        r12 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r12 = r2 - r12;
        r12 = (float) r12;
        r14 = r1.durationWidth;
        r14 = r14 + r6;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r14 = r14 + r15;
        r14 = (float) r14;
        r4 = r4 + r2;
        r4 = (float) r4;
        r5.set(r7, r12, r14, r4);
        r4 = r1.rect;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r5 = (float) r5;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r7 = (float) r7;
        r12 = org.telegram.p004ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r8.drawRoundRect(r4, r5, r7, r12);
        goto L_0x0824;
    L_0x0823:
        r6 = r9;
    L_0x0824:
        r29.save();
        r4 = (float) r6;
        r2 = (float) r2;
        r8.translate(r4, r2);
        r2 = r1.hasInvoicePreview;
        if (r2 == 0) goto L_0x085f;
    L_0x0830:
        r2 = r1.drawPhotoImage;
        if (r2 == 0) goto L_0x0840;
    L_0x0834:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_shipmentPaint;
        r4 = "chat_previewGameText";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r2.setColor(r4);
        goto L_0x085f;
    L_0x0840:
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x0854;
    L_0x0848:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_shipmentPaint;
        r4 = "chat_messageTextOut";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r2.setColor(r4);
        goto L_0x085f;
    L_0x0854:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_shipmentPaint;
        r4 = "chat_messageTextIn";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r2.setColor(r4);
    L_0x085f:
        r2 = r1.videoInfoLayout;
        r2.draw(r8);
        r29.restore();
    L_0x0867:
        r2 = r1.drawInstantView;
        if (r2 == 0) goto L_0x092f;
    L_0x086b:
        r2 = r1.linkPreviewHeight;
        r0 = r0 + r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r0 = r0 + r2;
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewRectPaint;
        r4 = r1.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 == 0) goto L_0x0894;
    L_0x087d:
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_msgOutInstantDrawable;
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r6 = "chat_outPreviewInstantText";
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r5.setColor(r6);
        r5 = "chat_outPreviewInstantText";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r2.setColor(r5);
        goto L_0x08aa;
    L_0x0894:
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_msgInInstantDrawable;
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r6 = "chat_inPreviewInstantText";
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r5.setColor(r6);
        r5 = "chat_inPreviewInstantText";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r2.setColor(r5);
    L_0x08aa:
        r5 = android.os.Build.VERSION.SDK_INT;
        r6 = 21;
        if (r5 < r6) goto L_0x08c4;
    L_0x08b0:
        r5 = r1.selectorDrawable;
        r6 = r1.instantWidth;
        r6 = r6 + r9;
        r7 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r7 = r7 + r0;
        r5.setBounds(r9, r0, r6, r7);
        r5 = r1.selectorDrawable;
        r5.draw(r8);
    L_0x08c4:
        r5 = r1.rect;
        r6 = (float) r9;
        r7 = (float) r0;
        r12 = r1.instantWidth;
        r12 = r12 + r9;
        r12 = (float) r12;
        r14 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r14 = r14 + r0;
        r14 = (float) r14;
        r5.set(r6, r7, r12, r14);
        r5 = r1.rect;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r6 = (float) r6;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r7 = (float) r7;
        r8.drawRoundRect(r5, r6, r7, r2);
        r2 = r1.drawInstantViewType;
        if (r2 != 0) goto L_0x0910;
    L_0x08ea:
        r2 = r1.instantTextLeftX;
        r5 = r1.instantTextX;
        r2 = r2 + r5;
        r2 = r2 + r9;
        r5 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r2 = r2 - r5;
        r5 = 1094189056; // 0x41380000 float:11.5 double:5.406012226E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r5 = r5 + r0;
        r6 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r7 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r4, r2, r5, r6, r7);
        r4.draw(r8);
    L_0x0910:
        r2 = r1.instantViewLayout;
        if (r2 == 0) goto L_0x092f;
    L_0x0914:
        r29.save();
        r2 = r1.instantTextX;
        r6 = r9 + r2;
        r2 = (float) r6;
        r4 = 1093140480; // 0x41280000 float:10.5 double:5.40083157E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r0 = r0 + r4;
        r0 = (float) r0;
        r8.translate(r2, r0);
        r0 = r1.instantViewLayout;
        r0.draw(r8);
        r29.restore();
    L_0x092f:
        r9 = r3;
    L_0x0930:
        r1.drawTime = r10;
        goto L_0x0b13;
    L_0x0934:
        r2 = r1.drawPhotoImage;
        if (r2 == 0) goto L_0x0b12;
    L_0x0938:
        r0 = r0.isRoundVideo();
        if (r0 == 0) goto L_0x0959;
    L_0x093e:
        r0 = org.telegram.messenger.MediaController.getInstance();
        r2 = r1.currentMessageObject;
        r0 = r0.isPlayingMessage(r2);
        if (r0 == 0) goto L_0x0959;
    L_0x094a:
        r0 = org.telegram.messenger.MediaController.getInstance();
        r0 = r0.isVideoDrawingReady();
        if (r0 == 0) goto L_0x0959;
    L_0x0954:
        r1.drawTime = r10;
        r9 = 1;
        goto L_0x0b13;
    L_0x0959:
        r0 = r1.currentMessageObject;
        r0 = r0.type;
        r2 = 5;
        if (r0 != r2) goto L_0x09dc;
    L_0x0960:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_roundVideoShadow;
        if (r0 == 0) goto L_0x09dc;
    L_0x0964:
        r0 = r1.photoImage;
        r0 = r0.getImageX();
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r0 = r0 - r2;
        r2 = r1.photoImage;
        r2 = r2.getImageY();
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r2 = r2 - r3;
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_roundVideoShadow;
        r4 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r3.setAlpha(r4);
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_roundVideoShadow;
        r4 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r4 = r4 + r0;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r4 = r4 + r5;
        r5 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r5 = r5 + r2;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r5 = r5 + r6;
        r3.setBounds(r0, r2, r4, r5);
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_roundVideoShadow;
        r0.draw(r8);
        r0 = r1.photoImage;
        r0 = r0.hasBitmapImage();
        if (r0 == 0) goto L_0x09ad;
    L_0x09a3:
        r0 = r1.photoImage;
        r0 = r0.getCurrentAlpha();
        r0 = (r0 > r13 ? 1 : (r0 == r13 ? 0 : -1));
        if (r0 == 0) goto L_0x09dc;
    L_0x09ad:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x09ba;
    L_0x09b7:
        r2 = "chat_outBubble";
        goto L_0x09bc;
    L_0x09ba:
        r2 = "chat_inBubble";
    L_0x09bc:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = r1.photoImage;
        r0 = r0.getCenterX();
        r2 = r1.photoImage;
        r2 = r2.getCenterY();
        r3 = r1.photoImage;
        r3 = r3.getImageWidth();
        r3 = r3 / r11;
        r3 = (float) r3;
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r8.drawCircle(r0, r2, r3, r4);
    L_0x09dc:
        r0 = r1.photoCheckBox;
        if (r0 == 0) goto L_0x09ff;
    L_0x09e0:
        r2 = r1.checkBoxVisible;
        if (r2 != 0) goto L_0x09f1;
    L_0x09e4:
        r0 = r0.getProgress();
        r2 = 0;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 != 0) goto L_0x09f1;
    L_0x09ed:
        r0 = r1.checkBoxAnimationInProgress;
        if (r0 == 0) goto L_0x09ff;
    L_0x09f1:
        r0 = r1.currentMessagesGroup;
        if (r0 == 0) goto L_0x09ff;
    L_0x09f5:
        r0 = r0.messages;
        r0 = r0.size();
        if (r0 <= r10) goto L_0x09ff;
    L_0x09fd:
        r0 = 1;
        goto L_0x0a00;
    L_0x09ff:
        r0 = 0;
    L_0x0a00:
        r1.drawPhotoCheckBox = r0;
        r0 = r1.drawPhotoCheckBox;
        if (r0 == 0) goto L_0x0a96;
    L_0x0a06:
        r0 = r1.photoCheckBox;
        r0 = r0.isChecked();
        if (r0 != 0) goto L_0x0a1d;
    L_0x0a0e:
        r0 = r1.photoCheckBox;
        r0 = r0.getProgress();
        r2 = 0;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 != 0) goto L_0x0a1d;
    L_0x0a19:
        r0 = r1.checkBoxAnimationInProgress;
        if (r0 == 0) goto L_0x0a96;
    L_0x0a1d:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_replyLinePaint;
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x0a2a;
    L_0x0a27:
        r2 = "chat_outBubbleSelected";
        goto L_0x0a2c;
    L_0x0a2a:
        r2 = "chat_inBubbleSelected";
    L_0x0a2c:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = r1.rect;
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r2 = (float) r2;
        r3 = r1.photoImage;
        r3 = r3.getImageY();
        r3 = (float) r3;
        r4 = r1.photoImage;
        r4 = r4.getImageX2();
        r4 = (float) r4;
        r5 = r1.photoImage;
        r5 = r5.getImageY2();
        r5 = (float) r5;
        r0.set(r2, r3, r4, r5);
        r0 = r1.rect;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r2 = (float) r2;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r3 = (float) r3;
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_replyLinePaint;
        r8.drawRoundRect(r0, r2, r3, r4);
        r0 = r1.photoImage;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r3;
        r3 = r1.photoCheckBox;
        r3 = r3.getProgress();
        r2 = r2 * r3;
        r0.setSideClip(r2);
        r0 = r1.checkBoxAnimationInProgress;
        if (r0 == 0) goto L_0x0a85;
    L_0x0a7d:
        r0 = r1.photoCheckBox;
        r2 = r1.checkBoxAnimationProgress;
        r0.setBackgroundAlpha(r2);
        goto L_0x0a9c;
    L_0x0a85:
        r0 = r1.photoCheckBox;
        r2 = r1.checkBoxVisible;
        if (r2 == 0) goto L_0x0a8e;
    L_0x0a8b:
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x0a92;
    L_0x0a8e:
        r2 = r0.getProgress();
    L_0x0a92:
        r0.setBackgroundAlpha(r2);
        goto L_0x0a9c;
    L_0x0a96:
        r0 = r1.photoImage;
        r2 = 0;
        r0.setSideClip(r2);
    L_0x0a9c:
        r0 = r1.photoImage;
        r9 = r0.draw(r8);
        r0 = r1.drawTime;
        r2 = r1.photoImage;
        r2 = r2.getVisible();
        r1.drawTime = r2;
        r2 = r1.currentPosition;
        if (r2 == 0) goto L_0x0b13;
    L_0x0ab0:
        r2 = r1.drawTime;
        if (r0 == r2) goto L_0x0b13;
    L_0x0ab4:
        r0 = r28.getParent();
        r0 = (android.view.ViewGroup) r0;
        if (r0 == 0) goto L_0x0b13;
    L_0x0abc:
        r2 = r1.currentPosition;
        r2 = r2.last;
        if (r2 != 0) goto L_0x0b0e;
    L_0x0ac2:
        r2 = r0.getChildCount();
        r3 = 0;
    L_0x0ac7:
        if (r3 >= r2) goto L_0x0b13;
    L_0x0ac9:
        r4 = r0.getChildAt(r3);
        if (r4 == r1) goto L_0x0b0b;
    L_0x0acf:
        r5 = r4 instanceof org.telegram.p004ui.Cells.ChatMessageCell;
        if (r5 != 0) goto L_0x0ad4;
    L_0x0ad3:
        goto L_0x0b0b;
    L_0x0ad4:
        r4 = (org.telegram.p004ui.Cells.ChatMessageCell) r4;
        r5 = r4.getCurrentMessagesGroup();
        r6 = r1.currentMessagesGroup;
        if (r5 != r6) goto L_0x0b0b;
    L_0x0ade:
        r5 = r4.getCurrentPosition();
        r6 = r5.last;
        if (r6 == 0) goto L_0x0b0b;
    L_0x0ae6:
        r5 = r5.maxY;
        r6 = r1.currentPosition;
        r6 = r6.maxY;
        if (r5 != r6) goto L_0x0b0b;
    L_0x0aee:
        r5 = r4.timeX;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r5 = r5 - r6;
        r6 = r4.getLeft();
        r5 = r5 + r6;
        r6 = r28.getRight();
        if (r5 >= r6) goto L_0x0b0b;
    L_0x0b00:
        r5 = r1.drawTime;
        r5 = r5 ^ r10;
        r4.groupPhotoInvisible = r5;
        r4.invalidate();
        r0.invalidate();
    L_0x0b0b:
        r3 = r3 + 1;
        goto L_0x0ac7;
    L_0x0b0e:
        r0.invalidate();
        goto L_0x0b13;
    L_0x0b12:
        r9 = 0;
    L_0x0b13:
        r0 = r1.documentAttachType;
        if (r0 != r11) goto L_0x0b79;
    L_0x0b17:
        r0 = r1.photoImage;
        r0 = r0.getVisible();
        if (r0 == 0) goto L_0x0f37;
    L_0x0b1f:
        r0 = r1.hasGamePreview;
        if (r0 != 0) goto L_0x0f37;
    L_0x0b23:
        r0 = r1.currentMessageObject;
        r0 = r0.needDrawBluredPreview();
        if (r0 != 0) goto L_0x0f37;
    L_0x0b2b:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r0 = (android.graphics.drawable.BitmapDrawable) r0;
        r0 = r0.getPaint();
        r0 = r0.getAlpha();
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r3 = (float) r0;
        r4 = r1.controlsAlpha;
        r3 = r3 * r4;
        r3 = (int) r3;
        r2.setAlpha(r3);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r3 = r1.photoImage;
        r3 = r3.getImageX();
        r4 = r1.photoImage;
        r4 = r4.getImageWidth();
        r3 = r3 + r4;
        r4 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 - r4;
        r1.otherX = r3;
        r4 = r1.photoImage;
        r4 = r4.getImageY();
        r5 = 1090623898; // 0x4101999a float:8.1 double:5.388398005E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r4 = r4 + r5;
        r1.otherY = r4;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r2, r3, r4);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r2.draw(r8);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r2.setAlpha(r0);
        goto L_0x0f37;
    L_0x0b79:
        r2 = 7;
        if (r0 != r2) goto L_0x0ccf;
    L_0x0b7c:
        r0 = r1.durationLayout;
        if (r0 == 0) goto L_0x0f37;
    L_0x0b80:
        r0 = org.telegram.messenger.MediaController.getInstance();
        r2 = r1.currentMessageObject;
        r0 = r0.isPlayingMessage(r2);
        if (r0 == 0) goto L_0x0b99;
    L_0x0b8c:
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        r3 = 5;
        if (r2 != r3) goto L_0x0b99;
    L_0x0b93:
        r28.drawRoundProgress(r29);
        r28.drawOverlays(r29);
    L_0x0b99:
        r2 = r1.currentMessageObject;
        r3 = r2.type;
        r4 = 5;
        if (r3 != r4) goto L_0x0c78;
    L_0x0ba0:
        r2 = r1.backgroundDrawableLeft;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r2 = r2 + r3;
        r3 = r1.layoutHeight;
        r4 = r1.drawPinnedBottom;
        if (r4 == 0) goto L_0x0baf;
    L_0x0bad:
        r4 = 2;
        goto L_0x0bb0;
    L_0x0baf:
        r4 = 0;
    L_0x0bb0:
        r4 = 28 - r4;
        r4 = (float) r4;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 - r4;
        r4 = r1.rect;
        r5 = (float) r2;
        r6 = (float) r3;
        r7 = r1.timeWidthAudio;
        r7 = r7 + r2;
        r12 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r7 = r7 + r12;
        r7 = (float) r7;
        r12 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r14 = r14 + r3;
        r12 = (float) r14;
        r4.set(r5, r6, r7, r12);
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r4 = r4.getAlpha();
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r6 = (float) r4;
        r7 = r1.timeAlpha;
        r6 = r6 * r7;
        r6 = (int) r6;
        r5.setAlpha(r6);
        r5 = r1.rect;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r6 = (float) r6;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r7 = (float) r7;
        r12 = org.telegram.p004ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r8.drawRoundRect(r5, r6, r7, r12);
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r5.setAlpha(r4);
        if (r0 != 0) goto L_0x0c39;
    L_0x0bfb:
        r4 = r1.currentMessageObject;
        r4 = r4.isContentUnread();
        if (r4 == 0) goto L_0x0c39;
    L_0x0c03:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r4 = "chat_mediaTimeText";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r0.setColor(r4);
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r4 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r5 = r1.timeAlpha;
        r5 = r5 * r4;
        r4 = (int) r5;
        r0.setAlpha(r4);
        r0 = r1.timeWidthAudio;
        r0 = r0 + r2;
        r4 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r0 = r0 + r4;
        r0 = (float) r0;
        r4 = 1090833613; // 0x4104cccd float:8.3 double:5.389434135E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r4 = r4 + r3;
        r4 = (float) r4;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r5 = (float) r5;
        r6 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r8.drawCircle(r0, r4, r5, r6);
        goto L_0x0c6a;
    L_0x0c39:
        if (r0 == 0) goto L_0x0c4b;
    L_0x0c3b:
        r0 = org.telegram.messenger.MediaController.getInstance();
        r0 = r0.isMessagePaused();
        if (r0 != 0) goto L_0x0c4b;
    L_0x0c45:
        r0 = r1.roundVideoPlayingDrawable;
        r0.start();
        goto L_0x0c50;
    L_0x0c4b:
        r0 = r1.roundVideoPlayingDrawable;
        r0.stop();
    L_0x0c50:
        r0 = r1.roundVideoPlayingDrawable;
        r4 = r1.timeWidthAudio;
        r4 = r4 + r2;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r4 = r4 + r5;
        r5 = 1075000115; // 0x40133333 float:2.3 double:5.31120626E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r5 = r5 + r3;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r4, r5);
        r0 = r1.roundVideoPlayingDrawable;
        r0.draw(r8);
    L_0x0c6a:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r2 = r2 + r0;
        r0 = 1071225242; // 0x3fd9999a float:1.7 double:5.29255591E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r3 = r3 + r0;
        goto L_0x0caa;
    L_0x0c78:
        r0 = r1.backgroundDrawableLeft;
        r2 = r2.isOutOwner();
        if (r2 != 0) goto L_0x0c88;
    L_0x0c80:
        r2 = r1.drawPinnedBottom;
        if (r2 == 0) goto L_0x0c85;
    L_0x0c84:
        goto L_0x0c88;
    L_0x0c85:
        r2 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        goto L_0x0c8a;
    L_0x0c88:
        r2 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
    L_0x0c8a:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = r2 + r0;
        r0 = r1.layoutHeight;
        r3 = 1086953882; // 0x40c9999a float:6.3 double:5.370265717E-315;
        r4 = r1.drawPinnedBottom;
        if (r4 == 0) goto L_0x0c9a;
    L_0x0c98:
        r4 = 2;
        goto L_0x0c9b;
    L_0x0c9a:
        r4 = 0;
    L_0x0c9b:
        r4 = (float) r4;
        r3 = r3 - r4;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r3 = r1.timeLayout;
        r3 = r3.getHeight();
        r3 = r0 - r3;
    L_0x0caa:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_timePaint;
        r4 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r5 = r1.timeAlpha;
        r5 = r5 * r4;
        r4 = (int) r5;
        r0.setAlpha(r4);
        r29.save();
        r0 = (float) r2;
        r2 = (float) r3;
        r8.translate(r0, r2);
        r0 = r1.durationLayout;
        r0.draw(r8);
        r29.restore();
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_timePaint;
        r2 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r0.setAlpha(r2);
        goto L_0x0f37;
    L_0x0ccf:
        r2 = 5;
        if (r0 != r2) goto L_0x0e3c;
    L_0x0cd2:
        r0 = r1.currentMessageObject;
        r0 = r0.isOutOwner();
        if (r0 == 0) goto L_0x0d27;
    L_0x0cda:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_audioTitlePaint;
        r2 = "chat_outAudioTitleText";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_audioPerformerPaint;
        r2 = r28.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x0cf0;
    L_0x0ced:
        r2 = "chat_outAudioPerfomerSelectedText";
        goto L_0x0cf2;
    L_0x0cf0:
        r2 = "chat_outAudioPerfomerText";
    L_0x0cf2:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_audioTimePaint;
        r2 = r28.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x0d04;
    L_0x0d01:
        r2 = "chat_outAudioDurationSelectedText";
        goto L_0x0d06;
    L_0x0d04:
        r2 = "chat_outAudioDurationText";
    L_0x0d06:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = r1.radialProgress;
        r2 = r28.isDrawSelectionBackground();
        if (r2 != 0) goto L_0x0d1d;
    L_0x0d15:
        r2 = r1.buttonPressed;
        if (r2 == 0) goto L_0x0d1a;
    L_0x0d19:
        goto L_0x0d1d;
    L_0x0d1a:
        r2 = "chat_outAudioProgress";
        goto L_0x0d1f;
    L_0x0d1d:
        r2 = "chat_outAudioSelectedProgress";
    L_0x0d1f:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setProgressColor(r2);
        goto L_0x0d73;
    L_0x0d27:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_audioTitlePaint;
        r2 = "chat_inAudioTitleText";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_audioPerformerPaint;
        r2 = r28.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x0d3d;
    L_0x0d3a:
        r2 = "chat_inAudioPerfomerSelectedText";
        goto L_0x0d3f;
    L_0x0d3d:
        r2 = "chat_inAudioPerfomerText";
    L_0x0d3f:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_audioTimePaint;
        r2 = r28.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x0d51;
    L_0x0d4e:
        r2 = "chat_inAudioDurationSelectedText";
        goto L_0x0d53;
    L_0x0d51:
        r2 = "chat_inAudioDurationText";
    L_0x0d53:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = r1.radialProgress;
        r2 = r28.isDrawSelectionBackground();
        if (r2 != 0) goto L_0x0d6a;
    L_0x0d62:
        r2 = r1.buttonPressed;
        if (r2 == 0) goto L_0x0d67;
    L_0x0d66:
        goto L_0x0d6a;
    L_0x0d67:
        r2 = "chat_inAudioProgress";
        goto L_0x0d6c;
    L_0x0d6a:
        r2 = "chat_inAudioSelectedProgress";
    L_0x0d6c:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setProgressColor(r2);
    L_0x0d73:
        r0 = r1.radialProgress;
        r0.draw(r8);
        r29.save();
        r0 = r1.timeAudioX;
        r2 = r1.songX;
        r0 = r0 + r2;
        r0 = (float) r0;
        r2 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = r1.namesOffset;
        r2 = r2 + r3;
        r3 = r1.mediaOffsetY;
        r2 = r2 + r3;
        r2 = (float) r2;
        r8.translate(r0, r2);
        r0 = r1.songLayout;
        r0.draw(r8);
        r29.restore();
        r29.save();
        r0 = org.telegram.messenger.MediaController.getInstance();
        r2 = r1.currentMessageObject;
        r0 = r0.isPlayingMessage(r2);
        if (r0 == 0) goto L_0x0db7;
    L_0x0da8:
        r0 = r1.seekBarX;
        r0 = (float) r0;
        r2 = r1.seekBarY;
        r2 = (float) r2;
        r8.translate(r0, r2);
        r0 = r1.seekBar;
        r0.draw(r8);
        goto L_0x0dd2;
    L_0x0db7:
        r0 = r1.timeAudioX;
        r2 = r1.performerX;
        r0 = r0 + r2;
        r0 = (float) r0;
        r2 = 1108082688; // 0x420c0000 float:35.0 double:5.47465589E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = r1.namesOffset;
        r2 = r2 + r3;
        r3 = r1.mediaOffsetY;
        r2 = r2 + r3;
        r2 = (float) r2;
        r8.translate(r0, r2);
        r0 = r1.performerLayout;
        r0.draw(r8);
    L_0x0dd2:
        r29.restore();
        r29.save();
        r0 = r1.timeAudioX;
        r0 = (float) r0;
        r2 = 1113849856; // 0x42640000 float:57.0 double:5.503149485E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = r1.namesOffset;
        r2 = r2 + r3;
        r3 = r1.mediaOffsetY;
        r2 = r2 + r3;
        r2 = (float) r2;
        r8.translate(r0, r2);
        r0 = r1.durationLayout;
        r0.draw(r8);
        r29.restore();
        r0 = r1.currentMessageObject;
        r0 = r0.isOutOwner();
        if (r0 == 0) goto L_0x0e07;
    L_0x0dfb:
        r0 = r28.isDrawSelectionBackground();
        if (r0 == 0) goto L_0x0e04;
    L_0x0e01:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_msgOutMenuSelectedDrawable;
        goto L_0x0e12;
    L_0x0e04:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_msgOutMenuDrawable;
        goto L_0x0e12;
    L_0x0e07:
        r0 = r28.isDrawSelectionBackground();
        if (r0 == 0) goto L_0x0e10;
    L_0x0e0d:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_msgInMenuSelectedDrawable;
        goto L_0x0e12;
    L_0x0e10:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_msgInMenuDrawable;
    L_0x0e12:
        r2 = r1.buttonX;
        r3 = r1.backgroundWidth;
        r2 = r2 + r3;
        r3 = r1.currentMessageObject;
        r3 = r3.type;
        if (r3 != 0) goto L_0x0e20;
    L_0x0e1d:
        r3 = 1114112000; // 0x42680000 float:58.0 double:5.50444465E-315;
        goto L_0x0e22;
    L_0x0e20:
        r3 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
    L_0x0e22:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r3;
        r1.otherX = r2;
        r3 = r1.buttonY;
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 - r4;
        r1.otherY = r3;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r2, r3);
        r0.draw(r8);
        goto L_0x0f37;
    L_0x0e3c:
        r2 = 3;
        if (r0 != r2) goto L_0x0f37;
    L_0x0e3f:
        r0 = r1.currentMessageObject;
        r0 = r0.isOutOwner();
        if (r0 == 0) goto L_0x0e75;
    L_0x0e47:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_audioTimePaint;
        r2 = r28.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x0e52;
    L_0x0e4f:
        r2 = "chat_outAudioDurationSelectedText";
        goto L_0x0e54;
    L_0x0e52:
        r2 = "chat_outAudioDurationText";
    L_0x0e54:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = r1.radialProgress;
        r2 = r28.isDrawSelectionBackground();
        if (r2 != 0) goto L_0x0e6b;
    L_0x0e63:
        r2 = r1.buttonPressed;
        if (r2 == 0) goto L_0x0e68;
    L_0x0e67:
        goto L_0x0e6b;
    L_0x0e68:
        r2 = "chat_outAudioProgress";
        goto L_0x0e6d;
    L_0x0e6b:
        r2 = "chat_outAudioSelectedProgress";
    L_0x0e6d:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setProgressColor(r2);
        goto L_0x0ea2;
    L_0x0e75:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_audioTimePaint;
        r2 = r28.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x0e80;
    L_0x0e7d:
        r2 = "chat_inAudioDurationSelectedText";
        goto L_0x0e82;
    L_0x0e80:
        r2 = "chat_inAudioDurationText";
    L_0x0e82:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = r1.radialProgress;
        r2 = r28.isDrawSelectionBackground();
        if (r2 != 0) goto L_0x0e99;
    L_0x0e91:
        r2 = r1.buttonPressed;
        if (r2 == 0) goto L_0x0e96;
    L_0x0e95:
        goto L_0x0e99;
    L_0x0e96:
        r2 = "chat_inAudioProgress";
        goto L_0x0e9b;
    L_0x0e99:
        r2 = "chat_inAudioSelectedProgress";
    L_0x0e9b:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setProgressColor(r2);
    L_0x0ea2:
        r0 = r1.radialProgress;
        r0.draw(r8);
        r29.save();
        r0 = r1.useSeekBarWaweform;
        if (r0 == 0) goto L_0x0ec4;
    L_0x0eae:
        r0 = r1.seekBarX;
        r2 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 + r2;
        r0 = (float) r0;
        r2 = r1.seekBarY;
        r2 = (float) r2;
        r8.translate(r0, r2);
        r0 = r1.seekBarWaveform;
        r0.draw(r8);
        goto L_0x0ed2;
    L_0x0ec4:
        r0 = r1.seekBarX;
        r0 = (float) r0;
        r2 = r1.seekBarY;
        r2 = (float) r2;
        r8.translate(r0, r2);
        r0 = r1.seekBar;
        r0.draw(r8);
    L_0x0ed2:
        r29.restore();
        r29.save();
        r0 = r1.timeAudioX;
        r0 = (float) r0;
        r2 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = r1.namesOffset;
        r2 = r2 + r3;
        r3 = r1.mediaOffsetY;
        r2 = r2 + r3;
        r2 = (float) r2;
        r8.translate(r0, r2);
        r0 = r1.durationLayout;
        r0.draw(r8);
        r29.restore();
        r0 = r1.currentMessageObject;
        r2 = r0.type;
        if (r2 == 0) goto L_0x0f37;
    L_0x0ef9:
        r0 = r0.isContentUnread();
        if (r0 == 0) goto L_0x0f37;
    L_0x0eff:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x0f0c;
    L_0x0f09:
        r2 = "chat_outVoiceSeekbarFill";
        goto L_0x0f0e;
    L_0x0f0c:
        r2 = "chat_inVoiceSeekbarFill";
    L_0x0f0e:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = r1.timeAudioX;
        r2 = r1.timeWidthAudio;
        r0 = r0 + r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r0 = r0 + r2;
        r0 = (float) r0;
        r2 = 1112276992; // 0x424c0000 float:51.0 double:5.495378504E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = r1.namesOffset;
        r2 = r2 + r3;
        r3 = r1.mediaOffsetY;
        r2 = r2 + r3;
        r2 = (float) r2;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r3 = (float) r3;
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r8.drawCircle(r0, r2, r3, r4);
    L_0x0f37:
        r0 = r1.captionLayout;
        if (r0 == 0) goto L_0x0fea;
    L_0x0f3b:
        r0 = r1.currentMessageObject;
        r2 = r0.type;
        if (r2 == r10) goto L_0x0fc1;
    L_0x0f41:
        r3 = r1.documentAttachType;
        r4 = 4;
        if (r3 == r4) goto L_0x0fc1;
    L_0x0f46:
        r3 = 8;
        if (r2 != r3) goto L_0x0f4c;
    L_0x0f4a:
        goto L_0x0fc1;
    L_0x0f4c:
        r2 = r1.hasOldCaptionPreview;
        if (r2 == 0) goto L_0x0f87;
    L_0x0f50:
        r2 = r1.backgroundDrawableLeft;
        r0 = r0.isOutOwner();
        if (r0 == 0) goto L_0x0f5b;
    L_0x0f58:
        r0 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        goto L_0x0f5d;
    L_0x0f5b:
        r0 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
    L_0x0f5d:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r2 = r2 + r0;
        r0 = r1.captionOffsetX;
        r2 = r2 + r0;
        r1.captionX = r2;
        r0 = r1.totalHeight;
        r2 = r1.captionHeight;
        r0 = r0 - r2;
        r2 = r1.drawPinnedTop;
        if (r2 == 0) goto L_0x0f73;
    L_0x0f70:
        r2 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        goto L_0x0f75;
    L_0x0f73:
        r2 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
    L_0x0f75:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        r2 = r1.linkPreviewHeight;
        r0 = r0 - r2;
        r12 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r0 = r0 - r2;
        r1.captionY = r0;
        goto L_0x0fec;
    L_0x0f87:
        r12 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r2 = r1.backgroundDrawableLeft;
        r0 = r0.isOutOwner();
        if (r0 != 0) goto L_0x0f9f;
    L_0x0f91:
        r0 = r1.mediaBackground;
        if (r0 != 0) goto L_0x0f9f;
    L_0x0f95:
        if (r0 != 0) goto L_0x0f9c;
    L_0x0f97:
        r0 = r1.drawPinnedBottom;
        if (r0 == 0) goto L_0x0f9c;
    L_0x0f9b:
        goto L_0x0f9f;
    L_0x0f9c:
        r0 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        goto L_0x0fa1;
    L_0x0f9f:
        r0 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
    L_0x0fa1:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r2 = r2 + r0;
        r0 = r1.captionOffsetX;
        r2 = r2 + r0;
        r1.captionX = r2;
        r0 = r1.totalHeight;
        r2 = r1.captionHeight;
        r0 = r0 - r2;
        r2 = r1.drawPinnedTop;
        if (r2 == 0) goto L_0x0fb7;
    L_0x0fb4:
        r2 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        goto L_0x0fb9;
    L_0x0fb7:
        r2 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
    L_0x0fb9:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        r1.captionY = r0;
        goto L_0x0fec;
    L_0x0fc1:
        r12 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r0 = r1.photoImage;
        r0 = r0.getImageX();
        r2 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 + r2;
        r2 = r1.captionOffsetX;
        r0 = r0 + r2;
        r1.captionX = r0;
        r0 = r1.photoImage;
        r0 = r0.getImageY();
        r2 = r1.photoImage;
        r2 = r2.getImageHeight();
        r0 = r0 + r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r0 = r0 + r2;
        r1.captionY = r0;
        goto L_0x0fec;
    L_0x0fea:
        r12 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
    L_0x0fec:
        r0 = r1.currentPosition;
        if (r0 != 0) goto L_0x0ff5;
    L_0x0ff0:
        r14 = 0;
        r1.drawCaptionLayout(r8, r14);
        goto L_0x0ff6;
    L_0x0ff5:
        r14 = 0;
    L_0x0ff6:
        r0 = r1.hasOldCaptionPreview;
        if (r0 == 0) goto L_0x1118;
    L_0x0ffa:
        r0 = r1.currentMessageObject;
        r2 = r0.type;
        if (r2 == r10) goto L_0x101b;
    L_0x1000:
        r3 = r1.documentAttachType;
        r4 = 4;
        if (r3 == r4) goto L_0x101b;
    L_0x1005:
        r3 = 8;
        if (r2 != r3) goto L_0x100a;
    L_0x1009:
        goto L_0x101b;
    L_0x100a:
        r2 = r1.backgroundDrawableLeft;
        r0 = r0.isOutOwner();
        if (r0 == 0) goto L_0x1013;
    L_0x1012:
        goto L_0x1015;
    L_0x1013:
        r16 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
    L_0x1015:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r2 = r2 + r0;
        goto L_0x1028;
    L_0x101b:
        r0 = r1.photoImage;
        r0 = r0.getImageX();
        r2 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = r2 + r0;
    L_0x1028:
        r0 = r2;
        r2 = r1.totalHeight;
        r3 = r1.drawPinnedTop;
        if (r3 == 0) goto L_0x1032;
    L_0x102f:
        r3 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        goto L_0x1034;
    L_0x1032:
        r3 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
    L_0x1034:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r3;
        r3 = r1.linkPreviewHeight;
        r2 = r2 - r3;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r12 = r2 - r3;
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_replyLinePaint;
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x104f;
    L_0x104c:
        r3 = "chat_outPreviewLine";
        goto L_0x1051;
    L_0x104f:
        r3 = "chat_inPreviewLine";
    L_0x1051:
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r3 = (float) r0;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r2 = r12 - r2;
        r4 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r2 = r2 + r0;
        r5 = (float) r2;
        r2 = r1.linkPreviewHeight;
        r2 = r2 + r12;
        r6 = (float) r2;
        r7 = org.telegram.p004ui.ActionBar.Theme.chat_replyLinePaint;
        r2 = r29;
        r2.drawRect(r3, r4, r5, r6, r7);
        r2 = r1.siteNameLayout;
        if (r2 == 0) goto L_0x10ca;
    L_0x1075:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x1082;
    L_0x107f:
        r3 = "chat_outSiteNameText";
        goto L_0x1084;
    L_0x1082:
        r3 = "chat_inSiteNameText";
    L_0x1084:
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r29.save();
        r2 = r1.siteNameRtl;
        if (r2 == 0) goto L_0x109f;
    L_0x1092:
        r2 = r1.backgroundWidth;
        r3 = r1.siteNameWidth;
        r2 = r2 - r3;
        r3 = 1107296256; // 0x42000000 float:32.0 double:5.4707704E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r3;
        goto L_0x10a9;
    L_0x109f:
        r2 = r1.hasInvoicePreview;
        if (r2 == 0) goto L_0x10a5;
    L_0x10a3:
        r2 = 0;
        goto L_0x10a9;
    L_0x10a5:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
    L_0x10a9:
        r2 = r2 + r0;
        r2 = (float) r2;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r3 = r12 - r3;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.siteNameLayout;
        r2.draw(r8);
        r29.restore();
        r2 = r1.siteNameLayout;
        r3 = r2.getLineCount();
        r3 = r3 - r10;
        r2 = r2.getLineBottom(r3);
        r2 = r2 + r12;
        goto L_0x10cb;
    L_0x10ca:
        r2 = r12;
    L_0x10cb:
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x10df;
    L_0x10d3:
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;
        r4 = "chat_messageTextOut";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r3.setColor(r4);
        goto L_0x10ea;
    L_0x10df:
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;
        r4 = "chat_messageTextIn";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r3.setColor(r4);
    L_0x10ea:
        r3 = r1.descriptionLayout;
        if (r3 == 0) goto L_0x1116;
    L_0x10ee:
        if (r2 == r12) goto L_0x10f5;
    L_0x10f0:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r2 = r2 + r3;
    L_0x10f5:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r2 = r2 - r3;
        r1.descriptionY = r2;
        r29.save();
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r0 = r0 + r2;
        r2 = r1.descriptionX;
        r0 = r0 + r2;
        r0 = (float) r0;
        r2 = r1.descriptionY;
        r2 = (float) r2;
        r8.translate(r0, r2);
        r0 = r1.descriptionLayout;
        r0.draw(r8);
        r29.restore();
    L_0x1116:
        r1.drawTime = r10;
    L_0x1118:
        r0 = r1.documentAttachType;
        if (r0 != r10) goto L_0x1445;
    L_0x111c:
        r0 = r1.currentMessageObject;
        r0 = r0.isOutOwner();
        if (r0 == 0) goto L_0x1163;
    L_0x1124:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_docNamePaint;
        r2 = "chat_outFileNameText";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_infoPaint;
        r2 = r28.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x113a;
    L_0x1137:
        r2 = "chat_outFileInfoSelectedText";
        goto L_0x113c;
    L_0x113a:
        r2 = "chat_outFileInfoText";
    L_0x113c:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r2 = r28.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x114e;
    L_0x114b:
        r2 = "chat_outFileBackgroundSelected";
        goto L_0x1150;
    L_0x114e:
        r2 = "chat_outFileBackground";
    L_0x1150:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = r28.isDrawSelectionBackground();
        if (r0 == 0) goto L_0x1160;
    L_0x115d:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_msgOutMenuSelectedDrawable;
        goto L_0x11a1;
    L_0x1160:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_msgOutMenuDrawable;
        goto L_0x11a1;
    L_0x1163:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_docNamePaint;
        r2 = "chat_inFileNameText";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_infoPaint;
        r2 = r28.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x1179;
    L_0x1176:
        r2 = "chat_inFileInfoSelectedText";
        goto L_0x117b;
    L_0x1179:
        r2 = "chat_inFileInfoText";
    L_0x117b:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r2 = r28.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x118d;
    L_0x118a:
        r2 = "chat_inFileBackgroundSelected";
        goto L_0x118f;
    L_0x118d:
        r2 = "chat_inFileBackground";
    L_0x118f:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = r28.isDrawSelectionBackground();
        if (r0 == 0) goto L_0x119f;
    L_0x119c:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_msgInMenuSelectedDrawable;
        goto L_0x11a1;
    L_0x119f:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_msgInMenuDrawable;
    L_0x11a1:
        r2 = r1.drawPhotoImage;
        if (r2 == 0) goto L_0x1343;
    L_0x11a5:
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        if (r2 != 0) goto L_0x11ce;
    L_0x11ab:
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r3 = r1.backgroundWidth;
        r2 = r2 + r3;
        r3 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r3;
        r1.otherX = r2;
        r3 = r1.photoImage;
        r3 = r3.getImageY();
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r3 = r3 + r4;
        r1.otherY = r3;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r2, r3);
        goto L_0x11f0;
    L_0x11ce:
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r3 = r1.backgroundWidth;
        r2 = r2 + r3;
        r3 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r3;
        r1.otherX = r2;
        r3 = r1.photoImage;
        r3 = r3.getImageY();
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r3 = r3 + r4;
        r1.otherY = r3;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r2, r3);
    L_0x11f0:
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r3 = r1.photoImage;
        r3 = r3.getImageWidth();
        r2 = r2 + r3;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r2 = r2 + r3;
        r3 = r1.photoImage;
        r3 = r3.getImageY();
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r3 = r3 + r4;
        r4 = r1.photoImage;
        r4 = r4.getImageY();
        r5 = r1.docTitleLayout;
        if (r5 == 0) goto L_0x1228;
    L_0x1217:
        r6 = r5.getLineCount();
        r6 = r6 - r10;
        r5 = r5.getLineBottom(r6);
        r6 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 + r6;
        goto L_0x122c;
    L_0x1228:
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
    L_0x122c:
        r4 = r4 + r5;
        if (r9 != 0) goto L_0x12fe;
    L_0x122f:
        r5 = r1.currentMessageObject;
        r5 = r5.isOutOwner();
        if (r5 == 0) goto L_0x127a;
    L_0x1237:
        r5 = r1.radialProgress;
        r6 = "chat_outLoader";
        r7 = "chat_outLoaderSelected";
        r9 = "chat_outMediaIcon";
        r12 = "chat_outMediaIconSelected";
        r5.setColors(r6, r7, r9, r12);
        r5 = r1.radialProgress;
        r6 = r28.isDrawSelectionBackground();
        if (r6 == 0) goto L_0x124f;
    L_0x124c:
        r6 = "chat_outFileProgressSelected";
        goto L_0x1251;
    L_0x124f:
        r6 = "chat_outFileProgress";
    L_0x1251:
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r5.setProgressColor(r6);
        r5 = r1.videoRadialProgress;
        r6 = "chat_outLoader";
        r7 = "chat_outLoaderSelected";
        r9 = "chat_outMediaIcon";
        r12 = "chat_outMediaIconSelected";
        r5.setColors(r6, r7, r9, r12);
        r5 = r1.videoRadialProgress;
        r6 = r28.isDrawSelectionBackground();
        if (r6 == 0) goto L_0x1270;
    L_0x126d:
        r6 = "chat_outFileProgressSelected";
        goto L_0x1272;
    L_0x1270:
        r6 = "chat_outFileProgress";
    L_0x1272:
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r5.setProgressColor(r6);
        goto L_0x12bc;
    L_0x127a:
        r5 = r1.radialProgress;
        r6 = "chat_inLoader";
        r7 = "chat_inLoaderSelected";
        r9 = "chat_inMediaIcon";
        r12 = "chat_inMediaIconSelected";
        r5.setColors(r6, r7, r9, r12);
        r5 = r1.radialProgress;
        r6 = r28.isDrawSelectionBackground();
        if (r6 == 0) goto L_0x1292;
    L_0x128f:
        r6 = "chat_inFileProgressSelected";
        goto L_0x1294;
    L_0x1292:
        r6 = "chat_inFileProgress";
    L_0x1294:
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r5.setProgressColor(r6);
        r5 = r1.videoRadialProgress;
        r6 = "chat_inLoader";
        r7 = "chat_inLoaderSelected";
        r9 = "chat_inMediaIcon";
        r12 = "chat_inMediaIconSelected";
        r5.setColors(r6, r7, r9, r12);
        r5 = r1.videoRadialProgress;
        r6 = r28.isDrawSelectionBackground();
        if (r6 == 0) goto L_0x12b3;
    L_0x12b0:
        r6 = "chat_inFileProgressSelected";
        goto L_0x12b5;
    L_0x12b3:
        r6 = "chat_inFileProgress";
    L_0x12b5:
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r5.setProgressColor(r6);
    L_0x12bc:
        r5 = r1.rect;
        r6 = r1.photoImage;
        r6 = r6.getImageX();
        r6 = (float) r6;
        r7 = r1.photoImage;
        r7 = r7.getImageY();
        r7 = (float) r7;
        r9 = r1.photoImage;
        r9 = r9.getImageX();
        r12 = r1.photoImage;
        r12 = r12.getImageWidth();
        r9 = r9 + r12;
        r9 = (float) r9;
        r12 = r1.photoImage;
        r12 = r12.getImageY();
        r15 = r1.photoImage;
        r15 = r15.getImageHeight();
        r12 = r12 + r15;
        r12 = (float) r12;
        r5.set(r6, r7, r9, r12);
        r5 = r1.rect;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r6 = (float) r6;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r7 = (float) r7;
        r9 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r8.drawRoundRect(r5, r6, r7, r9);
        goto L_0x140d;
    L_0x12fe:
        r5 = r1.radialProgress;
        r6 = "chat_mediaLoaderPhoto";
        r7 = "chat_mediaLoaderPhotoSelected";
        r9 = "chat_mediaLoaderPhotoIcon";
        r12 = "chat_mediaLoaderPhotoIconSelected";
        r5.setColors(r6, r7, r9, r12);
        r5 = r1.radialProgress;
        r6 = "chat_mediaProgress";
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r5.setProgressColor(r6);
        r5 = r1.videoRadialProgress;
        r6 = "chat_mediaLoaderPhoto";
        r7 = "chat_mediaLoaderPhotoSelected";
        r9 = "chat_mediaLoaderPhotoIcon";
        r12 = "chat_mediaLoaderPhotoIconSelected";
        r5.setColors(r6, r7, r9, r12);
        r5 = r1.videoRadialProgress;
        r6 = "chat_mediaProgress";
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r5.setProgressColor(r6);
        r5 = r1.buttonState;
        r6 = -1;
        if (r5 != r6) goto L_0x140d;
    L_0x1333:
        r5 = r1.radialProgress;
        r5 = r5.getIcon();
        r6 = 4;
        if (r5 == r6) goto L_0x140d;
    L_0x133c:
        r5 = r1.radialProgress;
        r5.setIcon(r6, r10, r10);
        goto L_0x140d;
    L_0x1343:
        r2 = r1.buttonX;
        r3 = r1.backgroundWidth;
        r2 = r2 + r3;
        r3 = r1.currentMessageObject;
        r3 = r3.type;
        if (r3 != 0) goto L_0x1351;
    L_0x134e:
        r3 = 1114112000; // 0x42680000 float:58.0 double:5.50444465E-315;
        goto L_0x1353;
    L_0x1351:
        r3 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
    L_0x1353:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r3;
        r1.otherX = r2;
        r3 = r1.buttonY;
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 - r4;
        r1.otherY = r3;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r2, r3);
        r2 = r1.buttonX;
        r3 = 1112801280; // 0x42540000 float:53.0 double:5.49796883E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 + r3;
        r3 = r1.buttonY;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r3 = r3 + r4;
        r4 = r1.buttonY;
        r5 = 1104674816; // 0x41d80000 float:27.0 double:5.457818764E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r4 = r4 + r5;
        r5 = r1.docTitleLayout;
        if (r5 == 0) goto L_0x13a0;
    L_0x1385:
        r5 = r5.getLineCount();
        if (r5 <= r10) goto L_0x13a0;
    L_0x138b:
        r5 = r1.docTitleLayout;
        r5 = r5.getLineCount();
        r5 = r5 - r10;
        r6 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 * r6;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r5 = r5 + r6;
        r4 = r4 + r5;
    L_0x13a0:
        r5 = r1.currentMessageObject;
        r5 = r5.isOutOwner();
        if (r5 == 0) goto L_0x13db;
    L_0x13a8:
        r5 = r1.radialProgress;
        r6 = r28.isDrawSelectionBackground();
        if (r6 != 0) goto L_0x13b8;
    L_0x13b0:
        r6 = r1.buttonPressed;
        if (r6 == 0) goto L_0x13b5;
    L_0x13b4:
        goto L_0x13b8;
    L_0x13b5:
        r6 = "chat_outAudioProgress";
        goto L_0x13ba;
    L_0x13b8:
        r6 = "chat_outAudioSelectedProgress";
    L_0x13ba:
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r5.setProgressColor(r6);
        r5 = r1.videoRadialProgress;
        r6 = r28.isDrawSelectionBackground();
        if (r6 != 0) goto L_0x13d1;
    L_0x13c9:
        r6 = r1.videoButtonPressed;
        if (r6 == 0) goto L_0x13ce;
    L_0x13cd:
        goto L_0x13d1;
    L_0x13ce:
        r6 = "chat_outAudioProgress";
        goto L_0x13d3;
    L_0x13d1:
        r6 = "chat_outAudioSelectedProgress";
    L_0x13d3:
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r5.setProgressColor(r6);
        goto L_0x140d;
    L_0x13db:
        r5 = r1.radialProgress;
        r6 = r28.isDrawSelectionBackground();
        if (r6 != 0) goto L_0x13eb;
    L_0x13e3:
        r6 = r1.buttonPressed;
        if (r6 == 0) goto L_0x13e8;
    L_0x13e7:
        goto L_0x13eb;
    L_0x13e8:
        r6 = "chat_inAudioProgress";
        goto L_0x13ed;
    L_0x13eb:
        r6 = "chat_inAudioSelectedProgress";
    L_0x13ed:
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r5.setProgressColor(r6);
        r5 = r1.videoRadialProgress;
        r6 = r28.isDrawSelectionBackground();
        if (r6 != 0) goto L_0x1404;
    L_0x13fc:
        r6 = r1.videoButtonPressed;
        if (r6 == 0) goto L_0x1401;
    L_0x1400:
        goto L_0x1404;
    L_0x1401:
        r6 = "chat_inAudioProgress";
        goto L_0x1406;
    L_0x1404:
        r6 = "chat_inAudioSelectedProgress";
    L_0x1406:
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r5.setProgressColor(r6);
    L_0x140d:
        r0.draw(r8);
        r0 = r1.docTitleLayout;	 Catch:{ Exception -> 0x1428 }
        if (r0 == 0) goto L_0x142c;
    L_0x1414:
        r29.save();	 Catch:{ Exception -> 0x1428 }
        r0 = r1.docTitleOffsetX;	 Catch:{ Exception -> 0x1428 }
        r0 = r0 + r2;
        r0 = (float) r0;	 Catch:{ Exception -> 0x1428 }
        r3 = (float) r3;	 Catch:{ Exception -> 0x1428 }
        r8.translate(r0, r3);	 Catch:{ Exception -> 0x1428 }
        r0 = r1.docTitleLayout;	 Catch:{ Exception -> 0x1428 }
        r0.draw(r8);	 Catch:{ Exception -> 0x1428 }
        r29.restore();	 Catch:{ Exception -> 0x1428 }
        goto L_0x142c;
    L_0x1428:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x142c:
        r0 = r1.infoLayout;	 Catch:{ Exception -> 0x1441 }
        if (r0 == 0) goto L_0x1445;
    L_0x1430:
        r29.save();	 Catch:{ Exception -> 0x1441 }
        r0 = (float) r2;	 Catch:{ Exception -> 0x1441 }
        r2 = (float) r4;	 Catch:{ Exception -> 0x1441 }
        r8.translate(r0, r2);	 Catch:{ Exception -> 0x1441 }
        r0 = r1.infoLayout;	 Catch:{ Exception -> 0x1441 }
        r0.draw(r8);	 Catch:{ Exception -> 0x1441 }
        r29.restore();	 Catch:{ Exception -> 0x1441 }
        goto L_0x1445;
    L_0x1441:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x1445:
        r0 = r1.buttonState;
        r2 = -1;
        if (r0 != r2) goto L_0x14e6;
    L_0x144a:
        r0 = r1.currentMessageObject;
        r0 = r0.needDrawBluredPreview();
        if (r0 == 0) goto L_0x14e6;
    L_0x1452:
        r0 = org.telegram.messenger.MediaController.getInstance();
        r2 = r1.currentMessageObject;
        r0 = r0.isPlayingMessage(r2);
        if (r0 != 0) goto L_0x14e6;
    L_0x145e:
        r0 = r1.photoImage;
        r0 = r0.getVisible();
        if (r0 == 0) goto L_0x14e6;
    L_0x1466:
        r0 = r1.currentMessageObject;
        r2 = r0.messageOwner;
        r2 = r2.destroyTime;
        if (r2 == 0) goto L_0x14e6;
    L_0x146e:
        r0 = r0.isOutOwner();
        if (r0 != 0) goto L_0x14e1;
    L_0x1474:
        r2 = java.lang.System.currentTimeMillis();
        r0 = r1.currentAccount;
        r0 = org.telegram.tgnet.ConnectionsManager.getInstance(r0);
        r0 = r0.getTimeDifference();
        r0 = r0 * 1000;
        r4 = (long) r0;
        r2 = r2 + r4;
        r4 = 0;
        r0 = r1.currentMessageObject;
        r0 = r0.messageOwner;
        r0 = r0.destroyTime;
        r6 = (long) r0;
        r15 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r6 = r6 * r15;
        r6 = r6 - r2;
        r2 = java.lang.Math.max(r4, r6);
        r0 = (float) r2;
        r2 = r1.currentMessageObject;
        r2 = r2.messageOwner;
        r2 = r2.ttl;
        r2 = (float) r2;
        r3 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r2 = r2 * r3;
        r0 = r0 / r2;
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_deleteProgressPaint;
        r3 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r4 = r1.controlsAlpha;
        r4 = r4 * r3;
        r3 = (int) r4;
        r2.setAlpha(r3);
        r3 = r1.deleteProgressRect;
        r4 = -1028390912; // 0xffffffffc2b40000 float:-90.0 double:NaN;
        r2 = -1011613696; // 0xffffffffc3b40000 float:-360.0 double:NaN;
        r5 = r0 * r2;
        r6 = 1;
        r7 = org.telegram.p004ui.ActionBar.Theme.chat_deleteProgressPaint;
        r2 = r29;
        r2.drawArc(r3, r4, r5, r6, r7);
        r2 = 0;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 == 0) goto L_0x14e1;
    L_0x14c6:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r2 = r1.deleteProgressRect;
        r3 = r2.left;
        r3 = (int) r3;
        r3 = r3 - r0;
        r4 = r2.top;
        r4 = (int) r4;
        r4 = r4 - r0;
        r5 = r2.right;
        r5 = (int) r5;
        r0 = r0 * 2;
        r5 = r5 + r0;
        r2 = r2.bottom;
        r2 = (int) r2;
        r2 = r2 + r0;
        r1.invalidate(r3, r4, r5, r2);
    L_0x14e1:
        r0 = r1.currentMessageObject;
        r1.updateSecretTimeText(r0);
    L_0x14e6:
        r0 = r1.currentMessageObject;
        r2 = r0.type;
        r3 = 4;
        if (r2 != r3) goto L_0x1555;
    L_0x14ed:
        r0 = r0.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r0 != 0) goto L_0x1555;
    L_0x14f5:
        r0 = r1.currentMapProvider;
        if (r0 != r11) goto L_0x1555;
    L_0x14f9:
        r0 = r1.photoImage;
        r0 = r0.hasNotThumb();
        if (r0 == 0) goto L_0x1555;
    L_0x1501:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_redLocationIcon;
        r0 = r0.getIntrinsicWidth();
        r0 = (float) r0;
        r2 = 1061997773; // 0x3f4ccccd float:0.8 double:5.246966156E-315;
        r0 = r0 * r2;
        r0 = (int) r0;
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_redLocationIcon;
        r2 = r2.getIntrinsicHeight();
        r2 = (float) r2;
        r3 = 1061997773; // 0x3f4ccccd float:0.8 double:5.246966156E-315;
        r2 = r2 * r3;
        r2 = (int) r2;
        r3 = r1.photoImage;
        r3 = r3.getImageX();
        r4 = r1.photoImage;
        r4 = r4.getImageWidth();
        r4 = r4 - r0;
        r4 = r4 / r11;
        r3 = r3 + r4;
        r4 = r1.photoImage;
        r4 = r4.getImageY();
        r5 = r1.photoImage;
        r5 = r5.getImageHeight();
        r5 = r5 / r11;
        r5 = r5 - r2;
        r4 = r4 + r5;
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_redLocationIcon;
        r6 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r7 = r1.photoImage;
        r7 = r7.getCurrentAlpha();
        r7 = r7 * r6;
        r6 = (int) r7;
        r5.setAlpha(r6);
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_redLocationIcon;
        r0 = r0 + r3;
        r2 = r2 + r4;
        r5.setBounds(r3, r4, r0, r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_redLocationIcon;
        r0.draw(r8);
    L_0x1555:
        r0 = r1.botButtons;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x17ec;
    L_0x155d:
        r0 = r1.currentMessageObject;
        r0 = r0.isOutOwner();
        if (r0 == 0) goto L_0x1572;
    L_0x1565:
        r0 = r28.getMeasuredWidth();
        r2 = r1.widthForButtons;
        r0 = r0 - r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r19);
        r0 = r0 - r2;
        goto L_0x1582;
    L_0x1572:
        r0 = r1.backgroundDrawableLeft;
        r2 = r1.mediaBackground;
        if (r2 == 0) goto L_0x157b;
    L_0x1578:
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x157d;
    L_0x157b:
        r2 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
    L_0x157d:
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 + r2;
    L_0x1582:
        r9 = 0;
    L_0x1583:
        r2 = r1.botButtons;
        r2 = r2.size();
        if (r9 >= r2) goto L_0x17ec;
    L_0x158b:
        r2 = r1.botButtons;
        r2 = r2.get(r9);
        r12 = r2;
        r12 = (org.telegram.p004ui.Cells.ChatMessageCell.BotButton) r12;
        r2 = r12.f576y;
        r3 = r1.layoutHeight;
        r2 = r2 + r3;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r2 = r2 - r3;
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_systemDrawable;
        r4 = r1.pressedBotButton;
        if (r9 != r4) goto L_0x15a9;
    L_0x15a6:
        r4 = org.telegram.p004ui.ActionBar.Theme.colorPressedFilter;
        goto L_0x15ab;
    L_0x15a9:
        r4 = org.telegram.p004ui.ActionBar.Theme.colorFilter;
    L_0x15ab:
        r3.setColorFilter(r4);
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_systemDrawable;
        r4 = r12.f575x;
        r4 = r4 + r0;
        r5 = r12.f575x;
        r5 = r5 + r0;
        r6 = r12.width;
        r5 = r5 + r6;
        r6 = r12.height;
        r6 = r6 + r2;
        r3.setBounds(r4, r2, r5, r6);
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_systemDrawable;
        r3.draw(r8);
        r29.save();
        r3 = r12.f575x;
        r3 = r3 + r0;
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 + r4;
        r3 = (float) r3;
        r4 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r5 = r12.title;
        r6 = r12.title;
        r6 = r6.getLineCount();
        r6 = r6 - r10;
        r5 = r5.getLineBottom(r6);
        r4 = r4 - r5;
        r4 = r4 / r11;
        r4 = r4 + r2;
        r4 = (float) r4;
        r8.translate(r3, r4);
        r3 = r12.title;
        r3.draw(r8);
        r29.restore();
        r3 = r12.button;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonUrl;
        if (r3 == 0) goto L_0x1634;
    L_0x160c:
        r3 = r12.f575x;
        r4 = r12.width;
        r3 = r3 + r4;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r3 = r3 - r4;
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_botLinkDrawalbe;
        r4 = r4.getIntrinsicWidth();
        r3 = r3 - r4;
        r3 = r3 + r0;
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_botLinkDrawalbe;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r2 = r2 + r5;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r4, r3, r2);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_botLinkDrawalbe;
        r2.draw(r8);
    L_0x1631:
        r7 = 0;
        goto L_0x17e8;
    L_0x1634:
        r3 = r12.button;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonSwitchInline;
        if (r3 == 0) goto L_0x1662;
    L_0x163c:
        r3 = r12.f575x;
        r4 = r12.width;
        r3 = r3 + r4;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r3 = r3 - r4;
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_botInlineDrawable;
        r4 = r4.getIntrinsicWidth();
        r3 = r3 - r4;
        r3 = r3 + r0;
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_botInlineDrawable;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r2 = r2 + r5;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r4, r3, r2);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_botInlineDrawable;
        r2.draw(r8);
        goto L_0x1631;
    L_0x1662:
        r3 = r12.button;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonCallback;
        if (r3 != 0) goto L_0x168a;
    L_0x166a:
        r3 = r12.button;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonRequestGeoLocation;
        if (r3 != 0) goto L_0x168a;
    L_0x1672:
        r3 = r12.button;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonGame;
        if (r3 != 0) goto L_0x168a;
    L_0x167a:
        r3 = r12.button;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonBuy;
        if (r3 != 0) goto L_0x168a;
    L_0x1682:
        r3 = r12.button;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonUrlAuth;
        if (r3 == 0) goto L_0x1631;
    L_0x168a:
        r3 = r12.button;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonCallback;
        if (r3 != 0) goto L_0x16aa;
    L_0x1692:
        r3 = r12.button;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonGame;
        if (r3 != 0) goto L_0x16aa;
    L_0x169a:
        r3 = r12.button;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonBuy;
        if (r3 != 0) goto L_0x16aa;
    L_0x16a2:
        r3 = r12.button;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonUrlAuth;
        if (r3 == 0) goto L_0x16bc;
    L_0x16aa:
        r3 = r1.currentAccount;
        r3 = org.telegram.messenger.SendMessagesHelper.getInstance(r3);
        r4 = r1.currentMessageObject;
        r5 = r12.button;
        r3 = r3.isSendingCallback(r4, r5);
        if (r3 != 0) goto L_0x16d9;
    L_0x16bc:
        r3 = r12.button;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonRequestGeoLocation;
        if (r3 == 0) goto L_0x16d7;
    L_0x16c4:
        r3 = r1.currentAccount;
        r3 = org.telegram.messenger.SendMessagesHelper.getInstance(r3);
        r4 = r1.currentMessageObject;
        r5 = r12.button;
        r3 = r3.isSendingCurrentLocation(r4, r5);
        if (r3 == 0) goto L_0x16d7;
    L_0x16d6:
        goto L_0x16d9;
    L_0x16d7:
        r15 = 0;
        goto L_0x16da;
    L_0x16d9:
        r15 = 1;
    L_0x16da:
        if (r15 != 0) goto L_0x16e7;
    L_0x16dc:
        if (r15 != 0) goto L_0x1631;
    L_0x16de:
        r3 = r12.progressAlpha;
        r4 = 0;
        r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r3 == 0) goto L_0x1631;
    L_0x16e7:
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_botProgressPaint;
        r4 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r5 = r12.progressAlpha;
        r6 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r5 = r5 * r6;
        r5 = (int) r5;
        r4 = java.lang.Math.min(r4, r5);
        r3.setAlpha(r4);
        r3 = r12.f575x;
        r4 = r12.width;
        r3 = r3 + r4;
        r4 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 - r4;
        r3 = r3 + r0;
        r4 = r1.rect;
        r5 = (float) r3;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r22);
        r6 = r6 + r2;
        r6 = (float) r6;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r3 = r3 + r7;
        r3 = (float) r3;
        r7 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r2 = r2 + r7;
        r2 = (float) r2;
        r4.set(r5, r6, r3, r2);
        r3 = r1.rect;
        r2 = r12.angle;
        r4 = (float) r2;
        r5 = 1130102784; // 0x435c0000 float:220.0 double:5.58344962E-315;
        r6 = 0;
        r7 = org.telegram.p004ui.ActionBar.Theme.chat_botProgressPaint;
        r2 = r29;
        r2.drawArc(r3, r4, r5, r6, r7);
        r2 = r1.rect;
        r2 = r2.left;
        r2 = (int) r2;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r2 = r2 - r3;
        r3 = r1.rect;
        r3 = r3.top;
        r3 = (int) r3;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r3 = r3 - r4;
        r4 = r1.rect;
        r4 = r4.right;
        r4 = (int) r4;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r4 = r4 + r5;
        r5 = r1.rect;
        r5 = r5.bottom;
        r5 = (int) r5;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r21);
        r5 = r5 + r6;
        r1.invalidate(r2, r3, r4, r5);
        r2 = java.lang.System.currentTimeMillis();
        r4 = r12.lastUpdateTime;
        r6 = java.lang.System.currentTimeMillis();
        r4 = r4 - r6;
        r4 = java.lang.Math.abs(r4);
        r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r16 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r16 >= 0) goto L_0x17e4;
    L_0x1779:
        r4 = r12.lastUpdateTime;
        r4 = r2 - r4;
        r6 = 360; // 0x168 float:5.04E-43 double:1.78E-321;
        r6 = r6 * r4;
        r6 = (float) r6;
        r7 = 1157234688; // 0x44fa0000 float:2000.0 double:5.717499035E-315;
        r6 = r6 / r7;
        r7 = r12.angle;
        r7 = (float) r7;
        r7 = r7 + r6;
        r6 = (int) r7;
        r12.angle = r6;
        r6 = r12.angle;
        r7 = r12.angle;
        r7 = r7 / 360;
        r7 = r7 * 360;
        r6 = r6 - r7;
        r12.angle = r6;
        if (r15 == 0) goto L_0x17c3;
    L_0x17a3:
        r6 = r12.progressAlpha;
        r6 = (r6 > r13 ? 1 : (r6 == r13 ? 0 : -1));
        if (r6 >= 0) goto L_0x17e4;
    L_0x17ab:
        r6 = r12.progressAlpha;
        r4 = (float) r4;
        r5 = 1128792064; // 0x43480000 float:200.0 double:5.5769738E-315;
        r4 = r4 / r5;
        r6 = r6 + r4;
        r12.progressAlpha = r6;
        r4 = r12.progressAlpha;
        r4 = (r4 > r13 ? 1 : (r4 == r13 ? 0 : -1));
        if (r4 <= 0) goto L_0x17e4;
    L_0x17bf:
        r12.progressAlpha = r13;
        goto L_0x17e4;
    L_0x17c3:
        r6 = r12.progressAlpha;
        r7 = 0;
        r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r6 <= 0) goto L_0x17e5;
    L_0x17cc:
        r6 = r12.progressAlpha;
        r4 = (float) r4;
        r5 = 1128792064; // 0x43480000 float:200.0 double:5.5769738E-315;
        r4 = r4 / r5;
        r6 = r6 - r4;
        r12.progressAlpha = r6;
        r4 = r12.progressAlpha;
        r4 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1));
        if (r4 >= 0) goto L_0x17e5;
    L_0x17e0:
        r12.progressAlpha = r7;
        goto L_0x17e5;
    L_0x17e4:
        r7 = 0;
    L_0x17e5:
        r12.lastUpdateTime = r2;
    L_0x17e8:
        r9 = r9 + 1;
        goto L_0x1583;
    L_0x17ec:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.drawContent(android.graphics.Canvas):void");
    }

    private int getMiniIconForCurrentState() {
        int i = this.miniButtonState;
        if (i < 0) {
            return 4;
        }
        return i == 0 ? 2 : 3;
    }

    /* JADX WARNING: Missing block: B:47:0x0097, code skipped:
            if (r0.attachPathExists == false) goto L_0x009a;
     */
    private int getIconForCurrentState() {
        /*
        r15 = this;
        r0 = r15.documentAttachType;
        r1 = "chat_outMediaIconSelected";
        r2 = "chat_outMediaIcon";
        r3 = "chat_outLoaderSelected";
        r4 = "chat_outLoader";
        r5 = "chat_inMediaIconSelected";
        r6 = "chat_inMediaIcon";
        r7 = "chat_inLoaderSelected";
        r8 = "chat_inLoader";
        r9 = 0;
        r10 = 4;
        r11 = 2;
        r12 = 3;
        r13 = 1;
        if (r0 == r12) goto L_0x00c0;
    L_0x0019:
        r14 = 5;
        if (r0 != r14) goto L_0x001e;
    L_0x001c:
        goto L_0x00c0;
    L_0x001e:
        if (r0 != r13) goto L_0x0043;
    L_0x0020:
        r0 = r15.drawPhotoImage;
        if (r0 != 0) goto L_0x0043;
    L_0x0024:
        r0 = r15.currentMessageObject;
        r0 = r0.isOutOwner();
        if (r0 == 0) goto L_0x0032;
    L_0x002c:
        r0 = r15.radialProgress;
        r0.setColors(r4, r3, r2, r1);
        goto L_0x0037;
    L_0x0032:
        r0 = r15.radialProgress;
        r0.setColors(r8, r7, r6, r5);
    L_0x0037:
        r0 = r15.buttonState;
        r1 = -1;
        if (r0 != r1) goto L_0x003d;
    L_0x003c:
        return r14;
    L_0x003d:
        if (r0 != 0) goto L_0x0040;
    L_0x003f:
        return r11;
    L_0x0040:
        if (r0 != r13) goto L_0x00bf;
    L_0x0042:
        return r12;
    L_0x0043:
        r0 = r15.radialProgress;
        r1 = "chat_mediaLoaderPhoto";
        r2 = "chat_mediaLoaderPhotoSelected";
        r3 = "chat_mediaLoaderPhotoIcon";
        r4 = "chat_mediaLoaderPhotoIconSelected";
        r0.setColors(r1, r2, r3, r4);
        r0 = r15.videoRadialProgress;
        r2 = "chat_mediaLoaderPhotoSelected";
        r3 = "chat_mediaLoaderPhotoIcon";
        r4 = "chat_mediaLoaderPhotoIconSelected";
        r0.setColors(r1, r2, r3, r4);
        r0 = r15.buttonState;
        if (r0 < 0) goto L_0x0072;
    L_0x005f:
        if (r0 >= r10) goto L_0x0072;
    L_0x0061:
        if (r0 != 0) goto L_0x0064;
    L_0x0063:
        return r11;
    L_0x0064:
        if (r0 != r13) goto L_0x0067;
    L_0x0066:
        return r12;
    L_0x0067:
        if (r0 != r11) goto L_0x006a;
    L_0x0069:
        return r9;
    L_0x006a:
        if (r0 != r12) goto L_0x00bf;
    L_0x006c:
        r0 = r15.autoPlayingMedia;
        if (r0 == 0) goto L_0x0071;
    L_0x0070:
        r9 = 4;
    L_0x0071:
        return r9;
    L_0x0072:
        r0 = r15.buttonState;
        r1 = -1;
        if (r0 != r1) goto L_0x00bf;
    L_0x0077:
        r0 = r15.documentAttachType;
        if (r0 != r13) goto L_0x009c;
    L_0x007b:
        r0 = r15.drawPhotoImage;
        if (r0 == 0) goto L_0x009a;
    L_0x007f:
        r0 = r15.currentPhotoObject;
        if (r0 != 0) goto L_0x0087;
    L_0x0083:
        r0 = r15.currentPhotoObjectThumb;
        if (r0 == 0) goto L_0x009a;
    L_0x0087:
        r0 = r15.photoImage;
        r0 = r0.hasBitmapImage();
        if (r0 != 0) goto L_0x009b;
    L_0x008f:
        r0 = r15.currentMessageObject;
        r1 = r0.mediaExists;
        if (r1 != 0) goto L_0x009b;
    L_0x0095:
        r0 = r0.attachPathExists;
        if (r0 == 0) goto L_0x009a;
    L_0x0099:
        goto L_0x009b;
    L_0x009a:
        r10 = 5;
    L_0x009b:
        return r10;
    L_0x009c:
        r0 = r15.currentMessageObject;
        r0 = r0.needDrawBluredPreview();
        if (r0 == 0) goto L_0x00ba;
    L_0x00a4:
        r0 = r15.currentMessageObject;
        r1 = r0.messageOwner;
        r1 = r1.destroyTime;
        if (r1 == 0) goto L_0x00b8;
    L_0x00ac:
        r0 = r0.isOutOwner();
        if (r0 == 0) goto L_0x00b5;
    L_0x00b2:
        r0 = 9;
        return r0;
    L_0x00b5:
        r0 = 11;
        return r0;
    L_0x00b8:
        r0 = 7;
        return r0;
    L_0x00ba:
        r0 = r15.hasEmbed;
        if (r0 == 0) goto L_0x00bf;
    L_0x00be:
        return r9;
    L_0x00bf:
        return r10;
    L_0x00c0:
        r0 = r15.currentMessageObject;
        r0 = r0.isOutOwner();
        if (r0 == 0) goto L_0x00ce;
    L_0x00c8:
        r0 = r15.radialProgress;
        r0.setColors(r4, r3, r2, r1);
        goto L_0x00d3;
    L_0x00ce:
        r0 = r15.radialProgress;
        r0.setColors(r8, r7, r6, r5);
    L_0x00d3:
        r0 = r15.buttonState;
        if (r0 != r13) goto L_0x00d8;
    L_0x00d7:
        return r13;
    L_0x00d8:
        if (r0 != r11) goto L_0x00db;
    L_0x00da:
        return r11;
    L_0x00db:
        if (r0 != r10) goto L_0x00de;
    L_0x00dd:
        return r12;
    L_0x00de:
        return r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.getIconForCurrentState():int");
    }

    private int getMaxNameWidth() {
        int dp;
        int i = this.documentAttachType;
        if (i == 6 || i == 8 || this.currentMessageObject.type == 5) {
            Point point;
            if (AndroidUtilities.isTablet()) {
                if (this.isChat && !this.currentMessageObject.isOutOwner() && this.currentMessageObject.needDrawAvatar()) {
                    i = AndroidUtilities.getMinTabletSide();
                    dp = AndroidUtilities.m26dp(42.0f);
                } else {
                    i = AndroidUtilities.getMinTabletSide();
                    i -= this.backgroundWidth;
                    dp = AndroidUtilities.m26dp(57.0f);
                }
            } else if (this.isChat && !this.currentMessageObject.isOutOwner() && this.currentMessageObject.needDrawAvatar()) {
                point = AndroidUtilities.displaySize;
                i = Math.min(point.x, point.y);
                dp = AndroidUtilities.m26dp(42.0f);
            } else {
                point = AndroidUtilities.displaySize;
                i = Math.min(point.x, point.y);
                i -= this.backgroundWidth;
                dp = AndroidUtilities.m26dp(57.0f);
            }
            i -= dp;
            i -= this.backgroundWidth;
            dp = AndroidUtilities.m26dp(57.0f);
        } else if (this.currentMessagesGroup != null) {
            if (AndroidUtilities.isTablet()) {
                i = AndroidUtilities.getMinTabletSide();
            } else {
                i = AndroidUtilities.displaySize.x;
            }
            dp = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < this.currentMessagesGroup.posArray.size(); i3++) {
                GroupedMessagePosition groupedMessagePosition = (GroupedMessagePosition) this.currentMessagesGroup.posArray.get(i3);
                if (groupedMessagePosition.minY != (byte) 0) {
                    break;
                }
                double d = (double) i2;
                double ceil = Math.ceil((double) ((((float) (groupedMessagePosition.f57pw + groupedMessagePosition.leftSpanOffset)) / 1000.0f) * ((float) i)));
                Double.isNaN(d);
                i2 = (int) (d + ceil);
            }
            if (this.isAvatarVisible) {
                dp = 48;
            }
            return i2 - AndroidUtilities.m26dp((float) (dp + 31));
        } else {
            i = this.backgroundWidth;
            dp = AndroidUtilities.m26dp(this.mediaBackground ? 22.0f : 31.0f);
        }
        return i - dp;
    }

    /* JADX WARNING: Removed duplicated region for block: B:78:0x0114  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00e3  */
    /* JADX WARNING: Missing block: B:42:0x009c, code skipped:
            if (r5 != 5) goto L_0x009f;
     */
    /* JADX WARNING: Missing block: B:73:0x0109, code skipped:
            if ((r2 & 2) != 0) goto L_0x0116;
     */
    public void updateButtonState(boolean r17, boolean r18, boolean r19) {
        /*
        r16 = this;
        r0 = r16;
        r1 = r17;
        r2 = 0;
        if (r18 == 0) goto L_0x0015;
    L_0x0007:
        r3 = r0.currentMessageObject;
        r3 = org.telegram.p004ui.PhotoViewer.isShowingImage(r3);
        if (r3 != 0) goto L_0x0013;
    L_0x000f:
        r3 = r0.attachedToWindow;
        if (r3 != 0) goto L_0x0015;
    L_0x0013:
        r3 = 0;
        goto L_0x0017;
    L_0x0015:
        r3 = r18;
    L_0x0017:
        r0.drawRadialCheckBackground = r2;
        r4 = 0;
        r5 = r0.currentMessageObject;
        r5 = r5.type;
        r6 = 5;
        r7 = 8;
        r8 = 3;
        r9 = 7;
        r10 = 4;
        r11 = 1;
        if (r5 != r11) goto L_0x003b;
    L_0x0027:
        r4 = r0.currentPhotoObject;
        if (r4 != 0) goto L_0x0031;
    L_0x002b:
        r2 = r0.radialProgress;
        r2.setIcon(r10, r1, r3);
        return;
    L_0x0031:
        r4 = org.telegram.messenger.FileLoader.getAttachFileName(r4);
        r5 = r0.currentMessageObject;
        r5 = r5.mediaExists;
        goto L_0x00ab;
    L_0x003b:
        if (r5 == r7) goto L_0x0068;
    L_0x003d:
        r12 = r0.documentAttachType;
        if (r12 == r9) goto L_0x0068;
    L_0x0041:
        if (r12 == r10) goto L_0x0068;
    L_0x0043:
        if (r12 == r7) goto L_0x0068;
    L_0x0045:
        r13 = 9;
        if (r5 == r13) goto L_0x0068;
    L_0x0049:
        if (r12 == r8) goto L_0x0068;
    L_0x004b:
        if (r12 != r6) goto L_0x004e;
    L_0x004d:
        goto L_0x0068;
    L_0x004e:
        if (r12 == 0) goto L_0x005b;
    L_0x0050:
        r4 = r0.documentAttach;
        r4 = org.telegram.messenger.FileLoader.getAttachFileName(r4);
        r5 = r0.currentMessageObject;
        r5 = r5.mediaExists;
        goto L_0x00ab;
    L_0x005b:
        r5 = r0.currentPhotoObject;
        if (r5 == 0) goto L_0x009f;
    L_0x005f:
        r4 = org.telegram.messenger.FileLoader.getAttachFileName(r5);
        r5 = r0.currentMessageObject;
        r5 = r5.mediaExists;
        goto L_0x00ab;
    L_0x0068:
        r5 = r0.currentMessageObject;
        r12 = r5.useCustomPhoto;
        if (r12 == 0) goto L_0x007a;
    L_0x006e:
        r0.buttonState = r11;
        r2 = r0.radialProgress;
        r4 = r16.getIconForCurrentState();
        r2.setIcon(r4, r1, r3);
        return;
    L_0x007a:
        r12 = r5.attachPathExists;
        if (r12 == 0) goto L_0x0090;
    L_0x007e:
        r5 = r5.messageOwner;
        r5 = r5.attachPath;
        r5 = android.text.TextUtils.isEmpty(r5);
        if (r5 != 0) goto L_0x0090;
    L_0x0088:
        r4 = r0.currentMessageObject;
        r4 = r4.messageOwner;
        r4 = r4.attachPath;
        r5 = 1;
        goto L_0x00ab;
    L_0x0090:
        r5 = r0.currentMessageObject;
        r5 = r5.isSendError();
        if (r5 == 0) goto L_0x00a1;
    L_0x0098:
        r5 = r0.documentAttachType;
        if (r5 == r8) goto L_0x00a1;
    L_0x009c:
        if (r5 != r6) goto L_0x009f;
    L_0x009e:
        goto L_0x00a1;
    L_0x009f:
        r5 = 0;
        goto L_0x00ab;
    L_0x00a1:
        r4 = r0.currentMessageObject;
        r4 = r4.getFileName();
        r5 = r0.currentMessageObject;
        r5 = r5.mediaExists;
    L_0x00ab:
        r12 = r0.currentAccount;
        r12 = org.telegram.messenger.DownloadController.getInstance(r12);
        r13 = r0.currentMessageObject;
        r12 = r12.canDownloadMedia(r13);
        r13 = r0.currentMessageObject;
        r13 = r13.isSent();
        r14 = 2;
        if (r13 == 0) goto L_0x00dc;
    L_0x00c0:
        r13 = r0.documentAttachType;
        if (r13 == r10) goto L_0x00ca;
    L_0x00c4:
        if (r13 == r9) goto L_0x00ca;
    L_0x00c6:
        if (r13 != r14) goto L_0x00dc;
    L_0x00c8:
        if (r12 == 0) goto L_0x00dc;
    L_0x00ca:
        r13 = r0.currentMessageObject;
        r13 = r13.canStreamVideo();
        if (r13 == 0) goto L_0x00dc;
    L_0x00d2:
        r13 = r0.currentMessageObject;
        r13 = r13.needDrawBluredPreview();
        if (r13 != 0) goto L_0x00dc;
    L_0x00da:
        r13 = 1;
        goto L_0x00dd;
    L_0x00dc:
        r13 = 0;
    L_0x00dd:
        r0.canStreamVideo = r13;
        r13 = org.telegram.messenger.SharedConfig.streamMedia;
        if (r13 == 0) goto L_0x0114;
    L_0x00e3:
        r13 = r0.currentMessageObject;
        r18 = r3;
        r2 = r13.getDialogId();
        r3 = (int) r2;
        if (r3 == 0) goto L_0x0116;
    L_0x00ee:
        r2 = r0.currentMessageObject;
        r2 = r2.isSecretMedia();
        if (r2 != 0) goto L_0x0116;
    L_0x00f6:
        r2 = r0.documentAttachType;
        if (r2 == r6) goto L_0x010b;
    L_0x00fa:
        r2 = r0.canStreamVideo;
        if (r2 == 0) goto L_0x0116;
    L_0x00fe:
        r2 = r0.currentPosition;
        if (r2 == 0) goto L_0x0116;
    L_0x0102:
        r2 = r2.flags;
        r3 = r2 & 1;
        if (r3 == 0) goto L_0x010b;
    L_0x0108:
        r2 = r2 & r14;
        if (r2 != 0) goto L_0x0116;
    L_0x010b:
        if (r5 == 0) goto L_0x010f;
    L_0x010d:
        r2 = 1;
        goto L_0x0110;
    L_0x010f:
        r2 = 2;
    L_0x0110:
        r0.hasMiniProgress = r2;
        r5 = 1;
        goto L_0x0116;
    L_0x0114:
        r18 = r3;
    L_0x0116:
        r2 = r0.currentMessageObject;
        r2 = r2.isSendError();
        if (r2 != 0) goto L_0x072f;
    L_0x011e:
        r2 = android.text.TextUtils.isEmpty(r4);
        if (r2 == 0) goto L_0x0136;
    L_0x0124:
        r2 = r0.currentMessageObject;
        r2 = r2.isSending();
        if (r2 != 0) goto L_0x0136;
    L_0x012c:
        r2 = r0.currentMessageObject;
        r2 = r2.isEditing();
        if (r2 != 0) goto L_0x0136;
    L_0x0134:
        goto L_0x072f;
    L_0x0136:
        r2 = r0.currentMessageObject;
        r2 = r2.messageOwner;
        r2 = r2.params;
        if (r2 == 0) goto L_0x0148;
    L_0x013e:
        r3 = "query_id";
        r2 = r2.containsKey(r3);
        if (r2 == 0) goto L_0x0148;
    L_0x0146:
        r2 = 1;
        goto L_0x0149;
    L_0x0148:
        r2 = 0;
    L_0x0149:
        r3 = r0.documentAttachType;
        r13 = -1;
        r15 = 0;
        if (r3 == r8) goto L_0x0573;
    L_0x014f:
        if (r3 != r6) goto L_0x0153;
    L_0x0151:
        goto L_0x0573;
    L_0x0153:
        r2 = r0.currentMessageObject;
        r2 = r2.type;
        if (r2 != 0) goto L_0x01f7;
    L_0x0159:
        if (r3 == r11) goto L_0x01f7;
    L_0x015b:
        if (r3 == r14) goto L_0x01f7;
    L_0x015d:
        if (r3 == r9) goto L_0x01f7;
    L_0x015f:
        if (r3 == r10) goto L_0x01f7;
    L_0x0161:
        if (r3 == r7) goto L_0x01f7;
    L_0x0163:
        r2 = r0.currentPhotoObject;
        if (r2 == 0) goto L_0x01f6;
    L_0x0167:
        r2 = r0.drawImageButton;
        if (r2 != 0) goto L_0x016d;
    L_0x016b:
        goto L_0x01f6;
    L_0x016d:
        if (r5 != 0) goto L_0x01cc;
    L_0x016f:
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.DownloadController.getInstance(r2);
        r3 = r0.currentMessageObject;
        r2.addLoadingFileObserver(r4, r3, r0);
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.FileLoader.getInstance(r2);
        r2 = r2.isLoadingFile(r4);
        if (r2 != 0) goto L_0x01a5;
    L_0x0186:
        r2 = r0.cancelLoading;
        if (r2 != 0) goto L_0x01a1;
    L_0x018a:
        r2 = r0.documentAttachType;
        if (r2 != 0) goto L_0x0190;
    L_0x018e:
        if (r12 != 0) goto L_0x019e;
    L_0x0190:
        r2 = r0.documentAttachType;
        if (r2 != r14) goto L_0x01a1;
    L_0x0194:
        r2 = r0.documentAttach;
        r2 = org.telegram.messenger.MessageObject.isGifDocument(r2);
        if (r2 == 0) goto L_0x01a1;
    L_0x019c:
        if (r12 == 0) goto L_0x01a1;
    L_0x019e:
        r0.buttonState = r11;
        goto L_0x01b6;
    L_0x01a1:
        r2 = 0;
        r0.buttonState = r2;
        goto L_0x01b6;
    L_0x01a5:
        r0.buttonState = r11;
        r3 = org.telegram.messenger.ImageLoader.getInstance();
        r3 = r3.getFileProgress(r4);
        if (r3 == 0) goto L_0x01b6;
    L_0x01b1:
        r3 = r3.floatValue();
        r15 = r3;
    L_0x01b6:
        r3 = r0.radialProgress;
        r2 = 0;
        r3.setProgress(r15, r2);
        r3 = r0.radialProgress;
        r4 = r16.getIconForCurrentState();
        r6 = r18;
        r3.setIcon(r4, r1, r6);
        r16.invalidate();
        goto L_0x0724;
    L_0x01cc:
        r6 = r18;
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.DownloadController.getInstance(r3);
        r3.removeLoadingFileObserver(r0);
        r3 = r0.documentAttachType;
        if (r3 != r14) goto L_0x01e6;
    L_0x01db:
        r3 = r0.photoImage;
        r3 = r3.isAllowStartAnimation();
        if (r3 != 0) goto L_0x01e6;
    L_0x01e3:
        r0.buttonState = r14;
        goto L_0x01e8;
    L_0x01e6:
        r0.buttonState = r13;
    L_0x01e8:
        r3 = r0.radialProgress;
        r4 = r16.getIconForCurrentState();
        r3.setIcon(r4, r1, r6);
        r16.invalidate();
        goto L_0x0724;
    L_0x01f6:
        return;
    L_0x01f7:
        r6 = r18;
        r3 = r0.currentMessageObject;
        r3 = r3.isOut();
        if (r3 == 0) goto L_0x02f9;
    L_0x0201:
        r3 = r0.currentMessageObject;
        r3 = r3.isSending();
        if (r3 != 0) goto L_0x0211;
    L_0x0209:
        r3 = r0.currentMessageObject;
        r3 = r3.isEditing();
        if (r3 == 0) goto L_0x02f9;
    L_0x0211:
        r3 = r0.currentMessageObject;
        r3 = r3.messageOwner;
        r3 = r3.attachPath;
        r3 = android.text.TextUtils.isEmpty(r3);
        if (r3 != 0) goto L_0x02c5;
    L_0x021d:
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.DownloadController.getInstance(r3);
        r4 = r0.currentMessageObject;
        r5 = r4.messageOwner;
        r5 = r5.attachPath;
        r3.addLoadingFileObserver(r5, r4, r0);
        r0.wasSending = r11;
        r3 = r0.currentMessageObject;
        r3 = r3.messageOwner;
        r3 = r3.attachPath;
        if (r3 == 0) goto L_0x0241;
    L_0x0236:
        r4 = "http";
        r3 = r3.startsWith(r4);
        if (r3 != 0) goto L_0x023f;
    L_0x023e:
        goto L_0x0241;
    L_0x023f:
        r3 = 0;
        goto L_0x0242;
    L_0x0241:
        r3 = 1;
    L_0x0242:
        r4 = r0.currentMessageObject;
        r4 = r4.messageOwner;
        r5 = r4.params;
        r4 = r4.message;
        if (r4 == 0) goto L_0x0262;
    L_0x024c:
        if (r5 == 0) goto L_0x0262;
    L_0x024e:
        r4 = "url";
        r4 = r5.containsKey(r4);
        if (r4 != 0) goto L_0x025e;
    L_0x0256:
        r4 = "bot";
        r4 = r5.containsKey(r4);
        if (r4 == 0) goto L_0x0262;
    L_0x025e:
        r0.buttonState = r13;
        r3 = 0;
        goto L_0x0264;
    L_0x0262:
        r0.buttonState = r11;
    L_0x0264:
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.SendMessagesHelper.getInstance(r4);
        r5 = r0.currentMessageObject;
        r5 = r5.getId();
        r4 = r4.isSendingMessage(r5);
        r5 = r0.currentPosition;
        if (r5 == 0) goto L_0x028a;
    L_0x0278:
        if (r4 == 0) goto L_0x028a;
    L_0x027a:
        r5 = r0.buttonState;
        if (r5 != r11) goto L_0x028a;
    L_0x027e:
        r0.drawRadialCheckBackground = r11;
        r16.getIconForCurrentState();
        r5 = r0.radialProgress;
        r7 = 6;
        r5.setIcon(r7, r1, r6);
        goto L_0x0293;
    L_0x028a:
        r5 = r0.radialProgress;
        r7 = r16.getIconForCurrentState();
        r5.setIcon(r7, r1, r6);
    L_0x0293:
        if (r3 == 0) goto L_0x02ba;
    L_0x0295:
        r3 = org.telegram.messenger.ImageLoader.getInstance();
        r5 = r0.currentMessageObject;
        r5 = r5.messageOwner;
        r5 = r5.attachPath;
        r3 = r3.getFileProgress(r5);
        if (r3 != 0) goto L_0x02ad;
    L_0x02a5:
        if (r4 == 0) goto L_0x02ad;
    L_0x02a7:
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = java.lang.Float.valueOf(r4);
    L_0x02ad:
        r4 = r0.radialProgress;
        if (r3 == 0) goto L_0x02b5;
    L_0x02b1:
        r15 = r3.floatValue();
    L_0x02b5:
        r2 = 0;
        r4.setProgress(r15, r2);
        goto L_0x02c0;
    L_0x02ba:
        r2 = 0;
        r3 = r0.radialProgress;
        r3.setProgress(r15, r2);
    L_0x02c0:
        r16.invalidate();
        r4 = 0;
        goto L_0x02f2;
    L_0x02c5:
        r0.buttonState = r13;
        r16.getIconForCurrentState();
        r3 = r0.radialProgress;
        r4 = r0.currentMessageObject;
        r4 = r4.isSticker();
        if (r4 != 0) goto L_0x02e8;
    L_0x02d4:
        r4 = r0.currentMessageObject;
        r4 = r4.isAnimatedSticker();
        if (r4 != 0) goto L_0x02e8;
    L_0x02dc:
        r4 = r0.currentMessageObject;
        r4 = r4.isLocation();
        if (r4 == 0) goto L_0x02e5;
    L_0x02e4:
        goto L_0x02e8;
    L_0x02e5:
        r2 = 12;
        goto L_0x02e9;
    L_0x02e8:
        r2 = 4;
    L_0x02e9:
        r4 = 0;
        r3.setIcon(r2, r1, r4);
        r2 = r0.radialProgress;
        r2.setProgress(r15, r4);
    L_0x02f2:
        r2 = r0.videoRadialProgress;
        r2.setIcon(r10, r1, r4);
        goto L_0x0724;
    L_0x02f9:
        r3 = r0.wasSending;
        if (r3 == 0) goto L_0x0312;
    L_0x02fd:
        r3 = r0.currentMessageObject;
        r3 = r3.messageOwner;
        r3 = r3.attachPath;
        r3 = android.text.TextUtils.isEmpty(r3);
        if (r3 != 0) goto L_0x0312;
    L_0x0309:
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.DownloadController.getInstance(r3);
        r3.removeLoadingFileObserver(r0);
    L_0x0312:
        r3 = r0.documentAttachType;
        if (r3 == r10) goto L_0x031a;
    L_0x0316:
        if (r3 == r14) goto L_0x031a;
    L_0x0318:
        if (r3 != r9) goto L_0x0367;
    L_0x031a:
        r3 = r0.autoPlayingMedia;
        if (r3 == 0) goto L_0x0367;
    L_0x031e:
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.FileLoader.getInstance(r3);
        r2 = r0.documentAttach;
        r7 = org.telegram.messenger.MediaController.getInstance();
        r9 = r0.currentMessageObject;
        r7 = r7.isPlayingMessage(r9);
        r2 = r3.isLoadingVideo(r2, r7);
        r3 = r0.photoImage;
        r3 = r3.getAnimation();
        if (r3 == 0) goto L_0x035c;
    L_0x033c:
        r7 = r0.currentMessageObject;
        r9 = r7.hadAnimationNotReadyLoading;
        if (r9 == 0) goto L_0x034e;
    L_0x0342:
        r3 = r3.hasBitmap();
        if (r3 == 0) goto L_0x0368;
    L_0x0348:
        r3 = r0.currentMessageObject;
        r7 = 0;
        r3.hadAnimationNotReadyLoading = r7;
        goto L_0x0368;
    L_0x034e:
        if (r2 == 0) goto L_0x0358;
    L_0x0350:
        r3 = r3.hasBitmap();
        if (r3 != 0) goto L_0x0358;
    L_0x0356:
        r3 = 1;
        goto L_0x0359;
    L_0x0358:
        r3 = 0;
    L_0x0359:
        r7.hadAnimationNotReadyLoading = r3;
        goto L_0x0368;
    L_0x035c:
        r3 = r0.documentAttachType;
        if (r3 != r14) goto L_0x0368;
    L_0x0360:
        if (r5 != 0) goto L_0x0368;
    L_0x0362:
        r3 = r0.currentMessageObject;
        r3.hadAnimationNotReadyLoading = r11;
        goto L_0x0368;
    L_0x0367:
        r2 = 0;
    L_0x0368:
        r3 = r0.hasMiniProgress;
        if (r3 == 0) goto L_0x03d3;
    L_0x036c:
        r2 = r0.radialProgress;
        r3 = "chat_inLoaderPhoto";
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r2.setMiniProgressBackgroundColor(r3);
        r0.buttonState = r8;
        r2 = r0.radialProgress;
        r3 = r16.getIconForCurrentState();
        r2.setIcon(r3, r1, r6);
        r2 = r0.hasMiniProgress;
        if (r2 != r11) goto L_0x0392;
    L_0x0386:
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.DownloadController.getInstance(r2);
        r2.removeLoadingFileObserver(r0);
        r0.miniButtonState = r13;
        goto L_0x03c8;
    L_0x0392:
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.DownloadController.getInstance(r2);
        r3 = r0.currentMessageObject;
        r2.addLoadingFileObserver(r4, r3, r0);
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.FileLoader.getInstance(r2);
        r2 = r2.isLoadingFile(r4);
        if (r2 != 0) goto L_0x03ad;
    L_0x03a9:
        r2 = 0;
        r0.miniButtonState = r2;
        goto L_0x03c8;
    L_0x03ad:
        r0.miniButtonState = r11;
        r2 = org.telegram.messenger.ImageLoader.getInstance();
        r2 = r2.getFileProgress(r4);
        if (r2 == 0) goto L_0x03c3;
    L_0x03b9:
        r4 = r0.radialProgress;
        r2 = r2.floatValue();
        r4.setProgress(r2, r6);
        goto L_0x03c8;
    L_0x03c3:
        r2 = r0.radialProgress;
        r2.setProgress(r15, r6);
    L_0x03c8:
        r2 = r0.radialProgress;
        r4 = r16.getMiniIconForCurrentState();
        r2.setMiniIcon(r4, r1, r6);
        goto L_0x0724;
    L_0x03d3:
        if (r5 != 0) goto L_0x04f5;
    L_0x03d5:
        r5 = r0.documentAttachType;
        if (r5 == r10) goto L_0x03de;
    L_0x03d9:
        if (r5 == r14) goto L_0x03de;
    L_0x03db:
        r7 = 7;
        if (r5 != r7) goto L_0x03ec;
    L_0x03de:
        r5 = r0.autoPlayingMedia;
        if (r5 == 0) goto L_0x03ec;
    L_0x03e2:
        r5 = r0.currentMessageObject;
        r5 = r5.hadAnimationNotReadyLoading;
        if (r5 != 0) goto L_0x03ec;
    L_0x03e8:
        if (r2 != 0) goto L_0x03ec;
    L_0x03ea:
        goto L_0x04f5;
    L_0x03ec:
        r2 = r0.documentAttachType;
        if (r2 == r10) goto L_0x03f5;
    L_0x03f0:
        if (r2 != r14) goto L_0x03f3;
    L_0x03f2:
        goto L_0x03f5;
    L_0x03f3:
        r2 = 0;
        goto L_0x03f6;
    L_0x03f5:
        r2 = 1;
    L_0x03f6:
        r0.drawVideoSize = r2;
        r2 = r0.documentAttachType;
        if (r2 == r10) goto L_0x0401;
    L_0x03fc:
        if (r2 == r14) goto L_0x0401;
    L_0x03fe:
        r5 = 7;
        if (r2 != r5) goto L_0x041e;
    L_0x0401:
        r2 = r0.canStreamVideo;
        if (r2 == 0) goto L_0x041e;
    L_0x0405:
        r2 = r0.drawVideoImageButton;
        if (r2 != 0) goto L_0x041e;
    L_0x0409:
        if (r6 == 0) goto L_0x041e;
    L_0x040b:
        r2 = r0.animatingDrawVideoImageButton;
        if (r2 == r14) goto L_0x0426;
    L_0x040f:
        r5 = r0.animatingDrawVideoImageButtonProgress;
        r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r5 >= 0) goto L_0x0426;
    L_0x0417:
        if (r2 != 0) goto L_0x041b;
    L_0x0419:
        r0.animatingDrawVideoImageButtonProgress = r15;
    L_0x041b:
        r0.animatingDrawVideoImageButton = r14;
        goto L_0x0426;
    L_0x041e:
        r2 = r0.animatingDrawVideoImageButton;
        if (r2 != 0) goto L_0x0426;
    L_0x0422:
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0.animatingDrawVideoImageButtonProgress = r2;
    L_0x0426:
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.DownloadController.getInstance(r2);
        r5 = r0.currentMessageObject;
        r2.addLoadingFileObserver(r4, r5, r0);
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.FileLoader.getInstance(r2);
        r2 = r2.isLoadingFile(r4);
        if (r2 != 0) goto L_0x0489;
    L_0x043d:
        r2 = r0.cancelLoading;
        if (r2 != 0) goto L_0x0446;
    L_0x0441:
        if (r12 == 0) goto L_0x0446;
    L_0x0443:
        r0.buttonState = r11;
        goto L_0x0449;
    L_0x0446:
        r2 = 0;
        r0.buttonState = r2;
    L_0x0449:
        r3 = r0.documentAttachType;
        if (r3 == r10) goto L_0x0451;
    L_0x044d:
        if (r3 != r14) goto L_0x046d;
    L_0x044f:
        if (r12 == 0) goto L_0x046d;
    L_0x0451:
        r3 = r0.canStreamVideo;
        if (r3 == 0) goto L_0x046d;
    L_0x0455:
        r0.drawVideoImageButton = r11;
        r16.getIconForCurrentState();
        r3 = r0.radialProgress;
        r4 = r0.autoPlayingMedia;
        if (r4 == 0) goto L_0x0462;
    L_0x0460:
        r4 = 4;
        goto L_0x0463;
    L_0x0462:
        r4 = 0;
    L_0x0463:
        r3.setIcon(r4, r1, r6);
        r3 = r0.videoRadialProgress;
        r3.setIcon(r14, r1, r6);
        goto L_0x04f0;
    L_0x046d:
        r2 = 0;
        r0.drawVideoImageButton = r2;
        r3 = r0.radialProgress;
        r4 = r16.getIconForCurrentState();
        r3.setIcon(r4, r1, r6);
        r3 = r0.videoRadialProgress;
        r3.setIcon(r10, r1, r2);
        r1 = r0.drawVideoSize;
        if (r1 != 0) goto L_0x04f0;
    L_0x0482:
        r1 = r0.animatingDrawVideoImageButton;
        if (r1 != 0) goto L_0x04f0;
    L_0x0486:
        r0.animatingDrawVideoImageButtonProgress = r15;
        goto L_0x04f0;
    L_0x0489:
        r0.buttonState = r11;
        r3 = org.telegram.messenger.ImageLoader.getInstance();
        r3 = r3.getFileProgress(r4);
        r4 = r0.documentAttachType;
        if (r4 == r10) goto L_0x049b;
    L_0x0497:
        if (r4 != r14) goto L_0x04c8;
    L_0x0499:
        if (r12 == 0) goto L_0x04c8;
    L_0x049b:
        r4 = r0.canStreamVideo;
        if (r4 == 0) goto L_0x04c8;
    L_0x049f:
        r0.drawVideoImageButton = r11;
        r16.getIconForCurrentState();
        r4 = r0.radialProgress;
        r5 = r0.autoPlayingMedia;
        if (r5 != 0) goto L_0x04b1;
    L_0x04aa:
        r5 = r0.documentAttachType;
        if (r5 != r14) goto L_0x04af;
    L_0x04ae:
        goto L_0x04b1;
    L_0x04af:
        r5 = 0;
        goto L_0x04b2;
    L_0x04b1:
        r5 = 4;
    L_0x04b2:
        r4.setIcon(r5, r1, r6);
        r4 = r0.videoRadialProgress;
        if (r3 == 0) goto L_0x04bd;
    L_0x04b9:
        r15 = r3.floatValue();
    L_0x04bd:
        r4.setProgress(r15, r6);
        r3 = r0.videoRadialProgress;
        r4 = 14;
        r3.setIcon(r4, r1, r6);
        goto L_0x04f0;
    L_0x04c8:
        r2 = 0;
        r0.drawVideoImageButton = r2;
        r4 = r0.radialProgress;
        if (r3 == 0) goto L_0x04d4;
    L_0x04cf:
        r3 = r3.floatValue();
        goto L_0x04d5;
    L_0x04d4:
        r3 = 0;
    L_0x04d5:
        r4.setProgress(r3, r6);
        r3 = r0.radialProgress;
        r4 = r16.getIconForCurrentState();
        r3.setIcon(r4, r1, r6);
        r3 = r0.videoRadialProgress;
        r3.setIcon(r10, r1, r2);
        r1 = r0.drawVideoSize;
        if (r1 != 0) goto L_0x04f0;
    L_0x04ea:
        r1 = r0.animatingDrawVideoImageButton;
        if (r1 != 0) goto L_0x04f0;
    L_0x04ee:
        r0.animatingDrawVideoImageButtonProgress = r15;
    L_0x04f0:
        r16.invalidate();
        goto L_0x0724;
    L_0x04f5:
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.DownloadController.getInstance(r3);
        r3.removeLoadingFileObserver(r0);
        r3 = r0.drawVideoImageButton;
        if (r3 == 0) goto L_0x0517;
    L_0x0502:
        if (r6 == 0) goto L_0x0517;
    L_0x0504:
        r3 = r0.animatingDrawVideoImageButton;
        if (r3 == r11) goto L_0x051d;
    L_0x0508:
        r4 = r0.animatingDrawVideoImageButtonProgress;
        r4 = (r4 > r15 ? 1 : (r4 == r15 ? 0 : -1));
        if (r4 <= 0) goto L_0x051d;
    L_0x050e:
        if (r3 != 0) goto L_0x0514;
    L_0x0510:
        r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0.animatingDrawVideoImageButtonProgress = r3;
    L_0x0514:
        r0.animatingDrawVideoImageButton = r11;
        goto L_0x051d;
    L_0x0517:
        r3 = r0.animatingDrawVideoImageButton;
        if (r3 != 0) goto L_0x051d;
    L_0x051b:
        r0.animatingDrawVideoImageButtonProgress = r15;
    L_0x051d:
        r2 = 0;
        r0.drawVideoImageButton = r2;
        r0.drawVideoSize = r2;
        r2 = r0.currentMessageObject;
        r2 = r2.needDrawBluredPreview();
        if (r2 == 0) goto L_0x052d;
    L_0x052a:
        r0.buttonState = r13;
        goto L_0x0549;
    L_0x052d:
        r2 = r0.currentMessageObject;
        r4 = r2.type;
        r5 = 8;
        if (r4 != r5) goto L_0x0540;
    L_0x0535:
        r2 = r2.gifState;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 != 0) goto L_0x0540;
    L_0x053d:
        r0.buttonState = r14;
        goto L_0x0549;
    L_0x0540:
        r2 = r0.documentAttachType;
        if (r2 != r10) goto L_0x0547;
    L_0x0544:
        r0.buttonState = r8;
        goto L_0x0549;
    L_0x0547:
        r0.buttonState = r13;
    L_0x0549:
        r2 = r0.videoRadialProgress;
        r4 = r0.animatingDrawVideoImageButton;
        if (r4 == 0) goto L_0x0550;
    L_0x054f:
        goto L_0x0551;
    L_0x0550:
        r11 = 0;
    L_0x0551:
        r2.setIcon(r10, r1, r11);
        r2 = r0.radialProgress;
        r4 = r16.getIconForCurrentState();
        r2.setIcon(r4, r1, r6);
        if (r19 != 0) goto L_0x056e;
    L_0x055f:
        r1 = r0.photoNotSet;
        if (r1 == 0) goto L_0x056e;
    L_0x0563:
        r1 = r0.currentMessageObject;
        r2 = r0.currentMessagesGroup;
        r4 = r0.pinnedBottom;
        r5 = r0.pinnedTop;
        r0.setMessageObject(r1, r2, r4, r5);
    L_0x056e:
        r16.invalidate();
        goto L_0x0724;
    L_0x0573:
        r6 = r18;
        r7 = r0.currentMessageObject;
        r7 = r7.isOut();
        if (r7 == 0) goto L_0x058d;
    L_0x057d:
        r7 = r0.currentMessageObject;
        r7 = r7.isSending();
        if (r7 != 0) goto L_0x0597;
    L_0x0585:
        r7 = r0.currentMessageObject;
        r7 = r7.isEditing();
        if (r7 != 0) goto L_0x0597;
    L_0x058d:
        r7 = r0.currentMessageObject;
        r7 = r7.isSendError();
        if (r7 == 0) goto L_0x0613;
    L_0x0595:
        if (r2 == 0) goto L_0x0613;
    L_0x0597:
        r4 = r0.currentMessageObject;
        r4 = r4.messageOwner;
        r4 = r4.attachPath;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x05ff;
    L_0x05a3:
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.DownloadController.getInstance(r4);
        r5 = r0.currentMessageObject;
        r7 = r5.messageOwner;
        r7 = r7.attachPath;
        r4.addLoadingFileObserver(r7, r5, r0);
        r0.wasSending = r11;
        r0.buttonState = r10;
        r4 = r0.radialProgress;
        r5 = r16.getIconForCurrentState();
        r4.setIcon(r5, r1, r6);
        if (r2 != 0) goto L_0x05f7;
    L_0x05c1:
        r1 = org.telegram.messenger.ImageLoader.getInstance();
        r2 = r0.currentMessageObject;
        r2 = r2.messageOwner;
        r2 = r2.attachPath;
        r1 = r1.getFileProgress(r2);
        if (r1 != 0) goto L_0x05e9;
    L_0x05d1:
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.SendMessagesHelper.getInstance(r2);
        r4 = r0.currentMessageObject;
        r4 = r4.getId();
        r2 = r2.isSendingMessage(r4);
        if (r2 == 0) goto L_0x05e9;
    L_0x05e3:
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r1 = java.lang.Float.valueOf(r2);
    L_0x05e9:
        r2 = r0.radialProgress;
        if (r1 == 0) goto L_0x05f1;
    L_0x05ed:
        r15 = r1.floatValue();
    L_0x05f1:
        r3 = 0;
        r2.setProgress(r15, r3);
        goto L_0x0721;
    L_0x05f7:
        r3 = 0;
        r1 = r0.radialProgress;
        r1.setProgress(r15, r3);
        goto L_0x0721;
    L_0x05ff:
        r3 = 0;
        r0.buttonState = r13;
        r16.getIconForCurrentState();
        r2 = r0.radialProgress;
        r4 = 12;
        r2.setIcon(r4, r1, r3);
        r1 = r0.radialProgress;
        r1.setProgress(r15, r3);
        goto L_0x0721;
    L_0x0613:
        r3 = r0.hasMiniProgress;
        if (r3 == 0) goto L_0x06a6;
    L_0x0617:
        r3 = r0.radialProgress;
        r5 = r0.currentMessageObject;
        r5 = r5.isOutOwner();
        if (r5 == 0) goto L_0x0624;
    L_0x0621:
        r5 = "chat_outLoader";
        goto L_0x0626;
    L_0x0624:
        r5 = "chat_inLoader";
    L_0x0626:
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r3.setMiniProgressBackgroundColor(r5);
        r3 = org.telegram.messenger.MediaController.getInstance();
        r5 = r0.currentMessageObject;
        r3 = r3.isPlayingMessage(r5);
        if (r3 == 0) goto L_0x0649;
    L_0x0639:
        if (r3 == 0) goto L_0x0646;
    L_0x063b:
        r3 = org.telegram.messenger.MediaController.getInstance();
        r3 = r3.isMessagePaused();
        if (r3 == 0) goto L_0x0646;
    L_0x0645:
        goto L_0x0649;
    L_0x0646:
        r0.buttonState = r11;
        goto L_0x064c;
    L_0x0649:
        r2 = 0;
        r0.buttonState = r2;
    L_0x064c:
        r3 = r0.radialProgress;
        r5 = r16.getIconForCurrentState();
        r3.setIcon(r5, r1, r6);
        r3 = r0.hasMiniProgress;
        if (r3 != r11) goto L_0x0665;
    L_0x0659:
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.DownloadController.getInstance(r3);
        r3.removeLoadingFileObserver(r0);
        r0.miniButtonState = r13;
        goto L_0x069b;
    L_0x0665:
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.DownloadController.getInstance(r3);
        r5 = r0.currentMessageObject;
        r3.addLoadingFileObserver(r4, r5, r0);
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.FileLoader.getInstance(r3);
        r3 = r3.isLoadingFile(r4);
        if (r3 != 0) goto L_0x0680;
    L_0x067c:
        r2 = 0;
        r0.miniButtonState = r2;
        goto L_0x069b;
    L_0x0680:
        r0.miniButtonState = r11;
        r3 = org.telegram.messenger.ImageLoader.getInstance();
        r3 = r3.getFileProgress(r4);
        if (r3 == 0) goto L_0x0696;
    L_0x068c:
        r4 = r0.radialProgress;
        r3 = r3.floatValue();
        r4.setProgress(r3, r6);
        goto L_0x069b;
    L_0x0696:
        r3 = r0.radialProgress;
        r3.setProgress(r15, r6);
    L_0x069b:
        r3 = r0.radialProgress;
        r4 = r16.getMiniIconForCurrentState();
        r3.setMiniIcon(r4, r1, r6);
        goto L_0x0721;
    L_0x06a6:
        if (r5 == 0) goto L_0x06da;
    L_0x06a8:
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.DownloadController.getInstance(r3);
        r3.removeLoadingFileObserver(r0);
        r3 = org.telegram.messenger.MediaController.getInstance();
        r4 = r0.currentMessageObject;
        r3 = r3.isPlayingMessage(r4);
        if (r3 == 0) goto L_0x06cd;
    L_0x06bd:
        if (r3 == 0) goto L_0x06ca;
    L_0x06bf:
        r3 = org.telegram.messenger.MediaController.getInstance();
        r3 = r3.isMessagePaused();
        if (r3 == 0) goto L_0x06ca;
    L_0x06c9:
        goto L_0x06cd;
    L_0x06ca:
        r0.buttonState = r11;
        goto L_0x06d0;
    L_0x06cd:
        r2 = 0;
        r0.buttonState = r2;
    L_0x06d0:
        r3 = r0.radialProgress;
        r4 = r16.getIconForCurrentState();
        r3.setIcon(r4, r1, r6);
        goto L_0x0721;
    L_0x06da:
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.DownloadController.getInstance(r3);
        r5 = r0.currentMessageObject;
        r3.addLoadingFileObserver(r4, r5, r0);
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.FileLoader.getInstance(r3);
        r3 = r3.isLoadingFile(r4);
        if (r3 != 0) goto L_0x06fd;
    L_0x06f1:
        r0.buttonState = r14;
        r3 = r0.radialProgress;
        r4 = r16.getIconForCurrentState();
        r3.setIcon(r4, r1, r6);
        goto L_0x0721;
    L_0x06fd:
        r0.buttonState = r10;
        r3 = org.telegram.messenger.ImageLoader.getInstance();
        r3 = r3.getFileProgress(r4);
        if (r3 == 0) goto L_0x0713;
    L_0x0709:
        r4 = r0.radialProgress;
        r3 = r3.floatValue();
        r4.setProgress(r3, r6);
        goto L_0x0718;
    L_0x0713:
        r3 = r0.radialProgress;
        r3.setProgress(r15, r6);
    L_0x0718:
        r3 = r0.radialProgress;
        r4 = r16.getIconForCurrentState();
        r3.setIcon(r4, r1, r6);
    L_0x0721:
        r16.updatePlayingMessageProgress();
    L_0x0724:
        r1 = r0.hasMiniProgress;
        if (r1 != 0) goto L_0x072e;
    L_0x0728:
        r1 = r0.radialProgress;
        r2 = 0;
        r1.setMiniIcon(r10, r2, r6);
    L_0x072e:
        return;
    L_0x072f:
        r2 = 0;
        r3 = r0.radialProgress;
        r3.setIcon(r10, r1, r2);
        r3 = r0.radialProgress;
        r3.setMiniIcon(r10, r1, r2);
        r3 = r0.videoRadialProgress;
        r3.setIcon(r10, r1, r2);
        r3 = r0.videoRadialProgress;
        r3.setMiniIcon(r10, r1, r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.updateButtonState(boolean, boolean, boolean):void");
    }

    private void didPressMiniButton(boolean z) {
        int i = this.miniButtonState;
        if (i == 0) {
            this.miniButtonState = 1;
            this.radialProgress.setProgress(0.0f, false);
            i = this.documentAttachType;
            if (i == 3 || i == 5) {
                FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.currentMessageObject, 1, 0);
            } else if (i == 4) {
                FileLoader instance = FileLoader.getInstance(this.currentAccount);
                Document document = this.documentAttach;
                MessageObject messageObject = this.currentMessageObject;
                instance.loadFile(document, messageObject, 1, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 0);
            }
            this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), false, true);
            invalidate();
        } else if (i == 1) {
            i = this.documentAttachType;
            if ((i == 3 || i == 5) && MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                MediaController.getInstance().cleanupPlayer(true, true);
            }
            this.miniButtonState = 0;
            FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
            this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), false, true);
            invalidate();
        }
    }

    private void didPressButton(boolean z, boolean z2) {
        boolean z3 = z;
        int i = 2;
        int i2;
        if (this.buttonState == 0 && (!this.drawVideoImageButton || z2)) {
            i2 = this.documentAttachType;
            if (i2 == 3 || i2 == 5) {
                if (this.miniButtonState == 0) {
                    FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.currentMessageObject, 1, 0);
                }
                if (this.delegate.needPlayMessage(this.currentMessageObject)) {
                    if (this.hasMiniProgress == 2 && this.miniButtonState != 1) {
                        this.miniButtonState = 1;
                        this.radialProgress.setProgress(0.0f, false);
                        this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), false, true);
                    }
                    updatePlayingMessageProgress();
                    this.buttonState = 1;
                    this.radialProgress.setIcon(getIconForCurrentState(), false, true);
                    invalidate();
                    return;
                }
                return;
            }
            PhotoSize photoSize;
            String str;
            this.cancelLoading = false;
            if (z2) {
                this.videoRadialProgress.setProgress(0.0f, false);
            } else {
                this.radialProgress.setProgress(0.0f, false);
            }
            if (this.currentPhotoObject == null || !(this.photoImage.hasNotThumb() || this.currentPhotoObjectThumb == null)) {
                photoSize = this.currentPhotoObjectThumb;
                str = this.currentPhotoFilterThumb;
            } else {
                photoSize = this.currentPhotoObject;
                if (!(photoSize instanceof TL_photoStrippedSize)) {
                    if (!"s".equals(photoSize.type)) {
                        str = this.currentPhotoFilter;
                    }
                }
                str = this.currentPhotoFilterThumb;
            }
            String str2 = str;
            MessageObject messageObject = this.currentMessageObject;
            int i3 = messageObject.type;
            int i4;
            MessageObject messageObject2;
            Document document;
            if (i3 == 1) {
                this.photoImage.setForceLoading(true);
                ImageReceiver imageReceiver = this.photoImage;
                ImageLocation forObject = ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject);
                String str3 = this.currentPhotoFilter;
                ImageLocation forObject2 = ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject);
                String str4 = this.currentPhotoFilterThumb;
                i4 = this.currentPhotoObject.size;
                messageObject2 = this.currentMessageObject;
                imageReceiver.setImage(forObject, str3, forObject2, str4, i4, null, messageObject2, messageObject2.shouldEncryptPhotoOrVideo() ? 2 : 0);
            } else if (i3 == 8) {
                FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.currentMessageObject, 1, 0);
            } else if (!messageObject.isRoundVideo()) {
                i2 = this.currentMessageObject.type;
                if (i2 == 9) {
                    FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.currentMessageObject, 0, 0);
                } else {
                    i4 = this.documentAttachType;
                    if (i4 == 4) {
                        FileLoader instance = FileLoader.getInstance(this.currentAccount);
                        document = this.documentAttach;
                        messageObject2 = this.currentMessageObject;
                        if (!messageObject2.shouldEncryptPhotoOrVideo()) {
                            i = 0;
                        }
                        instance.loadFile(document, messageObject2, 1, i);
                    } else if (i2 != 0 || i4 == 0) {
                        this.photoImage.setForceLoading(true);
                        this.photoImage.setImage(ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject), this.currentPhotoFilter, ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject), this.currentPhotoFilterThumb, 0, null, this.currentMessageObject, 0);
                    } else if (i4 == 2) {
                        this.photoImage.setForceLoading(true);
                        this.photoImage.setImage(ImageLocation.getForDocument(this.documentAttach), null, ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), this.currentPhotoFilterThumb, this.documentAttach.size, null, this.currentMessageObject, 0);
                        this.currentMessageObject.gifState = 2.0f;
                    } else if (i4 == 1) {
                        FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.currentMessageObject, 0, 0);
                    } else if (i4 == 8) {
                        this.photoImage.setImage(ImageLocation.getForDocument(this.documentAttach), this.currentPhotoFilter, ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), "b1", 0, "jpg", this.currentMessageObject, 1);
                    }
                }
            } else if (this.currentMessageObject.isSecretMedia()) {
                FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 1);
            } else {
                MessageObject messageObject3 = this.currentMessageObject;
                messageObject3.gifState = 2.0f;
                document = messageObject3.getDocument();
                this.photoImage.setForceLoading(true);
                this.photoImage.setImage(ImageLocation.getForDocument(document), null, ImageLocation.getForObject(photoSize, document), str2, document.size, null, this.currentMessageObject, 0);
            }
            this.buttonState = 1;
            if (z2) {
                this.videoRadialProgress.setIcon(14, false, z3);
            } else {
                this.radialProgress.setIcon(getIconForCurrentState(), false, z3);
            }
            invalidate();
        } else if (this.buttonState != 1 || (this.drawVideoImageButton && !z2)) {
            i2 = this.buttonState;
            if (i2 == 2) {
                i2 = this.documentAttachType;
                if (i2 == 3 || i2 == 5) {
                    this.radialProgress.setProgress(0.0f, false);
                    FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.currentMessageObject, 1, 0);
                    this.buttonState = 4;
                    this.radialProgress.setIcon(getIconForCurrentState(), true, z3);
                    invalidate();
                    return;
                }
                if (this.currentMessageObject.isRoundVideo()) {
                    MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                    if (playingMessageObject == null || !playingMessageObject.isRoundVideo()) {
                        this.photoImage.setAllowStartAnimation(true);
                        this.photoImage.startAnimation();
                    }
                } else {
                    this.photoImage.setAllowStartAnimation(true);
                    this.photoImage.startAnimation();
                }
                this.currentMessageObject.gifState = 0.0f;
                this.buttonState = -1;
                this.radialProgress.setIcon(getIconForCurrentState(), false, z3);
            } else if (i2 == 3 || (i2 == 0 && this.drawVideoImageButton)) {
                if (this.hasMiniProgress == 2 && this.miniButtonState != 1) {
                    this.miniButtonState = 1;
                    this.radialProgress.setProgress(0.0f, false);
                    this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), false, z3);
                }
                this.delegate.didPressImage(this, 0.0f, 0.0f);
            } else if (this.buttonState == 4) {
                i2 = this.documentAttachType;
                if (i2 != 3 && i2 != 5) {
                    return;
                }
                if ((this.currentMessageObject.isOut() && (this.currentMessageObject.isSending() || this.currentMessageObject.isEditing())) || this.currentMessageObject.isSendError()) {
                    ChatMessageCellDelegate chatMessageCellDelegate = this.delegate;
                    if (chatMessageCellDelegate != null) {
                        chatMessageCellDelegate.didPressCancelSendButton(this);
                        return;
                    }
                    return;
                }
                FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
                this.buttonState = 2;
                this.radialProgress.setIcon(getIconForCurrentState(), false, z3);
                invalidate();
            }
        } else {
            this.photoImage.setForceLoading(false);
            i2 = this.documentAttachType;
            if (i2 == 3 || i2 == 5) {
                if (MediaController.getInstance().lambda$startAudioAgain$5$MediaController(this.currentMessageObject)) {
                    this.buttonState = 0;
                    this.radialProgress.setIcon(getIconForCurrentState(), false, z3);
                    invalidate();
                }
            } else if (!this.currentMessageObject.isOut() || (!this.currentMessageObject.isSending() && !this.currentMessageObject.isEditing())) {
                this.cancelLoading = true;
                i2 = this.documentAttachType;
                if (i2 == 2 || i2 == 4 || i2 == 1 || i2 == 8) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
                } else {
                    i2 = this.currentMessageObject.type;
                    if (i2 == 0 || i2 == 1 || i2 == 8 || i2 == 5) {
                        ImageLoader.getInstance().cancelForceLoadingForImageReceiver(this.photoImage);
                        this.photoImage.cancelLoadImage();
                    } else if (i2 == 9) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.messageOwner.media.document);
                    }
                }
                this.buttonState = 0;
                if (z2) {
                    this.videoRadialProgress.setIcon(2, false, z3);
                } else {
                    this.radialProgress.setIcon(getIconForCurrentState(), false, z3);
                }
                invalidate();
            } else if (this.radialProgress.getIcon() != 6) {
                this.delegate.didPressCancelSendButton(this);
            }
        }
    }

    public void onFailedDownload(String str, boolean z) {
        int i = this.documentAttachType;
        boolean z2 = i == 3 || i == 5;
        updateButtonState(true, z2, false);
    }

    /* JADX WARNING: Missing block: B:33:0x009b, code skipped:
            if ((r1 & 2) != 0) goto L_0x009d;
     */
    public void onSuccessDownload(java.lang.String r22) {
        /*
        r21 = this;
        r0 = r21;
        r1 = r0.documentAttachType;
        r2 = 0;
        r3 = 1;
        r4 = 3;
        if (r1 == r4) goto L_0x01aa;
    L_0x0009:
        r4 = 5;
        if (r1 != r4) goto L_0x000e;
    L_0x000c:
        goto L_0x01aa;
    L_0x000e:
        r1 = r0.drawVideoImageButton;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        if (r1 == 0) goto L_0x001a;
    L_0x0014:
        r1 = r0.videoRadialProgress;
        r1.setProgress(r4, r3);
        goto L_0x001f;
    L_0x001a:
        r1 = r0.radialProgress;
        r1.setProgress(r4, r3);
    L_0x001f:
        r1 = r0.currentMessageObject;
        r1 = r1.needDrawBluredPreview();
        r5 = 2;
        if (r1 != 0) goto L_0x0165;
    L_0x0028:
        r1 = r0.autoPlayingMedia;
        if (r1 != 0) goto L_0x0165;
    L_0x002c:
        r1 = r0.documentAttach;
        if (r1 == 0) goto L_0x0165;
    L_0x0030:
        r6 = r0.documentAttachType;
        r7 = 7;
        r8 = "s";
        if (r6 != r7) goto L_0x0089;
    L_0x0037:
        r9 = r0.photoImage;
        r10 = org.telegram.messenger.ImageLocation.getForDocument(r1);
        r1 = r0.currentPhotoObject;
        r6 = r0.photoParentObject;
        r12 = org.telegram.messenger.ImageLocation.getForObject(r1, r6);
        r1 = r0.currentPhotoObject;
        r6 = r1 instanceof org.telegram.tgnet.TLRPC.TL_photoStrippedSize;
        if (r6 != 0) goto L_0x0059;
    L_0x004b:
        if (r1 == 0) goto L_0x0056;
    L_0x004d:
        r1 = r1.type;
        r1 = r8.equals(r1);
        if (r1 == 0) goto L_0x0056;
    L_0x0055:
        goto L_0x0059;
    L_0x0056:
        r1 = r0.currentPhotoFilter;
        goto L_0x005b;
    L_0x0059:
        r1 = r0.currentPhotoFilterThumb;
    L_0x005b:
        r13 = r1;
        r1 = r0.currentPhotoObjectThumb;
        r6 = r0.photoParentObject;
        r14 = org.telegram.messenger.ImageLocation.getForObject(r1, r6);
        r15 = r0.currentPhotoFilterThumb;
        r16 = 0;
        r1 = r0.documentAttach;
        r1 = r1.size;
        r18 = 0;
        r6 = r0.currentMessageObject;
        r20 = 0;
        r11 = "g";
        r17 = r1;
        r19 = r6;
        r9.setImage(r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20);
        r1 = r0.photoImage;
        r1.setAllowStartAnimation(r3);
        r1 = r0.photoImage;
        r1.startAnimation();
        r0.autoPlayingMedia = r3;
        goto L_0x0165;
    L_0x0089:
        r1 = org.telegram.messenger.SharedConfig.autoplayVideo;
        if (r1 == 0) goto L_0x0100;
    L_0x008d:
        r1 = 4;
        if (r6 != r1) goto L_0x0100;
    L_0x0090:
        r1 = r0.currentPosition;
        if (r1 == 0) goto L_0x009d;
    L_0x0094:
        r1 = r1.flags;
        r6 = r1 & 1;
        if (r6 == 0) goto L_0x0100;
    L_0x009a:
        r1 = r1 & r5;
        if (r1 == 0) goto L_0x0100;
    L_0x009d:
        r0.animatingNoSound = r5;
        r9 = r0.photoImage;
        r1 = r0.documentAttach;
        r10 = org.telegram.messenger.ImageLocation.getForDocument(r1);
        r1 = r0.currentPhotoObject;
        r6 = r0.photoParentObject;
        r12 = org.telegram.messenger.ImageLocation.getForObject(r1, r6);
        r1 = r0.currentPhotoObject;
        r6 = r1 instanceof org.telegram.tgnet.TLRPC.TL_photoStrippedSize;
        if (r6 != 0) goto L_0x00c3;
    L_0x00b5:
        if (r1 == 0) goto L_0x00c0;
    L_0x00b7:
        r1 = r1.type;
        r1 = r8.equals(r1);
        if (r1 == 0) goto L_0x00c0;
    L_0x00bf:
        goto L_0x00c3;
    L_0x00c0:
        r1 = r0.currentPhotoFilter;
        goto L_0x00c5;
    L_0x00c3:
        r1 = r0.currentPhotoFilterThumb;
    L_0x00c5:
        r13 = r1;
        r1 = r0.currentPhotoObjectThumb;
        r6 = r0.photoParentObject;
        r14 = org.telegram.messenger.ImageLocation.getForObject(r1, r6);
        r15 = r0.currentPhotoFilterThumb;
        r16 = 0;
        r1 = r0.documentAttach;
        r1 = r1.size;
        r18 = 0;
        r6 = r0.currentMessageObject;
        r20 = 0;
        r11 = "g";
        r17 = r1;
        r19 = r6;
        r9.setImage(r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20);
        r1 = r0.currentMessageObject;
        r1 = org.telegram.p004ui.PhotoViewer.isPlayingMessage(r1);
        if (r1 != 0) goto L_0x00f8;
    L_0x00ed:
        r1 = r0.photoImage;
        r1.setAllowStartAnimation(r3);
        r1 = r0.photoImage;
        r1.startAnimation();
        goto L_0x00fd;
    L_0x00f8:
        r1 = r0.photoImage;
        r1.setAllowStartAnimation(r2);
    L_0x00fd:
        r0.autoPlayingMedia = r3;
        goto L_0x0165;
    L_0x0100:
        r1 = r0.documentAttachType;
        if (r1 != r5) goto L_0x0165;
    L_0x0104:
        r9 = r0.photoImage;
        r1 = r0.documentAttach;
        r10 = org.telegram.messenger.ImageLocation.getForDocument(r1);
        r1 = r0.currentPhotoObject;
        r6 = r0.photoParentObject;
        r12 = org.telegram.messenger.ImageLocation.getForObject(r1, r6);
        r1 = r0.currentPhotoObject;
        r6 = r1 instanceof org.telegram.tgnet.TLRPC.TL_photoStrippedSize;
        if (r6 != 0) goto L_0x0128;
    L_0x011a:
        if (r1 == 0) goto L_0x0125;
    L_0x011c:
        r1 = r1.type;
        r1 = r8.equals(r1);
        if (r1 == 0) goto L_0x0125;
    L_0x0124:
        goto L_0x0128;
    L_0x0125:
        r1 = r0.currentPhotoFilter;
        goto L_0x012a;
    L_0x0128:
        r1 = r0.currentPhotoFilterThumb;
    L_0x012a:
        r13 = r1;
        r1 = r0.currentPhotoObjectThumb;
        r6 = r0.photoParentObject;
        r14 = org.telegram.messenger.ImageLocation.getForObject(r1, r6);
        r15 = r0.currentPhotoFilterThumb;
        r16 = 0;
        r1 = r0.documentAttach;
        r1 = r1.size;
        r18 = 0;
        r6 = r0.currentMessageObject;
        r20 = 0;
        r11 = "g";
        r17 = r1;
        r19 = r6;
        r9.setImage(r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20);
        r1 = org.telegram.messenger.SharedConfig.autoplayGifs;
        if (r1 == 0) goto L_0x0159;
    L_0x014e:
        r1 = r0.photoImage;
        r1.setAllowStartAnimation(r3);
        r1 = r0.photoImage;
        r1.startAnimation();
        goto L_0x0163;
    L_0x0159:
        r1 = r0.photoImage;
        r1.setAllowStartAnimation(r2);
        r1 = r0.photoImage;
        r1.stopAnimation();
    L_0x0163:
        r0.autoPlayingMedia = r3;
    L_0x0165:
        r1 = r0.currentMessageObject;
        r6 = r1.type;
        if (r6 != 0) goto L_0x0193;
    L_0x016b:
        r6 = r0.autoPlayingMedia;
        if (r6 != 0) goto L_0x017f;
    L_0x016f:
        r6 = r0.documentAttachType;
        if (r6 != r5) goto L_0x017f;
    L_0x0173:
        r1 = r1.gifState;
        r1 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1));
        if (r1 == 0) goto L_0x017f;
    L_0x0179:
        r0.buttonState = r5;
        r0.didPressButton(r3, r2);
        goto L_0x01b0;
    L_0x017f:
        r1 = r0.photoNotSet;
        if (r1 != 0) goto L_0x0187;
    L_0x0183:
        r0.updateButtonState(r2, r3, r2);
        goto L_0x01b0;
    L_0x0187:
        r1 = r0.currentMessageObject;
        r2 = r0.currentMessagesGroup;
        r3 = r0.pinnedBottom;
        r4 = r0.pinnedTop;
        r0.setMessageObject(r1, r2, r3, r4);
        goto L_0x01b0;
    L_0x0193:
        r1 = r0.photoNotSet;
        if (r1 != 0) goto L_0x019a;
    L_0x0197:
        r0.updateButtonState(r2, r3, r2);
    L_0x019a:
        r1 = r0.photoNotSet;
        if (r1 == 0) goto L_0x01b0;
    L_0x019e:
        r1 = r0.currentMessageObject;
        r2 = r0.currentMessagesGroup;
        r3 = r0.pinnedBottom;
        r4 = r0.pinnedTop;
        r0.setMessageObject(r1, r2, r3, r4);
        goto L_0x01b0;
    L_0x01aa:
        r0.updateButtonState(r2, r3, r2);
        r21.updateWaveform();
    L_0x01b0:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.onSuccessDownload(java.lang.String):void");
    }

    /* JADX WARNING: Missing block: B:14:0x001e, code skipped:
            if (r1 != 6) goto L_0x0020;
     */
    public void didSetImage(org.telegram.messenger.ImageReceiver r1, boolean r2, boolean r3) {
        /*
        r0 = this;
        r1 = r0.currentMessageObject;
        if (r1 == 0) goto L_0x002e;
    L_0x0004:
        if (r2 == 0) goto L_0x002e;
    L_0x0006:
        if (r3 != 0) goto L_0x002e;
    L_0x0008:
        r2 = r1.mediaExists;
        if (r2 != 0) goto L_0x002e;
    L_0x000c:
        r2 = r1.attachPathExists;
        if (r2 != 0) goto L_0x002e;
    L_0x0010:
        r1 = r1.type;
        r2 = 1;
        if (r1 != 0) goto L_0x0020;
    L_0x0015:
        r1 = r0.documentAttachType;
        r3 = 8;
        if (r1 == r3) goto L_0x0026;
    L_0x001b:
        if (r1 == 0) goto L_0x0026;
    L_0x001d:
        r3 = 6;
        if (r1 == r3) goto L_0x0026;
    L_0x0020:
        r1 = r0.currentMessageObject;
        r1 = r1.type;
        if (r1 != r2) goto L_0x002e;
    L_0x0026:
        r1 = r0.currentMessageObject;
        r1.mediaExists = r2;
        r1 = 0;
        r0.updateButtonState(r1, r2, r1);
    L_0x002e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.didSetImage(org.telegram.messenger.ImageReceiver, boolean, boolean):void");
    }

    public void onProgressDownload(String str, float f) {
        if (this.drawVideoImageButton) {
            this.videoRadialProgress.setProgress(f, true);
        } else {
            this.radialProgress.setProgress(f, true);
        }
        int i = this.documentAttachType;
        if (i == 3 || i == 5) {
            if (this.hasMiniProgress != 0) {
                if (this.miniButtonState != 1) {
                    updateButtonState(false, false, false);
                }
            } else if (this.buttonState != 4) {
                updateButtonState(false, false, false);
            }
        } else if (this.hasMiniProgress != 0) {
            if (this.miniButtonState != 1) {
                updateButtonState(false, false, false);
            }
        } else if (this.buttonState != 1) {
            updateButtonState(false, false, false);
        }
    }

    public void onProgressUpload(String str, float f, boolean z) {
        this.radialProgress.setProgress(f, true);
        if (f == 1.0f && this.currentPosition != null && SendMessagesHelper.getInstance(this.currentAccount).isSendingMessage(this.currentMessageObject.getId()) && this.buttonState == 1) {
            this.drawRadialCheckBackground = true;
            getIconForCurrentState();
            this.radialProgress.setIcon(6, false, true);
        }
    }

    public void onProvideStructure(ViewStructure viewStructure) {
        super.onProvideStructure(viewStructure);
        if (this.allowAssistant && VERSION.SDK_INT >= 23) {
            CharSequence charSequence = this.currentMessageObject.messageText;
            if (charSequence == null || charSequence.length() <= 0) {
                charSequence = this.currentMessageObject.caption;
                if (charSequence != null && charSequence.length() > 0) {
                    viewStructure.setText(this.currentMessageObject.caption);
                    return;
                }
                return;
            }
            viewStructure.setText(this.currentMessageObject.messageText);
        }
    }

    public void setDelegate(ChatMessageCellDelegate chatMessageCellDelegate) {
        this.delegate = chatMessageCellDelegate;
    }

    public void setAllowAssistant(boolean z) {
        this.allowAssistant = z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:53:0x0107  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00d8  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x012e  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x014a  */
    /* JADX WARNING: Removed duplicated region for block: B:82:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0187  */
    /* JADX WARNING: Missing block: B:48:0x00d0, code skipped:
            if (r12.isEditing() == false) goto L_0x00d3;
     */
    private void measureTime(org.telegram.messenger.MessageObject r12) {
        /*
        r11 = this;
        r0 = r12.messageOwner;
        r1 = r0.post_author;
        r2 = "\n";
        r3 = "";
        r4 = 0;
        if (r1 == 0) goto L_0x0010;
    L_0x000b:
        r0 = r1.replace(r2, r3);
        goto L_0x0053;
    L_0x0010:
        r0 = r0.fwd_from;
        if (r0 == 0) goto L_0x001d;
    L_0x0014:
        r0 = r0.post_author;
        if (r0 == 0) goto L_0x001d;
    L_0x0018:
        r0 = r0.replace(r2, r3);
        goto L_0x0053;
    L_0x001d:
        r0 = r12.isOutOwner();
        if (r0 != 0) goto L_0x0052;
    L_0x0023:
        r0 = r12.messageOwner;
        r1 = r0.from_id;
        if (r1 <= 0) goto L_0x0052;
    L_0x0029:
        r0 = r0.post;
        if (r0 == 0) goto L_0x0052;
    L_0x002d:
        r0 = r11.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r1 = r12.messageOwner;
        r1 = r1.from_id;
        r1 = java.lang.Integer.valueOf(r1);
        r0 = r0.getUser(r1);
        if (r0 == 0) goto L_0x0052;
    L_0x0041:
        r1 = r0.first_name;
        r0 = r0.last_name;
        r0 = org.telegram.messenger.ContactsController.formatName(r1, r0);
        r1 = 10;
        r2 = 32;
        r0 = r0.replace(r1, r2);
        goto L_0x0053;
    L_0x0052:
        r0 = r4;
    L_0x0053:
        r1 = r11.currentMessageObject;
        r1 = r1.isFromUser();
        if (r1 == 0) goto L_0x006e;
    L_0x005b:
        r1 = r11.currentAccount;
        r1 = org.telegram.messenger.MessagesController.getInstance(r1);
        r2 = r12.messageOwner;
        r2 = r2.from_id;
        r2 = java.lang.Integer.valueOf(r2);
        r1 = r1.getUser(r2);
        goto L_0x006f;
    L_0x006e:
        r1 = r4;
    L_0x006f:
        r2 = r12.isLiveLocation();
        r5 = 1;
        r6 = 0;
        if (r2 != 0) goto L_0x00d3;
    L_0x0077:
        r7 = r12.getDialogId();
        r9 = 777000; // 0xbdb28 float:1.088809E-39 double:3.83889E-318;
        r2 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r2 == 0) goto L_0x00d3;
    L_0x0082:
        r2 = r12.messageOwner;
        r7 = r2.via_bot_id;
        if (r7 != 0) goto L_0x00d3;
    L_0x0088:
        r2 = r2.via_bot_name;
        if (r2 != 0) goto L_0x00d3;
    L_0x008c:
        if (r1 == 0) goto L_0x0093;
    L_0x008e:
        r1 = r1.bot;
        if (r1 == 0) goto L_0x0093;
    L_0x0092:
        goto L_0x00d3;
    L_0x0093:
        r1 = r11.currentPosition;
        r2 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        if (r1 == 0) goto L_0x00c5;
    L_0x009a:
        r1 = r11.currentMessagesGroup;
        if (r1 != 0) goto L_0x009f;
    L_0x009e:
        goto L_0x00c5;
    L_0x009f:
        r1 = r1.messages;
        r1 = r1.size();
        r7 = 0;
    L_0x00a6:
        if (r7 >= r1) goto L_0x00d3;
    L_0x00a8:
        r8 = r11.currentMessagesGroup;
        r8 = r8.messages;
        r8 = r8.get(r7);
        r8 = (org.telegram.messenger.MessageObject) r8;
        r9 = r8.messageOwner;
        r9 = r9.flags;
        r9 = r9 & r2;
        if (r9 != 0) goto L_0x00c3;
    L_0x00b9:
        r8 = r8.isEditing();
        if (r8 == 0) goto L_0x00c0;
    L_0x00bf:
        goto L_0x00c3;
    L_0x00c0:
        r7 = r7 + 1;
        goto L_0x00a6;
    L_0x00c3:
        r1 = 1;
        goto L_0x00d4;
    L_0x00c5:
        r1 = r12.messageOwner;
        r1 = r1.flags;
        r1 = r1 & r2;
        if (r1 != 0) goto L_0x00c3;
    L_0x00cc:
        r1 = r12.isEditing();
        if (r1 == 0) goto L_0x00d3;
    L_0x00d2:
        goto L_0x00c3;
    L_0x00d3:
        r1 = 0;
    L_0x00d4:
        r7 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        if (r1 == 0) goto L_0x0107;
    L_0x00d8:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = 2131559327; // 0x7f0d039f float:1.8743995E38 double:1.0531302355E-314;
        r9 = "EditedMessage";
        r2 = org.telegram.messenger.LocaleController.getString(r9, r2);
        r1.append(r2);
        r2 = " ";
        r1.append(r2);
        r2 = org.telegram.messenger.LocaleController.getInstance();
        r2 = r2.formatterDay;
        r9 = r12.messageOwner;
        r9 = r9.date;
        r9 = (long) r9;
        r9 = r9 * r7;
        r2 = r2.format(r9);
        r1.append(r2);
        r1 = r1.toString();
        goto L_0x0118;
    L_0x0107:
        r1 = org.telegram.messenger.LocaleController.getInstance();
        r1 = r1.formatterDay;
        r2 = r12.messageOwner;
        r2 = r2.date;
        r9 = (long) r2;
        r9 = r9 * r7;
        r1 = r1.format(r9);
    L_0x0118:
        if (r0 == 0) goto L_0x012e;
    L_0x011a:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r7 = ", ";
        r2.append(r7);
        r2.append(r1);
        r1 = r2.toString();
        r11.currentTimeString = r1;
        goto L_0x0130;
    L_0x012e:
        r11.currentTimeString = r1;
    L_0x0130:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_timePaint;
        r2 = r11.currentTimeString;
        r1 = r1.measureText(r2);
        r1 = (double) r1;
        r1 = java.lang.Math.ceil(r1);
        r1 = (int) r1;
        r11.timeWidth = r1;
        r11.timeTextWidth = r1;
        r1 = r12.messageOwner;
        r2 = r1.flags;
        r2 = r2 & 1024;
        if (r2 == 0) goto L_0x0185;
    L_0x014a:
        r2 = new java.lang.Object[r5];
        r1 = r1.views;
        r1 = java.lang.Math.max(r5, r1);
        r1 = org.telegram.messenger.LocaleController.formatShortNumber(r1, r4);
        r2[r6] = r1;
        r1 = "%s";
        r1 = java.lang.String.format(r1, r2);
        r11.currentViewsString = r1;
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_timePaint;
        r2 = r11.currentViewsString;
        r1 = r1.measureText(r2);
        r1 = (double) r1;
        r1 = java.lang.Math.ceil(r1);
        r1 = (int) r1;
        r11.viewsTextWidth = r1;
        r1 = r11.timeWidth;
        r2 = r11.viewsTextWidth;
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_msgInViewsDrawable;
        r4 = r4.getIntrinsicWidth();
        r2 = r2 + r4;
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r2 = r2 + r4;
        r1 = r1 + r2;
        r11.timeWidth = r1;
    L_0x0185:
        if (r0 == 0) goto L_0x01f0;
    L_0x0187:
        r1 = r11.availableTimeWidth;
        if (r1 != 0) goto L_0x0193;
    L_0x018b:
        r1 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r11.availableTimeWidth = r1;
    L_0x0193:
        r1 = r11.availableTimeWidth;
        r2 = r11.timeWidth;
        r1 = r1 - r2;
        r2 = r12.isOutOwner();
        if (r2 == 0) goto L_0x01b1;
    L_0x019e:
        r12 = r12.type;
        r2 = 5;
        if (r12 != r2) goto L_0x01aa;
    L_0x01a3:
        r12 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        goto L_0x01b0;
    L_0x01aa:
        r12 = 1119879168; // 0x42c00000 float:96.0 double:5.532938244E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
    L_0x01b0:
        r1 = r1 - r12;
    L_0x01b1:
        r12 = org.telegram.p004ui.ActionBar.Theme.chat_timePaint;
        r2 = r0.length();
        r12 = r12.measureText(r0, r6, r2);
        r4 = (double) r12;
        r4 = java.lang.Math.ceil(r4);
        r12 = (int) r4;
        if (r12 <= r1) goto L_0x01d2;
    L_0x01c3:
        if (r1 > 0) goto L_0x01c7;
    L_0x01c5:
        r12 = 0;
        goto L_0x01d3;
    L_0x01c7:
        r12 = org.telegram.p004ui.ActionBar.Theme.chat_timePaint;
        r2 = (float) r1;
        r3 = android.text.TextUtils.TruncateAt.END;
        r3 = android.text.TextUtils.ellipsize(r0, r12, r2, r3);
        r12 = r1;
        goto L_0x01d3;
    L_0x01d2:
        r3 = r0;
    L_0x01d3:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r0.append(r3);
        r1 = r11.currentTimeString;
        r0.append(r1);
        r0 = r0.toString();
        r11.currentTimeString = r0;
        r0 = r11.timeTextWidth;
        r0 = r0 + r12;
        r11.timeTextWidth = r0;
        r0 = r11.timeWidth;
        r0 = r0 + r12;
        r11.timeWidth = r0;
    L_0x01f0:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.measureTime(org.telegram.messenger.MessageObject):void");
    }

    private boolean isDrawSelectionBackground() {
        return (isPressed() && this.isCheckPressed) || ((!this.isCheckPressed && this.isPressed) || this.isHighlighted);
    }

    private boolean isOpenChatByShare(MessageObject messageObject) {
        MessageFwdHeader messageFwdHeader = messageObject.messageOwner.fwd_from;
        return (messageFwdHeader == null || messageFwdHeader.saved_from_peer == null) ? false : true;
    }

    private boolean checkNeedDrawShareButton(MessageObject messageObject) {
        GroupedMessagePosition groupedMessagePosition = this.currentPosition;
        if (groupedMessagePosition != null && !groupedMessagePosition.last) {
            return false;
        }
        if (!(messageObject.messageOwner.fwd_from == null || messageObject.isOutOwner() || messageObject.messageOwner.fwd_from.saved_from_peer == null || messageObject.getDialogId() != ((long) UserConfig.getInstance(this.currentAccount).getClientUserId()))) {
            this.drwaShareGoIcon = true;
        }
        return messageObject.needDrawShareButton();
    }

    public boolean isInsideBackground(float f, float f2) {
        if (this.currentBackgroundDrawable != null) {
            int i = this.backgroundDrawableLeft;
            if (f >= ((float) i) && f <= ((float) (i + this.backgroundDrawableRight))) {
                return true;
            }
        }
        return false;
    }

    private void updateCurrentUserAndChat() {
        MessagesController instance = MessagesController.getInstance(this.currentAccount);
        MessageFwdHeader messageFwdHeader = this.currentMessageObject.messageOwner.fwd_from;
        int clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        if (messageFwdHeader == null || messageFwdHeader.channel_id == 0 || this.currentMessageObject.getDialogId() != ((long) clientUserId)) {
            if (messageFwdHeader != null) {
                Peer peer = messageFwdHeader.saved_from_peer;
                if (peer != null) {
                    clientUserId = peer.user_id;
                    int i;
                    if (clientUserId != 0) {
                        i = messageFwdHeader.from_id;
                        if (i != 0) {
                            this.currentUser = instance.getUser(Integer.valueOf(i));
                            return;
                        } else {
                            this.currentUser = instance.getUser(Integer.valueOf(clientUserId));
                            return;
                        }
                    } else if (peer.channel_id != 0) {
                        if (this.currentMessageObject.isSavedFromMegagroup()) {
                            clientUserId = messageFwdHeader.from_id;
                            if (clientUserId != 0) {
                                this.currentUser = instance.getUser(Integer.valueOf(clientUserId));
                                return;
                            }
                        }
                        this.currentChat = instance.getChat(Integer.valueOf(messageFwdHeader.saved_from_peer.channel_id));
                        return;
                    } else {
                        clientUserId = peer.chat_id;
                        if (clientUserId != 0) {
                            i = messageFwdHeader.from_id;
                            if (i != 0) {
                                this.currentUser = instance.getUser(Integer.valueOf(i));
                                return;
                            } else {
                                this.currentChat = instance.getChat(Integer.valueOf(clientUserId));
                                return;
                            }
                        }
                        return;
                    }
                }
            }
            if (messageFwdHeader != null && messageFwdHeader.from_id != 0 && messageFwdHeader.channel_id == 0 && this.currentMessageObject.getDialogId() == ((long) clientUserId)) {
                this.currentUser = instance.getUser(Integer.valueOf(messageFwdHeader.from_id));
                return;
            } else if (messageFwdHeader != null && !TextUtils.isEmpty(messageFwdHeader.from_name) && this.currentMessageObject.getDialogId() == ((long) clientUserId)) {
                this.currentUser = new TL_user();
                this.currentUser.first_name = messageFwdHeader.from_name;
                return;
            } else if (this.currentMessageObject.isFromUser()) {
                this.currentUser = instance.getUser(Integer.valueOf(this.currentMessageObject.messageOwner.from_id));
                return;
            } else {
                Message message = this.currentMessageObject.messageOwner;
                clientUserId = message.from_id;
                if (clientUserId < 0) {
                    this.currentChat = instance.getChat(Integer.valueOf(-clientUserId));
                    return;
                } else if (message.post) {
                    this.currentChat = instance.getChat(Integer.valueOf(message.to_id.channel_id));
                    return;
                } else {
                    return;
                }
            }
        }
        this.currentChat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(messageFwdHeader.channel_id));
    }

    /* JADX WARNING: Removed duplicated region for block: B:59:0x0194  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0232  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0219  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x0245  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0242  */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x032d  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x0250  */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x03b7 A:{Catch:{ Exception -> 0x03bb }} */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x0386 A:{Catch:{ Exception -> 0x03bb }} */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x03cb  */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x03c7  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x03f7  */
    /* JADX WARNING: Removed duplicated region for block: B:144:0x0415  */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x0433  */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x0465  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x0445  */
    /* JADX WARNING: Removed duplicated region for block: B:173:0x0523  */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x04da  */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x060a A:{Catch:{ Exception -> 0x0616 }} */
    /* JADX WARNING: Removed duplicated region for block: B:190:0x0620  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0152  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0167  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x017b A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0194  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x01a0  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x01d1  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0219  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0232  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0242  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x0245  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x0250  */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x032d  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x0352 A:{Catch:{ Exception -> 0x03bb }} */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x0386 A:{Catch:{ Exception -> 0x03bb }} */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x03b7 A:{Catch:{ Exception -> 0x03bb }} */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x03c7  */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x03cb  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x03f7  */
    /* JADX WARNING: Removed duplicated region for block: B:144:0x0415  */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x0433  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x0445  */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x0465  */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x04da  */
    /* JADX WARNING: Removed duplicated region for block: B:173:0x0523  */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x060a A:{Catch:{ Exception -> 0x0616 }} */
    /* JADX WARNING: Removed duplicated region for block: B:190:0x0620  */
    private void setMessageObjectInternal(org.telegram.messenger.MessageObject r39) {
        /*
        r38 = this;
        r1 = r38;
        r2 = r39;
        r0 = r2.messageOwner;
        r0 = r0.flags;
        r0 = r0 & 1024;
        r3 = 1;
        if (r0 == 0) goto L_0x0022;
    L_0x000d:
        r0 = r1.currentMessageObject;
        r0 = r0.viewsReloaded;
        if (r0 != 0) goto L_0x0022;
    L_0x0013:
        r0 = r1.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r4 = r1.currentMessageObject;
        r0.addToViewsQueue(r4);
        r0 = r1.currentMessageObject;
        r0.viewsReloaded = r3;
    L_0x0022:
        r38.updateCurrentUserAndChat();
        r0 = r1.isAvatarVisible;
        r4 = 0;
        r5 = 0;
        if (r0 == 0) goto L_0x0097;
    L_0x002b:
        r0 = r1.currentUser;
        if (r0 == 0) goto L_0x0055;
    L_0x002f:
        r0 = r0.photo;
        if (r0 == 0) goto L_0x0038;
    L_0x0033:
        r0 = r0.photo_small;
        r1.currentPhoto = r0;
        goto L_0x003a;
    L_0x0038:
        r1.currentPhoto = r4;
    L_0x003a:
        r0 = r1.avatarDrawable;
        r6 = r1.currentUser;
        r0.setInfo(r6);
        r7 = r1.avatarImage;
        r0 = r1.currentUser;
        r8 = org.telegram.messenger.ImageLocation.getForUser(r0, r5);
        r10 = r1.avatarDrawable;
        r11 = 0;
        r12 = r1.currentUser;
        r13 = 0;
        r9 = "50_50";
        r7.setImage(r8, r9, r10, r11, r12, r13);
        goto L_0x0099;
    L_0x0055:
        r0 = r1.currentChat;
        if (r0 == 0) goto L_0x007f;
    L_0x0059:
        r0 = r0.photo;
        if (r0 == 0) goto L_0x0062;
    L_0x005d:
        r0 = r0.photo_small;
        r1.currentPhoto = r0;
        goto L_0x0064;
    L_0x0062:
        r1.currentPhoto = r4;
    L_0x0064:
        r0 = r1.avatarDrawable;
        r6 = r1.currentChat;
        r0.setInfo(r6);
        r7 = r1.avatarImage;
        r0 = r1.currentChat;
        r8 = org.telegram.messenger.ImageLocation.getForChat(r0, r5);
        r10 = r1.avatarDrawable;
        r11 = 0;
        r12 = r1.currentChat;
        r13 = 0;
        r9 = "50_50";
        r7.setImage(r8, r9, r10, r11, r12, r13);
        goto L_0x0099;
    L_0x007f:
        r1.currentPhoto = r4;
        r0 = r1.avatarDrawable;
        r6 = r2.messageOwner;
        r6 = r6.from_id;
        r0.setInfo(r6, r4, r4, r5);
        r7 = r1.avatarImage;
        r8 = 0;
        r9 = 0;
        r10 = r1.avatarDrawable;
        r11 = 0;
        r12 = 0;
        r13 = 0;
        r7.setImage(r8, r9, r10, r11, r12, r13);
        goto L_0x0099;
    L_0x0097:
        r1.currentPhoto = r4;
    L_0x0099:
        r38.measureTime(r39);
        r1.namesOffset = r5;
        r0 = r2.messageOwner;
        r6 = r0.via_bot_id;
        r7 = 2131561039; // 0x7f0d0a4f float:1.8747467E38 double:1.0531310814E-314;
        r8 = "ViaBot";
        r9 = 2;
        if (r6 == 0) goto L_0x0104;
    L_0x00aa:
        r0 = r1.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r6 = r2.messageOwner;
        r6 = r6.via_bot_id;
        r6 = java.lang.Integer.valueOf(r6);
        r0 = r0.getUser(r6);
        if (r0 == 0) goto L_0x014c;
    L_0x00be:
        r6 = r0.username;
        if (r6 == 0) goto L_0x014c;
    L_0x00c2:
        r6 = r6.length();
        if (r6 <= 0) goto L_0x014c;
    L_0x00c8:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r10 = "@";
        r6.append(r10);
        r10 = r0.username;
        r6.append(r10);
        r6 = r6.toString();
        r10 = new java.lang.Object[r9];
        r11 = org.telegram.messenger.LocaleController.getString(r8, r7);
        r10[r5] = r11;
        r10[r3] = r6;
        r11 = " %s <b>%s</b>";
        r10 = java.lang.String.format(r11, r10);
        r10 = org.telegram.messenger.AndroidUtilities.replaceTags(r10);
        r11 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;
        r12 = r10.length();
        r11 = r11.measureText(r10, r5, r12);
        r11 = (double) r11;
        r11 = java.lang.Math.ceil(r11);
        r11 = (int) r11;
        r1.viaWidth = r11;
        r1.currentViaBotUser = r0;
        goto L_0x014e;
    L_0x0104:
        r0 = r0.via_bot_name;
        if (r0 == 0) goto L_0x014c;
    L_0x0108:
        r0 = r0.length();
        if (r0 <= 0) goto L_0x014c;
    L_0x010e:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r6 = "@";
        r0.append(r6);
        r6 = r2.messageOwner;
        r6 = r6.via_bot_name;
        r0.append(r6);
        r0 = r0.toString();
        r6 = new java.lang.Object[r9];
        r10 = org.telegram.messenger.LocaleController.getString(r8, r7);
        r6[r5] = r10;
        r6[r3] = r0;
        r10 = " %s <b>%s</b>";
        r6 = java.lang.String.format(r10, r6);
        r6 = org.telegram.messenger.AndroidUtilities.replaceTags(r6);
        r10 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;
        r11 = r6.length();
        r10 = r10.measureText(r6, r5, r11);
        r10 = (double) r10;
        r10 = java.lang.Math.ceil(r10);
        r10 = (int) r10;
        r1.viaWidth = r10;
        r10 = r6;
        r6 = r0;
        goto L_0x014e;
    L_0x014c:
        r6 = r4;
        r10 = r6;
    L_0x014e:
        r0 = r1.drawName;
        if (r0 == 0) goto L_0x0160;
    L_0x0152:
        r0 = r1.isChat;
        if (r0 == 0) goto L_0x0160;
    L_0x0156:
        r0 = r1.currentMessageObject;
        r0 = r0.isOutOwner();
        if (r0 != 0) goto L_0x0160;
    L_0x015e:
        r0 = 1;
        goto L_0x0161;
    L_0x0160:
        r0 = 0;
    L_0x0161:
        r11 = r2.messageOwner;
        r11 = r11.fwd_from;
        if (r11 == 0) goto L_0x016d;
    L_0x0167:
        r11 = r2.type;
        r12 = 14;
        if (r11 != r12) goto L_0x0171;
    L_0x016d:
        if (r6 == 0) goto L_0x0171;
    L_0x016f:
        r11 = 1;
        goto L_0x0172;
    L_0x0171:
        r11 = 0;
    L_0x0172:
        r13 = "fonts/rmedium.ttf";
        r15 = 32;
        r12 = 10;
        r14 = 5;
        if (r0 != 0) goto L_0x0188;
    L_0x017b:
        if (r11 == 0) goto L_0x017e;
    L_0x017d:
        goto L_0x0188;
    L_0x017e:
        r1.currentNameString = r4;
        r1.nameLayout = r4;
        r1.nameWidth = r5;
        r3 = r4;
        r11 = r8;
        goto L_0x03cc;
    L_0x0188:
        r1.drawNameLayout = r3;
        r4 = r38.getMaxNameWidth();
        r1.nameWidth = r4;
        r4 = r1.nameWidth;
        if (r4 >= 0) goto L_0x019c;
    L_0x0194:
        r4 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r1.nameWidth = r4;
    L_0x019c:
        r4 = r1.isMegagroup;
        if (r4 == 0) goto L_0x01cd;
    L_0x01a0:
        r4 = r1.currentChat;
        if (r4 == 0) goto L_0x01cd;
    L_0x01a4:
        r4 = r1.currentMessageObject;
        r4 = r4.isForwardedChannelPost();
        if (r4 == 0) goto L_0x01cd;
    L_0x01ac:
        r4 = 2131559279; // 0x7f0d036f float:1.8743898E38 double:1.053130212E-314;
        r9 = "DiscussChannel";
        r4 = org.telegram.messenger.LocaleController.getString(r9, r4);
        r9 = org.telegram.p004ui.ActionBar.Theme.chat_adminPaint;
        r9 = r9.measureText(r4);
        r21 = r4;
        r3 = (double) r9;
        r3 = java.lang.Math.ceil(r3);
        r3 = (int) r3;
        r4 = r1.nameWidth;
        r4 = r4 - r3;
        r1.nameWidth = r4;
        r23 = r21;
        r21 = r8;
        goto L_0x0217;
    L_0x01cd:
        r3 = r1.currentUser;
        if (r3 == 0) goto L_0x0212;
    L_0x01d1:
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 != 0) goto L_0x0212;
    L_0x01d9:
        r3 = r1.currentMessageObject;
        r3 = r3.isAnyKindOfSticker();
        if (r3 != 0) goto L_0x0212;
    L_0x01e1:
        r3 = r1.currentMessageObject;
        r3 = r3.type;
        if (r3 == r14) goto L_0x0212;
    L_0x01e7:
        r3 = r1.delegate;
        r4 = r1.currentUser;
        r4 = r4.f534id;
        r3 = r3.isChatAdminCell(r4);
        if (r3 == 0) goto L_0x0212;
    L_0x01f3:
        r3 = 2131559021; // 0x7f0d026d float:1.8743374E38 double:1.0531300844E-314;
        r4 = "ChatAdmin";
        r4 = org.telegram.messenger.LocaleController.getString(r4, r3);
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_adminPaint;
        r3 = r3.measureText(r4);
        r21 = r8;
        r7 = (double) r3;
        r7 = java.lang.Math.ceil(r7);
        r3 = (int) r7;
        r7 = r1.nameWidth;
        r7 = r7 - r3;
        r1.nameWidth = r7;
        r23 = r4;
        goto L_0x0217;
    L_0x0212:
        r21 = r8;
        r3 = 0;
        r23 = 0;
    L_0x0217:
        if (r0 == 0) goto L_0x0232;
    L_0x0219:
        r0 = r1.currentUser;
        if (r0 == 0) goto L_0x0224;
    L_0x021d:
        r0 = org.telegram.messenger.UserObject.getUserName(r0);
        r1.currentNameString = r0;
        goto L_0x0236;
    L_0x0224:
        r0 = r1.currentChat;
        if (r0 == 0) goto L_0x022d;
    L_0x0228:
        r0 = r0.title;
        r1.currentNameString = r0;
        goto L_0x0236;
    L_0x022d:
        r0 = "DELETED";
        r1.currentNameString = r0;
        goto L_0x0236;
    L_0x0232:
        r0 = "";
        r1.currentNameString = r0;
    L_0x0236:
        r0 = r1.currentNameString;
        r0 = r0.replace(r12, r15);
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_namePaint;
        r7 = r1.nameWidth;
        if (r11 == 0) goto L_0x0245;
    L_0x0242:
        r8 = r1.viaWidth;
        goto L_0x0246;
    L_0x0245:
        r8 = 0;
    L_0x0246:
        r7 = r7 - r8;
        r7 = (float) r7;
        r8 = android.text.TextUtils.TruncateAt.END;
        r0 = android.text.TextUtils.ellipsize(r0, r4, r7, r8);
        if (r11 == 0) goto L_0x032d;
    L_0x0250:
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_namePaint;
        r7 = r0.length();
        r4 = r4.measureText(r0, r5, r7);
        r7 = (double) r4;
        r7 = java.lang.Math.ceil(r7);
        r4 = (int) r7;
        r1.viaNameWidth = r4;
        r4 = r1.viaNameWidth;
        if (r4 == 0) goto L_0x026f;
    L_0x0266:
        r7 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r4 = r4 + r7;
        r1.viaNameWidth = r4;
    L_0x026f:
        r4 = r1.currentMessageObject;
        r4 = r4.shouldDrawWithoutBackground();
        if (r4 == 0) goto L_0x027e;
    L_0x0277:
        r4 = "chat_stickerViaBotNameText";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        goto L_0x028f;
    L_0x027e:
        r4 = r1.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 == 0) goto L_0x0289;
    L_0x0286:
        r4 = "chat_outViaBotNameText";
        goto L_0x028b;
    L_0x0289:
        r4 = "chat_inViaBotNameText";
    L_0x028b:
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
    L_0x028f:
        r7 = r1.currentNameString;
        r7 = r7.length();
        if (r7 <= 0) goto L_0x02e8;
    L_0x0297:
        r7 = new android.text.SpannableStringBuilder;
        r8 = 3;
        r8 = new java.lang.Object[r8];
        r8[r5] = r0;
        r11 = r21;
        r9 = 2131561039; // 0x7f0d0a4f float:1.8747467E38 double:1.0531310814E-314;
        r21 = org.telegram.messenger.LocaleController.getString(r11, r9);
        r20 = 1;
        r8[r20] = r21;
        r17 = 2;
        r8[r17] = r6;
        r9 = "%s %s %s";
        r8 = java.lang.String.format(r9, r8);
        r7.<init>(r8);
        r8 = new org.telegram.ui.Components.TypefaceSpan;
        r9 = android.graphics.Typeface.DEFAULT;
        r8.<init>(r9, r5, r4);
        r9 = r0.length();
        r9 = r9 + 1;
        r22 = r0.length();
        r16 = 4;
        r12 = r22 + 4;
        r15 = 33;
        r7.setSpan(r8, r9, r12, r15);
        r8 = new org.telegram.ui.Components.TypefaceSpan;
        r9 = org.telegram.messenger.AndroidUtilities.getTypeface(r13);
        r8.<init>(r9, r5, r4);
        r0 = r0.length();
        r0 = r0 + r14;
        r4 = r7.length();
        r7.setSpan(r8, r0, r4, r15);
        goto L_0x0321;
    L_0x02e8:
        r11 = r21;
        r7 = new android.text.SpannableStringBuilder;
        r8 = 2;
        r0 = new java.lang.Object[r8];
        r8 = 2131561039; // 0x7f0d0a4f float:1.8747467E38 double:1.0531310814E-314;
        r12 = org.telegram.messenger.LocaleController.getString(r11, r8);
        r0[r5] = r12;
        r8 = 1;
        r0[r8] = r6;
        r8 = "%s %s";
        r0 = java.lang.String.format(r8, r0);
        r7.<init>(r0);
        r0 = new org.telegram.ui.Components.TypefaceSpan;
        r8 = android.graphics.Typeface.DEFAULT;
        r0.<init>(r8, r5, r4);
        r8 = 4;
        r12 = 33;
        r7.setSpan(r0, r5, r8, r12);
        r0 = new org.telegram.ui.Components.TypefaceSpan;
        r15 = org.telegram.messenger.AndroidUtilities.getTypeface(r13);
        r0.<init>(r15, r5, r4);
        r4 = r7.length();
        r7.setSpan(r0, r8, r4, r12);
    L_0x0321:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_namePaint;
        r4 = r1.nameWidth;
        r4 = (float) r4;
        r8 = android.text.TextUtils.TruncateAt.END;
        r0 = android.text.TextUtils.ellipsize(r7, r0, r4, r8);
        goto L_0x032f;
    L_0x032d:
        r11 = r21;
    L_0x032f:
        r31 = r0;
        r0 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x03bb }
        r32 = org.telegram.p004ui.ActionBar.Theme.chat_namePaint;	 Catch:{ Exception -> 0x03bb }
        r4 = r1.nameWidth;	 Catch:{ Exception -> 0x03bb }
        r7 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = org.telegram.messenger.AndroidUtilities.m26dp(r7);	 Catch:{ Exception -> 0x03bb }
        r33 = r4 + r8;
        r34 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x03bb }
        r35 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r36 = 0;
        r37 = 0;
        r30 = r0;
        r30.<init>(r31, r32, r33, r34, r35, r36, r37);	 Catch:{ Exception -> 0x03bb }
        r1.nameLayout = r0;	 Catch:{ Exception -> 0x03bb }
        r0 = r1.nameLayout;	 Catch:{ Exception -> 0x03bb }
        if (r0 == 0) goto L_0x0382;
    L_0x0352:
        r0 = r1.nameLayout;	 Catch:{ Exception -> 0x03bb }
        r0 = r0.getLineCount();	 Catch:{ Exception -> 0x03bb }
        if (r0 <= 0) goto L_0x0382;
    L_0x035a:
        r0 = r1.nameLayout;	 Catch:{ Exception -> 0x03bb }
        r0 = r0.getLineWidth(r5);	 Catch:{ Exception -> 0x03bb }
        r7 = (double) r0;	 Catch:{ Exception -> 0x03bb }
        r7 = java.lang.Math.ceil(r7);	 Catch:{ Exception -> 0x03bb }
        r0 = (int) r7;	 Catch:{ Exception -> 0x03bb }
        r1.nameWidth = r0;	 Catch:{ Exception -> 0x03bb }
        r0 = r39.isAnyKindOfSticker();	 Catch:{ Exception -> 0x03bb }
        if (r0 != 0) goto L_0x0379;
    L_0x036e:
        r0 = r1.namesOffset;	 Catch:{ Exception -> 0x03bb }
        r4 = 1100480512; // 0x41980000 float:19.0 double:5.43709615E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);	 Catch:{ Exception -> 0x03bb }
        r0 = r0 + r4;
        r1.namesOffset = r0;	 Catch:{ Exception -> 0x03bb }
    L_0x0379:
        r0 = r1.nameLayout;	 Catch:{ Exception -> 0x03bb }
        r0 = r0.getLineLeft(r5);	 Catch:{ Exception -> 0x03bb }
        r1.nameOffsetX = r0;	 Catch:{ Exception -> 0x03bb }
        goto L_0x0384;
    L_0x0382:
        r1.nameWidth = r5;	 Catch:{ Exception -> 0x03bb }
    L_0x0384:
        if (r23 == 0) goto L_0x03b7;
    L_0x0386:
        r0 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x03bb }
        r24 = org.telegram.p004ui.ActionBar.Theme.chat_adminPaint;	 Catch:{ Exception -> 0x03bb }
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r4);	 Catch:{ Exception -> 0x03bb }
        r25 = r3 + r7;
        r26 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x03bb }
        r27 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r28 = 0;
        r29 = 0;
        r22 = r0;
        r22.<init>(r23, r24, r25, r26, r27, r28, r29);	 Catch:{ Exception -> 0x03bb }
        r1.adminLayout = r0;	 Catch:{ Exception -> 0x03bb }
        r0 = r1.nameWidth;	 Catch:{ Exception -> 0x03bb }
        r0 = (float) r0;	 Catch:{ Exception -> 0x03bb }
        r3 = r1.adminLayout;	 Catch:{ Exception -> 0x03bb }
        r3 = r3.getLineWidth(r5);	 Catch:{ Exception -> 0x03bb }
        r4 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r4);	 Catch:{ Exception -> 0x03bb }
        r4 = (float) r7;	 Catch:{ Exception -> 0x03bb }
        r3 = r3 + r4;
        r0 = r0 + r3;
        r0 = (int) r0;	 Catch:{ Exception -> 0x03bb }
        r1.nameWidth = r0;	 Catch:{ Exception -> 0x03bb }
        goto L_0x03bf;
    L_0x03b7:
        r3 = 0;
        r1.adminLayout = r3;	 Catch:{ Exception -> 0x03bb }
        goto L_0x03bf;
    L_0x03bb:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x03bf:
        r0 = r1.currentNameString;
        r0 = r0.length();
        if (r0 != 0) goto L_0x03cb;
    L_0x03c7:
        r3 = 0;
        r1.currentNameString = r3;
        goto L_0x03cc;
    L_0x03cb:
        r3 = 0;
    L_0x03cc:
        r1.currentForwardUser = r3;
        r1.currentForwardNameString = r3;
        r1.currentForwardChannel = r3;
        r1.currentForwardName = r3;
        r0 = r1.forwardedNameLayout;
        r0[r5] = r3;
        r4 = 1;
        r0[r4] = r3;
        r1.forwardedNameWidth = r5;
        r0 = r1.drawForwardedName;
        if (r0 == 0) goto L_0x061a;
    L_0x03e1:
        r0 = r39.needDrawForwarded();
        if (r0 == 0) goto L_0x061a;
    L_0x03e7:
        r0 = r1.currentPosition;
        if (r0 == 0) goto L_0x03ef;
    L_0x03eb:
        r0 = r0.minY;
        if (r0 != 0) goto L_0x061a;
    L_0x03ef:
        r0 = r2.messageOwner;
        r0 = r0.fwd_from;
        r0 = r0.channel_id;
        if (r0 == 0) goto L_0x040d;
    L_0x03f7:
        r0 = r1.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r3 = r2.messageOwner;
        r3 = r3.fwd_from;
        r3 = r3.channel_id;
        r3 = java.lang.Integer.valueOf(r3);
        r0 = r0.getChat(r3);
        r1.currentForwardChannel = r0;
    L_0x040d:
        r0 = r2.messageOwner;
        r0 = r0.fwd_from;
        r0 = r0.from_id;
        if (r0 == 0) goto L_0x042b;
    L_0x0415:
        r0 = r1.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r3 = r2.messageOwner;
        r3 = r3.fwd_from;
        r3 = r3.from_id;
        r3 = java.lang.Integer.valueOf(r3);
        r0 = r0.getUser(r3);
        r1.currentForwardUser = r0;
    L_0x042b:
        r0 = r2.messageOwner;
        r0 = r0.fwd_from;
        r0 = r0.from_name;
        if (r0 == 0) goto L_0x0435;
    L_0x0433:
        r1.currentForwardName = r0;
    L_0x0435:
        r0 = r1.currentForwardUser;
        if (r0 != 0) goto L_0x0441;
    L_0x0439:
        r0 = r1.currentForwardChannel;
        if (r0 != 0) goto L_0x0441;
    L_0x043d:
        r0 = r1.currentForwardName;
        if (r0 == 0) goto L_0x061a;
    L_0x0441:
        r0 = r1.currentForwardChannel;
        if (r0 == 0) goto L_0x0465;
    L_0x0445:
        r3 = r1.currentForwardUser;
        if (r3 == 0) goto L_0x0460;
    L_0x0449:
        r4 = 2;
        r7 = new java.lang.Object[r4];
        r0 = r0.title;
        r7[r5] = r0;
        r0 = org.telegram.messenger.UserObject.getUserName(r3);
        r3 = 1;
        r7[r3] = r0;
        r0 = "%s (%s)";
        r0 = java.lang.String.format(r0, r7);
        r1.currentForwardNameString = r0;
        goto L_0x0476;
    L_0x0460:
        r0 = r0.title;
        r1.currentForwardNameString = r0;
        goto L_0x0476;
    L_0x0465:
        r0 = r1.currentForwardUser;
        if (r0 == 0) goto L_0x0470;
    L_0x0469:
        r0 = org.telegram.messenger.UserObject.getUserName(r0);
        r1.currentForwardNameString = r0;
        goto L_0x0476;
    L_0x0470:
        r0 = r1.currentForwardName;
        if (r0 == 0) goto L_0x0476;
    L_0x0474:
        r1.currentForwardNameString = r0;
    L_0x0476:
        r0 = r38.getMaxNameWidth();
        r1.forwardedNameWidth = r0;
        r0 = 2131559574; // 0x7f0d0496 float:1.8744496E38 double:1.0531303576E-314;
        r3 = "From";
        r0 = org.telegram.messenger.LocaleController.getString(r3, r0);
        r3 = 2131559582; // 0x7f0d049e float:1.8744512E38 double:1.0531303615E-314;
        r4 = "FromFormatted";
        r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
        r4 = "%1$s";
        r4 = r3.indexOf(r4);
        r7 = org.telegram.p004ui.ActionBar.Theme.chat_forwardNamePaint;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r8.append(r0);
        r0 = " ";
        r8.append(r0);
        r0 = r8.toString();
        r0 = r7.measureText(r0);
        r7 = (double) r0;
        r7 = java.lang.Math.ceil(r7);
        r0 = (int) r7;
        r7 = r1.currentForwardNameString;
        r8 = 32;
        r12 = 10;
        r7 = r7.replace(r12, r8);
        r8 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;
        r12 = r1.forwardedNameWidth;
        r12 = r12 - r0;
        r15 = r1.viaWidth;
        r12 = r12 - r15;
        r12 = (float) r12;
        r15 = android.text.TextUtils.TruncateAt.END;
        r7 = android.text.TextUtils.ellipsize(r7, r8, r12, r15);
        r8 = 1;
        r12 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x04d4 }
        r12[r5] = r7;	 Catch:{ Exception -> 0x04d4 }
        r8 = java.lang.String.format(r3, r12);	 Catch:{ Exception -> 0x04d4 }
        goto L_0x04d8;
    L_0x04d4:
        r8 = r7.toString();
    L_0x04d8:
        if (r10 == 0) goto L_0x0523;
    L_0x04da:
        r3 = new android.text.SpannableStringBuilder;
        r10 = 3;
        r10 = new java.lang.Object[r10];
        r10[r5] = r8;
        r9 = 2131561039; // 0x7f0d0a4f float:1.8747467E38 double:1.0531310814E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r11, r9);
        r11 = 1;
        r10[r11] = r9;
        r9 = 2;
        r10[r9] = r6;
        r9 = "%s %s %s";
        r9 = java.lang.String.format(r9, r10);
        r3.<init>(r9);
        r9 = org.telegram.p004ui.ActionBar.Theme.chat_forwardNamePaint;
        r8 = r9.measureText(r8);
        r8 = (double) r8;
        r8 = java.lang.Math.ceil(r8);
        r8 = (int) r8;
        r1.viaNameWidth = r8;
        r8 = new org.telegram.ui.Components.TypefaceSpan;
        r9 = org.telegram.messenger.AndroidUtilities.getTypeface(r13);
        r8.<init>(r9);
        r9 = r3.length();
        r6 = r6.length();
        r9 = r9 - r6;
        r6 = 1;
        r9 = r9 - r6;
        r10 = r3.length();
        r11 = 33;
        r3.setSpan(r8, r9, r10, r11);
        goto L_0x0532;
    L_0x0523:
        r6 = 1;
        r8 = new android.text.SpannableStringBuilder;
        r9 = new java.lang.Object[r6];
        r9[r5] = r7;
        r3 = java.lang.String.format(r3, r9);
        r8.<init>(r3);
        r3 = r8;
    L_0x0532:
        r6 = org.telegram.p004ui.ActionBar.Theme.chat_forwardNamePaint;
        r8 = r7.length();
        r6 = r6.measureText(r7, r5, r8);
        r8 = (double) r6;
        r8 = java.lang.Math.ceil(r8);
        r6 = (int) r8;
        r8 = 2;
        r6 = r6 / r8;
        r0 = r0 + r6;
        r1.forwardNameCenterX = r0;
        if (r4 < 0) goto L_0x0568;
    L_0x0549:
        r0 = r1.currentForwardName;
        if (r0 == 0) goto L_0x0555;
    L_0x054d:
        r0 = r2.messageOwner;
        r0 = r0.fwd_from;
        r0 = r0.from_id;
        if (r0 == 0) goto L_0x0568;
    L_0x0555:
        r0 = new org.telegram.ui.Components.TypefaceSpan;
        r6 = org.telegram.messenger.AndroidUtilities.getTypeface(r13);
        r0.<init>(r6);
        r6 = r7.length();
        r6 = r6 + r4;
        r7 = 33;
        r3.setSpan(r0, r4, r6, r7);
    L_0x0568:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_forwardNamePaint;
        r4 = r1.forwardedNameWidth;
        r4 = (float) r4;
        r6 = android.text.TextUtils.TruncateAt.END;
        r22 = android.text.TextUtils.ellipsize(r3, r0, r4, r6);
        r0 = r1.forwardedNameLayout;	 Catch:{ Exception -> 0x0616 }
        r3 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0616 }
        r23 = org.telegram.p004ui.ActionBar.Theme.chat_forwardNamePaint;	 Catch:{ Exception -> 0x0616 }
        r4 = r1.forwardedNameWidth;	 Catch:{ Exception -> 0x0616 }
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r6);	 Catch:{ Exception -> 0x0616 }
        r24 = r4 + r7;
        r25 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0616 }
        r26 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r27 = 0;
        r28 = 0;
        r21 = r3;
        r21.<init>(r22, r23, r24, r25, r26, r27, r28);	 Catch:{ Exception -> 0x0616 }
        r4 = 1;
        r0[r4] = r3;	 Catch:{ Exception -> 0x0616 }
        r0 = "ForwardedMessage";
        r3 = 2131559530; // 0x7f0d046a float:1.8744407E38 double:1.053130336E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r3);	 Catch:{ Exception -> 0x0616 }
        r0 = org.telegram.messenger.AndroidUtilities.replaceTags(r0);	 Catch:{ Exception -> 0x0616 }
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_forwardNamePaint;	 Catch:{ Exception -> 0x0616 }
        r4 = r1.forwardedNameWidth;	 Catch:{ Exception -> 0x0616 }
        r4 = (float) r4;	 Catch:{ Exception -> 0x0616 }
        r6 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x0616 }
        r22 = android.text.TextUtils.ellipsize(r0, r3, r4, r6);	 Catch:{ Exception -> 0x0616 }
        r0 = r1.forwardedNameLayout;	 Catch:{ Exception -> 0x0616 }
        r3 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0616 }
        r23 = org.telegram.p004ui.ActionBar.Theme.chat_forwardNamePaint;	 Catch:{ Exception -> 0x0616 }
        r4 = r1.forwardedNameWidth;	 Catch:{ Exception -> 0x0616 }
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);	 Catch:{ Exception -> 0x0616 }
        r24 = r4 + r6;
        r25 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0616 }
        r26 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r27 = 0;
        r28 = 0;
        r21 = r3;
        r21.<init>(r22, r23, r24, r25, r26, r27, r28);	 Catch:{ Exception -> 0x0616 }
        r0[r5] = r3;	 Catch:{ Exception -> 0x0616 }
        r0 = r1.forwardedNameLayout;	 Catch:{ Exception -> 0x0616 }
        r0 = r0[r5];	 Catch:{ Exception -> 0x0616 }
        r0 = r0.getLineWidth(r5);	 Catch:{ Exception -> 0x0616 }
        r3 = (double) r0;	 Catch:{ Exception -> 0x0616 }
        r3 = java.lang.Math.ceil(r3);	 Catch:{ Exception -> 0x0616 }
        r0 = (int) r3;	 Catch:{ Exception -> 0x0616 }
        r3 = r1.forwardedNameLayout;	 Catch:{ Exception -> 0x0616 }
        r4 = 1;
        r3 = r3[r4];	 Catch:{ Exception -> 0x0616 }
        r3 = r3.getLineWidth(r5);	 Catch:{ Exception -> 0x0616 }
        r3 = (double) r3;	 Catch:{ Exception -> 0x0616 }
        r3 = java.lang.Math.ceil(r3);	 Catch:{ Exception -> 0x0616 }
        r3 = (int) r3;	 Catch:{ Exception -> 0x0616 }
        r0 = java.lang.Math.max(r0, r3);	 Catch:{ Exception -> 0x0616 }
        r1.forwardedNameWidth = r0;	 Catch:{ Exception -> 0x0616 }
        r0 = r1.forwardNameOffsetX;	 Catch:{ Exception -> 0x0616 }
        r3 = r1.forwardedNameLayout;	 Catch:{ Exception -> 0x0616 }
        r3 = r3[r5];	 Catch:{ Exception -> 0x0616 }
        r3 = r3.getLineLeft(r5);	 Catch:{ Exception -> 0x0616 }
        r0[r5] = r3;	 Catch:{ Exception -> 0x0616 }
        r0 = r1.forwardNameOffsetX;	 Catch:{ Exception -> 0x0616 }
        r3 = r1.forwardedNameLayout;	 Catch:{ Exception -> 0x0616 }
        r4 = 1;
        r3 = r3[r4];	 Catch:{ Exception -> 0x0616 }
        r3 = r3.getLineLeft(r5);	 Catch:{ Exception -> 0x0616 }
        r0[r4] = r3;	 Catch:{ Exception -> 0x0616 }
        r0 = r2.type;	 Catch:{ Exception -> 0x0616 }
        if (r0 == r14) goto L_0x061a;
    L_0x060a:
        r0 = r1.namesOffset;	 Catch:{ Exception -> 0x0616 }
        r3 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);	 Catch:{ Exception -> 0x0616 }
        r0 = r0 + r3;
        r1.namesOffset = r0;	 Catch:{ Exception -> 0x0616 }
        goto L_0x061a;
    L_0x0616:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x061a:
        r0 = r39.hasValidReplyMessageObject();
        if (r0 == 0) goto L_0x08e3;
    L_0x0620:
        r0 = r1.currentPosition;
        if (r0 == 0) goto L_0x0628;
    L_0x0624:
        r0 = r0.minY;
        if (r0 != 0) goto L_0x08e3;
    L_0x0628:
        r0 = r39.isAnyKindOfSticker();
        if (r0 != 0) goto L_0x064c;
    L_0x062e:
        r0 = r2.type;
        if (r0 == r14) goto L_0x064c;
    L_0x0632:
        r0 = r1.namesOffset;
        r3 = 1109917696; // 0x42280000 float:42.0 double:5.483722033E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 + r3;
        r1.namesOffset = r0;
        r0 = r2.type;
        if (r0 == 0) goto L_0x064c;
    L_0x0641:
        r0 = r1.namesOffset;
        r3 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 + r3;
        r1.namesOffset = r0;
    L_0x064c:
        r0 = r38.getMaxNameWidth();
        r3 = r39.shouldDrawWithoutBackground();
        if (r3 != 0) goto L_0x065e;
    L_0x0656:
        r3 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        goto L_0x0669;
    L_0x065e:
        r3 = r2.type;
        if (r3 != r14) goto L_0x0669;
    L_0x0662:
        r3 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 + r3;
    L_0x0669:
        r3 = r2.replyMessageObject;
        r3 = r3.photoThumbs2;
        r4 = 320; // 0x140 float:4.48E-43 double:1.58E-321;
        r3 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r3, r4);
        r4 = r2.replyMessageObject;
        r4 = r4.photoThumbs2;
        r6 = 40;
        r4 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r4, r6);
        r6 = r2.replyMessageObject;
        r7 = r6.photoThumbsObject2;
        if (r3 != 0) goto L_0x06b8;
    L_0x0683:
        r3 = r6.mediaExists;
        if (r3 == 0) goto L_0x0699;
    L_0x0687:
        r3 = r6.photoThumbs;
        r4 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        r3 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r3, r4);
        if (r3 == 0) goto L_0x0696;
    L_0x0693:
        r4 = r3.size;
        goto L_0x0697;
    L_0x0696:
        r4 = 0;
    L_0x0697:
        r6 = 0;
        goto L_0x06a3;
    L_0x0699:
        r3 = r6.photoThumbs;
        r4 = 320; // 0x140 float:4.48E-43 double:1.58E-321;
        r3 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r3, r4);
        r4 = 0;
        r6 = 1;
    L_0x06a3:
        r7 = r2.replyMessageObject;
        r7 = r7.photoThumbs;
        r8 = 40;
        r7 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r7, r8);
        r8 = r2.replyMessageObject;
        r8 = r8.photoThumbsObject;
        r26 = r4;
        r29 = r6;
        r4 = r7;
        r7 = r8;
        goto L_0x06bc;
    L_0x06b8:
        r26 = 0;
        r29 = 1;
    L_0x06bc:
        if (r4 != r3) goto L_0x06bf;
    L_0x06be:
        r4 = 0;
    L_0x06bf:
        if (r3 == 0) goto L_0x071f;
    L_0x06c1:
        r6 = r2.replyMessageObject;
        r6 = r6.isAnyKindOfSticker();
        if (r6 != 0) goto L_0x071f;
    L_0x06c9:
        r6 = r39.isAnyKindOfSticker();
        if (r6 == 0) goto L_0x06d5;
    L_0x06cf:
        r6 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r6 == 0) goto L_0x071f;
    L_0x06d5:
        r6 = r2.replyMessageObject;
        r6 = r6.isSecretMedia();
        if (r6 == 0) goto L_0x06de;
    L_0x06dd:
        goto L_0x071f;
    L_0x06de:
        r6 = r2.replyMessageObject;
        r6 = r6.isRoundVideo();
        if (r6 == 0) goto L_0x06f2;
    L_0x06e6:
        r6 = r1.replyImageReceiver;
        r8 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r8 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r6.setRoundRadius(r8);
        goto L_0x06f7;
    L_0x06f2:
        r6 = r1.replyImageReceiver;
        r6.setRoundRadius(r5);
    L_0x06f7:
        r1.currentReplyPhoto = r3;
        r6 = r1.replyImageReceiver;
        r22 = org.telegram.messenger.ImageLocation.getForObject(r3, r7);
        r24 = org.telegram.messenger.ImageLocation.getForObject(r4, r7);
        r27 = 0;
        r3 = r2.replyMessageObject;
        r23 = "50_50";
        r25 = "50_50_b";
        r21 = r6;
        r28 = r3;
        r21.setImage(r22, r23, r24, r25, r26, r27, r28, r29);
        r3 = 1;
        r1.needReplyImage = r3;
        r3 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r3 = r0;
        r4 = 0;
        goto L_0x0728;
    L_0x071f:
        r3 = r1.replyImageReceiver;
        r4 = 0;
        r3.setImageBitmap(r4);
        r1.needReplyImage = r5;
        r3 = r0;
    L_0x0728:
        r0 = r2.customReplyName;
        if (r0 == 0) goto L_0x072d;
    L_0x072c:
        goto L_0x078e;
    L_0x072d:
        r0 = r2.replyMessageObject;
        r0 = r0.isFromUser();
        if (r0 == 0) goto L_0x0750;
    L_0x0735:
        r0 = r1.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r6 = r2.replyMessageObject;
        r6 = r6.messageOwner;
        r6 = r6.from_id;
        r6 = java.lang.Integer.valueOf(r6);
        r0 = r0.getUser(r6);
        if (r0 == 0) goto L_0x078d;
    L_0x074b:
        r0 = org.telegram.messenger.UserObject.getUserName(r0);
        goto L_0x078e;
    L_0x0750:
        r0 = r2.replyMessageObject;
        r0 = r0.messageOwner;
        r0 = r0.from_id;
        if (r0 >= 0) goto L_0x0772;
    L_0x0758:
        r0 = r1.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r6 = r2.replyMessageObject;
        r6 = r6.messageOwner;
        r6 = r6.from_id;
        r6 = -r6;
        r6 = java.lang.Integer.valueOf(r6);
        r0 = r0.getChat(r6);
        if (r0 == 0) goto L_0x078d;
    L_0x076f:
        r0 = r0.title;
        goto L_0x078e;
    L_0x0772:
        r0 = r1.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r6 = r2.replyMessageObject;
        r6 = r6.messageOwner;
        r6 = r6.to_id;
        r6 = r6.channel_id;
        r6 = java.lang.Integer.valueOf(r6);
        r0 = r0.getChat(r6);
        if (r0 == 0) goto L_0x078d;
    L_0x078a:
        r0 = r0.title;
        goto L_0x078e;
    L_0x078d:
        r0 = r4;
    L_0x078e:
        if (r0 != 0) goto L_0x0799;
    L_0x0790:
        r0 = 2131559768; // 0x7f0d0558 float:1.874489E38 double:1.0531304534E-314;
        r6 = "Loading";
        r0 = org.telegram.messenger.LocaleController.getString(r6, r0);
    L_0x0799:
        r6 = 32;
        r7 = 10;
        r0 = r0.replace(r7, r6);
        r6 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;
        r7 = (float) r3;
        r8 = android.text.TextUtils.TruncateAt.END;
        r18 = android.text.TextUtils.ellipsize(r0, r6, r7, r8);
        r0 = r2.replyMessageObject;
        r6 = r0.messageOwner;
        r6 = r6.media;
        r8 = r6 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        r9 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        if (r8 == 0) goto L_0x07d2;
    L_0x07b6:
        r0 = r6.game;
        r0 = r0.title;
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;
        r2 = r2.getFontMetricsInt();
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r0 = org.telegram.messenger.Emoji.replaceEmoji(r0, r2, r4, r5);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;
        r4 = android.text.TextUtils.TruncateAt.END;
        r4 = android.text.TextUtils.ellipsize(r0, r2, r7, r4);
    L_0x07d0:
        r7 = r4;
        goto L_0x082e;
    L_0x07d2:
        r8 = r6 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
        if (r8 == 0) goto L_0x07ef;
    L_0x07d6:
        r0 = r6.title;
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;
        r2 = r2.getFontMetricsInt();
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r0 = org.telegram.messenger.Emoji.replaceEmoji(r0, r2, r4, r5);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;
        r4 = android.text.TextUtils.TruncateAt.END;
        r4 = android.text.TextUtils.ellipsize(r0, r2, r7, r4);
        goto L_0x07d0;
    L_0x07ef:
        r0 = r0.messageText;
        if (r0 == 0) goto L_0x07d0;
    L_0x07f3:
        r0 = r0.length();
        if (r0 <= 0) goto L_0x07d0;
    L_0x07f9:
        r0 = r2.replyMessageObject;
        r0 = r0.messageText;
        r0 = r0.toString();
        r2 = r0.length();
        r4 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        if (r2 <= r4) goto L_0x080f;
    L_0x0809:
        r2 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        r0 = r0.substring(r5, r2);
    L_0x080f:
        r2 = 32;
        r4 = 10;
        r0 = r0.replace(r4, r2);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;
        r2 = r2.getFontMetricsInt();
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r0 = org.telegram.messenger.Emoji.replaceEmoji(r0, r2, r4, r5);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;
        r4 = android.text.TextUtils.TruncateAt.END;
        r4 = android.text.TextUtils.ellipsize(r0, r2, r7, r4);
        goto L_0x07d0;
    L_0x082e:
        r0 = r1.needReplyImage;	 Catch:{ Exception -> 0x0886 }
        if (r0 == 0) goto L_0x0835;
    L_0x0832:
        r0 = 44;
        goto L_0x0836;
    L_0x0835:
        r0 = 0;
    L_0x0836:
        r2 = 4;
        r14 = r2 + r0;
        r0 = (float) r14;	 Catch:{ Exception -> 0x0886 }
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);	 Catch:{ Exception -> 0x0886 }
        r1.replyNameWidth = r0;	 Catch:{ Exception -> 0x0886 }
        if (r18 == 0) goto L_0x088a;
    L_0x0842:
        r0 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0886 }
        r19 = org.telegram.p004ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x0886 }
        r2 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);	 Catch:{ Exception -> 0x0886 }
        r20 = r3 + r2;
        r21 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0886 }
        r22 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r23 = 0;
        r24 = 0;
        r17 = r0;
        r17.<init>(r18, r19, r20, r21, r22, r23, r24);	 Catch:{ Exception -> 0x0886 }
        r1.replyNameLayout = r0;	 Catch:{ Exception -> 0x0886 }
        r0 = r1.replyNameLayout;	 Catch:{ Exception -> 0x0886 }
        r0 = r0.getLineCount();	 Catch:{ Exception -> 0x0886 }
        if (r0 <= 0) goto L_0x088a;
    L_0x0865:
        r0 = r1.replyNameWidth;	 Catch:{ Exception -> 0x0886 }
        r2 = r1.replyNameLayout;	 Catch:{ Exception -> 0x0886 }
        r2 = r2.getLineWidth(r5);	 Catch:{ Exception -> 0x0886 }
        r8 = (double) r2;	 Catch:{ Exception -> 0x0886 }
        r8 = java.lang.Math.ceil(r8);	 Catch:{ Exception -> 0x0886 }
        r2 = (int) r8;	 Catch:{ Exception -> 0x0886 }
        r4 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);	 Catch:{ Exception -> 0x0886 }
        r2 = r2 + r6;
        r0 = r0 + r2;
        r1.replyNameWidth = r0;	 Catch:{ Exception -> 0x0886 }
        r0 = r1.replyNameLayout;	 Catch:{ Exception -> 0x0886 }
        r0 = r0.getLineLeft(r5);	 Catch:{ Exception -> 0x0886 }
        r1.replyNameOffset = r0;	 Catch:{ Exception -> 0x0886 }
        goto L_0x088a;
    L_0x0886:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x088a:
        r0 = r1.needReplyImage;	 Catch:{ Exception -> 0x08df }
        if (r0 == 0) goto L_0x0891;
    L_0x088e:
        r0 = 44;
        goto L_0x0892;
    L_0x0891:
        r0 = 0;
    L_0x0892:
        r2 = 4;
        r14 = r2 + r0;
        r0 = (float) r14;	 Catch:{ Exception -> 0x08df }
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);	 Catch:{ Exception -> 0x08df }
        r1.replyTextWidth = r0;	 Catch:{ Exception -> 0x08df }
        if (r7 == 0) goto L_0x08e3;
    L_0x089e:
        r0 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x08df }
        r8 = org.telegram.p004ui.ActionBar.Theme.chat_replyTextPaint;	 Catch:{ Exception -> 0x08df }
        r2 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);	 Catch:{ Exception -> 0x08df }
        r9 = r3 + r2;
        r10 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x08df }
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r12 = 0;
        r13 = 0;
        r6 = r0;
        r6.<init>(r7, r8, r9, r10, r11, r12, r13);	 Catch:{ Exception -> 0x08df }
        r1.replyTextLayout = r0;	 Catch:{ Exception -> 0x08df }
        r0 = r1.replyTextLayout;	 Catch:{ Exception -> 0x08df }
        r0 = r0.getLineCount();	 Catch:{ Exception -> 0x08df }
        if (r0 <= 0) goto L_0x08e3;
    L_0x08be:
        r0 = r1.replyTextWidth;	 Catch:{ Exception -> 0x08df }
        r2 = r1.replyTextLayout;	 Catch:{ Exception -> 0x08df }
        r2 = r2.getLineWidth(r5);	 Catch:{ Exception -> 0x08df }
        r2 = (double) r2;	 Catch:{ Exception -> 0x08df }
        r2 = java.lang.Math.ceil(r2);	 Catch:{ Exception -> 0x08df }
        r2 = (int) r2;	 Catch:{ Exception -> 0x08df }
        r3 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);	 Catch:{ Exception -> 0x08df }
        r2 = r2 + r3;
        r0 = r0 + r2;
        r1.replyTextWidth = r0;	 Catch:{ Exception -> 0x08df }
        r0 = r1.replyTextLayout;	 Catch:{ Exception -> 0x08df }
        r0 = r0.getLineLeft(r5);	 Catch:{ Exception -> 0x08df }
        r1.replyTextOffset = r0;	 Catch:{ Exception -> 0x08df }
        goto L_0x08e3;
    L_0x08df:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x08e3:
        r38.requestLayout();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.setMessageObjectInternal(org.telegram.messenger.MessageObject):void");
    }

    public int getCaptionHeight() {
        return this.addedCaptionHeight;
    }

    public ImageReceiver getAvatarImage() {
        return this.isAvatarVisible ? this.avatarImage : null;
    }

    public float getCheckBoxTranslation() {
        return (float) this.checkBoxTranslation;
    }

    public void drawCheckBox(Canvas canvas) {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && !messageObject.isSending() && !this.currentMessageObject.isSendError() && this.checkBox != null) {
            if (this.checkBoxVisible || this.checkBoxAnimationInProgress) {
                GroupedMessagePosition groupedMessagePosition = this.currentPosition;
                if (groupedMessagePosition != null) {
                    int i = groupedMessagePosition.flags;
                    if ((i & 8) == 0 || (i & 1) == 0) {
                        return;
                    }
                }
                canvas.save();
                canvas.translate(0.0f, (float) getTop());
                this.checkBox.draw(canvas);
                canvas.restore();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            if (this.wasLayout) {
                int i;
                Drawable drawable;
                Drawable drawable2;
                Drawable drawable3;
                int i2;
                float f;
                int i3;
                TextPaint textPaint;
                String str;
                if (messageObject.isOutOwner()) {
                    Theme.chat_msgTextPaint.setColor(Theme.getColor(Theme.key_chat_messageTextOut));
                    textPaint = Theme.chat_msgTextPaint;
                    str = Theme.key_chat_messageLinkOut;
                    textPaint.linkColor = Theme.getColor(str);
                    Theme.chat_msgGameTextPaint.setColor(Theme.getColor(Theme.key_chat_messageTextOut));
                    Theme.chat_msgGameTextPaint.linkColor = Theme.getColor(str);
                    Theme.chat_replyTextPaint.linkColor = Theme.getColor(str);
                } else {
                    Theme.chat_msgTextPaint.setColor(Theme.getColor(Theme.key_chat_messageTextIn));
                    textPaint = Theme.chat_msgTextPaint;
                    str = Theme.key_chat_messageLinkIn;
                    textPaint.linkColor = Theme.getColor(str);
                    Theme.chat_msgGameTextPaint.setColor(Theme.getColor(Theme.key_chat_messageTextIn));
                    Theme.chat_msgGameTextPaint.linkColor = Theme.getColor(str);
                    Theme.chat_replyTextPaint.linkColor = Theme.getColor(str);
                }
                if (this.documentAttach != null) {
                    i = this.documentAttachType;
                    String str2 = Theme.key_chat_outAudioSeekbarFill;
                    String str3 = Theme.key_chat_inAudioSeekbarFill;
                    if (i == 3) {
                        if (this.currentMessageObject.isOutOwner()) {
                            this.seekBarWaveform.setColors(Theme.getColor(Theme.key_chat_outVoiceSeekbar), Theme.getColor(Theme.key_chat_outVoiceSeekbarFill), Theme.getColor(Theme.key_chat_outVoiceSeekbarSelected));
                            this.seekBar.setColors(Theme.getColor(Theme.key_chat_outAudioSeekbar), Theme.getColor(Theme.key_chat_outAudioCacheSeekbar), Theme.getColor(str2), Theme.getColor(str2), Theme.getColor(Theme.key_chat_outAudioSeekbarSelected));
                        } else {
                            this.seekBarWaveform.setColors(Theme.getColor(Theme.key_chat_inVoiceSeekbar), Theme.getColor(Theme.key_chat_inVoiceSeekbarFill), Theme.getColor(Theme.key_chat_inVoiceSeekbarSelected));
                            this.seekBar.setColors(Theme.getColor(Theme.key_chat_inAudioSeekbar), Theme.getColor(Theme.key_chat_inAudioCacheSeekbar), Theme.getColor(str3), Theme.getColor(str3), Theme.getColor(Theme.key_chat_inAudioSeekbarSelected));
                        }
                    } else if (i == 5) {
                        this.documentAttachType = 5;
                        if (this.currentMessageObject.isOutOwner()) {
                            this.seekBar.setColors(Theme.getColor(Theme.key_chat_outAudioSeekbar), Theme.getColor(Theme.key_chat_outAudioCacheSeekbar), Theme.getColor(str2), Theme.getColor(str2), Theme.getColor(Theme.key_chat_outAudioSeekbarSelected));
                        } else {
                            this.seekBar.setColors(Theme.getColor(Theme.key_chat_inAudioSeekbar), Theme.getColor(Theme.key_chat_inAudioCacheSeekbar), Theme.getColor(str3), Theme.getColor(str3), Theme.getColor(Theme.key_chat_inAudioSeekbarSelected));
                        }
                    }
                }
                messageObject = this.currentMessageObject;
                if (messageObject.type == 5) {
                    Theme.chat_timePaint.setColor(Theme.getColor(Theme.key_chat_mediaTimeText));
                } else if (this.mediaBackground) {
                    if (messageObject.shouldDrawWithoutBackground()) {
                        Theme.chat_timePaint.setColor(Theme.getColor(Theme.key_chat_serviceText));
                    } else {
                        Theme.chat_timePaint.setColor(Theme.getColor(Theme.key_chat_mediaTimeText));
                    }
                } else if (messageObject.isOutOwner()) {
                    Theme.chat_timePaint.setColor(Theme.getColor(isDrawSelectionBackground() ? Theme.key_chat_outTimeSelectedText : Theme.key_chat_outTimeText));
                } else {
                    Theme.chat_timePaint.setColor(Theme.getColor(isDrawSelectionBackground() ? Theme.key_chat_inTimeSelectedText : Theme.key_chat_inTimeText));
                }
                int i4 = 0;
                int i5;
                int dp;
                int dp2;
                if (this.currentMessageObject.isOutOwner()) {
                    int i6;
                    if (this.mediaBackground || this.drawPinnedBottom) {
                        this.currentBackgroundDrawable = Theme.chat_msgOutMediaDrawable;
                        drawable = Theme.chat_msgOutMediaSelectedDrawable;
                        drawable2 = Theme.chat_msgOutMediaShadowDrawable;
                    } else {
                        this.currentBackgroundDrawable = Theme.chat_msgOutDrawable;
                        drawable = Theme.chat_msgOutSelectedDrawable;
                        drawable2 = Theme.chat_msgOutShadowDrawable;
                    }
                    this.backgroundDrawableLeft = (this.layoutWidth - this.backgroundWidth) - (!this.mediaBackground ? 0 : AndroidUtilities.m26dp(9.0f));
                    this.backgroundDrawableRight = this.backgroundWidth - (this.mediaBackground ? 0 : AndroidUtilities.m26dp(3.0f));
                    if (!(this.currentMessagesGroup == null || this.currentPosition.edge)) {
                        this.backgroundDrawableRight += AndroidUtilities.m26dp(10.0f);
                    }
                    i5 = this.backgroundDrawableLeft;
                    if (!this.mediaBackground && this.drawPinnedBottom) {
                        this.backgroundDrawableRight -= AndroidUtilities.m26dp(6.0f);
                    }
                    GroupedMessagePosition groupedMessagePosition = this.currentPosition;
                    if (groupedMessagePosition != null) {
                        if ((groupedMessagePosition.flags & 2) == 0) {
                            this.backgroundDrawableRight += AndroidUtilities.m26dp(8.0f);
                        }
                        if ((this.currentPosition.flags & 1) == 0) {
                            i5 -= AndroidUtilities.m26dp(8.0f);
                            this.backgroundDrawableRight += AndroidUtilities.m26dp(8.0f);
                        }
                        if ((this.currentPosition.flags & 4) == 0) {
                            dp = 0 - AndroidUtilities.m26dp(9.0f);
                            dp2 = AndroidUtilities.m26dp(9.0f) + 0;
                        } else {
                            dp = 0;
                            dp2 = 0;
                        }
                        if ((this.currentPosition.flags & 8) == 0) {
                            dp2 += AndroidUtilities.m26dp(9.0f);
                        }
                    } else {
                        dp = 0;
                        dp2 = 0;
                    }
                    if (this.drawPinnedBottom && this.drawPinnedTop) {
                        i6 = 0;
                    } else if (this.drawPinnedBottom) {
                        i6 = AndroidUtilities.m26dp(1.0f);
                    } else {
                        i6 = AndroidUtilities.m26dp(2.0f);
                    }
                    boolean z = this.drawPinnedTop;
                    int dp3 = (z || (z && this.drawPinnedBottom)) ? 0 : AndroidUtilities.m26dp(1.0f);
                    dp += dp3;
                    BaseCell.setDrawableBounds(this.currentBackgroundDrawable, i5, dp, this.backgroundDrawableRight, (this.layoutHeight - i6) + dp2);
                    BaseCell.setDrawableBounds(drawable, i5, dp, this.backgroundDrawableRight, (this.layoutHeight - i6) + dp2);
                    BaseCell.setDrawableBounds(drawable2, i5, dp, this.backgroundDrawableRight, (this.layoutHeight - i6) + dp2);
                } else {
                    if (this.mediaBackground || this.drawPinnedBottom) {
                        this.currentBackgroundDrawable = Theme.chat_msgInMediaDrawable;
                        drawable = Theme.chat_msgInMediaSelectedDrawable;
                        drawable3 = Theme.chat_msgInMediaShadowDrawable;
                    } else {
                        this.currentBackgroundDrawable = Theme.chat_msgInDrawable;
                        drawable = Theme.chat_msgInSelectedDrawable;
                        drawable3 = Theme.chat_msgInShadowDrawable;
                    }
                    drawable2 = drawable3;
                    i2 = (this.isChat && this.isAvatarVisible) ? 48 : 0;
                    this.backgroundDrawableLeft = AndroidUtilities.m26dp((float) (i2 + (!this.mediaBackground ? 3 : 9)));
                    this.backgroundDrawableRight = this.backgroundWidth - (this.mediaBackground ? 0 : AndroidUtilities.m26dp(3.0f));
                    if (this.currentMessagesGroup != null) {
                        if (!this.currentPosition.edge) {
                            this.backgroundDrawableLeft -= AndroidUtilities.m26dp(10.0f);
                            this.backgroundDrawableRight += AndroidUtilities.m26dp(10.0f);
                        }
                        i2 = this.currentPosition.leftSpanOffset;
                        if (i2 != 0) {
                            this.backgroundDrawableLeft += (int) Math.ceil((double) ((((float) i2) / 1000.0f) * ((float) getGroupPhotosWidth())));
                        }
                    }
                    if (!this.mediaBackground && this.drawPinnedBottom) {
                        this.backgroundDrawableRight -= AndroidUtilities.m26dp(6.0f);
                        this.backgroundDrawableLeft += AndroidUtilities.m26dp(6.0f);
                    }
                    GroupedMessagePosition groupedMessagePosition2 = this.currentPosition;
                    if (groupedMessagePosition2 != null) {
                        if ((groupedMessagePosition2.flags & 2) == 0) {
                            this.backgroundDrawableRight += AndroidUtilities.m26dp(8.0f);
                        }
                        if ((this.currentPosition.flags & 1) == 0) {
                            this.backgroundDrawableLeft -= AndroidUtilities.m26dp(8.0f);
                            this.backgroundDrawableRight += AndroidUtilities.m26dp(8.0f);
                        }
                        if ((this.currentPosition.flags & 4) == 0) {
                            i2 = 0 - AndroidUtilities.m26dp(9.0f);
                            dp = AndroidUtilities.m26dp(9.0f) + 0;
                        } else {
                            i2 = 0;
                            dp = 0;
                        }
                        if ((this.currentPosition.flags & 8) == 0) {
                            dp += AndroidUtilities.m26dp(10.0f);
                        }
                    } else {
                        i2 = 0;
                        dp = 0;
                    }
                    if (this.drawPinnedBottom && this.drawPinnedTop) {
                        i5 = 0;
                    } else if (this.drawPinnedBottom) {
                        i5 = AndroidUtilities.m26dp(1.0f);
                    } else {
                        i5 = AndroidUtilities.m26dp(2.0f);
                    }
                    boolean z2 = this.drawPinnedTop;
                    dp2 = (z2 || (z2 && this.drawPinnedBottom)) ? 0 : AndroidUtilities.m26dp(1.0f);
                    i2 += dp2;
                    BaseCell.setDrawableBounds(this.currentBackgroundDrawable, this.backgroundDrawableLeft, i2, this.backgroundDrawableRight, (this.layoutHeight - i5) + dp);
                    BaseCell.setDrawableBounds(drawable, this.backgroundDrawableLeft, i2, this.backgroundDrawableRight, (this.layoutHeight - i5) + dp);
                    BaseCell.setDrawableBounds(drawable2, this.backgroundDrawableLeft, i2, this.backgroundDrawableRight, (this.layoutHeight - i5) + dp);
                }
                if (this.checkBoxVisible || this.checkBoxAnimationInProgress) {
                    if ((this.checkBoxVisible && this.checkBoxAnimationProgress == 1.0f) || (!this.checkBoxVisible && this.checkBoxAnimationProgress == 0.0f)) {
                        this.checkBoxAnimationInProgress = false;
                    }
                    this.checkBoxTranslation = (int) Math.ceil((double) ((this.checkBoxVisible ? CubicBezierInterpolator.EASE_OUT : CubicBezierInterpolator.EASE_IN).getInterpolation(this.checkBoxAnimationProgress) * ((float) AndroidUtilities.m26dp(35.0f))));
                    if (!this.currentMessageObject.isOutOwner()) {
                        setTranslationX((float) this.checkBoxTranslation);
                    }
                    i2 = AndroidUtilities.m26dp(21.0f);
                    this.checkBox.setBounds(AndroidUtilities.m26dp(-27.0f) + this.checkBoxTranslation, (this.currentBackgroundDrawable.getBounds().bottom - AndroidUtilities.m26dp(8.0f)) - i2, i2, i2);
                    if (this.checkBoxAnimationInProgress) {
                        long uptimeMillis = SystemClock.uptimeMillis();
                        long j = uptimeMillis - this.lastCheckBoxAnimationTime;
                        this.lastCheckBoxAnimationTime = uptimeMillis;
                        if (this.checkBoxVisible) {
                            this.checkBoxAnimationProgress += ((float) j) / 200.0f;
                            if (this.checkBoxAnimationProgress > 1.0f) {
                                this.checkBoxAnimationProgress = 1.0f;
                            }
                            invalidate();
                            ((View) getParent()).invalidate();
                        } else {
                            this.checkBoxAnimationProgress -= ((float) j) / 200.0f;
                            if (this.checkBoxAnimationProgress <= 0.0f) {
                                this.checkBoxAnimationProgress = 0.0f;
                            }
                            invalidate();
                            ((View) getParent()).invalidate();
                        }
                    }
                }
                if (this.drawBackground) {
                    drawable3 = this.currentBackgroundDrawable;
                    if (drawable3 != null) {
                        if (this.isHighlightedAnimated) {
                            drawable3.draw(canvas2);
                            i2 = this.highlightProgress;
                            f = i2 >= 300 ? 1.0f : ((float) i2) / 300.0f;
                            if (this.currentPosition == null) {
                                drawable.setAlpha((int) (f * 255.0f));
                                drawable.draw(canvas2);
                            }
                        } else if (!isDrawSelectionBackground() || (this.currentPosition != null && getBackground() == null)) {
                            this.currentBackgroundDrawable.draw(canvas2);
                        } else {
                            drawable.setAlpha(NalUnitUtil.EXTENDED_SAR);
                            drawable.draw(canvas2);
                        }
                        GroupedMessagePosition groupedMessagePosition3 = this.currentPosition;
                        if (groupedMessagePosition3 == null || groupedMessagePosition3.flags != 0) {
                            drawable2.draw(canvas2);
                        }
                    }
                }
                if (this.isHighlightedAnimated) {
                    long currentTimeMillis = System.currentTimeMillis();
                    long abs = Math.abs(currentTimeMillis - this.lastHighlightProgressTime);
                    if (abs > 17) {
                        abs = 17;
                    }
                    this.highlightProgress = (int) (((long) this.highlightProgress) - abs);
                    this.lastHighlightProgressTime = currentTimeMillis;
                    if (this.highlightProgress <= 0) {
                        this.highlightProgress = 0;
                        this.isHighlightedAnimated = false;
                    }
                    invalidate();
                    if (getParent() != null) {
                        ((View) getParent()).invalidate();
                    }
                }
                drawContent(canvas);
                f = 12.0f;
                if (this.drawShareButton) {
                    if (this.sharePressed) {
                        if (!Theme.isCustomTheme() || Theme.hasThemeKey(Theme.key_chat_shareBackgroundSelected)) {
                            Theme.chat_shareDrawable.setColorFilter(Theme.getShareColorFilter(Theme.getColor(Theme.key_chat_shareBackgroundSelected), true));
                        } else {
                            Theme.chat_shareDrawable.setColorFilter(Theme.colorPressedFilter2);
                        }
                    } else if (!Theme.isCustomTheme() || Theme.hasThemeKey(Theme.key_chat_shareBackground)) {
                        Theme.chat_shareDrawable.setColorFilter(Theme.getShareColorFilter(Theme.getColor(Theme.key_chat_shareBackground), false));
                    } else {
                        Theme.chat_shareDrawable.setColorFilter(Theme.colorFilter2);
                    }
                    if (this.currentMessageObject.isOutOwner()) {
                        this.shareStartX = (this.currentBackgroundDrawable.getBounds().left - AndroidUtilities.m26dp(8.0f)) - Theme.chat_shareDrawable.getIntrinsicWidth();
                    } else {
                        this.shareStartX = this.currentBackgroundDrawable.getBounds().right + AndroidUtilities.m26dp(8.0f);
                    }
                    drawable = Theme.chat_shareDrawable;
                    i3 = this.shareStartX;
                    int dp4 = this.layoutHeight - AndroidUtilities.m26dp(41.0f);
                    this.shareStartY = dp4;
                    BaseCell.setDrawableBounds(drawable, i3, dp4);
                    Theme.chat_shareDrawable.draw(canvas2);
                    if (this.drwaShareGoIcon) {
                        BaseCell.setDrawableBounds(Theme.chat_goIconDrawable, this.shareStartX + AndroidUtilities.m26dp(12.0f), this.shareStartY + AndroidUtilities.m26dp(9.0f));
                        Theme.chat_goIconDrawable.draw(canvas2);
                    } else {
                        BaseCell.setDrawableBounds(Theme.chat_shareIconDrawable, this.shareStartX + AndroidUtilities.m26dp(8.0f), this.shareStartY + AndroidUtilities.m26dp(9.0f));
                        Theme.chat_shareIconDrawable.draw(canvas2);
                    }
                }
                if (this.replyNameLayout != null) {
                    if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                        if (this.currentMessageObject.isOutOwner()) {
                            this.replyStartX = AndroidUtilities.m26dp(23.0f);
                        } else if (this.currentMessageObject.type == 5) {
                            this.replyStartX = (this.backgroundDrawableLeft + this.backgroundDrawableRight) + AndroidUtilities.m26dp(4.0f);
                        } else {
                            this.replyStartX = (this.backgroundDrawableLeft + this.backgroundDrawableRight) + AndroidUtilities.m26dp(17.0f);
                        }
                        this.replyStartY = AndroidUtilities.m26dp(12.0f);
                    } else {
                        if (this.currentMessageObject.isOutOwner()) {
                            this.replyStartX = this.backgroundDrawableLeft + AndroidUtilities.m26dp(12.0f);
                        } else {
                            boolean z3 = this.mediaBackground;
                            if (z3) {
                                this.replyStartX = this.backgroundDrawableLeft + AndroidUtilities.m26dp(12.0f);
                            } else {
                                i3 = this.backgroundDrawableLeft;
                                if (z3 || !this.drawPinnedBottom) {
                                    f = 18.0f;
                                }
                                this.replyStartX = i3 + AndroidUtilities.m26dp(f);
                            }
                        }
                        i2 = (!this.drawForwardedName || this.forwardedNameLayout[0] == null) ? 0 : 36;
                        i = 12 + i2;
                        if (this.drawNameLayout && this.nameLayout != null) {
                            i4 = 20;
                        }
                        this.replyStartY = AndroidUtilities.m26dp((float) (i + i4));
                    }
                }
                if (this.currentPosition == null) {
                    drawNamesLayout(canvas);
                }
                if (!(this.autoPlayingMedia && MediaController.getInstance().isPlayingMessageAndReadyToDraw(this.currentMessageObject))) {
                    drawOverlays(canvas);
                }
                if ((this.drawTime || !this.mediaBackground) && !this.forceNotDrawTime) {
                    drawTime(canvas);
                }
                if (!((this.controlsAlpha == 1.0f && this.timeAlpha == 1.0f) || this.currentMessageObject.type == 5)) {
                    long currentTimeMillis2 = System.currentTimeMillis();
                    long abs2 = Math.abs(this.lastControlsAlphaChangeTime - currentTimeMillis2);
                    if (abs2 > 17) {
                        abs2 = 17;
                    }
                    this.totalChangeTime += abs2;
                    if (this.totalChangeTime > 100) {
                        this.totalChangeTime = 100;
                    }
                    this.lastControlsAlphaChangeTime = currentTimeMillis2;
                    if (this.controlsAlpha != 1.0f) {
                        this.controlsAlpha = AndroidUtilities.decelerateInterpolator.getInterpolation(((float) this.totalChangeTime) / 100.0f);
                    }
                    if (this.timeAlpha != 1.0f) {
                        this.timeAlpha = AndroidUtilities.decelerateInterpolator.getInterpolation(((float) this.totalChangeTime) / 100.0f);
                    }
                    invalidate();
                    if (this.forceNotDrawTime) {
                        GroupedMessagePosition groupedMessagePosition4 = this.currentPosition;
                        if (!(groupedMessagePosition4 == null || !groupedMessagePosition4.last || getParent() == null)) {
                            ((View) getParent()).invalidate();
                        }
                    }
                }
                return;
            }
            requestLayout();
        }
    }

    public void setTimeAlpha(float f) {
        this.timeAlpha = f;
    }

    public float getTimeAlpha() {
        return this.timeAlpha;
    }

    public int getBackgroundDrawableLeft() {
        int i = 0;
        if (this.currentMessageObject.isOutOwner()) {
            int i2 = this.layoutWidth - this.backgroundWidth;
            if (this.mediaBackground) {
                i = AndroidUtilities.m26dp(9.0f);
            }
            return i2 - i;
        }
        if (this.isChat && this.isAvatarVisible) {
            i = 48;
        }
        return AndroidUtilities.m26dp((float) (i + (!this.mediaBackground ? 3 : 9)));
    }

    public boolean hasNameLayout() {
        if (this.drawNameLayout && this.nameLayout != null) {
            return true;
        }
        if (this.drawForwardedName) {
            StaticLayout[] staticLayoutArr = this.forwardedNameLayout;
            if (!(staticLayoutArr[0] == null || staticLayoutArr[1] == null)) {
                GroupedMessagePosition groupedMessagePosition = this.currentPosition;
                if (groupedMessagePosition == null) {
                    return true;
                }
                if (groupedMessagePosition.minY == (byte) 0 && groupedMessagePosition.minX == (byte) 0) {
                    return true;
                }
            }
        }
        return this.replyNameLayout != null;
    }

    public boolean isDrawNameLayout() {
        return this.drawNameLayout && this.nameLayout != null;
    }

    public void drawNamesLayout(Canvas canvas) {
        int i;
        GroupedMessagePosition groupedMessagePosition;
        float f = 17.0f;
        int i2 = 0;
        if (this.drawNameLayout && this.nameLayout != null) {
            canvas.save();
            if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                Theme.chat_namePaint.setColor(Theme.getColor(Theme.key_chat_stickerNameText));
                if (this.currentMessageObject.isOutOwner()) {
                    this.nameX = (float) AndroidUtilities.m26dp(28.0f);
                } else {
                    this.nameX = (float) ((this.backgroundDrawableLeft + this.backgroundDrawableRight) + AndroidUtilities.m26dp(22.0f));
                }
                this.nameY = (float) (this.layoutHeight - AndroidUtilities.m26dp(38.0f));
                Theme.chat_systemDrawable.setColorFilter(Theme.colorFilter);
                Theme.chat_systemDrawable.setBounds(((int) this.nameX) - AndroidUtilities.m26dp(12.0f), ((int) this.nameY) - AndroidUtilities.m26dp(5.0f), (((int) this.nameX) + AndroidUtilities.m26dp(12.0f)) + this.nameWidth, ((int) this.nameY) + AndroidUtilities.m26dp(22.0f));
                Theme.chat_systemDrawable.draw(canvas);
            } else {
                if (this.mediaBackground || this.currentMessageObject.isOutOwner()) {
                    this.nameX = ((float) (this.backgroundDrawableLeft + AndroidUtilities.m26dp(11.0f))) - this.nameOffsetX;
                } else {
                    i = this.backgroundDrawableLeft;
                    float f2 = (this.mediaBackground || !this.drawPinnedBottom) ? 17.0f : 11.0f;
                    this.nameX = ((float) (i + AndroidUtilities.m26dp(f2))) - this.nameOffsetX;
                }
                User user = this.currentUser;
                if (user != null) {
                    Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(user.f534id));
                } else {
                    Chat chat = this.currentChat;
                    if (chat == null) {
                        Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(0));
                    } else if (!ChatObject.isChannel(chat) || this.currentChat.megagroup) {
                        Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(this.currentChat.f434id));
                    } else {
                        Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(5));
                    }
                }
                this.nameY = (float) AndroidUtilities.m26dp(this.drawPinnedTop ? 9.0f : 10.0f);
            }
            canvas.translate(this.nameX, this.nameY);
            this.nameLayout.draw(canvas);
            canvas.restore();
            if (this.adminLayout != null) {
                Theme.chat_adminPaint.setColor(Theme.getColor(isDrawSelectionBackground() ? Theme.key_chat_adminSelectedText : Theme.key_chat_adminText));
                canvas.save();
                canvas.translate(((float) ((this.backgroundDrawableLeft + this.backgroundDrawableRight) - AndroidUtilities.m26dp(11.0f))) - this.adminLayout.getLineWidth(0), this.nameY + ((float) AndroidUtilities.m26dp(0.5f)));
                this.adminLayout.draw(canvas);
                canvas.restore();
            }
        }
        boolean z = this.drawForwardedName;
        String str = Theme.key_chat_stickerReplyNameText;
        if (z) {
            StaticLayout[] staticLayoutArr = this.forwardedNameLayout;
            if (!(staticLayoutArr[0] == null || staticLayoutArr[1] == null)) {
                groupedMessagePosition = this.currentPosition;
                if (groupedMessagePosition == null || (groupedMessagePosition.minY == (byte) 0 && groupedMessagePosition.minX == (byte) 0)) {
                    if (this.currentMessageObject.type == 5) {
                        Theme.chat_forwardNamePaint.setColor(Theme.getColor(str));
                        if (this.currentMessageObject.isOutOwner()) {
                            this.forwardNameX = AndroidUtilities.m26dp(23.0f);
                        } else {
                            this.forwardNameX = (this.backgroundDrawableLeft + this.backgroundDrawableRight) + AndroidUtilities.m26dp(17.0f);
                        }
                        this.forwardNameY = AndroidUtilities.m26dp(12.0f);
                        i = this.forwardedNameWidth + AndroidUtilities.m26dp(14.0f);
                        Theme.chat_systemDrawable.setColorFilter(Theme.colorFilter);
                        Theme.chat_systemDrawable.setBounds(this.forwardNameX - AndroidUtilities.m26dp(7.0f), this.forwardNameY - AndroidUtilities.m26dp(6.0f), (this.forwardNameX - AndroidUtilities.m26dp(7.0f)) + i, this.forwardNameY + AndroidUtilities.m26dp(38.0f));
                        Theme.chat_systemDrawable.draw(canvas);
                    } else {
                        this.forwardNameY = AndroidUtilities.m26dp((float) ((this.drawNameLayout ? 19 : 0) + 10));
                        if (this.currentMessageObject.isOutOwner()) {
                            Theme.chat_forwardNamePaint.setColor(Theme.getColor(Theme.key_chat_outForwardedNameText));
                            this.forwardNameX = this.backgroundDrawableLeft + AndroidUtilities.m26dp(11.0f);
                        } else {
                            Theme.chat_forwardNamePaint.setColor(Theme.getColor(Theme.key_chat_inForwardedNameText));
                            z = this.mediaBackground;
                            if (z) {
                                this.forwardNameX = this.backgroundDrawableLeft + AndroidUtilities.m26dp(11.0f);
                            } else {
                                int i3 = this.backgroundDrawableLeft;
                                if (!z && this.drawPinnedBottom) {
                                    f = 11.0f;
                                }
                                this.forwardNameX = i3 + AndroidUtilities.m26dp(f);
                            }
                        }
                    }
                    for (i = 0; i < 2; i++) {
                        canvas.save();
                        canvas.translate(((float) this.forwardNameX) - this.forwardNameOffsetX[i], (float) (this.forwardNameY + (AndroidUtilities.m26dp(16.0f) * i)));
                        this.forwardedNameLayout[i].draw(canvas);
                        canvas.restore();
                    }
                }
            }
        }
        if (this.replyNameLayout != null) {
            MessageObject messageObject;
            MessageMedia messageMedia;
            if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                Theme.chat_replyLinePaint.setColor(Theme.getColor(Theme.key_chat_stickerReplyLine));
                Theme.chat_replyNamePaint.setColor(Theme.getColor(str));
                Theme.chat_replyTextPaint.setColor(Theme.getColor(Theme.key_chat_stickerReplyMessageText));
                i = Math.max(this.replyNameWidth, this.replyTextWidth) + AndroidUtilities.m26dp(14.0f);
                Theme.chat_systemDrawable.setColorFilter(Theme.colorFilter);
                Theme.chat_systemDrawable.setBounds(this.replyStartX - AndroidUtilities.m26dp(7.0f), this.replyStartY - AndroidUtilities.m26dp(6.0f), (this.replyStartX - AndroidUtilities.m26dp(7.0f)) + i, this.replyStartY + AndroidUtilities.m26dp(41.0f));
                Theme.chat_systemDrawable.draw(canvas);
            } else if (this.currentMessageObject.isOutOwner()) {
                Theme.chat_replyLinePaint.setColor(Theme.getColor(Theme.key_chat_outReplyLine));
                Theme.chat_replyNamePaint.setColor(Theme.getColor(Theme.key_chat_outReplyNameText));
                if (this.currentMessageObject.hasValidReplyMessageObject()) {
                    messageObject = this.currentMessageObject.replyMessageObject;
                    if (messageObject.type == 0) {
                        messageMedia = messageObject.messageOwner.media;
                        if (!((messageMedia instanceof TL_messageMediaGame) || (messageMedia instanceof TL_messageMediaInvoice))) {
                            Theme.chat_replyTextPaint.setColor(Theme.getColor(Theme.key_chat_outReplyMessageText));
                        }
                    }
                }
                Theme.chat_replyTextPaint.setColor(Theme.getColor(isDrawSelectionBackground() ? Theme.key_chat_outReplyMediaMessageSelectedText : Theme.key_chat_outReplyMediaMessageText));
            } else {
                Theme.chat_replyLinePaint.setColor(Theme.getColor(Theme.key_chat_inReplyLine));
                Theme.chat_replyNamePaint.setColor(Theme.getColor(Theme.key_chat_inReplyNameText));
                if (this.currentMessageObject.hasValidReplyMessageObject()) {
                    messageObject = this.currentMessageObject.replyMessageObject;
                    if (messageObject.type == 0) {
                        messageMedia = messageObject.messageOwner.media;
                        if (!((messageMedia instanceof TL_messageMediaGame) || (messageMedia instanceof TL_messageMediaInvoice))) {
                            Theme.chat_replyTextPaint.setColor(Theme.getColor(Theme.key_chat_inReplyMessageText));
                        }
                    }
                }
                Theme.chat_replyTextPaint.setColor(Theme.getColor(isDrawSelectionBackground() ? Theme.key_chat_inReplyMediaMessageSelectedText : Theme.key_chat_inReplyMediaMessageText));
            }
            groupedMessagePosition = this.currentPosition;
            if (groupedMessagePosition == null || (groupedMessagePosition.minY == (byte) 0 && groupedMessagePosition.minX == (byte) 0)) {
                i = this.replyStartX;
                canvas.drawRect((float) i, (float) this.replyStartY, (float) (i + AndroidUtilities.m26dp(2.0f)), (float) (this.replyStartY + AndroidUtilities.m26dp(35.0f)), Theme.chat_replyLinePaint);
                if (this.needReplyImage) {
                    this.replyImageReceiver.setImageCoords(this.replyStartX + AndroidUtilities.m26dp(10.0f), this.replyStartY, AndroidUtilities.m26dp(35.0f), AndroidUtilities.m26dp(35.0f));
                    this.replyImageReceiver.draw(canvas);
                }
                if (this.replyNameLayout != null) {
                    canvas.save();
                    canvas.translate((((float) this.replyStartX) - this.replyNameOffset) + ((float) AndroidUtilities.m26dp((float) ((this.needReplyImage ? 44 : 0) + 10))), (float) this.replyStartY);
                    this.replyNameLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.replyTextLayout != null) {
                    canvas.save();
                    float f3 = ((float) this.replyStartX) - this.replyTextOffset;
                    if (this.needReplyImage) {
                        i2 = 44;
                    }
                    canvas.translate(f3 + ((float) AndroidUtilities.m26dp((float) (i2 + 10))), (float) (this.replyStartY + AndroidUtilities.m26dp(19.0f)));
                    this.replyTextLayout.draw(canvas);
                    canvas.restore();
                }
            }
        }
    }

    public boolean hasCaptionLayout() {
        return this.captionLayout != null;
    }

    public void setDrawSelectionBackground(boolean z) {
        this.drawSelectionBackground = z;
        invalidate();
    }

    public boolean isDrawingSelectionBackground() {
        return this.drawSelectionBackground || this.isHighlightedAnimated || this.isHighlighted;
    }

    public float getHightlightAlpha() {
        if (this.drawSelectionBackground || !this.isHighlightedAnimated) {
            return 1.0f;
        }
        int i = this.highlightProgress;
        if (i >= 300) {
            return 1.0f;
        }
        return ((float) i) / 300.0f;
    }

    public void setCheckBoxVisible(boolean z, boolean z2) {
        if (z && this.checkBox == null) {
            this.checkBox = new CheckBoxBase(this);
            if (this.attachedToWindow) {
                this.checkBox.onAttachedToWindow();
            }
        }
        if (z && this.photoCheckBox == null) {
            GroupedMessages groupedMessages = this.currentMessagesGroup;
            if (groupedMessages != null && groupedMessages.messages.size() > 1) {
                this.photoCheckBox = new CheckBoxBase(this);
                this.photoCheckBox.setUseDefaultCheck(true);
                if (this.attachedToWindow) {
                    this.photoCheckBox.onAttachedToWindow();
                }
            }
        }
        float f = 1.0f;
        if (this.checkBoxVisible == z) {
            if (!(z2 == this.checkBoxAnimationInProgress || z2)) {
                if (!z) {
                    f = 0.0f;
                }
                this.checkBoxAnimationProgress = f;
                invalidate();
            }
            return;
        }
        this.checkBoxAnimationInProgress = z2;
        this.checkBoxVisible = z;
        if (z2) {
            this.lastCheckBoxAnimationTime = SystemClock.uptimeMillis();
        } else {
            if (!z) {
                f = 0.0f;
            }
            this.checkBoxAnimationProgress = f;
        }
        invalidate();
    }

    public void setChecked(boolean z, boolean z2, boolean z3) {
        CheckBoxBase checkBoxBase = this.checkBox;
        if (checkBoxBase != null) {
            checkBoxBase.setChecked(z2, z3);
        }
        CheckBoxBase checkBoxBase2 = this.photoCheckBox;
        if (checkBoxBase2 != null) {
            checkBoxBase2.setChecked(z, z3);
        }
    }

    public void drawCaptionLayout(Canvas canvas, boolean z) {
        if (this.captionLayout == null) {
            return;
        }
        if (!z || this.pressedLink != null) {
            canvas.save();
            canvas.translate((float) this.captionX, (float) this.captionY);
            if (this.pressedLink != null) {
                for (int i = 0; i < this.urlPath.size(); i++) {
                    canvas.drawPath((Path) this.urlPath.get(i), Theme.chat_urlPaint);
                }
            }
            if (!this.urlPathSelection.isEmpty()) {
                for (int i2 = 0; i2 < this.urlPathSelection.size(); i2++) {
                    canvas.drawPath((Path) this.urlPathSelection.get(i2), Theme.chat_textSearchSelectionPaint);
                }
            }
            if (!z) {
                try {
                    this.captionLayout.draw(canvas);
                } catch (Exception e) {
                    FileLog.m30e(e);
                }
            }
            canvas.restore();
        }
    }

    public boolean needDrawTime() {
        return this.forceNotDrawTime ^ 1;
    }

    public void drawTime(Canvas canvas) {
        Canvas canvas2 = canvas;
        if (((this.drawTime && !this.groupPhotoInvisible) || !this.mediaBackground || this.captionLayout != null) && this.timeLayout != null) {
            int i;
            int alpha;
            MessageObject messageObject = this.currentMessageObject;
            int i2 = messageObject.type;
            String str = Theme.key_chat_mediaTimeText;
            if (i2 == 5) {
                Theme.chat_timePaint.setColor(Theme.getColor(str));
            } else if (this.mediaBackground && this.captionLayout == null) {
                if (messageObject.shouldDrawWithoutBackground()) {
                    Theme.chat_timePaint.setColor(Theme.getColor(Theme.key_chat_serviceText));
                } else {
                    Theme.chat_timePaint.setColor(Theme.getColor(str));
                }
            } else if (this.currentMessageObject.isOutOwner()) {
                Theme.chat_timePaint.setColor(Theme.getColor(isDrawSelectionBackground() ? Theme.key_chat_outTimeSelectedText : Theme.key_chat_outTimeText));
            } else {
                Theme.chat_timePaint.setColor(Theme.getColor(isDrawSelectionBackground() ? Theme.key_chat_inTimeSelectedText : Theme.key_chat_inTimeText));
            }
            if (this.drawPinnedBottom) {
                canvas2.translate(0.0f, (float) AndroidUtilities.m26dp(2.0f));
            }
            int i3 = 0;
            Drawable drawable;
            if (this.mediaBackground && this.captionLayout == null) {
                Paint paint;
                if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                    paint = Theme.chat_actionBackgroundPaint;
                } else {
                    paint = Theme.chat_timeBackgroundPaint;
                }
                int alpha2 = paint.getAlpha();
                paint.setAlpha((int) (((float) alpha2) * this.timeAlpha));
                Theme.chat_timePaint.setAlpha((int) (this.timeAlpha * 255.0f));
                int dp = this.timeX - AndroidUtilities.m26dp(4.0f);
                int dp2 = this.layoutHeight - AndroidUtilities.m26dp(28.0f);
                this.rect.set((float) dp, (float) dp2, (float) ((dp + this.timeWidth) + AndroidUtilities.m26dp((float) ((this.currentMessageObject.isOutOwner() ? 20 : 0) + 8))), (float) (dp2 + AndroidUtilities.m26dp(17.0f)));
                canvas2.drawRoundRect(this.rect, (float) AndroidUtilities.m26dp(4.0f), (float) AndroidUtilities.m26dp(4.0f), paint);
                paint.setAlpha(alpha2);
                i = (int) (-this.timeLayout.getLineLeft(0));
                if ((this.currentMessageObject.messageOwner.flags & 1024) != 0) {
                    i += (int) (((float) this.timeWidth) - this.timeLayout.getLineWidth(0));
                    if (this.currentMessageObject.isSending() || this.currentMessageObject.isEditing()) {
                        if (!this.currentMessageObject.isOutOwner()) {
                            BaseCell.setDrawableBounds(Theme.chat_msgMediaClockDrawable, this.timeX + AndroidUtilities.m26dp(11.0f), (this.layoutHeight - AndroidUtilities.m26dp(14.0f)) - Theme.chat_msgMediaClockDrawable.getIntrinsicHeight());
                            Theme.chat_msgMediaClockDrawable.draw(canvas2);
                        }
                    } else if (!this.currentMessageObject.isSendError()) {
                        if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                            drawable = Theme.chat_msgStickerViewsDrawable;
                        } else {
                            drawable = Theme.chat_msgMediaViewsDrawable;
                        }
                        alpha = ((BitmapDrawable) drawable).getPaint().getAlpha();
                        drawable.setAlpha((int) (this.timeAlpha * ((float) alpha)));
                        BaseCell.setDrawableBounds(drawable, this.timeX, (this.layoutHeight - AndroidUtilities.m26dp(10.5f)) - this.timeLayout.getHeight());
                        drawable.draw(canvas2);
                        drawable.setAlpha(alpha);
                        if (this.viewsLayout != null) {
                            canvas.save();
                            canvas2.translate((float) ((this.timeX + drawable.getIntrinsicWidth()) + AndroidUtilities.m26dp(3.0f)), (float) ((this.layoutHeight - AndroidUtilities.m26dp(12.3f)) - this.timeLayout.getHeight()));
                            this.viewsLayout.draw(canvas2);
                            canvas.restore();
                        }
                    } else if (!this.currentMessageObject.isOutOwner()) {
                        i2 = this.timeX + AndroidUtilities.m26dp(11.0f);
                        alpha = this.layoutHeight - AndroidUtilities.m26dp(27.5f);
                        this.rect.set((float) i2, (float) alpha, (float) (AndroidUtilities.m26dp(14.0f) + i2), (float) (AndroidUtilities.m26dp(14.0f) + alpha));
                        canvas2.drawRoundRect(this.rect, (float) AndroidUtilities.m26dp(1.0f), (float) AndroidUtilities.m26dp(1.0f), Theme.chat_msgErrorPaint);
                        BaseCell.setDrawableBounds(Theme.chat_msgErrorDrawable, i2 + AndroidUtilities.m26dp(6.0f), alpha + AndroidUtilities.m26dp(2.0f));
                        Theme.chat_msgErrorDrawable.draw(canvas2);
                    }
                }
                canvas.save();
                canvas2.translate((float) (this.timeX + i), (float) ((this.layoutHeight - AndroidUtilities.m26dp(12.3f)) - this.timeLayout.getHeight()));
                this.timeLayout.draw(canvas2);
                canvas.restore();
                Theme.chat_timePaint.setAlpha(NalUnitUtil.EXTENDED_SAR);
            } else {
                i = (int) (-this.timeLayout.getLineLeft(0));
                if ((this.currentMessageObject.messageOwner.flags & 1024) != 0) {
                    i += (int) (((float) this.timeWidth) - this.timeLayout.getLineWidth(0));
                    if (this.currentMessageObject.isSending() || this.currentMessageObject.isEditing()) {
                        if (!this.currentMessageObject.isOutOwner()) {
                            drawable = isDrawSelectionBackground() ? Theme.chat_msgInSelectedClockDrawable : Theme.chat_msgInClockDrawable;
                            BaseCell.setDrawableBounds(drawable, this.timeX + AndroidUtilities.m26dp(11.0f), (this.layoutHeight - AndroidUtilities.m26dp(8.5f)) - drawable.getIntrinsicHeight());
                            drawable.draw(canvas2);
                        }
                    } else if (!this.currentMessageObject.isSendError()) {
                        if (this.currentMessageObject.isOutOwner()) {
                            drawable = isDrawSelectionBackground() ? Theme.chat_msgOutViewsSelectedDrawable : Theme.chat_msgOutViewsDrawable;
                            BaseCell.setDrawableBounds(drawable, this.timeX, (this.layoutHeight - AndroidUtilities.m26dp(4.5f)) - this.timeLayout.getHeight());
                            drawable.draw(canvas2);
                        } else {
                            drawable = isDrawSelectionBackground() ? Theme.chat_msgInViewsSelectedDrawable : Theme.chat_msgInViewsDrawable;
                            BaseCell.setDrawableBounds(drawable, this.timeX, (this.layoutHeight - AndroidUtilities.m26dp(4.5f)) - this.timeLayout.getHeight());
                            drawable.draw(canvas2);
                        }
                        if (this.viewsLayout != null) {
                            canvas.save();
                            canvas2.translate((float) ((this.timeX + Theme.chat_msgInViewsDrawable.getIntrinsicWidth()) + AndroidUtilities.m26dp(3.0f)), (float) ((this.layoutHeight - AndroidUtilities.m26dp(6.5f)) - this.timeLayout.getHeight()));
                            this.viewsLayout.draw(canvas2);
                            canvas.restore();
                        }
                    } else if (!this.currentMessageObject.isOutOwner()) {
                        i2 = this.timeX + AndroidUtilities.m26dp(11.0f);
                        alpha = this.layoutHeight - AndroidUtilities.m26dp(20.5f);
                        this.rect.set((float) i2, (float) alpha, (float) (AndroidUtilities.m26dp(14.0f) + i2), (float) (AndroidUtilities.m26dp(14.0f) + alpha));
                        canvas2.drawRoundRect(this.rect, (float) AndroidUtilities.m26dp(1.0f), (float) AndroidUtilities.m26dp(1.0f), Theme.chat_msgErrorPaint);
                        BaseCell.setDrawableBounds(Theme.chat_msgErrorDrawable, i2 + AndroidUtilities.m26dp(6.0f), alpha + AndroidUtilities.m26dp(2.0f));
                        Theme.chat_msgErrorDrawable.draw(canvas2);
                    }
                }
                canvas.save();
                canvas2.translate((float) (this.timeX + i), (float) ((this.layoutHeight - AndroidUtilities.m26dp(6.5f)) - this.timeLayout.getHeight()));
                this.timeLayout.draw(canvas2);
                canvas.restore();
            }
            if (this.currentMessageObject.isOutOwner()) {
                Object obj;
                Object obj2;
                Object obj3 = 1;
                Object obj4 = ((int) (this.currentMessageObject.getDialogId() >> 32)) == 1 ? 1 : null;
                if (this.currentMessageObject.isSending() || this.currentMessageObject.isEditing()) {
                    obj3 = null;
                    obj = null;
                    obj2 = null;
                    i3 = 1;
                } else if (this.currentMessageObject.isSendError()) {
                    obj3 = null;
                    obj = null;
                    obj2 = 1;
                } else {
                    if (!this.currentMessageObject.isSent()) {
                        obj3 = null;
                    } else if (!this.currentMessageObject.isUnread()) {
                        obj = 1;
                        obj2 = null;
                    }
                    obj = null;
                    obj2 = null;
                }
                if (i3 != 0) {
                    if (!this.mediaBackground || this.captionLayout != null) {
                        BaseCell.setDrawableBounds(Theme.chat_msgOutClockDrawable, (this.layoutWidth - AndroidUtilities.m26dp(18.5f)) - Theme.chat_msgOutClockDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(8.5f)) - Theme.chat_msgOutClockDrawable.getIntrinsicHeight());
                        Theme.chat_msgOutClockDrawable.draw(canvas2);
                    } else if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                        Theme.chat_msgStickerClockDrawable.setAlpha((int) (this.timeAlpha * 255.0f));
                        BaseCell.setDrawableBounds(Theme.chat_msgStickerClockDrawable, (this.layoutWidth - AndroidUtilities.m26dp(22.0f)) - Theme.chat_msgStickerClockDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(13.5f)) - Theme.chat_msgStickerClockDrawable.getIntrinsicHeight());
                        Theme.chat_msgStickerClockDrawable.draw(canvas2);
                        Theme.chat_msgStickerClockDrawable.setAlpha(NalUnitUtil.EXTENDED_SAR);
                    } else {
                        BaseCell.setDrawableBounds(Theme.chat_msgMediaClockDrawable, (this.layoutWidth - AndroidUtilities.m26dp(22.0f)) - Theme.chat_msgMediaClockDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(13.5f)) - Theme.chat_msgMediaClockDrawable.getIntrinsicHeight());
                        Theme.chat_msgMediaClockDrawable.draw(canvas2);
                    }
                }
                if (obj4 == null) {
                    Drawable drawable2;
                    if (obj3 != null) {
                        if (!this.mediaBackground || this.captionLayout != null) {
                            drawable2 = isDrawSelectionBackground() ? Theme.chat_msgOutCheckSelectedDrawable : Theme.chat_msgOutCheckDrawable;
                            if (obj != null) {
                                BaseCell.setDrawableBounds(drawable2, (this.layoutWidth - AndroidUtilities.m26dp(22.5f)) - drawable2.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(8.0f)) - drawable2.getIntrinsicHeight());
                            } else {
                                BaseCell.setDrawableBounds(drawable2, (this.layoutWidth - AndroidUtilities.m26dp(18.5f)) - drawable2.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(8.0f)) - drawable2.getIntrinsicHeight());
                            }
                            drawable2.draw(canvas2);
                        } else if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                            if (obj != null) {
                                BaseCell.setDrawableBounds(Theme.chat_msgStickerCheckDrawable, (this.layoutWidth - AndroidUtilities.m26dp(26.3f)) - Theme.chat_msgStickerCheckDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(13.5f)) - Theme.chat_msgStickerCheckDrawable.getIntrinsicHeight());
                            } else {
                                BaseCell.setDrawableBounds(Theme.chat_msgStickerCheckDrawable, (this.layoutWidth - AndroidUtilities.m26dp(21.5f)) - Theme.chat_msgStickerCheckDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(13.5f)) - Theme.chat_msgStickerCheckDrawable.getIntrinsicHeight());
                            }
                            Theme.chat_msgStickerCheckDrawable.draw(canvas2);
                        } else {
                            if (obj != null) {
                                BaseCell.setDrawableBounds(Theme.chat_msgMediaCheckDrawable, (this.layoutWidth - AndroidUtilities.m26dp(26.3f)) - Theme.chat_msgMediaCheckDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(13.5f)) - Theme.chat_msgMediaCheckDrawable.getIntrinsicHeight());
                            } else {
                                BaseCell.setDrawableBounds(Theme.chat_msgMediaCheckDrawable, (this.layoutWidth - AndroidUtilities.m26dp(21.5f)) - Theme.chat_msgMediaCheckDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(13.5f)) - Theme.chat_msgMediaCheckDrawable.getIntrinsicHeight());
                            }
                            Theme.chat_msgMediaCheckDrawable.setAlpha((int) (this.timeAlpha * 255.0f));
                            Theme.chat_msgMediaCheckDrawable.draw(canvas2);
                            Theme.chat_msgMediaCheckDrawable.setAlpha(NalUnitUtil.EXTENDED_SAR);
                        }
                    }
                    if (obj != null) {
                        if (!this.mediaBackground || this.captionLayout != null) {
                            drawable2 = isDrawSelectionBackground() ? Theme.chat_msgOutHalfCheckSelectedDrawable : Theme.chat_msgOutHalfCheckDrawable;
                            BaseCell.setDrawableBounds(drawable2, (this.layoutWidth - AndroidUtilities.m26dp(18.0f)) - drawable2.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(8.0f)) - drawable2.getIntrinsicHeight());
                            drawable2.draw(canvas2);
                        } else if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                            BaseCell.setDrawableBounds(Theme.chat_msgStickerHalfCheckDrawable, (this.layoutWidth - AndroidUtilities.m26dp(21.5f)) - Theme.chat_msgStickerHalfCheckDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(13.5f)) - Theme.chat_msgStickerHalfCheckDrawable.getIntrinsicHeight());
                            Theme.chat_msgStickerHalfCheckDrawable.draw(canvas2);
                        } else {
                            BaseCell.setDrawableBounds(Theme.chat_msgMediaHalfCheckDrawable, (this.layoutWidth - AndroidUtilities.m26dp(21.5f)) - Theme.chat_msgMediaHalfCheckDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(13.5f)) - Theme.chat_msgMediaHalfCheckDrawable.getIntrinsicHeight());
                            Theme.chat_msgMediaHalfCheckDrawable.setAlpha((int) (this.timeAlpha * 255.0f));
                            Theme.chat_msgMediaHalfCheckDrawable.draw(canvas2);
                            Theme.chat_msgMediaHalfCheckDrawable.setAlpha(NalUnitUtil.EXTENDED_SAR);
                        }
                    }
                } else if (!(obj == null && obj3 == null)) {
                    if (this.mediaBackground && this.captionLayout == null) {
                        BaseCell.setDrawableBounds(Theme.chat_msgBroadcastMediaDrawable, (this.layoutWidth - AndroidUtilities.m26dp(24.0f)) - Theme.chat_msgBroadcastMediaDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(14.0f)) - Theme.chat_msgBroadcastMediaDrawable.getIntrinsicHeight());
                        Theme.chat_msgBroadcastMediaDrawable.draw(canvas2);
                    } else {
                        BaseCell.setDrawableBounds(Theme.chat_msgBroadcastDrawable, (this.layoutWidth - AndroidUtilities.m26dp(20.5f)) - Theme.chat_msgBroadcastDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.m26dp(8.0f)) - Theme.chat_msgBroadcastDrawable.getIntrinsicHeight());
                        Theme.chat_msgBroadcastDrawable.draw(canvas2);
                    }
                }
                if (obj2 != null) {
                    if (this.mediaBackground && this.captionLayout == null) {
                        i = this.layoutWidth - AndroidUtilities.m26dp(34.5f);
                        i2 = this.layoutHeight;
                        alpha = AndroidUtilities.m26dp(26.5f);
                    } else {
                        i = this.layoutWidth - AndroidUtilities.m26dp(32.0f);
                        i2 = this.layoutHeight;
                        alpha = AndroidUtilities.m26dp(21.0f);
                    }
                    i2 -= alpha;
                    this.rect.set((float) i, (float) i2, (float) (AndroidUtilities.m26dp(14.0f) + i), (float) (AndroidUtilities.m26dp(14.0f) + i2));
                    canvas2.drawRoundRect(this.rect, (float) AndroidUtilities.m26dp(1.0f), (float) AndroidUtilities.m26dp(1.0f), Theme.chat_msgErrorPaint);
                    BaseCell.setDrawableBounds(Theme.chat_msgErrorDrawable, i + AndroidUtilities.m26dp(6.0f), i2 + AndroidUtilities.m26dp(2.0f));
                    Theme.chat_msgErrorDrawable.draw(canvas2);
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:374:0x0c07  */
    /* JADX WARNING: Removed duplicated region for block: B:384:0x0c29  */
    /* JADX WARNING: Removed duplicated region for block: B:397:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:388:0x0c37  */
    public void drawOverlays(android.graphics.Canvas r24) {
        /*
        r23 = this;
        r0 = r23;
        r7 = r24;
        r1 = android.os.SystemClock.uptimeMillis();
        r3 = r0.lastAnimationTime;
        r3 = r1 - r3;
        r5 = 17;
        r8 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r8 <= 0) goto L_0x0014;
    L_0x0012:
        r3 = 17;
    L_0x0014:
        r0.lastAnimationTime = r1;
        r1 = r0.currentMessageObject;
        r1 = r1.hadAnimationNotReadyLoading;
        r2 = 4;
        r8 = 2;
        r9 = 1;
        r10 = 0;
        if (r1 == 0) goto L_0x004e;
    L_0x0020:
        r1 = r0.photoImage;
        r1 = r1.getVisible();
        if (r1 == 0) goto L_0x004e;
    L_0x0028:
        r1 = r0.currentMessageObject;
        r1 = r1.needDrawBluredPreview();
        if (r1 != 0) goto L_0x004e;
    L_0x0030:
        r1 = r0.documentAttachType;
        r5 = 7;
        if (r1 == r5) goto L_0x0039;
    L_0x0035:
        if (r1 == r2) goto L_0x0039;
    L_0x0037:
        if (r1 != r8) goto L_0x004e;
    L_0x0039:
        r1 = r0.photoImage;
        r1 = r1.getAnimation();
        if (r1 == 0) goto L_0x004e;
    L_0x0041:
        r1 = r1.hasBitmap();
        if (r1 == 0) goto L_0x004e;
    L_0x0047:
        r1 = r0.currentMessageObject;
        r1.hadAnimationNotReadyLoading = r10;
        r0.updateButtonState(r10, r9, r10);
    L_0x004e:
        r1 = r0.currentMessageObject;
        r5 = r1.type;
        r11 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r12 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r13 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r14 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r15 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        if (r5 == r9) goto L_0x092f;
    L_0x005e:
        r6 = r0.documentAttachType;
        if (r6 == r2) goto L_0x092f;
    L_0x0062:
        if (r6 != r8) goto L_0x0066;
    L_0x0064:
        goto L_0x092f;
    L_0x0066:
        r3 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r17 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r18 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r5 != r2) goto L_0x029a;
    L_0x006e:
        r2 = r0.docTitleLayout;
        if (r2 == 0) goto L_0x0bf3;
    L_0x0072:
        r1 = r1.isOutOwner();
        if (r1 == 0) goto L_0x0098;
    L_0x0078:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_locationTitlePaint;
        r2 = "chat_messageTextOut";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_locationAddressPaint;
        r2 = r23.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x008e;
    L_0x008b:
        r2 = "chat_outVenueInfoSelectedText";
        goto L_0x0090;
    L_0x008e:
        r2 = "chat_outVenueInfoText";
    L_0x0090:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
        goto L_0x00b7;
    L_0x0098:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_locationTitlePaint;
        r2 = "chat_messageTextIn";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_locationAddressPaint;
        r2 = r23.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x00ae;
    L_0x00ab:
        r2 = "chat_inVenueInfoSelectedText";
        goto L_0x00b0;
    L_0x00ae:
        r2 = "chat_inVenueInfoText";
    L_0x00b0:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
    L_0x00b7:
        r1 = r0.currentMessageObject;
        r1 = r1.messageOwner;
        r1 = r1.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r1 == 0) goto L_0x025c;
    L_0x00c1:
        r1 = r0.photoImage;
        r1 = r1.getImageY2();
        r2 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r10 = r1 + r2;
        r1 = r0.locationExpired;
        if (r1 != 0) goto L_0x0202;
    L_0x00d3:
        r0.forceNotDrawTime = r9;
        r1 = r0.currentAccount;
        r1 = org.telegram.tgnet.ConnectionsManager.getInstance(r1);
        r1 = r1.getCurrentTime();
        r2 = r0.currentMessageObject;
        r2 = r2.messageOwner;
        r2 = r2.date;
        r1 = r1 - r2;
        r1 = java.lang.Math.abs(r1);
        r1 = (float) r1;
        r2 = r0.currentMessageObject;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2.period;
        r2 = (float) r2;
        r1 = r1 / r2;
        r1 = r15 - r1;
        r2 = r0.rect;
        r4 = r0.photoImage;
        r4 = r4.getImageX2();
        r5 = 1110179840; // 0x422c0000 float:43.0 double:5.485017196E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r4 = r4 - r5;
        r4 = (float) r4;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r5 = r10 - r5;
        r5 = (float) r5;
        r6 = r0.photoImage;
        r6 = r6.getImageX2();
        r9 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r6 = r6 - r9;
        r6 = (float) r6;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r9 = r9 + r10;
        r9 = (float) r9;
        r2.set(r4, r5, r6, r9);
        r2 = r0.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x0144;
    L_0x012d:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_radialProgress2Paint;
        r4 = "chat_outInstant";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r2.setColor(r4);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_livePaint;
        r4 = "chat_outInstant";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r2.setColor(r4);
        goto L_0x015a;
    L_0x0144:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_radialProgress2Paint;
        r4 = "chat_inInstant";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r2.setColor(r4);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_livePaint;
        r4 = "chat_inInstant";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r2.setColor(r4);
    L_0x015a:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_radialProgress2Paint;
        r4 = 50;
        r2.setAlpha(r4);
        r2 = r0.rect;
        r2 = r2.centerX();
        r4 = r0.rect;
        r4 = r4.centerY();
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = (float) r3;
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_radialProgress2Paint;
        r7.drawCircle(r2, r4, r3, r5);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_radialProgress2Paint;
        r2.setAlpha(r12);
        r2 = r0.rect;
        r3 = -1028390912; // 0xffffffffc2b40000 float:-90.0 double:NaN;
        r4 = -1011613696; // 0xffffffffc3b40000 float:-360.0 double:NaN;
        r4 = r4 * r1;
        r5 = 0;
        r6 = org.telegram.p004ui.ActionBar.Theme.chat_radialProgress2Paint;
        r1 = r24;
        r9 = 0;
        r1.drawArc(r2, r3, r4, r5, r6);
        r1 = r0.currentMessageObject;
        r1 = r1.messageOwner;
        r1 = r1.media;
        r1 = r1.period;
        r2 = r0.currentAccount;
        r2 = org.telegram.tgnet.ConnectionsManager.getInstance(r2);
        r2 = r2.getCurrentTime();
        r3 = r0.currentMessageObject;
        r3 = r3.messageOwner;
        r3 = r3.date;
        r2 = r2 - r3;
        r1 = r1 - r2;
        r1 = java.lang.Math.abs(r1);
        r1 = org.telegram.messenger.LocaleController.formatLocationLeftTime(r1);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_livePaint;
        r2 = r2.measureText(r1);
        r3 = r0.rect;
        r3 = r3.centerX();
        r2 = r2 / r18;
        r3 = r3 - r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r10 = r10 + r2;
        r2 = (float) r10;
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_livePaint;
        r7.drawText(r1, r3, r2, r4);
        r24.save();
        r1 = r0.photoImage;
        r1 = r1.getImageX();
        r2 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r1 = r1 + r2;
        r1 = (float) r1;
        r2 = r0.photoImage;
        r2 = r2.getImageY2();
        r3 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 + r3;
        r2 = (float) r2;
        r7.translate(r1, r2);
        r1 = r0.docTitleLayout;
        r1.draw(r7);
        r1 = 1102577664; // 0x41b80000 float:23.0 double:5.447457457E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r1 = (float) r1;
        r7.translate(r9, r1);
        r1 = r0.infoLayout;
        r1.draw(r7);
        r24.restore();
    L_0x0202:
        r1 = r0.photoImage;
        r1 = r1.getImageX();
        r2 = r0.photoImage;
        r2 = r2.getImageWidth();
        r2 = r2 / r8;
        r1 = r1 + r2;
        r2 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r1 = r1 - r2;
        r2 = r0.photoImage;
        r2 = r2.getImageY();
        r3 = r0.photoImage;
        r3 = r3.getImageHeight();
        r3 = r3 / r8;
        r2 = r2 + r3;
        r3 = 1108869120; // 0x42180000 float:38.0 double:5.47854138E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r3;
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_msgAvatarLiveLocationDrawable;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r3, r1, r2);
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_msgAvatarLiveLocationDrawable;
        r3.draw(r7);
        r3 = r0.locationImageReceiver;
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r1 = r1 + r4;
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r2 = r2 + r4;
        r4 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r5 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r3.setImageCoords(r1, r2, r4, r5);
        r1 = r0.locationImageReceiver;
        r1.draw(r7);
        goto L_0x0bf3;
    L_0x025c:
        r9 = 0;
        r24.save();
        r1 = r0.photoImage;
        r1 = r1.getImageX();
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r1 = r1 + r2;
        r1 = (float) r1;
        r2 = r0.photoImage;
        r2 = r2.getImageY2();
        r3 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 + r3;
        r2 = (float) r2;
        r7.translate(r1, r2);
        r1 = r0.docTitleLayout;
        r1.draw(r7);
        r1 = r0.infoLayout;
        if (r1 == 0) goto L_0x0295;
    L_0x0286:
        r1 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r1 = (float) r1;
        r7.translate(r9, r1);
        r1 = r0.infoLayout;
        r1.draw(r7);
    L_0x0295:
        r24.restore();
        goto L_0x0bf3;
    L_0x029a:
        r8 = 0;
        r2 = 16;
        if (r5 != r2) goto L_0x03c6;
    L_0x029f:
        r1 = r1.isOutOwner();
        if (r1 == 0) goto L_0x02c5;
    L_0x02a5:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_audioTitlePaint;
        r2 = "chat_messageTextOut";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_contactPhonePaint;
        r2 = r23.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x02bb;
    L_0x02b8:
        r2 = "chat_outTimeSelectedText";
        goto L_0x02bd;
    L_0x02bb:
        r2 = "chat_outTimeText";
    L_0x02bd:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
        goto L_0x02e4;
    L_0x02c5:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_audioTitlePaint;
        r2 = "chat_messageTextIn";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_contactPhonePaint;
        r2 = r23.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x02db;
    L_0x02d8:
        r2 = "chat_inTimeSelectedText";
        goto L_0x02dd;
    L_0x02db:
        r2 = "chat_inTimeText";
    L_0x02dd:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
    L_0x02e4:
        r0.forceNotDrawTime = r9;
        r1 = r0.currentMessageObject;
        r1 = r1.isOutOwner();
        if (r1 == 0) goto L_0x02fb;
    L_0x02ee:
        r1 = r0.layoutWidth;
        r2 = r0.backgroundWidth;
        r1 = r1 - r2;
        r2 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r1 = r1 + r2;
        goto L_0x0314;
    L_0x02fb:
        r1 = r0.isChat;
        if (r1 == 0) goto L_0x030e;
    L_0x02ff:
        r1 = r0.currentMessageObject;
        r1 = r1.needDrawAvatar();
        if (r1 == 0) goto L_0x030e;
    L_0x0307:
        r1 = 1116995584; // 0x42940000 float:74.0 double:5.518691446E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        goto L_0x0314;
    L_0x030e:
        r1 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
    L_0x0314:
        r0.otherX = r1;
        r2 = r0.titleLayout;
        if (r2 == 0) goto L_0x0333;
    L_0x031a:
        r24.save();
        r2 = (float) r1;
        r3 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r4 = r0.namesOffset;
        r3 = r3 + r4;
        r3 = (float) r3;
        r7.translate(r2, r3);
        r2 = r0.titleLayout;
        r2.draw(r7);
        r24.restore();
    L_0x0333:
        r2 = r0.docTitleLayout;
        if (r2 == 0) goto L_0x0357;
    L_0x0337:
        r24.save();
        r2 = 1100480512; // 0x41980000 float:19.0 double:5.43709615E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = r2 + r1;
        r2 = (float) r2;
        r3 = 1108606976; // 0x42140000 float:37.0 double:5.477246216E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r4 = r0.namesOffset;
        r3 = r3 + r4;
        r3 = (float) r3;
        r7.translate(r2, r3);
        r2 = r0.docTitleLayout;
        r2.draw(r7);
        r24.restore();
    L_0x0357:
        r2 = r0.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x0372;
    L_0x035f:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_msgCallUpGreenDrawable;
        r3 = r23.isDrawSelectionBackground();
        if (r3 != 0) goto L_0x036f;
    L_0x0367:
        r3 = r0.otherPressed;
        if (r3 == 0) goto L_0x036c;
    L_0x036b:
        goto L_0x036f;
    L_0x036c:
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_msgOutCallDrawable;
        goto L_0x0398;
    L_0x036f:
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_msgOutCallSelectedDrawable;
        goto L_0x0398;
    L_0x0372:
        r2 = r0.currentMessageObject;
        r2 = r2.messageOwner;
        r2 = r2.action;
        r2 = r2.reason;
        r3 = r2 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonMissed;
        if (r3 != 0) goto L_0x0386;
    L_0x037e:
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonBusy;
        if (r2 == 0) goto L_0x0383;
    L_0x0382:
        goto L_0x0386;
    L_0x0383:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_msgCallDownGreenDrawable;
        goto L_0x0388;
    L_0x0386:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_msgCallDownRedDrawable;
    L_0x0388:
        r3 = r23.isDrawSelectionBackground();
        if (r3 != 0) goto L_0x0396;
    L_0x038e:
        r3 = r0.otherPressed;
        if (r3 == 0) goto L_0x0393;
    L_0x0392:
        goto L_0x0396;
    L_0x0393:
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_msgInCallDrawable;
        goto L_0x0398;
    L_0x0396:
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_msgInCallSelectedDrawable;
    L_0x0398:
        r4 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r4 = r1 - r4;
        r5 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r6 = r0.namesOffset;
        r5 = r5 + r6;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r2, r4, r5);
        r2.draw(r7);
        r2 = 1129119744; // 0x434d0000 float:205.0 double:5.578592756E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r1 = r1 + r2;
        r2 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0.otherY = r2;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r3, r1, r2);
        r3.draw(r7);
        goto L_0x0bf3;
    L_0x03c6:
        r2 = 17;
        r16 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        if (r5 != r2) goto L_0x0799;
    L_0x03cc:
        r1 = r1.isOutOwner();
        if (r1 == 0) goto L_0x0401;
    L_0x03d2:
        r1 = "chat_messageTextOut";
        r1 = org.telegram.p004ui.ActionBar.Theme.getColor(r1);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_audioTitlePaint;
        r2.setColor(r1);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_audioPerformerPaint;
        r2.setColor(r1);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r2.setColor(r1);
        r1 = r23.isDrawSelectionBackground();
        if (r1 == 0) goto L_0x03f0;
    L_0x03ed:
        r1 = "chat_outTimeSelectedText";
        goto L_0x03f2;
    L_0x03f0:
        r1 = "chat_outTimeText";
    L_0x03f2:
        r1 = org.telegram.p004ui.ActionBar.Theme.getColor(r1);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_timePaint;
        r2.setColor(r1);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_livePaint;
        r2.setColor(r1);
        goto L_0x042f;
    L_0x0401:
        r1 = "chat_messageTextIn";
        r1 = org.telegram.p004ui.ActionBar.Theme.getColor(r1);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_audioTitlePaint;
        r2.setColor(r1);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_audioPerformerPaint;
        r2.setColor(r1);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r2.setColor(r1);
        r1 = r23.isDrawSelectionBackground();
        if (r1 == 0) goto L_0x041f;
    L_0x041c:
        r1 = "chat_inTimeSelectedText";
        goto L_0x0421;
    L_0x041f:
        r1 = "chat_inTimeText";
    L_0x0421:
        r1 = org.telegram.p004ui.ActionBar.Theme.getColor(r1);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_timePaint;
        r2.setColor(r1);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_livePaint;
        r2.setColor(r1);
    L_0x042f:
        r1 = r0.currentMessageObject;
        r1 = r1.isOutOwner();
        if (r1 == 0) goto L_0x0445;
    L_0x0437:
        r1 = r0.layoutWidth;
        r2 = r0.backgroundWidth;
        r1 = r1 - r2;
        r2 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r1 = r1 + r2;
    L_0x0443:
        r6 = r1;
        goto L_0x045f;
    L_0x0445:
        r1 = r0.isChat;
        if (r1 == 0) goto L_0x0458;
    L_0x0449:
        r1 = r0.currentMessageObject;
        r1 = r1.needDrawAvatar();
        if (r1 == 0) goto L_0x0458;
    L_0x0451:
        r1 = 1116209152; // 0x42880000 float:68.0 double:5.514805956E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        goto L_0x0443;
    L_0x0458:
        r1 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        goto L_0x0443;
    L_0x045f:
        r1 = r0.titleLayout;
        if (r1 == 0) goto L_0x047a;
    L_0x0463:
        r24.save();
        r1 = (float) r6;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = r0.namesOffset;
        r2 = r2 + r3;
        r2 = (float) r2;
        r7.translate(r1, r2);
        r1 = r0.titleLayout;
        r1.draw(r7);
        r24.restore();
    L_0x047a:
        r1 = r0.docTitleLayout;
        if (r1 == 0) goto L_0x04a5;
    L_0x047e:
        r24.save();
        r1 = r0.docTitleOffsetX;
        r1 = r1 + r6;
        r1 = (float) r1;
        r2 = r0.titleLayout;
        if (r2 == 0) goto L_0x048e;
    L_0x0489:
        r2 = r2.getHeight();
        goto L_0x048f;
    L_0x048e:
        r2 = 0;
    L_0x048f:
        r3 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 + r3;
        r3 = r0.namesOffset;
        r2 = r2 + r3;
        r2 = (float) r2;
        r7.translate(r1, r2);
        r1 = r0.docTitleLayout;
        r1.draw(r7);
        r24.restore();
    L_0x04a5:
        r1 = android.os.Build.VERSION.SDK_INT;
        r2 = 21;
        if (r1 < r2) goto L_0x04b2;
    L_0x04ab:
        r1 = r0.selectorDrawable;
        if (r1 == 0) goto L_0x04b2;
    L_0x04af:
        r1.draw(r7);
    L_0x04b2:
        r1 = r0.pollButtons;
        r5 = r1.size();
        r4 = 0;
        r19 = 0;
    L_0x04bb:
        if (r4 >= r5) goto L_0x0772;
    L_0x04bd:
        r1 = r0.pollButtons;
        r1 = r1.get(r4);
        r3 = r1;
        r3 = (org.telegram.p004ui.Cells.ChatMessageCell.PollButton) r3;
        r3.f577x = r6;
        r24.save();
        r1 = 1107820544; // 0x42080000 float:34.0 double:5.473360725E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r1 = r1 + r6;
        r1 = (float) r1;
        r2 = r3.f578y;
        r13 = r0.namesOffset;
        r2 = r2 + r13;
        r2 = (float) r2;
        r7.translate(r1, r2);
        r1 = r3.title;
        r1.draw(r7);
        r1 = r0.animatePollAnswerAlpha;
        if (r1 == 0) goto L_0x0500;
    L_0x04ea:
        r1 = r0.pollUnvoteInProgress;
        if (r1 == 0) goto L_0x04f3;
    L_0x04ee:
        r1 = r0.pollAnimationProgress;
        r1 = r15 - r1;
        goto L_0x04f5;
    L_0x04f3:
        r1 = r0.pollAnimationProgress;
    L_0x04f5:
        r2 = 1050253722; // 0x3e99999a float:0.3 double:5.188942835E-315;
        r1 = r1 / r2;
        r1 = java.lang.Math.min(r1, r15);
        r1 = r1 * r14;
        goto L_0x0502;
    L_0x0500:
        r1 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
    L_0x0502:
        r13 = (int) r1;
        r1 = r0.pollVoted;
        if (r1 != 0) goto L_0x050f;
    L_0x0507:
        r1 = r0.pollClosed;
        if (r1 != 0) goto L_0x050f;
    L_0x050b:
        r1 = r0.animatePollAnswerAlpha;
        if (r1 == 0) goto L_0x05dd;
    L_0x050f:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r2 = r0.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x051c;
    L_0x0519:
        r2 = "chat_outAudioSeekbarFill";
        goto L_0x051e;
    L_0x051c:
        r2 = "chat_inAudioSeekbarFill";
    L_0x051e:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
        r1 = r0.animatePollAnswerAlpha;
        if (r1 == 0) goto L_0x054a;
    L_0x0529:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r1 = r1.getAlpha();
        r1 = (float) r1;
        r1 = r1 / r14;
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r15 = (float) r13;
        r1 = r1 * r15;
        r1 = (int) r1;
        r2.setAlpha(r1);
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r1 = r1.getAlpha();
        r1 = (float) r1;
        r1 = r1 / r14;
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r15 = r15 * r1;
        r1 = (int) r15;
        r2.setAlpha(r1);
    L_0x054a:
        r1 = r3.prevPercent;
        r1 = (float) r1;
        r2 = r3.percent;
        r15 = r3.prevPercent;
        r2 = r2 - r15;
        r2 = (float) r2;
        r15 = r0.pollAnimationProgress;
        r2 = r2 * r15;
        r1 = r1 + r2;
        r1 = (double) r1;
        r1 = java.lang.Math.ceil(r1);
        r1 = (int) r1;
        r2 = new java.lang.Object[r9];
        r1 = java.lang.Integer.valueOf(r1);
        r2[r10] = r1;
        r1 = "%d%%";
        r1 = java.lang.String.format(r1, r2);
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r2 = r2.measureText(r1);
        r9 = (double) r2;
        r9 = java.lang.Math.ceil(r9);
        r2 = (int) r9;
        r9 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r9 = -r9;
        r9 = r9 - r2;
        r2 = (float) r9;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r9 = (float) r9;
        r10 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r7.drawText(r1, r2, r9, r10);
        r1 = r0.backgroundWidth;
        r2 = 1117257728; // 0x42980000 float:76.0 double:5.51998661E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r1 = r1 - r2;
        r2 = r3.prevPercentProgress;
        r9 = r3.percentProgress;
        r10 = r3.prevPercentProgress;
        r9 = r9 - r10;
        r10 = r0.pollAnimationProgress;
        r9 = r9 * r10;
        r2 = r2 + r9;
        r9 = r0.instantButtonRect;
        r10 = r3.height;
        r20 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r10 = r10 + r20;
        r10 = (float) r10;
        r1 = (float) r1;
        r1 = r1 * r2;
        r2 = r3.height;
        r20 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r20 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r2 = r2 + r20;
        r2 = (float) r2;
        r9.set(r8, r10, r1, r2);
        r1 = r0.instantButtonRect;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r2 = (float) r2;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r9 = (float) r9;
        r10 = org.telegram.p004ui.ActionBar.Theme.chat_docBackPaint;
        r7.drawRoundRect(r1, r2, r9, r10);
    L_0x05dd:
        r1 = r0.pollVoted;
        if (r1 != 0) goto L_0x05e5;
    L_0x05e1:
        r1 = r0.pollClosed;
        if (r1 == 0) goto L_0x05e9;
    L_0x05e5:
        r1 = r0.animatePollAnswerAlpha;
        if (r1 == 0) goto L_0x074a;
    L_0x05e9:
        r1 = r23.isDrawSelectionBackground();
        if (r1 == 0) goto L_0x0606;
    L_0x05ef:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_replyLinePaint;
        r2 = r0.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x05fc;
    L_0x05f9:
        r2 = "chat_outVoiceSeekbarSelected";
        goto L_0x05fe;
    L_0x05fc:
        r2 = "chat_inVoiceSeekbarSelected";
    L_0x05fe:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
        goto L_0x061c;
    L_0x0606:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_replyLinePaint;
        r2 = r0.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x0613;
    L_0x0610:
        r2 = "chat_outVoiceSeekbar";
        goto L_0x0615;
    L_0x0613:
        r2 = "chat_inVoiceSeekbar";
    L_0x0615:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
    L_0x061c:
        r1 = r0.animatePollAnswerAlpha;
        if (r1 == 0) goto L_0x0633;
    L_0x0620:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_replyLinePaint;
        r1 = r1.getAlpha();
        r1 = (float) r1;
        r1 = r1 / r14;
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_replyLinePaint;
        r9 = 255 - r13;
        r9 = (float) r9;
        r9 = r9 * r1;
        r1 = (int) r9;
        r2.setAlpha(r1);
    L_0x0633:
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r1 = -r1;
        r2 = (float) r1;
        r1 = r3.height;
        r9 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r1 = r1 + r9;
        r9 = (float) r1;
        r1 = r0.backgroundWidth;
        r10 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r1 = r1 - r10;
        r10 = (float) r1;
        r1 = r3.height;
        r20 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r20 = org.telegram.messenger.AndroidUtilities.m26dp(r20);
        r1 = r1 + r20;
        r1 = (float) r1;
        r20 = org.telegram.p004ui.ActionBar.Theme.chat_replyLinePaint;
        r21 = r1;
        r1 = r24;
        r22 = r3;
        r3 = r9;
        r9 = r4;
        r4 = r10;
        r10 = r5;
        r5 = r21;
        r21 = r6;
        r6 = r20;
        r1.drawLine(r2, r3, r4, r5, r6);
        r1 = r0.pollVoteInProgress;
        r2 = 1091043328; // 0x41080000 float:8.5 double:5.390470265E-315;
        if (r1 == 0) goto L_0x06ea;
    L_0x0677:
        r1 = r0.pollVoteInProgressNum;
        if (r9 != r1) goto L_0x06ea;
    L_0x067b:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewRectPaint;
        r3 = r0.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x0688;
    L_0x0685:
        r3 = "chat_outAudioSeekbarFill";
        goto L_0x068a;
    L_0x0688:
        r3 = "chat_inAudioSeekbarFill";
    L_0x068a:
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r1.setColor(r3);
        r1 = r0.animatePollAnswerAlpha;
        if (r1 == 0) goto L_0x06a8;
    L_0x0695:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewRectPaint;
        r1 = r1.getAlpha();
        r1 = (float) r1;
        r1 = r1 / r14;
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewRectPaint;
        r4 = 255 - r13;
        r4 = (float) r4;
        r4 = r4 * r1;
        r1 = (int) r4;
        r3.setAlpha(r1);
    L_0x06a8:
        r1 = r0.instantButtonRect;
        r3 = 1102577664; // 0x41b80000 float:23.0 double:5.447457457E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = -r3;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = r3 - r4;
        r3 = (float) r3;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r4 = r4 - r5;
        r4 = (float) r4;
        r5 = 1102577664; // 0x41b80000 float:23.0 double:5.447457457E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r5 = -r5;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r5 = r5 + r6;
        r5 = (float) r5;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r6 = r6 + r2;
        r2 = (float) r6;
        r1.set(r3, r4, r5, r2);
        r2 = r0.instantButtonRect;
        r3 = r0.voteRadOffset;
        r4 = r0.voteCurrentCircleLength;
        r5 = 0;
        r6 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewRectPaint;
        r1 = r24;
        r1.drawArc(r2, r3, r4, r5, r6);
        goto L_0x0750;
    L_0x06ea:
        r1 = r0.currentMessageObject;
        r1 = r1.isOutOwner();
        if (r1 == 0) goto L_0x0707;
    L_0x06f2:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewRectPaint;
        r3 = r23.isDrawSelectionBackground();
        if (r3 == 0) goto L_0x06fd;
    L_0x06fa:
        r3 = "chat_outMenuSelected";
        goto L_0x06ff;
    L_0x06fd:
        r3 = "chat_outMenu";
    L_0x06ff:
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r1.setColor(r3);
        goto L_0x071b;
    L_0x0707:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewRectPaint;
        r3 = r23.isDrawSelectionBackground();
        if (r3 == 0) goto L_0x0712;
    L_0x070f:
        r3 = "chat_inMenuSelected";
        goto L_0x0714;
    L_0x0712:
        r3 = "chat_inMenu";
    L_0x0714:
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r1.setColor(r3);
    L_0x071b:
        r1 = r0.animatePollAnswerAlpha;
        if (r1 == 0) goto L_0x0732;
    L_0x071f:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewRectPaint;
        r1 = r1.getAlpha();
        r1 = (float) r1;
        r1 = r1 / r14;
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewRectPaint;
        r4 = 255 - r13;
        r4 = (float) r4;
        r4 = r4 * r1;
        r1 = (int) r4;
        r3.setAlpha(r1);
    L_0x0732:
        r1 = 1102577664; // 0x41b80000 float:23.0 double:5.447457457E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r1 = -r1;
        r1 = (float) r1;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r3 = (float) r3;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewRectPaint;
        r7.drawCircle(r1, r3, r2, r4);
        goto L_0x0750;
    L_0x074a:
        r22 = r3;
        r9 = r4;
        r10 = r5;
        r21 = r6;
    L_0x0750:
        r24.restore();
        r5 = r10 + -1;
        if (r9 != r5) goto L_0x0765;
    L_0x0757:
        r1 = r22.f578y;
        r2 = r0.namesOffset;
        r1 = r1 + r2;
        r2 = r22.height;
        r1 = r1 + r2;
        r19 = r1;
    L_0x0765:
        r4 = r9 + 1;
        r5 = r10;
        r6 = r21;
        r9 = 1;
        r10 = 0;
        r13 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r15 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x04bb;
    L_0x0772:
        r21 = r6;
        r1 = r0.infoLayout;
        if (r1 == 0) goto L_0x0794;
    L_0x0778:
        r24.save();
        r1 = r0.infoX;
        r6 = r21 + r1;
        r1 = (float) r6;
        r2 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = r19 + r2;
        r2 = (float) r2;
        r7.translate(r1, r2);
        r1 = r0.infoLayout;
        r1.draw(r7);
        r24.restore();
    L_0x0794:
        r23.updatePollAnimations();
        goto L_0x0bf3;
    L_0x0799:
        r2 = 12;
        if (r5 != r2) goto L_0x0bf3;
    L_0x079d:
        r1 = r1.isOutOwner();
        if (r1 == 0) goto L_0x07c3;
    L_0x07a3:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_contactNamePaint;
        r2 = "chat_outContactNameText";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_contactPhonePaint;
        r2 = r23.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x07b9;
    L_0x07b6:
        r2 = "chat_outContactPhoneSelectedText";
        goto L_0x07bb;
    L_0x07b9:
        r2 = "chat_outContactPhoneText";
    L_0x07bb:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
        goto L_0x07e2;
    L_0x07c3:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_contactNamePaint;
        r2 = "chat_inContactNameText";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_contactPhonePaint;
        r2 = r23.isDrawSelectionBackground();
        if (r2 == 0) goto L_0x07d9;
    L_0x07d6:
        r2 = "chat_inContactPhoneSelectedText";
        goto L_0x07db;
    L_0x07d9:
        r2 = "chat_inContactPhoneText";
    L_0x07db:
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r1.setColor(r2);
    L_0x07e2:
        r1 = r0.titleLayout;
        if (r1 == 0) goto L_0x0811;
    L_0x07e6:
        r24.save();
        r1 = r0.photoImage;
        r1 = r1.getImageX();
        r2 = r0.photoImage;
        r2 = r2.getImageWidth();
        r1 = r1 + r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r1 = r1 + r2;
        r1 = (float) r1;
        r2 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = r0.namesOffset;
        r2 = r2 + r3;
        r2 = (float) r2;
        r7.translate(r1, r2);
        r1 = r0.titleLayout;
        r1.draw(r7);
        r24.restore();
    L_0x0811:
        r1 = r0.docTitleLayout;
        if (r1 == 0) goto L_0x0840;
    L_0x0815:
        r24.save();
        r1 = r0.photoImage;
        r1 = r1.getImageX();
        r2 = r0.photoImage;
        r2 = r2.getImageWidth();
        r1 = r1 + r2;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r1 = r1 + r2;
        r1 = (float) r1;
        r2 = 1109131264; // 0x421c0000 float:39.0 double:5.479836543E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = r0.namesOffset;
        r2 = r2 + r3;
        r2 = (float) r2;
        r7.translate(r1, r2);
        r1 = r0.docTitleLayout;
        r1.draw(r7);
        r24.restore();
    L_0x0840:
        r1 = r0.currentMessageObject;
        r1 = r1.isOutOwner();
        if (r1 == 0) goto L_0x0854;
    L_0x0848:
        r1 = r23.isDrawSelectionBackground();
        if (r1 == 0) goto L_0x0851;
    L_0x084e:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_msgOutMenuSelectedDrawable;
        goto L_0x085f;
    L_0x0851:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_msgOutMenuDrawable;
        goto L_0x085f;
    L_0x0854:
        r1 = r23.isDrawSelectionBackground();
        if (r1 == 0) goto L_0x085d;
    L_0x085a:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_msgInMenuSelectedDrawable;
        goto L_0x085f;
    L_0x085d:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_msgInMenuDrawable;
    L_0x085f:
        r2 = r0.photoImage;
        r2 = r2.getImageX();
        r3 = r0.backgroundWidth;
        r2 = r2 + r3;
        r3 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r3;
        r0.otherX = r2;
        r3 = r0.photoImage;
        r3 = r3.getImageY();
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 - r4;
        r0.otherY = r3;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r1, r2, r3);
        r1.draw(r7);
        r1 = r0.drawInstantView;
        if (r1 == 0) goto L_0x0bf3;
    L_0x088a:
        r1 = r0.photoImage;
        r1 = r1.getImageX();
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r1 = r1 - r2;
        r2 = r23.getMeasuredHeight();
        r3 = 1115684864; // 0x42800000 float:64.0 double:5.51221563E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r3;
        r3 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewRectPaint;
        r4 = r0.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 == 0) goto L_0x08bf;
    L_0x08aa:
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r5 = "chat_outPreviewInstantText";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r4.setColor(r5);
        r4 = "chat_outPreviewInstantText";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r3.setColor(r4);
        goto L_0x08d3;
    L_0x08bf:
        r4 = org.telegram.p004ui.ActionBar.Theme.chat_instantViewPaint;
        r5 = "chat_inPreviewInstantText";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r4.setColor(r5);
        r4 = "chat_inPreviewInstantText";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r3.setColor(r4);
    L_0x08d3:
        r4 = android.os.Build.VERSION.SDK_INT;
        r5 = 21;
        if (r4 < r5) goto L_0x08ed;
    L_0x08d9:
        r4 = r0.selectorDrawable;
        r5 = r0.instantWidth;
        r5 = r5 + r1;
        r6 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r6 = r6 + r2;
        r4.setBounds(r1, r2, r5, r6);
        r4 = r0.selectorDrawable;
        r4.draw(r7);
    L_0x08ed:
        r4 = r0.instantButtonRect;
        r5 = (float) r1;
        r6 = (float) r2;
        r8 = r0.instantWidth;
        r8 = r8 + r1;
        r8 = (float) r8;
        r9 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r9 = r9 + r2;
        r9 = (float) r9;
        r4.set(r5, r6, r8, r9);
        r4 = r0.instantButtonRect;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r5 = (float) r5;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r6 = (float) r6;
        r7.drawRoundRect(r4, r5, r6, r3);
        r3 = r0.instantViewLayout;
        if (r3 == 0) goto L_0x0bf3;
    L_0x0913:
        r24.save();
        r3 = r0.instantTextX;
        r1 = r1 + r3;
        r1 = (float) r1;
        r3 = 1093140480; // 0x41280000 float:10.5 double:5.40083157E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 + r3;
        r2 = (float) r2;
        r7.translate(r1, r2);
        r1 = r0.instantViewLayout;
        r1.draw(r7);
        r24.restore();
        goto L_0x0bf3;
    L_0x092f:
        r1 = 0;
        r5 = r0.photoImage;
        r5 = r5.getVisible();
        if (r5 == 0) goto L_0x0bf3;
    L_0x0938:
        r5 = r0.currentMessageObject;
        r5 = r5.needDrawBluredPreview();
        if (r5 != 0) goto L_0x09a6;
    L_0x0940:
        r5 = r0.documentAttachType;
        if (r5 != r2) goto L_0x09a6;
    L_0x0944:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r2 = (android.graphics.drawable.BitmapDrawable) r2;
        r2 = r2.getPaint();
        r2 = r2.getAlpha();
        r5 = r0.drawPhotoCheckBox;
        if (r5 == 0) goto L_0x0968;
    L_0x0954:
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r6 = (float) r2;
        r9 = r0.controlsAlpha;
        r6 = r6 * r9;
        r9 = r0.checkBoxAnimationProgress;
        r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r9 = r10 - r9;
        r6 = r6 * r9;
        r6 = (int) r6;
        r5.setAlpha(r6);
        goto L_0x0973;
    L_0x0968:
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r6 = (float) r2;
        r9 = r0.controlsAlpha;
        r6 = r6 * r9;
        r6 = (int) r6;
        r5.setAlpha(r6);
    L_0x0973:
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r6 = r0.photoImage;
        r6 = r6.getImageX();
        r9 = r0.photoImage;
        r9 = r9.getImageWidth();
        r6 = r6 + r9;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r6 = r6 - r9;
        r0.otherX = r6;
        r9 = r0.photoImage;
        r9 = r9.getImageY();
        r10 = 1090623898; // 0x4101999a float:8.1 double:5.388398005E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r9 = r9 + r10;
        r0.otherY = r9;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r5, r6, r9);
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r5.draw(r7);
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r5.setAlpha(r2);
    L_0x09a6:
        r2 = org.telegram.messenger.MediaController.getInstance();
        r5 = r0.currentMessageObject;
        r2 = r2.isPlayingMessage(r5);
        r5 = r0.animatingNoSoundPlaying;
        if (r5 == r2) goto L_0x09c5;
    L_0x09b4:
        r0.animatingNoSoundPlaying = r2;
        if (r2 == 0) goto L_0x09ba;
    L_0x09b8:
        r5 = 1;
        goto L_0x09bb;
    L_0x09ba:
        r5 = 2;
    L_0x09bb:
        r0.animatingNoSound = r5;
        if (r2 == 0) goto L_0x09c2;
    L_0x09bf:
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x09c3;
    L_0x09c2:
        r5 = 0;
    L_0x09c3:
        r0.animatingNoSoundProgress = r5;
    L_0x09c5:
        r5 = r0.buttonState;
        r6 = 1;
        if (r5 == r6) goto L_0x09dc;
    L_0x09ca:
        if (r5 == r8) goto L_0x09dc;
    L_0x09cc:
        if (r5 == 0) goto L_0x09dc;
    L_0x09ce:
        r6 = 3;
        if (r5 == r6) goto L_0x09dc;
    L_0x09d1:
        r6 = -1;
        if (r5 == r6) goto L_0x09dc;
    L_0x09d4:
        r5 = r0.currentMessageObject;
        r5 = r5.needDrawBluredPreview();
        if (r5 == 0) goto L_0x0b83;
    L_0x09dc:
        r5 = r0.autoPlayingMedia;
        if (r5 == 0) goto L_0x09e3;
    L_0x09e0:
        r23.updatePlayingMessageProgress();
    L_0x09e3:
        r5 = r0.infoLayout;
        if (r5 == 0) goto L_0x0b83;
    L_0x09e7:
        r5 = r0.forceNotDrawTime;
        if (r5 == 0) goto L_0x09f3;
    L_0x09eb:
        r5 = r0.autoPlayingMedia;
        if (r5 != 0) goto L_0x09f3;
    L_0x09ef:
        r5 = r0.drawVideoImageButton;
        if (r5 == 0) goto L_0x0b83;
    L_0x09f3:
        r5 = r0.currentMessageObject;
        r5 = r5.needDrawBluredPreview();
        if (r5 == 0) goto L_0x0a01;
    L_0x09fb:
        r5 = r0.docTitleLayout;
        if (r5 != 0) goto L_0x0a01;
    L_0x09ff:
        r6 = 0;
        goto L_0x0a03;
    L_0x0a01:
        r6 = r0.animatingDrawVideoImageButtonProgress;
    L_0x0a03:
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_infoPaint;
        r9 = "chat_mediaInfoText";
        r9 = org.telegram.p004ui.ActionBar.Theme.getColor(r9);
        r5.setColor(r9);
        r5 = r0.photoImage;
        r5 = r5.getImageX();
        r9 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r5 = r5 + r10;
        r10 = r0.photoImage;
        r10 = r10.getImageY();
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r10 = r10 + r13;
        r9 = r0.autoPlayingMedia;
        if (r9 == 0) goto L_0x0a44;
    L_0x0a2a:
        if (r2 == 0) goto L_0x0a30;
    L_0x0a2c:
        r2 = r0.animatingNoSound;
        if (r2 == 0) goto L_0x0a44;
    L_0x0a30:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_msgNoSoundDrawable;
        r2 = r2.getIntrinsicWidth();
        r9 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r2 = r2 + r13;
        r2 = (float) r2;
        r9 = r0.animatingNoSoundProgress;
        r2 = r2 * r9;
        r2 = (int) r2;
        goto L_0x0a45;
    L_0x0a44:
        r2 = 0;
    L_0x0a45:
        r9 = r0.infoWidth;
        r13 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r9 = r9 + r13;
        r9 = r9 + r2;
        r9 = (float) r9;
        r13 = r0.infoWidth;
        r13 = r13 + r2;
        r15 = r0.docTitleWidth;
        r13 = java.lang.Math.max(r13, r15);
        r15 = r0.canStreamVideo;
        if (r15 == 0) goto L_0x0a64;
    L_0x0a5d:
        r15 = 1107296256; // 0x42000000 float:32.0 double:5.4707704E-315;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        goto L_0x0a65;
    L_0x0a64:
        r15 = 0;
    L_0x0a65:
        r13 = r13 + r15;
        r15 = r0.infoWidth;
        r13 = r13 - r15;
        r13 = r13 - r2;
        r13 = (float) r13;
        r13 = r13 * r6;
        r9 = r9 + r13;
        r12 = (double) r9;
        r12 = java.lang.Math.ceil(r12);
        r9 = (int) r12;
        r12 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1));
        if (r12 == 0) goto L_0x0a7d;
    L_0x0a78:
        r12 = r0.docTitleLayout;
        if (r12 != 0) goto L_0x0a7d;
    L_0x0a7c:
        r6 = 0;
    L_0x0a7d:
        r12 = r0.rect;
        r13 = (float) r5;
        r15 = (float) r10;
        r5 = r5 + r9;
        r5 = (float) r5;
        r9 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        r18 = 1098383360; // 0x41780000 float:15.5 double:5.42673484E-315;
        r18 = r18 * r6;
        r18 = r18 + r9;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r10 = r10 + r9;
        r9 = (float) r10;
        r12.set(r13, r15, r5, r9);
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r5 = r5.getAlpha();
        r9 = org.telegram.p004ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r10 = (float) r5;
        r12 = r0.controlsAlpha;
        r10 = r10 * r12;
        r10 = (int) r10;
        r9.setAlpha(r10);
        r9 = r0.rect;
        r10 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r12 = (float) r12;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r10 = (float) r13;
        r13 = org.telegram.p004ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r7.drawRoundRect(r9, r12, r10, r13);
        r9 = org.telegram.p004ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r9.setAlpha(r5);
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_infoPaint;
        r9 = r0.controlsAlpha;
        r9 = r9 * r14;
        r9 = (int) r9;
        r5.setAlpha(r9);
        r24.save();
        r5 = r0.photoImage;
        r5 = r5.getImageX();
        r9 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r10 = r0.canStreamVideo;
        if (r10 == 0) goto L_0x0adb;
    L_0x0ad6:
        r10 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r10 = r10 * r6;
        goto L_0x0adc;
    L_0x0adb:
        r10 = 0;
    L_0x0adc:
        r10 = r10 + r9;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r5 = r5 + r9;
        r0.noSoundCenterX = r5;
        r5 = (float) r5;
        r9 = r0.photoImage;
        r9 = r9.getImageY();
        r10 = 1085276160; // 0x40b00000 float:5.5 double:5.36197667E-315;
        r12 = 1045220557; // 0x3e4ccccd float:0.2 double:5.164075695E-315;
        r12 = r12 * r6;
        r12 = r12 + r10;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r9 = r9 + r10;
        r9 = (float) r9;
        r7.translate(r5, r9);
        r5 = r0.infoLayout;
        if (r5 == 0) goto L_0x0b03;
    L_0x0b00:
        r5.draw(r7);
    L_0x0b03:
        r5 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1));
        if (r5 <= 0) goto L_0x0b2f;
    L_0x0b07:
        r5 = r0.docTitleLayout;
        if (r5 == 0) goto L_0x0b2f;
    L_0x0b0b:
        r24.save();
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_infoPaint;
        r9 = r0.controlsAlpha;
        r9 = r9 * r14;
        r9 = r9 * r6;
        r9 = (int) r9;
        r5.setAlpha(r9);
        r5 = 1097125069; // 0x4164cccd float:14.3 double:5.42051806E-315;
        r6 = r6 * r5;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = (float) r5;
        r7.translate(r1, r5);
        r5 = r0.docTitleLayout;
        r5.draw(r7);
        r24.restore();
    L_0x0b2f:
        if (r2 == 0) goto L_0x0b79;
    L_0x0b31:
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_msgNoSoundDrawable;
        r5 = r0.animatingNoSoundProgress;
        r14 = r14 * r5;
        r14 = r14 * r5;
        r5 = r0.controlsAlpha;
        r14 = r14 * r5;
        r5 = (int) r14;
        r2.setAlpha(r5);
        r2 = r0.infoWidth;
        r5 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r2 = r2 + r6;
        r2 = (float) r2;
        r7.translate(r2, r1);
        r2 = r0.animatingNoSoundProgress;
        r2 = r2 * r11;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r5 = r5 - r2;
        r5 = r5 / r8;
        r6 = org.telegram.p004ui.ActionBar.Theme.chat_msgNoSoundDrawable;
        r9 = r5 + r2;
        r10 = 0;
        r6.setBounds(r10, r5, r2, r9);
        r5 = org.telegram.p004ui.ActionBar.Theme.chat_msgNoSoundDrawable;
        r5.draw(r7);
        r5 = r0.noSoundCenterX;
        r6 = r0.infoWidth;
        r9 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r6 = r6 + r10;
        r2 = r2 / r8;
        r6 = r6 + r2;
        r5 = r5 + r6;
        r0.noSoundCenterX = r5;
    L_0x0b79:
        r24.restore();
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_infoPaint;
        r5 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r2.setAlpha(r5);
    L_0x0b83:
        r2 = r0.animatingDrawVideoImageButton;
        r5 = 1;
        if (r2 != r5) goto L_0x0ba0;
    L_0x0b88:
        r2 = r0.animatingDrawVideoImageButtonProgress;
        r5 = (float) r3;
        r6 = 1126170624; // 0x43200000 float:160.0 double:5.564022167E-315;
        r5 = r5 / r6;
        r2 = r2 - r5;
        r0.animatingDrawVideoImageButtonProgress = r2;
        r2 = r0.animatingDrawVideoImageButtonProgress;
        r2 = (r2 > r1 ? 1 : (r2 == r1 ? 0 : -1));
        if (r2 > 0) goto L_0x0b9c;
    L_0x0b97:
        r0.animatingDrawVideoImageButtonProgress = r1;
        r2 = 0;
        r0.animatingDrawVideoImageButton = r2;
    L_0x0b9c:
        r23.invalidate();
        goto L_0x0bbb;
    L_0x0ba0:
        if (r2 != r8) goto L_0x0bbb;
    L_0x0ba2:
        r2 = r0.animatingDrawVideoImageButtonProgress;
        r5 = (float) r3;
        r6 = 1126170624; // 0x43200000 float:160.0 double:5.564022167E-315;
        r5 = r5 / r6;
        r2 = r2 + r5;
        r0.animatingDrawVideoImageButtonProgress = r2;
        r2 = r0.animatingDrawVideoImageButtonProgress;
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1));
        if (r2 < 0) goto L_0x0bb8;
    L_0x0bb3:
        r0.animatingDrawVideoImageButtonProgress = r5;
        r2 = 0;
        r0.animatingDrawVideoImageButton = r2;
    L_0x0bb8:
        r23.invalidate();
    L_0x0bbb:
        r2 = r0.animatingNoSound;
        r5 = 1;
        if (r2 != r5) goto L_0x0bd8;
    L_0x0bc0:
        r2 = r0.animatingNoSoundProgress;
        r3 = (float) r3;
        r4 = 1127481344; // 0x43340000 float:180.0 double:5.570497984E-315;
        r3 = r3 / r4;
        r2 = r2 - r3;
        r0.animatingNoSoundProgress = r2;
        r2 = r0.animatingNoSoundProgress;
        r2 = (r2 > r1 ? 1 : (r2 == r1 ? 0 : -1));
        if (r2 > 0) goto L_0x0bd4;
    L_0x0bcf:
        r0.animatingNoSoundProgress = r1;
        r1 = 0;
        r0.animatingNoSound = r1;
    L_0x0bd4:
        r23.invalidate();
        goto L_0x0bf3;
    L_0x0bd8:
        if (r2 != r8) goto L_0x0bf3;
    L_0x0bda:
        r1 = r0.animatingNoSoundProgress;
        r2 = (float) r3;
        r3 = 1127481344; // 0x43340000 float:180.0 double:5.570497984E-315;
        r2 = r2 / r3;
        r1 = r1 + r2;
        r0.animatingNoSoundProgress = r1;
        r1 = r0.animatingNoSoundProgress;
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r1 < 0) goto L_0x0bf0;
    L_0x0beb:
        r0.animatingNoSoundProgress = r2;
        r1 = 0;
        r0.animatingNoSound = r1;
    L_0x0bf0:
        r23.invalidate();
    L_0x0bf3:
        r1 = r0.drawImageButton;
        if (r1 == 0) goto L_0x0c11;
    L_0x0bf7:
        r1 = r0.photoImage;
        r1 = r1.getVisible();
        if (r1 == 0) goto L_0x0c11;
    L_0x0bff:
        r1 = r0.controlsAlpha;
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r3 == 0) goto L_0x0c0c;
    L_0x0c07:
        r2 = r0.radialProgress;
        r2.setOverrideAlpha(r1);
    L_0x0c0c:
        r1 = r0.radialProgress;
        r1.draw(r7);
    L_0x0c11:
        r1 = r0.drawVideoImageButton;
        if (r1 != 0) goto L_0x0c19;
    L_0x0c15:
        r1 = r0.animatingDrawVideoImageButton;
        if (r1 == 0) goto L_0x0c33;
    L_0x0c19:
        r1 = r0.photoImage;
        r1 = r1.getVisible();
        if (r1 == 0) goto L_0x0c33;
    L_0x0c21:
        r1 = r0.controlsAlpha;
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x0c2e;
    L_0x0c29:
        r2 = r0.videoRadialProgress;
        r2.setOverrideAlpha(r1);
    L_0x0c2e:
        r1 = r0.videoRadialProgress;
        r1.draw(r7);
    L_0x0c33:
        r1 = r0.drawPhotoCheckBox;
        if (r1 == 0) goto L_0x0c75;
    L_0x0c37:
        r1 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r2 = r0.photoCheckBox;
        r3 = 0;
        r4 = 0;
        r5 = r0.currentMessageObject;
        r5 = r5.isOutOwner();
        if (r5 == 0) goto L_0x0c4c;
    L_0x0c49:
        r5 = "chat_outBubbleSelected";
        goto L_0x0c4e;
    L_0x0c4c:
        r5 = "chat_inBubbleSelected";
    L_0x0c4e:
        r2.setColor(r3, r4, r5);
        r2 = r0.photoCheckBox;
        r3 = r0.photoImage;
        r3 = r3.getImageX2();
        r4 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 - r4;
        r4 = r0.photoImage;
        r4 = r4.getImageY();
        r5 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r4 = r4 + r5;
        r2.setBounds(r3, r4, r1, r1);
        r1 = r0.photoCheckBox;
        r1.draw(r7);
    L_0x0c75:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.ChatMessageCell.drawOverlays(android.graphics.Canvas):void");
    }

    public int getObserverTag() {
        return this.TAG;
    }

    public MessageObject getMessageObject() {
        MessageObject messageObject = this.messageObjectToSet;
        return messageObject != null ? messageObject : this.currentMessageObject;
    }

    public Document getStreamingMedia() {
        int i = this.documentAttachType;
        return (i == 4 || i == 7 || i == 2) ? this.documentAttach : null;
    }

    public boolean isPinnedBottom() {
        return this.pinnedBottom;
    }

    public boolean isPinnedTop() {
        return this.pinnedTop;
    }

    public GroupedMessages getCurrentMessagesGroup() {
        return this.currentMessagesGroup;
    }

    public GroupedMessagePosition getCurrentPosition() {
        return this.currentPosition;
    }

    public int getLayoutHeight() {
        return this.layoutHeight;
    }

    public boolean performAccessibilityAction(int i, Bundle bundle) {
        if (i == 16) {
            if (getIconForCurrentState() != 4) {
                didPressButton(true, false);
            } else if (this.currentMessageObject.type == 16) {
                this.delegate.didPressOther(this, (float) this.otherX, (float) this.otherY);
            } else {
                didClickedImage();
            }
            return true;
        }
        if (i == C1067R.C1066id.acc_action_small_button) {
            didPressMiniButton(true);
        } else if (i == C1067R.C1066id.acc_action_msg_options) {
            ChatMessageCellDelegate chatMessageCellDelegate = this.delegate;
            if (chatMessageCellDelegate != null) {
                if (this.currentMessageObject.type == 16) {
                    chatMessageCellDelegate.didLongPress(this, 0.0f, 0.0f);
                } else {
                    chatMessageCellDelegate.didPressOther(this, (float) this.otherX, (float) this.otherY);
                }
            }
        }
        return super.performAccessibilityAction(i, bundle);
    }

    public boolean onHoverEvent(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int i = 0;
        if (motionEvent.getAction() == 9 || motionEvent.getAction() == 7) {
            while (i < this.accessibilityVirtualViewBounds.size()) {
                if (((Rect) this.accessibilityVirtualViewBounds.valueAt(i)).contains(x, y)) {
                    int keyAt = this.accessibilityVirtualViewBounds.keyAt(i);
                    if (keyAt != this.currentFocusedVirtualView) {
                        this.currentFocusedVirtualView = keyAt;
                        sendAccessibilityEventForVirtualView(keyAt, 32768);
                    }
                    return true;
                }
                i++;
            }
        } else if (motionEvent.getAction() == 10) {
            this.currentFocusedVirtualView = 0;
        }
        return super.onHoverEvent(motionEvent);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
    }

    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        return new MessageAccessibilityNodeProvider(this, null);
    }

    private void sendAccessibilityEventForVirtualView(int i, int i2) {
        if (((AccessibilityManager) getContext().getSystemService("accessibility")).isTouchExplorationEnabled()) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain(i2);
            obtain.setPackageName(getContext().getPackageName());
            obtain.setSource(this, i);
            getParent().requestSendAccessibilityEvent(this, obtain);
        }
    }
}