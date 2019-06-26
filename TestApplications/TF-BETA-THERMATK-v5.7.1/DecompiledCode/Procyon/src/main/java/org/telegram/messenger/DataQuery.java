// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.ui.ActionBar.AlertDialog;
import android.os.Parcelable;
import android.content.pm.ShortcutManager;
import android.text.SpannedString;
import android.text.SpannableStringBuilder;
import android.app.Activity;
import android.app.Dialog;
import org.telegram.ui.Components.StickersArchiveAlert;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;
import org.telegram.ui.Components.TypefaceSpan;
import android.text.Spanned;
import java.util.concurrent.CountDownLatch;
import android.content.SharedPreferences$Editor;
import android.os.Build$VERSION;
import android.widget.Toast;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.SQLite.SQLiteCursor;
import android.text.TextUtils;
import java.util.Locale;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import android.content.Intent;
import java.util.Iterator;
import android.content.Context;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.SerializedData;
import java.util.Map;
import android.content.SharedPreferences;
import org.telegram.messenger.support.SparseLongArray;
import android.util.LongSparseArray;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.HashMap;
import android.graphics.Path;
import android.graphics.Paint;
import org.telegram.tgnet.TLRPC;
import java.util.Comparator;
import android.graphics.RectF;

public class DataQuery
{
    private static volatile DataQuery[] Instance;
    public static final int MEDIA_AUDIO = 2;
    public static final int MEDIA_FILE = 1;
    public static final int MEDIA_MUSIC = 4;
    public static final int MEDIA_PHOTOVIDEO = 0;
    public static final int MEDIA_TYPES_COUNT = 5;
    public static final int MEDIA_URL = 3;
    public static final int TYPE_FAVE = 2;
    public static final int TYPE_FEATURED = 3;
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_MASK = 1;
    private static RectF bitmapRect;
    private static Comparator<TLRPC.MessageEntity> entityComparator;
    private static Paint erasePaint;
    private static Paint roundPaint;
    private static Path roundPath;
    private HashMap<String, ArrayList<TLRPC.Document>> allStickers;
    private HashMap<String, ArrayList<TLRPC.Document>> allStickersFeatured;
    private int[] archivedStickersCount;
    private SparseArray<TLRPC.BotInfo> botInfos;
    private LongSparseArray<TLRPC.Message> botKeyboards;
    private SparseLongArray botKeyboardsByMids;
    private int currentAccount;
    private HashMap<String, Boolean> currentFetchingEmoji;
    private LongSparseArray<TLRPC.Message> draftMessages;
    private LongSparseArray<TLRPC.DraftMessage> drafts;
    private ArrayList<TLRPC.StickerSetCovered> featuredStickerSets;
    private LongSparseArray<TLRPC.StickerSetCovered> featuredStickerSetsById;
    private boolean featuredStickersLoaded;
    private LongSparseArray<TLRPC.TL_messages_stickerSet> groupStickerSets;
    public ArrayList<TLRPC.TL_topPeer> hints;
    private boolean inTransaction;
    public ArrayList<TLRPC.TL_topPeer> inlineBots;
    private LongSparseArray<TLRPC.TL_messages_stickerSet> installedStickerSetsById;
    private long lastMergeDialogId;
    private int lastReqId;
    private int lastReturnedNum;
    private String lastSearchQuery;
    private int[] loadDate;
    private int loadFeaturedDate;
    private int loadFeaturedHash;
    private int[] loadHash;
    boolean loaded;
    boolean loading;
    private boolean loadingDrafts;
    private boolean loadingFeaturedStickers;
    private boolean loadingRecentGifs;
    private boolean[] loadingRecentStickers;
    private boolean[] loadingStickers;
    private int mergeReqId;
    private int[] messagesSearchCount;
    private boolean[] messagesSearchEndReached;
    private SharedPreferences preferences;
    private ArrayList<Long> readingStickerSets;
    private ArrayList<TLRPC.Document> recentGifs;
    private boolean recentGifsLoaded;
    private ArrayList<TLRPC.Document>[] recentStickers;
    private boolean[] recentStickersLoaded;
    private int reqId;
    private ArrayList<MessageObject> searchResultMessages;
    private SparseArray<MessageObject>[] searchResultMessagesMap;
    private ArrayList<TLRPC.TL_messages_stickerSet>[] stickerSets;
    private LongSparseArray<TLRPC.TL_messages_stickerSet> stickerSetsById;
    private HashMap<String, TLRPC.TL_messages_stickerSet> stickerSetsByName;
    private LongSparseArray<String> stickersByEmoji;
    private boolean[] stickersLoaded;
    private ArrayList<Long> unreadStickerSets;
    
    static {
        DataQuery.Instance = new DataQuery[3];
        DataQuery.entityComparator = (Comparator<TLRPC.MessageEntity>)_$$Lambda$DataQuery$_GtBS_Mb74mqs3D5Wip5N2Gb424.INSTANCE;
    }
    
    public DataQuery(final int currentAccount) {
        this.stickerSets = (ArrayList<TLRPC.TL_messages_stickerSet>[])new ArrayList[] { new ArrayList(), new ArrayList(), new ArrayList(0), new ArrayList() };
        this.stickerSetsById = (LongSparseArray<TLRPC.TL_messages_stickerSet>)new LongSparseArray();
        this.installedStickerSetsById = (LongSparseArray<TLRPC.TL_messages_stickerSet>)new LongSparseArray();
        this.groupStickerSets = (LongSparseArray<TLRPC.TL_messages_stickerSet>)new LongSparseArray();
        this.stickerSetsByName = new HashMap<String, TLRPC.TL_messages_stickerSet>();
        this.loadingStickers = new boolean[4];
        this.stickersLoaded = new boolean[4];
        this.loadHash = new int[4];
        this.loadDate = new int[4];
        this.archivedStickersCount = new int[2];
        this.stickersByEmoji = (LongSparseArray<String>)new LongSparseArray();
        this.allStickers = new HashMap<String, ArrayList<TLRPC.Document>>();
        this.allStickersFeatured = new HashMap<String, ArrayList<TLRPC.Document>>();
        this.recentStickers = (ArrayList<TLRPC.Document>[])new ArrayList[] { new ArrayList(), new ArrayList(), new ArrayList() };
        this.loadingRecentStickers = new boolean[3];
        this.recentStickersLoaded = new boolean[3];
        this.recentGifs = new ArrayList<TLRPC.Document>();
        this.featuredStickerSets = new ArrayList<TLRPC.StickerSetCovered>();
        this.featuredStickerSetsById = (LongSparseArray<TLRPC.StickerSetCovered>)new LongSparseArray();
        this.unreadStickerSets = new ArrayList<Long>();
        this.readingStickerSets = new ArrayList<Long>();
        this.messagesSearchCount = new int[] { 0, 0 };
        this.messagesSearchEndReached = new boolean[] { false, false };
        this.searchResultMessages = new ArrayList<MessageObject>();
        this.searchResultMessagesMap = (SparseArray<MessageObject>[])new SparseArray[] { new SparseArray(), new SparseArray() };
        this.hints = new ArrayList<TLRPC.TL_topPeer>();
        this.inlineBots = new ArrayList<TLRPC.TL_topPeer>();
        this.drafts = (LongSparseArray<TLRPC.DraftMessage>)new LongSparseArray();
        this.draftMessages = (LongSparseArray<TLRPC.Message>)new LongSparseArray();
        this.botInfos = (SparseArray<TLRPC.BotInfo>)new SparseArray();
        this.botKeyboards = (LongSparseArray<TLRPC.Message>)new LongSparseArray();
        this.botKeyboardsByMids = new SparseLongArray();
        this.currentFetchingEmoji = new HashMap<String, Boolean>();
        this.currentAccount = currentAccount;
        if (this.currentAccount == 0) {
            this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("drafts", 0);
        }
        else {
            final Context applicationContext = ApplicationLoader.applicationContext;
            final StringBuilder sb = new StringBuilder();
            sb.append("drafts");
            sb.append(this.currentAccount);
            this.preferences = applicationContext.getSharedPreferences(sb.toString(), 0);
        }
        final Iterator<Map.Entry<String, V>> iterator = this.preferences.getAll().entrySet().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                return;
            }
            final Map.Entry<String, V> entry = iterator.next();
            try {
                final String s = entry.getKey();
                final long longValue = Utilities.parseLong(s);
                final SerializedData serializedData = new SerializedData(Utilities.hexToBytes((String)entry.getValue()));
                if (s.startsWith("r_")) {
                    final TLRPC.Message tLdeserialize = TLRPC.Message.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                    tLdeserialize.readAttachPath(serializedData, UserConfig.getInstance(this.currentAccount).clientUserId);
                    if (tLdeserialize != null) {
                        this.draftMessages.put(longValue, (Object)tLdeserialize);
                    }
                }
                else {
                    final TLRPC.DraftMessage tLdeserialize2 = TLRPC.DraftMessage.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                    if (tLdeserialize2 != null) {
                        this.drafts.put(longValue, (Object)tLdeserialize2);
                    }
                }
                serializedData.cleanup();
                continue;
            }
            catch (Exception ex) {}
        }
    }
    
    private MessageObject broadcastPinnedMessage(final TLRPC.Message message, final ArrayList<TLRPC.User> list, final ArrayList<TLRPC.Chat> list2, final boolean b, final boolean b2) {
        final SparseArray sparseArray = new SparseArray();
        final int n = 0;
        for (int i = 0; i < list.size(); ++i) {
            final TLRPC.User user = list.get(i);
            sparseArray.put(user.id, (Object)user);
        }
        final SparseArray sparseArray2 = new SparseArray();
        for (int j = n; j < list2.size(); ++j) {
            final TLRPC.Chat chat = list2.get(j);
            sparseArray2.put(chat.id, (Object)chat);
        }
        if (b2) {
            return new MessageObject(this.currentAccount, message, (SparseArray<TLRPC.User>)sparseArray, (SparseArray<TLRPC.Chat>)sparseArray2, false);
        }
        AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$Yx2wTX87h9OMWDNW8TJbWVjbdlk(this, list, b, list2, message, sparseArray, sparseArray2));
        return null;
    }
    
    private void broadcastReplyMessages(final ArrayList<TLRPC.Message> list, final SparseArray<ArrayList<MessageObject>> sparseArray, final ArrayList<TLRPC.User> list2, final ArrayList<TLRPC.Chat> list3, final long n, final boolean b) {
        final SparseArray sparseArray2 = new SparseArray();
        final int n2 = 0;
        for (int i = 0; i < list2.size(); ++i) {
            final TLRPC.User user = list2.get(i);
            sparseArray2.put(user.id, (Object)user);
        }
        final SparseArray sparseArray3 = new SparseArray();
        for (int j = n2; j < list3.size(); ++j) {
            final TLRPC.Chat chat = list3.get(j);
            sparseArray3.put(chat.id, (Object)chat);
        }
        AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$j3QDbBWi358gz3ac8yzLPqCuKcU(this, list2, b, list3, list, sparseArray, sparseArray2, sparseArray3, n));
    }
    
    private static int calcDocumentsHash(final ArrayList<TLRPC.Document> list) {
        int i = 0;
        if (list == null) {
            return 0;
        }
        long n = 0L;
        while (i < Math.min(200, list.size())) {
            final TLRPC.Document document = list.get(i);
            if (document != null) {
                final long id = document.id;
                n = ((n * 20261L + 2147483648L + (int)(id >> 32)) % 2147483648L * 20261L + 2147483648L + (int)id) % 2147483648L;
            }
            ++i;
        }
        return (int)n;
    }
    
    private int calcFeaturedStickersHash(final ArrayList<TLRPC.StickerSetCovered> list) {
        long n = 0L;
        for (int i = 0; i < list.size(); ++i) {
            final TLRPC.StickerSet set = list.get(i).set;
            if (!set.archived) {
                final long id = set.id;
                final long n2 = n = ((n * 20261L + 2147483648L + (int)(id >> 32)) % 2147483648L * 20261L + 2147483648L + (int)id) % 2147483648L;
                if (this.unreadStickerSets.contains(id)) {
                    n = (n2 * 20261L + 2147483648L + 1L) % 2147483648L;
                }
            }
        }
        return (int)n;
    }
    
    private static int calcStickersHash(final ArrayList<TLRPC.TL_messages_stickerSet> list) {
        long n = 0L;
        for (int i = 0; i < list.size(); ++i) {
            final TLRPC.StickerSet set = list.get(i).set;
            if (!set.archived) {
                n = (n * 20261L + 2147483648L + set.hash) % 2147483648L;
            }
        }
        return (int)n;
    }
    
    public static boolean canAddMessageToMedia(final TLRPC.Message message) {
        final boolean b = message instanceof TLRPC.TL_message_secret;
        if (b && (message.media instanceof TLRPC.TL_messageMediaPhoto || MessageObject.isVideoMessage(message) || MessageObject.isGifMessage(message))) {
            final int ttl_seconds = message.media.ttl_seconds;
            if (ttl_seconds != 0 && ttl_seconds <= 60) {
                return false;
            }
        }
        if (!b && message instanceof TLRPC.TL_message) {
            final TLRPC.MessageMedia media = message.media;
            if ((media instanceof TLRPC.TL_messageMediaPhoto || media instanceof TLRPC.TL_messageMediaDocument) && message.media.ttl_seconds != 0) {
                return false;
            }
        }
        final TLRPC.MessageMedia media2 = message.media;
        if (!(media2 instanceof TLRPC.TL_messageMediaPhoto) && (!(media2 instanceof TLRPC.TL_messageMediaDocument) || MessageObject.isGifDocument(media2.document))) {
            if (!message.entities.isEmpty()) {
                for (int i = 0; i < message.entities.size(); ++i) {
                    final TLRPC.MessageEntity messageEntity = message.entities.get(i);
                    if (messageEntity instanceof TLRPC.TL_messageEntityUrl || messageEntity instanceof TLRPC.TL_messageEntityTextUrl || messageEntity instanceof TLRPC.TL_messageEntityEmail) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }
    
    private static boolean checkInclusion(final int n, final ArrayList<TLRPC.MessageEntity> list) {
        if (list != null) {
            if (!list.isEmpty()) {
                for (int size = list.size(), i = 0; i < size; ++i) {
                    final TLRPC.MessageEntity messageEntity = list.get(i);
                    final int offset = messageEntity.offset;
                    if (offset <= n && offset + messageEntity.length > n) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private static boolean checkIntersection(final int n, final int n2, final ArrayList<TLRPC.MessageEntity> list) {
        if (list != null) {
            if (!list.isEmpty()) {
                for (int size = list.size(), i = 0; i < size; ++i) {
                    final TLRPC.MessageEntity messageEntity = list.get(i);
                    final int offset = messageEntity.offset;
                    if (offset > n && offset + messageEntity.length <= n2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private Intent createIntrnalShortcutIntent(final long lng) {
        final Intent intent = new Intent(ApplicationLoader.applicationContext, (Class)OpenChatReceiver.class);
        final int n = (int)lng;
        final int i = (int)(lng >> 32);
        if (n == 0) {
            intent.putExtra("encId", i);
            if (MessagesController.getInstance(this.currentAccount).getEncryptedChat(i) == null) {
                return null;
            }
        }
        else if (n > 0) {
            intent.putExtra("userId", n);
        }
        else {
            if (n >= 0) {
                return null;
            }
            intent.putExtra("chatId", -n);
        }
        intent.putExtra("currentAccount", this.currentAccount);
        final StringBuilder sb = new StringBuilder();
        sb.append("com.tmessages.openchat");
        sb.append(lng);
        intent.setAction(sb.toString());
        intent.addFlags(67108864);
        return intent;
    }
    
    private void deletePeer(final int n, final int n2) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$zfORkxHX1nBnvnpPSEIl0qTrWEM(this, n, n2));
    }
    
    public static TLRPC.InputStickerSet getInputStickerSet(final TLRPC.Document document) {
        int i = 0;
        while (i < document.attributes.size()) {
            final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                final TLRPC.InputStickerSet stickerset = documentAttribute.stickerset;
                if (stickerset instanceof TLRPC.TL_inputStickerSetEmpty) {
                    return null;
                }
                return stickerset;
            }
            else {
                ++i;
            }
        }
        return null;
    }
    
    public static DataQuery getInstance(final int n) {
        final DataQuery dataQuery;
        if ((dataQuery = DataQuery.Instance[n]) == null) {
            synchronized (DataQuery.class) {
                if (DataQuery.Instance[n] == null) {
                    DataQuery.Instance[n] = new DataQuery(n);
                }
            }
        }
        return dataQuery;
    }
    
    private int getMask() {
        final int lastReturnedNum = this.lastReturnedNum;
        final int size = this.searchResultMessages.size();
        int n2;
        final int n = n2 = 1;
        if (lastReturnedNum >= size - 1) {
            final boolean[] messagesSearchEndReached = this.messagesSearchEndReached;
            n2 = n;
            if (messagesSearchEndReached[0]) {
                if (!messagesSearchEndReached[1]) {
                    n2 = n;
                }
                else {
                    n2 = 0;
                }
            }
        }
        int n3 = n2;
        if (this.lastReturnedNum > 0) {
            n3 = (n2 | 0x2);
        }
        return n3;
    }
    
    private void getMediaCountDatabase(final long n, final int n2, final int n3) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$r7IDcaPS4__Lq9u_Bcjhps7n1wc(this, n, n2, n3));
    }
    
    public static int getMediaType(final TLRPC.Message message) {
        if (message == null) {
            return -1;
        }
        final TLRPC.MessageMedia media = message.media;
        final boolean b = media instanceof TLRPC.TL_messageMediaPhoto;
        int i = 0;
        if (b) {
            return 0;
        }
        if (!(media instanceof TLRPC.TL_messageMediaDocument)) {
            if (!message.entities.isEmpty()) {
                while (i < message.entities.size()) {
                    final TLRPC.MessageEntity messageEntity = message.entities.get(i);
                    if (messageEntity instanceof TLRPC.TL_messageEntityUrl || messageEntity instanceof TLRPC.TL_messageEntityTextUrl || messageEntity instanceof TLRPC.TL_messageEntityEmail) {
                        return 3;
                    }
                    ++i;
                }
            }
            return -1;
        }
        if (MessageObject.isVoiceMessage(message) || MessageObject.isRoundVideoMessage(message)) {
            return 2;
        }
        if (MessageObject.isVideoMessage(message)) {
            return 0;
        }
        if (MessageObject.isStickerMessage(message)) {
            return -1;
        }
        if (MessageObject.isMusicMessage(message)) {
            return 4;
        }
        return 1;
    }
    
    public static long getStickerSetId(final TLRPC.Document document) {
        int i = 0;
        while (i < document.attributes.size()) {
            final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                final TLRPC.InputStickerSet stickerset = documentAttribute.stickerset;
                if (stickerset instanceof TLRPC.TL_inputStickerSetID) {
                    return stickerset.id;
                }
                break;
            }
            else {
                ++i;
            }
        }
        return -1L;
    }
    
    private void loadGroupStickerSet(final TLRPC.StickerSet set, final boolean b) {
        if (b) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$_TcC0mAoIoAzht6PKxrSMA0GNwQ(this, set));
        }
        else {
            final TLRPC.TL_messages_getStickerSet set2 = new TLRPC.TL_messages_getStickerSet();
            set2.stickerset = new TLRPC.TL_inputStickerSetID();
            final TLRPC.InputStickerSet stickerset = set2.stickerset;
            stickerset.id = set.id;
            stickerset.access_hash = set.access_hash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(set2, new _$$Lambda$DataQuery$sSt__gYzisWLp4pAgk5FJ0UZgFM(this));
        }
    }
    
    private void loadMediaDatabase(final long n, final int n2, final int n3, final int n4, final int n5, final boolean b, final int n6) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$OqcREWH3JA8hAPi_XM3PyDyEsWM(this, n2, n, n3, b, n4, n6, n5));
    }
    
    private MessageObject loadPinnedMessageInternal(final long dialog_id, final int n, final int n2, final boolean b) {
        long l;
        if (n != 0) {
            l = ((long)n2 | (long)n << 32);
        }
        else {
            l = n2;
        }
        try {
            final ArrayList<TLRPC.User> list = new ArrayList<TLRPC.User>();
            final ArrayList<TLRPC.Chat> list2 = new ArrayList<TLRPC.Chat>();
            final ArrayList list3 = new ArrayList<Integer>();
            final ArrayList list4 = new ArrayList<Integer>();
            final SQLiteCursor queryFinalized = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid, date FROM messages WHERE mid = %d", l), new Object[0]);
            TLObject tLdeserialize = null;
            Label_0217: {
                if (queryFinalized.next()) {
                    final NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(0);
                    if (byteBufferValue != null) {
                        tLdeserialize = TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                        ((TLRPC.Message)tLdeserialize).readAttachPath(byteBufferValue, UserConfig.getInstance(this.currentAccount).clientUserId);
                        byteBufferValue.reuse();
                        if (!(((TLRPC.Message)tLdeserialize).action instanceof TLRPC.TL_messageActionHistoryClear)) {
                            ((TLRPC.Message)tLdeserialize).id = queryFinalized.intValue(1);
                            ((TLRPC.Message)tLdeserialize).date = queryFinalized.intValue(2);
                            ((TLRPC.Message)tLdeserialize).dialog_id = dialog_id;
                            MessagesStorage.addUsersAndChatsFromMessage((TLRPC.Message)tLdeserialize, list3, list4);
                            break Label_0217;
                        }
                    }
                }
                tLdeserialize = null;
            }
            queryFinalized.dispose();
            TLObject tLdeserialize2 = tLdeserialize;
            if (tLdeserialize == null) {
                final SQLiteCursor queryFinalized2 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data FROM chat_pinned WHERE uid = %d", dialog_id), new Object[0]);
                tLdeserialize2 = tLdeserialize;
                if (queryFinalized2.next()) {
                    final NativeByteBuffer byteBufferValue2 = queryFinalized2.byteBufferValue(0);
                    tLdeserialize2 = tLdeserialize;
                    if (byteBufferValue2 != null) {
                        tLdeserialize2 = TLRPC.Message.TLdeserialize(byteBufferValue2, byteBufferValue2.readInt32(false), false);
                        ((TLRPC.Message)tLdeserialize2).readAttachPath(byteBufferValue2, UserConfig.getInstance(this.currentAccount).clientUserId);
                        byteBufferValue2.reuse();
                        if (((TLRPC.Message)tLdeserialize2).id == n2 && !(((TLRPC.Message)tLdeserialize2).action instanceof TLRPC.TL_messageActionHistoryClear)) {
                            ((TLRPC.Message)tLdeserialize2).dialog_id = dialog_id;
                            MessagesStorage.addUsersAndChatsFromMessage((TLRPC.Message)tLdeserialize2, list3, list4);
                        }
                        else {
                            tLdeserialize2 = null;
                        }
                    }
                }
                queryFinalized2.dispose();
            }
            if (tLdeserialize2 == null) {
                if (n != 0) {
                    final TLRPC.TL_channels_getMessages tl_channels_getMessages = new TLRPC.TL_channels_getMessages();
                    tl_channels_getMessages.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(n);
                    tl_channels_getMessages.id.add(n2);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_getMessages, new _$$Lambda$DataQuery$sut4gZqSHSa63dx_2bNsxpC35Sk(this, n));
                }
                else {
                    final TLRPC.TL_messages_getMessages tl_messages_getMessages = new TLRPC.TL_messages_getMessages();
                    tl_messages_getMessages.id.add(n2);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getMessages, new _$$Lambda$DataQuery$bn_59i2M3GKJW8EWB0ORPpDQN_w(this, n));
                }
            }
            else {
                if (b) {
                    return this.broadcastPinnedMessage((TLRPC.Message)tLdeserialize2, list, list2, true, b);
                }
                if (!list3.isEmpty()) {
                    MessagesStorage.getInstance(this.currentAccount).getUsersInternal(TextUtils.join((CharSequence)",", (Iterable)list3), list);
                }
                if (!list4.isEmpty()) {
                    MessagesStorage.getInstance(this.currentAccount).getChatsInternal(TextUtils.join((CharSequence)",", (Iterable)list4), list2);
                }
                this.broadcastPinnedMessage((TLRPC.Message)tLdeserialize2, list, list2, true, false);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return null;
    }
    
    private void processLoadStickersResponse(final int n, final TLRPC.TL_messages_allStickers tl_messages_allStickers) {
        final ArrayList<TLRPC.TL_messages_stickerSet> list = new ArrayList<TLRPC.TL_messages_stickerSet>();
        if (tl_messages_allStickers.sets.isEmpty()) {
            this.processLoadedStickers(n, list, false, (int)(System.currentTimeMillis() / 1000L), tl_messages_allStickers.hash);
        }
        else {
            final LongSparseArray longSparseArray = new LongSparseArray();
            for (int i = 0; i < tl_messages_allStickers.sets.size(); ++i) {
                final TLRPC.StickerSet set = tl_messages_allStickers.sets.get(i);
                final TLRPC.TL_messages_stickerSet e = (TLRPC.TL_messages_stickerSet)this.stickerSetsById.get(set.id);
                if (e != null) {
                    final TLRPC.StickerSet set2 = e.set;
                    if (set2.hash == set.hash) {
                        set2.archived = set.archived;
                        set2.installed = set.installed;
                        set2.official = set.official;
                        longSparseArray.put(set2.id, (Object)e);
                        list.add(e);
                        if (longSparseArray.size() == tl_messages_allStickers.sets.size()) {
                            this.processLoadedStickers(n, list, false, (int)(System.currentTimeMillis() / 1000L), tl_messages_allStickers.hash);
                        }
                        continue;
                    }
                }
                list.add(null);
                final TLRPC.TL_messages_getStickerSet set3 = new TLRPC.TL_messages_getStickerSet();
                set3.stickerset = new TLRPC.TL_inputStickerSetID();
                final TLRPC.InputStickerSet stickerset = set3.stickerset;
                stickerset.id = set.id;
                stickerset.access_hash = set.access_hash;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(set3, new _$$Lambda$DataQuery$w1XixRjF16wrwBdg1yg9ZISWPKs(this, list, i, longSparseArray, set, tl_messages_allStickers, n));
            }
        }
    }
    
    private void processLoadedFeaturedStickers(final ArrayList<TLRPC.StickerSetCovered> list, final ArrayList<Long> list2, final boolean b, final int n, final int n2) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$Br07Yue1FWlAHhubZJ8mcHmpI1c(this));
        Utilities.stageQueue.postRunnable(new _$$Lambda$DataQuery$cOH_T13u95HL6anewGDnniRkdeE(this, b, list, n, n2, list2));
    }
    
    private void processLoadedMedia(final TLRPC.messages_Messages messages_Messages, final long n, int i, int n2, final int n3, final int n4, final int n5, final boolean b, final boolean b2) {
        final int n6 = (int)n;
        if (n4 != 0 && messages_Messages.messages.isEmpty() && n6 != 0) {
            if (n4 == 2) {
                return;
            }
            this.loadMedia(n, i, n2, n3, 0, n5);
        }
        else {
            if (n4 == 0) {
                ImageLoader.saveMessagesThumbs(messages_Messages.messages);
                MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(messages_Messages.users, messages_Messages.chats, true, true);
                this.putMediaDatabase(n, n3, messages_Messages.messages, n2, b2);
            }
            final SparseArray sparseArray = new SparseArray();
            n2 = 0;
            TLRPC.User user;
            for (i = 0; i < messages_Messages.users.size(); ++i) {
                user = messages_Messages.users.get(i);
                sparseArray.put(user.id, (Object)user);
            }
            final ArrayList<MessageObject> list = new ArrayList<MessageObject>();
            for (i = n2; i < messages_Messages.messages.size(); ++i) {
                list.add(new MessageObject(this.currentAccount, messages_Messages.messages.get(i), (SparseArray<TLRPC.User>)sparseArray, true));
            }
            AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$0dlxm8i_c0v_chVqHF7I_je7M4g(this, messages_Messages, n4, n, list, n5, n3, b2));
        }
    }
    
    private void processLoadedMediaCount(final int n, final long n2, final int n3, final int n4, final boolean b, final int n5) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$GYaSd5P0GrMZfcQPvBwtuZFrwqw(this, n2, b, n, n3, n5, n4));
    }
    
    private void processLoadedStickers(final int n, final ArrayList<TLRPC.TL_messages_stickerSet> list, final boolean b, final int n2, final int n3) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$9vMJ1w_Dj5oCt4zZwXAybfYG_nU(this, n));
        Utilities.stageQueue.postRunnable(new _$$Lambda$DataQuery$inBvXSiWQIx1Y9BuZIwlL6K4fAA(this, b, list, n2, n3, n));
    }
    
    private void putEmojiKeywords(final String s, final TLRPC.TL_emojiKeywordsDifference tl_emojiKeywordsDifference) {
        if (tl_emojiKeywordsDifference == null) {
            return;
        }
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$_y89sXvt9qXhyQF6eZF_c0a7Hdc(this, tl_emojiKeywordsDifference, s));
    }
    
    private void putFeaturedStickersToCache(final ArrayList<TLRPC.StickerSetCovered> c, final ArrayList<Long> list, final int n, final int n2) {
        ArrayList list2;
        if (c != null) {
            list2 = new ArrayList((Collection<? extends E>)c);
        }
        else {
            list2 = null;
        }
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$EvRKZ0icyHpXu5syph8WWuRUigE(this, list2, list, n, n2));
    }
    
    private void putMediaCountDatabase(final long n, final int n2, final int n3) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$VPyo4uiijc6Mw5vRqsga_xtHpO4(this, n, n2, n3));
    }
    
    private void putMediaDatabase(final long n, final int n2, final ArrayList<TLRPC.Message> list, final int n3, final boolean b) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$VgKCIvejIuaPm3YY59TrIO7FIyE(this, list, b, n, n3, n2));
    }
    
    private void putSetToCache(final TLRPC.TL_messages_stickerSet set) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$Gs_0kL3OC_eV_RiFuvATRDxcFsE(this, set));
    }
    
    private void putStickersToCache(final int n, final ArrayList<TLRPC.TL_messages_stickerSet> c, final int n2, final int n3) {
        ArrayList list;
        if (c != null) {
            list = new ArrayList((Collection<? extends E>)c);
        }
        else {
            list = null;
        }
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$HuXXMuf2NeuYCbcUMY_TfU_FwGA(this, list, n, n2, n3));
    }
    
    private static void removeEmptyMessages(final ArrayList<TLRPC.Message> list) {
        int n;
        for (int i = 0; i < list.size(); i = n + 1) {
            final TLRPC.Message message = list.get(i);
            if (message != null && !(message instanceof TLRPC.TL_messageEmpty)) {
                n = i;
                if (!(message.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                    continue;
                }
            }
            list.remove(i);
            n = i - 1;
        }
    }
    
    private static void removeOffsetAfter(final int n, final int n2, final ArrayList<TLRPC.MessageEntity> list) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            final TLRPC.MessageEntity messageEntity = list.get(i);
            final int offset = messageEntity.offset;
            if (offset > n) {
                messageEntity.offset = offset - n2;
            }
        }
    }
    
    private void saveDraftReplyMessage(final long n, final TLRPC.Message message) {
        if (message == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$d7oypRr3g0xB92hPNQy49uD_p8A(this, n, message));
    }
    
    private void savePeer(final int n, final int n2, final double n3) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$v4m2TDWRtPX_m91A0Er26YD_Gv8(this, n, n2, n3));
    }
    
    private void savePinnedMessage(final TLRPC.Message message) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$9sCQAO18nnba6ToshnaeVPhZWRk(this, message));
    }
    
    private void saveReplyMessages(final SparseArray<ArrayList<MessageObject>> sparseArray, final ArrayList<TLRPC.Message> list) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$H2PEh31YFWmyOEdst_RwAHB__gE(this, list, sparseArray));
    }
    
    private void searchMessagesInChat(String lastSearchQuery, long n, final long lastMergeDialogId, final int i, int n2, final boolean b, final TLRPC.User user) {
        boolean b2 = b ^ true;
        if (this.reqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.reqId, true);
            this.reqId = 0;
        }
        if (this.mergeReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.mergeReqId, true);
            this.mergeReqId = 0;
        }
        long n4;
        int offset_id;
        if (lastSearchQuery == null) {
            if (this.searchResultMessages.isEmpty()) {
                return;
            }
            if (n2 != 1) {
                if (n2 == 2) {
                    --this.lastReturnedNum;
                    n2 = this.lastReturnedNum;
                    if (n2 < 0) {
                        this.lastReturnedNum = 0;
                        return;
                    }
                    if (n2 >= this.searchResultMessages.size()) {
                        this.lastReturnedNum = this.searchResultMessages.size() - 1;
                    }
                    final MessageObject messageObject = this.searchResultMessages.get(this.lastReturnedNum);
                    final NotificationCenter instance = NotificationCenter.getInstance(this.currentAccount);
                    final int chatSearchResultsAvailable = NotificationCenter.chatSearchResultsAvailable;
                    n2 = messageObject.getId();
                    final int mask = this.getMask();
                    n = messageObject.getDialogId();
                    final int lastReturnedNum = this.lastReturnedNum;
                    final int[] messagesSearchCount = this.messagesSearchCount;
                    instance.postNotificationName(chatSearchResultsAvailable, i, n2, mask, n, lastReturnedNum, messagesSearchCount[0] + messagesSearchCount[1]);
                }
                return;
            }
            ++this.lastReturnedNum;
            if (this.lastReturnedNum < this.searchResultMessages.size()) {
                final MessageObject messageObject2 = this.searchResultMessages.get(this.lastReturnedNum);
                final NotificationCenter instance2 = NotificationCenter.getInstance(this.currentAccount);
                n2 = NotificationCenter.chatSearchResultsAvailable;
                final int id = messageObject2.getId();
                final int mask2 = this.getMask();
                n = messageObject2.getDialogId();
                final int lastReturnedNum2 = this.lastReturnedNum;
                final int[] messagesSearchCount2 = this.messagesSearchCount;
                instance2.postNotificationName(n2, i, id, mask2, n, lastReturnedNum2, messagesSearchCount2[0] + messagesSearchCount2[1]);
                return;
            }
            final boolean[] messagesSearchEndReached = this.messagesSearchEndReached;
            if (messagesSearchEndReached[0] && lastMergeDialogId == 0L && messagesSearchEndReached[1]) {
                --this.lastReturnedNum;
                return;
            }
            lastSearchQuery = this.lastSearchQuery;
            final ArrayList<MessageObject> searchResultMessages = this.searchResultMessages;
            final MessageObject messageObject3 = searchResultMessages.get(searchResultMessages.size() - 1);
            int n3;
            if (messageObject3.getDialogId() == n && !this.messagesSearchEndReached[0]) {
                n3 = messageObject3.getId();
                n4 = n;
            }
            else {
                if (messageObject3.getDialogId() == lastMergeDialogId) {
                    n3 = messageObject3.getId();
                }
                else {
                    n3 = 0;
                }
                this.messagesSearchEndReached[1] = false;
                n4 = lastMergeDialogId;
            }
            offset_id = n3;
            b2 = false;
        }
        else {
            if (b2) {
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatSearchResultsLoading, i);
                final boolean[] messagesSearchEndReached2 = this.messagesSearchEndReached;
                messagesSearchEndReached2[0] = (messagesSearchEndReached2[1] = false);
                final int[] messagesSearchCount3 = this.messagesSearchCount;
                messagesSearchCount3[messagesSearchCount3[1] = 0] = 0;
                this.searchResultMessages.clear();
                this.searchResultMessagesMap[0].clear();
                this.searchResultMessagesMap[1].clear();
            }
            n4 = n;
            offset_id = 0;
        }
        final boolean[] messagesSearchEndReached3 = this.messagesSearchEndReached;
        long n5 = n4;
        if (messagesSearchEndReached3[0]) {
            n5 = n4;
            if (!messagesSearchEndReached3[1]) {
                n5 = n4;
                if (lastMergeDialogId != 0L) {
                    n5 = lastMergeDialogId;
                }
            }
        }
        String s = "";
        if (n5 == n && b2) {
            if (lastMergeDialogId != 0L) {
                final TLRPC.InputPeer inputPeer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)lastMergeDialogId);
                if (inputPeer == null) {
                    return;
                }
                final TLRPC.TL_messages_search tl_messages_search = new TLRPC.TL_messages_search();
                tl_messages_search.peer = inputPeer;
                this.lastMergeDialogId = lastMergeDialogId;
                tl_messages_search.limit = 1;
                if (lastSearchQuery != null) {
                    s = lastSearchQuery;
                }
                tl_messages_search.q = s;
                if (user != null) {
                    tl_messages_search.from_id = MessagesController.getInstance(this.currentAccount).getInputUser(user);
                    tl_messages_search.flags |= 0x1;
                }
                tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterEmpty();
                this.mergeReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_search, new _$$Lambda$DataQuery$X_xjH4zrLbF98x7_YlP3_d_hOC0(this, lastMergeDialogId, tl_messages_search, n, i, n2, user), 2);
                return;
            }
            else {
                this.lastMergeDialogId = 0L;
                this.messagesSearchEndReached[1] = true;
                this.messagesSearchCount[1] = 0;
            }
        }
        final TLRPC.TL_messages_search tl_messages_search2 = new TLRPC.TL_messages_search();
        tl_messages_search2.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)n5);
        if (tl_messages_search2.peer == null) {
            return;
        }
        tl_messages_search2.limit = 21;
        if (lastSearchQuery != null) {
            s = lastSearchQuery;
        }
        tl_messages_search2.q = s;
        tl_messages_search2.offset_id = offset_id;
        if (user != null) {
            tl_messages_search2.from_id = MessagesController.getInstance(this.currentAccount).getInputUser(user);
            tl_messages_search2.flags |= 0x1;
        }
        tl_messages_search2.filter = new TLRPC.TL_inputMessagesFilterEmpty();
        n2 = this.lastReqId + 1;
        this.lastReqId = n2;
        this.lastSearchQuery = lastSearchQuery;
        this.reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_search2, new _$$Lambda$DataQuery$YQtVaGINuYPbSOpEA3NgINY1Mog(this, n2, tl_messages_search2, n5, n, i, lastMergeDialogId, user), 2);
    }
    
    public static void sortEntities(final ArrayList<TLRPC.MessageEntity> list) {
        Collections.sort((List<Object>)list, (Comparator<? super Object>)DataQuery.entityComparator);
    }
    
    public void addNewStickerSet(final TLRPC.TL_messages_stickerSet set) {
        if (this.stickerSetsById.indexOfKey(set.set.id) < 0) {
            if (!this.stickerSetsByName.containsKey(set.set.short_name)) {
                final int masks = set.set.masks ? 1 : 0;
                this.stickerSets[masks].add(0, set);
                this.stickerSetsById.put(set.set.id, (Object)set);
                this.installedStickerSetsById.put(set.set.id, (Object)set);
                this.stickerSetsByName.put(set.set.short_name, set);
                final LongSparseArray longSparseArray = new LongSparseArray();
                for (int i = 0; i < set.documents.size(); ++i) {
                    final TLRPC.Document document = set.documents.get(i);
                    longSparseArray.put(document.id, (Object)document);
                }
                for (int j = 0; j < set.packs.size(); ++j) {
                    final TLRPC.TL_stickerPack tl_stickerPack = set.packs.get(j);
                    tl_stickerPack.emoticon = tl_stickerPack.emoticon.replace("\ufe0f", "");
                    ArrayList<TLRPC.Document> value;
                    if ((value = this.allStickers.get(tl_stickerPack.emoticon)) == null) {
                        value = new ArrayList<TLRPC.Document>();
                        this.allStickers.put(tl_stickerPack.emoticon, value);
                    }
                    for (int k = 0; k < tl_stickerPack.documents.size(); ++k) {
                        final Long n = tl_stickerPack.documents.get(k);
                        if (this.stickersByEmoji.indexOfKey((long)n) < 0) {
                            this.stickersByEmoji.put((long)n, (Object)tl_stickerPack.emoticon);
                        }
                        final TLRPC.Document e = (TLRPC.Document)longSparseArray.get((long)n);
                        if (e != null) {
                            value.add(e);
                        }
                    }
                }
                this.loadHash[masks] = calcStickersHash(this.stickerSets[masks]);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.stickersDidLoad, masks);
                this.loadStickers(masks, false, true);
            }
        }
    }
    
    public void addRecentGif(final TLRPC.Document document, final int n) {
        while (true) {
            for (int i = 0; i < this.recentGifs.size(); ++i) {
                final TLRPC.Document element = this.recentGifs.get(i);
                if (element.id == document.id) {
                    this.recentGifs.remove(i);
                    this.recentGifs.add(0, element);
                    final boolean b = true;
                    if (!b) {
                        this.recentGifs.add(0, document);
                    }
                    if (this.recentGifs.size() > MessagesController.getInstance(this.currentAccount).maxRecentGifsCount) {
                        final ArrayList<TLRPC.Document> recentGifs = this.recentGifs;
                        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$xvF6o_1_RcVDv47fzE1DMviRP_s(this, recentGifs.remove(recentGifs.size() - 1)));
                    }
                    final ArrayList<TLRPC.Document> list = new ArrayList<TLRPC.Document>();
                    list.add(document);
                    this.processLoadedRecentDocuments(0, list, true, n, false);
                    return;
                }
            }
            final boolean b = false;
            continue;
        }
    }
    
    public void addRecentSticker(final int i, Object o, final TLRPC.Document document, final int n, final boolean unfave) {
        while (true) {
            for (int j = 0; j < this.recentStickers[i].size(); ++j) {
                final TLRPC.Document element = this.recentStickers[i].get(j);
                if (element.id == document.id) {
                    this.recentStickers[i].remove(j);
                    if (!unfave) {
                        this.recentStickers[i].add(0, element);
                    }
                    final boolean b = true;
                    if (!b && !unfave) {
                        this.recentStickers[i].add(0, document);
                    }
                    int n2;
                    if (i == 2) {
                        if (unfave) {
                            Toast.makeText(ApplicationLoader.applicationContext, (CharSequence)LocaleController.getString("RemovedFromFavorites", 2131560555), 0).show();
                        }
                        else {
                            Toast.makeText(ApplicationLoader.applicationContext, (CharSequence)LocaleController.getString("AddedToFavorites", 2131558597), 0).show();
                        }
                        final TLRPC.TL_messages_faveSticker tl_messages_faveSticker = new TLRPC.TL_messages_faveSticker();
                        tl_messages_faveSticker.id = new TLRPC.TL_inputDocument();
                        final TLRPC.InputDocument id = tl_messages_faveSticker.id;
                        id.id = document.id;
                        id.access_hash = document.access_hash;
                        id.file_reference = document.file_reference;
                        if (id.file_reference == null) {
                            id.file_reference = new byte[0];
                        }
                        tl_messages_faveSticker.unfave = unfave;
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_faveSticker, new _$$Lambda$DataQuery$s_0gO8L3a_nFeJVvPBKP6JduK9o(this, o, tl_messages_faveSticker));
                        n2 = MessagesController.getInstance(this.currentAccount).maxFaveStickersCount;
                    }
                    else {
                        n2 = MessagesController.getInstance(this.currentAccount).maxRecentStickersCount;
                    }
                    if (this.recentStickers[i].size() > n2 || unfave) {
                        TLRPC.Document document2;
                        if (unfave) {
                            document2 = document;
                        }
                        else {
                            final ArrayList<TLRPC.Document>[] recentStickers = this.recentStickers;
                            document2 = recentStickers[i].remove(recentStickers[i].size() - 1);
                        }
                        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$HfncZGNQeEbuO4TOGgzIo_2VPzk(this, i, document2));
                    }
                    if (!unfave) {
                        o = new ArrayList();
                        ((ArrayList<TLRPC.Document>)o).add(document);
                        this.processLoadedRecentDocuments(i, (ArrayList<TLRPC.Document>)o, false, n, false);
                    }
                    if (i == 2) {
                        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.recentDocumentsDidLoad, false, i);
                    }
                    return;
                }
            }
            final boolean b = false;
            continue;
        }
    }
    
    public boolean areAllTrendingStickerSetsUnread() {
        for (int size = this.featuredStickerSets.size(), i = 0; i < size; ++i) {
            final TLRPC.StickerSetCovered stickerSetCovered = this.featuredStickerSets.get(i);
            if (!getInstance(this.currentAccount).isStickerPackInstalled(stickerSetCovered.set.id)) {
                if (!stickerSetCovered.covers.isEmpty() || stickerSetCovered.cover != null) {
                    if (!this.unreadStickerSets.contains(stickerSetCovered.set.id)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public void beginTransaction() {
        this.inTransaction = true;
    }
    
    public void buildShortcuts() {
        if (Build$VERSION.SDK_INT < 25) {
            return;
        }
        final ArrayList<TLRPC.TL_topPeer> list = new ArrayList<TLRPC.TL_topPeer>();
        for (int i = 0; i < this.hints.size(); ++i) {
            list.add(this.hints.get(i));
            if (list.size() == 3) {
                break;
            }
        }
        Utilities.globalQueue.postRunnable(new _$$Lambda$DataQuery$usUs3tLksjqGquVtjg9TZkSDUaI(this, list));
    }
    
    public void calcNewHash(final int n) {
        this.loadHash[n] = calcStickersHash(this.stickerSets[n]);
    }
    
    public boolean canAddStickerToFavorites() {
        final boolean[] stickersLoaded = this.stickersLoaded;
        boolean b = false;
        if (!stickersLoaded[0] || this.stickerSets[0].size() >= 5 || !this.recentStickers[2].isEmpty()) {
            b = true;
        }
        return b;
    }
    
    public void checkFeaturedStickers() {
        if (!this.loadingFeaturedStickers && (!this.featuredStickersLoaded || Math.abs(System.currentTimeMillis() / 1000L - this.loadFeaturedDate) >= 3600L)) {
            this.loadFeaturedStickers(true, false);
        }
    }
    
    public void checkStickers(final int n) {
        if (!this.loadingStickers[n] && (!this.stickersLoaded[n] || Math.abs(System.currentTimeMillis() / 1000L - this.loadDate[n]) >= 3600L)) {
            this.loadStickers(n, true, false);
        }
    }
    
    public void cleanDraft(final long n, final boolean b) {
        final TLRPC.DraftMessage draftMessage = (TLRPC.DraftMessage)this.drafts.get(n);
        if (draftMessage == null) {
            return;
        }
        if (!b) {
            this.drafts.remove(n);
            this.draftMessages.remove(n);
            final SharedPreferences$Editor edit = this.preferences.edit();
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(n);
            final SharedPreferences$Editor remove = edit.remove(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("r_");
            sb2.append(n);
            remove.remove(sb2.toString()).commit();
            MessagesController.getInstance(this.currentAccount).sortDialogs(null);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        }
        else if (draftMessage.reply_to_msg_id != 0) {
            draftMessage.reply_to_msg_id = 0;
            draftMessage.flags &= 0xFFFFFFFE;
            this.saveDraft(n, draftMessage.message, draftMessage.entities, null, draftMessage.no_webpage, true);
        }
    }
    
    public void cleanup() {
        for (int i = 0; i < 3; ++i) {
            this.recentStickers[i].clear();
            this.loadingRecentStickers[i] = false;
            this.recentStickersLoaded[i] = false;
        }
        for (int j = 0; j < 4; ++j) {
            this.loadHash[j] = 0;
            this.loadDate[j] = 0;
            this.stickerSets[j].clear();
            this.loadingStickers[j] = false;
            this.stickersLoaded[j] = false;
        }
        this.featuredStickerSets.clear();
        this.loadFeaturedDate = 0;
        this.loadFeaturedHash = 0;
        this.allStickers.clear();
        this.allStickersFeatured.clear();
        this.stickersByEmoji.clear();
        this.featuredStickerSetsById.clear();
        this.featuredStickerSets.clear();
        this.unreadStickerSets.clear();
        this.recentGifs.clear();
        this.stickerSetsById.clear();
        this.installedStickerSetsById.clear();
        this.stickerSetsByName.clear();
        this.loadingFeaturedStickers = false;
        this.featuredStickersLoaded = false;
        this.loadingRecentGifs = false;
        this.recentGifsLoaded = false;
        this.currentFetchingEmoji.clear();
        this.loading = false;
        this.loaded = false;
        this.hints.clear();
        this.inlineBots.clear();
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadHints, new Object[0]);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadInlineHints, new Object[0]);
        this.drafts.clear();
        this.draftMessages.clear();
        this.preferences.edit().clear().commit();
        this.botInfos.clear();
        this.botKeyboards.clear();
        this.botKeyboardsByMids.clear();
    }
    
    public void clearAllDrafts() {
        this.drafts.clear();
        this.draftMessages.clear();
        this.preferences.edit().clear().commit();
        MessagesController.getInstance(this.currentAccount).sortDialogs(null);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
    }
    
    public void clearBotKeyboard(final long n, final ArrayList<Integer> list) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$4JOcfs_Jho1Vm_zEM2xMi0yW30I(this, list, n));
    }
    
    public void clearTopPeers() {
        this.hints.clear();
        this.inlineBots.clear();
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadHints, new Object[0]);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadInlineHints, new Object[0]);
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$HUiUbvM6V_1I_4yUAM7UkocMBMA(this));
        this.buildShortcuts();
    }
    
    public void endTransaction() {
        this.inTransaction = false;
    }
    
    public void fetchNewEmojiKeywords(final String[] array) {
        if (array == null) {
            return;
        }
        for (int i = 0; i < array.length; ++i) {
            final String s = array[i];
            if (TextUtils.isEmpty((CharSequence)s)) {
                return;
            }
            if (this.currentFetchingEmoji.get(s) != null) {
                return;
            }
            this.currentFetchingEmoji.put(s, true);
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$PLdKh5WsK6t3m___OQnCRDAEWbk(this, s));
        }
    }
    
    public HashMap<String, ArrayList<TLRPC.Document>> getAllStickers() {
        return this.allStickers;
    }
    
    public HashMap<String, ArrayList<TLRPC.Document>> getAllStickersFeatured() {
        return this.allStickersFeatured;
    }
    
    public int getArchivedStickersCount(final int n) {
        return this.archivedStickersCount[n];
    }
    
    public TLRPC.DraftMessage getDraft(final long n) {
        return (TLRPC.DraftMessage)this.drafts.get(n);
    }
    
    public TLRPC.Message getDraftMessage(final long n) {
        return (TLRPC.Message)this.draftMessages.get(n);
    }
    
    public String getEmojiForSticker(final long n) {
        String s = (String)this.stickersByEmoji.get(n);
        if (s == null) {
            s = "";
        }
        return s;
    }
    
    public void getEmojiSuggestions(final String[] array, final String s, final boolean b, final KeywordResultCallback keywordResultCallback) {
        this.getEmojiSuggestions(array, s, b, keywordResultCallback, null);
    }
    
    public void getEmojiSuggestions(final String[] array, final String s, final boolean b, final KeywordResultCallback keywordResultCallback, final CountDownLatch countDownLatch) {
        if (keywordResultCallback == null) {
            return;
        }
        Label_0073: {
            if (TextUtils.isEmpty((CharSequence)s) || array == null) {
                break Label_0073;
            }
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$gSc9XYndBuKxL05LBD_zU8iVGWA(this, array, keywordResultCallback, s, b, new ArrayList((Collection<? extends E>)Emoji.recentEmoji), countDownLatch));
            if (countDownLatch == null) {
                return;
            }
            try {
                countDownLatch.await();
                return;
                keywordResultCallback.run(new ArrayList<KeywordResult>(), null);
            }
            catch (Throwable t) {}
        }
    }
    
    public ArrayList<TLRPC.MessageEntity> getEntities(final CharSequence[] array) {
        if (array != null && array[0] != null) {
            Object o = null;
            int n = 0;
            int n3 = 0;
            int n5 = 0;
        Label_0697:
            while (true) {
                final int n2 = 0;
                n3 = -1;
                int n4 = n;
                n5 = n2;
                while (true) {
                    final CharSequence charSequence = array[0];
                    String s;
                    if (n5 == 0) {
                        s = "`";
                    }
                    else {
                        s = "```";
                    }
                    int index = TextUtils.indexOf(charSequence, (CharSequence)s, n4);
                    if (index == -1) {
                        break Label_0697;
                    }
                    if (n3 != -1) {
                        ArrayList<TLRPC.TL_messageEntityTextUrl> list;
                        if ((list = (ArrayList<TLRPC.TL_messageEntityTextUrl>)o) == null) {
                            list = new ArrayList<TLRPC.TL_messageEntityTextUrl>();
                        }
                        int n6;
                        if (n5 != 0) {
                            n6 = 3;
                        }
                        else {
                            n6 = 1;
                        }
                        for (int n7 = n6 + index; n7 < array[0].length() && array[0].charAt(n7) == '`'; ++n7) {
                            ++index;
                        }
                        int n8;
                        if (n5 != 0) {
                            n8 = 3;
                        }
                        else {
                            n8 = 1;
                        }
                        final int n9 = n8 + index;
                        if (n5 != 0) {
                            char char1;
                            if (n3 > 0) {
                                char1 = array[0].charAt(n3 - 1);
                            }
                            else {
                                char1 = '\0';
                            }
                            int n10;
                            if (char1 != ' ' && char1 != '\n') {
                                n10 = 0;
                            }
                            else {
                                n10 = 1;
                            }
                            CharSequence charSequence2 = this.substring(array[0], 0, n3 - n10);
                            final CharSequence substring = this.substring(array[0], n3 + 3, index);
                            final int n11 = index + 3;
                            char char2;
                            if (n11 < array[0].length()) {
                                char2 = array[0].charAt(n11);
                            }
                            else {
                                char2 = '\0';
                            }
                            final CharSequence charSequence3 = array[0];
                            int n12;
                            if (char2 != ' ' && char2 != '\n') {
                                n12 = 0;
                            }
                            else {
                                n12 = 1;
                            }
                            final CharSequence substring2 = this.substring(charSequence3, n11 + n12, array[0].length());
                            int n13;
                            if (charSequence2.length() != 0) {
                                charSequence2 = AndroidUtilities.concat(charSequence2, "\n");
                                n13 = n10;
                            }
                            else {
                                n13 = 1;
                            }
                            CharSequence concat = substring2;
                            if (substring2.length() != 0) {
                                concat = AndroidUtilities.concat("\n", substring2);
                            }
                            n = n9;
                            if (!TextUtils.isEmpty(substring)) {
                                array[0] = AndroidUtilities.concat(charSequence2, substring, concat);
                                final TLRPC.TL_messageEntityPre e = new TLRPC.TL_messageEntityPre();
                                e.offset = (n13 ^ 0x1) + n3;
                                e.length = index - n3 - 3 + (n13 ^ 0x1);
                                e.language = "";
                                list.add((TLRPC.TL_messageEntityTextUrl)e);
                                n = n9 - 6;
                            }
                        }
                        else {
                            final int n14 = n3 + 1;
                            n = n9;
                            if (n14 != index) {
                                array[0] = AndroidUtilities.concat(this.substring(array[0], 0, n3), this.substring(array[0], n14, index), this.substring(array[0], index + 1, array[0].length()));
                                final TLRPC.TL_messageEntityCode e2 = new TLRPC.TL_messageEntityCode();
                                e2.offset = n3;
                                e2.length = index - n3 - 1;
                                list.add((TLRPC.TL_messageEntityTextUrl)e2);
                                n = n9 - 2;
                            }
                        }
                        o = list;
                        break;
                    }
                    if (array[0].length() - index > 2 && array[0].charAt(index + 1) == '`' && array[0].charAt(index + 2) == '`') {
                        n5 = 1;
                    }
                    else {
                        n5 = 0;
                    }
                    int n15;
                    if (n5 != 0) {
                        n15 = 3;
                    }
                    else {
                        n15 = 1;
                    }
                    n3 = index;
                    n4 = index + n15;
                }
            }
            List<E> list2 = (List<E>)o;
            if (n3 != -1) {
                list2 = (List<E>)o;
                if (n5 != 0) {
                    array[0] = AndroidUtilities.concat(this.substring(array[0], 0, n3), this.substring(array[0], n3 + 2, array[0].length()));
                    if ((list2 = (List<E>)o) == null) {
                        list2 = (List<E>)new ArrayList<TLRPC.MessageEntity>();
                    }
                    final TLRPC.TL_messageEntityCode e3 = new TLRPC.TL_messageEntityCode();
                    e3.offset = n3;
                    e3.length = 1;
                    ((ArrayList<TLRPC.TL_messageEntityTextUrl>)list2).add((TLRPC.TL_messageEntityTextUrl)e3);
                }
            }
            Object o2 = list2;
            if (array[0] instanceof Spanned) {
                final Spanned spanned = (Spanned)array[0];
                final TypefaceSpan[] array2 = (TypefaceSpan[])spanned.getSpans(0, array[0].length(), (Class)TypefaceSpan.class);
                Object o3 = list2;
                if (array2 != null) {
                    o3 = list2;
                    if (array2.length > 0) {
                        int n16 = 0;
                        while (true) {
                            o3 = list2;
                            if (n16 >= array2.length) {
                                break;
                            }
                            final TypefaceSpan typefaceSpan = array2[n16];
                            final int spanStart = spanned.getSpanStart((Object)typefaceSpan);
                            final int spanEnd = spanned.getSpanEnd((Object)typefaceSpan);
                            ArrayList<TLRPC.MessageEntity> list3 = (ArrayList<TLRPC.MessageEntity>)list2;
                            if (!checkInclusion(spanStart, (ArrayList<TLRPC.MessageEntity>)list2)) {
                                list3 = (ArrayList<TLRPC.MessageEntity>)list2;
                                if (!checkInclusion(spanEnd, (ArrayList<TLRPC.MessageEntity>)list2)) {
                                    if (checkIntersection(spanStart, spanEnd, (ArrayList<TLRPC.MessageEntity>)list2)) {
                                        list3 = (ArrayList<TLRPC.MessageEntity>)list2;
                                    }
                                    else {
                                        if ((list3 = (ArrayList<TLRPC.MessageEntity>)list2) == null) {
                                            list3 = new ArrayList<TLRPC.MessageEntity>();
                                        }
                                        TLRPC.MessageEntity e4;
                                        if (typefaceSpan.isMono()) {
                                            e4 = new TLRPC.TL_messageEntityCode();
                                        }
                                        else if (typefaceSpan.isBold()) {
                                            e4 = new TLRPC.TL_messageEntityBold();
                                        }
                                        else {
                                            e4 = new TLRPC.TL_messageEntityItalic();
                                        }
                                        e4.offset = spanStart;
                                        e4.length = spanEnd - spanStart;
                                        list3.add(e4);
                                    }
                                }
                            }
                            ++n16;
                            list2 = (List<E>)list3;
                        }
                    }
                }
                final URLSpanUserMention[] array3 = (URLSpanUserMention[])spanned.getSpans(0, array[0].length(), (Class)URLSpanUserMention.class);
                Object o4 = o3;
                if (array3 != null) {
                    o4 = o3;
                    if (array3.length > 0) {
                        Object o5;
                        if ((o5 = o3) == null) {
                            o5 = new ArrayList<TLRPC.MessageEntity>();
                        }
                        int n17 = 0;
                        while (true) {
                            o4 = o5;
                            if (n17 >= array3.length) {
                                break;
                            }
                            final TLRPC.TL_inputMessageEntityMentionName e5 = new TLRPC.TL_inputMessageEntityMentionName();
                            e5.user_id = MessagesController.getInstance(this.currentAccount).getInputUser(Utilities.parseInt(array3[n17].getURL()));
                            if (e5.user_id != null) {
                                e5.offset = spanned.getSpanStart((Object)array3[n17]);
                                final int min = Math.min(spanned.getSpanEnd((Object)array3[n17]), array[0].length());
                                final int offset = e5.offset;
                                e5.length = min - offset;
                                if (array[0].charAt(offset + e5.length - 1) == ' ') {
                                    --e5.length;
                                }
                                ((ArrayList<TLRPC.TL_messageEntityTextUrl>)o5).add((TLRPC.TL_messageEntityTextUrl)e5);
                            }
                            ++n17;
                        }
                    }
                }
                final URLSpanReplacement[] array4 = (URLSpanReplacement[])spanned.getSpans(0, array[0].length(), (Class)URLSpanReplacement.class);
                o2 = o4;
                if (array4 != null) {
                    o2 = o4;
                    if (array4.length > 0) {
                        ArrayList<TLRPC.TL_messageEntityTextUrl> list4;
                        if ((list4 = (ArrayList<TLRPC.TL_messageEntityTextUrl>)o4) == null) {
                            list4 = new ArrayList<TLRPC.TL_messageEntityTextUrl>();
                        }
                        int n18 = 0;
                        while (true) {
                            o2 = list4;
                            if (n18 >= array4.length) {
                                break;
                            }
                            final TLRPC.TL_messageEntityTextUrl e6 = new TLRPC.TL_messageEntityTextUrl();
                            e6.offset = spanned.getSpanStart((Object)array4[n18]);
                            e6.length = Math.min(spanned.getSpanEnd((Object)array4[n18]), array[0].length()) - e6.offset;
                            e6.url = array4[n18].getURL();
                            list4.add(e6);
                            ++n18;
                        }
                    }
                }
            }
            for (int i = 0; i < 2; ++i) {
                String s2;
                if (i == 0) {
                    s2 = "**";
                }
                else {
                    s2 = "__";
                }
                char c;
                if (i == 0) {
                    c = '*';
                }
                else {
                    c = '_';
                }
                int index2 = 0;
                int offset2 = -1;
                while (true) {
                    index2 = TextUtils.indexOf(array[0], (CharSequence)s2, index2);
                    if (index2 == -1) {
                        break;
                    }
                    if (offset2 == -1) {
                        int char3;
                        if (index2 == 0) {
                            char3 = 32;
                        }
                        else {
                            char3 = array[0].charAt(index2 - 1);
                        }
                        int n19 = offset2;
                        Label_1549: {
                            if (!checkInclusion(index2, (ArrayList<TLRPC.MessageEntity>)o2)) {
                                if (char3 != 32) {
                                    n19 = offset2;
                                    if (char3 != 10) {
                                        break Label_1549;
                                    }
                                }
                                n19 = index2;
                            }
                        }
                        index2 += 2;
                        offset2 = n19;
                    }
                    else {
                        for (int n20 = index2 + 2; n20 < array[0].length() && array[0].charAt(n20) == c; ++n20) {
                            ++index2;
                        }
                        final int n21 = index2 + 2;
                        ArrayList<TLRPC.MessageEntity> list5;
                        int n23;
                        if (!checkInclusion(index2, (ArrayList<TLRPC.MessageEntity>)o2) && !checkIntersection(offset2, index2, (ArrayList<TLRPC.MessageEntity>)o2)) {
                            final int n22 = offset2 + 2;
                            if (n22 != index2) {
                                if ((list5 = (ArrayList<TLRPC.MessageEntity>)o2) == null) {
                                    list5 = new ArrayList<TLRPC.MessageEntity>();
                                }
                                try {
                                    array[0] = AndroidUtilities.concat(this.substring(array[0], 0, offset2), this.substring(array[0], n22, index2), this.substring(array[0], n21, array[0].length()));
                                }
                                catch (Exception ex) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append(this.substring(array[0], 0, offset2).toString());
                                    sb.append(this.substring(array[0], n22, index2).toString());
                                    sb.append(this.substring(array[0], n21, array[0].length()).toString());
                                    array[0] = sb.toString();
                                }
                                TLRPC.MessageEntity e7;
                                if (i == 0) {
                                    e7 = new TLRPC.TL_messageEntityBold();
                                }
                                else {
                                    e7 = new TLRPC.TL_messageEntityItalic();
                                }
                                e7.offset = offset2;
                                e7.length = index2 - offset2 - 2;
                                removeOffsetAfter(e7.offset + e7.length, 4, list5);
                                list5.add(e7);
                                n23 = n21 - 4;
                            }
                            else {
                                list5 = (ArrayList<TLRPC.MessageEntity>)o2;
                                n23 = n21;
                            }
                        }
                        else {
                            n23 = n21;
                            list5 = (ArrayList<TLRPC.MessageEntity>)o2;
                        }
                        final int n24 = -1;
                        index2 = n23;
                        o2 = list5;
                        offset2 = n24;
                    }
                }
            }
            return (ArrayList<TLRPC.MessageEntity>)o2;
        }
        return null;
    }
    
    public ArrayList<TLRPC.StickerSetCovered> getFeaturedStickerSets() {
        return this.featuredStickerSets;
    }
    
    public int getFeaturesStickersHashWithoutUnread() {
        long n = 0L;
        for (int i = 0; i < this.featuredStickerSets.size(); ++i) {
            final TLRPC.StickerSet set = this.featuredStickerSets.get(i).set;
            if (!set.archived) {
                final long id = set.id;
                n = ((n * 20261L + 2147483648L + (int)(id >> 32)) % 2147483648L * 20261L + 2147483648L + (int)id) % 2147483648L;
            }
        }
        return (int)n;
    }
    
    public TLRPC.TL_messages_stickerSet getGroupStickerSetById(final TLRPC.StickerSet set) {
        TLRPC.TL_messages_stickerSet set2;
        if ((set2 = (TLRPC.TL_messages_stickerSet)this.stickerSetsById.get(set.id)) == null) {
            final TLRPC.TL_messages_stickerSet set3 = (TLRPC.TL_messages_stickerSet)this.groupStickerSets.get(set.id);
            if (set3 != null) {
                final TLRPC.StickerSet set4 = set3.set;
                if (set4 != null) {
                    set2 = set3;
                    if (set4.hash != set.hash) {
                        this.loadGroupStickerSet(set, false);
                        set2 = set3;
                        return set2;
                    }
                    return set2;
                }
            }
            this.loadGroupStickerSet(set, true);
            set2 = set3;
        }
        return set2;
    }
    
    public String getLastSearchQuery() {
        return this.lastSearchQuery;
    }
    
    public void getMediaCount(final long n, int sendRequest, final int n2, final boolean b) {
        final int n3 = (int)n;
        if (!b && n3 != 0) {
            final TLRPC.TL_messages_search tl_messages_search = new TLRPC.TL_messages_search();
            tl_messages_search.limit = 1;
            tl_messages_search.offset_id = 0;
            if (sendRequest == 0) {
                tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterPhotoVideo();
            }
            else if (sendRequest == 1) {
                tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterDocument();
            }
            else if (sendRequest == 2) {
                tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterRoundVoice();
            }
            else if (sendRequest == 3) {
                tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterUrl();
            }
            else if (sendRequest == 4) {
                tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterMusic();
            }
            tl_messages_search.q = "";
            tl_messages_search.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(n3);
            if (tl_messages_search.peer == null) {
                return;
            }
            sendRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_search, new _$$Lambda$DataQuery$b79Pt8rqwc9fu4IUQhVikz5ynL4(this, n, sendRequest, n2));
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(sendRequest, n2);
        }
        else {
            this.getMediaCountDatabase(n, sendRequest, n2);
        }
    }
    
    public void getMediaCounts(final long n, final int n2) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$NQ5MkErWobCUOgqYnlpeC3jx_Y8(this, n, n2));
    }
    
    public ArrayList<TLRPC.Document> getRecentGifs() {
        return new ArrayList<TLRPC.Document>(this.recentGifs);
    }
    
    public ArrayList<TLRPC.Document> getRecentStickers(final int n) {
        final ArrayList<TLRPC.Document> list = this.recentStickers[n];
        return new ArrayList<TLRPC.Document>(list.subList(0, Math.min(list.size(), 20)));
    }
    
    public ArrayList<TLRPC.Document> getRecentStickersNoCopy(final int n) {
        return this.recentStickers[n];
    }
    
    public TLRPC.TL_messages_stickerSet getStickerSetById(final long n) {
        return (TLRPC.TL_messages_stickerSet)this.stickerSetsById.get(n);
    }
    
    public TLRPC.TL_messages_stickerSet getStickerSetByName(final String key) {
        return this.stickerSetsByName.get(key);
    }
    
    public String getStickerSetName(final long n) {
        final TLRPC.TL_messages_stickerSet set = (TLRPC.TL_messages_stickerSet)this.stickerSetsById.get(n);
        if (set != null) {
            return set.set.short_name;
        }
        final TLRPC.StickerSetCovered stickerSetCovered = (TLRPC.StickerSetCovered)this.featuredStickerSetsById.get(n);
        if (stickerSetCovered != null) {
            return stickerSetCovered.set.short_name;
        }
        return null;
    }
    
    public ArrayList<TLRPC.TL_messages_stickerSet> getStickerSets(final int n) {
        if (n == 3) {
            return this.stickerSets[2];
        }
        return this.stickerSets[n];
    }
    
    public ArrayList<Long> getUnreadStickerSets() {
        return this.unreadStickerSets;
    }
    
    public boolean hasRecentGif(final TLRPC.Document document) {
        for (int i = 0; i < this.recentGifs.size(); ++i) {
            final TLRPC.Document element = this.recentGifs.get(i);
            if (element.id == document.id) {
                this.recentGifs.remove(i);
                this.recentGifs.add(0, element);
                return true;
            }
        }
        return false;
    }
    
    public void increaseInlineRaiting(final int user_id) {
        if (!UserConfig.getInstance(this.currentAccount).suggestContacts) {
            return;
        }
        int max;
        if (UserConfig.getInstance(this.currentAccount).botRatingLoadTime != 0) {
            max = Math.max(1, (int)(System.currentTimeMillis() / 1000L) - UserConfig.getInstance(this.currentAccount).botRatingLoadTime);
        }
        else {
            max = 60;
        }
        final TLRPC.TL_topPeer tl_topPeer = null;
        int index = 0;
        TLRPC.TL_topPeer tl_topPeer2;
        while (true) {
            tl_topPeer2 = tl_topPeer;
            if (index >= this.inlineBots.size()) {
                break;
            }
            tl_topPeer2 = this.inlineBots.get(index);
            if (tl_topPeer2.peer.user_id == user_id) {
                break;
            }
            ++index;
        }
        TLRPC.TL_topPeer e;
        if ((e = tl_topPeer2) == null) {
            e = new TLRPC.TL_topPeer();
            e.peer = new TLRPC.TL_peerUser();
            e.peer.user_id = user_id;
            this.inlineBots.add(e);
        }
        e.rating += Math.exp(max / MessagesController.getInstance(this.currentAccount).ratingDecay);
        Collections.sort(this.inlineBots, (Comparator<? super TLRPC.TL_topPeer>)_$$Lambda$DataQuery$PxKOMpM0CQNQDkCfpxm9xlSwLx0.INSTANCE);
        if (this.inlineBots.size() > 20) {
            final ArrayList<TLRPC.TL_topPeer> inlineBots = this.inlineBots;
            inlineBots.remove(inlineBots.size() - 1);
        }
        this.savePeer(user_id, 1, e.rating);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadInlineHints, new Object[0]);
    }
    
    public void increasePeerRaiting(final long n) {
        if (!UserConfig.getInstance(this.currentAccount).suggestContacts) {
            return;
        }
        final int i = (int)n;
        if (i <= 0) {
            return;
        }
        TLRPC.User user;
        if (i > 0) {
            user = MessagesController.getInstance(this.currentAccount).getUser(i);
        }
        else {
            user = null;
        }
        if (user != null && !user.bot) {
            if (!user.self) {
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$5no8NqhTBSPD_1vCOByuz2PkDVw(this, n, i));
            }
        }
    }
    
    public void installShortcut(final long p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: lload_1        
        //     2: invokespecial   org/telegram/messenger/DataQuery.createIntrnalShortcutIntent:(J)Landroid/content/Intent;
        //     5: astore_3       
        //     6: lload_1        
        //     7: l2i            
        //     8: istore          4
        //    10: lload_1        
        //    11: bipush          32
        //    13: lshr           
        //    14: l2i            
        //    15: istore          5
        //    17: iload           4
        //    19: ifne            68
        //    22: aload_0        
        //    23: getfield        org/telegram/messenger/DataQuery.currentAccount:I
        //    26: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //    29: iload           5
        //    31: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    34: invokevirtual   org/telegram/messenger/MessagesController.getEncryptedChat:(Ljava/lang/Integer;)Lorg/telegram/tgnet/TLRPC$EncryptedChat;
        //    37: astore          6
        //    39: aload           6
        //    41: ifnonnull       45
        //    44: return         
        //    45: aload_0        
        //    46: getfield        org/telegram/messenger/DataQuery.currentAccount:I
        //    49: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //    52: aload           6
        //    54: getfield        org/telegram/tgnet/TLRPC$EncryptedChat.user_id:I
        //    57: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    60: invokevirtual   org/telegram/messenger/MessagesController.getUser:(Ljava/lang/Integer;)Lorg/telegram/tgnet/TLRPC$User;
        //    63: astore          6
        //    65: goto            90
        //    68: iload           4
        //    70: ifle            100
        //    73: aload_0        
        //    74: getfield        org/telegram/messenger/DataQuery.currentAccount:I
        //    77: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //    80: iload           4
        //    82: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    85: invokevirtual   org/telegram/messenger/MessagesController.getUser:(Ljava/lang/Integer;)Lorg/telegram/tgnet/TLRPC$User;
        //    88: astore          6
        //    90: aconst_null    
        //    91: astore          7
        //    93: aload           6
        //    95: astore          8
        //    97: goto            126
        //   100: iload           4
        //   102: ifge            1067
        //   105: aload_0        
        //   106: getfield        org/telegram/messenger/DataQuery.currentAccount:I
        //   109: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //   112: iload           4
        //   114: ineg           
        //   115: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   118: invokevirtual   org/telegram/messenger/MessagesController.getChat:(Ljava/lang/Integer;)Lorg/telegram/tgnet/TLRPC$Chat;
        //   121: astore          7
        //   123: aconst_null    
        //   124: astore          8
        //   126: aload           8
        //   128: ifnonnull       137
        //   131: aload           7
        //   133: ifnonnull       137
        //   136: return         
        //   137: aload           8
        //   139: ifnull          218
        //   142: aload           8
        //   144: invokestatic    org/telegram/messenger/UserObject.isUserSelf:(Lorg/telegram/tgnet/TLRPC$User;)Z
        //   147: ifeq            170
        //   150: ldc_w           "SavedMessages"
        //   153: ldc_w           2131560633
        //   156: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //   159: astore          9
        //   161: aconst_null    
        //   162: astore          6
        //   164: iconst_1       
        //   165: istore          5
        //   167: goto            268
        //   170: aload           8
        //   172: getfield        org/telegram/tgnet/TLRPC$User.first_name:Ljava/lang/String;
        //   175: aload           8
        //   177: getfield        org/telegram/tgnet/TLRPC$User.last_name:Ljava/lang/String;
        //   180: invokestatic    org/telegram/messenger/ContactsController.formatName:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   183: astore          6
        //   185: aload           6
        //   187: astore          10
        //   189: aload           8
        //   191: getfield        org/telegram/tgnet/TLRPC$User.photo:Lorg/telegram/tgnet/TLRPC$UserProfilePhoto;
        //   194: ifnull          258
        //   197: aload           8
        //   199: getfield        org/telegram/tgnet/TLRPC$User.photo:Lorg/telegram/tgnet/TLRPC$UserProfilePhoto;
        //   202: getfield        org/telegram/tgnet/TLRPC$UserProfilePhoto.photo_small:Lorg/telegram/tgnet/TLRPC$FileLocation;
        //   205: astore          9
        //   207: aload           6
        //   209: astore          10
        //   211: aload           9
        //   213: astore          6
        //   215: goto            261
        //   218: aload           7
        //   220: getfield        org/telegram/tgnet/TLRPC$Chat.title:Ljava/lang/String;
        //   223: astore          6
        //   225: aload           6
        //   227: astore          10
        //   229: aload           7
        //   231: getfield        org/telegram/tgnet/TLRPC$Chat.photo:Lorg/telegram/tgnet/TLRPC$ChatPhoto;
        //   234: ifnull          258
        //   237: aload           7
        //   239: getfield        org/telegram/tgnet/TLRPC$Chat.photo:Lorg/telegram/tgnet/TLRPC$ChatPhoto;
        //   242: getfield        org/telegram/tgnet/TLRPC$ChatPhoto.photo_small:Lorg/telegram/tgnet/TLRPC$FileLocation;
        //   245: astore          9
        //   247: aload           6
        //   249: astore          10
        //   251: aload           9
        //   253: astore          6
        //   255: goto            261
        //   258: aconst_null    
        //   259: astore          6
        //   261: iconst_0       
        //   262: istore          5
        //   264: aload           10
        //   266: astore          9
        //   268: iload           5
        //   270: ifne            287
        //   273: aload           6
        //   275: ifnull          281
        //   278: goto            287
        //   281: aconst_null    
        //   282: astore          6
        //   284: goto            656
        //   287: iload           5
        //   289: ifne            317
        //   292: aload           6
        //   294: iconst_1       
        //   295: invokestatic    org/telegram/messenger/FileLoader.getPathToAttach:(Lorg/telegram/tgnet/TLObject;Z)Ljava/io/File;
        //   298: invokevirtual   java/io/File.toString:()Ljava/lang/String;
        //   301: invokestatic    android/graphics/BitmapFactory.decodeFile:(Ljava/lang/String;)Landroid/graphics/Bitmap;
        //   304: astore          6
        //   306: goto            320
        //   309: astore          10
        //   311: aconst_null    
        //   312: astore          6
        //   314: goto            643
        //   317: aconst_null    
        //   318: astore          6
        //   320: iload           5
        //   322: ifne            334
        //   325: aload           6
        //   327: astore          10
        //   329: aload           6
        //   331: ifnull          652
        //   334: ldc_w           58.0
        //   337: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   340: istore          4
        //   342: iload           4
        //   344: iload           4
        //   346: getstatic       android/graphics/Bitmap$Config.ARGB_8888:Landroid/graphics/Bitmap$Config;
        //   349: invokestatic    android/graphics/Bitmap.createBitmap:(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
        //   352: astore          10
        //   354: aload           10
        //   356: iconst_0       
        //   357: invokevirtual   android/graphics/Bitmap.eraseColor:(I)V
        //   360: new             Landroid/graphics/Canvas;
        //   363: astore          11
        //   365: aload           11
        //   367: aload           10
        //   369: invokespecial   android/graphics/Canvas.<init>:(Landroid/graphics/Bitmap;)V
        //   372: iload           5
        //   374: ifeq            416
        //   377: new             Lorg/telegram/ui/Components/AvatarDrawable;
        //   380: astore          12
        //   382: aload           12
        //   384: aload           8
        //   386: invokespecial   org/telegram/ui/Components/AvatarDrawable.<init>:(Lorg/telegram/tgnet/TLRPC$User;)V
        //   389: aload           12
        //   391: iconst_1       
        //   392: invokevirtual   org/telegram/ui/Components/AvatarDrawable.setAvatarType:(I)V
        //   395: aload           12
        //   397: iconst_0       
        //   398: iconst_0       
        //   399: iload           4
        //   401: iload           4
        //   403: invokevirtual   android/graphics/drawable/Drawable.setBounds:(IIII)V
        //   406: aload           12
        //   408: aload           11
        //   410: invokevirtual   org/telegram/ui/Components/AvatarDrawable.draw:(Landroid/graphics/Canvas;)V
        //   413: goto            555
        //   416: new             Landroid/graphics/BitmapShader;
        //   419: astore          12
        //   421: aload           12
        //   423: aload           6
        //   425: getstatic       android/graphics/Shader$TileMode.CLAMP:Landroid/graphics/Shader$TileMode;
        //   428: getstatic       android/graphics/Shader$TileMode.CLAMP:Landroid/graphics/Shader$TileMode;
        //   431: invokespecial   android/graphics/BitmapShader.<init>:(Landroid/graphics/Bitmap;Landroid/graphics/Shader$TileMode;Landroid/graphics/Shader$TileMode;)V
        //   434: getstatic       org/telegram/messenger/DataQuery.roundPaint:Landroid/graphics/Paint;
        //   437: ifnonnull       471
        //   440: new             Landroid/graphics/Paint;
        //   443: astore          13
        //   445: aload           13
        //   447: iconst_1       
        //   448: invokespecial   android/graphics/Paint.<init>:(I)V
        //   451: aload           13
        //   453: putstatic       org/telegram/messenger/DataQuery.roundPaint:Landroid/graphics/Paint;
        //   456: new             Landroid/graphics/RectF;
        //   459: astore          13
        //   461: aload           13
        //   463: invokespecial   android/graphics/RectF.<init>:()V
        //   466: aload           13
        //   468: putstatic       org/telegram/messenger/DataQuery.bitmapRect:Landroid/graphics/RectF;
        //   471: iload           4
        //   473: i2f            
        //   474: aload           6
        //   476: invokevirtual   android/graphics/Bitmap.getWidth:()I
        //   479: i2f            
        //   480: fdiv           
        //   481: fstore          14
        //   483: aload           11
        //   485: invokevirtual   android/graphics/Canvas.save:()I
        //   488: pop            
        //   489: aload           11
        //   491: fload           14
        //   493: fload           14
        //   495: invokevirtual   android/graphics/Canvas.scale:(FF)V
        //   498: getstatic       org/telegram/messenger/DataQuery.roundPaint:Landroid/graphics/Paint;
        //   501: aload           12
        //   503: invokevirtual   android/graphics/Paint.setShader:(Landroid/graphics/Shader;)Landroid/graphics/Shader;
        //   506: pop            
        //   507: getstatic       org/telegram/messenger/DataQuery.bitmapRect:Landroid/graphics/RectF;
        //   510: fconst_0       
        //   511: fconst_0       
        //   512: aload           6
        //   514: invokevirtual   android/graphics/Bitmap.getWidth:()I
        //   517: i2f            
        //   518: aload           6
        //   520: invokevirtual   android/graphics/Bitmap.getHeight:()I
        //   523: i2f            
        //   524: invokevirtual   android/graphics/RectF.set:(FFFF)V
        //   527: aload           11
        //   529: getstatic       org/telegram/messenger/DataQuery.bitmapRect:Landroid/graphics/RectF;
        //   532: aload           6
        //   534: invokevirtual   android/graphics/Bitmap.getWidth:()I
        //   537: i2f            
        //   538: aload           6
        //   540: invokevirtual   android/graphics/Bitmap.getHeight:()I
        //   543: i2f            
        //   544: getstatic       org/telegram/messenger/DataQuery.roundPaint:Landroid/graphics/Paint;
        //   547: invokevirtual   android/graphics/Canvas.drawRoundRect:(Landroid/graphics/RectF;FFLandroid/graphics/Paint;)V
        //   550: aload           11
        //   552: invokevirtual   android/graphics/Canvas.restore:()V
        //   555: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   558: invokevirtual   android/content/Context.getResources:()Landroid/content/res/Resources;
        //   561: ldc_w           2131165314
        //   564: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   567: astore          12
        //   569: ldc_w           15.0
        //   572: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   575: istore          5
        //   577: iload           4
        //   579: iload           5
        //   581: isub           
        //   582: istore          15
        //   584: iload           15
        //   586: fconst_2       
        //   587: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   590: isub           
        //   591: istore          4
        //   593: iload           15
        //   595: fconst_2       
        //   596: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   599: isub           
        //   600: istore          15
        //   602: aload           12
        //   604: iload           4
        //   606: iload           15
        //   608: iload           4
        //   610: iload           5
        //   612: iadd           
        //   613: iload           5
        //   615: iload           15
        //   617: iadd           
        //   618: invokevirtual   android/graphics/drawable/Drawable.setBounds:(IIII)V
        //   621: aload           12
        //   623: aload           11
        //   625: invokevirtual   android/graphics/drawable/Drawable.draw:(Landroid/graphics/Canvas;)V
        //   628: aload           11
        //   630: aconst_null    
        //   631: invokevirtual   android/graphics/Canvas.setBitmap:(Landroid/graphics/Bitmap;)V
        //   634: aload           10
        //   636: astore          6
        //   638: goto            656
        //   641: astore          10
        //   643: aload           10
        //   645: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   648: aload           6
        //   650: astore          10
        //   652: aload           10
        //   654: astore          6
        //   656: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   659: bipush          26
        //   661: if_icmplt       872
        //   664: new             Landroid/content/pm/ShortcutInfo$Builder;
        //   667: astore          11
        //   669: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   672: astore          12
        //   674: new             Ljava/lang/StringBuilder;
        //   677: astore          10
        //   679: aload           10
        //   681: invokespecial   java/lang/StringBuilder.<init>:()V
        //   684: aload           10
        //   686: ldc_w           "sdid_"
        //   689: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   692: pop            
        //   693: aload           10
        //   695: lload_1        
        //   696: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   699: pop            
        //   700: aload           11
        //   702: aload           12
        //   704: aload           10
        //   706: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   709: invokespecial   android/content/pm/ShortcutInfo$Builder.<init>:(Landroid/content/Context;Ljava/lang/String;)V
        //   712: aload           11
        //   714: aload           9
        //   716: invokevirtual   android/content/pm/ShortcutInfo$Builder.setShortLabel:(Ljava/lang/CharSequence;)Landroid/content/pm/ShortcutInfo$Builder;
        //   719: aload_3        
        //   720: invokevirtual   android/content/pm/ShortcutInfo$Builder.setIntent:(Landroid/content/Intent;)Landroid/content/pm/ShortcutInfo$Builder;
        //   723: astore          10
        //   725: aload           6
        //   727: ifnull          744
        //   730: aload           10
        //   732: aload           6
        //   734: invokestatic    android/graphics/drawable/Icon.createWithBitmap:(Landroid/graphics/Bitmap;)Landroid/graphics/drawable/Icon;
        //   737: invokevirtual   android/content/pm/ShortcutInfo$Builder.setIcon:(Landroid/graphics/drawable/Icon;)Landroid/content/pm/ShortcutInfo$Builder;
        //   740: pop            
        //   741: goto            847
        //   744: aload           8
        //   746: ifnull          793
        //   749: aload           8
        //   751: getfield        org/telegram/tgnet/TLRPC$User.bot:Z
        //   754: ifeq            775
        //   757: aload           10
        //   759: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   762: ldc_w           2131165311
        //   765: invokestatic    android/graphics/drawable/Icon.createWithResource:(Landroid/content/Context;I)Landroid/graphics/drawable/Icon;
        //   768: invokevirtual   android/content/pm/ShortcutInfo$Builder.setIcon:(Landroid/graphics/drawable/Icon;)Landroid/content/pm/ShortcutInfo$Builder;
        //   771: pop            
        //   772: goto            847
        //   775: aload           10
        //   777: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   780: ldc_w           2131165315
        //   783: invokestatic    android/graphics/drawable/Icon.createWithResource:(Landroid/content/Context;I)Landroid/graphics/drawable/Icon;
        //   786: invokevirtual   android/content/pm/ShortcutInfo$Builder.setIcon:(Landroid/graphics/drawable/Icon;)Landroid/content/pm/ShortcutInfo$Builder;
        //   789: pop            
        //   790: goto            847
        //   793: aload           7
        //   795: ifnull          847
        //   798: aload           7
        //   800: invokestatic    org/telegram/messenger/ChatObject.isChannel:(Lorg/telegram/tgnet/TLRPC$Chat;)Z
        //   803: ifeq            832
        //   806: aload           7
        //   808: getfield        org/telegram/tgnet/TLRPC$Chat.megagroup:Z
        //   811: ifne            832
        //   814: aload           10
        //   816: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   819: ldc_w           2131165312
        //   822: invokestatic    android/graphics/drawable/Icon.createWithResource:(Landroid/content/Context;I)Landroid/graphics/drawable/Icon;
        //   825: invokevirtual   android/content/pm/ShortcutInfo$Builder.setIcon:(Landroid/graphics/drawable/Icon;)Landroid/content/pm/ShortcutInfo$Builder;
        //   828: pop            
        //   829: goto            847
        //   832: aload           10
        //   834: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   837: ldc_w           2131165313
        //   840: invokestatic    android/graphics/drawable/Icon.createWithResource:(Landroid/content/Context;I)Landroid/graphics/drawable/Icon;
        //   843: invokevirtual   android/content/pm/ShortcutInfo$Builder.setIcon:(Landroid/graphics/drawable/Icon;)Landroid/content/pm/ShortcutInfo$Builder;
        //   846: pop            
        //   847: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   850: ldc_w           Landroid/content/pm/ShortcutManager;.class
        //   853: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/Class;)Ljava/lang/Object;
        //   856: checkcast       Landroid/content/pm/ShortcutManager;
        //   859: aload           10
        //   861: invokevirtual   android/content/pm/ShortcutInfo$Builder.build:()Landroid/content/pm/ShortcutInfo;
        //   864: aconst_null    
        //   865: invokevirtual   android/content/pm/ShortcutManager.requestPinShortcut:(Landroid/content/pm/ShortcutInfo;Landroid/content/IntentSender;)Z
        //   868: pop            
        //   869: goto            1075
        //   872: new             Landroid/content/Intent;
        //   875: astore          10
        //   877: aload           10
        //   879: invokespecial   android/content/Intent.<init>:()V
        //   882: aload           6
        //   884: ifnull          901
        //   887: aload           10
        //   889: ldc_w           "android.intent.extra.shortcut.ICON"
        //   892: aload           6
        //   894: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;Landroid/os/Parcelable;)Landroid/content/Intent;
        //   897: pop            
        //   898: goto            1016
        //   901: aload           8
        //   903: ifnull          956
        //   906: aload           8
        //   908: getfield        org/telegram/tgnet/TLRPC$User.bot:Z
        //   911: ifeq            935
        //   914: aload           10
        //   916: ldc_w           "android.intent.extra.shortcut.ICON_RESOURCE"
        //   919: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   922: ldc_w           2131165311
        //   925: invokestatic    android/content/Intent$ShortcutIconResource.fromContext:(Landroid/content/Context;I)Landroid/content/Intent$ShortcutIconResource;
        //   928: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;Landroid/os/Parcelable;)Landroid/content/Intent;
        //   931: pop            
        //   932: goto            1016
        //   935: aload           10
        //   937: ldc_w           "android.intent.extra.shortcut.ICON_RESOURCE"
        //   940: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   943: ldc_w           2131165315
        //   946: invokestatic    android/content/Intent$ShortcutIconResource.fromContext:(Landroid/content/Context;I)Landroid/content/Intent$ShortcutIconResource;
        //   949: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;Landroid/os/Parcelable;)Landroid/content/Intent;
        //   952: pop            
        //   953: goto            1016
        //   956: aload           7
        //   958: ifnull          1016
        //   961: aload           7
        //   963: invokestatic    org/telegram/messenger/ChatObject.isChannel:(Lorg/telegram/tgnet/TLRPC$Chat;)Z
        //   966: ifeq            998
        //   969: aload           7
        //   971: getfield        org/telegram/tgnet/TLRPC$Chat.megagroup:Z
        //   974: ifne            998
        //   977: aload           10
        //   979: ldc_w           "android.intent.extra.shortcut.ICON_RESOURCE"
        //   982: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   985: ldc_w           2131165312
        //   988: invokestatic    android/content/Intent$ShortcutIconResource.fromContext:(Landroid/content/Context;I)Landroid/content/Intent$ShortcutIconResource;
        //   991: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;Landroid/os/Parcelable;)Landroid/content/Intent;
        //   994: pop            
        //   995: goto            1016
        //   998: aload           10
        //  1000: ldc_w           "android.intent.extra.shortcut.ICON_RESOURCE"
        //  1003: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  1006: ldc_w           2131165313
        //  1009: invokestatic    android/content/Intent$ShortcutIconResource.fromContext:(Landroid/content/Context;I)Landroid/content/Intent$ShortcutIconResource;
        //  1012: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;Landroid/os/Parcelable;)Landroid/content/Intent;
        //  1015: pop            
        //  1016: aload           10
        //  1018: ldc_w           "android.intent.extra.shortcut.INTENT"
        //  1021: aload_3        
        //  1022: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;Landroid/os/Parcelable;)Landroid/content/Intent;
        //  1025: pop            
        //  1026: aload           10
        //  1028: ldc_w           "android.intent.extra.shortcut.NAME"
        //  1031: aload           9
        //  1033: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
        //  1036: pop            
        //  1037: aload           10
        //  1039: ldc_w           "duplicate"
        //  1042: iconst_0       
        //  1043: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;Z)Landroid/content/Intent;
        //  1046: pop            
        //  1047: aload           10
        //  1049: ldc_w           "com.android.launcher.action.INSTALL_SHORTCUT"
        //  1052: invokevirtual   android/content/Intent.setAction:(Ljava/lang/String;)Landroid/content/Intent;
        //  1055: pop            
        //  1056: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  1059: aload           10
        //  1061: invokevirtual   android/content/Context.sendBroadcast:(Landroid/content/Intent;)V
        //  1064: goto            1075
        //  1067: return         
        //  1068: astore          6
        //  1070: aload           6
        //  1072: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1075: return         
        //  1076: astore          6
        //  1078: aload           10
        //  1080: astore          6
        //  1082: goto            656
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      6      1068   1075   Ljava/lang/Exception;
        //  22     39     1068   1075   Ljava/lang/Exception;
        //  45     65     1068   1075   Ljava/lang/Exception;
        //  73     90     1068   1075   Ljava/lang/Exception;
        //  105    123    1068   1075   Ljava/lang/Exception;
        //  142    161    1068   1075   Ljava/lang/Exception;
        //  170    185    1068   1075   Ljava/lang/Exception;
        //  189    207    1068   1075   Ljava/lang/Exception;
        //  218    225    1068   1075   Ljava/lang/Exception;
        //  229    247    1068   1075   Ljava/lang/Exception;
        //  292    306    309    317    Ljava/lang/Throwable;
        //  334    372    641    643    Ljava/lang/Throwable;
        //  377    413    641    643    Ljava/lang/Throwable;
        //  416    471    641    643    Ljava/lang/Throwable;
        //  471    555    641    643    Ljava/lang/Throwable;
        //  555    577    641    643    Ljava/lang/Throwable;
        //  584    628    641    643    Ljava/lang/Throwable;
        //  628    634    1076   1085   Ljava/lang/Exception;
        //  628    634    641    643    Ljava/lang/Throwable;
        //  643    648    1068   1075   Ljava/lang/Exception;
        //  656    725    1068   1075   Ljava/lang/Exception;
        //  730    741    1068   1075   Ljava/lang/Exception;
        //  749    772    1068   1075   Ljava/lang/Exception;
        //  775    790    1068   1075   Ljava/lang/Exception;
        //  798    829    1068   1075   Ljava/lang/Exception;
        //  832    847    1068   1075   Ljava/lang/Exception;
        //  847    869    1068   1075   Ljava/lang/Exception;
        //  872    882    1068   1075   Ljava/lang/Exception;
        //  887    898    1068   1075   Ljava/lang/Exception;
        //  906    932    1068   1075   Ljava/lang/Exception;
        //  935    953    1068   1075   Ljava/lang/Exception;
        //  961    995    1068   1075   Ljava/lang/Exception;
        //  998    1016   1068   1075   Ljava/lang/Exception;
        //  1016   1064   1068   1075   Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0643:
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
    
    public boolean isLoadingStickers(final int n) {
        return this.loadingStickers[n];
    }
    
    public boolean isMessageFound(final int n, final boolean b) {
        return this.searchResultMessagesMap[b].indexOfKey(n) >= 0;
    }
    
    public boolean isStickerInFavorites(final TLRPC.Document document) {
        for (int i = 0; i < this.recentStickers[2].size(); ++i) {
            final TLRPC.Document document2 = this.recentStickers[2].get(i);
            if (document2.id == document.id && document2.dc_id == document.dc_id) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isStickerPackInstalled(final long n) {
        return this.installedStickerSetsById.indexOfKey(n) >= 0;
    }
    
    public boolean isStickerPackInstalled(final String key) {
        return this.stickerSetsByName.containsKey(key);
    }
    
    public boolean isStickerPackUnread(final long l) {
        return this.unreadStickerSets.contains(l);
    }
    
    public void loadArchivedStickersCount(final int n, final boolean b) {
        final boolean b2 = true;
        if (b) {
            final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
            final StringBuilder sb = new StringBuilder();
            sb.append("archivedStickersCount");
            sb.append(n);
            final int int1 = notificationsSettings.getInt(sb.toString(), -1);
            if (int1 == -1) {
                this.loadArchivedStickersCount(n, false);
            }
            else {
                this.archivedStickersCount[n] = int1;
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.archivedStickersCountDidLoad, n);
            }
        }
        else {
            final TLRPC.TL_messages_getArchivedStickers tl_messages_getArchivedStickers = new TLRPC.TL_messages_getArchivedStickers();
            tl_messages_getArchivedStickers.limit = 0;
            tl_messages_getArchivedStickers.masks = (n == 1 && b2);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getArchivedStickers, new _$$Lambda$DataQuery$vXxSlr6eK9Z6xNmLs0Dj7UWvraQ(this, n));
        }
    }
    
    public void loadBotInfo(final int n, final boolean b, final int i) {
        if (b) {
            final TLRPC.BotInfo botInfo = (TLRPC.BotInfo)this.botInfos.get(n);
            if (botInfo != null) {
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.botInfoDidLoad, botInfo, i);
                return;
            }
        }
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$RU0vB5vrHjb7JApo8zy3krcj73Y(this, n, i));
    }
    
    public void loadBotKeyboard(final long l) {
        final TLRPC.Message message = (TLRPC.Message)this.botKeyboards.get(l);
        if (message != null) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.botKeyboardDidLoad, message, l);
            return;
        }
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$C70bX86RVrRMk7D3n7tAtGTuntU(this, l));
    }
    
    public void loadDrafts() {
        if (!UserConfig.getInstance(this.currentAccount).draftsLoaded) {
            if (!this.loadingDrafts) {
                this.loadingDrafts = true;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_messages_getAllDrafts(), new _$$Lambda$DataQuery$A03PPqgH8dlVye1igOOaKjX18Mo(this));
            }
        }
    }
    
    public void loadFeaturedStickers(final boolean b, final boolean b2) {
        if (this.loadingFeaturedStickers) {
            return;
        }
        this.loadingFeaturedStickers = true;
        if (b) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$_js4uGbpT5ecS0xV_WWy27wFeXg(this));
        }
        else {
            final TLRPC.TL_messages_getFeaturedStickers tl_messages_getFeaturedStickers = new TLRPC.TL_messages_getFeaturedStickers();
            int loadFeaturedHash;
            if (b2) {
                loadFeaturedHash = 0;
            }
            else {
                loadFeaturedHash = this.loadFeaturedHash;
            }
            tl_messages_getFeaturedStickers.hash = loadFeaturedHash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getFeaturedStickers, new _$$Lambda$DataQuery$KjoE15J_7Y5rDsfACZqjBs6rNXk(this, tl_messages_getFeaturedStickers));
        }
    }
    
    public void loadHints(final boolean b) {
        if (!this.loading) {
            if (UserConfig.getInstance(this.currentAccount).suggestContacts) {
                if (b) {
                    if (this.loaded) {
                        return;
                    }
                    this.loading = true;
                    MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$lAwKQ3k2p_MRBgXcDMGAD4Or380(this));
                    this.loaded = true;
                }
                else {
                    this.loading = true;
                    final TLRPC.TL_contacts_getTopPeers tl_contacts_getTopPeers = new TLRPC.TL_contacts_getTopPeers();
                    tl_contacts_getTopPeers.hash = 0;
                    tl_contacts_getTopPeers.bots_pm = false;
                    tl_contacts_getTopPeers.correspondents = true;
                    tl_contacts_getTopPeers.groups = false;
                    tl_contacts_getTopPeers.channels = false;
                    tl_contacts_getTopPeers.bots_inline = true;
                    tl_contacts_getTopPeers.offset = 0;
                    tl_contacts_getTopPeers.limit = 20;
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_contacts_getTopPeers, new _$$Lambda$DataQuery$pT1Y9otWt0WNp61eT_y0o9CzuLY(this));
                }
            }
        }
    }
    
    public void loadMedia(final long n, int sendRequest, final int offset_id, final int n2, final int n3, final int n4) {
        final int n5 = (int)n;
        final boolean b = n5 < 0 && ChatObject.isChannel(-n5, this.currentAccount);
        if (n3 == 0 && n5 != 0) {
            final TLRPC.TL_messages_search tl_messages_search = new TLRPC.TL_messages_search();
            tl_messages_search.limit = sendRequest;
            tl_messages_search.offset_id = offset_id;
            if (n2 == 0) {
                tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterPhotoVideo();
            }
            else if (n2 == 1) {
                tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterDocument();
            }
            else if (n2 == 2) {
                tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterRoundVoice();
            }
            else if (n2 == 3) {
                tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterUrl();
            }
            else if (n2 == 4) {
                tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterMusic();
            }
            tl_messages_search.q = "";
            tl_messages_search.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(n5);
            if (tl_messages_search.peer == null) {
                return;
            }
            sendRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_search, new _$$Lambda$DataQuery$sZkwI2OJzSWG6qrblxHbvHglZsU(this, n, sendRequest, offset_id, n2, n4, b));
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(sendRequest, n4);
        }
        else {
            this.loadMediaDatabase(n, sendRequest, offset_id, n2, n4, b, n3);
        }
    }
    
    public void loadMusic(final long n, final long n2) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$cA_X50sc6geZKVWNtiHKllnXTb8(this, n, n2));
    }
    
    public MessageObject loadPinnedMessage(final long n, final int n2, final int n3, final boolean b) {
        if (b) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$hzk0ktoumSLB8vFJVERgSnj2uGI(this, n, n2, n3));
            return null;
        }
        return this.loadPinnedMessageInternal(n, n2, n3, true);
    }
    
    public void loadRecents(final int n, final boolean b, boolean b2, final boolean b3) {
        final boolean b4 = false;
        Label_0060: {
            if (b) {
                if (this.loadingRecentGifs) {
                    return;
                }
                this.loadingRecentGifs = true;
                if (!this.recentGifsLoaded) {
                    break Label_0060;
                }
            }
            else {
                final boolean[] loadingRecentStickers = this.loadingRecentStickers;
                if (loadingRecentStickers[n]) {
                    return;
                }
                loadingRecentStickers[n] = true;
                if (!this.recentStickersLoaded[n]) {
                    break Label_0060;
                }
            }
            b2 = false;
        }
        if (b2) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$vDbI4SDDFGBUSoYXXeeYvbjHYJY(this, b, n));
        }
        else {
            final SharedPreferences emojiSettings = MessagesController.getEmojiSettings(this.currentAccount);
            if (!b3) {
                long n2;
                if (b) {
                    n2 = emojiSettings.getLong("lastGifLoadTime", 0L);
                }
                else if (n == 0) {
                    n2 = emojiSettings.getLong("lastStickersLoadTime", 0L);
                }
                else if (n == 1) {
                    n2 = emojiSettings.getLong("lastStickersLoadTimeMask", 0L);
                }
                else {
                    n2 = emojiSettings.getLong("lastStickersLoadTimeFavs", 0L);
                }
                if (Math.abs(System.currentTimeMillis() - n2) < 3600000L) {
                    if (b) {
                        this.loadingRecentGifs = false;
                    }
                    else {
                        this.loadingRecentStickers[n] = false;
                    }
                    return;
                }
            }
            if (b) {
                final TLRPC.TL_messages_getSavedGifs tl_messages_getSavedGifs = new TLRPC.TL_messages_getSavedGifs();
                tl_messages_getSavedGifs.hash = calcDocumentsHash(this.recentGifs);
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getSavedGifs, new _$$Lambda$DataQuery$5cEA9KV_G5ibi7bkh4w7n6aFdJ8(this, n, b));
            }
            else {
                TLObject tlObject;
                if (n == 2) {
                    tlObject = new TLRPC.TL_messages_getFavedStickers();
                    ((TLRPC.TL_messages_getFavedStickers)tlObject).hash = calcDocumentsHash(this.recentStickers[n]);
                }
                else {
                    tlObject = new TLRPC.TL_messages_getRecentStickers();
                    ((TLRPC.TL_messages_getRecentStickers)tlObject).hash = calcDocumentsHash(this.recentStickers[n]);
                    boolean attached = b4;
                    if (n == 1) {
                        attached = true;
                    }
                    ((TLRPC.TL_messages_getRecentStickers)tlObject).attached = attached;
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tlObject, new _$$Lambda$DataQuery$5qGaDi2FRyj1QCnrOleMSVLz1k4(this, n, b));
            }
        }
    }
    
    public void loadReplyMessagesForMessages(final ArrayList<MessageObject> list, final long n) {
        final int n2 = (int)n;
        int i = 0;
        int j = 0;
        if (n2 == 0) {
            final ArrayList<Long> list2 = new ArrayList<Long>();
            final LongSparseArray longSparseArray = new LongSparseArray();
            while (j < list.size()) {
                final MessageObject e = list.get(j);
                if (e.isReply() && e.replyMessageObject == null) {
                    final long reply_to_random_id = e.messageOwner.reply_to_random_id;
                    ArrayList<MessageObject> list3;
                    if ((list3 = (ArrayList<MessageObject>)longSparseArray.get(reply_to_random_id)) == null) {
                        list3 = new ArrayList<MessageObject>();
                        longSparseArray.put(reply_to_random_id, (Object)list3);
                    }
                    list3.add(e);
                    if (!list2.contains(reply_to_random_id)) {
                        list2.add(reply_to_random_id);
                    }
                }
                ++j;
            }
            if (list2.isEmpty()) {
                return;
            }
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$WzRDIT94H3f0FgoSArkqNqup9dc(this, list2, n, longSparseArray));
        }
        else {
            final ArrayList<Integer> list4 = new ArrayList<Integer>();
            final SparseArray sparseArray = new SparseArray();
            final StringBuilder sb = new StringBuilder();
            int n3 = 0;
            while (i < list.size()) {
                final MessageObject e2 = list.get(i);
                int n4 = n3;
                if (e2.getId() > 0) {
                    n4 = n3;
                    if (e2.isReply()) {
                        n4 = n3;
                        if (e2.replyMessageObject == null) {
                            final TLRPC.Message messageOwner = e2.messageOwner;
                            final int reply_to_msg_id = messageOwner.reply_to_msg_id;
                            final long n5 = reply_to_msg_id;
                            final int channel_id = messageOwner.to_id.channel_id;
                            long lng = n5;
                            if (channel_id != 0) {
                                lng = (n5 | (long)channel_id << 32);
                                n3 = channel_id;
                            }
                            if (sb.length() > 0) {
                                sb.append(',');
                            }
                            sb.append(lng);
                            ArrayList<MessageObject> list5;
                            if ((list5 = (ArrayList<MessageObject>)sparseArray.get(reply_to_msg_id)) == null) {
                                list5 = new ArrayList<MessageObject>();
                                sparseArray.put(reply_to_msg_id, (Object)list5);
                            }
                            list5.add(e2);
                            n4 = n3;
                            if (!list4.contains(reply_to_msg_id)) {
                                list4.add(reply_to_msg_id);
                                n4 = n3;
                            }
                        }
                    }
                }
                ++i;
                n3 = n4;
            }
            if (list4.isEmpty()) {
                return;
            }
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$BoxhS9ivFCv8Vy62eC0FaKjPf8o(this, sb, n, list4, sparseArray, n3));
        }
    }
    
    public void loadStickers(final int n, final boolean b, final boolean b2) {
        if (this.loadingStickers[n]) {
            return;
        }
        if (n == 3) {
            if (this.featuredStickerSets.isEmpty() || !MessagesController.getInstance(this.currentAccount).preloadFeaturedStickers) {
                return;
            }
        }
        else {
            this.loadArchivedStickersCount(n, b);
        }
        this.loadingStickers[n] = true;
        if (b) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$xa4Sj3vXQ9Lm0vsRxYjFZWyzhng(this, n));
        }
        else {
            final int n2 = 0;
            final int n3 = 0;
            int i = 0;
            if (n == 3) {
                final TLRPC.TL_messages_allStickers tl_messages_allStickers = new TLRPC.TL_messages_allStickers();
                tl_messages_allStickers.hash = this.loadFeaturedHash;
                while (i < this.featuredStickerSets.size()) {
                    tl_messages_allStickers.sets.add(this.featuredStickerSets.get(i).set);
                    ++i;
                }
                this.processLoadStickersResponse(n, tl_messages_allStickers);
                return;
            }
            TLObject tlObject;
            int n4;
            if (n == 0) {
                tlObject = new TLRPC.TL_messages_getAllStickers();
                if (b2) {
                    n4 = n2;
                }
                else {
                    n4 = this.loadHash[n];
                }
                ((TLRPC.TL_messages_getAllStickers)tlObject).hash = n4;
            }
            else {
                tlObject = new TLRPC.TL_messages_getMaskStickers();
                if (b2) {
                    n4 = n3;
                }
                else {
                    n4 = this.loadHash[n];
                }
                ((TLRPC.TL_messages_getMaskStickers)tlObject).hash = n4;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tlObject, new _$$Lambda$DataQuery$rOQjG3YS0y6BqT7zHxJCSwr131o(this, n, n4));
        }
    }
    
    public void markFaturedStickersAsRead(final boolean b) {
        if (this.unreadStickerSets.isEmpty()) {
            return;
        }
        this.unreadStickerSets.clear();
        this.loadFeaturedHash = this.calcFeaturedStickersHash(this.featuredStickerSets);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.featuredStickersDidLoad, new Object[0]);
        this.putFeaturedStickersToCache(this.featuredStickerSets, this.unreadStickerSets, this.loadFeaturedDate, this.loadFeaturedHash);
        if (b) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_messages_readFeaturedStickers(), (RequestDelegate)_$$Lambda$DataQuery$TygWBWHmoEKh7kTWN3Sp59N9f8M.INSTANCE);
        }
    }
    
    public void markFaturedStickersByIdAsRead(final long n) {
        if (this.unreadStickerSets.contains(n)) {
            if (!this.readingStickerSets.contains(n)) {
                this.readingStickerSets.add(n);
                final TLRPC.TL_messages_readFeaturedStickers tl_messages_readFeaturedStickers = new TLRPC.TL_messages_readFeaturedStickers();
                tl_messages_readFeaturedStickers.id.add(n);
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_readFeaturedStickers, (RequestDelegate)_$$Lambda$DataQuery$CoOuaI5Ui4g_M8Hv23Yz8tRYQcY.INSTANCE);
                AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$g0o9Z_244wbMtOYxpACC2qzITTc(this, n), 1000L);
            }
        }
    }
    
    protected void processLoadedRecentDocuments(final int n, final ArrayList<TLRPC.Document> list, final boolean b, final int n2, final boolean b2) {
        if (list != null) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$uuHSgHUH35SvbaUdMusqTnQ_iPk(this, b, n, list, b2, n2));
        }
        if (n2 == 0) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$7M05Rnf_3mDk_ioxbIIY86tv1DE(this, b, n, list));
        }
    }
    
    public void putBotInfo(final TLRPC.BotInfo botInfo) {
        if (botInfo == null) {
            return;
        }
        this.botInfos.put(botInfo.user_id, (Object)botInfo);
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$Z9uUIzBqjWHBx6zjrK5T9uXZVkc(this, botInfo));
    }
    
    public void putBotKeyboard(final long l, final TLRPC.Message message) {
        if (message == null) {
            return;
        }
        try {
            final SQLiteDatabase database = MessagesStorage.getInstance(this.currentAccount).getDatabase();
            final Locale us = Locale.US;
            int intValue = 0;
            final SQLiteCursor queryFinalized = database.queryFinalized(String.format(us, "SELECT mid FROM bot_keyboard WHERE uid = %d", l), new Object[0]);
            if (queryFinalized.next()) {
                intValue = queryFinalized.intValue(0);
            }
            queryFinalized.dispose();
            if (intValue >= message.id) {
                return;
            }
            final SQLitePreparedStatement executeFast = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO bot_keyboard VALUES(?, ?, ?)");
            executeFast.requery();
            final NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(message.getObjectSize());
            message.serializeToStream(nativeByteBuffer);
            executeFast.bindLong(1, l);
            executeFast.bindInteger(2, message.id);
            executeFast.bindByteBuffer(3, nativeByteBuffer);
            executeFast.step();
            nativeByteBuffer.reuse();
            executeFast.dispose();
            AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$g5fV5AzEjpYa9k_FQikVmx9CWxY(this, l, message));
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void putGroupStickerSet(final TLRPC.TL_messages_stickerSet set) {
        this.groupStickerSets.put(set.set.id, (Object)set);
    }
    
    public void removeInline(final int n) {
        for (int i = 0; i < this.inlineBots.size(); ++i) {
            if (this.inlineBots.get(i).peer.user_id == n) {
                this.inlineBots.remove(i);
                final TLRPC.TL_contacts_resetTopPeerRating tl_contacts_resetTopPeerRating = new TLRPC.TL_contacts_resetTopPeerRating();
                tl_contacts_resetTopPeerRating.category = new TLRPC.TL_topPeerCategoryBotsInline();
                tl_contacts_resetTopPeerRating.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(n);
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_contacts_resetTopPeerRating, (RequestDelegate)_$$Lambda$DataQuery$nEW3BBG5NIOAbnpoGoJ7S4IYot0.INSTANCE);
                this.deletePeer(n, 1);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadInlineHints, new Object[0]);
                return;
            }
        }
    }
    
    public void removePeer(final int n) {
        for (int i = 0; i < this.hints.size(); ++i) {
            if (this.hints.get(i).peer.user_id == n) {
                this.hints.remove(i);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadHints, new Object[0]);
                final TLRPC.TL_contacts_resetTopPeerRating tl_contacts_resetTopPeerRating = new TLRPC.TL_contacts_resetTopPeerRating();
                tl_contacts_resetTopPeerRating.category = new TLRPC.TL_topPeerCategoryCorrespondents();
                tl_contacts_resetTopPeerRating.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(n);
                this.deletePeer(n, 0);
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_contacts_resetTopPeerRating, (RequestDelegate)_$$Lambda$DataQuery$eHV0FXEJJkBEZzT5FGLWr_l3mT8.INSTANCE);
                return;
            }
        }
    }
    
    public void removeRecentGif(final TLRPC.Document o) {
        this.recentGifs.remove(o);
        final TLRPC.TL_messages_saveGif tl_messages_saveGif = new TLRPC.TL_messages_saveGif();
        tl_messages_saveGif.id = new TLRPC.TL_inputDocument();
        final TLRPC.InputDocument id = tl_messages_saveGif.id;
        id.id = o.id;
        id.access_hash = o.access_hash;
        id.file_reference = o.file_reference;
        if (id.file_reference == null) {
            id.file_reference = new byte[0];
        }
        tl_messages_saveGif.unsave = true;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_saveGif, new _$$Lambda$DataQuery$2rJc9xeBmgwGexV2v1U_RebLTns(this, tl_messages_saveGif));
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$_3R_0wEE3expP9wo0aColXdZCOk(this, o));
    }
    
    public void removeStickersSet(final Context context, final TLRPC.StickerSet set, final int n, final BaseFragment baseFragment, final boolean b) {
        final int masks = set.masks ? 1 : 0;
        final TLRPC.TL_inputStickerSetID tl_inputStickerSetID = new TLRPC.TL_inputStickerSetID();
        tl_inputStickerSetID.access_hash = set.access_hash;
        tl_inputStickerSetID.id = set.id;
        if (n != 0) {
            final boolean b2 = false;
            set.archived = (n == 1);
            int i = 0;
            while (i < this.stickerSets[masks].size()) {
                final TLRPC.TL_messages_stickerSet element = this.stickerSets[masks].get(i);
                if (element.set.id == set.id) {
                    this.stickerSets[masks].remove(i);
                    if (n == 2) {
                        this.stickerSets[masks].add(0, element);
                        break;
                    }
                    this.stickerSetsById.remove(element.set.id);
                    this.installedStickerSetsById.remove(element.set.id);
                    this.stickerSetsByName.remove(element.set.short_name);
                    break;
                }
                else {
                    ++i;
                }
            }
            this.loadHash[masks] = calcStickersHash(this.stickerSets[masks]);
            this.putStickersToCache(masks, this.stickerSets[masks], this.loadDate[masks], this.loadHash[masks]);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.stickersDidLoad, masks);
            final TLRPC.TL_messages_installStickerSet set2 = new TLRPC.TL_messages_installStickerSet();
            set2.stickerset = tl_inputStickerSetID;
            boolean archived = b2;
            if (n == 1) {
                archived = true;
            }
            set2.archived = archived;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(set2, new _$$Lambda$DataQuery$qjcWF_Rlu499xbvvlZfzwoJxsQI(this, masks, n, baseFragment, b));
        }
        else {
            final TLRPC.TL_messages_uninstallStickerSet set3 = new TLRPC.TL_messages_uninstallStickerSet();
            set3.stickerset = tl_inputStickerSetID;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(set3, new _$$Lambda$DataQuery$DpM7Y4tX6aFYwIY3CiDaFq5CjNE(this, set, context, masks));
        }
    }
    
    public void reorderStickers(final int i, final ArrayList<Long> list) {
        Collections.sort(this.stickerSets[i], new _$$Lambda$DataQuery$dtOJW5lUpjPVOn6aZw8KSWcnFQs(list));
        this.loadHash[i] = calcStickersHash(this.stickerSets[i]);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.stickersDidLoad, i);
        this.loadStickers(i, false, true);
    }
    
    public void replaceStickerSet(final TLRPC.TL_messages_stickerSet set) {
        TLRPC.TL_messages_stickerSet set2;
        if ((set2 = (TLRPC.TL_messages_stickerSet)this.stickerSetsById.get(set.set.id)) == null) {
            set2 = this.stickerSetsByName.get(set.set.short_name);
        }
        final int n = 0;
        TLRPC.TL_messages_stickerSet set3 = null;
        boolean b = false;
        Label_0086: {
            if ((set3 = set2) == null) {
                final TLRPC.TL_messages_stickerSet set4 = (TLRPC.TL_messages_stickerSet)this.groupStickerSets.get(set.set.id);
                if ((set3 = set4) != null) {
                    b = true;
                    set3 = set4;
                    break Label_0086;
                }
            }
            b = false;
        }
        if (set3 == null) {
            return;
        }
        final LongSparseArray longSparseArray = new LongSparseArray();
        for (int size = set.documents.size(), i = 0; i < size; ++i) {
            final TLRPC.Document document = set.documents.get(i);
            longSparseArray.put(document.id, (Object)document);
        }
        final int size2 = set3.documents.size();
        boolean b2 = false;
        for (int j = n; j < size2; ++j) {
            final TLRPC.Document element = (TLRPC.Document)longSparseArray.get(set.documents.get(j).id);
            if (element != null) {
                set3.documents.set(j, element);
                b2 = true;
            }
        }
        if (b2) {
            if (b) {
                this.putSetToCache(set3);
            }
            else {
                final int masks = set.set.masks ? 1 : 0;
                this.putStickersToCache(masks, this.stickerSets[masks], this.loadDate[masks], this.loadHash[masks]);
            }
        }
    }
    
    public void saveDraft(final long n, final CharSequence charSequence, final ArrayList<TLRPC.MessageEntity> list, final TLRPC.Message message, final boolean b) {
        this.saveDraft(n, charSequence, list, message, b, false);
    }
    
    public void saveDraft(final long n, final CharSequence charSequence, final ArrayList<TLRPC.MessageEntity> entities, final TLRPC.Message message, final boolean no_webpage, final boolean b) {
        TLRPC.DraftMessage draftMessage;
        if (TextUtils.isEmpty(charSequence) && message == null) {
            draftMessage = new TLRPC.TL_draftMessageEmpty();
        }
        else {
            draftMessage = new TLRPC.TL_draftMessage();
        }
        draftMessage.date = (int)(System.currentTimeMillis() / 1000L);
        String string;
        if (charSequence == null) {
            string = "";
        }
        else {
            string = charSequence.toString();
        }
        draftMessage.message = string;
        draftMessage.no_webpage = no_webpage;
        if (message != null) {
            draftMessage.reply_to_msg_id = message.id;
            draftMessage.flags |= 0x1;
        }
        if (entities != null && !entities.isEmpty()) {
            draftMessage.entities = entities;
            draftMessage.flags |= 0x8;
        }
        final TLRPC.DraftMessage draftMessage2 = (TLRPC.DraftMessage)this.drafts.get(n);
        if (!b && ((draftMessage2 != null && draftMessage2.message.equals(draftMessage.message) && draftMessage2.reply_to_msg_id == draftMessage.reply_to_msg_id && draftMessage2.no_webpage == draftMessage.no_webpage) || (draftMessage2 == null && TextUtils.isEmpty((CharSequence)draftMessage.message) && draftMessage.reply_to_msg_id == 0))) {
            return;
        }
        this.saveDraft(n, draftMessage, message, false);
        final int n2 = (int)n;
        if (n2 != 0) {
            final TLRPC.TL_messages_saveDraft tl_messages_saveDraft = new TLRPC.TL_messages_saveDraft();
            tl_messages_saveDraft.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(n2);
            if (tl_messages_saveDraft.peer == null) {
                return;
            }
            tl_messages_saveDraft.message = draftMessage.message;
            tl_messages_saveDraft.no_webpage = draftMessage.no_webpage;
            tl_messages_saveDraft.reply_to_msg_id = draftMessage.reply_to_msg_id;
            tl_messages_saveDraft.entities = draftMessage.entities;
            tl_messages_saveDraft.flags = draftMessage.flags;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_saveDraft, (RequestDelegate)_$$Lambda$DataQuery$jWB8qVUldoWOaeSwZryuPoQq0I0.INSTANCE);
        }
        MessagesController.getInstance(this.currentAccount).sortDialogs(null);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
    }
    
    public void saveDraft(final long n, final TLRPC.DraftMessage draftMessage, final TLRPC.Message message, final boolean b) {
        final SharedPreferences$Editor edit = this.preferences.edit();
        if (draftMessage != null && !(draftMessage instanceof TLRPC.TL_draftMessageEmpty)) {
            this.drafts.put(n, (Object)draftMessage);
            try {
                final SerializedData serializedData = new SerializedData(draftMessage.getObjectSize());
                draftMessage.serializeToStream(serializedData);
                final StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(n);
                edit.putString(sb.toString(), Utilities.bytesToHex(serializedData.toByteArray()));
                serializedData.cleanup();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        else {
            this.drafts.remove(n);
            this.draftMessages.remove(n);
            final SharedPreferences$Editor edit2 = this.preferences.edit();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(n);
            final SharedPreferences$Editor remove = edit2.remove(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("r_");
            sb3.append(n);
            remove.remove(sb3.toString()).commit();
        }
        if (message == null) {
            this.draftMessages.remove(n);
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("r_");
            sb4.append(n);
            edit.remove(sb4.toString());
        }
        else {
            this.draftMessages.put(n, (Object)message);
            final SerializedData serializedData2 = new SerializedData(message.getObjectSize());
            message.serializeToStream(serializedData2);
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("r_");
            sb5.append(n);
            edit.putString(sb5.toString(), Utilities.bytesToHex(serializedData2.toByteArray()));
            serializedData2.cleanup();
        }
        edit.commit();
        if (b) {
            if (draftMessage.reply_to_msg_id != 0 && message == null) {
                final int i = (int)n;
                TLRPC.Chat chat = null;
                TLRPC.User user;
                if (i > 0) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(i);
                }
                else {
                    chat = MessagesController.getInstance(this.currentAccount).getChat(-i);
                    user = null;
                }
                if (user != null || chat != null) {
                    long n2 = draftMessage.reply_to_msg_id;
                    int id;
                    if (ChatObject.isChannel(chat)) {
                        id = chat.id;
                        n2 |= (long)id << 32;
                    }
                    else {
                        id = 0;
                    }
                    MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$JgCeTQWWzbO2EPbV9fjIFzenxxY(this, n2, id, n));
                }
            }
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.newDraftReceived, n);
        }
    }
    
    public void searchMessagesInChat(final String s, final long n, final long n2, final int n3, final int n4, final TLRPC.User user) {
        this.searchMessagesInChat(s, n, n2, n3, n4, false, user);
    }
    
    public CharSequence substring(final CharSequence charSequence, final int n, final int n2) {
        if (charSequence instanceof SpannableStringBuilder) {
            return charSequence.subSequence(n, n2);
        }
        if (charSequence instanceof SpannedString) {
            return charSequence.subSequence(n, n2);
        }
        return TextUtils.substring(charSequence, n, n2);
    }
    
    public void uninstallShortcut(final long lng) {
        try {
            if (Build$VERSION.SDK_INT >= 26) {
                final ShortcutManager shortcutManager = (ShortcutManager)ApplicationLoader.applicationContext.getSystemService((Class)ShortcutManager.class);
                final ArrayList<String> list = new ArrayList<String>();
                final StringBuilder sb = new StringBuilder();
                sb.append("sdid_");
                sb.append(lng);
                list.add(sb.toString());
                shortcutManager.removeDynamicShortcuts((List)list);
            }
            else {
                final int i = (int)lng;
                final int j = (int)(lng >> 32);
                TLRPC.Chat chat = null;
                TLRPC.User user;
                if (i == 0) {
                    final TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(j);
                    if (encryptedChat == null) {
                        return;
                    }
                    user = MessagesController.getInstance(this.currentAccount).getUser(encryptedChat.user_id);
                }
                else if (i > 0) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(i);
                }
                else {
                    if (i >= 0) {
                        return;
                    }
                    chat = MessagesController.getInstance(this.currentAccount).getChat(-i);
                    user = null;
                }
                if (user == null && chat == null) {
                    return;
                }
                String s;
                if (user != null) {
                    s = ContactsController.formatName(user.first_name, user.last_name);
                }
                else {
                    s = chat.title;
                }
                final Intent intent = new Intent();
                intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)this.createIntrnalShortcutIntent(lng));
                intent.putExtra("android.intent.extra.shortcut.NAME", s);
                intent.putExtra("duplicate", false);
                intent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
                ApplicationLoader.applicationContext.sendBroadcast(intent);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static class KeywordResult
    {
        public String emoji;
        public String keyword;
    }
    
    public interface KeywordResultCallback
    {
        void run(final ArrayList<KeywordResult> p0, final String p1);
    }
}
