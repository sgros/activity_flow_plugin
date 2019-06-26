// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.StringReader;
import android.text.StaticLayout;
import android.text.Layout$Alignment;
import android.net.Uri;
import java.net.URLEncoder;
import android.graphics.Point;
import java.util.Collection;
import android.text.SpannableStringBuilder;
import android.text.Spannable$Factory;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.SerializedData;
import android.util.Base64;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import org.telegram.tgnet.ConnectionsManager;
import java.util.regex.Matcher;
import android.text.util.Linkify;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.ui.Components.URLSpanBrowser;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanBotCommand;
import org.telegram.ui.Components.URLSpanUserMention;
import android.text.style.URLSpan;
import android.text.Spannable;
import java.util.HashMap;
import android.text.TextPaint;
import java.util.GregorianCalendar;
import android.text.TextUtils;
import org.telegram.ui.Components.URLSpanNoUnderlineBold;
import org.telegram.ui.Components.TypefaceSpan;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.content.Context;
import org.telegram.ui.ActionBar.Theme;
import java.util.AbstractMap;
import android.util.SparseArray;
import org.telegram.tgnet.TLObject;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;
import java.util.regex.Pattern;

public class MessageObject
{
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
    static final String[] excludeWords;
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
    public ArrayList<TLRPC.MessageEntity> editingMessageEntities;
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
    public ArrayList<TLRPC.PhotoSize> photoThumbs;
    public ArrayList<TLRPC.PhotoSize> photoThumbs2;
    public TLObject photoThumbsObject;
    public TLObject photoThumbsObject2;
    public long pollLastCheckTime;
    public boolean pollVisibleOnScreen;
    public String previousAttachPath;
    public String previousCaption;
    public ArrayList<TLRPC.MessageEntity> previousCaptionEntities;
    public TLRPC.MessageMedia previousMedia;
    public MessageObject replyMessageObject;
    public boolean resendAsIs;
    public int textHeight;
    public ArrayList<TextLayoutBlock> textLayoutBlocks;
    public int textWidth;
    public float textXOffset;
    public int type;
    public boolean useCustomPhoto;
    public CharSequence vCardData;
    public VideoEditedInfo videoEditedInfo;
    public boolean viewsReloaded;
    public int wantedBotKeyboardWidth;
    
    static {
        excludeWords = new String[] { " vs. ", " vs ", " versus ", " ft. ", " ft ", " featuring ", " feat. ", " feat ", " presents ", " pres. ", " pres ", " and ", " & ", " . " };
    }
    
    public MessageObject(final int n, final TLRPC.Message message, final SparseArray<TLRPC.User> sparseArray, final SparseArray<TLRPC.Chat> sparseArray2, final boolean b) {
        this(n, message, null, null, sparseArray, sparseArray2, b, 0L);
    }
    
    public MessageObject(final int n, final TLRPC.Message message, final SparseArray<TLRPC.User> sparseArray, final boolean b) {
        this(n, message, sparseArray, null, b);
    }
    
    public MessageObject(final int currentAccount, final TLRPC.Message messageOwner, final String messageText, final String localName, final String localUserName, final boolean b, final boolean localChannel, final boolean localEdit) {
        this.type = 1000;
        int localType;
        if (b) {
            localType = 2;
        }
        else {
            localType = 1;
        }
        this.localType = localType;
        this.currentAccount = currentAccount;
        this.localName = localName;
        this.localUserName = localUserName;
        this.messageText = messageText;
        this.messageOwner = messageOwner;
        this.localChannel = localChannel;
        this.localEdit = localEdit;
    }
    
    public MessageObject(int n, final TLRPC.Message messageOwner, final AbstractMap<Integer, TLRPC.User> abstractMap, final AbstractMap<Integer, TLRPC.Chat> abstractMap2, final SparseArray<TLRPC.User> sparseArray, final SparseArray<TLRPC.Chat> sparseArray2, boolean layoutCreated, long eventId) {
        this.type = 1000;
        Theme.createChatResources(null, true);
        this.currentAccount = n;
        this.messageOwner = messageOwner;
        this.eventId = eventId;
        final TLRPC.Message replyMessage = messageOwner.replyMessage;
        if (replyMessage != null) {
            this.replyMessageObject = new MessageObject(n, replyMessage, abstractMap, abstractMap2, sparseArray, sparseArray2, false, eventId);
        }
        final int from_id = messageOwner.from_id;
        TLRPC.User user2;
        if (from_id > 0) {
            TLRPC.User user;
            if (abstractMap != null) {
                user = abstractMap.get(from_id);
            }
            else if (sparseArray != null) {
                user = (TLRPC.User)sparseArray.get(from_id);
            }
            else {
                user = null;
            }
            user2 = user;
            if (user == null) {
                user2 = MessagesController.getInstance(n).getUser(messageOwner.from_id);
            }
        }
        else {
            user2 = null;
        }
        TLRPC.User user7 = null;
        Label_4658: {
            if (messageOwner instanceof TLRPC.TL_messageService) {
                final TLRPC.MessageAction action = messageOwner.action;
                if (action != null) {
                    if (action instanceof TLRPC.TL_messageActionCustomAction) {
                        this.messageText = action.message;
                    }
                    else if (action instanceof TLRPC.TL_messageActionChatCreate) {
                        if (this.isOut()) {
                            this.messageText = LocaleController.getString("ActionYouCreateGroup", 2131558549);
                        }
                        else {
                            this.messageText = this.replaceWithLink(LocaleController.getString("ActionCreateGroup", 2131558517), "un1", user2);
                        }
                    }
                    else if (action instanceof TLRPC.TL_messageActionChatDeleteUser) {
                        final int user_id = action.user_id;
                        if (user_id == messageOwner.from_id) {
                            if (this.isOut()) {
                                this.messageText = LocaleController.getString("ActionYouLeftUser", 2131558551);
                            }
                            else {
                                this.messageText = this.replaceWithLink(LocaleController.getString("ActionLeftUser", 2131558523), "un1", user2);
                            }
                        }
                        else {
                            TLRPC.User user3;
                            if (abstractMap != null) {
                                user3 = abstractMap.get(user_id);
                            }
                            else if (sparseArray != null) {
                                user3 = (TLRPC.User)sparseArray.get(user_id);
                            }
                            else {
                                user3 = null;
                            }
                            TLRPC.User user4 = user3;
                            if (user3 == null) {
                                user4 = MessagesController.getInstance(n).getUser(messageOwner.action.user_id);
                            }
                            if (this.isOut()) {
                                this.messageText = this.replaceWithLink(LocaleController.getString("ActionYouKickUser", 2131558550), "un2", user4);
                            }
                            else if (messageOwner.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                this.messageText = this.replaceWithLink(LocaleController.getString("ActionKickUserYou", 2131558522), "un1", user2);
                            }
                            else {
                                this.messageText = this.replaceWithLink(LocaleController.getString("ActionKickUser", 2131558521), "un2", user4);
                                this.messageText = this.replaceWithLink(this.messageText, "un1", user2);
                            }
                        }
                    }
                    else if (action instanceof TLRPC.TL_messageActionChatAddUser) {
                        final TLRPC.MessageAction action2 = this.messageOwner.action;
                        final int user_id2 = action2.user_id;
                        int intValue;
                        if ((intValue = user_id2) == 0) {
                            intValue = user_id2;
                            if (action2.users.size() == 1) {
                                intValue = this.messageOwner.action.users.get(0);
                            }
                        }
                        if (intValue != 0) {
                            TLRPC.User user5;
                            if (abstractMap != null) {
                                user5 = abstractMap.get(intValue);
                            }
                            else if (sparseArray != null) {
                                user5 = (TLRPC.User)sparseArray.get(intValue);
                            }
                            else {
                                user5 = null;
                            }
                            TLRPC.User user6 = user5;
                            if (user5 == null) {
                                user6 = MessagesController.getInstance(n).getUser(intValue);
                            }
                            if (intValue == messageOwner.from_id) {
                                if (messageOwner.to_id.channel_id != 0 && !this.isMegagroup()) {
                                    this.messageText = LocaleController.getString("ChannelJoined", 2131558956);
                                }
                                else if (messageOwner.to_id.channel_id != 0 && this.isMegagroup()) {
                                    if (intValue == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                        this.messageText = LocaleController.getString("ChannelMegaJoined", 2131558961);
                                    }
                                    else {
                                        this.messageText = this.replaceWithLink(LocaleController.getString("ActionAddUserSelfMega", 2131558493), "un1", user2);
                                    }
                                }
                                else if (this.isOut()) {
                                    this.messageText = LocaleController.getString("ActionAddUserSelfYou", 2131558494);
                                }
                                else {
                                    this.messageText = this.replaceWithLink(LocaleController.getString("ActionAddUserSelf", 2131558492), "un1", user2);
                                }
                            }
                            else if (this.isOut()) {
                                this.messageText = this.replaceWithLink(LocaleController.getString("ActionYouAddUser", 2131558546), "un2", user6);
                            }
                            else if (intValue == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                if (messageOwner.to_id.channel_id != 0) {
                                    if (this.isMegagroup()) {
                                        this.messageText = this.replaceWithLink(LocaleController.getString("MegaAddedBy", 2131559825), "un1", user2);
                                    }
                                    else {
                                        this.messageText = this.replaceWithLink(LocaleController.getString("ChannelAddedBy", 2131558924), "un1", user2);
                                    }
                                }
                                else {
                                    this.messageText = this.replaceWithLink(LocaleController.getString("ActionAddUserYou", 2131558495), "un1", user2);
                                }
                            }
                            else {
                                this.messageText = this.replaceWithLink(LocaleController.getString("ActionAddUser", 2131558491), "un2", user6);
                                this.messageText = this.replaceWithLink(this.messageText, "un1", user2);
                            }
                        }
                        else if (this.isOut()) {
                            this.messageText = this.replaceWithLink(LocaleController.getString("ActionYouAddUser", 2131558546), "un2", messageOwner.action.users, abstractMap, sparseArray);
                        }
                        else {
                            this.messageText = this.replaceWithLink(LocaleController.getString("ActionAddUser", 2131558491), "un2", messageOwner.action.users, abstractMap, sparseArray);
                            this.messageText = this.replaceWithLink(this.messageText, "un1", user2);
                        }
                    }
                    else {
                        final boolean b = layoutCreated;
                        if (action instanceof TLRPC.TL_messageActionChatJoinedByLink) {
                            if (this.isOut()) {
                                this.messageText = LocaleController.getString("ActionInviteYou", 2131558520);
                                layoutCreated = b;
                                user7 = user2;
                                break Label_4658;
                            }
                            this.messageText = this.replaceWithLink(LocaleController.getString("ActionInviteUser", 2131558519), "un1", user2);
                            layoutCreated = b;
                            user7 = user2;
                            break Label_4658;
                        }
                        else if (action instanceof TLRPC.TL_messageActionChatEditPhoto) {
                            if (messageOwner.to_id.channel_id != 0 && !this.isMegagroup()) {
                                this.messageText = LocaleController.getString("ActionChannelChangedPhoto", 2131558513);
                                layoutCreated = b;
                                user7 = user2;
                                break Label_4658;
                            }
                            if (this.isOut()) {
                                this.messageText = LocaleController.getString("ActionYouChangedPhoto", 2131558547);
                                layoutCreated = b;
                                user7 = user2;
                                break Label_4658;
                            }
                            this.messageText = this.replaceWithLink(LocaleController.getString("ActionChangedPhoto", 2131558511), "un1", user2);
                            layoutCreated = b;
                            user7 = user2;
                            break Label_4658;
                        }
                        else if (action instanceof TLRPC.TL_messageActionChatEditTitle) {
                            if (messageOwner.to_id.channel_id != 0 && !this.isMegagroup()) {
                                this.messageText = LocaleController.getString("ActionChannelChangedTitle", 2131558514).replace("un2", messageOwner.action.title);
                                layoutCreated = b;
                                user7 = user2;
                                break Label_4658;
                            }
                            if (this.isOut()) {
                                this.messageText = LocaleController.getString("ActionYouChangedTitle", 2131558548).replace("un2", messageOwner.action.title);
                                layoutCreated = b;
                                user7 = user2;
                                break Label_4658;
                            }
                            this.messageText = this.replaceWithLink(LocaleController.getString("ActionChangedTitle", 2131558512).replace("un2", messageOwner.action.title), "un1", user2);
                            layoutCreated = b;
                            user7 = user2;
                            break Label_4658;
                        }
                        else if (action instanceof TLRPC.TL_messageActionChatDeletePhoto) {
                            if (messageOwner.to_id.channel_id != 0 && !this.isMegagroup()) {
                                this.messageText = LocaleController.getString("ActionChannelRemovedPhoto", 2131558515);
                                layoutCreated = b;
                                user7 = user2;
                                break Label_4658;
                            }
                            if (this.isOut()) {
                                this.messageText = LocaleController.getString("ActionYouRemovedPhoto", 2131558552);
                                layoutCreated = b;
                                user7 = user2;
                                break Label_4658;
                            }
                            this.messageText = this.replaceWithLink(LocaleController.getString("ActionRemovedPhoto", 2131558541), "un1", user2);
                            layoutCreated = b;
                            user7 = user2;
                            break Label_4658;
                        }
                        else if (action instanceof TLRPC.TL_messageActionTTLChange) {
                            if (action.ttl != 0) {
                                if (this.isOut()) {
                                    this.messageText = LocaleController.formatString("MessageLifetimeChangedOutgoing", 2131559848, LocaleController.formatTTLString(messageOwner.action.ttl));
                                    layoutCreated = b;
                                    user7 = user2;
                                    break Label_4658;
                                }
                                this.messageText = LocaleController.formatString("MessageLifetimeChanged", 2131559847, UserObject.getFirstName(user2), LocaleController.formatTTLString(messageOwner.action.ttl));
                                layoutCreated = b;
                                user7 = user2;
                                break Label_4658;
                            }
                            else {
                                if (this.isOut()) {
                                    this.messageText = LocaleController.getString("MessageLifetimeYouRemoved", 2131559852);
                                    layoutCreated = b;
                                    user7 = user2;
                                    break Label_4658;
                                }
                                this.messageText = LocaleController.formatString("MessageLifetimeRemoved", 2131559850, UserObject.getFirstName(user2));
                                layoutCreated = b;
                                user7 = user2;
                                break Label_4658;
                            }
                        }
                        else {
                            if (action instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
                                eventId = messageOwner.date * 1000L;
                                String s;
                                if (LocaleController.getInstance().formatterDay != null && LocaleController.getInstance().formatterYear != null) {
                                    s = LocaleController.formatString("formatDateAtTime", 2131561210, LocaleController.getInstance().formatterYear.format(eventId), LocaleController.getInstance().formatterDay.format(eventId));
                                }
                                else {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("");
                                    sb.append(messageOwner.date);
                                    s = sb.toString();
                                }
                                TLRPC.User user9;
                                final TLRPC.User user8 = user9 = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                                if (user8 == null) {
                                    TLRPC.User user10;
                                    if (abstractMap != null) {
                                        user10 = abstractMap.get(this.messageOwner.to_id.user_id);
                                    }
                                    else {
                                        user10 = user8;
                                        if (sparseArray != null) {
                                            user10 = (TLRPC.User)sparseArray.get(this.messageOwner.to_id.user_id);
                                        }
                                    }
                                    if ((user9 = user10) == null) {
                                        user9 = MessagesController.getInstance(n).getUser(this.messageOwner.to_id.user_id);
                                    }
                                }
                                String firstName;
                                if (user9 != null) {
                                    firstName = UserObject.getFirstName(user9);
                                }
                                else {
                                    firstName = "";
                                }
                                final TLRPC.MessageAction action3 = messageOwner.action;
                                this.messageText = LocaleController.formatString("NotificationUnrecognizedDevice", 2131560054, firstName, s, action3.title, action3.address);
                                layoutCreated = b;
                                user7 = user2;
                                break Label_4658;
                            }
                            if (action instanceof TLRPC.TL_messageActionUserJoined || action instanceof TLRPC.TL_messageActionContactSignUp) {
                                this.messageText = LocaleController.formatString("NotificationContactJoined", 2131559997, UserObject.getUserName(user2));
                                layoutCreated = b;
                                user7 = user2;
                                break Label_4658;
                            }
                            if (action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                                this.messageText = LocaleController.formatString("NotificationContactNewPhoto", 2131559998, UserObject.getUserName(user2));
                                layoutCreated = b;
                                user7 = user2;
                                break Label_4658;
                            }
                            if (action instanceof TLRPC.TL_messageEncryptedAction) {
                                final TLRPC.DecryptedMessageAction encryptedAction = action.encryptedAction;
                                if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) {
                                    if (this.isOut()) {
                                        this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", 2131558543, new Object[0]);
                                        layoutCreated = b;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                    this.messageText = this.replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", 2131558542), "un1", user2);
                                    layoutCreated = b;
                                    user7 = user2;
                                    break Label_4658;
                                }
                                else {
                                    layoutCreated = b;
                                    user7 = user2;
                                    if (!(encryptedAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL)) {
                                        break Label_4658;
                                    }
                                    final TLRPC.TL_decryptedMessageActionSetMessageTTL tl_decryptedMessageActionSetMessageTTL = (TLRPC.TL_decryptedMessageActionSetMessageTTL)encryptedAction;
                                    if (tl_decryptedMessageActionSetMessageTTL.ttl_seconds != 0) {
                                        if (this.isOut()) {
                                            this.messageText = LocaleController.formatString("MessageLifetimeChangedOutgoing", 2131559848, LocaleController.formatTTLString(tl_decryptedMessageActionSetMessageTTL.ttl_seconds));
                                            layoutCreated = b;
                                            user7 = user2;
                                            break Label_4658;
                                        }
                                        this.messageText = LocaleController.formatString("MessageLifetimeChanged", 2131559847, UserObject.getFirstName(user2), LocaleController.formatTTLString(tl_decryptedMessageActionSetMessageTTL.ttl_seconds));
                                        layoutCreated = b;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                    else {
                                        if (this.isOut()) {
                                            this.messageText = LocaleController.getString("MessageLifetimeYouRemoved", 2131559852);
                                            layoutCreated = b;
                                            user7 = user2;
                                            break Label_4658;
                                        }
                                        this.messageText = LocaleController.formatString("MessageLifetimeRemoved", 2131559850, UserObject.getFirstName(user2));
                                        layoutCreated = b;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                }
                            }
                            else if (action instanceof TLRPC.TL_messageActionScreenshotTaken) {
                                if (this.isOut()) {
                                    this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", 2131558543, new Object[0]);
                                    layoutCreated = b;
                                    user7 = user2;
                                    break Label_4658;
                                }
                                this.messageText = this.replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", 2131558542), "un1", user2);
                                layoutCreated = b;
                                user7 = user2;
                                break Label_4658;
                            }
                            else {
                                if (action instanceof TLRPC.TL_messageActionCreatedBroadcastList) {
                                    this.messageText = LocaleController.formatString("YouCreatedBroadcastList", 2131561138, new Object[0]);
                                    layoutCreated = b;
                                    user7 = user2;
                                    break Label_4658;
                                }
                                if (action instanceof TLRPC.TL_messageActionChannelCreate) {
                                    if (this.isMegagroup()) {
                                        this.messageText = LocaleController.getString("ActionCreateMega", 2131558518);
                                        layoutCreated = b;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                    this.messageText = LocaleController.getString("ActionCreateChannel", 2131558516);
                                    layoutCreated = b;
                                    user7 = user2;
                                    break Label_4658;
                                }
                                else {
                                    if (action instanceof TLRPC.TL_messageActionChatMigrateTo) {
                                        this.messageText = LocaleController.getString("ActionMigrateFromGroup", 2131558524);
                                        layoutCreated = b;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                    if (action instanceof TLRPC.TL_messageActionChannelMigrateFrom) {
                                        this.messageText = LocaleController.getString("ActionMigrateFromGroup", 2131558524);
                                        layoutCreated = b;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                    if (action instanceof TLRPC.TL_messageActionPinMessage) {
                                        TLRPC.Chat chat = null;
                                        Label_2653: {
                                            if (user2 == null) {
                                                if (abstractMap2 != null) {
                                                    chat = abstractMap2.get(messageOwner.to_id.channel_id);
                                                    break Label_2653;
                                                }
                                                if (sparseArray2 != null) {
                                                    chat = (TLRPC.Chat)sparseArray2.get(messageOwner.to_id.channel_id);
                                                    break Label_2653;
                                                }
                                            }
                                            chat = null;
                                        }
                                        this.generatePinMessageText(user2, chat);
                                        layoutCreated = b;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                    if (action instanceof TLRPC.TL_messageActionHistoryClear) {
                                        this.messageText = LocaleController.getString("HistoryCleared", 2131559639);
                                        layoutCreated = b;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                    if (action instanceof TLRPC.TL_messageActionGameScore) {
                                        this.generateGameMessageText(user2);
                                        layoutCreated = b;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                    if (action instanceof TLRPC.TL_messageActionPhoneCall) {
                                        final TLRPC.Message messageOwner2 = this.messageOwner;
                                        final TLRPC.TL_messageActionPhoneCall tl_messageActionPhoneCall = (TLRPC.TL_messageActionPhoneCall)messageOwner2.action;
                                        final boolean b2 = tl_messageActionPhoneCall.reason instanceof TLRPC.TL_phoneCallDiscardReasonMissed;
                                        if (messageOwner2.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                            if (b2) {
                                                this.messageText = LocaleController.getString("CallMessageOutgoingMissed", 2131558877);
                                            }
                                            else {
                                                this.messageText = LocaleController.getString("CallMessageOutgoing", 2131558876);
                                            }
                                        }
                                        else if (b2) {
                                            this.messageText = LocaleController.getString("CallMessageIncomingMissed", 2131558875);
                                        }
                                        else if (tl_messageActionPhoneCall.reason instanceof TLRPC.TL_phoneCallDiscardReasonBusy) {
                                            this.messageText = LocaleController.getString("CallMessageIncomingDeclined", 2131558874);
                                        }
                                        else {
                                            this.messageText = LocaleController.getString("CallMessageIncoming", 2131558873);
                                        }
                                        n = tl_messageActionPhoneCall.duration;
                                        layoutCreated = b;
                                        user7 = user2;
                                        if (n <= 0) {
                                            break Label_4658;
                                        }
                                        final String formatCallDuration = LocaleController.formatCallDuration(n);
                                        this.messageText = LocaleController.formatString("CallMessageWithDuration", 2131558879, this.messageText, formatCallDuration);
                                        final String string = this.messageText.toString();
                                        final int index = string.indexOf(formatCallDuration);
                                        layoutCreated = b;
                                        user7 = user2;
                                        if (index != -1) {
                                            final SpannableString messageText = new SpannableString(this.messageText);
                                            final int index2 = formatCallDuration.length() + index;
                                            if ((n = index) > 0) {
                                                n = index;
                                                if (string.charAt(index - 1) == '(') {
                                                    n = index - 1;
                                                }
                                            }
                                            int n2;
                                            if ((n2 = index2) < string.length()) {
                                                n2 = index2;
                                                if (string.charAt(index2) == ')') {
                                                    n2 = index2 + 1;
                                                }
                                            }
                                            messageText.setSpan((Object)new TypefaceSpan(Typeface.DEFAULT), n, n2, 0);
                                            this.messageText = (CharSequence)messageText;
                                            layoutCreated = b;
                                            user7 = user2;
                                        }
                                        break Label_4658;
                                    }
                                    else {
                                        if (action instanceof TLRPC.TL_messageActionPaymentSent) {
                                            final int n3 = (int)this.getDialogId();
                                            if (abstractMap != null) {
                                                user2 = abstractMap.get(n3);
                                            }
                                            else if (sparseArray != null) {
                                                user2 = (TLRPC.User)sparseArray.get(n3);
                                            }
                                            TLRPC.User user11;
                                            if ((user11 = user2) == null) {
                                                user11 = MessagesController.getInstance(n).getUser(n3);
                                            }
                                            this.generatePaymentSentMessageText(null);
                                            layoutCreated = b;
                                            user7 = user11;
                                            break Label_4658;
                                        }
                                        if (action instanceof TLRPC.TL_messageActionBotAllowed) {
                                            final String domain = ((TLRPC.TL_messageActionBotAllowed)action).domain;
                                            final String string2 = LocaleController.getString("ActionBotAllowed", 2131558496);
                                            n = string2.indexOf("%1$s");
                                            final SpannableString messageText2 = new SpannableString((CharSequence)String.format(string2, domain));
                                            if (n >= 0) {
                                                final StringBuilder sb2 = new StringBuilder();
                                                sb2.append("http://");
                                                sb2.append(domain);
                                                messageText2.setSpan((Object)new URLSpanNoUnderlineBold(sb2.toString()), n, domain.length() + n, 33);
                                            }
                                            this.messageText = (CharSequence)messageText2;
                                            layoutCreated = b;
                                            user7 = user2;
                                            break Label_4658;
                                        }
                                        layoutCreated = b;
                                        user7 = user2;
                                        if (action instanceof TLRPC.TL_messageActionSecureValuesSent) {
                                            final TLRPC.TL_messageActionSecureValuesSent tl_messageActionSecureValuesSent = (TLRPC.TL_messageActionSecureValuesSent)action;
                                            final StringBuilder sb3 = new StringBuilder();
                                            for (int size = tl_messageActionSecureValuesSent.types.size(), i = 0; i < size; ++i) {
                                                final TLRPC.SecureValueType secureValueType = tl_messageActionSecureValuesSent.types.get(i);
                                                if (sb3.length() > 0) {
                                                    sb3.append(", ");
                                                }
                                                if (secureValueType instanceof TLRPC.TL_secureValueTypePhone) {
                                                    sb3.append(LocaleController.getString("ActionBotDocumentPhone", 2131558506));
                                                }
                                                else if (secureValueType instanceof TLRPC.TL_secureValueTypeEmail) {
                                                    sb3.append(LocaleController.getString("ActionBotDocumentEmail", 2131558500));
                                                }
                                                else if (secureValueType instanceof TLRPC.TL_secureValueTypeAddress) {
                                                    sb3.append(LocaleController.getString("ActionBotDocumentAddress", 2131558497));
                                                }
                                                else if (secureValueType instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                                                    sb3.append(LocaleController.getString("ActionBotDocumentIdentity", 2131558501));
                                                }
                                                else if (secureValueType instanceof TLRPC.TL_secureValueTypePassport) {
                                                    sb3.append(LocaleController.getString("ActionBotDocumentPassport", 2131558504));
                                                }
                                                else if (secureValueType instanceof TLRPC.TL_secureValueTypeDriverLicense) {
                                                    sb3.append(LocaleController.getString("ActionBotDocumentDriverLicence", 2131558499));
                                                }
                                                else if (secureValueType instanceof TLRPC.TL_secureValueTypeIdentityCard) {
                                                    sb3.append(LocaleController.getString("ActionBotDocumentIdentityCard", 2131558502));
                                                }
                                                else if (secureValueType instanceof TLRPC.TL_secureValueTypeUtilityBill) {
                                                    sb3.append(LocaleController.getString("ActionBotDocumentUtilityBill", 2131558509));
                                                }
                                                else if (secureValueType instanceof TLRPC.TL_secureValueTypeBankStatement) {
                                                    sb3.append(LocaleController.getString("ActionBotDocumentBankStatement", 2131558498));
                                                }
                                                else if (secureValueType instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
                                                    sb3.append(LocaleController.getString("ActionBotDocumentRentalAgreement", 2131558507));
                                                }
                                                else if (secureValueType instanceof TLRPC.TL_secureValueTypeInternalPassport) {
                                                    sb3.append(LocaleController.getString("ActionBotDocumentInternalPassport", 2131558503));
                                                }
                                                else if (secureValueType instanceof TLRPC.TL_secureValueTypePassportRegistration) {
                                                    sb3.append(LocaleController.getString("ActionBotDocumentPassportRegistration", 2131558505));
                                                }
                                                else if (secureValueType instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
                                                    sb3.append(LocaleController.getString("ActionBotDocumentTemporaryRegistration", 2131558508));
                                                }
                                            }
                                            final TLRPC.Peer to_id = messageOwner.to_id;
                                            TLRPC.User user13;
                                            if (to_id != null) {
                                                TLRPC.User user12;
                                                if (abstractMap != null) {
                                                    user12 = abstractMap.get(to_id.user_id);
                                                }
                                                else if (sparseArray != null) {
                                                    user12 = (TLRPC.User)sparseArray.get(to_id.user_id);
                                                }
                                                else {
                                                    user12 = null;
                                                }
                                                if (user12 == null) {
                                                    user13 = MessagesController.getInstance(n).getUser(messageOwner.to_id.user_id);
                                                }
                                                else {
                                                    user13 = user12;
                                                }
                                            }
                                            else {
                                                user13 = null;
                                            }
                                            this.messageText = LocaleController.formatString("ActionBotDocuments", 2131558510, UserObject.getFirstName(user13), sb3.toString());
                                            layoutCreated = b;
                                            user7 = user2;
                                        }
                                        break Label_4658;
                                    }
                                }
                            }
                        }
                    }
                }
                user7 = user2;
            }
            else {
                final boolean b3 = layoutCreated;
                if (!this.isMediaEmpty()) {
                    final TLRPC.MessageMedia media = messageOwner.media;
                    if (media instanceof TLRPC.TL_messageMediaPoll) {
                        this.messageText = LocaleController.getString("Poll", 2131560467);
                        layoutCreated = b3;
                        user7 = user2;
                    }
                    else if (media instanceof TLRPC.TL_messageMediaPhoto) {
                        if (media.ttl_seconds != 0 && !(messageOwner instanceof TLRPC.TL_message_secret)) {
                            this.messageText = LocaleController.getString("AttachDestructingPhoto", 2131558712);
                            layoutCreated = b3;
                            user7 = user2;
                        }
                        else {
                            this.messageText = LocaleController.getString("AttachPhoto", 2131558727);
                            layoutCreated = b3;
                            user7 = user2;
                        }
                    }
                    else {
                        if (!this.isVideo()) {
                            final TLRPC.MessageMedia media2 = messageOwner.media;
                            if (!(media2 instanceof TLRPC.TL_messageMediaDocument) || !(media2.document instanceof TLRPC.TL_documentEmpty) || media2.ttl_seconds == 0) {
                                if (this.isVoice()) {
                                    this.messageText = LocaleController.getString("AttachAudio", 2131558709);
                                    layoutCreated = b3;
                                    user7 = user2;
                                    break Label_4658;
                                }
                                if (this.isRoundVideo()) {
                                    this.messageText = LocaleController.getString("AttachRound", 2131558729);
                                    layoutCreated = b3;
                                    user7 = user2;
                                    break Label_4658;
                                }
                                final TLRPC.MessageMedia media3 = messageOwner.media;
                                if (media3 instanceof TLRPC.TL_messageMediaGeo || media3 instanceof TLRPC.TL_messageMediaVenue) {
                                    this.messageText = LocaleController.getString("AttachLocation", 2131558723);
                                    layoutCreated = b3;
                                    user7 = user2;
                                    break Label_4658;
                                }
                                if (media3 instanceof TLRPC.TL_messageMediaGeoLive) {
                                    this.messageText = LocaleController.getString("AttachLiveLocation", 2131558721);
                                    layoutCreated = b3;
                                    user7 = user2;
                                    break Label_4658;
                                }
                                if (media3 instanceof TLRPC.TL_messageMediaContact) {
                                    this.messageText = LocaleController.getString("AttachContact", 2131558711);
                                    layoutCreated = b3;
                                    user7 = user2;
                                    if (!TextUtils.isEmpty((CharSequence)messageOwner.media.vcard)) {
                                        this.vCardData = VCardData.parse(messageOwner.media.vcard);
                                        layoutCreated = b3;
                                        user7 = user2;
                                    }
                                    break Label_4658;
                                }
                                else {
                                    if (media3 instanceof TLRPC.TL_messageMediaGame) {
                                        this.messageText = messageOwner.message;
                                        layoutCreated = b3;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                    if (media3 instanceof TLRPC.TL_messageMediaInvoice) {
                                        this.messageText = media3.description;
                                        layoutCreated = b3;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                    if (media3 instanceof TLRPC.TL_messageMediaUnsupported) {
                                        this.messageText = LocaleController.getString("UnsupportedMedia", 2131560947).replace("https://telegram.org/update", "https://github.com/Telegram-FOSS-Team/Telegram-FOSS/blob/master/Update.md");
                                        layoutCreated = b3;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                    layoutCreated = b3;
                                    user7 = user2;
                                    if (!(media3 instanceof TLRPC.TL_messageMediaDocument)) {
                                        break Label_4658;
                                    }
                                    if (!this.isSticker() && !this.isAnimatedSticker()) {
                                        if (this.isMusic()) {
                                            this.messageText = LocaleController.getString("AttachMusic", 2131558726);
                                            layoutCreated = b3;
                                            user7 = user2;
                                            break Label_4658;
                                        }
                                        if (this.isGif()) {
                                            this.messageText = LocaleController.getString("AttachGif", 2131558716);
                                            layoutCreated = b3;
                                            user7 = user2;
                                            break Label_4658;
                                        }
                                        final String documentFileName = FileLoader.getDocumentFileName(messageOwner.media.document);
                                        if (documentFileName != null && documentFileName.length() > 0) {
                                            this.messageText = documentFileName;
                                            layoutCreated = b3;
                                            user7 = user2;
                                            break Label_4658;
                                        }
                                        this.messageText = LocaleController.getString("AttachDocument", 2131558714);
                                        layoutCreated = b3;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                    else {
                                        final String strickerChar = this.getStrickerChar();
                                        if (strickerChar != null && strickerChar.length() > 0) {
                                            this.messageText = String.format("%s %s", strickerChar, LocaleController.getString("AttachSticker", 2131558730));
                                            layoutCreated = b3;
                                            user7 = user2;
                                            break Label_4658;
                                        }
                                        this.messageText = LocaleController.getString("AttachSticker", 2131558730);
                                        layoutCreated = b3;
                                        user7 = user2;
                                        break Label_4658;
                                    }
                                }
                            }
                        }
                        if (messageOwner.media.ttl_seconds != 0 && !(messageOwner instanceof TLRPC.TL_message_secret)) {
                            this.messageText = LocaleController.getString("AttachDestructingVideo", 2131558713);
                            layoutCreated = b3;
                            user7 = user2;
                        }
                        else {
                            this.messageText = LocaleController.getString("AttachVideo", 2131558733);
                            layoutCreated = b3;
                            user7 = user2;
                        }
                    }
                }
                else {
                    this.messageText = messageOwner.message;
                    user7 = user2;
                    layoutCreated = b3;
                }
            }
        }
        if (this.messageText == null) {
            this.messageText = "";
        }
        this.setType();
        this.measureInlineBotButtons();
        final GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(this.messageOwner.date * 1000L);
        n = gregorianCalendar.get(6);
        final int value = gregorianCalendar.get(1);
        final int value2 = gregorianCalendar.get(2);
        this.dateKey = String.format("%d_%02d_%02d", value, value2, n);
        this.monthKey = String.format("%d_%02d", value, value2);
        this.createMessageSendInfo();
        this.generateCaption();
        if (layoutCreated) {
            TextPaint textPaint;
            if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                textPaint = Theme.chat_msgGameTextPaint;
            }
            else {
                textPaint = Theme.chat_msgTextPaint;
            }
            int[] array;
            if (SharedConfig.allowBigEmoji) {
                array = new int[] { 0 };
            }
            else {
                array = null;
            }
            this.messageText = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, array);
            this.checkEmojiOnly(array);
            this.generateLayout(user7);
        }
        this.layoutCreated = layoutCreated;
        this.generateThumbs(false);
        this.checkMediaExistance();
    }
    
    public MessageObject(final int n, final TLRPC.Message message, final AbstractMap<Integer, TLRPC.User> abstractMap, final AbstractMap<Integer, TLRPC.Chat> abstractMap2, final boolean b) {
        this(n, message, abstractMap, abstractMap2, b, 0L);
    }
    
    public MessageObject(final int n, final TLRPC.Message message, final AbstractMap<Integer, TLRPC.User> abstractMap, final AbstractMap<Integer, TLRPC.Chat> abstractMap2, final boolean b, final long n2) {
        this(n, message, abstractMap, abstractMap2, null, null, b, n2);
    }
    
    public MessageObject(final int n, final TLRPC.Message message, final AbstractMap<Integer, TLRPC.User> abstractMap, final boolean b) {
        this(n, message, abstractMap, null, b);
    }
    
    public MessageObject(final int n, final TLRPC.Message message, final boolean b) {
        this(n, message, null, null, null, null, b, 0L);
    }
    
    public MessageObject(final int n, final TLRPC.TL_channelAdminLogEvent currentEvent, final ArrayList<MessageObject> list, final HashMap<String, ArrayList<MessageObject>> hashMap, final TLRPC.Chat chat, final int[] array) {
        this.type = 1000;
        TLRPC.User user;
        if (currentEvent.user_id > 0) {
            user = MessagesController.getInstance(n).getUser(currentEvent.user_id);
        }
        else {
            user = null;
        }
        this.currentEvent = currentEvent;
        final GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(currentEvent.date * 1000L);
        final int value = gregorianCalendar.get(6);
        final int value2 = gregorianCalendar.get(1);
        final int value3 = gregorianCalendar.get(2);
        int n2 = 0;
        this.dateKey = String.format("%d_%02d_%02d", value2, value3, value);
        this.monthKey = String.format("%d_%02d", value2, value3);
        final TLRPC.TL_peerChannel to_id = new TLRPC.TL_peerChannel();
        to_id.channel_id = chat.id;
        final TLRPC.ChannelAdminLogEventAction action = currentEvent.action;
        TLRPC.Message message = null;
        Label_6396: {
            if (action instanceof TLRPC.TL_channelAdminLogEventActionChangeTitle) {
                final String new_value = ((TLRPC.TL_channelAdminLogEventActionChangeTitle)action).new_value;
                if (chat.megagroup) {
                    this.messageText = this.replaceWithLink(LocaleController.formatString("EventLogEditedGroupTitle", 2131559402, new_value), "un1", user);
                }
                else {
                    this.messageText = this.replaceWithLink(LocaleController.formatString("EventLogEditedChannelTitle", 2131559399, new_value), "un1", user);
                }
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionChangePhoto) {
                this.messageOwner = new TLRPC.TL_messageService();
                if (currentEvent.action.new_photo instanceof TLRPC.TL_photoEmpty) {
                    this.messageOwner.action = new TLRPC.TL_messageActionChatDeletePhoto();
                    if (chat.megagroup) {
                        this.messageText = this.replaceWithLink(LocaleController.getString("EventLogRemovedWGroupPhoto", 2131559448), "un1", user);
                    }
                    else {
                        this.messageText = this.replaceWithLink(LocaleController.getString("EventLogRemovedChannelPhoto", 2131559443), "un1", user);
                    }
                }
                else {
                    this.messageOwner.action = new TLRPC.TL_messageActionChatEditPhoto();
                    this.messageOwner.action.photo = currentEvent.action.new_photo;
                    if (chat.megagroup) {
                        this.messageText = this.replaceWithLink(LocaleController.getString("EventLogEditedGroupPhoto", 2131559401), "un1", user);
                    }
                    else {
                        this.messageText = this.replaceWithLink(LocaleController.getString("EventLogEditedChannelPhoto", 2131559398), "un1", user);
                    }
                }
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionParticipantJoin) {
                if (chat.megagroup) {
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogGroupJoined", 2131559420), "un1", user);
                }
                else {
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogChannelJoined", 2131559391), "un1", user);
                }
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionParticipantLeave) {
                this.messageOwner = new TLRPC.TL_messageService();
                this.messageOwner.action = new TLRPC.TL_messageActionChatDeleteUser();
                this.messageOwner.action.user_id = currentEvent.user_id;
                if (chat.megagroup) {
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogLeftGroup", 2131559425), "un1", user);
                }
                else {
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogLeftChannel", 2131559424), "un1", user);
                }
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionParticipantInvite) {
                this.messageOwner = new TLRPC.TL_messageService();
                this.messageOwner.action = new TLRPC.TL_messageActionChatAddUser();
                final TLRPC.User user2 = MessagesController.getInstance(n).getUser(currentEvent.action.participant.user_id);
                if (currentEvent.action.participant.user_id == this.messageOwner.from_id) {
                    if (chat.megagroup) {
                        this.messageText = this.replaceWithLink(LocaleController.getString("EventLogGroupJoined", 2131559420), "un1", user);
                    }
                    else {
                        this.messageText = this.replaceWithLink(LocaleController.getString("EventLogChannelJoined", 2131559391), "un1", user);
                    }
                }
                else {
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogAdded", 2131559383), "un2", user2);
                    this.messageText = this.replaceWithLink(this.messageText, "un1", user);
                }
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionParticipantToggleAdmin) {
                this.messageOwner = new TLRPC.TL_message();
                final TLRPC.User user3 = MessagesController.getInstance(n).getUser(currentEvent.action.prev_participant.user_id);
                final String string = LocaleController.getString("EventLogPromoted", 2131559432);
                final StringBuilder sb = new StringBuilder(String.format(string, this.getUserName(user3, this.messageOwner.entities, string.indexOf("%1$s"))));
                sb.append("\n");
                final TLRPC.ChannelAdminLogEventAction action2 = currentEvent.action;
                final TLRPC.TL_chatAdminRights admin_rights = action2.prev_participant.admin_rights;
                final TLRPC.TL_chatAdminRights admin_rights2 = action2.new_participant.admin_rights;
                TLRPC.TL_chatAdminRights tl_chatAdminRights;
                if ((tl_chatAdminRights = admin_rights) == null) {
                    tl_chatAdminRights = new TLRPC.TL_chatAdminRights();
                }
                TLRPC.TL_chatAdminRights tl_chatAdminRights2;
                if ((tl_chatAdminRights2 = admin_rights2) == null) {
                    tl_chatAdminRights2 = new TLRPC.TL_chatAdminRights();
                }
                if (tl_chatAdminRights.change_info != tl_chatAdminRights2.change_info) {
                    sb.append('\n');
                    char c;
                    if (tl_chatAdminRights2.change_info) {
                        c = '+';
                    }
                    else {
                        c = '-';
                    }
                    sb.append(c);
                    sb.append(' ');
                    int n3;
                    String s;
                    if (chat.megagroup) {
                        n3 = 2131559437;
                        s = "EventLogPromotedChangeGroupInfo";
                    }
                    else {
                        n3 = 2131559436;
                        s = "EventLogPromotedChangeChannelInfo";
                    }
                    sb.append(LocaleController.getString(s, n3));
                }
                if (!chat.megagroup) {
                    if (tl_chatAdminRights.post_messages != tl_chatAdminRights2.post_messages) {
                        sb.append('\n');
                        char c2;
                        if (tl_chatAdminRights2.post_messages) {
                            c2 = '+';
                        }
                        else {
                            c2 = '-';
                        }
                        sb.append(c2);
                        sb.append(' ');
                        sb.append(LocaleController.getString("EventLogPromotedPostMessages", 2131559441));
                    }
                    if (tl_chatAdminRights.edit_messages != tl_chatAdminRights2.edit_messages) {
                        sb.append('\n');
                        char c3;
                        if (tl_chatAdminRights2.edit_messages) {
                            c3 = '+';
                        }
                        else {
                            c3 = '-';
                        }
                        sb.append(c3);
                        sb.append(' ');
                        sb.append(LocaleController.getString("EventLogPromotedEditMessages", 2131559439));
                    }
                }
                if (tl_chatAdminRights.delete_messages != tl_chatAdminRights2.delete_messages) {
                    sb.append('\n');
                    char c4;
                    if (tl_chatAdminRights2.delete_messages) {
                        c4 = '+';
                    }
                    else {
                        c4 = '-';
                    }
                    sb.append(c4);
                    sb.append(' ');
                    sb.append(LocaleController.getString("EventLogPromotedDeleteMessages", 2131559438));
                }
                if (tl_chatAdminRights.add_admins != tl_chatAdminRights2.add_admins) {
                    sb.append('\n');
                    char c5;
                    if (tl_chatAdminRights2.add_admins) {
                        c5 = '+';
                    }
                    else {
                        c5 = '-';
                    }
                    sb.append(c5);
                    sb.append(' ');
                    sb.append(LocaleController.getString("EventLogPromotedAddAdmins", 2131559433));
                }
                if (chat.megagroup && tl_chatAdminRights.ban_users != tl_chatAdminRights2.ban_users) {
                    sb.append('\n');
                    char c6;
                    if (tl_chatAdminRights2.ban_users) {
                        c6 = '+';
                    }
                    else {
                        c6 = '-';
                    }
                    sb.append(c6);
                    sb.append(' ');
                    sb.append(LocaleController.getString("EventLogPromotedBanUsers", 2131559435));
                }
                if (tl_chatAdminRights.invite_users != tl_chatAdminRights2.invite_users) {
                    sb.append('\n');
                    char c7;
                    if (tl_chatAdminRights2.invite_users) {
                        c7 = '+';
                    }
                    else {
                        c7 = '-';
                    }
                    sb.append(c7);
                    sb.append(' ');
                    sb.append(LocaleController.getString("EventLogPromotedAddUsers", 2131559434));
                }
                if (chat.megagroup && tl_chatAdminRights.pin_messages != tl_chatAdminRights2.pin_messages) {
                    sb.append('\n');
                    char c8;
                    if (tl_chatAdminRights2.pin_messages) {
                        c8 = '+';
                    }
                    else {
                        c8 = '-';
                    }
                    sb.append(c8);
                    sb.append(' ');
                    sb.append(LocaleController.getString("EventLogPromotedPinMessages", 2131559440));
                }
                this.messageText = sb.toString();
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionDefaultBannedRights) {
                final TLRPC.TL_channelAdminLogEventActionDefaultBannedRights tl_channelAdminLogEventActionDefaultBannedRights = (TLRPC.TL_channelAdminLogEventActionDefaultBannedRights)action;
                this.messageOwner = new TLRPC.TL_message();
                final TLRPC.TL_chatBannedRights prev_banned_rights = tl_channelAdminLogEventActionDefaultBannedRights.prev_banned_rights;
                final TLRPC.TL_chatBannedRights new_banned_rights = tl_channelAdminLogEventActionDefaultBannedRights.new_banned_rights;
                final StringBuilder sb2 = new StringBuilder(LocaleController.getString("EventLogDefaultPermissions", 2131559394));
                TLRPC.TL_chatBannedRights tl_chatBannedRights;
                if ((tl_chatBannedRights = prev_banned_rights) == null) {
                    tl_chatBannedRights = new TLRPC.TL_chatBannedRights();
                }
                TLRPC.TL_chatBannedRights tl_chatBannedRights2;
                if ((tl_chatBannedRights2 = new_banned_rights) == null) {
                    tl_chatBannedRights2 = new TLRPC.TL_chatBannedRights();
                }
                int n4;
                if (tl_chatBannedRights.send_messages != tl_chatBannedRights2.send_messages) {
                    sb2.append('\n');
                    sb2.append('\n');
                    char c9;
                    if (!tl_chatBannedRights2.send_messages) {
                        c9 = '+';
                    }
                    else {
                        c9 = '-';
                    }
                    sb2.append(c9);
                    sb2.append(' ');
                    sb2.append(LocaleController.getString("EventLogRestrictedSendMessages", 2131559455));
                    n4 = 1;
                }
                else {
                    n4 = 0;
                }
                int n5 = 0;
                Label_2020: {
                    if (tl_chatBannedRights.send_stickers == tl_chatBannedRights2.send_stickers && tl_chatBannedRights.send_inline == tl_chatBannedRights2.send_inline && tl_chatBannedRights.send_gifs == tl_chatBannedRights2.send_gifs) {
                        n5 = n4;
                        if (tl_chatBannedRights.send_games == tl_chatBannedRights2.send_games) {
                            break Label_2020;
                        }
                    }
                    if ((n5 = n4) == 0) {
                        sb2.append('\n');
                        n5 = 1;
                    }
                    sb2.append('\n');
                    char c10;
                    if (!tl_chatBannedRights2.send_stickers) {
                        c10 = '+';
                    }
                    else {
                        c10 = '-';
                    }
                    sb2.append(c10);
                    sb2.append(' ');
                    sb2.append(LocaleController.getString("EventLogRestrictedSendStickers", 2131559457));
                }
                int n6 = n5;
                if (tl_chatBannedRights.send_media != tl_chatBannedRights2.send_media) {
                    n6 = n5;
                    if (n5 == 0) {
                        sb2.append('\n');
                        n6 = 1;
                    }
                    sb2.append('\n');
                    char c11;
                    if (!tl_chatBannedRights2.send_media) {
                        c11 = '+';
                    }
                    else {
                        c11 = '-';
                    }
                    sb2.append(c11);
                    sb2.append(' ');
                    sb2.append(LocaleController.getString("EventLogRestrictedSendMedia", 2131559454));
                }
                int n7 = n6;
                if (tl_chatBannedRights.send_polls != tl_chatBannedRights2.send_polls) {
                    n7 = n6;
                    if (n6 == 0) {
                        sb2.append('\n');
                        n7 = 1;
                    }
                    sb2.append('\n');
                    char c12;
                    if (!tl_chatBannedRights2.send_polls) {
                        c12 = '+';
                    }
                    else {
                        c12 = '-';
                    }
                    sb2.append(c12);
                    sb2.append(' ');
                    sb2.append(LocaleController.getString("EventLogRestrictedSendPolls", 2131559456));
                }
                int n8 = n7;
                if (tl_chatBannedRights.embed_links != tl_chatBannedRights2.embed_links) {
                    n8 = n7;
                    if (n7 == 0) {
                        sb2.append('\n');
                        n8 = 1;
                    }
                    sb2.append('\n');
                    char c13;
                    if (!tl_chatBannedRights2.embed_links) {
                        c13 = '+';
                    }
                    else {
                        c13 = '-';
                    }
                    sb2.append(c13);
                    sb2.append(' ');
                    sb2.append(LocaleController.getString("EventLogRestrictedSendEmbed", 2131559453));
                }
                int n9 = n8;
                if (tl_chatBannedRights.change_info != tl_chatBannedRights2.change_info) {
                    n9 = n8;
                    if (n8 == 0) {
                        sb2.append('\n');
                        n9 = 1;
                    }
                    sb2.append('\n');
                    char c14;
                    if (!tl_chatBannedRights2.change_info) {
                        c14 = '+';
                    }
                    else {
                        c14 = '-';
                    }
                    sb2.append(c14);
                    sb2.append(' ');
                    sb2.append(LocaleController.getString("EventLogRestrictedChangeInfo", 2131559449));
                }
                boolean b = n9 != 0;
                if (tl_chatBannedRights.invite_users != tl_chatBannedRights2.invite_users) {
                    b = (n9 != 0);
                    if (n9 == 0) {
                        sb2.append('\n');
                        b = true;
                    }
                    sb2.append('\n');
                    char c15;
                    if (!tl_chatBannedRights2.invite_users) {
                        c15 = '+';
                    }
                    else {
                        c15 = '-';
                    }
                    sb2.append(c15);
                    sb2.append(' ');
                    sb2.append(LocaleController.getString("EventLogRestrictedInviteUsers", 2131559450));
                }
                if (tl_chatBannedRights.pin_messages != tl_chatBannedRights2.pin_messages) {
                    if (!b) {
                        sb2.append('\n');
                    }
                    sb2.append('\n');
                    char c16;
                    if (!tl_chatBannedRights2.pin_messages) {
                        c16 = '+';
                    }
                    else {
                        c16 = '-';
                    }
                    sb2.append(c16);
                    sb2.append(' ');
                    sb2.append(LocaleController.getString("EventLogRestrictedPinMessages", 2131559451));
                }
                this.messageText = sb2.toString();
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionParticipantToggleBan) {
                this.messageOwner = new TLRPC.TL_message();
                final TLRPC.User user4 = MessagesController.getInstance(n).getUser(currentEvent.action.prev_participant.user_id);
                final TLRPC.ChannelAdminLogEventAction action3 = currentEvent.action;
                final TLRPC.TL_chatBannedRights banned_rights = action3.prev_participant.banned_rights;
                final TLRPC.TL_chatBannedRights banned_rights2 = action3.new_participant.banned_rights;
                if (chat.megagroup && (banned_rights2 == null || !banned_rights2.view_messages || (banned_rights2 != null && banned_rights != null && banned_rights2.until_date != banned_rights.until_date))) {
                    StringBuilder sb4;
                    if (banned_rights2 != null && !AndroidUtilities.isBannedForever(banned_rights2)) {
                        final StringBuilder sb3 = new StringBuilder();
                        final int n10 = banned_rights2.until_date - currentEvent.date;
                        final int n11 = n10 / 60 / 60 / 24;
                        final int n12 = n10 - n11 * 60 * 60 * 24;
                        final int n13 = n12 / 60 / 60;
                        final int n14 = (n12 - n13 * 60 * 60) / 60;
                        int n15 = 0;
                        while (true) {
                            sb4 = sb3;
                            if (n2 >= 3) {
                                break;
                            }
                            String str = null;
                            Label_2923: {
                                Label_2920: {
                                    if (n2 == 0) {
                                        if (n11 == 0) {
                                            break Label_2920;
                                        }
                                        str = LocaleController.formatPluralString("Days", n11);
                                    }
                                    else if (n2 == 1) {
                                        if (n13 == 0) {
                                            break Label_2920;
                                        }
                                        str = LocaleController.formatPluralString("Hours", n13);
                                    }
                                    else {
                                        if (n14 == 0) {
                                            break Label_2920;
                                        }
                                        str = LocaleController.formatPluralString("Minutes", n14);
                                    }
                                    ++n15;
                                    break Label_2923;
                                }
                                str = null;
                            }
                            if (str != null) {
                                if (sb3.length() > 0) {
                                    sb3.append(", ");
                                }
                                sb3.append(str);
                            }
                            if (n15 == 2) {
                                sb4 = sb3;
                                break;
                            }
                            ++n2;
                        }
                    }
                    else {
                        sb4 = new StringBuilder(LocaleController.getString("UserRestrictionsUntilForever", 2131561019));
                    }
                    final String string2 = LocaleController.getString("EventLogRestrictedUntil", 2131559458);
                    final StringBuilder sb5 = new StringBuilder(String.format(string2, this.getUserName(user4, this.messageOwner.entities, string2.indexOf("%1$s")), sb4.toString()));
                    TLRPC.TL_chatBannedRights tl_chatBannedRights3;
                    if ((tl_chatBannedRights3 = banned_rights) == null) {
                        tl_chatBannedRights3 = new TLRPC.TL_chatBannedRights();
                    }
                    TLRPC.TL_chatBannedRights tl_chatBannedRights4;
                    if ((tl_chatBannedRights4 = banned_rights2) == null) {
                        tl_chatBannedRights4 = new TLRPC.TL_chatBannedRights();
                    }
                    int n16;
                    if (tl_chatBannedRights3.view_messages != tl_chatBannedRights4.view_messages) {
                        sb5.append('\n');
                        sb5.append('\n');
                        char c17;
                        if (!tl_chatBannedRights4.view_messages) {
                            c17 = '+';
                        }
                        else {
                            c17 = '-';
                        }
                        sb5.append(c17);
                        sb5.append(' ');
                        sb5.append(LocaleController.getString("EventLogRestrictedReadMessages", 2131559452));
                        n16 = 1;
                    }
                    else {
                        n16 = 0;
                    }
                    int n17 = n16;
                    if (tl_chatBannedRights3.send_messages != tl_chatBannedRights4.send_messages) {
                        if (n16 == 0) {
                            sb5.append('\n');
                            n16 = 1;
                        }
                        sb5.append('\n');
                        char c18;
                        if (!tl_chatBannedRights4.send_messages) {
                            c18 = '+';
                        }
                        else {
                            c18 = '-';
                        }
                        sb5.append(c18);
                        sb5.append(' ');
                        sb5.append(LocaleController.getString("EventLogRestrictedSendMessages", 2131559455));
                        n17 = n16;
                    }
                    int n18 = 0;
                    Label_3444: {
                        if (tl_chatBannedRights3.send_stickers == tl_chatBannedRights4.send_stickers && tl_chatBannedRights3.send_inline == tl_chatBannedRights4.send_inline && tl_chatBannedRights3.send_gifs == tl_chatBannedRights4.send_gifs) {
                            n18 = n17;
                            if (tl_chatBannedRights3.send_games == tl_chatBannedRights4.send_games) {
                                break Label_3444;
                            }
                        }
                        if (n17 == 0) {
                            sb5.append('\n');
                            n18 = 1;
                        }
                        else {
                            n18 = n17;
                        }
                        sb5.append('\n');
                        char c19;
                        if (!tl_chatBannedRights4.send_stickers) {
                            c19 = '+';
                        }
                        else {
                            c19 = '-';
                        }
                        sb5.append(c19);
                        sb5.append(' ');
                        sb5.append(LocaleController.getString("EventLogRestrictedSendStickers", 2131559457));
                    }
                    int n19 = n18;
                    if (tl_chatBannedRights3.send_media != tl_chatBannedRights4.send_media) {
                        if (n18 == 0) {
                            sb5.append('\n');
                            n18 = 1;
                        }
                        sb5.append('\n');
                        char c20;
                        if (!tl_chatBannedRights4.send_media) {
                            c20 = '+';
                        }
                        else {
                            c20 = '-';
                        }
                        sb5.append(c20);
                        sb5.append(' ');
                        sb5.append(LocaleController.getString("EventLogRestrictedSendMedia", 2131559454));
                        n19 = n18;
                    }
                    int n20 = n19;
                    if (tl_chatBannedRights3.send_polls != tl_chatBannedRights4.send_polls) {
                        if (n19 == 0) {
                            sb5.append('\n');
                            n19 = 1;
                        }
                        sb5.append('\n');
                        char c21;
                        if (!tl_chatBannedRights4.send_polls) {
                            c21 = '+';
                        }
                        else {
                            c21 = '-';
                        }
                        sb5.append(c21);
                        sb5.append(' ');
                        sb5.append(LocaleController.getString("EventLogRestrictedSendPolls", 2131559456));
                        n20 = n19;
                    }
                    int n21 = n20;
                    if (tl_chatBannedRights3.embed_links != tl_chatBannedRights4.embed_links) {
                        if (n20 == 0) {
                            sb5.append('\n');
                            n21 = 1;
                        }
                        else {
                            n21 = n20;
                        }
                        sb5.append('\n');
                        char c22;
                        if (!tl_chatBannedRights4.embed_links) {
                            c22 = '+';
                        }
                        else {
                            c22 = '-';
                        }
                        sb5.append(c22);
                        sb5.append(' ');
                        sb5.append(LocaleController.getString("EventLogRestrictedSendEmbed", 2131559453));
                    }
                    int n22 = n21;
                    if (tl_chatBannedRights3.change_info != tl_chatBannedRights4.change_info) {
                        if (n21 == 0) {
                            sb5.append('\n');
                            n21 = 1;
                        }
                        sb5.append('\n');
                        char c23;
                        if (!tl_chatBannedRights4.change_info) {
                            c23 = '+';
                        }
                        else {
                            c23 = '-';
                        }
                        sb5.append(c23);
                        sb5.append(' ');
                        sb5.append(LocaleController.getString("EventLogRestrictedChangeInfo", 2131559449));
                        n22 = n21;
                    }
                    int n23 = n22;
                    if (tl_chatBannedRights3.invite_users != tl_chatBannedRights4.invite_users) {
                        if (n22 == 0) {
                            sb5.append('\n');
                            n22 = 1;
                        }
                        sb5.append('\n');
                        char c24;
                        if (!tl_chatBannedRights4.invite_users) {
                            c24 = '+';
                        }
                        else {
                            c24 = '-';
                        }
                        sb5.append(c24);
                        sb5.append(' ');
                        sb5.append(LocaleController.getString("EventLogRestrictedInviteUsers", 2131559450));
                        n23 = n22;
                    }
                    if (tl_chatBannedRights3.pin_messages != tl_chatBannedRights4.pin_messages) {
                        if (n23 == 0) {
                            sb5.append('\n');
                        }
                        sb5.append('\n');
                        char c25;
                        if (!tl_chatBannedRights4.pin_messages) {
                            c25 = '+';
                        }
                        else {
                            c25 = '-';
                        }
                        sb5.append(c25);
                        sb5.append(' ');
                        sb5.append(LocaleController.getString("EventLogRestrictedPinMessages", 2131559451));
                    }
                    this.messageText = sb5.toString();
                }
                else {
                    String format;
                    if (banned_rights2 != null && (banned_rights == null || banned_rights2.view_messages)) {
                        format = LocaleController.getString("EventLogChannelRestricted", 2131559392);
                    }
                    else {
                        format = LocaleController.getString("EventLogChannelUnrestricted", 2131559393);
                    }
                    this.messageText = String.format(format, this.getUserName(user4, this.messageOwner.entities, format.indexOf("%1$s")));
                }
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionUpdatePinned) {
                if (user != null && user.id == 136817688 && action.message.fwd_from != null) {
                    final TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(currentEvent.action.message.fwd_from.channel_id);
                    if (currentEvent.action.message instanceof TLRPC.TL_messageEmpty) {
                        this.messageText = this.replaceWithLink(LocaleController.getString("EventLogUnpinnedMessages", 2131559467), "un1", chat2);
                    }
                    else {
                        this.messageText = this.replaceWithLink(LocaleController.getString("EventLogPinnedMessages", 2131559429), "un1", chat2);
                    }
                }
                else if (currentEvent.action.message instanceof TLRPC.TL_messageEmpty) {
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogUnpinnedMessages", 2131559467), "un1", user);
                }
                else {
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogPinnedMessages", 2131559429), "un1", user);
                }
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionStopPoll) {
                this.messageText = this.replaceWithLink(LocaleController.getString("EventLogStopPoll", 2131559460), "un1", user);
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionToggleSignatures) {
                if (((TLRPC.TL_channelAdminLogEventActionToggleSignatures)action).new_value) {
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogToggledSignaturesOn", 2131559466), "un1", user);
                }
                else {
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogToggledSignaturesOff", 2131559465), "un1", user);
                }
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionToggleInvites) {
                if (((TLRPC.TL_channelAdminLogEventActionToggleInvites)action).new_value) {
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogToggledInvitesOn", 2131559464), "un1", user);
                }
                else {
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogToggledInvitesOff", 2131559463), "un1", user);
                }
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
                this.messageText = this.replaceWithLink(LocaleController.getString("EventLogDeletedMessages", 2131559395), "un1", user);
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionChangeLinkedChat) {
                final int new_value2 = ((TLRPC.TL_channelAdminLogEventActionChangeLinkedChat)action).new_value;
                final int prev_value = ((TLRPC.TL_channelAdminLogEventActionChangeLinkedChat)action).prev_value;
                if (chat.megagroup) {
                    if (new_value2 == 0) {
                        final TLRPC.Chat chat3 = MessagesController.getInstance(this.currentAccount).getChat(prev_value);
                        this.messageText = this.replaceWithLink(LocaleController.getString("EventLogRemovedLinkedChannel", 2131559445), "un1", user);
                        this.messageText = this.replaceWithLink(this.messageText, "un2", chat3);
                    }
                    else {
                        final TLRPC.Chat chat4 = MessagesController.getInstance(this.currentAccount).getChat(new_value2);
                        this.messageText = this.replaceWithLink(LocaleController.getString("EventLogChangedLinkedChannel", 2131559388), "un1", user);
                        this.messageText = this.replaceWithLink(this.messageText, "un2", chat4);
                    }
                }
                else if (new_value2 == 0) {
                    final TLRPC.Chat chat5 = MessagesController.getInstance(this.currentAccount).getChat(prev_value);
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogRemovedLinkedGroup", 2131559446), "un1", user);
                    this.messageText = this.replaceWithLink(this.messageText, "un2", chat5);
                }
                else {
                    final TLRPC.Chat chat6 = MessagesController.getInstance(this.currentAccount).getChat(new_value2);
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogChangedLinkedGroup", 2131559389), "un1", user);
                    this.messageText = this.replaceWithLink(this.messageText, "un2", chat6);
                }
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionTogglePreHistoryHidden) {
                if (((TLRPC.TL_channelAdminLogEventActionTogglePreHistoryHidden)action).new_value) {
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogToggledInvitesHistoryOff", 2131559461), "un1", user);
                }
                else {
                    this.messageText = this.replaceWithLink(LocaleController.getString("EventLogToggledInvitesHistoryOn", 2131559462), "un1", user);
                }
            }
            else if (action instanceof TLRPC.TL_channelAdminLogEventActionChangeAbout) {
                int n24;
                String s2;
                if (chat.megagroup) {
                    n24 = 2131559400;
                    s2 = "EventLogEditedGroupDescription";
                }
                else {
                    n24 = 2131559397;
                    s2 = "EventLogEditedChannelDescription";
                }
                this.messageText = this.replaceWithLink(LocaleController.getString(s2, n24), "un1", user);
                message = new TLRPC.TL_message();
                message.out = false;
                message.unread = false;
                message.from_id = currentEvent.user_id;
                message.to_id = to_id;
                message.date = currentEvent.date;
                final TLRPC.ChannelAdminLogEventAction action4 = currentEvent.action;
                message.message = ((TLRPC.TL_channelAdminLogEventActionChangeAbout)action4).new_value;
                if (!TextUtils.isEmpty((CharSequence)((TLRPC.TL_channelAdminLogEventActionChangeAbout)action4).prev_value)) {
                    message.media = new TLRPC.TL_messageMediaWebPage();
                    message.media.webpage = new TLRPC.TL_webPage();
                    final TLRPC.WebPage webpage = message.media.webpage;
                    webpage.flags = 10;
                    webpage.display_url = "";
                    webpage.url = "";
                    webpage.site_name = LocaleController.getString("EventLogPreviousGroupDescription", 2131559430);
                    message.media.webpage.description = ((TLRPC.TL_channelAdminLogEventActionChangeAbout)currentEvent.action).prev_value;
                    break Label_6396;
                }
                message.media = new TLRPC.TL_messageMediaEmpty();
                break Label_6396;
            }
            else {
                if (action instanceof TLRPC.TL_channelAdminLogEventActionChangeUsername) {
                    final String new_value3 = ((TLRPC.TL_channelAdminLogEventActionChangeUsername)action).new_value;
                    if (!TextUtils.isEmpty((CharSequence)new_value3)) {
                        int n25;
                        String s3;
                        if (chat.megagroup) {
                            n25 = 2131559387;
                            s3 = "EventLogChangedGroupLink";
                        }
                        else {
                            n25 = 2131559386;
                            s3 = "EventLogChangedChannelLink";
                        }
                        this.messageText = this.replaceWithLink(LocaleController.getString(s3, n25), "un1", user);
                    }
                    else {
                        int n26;
                        String s4;
                        if (chat.megagroup) {
                            n26 = 2131559444;
                            s4 = "EventLogRemovedGroupLink";
                        }
                        else {
                            n26 = 2131559442;
                            s4 = "EventLogRemovedChannelLink";
                        }
                        this.messageText = this.replaceWithLink(LocaleController.getString(s4, n26), "un1", user);
                    }
                    message = new TLRPC.TL_message();
                    message.out = false;
                    message.unread = false;
                    message.from_id = currentEvent.user_id;
                    message.to_id = to_id;
                    message.date = currentEvent.date;
                    if (!TextUtils.isEmpty((CharSequence)new_value3)) {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("https://");
                        sb6.append(MessagesController.getInstance(n).linkPrefix);
                        sb6.append("/");
                        sb6.append(new_value3);
                        message.message = sb6.toString();
                    }
                    else {
                        message.message = "";
                    }
                    final TLRPC.TL_messageEntityUrl e = new TLRPC.TL_messageEntityUrl();
                    e.offset = 0;
                    e.length = message.message.length();
                    message.entities.add(e);
                    if (!TextUtils.isEmpty((CharSequence)((TLRPC.TL_channelAdminLogEventActionChangeUsername)currentEvent.action).prev_value)) {
                        message.media = new TLRPC.TL_messageMediaWebPage();
                        message.media.webpage = new TLRPC.TL_webPage();
                        final TLRPC.WebPage webpage2 = message.media.webpage;
                        webpage2.flags = 10;
                        webpage2.display_url = "";
                        webpage2.url = "";
                        webpage2.site_name = LocaleController.getString("EventLogPreviousLink", 2131559431);
                        final TLRPC.WebPage webpage3 = message.media.webpage;
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("https://");
                        sb7.append(MessagesController.getInstance(n).linkPrefix);
                        sb7.append("/");
                        sb7.append(((TLRPC.TL_channelAdminLogEventActionChangeUsername)currentEvent.action).prev_value);
                        webpage3.description = sb7.toString();
                    }
                    else {
                        message.media = new TLRPC.TL_messageMediaEmpty();
                    }
                    break Label_6396;
                }
                if (action instanceof TLRPC.TL_channelAdminLogEventActionEditMessage) {
                    final TLRPC.TL_message tl_message = new TLRPC.TL_message();
                    tl_message.out = false;
                    tl_message.unread = false;
                    tl_message.from_id = currentEvent.user_id;
                    tl_message.to_id = to_id;
                    tl_message.date = currentEvent.date;
                    final TLRPC.ChannelAdminLogEventAction action5 = currentEvent.action;
                    final TLRPC.Message new_message = ((TLRPC.TL_channelAdminLogEventActionEditMessage)action5).new_message;
                    final TLRPC.Message prev_message = ((TLRPC.TL_channelAdminLogEventActionEditMessage)action5).prev_message;
                    final TLRPC.MessageMedia media = new_message.media;
                    if (media != null && !(media instanceof TLRPC.TL_messageMediaEmpty) && !(media instanceof TLRPC.TL_messageMediaWebPage)) {
                        final boolean b2 = TextUtils.equals((CharSequence)new_message.message, (CharSequence)prev_message.message) ^ true;
                        boolean b3 = false;
                        Label_5884: {
                            Label_5881: {
                                if (new_message.media.getClass() == prev_message.media.getClass()) {
                                    final TLRPC.Photo photo = new_message.media.photo;
                                    if (photo != null) {
                                        final TLRPC.Photo photo2 = prev_message.media.photo;
                                        if (photo2 != null && photo.id != photo2.id) {
                                            break Label_5881;
                                        }
                                    }
                                    final TLRPC.Document document = new_message.media.document;
                                    if (document != null) {
                                        final TLRPC.Document document2 = prev_message.media.document;
                                        if (document2 != null && document.id != document2.id) {
                                            break Label_5881;
                                        }
                                    }
                                    b3 = false;
                                    break Label_5884;
                                }
                            }
                            b3 = true;
                        }
                        if (b3 && b2) {
                            this.messageText = this.replaceWithLink(LocaleController.getString("EventLogEditedMediaCaption", 2131559404), "un1", user);
                        }
                        else if (b2) {
                            this.messageText = this.replaceWithLink(LocaleController.getString("EventLogEditedCaption", 2131559396), "un1", user);
                        }
                        else {
                            this.messageText = this.replaceWithLink(LocaleController.getString("EventLogEditedMedia", 2131559403), "un1", user);
                        }
                        tl_message.media = new_message.media;
                        if (b2) {
                            tl_message.media.webpage = new TLRPC.TL_webPage();
                            tl_message.media.webpage.site_name = LocaleController.getString("EventLogOriginalCaption", 2131559426);
                            if (TextUtils.isEmpty((CharSequence)prev_message.message)) {
                                tl_message.media.webpage.description = LocaleController.getString("EventLogOriginalCaptionEmpty", 2131559427);
                            }
                            else {
                                tl_message.media.webpage.description = prev_message.message;
                            }
                        }
                    }
                    else {
                        this.messageText = this.replaceWithLink(LocaleController.getString("EventLogEditedMessages", 2131559405), "un1", user);
                        tl_message.message = new_message.message;
                        tl_message.media = new TLRPC.TL_messageMediaWebPage();
                        tl_message.media.webpage = new TLRPC.TL_webPage();
                        tl_message.media.webpage.site_name = LocaleController.getString("EventLogOriginalMessages", 2131559428);
                        if (TextUtils.isEmpty((CharSequence)prev_message.message)) {
                            tl_message.media.webpage.description = LocaleController.getString("EventLogOriginalCaptionEmpty", 2131559427);
                        }
                        else {
                            tl_message.media.webpage.description = prev_message.message;
                        }
                    }
                    tl_message.reply_markup = new_message.reply_markup;
                    final TLRPC.WebPage webpage4 = tl_message.media.webpage;
                    message = tl_message;
                    if (webpage4 != null) {
                        webpage4.flags = 10;
                        webpage4.display_url = "";
                        webpage4.url = "";
                        message = tl_message;
                    }
                    break Label_6396;
                }
                else if (action instanceof TLRPC.TL_channelAdminLogEventActionChangeStickerSet) {
                    final TLRPC.InputStickerSet new_stickerset = ((TLRPC.TL_channelAdminLogEventActionChangeStickerSet)action).new_stickerset;
                    final TLRPC.InputStickerSet new_stickerset2 = ((TLRPC.TL_channelAdminLogEventActionChangeStickerSet)action).new_stickerset;
                    if (new_stickerset != null && !(new_stickerset instanceof TLRPC.TL_inputStickerSetEmpty)) {
                        this.messageText = this.replaceWithLink(LocaleController.getString("EventLogChangedStickersSet", 2131559390), "un1", user);
                    }
                    else {
                        this.messageText = this.replaceWithLink(LocaleController.getString("EventLogRemovedStickersSet", 2131559447), "un1", user);
                    }
                }
                else {
                    final StringBuilder sb8 = new StringBuilder();
                    sb8.append("unsupported ");
                    sb8.append(currentEvent.action);
                    this.messageText = sb8.toString();
                }
            }
            message = null;
        }
        if (this.messageOwner == null) {
            this.messageOwner = new TLRPC.TL_messageService();
        }
        this.messageOwner.message = this.messageText.toString();
        final TLRPC.Message messageOwner = this.messageOwner;
        messageOwner.from_id = currentEvent.user_id;
        messageOwner.date = currentEvent.date;
        final int id = array[0];
        array[0] = id + 1;
        messageOwner.id = id;
        this.eventId = currentEvent.id;
        messageOwner.out = false;
        messageOwner.to_id = new TLRPC.TL_peerChannel();
        final TLRPC.Message messageOwner2 = this.messageOwner;
        messageOwner2.to_id.channel_id = chat.id;
        messageOwner2.unread = false;
        if (chat.megagroup) {
            messageOwner2.flags |= Integer.MIN_VALUE;
        }
        final MediaController instance = MediaController.getInstance();
        final TLRPC.Message message2 = currentEvent.action.message;
        TLRPC.Message message3 = message;
        if (message2 != null) {
            message3 = message;
            if (!(message2 instanceof TLRPC.TL_messageEmpty)) {
                message3 = message2;
            }
        }
        if (message3 != null) {
            message3.out = false;
            final int id2 = array[0];
            array[0] = id2 + 1;
            message3.id = id2;
            message3.reply_to_msg_id = 0;
            message3.flags &= 0xFFFF7FFF;
            if (chat.megagroup) {
                message3.flags |= Integer.MIN_VALUE;
            }
            final MessageObject element = new MessageObject(n, message3, null, null, true, this.eventId);
            if (element.contentType >= 0) {
                if (instance.isPlayingMessage(element)) {
                    final MessageObject playingMessageObject = instance.getPlayingMessageObject();
                    element.audioProgress = playingMessageObject.audioProgress;
                    element.audioProgressSec = playingMessageObject.audioProgressSec;
                }
                this.createDateArray(n, currentEvent, list, hashMap);
                list.add(list.size() - 1, element);
            }
            else {
                this.contentType = -1;
            }
        }
        if (this.contentType >= 0) {
            this.createDateArray(n, currentEvent, list, hashMap);
            list.add(list.size() - 1, this);
            if (this.messageText == null) {
                this.messageText = "";
            }
            this.setType();
            this.measureInlineBotButtons();
            this.generateCaption();
            TextPaint textPaint;
            if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                textPaint = Theme.chat_msgGameTextPaint;
            }
            else {
                textPaint = Theme.chat_msgTextPaint;
            }
            int[] array2;
            if (SharedConfig.allowBigEmoji) {
                array2 = new int[] { 0 };
            }
            else {
                array2 = null;
            }
            this.messageText = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, array2);
            this.checkEmojiOnly(array2);
            if (instance.isPlayingMessage(this)) {
                final MessageObject playingMessageObject2 = instance.getPlayingMessageObject();
                this.audioProgress = playingMessageObject2.audioProgress;
                this.audioProgressSec = playingMessageObject2.audioProgressSec;
            }
            this.generateLayout(user);
            this.layoutCreated = true;
            this.generateThumbs(false);
            this.checkMediaExistance();
        }
    }
    
    public static boolean addEntitiesToText(final CharSequence charSequence, final ArrayList<TLRPC.MessageEntity> list, final boolean b, int i, final boolean b2, final boolean b3, final boolean b4) {
        if (!(charSequence instanceof Spannable)) {
            return false;
        }
        final Spannable spannable = (Spannable)charSequence;
        final int size = list.size();
        final URLSpan[] array = (URLSpan[])spannable.getSpans(0, charSequence.length(), (Class)URLSpan.class);
        final boolean b5 = array != null && array.length > 0;
        byte b6;
        if (b3) {
            i = (b6 = 2);
        }
        else if (b) {
            i = (b6 = 1);
        }
        else {
            i = (b6 = 0);
        }
        boolean b7 = b5;
        TLRPC.MessageEntity messageEntity;
        boolean b8;
        int offset;
        int j;
        int spanStart;
        int spanEnd;
        int offset2;
        int offset3;
        TypefaceSpan typefaceSpan;
        int offset4;
        TypefaceSpan typefaceSpan2;
        int offset5;
        StringBuilder sb;
        URLSpanUserMention urlSpanUserMention;
        int offset6;
        StringBuilder sb2;
        URLSpanUserMention urlSpanUserMention2;
        int offset7;
        int offset8;
        String substring;
        URLSpanBotCommand urlSpanBotCommand;
        int offset9;
        StringBuilder sb3;
        URLSpanReplacement urlSpanReplacement;
        int offset10;
        StringBuilder sb4;
        URLSpanBrowser urlSpanBrowser;
        int offset11;
        URLSpanBrowser urlSpanBrowser2;
        int offset12;
        String s;
        StringBuilder sb5;
        StringBuilder sb6;
        URLSpanBrowser urlSpanBrowser3;
        int offset13;
        URLSpanReplacement urlSpanReplacement2;
        int offset14;
        URLSpanNoUnderline urlSpanNoUnderline;
        int offset15;
        int offset16;
        URLSpanMono urlSpanMono;
        int offset17;
        for (i = 0; i < size; ++i, b7 = b8) {
            messageEntity = list.get(i);
            b8 = b7;
            if (messageEntity.length > 0) {
                offset = messageEntity.offset;
                b8 = b7;
                if (offset >= 0) {
                    if (offset >= charSequence.length()) {
                        b8 = b7;
                    }
                    else {
                        if (messageEntity.offset + messageEntity.length > charSequence.length()) {
                            messageEntity.length = charSequence.length() - messageEntity.offset;
                        }
                        if ((!b4 || messageEntity instanceof TLRPC.TL_messageEntityBold || messageEntity instanceof TLRPC.TL_messageEntityItalic || messageEntity instanceof TLRPC.TL_messageEntityCode || messageEntity instanceof TLRPC.TL_messageEntityPre || messageEntity instanceof TLRPC.TL_messageEntityMentionName || messageEntity instanceof TLRPC.TL_inputMessageEntityMentionName) && array != null && array.length > 0) {
                            for (j = 0; j < array.length; ++j) {
                                if (array[j] != null) {
                                    spanStart = spannable.getSpanStart((Object)array[j]);
                                    spanEnd = spannable.getSpanEnd((Object)array[j]);
                                    offset2 = messageEntity.offset;
                                    if (offset2 > spanStart || offset2 + messageEntity.length < spanStart) {
                                        offset3 = messageEntity.offset;
                                        if (offset3 > spanEnd || offset3 + messageEntity.length < spanEnd) {
                                            continue;
                                        }
                                    }
                                    spannable.removeSpan((Object)array[j]);
                                    array[j] = null;
                                }
                            }
                        }
                        if (messageEntity instanceof TLRPC.TL_messageEntityBold) {
                            typefaceSpan = new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                            offset4 = messageEntity.offset;
                            spannable.setSpan((Object)typefaceSpan, offset4, messageEntity.length + offset4, 33);
                            b8 = b7;
                        }
                        else if (messageEntity instanceof TLRPC.TL_messageEntityItalic) {
                            typefaceSpan2 = new TypefaceSpan(AndroidUtilities.getTypeface("fonts/ritalic.ttf"));
                            offset5 = messageEntity.offset;
                            spannable.setSpan((Object)typefaceSpan2, offset5, messageEntity.length + offset5, 33);
                            b8 = b7;
                        }
                        else if (!(messageEntity instanceof TLRPC.TL_messageEntityCode) && !(messageEntity instanceof TLRPC.TL_messageEntityPre)) {
                            if (messageEntity instanceof TLRPC.TL_messageEntityMentionName) {
                                b8 = b7;
                                if (b2) {
                                    sb = new StringBuilder();
                                    sb.append("");
                                    sb.append(((TLRPC.TL_messageEntityMentionName)messageEntity).user_id);
                                    urlSpanUserMention = new URLSpanUserMention(sb.toString(), b6);
                                    offset6 = messageEntity.offset;
                                    spannable.setSpan((Object)urlSpanUserMention, offset6, messageEntity.length + offset6, 33);
                                    b8 = b7;
                                }
                            }
                            else if (messageEntity instanceof TLRPC.TL_inputMessageEntityMentionName) {
                                b8 = b7;
                                if (b2) {
                                    sb2 = new StringBuilder();
                                    sb2.append("");
                                    sb2.append(((TLRPC.TL_inputMessageEntityMentionName)messageEntity).user_id.user_id);
                                    urlSpanUserMention2 = new URLSpanUserMention(sb2.toString(), b6);
                                    offset7 = messageEntity.offset;
                                    spannable.setSpan((Object)urlSpanUserMention2, offset7, messageEntity.length + offset7, 33);
                                    b8 = b7;
                                }
                            }
                            else {
                                b8 = b7;
                                if (!b4) {
                                    offset8 = messageEntity.offset;
                                    substring = TextUtils.substring(charSequence, offset8, messageEntity.length + offset8);
                                    if (messageEntity instanceof TLRPC.TL_messageEntityBotCommand) {
                                        urlSpanBotCommand = new URLSpanBotCommand(substring, b6);
                                        offset9 = messageEntity.offset;
                                        spannable.setSpan((Object)urlSpanBotCommand, offset9, messageEntity.length + offset9, 33);
                                        b8 = b7;
                                    }
                                    else if (!(messageEntity instanceof TLRPC.TL_messageEntityHashtag) && (!b2 || !(messageEntity instanceof TLRPC.TL_messageEntityMention)) && !(messageEntity instanceof TLRPC.TL_messageEntityCashtag)) {
                                        if (messageEntity instanceof TLRPC.TL_messageEntityEmail) {
                                            sb3 = new StringBuilder();
                                            sb3.append("mailto:");
                                            sb3.append(substring);
                                            urlSpanReplacement = new URLSpanReplacement(sb3.toString());
                                            offset10 = messageEntity.offset;
                                            spannable.setSpan((Object)urlSpanReplacement, offset10, messageEntity.length + offset10, 33);
                                            b8 = b7;
                                        }
                                        else {
                                            if (messageEntity instanceof TLRPC.TL_messageEntityUrl) {
                                                if (Browser.isPassportUrl(messageEntity.url)) {
                                                    b8 = b7;
                                                    continue;
                                                }
                                                if (!substring.toLowerCase().startsWith("http") && !substring.toLowerCase().startsWith("tg://")) {
                                                    sb4 = new StringBuilder();
                                                    sb4.append("http://");
                                                    sb4.append(substring);
                                                    urlSpanBrowser = new URLSpanBrowser(sb4.toString());
                                                    offset11 = messageEntity.offset;
                                                    spannable.setSpan((Object)urlSpanBrowser, offset11, messageEntity.length + offset11, 33);
                                                }
                                                else {
                                                    urlSpanBrowser2 = new URLSpanBrowser(substring);
                                                    offset12 = messageEntity.offset;
                                                    spannable.setSpan((Object)urlSpanBrowser2, offset12, messageEntity.length + offset12, 33);
                                                }
                                            }
                                            else if (messageEntity instanceof TLRPC.TL_messageEntityPhone) {
                                                s = PhoneFormat.stripExceptNumbers(substring);
                                                if (substring.startsWith("+")) {
                                                    sb5 = new StringBuilder();
                                                    sb5.append("+");
                                                    sb5.append(s);
                                                    s = sb5.toString();
                                                }
                                                sb6 = new StringBuilder();
                                                sb6.append("tel:");
                                                sb6.append(s);
                                                urlSpanBrowser3 = new URLSpanBrowser(sb6.toString());
                                                offset13 = messageEntity.offset;
                                                spannable.setSpan((Object)urlSpanBrowser3, offset13, messageEntity.length + offset13, 33);
                                            }
                                            else {
                                                b8 = b7;
                                                if (!(messageEntity instanceof TLRPC.TL_messageEntityTextUrl)) {
                                                    continue;
                                                }
                                                if (Browser.isPassportUrl(messageEntity.url)) {
                                                    b8 = b7;
                                                    continue;
                                                }
                                                urlSpanReplacement2 = new URLSpanReplacement(messageEntity.url);
                                                offset14 = messageEntity.offset;
                                                spannable.setSpan((Object)urlSpanReplacement2, offset14, messageEntity.length + offset14, 33);
                                                b8 = b7;
                                                continue;
                                            }
                                            b8 = true;
                                        }
                                    }
                                    else {
                                        urlSpanNoUnderline = new URLSpanNoUnderline(substring);
                                        offset15 = messageEntity.offset;
                                        spannable.setSpan((Object)urlSpanNoUnderline, offset15, messageEntity.length + offset15, 33);
                                        b8 = b7;
                                    }
                                }
                            }
                        }
                        else {
                            offset16 = messageEntity.offset;
                            urlSpanMono = new URLSpanMono((CharSequence)spannable, offset16, messageEntity.length + offset16, b6);
                            offset17 = messageEntity.offset;
                            spannable.setSpan((Object)urlSpanMono, offset17, messageEntity.length + offset17, 33);
                            b8 = b7;
                        }
                    }
                }
            }
        }
        return b7;
    }
    
    private boolean addEntitiesToText(final CharSequence charSequence, final boolean b) {
        return addEntitiesToText(charSequence, this.messageOwner.entities, this.isOutOwner(), this.type, true, false, b);
    }
    
    public static void addLinks(final boolean b, final CharSequence charSequence) {
        addLinks(b, charSequence, true);
    }
    
    public static void addLinks(final boolean b, final CharSequence charSequence, final boolean b2) {
        if (charSequence instanceof Spannable && containsUrls(charSequence)) {
            if (charSequence.length() < 1000) {
                try {
                    Linkify.addLinks((Spannable)charSequence, 5);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            else {
                try {
                    Linkify.addLinks((Spannable)charSequence, 1);
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
            }
            addUsernamesAndHashtags(b, charSequence, b2, 0);
        }
    }
    
    private static void addUsernamesAndHashtags(final boolean b, final CharSequence charSequence, final boolean b2, final int n) {
        while (true) {
            if (n == 1) {
                try {
                    if (MessageObject.instagramUrlPattern == null) {
                        MessageObject.instagramUrlPattern = Pattern.compile("(^|\\s|\\()@[a-zA-Z\\d_.]{1,32}|(^|\\s|\\()#[\\w.]+");
                    }
                    Matcher matcher = MessageObject.instagramUrlPattern.matcher(charSequence);
                    while (true) {
                        while (matcher.find()) {
                            final int start = matcher.start();
                            final int end = matcher.end();
                            final char char1 = charSequence.charAt(start);
                            int n3;
                            char c;
                            if (n != 0) {
                                int n2 = start;
                                if (char1 != '@') {
                                    n2 = start;
                                    if (char1 != '#') {
                                        n2 = start + 1;
                                    }
                                }
                                final char char2 = charSequence.charAt(n2);
                                n3 = n2;
                                if ((c = char2) != '@') {
                                    n3 = n2;
                                    if ((c = char2) != '#') {
                                        continue;
                                    }
                                }
                            }
                            else {
                                n3 = start;
                                if ((c = char1) != '@') {
                                    n3 = start;
                                    if ((c = char1) != '#') {
                                        n3 = start;
                                        if ((c = char1) != '/') {
                                            n3 = start;
                                            if ((c = char1) != '$') {
                                                n3 = start + 1;
                                                c = char1;
                                            }
                                        }
                                    }
                                }
                            }
                            Object o = null;
                            if (n == 1) {
                                if (c == '@') {
                                    o = new(org.telegram.ui.Components.URLSpanNoUnderline.class);
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("https://instagram.com/");
                                    sb.append(charSequence.subSequence(n3 + 1, end).toString());
                                    new URLSpanNoUnderline(sb.toString());
                                }
                                else if (c == '#') {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("https://www.instagram.com/explore/tags/");
                                    sb2.append(charSequence.subSequence(n3 + 1, end).toString());
                                    o = new URLSpanNoUnderline(sb2.toString());
                                }
                            }
                            else if (n == 2) {
                                if (c == '@') {
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append("https://twitter.com/");
                                    sb3.append(charSequence.subSequence(n3 + 1, end).toString());
                                    o = new URLSpanNoUnderline(sb3.toString());
                                }
                                else if (c == '#') {
                                    final StringBuilder sb4 = new StringBuilder();
                                    sb4.append("https://twitter.com/hashtag/");
                                    sb4.append(charSequence.subSequence(n3 + 1, end).toString());
                                    o = new URLSpanNoUnderline(sb4.toString());
                                }
                            }
                            else if (charSequence.charAt(n3) == '/') {
                                if (b2) {
                                    final String string = charSequence.subSequence(n3, end).toString();
                                    boolean b3;
                                    if (b) {
                                        b3 = true;
                                    }
                                    else {
                                        b3 = false;
                                    }
                                    o = new URLSpanBotCommand(string, b3 ? 1 : 0);
                                }
                            }
                            else {
                                o = new URLSpanNoUnderline(charSequence.subSequence(n3, end).toString());
                            }
                            if (o != null) {
                                ((Spannable)charSequence).setSpan(o, n3, end, 0);
                            }
                        }
                        return;
                        MessageObject.urlPattern = Pattern.compile("(^|\\s)/[a-zA-Z@\\d_]{1,255}|(^|\\s|\\()@[a-zA-Z\\d_]{1,32}|(^|\\s|\\()#[\\w.]+|(^|\\s)\\$[A-Z]{3,8}([ ,.]|$)");
                        Label_0047: {
                            matcher = MessageObject.urlPattern.matcher(charSequence);
                        }
                        continue;
                    }
                }
                // iftrue(Label_0047:, MessageObject.urlPattern != null)
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                return;
            }
            continue;
        }
    }
    
    public static boolean canDeleteMessage(final int n, final TLRPC.Message message, final TLRPC.Chat chat) {
        if (message.id < 0) {
            return true;
        }
        TLRPC.Chat chat2;
        if ((chat2 = chat) == null) {
            chat2 = chat;
            if (message.to_id.channel_id != 0) {
                chat2 = MessagesController.getInstance(n).getChat(message.to_id.channel_id);
            }
        }
        final boolean channel = ChatObject.isChannel(chat2);
        boolean b = false;
        final boolean b2 = false;
        if (channel) {
            boolean b3 = b2;
            if (message.id != 1) {
                if (!chat2.creator) {
                    final TLRPC.TL_chatAdminRights admin_rights = chat2.admin_rights;
                    if (admin_rights == null || (!admin_rights.delete_messages && !message.out)) {
                        b3 = b2;
                        if (!chat2.megagroup) {
                            return b3;
                        }
                        b3 = b2;
                        if (!message.out) {
                            return b3;
                        }
                        b3 = b2;
                        if (message.from_id <= 0) {
                            return b3;
                        }
                    }
                }
                b3 = true;
            }
            return b3;
        }
        if (isOut(message) || !ChatObject.isChannel(chat2)) {
            b = true;
        }
        return b;
    }
    
    public static boolean canEditMessage(final int n, final TLRPC.Message message, TLRPC.Chat chat) {
        final boolean b = false;
        if (chat != null && (chat.left || chat.kicked)) {
            return false;
        }
        if (message != null && message.to_id != null) {
            final TLRPC.MessageMedia media = message.media;
            if (media == null || (!isRoundVideoDocument(media.document) && !isStickerDocument(message.media.document) && !isAnimatedStickerDocument(message.media.document) && !isLocationMessage(message))) {
                final TLRPC.MessageAction action = message.action;
                if ((action == null || action instanceof TLRPC.TL_messageActionEmpty) && !isForwardedMessage(message) && message.via_bot_id == 0) {
                    if (message.id >= 0) {
                        final int from_id = message.from_id;
                        if (from_id == message.to_id.user_id && from_id == UserConfig.getInstance(n).getClientUserId() && !isLiveLocationMessage(message) && !(message.media instanceof TLRPC.TL_messageMediaContact)) {
                            return true;
                        }
                        TLRPC.Chat chat2;
                        if ((chat2 = chat) == null) {
                            chat2 = chat;
                            if (message.to_id.channel_id != 0) {
                                chat = MessagesController.getInstance(n).getChat(message.to_id.channel_id);
                                if ((chat2 = chat) == null) {
                                    return false;
                                }
                            }
                        }
                        final TLRPC.MessageMedia media2 = message.media;
                        if (media2 != null && !(media2 instanceof TLRPC.TL_messageMediaEmpty) && !(media2 instanceof TLRPC.TL_messageMediaPhoto) && !(media2 instanceof TLRPC.TL_messageMediaDocument) && !(media2 instanceof TLRPC.TL_messageMediaWebPage)) {
                            return false;
                        }
                        Label_0313: {
                            if (message.out && chat2 != null && chat2.megagroup) {
                                if (!chat2.creator) {
                                    final TLRPC.TL_chatAdminRights admin_rights = chat2.admin_rights;
                                    if (admin_rights == null || !admin_rights.pin_messages) {
                                        break Label_0313;
                                    }
                                }
                                return true;
                            }
                        }
                        if (Math.abs(message.date - ConnectionsManager.getInstance(n).getCurrentTime()) > MessagesController.getInstance(n).maxEditTime) {
                            return false;
                        }
                        if (message.to_id.channel_id == 0) {
                            if (!message.out) {
                                final boolean b2 = b;
                                if (message.from_id != UserConfig.getInstance(n).getClientUserId()) {
                                    return b2;
                                }
                            }
                            final TLRPC.MessageMedia media3 = message.media;
                            if (!(media3 instanceof TLRPC.TL_messageMediaPhoto) && (!(media3 instanceof TLRPC.TL_messageMediaDocument) || isStickerMessage(message) || isAnimatedStickerMessage(message))) {
                                final TLRPC.MessageMedia media4 = message.media;
                                if (!(media4 instanceof TLRPC.TL_messageMediaEmpty) && !(media4 instanceof TLRPC.TL_messageMediaWebPage)) {
                                    final boolean b2 = b;
                                    if (media4 != null) {
                                        return b2;
                                    }
                                }
                            }
                            return true;
                        }
                        if (!chat2.megagroup || !message.out) {
                            if (chat2.megagroup) {
                                return false;
                            }
                            if (!chat2.creator) {
                                final TLRPC.TL_chatAdminRights admin_rights2 = chat2.admin_rights;
                                if (admin_rights2 == null || (!admin_rights2.edit_messages && !message.out)) {
                                    return false;
                                }
                            }
                            if (!message.post) {
                                return false;
                            }
                        }
                        final TLRPC.MessageMedia media5 = message.media;
                        if (!(media5 instanceof TLRPC.TL_messageMediaPhoto) && (!(media5 instanceof TLRPC.TL_messageMediaDocument) || isStickerMessage(message) || isAnimatedStickerMessage(message))) {
                            final TLRPC.MessageMedia media6 = message.media;
                            if (!(media6 instanceof TLRPC.TL_messageMediaEmpty) && !(media6 instanceof TLRPC.TL_messageMediaWebPage) && media6 != null) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean canEditMessageAnytime(final int n, final TLRPC.Message message, TLRPC.Chat chat) {
        if (message != null && message.to_id != null) {
            final TLRPC.MessageMedia media = message.media;
            if (media == null || (!isRoundVideoDocument(media.document) && !isStickerDocument(message.media.document) && !isAnimatedStickerDocument(message.media.document))) {
                final TLRPC.MessageAction action = message.action;
                if ((action == null || action instanceof TLRPC.TL_messageActionEmpty) && !isForwardedMessage(message) && message.via_bot_id == 0) {
                    if (message.id >= 0) {
                        final int from_id = message.from_id;
                        if (from_id == message.to_id.user_id && from_id == UserConfig.getInstance(n).getClientUserId() && !isLiveLocationMessage(message)) {
                            return true;
                        }
                        TLRPC.Chat chat2;
                        if ((chat2 = chat) == null) {
                            chat2 = chat;
                            if (message.to_id.channel_id != 0) {
                                chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(message.to_id.channel_id);
                                if ((chat2 = chat) == null) {
                                    return false;
                                }
                            }
                        }
                        if (message.out && chat2 != null && chat2.megagroup) {
                            if (!chat2.creator) {
                                final TLRPC.TL_chatAdminRights admin_rights = chat2.admin_rights;
                                if (admin_rights == null || !admin_rights.pin_messages) {
                                    return false;
                                }
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean canPreviewDocument(final TLRPC.Document document) {
        final boolean b = false;
        if (document != null) {
            final String mime_type = document.mime_type;
            if (mime_type != null) {
                final String lowerCase = mime_type.toLowerCase();
                if (isDocumentHasThumb(document) && (lowerCase.equals("image/png") || lowerCase.equals("image/jpg") || lowerCase.equals("image/jpeg"))) {
                    for (int i = 0; i < document.attributes.size(); ++i) {
                        final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                        if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                            final TLRPC.TL_documentAttributeImageSize tl_documentAttributeImageSize = (TLRPC.TL_documentAttributeImageSize)documentAttribute;
                            boolean b2 = b;
                            if (tl_documentAttributeImageSize.w < 6000) {
                                b2 = b;
                                if (tl_documentAttributeImageSize.h < 6000) {
                                    b2 = true;
                                }
                            }
                            return b2;
                        }
                    }
                }
                else if (BuildVars.DEBUG_PRIVATE_VERSION) {
                    final String documentFileName = FileLoader.getDocumentFileName(document);
                    if (documentFileName.startsWith("tg_secret_sticker") && documentFileName.endsWith("json")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void checkEmojiOnly(final int[] array) {
        if (array != null) {
            int i = 0;
            if (array[0] >= 1 && array[0] <= 3) {
                final int n = array[0];
                TextPaint textPaint;
                int n2;
                if (n != 1) {
                    if (n != 2) {
                        textPaint = Theme.chat_msgTextPaintThreeEmoji;
                        n2 = AndroidUtilities.dp(24.0f);
                        this.emojiOnlyCount = 3;
                    }
                    else {
                        textPaint = Theme.chat_msgTextPaintTwoEmoji;
                        n2 = AndroidUtilities.dp(28.0f);
                        this.emojiOnlyCount = 2;
                    }
                }
                else {
                    textPaint = Theme.chat_msgTextPaintOneEmoji;
                    n2 = AndroidUtilities.dp(32.0f);
                    this.emojiOnlyCount = 1;
                }
                final CharSequence messageText = this.messageText;
                final Emoji.EmojiSpan[] array2 = (Emoji.EmojiSpan[])((Spannable)messageText).getSpans(0, messageText.length(), (Class)Emoji.EmojiSpan.class);
                if (array2 != null && array2.length > 0) {
                    while (i < array2.length) {
                        array2[i].replaceFontMetrics(textPaint.getFontMetricsInt(), n2);
                        ++i;
                    }
                }
            }
        }
    }
    
    private static boolean containsUrls(final CharSequence charSequence) {
        if (charSequence != null && charSequence.length() >= 2) {
            if (charSequence.length() <= 20480) {
                final int length = charSequence.length();
                int i = 0;
                int n = 0;
                int n2 = 0;
                int n3 = 0;
                int n4 = 0;
                while (i < length) {
                    final char char1 = charSequence.charAt(i);
                    int n5 = 0;
                    int n6 = 0;
                    int n7 = 0;
                    Label_0133: {
                        if (char1 >= '0' && char1 <= '9') {
                            n5 = n + 1;
                            if (n5 >= 6) {
                                return true;
                            }
                            n6 = 0;
                            n7 = 0;
                        }
                        else {
                            if (char1 != ' ') {
                                n5 = n;
                                n6 = n2;
                                n7 = n3;
                                if (n > 0) {
                                    break Label_0133;
                                }
                            }
                            n5 = 0;
                            n7 = n3;
                            n6 = n2;
                        }
                    }
                    if ((char1 != '@' && char1 != '#' && char1 != '/' && char1 != '$') || i != 0) {
                        if (i != 0) {
                            final int n8 = i - 1;
                            if (charSequence.charAt(n8) == ' ') {
                                return true;
                            }
                            if (charSequence.charAt(n8) == '\n') {
                                return true;
                            }
                        }
                        Label_0304: {
                            if (char1 == ':') {
                                if (n6 == 0) {
                                    n6 = 1;
                                    break Label_0304;
                                }
                            }
                            else {
                                if (char1 != '/') {
                                    if (char1 == '.') {
                                        if (n7 == 0 && n4 != 32) {
                                            ++n7;
                                            break Label_0304;
                                        }
                                    }
                                    else if (char1 != ' ' && n4 == 46 && n7 == 1) {
                                        return true;
                                    }
                                    n7 = 0;
                                    break Label_0304;
                                }
                                if (n6 == 2) {
                                    return true;
                                }
                                if (n6 == 1) {
                                    ++n6;
                                    break Label_0304;
                                }
                            }
                            n6 = 0;
                        }
                        ++i;
                        n4 = char1;
                        n = n5;
                        n2 = n6;
                        n3 = n7;
                        continue;
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    private void createDateArray(final int n, final TLRPC.TL_channelAdminLogEvent tl_channelAdminLogEvent, final ArrayList<MessageObject> list, final HashMap<String, ArrayList<MessageObject>> hashMap) {
        if (hashMap.get(this.dateKey) == null) {
            hashMap.put(this.dateKey, new ArrayList<MessageObject>());
            final TLRPC.TL_message tl_message = new TLRPC.TL_message();
            tl_message.message = LocaleController.formatDateChat(tl_channelAdminLogEvent.date);
            tl_message.id = 0;
            tl_message.date = tl_channelAdminLogEvent.date;
            final MessageObject e = new MessageObject(n, tl_message, false);
            e.type = 10;
            e.contentType = 1;
            e.isDateObject = true;
            list.add(e);
        }
    }
    
    public static long getDialogId(final TLRPC.Message message) {
        if (message.dialog_id == 0L) {
            final TLRPC.Peer to_id = message.to_id;
            if (to_id != null) {
                final int chat_id = to_id.chat_id;
                if (chat_id != 0) {
                    if (chat_id < 0) {
                        message.dialog_id = AndroidUtilities.makeBroadcastId(chat_id);
                    }
                    else {
                        message.dialog_id = -chat_id;
                    }
                }
                else {
                    final int channel_id = to_id.channel_id;
                    if (channel_id != 0) {
                        message.dialog_id = -channel_id;
                    }
                    else if (isOut(message)) {
                        message.dialog_id = message.to_id.user_id;
                    }
                    else {
                        message.dialog_id = message.from_id;
                    }
                }
            }
        }
        return message.dialog_id;
    }
    
    public static TLRPC.Document getDocument(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        if (media instanceof TLRPC.TL_messageMediaWebPage) {
            return media.webpage.document;
        }
        if (media instanceof TLRPC.TL_messageMediaGame) {
            return media.game.document;
        }
        TLRPC.Document document;
        if (media != null) {
            document = media.document;
        }
        else {
            document = null;
        }
        return document;
    }
    
    private TLRPC.Document getDocumentWithId(final TLRPC.WebPage webPage, final long n) {
        if (webPage != null) {
            if (webPage.cached_page != null) {
                final TLRPC.Document document = webPage.document;
                if (document != null && document.id == n) {
                    return document;
                }
                for (int i = 0; i < webPage.cached_page.documents.size(); ++i) {
                    final TLRPC.Document document2 = webPage.cached_page.documents.get(i);
                    if (document2.id == n) {
                        return document2;
                    }
                }
            }
        }
        return null;
    }
    
    public static int getInlineResultDuration(final TLRPC.BotInlineResult botInlineResult) {
        int n;
        if ((n = getWebDocumentDuration(botInlineResult.content)) == 0) {
            n = getWebDocumentDuration(botInlineResult.thumb);
        }
        return n;
    }
    
    public static int[] getInlineResultWidthAndHeight(final TLRPC.BotInlineResult botInlineResult) {
        int[] array;
        if ((array = getWebDocumentWidthAndHeight(botInlineResult.content)) == null && (array = getWebDocumentWidthAndHeight(botInlineResult.thumb)) == null) {
            final int[] array2;
            array = (array2 = new int[2]);
            array2[1] = (array2[0] = 0);
        }
        return array;
    }
    
    public static TLRPC.InputStickerSet getInputStickerSet(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        if (media != null) {
            final TLRPC.Document document = media.document;
            if (document != null) {
                for (final TLRPC.DocumentAttribute documentAttribute : document.attributes) {
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                        final TLRPC.InputStickerSet stickerset = documentAttribute.stickerset;
                        if (stickerset instanceof TLRPC.TL_inputStickerSetEmpty) {
                            return null;
                        }
                        return stickerset;
                    }
                }
            }
        }
        return null;
    }
    
    private MessageObject getMessageObjectForBlock(final TLRPC.WebPage webPage, final TLRPC.PageBlock pageBlock) {
        TLRPC.TL_message tl_message;
        if (pageBlock instanceof TLRPC.TL_pageBlockPhoto) {
            final TLRPC.Photo photoWithId = this.getPhotoWithId(webPage, ((TLRPC.TL_pageBlockPhoto)pageBlock).photo_id);
            if (photoWithId == webPage.photo) {
                return this;
            }
            tl_message = new TLRPC.TL_message();
            tl_message.media = new TLRPC.TL_messageMediaPhoto();
            tl_message.media.photo = photoWithId;
        }
        else if (pageBlock instanceof TLRPC.TL_pageBlockVideo) {
            final TLRPC.TL_pageBlockVideo tl_pageBlockVideo = (TLRPC.TL_pageBlockVideo)pageBlock;
            if (this.getDocumentWithId(webPage, tl_pageBlockVideo.video_id) == webPage.document) {
                return this;
            }
            final TLRPC.TL_message tl_message2 = new TLRPC.TL_message();
            tl_message2.media = new TLRPC.TL_messageMediaDocument();
            tl_message2.media.document = this.getDocumentWithId(webPage, tl_pageBlockVideo.video_id);
            tl_message = tl_message2;
        }
        else {
            tl_message = null;
        }
        tl_message.message = "";
        tl_message.realId = this.getId();
        tl_message.id = Utilities.random.nextInt();
        final TLRPC.Message messageOwner = this.messageOwner;
        tl_message.date = messageOwner.date;
        tl_message.to_id = messageOwner.to_id;
        tl_message.out = messageOwner.out;
        tl_message.from_id = messageOwner.from_id;
        return new MessageObject(this.currentAccount, tl_message, false);
    }
    
    public static int getMessageSize(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        TLRPC.Document document;
        if (media instanceof TLRPC.TL_messageMediaWebPage) {
            document = media.webpage.document;
        }
        else if (media instanceof TLRPC.TL_messageMediaGame) {
            document = media.game.document;
        }
        else if (media != null) {
            document = media.document;
        }
        else {
            document = null;
        }
        if (document != null) {
            return document.size;
        }
        return 0;
    }
    
    public static TLRPC.Photo getPhoto(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        if (media instanceof TLRPC.TL_messageMediaWebPage) {
            return media.webpage.photo;
        }
        TLRPC.Photo photo;
        if (media != null) {
            photo = media.photo;
        }
        else {
            photo = null;
        }
        return photo;
    }
    
    private TLRPC.Photo getPhotoWithId(final TLRPC.WebPage webPage, final long n) {
        if (webPage != null) {
            if (webPage.cached_page != null) {
                final TLRPC.Photo photo = webPage.photo;
                if (photo != null && photo.id == n) {
                    return photo;
                }
                for (int i = 0; i < webPage.cached_page.photos.size(); ++i) {
                    final TLRPC.Photo photo2 = webPage.cached_page.photos.get(i);
                    if (photo2.id == n) {
                        return photo2;
                    }
                }
            }
        }
        return null;
    }
    
    public static long getStickerSetId(final TLRPC.Document document) {
        if (document == null) {
            return -1L;
        }
        int i = 0;
        while (i < document.attributes.size()) {
            final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                final TLRPC.InputStickerSet stickerset = documentAttribute.stickerset;
                if (stickerset instanceof TLRPC.TL_inputStickerSetEmpty) {
                    return -1L;
                }
                return stickerset.id;
            }
            else {
                ++i;
            }
        }
        return -1L;
    }
    
    public static int getUnreadFlags(final TLRPC.Message message) {
        int n;
        final boolean b = (n = (message.unread ? 0 : 1)) != 0;
        if (!message.media_unread) {
            n = ((b ? 1 : 0) | 0x2);
        }
        return n;
    }
    
    private String getUserName(final TLRPC.User user, final ArrayList<TLRPC.MessageEntity> list, final int offset) {
        String formatName;
        if (user == null) {
            formatName = "";
        }
        else {
            formatName = ContactsController.formatName(user.first_name, user.last_name);
        }
        if (offset >= 0) {
            final TLRPC.TL_messageEntityMentionName e = new TLRPC.TL_messageEntityMentionName();
            e.user_id = user.id;
            e.offset = offset;
            e.length = formatName.length();
            list.add(e);
        }
        if (!TextUtils.isEmpty((CharSequence)user.username)) {
            if (offset >= 0) {
                final TLRPC.TL_messageEntityMentionName e2 = new TLRPC.TL_messageEntityMentionName();
                e2.user_id = user.id;
                e2.offset = offset + formatName.length() + 2;
                e2.length = user.username.length() + 1;
                list.add(e2);
            }
            return String.format("%1$s (@%2$s)", formatName, user.username);
        }
        return formatName;
    }
    
    public static int getWebDocumentDuration(final TLRPC.WebDocument webDocument) {
        if (webDocument == null) {
            return 0;
        }
        for (int size = webDocument.attributes.size(), i = 0; i < size; ++i) {
            final TLRPC.DocumentAttribute documentAttribute = webDocument.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                return documentAttribute.duration;
            }
            if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                return documentAttribute.duration;
            }
        }
        return 0;
    }
    
    public static int[] getWebDocumentWidthAndHeight(final TLRPC.WebDocument webDocument) {
        if (webDocument == null) {
            return null;
        }
        for (int size = webDocument.attributes.size(), i = 0; i < size; ++i) {
            final TLRPC.DocumentAttribute documentAttribute = webDocument.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                return new int[] { documentAttribute.w, documentAttribute.h };
            }
            if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                return new int[] { documentAttribute.w, documentAttribute.h };
            }
        }
        return null;
    }
    
    public static boolean isAnimatedStickerDocument(final TLRPC.Document document) {
        return SharedConfig.showAnimatedStickers && document != null && "application/x-tgsticker".equals(document.mime_type) && !document.thumbs.isEmpty();
    }
    
    public static boolean isAnimatedStickerMessage(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        return media != null && isAnimatedStickerDocument(media.document);
    }
    
    public static boolean isContentUnread(final TLRPC.Message message) {
        return message.media_unread;
    }
    
    public static boolean isDocumentHasThumb(final TLRPC.Document document) {
        if (document != null) {
            if (!document.thumbs.isEmpty()) {
                for (int size = document.thumbs.size(), i = 0; i < size; ++i) {
                    final TLRPC.PhotoSize photoSize = document.thumbs.get(i);
                    if (photoSize != null && !(photoSize instanceof TLRPC.TL_photoSizeEmpty) && !(photoSize.location instanceof TLRPC.TL_fileLocationUnavailable)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isForwardedMessage(final TLRPC.Message message) {
        return (message.flags & 0x4) != 0x0 && message.fwd_from != null;
    }
    
    public static boolean isGameMessage(final TLRPC.Message message) {
        return message.media instanceof TLRPC.TL_messageMediaGame;
    }
    
    public static boolean isGifDocument(final WebFile webFile) {
        return webFile != null && (webFile.mime_type.equals("image/gif") || isNewGifDocument(webFile));
    }
    
    public static boolean isGifDocument(final TLRPC.Document document) {
        if (document != null) {
            final String mime_type = document.mime_type;
            if (mime_type != null && (mime_type.equals("image/gif") || isNewGifDocument(document))) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isGifMessage(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        if (media instanceof TLRPC.TL_messageMediaWebPage) {
            return isGifDocument(media.webpage.document);
        }
        return media != null && isGifDocument(media.document);
    }
    
    public static boolean isImageWebDocument(final WebFile webFile) {
        return webFile != null && !isGifDocument(webFile) && webFile.mime_type.startsWith("image/");
    }
    
    public static boolean isInvoiceMessage(final TLRPC.Message message) {
        return message.media instanceof TLRPC.TL_messageMediaInvoice;
    }
    
    public static boolean isLiveLocationMessage(final TLRPC.Message message) {
        return message.media instanceof TLRPC.TL_messageMediaGeoLive;
    }
    
    public static boolean isLocationMessage(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        return media instanceof TLRPC.TL_messageMediaGeo || media instanceof TLRPC.TL_messageMediaGeoLive || media instanceof TLRPC.TL_messageMediaVenue;
    }
    
    public static boolean isMaskDocument(final TLRPC.Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); ++i) {
                final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker && documentAttribute.mask) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isMaskMessage(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        return media != null && isMaskDocument(media.document);
    }
    
    public static boolean isMediaEmpty(final TLRPC.Message message) {
        if (message != null) {
            final TLRPC.MessageMedia media = message.media;
            if (media != null && !(media instanceof TLRPC.TL_messageMediaEmpty)) {
                if (!(media instanceof TLRPC.TL_messageMediaWebPage)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean isMediaEmptyWebpage(final TLRPC.Message message) {
        if (message != null) {
            final TLRPC.MessageMedia media = message.media;
            if (media != null) {
                if (!(media instanceof TLRPC.TL_messageMediaEmpty)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean isMegagroup(final TLRPC.Message message) {
        return (message.flags & Integer.MIN_VALUE) != 0x0;
    }
    
    public static boolean isMusicDocument(final TLRPC.Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); ++i) {
                final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                    return documentAttribute.voice ^ true;
                }
            }
            if (!TextUtils.isEmpty((CharSequence)document.mime_type)) {
                final String lowerCase = document.mime_type.toLowerCase();
                if (!lowerCase.equals("audio/flac") && !lowerCase.equals("audio/ogg") && !lowerCase.equals("audio/opus")) {
                    if (!lowerCase.equals("audio/x-opus+ogg")) {
                        if (!lowerCase.equals("application/octet-stream") || !FileLoader.getDocumentFileName(document).endsWith(".opus")) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean isMusicMessage(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        if (media instanceof TLRPC.TL_messageMediaWebPage) {
            return isMusicDocument(media.webpage.document);
        }
        return media != null && isMusicDocument(media.document);
    }
    
    public static boolean isNewGifDocument(final WebFile webFile) {
        if (webFile != null && "video/mp4".equals(webFile.mime_type)) {
            int i = 0;
            int n = 0;
            int w = 0;
            while (i < webFile.attributes.size()) {
                final TLRPC.DocumentAttribute documentAttribute = webFile.attributes.get(i);
                if (!(documentAttribute instanceof TLRPC.TL_documentAttributeAnimated)) {
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                        n = (w = documentAttribute.w);
                    }
                }
                ++i;
            }
            if (n <= 1280 && w <= 1280) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isNewGifDocument(final TLRPC.Document document) {
        if (document != null && "video/mp4".equals(document.mime_type)) {
            int i = 0;
            int n = 0;
            int n2 = 0;
            int w = 0;
            while (i < document.attributes.size()) {
                final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                int n3;
                if (documentAttribute instanceof TLRPC.TL_documentAttributeAnimated) {
                    n3 = 1;
                }
                else {
                    n3 = n;
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                        n2 = (w = documentAttribute.w);
                        n3 = n;
                    }
                }
                ++i;
                n = n3;
            }
            if (n != 0 && n2 <= 1280 && w <= 1280) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isNewGifMessage(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        if (media instanceof TLRPC.TL_messageMediaWebPage) {
            return isNewGifDocument(media.webpage.document);
        }
        return media != null && isNewGifDocument(media.document);
    }
    
    public static boolean isOut(final TLRPC.Message message) {
        return message.out;
    }
    
    public static boolean isPhoto(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        if (media instanceof TLRPC.TL_messageMediaWebPage) {
            final TLRPC.WebPage webpage = media.webpage;
            return webpage.photo instanceof TLRPC.TL_photo && !(webpage.document instanceof TLRPC.TL_document);
        }
        return media instanceof TLRPC.TL_messageMediaPhoto;
    }
    
    public static boolean isRoundVideoDocument(final TLRPC.Document document) {
        if (document != null && "video/mp4".equals(document.mime_type)) {
            int i = 0;
            boolean round_message = false;
            int n = 0;
            int w = 0;
            while (i < document.attributes.size()) {
                final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                    w = documentAttribute.w;
                    round_message = documentAttribute.round_message;
                    n = w;
                }
                ++i;
            }
            if (round_message && n <= 1280 && w <= 1280) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isRoundVideoMessage(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        if (media instanceof TLRPC.TL_messageMediaWebPage) {
            return isRoundVideoDocument(media.webpage.document);
        }
        return media != null && isRoundVideoDocument(media.document);
    }
    
    public static boolean isSecretMedia(final TLRPC.Message message) {
        final boolean b = message instanceof TLRPC.TL_message_secret;
        final boolean b2 = true;
        boolean b3 = true;
        if (b) {
            if ((!(message.media instanceof TLRPC.TL_messageMediaPhoto) && !isRoundVideoMessage(message) && !isVideoMessage(message)) || message.media.ttl_seconds == 0) {
                b3 = false;
            }
            return b3;
        }
        if (message instanceof TLRPC.TL_message) {
            final TLRPC.MessageMedia media = message.media;
            return (media instanceof TLRPC.TL_messageMediaPhoto || media instanceof TLRPC.TL_messageMediaDocument) && message.media.ttl_seconds != 0 && b2;
        }
        return false;
    }
    
    public static boolean isSecretPhotoOrVideo(final TLRPC.Message message) {
        final boolean b = message instanceof TLRPC.TL_message_secret;
        final boolean b2 = true;
        boolean b3 = true;
        if (b) {
            if (message.media instanceof TLRPC.TL_messageMediaPhoto || isRoundVideoMessage(message) || isVideoMessage(message)) {
                final int ttl = message.ttl;
                if (ttl > 0 && ttl <= 60) {
                    return b3;
                }
            }
            b3 = false;
            return b3;
        }
        if (message instanceof TLRPC.TL_message) {
            final TLRPC.MessageMedia media = message.media;
            return (media instanceof TLRPC.TL_messageMediaPhoto || media instanceof TLRPC.TL_messageMediaDocument) && message.media.ttl_seconds != 0 && b2;
        }
        return false;
    }
    
    public static boolean isStickerDocument(final TLRPC.Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); ++i) {
                if (document.attributes.get(i) instanceof TLRPC.TL_documentAttributeSticker) {
                    return "image/webp".equals(document.mime_type);
                }
            }
        }
        return false;
    }
    
    public static boolean isStickerMessage(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        return media != null && isStickerDocument(media.document);
    }
    
    public static boolean isUnread(final TLRPC.Message message) {
        return message.unread;
    }
    
    public static boolean isVideoDocument(final TLRPC.Document document) {
        boolean b2;
        final boolean b = b2 = false;
        if (document != null) {
            int i = 0;
            int n = 0;
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            while (i < document.attributes.size()) {
                final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                int w;
                int h;
                int n5;
                if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                    if (documentAttribute.round_message) {
                        return false;
                    }
                    w = documentAttribute.w;
                    h = documentAttribute.h;
                    n5 = 1;
                }
                else {
                    w = n2;
                    h = n3;
                    n5 = n4;
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeAnimated) {
                        n = 1;
                        n5 = n4;
                        h = n3;
                        w = n2;
                    }
                }
                ++i;
                n2 = w;
                n3 = h;
                n4 = n5;
            }
            boolean b3 = n != 0;
            Label_0169: {
                if (n != 0) {
                    if (n2 <= 1280) {
                        b3 = (n != 0);
                        if (n3 <= 1280) {
                            break Label_0169;
                        }
                    }
                    b3 = false;
                }
            }
            int n6 = n4;
            if (SharedConfig.streamMkv && (n6 = n4) == 0) {
                n6 = n4;
                if ("video/x-matroska".equals(document.mime_type)) {
                    n6 = 1;
                }
            }
            b2 = b;
            if (n6 != 0) {
                b2 = b;
                if (!b3) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    public static boolean isVideoMessage(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        if (media instanceof TLRPC.TL_messageMediaWebPage) {
            return isVideoDocument(media.webpage.document);
        }
        return media != null && isVideoDocument(media.document);
    }
    
    public static boolean isVideoWebDocument(final WebFile webFile) {
        return webFile != null && webFile.mime_type.startsWith("video/");
    }
    
    public static boolean isVoiceDocument(final TLRPC.Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); ++i) {
                final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                    return documentAttribute.voice;
                }
            }
        }
        return false;
    }
    
    public static boolean isVoiceMessage(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        if (media instanceof TLRPC.TL_messageMediaWebPage) {
            return isVoiceDocument(media.webpage.document);
        }
        return media != null && isVoiceDocument(media.document);
    }
    
    public static boolean isVoiceWebDocument(final WebFile webFile) {
        return webFile != null && webFile.mime_type.equals("audio/ogg");
    }
    
    private boolean needDrawAvatarInternal() {
        if ((!this.isFromChat() || !this.isFromUser()) && this.eventId == 0L) {
            final TLRPC.MessageFwdHeader fwd_from = this.messageOwner.fwd_from;
            if (fwd_from == null || fwd_from.saved_from_peer == null) {
                return false;
            }
        }
        return true;
    }
    
    public static void setUnreadFlags(final TLRPC.Message message, final int n) {
        final boolean b = false;
        message.unread = ((n & 0x1) == 0x0);
        boolean media_unread = b;
        if ((n & 0x2) == 0x0) {
            media_unread = true;
        }
        message.media_unread = media_unread;
    }
    
    public static boolean shouldEncryptPhotoOrVideo(final TLRPC.Message message) {
        final boolean b = message instanceof TLRPC.TL_message_secret;
        final boolean b2 = true;
        boolean b3 = true;
        if (b) {
            if (message.media instanceof TLRPC.TL_messageMediaPhoto || isVideoMessage(message)) {
                final int ttl = message.ttl;
                if (ttl > 0 && ttl <= 60) {
                    return b3;
                }
            }
            b3 = false;
            return b3;
        }
        final TLRPC.MessageMedia media = message.media;
        return (media instanceof TLRPC.TL_messageMediaPhoto || media instanceof TLRPC.TL_messageMediaDocument) && message.media.ttl_seconds != 0 && b2;
    }
    
    private static void updatePhotoSizeLocations(final ArrayList<TLRPC.PhotoSize> list, final ArrayList<TLRPC.PhotoSize> list2) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            final TLRPC.PhotoSize photoSize = list.get(i);
            for (int size2 = list2.size(), j = 0; j < size2; ++j) {
                final TLRPC.PhotoSize photoSize2 = list2.get(j);
                if (!(photoSize2 instanceof TLRPC.TL_photoSizeEmpty)) {
                    if (!(photoSize2 instanceof TLRPC.TL_photoCachedSize)) {
                        if (photoSize2.type.equals(photoSize.type)) {
                            photoSize.location = photoSize2.location;
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public static void updatePollResults(final TLRPC.TL_messageMediaPoll tl_messageMediaPoll, final TLRPC.TL_pollResults tl_pollResults) {
        if ((tl_pollResults.flags & 0x2) != 0x0) {
            final byte[] array = null;
            final boolean min = tl_pollResults.min;
            final int n = 0;
            byte[] option = array;
            if (min) {
                final ArrayList<TLRPC.TL_pollAnswerVoters> results = tl_messageMediaPoll.results.results;
                option = array;
                if (results != null) {
                    final int size = results.size();
                    int index = 0;
                    while (true) {
                        option = array;
                        if (index >= size) {
                            break;
                        }
                        final TLRPC.TL_pollAnswerVoters tl_pollAnswerVoters = tl_messageMediaPoll.results.results.get(index);
                        if (tl_pollAnswerVoters.chosen) {
                            option = tl_pollAnswerVoters.option;
                            break;
                        }
                        ++index;
                    }
                }
            }
            final TLRPC.TL_pollResults results2 = tl_messageMediaPoll.results;
            results2.results = tl_pollResults.results;
            if (option != null) {
                for (int size2 = results2.results.size(), i = n; i < size2; ++i) {
                    final TLRPC.TL_pollAnswerVoters tl_pollAnswerVoters2 = tl_messageMediaPoll.results.results.get(i);
                    if (Arrays.equals(tl_pollAnswerVoters2.option, option)) {
                        tl_pollAnswerVoters2.chosen = true;
                        break;
                    }
                }
            }
            final TLRPC.TL_pollResults results3 = tl_messageMediaPoll.results;
            results3.flags |= 0x2;
        }
        if ((tl_pollResults.flags & 0x4) != 0x0) {
            final TLRPC.TL_pollResults results4 = tl_messageMediaPoll.results;
            results4.total_voters = tl_pollResults.total_voters;
            results4.flags |= 0x4;
        }
    }
    
    public boolean addEntitiesToText(final CharSequence charSequence, final boolean b, final boolean b2) {
        return addEntitiesToText(charSequence, this.messageOwner.entities, this.isOutOwner(), this.type, true, b, b2);
    }
    
    public void applyMediaExistanceFlags(final int n) {
        if (n == -1) {
            this.checkMediaExistance();
        }
        else {
            final boolean b = false;
            this.attachPathExists = ((n & 0x1) != 0x0);
            boolean mediaExists = b;
            if ((n & 0x2) != 0x0) {
                mediaExists = true;
            }
            this.mediaExists = mediaExists;
        }
    }
    
    public void applyNewText() {
        if (TextUtils.isEmpty((CharSequence)this.messageOwner.message)) {
            return;
        }
        final boolean fromUser = this.isFromUser();
        int[] array = null;
        TLRPC.User user;
        if (fromUser) {
            user = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.from_id);
        }
        else {
            user = null;
        }
        final TLRPC.Message messageOwner = this.messageOwner;
        this.messageText = messageOwner.message;
        TextPaint textPaint;
        if (messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
            textPaint = Theme.chat_msgGameTextPaint;
        }
        else {
            textPaint = Theme.chat_msgTextPaint;
        }
        if (SharedConfig.allowBigEmoji) {
            array = new int[] { 0 };
        }
        this.messageText = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, array);
        this.checkEmojiOnly(array);
        this.generateLayout(user);
    }
    
    public boolean canDeleteMessage(final TLRPC.Chat chat) {
        return this.eventId == 0L && canDeleteMessage(this.currentAccount, this.messageOwner, chat);
    }
    
    public boolean canEditMedia() {
        final boolean secretMedia = this.isSecretMedia();
        final boolean b = false;
        if (secretMedia) {
            return false;
        }
        final TLRPC.MessageMedia media = this.messageOwner.media;
        if (media instanceof TLRPC.TL_messageMediaPhoto) {
            return true;
        }
        boolean b2 = b;
        if (media instanceof TLRPC.TL_messageMediaDocument) {
            b2 = b;
            if (!this.isVoice()) {
                b2 = b;
                if (!this.isSticker()) {
                    b2 = b;
                    if (!this.isAnimatedSticker()) {
                        b2 = b;
                        if (!this.isRoundVideo()) {
                            b2 = true;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    public boolean canEditMessage(final TLRPC.Chat chat) {
        return canEditMessage(this.currentAccount, this.messageOwner, chat);
    }
    
    public boolean canEditMessageAnytime(final TLRPC.Chat chat) {
        return canEditMessageAnytime(this.currentAccount, this.messageOwner, chat);
    }
    
    public boolean canForwardMessage() {
        return !(this.messageOwner instanceof TLRPC.TL_message_secret) && !this.needDrawBluredPreview() && !this.isLiveLocation() && this.type != 16;
    }
    
    public boolean canPreviewDocument() {
        return canPreviewDocument(this.getDocument());
    }
    
    public boolean canStreamVideo() {
        final TLRPC.Document document = this.getDocument();
        if (document != null) {
            if (!(document instanceof TLRPC.TL_documentEncrypted)) {
                if (SharedConfig.streamAllVideo) {
                    return true;
                }
                for (int i = 0; i < document.attributes.size(); ++i) {
                    final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                        return documentAttribute.supports_streaming;
                    }
                }
                if (SharedConfig.streamMkv && "video/x-matroska".equals(document.mime_type)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void checkForScam() {
    }
    
    public boolean checkLayout() {
        if (this.type == 0 && this.messageOwner.to_id != null) {
            final CharSequence messageText = this.messageText;
            if (messageText != null) {
                if (messageText.length() != 0) {
                    if (this.layoutCreated) {
                        int n;
                        if (AndroidUtilities.isTablet()) {
                            n = AndroidUtilities.getMinTabletSide();
                        }
                        else {
                            n = AndroidUtilities.displaySize.x;
                        }
                        if (Math.abs(this.generatedWithMinSize - n) > AndroidUtilities.dp(52.0f) || this.generatedWithDensity != AndroidUtilities.density) {
                            this.layoutCreated = false;
                        }
                    }
                    if (!this.layoutCreated) {
                        this.layoutCreated = true;
                        final boolean fromUser = this.isFromUser();
                        int[] array = null;
                        TLRPC.User user;
                        if (fromUser) {
                            user = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.from_id);
                        }
                        else {
                            user = null;
                        }
                        TextPaint textPaint;
                        if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                            textPaint = Theme.chat_msgGameTextPaint;
                        }
                        else {
                            textPaint = Theme.chat_msgTextPaint;
                        }
                        if (SharedConfig.allowBigEmoji) {
                            array = new int[] { 0 };
                        }
                        this.messageText = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, array);
                        this.checkEmojiOnly(array);
                        this.generateLayout(user);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void checkMediaExistance() {
        this.attachPathExists = false;
        this.mediaExists = false;
        final int type = this.type;
        if (type == 1) {
            if (FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize()) != null) {
                final File pathToMessage = FileLoader.getPathToMessage(this.messageOwner);
                if (this.needDrawBluredPreview()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(pathToMessage.getAbsolutePath());
                    sb.append(".enc");
                    this.mediaExists = new File(sb.toString()).exists();
                }
                if (!this.mediaExists) {
                    this.mediaExists = pathToMessage.exists();
                }
            }
        }
        else if (type != 8 && type != 3 && type != 9 && type != 2 && type != 14 && type != 5) {
            final TLRPC.Document document = this.getDocument();
            if (document != null) {
                if (this.isWallpaper()) {
                    this.mediaExists = FileLoader.getPathToAttach(document, true).exists();
                }
                else {
                    this.mediaExists = FileLoader.getPathToAttach(document).exists();
                }
            }
            else if (this.type == 0) {
                final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize());
                if (closestPhotoSizeWithSize == null) {
                    return;
                }
                if (closestPhotoSizeWithSize != null) {
                    this.mediaExists = FileLoader.getPathToAttach(closestPhotoSizeWithSize, true).exists();
                }
            }
        }
        else {
            final String attachPath = this.messageOwner.attachPath;
            if (attachPath != null && attachPath.length() > 0) {
                this.attachPathExists = new File(this.messageOwner.attachPath).exists();
            }
            if (!this.attachPathExists) {
                final File pathToMessage2 = FileLoader.getPathToMessage(this.messageOwner);
                if (this.type == 3 && this.needDrawBluredPreview()) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(pathToMessage2.getAbsolutePath());
                    sb2.append(".enc");
                    this.mediaExists = new File(sb2.toString()).exists();
                }
                if (!this.mediaExists) {
                    this.mediaExists = pathToMessage2.exists();
                }
            }
        }
    }
    
    public void createMessageSendInfo() {
        final TLRPC.Message messageOwner = this.messageOwner;
        if (messageOwner.message != null && (messageOwner.id < 0 || this.isEditing())) {
            final HashMap<String, String> params = this.messageOwner.params;
            if (params != null) {
                final String s = params.get("ve");
                if (s != null && (this.isVideo() || this.isNewGif() || this.isRoundVideo())) {
                    this.videoEditedInfo = new VideoEditedInfo();
                    if (!this.videoEditedInfo.parseString(s)) {
                        this.videoEditedInfo = null;
                    }
                    else {
                        this.videoEditedInfo.roundVideo = this.isRoundVideo();
                    }
                }
                final TLRPC.Message messageOwner2 = this.messageOwner;
                if (messageOwner2.send_state == 3) {
                    final String s2 = messageOwner2.params.get("prevMedia");
                    if (s2 != null) {
                        final SerializedData serializedData = new SerializedData(Base64.decode(s2, 0));
                        this.previousMedia = TLRPC.MessageMedia.TLdeserialize(serializedData, serializedData.readInt32(false), false);
                        this.previousCaption = serializedData.readString(false);
                        this.previousAttachPath = serializedData.readString(false);
                        final int int32 = serializedData.readInt32(false);
                        this.previousCaptionEntities = new ArrayList<TLRPC.MessageEntity>(int32);
                        for (int i = 0; i < int32; ++i) {
                            this.previousCaptionEntities.add(TLRPC.MessageEntity.TLdeserialize(serializedData, serializedData.readInt32(false), false));
                        }
                        serializedData.cleanup();
                    }
                }
            }
        }
    }
    
    public boolean equals(final MessageObject messageObject) {
        return this.getId() == messageObject.getId() && this.getDialogId() == messageObject.getDialogId();
    }
    
    public void generateCaption() {
        if (this.caption == null) {
            if (!this.isRoundVideo()) {
                if (!this.isMediaEmpty()) {
                    final TLRPC.Message messageOwner = this.messageOwner;
                    if (!(messageOwner.media instanceof TLRPC.TL_messageMediaGame) && !TextUtils.isEmpty((CharSequence)messageOwner.message)) {
                        this.caption = Emoji.replaceEmoji(this.messageOwner.message, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                        final TLRPC.Message messageOwner2 = this.messageOwner;
                        boolean b = false;
                        Label_0147: {
                            if (messageOwner2.send_state != 0) {
                                for (int i = 0; i < this.messageOwner.entities.size(); ++i) {
                                    if (!(this.messageOwner.entities.get(i) instanceof TLRPC.TL_inputMessageEntityMentionName)) {
                                        b = true;
                                        break Label_0147;
                                    }
                                }
                                b = false;
                            }
                            else {
                                b = (messageOwner2.entities.isEmpty() ^ true);
                            }
                        }
                        boolean b2 = false;
                        Label_0244: {
                            Label_0242: {
                                if (!b) {
                                    if (this.eventId == 0L) {
                                        final TLRPC.MessageMedia media = this.messageOwner.media;
                                        if (!(media instanceof TLRPC.TL_messageMediaPhoto_old) && !(media instanceof TLRPC.TL_messageMediaPhoto_layer68) && !(media instanceof TLRPC.TL_messageMediaPhoto_layer74) && !(media instanceof TLRPC.TL_messageMediaDocument_old) && !(media instanceof TLRPC.TL_messageMediaDocument_layer68) && !(media instanceof TLRPC.TL_messageMediaDocument_layer74) && (this.isOut() || this.messageOwner.send_state == 0) && this.messageOwner.id >= 0) {
                                            break Label_0242;
                                        }
                                    }
                                    b2 = true;
                                    break Label_0244;
                                }
                            }
                            b2 = false;
                        }
                        if (b2) {
                            if (containsUrls(this.caption)) {
                                try {
                                    Linkify.addLinks((Spannable)this.caption, 5);
                                }
                                catch (Exception ex) {
                                    FileLog.e(ex);
                                }
                            }
                            addUsernamesAndHashtags(this.isOutOwner(), this.caption, true, 0);
                        }
                        else {
                            try {
                                Linkify.addLinks((Spannable)this.caption, 4);
                            }
                            catch (Throwable t) {
                                FileLog.e(t);
                            }
                        }
                        this.addEntitiesToText(this.caption, b2);
                    }
                }
            }
        }
    }
    
    public void generateGameMessageText(final TLRPC.User user) {
        TLRPC.User user2 = user;
        if (user == null) {
            user2 = user;
            if (this.messageOwner.from_id > 0) {
                user2 = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.from_id);
            }
        }
        final TLObject tlObject = null;
        final MessageObject replyMessageObject = this.replyMessageObject;
        TLObject tlObject2 = tlObject;
        if (replyMessageObject != null) {
            final TLRPC.MessageMedia media = replyMessageObject.messageOwner.media;
            tlObject2 = tlObject;
            if (media != null) {
                final TLRPC.TL_game game = media.game;
                tlObject2 = tlObject;
                if (game != null) {
                    tlObject2 = game;
                }
            }
        }
        if (tlObject2 == null) {
            if (user2 != null && user2.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                this.messageText = LocaleController.formatString("ActionYouScored", 2131558553, LocaleController.formatPluralString("Points", this.messageOwner.action.score));
            }
            else {
                this.messageText = this.replaceWithLink(LocaleController.formatString("ActionUserScored", 2131558544, LocaleController.formatPluralString("Points", this.messageOwner.action.score)), "un1", user2);
            }
        }
        else {
            if (user2 != null && user2.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                this.messageText = LocaleController.formatString("ActionYouScoredInGame", 2131558554, LocaleController.formatPluralString("Points", this.messageOwner.action.score));
            }
            else {
                this.messageText = this.replaceWithLink(LocaleController.formatString("ActionUserScoredInGame", 2131558545, LocaleController.formatPluralString("Points", this.messageOwner.action.score)), "un1", user2);
            }
            this.messageText = this.replaceWithLink(this.messageText, "un2", tlObject2);
        }
    }
    
    public void generateLayout(final TLRPC.User p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        org/telegram/messenger/MessageObject.type:I
        //     4: ifne            1726
        //     7: aload_0        
        //     8: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //    11: getfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        //    14: ifnull          1726
        //    17: aload_0        
        //    18: getfield        org/telegram/messenger/MessageObject.messageText:Ljava/lang/CharSequence;
        //    21: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //    24: ifeq            30
        //    27: goto            1726
        //    30: aload_0        
        //    31: invokevirtual   org/telegram/messenger/MessageObject.generateLinkDescription:()V
        //    34: aload_0        
        //    35: new             Ljava/util/ArrayList;
        //    38: dup            
        //    39: invokespecial   java/util/ArrayList.<init>:()V
        //    42: putfield        org/telegram/messenger/MessageObject.textLayoutBlocks:Ljava/util/ArrayList;
        //    45: aload_0        
        //    46: iconst_0       
        //    47: putfield        org/telegram/messenger/MessageObject.textWidth:I
        //    50: aload_0        
        //    51: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //    54: astore_1       
        //    55: aload_1        
        //    56: getfield        org/telegram/tgnet/TLRPC$Message.send_state:I
        //    59: ifeq            111
        //    62: iconst_0       
        //    63: istore_2       
        //    64: iload_2        
        //    65: aload_0        
        //    66: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //    69: getfield        org/telegram/tgnet/TLRPC$Message.entities:Ljava/util/ArrayList;
        //    72: invokevirtual   java/util/ArrayList.size:()I
        //    75: if_icmpge       106
        //    78: aload_0        
        //    79: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //    82: getfield        org/telegram/tgnet/TLRPC$Message.entities:Ljava/util/ArrayList;
        //    85: iload_2        
        //    86: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //    89: instanceof      Lorg/telegram/tgnet/TLRPC$TL_inputMessageEntityMentionName;
        //    92: ifne            100
        //    95: iconst_1       
        //    96: istore_2       
        //    97: goto            121
        //   100: iinc            2, 1
        //   103: goto            64
        //   106: iconst_0       
        //   107: istore_2       
        //   108: goto            121
        //   111: aload_1        
        //   112: getfield        org/telegram/tgnet/TLRPC$Message.entities:Ljava/util/ArrayList;
        //   115: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //   118: iconst_1       
        //   119: ixor           
        //   120: istore_2       
        //   121: iload_2        
        //   122: ifne            242
        //   125: aload_0        
        //   126: getfield        org/telegram/messenger/MessageObject.eventId:J
        //   129: lconst_0       
        //   130: lcmp           
        //   131: ifne            237
        //   134: aload_0        
        //   135: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   138: astore_1       
        //   139: aload_1        
        //   140: instanceof      Lorg/telegram/tgnet/TLRPC$TL_message_old;
        //   143: ifne            237
        //   146: aload_1        
        //   147: instanceof      Lorg/telegram/tgnet/TLRPC$TL_message_old2;
        //   150: ifne            237
        //   153: aload_1        
        //   154: instanceof      Lorg/telegram/tgnet/TLRPC$TL_message_old3;
        //   157: ifne            237
        //   160: aload_1        
        //   161: instanceof      Lorg/telegram/tgnet/TLRPC$TL_message_old4;
        //   164: ifne            237
        //   167: aload_1        
        //   168: instanceof      Lorg/telegram/tgnet/TLRPC$TL_messageForwarded_old;
        //   171: ifne            237
        //   174: aload_1        
        //   175: instanceof      Lorg/telegram/tgnet/TLRPC$TL_messageForwarded_old2;
        //   178: ifne            237
        //   181: aload_1        
        //   182: instanceof      Lorg/telegram/tgnet/TLRPC$TL_message_secret;
        //   185: ifne            237
        //   188: aload_1        
        //   189: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   192: instanceof      Lorg/telegram/tgnet/TLRPC$TL_messageMediaInvoice;
        //   195: ifne            237
        //   198: aload_0        
        //   199: invokevirtual   org/telegram/messenger/MessageObject.isOut:()Z
        //   202: ifeq            215
        //   205: aload_0        
        //   206: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   209: getfield        org/telegram/tgnet/TLRPC$Message.send_state:I
        //   212: ifne            237
        //   215: aload_0        
        //   216: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   219: astore_1       
        //   220: aload_1        
        //   221: getfield        org/telegram/tgnet/TLRPC$Message.id:I
        //   224: iflt            237
        //   227: aload_1        
        //   228: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   231: instanceof      Lorg/telegram/tgnet/TLRPC$TL_messageMediaUnsupported;
        //   234: ifeq            242
        //   237: iconst_1       
        //   238: istore_3       
        //   239: goto            244
        //   242: iconst_0       
        //   243: istore_3       
        //   244: iload_3        
        //   245: ifeq            262
        //   248: aload_0        
        //   249: invokevirtual   org/telegram/messenger/MessageObject.isOutOwner:()Z
        //   252: aload_0        
        //   253: getfield        org/telegram/messenger/MessageObject.messageText:Ljava/lang/CharSequence;
        //   256: invokestatic    org/telegram/messenger/MessageObject.addLinks:(ZLjava/lang/CharSequence;)V
        //   259: goto            306
        //   262: aload_0        
        //   263: getfield        org/telegram/messenger/MessageObject.messageText:Ljava/lang/CharSequence;
        //   266: astore_1       
        //   267: aload_1        
        //   268: instanceof      Landroid/text/Spannable;
        //   271: ifeq            306
        //   274: aload_1        
        //   275: invokeinterface java/lang/CharSequence.length:()I
        //   280: sipush          1000
        //   283: if_icmpge       306
        //   286: aload_0        
        //   287: getfield        org/telegram/messenger/MessageObject.messageText:Ljava/lang/CharSequence;
        //   290: checkcast       Landroid/text/Spannable;
        //   293: iconst_4       
        //   294: invokestatic    android/text/util/Linkify.addLinks:(Landroid/text/Spannable;I)Z
        //   297: pop            
        //   298: goto            306
        //   301: astore_1       
        //   302: aload_1        
        //   303: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   306: aload_0        
        //   307: aload_0        
        //   308: getfield        org/telegram/messenger/MessageObject.messageText:Ljava/lang/CharSequence;
        //   311: iload_3        
        //   312: invokespecial   org/telegram/messenger/MessageObject.addEntitiesToText:(Ljava/lang/CharSequence;Z)Z
        //   315: istore_3       
        //   316: aload_0        
        //   317: invokevirtual   org/telegram/messenger/MessageObject.getMaxMessageTextWidth:()I
        //   320: istore          4
        //   322: aload_0        
        //   323: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   326: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   329: instanceof      Lorg/telegram/tgnet/TLRPC$TL_messageMediaGame;
        //   332: ifeq            342
        //   335: getstatic       org/telegram/ui/ActionBar/Theme.chat_msgGameTextPaint:Landroid/text/TextPaint;
        //   338: astore_1       
        //   339: goto            346
        //   342: getstatic       org/telegram/ui/ActionBar/Theme.chat_msgTextPaint:Landroid/text/TextPaint;
        //   345: astore_1       
        //   346: aload_1        
        //   347: astore          5
        //   349: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   352: bipush          24
        //   354: if_icmplt       399
        //   357: aload_0        
        //   358: getfield        org/telegram/messenger/MessageObject.messageText:Ljava/lang/CharSequence;
        //   361: iconst_0       
        //   362: aload_0        
        //   363: getfield        org/telegram/messenger/MessageObject.messageText:Ljava/lang/CharSequence;
        //   366: invokeinterface java/lang/CharSequence.length:()I
        //   371: aload           5
        //   373: iload           4
        //   375: invokestatic    android/text/StaticLayout$Builder.obtain:(Ljava/lang/CharSequence;IILandroid/text/TextPaint;I)Landroid/text/StaticLayout$Builder;
        //   378: iconst_1       
        //   379: invokevirtual   android/text/StaticLayout$Builder.setBreakStrategy:(I)Landroid/text/StaticLayout$Builder;
        //   382: iconst_0       
        //   383: invokevirtual   android/text/StaticLayout$Builder.setHyphenationFrequency:(I)Landroid/text/StaticLayout$Builder;
        //   386: getstatic       android/text/Layout$Alignment.ALIGN_NORMAL:Landroid/text/Layout$Alignment;
        //   389: invokevirtual   android/text/StaticLayout$Builder.setAlignment:(Landroid/text/Layout$Alignment;)Landroid/text/StaticLayout$Builder;
        //   392: invokevirtual   android/text/StaticLayout$Builder.build:()Landroid/text/StaticLayout;
        //   395: astore_1       
        //   396: goto            421
        //   399: new             Landroid/text/StaticLayout;
        //   402: dup            
        //   403: aload_0        
        //   404: getfield        org/telegram/messenger/MessageObject.messageText:Ljava/lang/CharSequence;
        //   407: aload           5
        //   409: iload           4
        //   411: getstatic       android/text/Layout$Alignment.ALIGN_NORMAL:Landroid/text/Layout$Alignment;
        //   414: fconst_1       
        //   415: fconst_0       
        //   416: iconst_0       
        //   417: invokespecial   android/text/StaticLayout.<init>:(Ljava/lang/CharSequence;Landroid/text/TextPaint;ILandroid/text/Layout$Alignment;FFZ)V
        //   420: astore_1       
        //   421: aload_0        
        //   422: aload_1        
        //   423: invokevirtual   android/text/StaticLayout.getHeight:()I
        //   426: putfield        org/telegram/messenger/MessageObject.textHeight:I
        //   429: aload_0        
        //   430: aload_1        
        //   431: invokevirtual   android/text/StaticLayout.getLineCount:()I
        //   434: putfield        org/telegram/messenger/MessageObject.linesCount:I
        //   437: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   440: bipush          24
        //   442: if_icmplt       451
        //   445: iconst_1       
        //   446: istore          6
        //   448: goto            467
        //   451: aload_0        
        //   452: getfield        org/telegram/messenger/MessageObject.linesCount:I
        //   455: i2f            
        //   456: ldc_w           10.0
        //   459: fdiv           
        //   460: f2d            
        //   461: invokestatic    java/lang/Math.ceil:(D)D
        //   464: d2i            
        //   465: istore          6
        //   467: iconst_0       
        //   468: istore_2       
        //   469: iconst_0       
        //   470: istore          7
        //   472: fconst_0       
        //   473: fstore          8
        //   475: iload           7
        //   477: iload           6
        //   479: if_icmpge       1720
        //   482: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   485: bipush          24
        //   487: if_icmplt       499
        //   490: aload_0        
        //   491: getfield        org/telegram/messenger/MessageObject.linesCount:I
        //   494: istore          9
        //   496: goto            512
        //   499: bipush          10
        //   501: aload_0        
        //   502: getfield        org/telegram/messenger/MessageObject.linesCount:I
        //   505: iload_2        
        //   506: isub           
        //   507: invokestatic    java/lang/Math.min:(II)I
        //   510: istore          9
        //   512: new             Lorg/telegram/messenger/MessageObject$TextLayoutBlock;
        //   515: dup            
        //   516: invokespecial   org/telegram/messenger/MessageObject$TextLayoutBlock.<init>:()V
        //   519: astore          10
        //   521: iload           6
        //   523: iconst_1       
        //   524: if_icmpne       706
        //   527: aload           10
        //   529: aload_1        
        //   530: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textLayout:Landroid/text/StaticLayout;
        //   533: aload           10
        //   535: fconst_0       
        //   536: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textYOffset:F
        //   539: aload           10
        //   541: iconst_0       
        //   542: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.charactersOffset:I
        //   545: aload_0        
        //   546: getfield        org/telegram/messenger/MessageObject.emojiOnlyCount:I
        //   549: istore          11
        //   551: iload           11
        //   553: ifeq            682
        //   556: iload           11
        //   558: iconst_1       
        //   559: if_icmpeq       649
        //   562: iload           11
        //   564: iconst_2       
        //   565: if_icmpeq       613
        //   568: iload           11
        //   570: iconst_3       
        //   571: if_icmpeq       577
        //   574: goto            682
        //   577: aload_0        
        //   578: aload_0        
        //   579: getfield        org/telegram/messenger/MessageObject.textHeight:I
        //   582: ldc_w           4.2
        //   585: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   588: isub           
        //   589: putfield        org/telegram/messenger/MessageObject.textHeight:I
        //   592: aload           10
        //   594: aload           10
        //   596: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textYOffset:F
        //   599: ldc_w           4.2
        //   602: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   605: i2f            
        //   606: fsub           
        //   607: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textYOffset:F
        //   610: goto            682
        //   613: aload_0        
        //   614: aload_0        
        //   615: getfield        org/telegram/messenger/MessageObject.textHeight:I
        //   618: ldc_w           4.5
        //   621: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   624: isub           
        //   625: putfield        org/telegram/messenger/MessageObject.textHeight:I
        //   628: aload           10
        //   630: aload           10
        //   632: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textYOffset:F
        //   635: ldc_w           4.5
        //   638: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   641: i2f            
        //   642: fsub           
        //   643: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textYOffset:F
        //   646: goto            682
        //   649: aload_0        
        //   650: aload_0        
        //   651: getfield        org/telegram/messenger/MessageObject.textHeight:I
        //   654: ldc_w           5.3
        //   657: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   660: isub           
        //   661: putfield        org/telegram/messenger/MessageObject.textHeight:I
        //   664: aload           10
        //   666: aload           10
        //   668: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textYOffset:F
        //   671: ldc_w           5.3
        //   674: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   677: i2f            
        //   678: fsub           
        //   679: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textYOffset:F
        //   682: aload           10
        //   684: aload_0        
        //   685: getfield        org/telegram/messenger/MessageObject.textHeight:I
        //   688: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.height:I
        //   691: iload           9
        //   693: istore          11
        //   695: iload           6
        //   697: istore          9
        //   699: iload           11
        //   701: istore          6
        //   703: goto            1029
        //   706: aload_1        
        //   707: iload_2        
        //   708: invokevirtual   android/text/StaticLayout.getLineStart:(I)I
        //   711: istore          11
        //   713: aload_1        
        //   714: iload_2        
        //   715: iload           9
        //   717: iadd           
        //   718: iconst_1       
        //   719: isub           
        //   720: invokevirtual   android/text/StaticLayout.getLineEnd:(I)I
        //   723: istore          12
        //   725: iload           12
        //   727: iload           11
        //   729: if_icmpge       739
        //   732: iload           6
        //   734: istore          9
        //   736: goto            1698
        //   739: aload           10
        //   741: iload           11
        //   743: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.charactersOffset:I
        //   746: aload           10
        //   748: iload           12
        //   750: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.charactersEnd:I
        //   753: iload_3        
        //   754: ifeq            810
        //   757: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   760: bipush          24
        //   762: if_icmplt       810
        //   765: aload           10
        //   767: aload_0        
        //   768: getfield        org/telegram/messenger/MessageObject.messageText:Ljava/lang/CharSequence;
        //   771: iload           11
        //   773: iload           12
        //   775: aload           5
        //   777: iload           4
        //   779: fconst_2       
        //   780: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   783: iadd           
        //   784: invokestatic    android/text/StaticLayout$Builder.obtain:(Ljava/lang/CharSequence;IILandroid/text/TextPaint;I)Landroid/text/StaticLayout$Builder;
        //   787: iconst_1       
        //   788: invokevirtual   android/text/StaticLayout$Builder.setBreakStrategy:(I)Landroid/text/StaticLayout$Builder;
        //   791: iconst_0       
        //   792: invokevirtual   android/text/StaticLayout$Builder.setHyphenationFrequency:(I)Landroid/text/StaticLayout$Builder;
        //   795: getstatic       android/text/Layout$Alignment.ALIGN_NORMAL:Landroid/text/Layout$Alignment;
        //   798: invokevirtual   android/text/StaticLayout$Builder.setAlignment:(Landroid/text/Layout$Alignment;)Landroid/text/StaticLayout$Builder;
        //   801: invokevirtual   android/text/StaticLayout$Builder.build:()Landroid/text/StaticLayout;
        //   804: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textLayout:Landroid/text/StaticLayout;
        //   807: goto            856
        //   810: new             Landroid/text/StaticLayout;
        //   813: astore          13
        //   815: aload_0        
        //   816: getfield        org/telegram/messenger/MessageObject.messageText:Ljava/lang/CharSequence;
        //   819: astore          14
        //   821: getstatic       android/text/Layout$Alignment.ALIGN_NORMAL:Landroid/text/Layout$Alignment;
        //   824: astore          15
        //   826: aload_1        
        //   827: astore          16
        //   829: aload           13
        //   831: aload           14
        //   833: iload           11
        //   835: iload           12
        //   837: aload           5
        //   839: iload           4
        //   841: aload           15
        //   843: fconst_1       
        //   844: fconst_0       
        //   845: iconst_0       
        //   846: invokespecial   android/text/StaticLayout.<init>:(Ljava/lang/CharSequence;IILandroid/text/TextPaint;ILandroid/text/Layout$Alignment;FFZ)V
        //   849: aload           10
        //   851: aload           13
        //   853: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textLayout:Landroid/text/StaticLayout;
        //   856: aload_1        
        //   857: astore          16
        //   859: aload           10
        //   861: astore          14
        //   863: iload           7
        //   865: istore          12
        //   867: iload_2        
        //   868: istore          11
        //   870: aload           14
        //   872: aload           16
        //   874: iload           11
        //   876: invokevirtual   android/text/StaticLayout.getLineTop:(I)I
        //   879: i2f            
        //   880: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textYOffset:F
        //   883: iload           12
        //   885: ifeq            902
        //   888: aload           14
        //   890: aload           14
        //   892: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textYOffset:F
        //   895: fload           8
        //   897: fsub           
        //   898: f2i            
        //   899: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.height:I
        //   902: aload           14
        //   904: aload           14
        //   906: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.height:I
        //   909: aload           14
        //   911: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textLayout:Landroid/text/StaticLayout;
        //   914: aload           14
        //   916: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textLayout:Landroid/text/StaticLayout;
        //   919: invokevirtual   android/text/StaticLayout.getLineCount:()I
        //   922: iconst_1       
        //   923: isub           
        //   924: invokevirtual   android/text/StaticLayout.getLineBottom:(I)I
        //   927: invokestatic    java/lang/Math.max:(II)I
        //   930: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.height:I
        //   933: aload           14
        //   935: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textYOffset:F
        //   938: fstore          17
        //   940: iload           6
        //   942: istore          11
        //   944: fload           17
        //   946: fstore          8
        //   948: iload           11
        //   950: istore          6
        //   952: iload           12
        //   954: iload           11
        //   956: iconst_1       
        //   957: isub           
        //   958: if_icmpne       691
        //   961: iload           9
        //   963: aload           14
        //   965: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textLayout:Landroid/text/StaticLayout;
        //   968: invokevirtual   android/text/StaticLayout.getLineCount:()I
        //   971: invokestatic    java/lang/Math.max:(II)I
        //   974: istore          6
        //   976: aload_0        
        //   977: aload_0        
        //   978: getfield        org/telegram/messenger/MessageObject.textHeight:I
        //   981: aload           14
        //   983: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textYOffset:F
        //   986: aload           14
        //   988: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textLayout:Landroid/text/StaticLayout;
        //   991: invokevirtual   android/text/StaticLayout.getHeight:()I
        //   994: i2f            
        //   995: fadd           
        //   996: f2i            
        //   997: invokestatic    java/lang/Math.max:(II)I
        //  1000: putfield        org/telegram/messenger/MessageObject.textHeight:I
        //  1003: fload           17
        //  1005: fstore          8
        //  1007: iload           11
        //  1009: istore          9
        //  1011: goto            1029
        //  1014: astore          16
        //  1016: aload           16
        //  1018: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1021: iload           11
        //  1023: istore          9
        //  1025: fload           17
        //  1027: fstore          8
        //  1029: aload           10
        //  1031: astore          16
        //  1033: iload           7
        //  1035: istore          18
        //  1037: iload_2        
        //  1038: istore          11
        //  1040: aload_0        
        //  1041: getfield        org/telegram/messenger/MessageObject.textLayoutBlocks:Ljava/util/ArrayList;
        //  1044: aload           16
        //  1046: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1049: pop            
        //  1050: aload           16
        //  1052: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textLayout:Landroid/text/StaticLayout;
        //  1055: iload           6
        //  1057: iconst_1       
        //  1058: isub           
        //  1059: invokevirtual   android/text/StaticLayout.getLineLeft:(I)F
        //  1062: fstore          19
        //  1064: iload           18
        //  1066: ifne            1098
        //  1069: fload           19
        //  1071: fstore          17
        //  1073: fload           19
        //  1075: fconst_0       
        //  1076: fcmpl          
        //  1077: iflt            1125
        //  1080: aload_0        
        //  1081: fload           19
        //  1083: putfield        org/telegram/messenger/MessageObject.textXOffset:F
        //  1086: fload           19
        //  1088: fstore          17
        //  1090: goto            1125
        //  1093: astore          10
        //  1095: goto            1107
        //  1098: fload           19
        //  1100: fstore          17
        //  1102: goto            1125
        //  1105: astore          10
        //  1107: iload           18
        //  1109: ifne            1117
        //  1112: aload_0        
        //  1113: fconst_0       
        //  1114: putfield        org/telegram/messenger/MessageObject.textXOffset:F
        //  1117: aload           10
        //  1119: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1122: fconst_0       
        //  1123: fstore          17
        //  1125: aload           16
        //  1127: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textLayout:Landroid/text/StaticLayout;
        //  1130: iload           6
        //  1132: iconst_1       
        //  1133: isub           
        //  1134: invokevirtual   android/text/StaticLayout.getLineWidth:(I)F
        //  1137: fstore          19
        //  1139: goto            1152
        //  1142: astore          10
        //  1144: aload           10
        //  1146: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1149: fconst_0       
        //  1150: fstore          19
        //  1152: fload           19
        //  1154: f2d            
        //  1155: invokestatic    java/lang/Math.ceil:(D)D
        //  1158: d2i            
        //  1159: istore          12
        //  1161: iload           12
        //  1163: istore_2       
        //  1164: iload           12
        //  1166: iload           4
        //  1168: bipush          80
        //  1170: iadd           
        //  1171: if_icmple       1177
        //  1174: iload           4
        //  1176: istore_2       
        //  1177: iload           9
        //  1179: iconst_1       
        //  1180: isub           
        //  1181: istore          20
        //  1183: iload           18
        //  1185: iload           20
        //  1187: if_icmpne       1195
        //  1190: aload_0        
        //  1191: iload_2        
        //  1192: putfield        org/telegram/messenger/MessageObject.lastLineWidth:I
        //  1195: fload           19
        //  1197: fload           17
        //  1199: fadd           
        //  1200: f2d            
        //  1201: invokestatic    java/lang/Math.ceil:(D)D
        //  1204: d2i            
        //  1205: istore          21
        //  1207: iload           6
        //  1209: iconst_1       
        //  1210: if_icmple       1533
        //  1213: iload           21
        //  1215: istore          22
        //  1217: iload_2        
        //  1218: istore          12
        //  1220: fconst_0       
        //  1221: fstore          19
        //  1223: iconst_0       
        //  1224: istore          23
        //  1226: iconst_0       
        //  1227: istore_2       
        //  1228: fconst_0       
        //  1229: fstore          17
        //  1231: iload           23
        //  1233: iload           6
        //  1235: if_icmpge       1468
        //  1238: aload           16
        //  1240: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textLayout:Landroid/text/StaticLayout;
        //  1243: iload           23
        //  1245: invokevirtual   android/text/StaticLayout.getLineWidth:(I)F
        //  1248: fstore          24
        //  1250: goto            1263
        //  1253: astore          10
        //  1255: aload           10
        //  1257: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1260: fconst_0       
        //  1261: fstore          24
        //  1263: fload           24
        //  1265: fstore          25
        //  1267: fload           24
        //  1269: iload           4
        //  1271: bipush          20
        //  1273: iadd           
        //  1274: i2f            
        //  1275: fcmpl          
        //  1276: ifle            1284
        //  1279: iload           4
        //  1281: i2f            
        //  1282: fstore          25
        //  1284: aload           16
        //  1286: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textLayout:Landroid/text/StaticLayout;
        //  1289: iload           23
        //  1291: invokevirtual   android/text/StaticLayout.getLineLeft:(I)F
        //  1294: fstore          24
        //  1296: goto            1309
        //  1299: astore          10
        //  1301: aload           10
        //  1303: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1306: fconst_0       
        //  1307: fstore          24
        //  1309: fload           24
        //  1311: fconst_0       
        //  1312: fcmpl          
        //  1313: ifle            1351
        //  1316: aload_0        
        //  1317: aload_0        
        //  1318: getfield        org/telegram/messenger/MessageObject.textXOffset:F
        //  1321: fload           24
        //  1323: invokestatic    java/lang/Math.min:(FF)F
        //  1326: putfield        org/telegram/messenger/MessageObject.textXOffset:F
        //  1329: aload           16
        //  1331: aload           16
        //  1333: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.directionFlags:B
        //  1336: iconst_1       
        //  1337: ior            
        //  1338: i2b            
        //  1339: i2b            
        //  1340: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.directionFlags:B
        //  1343: aload_0        
        //  1344: iconst_1       
        //  1345: putfield        org/telegram/messenger/MessageObject.hasRtl:Z
        //  1348: goto            1365
        //  1351: aload           16
        //  1353: aload           16
        //  1355: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.directionFlags:B
        //  1358: iconst_2       
        //  1359: ior            
        //  1360: i2b            
        //  1361: i2b            
        //  1362: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.directionFlags:B
        //  1365: iload_2        
        //  1366: istore          26
        //  1368: iload_2        
        //  1369: ifne            1406
        //  1372: iload_2        
        //  1373: istore          26
        //  1375: fload           24
        //  1377: fconst_0       
        //  1378: fcmpl          
        //  1379: ifne            1406
        //  1382: aload           16
        //  1384: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.textLayout:Landroid/text/StaticLayout;
        //  1387: iload           23
        //  1389: invokevirtual   android/text/StaticLayout.getParagraphDirection:(I)I
        //  1392: istore          27
        //  1394: iload_2        
        //  1395: istore          26
        //  1397: iload           27
        //  1399: iconst_1       
        //  1400: if_icmpne       1406
        //  1403: iconst_1       
        //  1404: istore          26
        //  1406: fload           19
        //  1408: fload           25
        //  1410: invokestatic    java/lang/Math.max:(FF)F
        //  1413: fstore          19
        //  1415: fload           24
        //  1417: fload           25
        //  1419: fadd           
        //  1420: fstore          24
        //  1422: fload           17
        //  1424: fload           24
        //  1426: invokestatic    java/lang/Math.max:(FF)F
        //  1429: fstore          17
        //  1431: iload           12
        //  1433: fload           25
        //  1435: f2d            
        //  1436: invokestatic    java/lang/Math.ceil:(D)D
        //  1439: d2i            
        //  1440: invokestatic    java/lang/Math.max:(II)I
        //  1443: istore          12
        //  1445: iload           22
        //  1447: fload           24
        //  1449: f2d            
        //  1450: invokestatic    java/lang/Math.ceil:(D)D
        //  1453: d2i            
        //  1454: invokestatic    java/lang/Math.max:(II)I
        //  1457: istore          22
        //  1459: iinc            23, 1
        //  1462: iload           26
        //  1464: istore_2       
        //  1465: goto            1231
        //  1468: iload_2        
        //  1469: ifeq            1496
        //  1472: fload           17
        //  1474: fstore          19
        //  1476: iload           18
        //  1478: iload           20
        //  1480: if_icmpne       1509
        //  1483: aload_0        
        //  1484: iload           21
        //  1486: putfield        org/telegram/messenger/MessageObject.lastLineWidth:I
        //  1489: fload           17
        //  1491: fstore          19
        //  1493: goto            1509
        //  1496: iload           18
        //  1498: iload           20
        //  1500: if_icmpne       1509
        //  1503: aload_0        
        //  1504: iload           12
        //  1506: putfield        org/telegram/messenger/MessageObject.lastLineWidth:I
        //  1509: aload_0        
        //  1510: aload_0        
        //  1511: getfield        org/telegram/messenger/MessageObject.textWidth:I
        //  1514: fload           19
        //  1516: f2d            
        //  1517: invokestatic    java/lang/Math.ceil:(D)D
        //  1520: d2i            
        //  1521: invokestatic    java/lang/Math.max:(II)I
        //  1524: putfield        org/telegram/messenger/MessageObject.textWidth:I
        //  1527: iload           4
        //  1529: istore_2       
        //  1530: goto            1654
        //  1533: fload           17
        //  1535: fconst_0       
        //  1536: fcmpl          
        //  1537: ifle            1613
        //  1540: aload_0        
        //  1541: aload_0        
        //  1542: getfield        org/telegram/messenger/MessageObject.textXOffset:F
        //  1545: fload           17
        //  1547: invokestatic    java/lang/Math.min:(FF)F
        //  1550: putfield        org/telegram/messenger/MessageObject.textXOffset:F
        //  1553: aload_0        
        //  1554: getfield        org/telegram/messenger/MessageObject.textXOffset:F
        //  1557: fconst_0       
        //  1558: fcmpl          
        //  1559: ifne            1572
        //  1562: iload_2        
        //  1563: i2f            
        //  1564: fload           17
        //  1566: fadd           
        //  1567: f2i            
        //  1568: istore_2       
        //  1569: goto            1572
        //  1572: iload           9
        //  1574: iconst_1       
        //  1575: if_icmpeq       1584
        //  1578: iconst_1       
        //  1579: istore          28
        //  1581: goto            1587
        //  1584: iconst_0       
        //  1585: istore          28
        //  1587: aload_0        
        //  1588: iload           28
        //  1590: putfield        org/telegram/messenger/MessageObject.hasRtl:Z
        //  1593: aload           16
        //  1595: aload           16
        //  1597: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.directionFlags:B
        //  1600: iconst_1       
        //  1601: ior            
        //  1602: i2b            
        //  1603: i2b            
        //  1604: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.directionFlags:B
        //  1607: iload_2        
        //  1608: istore          12
        //  1610: goto            1630
        //  1613: aload           16
        //  1615: aload           16
        //  1617: getfield        org/telegram/messenger/MessageObject$TextLayoutBlock.directionFlags:B
        //  1620: iconst_2       
        //  1621: ior            
        //  1622: i2b            
        //  1623: i2b            
        //  1624: putfield        org/telegram/messenger/MessageObject$TextLayoutBlock.directionFlags:B
        //  1627: iload_2        
        //  1628: istore          12
        //  1630: aload_0        
        //  1631: getfield        org/telegram/messenger/MessageObject.textWidth:I
        //  1634: istore          23
        //  1636: iload           4
        //  1638: istore_2       
        //  1639: aload_0        
        //  1640: iload           23
        //  1642: iload_2        
        //  1643: iload           12
        //  1645: invokestatic    java/lang/Math.min:(II)I
        //  1648: invokestatic    java/lang/Math.max:(II)I
        //  1651: putfield        org/telegram/messenger/MessageObject.textWidth:I
        //  1654: iload           11
        //  1656: iload           6
        //  1658: iadd           
        //  1659: istore          6
        //  1661: goto            1704
        //  1664: astore          10
        //  1666: iload           11
        //  1668: istore_2       
        //  1669: aload           16
        //  1671: astore_1       
        //  1672: goto            1680
        //  1675: astore          10
        //  1677: aload           16
        //  1679: astore_1       
        //  1680: iload           6
        //  1682: istore          9
        //  1684: goto            1693
        //  1687: astore          10
        //  1689: iload           6
        //  1691: istore          9
        //  1693: aload           10
        //  1695: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1698: iload_2        
        //  1699: istore          6
        //  1701: iload           4
        //  1703: istore_2       
        //  1704: iinc            7, 1
        //  1707: iload_2        
        //  1708: istore          4
        //  1710: iload           6
        //  1712: istore_2       
        //  1713: iload           9
        //  1715: istore          6
        //  1717: goto            475
        //  1720: return         
        //  1721: astore_1       
        //  1722: aload_1        
        //  1723: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1726: return         
        //  1727: astore          10
        //  1729: goto            1403
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  286    298    301    306    Ljava/lang/Throwable;
        //  349    396    1721   1726   Ljava/lang/Exception;
        //  399    421    1721   1726   Ljava/lang/Exception;
        //  757    807    1687   1693   Ljava/lang/Exception;
        //  810    826    1687   1693   Ljava/lang/Exception;
        //  829    856    1675   1680   Ljava/lang/Exception;
        //  870    883    1664   1675   Ljava/lang/Exception;
        //  888    902    1664   1675   Ljava/lang/Exception;
        //  902    940    1664   1675   Ljava/lang/Exception;
        //  976    1003   1014   1029   Ljava/lang/Exception;
        //  1050   1064   1105   1107   Ljava/lang/Exception;
        //  1080   1086   1093   1098   Ljava/lang/Exception;
        //  1125   1139   1142   1152   Ljava/lang/Exception;
        //  1238   1250   1253   1263   Ljava/lang/Exception;
        //  1284   1296   1299   1309   Ljava/lang/Exception;
        //  1382   1394   1727   1732   Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_1403:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
    
    public void generateLinkDescription() {
        if (this.linkDescription != null) {
            return;
        }
        final TLRPC.MessageMedia media = this.messageOwner.media;
        int n = 0;
        Label_0215: {
            Label_0213: {
                if (media instanceof TLRPC.TL_messageMediaWebPage) {
                    final TLRPC.WebPage webpage = media.webpage;
                    if (webpage instanceof TLRPC.TL_webPage && webpage.description != null) {
                        this.linkDescription = (CharSequence)Spannable$Factory.getInstance().newSpannable((CharSequence)this.messageOwner.media.webpage.description);
                        final String site_name = this.messageOwner.media.webpage.site_name;
                        String lowerCase;
                        if ((lowerCase = site_name) != null) {
                            lowerCase = site_name.toLowerCase();
                        }
                        if ("instagram".equals(lowerCase)) {
                            n = 1;
                            break Label_0215;
                        }
                        if ("twitter".equals(lowerCase)) {
                            n = 2;
                            break Label_0215;
                        }
                        break Label_0213;
                    }
                }
                final TLRPC.MessageMedia media2 = this.messageOwner.media;
                if (media2 instanceof TLRPC.TL_messageMediaGame && media2.game.description != null) {
                    this.linkDescription = (CharSequence)Spannable$Factory.getInstance().newSpannable((CharSequence)this.messageOwner.media.game.description);
                }
                else {
                    final TLRPC.MessageMedia media3 = this.messageOwner.media;
                    if (media3 instanceof TLRPC.TL_messageMediaInvoice && media3.description != null) {
                        this.linkDescription = (CharSequence)Spannable$Factory.getInstance().newSpannable((CharSequence)this.messageOwner.media.description);
                    }
                }
            }
            n = 0;
        }
        if (!TextUtils.isEmpty(this.linkDescription)) {
            if (containsUrls(this.linkDescription)) {
                try {
                    Linkify.addLinks((Spannable)this.linkDescription, 1);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            this.linkDescription = Emoji.replaceEmoji(this.linkDescription, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            if (n != 0) {
                final CharSequence linkDescription = this.linkDescription;
                if (!(linkDescription instanceof Spannable)) {
                    this.linkDescription = (CharSequence)new SpannableStringBuilder(linkDescription);
                }
                addUsernamesAndHashtags(this.isOutOwner(), this.linkDescription, false, n);
            }
        }
    }
    
    public void generatePaymentSentMessageText(final TLRPC.User user) {
        TLRPC.User user2 = user;
        if (user == null) {
            user2 = MessagesController.getInstance(this.currentAccount).getUser((int)this.getDialogId());
        }
        String firstName;
        if (user2 != null) {
            firstName = UserObject.getFirstName(user2);
        }
        else {
            firstName = "";
        }
        final MessageObject replyMessageObject = this.replyMessageObject;
        if (replyMessageObject != null && replyMessageObject.messageOwner.media instanceof TLRPC.TL_messageMediaInvoice) {
            final LocaleController instance = LocaleController.getInstance();
            final TLRPC.MessageAction action = this.messageOwner.action;
            this.messageText = LocaleController.formatString("PaymentSuccessfullyPaid", 2131560404, instance.formatCurrencyString(action.total_amount, action.currency), firstName, this.replyMessageObject.messageOwner.media.title);
        }
        else {
            final LocaleController instance2 = LocaleController.getInstance();
            final TLRPC.MessageAction action2 = this.messageOwner.action;
            this.messageText = LocaleController.formatString("PaymentSuccessfullyPaidNoItem", 2131560405, instance2.formatCurrencyString(action2.total_amount, action2.currency), firstName);
        }
    }
    
    public void generatePinMessageText(TLRPC.User user, final TLRPC.Chat chat) {
        TLRPC.User user2 = user;
        TLObject chat2 = chat;
        if (user == null) {
            user2 = user;
            if ((chat2 = chat) == null) {
                if (this.messageOwner.from_id > 0) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.from_id);
                }
                user2 = user;
                chat2 = chat;
                if (user == null) {
                    chat2 = MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.to_id.channel_id);
                    user2 = user;
                }
            }
        }
        final MessageObject replyMessageObject = this.replyMessageObject;
        if (replyMessageObject != null) {
            final TLRPC.Message messageOwner = replyMessageObject.messageOwner;
            if (!(messageOwner instanceof TLRPC.TL_messageEmpty)) {
                if (!(messageOwner.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                    if (replyMessageObject.isMusic()) {
                        final String string = LocaleController.getString("ActionPinnedMusic", 2131558532);
                        if (user2 != null) {
                            chat2 = user2;
                        }
                        this.messageText = this.replaceWithLink(string, "un1", chat2);
                        return;
                    }
                    if (this.replyMessageObject.isVideo()) {
                        final String string2 = LocaleController.getString("ActionPinnedVideo", 2131558539);
                        if (user2 == null) {
                            user2 = (TLRPC.User)chat2;
                        }
                        this.messageText = this.replaceWithLink(string2, "un1", user2);
                        return;
                    }
                    if (this.replyMessageObject.isGif()) {
                        final String string3 = LocaleController.getString("ActionPinnedGif", 2131558531);
                        if (user2 == null) {
                            user2 = (TLRPC.User)chat2;
                        }
                        this.messageText = this.replaceWithLink(string3, "un1", user2);
                        return;
                    }
                    if (this.replyMessageObject.isVoice()) {
                        final String string4 = LocaleController.getString("ActionPinnedVoice", 2131558540);
                        if (user2 == null) {
                            user2 = (TLRPC.User)chat2;
                        }
                        this.messageText = this.replaceWithLink(string4, "un1", user2);
                        return;
                    }
                    if (this.replyMessageObject.isRoundVideo()) {
                        final String string5 = LocaleController.getString("ActionPinnedRound", 2131558536);
                        if (user2 != null) {
                            chat2 = user2;
                        }
                        this.messageText = this.replaceWithLink(string5, "un1", chat2);
                        return;
                    }
                    if (this.replyMessageObject.isSticker() || this.replyMessageObject.isAnimatedSticker()) {
                        final String string6 = LocaleController.getString("ActionPinnedSticker", 2131558537);
                        if (user2 != null) {
                            chat2 = user2;
                        }
                        this.messageText = this.replaceWithLink(string6, "un1", chat2);
                        return;
                    }
                    final MessageObject replyMessageObject2 = this.replyMessageObject;
                    final TLRPC.MessageMedia media = replyMessageObject2.messageOwner.media;
                    if (media instanceof TLRPC.TL_messageMediaDocument) {
                        final String string7 = LocaleController.getString("ActionPinnedFile", 2131558527);
                        if (user2 == null) {
                            user2 = (TLRPC.User)chat2;
                        }
                        this.messageText = this.replaceWithLink(string7, "un1", user2);
                        return;
                    }
                    if (media instanceof TLRPC.TL_messageMediaGeo) {
                        final String string8 = LocaleController.getString("ActionPinnedGeo", 2131558529);
                        if (user2 != null) {
                            chat2 = user2;
                        }
                        this.messageText = this.replaceWithLink(string8, "un1", chat2);
                        return;
                    }
                    if (media instanceof TLRPC.TL_messageMediaGeoLive) {
                        final String string9 = LocaleController.getString("ActionPinnedGeoLive", 2131558530);
                        if (user2 != null) {
                            chat2 = user2;
                        }
                        this.messageText = this.replaceWithLink(string9, "un1", chat2);
                        return;
                    }
                    if (media instanceof TLRPC.TL_messageMediaContact) {
                        final String string10 = LocaleController.getString("ActionPinnedContact", 2131558526);
                        if (user2 == null) {
                            user2 = (TLRPC.User)chat2;
                        }
                        this.messageText = this.replaceWithLink(string10, "un1", user2);
                        return;
                    }
                    if (media instanceof TLRPC.TL_messageMediaPoll) {
                        final String string11 = LocaleController.getString("ActionPinnedPoll", 2131558535);
                        if (user2 == null) {
                            user2 = (TLRPC.User)chat2;
                        }
                        this.messageText = this.replaceWithLink(string11, "un1", user2);
                        return;
                    }
                    if (media instanceof TLRPC.TL_messageMediaPhoto) {
                        final String string12 = LocaleController.getString("ActionPinnedPhoto", 2131558534);
                        if (user2 != null) {
                            chat2 = user2;
                        }
                        this.messageText = this.replaceWithLink(string12, "un1", chat2);
                        return;
                    }
                    if (media instanceof TLRPC.TL_messageMediaGame) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("\ud83c\udfae ");
                        sb.append(this.replyMessageObject.messageOwner.media.game.title);
                        final String formatString = LocaleController.formatString("ActionPinnedGame", 2131558528, sb.toString());
                        if (user2 != null) {
                            chat2 = user2;
                        }
                        this.messageText = this.replaceWithLink(formatString, "un1", chat2);
                        this.messageText = Emoji.replaceEmoji(this.messageText, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                        return;
                    }
                    final CharSequence messageText = replyMessageObject2.messageText;
                    if (messageText != null && messageText.length() > 0) {
                        CharSequence charSequence2;
                        final CharSequence charSequence = charSequence2 = this.replyMessageObject.messageText;
                        if (charSequence.length() > 20) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append((Object)charSequence.subSequence(0, 20));
                            sb2.append("...");
                            charSequence2 = sb2.toString();
                        }
                        final String formatString2 = LocaleController.formatString("ActionPinnedText", 2131558538, Emoji.replaceEmoji(charSequence2, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false));
                        if (user2 != null) {
                            chat2 = user2;
                        }
                        this.messageText = this.replaceWithLink(formatString2, "un1", chat2);
                        return;
                    }
                    final String string13 = LocaleController.getString("ActionPinnedNoText", 2131558533);
                    if (user2 != null) {
                        chat2 = user2;
                    }
                    this.messageText = this.replaceWithLink(string13, "un1", chat2);
                    return;
                }
            }
        }
        final String string14 = LocaleController.getString("ActionPinnedNoText", 2131558533);
        if (user2 == null) {
            user2 = (TLRPC.User)chat2;
        }
        this.messageText = this.replaceWithLink(string14, "un1", user2);
    }
    
    public void generateThumbs(final boolean b) {
        final TLRPC.Message messageOwner = this.messageOwner;
        final boolean b2 = messageOwner instanceof TLRPC.TL_messageService;
        final int n = 0;
        if (b2) {
            final TLRPC.MessageAction action = messageOwner.action;
            if (action instanceof TLRPC.TL_messageActionChatEditPhoto) {
                final TLRPC.Photo photo = action.photo;
                if (!b) {
                    this.photoThumbs = new ArrayList<TLRPC.PhotoSize>(photo.sizes);
                }
                else {
                    final ArrayList<TLRPC.PhotoSize> photoThumbs = this.photoThumbs;
                    if (photoThumbs != null && !photoThumbs.isEmpty()) {
                        for (int i = 0; i < this.photoThumbs.size(); ++i) {
                            final TLRPC.PhotoSize photoSize = this.photoThumbs.get(i);
                            for (int j = 0; j < photo.sizes.size(); ++j) {
                                final TLRPC.PhotoSize photoSize2 = photo.sizes.get(j);
                                if (!(photoSize2 instanceof TLRPC.TL_photoSizeEmpty)) {
                                    if (photoSize2.type.equals(photoSize.type)) {
                                        photoSize.location = photoSize2.location;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (photo.dc_id != 0) {
                    for (int size = this.photoThumbs.size(), k = n; k < size; ++k) {
                        final TLRPC.FileLocation location = this.photoThumbs.get(k).location;
                        location.dc_id = photo.dc_id;
                        location.file_reference = photo.file_reference;
                    }
                }
                this.photoThumbsObject = this.messageOwner.action.photo;
            }
        }
        else {
            final TLRPC.MessageMedia media = messageOwner.media;
            if (media != null && !(media instanceof TLRPC.TL_messageMediaEmpty)) {
                if (media instanceof TLRPC.TL_messageMediaPhoto) {
                    final TLRPC.Photo photo2 = media.photo;
                    Label_0488: {
                        if (b) {
                            final ArrayList<TLRPC.PhotoSize> photoThumbs2 = this.photoThumbs;
                            if (photoThumbs2 == null || photoThumbs2.size() == photo2.sizes.size()) {
                                final ArrayList<TLRPC.PhotoSize> photoThumbs3 = this.photoThumbs;
                                if (photoThumbs3 != null && !photoThumbs3.isEmpty()) {
                                    for (int l = 0; l < this.photoThumbs.size(); ++l) {
                                        final TLRPC.PhotoSize photoSize3 = this.photoThumbs.get(l);
                                        if (photoSize3 != null) {
                                            for (int index = 0; index < photo2.sizes.size(); ++index) {
                                                final TLRPC.PhotoSize photoSize4 = photo2.sizes.get(index);
                                                if (photoSize4 != null) {
                                                    if (!(photoSize4 instanceof TLRPC.TL_photoSizeEmpty)) {
                                                        if (photoSize4.type.equals(photoSize3.type)) {
                                                            photoSize3.location = photoSize4.location;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                break Label_0488;
                            }
                        }
                        this.photoThumbs = new ArrayList<TLRPC.PhotoSize>(photo2.sizes);
                    }
                    this.photoThumbsObject = this.messageOwner.media.photo;
                }
                else if (media instanceof TLRPC.TL_messageMediaDocument) {
                    final TLRPC.Document document = media.document;
                    if (isDocumentHasThumb(document)) {
                        Label_0592: {
                            if (b) {
                                final ArrayList<TLRPC.PhotoSize> photoThumbs4 = this.photoThumbs;
                                if (photoThumbs4 != null) {
                                    if (photoThumbs4 != null && !photoThumbs4.isEmpty()) {
                                        updatePhotoSizeLocations(this.photoThumbs, document.thumbs);
                                    }
                                    break Label_0592;
                                }
                            }
                            (this.photoThumbs = new ArrayList<TLRPC.PhotoSize>()).addAll(document.thumbs);
                        }
                        this.photoThumbsObject = document;
                    }
                }
                else if (media instanceof TLRPC.TL_messageMediaGame) {
                    final TLRPC.Document document2 = media.game.document;
                    if (document2 != null && isDocumentHasThumb(document2)) {
                        if (!b) {
                            (this.photoThumbs = new ArrayList<TLRPC.PhotoSize>()).addAll(document2.thumbs);
                        }
                        else {
                            final ArrayList<TLRPC.PhotoSize> photoThumbs5 = this.photoThumbs;
                            if (photoThumbs5 != null && !photoThumbs5.isEmpty()) {
                                updatePhotoSizeLocations(this.photoThumbs, document2.thumbs);
                            }
                        }
                        this.photoThumbsObject = document2;
                    }
                    final TLRPC.Photo photo3 = this.messageOwner.media.game.photo;
                    if (photo3 != null) {
                        Label_0769: {
                            if (b) {
                                final ArrayList<TLRPC.PhotoSize> photoThumbs6 = this.photoThumbs2;
                                if (photoThumbs6 != null) {
                                    if (!photoThumbs6.isEmpty()) {
                                        updatePhotoSizeLocations(this.photoThumbs2, photo3.sizes);
                                    }
                                    break Label_0769;
                                }
                            }
                            this.photoThumbs2 = new ArrayList<TLRPC.PhotoSize>(photo3.sizes);
                        }
                        this.photoThumbsObject2 = photo3;
                    }
                    if (this.photoThumbs == null) {
                        final ArrayList<TLRPC.PhotoSize> photoThumbs7 = this.photoThumbs2;
                        if (photoThumbs7 != null) {
                            this.photoThumbs = photoThumbs7;
                            this.photoThumbs2 = null;
                            this.photoThumbsObject = this.photoThumbsObject2;
                            this.photoThumbsObject2 = null;
                        }
                    }
                }
                else if (media instanceof TLRPC.TL_messageMediaWebPage) {
                    final TLRPC.WebPage webpage = media.webpage;
                    final TLRPC.Photo photo4 = webpage.photo;
                    final TLRPC.Document document3 = webpage.document;
                    if (photo4 != null) {
                        Label_0902: {
                            if (b) {
                                final ArrayList<TLRPC.PhotoSize> photoThumbs8 = this.photoThumbs;
                                if (photoThumbs8 != null) {
                                    if (!photoThumbs8.isEmpty()) {
                                        updatePhotoSizeLocations(this.photoThumbs, photo4.sizes);
                                    }
                                    break Label_0902;
                                }
                            }
                            this.photoThumbs = new ArrayList<TLRPC.PhotoSize>(photo4.sizes);
                        }
                        this.photoThumbsObject = photo4;
                    }
                    else if (document3 != null && isDocumentHasThumb(document3)) {
                        if (!b) {
                            (this.photoThumbs = new ArrayList<TLRPC.PhotoSize>()).addAll(document3.thumbs);
                        }
                        else {
                            final ArrayList<TLRPC.PhotoSize> photoThumbs9 = this.photoThumbs;
                            if (photoThumbs9 != null && !photoThumbs9.isEmpty()) {
                                updatePhotoSizeLocations(this.photoThumbs, document3.thumbs);
                            }
                        }
                        this.photoThumbsObject = document3;
                    }
                }
            }
        }
    }
    
    public int getApproximateHeight() {
        final int type = this.type;
        int w = 0;
        final int n = 0;
        if (type == 0) {
            final int textHeight = this.textHeight;
            final TLRPC.MessageMedia media = this.messageOwner.media;
            int dp = n;
            if (media instanceof TLRPC.TL_messageMediaWebPage) {
                dp = n;
                if (media.webpage instanceof TLRPC.TL_webPage) {
                    dp = AndroidUtilities.dp(100.0f);
                }
            }
            int n2 = textHeight + dp;
            if (this.isReply()) {
                n2 += AndroidUtilities.dp(42.0f);
            }
            return n2;
        }
        if (type == 2) {
            return AndroidUtilities.dp(72.0f);
        }
        if (type == 12) {
            return AndroidUtilities.dp(71.0f);
        }
        if (type == 9) {
            return AndroidUtilities.dp(100.0f);
        }
        if (type == 4) {
            return AndroidUtilities.dp(114.0f);
        }
        if (type == 14) {
            return AndroidUtilities.dp(82.0f);
        }
        if (type == 10) {
            return AndroidUtilities.dp(30.0f);
        }
        if (type == 11) {
            return AndroidUtilities.dp(50.0f);
        }
        if (type == 5) {
            return AndroidUtilities.roundMessageSize;
        }
        if (type != 13 && type != 15) {
            int n3;
            if (AndroidUtilities.isTablet()) {
                n3 = AndroidUtilities.getMinTabletSide();
            }
            else {
                final Point displaySize = AndroidUtilities.displaySize;
                n3 = Math.min(displaySize.x, displaySize.y);
            }
            final int n4 = (int)(n3 * 0.7f);
            final int n5 = AndroidUtilities.dp(100.0f) + n4;
            int photoSize = n4;
            if (n4 > AndroidUtilities.getPhotoSize()) {
                photoSize = AndroidUtilities.getPhotoSize();
            }
            int n6;
            if ((n6 = n5) > AndroidUtilities.getPhotoSize()) {
                n6 = AndroidUtilities.getPhotoSize();
            }
            final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize());
            int n7 = n6;
            if (closestPhotoSizeWithSize != null) {
                int dp2;
                if ((dp2 = (int)(closestPhotoSizeWithSize.h / (closestPhotoSizeWithSize.w / (float)photoSize))) == 0) {
                    dp2 = AndroidUtilities.dp(100.0f);
                }
                if (dp2 <= n6) {
                    if (dp2 < AndroidUtilities.dp(120.0f)) {
                        n6 = AndroidUtilities.dp(120.0f);
                    }
                    else {
                        n6 = dp2;
                    }
                }
                n7 = n6;
                if (this.needDrawBluredPreview()) {
                    int n8;
                    if (AndroidUtilities.isTablet()) {
                        n8 = AndroidUtilities.getMinTabletSide();
                    }
                    else {
                        final Point displaySize2 = AndroidUtilities.displaySize;
                        n8 = Math.min(displaySize2.x, displaySize2.y);
                    }
                    n7 = (int)(n8 * 0.5f);
                }
            }
            return n7 + AndroidUtilities.dp(14.0f);
        }
        final float n9 = AndroidUtilities.displaySize.y * 0.4f;
        int n10;
        if (AndroidUtilities.isTablet()) {
            n10 = AndroidUtilities.getMinTabletSide();
        }
        else {
            n10 = AndroidUtilities.displaySize.x;
        }
        final float n11 = n10 * 0.5f;
        while (true) {
            for (final TLRPC.DocumentAttribute documentAttribute : this.messageOwner.media.document.attributes) {
                if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                    w = documentAttribute.w;
                    int h = documentAttribute.h;
                    int n12 = w;
                    if (w == 0) {
                        h = (int)n9;
                        n12 = AndroidUtilities.dp(100.0f) + h;
                    }
                    final float n13 = (float)h;
                    int n14 = n12;
                    if (n13 > n9) {
                        n14 = (int)(n12 * (n9 / n13));
                        h = (int)n9;
                    }
                    final float n15 = (float)n14;
                    int n16 = h;
                    if (n15 > n11) {
                        n16 = (int)(h * (n11 / n15));
                    }
                    return n16 + AndroidUtilities.dp(14.0f);
                }
            }
            int h = 0;
            continue;
        }
    }
    
    public String getArtworkUrl(final boolean b) {
        final TLRPC.Document document = this.getDocument();
        Label_0264: {
            if (document == null) {
                break Label_0264;
            }
            final int size = document.attributes.size();
            int index = 0;
        Label_0258_Outer:
            while (true) {
                if (index >= size) {
                    break Label_0264;
                }
                final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(index);
                while (true) {
                    if (!(documentAttribute instanceof TLRPC.TL_documentAttributeAudio)) {
                        break Label_0258;
                    }
                    if (documentAttribute.voice) {
                        return null;
                    }
                    final String performer = documentAttribute.performer;
                    final String title = documentAttribute.title;
                    String replace = performer;
                    if (!TextUtils.isEmpty((CharSequence)performer)) {
                        replace = performer;
                        int n = 0;
                        while (true) {
                            final String[] excludeWords = MessageObject.excludeWords;
                            if (n >= excludeWords.length) {
                                break;
                            }
                            replace = replace.replace(excludeWords[n], " ");
                            ++n;
                        }
                    }
                    if (TextUtils.isEmpty((CharSequence)replace) && TextUtils.isEmpty((CharSequence)title)) {
                        return null;
                    }
                    try {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("athumb://itunes.apple.com/search?term=");
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(replace);
                        sb2.append(" - ");
                        sb2.append(title);
                        sb.append(URLEncoder.encode(sb2.toString(), "UTF-8"));
                        sb.append("&entity=song&limit=4");
                        String str;
                        if (b) {
                            str = "&s=1";
                        }
                        else {
                            str = "";
                        }
                        sb.append(str);
                        return sb.toString();
                        ++index;
                        continue Label_0258_Outer;
                        return null;
                    }
                    catch (Exception ex) {
                        continue;
                    }
                    break;
                }
                break;
            }
        }
    }
    
    public int getChannelId() {
        final TLRPC.Peer to_id = this.messageOwner.to_id;
        if (to_id != null) {
            return to_id.channel_id;
        }
        return 0;
    }
    
    public long getDialogId() {
        return getDialogId(this.messageOwner);
    }
    
    public TLRPC.Document getDocument() {
        return getDocument(this.messageOwner);
    }
    
    public String getDocumentName() {
        final TLRPC.MessageMedia media = this.messageOwner.media;
        if (media instanceof TLRPC.TL_messageMediaDocument) {
            return FileLoader.getDocumentFileName(media.document);
        }
        if (media instanceof TLRPC.TL_messageMediaWebPage) {
            return FileLoader.getDocumentFileName(media.webpage.document);
        }
        return "";
    }
    
    public int getDuration() {
        final TLRPC.Document document = this.getDocument();
        int i = 0;
        if (document == null) {
            return 0;
        }
        final int audioPlayerDuration = this.audioPlayerDuration;
        if (audioPlayerDuration > 0) {
            return audioPlayerDuration;
        }
        while (i < document.attributes.size()) {
            final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                return documentAttribute.duration;
            }
            if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                return documentAttribute.duration;
            }
            ++i;
        }
        return this.audioPlayerDuration;
    }
    
    public String getExtension() {
        final String fileName = this.getFileName();
        final int lastIndex = fileName.lastIndexOf(46);
        String substring;
        if (lastIndex != -1) {
            substring = fileName.substring(lastIndex + 1);
        }
        else {
            substring = null;
        }
        String mime_type = null;
        Label_0057: {
            if (substring != null) {
                mime_type = substring;
                if (substring.length() != 0) {
                    break Label_0057;
                }
            }
            mime_type = this.messageOwner.media.document.mime_type;
        }
        String s;
        if ((s = mime_type) == null) {
            s = "";
        }
        return s.toUpperCase();
    }
    
    public String getFileName() {
        final TLRPC.MessageMedia media = this.messageOwner.media;
        if (media instanceof TLRPC.TL_messageMediaDocument) {
            return FileLoader.getAttachFileName(media.document);
        }
        if (media instanceof TLRPC.TL_messageMediaPhoto) {
            final ArrayList<TLRPC.PhotoSize> sizes = media.photo.sizes;
            if (sizes.size() > 0) {
                final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                if (closestPhotoSizeWithSize != null) {
                    return FileLoader.getAttachFileName(closestPhotoSizeWithSize);
                }
            }
        }
        else if (media instanceof TLRPC.TL_messageMediaWebPage) {
            return FileLoader.getAttachFileName(media.webpage.document);
        }
        return "";
    }
    
    public int getFileType() {
        if (this.isVideo()) {
            return 2;
        }
        if (this.isVoice()) {
            return 1;
        }
        final TLRPC.MessageMedia media = this.messageOwner.media;
        if (media instanceof TLRPC.TL_messageMediaDocument) {
            return 3;
        }
        if (media instanceof TLRPC.TL_messageMediaPhoto) {
            return 0;
        }
        return 4;
    }
    
    public String getForwardedName() {
        final TLRPC.MessageFwdHeader fwd_from = this.messageOwner.fwd_from;
        if (fwd_from != null) {
            if (fwd_from.channel_id != 0) {
                final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.fwd_from.channel_id);
                if (chat != null) {
                    return chat.title;
                }
            }
            else if (fwd_from.from_id != 0) {
                final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.fwd_from.from_id);
                if (user != null) {
                    return UserObject.getUserName(user);
                }
            }
            else {
                final String from_name = fwd_from.from_name;
                if (from_name != null) {
                    return from_name;
                }
            }
        }
        return null;
    }
    
    public int getFromId() {
        final TLRPC.MessageFwdHeader fwd_from = this.messageOwner.fwd_from;
        if (fwd_from != null) {
            final TLRPC.Peer saved_from_peer = fwd_from.saved_from_peer;
            if (saved_from_peer != null) {
                final int user_id = saved_from_peer.user_id;
                if (user_id != 0) {
                    final int from_id = fwd_from.from_id;
                    if (from_id != 0) {
                        return from_id;
                    }
                    return user_id;
                }
                else if (saved_from_peer.channel_id != 0) {
                    if (this.isSavedFromMegagroup()) {
                        final int from_id2 = this.messageOwner.fwd_from.from_id;
                        if (from_id2 != 0) {
                            return from_id2;
                        }
                    }
                    final TLRPC.MessageFwdHeader fwd_from2 = this.messageOwner.fwd_from;
                    final int channel_id = fwd_from2.channel_id;
                    if (channel_id != 0) {
                        return -channel_id;
                    }
                    return -fwd_from2.saved_from_peer.channel_id;
                }
                else {
                    final int chat_id = saved_from_peer.chat_id;
                    if (chat_id == 0) {
                        return 0;
                    }
                    final int from_id3 = fwd_from.from_id;
                    if (from_id3 != 0) {
                        return from_id3;
                    }
                    final int channel_id2 = fwd_from.channel_id;
                    if (channel_id2 != 0) {
                        return -channel_id2;
                    }
                    return -chat_id;
                }
            }
        }
        final TLRPC.Message messageOwner = this.messageOwner;
        final int from_id4 = messageOwner.from_id;
        if (from_id4 != 0) {
            return from_id4;
        }
        if (messageOwner.post) {
            return messageOwner.to_id.channel_id;
        }
        return 0;
    }
    
    public long getGroupId() {
        long n = this.localGroupId;
        if (n == 0L) {
            n = this.getGroupIdForUse();
        }
        return n;
    }
    
    public long getGroupIdForUse() {
        long n = this.localSentGroupId;
        if (n == 0L) {
            n = this.messageOwner.grouped_id;
        }
        return n;
    }
    
    public int getId() {
        return this.messageOwner.id;
    }
    
    public long getIdWithChannel() {
        final TLRPC.Message messageOwner = this.messageOwner;
        final long n = messageOwner.id;
        final TLRPC.Peer to_id = messageOwner.to_id;
        long n2 = n;
        if (to_id != null) {
            final int channel_id = to_id.channel_id;
            n2 = n;
            if (channel_id != 0) {
                n2 = (n | (long)channel_id << 32);
            }
        }
        return n2;
    }
    
    public TLRPC.InputStickerSet getInputStickerSet() {
        return getInputStickerSet(this.messageOwner);
    }
    
    public int getMaxMessageTextWidth() {
        if (AndroidUtilities.isTablet() && this.eventId != 0L) {
            this.generatedWithMinSize = AndroidUtilities.dp(530.0f);
        }
        else {
            int generatedWithMinSize;
            if (AndroidUtilities.isTablet()) {
                generatedWithMinSize = AndroidUtilities.getMinTabletSide();
            }
            else {
                generatedWithMinSize = AndroidUtilities.displaySize.x;
            }
            this.generatedWithMinSize = generatedWithMinSize;
        }
        this.generatedWithDensity = AndroidUtilities.density;
        final TLRPC.MessageMedia media = this.messageOwner.media;
        final boolean b = media instanceof TLRPC.TL_messageMediaWebPage;
        int n2;
        int n = n2 = 0;
        while (true) {
            if (!b) {
                break Label_0173;
            }
            final TLRPC.WebPage webpage = media.webpage;
            n2 = n;
            if (webpage == null) {
                break Label_0173;
            }
            n2 = n;
            if (!"telegram_background".equals(webpage.type)) {
                break Label_0173;
            }
            try {
                final Uri parse = Uri.parse(this.messageOwner.media.webpage.url);
                if (parse.getQueryParameter("bg_color") != null) {
                    n2 = AndroidUtilities.dp(220.0f);
                }
                else {
                    n2 = n;
                    if (parse.getLastPathSegment().length() == 6) {
                        n2 = AndroidUtilities.dp(200.0f);
                    }
                }
                if ((n = n2) == 0) {
                    final int generatedWithMinSize2 = this.generatedWithMinSize;
                    float n3;
                    if (this.needDrawAvatarInternal() && !this.isOutOwner()) {
                        n3 = 132.0f;
                    }
                    else {
                        n3 = 80.0f;
                    }
                    int n4;
                    n = (n4 = generatedWithMinSize2 - AndroidUtilities.dp(n3));
                    if (this.needDrawShareButton()) {
                        n4 = n;
                        if (!this.isOutOwner()) {
                            n4 = n - AndroidUtilities.dp(10.0f);
                        }
                    }
                    if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                        n = n4 - AndroidUtilities.dp(10.0f);
                    }
                    else {
                        n = n4;
                    }
                }
                return n;
            }
            catch (Exception ex) {
                n2 = n;
                continue;
            }
            break;
        }
    }
    
    public int getMediaExistanceFlags() {
        int attachPathExists;
        final boolean b = (attachPathExists = (this.attachPathExists ? 1 : 0)) != 0;
        if (this.mediaExists) {
            attachPathExists = ((b ? 1 : 0) | 0x2);
        }
        return attachPathExists;
    }
    
    public String getMimeType() {
        final TLRPC.MessageMedia media = this.messageOwner.media;
        if (media instanceof TLRPC.TL_messageMediaDocument) {
            return media.document.mime_type;
        }
        if (media instanceof TLRPC.TL_messageMediaInvoice) {
            final TLRPC.WebDocument photo = ((TLRPC.TL_messageMediaInvoice)media).photo;
            if (photo != null) {
                return photo.mime_type;
            }
        }
        else {
            if (media instanceof TLRPC.TL_messageMediaPhoto) {
                return "image/jpeg";
            }
            if (media instanceof TLRPC.TL_messageMediaWebPage) {
                final TLRPC.WebPage webpage = media.webpage;
                if (webpage.document != null) {
                    return media.document.mime_type;
                }
                if (webpage.photo != null) {
                    return "image/jpeg";
                }
            }
        }
        return "";
    }
    
    public String getMusicAuthor() {
        return this.getMusicAuthor(true);
    }
    
    public String getMusicAuthor(final boolean b) {
        final TLRPC.Document document = this.getDocument();
        if (document != null) {
            int i = 0;
            int n = 0;
            while (i < document.attributes.size()) {
                final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                int n2 = 0;
                Label_0128: {
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                        if (!documentAttribute.voice) {
                            String s2;
                            final String s = s2 = documentAttribute.performer;
                            if (TextUtils.isEmpty((CharSequence)s)) {
                                s2 = s;
                                if (b) {
                                    s2 = LocaleController.getString("AudioUnknownArtist", 2131558737);
                                }
                            }
                            return s2;
                        }
                    }
                    else {
                        n2 = n;
                        if (!(documentAttribute instanceof TLRPC.TL_documentAttributeVideo)) {
                            break Label_0128;
                        }
                        n2 = n;
                        if (!documentAttribute.round_message) {
                            break Label_0128;
                        }
                    }
                    n2 = 1;
                }
                Label_0469: {
                    if (n2 != 0) {
                        TLRPC.User user = null;
                        if (!b) {
                            return null;
                        }
                        if (!this.isOutOwner()) {
                            final TLRPC.MessageFwdHeader fwd_from = this.messageOwner.fwd_from;
                            if (fwd_from == null || fwd_from.from_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                final TLRPC.MessageFwdHeader fwd_from2 = this.messageOwner.fwd_from;
                                TLRPC.Chat chat = null;
                                Label_0437: {
                                    if (fwd_from2 != null && fwd_from2.channel_id != 0) {
                                        chat = MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.fwd_from.channel_id);
                                    }
                                    else {
                                        final TLRPC.MessageFwdHeader fwd_from3 = this.messageOwner.fwd_from;
                                        TLRPC.User user2;
                                        if (fwd_from3 != null && fwd_from3.from_id != 0) {
                                            user2 = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.fwd_from.from_id);
                                        }
                                        else {
                                            final TLRPC.MessageFwdHeader fwd_from4 = this.messageOwner.fwd_from;
                                            if (fwd_from4 != null) {
                                                final String from_name = fwd_from4.from_name;
                                                if (from_name != null) {
                                                    return from_name;
                                                }
                                            }
                                            final TLRPC.Message messageOwner = this.messageOwner;
                                            final int from_id = messageOwner.from_id;
                                            if (from_id < 0) {
                                                chat = MessagesController.getInstance(this.currentAccount).getChat(-this.messageOwner.from_id);
                                                break Label_0437;
                                            }
                                            if (from_id == 0 && messageOwner.to_id.channel_id != 0) {
                                                chat = MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.to_id.channel_id);
                                                break Label_0437;
                                            }
                                            user2 = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.from_id);
                                        }
                                        final TLRPC.Chat chat2 = null;
                                        user = user2;
                                        chat = chat2;
                                    }
                                }
                                if (user != null) {
                                    return UserObject.getUserName(user);
                                }
                                if (chat != null) {
                                    return chat.title;
                                }
                                break Label_0469;
                            }
                        }
                        return LocaleController.getString("FromYou", 2131559584);
                    }
                }
                ++i;
                n = n2;
            }
        }
        return LocaleController.getString("AudioUnknownArtist", 2131558737);
    }
    
    public String getMusicTitle() {
        return this.getMusicTitle(true);
    }
    
    public String getMusicTitle(final boolean b) {
        final TLRPC.Document document = this.getDocument();
        if (document != null) {
            int i = 0;
            while (i < document.attributes.size()) {
                final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                    if (!documentAttribute.voice) {
                        final String title = documentAttribute.title;
                        if (title != null) {
                            final String s = title;
                            if (title.length() != 0) {
                                return s;
                            }
                        }
                        String s;
                        final String s2 = s = FileLoader.getDocumentFileName(document);
                        if (TextUtils.isEmpty((CharSequence)s2)) {
                            s = s2;
                            if (b) {
                                s = LocaleController.getString("AudioUnknownTitle", 2131558738);
                            }
                        }
                        return s;
                    }
                    if (!b) {
                        return null;
                    }
                    return LocaleController.formatDateAudio(this.messageOwner.date);
                }
                else {
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo && documentAttribute.round_message) {
                        return LocaleController.formatDateAudio(this.messageOwner.date);
                    }
                    ++i;
                }
            }
            final String documentFileName = FileLoader.getDocumentFileName(document);
            if (!TextUtils.isEmpty((CharSequence)documentFileName)) {
                return documentFileName;
            }
        }
        return LocaleController.getString("AudioUnknownTitle", 2131558738);
    }
    
    public long getPollId() {
        if (this.type != 17) {
            return 0L;
        }
        return ((TLRPC.TL_messageMediaPoll)this.messageOwner.media).poll.id;
    }
    
    public int getRealId() {
        final TLRPC.Message messageOwner = this.messageOwner;
        int n = messageOwner.realId;
        if (n == 0) {
            n = messageOwner.id;
        }
        return n;
    }
    
    public int getSecretTimeLeft() {
        final TLRPC.Message messageOwner = this.messageOwner;
        int n = messageOwner.ttl;
        final int destroyTime = messageOwner.destroyTime;
        if (destroyTime != 0) {
            n = Math.max(0, destroyTime - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
        }
        return n;
    }
    
    public String getSecretTimeString() {
        if (!this.isSecretMedia()) {
            return null;
        }
        final int secretTimeLeft = this.getSecretTimeLeft();
        String s;
        if (secretTimeLeft < 60) {
            final StringBuilder sb = new StringBuilder();
            sb.append(secretTimeLeft);
            sb.append("s");
            s = sb.toString();
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(secretTimeLeft / 60);
            sb2.append("m");
            s = sb2.toString();
        }
        return s;
    }
    
    public int getSize() {
        return getMessageSize(this.messageOwner);
    }
    
    public String getStickerEmoji() {
        int index = 0;
        while (true) {
            final int size = this.messageOwner.media.document.attributes.size();
            final String s = null;
            if (index >= size) {
                return null;
            }
            final TLRPC.DocumentAttribute documentAttribute = this.messageOwner.media.document.attributes.get(index);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                final String alt = documentAttribute.alt;
                String alt2 = s;
                if (alt != null) {
                    alt2 = s;
                    if (alt.length() > 0) {
                        alt2 = documentAttribute.alt;
                    }
                }
                return alt2;
            }
            ++index;
        }
    }
    
    public String getStrickerChar() {
        final TLRPC.MessageMedia media = this.messageOwner.media;
        if (media != null) {
            final TLRPC.Document document = media.document;
            if (document != null) {
                for (final TLRPC.DocumentAttribute documentAttribute : document.attributes) {
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                        return documentAttribute.alt;
                    }
                }
            }
        }
        return null;
    }
    
    public int getUnradFlags() {
        return getUnreadFlags(this.messageOwner);
    }
    
    public ArrayList<MessageObject> getWebPagePhotos(final ArrayList<MessageObject> list, final ArrayList<TLRPC.PageBlock> list2) {
        ArrayList<MessageObject> list3 = list;
        if (list == null) {
            list3 = new ArrayList<MessageObject>();
        }
        final TLRPC.MessageMedia media = this.messageOwner.media;
        if (media != null) {
            final TLRPC.WebPage webpage = media.webpage;
            if (webpage != null) {
                final TLRPC.Page cached_page = webpage.cached_page;
                if (cached_page == null) {
                    return list3;
                }
                ArrayList<TLRPC.PageBlock> blocks;
                if ((blocks = list2) == null) {
                    blocks = cached_page.blocks;
                }
                for (int i = 0; i < blocks.size(); ++i) {
                    final TLRPC.PageBlock pageBlock = blocks.get(i);
                    if (pageBlock instanceof TLRPC.TL_pageBlockSlideshow) {
                        final TLRPC.TL_pageBlockSlideshow tl_pageBlockSlideshow = (TLRPC.TL_pageBlockSlideshow)pageBlock;
                        for (int j = 0; j < tl_pageBlockSlideshow.items.size(); ++j) {
                            list3.add(this.getMessageObjectForBlock(webpage, tl_pageBlockSlideshow.items.get(j)));
                        }
                    }
                    else if (pageBlock instanceof TLRPC.TL_pageBlockCollage) {
                        final TLRPC.TL_pageBlockCollage tl_pageBlockCollage = (TLRPC.TL_pageBlockCollage)pageBlock;
                        for (int k = 0; k < tl_pageBlockCollage.items.size(); ++k) {
                            list3.add(this.getMessageObjectForBlock(webpage, tl_pageBlockCollage.items.get(k)));
                        }
                    }
                }
            }
        }
        return list3;
    }
    
    public boolean hasPhotoStickers() {
        final TLRPC.MessageMedia media = this.messageOwner.media;
        if (media != null) {
            final TLRPC.Photo photo = media.photo;
            if (photo != null && photo.has_stickers) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasValidGroupId() {
        if (this.getGroupId() != 0L) {
            final ArrayList<TLRPC.PhotoSize> photoThumbs = this.photoThumbs;
            if (photoThumbs != null && !photoThumbs.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasValidReplyMessageObject() {
        final MessageObject replyMessageObject = this.replyMessageObject;
        if (replyMessageObject != null) {
            final TLRPC.Message messageOwner = replyMessageObject.messageOwner;
            if (!(messageOwner instanceof TLRPC.TL_messageEmpty) && !(messageOwner.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isAnimatedSticker() {
        final int type = this.type;
        if (type != 1000) {
            return type == 15;
        }
        return isAnimatedStickerMessage(this.messageOwner);
    }
    
    public boolean isAnyKindOfSticker() {
        final int type = this.type;
        return type == 13 || type == 15;
    }
    
    public boolean isContentUnread() {
        return this.messageOwner.media_unread;
    }
    
    public boolean isEditing() {
        final TLRPC.Message messageOwner = this.messageOwner;
        return messageOwner.send_state == 3 && messageOwner.id > 0;
    }
    
    public boolean isFcmMessage() {
        return this.localType != 0;
    }
    
    public boolean isForwarded() {
        return isForwardedMessage(this.messageOwner);
    }
    
    public boolean isForwardedChannelPost() {
        final TLRPC.Message messageOwner = this.messageOwner;
        if (messageOwner.from_id <= 0) {
            final TLRPC.MessageFwdHeader fwd_from = messageOwner.fwd_from;
            if (fwd_from != null && fwd_from.channel_post != 0) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isFromChat() {
        if (this.getDialogId() == UserConfig.getInstance(this.currentAccount).clientUserId) {
            return true;
        }
        if (!this.isMegagroup()) {
            final TLRPC.Peer to_id = this.messageOwner.to_id;
            if (to_id == null || to_id.chat_id == 0) {
                final TLRPC.Peer to_id2 = this.messageOwner.to_id;
                boolean b2;
                final boolean b = b2 = false;
                if (to_id2 != null) {
                    b2 = b;
                    if (to_id2.channel_id != 0) {
                        final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.to_id.channel_id);
                        b2 = b;
                        if (chat != null) {
                            b2 = b;
                            if (chat.megagroup) {
                                b2 = true;
                            }
                        }
                    }
                }
                return b2;
            }
        }
        return true;
    }
    
    public boolean isFromUser() {
        final TLRPC.Message messageOwner = this.messageOwner;
        return messageOwner.from_id > 0 && !messageOwner.post;
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
        final TLRPC.MessageMedia media = this.messageOwner.media;
        return media != null && isNewGifDocument(media.document);
    }
    
    public boolean isOut() {
        return this.messageOwner.out;
    }
    
    public boolean isOutOwner() {
        final TLRPC.Message messageOwner = this.messageOwner;
        if (!messageOwner.out || messageOwner.from_id <= 0 || messageOwner.post) {
            return false;
        }
        final TLRPC.MessageFwdHeader fwd_from = messageOwner.fwd_from;
        final boolean b = true;
        final boolean b2 = true;
        if (fwd_from == null) {
            return true;
        }
        final int clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        if (this.getDialogId() == clientUserId) {
            final TLRPC.MessageFwdHeader fwd_from2 = this.messageOwner.fwd_from;
            if (fwd_from2.from_id == clientUserId) {
                final TLRPC.Peer saved_from_peer = fwd_from2.saved_from_peer;
                boolean b3 = b2;
                if (saved_from_peer == null) {
                    return b3;
                }
                b3 = b2;
                if (saved_from_peer.user_id == clientUserId) {
                    return b3;
                }
            }
            final TLRPC.Peer saved_from_peer2 = this.messageOwner.fwd_from.saved_from_peer;
            return saved_from_peer2 != null && saved_from_peer2.user_id == clientUserId && b2;
        }
        final TLRPC.Peer saved_from_peer3 = this.messageOwner.fwd_from.saved_from_peer;
        boolean b4 = b;
        if (saved_from_peer3 != null) {
            b4 = (saved_from_peer3.user_id == clientUserId && b);
        }
        return b4;
    }
    
    public boolean isPhoto() {
        return isPhoto(this.messageOwner);
    }
    
    public boolean isPollClosed() {
        return this.type == 17 && ((TLRPC.TL_messageMediaPoll)this.messageOwner.media).poll.closed;
    }
    
    public boolean isReply() {
        final MessageObject replyMessageObject = this.replyMessageObject;
        if (replyMessageObject == null || !(replyMessageObject.messageOwner instanceof TLRPC.TL_messageEmpty)) {
            final TLRPC.Message messageOwner = this.messageOwner;
            if ((messageOwner.reply_to_msg_id != 0 || messageOwner.reply_to_random_id != 0L) && (this.messageOwner.flags & 0x8) != 0x0) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isRoundVideo() {
        final int isRoundVideoCached = this.isRoundVideoCached;
        boolean b = true;
        if (isRoundVideoCached == 0) {
            int isRoundVideoCached2;
            if (this.type != 5 && !isRoundVideoMessage(this.messageOwner)) {
                isRoundVideoCached2 = 2;
            }
            else {
                isRoundVideoCached2 = 1;
            }
            this.isRoundVideoCached = isRoundVideoCached2;
        }
        if (this.isRoundVideoCached != 1) {
            b = false;
        }
        return b;
    }
    
    public boolean isSavedFromMegagroup() {
        final TLRPC.MessageFwdHeader fwd_from = this.messageOwner.fwd_from;
        if (fwd_from != null) {
            final TLRPC.Peer saved_from_peer = fwd_from.saved_from_peer;
            if (saved_from_peer != null && saved_from_peer.channel_id != 0) {
                return ChatObject.isMegagroup(MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.fwd_from.saved_from_peer.channel_id));
            }
        }
        return false;
    }
    
    public boolean isSecretMedia() {
        final TLRPC.Message messageOwner = this.messageOwner;
        final boolean b = messageOwner instanceof TLRPC.TL_message_secret;
        boolean b2 = true;
        final boolean b3 = true;
        if (b) {
            if (messageOwner.media instanceof TLRPC.TL_messageMediaPhoto || this.isGif()) {
                final int ttl = this.messageOwner.ttl;
                if (ttl > 0) {
                    final boolean b4 = b3;
                    if (ttl <= 60) {
                        return b4;
                    }
                }
            }
            boolean b4 = b3;
            if (!this.isVoice()) {
                b4 = b3;
                if (!this.isRoundVideo()) {
                    b4 = (this.isVideo() && b3);
                }
            }
            return b4;
        }
        if (messageOwner instanceof TLRPC.TL_message) {
            final TLRPC.MessageMedia media = messageOwner.media;
            if ((!(media instanceof TLRPC.TL_messageMediaPhoto) && !(media instanceof TLRPC.TL_messageMediaDocument)) || this.messageOwner.media.ttl_seconds == 0) {
                b2 = false;
            }
            return b2;
        }
        return false;
    }
    
    public boolean isSendError() {
        final TLRPC.Message messageOwner = this.messageOwner;
        return messageOwner.send_state == 2 && messageOwner.id < 0;
    }
    
    public boolean isSending() {
        final TLRPC.Message messageOwner = this.messageOwner;
        final int send_state = messageOwner.send_state;
        boolean b = true;
        if (send_state != 1 || messageOwner.id >= 0) {
            b = false;
        }
        return b;
    }
    
    public boolean isSent() {
        final TLRPC.Message messageOwner = this.messageOwner;
        return messageOwner.send_state == 0 || messageOwner.id > 0;
    }
    
    public boolean isSticker() {
        final int type = this.type;
        if (type != 1000) {
            return type == 13;
        }
        return isStickerMessage(this.messageOwner);
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
        }
        final TLRPC.TL_messageMediaPoll tl_messageMediaPoll = (TLRPC.TL_messageMediaPoll)this.messageOwner.media;
        final TLRPC.TL_pollResults results = tl_messageMediaPoll.results;
        if (results != null) {
            if (!results.results.isEmpty()) {
                for (int size = tl_messageMediaPoll.results.results.size(), i = 0; i < size; ++i) {
                    if (tl_messageMediaPoll.results.results.get(i).chosen) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isWallpaper() {
        final TLRPC.MessageMedia media = this.messageOwner.media;
        if (media instanceof TLRPC.TL_messageMediaWebPage) {
            final TLRPC.WebPage webpage = media.webpage;
            if (webpage != null && "telegram_background".equals(webpage.type)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isWebpage() {
        return this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage;
    }
    
    public boolean isWebpageDocument() {
        final TLRPC.MessageMedia media = this.messageOwner.media;
        if (media instanceof TLRPC.TL_messageMediaWebPage) {
            final TLRPC.Document document = media.webpage.document;
            if (document != null && !isGifDocument(document)) {
                return true;
            }
        }
        return false;
    }
    
    public void measureInlineBotButtons() {
        this.wantedBotKeyboardWidth = 0;
        if (!(this.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup)) {
            return;
        }
        Theme.createChatResources(null, true);
        final StringBuilder botButtonsLayout = this.botButtonsLayout;
        if (botButtonsLayout == null) {
            this.botButtonsLayout = new StringBuilder();
        }
        else {
            botButtonsLayout.setLength(0);
        }
        for (int i = 0; i < this.messageOwner.reply_markup.rows.size(); ++i) {
            final TLRPC.TL_keyboardButtonRow tl_keyboardButtonRow = this.messageOwner.reply_markup.rows.get(i);
            final int size = tl_keyboardButtonRow.buttons.size();
            int j = 0;
            int a = 0;
            while (j < size) {
                final TLRPC.KeyboardButton keyboardButton = tl_keyboardButtonRow.buttons.get(j);
                final StringBuilder botButtonsLayout2 = this.botButtonsLayout;
                botButtonsLayout2.append(i);
                botButtonsLayout2.append(j);
                CharSequence charSequence;
                if (keyboardButton instanceof TLRPC.TL_keyboardButtonBuy && (this.messageOwner.media.flags & 0x4) != 0x0) {
                    charSequence = LocaleController.getString("PaymentReceipt", 2131560388);
                }
                else {
                    charSequence = Emoji.replaceEmoji(keyboardButton.text, Theme.chat_msgBotButtonPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false);
                }
                final StaticLayout staticLayout = new StaticLayout(charSequence, Theme.chat_msgBotButtonPaint, AndroidUtilities.dp(2000.0f), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                int max = a;
                if (staticLayout.getLineCount() > 0) {
                    final float lineWidth = staticLayout.getLineWidth(0);
                    final float lineLeft = staticLayout.getLineLeft(0);
                    float n = lineWidth;
                    if (lineLeft < lineWidth) {
                        n = lineWidth - lineLeft;
                    }
                    max = Math.max(a, (int)Math.ceil(n) + AndroidUtilities.dp(4.0f));
                }
                ++j;
                a = max;
            }
            this.wantedBotKeyboardWidth = Math.max(this.wantedBotKeyboardWidth, (a + AndroidUtilities.dp(12.0f)) * size + AndroidUtilities.dp(5.0f) * (size - 1));
        }
    }
    
    public boolean needDrawAvatar() {
        if (!this.isFromUser() && this.eventId == 0L) {
            final TLRPC.MessageFwdHeader fwd_from = this.messageOwner.fwd_from;
            if (fwd_from == null || fwd_from.saved_from_peer == null) {
                return false;
            }
        }
        return true;
    }
    
    public boolean needDrawBluredPreview() {
        final TLRPC.Message messageOwner = this.messageOwner;
        final boolean b = messageOwner instanceof TLRPC.TL_message_secret;
        boolean b2 = true;
        final boolean b3 = true;
        if (b) {
            final int max = Math.max(messageOwner.ttl, messageOwner.media.ttl_seconds);
            if (max > 0) {
                if (this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto || this.isVideo() || this.isGif()) {
                    final boolean b4 = b3;
                    if (max <= 60) {
                        return b4;
                    }
                }
                if (this.isRoundVideo()) {
                    return b3;
                }
            }
            return false;
        }
        if (messageOwner instanceof TLRPC.TL_message) {
            final TLRPC.MessageMedia media = messageOwner.media;
            if ((!(media instanceof TLRPC.TL_messageMediaPhoto) && !(media instanceof TLRPC.TL_messageMediaDocument)) || this.messageOwner.media.ttl_seconds == 0) {
                b2 = false;
            }
            return b2;
        }
        return false;
    }
    
    public boolean needDrawForwarded() {
        final TLRPC.Message messageOwner = this.messageOwner;
        if ((messageOwner.flags & 0x4) != 0x0) {
            final TLRPC.MessageFwdHeader fwd_from = messageOwner.fwd_from;
            if (fwd_from != null) {
                final TLRPC.Peer saved_from_peer = fwd_from.saved_from_peer;
                if ((saved_from_peer == null || saved_from_peer.channel_id != fwd_from.channel_id) && UserConfig.getInstance(this.currentAccount).getClientUserId() != this.getDialogId()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean needDrawShareButton() {
        if (this.eventId != 0L) {
            return false;
        }
        final TLRPC.MessageFwdHeader fwd_from = this.messageOwner.fwd_from;
        final boolean b = true;
        if (fwd_from != null && !this.isOutOwner() && this.messageOwner.fwd_from.saved_from_peer != null && this.getDialogId() == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            return true;
        }
        final int type = this.type;
        if (type != 13) {
            if (type != 15) {
                final TLRPC.MessageFwdHeader fwd_from2 = this.messageOwner.fwd_from;
                if (fwd_from2 != null && fwd_from2.channel_id != 0 && !this.isOutOwner()) {
                    return true;
                }
                if (this.isFromUser()) {
                    final TLRPC.MessageMedia media = this.messageOwner.media;
                    if (media instanceof TLRPC.TL_messageMediaEmpty || media == null || (media instanceof TLRPC.TL_messageMediaWebPage && !(media.webpage instanceof TLRPC.TL_webPage))) {
                        return false;
                    }
                    final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(this.messageOwner.from_id);
                    if (user != null && user.bot) {
                        return true;
                    }
                    if (!this.isOut()) {
                        final TLRPC.MessageMedia media2 = this.messageOwner.media;
                        boolean b2 = b;
                        if (!(media2 instanceof TLRPC.TL_messageMediaGame)) {
                            if (media2 instanceof TLRPC.TL_messageMediaInvoice) {
                                b2 = b;
                            }
                            else {
                                if (!this.isMegagroup()) {
                                    return false;
                                }
                                final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(this.messageOwner.to_id.channel_id);
                                if (chat != null) {
                                    final String username = chat.username;
                                    if (username != null && username.length() > 0) {
                                        final TLRPC.MessageMedia media3 = this.messageOwner.media;
                                        if (!(media3 instanceof TLRPC.TL_messageMediaContact) && !(media3 instanceof TLRPC.TL_messageMediaGeo)) {
                                            b2 = b;
                                            return b2;
                                        }
                                    }
                                }
                                b2 = false;
                            }
                        }
                        return b2;
                    }
                }
                else {
                    final TLRPC.Message messageOwner = this.messageOwner;
                    if (messageOwner.from_id < 0 || messageOwner.post) {
                        final TLRPC.Message messageOwner2 = this.messageOwner;
                        if (messageOwner2.to_id.channel_id != 0) {
                            if (messageOwner2.via_bot_id != 0 || messageOwner2.reply_to_msg_id != 0) {
                                final int type2 = this.type;
                                if (type2 == 13 || type2 == 15) {
                                    return false;
                                }
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public CharSequence replaceWithLink(final CharSequence charSequence, final String s, final ArrayList<Integer> list, final AbstractMap<Integer, TLRPC.User> abstractMap, final SparseArray<TLRPC.User> sparseArray) {
        CharSequence replace = charSequence;
        if (TextUtils.indexOf(charSequence, (CharSequence)s) >= 0) {
            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder((CharSequence)"");
            for (int i = 0; i < list.size(); ++i) {
                TLRPC.User user = null;
                if (abstractMap != null) {
                    user = abstractMap.get(list.get(i));
                }
                else if (sparseArray != null) {
                    user = (TLRPC.User)sparseArray.get((int)list.get(i));
                }
                TLRPC.User user2;
                if ((user2 = user) == null) {
                    user2 = MessagesController.getInstance(this.currentAccount).getUser(list.get(i));
                }
                if (user2 != null) {
                    final String userName = UserObject.getUserName(user2);
                    final int length = spannableStringBuilder.length();
                    if (spannableStringBuilder.length() != 0) {
                        spannableStringBuilder.append((CharSequence)", ");
                    }
                    spannableStringBuilder.append((CharSequence)userName);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(user2.id);
                    spannableStringBuilder.setSpan((Object)new URLSpanNoUnderlineBold(sb.toString()), length, userName.length() + length, 33);
                }
            }
            replace = TextUtils.replace(charSequence, new String[] { s }, new CharSequence[] { (CharSequence)spannableStringBuilder });
        }
        return replace;
    }
    
    public CharSequence replaceWithLink(final CharSequence charSequence, final String s, final TLObject tlObject) {
        final int index = TextUtils.indexOf(charSequence, (CharSequence)s);
        if (index >= 0) {
            String s2;
            String str;
            if (tlObject instanceof TLRPC.User) {
                final TLRPC.User user = (TLRPC.User)tlObject;
                s2 = UserObject.getUserName(user);
                final StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(user.id);
                str = sb.toString();
            }
            else if (tlObject instanceof TLRPC.Chat) {
                final TLRPC.Chat chat = (TLRPC.Chat)tlObject;
                s2 = chat.title;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                sb2.append(-chat.id);
                str = sb2.toString();
            }
            else if (tlObject instanceof TLRPC.TL_game) {
                s2 = ((TLRPC.TL_game)tlObject).title;
                str = "game";
            }
            else {
                str = "0";
                s2 = "";
            }
            final String replace = s2.replace('\n', ' ');
            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(TextUtils.replace(charSequence, new String[] { s }, (CharSequence[])new String[] { replace }));
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            sb3.append(str);
            spannableStringBuilder.setSpan((Object)new URLSpanNoUnderlineBold(sb3.toString()), index, replace.length() + index, 33);
            return (CharSequence)spannableStringBuilder;
        }
        return charSequence;
    }
    
    public void resetLayout() {
        this.layoutCreated = false;
    }
    
    public void resetPlayingProgress() {
        this.audioProgress = 0.0f;
        this.audioProgressSec = 0;
        this.bufferedProgress = 0.0f;
    }
    
    public void setContentIsRead() {
        this.messageOwner.media_unread = false;
    }
    
    public void setIsRead() {
        this.messageOwner.unread = false;
    }
    
    public void setType() {
        final int type = this.type;
        this.isRoundVideoCached = 0;
        final TLRPC.Message messageOwner = this.messageOwner;
        if (!(messageOwner instanceof TLRPC.TL_message) && !(messageOwner instanceof TLRPC.TL_messageForwarded_old2)) {
            if (messageOwner instanceof TLRPC.TL_messageService) {
                final TLRPC.MessageAction action = messageOwner.action;
                if (action instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
                    this.type = 0;
                }
                else if (!(action instanceof TLRPC.TL_messageActionChatEditPhoto) && !(action instanceof TLRPC.TL_messageActionUserUpdatedPhoto)) {
                    if (action instanceof TLRPC.TL_messageEncryptedAction) {
                        final TLRPC.DecryptedMessageAction encryptedAction = action.encryptedAction;
                        if (!(encryptedAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) && !(encryptedAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL)) {
                            this.contentType = -1;
                            this.type = -1;
                        }
                        else {
                            this.contentType = 1;
                            this.type = 10;
                        }
                    }
                    else if (action instanceof TLRPC.TL_messageActionHistoryClear) {
                        this.contentType = -1;
                        this.type = -1;
                    }
                    else if (action instanceof TLRPC.TL_messageActionPhoneCall) {
                        this.type = 16;
                    }
                    else {
                        this.contentType = 1;
                        this.type = 10;
                    }
                }
                else {
                    this.contentType = 1;
                    this.type = 11;
                }
            }
        }
        else if (this.isMediaEmpty()) {
            this.type = 0;
            if (TextUtils.isEmpty(this.messageText) && this.eventId == 0L) {
                this.messageText = "Empty message";
            }
        }
        else {
            final TLRPC.MessageMedia media = this.messageOwner.media;
            if (media.ttl_seconds != 0 && (media.photo instanceof TLRPC.TL_photoEmpty || media.document instanceof TLRPC.TL_documentEmpty)) {
                this.contentType = 1;
                this.type = 10;
            }
            else {
                final TLRPC.MessageMedia media2 = this.messageOwner.media;
                if (media2 instanceof TLRPC.TL_messageMediaPhoto) {
                    this.type = 1;
                }
                else if (!(media2 instanceof TLRPC.TL_messageMediaGeo) && !(media2 instanceof TLRPC.TL_messageMediaVenue) && !(media2 instanceof TLRPC.TL_messageMediaGeoLive)) {
                    if (this.isRoundVideo()) {
                        this.type = 5;
                    }
                    else if (this.isVideo()) {
                        this.type = 3;
                    }
                    else if (this.isVoice()) {
                        this.type = 2;
                    }
                    else if (this.isMusic()) {
                        this.type = 14;
                    }
                    else {
                        final TLRPC.MessageMedia media3 = this.messageOwner.media;
                        if (media3 instanceof TLRPC.TL_messageMediaContact) {
                            this.type = 12;
                        }
                        else if (media3 instanceof TLRPC.TL_messageMediaPoll) {
                            this.type = 17;
                        }
                        else if (media3 instanceof TLRPC.TL_messageMediaUnsupported) {
                            this.type = 0;
                        }
                        else if (media3 instanceof TLRPC.TL_messageMediaDocument) {
                            final TLRPC.Document document = media3.document;
                            if (document != null && document.mime_type != null) {
                                if (isGifDocument(document)) {
                                    this.type = 8;
                                }
                                else if (this.isSticker()) {
                                    this.type = 13;
                                }
                                else if (this.isAnimatedSticker()) {
                                    this.type = 15;
                                }
                                else {
                                    this.type = 9;
                                }
                            }
                            else {
                                this.type = 9;
                            }
                        }
                        else if (media3 instanceof TLRPC.TL_messageMediaGame) {
                            this.type = 0;
                        }
                        else if (media3 instanceof TLRPC.TL_messageMediaInvoice) {
                            this.type = 0;
                        }
                    }
                }
                else {
                    this.type = 4;
                }
            }
        }
        if (type != 1000 && type != this.type) {
            this.generateThumbs(false);
        }
    }
    
    public boolean shouldDrawWithoutBackground() {
        final int type = this.type;
        return type == 13 || type == 15 || type == 5;
    }
    
    public boolean shouldEncryptPhotoOrVideo() {
        return shouldEncryptPhotoOrVideo(this.messageOwner);
    }
    
    public static class GroupedMessagePosition
    {
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
        
        public void set(final int n, final int n2, final int n3, final int n4, final int n5, final float ph, final int n6) {
            this.minX = (byte)n;
            this.maxX = (byte)n2;
            this.minY = (byte)n3;
            this.maxY = (byte)n4;
            this.pw = n5;
            this.spanSize = n5;
            this.ph = ph;
            this.flags = (byte)n6;
        }
    }
    
    public static class GroupedMessages
    {
        private int firstSpanAdditionalSize;
        public long groupId;
        public boolean hasSibling;
        private int maxSizeWidth;
        public ArrayList<MessageObject> messages;
        public ArrayList<GroupedMessagePosition> posArray;
        public HashMap<MessageObject, GroupedMessagePosition> positions;
        
        public GroupedMessages() {
            this.messages = new ArrayList<MessageObject>();
            this.posArray = new ArrayList<GroupedMessagePosition>();
            this.positions = new HashMap<MessageObject, GroupedMessagePosition>();
            this.maxSizeWidth = 800;
            this.firstSpanAdditionalSize = 200;
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
            int size = this.messages.size();
            final byte b = 1;
            if (size <= 1) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            this.hasSibling = false;
            int i = 0;
            boolean b2 = false;
            float n = 1.0f;
            boolean b3 = false;
            int outOwner = 0;
            while (i < size) {
                final MessageObject key = this.messages.get(i);
                Label_0188: {
                    if (i == 0) {
                        outOwner = (key.isOutOwner() ? 1 : 0);
                        Label_0185: {
                            if (outOwner == 0) {
                                final TLRPC.MessageFwdHeader fwd_from = key.messageOwner.fwd_from;
                                if (fwd_from == null || fwd_from.saved_from_peer == null) {
                                    final TLRPC.Message messageOwner = key.messageOwner;
                                    if (messageOwner.from_id <= 0) {
                                        break Label_0185;
                                    }
                                    final TLRPC.Peer to_id = messageOwner.to_id;
                                    if (to_id.channel_id == 0 && to_id.chat_id == 0) {
                                        final TLRPC.MessageMedia media = messageOwner.media;
                                        if (!(media instanceof TLRPC.TL_messageMediaGame) && !(media instanceof TLRPC.TL_messageMediaInvoice)) {
                                            break Label_0185;
                                        }
                                    }
                                }
                                b2 = true;
                                break Label_0188;
                            }
                        }
                        b2 = false;
                    }
                }
                final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(key.photoThumbs, AndroidUtilities.getPhotoSize());
                final GroupedMessagePosition groupedMessagePosition = new GroupedMessagePosition();
                groupedMessagePosition.last = (i == size - 1);
                float aspectRatio;
                if (closestPhotoSizeWithSize == null) {
                    aspectRatio = 1.0f;
                }
                else {
                    aspectRatio = closestPhotoSizeWithSize.w / (float)closestPhotoSizeWithSize.h;
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
                n += aspectRatio3;
                if (aspectRatio3 > 2.0f) {
                    b3 = true;
                }
                this.positions.put(key, groupedMessagePosition);
                this.posArray.add(groupedMessagePosition);
                ++i;
            }
            if (b2) {
                this.maxSizeWidth -= 50;
                this.firstSpanAdditionalSize += 50;
            }
            final int dp = AndroidUtilities.dp(120.0f);
            final float n2 = (float)AndroidUtilities.dp(120.0f);
            final Point displaySize = AndroidUtilities.displaySize;
            final int a = (int)(n2 / (Math.min(displaySize.x, displaySize.y) / (float)this.maxSizeWidth));
            final float n3 = (float)AndroidUtilities.dp(40.0f);
            final Point displaySize2 = AndroidUtilities.displaySize;
            final float n4 = (float)Math.min(displaySize2.x, displaySize2.y);
            final int maxSizeWidth = this.maxSizeWidth;
            final int n5 = (int)(n3 / (n4 / maxSizeWidth));
            final float n6 = maxSizeWidth / 814.0f;
            final float n7 = n / size;
            final float n8 = AndroidUtilities.dp(100.0f) / 814.0f;
            byte b4 = 0;
            if (!b3 && (size == 2 || size == 3 || size == 4)) {
                Label_0968: {
                    if (size == 2) {
                        final GroupedMessagePosition groupedMessagePosition2 = this.posArray.get(0);
                        final GroupedMessagePosition groupedMessagePosition3 = this.posArray.get(1);
                        final String string = sb.toString();
                        if (string.equals("ww")) {
                            final double n9 = n7;
                            final double v = n6;
                            Double.isNaN(v);
                            if (n9 > v * 1.4) {
                                final float aspectRatio4 = groupedMessagePosition2.aspectRatio;
                                final float aspectRatio5 = groupedMessagePosition3.aspectRatio;
                                if (aspectRatio4 - aspectRatio5 < 0.2) {
                                    final int maxSizeWidth2 = this.maxSizeWidth;
                                    final float n10 = Math.round(Math.min(maxSizeWidth2 / aspectRatio4, Math.min(maxSizeWidth2 / aspectRatio5, 407.0f))) / 814.0f;
                                    groupedMessagePosition2.set(0, 0, 0, 0, this.maxSizeWidth, n10, 7);
                                    groupedMessagePosition3.set(0, 0, 1, 1, this.maxSizeWidth, n10, 11);
                                    b4 = 0;
                                    break Label_0968;
                                }
                            }
                        }
                        if (!string.equals("ww") && !string.equals("qq")) {
                            final int maxSizeWidth3 = this.maxSizeWidth;
                            final float n11 = (float)maxSizeWidth3;
                            final float n12 = (float)maxSizeWidth3;
                            final float aspectRatio6 = groupedMessagePosition2.aspectRatio;
                            final int n13 = (int)Math.max(n11 * 0.4f, (float)Math.round(n12 / aspectRatio6 / (1.0f / aspectRatio6 + 1.0f / groupedMessagePosition3.aspectRatio)));
                            final int n14 = this.maxSizeWidth - n13;
                            int n15 = n13;
                            int n16;
                            if ((n16 = n14) < a) {
                                n15 = n13 - (a - n14);
                                n16 = a;
                            }
                            final float n17 = Math.min(814.0f, (float)Math.round(Math.min(n16 / groupedMessagePosition2.aspectRatio, n15 / groupedMessagePosition3.aspectRatio))) / 814.0f;
                            groupedMessagePosition2.set(0, 0, 0, 0, n16, n17, 13);
                            groupedMessagePosition3.set(1, 1, 0, 0, n15, n17, 14);
                        }
                        else {
                            final int n18 = this.maxSizeWidth / 2;
                            final float n19 = (float)n18;
                            final float n20 = Math.round(Math.min(n19 / groupedMessagePosition2.aspectRatio, Math.min(n19 / groupedMessagePosition3.aspectRatio, 814.0f))) / 814.0f;
                            groupedMessagePosition2.set(0, 0, 0, 0, n18, n20, 13);
                            groupedMessagePosition3.set(1, 1, 0, 0, n18, n20, 14);
                        }
                        b4 = 1;
                    }
                    else {
                        final int n21 = outOwner;
                        if (size == 3) {
                            final GroupedMessagePosition groupedMessagePosition4 = this.posArray.get(0);
                            final GroupedMessagePosition groupedMessagePosition5 = this.posArray.get(1);
                            final GroupedMessagePosition groupedMessagePosition6 = this.posArray.get(2);
                            if (sb.charAt(0) == 'n') {
                                final float aspectRatio7 = groupedMessagePosition5.aspectRatio;
                                final float min = Math.min(407.0f, (float)Math.round(this.maxSizeWidth * aspectRatio7 / (groupedMessagePosition6.aspectRatio + aspectRatio7)));
                                final float n22 = 814.0f - min;
                                final int n23 = (int)Math.max((float)a, Math.min(this.maxSizeWidth * 0.5f, (float)Math.round(Math.min(groupedMessagePosition6.aspectRatio * min, groupedMessagePosition5.aspectRatio * n22))));
                                final int round = Math.round(Math.min(groupedMessagePosition4.aspectRatio * 814.0f + n5, (float)(this.maxSizeWidth - n23)));
                                groupedMessagePosition4.set(0, 0, 0, 1, round, 1.0f, 13);
                                final float n24 = n22 / 814.0f;
                                groupedMessagePosition5.set(1, 1, 0, 0, n23, n24, 6);
                                final float n25 = min / 814.0f;
                                groupedMessagePosition6.set(0, 1, 1, 1, n23, n25, 10);
                                final int maxSizeWidth4 = this.maxSizeWidth;
                                groupedMessagePosition6.spanSize = maxSizeWidth4;
                                groupedMessagePosition4.siblingHeights = new float[] { n25, n24 };
                                if (n21 != 0) {
                                    groupedMessagePosition4.spanSize = maxSizeWidth4 - n23;
                                }
                                else {
                                    groupedMessagePosition5.spanSize = maxSizeWidth4 - round;
                                    groupedMessagePosition6.leftSpanOffset = round;
                                }
                                this.hasSibling = true;
                                b4 = b;
                            }
                            else {
                                final float n26 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition4.aspectRatio, 537.24005f)) / 814.0f;
                                groupedMessagePosition4.set(0, 1, 0, 0, this.maxSizeWidth, n26, 7);
                                final int n27 = this.maxSizeWidth / 2;
                                final float n28 = (float)n27;
                                float n29;
                                if ((n29 = Math.min(814.0f - n26, (float)Math.round(Math.min(n28 / groupedMessagePosition5.aspectRatio, n28 / groupedMessagePosition6.aspectRatio))) / 814.0f) < n8) {
                                    n29 = n8;
                                }
                                groupedMessagePosition5.set(0, 0, 1, 1, n27, n29, 9);
                                groupedMessagePosition6.set(1, 1, 1, 1, n27, n29, 10);
                                b4 = b;
                            }
                        }
                        else if (size == 4) {
                            final GroupedMessagePosition groupedMessagePosition7 = this.posArray.get(0);
                            final GroupedMessagePosition groupedMessagePosition8 = this.posArray.get(1);
                            final GroupedMessagePosition groupedMessagePosition9 = this.posArray.get(2);
                            final GroupedMessagePosition groupedMessagePosition10 = this.posArray.get(3);
                            if (sb.charAt(0) == 'w') {
                                final float n30 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition7.aspectRatio, 537.24005f)) / 814.0f;
                                groupedMessagePosition7.set(0, 2, 0, 0, this.maxSizeWidth, n30, 7);
                                final float b5 = (float)Math.round(this.maxSizeWidth / (groupedMessagePosition8.aspectRatio + groupedMessagePosition9.aspectRatio + groupedMessagePosition10.aspectRatio));
                                final float n31 = (float)a;
                                final int n32 = (int)Math.max(n31, Math.min(this.maxSizeWidth * 0.4f, groupedMessagePosition8.aspectRatio * b5));
                                final int n33 = (int)Math.max(Math.max(n31, this.maxSizeWidth * 0.33f), groupedMessagePosition10.aspectRatio * b5);
                                final int n34 = this.maxSizeWidth - n32 - n33;
                                int n35 = n33;
                                int n36 = n32;
                                int dp2;
                                if ((dp2 = n34) < AndroidUtilities.dp(58.0f)) {
                                    final int n37 = AndroidUtilities.dp(58.0f) - n34;
                                    dp2 = AndroidUtilities.dp(58.0f);
                                    final int n38 = n37 / 2;
                                    n36 = n32 - n38;
                                    n35 = n33 - (n37 - n38);
                                }
                                float n39;
                                if ((n39 = Math.min(814.0f - n30, b5) / 814.0f) < n8) {
                                    n39 = n8;
                                }
                                groupedMessagePosition8.set(0, 0, 1, 1, n36, n39, 9);
                                groupedMessagePosition9.set(1, 1, 1, 1, dp2, n39, 8);
                                groupedMessagePosition10.set(2, 2, 1, 1, n35, n39, 10);
                                b4 = 2;
                            }
                            else {
                                final int max = Math.max(a, Math.round(814.0f / (1.0f / groupedMessagePosition8.aspectRatio + 1.0f / groupedMessagePosition9.aspectRatio + 1.0f / groupedMessagePosition10.aspectRatio)));
                                final float n40 = (float)dp;
                                final float n41 = (float)max;
                                final float min2 = Math.min(0.33f, Math.max(n40, n41 / groupedMessagePosition8.aspectRatio) / 814.0f);
                                final float min3 = Math.min(0.33f, Math.max(n40, n41 / groupedMessagePosition9.aspectRatio) / 814.0f);
                                final float n42 = 1.0f - min2 - min3;
                                final int round2 = Math.round(Math.min(814.0f * groupedMessagePosition7.aspectRatio + n5, (float)(this.maxSizeWidth - max)));
                                groupedMessagePosition7.set(0, 0, 0, 2, round2, min2 + min3 + n42, 13);
                                groupedMessagePosition8.set(1, 1, 0, 0, max, min2, 6);
                                groupedMessagePosition9.set(0, 1, 1, 1, max, min3, 2);
                                groupedMessagePosition9.spanSize = this.maxSizeWidth;
                                groupedMessagePosition10.set(0, 1, 2, 2, max, n42, 10);
                                final int maxSizeWidth5 = this.maxSizeWidth;
                                groupedMessagePosition10.spanSize = maxSizeWidth5;
                                if (n21 != 0) {
                                    groupedMessagePosition7.spanSize = maxSizeWidth5 - max;
                                }
                                else {
                                    groupedMessagePosition8.spanSize = maxSizeWidth5 - round2;
                                    groupedMessagePosition9.leftSpanOffset = round2;
                                    groupedMessagePosition10.leftSpanOffset = round2;
                                }
                                groupedMessagePosition7.siblingHeights = new float[] { min2, min3, n42 };
                                this.hasSibling = true;
                                b4 = 1;
                            }
                        }
                        else {
                            b4 = 0;
                        }
                    }
                }
            }
            else {
                final boolean b6 = outOwner != 0;
                final float[] array = new float[this.posArray.size()];
                for (int j = 0; j < size; ++j) {
                    if (n7 > 1.1f) {
                        array[j] = Math.max(1.0f, this.posArray.get(j).aspectRatio);
                    }
                    else {
                        array[j] = Math.min(1.0f, this.posArray.get(j).aspectRatio);
                    }
                    array[j] = Math.max(0.66667f, Math.min(1.7f, array[j]));
                }
                final ArrayList<MessageGroupedLayoutAttempt> list = new ArrayList<MessageGroupedLayoutAttempt>();
                for (int k = 1; k < array.length; ++k) {
                    final int n43 = array.length - k;
                    if (k <= 3) {
                        if (n43 <= 3) {
                            list.add(new MessageGroupedLayoutAttempt(k, n43, this.multiHeight(array, 0, k), this.multiHeight(array, k, array.length)));
                        }
                    }
                }
                final int n44 = size;
                int l = 1;
                final float a2 = n8;
                while (l < array.length - 1) {
                    for (int n45 = 1; n45 < array.length - l; ++n45) {
                        final int n46 = array.length - l - n45;
                        if (l <= 3) {
                            int n47;
                            if (n7 < 0.85f) {
                                n47 = 4;
                            }
                            else {
                                n47 = 3;
                            }
                            if (n45 <= n47) {
                                if (n46 <= 3) {
                                    final float multiHeight = this.multiHeight(array, 0, l);
                                    final int n48 = l + n45;
                                    list.add(new MessageGroupedLayoutAttempt(l, n45, n46, multiHeight, this.multiHeight(array, l, n48), this.multiHeight(array, n48, array.length)));
                                }
                            }
                        }
                    }
                    ++l;
                }
                for (int n49 = 1; n49 < array.length - 2; ++n49) {
                    for (int n50 = 1; n50 < array.length - n49; ++n50) {
                        for (int n51 = 1; n51 < array.length - n49 - n50; ++n51) {
                            final int n52 = array.length - n49 - n50 - n51;
                            if (n49 <= 3 && n50 <= 3 && n51 <= 3) {
                                if (n52 <= 3) {
                                    final float multiHeight2 = this.multiHeight(array, 0, n49);
                                    final int n53 = n49 + n50;
                                    final float multiHeight3 = this.multiHeight(array, n49, n53);
                                    final int n54 = n53 + n51;
                                    list.add(new MessageGroupedLayoutAttempt(n49, n50, n51, n52, multiHeight2, multiHeight3, this.multiHeight(array, n53, n54), this.multiHeight(array, n54, array.length)));
                                }
                            }
                        }
                    }
                }
                final float n55 = (float)(this.maxSizeWidth / 3 * 4);
                MessageGroupedLayoutAttempt messageGroupedLayoutAttempt = null;
                int index = 0;
                float n56 = 0.0f;
                while (index < list.size()) {
                    final MessageGroupedLayoutAttempt messageGroupedLayoutAttempt2 = list.get(index);
                    int n57 = 0;
                    float n58 = 0.0f;
                    float n59 = Float.MAX_VALUE;
                    while (true) {
                        final float[] heights = messageGroupedLayoutAttempt2.heights;
                        if (n57 >= heights.length) {
                            break;
                        }
                        n58 += heights[n57];
                        float n60 = n59;
                        if (heights[n57] < n59) {
                            n60 = heights[n57];
                        }
                        ++n57;
                        n59 = n60;
                    }
                    final float abs = Math.abs(n58 - n55);
                    final int[] lineCounts = messageGroupedLayoutAttempt2.lineCounts;
                    float n61 = 0.0f;
                    Label_2802: {
                        if (lineCounts.length > 1) {
                            if (lineCounts[0] <= lineCounts[1] && (lineCounts.length <= 2 || lineCounts[1] <= lineCounts[2])) {
                                final int[] lineCounts2 = messageGroupedLayoutAttempt2.lineCounts;
                                n61 = abs;
                                if (lineCounts2.length <= 3) {
                                    break Label_2802;
                                }
                                n61 = abs;
                                if (lineCounts2[2] <= lineCounts2[3]) {
                                    break Label_2802;
                                }
                            }
                            n61 = abs * 1.2f;
                        }
                        else {
                            n61 = abs;
                        }
                    }
                    float n63;
                    final float n62 = n63 = n61;
                    if (n59 < a) {
                        n63 = n62 * 1.5f;
                    }
                    float n64 = 0.0f;
                    Label_2851: {
                        if (messageGroupedLayoutAttempt != null) {
                            n64 = n56;
                            if (n63 >= n56) {
                                break Label_2851;
                            }
                        }
                        messageGroupedLayoutAttempt = messageGroupedLayoutAttempt2;
                        n64 = n63;
                    }
                    ++index;
                    n56 = n64;
                }
                if (messageGroupedLayoutAttempt == null) {
                    return;
                }
                int n65 = 0;
                int n66 = 0;
                byte a3 = 0;
                while (true) {
                    final int[] lineCounts3 = messageGroupedLayoutAttempt.lineCounts;
                    b4 = a3;
                    size = n44;
                    if (n65 >= lineCounts3.length) {
                        break;
                    }
                    final int n67 = lineCounts3[n65];
                    final float n68 = messageGroupedLayoutAttempt.heights[n65];
                    int maxSizeWidth6 = this.maxSizeWidth;
                    GroupedMessagePosition groupedMessagePosition11 = null;
                    final int b7 = n67 - 1;
                    final int max2 = Math.max(a3, b7);
                    int index2 = n66;
                    int n71;
                    GroupedMessagePosition groupedMessagePosition13;
                    for (int n69 = 0; n69 < n67; ++n69, maxSizeWidth6 = n71, groupedMessagePosition11 = groupedMessagePosition13) {
                        final int n70 = (int)(array[index2] * n68);
                        n71 = maxSizeWidth6 - n70;
                        final GroupedMessagePosition groupedMessagePosition12 = this.posArray.get(index2);
                        int n72;
                        if (n65 == 0) {
                            n72 = 4;
                        }
                        else {
                            n72 = 0;
                        }
                        int n73 = n72;
                        if (n65 == messageGroupedLayoutAttempt.lineCounts.length - 1) {
                            n73 = (n72 | 0x8);
                        }
                        groupedMessagePosition13 = groupedMessagePosition11;
                        int n74 = n73;
                        if (n69 == 0) {
                            final int n75 = n73 | 0x1;
                            groupedMessagePosition13 = groupedMessagePosition11;
                            n74 = n75;
                            if (b6) {
                                groupedMessagePosition13 = groupedMessagePosition12;
                                n74 = n75;
                            }
                        }
                        int n76 = n74;
                        if (n69 == b7) {
                            n76 = (n74 | 0x2);
                            if (!b6) {
                                groupedMessagePosition13 = groupedMessagePosition12;
                                n76 = n76;
                            }
                        }
                        groupedMessagePosition12.set(n69, n69, n65, n65, n70, Math.max(a2, n68 / 814.0f), n76);
                        ++index2;
                    }
                    groupedMessagePosition11.pw += maxSizeWidth6;
                    groupedMessagePosition11.spanSize += maxSizeWidth6;
                    ++n65;
                    n66 = index2;
                    a3 = (byte)max2;
                }
            }
            for (int n77 = 0; n77 < size; ++n77) {
                final GroupedMessagePosition groupedMessagePosition14 = this.posArray.get(n77);
                if (outOwner != 0) {
                    if (groupedMessagePosition14.minX == 0) {
                        groupedMessagePosition14.spanSize += this.firstSpanAdditionalSize;
                    }
                    if ((groupedMessagePosition14.flags & 0x2) != 0x0) {
                        groupedMessagePosition14.edge = true;
                    }
                }
                else {
                    if (groupedMessagePosition14.maxX == b4 || (groupedMessagePosition14.flags & 0x2) != 0x0) {
                        groupedMessagePosition14.spanSize += this.firstSpanAdditionalSize;
                    }
                    if ((groupedMessagePosition14.flags & 0x1) != 0x0) {
                        groupedMessagePosition14.edge = true;
                    }
                }
                final MessageObject messageObject = this.messages.get(n77);
                if (outOwner == 0 && messageObject.needDrawAvatarInternal()) {
                    if (groupedMessagePosition14.edge) {
                        final int spanSize = groupedMessagePosition14.spanSize;
                        if (spanSize != 1000) {
                            groupedMessagePosition14.spanSize = spanSize + 108;
                        }
                        groupedMessagePosition14.pw += 108;
                    }
                    else if ((groupedMessagePosition14.flags & 0x2) != 0x0) {
                        final int spanSize2 = groupedMessagePosition14.spanSize;
                        if (spanSize2 != 1000) {
                            groupedMessagePosition14.spanSize = spanSize2 - 108;
                        }
                        else {
                            final int leftSpanOffset = groupedMessagePosition14.leftSpanOffset;
                            if (leftSpanOffset != 0) {
                                groupedMessagePosition14.leftSpanOffset = leftSpanOffset + 108;
                            }
                        }
                    }
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
    
    public static class TextLayoutBlock
    {
        public int charactersEnd;
        public int charactersOffset;
        public byte directionFlags;
        public int height;
        public int heightByOffset;
        public StaticLayout textLayout;
        public float textYOffset;
        
        public boolean isRtl() {
            final byte directionFlags = this.directionFlags;
            return (directionFlags & 0x1) != 0x0 && (directionFlags & 0x2) == 0x0;
        }
    }
    
    public static class VCardData
    {
        private String company;
        private ArrayList<String> emails;
        private ArrayList<String> phones;
        
        public VCardData() {
            this.emails = new ArrayList<String>();
            this.phones = new ArrayList<String>();
        }
        
        public static CharSequence parse(String substring) {
            try {
                final BufferedReader bufferedReader = new BufferedReader(new StringReader(substring));
                final int n = 0;
                int n2 = 0;
                VCardData vCardData = null;
                substring = null;
                while (true) {
                    final String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.startsWith("PHOTO")) {
                        continue;
                    }
                    int n3 = n2;
                    VCardData vCardData2 = vCardData;
                    if (line.indexOf(58) >= 0) {
                        if (line.startsWith("BEGIN:VCARD")) {
                            vCardData2 = new VCardData();
                            n3 = n2;
                        }
                        else {
                            n3 = n2;
                            vCardData2 = vCardData;
                            if (line.startsWith("END:VCARD")) {
                                n3 = n2;
                                if ((vCardData2 = vCardData) != null) {
                                    n3 = 1;
                                    vCardData2 = vCardData;
                                }
                            }
                        }
                    }
                    String s = substring;
                    String string = line;
                    if (substring != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(substring);
                        sb.append(line);
                        string = sb.toString();
                        s = null;
                    }
                    if (string.contains("=QUOTED-PRINTABLE") && string.endsWith("=")) {
                        substring = string.substring(0, string.length() - 1);
                        n2 = n3;
                        vCardData = vCardData2;
                    }
                    else {
                        final int index = string.indexOf(":");
                        String[] array;
                        if (index >= 0) {
                            array = new String[] { string.substring(0, index), string.substring(index + 1).trim() };
                        }
                        else {
                            array = new String[] { string.trim() };
                        }
                        n2 = n3;
                        vCardData = vCardData2;
                        substring = s;
                        if (array.length < 2) {
                            continue;
                        }
                        if (vCardData2 == null) {
                            n2 = n3;
                            vCardData = vCardData2;
                            substring = s;
                        }
                        else if (array[0].startsWith("ORG")) {
                            final String[] split = array[0].split(";");
                            final int length = split.length;
                            int i = 0;
                            String s2 = null;
                            substring = null;
                            while (i < length) {
                                final String[] split2 = split[i].split("=");
                                String s3;
                                if (split2.length != 2) {
                                    s3 = substring;
                                }
                                else if (split2[0].equals("CHARSET")) {
                                    s3 = split2[1];
                                }
                                else {
                                    s3 = substring;
                                    if (split2[0].equals("ENCODING")) {
                                        s2 = split2[1];
                                        s3 = substring;
                                    }
                                }
                                ++i;
                                substring = s3;
                            }
                            vCardData2.company = array[1];
                            if (s2 != null && s2.equalsIgnoreCase("QUOTED-PRINTABLE")) {
                                final byte[] decodeQuotedPrintable = AndroidUtilities.decodeQuotedPrintable(AndroidUtilities.getStringBytes(vCardData2.company));
                                if (decodeQuotedPrintable != null && decodeQuotedPrintable.length != 0) {
                                    vCardData2.company = new String(decodeQuotedPrintable, substring);
                                }
                            }
                            vCardData2.company = vCardData2.company.replace(';', ' ');
                            n2 = n3;
                            vCardData = vCardData2;
                            substring = s;
                        }
                        else if (array[0].startsWith("TEL")) {
                            n2 = n3;
                            vCardData = vCardData2;
                            substring = s;
                            if (array[1].length() <= 0) {
                                continue;
                            }
                            vCardData2.phones.add(array[1]);
                            n2 = n3;
                            vCardData = vCardData2;
                            substring = s;
                        }
                        else {
                            n2 = n3;
                            vCardData = vCardData2;
                            substring = s;
                            if (!array[0].startsWith("EMAIL")) {
                                continue;
                            }
                            final String e = array[1];
                            n2 = n3;
                            vCardData = vCardData2;
                            substring = s;
                            if (e.length() <= 0) {
                                continue;
                            }
                            vCardData2.emails.add(e);
                            n2 = n3;
                            vCardData = vCardData2;
                            substring = s;
                        }
                    }
                }
                try {
                    bufferedReader.close();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                if (n2 != 0) {
                    final StringBuilder sb2 = new StringBuilder();
                    int index2 = 0;
                    int j;
                    while (true) {
                        j = n;
                        if (index2 >= vCardData.phones.size()) {
                            break;
                        }
                        if (sb2.length() > 0) {
                            sb2.append('\n');
                        }
                        final String str = vCardData.phones.get(index2);
                        if (!str.contains("#") && !str.contains("*")) {
                            sb2.append(PhoneFormat.getInstance().format(str));
                        }
                        else {
                            sb2.append(str);
                        }
                        ++index2;
                    }
                    while (j < vCardData.emails.size()) {
                        if (sb2.length() > 0) {
                            sb2.append('\n');
                        }
                        sb2.append(PhoneFormat.getInstance().format(vCardData.emails.get(j)));
                        ++j;
                    }
                    if (!TextUtils.isEmpty((CharSequence)vCardData.company)) {
                        if (sb2.length() > 0) {
                            sb2.append('\n');
                        }
                        sb2.append(vCardData.company);
                    }
                    return sb2;
                }
                return null;
            }
            catch (Throwable t) {
                return null;
            }
        }
    }
}
