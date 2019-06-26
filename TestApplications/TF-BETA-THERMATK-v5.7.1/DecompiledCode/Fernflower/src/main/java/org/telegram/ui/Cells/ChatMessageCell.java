package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.StaticLayout.Builder;
import android.text.TextUtils.TruncateAt;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.SparseArray;
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
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
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.WebFile;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.SecretMediaViewer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.CheckBoxBase;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.Components.RoundVideoPlayingDrawable;
import org.telegram.ui.Components.SeekBar;
import org.telegram.ui.Components.SeekBarWaveform;
import org.telegram.ui.Components.StaticLayoutEx;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanBotCommand;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;

public class ChatMessageCell extends BaseCell implements SeekBar.SeekBarDelegate, ImageReceiver.ImageReceiverDelegate, DownloadController.FileDownloadProgressListener {
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
   private SparseArray accessibilityVirtualViewBounds;
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
   private ImageReceiver avatarImage;
   private boolean avatarPressed;
   private int backgroundDrawableLeft;
   private int backgroundDrawableRight;
   private int backgroundWidth;
   private ArrayList botButtons = new ArrayList();
   private HashMap botButtonsByData = new HashMap();
   private HashMap botButtonsByPosition = new HashMap();
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
   private float controlsAlpha = 1.0F;
   private int currentAccount;
   private Drawable currentBackgroundDrawable;
   private CharSequence currentCaption;
   private TLRPC.Chat currentChat;
   private int currentFocusedVirtualView;
   private TLRPC.Chat currentForwardChannel;
   private String currentForwardName;
   private String currentForwardNameString;
   private TLRPC.User currentForwardUser;
   private int currentMapProvider;
   private MessageObject currentMessageObject;
   private MessageObject.GroupedMessages currentMessagesGroup;
   private String currentNameString;
   private TLRPC.FileLocation currentPhoto;
   private String currentPhotoFilter;
   private String currentPhotoFilterThumb;
   private TLRPC.PhotoSize currentPhotoObject;
   private TLRPC.PhotoSize currentPhotoObjectThumb;
   private MessageObject.GroupedMessagePosition currentPosition;
   private TLRPC.PhotoSize currentReplyPhoto;
   private String currentTimeString;
   private String currentUrl;
   private TLRPC.User currentUser;
   private TLRPC.User currentViaBotUser;
   private String currentViewsString;
   private WebFile currentWebFile;
   private ChatMessageCell.ChatMessageCellDelegate delegate;
   private RectF deleteProgressRect = new RectF();
   private StaticLayout descriptionLayout;
   private int descriptionX;
   private int descriptionY;
   private boolean disallowLongPress;
   private StaticLayout docTitleLayout;
   private int docTitleOffsetX;
   private int docTitleWidth;
   private TLRPC.Document documentAttach;
   private int documentAttachType;
   private boolean drawBackground;
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
   private boolean drawTime;
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
   private float[] forwardNameOffsetX;
   private boolean forwardNamePressed;
   private int forwardNameX;
   private int forwardNameY;
   private StaticLayout[] forwardedNameLayout;
   private int forwardedNameWidth;
   private boolean fullyDraw;
   private boolean gamePreviewPressed;
   private boolean groupPhotoInvisible;
   private MessageObject.GroupedMessages groupedMessagesToSet;
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
   private Runnable invalidateRunnable;
   private boolean invalidatesParent;
   private boolean isAvatarVisible;
   public boolean isChat;
   private boolean isCheckPressed;
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
   private TLRPC.TL_poll lastPoll;
   private ArrayList lastPollResults;
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
   private ArrayList pollButtons = new ArrayList();
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
   private float timeAlpha = 1.0F;
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
   private ArrayList urlPath = new ArrayList();
   private ArrayList urlPathCache = new ArrayList();
   private ArrayList urlPathSelection = new ArrayList();
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

   public ChatMessageCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.isCheckPressed = true;
      this.drawBackground = true;
      this.backgroundWidth = 100;
      this.forwardedNameLayout = new StaticLayout[2];
      this.forwardNameOffsetX = new float[2];
      this.drawTime = true;
      this.invalidateRunnable = new Runnable() {
         public void run() {
            ChatMessageCell.this.checkLocationExpired();
            if (ChatMessageCell.this.locationExpired) {
               ChatMessageCell.this.invalidate();
               ChatMessageCell.this.scheduledInvalidate = false;
            } else {
               ChatMessageCell var1 = ChatMessageCell.this;
               var1.invalidate((int)var1.rect.left - 5, (int)ChatMessageCell.this.rect.top - 5, (int)ChatMessageCell.this.rect.right + 5, (int)ChatMessageCell.this.rect.bottom + 5);
               if (ChatMessageCell.this.scheduledInvalidate) {
                  AndroidUtilities.runOnUIThread(ChatMessageCell.this.invalidateRunnable, 1000L);
               }
            }

         }
      };
      this.accessibilityVirtualViewBounds = new SparseArray();
      this.currentFocusedVirtualView = -1;
      this.avatarImage = new ImageReceiver();
      this.avatarImage.setRoundRadius(AndroidUtilities.dp(21.0F));
      this.avatarDrawable = new AvatarDrawable();
      this.replyImageReceiver = new ImageReceiver(this);
      this.locationImageReceiver = new ImageReceiver(this);
      this.locationImageReceiver.setRoundRadius(AndroidUtilities.dp(26.1F));
      this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
      this.contactAvatarDrawable = new AvatarDrawable();
      this.photoImage = new ImageReceiver(this);
      this.photoImage.setDelegate(this);
      this.radialProgress = new RadialProgress2(this);
      this.videoRadialProgress = new RadialProgress2(this);
      this.videoRadialProgress.setDrawBackground(false);
      this.videoRadialProgress.setCircleRadius(AndroidUtilities.dp(15.0F));
      this.seekBar = new SeekBar(var1);
      this.seekBar.setDelegate(this);
      this.seekBarWaveform = new SeekBarWaveform(var1);
      this.seekBarWaveform.setDelegate(this);
      this.seekBarWaveform.setParentView(this);
      this.roundVideoPlayingDrawable = new RoundVideoPlayingDrawable(this);
   }

   private void calcBackgroundWidth(int var1, int var2, int var3) {
      if (!this.hasLinkPreview && !this.hasOldCaptionPreview && !this.hasGamePreview && !this.hasInvoicePreview) {
         MessageObject var4 = this.currentMessageObject;
         int var5 = var4.lastLineWidth;
         if (var1 - var5 >= var2 && !var4.hasRtl) {
            var1 = var3 - var5;
            if (var1 >= 0 && var1 <= var2) {
               this.backgroundWidth = var3 + var2 - var1 + AndroidUtilities.dp(31.0F);
            } else {
               this.backgroundWidth = Math.max(var3, this.currentMessageObject.lastLineWidth + var2) + AndroidUtilities.dp(31.0F);
            }

            return;
         }
      }

      this.totalHeight += AndroidUtilities.dp(14.0F);
      this.hasNewLineForTime = true;
      this.backgroundWidth = Math.max(var3, this.currentMessageObject.lastLineWidth) + AndroidUtilities.dp(31.0F);
      var2 = this.backgroundWidth;
      if (this.currentMessageObject.isOutOwner()) {
         var1 = this.timeWidth + AndroidUtilities.dp(17.0F);
      } else {
         var1 = this.timeWidth;
      }

      this.backgroundWidth = Math.max(var2, var1 + AndroidUtilities.dp(31.0F));
   }

   private boolean checkAudioMotionEvent(MotionEvent var1) {
      int var2 = this.documentAttachType;
      if (var2 != 3 && var2 != 5) {
         return false;
      } else {
         int var3 = (int)var1.getX();
         int var4 = (int)var1.getY();
         boolean var5;
         if (this.useSeekBarWaweform) {
            var5 = this.seekBarWaveform.onTouch(var1.getAction(), var1.getX() - (float)this.seekBarX - (float)AndroidUtilities.dp(13.0F), var1.getY() - (float)this.seekBarY);
         } else {
            var5 = this.seekBar.onTouch(var1.getAction(), var1.getX() - (float)this.seekBarX, var1.getY() - (float)this.seekBarY);
         }

         boolean var6;
         if (var5) {
            if (!this.useSeekBarWaweform && var1.getAction() == 0) {
               this.getParent().requestDisallowInterceptTouchEvent(true);
            } else if (this.useSeekBarWaweform && !this.seekBarWaveform.isStartDraging() && var1.getAction() == 1) {
               this.didPressButton(true, false);
            }

            this.disallowLongPress = true;
            this.invalidate();
            var6 = var5;
         } else {
            int var7;
            int var8;
            boolean var9;
            label124: {
               var7 = AndroidUtilities.dp(36.0F);
               if (this.miniButtonState >= 0) {
                  var2 = AndroidUtilities.dp(27.0F);
                  var8 = this.buttonX;
                  if (var3 >= var8 + var2 && var3 <= var8 + var2 + var7) {
                     var8 = this.buttonY;
                     if (var4 >= var8 + var2 && var4 <= var8 + var2 + var7) {
                        var9 = true;
                        break label124;
                     }
                  }
               }

               var9 = false;
            }

            boolean var10;
            label116: {
               label141: {
                  if (!var9) {
                     var8 = this.buttonState;
                     if (var8 != 0 && var8 != 1 && var8 != 2) {
                        var8 = this.buttonX;
                        if (var3 >= var8 && var3 <= var8 + var7) {
                           var3 = this.buttonY;
                           if (var4 >= var3 && var4 <= var3 + var7) {
                              break label141;
                           }
                        }
                     } else if (var3 >= this.buttonX - AndroidUtilities.dp(12.0F) && var3 <= this.buttonX - AndroidUtilities.dp(12.0F) + this.backgroundWidth) {
                        if (this.drawInstantView) {
                           var3 = this.buttonY;
                        } else {
                           var3 = this.namesOffset + this.mediaOffsetY;
                        }

                        if (var4 >= var3) {
                           if (this.drawInstantView) {
                              var8 = this.buttonY;
                              var3 = var7;
                           } else {
                              var8 = this.namesOffset + this.mediaOffsetY;
                              var3 = AndroidUtilities.dp(82.0F);
                           }

                           if (var4 <= var8 + var3) {
                              break label141;
                           }
                        }
                     }
                  }

                  var10 = false;
                  break label116;
               }

               var10 = true;
            }

            if (var1.getAction() == 0) {
               if (!var10) {
                  var6 = var5;
                  if (!var9) {
                     return var6;
                  }
               }

               if (var10) {
                  this.buttonPressed = 1;
               } else {
                  this.miniButtonPressed = 1;
               }

               this.invalidate();
               var6 = true;
            } else if (this.buttonPressed != 0) {
               if (var1.getAction() == 1) {
                  this.buttonPressed = 0;
                  this.playSoundEffect(0);
                  this.didPressButton(true, false);
                  this.invalidate();
                  var6 = var5;
               } else if (var1.getAction() == 3) {
                  this.buttonPressed = 0;
                  this.invalidate();
                  var6 = var5;
               } else {
                  var6 = var5;
                  if (var1.getAction() == 2) {
                     var6 = var5;
                     if (!var10) {
                        this.buttonPressed = 0;
                        this.invalidate();
                        var6 = var5;
                     }
                  }
               }
            } else {
               var6 = var5;
               if (this.miniButtonPressed != 0) {
                  if (var1.getAction() == 1) {
                     this.miniButtonPressed = 0;
                     this.playSoundEffect(0);
                     this.didPressMiniButton(true);
                     this.invalidate();
                     var6 = var5;
                  } else if (var1.getAction() == 3) {
                     this.miniButtonPressed = 0;
                     this.invalidate();
                     var6 = var5;
                  } else {
                     var6 = var5;
                     if (var1.getAction() == 2) {
                        var6 = var5;
                        if (!var9) {
                           this.miniButtonPressed = 0;
                           this.invalidate();
                           var6 = var5;
                        }
                     }
                  }
               }
            }
         }

         return var6;
      }
   }

   private boolean checkBotButtonMotionEvent(MotionEvent var1) {
      boolean var2 = this.botButtons.isEmpty();
      boolean var3 = false;
      boolean var4 = var3;
      if (!var2) {
         if (this.currentMessageObject.eventId != 0L) {
            var4 = var3;
         } else {
            int var5 = (int)var1.getX();
            int var6 = (int)var1.getY();
            if (var1.getAction() == 0) {
               int var7;
               if (this.currentMessageObject.isOutOwner()) {
                  var7 = this.getMeasuredWidth() - this.widthForButtons - AndroidUtilities.dp(10.0F);
               } else {
                  var7 = this.backgroundDrawableLeft;
                  float var8;
                  if (this.mediaBackground) {
                     var8 = 1.0F;
                  } else {
                     var8 = 7.0F;
                  }

                  var7 += AndroidUtilities.dp(var8);
               }

               int var9 = 0;

               while(true) {
                  var4 = var3;
                  if (var9 >= this.botButtons.size()) {
                     break;
                  }

                  ChatMessageCell.BotButton var11 = (ChatMessageCell.BotButton)this.botButtons.get(var9);
                  int var10 = var11.y + this.layoutHeight - AndroidUtilities.dp(2.0F);
                  if (var5 >= var11.x + var7 && var5 <= var11.x + var7 + var11.width && var6 >= var10 && var6 <= var10 + var11.height) {
                     this.pressedBotButton = var9;
                     this.invalidate();
                     var4 = true;
                     break;
                  }

                  ++var9;
               }
            } else {
               var4 = var3;
               if (var1.getAction() == 1) {
                  var4 = var3;
                  if (this.pressedBotButton != -1) {
                     this.playSoundEffect(0);
                     this.delegate.didPressBotButton(this, ((ChatMessageCell.BotButton)this.botButtons.get(this.pressedBotButton)).button);
                     this.pressedBotButton = -1;
                     this.invalidate();
                     var4 = var3;
                  }
               }
            }
         }
      }

      return var4;
   }

   private boolean checkCaptionMotionEvent(MotionEvent var1) {
      if (this.currentCaption instanceof Spannable && this.captionLayout != null && (var1.getAction() == 0 || (this.linkPreviewPressed || this.pressedLink != null) && var1.getAction() == 1)) {
         int var2 = (int)var1.getX();
         int var3 = (int)var1.getY();
         int var4 = this.captionX;
         if (var2 >= var4 && var2 <= var4 + this.captionWidth) {
            var4 = this.captionY;
            if (var3 >= var4 && var3 <= var4 + this.captionHeight) {
               if (var1.getAction() == 0) {
                  Exception var10000;
                  Exception var24;
                  label149: {
                     boolean var10001;
                     StaticLayout var21;
                     try {
                        var4 = this.captionX;
                        int var5 = this.captionY;
                        var3 = this.captionLayout.getLineForVertical(var3 - var5);
                        var21 = this.captionLayout;
                     } catch (Exception var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label149;
                     }

                     float var6 = (float)(var2 - var4);

                     float var7;
                     try {
                        var2 = var21.getOffsetForHorizontal(var3, var6);
                        var7 = this.captionLayout.getLineLeft(var3);
                     } catch (Exception var19) {
                        var10000 = var19;
                        var10001 = false;
                        break label149;
                     }

                     if (var7 > var6) {
                        return false;
                     }

                     Spannable var8;
                     CharacterStyle[] var9;
                     try {
                        if (var7 + this.captionLayout.getLineWidth(var3) < var6) {
                           return false;
                        }

                        var8 = (Spannable)this.currentCaption;
                        var9 = (CharacterStyle[])var8.getSpans(var2, var2, ClickableSpan.class);
                     } catch (Exception var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label149;
                     }

                     CharacterStyle[] var22;
                     label117: {
                        if (var9 != null) {
                           var22 = var9;

                           try {
                              if (var9.length != 0) {
                                 break label117;
                              }
                           } catch (Exception var17) {
                              var10000 = var17;
                              var10001 = false;
                              break label149;
                           }
                        }

                        try {
                           var22 = (CharacterStyle[])var8.getSpans(var2, var2, URLSpanMono.class);
                        } catch (Exception var16) {
                           var10000 = var16;
                           var10001 = false;
                           break label149;
                        }
                     }

                     boolean var23;
                     label108: {
                        label107: {
                           try {
                              if (var22.length != 0 && (var22.length == 0 || !(var22[0] instanceof URLSpanBotCommand) || URLSpanBotCommand.enabled)) {
                                 break label107;
                              }
                           } catch (Exception var15) {
                              var10000 = var15;
                              var10001 = false;
                              break label149;
                           }

                           var23 = true;
                           break label108;
                        }

                        var23 = false;
                     }

                     if (var23) {
                        return false;
                     }

                     try {
                        this.pressedLink = var22[0];
                        this.pressedLinkType = 3;
                        this.resetUrlPaths(false);
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label149;
                     }

                     try {
                        LinkPath var25 = this.obtainNewUrlPath(false);
                        var2 = var8.getSpanStart(this.pressedLink);
                        var25.setCurrentLayout(this.captionLayout, var2, 0.0F);
                        this.captionLayout.getSelectionPath(var2, var8.getSpanEnd(this.pressedLink), var25);
                     } catch (Exception var13) {
                        var24 = var13;

                        try {
                           FileLog.e((Throwable)var24);
                        } catch (Exception var12) {
                           var10000 = var12;
                           var10001 = false;
                           break label149;
                        }
                     }

                     try {
                        if (this.currentMessagesGroup != null && this.getParent() != null) {
                           ((ViewGroup)this.getParent()).invalidate();
                        }
                     } catch (Exception var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label149;
                     }

                     try {
                        this.invalidate();
                        return true;
                     } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                     }
                  }

                  var24 = var10000;
                  FileLog.e((Throwable)var24);
               } else if (this.pressedLinkType == 3) {
                  this.delegate.didPressUrl(this.currentMessageObject, this.pressedLink, false);
                  this.resetPressedLink(3);
                  return true;
               }

               return false;
            }
         }

         this.resetPressedLink(3);
      }

      return false;
   }

   private boolean checkGameMotionEvent(MotionEvent var1) {
      if (!this.hasGamePreview) {
         return false;
      } else {
         int var2 = (int)var1.getX();
         int var3 = (int)var1.getY();
         if (var1.getAction() == 0) {
            int var4;
            if (this.drawPhotoImage && this.drawImageButton && this.buttonState != -1) {
               var4 = this.buttonX;
               if (var2 >= var4 && var2 <= var4 + AndroidUtilities.dp(48.0F)) {
                  var4 = this.buttonY;
                  if (var3 >= var4 && var3 <= var4 + AndroidUtilities.dp(48.0F) && this.radialProgress.getIcon() != 4) {
                     this.buttonPressed = 1;
                     this.invalidate();
                     return true;
                  }
               }
            }

            if (this.drawPhotoImage && this.photoImage.isInsideImage((float)var2, (float)var3)) {
               this.gamePreviewPressed = true;
               return true;
            }

            if (this.descriptionLayout != null && var3 >= this.descriptionY) {
               Exception var10000;
               Exception var20;
               label158: {
                  int var5;
                  int var6;
                  boolean var10001;
                  StaticLayout var18;
                  try {
                     var5 = this.unmovedTextX;
                     var6 = AndroidUtilities.dp(10.0F);
                     var4 = this.descriptionX;
                     int var7 = this.descriptionY;
                     var3 = this.descriptionLayout.getLineForVertical(var3 - var7);
                     var18 = this.descriptionLayout;
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }

                  float var8 = (float)(var2 - (var5 + var6 + var4));

                  float var9;
                  try {
                     var2 = var18.getOffsetForHorizontal(var3, var8);
                     var9 = this.descriptionLayout.getLineLeft(var3);
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label158;
                  }

                  if (var9 > var8) {
                     return false;
                  }

                  ClickableSpan[] var10;
                  Spannable var19;
                  boolean var22;
                  label137: {
                     label136: {
                        try {
                           if (var9 + this.descriptionLayout.getLineWidth(var3) < var8) {
                              return false;
                           }

                           var19 = (Spannable)this.currentMessageObject.linkDescription;
                           var10 = (ClickableSpan[])var19.getSpans(var2, var2, ClickableSpan.class);
                           if (var10.length != 0 && (var10.length == 0 || !(var10[0] instanceof URLSpanBotCommand) || URLSpanBotCommand.enabled)) {
                              break label136;
                           }
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break label158;
                        }

                        var22 = true;
                        break label137;
                     }

                     var22 = false;
                  }

                  if (var22) {
                     return false;
                  }

                  try {
                     this.pressedLink = var10[0];
                     this.linkBlockNum = -10;
                     this.pressedLinkType = 2;
                     this.resetUrlPaths(false);
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label158;
                  }

                  try {
                     LinkPath var24 = this.obtainNewUrlPath(false);
                     var2 = var19.getSpanStart(this.pressedLink);
                     var24.setCurrentLayout(this.descriptionLayout, var2, 0.0F);
                     this.descriptionLayout.getSelectionPath(var2, var19.getSpanEnd(this.pressedLink), var24);
                  } catch (Exception var13) {
                     var20 = var13;

                     try {
                        FileLog.e((Throwable)var20);
                     } catch (Exception var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label158;
                     }
                  }

                  try {
                     this.invalidate();
                     return true;
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                  }
               }

               var20 = var10000;
               FileLog.e((Throwable)var20);
            }
         } else if (var1.getAction() == 1) {
            if (this.pressedLinkType != 2 && !this.gamePreviewPressed && this.buttonPressed == 0) {
               this.resetPressedLink(2);
            } else if (this.buttonPressed != 0) {
               this.buttonPressed = 0;
               this.playSoundEffect(0);
               this.didPressButton(true, false);
               this.invalidate();
            } else {
               CharacterStyle var21 = this.pressedLink;
               if (var21 == null) {
                  this.gamePreviewPressed = false;

                  for(var2 = 0; var2 < this.botButtons.size(); ++var2) {
                     ChatMessageCell.BotButton var23 = (ChatMessageCell.BotButton)this.botButtons.get(var2);
                     if (var23.button instanceof TLRPC.TL_keyboardButtonGame) {
                        this.playSoundEffect(0);
                        this.delegate.didPressBotButton(this, var23.button);
                        this.invalidate();
                        break;
                     }
                  }

                  this.resetPressedLink(2);
                  return true;
               }

               if (var21 instanceof URLSpan) {
                  Browser.openUrl(this.getContext(), ((URLSpan)this.pressedLink).getURL());
               } else if (var21 instanceof ClickableSpan) {
                  ((ClickableSpan)var21).onClick(this);
               }

               this.resetPressedLink(2);
            }
         }

         return false;
      }
   }

   private boolean checkInstantButtonMotionEvent(MotionEvent var1) {
      if (this.drawInstantView && this.currentMessageObject.type != 0) {
         int var2 = (int)var1.getX();
         int var3 = (int)var1.getY();
         Drawable var7;
         if (var1.getAction() == 0) {
            if (this.drawInstantView) {
               RectF var6 = this.instantButtonRect;
               float var4 = (float)var2;
               float var5 = (float)var3;
               if (var6.contains(var4, var5)) {
                  this.instantPressed = true;
                  if (VERSION.SDK_INT >= 21) {
                     var7 = this.selectorDrawable;
                     if (var7 != null && var7.getBounds().contains(var2, var3)) {
                        this.selectorDrawable.setState(this.pressedState);
                        this.selectorDrawable.setHotspot(var4, var5);
                        this.instantButtonPressed = true;
                     }
                  }

                  this.invalidate();
                  return true;
               }
            }
         } else if (var1.getAction() == 1) {
            if (this.instantPressed) {
               ChatMessageCell.ChatMessageCellDelegate var8 = this.delegate;
               if (var8 != null) {
                  var8.didPressInstantButton(this, this.drawInstantViewType);
               }

               this.playSoundEffect(0);
               if (VERSION.SDK_INT >= 21) {
                  var7 = this.selectorDrawable;
                  if (var7 != null) {
                     var7.setState(StateSet.NOTHING);
                  }
               }

               this.instantButtonPressed = false;
               this.instantPressed = false;
               this.invalidate();
            }
         } else if (var1.getAction() == 2 && this.instantButtonPressed && VERSION.SDK_INT >= 21) {
            var7 = this.selectorDrawable;
            if (var7 != null) {
               var7.setHotspot((float)var2, (float)var3);
            }
         }
      }

      return false;
   }

   private boolean checkLinkPreviewMotionEvent(MotionEvent var1) {
      if (this.currentMessageObject.type == 0 && this.hasLinkPreview) {
         int var2 = (int)var1.getX();
         int var3 = (int)var1.getY();
         int var4 = this.unmovedTextX;
         if (var2 >= var4 && var2 <= var4 + this.backgroundWidth) {
            int var5 = this.textY;
            int var6 = this.currentMessageObject.textHeight;
            if (var3 >= var5 + var6) {
               int var7 = this.linkPreviewHeight;
               byte var25;
               if (this.drawInstantView) {
                  var25 = 46;
               } else {
                  var25 = 0;
               }

               if (var3 <= var5 + var6 + var7 + AndroidUtilities.dp((float)(var25 + 8))) {
                  TLRPC.WebPage var21;
                  Drawable var22;
                  if (var1.getAction() != 0) {
                     if (var1.getAction() == 1) {
                        if (this.instantPressed) {
                           ChatMessageCell.ChatMessageCellDelegate var23 = this.delegate;
                           if (var23 != null) {
                              var23.didPressInstantButton(this, this.drawInstantViewType);
                           }

                           this.playSoundEffect(0);
                           if (VERSION.SDK_INT >= 21) {
                              var22 = this.selectorDrawable;
                              if (var22 != null) {
                                 var22.setState(StateSet.NOTHING);
                              }
                           }

                           this.instantButtonPressed = false;
                           this.instantPressed = false;
                           this.invalidate();
                        } else if (this.pressedLinkType != 2 && this.buttonPressed == 0 && this.miniButtonPressed == 0 && this.videoButtonPressed == 0 && !this.linkPreviewPressed) {
                           this.resetPressedLink(2);
                        } else if (this.videoButtonPressed == 1) {
                           this.videoButtonPressed = 0;
                           this.playSoundEffect(0);
                           this.didPressButton(true, true);
                           this.invalidate();
                        } else if (this.buttonPressed != 0) {
                           this.buttonPressed = 0;
                           this.playSoundEffect(0);
                           if (this.drawVideoImageButton) {
                              this.didClickedImage();
                           } else {
                              this.didPressButton(true, false);
                           }

                           this.invalidate();
                        } else if (this.miniButtonPressed != 0) {
                           this.miniButtonPressed = 0;
                           this.playSoundEffect(0);
                           this.didPressMiniButton(true);
                           this.invalidate();
                        } else {
                           CharacterStyle var24 = this.pressedLink;
                           if (var24 == null) {
                              var4 = this.documentAttachType;
                              if (var4 == 7) {
                                 if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject) && !MediaController.getInstance().isMessagePaused()) {
                                    MediaController.getInstance().pauseMessage(this.currentMessageObject);
                                 } else {
                                    this.delegate.needPlayMessage(this.currentMessageObject);
                                 }
                              } else if (var4 == 2 && this.drawImageButton) {
                                 var4 = this.buttonState;
                                 if (var4 == -1) {
                                    if (SharedConfig.autoplayGifs) {
                                       this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
                                    } else {
                                       this.buttonState = 2;
                                       this.currentMessageObject.gifState = 1.0F;
                                       this.photoImage.setAllowStartAnimation(false);
                                       this.photoImage.stopAnimation();
                                       this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
                                       this.invalidate();
                                       this.playSoundEffect(0);
                                    }
                                 } else if (var4 == 2 || var4 == 0) {
                                    this.didPressButton(true, false);
                                    this.playSoundEffect(0);
                                 }
                              } else {
                                 var21 = this.currentMessageObject.messageOwner.media.webpage;
                                 if (var21 != null && !TextUtils.isEmpty(var21.embed_url)) {
                                    this.delegate.needOpenWebView(var21.embed_url, var21.site_name, var21.title, var21.url, var21.embed_width, var21.embed_height);
                                 } else {
                                    var4 = this.buttonState;
                                    if (var4 != -1 && var4 != 3) {
                                       if (var21 != null) {
                                          Browser.openUrl(this.getContext(), var21.url);
                                       }
                                    } else {
                                       this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
                                       this.playSoundEffect(0);
                                    }
                                 }
                              }

                              this.resetPressedLink(2);
                              return true;
                           }

                           if (var24 instanceof URLSpan) {
                              Browser.openUrl(this.getContext(), ((URLSpan)this.pressedLink).getURL());
                           } else if (var24 instanceof ClickableSpan) {
                              ((ClickableSpan)var24).onClick(this);
                           }

                           this.resetPressedLink(2);
                        }
                     } else if (var1.getAction() == 2 && this.instantButtonPressed && VERSION.SDK_INT >= 21) {
                        var22 = this.selectorDrawable;
                        if (var22 != null) {
                           var22.setHotspot((float)var2, (float)var3);
                        }
                     }
                  } else {
                     boolean var26;
                     if (this.descriptionLayout != null && var3 >= this.descriptionY) {
                        label329: {
                           Exception var10000;
                           Exception var20;
                           label339: {
                              boolean var10001;
                              StaticLayout var18;
                              try {
                                 var4 = this.unmovedTextX;
                                 var6 = AndroidUtilities.dp(10.0F);
                                 var7 = this.descriptionX;
                                 var5 = var3 - this.descriptionY;
                                 if (var5 > this.descriptionLayout.getHeight()) {
                                    break label329;
                                 }

                                 var5 = this.descriptionLayout.getLineForVertical(var5);
                                 var18 = this.descriptionLayout;
                              } catch (Exception var17) {
                                 var10000 = var17;
                                 var10001 = false;
                                 break label339;
                              }

                              float var8 = (float)(var2 - (var4 + var6 + var7));

                              float var9;
                              try {
                                 var4 = var18.getOffsetForHorizontal(var5, var8);
                                 var9 = this.descriptionLayout.getLineLeft(var5);
                              } catch (Exception var16) {
                                 var10000 = var16;
                                 var10001 = false;
                                 break label339;
                              }

                              if (var9 > var8) {
                                 break label329;
                              }

                              ClickableSpan[] var10;
                              Spannable var19;
                              label319: {
                                 label318: {
                                    try {
                                       if (var9 + this.descriptionLayout.getLineWidth(var5) < var8) {
                                          break label329;
                                       }

                                       var19 = (Spannable)this.currentMessageObject.linkDescription;
                                       var10 = (ClickableSpan[])var19.getSpans(var4, var4, ClickableSpan.class);
                                       if (var10.length != 0 && (var10.length == 0 || !(var10[0] instanceof URLSpanBotCommand) || URLSpanBotCommand.enabled)) {
                                          break label318;
                                       }
                                    } catch (Exception var15) {
                                       var10000 = var15;
                                       var10001 = false;
                                       break label339;
                                    }

                                    var26 = true;
                                    break label319;
                                 }

                                 var26 = false;
                              }

                              if (var26) {
                                 break label329;
                              }

                              try {
                                 this.pressedLink = var10[0];
                                 this.linkBlockNum = -10;
                                 this.pressedLinkType = 2;
                                 this.resetUrlPaths(false);
                              } catch (Exception var14) {
                                 var10000 = var14;
                                 var10001 = false;
                                 break label339;
                              }

                              try {
                                 LinkPath var27 = this.obtainNewUrlPath(false);
                                 var4 = var19.getSpanStart(this.pressedLink);
                                 var27.setCurrentLayout(this.descriptionLayout, var4, 0.0F);
                                 this.descriptionLayout.getSelectionPath(var4, var19.getSpanEnd(this.pressedLink), var27);
                              } catch (Exception var13) {
                                 var20 = var13;

                                 try {
                                    FileLog.e((Throwable)var20);
                                 } catch (Exception var12) {
                                    var10000 = var12;
                                    var10001 = false;
                                    break label339;
                                 }
                              }

                              try {
                                 this.invalidate();
                                 return true;
                              } catch (Exception var11) {
                                 var10000 = var11;
                                 var10001 = false;
                              }
                           }

                           var20 = var10000;
                           FileLog.e((Throwable)var20);
                        }
                     }

                     if (this.pressedLink == null) {
                        label287: {
                           var5 = AndroidUtilities.dp(48.0F);
                           if (this.miniButtonState >= 0) {
                              var4 = AndroidUtilities.dp(27.0F);
                              var7 = this.buttonX;
                              if (var2 >= var7 + var4 && var2 <= var7 + var4 + var5) {
                                 var7 = this.buttonY;
                                 if (var3 >= var7 + var4 && var3 <= var7 + var4 + var5) {
                                    var26 = true;
                                    break label287;
                                 }
                              }
                           }

                           var26 = false;
                        }

                        if (var26) {
                           this.miniButtonPressed = 1;
                           this.invalidate();
                           return true;
                        }

                        if (this.drawVideoImageButton && this.buttonState != -1) {
                           var4 = this.videoButtonX;
                           if (var2 >= var4 && var2 <= var4 + AndroidUtilities.dp(34.0F) + Math.max(this.infoWidth, this.docTitleWidth)) {
                              var4 = this.videoButtonY;
                              if (var3 >= var4 && var3 <= var4 + AndroidUtilities.dp(30.0F)) {
                                 this.videoButtonPressed = 1;
                                 this.invalidate();
                                 return true;
                              }
                           }
                        }

                        if (this.drawPhotoImage && this.drawImageButton && this.buttonState != -1) {
                           label360: {
                              if (this.checkOnlyButtonPressed || !this.photoImage.isInsideImage((float)var2, (float)var3)) {
                                 var4 = this.buttonX;
                                 if (var2 < var4 || var2 > var4 + AndroidUtilities.dp(48.0F)) {
                                    break label360;
                                 }

                                 var4 = this.buttonY;
                                 if (var3 < var4 || var3 > var4 + AndroidUtilities.dp(48.0F) || this.radialProgress.getIcon() == 4) {
                                    break label360;
                                 }
                              }

                              this.buttonPressed = 1;
                              this.invalidate();
                              return true;
                           }
                        }

                        if (this.drawInstantView) {
                           this.instantPressed = true;
                           if (VERSION.SDK_INT >= 21) {
                              var22 = this.selectorDrawable;
                              if (var22 != null && var22.getBounds().contains(var2, var3)) {
                                 this.selectorDrawable.setState(this.pressedState);
                                 this.selectorDrawable.setHotspot((float)var2, (float)var3);
                                 this.instantButtonPressed = true;
                              }
                           }

                           this.invalidate();
                           return true;
                        }

                        if (this.documentAttachType != 1 && this.drawPhotoImage && this.photoImage.isInsideImage((float)var2, (float)var3)) {
                           this.linkPreviewPressed = true;
                           var21 = this.currentMessageObject.messageOwner.media.webpage;
                           if (this.documentAttachType != 2 || this.buttonState != -1 || !SharedConfig.autoplayGifs || this.photoImage.getAnimation() != null && TextUtils.isEmpty(var21.embed_url)) {
                              return true;
                           }

                           this.linkPreviewPressed = false;
                           return false;
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private void checkLocationExpired() {
      MessageObject var1 = this.currentMessageObject;
      if (var1 != null) {
         boolean var2 = this.isCurrentLocationTimeExpired(var1);
         if (var2 != this.locationExpired) {
            this.locationExpired = var2;
            if (!this.locationExpired) {
               AndroidUtilities.runOnUIThread(this.invalidateRunnable, 1000L);
               this.scheduledInvalidate = true;
               int var3 = this.backgroundWidth - AndroidUtilities.dp(91.0F);
               this.docTitleLayout = new StaticLayout(TextUtils.ellipsize(LocaleController.getString("AttachLiveLocation", 2131558721), Theme.chat_locationTitlePaint, (float)var3, TruncateAt.END), Theme.chat_locationTitlePaint, var3, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            } else {
               var1 = this.currentMessageObject;
               this.currentMessageObject = null;
               this.setMessageObject(var1, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
            }
         }

      }
   }

   private boolean checkNeedDrawShareButton(MessageObject var1) {
      MessageObject.GroupedMessagePosition var2 = this.currentPosition;
      if (var2 != null && !var2.last) {
         return false;
      } else {
         if (var1.messageOwner.fwd_from != null && !var1.isOutOwner() && var1.messageOwner.fwd_from.saved_from_peer != null && var1.getDialogId() == (long)UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            this.drwaShareGoIcon = true;
         }

         return var1.needDrawShareButton();
      }
   }

   private boolean checkOtherButtonMotionEvent(MotionEvent var1) {
      int var2 = this.currentMessageObject.type;
      boolean var3 = true;
      boolean var4;
      if (var2 == 16) {
         var4 = true;
      } else {
         var4 = false;
      }

      boolean var6 = var4;
      int var7;
      if (!var4) {
         label66: {
            label65: {
               var7 = this.documentAttachType;
               if (var7 != 1) {
                  var2 = this.currentMessageObject.type;
                  if (var2 != 12 && var7 != 5 && var7 != 4 && var7 != 2 && var2 != 8) {
                     break label65;
                  }
               }

               if (!this.hasGamePreview && !this.hasInvoicePreview) {
                  var6 = true;
                  break label66;
               }
            }

            var6 = false;
         }
      }

      if (!var6) {
         return false;
      } else {
         int var5 = (int)var1.getX();
         var2 = (int)var1.getY();
         if (var1.getAction() == 0) {
            if (this.currentMessageObject.type == 16) {
               var7 = this.otherX;
               if (var5 >= var7 && var5 <= var7 + AndroidUtilities.dp(235.0F) && var2 >= this.otherY - AndroidUtilities.dp(14.0F) && var2 <= this.otherY + AndroidUtilities.dp(50.0F)) {
                  this.otherPressed = true;
                  this.invalidate();
                  return var3;
               }
            } else if (var5 >= this.otherX - AndroidUtilities.dp(20.0F) && var5 <= this.otherX + AndroidUtilities.dp(20.0F) && var2 >= this.otherY - AndroidUtilities.dp(4.0F) && var2 <= this.otherY + AndroidUtilities.dp(30.0F)) {
               this.otherPressed = true;
               this.invalidate();
               return var3;
            }
         } else if (var1.getAction() == 1 && this.otherPressed) {
            this.otherPressed = false;
            this.playSoundEffect(0);
            this.delegate.didPressOther(this, (float)this.otherX, (float)this.otherY);
            this.invalidate();
            return var3;
         }

         var3 = false;
         return var3;
      }
   }

   private boolean checkPhotoImageMotionEvent(MotionEvent var1) {
      boolean var2 = this.drawPhotoImage;
      boolean var3 = false;
      boolean var4 = true;
      boolean var5 = true;
      if (!var2 && this.documentAttachType != 1) {
         return false;
      } else {
         int var6 = (int)var1.getX();
         int var7 = (int)var1.getY();
         int var9;
         if (var1.getAction() == 0) {
            int var8;
            boolean var11;
            label136: {
               var8 = AndroidUtilities.dp(48.0F);
               if (this.miniButtonState >= 0) {
                  var9 = AndroidUtilities.dp(27.0F);
                  int var10 = this.buttonX;
                  if (var6 >= var10 + var9 && var6 <= var10 + var9 + var8) {
                     var10 = this.buttonY;
                     if (var7 >= var10 + var9 && var7 <= var10 + var9 + var8) {
                        var11 = true;
                        break label136;
                     }
                  }
               }

               var11 = false;
            }

            if (var11) {
               this.miniButtonPressed = 1;
               this.invalidate();
            } else {
               label157: {
                  if (this.buttonState != -1 && this.radialProgress.getIcon() != 4) {
                     var9 = this.buttonX;
                     if (var6 >= var9 && var6 <= var9 + var8) {
                        var9 = this.buttonY;
                        if (var7 >= var9 && var7 <= var9 + var8) {
                           this.buttonPressed = 1;
                           this.invalidate();
                           break label157;
                        }
                     }
                  }

                  if (this.drawVideoImageButton && this.buttonState != -1) {
                     var9 = this.videoButtonX;
                     if (var6 >= var9 && var6 <= var9 + AndroidUtilities.dp(34.0F) + Math.max(this.infoWidth, this.docTitleWidth)) {
                        var9 = this.videoButtonY;
                        if (var7 >= var9 && var7 <= var9 + AndroidUtilities.dp(30.0F)) {
                           this.videoButtonPressed = 1;
                           this.invalidate();
                           break label157;
                        }
                     }
                  }

                  if (this.documentAttachType == 1) {
                     if (var6 >= this.photoImage.getImageX() && var6 <= this.photoImage.getImageX() + this.backgroundWidth - AndroidUtilities.dp(50.0F) && var7 >= this.photoImage.getImageY() && var7 <= this.photoImage.getImageY() + this.photoImage.getImageHeight()) {
                        this.imagePressed = true;
                        break label157;
                     }
                  } else if (!this.currentMessageObject.isAnyKindOfSticker() || this.currentMessageObject.getInputStickerSet() != null) {
                     if (var6 >= this.photoImage.getImageX() && var6 <= this.photoImage.getImageX() + this.photoImage.getImageWidth() && var7 >= this.photoImage.getImageY() && var7 <= this.photoImage.getImageY() + this.photoImage.getImageHeight()) {
                        this.imagePressed = true;
                     } else {
                        var5 = false;
                     }

                     var4 = var5;
                     if (this.currentMessageObject.type != 12) {
                        break label157;
                     }

                     var4 = var5;
                     if (MessagesController.getInstance(this.currentAccount).getUser(this.currentMessageObject.messageOwner.media.user_id) != null) {
                        break label157;
                     }

                     this.imagePressed = false;
                  }

                  var4 = false;
               }
            }

            if (this.imagePressed) {
               if (this.currentMessageObject.isSendError()) {
                  this.imagePressed = false;
                  var4 = var3;
               } else if (this.currentMessageObject.type == 8 && this.buttonState == -1 && SharedConfig.autoplayGifs && this.photoImage.getAnimation() == null) {
                  this.imagePressed = false;
                  var4 = var3;
               }
            }
         } else {
            var4 = var3;
            if (var1.getAction() == 1) {
               if (this.videoButtonPressed == 1) {
                  this.videoButtonPressed = 0;
                  this.playSoundEffect(0);
                  this.didPressButton(true, true);
                  this.invalidate();
                  var4 = var3;
               } else if (this.buttonPressed == 1) {
                  this.buttonPressed = 0;
                  this.playSoundEffect(0);
                  if (this.drawVideoImageButton) {
                     this.didClickedImage();
                  } else {
                     this.didPressButton(true, false);
                  }

                  this.invalidate();
                  var4 = var3;
               } else if (this.miniButtonPressed == 1) {
                  this.miniButtonPressed = 0;
                  this.playSoundEffect(0);
                  this.didPressMiniButton(true);
                  this.invalidate();
                  var4 = var3;
               } else {
                  var4 = var3;
                  if (this.imagePressed) {
                     this.imagePressed = false;
                     var9 = this.buttonState;
                     if (var9 != -1 && var9 != 2 && var9 != 3 && !this.drawVideoImageButton) {
                        if (var9 == 0) {
                           this.playSoundEffect(0);
                           this.didPressButton(true, false);
                        }
                     } else {
                        this.playSoundEffect(0);
                        this.didClickedImage();
                     }

                     this.invalidate();
                     var4 = var3;
                  }
               }
            }
         }

         return var4;
      }
   }

   private boolean checkPollButtonMotionEvent(MotionEvent var1) {
      long var2 = this.currentMessageObject.eventId;
      boolean var4 = false;
      boolean var5 = var4;
      if (var2 == 0L) {
         var5 = var4;
         if (!this.pollVoted) {
            var5 = var4;
            if (!this.pollClosed) {
               var5 = var4;
               if (!this.pollVoteInProgress) {
                  var5 = var4;
                  if (!this.pollUnvoteInProgress) {
                     var5 = var4;
                     if (!this.pollButtons.isEmpty()) {
                        MessageObject var6 = this.currentMessageObject;
                        var5 = var4;
                        if (var6.type == 17) {
                           if (!var6.isSent()) {
                              var5 = var4;
                           } else {
                              int var7 = (int)var1.getX();
                              int var8 = (int)var1.getY();
                              int var9;
                              if (var1.getAction() == 0) {
                                 this.pressedVoteButton = -1;
                                 var9 = 0;

                                 while(true) {
                                    var5 = var4;
                                    if (var9 >= this.pollButtons.size()) {
                                       break;
                                    }

                                    ChatMessageCell.PollButton var11 = (ChatMessageCell.PollButton)this.pollButtons.get(var9);
                                    int var10 = var11.y + this.namesOffset - AndroidUtilities.dp(13.0F);
                                    if (var7 >= var11.x && var7 <= var11.x + this.backgroundWidth - AndroidUtilities.dp(31.0F) && var8 >= var10 && var8 <= var11.height + var10 + AndroidUtilities.dp(26.0F)) {
                                       this.pressedVoteButton = var9;
                                       if (VERSION.SDK_INT >= 21) {
                                          Drawable var13 = this.selectorDrawable;
                                          if (var13 != null) {
                                             var13.setBounds(var11.x - AndroidUtilities.dp(9.0F), var10, var11.x + this.backgroundWidth - AndroidUtilities.dp(22.0F), var11.height + var10 + AndroidUtilities.dp(26.0F));
                                             this.selectorDrawable.setState(this.pressedState);
                                             this.selectorDrawable.setHotspot((float)var7, (float)var8);
                                          }
                                       }

                                       this.invalidate();
                                       var5 = true;
                                       break;
                                    }

                                    ++var9;
                                 }
                              } else {
                                 Drawable var12;
                                 if (var1.getAction() == 1) {
                                    var5 = var4;
                                    if (this.pressedVoteButton != -1) {
                                       this.playSoundEffect(0);
                                       if (VERSION.SDK_INT >= 21) {
                                          var12 = this.selectorDrawable;
                                          if (var12 != null) {
                                             var12.setState(StateSet.NOTHING);
                                          }
                                       }

                                       var9 = this.pressedVoteButton;
                                       this.pollVoteInProgressNum = var9;
                                       this.pollVoteInProgress = true;
                                       this.voteCurrentProgressTime = 0.0F;
                                       this.firstCircleLength = true;
                                       this.voteCurrentCircleLength = 360.0F;
                                       this.voteRisingCircleLength = false;
                                       this.delegate.didPressVoteButton(this, ((ChatMessageCell.PollButton)this.pollButtons.get(var9)).answer);
                                       this.pressedVoteButton = -1;
                                       this.invalidate();
                                       var5 = var4;
                                    }
                                 } else {
                                    var5 = var4;
                                    if (var1.getAction() == 2) {
                                       var5 = var4;
                                       if (this.pressedVoteButton != -1) {
                                          var5 = var4;
                                          if (VERSION.SDK_INT >= 21) {
                                             var12 = this.selectorDrawable;
                                             var5 = var4;
                                             if (var12 != null) {
                                                var12.setHotspot((float)var7, (float)var8);
                                                var5 = var4;
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
               }
            }
         }
      }

      return var5;
   }

   private boolean checkTextBlockMotionEvent(MotionEvent var1) {
      MessageObject var2 = this.currentMessageObject;
      if (var2.type == 0) {
         ArrayList var39 = var2.textLayoutBlocks;
         if (var39 != null && !var39.isEmpty() && this.currentMessageObject.messageText instanceof Spannable && (var1.getAction() == 0 || var1.getAction() == 1 && this.pressedLinkType == 1)) {
            int var3 = (int)var1.getX();
            int var4 = (int)var1.getY();
            int var5 = this.textX;
            if (var3 >= var5) {
               int var6 = this.textY;
               if (var4 >= var6) {
                  var2 = this.currentMessageObject;
                  if (var3 <= var5 + var2.textWidth && var4 <= var2.textHeight + var6) {
                     var6 = var4 - var6;
                     var4 = 0;

                     for(var5 = 0; var4 < this.currentMessageObject.textLayoutBlocks.size() && ((MessageObject.TextLayoutBlock)this.currentMessageObject.textLayoutBlocks.get(var4)).textYOffset <= (float)var6; var5 = var4++) {
                     }

                     Exception var10000;
                     Exception var43;
                     label307: {
                        MessageObject.TextLayoutBlock var7;
                        boolean var10001;
                        try {
                           var7 = (MessageObject.TextLayoutBlock)this.currentMessageObject.textLayoutBlocks.get(var5);
                        } catch (Exception var36) {
                           var10000 = var36;
                           var10001 = false;
                           break label307;
                        }

                        float var8 = (float)var3;

                        float var9;
                        float var10;
                        label272: {
                           try {
                              var9 = (float)this.textX;
                              if (var7.isRtl()) {
                                 var10 = this.currentMessageObject.textXOffset;
                                 break label272;
                              }
                           } catch (Exception var35) {
                              var10000 = var35;
                              var10001 = false;
                              break label307;
                           }

                           var10 = 0.0F;
                        }

                        var4 = (int)(var8 - (var9 - var10));

                        StaticLayout var40;
                        try {
                           var3 = (int)((float)var6 - var7.textYOffset);
                           var3 = var7.textLayout.getLineForVertical(var3);
                           var40 = var7.textLayout;
                        } catch (Exception var34) {
                           var10000 = var34;
                           var10001 = false;
                           break label307;
                        }

                        var10 = (float)var4;

                        try {
                           var4 = var40.getOffsetForHorizontal(var3, var10);
                           var8 = var7.textLayout.getLineLeft(var3);
                        } catch (Exception var33) {
                           var10000 = var33;
                           var10001 = false;
                           break label307;
                        }

                        if (var8 > var10) {
                           return false;
                        }

                        Spannable var11;
                        CharacterStyle[] var41;
                        try {
                           if (var8 + var7.textLayout.getLineWidth(var3) < var10) {
                              return false;
                           }

                           var11 = (Spannable)this.currentMessageObject.messageText;
                           var41 = (CharacterStyle[])var11.getSpans(var4, var4, ClickableSpan.class);
                        } catch (Exception var32) {
                           var10000 = var32;
                           var10001 = false;
                           break label307;
                        }

                        boolean var45;
                        label319: {
                           if (var41 != null) {
                              label317: {
                                 try {
                                    if (var41.length == 0) {
                                       break label317;
                                    }
                                 } catch (Exception var31) {
                                    var10000 = var31;
                                    var10001 = false;
                                    break label307;
                                 }

                                 var45 = false;
                                 break label319;
                              }
                           }

                           try {
                              var41 = (CharacterStyle[])var11.getSpans(var4, var4, URLSpanMono.class);
                           } catch (Exception var30) {
                              var10000 = var30;
                              var10001 = false;
                              break label307;
                           }

                           var45 = true;
                        }

                        boolean var42;
                        label237: {
                           label236: {
                              try {
                                 if (var41.length != 0 && (var41.length == 0 || !(var41[0] instanceof URLSpanBotCommand) || URLSpanBotCommand.enabled)) {
                                    break label236;
                                 }
                              } catch (Exception var29) {
                                 var10000 = var29;
                                 var10001 = false;
                                 break label307;
                              }

                              var42 = true;
                              break label237;
                           }

                           var42 = false;
                        }

                        if (var42) {
                           return false;
                        }

                        label221: {
                           try {
                              if (var1.getAction() == 0) {
                                 this.pressedLink = var41[0];
                                 this.linkBlockNum = var5;
                                 this.pressedLinkType = 1;
                                 this.resetUrlPaths(false);
                                 break label221;
                              }
                           } catch (Exception var28) {
                              var10000 = var28;
                              var10001 = false;
                              break label307;
                           }

                           try {
                              if (var41[0] == this.pressedLink) {
                                 this.delegate.didPressUrl(this.currentMessageObject, this.pressedLink, false);
                                 this.resetPressedLink(1);
                                 return true;
                              }

                              return false;
                           } catch (Exception var27) {
                              var10000 = var27;
                              var10001 = false;
                              break label307;
                           }
                        }

                        label311: {
                           label312: {
                              int var12;
                              LinkPath var37;
                              CharacterStyle[] var38;
                              MessageObject.TextLayoutBlock var44;
                              label313: {
                                 try {
                                    var37 = this.obtainNewUrlPath(false);
                                    var6 = var11.getSpanStart(this.pressedLink);
                                    var12 = var11.getSpanEnd(this.pressedLink);
                                    var37.setCurrentLayout(var7.textLayout, var6, 0.0F);
                                    var7.textLayout.getSelectionPath(var6, var12, var37);
                                    if (var12 < var7.charactersEnd) {
                                       break label313;
                                    }
                                 } catch (Exception var24) {
                                    var10000 = var24;
                                    var10001 = false;
                                    break label312;
                                 }

                                 var3 = var5 + 1;

                                 while(true) {
                                    try {
                                       if (var3 >= this.currentMessageObject.textLayoutBlocks.size()) {
                                          break;
                                       }

                                       var44 = (MessageObject.TextLayoutBlock)this.currentMessageObject.textLayoutBlocks.get(var3);
                                    } catch (Exception var23) {
                                       var10000 = var23;
                                       var10001 = false;
                                       break label312;
                                    }

                                    if (var45) {
                                       try {
                                          var38 = (CharacterStyle[])var11.getSpans(var44.charactersOffset, var44.charactersOffset, URLSpanMono.class);
                                       } catch (Exception var22) {
                                          var10000 = var22;
                                          var10001 = false;
                                          break label312;
                                       }
                                    } else {
                                       try {
                                          var38 = (CharacterStyle[])var11.getSpans(var44.charactersOffset, var44.charactersOffset, ClickableSpan.class);
                                       } catch (Exception var21) {
                                          var10000 = var21;
                                          var10001 = false;
                                          break label312;
                                       }
                                    }

                                    if (var38 == null) {
                                       break;
                                    }

                                    label204:
                                    try {
                                       if (var38.length != 0 && var38[0] == this.pressedLink) {
                                          break label204;
                                       }
                                       break;
                                    } catch (Exception var26) {
                                       var10000 = var26;
                                       var10001 = false;
                                       break label312;
                                    }

                                    try {
                                       var37 = this.obtainNewUrlPath(false);
                                       var37.setCurrentLayout(var44.textLayout, 0, var44.textYOffset - var7.textYOffset);
                                       var44.textLayout.getSelectionPath(0, var12, var37);
                                       if (var12 < var44.charactersEnd - 1) {
                                          break;
                                       }
                                    } catch (Exception var25) {
                                       var10000 = var25;
                                       var10001 = false;
                                       break label312;
                                    }

                                    ++var3;
                                 }
                              }

                              try {
                                 if (var6 > var7.charactersOffset) {
                                    break label311;
                                 }
                              } catch (Exception var20) {
                                 var10000 = var20;
                                 var10001 = false;
                                 break label312;
                              }

                              --var5;
                              var3 = 0;

                              while(true) {
                                 if (var5 < 0) {
                                    break label311;
                                 }

                                 try {
                                    var44 = (MessageObject.TextLayoutBlock)this.currentMessageObject.textLayoutBlocks.get(var5);
                                 } catch (Exception var19) {
                                    var10000 = var19;
                                    var10001 = false;
                                    break;
                                 }

                                 if (var45) {
                                    try {
                                       var38 = (CharacterStyle[])var11.getSpans(var44.charactersEnd - 1, var44.charactersEnd - 1, URLSpanMono.class);
                                    } catch (Exception var18) {
                                       var10000 = var18;
                                       var10001 = false;
                                       break;
                                    }
                                 } else {
                                    try {
                                       var38 = (CharacterStyle[])var11.getSpans(var44.charactersEnd - 1, var44.charactersEnd - 1, ClickableSpan.class);
                                    } catch (Exception var17) {
                                       var10000 = var17;
                                       var10001 = false;
                                       break;
                                    }
                                 }

                                 if (var38 == null) {
                                    break label311;
                                 }

                                 try {
                                    if (var38.length == 0 || var38[0] != this.pressedLink) {
                                       break label311;
                                    }
                                 } catch (Exception var16) {
                                    var10000 = var16;
                                    var10001 = false;
                                    break;
                                 }

                                 try {
                                    var37 = this.obtainNewUrlPath(false);
                                    var6 = var11.getSpanStart(this.pressedLink);
                                    var3 -= var44.height;
                                    var37.setCurrentLayout(var44.textLayout, var6, (float)var3);
                                    var44.textLayout.getSelectionPath(var6, var11.getSpanEnd(this.pressedLink), var37);
                                    var12 = var44.charactersOffset;
                                 } catch (Exception var15) {
                                    var10000 = var15;
                                    var10001 = false;
                                    break;
                                 }

                                 if (var6 > var12) {
                                    break label311;
                                 }

                                 --var5;
                              }
                           }

                           var43 = var10000;

                           try {
                              FileLog.e((Throwable)var43);
                           } catch (Exception var14) {
                              var10000 = var14;
                              var10001 = false;
                              break label307;
                           }
                        }

                        try {
                           this.invalidate();
                           return true;
                        } catch (Exception var13) {
                           var10000 = var13;
                           var10001 = false;
                        }
                     }

                     var43 = var10000;
                     FileLog.e((Throwable)var43);
                     return false;
                  }
               }
            }

            this.resetPressedLink(1);
         }
      }

      return false;
   }

   private int createDocumentLayout(int var1, MessageObject var2) {
      int var3 = var1;
      if (var2.type == 0) {
         this.documentAttach = var2.messageOwner.media.webpage.document;
      } else {
         this.documentAttach = var2.messageOwner.media.document;
      }

      TLRPC.Document var4 = this.documentAttach;
      byte var5 = 0;
      if (var4 == null) {
         return 0;
      } else {
         int var14;
         if (MessageObject.isVoiceDocument(var4)) {
            this.documentAttachType = 3;
            var1 = 0;

            while(true) {
               if (var1 >= this.documentAttach.attributes.size()) {
                  var1 = 0;
                  break;
               }

               TLRPC.DocumentAttribute var20 = (TLRPC.DocumentAttribute)this.documentAttach.attributes.get(var1);
               if (var20 instanceof TLRPC.TL_documentAttributeAudio) {
                  var1 = var20.duration;
                  break;
               }

               ++var1;
            }

            this.widthBeforeNewTimeLine = var3 - AndroidUtilities.dp(94.0F) - (int)Math.ceil((double)Theme.chat_audioTimePaint.measureText("00:00"));
            this.availableTimeWidth = var3 - AndroidUtilities.dp(18.0F);
            this.measureTime(var2);
            int var17 = AndroidUtilities.dp(174.0F);
            var14 = this.timeWidth;
            if (!this.hasLinkPreview) {
               this.backgroundWidth = Math.min(var3, var17 + var14 + var1 * AndroidUtilities.dp(10.0F));
            }

            this.seekBarWaveform.setMessageObject(var2);
            return 0;
         } else if (MessageObject.isMusicDocument(this.documentAttach)) {
            this.documentAttachType = 5;
            var3 = var1 - AndroidUtilities.dp(86.0F);
            var1 = var3;
            if (var3 < 0) {
               var1 = AndroidUtilities.dp(100.0F);
            }

            this.songLayout = new StaticLayout(TextUtils.ellipsize(var2.getMusicTitle().replace('\n', ' '), Theme.chat_audioTitlePaint, (float)(var1 - AndroidUtilities.dp(12.0F)), TruncateAt.END), Theme.chat_audioTitlePaint, var1, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            if (this.songLayout.getLineCount() > 0) {
               this.songX = -((int)Math.ceil((double)this.songLayout.getLineLeft(0)));
            }

            this.performerLayout = new StaticLayout(TextUtils.ellipsize(var2.getMusicAuthor().replace('\n', ' '), Theme.chat_audioPerformerPaint, (float)var1, TruncateAt.END), Theme.chat_audioPerformerPaint, var1, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            if (this.performerLayout.getLineCount() > 0) {
               this.performerX = -((int)Math.ceil((double)this.performerLayout.getLineLeft(0)));
            }

            var1 = 0;

            while(true) {
               if (var1 >= this.documentAttach.attributes.size()) {
                  var1 = 0;
                  break;
               }

               TLRPC.DocumentAttribute var11 = (TLRPC.DocumentAttribute)this.documentAttach.attributes.get(var1);
               if (var11 instanceof TLRPC.TL_documentAttributeAudio) {
                  var1 = var11.duration;
                  break;
               }

               ++var1;
            }

            TextPaint var12 = Theme.chat_audioTimePaint;
            var3 = var1 / 60;
            var1 %= 60;
            var1 = (int)Math.ceil((double)var12.measureText(String.format("%d:%02d / %d:%02d", var3, var1, var3, var1)));
            this.widthBeforeNewTimeLine = this.backgroundWidth - AndroidUtilities.dp(86.0F) - var1;
            this.availableTimeWidth = this.backgroundWidth - AndroidUtilities.dp(28.0F);
            return var1;
         } else {
            String var10;
            if (MessageObject.isVideoDocument(this.documentAttach)) {
               this.documentAttachType = 4;
               if (!var2.needDrawBluredPreview()) {
                  this.updatePlayingMessageProgress();
                  var10 = String.format("%s", AndroidUtilities.formatFileSize((long)this.documentAttach.size));
                  this.docTitleWidth = (int)Math.ceil((double)Theme.chat_infoPaint.measureText(var10));
                  this.docTitleLayout = new StaticLayout(var10, Theme.chat_infoPaint, this.docTitleWidth, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
               }

               return 0;
            } else if (MessageObject.isGifDocument(this.documentAttach)) {
               this.documentAttachType = 2;
               if (!var2.needDrawBluredPreview()) {
                  var10 = LocaleController.getString("AttachGif", 2131558716);
                  this.infoWidth = (int)Math.ceil((double)Theme.chat_infoPaint.measureText(var10));
                  this.infoLayout = new StaticLayout(var10, Theme.chat_infoPaint, this.infoWidth, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                  var10 = String.format("%s", AndroidUtilities.formatFileSize((long)this.documentAttach.size));
                  this.docTitleWidth = (int)Math.ceil((double)Theme.chat_infoPaint.measureText(var10));
                  this.docTitleLayout = new StaticLayout(var10, Theme.chat_infoPaint, this.docTitleWidth, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
               }

               return 0;
            } else {
               String var13 = this.documentAttach.mime_type;
               boolean var7;
               if ((var13 == null || !var13.toLowerCase().startsWith("image/")) && !MessageObject.isDocumentHasThumb(this.documentAttach)) {
                  var7 = false;
               } else {
                  var7 = true;
               }

               this.drawPhotoImage = var7;
               var1 = var1;
               if (!this.drawPhotoImage) {
                  var1 = var3 + AndroidUtilities.dp(30.0F);
               }

               label140: {
                  this.documentAttachType = 1;
                  String var8 = FileLoader.getDocumentFileName(this.documentAttach);
                  if (var8 != null) {
                     var13 = var8;
                     if (var8.length() != 0) {
                        break label140;
                     }
                  }

                  var13 = LocaleController.getString("AttachDocument", 2131558714);
               }

               this.docTitleLayout = StaticLayoutEx.createStaticLayout(var13, Theme.chat_docNamePaint, var1, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false, TruncateAt.MIDDLE, var1, 2, false);
               this.docTitleOffsetX = Integer.MIN_VALUE;
               StaticLayout var15 = this.docTitleLayout;
               if (var15 != null && var15.getLineCount() > 0) {
                  byte var6 = 0;
                  var3 = var5;

                  for(var14 = var6; var3 < this.docTitleLayout.getLineCount(); ++var3) {
                     var14 = Math.max(var14, (int)Math.ceil((double)this.docTitleLayout.getLineWidth(var3)));
                     this.docTitleOffsetX = Math.max(this.docTitleOffsetX, (int)Math.ceil((double)(-this.docTitleLayout.getLineLeft(var3))));
                  }

                  var3 = Math.min(var1, var14);
               } else {
                  this.docTitleOffsetX = 0;
                  var3 = var1;
               }

               StringBuilder var16 = new StringBuilder();
               var16.append(AndroidUtilities.formatFileSize((long)this.documentAttach.size));
               var16.append(" ");
               var16.append(FileLoader.getDocumentExtension(this.documentAttach));
               var13 = var16.toString();
               this.infoWidth = Math.min(var1 - AndroidUtilities.dp(30.0F), (int)Math.ceil((double)Theme.chat_infoPaint.measureText(var13)));
               CharSequence var18 = TextUtils.ellipsize(var13, Theme.chat_infoPaint, (float)this.infoWidth, TruncateAt.END);

               try {
                  if (this.infoWidth < 0) {
                     this.infoWidth = AndroidUtilities.dp(10.0F);
                  }

                  StaticLayout var21 = new StaticLayout(var18, Theme.chat_infoPaint, this.infoWidth, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                  this.infoLayout = var21;
               } catch (Exception var9) {
                  FileLog.e((Throwable)var9);
               }

               if (this.drawPhotoImage) {
                  this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(var2.photoThumbs, 320);
                  this.currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(var2.photoThumbs, 40);
                  if ((DownloadController.getInstance(this.currentAccount).getAutodownloadMask() & 1) == 0) {
                     this.currentPhotoObject = null;
                  }

                  TLRPC.PhotoSize var19 = this.currentPhotoObject;
                  if (var19 == null || var19 == this.currentPhotoObjectThumb) {
                     this.currentPhotoObject = null;
                     this.photoImage.setNeedsQualityThumb(true);
                     this.photoImage.setShouldGenerateQualityThumb(true);
                  }

                  this.currentPhotoFilter = "86_86_b";
                  this.photoImage.setImage(ImageLocation.getForObject(this.currentPhotoObject, var2.photoThumbsObject), "86_86", ImageLocation.getForObject(this.currentPhotoObjectThumb, var2.photoThumbsObject), this.currentPhotoFilter, 0, (String)null, var2, 1);
               }

               return var3;
            }
         }
      }
   }

   private void createInstantViewButton() {
      if (VERSION.SDK_INT >= 21 && this.drawInstantView) {
         this.createSelectorDrawable();
      }

      if (this.drawInstantView && this.instantViewLayout == null) {
         this.instantWidth = AndroidUtilities.dp(33.0F);
         int var1 = this.drawInstantViewType;
         String var2;
         if (var1 == 1) {
            var2 = LocaleController.getString("OpenChannel", 2131560112);
         } else if (var1 == 2) {
            var2 = LocaleController.getString("OpenGroup", 2131560114);
         } else if (var1 == 3) {
            var2 = LocaleController.getString("OpenMessage", 2131560117);
         } else if (var1 == 5) {
            var2 = LocaleController.getString("ViewContact", 2131561052);
         } else if (var1 == 6) {
            var2 = LocaleController.getString("OpenBackground", 2131560111);
         } else {
            var2 = LocaleController.getString("InstantView", 2131559667);
         }

         var1 = this.backgroundWidth - AndroidUtilities.dp(75.0F);
         this.instantViewLayout = new StaticLayout(TextUtils.ellipsize(var2, Theme.chat_instantViewPaint, (float)var1, TruncateAt.END), Theme.chat_instantViewPaint, var1 + AndroidUtilities.dp(2.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         this.instantWidth = this.backgroundWidth - AndroidUtilities.dp(34.0F);
         this.totalHeight += AndroidUtilities.dp(46.0F);
         if (this.currentMessageObject.type == 12) {
            this.totalHeight += AndroidUtilities.dp(14.0F);
         }

         StaticLayout var8 = this.instantViewLayout;
         if (var8 != null && var8.getLineCount() > 0) {
            double var3 = (double)this.instantWidth;
            double var5 = Math.ceil((double)this.instantViewLayout.getLineWidth(0));
            Double.isNaN(var3);
            int var7 = (int)(var3 - var5) / 2;
            if (this.drawInstantViewType == 0) {
               var1 = AndroidUtilities.dp(8.0F);
            } else {
               var1 = 0;
            }

            this.instantTextX = var7 + var1;
            this.instantTextLeftX = (int)this.instantViewLayout.getLineLeft(0);
            this.instantTextX += -this.instantTextLeftX;
         }
      }

   }

   private void createSelectorDrawable() {
      if (VERSION.SDK_INT >= 21) {
         Drawable var1 = this.selectorDrawable;
         String var2 = "chat_outPreviewInstantText";
         if (var1 == null) {
            final Paint var5 = new Paint(1);
            var5.setColor(-1);
            Drawable var3 = new Drawable() {
               RectF rect = new RectF();

               public void draw(Canvas var1) {
                  Rect var2 = this.getBounds();
                  this.rect.set((float)var2.left, (float)var2.top, (float)var2.right, (float)var2.bottom);
                  var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(6.0F), (float)AndroidUtilities.dp(6.0F), var5);
               }

               public int getOpacity() {
                  return -1;
               }

               public void setAlpha(int var1) {
               }

               public void setColorFilter(ColorFilter var1) {
               }
            };
            int[] var6 = StateSet.WILD_CARD;
            if (!this.currentMessageObject.isOutOwner()) {
               var2 = "chat_inPreviewInstantText";
            }

            int var4 = Theme.getColor(var2);
            this.selectorDrawable = new RippleDrawable(new ColorStateList(new int[][]{var6}, new int[]{1610612735 & var4}), (Drawable)null, var3);
            this.selectorDrawable.setCallback(this);
         } else {
            if (!this.currentMessageObject.isOutOwner()) {
               var2 = "chat_inPreviewInstantText";
            }

            Theme.setSelectorDrawableColor(var1, 1610612735 & Theme.getColor(var2), true);
         }

         this.selectorDrawable.setVisible(true, false);
      }
   }

   private void didClickedImage() {
      MessageObject var1 = this.currentMessageObject;
      int var3;
      if (var1.type != 1 && !var1.isAnyKindOfSticker()) {
         var1 = this.currentMessageObject;
         int var2 = var1.type;
         if (var2 == 12) {
            TLRPC.User var5 = MessagesController.getInstance(this.currentAccount).getUser(this.currentMessageObject.messageOwner.media.user_id);
            this.delegate.didPressUserAvatar(this, var5, this.lastTouchX, this.lastTouchY);
         } else if (var2 == 5) {
            if (this.buttonState != -1) {
               this.didPressButton(true, false);
            } else if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject) && !MediaController.getInstance().isMessagePaused()) {
               MediaController.getInstance().pauseMessage(this.currentMessageObject);
            } else {
               this.delegate.needPlayMessage(this.currentMessageObject);
            }
         } else if (var2 == 8) {
            var3 = this.buttonState;
            if (var3 != -1 && (var3 != 1 || !this.canStreamVideo || !this.autoPlayingMedia)) {
               var3 = this.buttonState;
               if (var3 == 2 || var3 == 0) {
                  this.didPressButton(true, false);
               }
            } else {
               this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
            }
         } else {
            var3 = this.documentAttachType;
            if (var3 == 4) {
               if (this.buttonState != -1 && (!this.drawVideoImageButton || !this.autoPlayingMedia && (!SharedConfig.streamMedia || !this.canStreamVideo))) {
                  if (this.drawVideoImageButton) {
                     this.didPressButton(true, true);
                  } else {
                     var3 = this.buttonState;
                     if (var3 == 0 || var3 == 3) {
                        this.didPressButton(true, false);
                     }
                  }
               } else {
                  this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
               }
            } else if (var2 == 4) {
               this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
            } else if (var3 == 1) {
               if (this.buttonState == -1) {
                  this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
               }
            } else if (var3 == 2) {
               if (this.buttonState == -1) {
                  TLRPC.WebPage var4 = var1.messageOwner.media.webpage;
                  if (var4 != null) {
                     String var6 = var4.embed_url;
                     if (var6 != null && var6.length() != 0) {
                        this.delegate.needOpenWebView(var4.embed_url, var4.site_name, var4.description, var4.url, var4.embed_width, var4.embed_height);
                     } else {
                        Browser.openUrl(this.getContext(), var4.url);
                     }
                  }
               }
            } else if (this.hasInvoicePreview && this.buttonState == -1) {
               this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
            }
         }
      } else {
         var3 = this.buttonState;
         if (var3 == -1) {
            this.delegate.didPressImage(this, this.lastTouchX, this.lastTouchY);
         } else if (var3 == 0) {
            this.didPressButton(true, false);
         }
      }

   }

   private void didPressButton(boolean var1, boolean var2) {
      int var3 = this.buttonState;
      byte var4 = 2;
      if (var3 != 0 || this.drawVideoImageButton && !var2) {
         int var13;
         if (this.buttonState != 1 || this.drawVideoImageButton && !var2) {
            var13 = this.buttonState;
            if (var13 == 2) {
               var13 = this.documentAttachType;
               if (var13 != 3 && var13 != 5) {
                  if (this.currentMessageObject.isRoundVideo()) {
                     MessageObject var18 = MediaController.getInstance().getPlayingMessageObject();
                     if (var18 == null || !var18.isRoundVideo()) {
                        this.photoImage.setAllowStartAnimation(true);
                        this.photoImage.startAnimation();
                     }
                  } else {
                     this.photoImage.setAllowStartAnimation(true);
                     this.photoImage.startAnimation();
                  }

                  this.currentMessageObject.gifState = 0.0F;
                  this.buttonState = -1;
                  this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
               } else {
                  this.radialProgress.setProgress(0.0F, false);
                  FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.currentMessageObject, 1, 0);
                  this.buttonState = 4;
                  this.radialProgress.setIcon(this.getIconForCurrentState(), true, var1);
                  this.invalidate();
               }
            } else if (var13 == 3 || var13 == 0 && this.drawVideoImageButton) {
               if (this.hasMiniProgress == 2 && this.miniButtonState != 1) {
                  this.miniButtonState = 1;
                  this.radialProgress.setProgress(0.0F, false);
                  this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), false, var1);
               }

               this.delegate.didPressImage(this, 0.0F, 0.0F);
            } else if (this.buttonState == 4) {
               var13 = this.documentAttachType;
               if (var13 == 3 || var13 == 5) {
                  if ((!this.currentMessageObject.isOut() || !this.currentMessageObject.isSending() && !this.currentMessageObject.isEditing()) && !this.currentMessageObject.isSendError()) {
                     FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
                     this.buttonState = 2;
                     this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
                     this.invalidate();
                  } else {
                     ChatMessageCell.ChatMessageCellDelegate var19 = this.delegate;
                     if (var19 != null) {
                        var19.didPressCancelSendButton(this);
                     }
                  }
               }
            }
         } else {
            this.photoImage.setForceLoading(false);
            var13 = this.documentAttachType;
            if (var13 != 3 && var13 != 5) {
               if (!this.currentMessageObject.isOut() || !this.currentMessageObject.isSending() && !this.currentMessageObject.isEditing()) {
                  this.cancelLoading = true;
                  var13 = this.documentAttachType;
                  if (var13 != 2 && var13 != 4 && var13 != 1 && var13 != 8) {
                     var13 = this.currentMessageObject.type;
                     if (var13 != 0 && var13 != 1 && var13 != 8 && var13 != 5) {
                        if (var13 == 9) {
                           FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.messageOwner.media.document);
                        }
                     } else {
                        ImageLoader.getInstance().cancelForceLoadingForImageReceiver(this.photoImage);
                        this.photoImage.cancelLoadImage();
                     }
                  } else {
                     FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
                  }

                  this.buttonState = 0;
                  if (var2) {
                     this.videoRadialProgress.setIcon(2, false, var1);
                  } else {
                     this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
                  }

                  this.invalidate();
               } else if (this.radialProgress.getIcon() != 6) {
                  this.delegate.didPressCancelSendButton(this);
               }
            } else if (MediaController.getInstance().pauseMessage(this.currentMessageObject)) {
               this.buttonState = 0;
               this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
               this.invalidate();
            }
         }
      } else {
         var3 = this.documentAttachType;
         if (var3 != 3 && var3 != 5) {
            this.cancelLoading = false;
            if (var2) {
               this.videoRadialProgress.setProgress(0.0F, false);
            } else {
               this.radialProgress.setProgress(0.0F, false);
            }

            TLRPC.PhotoSize var5;
            String var6;
            if (this.currentPhotoObject != null && (this.photoImage.hasNotThumb() || this.currentPhotoObjectThumb == null)) {
               var5 = this.currentPhotoObject;
               if (!(var5 instanceof TLRPC.TL_photoStrippedSize) && !"s".equals(var5.type)) {
                  var6 = this.currentPhotoFilter;
               } else {
                  var6 = this.currentPhotoFilterThumb;
               }
            } else {
               var5 = this.currentPhotoObjectThumb;
               var6 = this.currentPhotoFilterThumb;
            }

            MessageObject var7 = this.currentMessageObject;
            var3 = var7.type;
            if (var3 == 1) {
               this.photoImage.setForceLoading(true);
               ImageReceiver var17 = this.photoImage;
               ImageLocation var8 = ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject);
               String var9 = this.currentPhotoFilter;
               ImageLocation var10 = ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject);
               String var14 = this.currentPhotoFilterThumb;
               var3 = this.currentPhotoObject.size;
               var7 = this.currentMessageObject;
               if (var7.shouldEncryptPhotoOrVideo()) {
                  var4 = 2;
               } else {
                  var4 = 0;
               }

               var17.setImage(var8, var9, var10, var14, var3, (String)null, var7, var4);
            } else if (var3 == 8) {
               FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.currentMessageObject, 1, 0);
            } else {
               TLRPC.Document var16;
               if (var7.isRoundVideo()) {
                  if (this.currentMessageObject.isSecretMedia()) {
                     FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 1);
                  } else {
                     var7 = this.currentMessageObject;
                     var7.gifState = 2.0F;
                     var16 = var7.getDocument();
                     this.photoImage.setForceLoading(true);
                     this.photoImage.setImage(ImageLocation.getForDocument(var16), (String)null, ImageLocation.getForObject(var5, var16), var6, var16.size, (String)null, this.currentMessageObject, 0);
                  }
               } else {
                  int var11 = this.currentMessageObject.type;
                  if (var11 == 9) {
                     FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.currentMessageObject, 0, 0);
                  } else {
                     var3 = this.documentAttachType;
                     if (var3 == 4) {
                        FileLoader var15 = FileLoader.getInstance(this.currentAccount);
                        var16 = this.documentAttach;
                        MessageObject var12 = this.currentMessageObject;
                        if (!var12.shouldEncryptPhotoOrVideo()) {
                           var4 = 0;
                        }

                        var15.loadFile(var16, var12, 1, var4);
                     } else if (var11 == 0 && var3 != 0) {
                        if (var3 == 2) {
                           this.photoImage.setForceLoading(true);
                           this.photoImage.setImage(ImageLocation.getForDocument(this.documentAttach), (String)null, ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), this.currentPhotoFilterThumb, this.documentAttach.size, (String)null, this.currentMessageObject, 0);
                           this.currentMessageObject.gifState = 2.0F;
                        } else if (var3 == 1) {
                           FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.currentMessageObject, 0, 0);
                        } else if (var3 == 8) {
                           this.photoImage.setImage(ImageLocation.getForDocument(this.documentAttach), this.currentPhotoFilter, ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), "b1", 0, "jpg", this.currentMessageObject, 1);
                        }
                     } else {
                        this.photoImage.setForceLoading(true);
                        this.photoImage.setImage(ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject), this.currentPhotoFilter, ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject), this.currentPhotoFilterThumb, 0, (String)null, this.currentMessageObject, 0);
                     }
                  }
               }
            }

            this.buttonState = 1;
            if (var2) {
               this.videoRadialProgress.setIcon(14, false, var1);
            } else {
               this.radialProgress.setIcon(this.getIconForCurrentState(), false, var1);
            }

            this.invalidate();
         } else {
            if (this.miniButtonState == 0) {
               FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.currentMessageObject, 1, 0);
            }

            if (this.delegate.needPlayMessage(this.currentMessageObject)) {
               if (this.hasMiniProgress == 2 && this.miniButtonState != 1) {
                  this.miniButtonState = 1;
                  this.radialProgress.setProgress(0.0F, false);
                  this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), false, true);
               }

               this.updatePlayingMessageProgress();
               this.buttonState = 1;
               this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
               this.invalidate();
            }
         }
      }

   }

   private void didPressMiniButton(boolean var1) {
      int var2 = this.miniButtonState;
      if (var2 == 0) {
         this.miniButtonState = 1;
         this.radialProgress.setProgress(0.0F, false);
         var2 = this.documentAttachType;
         if (var2 != 3 && var2 != 5) {
            if (var2 == 4) {
               FileLoader var3 = FileLoader.getInstance(this.currentAccount);
               TLRPC.Document var4 = this.documentAttach;
               MessageObject var5 = this.currentMessageObject;
               byte var6;
               if (var5.shouldEncryptPhotoOrVideo()) {
                  var6 = 2;
               } else {
                  var6 = 0;
               }

               var3.loadFile(var4, var5, 1, var6);
            }
         } else {
            FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.currentMessageObject, 1, 0);
         }

         this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), false, true);
         this.invalidate();
      } else if (var2 == 1) {
         var2 = this.documentAttachType;
         if ((var2 == 3 || var2 == 5) && MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
            MediaController.getInstance().cleanupPlayer(true, true);
         }

         this.miniButtonState = 0;
         FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
         this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), false, true);
         this.invalidate();
      }

   }

   private void drawContent(Canvas var1) {
      boolean var2 = this.needNewVisiblePart;
      boolean var3 = false;
      int var5;
      if (var2 && this.currentMessageObject.type == 0) {
         this.getLocalVisibleRect(this.scrollRect);
         Rect var4 = this.scrollRect;
         var5 = var4.top;
         this.setVisiblePart(var5, var4.bottom - var5);
         this.needNewVisiblePart = false;
      }

      if (this.currentMessagesGroup != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.forceNotDrawTime = var2;
      ImageReceiver var24 = this.photoImage;
      if (!PhotoViewer.isShowingImage(this.currentMessageObject) && !SecretMediaViewer.getInstance().isShowingImage(this.currentMessageObject)) {
         var2 = true;
      } else {
         var2 = false;
      }

      var24.setVisible(var2, false);
      if (!this.photoImage.getVisible()) {
         this.mediaWasInvisible = true;
         this.timeWasInvisible = true;
         var5 = this.animatingNoSound;
         if (var5 == 1) {
            this.animatingNoSoundProgress = 0.0F;
            this.animatingNoSound = 0;
         } else if (var5 == 2) {
            this.animatingNoSoundProgress = 1.0F;
            this.animatingNoSound = 0;
         }
      } else if (this.groupPhotoInvisible) {
         this.timeWasInvisible = true;
      } else if (this.mediaWasInvisible || this.timeWasInvisible) {
         if (this.mediaWasInvisible) {
            this.controlsAlpha = 0.0F;
            this.mediaWasInvisible = false;
         }

         if (this.timeWasInvisible) {
            this.timeAlpha = 0.0F;
            this.timeWasInvisible = false;
         }

         this.lastControlsAlphaChangeTime = System.currentTimeMillis();
         this.totalChangeTime = 0L;
      }

      this.radialProgress.setProgressColor(Theme.getColor("chat_mediaProgress"));
      this.videoRadialProgress.setProgressColor(Theme.getColor("chat_mediaProgress"));
      MessageObject var25 = this.currentMessageObject;
      var5 = var25.type;
      float var6 = 11.0F;
      float var7;
      int var8;
      int var9;
      Paint var10;
      int var11;
      int var12;
      int var13;
      StaticLayout var26;
      String var29;
      byte var30;
      TextPaint var32;
      Drawable var34;
      if (var5 == 0) {
         if (var25.isOutOwner()) {
            this.textX = this.currentBackgroundDrawable.getBounds().left + AndroidUtilities.dp(11.0F);
         } else {
            var5 = this.currentBackgroundDrawable.getBounds().left;
            if (!this.mediaBackground && this.drawPinnedBottom) {
               var7 = 11.0F;
            } else {
               var7 = 17.0F;
            }

            this.textX = var5 + AndroidUtilities.dp(var7);
         }

         if (this.hasGamePreview) {
            this.textX += AndroidUtilities.dp(11.0F);
            this.textY = AndroidUtilities.dp(14.0F) + this.namesOffset;
            var26 = this.siteNameLayout;
            if (var26 != null) {
               this.textY += var26.getLineBottom(var26.getLineCount() - 1);
            }
         } else if (this.hasInvoicePreview) {
            this.textY = AndroidUtilities.dp(14.0F) + this.namesOffset;
            var26 = this.siteNameLayout;
            if (var26 != null) {
               this.textY += var26.getLineBottom(var26.getLineCount() - 1);
            }
         } else {
            this.textY = AndroidUtilities.dp(10.0F) + this.namesOffset;
         }

         this.unmovedTextX = this.textX;
         if (this.currentMessageObject.textXOffset != 0.0F && this.replyNameLayout != null) {
            var8 = this.backgroundWidth;
            var5 = AndroidUtilities.dp(31.0F);
            var25 = this.currentMessageObject;
            var8 = var8 - var5 - var25.textWidth;
            var5 = var8;
            if (!this.hasNewLineForTime) {
               var9 = this.timeWidth;
               if (var25.isOutOwner()) {
                  var30 = 20;
               } else {
                  var30 = 0;
               }

               var5 = var8 - (var9 + AndroidUtilities.dp((float)(var30 + 4)));
            }

            if (var5 > 0) {
               this.textX += var5;
            }
         }

         ArrayList var27 = this.currentMessageObject.textLayoutBlocks;
         if (var27 != null && !var27.isEmpty()) {
            if (this.fullyDraw) {
               this.firstVisibleBlockNum = 0;
               this.lastVisibleBlockNum = this.currentMessageObject.textLayoutBlocks.size();
            }

            var5 = this.firstVisibleBlockNum;
            if (var5 >= 0) {
               while(var5 <= this.lastVisibleBlockNum && var5 < this.currentMessageObject.textLayoutBlocks.size()) {
                  MessageObject.TextLayoutBlock var28 = (MessageObject.TextLayoutBlock)this.currentMessageObject.textLayoutBlocks.get(var5);
                  var1.save();
                  var9 = this.textX;
                  if (var28.isRtl()) {
                     var8 = (int)Math.ceil((double)this.currentMessageObject.textXOffset);
                  } else {
                     var8 = 0;
                  }

                  var1.translate((float)(var9 - var8), (float)this.textY + var28.textYOffset);
                  if (this.pressedLink != null && var5 == this.linkBlockNum) {
                     for(var8 = 0; var8 < this.urlPath.size(); ++var8) {
                        var1.drawPath((Path)this.urlPath.get(var8), Theme.chat_urlPaint);
                     }
                  }

                  if (var5 == this.linkSelectionBlockNum && !this.urlPathSelection.isEmpty()) {
                     for(var8 = 0; var8 < this.urlPathSelection.size(); ++var8) {
                        var1.drawPath((Path)this.urlPathSelection.get(var8), Theme.chat_textSearchSelectionPaint);
                     }
                  }

                  try {
                     var28.textLayout.draw(var1);
                  } catch (Exception var23) {
                     FileLog.e((Throwable)var23);
                  }

                  var1.restore();
                  ++var5;
               }
            }
         }

         label1226: {
            if (!this.hasLinkPreview && !this.hasGamePreview) {
               var2 = var3;
               if (!this.hasInvoicePreview) {
                  break label1226;
               }
            }

            if (this.hasGamePreview) {
               var5 = AndroidUtilities.dp(14.0F) + this.namesOffset;
               var8 = this.unmovedTextX - AndroidUtilities.dp(10.0F);
            } else {
               if (this.hasInvoicePreview) {
                  var5 = AndroidUtilities.dp(14.0F) + this.namesOffset;
                  var9 = this.unmovedTextX;
                  var8 = AndroidUtilities.dp(1.0F);
               } else {
                  var5 = this.textY + this.currentMessageObject.textHeight + AndroidUtilities.dp(8.0F);
                  var9 = this.unmovedTextX;
                  var8 = AndroidUtilities.dp(1.0F);
               }

               var8 += var9;
            }

            if (!this.hasInvoicePreview) {
               var10 = Theme.chat_replyLinePaint;
               if (this.currentMessageObject.isOutOwner()) {
                  var29 = "chat_outPreviewLine";
               } else {
                  var29 = "chat_inPreviewLine";
               }

               var10.setColor(Theme.getColor(var29));
               var1.drawRect((float)var8, (float)(var5 - AndroidUtilities.dp(3.0F)), (float)(AndroidUtilities.dp(2.0F) + var8), (float)(this.linkPreviewHeight + var5 + AndroidUtilities.dp(3.0F)), Theme.chat_replyLinePaint);
            }

            var11 = var8;
            if (this.siteNameLayout != null) {
               var32 = Theme.chat_replyNamePaint;
               if (this.currentMessageObject.isOutOwner()) {
                  var29 = "chat_outSiteNameText";
               } else {
                  var29 = "chat_inSiteNameText";
               }

               var32.setColor(Theme.getColor(var29));
               var1.save();
               if (this.siteNameRtl) {
                  var8 = this.backgroundWidth - this.siteNameWidth - AndroidUtilities.dp(32.0F);
               } else if (this.hasInvoicePreview) {
                  var8 = 0;
               } else {
                  var8 = AndroidUtilities.dp(10.0F);
               }

               var1.translate((float)(var11 + var8), (float)(var5 - AndroidUtilities.dp(3.0F)));
               this.siteNameLayout.draw(var1);
               var1.restore();
               var26 = this.siteNameLayout;
               var9 = var26.getLineBottom(var26.getLineCount() - 1) + var5;
            } else {
               var9 = var5;
            }

            label1056: {
               if (!this.hasGamePreview) {
                  var12 = var5;
                  var8 = var9;
                  if (!this.hasInvoicePreview) {
                     break label1056;
                  }
               }

               var13 = this.currentMessageObject.textHeight;
               var12 = var5;
               var8 = var9;
               if (var13 != 0) {
                  var12 = var5 + var13 + AndroidUtilities.dp(4.0F);
                  var8 = var9 + this.currentMessageObject.textHeight + AndroidUtilities.dp(4.0F);
               }
            }

            RadialProgress2 var31;
            if ((!this.drawPhotoImage || !this.drawInstantView) && (this.drawInstantViewType != 6 || this.imageBackgroundColor == 0)) {
               var3 = false;
               var13 = var8;
            } else {
               var5 = var8;
               if (var8 != var12) {
                  var5 = var8 + AndroidUtilities.dp(2.0F);
               }

               if (this.imageBackgroundSideColor != 0) {
                  var8 = var11 + AndroidUtilities.dp(10.0F);
                  var24 = this.photoImage;
                  var24.setImageCoords((this.imageBackgroundSideWidth - var24.getImageWidth()) / 2 + var8, var5, this.photoImage.getImageWidth(), this.photoImage.getImageHeight());
                  this.rect.set((float)var8, (float)this.photoImage.getImageY(), (float)(var8 + this.imageBackgroundSideWidth), (float)this.photoImage.getImageY2());
                  Theme.chat_instantViewPaint.setColor(this.imageBackgroundSideColor);
                  var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), Theme.chat_instantViewPaint);
               } else {
                  this.photoImage.setImageCoords(var11 + AndroidUtilities.dp(10.0F), var5, this.photoImage.getImageWidth(), this.photoImage.getImageHeight());
               }

               var8 = this.imageBackgroundColor;
               if (var8 != 0) {
                  Theme.chat_instantViewPaint.setColor(var8);
                  this.rect.set((float)this.photoImage.getImageX(), (float)this.photoImage.getImageY(), (float)this.photoImage.getImageX2(), (float)this.photoImage.getImageY2());
                  if (this.imageBackgroundSideColor != 0) {
                     var1.drawRect((float)this.photoImage.getImageX(), (float)this.photoImage.getImageY(), (float)this.photoImage.getImageX2(), (float)this.photoImage.getImageY2(), Theme.chat_instantViewPaint);
                  } else {
                     var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), Theme.chat_instantViewPaint);
                  }
               }

               if (this.drawPhotoImage && this.drawInstantView) {
                  if (this.drawImageButton) {
                     var8 = AndroidUtilities.dp(48.0F);
                     this.buttonX = (int)((float)this.photoImage.getImageX() + (float)(this.photoImage.getImageWidth() - var8) / 2.0F);
                     this.buttonY = (int)((float)this.photoImage.getImageY() + (float)(this.photoImage.getImageHeight() - var8) / 2.0F);
                     var31 = this.radialProgress;
                     var9 = this.buttonX;
                     var13 = this.buttonY;
                     var31.setProgressRect(var9, var13, var9 + var8, var8 + var13);
                  }

                  var2 = this.photoImage.draw(var1);
               } else {
                  var2 = false;
               }

               var8 = this.photoImage.getImageHeight();
               var9 = AndroidUtilities.dp(6.0F);
               var13 = var5 + var8 + var9;
               var3 = var2;
            }

            if (this.currentMessageObject.isOutOwner()) {
               Theme.chat_replyNamePaint.setColor(Theme.getColor("chat_messageTextOut"));
               Theme.chat_replyTextPaint.setColor(Theme.getColor("chat_messageTextOut"));
            } else {
               Theme.chat_replyNamePaint.setColor(Theme.getColor("chat_messageTextIn"));
               Theme.chat_replyTextPaint.setColor(Theme.getColor("chat_messageTextIn"));
            }

            if (this.titleLayout != null) {
               var5 = var13;
               if (var13 != var12) {
                  var5 = var13 + AndroidUtilities.dp(2.0F);
               }

               var8 = var5 - AndroidUtilities.dp(1.0F);
               var1.save();
               var1.translate((float)(var11 + AndroidUtilities.dp(10.0F) + this.titleX), (float)(var5 - AndroidUtilities.dp(3.0F)));
               this.titleLayout.draw(var1);
               var1.restore();
               var26 = this.titleLayout;
               var13 = var5 + var26.getLineBottom(var26.getLineCount() - 1);
            } else {
               var8 = 0;
            }

            var9 = var13;
            var5 = var8;
            if (this.authorLayout != null) {
               var9 = var13;
               if (var13 != var12) {
                  var9 = var13 + AndroidUtilities.dp(2.0F);
               }

               var5 = var8;
               if (var8 == 0) {
                  var5 = var9 - AndroidUtilities.dp(1.0F);
               }

               var1.save();
               var1.translate((float)(var11 + AndroidUtilities.dp(10.0F) + this.authorX), (float)(var9 - AndroidUtilities.dp(3.0F)));
               this.authorLayout.draw(var1);
               var1.restore();
               var26 = this.authorLayout;
               var9 += var26.getLineBottom(var26.getLineCount() - 1);
            }

            var8 = var9;
            var13 = var5;
            if (this.descriptionLayout != null) {
               var13 = var9;
               if (var9 != var12) {
                  var13 = var9 + AndroidUtilities.dp(2.0F);
               }

               var8 = var5;
               if (var5 == 0) {
                  var8 = var13 - AndroidUtilities.dp(1.0F);
               }

               this.descriptionY = var13 - AndroidUtilities.dp(3.0F);
               var1.save();
               if (this.hasInvoicePreview) {
                  var5 = 0;
               } else {
                  var5 = AndroidUtilities.dp(10.0F);
               }

               var1.translate((float)(var11 + var5 + this.descriptionX), (float)this.descriptionY);
               if (this.pressedLink != null && this.linkBlockNum == -10) {
                  for(var5 = 0; var5 < this.urlPath.size(); ++var5) {
                     var1.drawPath((Path)this.urlPath.get(var5), Theme.chat_urlPaint);
                  }
               }

               this.descriptionLayout.draw(var1);
               var1.restore();
               var26 = this.descriptionLayout;
               var5 = var13 + var26.getLineBottom(var26.getLineCount() - 1);
               var13 = var8;
               var8 = var5;
            }

            var5 = var8;
            var2 = var3;
            if (this.drawPhotoImage) {
               var5 = var8;
               var2 = var3;
               if (!this.drawInstantView) {
                  var5 = var8;
                  if (var8 != var12) {
                     var5 = var8 + AndroidUtilities.dp(2.0F);
                  }

                  if (this.isSmallImage) {
                     this.photoImage.setImageCoords(var11 + this.backgroundWidth - AndroidUtilities.dp(81.0F), var13, this.photoImage.getImageWidth(), this.photoImage.getImageHeight());
                  } else {
                     var24 = this.photoImage;
                     if (this.hasInvoicePreview) {
                        var8 = -AndroidUtilities.dp(6.3F);
                     } else {
                        var8 = AndroidUtilities.dp(10.0F);
                     }

                     var24.setImageCoords(var11 + var8, var5, this.photoImage.getImageWidth(), this.photoImage.getImageHeight());
                     if (this.drawImageButton) {
                        var8 = AndroidUtilities.dp(48.0F);
                        this.buttonX = (int)((float)this.photoImage.getImageX() + (float)(this.photoImage.getImageWidth() - var8) / 2.0F);
                        this.buttonY = (int)((float)this.photoImage.getImageY() + (float)(this.photoImage.getImageHeight() - var8) / 2.0F);
                        var31 = this.radialProgress;
                        var9 = this.buttonX;
                        var13 = this.buttonY;
                        var31.setProgressRect(var9, var13, var9 + var8, var8 + var13);
                     }
                  }

                  if (this.currentMessageObject.isRoundVideo() && MediaController.getInstance().isPlayingMessage(this.currentMessageObject) && MediaController.getInstance().isVideoDrawingReady()) {
                     this.drawTime = true;
                     var2 = true;
                  } else {
                     var2 = this.photoImage.draw(var1);
                  }
               }
            }

            if (this.documentAttachType == 4) {
               this.videoButtonX = this.photoImage.getImageX() + AndroidUtilities.dp(8.0F);
               this.videoButtonY = this.photoImage.getImageY() + AndroidUtilities.dp(8.0F);
               var31 = this.videoRadialProgress;
               var8 = this.videoButtonX;
               var31.setProgressRect(var8, this.videoButtonY, AndroidUtilities.dp(24.0F) + var8, this.videoButtonY + AndroidUtilities.dp(24.0F));
            }

            if (this.photosCountLayout != null && this.photoImage.getVisible()) {
               var13 = this.photoImage.getImageX() + this.photoImage.getImageWidth() - AndroidUtilities.dp(8.0F) - this.photosCountWidth;
               var8 = this.photoImage.getImageY() + this.photoImage.getImageHeight() - AndroidUtilities.dp(19.0F);
               this.rect.set((float)(var13 - AndroidUtilities.dp(4.0F)), (float)(var8 - AndroidUtilities.dp(1.5F)), (float)(this.photosCountWidth + var13 + AndroidUtilities.dp(4.0F)), (float)(var8 + AndroidUtilities.dp(14.5F)));
               var9 = Theme.chat_timeBackgroundPaint.getAlpha();
               Theme.chat_timeBackgroundPaint.setAlpha((int)((float)var9 * this.controlsAlpha));
               Theme.chat_durationPaint.setAlpha((int)(this.controlsAlpha * 255.0F));
               var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), Theme.chat_timeBackgroundPaint);
               Theme.chat_timeBackgroundPaint.setAlpha(var9);
               var1.save();
               var1.translate((float)var13, (float)var8);
               this.photosCountLayout.draw(var1);
               var1.restore();
               Theme.chat_durationPaint.setAlpha(255);
            }

            if (this.videoInfoLayout != null && (!this.drawPhotoImage || this.photoImage.getVisible()) && this.imageBackgroundSideColor == 0) {
               if (!this.hasGamePreview && !this.hasInvoicePreview && this.documentAttachType != 8) {
                  var8 = this.photoImage.getImageX() + this.photoImage.getImageWidth() - AndroidUtilities.dp(8.0F) - this.durationWidth;
                  var5 = this.photoImage.getImageY() + this.photoImage.getImageHeight() - AndroidUtilities.dp(19.0F);
                  this.rect.set((float)(var8 - AndroidUtilities.dp(4.0F)), (float)(var5 - AndroidUtilities.dp(1.5F)), (float)(this.durationWidth + var8 + AndroidUtilities.dp(4.0F)), (float)(AndroidUtilities.dp(14.5F) + var5));
                  var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), Theme.chat_timeBackgroundPaint);
               } else if (this.drawPhotoImage) {
                  var8 = this.photoImage.getImageX() + AndroidUtilities.dp(8.5F);
                  var5 = this.photoImage.getImageY() + AndroidUtilities.dp(6.0F);
                  if (this.documentAttachType == 8) {
                     var7 = 14.5F;
                  } else {
                     var7 = 16.5F;
                  }

                  var9 = AndroidUtilities.dp(var7);
                  this.rect.set((float)(var8 - AndroidUtilities.dp(4.0F)), (float)(var5 - AndroidUtilities.dp(1.5F)), (float)(this.durationWidth + var8 + AndroidUtilities.dp(4.0F)), (float)(var9 + var5));
                  var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), Theme.chat_timeBackgroundPaint);
               } else {
                  var8 = var11;
               }

               var1.save();
               var1.translate((float)var8, (float)var5);
               if (this.hasInvoicePreview) {
                  if (this.drawPhotoImage) {
                     Theme.chat_shipmentPaint.setColor(Theme.getColor("chat_previewGameText"));
                  } else if (this.currentMessageObject.isOutOwner()) {
                     Theme.chat_shipmentPaint.setColor(Theme.getColor("chat_messageTextOut"));
                  } else {
                     Theme.chat_shipmentPaint.setColor(Theme.getColor("chat_messageTextIn"));
                  }
               }

               this.videoInfoLayout.draw(var1);
               var1.restore();
            }

            if (this.drawInstantView) {
               var5 = var12 + this.linkPreviewHeight + AndroidUtilities.dp(10.0F);
               var10 = Theme.chat_instantViewRectPaint;
               if (this.currentMessageObject.isOutOwner()) {
                  var34 = Theme.chat_msgOutInstantDrawable;
                  Theme.chat_instantViewPaint.setColor(Theme.getColor("chat_outPreviewInstantText"));
                  var10.setColor(Theme.getColor("chat_outPreviewInstantText"));
               } else {
                  var34 = Theme.chat_msgInInstantDrawable;
                  Theme.chat_instantViewPaint.setColor(Theme.getColor("chat_inPreviewInstantText"));
                  var10.setColor(Theme.getColor("chat_inPreviewInstantText"));
               }

               if (VERSION.SDK_INT >= 21) {
                  this.selectorDrawable.setBounds(var11, var5, this.instantWidth + var11, AndroidUtilities.dp(36.0F) + var5);
                  this.selectorDrawable.draw(var1);
               }

               this.rect.set((float)var11, (float)var5, (float)(this.instantWidth + var11), (float)(AndroidUtilities.dp(36.0F) + var5));
               var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(6.0F), (float)AndroidUtilities.dp(6.0F), var10);
               if (this.drawInstantViewType == 0) {
                  BaseCell.setDrawableBounds(var34, this.instantTextLeftX + this.instantTextX + var11 - AndroidUtilities.dp(15.0F), AndroidUtilities.dp(11.5F) + var5, AndroidUtilities.dp(9.0F), AndroidUtilities.dp(13.0F));
                  var34.draw(var1);
               }

               if (this.instantViewLayout != null) {
                  var1.save();
                  var1.translate((float)(var11 + this.instantTextX), (float)(var5 + AndroidUtilities.dp(10.5F)));
                  this.instantViewLayout.draw(var1);
                  var1.restore();
               }
            }
         }

         this.drawTime = true;
      } else if (this.drawPhotoImage) {
         if (var25.isRoundVideo() && MediaController.getInstance().isPlayingMessage(this.currentMessageObject) && MediaController.getInstance().isVideoDrawingReady()) {
            this.drawTime = true;
            var2 = true;
         } else {
            if (this.currentMessageObject.type == 5 && Theme.chat_roundVideoShadow != null) {
               var5 = this.photoImage.getImageX() - AndroidUtilities.dp(3.0F);
               var8 = this.photoImage.getImageY() - AndroidUtilities.dp(2.0F);
               Theme.chat_roundVideoShadow.setAlpha(255);
               Theme.chat_roundVideoShadow.setBounds(var5, var8, AndroidUtilities.roundMessageSize + var5 + AndroidUtilities.dp(6.0F), AndroidUtilities.roundMessageSize + var8 + AndroidUtilities.dp(6.0F));
               Theme.chat_roundVideoShadow.draw(var1);
               if (!this.photoImage.hasBitmapImage() || this.photoImage.getCurrentAlpha() != 1.0F) {
                  var10 = Theme.chat_docBackPaint;
                  if (this.currentMessageObject.isOutOwner()) {
                     var29 = "chat_outBubble";
                  } else {
                     var29 = "chat_inBubble";
                  }

                  var10.setColor(Theme.getColor(var29));
                  var1.drawCircle(this.photoImage.getCenterX(), this.photoImage.getCenterY(), (float)(this.photoImage.getImageWidth() / 2), Theme.chat_docBackPaint);
               }
            }

            CheckBoxBase var37;
            label971: {
               var37 = this.photoCheckBox;
               if (var37 != null && (this.checkBoxVisible || var37.getProgress() != 0.0F || this.checkBoxAnimationInProgress)) {
                  MessageObject.GroupedMessages var38 = this.currentMessagesGroup;
                  if (var38 != null && var38.messages.size() > 1) {
                     var2 = true;
                     break label971;
                  }
               }

               var2 = false;
            }

            this.drawPhotoCheckBox = var2;
            if (this.drawPhotoCheckBox && (this.photoCheckBox.isChecked() || this.photoCheckBox.getProgress() != 0.0F || this.checkBoxAnimationInProgress)) {
               var10 = Theme.chat_replyLinePaint;
               if (this.currentMessageObject.isOutOwner()) {
                  var29 = "chat_outBubbleSelected";
               } else {
                  var29 = "chat_inBubbleSelected";
               }

               var10.setColor(Theme.getColor(var29));
               this.rect.set((float)this.photoImage.getImageX(), (float)this.photoImage.getImageY(), (float)this.photoImage.getImageX2(), (float)this.photoImage.getImageY2());
               var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), Theme.chat_replyLinePaint);
               this.photoImage.setSideClip((float)AndroidUtilities.dp(14.0F) * this.photoCheckBox.getProgress());
               if (this.checkBoxAnimationInProgress) {
                  this.photoCheckBox.setBackgroundAlpha(this.checkBoxAnimationProgress);
               } else {
                  var37 = this.photoCheckBox;
                  if (this.checkBoxVisible) {
                     var7 = 1.0F;
                  } else {
                     var7 = var37.getProgress();
                  }

                  var37.setBackgroundAlpha(var7);
               }
            } else {
               this.photoImage.setSideClip(0.0F);
            }

            var3 = this.photoImage.draw(var1);
            boolean var14 = this.drawTime;
            this.drawTime = this.photoImage.getVisible();
            var2 = var3;
            if (this.currentPosition != null) {
               var2 = var3;
               if (var14 != this.drawTime) {
                  ViewGroup var40 = (ViewGroup)this.getParent();
                  var2 = var3;
                  if (var40 != null) {
                     if (!this.currentPosition.last) {
                        var8 = var40.getChildCount();
                        var5 = 0;

                        while(true) {
                           var2 = var3;
                           if (var5 >= var8) {
                              break;
                           }

                           View var33 = var40.getChildAt(var5);
                           if (var33 != this && var33 instanceof ChatMessageCell) {
                              ChatMessageCell var35 = (ChatMessageCell)var33;
                              if (var35.getCurrentMessagesGroup() == this.currentMessagesGroup) {
                                 MessageObject.GroupedMessagePosition var15 = var35.getCurrentPosition();
                                 if (var15.last && var15.maxY == this.currentPosition.maxY && var35.timeX - AndroidUtilities.dp(4.0F) + var35.getLeft() < this.getRight()) {
                                    var35.groupPhotoInvisible = this.drawTime ^ true;
                                    var35.invalidate();
                                    var40.invalidate();
                                 }
                              }
                           }

                           ++var5;
                        }
                     } else {
                        var40.invalidate();
                        var2 = var3;
                     }
                  }
               }
            }
         }
      } else {
         var2 = false;
      }

      var5 = this.documentAttachType;
      if (var5 == 2) {
         if (this.photoImage.getVisible() && !this.hasGamePreview && !this.currentMessageObject.needDrawBluredPreview()) {
            var5 = ((BitmapDrawable)Theme.chat_msgMediaMenuDrawable).getPaint().getAlpha();
            Theme.chat_msgMediaMenuDrawable.setAlpha((int)((float)var5 * this.controlsAlpha));
            var34 = Theme.chat_msgMediaMenuDrawable;
            var8 = this.photoImage.getImageX() + this.photoImage.getImageWidth() - AndroidUtilities.dp(14.0F);
            this.otherX = var8;
            var9 = this.photoImage.getImageY() + AndroidUtilities.dp(8.1F);
            this.otherY = var9;
            BaseCell.setDrawableBounds(var34, var8, var9);
            Theme.chat_msgMediaMenuDrawable.draw(var1);
            Theme.chat_msgMediaMenuDrawable.setAlpha(var5);
         }
      } else if (var5 == 7) {
         if (this.durationLayout != null) {
            var3 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
            if (var3 && this.currentMessageObject.type == 5) {
               this.drawRoundProgress(var1);
               this.drawOverlays(var1);
            }

            var25 = this.currentMessageObject;
            if (var25.type == 5) {
               var8 = this.backgroundDrawableLeft + AndroidUtilities.dp(8.0F);
               var9 = this.layoutHeight;
               if (this.drawPinnedBottom) {
                  var30 = 2;
               } else {
                  var30 = 0;
               }

               var9 -= AndroidUtilities.dp((float)(28 - var30));
               this.rect.set((float)var8, (float)var9, (float)(this.timeWidthAudio + var8 + AndroidUtilities.dp(22.0F)), (float)(AndroidUtilities.dp(17.0F) + var9));
               var5 = Theme.chat_actionBackgroundPaint.getAlpha();
               Theme.chat_actionBackgroundPaint.setAlpha((int)((float)var5 * this.timeAlpha));
               var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), Theme.chat_actionBackgroundPaint);
               Theme.chat_actionBackgroundPaint.setAlpha(var5);
               if (!var3 && this.currentMessageObject.isContentUnread()) {
                  Theme.chat_docBackPaint.setColor(Theme.getColor("chat_mediaTimeText"));
                  Theme.chat_docBackPaint.setAlpha((int)(this.timeAlpha * 255.0F));
                  var1.drawCircle((float)(this.timeWidthAudio + var8 + AndroidUtilities.dp(12.0F)), (float)(AndroidUtilities.dp(8.3F) + var9), (float)AndroidUtilities.dp(3.0F), Theme.chat_docBackPaint);
               } else {
                  if (var3 && !MediaController.getInstance().isMessagePaused()) {
                     this.roundVideoPlayingDrawable.start();
                  } else {
                     this.roundVideoPlayingDrawable.stop();
                  }

                  BaseCell.setDrawableBounds(this.roundVideoPlayingDrawable, this.timeWidthAudio + var8 + AndroidUtilities.dp(6.0F), AndroidUtilities.dp(2.3F) + var9);
                  this.roundVideoPlayingDrawable.draw(var1);
               }

               var5 = var8 + AndroidUtilities.dp(4.0F);
               var8 = var9 + AndroidUtilities.dp(1.7F);
            } else {
               var5 = this.backgroundDrawableLeft;
               if (!var25.isOutOwner() && !this.drawPinnedBottom) {
                  var7 = 18.0F;
               } else {
                  var7 = 12.0F;
               }

               var9 = AndroidUtilities.dp(var7) + var5;
               var8 = this.layoutHeight;
               if (this.drawPinnedBottom) {
                  var30 = 2;
               } else {
                  var30 = 0;
               }

               var8 = var8 - AndroidUtilities.dp(6.3F - (float)var30) - this.timeLayout.getHeight();
               var5 = var9;
            }

            Theme.chat_timePaint.setAlpha((int)(this.timeAlpha * 255.0F));
            var1.save();
            var1.translate((float)var5, (float)var8);
            this.durationLayout.draw(var1);
            var1.restore();
            Theme.chat_timePaint.setAlpha(255);
         }
      } else {
         RadialProgress2 var36;
         if (var5 == 5) {
            if (this.currentMessageObject.isOutOwner()) {
               Theme.chat_audioTitlePaint.setColor(Theme.getColor("chat_outAudioTitleText"));
               var32 = Theme.chat_audioPerformerPaint;
               if (this.isDrawSelectionBackground()) {
                  var29 = "chat_outAudioPerfomerSelectedText";
               } else {
                  var29 = "chat_outAudioPerfomerText";
               }

               var32.setColor(Theme.getColor(var29));
               var32 = Theme.chat_audioTimePaint;
               if (this.isDrawSelectionBackground()) {
                  var29 = "chat_outAudioDurationSelectedText";
               } else {
                  var29 = "chat_outAudioDurationText";
               }

               var32.setColor(Theme.getColor(var29));
               var36 = this.radialProgress;
               if (!this.isDrawSelectionBackground() && this.buttonPressed == 0) {
                  var29 = "chat_outAudioProgress";
               } else {
                  var29 = "chat_outAudioSelectedProgress";
               }

               var36.setProgressColor(Theme.getColor(var29));
            } else {
               Theme.chat_audioTitlePaint.setColor(Theme.getColor("chat_inAudioTitleText"));
               var32 = Theme.chat_audioPerformerPaint;
               if (this.isDrawSelectionBackground()) {
                  var29 = "chat_inAudioPerfomerSelectedText";
               } else {
                  var29 = "chat_inAudioPerfomerText";
               }

               var32.setColor(Theme.getColor(var29));
               var32 = Theme.chat_audioTimePaint;
               if (this.isDrawSelectionBackground()) {
                  var29 = "chat_inAudioDurationSelectedText";
               } else {
                  var29 = "chat_inAudioDurationText";
               }

               var32.setColor(Theme.getColor(var29));
               var36 = this.radialProgress;
               if (!this.isDrawSelectionBackground() && this.buttonPressed == 0) {
                  var29 = "chat_inAudioProgress";
               } else {
                  var29 = "chat_inAudioSelectedProgress";
               }

               var36.setProgressColor(Theme.getColor(var29));
            }

            this.radialProgress.draw(var1);
            var1.save();
            var1.translate((float)(this.timeAudioX + this.songX), (float)(AndroidUtilities.dp(13.0F) + this.namesOffset + this.mediaOffsetY));
            this.songLayout.draw(var1);
            var1.restore();
            var1.save();
            if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
               var1.translate((float)this.seekBarX, (float)this.seekBarY);
               this.seekBar.draw(var1);
            } else {
               var1.translate((float)(this.timeAudioX + this.performerX), (float)(AndroidUtilities.dp(35.0F) + this.namesOffset + this.mediaOffsetY));
               this.performerLayout.draw(var1);
            }

            var1.restore();
            var1.save();
            var1.translate((float)this.timeAudioX, (float)(AndroidUtilities.dp(57.0F) + this.namesOffset + this.mediaOffsetY));
            this.durationLayout.draw(var1);
            var1.restore();
            if (this.currentMessageObject.isOutOwner()) {
               if (this.isDrawSelectionBackground()) {
                  var34 = Theme.chat_msgOutMenuSelectedDrawable;
               } else {
                  var34 = Theme.chat_msgOutMenuDrawable;
               }
            } else if (this.isDrawSelectionBackground()) {
               var34 = Theme.chat_msgInMenuSelectedDrawable;
            } else {
               var34 = Theme.chat_msgInMenuDrawable;
            }

            var5 = this.buttonX;
            var8 = this.backgroundWidth;
            if (this.currentMessageObject.type == 0) {
               var7 = 58.0F;
            } else {
               var7 = 48.0F;
            }

            var8 = var5 + var8 - AndroidUtilities.dp(var7);
            this.otherX = var8;
            var5 = this.buttonY - AndroidUtilities.dp(5.0F);
            this.otherY = var5;
            BaseCell.setDrawableBounds(var34, var8, var5);
            var34.draw(var1);
         } else if (var5 == 3) {
            if (this.currentMessageObject.isOutOwner()) {
               var32 = Theme.chat_audioTimePaint;
               if (this.isDrawSelectionBackground()) {
                  var29 = "chat_outAudioDurationSelectedText";
               } else {
                  var29 = "chat_outAudioDurationText";
               }

               var32.setColor(Theme.getColor(var29));
               var36 = this.radialProgress;
               if (!this.isDrawSelectionBackground() && this.buttonPressed == 0) {
                  var29 = "chat_outAudioProgress";
               } else {
                  var29 = "chat_outAudioSelectedProgress";
               }

               var36.setProgressColor(Theme.getColor(var29));
            } else {
               var32 = Theme.chat_audioTimePaint;
               if (this.isDrawSelectionBackground()) {
                  var29 = "chat_inAudioDurationSelectedText";
               } else {
                  var29 = "chat_inAudioDurationText";
               }

               var32.setColor(Theme.getColor(var29));
               var36 = this.radialProgress;
               if (!this.isDrawSelectionBackground() && this.buttonPressed == 0) {
                  var29 = "chat_inAudioProgress";
               } else {
                  var29 = "chat_inAudioSelectedProgress";
               }

               var36.setProgressColor(Theme.getColor(var29));
            }

            this.radialProgress.draw(var1);
            var1.save();
            if (this.useSeekBarWaweform) {
               var1.translate((float)(this.seekBarX + AndroidUtilities.dp(13.0F)), (float)this.seekBarY);
               this.seekBarWaveform.draw(var1);
            } else {
               var1.translate((float)this.seekBarX, (float)this.seekBarY);
               this.seekBar.draw(var1);
            }

            var1.restore();
            var1.save();
            var1.translate((float)this.timeAudioX, (float)(AndroidUtilities.dp(44.0F) + this.namesOffset + this.mediaOffsetY));
            this.durationLayout.draw(var1);
            var1.restore();
            var25 = this.currentMessageObject;
            if (var25.type != 0 && var25.isContentUnread()) {
               var10 = Theme.chat_docBackPaint;
               if (this.currentMessageObject.isOutOwner()) {
                  var29 = "chat_outVoiceSeekbarFill";
               } else {
                  var29 = "chat_inVoiceSeekbarFill";
               }

               var10.setColor(Theme.getColor(var29));
               var1.drawCircle((float)(this.timeAudioX + this.timeWidthAudio + AndroidUtilities.dp(6.0F)), (float)(AndroidUtilities.dp(51.0F) + this.namesOffset + this.mediaOffsetY), (float)AndroidUtilities.dp(3.0F), Theme.chat_docBackPaint);
            }
         }
      }

      if (this.captionLayout != null) {
         var25 = this.currentMessageObject;
         var5 = var25.type;
         if (var5 != 1 && this.documentAttachType != 4 && var5 != 8) {
            if (this.hasOldCaptionPreview) {
               var5 = this.backgroundDrawableLeft;
               if (var25.isOutOwner()) {
                  var7 = 11.0F;
               } else {
                  var7 = 17.0F;
               }

               this.captionX = var5 + AndroidUtilities.dp(var7) + this.captionOffsetX;
               var8 = this.totalHeight;
               var5 = this.captionHeight;
               if (this.drawPinnedTop) {
                  var7 = 9.0F;
               } else {
                  var7 = 10.0F;
               }

               this.captionY = var8 - var5 - AndroidUtilities.dp(var7) - this.linkPreviewHeight - AndroidUtilities.dp(17.0F);
            } else {
               label873: {
                  var5 = this.backgroundDrawableLeft;
                  if (!var25.isOutOwner()) {
                     var3 = this.mediaBackground;
                     if (!var3 && (var3 || !this.drawPinnedBottom)) {
                        var7 = 17.0F;
                        break label873;
                     }
                  }

                  var7 = 11.0F;
               }

               this.captionX = var5 + AndroidUtilities.dp(var7) + this.captionOffsetX;
               var5 = this.totalHeight;
               var8 = this.captionHeight;
               if (this.drawPinnedTop) {
                  var7 = 9.0F;
               } else {
                  var7 = 10.0F;
               }

               this.captionY = var5 - var8 - AndroidUtilities.dp(var7);
            }
         } else {
            this.captionX = this.photoImage.getImageX() + AndroidUtilities.dp(5.0F) + this.captionOffsetX;
            this.captionY = this.photoImage.getImageY() + this.photoImage.getImageHeight() + AndroidUtilities.dp(6.0F);
         }
      }

      if (this.currentPosition == null) {
         this.drawCaptionLayout(var1, false);
      }

      if (this.hasOldCaptionPreview) {
         var25 = this.currentMessageObject;
         var5 = var25.type;
         if (var5 != 1 && this.documentAttachType != 4 && var5 != 8) {
            var5 = this.backgroundDrawableLeft;
            if (var25.isOutOwner()) {
               var7 = var6;
            } else {
               var7 = 17.0F;
            }

            var8 = var5 + AndroidUtilities.dp(var7);
         } else {
            var5 = this.photoImage.getImageX();
            var8 = AndroidUtilities.dp(5.0F) + var5;
         }

         var5 = this.totalHeight;
         if (this.drawPinnedTop) {
            var7 = 9.0F;
         } else {
            var7 = 10.0F;
         }

         var13 = var5 - AndroidUtilities.dp(var7) - this.linkPreviewHeight - AndroidUtilities.dp(8.0F);
         var10 = Theme.chat_replyLinePaint;
         if (this.currentMessageObject.isOutOwner()) {
            var29 = "chat_outPreviewLine";
         } else {
            var29 = "chat_inPreviewLine";
         }

         var10.setColor(Theme.getColor(var29));
         var1.drawRect((float)var8, (float)(var13 - AndroidUtilities.dp(3.0F)), (float)(AndroidUtilities.dp(2.0F) + var8), (float)(this.linkPreviewHeight + var13), Theme.chat_replyLinePaint);
         if (this.siteNameLayout != null) {
            var32 = Theme.chat_replyNamePaint;
            if (this.currentMessageObject.isOutOwner()) {
               var29 = "chat_outSiteNameText";
            } else {
               var29 = "chat_inSiteNameText";
            }

            var32.setColor(Theme.getColor(var29));
            var1.save();
            if (this.siteNameRtl) {
               var5 = this.backgroundWidth - this.siteNameWidth - AndroidUtilities.dp(32.0F);
            } else if (this.hasInvoicePreview) {
               var5 = 0;
            } else {
               var5 = AndroidUtilities.dp(10.0F);
            }

            var1.translate((float)(var5 + var8), (float)(var13 - AndroidUtilities.dp(3.0F)));
            this.siteNameLayout.draw(var1);
            var1.restore();
            var26 = this.siteNameLayout;
            var5 = var26.getLineBottom(var26.getLineCount() - 1) + var13;
         } else {
            var5 = var13;
         }

         if (this.currentMessageObject.isOutOwner()) {
            Theme.chat_replyTextPaint.setColor(Theme.getColor("chat_messageTextOut"));
         } else {
            Theme.chat_replyTextPaint.setColor(Theme.getColor("chat_messageTextIn"));
         }

         if (this.descriptionLayout != null) {
            var9 = var5;
            if (var5 != var13) {
               var9 = var5 + AndroidUtilities.dp(2.0F);
            }

            this.descriptionY = var9 - AndroidUtilities.dp(3.0F);
            var1.save();
            var1.translate((float)(var8 + AndroidUtilities.dp(10.0F) + this.descriptionX), (float)this.descriptionY);
            this.descriptionLayout.draw(var1);
            var1.restore();
         }

         this.drawTime = true;
      }

      if (this.documentAttachType == 1) {
         if (this.currentMessageObject.isOutOwner()) {
            Theme.chat_docNamePaint.setColor(Theme.getColor("chat_outFileNameText"));
            var32 = Theme.chat_infoPaint;
            if (this.isDrawSelectionBackground()) {
               var29 = "chat_outFileInfoSelectedText";
            } else {
               var29 = "chat_outFileInfoText";
            }

            var32.setColor(Theme.getColor(var29));
            var10 = Theme.chat_docBackPaint;
            if (this.isDrawSelectionBackground()) {
               var29 = "chat_outFileBackgroundSelected";
            } else {
               var29 = "chat_outFileBackground";
            }

            var10.setColor(Theme.getColor(var29));
            if (this.isDrawSelectionBackground()) {
               var34 = Theme.chat_msgOutMenuSelectedDrawable;
            } else {
               var34 = Theme.chat_msgOutMenuDrawable;
            }
         } else {
            Theme.chat_docNamePaint.setColor(Theme.getColor("chat_inFileNameText"));
            var32 = Theme.chat_infoPaint;
            if (this.isDrawSelectionBackground()) {
               var29 = "chat_inFileInfoSelectedText";
            } else {
               var29 = "chat_inFileInfoText";
            }

            var32.setColor(Theme.getColor(var29));
            var10 = Theme.chat_docBackPaint;
            if (this.isDrawSelectionBackground()) {
               var29 = "chat_inFileBackgroundSelected";
            } else {
               var29 = "chat_inFileBackground";
            }

            var10.setColor(Theme.getColor(var29));
            if (this.isDrawSelectionBackground()) {
               var34 = Theme.chat_msgInMenuSelectedDrawable;
            } else {
               var34 = Theme.chat_msgInMenuDrawable;
            }
         }

         RadialProgress2 var39;
         StaticLayout var42;
         String var43;
         if (this.drawPhotoImage) {
            if (this.currentMessageObject.type == 0) {
               var8 = this.photoImage.getImageX() + this.backgroundWidth - AndroidUtilities.dp(56.0F);
               this.otherX = var8;
               var5 = this.photoImage.getImageY() + AndroidUtilities.dp(1.0F);
               this.otherY = var5;
               BaseCell.setDrawableBounds(var34, var8, var5);
            } else {
               var8 = this.photoImage.getImageX() + this.backgroundWidth - AndroidUtilities.dp(40.0F);
               this.otherX = var8;
               var5 = this.photoImage.getImageY() + AndroidUtilities.dp(1.0F);
               this.otherY = var5;
               BaseCell.setDrawableBounds(var34, var8, var5);
            }

            var11 = this.photoImage.getImageX() + this.photoImage.getImageWidth() + AndroidUtilities.dp(10.0F);
            var13 = this.photoImage.getImageY() + AndroidUtilities.dp(8.0F);
            var8 = this.photoImage.getImageY();
            var42 = this.docTitleLayout;
            if (var42 != null) {
               var5 = var42.getLineBottom(var42.getLineCount() - 1) + AndroidUtilities.dp(13.0F);
            } else {
               var5 = AndroidUtilities.dp(8.0F);
            }

            var12 = var8 + var5;
            if (!var2) {
               if (this.currentMessageObject.isOutOwner()) {
                  this.radialProgress.setColors("chat_outLoader", "chat_outLoaderSelected", "chat_outMediaIcon", "chat_outMediaIconSelected");
                  var39 = this.radialProgress;
                  if (this.isDrawSelectionBackground()) {
                     var43 = "chat_outFileProgressSelected";
                  } else {
                     var43 = "chat_outFileProgress";
                  }

                  var39.setProgressColor(Theme.getColor(var43));
                  this.videoRadialProgress.setColors("chat_outLoader", "chat_outLoaderSelected", "chat_outMediaIcon", "chat_outMediaIconSelected");
                  var39 = this.videoRadialProgress;
                  if (this.isDrawSelectionBackground()) {
                     var43 = "chat_outFileProgressSelected";
                  } else {
                     var43 = "chat_outFileProgress";
                  }

                  var39.setProgressColor(Theme.getColor(var43));
               } else {
                  this.radialProgress.setColors("chat_inLoader", "chat_inLoaderSelected", "chat_inMediaIcon", "chat_inMediaIconSelected");
                  var39 = this.radialProgress;
                  if (this.isDrawSelectionBackground()) {
                     var43 = "chat_inFileProgressSelected";
                  } else {
                     var43 = "chat_inFileProgress";
                  }

                  var39.setProgressColor(Theme.getColor(var43));
                  this.videoRadialProgress.setColors("chat_inLoader", "chat_inLoaderSelected", "chat_inMediaIcon", "chat_inMediaIconSelected");
                  var39 = this.videoRadialProgress;
                  if (this.isDrawSelectionBackground()) {
                     var43 = "chat_inFileProgressSelected";
                  } else {
                     var43 = "chat_inFileProgress";
                  }

                  var39.setProgressColor(Theme.getColor(var43));
               }

               this.rect.set((float)this.photoImage.getImageX(), (float)this.photoImage.getImageY(), (float)(this.photoImage.getImageX() + this.photoImage.getImageWidth()), (float)(this.photoImage.getImageY() + this.photoImage.getImageHeight()));
               var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(3.0F), (float)AndroidUtilities.dp(3.0F), Theme.chat_docBackPaint);
               var9 = var11;
               var8 = var13;
               var5 = var12;
            } else {
               this.radialProgress.setColors("chat_mediaLoaderPhoto", "chat_mediaLoaderPhotoSelected", "chat_mediaLoaderPhotoIcon", "chat_mediaLoaderPhotoIconSelected");
               this.radialProgress.setProgressColor(Theme.getColor("chat_mediaProgress"));
               this.videoRadialProgress.setColors("chat_mediaLoaderPhoto", "chat_mediaLoaderPhotoSelected", "chat_mediaLoaderPhotoIcon", "chat_mediaLoaderPhotoIconSelected");
               this.videoRadialProgress.setProgressColor(Theme.getColor("chat_mediaProgress"));
               var9 = var11;
               var8 = var13;
               var5 = var12;
               if (this.buttonState == -1) {
                  var9 = var11;
                  var8 = var13;
                  var5 = var12;
                  if (this.radialProgress.getIcon() != 4) {
                     this.radialProgress.setIcon(4, true, true);
                     var9 = var11;
                     var8 = var13;
                     var5 = var12;
                  }
               }
            }
         } else {
            var5 = this.buttonX;
            var8 = this.backgroundWidth;
            if (this.currentMessageObject.type == 0) {
               var7 = 58.0F;
            } else {
               var7 = 48.0F;
            }

            var5 = var5 + var8 - AndroidUtilities.dp(var7);
            this.otherX = var5;
            var8 = this.buttonY - AndroidUtilities.dp(5.0F);
            this.otherY = var8;
            BaseCell.setDrawableBounds(var34, var5, var8);
            var9 = this.buttonX + AndroidUtilities.dp(53.0F);
            var8 = this.buttonY + AndroidUtilities.dp(4.0F);
            var13 = this.buttonY + AndroidUtilities.dp(27.0F);
            var42 = this.docTitleLayout;
            var5 = var13;
            if (var42 != null) {
               var5 = var13;
               if (var42.getLineCount() > 1) {
                  var5 = var13 + (this.docTitleLayout.getLineCount() - 1) * AndroidUtilities.dp(16.0F) + AndroidUtilities.dp(2.0F);
               }
            }

            if (this.currentMessageObject.isOutOwner()) {
               var39 = this.radialProgress;
               if (!this.isDrawSelectionBackground() && this.buttonPressed == 0) {
                  var43 = "chat_outAudioProgress";
               } else {
                  var43 = "chat_outAudioSelectedProgress";
               }

               var39.setProgressColor(Theme.getColor(var43));
               var39 = this.videoRadialProgress;
               if (!this.isDrawSelectionBackground() && this.videoButtonPressed == 0) {
                  var43 = "chat_outAudioProgress";
               } else {
                  var43 = "chat_outAudioSelectedProgress";
               }

               var39.setProgressColor(Theme.getColor(var43));
            } else {
               var39 = this.radialProgress;
               if (!this.isDrawSelectionBackground() && this.buttonPressed == 0) {
                  var43 = "chat_inAudioProgress";
               } else {
                  var43 = "chat_inAudioSelectedProgress";
               }

               var39.setProgressColor(Theme.getColor(var43));
               var39 = this.videoRadialProgress;
               if (!this.isDrawSelectionBackground() && this.videoButtonPressed == 0) {
                  var43 = "chat_inAudioProgress";
               } else {
                  var43 = "chat_inAudioSelectedProgress";
               }

               var39.setProgressColor(Theme.getColor(var43));
            }
         }

         var34.draw(var1);

         try {
            if (this.docTitleLayout != null) {
               var1.save();
               var1.translate((float)(this.docTitleOffsetX + var9), (float)var8);
               this.docTitleLayout.draw(var1);
               var1.restore();
            }
         } catch (Exception var22) {
            FileLog.e((Throwable)var22);
         }

         try {
            if (this.infoLayout != null) {
               var1.save();
               var1.translate((float)var9, (float)var5);
               this.infoLayout.draw(var1);
               var1.restore();
            }
         } catch (Exception var21) {
            FileLog.e((Throwable)var21);
         }
      }

      long var16;
      long var18;
      if (this.buttonState == -1 && this.currentMessageObject.needDrawBluredPreview() && !MediaController.getInstance().isPlayingMessage(this.currentMessageObject) && this.photoImage.getVisible()) {
         var25 = this.currentMessageObject;
         if (var25.messageOwner.destroyTime != 0) {
            if (!var25.isOutOwner()) {
               var16 = System.currentTimeMillis();
               var18 = (long)(ConnectionsManager.getInstance(this.currentAccount).getTimeDifference() * 1000);
               var7 = (float)Math.max(0L, (long)this.currentMessageObject.messageOwner.destroyTime * 1000L - (var16 + var18)) / ((float)this.currentMessageObject.messageOwner.ttl * 1000.0F);
               Theme.chat_deleteProgressPaint.setAlpha((int)(this.controlsAlpha * 255.0F));
               var1.drawArc(this.deleteProgressRect, -90.0F, var7 * -360.0F, true, Theme.chat_deleteProgressPaint);
               if (var7 != 0.0F) {
                  var11 = AndroidUtilities.dp(2.0F);
                  RectF var46 = this.deleteProgressRect;
                  var13 = (int)var46.left;
                  var5 = (int)var46.top;
                  var8 = (int)var46.right;
                  var9 = var11 * 2;
                  this.invalidate(var13 - var11, var5 - var11, var8 + var9, (int)var46.bottom + var9);
               }
            }

            this.updateSecretTimeText(this.currentMessageObject);
         }
      }

      var25 = this.currentMessageObject;
      if (var25.type == 4 && !(var25.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive) && this.currentMapProvider == 2 && this.photoImage.hasNotThumb()) {
         var13 = (int)((float)Theme.chat_redLocationIcon.getIntrinsicWidth() * 0.8F);
         var8 = (int)((float)Theme.chat_redLocationIcon.getIntrinsicHeight() * 0.8F);
         var5 = this.photoImage.getImageX() + (this.photoImage.getImageWidth() - var13) / 2;
         var9 = this.photoImage.getImageY() + (this.photoImage.getImageHeight() / 2 - var8);
         Theme.chat_redLocationIcon.setAlpha((int)(this.photoImage.getCurrentAlpha() * 255.0F));
         Theme.chat_redLocationIcon.setBounds(var5, var9, var13 + var5, var8 + var9);
         Theme.chat_redLocationIcon.draw(var1);
      }

      if (!this.botButtons.isEmpty()) {
         if (this.currentMessageObject.isOutOwner()) {
            var5 = this.getMeasuredWidth() - this.widthForButtons - AndroidUtilities.dp(10.0F);
         } else {
            var5 = this.backgroundDrawableLeft;
            if (this.mediaBackground) {
               var7 = 1.0F;
            } else {
               var7 = 7.0F;
            }

            var5 += AndroidUtilities.dp(var7);
         }

         for(var8 = 0; var8 < this.botButtons.size(); ++var8) {
            ChatMessageCell.BotButton var41 = (ChatMessageCell.BotButton)this.botButtons.get(var8);
            var13 = var41.y + this.layoutHeight - AndroidUtilities.dp(2.0F);
            Drawable var45 = Theme.chat_systemDrawable;
            PorterDuffColorFilter var47;
            if (var8 == this.pressedBotButton) {
               var47 = Theme.colorPressedFilter;
            } else {
               var47 = Theme.colorFilter;
            }

            var45.setColorFilter(var47);
            Theme.chat_systemDrawable.setBounds(var41.x + var5, var13, var41.x + var5 + var41.width, var41.height + var13);
            Theme.chat_systemDrawable.draw(var1);
            var1.save();
            var1.translate((float)(var41.x + var5 + AndroidUtilities.dp(5.0F)), (float)((AndroidUtilities.dp(44.0F) - var41.title.getLineBottom(var41.title.getLineCount() - 1)) / 2 + var13));
            var41.title.draw(var1);
            var1.restore();
            int var20;
            if (var41.button instanceof TLRPC.TL_keyboardButtonUrl) {
               var11 = var41.x;
               var9 = var41.width;
               var12 = AndroidUtilities.dp(3.0F);
               var20 = Theme.chat_botLinkDrawalbe.getIntrinsicWidth();
               BaseCell.setDrawableBounds(Theme.chat_botLinkDrawalbe, var11 + var9 - var12 - var20 + var5, var13 + AndroidUtilities.dp(3.0F));
               Theme.chat_botLinkDrawalbe.draw(var1);
            } else if (var41.button instanceof TLRPC.TL_keyboardButtonSwitchInline) {
               var9 = var41.x;
               var11 = var41.width;
               var20 = AndroidUtilities.dp(3.0F);
               var12 = Theme.chat_botInlineDrawable.getIntrinsicWidth();
               BaseCell.setDrawableBounds(Theme.chat_botInlineDrawable, var9 + var11 - var20 - var12 + var5, var13 + AndroidUtilities.dp(3.0F));
               Theme.chat_botInlineDrawable.draw(var1);
            } else if (var41.button instanceof TLRPC.TL_keyboardButtonCallback || var41.button instanceof TLRPC.TL_keyboardButtonRequestGeoLocation || var41.button instanceof TLRPC.TL_keyboardButtonGame || var41.button instanceof TLRPC.TL_keyboardButtonBuy || var41.button instanceof TLRPC.TL_keyboardButtonUrlAuth) {
               boolean var44;
               if ((var41.button instanceof TLRPC.TL_keyboardButtonCallback || var41.button instanceof TLRPC.TL_keyboardButtonGame || var41.button instanceof TLRPC.TL_keyboardButtonBuy || var41.button instanceof TLRPC.TL_keyboardButtonUrlAuth) && SendMessagesHelper.getInstance(this.currentAccount).isSendingCallback(this.currentMessageObject, var41.button) || var41.button instanceof TLRPC.TL_keyboardButtonRequestGeoLocation && SendMessagesHelper.getInstance(this.currentAccount).isSendingCurrentLocation(this.currentMessageObject, var41.button)) {
                  var44 = true;
               } else {
                  var44 = false;
               }

               if (var44 || !var44 && var41.progressAlpha != 0.0F) {
                  Theme.chat_botProgressPaint.setAlpha(Math.min(255, (int)(var41.progressAlpha * 255.0F)));
                  var11 = var41.x + var41.width - AndroidUtilities.dp(12.0F) + var5;
                  this.rect.set((float)var11, (float)(AndroidUtilities.dp(4.0F) + var13), (float)(var11 + AndroidUtilities.dp(8.0F)), (float)(var13 + AndroidUtilities.dp(12.0F)));
                  var1.drawArc(this.rect, (float)var41.angle, 220.0F, false, Theme.chat_botProgressPaint);
                  this.invalidate((int)this.rect.left - AndroidUtilities.dp(2.0F), (int)this.rect.top - AndroidUtilities.dp(2.0F), (int)this.rect.right + AndroidUtilities.dp(2.0F), (int)this.rect.bottom + AndroidUtilities.dp(2.0F));
                  var18 = System.currentTimeMillis();
                  if (Math.abs(var41.lastUpdateTime - System.currentTimeMillis()) < 1000L) {
                     var16 = var18 - var41.lastUpdateTime;
                     var7 = (float)(360L * var16) / 2000.0F;
                     var41.angle = (int)((float)var41.angle + var7);
                     var41.angle = var41.angle - var41.angle / 360 * 360;
                     if (var44) {
                        if (var41.progressAlpha < 1.0F) {
                           var41.progressAlpha = var41.progressAlpha + (float)var16 / 200.0F;
                           if (var41.progressAlpha > 1.0F) {
                              var41.progressAlpha = 1.0F;
                           }
                        }
                     } else if (var41.progressAlpha > 0.0F) {
                        var41.progressAlpha = var41.progressAlpha - (float)var16 / 200.0F;
                        if (var41.progressAlpha < 0.0F) {
                           var41.progressAlpha = 0.0F;
                        }
                     }
                  }

                  var41.lastUpdateTime = var18;
               }
            }
         }
      }

   }

   public static StaticLayout generateStaticLayout(CharSequence var0, TextPaint var1, int var2, int var3, int var4, int var5) {
      SpannableStringBuilder var6 = new SpannableStringBuilder(var0);
      StaticLayout var7 = new StaticLayout(var0, var1, var3, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
      int var8 = 0;
      int var9 = 0;

      int var10;
      while(true) {
         var10 = var2;
         if (var8 >= var4) {
            break;
         }

         var7.getLineDirections(var8);
         if (var7.getLineLeft(var8) != 0.0F || var7.isRtlCharAt(var7.getLineStart(var8)) || var7.isRtlCharAt(var7.getLineEnd(var8))) {
            var2 = var3;
         }

         int var11 = var7.getLineEnd(var8);
         if (var11 == var0.length()) {
            var10 = var2;
            break;
         }

         var10 = var11 - 1 + var9;
         if (var6.charAt(var10) == ' ') {
            var6.replace(var10, var10 + 1, "\n");
            var11 = var9;
         } else {
            var11 = var9;
            if (var6.charAt(var10) != '\n') {
               var6.insert(var10, "\n");
               var11 = var9 + 1;
            }
         }

         var10 = var2;
         if (var8 == var7.getLineCount() - 1) {
            break;
         }

         if (var8 == var5 - 1) {
            var10 = var2;
            break;
         }

         ++var8;
         var9 = var11;
      }

      return StaticLayoutEx.createStaticLayout(var6, var1, var10, Alignment.ALIGN_NORMAL, 1.0F, (float)AndroidUtilities.dp(1.0F), false, TruncateAt.END, var10, var5, true);
   }

   private int getAdditionalWidthForPosition(MessageObject.GroupedMessagePosition var1) {
      int var2 = 0;
      int var3 = 0;
      if (var1 != null) {
         if ((var1.flags & 2) == 0) {
            var3 = 0 + AndroidUtilities.dp(4.0F);
         }

         var2 = var3;
         if ((var1.flags & 1) == 0) {
            var2 = var3 + AndroidUtilities.dp(4.0F);
         }
      }

      return var2;
   }

   private int getGroupPhotosWidth() {
      if (!AndroidUtilities.isInMultiwindow && AndroidUtilities.isTablet() && (!AndroidUtilities.isSmallTablet() || this.getResources().getConfiguration().orientation == 2)) {
         int var1 = AndroidUtilities.displaySize.x / 100 * 35;
         int var2 = var1;
         if (var1 < AndroidUtilities.dp(320.0F)) {
            var2 = AndroidUtilities.dp(320.0F);
         }

         return AndroidUtilities.displaySize.x - var2;
      } else {
         return AndroidUtilities.displaySize.x;
      }
   }

   private int getIconForCurrentState() {
      int var1 = this.documentAttachType;
      byte var2 = 0;
      byte var3 = 4;
      int var5;
      if (var1 != 3 && var1 != 5) {
         if (var1 == 1 && !this.drawPhotoImage) {
            if (this.currentMessageObject.isOutOwner()) {
               this.radialProgress.setColors("chat_outLoader", "chat_outLoaderSelected", "chat_outMediaIcon", "chat_outMediaIconSelected");
            } else {
               this.radialProgress.setColors("chat_inLoader", "chat_inLoaderSelected", "chat_inMediaIcon", "chat_inMediaIconSelected");
            }

            var5 = this.buttonState;
            if (var5 == -1) {
               return 5;
            }

            if (var5 == 0) {
               return 2;
            }

            if (var5 == 1) {
               return 3;
            }
         } else {
            this.radialProgress.setColors("chat_mediaLoaderPhoto", "chat_mediaLoaderPhotoSelected", "chat_mediaLoaderPhotoIcon", "chat_mediaLoaderPhotoIconSelected");
            this.videoRadialProgress.setColors("chat_mediaLoaderPhoto", "chat_mediaLoaderPhotoSelected", "chat_mediaLoaderPhotoIcon", "chat_mediaLoaderPhotoIconSelected");
            var1 = this.buttonState;
            if (var1 >= 0 && var1 < 4) {
               if (var1 == 0) {
                  return 2;
               }

               if (var1 == 1) {
                  return 3;
               }

               if (var1 == 2) {
                  return 0;
               }

               if (var1 == 3) {
                  if (this.autoPlayingMedia) {
                     var2 = 4;
                  }

                  return var2;
               }
            } else if (this.buttonState == -1) {
               MessageObject var4;
               if (this.documentAttachType == 1) {
                  if (this.drawPhotoImage && (this.currentPhotoObject != null || this.currentPhotoObjectThumb != null)) {
                     var2 = var3;
                     if (this.photoImage.hasBitmapImage()) {
                        return var2;
                     }

                     var4 = this.currentMessageObject;
                     var2 = var3;
                     if (var4.mediaExists) {
                        return var2;
                     }

                     if (var4.attachPathExists) {
                        var2 = var3;
                        return var2;
                     }
                  }

                  var2 = 5;
                  return var2;
               }

               if (this.currentMessageObject.needDrawBluredPreview()) {
                  var4 = this.currentMessageObject;
                  if (var4.messageOwner.destroyTime != 0) {
                     if (var4.isOutOwner()) {
                        return 9;
                     }

                     return 11;
                  }

                  return 7;
               }

               if (this.hasEmbed) {
                  return 0;
               }
            }
         }

         return 4;
      } else {
         if (this.currentMessageObject.isOutOwner()) {
            this.radialProgress.setColors("chat_outLoader", "chat_outLoaderSelected", "chat_outMediaIcon", "chat_outMediaIconSelected");
         } else {
            this.radialProgress.setColors("chat_inLoader", "chat_inLoaderSelected", "chat_inMediaIcon", "chat_inMediaIconSelected");
         }

         var5 = this.buttonState;
         if (var5 == 1) {
            return 1;
         } else if (var5 == 2) {
            return 2;
         } else {
            return var5 == 4 ? 3 : 0;
         }
      }
   }

   private int getMaxNameWidth() {
      int var1 = this.documentAttachType;
      int var4;
      if (var1 != 6 && var1 != 8 && this.currentMessageObject.type != 5) {
         if (this.currentMessagesGroup != null) {
            if (AndroidUtilities.isTablet()) {
               var1 = AndroidUtilities.getMinTabletSide();
            } else {
               var1 = AndroidUtilities.displaySize.x;
            }

            byte var2 = 0;
            int var3 = 0;

            for(var4 = 0; var3 < this.currentMessagesGroup.posArray.size(); ++var3) {
               MessageObject.GroupedMessagePosition var12 = (MessageObject.GroupedMessagePosition)this.currentMessagesGroup.posArray.get(var3);
               if (var12.minY != 0) {
                  break;
               }

               double var6 = (double)var4;
               double var8 = Math.ceil((double)((float)(var12.pw + var12.leftSpanOffset) / 1000.0F * (float)var1));
               Double.isNaN(var6);
               var4 = (int)(var6 + var8);
            }

            byte var11 = var2;
            if (this.isAvatarVisible) {
               var11 = 48;
            }

            return var4 - AndroidUtilities.dp((float)(var11 + 31));
         }

         var1 = this.backgroundWidth;
         float var10;
         if (this.mediaBackground) {
            var10 = 22.0F;
         } else {
            var10 = 31.0F;
         }

         var4 = AndroidUtilities.dp(var10);
      } else {
         label69: {
            if (AndroidUtilities.isTablet()) {
               if (!this.isChat || this.currentMessageObject.isOutOwner() || !this.currentMessageObject.needDrawAvatar()) {
                  var1 = AndroidUtilities.getMinTabletSide();
                  break label69;
               }

               var4 = AndroidUtilities.getMinTabletSide();
               var1 = AndroidUtilities.dp(42.0F);
            } else {
               Point var5;
               if (!this.isChat || this.currentMessageObject.isOutOwner() || !this.currentMessageObject.needDrawAvatar()) {
                  var5 = AndroidUtilities.displaySize;
                  var1 = Math.min(var5.x, var5.y);
                  break label69;
               }

               var5 = AndroidUtilities.displaySize;
               var4 = Math.min(var5.x, var5.y);
               var1 = AndroidUtilities.dp(42.0F);
            }

            var1 = var4 - var1;
         }

         var1 -= this.backgroundWidth;
         var4 = AndroidUtilities.dp(57.0F);
      }

      return var1 - var4;
   }

   private int getMiniIconForCurrentState() {
      int var1 = this.miniButtonState;
      if (var1 < 0) {
         return 4;
      } else {
         return var1 == 0 ? 2 : 3;
      }
   }

   private boolean intersect(float var1, float var2, float var3, float var4) {
      boolean var5 = true;
      boolean var6 = true;
      if (var1 <= var3) {
         if (var2 < var3) {
            var6 = false;
         }

         return var6;
      } else {
         if (var1 <= var4) {
            var6 = var5;
         } else {
            var6 = false;
         }

         return var6;
      }
   }

   private boolean isCurrentLocationTimeExpired(MessageObject var1) {
      int var2 = this.currentMessageObject.messageOwner.media.period;
      boolean var3 = true;
      boolean var4 = true;
      if (var2 % 60 == 0) {
         if (Math.abs(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - var1.messageOwner.date) <= var1.messageOwner.media.period) {
            var4 = false;
         }

         return var4;
      } else {
         if (Math.abs(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - var1.messageOwner.date) > var1.messageOwner.media.period - 5) {
            var4 = var3;
         } else {
            var4 = false;
         }

         return var4;
      }
   }

   private boolean isDrawSelectionBackground() {
      boolean var1;
      if ((!this.isPressed() || !this.isCheckPressed) && (this.isCheckPressed || !this.isPressed) && !this.isHighlighted) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private boolean isOpenChatByShare(MessageObject var1) {
      TLRPC.MessageFwdHeader var3 = var1.messageOwner.fwd_from;
      boolean var2;
      if (var3 != null && var3.saved_from_peer != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private boolean isPhotoDataChanged(MessageObject var1) {
      int var2 = var1.type;
      boolean var3 = false;
      boolean var4 = var3;
      if (var2 != 0) {
         if (var2 == 14) {
            var4 = var3;
         } else {
            if (var2 == 4) {
               if (this.currentUrl == null) {
                  return true;
               }

               TLRPC.MessageMedia var21 = var1.messageOwner.media;
               TLRPC.GeoPoint var19 = var21.geo;
               double var6 = var19.lat;
               double var8 = var19._long;
               int var10;
               int var11;
               int var16;
               float var17;
               float var18;
               String var20;
               if (var21 instanceof TLRPC.TL_messageMediaGeoLive) {
                  var10 = this.backgroundWidth;
                  var2 = AndroidUtilities.dp(21.0F);
                  var11 = AndroidUtilities.dp(195.0F);
                  double var12 = (double)268435456;
                  Double.isNaN(var12);
                  double var14 = var12 / 3.141592653589793D;
                  var6 = var6 * 3.141592653589793D / 180.0D;
                  var6 = Math.log((Math.sin(var6) + 1.0D) / (1.0D - Math.sin(var6))) * var14 / 2.0D;
                  Double.isNaN(var12);
                  var6 = (double)(Math.round(var12 - var6) - (long)(AndroidUtilities.dp(10.3F) << 6));
                  Double.isNaN(var6);
                  Double.isNaN(var12);
                  var12 = (1.5707963267948966D - Math.atan(Math.exp((var6 - var12) / var14)) * 2.0D) * 180.0D / 3.141592653589793D;
                  var16 = this.currentAccount;
                  var17 = (float)(var10 - var2);
                  var18 = AndroidUtilities.density;
                  var20 = AndroidUtilities.formapMapUrl(var16, var12, var8, (int)(var17 / var18), (int)((float)var11 / var18), false, 15);
               } else if (!TextUtils.isEmpty(var21.title)) {
                  var2 = this.backgroundWidth;
                  var10 = AndroidUtilities.dp(21.0F);
                  var16 = AndroidUtilities.dp(195.0F);
                  var11 = this.currentAccount;
                  var17 = (float)(var2 - var10);
                  var18 = AndroidUtilities.density;
                  var20 = AndroidUtilities.formapMapUrl(var11, var6, var8, (int)(var17 / var18), (int)((float)var16 / var18), true, 15);
               } else {
                  var11 = this.backgroundWidth;
                  var2 = AndroidUtilities.dp(12.0F);
                  var16 = AndroidUtilities.dp(195.0F);
                  var10 = this.currentAccount;
                  var17 = (float)(var11 - var2);
                  var18 = AndroidUtilities.density;
                  var20 = AndroidUtilities.formapMapUrl(var10, var6, var8, (int)(var17 / var18), (int)((float)var16 / var18), true, 15);
               }

               return var20.equals(this.currentUrl) ^ true;
            }

            TLRPC.PhotoSize var5 = this.currentPhotoObject;
            if (var5 != null && !(var5.location instanceof TLRPC.TL_fileLocationUnavailable)) {
               var1 = this.currentMessageObject;
               if (var1 != null && this.photoNotSet) {
                  return FileLoader.getPathToMessage(var1.messageOwner).exists();
               }

               return false;
            }

            var2 = var1.type;
            if (var2 != 1 && var2 != 5 && var2 != 3 && var2 != 8) {
               var4 = var3;
               if (!var1.isAnyKindOfSticker()) {
                  return var4;
               }
            }

            var4 = true;
         }
      }

      return var4;
   }

   private boolean isUserDataChanged() {
      MessageObject var1 = this.currentMessageObject;
      boolean var2 = true;
      if (var1 != null && !this.hasLinkPreview) {
         TLRPC.MessageMedia var7 = var1.messageOwner.media;
         if (var7 != null && var7.webpage instanceof TLRPC.TL_webPage) {
            return true;
         }
      }

      if (this.currentMessageObject != null && (this.currentUser != null || this.currentChat != null)) {
         int var3 = this.lastSendState;
         TLRPC.Message var8 = this.currentMessageObject.messageOwner;
         if (var3 != var8.send_state) {
            return true;
         } else if (this.lastDeleteDate != var8.destroyTime) {
            return true;
         } else if (this.lastViewsCount != var8.views) {
            return true;
         } else {
            boolean var4;
            String var5;
            TLRPC.User var9;
            TLRPC.FileLocation var13;
            label155: {
               this.updateCurrentUserAndChat();
               var4 = this.isAvatarVisible;
               var5 = null;
               if (var4) {
                  var9 = this.currentUser;
                  if (var9 != null) {
                     TLRPC.UserProfilePhoto var10 = var9.photo;
                     if (var10 != null) {
                        var13 = var10.photo_small;
                        break label155;
                     }
                  }

                  TLRPC.Chat var11 = this.currentChat;
                  if (var11 != null) {
                     TLRPC.ChatPhoto var12 = var11.photo;
                     if (var12 != null) {
                        var13 = var12.photo_small;
                        break label155;
                     }
                  }
               }

               var13 = null;
            }

            if (this.replyTextLayout == null && this.currentMessageObject.replyMessageObject != null) {
               return true;
            } else if (this.currentPhoto == null && var13 != null || this.currentPhoto != null && var13 == null) {
               return true;
            } else {
               TLRPC.FileLocation var6 = this.currentPhoto;
               if (var6 != null && var13 != null && (var6.local_id != var13.local_id || var6.volume_id != var13.volume_id)) {
                  return true;
               } else {
                  TLRPC.PhotoSize var14;
                  label120: {
                     if (this.replyNameLayout != null) {
                        var14 = FileLoader.getClosestPhotoSizeWithSize(this.currentMessageObject.replyMessageObject.photoThumbs, 40);
                        if (var14 != null && !this.currentMessageObject.replyMessageObject.isAnyKindOfSticker()) {
                           break label120;
                        }
                     }

                     var14 = null;
                  }

                  if (this.currentReplyPhoto == null && var14 != null) {
                     return true;
                  } else {
                     String var15 = var5;
                     if (this.drawName) {
                        var15 = var5;
                        if (this.isChat) {
                           var15 = var5;
                           if (!this.currentMessageObject.isOutOwner()) {
                              var9 = this.currentUser;
                              if (var9 != null) {
                                 var15 = UserObject.getUserName(var9);
                              } else {
                                 TLRPC.Chat var16 = this.currentChat;
                                 var15 = var5;
                                 if (var16 != null) {
                                    var15 = var16.title;
                                 }
                              }
                           }
                        }
                     }

                     if ((this.currentNameString != null || var15 == null) && (this.currentNameString == null || var15 != null)) {
                        var5 = this.currentNameString;
                        if (var5 == null || var15 == null || var5.equals(var15)) {
                           if (this.drawForwardedName && this.currentMessageObject.needDrawForwarded()) {
                              var5 = this.currentMessageObject.getForwardedName();
                              if (this.currentForwardNameString == null) {
                                 var4 = var2;
                                 if (var5 != null) {
                                    return var4;
                                 }
                              }

                              if (this.currentForwardNameString != null) {
                                 var4 = var2;
                                 if (var5 == null) {
                                    return var4;
                                 }
                              }

                              var15 = this.currentForwardNameString;
                              if (var15 != null && var5 != null && !var15.equals(var5)) {
                                 var4 = var2;
                              } else {
                                 var4 = false;
                              }

                              return var4;
                           }

                           return false;
                        }
                     }

                     return true;
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   static int lambda$setMessageContent$0(ChatMessageCell.PollButton var0, ChatMessageCell.PollButton var1) {
      if (var0.decimal > var1.decimal) {
         return -1;
      } else {
         return var0.decimal < var1.decimal ? 1 : 0;
      }
   }

   private void measureTime(MessageObject var1) {
      TLRPC.Message var2 = var1.messageOwner;
      String var3 = var2.post_author;
      String var4 = "";
      String var9;
      if (var3 != null) {
         var9 = var3.replace("\n", "");
      } else {
         label125: {
            TLRPC.MessageFwdHeader var10 = var2.fwd_from;
            if (var10 != null) {
               var9 = var10.post_author;
               if (var9 != null) {
                  var9 = var9.replace("\n", "");
                  break label125;
               }
            }

            if (!var1.isOutOwner()) {
               var2 = var1.messageOwner;
               if (var2.from_id > 0 && var2.post) {
                  TLRPC.User var12 = MessagesController.getInstance(this.currentAccount).getUser(var1.messageOwner.from_id);
                  if (var12 != null) {
                     var9 = ContactsController.formatName(var12.first_name, var12.last_name).replace('\n', ' ');
                     break label125;
                  }
               }
            }

            var9 = null;
         }
      }

      TLRPC.User var11;
      if (this.currentMessageObject.isFromUser()) {
         var11 = MessagesController.getInstance(this.currentAccount).getUser(var1.messageOwner.from_id);
      } else {
         var11 = null;
      }

      int var6;
      int var7;
      boolean var19;
      label133: {
         if (!var1.isLiveLocation() && var1.getDialogId() != 777000L) {
            TLRPC.Message var5 = var1.messageOwner;
            if (var5.via_bot_id == 0 && var5.via_bot_name == null && (var11 == null || !var11.bot)) {
               label134: {
                  label106: {
                     if (this.currentPosition != null) {
                        MessageObject.GroupedMessages var13 = this.currentMessagesGroup;
                        if (var13 != null) {
                           var6 = var13.messages.size();
                           var7 = 0;

                           while(true) {
                              if (var7 >= var6) {
                                 break label134;
                              }

                              MessageObject var14 = (MessageObject)this.currentMessagesGroup.messages.get(var7);
                              if ((var14.messageOwner.flags & '耀') != 0 || var14.isEditing()) {
                                 break label106;
                              }

                              ++var7;
                           }
                        }
                     }

                     if ((var1.messageOwner.flags & '耀') == 0 && !var1.isEditing()) {
                        break label134;
                     }
                  }

                  var19 = true;
                  break label133;
               }
            }
         }

         var19 = false;
      }

      if (var19) {
         StringBuilder var15 = new StringBuilder();
         var15.append(LocaleController.getString("EditedMessage", 2131559327));
         var15.append(" ");
         long var10002 = (long)var1.messageOwner.date;
         var15.append(LocaleController.getInstance().formatterDay.format(var10002 * 1000L));
         var3 = var15.toString();
      } else {
         long var10001 = (long)var1.messageOwner.date;
         var3 = LocaleController.getInstance().formatterDay.format(var10001 * 1000L);
      }

      if (var9 != null) {
         StringBuilder var16 = new StringBuilder();
         var16.append(", ");
         var16.append(var3);
         this.currentTimeString = var16.toString();
      } else {
         this.currentTimeString = var3;
      }

      var7 = (int)Math.ceil((double)Theme.chat_timePaint.measureText(this.currentTimeString));
      this.timeWidth = var7;
      this.timeTextWidth = var7;
      TLRPC.Message var18 = var1.messageOwner;
      if ((var18.flags & 1024) != 0) {
         this.currentViewsString = String.format("%s", LocaleController.formatShortNumber(Math.max(1, var18.views), (int[])null));
         this.viewsTextWidth = (int)Math.ceil((double)Theme.chat_timePaint.measureText(this.currentViewsString));
         this.timeWidth += this.viewsTextWidth + Theme.chat_msgInViewsDrawable.getIntrinsicWidth() + AndroidUtilities.dp(10.0F);
      }

      if (var9 != null) {
         if (this.availableTimeWidth == 0) {
            this.availableTimeWidth = AndroidUtilities.dp(1000.0F);
         }

         var6 = this.availableTimeWidth - this.timeWidth;
         var7 = var6;
         if (var1.isOutOwner()) {
            if (var1.type == 5) {
               var7 = AndroidUtilities.dp(20.0F);
            } else {
               var7 = AndroidUtilities.dp(96.0F);
            }

            var7 = var6 - var7;
         }

         var6 = (int)Math.ceil((double)Theme.chat_timePaint.measureText(var9, 0, var9.length()));
         Object var8;
         if (var6 > var7) {
            if (var7 <= 0) {
               var7 = 0;
               var8 = var4;
            } else {
               var8 = TextUtils.ellipsize(var9, Theme.chat_timePaint, (float)var7, TruncateAt.END);
            }
         } else {
            var8 = var9;
            var7 = var6;
         }

         StringBuilder var17 = new StringBuilder();
         var17.append(var8);
         var17.append(this.currentTimeString);
         this.currentTimeString = var17.toString();
         this.timeTextWidth += var7;
         this.timeWidth += var7;
      }

   }

   private LinkPath obtainNewUrlPath(boolean var1) {
      LinkPath var2;
      if (!this.urlPathCache.isEmpty()) {
         var2 = (LinkPath)this.urlPathCache.get(0);
         this.urlPathCache.remove(0);
      } else {
         var2 = new LinkPath();
      }

      var2.reset();
      if (var1) {
         this.urlPathSelection.add(var2);
      } else {
         this.urlPath.add(var2);
      }

      return var2;
   }

   private void resetPressedLink(int var1) {
      if (this.pressedLink != null && (this.pressedLinkType == var1 || var1 == -1)) {
         this.resetUrlPaths(false);
         this.pressedLink = null;
         this.pressedLinkType = -1;
         this.invalidate();
      }

   }

   private void resetUrlPaths(boolean var1) {
      if (var1) {
         if (this.urlPathSelection.isEmpty()) {
            return;
         }

         this.urlPathCache.addAll(this.urlPathSelection);
         this.urlPathSelection.clear();
      } else {
         if (this.urlPath.isEmpty()) {
            return;
         }

         this.urlPathCache.addAll(this.urlPath);
         this.urlPath.clear();
      }

   }

   private void sendAccessibilityEventForVirtualView(int var1, int var2) {
      if (((AccessibilityManager)this.getContext().getSystemService("accessibility")).isTouchExplorationEnabled()) {
         AccessibilityEvent var3 = AccessibilityEvent.obtain(var2);
         var3.setPackageName(this.getContext().getPackageName());
         var3.setSource(this, var1);
         this.getParent().requestSendAccessibilityEvent(this, var3);
      }

   }

   private void setMessageContent(MessageObject var1, MessageObject.GroupedMessages var2, boolean var3, boolean var4) {
      MessageObject var5 = var1;
      if (var1.checkLayout() || this.currentPosition != null && this.lastHeight != AndroidUtilities.displaySize.y) {
         this.currentMessageObject = null;
      }

      this.lastHeight = AndroidUtilities.displaySize.y;
      MessageObject var6 = this.currentMessageObject;
      boolean var7;
      if (var6 != null && var6.getId() == var1.getId()) {
         var7 = false;
      } else {
         var7 = true;
      }

      boolean var8;
      if (this.currentMessageObject == var1 && !var1.forceUpdate) {
         var8 = false;
      } else {
         var8 = true;
      }

      var6 = this.currentMessageObject;
      boolean var9;
      if ((var6 == null || var6.getId() != var1.getId() || this.lastSendState != 3 || !var1.isSent()) && (this.currentMessageObject != var1 || !this.isUserDataChanged() && !this.photoNotSet)) {
         var9 = false;
      } else {
         var9 = true;
      }

      boolean var10;
      if (var2 != this.currentMessagesGroup) {
         var10 = true;
      } else {
         var10 = false;
      }

      int var13;
      boolean var14;
      boolean var15;
      TLRPC.MessageMedia var157;
      boolean var182;
      if (!var8 && var1.type == 17) {
         var157 = var1.messageOwner.media;
         Object var12;
         TLRPC.TL_poll var160;
         if (var157 instanceof TLRPC.TL_messageMediaPoll) {
            TLRPC.TL_messageMediaPoll var158 = (TLRPC.TL_messageMediaPoll)var157;
            TLRPC.TL_pollResults var11 = var158.results;
            var12 = var11.results;
            var160 = var158.poll;
            var13 = var11.total_voters;
         } else {
            var160 = null;
            var12 = var160;
            var13 = 0;
         }

         if (var12 != null && this.lastPollResults != null && var13 != this.lastPollResultsVoters) {
            var14 = true;
         } else {
            var14 = false;
         }

         var182 = var14;
         if (!var14) {
            var182 = var14;
            if (var12 != this.lastPollResults) {
               var182 = true;
            }
         }

         var14 = var182;
         if (!var182) {
            TLRPC.TL_poll var177 = this.lastPoll;
            var14 = var182;
            if (var177 != var160) {
               var14 = var182;
               if (var177.closed != var160.closed) {
                  var14 = true;
               }
            }
         }

         var15 = var14;
         if (var14) {
            var15 = var14;
            if (this.attachedToWindow) {
               this.pollAnimationProgressTime = 0.0F;
               var15 = var14;
               if (this.pollVoted) {
                  var15 = var14;
                  if (!var1.isVoted()) {
                     this.pollUnvoteInProgress = true;
                     var15 = var14;
                  }
               }
            }
         }
      } else {
         var15 = false;
      }

      var182 = var10;
      MessageObject.GroupedMessagePosition var162;
      if (!var10) {
         var182 = var10;
         if (var2 != null) {
            if (var2.messages.size() > 1) {
               var162 = (MessageObject.GroupedMessagePosition)this.currentMessagesGroup.positions.get(this.currentMessageObject);
            } else {
               var162 = null;
            }

            if (var162 != this.currentPosition) {
               var182 = true;
            } else {
               var182 = false;
            }
         }
      }

      if (var8 || var9 || var182 || var15 || this.isPhotoDataChanged(var1) || this.pinnedBottom != var3 || this.pinnedTop != var4) {
         this.pinnedBottom = var3;
         this.pinnedTop = var4;
         this.currentMessageObject = var1;
         this.currentMessagesGroup = var2;
         this.lastTime = -2;
         this.isHighlightedAnimated = false;
         this.widthBeforeNewTimeLine = -1;
         var2 = this.currentMessagesGroup;
         if (var2 != null && var2.posArray.size() > 1) {
            this.currentPosition = (MessageObject.GroupedMessagePosition)this.currentMessagesGroup.positions.get(this.currentMessageObject);
            if (this.currentPosition == null) {
               this.currentMessagesGroup = null;
            }
         } else {
            this.currentMessagesGroup = null;
            this.currentPosition = null;
         }

         MessageObject.GroupedMessagePosition var151;
         label4053: {
            if (this.pinnedTop) {
               var151 = this.currentPosition;
               if (var151 == null || (var151.flags & 4) != 0) {
                  var3 = true;
                  break label4053;
               }
            }

            var3 = false;
         }

         label4044: {
            this.drawPinnedTop = var3;
            if (this.pinnedBottom) {
               var151 = this.currentPosition;
               if (var151 == null || (var151.flags & 8) != 0) {
                  var3 = true;
                  break label4044;
               }
            }

            var3 = false;
         }

         TLRPC.Message var152;
         label4035: {
            this.drawPinnedBottom = var3;
            this.photoImage.setCrossfadeWithOldImage(false);
            var152 = var1.messageOwner;
            this.lastSendState = var152.send_state;
            this.lastDeleteDate = var152.destroyTime;
            this.lastViewsCount = var152.views;
            this.isPressed = false;
            this.gamePreviewPressed = false;
            this.sharePressed = false;
            this.isCheckPressed = true;
            this.hasNewLineForTime = false;
            if (this.isChat && !var1.isOutOwner() && var1.needDrawAvatar()) {
               var151 = this.currentPosition;
               if (var151 == null || var151.edge) {
                  var3 = true;
                  break label4035;
               }
            }

            var3 = false;
         }

         this.isAvatarVisible = var3;
         this.wasLayout = false;
         this.drwaShareGoIcon = false;
         this.groupPhotoInvisible = false;
         this.animatingDrawVideoImageButton = 0;
         this.drawVideoSize = false;
         this.canStreamVideo = false;
         this.animatingNoSound = 0;
         this.drawShareButton = this.checkNeedDrawShareButton(var1);
         this.replyNameLayout = null;
         this.adminLayout = null;
         this.checkOnlyButtonPressed = false;
         this.replyTextLayout = null;
         this.hasEmbed = false;
         this.autoPlayingMedia = false;
         this.replyNameWidth = 0;
         this.replyTextWidth = 0;
         this.viaWidth = 0;
         this.viaNameWidth = 0;
         this.addedCaptionHeight = 0;
         this.currentReplyPhoto = null;
         this.currentUser = null;
         this.currentChat = null;
         this.currentViaBotUser = null;
         this.instantViewLayout = null;
         this.drawNameLayout = false;
         if (this.scheduledInvalidate) {
            AndroidUtilities.cancelRunOnUIThread(this.invalidateRunnable);
            this.scheduledInvalidate = false;
         }

         this.resetPressedLink(-1);
         var1.forceUpdate = false;
         this.drawPhotoImage = false;
         this.drawPhotoCheckBox = false;
         this.hasLinkPreview = false;
         this.hasOldCaptionPreview = false;
         this.hasGamePreview = false;
         this.hasInvoicePreview = false;
         this.instantButtonPressed = false;
         this.instantPressed = false;
         if (!var15 && VERSION.SDK_INT >= 21) {
            Drawable var153 = this.selectorDrawable;
            if (var153 != null) {
               var153.setVisible(false, false);
               this.selectorDrawable.setState(StateSet.NOTHING);
            }
         }

         this.linkPreviewPressed = false;
         this.buttonPressed = 0;
         this.miniButtonPressed = 0;
         this.pressedBotButton = -1;
         this.pressedVoteButton = -1;
         this.linkPreviewHeight = 0;
         this.mediaOffsetY = 0;
         this.documentAttachType = 0;
         this.documentAttach = null;
         this.descriptionLayout = null;
         this.titleLayout = null;
         this.videoInfoLayout = null;
         this.photosCountLayout = null;
         this.siteNameLayout = null;
         this.authorLayout = null;
         this.captionLayout = null;
         this.captionOffsetX = 0;
         this.currentCaption = null;
         this.docTitleLayout = null;
         this.drawImageButton = false;
         this.drawVideoImageButton = false;
         this.currentPhotoObject = null;
         this.photoParentObject = null;
         this.currentPhotoObjectThumb = null;
         this.currentPhotoFilter = null;
         this.infoLayout = null;
         this.cancelLoading = false;
         this.buttonState = -1;
         this.miniButtonState = -1;
         this.hasMiniProgress = 0;
         if (this.addedForTest && this.currentUrl != null && this.currentWebFile != null) {
            ImageLoader.getInstance().removeTestWebFile(this.currentUrl);
         }

         this.addedForTest = false;
         this.currentUrl = null;
         this.currentWebFile = null;
         this.photoNotSet = false;
         this.drawBackground = true;
         this.drawName = false;
         this.useSeekBarWaweform = false;
         this.drawInstantView = false;
         this.drawInstantViewType = 0;
         this.drawForwardedName = false;
         this.photoImage.setSideClip(0.0F);
         this.imageBackgroundColor = 0;
         this.imageBackgroundSideColor = 0;
         this.mediaBackground = false;
         this.photoImage.setAlpha(1.0F);
         if (var8 || var9) {
            this.pollButtons.clear();
         }

         this.availableTimeWidth = 0;
         this.photoImage.setForceLoading(false);
         this.photoImage.setNeedsQualityThumb(false);
         this.photoImage.setShouldGenerateQualityThumb(false);
         this.photoImage.setAllowDecodeSingleFrame(false);
         this.photoImage.setRoundRadius(AndroidUtilities.dp(4.0F));
         this.photoImage.setColorFilter((ColorFilter)null);
         if (var8) {
            this.firstVisibleBlockNum = 0;
            this.lastVisibleBlockNum = 0;
            this.needNewVisiblePart = true;
         }

         String var26;
         int var28;
         float var35;
         float var43;
         Point var154;
         String var156;
         int var163;
         String var164;
         int var170;
         TLRPC.PhotoSize var189;
         int var192;
         int var193;
         StringBuilder var198;
         String var205;
         StaticLayout var224;
         MessageObject var228;
         byte var247;
         Exception var254;
         byte var267;
         CharSequence var270;
         StringBuilder var271;
         Exception var10000;
         boolean var10001;
         label4015: {
            var170 = var1.type;
            double var16;
            double var18;
            double var20;
            int var22;
            int var23;
            String var27;
            int var31;
            int var33;
            int var36;
            int var40;
            ImageReceiver var165;
            byte var166;
            ArrayList var168;
            TLRPC.PhotoSize var173;
            String var174;
            WebFile var176;
            String var179;
            ImageReceiver var196;
            ImageLocation var200;
            StringBuilder var206;
            byte var225;
            boolean var230;
            boolean var265;
            if (var170 == 0) {
               this.drawForwardedName = true;
               if (AndroidUtilities.isTablet()) {
                  if (this.isChat && !var1.isOutOwner() && var1.needDrawAvatar()) {
                     var170 = AndroidUtilities.getMinTabletSide() - AndroidUtilities.dp(122.0F);
                     this.drawName = true;
                  } else {
                     if (var1.messageOwner.to_id.channel_id != 0 && !var1.isOutOwner()) {
                        var3 = true;
                     } else {
                        var3 = false;
                     }

                     this.drawName = var3;
                     var170 = AndroidUtilities.getMinTabletSide() - AndroidUtilities.dp(80.0F);
                  }
               } else if (this.isChat && !var1.isOutOwner() && var1.needDrawAvatar()) {
                  var154 = AndroidUtilities.displaySize;
                  var170 = Math.min(var154.x, var154.y) - AndroidUtilities.dp(122.0F);
                  this.drawName = true;
               } else {
                  var154 = AndroidUtilities.displaySize;
                  var170 = Math.min(var154.x, var154.y) - AndroidUtilities.dp(80.0F);
                  if (var1.messageOwner.to_id.channel_id != 0 && !var1.isOutOwner()) {
                     var3 = true;
                  } else {
                     var3 = false;
                  }

                  this.drawName = var3;
               }

               this.availableTimeWidth = var170;
               if (var1.isRoundVideo()) {
                  var16 = (double)this.availableTimeWidth;
                  var18 = Math.ceil((double)Theme.chat_audioTimePaint.measureText("00:00"));
                  if (var1.isOutOwner()) {
                     var13 = 0;
                  } else {
                     var13 = AndroidUtilities.dp(64.0F);
                  }

                  var20 = (double)var13;
                  Double.isNaN(var20);
                  Double.isNaN(var16);
                  this.availableTimeWidth = (int)(var16 - (var18 + var20));
               }

               var22 = var170;
               this.measureTime(var1);
               var170 = this.timeWidth + AndroidUtilities.dp(6.0F);
               var23 = var170;
               if (var1.isOutOwner()) {
                  var23 = var170 + AndroidUtilities.dp(20.5F);
               }

               TLRPC.MessageMedia var155 = var1.messageOwner.media;
               if (var155 instanceof TLRPC.TL_messageMediaGame && var155.game instanceof TLRPC.TL_game) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               this.hasGamePreview = var3;
               var155 = var1.messageOwner.media;
               this.hasInvoicePreview = var155 instanceof TLRPC.TL_messageMediaInvoice;
               if (var155 instanceof TLRPC.TL_messageMediaWebPage && var155.webpage instanceof TLRPC.TL_webPage) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               this.hasLinkPreview = var3;
               if (this.hasLinkPreview && var1.messageOwner.media.webpage.cached_page != null) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               this.drawInstantView = var3;
               if (this.hasLinkPreview && !TextUtils.isEmpty(var1.messageOwner.media.webpage.embed_url) && !var1.isGif()) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               this.hasEmbed = var3;
               if (this.hasLinkPreview) {
                  var156 = var1.messageOwner.media.webpage.site_name;
               } else {
                  var156 = null;
               }

               if (this.hasLinkPreview) {
                  var164 = var1.messageOwner.media.webpage.type;
               } else {
                  var164 = null;
               }

               TLRPC.Document var181;
               label4358: {
                  if (!this.drawInstantView) {
                     if ("telegram_channel".equals(var164)) {
                        this.drawInstantView = true;
                        this.drawInstantViewType = 1;
                     } else if ("telegram_megagroup".equals(var164)) {
                        this.drawInstantView = true;
                        this.drawInstantViewType = 2;
                     } else if ("telegram_message".equals(var164)) {
                        this.drawInstantView = true;
                        this.drawInstantViewType = 3;
                     } else if ("telegram_background".equals(var164)) {
                        label4460: {
                           this.drawInstantView = true;
                           this.drawInstantViewType = 6;

                           Uri var171;
                           try {
                              var171 = Uri.parse(var5.messageOwner.media.webpage.url);
                              var13 = Utilities.parseInt(var171.getQueryParameter("intensity"));
                              var164 = var171.getQueryParameter("bg_color");
                           } catch (Exception var131) {
                              var10001 = false;
                              break label4460;
                           }

                           var170 = var13;
                           var179 = var164;

                           label4359: {
                              try {
                                 if (!TextUtils.isEmpty(var164)) {
                                    break label4359;
                                 }

                                 var181 = var1.getDocument();
                              } catch (Exception var130) {
                                 var10001 = false;
                                 break label4460;
                              }

                              var156 = var164;
                              if (var181 != null) {
                                 label4282: {
                                    var156 = var164;

                                    try {
                                       if (!"image/png".equals(var181.mime_type)) {
                                          break label4282;
                                       }
                                    } catch (Exception var129) {
                                       var10001 = false;
                                       break label4460;
                                    }

                                    var156 = "ffffff";
                                 }
                              }

                              var170 = var13;
                              var179 = var156;
                              if (var13 == 0) {
                                 var170 = 50;
                                 var179 = var156;
                              }
                           }

                           if (var179 != null) {
                              try {
                                 this.imageBackgroundColor = Integer.parseInt(var179, 16) | -16777216;
                                 this.imageBackgroundSideColor = AndroidUtilities.getPatternSideColor(this.imageBackgroundColor);
                                 var165 = this.photoImage;
                                 PorterDuffColorFilter var172 = new PorterDuffColorFilter(AndroidUtilities.getPatternColor(this.imageBackgroundColor), Mode.SRC_IN);
                                 var165.setColorFilter(var172);
                                 this.photoImage.setAlpha((float)var170 / 100.0F);
                              } catch (Exception var126) {
                                 var10001 = false;
                              }
                           } else {
                              label4128: {
                                 try {
                                    var156 = var171.getLastPathSegment();
                                 } catch (Exception var128) {
                                    var10001 = false;
                                    break label4128;
                                 }

                                 if (var156 != null) {
                                    try {
                                       if (var156.length() == 6) {
                                          this.imageBackgroundColor = Integer.parseInt(var156, 16) | -16777216;
                                          TLRPC.TL_photoSizeEmpty var167 = new TLRPC.TL_photoSizeEmpty();
                                          this.currentPhotoObject = var167;
                                          this.currentPhotoObject.type = "s";
                                          this.currentPhotoObject.w = AndroidUtilities.dp(180.0F);
                                          this.currentPhotoObject.h = AndroidUtilities.dp(150.0F);
                                          var173 = this.currentPhotoObject;
                                          TLRPC.TL_fileLocationUnavailable var169 = new TLRPC.TL_fileLocationUnavailable();
                                          var173.location = var169;
                                       }
                                    } catch (Exception var127) {
                                       var10001 = false;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  } else if (var156 != null) {
                     var156 = var156.toLowerCase();
                     if (var156.equals("instagram") || var156.equals("twitter") || "telegram_album".equals(var164)) {
                        TLRPC.WebPage var159 = var1.messageOwner.media.webpage;
                        if (var159.cached_page instanceof TLRPC.TL_page && (var159.photo instanceof TLRPC.TL_photo || MessageObject.isVideoDocument(var159.document))) {
                           this.drawInstantView = false;
                           var168 = var1.messageOwner.media.webpage.cached_page.blocks;
                           var13 = 0;

                           for(var170 = 1; var13 < var168.size(); ++var13) {
                              TLRPC.PageBlock var161 = (TLRPC.PageBlock)var168.get(var13);
                              if (var161 instanceof TLRPC.TL_pageBlockSlideshow) {
                                 var170 = ((TLRPC.TL_pageBlockSlideshow)var161).items.size();
                              } else if (var161 instanceof TLRPC.TL_pageBlockCollage) {
                                 var170 = ((TLRPC.TL_pageBlockCollage)var161).items.size();
                              }
                           }

                           var156 = LocaleController.formatString("Of", 2131560099, 1, var170);
                           this.photosCountWidth = (int)Math.ceil((double)Theme.chat_durationPaint.measureText(var156));
                           this.photosCountLayout = new StaticLayout(var156, Theme.chat_durationPaint, this.photosCountWidth, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                           var14 = true;
                           break label4358;
                        }
                     }
                  }

                  var14 = false;
               }

               label3293: {
                  this.backgroundWidth = var22;
                  if (!this.hasLinkPreview && !this.hasGamePreview && !this.hasInvoicePreview) {
                     var13 = var1.lastLineWidth;
                     if (var22 - var13 >= var23) {
                        var170 = this.backgroundWidth;
                        var13 = var170 - var13;
                        if (var13 >= 0 && var13 <= var23) {
                           this.backgroundWidth = var170 + var23 - var13 + AndroidUtilities.dp(31.0F);
                           break label3293;
                        }

                        this.backgroundWidth = Math.max(this.backgroundWidth, var1.lastLineWidth + var23) + AndroidUtilities.dp(31.0F);
                        break label3293;
                     }
                  }

                  this.backgroundWidth = Math.max(this.backgroundWidth, var1.lastLineWidth) + AndroidUtilities.dp(31.0F);
                  this.backgroundWidth = Math.max(this.backgroundWidth, this.timeWidth + AndroidUtilities.dp(31.0F));
               }

               this.availableTimeWidth = this.backgroundWidth - AndroidUtilities.dp(31.0F);
               if (var1.isRoundVideo()) {
                  var20 = (double)this.availableTimeWidth;
                  var16 = Math.ceil((double)Theme.chat_audioTimePaint.measureText("00:00"));
                  if (var1.isOutOwner()) {
                     var170 = 0;
                  } else {
                     var170 = AndroidUtilities.dp(64.0F);
                  }

                  var18 = (double)var170;
                  Double.isNaN(var18);
                  Double.isNaN(var20);
                  this.availableTimeWidth = (int)(var20 - (var16 + var18));
               }

               this.setMessageObjectInternal(var1);
               var13 = var1.textWidth;
               if (!this.hasGamePreview && !this.hasInvoicePreview) {
                  var170 = 0;
               } else {
                  var170 = AndroidUtilities.dp(10.0F);
               }

               this.backgroundWidth = var13 + var170;
               var170 = var1.textHeight;
               var13 = AndroidUtilities.dp(19.5F);
               var163 = this.namesOffset;
               this.totalHeight = var170 + var13 + var163;
               if (this.drawPinnedTop) {
                  this.namesOffset = var163 - AndroidUtilities.dp(1.0F);
               }

               var163 = Math.max(Math.max(Math.max(Math.max(this.backgroundWidth, this.nameWidth), this.forwardedNameWidth), this.replyNameWidth), this.replyTextWidth);
               if (!this.hasLinkPreview && !this.hasGamePreview && !this.hasInvoicePreview) {
                  this.photoImage.setImageBitmap((Drawable)null);
                  this.calcBackgroundWidth(var22, var23, var163);
               } else {
                  if (AndroidUtilities.isTablet()) {
                     if (this.isChat && var1.needDrawAvatar() && !this.currentMessageObject.isOutOwner()) {
                        var170 = AndroidUtilities.getMinTabletSide();
                        var13 = AndroidUtilities.dp(132.0F);
                     } else {
                        var170 = AndroidUtilities.getMinTabletSide();
                        var13 = AndroidUtilities.dp(80.0F);
                     }
                  } else if (this.isChat && var1.needDrawAvatar() && !this.currentMessageObject.isOutOwner()) {
                     var170 = AndroidUtilities.displaySize.x;
                     var13 = AndroidUtilities.dp(132.0F);
                  } else {
                     var170 = AndroidUtilities.displaySize.x;
                     var13 = AndroidUtilities.dp(80.0F);
                  }

                  var13 = var170 - var13;
                  var170 = var13;
                  if (this.drawShareButton) {
                     var170 = var13 - AndroidUtilities.dp(20.0F);
                  }

                  String var29;
                  String var30;
                  TLRPC.Photo var175;
                  label3258: {
                     String var25;
                     if (this.hasLinkPreview) {
                        TLRPC.TL_webPage var24 = (TLRPC.TL_webPage)var1.messageOwner.media.webpage;
                        var156 = var24.site_name;
                        if (this.drawInstantViewType != 6) {
                           var174 = var24.title;
                        } else {
                           var174 = null;
                        }

                        if (this.drawInstantViewType != 6) {
                           var25 = var24.author;
                        } else {
                           var25 = null;
                        }

                        if (this.drawInstantViewType != 6) {
                           var26 = var24.description;
                        } else {
                           var26 = null;
                        }

                        var175 = var24.photo;
                        var181 = var24.document;
                        var27 = var24.type;
                        var28 = var24.duration;
                        if (var156 != null && var175 != null && var156.toLowerCase().equals("instagram")) {
                           var170 = Math.max(AndroidUtilities.displaySize.y / 3, this.currentMessageObject.textWidth);
                        }

                        if (!"app".equals(var27) && !"profile".equals(var27) && !"article".equals(var27)) {
                           var15 = false;
                        } else {
                           var15 = true;
                        }

                        if (!var14 && !this.drawInstantView && var181 == null && var15) {
                           var182 = true;
                        } else {
                           var182 = false;
                        }

                        if (!var14 && !this.drawInstantView && var181 == null && var26 != null && var27 != null && var15 && this.currentMessageObject.photoThumbs != null) {
                           var3 = true;
                        } else {
                           var3 = false;
                        }

                        this.isSmallImage = var3;
                        var14 = var182;
                        var13 = var28;
                     } else {
                        if (this.hasInvoicePreview) {
                           var157 = var1.messageOwner.media;
                           TLRPC.TL_messageMediaInvoice var178 = (TLRPC.TL_messageMediaInvoice)var157;
                           var174 = var157.title;
                           TLRPC.WebDocument var180 = var178.photo;
                           if (var180 instanceof TLRPC.TL_webDocument) {
                              var176 = WebFile.createWithWebDocument(var180);
                           } else {
                              var176 = null;
                           }

                           this.isSmallImage = false;
                           var27 = "invoice";
                           var29 = null;
                           var30 = null;
                           var205 = null;
                           var14 = false;
                           var181 = null;
                           var175 = null;
                           var31 = 0;
                           break label3258;
                        }

                        TLRPC.TL_game var187 = var1.messageOwner.media.game;
                        var27 = var187.title;
                        if (TextUtils.isEmpty(var1.messageText)) {
                           var156 = var187.description;
                        } else {
                           var156 = null;
                        }

                        var175 = var187.photo;
                        var181 = var187.document;
                        this.isSmallImage = false;
                        var205 = "game";
                        var174 = null;
                        var25 = null;
                        var14 = false;
                        var13 = 0;
                        var26 = var156;
                        var156 = var27;
                        var27 = var205;
                     }

                     Object var32 = null;
                     var31 = var13;
                     var205 = var26;
                     var30 = var25;
                     var29 = var174;
                     var174 = var156;
                     var176 = (WebFile)var32;
                  }

                  var26 = var174;
                  if (this.drawInstantViewType == 6) {
                     var26 = LocaleController.getString("ChatBackground", 2131559024);
                  }

                  if (this.hasInvoicePreview) {
                     var33 = 0;
                  } else {
                     var33 = AndroidUtilities.dp(10.0F);
                  }

                  int var34 = var170 - var33;
                  MessageObject var183 = this.currentMessageObject;
                  if (var183.photoThumbs == null && var175 != null) {
                     var183.generateThumbs(true);
                  }

                  Exception var185;
                  StaticLayout var186;
                  if (var26 != null) {
                     label3223: {
                        label3222: {
                           label4133: {
                              label3220: {
                                 label4134: {
                                    try {
                                       var35 = Theme.chat_replyNamePaint.measureText(var26);
                                    } catch (Exception var125) {
                                       var185 = var125;
                                       break label4134;
                                    }

                                    var18 = (double)(var35 + 1.0F);

                                    try {
                                       var170 = (int)Math.ceil(var18);
                                       var186 = new StaticLayout(var26, Theme.chat_replyNamePaint, Math.min(var170, var34), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                                       this.siteNameLayout = var186;
                                       var35 = this.siteNameLayout.getLineLeft(0);
                                    } catch (Exception var124) {
                                       var185 = var124;
                                       break label4134;
                                    }

                                    if (var35 != 0.0F) {
                                       var3 = true;
                                    } else {
                                       var3 = false;
                                    }

                                    try {
                                       this.siteNameRtl = var3;
                                       var170 = this.siteNameLayout.getLineBottom(this.siteNameLayout.getLineCount() - 1);
                                       this.linkPreviewHeight += var170;
                                       this.totalHeight += var170;
                                       break label3220;
                                    } catch (Exception var123) {
                                       var185 = var123;
                                    }
                                 }

                                 var192 = 0;
                                 var170 = var163;
                                 break label4133;
                              }

                              var192 = var170 + 0;
                              var170 = var163;

                              label4135: {
                                 try {
                                    var13 = this.siteNameLayout.getWidth();
                                 } catch (Exception var122) {
                                    var10000 = var122;
                                    var10001 = false;
                                    break label4135;
                                 }

                                 var170 = var163;

                                 try {
                                    this.siteNameWidth = var13;
                                 } catch (Exception var121) {
                                    var10000 = var121;
                                    var10001 = false;
                                    break label4135;
                                 }

                                 var28 = var13 + var33;
                                 var170 = var163;

                                 try {
                                    var13 = Math.max(var163, var28);
                                 } catch (Exception var120) {
                                    var10000 = var120;
                                    var10001 = false;
                                    break label4135;
                                 }

                                 var170 = var13;

                                 try {
                                    var163 = Math.max(0, var28);
                                    break label3222;
                                 } catch (Exception var119) {
                                    var10000 = var119;
                                    var10001 = false;
                                 }
                              }

                              var185 = var10000;
                           }

                           FileLog.e((Throwable)var185);
                           var166 = 0;
                           var13 = var170;
                           var170 = var166;
                           break label3223;
                        }

                        var170 = var163;
                     }
                  } else {
                     var170 = 0;
                     var192 = 0;
                     var13 = var163;
                  }

                  boolean var37;
                  int var38;
                  int var41;
                  TLRPC.Document var184;
                  TLRPC.Photo var190;
                  boolean var266;
                  if (var29 != null) {
                     TLRPC.Photo var188;
                     TLRPC.Document var215;
                     label3184: {
                        label3183: {
                           Exception var191;
                           label4136: {
                              byte var259;
                              label3181: {
                                 Exception var210;
                                 label4137: {
                                    label3179: {
                                       label3178: {
                                          label4138: {
                                             try {
                                                this.titleX = Integer.MAX_VALUE;
                                                var163 = this.linkPreviewHeight;
                                             } catch (Exception var118) {
                                                var10000 = var118;
                                                var10001 = false;
                                                break label4138;
                                             }

                                             if (var163 != 0) {
                                                try {
                                                   this.linkPreviewHeight += AndroidUtilities.dp(2.0F);
                                                   this.totalHeight += AndroidUtilities.dp(2.0F);
                                                } catch (Exception var116) {
                                                   var10000 = var116;
                                                   var10001 = false;
                                                   break label3178;
                                                }
                                             }

                                             try {
                                                var3 = this.isSmallImage;
                                             } catch (Exception var117) {
                                                var10000 = var117;
                                                var10001 = false;
                                                break label4138;
                                             }

                                             if (var3 && var205 != null) {
                                                try {
                                                   this.titleLayout = generateStaticLayout(var29, Theme.chat_replyNamePaint, var34, var34 - AndroidUtilities.dp(52.0F), 3, 4);
                                                   var163 = this.titleLayout.getLineCount();
                                                } catch (Exception var114) {
                                                   var10000 = var114;
                                                   var10001 = false;
                                                   break label3178;
                                                }

                                                var163 = 3 - var163;
                                                var259 = 3;
                                                break label3179;
                                             }

                                             try {
                                                this.titleLayout = StaticLayoutEx.createStaticLayout(var29, Theme.chat_replyNamePaint, var34, Alignment.ALIGN_NORMAL, 1.0F, (float)AndroidUtilities.dp(1.0F), false, TruncateAt.END, var34, 4);
                                             } catch (Exception var115) {
                                                var10000 = var115;
                                                var10001 = false;
                                                break label4138;
                                             }

                                             var259 = 0;
                                             var163 = 3;
                                             break label3179;
                                          }

                                          var210 = var10000;
                                          var163 = 3;
                                          var36 = var170;
                                          break label4137;
                                       }

                                       var185 = var10000;
                                       var163 = 3;
                                       var230 = false;
                                       var36 = var170;
                                       var10 = var14;
                                       var191 = var185;
                                       var14 = var230;
                                       var188 = var175;
                                       var184 = var181;
                                       break label4136;
                                    }

                                    label3156: {
                                       try {
                                          var186 = this.titleLayout;
                                       } catch (Exception var113) {
                                          var210 = var113;
                                          break label3156;
                                       }

                                       try {
                                          var28 = var186.getLineBottom(this.titleLayout.getLineCount() - 1);
                                          this.linkPreviewHeight += var28;
                                          this.totalHeight += var28;
                                          break label3181;
                                       } catch (Exception var112) {
                                          var210 = var112;
                                       }
                                    }

                                    var36 = var170;
                                 }

                                 var188 = var175;
                                 var10 = var14;
                                 var14 = false;
                                 var184 = var181;
                                 var191 = var210;
                                 break label4136;
                              }

                              var38 = 0;
                              var230 = false;
                              var36 = var170;
                              var170 = var13;

                              while(true) {
                                 label3146: {
                                    label4360: {
                                       try {
                                          if (var38 >= this.titleLayout.getLineCount()) {
                                             break label3183;
                                          }

                                          var35 = this.titleLayout.getLineLeft(var38);
                                       } catch (Exception var111) {
                                          var185 = var111;
                                          break label4360;
                                       }

                                       int var39 = (int)var35;
                                       if (var39 != 0) {
                                          var230 = true;
                                       }

                                       var188 = var175;

                                       try {
                                          var13 = this.titleX;
                                       } catch (Exception var101) {
                                          var184 = var181;
                                          var191 = var101;
                                          var13 = var170;
                                          var10 = var14;
                                          var14 = var230;
                                          break;
                                       }

                                       label4140: {
                                          if (var13 == Integer.MAX_VALUE) {
                                             var13 = -var39;

                                             try {
                                                this.titleX = var13;
                                             } catch (Exception var110) {
                                                var10000 = var110;
                                                var10001 = false;
                                                break label4140;
                                             }
                                          } else {
                                             try {
                                                this.titleX = Math.max(this.titleX, -var39);
                                             } catch (Exception var109) {
                                                var10000 = var109;
                                                var10001 = false;
                                                break label4140;
                                             }
                                          }

                                          label4141: {
                                             if (var39 != 0) {
                                                try {
                                                   var40 = this.titleLayout.getWidth() - var39;
                                                } catch (Exception var106) {
                                                   var10000 = var106;
                                                   var10001 = false;
                                                   break label4140;
                                                }
                                             } else {
                                                try {
                                                   var35 = this.titleLayout.getLineWidth(var38);
                                                } catch (Exception var107) {
                                                   var10000 = var107;
                                                   var10001 = false;
                                                   break label4140;
                                                }

                                                var18 = (double)var35;
                                                var13 = var170;

                                                try {
                                                   var40 = (int)Math.ceil(var18);
                                                } catch (Exception var108) {
                                                   var10000 = var108;
                                                   var10001 = false;
                                                   break label4141;
                                                }
                                             }

                                             label4143: {
                                                if (var38 >= var259) {
                                                   var41 = var40;
                                                   if (var39 == 0) {
                                                      break label4143;
                                                   }

                                                   var41 = var40;
                                                   var13 = var170;

                                                   try {
                                                      if (!this.isSmallImage) {
                                                         break label4143;
                                                      }
                                                   } catch (Exception var105) {
                                                      var10000 = var105;
                                                      var10001 = false;
                                                      break label4141;
                                                   }
                                                }

                                                var13 = var170;

                                                try {
                                                   var41 = var40 + AndroidUtilities.dp(52.0F);
                                                } catch (Exception var104) {
                                                   var10000 = var104;
                                                   var10001 = false;
                                                   break label4141;
                                                }
                                             }

                                             var40 = var41 + var33;
                                             var13 = var170;

                                             try {
                                                var170 = Math.max(var170, var40);
                                             } catch (Exception var103) {
                                                var10000 = var103;
                                                var10001 = false;
                                                break label4141;
                                             }

                                             var13 = var170;

                                             try {
                                                var40 = Math.max(var36, var40);
                                                break label3146;
                                             } catch (Exception var102) {
                                                var10000 = var102;
                                                var10001 = false;
                                             }
                                          }

                                          var10 = var14;
                                          var191 = var10000;
                                          var14 = var230;
                                          var184 = var181;
                                          break;
                                       }

                                       var185 = var10000;
                                    }

                                    var191 = var185;
                                    var13 = var170;
                                    var10 = var14;
                                    var14 = var230;
                                    var188 = var175;
                                    var184 = var181;
                                    break;
                                 }

                                 ++var38;
                                 var175 = var175;
                                 var181 = var181;
                                 var36 = var40;
                              }
                           }

                           FileLog.e((Throwable)var191);
                           var215 = var184;
                           var266 = var10;
                           var170 = var13;
                           break label3184;
                        }

                        var266 = var14;
                        var14 = var230;
                        var188 = var175;
                        var215 = var181;
                     }

                     var13 = var170;
                     var38 = var36;
                     var28 = var34;
                     var170 = var170;
                     var36 = var36;
                     var193 = var163;
                     var37 = var14;
                     var8 = var266;
                     var190 = var188;
                     var184 = var215;
                     if (var14) {
                        var28 = var34;
                        var170 = var13;
                        var36 = var38;
                        var193 = var163;
                        var37 = var14;
                        var8 = var266;
                        var190 = var188;
                        var184 = var215;
                        if (this.isSmallImage) {
                           var28 = var34 - AndroidUtilities.dp(48.0F);
                           var170 = var13;
                           var36 = var38;
                           var193 = var163;
                           var37 = var14;
                           var8 = var266;
                           var190 = var188;
                           var184 = var215;
                        }
                     }
                  } else {
                     var225 = 3;
                     var37 = false;
                     var184 = var181;
                     var190 = var175;
                     var8 = var14;
                     var193 = var225;
                     var36 = var170;
                     var170 = var13;
                     var28 = var34;
                  }

                  if (var30 != null && var29 == null) {
                     label4145: {
                        var192 = var193;

                        label3086: {
                           label4146: {
                              label3084: {
                                 label4147: {
                                    label4148: {
                                       try {
                                          if (this.linkPreviewHeight == 0) {
                                             break label4148;
                                          }
                                       } catch (Exception var100) {
                                          var10000 = var100;
                                          var10001 = false;
                                          break label4147;
                                       }

                                       var192 = var193;

                                       try {
                                          this.linkPreviewHeight += AndroidUtilities.dp(2.0F);
                                       } catch (Exception var99) {
                                          var10000 = var99;
                                          var10001 = false;
                                          break label4147;
                                       }

                                       var192 = var193;

                                       try {
                                          this.totalHeight += AndroidUtilities.dp(2.0F);
                                       } catch (Exception var98) {
                                          var10000 = var98;
                                          var10001 = false;
                                          break label4147;
                                       }
                                    }

                                    label4149: {
                                       label4150: {
                                          if (var193 == 3) {
                                             var192 = var193;

                                             try {
                                                if (!this.isSmallImage) {
                                                   break label4150;
                                                }
                                             } catch (Exception var97) {
                                                var10000 = var97;
                                                var10001 = false;
                                                break label4147;
                                             }

                                             if (var205 == null) {
                                                break label4150;
                                             }
                                          }

                                          var192 = var193;

                                          try {
                                             this.authorLayout = generateStaticLayout(var30, Theme.chat_replyNamePaint, var28, var28 - AndroidUtilities.dp(52.0F), var193, 1);
                                          } catch (Exception var93) {
                                             var10000 = var93;
                                             var10001 = false;
                                             break label4147;
                                          }

                                          var192 = var193;

                                          try {
                                             var13 = var193 - this.authorLayout.getLineCount();
                                             break label4149;
                                          } catch (Exception var92) {
                                             var10000 = var92;
                                             var10001 = false;
                                             break label4147;
                                          }
                                       }

                                       var192 = var193;

                                       try {
                                          var186 = new StaticLayout;
                                       } catch (Exception var96) {
                                          var10000 = var96;
                                          var10001 = false;
                                          break label4147;
                                       }

                                       var192 = var193;

                                       try {
                                          var186.<init>(var30, Theme.chat_replyNamePaint, var28, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                                       } catch (Exception var95) {
                                          var10000 = var95;
                                          var10001 = false;
                                          break label4147;
                                       }

                                       var192 = var193;

                                       try {
                                          this.authorLayout = var186;
                                       } catch (Exception var94) {
                                          var10000 = var94;
                                          var10001 = false;
                                          break label4147;
                                       }

                                       var13 = var193;
                                    }

                                    var192 = var13;

                                    try {
                                       var193 = this.authorLayout.getLineBottom(this.authorLayout.getLineCount() - 1);
                                    } catch (Exception var91) {
                                       var10000 = var91;
                                       var10001 = false;
                                       break label4147;
                                    }

                                    var192 = var13;

                                    try {
                                       this.linkPreviewHeight += var193;
                                    } catch (Exception var90) {
                                       var10000 = var90;
                                       var10001 = false;
                                       break label4147;
                                    }

                                    var192 = var13;

                                    try {
                                       this.totalHeight += var193;
                                    } catch (Exception var89) {
                                       var10000 = var89;
                                       var10001 = false;
                                       break label4147;
                                    }

                                    var192 = var13;

                                    try {
                                       var193 = (int)this.authorLayout.getLineLeft(0);
                                    } catch (Exception var88) {
                                       var10000 = var88;
                                       var10001 = false;
                                       break label4147;
                                    }

                                    var192 = var13;

                                    try {
                                       this.authorX = -var193;
                                    } catch (Exception var87) {
                                       var10000 = var87;
                                       var10001 = false;
                                       break label4147;
                                    }

                                    if (var193 != 0) {
                                       label4152: {
                                          var192 = var13;

                                          try {
                                             var193 = this.authorLayout.getWidth() - var193;
                                          } catch (Exception var85) {
                                             var10000 = var85;
                                             var10001 = false;
                                             break label4152;
                                          }

                                          var266 = true;
                                          var192 = var193;
                                          var14 = var266;
                                       }
                                    } else {
                                       label4153: {
                                          var192 = var13;

                                          try {
                                             var18 = Math.ceil((double)this.authorLayout.getLineWidth(0));
                                          } catch (Exception var86) {
                                             var10000 = var86;
                                             var10001 = false;
                                             break label4153;
                                          }

                                          var192 = (int)var18;
                                          var14 = false;
                                       }
                                    }
                                    break label3084;
                                 }

                                 var185 = var10000;
                                 var14 = false;
                                 var13 = var192;
                                 break label4146;
                              }

                              var40 = var192 + var33;
                              var192 = var170;

                              label4154: {
                                 try {
                                    var170 = Math.max(var170, var40);
                                 } catch (Exception var84) {
                                    var10000 = var84;
                                    var10001 = false;
                                    break label4154;
                                 }

                                 var192 = var170;

                                 try {
                                    var40 = Math.max(var36, var40);
                                    break label3086;
                                 } catch (Exception var83) {
                                    var10000 = var83;
                                    var10001 = false;
                                 }
                              }

                              var185 = var10000;
                              var170 = var192;
                           }

                           FileLog.e((Throwable)var185);
                           var15 = var14;
                           var193 = var13;
                           break label4145;
                        }

                        var36 = var40;
                        var15 = var14;
                        var193 = var13;
                     }
                  } else {
                     var15 = false;
                  }

                  var13 = var170;
                  if (var205 != null) {
                     label4287: {
                        label3001: {
                           label4156: {
                              label2999: {
                                 label4157: {
                                    try {
                                       this.descriptionX = 0;
                                       this.currentMessageObject.generateLinkDescription();
                                       if (this.linkPreviewHeight != 0) {
                                          this.linkPreviewHeight += AndroidUtilities.dp(2.0F);
                                          this.totalHeight += AndroidUtilities.dp(2.0F);
                                       }
                                    } catch (Exception var82) {
                                       var10000 = var82;
                                       var10001 = false;
                                       break label4157;
                                    }

                                    label2994: {
                                       label2993: {
                                          if (var26 != null) {
                                             try {
                                                if (var26.toLowerCase().equals("twitter")) {
                                                   break label2993;
                                                }
                                             } catch (Exception var81) {
                                                var10000 = var81;
                                                var10001 = false;
                                                break label4157;
                                             }
                                          }

                                          var182 = false;
                                          break label2994;
                                       }

                                       var182 = true;
                                    }

                                    label4158: {
                                       TruncateAt var195;
                                       CharSequence var211;
                                       TextPaint var237;
                                       Alignment var241;
                                       label4159: {
                                          if (var193 == 3) {
                                             try {
                                                if (!this.isSmallImage) {
                                                   var211 = var5.linkDescription;
                                                   var237 = Theme.chat_replyTextPaint;
                                                   var241 = Alignment.ALIGN_NORMAL;
                                                   var35 = (float)AndroidUtilities.dp(1.0F);
                                                   var195 = TruncateAt.END;
                                                   break label4159;
                                                }
                                             } catch (Exception var80) {
                                                var10000 = var80;
                                                var10001 = false;
                                                break label4157;
                                             }
                                          }

                                          TextPaint var194;
                                          CharSequence var234;
                                          try {
                                             var234 = var5.linkDescription;
                                             var194 = Theme.chat_replyTextPaint;
                                             var40 = AndroidUtilities.dp(52.0F);
                                          } catch (Exception var78) {
                                             var10000 = var78;
                                             var10001 = false;
                                             break label4157;
                                          }

                                          if (var182) {
                                             var247 = 100;
                                          } else {
                                             var247 = 6;
                                          }

                                          try {
                                             this.descriptionLayout = generateStaticLayout(var234, var194, var28, var28 - var40, var193, var247);
                                             break label4158;
                                          } catch (Exception var77) {
                                             var10000 = var77;
                                             var10001 = false;
                                             break label4157;
                                          }
                                       }

                                       if (var182) {
                                          var247 = 100;
                                       } else {
                                          var247 = 6;
                                       }

                                       try {
                                          this.descriptionLayout = StaticLayoutEx.createStaticLayout(var211, var237, var28, var241, 1.0F, var35, false, var195, var28, var247);
                                       } catch (Exception var79) {
                                          var10000 = var79;
                                          var10001 = false;
                                          break label4157;
                                       }

                                       var193 = 0;
                                    }

                                    try {
                                       var13 = this.descriptionLayout.getLineBottom(this.descriptionLayout.getLineCount() - 1);
                                       this.linkPreviewHeight += var13;
                                       this.totalHeight += var13;
                                    } catch (Exception var76) {
                                       var10000 = var76;
                                       var10001 = false;
                                       break label4157;
                                    }

                                    var40 = 0;
                                    var182 = false;

                                    while(true) {
                                       try {
                                          if (var40 >= this.descriptionLayout.getLineCount()) {
                                             break;
                                          }

                                          var38 = (int)Math.ceil((double)this.descriptionLayout.getLineLeft(var40));
                                       } catch (Exception var74) {
                                          var10000 = var74;
                                          var10001 = false;
                                          break label4157;
                                       }

                                       if (var38 != 0) {
                                          label2960: {
                                             try {
                                                if (this.descriptionX == 0) {
                                                   this.descriptionX = -var38;
                                                   break label2960;
                                                }
                                             } catch (Exception var75) {
                                                var10000 = var75;
                                                var10001 = false;
                                                break label4157;
                                             }

                                             try {
                                                this.descriptionX = Math.max(this.descriptionX, -var38);
                                             } catch (Exception var73) {
                                                var10000 = var73;
                                                var10001 = false;
                                                break label4157;
                                             }
                                          }

                                          var182 = true;
                                       }

                                       ++var40;
                                    }

                                    try {
                                       var34 = this.descriptionLayout.getWidth();
                                       break label2999;
                                    } catch (Exception var72) {
                                       var10000 = var72;
                                       var10001 = false;
                                    }
                                 }

                                 var185 = var10000;
                                 break label4156;
                              }

                              var38 = var36;
                              var40 = 0;
                              var265 = var182;

                              while(true) {
                                 int var42;
                                 try {
                                    if (var40 >= this.descriptionLayout.getLineCount()) {
                                       break label3001;
                                    }

                                    var42 = (int)Math.ceil((double)this.descriptionLayout.getLineLeft(var40));
                                 } catch (Exception var70) {
                                    var10000 = var70;
                                    var10001 = false;
                                    break;
                                 }

                                 if (var42 == 0) {
                                    try {
                                       if (this.descriptionX != 0) {
                                          this.descriptionX = 0;
                                       }
                                    } catch (Exception var69) {
                                       var10000 = var69;
                                       var10001 = false;
                                       break;
                                    }
                                 }

                                 if (var42 != 0) {
                                    var13 = var34 - var42;
                                 } else if (var265) {
                                    var13 = var34;
                                 } else {
                                    try {
                                       var13 = Math.min((int)Math.ceil((double)this.descriptionLayout.getLineWidth(var40)), var34);
                                    } catch (Exception var68) {
                                       var10000 = var68;
                                       var10001 = false;
                                       break;
                                    }
                                 }

                                 label2936: {
                                    if (var40 >= var193) {
                                       var41 = var13;
                                       if (var193 == 0) {
                                          break label2936;
                                       }

                                       var41 = var13;
                                       if (var42 == 0) {
                                          break label2936;
                                       }

                                       var41 = var13;

                                       try {
                                          if (!this.isSmallImage) {
                                             break label2936;
                                          }
                                       } catch (Exception var71) {
                                          var10000 = var71;
                                          var10001 = false;
                                          break;
                                       }
                                    }

                                    try {
                                       var41 = var13 + AndroidUtilities.dp(52.0F);
                                    } catch (Exception var67) {
                                       var10000 = var67;
                                       var10001 = false;
                                       break;
                                    }
                                 }

                                 var41 += var33;
                                 var13 = var38;
                                 if (var38 < var41) {
                                    if (var37) {
                                       try {
                                          this.titleX += var41 - var38;
                                       } catch (Exception var66) {
                                          var10000 = var66;
                                          var10001 = false;
                                          break;
                                       }
                                    }

                                    if (var15) {
                                       try {
                                          this.authorX += var41 - var38;
                                       } catch (Exception var65) {
                                          var10000 = var65;
                                          var10001 = false;
                                          break;
                                       }
                                    }

                                    var13 = var41;
                                 }

                                 try {
                                    var38 = Math.max(var170, var41);
                                 } catch (Exception var64) {
                                    var10000 = var64;
                                    var10001 = false;
                                    break;
                                 }

                                 ++var40;
                                 var170 = var38;
                                 var38 = var13;
                              }

                              var185 = var10000;
                           }

                           FileLog.e((Throwable)var185);
                           var13 = var170;
                           break label4287;
                        }

                        var13 = var170;
                     }
                  }

                  var15 = var8;
                  if (var8) {
                     label4288: {
                        var186 = this.descriptionLayout;
                        if (var186 != null) {
                           var15 = var8;
                           if (var186 == null) {
                              break label4288;
                           }

                           var15 = var8;
                           if (var186.getLineCount() != 1) {
                              break label4288;
                           }
                        }

                        this.isSmallImage = false;
                        var15 = false;
                     }
                  }

                  var170 = var28;
                  if (var15) {
                     var170 = AndroidUtilities.dp(48.0F);
                  }

                  label2893: {
                     label2892: {
                        if (var184 != null) {
                           if (MessageObject.isRoundVideoDocument(var184)) {
                              this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(var184.thumbs, 90);
                              this.photoParentObject = var184;
                              this.documentAttach = var184;
                              this.documentAttachType = 7;
                           } else {
                              TLRPC.Document var201 = var184;
                              TLRPC.DocumentAttribute var216;
                              TLRPC.PhotoSize var239;
                              if (MessageObject.isGifDocument(var184)) {
                                 if (!var1.isGame() && !SharedConfig.autoplayGifs) {
                                    var1.gifState = 1.0F;
                                 }

                                 ImageReceiver var238 = this.photoImage;
                                 if (var1.gifState != 1.0F) {
                                    var3 = true;
                                 } else {
                                    var3 = false;
                                 }

                                 var238.setAllowStartAnimation(var3);
                                 this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(var184.thumbs, 90);
                                 this.photoParentObject = var184;
                                 var239 = this.currentPhotoObject;
                                 if (var239 != null && (var239.w == 0 || var239.h == 0)) {
                                    for(var193 = 0; var193 < var201.attributes.size(); ++var193) {
                                       var216 = (TLRPC.DocumentAttribute)var201.attributes.get(var193);
                                       if (var216 instanceof TLRPC.TL_documentAttributeImageSize || var216 instanceof TLRPC.TL_documentAttributeVideo) {
                                          var239 = this.currentPhotoObject;
                                          var239.w = var216.w;
                                          var239.h = var216.h;
                                          break;
                                       }
                                    }

                                    var239 = this.currentPhotoObject;
                                    if (var239.w == 0 || var239.h == 0) {
                                       var239 = this.currentPhotoObject;
                                       var193 = AndroidUtilities.dp(150.0F);
                                       var239.h = var193;
                                       var239.w = var193;
                                    }
                                 }

                                 this.documentAttach = var201;
                                 this.documentAttachType = 2;
                              } else {
                                 TLRPC.PhotoSize var218;
                                 if (MessageObject.isVideoDocument(var184)) {
                                    if (var190 != null) {
                                       this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(var190.sizes, AndroidUtilities.getPhotoSize(), true);
                                       this.currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(var190.sizes, 40);
                                       this.photoParentObject = var190;
                                    }

                                    if (this.currentPhotoObject == null) {
                                       this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(var184.thumbs, 320);
                                       this.currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(var184.thumbs, 40);
                                       this.photoParentObject = var184;
                                    }

                                    if (this.currentPhotoObject == this.currentPhotoObjectThumb) {
                                       this.currentPhotoObjectThumb = null;
                                    }

                                    if (this.currentPhotoObject == null) {
                                       this.currentPhotoObject = new TLRPC.TL_photoSize();
                                       var218 = this.currentPhotoObject;
                                       var218.type = "s";
                                       var218.location = new TLRPC.TL_fileLocationUnavailable();
                                    }

                                    var218 = this.currentPhotoObject;
                                    if (var218 != null && (var218.w == 0 || var218.h == 0 || var218 instanceof TLRPC.TL_photoStrippedSize)) {
                                       TLRPC.PhotoSize var203;
                                       for(var193 = 0; var193 < var201.attributes.size(); ++var193) {
                                          var216 = (TLRPC.DocumentAttribute)var201.attributes.get(var193);
                                          if (var216 instanceof TLRPC.TL_documentAttributeVideo) {
                                             var203 = this.currentPhotoObject;
                                             if (var203 instanceof TLRPC.TL_photoStrippedSize) {
                                                var193 = var216.w;
                                                var35 = (float)Math.max(var193, var193) / 50.0F;
                                                var203 = this.currentPhotoObject;
                                                var203.w = (int)((float)var216.w / var35);
                                                var203.h = (int)((float)var216.h / var35);
                                             } else {
                                                var203.w = var216.w;
                                                var203.h = var216.h;
                                             }
                                             break;
                                          }
                                       }

                                       var203 = this.currentPhotoObject;
                                       if (var203.w == 0 || var203.h == 0) {
                                          var203 = this.currentPhotoObject;
                                          var193 = AndroidUtilities.dp(150.0F);
                                          var203.h = var193;
                                          var203.w = var193;
                                       }
                                    }

                                    this.createDocumentLayout(0, var1);
                                 } else if (MessageObject.isStickerDocument(var184)) {
                                    this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(var184.thumbs, 90);
                                    this.photoParentObject = var184;
                                    var239 = this.currentPhotoObject;
                                    if (var239 != null && (var239.w == 0 || var239.h == 0)) {
                                       for(var193 = 0; var193 < var201.attributes.size(); ++var193) {
                                          var216 = (TLRPC.DocumentAttribute)var201.attributes.get(var193);
                                          if (var216 instanceof TLRPC.TL_documentAttributeImageSize) {
                                             var239 = this.currentPhotoObject;
                                             var239.w = var216.w;
                                             var239.h = var216.h;
                                             break;
                                          }
                                       }

                                       var239 = this.currentPhotoObject;
                                       if (var239.w == 0 || var239.h == 0) {
                                          var239 = this.currentPhotoObject;
                                          var193 = AndroidUtilities.dp(150.0F);
                                          var239.h = var193;
                                          var239.w = var193;
                                       }
                                    }

                                    this.documentAttach = var201;
                                    this.documentAttachType = 6;
                                 } else if (this.drawInstantViewType == 6) {
                                    this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(var184.thumbs, 320);
                                    this.photoParentObject = var184;
                                    var239 = this.currentPhotoObject;
                                    if (var239 != null && (var239.w == 0 || var239.h == 0)) {
                                       for(var193 = 0; var193 < var201.attributes.size(); ++var193) {
                                          TLRPC.DocumentAttribute var250 = (TLRPC.DocumentAttribute)var201.attributes.get(var193);
                                          if (var250 instanceof TLRPC.TL_documentAttributeImageSize) {
                                             var218 = this.currentPhotoObject;
                                             var218.w = var250.w;
                                             var218.h = var250.h;
                                             break;
                                          }
                                       }

                                       var239 = this.currentPhotoObject;
                                       if (var239.w == 0 || var239.h == 0) {
                                          var239 = this.currentPhotoObject;
                                          var193 = AndroidUtilities.dp(150.0F);
                                          var239.h = var193;
                                          var239.w = var193;
                                       }
                                    }

                                    this.documentAttach = var201;
                                    this.documentAttachType = 8;
                                    var174 = AndroidUtilities.formatFileSize((long)this.documentAttach.size);
                                    this.durationWidth = (int)Math.ceil((double)Theme.chat_durationPaint.measureText(var174));
                                    this.videoInfoLayout = new StaticLayout(var174, Theme.chat_durationPaint, this.durationWidth, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                                 } else {
                                    this.calcBackgroundWidth(var22, var23, var13);
                                    if (this.backgroundWidth < AndroidUtilities.dp(20.0F) + var22) {
                                       this.backgroundWidth = AndroidUtilities.dp(20.0F) + var22;
                                    }

                                    if (MessageObject.isVoiceDocument(var184)) {
                                       this.createDocumentLayout(this.backgroundWidth - AndroidUtilities.dp(10.0F), var1);
                                       this.mediaOffsetY = this.currentMessageObject.textHeight + AndroidUtilities.dp(8.0F) + this.linkPreviewHeight;
                                       this.totalHeight += AndroidUtilities.dp(44.0F);
                                       this.linkPreviewHeight += AndroidUtilities.dp(44.0F);
                                       var193 = var22 - AndroidUtilities.dp(86.0F);
                                       if (AndroidUtilities.isTablet()) {
                                          var28 = AndroidUtilities.getMinTabletSide();
                                          if (this.isChat && var1.needDrawAvatar() && !var1.isOutOwner()) {
                                             var35 = 52.0F;
                                          } else {
                                             var35 = 0.0F;
                                          }

                                          var13 = Math.max(var13, Math.min(var28 - AndroidUtilities.dp(var35), AndroidUtilities.dp(220.0F)) - AndroidUtilities.dp(30.0F) + var33);
                                       } else {
                                          var28 = AndroidUtilities.displaySize.x;
                                          if (this.isChat && var1.needDrawAvatar() && !var1.isOutOwner()) {
                                             var35 = 52.0F;
                                          } else {
                                             var35 = 0.0F;
                                          }

                                          var13 = Math.max(var13, Math.min(var28 - AndroidUtilities.dp(var35), AndroidUtilities.dp(220.0F)) - AndroidUtilities.dp(30.0F) + var33);
                                       }

                                       this.calcBackgroundWidth(var193, var23, var13);
                                       break label2892;
                                    }

                                    if (MessageObject.isMusicDocument(var184)) {
                                       var36 = this.createDocumentLayout(this.backgroundWidth - AndroidUtilities.dp(10.0F), var1);
                                       this.mediaOffsetY = this.currentMessageObject.textHeight + AndroidUtilities.dp(8.0F) + this.linkPreviewHeight;
                                       this.totalHeight += AndroidUtilities.dp(56.0F);
                                       this.linkPreviewHeight += AndroidUtilities.dp(56.0F);
                                       var28 = var22 - AndroidUtilities.dp(86.0F);
                                       var193 = Math.max(var13, var36 + var33 + AndroidUtilities.dp(94.0F));
                                       var186 = this.songLayout;
                                       var13 = var193;
                                       if (var186 != null) {
                                          var13 = var193;
                                          if (var186.getLineCount() > 0) {
                                             var13 = (int)Math.max((float)var193, this.songLayout.getLineWidth(0) + (float)var33 + (float)AndroidUtilities.dp(86.0F));
                                          }
                                       }

                                       var186 = this.performerLayout;
                                       var193 = var13;
                                       if (var186 != null) {
                                          var193 = var13;
                                          if (var186.getLineCount() > 0) {
                                             var193 = (int)Math.max((float)var13, this.performerLayout.getLineWidth(0) + (float)var33 + (float)AndroidUtilities.dp(86.0F));
                                          }
                                       }

                                       var13 = var193;
                                       this.calcBackgroundWidth(var28, var23, var193);
                                       var193 = var28;
                                       break label2892;
                                    }

                                    this.createDocumentLayout(this.backgroundWidth - AndroidUtilities.dp(168.0F), var1);
                                    this.drawImageButton = true;
                                    if (this.drawPhotoImage) {
                                       this.totalHeight += AndroidUtilities.dp(100.0F);
                                       this.linkPreviewHeight += AndroidUtilities.dp(86.0F);
                                       this.photoImage.setImageCoords(0, this.totalHeight + this.namesOffset, AndroidUtilities.dp(86.0F), AndroidUtilities.dp(86.0F));
                                    } else {
                                       this.mediaOffsetY = this.currentMessageObject.textHeight + AndroidUtilities.dp(8.0F) + this.linkPreviewHeight;
                                       this.photoImage.setImageCoords(0, this.totalHeight + this.namesOffset - AndroidUtilities.dp(14.0F), AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
                                       this.totalHeight += AndroidUtilities.dp(64.0F);
                                       this.linkPreviewHeight += AndroidUtilities.dp(50.0F);
                                       var186 = this.docTitleLayout;
                                       if (var186 != null && var186.getLineCount() > 1) {
                                          var193 = (this.docTitleLayout.getLineCount() - 1) * AndroidUtilities.dp(16.0F);
                                          this.totalHeight += var193;
                                          this.linkPreviewHeight += var193;
                                       }
                                    }
                                 }
                              }
                           }
                        } else if (var190 != null) {
                           if (var27 != null && var27.equals("photo")) {
                              var14 = true;
                           } else {
                              var14 = false;
                           }

                           ArrayList var253 = var1.photoThumbs;
                           if (!var14 && var15) {
                              var163 = var170;
                           } else {
                              var163 = AndroidUtilities.getPhotoSize();
                           }

                           this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(var253, var163, var14 ^ true);
                           this.photoParentObject = var1.photoThumbsObject;
                           this.checkOnlyButtonPressed = var14 ^ true;
                           this.currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(var1.photoThumbs, 40);
                           if (this.currentPhotoObjectThumb == this.currentPhotoObject) {
                              this.currentPhotoObjectThumb = null;
                           }
                        } else if (var176 != null) {
                           WebFile var212 = var176;
                           var176 = var176;
                           if (!var212.mime_type.startsWith("image/")) {
                              var176 = null;
                           }

                           this.drawImageButton = false;
                        }

                        var28 = var22;
                        break label2893;
                     }

                     var28 = var193;
                  }

                  var193 = this.documentAttachType;
                  if (var193 != 5 && var193 != 3 && var193 != 1) {
                     if (this.currentPhotoObject == null && var176 == null) {
                        this.photoImage.setImageBitmap((Drawable)null);
                        this.linkPreviewHeight -= AndroidUtilities.dp(6.0F);
                        this.totalHeight += AndroidUtilities.dp(4.0F);
                     } else {
                        label4416: {
                           if (var190 == null || var15) {
                              label4394: {
                                 if (var27 != null) {
                                    if (var27.equals("photo") || var27.equals("document") && this.documentAttachType != 6 || var27.equals("gif")) {
                                       break label4394;
                                    }

                                    var193 = this.documentAttachType;
                                    if (var193 == 4 || var193 == 8) {
                                       break label4394;
                                    }
                                 }

                                 var3 = false;
                                 break label4416;
                              }
                           }

                           var3 = true;
                        }

                        this.drawImageButton = var3;
                        var193 = this.linkPreviewHeight;
                        if (var193 != 0) {
                           this.linkPreviewHeight = var193 + AndroidUtilities.dp(2.0F);
                           this.totalHeight += AndroidUtilities.dp(2.0F);
                        }

                        if (this.imageBackgroundSideColor != 0) {
                           var170 = AndroidUtilities.dp(208.0F);
                        } else {
                           label4297: {
                              var173 = this.currentPhotoObject;
                              if (var173 instanceof TLRPC.TL_photoSizeEmpty) {
                                 var193 = var173.w;
                                 if (var193 != 0) {
                                    var170 = var193;
                                    break label4297;
                                 }
                              }

                              var193 = this.documentAttachType;
                              if (var193 != 6 && var193 != 8) {
                                 if (var193 == 7) {
                                    var170 = AndroidUtilities.roundMessageSize;
                                    this.photoImage.setAllowDecodeSingleFrame(true);
                                 }
                              } else {
                                 if (AndroidUtilities.isTablet()) {
                                    var170 = AndroidUtilities.getMinTabletSide();
                                 } else {
                                    var170 = AndroidUtilities.displaySize.x;
                                 }

                                 var170 = (int)((float)var170 * 0.5F);
                              }
                           }
                        }

                        if (this.hasInvoicePreview) {
                           var193 = AndroidUtilities.dp(12.0F);
                        } else {
                           var193 = 0;
                        }

                        var36 = Math.max(var13, var170 - var193 + var33);
                        var173 = this.currentPhotoObject;
                        if (var173 != null) {
                           var173.size = -1;
                           var173 = this.currentPhotoObjectThumb;
                           if (var173 != null) {
                              var173.size = -1;
                           }
                        } else {
                           var176.size = -1;
                        }

                        if (this.imageBackgroundSideColor != 0) {
                           this.imageBackgroundSideWidth = var36 - AndroidUtilities.dp(13.0F);
                        }

                        if (!var15 && this.documentAttachType != 7) {
                           if (!this.hasGamePreview && !this.hasInvoicePreview) {
                              var173 = this.currentPhotoObject;
                              var193 = var173.w;
                              var13 = var173.h;
                              var43 = (float)var193;
                              var35 = var43 / (float)(var170 - AndroidUtilities.dp(2.0F));
                              var193 = (int)(var43 / var35);
                              var13 = (int)((float)var13 / var35);
                              if (var26 != null && (var26 == null || var26.toLowerCase().equals("instagram") || this.documentAttachType != 0)) {
                                 var163 = AndroidUtilities.displaySize.y;
                                 var170 = var13;
                                 if (var13 > var163 / 2) {
                                    var170 = var163 / 2;
                                 }
                              } else {
                                 var163 = AndroidUtilities.displaySize.y;
                                 var170 = var13;
                                 if (var13 > var163 / 3) {
                                    var170 = var163 / 3;
                                 }
                              }

                              var163 = var170;
                              var13 = var193;
                              if (this.imageBackgroundSideColor != 0) {
                                 var43 = (float)var170;
                                 var35 = var43 / (float)AndroidUtilities.dp(160.0F);
                                 var13 = (int)((float)var193 / var35);
                                 var163 = (int)(var43 / var35);
                              }

                              var170 = var163;
                              var193 = var13;
                              if (var163 < AndroidUtilities.dp(60.0F)) {
                                 var170 = AndroidUtilities.dp(60.0F);
                                 var193 = var13;
                              }
                           } else {
                              var43 = (float)640;
                              var35 = var43 / (float)(var170 - AndroidUtilities.dp(2.0F));
                              var193 = (int)(var43 / var35);
                              var170 = (int)((float)360 / var35);
                           }
                        } else {
                           var193 = var170;
                        }

                        if (this.isSmallImage) {
                           if (AndroidUtilities.dp(50.0F) + var192 > this.linkPreviewHeight) {
                              this.totalHeight += AndroidUtilities.dp(50.0F) + var192 - this.linkPreviewHeight + AndroidUtilities.dp(8.0F);
                              this.linkPreviewHeight = AndroidUtilities.dp(50.0F) + var192;
                           }

                           this.linkPreviewHeight -= AndroidUtilities.dp(8.0F);
                        } else {
                           this.totalHeight += AndroidUtilities.dp(12.0F) + var170;
                           this.linkPreviewHeight += var170;
                        }

                        if (this.documentAttachType == 8 && this.imageBackgroundSideColor == 0) {
                           this.photoImage.setImageCoords(0, 0, Math.max(var36 - AndroidUtilities.dp(13.0F), var193), var170);
                        } else {
                           this.photoImage.setImageCoords(0, 0, var193, var170);
                        }

                        this.currentPhotoFilter = String.format(Locale.US, "%d_%d", var193, var170);
                        this.currentPhotoFilterThumb = String.format(Locale.US, "%d_%d_b", var193, var170);
                        if (var176 != null) {
                           this.photoImage.setImage(ImageLocation.getForWebFile(var176), this.currentPhotoFilter, (ImageLocation)null, (String)null, var176.size, (String)null, var1, 1);
                        } else {
                           var13 = this.documentAttachType;
                           if (var13 == 8) {
                              if (var1.mediaExists) {
                                 this.photoImage.setImage(ImageLocation.getForDocument(this.documentAttach), this.currentPhotoFilter, ImageLocation.getForDocument(this.currentPhotoObject, var184), "b1", 0, "jpg", var1, 1);
                              } else {
                                 this.photoImage.setImage((ImageLocation)null, (String)null, ImageLocation.getForDocument(this.currentPhotoObject, var184), "b1", 0, "jpg", var1, 1);
                              }
                           } else if (var13 == 6) {
                              this.photoImage.setImage(ImageLocation.getForDocument(this.documentAttach), this.currentPhotoFilter, ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), "b1", this.documentAttach.size, "webp", var1, 1);
                           } else if (var13 == 4) {
                              this.photoImage.setNeedsQualityThumb(true);
                              this.photoImage.setShouldGenerateQualityThumb(true);
                              if (!SharedConfig.autoplayVideo || !this.currentMessageObject.mediaExists && (!var1.canStreamVideo() || !DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.currentMessageObject))) {
                                 if (this.currentPhotoObjectThumb != null) {
                                    this.photoImage.setImage(ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), this.currentPhotoFilter, ImageLocation.getForDocument(this.currentPhotoObjectThumb, this.documentAttach), this.currentPhotoFilterThumb, 0, (String)null, var1, 0);
                                 } else {
                                    var196 = this.photoImage;
                                    var200 = ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach);
                                    var189 = this.currentPhotoObject;
                                    if (!(var189 instanceof TLRPC.TL_photoStrippedSize) && !"s".equals(var189.type)) {
                                       var156 = this.currentPhotoFilter;
                                    } else {
                                       var156 = this.currentPhotoFilterThumb;
                                    }

                                    var196.setImage((ImageLocation)null, (String)null, var200, var156, 0, (String)null, var1, 0);
                                 }
                              } else {
                                 this.photoImage.setAllowDecodeSingleFrame(true);
                                 this.photoImage.setAllowStartAnimation(true);
                                 this.photoImage.startAnimation();
                                 this.photoImage.setImage(ImageLocation.getForDocument(this.documentAttach), "g", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), this.currentPhotoFilter, ImageLocation.getForDocument(this.currentPhotoObjectThumb, this.documentAttach), this.currentPhotoFilterThumb, (Drawable)null, this.documentAttach.size, (String)null, var1, 0);
                                 this.autoPlayingMedia = true;
                              }
                           } else if (var13 != 2 && var13 != 7) {
                              var3 = var1.mediaExists;
                              var156 = FileLoader.getAttachFileName(this.currentPhotoObject);
                              if (!this.hasGamePreview && !var3 && !DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.currentMessageObject) && !FileLoader.getInstance(this.currentAccount).isLoadingFile(var156)) {
                                 this.photoNotSet = true;
                                 var189 = this.currentPhotoObjectThumb;
                                 if (var189 != null) {
                                    this.photoImage.setImage((ImageLocation)null, (String)null, ImageLocation.getForObject(var189, this.photoParentObject), String.format(Locale.US, "%d_%d_b", var193, var170), 0, (String)null, var1, 0);
                                 } else {
                                    this.photoImage.setImageBitmap((Drawable)null);
                                 }
                              } else {
                                 this.photoNotSet = false;
                                 this.photoImage.setImage(ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject), this.currentPhotoFilter, ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject), this.currentPhotoFilterThumb, 0, (String)null, var1, 0);
                              }
                           } else {
                              this.photoImage.setAllowDecodeSingleFrame(true);
                              FileLoader.getAttachFileName(var184);
                              if (MessageObject.isRoundVideoDocument(var184)) {
                                 this.photoImage.setRoundRadius(AndroidUtilities.roundMessageSize / 2);
                                 var3 = DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.currentMessageObject);
                              } else if (MessageObject.isGifDocument(var184)) {
                                 var3 = DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.currentMessageObject);
                              } else {
                                 var3 = false;
                              }

                              var189 = this.currentPhotoObject;
                              if (!(var189 instanceof TLRPC.TL_photoStrippedSize) && !"s".equals(var189.type)) {
                                 var156 = this.currentPhotoFilter;
                              } else {
                                 var156 = this.currentPhotoFilterThumb;
                              }

                              if (!var1.mediaExists && !var3) {
                                 this.photoImage.setImage((ImageLocation)null, (String)null, ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), var156, 0, (String)null, this.currentMessageObject, 0);
                              } else {
                                 this.autoPlayingMedia = true;
                                 ImageReceiver var226 = this.photoImage;
                                 var200 = ImageLocation.getForDocument(var184);
                                 if (var184.size < 32768) {
                                    var164 = null;
                                 } else {
                                    var164 = "g";
                                 }

                                 var226.setImage(var200, var164, ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), var156, ImageLocation.getForDocument(this.currentPhotoObjectThumb, this.documentAttach), this.currentPhotoFilterThumb, (Drawable)null, var184.size, (String)null, var1, 0);
                              }
                           }
                        }

                        this.drawPhotoImage = true;
                        if (var27 != null && var27.equals("video") && var31 != 0) {
                           var170 = var31 / 60;
                           var156 = String.format("%d:%02d", var170, var31 - var170 * 60);
                           this.durationWidth = (int)Math.ceil((double)Theme.chat_durationPaint.measureText(var156));
                           this.videoInfoLayout = new StaticLayout(var156, Theme.chat_durationPaint, this.durationWidth, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                        } else if (this.hasGamePreview) {
                           var156 = LocaleController.getString("AttachGame", 2131558715).toUpperCase();
                           this.durationWidth = (int)Math.ceil((double)Theme.chat_gamePaint.measureText(var156));
                           this.videoInfoLayout = new StaticLayout(var156, Theme.chat_gamePaint, this.durationWidth, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                        }

                        var13 = var36;
                     }

                     var170 = var13;
                     if (this.hasInvoicePreview) {
                        var155 = var1.messageOwner.media;
                        if ((var155.flags & 4) != 0) {
                           var156 = LocaleController.getString("PaymentReceipt", 2131560388).toUpperCase();
                        } else if (var155.test) {
                           var156 = LocaleController.getString("PaymentTestInvoice", 2131560406).toUpperCase();
                        } else {
                           var156 = LocaleController.getString("PaymentInvoice", 2131560375).toUpperCase();
                        }

                        LocaleController var202 = LocaleController.getInstance();
                        var157 = var1.messageOwner.media;
                        var164 = var202.formatCurrencyString(var157.total_amount, var157.currency);
                        var206 = new StringBuilder();
                        var206.append(var164);
                        var206.append(" ");
                        var206.append(var156);
                        SpannableStringBuilder var197 = new SpannableStringBuilder(var206.toString());
                        var197.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), 0, var164.length(), 33);
                        this.durationWidth = (int)Math.ceil((double)Theme.chat_shipmentPaint.measureText(var197, 0, var197.length()));
                        this.videoInfoLayout = new StaticLayout(var197, Theme.chat_shipmentPaint, this.durationWidth + AndroidUtilities.dp(10.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                        var170 = var13;
                        if (!this.drawPhotoImage) {
                           this.totalHeight += AndroidUtilities.dp(6.0F);
                           var193 = this.timeWidth;
                           if (var1.isOutOwner()) {
                              var267 = 20;
                           } else {
                              var267 = 0;
                           }

                           var170 = var193 + AndroidUtilities.dp((float)(var267 + 14));
                           var193 = this.durationWidth;
                           if (var193 + var170 > var28) {
                              var170 = Math.max(var193, var13);
                              this.totalHeight += AndroidUtilities.dp(12.0F);
                           } else {
                              var170 = Math.max(var193 + var170, var13);
                           }
                        }
                     }

                     if (this.hasGamePreview) {
                        var13 = var1.textHeight;
                        if (var13 != 0) {
                           this.linkPreviewHeight += var13 + AndroidUtilities.dp(6.0F);
                           this.totalHeight += AndroidUtilities.dp(4.0F);
                        }
                     }

                     this.calcBackgroundWidth(var28, var23, var170);
                  }

                  this.createInstantViewButton();
               }
            } else {
               var35 = 102.0F;
               if (var170 == 16) {
                  this.drawName = false;
                  this.drawForwardedName = false;
                  this.drawPhotoImage = false;
                  if (AndroidUtilities.isTablet()) {
                     var170 = AndroidUtilities.getMinTabletSide();
                     if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                        var35 = 50.0F;
                     }

                     this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(270.0F));
                  } else {
                     var170 = AndroidUtilities.displaySize.x;
                     if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                        var35 = 50.0F;
                     }

                     this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(270.0F));
                  }

                  this.availableTimeWidth = this.backgroundWidth - AndroidUtilities.dp(31.0F);
                  var13 = this.getMaxNameWidth() - AndroidUtilities.dp(50.0F);
                  var170 = var13;
                  if (var13 < 0) {
                     var170 = AndroidUtilities.dp(10.0F);
                  }

                  long var280 = (long)var1.messageOwner.date;
                  var179 = LocaleController.getInstance().formatterDay.format(var280 * 1000L);
                  TLRPC.TL_messageActionPhoneCall var217 = (TLRPC.TL_messageActionPhoneCall)var1.messageOwner.action;
                  var3 = var217.reason instanceof TLRPC.TL_phoneCallDiscardReasonMissed;
                  if (var1.isOutOwner()) {
                     if (var3) {
                        var156 = LocaleController.getString("CallMessageOutgoingMissed", 2131558877);
                     } else {
                        var156 = LocaleController.getString("CallMessageOutgoing", 2131558876);
                     }
                  } else if (var3) {
                     var156 = LocaleController.getString("CallMessageIncomingMissed", 2131558875);
                  } else if (var217.reason instanceof TLRPC.TL_phoneCallDiscardReasonBusy) {
                     var156 = LocaleController.getString("CallMessageIncomingDeclined", 2131558874);
                  } else {
                     var156 = LocaleController.getString("CallMessageIncoming", 2131558873);
                  }

                  var164 = var179;
                  if (var217.duration > 0) {
                     var198 = new StringBuilder();
                     var198.append(var179);
                     var198.append(", ");
                     var198.append(LocaleController.formatCallDuration(var217.duration));
                     var164 = var198.toString();
                  }

                  TextPaint var208 = Theme.chat_audioTitlePaint;
                  var35 = (float)var170;
                  this.titleLayout = new StaticLayout(TextUtils.ellipsize(var156, var208, var35, TruncateAt.END), Theme.chat_audioTitlePaint, var170 + AndroidUtilities.dp(2.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                  this.docTitleLayout = new StaticLayout(TextUtils.ellipsize(var164, Theme.chat_contactPhonePaint, var35, TruncateAt.END), Theme.chat_contactPhonePaint, var170 + AndroidUtilities.dp(2.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                  this.setMessageObjectInternal(var1);
                  var170 = AndroidUtilities.dp(65.0F);
                  var13 = this.namesOffset;
                  this.totalHeight = var170 + var13;
                  if (this.drawPinnedTop) {
                     this.namesOffset = var13 - AndroidUtilities.dp(1.0F);
                  }
               } else {
                  ImageReceiver var209;
                  ImageLocation var219;
                  if (var170 == 12) {
                     this.drawName = false;
                     this.drawForwardedName = true;
                     this.drawPhotoImage = true;
                     this.photoImage.setRoundRadius(AndroidUtilities.dp(22.0F));
                     if (AndroidUtilities.isTablet()) {
                        var170 = AndroidUtilities.getMinTabletSide();
                        if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                           var35 = 50.0F;
                        }

                        this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(270.0F));
                     } else {
                        var170 = AndroidUtilities.displaySize.x;
                        if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                           var35 = 50.0F;
                        }

                        this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(270.0F));
                     }

                     this.availableTimeWidth = this.backgroundWidth - AndroidUtilities.dp(31.0F);
                     var170 = var1.messageOwner.media.user_id;
                     TLRPC.User var204 = MessagesController.getInstance(this.currentAccount).getUser(var170);
                     var13 = this.getMaxNameWidth() - AndroidUtilities.dp(80.0F);
                     var170 = var13;
                     if (var13 < 0) {
                        var170 = AndroidUtilities.dp(10.0F);
                     }

                     if (var204 != null) {
                        this.contactAvatarDrawable.setInfo(var204);
                     }

                     var209 = this.photoImage;
                     var219 = ImageLocation.getForUser(var204, false);
                     Object var207;
                     if (var204 != null) {
                        var207 = this.contactAvatarDrawable;
                     } else {
                        var207 = Theme.chat_contactDrawable[var1.isOutOwner()];
                     }

                     var209.setImage(var219, "50_50", (Drawable)var207, (String)null, var1, 0);
                     if (!TextUtils.isEmpty(var1.vCardData)) {
                        var207 = var1.vCardData;
                        this.drawInstantView = true;
                        this.drawInstantViewType = 5;
                     } else if (var204 != null && !TextUtils.isEmpty(var204.phone)) {
                        PhoneFormat var214 = PhoneFormat.getInstance();
                        var206 = new StringBuilder();
                        var206.append("+");
                        var206.append(var204.phone);
                        var207 = var214.format(var206.toString());
                     } else {
                        var156 = var1.messageOwner.media.phone_number;
                        if (!TextUtils.isEmpty(var156)) {
                           var207 = PhoneFormat.getInstance().format(var156);
                        } else {
                           var207 = LocaleController.getString("NumberUnknown", 2131560096);
                        }
                     }

                     var157 = var1.messageOwner.media;
                     var179 = ContactsController.formatName(var157.first_name, var157.last_name).replace('\n', ' ');
                     var164 = var179;
                     if (var179.length() == 0) {
                        var179 = var1.messageOwner.media.phone_number;
                        var164 = var179;
                        if (var179 == null) {
                           var164 = "";
                        }
                     }

                     label2471: {
                        this.titleLayout = new StaticLayout(TextUtils.ellipsize(var164, Theme.chat_contactNamePaint, (float)var170, TruncateAt.END), Theme.chat_contactNamePaint, var170 + AndroidUtilities.dp(4.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                        this.docTitleLayout = new StaticLayout((CharSequence)var207, Theme.chat_contactPhonePaint, var170 + AndroidUtilities.dp(2.0F), Alignment.ALIGN_NORMAL, 1.0F, (float)AndroidUtilities.dp(1.0F), false);
                        this.setMessageObjectInternal(var1);
                        if (this.drawForwardedName && var1.needDrawForwarded()) {
                           var151 = this.currentPosition;
                           if (var151 == null || var151.minY == 0) {
                              this.namesOffset += AndroidUtilities.dp(5.0F);
                              break label2471;
                           }
                        }

                        if (this.drawNameLayout && var1.messageOwner.reply_to_msg_id == 0) {
                           this.namesOffset += AndroidUtilities.dp(7.0F);
                        }
                     }

                     this.totalHeight = AndroidUtilities.dp(55.0F) + this.namesOffset + this.docTitleLayout.getHeight();
                     if (this.drawPinnedTop) {
                        this.namesOffset -= AndroidUtilities.dp(1.0F);
                     }

                     if (this.drawInstantView) {
                        this.createInstantViewButton();
                     } else if (this.docTitleLayout.getLineCount() > 0) {
                        var13 = this.backgroundWidth;
                        var170 = AndroidUtilities.dp(110.0F);
                        var224 = this.docTitleLayout;
                        if (var13 - var170 - (int)Math.ceil((double)var224.getLineWidth(var224.getLineCount() - 1)) < this.timeWidth) {
                           this.totalHeight += AndroidUtilities.dp(8.0F);
                        }
                     }
                  } else if (var170 == 2) {
                     this.drawForwardedName = true;
                     if (AndroidUtilities.isTablet()) {
                        var170 = AndroidUtilities.getMinTabletSide();
                        if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                           var35 = 50.0F;
                        }

                        this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(270.0F));
                     } else {
                        var170 = AndroidUtilities.displaySize.x;
                        if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                           var35 = 50.0F;
                        }

                        this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(270.0F));
                     }

                     this.createDocumentLayout(this.backgroundWidth, var1);
                     this.setMessageObjectInternal(var1);
                     var13 = AndroidUtilities.dp(70.0F);
                     var170 = this.namesOffset;
                     this.totalHeight = var13 + var170;
                     if (this.drawPinnedTop) {
                        this.namesOffset = var170 - AndroidUtilities.dp(1.0F);
                     }
                  } else if (var170 == 14) {
                     if (AndroidUtilities.isTablet()) {
                        var170 = AndroidUtilities.getMinTabletSide();
                        if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                           var35 = 50.0F;
                        }

                        this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(270.0F));
                     } else {
                        var170 = AndroidUtilities.displaySize.x;
                        if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                           var35 = 50.0F;
                        }

                        this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(270.0F));
                     }

                     this.createDocumentLayout(this.backgroundWidth, var1);
                     this.setMessageObjectInternal(var1);
                     var170 = AndroidUtilities.dp(82.0F);
                     var13 = this.namesOffset;
                     this.totalHeight = var170 + var13;
                     if (this.drawPinnedTop) {
                        this.namesOffset = var13 - AndroidUtilities.dp(1.0F);
                     }
                  } else {
                     ArrayList var245;
                     if (var170 != 17) {
                        if (var1.messageOwner.fwd_from != null && !var1.isAnyKindOfSticker()) {
                           var3 = true;
                        } else {
                           var3 = false;
                        }

                        this.drawForwardedName = var3;
                        if (var1.type != 9) {
                           var3 = true;
                        } else {
                           var3 = false;
                        }

                        this.mediaBackground = var3;
                        this.drawImageButton = true;
                        this.drawPhotoImage = true;
                        if (var1.gifState != 2.0F && !SharedConfig.autoplayGifs) {
                           var170 = var1.type;
                           if (var170 == 8 || var170 == 5) {
                              var1.gifState = 1.0F;
                           }
                        }

                        this.photoImage.setAllowDecodeSingleFrame(true);
                        if (var1.isVideo()) {
                           this.photoImage.setAllowStartAnimation(true);
                        } else if (var1.isRoundVideo()) {
                           var6 = MediaController.getInstance().getPlayingMessageObject();
                           var165 = this.photoImage;
                           if (var6 != null && var6.isRoundVideo()) {
                              var3 = false;
                           } else {
                              var3 = true;
                           }

                           var165.setAllowStartAnimation(var3);
                        } else {
                           var165 = this.photoImage;
                           if (var1.gifState == 0.0F) {
                              var3 = true;
                           } else {
                              var3 = false;
                           }

                           var165.setAllowStartAnimation(var3);
                        }

                        label3985: {
                           this.photoImage.setForcePreview(var1.needDrawBluredPreview());
                           var170 = var1.type;
                           byte var249;
                           if (var170 == 9) {
                              if (AndroidUtilities.isTablet()) {
                                 var170 = AndroidUtilities.getMinTabletSide();
                                 if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                                    var35 = 50.0F;
                                 }

                                 this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(300.0F));
                              } else {
                                 var170 = AndroidUtilities.displaySize.x;
                                 if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                                    var35 = 50.0F;
                                 }

                                 this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(300.0F));
                              }

                              if (this.checkNeedDrawShareButton(var1)) {
                                 this.backgroundWidth -= AndroidUtilities.dp(20.0F);
                              }

                              var192 = this.backgroundWidth - AndroidUtilities.dp(138.0F);
                              this.createDocumentLayout(var192, var1);
                              if (!TextUtils.isEmpty(var1.caption)) {
                                 label3963: {
                                    label4199: {
                                       try {
                                          this.currentCaption = var5.caption;
                                          var170 = this.backgroundWidth;
                                          var193 = AndroidUtilities.dp(31.0F);
                                          var13 = AndroidUtilities.dp(10.0F);
                                       } catch (Exception var149) {
                                          var254 = var149;
                                          var193 = 0;
                                          break label4199;
                                       }

                                       var193 = var170 - var193 - var13;

                                       label3960: {
                                          try {
                                             if (VERSION.SDK_INT >= 24) {
                                                this.captionLayout = Builder.obtain(var5.caption, 0, var5.caption.length(), Theme.chat_msgTextPaint, var193).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Alignment.ALIGN_NORMAL).build();
                                                break label3963;
                                             }
                                          } catch (Exception var150) {
                                             var10000 = var150;
                                             var10001 = false;
                                             break label3960;
                                          }

                                          try {
                                             var224 = new StaticLayout(var5.caption, Theme.chat_msgTextPaint, var193, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                                             this.captionLayout = var224;
                                             break label3963;
                                          } catch (Exception var148) {
                                             var10000 = var148;
                                             var10001 = false;
                                          }
                                       }

                                       var254 = var10000;
                                    }

                                    FileLog.e((Throwable)var254);
                                 }
                              } else {
                                 var193 = 0;
                              }

                              var224 = this.docTitleLayout;
                              if (var224 != null) {
                                 var28 = var224.getLineCount();
                                 var163 = 0;
                                 var170 = 0;

                                 while(true) {
                                    var13 = var170;
                                    if (var163 >= var28) {
                                       break;
                                    }

                                    var36 = (int)Math.ceil((double)(this.docTitleLayout.getLineWidth(var163) + this.docTitleLayout.getLineLeft(var163)));
                                    if (this.drawPhotoImage) {
                                       var247 = 52;
                                    } else {
                                       var247 = 22;
                                    }

                                    var170 = Math.max(var170, var36 + AndroidUtilities.dp((float)(var247 + 86)));
                                    ++var163;
                                 }
                              } else {
                                 var13 = 0;
                              }

                              var224 = this.infoLayout;
                              var170 = var13;
                              if (var224 != null) {
                                 var28 = var224.getLineCount();
                                 var163 = 0;

                                 while(true) {
                                    var170 = var13;
                                    if (var163 >= var28) {
                                       break;
                                    }

                                    var36 = (int)Math.ceil((double)this.infoLayout.getLineWidth(var163));
                                    if (this.drawPhotoImage) {
                                       var267 = 52;
                                    } else {
                                       var267 = 22;
                                    }

                                    var13 = Math.max(var13, var36 + AndroidUtilities.dp((float)(var267 + 86)));
                                    ++var163;
                                 }
                              }

                              var224 = this.captionLayout;
                              var163 = var170;
                              if (var224 != null) {
                                 var36 = var224.getLineCount();
                                 var13 = 0;

                                 while(true) {
                                    var163 = var170;
                                    if (var13 >= var36) {
                                       break;
                                    }

                                    var28 = (int)Math.ceil((double)Math.min((float)var193, this.captionLayout.getLineWidth(var13) + this.captionLayout.getLineLeft(var13))) + AndroidUtilities.dp(31.0F);
                                    var163 = var170;
                                    if (var28 > var170) {
                                       var163 = var28;
                                    }

                                    ++var13;
                                    var170 = var163;
                                 }
                              }

                              var193 = var192;
                              if (var163 > 0) {
                                 this.backgroundWidth = var163;
                                 var193 = var163 - AndroidUtilities.dp(31.0F);
                              }

                              if (this.drawPhotoImage) {
                                 var13 = AndroidUtilities.dp(86.0F);
                                 var170 = AndroidUtilities.dp(86.0F);
                              } else {
                                 var163 = AndroidUtilities.dp(56.0F);
                                 var192 = AndroidUtilities.dp(56.0F);
                                 var224 = this.docTitleLayout;
                                 var13 = var163;
                                 var170 = var192;
                                 if (var224 != null) {
                                    var13 = var163;
                                    var170 = var192;
                                    if (var224.getLineCount() > 1) {
                                       var170 = var192 + (this.docTitleLayout.getLineCount() - 1) * AndroidUtilities.dp(16.0F);
                                       var13 = var163;
                                    }
                                 }
                              }

                              this.availableTimeWidth = var193;
                              var193 = var170;
                              if (!this.drawPhotoImage) {
                                 var193 = var170;
                                 if (TextUtils.isEmpty(var1.caption)) {
                                    var224 = this.infoLayout;
                                    var193 = var170;
                                    if (var224 != null) {
                                       label4313: {
                                          var163 = var224.getLineCount();
                                          this.measureTime(var1);
                                          if (this.backgroundWidth - AndroidUtilities.dp(122.0F) - (int)Math.ceil((double)this.infoLayout.getLineWidth(0)) < this.timeWidth) {
                                             var193 = AndroidUtilities.dp(12.0F);
                                          } else {
                                             var193 = var170;
                                             if (var163 != 1) {
                                                break label4313;
                                             }

                                             var193 = AndroidUtilities.dp(4.0F);
                                          }

                                          var193 += var170;
                                       }
                                    }
                                 }
                              }

                              var170 = var193;
                           } else {
                              float var46;
                              if (var170 == 4) {
                                 label3895: {
                                    var157 = var1.messageOwner.media;
                                    TLRPC.GeoPoint var273 = var157.geo;
                                    double var44 = var273.lat;
                                    var18 = var273._long;
                                    if (var157 instanceof TLRPC.TL_messageMediaGeoLive) {
                                       if (AndroidUtilities.isTablet()) {
                                          var170 = AndroidUtilities.getMinTabletSide();
                                          if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                                             var35 = 50.0F;
                                          }

                                          this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(289.0F));
                                       } else {
                                          var170 = AndroidUtilities.displaySize.x;
                                          if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                                             var35 = 50.0F;
                                          }

                                          this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(289.0F));
                                       }

                                       this.backgroundWidth -= AndroidUtilities.dp(4.0F);
                                       if (this.checkNeedDrawShareButton(var1)) {
                                          this.backgroundWidth -= AndroidUtilities.dp(20.0F);
                                       }

                                       var170 = this.backgroundWidth - AndroidUtilities.dp(37.0F);
                                       this.availableTimeWidth = var170;
                                       var13 = var170 - AndroidUtilities.dp(54.0F);
                                       var193 = this.backgroundWidth - AndroidUtilities.dp(17.0F);
                                       var163 = AndroidUtilities.dp(195.0F);
                                       var16 = (double)268435456;
                                       Double.isNaN(var16);
                                       var20 = var16 / 3.141592653589793D;
                                       var44 = var44 * 3.141592653589793D / 180.0D;
                                       var44 = Math.log((Math.sin(var44) + 1.0D) / (1.0D - Math.sin(var44))) * var20 / 2.0D;
                                       Double.isNaN(var16);
                                       var44 = (double)(Math.round(var16 - var44) - (long)(AndroidUtilities.dp(10.3F) << 6));
                                       Double.isNaN(var44);
                                       Double.isNaN(var16);
                                       var16 = (1.5707963267948966D - Math.atan(Math.exp((var44 - var16) / var20)) * 2.0D) * 180.0D / 3.141592653589793D;
                                       var192 = this.currentAccount;
                                       var35 = (float)var193;
                                       var46 = AndroidUtilities.density;
                                       var170 = (int)(var35 / var46);
                                       var43 = (float)var163;
                                       this.currentUrl = AndroidUtilities.formapMapUrl(var192, var16, var18, var170, (int)(var43 / var46), false, 15);
                                       long var47 = var273.access_hash;
                                       var46 = AndroidUtilities.density;
                                       this.currentWebFile = WebFile.createWithGeoPoint(var16, var18, var47, (int)(var35 / var46), (int)(var43 / var46), 15, Math.min(2, (int)Math.ceil((double)var46)));
                                       var3 = this.isCurrentLocationTimeExpired(var1);
                                       this.locationExpired = var3;
                                       if (!var3) {
                                          this.photoImage.setCrossfadeWithOldImage(true);
                                          this.mediaBackground = false;
                                          var170 = AndroidUtilities.dp(56.0F);
                                          AndroidUtilities.runOnUIThread(this.invalidateRunnable, 1000L);
                                          this.scheduledInvalidate = true;
                                       } else {
                                          this.backgroundWidth -= AndroidUtilities.dp(9.0F);
                                          var170 = 0;
                                       }

                                       this.docTitleLayout = new StaticLayout(TextUtils.ellipsize(LocaleController.getString("AttachLiveLocation", 2131558721), Theme.chat_locationTitlePaint, (float)var13, TruncateAt.END), Theme.chat_locationTitlePaint, var13, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                                       this.updateCurrentUserAndChat();
                                       TLRPC.User var274 = this.currentUser;
                                       if (var274 != null) {
                                          this.contactAvatarDrawable.setInfo(var274);
                                          this.locationImageReceiver.setImage(ImageLocation.getForUser(this.currentUser, false), "50_50", this.contactAvatarDrawable, (String)null, this.currentUser, 0);
                                       } else {
                                          TLRPC.Chat var275 = this.currentChat;
                                          if (var275 != null) {
                                             TLRPC.ChatPhoto var276 = var275.photo;
                                             if (var276 != null) {
                                                this.currentPhoto = var276.photo_small;
                                             }

                                             this.contactAvatarDrawable.setInfo(this.currentChat);
                                             this.locationImageReceiver.setImage(ImageLocation.getForChat(this.currentChat, false), "50_50", this.contactAvatarDrawable, (String)null, this.currentChat, 0);
                                          } else {
                                             this.locationImageReceiver.setImage((ImageLocation)null, (String)null, this.contactAvatarDrawable, (String)null, (Object)null, 0);
                                          }
                                       }

                                       var152 = var1.messageOwner;
                                       var192 = var152.edit_date;
                                       if (var192 != 0) {
                                          var47 = (long)var192;
                                       } else {
                                          var47 = (long)var152.date;
                                       }

                                       this.infoLayout = new StaticLayout(LocaleController.formatLocationUpdateDate(var47), Theme.chat_locationAddressPaint, var13, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                                       var13 = var170;
                                    } else {
                                       if (TextUtils.isEmpty(var157.title)) {
                                          if (AndroidUtilities.isTablet()) {
                                             var170 = AndroidUtilities.getMinTabletSide();
                                             if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                                                var35 = 50.0F;
                                             }

                                             this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(289.0F));
                                          } else {
                                             var170 = AndroidUtilities.displaySize.x;
                                             if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                                                var35 = 50.0F;
                                             }

                                             this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(289.0F));
                                          }

                                          this.backgroundWidth -= AndroidUtilities.dp(4.0F);
                                          if (this.checkNeedDrawShareButton(var1)) {
                                             this.backgroundWidth -= AndroidUtilities.dp(20.0F);
                                          }

                                          this.availableTimeWidth = this.backgroundWidth - AndroidUtilities.dp(34.0F);
                                          var193 = this.backgroundWidth - AndroidUtilities.dp(8.0F);
                                          var170 = AndroidUtilities.dp(195.0F);
                                          var13 = this.currentAccount;
                                          var35 = (float)var193;
                                          var46 = AndroidUtilities.density;
                                          var163 = (int)(var35 / var46);
                                          var43 = (float)var170;
                                          this.currentUrl = AndroidUtilities.formapMapUrl(var13, var44, var18, var163, (int)(var43 / var46), true, 15);
                                          var46 = AndroidUtilities.density;
                                          this.currentWebFile = WebFile.createWithGeoPoint(var273, (int)(var35 / var46), (int)(var43 / var46), 15, Math.min(2, (int)Math.ceil((double)var46)));
                                          var13 = 0;
                                          break label3895;
                                       }

                                       if (AndroidUtilities.isTablet()) {
                                          var170 = AndroidUtilities.getMinTabletSide();
                                          if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                                             var35 = 50.0F;
                                          }

                                          this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(289.0F));
                                       } else {
                                          var170 = AndroidUtilities.displaySize.x;
                                          if (!this.isChat || !var1.needDrawAvatar() || var1.isOutOwner()) {
                                             var35 = 50.0F;
                                          }

                                          this.backgroundWidth = Math.min(var170 - AndroidUtilities.dp(var35), AndroidUtilities.dp(289.0F));
                                       }

                                       this.backgroundWidth -= AndroidUtilities.dp(4.0F);
                                       if (this.checkNeedDrawShareButton(var1)) {
                                          this.backgroundWidth -= AndroidUtilities.dp(20.0F);
                                       }

                                       var170 = this.backgroundWidth - AndroidUtilities.dp(34.0F);
                                       this.availableTimeWidth = var170;
                                       var193 = this.backgroundWidth - AndroidUtilities.dp(17.0F);
                                       var163 = AndroidUtilities.dp(195.0F);
                                       this.mediaBackground = false;
                                       var13 = this.currentAccount;
                                       var43 = (float)var193;
                                       var46 = AndroidUtilities.density;
                                       var192 = (int)(var43 / var46);
                                       var35 = (float)var163;
                                       this.currentUrl = AndroidUtilities.formapMapUrl(var13, var44, var18, var192, (int)(var35 / var46), true, 15);
                                       var46 = AndroidUtilities.density;
                                       this.currentWebFile = WebFile.createWithGeoPoint(var273, (int)(var43 / var46), (int)(var35 / var46), 15, Math.min(2, (int)Math.ceil((double)var46)));
                                       this.docTitleLayout = StaticLayoutEx.createStaticLayout(var1.messageOwner.media.title, Theme.chat_locationTitlePaint, var170, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false, TruncateAt.END, var170, 1);
                                       var192 = 0 + AndroidUtilities.dp(50.0F);
                                       this.docTitleLayout.getLineCount();
                                       if (!TextUtils.isEmpty(var1.messageOwner.media.address)) {
                                          this.infoLayout = StaticLayoutEx.createStaticLayout(var1.messageOwner.media.address, Theme.chat_locationAddressPaint, var170, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false, TruncateAt.END, var170, 1);
                                          this.measureTime(var1);
                                          var28 = this.backgroundWidth;
                                          var23 = (int)Math.ceil((double)this.infoLayout.getLineWidth(0));
                                          var22 = AndroidUtilities.dp(24.0F);
                                          var36 = this.timeWidth;
                                          if (var1.isOutOwner()) {
                                             var247 = 20;
                                          } else {
                                             var247 = 0;
                                          }

                                          var170 = var192;
                                          if (var28 - var23 - var22 < var36 + AndroidUtilities.dp((float)(var247 + 20))) {
                                             var170 = var192 + AndroidUtilities.dp(8.0F);
                                          }
                                       } else {
                                          this.infoLayout = null;
                                          var170 = var192;
                                       }

                                       var13 = var170;
                                    }

                                    var170 = var163;
                                 }

                                 if ((int)var1.getDialogId() == 0) {
                                    var163 = SharedConfig.mapPreviewType;
                                    if (var163 == 0) {
                                       this.currentMapProvider = 2;
                                    } else if (var163 == 1) {
                                       this.currentMapProvider = 1;
                                    } else {
                                       this.currentMapProvider = -1;
                                    }
                                 } else {
                                    this.currentMapProvider = MessagesController.getInstance(var1.currentAccount).mapProvider;
                                    if (this.currentMapProvider != -1) {
                                       this.currentMapProvider = 2;
                                    }
                                 }

                                 var163 = this.currentMapProvider;
                                 if (var163 == -1) {
                                    this.photoImage.setImage((ImageLocation)null, (String)null, Theme.chat_locationDrawable[var1.isOutOwner()], (String)null, var1, 0);
                                 } else if (var163 == 2) {
                                    var176 = this.currentWebFile;
                                    if (var176 != null) {
                                       this.photoImage.setImage(ImageLocation.getForWebFile(var176), (String)null, Theme.chat_locationDrawable[var1.isOutOwner()], (String)null, var1, 0);
                                    }
                                 } else {
                                    if (var163 == 3 || var163 == 4) {
                                       ImageLoader.getInstance().addTestWebFile(this.currentUrl, this.currentWebFile);
                                       this.addedForTest = true;
                                    }

                                    var156 = this.currentUrl;
                                    if (var156 != null) {
                                       this.photoImage.setImage(var156, (String)null, Theme.chat_locationDrawable[var1.isOutOwner()], (String)null, 0);
                                    }
                                 }

                                 var163 = var13;
                                 var247 = 0;
                                 break label3985;
                              }

                              ImageLocation var236;
                              TLRPC.DocumentAttribute var260;
                              TLRPC.Document var263;
                              if (!var1.isAnyKindOfSticker()) {
                                 label3831: {
                                    this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(var1.photoThumbs, AndroidUtilities.getPhotoSize());
                                    this.photoParentObject = var1.photoThumbsObject;
                                    if (var1.type == 5) {
                                       var170 = AndroidUtilities.roundMessageSize;
                                       this.documentAttach = var1.getDocument();
                                       this.documentAttachType = 7;
                                    } else {
                                       if (AndroidUtilities.isTablet()) {
                                          var170 = AndroidUtilities.getMinTabletSide();
                                       } else {
                                          if (this.currentPhotoObject != null) {
                                             var170 = var1.type;
                                             if (var170 == 1 || var170 == 3 || var170 == 8) {
                                                var189 = this.currentPhotoObject;
                                                if (var189.w >= var189.h) {
                                                   var154 = AndroidUtilities.displaySize;
                                                   var170 = Math.min(var154.x, var154.y) - AndroidUtilities.dp(64.0F);
                                                   var182 = true;
                                                   break label3831;
                                                }
                                             }
                                          }

                                          var154 = AndroidUtilities.displaySize;
                                          var170 = Math.min(var154.x, var154.y);
                                       }

                                       var170 = (int)((float)var170 * 0.7F);
                                    }

                                    var182 = false;
                                 }

                                 var28 = AndroidUtilities.dp(100.0F) + var170;
                                 if (var182) {
                                    if (this.isChat && var1.needDrawAvatar() && !var1.isOutOwner()) {
                                       var13 = AndroidUtilities.dp(52.0F);
                                       var163 = var170;
                                       var192 = var170 - var13;
                                       var193 = var28;
                                    } else {
                                       var163 = var170;
                                       var193 = var28;
                                       var192 = var170;
                                    }
                                 } else {
                                    if (var1.type != 5 && this.checkNeedDrawShareButton(var1)) {
                                       var13 = var170 - AndroidUtilities.dp(20.0F);
                                       var193 = var170 - AndroidUtilities.dp(20.0F);
                                    } else {
                                       var13 = var170;
                                       var193 = var170;
                                    }

                                    var170 = var193;
                                    if (var193 > AndroidUtilities.getPhotoSize()) {
                                       var170 = AndroidUtilities.getPhotoSize();
                                    }

                                    var192 = var170;
                                    var163 = var13;
                                    var193 = var28;
                                    if (var28 > AndroidUtilities.getPhotoSize()) {
                                       var193 = AndroidUtilities.getPhotoSize();
                                       var192 = var170;
                                       var163 = var13;
                                    }
                                 }

                                 label4379: {
                                    var170 = var1.type;
                                    if (var170 == 1) {
                                       this.updateSecretTimeText(var1);
                                       this.currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(var1.photoThumbs, 40);
                                    } else {
                                       label4440: {
                                          if (var170 != 3 && var170 != 8) {
                                             if (var170 != 5) {
                                                break label4440;
                                             }

                                             this.currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(var1.photoThumbs, 40);
                                          } else {
                                             this.createDocumentLayout(0, var1);
                                             this.currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(var1.photoThumbs, 40);
                                             this.updateSecretTimeText(var1);
                                          }

                                          var265 = true;
                                          break label4379;
                                       }
                                    }

                                    var265 = false;
                                 }

                                 if (var1.type == 5) {
                                    var170 = AndroidUtilities.roundMessageSize;
                                    var13 = var170;
                                 } else {
                                    var189 = this.currentPhotoObject;
                                    if (var189 == null) {
                                       var189 = this.currentPhotoObjectThumb;
                                    }

                                    if (var189 != null) {
                                       var170 = var189.w;
                                       var13 = var189.h;
                                    } else {
                                       var263 = this.documentAttach;
                                       if (var263 != null) {
                                          var22 = var263.attributes.size();
                                          var28 = 0;
                                          var13 = 0;

                                          for(var170 = 0; var28 < var22; ++var28) {
                                             var260 = (TLRPC.DocumentAttribute)this.documentAttach.attributes.get(var28);
                                             if (var260 instanceof TLRPC.TL_documentAttributeVideo) {
                                                var170 = var260.w;
                                                var13 = var260.h;
                                             }
                                          }
                                       } else {
                                          var13 = 0;
                                          var170 = 0;
                                       }
                                    }

                                    var43 = (float)var170;
                                    var35 = (float)var192;
                                    var46 = var43 / var35;
                                    var28 = (int)(var43 / var46);
                                    float var49 = (float)var13;
                                    var13 = (int)(var49 / var46);
                                    var170 = var28;
                                    if (var28 == 0) {
                                       var170 = AndroidUtilities.dp(150.0F);
                                    }

                                    var28 = var13;
                                    if (var13 == 0) {
                                       var28 = AndroidUtilities.dp(150.0F);
                                    }

                                    if (var28 > var193) {
                                       var35 = (float)var28 / (float)var193;
                                       var170 = (int)((float)var170 / var35);
                                       var13 = var193;
                                    } else {
                                       var13 = var28;
                                       if (var28 < AndroidUtilities.dp(120.0F)) {
                                          var28 = AndroidUtilities.dp(120.0F);
                                          var43 /= var49 / (float)var28;
                                          var13 = var28;
                                          if (var43 < var35) {
                                             var170 = (int)var43;
                                             var13 = var28;
                                          }
                                       }
                                    }
                                 }

                                 var189 = this.currentPhotoObject;
                                 if (var189 != null && "s".equals(var189.type)) {
                                    this.currentPhotoObject = null;
                                 }

                                 var189 = this.currentPhotoObject;
                                 if (var189 != null && var189 == this.currentPhotoObjectThumb) {
                                    if (var1.type == 1) {
                                       this.currentPhotoObjectThumb = null;
                                    } else {
                                       this.currentPhotoObject = null;
                                    }
                                 }

                                 if (var265 && !var1.needDrawBluredPreview()) {
                                    var189 = this.currentPhotoObject;
                                    if (var189 == null || var189 == this.currentPhotoObjectThumb) {
                                       var189 = this.currentPhotoObjectThumb;
                                       if (var189 == null || !"m".equals(var189.type)) {
                                          this.photoImage.setNeedsQualityThumb(true);
                                          this.photoImage.setShouldGenerateQualityThumb(true);
                                       }
                                    }
                                 }

                                 if (this.currentMessagesGroup == null && var1.caption != null) {
                                    this.mediaBackground = false;
                                 }

                                 label4441: {
                                    if ((var170 == 0 || var13 == 0) && var1.type == 8) {
                                       for(var28 = 0; var28 < var5.messageOwner.media.document.attributes.size(); ++var28) {
                                          var260 = (TLRPC.DocumentAttribute)var5.messageOwner.media.document.attributes.get(var28);
                                          if (var260 instanceof TLRPC.TL_documentAttributeImageSize || var260 instanceof TLRPC.TL_documentAttributeVideo) {
                                             var170 = var260.w;
                                             var43 = (float)var170;
                                             var35 = (float)var192;
                                             var43 /= var35;
                                             var192 = (int)((float)var170 / var43);
                                             var170 = (int)((float)var260.h / var43);
                                             if (var170 > var193) {
                                                var35 = (float)var170 / (float)var193;
                                                var13 = (int)((float)var192 / var35);
                                                var170 = var193;
                                                break label4441;
                                             }

                                             if (var170 < AndroidUtilities.dp(120.0F)) {
                                                var13 = AndroidUtilities.dp(120.0F);
                                                var43 = (float)var260.h / (float)var13;
                                                var193 = var260.w;
                                                var170 = var13;
                                                if ((float)var193 / var43 < var35) {
                                                   var193 = (int)((float)var193 / var43);
                                                   var170 = var13;
                                                   var13 = var193;
                                                   break label4441;
                                                }
                                             }

                                             var13 = var192;
                                             break label4441;
                                          }
                                       }
                                    }

                                    var193 = var13;
                                    var13 = var170;
                                    var170 = var193;
                                 }

                                 label3736: {
                                    if (var13 != 0) {
                                       var193 = var170;
                                       if (var170 != 0) {
                                          break label3736;
                                       }
                                    }

                                    var13 = AndroidUtilities.dp(150.0F);
                                    var193 = var13;
                                 }

                                 var170 = var13;
                                 if (var5.type == 3) {
                                    var170 = var13;
                                    if (var13 < this.infoWidth + AndroidUtilities.dp(40.0F)) {
                                       var170 = this.infoWidth + AndroidUtilities.dp(40.0F);
                                    }
                                 }

                                 if (this.currentMessagesGroup == null) {
                                    this.availableTimeWidth = var163 - AndroidUtilities.dp(14.0F);
                                 } else {
                                    var192 = this.getGroupPhotosWidth();
                                    var163 = 0;

                                    for(var13 = 0; var163 < this.currentMessagesGroup.posArray.size(); ++var163) {
                                       var151 = (MessageObject.GroupedMessagePosition)this.currentMessagesGroup.posArray.get(var163);
                                       if (var151.minY != 0) {
                                          break;
                                       }

                                       var16 = (double)var13;
                                       var18 = Math.ceil((double)((float)(var151.pw + var151.leftSpanOffset) / 1000.0F * (float)var192));
                                       Double.isNaN(var16);
                                       var13 = (int)(var16 + var18);
                                    }

                                    this.availableTimeWidth = var13 - AndroidUtilities.dp(35.0F);
                                 }

                                 if (var5.type == 5) {
                                    var20 = (double)this.availableTimeWidth;
                                    var18 = Math.ceil((double)Theme.chat_audioTimePaint.measureText("00:00"));
                                    var16 = (double)AndroidUtilities.dp(26.0F);
                                    Double.isNaN(var16);
                                    Double.isNaN(var20);
                                    this.availableTimeWidth = (int)(var20 - (var18 + var16));
                                 }

                                 this.measureTime(var1);
                                 var163 = this.timeWidth;
                                 if (var1.isOutOwner()) {
                                    var247 = 20;
                                 } else {
                                    var247 = 0;
                                 }

                                 var40 = var163 + AndroidUtilities.dp((float)(var247 + 14));
                                 var13 = var170;
                                 if (var170 < var40) {
                                    var13 = var40;
                                 }

                                 label3715: {
                                    if (var1.isRoundVideo()) {
                                       var170 = Math.min(var13, var193);
                                       this.drawBackground = false;
                                       this.photoImage.setRoundRadius(var170 / 2);
                                    } else {
                                       var170 = var13;
                                       if (!var1.needDrawBluredPreview()) {
                                          break label3715;
                                       }

                                       if (AndroidUtilities.isTablet()) {
                                          var170 = AndroidUtilities.getMinTabletSide();
                                       } else {
                                          var154 = AndroidUtilities.displaySize;
                                          var170 = Math.min(var154.x, var154.y);
                                       }

                                       var170 = (int)((float)var170 * 0.5F);
                                    }

                                    var193 = var170;
                                    var170 = var170;
                                 }

                                 if (this.currentMessagesGroup != null) {
                                    var154 = AndroidUtilities.displaySize;
                                    var35 = (float)Math.max(var154.x, var154.y) * 0.5F;
                                    var170 = this.getGroupPhotosWidth();
                                    var43 = (float)this.currentPosition.pw / 1000.0F;
                                    var46 = (float)var170;
                                    var28 = (int)Math.ceil((double)(var43 * var46));
                                    var170 = var28;
                                    if (this.currentPosition.minY != 0) {
                                       label4443: {
                                          if (!var1.isOutOwner() || (this.currentPosition.flags & 1) == 0) {
                                             var170 = var28;
                                             if (var1.isOutOwner()) {
                                                break label4443;
                                             }

                                             var170 = var28;
                                             if ((this.currentPosition.flags & 2) == 0) {
                                                break label4443;
                                             }
                                          }

                                          var13 = 0;
                                          var193 = 0;

                                          for(var170 = 0; var13 < this.currentMessagesGroup.posArray.size(); var170 = var192) {
                                             var151 = (MessageObject.GroupedMessagePosition)this.currentMessagesGroup.posArray.get(var13);
                                             byte var268 = var151.minY;
                                             if (var268 == 0) {
                                                var16 = (double)var193;
                                                var20 = Math.ceil((double)((float)var151.pw / 1000.0F * var46));
                                                var193 = var151.leftSpanOffset;
                                                if (var193 != 0) {
                                                   var18 = Math.ceil((double)((float)var193 / 1000.0F * var46));
                                                } else {
                                                   var18 = 0.0D;
                                                }

                                                Double.isNaN(var16);
                                                var163 = (int)(var16 + var20 + var18);
                                                var192 = var170;
                                             } else {
                                                byte var199 = this.currentPosition.minY;
                                                if (var268 == var199) {
                                                   var20 = (double)var170;
                                                   var16 = Math.ceil((double)((float)var151.pw / 1000.0F * var46));
                                                   var170 = var151.leftSpanOffset;
                                                   if (var170 != 0) {
                                                      var18 = Math.ceil((double)((float)var170 / 1000.0F * var46));
                                                   } else {
                                                      var18 = 0.0D;
                                                   }

                                                   Double.isNaN(var20);
                                                   var192 = (int)(var20 + var16 + var18);
                                                   var163 = var193;
                                                } else {
                                                   var163 = var193;
                                                   var192 = var170;
                                                   if (var268 > var199) {
                                                      break;
                                                   }
                                                }
                                             }

                                             ++var13;
                                             var193 = var163;
                                          }

                                          var170 = var28 + (var193 - var170);
                                       }
                                    }

                                    var170 -= AndroidUtilities.dp(9.0F);
                                    var193 = var170;
                                    if (this.isAvatarVisible) {
                                       var193 = var170 - AndroidUtilities.dp(48.0F);
                                    }

                                    var151 = this.currentPosition;
                                    if (var151.siblingHeights != null) {
                                       var170 = 0;
                                       var13 = 0;

                                       while(true) {
                                          var151 = this.currentPosition;
                                          float[] var233 = var151.siblingHeights;
                                          if (var170 >= var233.length) {
                                             var13 += (var151.maxY - var151.minY) * Math.round(AndroidUtilities.density * 7.0F);
                                             break;
                                          }

                                          var13 += (int)Math.ceil((double)(var233[var170] * var35));
                                          ++var170;
                                       }
                                    } else {
                                       var13 = (int)Math.ceil((double)(var35 * var151.ph));
                                    }

                                    this.backgroundWidth = var193;
                                    var170 = this.currentPosition.flags;
                                    if ((var170 & 2) != 0 && (var170 & 1) != 0) {
                                       var170 = AndroidUtilities.dp(8.0F);
                                    } else {
                                       var170 = this.currentPosition.flags;
                                       if ((var170 & 2) == 0 && (var170 & 1) == 0) {
                                          var170 = AndroidUtilities.dp(11.0F);
                                       } else if ((this.currentPosition.flags & 2) != 0) {
                                          var170 = AndroidUtilities.dp(10.0F);
                                       } else {
                                          var170 = AndroidUtilities.dp(9.0F);
                                       }
                                    }

                                    var170 = var193 - var170;
                                    if (!this.currentPosition.edge) {
                                       var193 = AndroidUtilities.dp(10.0F) + var170;
                                    } else {
                                       var193 = var170;
                                    }

                                    var28 = 0 + (var193 - AndroidUtilities.dp(10.0F));
                                    var163 = this.currentPosition.flags;
                                    if ((var163 & 8) == 0 && (!this.currentMessagesGroup.hasSibling || (var163 & 4) != 0)) {
                                       var163 = var193;
                                       var192 = var13;
                                       var193 = var170;
                                       var170 = var28;
                                       var13 = var163;
                                    } else {
                                       var163 = var28 + this.getAdditionalWidthForPosition(this.currentPosition);
                                       var33 = this.currentMessagesGroup.messages.size();
                                       var192 = var170;
                                       var22 = 0;
                                       var170 = var163;

                                       while(true) {
                                          if (var22 >= var33) {
                                             var163 = var193;
                                             var193 = var13;
                                             var13 = var163;
                                             var163 = var170;
                                             break;
                                          }

                                          var6 = (MessageObject)this.currentMessagesGroup.messages.get(var22);
                                          MessageObject.GroupedMessagePosition var222 = (MessageObject.GroupedMessagePosition)this.currentMessagesGroup.posArray.get(var22);
                                          if (var222 != this.currentPosition && (var222.flags & 8) != 0) {
                                             label4473: {
                                                var31 = (int)Math.ceil((double)((float)var222.pw / 1000.0F * var46));
                                                if (var222.minY == 0 || (!var1.isOutOwner() || (var222.flags & 1) == 0) && (var1.isOutOwner() || (var222.flags & 2) == 0)) {
                                                   var192 = var193;
                                                   var193 = var13;
                                                   var13 = var170;
                                                   var163 = var31;
                                                } else {
                                                   var28 = 0;
                                                   var192 = 0;

                                                   for(var163 = 0; var28 < this.currentMessagesGroup.posArray.size(); var163 = var23) {
                                                      var151 = (MessageObject.GroupedMessagePosition)this.currentMessagesGroup.posArray.get(var28);
                                                      byte var264 = var151.minY;
                                                      if (var264 == 0) {
                                                         var16 = (double)var192;
                                                         var20 = Math.ceil((double)((float)var151.pw / 1000.0F * var46));
                                                         var192 = var151.leftSpanOffset;
                                                         if (var192 != 0) {
                                                            var18 = Math.ceil((double)((float)var192 / 1000.0F * var46));
                                                         } else {
                                                            var18 = 0.0D;
                                                         }

                                                         Double.isNaN(var16);
                                                         var36 = (int)(var16 + var20 + var18);
                                                         var23 = var163;
                                                      } else {
                                                         byte var262 = var222.minY;
                                                         if (var264 == var262) {
                                                            var16 = (double)var163;
                                                            var20 = Math.ceil((double)((float)var151.pw / 1000.0F * var46));
                                                            var163 = var151.leftSpanOffset;
                                                            if (var163 != 0) {
                                                               var18 = Math.ceil((double)((float)var163 / 1000.0F * var46));
                                                            } else {
                                                               var18 = 0.0D;
                                                            }

                                                            Double.isNaN(var16);
                                                            var23 = (int)(var16 + var20 + var18);
                                                            var36 = var192;
                                                         } else {
                                                            var36 = var192;
                                                            var23 = var163;
                                                            if (var264 > var262) {
                                                               break;
                                                            }
                                                         }
                                                      }

                                                      ++var28;
                                                      var192 = var36;
                                                   }

                                                   var28 = var13;
                                                   var163 = var31 + (var192 - var163);
                                                   var192 = var193;
                                                   var13 = var170;
                                                   var193 = var28;
                                                }

                                                var28 = AndroidUtilities.dp(9.0F);
                                                var170 = var222.flags;
                                                if ((var170 & 2) != 0 && (var170 & 1) != 0) {
                                                   var170 = AndroidUtilities.dp(8.0F);
                                                } else {
                                                   var170 = var222.flags;
                                                   if ((var170 & 2) == 0 && (var170 & 1) == 0) {
                                                      var170 = AndroidUtilities.dp(11.0F);
                                                   } else if ((var222.flags & 2) != 0) {
                                                      var170 = AndroidUtilities.dp(10.0F);
                                                   } else {
                                                      var170 = AndroidUtilities.dp(9.0F);
                                                   }
                                                }

                                                var163 = var163 - var28 - var170;
                                                var170 = var163;
                                                if (this.isChat) {
                                                   var170 = var163;
                                                   if (!var6.isOutOwner()) {
                                                      var170 = var163;
                                                      if (var6.needDrawAvatar()) {
                                                         label3609: {
                                                            if (var222 != null) {
                                                               var170 = var163;
                                                               if (!var222.edge) {
                                                                  break label3609;
                                                               }
                                                            }

                                                            var170 = var163 - AndroidUtilities.dp(48.0F);
                                                         }
                                                      }
                                                   }
                                                }

                                                var170 += this.getAdditionalWidthForPosition(var222);
                                                var36 = var170;
                                                if (!var222.edge) {
                                                   var36 = var170 + AndroidUtilities.dp(10.0F);
                                                }

                                                var23 = var13 + var36;
                                                if (var222.minX >= this.currentPosition.minX) {
                                                   var28 = var36;
                                                   var163 = var23;
                                                   var13 = var192;
                                                   var170 = var193;
                                                   if (!this.currentMessagesGroup.hasSibling) {
                                                      break label4473;
                                                   }

                                                   var28 = var36;
                                                   var163 = var23;
                                                   var13 = var192;
                                                   var170 = var193;
                                                   if (var222.minY == var222.maxY) {
                                                      break label4473;
                                                   }
                                                }

                                                this.captionOffsetX -= var36;
                                                var28 = var36;
                                                var163 = var23;
                                                var13 = var192;
                                                var170 = var193;
                                             }
                                          } else {
                                             var163 = var170;
                                             var170 = var13;
                                             var13 = var193;
                                             var28 = var192;
                                          }

                                          var270 = var6.caption;
                                          if (var270 != null) {
                                             if (this.currentCaption != null) {
                                                this.currentCaption = null;
                                                var192 = var28;
                                                var193 = var170;
                                                break;
                                             }

                                             this.currentCaption = var270;
                                          }

                                          ++var22;
                                          var193 = var13;
                                          var13 = var170;
                                          var192 = var28;
                                          var170 = var163;
                                       }

                                       var28 = var192;
                                       var192 = var193;
                                       var170 = var163;
                                       var193 = var28;
                                    }

                                    var36 = var192;
                                    var230 = false;
                                    var192 = var193;
                                    var163 = var13;
                                    var13 = var170;
                                    var193 = var36;
                                 } else {
                                    this.currentCaption = var5.caption;
                                    if (AndroidUtilities.isTablet()) {
                                       var13 = AndroidUtilities.getMinTabletSide();
                                    } else {
                                       var154 = AndroidUtilities.displaySize;
                                       var13 = Math.min(var154.x, var154.y);
                                    }

                                    var13 = (int)((float)var13 * 0.65F);
                                    if (!var1.needDrawBluredPreview() && this.currentCaption != null && var170 < var13) {
                                       var8 = true;
                                    } else {
                                       var13 = var170 - AndroidUtilities.dp(10.0F);
                                       var8 = false;
                                    }

                                    this.backgroundWidth = AndroidUtilities.dp(8.0F) + var170;
                                    if (!this.mediaBackground) {
                                       this.backgroundWidth += AndroidUtilities.dp(9.0F);
                                    }

                                    var230 = var8;
                                    var192 = var170;
                                    var163 = var170;
                                 }

                                 var270 = this.currentCaption;
                                 if (var270 == null) {
                                    var247 = 0;
                                    var170 = 0;
                                 } else {
                                    label4328: {
                                       label3588: {
                                          label3587: {
                                             label3586: {
                                                label4277: {
                                                   label3583: {
                                                      try {
                                                         if (VERSION.SDK_INT >= 24) {
                                                            this.captionLayout = Builder.obtain(var270, 0, var270.length(), Theme.chat_msgTextPaint, var13).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Alignment.ALIGN_NORMAL).build();
                                                            break label3583;
                                                         }
                                                      } catch (Exception var147) {
                                                         var10000 = var147;
                                                         var10001 = false;
                                                         break label4277;
                                                      }

                                                      try {
                                                         StaticLayout var223 = new StaticLayout(var270, Theme.chat_msgTextPaint, var13, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                                                         this.captionLayout = var223;
                                                      } catch (Exception var146) {
                                                         var10000 = var146;
                                                         var10001 = false;
                                                         break label4277;
                                                      }
                                                   }

                                                   try {
                                                      var36 = this.captionLayout.getLineCount();
                                                   } catch (Exception var145) {
                                                      var10000 = var145;
                                                      var10001 = false;
                                                      break label4277;
                                                   }

                                                   if (var36 <= 0) {
                                                      break label3587;
                                                   }

                                                   if (var230) {
                                                      try {
                                                         this.captionWidth = 0;
                                                      } catch (Exception var142) {
                                                         var10000 = var142;
                                                         var10001 = false;
                                                         break label4277;
                                                      }

                                                      for(var170 = 0; var170 < var36; ++var170) {
                                                         try {
                                                            this.captionWidth = (int)Math.max((double)this.captionWidth, Math.ceil((double)this.captionLayout.getLineWidth(var170)));
                                                            if (this.captionLayout.getLineLeft(var170) != 0.0F) {
                                                               this.captionWidth = var13;
                                                               break;
                                                            }
                                                         } catch (Exception var144) {
                                                            var10000 = var144;
                                                            var10001 = false;
                                                            break label4277;
                                                         }
                                                      }

                                                      try {
                                                         if (this.captionWidth > var13) {
                                                            this.captionWidth = var13;
                                                         }
                                                      } catch (Exception var143) {
                                                         var10000 = var143;
                                                         var10001 = false;
                                                         break label4277;
                                                      }
                                                   } else {
                                                      try {
                                                         this.captionWidth = var13;
                                                      } catch (Exception var141) {
                                                         var10000 = var141;
                                                         var10001 = false;
                                                         break label4277;
                                                      }
                                                   }

                                                   label4279: {
                                                      try {
                                                         this.captionHeight = this.captionLayout.getHeight();
                                                         this.addedCaptionHeight = this.captionHeight + AndroidUtilities.dp(9.0F);
                                                         if (this.currentPosition != null && (this.currentPosition.flags & 8) == 0) {
                                                            break label4279;
                                                         }
                                                      } catch (Exception var140) {
                                                         var10000 = var140;
                                                         var10001 = false;
                                                         break label4277;
                                                      }

                                                      try {
                                                         var170 = this.addedCaptionHeight;
                                                      } catch (Exception var139) {
                                                         var10000 = var139;
                                                         var10001 = false;
                                                         break label4277;
                                                      }

                                                      var13 = 0 + var170;
                                                      var170 = var13;

                                                      label4245: {
                                                         try {
                                                            var36 = Math.max(this.captionWidth, var163 - AndroidUtilities.dp(10.0F));
                                                         } catch (Exception var137) {
                                                            var10000 = var137;
                                                            var10001 = false;
                                                            break label4245;
                                                         }

                                                         var170 = var13;

                                                         try {
                                                            var43 = this.captionLayout.getLineWidth(this.captionLayout.getLineCount() - 1);
                                                         } catch (Exception var136) {
                                                            var10000 = var136;
                                                            var10001 = false;
                                                            break label4245;
                                                         }

                                                         var170 = var13;

                                                         try {
                                                            var35 = this.captionLayout.getLineLeft(this.captionLayout.getLineCount() - 1);
                                                         } catch (Exception var135) {
                                                            var10000 = var135;
                                                            var10001 = false;
                                                            break label4245;
                                                         }

                                                         var170 = var13;

                                                         label4246: {
                                                            label3513: {
                                                               try {
                                                                  if ((float)(var36 + AndroidUtilities.dp(2.0F)) - (var43 + var35) < (float)var40) {
                                                                     break label3513;
                                                                  }
                                                               } catch (Exception var134) {
                                                                  var10000 = var134;
                                                                  var10001 = false;
                                                                  break label4245;
                                                               }

                                                               byte var269 = 0;
                                                               var170 = var13;
                                                               var247 = var269;
                                                               break label4246;
                                                            }

                                                            var170 = var13;

                                                            try {
                                                               var36 = var13 + AndroidUtilities.dp(14.0F);
                                                            } catch (Exception var133) {
                                                               var10000 = var133;
                                                               var10001 = false;
                                                               break label4245;
                                                            }

                                                            var170 = var36;

                                                            try {
                                                               this.addedCaptionHeight += AndroidUtilities.dp(14.0F);
                                                            } catch (Exception var132) {
                                                               var10000 = var132;
                                                               var10001 = false;
                                                               break label4245;
                                                            }

                                                            var247 = 1;
                                                            var170 = var36;
                                                         }

                                                         var13 = var170;
                                                         var267 = var247;
                                                         break label3588;
                                                      }

                                                      var254 = var10000;
                                                      break label3586;
                                                   }

                                                   try {
                                                      this.captionLayout = null;
                                                      break label3587;
                                                   } catch (Exception var138) {
                                                      var10000 = var138;
                                                      var10001 = false;
                                                   }
                                                }

                                                var254 = var10000;
                                                var170 = 0;
                                             }

                                             FileLog.e((Throwable)var254);
                                             var247 = 0;
                                             break label4328;
                                          }

                                          var13 = 0;
                                          var267 = 0;
                                       }

                                       var247 = var267;
                                       var170 = var13;
                                    }
                                 }

                                 if (var230 && var163 < this.captionWidth + AndroidUtilities.dp(10.0F)) {
                                    var163 = this.captionWidth + AndroidUtilities.dp(10.0F);
                                    this.backgroundWidth = AndroidUtilities.dp(8.0F) + var163;
                                    if (!this.mediaBackground) {
                                       this.backgroundWidth += AndroidUtilities.dp(9.0F);
                                    }
                                 }

                                 label4464: {
                                    var156 = String.format(Locale.US, "%d_%d", (int)((float)var192 / AndroidUtilities.density), (int)((float)var193 / AndroidUtilities.density));
                                    this.currentPhotoFilterThumb = var156;
                                    this.currentPhotoFilter = var156;
                                    var245 = var1.photoThumbs;
                                    if (var245 == null || var245.size() <= 1) {
                                       var192 = var1.type;
                                       if (var192 != 3 && var192 != 8 && var192 != 5) {
                                          break label4464;
                                       }
                                    }

                                    if (var1.needDrawBluredPreview()) {
                                       var271 = new StringBuilder();
                                       var271.append(this.currentPhotoFilter);
                                       var271.append("_b2");
                                       this.currentPhotoFilter = var271.toString();
                                       var271 = new StringBuilder();
                                       var271.append(this.currentPhotoFilterThumb);
                                       var271.append("_b2");
                                       this.currentPhotoFilterThumb = var271.toString();
                                    } else {
                                       var271 = new StringBuilder();
                                       var271.append(this.currentPhotoFilterThumb);
                                       var271.append("_b");
                                       this.currentPhotoFilterThumb = var271.toString();
                                    }
                                 }

                                 var192 = var1.type;
                                 if (var192 != 3 && var192 != 8 && var192 != 5) {
                                    var15 = false;
                                 } else {
                                    var15 = true;
                                 }

                                 var189 = this.currentPhotoObject;
                                 if (var189 != null && !var15 && var189.size == 0) {
                                    var189.size = -1;
                                 }

                                 var189 = this.currentPhotoObjectThumb;
                                 if (var189 != null && !var15 && var189.size == 0) {
                                    var189.size = -1;
                                 }

                                 if (SharedConfig.autoplayVideo && var1.type == 3 && !var1.needDrawBluredPreview() && (this.currentMessageObject.mediaExists || var1.canStreamVideo() && DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.currentMessageObject))) {
                                    var151 = this.currentPosition;
                                    if (var151 == null) {
                                       this.autoPlayingMedia = true;
                                    } else {
                                       var28 = var151.flags;
                                       if ((var28 & 1) != 0 && (var28 & 2) != 0) {
                                          var3 = true;
                                       } else {
                                          var3 = false;
                                       }

                                       this.autoPlayingMedia = var3;
                                    }
                                 }

                                 if (this.autoPlayingMedia) {
                                    this.photoImage.setAllowStartAnimation(true);
                                    this.photoImage.startAnimation();
                                    var263 = var1.messageOwner.media.document;
                                    this.photoImage.setImage(ImageLocation.getForDocument(var263), "g", ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject), this.currentPhotoFilter, ImageLocation.getForDocument(this.currentPhotoObjectThumb, var263), this.currentPhotoFilterThumb, (Drawable)null, var1.messageOwner.media.document.size, (String)null, var1, 0);
                                 } else {
                                    var28 = var1.type;
                                    if (var28 == 1) {
                                       if (var1.useCustomPhoto) {
                                          this.photoImage.setImageBitmap(this.getResources().getDrawable(2131165874));
                                       } else {
                                          var189 = this.currentPhotoObject;
                                          if (var189 != null) {
                                             var156 = FileLoader.getAttachFileName(var189);
                                             if (var1.mediaExists) {
                                                DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                                                var230 = true;
                                             } else {
                                                var230 = false;
                                             }

                                             if (!var230 && !DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.currentMessageObject) && !FileLoader.getInstance(this.currentAccount).isLoadingFile(var156)) {
                                                this.photoNotSet = true;
                                                var173 = this.currentPhotoObjectThumb;
                                                if (var173 != null) {
                                                   var165 = this.photoImage;
                                                   var200 = ImageLocation.getForObject(var173, this.photoParentObject);
                                                   var174 = this.currentPhotoFilterThumb;
                                                   var6 = this.currentMessageObject;
                                                   if (var6.shouldEncryptPhotoOrVideo()) {
                                                      var249 = 2;
                                                   } else {
                                                      var249 = 0;
                                                   }

                                                   var165.setImage((ImageLocation)null, (String)null, var200, var174, 0, (String)null, var6, var249);
                                                } else {
                                                   this.photoImage.setImageBitmap((Drawable)null);
                                                }
                                             } else {
                                                var165 = this.photoImage;
                                                ImageLocation var257 = ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject);
                                                var27 = this.currentPhotoFilter;
                                                ImageLocation var240 = ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject);
                                                var174 = this.currentPhotoFilterThumb;
                                                if (var15) {
                                                   var192 = 0;
                                                } else {
                                                   var192 = this.currentPhotoObject.size;
                                                }

                                                var228 = this.currentMessageObject;
                                                if (var228.shouldEncryptPhotoOrVideo()) {
                                                   var225 = 2;
                                                } else {
                                                   var225 = 0;
                                                }

                                                var165.setImage(var257, var27, var240, var174, var192, (String)null, var228, var225);
                                             }
                                          } else {
                                             this.photoImage.setImageBitmap((Drawable)null);
                                          }
                                       }
                                    } else if (var28 != 8 && var28 != 5) {
                                       var196 = this.photoImage;
                                       var236 = ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject);
                                       var179 = this.currentPhotoFilter;
                                       ImageLocation var272 = ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject);
                                       var174 = this.currentPhotoFilterThumb;
                                       if (this.currentMessageObject.shouldEncryptPhotoOrVideo()) {
                                          var249 = 2;
                                       } else {
                                          var249 = 0;
                                       }

                                       var196.setImage(var236, var179, var272, var174, 0, (String)null, var1, var249);
                                    } else {
                                       var156 = FileLoader.getAttachFileName(var1.messageOwner.media.document);
                                       if (var1.attachPathExists) {
                                          DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                                          var249 = 1;
                                       } else if (var1.mediaExists) {
                                          var249 = 2;
                                       } else {
                                          var249 = 0;
                                       }

                                       if (!MessageObject.isGifDocument(var1.messageOwner.media.document) && var1.type != 5) {
                                          var3 = false;
                                       } else {
                                          var3 = DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.currentMessageObject);
                                       }

                                       if (!var1.isSending() && !var1.isEditing() && (var249 != 0 || FileLoader.getInstance(this.currentAccount).isLoadingFile(var156) || var3)) {
                                          if (var249 != 1 && !var1.needDrawBluredPreview() && (var249 != 0 || var1.canStreamVideo() && var3)) {
                                             this.autoPlayingMedia = true;
                                             this.photoImage.setImage(ImageLocation.getForDocument(var1.messageOwner.media.document), "g", ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject), this.currentPhotoFilter, ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject), this.currentPhotoFilterThumb, (Drawable)null, var1.messageOwner.media.document.size, (String)null, var1, 0);
                                          } else if (var249 == 1) {
                                             var209 = this.photoImage;
                                             if (var1.isSendError()) {
                                                var156 = null;
                                             } else {
                                                var156 = var1.messageOwner.attachPath;
                                             }

                                             var209.setImage(ImageLocation.getForPath(var156), (String)null, ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject), this.currentPhotoFilterThumb, 0, (String)null, var1, 0);
                                          } else {
                                             this.photoImage.setImage(ImageLocation.getForDocument(var1.messageOwner.media.document), (String)null, ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject), this.currentPhotoFilter, ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject), this.currentPhotoFilterThumb, (Drawable)null, var1.messageOwner.media.document.size, (String)null, var1, 0);
                                          }
                                       } else {
                                          this.photoImage.setImage(ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject), this.currentPhotoFilter, ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject), this.currentPhotoFilterThumb, 0, (String)null, var1, 0);
                                       }
                                    }
                                 }

                                 var192 = var163;
                                 var163 = var170;
                                 var170 = var193;
                                 var193 = var192;
                                 break label3985;
                              }

                              this.drawBackground = false;
                              if (var1.type == 13) {
                                 var15 = true;
                              } else {
                                 var15 = false;
                              }

                              var193 = 0;
                              var13 = 0;

                              for(var170 = 0; var193 < var5.messageOwner.media.document.attributes.size(); ++var193) {
                                 var260 = (TLRPC.DocumentAttribute)var5.messageOwner.media.document.attributes.get(var193);
                                 if (var260 instanceof TLRPC.TL_documentAttributeImageSize) {
                                    var13 = var260.w;
                                    var170 = var260.h;
                                 }
                              }

                              var163 = var13;
                              var193 = var170;
                              if (var13 == 0) {
                                 var163 = var13;
                                 var193 = var170;
                                 if (var170 == 0) {
                                    var163 = var13;
                                    var193 = var170;
                                    if (var1.isAnimatedSticker()) {
                                       var163 = 512;
                                       var193 = 512;
                                    }
                                 }
                              }

                              if (AndroidUtilities.isTablet()) {
                                 var35 = (float)AndroidUtilities.getMinTabletSide();
                                 var43 = 0.4F;
                              } else {
                                 var154 = AndroidUtilities.displaySize;
                                 var35 = (float)Math.min(var154.x, var154.y);
                                 var43 = 0.5F;
                              }

                              var43 = var35 * var43;
                              var170 = var163;
                              if (var163 == 0) {
                                 var193 = (int)var43;
                                 var170 = AndroidUtilities.dp(100.0F) + var193;
                              }

                              var13 = (int)((float)var193 * (var43 / (float)var170));
                              var170 = (int)var43;
                              var35 = (float)var13;
                              if (var35 > var43) {
                                 var13 = (int)((float)var170 * (var43 / var35));
                              } else {
                                 var193 = var170;
                                 var170 = var13;
                                 var13 = var193;
                              }

                              this.documentAttachType = 6;
                              this.availableTimeWidth = var13 - AndroidUtilities.dp(14.0F);
                              this.backgroundWidth = AndroidUtilities.dp(12.0F) + var13;
                              this.currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(var5.photoThumbs, 40);
                              this.photoParentObject = var5.photoThumbsObject;
                              if (var5.attachPathExists) {
                                 var196 = this.photoImage;
                                 var219 = ImageLocation.getForPath(var5.messageOwner.attachPath);
                                 var179 = String.format(Locale.US, "%d_%d", var13, var170);
                                 var236 = ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject);
                                 var193 = var5.messageOwner.media.document.size;
                                 if (var15) {
                                    var156 = "webp";
                                 } else {
                                    var156 = null;
                                 }

                                 var196.setImage(var219, var179, var236, "b1", var193, var156, var1, 1);
                              } else {
                                 var263 = var5.messageOwner.media.document;
                                 if (var263.id != 0L) {
                                    var196 = this.photoImage;
                                    var236 = ImageLocation.getForDocument(var263);
                                    var174 = String.format(Locale.US, "%d_%d", var13, var170);
                                    var200 = ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject);
                                    var193 = var5.messageOwner.media.document.size;
                                    if (var15) {
                                       var156 = "webp";
                                    } else {
                                       var156 = null;
                                    }

                                    var196.setImage(var236, var174, var200, "b1", var193, var156, var1, 1);
                                 }
                              }
                           }

                           var249 = 0;
                           var163 = 0;
                           var193 = var13;
                           var247 = var249;
                        }

                        label4383: {
                           this.setMessageObjectInternal(var1);
                           if (this.drawForwardedName && var1.needDrawForwarded()) {
                              var162 = this.currentPosition;
                              if (var162 == null || var162.minY == 0) {
                                 if (var1.type != 5) {
                                    this.namesOffset += AndroidUtilities.dp(5.0F);
                                 }
                                 break label4383;
                              }
                           }

                           if (this.drawNameLayout && var1.messageOwner.reply_to_msg_id == 0) {
                              this.namesOffset += AndroidUtilities.dp(7.0F);
                           }
                        }

                        this.totalHeight = AndroidUtilities.dp(14.0F) + var170 + this.namesOffset + var163;
                        var151 = this.currentPosition;
                        if (var151 != null && (var151.flags & 8) == 0) {
                           this.totalHeight -= AndroidUtilities.dp(3.0F);
                        }

                        var151 = this.currentPosition;
                        if (var151 != null) {
                           var36 = var193 + this.getAdditionalWidthForPosition(var151);
                           if ((this.currentPosition.flags & 4) == 0) {
                              var193 = var170 + AndroidUtilities.dp(4.0F);
                              var170 = 0 - AndroidUtilities.dp(4.0F);
                           } else {
                              var166 = 0;
                              var193 = var170;
                              var170 = var166;
                           }

                           var28 = var36;
                           var192 = var170;
                           var163 = var193;
                           if ((this.currentPosition.flags & 8) == 0) {
                              var163 = var193 + AndroidUtilities.dp(1.0F);
                              var28 = var36;
                              var192 = var170;
                           }
                        } else {
                           var192 = 0;
                           var163 = var170;
                           var28 = var193;
                        }

                        if (this.drawPinnedTop) {
                           this.namesOffset -= AndroidUtilities.dp(1.0F);
                        }

                        if (this.currentPosition != null) {
                           if (this.namesOffset > 0) {
                              var170 = AndroidUtilities.dp(7.0F);
                              this.totalHeight -= AndroidUtilities.dp(2.0F);
                           } else {
                              var170 = AndroidUtilities.dp(5.0F);
                              this.totalHeight -= AndroidUtilities.dp(4.0F);
                           }
                        } else if (this.namesOffset > 0) {
                           var170 = AndroidUtilities.dp(7.0F);
                           this.totalHeight -= AndroidUtilities.dp(2.0F);
                        } else {
                           var170 = AndroidUtilities.dp(5.0F);
                           this.totalHeight -= AndroidUtilities.dp(4.0F);
                        }

                        this.photoImage.setImageCoords(0, var170 + this.namesOffset + var192, var28, var163);
                        this.invalidate();
                        var267 = var247;
                        break label4015;
                     }

                     TLRPC.TL_messageMediaPoll var213;
                     label2650: {
                        this.createSelectorDrawable();
                        this.drawName = true;
                        this.drawForwardedName = true;
                        this.drawPhotoImage = false;
                        var23 = Math.min(AndroidUtilities.dp(500.0F), var1.getMaxMessageTextWidth());
                        this.availableTimeWidth = var23;
                        this.backgroundWidth = AndroidUtilities.dp(31.0F) + var23;
                        this.availableTimeWidth = AndroidUtilities.dp(120.0F);
                        this.measureTime(var1);
                        var213 = (TLRPC.TL_messageMediaPoll)var1.messageOwner.media;
                        this.pollClosed = var213.poll.closed;
                        this.pollVoted = var1.isVoted();
                        this.titleLayout = new StaticLayout(Emoji.replaceEmoji(var213.poll.question, Theme.chat_audioTitlePaint.getFontMetricsInt(), AndroidUtilities.dp(16.0F), false), Theme.chat_audioTitlePaint, var23 + AndroidUtilities.dp(2.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                        var224 = this.titleLayout;
                        if (var224 != null) {
                           var13 = var224.getLineCount();

                           for(var170 = 0; var170 < var13; ++var170) {
                              if (this.titleLayout.getLineLeft(var170) != 0.0F) {
                                 var10 = true;
                                 break label2650;
                              }
                           }
                        }

                        var10 = false;
                     }

                     if (var213.poll.closed) {
                        var13 = 2131559490;
                        var156 = "FinalResults";
                     } else {
                        var13 = 2131558633;
                        var156 = "AnonymousPoll";
                     }

                     var164 = LocaleController.getString(var156, var13);
                     TextPaint var235 = Theme.chat_timePaint;
                     var35 = (float)var23;
                     this.docTitleLayout = new StaticLayout(TextUtils.ellipsize(var164, var235, var35, TruncateAt.END), Theme.chat_timePaint, var23 + AndroidUtilities.dp(2.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                     var224 = this.docTitleLayout;
                     if (var224 != null && var224.getLineCount() > 0) {
                        if (var10 && !LocaleController.isRTL) {
                           this.docTitleOffsetX = (int)Math.ceil((double)(var35 - this.docTitleLayout.getLineWidth(0)));
                        } else if (!var10 && LocaleController.isRTL) {
                           this.docTitleOffsetX = -((int)Math.ceil((double)this.docTitleLayout.getLineLeft(0)));
                        }
                     }

                     var170 = this.timeWidth;
                     if (var1.isOutOwner()) {
                        var35 = 28.0F;
                     } else {
                        var35 = 8.0F;
                     }

                     var170 = var23 - var170 - AndroidUtilities.dp(var35);
                     var13 = var213.results.total_voters;
                     if (var13 == 0) {
                        var156 = LocaleController.getString("NoVotes", 2131559956);
                     } else {
                        var156 = LocaleController.formatPluralString("Vote", var13);
                     }

                     this.infoLayout = new StaticLayout(TextUtils.ellipsize(var156, Theme.chat_livePaint, (float)var170, TruncateAt.END), Theme.chat_livePaint, var170, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                     var224 = this.infoLayout;
                     if (var224 != null && var224.getLineCount() > 0) {
                        var18 = (double)(-this.infoLayout.getLineLeft(0));
                     } else {
                        var18 = 0.0D;
                     }

                     this.infoX = (int)Math.ceil(var18);
                     this.lastPoll = var213.poll;
                     TLRPC.TL_pollResults var243 = var213.results;
                     this.lastPollResults = var243.results;
                     this.lastPollResultsVoters = var243.total_voters;
                     if (!this.animatePollAnswer && this.pollVoteInProgress) {
                        this.performHapticFeedback(3, 2);
                     }

                     if (!this.attachedToWindow || !this.pollVoteInProgress && !this.pollUnvoteInProgress) {
                        var3 = false;
                     } else {
                        var3 = true;
                     }

                     this.animatePollAnswer = var3;
                     this.animatePollAnswerAlpha = var3;
                     ArrayList var220 = new ArrayList();
                     if (this.pollButtons.isEmpty()) {
                        var168 = null;
                     } else {
                        var245 = new ArrayList(this.pollButtons);
                        this.pollButtons.clear();
                        if (!this.animatePollAnswer) {
                           if (!this.attachedToWindow || !this.pollVoted && !this.pollClosed) {
                              var3 = false;
                           } else {
                              var3 = true;
                           }

                           this.animatePollAnswer = var3;
                        }

                        var35 = this.pollAnimationProgress;
                        if (var35 > 0.0F) {
                           var168 = var245;
                           if (var35 < 1.0F) {
                              var13 = var245.size();
                              var170 = 0;

                              while(true) {
                                 var168 = var245;
                                 if (var170 >= var13) {
                                    break;
                                 }

                                 ChatMessageCell.PollButton var221 = (ChatMessageCell.PollButton)var245.get(var170);
                                 var221.percent = (int)Math.ceil((double)((float)var221.prevPercent + (float)(var221.percent - var221.prevPercent) * this.pollAnimationProgress));
                                 var221.percentProgress = var221.prevPercentProgress + (var221.percentProgress - var221.prevPercentProgress) * this.pollAnimationProgress;
                                 ++var170;
                              }
                           }
                        } else {
                           var168 = var245;
                        }
                     }

                     if (this.animatePollAnswer) {
                        var35 = 0.0F;
                     } else {
                        var35 = 1.0F;
                     }

                     this.pollAnimationProgress = var35;
                     byte[] var246;
                     if (!this.animatePollAnswerAlpha) {
                        this.pollVoteInProgress = false;
                        this.pollVoteInProgressNum = -1;
                        var246 = SendMessagesHelper.getInstance(this.currentAccount).isSendingVote(this.currentMessageObject);
                     } else {
                        var246 = null;
                     }

                     StaticLayout var229 = this.titleLayout;
                     if (var229 != null) {
                        var170 = var229.getHeight();
                     } else {
                        var170 = 0;
                     }

                     var192 = var213.poll.answers.size();
                     var36 = 0;
                     var14 = false;
                     var22 = 100;
                     var13 = 0;
                     var28 = 0;
                     var163 = var170;

                     for(var170 = var23; var36 < var192; var28 = var23) {
                        ChatMessageCell.PollButton var232;
                        label2575: {
                           var232 = new ChatMessageCell.PollButton();
                           var232.answer = (TLRPC.TL_pollAnswer)var213.poll.answers.get(var36);
                           var232.title = new StaticLayout(Emoji.replaceEmoji(var232.answer.text, Theme.chat_audioPerformerPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0F), false), Theme.chat_audioPerformerPaint, var170 - AndroidUtilities.dp(33.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                           var232.y = AndroidUtilities.dp(52.0F) + var163;
                           var232.height = var232.title.getHeight();
                           this.pollButtons.add(var232);
                           var220.add(var232);
                           var40 = var163 + var232.height + AndroidUtilities.dp(26.0F);
                           if (!var213.results.results.isEmpty()) {
                              var23 = var213.results.results.size();

                              for(var163 = 0; var163 < var23; ++var163) {
                                 TLRPC.TL_pollAnswerVoters var255 = (TLRPC.TL_pollAnswerVoters)var213.results.results.get(var163);
                                 if (Arrays.equals(var232.answer.option, var255.option)) {
                                    label4449: {
                                       if (this.pollVoted || this.pollClosed) {
                                          var163 = var213.results.total_voters;
                                          if (var163 > 0) {
                                             var232.decimal = (float)var255.voters / (float)var163 * 100.0F;
                                             var232.percent = (int)var232.decimal;
                                             var232.decimal = var232.decimal - (float)var232.percent;
                                             break label4449;
                                          }
                                       }

                                       var232.percent = 0;
                                       var232.decimal = 0.0F;
                                    }

                                    label2557: {
                                       if (var13 == 0) {
                                          var163 = var232.percent;
                                       } else {
                                          var163 = var13;
                                          if (var232.percent != 0) {
                                             var163 = var13;
                                             if (var13 != var232.percent) {
                                                var14 = true;
                                                break label2557;
                                             }
                                          }
                                       }

                                       var13 = var163;
                                    }

                                    var22 -= var232.percent;
                                    var23 = Math.max(var232.percent, var28);
                                    var28 = var13;
                                    break label2575;
                                 }
                              }
                           }

                           var23 = var28;
                           var28 = var13;
                        }

                        label2586: {
                           var13 = var170;
                           if (var168 != null) {
                              var31 = var168.size();
                              var163 = 0;

                              while(true) {
                                 var13 = var170;
                                 if (var163 >= var31) {
                                    break;
                                 }

                                 ChatMessageCell.PollButton var227 = (ChatMessageCell.PollButton)var168.get(var163);
                                 byte[] var256 = var232.answer.option;
                                 if (Arrays.equals(var256, var227.answer.option)) {
                                    var232.prevPercent = var227.percent;
                                    var232.prevPercentProgress = var227.percentProgress;
                                    break label2586;
                                 }

                                 ++var163;
                              }
                           }

                           var170 = var13;
                        }

                        if (var246 != null && Arrays.equals(var232.answer.option, var246)) {
                           this.pollVoteInProgressNum = var36;
                           this.pollVoteInProgress = true;
                           var246 = null;
                        }

                        ++var36;
                        var163 = var40;
                        var13 = var28;
                     }

                     ChatMessageCell.PollButton var251;
                     if (var14 && var22 != 0) {
                        Collections.sort(var220, _$$Lambda$ChatMessageCell$hzMG4njhE1StYhHOT542pSi6Cf0.INSTANCE);
                        var13 = Math.min(var22, var220.size());

                        for(var170 = 0; var170 < var13; ++var170) {
                           var251 = (ChatMessageCell.PollButton)var220.get(var170);
                           var251.percent = var251.percent + 1;
                        }
                     }

                     var13 = this.backgroundWidth;
                     var192 = AndroidUtilities.dp(76.0F);
                     var193 = this.pollButtons.size();

                     for(var170 = 0; var170 < var193; ++var170) {
                        var251 = (ChatMessageCell.PollButton)this.pollButtons.get(var170);
                        var43 = (float)AndroidUtilities.dp(5.0F) / (float)(var13 - var192);
                        if (var28 != 0) {
                           var35 = (float)var251.percent / (float)var28;
                        } else {
                           var35 = 0.0F;
                        }

                        var251.percentProgress = Math.max(var43, var35);
                     }

                     this.setMessageObjectInternal(var1);
                     var13 = AndroidUtilities.dp(73.0F);
                     var170 = this.namesOffset;
                     this.totalHeight = var13 + var170 + var163;
                     if (this.drawPinnedTop) {
                        this.namesOffset = var170 - AndroidUtilities.dp(1.0F);
                     }
                  }
               }
            }

            var267 = 0;
         }

         var228 = var1;
         var247 = var267;
         if (this.currentPosition == null) {
            var247 = var267;
            if (!var1.isAnyKindOfSticker()) {
               var247 = var267;
               if (this.addedCaptionHeight == 0) {
                  if (this.captionLayout == null) {
                     var270 = var1.caption;
                     if (var270 != null) {
                        label2441: {
                           label2440: {
                              try {
                                 this.currentCaption = var270;
                                 var13 = this.backgroundWidth - AndroidUtilities.dp(31.0F) - AndroidUtilities.dp(10.0F);
                                 if (VERSION.SDK_INT >= 24) {
                                    this.captionLayout = Builder.obtain(var228.caption, 0, var228.caption.length(), Theme.chat_msgTextPaint, var13).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Alignment.ALIGN_NORMAL).build();
                                    break label2441;
                                 }
                              } catch (Exception var63) {
                                 var10000 = var63;
                                 var10001 = false;
                                 break label2440;
                              }

                              try {
                                 var224 = new StaticLayout(var228.caption, Theme.chat_msgTextPaint, var13, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                                 this.captionLayout = var224;
                                 break label2441;
                              } catch (Exception var62) {
                                 var10000 = var62;
                                 var10001 = false;
                              }
                           }

                           var254 = var10000;
                           FileLog.e((Throwable)var254);
                        }
                     }
                  }

                  var247 = var267;
                  if (this.captionLayout != null) {
                     label4351: {
                        label2429: {
                           label4280: {
                              try {
                                 var163 = this.backgroundWidth - AndroidUtilities.dp(31.0F);
                                 var193 = AndroidUtilities.dp(10.0F);
                              } catch (Exception var61) {
                                 var10000 = var61;
                                 var10001 = false;
                                 break label4280;
                              }

                              var247 = var267;

                              try {
                                 if (this.captionLayout == null) {
                                    break label4351;
                                 }
                              } catch (Exception var60) {
                                 var10000 = var60;
                                 var10001 = false;
                                 break label4280;
                              }

                              var247 = var267;

                              label2418: {
                                 try {
                                    if (this.captionLayout.getLineCount() <= 0) {
                                       break label4351;
                                    }

                                    this.captionWidth = var163 - var193;
                                    var192 = this.timeWidth;
                                    if (var1.isOutOwner()) {
                                       var193 = AndroidUtilities.dp(20.0F);
                                       break label2418;
                                    }
                                 } catch (Exception var59) {
                                    var10000 = var59;
                                    var10001 = false;
                                    break label4280;
                                 }

                                 var193 = 0;
                              }

                              try {
                                 this.captionHeight = this.captionLayout.getHeight();
                                 this.totalHeight += this.captionHeight + AndroidUtilities.dp(9.0F);
                                 var35 = this.captionLayout.getLineWidth(this.captionLayout.getLineCount() - 1);
                                 var43 = this.captionLayout.getLineLeft(this.captionLayout.getLineCount() - 1);
                              } catch (Exception var58) {
                                 var10000 = var58;
                                 var10001 = false;
                                 break label4280;
                              }

                              var247 = var267;

                              try {
                                 if ((float)(var163 - AndroidUtilities.dp(8.0F)) - (var35 + var43) >= (float)(var192 + var193)) {
                                    break label4351;
                                 }

                                 this.totalHeight += AndroidUtilities.dp(14.0F);
                                 this.captionHeight += AndroidUtilities.dp(14.0F);
                                 break label2429;
                              } catch (Exception var57) {
                                 var10000 = var57;
                                 var10001 = false;
                              }
                           }

                           var254 = var10000;
                           FileLog.e((Throwable)var254);
                           var247 = var267;
                           break label4351;
                        }

                        var247 = 2;
                     }
                  }
               }
            }
         }

         if (this.captionLayout == null) {
            var170 = this.widthBeforeNewTimeLine;
            if (var170 != -1 && this.availableTimeWidth - var170 < this.timeWidth) {
               this.totalHeight += AndroidUtilities.dp(14.0F);
            }
         }

         MessageObject var277 = this.currentMessageObject;
         if (var277.eventId != 0L && !var277.isMediaEmpty() && this.currentMessageObject.messageOwner.media.webpage != null) {
            var170 = this.backgroundWidth - AndroidUtilities.dp(41.0F);
            this.hasOldCaptionPreview = true;
            this.linkPreviewHeight = 0;
            TLRPC.WebPage var244 = this.currentMessageObject.messageOwner.media.webpage;

            label2387: {
               label4269: {
                  try {
                     var193 = (int)Math.ceil((double)(Theme.chat_replyNamePaint.measureText(var244.site_name) + 1.0F));
                     this.siteNameWidth = var193;
                     var224 = new StaticLayout(var244.site_name, Theme.chat_replyNamePaint, Math.min(var193, var170), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                     this.siteNameLayout = var224;
                     var35 = this.siteNameLayout.getLineLeft(0);
                  } catch (Exception var56) {
                     var254 = var56;
                     break label4269;
                  }

                  if (var35 != 0.0F) {
                     var3 = true;
                  } else {
                     var3 = false;
                  }

                  try {
                     this.siteNameRtl = var3;
                     var193 = this.siteNameLayout.getLineBottom(this.siteNameLayout.getLineCount() - 1);
                     this.linkPreviewHeight += var193;
                     this.totalHeight += var193;
                     break label2387;
                  } catch (Exception var55) {
                     var254 = var55;
                  }
               }

               FileLog.e((Throwable)var254);
            }

            label2378: {
               label4270: {
                  try {
                     this.descriptionX = 0;
                     if (this.linkPreviewHeight != 0) {
                        this.totalHeight += AndroidUtilities.dp(2.0F);
                     }
                  } catch (Exception var54) {
                     var10000 = var54;
                     var10001 = false;
                     break label4270;
                  }

                  try {
                     this.descriptionLayout = StaticLayoutEx.createStaticLayout(var244.description, Theme.chat_replyTextPaint, var170, Alignment.ALIGN_NORMAL, 1.0F, (float)AndroidUtilities.dp(1.0F), false, TruncateAt.END, var170, 6);
                     var170 = this.descriptionLayout.getLineBottom(this.descriptionLayout.getLineCount() - 1);
                     this.linkPreviewHeight += var170;
                     this.totalHeight += var170;
                  } catch (Exception var53) {
                     var10000 = var53;
                     var10001 = false;
                     break label4270;
                  }

                  var170 = 0;

                  while(true) {
                     try {
                        if (var170 >= this.descriptionLayout.getLineCount()) {
                           break label2378;
                        }

                        var193 = (int)Math.ceil((double)this.descriptionLayout.getLineLeft(var170));
                     } catch (Exception var51) {
                        var10000 = var51;
                        var10001 = false;
                        break;
                     }

                     if (var193 != 0) {
                        label2366: {
                           try {
                              if (this.descriptionX == 0) {
                                 this.descriptionX = -var193;
                                 break label2366;
                              }
                           } catch (Exception var52) {
                              var10000 = var52;
                              var10001 = false;
                              break;
                           }

                           try {
                              this.descriptionX = Math.max(this.descriptionX, -var193);
                           } catch (Exception var50) {
                              var10000 = var50;
                              var10001 = false;
                              break;
                           }
                        }
                     }

                     ++var170;
                  }
               }

               var254 = var10000;
               FileLog.e((Throwable)var254);
            }

            this.totalHeight += AndroidUtilities.dp(17.0F);
            if (var247 != 0) {
               this.totalHeight -= AndroidUtilities.dp(14.0F);
               if (var247 == 2) {
                  this.captionHeight -= AndroidUtilities.dp(14.0F);
               }
            }
         }

         var35 = 0.0F;
         this.botButtons.clear();
         if (var7) {
            this.botButtonsByData.clear();
            this.botButtonsByPosition.clear();
            this.botButtonsLayout = null;
         }

         label2342: {
            if (this.currentPosition == null) {
               TLRPC.ReplyMarkup var278 = var1.messageOwner.reply_markup;
               if (var278 instanceof TLRPC.TL_replyInlineMarkup) {
                  var192 = var278.rows.size();
                  var170 = AndroidUtilities.dp(48.0F) * var192 + AndroidUtilities.dp(1.0F);
                  this.keyboardHeight = var170;
                  this.substractBackgroundHeight = var170;
                  var170 = this.backgroundWidth;
                  if (!this.mediaBackground) {
                     var35 = 9.0F;
                  }

                  this.widthForButtons = var170 - AndroidUtilities.dp(var35);
                  if (var1.wantedBotKeyboardWidth > this.widthForButtons) {
                     if (this.isChat && var1.needDrawAvatar() && !var1.isOutOwner()) {
                        var35 = 62.0F;
                     } else {
                        var35 = 10.0F;
                     }

                     var13 = -AndroidUtilities.dp(var35);
                     if (AndroidUtilities.isTablet()) {
                        var170 = AndroidUtilities.getMinTabletSide();
                     } else {
                        var154 = AndroidUtilities.displaySize;
                        var170 = Math.min(var154.x, var154.y) - AndroidUtilities.dp(5.0F);
                     }

                     this.widthForButtons = Math.max(this.backgroundWidth, Math.min(var1.wantedBotKeyboardWidth, var13 + var170));
                  }

                  HashMap var231;
                  HashMap var279;
                  label4274: {
                     var231 = new HashMap(this.botButtonsByData);
                     var271 = var1.botButtonsLayout;
                     if (var271 != null) {
                        var164 = this.botButtonsLayout;
                        if (var164 != null && var164.equals(var271.toString())) {
                           var279 = new HashMap(this.botButtonsByPosition);
                           break label4274;
                        }
                     }

                     var271 = var1.botButtonsLayout;
                     if (var271 != null) {
                        this.botButtonsLayout = var271.toString();
                     }

                     var279 = null;
                  }

                  this.botButtonsByData.clear();
                  var13 = 0;

                  for(var170 = 0; var13 < var192; ++var13) {
                     TLRPC.TL_keyboardButtonRow var242 = (TLRPC.TL_keyboardButtonRow)var228.messageOwner.reply_markup.rows.get(var13);
                     var193 = var242.buttons.size();
                     if (var193 != 0) {
                        var28 = (this.widthForButtons - AndroidUtilities.dp(5.0F) * (var193 - 1) - AndroidUtilities.dp(2.0F)) / var193;

                        for(var193 = 0; var193 < var242.buttons.size(); var170 = var163) {
                           ChatMessageCell.BotButton var258 = new ChatMessageCell.BotButton();
                           var258.button = (TLRPC.KeyboardButton)var242.buttons.get(var193);
                           var26 = Utilities.bytesToHex(var258.button.data);
                           var198 = new StringBuilder();
                           var198.append(var13);
                           var198.append("");
                           var198.append(var193);
                           var205 = var198.toString();
                           ChatMessageCell.BotButton var248;
                           if (var279 != null) {
                              var248 = (ChatMessageCell.BotButton)var279.get(var205);
                           } else {
                              var248 = (ChatMessageCell.BotButton)var231.get(var26);
                           }

                           if (var248 != null) {
                              var258.progressAlpha = var248.progressAlpha;
                              var258.angle = var248.angle;
                              var258.lastUpdateTime = var248.lastUpdateTime;
                           } else {
                              var258.lastUpdateTime = System.currentTimeMillis();
                           }

                           this.botButtonsByData.put(var26, var258);
                           this.botButtonsByPosition.put(var205, var258);
                           var258.x = (AndroidUtilities.dp(5.0F) + var28) * var193;
                           var258.y = AndroidUtilities.dp(48.0F) * var13 + AndroidUtilities.dp(5.0F);
                           var258.width = var28;
                           var258.height = AndroidUtilities.dp(44.0F);
                           Object var252;
                           if (var258.button instanceof TLRPC.TL_keyboardButtonBuy && (var228.messageOwner.media.flags & 4) != 0) {
                              var252 = LocaleController.getString("PaymentReceipt", 2131560388);
                           } else {
                              var252 = TextUtils.ellipsize(Emoji.replaceEmoji(var258.button.text, Theme.chat_botButtonPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0F), false), Theme.chat_botButtonPaint, (float)(var28 - AndroidUtilities.dp(10.0F)), TruncateAt.END);
                           }

                           var258.title = new StaticLayout((CharSequence)var252, Theme.chat_botButtonPaint, var28 - AndroidUtilities.dp(10.0F), Alignment.ALIGN_CENTER, 1.0F, 0.0F, false);
                           this.botButtons.add(var258);
                           var163 = var170;
                           if (var193 == var242.buttons.size() - 1) {
                              var163 = Math.max(var170, var258.x + var258.width);
                           }

                           ++var193;
                        }
                     }
                  }

                  this.widthForButtons = var170;
                  break label2342;
               }
            }

            this.substractBackgroundHeight = 0;
            this.keyboardHeight = 0;
         }

         if (this.drawPinnedBottom && this.drawPinnedTop) {
            this.totalHeight -= AndroidUtilities.dp(2.0F);
         } else if (this.drawPinnedBottom) {
            this.totalHeight -= AndroidUtilities.dp(1.0F);
         } else if (this.drawPinnedTop && this.pinnedBottom) {
            var151 = this.currentPosition;
            if (var151 != null && var151.siblingHeights == null) {
               this.totalHeight -= AndroidUtilities.dp(1.0F);
            }
         }

         if (var1.isAnyKindOfSticker() && this.totalHeight < AndroidUtilities.dp(70.0F)) {
            this.totalHeight = AndroidUtilities.dp(70.0F);
         }

         if (!this.drawPhotoImage) {
            this.photoImage.setImageBitmap((Drawable)null);
         }

         if (this.documentAttachType == 5) {
            if (MessageObject.isDocumentHasThumb(this.documentAttach)) {
               var189 = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
               this.radialProgress.setImageOverlay(var189, this.documentAttach, var228);
            } else {
               var156 = var228.getArtworkUrl(true);
               if (!TextUtils.isEmpty(var156)) {
                  this.radialProgress.setImageOverlay(var156);
               } else {
                  this.radialProgress.setImageOverlay((TLRPC.PhotoSize)null, (TLRPC.Document)null, (Object)null);
               }
            }
         } else {
            this.radialProgress.setImageOverlay((TLRPC.PhotoSize)null, (TLRPC.Document)null, (Object)null);
         }
      }

      this.updateWaveform();
      if (var9 && !var1.cancelEditing) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.updateButtonState(false, var3, true);
      if (this.buttonState == 2 && this.documentAttachType == 3 && DownloadController.getInstance(this.currentAccount).canDownloadMedia(var1)) {
         FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.currentMessageObject, 1, 0);
         this.buttonState = 4;
         this.radialProgress.setIcon(this.getIconForCurrentState(), false, false);
      }

      this.accessibilityVirtualViewBounds.clear();
   }

   private void setMessageObjectInternal(MessageObject var1) {
      if ((var1.messageOwner.flags & 1024) != 0 && !this.currentMessageObject.viewsReloaded) {
         MessagesController.getInstance(this.currentAccount).addToViewsQueue(this.currentMessageObject);
         this.currentMessageObject.viewsReloaded = true;
      }

      this.updateCurrentUserAndChat();
      TLRPC.User var2;
      TLRPC.Chat var32;
      if (this.isAvatarVisible) {
         var2 = this.currentUser;
         if (var2 != null) {
            TLRPC.UserProfilePhoto var30 = var2.photo;
            if (var30 != null) {
               this.currentPhoto = var30.photo_small;
            } else {
               this.currentPhoto = null;
            }

            this.avatarDrawable.setInfo(this.currentUser);
            this.avatarImage.setImage(ImageLocation.getForUser(this.currentUser, false), "50_50", this.avatarDrawable, (String)null, this.currentUser, 0);
         } else {
            var32 = this.currentChat;
            if (var32 != null) {
               TLRPC.ChatPhoto var34 = var32.photo;
               if (var34 != null) {
                  this.currentPhoto = var34.photo_small;
               } else {
                  this.currentPhoto = null;
               }

               this.avatarDrawable.setInfo(this.currentChat);
               this.avatarImage.setImage(ImageLocation.getForChat(this.currentChat, false), "50_50", this.avatarDrawable, (String)null, this.currentChat, 0);
            } else {
               this.currentPhoto = null;
               this.avatarDrawable.setInfo(var1.messageOwner.from_id, (String)null, (String)null, false);
               this.avatarImage.setImage((ImageLocation)null, (String)null, this.avatarDrawable, (String)null, (Object)null, 0);
            }
         }
      } else {
         this.currentPhoto = null;
      }

      TLRPC.User var3;
      Object var4;
      String var37;
      label471: {
         this.measureTime(var1);
         this.namesOffset = 0;
         TLRPC.Message var35 = var1.messageOwner;
         StringBuilder var38;
         if (var35.via_bot_id != 0) {
            var3 = MessagesController.getInstance(this.currentAccount).getUser(var1.messageOwner.via_bot_id);
            if (var3 != null) {
               var37 = var3.username;
               if (var37 != null && var37.length() > 0) {
                  var38 = new StringBuilder();
                  var38.append("@");
                  var38.append(var3.username);
                  var37 = var38.toString();
                  var4 = AndroidUtilities.replaceTags(String.format(" %s <b>%s</b>", LocaleController.getString("ViaBot", 2131561039), var37));
                  this.viaWidth = (int)Math.ceil((double)Theme.chat_replyNamePaint.measureText((CharSequence)var4, 0, ((CharSequence)var4).length()));
                  this.currentViaBotUser = var3;
                  break label471;
               }
            }
         } else {
            var37 = var35.via_bot_name;
            if (var37 != null && var37.length() > 0) {
               var38 = new StringBuilder();
               var38.append("@");
               var38.append(var1.messageOwner.via_bot_name);
               var37 = var38.toString();
               var4 = AndroidUtilities.replaceTags(String.format(" %s <b>%s</b>", LocaleController.getString("ViaBot", 2131561039), var37));
               this.viaWidth = (int)Math.ceil((double)Theme.chat_replyNamePaint.measureText((CharSequence)var4, 0, ((CharSequence)var4).length()));
               break label471;
            }
         }

         var37 = null;
         var4 = var37;
      }

      boolean var5;
      if (this.drawName && this.isChat && !this.currentMessageObject.isOutOwner()) {
         var5 = true;
      } else {
         var5 = false;
      }

      boolean var6;
      if ((var1.messageOwner.fwd_from == null || var1.type == 14) && var37 != null) {
         var6 = true;
      } else {
         var6 = false;
      }

      int var7;
      TLRPC.User var8;
      Exception var10000;
      boolean var10001;
      String var36;
      int var42;
      int var48;
      TLRPC.Chat var51;
      String var52;
      CharSequence var56;
      CharSequence var65;
      if (!var5 && !var6) {
         this.currentNameString = null;
         this.nameLayout = null;
         this.nameWidth = 0;
         var36 = "ViaBot";
      } else {
         this.drawNameLayout = true;
         this.nameWidth = this.getMaxNameWidth();
         if (this.nameWidth < 0) {
            this.nameWidth = AndroidUtilities.dp(100.0F);
         }

         if (this.isMegagroup && this.currentChat != null && this.currentMessageObject.isForwardedChannelPost()) {
            var36 = LocaleController.getString("DiscussChannel", 2131559279);
            var7 = (int)Math.ceil((double)Theme.chat_adminPaint.measureText(var36));
            this.nameWidth -= var7;
         } else if (this.currentUser != null && !this.currentMessageObject.isOutOwner() && !this.currentMessageObject.isAnyKindOfSticker() && this.currentMessageObject.type != 5 && this.delegate.isChatAdminCell(this.currentUser.id)) {
            var36 = LocaleController.getString("ChatAdmin", 2131559021);
            var7 = (int)Math.ceil((double)Theme.chat_adminPaint.measureText(var36));
            this.nameWidth -= var7;
         } else {
            var7 = 0;
            var36 = null;
         }

         if (var5) {
            var8 = this.currentUser;
            if (var8 != null) {
               this.currentNameString = UserObject.getUserName(var8);
            } else {
               var51 = this.currentChat;
               if (var51 != null) {
                  this.currentNameString = var51.title;
               } else {
                  this.currentNameString = "DELETED";
               }
            }
         } else {
            this.currentNameString = "";
         }

         var52 = this.currentNameString.replace('\n', ' ');
         TextPaint var9 = Theme.chat_namePaint;
         int var10 = this.nameWidth;
         if (var6) {
            var42 = this.viaWidth;
         } else {
            var42 = 0;
         }

         var56 = TextUtils.ellipsize(var52, var9, (float)(var10 - var42), TruncateAt.END);
         if (var6) {
            this.viaNameWidth = (int)Math.ceil((double)Theme.chat_namePaint.measureText(var56, 0, var56.length()));
            var48 = this.viaNameWidth;
            if (var48 != 0) {
               this.viaNameWidth = var48 + AndroidUtilities.dp(4.0F);
            }

            if (this.currentMessageObject.shouldDrawWithoutBackground()) {
               var48 = Theme.getColor("chat_stickerViaBotNameText");
            } else {
               if (this.currentMessageObject.isOutOwner()) {
                  var52 = "chat_outViaBotNameText";
               } else {
                  var52 = "chat_inViaBotNameText";
               }

               var48 = Theme.getColor(var52);
            }

            SpannableStringBuilder var63;
            if (this.currentNameString.length() > 0) {
               var63 = new SpannableStringBuilder(String.format("%s %s %s", var56, LocaleController.getString("ViaBot", 2131561039), var37));
               var63.setSpan(new TypefaceSpan(Typeface.DEFAULT, 0, var48), var56.length() + 1, var56.length() + 4, 33);
               var63.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, var48), var56.length() + 5, var63.length(), 33);
            } else {
               var63 = new SpannableStringBuilder(String.format("%s %s", LocaleController.getString("ViaBot", 2131561039), var37));
               var63.setSpan(new TypefaceSpan(Typeface.DEFAULT, 0, var48), 0, 4, 33);
               var63.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, var48), 4, var63.length(), 33);
            }

            var65 = TextUtils.ellipsize(var63, Theme.chat_namePaint, (float)this.nameWidth, TruncateAt.END);
         } else {
            var65 = var56;
         }

         String var60 = "ViaBot";

         label429: {
            label428: {
               label427: {
                  label426: {
                     try {
                        StaticLayout var11 = new StaticLayout(var65, Theme.chat_namePaint, this.nameWidth + AndroidUtilities.dp(2.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                        this.nameLayout = var11;
                        if (this.nameLayout == null || this.nameLayout.getLineCount() <= 0) {
                           break label426;
                        }

                        this.nameWidth = (int)Math.ceil((double)this.nameLayout.getLineWidth(0));
                        if (!var1.isAnyKindOfSticker()) {
                           this.namesOffset += AndroidUtilities.dp(19.0F);
                        }
                     } catch (Exception var28) {
                        var10000 = var28;
                        var10001 = false;
                        break label428;
                     }

                     try {
                        this.nameOffsetX = this.nameLayout.getLineLeft(0);
                        break label427;
                     } catch (Exception var27) {
                        var10000 = var27;
                        var10001 = false;
                        break label428;
                     }
                  }

                  try {
                     this.nameWidth = 0;
                  } catch (Exception var26) {
                     var10000 = var26;
                     var10001 = false;
                     break label428;
                  }
               }

               if (var36 != null) {
                  try {
                     StaticLayout var67 = new StaticLayout(var36, Theme.chat_adminPaint, var7 + AndroidUtilities.dp(2.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                     this.adminLayout = var67;
                     this.nameWidth = (int)((float)this.nameWidth + this.adminLayout.getLineWidth(0) + (float)AndroidUtilities.dp(8.0F));
                     break label429;
                  } catch (Exception var24) {
                     var10000 = var24;
                     var10001 = false;
                  }
               } else {
                  try {
                     this.adminLayout = null;
                     break label429;
                  } catch (Exception var25) {
                     var10000 = var25;
                     var10001 = false;
                  }
               }
            }

            Exception var39 = var10000;
            FileLog.e((Throwable)var39);
         }

         if (this.currentNameString.length() == 0) {
            this.currentNameString = null;
            var36 = var60;
         } else {
            var36 = var60;
         }
      }

      this.currentForwardUser = null;
      this.currentForwardNameString = null;
      this.currentForwardChannel = null;
      this.currentForwardName = null;
      StaticLayout[] var68 = this.forwardedNameLayout;
      var68[0] = null;
      var68[1] = null;
      this.forwardedNameWidth = 0;
      CharSequence var43;
      CharSequence var54;
      if (this.drawForwardedName && var1.needDrawForwarded()) {
         MessageObject.GroupedMessagePosition var69 = this.currentPosition;
         if (var69 == null || var69.minY == 0) {
            if (var1.messageOwner.fwd_from.channel_id != 0) {
               this.currentForwardChannel = MessagesController.getInstance(this.currentAccount).getChat(var1.messageOwner.fwd_from.channel_id);
            }

            if (var1.messageOwner.fwd_from.from_id != 0) {
               this.currentForwardUser = MessagesController.getInstance(this.currentAccount).getUser(var1.messageOwner.fwd_from.from_id);
            }

            var52 = var1.messageOwner.fwd_from.from_name;
            if (var52 != null) {
               this.currentForwardName = var52;
            }

            if (this.currentForwardUser != null || this.currentForwardChannel != null || this.currentForwardName != null) {
               label495: {
                  var51 = this.currentForwardChannel;
                  if (var51 != null) {
                     TLRPC.User var62 = this.currentForwardUser;
                     if (var62 != null) {
                        this.currentForwardNameString = String.format("%s (%s)", var51.title, UserObject.getUserName(var62));
                     } else {
                        this.currentForwardNameString = var51.title;
                     }
                  } else {
                     var8 = this.currentForwardUser;
                     if (var8 != null) {
                        this.currentForwardNameString = UserObject.getUserName(var8);
                     } else {
                        var52 = this.currentForwardName;
                        if (var52 != null) {
                           this.currentForwardNameString = var52;
                        }
                     }
                  }

                  this.forwardedNameWidth = this.getMaxNameWidth();
                  String var12 = LocaleController.getString("From", 2131559574);
                  String var66 = LocaleController.getString("FromFormatted", 2131559582);
                  var48 = var66.indexOf("%1$s");
                  TextPaint var70 = Theme.chat_forwardNamePaint;
                  StringBuilder var64 = new StringBuilder();
                  var64.append(var12);
                  var64.append(" ");
                  var7 = (int)Math.ceil((double)var70.measureText(var64.toString()));
                  var56 = TextUtils.ellipsize(this.currentForwardNameString.replace('\n', ' '), Theme.chat_replyNamePaint, (float)(this.forwardedNameWidth - var7 - this.viaWidth), TruncateAt.END);

                  try {
                     var52 = String.format(var66, var56);
                  } catch (Exception var14) {
                     var52 = var56.toString();
                  }

                  SpannableStringBuilder var49;
                  if (var4 != null) {
                     SpannableStringBuilder var40 = new SpannableStringBuilder(String.format("%s %s %s", var52, LocaleController.getString(var36, 2131561039), var37));
                     this.viaNameWidth = (int)Math.ceil((double)Theme.chat_forwardNamePaint.measureText(var52));
                     var40.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), var40.length() - var37.length() - 1, var40.length(), 33);
                     var49 = var40;
                  } else {
                     var49 = new SpannableStringBuilder(String.format(var66, var56));
                  }

                  this.forwardNameCenterX = var7 + (int)Math.ceil((double)Theme.chat_forwardNamePaint.measureText(var56, 0, var56.length())) / 2;
                  if (var48 >= 0 && (this.currentForwardName == null || var1.messageOwner.fwd_from.from_id != 0)) {
                     var49.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), var48, var56.length() + var48, 33);
                  }

                  var54 = TextUtils.ellipsize(var49, Theme.chat_forwardNamePaint, (float)this.forwardedNameWidth, TruncateAt.END);

                  label483: {
                     StaticLayout[] var41;
                     StaticLayout var45;
                     try {
                        var41 = this.forwardedNameLayout;
                        var45 = new StaticLayout(var54, Theme.chat_forwardNamePaint, this.forwardedNameWidth + AndroidUtilities.dp(2.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                     } catch (Exception var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label483;
                     }

                     var41[1] = var45;

                     StaticLayout[] var55;
                     try {
                        var43 = TextUtils.ellipsize(AndroidUtilities.replaceTags(LocaleController.getString("ForwardedMessage", 2131559530)), Theme.chat_forwardNamePaint, (float)this.forwardedNameWidth, TruncateAt.END);
                        var55 = this.forwardedNameLayout;
                        var45 = new StaticLayout(var43, Theme.chat_forwardNamePaint, this.forwardedNameWidth + AndroidUtilities.dp(2.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                     } catch (Exception var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label483;
                     }

                     var55[0] = var45;

                     try {
                        this.forwardedNameWidth = Math.max((int)Math.ceil((double)this.forwardedNameLayout[0].getLineWidth(0)), (int)Math.ceil((double)this.forwardedNameLayout[1].getLineWidth(0)));
                        this.forwardNameOffsetX[0] = this.forwardedNameLayout[0].getLineLeft(0);
                        this.forwardNameOffsetX[1] = this.forwardedNameLayout[1].getLineLeft(0);
                        if (var1.type != 5) {
                           this.namesOffset += AndroidUtilities.dp(36.0F);
                        }
                        break label495;
                     } catch (Exception var21) {
                        var10000 = var21;
                        var10001 = false;
                     }
                  }

                  Exception var57 = var10000;
                  FileLog.e((Throwable)var57);
               }
            }
         }
      }

      if (var1.hasValidReplyMessageObject()) {
         MessageObject.GroupedMessagePosition var58 = this.currentPosition;
         if (var58 == null || var58.minY == 0) {
            label501: {
               if (!var1.isAnyKindOfSticker() && var1.type != 5) {
                  this.namesOffset += AndroidUtilities.dp(42.0F);
                  if (var1.type != 0) {
                     this.namesOffset += AndroidUtilities.dp(5.0F);
                  }
               }

               var48 = this.getMaxNameWidth();
               if (!var1.shouldDrawWithoutBackground()) {
                  var7 = var48 - AndroidUtilities.dp(10.0F);
               } else {
                  var7 = var48;
                  if (var1.type == 5) {
                     var7 = var48 + AndroidUtilities.dp(13.0F);
                  }
               }

               TLRPC.PhotoSize var47 = FileLoader.getClosestPhotoSizeWithSize(var1.replyMessageObject.photoThumbs2, 320);
               TLRPC.PhotoSize var61 = FileLoader.getClosestPhotoSizeWithSize(var1.replyMessageObject.photoThumbs2, 40);
               MessageObject var72 = var1.replyMessageObject;
               TLObject var44 = var72.photoThumbsObject2;
               byte var59;
               if (var47 == null) {
                  if (var72.mediaExists) {
                     var61 = FileLoader.getClosestPhotoSizeWithSize(var72.photoThumbs, AndroidUtilities.getPhotoSize());
                     if (var61 != null) {
                        var42 = var61.size;
                     } else {
                        var42 = 0;
                     }

                     var59 = 0;
                  } else {
                     var61 = FileLoader.getClosestPhotoSizeWithSize(var72.photoThumbs, 320);
                     var42 = 0;
                     var59 = 1;
                  }

                  TLRPC.PhotoSize var46 = FileLoader.getClosestPhotoSizeWithSize(var1.replyMessageObject.photoThumbs, 40);
                  TLObject var73 = var1.replyMessageObject.photoThumbsObject;
                  var47 = var61;
                  var61 = var46;
                  var44 = var73;
               } else {
                  var42 = 0;
                  var59 = 1;
               }

               TLRPC.PhotoSize var74 = var61;
               if (var61 == var47) {
                  var74 = null;
               }

               if (var47 != null && !var1.replyMessageObject.isAnyKindOfSticker() && (!var1.isAnyKindOfSticker() || AndroidUtilities.isTablet()) && !var1.replyMessageObject.isSecretMedia()) {
                  if (var1.replyMessageObject.isRoundVideo()) {
                     this.replyImageReceiver.setRoundRadius(AndroidUtilities.dp(22.0F));
                  } else {
                     this.replyImageReceiver.setRoundRadius(0);
                  }

                  this.currentReplyPhoto = var47;
                  this.replyImageReceiver.setImage(ImageLocation.getForObject(var47, var44), "50_50", ImageLocation.getForObject(var74, var44), "50_50_b", var42, (String)null, var1.replyMessageObject, var59);
                  this.needReplyImage = true;
                  var7 -= AndroidUtilities.dp(44.0F);
               } else {
                  this.replyImageReceiver.setImageBitmap((Drawable)null);
                  this.needReplyImage = false;
               }

               var3 = null;
               var37 = var1.customReplyName;
               if (var37 == null) {
                  label489: {
                     if (var1.replyMessageObject.isFromUser()) {
                        var2 = MessagesController.getInstance(this.currentAccount).getUser(var1.replyMessageObject.messageOwner.from_id);
                        if (var2 != null) {
                           var37 = UserObject.getUserName(var2);
                           break label489;
                        }
                     } else if (var1.replyMessageObject.messageOwner.from_id < 0) {
                        var32 = MessagesController.getInstance(this.currentAccount).getChat(-var1.replyMessageObject.messageOwner.from_id);
                        if (var32 != null) {
                           var37 = var32.title;
                           break label489;
                        }
                     } else {
                        var32 = MessagesController.getInstance(this.currentAccount).getChat(var1.replyMessageObject.messageOwner.to_id.channel_id);
                        if (var32 != null) {
                           var37 = var32.title;
                           break label489;
                        }
                     }

                     var37 = null;
                  }
               }

               String var50 = var37;
               if (var37 == null) {
                  var50 = LocaleController.getString("Loading", 2131559768);
               }

               var37 = var50.replace('\n', ' ');
               TextPaint var53 = Theme.chat_replyNamePaint;
               float var13 = (float)var7;
               var43 = TextUtils.ellipsize(var37, var53, var13, TruncateAt.END);
               MessageObject var71 = var1.replyMessageObject;
               TLRPC.MessageMedia var75 = var71.messageOwner.media;
               if (var75 instanceof TLRPC.TL_messageMediaGame) {
                  var54 = TextUtils.ellipsize(Emoji.replaceEmoji(var75.game.title, Theme.chat_replyTextPaint.getFontMetricsInt(), AndroidUtilities.dp(14.0F), false), Theme.chat_replyTextPaint, var13, TruncateAt.END);
               } else if (var75 instanceof TLRPC.TL_messageMediaInvoice) {
                  var54 = TextUtils.ellipsize(Emoji.replaceEmoji(var75.title, Theme.chat_replyTextPaint.getFontMetricsInt(), AndroidUtilities.dp(14.0F), false), Theme.chat_replyTextPaint, var13, TruncateAt.END);
               } else {
                  var65 = var71.messageText;
                  var54 = var3;
                  if (var65 != null) {
                     var54 = var3;
                     if (var65.length() > 0) {
                        var37 = var1.replyMessageObject.messageText.toString();
                        String var29 = var37;
                        if (var37.length() > 150) {
                           var29 = var37.substring(0, 150);
                        }

                        var54 = TextUtils.ellipsize(Emoji.replaceEmoji(var29.replace('\n', ' '), Theme.chat_replyTextPaint.getFontMetricsInt(), AndroidUtilities.dp(14.0F), false), Theme.chat_replyTextPaint, var13, TruncateAt.END);
                     }
                  }
               }

               Exception var31;
               StaticLayout var33;
               label336: {
                  label486: {
                     label334: {
                        label333: {
                           try {
                              if (this.needReplyImage) {
                                 break label333;
                              }
                           } catch (Exception var20) {
                              var10000 = var20;
                              var10001 = false;
                              break label486;
                           }

                           var59 = 0;
                           break label334;
                        }

                        var59 = 44;
                     }

                     try {
                        this.replyNameWidth = AndroidUtilities.dp((float)(4 + var59));
                     } catch (Exception var19) {
                        var10000 = var19;
                        var10001 = false;
                        break label486;
                     }

                     if (var43 == null) {
                        break label336;
                     }

                     try {
                        var33 = new StaticLayout(var43, Theme.chat_replyNamePaint, var7 + AndroidUtilities.dp(6.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                        this.replyNameLayout = var33;
                        if (this.replyNameLayout.getLineCount() > 0) {
                           this.replyNameWidth += (int)Math.ceil((double)this.replyNameLayout.getLineWidth(0)) + AndroidUtilities.dp(8.0F);
                           this.replyNameOffset = this.replyNameLayout.getLineLeft(0);
                        }
                        break label336;
                     } catch (Exception var18) {
                        var10000 = var18;
                        var10001 = false;
                     }
                  }

                  var31 = var10000;
                  FileLog.e((Throwable)var31);
               }

               label487: {
                  label317: {
                     label316: {
                        try {
                           if (this.needReplyImage) {
                              break label316;
                           }
                        } catch (Exception var17) {
                           var10000 = var17;
                           var10001 = false;
                           break label487;
                        }

                        var59 = 0;
                        break label317;
                     }

                     var59 = 44;
                  }

                  try {
                     this.replyTextWidth = AndroidUtilities.dp((float)(4 + var59));
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label487;
                  }

                  if (var54 == null) {
                     break label501;
                  }

                  try {
                     var33 = new StaticLayout(var54, Theme.chat_replyTextPaint, var7 + AndroidUtilities.dp(6.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                     this.replyTextLayout = var33;
                     if (this.replyTextLayout.getLineCount() > 0) {
                        this.replyTextWidth += (int)Math.ceil((double)this.replyTextLayout.getLineWidth(0)) + AndroidUtilities.dp(8.0F);
                        this.replyTextOffset = this.replyTextLayout.getLineLeft(0);
                     }
                     break label501;
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                  }
               }

               var31 = var10000;
               FileLog.e((Throwable)var31);
            }
         }
      }

      this.requestLayout();
   }

   private void updateCurrentUserAndChat() {
      MessagesController var1 = MessagesController.getInstance(this.currentAccount);
      TLRPC.MessageFwdHeader var2 = this.currentMessageObject.messageOwner.fwd_from;
      int var3 = UserConfig.getInstance(this.currentAccount).getClientUserId();
      if (var2 != null && var2.channel_id != 0 && this.currentMessageObject.getDialogId() == (long)var3) {
         this.currentChat = MessagesController.getInstance(this.currentAccount).getChat(var2.channel_id);
      } else {
         if (var2 != null) {
            TLRPC.Peer var4 = var2.saved_from_peer;
            if (var4 != null) {
               int var5 = var4.user_id;
               if (var5 != 0) {
                  var3 = var2.from_id;
                  if (var3 != 0) {
                     this.currentUser = var1.getUser(var3);
                  } else {
                     this.currentUser = var1.getUser(var5);
                  }

                  return;
               } else {
                  if (var4.channel_id != 0) {
                     if (this.currentMessageObject.isSavedFromMegagroup()) {
                        var3 = var2.from_id;
                        if (var3 != 0) {
                           this.currentUser = var1.getUser(var3);
                           return;
                        }
                     }

                     this.currentChat = var1.getChat(var2.saved_from_peer.channel_id);
                  } else {
                     var3 = var4.chat_id;
                     if (var3 != 0) {
                        var5 = var2.from_id;
                        if (var5 != 0) {
                           this.currentUser = var1.getUser(var5);
                        } else {
                           this.currentChat = var1.getChat(var3);
                        }

                        return;
                     }
                  }

                  return;
               }
            }
         }

         if (var2 != null && var2.from_id != 0 && var2.channel_id == 0 && this.currentMessageObject.getDialogId() == (long)var3) {
            this.currentUser = var1.getUser(var2.from_id);
         } else if (var2 != null && !TextUtils.isEmpty(var2.from_name) && this.currentMessageObject.getDialogId() == (long)var3) {
            this.currentUser = new TLRPC.TL_user();
            this.currentUser.first_name = var2.from_name;
         } else if (this.currentMessageObject.isFromUser()) {
            this.currentUser = var1.getUser(this.currentMessageObject.messageOwner.from_id);
         } else {
            TLRPC.Message var6 = this.currentMessageObject.messageOwner;
            var3 = var6.from_id;
            if (var3 < 0) {
               this.currentChat = var1.getChat(-var3);
            } else if (var6.post) {
               this.currentChat = var1.getChat(var6.to_id.channel_id);
            }
         }
      }

   }

   private void updatePollAnimations() {
      long var1 = System.currentTimeMillis();
      long var3 = var1 - this.voteLastUpdateTime;
      long var5 = var3;
      if (var3 > 17L) {
         var5 = 17L;
      }

      this.voteLastUpdateTime = var1;
      if (this.pollVoteInProgress) {
         this.voteRadOffset += (float)(360L * var5) / 2000.0F;
         float var7 = this.voteRadOffset;
         int var8 = (int)(var7 / 360.0F);
         short var9 = 360;
         this.voteRadOffset = var7 - (float)(var8 * 360);
         this.voteCurrentProgressTime += (float)var5;
         if (this.voteCurrentProgressTime >= 500.0F) {
            this.voteCurrentProgressTime = 500.0F;
         }

         if (this.voteRisingCircleLength) {
            this.voteCurrentCircleLength = AndroidUtilities.accelerateInterpolator.getInterpolation(this.voteCurrentProgressTime / 500.0F) * 266.0F + 4.0F;
         } else {
            if (!this.firstCircleLength) {
               var9 = 270;
            }

            this.voteCurrentCircleLength = 4.0F - (float)var9 * (1.0F - AndroidUtilities.decelerateInterpolator.getInterpolation(this.voteCurrentProgressTime / 500.0F));
         }

         if (this.voteCurrentProgressTime == 500.0F) {
            if (this.voteRisingCircleLength) {
               this.voteRadOffset += 270.0F;
               this.voteCurrentCircleLength = -266.0F;
            }

            this.voteRisingCircleLength ^= true;
            if (this.firstCircleLength) {
               this.firstCircleLength = false;
            }

            this.voteCurrentProgressTime = 0.0F;
         }

         this.invalidate();
      }

      if (this.animatePollAnswer) {
         this.pollAnimationProgressTime += (float)var5;
         if (this.pollAnimationProgressTime >= 300.0F) {
            this.pollAnimationProgressTime = 300.0F;
         }

         this.pollAnimationProgress = AndroidUtilities.decelerateInterpolator.getInterpolation(this.pollAnimationProgressTime / 300.0F);
         if (this.pollAnimationProgress >= 1.0F) {
            this.pollAnimationProgress = 1.0F;
            this.animatePollAnswer = false;
            this.animatePollAnswerAlpha = false;
            this.pollVoteInProgress = false;
            this.pollUnvoteInProgress = false;
         }

         this.invalidate();
      }

   }

   private void updateRadialProgressBackground() {
      if (!this.drawRadialCheckBackground) {
         boolean var1 = this.isHighlighted;
         boolean var2 = true;
         boolean var3;
         if ((var1 || this.isPressed || this.isPressed()) && (!this.drawPhotoImage || !this.photoImage.hasBitmapImage())) {
            var3 = true;
         } else {
            var3 = false;
         }

         RadialProgress2 var4 = this.radialProgress;
         if (!var3 && this.buttonPressed == 0) {
            var1 = false;
         } else {
            var1 = true;
         }

         var4.setPressed(var1, false);
         if (this.hasMiniProgress != 0) {
            var4 = this.radialProgress;
            if (!var3 && this.miniButtonPressed == 0) {
               var1 = false;
            } else {
               var1 = true;
            }

            var4.setPressed(var1, true);
         }

         var4 = this.videoRadialProgress;
         var1 = var2;
         if (!var3) {
            if (this.videoButtonPressed != 0) {
               var1 = var2;
            } else {
               var1 = false;
            }
         }

         var4.setPressed(var1, false);
      }
   }

   private void updateSecretTimeText(MessageObject var1) {
      if (var1 != null && var1.needDrawBluredPreview()) {
         String var2 = var1.getSecretTimeString();
         if (var2 == null) {
            return;
         }

         this.infoWidth = (int)Math.ceil((double)Theme.chat_infoPaint.measureText(var2));
         this.infoLayout = new StaticLayout(TextUtils.ellipsize(var2, Theme.chat_infoPaint, (float)this.infoWidth, TruncateAt.END), Theme.chat_infoPaint, this.infoWidth, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         this.invalidate();
      }

   }

   private void updateWaveform() {
      if (this.currentMessageObject != null && this.documentAttachType == 3) {
         boolean var1 = false;

         for(int var2 = 0; var2 < this.documentAttach.attributes.size(); ++var2) {
            TLRPC.DocumentAttribute var3 = (TLRPC.DocumentAttribute)this.documentAttach.attributes.get(var2);
            if (var3 instanceof TLRPC.TL_documentAttributeAudio) {
               byte[] var4 = var3.waveform;
               if (var4 == null || var4.length == 0) {
                  MediaController.getInstance().generateWaveform(this.currentMessageObject);
               }

               if (var3.waveform != null) {
                  var1 = true;
               }

               this.useSeekBarWaweform = var1;
               this.seekBarWaveform.setWaveform(var3.waveform);
               break;
            }
         }
      }

   }

   public void checkVideoPlayback(boolean var1) {
      if (this.currentMessageObject.isVideo()) {
         if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
            this.photoImage.setAllowStartAnimation(false);
            this.photoImage.stopAnimation();
         } else {
            this.photoImage.setAllowStartAnimation(true);
            this.photoImage.startAnimation();
         }
      } else {
         boolean var2 = var1;
         if (var1) {
            MessageObject var3 = MediaController.getInstance().getPlayingMessageObject();
            if (var3 != null && var3.isRoundVideo()) {
               var2 = false;
            } else {
               var2 = true;
            }
         }

         this.photoImage.setAllowStartAnimation(var2);
         if (var2) {
            this.photoImage.startAnimation();
         } else {
            this.photoImage.stopAnimation();
         }
      }

   }

   public void didSetImage(ImageReceiver var1, boolean var2, boolean var3) {
      MessageObject var5 = this.currentMessageObject;
      if (var5 != null && var2 && !var3 && !var5.mediaExists && !var5.attachPathExists) {
         label25: {
            if (var5.type == 0) {
               int var4 = this.documentAttachType;
               if (var4 == 8 || var4 == 0 || var4 == 6) {
                  break label25;
               }
            }

            if (this.currentMessageObject.type != 1) {
               return;
            }
         }

         this.currentMessageObject.mediaExists = true;
         this.updateButtonState(false, true, false);
      }

   }

   public void drawCaptionLayout(Canvas var1, boolean var2) {
      if (this.captionLayout != null && (!var2 || this.pressedLink != null)) {
         var1.save();
         var1.translate((float)this.captionX, (float)this.captionY);
         CharacterStyle var3 = this.pressedLink;
         byte var4 = 0;
         int var5;
         if (var3 != null) {
            for(var5 = 0; var5 < this.urlPath.size(); ++var5) {
               var1.drawPath((Path)this.urlPath.get(var5), Theme.chat_urlPaint);
            }
         }

         if (!this.urlPathSelection.isEmpty()) {
            for(var5 = var4; var5 < this.urlPathSelection.size(); ++var5) {
               var1.drawPath((Path)this.urlPathSelection.get(var5), Theme.chat_textSearchSelectionPaint);
            }
         }

         if (!var2) {
            try {
               this.captionLayout.draw(var1);
            } catch (Exception var6) {
               FileLog.e((Throwable)var6);
            }
         }

         var1.restore();
      }

   }

   public void drawCheckBox(Canvas var1) {
      MessageObject var2 = this.currentMessageObject;
      if (var2 != null && !var2.isSending() && !this.currentMessageObject.isSendError() && this.checkBox != null && (this.checkBoxVisible || this.checkBoxAnimationInProgress)) {
         MessageObject.GroupedMessagePosition var4 = this.currentPosition;
         if (var4 != null) {
            int var3 = var4.flags;
            if ((var3 & 8) == 0 || (var3 & 1) == 0) {
               return;
            }
         }

         var1.save();
         var1.translate(0.0F, (float)this.getTop());
         this.checkBox.draw(var1);
         var1.restore();
      }

   }

   public void drawNamesLayout(Canvas var1) {
      boolean var2 = this.drawNameLayout;
      float var3 = 17.0F;
      byte var4 = 0;
      int var5;
      float var6;
      TextPaint var8;
      String var12;
      if (var2 && this.nameLayout != null) {
         var1.save();
         if (this.currentMessageObject.shouldDrawWithoutBackground()) {
            Theme.chat_namePaint.setColor(Theme.getColor("chat_stickerNameText"));
            if (this.currentMessageObject.isOutOwner()) {
               this.nameX = (float)AndroidUtilities.dp(28.0F);
            } else {
               this.nameX = (float)(this.backgroundDrawableLeft + this.backgroundDrawableRight + AndroidUtilities.dp(22.0F));
            }

            this.nameY = (float)(this.layoutHeight - AndroidUtilities.dp(38.0F));
            Theme.chat_systemDrawable.setColorFilter(Theme.colorFilter);
            Theme.chat_systemDrawable.setBounds((int)this.nameX - AndroidUtilities.dp(12.0F), (int)this.nameY - AndroidUtilities.dp(5.0F), (int)this.nameX + AndroidUtilities.dp(12.0F) + this.nameWidth, (int)this.nameY + AndroidUtilities.dp(22.0F));
            Theme.chat_systemDrawable.draw(var1);
         } else {
            if (!this.mediaBackground && !this.currentMessageObject.isOutOwner()) {
               var5 = this.backgroundDrawableLeft;
               if (!this.mediaBackground && this.drawPinnedBottom) {
                  var6 = 11.0F;
               } else {
                  var6 = 17.0F;
               }

               this.nameX = (float)(var5 + AndroidUtilities.dp(var6)) - this.nameOffsetX;
            } else {
               this.nameX = (float)(this.backgroundDrawableLeft + AndroidUtilities.dp(11.0F)) - this.nameOffsetX;
            }

            TLRPC.User var7 = this.currentUser;
            if (var7 != null) {
               Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(var7.id));
            } else {
               TLRPC.Chat var11 = this.currentChat;
               if (var11 != null) {
                  if (ChatObject.isChannel(var11) && !this.currentChat.megagroup) {
                     Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(5));
                  } else {
                     Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(this.currentChat.id));
                  }
               } else {
                  Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(0));
               }
            }

            if (this.drawPinnedTop) {
               var6 = 9.0F;
            } else {
               var6 = 10.0F;
            }

            this.nameY = (float)AndroidUtilities.dp(var6);
         }

         var1.translate(this.nameX, this.nameY);
         this.nameLayout.draw(var1);
         var1.restore();
         if (this.adminLayout != null) {
            var8 = Theme.chat_adminPaint;
            if (this.isDrawSelectionBackground()) {
               var12 = "chat_adminSelectedText";
            } else {
               var12 = "chat_adminText";
            }

            var8.setColor(Theme.getColor(var12));
            var1.save();
            var1.translate((float)(this.backgroundDrawableLeft + this.backgroundDrawableRight - AndroidUtilities.dp(11.0F)) - this.adminLayout.getLineWidth(0), this.nameY + (float)AndroidUtilities.dp(0.5F));
            this.adminLayout.draw(var1);
            var1.restore();
         }
      }

      int var9;
      byte var10;
      MessageObject.GroupedMessagePosition var14;
      if (this.drawForwardedName) {
         StaticLayout[] var13 = this.forwardedNameLayout;
         if (var13[0] != null && var13[1] != null) {
            var14 = this.currentPosition;
            if (var14 == null || var14.minY == 0 && var14.minX == 0) {
               if (this.currentMessageObject.type == 5) {
                  Theme.chat_forwardNamePaint.setColor(Theme.getColor("chat_stickerReplyNameText"));
                  if (this.currentMessageObject.isOutOwner()) {
                     this.forwardNameX = AndroidUtilities.dp(23.0F);
                  } else {
                     this.forwardNameX = this.backgroundDrawableLeft + this.backgroundDrawableRight + AndroidUtilities.dp(17.0F);
                  }

                  this.forwardNameY = AndroidUtilities.dp(12.0F);
                  var9 = this.forwardedNameWidth;
                  var5 = AndroidUtilities.dp(14.0F);
                  Theme.chat_systemDrawable.setColorFilter(Theme.colorFilter);
                  Theme.chat_systemDrawable.setBounds(this.forwardNameX - AndroidUtilities.dp(7.0F), this.forwardNameY - AndroidUtilities.dp(6.0F), this.forwardNameX - AndroidUtilities.dp(7.0F) + var9 + var5, this.forwardNameY + AndroidUtilities.dp(38.0F));
                  Theme.chat_systemDrawable.draw(var1);
               } else {
                  if (this.drawNameLayout) {
                     var10 = 19;
                  } else {
                     var10 = 0;
                  }

                  this.forwardNameY = AndroidUtilities.dp((float)(var10 + 10));
                  if (this.currentMessageObject.isOutOwner()) {
                     Theme.chat_forwardNamePaint.setColor(Theme.getColor("chat_outForwardedNameText"));
                     this.forwardNameX = this.backgroundDrawableLeft + AndroidUtilities.dp(11.0F);
                  } else {
                     Theme.chat_forwardNamePaint.setColor(Theme.getColor("chat_inForwardedNameText"));
                     var2 = this.mediaBackground;
                     if (var2) {
                        this.forwardNameX = this.backgroundDrawableLeft + AndroidUtilities.dp(11.0F);
                     } else {
                        var5 = this.backgroundDrawableLeft;
                        var6 = var3;
                        if (!var2) {
                           var6 = var3;
                           if (this.drawPinnedBottom) {
                              var6 = 11.0F;
                           }
                        }

                        this.forwardNameX = var5 + AndroidUtilities.dp(var6);
                     }
                  }
               }

               for(var5 = 0; var5 < 2; ++var5) {
                  var1.save();
                  var1.translate((float)this.forwardNameX - this.forwardNameOffsetX[var5], (float)(this.forwardNameY + AndroidUtilities.dp(16.0F) * var5));
                  this.forwardedNameLayout[var5].draw(var1);
                  var1.restore();
               }
            }
         }
      }

      if (this.replyNameLayout != null) {
         if (this.currentMessageObject.shouldDrawWithoutBackground()) {
            Theme.chat_replyLinePaint.setColor(Theme.getColor("chat_stickerReplyLine"));
            Theme.chat_replyNamePaint.setColor(Theme.getColor("chat_stickerReplyNameText"));
            Theme.chat_replyTextPaint.setColor(Theme.getColor("chat_stickerReplyMessageText"));
            var5 = Math.max(this.replyNameWidth, this.replyTextWidth);
            var9 = AndroidUtilities.dp(14.0F);
            Theme.chat_systemDrawable.setColorFilter(Theme.colorFilter);
            Theme.chat_systemDrawable.setBounds(this.replyStartX - AndroidUtilities.dp(7.0F), this.replyStartY - AndroidUtilities.dp(6.0F), this.replyStartX - AndroidUtilities.dp(7.0F) + var5 + var9, this.replyStartY + AndroidUtilities.dp(41.0F));
            Theme.chat_systemDrawable.draw(var1);
         } else {
            MessageObject var15;
            TLRPC.MessageMedia var16;
            if (this.currentMessageObject.isOutOwner()) {
               label198: {
                  Theme.chat_replyLinePaint.setColor(Theme.getColor("chat_outReplyLine"));
                  Theme.chat_replyNamePaint.setColor(Theme.getColor("chat_outReplyNameText"));
                  if (this.currentMessageObject.hasValidReplyMessageObject()) {
                     var15 = this.currentMessageObject.replyMessageObject;
                     if (var15.type == 0) {
                        var16 = var15.messageOwner.media;
                        if (!(var16 instanceof TLRPC.TL_messageMediaGame) && !(var16 instanceof TLRPC.TL_messageMediaInvoice)) {
                           Theme.chat_replyTextPaint.setColor(Theme.getColor("chat_outReplyMessageText"));
                           break label198;
                        }
                     }
                  }

                  var8 = Theme.chat_replyTextPaint;
                  if (this.isDrawSelectionBackground()) {
                     var12 = "chat_outReplyMediaMessageSelectedText";
                  } else {
                     var12 = "chat_outReplyMediaMessageText";
                  }

                  var8.setColor(Theme.getColor(var12));
               }
            } else {
               label201: {
                  Theme.chat_replyLinePaint.setColor(Theme.getColor("chat_inReplyLine"));
                  Theme.chat_replyNamePaint.setColor(Theme.getColor("chat_inReplyNameText"));
                  if (this.currentMessageObject.hasValidReplyMessageObject()) {
                     var15 = this.currentMessageObject.replyMessageObject;
                     if (var15.type == 0) {
                        var16 = var15.messageOwner.media;
                        if (!(var16 instanceof TLRPC.TL_messageMediaGame) && !(var16 instanceof TLRPC.TL_messageMediaInvoice)) {
                           Theme.chat_replyTextPaint.setColor(Theme.getColor("chat_inReplyMessageText"));
                           break label201;
                        }
                     }
                  }

                  var8 = Theme.chat_replyTextPaint;
                  if (this.isDrawSelectionBackground()) {
                     var12 = "chat_inReplyMediaMessageSelectedText";
                  } else {
                     var12 = "chat_inReplyMediaMessageText";
                  }

                  var8.setColor(Theme.getColor(var12));
               }
            }
         }

         var14 = this.currentPosition;
         if (var14 == null || var14.minY == 0 && var14.minX == 0) {
            var5 = this.replyStartX;
            var1.drawRect((float)var5, (float)this.replyStartY, (float)(var5 + AndroidUtilities.dp(2.0F)), (float)(this.replyStartY + AndroidUtilities.dp(35.0F)), Theme.chat_replyLinePaint);
            if (this.needReplyImage) {
               this.replyImageReceiver.setImageCoords(this.replyStartX + AndroidUtilities.dp(10.0F), this.replyStartY, AndroidUtilities.dp(35.0F), AndroidUtilities.dp(35.0F));
               this.replyImageReceiver.draw(var1);
            }

            if (this.replyNameLayout != null) {
               var1.save();
               var6 = (float)this.replyStartX;
               var3 = this.replyNameOffset;
               if (this.needReplyImage) {
                  var10 = 44;
               } else {
                  var10 = 0;
               }

               var1.translate(var6 - var3 + (float)AndroidUtilities.dp((float)(var10 + 10)), (float)this.replyStartY);
               this.replyNameLayout.draw(var1);
               var1.restore();
            }

            if (this.replyTextLayout != null) {
               var1.save();
               var6 = (float)this.replyStartX;
               var3 = this.replyTextOffset;
               var10 = var4;
               if (this.needReplyImage) {
                  var10 = 44;
               }

               var1.translate(var6 - var3 + (float)AndroidUtilities.dp((float)(var10 + 10)), (float)(this.replyStartY + AndroidUtilities.dp(19.0F)));
               this.replyTextLayout.draw(var1);
               var1.restore();
            }
         }
      }

   }

   public void drawOverlays(Canvas var1) {
      long var2 = SystemClock.uptimeMillis();
      long var4 = var2 - this.lastAnimationTime;
      long var6 = var4;
      if (var4 > 17L) {
         var6 = 17L;
      }

      this.lastAnimationTime = var2;
      int var8;
      if (this.currentMessageObject.hadAnimationNotReadyLoading && this.photoImage.getVisible() && !this.currentMessageObject.needDrawBluredPreview()) {
         var8 = this.documentAttachType;
         if (var8 == 7 || var8 == 4 || var8 == 2) {
            AnimatedFileDrawable var9 = this.photoImage.getAnimation();
            if (var9 != null && var9.hasBitmap()) {
               this.currentMessageObject.hadAnimationNotReadyLoading = false;
               this.updateButtonState(false, true, false);
            }
         }
      }

      float var12;
      String var27;
      label517: {
         MessageObject var24 = this.currentMessageObject;
         int var10 = var24.type;
         int var13;
         int var14;
         int var15;
         float var17;
         Drawable var25;
         StaticLayout var26;
         if (var10 != 1) {
            var8 = this.documentAttachType;
            if (var8 != 4 && var8 != 2) {
               TextPaint var11;
               if (var10 == 4) {
                  if (this.docTitleLayout != null) {
                     if (var24.isOutOwner()) {
                        Theme.chat_locationTitlePaint.setColor(Theme.getColor("chat_messageTextOut"));
                        var11 = Theme.chat_locationAddressPaint;
                        if (this.isDrawSelectionBackground()) {
                           var27 = "chat_outVenueInfoSelectedText";
                        } else {
                           var27 = "chat_outVenueInfoText";
                        }

                        var11.setColor(Theme.getColor(var27));
                     } else {
                        Theme.chat_locationTitlePaint.setColor(Theme.getColor("chat_messageTextIn"));
                        var11 = Theme.chat_locationAddressPaint;
                        if (this.isDrawSelectionBackground()) {
                           var27 = "chat_inVenueInfoSelectedText";
                        } else {
                           var27 = "chat_inVenueInfoText";
                        }

                        var11.setColor(Theme.getColor(var27));
                     }

                     if (this.currentMessageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive) {
                        var8 = this.photoImage.getImageY2() + AndroidUtilities.dp(30.0F);
                        if (!this.locationExpired) {
                           this.forceNotDrawTime = true;
                           var12 = (float)Math.abs(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - this.currentMessageObject.messageOwner.date) / (float)this.currentMessageObject.messageOwner.media.period;
                           this.rect.set((float)(this.photoImage.getImageX2() - AndroidUtilities.dp(43.0F)), (float)(var8 - AndroidUtilities.dp(15.0F)), (float)(this.photoImage.getImageX2() - AndroidUtilities.dp(13.0F)), (float)(AndroidUtilities.dp(15.0F) + var8));
                           if (this.currentMessageObject.isOutOwner()) {
                              Theme.chat_radialProgress2Paint.setColor(Theme.getColor("chat_outInstant"));
                              Theme.chat_livePaint.setColor(Theme.getColor("chat_outInstant"));
                           } else {
                              Theme.chat_radialProgress2Paint.setColor(Theme.getColor("chat_inInstant"));
                              Theme.chat_livePaint.setColor(Theme.getColor("chat_inInstant"));
                           }

                           Theme.chat_radialProgress2Paint.setAlpha(50);
                           var1.drawCircle(this.rect.centerX(), this.rect.centerY(), (float)AndroidUtilities.dp(15.0F), Theme.chat_radialProgress2Paint);
                           Theme.chat_radialProgress2Paint.setAlpha(255);
                           var1.drawArc(this.rect, -90.0F, -360.0F * (1.0F - var12), false, Theme.chat_radialProgress2Paint);
                           var27 = LocaleController.formatLocationLeftTime(Math.abs(this.currentMessageObject.messageOwner.media.period - (ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - this.currentMessageObject.messageOwner.date)));
                           var12 = Theme.chat_livePaint.measureText(var27);
                           var1.drawText(var27, this.rect.centerX() - var12 / 2.0F, (float)(var8 + AndroidUtilities.dp(4.0F)), Theme.chat_livePaint);
                           var1.save();
                           var1.translate((float)(this.photoImage.getImageX() + AndroidUtilities.dp(10.0F)), (float)(this.photoImage.getImageY2() + AndroidUtilities.dp(10.0F)));
                           this.docTitleLayout.draw(var1);
                           var1.translate(0.0F, (float)AndroidUtilities.dp(23.0F));
                           this.infoLayout.draw(var1);
                           var1.restore();
                        }

                        var10 = this.photoImage.getImageX() + this.photoImage.getImageWidth() / 2 - AndroidUtilities.dp(31.0F);
                        var8 = this.photoImage.getImageY() + this.photoImage.getImageHeight() / 2 - AndroidUtilities.dp(38.0F);
                        BaseCell.setDrawableBounds(Theme.chat_msgAvatarLiveLocationDrawable, var10, var8);
                        Theme.chat_msgAvatarLiveLocationDrawable.draw(var1);
                        this.locationImageReceiver.setImageCoords(var10 + AndroidUtilities.dp(5.0F), var8 + AndroidUtilities.dp(5.0F), AndroidUtilities.dp(52.0F), AndroidUtilities.dp(52.0F));
                        this.locationImageReceiver.draw(var1);
                     } else {
                        var1.save();
                        var1.translate((float)(this.photoImage.getImageX() + AndroidUtilities.dp(6.0F)), (float)(this.photoImage.getImageY2() + AndroidUtilities.dp(8.0F)));
                        this.docTitleLayout.draw(var1);
                        if (this.infoLayout != null) {
                           var1.translate(0.0F, (float)AndroidUtilities.dp(21.0F));
                           this.infoLayout.draw(var1);
                        }

                        var1.restore();
                     }
                  }
               } else if (var10 == 16) {
                  if (var24.isOutOwner()) {
                     Theme.chat_audioTitlePaint.setColor(Theme.getColor("chat_messageTextOut"));
                     var11 = Theme.chat_contactPhonePaint;
                     if (this.isDrawSelectionBackground()) {
                        var27 = "chat_outTimeSelectedText";
                     } else {
                        var27 = "chat_outTimeText";
                     }

                     var11.setColor(Theme.getColor(var27));
                  } else {
                     Theme.chat_audioTitlePaint.setColor(Theme.getColor("chat_messageTextIn"));
                     var11 = Theme.chat_contactPhonePaint;
                     if (this.isDrawSelectionBackground()) {
                        var27 = "chat_inTimeSelectedText";
                     } else {
                        var27 = "chat_inTimeText";
                     }

                     var11.setColor(Theme.getColor(var27));
                  }

                  this.forceNotDrawTime = true;
                  if (this.currentMessageObject.isOutOwner()) {
                     var8 = this.layoutWidth - this.backgroundWidth + AndroidUtilities.dp(16.0F);
                  } else if (this.isChat && this.currentMessageObject.needDrawAvatar()) {
                     var8 = AndroidUtilities.dp(74.0F);
                  } else {
                     var8 = AndroidUtilities.dp(25.0F);
                  }

                  this.otherX = var8;
                  if (this.titleLayout != null) {
                     var1.save();
                     var1.translate((float)var8, (float)(AndroidUtilities.dp(12.0F) + this.namesOffset));
                     this.titleLayout.draw(var1);
                     var1.restore();
                  }

                  if (this.docTitleLayout != null) {
                     var1.save();
                     var1.translate((float)(AndroidUtilities.dp(19.0F) + var8), (float)(AndroidUtilities.dp(37.0F) + this.namesOffset));
                     this.docTitleLayout.draw(var1);
                     var1.restore();
                  }

                  Drawable var28;
                  if (this.currentMessageObject.isOutOwner()) {
                     var28 = Theme.chat_msgCallUpGreenDrawable;
                     if (!this.isDrawSelectionBackground() && !this.otherPressed) {
                        var25 = Theme.chat_msgOutCallDrawable;
                     } else {
                        var25 = Theme.chat_msgOutCallSelectedDrawable;
                     }
                  } else {
                     TLRPC.PhoneCallDiscardReason var32 = this.currentMessageObject.messageOwner.action.reason;
                     if (!(var32 instanceof TLRPC.TL_phoneCallDiscardReasonMissed) && !(var32 instanceof TLRPC.TL_phoneCallDiscardReasonBusy)) {
                        var28 = Theme.chat_msgCallDownGreenDrawable;
                     } else {
                        var28 = Theme.chat_msgCallDownRedDrawable;
                     }

                     if (!this.isDrawSelectionBackground() && !this.otherPressed) {
                        var25 = Theme.chat_msgInCallDrawable;
                     } else {
                        var25 = Theme.chat_msgInCallSelectedDrawable;
                     }
                  }

                  BaseCell.setDrawableBounds(var28, var8 - AndroidUtilities.dp(3.0F), AndroidUtilities.dp(36.0F) + this.namesOffset);
                  var28.draw(var1);
                  var10 = AndroidUtilities.dp(205.0F);
                  var13 = AndroidUtilities.dp(22.0F);
                  this.otherY = var13;
                  BaseCell.setDrawableBounds(var25, var8 + var10, var13);
                  var25.draw(var1);
               } else if (var10 == 17) {
                  if (var24.isOutOwner()) {
                     var8 = Theme.getColor("chat_messageTextOut");
                     Theme.chat_audioTitlePaint.setColor(var8);
                     Theme.chat_audioPerformerPaint.setColor(var8);
                     Theme.chat_instantViewPaint.setColor(var8);
                     if (this.isDrawSelectionBackground()) {
                        var27 = "chat_outTimeSelectedText";
                     } else {
                        var27 = "chat_outTimeText";
                     }

                     var8 = Theme.getColor(var27);
                     Theme.chat_timePaint.setColor(var8);
                     Theme.chat_livePaint.setColor(var8);
                  } else {
                     var8 = Theme.getColor("chat_messageTextIn");
                     Theme.chat_audioTitlePaint.setColor(var8);
                     Theme.chat_audioPerformerPaint.setColor(var8);
                     Theme.chat_instantViewPaint.setColor(var8);
                     if (this.isDrawSelectionBackground()) {
                        var27 = "chat_inTimeSelectedText";
                     } else {
                        var27 = "chat_inTimeText";
                     }

                     var8 = Theme.getColor(var27);
                     Theme.chat_timePaint.setColor(var8);
                     Theme.chat_livePaint.setColor(var8);
                  }

                  if (this.currentMessageObject.isOutOwner()) {
                     var8 = this.layoutWidth - this.backgroundWidth + AndroidUtilities.dp(11.0F);
                  } else if (this.isChat && this.currentMessageObject.needDrawAvatar()) {
                     var8 = AndroidUtilities.dp(68.0F);
                  } else {
                     var8 = AndroidUtilities.dp(20.0F);
                  }

                  if (this.titleLayout != null) {
                     var1.save();
                     var1.translate((float)var8, (float)(AndroidUtilities.dp(15.0F) + this.namesOffset));
                     this.titleLayout.draw(var1);
                     var1.restore();
                  }

                  if (this.docTitleLayout != null) {
                     var1.save();
                     var12 = (float)(this.docTitleOffsetX + var8);
                     var26 = this.titleLayout;
                     if (var26 != null) {
                        var10 = var26.getHeight();
                     } else {
                        var10 = 0;
                     }

                     var1.translate(var12, (float)(var10 + AndroidUtilities.dp(20.0F) + this.namesOffset));
                     this.docTitleLayout.draw(var1);
                     var1.restore();
                  }

                  if (VERSION.SDK_INT >= 21) {
                     var25 = this.selectorDrawable;
                     if (var25 != null) {
                        var25.draw(var1);
                     }
                  }

                  var10 = this.pollButtons.size();
                  byte var30 = 0;
                  var15 = 0;
                  var13 = var8;

                  for(var8 = var30; var8 < var10; ++var8) {
                     ChatMessageCell.PollButton var29 = (ChatMessageCell.PollButton)this.pollButtons.get(var8);
                     var29.x = var13;
                     var1.save();
                     var1.translate((float)(AndroidUtilities.dp(34.0F) + var13), (float)(var29.y + this.namesOffset));
                     var29.title.draw(var1);
                     if (this.animatePollAnswerAlpha) {
                        if (this.pollUnvoteInProgress) {
                           var12 = 1.0F - this.pollAnimationProgress;
                        } else {
                           var12 = this.pollAnimationProgress;
                        }

                        var12 = Math.min(var12 / 0.3F, 1.0F) * 255.0F;
                     } else {
                        var12 = 255.0F;
                     }

                     var14 = (int)var12;
                     Paint var16;
                     if (this.pollVoted || this.pollClosed || this.animatePollAnswerAlpha) {
                        var16 = Theme.chat_docBackPaint;
                        if (this.currentMessageObject.isOutOwner()) {
                           var27 = "chat_outAudioSeekbarFill";
                        } else {
                           var27 = "chat_inAudioSeekbarFill";
                        }

                        var16.setColor(Theme.getColor(var27));
                        if (this.animatePollAnswerAlpha) {
                           var17 = (float)Theme.chat_instantViewPaint.getAlpha() / 255.0F;
                           TextPaint var33 = Theme.chat_instantViewPaint;
                           var12 = (float)var14;
                           var33.setAlpha((int)(var17 * var12));
                           var17 = (float)Theme.chat_docBackPaint.getAlpha() / 255.0F;
                           Theme.chat_docBackPaint.setAlpha((int)(var12 * var17));
                        }

                        var27 = String.format("%d%%", (int)Math.ceil((double)((float)var29.prevPercent + (float)(var29.percent - var29.prevPercent) * this.pollAnimationProgress)));
                        int var18 = (int)Math.ceil((double)Theme.chat_instantViewPaint.measureText(var27));
                        var1.drawText(var27, (float)(-AndroidUtilities.dp(7.0F) - var18), (float)AndroidUtilities.dp(14.0F), Theme.chat_instantViewPaint);
                        var18 = this.backgroundWidth;
                        int var19 = AndroidUtilities.dp(76.0F);
                        float var20 = var29.prevPercentProgress;
                        var17 = var29.percentProgress;
                        float var21 = var29.prevPercentProgress;
                        var12 = this.pollAnimationProgress;
                        this.instantButtonRect.set(0.0F, (float)(var29.height + AndroidUtilities.dp(6.0F)), (float)(var18 - var19) * (var20 + (var17 - var21) * var12), (float)(var29.height + AndroidUtilities.dp(11.0F)));
                        var1.drawRoundRect(this.instantButtonRect, (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(2.0F), Theme.chat_docBackPaint);
                     }

                     if (!this.pollVoted && !this.pollClosed || this.animatePollAnswerAlpha) {
                        if (this.isDrawSelectionBackground()) {
                           var16 = Theme.chat_replyLinePaint;
                           if (this.currentMessageObject.isOutOwner()) {
                              var27 = "chat_outVoiceSeekbarSelected";
                           } else {
                              var27 = "chat_inVoiceSeekbarSelected";
                           }

                           var16.setColor(Theme.getColor(var27));
                        } else {
                           var16 = Theme.chat_replyLinePaint;
                           if (this.currentMessageObject.isOutOwner()) {
                              var27 = "chat_outVoiceSeekbar";
                           } else {
                              var27 = "chat_inVoiceSeekbar";
                           }

                           var16.setColor(Theme.getColor(var27));
                        }

                        if (this.animatePollAnswerAlpha) {
                           var12 = (float)Theme.chat_replyLinePaint.getAlpha() / 255.0F;
                           Theme.chat_replyLinePaint.setAlpha((int)((float)(255 - var14) * var12));
                        }

                        var1.drawLine((float)(-AndroidUtilities.dp(2.0F)), (float)(var29.height + AndroidUtilities.dp(13.0F)), (float)(this.backgroundWidth - AndroidUtilities.dp(56.0F)), (float)(var29.height + AndroidUtilities.dp(13.0F)), Theme.chat_replyLinePaint);
                        if (this.pollVoteInProgress && var8 == this.pollVoteInProgressNum) {
                           var16 = Theme.chat_instantViewRectPaint;
                           if (this.currentMessageObject.isOutOwner()) {
                              var27 = "chat_outAudioSeekbarFill";
                           } else {
                              var27 = "chat_inAudioSeekbarFill";
                           }

                           var16.setColor(Theme.getColor(var27));
                           if (this.animatePollAnswerAlpha) {
                              var12 = (float)Theme.chat_instantViewRectPaint.getAlpha() / 255.0F;
                              Theme.chat_instantViewRectPaint.setAlpha((int)((float)(255 - var14) * var12));
                           }

                           this.instantButtonRect.set((float)(-AndroidUtilities.dp(23.0F) - AndroidUtilities.dp(8.5F)), (float)(AndroidUtilities.dp(9.0F) - AndroidUtilities.dp(8.5F)), (float)(-AndroidUtilities.dp(23.0F) + AndroidUtilities.dp(8.5F)), (float)(AndroidUtilities.dp(9.0F) + AndroidUtilities.dp(8.5F)));
                           var1.drawArc(this.instantButtonRect, this.voteRadOffset, this.voteCurrentCircleLength, false, Theme.chat_instantViewRectPaint);
                        } else {
                           if (this.currentMessageObject.isOutOwner()) {
                              var16 = Theme.chat_instantViewRectPaint;
                              if (this.isDrawSelectionBackground()) {
                                 var27 = "chat_outMenuSelected";
                              } else {
                                 var27 = "chat_outMenu";
                              }

                              var16.setColor(Theme.getColor(var27));
                           } else {
                              var16 = Theme.chat_instantViewRectPaint;
                              if (this.isDrawSelectionBackground()) {
                                 var27 = "chat_inMenuSelected";
                              } else {
                                 var27 = "chat_inMenu";
                              }

                              var16.setColor(Theme.getColor(var27));
                           }

                           if (this.animatePollAnswerAlpha) {
                              var12 = (float)Theme.chat_instantViewRectPaint.getAlpha() / 255.0F;
                              Theme.chat_instantViewRectPaint.setAlpha((int)((float)(255 - var14) * var12));
                           }

                           var1.drawCircle((float)(-AndroidUtilities.dp(23.0F)), (float)AndroidUtilities.dp(9.0F), (float)AndroidUtilities.dp(8.5F), Theme.chat_instantViewRectPaint);
                        }
                     }

                     var1.restore();
                     if (var8 == var10 - 1) {
                        var15 = var29.y + this.namesOffset + var29.height;
                     }
                  }

                  if (this.infoLayout != null) {
                     var1.save();
                     var1.translate((float)(var13 + this.infoX), (float)(var15 + AndroidUtilities.dp(22.0F)));
                     this.infoLayout.draw(var1);
                     var1.restore();
                  }

                  this.updatePollAnimations();
               } else if (var10 == 12) {
                  if (var24.isOutOwner()) {
                     Theme.chat_contactNamePaint.setColor(Theme.getColor("chat_outContactNameText"));
                     var11 = Theme.chat_contactPhonePaint;
                     if (this.isDrawSelectionBackground()) {
                        var27 = "chat_outContactPhoneSelectedText";
                     } else {
                        var27 = "chat_outContactPhoneText";
                     }

                     var11.setColor(Theme.getColor(var27));
                  } else {
                     Theme.chat_contactNamePaint.setColor(Theme.getColor("chat_inContactNameText"));
                     var11 = Theme.chat_contactPhonePaint;
                     if (this.isDrawSelectionBackground()) {
                        var27 = "chat_inContactPhoneSelectedText";
                     } else {
                        var27 = "chat_inContactPhoneText";
                     }

                     var11.setColor(Theme.getColor(var27));
                  }

                  if (this.titleLayout != null) {
                     var1.save();
                     var1.translate((float)(this.photoImage.getImageX() + this.photoImage.getImageWidth() + AndroidUtilities.dp(9.0F)), (float)(AndroidUtilities.dp(16.0F) + this.namesOffset));
                     this.titleLayout.draw(var1);
                     var1.restore();
                  }

                  if (this.docTitleLayout != null) {
                     var1.save();
                     var1.translate((float)(this.photoImage.getImageX() + this.photoImage.getImageWidth() + AndroidUtilities.dp(9.0F)), (float)(AndroidUtilities.dp(39.0F) + this.namesOffset));
                     this.docTitleLayout.draw(var1);
                     var1.restore();
                  }

                  if (this.currentMessageObject.isOutOwner()) {
                     if (this.isDrawSelectionBackground()) {
                        var25 = Theme.chat_msgOutMenuSelectedDrawable;
                     } else {
                        var25 = Theme.chat_msgOutMenuDrawable;
                     }
                  } else if (this.isDrawSelectionBackground()) {
                     var25 = Theme.chat_msgInMenuSelectedDrawable;
                  } else {
                     var25 = Theme.chat_msgInMenuDrawable;
                  }

                  var10 = this.photoImage.getImageX() + this.backgroundWidth - AndroidUtilities.dp(48.0F);
                  this.otherX = var10;
                  var8 = this.photoImage.getImageY() - AndroidUtilities.dp(5.0F);
                  this.otherY = var8;
                  BaseCell.setDrawableBounds(var25, var10, var8);
                  var25.draw(var1);
                  if (this.drawInstantView) {
                     var10 = this.photoImage.getImageX() - AndroidUtilities.dp(2.0F);
                     var8 = this.getMeasuredHeight() - AndroidUtilities.dp(64.0F);
                     Paint var34 = Theme.chat_instantViewRectPaint;
                     if (this.currentMessageObject.isOutOwner()) {
                        Theme.chat_instantViewPaint.setColor(Theme.getColor("chat_outPreviewInstantText"));
                        var34.setColor(Theme.getColor("chat_outPreviewInstantText"));
                     } else {
                        Theme.chat_instantViewPaint.setColor(Theme.getColor("chat_inPreviewInstantText"));
                        var34.setColor(Theme.getColor("chat_inPreviewInstantText"));
                     }

                     if (VERSION.SDK_INT >= 21) {
                        this.selectorDrawable.setBounds(var10, var8, this.instantWidth + var10, AndroidUtilities.dp(36.0F) + var8);
                        this.selectorDrawable.draw(var1);
                     }

                     this.instantButtonRect.set((float)var10, (float)var8, (float)(this.instantWidth + var10), (float)(AndroidUtilities.dp(36.0F) + var8));
                     var1.drawRoundRect(this.instantButtonRect, (float)AndroidUtilities.dp(6.0F), (float)AndroidUtilities.dp(6.0F), var34);
                     if (this.instantViewLayout != null) {
                        var1.save();
                        var1.translate((float)(var10 + this.instantTextX), (float)(var8 + AndroidUtilities.dp(10.5F)));
                        this.instantViewLayout.draw(var1);
                        var1.restore();
                     }
                  }
               }
               break label517;
            }
         }

         if (this.photoImage.getVisible()) {
            if (!this.currentMessageObject.needDrawBluredPreview() && this.documentAttachType == 4) {
               var10 = ((BitmapDrawable)Theme.chat_msgMediaMenuDrawable).getPaint().getAlpha();
               if (this.drawPhotoCheckBox) {
                  Theme.chat_msgMediaMenuDrawable.setAlpha((int)((float)var10 * this.controlsAlpha * (1.0F - this.checkBoxAnimationProgress)));
               } else {
                  Theme.chat_msgMediaMenuDrawable.setAlpha((int)((float)var10 * this.controlsAlpha));
               }

               var25 = Theme.chat_msgMediaMenuDrawable;
               var8 = this.photoImage.getImageX() + this.photoImage.getImageWidth() - AndroidUtilities.dp(14.0F);
               this.otherX = var8;
               var13 = this.photoImage.getImageY() + AndroidUtilities.dp(8.1F);
               this.otherY = var13;
               BaseCell.setDrawableBounds(var25, var8, var13);
               Theme.chat_msgMediaMenuDrawable.draw(var1);
               Theme.chat_msgMediaMenuDrawable.setAlpha(var10);
            }

            boolean var22 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
            if (this.animatingNoSoundPlaying != var22) {
               this.animatingNoSoundPlaying = var22;
               byte var23;
               if (var22) {
                  var23 = 1;
               } else {
                  var23 = 2;
               }

               this.animatingNoSound = var23;
               if (var22) {
                  var12 = 1.0F;
               } else {
                  var12 = 0.0F;
               }

               this.animatingNoSoundProgress = var12;
            }

            var8 = this.buttonState;
            if (var8 == 1 || var8 == 2 || var8 == 0 || var8 == 3 || var8 == -1 || this.currentMessageObject.needDrawBluredPreview()) {
               if (this.autoPlayingMedia) {
                  this.updatePlayingMessageProgress();
               }

               if (this.infoLayout != null && (!this.forceNotDrawTime || this.autoPlayingMedia || this.drawVideoImageButton)) {
                  if (this.currentMessageObject.needDrawBluredPreview() && this.docTitleLayout == null) {
                     var12 = 0.0F;
                  } else {
                     var12 = this.animatingDrawVideoImageButtonProgress;
                  }

                  Theme.chat_infoPaint.setColor(Theme.getColor("chat_mediaInfoText"));
                  var13 = this.photoImage.getImageX() + AndroidUtilities.dp(4.0F);
                  var15 = this.photoImage.getImageY() + AndroidUtilities.dp(4.0F);
                  if (!this.autoPlayingMedia || var22 && this.animatingNoSound == 0) {
                     var8 = 0;
                  } else {
                     var8 = (int)((float)(Theme.chat_msgNoSoundDrawable.getIntrinsicWidth() + AndroidUtilities.dp(4.0F)) * this.animatingNoSoundProgress);
                  }

                  var17 = (float)(this.infoWidth + AndroidUtilities.dp(8.0F) + var8);
                  var14 = Math.max(this.infoWidth + var8, this.docTitleWidth);
                  if (this.canStreamVideo) {
                     var10 = AndroidUtilities.dp(32.0F);
                  } else {
                     var10 = 0;
                  }

                  var10 = (int)Math.ceil((double)(var17 + (float)(var14 + var10 - this.infoWidth - var8) * var12));
                  var17 = var12;
                  if (var12 != 0.0F) {
                     var17 = var12;
                     if (this.docTitleLayout == null) {
                        var17 = 0.0F;
                     }
                  }

                  this.rect.set((float)var13, (float)var15, (float)(var13 + var10), (float)(var15 + AndroidUtilities.dp(15.5F * var17 + 16.5F)));
                  var10 = Theme.chat_timeBackgroundPaint.getAlpha();
                  Theme.chat_timeBackgroundPaint.setAlpha((int)((float)var10 * this.controlsAlpha));
                  var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), Theme.chat_timeBackgroundPaint);
                  Theme.chat_timeBackgroundPaint.setAlpha(var10);
                  Theme.chat_infoPaint.setAlpha((int)(this.controlsAlpha * 255.0F));
                  var1.save();
                  var10 = this.photoImage.getImageX();
                  if (this.canStreamVideo) {
                     var12 = 30.0F * var17;
                  } else {
                     var12 = 0.0F;
                  }

                  var10 += AndroidUtilities.dp(var12 + 8.0F);
                  this.noSoundCenterX = var10;
                  var1.translate((float)var10, (float)(this.photoImage.getImageY() + AndroidUtilities.dp(0.2F * var17 + 5.5F)));
                  var26 = this.infoLayout;
                  if (var26 != null) {
                     var26.draw(var1);
                  }

                  if (var17 > 0.0F && this.docTitleLayout != null) {
                     var1.save();
                     Theme.chat_infoPaint.setAlpha((int)(this.controlsAlpha * 255.0F * var17));
                     var1.translate(0.0F, (float)AndroidUtilities.dp(var17 * 14.3F));
                     this.docTitleLayout.draw(var1);
                     var1.restore();
                  }

                  if (var8 != 0) {
                     var25 = Theme.chat_msgNoSoundDrawable;
                     var12 = this.animatingNoSoundProgress;
                     var25.setAlpha((int)(255.0F * var12 * var12 * this.controlsAlpha));
                     var1.translate((float)(this.infoWidth + AndroidUtilities.dp(4.0F)), 0.0F);
                     var10 = AndroidUtilities.dp(this.animatingNoSoundProgress * 14.0F);
                     var8 = (AndroidUtilities.dp(14.0F) - var10) / 2;
                     Theme.chat_msgNoSoundDrawable.setBounds(0, var8, var10, var8 + var10);
                     Theme.chat_msgNoSoundDrawable.draw(var1);
                     this.noSoundCenterX += this.infoWidth + AndroidUtilities.dp(4.0F) + var10 / 2;
                  }

                  var1.restore();
                  Theme.chat_infoPaint.setAlpha(255);
               }
            }

            var8 = this.animatingDrawVideoImageButton;
            if (var8 == 1) {
               this.animatingDrawVideoImageButtonProgress -= (float)var6 / 160.0F;
               if (this.animatingDrawVideoImageButtonProgress <= 0.0F) {
                  this.animatingDrawVideoImageButtonProgress = 0.0F;
                  this.animatingDrawVideoImageButton = 0;
               }

               this.invalidate();
            } else if (var8 == 2) {
               this.animatingDrawVideoImageButtonProgress += (float)var6 / 160.0F;
               if (this.animatingDrawVideoImageButtonProgress >= 1.0F) {
                  this.animatingDrawVideoImageButtonProgress = 1.0F;
                  this.animatingDrawVideoImageButton = 0;
               }

               this.invalidate();
            }

            var8 = this.animatingNoSound;
            if (var8 == 1) {
               this.animatingNoSoundProgress -= (float)var6 / 180.0F;
               if (this.animatingNoSoundProgress <= 0.0F) {
                  this.animatingNoSoundProgress = 0.0F;
                  this.animatingNoSound = 0;
               }

               this.invalidate();
            } else if (var8 == 2) {
               this.animatingNoSoundProgress += (float)var6 / 180.0F;
               if (this.animatingNoSoundProgress >= 1.0F) {
                  this.animatingNoSoundProgress = 1.0F;
                  this.animatingNoSound = 0;
               }

               this.invalidate();
            }
         }
      }

      if (this.drawImageButton && this.photoImage.getVisible()) {
         var12 = this.controlsAlpha;
         if (var12 != 1.0F) {
            this.radialProgress.setOverrideAlpha(var12);
         }

         this.radialProgress.draw(var1);
      }

      if ((this.drawVideoImageButton || this.animatingDrawVideoImageButton != 0) && this.photoImage.getVisible()) {
         var12 = this.controlsAlpha;
         if (var12 != 1.0F) {
            this.videoRadialProgress.setOverrideAlpha(var12);
         }

         this.videoRadialProgress.draw(var1);
      }

      if (this.drawPhotoCheckBox) {
         var8 = AndroidUtilities.dp(21.0F);
         CheckBoxBase var31 = this.photoCheckBox;
         if (this.currentMessageObject.isOutOwner()) {
            var27 = "chat_outBubbleSelected";
         } else {
            var27 = "chat_inBubbleSelected";
         }

         var31.setColor((String)null, (String)null, var27);
         this.photoCheckBox.setBounds(this.photoImage.getImageX2() - AndroidUtilities.dp(25.0F), this.photoImage.getImageY() + AndroidUtilities.dp(4.0F), var8, var8);
         this.photoCheckBox.draw(var1);
      }

   }

   public void drawRoundProgress(Canvas var1) {
      this.rect.set((float)this.photoImage.getImageX() + AndroidUtilities.dpf2(1.5F), (float)this.photoImage.getImageY() + AndroidUtilities.dpf2(1.5F), (float)this.photoImage.getImageX2() - AndroidUtilities.dpf2(1.5F), (float)this.photoImage.getImageY2() - AndroidUtilities.dpf2(1.5F));
      var1.drawArc(this.rect, -90.0F, this.currentMessageObject.audioProgress * 360.0F, false, Theme.chat_radialProgressPaint);
   }

   public void drawTime(Canvas var1) {
      if ((this.drawTime && !this.groupPhotoInvisible || !this.mediaBackground || this.captionLayout != null) && this.timeLayout != null) {
         MessageObject var2 = this.currentMessageObject;
         if (var2.type == 5) {
            Theme.chat_timePaint.setColor(Theme.getColor("chat_mediaTimeText"));
         } else if (this.mediaBackground && this.captionLayout == null) {
            if (var2.shouldDrawWithoutBackground()) {
               Theme.chat_timePaint.setColor(Theme.getColor("chat_serviceText"));
            } else {
               Theme.chat_timePaint.setColor(Theme.getColor("chat_mediaTimeText"));
            }
         } else {
            TextPaint var3;
            String var13;
            if (this.currentMessageObject.isOutOwner()) {
               var3 = Theme.chat_timePaint;
               if (this.isDrawSelectionBackground()) {
                  var13 = "chat_outTimeSelectedText";
               } else {
                  var13 = "chat_outTimeText";
               }

               var3.setColor(Theme.getColor(var13));
            } else {
               var3 = Theme.chat_timePaint;
               if (this.isDrawSelectionBackground()) {
                  var13 = "chat_inTimeSelectedText";
               } else {
                  var13 = "chat_inTimeText";
               }

               var3.setColor(Theme.getColor(var13));
            }
         }

         if (this.drawPinnedBottom) {
            var1.translate(0.0F, (float)AndroidUtilities.dp(2.0F));
         }

         boolean var4 = this.mediaBackground;
         boolean var5 = false;
         int var8;
         int var11;
         int var12;
         Drawable var15;
         if (var4 && this.captionLayout == null) {
            Paint var16;
            if (this.currentMessageObject.shouldDrawWithoutBackground()) {
               var16 = Theme.chat_actionBackgroundPaint;
            } else {
               var16 = Theme.chat_timeBackgroundPaint;
            }

            int var6 = var16.getAlpha();
            var16.setAlpha((int)((float)var6 * this.timeAlpha));
            Theme.chat_timePaint.setAlpha((int)(this.timeAlpha * 255.0F));
            int var7 = this.timeX - AndroidUtilities.dp(4.0F);
            var8 = this.layoutHeight - AndroidUtilities.dp(28.0F);
            RectF var14 = this.rect;
            float var9 = (float)var7;
            float var10 = (float)var8;
            var11 = this.timeWidth;
            byte var20;
            if (this.currentMessageObject.isOutOwner()) {
               var20 = 20;
            } else {
               var20 = 0;
            }

            var14.set(var9, var10, (float)(var7 + var11 + AndroidUtilities.dp((float)(var20 + 8))), (float)(var8 + AndroidUtilities.dp(17.0F)));
            var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), var16);
            var16.setAlpha(var6);
            var11 = (int)(-this.timeLayout.getLineLeft(0));
            var12 = var11;
            if ((this.currentMessageObject.messageOwner.flags & 1024) != 0) {
               var11 += (int)((float)this.timeWidth - this.timeLayout.getLineWidth(0));
               if (!this.currentMessageObject.isSending() && !this.currentMessageObject.isEditing()) {
                  if (this.currentMessageObject.isSendError()) {
                     var12 = var11;
                     if (!this.currentMessageObject.isOutOwner()) {
                        var12 = this.timeX + AndroidUtilities.dp(11.0F);
                        var8 = this.layoutHeight - AndroidUtilities.dp(27.5F);
                        this.rect.set((float)var12, (float)var8, (float)(AndroidUtilities.dp(14.0F) + var12), (float)(AndroidUtilities.dp(14.0F) + var8));
                        var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(1.0F), (float)AndroidUtilities.dp(1.0F), Theme.chat_msgErrorPaint);
                        BaseCell.setDrawableBounds(Theme.chat_msgErrorDrawable, var12 + AndroidUtilities.dp(6.0F), var8 + AndroidUtilities.dp(2.0F));
                        Theme.chat_msgErrorDrawable.draw(var1);
                        var12 = var11;
                     }
                  } else {
                     if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                        var15 = Theme.chat_msgStickerViewsDrawable;
                     } else {
                        var15 = Theme.chat_msgMediaViewsDrawable;
                     }

                     var12 = ((BitmapDrawable)var15).getPaint().getAlpha();
                     var15.setAlpha((int)(this.timeAlpha * (float)var12));
                     BaseCell.setDrawableBounds(var15, this.timeX, this.layoutHeight - AndroidUtilities.dp(10.5F) - this.timeLayout.getHeight());
                     var15.draw(var1);
                     var15.setAlpha(var12);
                     var12 = var11;
                     if (this.viewsLayout != null) {
                        var1.save();
                        var1.translate((float)(this.timeX + var15.getIntrinsicWidth() + AndroidUtilities.dp(3.0F)), (float)(this.layoutHeight - AndroidUtilities.dp(12.3F) - this.timeLayout.getHeight()));
                        this.viewsLayout.draw(var1);
                        var1.restore();
                        var12 = var11;
                     }
                  }
               } else {
                  var12 = var11;
                  if (!this.currentMessageObject.isOutOwner()) {
                     BaseCell.setDrawableBounds(Theme.chat_msgMediaClockDrawable, this.timeX + AndroidUtilities.dp(11.0F), this.layoutHeight - AndroidUtilities.dp(14.0F) - Theme.chat_msgMediaClockDrawable.getIntrinsicHeight());
                     Theme.chat_msgMediaClockDrawable.draw(var1);
                     var12 = var11;
                  }
               }
            }

            var1.save();
            var1.translate((float)(this.timeX + var12), (float)(this.layoutHeight - AndroidUtilities.dp(12.3F) - this.timeLayout.getHeight()));
            this.timeLayout.draw(var1);
            var1.restore();
            Theme.chat_timePaint.setAlpha(255);
         } else {
            var11 = (int)(-this.timeLayout.getLineLeft(0));
            var12 = var11;
            if ((this.currentMessageObject.messageOwner.flags & 1024) != 0) {
               var11 += (int)((float)this.timeWidth - this.timeLayout.getLineWidth(0));
               if (!this.currentMessageObject.isSending() && !this.currentMessageObject.isEditing()) {
                  if (this.currentMessageObject.isSendError()) {
                     var12 = var11;
                     if (!this.currentMessageObject.isOutOwner()) {
                        var12 = this.timeX + AndroidUtilities.dp(11.0F);
                        var8 = this.layoutHeight - AndroidUtilities.dp(20.5F);
                        this.rect.set((float)var12, (float)var8, (float)(AndroidUtilities.dp(14.0F) + var12), (float)(AndroidUtilities.dp(14.0F) + var8));
                        var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(1.0F), (float)AndroidUtilities.dp(1.0F), Theme.chat_msgErrorPaint);
                        BaseCell.setDrawableBounds(Theme.chat_msgErrorDrawable, var12 + AndroidUtilities.dp(6.0F), var8 + AndroidUtilities.dp(2.0F));
                        Theme.chat_msgErrorDrawable.draw(var1);
                        var12 = var11;
                     }
                  } else {
                     if (!this.currentMessageObject.isOutOwner()) {
                        if (this.isDrawSelectionBackground()) {
                           var15 = Theme.chat_msgInViewsSelectedDrawable;
                        } else {
                           var15 = Theme.chat_msgInViewsDrawable;
                        }

                        BaseCell.setDrawableBounds(var15, this.timeX, this.layoutHeight - AndroidUtilities.dp(4.5F) - this.timeLayout.getHeight());
                        var15.draw(var1);
                     } else {
                        if (this.isDrawSelectionBackground()) {
                           var15 = Theme.chat_msgOutViewsSelectedDrawable;
                        } else {
                           var15 = Theme.chat_msgOutViewsDrawable;
                        }

                        BaseCell.setDrawableBounds(var15, this.timeX, this.layoutHeight - AndroidUtilities.dp(4.5F) - this.timeLayout.getHeight());
                        var15.draw(var1);
                     }

                     var12 = var11;
                     if (this.viewsLayout != null) {
                        var1.save();
                        var1.translate((float)(this.timeX + Theme.chat_msgInViewsDrawable.getIntrinsicWidth() + AndroidUtilities.dp(3.0F)), (float)(this.layoutHeight - AndroidUtilities.dp(6.5F) - this.timeLayout.getHeight()));
                        this.viewsLayout.draw(var1);
                        var1.restore();
                        var12 = var11;
                     }
                  }
               } else {
                  var12 = var11;
                  if (!this.currentMessageObject.isOutOwner()) {
                     if (this.isDrawSelectionBackground()) {
                        var15 = Theme.chat_msgInSelectedClockDrawable;
                     } else {
                        var15 = Theme.chat_msgInClockDrawable;
                     }

                     BaseCell.setDrawableBounds(var15, this.timeX + AndroidUtilities.dp(11.0F), this.layoutHeight - AndroidUtilities.dp(8.5F) - var15.getIntrinsicHeight());
                     var15.draw(var1);
                     var12 = var11;
                  }
               }
            }

            var1.save();
            var1.translate((float)(this.timeX + var12), (float)(this.layoutHeight - AndroidUtilities.dp(6.5F) - this.timeLayout.getHeight()));
            this.timeLayout.draw(var1);
            var1.restore();
         }

         if (this.currentMessageObject.isOutOwner()) {
            var8 = (int)(this.currentMessageObject.getDialogId() >> 32);
            boolean var19 = true;
            boolean var21 = true;
            boolean var17;
            if (var8 == 1) {
               var17 = true;
            } else {
               var17 = false;
            }

            boolean var18;
            if (!this.currentMessageObject.isSending() && !this.currentMessageObject.isEditing()) {
               if (this.currentMessageObject.isSendError()) {
                  var21 = false;
                  var19 = false;
                  var18 = true;
               } else {
                  label222: {
                     if (this.currentMessageObject.isSent()) {
                        if (!this.currentMessageObject.isUnread()) {
                           var18 = true;
                           var21 = var19;
                           var19 = var18;
                           break label222;
                        }
                     } else {
                        var21 = false;
                     }

                     var19 = false;
                  }

                  var18 = false;
               }
            } else {
               var21 = false;
               var19 = false;
               var18 = false;
               var5 = true;
            }

            if (var5) {
               if (this.mediaBackground && this.captionLayout == null) {
                  if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                     Theme.chat_msgStickerClockDrawable.setAlpha((int)(this.timeAlpha * 255.0F));
                     BaseCell.setDrawableBounds(Theme.chat_msgStickerClockDrawable, this.layoutWidth - AndroidUtilities.dp(22.0F) - Theme.chat_msgStickerClockDrawable.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(13.5F) - Theme.chat_msgStickerClockDrawable.getIntrinsicHeight());
                     Theme.chat_msgStickerClockDrawable.draw(var1);
                     Theme.chat_msgStickerClockDrawable.setAlpha(255);
                  } else {
                     BaseCell.setDrawableBounds(Theme.chat_msgMediaClockDrawable, this.layoutWidth - AndroidUtilities.dp(22.0F) - Theme.chat_msgMediaClockDrawable.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(13.5F) - Theme.chat_msgMediaClockDrawable.getIntrinsicHeight());
                     Theme.chat_msgMediaClockDrawable.draw(var1);
                  }
               } else {
                  BaseCell.setDrawableBounds(Theme.chat_msgOutClockDrawable, this.layoutWidth - AndroidUtilities.dp(18.5F) - Theme.chat_msgOutClockDrawable.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(8.5F) - Theme.chat_msgOutClockDrawable.getIntrinsicHeight());
                  Theme.chat_msgOutClockDrawable.draw(var1);
               }
            }

            if (var17) {
               if (var19 || var21) {
                  if (this.mediaBackground && this.captionLayout == null) {
                     BaseCell.setDrawableBounds(Theme.chat_msgBroadcastMediaDrawable, this.layoutWidth - AndroidUtilities.dp(24.0F) - Theme.chat_msgBroadcastMediaDrawable.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(14.0F) - Theme.chat_msgBroadcastMediaDrawable.getIntrinsicHeight());
                     Theme.chat_msgBroadcastMediaDrawable.draw(var1);
                  } else {
                     BaseCell.setDrawableBounds(Theme.chat_msgBroadcastDrawable, this.layoutWidth - AndroidUtilities.dp(20.5F) - Theme.chat_msgBroadcastDrawable.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(8.0F) - Theme.chat_msgBroadcastDrawable.getIntrinsicHeight());
                     Theme.chat_msgBroadcastDrawable.draw(var1);
                  }
               }
            } else {
               if (var21) {
                  if (this.mediaBackground && this.captionLayout == null) {
                     if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                        if (var19) {
                           BaseCell.setDrawableBounds(Theme.chat_msgStickerCheckDrawable, this.layoutWidth - AndroidUtilities.dp(26.3F) - Theme.chat_msgStickerCheckDrawable.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(13.5F) - Theme.chat_msgStickerCheckDrawable.getIntrinsicHeight());
                        } else {
                           BaseCell.setDrawableBounds(Theme.chat_msgStickerCheckDrawable, this.layoutWidth - AndroidUtilities.dp(21.5F) - Theme.chat_msgStickerCheckDrawable.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(13.5F) - Theme.chat_msgStickerCheckDrawable.getIntrinsicHeight());
                        }

                        Theme.chat_msgStickerCheckDrawable.draw(var1);
                     } else {
                        if (var19) {
                           BaseCell.setDrawableBounds(Theme.chat_msgMediaCheckDrawable, this.layoutWidth - AndroidUtilities.dp(26.3F) - Theme.chat_msgMediaCheckDrawable.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(13.5F) - Theme.chat_msgMediaCheckDrawable.getIntrinsicHeight());
                        } else {
                           BaseCell.setDrawableBounds(Theme.chat_msgMediaCheckDrawable, this.layoutWidth - AndroidUtilities.dp(21.5F) - Theme.chat_msgMediaCheckDrawable.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(13.5F) - Theme.chat_msgMediaCheckDrawable.getIntrinsicHeight());
                        }

                        Theme.chat_msgMediaCheckDrawable.setAlpha((int)(this.timeAlpha * 255.0F));
                        Theme.chat_msgMediaCheckDrawable.draw(var1);
                        Theme.chat_msgMediaCheckDrawable.setAlpha(255);
                     }
                  } else {
                     if (this.isDrawSelectionBackground()) {
                        var15 = Theme.chat_msgOutCheckSelectedDrawable;
                     } else {
                        var15 = Theme.chat_msgOutCheckDrawable;
                     }

                     if (var19) {
                        BaseCell.setDrawableBounds(var15, this.layoutWidth - AndroidUtilities.dp(22.5F) - var15.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(8.0F) - var15.getIntrinsicHeight());
                     } else {
                        BaseCell.setDrawableBounds(var15, this.layoutWidth - AndroidUtilities.dp(18.5F) - var15.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(8.0F) - var15.getIntrinsicHeight());
                     }

                     var15.draw(var1);
                  }
               }

               if (var19) {
                  if (this.mediaBackground && this.captionLayout == null) {
                     if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                        BaseCell.setDrawableBounds(Theme.chat_msgStickerHalfCheckDrawable, this.layoutWidth - AndroidUtilities.dp(21.5F) - Theme.chat_msgStickerHalfCheckDrawable.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(13.5F) - Theme.chat_msgStickerHalfCheckDrawable.getIntrinsicHeight());
                        Theme.chat_msgStickerHalfCheckDrawable.draw(var1);
                     } else {
                        BaseCell.setDrawableBounds(Theme.chat_msgMediaHalfCheckDrawable, this.layoutWidth - AndroidUtilities.dp(21.5F) - Theme.chat_msgMediaHalfCheckDrawable.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(13.5F) - Theme.chat_msgMediaHalfCheckDrawable.getIntrinsicHeight());
                        Theme.chat_msgMediaHalfCheckDrawable.setAlpha((int)(this.timeAlpha * 255.0F));
                        Theme.chat_msgMediaHalfCheckDrawable.draw(var1);
                        Theme.chat_msgMediaHalfCheckDrawable.setAlpha(255);
                     }
                  } else {
                     if (this.isDrawSelectionBackground()) {
                        var15 = Theme.chat_msgOutHalfCheckSelectedDrawable;
                     } else {
                        var15 = Theme.chat_msgOutHalfCheckDrawable;
                     }

                     BaseCell.setDrawableBounds(var15, this.layoutWidth - AndroidUtilities.dp(18.0F) - var15.getIntrinsicWidth(), this.layoutHeight - AndroidUtilities.dp(8.0F) - var15.getIntrinsicHeight());
                     var15.draw(var1);
                  }
               }
            }

            if (var18) {
               if (this.mediaBackground && this.captionLayout == null) {
                  var12 = this.layoutWidth - AndroidUtilities.dp(34.5F);
                  var8 = this.layoutHeight;
                  var11 = AndroidUtilities.dp(26.5F);
               } else {
                  var12 = this.layoutWidth - AndroidUtilities.dp(32.0F);
                  var8 = this.layoutHeight;
                  var11 = AndroidUtilities.dp(21.0F);
               }

               var11 = var8 - var11;
               this.rect.set((float)var12, (float)var11, (float)(AndroidUtilities.dp(14.0F) + var12), (float)(AndroidUtilities.dp(14.0F) + var11));
               var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(1.0F), (float)AndroidUtilities.dp(1.0F), Theme.chat_msgErrorPaint);
               BaseCell.setDrawableBounds(Theme.chat_msgErrorDrawable, var12 + AndroidUtilities.dp(6.0F), var11 + AndroidUtilities.dp(2.0F));
               Theme.chat_msgErrorDrawable.draw(var1);
            }
         }

      }
   }

   public void forceResetMessageObject() {
      MessageObject var1 = this.messageObjectToSet;
      if (var1 == null) {
         var1 = this.currentMessageObject;
      }

      this.currentMessageObject = null;
      this.setMessageObject(var1, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
   }

   public AccessibilityNodeProvider getAccessibilityNodeProvider() {
      return new ChatMessageCell.MessageAccessibilityNodeProvider();
   }

   public ImageReceiver getAvatarImage() {
      ImageReceiver var1;
      if (this.isAvatarVisible) {
         var1 = this.avatarImage;
      } else {
         var1 = null;
      }

      return var1;
   }

   public int getBackgroundDrawableLeft() {
      boolean var1 = this.currentMessageObject.isOutOwner();
      byte var2 = 0;
      int var3 = 0;
      if (var1) {
         int var5 = this.layoutWidth;
         int var4 = this.backgroundWidth;
         if (this.mediaBackground) {
            var3 = AndroidUtilities.dp(9.0F);
         }

         return var5 - var4 - var3;
      } else {
         byte var6 = var2;
         if (this.isChat) {
            var6 = var2;
            if (this.isAvatarVisible) {
               var6 = 48;
            }
         }

         if (!this.mediaBackground) {
            var2 = 3;
         } else {
            var2 = 9;
         }

         return AndroidUtilities.dp((float)(var6 + var2));
      }
   }

   public int getCaptionHeight() {
      return this.addedCaptionHeight;
   }

   public float getCheckBoxTranslation() {
      return (float)this.checkBoxTranslation;
   }

   public MessageObject.GroupedMessages getCurrentMessagesGroup() {
      return this.currentMessagesGroup;
   }

   public MessageObject.GroupedMessagePosition getCurrentPosition() {
      return this.currentPosition;
   }

   public int getForwardNameCenterX() {
      TLRPC.User var1 = this.currentUser;
      return var1 != null && var1.id == 0 ? (int)this.avatarImage.getCenterX() : this.forwardNameX + this.forwardNameCenterX;
   }

   public float getHightlightAlpha() {
      boolean var1 = this.drawSelectionBackground;
      float var2 = 1.0F;
      float var3 = var2;
      if (!var1) {
         var3 = var2;
         if (this.isHighlightedAnimated) {
            int var4 = this.highlightProgress;
            if (var4 >= 300) {
               var3 = var2;
            } else {
               var3 = (float)var4 / 300.0F;
            }
         }
      }

      return var3;
   }

   public int getLayoutHeight() {
      return this.layoutHeight;
   }

   public MessageObject getMessageObject() {
      MessageObject var1 = this.messageObjectToSet;
      if (var1 == null) {
         var1 = this.currentMessageObject;
      }

      return var1;
   }

   public int getNoSoundIconCenterX() {
      return this.noSoundCenterX;
   }

   public int getObserverTag() {
      return this.TAG;
   }

   public ImageReceiver getPhotoImage() {
      return this.photoImage;
   }

   public TLRPC.Document getStreamingMedia() {
      int var1 = this.documentAttachType;
      TLRPC.Document var2;
      if (var1 != 4 && var1 != 7 && var1 != 2) {
         var2 = null;
      } else {
         var2 = this.documentAttach;
      }

      return var2;
   }

   public float getTimeAlpha() {
      return this.timeAlpha;
   }

   public boolean hasCaptionLayout() {
      boolean var1;
      if (this.captionLayout != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean hasNameLayout() {
      boolean var1 = this.drawNameLayout;
      boolean var2 = true;
      if (var1) {
         var1 = var2;
         if (this.nameLayout != null) {
            return var1;
         }
      }

      if (this.drawForwardedName) {
         StaticLayout[] var3 = this.forwardedNameLayout;
         if (var3[0] != null && var3[1] != null) {
            MessageObject.GroupedMessagePosition var4 = this.currentPosition;
            var1 = var2;
            if (var4 == null) {
               return var1;
            }

            if (var4.minY == 0) {
               var1 = var2;
               if (var4.minX == 0) {
                  return var1;
               }
            }
         }
      }

      if (this.replyNameLayout != null) {
         var1 = var2;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void invalidate() {
      super.invalidate();
      if (this.invalidatesParent && this.getParent() != null) {
         View var1 = (View)this.getParent();
         if (var1.getParent() != null) {
            ((View)var1.getParent()).invalidate();
         }
      }

   }

   public boolean isDrawNameLayout() {
      boolean var1;
      if (this.drawNameLayout && this.nameLayout != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isDrawingSelectionBackground() {
      boolean var1;
      if (!this.drawSelectionBackground && !this.isHighlightedAnimated && !this.isHighlighted) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isHighlighted() {
      return this.isHighlighted;
   }

   public boolean isInsideBackground(float var1, float var2) {
      boolean var4;
      if (this.currentBackgroundDrawable != null) {
         int var3 = this.backgroundDrawableLeft;
         if (var1 >= (float)var3 && var1 <= (float)(var3 + this.backgroundDrawableRight)) {
            var4 = true;
            return var4;
         }
      }

      var4 = false;
      return var4;
   }

   public boolean isPinnedBottom() {
      return this.pinnedBottom;
   }

   public boolean isPinnedTop() {
      return this.pinnedTop;
   }

   public boolean needDelayRoundProgressDraw() {
      int var1 = this.documentAttachType;
      boolean var2;
      if ((var1 == 7 || var1 == 4) && this.currentMessageObject.type != 5 && MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean needDrawTime() {
      return this.forceNotDrawTime ^ true;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      MessageObject var1 = this.messageObjectToSet;
      if (var1 != null) {
         this.setMessageContent(var1, this.groupedMessagesToSet, this.bottomNearToSet, this.topNearToSet);
         this.messageObjectToSet = null;
         this.groupedMessagesToSet = null;
      }

      CheckBoxBase var5 = this.checkBox;
      if (var5 != null) {
         var5.onAttachedToWindow();
      }

      var5 = this.photoCheckBox;
      if (var5 != null) {
         var5.onAttachedToWindow();
      }

      this.attachedToWindow = true;
      float var2 = 0.0F;
      float var3 = 0.0F;
      this.setTranslationX(0.0F);
      this.radialProgress.onAttachedToWindow();
      this.videoRadialProgress.onAttachedToWindow();
      this.avatarImage.onAttachedToWindow();
      this.avatarImage.setParentView((View)this.getParent());
      this.replyImageReceiver.onAttachedToWindow();
      this.locationImageReceiver.onAttachedToWindow();
      if (this.photoImage.onAttachedToWindow()) {
         if (this.drawPhotoImage) {
            this.updateButtonState(false, false, false);
         }
      } else {
         this.updateButtonState(false, false, false);
      }

      var1 = this.currentMessageObject;
      if (var1 != null && (var1.isRoundVideo() || this.currentMessageObject.isVideo())) {
         this.checkVideoPlayback(true);
      }

      if (this.documentAttachType == 4 && this.autoPlayingMedia) {
         this.animatingNoSoundPlaying = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
         if (!this.animatingNoSoundPlaying) {
            var3 = 1.0F;
         }

         this.animatingNoSoundProgress = var3;
         this.animatingNoSound = 0;
      } else {
         label39: {
            this.animatingNoSoundPlaying = false;
            this.animatingNoSoundProgress = 0.0F;
            int var4 = this.documentAttachType;
            if (var4 != 4) {
               var3 = var2;
               if (var4 != 2) {
                  break label39;
               }
            }

            var3 = var2;
            if (this.drawVideoSize) {
               var3 = 1.0F;
            }
         }

         this.animatingDrawVideoImageButtonProgress = var3;
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      CheckBoxBase var1 = this.checkBox;
      if (var1 != null) {
         var1.onDetachedFromWindow();
      }

      var1 = this.photoCheckBox;
      if (var1 != null) {
         var1.onDetachedFromWindow();
      }

      this.attachedToWindow = false;
      this.radialProgress.onDetachedFromWindow();
      this.videoRadialProgress.onDetachedFromWindow();
      this.avatarImage.onDetachedFromWindow();
      this.replyImageReceiver.onDetachedFromWindow();
      this.locationImageReceiver.onDetachedFromWindow();
      this.photoImage.onDetachedFromWindow();
      if (this.addedForTest && this.currentUrl != null && this.currentWebFile != null) {
         ImageLoader.getInstance().removeTestWebFile(this.currentUrl);
         this.addedForTest = false;
      }

      DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
   }

   protected void onDraw(Canvas var1) {
      MessageObject var2 = this.currentMessageObject;
      if (var2 != null) {
         if (!this.wasLayout) {
            this.requestLayout();
         } else {
            if (var2.isOutOwner()) {
               Theme.chat_msgTextPaint.setColor(Theme.getColor("chat_messageTextOut"));
               Theme.chat_msgTextPaint.linkColor = Theme.getColor("chat_messageLinkOut");
               Theme.chat_msgGameTextPaint.setColor(Theme.getColor("chat_messageTextOut"));
               Theme.chat_msgGameTextPaint.linkColor = Theme.getColor("chat_messageLinkOut");
               Theme.chat_replyTextPaint.linkColor = Theme.getColor("chat_messageLinkOut");
            } else {
               Theme.chat_msgTextPaint.setColor(Theme.getColor("chat_messageTextIn"));
               Theme.chat_msgTextPaint.linkColor = Theme.getColor("chat_messageLinkIn");
               Theme.chat_msgGameTextPaint.setColor(Theme.getColor("chat_messageTextIn"));
               Theme.chat_msgGameTextPaint.linkColor = Theme.getColor("chat_messageLinkIn");
               Theme.chat_replyTextPaint.linkColor = Theme.getColor("chat_messageLinkIn");
            }

            int var3;
            if (this.documentAttach != null) {
               var3 = this.documentAttachType;
               if (var3 == 3) {
                  if (this.currentMessageObject.isOutOwner()) {
                     this.seekBarWaveform.setColors(Theme.getColor("chat_outVoiceSeekbar"), Theme.getColor("chat_outVoiceSeekbarFill"), Theme.getColor("chat_outVoiceSeekbarSelected"));
                     this.seekBar.setColors(Theme.getColor("chat_outAudioSeekbar"), Theme.getColor("chat_outAudioCacheSeekbar"), Theme.getColor("chat_outAudioSeekbarFill"), Theme.getColor("chat_outAudioSeekbarFill"), Theme.getColor("chat_outAudioSeekbarSelected"));
                  } else {
                     this.seekBarWaveform.setColors(Theme.getColor("chat_inVoiceSeekbar"), Theme.getColor("chat_inVoiceSeekbarFill"), Theme.getColor("chat_inVoiceSeekbarSelected"));
                     this.seekBar.setColors(Theme.getColor("chat_inAudioSeekbar"), Theme.getColor("chat_inAudioCacheSeekbar"), Theme.getColor("chat_inAudioSeekbarFill"), Theme.getColor("chat_inAudioSeekbarFill"), Theme.getColor("chat_inAudioSeekbarSelected"));
                  }
               } else if (var3 == 5) {
                  this.documentAttachType = 5;
                  if (this.currentMessageObject.isOutOwner()) {
                     this.seekBar.setColors(Theme.getColor("chat_outAudioSeekbar"), Theme.getColor("chat_outAudioCacheSeekbar"), Theme.getColor("chat_outAudioSeekbarFill"), Theme.getColor("chat_outAudioSeekbarFill"), Theme.getColor("chat_outAudioSeekbarSelected"));
                  } else {
                     this.seekBar.setColors(Theme.getColor("chat_inAudioSeekbar"), Theme.getColor("chat_inAudioCacheSeekbar"), Theme.getColor("chat_inAudioSeekbarFill"), Theme.getColor("chat_inAudioSeekbarFill"), Theme.getColor("chat_inAudioSeekbarSelected"));
                  }
               }
            }

            var2 = this.currentMessageObject;
            if (var2.type == 5) {
               Theme.chat_timePaint.setColor(Theme.getColor("chat_mediaTimeText"));
            } else if (this.mediaBackground) {
               if (var2.shouldDrawWithoutBackground()) {
                  Theme.chat_timePaint.setColor(Theme.getColor("chat_serviceText"));
               } else {
                  Theme.chat_timePaint.setColor(Theme.getColor("chat_mediaTimeText"));
               }
            } else {
               TextPaint var4;
               String var21;
               if (var2.isOutOwner()) {
                  var4 = Theme.chat_timePaint;
                  if (this.isDrawSelectionBackground()) {
                     var21 = "chat_outTimeSelectedText";
                  } else {
                     var21 = "chat_outTimeText";
                  }

                  var4.setColor(Theme.getColor(var21));
               } else {
                  var4 = Theme.chat_timePaint;
                  if (this.isDrawSelectionBackground()) {
                     var21 = "chat_inTimeSelectedText";
                  } else {
                     var21 = "chat_inTimeText";
                  }

                  var4.setColor(Theme.getColor(var21));
               }
            }

            boolean var5 = this.currentMessageObject.isOutOwner();
            byte var6 = 0;
            int var7;
            int var8;
            MessageObject.GroupedMessagePosition var10;
            int var11;
            Drawable var22;
            Drawable var23;
            byte var24;
            byte var27;
            Drawable var28;
            if (var5) {
               if (!this.mediaBackground && !this.drawPinnedBottom) {
                  this.currentBackgroundDrawable = Theme.chat_msgOutDrawable;
                  var22 = Theme.chat_msgOutSelectedDrawable;
                  var23 = Theme.chat_msgOutShadowDrawable;
               } else {
                  this.currentBackgroundDrawable = Theme.chat_msgOutMediaDrawable;
                  var22 = Theme.chat_msgOutMediaSelectedDrawable;
                  var23 = Theme.chat_msgOutMediaShadowDrawable;
               }

               var7 = this.layoutWidth;
               var8 = this.backgroundWidth;
               if (!this.mediaBackground) {
                  var3 = 0;
               } else {
                  var3 = AndroidUtilities.dp(9.0F);
               }

               this.backgroundDrawableLeft = var7 - var8 - var3;
               var7 = this.backgroundWidth;
               if (this.mediaBackground) {
                  var3 = 0;
               } else {
                  var3 = AndroidUtilities.dp(3.0F);
               }

               this.backgroundDrawableRight = var7 - var3;
               if (this.currentMessagesGroup != null && !this.currentPosition.edge) {
                  this.backgroundDrawableRight += AndroidUtilities.dp(10.0F);
               }

               int var9 = this.backgroundDrawableLeft;
               if (!this.mediaBackground && this.drawPinnedBottom) {
                  this.backgroundDrawableRight -= AndroidUtilities.dp(6.0F);
               }

               var10 = this.currentPosition;
               int var12;
               if (var10 != null) {
                  if ((var10.flags & 2) == 0) {
                     this.backgroundDrawableRight += AndroidUtilities.dp(8.0F);
                  }

                  var7 = var9;
                  if ((this.currentPosition.flags & 1) == 0) {
                     var7 = var9 - AndroidUtilities.dp(8.0F);
                     this.backgroundDrawableRight += AndroidUtilities.dp(8.0F);
                  }

                  if ((this.currentPosition.flags & 4) == 0) {
                     var8 = 0 - AndroidUtilities.dp(9.0F);
                     var11 = AndroidUtilities.dp(9.0F) + 0;
                  } else {
                     var8 = 0;
                     var11 = 0;
                  }

                  var12 = var8;
                  var9 = var7;
                  var3 = var11;
                  if ((this.currentPosition.flags & 8) == 0) {
                     var3 = var11 + AndroidUtilities.dp(9.0F);
                     var12 = var8;
                     var9 = var7;
                  }
               } else {
                  var12 = 0;
                  var3 = 0;
               }

               if (this.drawPinnedBottom && this.drawPinnedTop) {
                  var7 = 0;
               } else if (this.drawPinnedBottom) {
                  var7 = AndroidUtilities.dp(1.0F);
               } else {
                  var7 = AndroidUtilities.dp(2.0F);
               }

               var5 = this.drawPinnedTop;
               if (var5 || var5 && this.drawPinnedBottom) {
                  var8 = 0;
               } else {
                  var8 = AndroidUtilities.dp(1.0F);
               }

               var8 += var12;
               BaseCell.setDrawableBounds(this.currentBackgroundDrawable, var9, var8, this.backgroundDrawableRight, this.layoutHeight - var7 + var3);
               BaseCell.setDrawableBounds(var22, var9, var8, this.backgroundDrawableRight, this.layoutHeight - var7 + var3);
               BaseCell.setDrawableBounds(var23, var9, var8, this.backgroundDrawableRight, this.layoutHeight - var7 + var3);
               var28 = var22;
               var22 = var23;
            } else {
               if (!this.mediaBackground && !this.drawPinnedBottom) {
                  this.currentBackgroundDrawable = Theme.chat_msgInDrawable;
                  var23 = Theme.chat_msgInSelectedDrawable;
                  var22 = Theme.chat_msgInShadowDrawable;
               } else {
                  this.currentBackgroundDrawable = Theme.chat_msgInMediaDrawable;
                  var23 = Theme.chat_msgInMediaSelectedDrawable;
                  var22 = Theme.chat_msgInMediaShadowDrawable;
               }

               if (this.isChat && this.isAvatarVisible) {
                  var24 = 48;
               } else {
                  var24 = 0;
               }

               if (!this.mediaBackground) {
                  var27 = 3;
               } else {
                  var27 = 9;
               }

               this.backgroundDrawableLeft = AndroidUtilities.dp((float)(var24 + var27));
               var7 = this.backgroundWidth;
               if (this.mediaBackground) {
                  var3 = 0;
               } else {
                  var3 = AndroidUtilities.dp(3.0F);
               }

               this.backgroundDrawableRight = var7 - var3;
               if (this.currentMessagesGroup != null) {
                  if (!this.currentPosition.edge) {
                     this.backgroundDrawableLeft -= AndroidUtilities.dp(10.0F);
                     this.backgroundDrawableRight += AndroidUtilities.dp(10.0F);
                  }

                  var3 = this.currentPosition.leftSpanOffset;
                  if (var3 != 0) {
                     this.backgroundDrawableLeft += (int)Math.ceil((double)((float)var3 / 1000.0F * (float)this.getGroupPhotosWidth()));
                  }
               }

               if (!this.mediaBackground && this.drawPinnedBottom) {
                  this.backgroundDrawableRight -= AndroidUtilities.dp(6.0F);
                  this.backgroundDrawableLeft += AndroidUtilities.dp(6.0F);
               }

               var10 = this.currentPosition;
               if (var10 != null) {
                  if ((var10.flags & 2) == 0) {
                     this.backgroundDrawableRight += AndroidUtilities.dp(8.0F);
                  }

                  if ((this.currentPosition.flags & 1) == 0) {
                     this.backgroundDrawableLeft -= AndroidUtilities.dp(8.0F);
                     this.backgroundDrawableRight += AndroidUtilities.dp(8.0F);
                  }

                  if ((this.currentPosition.flags & 4) == 0) {
                     var7 = 0 - AndroidUtilities.dp(9.0F);
                     var8 = AndroidUtilities.dp(9.0F) + 0;
                  } else {
                     var7 = 0;
                     var8 = 0;
                  }

                  var11 = var7;
                  var3 = var8;
                  if ((this.currentPosition.flags & 8) == 0) {
                     var3 = var8 + AndroidUtilities.dp(10.0F);
                     var11 = var7;
                  }
               } else {
                  var11 = 0;
                  var3 = 0;
               }

               if (this.drawPinnedBottom && this.drawPinnedTop) {
                  var7 = 0;
               } else if (this.drawPinnedBottom) {
                  var7 = AndroidUtilities.dp(1.0F);
               } else {
                  var7 = AndroidUtilities.dp(2.0F);
               }

               var5 = this.drawPinnedTop;
               if (var5 || var5 && this.drawPinnedBottom) {
                  var8 = 0;
               } else {
                  var8 = AndroidUtilities.dp(1.0F);
               }

               var8 += var11;
               BaseCell.setDrawableBounds(this.currentBackgroundDrawable, this.backgroundDrawableLeft, var8, this.backgroundDrawableRight, this.layoutHeight - var7 + var3);
               BaseCell.setDrawableBounds(var23, this.backgroundDrawableLeft, var8, this.backgroundDrawableRight, this.layoutHeight - var7 + var3);
               BaseCell.setDrawableBounds(var22, this.backgroundDrawableLeft, var8, this.backgroundDrawableRight, this.layoutHeight - var7 + var3);
               var28 = var23;
            }

            long var13;
            long var15;
            if (this.checkBoxVisible || this.checkBoxAnimationInProgress) {
               if (this.checkBoxVisible && this.checkBoxAnimationProgress == 1.0F || !this.checkBoxVisible && this.checkBoxAnimationProgress == 0.0F) {
                  this.checkBoxAnimationInProgress = false;
               }

               CubicBezierInterpolator var25;
               if (this.checkBoxVisible) {
                  var25 = CubicBezierInterpolator.EASE_OUT;
               } else {
                  var25 = CubicBezierInterpolator.EASE_IN;
               }

               this.checkBoxTranslation = (int)Math.ceil((double)(var25.getInterpolation(this.checkBoxAnimationProgress) * (float)AndroidUtilities.dp(35.0F)));
               if (!this.currentMessageObject.isOutOwner()) {
                  this.setTranslationX((float)this.checkBoxTranslation);
               }

               var3 = AndroidUtilities.dp(21.0F);
               this.checkBox.setBounds(AndroidUtilities.dp(-27.0F) + this.checkBoxTranslation, this.currentBackgroundDrawable.getBounds().bottom - AndroidUtilities.dp(8.0F) - var3, var3, var3);
               if (this.checkBoxAnimationInProgress) {
                  var13 = SystemClock.uptimeMillis();
                  var15 = var13 - this.lastCheckBoxAnimationTime;
                  this.lastCheckBoxAnimationTime = var13;
                  if (this.checkBoxVisible) {
                     this.checkBoxAnimationProgress += (float)var15 / 200.0F;
                     if (this.checkBoxAnimationProgress > 1.0F) {
                        this.checkBoxAnimationProgress = 1.0F;
                     }

                     this.invalidate();
                     ((View)this.getParent()).invalidate();
                  } else {
                     this.checkBoxAnimationProgress -= (float)var15 / 200.0F;
                     if (this.checkBoxAnimationProgress <= 0.0F) {
                        this.checkBoxAnimationProgress = 0.0F;
                     }

                     this.invalidate();
                     ((View)this.getParent()).invalidate();
                  }
               }
            }

            float var17;
            if (this.drawBackground) {
               var23 = this.currentBackgroundDrawable;
               if (var23 != null) {
                  if (this.isHighlightedAnimated) {
                     var23.draw(var1);
                     var3 = this.highlightProgress;
                     if (var3 >= 300) {
                        var17 = 1.0F;
                     } else {
                        var17 = (float)var3 / 300.0F;
                     }

                     if (this.currentPosition == null) {
                        var28.setAlpha((int)(var17 * 255.0F));
                        var28.draw(var1);
                     }
                  } else if (!this.isDrawSelectionBackground() || this.currentPosition != null && this.getBackground() == null) {
                     this.currentBackgroundDrawable.draw(var1);
                  } else {
                     var28.setAlpha(255);
                     var28.draw(var1);
                  }

                  MessageObject.GroupedMessagePosition var26 = this.currentPosition;
                  if (var26 == null || var26.flags != 0) {
                     var22.draw(var1);
                  }
               }
            }

            long var18;
            if (this.isHighlightedAnimated) {
               var18 = System.currentTimeMillis();
               var13 = Math.abs(var18 - this.lastHighlightProgressTime);
               var15 = var13;
               if (var13 > 17L) {
                  var15 = 17L;
               }

               this.highlightProgress = (int)((long)this.highlightProgress - var15);
               this.lastHighlightProgressTime = var18;
               if (this.highlightProgress <= 0) {
                  this.highlightProgress = 0;
                  this.isHighlightedAnimated = false;
               }

               this.invalidate();
               if (this.getParent() != null) {
                  ((View)this.getParent()).invalidate();
               }
            }

            this.drawContent(var1);
            var5 = this.drawShareButton;
            var17 = 12.0F;
            if (var5) {
               if (this.sharePressed) {
                  if (Theme.isCustomTheme() && !Theme.hasThemeKey("chat_shareBackgroundSelected")) {
                     Theme.chat_shareDrawable.setColorFilter(Theme.colorPressedFilter2);
                  } else {
                     Theme.chat_shareDrawable.setColorFilter(Theme.getShareColorFilter(Theme.getColor("chat_shareBackgroundSelected"), true));
                  }
               } else if (Theme.isCustomTheme() && !Theme.hasThemeKey("chat_shareBackground")) {
                  Theme.chat_shareDrawable.setColorFilter(Theme.colorFilter2);
               } else {
                  Theme.chat_shareDrawable.setColorFilter(Theme.getShareColorFilter(Theme.getColor("chat_shareBackground"), false));
               }

               if (this.currentMessageObject.isOutOwner()) {
                  this.shareStartX = this.currentBackgroundDrawable.getBounds().left - AndroidUtilities.dp(8.0F) - Theme.chat_shareDrawable.getIntrinsicWidth();
               } else {
                  this.shareStartX = this.currentBackgroundDrawable.getBounds().right + AndroidUtilities.dp(8.0F);
               }

               var23 = Theme.chat_shareDrawable;
               var3 = this.shareStartX;
               var7 = this.layoutHeight - AndroidUtilities.dp(41.0F);
               this.shareStartY = var7;
               BaseCell.setDrawableBounds(var23, var3, var7);
               Theme.chat_shareDrawable.draw(var1);
               if (this.drwaShareGoIcon) {
                  BaseCell.setDrawableBounds(Theme.chat_goIconDrawable, this.shareStartX + AndroidUtilities.dp(12.0F), this.shareStartY + AndroidUtilities.dp(9.0F));
                  Theme.chat_goIconDrawable.draw(var1);
               } else {
                  BaseCell.setDrawableBounds(Theme.chat_shareIconDrawable, this.shareStartX + AndroidUtilities.dp(8.0F), this.shareStartY + AndroidUtilities.dp(9.0F));
                  Theme.chat_shareIconDrawable.draw(var1);
               }
            }

            if (this.replyNameLayout != null) {
               if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                  if (this.currentMessageObject.isOutOwner()) {
                     this.replyStartX = AndroidUtilities.dp(23.0F);
                  } else if (this.currentMessageObject.type == 5) {
                     this.replyStartX = this.backgroundDrawableLeft + this.backgroundDrawableRight + AndroidUtilities.dp(4.0F);
                  } else {
                     this.replyStartX = this.backgroundDrawableLeft + this.backgroundDrawableRight + AndroidUtilities.dp(17.0F);
                  }

                  this.replyStartY = AndroidUtilities.dp(12.0F);
               } else {
                  if (this.currentMessageObject.isOutOwner()) {
                     this.replyStartX = this.backgroundDrawableLeft + AndroidUtilities.dp(12.0F);
                  } else {
                     var5 = this.mediaBackground;
                     if (var5) {
                        this.replyStartX = this.backgroundDrawableLeft + AndroidUtilities.dp(12.0F);
                     } else {
                        var3 = this.backgroundDrawableLeft;
                        if (var5 || !this.drawPinnedBottom) {
                           var17 = 18.0F;
                        }

                        this.replyStartX = var3 + AndroidUtilities.dp(var17);
                     }
                  }

                  if (this.drawForwardedName && this.forwardedNameLayout[0] != null) {
                     var24 = 36;
                  } else {
                     var24 = 0;
                  }

                  var27 = var6;
                  if (this.drawNameLayout) {
                     var27 = var6;
                     if (this.nameLayout != null) {
                        var27 = 20;
                     }
                  }

                  this.replyStartY = AndroidUtilities.dp((float)(12 + var24 + var27));
               }
            }

            if (this.currentPosition == null) {
               this.drawNamesLayout(var1);
            }

            if (!this.autoPlayingMedia || !MediaController.getInstance().isPlayingMessageAndReadyToDraw(this.currentMessageObject)) {
               this.drawOverlays(var1);
            }

            if ((this.drawTime || !this.mediaBackground) && !this.forceNotDrawTime) {
               this.drawTime(var1);
            }

            if ((this.controlsAlpha != 1.0F || this.timeAlpha != 1.0F) && this.currentMessageObject.type != 5) {
               var18 = System.currentTimeMillis();
               var13 = Math.abs(this.lastControlsAlphaChangeTime - var18);
               var15 = var13;
               if (var13 > 17L) {
                  var15 = 17L;
               }

               this.totalChangeTime += var15;
               if (this.totalChangeTime > 100L) {
                  this.totalChangeTime = 100L;
               }

               this.lastControlsAlphaChangeTime = var18;
               if (this.controlsAlpha != 1.0F) {
                  this.controlsAlpha = AndroidUtilities.decelerateInterpolator.getInterpolation((float)this.totalChangeTime / 100.0F);
               }

               if (this.timeAlpha != 1.0F) {
                  this.timeAlpha = AndroidUtilities.decelerateInterpolator.getInterpolation((float)this.totalChangeTime / 100.0F);
               }

               this.invalidate();
               if (this.forceNotDrawTime) {
                  MessageObject.GroupedMessagePosition var20 = this.currentPosition;
                  if (var20 != null && var20.last && this.getParent() != null) {
                     ((View)this.getParent()).invalidate();
                  }
               }
            }

         }
      }
   }

   public void onFailedDownload(String var1, boolean var2) {
      int var3 = this.documentAttachType;
      if (var3 != 3 && var3 != 5) {
         var2 = false;
      } else {
         var2 = true;
      }

      this.updateButtonState(true, var2, false);
   }

   public boolean onHoverEvent(MotionEvent var1) {
      int var2 = (int)var1.getX();
      int var3 = (int)var1.getY();
      int var4 = var1.getAction();
      byte var5 = 0;
      int var6 = var5;
      if (var4 != 9) {
         if (var1.getAction() != 7) {
            if (var1.getAction() == 10) {
               this.currentFocusedVirtualView = 0;
            }

            return super.onHoverEvent(var1);
         }

         var6 = var5;
      }

      while(var6 < this.accessibilityVirtualViewBounds.size()) {
         if (((Rect)this.accessibilityVirtualViewBounds.valueAt(var6)).contains(var2, var3)) {
            var6 = this.accessibilityVirtualViewBounds.keyAt(var6);
            if (var6 != this.currentFocusedVirtualView) {
               this.currentFocusedVirtualView = var6;
               this.sendAccessibilityEventForVirtualView(var6, 32768);
            }

            return true;
         }

         ++var6;
      }

      return super.onHoverEvent(var1);
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
   }

   @SuppressLint({"DrawAllocation"})
   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (this.currentMessageObject != null) {
         byte var11 = 0;
         byte var10 = 0;
         MessageObject.GroupedMessagePosition var13;
         if (var1 || !this.wasLayout) {
            this.layoutWidth = this.getMeasuredWidth();
            this.layoutHeight = this.getMeasuredHeight() - this.substractBackgroundHeight;
            if (this.timeTextWidth < 0) {
               this.timeTextWidth = AndroidUtilities.dp(10.0F);
            }

            String var6 = this.currentTimeString;
            TextPaint var7 = Theme.chat_timePaint;
            var2 = this.timeTextWidth;
            this.timeLayout = new StaticLayout(var6, var7, AndroidUtilities.dp(100.0F) + var2, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            int var8;
            int var9;
            if (!this.mediaBackground) {
               if (!this.currentMessageObject.isOutOwner()) {
                  var5 = this.backgroundWidth;
                  var8 = AndroidUtilities.dp(9.0F);
                  var9 = this.timeWidth;
                  if (this.isAvatarVisible) {
                     var2 = AndroidUtilities.dp(48.0F);
                  } else {
                     var2 = 0;
                  }

                  this.timeX = var5 - var8 - var9 + var2;
               } else {
                  this.timeX = this.layoutWidth - this.timeWidth - AndroidUtilities.dp(38.5F);
               }
            } else if (!this.currentMessageObject.isOutOwner()) {
               var9 = this.backgroundWidth;
               var5 = AndroidUtilities.dp(4.0F);
               var8 = this.timeWidth;
               if (this.isAvatarVisible) {
                  var2 = AndroidUtilities.dp(48.0F);
               } else {
                  var2 = 0;
               }

               this.timeX = var9 - var5 - var8 + var2;
               var13 = this.currentPosition;
               if (var13 != null) {
                  var2 = var13.leftSpanOffset;
                  if (var2 != 0) {
                     this.timeX += (int)Math.ceil((double)((float)var2 / 1000.0F * (float)this.getGroupPhotosWidth()));
                  }
               }
            } else {
               this.timeX = this.layoutWidth - this.timeWidth - AndroidUtilities.dp(42.0F);
            }

            if ((this.currentMessageObject.messageOwner.flags & 1024) != 0) {
               this.viewsLayout = new StaticLayout(this.currentViewsString, Theme.chat_timePaint, this.viewsTextWidth, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            } else {
               this.viewsLayout = null;
            }

            if (this.isAvatarVisible) {
               this.avatarImage.setImageCoords(AndroidUtilities.dp(6.0F), this.avatarImage.getImageY(), AndroidUtilities.dp(42.0F), AndroidUtilities.dp(42.0F));
            }

            this.wasLayout = true;
         }

         if (this.currentMessageObject.type == 0) {
            this.textY = AndroidUtilities.dp(10.0F) + this.namesOffset;
         }

         if (this.currentMessageObject.isRoundVideo()) {
            this.updatePlayingMessageProgress();
         }

         var2 = this.documentAttachType;
         byte var12;
         SeekBar var15;
         RadialProgress2 var16;
         if (var2 == 3) {
            if (this.currentMessageObject.isOutOwner()) {
               this.seekBarX = this.layoutWidth - this.backgroundWidth + AndroidUtilities.dp(57.0F);
               this.buttonX = this.layoutWidth - this.backgroundWidth + AndroidUtilities.dp(14.0F);
               this.timeAudioX = this.layoutWidth - this.backgroundWidth + AndroidUtilities.dp(67.0F);
            } else if (this.isChat && this.currentMessageObject.needDrawAvatar()) {
               this.seekBarX = AndroidUtilities.dp(114.0F);
               this.buttonX = AndroidUtilities.dp(71.0F);
               this.timeAudioX = AndroidUtilities.dp(124.0F);
            } else {
               this.seekBarX = AndroidUtilities.dp(66.0F);
               this.buttonX = AndroidUtilities.dp(23.0F);
               this.timeAudioX = AndroidUtilities.dp(76.0F);
            }

            if (this.hasLinkPreview) {
               this.seekBarX += AndroidUtilities.dp(10.0F);
               this.buttonX += AndroidUtilities.dp(10.0F);
               this.timeAudioX += AndroidUtilities.dp(10.0F);
            }

            SeekBarWaveform var14 = this.seekBarWaveform;
            var4 = this.backgroundWidth;
            if (this.hasLinkPreview) {
               var12 = 10;
            } else {
               var12 = 0;
            }

            var14.setSize(var4 - AndroidUtilities.dp((float)(var12 + 92)), AndroidUtilities.dp(30.0F));
            var15 = this.seekBar;
            var4 = this.backgroundWidth;
            var12 = var10;
            if (this.hasLinkPreview) {
               var12 = 10;
            }

            var15.setSize(var4 - AndroidUtilities.dp((float)(var12 + 72)), AndroidUtilities.dp(30.0F));
            this.seekBarY = AndroidUtilities.dp(13.0F) + this.namesOffset + this.mediaOffsetY;
            this.buttonY = AndroidUtilities.dp(13.0F) + this.namesOffset + this.mediaOffsetY;
            var16 = this.radialProgress;
            var2 = this.buttonX;
            var16.setProgressRect(var2, this.buttonY, AndroidUtilities.dp(44.0F) + var2, this.buttonY + AndroidUtilities.dp(44.0F));
            this.updatePlayingMessageProgress();
         } else if (var2 == 5) {
            if (this.currentMessageObject.isOutOwner()) {
               this.seekBarX = this.layoutWidth - this.backgroundWidth + AndroidUtilities.dp(56.0F);
               this.buttonX = this.layoutWidth - this.backgroundWidth + AndroidUtilities.dp(14.0F);
               this.timeAudioX = this.layoutWidth - this.backgroundWidth + AndroidUtilities.dp(67.0F);
            } else if (this.isChat && this.currentMessageObject.needDrawAvatar()) {
               this.seekBarX = AndroidUtilities.dp(113.0F);
               this.buttonX = AndroidUtilities.dp(71.0F);
               this.timeAudioX = AndroidUtilities.dp(124.0F);
            } else {
               this.seekBarX = AndroidUtilities.dp(65.0F);
               this.buttonX = AndroidUtilities.dp(23.0F);
               this.timeAudioX = AndroidUtilities.dp(76.0F);
            }

            if (this.hasLinkPreview) {
               this.seekBarX += AndroidUtilities.dp(10.0F);
               this.buttonX += AndroidUtilities.dp(10.0F);
               this.timeAudioX += AndroidUtilities.dp(10.0F);
            }

            var15 = this.seekBar;
            var3 = this.backgroundWidth;
            var12 = var11;
            if (this.hasLinkPreview) {
               var12 = 10;
            }

            var15.setSize(var3 - AndroidUtilities.dp((float)(var12 + 65)), AndroidUtilities.dp(30.0F));
            this.seekBarY = AndroidUtilities.dp(29.0F) + this.namesOffset + this.mediaOffsetY;
            this.buttonY = AndroidUtilities.dp(13.0F) + this.namesOffset + this.mediaOffsetY;
            var16 = this.radialProgress;
            var2 = this.buttonX;
            var16.setProgressRect(var2, this.buttonY, AndroidUtilities.dp(44.0F) + var2, this.buttonY + AndroidUtilities.dp(44.0F));
            this.updatePlayingMessageProgress();
         } else if (var2 == 1 && !this.drawPhotoImage) {
            if (this.currentMessageObject.isOutOwner()) {
               this.buttonX = this.layoutWidth - this.backgroundWidth + AndroidUtilities.dp(14.0F);
            } else if (this.isChat && this.currentMessageObject.needDrawAvatar()) {
               this.buttonX = AndroidUtilities.dp(71.0F);
            } else {
               this.buttonX = AndroidUtilities.dp(23.0F);
            }

            if (this.hasLinkPreview) {
               this.buttonX += AndroidUtilities.dp(10.0F);
            }

            this.buttonY = AndroidUtilities.dp(13.0F) + this.namesOffset + this.mediaOffsetY;
            var16 = this.radialProgress;
            var2 = this.buttonX;
            var16.setProgressRect(var2, this.buttonY, AndroidUtilities.dp(44.0F) + var2, this.buttonY + AndroidUtilities.dp(44.0F));
            this.photoImage.setImageCoords(this.buttonX - AndroidUtilities.dp(10.0F), this.buttonY - AndroidUtilities.dp(10.0F), this.photoImage.getImageWidth(), this.photoImage.getImageHeight());
         } else {
            MessageObject var17 = this.currentMessageObject;
            var2 = var17.type;
            if (var2 == 12) {
               if (var17.isOutOwner()) {
                  var2 = this.layoutWidth - this.backgroundWidth + AndroidUtilities.dp(14.0F);
               } else if (this.isChat && this.currentMessageObject.needDrawAvatar()) {
                  var2 = AndroidUtilities.dp(72.0F);
               } else {
                  var2 = AndroidUtilities.dp(23.0F);
               }

               this.photoImage.setImageCoords(var2, AndroidUtilities.dp(13.0F) + this.namesOffset, AndroidUtilities.dp(44.0F), AndroidUtilities.dp(44.0F));
            } else {
               label215: {
                  label270: {
                     if (var2 != 0 || !this.hasLinkPreview && !this.hasGamePreview && !this.hasInvoicePreview) {
                        if (!this.currentMessageObject.isOutOwner()) {
                           if (this.isChat && this.isAvatarVisible) {
                              var3 = AndroidUtilities.dp(63.0F);
                           } else {
                              var3 = AndroidUtilities.dp(15.0F);
                           }

                           var13 = this.currentPosition;
                           var2 = var3;
                           if (var13 == null) {
                              break label215;
                           }

                           var2 = var3;
                           if (var13.edge) {
                              break label215;
                           }

                           var2 = AndroidUtilities.dp(10.0F);
                           break label270;
                        }

                        if (this.mediaBackground) {
                           var3 = this.layoutWidth - this.backgroundWidth;
                           var2 = AndroidUtilities.dp(3.0F);
                           break label270;
                        }

                        var3 = this.layoutWidth - this.backgroundWidth;
                        var2 = AndroidUtilities.dp(6.0F);
                     } else {
                        if (this.hasGamePreview) {
                           var2 = this.unmovedTextX - AndroidUtilities.dp(10.0F);
                        } else {
                           if (this.hasInvoicePreview) {
                              var3 = this.unmovedTextX;
                              var2 = AndroidUtilities.dp(1.0F);
                           } else {
                              var3 = this.unmovedTextX;
                              var2 = AndroidUtilities.dp(1.0F);
                           }

                           var2 += var3;
                        }

                        if (this.isSmallImage) {
                           var3 = var2 + this.backgroundWidth;
                           var2 = AndroidUtilities.dp(81.0F);
                           break label270;
                        }

                        if (this.hasInvoicePreview) {
                           var4 = -AndroidUtilities.dp(6.3F);
                           var3 = var2;
                           var2 = var4;
                        } else {
                           var4 = AndroidUtilities.dp(10.0F);
                           var3 = var2;
                           var2 = var4;
                        }
                     }

                     var2 += var3;
                     break label215;
                  }

                  var2 = var3 - var2;
               }

               var13 = this.currentPosition;
               var3 = var2;
               if (var13 != null) {
                  var4 = var2;
                  if ((1 & var13.flags) == 0) {
                     var4 = var2 - AndroidUtilities.dp(2.0F);
                  }

                  var2 = this.currentPosition.leftSpanOffset;
                  var3 = var4;
                  if (var2 != 0) {
                     var3 = var4 + (int)Math.ceil((double)((float)var2 / 1000.0F * (float)this.getGroupPhotosWidth()));
                  }
               }

               var2 = var3;
               if (this.currentMessageObject.type != 0) {
                  var2 = var3 - AndroidUtilities.dp(2.0F);
               }

               ImageReceiver var18 = this.photoImage;
               var18.setImageCoords(var2, var18.getImageY(), this.photoImage.getImageWidth(), this.photoImage.getImageHeight());
               this.buttonX = (int)((float)var2 + (float)(this.photoImage.getImageWidth() - AndroidUtilities.dp(48.0F)) / 2.0F);
               this.buttonY = this.photoImage.getImageY() + (this.photoImage.getImageHeight() - AndroidUtilities.dp(48.0F)) / 2;
               var16 = this.radialProgress;
               var2 = this.buttonX;
               var16.setProgressRect(var2, this.buttonY, AndroidUtilities.dp(48.0F) + var2, this.buttonY + AndroidUtilities.dp(48.0F));
               this.deleteProgressRect.set((float)(this.buttonX + AndroidUtilities.dp(5.0F)), (float)(this.buttonY + AndroidUtilities.dp(5.0F)), (float)(this.buttonX + AndroidUtilities.dp(43.0F)), (float)(this.buttonY + AndroidUtilities.dp(43.0F)));
               var2 = this.documentAttachType;
               if (var2 == 4 || var2 == 2) {
                  this.videoButtonX = this.photoImage.getImageX() + AndroidUtilities.dp(8.0F);
                  this.videoButtonY = this.photoImage.getImageY() + AndroidUtilities.dp(8.0F);
                  var16 = this.videoRadialProgress;
                  var2 = this.videoButtonX;
                  var16.setProgressRect(var2, this.videoButtonY, AndroidUtilities.dp(24.0F) + var2, this.videoButtonY + AndroidUtilities.dp(24.0F));
               }
            }
         }

      }
   }

   protected void onLongPress() {
      CharacterStyle var1 = this.pressedLink;
      if (var1 instanceof URLSpanMono) {
         this.delegate.didPressUrl(this.currentMessageObject, var1, true);
      } else {
         if (var1 instanceof URLSpanNoUnderline) {
            if (((URLSpanNoUnderline)var1).getURL().startsWith("/")) {
               this.delegate.didPressUrl(this.currentMessageObject, this.pressedLink, true);
               return;
            }
         } else if (var1 instanceof URLSpan) {
            this.delegate.didPressUrl(this.currentMessageObject, var1, true);
            return;
         }

         this.resetPressedLink(-1);
         if (this.buttonPressed != 0 || this.miniButtonPressed != 0 || this.videoButtonPressed != 0 || this.pressedBotButton != -1) {
            this.buttonPressed = 0;
            this.miniButtonPressed = 0;
            this.videoButtonPressed = 0;
            this.pressedBotButton = -1;
            this.invalidate();
         }

         this.linkPreviewPressed = false;
         this.otherPressed = false;
         this.sharePressed = false;
         this.imagePressed = false;
         this.gamePreviewPressed = false;
         Drawable var2;
         if (this.instantPressed) {
            this.instantButtonPressed = false;
            this.instantPressed = false;
            if (VERSION.SDK_INT >= 21) {
               var2 = this.selectorDrawable;
               if (var2 != null) {
                  var2.setState(StateSet.NOTHING);
               }
            }

            this.invalidate();
         }

         if (this.pressedVoteButton != -1) {
            this.pressedVoteButton = -1;
            if (VERSION.SDK_INT >= 21) {
               var2 = this.selectorDrawable;
               if (var2 != null) {
                  var2.setState(StateSet.NOTHING);
               }
            }

            this.invalidate();
         }

         ChatMessageCell.ChatMessageCellDelegate var3 = this.delegate;
         if (var3 != null) {
            var3.didLongPress(this, this.lastTouchX, this.lastTouchY);
         }

      }
   }

   protected void onMeasure(int var1, int var2) {
      MessageObject var3 = this.currentMessageObject;
      if (var3 != null && (var3.checkLayout() || this.lastHeight != AndroidUtilities.displaySize.y)) {
         this.inLayout = true;
         var3 = this.currentMessageObject;
         this.currentMessageObject = null;
         this.setMessageObject(var3, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
         this.inLayout = false;
      }

      this.setMeasuredDimension(MeasureSpec.getSize(var1), this.totalHeight + this.keyboardHeight);
   }

   public void onProgressDownload(String var1, float var2) {
      if (this.drawVideoImageButton) {
         this.videoRadialProgress.setProgress(var2, true);
      } else {
         this.radialProgress.setProgress(var2, true);
      }

      int var3 = this.documentAttachType;
      if (var3 != 3 && var3 != 5) {
         if (this.hasMiniProgress != 0) {
            if (this.miniButtonState != 1) {
               this.updateButtonState(false, false, false);
            }
         } else if (this.buttonState != 1) {
            this.updateButtonState(false, false, false);
         }
      } else if (this.hasMiniProgress != 0) {
         if (this.miniButtonState != 1) {
            this.updateButtonState(false, false, false);
         }
      } else if (this.buttonState != 4) {
         this.updateButtonState(false, false, false);
      }

   }

   public void onProgressUpload(String var1, float var2, boolean var3) {
      this.radialProgress.setProgress(var2, true);
      if (var2 == 1.0F && this.currentPosition != null && SendMessagesHelper.getInstance(this.currentAccount).isSendingMessage(this.currentMessageObject.getId()) && this.buttonState == 1) {
         this.drawRadialCheckBackground = true;
         this.getIconForCurrentState();
         this.radialProgress.setIcon(6, false, true);
      }

   }

   public void onProvideStructure(ViewStructure var1) {
      super.onProvideStructure(var1);
      if (this.allowAssistant && VERSION.SDK_INT >= 23) {
         CharSequence var2 = this.currentMessageObject.messageText;
         if (var2 != null && var2.length() > 0) {
            var1.setText(this.currentMessageObject.messageText);
         } else {
            var2 = this.currentMessageObject.caption;
            if (var2 != null && var2.length() > 0) {
               var1.setText(this.currentMessageObject.caption);
            }
         }
      }

   }

   public void onSeekBarDrag(float var1) {
      MessageObject var2 = this.currentMessageObject;
      if (var2 != null) {
         var2.audioProgress = var1;
         MediaController.getInstance().seekToProgress(this.currentMessageObject, var1);
      }
   }

   public void onSuccessDownload(String var1) {
      int var2 = this.documentAttachType;
      if (var2 != 3 && var2 != 5) {
         if (this.drawVideoImageButton) {
            this.videoRadialProgress.setProgress(1.0F, true);
         } else {
            this.radialProgress.setProgress(1.0F, true);
         }

         if (!this.currentMessageObject.needDrawBluredPreview() && !this.autoPlayingMedia) {
            TLRPC.Document var6 = this.documentAttach;
            if (var6 != null) {
               var2 = this.documentAttachType;
               ImageLocation var5;
               TLRPC.PhotoSize var8;
               ImageReceiver var9;
               ImageLocation var10;
               if (var2 == 7) {
                  var9 = this.photoImage;
                  var10 = ImageLocation.getForDocument(var6);
                  var5 = ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject);
                  var8 = this.currentPhotoObject;
                  if (!(var8 instanceof TLRPC.TL_photoStrippedSize) && (var8 == null || !"s".equals(var8.type))) {
                     var1 = this.currentPhotoFilter;
                  } else {
                     var1 = this.currentPhotoFilterThumb;
                  }

                  var9.setImage(var10, "g", var5, var1, ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject), this.currentPhotoFilterThumb, (Drawable)null, this.documentAttach.size, (String)null, this.currentMessageObject, 0);
                  this.photoImage.setAllowStartAnimation(true);
                  this.photoImage.startAnimation();
                  this.autoPlayingMedia = true;
               } else {
                  label147: {
                     if (SharedConfig.autoplayVideo && var2 == 4) {
                        label152: {
                           MessageObject.GroupedMessagePosition var7 = this.currentPosition;
                           if (var7 != null) {
                              var2 = var7.flags;
                              if ((var2 & 1) == 0 || (var2 & 2) == 0) {
                                 break label152;
                              }
                           }

                           this.animatingNoSound = 2;
                           ImageReceiver var4 = this.photoImage;
                           ImageLocation var3 = ImageLocation.getForDocument(this.documentAttach);
                           var5 = ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject);
                           var8 = this.currentPhotoObject;
                           if (var8 instanceof TLRPC.TL_photoStrippedSize || var8 != null && "s".equals(var8.type)) {
                              var1 = this.currentPhotoFilterThumb;
                           } else {
                              var1 = this.currentPhotoFilter;
                           }

                           var4.setImage(var3, "g", var5, var1, ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject), this.currentPhotoFilterThumb, (Drawable)null, this.documentAttach.size, (String)null, this.currentMessageObject, 0);
                           if (!PhotoViewer.isPlayingMessage(this.currentMessageObject)) {
                              this.photoImage.setAllowStartAnimation(true);
                              this.photoImage.startAnimation();
                           } else {
                              this.photoImage.setAllowStartAnimation(false);
                           }

                           this.autoPlayingMedia = true;
                           break label147;
                        }
                     }

                     if (this.documentAttachType == 2) {
                        var9 = this.photoImage;
                        var10 = ImageLocation.getForDocument(this.documentAttach);
                        var5 = ImageLocation.getForObject(this.currentPhotoObject, this.photoParentObject);
                        var8 = this.currentPhotoObject;
                        if (var8 instanceof TLRPC.TL_photoStrippedSize || var8 != null && "s".equals(var8.type)) {
                           var1 = this.currentPhotoFilterThumb;
                        } else {
                           var1 = this.currentPhotoFilter;
                        }

                        var9.setImage(var10, "g", var5, var1, ImageLocation.getForObject(this.currentPhotoObjectThumb, this.photoParentObject), this.currentPhotoFilterThumb, (Drawable)null, this.documentAttach.size, (String)null, this.currentMessageObject, 0);
                        if (SharedConfig.autoplayGifs) {
                           this.photoImage.setAllowStartAnimation(true);
                           this.photoImage.startAnimation();
                        } else {
                           this.photoImage.setAllowStartAnimation(false);
                           this.photoImage.stopAnimation();
                        }

                        this.autoPlayingMedia = true;
                     }
                  }
               }
            }
         }

         MessageObject var11 = this.currentMessageObject;
         if (var11.type == 0) {
            if (!this.autoPlayingMedia && this.documentAttachType == 2 && var11.gifState != 1.0F) {
               this.buttonState = 2;
               this.didPressButton(true, false);
            } else if (!this.photoNotSet) {
               this.updateButtonState(false, true, false);
            } else {
               this.setMessageObject(this.currentMessageObject, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
            }
         } else {
            if (!this.photoNotSet) {
               this.updateButtonState(false, true, false);
            }

            if (this.photoNotSet) {
               this.setMessageObject(this.currentMessageObject, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
            }
         }
      } else {
         this.updateButtonState(false, true, false);
         this.updateWaveform();
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (this.currentMessageObject != null && this.delegate.canPerformActions()) {
         this.disallowLongPress = false;
         this.lastTouchX = var1.getX();
         this.lastTouchX = var1.getY();
         boolean var2 = this.checkTextBlockMotionEvent(var1);
         boolean var3 = var2;
         if (!var2) {
            var3 = this.checkOtherButtonMotionEvent(var1);
         }

         boolean var4 = var3;
         if (!var3) {
            var4 = this.checkCaptionMotionEvent(var1);
         }

         var2 = var4;
         if (!var4) {
            var2 = this.checkAudioMotionEvent(var1);
         }

         var3 = var2;
         if (!var2) {
            var3 = this.checkLinkPreviewMotionEvent(var1);
         }

         var2 = var3;
         if (!var3) {
            var2 = this.checkInstantButtonMotionEvent(var1);
         }

         var3 = var2;
         if (!var2) {
            var3 = this.checkGameMotionEvent(var1);
         }

         var2 = var3;
         if (!var3) {
            var2 = this.checkPhotoImageMotionEvent(var1);
         }

         var3 = var2;
         if (!var2) {
            var3 = this.checkBotButtonMotionEvent(var1);
         }

         var2 = var3;
         if (!var3) {
            var2 = this.checkPollButtonMotionEvent(var1);
         }

         if (var1.getAction() == 3) {
            this.buttonPressed = 0;
            this.miniButtonPressed = 0;
            this.pressedBotButton = -1;
            this.pressedVoteButton = -1;
            this.linkPreviewPressed = false;
            this.otherPressed = false;
            this.sharePressed = false;
            this.imagePressed = false;
            this.gamePreviewPressed = false;
            this.instantButtonPressed = false;
            this.instantPressed = false;
            if (VERSION.SDK_INT >= 21) {
               Drawable var5 = this.selectorDrawable;
               if (var5 != null) {
                  var5.setState(StateSet.NOTHING);
               }
            }

            this.resetPressedLink(-1);
            var3 = false;
         } else {
            var3 = var2;
         }

         this.updateRadialProgressBackground();
         if (!this.disallowLongPress && var3 && var1.getAction() == 0) {
            this.startCheckLongPress();
         }

         if (var1.getAction() != 0 && var1.getAction() != 2) {
            this.cancelCheckLongPress();
         }

         var4 = var3;
         if (!var3) {
            float var6 = var1.getX();
            float var7 = var1.getY();
            int var8;
            float var9;
            int var10;
            ChatMessageCell.ChatMessageCellDelegate var11;
            if (var1.getAction() == 0) {
               var11 = this.delegate;
               if (var11 != null) {
                  var4 = var3;
                  if (!var11.canPerformActions()) {
                     return var4;
                  }
               }

               label344: {
                  if (this.isAvatarVisible && this.avatarImage.isInsideImage(var6, (float)this.getTop() + var7)) {
                     this.avatarPressed = true;
                  } else {
                     label343: {
                        if (this.drawForwardedName && this.forwardedNameLayout[0] != null) {
                           var8 = this.forwardNameX;
                           if (var6 >= (float)var8 && var6 <= (float)(var8 + this.forwardedNameWidth)) {
                              var8 = this.forwardNameY;
                              if (var7 >= (float)var8 && var7 <= (float)(var8 + AndroidUtilities.dp(32.0F))) {
                                 if (this.viaWidth != 0 && var6 >= (float)(this.forwardNameX + this.viaNameWidth + AndroidUtilities.dp(4.0F))) {
                                    this.forwardBotPressed = true;
                                 } else {
                                    this.forwardNamePressed = true;
                                 }
                                 break label343;
                              }
                           }
                        }

                        if (this.drawNameLayout && this.nameLayout != null) {
                           var8 = this.viaWidth;
                           if (var8 != 0) {
                              var9 = this.nameX;
                              var10 = this.viaNameWidth;
                              if (var6 >= (float)var10 + var9 && var6 <= var9 + (float)var10 + (float)var8 && var7 >= this.nameY - (float)AndroidUtilities.dp(4.0F) && var7 <= this.nameY + (float)AndroidUtilities.dp(20.0F)) {
                                 this.forwardBotPressed = true;
                                 break label343;
                              }
                           }
                        }

                        if (this.drawShareButton) {
                           var8 = this.shareStartX;
                           if (var6 >= (float)var8 && var6 <= (float)(var8 + AndroidUtilities.dp(40.0F))) {
                              var8 = this.shareStartY;
                              if (var7 >= (float)var8 && var7 <= (float)(var8 + AndroidUtilities.dp(32.0F))) {
                                 this.sharePressed = true;
                                 this.invalidate();
                                 break label343;
                              }
                           }
                        }

                        var2 = var3;
                        if (this.replyNameLayout == null) {
                           break label344;
                        }

                        if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                           var8 = this.replyStartX;
                           var10 = Math.max(this.replyNameWidth, this.replyTextWidth);
                        } else {
                           var8 = this.replyStartX;
                           var10 = this.backgroundDrawableRight;
                        }

                        var2 = var3;
                        if (var6 < (float)this.replyStartX) {
                           break label344;
                        }

                        var2 = var3;
                        if (var6 > (float)(var8 + var10)) {
                           break label344;
                        }

                        var8 = this.replyStartY;
                        var2 = var3;
                        if (var7 < (float)var8) {
                           break label344;
                        }

                        var2 = var3;
                        if (var7 > (float)(var8 + AndroidUtilities.dp(35.0F))) {
                           break label344;
                        }

                        this.replyPressed = true;
                     }
                  }

                  var2 = true;
               }

               var4 = var2;
               if (var2) {
                  this.startCheckLongPress();
                  var4 = var2;
               }
            } else {
               if (var1.getAction() != 2) {
                  this.cancelCheckLongPress();
               }

               TLRPC.User var14;
               TLRPC.Chat var15;
               if (this.avatarPressed) {
                  if (var1.getAction() == 1) {
                     this.avatarPressed = false;
                     this.playSoundEffect(0);
                     var11 = this.delegate;
                     var4 = var3;
                     if (var11 != null) {
                        var14 = this.currentUser;
                        if (var14 != null) {
                           if (var14.id == 0) {
                              var11.didPressHiddenForward(this);
                              var4 = var3;
                           } else {
                              var11.didPressUserAvatar(this, var14, this.lastTouchX, this.lastTouchY);
                              var4 = var3;
                           }
                        } else {
                           var15 = this.currentChat;
                           var4 = var3;
                           if (var15 != null) {
                              var11.didPressChannelAvatar(this, var15, this.currentMessageObject.messageOwner.fwd_from.channel_post, this.lastTouchX, this.lastTouchY);
                              var4 = var3;
                           }
                        }
                     }
                  } else if (var1.getAction() == 3) {
                     this.avatarPressed = false;
                     var4 = var3;
                  } else {
                     var4 = var3;
                     if (var1.getAction() == 2) {
                        var4 = var3;
                        if (this.isAvatarVisible) {
                           var4 = var3;
                           if (!this.avatarImage.isInsideImage(var6, var7 + (float)this.getTop())) {
                              this.avatarPressed = false;
                              var4 = var3;
                           }
                        }
                     }
                  }
               } else if (this.forwardNamePressed) {
                  if (var1.getAction() == 1) {
                     this.forwardNamePressed = false;
                     this.playSoundEffect(0);
                     var11 = this.delegate;
                     var4 = var3;
                     if (var11 != null) {
                        var15 = this.currentForwardChannel;
                        if (var15 != null) {
                           var11.didPressChannelAvatar(this, var15, this.currentMessageObject.messageOwner.fwd_from.channel_post, this.lastTouchX, this.lastTouchY);
                           var4 = var3;
                        } else {
                           var14 = this.currentForwardUser;
                           if (var14 != null) {
                              var11.didPressUserAvatar(this, var14, this.lastTouchX, this.lastTouchY);
                              var4 = var3;
                           } else {
                              var4 = var3;
                              if (this.currentForwardName != null) {
                                 var11.didPressHiddenForward(this);
                                 var4 = var3;
                              }
                           }
                        }
                     }
                  } else if (var1.getAction() == 3) {
                     this.forwardNamePressed = false;
                     var4 = var3;
                  } else {
                     var4 = var3;
                     if (var1.getAction() == 2) {
                        var8 = this.forwardNameX;
                        if (var6 >= (float)var8 && var6 <= (float)(var8 + this.forwardedNameWidth)) {
                           var8 = this.forwardNameY;
                           if (var7 >= (float)var8) {
                              var4 = var3;
                              if (var7 <= (float)(var8 + AndroidUtilities.dp(32.0F))) {
                                 return var4;
                              }
                           }
                        }

                        this.forwardNamePressed = false;
                        var4 = var3;
                     }
                  }
               } else if (this.forwardBotPressed) {
                  if (var1.getAction() == 1) {
                     this.forwardBotPressed = false;
                     this.playSoundEffect(0);
                     ChatMessageCell.ChatMessageCellDelegate var16 = this.delegate;
                     var4 = var3;
                     if (var16 != null) {
                        TLRPC.User var12 = this.currentViaBotUser;
                        String var13;
                        if (var12 != null) {
                           var13 = var12.username;
                        } else {
                           var13 = this.currentMessageObject.messageOwner.via_bot_name;
                        }

                        var16.didPressViaBot(this, var13);
                        var4 = var3;
                     }
                  } else if (var1.getAction() == 3) {
                     this.forwardBotPressed = false;
                     var4 = var3;
                  } else {
                     var4 = var3;
                     if (var1.getAction() == 2) {
                        if (this.drawForwardedName && this.forwardedNameLayout[0] != null) {
                           var8 = this.forwardNameX;
                           if (var6 >= (float)var8 && var6 <= (float)(var8 + this.forwardedNameWidth)) {
                              var8 = this.forwardNameY;
                              if (var7 >= (float)var8) {
                                 var4 = var3;
                                 if (var7 <= (float)(var8 + AndroidUtilities.dp(32.0F))) {
                                    return var4;
                                 }
                              }
                           }

                           this.forwardBotPressed = false;
                           var4 = var3;
                        } else {
                           var9 = this.nameX;
                           var8 = this.viaNameWidth;
                           if (var6 >= (float)var8 + var9 && var6 <= var9 + (float)var8 + (float)this.viaWidth && var7 >= this.nameY - (float)AndroidUtilities.dp(4.0F)) {
                              var4 = var3;
                              if (var7 <= this.nameY + (float)AndroidUtilities.dp(20.0F)) {
                                 return var4;
                              }
                           }

                           this.forwardBotPressed = false;
                           var4 = var3;
                        }
                     }
                  }
               } else if (this.replyPressed) {
                  if (var1.getAction() == 1) {
                     this.replyPressed = false;
                     this.playSoundEffect(0);
                     var11 = this.delegate;
                     var4 = var3;
                     if (var11 != null) {
                        var11.didPressReplyMessage(this, this.currentMessageObject.messageOwner.reply_to_msg_id);
                        var4 = var3;
                     }
                  } else if (var1.getAction() == 3) {
                     this.replyPressed = false;
                     var4 = var3;
                  } else {
                     var4 = var3;
                     if (var1.getAction() == 2) {
                        if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                           var10 = this.replyStartX;
                           var8 = Math.max(this.replyNameWidth, this.replyTextWidth);
                        } else {
                           var10 = this.replyStartX;
                           var8 = this.backgroundDrawableRight;
                        }

                        if (var6 >= (float)this.replyStartX && var6 <= (float)(var10 + var8)) {
                           var8 = this.replyStartY;
                           if (var7 >= (float)var8) {
                              var4 = var3;
                              if (var7 <= (float)(var8 + AndroidUtilities.dp(35.0F))) {
                                 return var4;
                              }
                           }
                        }

                        this.replyPressed = false;
                        var4 = var3;
                     }
                  }
               } else {
                  var4 = var3;
                  if (this.sharePressed) {
                     if (var1.getAction() == 1) {
                        this.sharePressed = false;
                        this.playSoundEffect(0);
                        var11 = this.delegate;
                        if (var11 != null) {
                           var11.didPressShare(this);
                        }
                     } else if (var1.getAction() == 3) {
                        this.sharePressed = false;
                     } else if (var1.getAction() == 2) {
                        label340: {
                           var8 = this.shareStartX;
                           if (var6 >= (float)var8 && var6 <= (float)(var8 + AndroidUtilities.dp(40.0F))) {
                              var8 = this.shareStartY;
                              if (var7 >= (float)var8 && var7 <= (float)(var8 + AndroidUtilities.dp(32.0F))) {
                                 break label340;
                              }
                           }

                           this.sharePressed = false;
                        }
                     }

                     this.invalidate();
                     var4 = var3;
                  }
               }
            }
         }

         return var4;
      } else {
         return super.onTouchEvent(var1);
      }
   }

   public boolean performAccessibilityAction(int var1, Bundle var2) {
      if (var1 == 16) {
         if (this.getIconForCurrentState() != 4) {
            this.didPressButton(true, false);
         } else if (this.currentMessageObject.type == 16) {
            this.delegate.didPressOther(this, (float)this.otherX, (float)this.otherY);
         } else {
            this.didClickedImage();
         }

         return true;
      } else {
         if (var1 == 2131230728) {
            this.didPressMiniButton(true);
         } else if (var1 == 2131230726) {
            ChatMessageCell.ChatMessageCellDelegate var3 = this.delegate;
            if (var3 != null) {
               if (this.currentMessageObject.type == 16) {
                  var3.didLongPress(this, 0.0F, 0.0F);
               } else {
                  var3.didPressOther(this, (float)this.otherX, (float)this.otherY);
               }
            }
         }

         return super.performAccessibilityAction(var1, var2);
      }
   }

   public void requestLayout() {
      if (!this.inLayout) {
         super.requestLayout();
      }
   }

   public void setAllowAssistant(boolean var1) {
      this.allowAssistant = var1;
   }

   public void setCheckBoxVisible(boolean var1, boolean var2) {
      if (var1 && this.checkBox == null) {
         this.checkBox = new CheckBoxBase(this);
         if (this.attachedToWindow) {
            this.checkBox.onAttachedToWindow();
         }
      }

      if (var1 && this.photoCheckBox == null) {
         MessageObject.GroupedMessages var3 = this.currentMessagesGroup;
         if (var3 != null && var3.messages.size() > 1) {
            this.photoCheckBox = new CheckBoxBase(this);
            this.photoCheckBox.setUseDefaultCheck(true);
            if (this.attachedToWindow) {
               this.photoCheckBox.onAttachedToWindow();
            }
         }
      }

      boolean var4 = this.checkBoxVisible;
      float var5 = 1.0F;
      if (var4 == var1) {
         if (var2 != this.checkBoxAnimationInProgress && !var2) {
            if (!var1) {
               var5 = 0.0F;
            }

            this.checkBoxAnimationProgress = var5;
            this.invalidate();
         }

      } else {
         this.checkBoxAnimationInProgress = var2;
         this.checkBoxVisible = var1;
         if (var2) {
            this.lastCheckBoxAnimationTime = SystemClock.uptimeMillis();
         } else {
            if (!var1) {
               var5 = 0.0F;
            }

            this.checkBoxAnimationProgress = var5;
         }

         this.invalidate();
      }
   }

   public void setCheckPressed(boolean var1, boolean var2) {
      this.isCheckPressed = var1;
      this.isPressed = var2;
      this.updateRadialProgressBackground();
      if (this.useSeekBarWaweform) {
         this.seekBarWaveform.setSelected(this.isDrawSelectionBackground());
      } else {
         this.seekBar.setSelected(this.isDrawSelectionBackground());
      }

      this.invalidate();
   }

   public void setChecked(boolean var1, boolean var2, boolean var3) {
      CheckBoxBase var4 = this.checkBox;
      if (var4 != null) {
         var4.setChecked(var2, var3);
      }

      var4 = this.photoCheckBox;
      if (var4 != null) {
         var4.setChecked(var1, var3);
      }

   }

   public void setDelegate(ChatMessageCell.ChatMessageCellDelegate var1) {
      this.delegate = var1;
   }

   public void setDrawSelectionBackground(boolean var1) {
      this.drawSelectionBackground = var1;
      this.invalidate();
   }

   public void setFullyDraw(boolean var1) {
      this.fullyDraw = var1;
   }

   public void setHighlighted(boolean var1) {
      if (this.isHighlighted != var1) {
         this.isHighlighted = var1;
         if (!this.isHighlighted) {
            this.lastHighlightProgressTime = System.currentTimeMillis();
            this.isHighlightedAnimated = true;
            this.highlightProgress = 300;
         } else {
            this.isHighlightedAnimated = false;
            this.highlightProgress = 0;
         }

         this.updateRadialProgressBackground();
         if (this.useSeekBarWaweform) {
            this.seekBarWaveform.setSelected(this.isDrawSelectionBackground());
         } else {
            this.seekBar.setSelected(this.isDrawSelectionBackground());
         }

         this.invalidate();
         if (this.getParent() != null) {
            ((View)this.getParent()).invalidate();
         }

      }
   }

   public void setHighlightedAnimated() {
      this.isHighlightedAnimated = true;
      this.highlightProgress = 1000;
      this.lastHighlightProgressTime = System.currentTimeMillis();
      this.invalidate();
      if (this.getParent() != null) {
         ((View)this.getParent()).invalidate();
      }

   }

   public void setHighlightedText(String var1) {
      MessageObject var2 = this.messageObjectToSet;
      if (var2 == null) {
         var2 = this.currentMessageObject;
      }

      if (var2 != null && !TextUtils.isEmpty(var1)) {
         String var3 = var1.toLowerCase();
         var1 = var2.messageOwner.message.toLowerCase();
         int var4 = var1.length();
         int var5 = 0;
         int var6 = -1;

         int var7;
         int var12;
         int var13;
         for(var7 = -1; var5 < var4; var7 = var12) {
            int var8 = Math.min(var3.length(), var4 - var5);
            int var9 = 0;
            int var10 = 0;

            int var11;
            while(true) {
               var11 = var6;
               var12 = var7;
               if (var9 >= var8) {
                  break;
               }

               boolean var23;
               if (var1.charAt(var5 + var9) == var3.charAt(var9)) {
                  var23 = true;
               } else {
                  var23 = false;
               }

               boolean var22 = var23;
               var13 = var10;
               if (var23) {
                  if (var10 == 0 && var5 != 0 && " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\n".indexOf(var1.charAt(var5 - 1)) < 0) {
                     var22 = false;
                     var13 = var10;
                  } else {
                     var13 = var10 + 1;
                     var22 = var23;
                  }
               }

               if (!var22 || var9 == var8 - 1) {
                  var11 = var6;
                  var12 = var7;
                  if (var13 > 0) {
                     var11 = var6;
                     var12 = var7;
                     if (var13 > var7) {
                        var11 = var5;
                        var12 = var13;
                     }
                  }
                  break;
               }

               ++var9;
               var10 = var13;
            }

            ++var5;
            var6 = var11;
         }

         if (var6 == -1) {
            if (!this.urlPathSelection.isEmpty()) {
               this.linkSelectionBlockNum = -1;
               this.resetUrlPaths(true);
               this.invalidate();
            }

         } else {
            var5 = var6 + var7;

            for(var13 = var1.length(); var5 < var13 && " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\n".indexOf(var1.charAt(var5)) < 0; ++var5) {
               ++var7;
            }

            var13 = var6 + var7;
            if (this.captionLayout != null && !TextUtils.isEmpty(var2.caption)) {
               this.resetUrlPaths(true);

               try {
                  LinkPath var20 = this.obtainNewUrlPath(true);
                  var20.setCurrentLayout(this.captionLayout, var6, 0.0F);
                  this.captionLayout.getSelectionPath(var6, var13, var20);
               } catch (Exception var15) {
                  FileLog.e((Throwable)var15);
               }

               this.invalidate();
            } else if (var2.textLayoutBlocks != null) {
               for(var5 = 0; var5 < var2.textLayoutBlocks.size(); ++var5) {
                  MessageObject.TextLayoutBlock var18 = (MessageObject.TextLayoutBlock)var2.textLayoutBlocks.get(var5);
                  var12 = var18.charactersOffset;
                  if (var6 >= var12 && var6 < var12 + var18.textLayout.getText().length()) {
                     this.linkSelectionBlockNum = var5;
                     this.resetUrlPaths(true);

                     label100: {
                        Exception var10000;
                        label161: {
                           boolean var10001;
                           LinkPath var21;
                           try {
                              var21 = this.obtainNewUrlPath(true);
                              var21.setCurrentLayout(var18.textLayout, var6, 0.0F);
                              var18.textLayout.getSelectionPath(var6, var13 - var18.charactersOffset, var21);
                              if (var13 < var18.charactersOffset + var7) {
                                 break label100;
                              }
                           } catch (Exception var17) {
                              var10000 = var17;
                              var10001 = false;
                              break label161;
                           }

                           var7 = var5 + 1;

                           while(true) {
                              try {
                                 if (var7 >= var2.textLayoutBlocks.size()) {
                                    break label100;
                                 }

                                 MessageObject.TextLayoutBlock var14 = (MessageObject.TextLayoutBlock)var2.textLayoutBlocks.get(var7);
                                 var5 = var14.textLayout.getText().length();
                                 var21 = this.obtainNewUrlPath(true);
                                 var21.setCurrentLayout(var14.textLayout, 0, (float)var14.height);
                                 var14.textLayout.getSelectionPath(0, var13 - var14.charactersOffset, var21);
                                 var12 = var18.charactersOffset;
                              } catch (Exception var16) {
                                 var10000 = var16;
                                 var10001 = false;
                                 break;
                              }

                              if (var13 < var12 + var5 - 1) {
                                 break label100;
                              }

                              ++var7;
                           }
                        }

                        Exception var19 = var10000;
                        FileLog.e((Throwable)var19);
                     }

                     this.invalidate();
                     break;
                  }
               }
            }

         }
      } else {
         if (!this.urlPathSelection.isEmpty()) {
            this.linkSelectionBlockNum = -1;
            this.resetUrlPaths(true);
            this.invalidate();
         }

      }
   }

   public void setInvalidatesParent(boolean var1) {
      this.invalidatesParent = var1;
   }

   public void setMessageObject(MessageObject var1, MessageObject.GroupedMessages var2, boolean var3, boolean var4) {
      if (this.attachedToWindow) {
         this.setMessageContent(var1, var2, var3, var4);
      } else {
         this.messageObjectToSet = var1;
         this.groupedMessagesToSet = var2;
         this.bottomNearToSet = var3;
         this.topNearToSet = var4;
      }

   }

   public void setPressed(boolean var1) {
      super.setPressed(var1);
      this.updateRadialProgressBackground();
      if (this.useSeekBarWaweform) {
         this.seekBarWaveform.setSelected(this.isDrawSelectionBackground());
      } else {
         this.seekBar.setSelected(this.isDrawSelectionBackground());
      }

      this.invalidate();
   }

   public void setTimeAlpha(float var1) {
      this.timeAlpha = var1;
   }

   public void setVisiblePart(int var1, int var2) {
      MessageObject var3 = this.currentMessageObject;
      if (var3 != null && var3.textLayoutBlocks != null) {
         int var4 = var1 - this.textY;
         int var5 = 0;

         for(var1 = 0; var5 < this.currentMessageObject.textLayoutBlocks.size() && ((MessageObject.TextLayoutBlock)this.currentMessageObject.textLayoutBlocks.get(var5)).textYOffset <= (float)var4; var1 = var5++) {
         }

         var5 = -1;
         int var6 = -1;

         int var7;
         int var12;
         for(var7 = 0; var1 < this.currentMessageObject.textLayoutBlocks.size(); var7 = var12) {
            MessageObject.TextLayoutBlock var14 = (MessageObject.TextLayoutBlock)this.currentMessageObject.textLayoutBlocks.get(var1);
            float var8 = var14.textYOffset;
            float var9 = (float)var14.height;
            float var10 = (float)var4;
            int var11;
            int var13;
            if (this.intersect(var8, var9 + var8, var10, (float)(var4 + var2))) {
               var11 = var5;
               if (var5 == -1) {
                  var11 = var1;
               }

               var12 = var7 + 1;
               var13 = var1;
            } else {
               var11 = var5;
               var13 = var6;
               var12 = var7;
               if (var8 > var10) {
                  break;
               }
            }

            ++var1;
            var5 = var11;
            var6 = var13;
         }

         if (this.lastVisibleBlockNum != var6 || this.firstVisibleBlockNum != var5 || this.totalVisibleBlocksCount != var7) {
            this.lastVisibleBlockNum = var6;
            this.firstVisibleBlockNum = var5;
            this.totalVisibleBlocksCount = var7;
            this.invalidate();
         }
      }

   }

   public void updateButtonState(boolean var1, boolean var2, boolean var3) {
      boolean var4;
      if (!var2 || !PhotoViewer.isShowingImage(this.currentMessageObject) && this.attachedToWindow) {
         var4 = var2;
      } else {
         var4 = false;
      }

      this.drawRadialCheckBackground = false;
      String var5 = null;
      int var6 = this.currentMessageObject.type;
      boolean var7 = true;
      int var8;
      MessageObject var9;
      if (var6 == 1) {
         TLRPC.PhotoSize var19 = this.currentPhotoObject;
         if (var19 == null) {
            this.radialProgress.setIcon(4, var1, var4);
            return;
         }

         var5 = FileLoader.getAttachFileName(var19);
         var2 = this.currentMessageObject.mediaExists;
      } else {
         label635: {
            label634: {
               label648: {
                  if (var6 != 8) {
                     var8 = this.documentAttachType;
                     if (var8 != 7 && var8 != 4 && var8 != 8 && var6 != 9 && var8 != 3 && var8 != 5) {
                        if (var8 != 0) {
                           var5 = FileLoader.getAttachFileName(this.documentAttach);
                           var2 = this.currentMessageObject.mediaExists;
                           break label635;
                        }

                        TLRPC.PhotoSize var22 = this.currentPhotoObject;
                        if (var22 != null) {
                           var5 = FileLoader.getAttachFileName(var22);
                           var2 = this.currentMessageObject.mediaExists;
                           break label635;
                        }
                        break label648;
                     }
                  }

                  var9 = this.currentMessageObject;
                  if (var9.useCustomPhoto) {
                     this.buttonState = 1;
                     this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var4);
                     return;
                  }

                  if (var9.attachPathExists && !TextUtils.isEmpty(var9.messageOwner.attachPath)) {
                     var5 = this.currentMessageObject.messageOwner.attachPath;
                     var2 = true;
                     break label635;
                  }

                  if (!this.currentMessageObject.isSendError()) {
                     break label634;
                  }

                  var8 = this.documentAttachType;
                  if (var8 == 3 || var8 == 5) {
                     break label634;
                  }
               }

               var2 = false;
               break label635;
            }

            var5 = this.currentMessageObject.getFileName();
            var2 = this.currentMessageObject.mediaExists;
         }
      }

      boolean var10;
      boolean var11;
      label609: {
         var10 = DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.currentMessageObject);
         if (this.currentMessageObject.isSent()) {
            var8 = this.documentAttachType;
            if ((var8 == 4 || var8 == 7 || var8 == 2 && var10) && this.currentMessageObject.canStreamVideo() && !this.currentMessageObject.needDrawBluredPreview()) {
               var11 = true;
               break label609;
            }
         }

         var11 = false;
      }

      this.canStreamVideo = var11;
      byte var23;
      if (SharedConfig.streamMedia) {
         var11 = var2;
         if ((int)this.currentMessageObject.getDialogId() != 0) {
            var11 = var2;
            if (!this.currentMessageObject.isSecretMedia()) {
               label649: {
                  if (this.documentAttachType != 5) {
                     var11 = var2;
                     if (!this.canStreamVideo) {
                        break label649;
                     }

                     MessageObject.GroupedMessagePosition var24 = this.currentPosition;
                     var11 = var2;
                     if (var24 == null) {
                        break label649;
                     }

                     var8 = var24.flags;
                     if ((var8 & 1) != 0) {
                        var11 = var2;
                        if ((var8 & 2) != 0) {
                           break label649;
                        }
                     }
                  }

                  if (var2) {
                     var23 = 1;
                  } else {
                     var23 = 2;
                  }

                  this.hasMiniProgress = var23;
                  var11 = true;
               }
            }
         }
      } else {
         var11 = var2;
      }

      if (!this.currentMessageObject.isSendError() && (!TextUtils.isEmpty(var5) || this.currentMessageObject.isSending() || this.currentMessageObject.isEditing())) {
         HashMap var25 = this.currentMessageObject.messageOwner.params;
         boolean var27;
         if (var25 != null && var25.containsKey("query_id")) {
            var27 = true;
         } else {
            var27 = false;
         }

         var6 = this.documentAttachType;
         float var12 = 0.0F;
         float var13 = 0.0F;
         float var14 = 0.0F;
         float var15 = 0.0F;
         Float var20;
         Float var31;
         RadialProgress2 var32;
         if (var6 != 3 && var6 != 5) {
            if (this.currentMessageObject.type == 0 && var6 != 1 && var6 != 2 && var6 != 7 && var6 != 4 && var6 != 8) {
               if (this.currentPhotoObject == null || !this.drawImageButton) {
                  return;
               }

               if (!var11) {
                  DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(var5, this.currentMessageObject, this);
                  if (FileLoader.getInstance(this.currentAccount).isLoadingFile(var5)) {
                     this.buttonState = 1;
                     var20 = ImageLoader.getInstance().getFileProgress(var5);
                     if (var20 != null) {
                        var15 = var20;
                     }
                  } else if (this.cancelLoading || (this.documentAttachType != 0 || !var10) && (this.documentAttachType != 2 || !MessageObject.isGifDocument(this.documentAttach) || !var10)) {
                     this.buttonState = 0;
                  } else {
                     this.buttonState = 1;
                  }

                  this.radialProgress.setProgress(var15, false);
                  this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var4);
                  this.invalidate();
               } else {
                  DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                  if (this.documentAttachType == 2 && !this.photoImage.isAllowStartAnimation()) {
                     this.buttonState = 2;
                  } else {
                     this.buttonState = -1;
                  }

                  this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var4);
                  this.invalidate();
               }
            } else {
               boolean var16 = var4;
               RadialProgress2 var26;
               MessageObject var28;
               if (!this.currentMessageObject.isOut() || !this.currentMessageObject.isSending() && !this.currentMessageObject.isEditing()) {
                  if (this.wasSending && !TextUtils.isEmpty(this.currentMessageObject.messageOwner.attachPath)) {
                     DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                  }

                  var8 = this.documentAttachType;
                  if ((var8 == 4 || var8 == 2 || var8 == 7) && this.autoPlayingMedia) {
                     boolean var17 = FileLoader.getInstance(this.currentAccount).isLoadingVideo(this.documentAttach, MediaController.getInstance().isPlayingMessage(this.currentMessageObject));
                     AnimatedFileDrawable var35 = this.photoImage.getAnimation();
                     if (var35 != null) {
                        MessageObject var36 = this.currentMessageObject;
                        if (var36.hadAnimationNotReadyLoading) {
                           var4 = var17;
                           if (var35.hasBitmap()) {
                              this.currentMessageObject.hadAnimationNotReadyLoading = false;
                              var4 = var17;
                           }
                        } else {
                           if (var17 && !var35.hasBitmap()) {
                              var4 = true;
                           } else {
                              var4 = false;
                           }

                           var36.hadAnimationNotReadyLoading = var4;
                           var4 = var17;
                        }
                     } else {
                        var4 = var17;
                        if (this.documentAttachType == 2) {
                           var4 = var17;
                           if (!var11) {
                              this.currentMessageObject.hadAnimationNotReadyLoading = true;
                              var4 = var17;
                           }
                        }
                     }
                  } else {
                     var4 = false;
                  }

                  if (this.hasMiniProgress != 0) {
                     this.radialProgress.setMiniProgressBackgroundColor(Theme.getColor("chat_inLoaderPhoto"));
                     this.buttonState = 3;
                     this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var16);
                     if (this.hasMiniProgress == 1) {
                        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                        this.miniButtonState = -1;
                     } else {
                        DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(var5, this.currentMessageObject, this);
                        if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(var5)) {
                           this.miniButtonState = 0;
                        } else {
                           this.miniButtonState = 1;
                           var20 = ImageLoader.getInstance().getFileProgress(var5);
                           if (var20 != null) {
                              this.radialProgress.setProgress(var20, var16);
                           } else {
                              this.radialProgress.setProgress(0.0F, var16);
                           }
                        }
                     }

                     this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), var1, var16);
                  } else {
                     label690: {
                        if (!var11) {
                           var8 = this.documentAttachType;
                           if (var8 != 4 && var8 != 2 && var8 != 7 || !this.autoPlayingMedia || this.currentMessageObject.hadAnimationNotReadyLoading || var4) {
                              var8 = this.documentAttachType;
                              if (var8 != 4 && var8 != 2) {
                                 var3 = false;
                              } else {
                                 var3 = true;
                              }

                              this.drawVideoSize = var3;
                              var8 = this.documentAttachType;
                              if ((var8 == 4 || var8 == 2 || var8 == 7) && this.canStreamVideo && !this.drawVideoImageButton && var16) {
                                 var8 = this.animatingDrawVideoImageButton;
                                 if (var8 != 2 && this.animatingDrawVideoImageButtonProgress < 1.0F) {
                                    if (var8 == 0) {
                                       this.animatingDrawVideoImageButtonProgress = 0.0F;
                                    }

                                    this.animatingDrawVideoImageButton = 2;
                                 }
                              } else if (this.animatingDrawVideoImageButton == 0) {
                                 this.animatingDrawVideoImageButtonProgress = 1.0F;
                              }

                              DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(var5, this.currentMessageObject, this);
                              if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(var5)) {
                                 if (!this.cancelLoading && var10) {
                                    this.buttonState = 1;
                                 } else {
                                    this.buttonState = 0;
                                 }

                                 var8 = this.documentAttachType;
                                 if ((var8 == 4 || var8 == 2 && var10) && this.canStreamVideo) {
                                    this.drawVideoImageButton = true;
                                    this.getIconForCurrentState();
                                    var26 = this.radialProgress;
                                    if (this.autoPlayingMedia) {
                                       var23 = 4;
                                    } else {
                                       var23 = 0;
                                    }

                                    var26.setIcon(var23, var1, var16);
                                    this.videoRadialProgress.setIcon(2, var1, var16);
                                 } else {
                                    this.drawVideoImageButton = false;
                                    this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var16);
                                    this.videoRadialProgress.setIcon(4, var1, false);
                                    if (!this.drawVideoSize && this.animatingDrawVideoImageButton == 0) {
                                       this.animatingDrawVideoImageButtonProgress = 0.0F;
                                    }
                                 }
                              } else {
                                 this.buttonState = 1;
                                 var20 = ImageLoader.getInstance().getFileProgress(var5);
                                 var8 = this.documentAttachType;
                                 if ((var8 == 4 || var8 == 2 && var10) && this.canStreamVideo) {
                                    this.drawVideoImageButton = true;
                                    this.getIconForCurrentState();
                                    var32 = this.radialProgress;
                                    if (!this.autoPlayingMedia && this.documentAttachType != 2) {
                                       var23 = 0;
                                    } else {
                                       var23 = 4;
                                    }

                                    var32.setIcon(var23, var1, var16);
                                    var32 = this.videoRadialProgress;
                                    var15 = var13;
                                    if (var20 != null) {
                                       var15 = var20;
                                    }

                                    var32.setProgress(var15, var16);
                                    this.videoRadialProgress.setIcon(14, var1, var16);
                                 } else {
                                    this.drawVideoImageButton = false;
                                    var32 = this.radialProgress;
                                    if (var20 != null) {
                                       var15 = var20;
                                    } else {
                                       var15 = 0.0F;
                                    }

                                    var32.setProgress(var15, var16);
                                    this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var16);
                                    this.videoRadialProgress.setIcon(4, var1, false);
                                    if (!this.drawVideoSize && this.animatingDrawVideoImageButton == 0) {
                                       this.animatingDrawVideoImageButtonProgress = 0.0F;
                                    }
                                 }
                              }

                              this.invalidate();
                              break label690;
                           }
                        }

                        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                        if (this.drawVideoImageButton && var16) {
                           var8 = this.animatingDrawVideoImageButton;
                           if (var8 != 1 && this.animatingDrawVideoImageButtonProgress > 0.0F) {
                              if (var8 == 0) {
                                 this.animatingDrawVideoImageButtonProgress = 1.0F;
                              }

                              this.animatingDrawVideoImageButton = 1;
                           }
                        } else if (this.animatingDrawVideoImageButton == 0) {
                           this.animatingDrawVideoImageButtonProgress = 0.0F;
                        }

                        this.drawVideoImageButton = false;
                        this.drawVideoSize = false;
                        if (this.currentMessageObject.needDrawBluredPreview()) {
                           this.buttonState = -1;
                        } else {
                           var28 = this.currentMessageObject;
                           if (var28.type == 8 && var28.gifState == 1.0F) {
                              this.buttonState = 2;
                           } else if (this.documentAttachType == 4) {
                              this.buttonState = 3;
                           } else {
                              this.buttonState = -1;
                           }
                        }

                        var26 = this.videoRadialProgress;
                        if (this.animatingDrawVideoImageButton != 0) {
                           var4 = var7;
                        } else {
                           var4 = false;
                        }

                        var26.setIcon(4, var1, var4);
                        this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var16);
                        if (!var3 && this.photoNotSet) {
                           this.setMessageObject(this.currentMessageObject, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
                        }

                        this.invalidate();
                     }
                  }
               } else {
                  if (!TextUtils.isEmpty(this.currentMessageObject.messageOwner.attachPath)) {
                     DownloadController var33 = DownloadController.getInstance(this.currentAccount);
                     var28 = this.currentMessageObject;
                     var33.addLoadingFileObserver(var28.messageOwner.attachPath, var28, this);
                     this.wasSending = true;
                     var5 = this.currentMessageObject.messageOwner.attachPath;
                     if (var5 != null && var5.startsWith("http")) {
                        var27 = false;
                     } else {
                        var27 = true;
                     }

                     TLRPC.Message var34 = this.currentMessageObject.messageOwner;
                     HashMap var30 = var34.params;
                     if (var34.message == null || var30 == null || !var30.containsKey("url") && !var30.containsKey("bot")) {
                        this.buttonState = 1;
                     } else {
                        this.buttonState = -1;
                        var27 = false;
                     }

                     var3 = SendMessagesHelper.getInstance(this.currentAccount).isSendingMessage(this.currentMessageObject.getId());
                     if (this.currentPosition != null && var3 && this.buttonState == 1) {
                        this.drawRadialCheckBackground = true;
                        this.getIconForCurrentState();
                        this.radialProgress.setIcon(6, var1, var4);
                     } else {
                        this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var4);
                     }

                     if (var27) {
                        var31 = ImageLoader.getInstance().getFileProgress(this.currentMessageObject.messageOwner.attachPath);
                        var20 = var31;
                        if (var31 == null) {
                           var20 = var31;
                           if (var3) {
                              var20 = 1.0F;
                           }
                        }

                        var32 = this.radialProgress;
                        var15 = var12;
                        if (var20 != null) {
                           var15 = var20;
                        }

                        var32.setProgress(var15, false);
                     } else {
                        this.radialProgress.setProgress(0.0F, false);
                     }

                     this.invalidate();
                  } else {
                     this.buttonState = -1;
                     this.getIconForCurrentState();
                     var26 = this.radialProgress;
                     if (!this.currentMessageObject.isSticker() && !this.currentMessageObject.isAnimatedSticker() && !this.currentMessageObject.isLocation()) {
                        var23 = 12;
                     } else {
                        var23 = 4;
                     }

                     var26.setIcon(var23, var1, false);
                     this.radialProgress.setProgress(0.0F, false);
                  }

                  this.videoRadialProgress.setIcon(4, var1, false);
               }
            }
         } else {
            var3 = var4;
            if (this.currentMessageObject.isOut() && (this.currentMessageObject.isSending() || this.currentMessageObject.isEditing()) || this.currentMessageObject.isSendError() && var27) {
               if (!TextUtils.isEmpty(this.currentMessageObject.messageOwner.attachPath)) {
                  DownloadController var21 = DownloadController.getInstance(this.currentAccount);
                  var9 = this.currentMessageObject;
                  var21.addLoadingFileObserver(var9.messageOwner.attachPath, var9, this);
                  this.wasSending = true;
                  this.buttonState = 4;
                  this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var4);
                  if (!var27) {
                     var31 = ImageLoader.getInstance().getFileProgress(this.currentMessageObject.messageOwner.attachPath);
                     var20 = var31;
                     if (var31 == null) {
                        var20 = var31;
                        if (SendMessagesHelper.getInstance(this.currentAccount).isSendingMessage(this.currentMessageObject.getId())) {
                           var20 = 1.0F;
                        }
                     }

                     var32 = this.radialProgress;
                     var15 = var14;
                     if (var20 != null) {
                        var15 = var20;
                     }

                     var32.setProgress(var15, false);
                  } else {
                     this.radialProgress.setProgress(0.0F, false);
                  }
               } else {
                  this.buttonState = -1;
                  this.getIconForCurrentState();
                  this.radialProgress.setIcon(12, var1, false);
                  this.radialProgress.setProgress(0.0F, false);
               }
            } else if (this.hasMiniProgress != 0) {
               RadialProgress2 var18 = this.radialProgress;
               String var29;
               if (this.currentMessageObject.isOutOwner()) {
                  var29 = "chat_outLoader";
               } else {
                  var29 = "chat_inLoader";
               }

               var18.setMiniProgressBackgroundColor(Theme.getColor(var29));
               var4 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
               if (!var4 || var4 && MediaController.getInstance().isMessagePaused()) {
                  this.buttonState = 0;
               } else {
                  this.buttonState = 1;
               }

               this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var3);
               if (this.hasMiniProgress == 1) {
                  DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                  this.miniButtonState = -1;
               } else {
                  DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(var5, this.currentMessageObject, this);
                  if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(var5)) {
                     this.miniButtonState = 0;
                  } else {
                     this.miniButtonState = 1;
                     var20 = ImageLoader.getInstance().getFileProgress(var5);
                     if (var20 != null) {
                        this.radialProgress.setProgress(var20, var3);
                     } else {
                        this.radialProgress.setProgress(0.0F, var3);
                     }
                  }
               }

               this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), var1, var3);
            } else if (!var11) {
               DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(var5, this.currentMessageObject, this);
               if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(var5)) {
                  this.buttonState = 2;
                  this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var4);
               } else {
                  this.buttonState = 4;
                  var20 = ImageLoader.getInstance().getFileProgress(var5);
                  if (var20 != null) {
                     this.radialProgress.setProgress(var20, var4);
                  } else {
                     this.radialProgress.setProgress(0.0F, var4);
                  }

                  this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var4);
               }
            } else {
               DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
               var4 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
               if (!var4 || var4 && MediaController.getInstance().isMessagePaused()) {
                  this.buttonState = 0;
               } else {
                  this.buttonState = 1;
               }

               this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var3);
            }

            this.updatePlayingMessageProgress();
         }

         if (this.hasMiniProgress == 0) {
            this.radialProgress.setMiniIcon(4, false, var4);
         }

      } else {
         this.radialProgress.setIcon(4, var1, false);
         this.radialProgress.setMiniIcon(4, var1, false);
         this.videoRadialProgress.setIcon(4, var1, false);
         this.videoRadialProgress.setMiniIcon(4, var1, false);
      }
   }

   public void updatePlayingMessageProgress() {
      MessageObject var1 = this.currentMessageObject;
      if (var1 != null) {
         int var3;
         int var5;
         String var9;
         if (this.documentAttachType == 4) {
            if (PhotoViewer.isPlayingMessage(var1) || MediaController.getInstance().isGoingToShowMessageObject(this.currentMessageObject)) {
               return;
            }

            AnimatedFileDrawable var11 = this.photoImage.getAnimation();
            if (var11 != null) {
               MessageObject var10 = this.currentMessageObject;
               var3 = var11.getDurationMs() / 1000;
               var10.audioPlayerDuration = var3;
               var10 = this.currentMessageObject;
               TLRPC.Message var4 = var10.messageOwner;
               var5 = var3;
               if (var4.ttl > 0) {
                  var5 = var3;
                  if (var4.destroyTime == 0) {
                     var5 = var3;
                     if (!var10.needDrawBluredPreview()) {
                        var5 = var3;
                        if (this.currentMessageObject.isVideo()) {
                           var5 = var3;
                           if (var11.hasBitmap()) {
                              this.delegate.didStartVideoStream(this.currentMessageObject);
                              var5 = var3;
                           }
                        }
                     }
                  }
               }
            } else {
               var5 = 0;
            }

            var3 = var5;
            if (var5 == 0) {
               var3 = this.currentMessageObject.getDuration();
            }

            if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
               float var6 = (float)var3;
               var5 = (int)(var6 - this.currentMessageObject.audioProgress * var6);
            } else {
               var5 = var3;
               if (var11 != null) {
                  int var7 = var3;
                  if (var3 != 0) {
                     var7 = var3 - var11.getCurrentProgressMs() / 1000;
                  }

                  var5 = var7;
                  if (this.delegate != null) {
                     var5 = var7;
                     if (var11.getCurrentProgressMs() >= 3000) {
                        this.delegate.videoTimerReached();
                        var5 = var7;
                     }
                  }
               }
            }

            var3 = var5 / 60;
            if (this.lastTime != var5) {
               var9 = String.format("%d:%02d", var3, var5 - var3 * 60);
               this.infoWidth = (int)Math.ceil((double)Theme.chat_infoPaint.measureText(var9));
               this.infoLayout = new StaticLayout(var9, Theme.chat_infoPaint, this.infoWidth, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
               this.lastTime = var5;
            }
         } else {
            TLRPC.DocumentAttribute var8;
            if (var1.isRoundVideo()) {
               TLRPC.Document var2 = this.currentMessageObject.getDocument();
               var3 = 0;

               while(true) {
                  if (var3 >= var2.attributes.size()) {
                     var3 = 0;
                     break;
                  }

                  var8 = (TLRPC.DocumentAttribute)var2.attributes.get(var3);
                  if (var8 instanceof TLRPC.TL_documentAttributeVideo) {
                     var3 = var8.duration;
                     break;
                  }

                  ++var3;
               }

               var5 = var3;
               if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                  var5 = Math.max(0, var3 - this.currentMessageObject.audioProgressSec);
               }

               if (this.lastTime != var5) {
                  this.lastTime = var5;
                  var9 = String.format("%02d:%02d", var5 / 60, var5 % 60);
                  this.timeWidthAudio = (int)Math.ceil((double)Theme.chat_timePaint.measureText(var9));
                  this.durationLayout = new StaticLayout(var9, Theme.chat_timePaint, this.timeWidthAudio, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                  this.invalidate();
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

               if (this.documentAttachType != 3) {
                  var5 = this.currentMessageObject.getDuration();
                  if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                     var3 = this.currentMessageObject.audioProgressSec;
                  } else {
                     var3 = 0;
                  }

                  if (this.lastTime != var3) {
                     this.lastTime = var3;
                     if (var5 == 0) {
                        var9 = String.format("%d:%02d / -:--", var3 / 60, var3 % 60);
                     } else {
                        var9 = String.format("%d:%02d / %d:%02d", var3 / 60, var3 % 60, var5 / 60, var5 % 60);
                     }

                     var3 = (int)Math.ceil((double)Theme.chat_audioTimePaint.measureText(var9));
                     this.durationLayout = new StaticLayout(var9, Theme.chat_audioTimePaint, var3, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                  }
               } else {
                  if (!MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                     var3 = 0;

                     while(true) {
                        if (var3 >= this.documentAttach.attributes.size()) {
                           var3 = 0;
                           break;
                        }

                        var8 = (TLRPC.DocumentAttribute)this.documentAttach.attributes.get(var3);
                        if (var8 instanceof TLRPC.TL_documentAttributeAudio) {
                           var3 = var8.duration;
                           break;
                        }

                        ++var3;
                     }
                  } else {
                     var3 = this.currentMessageObject.audioProgressSec;
                  }

                  if (this.lastTime != var3) {
                     this.lastTime = var3;
                     var9 = String.format("%02d:%02d", var3 / 60, var3 % 60);
                     this.timeWidthAudio = (int)Math.ceil((double)Theme.chat_audioTimePaint.measureText(var9));
                     this.durationLayout = new StaticLayout(var9, Theme.chat_audioTimePaint, this.timeWidthAudio, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                  }
               }

               this.invalidate();
            }
         }

      }
   }

   protected boolean verifyDrawable(Drawable var1) {
      boolean var2;
      if (!super.verifyDrawable(var1) && var1 != this.selectorDrawable) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private class BotButton {
      private int angle;
      private TLRPC.KeyboardButton button;
      private int height;
      private long lastUpdateTime;
      private float progressAlpha;
      private StaticLayout title;
      private int width;
      private int x;
      private int y;

      private BotButton() {
      }

      // $FF: synthetic method
      BotButton(Object var2) {
         this();
      }
   }

   public interface ChatMessageCellDelegate {
      boolean canPerformActions();

      void didLongPress(ChatMessageCell var1, float var2, float var3);

      void didPressBotButton(ChatMessageCell var1, TLRPC.KeyboardButton var2);

      void didPressCancelSendButton(ChatMessageCell var1);

      void didPressChannelAvatar(ChatMessageCell var1, TLRPC.Chat var2, int var3, float var4, float var5);

      void didPressHiddenForward(ChatMessageCell var1);

      void didPressImage(ChatMessageCell var1, float var2, float var3);

      void didPressInstantButton(ChatMessageCell var1, int var2);

      void didPressOther(ChatMessageCell var1, float var2, float var3);

      void didPressReplyMessage(ChatMessageCell var1, int var2);

      void didPressShare(ChatMessageCell var1);

      void didPressUrl(MessageObject var1, CharacterStyle var2, boolean var3);

      void didPressUserAvatar(ChatMessageCell var1, TLRPC.User var2, float var3, float var4);

      void didPressViaBot(ChatMessageCell var1, String var2);

      void didPressVoteButton(ChatMessageCell var1, TLRPC.TL_pollAnswer var2);

      void didStartVideoStream(MessageObject var1);

      boolean isChatAdminCell(int var1);

      void needOpenWebView(String var1, String var2, String var3, String var4, int var5, int var6);

      boolean needPlayMessage(MessageObject var1);

      void videoTimerReached();
   }

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

      // $FF: synthetic method
      MessageAccessibilityNodeProvider(Object var2) {
         this();
      }

      private ClickableSpan getLinkById(int var1) {
         var1 -= 2000;
         if (!(ChatMessageCell.this.currentMessageObject.messageText instanceof Spannable)) {
            return null;
         } else {
            Spannable var2 = (Spannable)ChatMessageCell.this.currentMessageObject.messageText;
            ClickableSpan[] var3 = (ClickableSpan[])var2.getSpans(0, var2.length(), ClickableSpan.class);
            return var3.length <= var1 ? null : var3[var1];
         }
      }

      public AccessibilityNodeInfo createAccessibilityNodeInfo(int var1) {
         int[] var2 = new int[]{0, 0};
         ChatMessageCell.this.getLocationOnScreen(var2);
         AccessibilityNodeInfo var3 = null;
         byte var4 = 0;
         int var7;
         int var8;
         StringBuilder var27;
         if (var1 == -1) {
            AccessibilityNodeInfo var26 = AccessibilityNodeInfo.obtain(ChatMessageCell.this);
            ChatMessageCell.this.onInitializeAccessibilityNodeInfo(var26);
            var27 = new StringBuilder();
            ChatMessageCell var12 = ChatMessageCell.this;
            if (var12.isChat && var12.currentUser != null && !ChatMessageCell.this.currentMessageObject.isOut()) {
               var27.append(UserObject.getUserName(ChatMessageCell.this.currentUser));
               var27.append('\n');
            }

            if (!TextUtils.isEmpty(ChatMessageCell.this.currentMessageObject.messageText)) {
               var27.append(ChatMessageCell.this.currentMessageObject.messageText);
            }

            if (ChatMessageCell.this.currentMessageObject.isMusic()) {
               var27.append("\n");
               var27.append(LocaleController.formatString("AccDescrMusicInfo", 2131558447, ChatMessageCell.this.currentMessageObject.getMusicAuthor(), ChatMessageCell.this.currentMessageObject.getMusicTitle()));
            } else if (ChatMessageCell.this.currentMessageObject.isVoice() || ChatMessageCell.this.currentMessageObject.isRoundVideo()) {
               var27.append(", ");
               var27.append(LocaleController.formatCallDuration(ChatMessageCell.this.currentMessageObject.getDuration()));
               if (ChatMessageCell.this.currentMessageObject.isContentUnread()) {
                  var27.append(", ");
                  var27.append(LocaleController.getString("AccDescrMsgNotPlayed", 2131558444));
               }
            }

            if (ChatMessageCell.this.lastPoll != null) {
               var27.append(", ");
               var27.append(ChatMessageCell.this.lastPoll.question);
               var27.append(", ");
               var27.append(LocaleController.getString("AnonymousPoll", 2131558633));
            }

            if (ChatMessageCell.this.currentMessageObject.messageOwner.media != null && !TextUtils.isEmpty(ChatMessageCell.this.currentMessageObject.caption)) {
               var27.append("\n");
               var27.append(ChatMessageCell.this.currentMessageObject.caption);
            }

            var27.append("\n");
            StringBuilder var13 = new StringBuilder();
            var13.append(LocaleController.getString("TodayAt", 2131560907));
            var13.append(" ");
            var13.append(ChatMessageCell.this.currentTimeString);
            String var14 = var13.toString();
            if (ChatMessageCell.this.currentMessageObject.isOut()) {
               var27.append(LocaleController.formatString("AccDescrSentDate", 2131558471, var14));
               var27.append(", ");
               if (ChatMessageCell.this.currentMessageObject.isUnread()) {
                  var1 = 2131558446;
                  var14 = "AccDescrMsgUnread";
               } else {
                  var1 = 2131558445;
                  var14 = "AccDescrMsgRead";
               }

               var27.append(LocaleController.getString(var14, var1));
            } else {
               var27.append(LocaleController.formatString("AccDescrReceivedDate", 2131558461, var14));
            }

            var26.setContentDescription(var27.toString());
            var26.setEnabled(true);
            if (VERSION.SDK_INT >= 19) {
               CollectionItemInfo var18 = var26.getCollectionItemInfo();
               if (var18 != null) {
                  var26.setCollectionItemInfo(CollectionItemInfo.obtain(var18.getRowIndex(), 1, 0, 1, false));
               }
            }

            if (VERSION.SDK_INT >= 21) {
               var26.addAction(new AccessibilityAction(2131230726, LocaleController.getString("AccActionMessageOptions", 2131558406)));
               var1 = ChatMessageCell.this.getIconForCurrentState();
               if (var1 != 0) {
                  if (var1 != 1) {
                     if (var1 != 2) {
                        if (var1 != 3) {
                           if (var1 != 5) {
                              var14 = var3;
                              if (ChatMessageCell.this.currentMessageObject.type == 16) {
                                 var14 = LocaleController.getString("CallAgain", 2131558870);
                              }
                           } else {
                              var14 = LocaleController.getString("AccActionOpenFile", 2131558407);
                           }
                        } else {
                           var14 = LocaleController.getString("AccActionCancelDownload", 2131558403);
                        }
                     } else {
                        var14 = LocaleController.getString("AccActionDownload", 2131558404);
                     }
                  } else {
                     var14 = LocaleController.getString("AccActionPause", 2131558408);
                  }
               } else {
                  var14 = LocaleController.getString("AccActionPlay", 2131558409);
               }

               var26.addAction(new AccessibilityAction(16, var14));
               var26.addAction(new AccessibilityAction(32, LocaleController.getString("AccActionEnterSelectionMode", 2131558405)));
               if (ChatMessageCell.this.getMiniIconForCurrentState() == 2) {
                  var26.addAction(new AccessibilityAction(2131230728, LocaleController.getString("AccActionDownload", 2131558404)));
               }
            } else {
               var26.addAction(16);
               var26.addAction(32);
            }

            if (ChatMessageCell.this.currentMessageObject.messageText instanceof Spannable) {
               Spannable var28 = (Spannable)ChatMessageCell.this.currentMessageObject.messageText;
               CharacterStyle[] var15 = (CharacterStyle[])var28.getSpans(0, var28.length(), ClickableSpan.class);
               var7 = var15.length;
               var1 = 0;

               for(var8 = 0; var1 < var7; ++var1) {
                  CharacterStyle var10000 = var15[var1];
                  var26.addChild(ChatMessageCell.this, var8 + 2000);
                  ++var8;
               }
            }

            Iterator var16 = ChatMessageCell.this.botButtons.iterator();

            for(var1 = 0; var16.hasNext(); ++var1) {
               ChatMessageCell.BotButton var29 = (ChatMessageCell.BotButton)var16.next();
               var26.addChild(ChatMessageCell.this, var1 + 1000);
            }

            var16 = ChatMessageCell.this.pollButtons.iterator();

            for(var1 = var4; var16.hasNext(); ++var1) {
               ChatMessageCell.PollButton var30 = (ChatMessageCell.PollButton)var16.next();
               var26.addChild(ChatMessageCell.this, var1 + 500);
            }

            if (ChatMessageCell.this.drawInstantView) {
               var26.addChild(ChatMessageCell.this, 499);
            }

            if (ChatMessageCell.this.drawShareButton) {
               var26.addChild(ChatMessageCell.this, 498);
            }

            if (ChatMessageCell.this.replyNameLayout != null) {
               var26.addChild(ChatMessageCell.this, 497);
            }

            if (ChatMessageCell.this.drawSelectionBackground || ChatMessageCell.this.getBackground() != null) {
               var26.setSelected(true);
            }

            return var26;
         } else {
            var3 = AccessibilityNodeInfo.obtain();
            var3.setSource(ChatMessageCell.this, var1);
            var3.setParent(ChatMessageCell.this);
            var3.setPackageName(ChatMessageCell.this.getContext().getPackageName());
            int var17;
            if (var1 >= 2000) {
               Spannable var6 = (Spannable)ChatMessageCell.this.currentMessageObject.messageText;
               ClickableSpan var5 = this.getLinkById(var1);
               if (var5 == null) {
                  return null;
               }

               var8 = var6.getSpanStart(var5);
               int var9 = var6.getSpanEnd(var5);
               var3.setText(var6.subSequence(var8, var9).toString());
               Iterator var22 = ChatMessageCell.this.currentMessageObject.textLayoutBlocks.iterator();

               while(var22.hasNext()) {
                  MessageObject.TextLayoutBlock var19 = (MessageObject.TextLayoutBlock)var22.next();
                  var7 = var19.textLayout.getText().length();
                  var17 = var19.charactersOffset;
                  if (var17 <= var8 && var7 + var17 >= var9) {
                     var19.textLayout.getSelectionPath(var8 - var17, var9 - var17, this.linkPath);
                     this.linkPath.computeBounds(this.rectF, true);
                     Rect var24 = this.rect;
                     RectF var10 = this.rectF;
                     var24.set((int)var10.left, (int)var10.top, (int)var10.right, (int)var10.bottom);
                     this.rect.offset(0, (int)var19.textYOffset);
                     this.rect.offset(ChatMessageCell.this.textX, ChatMessageCell.this.textY);
                     var3.setBoundsInParent(this.rect);
                     if (ChatMessageCell.this.accessibilityVirtualViewBounds.get(var1) == null) {
                        ChatMessageCell.this.accessibilityVirtualViewBounds.put(var1, new Rect(this.rect));
                     }

                     this.rect.offset(var2[0], var2[1]);
                     var3.setBoundsInScreen(this.rect);
                     break;
                  }
               }

               var3.setClassName("android.widget.TextView");
               var3.setEnabled(true);
               var3.setClickable(true);
               var3.setLongClickable(true);
               var3.addAction(16);
               var3.addAction(32);
            } else {
               float var11;
               if (var1 >= 1000) {
                  var8 = var1 - 1000;
                  if (var8 >= ChatMessageCell.this.botButtons.size()) {
                     return null;
                  }

                  ChatMessageCell.BotButton var20 = (ChatMessageCell.BotButton)ChatMessageCell.this.botButtons.get(var8);
                  var3.setText(var20.title.getText());
                  var3.setClassName("android.widget.Button");
                  var3.setEnabled(true);
                  var3.setClickable(true);
                  var3.addAction(16);
                  this.rect.set(var20.x, var20.y, var20.x + var20.width, var20.y + var20.height);
                  if (ChatMessageCell.this.currentMessageObject.isOutOwner()) {
                     var8 = ChatMessageCell.this.getMeasuredWidth() - ChatMessageCell.this.widthForButtons - AndroidUtilities.dp(10.0F);
                  } else {
                     var8 = ChatMessageCell.this.backgroundDrawableLeft;
                     if (ChatMessageCell.this.mediaBackground) {
                        var11 = 1.0F;
                     } else {
                        var11 = 7.0F;
                     }

                     var8 += AndroidUtilities.dp(var11);
                  }

                  this.rect.offset(var8, ChatMessageCell.this.layoutHeight);
                  var3.setBoundsInParent(this.rect);
                  if (ChatMessageCell.this.accessibilityVirtualViewBounds.get(var1) == null) {
                     ChatMessageCell.this.accessibilityVirtualViewBounds.put(var1, new Rect(this.rect));
                  }

                  this.rect.offset(var2[0], var2[1]);
                  var3.setBoundsInScreen(this.rect);
               } else if (var1 >= 500) {
                  var8 = var1 - 500;
                  if (var8 >= ChatMessageCell.this.pollButtons.size()) {
                     return null;
                  }

                  ChatMessageCell.PollButton var21 = (ChatMessageCell.PollButton)ChatMessageCell.this.pollButtons.get(var8);
                  var3.setText(var21.title.getText());
                  if (!ChatMessageCell.this.pollVoted) {
                     var3.setClassName("android.widget.Button");
                  } else {
                     var27 = new StringBuilder();
                     var27.append(var3.getText());
                     var27.append(", ");
                     var27.append(var21.percent);
                     var27.append("%");
                     var3.setText(var27.toString());
                  }

                  var3.setEnabled(true);
                  var3.addAction(16);
                  var17 = ChatMessageCell.this.backgroundWidth;
                  var8 = AndroidUtilities.dp(76.0F);
                  this.rect.set(var21.x, var21.y, var21.x + (var17 - var8), var21.y + var21.height);
                  var3.setBoundsInParent(this.rect);
                  if (ChatMessageCell.this.accessibilityVirtualViewBounds.get(var1) == null) {
                     ChatMessageCell.this.accessibilityVirtualViewBounds.put(var1, new Rect(this.rect));
                  }

                  this.rect.offset(var2[0], var2[1]);
                  var3.setBoundsInScreen(this.rect);
                  var3.setClickable(true);
               } else if (var1 == 499) {
                  var3.setClassName("android.widget.Button");
                  var3.setEnabled(true);
                  if (ChatMessageCell.this.instantViewLayout != null) {
                     var3.setText(ChatMessageCell.this.instantViewLayout.getText());
                  }

                  var3.addAction(16);
                  var7 = ChatMessageCell.this.photoImage.getImageX();
                  var17 = ChatMessageCell.this.getMeasuredHeight() - AndroidUtilities.dp(64.0F);
                  if (ChatMessageCell.this.currentMessageObject.isOutOwner()) {
                     var8 = ChatMessageCell.this.getMeasuredWidth() - ChatMessageCell.this.widthForButtons - AndroidUtilities.dp(10.0F);
                  } else {
                     var8 = ChatMessageCell.this.backgroundDrawableLeft;
                     if (ChatMessageCell.this.mediaBackground) {
                        var11 = 1.0F;
                     } else {
                        var11 = 7.0F;
                     }

                     var8 += AndroidUtilities.dp(var11);
                  }

                  this.rect.set(var7 + var8, var17, var7 + ChatMessageCell.this.instantWidth + var8, AndroidUtilities.dp(38.0F) + var17);
                  var3.setBoundsInParent(this.rect);
                  if (ChatMessageCell.this.accessibilityVirtualViewBounds.get(var1) == null || !((Rect)ChatMessageCell.this.accessibilityVirtualViewBounds.get(var1)).equals(this.rect)) {
                     ChatMessageCell.this.accessibilityVirtualViewBounds.put(var1, new Rect(this.rect));
                  }

                  this.rect.offset(var2[0], var2[1]);
                  var3.setBoundsInScreen(this.rect);
                  var3.setClickable(true);
               } else if (var1 == 498) {
                  var3.setClassName("android.widget.ImageButton");
                  var3.setEnabled(true);
                  ChatMessageCell var23 = ChatMessageCell.this;
                  if (var23.isOpenChatByShare(var23.currentMessageObject)) {
                     var3.setContentDescription(LocaleController.getString("AccDescrOpenChat", 2131558450));
                  } else {
                     var3.setContentDescription(LocaleController.getString("ShareFile", 2131560748));
                  }

                  var3.addAction(16);
                  this.rect.set(ChatMessageCell.this.shareStartX, ChatMessageCell.this.shareStartY, ChatMessageCell.this.shareStartX + AndroidUtilities.dp(40.0F), ChatMessageCell.this.shareStartY + AndroidUtilities.dp(32.0F));
                  var3.setBoundsInParent(this.rect);
                  if (ChatMessageCell.this.accessibilityVirtualViewBounds.get(var1) == null || !((Rect)ChatMessageCell.this.accessibilityVirtualViewBounds.get(var1)).equals(this.rect)) {
                     ChatMessageCell.this.accessibilityVirtualViewBounds.put(var1, new Rect(this.rect));
                  }

                  this.rect.offset(var2[0], var2[1]);
                  var3.setBoundsInScreen(this.rect);
                  var3.setClickable(true);
               } else if (var1 == 497) {
                  var3.setEnabled(true);
                  StringBuilder var25 = new StringBuilder();
                  var25.append(LocaleController.getString("Reply", 2131560565));
                  var25.append(", ");
                  if (ChatMessageCell.this.replyNameLayout != null) {
                     var25.append(ChatMessageCell.this.replyNameLayout.getText());
                     var25.append(", ");
                  }

                  if (ChatMessageCell.this.replyTextLayout != null) {
                     var25.append(ChatMessageCell.this.replyTextLayout.getText());
                  }

                  var3.setContentDescription(var25.toString());
                  var3.addAction(16);
                  this.rect.set(ChatMessageCell.this.replyStartX, ChatMessageCell.this.replyStartY, ChatMessageCell.this.replyStartX + Math.max(ChatMessageCell.this.replyNameWidth, ChatMessageCell.this.replyTextWidth), ChatMessageCell.this.replyStartY + AndroidUtilities.dp(35.0F));
                  var3.setBoundsInParent(this.rect);
                  if (ChatMessageCell.this.accessibilityVirtualViewBounds.get(var1) == null || !((Rect)ChatMessageCell.this.accessibilityVirtualViewBounds.get(var1)).equals(this.rect)) {
                     ChatMessageCell.this.accessibilityVirtualViewBounds.put(var1, new Rect(this.rect));
                  }

                  this.rect.offset(var2[0], var2[1]);
                  var3.setBoundsInScreen(this.rect);
                  var3.setClickable(true);
               }
            }

            var3.setFocusable(true);
            var3.setVisibleToUser(true);
            return var3;
         }
      }

      public boolean performAction(int var1, int var2, Bundle var3) {
         if (var1 == -1) {
            ChatMessageCell.this.performAccessibilityAction(var2, var3);
         } else if (var2 == 64) {
            ChatMessageCell.this.sendAccessibilityEventForVirtualView(var1, 32768);
         } else {
            ClickableSpan var5;
            if (var2 == 16) {
               if (var1 >= 2000) {
                  var5 = this.getLinkById(var1);
                  if (var5 != null) {
                     ChatMessageCell.this.delegate.didPressUrl(ChatMessageCell.this.currentMessageObject, var5, false);
                     ChatMessageCell.this.sendAccessibilityEventForVirtualView(var1, 1);
                  }
               } else if (var1 >= 1000) {
                  var2 = var1 - 1000;
                  if (var2 >= ChatMessageCell.this.botButtons.size()) {
                     return false;
                  }

                  ChatMessageCell.BotButton var6 = (ChatMessageCell.BotButton)ChatMessageCell.this.botButtons.get(var2);
                  if (ChatMessageCell.this.delegate != null) {
                     ChatMessageCell.this.delegate.didPressBotButton(ChatMessageCell.this, var6.button);
                  }

                  ChatMessageCell.this.sendAccessibilityEventForVirtualView(var1, 1);
               } else if (var1 >= 500) {
                  var2 = var1 - 500;
                  if (var2 >= ChatMessageCell.this.pollButtons.size()) {
                     return false;
                  }

                  ChatMessageCell.PollButton var7 = (ChatMessageCell.PollButton)ChatMessageCell.this.pollButtons.get(var2);
                  if (ChatMessageCell.this.delegate != null) {
                     ChatMessageCell.this.delegate.didPressVoteButton(ChatMessageCell.this, var7.answer);
                  }

                  ChatMessageCell.this.sendAccessibilityEventForVirtualView(var1, 1);
               } else {
                  ChatMessageCell var4;
                  ChatMessageCell.ChatMessageCellDelegate var8;
                  if (var1 == 499) {
                     if (ChatMessageCell.this.delegate != null) {
                        var8 = ChatMessageCell.this.delegate;
                        var4 = ChatMessageCell.this;
                        var8.didPressInstantButton(var4, var4.drawInstantViewType);
                     }
                  } else if (var1 == 498) {
                     if (ChatMessageCell.this.delegate != null) {
                        ChatMessageCell.this.delegate.didPressShare(ChatMessageCell.this);
                     }
                  } else if (var1 == 497 && ChatMessageCell.this.delegate != null) {
                     var8 = ChatMessageCell.this.delegate;
                     var4 = ChatMessageCell.this;
                     var8.didPressReplyMessage(var4, var4.currentMessageObject.messageOwner.reply_to_msg_id);
                  }
               }
            } else if (var2 == 32) {
               var5 = this.getLinkById(var1);
               if (var5 != null) {
                  ChatMessageCell.this.delegate.didPressUrl(ChatMessageCell.this.currentMessageObject, var5, true);
                  ChatMessageCell.this.sendAccessibilityEventForVirtualView(var1, 2);
               }
            }
         }

         return true;
      }
   }

   private class PollButton {
      private TLRPC.TL_pollAnswer answer;
      private float decimal;
      private int height;
      private int percent;
      private float percentProgress;
      private int prevPercent;
      private float prevPercentProgress;
      private StaticLayout title;
      private int x;
      private int y;

      private PollButton() {
      }

      // $FF: synthetic method
      PollButton(Object var2) {
         this();
      }
   }
}
