package org.telegram.messenger;

import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.SparseArray;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.PhoneFormat.C0278PhoneFormat;
import org.telegram.messenger.Emoji.EmojiSpan;
import org.telegram.messenger.browser.Browser;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.TypefaceSpan;
import org.telegram.p004ui.Components.URLSpanBotCommand;
import org.telegram.p004ui.Components.URLSpanBrowser;
import org.telegram.p004ui.Components.URLSpanMono;
import org.telegram.p004ui.Components.URLSpanNoUnderline;
import org.telegram.p004ui.Components.URLSpanNoUnderlineBold;
import org.telegram.p004ui.Components.URLSpanReplacement;
import org.telegram.p004ui.Components.URLSpanUserMention;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.BotInlineResult;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.DecryptedMessageAction;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.InputStickerSet;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.MessageAction;
import org.telegram.tgnet.TLRPC.MessageEntity;
import org.telegram.tgnet.TLRPC.MessageFwdHeader;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.Page;
import org.telegram.tgnet.TLRPC.PageBlock;
import org.telegram.tgnet.TLRPC.Peer;
import org.telegram.tgnet.TLRPC.Photo;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.SecureValueType;
import org.telegram.tgnet.TLRPC.TL_channelAdminLogEvent;
import org.telegram.tgnet.TLRPC.TL_chatAdminRights;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionScreenshotMessages;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL;
import org.telegram.tgnet.TLRPC.TL_document;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAnimated;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC.TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC.TL_documentEmpty;
import org.telegram.tgnet.TLRPC.TL_documentEncrypted;
import org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC.TL_game;
import org.telegram.tgnet.TLRPC.TL_inputMessageEntityMentionName;
import org.telegram.tgnet.TLRPC.TL_inputStickerSetEmpty;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonBuy;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonRow;
import org.telegram.tgnet.TLRPC.TL_message;
import org.telegram.tgnet.TLRPC.TL_messageActionBotAllowed;
import org.telegram.tgnet.TLRPC.TL_messageActionChannelCreate;
import org.telegram.tgnet.TLRPC.TL_messageActionChannelMigrateFrom;
import org.telegram.tgnet.TLRPC.TL_messageActionChatAddUser;
import org.telegram.tgnet.TLRPC.TL_messageActionChatCreate;
import org.telegram.tgnet.TLRPC.TL_messageActionChatDeletePhoto;
import org.telegram.tgnet.TLRPC.TL_messageActionChatDeleteUser;
import org.telegram.tgnet.TLRPC.TL_messageActionChatEditPhoto;
import org.telegram.tgnet.TLRPC.TL_messageActionChatEditTitle;
import org.telegram.tgnet.TLRPC.TL_messageActionChatJoinedByLink;
import org.telegram.tgnet.TLRPC.TL_messageActionChatMigrateTo;
import org.telegram.tgnet.TLRPC.TL_messageActionContactSignUp;
import org.telegram.tgnet.TLRPC.TL_messageActionCreatedBroadcastList;
import org.telegram.tgnet.TLRPC.TL_messageActionCustomAction;
import org.telegram.tgnet.TLRPC.TL_messageActionEmpty;
import org.telegram.tgnet.TLRPC.TL_messageActionGameScore;
import org.telegram.tgnet.TLRPC.TL_messageActionHistoryClear;
import org.telegram.tgnet.TLRPC.TL_messageActionLoginUnknownLocation;
import org.telegram.tgnet.TLRPC.TL_messageActionPaymentSent;
import org.telegram.tgnet.TLRPC.TL_messageActionPhoneCall;
import org.telegram.tgnet.TLRPC.TL_messageActionPinMessage;
import org.telegram.tgnet.TLRPC.TL_messageActionScreenshotTaken;
import org.telegram.tgnet.TLRPC.TL_messageActionSecureValuesSent;
import org.telegram.tgnet.TLRPC.TL_messageActionTTLChange;
import org.telegram.tgnet.TLRPC.TL_messageActionUserJoined;
import org.telegram.tgnet.TLRPC.TL_messageActionUserUpdatedPhoto;
import org.telegram.tgnet.TLRPC.TL_messageEmpty;
import org.telegram.tgnet.TLRPC.TL_messageEncryptedAction;
import org.telegram.tgnet.TLRPC.TL_messageEntityBold;
import org.telegram.tgnet.TLRPC.TL_messageEntityBotCommand;
import org.telegram.tgnet.TLRPC.TL_messageEntityCashtag;
import org.telegram.tgnet.TLRPC.TL_messageEntityCode;
import org.telegram.tgnet.TLRPC.TL_messageEntityEmail;
import org.telegram.tgnet.TLRPC.TL_messageEntityHashtag;
import org.telegram.tgnet.TLRPC.TL_messageEntityItalic;
import org.telegram.tgnet.TLRPC.TL_messageEntityMention;
import org.telegram.tgnet.TLRPC.TL_messageEntityMentionName;
import org.telegram.tgnet.TLRPC.TL_messageEntityPhone;
import org.telegram.tgnet.TLRPC.TL_messageEntityPre;
import org.telegram.tgnet.TLRPC.TL_messageEntityTextUrl;
import org.telegram.tgnet.TLRPC.TL_messageEntityUrl;
import org.telegram.tgnet.TLRPC.TL_messageForwarded_old2;
import org.telegram.tgnet.TLRPC.TL_messageMediaContact;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_messageMediaGame;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
import org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC.TL_messageMediaUnsupported;
import org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_messageService;
import org.telegram.tgnet.TLRPC.TL_message_secret;
import org.telegram.tgnet.TLRPC.TL_pageBlockCollage;
import org.telegram.tgnet.TLRPC.TL_pageBlockPhoto;
import org.telegram.tgnet.TLRPC.TL_pageBlockSlideshow;
import org.telegram.tgnet.TLRPC.TL_pageBlockVideo;
import org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonBusy;
import org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonMissed;
import org.telegram.tgnet.TLRPC.TL_photo;
import org.telegram.tgnet.TLRPC.TL_photoCachedSize;
import org.telegram.tgnet.TLRPC.TL_photoEmpty;
import org.telegram.tgnet.TLRPC.TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC.TL_pollAnswerVoters;
import org.telegram.tgnet.TLRPC.TL_pollResults;
import org.telegram.tgnet.TLRPC.TL_replyInlineMarkup;
import org.telegram.tgnet.TLRPC.TL_secureValueTypeAddress;
import org.telegram.tgnet.TLRPC.TL_secureValueTypeBankStatement;
import org.telegram.tgnet.TLRPC.TL_secureValueTypeDriverLicense;
import org.telegram.tgnet.TLRPC.TL_secureValueTypeEmail;
import org.telegram.tgnet.TLRPC.TL_secureValueTypeIdentityCard;
import org.telegram.tgnet.TLRPC.TL_secureValueTypeInternalPassport;
import org.telegram.tgnet.TLRPC.TL_secureValueTypePassport;
import org.telegram.tgnet.TLRPC.TL_secureValueTypePassportRegistration;
import org.telegram.tgnet.TLRPC.TL_secureValueTypePersonalDetails;
import org.telegram.tgnet.TLRPC.TL_secureValueTypePhone;
import org.telegram.tgnet.TLRPC.TL_secureValueTypeRentalAgreement;
import org.telegram.tgnet.TLRPC.TL_secureValueTypeTemporaryRegistration;
import org.telegram.tgnet.TLRPC.TL_secureValueTypeUtilityBill;
import org.telegram.tgnet.TLRPC.TL_webPage;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.WebDocument;
import org.telegram.tgnet.TLRPC.WebPage;

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
    public TL_channelAdminLogEvent currentEvent;
    public String customReplyName;
    public String dateKey;
    public boolean deleted;
    public CharSequence editingMessage;
    public ArrayList<MessageEntity> editingMessageEntities;
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
    public Message messageOwner;
    public CharSequence messageText;
    public String monthKey;
    public ArrayList<PhotoSize> photoThumbs;
    public ArrayList<PhotoSize> photoThumbs2;
    public TLObject photoThumbsObject;
    public TLObject photoThumbsObject2;
    public long pollLastCheckTime;
    public boolean pollVisibleOnScreen;
    public String previousAttachPath;
    public String previousCaption;
    public ArrayList<MessageEntity> previousCaptionEntities;
    public MessageMedia previousMedia;
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
        /* renamed from: ph */
        public float f56ph;
        /* renamed from: pw */
        public int f57pw;
        public float[] siblingHeights;
        public int spanSize;

        public void set(int i, int i2, int i3, int i4, int i5, float f, int i6) {
            this.minX = (byte) i;
            this.maxX = (byte) i2;
            this.minY = (byte) i3;
            this.maxY = (byte) i4;
            this.f57pw = i5;
            this.spanSize = i5;
            this.f56ph = f;
            this.flags = (byte) i6;
        }
    }

    public static class GroupedMessages {
        private int firstSpanAdditionalSize = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        public long groupId;
        public boolean hasSibling;
        private int maxSizeWidth = 800;
        public ArrayList<MessageObject> messages = new ArrayList();
        public ArrayList<GroupedMessagePosition> posArray = new ArrayList();
        public HashMap<MessageObject, GroupedMessagePosition> positions = new HashMap();

        private class MessageGroupedLayoutAttempt {
            public float[] heights;
            public int[] lineCounts;

            public MessageGroupedLayoutAttempt(int i, int i2, float f, float f2) {
                this.lineCounts = new int[]{i, i2};
                this.heights = new float[]{f, f2};
            }

            public MessageGroupedLayoutAttempt(int i, int i2, int i3, float f, float f2, float f3) {
                this.lineCounts = new int[]{i, i2, i3};
                this.heights = new float[]{f, f2, f3};
            }

            public MessageGroupedLayoutAttempt(int i, int i2, int i3, int i4, float f, float f2, float f3, float f4) {
                this.lineCounts = new int[]{i, i2, i3, i4};
                this.heights = new float[]{f, f2, f3, f4};
            }
        }

        private float multiHeight(float[] fArr, int i, int i2) {
            float f = 0.0f;
            while (i < i2) {
                f += fArr[i];
                i++;
            }
            return ((float) this.maxSizeWidth) / f;
        }

        /* JADX WARNING: Missing block: B:23:0x005e, code skipped:
            if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice) == false) goto L_0x0062;
     */
        /* JADX WARNING: Missing block: B:179:0x06d9, code skipped:
            if (r6[2] > r6[3]) goto L_0x06dd;
     */
        public void calculate() {
            /*
            r36 = this;
            r10 = r36;
            r0 = r10.posArray;
            r0.clear();
            r0 = r10.positions;
            r0.clear();
            r0 = r10.messages;
            r11 = r0.size();
            r12 = 1;
            if (r11 > r12) goto L_0x0016;
        L_0x0015:
            return;
        L_0x0016:
            r13 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r14 = 0;
            r10.hasSibling = r14;
            r2 = 0;
            r3 = 0;
            r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r5 = 0;
            r15 = 0;
        L_0x0027:
            r16 = 1067030938; // 0x3f99999a float:1.2 double:5.271833295E-315;
            if (r2 >= r11) goto L_0x00c2;
        L_0x002c:
            r6 = r10.messages;
            r6 = r6.get(r2);
            r6 = (org.telegram.messenger.MessageObject) r6;
            if (r2 != 0) goto L_0x0065;
        L_0x0036:
            r3 = r6.isOutOwner();
            if (r3 != 0) goto L_0x0062;
        L_0x003c:
            r7 = r6.messageOwner;
            r7 = r7.fwd_from;
            if (r7 == 0) goto L_0x0046;
        L_0x0042:
            r7 = r7.saved_from_peer;
            if (r7 != 0) goto L_0x0060;
        L_0x0046:
            r7 = r6.messageOwner;
            r8 = r7.from_id;
            if (r8 <= 0) goto L_0x0062;
        L_0x004c:
            r8 = r7.to_id;
            r9 = r8.channel_id;
            if (r9 != 0) goto L_0x0060;
        L_0x0052:
            r8 = r8.chat_id;
            if (r8 != 0) goto L_0x0060;
        L_0x0056:
            r7 = r7.media;
            r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
            if (r8 != 0) goto L_0x0060;
        L_0x005c:
            r7 = r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
            if (r7 == 0) goto L_0x0062;
        L_0x0060:
            r7 = 1;
            goto L_0x0063;
        L_0x0062:
            r7 = 0;
        L_0x0063:
            r15 = r3;
            r3 = r7;
        L_0x0065:
            r7 = r6.photoThumbs;
            r8 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
            r7 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r7, r8);
            r8 = new org.telegram.messenger.MessageObject$GroupedMessagePosition;
            r8.<init>();
            r9 = r11 + -1;
            if (r2 != r9) goto L_0x007a;
        L_0x0078:
            r9 = 1;
            goto L_0x007b;
        L_0x007a:
            r9 = 0;
        L_0x007b:
            r8.last = r9;
            if (r7 != 0) goto L_0x0082;
        L_0x007f:
            r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            goto L_0x008a;
        L_0x0082:
            r9 = r7.f465w;
            r9 = (float) r9;
            r7 = r7.f464h;
            r7 = (float) r7;
            r7 = r9 / r7;
        L_0x008a:
            r8.aspectRatio = r7;
            r7 = r8.aspectRatio;
            r9 = (r7 > r16 ? 1 : (r7 == r16 ? 0 : -1));
            if (r9 <= 0) goto L_0x0098;
        L_0x0092:
            r7 = "w";
            r0.append(r7);
            goto L_0x00aa;
        L_0x0098:
            r9 = 1061997773; // 0x3f4ccccd float:0.8 double:5.246966156E-315;
            r7 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
            if (r7 >= 0) goto L_0x00a5;
        L_0x009f:
            r7 = "n";
            r0.append(r7);
            goto L_0x00aa;
        L_0x00a5:
            r7 = "q";
            r0.append(r7);
        L_0x00aa:
            r7 = r8.aspectRatio;
            r4 = r4 + r7;
            r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
            r7 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
            if (r7 <= 0) goto L_0x00b4;
        L_0x00b3:
            r5 = 1;
        L_0x00b4:
            r7 = r10.positions;
            r7.put(r6, r8);
            r6 = r10.posArray;
            r6.add(r8);
            r2 = r2 + 1;
            goto L_0x0027;
        L_0x00c2:
            if (r3 == 0) goto L_0x00d0;
        L_0x00c4:
            r2 = r10.maxSizeWidth;
            r2 = r2 + -50;
            r10.maxSizeWidth = r2;
            r2 = r10.firstSpanAdditionalSize;
            r2 = r2 + 50;
            r10.firstSpanAdditionalSize = r2;
        L_0x00d0:
            r2 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
            r3 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
            r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
            r2 = (float) r2;
            r6 = org.telegram.messenger.AndroidUtilities.displaySize;
            r7 = r6.x;
            r6 = r6.y;
            r6 = java.lang.Math.min(r7, r6);
            r6 = (float) r6;
            r7 = r10.maxSizeWidth;
            r7 = (float) r7;
            r6 = r6 / r7;
            r2 = r2 / r6;
            r9 = (int) r2;
            r2 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
            r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
            r2 = (float) r2;
            r6 = org.telegram.messenger.AndroidUtilities.displaySize;
            r7 = r6.x;
            r6 = r6.y;
            r6 = java.lang.Math.min(r7, r6);
            r6 = (float) r6;
            r7 = r10.maxSizeWidth;
            r8 = (float) r7;
            r6 = r6 / r8;
            r2 = r2 / r6;
            r2 = (int) r2;
            r6 = (float) r7;
            r6 = r6 / r13;
            r7 = (float) r11;
            r8 = r4 / r7;
            r4 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
            r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
            r4 = (float) r4;
            r7 = r4 / r13;
            r4 = 4;
            r1 = 3;
            r13 = 2;
            if (r5 != 0) goto L_0x0530;
        L_0x0117:
            if (r11 == r13) goto L_0x011d;
        L_0x0119:
            if (r11 == r1) goto L_0x011d;
        L_0x011b:
            if (r11 != r4) goto L_0x0530;
        L_0x011d:
            r5 = 1053609165; // 0x3ecccccd float:0.4 double:5.205520926E-315;
            r4 = 1137410048; // 0x43cb8000 float:407.0 double:5.6195523E-315;
            if (r11 != r13) goto L_0x0252;
        L_0x0125:
            r1 = r10.posArray;
            r1 = r1.get(r14);
            r1 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r1;
            r2 = r10.posArray;
            r2 = r2.get(r12);
            r2 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r2;
            r0 = r0.toString();
            r3 = "ww";
            r7 = r0.equals(r3);
            if (r7 == 0) goto L_0x01a1;
        L_0x0141:
            r7 = (double) r8;
            r18 = 4608983858650965606; // 0x3ff6666666666666 float:2.720083E23 double:1.4;
            r26 = r15;
            r14 = (double) r6;
            java.lang.Double.isNaN(r14);
            r14 = r14 * r18;
            r6 = (r7 > r14 ? 1 : (r7 == r14 ? 0 : -1));
            if (r6 <= 0) goto L_0x01a3;
        L_0x0153:
            r6 = r1.aspectRatio;
            r7 = r2.aspectRatio;
            r8 = r6 - r7;
            r14 = (double) r8;
            r18 = 4596373779694328218; // 0x3fc999999999999a float:-1.5881868E-23 double:0.2;
            r8 = (r14 > r18 ? 1 : (r14 == r18 ? 0 : -1));
            if (r8 >= 0) goto L_0x01a3;
        L_0x0163:
            r0 = r10.maxSizeWidth;
            r3 = (float) r0;
            r3 = r3 / r6;
            r0 = (float) r0;
            r0 = r0 / r7;
            r0 = java.lang.Math.min(r0, r4);
            r0 = java.lang.Math.min(r3, r0);
            r0 = java.lang.Math.round(r0);
            r0 = (float) r0;
            r3 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r0 = r0 / r3;
            r19 = 0;
            r20 = 0;
            r21 = 0;
            r22 = 0;
            r3 = r10.maxSizeWidth;
            r25 = 7;
            r18 = r1;
            r23 = r3;
            r24 = r0;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r21 = 1;
            r22 = 1;
            r1 = r10.maxSizeWidth;
            r25 = 11;
            r18 = r2;
            r23 = r1;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r0 = 0;
            goto L_0x024c;
        L_0x01a1:
            r26 = r15;
        L_0x01a3:
            r3 = r0.equals(r3);
            if (r3 != 0) goto L_0x0211;
        L_0x01a9:
            r3 = "qq";
            r0 = r0.equals(r3);
            if (r0 == 0) goto L_0x01b2;
        L_0x01b1:
            goto L_0x0211;
        L_0x01b2:
            r0 = r10.maxSizeWidth;
            r3 = (float) r0;
            r3 = r3 * r5;
            r0 = (float) r0;
            r4 = r1.aspectRatio;
            r0 = r0 / r4;
            r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r4 = r5 / r4;
            r6 = r2.aspectRatio;
            r5 = r5 / r6;
            r4 = r4 + r5;
            r0 = r0 / r4;
            r0 = java.lang.Math.round(r0);
            r0 = (float) r0;
            r0 = java.lang.Math.max(r3, r0);
            r0 = (int) r0;
            r3 = r10.maxSizeWidth;
            r3 = r3 - r0;
            if (r3 >= r9) goto L_0x01d7;
        L_0x01d3:
            r3 = r9 - r3;
            r0 = r0 - r3;
            r3 = r9;
        L_0x01d7:
            r4 = (float) r3;
            r5 = r1.aspectRatio;
            r4 = r4 / r5;
            r5 = (float) r0;
            r6 = r2.aspectRatio;
            r5 = r5 / r6;
            r4 = java.lang.Math.min(r4, r5);
            r4 = java.lang.Math.round(r4);
            r4 = (float) r4;
            r5 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r4 = java.lang.Math.min(r5, r4);
            r4 = r4 / r5;
            r19 = 0;
            r20 = 0;
            r21 = 0;
            r22 = 0;
            r25 = 13;
            r18 = r1;
            r23 = r3;
            r24 = r4;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r19 = 1;
            r20 = 1;
            r25 = 14;
            r18 = r2;
            r23 = r0;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            goto L_0x024b;
        L_0x0211:
            r0 = r10.maxSizeWidth;
            r0 = r0 / r13;
            r3 = (float) r0;
            r4 = r1.aspectRatio;
            r4 = r3 / r4;
            r5 = r2.aspectRatio;
            r3 = r3 / r5;
            r5 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r3 = java.lang.Math.min(r3, r5);
            r3 = java.lang.Math.min(r4, r3);
            r3 = java.lang.Math.round(r3);
            r3 = (float) r3;
            r3 = r3 / r5;
            r19 = 0;
            r20 = 0;
            r21 = 0;
            r22 = 0;
            r25 = 13;
            r18 = r1;
            r23 = r0;
            r24 = r3;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r19 = 1;
            r20 = 1;
            r25 = 14;
            r18 = r2;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
        L_0x024b:
            r0 = 1;
        L_0x024c:
            r12 = r0;
        L_0x024d:
            r17 = r11;
            r8 = 0;
            goto L_0x077d;
        L_0x0252:
            r26 = r15;
            r6 = 1141264221; // 0x44064f5d float:537.24005 double:5.638594444E-315;
            if (r11 != r1) goto L_0x038c;
        L_0x0259:
            r1 = r10.posArray;
            r3 = 0;
            r1 = r1.get(r3);
            r1 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r1;
            r5 = r10.posArray;
            r5 = r5.get(r12);
            r5 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r5;
            r8 = r10.posArray;
            r8 = r8.get(r13);
            r8 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r8;
            r0 = r0.charAt(r3);
            r3 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
            if (r0 != r3) goto L_0x0321;
        L_0x027a:
            r0 = r5.aspectRatio;
            r3 = r10.maxSizeWidth;
            r3 = (float) r3;
            r3 = r3 * r0;
            r6 = r8.aspectRatio;
            r6 = r6 + r0;
            r3 = r3 / r6;
            r0 = java.lang.Math.round(r3);
            r0 = (float) r0;
            r0 = java.lang.Math.min(r4, r0);
            r3 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r4 = r3 - r0;
            r3 = (float) r9;
            r6 = r10.maxSizeWidth;
            r6 = (float) r6;
            r7 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
            r6 = r6 * r7;
            r7 = r8.aspectRatio;
            r7 = r7 * r0;
            r9 = r5.aspectRatio;
            r9 = r9 * r4;
            r7 = java.lang.Math.min(r7, r9);
            r7 = java.lang.Math.round(r7);
            r7 = (float) r7;
            r6 = java.lang.Math.min(r6, r7);
            r3 = java.lang.Math.max(r3, r6);
            r3 = (int) r3;
            r6 = r1.aspectRatio;
            r7 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r6 = r6 * r7;
            r2 = (float) r2;
            r6 = r6 + r2;
            r2 = r10.maxSizeWidth;
            r2 = r2 - r3;
            r2 = (float) r2;
            r2 = java.lang.Math.min(r6, r2);
            r2 = java.lang.Math.round(r2);
            r19 = 0;
            r20 = 0;
            r21 = 0;
            r22 = 1;
            r24 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r25 = 13;
            r18 = r1;
            r23 = r2;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r19 = 1;
            r20 = 1;
            r22 = 0;
            r6 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r4 = r4 / r6;
            r25 = 6;
            r18 = r5;
            r23 = r3;
            r24 = r4;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r19 = 0;
            r21 = 1;
            r22 = 1;
            r6 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r0 = r0 / r6;
            r25 = 10;
            r18 = r8;
            r24 = r0;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r6 = r10.maxSizeWidth;
            r8.spanSize = r6;
            r7 = new float[r13];
            r9 = 0;
            r7[r9] = r0;
            r7[r12] = r4;
            r1.siblingHeights = r7;
            if (r26 == 0) goto L_0x0318;
        L_0x0314:
            r6 = r6 - r3;
            r1.spanSize = r6;
            goto L_0x031d;
        L_0x0318:
            r6 = r6 - r2;
            r5.spanSize = r6;
            r8.leftSpanOffset = r2;
        L_0x031d:
            r10.hasSibling = r12;
            goto L_0x024d;
        L_0x0321:
            r0 = r10.maxSizeWidth;
            r0 = (float) r0;
            r2 = r1.aspectRatio;
            r0 = r0 / r2;
            r0 = java.lang.Math.min(r0, r6);
            r0 = java.lang.Math.round(r0);
            r0 = (float) r0;
            r2 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r0 = r0 / r2;
            r19 = 0;
            r20 = 1;
            r21 = 0;
            r22 = 0;
            r2 = r10.maxSizeWidth;
            r25 = 7;
            r18 = r1;
            r23 = r2;
            r24 = r0;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r1 = r10.maxSizeWidth;
            r1 = r1 / r13;
            r2 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r0 = r2 - r0;
            r3 = (float) r1;
            r4 = r5.aspectRatio;
            r4 = r3 / r4;
            r6 = r8.aspectRatio;
            r3 = r3 / r6;
            r3 = java.lang.Math.min(r4, r3);
            r3 = java.lang.Math.round(r3);
            r3 = (float) r3;
            r0 = java.lang.Math.min(r0, r3);
            r0 = r0 / r2;
            r2 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1));
            if (r2 >= 0) goto L_0x036c;
        L_0x036b:
            r0 = r7;
        L_0x036c:
            r19 = 0;
            r20 = 0;
            r21 = 1;
            r22 = 1;
            r25 = 9;
            r18 = r5;
            r23 = r1;
            r24 = r0;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r19 = 1;
            r20 = 1;
            r25 = 10;
            r18 = r8;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            goto L_0x024d;
        L_0x038c:
            r4 = 4;
            if (r11 != r4) goto L_0x052a;
        L_0x038f:
            r4 = r10.posArray;
            r8 = 0;
            r4 = r4.get(r8);
            r4 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r4;
            r14 = r10.posArray;
            r14 = r14.get(r12);
            r14 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r14;
            r15 = r10.posArray;
            r15 = r15.get(r13);
            r15 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r15;
            r13 = r10.posArray;
            r13 = r13.get(r1);
            r13 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r13;
            r0 = r0.charAt(r8);
            r8 = 119; // 0x77 float:1.67E-43 double:5.9E-322;
            r12 = 1051260355; // 0x3ea8f5c3 float:0.33 double:5.19391626E-315;
            if (r0 != r8) goto L_0x0473;
        L_0x03bb:
            r0 = r10.maxSizeWidth;
            r0 = (float) r0;
            r1 = r4.aspectRatio;
            r0 = r0 / r1;
            r0 = java.lang.Math.min(r0, r6);
            r0 = java.lang.Math.round(r0);
            r0 = (float) r0;
            r1 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r0 = r0 / r1;
            r19 = 0;
            r20 = 2;
            r21 = 0;
            r22 = 0;
            r1 = r10.maxSizeWidth;
            r25 = 7;
            r18 = r4;
            r23 = r1;
            r24 = r0;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r1 = r10.maxSizeWidth;
            r1 = (float) r1;
            r2 = r14.aspectRatio;
            r3 = r15.aspectRatio;
            r2 = r2 + r3;
            r3 = r13.aspectRatio;
            r2 = r2 + r3;
            r1 = r1 / r2;
            r1 = java.lang.Math.round(r1);
            r1 = (float) r1;
            r2 = (float) r9;
            r3 = r10.maxSizeWidth;
            r3 = (float) r3;
            r3 = r3 * r5;
            r4 = r14.aspectRatio;
            r4 = r4 * r1;
            r3 = java.lang.Math.min(r3, r4);
            r3 = java.lang.Math.max(r2, r3);
            r3 = (int) r3;
            r4 = r10.maxSizeWidth;
            r4 = (float) r4;
            r4 = r4 * r12;
            r2 = java.lang.Math.max(r2, r4);
            r4 = r13.aspectRatio;
            r4 = r4 * r1;
            r2 = java.lang.Math.max(r2, r4);
            r2 = (int) r2;
            r4 = r10.maxSizeWidth;
            r4 = r4 - r3;
            r4 = r4 - r2;
            r5 = 1114112000; // 0x42680000 float:58.0 double:5.50444465E-315;
            r6 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
            if (r4 >= r6) goto L_0x0433;
        L_0x0425:
            r6 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
            r6 = r6 - r4;
            r4 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
            r5 = r6 / 2;
            r3 = r3 - r5;
            r6 = r6 - r5;
            r2 = r2 - r6;
        L_0x0433:
            r23 = r3;
            r3 = r2;
            r2 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r0 = r2 - r0;
            r0 = java.lang.Math.min(r0, r1);
            r0 = r0 / r2;
            r1 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1));
            if (r1 >= 0) goto L_0x0445;
        L_0x0444:
            r0 = r7;
        L_0x0445:
            r19 = 0;
            r20 = 0;
            r21 = 1;
            r22 = 1;
            r25 = 9;
            r18 = r14;
            r24 = r0;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r19 = 1;
            r20 = 1;
            r25 = 8;
            r18 = r15;
            r23 = r4;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r19 = 2;
            r20 = 2;
            r25 = 10;
            r18 = r13;
            r23 = r3;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r12 = 2;
            goto L_0x024d;
        L_0x0473:
            r0 = r14.aspectRatio;
            r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r0 = r5 / r0;
            r6 = r15.aspectRatio;
            r6 = r5 / r6;
            r0 = r0 + r6;
            r6 = r13.aspectRatio;
            r6 = r5 / r6;
            r0 = r0 + r6;
            r5 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r0 = r5 / r0;
            r0 = java.lang.Math.round(r0);
            r0 = java.lang.Math.max(r9, r0);
            r3 = (float) r3;
            r6 = (float) r0;
            r7 = r14.aspectRatio;
            r7 = r6 / r7;
            r7 = java.lang.Math.max(r3, r7);
            r7 = r7 / r5;
            r7 = java.lang.Math.min(r12, r7);
            r8 = r15.aspectRatio;
            r6 = r6 / r8;
            r3 = java.lang.Math.max(r3, r6);
            r3 = r3 / r5;
            r3 = java.lang.Math.min(r12, r3);
            r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r6 = r6 - r7;
            r6 = r6 - r3;
            r8 = r4.aspectRatio;
            r5 = r5 * r8;
            r2 = (float) r2;
            r5 = r5 + r2;
            r2 = r10.maxSizeWidth;
            r2 = r2 - r0;
            r2 = (float) r2;
            r2 = java.lang.Math.min(r5, r2);
            r2 = java.lang.Math.round(r2);
            r19 = 0;
            r20 = 0;
            r21 = 0;
            r22 = 2;
            r5 = r7 + r3;
            r24 = r5 + r6;
            r25 = 13;
            r18 = r4;
            r23 = r2;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r19 = 1;
            r20 = 1;
            r22 = 0;
            r25 = 6;
            r18 = r14;
            r23 = r0;
            r24 = r7;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r19 = 0;
            r21 = 1;
            r22 = 1;
            r25 = 2;
            r18 = r15;
            r24 = r3;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r5 = r10.maxSizeWidth;
            r15.spanSize = r5;
            r21 = 2;
            r22 = 2;
            r25 = 10;
            r18 = r13;
            r24 = r6;
            r18.set(r19, r20, r21, r22, r23, r24, r25);
            r5 = r10.maxSizeWidth;
            r13.spanSize = r5;
            if (r26 == 0) goto L_0x0511;
        L_0x050d:
            r5 = r5 - r0;
            r4.spanSize = r5;
            goto L_0x0518;
        L_0x0511:
            r5 = r5 - r2;
            r14.spanSize = r5;
            r15.leftSpanOffset = r2;
            r13.leftSpanOffset = r2;
        L_0x0518:
            r0 = new float[r1];
            r1 = 0;
            r0[r1] = r7;
            r1 = 1;
            r0[r1] = r3;
            r2 = 2;
            r0[r2] = r6;
            r4.siblingHeights = r0;
            r10.hasSibling = r1;
            r12 = 1;
            goto L_0x024d;
        L_0x052a:
            r17 = r11;
            r8 = 0;
            r12 = 0;
            goto L_0x077d;
        L_0x0530:
            r26 = r15;
            r0 = r10.posArray;
            r0 = r0.size();
            r12 = new float[r0];
            r0 = 0;
        L_0x053b:
            if (r0 >= r11) goto L_0x057e;
        L_0x053d:
            r2 = 1066192077; // 0x3f8ccccd float:1.1 double:5.26768877E-315;
            r2 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1));
            if (r2 <= 0) goto L_0x0557;
        L_0x0544:
            r2 = r10.posArray;
            r2 = r2.get(r0);
            r2 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r2;
            r2 = r2.aspectRatio;
            r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r2 = java.lang.Math.max(r3, r2);
            r12[r0] = r2;
            goto L_0x0569;
        L_0x0557:
            r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r2 = r10.posArray;
            r2 = r2.get(r0);
            r2 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r2;
            r2 = r2.aspectRatio;
            r2 = java.lang.Math.min(r3, r2);
            r12[r0] = r2;
        L_0x0569:
            r2 = 1059760867; // 0x3f2aaae3 float:0.66667 double:5.23591437E-315;
            r5 = 1071225242; // 0x3fd9999a float:1.7 double:5.29255591E-315;
            r6 = r12[r0];
            r5 = java.lang.Math.min(r5, r6);
            r2 = java.lang.Math.max(r2, r5);
            r12[r0] = r2;
            r0 = r0 + 1;
            goto L_0x053b;
        L_0x057e:
            r13 = new java.util.ArrayList;
            r13.<init>();
            r6 = 1;
        L_0x0584:
            r0 = r12.length;
            if (r6 >= r0) goto L_0x05ba;
        L_0x0587:
            r0 = r12.length;
            r3 = r0 - r6;
            if (r6 > r1) goto L_0x05ae;
        L_0x058c:
            if (r3 <= r1) goto L_0x058f;
        L_0x058e:
            goto L_0x05ae;
        L_0x058f:
            r14 = new org.telegram.messenger.MessageObject$GroupedMessages$MessageGroupedLayoutAttempt;
            r0 = 0;
            r5 = r10.multiHeight(r12, r0, r6);
            r0 = r12.length;
            r15 = r10.multiHeight(r12, r6, r0);
            r0 = r14;
            r2 = 3;
            r1 = r36;
            r17 = r11;
            r11 = 3;
            r2 = r6;
            r18 = 4;
            r4 = r5;
            r5 = r15;
            r0.<init>(r2, r3, r4, r5);
            r13.add(r14);
            goto L_0x05b3;
        L_0x05ae:
            r17 = r11;
            r11 = 3;
            r18 = 4;
        L_0x05b3:
            r6 = r6 + 1;
            r11 = r17;
            r1 = 3;
            r4 = 4;
            goto L_0x0584;
        L_0x05ba:
            r17 = r11;
            r11 = 3;
            r18 = 4;
            r14 = 1;
        L_0x05c0:
            r0 = r12.length;
            r1 = 1;
            r0 = r0 - r1;
            if (r14 >= r0) goto L_0x0612;
        L_0x05c5:
            r15 = 1;
        L_0x05c6:
            r0 = r12.length;
            r0 = r0 - r14;
            if (r15 >= r0) goto L_0x060c;
        L_0x05ca:
            r0 = r12.length;
            r0 = r0 - r14;
            r4 = r0 - r15;
            if (r14 > r11) goto L_0x0604;
        L_0x05d0:
            r0 = 1062836634; // 0x3f59999a float:0.85 double:5.25111068E-315;
            r0 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1));
            if (r0 >= 0) goto L_0x05d9;
        L_0x05d7:
            r0 = 4;
            goto L_0x05da;
        L_0x05d9:
            r0 = 3;
        L_0x05da:
            if (r15 > r0) goto L_0x0604;
        L_0x05dc:
            if (r4 <= r11) goto L_0x05df;
        L_0x05de:
            goto L_0x0604;
        L_0x05df:
            r6 = new org.telegram.messenger.MessageObject$GroupedMessages$MessageGroupedLayoutAttempt;
            r0 = 0;
            r5 = r10.multiHeight(r12, r0, r14);
            r0 = r14 + r15;
            r19 = r10.multiHeight(r12, r14, r0);
            r1 = r12.length;
            r20 = r10.multiHeight(r12, r0, r1);
            r0 = r6;
            r1 = r36;
            r2 = r14;
            r3 = r15;
            r11 = r6;
            r6 = r19;
            r27 = r7;
            r7 = r20;
            r0.<init>(r2, r3, r4, r5, r6, r7);
            r13.add(r11);
            goto L_0x0606;
        L_0x0604:
            r27 = r7;
        L_0x0606:
            r15 = r15 + 1;
            r7 = r27;
            r11 = 3;
            goto L_0x05c6;
        L_0x060c:
            r27 = r7;
            r14 = r14 + 1;
            r11 = 3;
            goto L_0x05c0;
        L_0x0612:
            r27 = r7;
            r11 = 1;
        L_0x0615:
            r0 = r12.length;
            r1 = 2;
            r0 = r0 - r1;
            if (r11 >= r0) goto L_0x067b;
        L_0x061a:
            r14 = 1;
        L_0x061b:
            r0 = r12.length;
            r0 = r0 - r11;
            if (r14 >= r0) goto L_0x0674;
        L_0x061f:
            r15 = 1;
        L_0x0620:
            r0 = r12.length;
            r0 = r0 - r11;
            r0 = r0 - r14;
            if (r15 >= r0) goto L_0x066d;
        L_0x0625:
            r0 = r12.length;
            r0 = r0 - r11;
            r0 = r0 - r14;
            r5 = r0 - r15;
            r0 = 3;
            if (r11 > r0) goto L_0x0662;
        L_0x062d:
            if (r14 > r0) goto L_0x0662;
        L_0x062f:
            if (r15 > r0) goto L_0x0662;
        L_0x0631:
            if (r5 <= r0) goto L_0x0634;
        L_0x0633:
            goto L_0x0662;
        L_0x0634:
            r8 = new org.telegram.messenger.MessageObject$GroupedMessages$MessageGroupedLayoutAttempt;
            r0 = 0;
            r6 = r10.multiHeight(r12, r0, r11);
            r0 = r11 + r14;
            r7 = r10.multiHeight(r12, r11, r0);
            r1 = r0 + r15;
            r19 = r10.multiHeight(r12, r0, r1);
            r0 = r12.length;
            r20 = r10.multiHeight(r12, r1, r0);
            r0 = r8;
            r1 = r36;
            r2 = r11;
            r3 = r14;
            r4 = r15;
            r22 = r12;
            r12 = r8;
            r8 = r19;
            r28 = r9;
            r9 = r20;
            r0.<init>(r2, r3, r4, r5, r6, r7, r8, r9);
            r13.add(r12);
            goto L_0x0666;
        L_0x0662:
            r28 = r9;
            r22 = r12;
        L_0x0666:
            r15 = r15 + 1;
            r12 = r22;
            r9 = r28;
            goto L_0x0620;
        L_0x066d:
            r28 = r9;
            r22 = r12;
            r14 = r14 + 1;
            goto L_0x061b;
        L_0x0674:
            r28 = r9;
            r22 = r12;
            r11 = r11 + 1;
            goto L_0x0615;
        L_0x067b:
            r28 = r9;
            r22 = r12;
            r0 = 0;
            r1 = 0;
            r2 = r10.maxSizeWidth;
            r3 = 3;
            r2 = r2 / r3;
            r2 = r2 * 4;
            r2 = (float) r2;
            r1 = r0;
            r0 = 0;
            r3 = 0;
        L_0x068b:
            r4 = r13.size();
            if (r0 >= r4) goto L_0x06fb;
        L_0x0691:
            r4 = r13.get(r0);
            r4 = (org.telegram.messenger.MessageObject.GroupedMessages.MessageGroupedLayoutAttempt) r4;
            r5 = 0;
            r6 = 2139095039; // 0x7f7fffff float:3.4028235E38 double:1.056853372E-314;
            r5 = 0;
            r6 = 0;
            r7 = 2139095039; // 0x7f7fffff float:3.4028235E38 double:1.056853372E-314;
        L_0x06a0:
            r8 = r4.heights;
            r9 = r8.length;
            if (r5 >= r9) goto L_0x06b3;
        L_0x06a5:
            r9 = r8[r5];
            r6 = r6 + r9;
            r9 = r8[r5];
            r9 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1));
            if (r9 >= 0) goto L_0x06b0;
        L_0x06ae:
            r7 = r8[r5];
        L_0x06b0:
            r5 = r5 + 1;
            goto L_0x06a0;
        L_0x06b3:
            r6 = r6 - r2;
            r5 = java.lang.Math.abs(r6);
            r6 = r4.lineCounts;
            r8 = r6.length;
            r9 = 1;
            if (r8 <= r9) goto L_0x06e0;
        L_0x06be:
            r8 = 0;
            r11 = r6[r8];
            r12 = r6[r9];
            if (r11 > r12) goto L_0x06dc;
        L_0x06c5:
            r11 = r6.length;
            r12 = 2;
            if (r11 <= r12) goto L_0x06cf;
        L_0x06c9:
            r11 = r6[r9];
            r6 = r6[r12];
            if (r11 > r6) goto L_0x06dc;
        L_0x06cf:
            r6 = r4.lineCounts;
            r9 = r6.length;
            r11 = 3;
            if (r9 <= r11) goto L_0x06e2;
        L_0x06d5:
            r9 = r6[r12];
            r6 = r6[r11];
            if (r9 <= r6) goto L_0x06e2;
        L_0x06db:
            goto L_0x06dd;
        L_0x06dc:
            r11 = 3;
        L_0x06dd:
            r5 = r5 * r16;
            goto L_0x06e2;
        L_0x06e0:
            r8 = 0;
            r11 = 3;
        L_0x06e2:
            r6 = r5;
            r5 = r28;
            r9 = (float) r5;
            r7 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
            if (r7 >= 0) goto L_0x06ee;
        L_0x06ea:
            r7 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
            r6 = r6 * r7;
        L_0x06ee:
            if (r1 == 0) goto L_0x06f4;
        L_0x06f0:
            r7 = (r6 > r3 ? 1 : (r6 == r3 ? 0 : -1));
            if (r7 >= 0) goto L_0x06f6;
        L_0x06f4:
            r1 = r4;
            r3 = r6;
        L_0x06f6:
            r0 = r0 + 1;
            r28 = r5;
            goto L_0x068b;
        L_0x06fb:
            r8 = 0;
            if (r1 != 0) goto L_0x06ff;
        L_0x06fe:
            return;
        L_0x06ff:
            r0 = 0;
            r2 = 0;
            r12 = 0;
        L_0x0702:
            r3 = r1.lineCounts;
            r4 = r3.length;
            if (r0 >= r4) goto L_0x077d;
        L_0x0707:
            r3 = r3[r0];
            r4 = r1.heights;
            r4 = r4[r0];
            r5 = r10.maxSizeWidth;
            r6 = 0;
            r7 = r3 + -1;
            r12 = java.lang.Math.max(r12, r7);
            r9 = r2;
            r2 = 0;
        L_0x0718:
            if (r2 >= r3) goto L_0x076a;
        L_0x071a:
            r11 = r22[r9];
            r11 = r11 * r4;
            r11 = (int) r11;
            r5 = r5 - r11;
            r13 = r10.posArray;
            r13 = r13.get(r9);
            r28 = r13;
            r28 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r28;
            if (r0 != 0) goto L_0x072e;
        L_0x072c:
            r13 = 4;
            goto L_0x072f;
        L_0x072e:
            r13 = 0;
        L_0x072f:
            r14 = r1.lineCounts;
            r14 = r14.length;
            r15 = 1;
            r14 = r14 - r15;
            if (r0 != r14) goto L_0x0738;
        L_0x0736:
            r13 = r13 | 8;
        L_0x0738:
            if (r2 != 0) goto L_0x0740;
        L_0x073a:
            r13 = r13 | 1;
            if (r26 == 0) goto L_0x0740;
        L_0x073e:
            r6 = r28;
        L_0x0740:
            if (r2 != r7) goto L_0x074b;
        L_0x0742:
            r13 = r13 | 2;
            if (r26 != 0) goto L_0x074b;
        L_0x0746:
            r35 = r13;
            r6 = r28;
            goto L_0x074d;
        L_0x074b:
            r35 = r13;
        L_0x074d:
            r13 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r14 = r4 / r13;
            r15 = r27;
            r34 = java.lang.Math.max(r15, r14);
            r29 = r2;
            r30 = r2;
            r31 = r0;
            r32 = r0;
            r33 = r11;
            r28.set(r29, r30, r31, r32, r33, r34, r35);
            r9 = r9 + 1;
            r2 = r2 + 1;
            goto L_0x0718;
        L_0x076a:
            r15 = r27;
            r13 = 1145798656; // 0x444b8000 float:814.0 double:5.66099753E-315;
            r2 = r6.f57pw;
            r2 = r2 + r5;
            r6.f57pw = r2;
            r2 = r6.spanSize;
            r2 = r2 + r5;
            r6.spanSize = r2;
            r0 = r0 + 1;
            r2 = r9;
            goto L_0x0702;
        L_0x077d:
            r0 = r17;
        L_0x077f:
            if (r8 >= r0) goto L_0x07fd;
        L_0x0781:
            r1 = r10.posArray;
            r1 = r1.get(r8);
            r1 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r1;
            if (r26 == 0) goto L_0x07a1;
        L_0x078b:
            r2 = r1.minX;
            if (r2 != 0) goto L_0x0796;
        L_0x078f:
            r2 = r1.spanSize;
            r3 = r10.firstSpanAdditionalSize;
            r2 = r2 + r3;
            r1.spanSize = r2;
        L_0x0796:
            r2 = r1.flags;
            r3 = 2;
            r2 = r2 & r3;
            if (r2 == 0) goto L_0x079f;
        L_0x079c:
            r2 = 1;
            r1.edge = r2;
        L_0x079f:
            r3 = 1;
            goto L_0x07ba;
        L_0x07a1:
            r3 = 2;
            r2 = r1.maxX;
            if (r2 == r12) goto L_0x07ab;
        L_0x07a6:
            r2 = r1.flags;
            r2 = r2 & r3;
            if (r2 == 0) goto L_0x07b2;
        L_0x07ab:
            r2 = r1.spanSize;
            r3 = r10.firstSpanAdditionalSize;
            r2 = r2 + r3;
            r1.spanSize = r2;
        L_0x07b2:
            r2 = r1.flags;
            r3 = 1;
            r2 = r2 & r3;
            if (r2 == 0) goto L_0x07ba;
        L_0x07b8:
            r1.edge = r3;
        L_0x07ba:
            r2 = r10.messages;
            r2 = r2.get(r8);
            r2 = (org.telegram.messenger.MessageObject) r2;
            if (r26 != 0) goto L_0x07f9;
        L_0x07c4:
            r2 = r2.needDrawAvatarInternal();
            if (r2 == 0) goto L_0x07f9;
        L_0x07ca:
            r2 = r1.edge;
            if (r2 == 0) goto L_0x07df;
        L_0x07ce:
            r2 = r1.spanSize;
            r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            if (r2 == r4) goto L_0x07d8;
        L_0x07d4:
            r2 = r2 + 108;
            r1.spanSize = r2;
        L_0x07d8:
            r2 = r1.f57pw;
            r2 = r2 + 108;
            r1.f57pw = r2;
            goto L_0x07f9;
        L_0x07df:
            r2 = r1.flags;
            r4 = 2;
            r2 = r2 & r4;
            if (r2 == 0) goto L_0x07fa;
        L_0x07e5:
            r2 = r1.spanSize;
            r5 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            if (r2 == r5) goto L_0x07f0;
        L_0x07eb:
            r2 = r2 + -108;
            r1.spanSize = r2;
            goto L_0x07fa;
        L_0x07f0:
            r2 = r1.leftSpanOffset;
            if (r2 == 0) goto L_0x07fa;
        L_0x07f4:
            r2 = r2 + 108;
            r1.leftSpanOffset = r2;
            goto L_0x07fa;
        L_0x07f9:
            r4 = 2;
        L_0x07fa:
            r8 = r8 + 1;
            goto L_0x077f;
        L_0x07fd:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject$GroupedMessages.calculate():void");
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
            byte b = this.directionFlags;
            return (b & 1) != 0 && (b & 2) == 0;
        }
    }

    public static class VCardData {
        private String company;
        private ArrayList<String> emails = new ArrayList();
        private ArrayList<String> phones = new ArrayList();

        public static CharSequence parse(String str) {
            int i;
            Object obj;
            VCardData vCardData;
            try {
                BufferedReader bufferedReader = new BufferedReader(new StringReader(str));
                i = 0;
                obj = null;
                vCardData = null;
                String str2 = null;
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    } else if (!readLine.startsWith("PHOTO")) {
                        if (readLine.indexOf(58) >= 0) {
                            if (readLine.startsWith("BEGIN:VCARD")) {
                                vCardData = new VCardData();
                            } else if (readLine.startsWith("END:VCARD") && vCardData != null) {
                                obj = 1;
                            }
                        }
                        if (str2 != null) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(str2);
                            stringBuilder.append(readLine);
                            readLine = stringBuilder.toString();
                            str2 = null;
                        }
                        String str3 = "=";
                        if (readLine.contains("=QUOTED-PRINTABLE")) {
                            if (readLine.endsWith(str3)) {
                                str2 = readLine.substring(0, readLine.length() - 1);
                            }
                        }
                        int i2 = 2;
                        String[] strArr = readLine.indexOf(":") >= 0 ? new String[]{readLine.substring(0, readLine.indexOf(":")), readLine.substring(readLine.indexOf(":") + 1).trim()} : new String[]{readLine.trim()};
                        if (strArr.length >= 2) {
                            if (vCardData != null) {
                                if (strArr[0].startsWith("ORG")) {
                                    String[] split = strArr[0].split(";");
                                    int length = split.length;
                                    int i3 = 0;
                                    String str4 = null;
                                    String str5 = null;
                                    while (i3 < length) {
                                        String[] split2 = split[i3].split(str3);
                                        if (split2.length == i2) {
                                            if (split2[0].equals("CHARSET")) {
                                                str5 = split2[1];
                                            } else if (split2[0].equals("ENCODING")) {
                                                str4 = split2[1];
                                            }
                                        }
                                        i3++;
                                        i2 = 2;
                                    }
                                    vCardData.company = strArr[1];
                                    if (str4 != null && str4.equalsIgnoreCase("QUOTED-PRINTABLE")) {
                                        byte[] decodeQuotedPrintable = AndroidUtilities.decodeQuotedPrintable(AndroidUtilities.getStringBytes(vCardData.company));
                                        if (!(decodeQuotedPrintable == null || decodeQuotedPrintable.length == 0)) {
                                            vCardData.company = new String(decodeQuotedPrintable, str5);
                                        }
                                    }
                                    vCardData.company = vCardData.company.replace(';', ' ');
                                } else if (strArr[0].startsWith("TEL")) {
                                    if (strArr[1].length() > 0) {
                                        vCardData.phones.add(strArr[1]);
                                    }
                                } else if (strArr[0].startsWith("EMAIL")) {
                                    String str6 = strArr[1];
                                    if (str6.length() > 0) {
                                        vCardData.emails.add(str6);
                                    }
                                }
                            }
                        }
                    }
                }
                bufferedReader.close();
            } catch (Exception e) {
                FileLog.m30e(e);
            } catch (Throwable unused) {
            }
            if (obj != null) {
                StringBuilder stringBuilder2 = new StringBuilder();
                for (int i4 = 0; i4 < vCardData.phones.size(); i4++) {
                    if (stringBuilder2.length() > 0) {
                        stringBuilder2.append(10);
                    }
                    String str7 = (String) vCardData.phones.get(i4);
                    if (!str7.contains("#")) {
                        if (!str7.contains("*")) {
                            stringBuilder2.append(C0278PhoneFormat.getInstance().format(str7));
                        }
                    }
                    stringBuilder2.append(str7);
                }
                while (i < vCardData.emails.size()) {
                    if (stringBuilder2.length() > 0) {
                        stringBuilder2.append(10);
                    }
                    stringBuilder2.append(C0278PhoneFormat.getInstance().format((String) vCardData.emails.get(i)));
                    i++;
                }
                if (!TextUtils.isEmpty(vCardData.company)) {
                    if (stringBuilder2.length() > 0) {
                        stringBuilder2.append(10);
                    }
                    stringBuilder2.append(vCardData.company);
                }
                return stringBuilder2;
            }
            return null;
        }
    }

    public void checkForScam() {
    }

    public MessageObject(int i, Message message, String str, String str2, String str3, boolean z, boolean z2, boolean z3) {
        this.type = 1000;
        this.localType = z ? 2 : 1;
        this.currentAccount = i;
        this.localName = str2;
        this.localUserName = str3;
        this.messageText = str;
        this.messageOwner = message;
        this.localChannel = z2;
        this.localEdit = z3;
    }

    public MessageObject(int i, Message message, AbstractMap<Integer, User> abstractMap, boolean z) {
        this(i, message, (AbstractMap) abstractMap, null, z);
    }

    public MessageObject(int i, Message message, SparseArray<User> sparseArray, boolean z) {
        this(i, message, (SparseArray) sparseArray, null, z);
    }

    public MessageObject(int i, Message message, boolean z) {
        this(i, message, null, null, null, null, z, 0);
    }

    public MessageObject(int i, Message message, AbstractMap<Integer, User> abstractMap, AbstractMap<Integer, Chat> abstractMap2, boolean z) {
        this(i, message, (AbstractMap) abstractMap, (AbstractMap) abstractMap2, z, 0);
    }

    public MessageObject(int i, Message message, SparseArray<User> sparseArray, SparseArray<Chat> sparseArray2, boolean z) {
        this(i, message, null, null, (SparseArray) sparseArray, (SparseArray) sparseArray2, z, 0);
    }

    public MessageObject(int i, Message message, AbstractMap<Integer, User> abstractMap, AbstractMap<Integer, Chat> abstractMap2, boolean z, long j) {
        this(i, message, (AbstractMap) abstractMap, (AbstractMap) abstractMap2, null, null, z, j);
    }

    public MessageObject(int i, Message message, AbstractMap<Integer, User> abstractMap, AbstractMap<Integer, Chat> abstractMap2, SparseArray<User> sparseArray, SparseArray<Chat> sparseArray2, boolean z, long j) {
        User user;
        int i2;
        int i3;
        int i4;
        int i5;
        Message message2 = message;
        AbstractMap<Integer, User> abstractMap3 = abstractMap;
        AbstractMap<Integer, Chat> abstractMap4 = abstractMap2;
        SparseArray<User> sparseArray3 = sparseArray;
        SparseArray<Chat> sparseArray4 = sparseArray2;
        boolean z2 = z;
        this.type = 1000;
        Theme.createChatResources(null, true);
        this.currentAccount = i;
        this.messageOwner = message2;
        this.eventId = j;
        Message message3 = message2.replyMessage;
        int i6;
        if (message3 != null) {
            MessageObject messageObject = r7;
            i6 = 1;
            MessageObject messageObject2 = new MessageObject(i, message3, (AbstractMap) abstractMap, (AbstractMap) abstractMap2, (SparseArray) sparseArray, (SparseArray) sparseArray2, false, j);
            this.replyMessageObject = messageObject;
        } else {
            i6 = 1;
        }
        int i7 = message2.from_id;
        if (i7 > 0) {
            user = abstractMap3 != null ? (User) abstractMap3.get(Integer.valueOf(i7)) : sparseArray3 != null ? (User) sparseArray3.get(i7) : null;
            if (user == null) {
                user = MessagesController.getInstance(i).getUser(Integer.valueOf(message2.from_id));
            }
        } else {
            user = null;
        }
        String str = "";
        String formatCallDuration;
        if (message2 instanceof TL_messageService) {
            MessageAction messageAction = message2.action;
            if (messageAction != null) {
                if (messageAction instanceof TL_messageActionCustomAction) {
                    this.messageText = messageAction.message;
                } else {
                    String str2 = "un1";
                    if (!(messageAction instanceof TL_messageActionChatCreate)) {
                        String str3 = "un2";
                        String str4;
                        if (messageAction instanceof TL_messageActionChatDeleteUser) {
                            i2 = messageAction.user_id;
                            if (i2 != message2.from_id) {
                                TLObject tLObject = abstractMap3 != null ? (User) abstractMap3.get(Integer.valueOf(i2)) : sparseArray3 != null ? (User) sparseArray3.get(i2) : null;
                                if (tLObject == null) {
                                    tLObject = MessagesController.getInstance(i).getUser(Integer.valueOf(message2.action.user_id));
                                }
                                if (isOut()) {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionYouKickUser", C1067R.string.ActionYouKickUser), str3, tLObject);
                                } else if (message2.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionKickUserYou", C1067R.string.ActionKickUserYou), str2, user);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionKickUser", C1067R.string.ActionKickUser), str3, tLObject);
                                    this.messageText = replaceWithLink(this.messageText, str2, user);
                                }
                            } else if (isOut()) {
                                this.messageText = LocaleController.getString("ActionYouLeftUser", C1067R.string.ActionYouLeftUser);
                            } else {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionLeftUser", C1067R.string.ActionLeftUser), str2, user);
                            }
                        } else if (messageAction instanceof TL_messageActionChatAddUser) {
                            boolean z3;
                            MessageAction messageAction2 = this.messageOwner.action;
                            i3 = messageAction2.user_id;
                            if (i3 == 0 && messageAction2.users.size() == i6) {
                                i3 = ((Integer) this.messageOwner.action.users.get(0)).intValue();
                            }
                            String str5 = "ActionYouAddUser";
                            str4 = "ActionAddUser";
                            if (i3 != 0) {
                                TLObject tLObject2 = abstractMap3 != null ? (User) abstractMap3.get(Integer.valueOf(i3)) : sparseArray3 != null ? (User) sparseArray3.get(i3) : null;
                                if (tLObject2 == null) {
                                    tLObject2 = MessagesController.getInstance(i).getUser(Integer.valueOf(i3));
                                }
                                if (i3 == message2.from_id) {
                                    if (message2.to_id.channel_id != 0 && !isMegagroup()) {
                                        this.messageText = LocaleController.getString("ChannelJoined", C1067R.string.ChannelJoined);
                                    } else if (message2.to_id.channel_id == 0 || !isMegagroup()) {
                                        if (isOut()) {
                                            this.messageText = LocaleController.getString("ActionAddUserSelfYou", C1067R.string.ActionAddUserSelfYou);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserSelf", C1067R.string.ActionAddUserSelf), str2, user);
                                        }
                                    } else if (i3 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                        this.messageText = LocaleController.getString("ChannelMegaJoined", C1067R.string.ChannelMegaJoined);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserSelfMega", C1067R.string.ActionAddUserSelfMega), str2, user);
                                    }
                                } else if (isOut()) {
                                    this.messageText = replaceWithLink(LocaleController.getString(str5, C1067R.string.ActionYouAddUser), str3, tLObject2);
                                } else if (i3 != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                    this.messageText = replaceWithLink(LocaleController.getString(str4, C1067R.string.ActionAddUser), str3, tLObject2);
                                    this.messageText = replaceWithLink(this.messageText, str2, user);
                                } else if (message2.to_id.channel_id == 0) {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserYou", C1067R.string.ActionAddUserYou), str2, user);
                                } else if (isMegagroup()) {
                                    this.messageText = replaceWithLink(LocaleController.getString("MegaAddedBy", C1067R.string.MegaAddedBy), str2, user);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ChannelAddedBy", C1067R.string.ChannelAddedBy), str2, user);
                                }
                                z3 = z;
                            } else if (isOut()) {
                                z3 = z;
                                this.messageText = replaceWithLink(LocaleController.getString(str5, C1067R.string.ActionYouAddUser), "un2", message2.action.users, abstractMap, sparseArray);
                            } else {
                                z3 = z;
                                this.messageText = replaceWithLink(LocaleController.getString(str4, C1067R.string.ActionAddUser), "un2", message2.action.users, abstractMap, sparseArray);
                                this.messageText = replaceWithLink(this.messageText, str2, user);
                                z2 = z3;
                                i4 = 1;
                            }
                            z2 = z3;
                            i4 = 1;
                        } else {
                            z2 = z;
                            i4 = 1;
                            if (messageAction instanceof TL_messageActionChatJoinedByLink) {
                                if (isOut()) {
                                    this.messageText = LocaleController.getString("ActionInviteYou", C1067R.string.ActionInviteYou);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionInviteUser", C1067R.string.ActionInviteUser), str2, user);
                                }
                            } else if (messageAction instanceof TL_messageActionChatEditPhoto) {
                                if (message2.to_id.channel_id != 0 && !isMegagroup()) {
                                    this.messageText = LocaleController.getString("ActionChannelChangedPhoto", C1067R.string.ActionChannelChangedPhoto);
                                } else if (isOut()) {
                                    this.messageText = LocaleController.getString("ActionYouChangedPhoto", C1067R.string.ActionYouChangedPhoto);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionChangedPhoto", C1067R.string.ActionChangedPhoto), str2, user);
                                }
                            } else if (messageAction instanceof TL_messageActionChatEditTitle) {
                                if (message2.to_id.channel_id != 0 && !isMegagroup()) {
                                    this.messageText = LocaleController.getString("ActionChannelChangedTitle", C1067R.string.ActionChannelChangedTitle).replace(str3, message2.action.title);
                                } else if (isOut()) {
                                    this.messageText = LocaleController.getString("ActionYouChangedTitle", C1067R.string.ActionYouChangedTitle).replace(str3, message2.action.title);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionChangedTitle", C1067R.string.ActionChangedTitle).replace(str3, message2.action.title), str2, user);
                                }
                            } else if (!(messageAction instanceof TL_messageActionChatDeletePhoto)) {
                                str4 = "MessageLifetimeRemoved";
                                String stringBuilder;
                                String firstName;
                                if (messageAction instanceof TL_messageActionTTLChange) {
                                    if (messageAction.ttl != 0) {
                                        if (isOut()) {
                                            this.messageText = LocaleController.formatString("MessageLifetimeChangedOutgoing", C1067R.string.MessageLifetimeChangedOutgoing, LocaleController.formatTTLString(message2.action.ttl));
                                        } else {
                                            this.messageText = LocaleController.formatString("MessageLifetimeChanged", C1067R.string.MessageLifetimeChanged, UserObject.getFirstName(user), LocaleController.formatTTLString(message2.action.ttl));
                                        }
                                    } else if (isOut()) {
                                        this.messageText = LocaleController.getString("MessageLifetimeYouRemoved", C1067R.string.MessageLifetimeYouRemoved);
                                    } else {
                                        this.messageText = LocaleController.formatString(str4, C1067R.string.MessageLifetimeRemoved, UserObject.getFirstName(user));
                                    }
                                } else if (messageAction instanceof TL_messageActionLoginUnknownLocation) {
                                    long j2 = ((long) message2.date) * 1000;
                                    if (LocaleController.getInstance().formatterDay == null || LocaleController.getInstance().formatterYear == null) {
                                        StringBuilder stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append(str);
                                        stringBuilder2.append(message2.date);
                                        stringBuilder = stringBuilder2.toString();
                                    } else {
                                        stringBuilder = LocaleController.formatString("formatDateAtTime", C1067R.string.formatDateAtTime, LocaleController.getInstance().formatterYear.format(j2), LocaleController.getInstance().formatterDay.format(j2));
                                    }
                                    User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                                    if (currentUser == null) {
                                        if (abstractMap3 != null) {
                                            currentUser = (User) abstractMap3.get(Integer.valueOf(this.messageOwner.to_id.user_id));
                                        } else if (sparseArray3 != null) {
                                            currentUser = (User) sparseArray3.get(this.messageOwner.to_id.user_id);
                                        }
                                        if (currentUser == null) {
                                            currentUser = MessagesController.getInstance(i).getUser(Integer.valueOf(this.messageOwner.to_id.user_id));
                                        }
                                    }
                                    firstName = currentUser != null ? UserObject.getFirstName(currentUser) : str;
                                    r5 = new Object[4];
                                    MessageAction messageAction3 = message2.action;
                                    r5[2] = messageAction3.title;
                                    r5[3] = messageAction3.address;
                                    this.messageText = LocaleController.formatString("NotificationUnrecognizedDevice", C1067R.string.NotificationUnrecognizedDevice, r5);
                                } else if ((messageAction instanceof TL_messageActionUserJoined) || (messageAction instanceof TL_messageActionContactSignUp)) {
                                    this.messageText = LocaleController.formatString("NotificationContactJoined", C1067R.string.NotificationContactJoined, UserObject.getUserName(user));
                                } else if (messageAction instanceof TL_messageActionUserUpdatedPhoto) {
                                    this.messageText = LocaleController.formatString("NotificationContactNewPhoto", C1067R.string.NotificationContactNewPhoto, UserObject.getUserName(user));
                                } else if (messageAction instanceof TL_messageEncryptedAction) {
                                    DecryptedMessageAction decryptedMessageAction = messageAction.encryptedAction;
                                    if (decryptedMessageAction instanceof TL_decryptedMessageActionScreenshotMessages) {
                                        if (isOut()) {
                                            this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", C1067R.string.ActionTakeScreenshootYou, new Object[0]);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", C1067R.string.ActionTakeScreenshoot), str2, user);
                                        }
                                    } else if (decryptedMessageAction instanceof TL_decryptedMessageActionSetMessageTTL) {
                                        if (((TL_decryptedMessageActionSetMessageTTL) decryptedMessageAction).ttl_seconds != 0) {
                                            if (isOut()) {
                                                this.messageText = LocaleController.formatString("MessageLifetimeChangedOutgoing", C1067R.string.MessageLifetimeChangedOutgoing, LocaleController.formatTTLString(r0.ttl_seconds));
                                            } else {
                                                this.messageText = LocaleController.formatString("MessageLifetimeChanged", C1067R.string.MessageLifetimeChanged, UserObject.getFirstName(user), LocaleController.formatTTLString(r0.ttl_seconds));
                                            }
                                        } else if (isOut()) {
                                            this.messageText = LocaleController.getString("MessageLifetimeYouRemoved", C1067R.string.MessageLifetimeYouRemoved);
                                        } else {
                                            this.messageText = LocaleController.formatString(str4, C1067R.string.MessageLifetimeRemoved, UserObject.getFirstName(user));
                                        }
                                    }
                                } else if (messageAction instanceof TL_messageActionScreenshotTaken) {
                                    if (isOut()) {
                                        this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", C1067R.string.ActionTakeScreenshootYou, new Object[0]);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", C1067R.string.ActionTakeScreenshoot), str2, user);
                                    }
                                } else if (messageAction instanceof TL_messageActionCreatedBroadcastList) {
                                    this.messageText = LocaleController.formatString("YouCreatedBroadcastList", C1067R.string.YouCreatedBroadcastList, new Object[0]);
                                } else if (messageAction instanceof TL_messageActionChannelCreate) {
                                    if (isMegagroup()) {
                                        this.messageText = LocaleController.getString("ActionCreateMega", C1067R.string.ActionCreateMega);
                                    } else {
                                        this.messageText = LocaleController.getString("ActionCreateChannel", C1067R.string.ActionCreateChannel);
                                    }
                                } else if (messageAction instanceof TL_messageActionChatMigrateTo) {
                                    this.messageText = LocaleController.getString("ActionMigrateFromGroup", C1067R.string.ActionMigrateFromGroup);
                                } else if (messageAction instanceof TL_messageActionChannelMigrateFrom) {
                                    this.messageText = LocaleController.getString("ActionMigrateFromGroup", C1067R.string.ActionMigrateFromGroup);
                                } else if (messageAction instanceof TL_messageActionPinMessage) {
                                    Chat chat;
                                    if (user == null) {
                                        if (abstractMap4 != null) {
                                            chat = (Chat) abstractMap4.get(Integer.valueOf(message2.to_id.channel_id));
                                        } else if (sparseArray4 != null) {
                                            chat = (Chat) sparseArray4.get(message2.to_id.channel_id);
                                        }
                                        generatePinMessageText(user, chat);
                                    }
                                    chat = null;
                                    generatePinMessageText(user, chat);
                                } else if (messageAction instanceof TL_messageActionHistoryClear) {
                                    this.messageText = LocaleController.getString("HistoryCleared", C1067R.string.HistoryCleared);
                                } else if (messageAction instanceof TL_messageActionGameScore) {
                                    generateGameMessageText(user);
                                } else if (messageAction instanceof TL_messageActionPhoneCall) {
                                    message2 = this.messageOwner;
                                    TL_messageActionPhoneCall tL_messageActionPhoneCall = (TL_messageActionPhoneCall) message2.action;
                                    boolean z4 = tL_messageActionPhoneCall.reason instanceof TL_phoneCallDiscardReasonMissed;
                                    if (message2.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                        if (z4) {
                                            this.messageText = LocaleController.getString("CallMessageOutgoingMissed", C1067R.string.CallMessageOutgoingMissed);
                                        } else {
                                            this.messageText = LocaleController.getString("CallMessageOutgoing", C1067R.string.CallMessageOutgoing);
                                        }
                                    } else if (z4) {
                                        this.messageText = LocaleController.getString("CallMessageIncomingMissed", C1067R.string.CallMessageIncomingMissed);
                                    } else if (tL_messageActionPhoneCall.reason instanceof TL_phoneCallDiscardReasonBusy) {
                                        this.messageText = LocaleController.getString("CallMessageIncomingDeclined", C1067R.string.CallMessageIncomingDeclined);
                                    } else {
                                        this.messageText = LocaleController.getString("CallMessageIncoming", C1067R.string.CallMessageIncoming);
                                    }
                                    i5 = tL_messageActionPhoneCall.duration;
                                    if (i5 > 0) {
                                        formatCallDuration = LocaleController.formatCallDuration(i5);
                                        this.messageText = LocaleController.formatString("CallMessageWithDuration", C1067R.string.CallMessageWithDuration, this.messageText, formatCallDuration);
                                        stringBuilder = this.messageText.toString();
                                        i3 = stringBuilder.indexOf(formatCallDuration);
                                        if (i3 != -1) {
                                            SpannableString spannableString = new SpannableString(this.messageText);
                                            i5 = formatCallDuration.length() + i3;
                                            if (i3 > 0 && stringBuilder.charAt(i3 - 1) == '(') {
                                                i3--;
                                            }
                                            if (i5 < stringBuilder.length() && stringBuilder.charAt(i5) == ')') {
                                                i5++;
                                            }
                                            spannableString.setSpan(new TypefaceSpan(Typeface.DEFAULT), i3, i5, 0);
                                            this.messageText = spannableString;
                                        }
                                    }
                                } else if (messageAction instanceof TL_messageActionPaymentSent) {
                                    i2 = (int) getDialogId();
                                    if (abstractMap3 != null) {
                                        user = (User) abstractMap3.get(Integer.valueOf(i2));
                                    } else if (sparseArray3 != null) {
                                        user = (User) sparseArray3.get(i2);
                                    }
                                    if (user == null) {
                                        user = MessagesController.getInstance(i).getUser(Integer.valueOf(i2));
                                    }
                                    generatePaymentSentMessageText(null);
                                } else if (messageAction instanceof TL_messageActionBotAllowed) {
                                    formatCallDuration = ((TL_messageActionBotAllowed) messageAction).domain;
                                    firstName = LocaleController.getString("ActionBotAllowed", C1067R.string.ActionBotAllowed);
                                    int indexOf = firstName.indexOf("%1$s");
                                    SpannableString spannableString2 = new SpannableString(String.format(firstName, new Object[]{formatCallDuration}));
                                    if (indexOf >= 0) {
                                        StringBuilder stringBuilder3 = new StringBuilder();
                                        stringBuilder3.append("http://");
                                        stringBuilder3.append(formatCallDuration);
                                        spannableString2.setSpan(new URLSpanNoUnderlineBold(stringBuilder3.toString()), indexOf, formatCallDuration.length() + indexOf, 33);
                                    }
                                    this.messageText = spannableString2;
                                } else if (messageAction instanceof TL_messageActionSecureValuesSent) {
                                    User user2;
                                    TL_messageActionSecureValuesSent tL_messageActionSecureValuesSent = (TL_messageActionSecureValuesSent) messageAction;
                                    StringBuilder stringBuilder4 = new StringBuilder();
                                    int size = tL_messageActionSecureValuesSent.types.size();
                                    for (int i8 = 0; i8 < size; i8++) {
                                        SecureValueType secureValueType = (SecureValueType) tL_messageActionSecureValuesSent.types.get(i8);
                                        if (stringBuilder4.length() > 0) {
                                            stringBuilder4.append(", ");
                                        }
                                        if (secureValueType instanceof TL_secureValueTypePhone) {
                                            stringBuilder4.append(LocaleController.getString("ActionBotDocumentPhone", C1067R.string.ActionBotDocumentPhone));
                                        } else if (secureValueType instanceof TL_secureValueTypeEmail) {
                                            stringBuilder4.append(LocaleController.getString("ActionBotDocumentEmail", C1067R.string.ActionBotDocumentEmail));
                                        } else if (secureValueType instanceof TL_secureValueTypeAddress) {
                                            stringBuilder4.append(LocaleController.getString("ActionBotDocumentAddress", C1067R.string.ActionBotDocumentAddress));
                                        } else if (secureValueType instanceof TL_secureValueTypePersonalDetails) {
                                            stringBuilder4.append(LocaleController.getString("ActionBotDocumentIdentity", C1067R.string.ActionBotDocumentIdentity));
                                        } else if (secureValueType instanceof TL_secureValueTypePassport) {
                                            stringBuilder4.append(LocaleController.getString("ActionBotDocumentPassport", C1067R.string.ActionBotDocumentPassport));
                                        } else if (secureValueType instanceof TL_secureValueTypeDriverLicense) {
                                            stringBuilder4.append(LocaleController.getString("ActionBotDocumentDriverLicence", C1067R.string.ActionBotDocumentDriverLicence));
                                        } else if (secureValueType instanceof TL_secureValueTypeIdentityCard) {
                                            stringBuilder4.append(LocaleController.getString("ActionBotDocumentIdentityCard", C1067R.string.ActionBotDocumentIdentityCard));
                                        } else if (secureValueType instanceof TL_secureValueTypeUtilityBill) {
                                            stringBuilder4.append(LocaleController.getString("ActionBotDocumentUtilityBill", C1067R.string.ActionBotDocumentUtilityBill));
                                        } else if (secureValueType instanceof TL_secureValueTypeBankStatement) {
                                            stringBuilder4.append(LocaleController.getString("ActionBotDocumentBankStatement", C1067R.string.ActionBotDocumentBankStatement));
                                        } else if (secureValueType instanceof TL_secureValueTypeRentalAgreement) {
                                            stringBuilder4.append(LocaleController.getString("ActionBotDocumentRentalAgreement", C1067R.string.ActionBotDocumentRentalAgreement));
                                        } else if (secureValueType instanceof TL_secureValueTypeInternalPassport) {
                                            stringBuilder4.append(LocaleController.getString("ActionBotDocumentInternalPassport", C1067R.string.ActionBotDocumentInternalPassport));
                                        } else if (secureValueType instanceof TL_secureValueTypePassportRegistration) {
                                            stringBuilder4.append(LocaleController.getString("ActionBotDocumentPassportRegistration", C1067R.string.ActionBotDocumentPassportRegistration));
                                        } else if (secureValueType instanceof TL_secureValueTypeTemporaryRegistration) {
                                            stringBuilder4.append(LocaleController.getString("ActionBotDocumentTemporaryRegistration", C1067R.string.ActionBotDocumentTemporaryRegistration));
                                        }
                                    }
                                    Peer peer = message2.to_id;
                                    if (peer != null) {
                                        User user3 = abstractMap3 != null ? (User) abstractMap3.get(Integer.valueOf(peer.user_id)) : sparseArray3 != null ? (User) sparseArray3.get(peer.user_id) : null;
                                        user2 = user3 == null ? MessagesController.getInstance(i).getUser(Integer.valueOf(message2.to_id.user_id)) : user3;
                                    } else {
                                        user2 = null;
                                    }
                                    this.messageText = LocaleController.formatString("ActionBotDocuments", C1067R.string.ActionBotDocuments, UserObject.getFirstName(user2), stringBuilder4.toString());
                                }
                            } else if (message2.to_id.channel_id != 0 && !isMegagroup()) {
                                this.messageText = LocaleController.getString("ActionChannelRemovedPhoto", C1067R.string.ActionChannelRemovedPhoto);
                            } else if (isOut()) {
                                this.messageText = LocaleController.getString("ActionYouRemovedPhoto", C1067R.string.ActionYouRemovedPhoto);
                            } else {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionRemovedPhoto", C1067R.string.ActionRemovedPhoto), str2, user);
                            }
                        }
                    } else if (isOut()) {
                        this.messageText = LocaleController.getString("ActionYouCreateGroup", C1067R.string.ActionYouCreateGroup);
                    } else {
                        this.messageText = replaceWithLink(LocaleController.getString("ActionCreateGroup", C1067R.string.ActionCreateGroup), str2, user);
                    }
                }
            }
            z2 = z;
            i4 = 1;
        } else {
            z2 = z;
            i4 = 1;
            if (isMediaEmpty()) {
                this.messageText = message2.message;
            } else {
                MessageMedia messageMedia = message2.media;
                if (messageMedia instanceof TL_messageMediaPoll) {
                    this.messageText = LocaleController.getString("Poll", C1067R.string.Poll);
                } else if (!(messageMedia instanceof TL_messageMediaPhoto)) {
                    if (!isVideo()) {
                        messageMedia = message2.media;
                        if (!((messageMedia instanceof TL_messageMediaDocument) && (messageMedia.document instanceof TL_documentEmpty) && messageMedia.ttl_seconds != 0)) {
                            if (isVoice()) {
                                this.messageText = LocaleController.getString("AttachAudio", C1067R.string.AttachAudio);
                            } else if (isRoundVideo()) {
                                this.messageText = LocaleController.getString("AttachRound", C1067R.string.AttachRound);
                            } else {
                                messageMedia = message2.media;
                                if ((messageMedia instanceof TL_messageMediaGeo) || (messageMedia instanceof TL_messageMediaVenue)) {
                                    this.messageText = LocaleController.getString("AttachLocation", C1067R.string.AttachLocation);
                                } else if (messageMedia instanceof TL_messageMediaGeoLive) {
                                    this.messageText = LocaleController.getString("AttachLiveLocation", C1067R.string.AttachLiveLocation);
                                } else if (messageMedia instanceof TL_messageMediaContact) {
                                    this.messageText = LocaleController.getString("AttachContact", C1067R.string.AttachContact);
                                    if (!TextUtils.isEmpty(message2.media.vcard)) {
                                        this.vCardData = VCardData.parse(message2.media.vcard);
                                    }
                                } else if (messageMedia instanceof TL_messageMediaGame) {
                                    this.messageText = message2.message;
                                } else if (messageMedia instanceof TL_messageMediaInvoice) {
                                    this.messageText = messageMedia.description;
                                } else if (messageMedia instanceof TL_messageMediaUnsupported) {
                                    this.messageText = LocaleController.getString("UnsupportedMedia", C1067R.string.UnsupportedMedia).replace("https://telegram.org/update", "https://github.com/Telegram-FOSS-Team/Telegram-FOSS/blob/master/Update.md");
                                } else if (messageMedia instanceof TL_messageMediaDocument) {
                                    if (isSticker() || isAnimatedSticker()) {
                                        formatCallDuration = getStrickerChar();
                                        if (formatCallDuration == null || formatCallDuration.length() <= 0) {
                                            this.messageText = LocaleController.getString("AttachSticker", C1067R.string.AttachSticker);
                                        } else {
                                            this.messageText = String.format("%s %s", new Object[]{formatCallDuration, LocaleController.getString("AttachSticker", C1067R.string.AttachSticker)});
                                        }
                                    } else if (isMusic()) {
                                        this.messageText = LocaleController.getString("AttachMusic", C1067R.string.AttachMusic);
                                    } else if (isGif()) {
                                        this.messageText = LocaleController.getString("AttachGif", C1067R.string.AttachGif);
                                    } else {
                                        formatCallDuration = FileLoader.getDocumentFileName(message2.media.document);
                                        if (formatCallDuration == null || formatCallDuration.length() <= 0) {
                                            this.messageText = LocaleController.getString("AttachDocument", C1067R.string.AttachDocument);
                                        } else {
                                            this.messageText = formatCallDuration;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (message2.media.ttl_seconds == 0 || (message2 instanceof TL_message_secret)) {
                        this.messageText = LocaleController.getString("AttachVideo", C1067R.string.AttachVideo);
                    } else {
                        this.messageText = LocaleController.getString("AttachDestructingVideo", C1067R.string.AttachDestructingVideo);
                    }
                } else if (messageMedia.ttl_seconds == 0 || (message2 instanceof TL_message_secret)) {
                    this.messageText = LocaleController.getString("AttachPhoto", C1067R.string.AttachPhoto);
                } else {
                    this.messageText = LocaleController.getString("AttachDestructingPhoto", C1067R.string.AttachDestructingPhoto);
                }
            }
        }
        if (this.messageText == null) {
            this.messageText = str;
        }
        setType();
        measureInlineBotButtons();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(((long) this.messageOwner.date) * 1000);
        i2 = gregorianCalendar.get(6);
        i3 = gregorianCalendar.get(i4);
        i5 = gregorianCalendar.get(2);
        this.dateKey = String.format("%d_%02d_%02d", new Object[]{Integer.valueOf(i3), Integer.valueOf(i5), Integer.valueOf(i2)});
        this.monthKey = String.format("%d_%02d", new Object[]{Integer.valueOf(i3), Integer.valueOf(i5)});
        createMessageSendInfo();
        generateCaption();
        if (z2) {
            TextPaint textPaint;
            if (this.messageOwner.media instanceof TL_messageMediaGame) {
                textPaint = Theme.chat_msgGameTextPaint;
            } else {
                textPaint = Theme.chat_msgTextPaint;
            }
            int[] iArr = SharedConfig.allowBigEmoji ? new int[i4] : null;
            this.messageText = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.m26dp(20.0f), false, iArr);
            checkEmojiOnly(iArr);
            generateLayout(user);
        }
        this.layoutCreated = z2;
        generateThumbs(false);
        checkMediaExistance();
    }

    private void createDateArray(int i, TL_channelAdminLogEvent tL_channelAdminLogEvent, ArrayList<MessageObject> arrayList, HashMap<String, ArrayList<MessageObject>> hashMap) {
        if (((ArrayList) hashMap.get(this.dateKey)) == null) {
            hashMap.put(this.dateKey, new ArrayList());
            TL_message tL_message = new TL_message();
            tL_message.message = LocaleController.formatDateChat((long) tL_channelAdminLogEvent.date);
            tL_message.f461id = 0;
            tL_message.date = tL_channelAdminLogEvent.date;
            MessageObject messageObject = new MessageObject(i, tL_message, false);
            messageObject.type = 10;
            messageObject.contentType = 1;
            messageObject.isDateObject = true;
            arrayList.add(messageObject);
        }
    }

    private void checkEmojiOnly(int[] iArr) {
        if (iArr != null) {
            int i = 0;
            if (iArr[0] >= 1 && iArr[0] <= 3) {
                TextPaint textPaint;
                int dp;
                int i2 = iArr[0];
                if (i2 == 1) {
                    textPaint = Theme.chat_msgTextPaintOneEmoji;
                    dp = AndroidUtilities.m26dp(32.0f);
                    this.emojiOnlyCount = 1;
                } else if (i2 != 2) {
                    textPaint = Theme.chat_msgTextPaintThreeEmoji;
                    dp = AndroidUtilities.m26dp(24.0f);
                    this.emojiOnlyCount = 3;
                } else {
                    textPaint = Theme.chat_msgTextPaintTwoEmoji;
                    int dp2 = AndroidUtilities.m26dp(28.0f);
                    this.emojiOnlyCount = 2;
                    dp = dp2;
                }
                CharSequence charSequence = this.messageText;
                EmojiSpan[] emojiSpanArr = (EmojiSpan[]) ((Spannable) charSequence).getSpans(0, charSequence.length(), EmojiSpan.class);
                if (emojiSpanArr != null && emojiSpanArr.length > 0) {
                    while (i < emojiSpanArr.length) {
                        emojiSpanArr[i].replaceFontMetrics(textPaint.getFontMetricsInt(), dp);
                        i++;
                    }
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:236:0x05f9  */
    /* JADX WARNING: Removed duplicated region for block: B:231:0x05e5  */
    /* JADX WARNING: Removed duplicated region for block: B:239:0x05ff A:{LOOP_END, LOOP:0: B:217:0x05b8->B:239:0x05ff} */
    /* JADX WARNING: Removed duplicated region for block: B:524:0x0619 A:{SYNTHETIC, EDGE_INSN: B:524:0x0619->B:241:0x0619 ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:231:0x05e5  */
    /* JADX WARNING: Removed duplicated region for block: B:236:0x05f9  */
    /* JADX WARNING: Removed duplicated region for block: B:524:0x0619 A:{SYNTHETIC, EDGE_INSN: B:524:0x0619->B:241:0x0619 ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:239:0x05ff A:{LOOP_END, LOOP:0: B:217:0x05b8->B:239:0x05ff} */
    /* JADX WARNING: Removed duplicated region for block: B:236:0x05f9  */
    /* JADX WARNING: Removed duplicated region for block: B:231:0x05e5  */
    /* JADX WARNING: Removed duplicated region for block: B:239:0x05ff A:{LOOP_END, LOOP:0: B:217:0x05b8->B:239:0x05ff} */
    /* JADX WARNING: Removed duplicated region for block: B:524:0x0619 A:{SYNTHETIC, EDGE_INSN: B:524:0x0619->B:241:0x0619 ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:459:0x0c4a  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0c3a  */
    /* JADX WARNING: Removed duplicated region for block: B:462:0x0c5f  */
    /* JADX WARNING: Removed duplicated region for block: B:485:0x0d4b  */
    /* JADX WARNING: Removed duplicated region for block: B:488:0x0d8a  */
    /* JADX WARNING: Removed duplicated region for block: B:505:0x0dff  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0da2  */
    /* JADX WARNING: Removed duplicated region for block: B:526:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:508:0x0e04  */
    /* JADX WARNING: Missing block: B:445:0x0c0a, code skipped:
            if (r9.f463id != r10.f463id) goto L_0x0c23;
     */
    /* JADX WARNING: Missing block: B:451:0x0c1e, code skipped:
            if (r9.f441id != r10.f441id) goto L_0x0c23;
     */
    public MessageObject(int r30, org.telegram.tgnet.TLRPC.TL_channelAdminLogEvent r31, java.util.ArrayList<org.telegram.messenger.MessageObject> r32, java.util.HashMap<java.lang.String, java.util.ArrayList<org.telegram.messenger.MessageObject>> r33, org.telegram.tgnet.TLRPC.Chat r34, int[] r35) {
        /*
        r29 = this;
        r0 = r29;
        r1 = r31;
        r2 = r32;
        r3 = r34;
        r29.<init>();
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0.type = r4;
        r4 = r1.user_id;
        if (r4 <= 0) goto L_0x0022;
    L_0x0013:
        r4 = org.telegram.messenger.MessagesController.getInstance(r30);
        r6 = r1.user_id;
        r6 = java.lang.Integer.valueOf(r6);
        r4 = r4.getUser(r6);
        goto L_0x0023;
    L_0x0022:
        r4 = 0;
    L_0x0023:
        r0.currentEvent = r1;
        r6 = new java.util.GregorianCalendar;
        r6.<init>();
        r7 = r1.date;
        r7 = (long) r7;
        r9 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r7 = r7 * r9;
        r6.setTimeInMillis(r7);
        r7 = 6;
        r7 = r6.get(r7);
        r8 = 1;
        r9 = r6.get(r8);
        r10 = 2;
        r6 = r6.get(r10);
        r11 = 3;
        r12 = new java.lang.Object[r11];
        r13 = java.lang.Integer.valueOf(r9);
        r14 = 0;
        r12[r14] = r13;
        r13 = java.lang.Integer.valueOf(r6);
        r12[r8] = r13;
        r7 = java.lang.Integer.valueOf(r7);
        r12[r10] = r7;
        r7 = "%d_%02d_%02d";
        r7 = java.lang.String.format(r7, r12);
        r0.dateKey = r7;
        r7 = new java.lang.Object[r10];
        r9 = java.lang.Integer.valueOf(r9);
        r7[r14] = r9;
        r6 = java.lang.Integer.valueOf(r6);
        r7[r8] = r6;
        r6 = "%d_%02d";
        r6 = java.lang.String.format(r6, r7);
        r0.monthKey = r6;
        r6 = new org.telegram.tgnet.TLRPC$TL_peerChannel;
        r6.<init>();
        r7 = r3.f434id;
        r6.channel_id = r7;
        r7 = r1.action;
        r9 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeTitle;
        r12 = "";
        r13 = "un1";
        if (r9 == 0) goto L_0x00bc;
    L_0x008a:
        r7 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeTitle) r7;
        r6 = r7.new_value;
        r7 = r3.megagroup;
        if (r7 == 0) goto L_0x00a7;
    L_0x0092:
        r7 = 2131559402; // 0x7f0d03ea float:1.8744147E38 double:1.0531302726E-314;
        r9 = new java.lang.Object[r8];
        r9[r14] = r6;
        r6 = "EventLogEditedGroupTitle";
        r6 = org.telegram.messenger.LocaleController.formatString(r6, r7, r9);
        r6 = r0.replaceWithLink(r6, r13, r4);
        r0.messageText = r6;
        goto L_0x0d46;
    L_0x00a7:
        r7 = 2131559399; // 0x7f0d03e7 float:1.874414E38 double:1.053130271E-314;
        r9 = new java.lang.Object[r8];
        r9[r14] = r6;
        r6 = "EventLogEditedChannelTitle";
        r6 = org.telegram.messenger.LocaleController.formatString(r6, r7, r9);
        r6 = r0.replaceWithLink(r6, r13, r4);
        r0.messageText = r6;
        goto L_0x0d46;
    L_0x00bc:
        r9 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangePhoto;
        if (r9 == 0) goto L_0x0137;
    L_0x00c0:
        r6 = new org.telegram.tgnet.TLRPC$TL_messageService;
        r6.<init>();
        r0.messageOwner = r6;
        r6 = r1.action;
        r6 = r6.new_photo;
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_photoEmpty;
        if (r6 == 0) goto L_0x00fe;
    L_0x00cf:
        r6 = r0.messageOwner;
        r7 = new org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto;
        r7.<init>();
        r6.action = r7;
        r6 = r3.megagroup;
        if (r6 == 0) goto L_0x00ed;
    L_0x00dc:
        r6 = 2131559448; // 0x7f0d0418 float:1.874424E38 double:1.0531302953E-314;
        r7 = "EventLogRemovedWGroupPhoto";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
        r6 = r0.replaceWithLink(r6, r13, r4);
        r0.messageText = r6;
        goto L_0x0d46;
    L_0x00ed:
        r6 = 2131559443; // 0x7f0d0413 float:1.874423E38 double:1.053130293E-314;
        r7 = "EventLogRemovedChannelPhoto";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
        r6 = r0.replaceWithLink(r6, r13, r4);
        r0.messageText = r6;
        goto L_0x0d46;
    L_0x00fe:
        r6 = r0.messageOwner;
        r7 = new org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto;
        r7.<init>();
        r6.action = r7;
        r6 = r0.messageOwner;
        r6 = r6.action;
        r7 = r1.action;
        r7 = r7.new_photo;
        r6.photo = r7;
        r6 = r3.megagroup;
        if (r6 == 0) goto L_0x0126;
    L_0x0115:
        r6 = 2131559401; // 0x7f0d03e9 float:1.8744145E38 double:1.053130272E-314;
        r7 = "EventLogEditedGroupPhoto";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
        r6 = r0.replaceWithLink(r6, r13, r4);
        r0.messageText = r6;
        goto L_0x0d46;
    L_0x0126:
        r6 = 2131559398; // 0x7f0d03e6 float:1.8744139E38 double:1.0531302706E-314;
        r7 = "EventLogEditedChannelPhoto";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
        r6 = r0.replaceWithLink(r6, r13, r4);
        r0.messageText = r6;
        goto L_0x0d46;
    L_0x0137:
        r9 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionParticipantJoin;
        r15 = "EventLogChannelJoined";
        if (r9 == 0) goto L_0x0161;
    L_0x013d:
        r6 = r3.megagroup;
        if (r6 == 0) goto L_0x0152;
    L_0x0141:
        r6 = 2131559420; // 0x7f0d03fc float:1.8744184E38 double:1.0531302815E-314;
        r7 = "EventLogGroupJoined";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
        r6 = r0.replaceWithLink(r6, r13, r4);
        r0.messageText = r6;
        goto L_0x0d46;
    L_0x0152:
        r6 = 2131559391; // 0x7f0d03df float:1.8744125E38 double:1.053130267E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r15, r6);
        r6 = r0.replaceWithLink(r6, r13, r4);
        r0.messageText = r6;
        goto L_0x0d46;
    L_0x0161:
        r9 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionParticipantLeave;
        if (r9 == 0) goto L_0x01a3;
    L_0x0165:
        r6 = new org.telegram.tgnet.TLRPC$TL_messageService;
        r6.<init>();
        r0.messageOwner = r6;
        r6 = r0.messageOwner;
        r7 = new org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser;
        r7.<init>();
        r6.action = r7;
        r6 = r0.messageOwner;
        r6 = r6.action;
        r7 = r1.user_id;
        r6.user_id = r7;
        r6 = r3.megagroup;
        if (r6 == 0) goto L_0x0192;
    L_0x0181:
        r6 = 2131559425; // 0x7f0d0401 float:1.8744194E38 double:1.053130284E-314;
        r7 = "EventLogLeftGroup";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
        r6 = r0.replaceWithLink(r6, r13, r4);
        r0.messageText = r6;
        goto L_0x0d46;
    L_0x0192:
        r6 = 2131559424; // 0x7f0d0400 float:1.8744192E38 double:1.0531302835E-314;
        r7 = "EventLogLeftChannel";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
        r6 = r0.replaceWithLink(r6, r13, r4);
        r0.messageText = r6;
        goto L_0x0d46;
    L_0x01a3:
        r9 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionParticipantInvite;
        r5 = "un2";
        if (r9 == 0) goto L_0x0214;
    L_0x01a9:
        r6 = new org.telegram.tgnet.TLRPC$TL_messageService;
        r6.<init>();
        r0.messageOwner = r6;
        r6 = r0.messageOwner;
        r7 = new org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser;
        r7.<init>();
        r6.action = r7;
        r6 = org.telegram.messenger.MessagesController.getInstance(r30);
        r7 = r1.action;
        r7 = r7.participant;
        r7 = r7.user_id;
        r7 = java.lang.Integer.valueOf(r7);
        r6 = r6.getUser(r7);
        r7 = r1.action;
        r7 = r7.participant;
        r7 = r7.user_id;
        r9 = r0.messageOwner;
        r9 = r9.from_id;
        if (r7 != r9) goto L_0x01fb;
    L_0x01d7:
        r5 = r3.megagroup;
        if (r5 == 0) goto L_0x01ec;
    L_0x01db:
        r5 = 2131559420; // 0x7f0d03fc float:1.8744184E38 double:1.0531302815E-314;
        r6 = "EventLogGroupJoined";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x01ec:
        r5 = 2131559391; // 0x7f0d03df float:1.8744125E38 double:1.053130267E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r15, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x01fb:
        r7 = 2131559383; // 0x7f0d03d7 float:1.8744109E38 double:1.053130263E-314;
        r9 = "EventLogAdded";
        r7 = org.telegram.messenger.LocaleController.getString(r9, r7);
        r5 = r0.replaceWithLink(r7, r5, r6);
        r0.messageText = r5;
        r5 = r0.messageText;
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x0214:
        r9 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionParticipantToggleAdmin;
        r15 = "%1$s";
        r10 = 32;
        r16 = 43;
        r17 = 45;
        r11 = 10;
        if (r9 == 0) goto L_0x03b8;
    L_0x0222:
        r5 = new org.telegram.tgnet.TLRPC$TL_message;
        r5.<init>();
        r0.messageOwner = r5;
        r5 = org.telegram.messenger.MessagesController.getInstance(r30);
        r6 = r1.action;
        r6 = r6.prev_participant;
        r6 = r6.user_id;
        r6 = java.lang.Integer.valueOf(r6);
        r5 = r5.getUser(r6);
        r6 = 2131559432; // 0x7f0d0408 float:1.8744208E38 double:1.0531302874E-314;
        r7 = "EventLogPromoted";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
        r7 = r6.indexOf(r15);
        r9 = new java.lang.StringBuilder;
        r13 = new java.lang.Object[r8];
        r15 = r0.messageOwner;
        r15 = r15.entities;
        r5 = r0.getUserName(r5, r15, r7);
        r13[r14] = r5;
        r5 = java.lang.String.format(r6, r13);
        r9.<init>(r5);
        r5 = "\n";
        r9.append(r5);
        r5 = r1.action;
        r6 = r5.prev_participant;
        r6 = r6.admin_rights;
        r5 = r5.new_participant;
        r5 = r5.admin_rights;
        if (r6 != 0) goto L_0x0273;
    L_0x026e:
        r6 = new org.telegram.tgnet.TLRPC$TL_chatAdminRights;
        r6.<init>();
    L_0x0273:
        if (r5 != 0) goto L_0x027a;
    L_0x0275:
        r5 = new org.telegram.tgnet.TLRPC$TL_chatAdminRights;
        r5.<init>();
    L_0x027a:
        r7 = r6.change_info;
        r13 = r5.change_info;
        if (r7 == r13) goto L_0x02a8;
    L_0x0280:
        r9.append(r11);
        r7 = r5.change_info;
        if (r7 == 0) goto L_0x028a;
    L_0x0287:
        r7 = 43;
        goto L_0x028c;
    L_0x028a:
        r7 = 45;
    L_0x028c:
        r9.append(r7);
        r9.append(r10);
        r7 = r3.megagroup;
        if (r7 == 0) goto L_0x029c;
    L_0x0296:
        r7 = 2131559437; // 0x7f0d040d float:1.8744218E38 double:1.05313029E-314;
        r13 = "EventLogPromotedChangeGroupInfo";
        goto L_0x02a1;
    L_0x029c:
        r7 = 2131559436; // 0x7f0d040c float:1.8744216E38 double:1.0531302894E-314;
        r13 = "EventLogPromotedChangeChannelInfo";
    L_0x02a1:
        r7 = org.telegram.messenger.LocaleController.getString(r13, r7);
        r9.append(r7);
    L_0x02a8:
        r7 = r3.megagroup;
        if (r7 != 0) goto L_0x02f4;
    L_0x02ac:
        r7 = r6.post_messages;
        r13 = r5.post_messages;
        if (r7 == r13) goto L_0x02d0;
    L_0x02b2:
        r9.append(r11);
        r7 = r5.post_messages;
        if (r7 == 0) goto L_0x02bc;
    L_0x02b9:
        r7 = 43;
        goto L_0x02be;
    L_0x02bc:
        r7 = 45;
    L_0x02be:
        r9.append(r7);
        r9.append(r10);
        r7 = 2131559441; // 0x7f0d0411 float:1.8744226E38 double:1.053130292E-314;
        r13 = "EventLogPromotedPostMessages";
        r7 = org.telegram.messenger.LocaleController.getString(r13, r7);
        r9.append(r7);
    L_0x02d0:
        r7 = r6.edit_messages;
        r13 = r5.edit_messages;
        if (r7 == r13) goto L_0x02f4;
    L_0x02d6:
        r9.append(r11);
        r7 = r5.edit_messages;
        if (r7 == 0) goto L_0x02e0;
    L_0x02dd:
        r7 = 43;
        goto L_0x02e2;
    L_0x02e0:
        r7 = 45;
    L_0x02e2:
        r9.append(r7);
        r9.append(r10);
        r7 = 2131559439; // 0x7f0d040f float:1.8744222E38 double:1.053130291E-314;
        r13 = "EventLogPromotedEditMessages";
        r7 = org.telegram.messenger.LocaleController.getString(r13, r7);
        r9.append(r7);
    L_0x02f4:
        r7 = r6.delete_messages;
        r13 = r5.delete_messages;
        if (r7 == r13) goto L_0x0318;
    L_0x02fa:
        r9.append(r11);
        r7 = r5.delete_messages;
        if (r7 == 0) goto L_0x0304;
    L_0x0301:
        r7 = 43;
        goto L_0x0306;
    L_0x0304:
        r7 = 45;
    L_0x0306:
        r9.append(r7);
        r9.append(r10);
        r7 = 2131559438; // 0x7f0d040e float:1.874422E38 double:1.0531302904E-314;
        r13 = "EventLogPromotedDeleteMessages";
        r7 = org.telegram.messenger.LocaleController.getString(r13, r7);
        r9.append(r7);
    L_0x0318:
        r7 = r6.add_admins;
        r13 = r5.add_admins;
        if (r7 == r13) goto L_0x033c;
    L_0x031e:
        r9.append(r11);
        r7 = r5.add_admins;
        if (r7 == 0) goto L_0x0328;
    L_0x0325:
        r7 = 43;
        goto L_0x032a;
    L_0x0328:
        r7 = 45;
    L_0x032a:
        r9.append(r7);
        r9.append(r10);
        r7 = 2131559433; // 0x7f0d0409 float:1.874421E38 double:1.053130288E-314;
        r13 = "EventLogPromotedAddAdmins";
        r7 = org.telegram.messenger.LocaleController.getString(r13, r7);
        r9.append(r7);
    L_0x033c:
        r7 = r3.megagroup;
        if (r7 == 0) goto L_0x0364;
    L_0x0340:
        r7 = r6.ban_users;
        r13 = r5.ban_users;
        if (r7 == r13) goto L_0x0364;
    L_0x0346:
        r9.append(r11);
        r7 = r5.ban_users;
        if (r7 == 0) goto L_0x0350;
    L_0x034d:
        r7 = 43;
        goto L_0x0352;
    L_0x0350:
        r7 = 45;
    L_0x0352:
        r9.append(r7);
        r9.append(r10);
        r7 = 2131559435; // 0x7f0d040b float:1.8744214E38 double:1.053130289E-314;
        r13 = "EventLogPromotedBanUsers";
        r7 = org.telegram.messenger.LocaleController.getString(r13, r7);
        r9.append(r7);
    L_0x0364:
        r7 = r6.invite_users;
        r13 = r5.invite_users;
        if (r7 == r13) goto L_0x0388;
    L_0x036a:
        r9.append(r11);
        r7 = r5.invite_users;
        if (r7 == 0) goto L_0x0374;
    L_0x0371:
        r7 = 43;
        goto L_0x0376;
    L_0x0374:
        r7 = 45;
    L_0x0376:
        r9.append(r7);
        r9.append(r10);
        r7 = 2131559434; // 0x7f0d040a float:1.8744212E38 double:1.0531302884E-314;
        r13 = "EventLogPromotedAddUsers";
        r7 = org.telegram.messenger.LocaleController.getString(r13, r7);
        r9.append(r7);
    L_0x0388:
        r7 = r3.megagroup;
        if (r7 == 0) goto L_0x03b0;
    L_0x038c:
        r6 = r6.pin_messages;
        r7 = r5.pin_messages;
        if (r6 == r7) goto L_0x03b0;
    L_0x0392:
        r9.append(r11);
        r5 = r5.pin_messages;
        if (r5 == 0) goto L_0x039c;
    L_0x0399:
        r5 = 43;
        goto L_0x039e;
    L_0x039c:
        r5 = 45;
    L_0x039e:
        r9.append(r5);
        r9.append(r10);
        r5 = 2131559440; // 0x7f0d0410 float:1.8744224E38 double:1.0531302914E-314;
        r6 = "EventLogPromotedPinMessages";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r9.append(r5);
    L_0x03b0:
        r5 = r9.toString();
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x03b8:
        r9 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionDefaultBannedRights;
        if (r9 == 0) goto L_0x054e;
    L_0x03bc:
        r7 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionDefaultBannedRights) r7;
        r5 = new org.telegram.tgnet.TLRPC$TL_message;
        r5.<init>();
        r0.messageOwner = r5;
        r5 = r7.prev_banned_rights;
        r6 = r7.new_banned_rights;
        r7 = new java.lang.StringBuilder;
        r9 = 2131559394; // 0x7f0d03e2 float:1.874413E38 double:1.0531302686E-314;
        r13 = "EventLogDefaultPermissions";
        r9 = org.telegram.messenger.LocaleController.getString(r13, r9);
        r7.<init>(r9);
        if (r5 != 0) goto L_0x03de;
    L_0x03d9:
        r5 = new org.telegram.tgnet.TLRPC$TL_chatBannedRights;
        r5.<init>();
    L_0x03de:
        if (r6 != 0) goto L_0x03e5;
    L_0x03e0:
        r6 = new org.telegram.tgnet.TLRPC$TL_chatBannedRights;
        r6.<init>();
    L_0x03e5:
        r9 = r5.send_messages;
        r13 = r6.send_messages;
        if (r9 == r13) goto L_0x040e;
    L_0x03eb:
        r7.append(r11);
        r7.append(r11);
        r9 = r6.send_messages;
        if (r9 != 0) goto L_0x03f8;
    L_0x03f5:
        r9 = 43;
        goto L_0x03fa;
    L_0x03f8:
        r9 = 45;
    L_0x03fa:
        r7.append(r9);
        r7.append(r10);
        r9 = 2131559455; // 0x7f0d041f float:1.8744255E38 double:1.053130299E-314;
        r13 = "EventLogRestrictedSendMessages";
        r9 = org.telegram.messenger.LocaleController.getString(r13, r9);
        r7.append(r9);
        r9 = 1;
        goto L_0x040f;
    L_0x040e:
        r9 = 0;
    L_0x040f:
        r13 = r5.send_stickers;
        r15 = r6.send_stickers;
        if (r13 != r15) goto L_0x0427;
    L_0x0415:
        r13 = r5.send_inline;
        r15 = r6.send_inline;
        if (r13 != r15) goto L_0x0427;
    L_0x041b:
        r13 = r5.send_gifs;
        r15 = r6.send_gifs;
        if (r13 != r15) goto L_0x0427;
    L_0x0421:
        r13 = r5.send_games;
        r15 = r6.send_games;
        if (r13 == r15) goto L_0x044b;
    L_0x0427:
        if (r9 != 0) goto L_0x042d;
    L_0x0429:
        r7.append(r11);
        r9 = 1;
    L_0x042d:
        r7.append(r11);
        r13 = r6.send_stickers;
        if (r13 != 0) goto L_0x0437;
    L_0x0434:
        r13 = 43;
        goto L_0x0439;
    L_0x0437:
        r13 = 45;
    L_0x0439:
        r7.append(r13);
        r7.append(r10);
        r13 = 2131559457; // 0x7f0d0421 float:1.8744259E38 double:1.0531303E-314;
        r15 = "EventLogRestrictedSendStickers";
        r13 = org.telegram.messenger.LocaleController.getString(r15, r13);
        r7.append(r13);
    L_0x044b:
        r13 = r5.send_media;
        r15 = r6.send_media;
        if (r13 == r15) goto L_0x0475;
    L_0x0451:
        if (r9 != 0) goto L_0x0457;
    L_0x0453:
        r7.append(r11);
        r9 = 1;
    L_0x0457:
        r7.append(r11);
        r13 = r6.send_media;
        if (r13 != 0) goto L_0x0461;
    L_0x045e:
        r13 = 43;
        goto L_0x0463;
    L_0x0461:
        r13 = 45;
    L_0x0463:
        r7.append(r13);
        r7.append(r10);
        r13 = 2131559454; // 0x7f0d041e float:1.8744253E38 double:1.0531302983E-314;
        r15 = "EventLogRestrictedSendMedia";
        r13 = org.telegram.messenger.LocaleController.getString(r15, r13);
        r7.append(r13);
    L_0x0475:
        r13 = r5.send_polls;
        r15 = r6.send_polls;
        if (r13 == r15) goto L_0x049f;
    L_0x047b:
        if (r9 != 0) goto L_0x0481;
    L_0x047d:
        r7.append(r11);
        r9 = 1;
    L_0x0481:
        r7.append(r11);
        r13 = r6.send_polls;
        if (r13 != 0) goto L_0x048b;
    L_0x0488:
        r13 = 43;
        goto L_0x048d;
    L_0x048b:
        r13 = 45;
    L_0x048d:
        r7.append(r13);
        r7.append(r10);
        r13 = 2131559456; // 0x7f0d0420 float:1.8744257E38 double:1.0531302993E-314;
        r15 = "EventLogRestrictedSendPolls";
        r13 = org.telegram.messenger.LocaleController.getString(r15, r13);
        r7.append(r13);
    L_0x049f:
        r13 = r5.embed_links;
        r15 = r6.embed_links;
        if (r13 == r15) goto L_0x04c9;
    L_0x04a5:
        if (r9 != 0) goto L_0x04ab;
    L_0x04a7:
        r7.append(r11);
        r9 = 1;
    L_0x04ab:
        r7.append(r11);
        r13 = r6.embed_links;
        if (r13 != 0) goto L_0x04b5;
    L_0x04b2:
        r13 = 43;
        goto L_0x04b7;
    L_0x04b5:
        r13 = 45;
    L_0x04b7:
        r7.append(r13);
        r7.append(r10);
        r13 = 2131559453; // 0x7f0d041d float:1.874425E38 double:1.053130298E-314;
        r15 = "EventLogRestrictedSendEmbed";
        r13 = org.telegram.messenger.LocaleController.getString(r15, r13);
        r7.append(r13);
    L_0x04c9:
        r13 = r5.change_info;
        r15 = r6.change_info;
        if (r13 == r15) goto L_0x04f3;
    L_0x04cf:
        if (r9 != 0) goto L_0x04d5;
    L_0x04d1:
        r7.append(r11);
        r9 = 1;
    L_0x04d5:
        r7.append(r11);
        r13 = r6.change_info;
        if (r13 != 0) goto L_0x04df;
    L_0x04dc:
        r13 = 43;
        goto L_0x04e1;
    L_0x04df:
        r13 = 45;
    L_0x04e1:
        r7.append(r13);
        r7.append(r10);
        r13 = 2131559449; // 0x7f0d0419 float:1.8744242E38 double:1.053130296E-314;
        r15 = "EventLogRestrictedChangeInfo";
        r13 = org.telegram.messenger.LocaleController.getString(r15, r13);
        r7.append(r13);
    L_0x04f3:
        r13 = r5.invite_users;
        r15 = r6.invite_users;
        if (r13 == r15) goto L_0x051d;
    L_0x04f9:
        if (r9 != 0) goto L_0x04ff;
    L_0x04fb:
        r7.append(r11);
        r9 = 1;
    L_0x04ff:
        r7.append(r11);
        r13 = r6.invite_users;
        if (r13 != 0) goto L_0x0509;
    L_0x0506:
        r13 = 43;
        goto L_0x050b;
    L_0x0509:
        r13 = 45;
    L_0x050b:
        r7.append(r13);
        r7.append(r10);
        r13 = 2131559450; // 0x7f0d041a float:1.8744244E38 double:1.0531302963E-314;
        r15 = "EventLogRestrictedInviteUsers";
        r13 = org.telegram.messenger.LocaleController.getString(r15, r13);
        r7.append(r13);
    L_0x051d:
        r5 = r5.pin_messages;
        r13 = r6.pin_messages;
        if (r5 == r13) goto L_0x0546;
    L_0x0523:
        if (r9 != 0) goto L_0x0528;
    L_0x0525:
        r7.append(r11);
    L_0x0528:
        r7.append(r11);
        r5 = r6.pin_messages;
        if (r5 != 0) goto L_0x0532;
    L_0x052f:
        r5 = 43;
        goto L_0x0534;
    L_0x0532:
        r5 = 45;
    L_0x0534:
        r7.append(r5);
        r7.append(r10);
        r5 = 2131559451; // 0x7f0d041b float:1.8744246E38 double:1.053130297E-314;
        r6 = "EventLogRestrictedPinMessages";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r7.append(r5);
    L_0x0546:
        r5 = r7.toString();
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x054e:
        r9 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionParticipantToggleBan;
        if (r9 == 0) goto L_0x0856;
    L_0x0552:
        r5 = new org.telegram.tgnet.TLRPC$TL_message;
        r5.<init>();
        r0.messageOwner = r5;
        r5 = org.telegram.messenger.MessagesController.getInstance(r30);
        r6 = r1.action;
        r6 = r6.prev_participant;
        r6 = r6.user_id;
        r6 = java.lang.Integer.valueOf(r6);
        r5 = r5.getUser(r6);
        r6 = r1.action;
        r7 = r6.prev_participant;
        r7 = r7.banned_rights;
        r6 = r6.new_participant;
        r6 = r6.banned_rights;
        r9 = r3.megagroup;
        if (r9 == 0) goto L_0x0821;
    L_0x0579:
        if (r6 == 0) goto L_0x0589;
    L_0x057b:
        r9 = r6.view_messages;
        if (r9 == 0) goto L_0x0589;
    L_0x057f:
        if (r6 == 0) goto L_0x0821;
    L_0x0581:
        if (r7 == 0) goto L_0x0821;
    L_0x0583:
        r9 = r6.until_date;
        r13 = r7.until_date;
        if (r9 == r13) goto L_0x0821;
    L_0x0589:
        if (r6 == 0) goto L_0x060b;
    L_0x058b:
        r9 = org.telegram.messenger.AndroidUtilities.isBannedForever(r6);
        if (r9 != 0) goto L_0x060b;
    L_0x0591:
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r13 = r6.until_date;
        r10 = r1.date;
        r13 = r13 - r10;
        r10 = r13 / 60;
        r10 = r10 / 60;
        r10 = r10 / 24;
        r19 = r10 * 60;
        r19 = r19 * 60;
        r19 = r19 * 24;
        r13 = r13 - r19;
        r19 = r13 / 60;
        r11 = r19 / 60;
        r19 = r11 * 60;
        r19 = r19 * 60;
        r13 = r13 - r19;
        r13 = r13 / 60;
        r8 = 3;
        r18 = 0;
    L_0x05b8:
        if (r14 >= r8) goto L_0x0619;
    L_0x05ba:
        if (r14 != 0) goto L_0x05c7;
    L_0x05bc:
        if (r10 == 0) goto L_0x05dc;
    L_0x05be:
        r8 = "Days";
        r8 = org.telegram.messenger.LocaleController.formatPluralString(r8, r10);
    L_0x05c4:
        r18 = r18 + 1;
        goto L_0x05dd;
    L_0x05c7:
        r8 = 1;
        if (r14 != r8) goto L_0x05d3;
    L_0x05ca:
        if (r11 == 0) goto L_0x05dc;
    L_0x05cc:
        r8 = "Hours";
        r8 = org.telegram.messenger.LocaleController.formatPluralString(r8, r11);
        goto L_0x05c4;
    L_0x05d3:
        if (r13 == 0) goto L_0x05dc;
    L_0x05d5:
        r8 = "Minutes";
        r8 = org.telegram.messenger.LocaleController.formatPluralString(r8, r13);
        goto L_0x05c4;
    L_0x05dc:
        r8 = 0;
    L_0x05dd:
        r28 = r18;
        r18 = r10;
        r10 = r28;
        if (r8 == 0) goto L_0x05f9;
    L_0x05e5:
        r21 = r9.length();
        if (r21 <= 0) goto L_0x05f3;
    L_0x05eb:
        r21 = r11;
        r11 = ", ";
        r9.append(r11);
        goto L_0x05f5;
    L_0x05f3:
        r21 = r11;
    L_0x05f5:
        r9.append(r8);
        goto L_0x05fb;
    L_0x05f9:
        r21 = r11;
    L_0x05fb:
        r8 = 2;
        if (r10 != r8) goto L_0x05ff;
    L_0x05fe:
        goto L_0x0619;
    L_0x05ff:
        r14 = r14 + 1;
        r11 = r21;
        r8 = 3;
        r28 = r18;
        r18 = r10;
        r10 = r28;
        goto L_0x05b8;
    L_0x060b:
        r9 = new java.lang.StringBuilder;
        r8 = 2131561019; // 0x7f0d0a3b float:1.8747427E38 double:1.0531310715E-314;
        r10 = "UserRestrictionsUntilForever";
        r8 = org.telegram.messenger.LocaleController.getString(r10, r8);
        r9.<init>(r8);
    L_0x0619:
        r8 = 2131559458; // 0x7f0d0422 float:1.874426E38 double:1.0531303003E-314;
        r10 = "EventLogRestrictedUntil";
        r8 = org.telegram.messenger.LocaleController.getString(r10, r8);
        r10 = r8.indexOf(r15);
        r11 = new java.lang.StringBuilder;
        r13 = 2;
        r13 = new java.lang.Object[r13];
        r14 = r0.messageOwner;
        r14 = r14.entities;
        r5 = r0.getUserName(r5, r14, r10);
        r10 = 0;
        r13[r10] = r5;
        r5 = r9.toString();
        r9 = 1;
        r13[r9] = r5;
        r5 = java.lang.String.format(r8, r13);
        r11.<init>(r5);
        if (r7 != 0) goto L_0x064b;
    L_0x0646:
        r7 = new org.telegram.tgnet.TLRPC$TL_chatBannedRights;
        r7.<init>();
    L_0x064b:
        if (r6 != 0) goto L_0x0652;
    L_0x064d:
        r6 = new org.telegram.tgnet.TLRPC$TL_chatBannedRights;
        r6.<init>();
    L_0x0652:
        r5 = r7.view_messages;
        r8 = r6.view_messages;
        if (r5 == r8) goto L_0x067f;
    L_0x0658:
        r5 = 10;
        r11.append(r5);
        r11.append(r5);
        r5 = r6.view_messages;
        if (r5 != 0) goto L_0x0667;
    L_0x0664:
        r5 = 43;
        goto L_0x0669;
    L_0x0667:
        r5 = 45;
    L_0x0669:
        r11.append(r5);
        r5 = 32;
        r11.append(r5);
        r5 = 2131559452; // 0x7f0d041c float:1.8744248E38 double:1.0531302973E-314;
        r8 = "EventLogRestrictedReadMessages";
        r5 = org.telegram.messenger.LocaleController.getString(r8, r5);
        r11.append(r5);
        r8 = 1;
        goto L_0x0680;
    L_0x067f:
        r8 = 0;
    L_0x0680:
        r5 = r7.send_messages;
        r9 = r6.send_messages;
        if (r5 == r9) goto L_0x06b1;
    L_0x0686:
        if (r8 != 0) goto L_0x068f;
    L_0x0688:
        r5 = 10;
        r11.append(r5);
        r8 = 1;
        goto L_0x0691;
    L_0x068f:
        r5 = 10;
    L_0x0691:
        r11.append(r5);
        r5 = r6.send_messages;
        if (r5 != 0) goto L_0x069b;
    L_0x0698:
        r5 = 43;
        goto L_0x069d;
    L_0x069b:
        r5 = 45;
    L_0x069d:
        r11.append(r5);
        r5 = 32;
        r11.append(r5);
        r5 = 2131559455; // 0x7f0d041f float:1.8744255E38 double:1.053130299E-314;
        r9 = "EventLogRestrictedSendMessages";
        r5 = org.telegram.messenger.LocaleController.getString(r9, r5);
        r11.append(r5);
    L_0x06b1:
        r5 = r7.send_stickers;
        r9 = r6.send_stickers;
        if (r5 != r9) goto L_0x06c9;
    L_0x06b7:
        r5 = r7.send_inline;
        r9 = r6.send_inline;
        if (r5 != r9) goto L_0x06c9;
    L_0x06bd:
        r5 = r7.send_gifs;
        r9 = r6.send_gifs;
        if (r5 != r9) goto L_0x06c9;
    L_0x06c3:
        r5 = r7.send_games;
        r9 = r6.send_games;
        if (r5 == r9) goto L_0x06f4;
    L_0x06c9:
        if (r8 != 0) goto L_0x06d2;
    L_0x06cb:
        r5 = 10;
        r11.append(r5);
        r8 = 1;
        goto L_0x06d4;
    L_0x06d2:
        r5 = 10;
    L_0x06d4:
        r11.append(r5);
        r5 = r6.send_stickers;
        if (r5 != 0) goto L_0x06de;
    L_0x06db:
        r5 = 43;
        goto L_0x06e0;
    L_0x06de:
        r5 = 45;
    L_0x06e0:
        r11.append(r5);
        r5 = 32;
        r11.append(r5);
        r5 = 2131559457; // 0x7f0d0421 float:1.8744259E38 double:1.0531303E-314;
        r9 = "EventLogRestrictedSendStickers";
        r5 = org.telegram.messenger.LocaleController.getString(r9, r5);
        r11.append(r5);
    L_0x06f4:
        r5 = r7.send_media;
        r9 = r6.send_media;
        if (r5 == r9) goto L_0x0725;
    L_0x06fa:
        if (r8 != 0) goto L_0x0703;
    L_0x06fc:
        r5 = 10;
        r11.append(r5);
        r8 = 1;
        goto L_0x0705;
    L_0x0703:
        r5 = 10;
    L_0x0705:
        r11.append(r5);
        r5 = r6.send_media;
        if (r5 != 0) goto L_0x070f;
    L_0x070c:
        r5 = 43;
        goto L_0x0711;
    L_0x070f:
        r5 = 45;
    L_0x0711:
        r11.append(r5);
        r5 = 32;
        r11.append(r5);
        r5 = 2131559454; // 0x7f0d041e float:1.8744253E38 double:1.0531302983E-314;
        r9 = "EventLogRestrictedSendMedia";
        r5 = org.telegram.messenger.LocaleController.getString(r9, r5);
        r11.append(r5);
    L_0x0725:
        r5 = r7.send_polls;
        r9 = r6.send_polls;
        if (r5 == r9) goto L_0x0756;
    L_0x072b:
        if (r8 != 0) goto L_0x0734;
    L_0x072d:
        r5 = 10;
        r11.append(r5);
        r8 = 1;
        goto L_0x0736;
    L_0x0734:
        r5 = 10;
    L_0x0736:
        r11.append(r5);
        r5 = r6.send_polls;
        if (r5 != 0) goto L_0x0740;
    L_0x073d:
        r5 = 43;
        goto L_0x0742;
    L_0x0740:
        r5 = 45;
    L_0x0742:
        r11.append(r5);
        r5 = 32;
        r11.append(r5);
        r5 = 2131559456; // 0x7f0d0420 float:1.8744257E38 double:1.0531302993E-314;
        r9 = "EventLogRestrictedSendPolls";
        r5 = org.telegram.messenger.LocaleController.getString(r9, r5);
        r11.append(r5);
    L_0x0756:
        r5 = r7.embed_links;
        r9 = r6.embed_links;
        if (r5 == r9) goto L_0x0787;
    L_0x075c:
        if (r8 != 0) goto L_0x0765;
    L_0x075e:
        r5 = 10;
        r11.append(r5);
        r8 = 1;
        goto L_0x0767;
    L_0x0765:
        r5 = 10;
    L_0x0767:
        r11.append(r5);
        r5 = r6.embed_links;
        if (r5 != 0) goto L_0x0771;
    L_0x076e:
        r5 = 43;
        goto L_0x0773;
    L_0x0771:
        r5 = 45;
    L_0x0773:
        r11.append(r5);
        r5 = 32;
        r11.append(r5);
        r5 = 2131559453; // 0x7f0d041d float:1.874425E38 double:1.053130298E-314;
        r9 = "EventLogRestrictedSendEmbed";
        r5 = org.telegram.messenger.LocaleController.getString(r9, r5);
        r11.append(r5);
    L_0x0787:
        r5 = r7.change_info;
        r9 = r6.change_info;
        if (r5 == r9) goto L_0x07b8;
    L_0x078d:
        if (r8 != 0) goto L_0x0796;
    L_0x078f:
        r5 = 10;
        r11.append(r5);
        r8 = 1;
        goto L_0x0798;
    L_0x0796:
        r5 = 10;
    L_0x0798:
        r11.append(r5);
        r5 = r6.change_info;
        if (r5 != 0) goto L_0x07a2;
    L_0x079f:
        r5 = 43;
        goto L_0x07a4;
    L_0x07a2:
        r5 = 45;
    L_0x07a4:
        r11.append(r5);
        r5 = 32;
        r11.append(r5);
        r5 = 2131559449; // 0x7f0d0419 float:1.8744242E38 double:1.053130296E-314;
        r9 = "EventLogRestrictedChangeInfo";
        r5 = org.telegram.messenger.LocaleController.getString(r9, r5);
        r11.append(r5);
    L_0x07b8:
        r5 = r7.invite_users;
        r9 = r6.invite_users;
        if (r5 == r9) goto L_0x07e9;
    L_0x07be:
        if (r8 != 0) goto L_0x07c7;
    L_0x07c0:
        r5 = 10;
        r11.append(r5);
        r8 = 1;
        goto L_0x07c9;
    L_0x07c7:
        r5 = 10;
    L_0x07c9:
        r11.append(r5);
        r5 = r6.invite_users;
        if (r5 != 0) goto L_0x07d3;
    L_0x07d0:
        r5 = 43;
        goto L_0x07d5;
    L_0x07d3:
        r5 = 45;
    L_0x07d5:
        r11.append(r5);
        r5 = 32;
        r11.append(r5);
        r5 = 2131559450; // 0x7f0d041a float:1.8744244E38 double:1.0531302963E-314;
        r9 = "EventLogRestrictedInviteUsers";
        r5 = org.telegram.messenger.LocaleController.getString(r9, r5);
        r11.append(r5);
    L_0x07e9:
        r5 = r7.pin_messages;
        r7 = r6.pin_messages;
        if (r5 == r7) goto L_0x0819;
    L_0x07ef:
        if (r8 != 0) goto L_0x07f7;
    L_0x07f1:
        r5 = 10;
        r11.append(r5);
        goto L_0x07f9;
    L_0x07f7:
        r5 = 10;
    L_0x07f9:
        r11.append(r5);
        r5 = r6.pin_messages;
        if (r5 != 0) goto L_0x0803;
    L_0x0800:
        r5 = 43;
        goto L_0x0805;
    L_0x0803:
        r5 = 45;
    L_0x0805:
        r11.append(r5);
        r5 = 32;
        r11.append(r5);
        r5 = 2131559451; // 0x7f0d041b float:1.8744246E38 double:1.053130297E-314;
        r6 = "EventLogRestrictedPinMessages";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r11.append(r5);
    L_0x0819:
        r5 = r11.toString();
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x0821:
        if (r6 == 0) goto L_0x0833;
    L_0x0823:
        if (r7 == 0) goto L_0x0829;
    L_0x0825:
        r6 = r6.view_messages;
        if (r6 == 0) goto L_0x0833;
    L_0x0829:
        r6 = 2131559392; // 0x7f0d03e0 float:1.8744127E38 double:1.0531302677E-314;
        r7 = "EventLogChannelRestricted";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
        goto L_0x083c;
    L_0x0833:
        r6 = 2131559393; // 0x7f0d03e1 float:1.8744129E38 double:1.053130268E-314;
        r7 = "EventLogChannelUnrestricted";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
    L_0x083c:
        r7 = r6.indexOf(r15);
        r8 = 1;
        r9 = new java.lang.Object[r8];
        r8 = r0.messageOwner;
        r8 = r8.entities;
        r5 = r0.getUserName(r5, r8, r7);
        r7 = 0;
        r9[r7] = r5;
        r5 = java.lang.String.format(r6, r9);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x0856:
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionUpdatePinned;
        if (r8 == 0) goto L_0x08d3;
    L_0x085a:
        if (r4 == 0) goto L_0x08a9;
    L_0x085c:
        r5 = r4.f534id;
        r6 = 136817688; // 0x827ac18 float:5.045703E-34 double:6.75969194E-316;
        if (r5 != r6) goto L_0x08a9;
    L_0x0863:
        r5 = r7.message;
        r5 = r5.fwd_from;
        if (r5 == 0) goto L_0x08a9;
    L_0x0869:
        r5 = r0.currentAccount;
        r5 = org.telegram.messenger.MessagesController.getInstance(r5);
        r6 = r1.action;
        r6 = r6.message;
        r6 = r6.fwd_from;
        r6 = r6.channel_id;
        r6 = java.lang.Integer.valueOf(r6);
        r5 = r5.getChat(r6);
        r6 = r1.action;
        r6 = r6.message;
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_messageEmpty;
        if (r6 == 0) goto L_0x0898;
    L_0x0887:
        r6 = 2131559467; // 0x7f0d042b float:1.8744279E38 double:1.0531303047E-314;
        r7 = "EventLogUnpinnedMessages";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
        r5 = r0.replaceWithLink(r6, r13, r5);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x0898:
        r6 = 2131559429; // 0x7f0d0405 float:1.8744202E38 double:1.053130286E-314;
        r7 = "EventLogPinnedMessages";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
        r5 = r0.replaceWithLink(r6, r13, r5);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x08a9:
        r5 = r1.action;
        r5 = r5.message;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageEmpty;
        if (r5 == 0) goto L_0x08c2;
    L_0x08b1:
        r5 = 2131559467; // 0x7f0d042b float:1.8744279E38 double:1.0531303047E-314;
        r6 = "EventLogUnpinnedMessages";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x08c2:
        r5 = 2131559429; // 0x7f0d0405 float:1.8744202E38 double:1.053130286E-314;
        r6 = "EventLogPinnedMessages";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x08d3:
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionStopPoll;
        if (r8 == 0) goto L_0x08e8;
    L_0x08d7:
        r5 = 2131559460; // 0x7f0d0424 float:1.8744265E38 double:1.0531303013E-314;
        r6 = "EventLogStopPoll";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x08e8:
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionToggleSignatures;
        if (r8 == 0) goto L_0x0914;
    L_0x08ec:
        r7 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionToggleSignatures) r7;
        r5 = r7.new_value;
        if (r5 == 0) goto L_0x0903;
    L_0x08f2:
        r5 = 2131559466; // 0x7f0d042a float:1.8744277E38 double:1.053130304E-314;
        r6 = "EventLogToggledSignaturesOn";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x0903:
        r5 = 2131559465; // 0x7f0d0429 float:1.8744275E38 double:1.0531303037E-314;
        r6 = "EventLogToggledSignaturesOff";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x0914:
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionToggleInvites;
        if (r8 == 0) goto L_0x0940;
    L_0x0918:
        r7 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionToggleInvites) r7;
        r5 = r7.new_value;
        if (r5 == 0) goto L_0x092f;
    L_0x091e:
        r5 = 2131559464; // 0x7f0d0428 float:1.8744273E38 double:1.053130303E-314;
        r6 = "EventLogToggledInvitesOn";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x092f:
        r5 = 2131559463; // 0x7f0d0427 float:1.874427E38 double:1.0531303027E-314;
        r6 = "EventLogToggledInvitesOff";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x0940:
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionDeleteMessage;
        if (r8 == 0) goto L_0x0955;
    L_0x0944:
        r5 = 2131559395; // 0x7f0d03e3 float:1.8744133E38 double:1.053130269E-314;
        r6 = "EventLogDeletedMessages";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x0955:
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeLinkedChat;
        if (r8 == 0) goto L_0x0a06;
    L_0x0959:
        r6 = r7;
        r6 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeLinkedChat) r6;
        r6 = r6.new_value;
        r7 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeLinkedChat) r7;
        r7 = r7.prev_value;
        r8 = r3.megagroup;
        if (r8 == 0) goto L_0x09b6;
    L_0x0966:
        if (r6 != 0) goto L_0x098f;
    L_0x0968:
        r6 = r0.currentAccount;
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);
        r7 = java.lang.Integer.valueOf(r7);
        r6 = r6.getChat(r7);
        r7 = 2131559445; // 0x7f0d0415 float:1.8744234E38 double:1.053130294E-314;
        r8 = "EventLogRemovedLinkedChannel";
        r7 = org.telegram.messenger.LocaleController.getString(r8, r7);
        r7 = r0.replaceWithLink(r7, r13, r4);
        r0.messageText = r7;
        r7 = r0.messageText;
        r5 = r0.replaceWithLink(r7, r5, r6);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x098f:
        r7 = r0.currentAccount;
        r7 = org.telegram.messenger.MessagesController.getInstance(r7);
        r6 = java.lang.Integer.valueOf(r6);
        r6 = r7.getChat(r6);
        r7 = 2131559388; // 0x7f0d03dc float:1.8744119E38 double:1.0531302657E-314;
        r8 = "EventLogChangedLinkedChannel";
        r7 = org.telegram.messenger.LocaleController.getString(r8, r7);
        r7 = r0.replaceWithLink(r7, r13, r4);
        r0.messageText = r7;
        r7 = r0.messageText;
        r5 = r0.replaceWithLink(r7, r5, r6);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x09b6:
        if (r6 != 0) goto L_0x09df;
    L_0x09b8:
        r6 = r0.currentAccount;
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);
        r7 = java.lang.Integer.valueOf(r7);
        r6 = r6.getChat(r7);
        r7 = 2131559446; // 0x7f0d0416 float:1.8744236E38 double:1.0531302943E-314;
        r8 = "EventLogRemovedLinkedGroup";
        r7 = org.telegram.messenger.LocaleController.getString(r8, r7);
        r7 = r0.replaceWithLink(r7, r13, r4);
        r0.messageText = r7;
        r7 = r0.messageText;
        r5 = r0.replaceWithLink(r7, r5, r6);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x09df:
        r7 = r0.currentAccount;
        r7 = org.telegram.messenger.MessagesController.getInstance(r7);
        r6 = java.lang.Integer.valueOf(r6);
        r6 = r7.getChat(r6);
        r7 = 2131559389; // 0x7f0d03dd float:1.874412E38 double:1.053130266E-314;
        r8 = "EventLogChangedLinkedGroup";
        r7 = org.telegram.messenger.LocaleController.getString(r8, r7);
        r7 = r0.replaceWithLink(r7, r13, r4);
        r0.messageText = r7;
        r7 = r0.messageText;
        r5 = r0.replaceWithLink(r7, r5, r6);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x0a06:
        r5 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionTogglePreHistoryHidden;
        if (r5 == 0) goto L_0x0a32;
    L_0x0a0a:
        r7 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionTogglePreHistoryHidden) r7;
        r5 = r7.new_value;
        if (r5 == 0) goto L_0x0a21;
    L_0x0a10:
        r5 = 2131559461; // 0x7f0d0425 float:1.8744267E38 double:1.0531303017E-314;
        r6 = "EventLogToggledInvitesHistoryOff";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x0a21:
        r5 = 2131559462; // 0x7f0d0426 float:1.8744269E38 double:1.053130302E-314;
        r6 = "EventLogToggledInvitesHistoryOn";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x0a32:
        r5 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeAbout;
        if (r5 == 0) goto L_0x0ab4;
    L_0x0a36:
        r5 = r3.megagroup;
        if (r5 == 0) goto L_0x0a40;
    L_0x0a3a:
        r5 = 2131559400; // 0x7f0d03e8 float:1.8744143E38 double:1.0531302716E-314;
        r7 = "EventLogEditedGroupDescription";
        goto L_0x0a45;
    L_0x0a40:
        r5 = 2131559397; // 0x7f0d03e5 float:1.8744137E38 double:1.05313027E-314;
        r7 = "EventLogEditedChannelDescription";
    L_0x0a45:
        r5 = org.telegram.messenger.LocaleController.getString(r7, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        r5 = new org.telegram.tgnet.TLRPC$TL_message;
        r5.<init>();
        r7 = 0;
        r5.out = r7;
        r5.unread = r7;
        r7 = r1.user_id;
        r5.from_id = r7;
        r5.to_id = r6;
        r6 = r1.date;
        r5.date = r6;
        r6 = r1.action;
        r7 = r6;
        r7 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeAbout) r7;
        r7 = r7.new_value;
        r5.message = r7;
        r6 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeAbout) r6;
        r6 = r6.prev_value;
        r6 = android.text.TextUtils.isEmpty(r6);
        if (r6 != 0) goto L_0x0aab;
    L_0x0a76:
        r6 = new org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
        r6.<init>();
        r5.media = r6;
        r6 = r5.media;
        r7 = new org.telegram.tgnet.TLRPC$TL_webPage;
        r7.<init>();
        r6.webpage = r7;
        r6 = r5.media;
        r6 = r6.webpage;
        r7 = 10;
        r6.flags = r7;
        r6.display_url = r12;
        r6.url = r12;
        r7 = 2131559430; // 0x7f0d0406 float:1.8744204E38 double:1.0531302864E-314;
        r8 = "EventLogPreviousGroupDescription";
        r7 = org.telegram.messenger.LocaleController.getString(r8, r7);
        r6.site_name = r7;
        r6 = r5.media;
        r6 = r6.webpage;
        r7 = r1.action;
        r7 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeAbout) r7;
        r7 = r7.prev_value;
        r6.description = r7;
        goto L_0x0d47;
    L_0x0aab:
        r6 = new org.telegram.tgnet.TLRPC$TL_messageMediaEmpty;
        r6.<init>();
        r5.media = r6;
        goto L_0x0d47;
    L_0x0ab4:
        r5 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeUsername;
        if (r5 == 0) goto L_0x0bb1;
    L_0x0ab8:
        r7 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeUsername) r7;
        r5 = r7.new_value;
        r7 = android.text.TextUtils.isEmpty(r5);
        if (r7 != 0) goto L_0x0adc;
    L_0x0ac2:
        r7 = r3.megagroup;
        if (r7 == 0) goto L_0x0acc;
    L_0x0ac6:
        r7 = 2131559387; // 0x7f0d03db float:1.8744117E38 double:1.053130265E-314;
        r8 = "EventLogChangedGroupLink";
        goto L_0x0ad1;
    L_0x0acc:
        r7 = 2131559386; // 0x7f0d03da float:1.8744115E38 double:1.0531302647E-314;
        r8 = "EventLogChangedChannelLink";
    L_0x0ad1:
        r7 = org.telegram.messenger.LocaleController.getString(r8, r7);
        r7 = r0.replaceWithLink(r7, r13, r4);
        r0.messageText = r7;
        goto L_0x0af5;
    L_0x0adc:
        r7 = r3.megagroup;
        if (r7 == 0) goto L_0x0ae6;
    L_0x0ae0:
        r7 = 2131559444; // 0x7f0d0414 float:1.8744232E38 double:1.0531302933E-314;
        r8 = "EventLogRemovedGroupLink";
        goto L_0x0aeb;
    L_0x0ae6:
        r7 = 2131559442; // 0x7f0d0412 float:1.8744228E38 double:1.0531302924E-314;
        r8 = "EventLogRemovedChannelLink";
    L_0x0aeb:
        r7 = org.telegram.messenger.LocaleController.getString(r8, r7);
        r7 = r0.replaceWithLink(r7, r13, r4);
        r0.messageText = r7;
    L_0x0af5:
        r7 = new org.telegram.tgnet.TLRPC$TL_message;
        r7.<init>();
        r8 = 0;
        r7.out = r8;
        r7.unread = r8;
        r8 = r1.user_id;
        r7.from_id = r8;
        r7.to_id = r6;
        r6 = r1.date;
        r7.date = r6;
        r6 = android.text.TextUtils.isEmpty(r5);
        if (r6 != 0) goto L_0x0b31;
    L_0x0b0f:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r8 = "https://";
        r6.append(r8);
        r8 = org.telegram.messenger.MessagesController.getInstance(r30);
        r8 = r8.linkPrefix;
        r6.append(r8);
        r8 = "/";
        r6.append(r8);
        r6.append(r5);
        r5 = r6.toString();
        r7.message = r5;
        goto L_0x0b33;
    L_0x0b31:
        r7.message = r12;
    L_0x0b33:
        r5 = new org.telegram.tgnet.TLRPC$TL_messageEntityUrl;
        r5.<init>();
        r6 = 0;
        r5.offset = r6;
        r6 = r7.message;
        r6 = r6.length();
        r5.length = r6;
        r6 = r7.entities;
        r6.add(r5);
        r5 = r1.action;
        r5 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeUsername) r5;
        r5 = r5.prev_value;
        r5 = android.text.TextUtils.isEmpty(r5);
        if (r5 != 0) goto L_0x0ba7;
    L_0x0b54:
        r5 = new org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
        r5.<init>();
        r7.media = r5;
        r5 = r7.media;
        r6 = new org.telegram.tgnet.TLRPC$TL_webPage;
        r6.<init>();
        r5.webpage = r6;
        r5 = r7.media;
        r5 = r5.webpage;
        r6 = 10;
        r5.flags = r6;
        r5.display_url = r12;
        r5.url = r12;
        r6 = 2131559431; // 0x7f0d0407 float:1.8744206E38 double:1.053130287E-314;
        r8 = "EventLogPreviousLink";
        r6 = org.telegram.messenger.LocaleController.getString(r8, r6);
        r5.site_name = r6;
        r5 = r7.media;
        r5 = r5.webpage;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r8 = "https://";
        r6.append(r8);
        r8 = org.telegram.messenger.MessagesController.getInstance(r30);
        r8 = r8.linkPrefix;
        r6.append(r8);
        r8 = "/";
        r6.append(r8);
        r8 = r1.action;
        r8 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeUsername) r8;
        r8 = r8.prev_value;
        r6.append(r8);
        r6 = r6.toString();
        r5.description = r6;
        goto L_0x0bae;
    L_0x0ba7:
        r5 = new org.telegram.tgnet.TLRPC$TL_messageMediaEmpty;
        r5.<init>();
        r7.media = r5;
    L_0x0bae:
        r5 = r7;
        goto L_0x0d47;
    L_0x0bb1:
        r5 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionEditMessage;
        if (r5 == 0) goto L_0x0cfd;
    L_0x0bb5:
        r5 = new org.telegram.tgnet.TLRPC$TL_message;
        r5.<init>();
        r7 = 0;
        r5.out = r7;
        r5.unread = r7;
        r7 = r1.user_id;
        r5.from_id = r7;
        r5.to_id = r6;
        r6 = r1.date;
        r5.date = r6;
        r6 = r1.action;
        r7 = r6;
        r7 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionEditMessage) r7;
        r7 = r7.new_message;
        r6 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionEditMessage) r6;
        r6 = r6.prev_message;
        r8 = r7.media;
        if (r8 == 0) goto L_0x0c98;
    L_0x0bd8:
        r9 = r8 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
        if (r9 != 0) goto L_0x0c98;
    L_0x0bdc:
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r8 != 0) goto L_0x0c98;
    L_0x0be0:
        r8 = r7.message;
        r9 = r6.message;
        r8 = android.text.TextUtils.equals(r8, r9);
        r9 = 1;
        r8 = r8 ^ r9;
        r9 = r7.media;
        r9 = r9.getClass();
        r10 = r6.media;
        r10 = r10.getClass();
        if (r9 != r10) goto L_0x0c23;
    L_0x0bf8:
        r9 = r7.media;
        r9 = r9.photo;
        if (r9 == 0) goto L_0x0c0c;
    L_0x0bfe:
        r10 = r6.media;
        r10 = r10.photo;
        if (r10 == 0) goto L_0x0c0c;
    L_0x0c04:
        r14 = r9.f463id;
        r9 = r10.f463id;
        r11 = (r14 > r9 ? 1 : (r14 == r9 ? 0 : -1));
        if (r11 != 0) goto L_0x0c23;
    L_0x0c0c:
        r9 = r7.media;
        r9 = r9.document;
        if (r9 == 0) goto L_0x0c21;
    L_0x0c12:
        r10 = r6.media;
        r10 = r10.document;
        if (r10 == 0) goto L_0x0c21;
    L_0x0c18:
        r14 = r9.f441id;
        r9 = r10.f441id;
        r11 = (r14 > r9 ? 1 : (r14 == r9 ? 0 : -1));
        if (r11 == 0) goto L_0x0c21;
    L_0x0c20:
        goto L_0x0c23;
    L_0x0c21:
        r9 = 0;
        goto L_0x0c24;
    L_0x0c23:
        r9 = 1;
    L_0x0c24:
        if (r9 == 0) goto L_0x0c38;
    L_0x0c26:
        if (r8 == 0) goto L_0x0c38;
    L_0x0c28:
        r9 = 2131559404; // 0x7f0d03ec float:1.8744151E38 double:1.0531302736E-314;
        r10 = "EventLogEditedMediaCaption";
        r9 = org.telegram.messenger.LocaleController.getString(r10, r9);
        r9 = r0.replaceWithLink(r9, r13, r4);
        r0.messageText = r9;
        goto L_0x0c59;
    L_0x0c38:
        if (r8 == 0) goto L_0x0c4a;
    L_0x0c3a:
        r9 = 2131559396; // 0x7f0d03e4 float:1.8744135E38 double:1.0531302696E-314;
        r10 = "EventLogEditedCaption";
        r9 = org.telegram.messenger.LocaleController.getString(r10, r9);
        r9 = r0.replaceWithLink(r9, r13, r4);
        r0.messageText = r9;
        goto L_0x0c59;
    L_0x0c4a:
        r9 = 2131559403; // 0x7f0d03eb float:1.874415E38 double:1.053130273E-314;
        r10 = "EventLogEditedMedia";
        r9 = org.telegram.messenger.LocaleController.getString(r10, r9);
        r9 = r0.replaceWithLink(r9, r13, r4);
        r0.messageText = r9;
    L_0x0c59:
        r9 = r7.media;
        r5.media = r9;
        if (r8 == 0) goto L_0x0cea;
    L_0x0c5f:
        r8 = r5.media;
        r9 = new org.telegram.tgnet.TLRPC$TL_webPage;
        r9.<init>();
        r8.webpage = r9;
        r8 = r5.media;
        r8 = r8.webpage;
        r9 = 2131559426; // 0x7f0d0402 float:1.8744196E38 double:1.0531302845E-314;
        r10 = "EventLogOriginalCaption";
        r9 = org.telegram.messenger.LocaleController.getString(r10, r9);
        r8.site_name = r9;
        r8 = r6.message;
        r8 = android.text.TextUtils.isEmpty(r8);
        if (r8 == 0) goto L_0x0c8f;
    L_0x0c7f:
        r6 = r5.media;
        r6 = r6.webpage;
        r8 = 2131559427; // 0x7f0d0403 float:1.8744198E38 double:1.053130285E-314;
        r9 = "EventLogOriginalCaptionEmpty";
        r8 = org.telegram.messenger.LocaleController.getString(r9, r8);
        r6.description = r8;
        goto L_0x0cea;
    L_0x0c8f:
        r8 = r5.media;
        r8 = r8.webpage;
        r6 = r6.message;
        r8.description = r6;
        goto L_0x0cea;
    L_0x0c98:
        r8 = 2131559405; // 0x7f0d03ed float:1.8744153E38 double:1.053130274E-314;
        r9 = "EventLogEditedMessages";
        r8 = org.telegram.messenger.LocaleController.getString(r9, r8);
        r8 = r0.replaceWithLink(r8, r13, r4);
        r0.messageText = r8;
        r8 = r7.message;
        r5.message = r8;
        r8 = new org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
        r8.<init>();
        r5.media = r8;
        r8 = r5.media;
        r9 = new org.telegram.tgnet.TLRPC$TL_webPage;
        r9.<init>();
        r8.webpage = r9;
        r8 = r5.media;
        r8 = r8.webpage;
        r9 = 2131559428; // 0x7f0d0404 float:1.87442E38 double:1.0531302854E-314;
        r10 = "EventLogOriginalMessages";
        r9 = org.telegram.messenger.LocaleController.getString(r10, r9);
        r8.site_name = r9;
        r8 = r6.message;
        r8 = android.text.TextUtils.isEmpty(r8);
        if (r8 == 0) goto L_0x0ce2;
    L_0x0cd2:
        r6 = r5.media;
        r6 = r6.webpage;
        r8 = 2131559427; // 0x7f0d0403 float:1.8744198E38 double:1.053130285E-314;
        r9 = "EventLogOriginalCaptionEmpty";
        r8 = org.telegram.messenger.LocaleController.getString(r9, r8);
        r6.description = r8;
        goto L_0x0cea;
    L_0x0ce2:
        r8 = r5.media;
        r8 = r8.webpage;
        r6 = r6.message;
        r8.description = r6;
    L_0x0cea:
        r6 = r7.reply_markup;
        r5.reply_markup = r6;
        r6 = r5.media;
        r6 = r6.webpage;
        if (r6 == 0) goto L_0x0d47;
    L_0x0cf4:
        r7 = 10;
        r6.flags = r7;
        r6.display_url = r12;
        r6.url = r12;
        goto L_0x0d47;
    L_0x0cfd:
        r5 = r7 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeStickerSet;
        if (r5 == 0) goto L_0x0d31;
    L_0x0d01:
        r5 = r7;
        r5 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeStickerSet) r5;
        r5 = r5.new_stickerset;
        r7 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeStickerSet) r7;
        r6 = r7.new_stickerset;
        if (r5 == 0) goto L_0x0d21;
    L_0x0d0c:
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_inputStickerSetEmpty;
        if (r5 == 0) goto L_0x0d11;
    L_0x0d10:
        goto L_0x0d21;
    L_0x0d11:
        r5 = 2131559390; // 0x7f0d03de float:1.8744123E38 double:1.0531302667E-314;
        r6 = "EventLogChangedStickersSet";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x0d21:
        r5 = 2131559447; // 0x7f0d0417 float:1.8744238E38 double:1.053130295E-314;
        r6 = "EventLogRemovedStickersSet";
        r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
        r5 = r0.replaceWithLink(r5, r13, r4);
        r0.messageText = r5;
        goto L_0x0d46;
    L_0x0d31:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "unsupported ";
        r5.append(r6);
        r6 = r1.action;
        r5.append(r6);
        r5 = r5.toString();
        r0.messageText = r5;
    L_0x0d46:
        r5 = 0;
    L_0x0d47:
        r6 = r0.messageOwner;
        if (r6 != 0) goto L_0x0d52;
    L_0x0d4b:
        r6 = new org.telegram.tgnet.TLRPC$TL_messageService;
        r6.<init>();
        r0.messageOwner = r6;
    L_0x0d52:
        r6 = r0.messageOwner;
        r7 = r0.messageText;
        r7 = r7.toString();
        r6.message = r7;
        r6 = r0.messageOwner;
        r7 = r1.user_id;
        r6.from_id = r7;
        r7 = r1.date;
        r6.date = r7;
        r7 = 0;
        r8 = r35[r7];
        r9 = r8 + 1;
        r35[r7] = r9;
        r6.f461id = r8;
        r8 = r1.f470id;
        r0.eventId = r8;
        r6.out = r7;
        r8 = new org.telegram.tgnet.TLRPC$TL_peerChannel;
        r8.<init>();
        r6.to_id = r8;
        r6 = r0.messageOwner;
        r8 = r6.to_id;
        r9 = r3.f434id;
        r8.channel_id = r9;
        r6.unread = r7;
        r7 = r3.megagroup;
        if (r7 == 0) goto L_0x0d91;
    L_0x0d8a:
        r7 = r6.flags;
        r8 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r7 = r7 | r8;
        r6.flags = r7;
    L_0x0d91:
        r6 = org.telegram.messenger.MediaController.getInstance();
        r7 = r1.action;
        r7 = r7.message;
        if (r7 == 0) goto L_0x0da0;
    L_0x0d9b:
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_messageEmpty;
        if (r8 != 0) goto L_0x0da0;
    L_0x0d9f:
        r5 = r7;
    L_0x0da0:
        if (r5 == 0) goto L_0x0dff;
    L_0x0da2:
        r7 = 0;
        r5.out = r7;
        r8 = r35[r7];
        r9 = r8 + 1;
        r35[r7] = r9;
        r5.f461id = r8;
        r5.reply_to_msg_id = r7;
        r7 = r5.flags;
        r8 = -32769; // 0xffffffffffff7fff float:NaN double:NaN;
        r7 = r7 & r8;
        r5.flags = r7;
        r3 = r3.megagroup;
        if (r3 == 0) goto L_0x0dc2;
    L_0x0dbb:
        r3 = r5.flags;
        r7 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r3 = r3 | r7;
        r5.flags = r3;
    L_0x0dc2:
        r3 = new org.telegram.messenger.MessageObject;
        r23 = 0;
        r24 = 0;
        r25 = 1;
        r7 = r0.eventId;
        r20 = r3;
        r21 = r30;
        r22 = r5;
        r26 = r7;
        r20.<init>(r21, r22, r23, r24, r25, r26);
        r5 = r3.contentType;
        if (r5 < 0) goto L_0x0dfa;
    L_0x0ddb:
        r5 = r6.isPlayingMessage(r3);
        if (r5 == 0) goto L_0x0ded;
    L_0x0de1:
        r5 = r6.getPlayingMessageObject();
        r7 = r5.audioProgress;
        r3.audioProgress = r7;
        r5 = r5.audioProgressSec;
        r3.audioProgressSec = r5;
    L_0x0ded:
        r29.createDateArray(r30, r31, r32, r33);
        r5 = r32.size();
        r7 = 1;
        r5 = r5 - r7;
        r2.add(r5, r3);
        goto L_0x0e00;
    L_0x0dfa:
        r7 = 1;
        r3 = -1;
        r0.contentType = r3;
        goto L_0x0e00;
    L_0x0dff:
        r7 = 1;
    L_0x0e00:
        r3 = r0.contentType;
        if (r3 < 0) goto L_0x0e69;
    L_0x0e04:
        r29.createDateArray(r30, r31, r32, r33);
        r1 = r32.size();
        r1 = r1 - r7;
        r2.add(r1, r0);
        r1 = r0.messageText;
        if (r1 != 0) goto L_0x0e15;
    L_0x0e13:
        r0.messageText = r12;
    L_0x0e15:
        r29.setType();
        r29.measureInlineBotButtons();
        r29.generateCaption();
        r1 = r0.messageOwner;
        r1 = r1.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r1 == 0) goto L_0x0e29;
    L_0x0e26:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_msgGameTextPaint;
        goto L_0x0e2b;
    L_0x0e29:
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_msgTextPaint;
    L_0x0e2b:
        r2 = org.telegram.messenger.SharedConfig.allowBigEmoji;
        if (r2 == 0) goto L_0x0e33;
    L_0x0e2f:
        r2 = 1;
        r5 = new int[r2];
        goto L_0x0e34;
    L_0x0e33:
        r5 = 0;
    L_0x0e34:
        r2 = r0.messageText;
        r1 = r1.getFontMetricsInt();
        r3 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r7 = 0;
        r1 = org.telegram.messenger.Emoji.replaceEmoji(r2, r1, r3, r7, r5);
        r0.messageText = r1;
        r0.checkEmojiOnly(r5);
        r1 = r6.isPlayingMessage(r0);
        if (r1 == 0) goto L_0x0e5c;
    L_0x0e50:
        r1 = r6.getPlayingMessageObject();
        r2 = r1.audioProgress;
        r0.audioProgress = r2;
        r1 = r1.audioProgressSec;
        r0.audioProgressSec = r1;
    L_0x0e5c:
        r0.generateLayout(r4);
        r1 = 1;
        r0.layoutCreated = r1;
        r1 = 0;
        r0.generateThumbs(r1);
        r29.checkMediaExistance();
    L_0x0e69:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.<init>(int, org.telegram.tgnet.TLRPC$TL_channelAdminLogEvent, java.util.ArrayList, java.util.HashMap, org.telegram.tgnet.TLRPC$Chat, int[]):void");
    }

    private String getUserName(User user, ArrayList<MessageEntity> arrayList, int i) {
        String formatName = user == null ? "" : ContactsController.formatName(user.first_name, user.last_name);
        if (i >= 0) {
            TL_messageEntityMentionName tL_messageEntityMentionName = new TL_messageEntityMentionName();
            tL_messageEntityMentionName.user_id = user.f534id;
            tL_messageEntityMentionName.offset = i;
            tL_messageEntityMentionName.length = formatName.length();
            arrayList.add(tL_messageEntityMentionName);
        }
        if (TextUtils.isEmpty(user.username)) {
            return formatName;
        }
        if (i >= 0) {
            TL_messageEntityMentionName tL_messageEntityMentionName2 = new TL_messageEntityMentionName();
            tL_messageEntityMentionName2.user_id = user.f534id;
            tL_messageEntityMentionName2.offset = (i + formatName.length()) + 2;
            tL_messageEntityMentionName2.length = user.username.length() + 1;
            arrayList.add(tL_messageEntityMentionName2);
        }
        return String.format("%1$s (@%2$s)", new Object[]{formatName, user.username});
    }

    public void applyNewText() {
        if (!TextUtils.isEmpty(this.messageOwner.message)) {
            TextPaint textPaint;
            int[] iArr = null;
            User user = isFromUser() ? MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.messageOwner.from_id)) : null;
            Message message = this.messageOwner;
            this.messageText = message.message;
            if (message.media instanceof TL_messageMediaGame) {
                textPaint = Theme.chat_msgGameTextPaint;
            } else {
                textPaint = Theme.chat_msgTextPaint;
            }
            if (SharedConfig.allowBigEmoji) {
                iArr = new int[1];
            }
            this.messageText = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.m26dp(20.0f), false, iArr);
            checkEmojiOnly(iArr);
            generateLayout(user);
        }
    }

    public void generateGameMessageText(User user) {
        if (user == null && this.messageOwner.from_id > 0) {
            user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.messageOwner.from_id));
        }
        TLObject tLObject = null;
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null) {
            MessageMedia messageMedia = messageObject.messageOwner.media;
            if (messageMedia != null) {
                TLObject tLObject2 = messageMedia.game;
                if (tLObject2 != null) {
                    tLObject = tLObject2;
                }
            }
        }
        String str = "un1";
        String str2 = "Points";
        if (tLObject != null) {
            if (user == null || user.f534id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                this.messageText = replaceWithLink(LocaleController.formatString("ActionUserScoredInGame", C1067R.string.ActionUserScoredInGame, LocaleController.formatPluralString(str2, this.messageOwner.action.score)), str, user);
            } else {
                this.messageText = LocaleController.formatString("ActionYouScoredInGame", C1067R.string.ActionYouScoredInGame, LocaleController.formatPluralString(str2, this.messageOwner.action.score));
            }
            this.messageText = replaceWithLink(this.messageText, "un2", tLObject);
        } else if (user == null || user.f534id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            this.messageText = replaceWithLink(LocaleController.formatString("ActionUserScored", C1067R.string.ActionUserScored, LocaleController.formatPluralString(str2, this.messageOwner.action.score)), str, user);
        } else {
            this.messageText = LocaleController.formatString("ActionYouScored", C1067R.string.ActionYouScored, LocaleController.formatPluralString(str2, this.messageOwner.action.score));
        }
    }

    public boolean hasValidReplyMessageObject() {
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null) {
            Message message = messageObject.messageOwner;
            if (!((message instanceof TL_messageEmpty) || (message.action instanceof TL_messageActionHistoryClear))) {
                return true;
            }
        }
        return false;
    }

    public void generatePaymentSentMessageText(User user) {
        if (user == null) {
            user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf((int) getDialogId()));
        }
        String firstName = user != null ? UserObject.getFirstName(user) : "";
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject == null || !(messageObject.messageOwner.media instanceof TL_messageMediaInvoice)) {
            Object[] objArr = new Object[2];
            LocaleController instance = LocaleController.getInstance();
            MessageAction messageAction = this.messageOwner.action;
            objArr[0] = instance.formatCurrencyString(messageAction.total_amount, messageAction.currency);
            objArr[1] = firstName;
            this.messageText = LocaleController.formatString("PaymentSuccessfullyPaidNoItem", C1067R.string.PaymentSuccessfullyPaidNoItem, objArr);
            return;
        }
        r4 = new Object[3];
        LocaleController instance2 = LocaleController.getInstance();
        MessageAction messageAction2 = this.messageOwner.action;
        r4[0] = instance2.formatCurrencyString(messageAction2.total_amount, messageAction2.currency);
        r4[1] = firstName;
        r4[2] = this.replyMessageObject.messageOwner.media.title;
        this.messageText = LocaleController.formatString("PaymentSuccessfullyPaid", C1067R.string.PaymentSuccessfullyPaid, r4);
    }

    public void generatePinMessageText(User user, Chat chat) {
        TLObject user2;
        TLObject chat2;
        String string;
        if (user == null && chat2 == null) {
            if (this.messageOwner.from_id > 0) {
                user2 = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.messageOwner.from_id));
            }
            if (user2 == null) {
                chat2 = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.messageOwner.to_id.channel_id));
            }
        }
        MessageObject messageObject = this.replyMessageObject;
        String str = "ActionPinnedNoText";
        String str2 = "un1";
        if (messageObject != null) {
            Message message = messageObject.messageOwner;
            if (!((message instanceof TL_messageEmpty) || (message.action instanceof TL_messageActionHistoryClear))) {
                if (messageObject.isMusic()) {
                    string = LocaleController.getString("ActionPinnedMusic", C1067R.string.ActionPinnedMusic);
                    if (user2 == null) {
                        user2 = chat2;
                    }
                    this.messageText = replaceWithLink(string, str2, user2);
                    return;
                } else if (this.replyMessageObject.isVideo()) {
                    string = LocaleController.getString("ActionPinnedVideo", C1067R.string.ActionPinnedVideo);
                    if (user2 == null) {
                        user2 = chat2;
                    }
                    this.messageText = replaceWithLink(string, str2, user2);
                    return;
                } else if (this.replyMessageObject.isGif()) {
                    string = LocaleController.getString("ActionPinnedGif", C1067R.string.ActionPinnedGif);
                    if (user2 == null) {
                        user2 = chat2;
                    }
                    this.messageText = replaceWithLink(string, str2, user2);
                    return;
                } else if (this.replyMessageObject.isVoice()) {
                    string = LocaleController.getString("ActionPinnedVoice", C1067R.string.ActionPinnedVoice);
                    if (user2 == null) {
                        user2 = chat2;
                    }
                    this.messageText = replaceWithLink(string, str2, user2);
                    return;
                } else if (this.replyMessageObject.isRoundVideo()) {
                    string = LocaleController.getString("ActionPinnedRound", C1067R.string.ActionPinnedRound);
                    if (user2 == null) {
                        user2 = chat2;
                    }
                    this.messageText = replaceWithLink(string, str2, user2);
                    return;
                } else if (this.replyMessageObject.isSticker() || this.replyMessageObject.isAnimatedSticker()) {
                    string = LocaleController.getString("ActionPinnedSticker", C1067R.string.ActionPinnedSticker);
                    if (user2 == null) {
                        user2 = chat2;
                    }
                    this.messageText = replaceWithLink(string, str2, user2);
                    return;
                } else {
                    messageObject = this.replyMessageObject;
                    MessageMedia messageMedia = messageObject.messageOwner.media;
                    if (messageMedia instanceof TL_messageMediaDocument) {
                        string = LocaleController.getString("ActionPinnedFile", C1067R.string.ActionPinnedFile);
                        if (user2 == null) {
                            user2 = chat2;
                        }
                        this.messageText = replaceWithLink(string, str2, user2);
                        return;
                    } else if (messageMedia instanceof TL_messageMediaGeo) {
                        string = LocaleController.getString("ActionPinnedGeo", C1067R.string.ActionPinnedGeo);
                        if (user2 == null) {
                            user2 = chat2;
                        }
                        this.messageText = replaceWithLink(string, str2, user2);
                        return;
                    } else if (messageMedia instanceof TL_messageMediaGeoLive) {
                        string = LocaleController.getString("ActionPinnedGeoLive", C1067R.string.ActionPinnedGeoLive);
                        if (user2 == null) {
                            user2 = chat2;
                        }
                        this.messageText = replaceWithLink(string, str2, user2);
                        return;
                    } else if (messageMedia instanceof TL_messageMediaContact) {
                        string = LocaleController.getString("ActionPinnedContact", C1067R.string.ActionPinnedContact);
                        if (user2 == null) {
                            user2 = chat2;
                        }
                        this.messageText = replaceWithLink(string, str2, user2);
                        return;
                    } else if (messageMedia instanceof TL_messageMediaPoll) {
                        string = LocaleController.getString("ActionPinnedPoll", C1067R.string.ActionPinnedPoll);
                        if (user2 == null) {
                            user2 = chat2;
                        }
                        this.messageText = replaceWithLink(string, str2, user2);
                        return;
                    } else if (messageMedia instanceof TL_messageMediaPhoto) {
                        string = LocaleController.getString("ActionPinnedPhoto", C1067R.string.ActionPinnedPhoto);
                        if (user2 == null) {
                            user2 = chat2;
                        }
                        this.messageText = replaceWithLink(string, str2, user2);
                        return;
                    } else if (messageMedia instanceof TL_messageMediaGame) {
                        Object[] objArr = new Object[1];
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("🎮 ");
                        stringBuilder.append(this.replyMessageObject.messageOwner.media.game.title);
                        objArr[0] = stringBuilder.toString();
                        string = LocaleController.formatString("ActionPinnedGame", C1067R.string.ActionPinnedGame, objArr);
                        if (user2 == null) {
                            user2 = chat2;
                        }
                        this.messageText = replaceWithLink(string, str2, user2);
                        this.messageText = Emoji.replaceEmoji(this.messageText, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.m26dp(20.0f), false);
                        return;
                    } else {
                        CharSequence charSequence = messageObject.messageText;
                        if (charSequence == null || charSequence.length() <= 0) {
                            string = LocaleController.getString(str, C1067R.string.ActionPinnedNoText);
                            if (user2 == null) {
                                user2 = chat2;
                            }
                            this.messageText = replaceWithLink(string, str2, user2);
                            return;
                        }
                        charSequence = this.replyMessageObject.messageText;
                        if (charSequence.length() > 20) {
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(charSequence.subSequence(0, 20));
                            stringBuilder2.append("...");
                            charSequence = stringBuilder2.toString();
                        }
                        charSequence = Emoji.replaceEmoji(charSequence, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.m26dp(20.0f), false);
                        string = LocaleController.formatString("ActionPinnedText", C1067R.string.ActionPinnedText, charSequence);
                        if (user2 == null) {
                            user2 = chat2;
                        }
                        this.messageText = replaceWithLink(string, str2, user2);
                        return;
                    }
                }
            }
        }
        string = LocaleController.getString(str, C1067R.string.ActionPinnedNoText);
        if (user2 == null) {
            user2 = chat2;
        }
        this.messageText = replaceWithLink(string, str2, user2);
    }

    public static void updatePollResults(TL_messageMediaPoll tL_messageMediaPoll, TL_pollResults tL_pollResults) {
        if ((tL_pollResults.flags & 2) != 0) {
            int size;
            byte[] bArr = null;
            if (tL_pollResults.min) {
                ArrayList arrayList = tL_messageMediaPoll.results.results;
                if (arrayList != null) {
                    size = arrayList.size();
                    for (int i = 0; i < size; i++) {
                        TL_pollAnswerVoters tL_pollAnswerVoters = (TL_pollAnswerVoters) tL_messageMediaPoll.results.results.get(i);
                        if (tL_pollAnswerVoters.chosen) {
                            bArr = tL_pollAnswerVoters.option;
                            break;
                        }
                    }
                }
            }
            TL_pollResults tL_pollResults2 = tL_messageMediaPoll.results;
            tL_pollResults2.results = tL_pollResults.results;
            if (bArr != null) {
                size = tL_pollResults2.results.size();
                for (int i2 = 0; i2 < size; i2++) {
                    TL_pollAnswerVoters tL_pollAnswerVoters2 = (TL_pollAnswerVoters) tL_messageMediaPoll.results.results.get(i2);
                    if (Arrays.equals(tL_pollAnswerVoters2.option, bArr)) {
                        tL_pollAnswerVoters2.chosen = true;
                        break;
                    }
                }
            }
            TL_pollResults tL_pollResults3 = tL_messageMediaPoll.results;
            tL_pollResults3.flags |= 2;
        }
        if ((tL_pollResults.flags & 4) != 0) {
            TL_pollResults tL_pollResults4 = tL_messageMediaPoll.results;
            tL_pollResults4.total_voters = tL_pollResults.total_voters;
            tL_pollResults4.flags |= 4;
        }
    }

    public boolean isPollClosed() {
        if (this.type != 17) {
            return false;
        }
        return ((TL_messageMediaPoll) this.messageOwner.media).poll.closed;
    }

    public boolean isVoted() {
        if (this.type != 17) {
            return false;
        }
        TL_messageMediaPoll tL_messageMediaPoll = (TL_messageMediaPoll) this.messageOwner.media;
        TL_pollResults tL_pollResults = tL_messageMediaPoll.results;
        if (!(tL_pollResults == null || tL_pollResults.results.isEmpty())) {
            int size = tL_messageMediaPoll.results.results.size();
            for (int i = 0; i < size; i++) {
                if (((TL_pollAnswerVoters) tL_messageMediaPoll.results.results.get(i)).chosen) {
                    return true;
                }
            }
        }
        return false;
    }

    public long getPollId() {
        if (this.type != 17) {
            return 0;
        }
        return ((TL_messageMediaPoll) this.messageOwner.media).poll.f527id;
    }

    private Photo getPhotoWithId(WebPage webPage, long j) {
        if (!(webPage == null || webPage.cached_page == null)) {
            Photo photo = webPage.photo;
            if (photo != null && photo.f463id == j) {
                return photo;
            }
            for (int i = 0; i < webPage.cached_page.photos.size(); i++) {
                Photo photo2 = (Photo) webPage.cached_page.photos.get(i);
                if (photo2.f463id == j) {
                    return photo2;
                }
            }
        }
        return null;
    }

    private Document getDocumentWithId(WebPage webPage, long j) {
        if (!(webPage == null || webPage.cached_page == null)) {
            Document document = webPage.document;
            if (document != null && document.f441id == j) {
                return document;
            }
            for (int i = 0; i < webPage.cached_page.documents.size(); i++) {
                Document document2 = (Document) webPage.cached_page.documents.get(i);
                if (document2.f441id == j) {
                    return document2;
                }
            }
        }
        return null;
    }

    private MessageObject getMessageObjectForBlock(WebPage webPage, PageBlock pageBlock) {
        Message tL_message;
        if (pageBlock instanceof TL_pageBlockPhoto) {
            Photo photoWithId = getPhotoWithId(webPage, ((TL_pageBlockPhoto) pageBlock).photo_id);
            if (photoWithId == webPage.photo) {
                return this;
            }
            tL_message = new TL_message();
            tL_message.media = new TL_messageMediaPhoto();
            tL_message.media.photo = photoWithId;
        } else if (pageBlock instanceof TL_pageBlockVideo) {
            TL_pageBlockVideo tL_pageBlockVideo = (TL_pageBlockVideo) pageBlock;
            if (getDocumentWithId(webPage, tL_pageBlockVideo.video_id) == webPage.document) {
                return this;
            }
            Message tL_message2 = new TL_message();
            tL_message2.media = new TL_messageMediaDocument();
            tL_message2.media.document = getDocumentWithId(webPage, tL_pageBlockVideo.video_id);
            tL_message = tL_message2;
        } else {
            tL_message = null;
        }
        tL_message.message = "";
        tL_message.realId = getId();
        tL_message.f461id = Utilities.random.nextInt();
        Message message = this.messageOwner;
        tL_message.date = message.date;
        tL_message.to_id = message.to_id;
        tL_message.out = message.out;
        tL_message.from_id = message.from_id;
        return new MessageObject(this.currentAccount, tL_message, false);
    }

    public ArrayList<MessageObject> getWebPagePhotos(ArrayList<MessageObject> arrayList, ArrayList<PageBlock> arrayList2) {
        if (arrayList == null) {
            arrayList = new ArrayList();
        }
        MessageMedia messageMedia = this.messageOwner.media;
        if (messageMedia != null) {
            WebPage webPage = messageMedia.webpage;
            if (webPage != null) {
                Page page = webPage.cached_page;
                if (page == null) {
                    return arrayList;
                }
                ArrayList arrayList22;
                if (arrayList22 == null) {
                    arrayList22 = page.blocks;
                }
                for (int i = 0; i < arrayList22.size(); i++) {
                    PageBlock pageBlock = (PageBlock) arrayList22.get(i);
                    int i2;
                    if (pageBlock instanceof TL_pageBlockSlideshow) {
                        TL_pageBlockSlideshow tL_pageBlockSlideshow = (TL_pageBlockSlideshow) pageBlock;
                        for (i2 = 0; i2 < tL_pageBlockSlideshow.items.size(); i2++) {
                            arrayList.add(getMessageObjectForBlock(webPage, (PageBlock) tL_pageBlockSlideshow.items.get(i2)));
                        }
                    } else if (pageBlock instanceof TL_pageBlockCollage) {
                        TL_pageBlockCollage tL_pageBlockCollage = (TL_pageBlockCollage) pageBlock;
                        for (i2 = 0; i2 < tL_pageBlockCollage.items.size(); i2++) {
                            arrayList.add(getMessageObjectForBlock(webPage, (PageBlock) tL_pageBlockCollage.items.get(i2)));
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    public void createMessageSendInfo() {
        Message message = this.messageOwner;
        if (message.message == null) {
            return;
        }
        if (message.f461id < 0 || isEditing()) {
            HashMap hashMap = this.messageOwner.params;
            if (hashMap != null) {
                String str = (String) hashMap.get("ve");
                if (str != null && (isVideo() || isNewGif() || isRoundVideo())) {
                    this.videoEditedInfo = new VideoEditedInfo();
                    if (this.videoEditedInfo.parseString(str)) {
                        this.videoEditedInfo.roundVideo = isRoundVideo();
                    } else {
                        this.videoEditedInfo = null;
                    }
                }
                message = this.messageOwner;
                if (message.send_state == 3) {
                    str = (String) message.params.get("prevMedia");
                    if (str != null) {
                        SerializedData serializedData = new SerializedData(Base64.decode(str, 0));
                        this.previousMedia = MessageMedia.TLdeserialize(serializedData, serializedData.readInt32(false), false);
                        this.previousCaption = serializedData.readString(false);
                        this.previousAttachPath = serializedData.readString(false);
                        int readInt32 = serializedData.readInt32(false);
                        this.previousCaptionEntities = new ArrayList(readInt32);
                        for (int i = 0; i < readInt32; i++) {
                            this.previousCaptionEntities.add(MessageEntity.TLdeserialize(serializedData, serializedData.readInt32(false), false));
                        }
                        serializedData.cleanup();
                    }
                }
            }
        }
    }

    public void measureInlineBotButtons() {
        this.wantedBotKeyboardWidth = 0;
        if (this.messageOwner.reply_markup instanceof TL_replyInlineMarkup) {
            Theme.createChatResources(null, true);
            StringBuilder stringBuilder = this.botButtonsLayout;
            if (stringBuilder == null) {
                this.botButtonsLayout = new StringBuilder();
            } else {
                stringBuilder.setLength(0);
            }
            for (int i = 0; i < this.messageOwner.reply_markup.rows.size(); i++) {
                TL_keyboardButtonRow tL_keyboardButtonRow = (TL_keyboardButtonRow) this.messageOwner.reply_markup.rows.get(i);
                int size = tL_keyboardButtonRow.buttons.size();
                int i2 = 0;
                for (int i3 = 0; i3 < size; i3++) {
                    String replaceEmoji;
                    KeyboardButton keyboardButton = (KeyboardButton) tL_keyboardButtonRow.buttons.get(i3);
                    StringBuilder stringBuilder2 = this.botButtonsLayout;
                    stringBuilder2.append(i);
                    stringBuilder2.append(i3);
                    if (!(keyboardButton instanceof TL_keyboardButtonBuy) || (this.messageOwner.media.flags & 4) == 0) {
                        replaceEmoji = Emoji.replaceEmoji(keyboardButton.text, Theme.chat_msgBotButtonPaint.getFontMetricsInt(), AndroidUtilities.m26dp(15.0f), false);
                    } else {
                        replaceEmoji = LocaleController.getString("PaymentReceipt", C1067R.string.PaymentReceipt);
                    }
                    StaticLayout staticLayout = new StaticLayout(replaceEmoji, Theme.chat_msgBotButtonPaint, AndroidUtilities.m26dp(2000.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    if (staticLayout.getLineCount() > 0) {
                        float lineWidth = staticLayout.getLineWidth(0);
                        float lineLeft = staticLayout.getLineLeft(0);
                        if (lineLeft < lineWidth) {
                            lineWidth -= lineLeft;
                        }
                        i2 = Math.max(i2, ((int) Math.ceil((double) lineWidth)) + AndroidUtilities.m26dp(4.0f));
                    }
                }
                this.wantedBotKeyboardWidth = Math.max(this.wantedBotKeyboardWidth, ((i2 + AndroidUtilities.m26dp(12.0f)) * size) + (AndroidUtilities.m26dp(5.0f) * (size - 1)));
            }
        }
    }

    public boolean isFcmMessage() {
        return this.localType != 0;
    }

    public void setType() {
        int i = this.type;
        this.isRoundVideoCached = 0;
        Message message = this.messageOwner;
        if ((message instanceof TL_message) || (message instanceof TL_messageForwarded_old2)) {
            if (isMediaEmpty()) {
                this.type = 0;
                if (TextUtils.isEmpty(this.messageText) && this.eventId == 0) {
                    this.messageText = "Empty message";
                }
            } else {
                MessageMedia messageMedia = this.messageOwner.media;
                if (messageMedia.ttl_seconds == 0 || !((messageMedia.photo instanceof TL_photoEmpty) || (messageMedia.document instanceof TL_documentEmpty))) {
                    messageMedia = this.messageOwner.media;
                    if (messageMedia instanceof TL_messageMediaPhoto) {
                        this.type = 1;
                    } else if ((messageMedia instanceof TL_messageMediaGeo) || (messageMedia instanceof TL_messageMediaVenue) || (messageMedia instanceof TL_messageMediaGeoLive)) {
                        this.type = 4;
                    } else if (isRoundVideo()) {
                        this.type = 5;
                    } else if (isVideo()) {
                        this.type = 3;
                    } else if (isVoice()) {
                        this.type = 2;
                    } else if (isMusic()) {
                        this.type = 14;
                    } else {
                        messageMedia = this.messageOwner.media;
                        if (messageMedia instanceof TL_messageMediaContact) {
                            this.type = 12;
                        } else if (messageMedia instanceof TL_messageMediaPoll) {
                            this.type = 17;
                        } else if (messageMedia instanceof TL_messageMediaUnsupported) {
                            this.type = 0;
                        } else if (messageMedia instanceof TL_messageMediaDocument) {
                            Document document = messageMedia.document;
                            if (document == null || document.mime_type == null) {
                                this.type = 9;
                            } else if (isGifDocument(document)) {
                                this.type = 8;
                            } else if (isSticker()) {
                                this.type = 13;
                            } else if (isAnimatedSticker()) {
                                this.type = 15;
                            } else {
                                this.type = 9;
                            }
                        } else if (messageMedia instanceof TL_messageMediaGame) {
                            this.type = 0;
                        } else if (messageMedia instanceof TL_messageMediaInvoice) {
                            this.type = 0;
                        }
                    }
                } else {
                    this.contentType = 1;
                    this.type = 10;
                }
            }
        } else if (message instanceof TL_messageService) {
            MessageAction messageAction = message.action;
            if (messageAction instanceof TL_messageActionLoginUnknownLocation) {
                this.type = 0;
            } else if ((messageAction instanceof TL_messageActionChatEditPhoto) || (messageAction instanceof TL_messageActionUserUpdatedPhoto)) {
                this.contentType = 1;
                this.type = 11;
            } else if (messageAction instanceof TL_messageEncryptedAction) {
                DecryptedMessageAction decryptedMessageAction = messageAction.encryptedAction;
                if ((decryptedMessageAction instanceof TL_decryptedMessageActionScreenshotMessages) || (decryptedMessageAction instanceof TL_decryptedMessageActionSetMessageTTL)) {
                    this.contentType = 1;
                    this.type = 10;
                } else {
                    this.contentType = -1;
                    this.type = -1;
                }
            } else if (messageAction instanceof TL_messageActionHistoryClear) {
                this.contentType = -1;
                this.type = -1;
            } else if (messageAction instanceof TL_messageActionPhoneCall) {
                this.type = 16;
            } else {
                this.contentType = 1;
                this.type = 10;
            }
        }
        if (i != 1000 && i != this.type) {
            generateThumbs(false);
        }
    }

    public boolean checkLayout() {
        if (this.type == 0 && this.messageOwner.to_id != null) {
            CharSequence charSequence = this.messageText;
            if (!(charSequence == null || charSequence.length() == 0)) {
                if (this.layoutCreated) {
                    if (Math.abs(this.generatedWithMinSize - (AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() : AndroidUtilities.displaySize.x)) > AndroidUtilities.m26dp(52.0f) || this.generatedWithDensity != AndroidUtilities.density) {
                        this.layoutCreated = false;
                    }
                }
                if (!this.layoutCreated) {
                    TextPaint textPaint;
                    this.layoutCreated = true;
                    int[] iArr = null;
                    User user = isFromUser() ? MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.messageOwner.from_id)) : null;
                    if (this.messageOwner.media instanceof TL_messageMediaGame) {
                        textPaint = Theme.chat_msgGameTextPaint;
                    } else {
                        textPaint = Theme.chat_msgTextPaint;
                    }
                    if (SharedConfig.allowBigEmoji) {
                        iArr = new int[1];
                    }
                    this.messageText = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.m26dp(20.0f), false, iArr);
                    checkEmojiOnly(iArr);
                    generateLayout(user);
                    return true;
                }
            }
        }
        return false;
    }

    public void resetLayout() {
        this.layoutCreated = false;
    }

    public String getMimeType() {
        MessageMedia messageMedia = this.messageOwner.media;
        if (messageMedia instanceof TL_messageMediaDocument) {
            return messageMedia.document.mime_type;
        }
        if (messageMedia instanceof TL_messageMediaInvoice) {
            WebDocument webDocument = ((TL_messageMediaInvoice) messageMedia).photo;
            if (webDocument != null) {
                return webDocument.mime_type;
            }
        }
        String str = "image/jpeg";
        if (messageMedia instanceof TL_messageMediaPhoto) {
            return str;
        }
        if (messageMedia instanceof TL_messageMediaWebPage) {
            WebPage webPage = messageMedia.webpage;
            if (webPage.document != null) {
                return messageMedia.document.mime_type;
            }
            if (webPage.photo != null) {
                return str;
            }
        }
        return "";
    }

    public boolean canPreviewDocument() {
        return canPreviewDocument(getDocument());
    }

    public static boolean isGifDocument(WebFile webFile) {
        return webFile != null && (webFile.mime_type.equals("image/gif") || isNewGifDocument(webFile));
    }

    public static boolean isGifDocument(Document document) {
        if (document != null) {
            String str = document.mime_type;
            if (str != null && (str.equals("image/gif") || isNewGifDocument(document))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDocumentHasThumb(Document document) {
        if (!(document == null || document.thumbs.isEmpty())) {
            int size = document.thumbs.size();
            for (int i = 0; i < size; i++) {
                PhotoSize photoSize = (PhotoSize) document.thumbs.get(i);
                if (photoSize != null && !(photoSize instanceof TL_photoSizeEmpty) && !(photoSize.location instanceof TL_fileLocationUnavailable)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canPreviewDocument(Document document) {
        boolean z = false;
        if (document != null) {
            String str = document.mime_type;
            if (str != null) {
                str = str.toLowerCase();
                if (isDocumentHasThumb(document) && (str.equals("image/png") || str.equals("image/jpg") || str.equals("image/jpeg"))) {
                    for (int i = 0; i < document.attributes.size(); i++) {
                        DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(i);
                        if (documentAttribute instanceof TL_documentAttributeImageSize) {
                            TL_documentAttributeImageSize tL_documentAttributeImageSize = (TL_documentAttributeImageSize) documentAttribute;
                            if (tL_documentAttributeImageSize.f444w < 6000 && tL_documentAttributeImageSize.f443h < 6000) {
                                z = true;
                            }
                            return z;
                        }
                    }
                } else if (BuildVars.DEBUG_PRIVATE_VERSION) {
                    String documentFileName = FileLoader.getDocumentFileName(document);
                    if (documentFileName.startsWith("tg_secret_sticker") && documentFileName.endsWith("json")) {
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean isRoundVideoDocument(Document document) {
        if (document != null) {
            if (MimeTypes.VIDEO_MP4.equals(document.mime_type)) {
                boolean z = false;
                int i = 0;
                int i2 = 0;
                for (int i3 = 0; i3 < document.attributes.size(); i3++) {
                    DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(i3);
                    if (documentAttribute instanceof TL_documentAttributeVideo) {
                        i2 = documentAttribute.f444w;
                        z = documentAttribute.round_message;
                        i = i2;
                    }
                }
                if (z && i <= 1280 && i2 <= 1280) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNewGifDocument(WebFile webFile) {
        if (webFile != null) {
            if (MimeTypes.VIDEO_MP4.equals(webFile.mime_type)) {
                int i = 0;
                int i2 = 0;
                for (int i3 = 0; i3 < webFile.attributes.size(); i3++) {
                    DocumentAttribute documentAttribute = (DocumentAttribute) webFile.attributes.get(i3);
                    if (!(documentAttribute instanceof TL_documentAttributeAnimated) && (documentAttribute instanceof TL_documentAttributeVideo)) {
                        i = documentAttribute.f444w;
                        i2 = i;
                    }
                }
                if (i <= 1280 && i2 <= 1280) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNewGifDocument(Document document) {
        if (document != null) {
            if (MimeTypes.VIDEO_MP4.equals(document.mime_type)) {
                Object obj = null;
                int i = 0;
                int i2 = 0;
                for (int i3 = 0; i3 < document.attributes.size(); i3++) {
                    DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(i3);
                    if (documentAttribute instanceof TL_documentAttributeAnimated) {
                        obj = 1;
                    } else if (documentAttribute instanceof TL_documentAttributeVideo) {
                        i = documentAttribute.f444w;
                        i2 = i;
                    }
                }
                if (obj == null || i > 1280 || i2 > 1280) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public void generateThumbs(boolean z) {
        Message message = this.messageOwner;
        Photo photo;
        ArrayList arrayList;
        int i;
        PhotoSize photoSize;
        int i2;
        PhotoSize photoSize2;
        if (message instanceof TL_messageService) {
            MessageAction messageAction = message.action;
            if (messageAction instanceof TL_messageActionChatEditPhoto) {
                photo = messageAction.photo;
                if (z) {
                    arrayList = this.photoThumbs;
                    if (!(arrayList == null || arrayList.isEmpty())) {
                        for (i = 0; i < this.photoThumbs.size(); i++) {
                            photoSize = (PhotoSize) this.photoThumbs.get(i);
                            for (i2 = 0; i2 < photo.sizes.size(); i2++) {
                                photoSize2 = (PhotoSize) photo.sizes.get(i2);
                                if (!(photoSize2 instanceof TL_photoSizeEmpty) && photoSize2.type.equals(photoSize.type)) {
                                    photoSize.location = photoSize2.location;
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    this.photoThumbs = new ArrayList(photo.sizes);
                }
                if (photo.dc_id != 0) {
                    i = this.photoThumbs.size();
                    for (int i3 = 0; i3 < i; i3++) {
                        FileLocation fileLocation = ((PhotoSize) this.photoThumbs.get(i3)).location;
                        fileLocation.dc_id = photo.dc_id;
                        fileLocation.file_reference = photo.file_reference;
                    }
                }
                this.photoThumbsObject = this.messageOwner.action.photo;
                return;
            }
            return;
        }
        MessageMedia messageMedia = message.media;
        if (messageMedia != null && !(messageMedia instanceof TL_messageMediaEmpty)) {
            Document document;
            if (messageMedia instanceof TL_messageMediaPhoto) {
                photo = messageMedia.photo;
                if (z) {
                    arrayList = this.photoThumbs;
                    if (arrayList == null || arrayList.size() == photo.sizes.size()) {
                        arrayList = this.photoThumbs;
                        if (!(arrayList == null || arrayList.isEmpty())) {
                            for (i = 0; i < this.photoThumbs.size(); i++) {
                                photoSize = (PhotoSize) this.photoThumbs.get(i);
                                if (photoSize != null) {
                                    for (i2 = 0; i2 < photo.sizes.size(); i2++) {
                                        photoSize2 = (PhotoSize) photo.sizes.get(i2);
                                        if (photoSize2 != null && !(photoSize2 instanceof TL_photoSizeEmpty) && photoSize2.type.equals(photoSize.type)) {
                                            photoSize.location = photoSize2.location;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        this.photoThumbsObject = this.messageOwner.media.photo;
                    }
                }
                this.photoThumbs = new ArrayList(photo.sizes);
                this.photoThumbsObject = this.messageOwner.media.photo;
            } else if (messageMedia instanceof TL_messageMediaDocument) {
                document = messageMedia.document;
                if (isDocumentHasThumb(document)) {
                    if (z) {
                        arrayList = this.photoThumbs;
                        if (arrayList != null) {
                            if (!(arrayList == null || arrayList.isEmpty())) {
                                updatePhotoSizeLocations(this.photoThumbs, document.thumbs);
                            }
                            this.photoThumbsObject = document;
                        }
                    }
                    this.photoThumbs = new ArrayList();
                    this.photoThumbs.addAll(document.thumbs);
                    this.photoThumbsObject = document;
                }
            } else if (messageMedia instanceof TL_messageMediaGame) {
                document = messageMedia.game.document;
                if (document != null && isDocumentHasThumb(document)) {
                    if (z) {
                        ArrayList arrayList2 = this.photoThumbs;
                        if (!(arrayList2 == null || arrayList2.isEmpty())) {
                            updatePhotoSizeLocations(this.photoThumbs, document.thumbs);
                        }
                    } else {
                        this.photoThumbs = new ArrayList();
                        this.photoThumbs.addAll(document.thumbs);
                    }
                    this.photoThumbsObject = document;
                }
                photo = this.messageOwner.media.game.photo;
                if (photo != null) {
                    if (z) {
                        arrayList = this.photoThumbs2;
                        if (arrayList != null) {
                            if (!arrayList.isEmpty()) {
                                updatePhotoSizeLocations(this.photoThumbs2, photo.sizes);
                            }
                            this.photoThumbsObject2 = photo;
                        }
                    }
                    this.photoThumbs2 = new ArrayList(photo.sizes);
                    this.photoThumbsObject2 = photo;
                }
                if (this.photoThumbs == null) {
                    arrayList = this.photoThumbs2;
                    if (arrayList != null) {
                        this.photoThumbs = arrayList;
                        this.photoThumbs2 = null;
                        this.photoThumbsObject = this.photoThumbsObject2;
                        this.photoThumbsObject2 = null;
                    }
                }
            } else if (messageMedia instanceof TL_messageMediaWebPage) {
                WebPage webPage = messageMedia.webpage;
                Photo photo2 = webPage.photo;
                document = webPage.document;
                if (photo2 != null) {
                    if (z) {
                        arrayList = this.photoThumbs;
                        if (arrayList != null) {
                            if (!arrayList.isEmpty()) {
                                updatePhotoSizeLocations(this.photoThumbs, photo2.sizes);
                            }
                            this.photoThumbsObject = photo2;
                        }
                    }
                    this.photoThumbs = new ArrayList(photo2.sizes);
                    this.photoThumbsObject = photo2;
                } else if (document != null && isDocumentHasThumb(document)) {
                    if (z) {
                        arrayList = this.photoThumbs;
                        if (!(arrayList == null || arrayList.isEmpty())) {
                            updatePhotoSizeLocations(this.photoThumbs, document.thumbs);
                        }
                    } else {
                        this.photoThumbs = new ArrayList();
                        this.photoThumbs.addAll(document.thumbs);
                    }
                    this.photoThumbsObject = document;
                }
            }
        }
    }

    private static void updatePhotoSizeLocations(ArrayList<PhotoSize> arrayList, ArrayList<PhotoSize> arrayList2) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            PhotoSize photoSize = (PhotoSize) arrayList.get(i);
            int size2 = arrayList2.size();
            for (int i2 = 0; i2 < size2; i2++) {
                PhotoSize photoSize2 = (PhotoSize) arrayList2.get(i2);
                if (!(photoSize2 instanceof TL_photoSizeEmpty) && !(photoSize2 instanceof TL_photoCachedSize) && photoSize2.type.equals(photoSize.type)) {
                    photoSize.location = photoSize2.location;
                    break;
                }
            }
        }
    }

    public CharSequence replaceWithLink(CharSequence charSequence, String str, ArrayList<Integer> arrayList, AbstractMap<Integer, User> abstractMap, SparseArray<User> sparseArray) {
        if (TextUtils.indexOf(charSequence, str) < 0) {
            return charSequence;
        }
        String str2 = "";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str2);
        for (int i = 0; i < arrayList.size(); i++) {
            User user = null;
            if (abstractMap != null) {
                user = (User) abstractMap.get(arrayList.get(i));
            } else if (sparseArray != null) {
                user = (User) sparseArray.get(((Integer) arrayList.get(i)).intValue());
            }
            if (user == null) {
                user = MessagesController.getInstance(this.currentAccount).getUser((Integer) arrayList.get(i));
            }
            if (user != null) {
                String userName = UserObject.getUserName(user);
                int length = spannableStringBuilder.length();
                if (spannableStringBuilder.length() != 0) {
                    spannableStringBuilder.append(", ");
                }
                spannableStringBuilder.append(userName);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(user.f534id);
                spannableStringBuilder.setSpan(new URLSpanNoUnderlineBold(stringBuilder.toString()), length, userName.length() + length, 33);
            }
        }
        return TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{spannableStringBuilder});
    }

    public CharSequence replaceWithLink(CharSequence charSequence, String str, TLObject tLObject) {
        int indexOf = TextUtils.indexOf(charSequence, str);
        if (indexOf < 0) {
            return charSequence;
        }
        String userName;
        String stringBuilder;
        String str2 = "";
        StringBuilder stringBuilder2;
        if (tLObject instanceof User) {
            User user = (User) tLObject;
            userName = UserObject.getUserName(user);
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(user.f534id);
            stringBuilder = stringBuilder2.toString();
        } else if (tLObject instanceof Chat) {
            Chat chat = (Chat) tLObject;
            userName = chat.title;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(-chat.f434id);
            stringBuilder = stringBuilder2.toString();
        } else if (tLObject instanceof TL_game) {
            userName = ((TL_game) tLObject).title;
            stringBuilder = "game";
        } else {
            stringBuilder = "0";
            userName = str2;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(TextUtils.replace(charSequence, new String[]{str}, new String[]{userName.replace(10, ' ')}));
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(str2);
        stringBuilder3.append(stringBuilder);
        spannableStringBuilder.setSpan(new URLSpanNoUnderlineBold(stringBuilder3.toString()), indexOf, userName.length() + indexOf, 33);
        return spannableStringBuilder;
    }

    public String getExtension() {
        String fileName = getFileName();
        int lastIndexOf = fileName.lastIndexOf(46);
        fileName = lastIndexOf != -1 ? fileName.substring(lastIndexOf + 1) : null;
        if (fileName == null || fileName.length() == 0) {
            fileName = this.messageOwner.media.document.mime_type;
        }
        if (fileName == null) {
            fileName = "";
        }
        return fileName.toUpperCase();
    }

    public String getFileName() {
        MessageMedia messageMedia = this.messageOwner.media;
        if (messageMedia instanceof TL_messageMediaDocument) {
            return FileLoader.getAttachFileName(messageMedia.document);
        }
        if (messageMedia instanceof TL_messageMediaPhoto) {
            ArrayList arrayList = messageMedia.photo.sizes;
            if (arrayList.size() > 0) {
                PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize());
                if (closestPhotoSizeWithSize != null) {
                    return FileLoader.getAttachFileName(closestPhotoSizeWithSize);
                }
            }
        } else if (messageMedia instanceof TL_messageMediaWebPage) {
            return FileLoader.getAttachFileName(messageMedia.webpage.document);
        }
        return "";
    }

    public int getFileType() {
        if (isVideo()) {
            return 2;
        }
        if (isVoice()) {
            return 1;
        }
        MessageMedia messageMedia = this.messageOwner.media;
        if (messageMedia instanceof TL_messageMediaDocument) {
            return 3;
        }
        return messageMedia instanceof TL_messageMediaPhoto ? 0 : 4;
    }

    private static boolean containsUrls(CharSequence charSequence) {
        if (charSequence != null && charSequence.length() >= 2 && charSequence.length() <= 20480) {
            int length = charSequence.length();
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            char c = 0;
            while (i < length) {
                char charAt = charSequence.charAt(i);
                if (charAt >= '0' && charAt <= '9') {
                    i2++;
                    if (i2 >= 6) {
                        return true;
                    }
                    i3 = 0;
                    i4 = 0;
                } else if (charAt == ' ' || i2 <= 0) {
                    i2 = 0;
                }
                if ((charAt != '@' && charAt != '#' && charAt != '/' && charAt != '$') || i != 0) {
                    if (i != 0) {
                        int i5 = i - 1;
                        if (charSequence.charAt(i5) != ' ') {
                            if (charSequence.charAt(i5) == 10) {
                            }
                        }
                    }
                    if (charAt != ':') {
                        if (charAt != '/') {
                            if (charAt == '.') {
                                if (i4 == 0 && c != ' ') {
                                    i4++;
                                }
                            } else if (charAt != ' ' && c == '.' && i4 == 1) {
                                return true;
                            }
                            i4 = 0;
                        } else if (i3 == 2) {
                            return true;
                        } else {
                            if (i3 == 1) {
                                i3++;
                            }
                        }
                        i++;
                        c = charAt;
                    } else if (i3 == 0) {
                        i3 = 1;
                        i++;
                        c = charAt;
                    }
                    i3 = 0;
                    i++;
                    c = charAt;
                }
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0093  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0093  */
    public void generateLinkDescription() {
        /*
        r5 = this;
        r0 = r5.linkDescription;
        if (r0 == 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = r5.messageOwner;
        r0 = r0.media;
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        r2 = 1;
        r3 = 0;
        if (r1 == 0) goto L_0x004d;
    L_0x000f:
        r0 = r0.webpage;
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_webPage;
        if (r1 == 0) goto L_0x004d;
    L_0x0015:
        r0 = r0.description;
        if (r0 == 0) goto L_0x004d;
    L_0x0019:
        r0 = android.text.Spannable.Factory.getInstance();
        r1 = r5.messageOwner;
        r1 = r1.media;
        r1 = r1.webpage;
        r1 = r1.description;
        r0 = r0.newSpannable(r1);
        r5.linkDescription = r0;
        r0 = r5.messageOwner;
        r0 = r0.media;
        r0 = r0.webpage;
        r0 = r0.site_name;
        if (r0 == 0) goto L_0x0039;
    L_0x0035:
        r0 = r0.toLowerCase();
    L_0x0039:
        r1 = "instagram";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x0043;
    L_0x0041:
        r0 = 1;
        goto L_0x008b;
    L_0x0043:
        r1 = "twitter";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x008a;
    L_0x004b:
        r0 = 2;
        goto L_0x008b;
    L_0x004d:
        r0 = r5.messageOwner;
        r0 = r0.media;
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r1 == 0) goto L_0x006e;
    L_0x0055:
        r0 = r0.game;
        r0 = r0.description;
        if (r0 == 0) goto L_0x006e;
    L_0x005b:
        r0 = android.text.Spannable.Factory.getInstance();
        r1 = r5.messageOwner;
        r1 = r1.media;
        r1 = r1.game;
        r1 = r1.description;
        r0 = r0.newSpannable(r1);
        r5.linkDescription = r0;
        goto L_0x008a;
    L_0x006e:
        r0 = r5.messageOwner;
        r0 = r0.media;
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
        if (r1 == 0) goto L_0x008a;
    L_0x0076:
        r0 = r0.description;
        if (r0 == 0) goto L_0x008a;
    L_0x007a:
        r0 = android.text.Spannable.Factory.getInstance();
        r1 = r5.messageOwner;
        r1 = r1.media;
        r1 = r1.description;
        r0 = r0.newSpannable(r1);
        r5.linkDescription = r0;
    L_0x008a:
        r0 = 0;
    L_0x008b:
        r1 = r5.linkDescription;
        r1 = android.text.TextUtils.isEmpty(r1);
        if (r1 != 0) goto L_0x00d3;
    L_0x0093:
        r1 = r5.linkDescription;
        r1 = containsUrls(r1);
        if (r1 == 0) goto L_0x00a7;
    L_0x009b:
        r1 = r5.linkDescription;	 Catch:{ Exception -> 0x00a3 }
        r1 = (android.text.Spannable) r1;	 Catch:{ Exception -> 0x00a3 }
        android.text.util.Linkify.addLinks(r1, r2);	 Catch:{ Exception -> 0x00a3 }
        goto L_0x00a7;
    L_0x00a3:
        r1 = move-exception;
        org.telegram.messenger.FileLog.m30e(r1);
    L_0x00a7:
        r1 = r5.linkDescription;
        r2 = org.telegram.p004ui.ActionBar.Theme.chat_msgTextPaint;
        r2 = r2.getFontMetricsInt();
        r4 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r1 = org.telegram.messenger.Emoji.replaceEmoji(r1, r2, r4, r3);
        r5.linkDescription = r1;
        if (r0 == 0) goto L_0x00d3;
    L_0x00bd:
        r1 = r5.linkDescription;
        r2 = r1 instanceof android.text.Spannable;
        if (r2 != 0) goto L_0x00ca;
    L_0x00c3:
        r2 = new android.text.SpannableStringBuilder;
        r2.<init>(r1);
        r5.linkDescription = r2;
    L_0x00ca:
        r1 = r5.isOutOwner();
        r2 = r5.linkDescription;
        addUsernamesAndHashtags(r1, r2, r3, r0);
    L_0x00d3:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.generateLinkDescription():void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:52:0x00c1 A:{SYNTHETIC, Splitter:B:52:0x00c1} */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00a2  */
    /* JADX WARNING: Missing block: B:39:0x0095, code skipped:
            if (r7.messageOwner.send_state == 0) goto L_0x0097;
     */
    /* JADX WARNING: Missing block: B:41:0x009b, code skipped:
            if (r7.messageOwner.f461id >= 0) goto L_0x009f;
     */
    public void generateCaption() {
        /*
        r7 = this;
        r0 = r7.caption;
        if (r0 != 0) goto L_0x00d3;
    L_0x0004:
        r0 = r7.isRoundVideo();
        if (r0 == 0) goto L_0x000c;
    L_0x000a:
        goto L_0x00d3;
    L_0x000c:
        r0 = r7.isMediaEmpty();
        if (r0 != 0) goto L_0x00d3;
    L_0x0012:
        r0 = r7.messageOwner;
        r1 = r0.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r1 != 0) goto L_0x00d3;
    L_0x001a:
        r0 = r0.message;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x00d3;
    L_0x0022:
        r0 = r7.messageOwner;
        r0 = r0.message;
        r1 = org.telegram.p004ui.ActionBar.Theme.chat_msgTextPaint;
        r1 = r1.getFontMetricsInt();
        r2 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = 0;
        r0 = org.telegram.messenger.Emoji.replaceEmoji(r0, r1, r2, r3);
        r7.caption = r0;
        r0 = r7.messageOwner;
        r1 = r0.send_state;
        r2 = 1;
        if (r1 == 0) goto L_0x005e;
    L_0x0040:
        r0 = 0;
    L_0x0041:
        r1 = r7.messageOwner;
        r1 = r1.entities;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x005c;
    L_0x004b:
        r1 = r7.messageOwner;
        r1 = r1.entities;
        r1 = r1.get(r0);
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_inputMessageEntityMentionName;
        if (r1 != 0) goto L_0x0059;
    L_0x0057:
        r0 = 1;
        goto L_0x0065;
    L_0x0059:
        r0 = r0 + 1;
        goto L_0x0041;
    L_0x005c:
        r0 = 0;
        goto L_0x0065;
    L_0x005e:
        r0 = r0.entities;
        r0 = r0.isEmpty();
        r0 = r0 ^ r2;
    L_0x0065:
        if (r0 != 0) goto L_0x009f;
    L_0x0067:
        r0 = r7.eventId;
        r4 = 0;
        r6 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r6 != 0) goto L_0x009d;
    L_0x006f:
        r0 = r7.messageOwner;
        r0 = r0.media;
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto_old;
        if (r1 != 0) goto L_0x009d;
    L_0x0077:
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto_layer68;
        if (r1 != 0) goto L_0x009d;
    L_0x007b:
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto_layer74;
        if (r1 != 0) goto L_0x009d;
    L_0x007f:
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument_old;
        if (r1 != 0) goto L_0x009d;
    L_0x0083:
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument_layer68;
        if (r1 != 0) goto L_0x009d;
    L_0x0087:
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument_layer74;
        if (r0 != 0) goto L_0x009d;
    L_0x008b:
        r0 = r7.isOut();
        if (r0 == 0) goto L_0x0097;
    L_0x0091:
        r0 = r7.messageOwner;
        r0 = r0.send_state;
        if (r0 != 0) goto L_0x009d;
    L_0x0097:
        r0 = r7.messageOwner;
        r0 = r0.f461id;
        if (r0 >= 0) goto L_0x009f;
    L_0x009d:
        r0 = 1;
        goto L_0x00a0;
    L_0x009f:
        r0 = 0;
    L_0x00a0:
        if (r0 == 0) goto L_0x00c1;
    L_0x00a2:
        r1 = r7.caption;
        r1 = containsUrls(r1);
        if (r1 == 0) goto L_0x00b7;
    L_0x00aa:
        r1 = r7.caption;	 Catch:{ Exception -> 0x00b3 }
        r1 = (android.text.Spannable) r1;	 Catch:{ Exception -> 0x00b3 }
        r4 = 5;
        android.text.util.Linkify.addLinks(r1, r4);	 Catch:{ Exception -> 0x00b3 }
        goto L_0x00b7;
    L_0x00b3:
        r1 = move-exception;
        org.telegram.messenger.FileLog.m30e(r1);
    L_0x00b7:
        r1 = r7.isOutOwner();
        r4 = r7.caption;
        addUsernamesAndHashtags(r1, r4, r2, r3);
        goto L_0x00ce;
    L_0x00c1:
        r1 = r7.caption;	 Catch:{ Throwable -> 0x00ca }
        r1 = (android.text.Spannable) r1;	 Catch:{ Throwable -> 0x00ca }
        r2 = 4;
        android.text.util.Linkify.addLinks(r1, r2);	 Catch:{ Throwable -> 0x00ca }
        goto L_0x00ce;
    L_0x00ca:
        r1 = move-exception;
        org.telegram.messenger.FileLog.m30e(r1);
    L_0x00ce:
        r1 = r7.caption;
        r7.addEntitiesToText(r1, r0);
    L_0x00d3:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.generateCaption():void");
    }

    private static void addUsernamesAndHashtags(boolean z, CharSequence charSequence, boolean z2, int i) {
        Matcher matcher;
        if (i == 1) {
            try {
                if (instagramUrlPattern == null) {
                    instagramUrlPattern = Pattern.compile("(^|\\s|\\()@[a-zA-Z\\d_.]{1,32}|(^|\\s|\\()#[\\w.]+");
                }
                matcher = instagramUrlPattern.matcher(charSequence);
            } catch (Exception e) {
                FileLog.m30e(e);
                return;
            }
        }
        if (urlPattern == null) {
            urlPattern = Pattern.compile("(^|\\s)/[a-zA-Z@\\d_]{1,255}|(^|\\s|\\()@[a-zA-Z\\d_]{1,32}|(^|\\s|\\()#[\\w.]+|(^|\\s)\\$[A-Z]{3,8}([ ,.]|$)");
        }
        matcher = urlPattern.matcher(charSequence);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            char charAt = charSequence.charAt(start);
            if (i != 0) {
                if (!(charAt == '@' || charAt == '#')) {
                    start++;
                }
                charAt = charSequence.charAt(start);
                if (!(charAt == '@' || charAt == '#')) {
                }
            } else if (!(charAt == '@' || charAt == '#' || charAt == '/' || charAt == '$')) {
                start++;
            }
            Object obj = null;
            StringBuilder stringBuilder;
            if (i == 1) {
                if (charAt == '@') {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("https://instagram.com/");
                    stringBuilder.append(charSequence.subSequence(start + 1, end).toString());
                    obj = new URLSpanNoUnderline(stringBuilder.toString());
                } else if (charAt == '#') {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("https://www.instagram.com/explore/tags/");
                    stringBuilder.append(charSequence.subSequence(start + 1, end).toString());
                    obj = new URLSpanNoUnderline(stringBuilder.toString());
                }
            } else if (i == 2) {
                if (charAt == '@') {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("https://twitter.com/");
                    stringBuilder.append(charSequence.subSequence(start + 1, end).toString());
                    obj = new URLSpanNoUnderline(stringBuilder.toString());
                } else if (charAt == '#') {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("https://twitter.com/hashtag/");
                    stringBuilder.append(charSequence.subSequence(start + 1, end).toString());
                    obj = new URLSpanNoUnderline(stringBuilder.toString());
                }
            } else if (charSequence.charAt(start) != '/') {
                obj = new URLSpanNoUnderline(charSequence.subSequence(start, end).toString());
            } else if (z2) {
                obj = new URLSpanBotCommand(charSequence.subSequence(start, end).toString(), z ? 1 : 0);
            }
            if (obj != null) {
                ((Spannable) charSequence).setSpan(obj, start, end, 0);
            }
        }
    }

    public static int[] getWebDocumentWidthAndHeight(WebDocument webDocument) {
        if (webDocument == null) {
            return null;
        }
        int size = webDocument.attributes.size();
        int i = 0;
        while (i < size) {
            DocumentAttribute documentAttribute = (DocumentAttribute) webDocument.attributes.get(i);
            if (documentAttribute instanceof TL_documentAttributeImageSize) {
                return new int[]{documentAttribute.f444w, documentAttribute.f443h};
            } else if (documentAttribute instanceof TL_documentAttributeVideo) {
                return new int[]{documentAttribute.f444w, documentAttribute.f443h};
            } else {
                i++;
            }
        }
        return null;
    }

    public static int getWebDocumentDuration(WebDocument webDocument) {
        if (webDocument == null) {
            return 0;
        }
        int size = webDocument.attributes.size();
        for (int i = 0; i < size; i++) {
            DocumentAttribute documentAttribute = (DocumentAttribute) webDocument.attributes.get(i);
            if (documentAttribute instanceof TL_documentAttributeVideo) {
                return documentAttribute.duration;
            }
            if (documentAttribute instanceof TL_documentAttributeAudio) {
                return documentAttribute.duration;
            }
        }
        return 0;
    }

    public static int[] getInlineResultWidthAndHeight(BotInlineResult botInlineResult) {
        int[] webDocumentWidthAndHeight = getWebDocumentWidthAndHeight(botInlineResult.content);
        if (webDocumentWidthAndHeight != null) {
            return webDocumentWidthAndHeight;
        }
        webDocumentWidthAndHeight = getWebDocumentWidthAndHeight(botInlineResult.thumb);
        return webDocumentWidthAndHeight == null ? new int[]{0, 0} : webDocumentWidthAndHeight;
    }

    public static int getInlineResultDuration(BotInlineResult botInlineResult) {
        int webDocumentDuration = getWebDocumentDuration(botInlineResult.content);
        return webDocumentDuration == 0 ? getWebDocumentDuration(botInlineResult.thumb) : webDocumentDuration;
    }

    public boolean hasValidGroupId() {
        if (getGroupId() != 0) {
            ArrayList arrayList = this.photoThumbs;
            if (!(arrayList == null || arrayList.isEmpty())) {
                return true;
            }
        }
        return false;
    }

    public long getGroupIdForUse() {
        long j = this.localSentGroupId;
        return j != 0 ? j : this.messageOwner.grouped_id;
    }

    public long getGroupId() {
        long j = this.localGroupId;
        return j != 0 ? j : getGroupIdForUse();
    }

    public static void addLinks(boolean z, CharSequence charSequence) {
        addLinks(z, charSequence, true);
    }

    public static void addLinks(boolean z, CharSequence charSequence, boolean z2) {
        if ((charSequence instanceof Spannable) && containsUrls(charSequence)) {
            if (charSequence.length() < 1000) {
                try {
                    Linkify.addLinks((Spannable) charSequence, 5);
                } catch (Exception e) {
                    FileLog.m30e(e);
                }
            } else {
                try {
                    Linkify.addLinks((Spannable) charSequence, 1);
                } catch (Exception e2) {
                    FileLog.m30e(e2);
                }
            }
            addUsernamesAndHashtags(z, charSequence, z2, 0);
        }
    }

    public void resetPlayingProgress() {
        this.audioProgress = 0.0f;
        this.audioProgressSec = 0;
        this.bufferedProgress = 0.0f;
    }

    private boolean addEntitiesToText(CharSequence charSequence, boolean z) {
        return addEntitiesToText(charSequence, this.messageOwner.entities, isOutOwner(), this.type, true, false, z);
    }

    public boolean addEntitiesToText(CharSequence charSequence, boolean z, boolean z2) {
        return addEntitiesToText(charSequence, this.messageOwner.entities, isOutOwner(), this.type, true, z, z2);
    }

    public static boolean addEntitiesToText(CharSequence charSequence, ArrayList<MessageEntity> arrayList, boolean z, int i, boolean z2, boolean z3, boolean z4) {
        CharSequence charSequence2 = charSequence;
        if (!(charSequence2 instanceof Spannable)) {
            return false;
        }
        Spannable spannable = (Spannable) charSequence2;
        int size = arrayList.size();
        URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, charSequence.length(), URLSpan.class);
        boolean z5 = uRLSpanArr != null && uRLSpanArr.length > 0;
        int i2 = z3 ? 2 : z ? 1 : 0;
        boolean z6 = z5;
        for (int i3 = 0; i3 < size; i3++) {
            MessageEntity messageEntity = (MessageEntity) arrayList.get(i3);
            if (messageEntity.length > 0) {
                int i4 = messageEntity.offset;
                if (i4 >= 0 && i4 < charSequence.length()) {
                    int spanEnd;
                    if (messageEntity.offset + messageEntity.length > charSequence.length()) {
                        messageEntity.length = charSequence.length() - messageEntity.offset;
                    }
                    if ((!z4 || (messageEntity instanceof TL_messageEntityBold) || (messageEntity instanceof TL_messageEntityItalic) || (messageEntity instanceof TL_messageEntityCode) || (messageEntity instanceof TL_messageEntityPre) || (messageEntity instanceof TL_messageEntityMentionName) || (messageEntity instanceof TL_inputMessageEntityMentionName)) && uRLSpanArr != null && uRLSpanArr.length > 0) {
                        for (i4 = 0; i4 < uRLSpanArr.length; i4++) {
                            if (uRLSpanArr[i4] != null) {
                                int spanStart = spannable.getSpanStart(uRLSpanArr[i4]);
                                spanEnd = spannable.getSpanEnd(uRLSpanArr[i4]);
                                int i5 = messageEntity.offset;
                                if (i5 > spanStart || i5 + messageEntity.length < spanStart) {
                                    spanStart = messageEntity.offset;
                                    if (spanStart <= spanEnd) {
                                        if (spanStart + messageEntity.length < spanEnd) {
                                        }
                                    }
                                }
                                spannable.removeSpan(uRLSpanArr[i4]);
                                uRLSpanArr[i4] = null;
                            }
                        }
                    }
                    TypefaceSpan typefaceSpan;
                    if (messageEntity instanceof TL_messageEntityBold) {
                        typefaceSpan = new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                        spanEnd = messageEntity.offset;
                        spannable.setSpan(typefaceSpan, spanEnd, messageEntity.length + spanEnd, 33);
                    } else if (messageEntity instanceof TL_messageEntityItalic) {
                        typefaceSpan = new TypefaceSpan(AndroidUtilities.getTypeface("fonts/ritalic.ttf"));
                        spanEnd = messageEntity.offset;
                        spannable.setSpan(typefaceSpan, spanEnd, messageEntity.length + spanEnd, 33);
                    } else if ((messageEntity instanceof TL_messageEntityCode) || (messageEntity instanceof TL_messageEntityPre)) {
                        spanEnd = messageEntity.offset;
                        URLSpanMono uRLSpanMono = new URLSpanMono(spannable, spanEnd, messageEntity.length + spanEnd, i2);
                        spanEnd = messageEntity.offset;
                        spannable.setSpan(uRLSpanMono, spanEnd, messageEntity.length + spanEnd, 33);
                    } else {
                        String str = "";
                        StringBuilder stringBuilder;
                        URLSpanUserMention uRLSpanUserMention;
                        if (messageEntity instanceof TL_messageEntityMentionName) {
                            if (z2) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(str);
                                stringBuilder.append(((TL_messageEntityMentionName) messageEntity).user_id);
                                uRLSpanUserMention = new URLSpanUserMention(stringBuilder.toString(), i2);
                                spanEnd = messageEntity.offset;
                                spannable.setSpan(uRLSpanUserMention, spanEnd, messageEntity.length + spanEnd, 33);
                            }
                        } else if (messageEntity instanceof TL_inputMessageEntityMentionName) {
                            if (z2) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(str);
                                stringBuilder.append(((TL_inputMessageEntityMentionName) messageEntity).user_id.user_id);
                                uRLSpanUserMention = new URLSpanUserMention(stringBuilder.toString(), i2);
                                spanEnd = messageEntity.offset;
                                spannable.setSpan(uRLSpanUserMention, spanEnd, messageEntity.length + spanEnd, 33);
                            }
                        } else if (!z4) {
                            i4 = messageEntity.offset;
                            String substring = TextUtils.substring(charSequence2, i4, messageEntity.length + i4);
                            if (messageEntity instanceof TL_messageEntityBotCommand) {
                                URLSpanBotCommand uRLSpanBotCommand = new URLSpanBotCommand(substring, i2);
                                i4 = messageEntity.offset;
                                spannable.setSpan(uRLSpanBotCommand, i4, messageEntity.length + i4, 33);
                            } else if ((messageEntity instanceof TL_messageEntityHashtag) || ((z2 && (messageEntity instanceof TL_messageEntityMention)) || (messageEntity instanceof TL_messageEntityCashtag))) {
                                URLSpanNoUnderline uRLSpanNoUnderline = new URLSpanNoUnderline(substring);
                                i4 = messageEntity.offset;
                                spannable.setSpan(uRLSpanNoUnderline, i4, messageEntity.length + i4, 33);
                            } else if (messageEntity instanceof TL_messageEntityEmail) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("mailto:");
                                stringBuilder.append(substring);
                                URLSpanReplacement uRLSpanReplacement = new URLSpanReplacement(stringBuilder.toString());
                                i4 = messageEntity.offset;
                                spannable.setSpan(uRLSpanReplacement, i4, messageEntity.length + i4, 33);
                            } else {
                                StringBuilder stringBuilder2;
                                if (messageEntity instanceof TL_messageEntityUrl) {
                                    if (!Browser.isPassportUrl(messageEntity.url)) {
                                        URLSpanBrowser uRLSpanBrowser;
                                        if (substring.toLowerCase().startsWith("http") || substring.toLowerCase().startsWith("tg://")) {
                                            uRLSpanBrowser = new URLSpanBrowser(substring);
                                            i4 = messageEntity.offset;
                                            spannable.setSpan(uRLSpanBrowser, i4, messageEntity.length + i4, 33);
                                        } else {
                                            stringBuilder2 = new StringBuilder();
                                            stringBuilder2.append("http://");
                                            stringBuilder2.append(substring);
                                            uRLSpanBrowser = new URLSpanBrowser(stringBuilder2.toString());
                                            i4 = messageEntity.offset;
                                            spannable.setSpan(uRLSpanBrowser, i4, messageEntity.length + i4, 33);
                                        }
                                    }
                                } else if (messageEntity instanceof TL_messageEntityPhone) {
                                    String stripExceptNumbers = C0278PhoneFormat.stripExceptNumbers(substring);
                                    str = "+";
                                    if (substring.startsWith(str)) {
                                        StringBuilder stringBuilder3 = new StringBuilder();
                                        stringBuilder3.append(str);
                                        stringBuilder3.append(stripExceptNumbers);
                                        stripExceptNumbers = stringBuilder3.toString();
                                    }
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("tel:");
                                    stringBuilder2.append(stripExceptNumbers);
                                    URLSpanBrowser uRLSpanBrowser2 = new URLSpanBrowser(stringBuilder2.toString());
                                    int i6 = messageEntity.offset;
                                    spannable.setSpan(uRLSpanBrowser2, i6, messageEntity.length + i6, 33);
                                } else if ((messageEntity instanceof TL_messageEntityTextUrl) && !Browser.isPassportUrl(messageEntity.url)) {
                                    URLSpanReplacement uRLSpanReplacement2 = new URLSpanReplacement(messageEntity.url);
                                    spanEnd = messageEntity.offset;
                                    spannable.setSpan(uRLSpanReplacement2, spanEnd, messageEntity.length + spanEnd, 33);
                                }
                                z6 = true;
                            }
                        }
                    }
                }
            }
        }
        return z6;
    }

    /* JADX WARNING: Missing block: B:54:0x00c7, code skipped:
            if ((r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo) == false) goto L_0x00cb;
     */
    public boolean needDrawShareButton() {
        /*
        r7 = this;
        r0 = r7.eventId;
        r2 = 0;
        r3 = 0;
        r5 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        if (r5 == 0) goto L_0x000a;
    L_0x0009:
        return r2;
    L_0x000a:
        r0 = r7.messageOwner;
        r0 = r0.fwd_from;
        r1 = 1;
        if (r0 == 0) goto L_0x0033;
    L_0x0011:
        r0 = r7.isOutOwner();
        if (r0 != 0) goto L_0x0033;
    L_0x0017:
        r0 = r7.messageOwner;
        r0 = r0.fwd_from;
        r0 = r0.saved_from_peer;
        if (r0 == 0) goto L_0x0033;
    L_0x001f:
        r3 = r7.getDialogId();
        r0 = r7.currentAccount;
        r0 = org.telegram.messenger.UserConfig.getInstance(r0);
        r0 = r0.getClientUserId();
        r5 = (long) r0;
        r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r0 != 0) goto L_0x0033;
    L_0x0032:
        return r1;
    L_0x0033:
        r0 = r7.type;
        r3 = 13;
        if (r0 == r3) goto L_0x00ee;
    L_0x0039:
        r4 = 15;
        if (r0 != r4) goto L_0x003f;
    L_0x003d:
        goto L_0x00ee;
    L_0x003f:
        r0 = r7.messageOwner;
        r0 = r0.fwd_from;
        if (r0 == 0) goto L_0x0050;
    L_0x0045:
        r0 = r0.channel_id;
        if (r0 == 0) goto L_0x0050;
    L_0x0049:
        r0 = r7.isOutOwner();
        if (r0 != 0) goto L_0x0050;
    L_0x004f:
        return r1;
    L_0x0050:
        r0 = r7.isFromUser();
        if (r0 == 0) goto L_0x00cd;
    L_0x0056:
        r0 = r7.messageOwner;
        r0 = r0.media;
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
        if (r3 != 0) goto L_0x00cc;
    L_0x005e:
        if (r0 == 0) goto L_0x00cc;
    L_0x0060:
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r3 == 0) goto L_0x006b;
    L_0x0064:
        r0 = r0.webpage;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_webPage;
        if (r0 != 0) goto L_0x006b;
    L_0x006a:
        goto L_0x00cc;
    L_0x006b:
        r0 = r7.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r3 = r7.messageOwner;
        r3 = r3.from_id;
        r3 = java.lang.Integer.valueOf(r3);
        r0 = r0.getUser(r3);
        if (r0 == 0) goto L_0x0084;
    L_0x007f:
        r0 = r0.bot;
        if (r0 == 0) goto L_0x0084;
    L_0x0083:
        return r1;
    L_0x0084:
        r0 = r7.isOut();
        if (r0 != 0) goto L_0x00ee;
    L_0x008a:
        r0 = r7.messageOwner;
        r0 = r0.media;
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r3 != 0) goto L_0x00cb;
    L_0x0092:
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
        if (r0 == 0) goto L_0x0097;
    L_0x0096:
        goto L_0x00cb;
    L_0x0097:
        r0 = r7.isMegagroup();
        if (r0 == 0) goto L_0x00ee;
    L_0x009d:
        r0 = r7.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r3 = r7.messageOwner;
        r3 = r3.to_id;
        r3 = r3.channel_id;
        r3 = java.lang.Integer.valueOf(r3);
        r0 = r0.getChat(r3);
        if (r0 == 0) goto L_0x00ca;
    L_0x00b3:
        r0 = r0.username;
        if (r0 == 0) goto L_0x00ca;
    L_0x00b7:
        r0 = r0.length();
        if (r0 <= 0) goto L_0x00ca;
    L_0x00bd:
        r0 = r7.messageOwner;
        r0 = r0.media;
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact;
        if (r3 != 0) goto L_0x00ca;
    L_0x00c5:
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
        if (r0 != 0) goto L_0x00ca;
    L_0x00c9:
        goto L_0x00cb;
    L_0x00ca:
        r1 = 0;
    L_0x00cb:
        return r1;
    L_0x00cc:
        return r2;
    L_0x00cd:
        r0 = r7.messageOwner;
        r5 = r0.from_id;
        if (r5 < 0) goto L_0x00d7;
    L_0x00d3:
        r0 = r0.post;
        if (r0 == 0) goto L_0x00ee;
    L_0x00d7:
        r0 = r7.messageOwner;
        r5 = r0.to_id;
        r5 = r5.channel_id;
        if (r5 == 0) goto L_0x00ee;
    L_0x00df:
        r5 = r0.via_bot_id;
        if (r5 != 0) goto L_0x00e7;
    L_0x00e3:
        r0 = r0.reply_to_msg_id;
        if (r0 == 0) goto L_0x00ed;
    L_0x00e7:
        r0 = r7.type;
        if (r0 == r3) goto L_0x00ee;
    L_0x00eb:
        if (r0 == r4) goto L_0x00ee;
    L_0x00ed:
        return r1;
    L_0x00ee:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.needDrawShareButton():boolean");
    }

    public int getMaxMessageTextWidth() {
        int dp;
        if (!AndroidUtilities.isTablet() || this.eventId == 0) {
            this.generatedWithMinSize = AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() : AndroidUtilities.displaySize.x;
        } else {
            this.generatedWithMinSize = AndroidUtilities.m26dp(530.0f);
        }
        this.generatedWithDensity = AndroidUtilities.density;
        MessageMedia messageMedia = this.messageOwner.media;
        int i = 0;
        if (messageMedia instanceof TL_messageMediaWebPage) {
            WebPage webPage = messageMedia.webpage;
            if (webPage != null) {
                if ("telegram_background".equals(webPage.type)) {
                    try {
                        Uri parse = Uri.parse(this.messageOwner.media.webpage.url);
                        if (parse.getQueryParameter("bg_color") != null) {
                            dp = AndroidUtilities.m26dp(220.0f);
                        } else if (parse.getLastPathSegment().length() == 6) {
                            dp = AndroidUtilities.m26dp(200.0f);
                        }
                        i = dp;
                    } catch (Exception unused) {
                    }
                }
            }
        }
        if (i != 0) {
            return i;
        }
        dp = this.generatedWithMinSize;
        float f = (!needDrawAvatarInternal() || isOutOwner()) ? 80.0f : 132.0f;
        dp -= AndroidUtilities.m26dp(f);
        if (needDrawShareButton() && !isOutOwner()) {
            dp -= AndroidUtilities.m26dp(10.0f);
        }
        return this.messageOwner.media instanceof TL_messageMediaGame ? dp - AndroidUtilities.m26dp(10.0f) : dp;
    }

    /* JADX WARNING: Removed duplicated region for block: B:106:0x0253 A:{Catch:{ Exception -> 0x03f0 }} */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x0278  */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x02b3  */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x02a9  */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x02d7  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x02dc  */
    /* JADX WARNING: Removed duplicated region for block: B:178:0x03a0  */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x02e9  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00a4  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00d4  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00fc A:{Catch:{ Exception -> 0x042b }} */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00dd A:{Catch:{ Exception -> 0x042b }} */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x011f  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0133  */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x02d7  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x02dc  */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x02e9  */
    /* JADX WARNING: Removed duplicated region for block: B:178:0x03a0  */
    /* JADX WARNING: Missing block: B:43:0x0093, code skipped:
            if ((r0.media instanceof org.telegram.tgnet.TLRPC.TL_messageMediaUnsupported) == false) goto L_0x0097;
     */
    public void generateLayout(org.telegram.tgnet.TLRPC.User r31) {
        /*
        r30 = this;
        r1 = r30;
        r0 = r1.type;
        if (r0 != 0) goto L_0x042f;
    L_0x0006:
        r0 = r1.messageOwner;
        r0 = r0.to_id;
        if (r0 == 0) goto L_0x042f;
    L_0x000c:
        r0 = r1.messageText;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 == 0) goto L_0x0016;
    L_0x0014:
        goto L_0x042f;
    L_0x0016:
        r30.generateLinkDescription();
        r0 = new java.util.ArrayList;
        r0.<init>();
        r1.textLayoutBlocks = r0;
        r2 = 0;
        r1.textWidth = r2;
        r0 = r1.messageOwner;
        r3 = r0.send_state;
        r4 = 1;
        if (r3 == 0) goto L_0x0048;
    L_0x002a:
        r0 = 0;
    L_0x002b:
        r3 = r1.messageOwner;
        r3 = r3.entities;
        r3 = r3.size();
        if (r0 >= r3) goto L_0x0046;
    L_0x0035:
        r3 = r1.messageOwner;
        r3 = r3.entities;
        r3 = r3.get(r0);
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_inputMessageEntityMentionName;
        if (r3 != 0) goto L_0x0043;
    L_0x0041:
        r0 = 1;
        goto L_0x004f;
    L_0x0043:
        r0 = r0 + 1;
        goto L_0x002b;
    L_0x0046:
        r0 = 0;
        goto L_0x004f;
    L_0x0048:
        r0 = r0.entities;
        r0 = r0.isEmpty();
        r0 = r0 ^ r4;
    L_0x004f:
        if (r0 != 0) goto L_0x0097;
    L_0x0051:
        r5 = r1.eventId;
        r7 = 0;
        r0 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r0 != 0) goto L_0x0095;
    L_0x0059:
        r0 = r1.messageOwner;
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_message_old;
        if (r3 != 0) goto L_0x0095;
    L_0x005f:
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_message_old2;
        if (r3 != 0) goto L_0x0095;
    L_0x0063:
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_message_old3;
        if (r3 != 0) goto L_0x0095;
    L_0x0067:
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_message_old4;
        if (r3 != 0) goto L_0x0095;
    L_0x006b:
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageForwarded_old;
        if (r3 != 0) goto L_0x0095;
    L_0x006f:
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageForwarded_old2;
        if (r3 != 0) goto L_0x0095;
    L_0x0073:
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_message_secret;
        if (r3 != 0) goto L_0x0095;
    L_0x0077:
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
        if (r0 != 0) goto L_0x0095;
    L_0x007d:
        r0 = r30.isOut();
        if (r0 == 0) goto L_0x0089;
    L_0x0083:
        r0 = r1.messageOwner;
        r0 = r0.send_state;
        if (r0 != 0) goto L_0x0095;
    L_0x0089:
        r0 = r1.messageOwner;
        r3 = r0.f461id;
        if (r3 < 0) goto L_0x0095;
    L_0x008f:
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaUnsupported;
        if (r0 == 0) goto L_0x0097;
    L_0x0095:
        r3 = 1;
        goto L_0x0098;
    L_0x0097:
        r3 = 0;
    L_0x0098:
        if (r3 == 0) goto L_0x00a4;
    L_0x009a:
        r0 = r30.isOutOwner();
        r5 = r1.messageText;
        addLinks(r0, r5);
        goto L_0x00bf;
    L_0x00a4:
        r0 = r1.messageText;
        r5 = r0 instanceof android.text.Spannable;
        if (r5 == 0) goto L_0x00bf;
    L_0x00aa:
        r0 = r0.length();
        r5 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        if (r0 >= r5) goto L_0x00bf;
    L_0x00b2:
        r0 = r1.messageText;	 Catch:{ Throwable -> 0x00bb }
        r0 = (android.text.Spannable) r0;	 Catch:{ Throwable -> 0x00bb }
        r5 = 4;
        android.text.util.Linkify.addLinks(r0, r5);	 Catch:{ Throwable -> 0x00bb }
        goto L_0x00bf;
    L_0x00bb:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x00bf:
        r0 = r1.messageText;
        r3 = r1.addEntitiesToText(r0, r3);
        r15 = r30.getMaxMessageTextWidth();
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r0 == 0) goto L_0x00d4;
    L_0x00d1:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_msgGameTextPaint;
        goto L_0x00d6;
    L_0x00d4:
        r0 = org.telegram.p004ui.ActionBar.Theme.chat_msgTextPaint;
    L_0x00d6:
        r14 = r0;
        r0 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x042b }
        r13 = 24;
        if (r0 < r13) goto L_0x00fc;
    L_0x00dd:
        r0 = r1.messageText;	 Catch:{ Exception -> 0x042b }
        r5 = r1.messageText;	 Catch:{ Exception -> 0x042b }
        r5 = r5.length();	 Catch:{ Exception -> 0x042b }
        r0 = android.text.StaticLayout.Builder.obtain(r0, r2, r5, r14, r15);	 Catch:{ Exception -> 0x042b }
        r0 = r0.setBreakStrategy(r4);	 Catch:{ Exception -> 0x042b }
        r0 = r0.setHyphenationFrequency(r2);	 Catch:{ Exception -> 0x042b }
        r5 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x042b }
        r0 = r0.setAlignment(r5);	 Catch:{ Exception -> 0x042b }
        r0 = r0.build();	 Catch:{ Exception -> 0x042b }
        goto L_0x010c;
    L_0x00fc:
        r0 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x042b }
        r6 = r1.messageText;	 Catch:{ Exception -> 0x042b }
        r9 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x042b }
        r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r11 = 0;
        r12 = 0;
        r5 = r0;
        r7 = r14;
        r8 = r15;
        r5.<init>(r6, r7, r8, r9, r10, r11, r12);	 Catch:{ Exception -> 0x042b }
    L_0x010c:
        r12 = r0;
        r0 = r12.getHeight();
        r1.textHeight = r0;
        r0 = r12.getLineCount();
        r1.linesCount = r0;
        r0 = android.os.Build.VERSION.SDK_INT;
        if (r0 < r13) goto L_0x011f;
    L_0x011d:
        r11 = 1;
        goto L_0x012c;
    L_0x011f:
        r0 = r1.linesCount;
        r0 = (float) r0;
        r5 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r0 = r0 / r5;
        r5 = (double) r0;
        r5 = java.lang.Math.ceil(r5);
        r0 = (int) r5;
        r11 = r0;
    L_0x012c:
        r10 = 0;
        r8 = 0;
        r9 = 0;
        r16 = 0;
    L_0x0131:
        if (r9 >= r11) goto L_0x042a;
    L_0x0133:
        r0 = android.os.Build.VERSION.SDK_INT;
        if (r0 < r13) goto L_0x013a;
    L_0x0137:
        r0 = r1.linesCount;
        goto L_0x0143;
    L_0x013a:
        r0 = 10;
        r5 = r1.linesCount;
        r5 = r5 - r8;
        r0 = java.lang.Math.min(r0, r5);
    L_0x0143:
        r7 = new org.telegram.messenger.MessageObject$TextLayoutBlock;
        r7.<init>();
        r6 = 2;
        if (r11 != r4) goto L_0x01b7;
    L_0x014b:
        r7.textLayout = r12;
        r7.textYOffset = r10;
        r7.charactersOffset = r2;
        r5 = r1.emojiOnlyCount;
        if (r5 == 0) goto L_0x01a3;
    L_0x0155:
        if (r5 == r4) goto L_0x018c;
    L_0x0157:
        if (r5 == r6) goto L_0x0175;
    L_0x0159:
        r6 = 3;
        if (r5 == r6) goto L_0x015d;
    L_0x015c:
        goto L_0x01a3;
    L_0x015d:
        r5 = r1.textHeight;
        r6 = 1082549862; // 0x40866666 float:4.2 double:5.348506967E-315;
        r17 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 - r17;
        r1.textHeight = r5;
        r5 = r7.textYOffset;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r6 = (float) r6;
        r5 = r5 - r6;
        r7.textYOffset = r5;
        goto L_0x01a3;
    L_0x0175:
        r5 = r1.textHeight;
        r6 = 1083179008; // 0x40900000 float:4.5 double:5.35161536E-315;
        r17 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 - r17;
        r1.textHeight = r5;
        r5 = r7.textYOffset;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r6 = (float) r6;
        r5 = r5 - r6;
        r7.textYOffset = r5;
        goto L_0x01a3;
    L_0x018c:
        r5 = r1.textHeight;
        r6 = 1084856730; // 0x40a9999a float:5.3 double:5.35990441E-315;
        r17 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 - r17;
        r1.textHeight = r5;
        r5 = r7.textYOffset;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r6 = (float) r6;
        r5 = r5 - r6;
        r7.textYOffset = r5;
    L_0x01a3:
        r5 = r1.textHeight;
        r7.height = r5;
        r5 = r7;
        r2 = r8;
        r4 = r9;
        r8 = r11;
        r6 = r12;
        r18 = r14;
        r7 = r16;
        r17 = 24;
        r25 = 2;
    L_0x01b4:
        r9 = r0;
        goto L_0x029a;
    L_0x01b7:
        r6 = r12.getLineStart(r8);
        r5 = r8 + r0;
        r5 = r5 - r4;
        r5 = r12.getLineEnd(r5);
        if (r5 >= r6) goto L_0x01d5;
    L_0x01c4:
        r19 = r3;
        r20 = r8;
        r4 = r9;
        r8 = r11;
        r28 = r12;
        r18 = r14;
        r3 = r15;
        r2 = 0;
        r10 = 1;
        r17 = 24;
        goto L_0x0416;
    L_0x01d5:
        r7.charactersOffset = r6;
        r7.charactersEnd = r5;
        if (r3 == 0) goto L_0x020e;
    L_0x01db:
        r10 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0403 }
        if (r10 < r13) goto L_0x020e;
    L_0x01df:
        r10 = r1.messageText;	 Catch:{ Exception -> 0x0403 }
        r18 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r18 = org.telegram.messenger.AndroidUtilities.m26dp(r18);	 Catch:{ Exception -> 0x0403 }
        r13 = r15 + r18;
        r5 = android.text.StaticLayout.Builder.obtain(r10, r6, r5, r14, r13);	 Catch:{ Exception -> 0x0403 }
        r5 = r5.setBreakStrategy(r4);	 Catch:{ Exception -> 0x0403 }
        r5 = r5.setHyphenationFrequency(r2);	 Catch:{ Exception -> 0x0403 }
        r6 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0403 }
        r5 = r5.setAlignment(r6);	 Catch:{ Exception -> 0x0403 }
        r5 = r5.build();	 Catch:{ Exception -> 0x0403 }
        r7.textLayout = r5;	 Catch:{ Exception -> 0x0403 }
        r5 = r7;
        r2 = r8;
        r4 = r9;
        r27 = r11;
        r6 = r12;
        r18 = r14;
        r17 = 24;
        r25 = 2;
        goto L_0x024a;
    L_0x020e:
        r13 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0403 }
        r10 = r1.messageText;	 Catch:{ Exception -> 0x0403 }
        r18 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0403 }
        r20 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r21 = 0;
        r22 = 0;
        r23 = r5;
        r5 = r13;
        r24 = r6;
        r25 = 2;
        r6 = r10;
        r10 = r7;
        r7 = r24;
        r2 = r8;
        r8 = r23;
        r4 = r9;
        r9 = r14;
        r26 = r10;
        r10 = r15;
        r27 = r11;
        r11 = r18;
        r28 = r12;
        r12 = r20;
        r29 = r13;
        r17 = 24;
        r13 = r21;
        r18 = r14;
        r14 = r22;
        r5.<init>(r6, r7, r8, r9, r10, r11, r12, r13, r14);	 Catch:{ Exception -> 0x03f8 }
        r5 = r26;
        r6 = r29;
        r5.textLayout = r6;	 Catch:{ Exception -> 0x03f8 }
        r6 = r28;
    L_0x024a:
        r7 = r6.getLineTop(r2);	 Catch:{ Exception -> 0x03f0 }
        r7 = (float) r7;	 Catch:{ Exception -> 0x03f0 }
        r5.textYOffset = r7;	 Catch:{ Exception -> 0x03f0 }
        if (r4 == 0) goto L_0x025a;
    L_0x0253:
        r7 = r5.textYOffset;	 Catch:{ Exception -> 0x03f0 }
        r7 = r7 - r16;
        r7 = (int) r7;	 Catch:{ Exception -> 0x03f0 }
        r5.height = r7;	 Catch:{ Exception -> 0x03f0 }
    L_0x025a:
        r7 = r5.height;	 Catch:{ Exception -> 0x03f0 }
        r8 = r5.textLayout;	 Catch:{ Exception -> 0x03f0 }
        r9 = r5.textLayout;	 Catch:{ Exception -> 0x03f0 }
        r9 = r9.getLineCount();	 Catch:{ Exception -> 0x03f0 }
        r10 = 1;
        r9 = r9 - r10;
        r8 = r8.getLineBottom(r9);	 Catch:{ Exception -> 0x03f0 }
        r7 = java.lang.Math.max(r7, r8);	 Catch:{ Exception -> 0x03f0 }
        r5.height = r7;	 Catch:{ Exception -> 0x03f0 }
        r7 = r5.textYOffset;	 Catch:{ Exception -> 0x03f0 }
        r8 = r27;
        r11 = r8 + -1;
        if (r4 != r11) goto L_0x01b4;
    L_0x0278:
        r9 = r5.textLayout;
        r9 = r9.getLineCount();
        r9 = java.lang.Math.max(r0, r9);
        r0 = r1.textHeight;	 Catch:{ Exception -> 0x0296 }
        r10 = r5.textYOffset;	 Catch:{ Exception -> 0x0296 }
        r11 = r5.textLayout;	 Catch:{ Exception -> 0x0296 }
        r11 = r11.getHeight();	 Catch:{ Exception -> 0x0296 }
        r11 = (float) r11;	 Catch:{ Exception -> 0x0296 }
        r10 = r10 + r11;
        r10 = (int) r10;	 Catch:{ Exception -> 0x0296 }
        r0 = java.lang.Math.max(r0, r10);	 Catch:{ Exception -> 0x0296 }
        r1.textHeight = r0;	 Catch:{ Exception -> 0x0296 }
        goto L_0x029a;
    L_0x0296:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x029a:
        r0 = r1.textLayoutBlocks;
        r0.add(r5);
        r0 = r5.textLayout;	 Catch:{ Exception -> 0x02b5 }
        r10 = r9 + -1;
        r10 = r0.getLineLeft(r10);	 Catch:{ Exception -> 0x02b5 }
        if (r4 != 0) goto L_0x02b3;
    L_0x02a9:
        r11 = 0;
        r0 = (r10 > r11 ? 1 : (r10 == r11 ? 0 : -1));
        if (r0 < 0) goto L_0x02bf;
    L_0x02ae:
        r1.textXOffset = r10;	 Catch:{ Exception -> 0x02b1 }
        goto L_0x02bf;
    L_0x02b1:
        r0 = move-exception;
        goto L_0x02b7;
    L_0x02b3:
        r11 = 0;
        goto L_0x02bf;
    L_0x02b5:
        r0 = move-exception;
        r11 = 0;
    L_0x02b7:
        if (r4 != 0) goto L_0x02bb;
    L_0x02b9:
        r1.textXOffset = r11;
    L_0x02bb:
        org.telegram.messenger.FileLog.m30e(r0);
        r10 = 0;
    L_0x02bf:
        r0 = r5.textLayout;	 Catch:{ Exception -> 0x02c8 }
        r12 = r9 + -1;
        r0 = r0.getLineWidth(r12);	 Catch:{ Exception -> 0x02c8 }
        goto L_0x02cd;
    L_0x02c8:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
        r0 = 0;
    L_0x02cd:
        r12 = (double) r0;
        r12 = java.lang.Math.ceil(r12);
        r12 = (int) r12;
        r13 = r15 + 80;
        if (r12 <= r13) goto L_0x02d8;
    L_0x02d7:
        r12 = r15;
    L_0x02d8:
        r13 = r8 + -1;
        if (r4 != r13) goto L_0x02de;
    L_0x02dc:
        r1.lastLineWidth = r12;
    L_0x02de:
        r0 = r0 + r10;
        r14 = r12;
        r11 = (double) r0;
        r11 = java.lang.Math.ceil(r11);
        r11 = (int) r11;
        r12 = 1;
        if (r9 <= r12) goto L_0x03a0;
    L_0x02e9:
        r19 = r3;
        r28 = r6;
        r16 = r7;
        r7 = r11;
        r3 = r14;
        r6 = 0;
        r10 = 0;
        r12 = 0;
        r14 = 0;
    L_0x02f5:
        if (r10 >= r9) goto L_0x037b;
    L_0x02f7:
        r0 = r5.textLayout;	 Catch:{ Exception -> 0x02fe }
        r0 = r0.getLineWidth(r10);	 Catch:{ Exception -> 0x02fe }
        goto L_0x0303;
    L_0x02fe:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
        r0 = 0;
    L_0x0303:
        r20 = r2;
        r2 = r15 + 20;
        r2 = (float) r2;
        r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x030d;
    L_0x030c:
        r0 = (float) r15;
    L_0x030d:
        r2 = r0;
        r0 = r5.textLayout;	 Catch:{ Exception -> 0x0315 }
        r0 = r0.getLineLeft(r10);	 Catch:{ Exception -> 0x0315 }
        goto L_0x031a;
    L_0x0315:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
        r0 = 0;
    L_0x031a:
        r21 = 0;
        r22 = (r0 > r21 ? 1 : (r0 == r21 ? 0 : -1));
        if (r22 <= 0) goto L_0x0336;
    L_0x0320:
        r21 = r9;
        r9 = r1.textXOffset;
        r9 = java.lang.Math.min(r9, r0);
        r1.textXOffset = r9;
        r9 = r5.directionFlags;
        r22 = r15;
        r15 = 1;
        r9 = r9 | r15;
        r9 = (byte) r9;
        r5.directionFlags = r9;
        r1.hasRtl = r15;
        goto L_0x0341;
    L_0x0336:
        r21 = r9;
        r22 = r15;
        r9 = r5.directionFlags;
        r9 = r9 | 2;
        r9 = (byte) r9;
        r5.directionFlags = r9;
    L_0x0341:
        if (r12 != 0) goto L_0x0352;
    L_0x0343:
        r9 = 0;
        r15 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1));
        if (r15 != 0) goto L_0x0352;
    L_0x0348:
        r9 = r5.textLayout;	 Catch:{ Exception -> 0x0351 }
        r9 = r9.getParagraphDirection(r10);	 Catch:{ Exception -> 0x0351 }
        r15 = 1;
        if (r9 != r15) goto L_0x0352;
    L_0x0351:
        r12 = 1;
    L_0x0352:
        r6 = java.lang.Math.max(r6, r2);
        r0 = r0 + r2;
        r14 = java.lang.Math.max(r14, r0);
        r9 = r14;
        r14 = (double) r2;
        r14 = java.lang.Math.ceil(r14);
        r2 = (int) r14;
        r3 = java.lang.Math.max(r3, r2);
        r14 = (double) r0;
        r14 = java.lang.Math.ceil(r14);
        r0 = (int) r14;
        r7 = java.lang.Math.max(r7, r0);
        r10 = r10 + 1;
        r14 = r9;
        r2 = r20;
        r9 = r21;
        r15 = r22;
        goto L_0x02f5;
    L_0x037b:
        r20 = r2;
        r21 = r9;
        r22 = r15;
        if (r12 == 0) goto L_0x0388;
    L_0x0383:
        if (r4 != r13) goto L_0x038d;
    L_0x0385:
        r1.lastLineWidth = r11;
        goto L_0x038d;
    L_0x0388:
        if (r4 != r13) goto L_0x038c;
    L_0x038a:
        r1.lastLineWidth = r3;
    L_0x038c:
        r14 = r6;
    L_0x038d:
        r0 = r1.textWidth;
        r2 = (double) r14;
        r2 = java.lang.Math.ceil(r2);
        r2 = (int) r2;
        r0 = java.lang.Math.max(r0, r2);
        r1.textWidth = r0;
        r3 = r22;
        r2 = 0;
        r10 = 1;
        goto L_0x03ed;
    L_0x03a0:
        r20 = r2;
        r19 = r3;
        r28 = r6;
        r16 = r7;
        r21 = r9;
        r22 = r15;
        r2 = 0;
        r0 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1));
        if (r0 <= 0) goto L_0x03d6;
    L_0x03b1:
        r0 = r1.textXOffset;
        r0 = java.lang.Math.min(r0, r10);
        r1.textXOffset = r0;
        r0 = r1.textXOffset;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 != 0) goto L_0x03c4;
    L_0x03bf:
        r15 = r14;
        r0 = (float) r15;
        r0 = r0 + r10;
        r12 = (int) r0;
        goto L_0x03c6;
    L_0x03c4:
        r15 = r14;
        r12 = r15;
    L_0x03c6:
        r10 = 1;
        if (r8 == r10) goto L_0x03cb;
    L_0x03c9:
        r0 = 1;
        goto L_0x03cc;
    L_0x03cb:
        r0 = 0;
    L_0x03cc:
        r1.hasRtl = r0;
        r0 = r5.directionFlags;
        r0 = r0 | r10;
        r0 = (byte) r0;
        r5.directionFlags = r0;
        r15 = r12;
        goto L_0x03df;
    L_0x03d6:
        r15 = r14;
        r10 = 1;
        r0 = r5.directionFlags;
        r0 = r0 | 2;
        r0 = (byte) r0;
        r5.directionFlags = r0;
    L_0x03df:
        r0 = r1.textWidth;
        r3 = r22;
        r5 = java.lang.Math.min(r3, r15);
        r0 = java.lang.Math.max(r0, r5);
        r1.textWidth = r0;
    L_0x03ed:
        r0 = r20 + r21;
        goto L_0x0418;
    L_0x03f0:
        r0 = move-exception;
        r20 = r2;
        r19 = r3;
        r28 = r6;
        goto L_0x03fd;
    L_0x03f8:
        r0 = move-exception;
        r20 = r2;
        r19 = r3;
    L_0x03fd:
        r3 = r15;
        r8 = r27;
        r2 = 0;
        r10 = 1;
        goto L_0x0413;
    L_0x0403:
        r0 = move-exception;
        r19 = r3;
        r20 = r8;
        r4 = r9;
        r8 = r11;
        r28 = r12;
        r18 = r14;
        r3 = r15;
        r2 = 0;
        r10 = 1;
        r17 = 24;
    L_0x0413:
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0416:
        r0 = r20;
    L_0x0418:
        r9 = r4 + 1;
        r15 = r3;
        r11 = r8;
        r14 = r18;
        r3 = r19;
        r12 = r28;
        r2 = 0;
        r4 = 1;
        r10 = 0;
        r13 = 24;
        r8 = r0;
        goto L_0x0131;
    L_0x042a:
        return;
    L_0x042b:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x042f:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.generateLayout(org.telegram.tgnet.TLRPC$User):void");
    }

    public boolean isOut() {
        return this.messageOwner.out;
    }

    /* JADX WARNING: Missing block: B:16:0x0037, code skipped:
            if (r3.user_id != r0) goto L_0x0039;
     */
    public boolean isOutOwner() {
        /*
        r8 = this;
        r0 = r8.messageOwner;
        r1 = r0.out;
        r2 = 0;
        if (r1 == 0) goto L_0x0057;
    L_0x0007:
        r1 = r0.from_id;
        if (r1 <= 0) goto L_0x0057;
    L_0x000b:
        r1 = r0.post;
        if (r1 == 0) goto L_0x0010;
    L_0x000f:
        goto L_0x0057;
    L_0x0010:
        r0 = r0.fwd_from;
        r1 = 1;
        if (r0 != 0) goto L_0x0016;
    L_0x0015:
        return r1;
    L_0x0016:
        r0 = r8.currentAccount;
        r0 = org.telegram.messenger.UserConfig.getInstance(r0);
        r0 = r0.getClientUserId();
        r3 = r8.getDialogId();
        r5 = (long) r0;
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 != 0) goto L_0x0048;
    L_0x0029:
        r3 = r8.messageOwner;
        r3 = r3.fwd_from;
        r4 = r3.from_id;
        if (r4 != r0) goto L_0x0039;
    L_0x0031:
        r3 = r3.saved_from_peer;
        if (r3 == 0) goto L_0x0047;
    L_0x0035:
        r3 = r3.user_id;
        if (r3 == r0) goto L_0x0047;
    L_0x0039:
        r3 = r8.messageOwner;
        r3 = r3.fwd_from;
        r3 = r3.saved_from_peer;
        if (r3 == 0) goto L_0x0046;
    L_0x0041:
        r3 = r3.user_id;
        if (r3 != r0) goto L_0x0046;
    L_0x0045:
        goto L_0x0047;
    L_0x0046:
        r1 = 0;
    L_0x0047:
        return r1;
    L_0x0048:
        r3 = r8.messageOwner;
        r3 = r3.fwd_from;
        r3 = r3.saved_from_peer;
        if (r3 == 0) goto L_0x0056;
    L_0x0050:
        r3 = r3.user_id;
        if (r3 != r0) goto L_0x0055;
    L_0x0054:
        goto L_0x0056;
    L_0x0055:
        r1 = 0;
    L_0x0056:
        return r1;
    L_0x0057:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.isOutOwner():boolean");
    }

    public boolean needDrawAvatar() {
        if (!isFromUser() && this.eventId == 0) {
            MessageFwdHeader messageFwdHeader = this.messageOwner.fwd_from;
            if (messageFwdHeader == null || messageFwdHeader.saved_from_peer == null) {
                return false;
            }
        }
        return true;
    }

    private boolean needDrawAvatarInternal() {
        if (!(isFromChat() && isFromUser()) && this.eventId == 0) {
            MessageFwdHeader messageFwdHeader = this.messageOwner.fwd_from;
            if (messageFwdHeader == null || messageFwdHeader.saved_from_peer == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isFromChat() {
        if (!(getDialogId() == ((long) UserConfig.getInstance(this.currentAccount).clientUserId) || isMegagroup())) {
            Peer peer = this.messageOwner.to_id;
            if (peer == null || peer.chat_id == 0) {
                peer = this.messageOwner.to_id;
                boolean z = false;
                if (!(peer == null || peer.channel_id == 0)) {
                    Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.messageOwner.to_id.channel_id));
                    if (chat != null && chat.megagroup) {
                        z = true;
                    }
                }
                return z;
            }
        }
        return true;
    }

    public boolean isFromUser() {
        Message message = this.messageOwner;
        return message.from_id > 0 && !message.post;
    }

    public boolean isForwardedChannelPost() {
        Message message = this.messageOwner;
        if (message.from_id <= 0) {
            MessageFwdHeader messageFwdHeader = message.fwd_from;
            if (!(messageFwdHeader == null || messageFwdHeader.channel_post == 0)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUnread() {
        return this.messageOwner.unread;
    }

    public boolean isContentUnread() {
        return this.messageOwner.media_unread;
    }

    public void setIsRead() {
        this.messageOwner.unread = false;
    }

    public int getUnradFlags() {
        return getUnreadFlags(this.messageOwner);
    }

    public static int getUnreadFlags(Message message) {
        int i = !message.unread ? 1 : 0;
        return !message.media_unread ? i | 2 : i;
    }

    public void setContentIsRead() {
        this.messageOwner.media_unread = false;
    }

    public int getId() {
        return this.messageOwner.f461id;
    }

    public int getRealId() {
        Message message = this.messageOwner;
        int i = message.realId;
        return i != 0 ? i : message.f461id;
    }

    public static int getMessageSize(Message message) {
        MessageMedia messageMedia = message.media;
        Document document = messageMedia instanceof TL_messageMediaWebPage ? messageMedia.webpage.document : messageMedia instanceof TL_messageMediaGame ? messageMedia.game.document : messageMedia != null ? messageMedia.document : null;
        return document != null ? document.size : 0;
    }

    public int getSize() {
        return getMessageSize(this.messageOwner);
    }

    public long getIdWithChannel() {
        Message message = this.messageOwner;
        long j = (long) message.f461id;
        Peer peer = message.to_id;
        if (peer == null) {
            return j;
        }
        int i = peer.channel_id;
        return i != 0 ? j | (((long) i) << 32) : j;
    }

    public int getChannelId() {
        Peer peer = this.messageOwner.to_id;
        return peer != null ? peer.channel_id : 0;
    }

    /* JADX WARNING: Missing block: B:9:0x0018, code skipped:
            if (r4 <= 60) goto L_0x001c;
     */
    public static boolean shouldEncryptPhotoOrVideo(org.telegram.tgnet.TLRPC.Message r4) {
        /*
        r0 = r4 instanceof org.telegram.tgnet.TLRPC.TL_message_secret;
        r1 = 1;
        r2 = 0;
        if (r0 == 0) goto L_0x001d;
    L_0x0006:
        r0 = r4.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r0 != 0) goto L_0x0012;
    L_0x000c:
        r0 = isVideoMessage(r4);
        if (r0 == 0) goto L_0x001b;
    L_0x0012:
        r4 = r4.ttl;
        if (r4 <= 0) goto L_0x001b;
    L_0x0016:
        r0 = 60;
        if (r4 > r0) goto L_0x001b;
    L_0x001a:
        goto L_0x001c;
    L_0x001b:
        r1 = 0;
    L_0x001c:
        return r1;
    L_0x001d:
        r0 = r4.media;
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r3 != 0) goto L_0x0027;
    L_0x0023:
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r0 == 0) goto L_0x002e;
    L_0x0027:
        r4 = r4.media;
        r4 = r4.ttl_seconds;
        if (r4 == 0) goto L_0x002e;
    L_0x002d:
        goto L_0x002f;
    L_0x002e:
        r1 = 0;
    L_0x002f:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.shouldEncryptPhotoOrVideo(org.telegram.tgnet.TLRPC$Message):boolean");
    }

    public boolean shouldEncryptPhotoOrVideo() {
        return shouldEncryptPhotoOrVideo(this.messageOwner);
    }

    /* JADX WARNING: Missing block: B:11:0x001e, code skipped:
            if (r4 <= 60) goto L_0x0022;
     */
    public static boolean isSecretPhotoOrVideo(org.telegram.tgnet.TLRPC.Message r4) {
        /*
        r0 = r4 instanceof org.telegram.tgnet.TLRPC.TL_message_secret;
        r1 = 1;
        r2 = 0;
        if (r0 == 0) goto L_0x0023;
    L_0x0006:
        r0 = r4.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r0 != 0) goto L_0x0018;
    L_0x000c:
        r0 = isRoundVideoMessage(r4);
        if (r0 != 0) goto L_0x0018;
    L_0x0012:
        r0 = isVideoMessage(r4);
        if (r0 == 0) goto L_0x0021;
    L_0x0018:
        r4 = r4.ttl;
        if (r4 <= 0) goto L_0x0021;
    L_0x001c:
        r0 = 60;
        if (r4 > r0) goto L_0x0021;
    L_0x0020:
        goto L_0x0022;
    L_0x0021:
        r1 = 0;
    L_0x0022:
        return r1;
    L_0x0023:
        r0 = r4 instanceof org.telegram.tgnet.TLRPC.TL_message;
        if (r0 == 0) goto L_0x003a;
    L_0x0027:
        r0 = r4.media;
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r3 != 0) goto L_0x0031;
    L_0x002d:
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r0 == 0) goto L_0x0038;
    L_0x0031:
        r4 = r4.media;
        r4 = r4.ttl_seconds;
        if (r4 == 0) goto L_0x0038;
    L_0x0037:
        goto L_0x0039;
    L_0x0038:
        r1 = 0;
    L_0x0039:
        return r1;
    L_0x003a:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.isSecretPhotoOrVideo(org.telegram.tgnet.TLRPC$Message):boolean");
    }

    public static boolean isSecretMedia(Message message) {
        boolean z = true;
        if (message instanceof TL_message_secret) {
            if (!((message.media instanceof TL_messageMediaPhoto) || isRoundVideoMessage(message) || isVideoMessage(message)) || message.media.ttl_seconds == 0) {
                z = false;
            }
            return z;
        } else if (!(message instanceof TL_message)) {
            return false;
        } else {
            MessageMedia messageMedia = message.media;
            if (!((messageMedia instanceof TL_messageMediaPhoto) || (messageMedia instanceof TL_messageMediaDocument)) || message.media.ttl_seconds == 0) {
                z = false;
            }
            return z;
        }
    }

    public boolean needDrawBluredPreview() {
        Message message = this.messageOwner;
        boolean z = true;
        if (message instanceof TL_message_secret) {
            int max = Math.max(message.ttl, message.media.ttl_seconds);
            if (max <= 0 || ((!((this.messageOwner.media instanceof TL_messageMediaPhoto) || isVideo() || isGif()) || max > 60) && !isRoundVideo())) {
                z = false;
            }
            return z;
        } else if (!(message instanceof TL_message)) {
            return false;
        } else {
            MessageMedia messageMedia = message.media;
            if (!((messageMedia instanceof TL_messageMediaPhoto) || (messageMedia instanceof TL_messageMediaDocument)) || this.messageOwner.media.ttl_seconds == 0) {
                z = false;
            }
            return z;
        }
    }

    /* JADX WARNING: Missing block: B:9:0x001c, code skipped:
            if (r0 <= 60) goto L_0x0032;
     */
    public boolean isSecretMedia() {
        /*
        r4 = this;
        r0 = r4.messageOwner;
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_message_secret;
        r2 = 1;
        r3 = 0;
        if (r1 == 0) goto L_0x0033;
    L_0x0008:
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r0 != 0) goto L_0x0014;
    L_0x000e:
        r0 = r4.isGif();
        if (r0 == 0) goto L_0x001e;
    L_0x0014:
        r0 = r4.messageOwner;
        r0 = r0.ttl;
        if (r0 <= 0) goto L_0x001e;
    L_0x001a:
        r1 = 60;
        if (r0 <= r1) goto L_0x0032;
    L_0x001e:
        r0 = r4.isVoice();
        if (r0 != 0) goto L_0x0032;
    L_0x0024:
        r0 = r4.isRoundVideo();
        if (r0 != 0) goto L_0x0032;
    L_0x002a:
        r0 = r4.isVideo();
        if (r0 == 0) goto L_0x0031;
    L_0x0030:
        goto L_0x0032;
    L_0x0031:
        r2 = 0;
    L_0x0032:
        return r2;
    L_0x0033:
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_message;
        if (r1 == 0) goto L_0x004c;
    L_0x0037:
        r0 = r0.media;
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r1 != 0) goto L_0x0041;
    L_0x003d:
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r0 == 0) goto L_0x004a;
    L_0x0041:
        r0 = r4.messageOwner;
        r0 = r0.media;
        r0 = r0.ttl_seconds;
        if (r0 == 0) goto L_0x004a;
    L_0x0049:
        goto L_0x004b;
    L_0x004a:
        r2 = 0;
    L_0x004b:
        return r2;
    L_0x004c:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.isSecretMedia():boolean");
    }

    public static void setUnreadFlags(Message message, int i) {
        boolean z = false;
        message.unread = (i & 1) == 0;
        if ((i & 2) == 0) {
            z = true;
        }
        message.media_unread = z;
    }

    public static boolean isUnread(Message message) {
        return message.unread;
    }

    public static boolean isContentUnread(Message message) {
        return message.media_unread;
    }

    public boolean isMegagroup() {
        return isMegagroup(this.messageOwner);
    }

    public boolean isSavedFromMegagroup() {
        MessageFwdHeader messageFwdHeader = this.messageOwner.fwd_from;
        if (messageFwdHeader != null) {
            Peer peer = messageFwdHeader.saved_from_peer;
            if (!(peer == null || peer.channel_id == 0)) {
                return ChatObject.isMegagroup(MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.messageOwner.fwd_from.saved_from_peer.channel_id)));
            }
        }
        return false;
    }

    public static boolean isMegagroup(Message message) {
        return (message.flags & Integer.MIN_VALUE) != 0;
    }

    public static boolean isOut(Message message) {
        return message.out;
    }

    public long getDialogId() {
        return getDialogId(this.messageOwner);
    }

    public boolean canStreamVideo() {
        Document document = getDocument();
        if (!(document == null || (document instanceof TL_documentEncrypted))) {
            if (SharedConfig.streamAllVideo) {
                return true;
            }
            for (int i = 0; i < document.attributes.size(); i++) {
                DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(i);
                if (documentAttribute instanceof TL_documentAttributeVideo) {
                    return documentAttribute.supports_streaming;
                }
            }
            if (SharedConfig.streamMkv) {
                if ("video/x-matroska".equals(document.mime_type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static long getDialogId(Message message) {
        if (message.dialog_id == 0) {
            Peer peer = message.to_id;
            if (peer != null) {
                int i = peer.chat_id;
                if (i == 0) {
                    int i2 = peer.channel_id;
                    if (i2 != 0) {
                        message.dialog_id = (long) (-i2);
                    } else if (isOut(message)) {
                        message.dialog_id = (long) message.to_id.user_id;
                    } else {
                        message.dialog_id = (long) message.from_id;
                    }
                } else if (i < 0) {
                    message.dialog_id = AndroidUtilities.makeBroadcastId(i);
                } else {
                    message.dialog_id = (long) (-i);
                }
            }
        }
        return message.dialog_id;
    }

    public boolean isSending() {
        Message message = this.messageOwner;
        return message.send_state == 1 && message.f461id < 0;
    }

    public boolean isEditing() {
        Message message = this.messageOwner;
        return message.send_state == 3 && message.f461id > 0;
    }

    public boolean isSendError() {
        Message message = this.messageOwner;
        return message.send_state == 2 && message.f461id < 0;
    }

    public boolean isSent() {
        Message message = this.messageOwner;
        return message.send_state == 0 || message.f461id > 0;
    }

    public int getSecretTimeLeft() {
        Message message = this.messageOwner;
        int i = message.ttl;
        int i2 = message.destroyTime;
        return i2 != 0 ? Math.max(0, i2 - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) : i;
    }

    public String getSecretTimeString() {
        if (!isSecretMedia()) {
            return null;
        }
        String stringBuilder;
        int secretTimeLeft = getSecretTimeLeft();
        if (secretTimeLeft < 60) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(secretTimeLeft);
            stringBuilder2.append("s");
            stringBuilder = stringBuilder2.toString();
        } else {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(secretTimeLeft / 60);
            stringBuilder3.append("m");
            stringBuilder = stringBuilder3.toString();
        }
        return stringBuilder;
    }

    public String getDocumentName() {
        MessageMedia messageMedia = this.messageOwner.media;
        if (messageMedia instanceof TL_messageMediaDocument) {
            return FileLoader.getDocumentFileName(messageMedia.document);
        }
        return messageMedia instanceof TL_messageMediaWebPage ? FileLoader.getDocumentFileName(messageMedia.webpage.document) : "";
    }

    public static boolean isStickerDocument(Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                if (((DocumentAttribute) document.attributes.get(i)) instanceof TL_documentAttributeSticker) {
                    return "image/webp".equals(document.mime_type);
                }
            }
        }
        return false;
    }

    public static boolean isAnimatedStickerDocument(Document document) {
        if (SharedConfig.showAnimatedStickers && document != null) {
            if ("application/x-tgsticker".equals(document.mime_type) && !document.thumbs.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMaskDocument(Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(i);
                if ((documentAttribute instanceof TL_documentAttributeSticker) && documentAttribute.mask) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isVoiceDocument(Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(i);
                if (documentAttribute instanceof TL_documentAttributeAudio) {
                    return documentAttribute.voice;
                }
            }
        }
        return false;
    }

    public static boolean isVoiceWebDocument(WebFile webFile) {
        return webFile != null && webFile.mime_type.equals("audio/ogg");
    }

    public static boolean isImageWebDocument(WebFile webFile) {
        return (webFile == null || isGifDocument(webFile) || !webFile.mime_type.startsWith("image/")) ? false : true;
    }

    public static boolean isVideoWebDocument(WebFile webFile) {
        return webFile != null && webFile.mime_type.startsWith("video/");
    }

    public static boolean isMusicDocument(Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(i);
                if (documentAttribute instanceof TL_documentAttributeAudio) {
                    return documentAttribute.voice ^ 1;
                }
            }
            if (!TextUtils.isEmpty(document.mime_type)) {
                String toLowerCase = document.mime_type.toLowerCase();
                if (toLowerCase.equals(MimeTypes.AUDIO_FLAC) || toLowerCase.equals("audio/ogg") || toLowerCase.equals(MimeTypes.AUDIO_OPUS) || toLowerCase.equals("audio/x-opus+ogg") || (toLowerCase.equals("application/octet-stream") && FileLoader.getDocumentFileName(document).endsWith(".opus"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isVideoDocument(Document document) {
        boolean z = false;
        if (document != null) {
            Object obj = null;
            int i = 0;
            int i2 = 0;
            Object obj2 = null;
            for (int i3 = 0; i3 < document.attributes.size(); i3++) {
                DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(i3);
                if (documentAttribute instanceof TL_documentAttributeVideo) {
                    if (documentAttribute.round_message) {
                        return false;
                    }
                    i = documentAttribute.f444w;
                    i2 = documentAttribute.f443h;
                    obj2 = 1;
                } else if (documentAttribute instanceof TL_documentAttributeAnimated) {
                    obj = 1;
                }
            }
            if (obj != null && (i > 1280 || i2 > 1280)) {
                obj = null;
            }
            if (SharedConfig.streamMkv && r5 == null) {
                if ("video/x-matroska".equals(document.mime_type)) {
                    obj2 = 1;
                }
            }
            if (obj2 != null && r2 == null) {
                z = true;
            }
        }
        return z;
    }

    public Document getDocument() {
        return getDocument(this.messageOwner);
    }

    public static Document getDocument(Message message) {
        MessageMedia messageMedia = message.media;
        if (messageMedia instanceof TL_messageMediaWebPage) {
            return messageMedia.webpage.document;
        }
        if (messageMedia instanceof TL_messageMediaGame) {
            return messageMedia.game.document;
        }
        return messageMedia != null ? messageMedia.document : null;
    }

    public static Photo getPhoto(Message message) {
        MessageMedia messageMedia = message.media;
        if (messageMedia instanceof TL_messageMediaWebPage) {
            return messageMedia.webpage.photo;
        }
        return messageMedia != null ? messageMedia.photo : null;
    }

    public static boolean isStickerMessage(Message message) {
        MessageMedia messageMedia = message.media;
        return messageMedia != null && isStickerDocument(messageMedia.document);
    }

    public static boolean isAnimatedStickerMessage(Message message) {
        MessageMedia messageMedia = message.media;
        return messageMedia != null && isAnimatedStickerDocument(messageMedia.document);
    }

    public static boolean isLocationMessage(Message message) {
        MessageMedia messageMedia = message.media;
        return (messageMedia instanceof TL_messageMediaGeo) || (messageMedia instanceof TL_messageMediaGeoLive) || (messageMedia instanceof TL_messageMediaVenue);
    }

    public static boolean isMaskMessage(Message message) {
        MessageMedia messageMedia = message.media;
        return messageMedia != null && isMaskDocument(messageMedia.document);
    }

    public static boolean isMusicMessage(Message message) {
        MessageMedia messageMedia = message.media;
        if (messageMedia instanceof TL_messageMediaWebPage) {
            return isMusicDocument(messageMedia.webpage.document);
        }
        boolean z = messageMedia != null && isMusicDocument(messageMedia.document);
        return z;
    }

    public static boolean isGifMessage(Message message) {
        MessageMedia messageMedia = message.media;
        if (messageMedia instanceof TL_messageMediaWebPage) {
            return isGifDocument(messageMedia.webpage.document);
        }
        boolean z = messageMedia != null && isGifDocument(messageMedia.document);
        return z;
    }

    public static boolean isRoundVideoMessage(Message message) {
        MessageMedia messageMedia = message.media;
        if (messageMedia instanceof TL_messageMediaWebPage) {
            return isRoundVideoDocument(messageMedia.webpage.document);
        }
        boolean z = messageMedia != null && isRoundVideoDocument(messageMedia.document);
        return z;
    }

    public static boolean isPhoto(Message message) {
        MessageMedia messageMedia = message.media;
        if (!(messageMedia instanceof TL_messageMediaWebPage)) {
            return messageMedia instanceof TL_messageMediaPhoto;
        }
        WebPage webPage = messageMedia.webpage;
        boolean z = (webPage.photo instanceof TL_photo) && !(webPage.document instanceof TL_document);
        return z;
    }

    public static boolean isVoiceMessage(Message message) {
        MessageMedia messageMedia = message.media;
        if (messageMedia instanceof TL_messageMediaWebPage) {
            return isVoiceDocument(messageMedia.webpage.document);
        }
        boolean z = messageMedia != null && isVoiceDocument(messageMedia.document);
        return z;
    }

    public static boolean isNewGifMessage(Message message) {
        MessageMedia messageMedia = message.media;
        if (messageMedia instanceof TL_messageMediaWebPage) {
            return isNewGifDocument(messageMedia.webpage.document);
        }
        boolean z = messageMedia != null && isNewGifDocument(messageMedia.document);
        return z;
    }

    public static boolean isLiveLocationMessage(Message message) {
        return message.media instanceof TL_messageMediaGeoLive;
    }

    public static boolean isVideoMessage(Message message) {
        MessageMedia messageMedia = message.media;
        if (messageMedia instanceof TL_messageMediaWebPage) {
            return isVideoDocument(messageMedia.webpage.document);
        }
        boolean z = messageMedia != null && isVideoDocument(messageMedia.document);
        return z;
    }

    public static boolean isGameMessage(Message message) {
        return message.media instanceof TL_messageMediaGame;
    }

    public static boolean isInvoiceMessage(Message message) {
        return message.media instanceof TL_messageMediaInvoice;
    }

    public static InputStickerSet getInputStickerSet(Message message) {
        MessageMedia messageMedia = message.media;
        if (messageMedia != null) {
            Document document = messageMedia.document;
            if (document != null) {
                Iterator it = document.attributes.iterator();
                while (it.hasNext()) {
                    DocumentAttribute documentAttribute = (DocumentAttribute) it.next();
                    if (documentAttribute instanceof TL_documentAttributeSticker) {
                        InputStickerSet inputStickerSet = documentAttribute.stickerset;
                        if (inputStickerSet instanceof TL_inputStickerSetEmpty) {
                            return null;
                        }
                        return inputStickerSet;
                    }
                }
            }
        }
        return null;
    }

    public static long getStickerSetId(Document document) {
        if (document == null) {
            return -1;
        }
        for (int i = 0; i < document.attributes.size(); i++) {
            DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(i);
            if (documentAttribute instanceof TL_documentAttributeSticker) {
                InputStickerSet inputStickerSet = documentAttribute.stickerset;
                if (inputStickerSet instanceof TL_inputStickerSetEmpty) {
                    return -1;
                }
                return inputStickerSet.f460id;
            }
        }
        return -1;
    }

    public String getStrickerChar() {
        MessageMedia messageMedia = this.messageOwner.media;
        if (messageMedia != null) {
            Document document = messageMedia.document;
            if (document != null) {
                Iterator it = document.attributes.iterator();
                while (it.hasNext()) {
                    DocumentAttribute documentAttribute = (DocumentAttribute) it.next();
                    if (documentAttribute instanceof TL_documentAttributeSticker) {
                        return documentAttribute.alt;
                    }
                }
            }
        }
        return null;
    }

    public int getApproximateHeight() {
        int i = this.type;
        int i2 = 0;
        if (i == 0) {
            i = this.textHeight;
            MessageMedia messageMedia = this.messageOwner.media;
            if ((messageMedia instanceof TL_messageMediaWebPage) && (messageMedia.webpage instanceof TL_webPage)) {
                i2 = AndroidUtilities.m26dp(100.0f);
            }
            i += i2;
            if (isReply()) {
                i += AndroidUtilities.m26dp(42.0f);
            }
            return i;
        } else if (i == 2) {
            return AndroidUtilities.m26dp(72.0f);
        } else {
            if (i == 12) {
                return AndroidUtilities.m26dp(71.0f);
            }
            if (i == 9) {
                return AndroidUtilities.m26dp(100.0f);
            }
            if (i == 4) {
                return AndroidUtilities.m26dp(114.0f);
            }
            if (i == 14) {
                return AndroidUtilities.m26dp(82.0f);
            }
            if (i == 10) {
                return AndroidUtilities.m26dp(30.0f);
            }
            if (i == 11) {
                return AndroidUtilities.m26dp(50.0f);
            }
            if (i == 5) {
                return AndroidUtilities.roundMessageSize;
            }
            if (i == 13 || i == 15) {
                int minTabletSide;
                int i3;
                float f = ((float) AndroidUtilities.displaySize.y) * 0.4f;
                if (AndroidUtilities.isTablet()) {
                    minTabletSide = AndroidUtilities.getMinTabletSide();
                } else {
                    minTabletSide = AndroidUtilities.displaySize.x;
                }
                float f2 = ((float) minTabletSide) * 0.5f;
                Iterator it = this.messageOwner.media.document.attributes.iterator();
                while (it.hasNext()) {
                    DocumentAttribute documentAttribute = (DocumentAttribute) it.next();
                    if (documentAttribute instanceof TL_documentAttributeImageSize) {
                        i2 = documentAttribute.f444w;
                        i3 = documentAttribute.f443h;
                        break;
                    }
                }
                i3 = 0;
                if (i2 == 0) {
                    i3 = (int) f;
                    i2 = AndroidUtilities.m26dp(100.0f) + i3;
                }
                float f3 = (float) i3;
                if (f3 > f) {
                    i2 = (int) (((float) i2) * (f / f3));
                    i3 = (int) f;
                }
                f = (float) i2;
                if (f > f2) {
                    i3 = (int) (((float) i3) * (f2 / f));
                }
                return i3 + AndroidUtilities.m26dp(14.0f);
            }
            Point point;
            if (AndroidUtilities.isTablet()) {
                i = AndroidUtilities.getMinTabletSide();
            } else {
                point = AndroidUtilities.displaySize;
                i = Math.min(point.x, point.y);
            }
            i = (int) (((float) i) * 0.7f);
            i2 = AndroidUtilities.m26dp(100.0f) + i;
            if (i > AndroidUtilities.getPhotoSize()) {
                i = AndroidUtilities.getPhotoSize();
            }
            if (i2 > AndroidUtilities.getPhotoSize()) {
                i2 = AndroidUtilities.getPhotoSize();
            }
            PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize());
            if (closestPhotoSizeWithSize != null) {
                i = (int) (((float) closestPhotoSizeWithSize.f464h) / (((float) closestPhotoSizeWithSize.f465w) / ((float) i)));
                if (i == 0) {
                    i = AndroidUtilities.m26dp(100.0f);
                }
                if (i <= i2) {
                    i2 = i < AndroidUtilities.m26dp(120.0f) ? AndroidUtilities.m26dp(120.0f) : i;
                }
                if (needDrawBluredPreview()) {
                    if (AndroidUtilities.isTablet()) {
                        i = AndroidUtilities.getMinTabletSide();
                    } else {
                        point = AndroidUtilities.displaySize;
                        i = Math.min(point.x, point.y);
                    }
                    i2 = (int) (((float) i) * 0.5f);
                }
            }
            return i2 + AndroidUtilities.m26dp(14.0f);
        }
    }

    public String getStickerEmoji() {
        int i = 0;
        while (true) {
            String str = null;
            if (i >= this.messageOwner.media.document.attributes.size()) {
                return null;
            }
            DocumentAttribute documentAttribute = (DocumentAttribute) this.messageOwner.media.document.attributes.get(i);
            if (documentAttribute instanceof TL_documentAttributeSticker) {
                String str2 = documentAttribute.alt;
                if (str2 != null && str2.length() > 0) {
                    str = documentAttribute.alt;
                }
                return str;
            }
            i++;
        }
    }

    public boolean isSticker() {
        int i = this.type;
        if (i == 1000) {
            return isStickerMessage(this.messageOwner);
        }
        return i == 13;
    }

    public boolean isAnimatedSticker() {
        int i = this.type;
        if (i == 1000) {
            return isAnimatedStickerMessage(this.messageOwner);
        }
        return i == 15;
    }

    public boolean isAnyKindOfSticker() {
        int i = this.type;
        return i == 13 || i == 15;
    }

    public boolean shouldDrawWithoutBackground() {
        int i = this.type;
        return i == 13 || i == 15 || i == 5;
    }

    public boolean isLocation() {
        return isLocationMessage(this.messageOwner);
    }

    public boolean isMask() {
        return isMaskMessage(this.messageOwner);
    }

    public boolean isMusic() {
        return isMusicMessage(this.messageOwner);
    }

    public boolean isVoice() {
        return isVoiceMessage(this.messageOwner);
    }

    public boolean isVideo() {
        return isVideoMessage(this.messageOwner);
    }

    public boolean isPhoto() {
        return isPhoto(this.messageOwner);
    }

    public boolean isLiveLocation() {
        return isLiveLocationMessage(this.messageOwner);
    }

    public boolean isGame() {
        return isGameMessage(this.messageOwner);
    }

    public boolean isInvoice() {
        return isInvoiceMessage(this.messageOwner);
    }

    public boolean isRoundVideo() {
        if (this.isRoundVideoCached == 0) {
            int i = (this.type == 5 || isRoundVideoMessage(this.messageOwner)) ? 1 : 2;
            this.isRoundVideoCached = i;
        }
        if (this.isRoundVideoCached == 1) {
            return true;
        }
        return false;
    }

    public boolean hasPhotoStickers() {
        MessageMedia messageMedia = this.messageOwner.media;
        if (messageMedia != null) {
            Photo photo = messageMedia.photo;
            if (photo != null && photo.has_stickers) {
                return true;
            }
        }
        return false;
    }

    public boolean isGif() {
        return isGifMessage(this.messageOwner);
    }

    public boolean isWebpageDocument() {
        MessageMedia messageMedia = this.messageOwner.media;
        if (messageMedia instanceof TL_messageMediaWebPage) {
            Document document = messageMedia.webpage.document;
            if (!(document == null || isGifDocument(document))) {
                return true;
            }
        }
        return false;
    }

    public boolean isWebpage() {
        return this.messageOwner.media instanceof TL_messageMediaWebPage;
    }

    public boolean isNewGif() {
        MessageMedia messageMedia = this.messageOwner.media;
        return messageMedia != null && isNewGifDocument(messageMedia.document);
    }

    public String getMusicTitle() {
        return getMusicTitle(true);
    }

    public String getMusicTitle(boolean z) {
        Document document = getDocument();
        String str = "AudioUnknownTitle";
        if (document != null) {
            int i = 0;
            while (i < document.attributes.size()) {
                DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(i);
                if (documentAttribute instanceof TL_documentAttributeAudio) {
                    if (!documentAttribute.voice) {
                        String str2 = documentAttribute.title;
                        if (str2 == null || str2.length() == 0) {
                            str2 = FileLoader.getDocumentFileName(document);
                            if (TextUtils.isEmpty(str2) && z) {
                                str2 = LocaleController.getString(str, C1067R.string.AudioUnknownTitle);
                            }
                        }
                        return str2;
                    } else if (z) {
                        return LocaleController.formatDateAudio((long) this.messageOwner.date);
                    } else {
                        return null;
                    }
                } else if ((documentAttribute instanceof TL_documentAttributeVideo) && documentAttribute.round_message) {
                    return LocaleController.formatDateAudio((long) this.messageOwner.date);
                } else {
                    i++;
                }
            }
            String documentFileName = FileLoader.getDocumentFileName(document);
            if (!TextUtils.isEmpty(documentFileName)) {
                return documentFileName;
            }
        }
        return LocaleController.getString(str, C1067R.string.AudioUnknownTitle);
    }

    public int getDuration() {
        Document document = getDocument();
        int i = 0;
        if (document == null) {
            return 0;
        }
        int i2 = this.audioPlayerDuration;
        if (i2 > 0) {
            return i2;
        }
        while (i < document.attributes.size()) {
            DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(i);
            if (documentAttribute instanceof TL_documentAttributeAudio) {
                return documentAttribute.duration;
            }
            if (documentAttribute instanceof TL_documentAttributeVideo) {
                return documentAttribute.duration;
            }
            i++;
        }
        return this.audioPlayerDuration;
    }

    public String getArtworkUrl(boolean z) {
        Document document = getDocument();
        if (document != null) {
            int size = document.attributes.size();
            int i = 0;
            while (i < size) {
                DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(i);
                if (!(documentAttribute instanceof TL_documentAttributeAudio)) {
                    i++;
                } else if (documentAttribute.voice) {
                    return null;
                } else {
                    CharSequence charSequence = documentAttribute.performer;
                    String str = documentAttribute.title;
                    if (!TextUtils.isEmpty(charSequence)) {
                        String str2 = charSequence;
                        int i2 = 0;
                        while (true) {
                            String[] strArr = excludeWords;
                            if (i2 >= strArr.length) {
                                break;
                            }
                            str2 = str2.replace(strArr[i2], " ");
                            i2++;
                        }
                        charSequence = str2;
                    }
                    if (TextUtils.isEmpty(charSequence) && TextUtils.isEmpty(str)) {
                        return null;
                    }
                    try {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("athumb://itunes.apple.com/search?term=");
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(charSequence);
                        stringBuilder2.append(" - ");
                        stringBuilder2.append(str);
                        stringBuilder.append(URLEncoder.encode(stringBuilder2.toString(), "UTF-8"));
                        stringBuilder.append("&entity=song&limit=4");
                        stringBuilder.append(z ? "&s=1" : "");
                        return stringBuilder.toString();
                    } catch (Exception unused) {
                    }
                }
            }
        }
        return null;
    }

    public String getMusicAuthor() {
        return getMusicAuthor(true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:72:0x010f A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0042  */
    /* JADX WARNING: Missing block: B:18:0x003d, code skipped:
            if (r5.round_message != false) goto L_0x0026;
     */
    public java.lang.String getMusicAuthor(boolean r10) {
        /*
        r9 = this;
        r0 = r9.getDocument();
        r1 = 2131558737; // 0x7f0d0151 float:1.8742798E38 double:1.053129944E-314;
        r2 = "AudioUnknownArtist";
        if (r0 == 0) goto L_0x0113;
    L_0x000b:
        r3 = 0;
        r4 = 0;
    L_0x000d:
        r5 = r0.attributes;
        r5 = r5.size();
        if (r3 >= r5) goto L_0x0113;
    L_0x0015:
        r5 = r0.attributes;
        r5 = r5.get(r3);
        r5 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r5;
        r6 = r5 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;
        r7 = 1;
        if (r6 == 0) goto L_0x0037;
    L_0x0022:
        r4 = r5.voice;
        if (r4 == 0) goto L_0x0028;
    L_0x0026:
        r4 = 1;
        goto L_0x0040;
    L_0x0028:
        r0 = r5.performer;
        r3 = android.text.TextUtils.isEmpty(r0);
        if (r3 == 0) goto L_0x0036;
    L_0x0030:
        if (r10 == 0) goto L_0x0036;
    L_0x0032:
        r0 = org.telegram.messenger.LocaleController.getString(r2, r1);
    L_0x0036:
        return r0;
    L_0x0037:
        r6 = r5 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
        if (r6 == 0) goto L_0x0040;
    L_0x003b:
        r5 = r5.round_message;
        if (r5 == 0) goto L_0x0040;
    L_0x003f:
        goto L_0x0026;
    L_0x0040:
        if (r4 == 0) goto L_0x010f;
    L_0x0042:
        r5 = 0;
        if (r10 != 0) goto L_0x0046;
    L_0x0045:
        return r5;
    L_0x0046:
        r6 = r9.isOutOwner();
        if (r6 != 0) goto L_0x0105;
    L_0x004c:
        r6 = r9.messageOwner;
        r6 = r6.fwd_from;
        if (r6 == 0) goto L_0x0062;
    L_0x0052:
        r6 = r6.from_id;
        r7 = r9.currentAccount;
        r7 = org.telegram.messenger.UserConfig.getInstance(r7);
        r7 = r7.getClientUserId();
        if (r6 != r7) goto L_0x0062;
    L_0x0060:
        goto L_0x0105;
    L_0x0062:
        r6 = r9.messageOwner;
        r6 = r6.fwd_from;
        if (r6 == 0) goto L_0x0082;
    L_0x0068:
        r6 = r6.channel_id;
        if (r6 == 0) goto L_0x0082;
    L_0x006c:
        r6 = r9.currentAccount;
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);
        r7 = r9.messageOwner;
        r7 = r7.fwd_from;
        r7 = r7.channel_id;
        r7 = java.lang.Integer.valueOf(r7);
        r6 = r6.getChat(r7);
        goto L_0x00f9;
    L_0x0082:
        r6 = r9.messageOwner;
        r6 = r6.fwd_from;
        if (r6 == 0) goto L_0x00a4;
    L_0x0088:
        r6 = r6.from_id;
        if (r6 == 0) goto L_0x00a4;
    L_0x008c:
        r6 = r9.currentAccount;
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);
        r7 = r9.messageOwner;
        r7 = r7.fwd_from;
        r7 = r7.from_id;
        r7 = java.lang.Integer.valueOf(r7);
        r6 = r6.getUser(r7);
    L_0x00a0:
        r8 = r6;
        r6 = r5;
        r5 = r8;
        goto L_0x00f9;
    L_0x00a4:
        r6 = r9.messageOwner;
        r6 = r6.fwd_from;
        if (r6 == 0) goto L_0x00af;
    L_0x00aa:
        r6 = r6.from_name;
        if (r6 == 0) goto L_0x00af;
    L_0x00ae:
        return r6;
    L_0x00af:
        r6 = r9.messageOwner;
        r7 = r6.from_id;
        if (r7 >= 0) goto L_0x00c9;
    L_0x00b5:
        r6 = r9.currentAccount;
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);
        r7 = r9.messageOwner;
        r7 = r7.from_id;
        r7 = -r7;
        r7 = java.lang.Integer.valueOf(r7);
        r6 = r6.getChat(r7);
        goto L_0x00f9;
    L_0x00c9:
        if (r7 != 0) goto L_0x00e6;
    L_0x00cb:
        r6 = r6.to_id;
        r6 = r6.channel_id;
        if (r6 == 0) goto L_0x00e6;
    L_0x00d1:
        r6 = r9.currentAccount;
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);
        r7 = r9.messageOwner;
        r7 = r7.to_id;
        r7 = r7.channel_id;
        r7 = java.lang.Integer.valueOf(r7);
        r6 = r6.getChat(r7);
        goto L_0x00f9;
    L_0x00e6:
        r6 = r9.currentAccount;
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);
        r7 = r9.messageOwner;
        r7 = r7.from_id;
        r7 = java.lang.Integer.valueOf(r7);
        r6 = r6.getUser(r7);
        goto L_0x00a0;
    L_0x00f9:
        if (r5 == 0) goto L_0x0100;
    L_0x00fb:
        r10 = org.telegram.messenger.UserObject.getUserName(r5);
        return r10;
    L_0x0100:
        if (r6 == 0) goto L_0x010f;
    L_0x0102:
        r10 = r6.title;
        return r10;
    L_0x0105:
        r10 = 2131559584; // 0x7f0d04a0 float:1.8744516E38 double:1.0531303625E-314;
        r0 = "FromYou";
        r10 = org.telegram.messenger.LocaleController.getString(r0, r10);
        return r10;
    L_0x010f:
        r3 = r3 + 1;
        goto L_0x000d;
    L_0x0113:
        r10 = org.telegram.messenger.LocaleController.getString(r2, r1);
        return r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.getMusicAuthor(boolean):java.lang.String");
    }

    public InputStickerSet getInputStickerSet() {
        return getInputStickerSet(this.messageOwner);
    }

    public boolean isForwarded() {
        return isForwardedMessage(this.messageOwner);
    }

    public boolean needDrawForwarded() {
        Message message = this.messageOwner;
        if ((message.flags & 4) != 0) {
            MessageFwdHeader messageFwdHeader = message.fwd_from;
            if (messageFwdHeader != null) {
                Peer peer = messageFwdHeader.saved_from_peer;
                if ((peer == null || peer.channel_id != messageFwdHeader.channel_id) && ((long) UserConfig.getInstance(this.currentAccount).getClientUserId()) != getDialogId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isForwardedMessage(Message message) {
        return ((message.flags & 4) == 0 || message.fwd_from == null) ? false : true;
    }

    public boolean isReply() {
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject == null || !(messageObject.messageOwner instanceof TL_messageEmpty)) {
            Message message = this.messageOwner;
            if (!((message.reply_to_msg_id == 0 && message.reply_to_random_id == 0) || (this.messageOwner.flags & 8) == 0)) {
                return true;
            }
        }
        return false;
    }

    public boolean isMediaEmpty() {
        return isMediaEmpty(this.messageOwner);
    }

    public boolean isMediaEmptyWebpage() {
        return isMediaEmptyWebpage(this.messageOwner);
    }

    public static boolean isMediaEmpty(Message message) {
        if (message != null) {
            MessageMedia messageMedia = message.media;
            if (!(messageMedia == null || (messageMedia instanceof TL_messageMediaEmpty) || (messageMedia instanceof TL_messageMediaWebPage))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMediaEmptyWebpage(Message message) {
        if (message != null) {
            MessageMedia messageMedia = message.media;
            if (!(messageMedia == null || (messageMedia instanceof TL_messageMediaEmpty))) {
                return false;
            }
        }
        return true;
    }

    public boolean canEditMessage(Chat chat) {
        return canEditMessage(this.currentAccount, this.messageOwner, chat);
    }

    public boolean canForwardMessage() {
        return ((this.messageOwner instanceof TL_message_secret) || needDrawBluredPreview() || isLiveLocation() || this.type == 16) ? false : true;
    }

    public boolean canEditMedia() {
        boolean z = false;
        if (isSecretMedia()) {
            return false;
        }
        MessageMedia messageMedia = this.messageOwner.media;
        if (messageMedia instanceof TL_messageMediaPhoto) {
            return true;
        }
        if (!(!(messageMedia instanceof TL_messageMediaDocument) || isVoice() || isSticker() || isAnimatedSticker() || isRoundVideo())) {
            z = true;
        }
        return z;
    }

    public boolean canEditMessageAnytime(Chat chat) {
        return canEditMessageAnytime(this.currentAccount, this.messageOwner, chat);
    }

    public static boolean canEditMessageAnytime(int i, Message message, Chat chat) {
        if (!(message == null || message.to_id == null)) {
            MessageMedia messageMedia = message.media;
            if (messageMedia == null || !(isRoundVideoDocument(messageMedia.document) || isStickerDocument(message.media.document) || isAnimatedStickerDocument(message.media.document))) {
                MessageAction messageAction = message.action;
                if ((messageAction == null || (messageAction instanceof TL_messageActionEmpty)) && !isForwardedMessage(message) && message.via_bot_id == 0 && message.f461id >= 0) {
                    int i2 = message.from_id;
                    if (i2 == message.to_id.user_id && i2 == UserConfig.getInstance(i).getClientUserId() && !isLiveLocationMessage(message)) {
                        return true;
                    }
                    if (chat == null && message.to_id.channel_id != 0) {
                        chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Integer.valueOf(message.to_id.channel_id));
                        if (chat == null) {
                            return false;
                        }
                    }
                    if (message.out && chat != null && chat.megagroup) {
                        if (!chat.creator) {
                            TL_chatAdminRights tL_chatAdminRights = chat.admin_rights;
                            if (tL_chatAdminRights == null || !tL_chatAdminRights.pin_messages) {
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

    /* JADX WARNING: Missing block: B:6:0x000b, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:66:0x00b4, code skipped:
            if (r1.pin_messages != false) goto L_0x00b6;
     */
    /* JADX WARNING: Missing block: B:89:0x0105, code skipped:
            if (r4 != null) goto L_0x0108;
     */
    /* JADX WARNING: Missing block: B:105:0x0123, code skipped:
            if (r5.out != false) goto L_0x0125;
     */
    /* JADX WARNING: Missing block: B:107:0x0127, code skipped:
            if (r5.post != false) goto L_0x0129;
     */
    public static boolean canEditMessage(int r4, org.telegram.tgnet.TLRPC.Message r5, org.telegram.tgnet.TLRPC.Chat r6) {
        /*
        r0 = 0;
        if (r6 == 0) goto L_0x000c;
    L_0x0003:
        r1 = r6.left;
        if (r1 != 0) goto L_0x000b;
    L_0x0007:
        r1 = r6.kicked;
        if (r1 == 0) goto L_0x000c;
    L_0x000b:
        return r0;
    L_0x000c:
        if (r5 == 0) goto L_0x014c;
    L_0x000e:
        r1 = r5.to_id;
        if (r1 == 0) goto L_0x014c;
    L_0x0012:
        r1 = r5.media;
        if (r1 == 0) goto L_0x0038;
    L_0x0016:
        r1 = r1.document;
        r1 = isRoundVideoDocument(r1);
        if (r1 != 0) goto L_0x014c;
    L_0x001e:
        r1 = r5.media;
        r1 = r1.document;
        r1 = isStickerDocument(r1);
        if (r1 != 0) goto L_0x014c;
    L_0x0028:
        r1 = r5.media;
        r1 = r1.document;
        r1 = isAnimatedStickerDocument(r1);
        if (r1 != 0) goto L_0x014c;
    L_0x0032:
        r1 = isLocationMessage(r5);
        if (r1 != 0) goto L_0x014c;
    L_0x0038:
        r1 = r5.action;
        if (r1 == 0) goto L_0x0040;
    L_0x003c:
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageActionEmpty;
        if (r1 == 0) goto L_0x014c;
    L_0x0040:
        r1 = isForwardedMessage(r5);
        if (r1 != 0) goto L_0x014c;
    L_0x0046:
        r1 = r5.via_bot_id;
        if (r1 != 0) goto L_0x014c;
    L_0x004a:
        r1 = r5.f461id;
        if (r1 >= 0) goto L_0x0050;
    L_0x004e:
        goto L_0x014c;
    L_0x0050:
        r1 = r5.from_id;
        r2 = r5.to_id;
        r2 = r2.user_id;
        r3 = 1;
        if (r1 != r2) goto L_0x0070;
    L_0x0059:
        r2 = org.telegram.messenger.UserConfig.getInstance(r4);
        r2 = r2.getClientUserId();
        if (r1 != r2) goto L_0x0070;
    L_0x0063:
        r1 = isLiveLocationMessage(r5);
        if (r1 != 0) goto L_0x0070;
    L_0x0069:
        r1 = r5.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact;
        if (r1 != 0) goto L_0x0070;
    L_0x006f:
        return r3;
    L_0x0070:
        if (r6 != 0) goto L_0x008b;
    L_0x0072:
        r1 = r5.to_id;
        r1 = r1.channel_id;
        if (r1 == 0) goto L_0x008b;
    L_0x0078:
        r6 = org.telegram.messenger.MessagesController.getInstance(r4);
        r1 = r5.to_id;
        r1 = r1.channel_id;
        r1 = java.lang.Integer.valueOf(r1);
        r6 = r6.getChat(r1);
        if (r6 != 0) goto L_0x008b;
    L_0x008a:
        return r0;
    L_0x008b:
        r1 = r5.media;
        if (r1 == 0) goto L_0x00a0;
    L_0x008f:
        r2 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
        if (r2 != 0) goto L_0x00a0;
    L_0x0093:
        r2 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r2 != 0) goto L_0x00a0;
    L_0x0097:
        r2 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r2 != 0) goto L_0x00a0;
    L_0x009b:
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r1 != 0) goto L_0x00a0;
    L_0x009f:
        return r0;
    L_0x00a0:
        r1 = r5.out;
        if (r1 == 0) goto L_0x00b7;
    L_0x00a4:
        if (r6 == 0) goto L_0x00b7;
    L_0x00a6:
        r1 = r6.megagroup;
        if (r1 == 0) goto L_0x00b7;
    L_0x00aa:
        r1 = r6.creator;
        if (r1 != 0) goto L_0x00b6;
    L_0x00ae:
        r1 = r6.admin_rights;
        if (r1 == 0) goto L_0x00b7;
    L_0x00b2:
        r1 = r1.pin_messages;
        if (r1 == 0) goto L_0x00b7;
    L_0x00b6:
        return r3;
    L_0x00b7:
        r1 = r5.date;
        r2 = org.telegram.tgnet.ConnectionsManager.getInstance(r4);
        r2 = r2.getCurrentTime();
        r1 = r1 - r2;
        r1 = java.lang.Math.abs(r1);
        r2 = org.telegram.messenger.MessagesController.getInstance(r4);
        r2 = r2.maxEditTime;
        if (r1 <= r2) goto L_0x00cf;
    L_0x00ce:
        return r0;
    L_0x00cf:
        r1 = r5.to_id;
        r1 = r1.channel_id;
        if (r1 != 0) goto L_0x0109;
    L_0x00d5:
        r6 = r5.out;
        if (r6 != 0) goto L_0x00e5;
    L_0x00d9:
        r6 = r5.from_id;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.getClientUserId();
        if (r6 != r4) goto L_0x0108;
    L_0x00e5:
        r4 = r5.media;
        r6 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r6 != 0) goto L_0x0107;
    L_0x00eb:
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r4 == 0) goto L_0x00fb;
    L_0x00ef:
        r4 = isStickerMessage(r5);
        if (r4 != 0) goto L_0x00fb;
    L_0x00f5:
        r4 = isAnimatedStickerMessage(r5);
        if (r4 == 0) goto L_0x0107;
    L_0x00fb:
        r4 = r5.media;
        r5 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
        if (r5 != 0) goto L_0x0107;
    L_0x0101:
        r5 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r5 != 0) goto L_0x0107;
    L_0x0105:
        if (r4 != 0) goto L_0x0108;
    L_0x0107:
        r0 = 1;
    L_0x0108:
        return r0;
    L_0x0109:
        r4 = r6.megagroup;
        if (r4 == 0) goto L_0x0111;
    L_0x010d:
        r4 = r5.out;
        if (r4 != 0) goto L_0x0129;
    L_0x0111:
        r4 = r6.megagroup;
        if (r4 != 0) goto L_0x014c;
    L_0x0115:
        r4 = r6.creator;
        if (r4 != 0) goto L_0x0125;
    L_0x0119:
        r4 = r6.admin_rights;
        if (r4 == 0) goto L_0x014c;
    L_0x011d:
        r4 = r4.edit_messages;
        if (r4 != 0) goto L_0x0125;
    L_0x0121:
        r4 = r5.out;
        if (r4 == 0) goto L_0x014c;
    L_0x0125:
        r4 = r5.post;
        if (r4 == 0) goto L_0x014c;
    L_0x0129:
        r4 = r5.media;
        r6 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r6 != 0) goto L_0x014b;
    L_0x012f:
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r4 == 0) goto L_0x013f;
    L_0x0133:
        r4 = isStickerMessage(r5);
        if (r4 != 0) goto L_0x013f;
    L_0x0139:
        r4 = isAnimatedStickerMessage(r5);
        if (r4 == 0) goto L_0x014b;
    L_0x013f:
        r4 = r5.media;
        r5 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
        if (r5 != 0) goto L_0x014b;
    L_0x0145:
        r5 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r5 != 0) goto L_0x014b;
    L_0x0149:
        if (r4 != 0) goto L_0x014c;
    L_0x014b:
        return r3;
    L_0x014c:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.canEditMessage(int, org.telegram.tgnet.TLRPC$Message, org.telegram.tgnet.TLRPC$Chat):boolean");
    }

    public boolean canDeleteMessage(Chat chat) {
        return this.eventId == 0 && canDeleteMessage(this.currentAccount, this.messageOwner, chat);
    }

    /* JADX WARNING: Missing block: B:18:0x0037, code skipped:
            if (r3.out != false) goto L_0x0045;
     */
    /* JADX WARNING: Missing block: B:24:0x0043, code skipped:
            if (r3.from_id > 0) goto L_0x0045;
     */
    public static boolean canDeleteMessage(int r2, org.telegram.tgnet.TLRPC.Message r3, org.telegram.tgnet.TLRPC.Chat r4) {
        /*
        r0 = r3.f461id;
        r1 = 1;
        if (r0 >= 0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        if (r4 != 0) goto L_0x001e;
    L_0x0008:
        r0 = r3.to_id;
        r0 = r0.channel_id;
        if (r0 == 0) goto L_0x001e;
    L_0x000e:
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);
        r4 = r3.to_id;
        r4 = r4.channel_id;
        r4 = java.lang.Integer.valueOf(r4);
        r4 = r2.getChat(r4);
    L_0x001e:
        r2 = org.telegram.messenger.ChatObject.isChannel(r4);
        r0 = 0;
        if (r2 == 0) goto L_0x0047;
    L_0x0025:
        r2 = r3.f461id;
        if (r2 == r1) goto L_0x0046;
    L_0x0029:
        r2 = r4.creator;
        if (r2 != 0) goto L_0x0045;
    L_0x002d:
        r2 = r4.admin_rights;
        if (r2 == 0) goto L_0x0039;
    L_0x0031:
        r2 = r2.delete_messages;
        if (r2 != 0) goto L_0x0045;
    L_0x0035:
        r2 = r3.out;
        if (r2 != 0) goto L_0x0045;
    L_0x0039:
        r2 = r4.megagroup;
        if (r2 == 0) goto L_0x0046;
    L_0x003d:
        r2 = r3.out;
        if (r2 == 0) goto L_0x0046;
    L_0x0041:
        r2 = r3.from_id;
        if (r2 <= 0) goto L_0x0046;
    L_0x0045:
        r0 = 1;
    L_0x0046:
        return r0;
    L_0x0047:
        r2 = isOut(r3);
        if (r2 != 0) goto L_0x0053;
    L_0x004d:
        r2 = org.telegram.messenger.ChatObject.isChannel(r4);
        if (r2 != 0) goto L_0x0054;
    L_0x0053:
        r0 = 1;
    L_0x0054:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.canDeleteMessage(int, org.telegram.tgnet.TLRPC$Message, org.telegram.tgnet.TLRPC$Chat):boolean");
    }

    public String getForwardedName() {
        MessageFwdHeader messageFwdHeader = this.messageOwner.fwd_from;
        if (messageFwdHeader != null) {
            if (messageFwdHeader.channel_id != 0) {
                Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.messageOwner.fwd_from.channel_id));
                if (chat != null) {
                    return chat.title;
                }
            } else if (messageFwdHeader.from_id != 0) {
                User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.messageOwner.fwd_from.from_id));
                if (user != null) {
                    return UserObject.getUserName(user);
                }
            } else {
                String str = messageFwdHeader.from_name;
                if (str != null) {
                    return str;
                }
            }
        }
        return null;
    }

    public int getFromId() {
        int i;
        MessageFwdHeader messageFwdHeader = this.messageOwner.fwd_from;
        if (messageFwdHeader != null) {
            Peer peer = messageFwdHeader.saved_from_peer;
            if (peer != null) {
                int i2 = peer.user_id;
                int i3;
                if (i2 != 0) {
                    i3 = messageFwdHeader.from_id;
                    return i3 != 0 ? i3 : i2;
                } else if (peer.channel_id != 0) {
                    if (isSavedFromMegagroup()) {
                        i3 = this.messageOwner.fwd_from.from_id;
                        if (i3 != 0) {
                            return i3;
                        }
                    }
                    messageFwdHeader = this.messageOwner.fwd_from;
                    i = messageFwdHeader.channel_id;
                    if (i != 0) {
                        return -i;
                    }
                    return -messageFwdHeader.saved_from_peer.channel_id;
                } else {
                    i = peer.chat_id;
                    if (i != 0) {
                        i2 = messageFwdHeader.from_id;
                        if (i2 != 0) {
                            return i2;
                        }
                        i3 = messageFwdHeader.channel_id;
                        return i3 != 0 ? -i3 : -i;
                    }
                    return 0;
                }
            }
        }
        Message message = this.messageOwner;
        i = message.from_id;
        if (i != 0) {
            return i;
        }
        if (message.post) {
            return message.to_id.channel_id;
        }
        return 0;
    }

    public boolean isWallpaper() {
        MessageMedia messageMedia = this.messageOwner.media;
        if (messageMedia instanceof TL_messageMediaWebPage) {
            WebPage webPage = messageMedia.webpage;
            if (webPage != null) {
                if ("telegram_background".equals(webPage.type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getMediaExistanceFlags() {
        int i = this.attachPathExists ? 1 : 0;
        return this.mediaExists ? i | 2 : i;
    }

    public void applyMediaExistanceFlags(int i) {
        if (i == -1) {
            checkMediaExistance();
            return;
        }
        boolean z = false;
        this.attachPathExists = (i & 1) != 0;
        if ((i & 2) != 0) {
            z = true;
        }
        this.mediaExists = z;
    }

    public void checkMediaExistance() {
        this.attachPathExists = false;
        this.mediaExists = false;
        int i = this.type;
        String str = ".enc";
        File pathToMessage;
        StringBuilder stringBuilder;
        if (i == 1) {
            if (FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize()) != null) {
                pathToMessage = FileLoader.getPathToMessage(this.messageOwner);
                if (needDrawBluredPreview()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(pathToMessage.getAbsolutePath());
                    stringBuilder.append(str);
                    this.mediaExists = new File(stringBuilder.toString()).exists();
                }
                if (!this.mediaExists) {
                    this.mediaExists = pathToMessage.exists();
                }
            }
        } else if (i == 8 || i == 3 || i == 9 || i == 2 || i == 14 || i == 5) {
            String str2 = this.messageOwner.attachPath;
            if (str2 != null && str2.length() > 0) {
                this.attachPathExists = new File(this.messageOwner.attachPath).exists();
            }
            if (!this.attachPathExists) {
                pathToMessage = FileLoader.getPathToMessage(this.messageOwner);
                if (this.type == 3 && needDrawBluredPreview()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(pathToMessage.getAbsolutePath());
                    stringBuilder.append(str);
                    this.mediaExists = new File(stringBuilder.toString()).exists();
                }
                if (!this.mediaExists) {
                    this.mediaExists = pathToMessage.exists();
                }
            }
        } else {
            Document document = getDocument();
            if (document != null) {
                if (isWallpaper()) {
                    this.mediaExists = FileLoader.getPathToAttach(document, true).exists();
                } else {
                    this.mediaExists = FileLoader.getPathToAttach(document).exists();
                }
            } else if (this.type == 0) {
                PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize());
                if (!(closestPhotoSizeWithSize == null || closestPhotoSizeWithSize == null)) {
                    this.mediaExists = FileLoader.getPathToAttach(closestPhotoSizeWithSize, true).exists();
                }
            }
        }
    }

    public boolean equals(MessageObject messageObject) {
        return getId() == messageObject.getId() && getDialogId() == messageObject.getDialogId();
    }
}