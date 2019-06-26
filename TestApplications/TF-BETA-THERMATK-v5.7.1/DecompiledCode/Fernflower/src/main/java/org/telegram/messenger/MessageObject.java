package org.telegram.messenger;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.Spannable.Factory;
import android.text.StaticLayout.Builder;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.SparseArray;
import java.io.File;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanBotCommand;
import org.telegram.ui.Components.URLSpanBrowser;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanNoUnderlineBold;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;

public class MessageObject {
   private static final int LINES_PER_BLOCK = 10;
   public static final int MESSAGE_SEND_STATE_EDITING = 3;
   public static final int MESSAGE_SEND_STATE_SENDING = 1;
   public static final int MESSAGE_SEND_STATE_SEND_ERROR = 2;
   public static final int MESSAGE_SEND_STATE_SENT = 0;
   public static final int POSITION_FLAG_BOTTOM = 8;
   public static final int POSITION_FLAG_LEFT = 1;
   public static final int POSITION_FLAG_RIGHT = 2;
   public static final int POSITION_FLAG_TOP = 4;
   public static final int TYPE_ANIMATED_STICKER = 15;
   public static final int TYPE_POLL = 17;
   public static final int TYPE_ROUND_VIDEO = 5;
   public static final int TYPE_STICKER = 13;
   static final String[] excludeWords = new String[]{" vs. ", " vs ", " versus ", " ft. ", " ft ", " featuring ", " feat. ", " feat ", " presents ", " pres. ", " pres ", " and ", " & ", " . "};
   public static Pattern instagramUrlPattern;
   public static Pattern urlPattern;
   public boolean attachPathExists;
   public int audioPlayerDuration;
   public float audioProgress;
   public int audioProgressMs;
   public int audioProgressSec;
   public StringBuilder botButtonsLayout;
   public float bufferedProgress;
   public boolean cancelEditing;
   public CharSequence caption;
   public int contentType;
   public int currentAccount;
   public TLRPC.TL_channelAdminLogEvent currentEvent;
   public String customReplyName;
   public String dateKey;
   public boolean deleted;
   public CharSequence editingMessage;
   public ArrayList editingMessageEntities;
   private int emojiOnlyCount;
   public long eventId;
   public boolean forceUpdate;
   private float generatedWithDensity;
   private int generatedWithMinSize;
   public float gifState;
   public boolean hadAnimationNotReadyLoading;
   public boolean hasRtl;
   public boolean isDateObject;
   private int isRoundVideoCached;
   public int lastLineWidth;
   private boolean layoutCreated;
   public int linesCount;
   public CharSequence linkDescription;
   public boolean localChannel;
   public boolean localEdit;
   public long localGroupId;
   public String localName;
   public long localSentGroupId;
   public int localType;
   public String localUserName;
   public boolean mediaExists;
   public TLRPC.Message messageOwner;
   public CharSequence messageText;
   public String monthKey;
   public ArrayList photoThumbs;
   public ArrayList photoThumbs2;
   public TLObject photoThumbsObject;
   public TLObject photoThumbsObject2;
   public long pollLastCheckTime;
   public boolean pollVisibleOnScreen;
   public String previousAttachPath;
   public String previousCaption;
   public ArrayList previousCaptionEntities;
   public TLRPC.MessageMedia previousMedia;
   public MessageObject replyMessageObject;
   public boolean resendAsIs;
   public int textHeight;
   public ArrayList textLayoutBlocks;
   public int textWidth;
   public float textXOffset;
   public int type;
   public boolean useCustomPhoto;
   public CharSequence vCardData;
   public VideoEditedInfo videoEditedInfo;
   public boolean viewsReloaded;
   public int wantedBotKeyboardWidth;

   public MessageObject(int var1, TLRPC.Message var2, SparseArray var3, SparseArray var4, boolean var5) {
      this(var1, var2, (AbstractMap)null, (AbstractMap)null, var3, var4, var5, 0L);
   }

   public MessageObject(int var1, TLRPC.Message var2, SparseArray var3, boolean var4) {
      this(var1, var2, (SparseArray)var3, (SparseArray)null, var4);
   }

   public MessageObject(int var1, TLRPC.Message var2, String var3, String var4, String var5, boolean var6, boolean var7, boolean var8) {
      this.type = 1000;
      byte var9;
      if (var6) {
         var9 = 2;
      } else {
         var9 = 1;
      }

      this.localType = var9;
      this.currentAccount = var1;
      this.localName = var4;
      this.localUserName = var5;
      this.messageText = var3;
      this.messageOwner = var2;
      this.localChannel = var7;
      this.localEdit = var8;
   }

   public MessageObject(int var1, TLRPC.Message var2, AbstractMap var3, AbstractMap var4, SparseArray var5, SparseArray var6, boolean var7, long var8) {
      this.type = 1000;
      Theme.createChatResources((Context)null, true);
      this.currentAccount = var1;
      this.messageOwner = var2;
      this.eventId = var8;
      TLRPC.Message var10 = var2.replyMessage;
      if (var10 != null) {
         this.replyMessageObject = new MessageObject(var1, var10, var3, var4, var5, var6, false, var8);
      }

      int var11 = var2.from_id;
      TLRPC.User var12;
      TLRPC.User var33;
      if (var11 > 0) {
         if (var3 != null) {
            var12 = (TLRPC.User)var3.get(var11);
         } else if (var5 != null) {
            var12 = (TLRPC.User)var5.get(var11);
         } else {
            var12 = null;
         }

         var33 = var12;
         if (var12 == null) {
            var33 = MessagesController.getInstance(var1).getUser(var2.from_id);
         }
      } else {
         var33 = null;
      }

      int var13;
      boolean var14;
      TLRPC.User var20;
      String var22;
      if (var2 instanceof TLRPC.TL_messageService) {
         label533: {
            TLRPC.MessageAction var37 = var2.action;
            if (var37 != null) {
               if (var37 instanceof TLRPC.TL_messageActionCustomAction) {
                  this.messageText = var37.message;
               } else if (var37 instanceof TLRPC.TL_messageActionChatCreate) {
                  if (this.isOut()) {
                     this.messageText = LocaleController.getString("ActionYouCreateGroup", 2131558549);
                  } else {
                     this.messageText = this.replaceWithLink(LocaleController.getString("ActionCreateGroup", 2131558517), "un1", var33);
                  }
               } else {
                  TLRPC.User var18;
                  if (var37 instanceof TLRPC.TL_messageActionChatDeleteUser) {
                     var11 = var37.user_id;
                     if (var11 == var2.from_id) {
                        if (this.isOut()) {
                           this.messageText = LocaleController.getString("ActionYouLeftUser", 2131558551);
                        } else {
                           this.messageText = this.replaceWithLink(LocaleController.getString("ActionLeftUser", 2131558523), "un1", var33);
                        }
                     } else {
                        if (var3 != null) {
                           var18 = (TLRPC.User)var3.get(var11);
                        } else if (var5 != null) {
                           var18 = (TLRPC.User)var5.get(var11);
                        } else {
                           var18 = null;
                        }

                        var20 = var18;
                        if (var18 == null) {
                           var20 = MessagesController.getInstance(var1).getUser(var2.action.user_id);
                        }

                        if (this.isOut()) {
                           this.messageText = this.replaceWithLink(LocaleController.getString("ActionYouKickUser", 2131558550), "un2", var20);
                        } else if (var2.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                           this.messageText = this.replaceWithLink(LocaleController.getString("ActionKickUserYou", 2131558522), "un1", var33);
                        } else {
                           this.messageText = this.replaceWithLink(LocaleController.getString("ActionKickUser", 2131558521), "un2", var20);
                           this.messageText = this.replaceWithLink(this.messageText, "un1", var33);
                        }
                     }
                  } else {
                     if (!(var37 instanceof TLRPC.TL_messageActionChatAddUser)) {
                        var14 = var7;
                        if (var37 instanceof TLRPC.TL_messageActionChatJoinedByLink) {
                           if (this.isOut()) {
                              this.messageText = LocaleController.getString("ActionInviteYou", 2131558520);
                              var7 = var7;
                              var20 = var33;
                           } else {
                              this.messageText = this.replaceWithLink(LocaleController.getString("ActionInviteUser", 2131558519), "un1", var33);
                              var7 = var7;
                              var20 = var33;
                           }
                        } else if (var37 instanceof TLRPC.TL_messageActionChatEditPhoto) {
                           if (var2.to_id.channel_id != 0 && !this.isMegagroup()) {
                              this.messageText = LocaleController.getString("ActionChannelChangedPhoto", 2131558513);
                              var7 = var7;
                              var20 = var33;
                           } else if (this.isOut()) {
                              this.messageText = LocaleController.getString("ActionYouChangedPhoto", 2131558547);
                              var7 = var7;
                              var20 = var33;
                           } else {
                              this.messageText = this.replaceWithLink(LocaleController.getString("ActionChangedPhoto", 2131558511), "un1", var33);
                              var7 = var7;
                              var20 = var33;
                           }
                        } else if (var37 instanceof TLRPC.TL_messageActionChatEditTitle) {
                           if (var2.to_id.channel_id != 0 && !this.isMegagroup()) {
                              this.messageText = LocaleController.getString("ActionChannelChangedTitle", 2131558514).replace("un2", var2.action.title);
                              var7 = var7;
                              var20 = var33;
                           } else if (this.isOut()) {
                              this.messageText = LocaleController.getString("ActionYouChangedTitle", 2131558548).replace("un2", var2.action.title);
                              var7 = var7;
                              var20 = var33;
                           } else {
                              this.messageText = this.replaceWithLink(LocaleController.getString("ActionChangedTitle", 2131558512).replace("un2", var2.action.title), "un1", var33);
                              var7 = var7;
                              var20 = var33;
                           }
                        } else if (var37 instanceof TLRPC.TL_messageActionChatDeletePhoto) {
                           if (var2.to_id.channel_id != 0 && !this.isMegagroup()) {
                              this.messageText = LocaleController.getString("ActionChannelRemovedPhoto", 2131558515);
                              var7 = var7;
                              var20 = var33;
                           } else if (this.isOut()) {
                              this.messageText = LocaleController.getString("ActionYouRemovedPhoto", 2131558552);
                              var7 = var7;
                              var20 = var33;
                           } else {
                              this.messageText = this.replaceWithLink(LocaleController.getString("ActionRemovedPhoto", 2131558541), "un1", var33);
                              var7 = var7;
                              var20 = var33;
                           }
                        } else if (var37 instanceof TLRPC.TL_messageActionTTLChange) {
                           if (var37.ttl != 0) {
                              if (this.isOut()) {
                                 this.messageText = LocaleController.formatString("MessageLifetimeChangedOutgoing", 2131559848, LocaleController.formatTTLString(var2.action.ttl));
                                 var7 = var7;
                                 var20 = var33;
                              } else {
                                 this.messageText = LocaleController.formatString("MessageLifetimeChanged", 2131559847, UserObject.getFirstName(var33), LocaleController.formatTTLString(var2.action.ttl));
                                 var7 = var7;
                                 var20 = var33;
                              }
                           } else if (this.isOut()) {
                              this.messageText = LocaleController.getString("MessageLifetimeYouRemoved", 2131559852);
                              var7 = var7;
                              var20 = var33;
                           } else {
                              this.messageText = LocaleController.formatString("MessageLifetimeRemoved", 2131559850, UserObject.getFirstName(var33));
                              var7 = var7;
                              var20 = var33;
                           }
                        } else {
                           String var26;
                           StringBuilder var36;
                           if (var37 instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
                              var8 = (long)var2.date * 1000L;
                              String var25;
                              if (LocaleController.getInstance().formatterDay != null && LocaleController.getInstance().formatterYear != null) {
                                 var25 = LocaleController.formatString("formatDateAtTime", 2131561210, LocaleController.getInstance().formatterYear.format(var8), LocaleController.getInstance().formatterDay.format(var8));
                              } else {
                                 var36 = new StringBuilder();
                                 var36.append("");
                                 var36.append(var2.date);
                                 var25 = var36.toString();
                              }

                              var12 = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                              var20 = var12;
                              if (var12 == null) {
                                 if (var3 != null) {
                                    var18 = (TLRPC.User)var3.get(this.messageOwner.to_id.user_id);
                                 } else {
                                    var18 = var12;
                                    if (var5 != null) {
                                       var18 = (TLRPC.User)var5.get(this.messageOwner.to_id.user_id);
                                    }
                                 }

                                 var20 = var18;
                                 if (var18 == null) {
                                    var20 = MessagesController.getInstance(var1).getUser(this.messageOwner.to_id.user_id);
                                 }
                              }

                              if (var20 != null) {
                                 var26 = UserObject.getFirstName(var20);
                              } else {
                                 var26 = "";
                              }

                              TLRPC.MessageAction var15 = var2.action;
                              this.messageText = LocaleController.formatString("NotificationUnrecognizedDevice", 2131560054, var26, var25, var15.title, var15.address);
                              var7 = var7;
                              var20 = var33;
                           } else if (!(var37 instanceof TLRPC.TL_messageActionUserJoined) && !(var37 instanceof TLRPC.TL_messageActionContactSignUp)) {
                              if (var37 instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                                 this.messageText = LocaleController.formatString("NotificationContactNewPhoto", 2131559998, UserObject.getUserName(var33));
                                 var7 = var7;
                                 var20 = var33;
                              } else if (var37 instanceof TLRPC.TL_messageEncryptedAction) {
                                 TLRPC.DecryptedMessageAction var16 = var37.encryptedAction;
                                 if (var16 instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) {
                                    if (this.isOut()) {
                                       this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", 2131558543);
                                       var7 = var7;
                                       var20 = var33;
                                    } else {
                                       this.messageText = this.replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", 2131558542), "un1", var33);
                                       var7 = var7;
                                       var20 = var33;
                                    }
                                 } else {
                                    var7 = var7;
                                    var20 = var33;
                                    if (var16 instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
                                       TLRPC.TL_decryptedMessageActionSetMessageTTL var17 = (TLRPC.TL_decryptedMessageActionSetMessageTTL)var16;
                                       if (var17.ttl_seconds != 0) {
                                          if (this.isOut()) {
                                             this.messageText = LocaleController.formatString("MessageLifetimeChangedOutgoing", 2131559848, LocaleController.formatTTLString(var17.ttl_seconds));
                                             var7 = var14;
                                             var20 = var33;
                                          } else {
                                             this.messageText = LocaleController.formatString("MessageLifetimeChanged", 2131559847, UserObject.getFirstName(var33), LocaleController.formatTTLString(var17.ttl_seconds));
                                             var7 = var14;
                                             var20 = var33;
                                          }
                                       } else if (this.isOut()) {
                                          this.messageText = LocaleController.getString("MessageLifetimeYouRemoved", 2131559852);
                                          var7 = var14;
                                          var20 = var33;
                                       } else {
                                          this.messageText = LocaleController.formatString("MessageLifetimeRemoved", 2131559850, UserObject.getFirstName(var33));
                                          var7 = var14;
                                          var20 = var33;
                                       }
                                    }
                                 }
                              } else if (var37 instanceof TLRPC.TL_messageActionScreenshotTaken) {
                                 if (this.isOut()) {
                                    this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", 2131558543);
                                    var7 = var7;
                                    var20 = var33;
                                 } else {
                                    this.messageText = this.replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", 2131558542), "un1", var33);
                                    var7 = var7;
                                    var20 = var33;
                                 }
                              } else if (var37 instanceof TLRPC.TL_messageActionCreatedBroadcastList) {
                                 this.messageText = LocaleController.formatString("YouCreatedBroadcastList", 2131561138);
                                 var7 = var7;
                                 var20 = var33;
                              } else if (var37 instanceof TLRPC.TL_messageActionChannelCreate) {
                                 if (this.isMegagroup()) {
                                    this.messageText = LocaleController.getString("ActionCreateMega", 2131558518);
                                    var7 = var7;
                                    var20 = var33;
                                 } else {
                                    this.messageText = LocaleController.getString("ActionCreateChannel", 2131558516);
                                    var7 = var7;
                                    var20 = var33;
                                 }
                              } else if (var37 instanceof TLRPC.TL_messageActionChatMigrateTo) {
                                 this.messageText = LocaleController.getString("ActionMigrateFromGroup", 2131558524);
                                 var7 = var7;
                                 var20 = var33;
                              } else if (var37 instanceof TLRPC.TL_messageActionChannelMigrateFrom) {
                                 this.messageText = LocaleController.getString("ActionMigrateFromGroup", 2131558524);
                                 var7 = var7;
                                 var20 = var33;
                              } else if (var37 instanceof TLRPC.TL_messageActionPinMessage) {
                                 TLRPC.Chat var19;
                                 label482: {
                                    if (var33 == null) {
                                       if (var4 != null) {
                                          var19 = (TLRPC.Chat)var4.get(var2.to_id.channel_id);
                                          break label482;
                                       }

                                       if (var6 != null) {
                                          var19 = (TLRPC.Chat)var6.get(var2.to_id.channel_id);
                                          break label482;
                                       }
                                    }

                                    var19 = null;
                                 }

                                 this.generatePinMessageText(var33, var19);
                                 var7 = var7;
                                 var20 = var33;
                              } else if (var37 instanceof TLRPC.TL_messageActionHistoryClear) {
                                 this.messageText = LocaleController.getString("HistoryCleared", 2131559639);
                                 var7 = var7;
                                 var20 = var33;
                              } else if (var37 instanceof TLRPC.TL_messageActionGameScore) {
                                 this.generateGameMessageText(var33);
                                 var7 = var7;
                                 var20 = var33;
                              } else {
                                 SpannableString var39;
                                 if (var37 instanceof TLRPC.TL_messageActionPhoneCall) {
                                    TLRPC.Message var29 = this.messageOwner;
                                    TLRPC.TL_messageActionPhoneCall var21 = (TLRPC.TL_messageActionPhoneCall)var29.action;
                                    var7 = var21.reason instanceof TLRPC.TL_phoneCallDiscardReasonMissed;
                                    if (var29.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                       if (var7) {
                                          this.messageText = LocaleController.getString("CallMessageOutgoingMissed", 2131558877);
                                       } else {
                                          this.messageText = LocaleController.getString("CallMessageOutgoing", 2131558876);
                                       }
                                    } else if (var7) {
                                       this.messageText = LocaleController.getString("CallMessageIncomingMissed", 2131558875);
                                    } else if (var21.reason instanceof TLRPC.TL_phoneCallDiscardReasonBusy) {
                                       this.messageText = LocaleController.getString("CallMessageIncomingDeclined", 2131558874);
                                    } else {
                                       this.messageText = LocaleController.getString("CallMessageIncoming", 2131558873);
                                    }

                                    var1 = var21.duration;
                                    var7 = var14;
                                    var20 = var33;
                                    if (var1 > 0) {
                                       var22 = LocaleController.formatCallDuration(var1);
                                       this.messageText = LocaleController.formatString("CallMessageWithDuration", 2131558879, this.messageText, var22);
                                       var26 = this.messageText.toString();
                                       var11 = var26.indexOf(var22);
                                       var7 = var14;
                                       var20 = var33;
                                       if (var11 != -1) {
                                          var39 = new SpannableString(this.messageText);
                                          var13 = var22.length() + var11;
                                          var1 = var11;
                                          if (var11 > 0) {
                                             var1 = var11;
                                             if (var26.charAt(var11 - 1) == '(') {
                                                var1 = var11 - 1;
                                             }
                                          }

                                          var11 = var13;
                                          if (var13 < var26.length()) {
                                             var11 = var13;
                                             if (var26.charAt(var13) == ')') {
                                                var11 = var13 + 1;
                                             }
                                          }

                                          var39.setSpan(new TypefaceSpan(Typeface.DEFAULT), var1, var11, 0);
                                          this.messageText = var39;
                                          var7 = var14;
                                          var20 = var33;
                                       }
                                    }
                                 } else {
                                    TLRPC.User var23;
                                    if (var37 instanceof TLRPC.TL_messageActionPaymentSent) {
                                       var11 = (int)this.getDialogId();
                                       if (var3 != null) {
                                          var33 = (TLRPC.User)var3.get(var11);
                                       } else if (var5 != null) {
                                          var33 = (TLRPC.User)var5.get(var11);
                                       }

                                       var23 = var33;
                                       if (var33 == null) {
                                          var23 = MessagesController.getInstance(var1).getUser(var11);
                                       }

                                       this.generatePaymentSentMessageText((TLRPC.User)null);
                                       var7 = var7;
                                       var20 = var23;
                                    } else if (var37 instanceof TLRPC.TL_messageActionBotAllowed) {
                                       var22 = ((TLRPC.TL_messageActionBotAllowed)var37).domain;
                                       var26 = LocaleController.getString("ActionBotAllowed", 2131558496);
                                       var1 = var26.indexOf("%1$s");
                                       var39 = new SpannableString(String.format(var26, var22));
                                       if (var1 >= 0) {
                                          StringBuilder var32 = new StringBuilder();
                                          var32.append("http://");
                                          var32.append(var22);
                                          var39.setSpan(new URLSpanNoUnderlineBold(var32.toString()), var1, var22.length() + var1, 33);
                                       }

                                       this.messageText = var39;
                                       var7 = var7;
                                       var20 = var33;
                                    } else {
                                       var7 = var7;
                                       var20 = var33;
                                       if (var37 instanceof TLRPC.TL_messageActionSecureValuesSent) {
                                          TLRPC.TL_messageActionSecureValuesSent var38 = (TLRPC.TL_messageActionSecureValuesSent)var37;
                                          var36 = new StringBuilder();
                                          var13 = var38.types.size();

                                          for(var11 = 0; var11 < var13; ++var11) {
                                             TLRPC.SecureValueType var27 = (TLRPC.SecureValueType)var38.types.get(var11);
                                             if (var36.length() > 0) {
                                                var36.append(", ");
                                             }

                                             if (var27 instanceof TLRPC.TL_secureValueTypePhone) {
                                                var36.append(LocaleController.getString("ActionBotDocumentPhone", 2131558506));
                                             } else if (var27 instanceof TLRPC.TL_secureValueTypeEmail) {
                                                var36.append(LocaleController.getString("ActionBotDocumentEmail", 2131558500));
                                             } else if (var27 instanceof TLRPC.TL_secureValueTypeAddress) {
                                                var36.append(LocaleController.getString("ActionBotDocumentAddress", 2131558497));
                                             } else if (var27 instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                                                var36.append(LocaleController.getString("ActionBotDocumentIdentity", 2131558501));
                                             } else if (var27 instanceof TLRPC.TL_secureValueTypePassport) {
                                                var36.append(LocaleController.getString("ActionBotDocumentPassport", 2131558504));
                                             } else if (var27 instanceof TLRPC.TL_secureValueTypeDriverLicense) {
                                                var36.append(LocaleController.getString("ActionBotDocumentDriverLicence", 2131558499));
                                             } else if (var27 instanceof TLRPC.TL_secureValueTypeIdentityCard) {
                                                var36.append(LocaleController.getString("ActionBotDocumentIdentityCard", 2131558502));
                                             } else if (var27 instanceof TLRPC.TL_secureValueTypeUtilityBill) {
                                                var36.append(LocaleController.getString("ActionBotDocumentUtilityBill", 2131558509));
                                             } else if (var27 instanceof TLRPC.TL_secureValueTypeBankStatement) {
                                                var36.append(LocaleController.getString("ActionBotDocumentBankStatement", 2131558498));
                                             } else if (var27 instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
                                                var36.append(LocaleController.getString("ActionBotDocumentRentalAgreement", 2131558507));
                                             } else if (var27 instanceof TLRPC.TL_secureValueTypeInternalPassport) {
                                                var36.append(LocaleController.getString("ActionBotDocumentInternalPassport", 2131558503));
                                             } else if (var27 instanceof TLRPC.TL_secureValueTypePassportRegistration) {
                                                var36.append(LocaleController.getString("ActionBotDocumentPassportRegistration", 2131558505));
                                             } else if (var27 instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
                                                var36.append(LocaleController.getString("ActionBotDocumentTemporaryRegistration", 2131558508));
                                             }
                                          }

                                          TLRPC.Peer var28 = var2.to_id;
                                          if (var28 != null) {
                                             if (var3 != null) {
                                                var18 = (TLRPC.User)var3.get(var28.user_id);
                                             } else if (var5 != null) {
                                                var18 = (TLRPC.User)var5.get(var28.user_id);
                                             } else {
                                                var18 = null;
                                             }

                                             if (var18 == null) {
                                                var23 = MessagesController.getInstance(var1).getUser(var2.to_id.user_id);
                                             } else {
                                                var23 = var18;
                                             }
                                          } else {
                                             var23 = null;
                                          }

                                          this.messageText = LocaleController.formatString("ActionBotDocuments", 2131558510, UserObject.getFirstName(var23), var36.toString());
                                          var7 = var14;
                                          var20 = var33;
                                       }
                                    }
                                 }
                              }
                           } else {
                              this.messageText = LocaleController.formatString("NotificationContactJoined", 2131559997, UserObject.getUserName(var33));
                              var7 = var7;
                              var20 = var33;
                           }
                        }
                        break label533;
                     }

                     TLRPC.MessageAction var24 = this.messageOwner.action;
                     var13 = var24.user_id;
                     var11 = var13;
                     if (var13 == 0) {
                        var11 = var13;
                        if (var24.users.size() == 1) {
                           var11 = (Integer)this.messageOwner.action.users.get(0);
                        }
                     }

                     if (var11 != 0) {
                        if (var3 != null) {
                           var18 = (TLRPC.User)var3.get(var11);
                        } else if (var5 != null) {
                           var18 = (TLRPC.User)var5.get(var11);
                        } else {
                           var18 = null;
                        }

                        var20 = var18;
                        if (var18 == null) {
                           var20 = MessagesController.getInstance(var1).getUser(var11);
                        }

                        if (var11 == var2.from_id) {
                           if (var2.to_id.channel_id != 0 && !this.isMegagroup()) {
                              this.messageText = LocaleController.getString("ChannelJoined", 2131558956);
                           } else if (var2.to_id.channel_id != 0 && this.isMegagroup()) {
                              if (var11 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                 this.messageText = LocaleController.getString("ChannelMegaJoined", 2131558961);
                              } else {
                                 this.messageText = this.replaceWithLink(LocaleController.getString("ActionAddUserSelfMega", 2131558493), "un1", var33);
                              }
                           } else if (this.isOut()) {
                              this.messageText = LocaleController.getString("ActionAddUserSelfYou", 2131558494);
                           } else {
                              this.messageText = this.replaceWithLink(LocaleController.getString("ActionAddUserSelf", 2131558492), "un1", var33);
                           }
                        } else if (this.isOut()) {
                           this.messageText = this.replaceWithLink(LocaleController.getString("ActionYouAddUser", 2131558546), "un2", var20);
                        } else if (var11 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                           if (var2.to_id.channel_id != 0) {
                              if (this.isMegagroup()) {
                                 this.messageText = this.replaceWithLink(LocaleController.getString("MegaAddedBy", 2131559825), "un1", var33);
                              } else {
                                 this.messageText = this.replaceWithLink(LocaleController.getString("ChannelAddedBy", 2131558924), "un1", var33);
                              }
                           } else {
                              this.messageText = this.replaceWithLink(LocaleController.getString("ActionAddUserYou", 2131558495), "un1", var33);
                           }
                        } else {
                           this.messageText = this.replaceWithLink(LocaleController.getString("ActionAddUser", 2131558491), "un2", var20);
                           this.messageText = this.replaceWithLink(this.messageText, "un1", var33);
                        }
                     } else if (this.isOut()) {
                        this.messageText = this.replaceWithLink(LocaleController.getString("ActionYouAddUser", 2131558546), "un2", var2.action.users, var3, var5);
                     } else {
                        this.messageText = this.replaceWithLink(LocaleController.getString("ActionAddUser", 2131558491), "un2", var2.action.users, var3, var5);
                        this.messageText = this.replaceWithLink(this.messageText, "un1", var33);
                     }
                  }
               }
            }

            var20 = var33;
         }
      } else {
         var14 = var7;
         if (!this.isMediaEmpty()) {
            TLRPC.MessageMedia var34 = var2.media;
            if (var34 instanceof TLRPC.TL_messageMediaPoll) {
               this.messageText = LocaleController.getString("Poll", 2131560467);
               var7 = var7;
               var20 = var33;
            } else if (var34 instanceof TLRPC.TL_messageMediaPhoto) {
               if (var34.ttl_seconds != 0 && !(var2 instanceof TLRPC.TL_message_secret)) {
                  this.messageText = LocaleController.getString("AttachDestructingPhoto", 2131558712);
                  var7 = var7;
                  var20 = var33;
               } else {
                  this.messageText = LocaleController.getString("AttachPhoto", 2131558727);
                  var7 = var7;
                  var20 = var33;
               }
            } else {
               label554: {
                  if (!this.isVideo()) {
                     var34 = var2.media;
                     if (!(var34 instanceof TLRPC.TL_messageMediaDocument) || !(var34.document instanceof TLRPC.TL_documentEmpty) || var34.ttl_seconds == 0) {
                        if (this.isVoice()) {
                           this.messageText = LocaleController.getString("AttachAudio", 2131558709);
                           var7 = var7;
                           var20 = var33;
                        } else if (this.isRoundVideo()) {
                           this.messageText = LocaleController.getString("AttachRound", 2131558729);
                           var7 = var7;
                           var20 = var33;
                        } else {
                           var34 = var2.media;
                           if (!(var34 instanceof TLRPC.TL_messageMediaGeo) && !(var34 instanceof TLRPC.TL_messageMediaVenue)) {
                              if (var34 instanceof TLRPC.TL_messageMediaGeoLive) {
                                 this.messageText = LocaleController.getString("AttachLiveLocation", 2131558721);
                                 var7 = var7;
                                 var20 = var33;
                              } else if (var34 instanceof TLRPC.TL_messageMediaContact) {
                                 this.messageText = LocaleController.getString("AttachContact", 2131558711);
                                 var7 = var7;
                                 var20 = var33;
                                 if (!TextUtils.isEmpty(var2.media.vcard)) {
                                    this.vCardData = MessageObject.VCardData.parse(var2.media.vcard);
                                    var7 = var14;
                                    var20 = var33;
                                 }
                              } else if (var34 instanceof TLRPC.TL_messageMediaGame) {
                                 this.messageText = var2.message;
                                 var7 = var7;
                                 var20 = var33;
                              } else if (var34 instanceof TLRPC.TL_messageMediaInvoice) {
                                 this.messageText = var34.description;
                                 var7 = var7;
                                 var20 = var33;
                              } else if (var34 instanceof TLRPC.TL_messageMediaUnsupported) {
                                 this.messageText = LocaleController.getString("UnsupportedMedia", 2131560947).replace("https://telegram.org/update", "https://github.com/Telegram-FOSS-Team/Telegram-FOSS/blob/master/Update.md");
                                 var7 = var7;
                                 var20 = var33;
                              } else {
                                 var7 = var7;
                                 var20 = var33;
                                 if (var34 instanceof TLRPC.TL_messageMediaDocument) {
                                    if (!this.isSticker() && !this.isAnimatedSticker()) {
                                       if (this.isMusic()) {
                                          this.messageText = LocaleController.getString("AttachMusic", 2131558726);
                                          var7 = var14;
                                          var20 = var33;
                                       } else if (this.isGif()) {
                                          this.messageText = LocaleController.getString("AttachGif", 2131558716);
                                          var7 = var14;
                                          var20 = var33;
                                       } else {
                                          var22 = FileLoader.getDocumentFileName(var2.media.document);
                                          if (var22 != null && var22.length() > 0) {
                                             this.messageText = var22;
                                             var7 = var14;
                                             var20 = var33;
                                          } else {
                                             this.messageText = LocaleController.getString("AttachDocument", 2131558714);
                                             var7 = var14;
                                             var20 = var33;
                                          }
                                       }
                                    } else {
                                       var22 = this.getStrickerChar();
                                       if (var22 != null && var22.length() > 0) {
                                          this.messageText = String.format("%s %s", var22, LocaleController.getString("AttachSticker", 2131558730));
                                          var7 = var14;
                                          var20 = var33;
                                       } else {
                                          this.messageText = LocaleController.getString("AttachSticker", 2131558730);
                                          var7 = var14;
                                          var20 = var33;
                                       }
                                    }
                                 }
                              }
                           } else {
                              this.messageText = LocaleController.getString("AttachLocation", 2131558723);
                              var7 = var7;
                              var20 = var33;
                           }
                        }
                        break label554;
                     }
                  }

                  if (var2.media.ttl_seconds != 0 && !(var2 instanceof TLRPC.TL_message_secret)) {
                     this.messageText = LocaleController.getString("AttachDestructingVideo", 2131558713);
                     var7 = var7;
                     var20 = var33;
                  } else {
                     this.messageText = LocaleController.getString("AttachVideo", 2131558733);
                     var7 = var7;
                     var20 = var33;
                  }
               }
            }
         } else {
            this.messageText = var2.message;
            var20 = var33;
            var7 = var7;
         }
      }

      if (this.messageText == null) {
         this.messageText = "";
      }

      this.setType();
      this.measureInlineBotButtons();
      GregorianCalendar var30 = new GregorianCalendar();
      var30.setTimeInMillis((long)this.messageOwner.date * 1000L);
      var1 = var30.get(6);
      var13 = var30.get(1);
      var11 = var30.get(2);
      this.dateKey = String.format("%d_%02d_%02d", var13, var11, var1);
      this.monthKey = String.format("%d_%02d", var13, var11);
      this.createMessageSendInfo();
      this.generateCaption();
      if (var7) {
         TextPaint var31;
         if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
            var31 = Theme.chat_msgGameTextPaint;
         } else {
            var31 = Theme.chat_msgTextPaint;
         }

         int[] var35;
         if (SharedConfig.allowBigEmoji) {
            var35 = new int[1];
         } else {
            var35 = null;
         }

         this.messageText = Emoji.replaceEmoji(this.messageText, var31.getFontMetricsInt(), AndroidUtilities.dp(20.0F), false, var35);
         this.checkEmojiOnly(var35);
         this.generateLayout(var20);
      }

      this.layoutCreated = var7;
      this.generateThumbs(false);
      this.checkMediaExistance();
   }

   public MessageObject(int var1, TLRPC.Message var2, AbstractMap var3, AbstractMap var4, boolean var5) {
      this(var1, var2, var3, var4, var5, 0L);
   }

   public MessageObject(int var1, TLRPC.Message var2, AbstractMap var3, AbstractMap var4, boolean var5, long var6) {
      this(var1, var2, var3, var4, (SparseArray)null, (SparseArray)null, var5, var6);
   }

   public MessageObject(int var1, TLRPC.Message var2, AbstractMap var3, boolean var4) {
      this(var1, var2, (AbstractMap)var3, (AbstractMap)null, var4);
   }

   public MessageObject(int var1, TLRPC.Message var2, boolean var3) {
      this(var1, var2, (AbstractMap)null, (AbstractMap)null, (SparseArray)null, (SparseArray)null, var3, 0L);
   }

   public MessageObject(int var1, TLRPC.TL_channelAdminLogEvent var2, ArrayList var3, HashMap var4, TLRPC.Chat var5, int[] var6) {
      this.type = 1000;
      TLRPC.User var7;
      if (var2.user_id > 0) {
         var7 = MessagesController.getInstance(var1).getUser(var2.user_id);
      } else {
         var7 = null;
      }

      int var11;
      TLRPC.Message var41;
      TLRPC.TL_message var57;
      label702: {
         this.currentEvent = var2;
         GregorianCalendar var8 = new GregorianCalendar();
         var8.setTimeInMillis((long)var2.date * 1000L);
         int var9 = var8.get(6);
         int var10 = var8.get(1);
         var11 = var8.get(2);
         int var12 = 0;
         this.dateKey = String.format("%d_%02d_%02d", var10, var11, var9);
         this.monthKey = String.format("%d_%02d", var10, var11);
         TLRPC.TL_peerChannel var13 = new TLRPC.TL_peerChannel();
         var13.channel_id = var5.id;
         TLRPC.ChannelAdminLogEventAction var14 = var2.action;
         String var24;
         if (var14 instanceof TLRPC.TL_channelAdminLogEventActionChangeTitle) {
            var24 = ((TLRPC.TL_channelAdminLogEventActionChangeTitle)var14).new_value;
            if (var5.megagroup) {
               this.messageText = this.replaceWithLink(LocaleController.formatString("EventLogEditedGroupTitle", 2131559402, var24), "un1", var7);
            } else {
               this.messageText = this.replaceWithLink(LocaleController.formatString("EventLogEditedChannelTitle", 2131559399, var24), "un1", var7);
            }
         } else if (var14 instanceof TLRPC.TL_channelAdminLogEventActionChangePhoto) {
            this.messageOwner = new TLRPC.TL_messageService();
            if (var2.action.new_photo instanceof TLRPC.TL_photoEmpty) {
               this.messageOwner.action = new TLRPC.TL_messageActionChatDeletePhoto();
               if (var5.megagroup) {
                  this.messageText = this.replaceWithLink(LocaleController.getString("EventLogRemovedWGroupPhoto", 2131559448), "un1", var7);
               } else {
                  this.messageText = this.replaceWithLink(LocaleController.getString("EventLogRemovedChannelPhoto", 2131559443), "un1", var7);
               }
            } else {
               this.messageOwner.action = new TLRPC.TL_messageActionChatEditPhoto();
               this.messageOwner.action.photo = var2.action.new_photo;
               if (var5.megagroup) {
                  this.messageText = this.replaceWithLink(LocaleController.getString("EventLogEditedGroupPhoto", 2131559401), "un1", var7);
               } else {
                  this.messageText = this.replaceWithLink(LocaleController.getString("EventLogEditedChannelPhoto", 2131559398), "un1", var7);
               }
            }
         } else if (var14 instanceof TLRPC.TL_channelAdminLogEventActionParticipantJoin) {
            if (var5.megagroup) {
               this.messageText = this.replaceWithLink(LocaleController.getString("EventLogGroupJoined", 2131559420), "un1", var7);
            } else {
               this.messageText = this.replaceWithLink(LocaleController.getString("EventLogChannelJoined", 2131559391), "un1", var7);
            }
         } else if (var14 instanceof TLRPC.TL_channelAdminLogEventActionParticipantLeave) {
            this.messageOwner = new TLRPC.TL_messageService();
            this.messageOwner.action = new TLRPC.TL_messageActionChatDeleteUser();
            this.messageOwner.action.user_id = var2.user_id;
            if (var5.megagroup) {
               this.messageText = this.replaceWithLink(LocaleController.getString("EventLogLeftGroup", 2131559425), "un1", var7);
            } else {
               this.messageText = this.replaceWithLink(LocaleController.getString("EventLogLeftChannel", 2131559424), "un1", var7);
            }
         } else {
            TLRPC.User var25;
            if (var14 instanceof TLRPC.TL_channelAdminLogEventActionParticipantInvite) {
               this.messageOwner = new TLRPC.TL_messageService();
               this.messageOwner.action = new TLRPC.TL_messageActionChatAddUser();
               var25 = MessagesController.getInstance(var1).getUser(var2.action.participant.user_id);
               if (var2.action.participant.user_id == this.messageOwner.from_id) {
                  if (var5.megagroup) {
                     this.messageText = this.replaceWithLink(LocaleController.getString("EventLogGroupJoined", 2131559420), "un1", var7);
                  } else {
                     this.messageText = this.replaceWithLink(LocaleController.getString("EventLogChannelJoined", 2131559391), "un1", var7);
                  }
               } else {
                  this.messageText = this.replaceWithLink(LocaleController.getString("EventLogAdded", 2131559383), "un2", var25);
                  this.messageText = this.replaceWithLink(this.messageText, "un1", var7);
               }
            } else {
               StringBuilder var15;
               char var16;
               TLRPC.ChannelAdminLogEventAction var26;
               byte var32;
               String var37;
               if (var14 instanceof TLRPC.TL_channelAdminLogEventActionParticipantToggleAdmin) {
                  this.messageOwner = new TLRPC.TL_message();
                  var25 = MessagesController.getInstance(var1).getUser(var2.action.prev_participant.user_id);
                  var37 = LocaleController.getString("EventLogPromoted", 2131559432);
                  var11 = var37.indexOf("%1$s");
                  var15 = new StringBuilder(String.format(var37, this.getUserName(var25, this.messageOwner.entities, var11)));
                  var15.append("\n");
                  var26 = var2.action;
                  TLRPC.TL_chatAdminRights var38 = var26.prev_participant.admin_rights;
                  TLRPC.TL_chatAdminRights var34 = var26.new_participant.admin_rights;
                  TLRPC.TL_chatAdminRights var28 = var38;
                  if (var38 == null) {
                     var28 = new TLRPC.TL_chatAdminRights();
                  }

                  var38 = var34;
                  if (var34 == null) {
                     var38 = new TLRPC.TL_chatAdminRights();
                  }

                  if (var28.change_info != var38.change_info) {
                     var15.append('\n');
                     if (var38.change_info) {
                        var32 = 43;
                        var16 = (char)var32;
                     } else {
                        var32 = 45;
                        var16 = (char)var32;
                     }

                     var15.append(var16);
                     var15.append(' ');
                     String var36;
                     if (var5.megagroup) {
                        var11 = 2131559437;
                        var36 = "EventLogPromotedChangeGroupInfo";
                     } else {
                        var11 = 2131559436;
                        var36 = "EventLogPromotedChangeChannelInfo";
                     }

                     var15.append(LocaleController.getString(var36, var11));
                  }

                  if (!var5.megagroup) {
                     if (var28.post_messages != var38.post_messages) {
                        var15.append('\n');
                        if (var38.post_messages) {
                           var32 = 43;
                           var16 = (char)var32;
                        } else {
                           var32 = 45;
                           var16 = (char)var32;
                        }

                        var15.append(var16);
                        var15.append(' ');
                        var15.append(LocaleController.getString("EventLogPromotedPostMessages", 2131559441));
                     }

                     if (var28.edit_messages != var38.edit_messages) {
                        var15.append('\n');
                        if (var38.edit_messages) {
                           var32 = 43;
                           var16 = (char)var32;
                        } else {
                           var32 = 45;
                           var16 = (char)var32;
                        }

                        var15.append(var16);
                        var15.append(' ');
                        var15.append(LocaleController.getString("EventLogPromotedEditMessages", 2131559439));
                     }
                  }

                  if (var28.delete_messages != var38.delete_messages) {
                     var15.append('\n');
                     if (var38.delete_messages) {
                        var32 = 43;
                        var16 = (char)var32;
                     } else {
                        var32 = 45;
                        var16 = (char)var32;
                     }

                     var15.append(var16);
                     var15.append(' ');
                     var15.append(LocaleController.getString("EventLogPromotedDeleteMessages", 2131559438));
                  }

                  if (var28.add_admins != var38.add_admins) {
                     var15.append('\n');
                     if (var38.add_admins) {
                        var32 = 43;
                        var16 = (char)var32;
                     } else {
                        var32 = 45;
                        var16 = (char)var32;
                     }

                     var15.append(var16);
                     var15.append(' ');
                     var15.append(LocaleController.getString("EventLogPromotedAddAdmins", 2131559433));
                  }

                  if (var5.megagroup && var28.ban_users != var38.ban_users) {
                     var15.append('\n');
                     if (var38.ban_users) {
                        var32 = 43;
                        var16 = (char)var32;
                     } else {
                        var32 = 45;
                        var16 = (char)var32;
                     }

                     var15.append(var16);
                     var15.append(' ');
                     var15.append(LocaleController.getString("EventLogPromotedBanUsers", 2131559435));
                  }

                  if (var28.invite_users != var38.invite_users) {
                     var15.append('\n');
                     if (var38.invite_users) {
                        var32 = 43;
                        var16 = (char)var32;
                     } else {
                        var32 = 45;
                        var16 = (char)var32;
                     }

                     var15.append(var16);
                     var15.append(' ');
                     var15.append(LocaleController.getString("EventLogPromotedAddUsers", 2131559434));
                  }

                  if (var5.megagroup && var28.pin_messages != var38.pin_messages) {
                     var15.append('\n');
                     if (var38.pin_messages) {
                        var32 = 43;
                        var16 = (char)var32;
                     } else {
                        var32 = 45;
                        var16 = (char)var32;
                     }

                     var15.append(var16);
                     var15.append(' ');
                     var15.append(LocaleController.getString("EventLogPromotedPinMessages", 2131559440));
                  }

                  this.messageText = var15.toString();
               } else {
                  boolean var27;
                  boolean var29;
                  TLRPC.TL_chatBannedRights var31;
                  byte var33;
                  TLRPC.TL_chatBannedRights var39;
                  TLRPC.TL_chatBannedRights var42;
                  boolean var53;
                  if (var14 instanceof TLRPC.TL_channelAdminLogEventActionDefaultBannedRights) {
                     TLRPC.TL_channelAdminLogEventActionDefaultBannedRights var30 = (TLRPC.TL_channelAdminLogEventActionDefaultBannedRights)var14;
                     this.messageOwner = new TLRPC.TL_message();
                     var42 = var30.prev_banned_rights;
                     var39 = var30.new_banned_rights;
                     var15 = new StringBuilder(LocaleController.getString("EventLogDefaultPermissions", 2131559394));
                     var31 = var42;
                     if (var42 == null) {
                        var31 = new TLRPC.TL_chatBannedRights();
                     }

                     var42 = var39;
                     if (var39 == null) {
                        var42 = new TLRPC.TL_chatBannedRights();
                     }

                     if (var31.send_messages != var42.send_messages) {
                        var15.append('\n');
                        var15.append('\n');
                        if (!var42.send_messages) {
                           var32 = 43;
                           var16 = (char)var32;
                        } else {
                           var32 = 45;
                           var16 = (char)var32;
                        }

                        var15.append(var16);
                        var15.append(' ');
                        var15.append(LocaleController.getString("EventLogRestrictedSendMessages", 2131559455));
                        var29 = true;
                     } else {
                        var29 = false;
                     }

                     label709: {
                        if (var31.send_stickers == var42.send_stickers && var31.send_inline == var42.send_inline && var31.send_gifs == var42.send_gifs) {
                           var53 = var29;
                           if (var31.send_games == var42.send_games) {
                              break label709;
                           }
                        }

                        var53 = var29;
                        if (!var29) {
                           var15.append('\n');
                           var53 = true;
                        }

                        var15.append('\n');
                        if (!var42.send_stickers) {
                           var33 = 43;
                           var16 = (char)var33;
                        } else {
                           var33 = 45;
                           var16 = (char)var33;
                        }

                        var15.append(var16);
                        var15.append(' ');
                        var15.append(LocaleController.getString("EventLogRestrictedSendStickers", 2131559457));
                     }

                     var27 = var53;
                     if (var31.send_media != var42.send_media) {
                        var27 = var53;
                        if (!var53) {
                           var15.append('\n');
                           var27 = true;
                        }

                        var15.append('\n');
                        if (!var42.send_media) {
                           var32 = 43;
                           var16 = (char)var32;
                        } else {
                           var32 = 45;
                           var16 = (char)var32;
                        }

                        var15.append(var16);
                        var15.append(' ');
                        var15.append(LocaleController.getString("EventLogRestrictedSendMedia", 2131559454));
                     }

                     var29 = var27;
                     if (var31.send_polls != var42.send_polls) {
                        var29 = var27;
                        if (!var27) {
                           var15.append('\n');
                           var29 = true;
                        }

                        var15.append('\n');
                        if (!var42.send_polls) {
                           var32 = 43;
                           var16 = (char)var32;
                        } else {
                           var32 = 45;
                           var16 = (char)var32;
                        }

                        var15.append(var16);
                        var15.append(' ');
                        var15.append(LocaleController.getString("EventLogRestrictedSendPolls", 2131559456));
                     }

                     var53 = var29;
                     if (var31.embed_links != var42.embed_links) {
                        var53 = var29;
                        if (!var29) {
                           var15.append('\n');
                           var53 = true;
                        }

                        var15.append('\n');
                        if (!var42.embed_links) {
                           var33 = 43;
                           var16 = (char)var33;
                        } else {
                           var33 = 45;
                           var16 = (char)var33;
                        }

                        var15.append(var16);
                        var15.append(' ');
                        var15.append(LocaleController.getString("EventLogRestrictedSendEmbed", 2131559453));
                     }

                     var29 = var53;
                     if (var31.change_info != var42.change_info) {
                        var29 = var53;
                        if (!var53) {
                           var15.append('\n');
                           var29 = true;
                        }

                        var15.append('\n');
                        if (!var42.change_info) {
                           var32 = 43;
                           var16 = (char)var32;
                        } else {
                           var32 = 45;
                           var16 = (char)var32;
                        }

                        var15.append(var16);
                        var15.append(' ');
                        var15.append(LocaleController.getString("EventLogRestrictedChangeInfo", 2131559449));
                     }

                     var53 = var29;
                     if (var31.invite_users != var42.invite_users) {
                        var53 = var29;
                        if (!var29) {
                           var15.append('\n');
                           var53 = true;
                        }

                        var15.append('\n');
                        if (!var42.invite_users) {
                           var33 = 43;
                           var16 = (char)var33;
                        } else {
                           var33 = 45;
                           var16 = (char)var33;
                        }

                        var15.append(var16);
                        var15.append(' ');
                        var15.append(LocaleController.getString("EventLogRestrictedInviteUsers", 2131559450));
                     }

                     if (var31.pin_messages != var42.pin_messages) {
                        if (!var53) {
                           var15.append('\n');
                        }

                        var15.append('\n');
                        if (!var42.pin_messages) {
                           var32 = 43;
                           var16 = (char)var32;
                        } else {
                           var32 = 45;
                           var16 = (char)var32;
                        }

                        var15.append(var16);
                        var15.append(' ');
                        var15.append(LocaleController.getString("EventLogRestrictedPinMessages", 2131559451));
                     }

                     this.messageText = var15.toString();
                  } else {
                     StringBuilder var35;
                     if (var14 instanceof TLRPC.TL_channelAdminLogEventActionParticipantToggleBan) {
                        this.messageOwner = new TLRPC.TL_message();
                        TLRPC.User var17 = MessagesController.getInstance(var1).getUser(var2.action.prev_participant.user_id);
                        var26 = var2.action;
                        var42 = var26.prev_participant.banned_rights;
                        var39 = var26.new_participant.banned_rights;
                        if (var5.megagroup && (var39 == null || !var39.view_messages || var39 != null && var42 != null && var39.until_date != var42.until_date)) {
                           if (var39 != null && !AndroidUtilities.isBannedForever(var39)) {
                              var15 = new StringBuilder();
                              var11 = var39.until_date - var2.date;
                              var10 = var11 / 60 / 60 / 24;
                              var11 -= var10 * 60 * 60 * 24;
                              var9 = var11 / 60 / 60;
                              int var18 = (var11 - var9 * 60 * 60) / 60;
                              var11 = 0;

                              while(true) {
                                 var35 = var15;
                                 if (var12 >= 3) {
                                    break;
                                 }

                                 label642: {
                                    label641: {
                                       if (var12 == 0) {
                                          if (var10 != 0) {
                                             var24 = LocaleController.formatPluralString("Days", var10);
                                             break label641;
                                          }
                                       } else if (var12 == 1) {
                                          if (var9 != 0) {
                                             var24 = LocaleController.formatPluralString("Hours", var9);
                                             break label641;
                                          }
                                       } else if (var18 != 0) {
                                          var24 = LocaleController.formatPluralString("Minutes", var18);
                                          break label641;
                                       }

                                       var24 = null;
                                       break label642;
                                    }

                                    ++var11;
                                 }

                                 if (var24 != null) {
                                    if (var15.length() > 0) {
                                       var15.append(", ");
                                    }

                                    var15.append(var24);
                                 }

                                 if (var11 == 2) {
                                    var35 = var15;
                                    break;
                                 }

                                 ++var12;
                              }
                           } else {
                              var35 = new StringBuilder(LocaleController.getString("UserRestrictionsUntilForever", 2131561019));
                           }

                           String var43 = LocaleController.getString("EventLogRestrictedUntil", 2131559458);
                           var11 = var43.indexOf("%1$s");
                           var15 = new StringBuilder(String.format(var43, this.getUserName(var17, this.messageOwner.entities, var11), var35.toString()));
                           var31 = var42;
                           if (var42 == null) {
                              var31 = new TLRPC.TL_chatBannedRights();
                           }

                           var42 = var39;
                           if (var39 == null) {
                              var42 = new TLRPC.TL_chatBannedRights();
                           }

                           if (var31.view_messages != var42.view_messages) {
                              var15.append('\n');
                              var15.append('\n');
                              if (!var42.view_messages) {
                                 var32 = 43;
                                 var16 = (char)var32;
                              } else {
                                 var32 = 45;
                                 var16 = (char)var32;
                              }

                              var15.append(var16);
                              var15.append(' ');
                              var15.append(LocaleController.getString("EventLogRestrictedReadMessages", 2131559452));
                              var53 = true;
                           } else {
                              var53 = false;
                           }

                           var29 = var53;
                           if (var31.send_messages != var42.send_messages) {
                              if (!var53) {
                                 var15.append('\n');
                                 var53 = true;
                              }

                              var15.append('\n');
                              if (!var42.send_messages) {
                                 var33 = 43;
                                 var16 = (char)var33;
                              } else {
                                 var33 = 45;
                                 var16 = (char)var33;
                              }

                              var15.append(var16);
                              var15.append(' ');
                              var15.append(LocaleController.getString("EventLogRestrictedSendMessages", 2131559455));
                              var29 = var53;
                           }

                           label721: {
                              if (var31.send_stickers == var42.send_stickers && var31.send_inline == var42.send_inline && var31.send_gifs == var42.send_gifs) {
                                 var53 = var29;
                                 if (var31.send_games == var42.send_games) {
                                    break label721;
                                 }
                              }

                              if (!var29) {
                                 var15.append('\n');
                                 var53 = true;
                              } else {
                                 var53 = var29;
                              }

                              var15.append('\n');
                              if (!var42.send_stickers) {
                                 var33 = 43;
                                 var16 = (char)var33;
                              } else {
                                 var33 = 45;
                                 var16 = (char)var33;
                              }

                              var15.append(var16);
                              var15.append(' ');
                              var15.append(LocaleController.getString("EventLogRestrictedSendStickers", 2131559457));
                           }

                           var29 = var53;
                           if (var31.send_media != var42.send_media) {
                              if (!var53) {
                                 var15.append('\n');
                                 var53 = true;
                              }

                              var15.append('\n');
                              if (!var42.send_media) {
                                 var33 = 43;
                                 var16 = (char)var33;
                              } else {
                                 var33 = 45;
                                 var16 = (char)var33;
                              }

                              var15.append(var16);
                              var15.append(' ');
                              var15.append(LocaleController.getString("EventLogRestrictedSendMedia", 2131559454));
                              var29 = var53;
                           }

                           var27 = var29;
                           if (var31.send_polls != var42.send_polls) {
                              if (!var29) {
                                 var15.append('\n');
                                 var29 = true;
                              }

                              var15.append('\n');
                              if (!var42.send_polls) {
                                 var32 = 43;
                                 var16 = (char)var32;
                              } else {
                                 var32 = 45;
                                 var16 = (char)var32;
                              }

                              var15.append(var16);
                              var15.append(' ');
                              var15.append(LocaleController.getString("EventLogRestrictedSendPolls", 2131559456));
                              var27 = var29;
                           }

                           var53 = var27;
                           if (var31.embed_links != var42.embed_links) {
                              if (!var27) {
                                 var15.append('\n');
                                 var53 = true;
                              } else {
                                 var53 = var27;
                              }

                              var15.append('\n');
                              if (!var42.embed_links) {
                                 var33 = 43;
                                 var16 = (char)var33;
                              } else {
                                 var33 = 45;
                                 var16 = (char)var33;
                              }

                              var15.append(var16);
                              var15.append(' ');
                              var15.append(LocaleController.getString("EventLogRestrictedSendEmbed", 2131559453));
                           }

                           var29 = var53;
                           if (var31.change_info != var42.change_info) {
                              if (!var53) {
                                 var15.append('\n');
                                 var53 = true;
                              }

                              var15.append('\n');
                              if (!var42.change_info) {
                                 var33 = 43;
                                 var16 = (char)var33;
                              } else {
                                 var33 = 45;
                                 var16 = (char)var33;
                              }

                              var15.append(var16);
                              var15.append(' ');
                              var15.append(LocaleController.getString("EventLogRestrictedChangeInfo", 2131559449));
                              var29 = var53;
                           }

                           var53 = var29;
                           if (var31.invite_users != var42.invite_users) {
                              if (!var29) {
                                 var15.append('\n');
                                 var29 = true;
                              }

                              var15.append('\n');
                              if (!var42.invite_users) {
                                 var32 = 43;
                                 var16 = (char)var32;
                              } else {
                                 var32 = 45;
                                 var16 = (char)var32;
                              }

                              var15.append(var16);
                              var15.append(' ');
                              var15.append(LocaleController.getString("EventLogRestrictedInviteUsers", 2131559450));
                              var53 = var29;
                           }

                           if (var31.pin_messages != var42.pin_messages) {
                              if (!var53) {
                                 var15.append('\n');
                              }

                              var15.append('\n');
                              if (!var42.pin_messages) {
                                 var32 = 43;
                                 var16 = (char)var32;
                              } else {
                                 var32 = 45;
                                 var16 = (char)var32;
                              }

                              var15.append(var16);
                              var15.append(' ');
                              var15.append(LocaleController.getString("EventLogRestrictedPinMessages", 2131559451));
                           }

                           this.messageText = var15.toString();
                        } else {
                           if (var39 == null || var42 != null && !var39.view_messages) {
                              var24 = LocaleController.getString("EventLogChannelUnrestricted", 2131559393);
                           } else {
                              var24 = LocaleController.getString("EventLogChannelRestricted", 2131559392);
                           }

                           var11 = var24.indexOf("%1$s");
                           this.messageText = String.format(var24, this.getUserName(var17, this.messageOwner.entities, var11));
                        }
                     } else {
                        TLRPC.Chat var48;
                        if (var14 instanceof TLRPC.TL_channelAdminLogEventActionUpdatePinned) {
                           if (var7 != null && var7.id == 136817688 && var14.message.fwd_from != null) {
                              var48 = MessagesController.getInstance(this.currentAccount).getChat(var2.action.message.fwd_from.channel_id);
                              if (var2.action.message instanceof TLRPC.TL_messageEmpty) {
                                 this.messageText = this.replaceWithLink(LocaleController.getString("EventLogUnpinnedMessages", 2131559467), "un1", var48);
                              } else {
                                 this.messageText = this.replaceWithLink(LocaleController.getString("EventLogPinnedMessages", 2131559429), "un1", var48);
                              }
                           } else if (var2.action.message instanceof TLRPC.TL_messageEmpty) {
                              this.messageText = this.replaceWithLink(LocaleController.getString("EventLogUnpinnedMessages", 2131559467), "un1", var7);
                           } else {
                              this.messageText = this.replaceWithLink(LocaleController.getString("EventLogPinnedMessages", 2131559429), "un1", var7);
                           }
                        } else if (var14 instanceof TLRPC.TL_channelAdminLogEventActionStopPoll) {
                           this.messageText = this.replaceWithLink(LocaleController.getString("EventLogStopPoll", 2131559460), "un1", var7);
                        } else if (var14 instanceof TLRPC.TL_channelAdminLogEventActionToggleSignatures) {
                           if (((TLRPC.TL_channelAdminLogEventActionToggleSignatures)var14).new_value) {
                              this.messageText = this.replaceWithLink(LocaleController.getString("EventLogToggledSignaturesOn", 2131559466), "un1", var7);
                           } else {
                              this.messageText = this.replaceWithLink(LocaleController.getString("EventLogToggledSignaturesOff", 2131559465), "un1", var7);
                           }
                        } else if (var14 instanceof TLRPC.TL_channelAdminLogEventActionToggleInvites) {
                           if (((TLRPC.TL_channelAdminLogEventActionToggleInvites)var14).new_value) {
                              this.messageText = this.replaceWithLink(LocaleController.getString("EventLogToggledInvitesOn", 2131559464), "un1", var7);
                           } else {
                              this.messageText = this.replaceWithLink(LocaleController.getString("EventLogToggledInvitesOff", 2131559463), "un1", var7);
                           }
                        } else if (var14 instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
                           this.messageText = this.replaceWithLink(LocaleController.getString("EventLogDeletedMessages", 2131559395), "un1", var7);
                        } else if (var14 instanceof TLRPC.TL_channelAdminLogEventActionChangeLinkedChat) {
                           var10 = ((TLRPC.TL_channelAdminLogEventActionChangeLinkedChat)var14).new_value;
                           var11 = ((TLRPC.TL_channelAdminLogEventActionChangeLinkedChat)var14).prev_value;
                           if (var5.megagroup) {
                              if (var10 == 0) {
                                 var48 = MessagesController.getInstance(this.currentAccount).getChat(var11);
                                 this.messageText = this.replaceWithLink(LocaleController.getString("EventLogRemovedLinkedChannel", 2131559445), "un1", var7);
                                 this.messageText = this.replaceWithLink(this.messageText, "un2", var48);
                              } else {
                                 var48 = MessagesController.getInstance(this.currentAccount).getChat(var10);
                                 this.messageText = this.replaceWithLink(LocaleController.getString("EventLogChangedLinkedChannel", 2131559388), "un1", var7);
                                 this.messageText = this.replaceWithLink(this.messageText, "un2", var48);
                              }
                           } else if (var10 == 0) {
                              var48 = MessagesController.getInstance(this.currentAccount).getChat(var11);
                              this.messageText = this.replaceWithLink(LocaleController.getString("EventLogRemovedLinkedGroup", 2131559446), "un1", var7);
                              this.messageText = this.replaceWithLink(this.messageText, "un2", var48);
                           } else {
                              var48 = MessagesController.getInstance(this.currentAccount).getChat(var10);
                              this.messageText = this.replaceWithLink(LocaleController.getString("EventLogChangedLinkedGroup", 2131559389), "un1", var7);
                              this.messageText = this.replaceWithLink(this.messageText, "un2", var48);
                           }
                        } else if (var14 instanceof TLRPC.TL_channelAdminLogEventActionTogglePreHistoryHidden) {
                           if (((TLRPC.TL_channelAdminLogEventActionTogglePreHistoryHidden)var14).new_value) {
                              this.messageText = this.replaceWithLink(LocaleController.getString("EventLogToggledInvitesHistoryOff", 2131559461), "un1", var7);
                           } else {
                              this.messageText = this.replaceWithLink(LocaleController.getString("EventLogToggledInvitesHistoryOn", 2131559462), "un1", var7);
                           }
                        } else {
                           TLRPC.WebPage var60;
                           if (var14 instanceof TLRPC.TL_channelAdminLogEventActionChangeAbout) {
                              if (var5.megagroup) {
                                 var11 = 2131559400;
                                 var24 = "EventLogEditedGroupDescription";
                              } else {
                                 var11 = 2131559397;
                                 var24 = "EventLogEditedChannelDescription";
                              }

                              this.messageText = this.replaceWithLink(LocaleController.getString(var24, var11), "un1", var7);
                              var57 = new TLRPC.TL_message();
                              var57.out = false;
                              var57.unread = false;
                              var57.from_id = var2.user_id;
                              var57.to_id = var13;
                              var57.date = var2.date;
                              var14 = var2.action;
                              var57.message = ((TLRPC.TL_channelAdminLogEventActionChangeAbout)var14).new_value;
                              if (!TextUtils.isEmpty(((TLRPC.TL_channelAdminLogEventActionChangeAbout)var14).prev_value)) {
                                 var57.media = new TLRPC.TL_messageMediaWebPage();
                                 var57.media.webpage = new TLRPC.TL_webPage();
                                 var60 = var57.media.webpage;
                                 var60.flags = 10;
                                 var60.display_url = "";
                                 var60.url = "";
                                 var60.site_name = LocaleController.getString("EventLogPreviousGroupDescription", 2131559430);
                                 var57.media.webpage.description = ((TLRPC.TL_channelAdminLogEventActionChangeAbout)var2.action).prev_value;
                              } else {
                                 var57.media = new TLRPC.TL_messageMediaEmpty();
                              }
                              break label702;
                           }

                           if (var14 instanceof TLRPC.TL_channelAdminLogEventActionChangeUsername) {
                              var37 = ((TLRPC.TL_channelAdminLogEventActionChangeUsername)var14).new_value;
                              if (!TextUtils.isEmpty(var37)) {
                                 if (var5.megagroup) {
                                    var11 = 2131559387;
                                    var24 = "EventLogChangedGroupLink";
                                 } else {
                                    var11 = 2131559386;
                                    var24 = "EventLogChangedChannelLink";
                                 }

                                 this.messageText = this.replaceWithLink(LocaleController.getString(var24, var11), "un1", var7);
                              } else {
                                 if (var5.megagroup) {
                                    var11 = 2131559444;
                                    var24 = "EventLogRemovedGroupLink";
                                 } else {
                                    var11 = 2131559442;
                                    var24 = "EventLogRemovedChannelLink";
                                 }

                                 this.messageText = this.replaceWithLink(LocaleController.getString(var24, var11), "un1", var7);
                              }

                              var57 = new TLRPC.TL_message();
                              var57.out = false;
                              var57.unread = false;
                              var57.from_id = var2.user_id;
                              var57.to_id = var13;
                              var57.date = var2.date;
                              StringBuilder var45;
                              if (!TextUtils.isEmpty(var37)) {
                                 var45 = new StringBuilder();
                                 var45.append("https://");
                                 var45.append(MessagesController.getInstance(var1).linkPrefix);
                                 var45.append("/");
                                 var45.append(var37);
                                 var57.message = var45.toString();
                              } else {
                                 var57.message = "";
                              }

                              TLRPC.TL_messageEntityUrl var58 = new TLRPC.TL_messageEntityUrl();
                              var58.offset = 0;
                              var58.length = var57.message.length();
                              var57.entities.add(var58);
                              if (!TextUtils.isEmpty(((TLRPC.TL_channelAdminLogEventActionChangeUsername)var2.action).prev_value)) {
                                 var57.media = new TLRPC.TL_messageMediaWebPage();
                                 var57.media.webpage = new TLRPC.TL_webPage();
                                 var60 = var57.media.webpage;
                                 var60.flags = 10;
                                 var60.display_url = "";
                                 var60.url = "";
                                 var60.site_name = LocaleController.getString("EventLogPreviousLink", 2131559431);
                                 var60 = var57.media.webpage;
                                 var45 = new StringBuilder();
                                 var45.append("https://");
                                 var45.append(MessagesController.getInstance(var1).linkPrefix);
                                 var45.append("/");
                                 var45.append(((TLRPC.TL_channelAdminLogEventActionChangeUsername)var2.action).prev_value);
                                 var60.description = var45.toString();
                              } else {
                                 var57.media = new TLRPC.TL_messageMediaEmpty();
                              }
                              break label702;
                           }

                           if (var14 instanceof TLRPC.TL_channelAdminLogEventActionEditMessage) {
                              TLRPC.TL_message var55 = new TLRPC.TL_message();
                              var55.out = false;
                              var55.unread = false;
                              var55.from_id = var2.user_id;
                              var55.to_id = var13;
                              var55.date = var2.date;
                              TLRPC.ChannelAdminLogEventAction var40 = var2.action;
                              TLRPC.Message var59 = ((TLRPC.TL_channelAdminLogEventActionEditMessage)var40).new_message;
                              var41 = ((TLRPC.TL_channelAdminLogEventActionEditMessage)var40).prev_message;
                              TLRPC.MessageMedia var46 = var59.media;
                              if (var46 != null && !(var46 instanceof TLRPC.TL_messageMediaEmpty) && !(var46 instanceof TLRPC.TL_messageMediaWebPage)) {
                                 label596: {
                                    label736: {
                                       var29 = TextUtils.equals(var59.message, var41.message) ^ true;
                                       if (var59.media.getClass() == var41.media.getClass()) {
                                          label734: {
                                             TLRPC.Photo var49 = var59.media.photo;
                                             if (var49 != null) {
                                                TLRPC.Photo var47 = var41.media.photo;
                                                if (var47 != null && var49.id != var47.id) {
                                                   break label734;
                                                }
                                             }

                                             TLRPC.Document var52 = var59.media.document;
                                             if (var52 == null) {
                                                break label736;
                                             }

                                             TLRPC.Document var50 = var41.media.document;
                                             if (var50 == null || var52.id == var50.id) {
                                                break label736;
                                             }
                                          }
                                       }

                                       var53 = true;
                                       break label596;
                                    }

                                    var53 = false;
                                 }

                                 if (var53 && var29) {
                                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogEditedMediaCaption", 2131559404), "un1", var7);
                                 } else if (var29) {
                                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogEditedCaption", 2131559396), "un1", var7);
                                 } else {
                                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogEditedMedia", 2131559403), "un1", var7);
                                 }

                                 var55.media = var59.media;
                                 if (var29) {
                                    var55.media.webpage = new TLRPC.TL_webPage();
                                    var55.media.webpage.site_name = LocaleController.getString("EventLogOriginalCaption", 2131559426);
                                    if (TextUtils.isEmpty(var41.message)) {
                                       var55.media.webpage.description = LocaleController.getString("EventLogOriginalCaptionEmpty", 2131559427);
                                    } else {
                                       var55.media.webpage.description = var41.message;
                                    }
                                 }
                              } else {
                                 this.messageText = this.replaceWithLink(LocaleController.getString("EventLogEditedMessages", 2131559405), "un1", var7);
                                 var55.message = var59.message;
                                 var55.media = new TLRPC.TL_messageMediaWebPage();
                                 var55.media.webpage = new TLRPC.TL_webPage();
                                 var55.media.webpage.site_name = LocaleController.getString("EventLogOriginalMessages", 2131559428);
                                 if (TextUtils.isEmpty(var41.message)) {
                                    var55.media.webpage.description = LocaleController.getString("EventLogOriginalCaptionEmpty", 2131559427);
                                 } else {
                                    var55.media.webpage.description = var41.message;
                                 }
                              }

                              var55.reply_markup = var59.reply_markup;
                              TLRPC.WebPage var44 = var55.media.webpage;
                              var57 = var55;
                              if (var44 != null) {
                                 var44.flags = 10;
                                 var44.display_url = "";
                                 var44.url = "";
                                 var57 = var55;
                              }
                              break label702;
                           }

                           if (var14 instanceof TLRPC.TL_channelAdminLogEventActionChangeStickerSet) {
                              TLRPC.InputStickerSet var56 = ((TLRPC.TL_channelAdminLogEventActionChangeStickerSet)var14).new_stickerset;
                              TLRPC.InputStickerSet var54 = ((TLRPC.TL_channelAdminLogEventActionChangeStickerSet)var14).new_stickerset;
                              if (var56 != null && !(var56 instanceof TLRPC.TL_inputStickerSetEmpty)) {
                                 this.messageText = this.replaceWithLink(LocaleController.getString("EventLogChangedStickersSet", 2131559390), "un1", var7);
                              } else {
                                 this.messageText = this.replaceWithLink(LocaleController.getString("EventLogRemovedStickersSet", 2131559447), "un1", var7);
                              }
                           } else {
                              var35 = new StringBuilder();
                              var35.append("unsupported ");
                              var35.append(var2.action);
                              this.messageText = var35.toString();
                           }
                        }
                     }
                  }
               }
            }
         }

         var57 = null;
      }

      if (this.messageOwner == null) {
         this.messageOwner = new TLRPC.TL_messageService();
      }

      this.messageOwner.message = this.messageText.toString();
      TLRPC.Message var61 = this.messageOwner;
      var61.from_id = var2.user_id;
      var61.date = var2.date;
      var11 = var6[0]++;
      var61.id = var11;
      this.eventId = var2.id;
      var61.out = false;
      var61.to_id = new TLRPC.TL_peerChannel();
      var61 = this.messageOwner;
      var61.to_id.channel_id = var5.id;
      var61.unread = false;
      if (var5.megagroup) {
         var61.flags |= Integer.MIN_VALUE;
      }

      MediaController var51 = MediaController.getInstance();
      var41 = var2.action.message;
      Object var62 = var57;
      if (var41 != null) {
         var62 = var57;
         if (!(var41 instanceof TLRPC.TL_messageEmpty)) {
            var62 = var41;
         }
      }

      if (var62 != null) {
         ((TLRPC.Message)var62).out = false;
         var11 = var6[0]++;
         ((TLRPC.Message)var62).id = var11;
         ((TLRPC.Message)var62).reply_to_msg_id = 0;
         ((TLRPC.Message)var62).flags &= -32769;
         if (var5.megagroup) {
            ((TLRPC.Message)var62).flags |= Integer.MIN_VALUE;
         }

         MessageObject var23 = new MessageObject(var1, (TLRPC.Message)var62, (AbstractMap)null, (AbstractMap)null, true, this.eventId);
         if (var23.contentType >= 0) {
            if (var51.isPlayingMessage(var23)) {
               MessageObject var22 = var51.getPlayingMessageObject();
               var23.audioProgress = var22.audioProgress;
               var23.audioProgressSec = var22.audioProgressSec;
            }

            this.createDateArray(var1, var2, var3, var4);
            var3.add(var3.size() - 1, var23);
         } else {
            this.contentType = -1;
         }
      }

      if (this.contentType >= 0) {
         this.createDateArray(var1, var2, var3, var4);
         var3.add(var3.size() - 1, this);
         if (this.messageText == null) {
            this.messageText = "";
         }

         this.setType();
         this.measureInlineBotButtons();
         this.generateCaption();
         TextPaint var19;
         if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
            var19 = Theme.chat_msgGameTextPaint;
         } else {
            var19 = Theme.chat_msgTextPaint;
         }

         int[] var20;
         if (SharedConfig.allowBigEmoji) {
            var20 = new int[1];
         } else {
            var20 = null;
         }

         this.messageText = Emoji.replaceEmoji(this.messageText, var19.getFontMetricsInt(), AndroidUtilities.dp(20.0F), false, var20);
         this.checkEmojiOnly(var20);
         if (var51.isPlayingMessage(this)) {
            MessageObject var21 = var51.getPlayingMessageObject();
            this.audioProgress = var21.audioProgress;
            this.audioProgressSec = var21.audioProgressSec;
         }

         this.generateLayout(var7);
         this.layoutCreated = true;
         this.generateThumbs(false);
         this.checkMediaExistance();
      }

   }

   public static boolean addEntitiesToText(CharSequence var0, ArrayList var1, boolean var2, int var3, boolean var4, boolean var5, boolean var6) {
      if (!(var0 instanceof Spannable)) {
         return false;
      } else {
         Spannable var7 = (Spannable)var0;
         int var8 = var1.size();
         URLSpan[] var9 = (URLSpan[])var7.getSpans(0, var0.length(), URLSpan.class);
         boolean var10;
         if (var9 != null && var9.length > 0) {
            var10 = true;
         } else {
            var10 = false;
         }

         byte var11;
         byte var20;
         if (var5) {
            var20 = 2;
            var11 = var20;
         } else if (var2) {
            var20 = 1;
            var11 = var20;
         } else {
            var20 = 0;
            var11 = var20;
         }

         var2 = var10;

         for(var3 = 0; var3 < var8; var2 = var5) {
            TLRPC.MessageEntity var12 = (TLRPC.MessageEntity)var1.get(var3);
            var5 = var2;
            if (var12.length > 0) {
               int var13 = var12.offset;
               var5 = var2;
               if (var13 >= 0) {
                  if (var13 >= var0.length()) {
                     var5 = var2;
                  } else {
                     if (var12.offset + var12.length > var0.length()) {
                        var12.length = var0.length() - var12.offset;
                     }

                     if ((!var6 || var12 instanceof TLRPC.TL_messageEntityBold || var12 instanceof TLRPC.TL_messageEntityItalic || var12 instanceof TLRPC.TL_messageEntityCode || var12 instanceof TLRPC.TL_messageEntityPre || var12 instanceof TLRPC.TL_messageEntityMentionName || var12 instanceof TLRPC.TL_inputMessageEntityMentionName) && var9 != null && var9.length > 0) {
                        for(var13 = 0; var13 < var9.length; ++var13) {
                           if (var9[var13] != null) {
                              int var14 = var7.getSpanStart(var9[var13]);
                              int var15 = var7.getSpanEnd(var9[var13]);
                              int var16 = var12.offset;
                              if (var16 > var14 || var16 + var12.length < var14) {
                                 var14 = var12.offset;
                                 if (var14 > var15 || var14 + var12.length < var15) {
                                    continue;
                                 }
                              }

                              var7.removeSpan(var9[var13]);
                              var9[var13] = null;
                           }
                        }
                     }

                     TypefaceSpan var29;
                     if (var12 instanceof TLRPC.TL_messageEntityBold) {
                        var29 = new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                        var13 = var12.offset;
                        var7.setSpan(var29, var13, var12.length + var13, 33);
                        var5 = var2;
                     } else if (var12 instanceof TLRPC.TL_messageEntityItalic) {
                        var29 = new TypefaceSpan(AndroidUtilities.getTypeface("fonts/ritalic.ttf"));
                        var13 = var12.offset;
                        var7.setSpan(var29, var13, var12.length + var13, 33);
                        var5 = var2;
                     } else if (!(var12 instanceof TLRPC.TL_messageEntityCode) && !(var12 instanceof TLRPC.TL_messageEntityPre)) {
                        StringBuilder var23;
                        URLSpanUserMention var28;
                        if (var12 instanceof TLRPC.TL_messageEntityMentionName) {
                           var5 = var2;
                           if (var4) {
                              var23 = new StringBuilder();
                              var23.append("");
                              var23.append(((TLRPC.TL_messageEntityMentionName)var12).user_id);
                              var28 = new URLSpanUserMention(var23.toString(), var11);
                              var13 = var12.offset;
                              var7.setSpan(var28, var13, var12.length + var13, 33);
                              var5 = var2;
                           }
                        } else if (var12 instanceof TLRPC.TL_inputMessageEntityMentionName) {
                           var5 = var2;
                           if (var4) {
                              var23 = new StringBuilder();
                              var23.append("");
                              var23.append(((TLRPC.TL_inputMessageEntityMentionName)var12).user_id.user_id);
                              var28 = new URLSpanUserMention(var23.toString(), var11);
                              var13 = var12.offset;
                              var7.setSpan(var28, var13, var12.length + var13, 33);
                              var5 = var2;
                           }
                        } else {
                           var5 = var2;
                           if (!var6) {
                              var13 = var12.offset;
                              String var18 = TextUtils.substring(var0, var13, var12.length + var13);
                              if (var12 instanceof TLRPC.TL_messageEntityBotCommand) {
                                 URLSpanBotCommand var27 = new URLSpanBotCommand(var18, var11);
                                 var13 = var12.offset;
                                 var7.setSpan(var27, var13, var12.length + var13, 33);
                                 var5 = var2;
                              } else if (!(var12 instanceof TLRPC.TL_messageEntityHashtag) && (!var4 || !(var12 instanceof TLRPC.TL_messageEntityMention)) && !(var12 instanceof TLRPC.TL_messageEntityCashtag)) {
                                 URLSpanReplacement var26;
                                 if (var12 instanceof TLRPC.TL_messageEntityEmail) {
                                    var23 = new StringBuilder();
                                    var23.append("mailto:");
                                    var23.append(var18);
                                    var26 = new URLSpanReplacement(var23.toString());
                                    var13 = var12.offset;
                                    var7.setSpan(var26, var13, var12.length + var13, 33);
                                    var5 = var2;
                                 } else {
                                    label202: {
                                       URLSpanBrowser var22;
                                       if (var12 instanceof TLRPC.TL_messageEntityUrl) {
                                          if (Browser.isPassportUrl(var12.url)) {
                                             var5 = var2;
                                             break label202;
                                          }

                                          if (!var18.toLowerCase().startsWith("http") && !var18.toLowerCase().startsWith("tg://")) {
                                             var23 = new StringBuilder();
                                             var23.append("http://");
                                             var23.append(var18);
                                             var22 = new URLSpanBrowser(var23.toString());
                                             var13 = var12.offset;
                                             var7.setSpan(var22, var13, var12.length + var13, 33);
                                          } else {
                                             var22 = new URLSpanBrowser(var18);
                                             var13 = var12.offset;
                                             var7.setSpan(var22, var13, var12.length + var13, 33);
                                          }
                                       } else {
                                          if (!(var12 instanceof TLRPC.TL_messageEntityPhone)) {
                                             var5 = var2;
                                             if (var12 instanceof TLRPC.TL_messageEntityTextUrl) {
                                                if (Browser.isPassportUrl(var12.url)) {
                                                   var5 = var2;
                                                } else {
                                                   var26 = new URLSpanReplacement(var12.url);
                                                   var13 = var12.offset;
                                                   var7.setSpan(var26, var13, var12.length + var13, 33);
                                                   var5 = var2;
                                                }
                                             }
                                             break label202;
                                          }

                                          String var19 = PhoneFormat.stripExceptNumbers(var18);
                                          String var24 = var19;
                                          if (var18.startsWith("+")) {
                                             var23 = new StringBuilder();
                                             var23.append("+");
                                             var23.append(var19);
                                             var24 = var23.toString();
                                          }

                                          StringBuilder var25 = new StringBuilder();
                                          var25.append("tel:");
                                          var25.append(var24);
                                          var22 = new URLSpanBrowser(var25.toString());
                                          var13 = var12.offset;
                                          var7.setSpan(var22, var13, var12.length + var13, 33);
                                       }

                                       var5 = true;
                                    }
                                 }
                              } else {
                                 URLSpanNoUnderline var21 = new URLSpanNoUnderline(var18);
                                 var13 = var12.offset;
                                 var7.setSpan(var21, var13, var12.length + var13, 33);
                                 var5 = var2;
                              }
                           }
                        }
                     } else {
                        var13 = var12.offset;
                        URLSpanMono var17 = new URLSpanMono(var7, var13, var12.length + var13, var11);
                        var13 = var12.offset;
                        var7.setSpan(var17, var13, var12.length + var13, 33);
                        var5 = var2;
                     }
                  }
               }
            }

            ++var3;
         }

         return var2;
      }
   }

   private boolean addEntitiesToText(CharSequence var1, boolean var2) {
      return addEntitiesToText(var1, this.messageOwner.entities, this.isOutOwner(), this.type, true, false, var2);
   }

   public static void addLinks(boolean var0, CharSequence var1) {
      addLinks(var0, var1, true);
   }

   public static void addLinks(boolean var0, CharSequence var1, boolean var2) {
      if (var1 instanceof Spannable && containsUrls(var1)) {
         if (var1.length() < 1000) {
            try {
               Linkify.addLinks((Spannable)var1, 5);
            } catch (Exception var5) {
               FileLog.e((Throwable)var5);
            }
         } else {
            try {
               Linkify.addLinks((Spannable)var1, 1);
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }
         }

         addUsernamesAndHashtags(var0, var1, var2, 0);
      }

   }

   private static void addUsernamesAndHashtags(boolean var0, CharSequence var1, boolean var2, int var3) {
      Exception var10000;
      label161: {
         Matcher var4;
         boolean var10001;
         if (var3 == 1) {
            try {
               if (instagramUrlPattern == null) {
                  instagramUrlPattern = Pattern.compile("(^|\\s|\\()@[a-zA-Z\\d_.]{1,32}|(^|\\s|\\()#[\\w.]+");
               }
            } catch (Exception var26) {
               var10000 = var26;
               var10001 = false;
               break label161;
            }

            try {
               var4 = instagramUrlPattern.matcher(var1);
            } catch (Exception var25) {
               var10000 = var25;
               var10001 = false;
               break label161;
            }
         } else {
            try {
               if (urlPattern == null) {
                  urlPattern = Pattern.compile("(^|\\s)/[a-zA-Z@\\d_]{1,255}|(^|\\s|\\()@[a-zA-Z\\d_]{1,32}|(^|\\s|\\()#[\\w.]+|(^|\\s)\\$[A-Z]{3,8}([ ,.]|$)");
               }
            } catch (Exception var27) {
               var10000 = var27;
               var10001 = false;
               break label161;
            }

            try {
               var4 = urlPattern.matcher(var1);
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
               break label161;
            }
         }

         while(true) {
            int var5;
            int var6;
            char var7;
            try {
               if (!var4.find()) {
                  return;
               }

               var5 = var4.start();
               var6 = var4.end();
               var7 = var1.charAt(var5);
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }

            int var9;
            char var10;
            if (var3 != 0) {
               int var8 = var5;
               if (var7 != '@') {
                  var8 = var5;
                  if (var7 != '#') {
                     var8 = var5 + 1;
                  }
               }

               char var29;
               try {
                  var29 = var1.charAt(var8);
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
                  break;
               }

               var9 = var8;
               var10 = var29;
               if (var29 != '@') {
                  var9 = var8;
                  var10 = var29;
                  if (var29 != '#') {
                     continue;
                  }
               }
            } else {
               var9 = var5;
               var10 = var7;
               if (var7 != '@') {
                  var9 = var5;
                  var10 = var7;
                  if (var7 != '#') {
                     var9 = var5;
                     var10 = var7;
                     if (var7 != '/') {
                        var9 = var5;
                        var10 = var7;
                        if (var7 != '$') {
                           var9 = var5 + 1;
                           var10 = var7;
                        }
                     }
                  }
               }
            }

            Object var11 = null;
            StringBuilder var31;
            if (var3 == 1) {
               if (var10 == '@') {
                  try {
                     StringBuilder var12 = new StringBuilder();
                     var12.append("https://instagram.com/");
                     var12.append(var1.subSequence(var9 + 1, var6).toString());
                     var11 = new URLSpanNoUnderline(var12.toString());
                  } catch (Exception var20) {
                     var10000 = var20;
                     var10001 = false;
                     break;
                  }
               } else if (var10 == '#') {
                  try {
                     var31 = new StringBuilder();
                     var31.append("https://www.instagram.com/explore/tags/");
                     var31.append(var1.subSequence(var9 + 1, var6).toString());
                     var11 = new URLSpanNoUnderline(var31.toString());
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break;
                  }
               }
            } else if (var3 == 2) {
               if (var10 == '@') {
                  try {
                     var31 = new StringBuilder();
                     var31.append("https://twitter.com/");
                     var31.append(var1.subSequence(var9 + 1, var6).toString());
                     var11 = new URLSpanNoUnderline(var31.toString());
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break;
                  }
               } else if (var10 == '#') {
                  try {
                     var31 = new StringBuilder();
                     var31.append("https://twitter.com/hashtag/");
                     var31.append(var1.subSequence(var9 + 1, var6).toString());
                     var11 = new URLSpanNoUnderline(var31.toString());
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break;
                  }
               }
            } else {
               label128: {
                  label127: {
                     try {
                        if (var1.charAt(var9) != '/') {
                           break label127;
                        }
                     } catch (Exception var23) {
                        var10000 = var23;
                        var10001 = false;
                        break;
                     }

                     if (var2) {
                        String var32;
                        try {
                           var32 = var1.subSequence(var9, var6).toString();
                        } catch (Exception var16) {
                           var10000 = var16;
                           var10001 = false;
                           break;
                        }

                        byte var30;
                        if (var0) {
                           var30 = 1;
                        } else {
                           var30 = 0;
                        }

                        try {
                           var11 = new URLSpanBotCommand(var32, var30);
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break;
                        }
                     }
                     break label128;
                  }

                  try {
                     var11 = new URLSpanNoUnderline(var1.subSequence(var9, var6).toString());
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break;
                  }
               }
            }

            if (var11 != null) {
               try {
                  ((Spannable)var1).setSpan(var11, var9, var6, 0);
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break;
               }
            }
         }
      }

      Exception var28 = var10000;
      FileLog.e((Throwable)var28);
   }

   public static boolean canDeleteMessage(int var0, TLRPC.Message var1, TLRPC.Chat var2) {
      if (var1.id < 0) {
         return true;
      } else {
         TLRPC.Chat var3 = var2;
         if (var2 == null) {
            var3 = var2;
            if (var1.to_id.channel_id != 0) {
               var3 = MessagesController.getInstance(var0).getChat(var1.to_id.channel_id);
            }
         }

         boolean var4 = ChatObject.isChannel(var3);
         boolean var5 = false;
         boolean var6 = false;
         if (var4) {
            var5 = var6;
            if (var1.id != 1) {
               if (!var3.creator) {
                  TLRPC.TL_chatAdminRights var7 = var3.admin_rights;
                  if (var7 == null || !var7.delete_messages && !var1.out) {
                     var5 = var6;
                     if (!var3.megagroup) {
                        return var5;
                     }

                     var5 = var6;
                     if (!var1.out) {
                        return var5;
                     }

                     var5 = var6;
                     if (var1.from_id <= 0) {
                        return var5;
                     }
                  }
               }

               var5 = true;
            }

            return var5;
         } else {
            if (isOut(var1) || !ChatObject.isChannel(var3)) {
               var5 = true;
            }

            return var5;
         }
      }
   }

   public static boolean canEditMessage(int var0, TLRPC.Message var1, TLRPC.Chat var2) {
      boolean var3 = false;
      if (var2 == null || !var2.left && !var2.kicked) {
         if (var1 != null && var1.to_id != null) {
            TLRPC.MessageMedia var4 = var1.media;
            if (var4 == null || !isRoundVideoDocument(var4.document) && !isStickerDocument(var1.media.document) && !isAnimatedStickerDocument(var1.media.document) && !isLocationMessage(var1)) {
               TLRPC.MessageAction var10 = var1.action;
               if ((var10 == null || var10 instanceof TLRPC.TL_messageActionEmpty) && !isForwardedMessage(var1) && var1.via_bot_id == 0 && var1.id >= 0) {
                  int var5 = var1.from_id;
                  if (var5 == var1.to_id.user_id && var5 == UserConfig.getInstance(var0).getClientUserId() && !isLiveLocationMessage(var1) && !(var1.media instanceof TLRPC.TL_messageMediaContact)) {
                     return true;
                  }

                  TLRPC.Chat var11 = var2;
                  if (var2 == null) {
                     var11 = var2;
                     if (var1.to_id.channel_id != 0) {
                        var2 = MessagesController.getInstance(var0).getChat(var1.to_id.channel_id);
                        var11 = var2;
                        if (var2 == null) {
                           return false;
                        }
                     }
                  }

                  TLRPC.MessageMedia var8 = var1.media;
                  if (var8 != null && !(var8 instanceof TLRPC.TL_messageMediaEmpty) && !(var8 instanceof TLRPC.TL_messageMediaPhoto) && !(var8 instanceof TLRPC.TL_messageMediaDocument) && !(var8 instanceof TLRPC.TL_messageMediaWebPage)) {
                     return false;
                  }

                  TLRPC.TL_chatAdminRights var9;
                  if (var1.out && var11 != null && var11.megagroup) {
                     if (var11.creator) {
                        return true;
                     }

                     var9 = var11.admin_rights;
                     if (var9 != null && var9.pin_messages) {
                        return true;
                     }
                  }

                  if (Math.abs(var1.date - ConnectionsManager.getInstance(var0).getCurrentTime()) > MessagesController.getInstance(var0).maxEditTime) {
                     return false;
                  }

                  TLRPC.MessageMedia var7;
                  if (var1.to_id.channel_id == 0) {
                     boolean var6;
                     if (!var1.out) {
                        var6 = var3;
                        if (var1.from_id != UserConfig.getInstance(var0).getClientUserId()) {
                           return var6;
                        }
                     }

                     var8 = var1.media;
                     if (!(var8 instanceof TLRPC.TL_messageMediaPhoto) && (!(var8 instanceof TLRPC.TL_messageMediaDocument) || isStickerMessage(var1) || isAnimatedStickerMessage(var1))) {
                        var7 = var1.media;
                        if (!(var7 instanceof TLRPC.TL_messageMediaEmpty) && !(var7 instanceof TLRPC.TL_messageMediaWebPage)) {
                           var6 = var3;
                           if (var7 != null) {
                              return var6;
                           }
                        }
                     }

                     var6 = true;
                     return var6;
                  }

                  if (!var11.megagroup || !var1.out) {
                     if (var11.megagroup) {
                        return false;
                     }

                     if (!var11.creator) {
                        var9 = var11.admin_rights;
                        if (var9 == null || !var9.edit_messages && !var1.out) {
                           return false;
                        }
                     }

                     if (!var1.post) {
                        return false;
                     }
                  }

                  var8 = var1.media;
                  if (var8 instanceof TLRPC.TL_messageMediaPhoto || var8 instanceof TLRPC.TL_messageMediaDocument && !isStickerMessage(var1) && !isAnimatedStickerMessage(var1)) {
                     return true;
                  }

                  var7 = var1.media;
                  if (var7 instanceof TLRPC.TL_messageMediaEmpty || var7 instanceof TLRPC.TL_messageMediaWebPage || var7 == null) {
                     return true;
                  }

                  return false;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean canEditMessageAnytime(int var0, TLRPC.Message var1, TLRPC.Chat var2) {
      if (var1 != null && var1.to_id != null) {
         TLRPC.MessageMedia var3 = var1.media;
         if (var3 == null || !isRoundVideoDocument(var3.document) && !isStickerDocument(var1.media.document) && !isAnimatedStickerDocument(var1.media.document)) {
            TLRPC.MessageAction var6 = var1.action;
            if ((var6 == null || var6 instanceof TLRPC.TL_messageActionEmpty) && !isForwardedMessage(var1) && var1.via_bot_id == 0 && var1.id >= 0) {
               int var4 = var1.from_id;
               if (var4 == var1.to_id.user_id && var4 == UserConfig.getInstance(var0).getClientUserId() && !isLiveLocationMessage(var1)) {
                  return true;
               }

               TLRPC.Chat var7 = var2;
               if (var2 == null) {
                  var7 = var2;
                  if (var1.to_id.channel_id != 0) {
                     var2 = MessagesController.getInstance(UserConfig.selectedAccount).getChat(var1.to_id.channel_id);
                     var7 = var2;
                     if (var2 == null) {
                        return false;
                     }
                  }
               }

               if (var1.out && var7 != null && var7.megagroup) {
                  if (var7.creator) {
                     return true;
                  }

                  TLRPC.TL_chatAdminRights var5 = var7.admin_rights;
                  if (var5 != null && var5.pin_messages) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public static boolean canPreviewDocument(TLRPC.Document var0) {
      boolean var1 = false;
      if (var0 != null) {
         String var2 = var0.mime_type;
         if (var2 != null) {
            var2 = var2.toLowerCase();
            if (!isDocumentHasThumb(var0) || !var2.equals("image/png") && !var2.equals("image/jpg") && !var2.equals("image/jpeg")) {
               if (BuildVars.DEBUG_PRIVATE_VERSION) {
                  String var6 = FileLoader.getDocumentFileName(var0);
                  if (var6.startsWith("tg_secret_sticker") && var6.endsWith("json")) {
                     return true;
                  }
               }
            } else {
               for(int var3 = 0; var3 < var0.attributes.size(); ++var3) {
                  TLRPC.DocumentAttribute var7 = (TLRPC.DocumentAttribute)var0.attributes.get(var3);
                  if (var7 instanceof TLRPC.TL_documentAttributeImageSize) {
                     TLRPC.TL_documentAttributeImageSize var5 = (TLRPC.TL_documentAttributeImageSize)var7;
                     boolean var4 = var1;
                     if (var5.w < 6000) {
                        var4 = var1;
                        if (var5.h < 6000) {
                           var4 = true;
                        }
                     }

                     return var4;
                  }
               }
            }
         }
      }

      return false;
   }

   private void checkEmojiOnly(int[] var1) {
      if (var1 != null) {
         int var2 = 0;
         if (var1[0] >= 1 && var1[0] <= 3) {
            int var3 = var1[0];
            TextPaint var5;
            if (var3 != 1) {
               if (var3 != 2) {
                  var5 = Theme.chat_msgTextPaintThreeEmoji;
                  var3 = AndroidUtilities.dp(24.0F);
                  this.emojiOnlyCount = 3;
               } else {
                  var5 = Theme.chat_msgTextPaintTwoEmoji;
                  var3 = AndroidUtilities.dp(28.0F);
                  this.emojiOnlyCount = 2;
               }
            } else {
               var5 = Theme.chat_msgTextPaintOneEmoji;
               var3 = AndroidUtilities.dp(32.0F);
               this.emojiOnlyCount = 1;
            }

            CharSequence var4 = this.messageText;
            Emoji.EmojiSpan[] var6 = (Emoji.EmojiSpan[])((Spannable)var4).getSpans(0, var4.length(), Emoji.EmojiSpan.class);
            if (var6 != null && var6.length > 0) {
               while(var2 < var6.length) {
                  var6[var2].replaceFontMetrics(var5.getFontMetricsInt(), var3);
                  ++var2;
               }
            }
         }
      }

   }

   private static boolean containsUrls(CharSequence var0) {
      if (var0 != null && var0.length() >= 2 && var0.length() <= 20480) {
         int var1 = var0.length();
         int var2 = 0;
         int var3 = 0;
         int var4 = 0;
         int var5 = 0;

         int var10;
         for(char var6 = 0; var2 < var1; var5 = var10) {
            char var7 = var0.charAt(var2);
            int var8;
            int var9;
            if (var7 >= '0' && var7 <= '9') {
               var8 = var3 + 1;
               if (var8 >= 6) {
                  return true;
               }

               var9 = 0;
               var10 = 0;
            } else {
               label104: {
                  if (var7 != ' ') {
                     var8 = var3;
                     var9 = var4;
                     var10 = var5;
                     if (var3 > 0) {
                        break label104;
                     }
                  }

                  var8 = 0;
                  var10 = var5;
                  var9 = var4;
               }
            }

            if ((var7 == '@' || var7 == '#' || var7 == '/' || var7 == '$') && var2 == 0) {
               return true;
            }

            if (var2 != 0) {
               var5 = var2 - 1;
               if (var0.charAt(var5) == ' ' || var0.charAt(var5) == '\n') {
                  return true;
               }
            }

            label73: {
               if (var7 == ':') {
                  if (var9 == 0) {
                     var9 = 1;
                     break label73;
                  }
               } else {
                  if (var7 != '/') {
                     if (var7 == '.') {
                        if (var10 == 0 && var6 != ' ') {
                           ++var10;
                           break label73;
                        }
                     } else if (var7 != ' ' && var6 == '.' && var10 == 1) {
                        return true;
                     }

                     var10 = 0;
                     break label73;
                  }

                  if (var9 == 2) {
                     return true;
                  }

                  if (var9 == 1) {
                     ++var9;
                     break label73;
                  }
               }

               var9 = 0;
            }

            ++var2;
            var6 = var7;
            var3 = var8;
            var4 = var9;
         }
      }

      return false;
   }

   private void createDateArray(int var1, TLRPC.TL_channelAdminLogEvent var2, ArrayList var3, HashMap var4) {
      if ((ArrayList)var4.get(this.dateKey) == null) {
         ArrayList var5 = new ArrayList();
         var4.put(this.dateKey, var5);
         TLRPC.TL_message var7 = new TLRPC.TL_message();
         var7.message = LocaleController.formatDateChat((long)var2.date);
         var7.id = 0;
         var7.date = var2.date;
         MessageObject var6 = new MessageObject(var1, var7, false);
         var6.type = 10;
         var6.contentType = 1;
         var6.isDateObject = true;
         var3.add(var6);
      }

   }

   public static long getDialogId(TLRPC.Message var0) {
      if (var0.dialog_id == 0L) {
         TLRPC.Peer var1 = var0.to_id;
         if (var1 != null) {
            int var2 = var1.chat_id;
            if (var2 != 0) {
               if (var2 < 0) {
                  var0.dialog_id = AndroidUtilities.makeBroadcastId(var2);
               } else {
                  var0.dialog_id = (long)(-var2);
               }
            } else {
               var2 = var1.channel_id;
               if (var2 != 0) {
                  var0.dialog_id = (long)(-var2);
               } else if (isOut(var0)) {
                  var0.dialog_id = (long)var0.to_id.user_id;
               } else {
                  var0.dialog_id = (long)var0.from_id;
               }
            }
         }
      }

      return var0.dialog_id;
   }

   public static TLRPC.Document getDocument(TLRPC.Message var0) {
      TLRPC.MessageMedia var1 = var0.media;
      if (var1 instanceof TLRPC.TL_messageMediaWebPage) {
         return var1.webpage.document;
      } else if (var1 instanceof TLRPC.TL_messageMediaGame) {
         return var1.game.document;
      } else {
         TLRPC.Document var2;
         if (var1 != null) {
            var2 = var1.document;
         } else {
            var2 = null;
         }

         return var2;
      }
   }

   private TLRPC.Document getDocumentWithId(TLRPC.WebPage var1, long var2) {
      if (var1 != null && var1.cached_page != null) {
         TLRPC.Document var4 = var1.document;
         if (var4 != null && var4.id == var2) {
            return var4;
         }

         for(int var5 = 0; var5 < var1.cached_page.documents.size(); ++var5) {
            var4 = (TLRPC.Document)var1.cached_page.documents.get(var5);
            if (var4.id == var2) {
               return var4;
            }
         }
      }

      return null;
   }

   public static int getInlineResultDuration(TLRPC.BotInlineResult var0) {
      int var1 = getWebDocumentDuration(var0.content);
      int var2 = var1;
      if (var1 == 0) {
         var2 = getWebDocumentDuration(var0.thumb);
      }

      return var2;
   }

   public static int[] getInlineResultWidthAndHeight(TLRPC.BotInlineResult var0) {
      int[] var1 = getWebDocumentWidthAndHeight(var0.content);
      int[] var2 = var1;
      if (var1 == null) {
         int[] var3 = getWebDocumentWidthAndHeight(var0.thumb);
         var2 = var3;
         if (var3 == null) {
            var2 = new int[]{0, 0};
         }
      }

      return var2;
   }

   public static TLRPC.InputStickerSet getInputStickerSet(TLRPC.Message var0) {
      TLRPC.MessageMedia var2 = var0.media;
      if (var2 != null) {
         TLRPC.Document var3 = var2.document;
         if (var3 != null) {
            Iterator var4 = var3.attributes.iterator();

            while(var4.hasNext()) {
               TLRPC.DocumentAttribute var1 = (TLRPC.DocumentAttribute)var4.next();
               if (var1 instanceof TLRPC.TL_documentAttributeSticker) {
                  TLRPC.InputStickerSet var5 = var1.stickerset;
                  if (var5 instanceof TLRPC.TL_inputStickerSetEmpty) {
                     return null;
                  }

                  return var5;
               }
            }
         }
      }

      return null;
   }

   private MessageObject getMessageObjectForBlock(TLRPC.WebPage var1, TLRPC.PageBlock var2) {
      TLRPC.TL_message var4;
      if (var2 instanceof TLRPC.TL_pageBlockPhoto) {
         TLRPC.Photo var5 = this.getPhotoWithId(var1, ((TLRPC.TL_pageBlockPhoto)var2).photo_id);
         if (var5 == var1.photo) {
            return this;
         }

         var4 = new TLRPC.TL_message();
         var4.media = new TLRPC.TL_messageMediaPhoto();
         var4.media.photo = var5;
      } else if (var2 instanceof TLRPC.TL_pageBlockVideo) {
         TLRPC.TL_pageBlockVideo var3 = (TLRPC.TL_pageBlockVideo)var2;
         if (this.getDocumentWithId(var1, var3.video_id) == var1.document) {
            return this;
         }

         TLRPC.TL_message var6 = new TLRPC.TL_message();
         var6.media = new TLRPC.TL_messageMediaDocument();
         var6.media.document = this.getDocumentWithId(var1, var3.video_id);
         var4 = var6;
      } else {
         var4 = null;
      }

      var4.message = "";
      var4.realId = this.getId();
      var4.id = Utilities.random.nextInt();
      TLRPC.Message var7 = this.messageOwner;
      var4.date = var7.date;
      var4.to_id = var7.to_id;
      var4.out = var7.out;
      var4.from_id = var7.from_id;
      return new MessageObject(this.currentAccount, var4, false);
   }

   public static int getMessageSize(TLRPC.Message var0) {
      TLRPC.MessageMedia var1 = var0.media;
      TLRPC.Document var2;
      if (var1 instanceof TLRPC.TL_messageMediaWebPage) {
         var2 = var1.webpage.document;
      } else if (var1 instanceof TLRPC.TL_messageMediaGame) {
         var2 = var1.game.document;
      } else if (var1 != null) {
         var2 = var1.document;
      } else {
         var2 = null;
      }

      return var2 != null ? var2.size : 0;
   }

   public static TLRPC.Photo getPhoto(TLRPC.Message var0) {
      TLRPC.MessageMedia var1 = var0.media;
      if (var1 instanceof TLRPC.TL_messageMediaWebPage) {
         return var1.webpage.photo;
      } else {
         TLRPC.Photo var2;
         if (var1 != null) {
            var2 = var1.photo;
         } else {
            var2 = null;
         }

         return var2;
      }
   }

   private TLRPC.Photo getPhotoWithId(TLRPC.WebPage var1, long var2) {
      if (var1 != null && var1.cached_page != null) {
         TLRPC.Photo var4 = var1.photo;
         if (var4 != null && var4.id == var2) {
            return var4;
         }

         for(int var5 = 0; var5 < var1.cached_page.photos.size(); ++var5) {
            var4 = (TLRPC.Photo)var1.cached_page.photos.get(var5);
            if (var4.id == var2) {
               return var4;
            }
         }
      }

      return null;
   }

   public static long getStickerSetId(TLRPC.Document var0) {
      if (var0 == null) {
         return -1L;
      } else {
         for(int var1 = 0; var1 < var0.attributes.size(); ++var1) {
            TLRPC.DocumentAttribute var2 = (TLRPC.DocumentAttribute)var0.attributes.get(var1);
            if (var2 instanceof TLRPC.TL_documentAttributeSticker) {
               TLRPC.InputStickerSet var3 = var2.stickerset;
               if (var3 instanceof TLRPC.TL_inputStickerSetEmpty) {
                  return -1L;
               }

               return var3.id;
            }
         }

         return -1L;
      }
   }

   public static int getUnreadFlags(TLRPC.Message var0) {
      byte var1;
      if (!var0.unread) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      int var2 = var1;
      if (!var0.media_unread) {
         var2 = var1 | 2;
      }

      return var2;
   }

   private String getUserName(TLRPC.User var1, ArrayList var2, int var3) {
      String var4;
      if (var1 == null) {
         var4 = "";
      } else {
         var4 = ContactsController.formatName(var1.first_name, var1.last_name);
      }

      TLRPC.TL_messageEntityMentionName var5;
      if (var3 >= 0) {
         var5 = new TLRPC.TL_messageEntityMentionName();
         var5.user_id = var1.id;
         var5.offset = var3;
         var5.length = var4.length();
         var2.add(var5);
      }

      if (!TextUtils.isEmpty(var1.username)) {
         if (var3 >= 0) {
            var5 = new TLRPC.TL_messageEntityMentionName();
            var5.user_id = var1.id;
            var5.offset = var3 + var4.length() + 2;
            var5.length = var1.username.length() + 1;
            var2.add(var5);
         }

         return String.format("%1$s (@%2$s)", var4, var1.username);
      } else {
         return var4;
      }
   }

   public static int getWebDocumentDuration(TLRPC.WebDocument var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = var0.attributes.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            TLRPC.DocumentAttribute var3 = (TLRPC.DocumentAttribute)var0.attributes.get(var2);
            if (var3 instanceof TLRPC.TL_documentAttributeVideo) {
               return var3.duration;
            }

            if (var3 instanceof TLRPC.TL_documentAttributeAudio) {
               return var3.duration;
            }
         }

         return 0;
      }
   }

   public static int[] getWebDocumentWidthAndHeight(TLRPC.WebDocument var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.attributes.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            TLRPC.DocumentAttribute var3 = (TLRPC.DocumentAttribute)var0.attributes.get(var2);
            if (var3 instanceof TLRPC.TL_documentAttributeImageSize) {
               return new int[]{var3.w, var3.h};
            }

            if (var3 instanceof TLRPC.TL_documentAttributeVideo) {
               return new int[]{var3.w, var3.h};
            }
         }

         return null;
      }
   }

   public static boolean isAnimatedStickerDocument(TLRPC.Document var0) {
      boolean var1;
      if (SharedConfig.showAnimatedStickers && var0 != null && "application/x-tgsticker".equals(var0.mime_type) && !var0.thumbs.isEmpty()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isAnimatedStickerMessage(TLRPC.Message var0) {
      TLRPC.MessageMedia var2 = var0.media;
      boolean var1;
      if (var2 != null && isAnimatedStickerDocument(var2.document)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isContentUnread(TLRPC.Message var0) {
      return var0.media_unread;
   }

   public static boolean isDocumentHasThumb(TLRPC.Document var0) {
      if (var0 != null && !var0.thumbs.isEmpty()) {
         int var1 = var0.thumbs.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            TLRPC.PhotoSize var3 = (TLRPC.PhotoSize)var0.thumbs.get(var2);
            if (var3 != null && !(var3 instanceof TLRPC.TL_photoSizeEmpty) && !(var3.location instanceof TLRPC.TL_fileLocationUnavailable)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isForwardedMessage(TLRPC.Message var0) {
      boolean var1;
      if ((var0.flags & 4) != 0 && var0.fwd_from != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isGameMessage(TLRPC.Message var0) {
      return var0.media instanceof TLRPC.TL_messageMediaGame;
   }

   public static boolean isGifDocument(WebFile var0) {
      boolean var1;
      if (var0 == null || !var0.mime_type.equals("image/gif") && !isNewGifDocument(var0)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isGifDocument(TLRPC.Document var0) {
      boolean var2;
      if (var0 != null) {
         String var1 = var0.mime_type;
         if (var1 != null && (var1.equals("image/gif") || isNewGifDocument(var0))) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public static boolean isGifMessage(TLRPC.Message var0) {
      TLRPC.MessageMedia var2 = var0.media;
      if (var2 instanceof TLRPC.TL_messageMediaWebPage) {
         return isGifDocument(var2.webpage.document);
      } else {
         boolean var1;
         if (var2 != null && isGifDocument(var2.document)) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public static boolean isImageWebDocument(WebFile var0) {
      boolean var1;
      if (var0 != null && !isGifDocument(var0) && var0.mime_type.startsWith("image/")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isInvoiceMessage(TLRPC.Message var0) {
      return var0.media instanceof TLRPC.TL_messageMediaInvoice;
   }

   public static boolean isLiveLocationMessage(TLRPC.Message var0) {
      return var0.media instanceof TLRPC.TL_messageMediaGeoLive;
   }

   public static boolean isLocationMessage(TLRPC.Message var0) {
      TLRPC.MessageMedia var2 = var0.media;
      boolean var1;
      if (!(var2 instanceof TLRPC.TL_messageMediaGeo) && !(var2 instanceof TLRPC.TL_messageMediaGeoLive) && !(var2 instanceof TLRPC.TL_messageMediaVenue)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isMaskDocument(TLRPC.Document var0) {
      if (var0 != null) {
         for(int var1 = 0; var1 < var0.attributes.size(); ++var1) {
            TLRPC.DocumentAttribute var2 = (TLRPC.DocumentAttribute)var0.attributes.get(var1);
            if (var2 instanceof TLRPC.TL_documentAttributeSticker && var2.mask) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isMaskMessage(TLRPC.Message var0) {
      TLRPC.MessageMedia var2 = var0.media;
      boolean var1;
      if (var2 != null && isMaskDocument(var2.document)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isMediaEmpty(TLRPC.Message var0) {
      boolean var1;
      if (var0 != null) {
         TLRPC.MessageMedia var2 = var0.media;
         if (var2 != null && !(var2 instanceof TLRPC.TL_messageMediaEmpty) && !(var2 instanceof TLRPC.TL_messageMediaWebPage)) {
            var1 = false;
            return var1;
         }
      }

      var1 = true;
      return var1;
   }

   public static boolean isMediaEmptyWebpage(TLRPC.Message var0) {
      boolean var1;
      if (var0 != null) {
         TLRPC.MessageMedia var2 = var0.media;
         if (var2 != null && !(var2 instanceof TLRPC.TL_messageMediaEmpty)) {
            var1 = false;
            return var1;
         }
      }

      var1 = true;
      return var1;
   }

   public static boolean isMegagroup(TLRPC.Message var0) {
      boolean var1;
      if ((var0.flags & Integer.MIN_VALUE) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isMusicDocument(TLRPC.Document var0) {
      if (var0 != null) {
         for(int var1 = 0; var1 < var0.attributes.size(); ++var1) {
            TLRPC.DocumentAttribute var2 = (TLRPC.DocumentAttribute)var0.attributes.get(var1);
            if (var2 instanceof TLRPC.TL_documentAttributeAudio) {
               return var2.voice ^ true;
            }
         }

         if (!TextUtils.isEmpty(var0.mime_type)) {
            String var3 = var0.mime_type.toLowerCase();
            if (var3.equals("audio/flac") || var3.equals("audio/ogg") || var3.equals("audio/opus") || var3.equals("audio/x-opus+ogg") || var3.equals("application/octet-stream") && FileLoader.getDocumentFileName(var0).endsWith(".opus")) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isMusicMessage(TLRPC.Message var0) {
      TLRPC.MessageMedia var2 = var0.media;
      if (var2 instanceof TLRPC.TL_messageMediaWebPage) {
         return isMusicDocument(var2.webpage.document);
      } else {
         boolean var1;
         if (var2 != null && isMusicDocument(var2.document)) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public static boolean isNewGifDocument(WebFile var0) {
      if (var0 != null && "video/mp4".equals(var0.mime_type)) {
         int var1 = 0;
         int var2 = 0;

         int var3;
         for(var3 = 0; var1 < var0.attributes.size(); ++var1) {
            TLRPC.DocumentAttribute var4 = (TLRPC.DocumentAttribute)var0.attributes.get(var1);
            if (!(var4 instanceof TLRPC.TL_documentAttributeAnimated) && var4 instanceof TLRPC.TL_documentAttributeVideo) {
               var2 = var4.w;
               var3 = var2;
            }
         }

         if (var2 <= 1280 && var3 <= 1280) {
            return true;
         }
      }

      return false;
   }

   public static boolean isNewGifDocument(TLRPC.Document var0) {
      if (var0 != null && "video/mp4".equals(var0.mime_type)) {
         int var1 = 0;
         boolean var2 = false;
         int var3 = 0;

         int var4;
         boolean var6;
         for(var4 = 0; var1 < var0.attributes.size(); var2 = var6) {
            TLRPC.DocumentAttribute var5 = (TLRPC.DocumentAttribute)var0.attributes.get(var1);
            if (var5 instanceof TLRPC.TL_documentAttributeAnimated) {
               var6 = true;
            } else {
               var6 = var2;
               if (var5 instanceof TLRPC.TL_documentAttributeVideo) {
                  var3 = var5.w;
                  var4 = var3;
                  var6 = var2;
               }
            }

            ++var1;
         }

         if (var2 && var3 <= 1280 && var4 <= 1280) {
            return true;
         }
      }

      return false;
   }

   public static boolean isNewGifMessage(TLRPC.Message var0) {
      TLRPC.MessageMedia var2 = var0.media;
      if (var2 instanceof TLRPC.TL_messageMediaWebPage) {
         return isNewGifDocument(var2.webpage.document);
      } else {
         boolean var1;
         if (var2 != null && isNewGifDocument(var2.document)) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public static boolean isOut(TLRPC.Message var0) {
      return var0.out;
   }

   public static boolean isPhoto(TLRPC.Message var0) {
      TLRPC.MessageMedia var2 = var0.media;
      if (!(var2 instanceof TLRPC.TL_messageMediaWebPage)) {
         return var2 instanceof TLRPC.TL_messageMediaPhoto;
      } else {
         TLRPC.WebPage var3 = var2.webpage;
         boolean var1;
         if (var3.photo instanceof TLRPC.TL_photo && !(var3.document instanceof TLRPC.TL_document)) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public static boolean isRoundVideoDocument(TLRPC.Document var0) {
      if (var0 != null && "video/mp4".equals(var0.mime_type)) {
         int var1 = 0;
         boolean var2 = false;
         int var3 = 0;

         int var4;
         for(var4 = 0; var1 < var0.attributes.size(); ++var1) {
            TLRPC.DocumentAttribute var5 = (TLRPC.DocumentAttribute)var0.attributes.get(var1);
            if (var5 instanceof TLRPC.TL_documentAttributeVideo) {
               var4 = var5.w;
               var2 = var5.round_message;
               var3 = var4;
            }
         }

         if (var2 && var3 <= 1280 && var4 <= 1280) {
            return true;
         }
      }

      return false;
   }

   public static boolean isRoundVideoMessage(TLRPC.Message var0) {
      TLRPC.MessageMedia var2 = var0.media;
      if (var2 instanceof TLRPC.TL_messageMediaWebPage) {
         return isRoundVideoDocument(var2.webpage.document);
      } else {
         boolean var1;
         if (var2 != null && isRoundVideoDocument(var2.document)) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public static boolean isSecretMedia(TLRPC.Message var0) {
      boolean var1 = var0 instanceof TLRPC.TL_message_secret;
      boolean var2 = true;
      boolean var3 = true;
      if (var1) {
         if (!(var0.media instanceof TLRPC.TL_messageMediaPhoto) && !isRoundVideoMessage(var0) && !isVideoMessage(var0) || var0.media.ttl_seconds == 0) {
            var3 = false;
         }

         return var3;
      } else if (!(var0 instanceof TLRPC.TL_message)) {
         return false;
      } else {
         TLRPC.MessageMedia var4 = var0.media;
         if ((var4 instanceof TLRPC.TL_messageMediaPhoto || var4 instanceof TLRPC.TL_messageMediaDocument) && var0.media.ttl_seconds != 0) {
            var3 = var2;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   public static boolean isSecretPhotoOrVideo(TLRPC.Message var0) {
      boolean var1 = var0 instanceof TLRPC.TL_message_secret;
      boolean var2 = true;
      boolean var3 = true;
      if (var1) {
         if (var0.media instanceof TLRPC.TL_messageMediaPhoto || isRoundVideoMessage(var0) || isVideoMessage(var0)) {
            int var4 = var0.ttl;
            if (var4 > 0 && var4 <= 60) {
               return var3;
            }
         }

         var3 = false;
         return var3;
      } else if (!(var0 instanceof TLRPC.TL_message)) {
         return false;
      } else {
         TLRPC.MessageMedia var5 = var0.media;
         if ((var5 instanceof TLRPC.TL_messageMediaPhoto || var5 instanceof TLRPC.TL_messageMediaDocument) && var0.media.ttl_seconds != 0) {
            var3 = var2;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   public static boolean isStickerDocument(TLRPC.Document var0) {
      if (var0 != null) {
         for(int var1 = 0; var1 < var0.attributes.size(); ++var1) {
            if ((TLRPC.DocumentAttribute)var0.attributes.get(var1) instanceof TLRPC.TL_documentAttributeSticker) {
               return "image/webp".equals(var0.mime_type);
            }
         }
      }

      return false;
   }

   public static boolean isStickerMessage(TLRPC.Message var0) {
      TLRPC.MessageMedia var2 = var0.media;
      boolean var1;
      if (var2 != null && isStickerDocument(var2.document)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isUnread(TLRPC.Message var0) {
      return var0.unread;
   }

   public static boolean isVideoDocument(TLRPC.Document var0) {
      boolean var1 = false;
      boolean var2 = var1;
      if (var0 != null) {
         int var3 = 0;
         boolean var4 = false;
         int var5 = 0;
         int var6 = 0;

         boolean var7;
         boolean var11;
         for(var7 = false; var3 < var0.attributes.size(); var7 = var11) {
            TLRPC.DocumentAttribute var8 = (TLRPC.DocumentAttribute)var0.attributes.get(var3);
            int var9;
            int var10;
            if (var8 instanceof TLRPC.TL_documentAttributeVideo) {
               if (var8.round_message) {
                  return false;
               }

               var9 = var8.w;
               var10 = var8.h;
               var11 = true;
            } else {
               var9 = var5;
               var10 = var6;
               var11 = var7;
               if (var8 instanceof TLRPC.TL_documentAttributeAnimated) {
                  var4 = true;
                  var11 = var7;
                  var10 = var6;
                  var9 = var5;
               }
            }

            ++var3;
            var5 = var9;
            var6 = var10;
         }

         boolean var12 = var4;
         if (var4) {
            label55: {
               if (var5 <= 1280) {
                  var12 = var4;
                  if (var6 <= 1280) {
                     break label55;
                  }
               }

               var12 = false;
            }
         }

         var4 = var7;
         if (SharedConfig.streamMkv) {
            var4 = var7;
            if (!var7) {
               var4 = var7;
               if ("video/x-matroska".equals(var0.mime_type)) {
                  var4 = true;
               }
            }
         }

         var2 = var1;
         if (var4) {
            var2 = var1;
            if (!var12) {
               var2 = true;
            }
         }
      }

      return var2;
   }

   public static boolean isVideoMessage(TLRPC.Message var0) {
      TLRPC.MessageMedia var2 = var0.media;
      if (var2 instanceof TLRPC.TL_messageMediaWebPage) {
         return isVideoDocument(var2.webpage.document);
      } else {
         boolean var1;
         if (var2 != null && isVideoDocument(var2.document)) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public static boolean isVideoWebDocument(WebFile var0) {
      boolean var1;
      if (var0 != null && var0.mime_type.startsWith("video/")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isVoiceDocument(TLRPC.Document var0) {
      if (var0 != null) {
         for(int var1 = 0; var1 < var0.attributes.size(); ++var1) {
            TLRPC.DocumentAttribute var2 = (TLRPC.DocumentAttribute)var0.attributes.get(var1);
            if (var2 instanceof TLRPC.TL_documentAttributeAudio) {
               return var2.voice;
            }
         }
      }

      return false;
   }

   public static boolean isVoiceMessage(TLRPC.Message var0) {
      TLRPC.MessageMedia var2 = var0.media;
      if (var2 instanceof TLRPC.TL_messageMediaWebPage) {
         return isVoiceDocument(var2.webpage.document);
      } else {
         boolean var1;
         if (var2 != null && isVoiceDocument(var2.document)) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public static boolean isVoiceWebDocument(WebFile var0) {
      boolean var1;
      if (var0 != null && var0.mime_type.equals("audio/ogg")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean needDrawAvatarInternal() {
      boolean var2;
      if ((!this.isFromChat() || !this.isFromUser()) && this.eventId == 0L) {
         TLRPC.MessageFwdHeader var1 = this.messageOwner.fwd_from;
         if (var1 == null || var1.saved_from_peer == null) {
            var2 = false;
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   public static void setUnreadFlags(TLRPC.Message var0, int var1) {
      boolean var2 = false;
      boolean var3;
      if ((var1 & 1) == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      var0.unread = var3;
      var3 = var2;
      if ((var1 & 2) == 0) {
         var3 = true;
      }

      var0.media_unread = var3;
   }

   public static boolean shouldEncryptPhotoOrVideo(TLRPC.Message var0) {
      boolean var1 = var0 instanceof TLRPC.TL_message_secret;
      boolean var2 = true;
      boolean var3 = true;
      if (var1) {
         if (var0.media instanceof TLRPC.TL_messageMediaPhoto || isVideoMessage(var0)) {
            int var4 = var0.ttl;
            if (var4 > 0 && var4 <= 60) {
               return var3;
            }
         }

         var3 = false;
         return var3;
      } else {
         TLRPC.MessageMedia var5 = var0.media;
         if ((var5 instanceof TLRPC.TL_messageMediaPhoto || var5 instanceof TLRPC.TL_messageMediaDocument) && var0.media.ttl_seconds != 0) {
            var3 = var2;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   private static void updatePhotoSizeLocations(ArrayList var0, ArrayList var1) {
      int var2 = var0.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         TLRPC.PhotoSize var4 = (TLRPC.PhotoSize)var0.get(var3);
         int var5 = var1.size();

         for(int var6 = 0; var6 < var5; ++var6) {
            TLRPC.PhotoSize var7 = (TLRPC.PhotoSize)var1.get(var6);
            if (!(var7 instanceof TLRPC.TL_photoSizeEmpty) && !(var7 instanceof TLRPC.TL_photoCachedSize) && var7.type.equals(var4.type)) {
               var4.location = var7.location;
               break;
            }
         }
      }

   }

   public static void updatePollResults(TLRPC.TL_messageMediaPoll var0, TLRPC.TL_pollResults var1) {
      if ((var1.flags & 2) != 0) {
         TLRPC.TL_pollResults var2 = null;
         boolean var3 = var1.min;
         byte var4 = 0;
         byte[] var5 = (byte[])var2;
         int var7;
         int var8;
         if (var3) {
            ArrayList var6 = var0.results.results;
            var5 = (byte[])var2;
            if (var6 != null) {
               var7 = var6.size();
               var8 = 0;

               while(true) {
                  var5 = (byte[])var2;
                  if (var8 >= var7) {
                     break;
                  }

                  TLRPC.TL_pollAnswerVoters var11 = (TLRPC.TL_pollAnswerVoters)var0.results.results.get(var8);
                  if (var11.chosen) {
                     var5 = var11.option;
                     break;
                  }

                  ++var8;
               }
            }
         }

         var2 = var0.results;
         var2.results = var1.results;
         if (var5 != null) {
            var7 = var2.results.size();

            for(var8 = var4; var8 < var7; ++var8) {
               TLRPC.TL_pollAnswerVoters var10 = (TLRPC.TL_pollAnswerVoters)var0.results.results.get(var8);
               if (Arrays.equals(var10.option, var5)) {
                  var10.chosen = true;
                  break;
               }
            }
         }

         TLRPC.TL_pollResults var12 = var0.results;
         var12.flags |= 2;
      }

      if ((var1.flags & 4) != 0) {
         TLRPC.TL_pollResults var9 = var0.results;
         var9.total_voters = var1.total_voters;
         var9.flags |= 4;
      }

   }

   public boolean addEntitiesToText(CharSequence var1, boolean var2, boolean var3) {
      return addEntitiesToText(var1, this.messageOwner.entities, this.isOutOwner(), this.type, true, var2, var3);
   }

   public void applyMediaExistanceFlags(int var1) {
      if (var1 == -1) {
         this.checkMediaExistance();
      } else {
         boolean var2 = false;
         boolean var3;
         if ((var1 & 1) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.attachPathExists = var3;
         var3 = var2;
         if ((var1 & 2) != 0) {
            var3 = true;
         }

         this.mediaExists = var3;
      }

   }

   public void applyNewText() {
      if (!TextUtils.isEmpty(this.messageOwner.message)) {
         boolean var1 = this.isFromUser();
         int[] var2 = null;
         TLRPC.User var3;
         if (var1) {
            var3 = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.from_id);
         } else {
            var3 = null;
         }

         TLRPC.Message var4 = this.messageOwner;
         this.messageText = var4.message;
         TextPaint var5;
         if (var4.media instanceof TLRPC.TL_messageMediaGame) {
            var5 = Theme.chat_msgGameTextPaint;
         } else {
            var5 = Theme.chat_msgTextPaint;
         }

         if (SharedConfig.allowBigEmoji) {
            var2 = new int[1];
         }

         this.messageText = Emoji.replaceEmoji(this.messageText, var5.getFontMetricsInt(), AndroidUtilities.dp(20.0F), false, var2);
         this.checkEmojiOnly(var2);
         this.generateLayout(var3);
      }
   }

   public boolean canDeleteMessage(TLRPC.Chat var1) {
      boolean var2;
      if (this.eventId == 0L && canDeleteMessage(this.currentAccount, this.messageOwner, var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean canEditMedia() {
      boolean var1 = this.isSecretMedia();
      boolean var2 = false;
      if (var1) {
         return false;
      } else {
         TLRPC.MessageMedia var3 = this.messageOwner.media;
         if (var3 instanceof TLRPC.TL_messageMediaPhoto) {
            return true;
         } else {
            var1 = var2;
            if (var3 instanceof TLRPC.TL_messageMediaDocument) {
               var1 = var2;
               if (!this.isVoice()) {
                  var1 = var2;
                  if (!this.isSticker()) {
                     var1 = var2;
                     if (!this.isAnimatedSticker()) {
                        var1 = var2;
                        if (!this.isRoundVideo()) {
                           var1 = true;
                        }
                     }
                  }
               }
            }

            return var1;
         }
      }
   }

   public boolean canEditMessage(TLRPC.Chat var1) {
      return canEditMessage(this.currentAccount, this.messageOwner, var1);
   }

   public boolean canEditMessageAnytime(TLRPC.Chat var1) {
      return canEditMessageAnytime(this.currentAccount, this.messageOwner, var1);
   }

   public boolean canForwardMessage() {
      boolean var1;
      if (!(this.messageOwner instanceof TLRPC.TL_message_secret) && !this.needDrawBluredPreview() && !this.isLiveLocation() && this.type != 16) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean canPreviewDocument() {
      return canPreviewDocument(this.getDocument());
   }

   public boolean canStreamVideo() {
      TLRPC.Document var1 = this.getDocument();
      if (var1 != null && !(var1 instanceof TLRPC.TL_documentEncrypted)) {
         if (SharedConfig.streamAllVideo) {
            return true;
         }

         for(int var2 = 0; var2 < var1.attributes.size(); ++var2) {
            TLRPC.DocumentAttribute var3 = (TLRPC.DocumentAttribute)var1.attributes.get(var2);
            if (var3 instanceof TLRPC.TL_documentAttributeVideo) {
               return var3.supports_streaming;
            }
         }

         if (SharedConfig.streamMkv && "video/x-matroska".equals(var1.mime_type)) {
            return true;
         }
      }

      return false;
   }

   public void checkForScam() {
   }

   public boolean checkLayout() {
      if (this.type == 0 && this.messageOwner.to_id != null) {
         CharSequence var1 = this.messageText;
         if (var1 != null && var1.length() != 0) {
            if (this.layoutCreated) {
               int var2;
               if (AndroidUtilities.isTablet()) {
                  var2 = AndroidUtilities.getMinTabletSide();
               } else {
                  var2 = AndroidUtilities.displaySize.x;
               }

               if (Math.abs(this.generatedWithMinSize - var2) > AndroidUtilities.dp(52.0F) || this.generatedWithDensity != AndroidUtilities.density) {
                  this.layoutCreated = false;
               }
            }

            if (!this.layoutCreated) {
               this.layoutCreated = true;
               boolean var3 = this.isFromUser();
               int[] var4 = null;
               TLRPC.User var6;
               if (var3) {
                  var6 = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.from_id);
               } else {
                  var6 = null;
               }

               TextPaint var5;
               if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                  var5 = Theme.chat_msgGameTextPaint;
               } else {
                  var5 = Theme.chat_msgTextPaint;
               }

               if (SharedConfig.allowBigEmoji) {
                  var4 = new int[1];
               }

               this.messageText = Emoji.replaceEmoji(this.messageText, var5.getFontMetricsInt(), AndroidUtilities.dp(20.0F), false, var4);
               this.checkEmojiOnly(var4);
               this.generateLayout(var6);
               return true;
            }
         }
      }

      return false;
   }

   public void checkMediaExistance() {
      this.attachPathExists = false;
      this.mediaExists = false;
      int var1 = this.type;
      if (var1 == 1) {
         if (FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize()) != null) {
            File var2 = FileLoader.getPathToMessage(this.messageOwner);
            if (this.needDrawBluredPreview()) {
               StringBuilder var3 = new StringBuilder();
               var3.append(var2.getAbsolutePath());
               var3.append(".enc");
               this.mediaExists = (new File(var3.toString())).exists();
            }

            if (!this.mediaExists) {
               this.mediaExists = var2.exists();
            }
         }
      } else if (var1 != 8 && var1 != 3 && var1 != 9 && var1 != 2 && var1 != 14 && var1 != 5) {
         TLRPC.Document var7 = this.getDocument();
         if (var7 != null) {
            if (this.isWallpaper()) {
               this.mediaExists = FileLoader.getPathToAttach(var7, true).exists();
            } else {
               this.mediaExists = FileLoader.getPathToAttach(var7).exists();
            }
         } else if (this.type == 0) {
            TLRPC.PhotoSize var8 = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize());
            if (var8 == null) {
               return;
            }

            if (var8 != null) {
               this.mediaExists = FileLoader.getPathToAttach(var8, true).exists();
            }
         }
      } else {
         String var5 = this.messageOwner.attachPath;
         if (var5 != null && var5.length() > 0) {
            this.attachPathExists = (new File(this.messageOwner.attachPath)).exists();
         }

         if (!this.attachPathExists) {
            File var6 = FileLoader.getPathToMessage(this.messageOwner);
            if (this.type == 3 && this.needDrawBluredPreview()) {
               StringBuilder var4 = new StringBuilder();
               var4.append(var6.getAbsolutePath());
               var4.append(".enc");
               this.mediaExists = (new File(var4.toString())).exists();
            }

            if (!this.mediaExists) {
               this.mediaExists = var6.exists();
            }
         }
      }

   }

   public void createMessageSendInfo() {
      TLRPC.Message var1 = this.messageOwner;
      if (var1.message != null && (var1.id < 0 || this.isEditing())) {
         HashMap var5 = this.messageOwner.params;
         if (var5 != null) {
            String var6 = (String)var5.get("ve");
            if (var6 != null && (this.isVideo() || this.isNewGif() || this.isRoundVideo())) {
               this.videoEditedInfo = new VideoEditedInfo();
               if (!this.videoEditedInfo.parseString(var6)) {
                  this.videoEditedInfo = null;
               } else {
                  this.videoEditedInfo.roundVideo = this.isRoundVideo();
               }
            }

            var1 = this.messageOwner;
            if (var1.send_state == 3) {
               var6 = (String)var1.params.get("prevMedia");
               if (var6 != null) {
                  SerializedData var2 = new SerializedData(Base64.decode(var6, 0));
                  this.previousMedia = TLRPC.MessageMedia.TLdeserialize(var2, var2.readInt32(false), false);
                  this.previousCaption = var2.readString(false);
                  this.previousAttachPath = var2.readString(false);
                  int var3 = var2.readInt32(false);
                  this.previousCaptionEntities = new ArrayList(var3);

                  for(int var4 = 0; var4 < var3; ++var4) {
                     TLRPC.MessageEntity var7 = TLRPC.MessageEntity.TLdeserialize(var2, var2.readInt32(false), false);
                     this.previousCaptionEntities.add(var7);
                  }

                  var2.cleanup();
               }
            }
         }
      }

   }

   public boolean equals(MessageObject var1) {
      boolean var2;
      if (this.getId() == var1.getId() && this.getDialogId() == var1.getDialogId()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void generateCaption() {
      if (this.caption == null && !this.isRoundVideo() && !this.isMediaEmpty()) {
         TLRPC.Message var1 = this.messageOwner;
         if (!(var1.media instanceof TLRPC.TL_messageMediaGame) && !TextUtils.isEmpty(var1.message)) {
            this.caption = Emoji.replaceEmoji(this.messageOwner.message, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
            var1 = this.messageOwner;
            boolean var2;
            if (var1.send_state != 0) {
               int var7 = 0;

               while(true) {
                  if (var7 >= this.messageOwner.entities.size()) {
                     var2 = false;
                     break;
                  }

                  if (!(this.messageOwner.entities.get(var7) instanceof TLRPC.TL_inputMessageEntityMentionName)) {
                     var2 = true;
                     break;
                  }

                  ++var7;
               }
            } else {
               var2 = var1.entities.isEmpty() ^ true;
            }

            boolean var3;
            label73: {
               label72: {
                  if (!var2) {
                     if (this.eventId != 0L) {
                        break label72;
                     }

                     TLRPC.MessageMedia var6 = this.messageOwner.media;
                     if (var6 instanceof TLRPC.TL_messageMediaPhoto_old || var6 instanceof TLRPC.TL_messageMediaPhoto_layer68 || var6 instanceof TLRPC.TL_messageMediaPhoto_layer74 || var6 instanceof TLRPC.TL_messageMediaDocument_old || var6 instanceof TLRPC.TL_messageMediaDocument_layer68 || var6 instanceof TLRPC.TL_messageMediaDocument_layer74 || this.isOut() && this.messageOwner.send_state != 0 || this.messageOwner.id < 0) {
                        break label72;
                     }
                  }

                  var3 = false;
                  break label73;
               }

               var3 = true;
            }

            if (var3) {
               if (containsUrls(this.caption)) {
                  try {
                     Linkify.addLinks((Spannable)this.caption, 5);
                  } catch (Exception var5) {
                     FileLog.e((Throwable)var5);
                  }
               }

               addUsernamesAndHashtags(this.isOutOwner(), this.caption, true, 0);
            } else {
               try {
                  Linkify.addLinks((Spannable)this.caption, 4);
               } catch (Throwable var4) {
                  FileLog.e(var4);
               }
            }

            this.addEntitiesToText(this.caption, var3);
         }
      }

   }

   public void generateGameMessageText(TLRPC.User var1) {
      TLRPC.User var2 = var1;
      if (var1 == null) {
         var2 = var1;
         if (this.messageOwner.from_id > 0) {
            var2 = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.from_id);
         }
      }

      Object var3 = null;
      MessageObject var4 = this.replyMessageObject;
      TLRPC.TL_game var5 = (TLRPC.TL_game)var3;
      if (var4 != null) {
         TLRPC.MessageMedia var6 = var4.messageOwner.media;
         var5 = (TLRPC.TL_game)var3;
         if (var6 != null) {
            TLRPC.TL_game var7 = var6.game;
            var5 = (TLRPC.TL_game)var3;
            if (var7 != null) {
               var5 = var7;
            }
         }
      }

      if (var5 == null) {
         if (var2 != null && var2.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            this.messageText = LocaleController.formatString("ActionYouScored", 2131558553, LocaleController.formatPluralString("Points", this.messageOwner.action.score));
         } else {
            this.messageText = this.replaceWithLink(LocaleController.formatString("ActionUserScored", 2131558544, LocaleController.formatPluralString("Points", this.messageOwner.action.score)), "un1", var2);
         }
      } else {
         if (var2 != null && var2.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            this.messageText = LocaleController.formatString("ActionYouScoredInGame", 2131558554, LocaleController.formatPluralString("Points", this.messageOwner.action.score));
         } else {
            this.messageText = this.replaceWithLink(LocaleController.formatString("ActionUserScoredInGame", 2131558545, LocaleController.formatPluralString("Points", this.messageOwner.action.score)), "un1", var2);
         }

         this.messageText = this.replaceWithLink(this.messageText, "un2", var5);
      }

   }

   public void generateLayout(TLRPC.User var1) {
      if (this.type == 0 && this.messageOwner.to_id != null && !TextUtils.isEmpty(this.messageText)) {
         this.generateLinkDescription();
         this.textLayoutBlocks = new ArrayList();
         this.textWidth = 0;
         TLRPC.Message var45 = this.messageOwner;
         boolean var2;
         int var46;
         if (var45.send_state != 0) {
            var46 = 0;

            while(true) {
               if (var46 >= this.messageOwner.entities.size()) {
                  var2 = false;
                  break;
               }

               if (!(this.messageOwner.entities.get(var46) instanceof TLRPC.TL_inputMessageEntityMentionName)) {
                  var2 = true;
                  break;
               }

               ++var46;
            }
         } else {
            var2 = var45.entities.isEmpty() ^ true;
         }

         boolean var3;
         label334: {
            label333: {
               if (!var2) {
                  if (this.eventId != 0L) {
                     break label333;
                  }

                  var45 = this.messageOwner;
                  if (var45 instanceof TLRPC.TL_message_old || var45 instanceof TLRPC.TL_message_old2 || var45 instanceof TLRPC.TL_message_old3 || var45 instanceof TLRPC.TL_message_old4 || var45 instanceof TLRPC.TL_messageForwarded_old || var45 instanceof TLRPC.TL_messageForwarded_old2 || var45 instanceof TLRPC.TL_message_secret || var45.media instanceof TLRPC.TL_messageMediaInvoice || this.isOut() && this.messageOwner.send_state != 0) {
                     break label333;
                  }

                  var45 = this.messageOwner;
                  if (var45.id < 0 || var45.media instanceof TLRPC.TL_messageMediaUnsupported) {
                     break label333;
                  }
               }

               var3 = false;
               break label334;
            }

            var3 = true;
         }

         if (var3) {
            addLinks(this.isOutOwner(), this.messageText);
         } else {
            CharSequence var47 = this.messageText;
            if (var47 instanceof Spannable && var47.length() < 1000) {
               try {
                  Linkify.addLinks((Spannable)this.messageText, 4);
               } catch (Throwable var32) {
                  FileLog.e(var32);
               }
            }
         }

         var3 = this.addEntitiesToText(this.messageText, var3);
         int var4 = this.getMaxMessageTextWidth();
         TextPaint var48;
         if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
            var48 = Theme.chat_msgGameTextPaint;
         } else {
            var48 = Theme.chat_msgTextPaint;
         }

         TextPaint var5 = var48;

         Exception var10000;
         boolean var10001;
         StaticLayout var50;
         label389: {
            label355: {
               try {
                  if (VERSION.SDK_INT >= 24) {
                     var50 = Builder.obtain(this.messageText, 0, this.messageText.length(), var5, var4).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Alignment.ALIGN_NORMAL).build();
                     break label389;
                  }
               } catch (Exception var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label355;
               }

               try {
                  var50 = new StaticLayout(this.messageText, var5, var4, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                  break label389;
               } catch (Exception var43) {
                  var10000 = var43;
                  var10001 = false;
               }
            }

            Exception var49 = var10000;
            FileLog.e((Throwable)var49);
            return;
         }

         this.textHeight = var50.getHeight();
         this.linesCount = var50.getLineCount();
         int var6;
         if (VERSION.SDK_INT >= 24) {
            var6 = 1;
         } else {
            var6 = (int)Math.ceil((double)((float)this.linesCount / 10.0F));
         }

         var46 = 0;
         int var7 = 0;

         int var9;
         for(float var8 = 0.0F; var7 < var6; var6 = var9) {
            if (VERSION.SDK_INT >= 24) {
               var9 = this.linesCount;
            } else {
               var9 = Math.min(10, this.linesCount - var46);
            }

            label367: {
               Object var10;
               int var11;
               int var12;
               float var17;
               label300: {
                  label382: {
                     var10 = new MessageObject.TextLayoutBlock();
                     if (var6 == 1) {
                        ((MessageObject.TextLayoutBlock)var10).textLayout = var50;
                        ((MessageObject.TextLayoutBlock)var10).textYOffset = 0.0F;
                        ((MessageObject.TextLayoutBlock)var10).charactersOffset = 0;
                        var11 = this.emojiOnlyCount;
                        if (var11 != 0) {
                           if (var11 != 1) {
                              if (var11 != 2) {
                                 if (var11 == 3) {
                                    this.textHeight -= AndroidUtilities.dp(4.2F);
                                    ((MessageObject.TextLayoutBlock)var10).textYOffset -= (float)AndroidUtilities.dp(4.2F);
                                 }
                              } else {
                                 this.textHeight -= AndroidUtilities.dp(4.5F);
                                 ((MessageObject.TextLayoutBlock)var10).textYOffset -= (float)AndroidUtilities.dp(4.5F);
                              }
                           } else {
                              this.textHeight -= AndroidUtilities.dp(5.3F);
                              ((MessageObject.TextLayoutBlock)var10).textYOffset -= (float)AndroidUtilities.dp(5.3F);
                           }
                        }

                        ((MessageObject.TextLayoutBlock)var10).height = this.textHeight;
                     } else {
                        var11 = var50.getLineStart(var46);
                        var12 = var50.getLineEnd(var46 + var9 - 1);
                        if (var12 < var11) {
                           var9 = var6;
                           break label382;
                        }

                        Object var52;
                        label294: {
                           Exception var51;
                           label293: {
                              label369: {
                                 label291: {
                                    label370: {
                                       ((MessageObject.TextLayoutBlock)var10).charactersOffset = var11;
                                       ((MessageObject.TextLayoutBlock)var10).charactersEnd = var12;
                                       if (var3) {
                                          try {
                                             if (VERSION.SDK_INT >= 24) {
                                                ((MessageObject.TextLayoutBlock)var10).textLayout = Builder.obtain(this.messageText, var11, var12, var5, var4 + AndroidUtilities.dp(2.0F)).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Alignment.ALIGN_NORMAL).build();
                                                break label291;
                                             }
                                          } catch (Exception var42) {
                                             var10000 = var42;
                                             var10001 = false;
                                             break label370;
                                          }
                                       }

                                       StaticLayout var13;
                                       CharSequence var14;
                                       Alignment var15;
                                       try {
                                          var13 = new StaticLayout;
                                          var14 = this.messageText;
                                          var15 = Alignment.ALIGN_NORMAL;
                                       } catch (Exception var41) {
                                          var10000 = var41;
                                          var10001 = false;
                                          break label370;
                                       }

                                       try {
                                          var13.<init>(var14, var11, var12, var5, var4, var15, 1.0F, 0.0F, false);
                                          ((MessageObject.TextLayoutBlock)var10).textLayout = var13;
                                          break label291;
                                       } catch (Exception var38) {
                                          var51 = var38;
                                          var50 = var50;
                                          break label369;
                                       }
                                    }

                                    var51 = var10000;
                                    var9 = var6;
                                    break label293;
                                 }

                                 StaticLayout var16 = var50;
                                 var52 = var10;
                                 var11 = var46;

                                 label371: {
                                    try {
                                       ((MessageObject.TextLayoutBlock)var52).textYOffset = (float)var16.getLineTop(var11);
                                    } catch (Exception var40) {
                                       var10000 = var40;
                                       var10001 = false;
                                       break label371;
                                    }

                                    if (var7 != 0) {
                                       try {
                                          ((MessageObject.TextLayoutBlock)var52).height = (int)(((MessageObject.TextLayoutBlock)var52).textYOffset - var8);
                                       } catch (Exception var39) {
                                          var10000 = var39;
                                          var10001 = false;
                                          break label371;
                                       }
                                    }

                                    try {
                                       ((MessageObject.TextLayoutBlock)var52).height = Math.max(((MessageObject.TextLayoutBlock)var52).height, ((MessageObject.TextLayoutBlock)var52).textLayout.getLineBottom(((MessageObject.TextLayoutBlock)var52).textLayout.getLineCount() - 1));
                                       var17 = ((MessageObject.TextLayoutBlock)var52).textYOffset;
                                       break label294;
                                    } catch (Exception var37) {
                                       var10000 = var37;
                                       var10001 = false;
                                    }
                                 }

                                 var51 = var10000;
                                 var46 = var46;
                                 var50 = var50;
                              }

                              var9 = var6;
                           }

                           FileLog.e((Throwable)var51);
                           break label382;
                        }

                        var11 = var6;
                        var8 = var17;
                        var6 = var6;
                        if (var7 == var11 - 1) {
                           var6 = Math.max(var9, ((MessageObject.TextLayoutBlock)var10).textLayout.getLineCount());

                           try {
                              this.textHeight = Math.max(this.textHeight, (int)(((MessageObject.TextLayoutBlock)var52).textYOffset + (float)((MessageObject.TextLayoutBlock)var52).textLayout.getHeight()));
                           } catch (Exception var36) {
                              FileLog.e((Throwable)var36);
                              var9 = var11;
                              var8 = var17;
                              break label300;
                           }

                           var8 = var17;
                           var9 = var11;
                           break label300;
                        }
                     }

                     var11 = var9;
                     var9 = var6;
                     var6 = var11;
                     break label300;
                  }

                  var6 = var46;
                  var46 = var4;
                  break label367;
               }

               Object var53 = var10;
               var11 = var46;
               this.textLayoutBlocks.add(var10);

               float var19;
               label373: {
                  label374: {
                     try {
                        var19 = ((MessageObject.TextLayoutBlock)var53).textLayout.getLineLeft(var6 - 1);
                     } catch (Exception var35) {
                        var10 = var35;
                        break label374;
                     }

                     if (var7 != 0) {
                        var17 = var19;
                        break label373;
                     }

                     var17 = var19;
                     if (var19 < 0.0F) {
                        break label373;
                     }

                     try {
                        this.textXOffset = var19;
                     } catch (Exception var34) {
                        var10 = var34;
                        break label374;
                     }

                     var17 = var19;
                     break label373;
                  }

                  if (var7 == 0) {
                     this.textXOffset = 0.0F;
                  }

                  FileLog.e((Throwable)var10);
                  var17 = 0.0F;
               }

               try {
                  var19 = ((MessageObject.TextLayoutBlock)var53).textLayout.getLineWidth(var6 - 1);
               } catch (Exception var31) {
                  FileLog.e((Throwable)var31);
                  var19 = 0.0F;
               }

               var12 = (int)Math.ceil((double)var19);
               var46 = var12;
               if (var12 > var4 + 80) {
                  var46 = var4;
               }

               int var20 = var9 - 1;
               if (var7 == var20) {
                  this.lastLineWidth = var46;
               }

               int var21 = (int)Math.ceil((double)(var19 + var17));
               int var23;
               if (var6 > 1) {
                  int var22 = var21;
                  var12 = var46;
                  var19 = 0.0F;
                  var23 = 0;
                  var2 = false;

                  boolean var26;
                  for(var17 = 0.0F; var23 < var6; var2 = var26) {
                     float var24;
                     try {
                        var24 = ((MessageObject.TextLayoutBlock)var53).textLayout.getLineWidth(var23);
                     } catch (Exception var30) {
                        FileLog.e((Throwable)var30);
                        var24 = 0.0F;
                     }

                     float var25 = var24;
                     if (var24 > (float)(var4 + 20)) {
                        var25 = (float)var4;
                     }

                     try {
                        var24 = ((MessageObject.TextLayoutBlock)var53).textLayout.getLineLeft(var23);
                     } catch (Exception var29) {
                        FileLog.e((Throwable)var29);
                        var24 = 0.0F;
                     }

                     if (var24 > 0.0F) {
                        this.textXOffset = Math.min(this.textXOffset, var24);
                        ((MessageObject.TextLayoutBlock)var53).directionFlags = (byte)((byte)(((MessageObject.TextLayoutBlock)var53).directionFlags | 1));
                        this.hasRtl = true;
                     } else {
                        ((MessageObject.TextLayoutBlock)var53).directionFlags = (byte)((byte)(((MessageObject.TextLayoutBlock)var53).directionFlags | 2));
                     }

                     var26 = var2;
                     if (!var2) {
                        var26 = var2;
                        if (var24 == 0.0F) {
                           label380: {
                              label227: {
                                 int var27;
                                 try {
                                    var27 = ((MessageObject.TextLayoutBlock)var53).textLayout.getParagraphDirection(var23);
                                 } catch (Exception var33) {
                                    break label227;
                                 }

                                 var26 = var2;
                                 if (var27 != 1) {
                                    break label380;
                                 }
                              }

                              var26 = true;
                           }
                        }
                     }

                     var19 = Math.max(var19, var25);
                     var24 += var25;
                     var17 = Math.max(var17, var24);
                     var12 = Math.max(var12, (int)Math.ceil((double)var25));
                     var22 = Math.max(var22, (int)Math.ceil((double)var24));
                     ++var23;
                  }

                  if (var2) {
                     var19 = var17;
                     if (var7 == var20) {
                        this.lastLineWidth = var21;
                        var19 = var17;
                     }
                  } else if (var7 == var20) {
                     this.lastLineWidth = var12;
                  }

                  this.textWidth = Math.max(this.textWidth, (int)Math.ceil((double)var19));
                  var46 = var4;
               } else {
                  if (var17 > 0.0F) {
                     this.textXOffset = Math.min(this.textXOffset, var17);
                     if (this.textXOffset == 0.0F) {
                        var46 = (int)((float)var46 + var17);
                     }

                     boolean var28;
                     if (var9 != 1) {
                        var28 = true;
                     } else {
                        var28 = false;
                     }

                     this.hasRtl = var28;
                     ((MessageObject.TextLayoutBlock)var10).directionFlags = (byte)((byte)(((MessageObject.TextLayoutBlock)var10).directionFlags | 1));
                     var12 = var46;
                  } else {
                     ((MessageObject.TextLayoutBlock)var10).directionFlags = (byte)((byte)(((MessageObject.TextLayoutBlock)var10).directionFlags | 2));
                     var12 = var46;
                  }

                  var23 = this.textWidth;
                  var46 = var4;
                  this.textWidth = Math.max(var23, Math.min(var4, var12));
               }

               var6 += var11;
            }

            ++var7;
            var4 = var46;
            var46 = var6;
         }

      }
   }

   public void generateLinkDescription() {
      if (this.linkDescription == null) {
         byte var3;
         label60: {
            label64: {
               TLRPC.MessageMedia var1 = this.messageOwner.media;
               if (var1 instanceof TLRPC.TL_messageMediaWebPage) {
                  TLRPC.WebPage var5 = var1.webpage;
                  if (var5 instanceof TLRPC.TL_webPage && var5.description != null) {
                     this.linkDescription = Factory.getInstance().newSpannable(this.messageOwner.media.webpage.description);
                     String var2 = this.messageOwner.media.webpage.site_name;
                     String var6 = var2;
                     if (var2 != null) {
                        var6 = var2.toLowerCase();
                     }

                     if ("instagram".equals(var6)) {
                        var3 = 1;
                        break label60;
                     }

                     if ("twitter".equals(var6)) {
                        var3 = 2;
                        break label60;
                     }
                     break label64;
                  }
               }

               var1 = this.messageOwner.media;
               if (var1 instanceof TLRPC.TL_messageMediaGame && var1.game.description != null) {
                  this.linkDescription = Factory.getInstance().newSpannable(this.messageOwner.media.game.description);
               } else {
                  var1 = this.messageOwner.media;
                  if (var1 instanceof TLRPC.TL_messageMediaInvoice && var1.description != null) {
                     this.linkDescription = Factory.getInstance().newSpannable(this.messageOwner.media.description);
                  }
               }
            }

            var3 = 0;
         }

         if (!TextUtils.isEmpty(this.linkDescription)) {
            if (containsUrls(this.linkDescription)) {
               try {
                  Linkify.addLinks((Spannable)this.linkDescription, 1);
               } catch (Exception var4) {
                  FileLog.e((Throwable)var4);
               }
            }

            this.linkDescription = Emoji.replaceEmoji(this.linkDescription, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
            if (var3 != 0) {
               CharSequence var7 = this.linkDescription;
               if (!(var7 instanceof Spannable)) {
                  this.linkDescription = new SpannableStringBuilder(var7);
               }

               addUsernamesAndHashtags(this.isOutOwner(), this.linkDescription, false, var3);
            }
         }

      }
   }

   public void generatePaymentSentMessageText(TLRPC.User var1) {
      TLRPC.User var2 = var1;
      if (var1 == null) {
         var2 = MessagesController.getInstance(this.currentAccount).getUser((int)this.getDialogId());
      }

      String var4;
      if (var2 != null) {
         var4 = UserObject.getFirstName(var2);
      } else {
         var4 = "";
      }

      MessageObject var5 = this.replyMessageObject;
      if (var5 != null && var5.messageOwner.media instanceof TLRPC.TL_messageMediaInvoice) {
         LocaleController var6 = LocaleController.getInstance();
         TLRPC.MessageAction var8 = this.messageOwner.action;
         this.messageText = LocaleController.formatString("PaymentSuccessfullyPaid", 2131560404, var6.formatCurrencyString(var8.total_amount, var8.currency), var4, this.replyMessageObject.messageOwner.media.title);
      } else {
         LocaleController var7 = LocaleController.getInstance();
         TLRPC.MessageAction var3 = this.messageOwner.action;
         this.messageText = LocaleController.formatString("PaymentSuccessfullyPaidNoItem", 2131560405, var7.formatCurrencyString(var3.total_amount, var3.currency), var4);
      }

   }

   public void generatePinMessageText(TLRPC.User var1, TLRPC.Chat var2) {
      Object var3 = var1;
      Object var4 = var2;
      if (var1 == null) {
         var3 = var1;
         var4 = var2;
         if (var2 == null) {
            if (this.messageOwner.from_id > 0) {
               var1 = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.from_id);
            }

            var3 = var1;
            var4 = var2;
            if (var1 == null) {
               var4 = MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.to_id.channel_id);
               var3 = var1;
            }
         }
      }

      MessageObject var5 = this.replyMessageObject;
      String var7;
      if (var5 != null) {
         TLRPC.Message var6 = var5.messageOwner;
         if (!(var6 instanceof TLRPC.TL_messageEmpty) && !(var6.action instanceof TLRPC.TL_messageActionHistoryClear)) {
            if (var5.isMusic()) {
               var7 = LocaleController.getString("ActionPinnedMusic", 2131558532);
               if (var3 != null) {
                  var4 = var3;
               }

               this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var4);
               return;
            } else if (this.replyMessageObject.isVideo()) {
               var7 = LocaleController.getString("ActionPinnedVideo", 2131558539);
               if (var3 == null) {
                  var3 = var4;
               }

               this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var3);
               return;
            } else if (this.replyMessageObject.isGif()) {
               var7 = LocaleController.getString("ActionPinnedGif", 2131558531);
               if (var3 == null) {
                  var3 = var4;
               }

               this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var3);
               return;
            } else if (this.replyMessageObject.isVoice()) {
               var7 = LocaleController.getString("ActionPinnedVoice", 2131558540);
               if (var3 == null) {
                  var3 = var4;
               }

               this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var3);
               return;
            } else if (this.replyMessageObject.isRoundVideo()) {
               var7 = LocaleController.getString("ActionPinnedRound", 2131558536);
               if (var3 != null) {
                  var4 = var3;
               }

               this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var4);
               return;
            } else if (!this.replyMessageObject.isSticker() && !this.replyMessageObject.isAnimatedSticker()) {
               var5 = this.replyMessageObject;
               TLRPC.MessageMedia var8 = var5.messageOwner.media;
               if (var8 instanceof TLRPC.TL_messageMediaDocument) {
                  var7 = LocaleController.getString("ActionPinnedFile", 2131558527);
                  if (var3 == null) {
                     var3 = var4;
                  }

                  this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var3);
                  return;
               } else if (var8 instanceof TLRPC.TL_messageMediaGeo) {
                  var7 = LocaleController.getString("ActionPinnedGeo", 2131558529);
                  if (var3 != null) {
                     var4 = var3;
                  }

                  this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var4);
                  return;
               } else if (var8 instanceof TLRPC.TL_messageMediaGeoLive) {
                  var7 = LocaleController.getString("ActionPinnedGeoLive", 2131558530);
                  if (var3 != null) {
                     var4 = var3;
                  }

                  this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var4);
                  return;
               } else if (var8 instanceof TLRPC.TL_messageMediaContact) {
                  var7 = LocaleController.getString("ActionPinnedContact", 2131558526);
                  if (var3 == null) {
                     var3 = var4;
                  }

                  this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var3);
                  return;
               } else if (var8 instanceof TLRPC.TL_messageMediaPoll) {
                  var7 = LocaleController.getString("ActionPinnedPoll", 2131558535);
                  if (var3 == null) {
                     var3 = var4;
                  }

                  this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var3);
                  return;
               } else if (var8 instanceof TLRPC.TL_messageMediaPhoto) {
                  var7 = LocaleController.getString("ActionPinnedPhoto", 2131558534);
                  if (var3 != null) {
                     var4 = var3;
                  }

                  this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var4);
                  return;
               } else {
                  StringBuilder var10;
                  if (var8 instanceof TLRPC.TL_messageMediaGame) {
                     var10 = new StringBuilder();
                     var10.append("\ud83c\udfae ");
                     var10.append(this.replyMessageObject.messageOwner.media.game.title);
                     var7 = LocaleController.formatString("ActionPinnedGame", 2131558528, var10.toString());
                     if (var3 != null) {
                        var4 = var3;
                     }

                     this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var4);
                     this.messageText = Emoji.replaceEmoji(this.messageText, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
                     return;
                  } else {
                     CharSequence var11 = var5.messageText;
                     if (var11 != null && var11.length() > 0) {
                        CharSequence var9 = this.replyMessageObject.messageText;
                        Object var12 = var9;
                        if (var9.length() > 20) {
                           var10 = new StringBuilder();
                           var10.append(var9.subSequence(0, 20));
                           var10.append("...");
                           var12 = var10.toString();
                        }

                        var7 = LocaleController.formatString("ActionPinnedText", 2131558538, Emoji.replaceEmoji((CharSequence)var12, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0F), false));
                        if (var3 != null) {
                           var4 = var3;
                        }

                        this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var4);
                     } else {
                        var7 = LocaleController.getString("ActionPinnedNoText", 2131558533);
                        if (var3 != null) {
                           var4 = var3;
                        }

                        this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var4);
                     }

                     return;
                  }
               }
            } else {
               var7 = LocaleController.getString("ActionPinnedSticker", 2131558537);
               if (var3 != null) {
                  var4 = var3;
               }

               this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var4);
               return;
            }
         }
      }

      var7 = LocaleController.getString("ActionPinnedNoText", 2131558533);
      if (var3 == null) {
         var3 = var4;
      }

      this.messageText = this.replaceWithLink(var7, "un1", (TLObject)var3);
   }

   public void generateThumbs(boolean var1) {
      TLRPC.Message var2 = this.messageOwner;
      boolean var3 = var2 instanceof TLRPC.TL_messageService;
      byte var4 = 0;
      ArrayList var5;
      int var6;
      int var7;
      TLRPC.PhotoSize var8;
      TLRPC.Photo var10;
      TLRPC.PhotoSize var13;
      if (var3) {
         TLRPC.MessageAction var9 = var2.action;
         if (var9 instanceof TLRPC.TL_messageActionChatEditPhoto) {
            var10 = var9.photo;
            if (!var1) {
               this.photoThumbs = new ArrayList(var10.sizes);
            } else {
               var5 = this.photoThumbs;
               if (var5 != null && !var5.isEmpty()) {
                  for(var6 = 0; var6 < this.photoThumbs.size(); ++var6) {
                     var13 = (TLRPC.PhotoSize)this.photoThumbs.get(var6);

                     for(var7 = 0; var7 < var10.sizes.size(); ++var7) {
                        var8 = (TLRPC.PhotoSize)var10.sizes.get(var7);
                        if (!(var8 instanceof TLRPC.TL_photoSizeEmpty) && var8.type.equals(var13.type)) {
                           var13.location = var8.location;
                           break;
                        }
                     }
                  }
               }
            }

            if (var10.dc_id != 0) {
               var7 = this.photoThumbs.size();

               for(var6 = var4; var6 < var7; ++var6) {
                  TLRPC.FileLocation var14 = ((TLRPC.PhotoSize)this.photoThumbs.get(var6)).location;
                  var14.dc_id = var10.dc_id;
                  var14.file_reference = var10.file_reference;
               }
            }

            this.photoThumbsObject = this.messageOwner.action.photo;
         }
      } else {
         TLRPC.MessageMedia var11 = var2.media;
         if (var11 != null && !(var11 instanceof TLRPC.TL_messageMediaEmpty)) {
            if (var11 instanceof TLRPC.TL_messageMediaPhoto) {
               label166: {
                  var10 = var11.photo;
                  if (var1) {
                     var5 = this.photoThumbs;
                     if (var5 == null || var5.size() == var10.sizes.size()) {
                        var5 = this.photoThumbs;
                        if (var5 != null && !var5.isEmpty()) {
                           for(var6 = 0; var6 < this.photoThumbs.size(); ++var6) {
                              var13 = (TLRPC.PhotoSize)this.photoThumbs.get(var6);
                              if (var13 != null) {
                                 for(var7 = 0; var7 < var10.sizes.size(); ++var7) {
                                    var8 = (TLRPC.PhotoSize)var10.sizes.get(var7);
                                    if (var8 != null && !(var8 instanceof TLRPC.TL_photoSizeEmpty) && var8.type.equals(var13.type)) {
                                       var13.location = var8.location;
                                       break;
                                    }
                                 }
                              }
                           }
                        }
                        break label166;
                     }
                  }

                  this.photoThumbs = new ArrayList(var10.sizes);
               }

               this.photoThumbsObject = this.messageOwner.media.photo;
            } else {
               ArrayList var12;
               TLRPC.Document var15;
               if (var11 instanceof TLRPC.TL_messageMediaDocument) {
                  var15 = var11.document;
                  if (isDocumentHasThumb(var15)) {
                     label136: {
                        if (var1) {
                           var12 = this.photoThumbs;
                           if (var12 != null) {
                              if (var12 != null && !var12.isEmpty()) {
                                 updatePhotoSizeLocations(this.photoThumbs, var15.thumbs);
                              }
                              break label136;
                           }
                        }

                        this.photoThumbs = new ArrayList();
                        this.photoThumbs.addAll(var15.thumbs);
                     }

                     this.photoThumbsObject = var15;
                  }
               } else if (var11 instanceof TLRPC.TL_messageMediaGame) {
                  var15 = var11.game.document;
                  if (var15 != null && isDocumentHasThumb(var15)) {
                     if (!var1) {
                        this.photoThumbs = new ArrayList();
                        this.photoThumbs.addAll(var15.thumbs);
                     } else {
                        var12 = this.photoThumbs;
                        if (var12 != null && !var12.isEmpty()) {
                           updatePhotoSizeLocations(this.photoThumbs, var15.thumbs);
                        }
                     }

                     this.photoThumbsObject = var15;
                  }

                  TLRPC.Photo var16 = this.messageOwner.media.game.photo;
                  if (var16 != null) {
                     label120: {
                        if (var1) {
                           var12 = this.photoThumbs2;
                           if (var12 != null) {
                              if (!var12.isEmpty()) {
                                 updatePhotoSizeLocations(this.photoThumbs2, var16.sizes);
                              }
                              break label120;
                           }
                        }

                        this.photoThumbs2 = new ArrayList(var16.sizes);
                     }

                     this.photoThumbsObject2 = var16;
                  }

                  if (this.photoThumbs == null) {
                     var12 = this.photoThumbs2;
                     if (var12 != null) {
                        this.photoThumbs = var12;
                        this.photoThumbs2 = null;
                        this.photoThumbsObject = this.photoThumbsObject2;
                        this.photoThumbsObject2 = null;
                     }
                  }
               } else if (var11 instanceof TLRPC.TL_messageMediaWebPage) {
                  TLRPC.WebPage var17 = var11.webpage;
                  var10 = var17.photo;
                  var15 = var17.document;
                  if (var10 != null) {
                     label128: {
                        if (var1) {
                           var5 = this.photoThumbs;
                           if (var5 != null) {
                              if (!var5.isEmpty()) {
                                 updatePhotoSizeLocations(this.photoThumbs, var10.sizes);
                              }
                              break label128;
                           }
                        }

                        this.photoThumbs = new ArrayList(var10.sizes);
                     }

                     this.photoThumbsObject = var10;
                  } else if (var15 != null && isDocumentHasThumb(var15)) {
                     if (!var1) {
                        this.photoThumbs = new ArrayList();
                        this.photoThumbs.addAll(var15.thumbs);
                     } else {
                        var12 = this.photoThumbs;
                        if (var12 != null && !var12.isEmpty()) {
                           updatePhotoSizeLocations(this.photoThumbs, var15.thumbs);
                        }
                     }

                     this.photoThumbsObject = var15;
                  }
               }
            }
         }
      }

   }

   public int getApproximateHeight() {
      int var1 = this.type;
      int var2 = 0;
      byte var3 = 0;
      if (var1 == 0) {
         var2 = this.textHeight;
         TLRPC.MessageMedia var12 = this.messageOwner.media;
         var1 = var3;
         if (var12 instanceof TLRPC.TL_messageMediaWebPage) {
            var1 = var3;
            if (var12.webpage instanceof TLRPC.TL_webPage) {
               var1 = AndroidUtilities.dp(100.0F);
            }
         }

         var2 += var1;
         var1 = var2;
         if (this.isReply()) {
            var1 = var2 + AndroidUtilities.dp(42.0F);
         }

         return var1;
      } else if (var1 == 2) {
         return AndroidUtilities.dp(72.0F);
      } else if (var1 == 12) {
         return AndroidUtilities.dp(71.0F);
      } else if (var1 == 9) {
         return AndroidUtilities.dp(100.0F);
      } else if (var1 == 4) {
         return AndroidUtilities.dp(114.0F);
      } else if (var1 == 14) {
         return AndroidUtilities.dp(82.0F);
      } else if (var1 == 10) {
         return AndroidUtilities.dp(30.0F);
      } else if (var1 == 11) {
         return AndroidUtilities.dp(50.0F);
      } else if (var1 == 5) {
         return AndroidUtilities.roundMessageSize;
      } else {
         float var5;
         int var9;
         if (var1 != 13 && var1 != 15) {
            Point var10;
            if (AndroidUtilities.isTablet()) {
               var1 = AndroidUtilities.getMinTabletSide();
            } else {
               var10 = AndroidUtilities.displaySize;
               var1 = Math.min(var10.x, var10.y);
            }

            var1 = (int)((float)var1 * 0.7F);
            var2 = AndroidUtilities.dp(100.0F) + var1;
            var9 = var1;
            if (var1 > AndroidUtilities.getPhotoSize()) {
               var9 = AndroidUtilities.getPhotoSize();
            }

            var1 = var2;
            if (var2 > AndroidUtilities.getPhotoSize()) {
               var1 = AndroidUtilities.getPhotoSize();
            }

            TLRPC.PhotoSize var11 = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize());
            var2 = var1;
            if (var11 != null) {
               var5 = (float)var11.w / (float)var9;
               var9 = (int)((float)var11.h / var5);
               var2 = var9;
               if (var9 == 0) {
                  var2 = AndroidUtilities.dp(100.0F);
               }

               if (var2 <= var1) {
                  if (var2 < AndroidUtilities.dp(120.0F)) {
                     var1 = AndroidUtilities.dp(120.0F);
                  } else {
                     var1 = var2;
                  }
               }

               var2 = var1;
               if (this.needDrawBluredPreview()) {
                  if (AndroidUtilities.isTablet()) {
                     var1 = AndroidUtilities.getMinTabletSide();
                  } else {
                     var10 = AndroidUtilities.displaySize;
                     var1 = Math.min(var10.x, var10.y);
                  }

                  var2 = (int)((float)var1 * 0.5F);
               }
            }

            return var2 + AndroidUtilities.dp(14.0F);
         } else {
            float var6 = (float)AndroidUtilities.displaySize.y * 0.4F;
            if (AndroidUtilities.isTablet()) {
               var1 = AndroidUtilities.getMinTabletSide();
            } else {
               var1 = AndroidUtilities.displaySize.x;
            }

            var5 = (float)var1 * 0.5F;
            Iterator var4 = this.messageOwner.media.document.attributes.iterator();

            while(true) {
               if (var4.hasNext()) {
                  TLRPC.DocumentAttribute var7 = (TLRPC.DocumentAttribute)var4.next();
                  if (!(var7 instanceof TLRPC.TL_documentAttributeImageSize)) {
                     continue;
                  }

                  var2 = var7.w;
                  var9 = var7.h;
                  break;
               }

               var9 = 0;
               break;
            }

            var1 = var2;
            if (var2 == 0) {
               var9 = (int)var6;
               var1 = AndroidUtilities.dp(100.0F) + var9;
            }

            float var8 = (float)var9;
            var2 = var1;
            if (var8 > var6) {
               var2 = (int)((float)var1 * (var6 / var8));
               var9 = (int)var6;
            }

            var8 = (float)var2;
            var1 = var9;
            if (var8 > var5) {
               var1 = (int)((float)var9 * (var5 / var8));
            }

            return var1 + AndroidUtilities.dp(14.0F);
         }
      }
   }

   public String getArtworkUrl(boolean var1) {
      TLRPC.Document var2 = this.getDocument();
      if (var2 != null) {
         int var3 = var2.attributes.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            TLRPC.DocumentAttribute var5 = (TLRPC.DocumentAttribute)var2.attributes.get(var4);
            if (var5 instanceof TLRPC.TL_documentAttributeAudio) {
               if (var5.voice) {
                  return null;
               }

               String var6 = var5.performer;
               String var7 = var5.title;
               String var12 = var6;
               if (!TextUtils.isEmpty(var6)) {
                  var12 = var6;
                  int var8 = 0;

                  while(true) {
                     String[] var13 = excludeWords;
                     if (var8 >= var13.length) {
                        break;
                     }

                     var12 = var12.replace(var13[var8], " ");
                     ++var8;
                  }
               }

               if (TextUtils.isEmpty(var12) && TextUtils.isEmpty(var7)) {
                  return null;
               }

               StringBuilder var14;
               boolean var10001;
               try {
                  var14 = new StringBuilder();
                  var14.append("athumb://itunes.apple.com/search?term=");
                  StringBuilder var9 = new StringBuilder();
                  var9.append(var12);
                  var9.append(" - ");
                  var9.append(var7);
                  var14.append(URLEncoder.encode(var9.toString(), "UTF-8"));
                  var14.append("&entity=song&limit=4");
               } catch (Exception var11) {
                  var10001 = false;
                  continue;
               }

               if (var1) {
                  var12 = "&s=1";
               } else {
                  var12 = "";
               }

               try {
                  var14.append(var12);
                  var12 = var14.toString();
                  return var12;
               } catch (Exception var10) {
                  var10001 = false;
               }
            }
         }
      }

      return null;
   }

   public int getChannelId() {
      TLRPC.Peer var1 = this.messageOwner.to_id;
      return var1 != null ? var1.channel_id : 0;
   }

   public long getDialogId() {
      return getDialogId(this.messageOwner);
   }

   public TLRPC.Document getDocument() {
      return getDocument(this.messageOwner);
   }

   public String getDocumentName() {
      TLRPC.MessageMedia var1 = this.messageOwner.media;
      if (var1 instanceof TLRPC.TL_messageMediaDocument) {
         return FileLoader.getDocumentFileName(var1.document);
      } else {
         return var1 instanceof TLRPC.TL_messageMediaWebPage ? FileLoader.getDocumentFileName(var1.webpage.document) : "";
      }
   }

   public int getDuration() {
      TLRPC.Document var1 = this.getDocument();
      int var2 = 0;
      if (var1 == null) {
         return 0;
      } else {
         int var3 = this.audioPlayerDuration;
         if (var3 > 0) {
            return var3;
         } else {
            while(var2 < var1.attributes.size()) {
               TLRPC.DocumentAttribute var4 = (TLRPC.DocumentAttribute)var1.attributes.get(var2);
               if (var4 instanceof TLRPC.TL_documentAttributeAudio) {
                  return var4.duration;
               }

               if (var4 instanceof TLRPC.TL_documentAttributeVideo) {
                  return var4.duration;
               }

               ++var2;
            }

            return this.audioPlayerDuration;
         }
      }
   }

   public String getExtension() {
      String var1 = this.getFileName();
      int var2 = var1.lastIndexOf(46);
      String var3;
      if (var2 != -1) {
         var3 = var1.substring(var2 + 1);
      } else {
         var3 = null;
      }

      label19: {
         if (var3 != null) {
            var1 = var3;
            if (var3.length() != 0) {
               break label19;
            }
         }

         var1 = this.messageOwner.media.document.mime_type;
      }

      var3 = var1;
      if (var1 == null) {
         var3 = "";
      }

      return var3.toUpperCase();
   }

   public String getFileName() {
      TLRPC.MessageMedia var1 = this.messageOwner.media;
      if (var1 instanceof TLRPC.TL_messageMediaDocument) {
         return FileLoader.getAttachFileName(var1.document);
      } else {
         if (var1 instanceof TLRPC.TL_messageMediaPhoto) {
            ArrayList var2 = var1.photo.sizes;
            if (var2.size() > 0) {
               TLRPC.PhotoSize var3 = FileLoader.getClosestPhotoSizeWithSize(var2, AndroidUtilities.getPhotoSize());
               if (var3 != null) {
                  return FileLoader.getAttachFileName(var3);
               }
            }
         } else if (var1 instanceof TLRPC.TL_messageMediaWebPage) {
            return FileLoader.getAttachFileName(var1.webpage.document);
         }

         return "";
      }
   }

   public int getFileType() {
      if (this.isVideo()) {
         return 2;
      } else if (this.isVoice()) {
         return 1;
      } else {
         TLRPC.MessageMedia var1 = this.messageOwner.media;
         if (var1 instanceof TLRPC.TL_messageMediaDocument) {
            return 3;
         } else {
            return var1 instanceof TLRPC.TL_messageMediaPhoto ? 0 : 4;
         }
      }
   }

   public String getForwardedName() {
      TLRPC.MessageFwdHeader var1 = this.messageOwner.fwd_from;
      if (var1 != null) {
         if (var1.channel_id != 0) {
            TLRPC.Chat var2 = MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.fwd_from.channel_id);
            if (var2 != null) {
               return var2.title;
            }
         } else if (var1.from_id != 0) {
            TLRPC.User var3 = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.fwd_from.from_id);
            if (var3 != null) {
               return UserObject.getUserName(var3);
            }
         } else {
            String var4 = var1.from_name;
            if (var4 != null) {
               return var4;
            }
         }
      }

      return null;
   }

   public int getFromId() {
      TLRPC.MessageFwdHeader var1 = this.messageOwner.fwd_from;
      int var3;
      if (var1 != null) {
         TLRPC.Peer var2 = var1.saved_from_peer;
         if (var2 != null) {
            var3 = var2.user_id;
            int var4;
            if (var3 != 0) {
               var4 = var1.from_id;
               if (var4 != 0) {
                  return var4;
               }

               return var3;
            }

            if (var2.channel_id != 0) {
               if (this.isSavedFromMegagroup()) {
                  var3 = this.messageOwner.fwd_from.from_id;
                  if (var3 != 0) {
                     return var3;
                  }
               }

               var1 = this.messageOwner.fwd_from;
               var3 = var1.channel_id;
               if (var3 != 0) {
                  return -var3;
               }

               return -var1.saved_from_peer.channel_id;
            }

            var3 = var2.chat_id;
            if (var3 != 0) {
               var4 = var1.from_id;
               if (var4 != 0) {
                  return var4;
               }

               var4 = var1.channel_id;
               if (var4 != 0) {
                  return -var4;
               }

               return -var3;
            }

            return 0;
         }
      }

      TLRPC.Message var5 = this.messageOwner;
      var3 = var5.from_id;
      if (var3 != 0) {
         return var3;
      } else if (var5.post) {
         return var5.to_id.channel_id;
      } else {
         return 0;
      }
   }

   public long getGroupId() {
      long var1 = this.localGroupId;
      if (var1 == 0L) {
         var1 = this.getGroupIdForUse();
      }

      return var1;
   }

   public long getGroupIdForUse() {
      long var1 = this.localSentGroupId;
      if (var1 == 0L) {
         var1 = this.messageOwner.grouped_id;
      }

      return var1;
   }

   public int getId() {
      return this.messageOwner.id;
   }

   public long getIdWithChannel() {
      TLRPC.Message var1 = this.messageOwner;
      long var2 = (long)var1.id;
      TLRPC.Peer var7 = var1.to_id;
      long var4 = var2;
      if (var7 != null) {
         int var6 = var7.channel_id;
         var4 = var2;
         if (var6 != 0) {
            var4 = var2 | (long)var6 << 32;
         }
      }

      return var4;
   }

   public TLRPC.InputStickerSet getInputStickerSet() {
      return getInputStickerSet(this.messageOwner);
   }

   public int getMaxMessageTextWidth() {
      int var1;
      if (AndroidUtilities.isTablet() && this.eventId != 0L) {
         this.generatedWithMinSize = AndroidUtilities.dp(530.0F);
      } else {
         if (AndroidUtilities.isTablet()) {
            var1 = AndroidUtilities.getMinTabletSide();
         } else {
            var1 = AndroidUtilities.displaySize.x;
         }

         this.generatedWithMinSize = var1;
      }

      this.generatedWithDensity = AndroidUtilities.density;
      TLRPC.MessageMedia var2 = this.messageOwner.media;
      boolean var3 = var2 instanceof TLRPC.TL_messageMediaWebPage;
      byte var4 = 0;
      var1 = var4;
      if (var3) {
         TLRPC.WebPage var8 = var2.webpage;
         var1 = var4;
         if (var8 != null) {
            var1 = var4;
            if ("telegram_background".equals(var8.type)) {
               label63: {
                  label75: {
                     boolean var10001;
                     Uri var9;
                     try {
                        var9 = Uri.parse(this.messageOwner.media.webpage.url);
                        if (var9.getQueryParameter("bg_color") != null) {
                           var1 = AndroidUtilities.dp(220.0F);
                           break label63;
                        }
                     } catch (Exception var7) {
                        var10001 = false;
                        break label75;
                     }

                     var1 = var4;

                     try {
                        if (var9.getLastPathSegment().length() == 6) {
                           var1 = AndroidUtilities.dp(200.0F);
                        }
                        break label63;
                     } catch (Exception var6) {
                        var10001 = false;
                     }
                  }

                  var1 = var4;
               }
            }
         }
      }

      int var10 = var1;
      if (var1 == 0) {
         var1 = this.generatedWithMinSize;
         float var5;
         if (this.needDrawAvatarInternal() && !this.isOutOwner()) {
            var5 = 132.0F;
         } else {
            var5 = 80.0F;
         }

         var10 = var1 - AndroidUtilities.dp(var5);
         var1 = var10;
         if (this.needDrawShareButton()) {
            var1 = var10;
            if (!this.isOutOwner()) {
               var1 = var10 - AndroidUtilities.dp(10.0F);
            }
         }

         if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
            var10 = var1 - AndroidUtilities.dp(10.0F);
         } else {
            var10 = var1;
         }
      }

      return var10;
   }

   public int getMediaExistanceFlags() {
      byte var1;
      if (this.attachPathExists) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      int var2 = var1;
      if (this.mediaExists) {
         var2 = var1 | 2;
      }

      return var2;
   }

   public String getMimeType() {
      TLRPC.MessageMedia var1 = this.messageOwner.media;
      if (var1 instanceof TLRPC.TL_messageMediaDocument) {
         return var1.document.mime_type;
      } else {
         if (var1 instanceof TLRPC.TL_messageMediaInvoice) {
            TLRPC.WebDocument var2 = ((TLRPC.TL_messageMediaInvoice)var1).photo;
            if (var2 != null) {
               return var2.mime_type;
            }
         } else {
            if (var1 instanceof TLRPC.TL_messageMediaPhoto) {
               return "image/jpeg";
            }

            if (var1 instanceof TLRPC.TL_messageMediaWebPage) {
               TLRPC.WebPage var3 = var1.webpage;
               if (var3.document != null) {
                  return var1.document.mime_type;
               }

               if (var3.photo != null) {
                  return "image/jpeg";
               }
            }
         }

         return "";
      }
   }

   public String getMusicAuthor() {
      return this.getMusicAuthor(true);
   }

   public String getMusicAuthor(boolean var1) {
      TLRPC.Document var2 = this.getDocument();
      if (var2 != null) {
         int var3 = 0;

         boolean var6;
         for(boolean var4 = false; var3 < var2.attributes.size(); var4 = var6) {
            String var10;
            label89: {
               TLRPC.DocumentAttribute var5 = (TLRPC.DocumentAttribute)var2.attributes.get(var3);
               if (var5 instanceof TLRPC.TL_documentAttributeAudio) {
                  if (!var5.voice) {
                     String var7 = var5.performer;
                     var10 = var7;
                     if (TextUtils.isEmpty(var7)) {
                        var10 = var7;
                        if (var1) {
                           var10 = LocaleController.getString("AudioUnknownArtist", 2131558737);
                        }
                     }

                     return var10;
                  }
               } else {
                  var6 = var4;
                  if (!(var5 instanceof TLRPC.TL_documentAttributeVideo)) {
                     break label89;
                  }

                  var6 = var4;
                  if (!var5.round_message) {
                     break label89;
                  }
               }

               var6 = true;
            }

            if (var6) {
               TLRPC.User var12 = null;
               if (!var1) {
                  return null;
               }

               if (this.isOutOwner()) {
                  return LocaleController.getString("FromYou", 2131559584);
               }

               TLRPC.MessageFwdHeader var11 = this.messageOwner.fwd_from;
               if (var11 != null && var11.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                  return LocaleController.getString("FromYou", 2131559584);
               }

               var11 = this.messageOwner.fwd_from;
               TLRPC.Chat var15;
               if (var11 != null && var11.channel_id != 0) {
                  var15 = MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.fwd_from.channel_id);
               } else {
                  label104: {
                     var11 = this.messageOwner.fwd_from;
                     TLRPC.User var14;
                     if (var11 != null && var11.from_id != 0) {
                        var14 = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.fwd_from.from_id);
                     } else {
                        var11 = this.messageOwner.fwd_from;
                        if (var11 != null) {
                           var10 = var11.from_name;
                           if (var10 != null) {
                              return var10;
                           }
                        }

                        TLRPC.Message var13 = this.messageOwner;
                        int var9 = var13.from_id;
                        if (var9 < 0) {
                           var15 = MessagesController.getInstance(this.currentAccount).getChat(-this.messageOwner.from_id);
                           break label104;
                        }

                        if (var9 == 0 && var13.to_id.channel_id != 0) {
                           var15 = MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.to_id.channel_id);
                           break label104;
                        }

                        var14 = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.from_id);
                     }

                     Object var8 = null;
                     var12 = var14;
                     var15 = (TLRPC.Chat)var8;
                  }
               }

               if (var12 != null) {
                  return UserObject.getUserName(var12);
               }

               if (var15 != null) {
                  return var15.title;
               }
            }

            ++var3;
         }
      }

      return LocaleController.getString("AudioUnknownArtist", 2131558737);
   }

   public String getMusicTitle() {
      return this.getMusicTitle(true);
   }

   public String getMusicTitle(boolean var1) {
      TLRPC.Document var2 = this.getDocument();
      if (var2 != null) {
         String var6;
         for(int var3 = 0; var3 < var2.attributes.size(); ++var3) {
            TLRPC.DocumentAttribute var4 = (TLRPC.DocumentAttribute)var2.attributes.get(var3);
            if (var4 instanceof TLRPC.TL_documentAttributeAudio) {
               if (var4.voice) {
                  if (!var1) {
                     return null;
                  }

                  return LocaleController.formatDateAudio((long)this.messageOwner.date);
               }

               String var5 = var4.title;
               if (var5 != null) {
                  var6 = var5;
                  if (var5.length() != 0) {
                     return var6;
                  }
               }

               var5 = FileLoader.getDocumentFileName(var2);
               var6 = var5;
               if (TextUtils.isEmpty(var5)) {
                  var6 = var5;
                  if (var1) {
                     var6 = LocaleController.getString("AudioUnknownTitle", 2131558738);
                  }
               }

               return var6;
            }

            if (var4 instanceof TLRPC.TL_documentAttributeVideo && var4.round_message) {
               return LocaleController.formatDateAudio((long)this.messageOwner.date);
            }
         }

         var6 = FileLoader.getDocumentFileName(var2);
         if (!TextUtils.isEmpty(var6)) {
            return var6;
         }
      }

      return LocaleController.getString("AudioUnknownTitle", 2131558738);
   }

   public long getPollId() {
      return this.type != 17 ? 0L : ((TLRPC.TL_messageMediaPoll)this.messageOwner.media).poll.id;
   }

   public int getRealId() {
      TLRPC.Message var1 = this.messageOwner;
      int var2 = var1.realId;
      if (var2 == 0) {
         var2 = var1.id;
      }

      return var2;
   }

   public int getSecretTimeLeft() {
      TLRPC.Message var1 = this.messageOwner;
      int var2 = var1.ttl;
      int var3 = var1.destroyTime;
      if (var3 != 0) {
         var2 = Math.max(0, var3 - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
      }

      return var2;
   }

   public String getSecretTimeString() {
      if (!this.isSecretMedia()) {
         return null;
      } else {
         int var1 = this.getSecretTimeLeft();
         StringBuilder var2;
         String var3;
         if (var1 < 60) {
            var2 = new StringBuilder();
            var2.append(var1);
            var2.append("s");
            var3 = var2.toString();
         } else {
            var2 = new StringBuilder();
            var2.append(var1 / 60);
            var2.append("m");
            var3 = var2.toString();
         }

         return var3;
      }
   }

   public int getSize() {
      return getMessageSize(this.messageOwner);
   }

   public String getStickerEmoji() {
      int var1 = 0;

      while(true) {
         int var2 = this.messageOwner.media.document.attributes.size();
         Object var3 = null;
         if (var1 >= var2) {
            return null;
         }

         TLRPC.DocumentAttribute var4 = (TLRPC.DocumentAttribute)this.messageOwner.media.document.attributes.get(var1);
         if (var4 instanceof TLRPC.TL_documentAttributeSticker) {
            String var5 = var4.alt;
            String var6 = (String)var3;
            if (var5 != null) {
               var6 = (String)var3;
               if (var5.length() > 0) {
                  var6 = var4.alt;
               }
            }

            return var6;
         }

         ++var1;
      }
   }

   public String getStrickerChar() {
      TLRPC.MessageMedia var1 = this.messageOwner.media;
      if (var1 != null) {
         TLRPC.Document var3 = var1.document;
         if (var3 != null) {
            Iterator var2 = var3.attributes.iterator();

            while(var2.hasNext()) {
               TLRPC.DocumentAttribute var4 = (TLRPC.DocumentAttribute)var2.next();
               if (var4 instanceof TLRPC.TL_documentAttributeSticker) {
                  return var4.alt;
               }
            }
         }
      }

      return null;
   }

   public int getUnradFlags() {
      return getUnreadFlags(this.messageOwner);
   }

   public ArrayList getWebPagePhotos(ArrayList var1, ArrayList var2) {
      ArrayList var3 = var1;
      if (var1 == null) {
         var3 = new ArrayList();
      }

      TLRPC.MessageMedia var8 = this.messageOwner.media;
      if (var8 != null) {
         TLRPC.WebPage var4 = var8.webpage;
         if (var4 != null) {
            TLRPC.Page var5 = var4.cached_page;
            if (var5 == null) {
               return var3;
            }

            var1 = var2;
            if (var2 == null) {
               var1 = var5.blocks;
            }

            for(int var6 = 0; var6 < var1.size(); ++var6) {
               TLRPC.PageBlock var9 = (TLRPC.PageBlock)var1.get(var6);
               int var7;
               if (var9 instanceof TLRPC.TL_pageBlockSlideshow) {
                  TLRPC.TL_pageBlockSlideshow var11 = (TLRPC.TL_pageBlockSlideshow)var9;

                  for(var7 = 0; var7 < var11.items.size(); ++var7) {
                     var3.add(this.getMessageObjectForBlock(var4, (TLRPC.PageBlock)var11.items.get(var7)));
                  }
               } else if (var9 instanceof TLRPC.TL_pageBlockCollage) {
                  TLRPC.TL_pageBlockCollage var10 = (TLRPC.TL_pageBlockCollage)var9;

                  for(var7 = 0; var7 < var10.items.size(); ++var7) {
                     var3.add(this.getMessageObjectForBlock(var4, (TLRPC.PageBlock)var10.items.get(var7)));
                  }
               }
            }
         }
      }

      return var3;
   }

   public boolean hasPhotoStickers() {
      TLRPC.MessageMedia var1 = this.messageOwner.media;
      boolean var2;
      if (var1 != null) {
         TLRPC.Photo var3 = var1.photo;
         if (var3 != null && var3.has_stickers) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public boolean hasValidGroupId() {
      boolean var2;
      if (this.getGroupId() != 0L) {
         ArrayList var1 = this.photoThumbs;
         if (var1 != null && !var1.isEmpty()) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public boolean hasValidReplyMessageObject() {
      MessageObject var1 = this.replyMessageObject;
      boolean var2;
      if (var1 != null) {
         TLRPC.Message var3 = var1.messageOwner;
         if (!(var3 instanceof TLRPC.TL_messageEmpty) && !(var3.action instanceof TLRPC.TL_messageActionHistoryClear)) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public boolean isAnimatedSticker() {
      int var1 = this.type;
      if (var1 != 1000) {
         boolean var2;
         if (var1 == 15) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      } else {
         return isAnimatedStickerMessage(this.messageOwner);
      }
   }

   public boolean isAnyKindOfSticker() {
      int var1 = this.type;
      boolean var2;
      if (var1 != 13 && var1 != 15) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public boolean isContentUnread() {
      return this.messageOwner.media_unread;
   }

   public boolean isEditing() {
      TLRPC.Message var1 = this.messageOwner;
      boolean var2;
      if (var1.send_state == 3 && var1.id > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isFcmMessage() {
      boolean var1;
      if (this.localType != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isForwarded() {
      return isForwardedMessage(this.messageOwner);
   }

   public boolean isForwardedChannelPost() {
      TLRPC.Message var1 = this.messageOwner;
      boolean var2;
      if (var1.from_id <= 0) {
         TLRPC.MessageFwdHeader var3 = var1.fwd_from;
         if (var3 != null && var3.channel_post != 0) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public boolean isFromChat() {
      if (this.getDialogId() == (long)UserConfig.getInstance(this.currentAccount).clientUserId) {
         return true;
      } else {
         if (!this.isMegagroup()) {
            TLRPC.Peer var1 = this.messageOwner.to_id;
            if (var1 == null || var1.chat_id == 0) {
               var1 = this.messageOwner.to_id;
               boolean var2 = false;
               boolean var3 = var2;
               if (var1 != null) {
                  var3 = var2;
                  if (var1.channel_id != 0) {
                     TLRPC.Chat var4 = MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.to_id.channel_id);
                     var3 = var2;
                     if (var4 != null) {
                        var3 = var2;
                        if (var4.megagroup) {
                           var3 = true;
                        }
                     }
                  }
               }

               return var3;
            }
         }

         return true;
      }
   }

   public boolean isFromUser() {
      TLRPC.Message var1 = this.messageOwner;
      boolean var2;
      if (var1.from_id > 0 && !var1.post) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isGame() {
      return isGameMessage(this.messageOwner);
   }

   public boolean isGif() {
      return isGifMessage(this.messageOwner);
   }

   public boolean isInvoice() {
      return isInvoiceMessage(this.messageOwner);
   }

   public boolean isLiveLocation() {
      return isLiveLocationMessage(this.messageOwner);
   }

   public boolean isLocation() {
      return isLocationMessage(this.messageOwner);
   }

   public boolean isMask() {
      return isMaskMessage(this.messageOwner);
   }

   public boolean isMediaEmpty() {
      return isMediaEmpty(this.messageOwner);
   }

   public boolean isMediaEmptyWebpage() {
      return isMediaEmptyWebpage(this.messageOwner);
   }

   public boolean isMegagroup() {
      return isMegagroup(this.messageOwner);
   }

   public boolean isMusic() {
      return isMusicMessage(this.messageOwner);
   }

   public boolean isNewGif() {
      TLRPC.MessageMedia var1 = this.messageOwner.media;
      boolean var2;
      if (var1 != null && isNewGifDocument(var1.document)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isOut() {
      return this.messageOwner.out;
   }

   public boolean isOutOwner() {
      TLRPC.Message var1 = this.messageOwner;
      if (var1.out && var1.from_id > 0 && !var1.post) {
         TLRPC.MessageFwdHeader var6 = var1.fwd_from;
         boolean var2 = true;
         boolean var3 = true;
         if (var6 == null) {
            return true;
         } else {
            int var4 = UserConfig.getInstance(this.currentAccount).getClientUserId();
            boolean var5;
            TLRPC.Peer var7;
            if (this.getDialogId() != (long)var4) {
               var7 = this.messageOwner.fwd_from.saved_from_peer;
               var5 = var2;
               if (var7 != null) {
                  if (var7.user_id == var4) {
                     var5 = var2;
                  } else {
                     var5 = false;
                  }
               }

               return var5;
            } else {
               var6 = this.messageOwner.fwd_from;
               if (var6.from_id == var4) {
                  var7 = var6.saved_from_peer;
                  var5 = var3;
                  if (var7 == null) {
                     return var5;
                  }

                  var5 = var3;
                  if (var7.user_id == var4) {
                     return var5;
                  }
               }

               var7 = this.messageOwner.fwd_from.saved_from_peer;
               if (var7 != null && var7.user_id == var4) {
                  var5 = var3;
               } else {
                  var5 = false;
               }

               return var5;
            }
         }
      } else {
         return false;
      }
   }

   public boolean isPhoto() {
      return isPhoto(this.messageOwner);
   }

   public boolean isPollClosed() {
      return this.type != 17 ? false : ((TLRPC.TL_messageMediaPoll)this.messageOwner.media).poll.closed;
   }

   public boolean isReply() {
      MessageObject var1 = this.replyMessageObject;
      boolean var2;
      if (var1 == null || !(var1.messageOwner instanceof TLRPC.TL_messageEmpty)) {
         TLRPC.Message var3 = this.messageOwner;
         if ((var3.reply_to_msg_id != 0 || var3.reply_to_random_id != 0L) && (this.messageOwner.flags & 8) != 0) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public boolean isRoundVideo() {
      int var1 = this.isRoundVideoCached;
      boolean var2 = true;
      if (var1 == 0) {
         byte var3;
         if (this.type != 5 && !isRoundVideoMessage(this.messageOwner)) {
            var3 = 2;
         } else {
            var3 = 1;
         }

         this.isRoundVideoCached = var3;
      }

      if (this.isRoundVideoCached != 1) {
         var2 = false;
      }

      return var2;
   }

   public boolean isSavedFromMegagroup() {
      TLRPC.MessageFwdHeader var1 = this.messageOwner.fwd_from;
      if (var1 != null) {
         TLRPC.Peer var2 = var1.saved_from_peer;
         if (var2 != null && var2.channel_id != 0) {
            return ChatObject.isMegagroup(MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.fwd_from.saved_from_peer.channel_id));
         }
      }

      return false;
   }

   public boolean isSecretMedia() {
      TLRPC.Message var1 = this.messageOwner;
      boolean var2 = var1 instanceof TLRPC.TL_message_secret;
      boolean var3 = true;
      boolean var4 = true;
      if (var2) {
         if (var1.media instanceof TLRPC.TL_messageMediaPhoto || this.isGif()) {
            int var5 = this.messageOwner.ttl;
            if (var5 > 0) {
               var3 = var4;
               if (var5 <= 60) {
                  return var3;
               }
            }
         }

         var3 = var4;
         if (!this.isVoice()) {
            var3 = var4;
            if (!this.isRoundVideo()) {
               if (this.isVideo()) {
                  var3 = var4;
               } else {
                  var3 = false;
               }
            }
         }

         return var3;
      } else if (!(var1 instanceof TLRPC.TL_message)) {
         return false;
      } else {
         TLRPC.MessageMedia var6 = var1.media;
         if (!(var6 instanceof TLRPC.TL_messageMediaPhoto) && !(var6 instanceof TLRPC.TL_messageMediaDocument) || this.messageOwner.media.ttl_seconds == 0) {
            var3 = false;
         }

         return var3;
      }
   }

   public boolean isSendError() {
      TLRPC.Message var1 = this.messageOwner;
      boolean var2;
      if (var1.send_state == 2 && var1.id < 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isSending() {
      TLRPC.Message var1 = this.messageOwner;
      int var2 = var1.send_state;
      boolean var3 = true;
      if (var2 != 1 || var1.id >= 0) {
         var3 = false;
      }

      return var3;
   }

   public boolean isSent() {
      TLRPC.Message var1 = this.messageOwner;
      boolean var2;
      if (var1.send_state != 0 && var1.id <= 0) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public boolean isSticker() {
      int var1 = this.type;
      if (var1 != 1000) {
         boolean var2;
         if (var1 == 13) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      } else {
         return isStickerMessage(this.messageOwner);
      }
   }

   public boolean isUnread() {
      return this.messageOwner.unread;
   }

   public boolean isVideo() {
      return isVideoMessage(this.messageOwner);
   }

   public boolean isVoice() {
      return isVoiceMessage(this.messageOwner);
   }

   public boolean isVoted() {
      if (this.type != 17) {
         return false;
      } else {
         TLRPC.TL_messageMediaPoll var1 = (TLRPC.TL_messageMediaPoll)this.messageOwner.media;
         TLRPC.TL_pollResults var2 = var1.results;
         if (var2 != null && !var2.results.isEmpty()) {
            int var3 = var1.results.results.size();

            for(int var4 = 0; var4 < var3; ++var4) {
               if (((TLRPC.TL_pollAnswerVoters)var1.results.results.get(var4)).chosen) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean isWallpaper() {
      TLRPC.MessageMedia var1 = this.messageOwner.media;
      boolean var2;
      if (var1 instanceof TLRPC.TL_messageMediaWebPage) {
         TLRPC.WebPage var3 = var1.webpage;
         if (var3 != null && "telegram_background".equals(var3.type)) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public boolean isWebpage() {
      return this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage;
   }

   public boolean isWebpageDocument() {
      TLRPC.MessageMedia var1 = this.messageOwner.media;
      boolean var2;
      if (var1 instanceof TLRPC.TL_messageMediaWebPage) {
         TLRPC.Document var3 = var1.webpage.document;
         if (var3 != null && !isGifDocument(var3)) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public void measureInlineBotButtons() {
      this.wantedBotKeyboardWidth = 0;
      if (this.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) {
         Theme.createChatResources((Context)null, true);
         StringBuilder var1 = this.botButtonsLayout;
         if (var1 == null) {
            this.botButtonsLayout = new StringBuilder();
         } else {
            var1.setLength(0);
         }

         for(int var2 = 0; var2 < this.messageOwner.reply_markup.rows.size(); ++var2) {
            TLRPC.TL_keyboardButtonRow var3 = (TLRPC.TL_keyboardButtonRow)this.messageOwner.reply_markup.rows.get(var2);
            int var4 = var3.buttons.size();
            int var5 = 0;

            int var6;
            int var8;
            for(var6 = 0; var5 < var4; var6 = var8) {
               TLRPC.KeyboardButton var7 = (TLRPC.KeyboardButton)var3.buttons.get(var5);
               var1 = this.botButtonsLayout;
               var1.append(var2);
               var1.append(var5);
               Object var12;
               if (var7 instanceof TLRPC.TL_keyboardButtonBuy && (this.messageOwner.media.flags & 4) != 0) {
                  var12 = LocaleController.getString("PaymentReceipt", 2131560388);
               } else {
                  var12 = Emoji.replaceEmoji(var7.text, Theme.chat_msgBotButtonPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0F), false);
               }

               StaticLayout var13 = new StaticLayout((CharSequence)var12, Theme.chat_msgBotButtonPaint, AndroidUtilities.dp(2000.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
               var8 = var6;
               if (var13.getLineCount() > 0) {
                  float var9 = var13.getLineWidth(0);
                  float var10 = var13.getLineLeft(0);
                  float var11 = var9;
                  if (var10 < var9) {
                     var11 = var9 - var10;
                  }

                  var8 = Math.max(var6, (int)Math.ceil((double)var11) + AndroidUtilities.dp(4.0F));
               }

               ++var5;
            }

            this.wantedBotKeyboardWidth = Math.max(this.wantedBotKeyboardWidth, (var6 + AndroidUtilities.dp(12.0F)) * var4 + AndroidUtilities.dp(5.0F) * (var4 - 1));
         }

      }
   }

   public boolean needDrawAvatar() {
      boolean var2;
      if (!this.isFromUser() && this.eventId == 0L) {
         TLRPC.MessageFwdHeader var1 = this.messageOwner.fwd_from;
         if (var1 == null || var1.saved_from_peer == null) {
            var2 = false;
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   public boolean needDrawBluredPreview() {
      TLRPC.Message var1 = this.messageOwner;
      boolean var2 = var1 instanceof TLRPC.TL_message_secret;
      boolean var3 = true;
      boolean var4 = true;
      if (var2) {
         int var5 = Math.max(var1.ttl, var1.media.ttl_seconds);
         if (var5 > 0) {
            if (this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto || this.isVideo() || this.isGif()) {
               var3 = var4;
               if (var5 <= 60) {
                  return var3;
               }
            }

            if (this.isRoundVideo()) {
               var3 = var4;
               return var3;
            }
         }

         var3 = false;
         return var3;
      } else if (!(var1 instanceof TLRPC.TL_message)) {
         return false;
      } else {
         TLRPC.MessageMedia var6 = var1.media;
         if (!(var6 instanceof TLRPC.TL_messageMediaPhoto) && !(var6 instanceof TLRPC.TL_messageMediaDocument) || this.messageOwner.media.ttl_seconds == 0) {
            var3 = false;
         }

         return var3;
      }
   }

   public boolean needDrawForwarded() {
      TLRPC.Message var1 = this.messageOwner;
      boolean var3;
      if ((var1.flags & 4) != 0) {
         TLRPC.MessageFwdHeader var4 = var1.fwd_from;
         if (var4 != null) {
            TLRPC.Peer var2 = var4.saved_from_peer;
            if ((var2 == null || var2.channel_id != var4.channel_id) && (long)UserConfig.getInstance(this.currentAccount).getClientUserId() != this.getDialogId()) {
               var3 = true;
               return var3;
            }
         }
      }

      var3 = false;
      return var3;
   }

   public boolean needDrawShareButton() {
      if (this.eventId != 0L) {
         return false;
      } else {
         TLRPC.MessageFwdHeader var1 = this.messageOwner.fwd_from;
         boolean var2 = true;
         if (var1 != null && !this.isOutOwner() && this.messageOwner.fwd_from.saved_from_peer != null && this.getDialogId() == (long)UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            return true;
         } else {
            int var3 = this.type;
            if (var3 != 13 && var3 != 15) {
               var1 = this.messageOwner.fwd_from;
               if (var1 != null && var1.channel_id != 0 && !this.isOutOwner()) {
                  return true;
               }

               if (this.isFromUser()) {
                  TLRPC.MessageMedia var6 = this.messageOwner.media;
                  if (var6 instanceof TLRPC.TL_messageMediaEmpty || var6 == null || var6 instanceof TLRPC.TL_messageMediaWebPage && !(var6.webpage instanceof TLRPC.TL_webPage)) {
                     return false;
                  }

                  TLRPC.User var7 = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.from_id);
                  if (var7 != null && var7.bot) {
                     return true;
                  }

                  if (!this.isOut()) {
                     var6 = this.messageOwner.media;
                     boolean var4 = var2;
                     if (var6 instanceof TLRPC.TL_messageMediaGame) {
                        return var4;
                     }

                     if (var6 instanceof TLRPC.TL_messageMediaInvoice) {
                        var4 = var2;
                        return var4;
                     }

                     if (this.isMegagroup()) {
                        TLRPC.Chat var8 = MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.to_id.channel_id);
                        if (var8 != null) {
                           String var9 = var8.username;
                           if (var9 != null && var9.length() > 0) {
                              var6 = this.messageOwner.media;
                              if (!(var6 instanceof TLRPC.TL_messageMediaContact) && !(var6 instanceof TLRPC.TL_messageMediaGeo)) {
                                 var4 = var2;
                                 return var4;
                              }
                           }
                        }

                        var4 = false;
                        return var4;
                     }
                  }
               } else {
                  TLRPC.Message var5 = this.messageOwner;
                  if (var5.from_id < 0 || var5.post) {
                     var5 = this.messageOwner;
                     if (var5.to_id.channel_id != 0) {
                        if (var5.via_bot_id == 0 && var5.reply_to_msg_id == 0) {
                           return true;
                        }

                        var3 = this.type;
                        if (var3 != 13 && var3 != 15) {
                           return true;
                        }
                     }
                  }
               }
            }

            return false;
         }
      }
   }

   public CharSequence replaceWithLink(CharSequence var1, String var2, ArrayList var3, AbstractMap var4, SparseArray var5) {
      CharSequence var6 = var1;
      if (TextUtils.indexOf(var1, var2) >= 0) {
         SpannableStringBuilder var7 = new SpannableStringBuilder("");

         for(int var8 = 0; var8 < var3.size(); ++var8) {
            TLRPC.User var12 = null;
            if (var4 != null) {
               var12 = (TLRPC.User)var4.get(var3.get(var8));
            } else if (var5 != null) {
               var12 = (TLRPC.User)var5.get((Integer)var3.get(var8));
            }

            TLRPC.User var9 = var12;
            if (var12 == null) {
               var9 = MessagesController.getInstance(this.currentAccount).getUser((Integer)var3.get(var8));
            }

            if (var9 != null) {
               String var13 = UserObject.getUserName(var9);
               int var10 = var7.length();
               if (var7.length() != 0) {
                  var7.append(", ");
               }

               var7.append(var13);
               StringBuilder var11 = new StringBuilder();
               var11.append("");
               var11.append(var9.id);
               var7.setSpan(new URLSpanNoUnderlineBold(var11.toString()), var10, var13.length() + var10, 33);
            }
         }

         var6 = TextUtils.replace(var1, new String[]{var2}, new CharSequence[]{var7});
      }

      return var6;
   }

   public CharSequence replaceWithLink(CharSequence var1, String var2, TLObject var3) {
      int var4 = TextUtils.indexOf(var1, var2);
      if (var4 >= 0) {
         String var5;
         StringBuilder var6;
         String var10;
         if (var3 instanceof TLRPC.User) {
            TLRPC.User var9 = (TLRPC.User)var3;
            var5 = UserObject.getUserName(var9);
            var6 = new StringBuilder();
            var6.append("");
            var6.append(var9.id);
            var10 = var6.toString();
         } else if (var3 instanceof TLRPC.Chat) {
            TLRPC.Chat var11 = (TLRPC.Chat)var3;
            var5 = var11.title;
            var6 = new StringBuilder();
            var6.append("");
            var6.append(-var11.id);
            var10 = var6.toString();
         } else if (var3 instanceof TLRPC.TL_game) {
            var5 = ((TLRPC.TL_game)var3).title;
            var10 = "game";
         } else {
            var10 = "0";
            var5 = "";
         }

         var5 = var5.replace('\n', ' ');
         SpannableStringBuilder var8 = new SpannableStringBuilder(TextUtils.replace(var1, new String[]{var2}, new String[]{var5}));
         StringBuilder var7 = new StringBuilder();
         var7.append("");
         var7.append(var10);
         var8.setSpan(new URLSpanNoUnderlineBold(var7.toString()), var4, var5.length() + var4, 33);
         return var8;
      } else {
         return var1;
      }
   }

   public void resetLayout() {
      this.layoutCreated = false;
   }

   public void resetPlayingProgress() {
      this.audioProgress = 0.0F;
      this.audioProgressSec = 0;
      this.bufferedProgress = 0.0F;
   }

   public void setContentIsRead() {
      this.messageOwner.media_unread = false;
   }

   public void setIsRead() {
      this.messageOwner.unread = false;
   }

   public void setType() {
      int var1 = this.type;
      this.isRoundVideoCached = 0;
      TLRPC.Message var2 = this.messageOwner;
      if (!(var2 instanceof TLRPC.TL_message) && !(var2 instanceof TLRPC.TL_messageForwarded_old2)) {
         if (var2 instanceof TLRPC.TL_messageService) {
            TLRPC.MessageAction var5 = var2.action;
            if (var5 instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
               this.type = 0;
            } else if (!(var5 instanceof TLRPC.TL_messageActionChatEditPhoto) && !(var5 instanceof TLRPC.TL_messageActionUserUpdatedPhoto)) {
               if (var5 instanceof TLRPC.TL_messageEncryptedAction) {
                  TLRPC.DecryptedMessageAction var6 = var5.encryptedAction;
                  if (!(var6 instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) && !(var6 instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL)) {
                     this.contentType = -1;
                     this.type = -1;
                  } else {
                     this.contentType = 1;
                     this.type = 10;
                  }
               } else if (var5 instanceof TLRPC.TL_messageActionHistoryClear) {
                  this.contentType = -1;
                  this.type = -1;
               } else if (var5 instanceof TLRPC.TL_messageActionPhoneCall) {
                  this.type = 16;
               } else {
                  this.contentType = 1;
                  this.type = 10;
               }
            } else {
               this.contentType = 1;
               this.type = 11;
            }
         }
      } else if (this.isMediaEmpty()) {
         this.type = 0;
         if (TextUtils.isEmpty(this.messageText) && this.eventId == 0L) {
            this.messageText = "Empty message";
         }
      } else {
         TLRPC.MessageMedia var3 = this.messageOwner.media;
         if (var3.ttl_seconds != 0 && (var3.photo instanceof TLRPC.TL_photoEmpty || var3.document instanceof TLRPC.TL_documentEmpty)) {
            this.contentType = 1;
            this.type = 10;
         } else {
            var3 = this.messageOwner.media;
            if (var3 instanceof TLRPC.TL_messageMediaPhoto) {
               this.type = 1;
            } else if (!(var3 instanceof TLRPC.TL_messageMediaGeo) && !(var3 instanceof TLRPC.TL_messageMediaVenue) && !(var3 instanceof TLRPC.TL_messageMediaGeoLive)) {
               if (this.isRoundVideo()) {
                  this.type = 5;
               } else if (this.isVideo()) {
                  this.type = 3;
               } else if (this.isVoice()) {
                  this.type = 2;
               } else if (this.isMusic()) {
                  this.type = 14;
               } else {
                  var3 = this.messageOwner.media;
                  if (var3 instanceof TLRPC.TL_messageMediaContact) {
                     this.type = 12;
                  } else if (var3 instanceof TLRPC.TL_messageMediaPoll) {
                     this.type = 17;
                  } else if (var3 instanceof TLRPC.TL_messageMediaUnsupported) {
                     this.type = 0;
                  } else if (var3 instanceof TLRPC.TL_messageMediaDocument) {
                     TLRPC.Document var4 = var3.document;
                     if (var4 != null && var4.mime_type != null) {
                        if (isGifDocument(var4)) {
                           this.type = 8;
                        } else if (this.isSticker()) {
                           this.type = 13;
                        } else if (this.isAnimatedSticker()) {
                           this.type = 15;
                        } else {
                           this.type = 9;
                        }
                     } else {
                        this.type = 9;
                     }
                  } else if (var3 instanceof TLRPC.TL_messageMediaGame) {
                     this.type = 0;
                  } else if (var3 instanceof TLRPC.TL_messageMediaInvoice) {
                     this.type = 0;
                  }
               }
            } else {
               this.type = 4;
            }
         }
      }

      if (var1 != 1000 && var1 != this.type) {
         this.generateThumbs(false);
      }

   }

   public boolean shouldDrawWithoutBackground() {
      int var1 = this.type;
      boolean var2;
      if (var1 != 13 && var1 != 15 && var1 != 5) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public boolean shouldEncryptPhotoOrVideo() {
      return shouldEncryptPhotoOrVideo(this.messageOwner);
   }

   public static class GroupedMessagePosition {
      public float aspectRatio;
      public boolean edge;
      public int flags;
      public boolean last;
      public int leftSpanOffset;
      public byte maxX;
      public byte maxY;
      public byte minX;
      public byte minY;
      public float ph;
      public int pw;
      public float[] siblingHeights;
      public int spanSize;

      public void set(int var1, int var2, int var3, int var4, int var5, float var6, int var7) {
         this.minX = (byte)((byte)var1);
         this.maxX = (byte)((byte)var2);
         this.minY = (byte)((byte)var3);
         this.maxY = (byte)((byte)var4);
         this.pw = var5;
         this.spanSize = var5;
         this.ph = var6;
         this.flags = (byte)var7;
      }
   }

   public static class GroupedMessages {
      private int firstSpanAdditionalSize = 200;
      public long groupId;
      public boolean hasSibling;
      private int maxSizeWidth = 800;
      public ArrayList messages = new ArrayList();
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
         int var1 = this.messages.size();
         byte var2 = 1;
         if (var1 > 1) {
            StringBuilder var3 = new StringBuilder();
            this.hasSibling = false;
            int var4 = 0;
            boolean var5 = false;
            float var6 = 1.0F;
            boolean var7 = false;

            boolean var8;
            MessageObject var9;
            boolean var12;
            float var13;
            MessageObject.GroupedMessagePosition var44;
            for(var8 = false; var4 < var1; ++var4) {
               var9 = (MessageObject)this.messages.get(var4);
               if (var4 == 0) {
                  label426: {
                     label393: {
                        var8 = var9.isOutOwner();
                        if (!var8) {
                           TLRPC.MessageFwdHeader var10 = var9.messageOwner.fwd_from;
                           if (var10 != null && var10.saved_from_peer != null) {
                              break label393;
                           }

                           TLRPC.Message var40 = var9.messageOwner;
                           if (var40.from_id > 0) {
                              TLRPC.Peer var11 = var40.to_id;
                              if (var11.channel_id != 0 || var11.chat_id != 0) {
                                 break label393;
                              }

                              TLRPC.MessageMedia var41 = var40.media;
                              if (var41 instanceof TLRPC.TL_messageMediaGame || var41 instanceof TLRPC.TL_messageMediaInvoice) {
                                 break label393;
                              }
                           }
                        }

                        var5 = false;
                        break label426;
                     }

                     var5 = true;
                  }
               }

               TLRPC.PhotoSize var42 = FileLoader.getClosestPhotoSizeWithSize(var9.photoThumbs, AndroidUtilities.getPhotoSize());
               var44 = new MessageObject.GroupedMessagePosition();
               if (var4 == var1 - 1) {
                  var12 = true;
               } else {
                  var12 = false;
               }

               var44.last = var12;
               if (var42 == null) {
                  var13 = 1.0F;
               } else {
                  var13 = (float)var42.w / (float)var42.h;
               }

               var44.aspectRatio = var13;
               var13 = var44.aspectRatio;
               if (var13 > 1.2F) {
                  var3.append("w");
               } else if (var13 < 0.8F) {
                  var3.append("n");
               } else {
                  var3.append("q");
               }

               var13 = var44.aspectRatio;
               var6 += var13;
               if (var13 > 2.0F) {
                  var7 = true;
               }

               this.positions.put(var9, var44);
               this.posArray.add(var44);
            }

            if (var5) {
               this.maxSizeWidth -= 50;
               this.firstSpanAdditionalSize += 50;
            }

            int var14 = AndroidUtilities.dp(120.0F);
            var13 = (float)AndroidUtilities.dp(120.0F);
            Point var38 = AndroidUtilities.displaySize;
            int var35 = (int)(var13 / ((float)Math.min(var38.x, var38.y) / (float)this.maxSizeWidth));
            var13 = (float)AndroidUtilities.dp(40.0F);
            var38 = AndroidUtilities.displaySize;
            float var15 = (float)Math.min(var38.x, var38.y);
            int var16 = this.maxSizeWidth;
            var4 = (int)(var13 / (var15 / (float)var16));
            var13 = (float)var16 / 814.0F;
            var15 = var6 / (float)var1;
            var6 = (float)AndroidUtilities.dp(100.0F) / 814.0F;
            MessageObject.GroupedMessagePosition var21;
            float var22;
            int var29;
            MessageObject.GroupedMessagePosition var34;
            int var36;
            MessageObject.GroupedMessagePosition var39;
            if (var7 || var1 != 2 && var1 != 3 && var1 != 4) {
               var12 = var8;
               float[] var46 = new float[this.posArray.size()];

               for(var36 = 0; var36 < var1; ++var36) {
                  if (var15 > 1.1F) {
                     var46[var36] = Math.max(1.0F, ((MessageObject.GroupedMessagePosition)this.posArray.get(var36)).aspectRatio);
                  } else {
                     var46[var36] = Math.min(1.0F, ((MessageObject.GroupedMessagePosition)this.posArray.get(var36)).aspectRatio);
                  }

                  var46[var36] = Math.max(0.66667F, Math.min(1.7F, var46[var36]));
               }

               ArrayList var43 = new ArrayList();

               for(var36 = 1; var36 < var46.length; ++var36) {
                  var4 = var46.length - var36;
                  if (var36 <= 3 && var4 <= 3) {
                     var43.add(new MessageObject.GroupedMessages.MessageGroupedLayoutAttempt(var36, var4, this.multiHeight(var46, 0, var36), this.multiHeight(var46, var36, var46.length)));
                  }
               }

               var29 = var1;
               var1 = 1;

               for(var13 = var6; var1 < var46.length - 1; ++var1) {
                  for(var36 = 1; var36 < var46.length - var1; ++var36) {
                     var14 = var46.length - var1 - var36;
                     if (var1 <= 3) {
                        byte var37;
                        if (var15 < 0.85F) {
                           var37 = 4;
                        } else {
                           var37 = 3;
                        }

                        if (var36 <= var37 && var14 <= 3) {
                           var6 = this.multiHeight(var46, 0, var1);
                           var4 = var1 + var36;
                           var43.add(new MessageObject.GroupedMessages.MessageGroupedLayoutAttempt(var1, var36, var14, var6, this.multiHeight(var46, var1, var4), this.multiHeight(var46, var4, var46.length)));
                        }
                     }
                  }
               }

               int var23;
               for(var1 = 1; var1 < var46.length - 2; ++var1) {
                  for(var36 = 1; var36 < var46.length - var1; ++var36) {
                     for(var4 = 1; var4 < var46.length - var1 - var36; ++var4) {
                        var14 = var46.length - var1 - var36 - var4;
                        if (var1 <= 3 && var36 <= 3 && var4 <= 3 && var14 <= 3) {
                           var6 = this.multiHeight(var46, 0, var1);
                           var23 = var1 + var36;
                           var15 = this.multiHeight(var46, var1, var23);
                           var16 = var23 + var4;
                           var43.add(new MessageObject.GroupedMessages.MessageGroupedLayoutAttempt(var1, var36, var4, var14, var6, var15, this.multiHeight(var46, var23, var16), this.multiHeight(var46, var16, var46.length)));
                        }
                     }
                  }
               }

               float var24 = (float)(this.maxSizeWidth / 3 * 4);
               MessageObject.GroupedMessages.MessageGroupedLayoutAttempt var47 = null;
               var1 = 0;

               for(var15 = 0.0F; var1 < var43.size(); var15 = var22) {
                  MessageObject.GroupedMessages.MessageGroupedLayoutAttempt var31 = (MessageObject.GroupedMessages.MessageGroupedLayoutAttempt)var43.get(var1);
                  var36 = 0;
                  float var25 = 0.0F;
                  var22 = Float.MAX_VALUE;

                  while(true) {
                     float[] var48 = var31.heights;
                     if (var36 >= var48.length) {
                        var25 = Math.abs(var25 - var24);
                        int[] var49 = var31.lineCounts;
                        if (var49.length <= 1) {
                           var6 = var25;
                        } else {
                           label424: {
                              if (var49[0] <= var49[1] && (var49.length <= 2 || var49[1] <= var49[2])) {
                                 var49 = var31.lineCounts;
                                 var6 = var25;
                                 if (var49.length <= 3) {
                                    break label424;
                                 }

                                 var6 = var25;
                                 if (var49[2] <= var49[3]) {
                                    break label424;
                                 }
                              }

                              var6 = var25 * 1.2F;
                           }
                        }

                        var25 = var6;
                        var6 = var6;
                        if (var22 < (float)var35) {
                           var6 = var25 * 1.5F;
                        }

                        label304: {
                           if (var47 != null) {
                              var22 = var15;
                              if (var6 >= var15) {
                                 break label304;
                              }
                           }

                           var47 = var31;
                           var22 = var6;
                        }

                        ++var1;
                        break;
                     }

                     var25 += var48[var36];
                     var6 = var22;
                     if (var48[var36] < var22) {
                        var6 = var48[var36];
                     }

                     ++var36;
                     var22 = var6;
                  }
               }

               if (var47 == null) {
                  return;
               }

               var14 = 0;
               var36 = 0;
               var4 = 0;

               while(true) {
                  int[] var32 = var47.lineCounts;
                  var35 = var4;
                  var1 = var29;
                  if (var14 >= var32.length) {
                     break;
                  }

                  int var26 = var32[var14];
                  var6 = var47.heights[var14];
                  var1 = this.maxSizeWidth;
                  var39 = null;
                  int var27 = var26 - 1;
                  var16 = Math.max(var4, var27);
                  var35 = var36;

                  for(var4 = 0; var4 < var26; var39 = var34) {
                     int var28 = (int)(var46[var35] * var6);
                     var23 = var1 - var28;
                     var21 = (MessageObject.GroupedMessagePosition)this.posArray.get(var35);
                     byte var33;
                     if (var14 == 0) {
                        var33 = 4;
                     } else {
                        var33 = 0;
                     }

                     var36 = var33;
                     if (var14 == var47.lineCounts.length - 1) {
                        var36 = var33 | 8;
                     }

                     var34 = var39;
                     var1 = var36;
                     if (var4 == 0) {
                        var36 |= 1;
                        var34 = var39;
                        var1 = var36;
                        if (var12) {
                           var34 = var21;
                           var1 = var36;
                        }
                     }

                     var36 = var1;
                     if (var4 == var27) {
                        var1 |= 2;
                        var36 = var1;
                        if (!var12) {
                           var34 = var21;
                           var36 = var1;
                        }
                     }

                     var21.set(var4, var4, var14, var14, var28, Math.max(var13, var6 / 814.0F), var36);
                     ++var35;
                     ++var4;
                     var1 = var23;
                  }

                  var39.pw += var1;
                  var39.spanSize += var1;
                  ++var14;
                  var36 = var35;
                  var4 = var16;
               }
            } else {
               MessageObject.GroupedMessagePosition var45;
               if (var1 == 2) {
                  label430: {
                     var45 = (MessageObject.GroupedMessagePosition)this.posArray.get(0);
                     var39 = (MessageObject.GroupedMessagePosition)this.posArray.get(1);
                     String var30 = var3.toString();
                     if (var30.equals("ww")) {
                        double var17 = (double)var15;
                        double var19 = (double)var13;
                        Double.isNaN(var19);
                        if (var17 > var19 * 1.4D) {
                           var6 = var45.aspectRatio;
                           var13 = var39.aspectRatio;
                           if ((double)(var6 - var13) < 0.2D) {
                              var35 = this.maxSizeWidth;
                              var6 = (float)Math.round(Math.min((float)var35 / var6, Math.min((float)var35 / var13, 407.0F))) / 814.0F;
                              var45.set(0, 0, 0, 0, this.maxSizeWidth, var6, 7);
                              var39.set(0, 0, 1, 1, this.maxSizeWidth, var6, 11);
                              var35 = 0;
                              break label430;
                           }
                        }
                     }

                     if (!var30.equals("ww") && !var30.equals("qq")) {
                        var36 = this.maxSizeWidth;
                        var6 = (float)var36;
                        var15 = (float)var36;
                        var13 = var45.aspectRatio;
                        var14 = (int)Math.max(var6 * 0.4F, (float)Math.round(var15 / var13 / (1.0F / var13 + 1.0F / var39.aspectRatio)));
                        var29 = this.maxSizeWidth - var14;
                        var4 = var14;
                        var36 = var29;
                        if (var29 < var35) {
                           var4 = var14 - (var35 - var29);
                           var36 = var35;
                        }

                        var6 = Math.min(814.0F, (float)Math.round(Math.min((float)var36 / var45.aspectRatio, (float)var4 / var39.aspectRatio))) / 814.0F;
                        var45.set(0, 0, 0, 0, var36, var6, 13);
                        var39.set(1, 1, 0, 0, var4, var6, 14);
                     } else {
                        var35 = this.maxSizeWidth / 2;
                        var6 = (float)var35;
                        var6 = (float)Math.round(Math.min(var6 / var45.aspectRatio, Math.min(var6 / var39.aspectRatio, 814.0F))) / 814.0F;
                        var45.set(0, 0, 0, 0, var35, var6, 13);
                        var39.set(1, 1, 0, 0, var35, var6, 14);
                     }

                     var35 = 1;
                  }
               } else if (var1 == 3) {
                  var39 = (MessageObject.GroupedMessagePosition)this.posArray.get(0);
                  var44 = (MessageObject.GroupedMessagePosition)this.posArray.get(1);
                  var45 = (MessageObject.GroupedMessagePosition)this.posArray.get(2);
                  if (var3.charAt(0) == 'n') {
                     var6 = var44.aspectRatio;
                     var13 = Math.min(407.0F, (float)Math.round((float)this.maxSizeWidth * var6 / (var45.aspectRatio + var6)));
                     var6 = 814.0F - var13;
                     var35 = (int)Math.max((float)var35, Math.min((float)this.maxSizeWidth * 0.5F, (float)Math.round(Math.min(var45.aspectRatio * var13, var44.aspectRatio * var6))));
                     var36 = Math.round(Math.min(var39.aspectRatio * 814.0F + (float)var4, (float)(this.maxSizeWidth - var35)));
                     var39.set(0, 0, 0, 1, var36, 1.0F, 13);
                     var6 /= 814.0F;
                     var44.set(1, 1, 0, 0, var35, var6, 6);
                     var13 /= 814.0F;
                     var45.set(0, 1, 1, 1, var35, var13, 10);
                     var4 = this.maxSizeWidth;
                     var45.spanSize = var4;
                     var39.siblingHeights = new float[]{var13, var6};
                     if (var8) {
                        var39.spanSize = var4 - var35;
                     } else {
                        var44.spanSize = var4 - var36;
                        var45.leftSpanOffset = var36;
                     }

                     this.hasSibling = true;
                     var35 = var2;
                  } else {
                     var15 = (float)Math.round(Math.min((float)this.maxSizeWidth / var39.aspectRatio, 537.24005F)) / 814.0F;
                     var39.set(0, 1, 0, 0, this.maxSizeWidth, var15, 7);
                     var35 = this.maxSizeWidth / 2;
                     var13 = (float)var35;
                     var15 = Math.min(814.0F - var15, (float)Math.round(Math.min(var13 / var44.aspectRatio, var13 / var45.aspectRatio))) / 814.0F;
                     var13 = var15;
                     if (var15 < var6) {
                        var13 = var6;
                     }

                     var44.set(0, 0, 1, 1, var35, var13, 9);
                     var45.set(1, 1, 1, 1, var35, var13, 10);
                     var35 = var2;
                  }
               } else if (var1 == 4) {
                  var39 = (MessageObject.GroupedMessagePosition)this.posArray.get(0);
                  var44 = (MessageObject.GroupedMessagePosition)this.posArray.get(1);
                  var21 = (MessageObject.GroupedMessagePosition)this.posArray.get(2);
                  var45 = (MessageObject.GroupedMessagePosition)this.posArray.get(3);
                  if (var3.charAt(0) == 'w') {
                     var15 = (float)Math.round(Math.min((float)this.maxSizeWidth / var39.aspectRatio, 537.24005F)) / 814.0F;
                     var39.set(0, 2, 0, 0, this.maxSizeWidth, var15, 7);
                     var13 = (float)Math.round((float)this.maxSizeWidth / (var44.aspectRatio + var21.aspectRatio + var45.aspectRatio));
                     var22 = (float)var35;
                     var14 = (int)Math.max(var22, Math.min((float)this.maxSizeWidth * 0.4F, var44.aspectRatio * var13));
                     var29 = (int)Math.max(Math.max(var22, (float)this.maxSizeWidth * 0.33F), var45.aspectRatio * var13);
                     var16 = this.maxSizeWidth - var14 - var29;
                     var4 = var29;
                     var36 = var14;
                     var35 = var16;
                     if (var16 < AndroidUtilities.dp(58.0F)) {
                        var4 = AndroidUtilities.dp(58.0F) - var16;
                        var35 = AndroidUtilities.dp(58.0F);
                        var16 = var4 / 2;
                        var36 = var14 - var16;
                        var4 = var29 - (var4 - var16);
                     }

                     var15 = Math.min(814.0F - var15, var13) / 814.0F;
                     var13 = var15;
                     if (var15 < var6) {
                        var13 = var6;
                     }

                     var44.set(0, 0, 1, 1, var36, var13, 9);
                     var21.set(1, 1, 1, 1, var35, var13, 8);
                     var45.set(2, 2, 1, 1, var4, var13, 10);
                     var35 = 2;
                  } else {
                     var35 = Math.max(var35, Math.round(814.0F / (1.0F / var44.aspectRatio + 1.0F / var21.aspectRatio + 1.0F / var45.aspectRatio)));
                     var13 = (float)var14;
                     var15 = (float)var35;
                     var6 = Math.min(0.33F, Math.max(var13, var15 / var44.aspectRatio) / 814.0F);
                     var15 = Math.min(0.33F, Math.max(var13, var15 / var21.aspectRatio) / 814.0F);
                     var13 = 1.0F - var6 - var15;
                     var36 = Math.round(Math.min(814.0F * var39.aspectRatio + (float)var4, (float)(this.maxSizeWidth - var35)));
                     var39.set(0, 0, 0, 2, var36, var6 + var15 + var13, 13);
                     var44.set(1, 1, 0, 0, var35, var6, 6);
                     var21.set(0, 1, 1, 1, var35, var15, 2);
                     var21.spanSize = this.maxSizeWidth;
                     var45.set(0, 1, 2, 2, var35, var13, 10);
                     var4 = this.maxSizeWidth;
                     var45.spanSize = var4;
                     if (var8) {
                        var39.spanSize = var4 - var35;
                     } else {
                        var44.spanSize = var4 - var36;
                        var21.leftSpanOffset = var36;
                        var45.leftSpanOffset = var36;
                     }

                     var39.siblingHeights = new float[]{var6, var15, var13};
                     this.hasSibling = true;
                     var35 = 1;
                  }
               } else {
                  var35 = 0;
               }
            }

            for(var36 = 0; var36 < var1; ++var36) {
               var34 = (MessageObject.GroupedMessagePosition)this.posArray.get(var36);
               if (var8) {
                  if (var34.minX == 0) {
                     var34.spanSize += this.firstSpanAdditionalSize;
                  }

                  if ((var34.flags & 2) != 0) {
                     var34.edge = true;
                  }
               } else {
                  if (var34.maxX == var35 || (var34.flags & 2) != 0) {
                     var34.spanSize += this.firstSpanAdditionalSize;
                  }

                  if ((var34.flags & 1) != 0) {
                     var34.edge = true;
                  }
               }

               var9 = (MessageObject)this.messages.get(var36);
               if (!var8 && var9.needDrawAvatarInternal()) {
                  if (var34.edge) {
                     var4 = var34.spanSize;
                     if (var4 != 1000) {
                        var34.spanSize = var4 + 108;
                     }

                     var34.pw += 108;
                  } else if ((var34.flags & 2) != 0) {
                     var4 = var34.spanSize;
                     if (var4 != 1000) {
                        var34.spanSize = var4 - 108;
                     } else {
                        var4 = var34.leftSpanOffset;
                        if (var4 != 0) {
                           var34.leftSpanOffset = var4 + 108;
                        }
                     }
                  }
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

   public static class TextLayoutBlock {
      public int charactersEnd;
      public int charactersOffset;
      public byte directionFlags;
      public int height;
      public int heightByOffset;
      public StaticLayout textLayout;
      public float textYOffset;

      public boolean isRtl() {
         byte var1 = this.directionFlags;
         boolean var2;
         if ((var1 & 1) != 0 && (var1 & 2) == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }
   }

   public static class VCardData {
      private String company;
      private ArrayList emails = new ArrayList();
      private ArrayList phones = new ArrayList();

      public static CharSequence parse(String param0) {
         // $FF: Couldn't be decompiled
      }
   }
}
