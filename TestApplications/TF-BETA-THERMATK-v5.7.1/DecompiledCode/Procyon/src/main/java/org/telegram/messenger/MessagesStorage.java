// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import java.util.Comparator;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import android.text.TextUtils;
import java.util.Collection;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.messenger.support.SparseLongArray;
import java.util.List;
import android.util.SparseIntArray;
import android.util.SparseArray;
import android.util.LongSparseArray;
import org.telegram.SQLite.SQLitePreparedStatement;
import java.util.Locale;
import org.telegram.SQLite.SQLiteCursor;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import org.telegram.SQLite.SQLiteDatabase;
import java.io.File;

public class MessagesStorage
{
    private static volatile MessagesStorage[] Instance;
    private static final int LAST_DB_VERSION = 60;
    private File cacheFile;
    private int currentAccount;
    private SQLiteDatabase database;
    private int lastDateValue;
    private int lastPtsValue;
    private int lastQtsValue;
    private int lastSavedDate;
    private int lastSavedPts;
    private int lastSavedQts;
    private int lastSavedSeq;
    private int lastSecretVersion;
    private int lastSeqValue;
    private AtomicLong lastTaskId;
    private CountDownLatch openSync;
    private int secretG;
    private byte[] secretPBytes;
    private File shmCacheFile;
    private DispatchQueue storageQueue;
    private File walCacheFile;
    
    static {
        MessagesStorage.Instance = new MessagesStorage[3];
    }
    
    public MessagesStorage(final int currentAccount) {
        this.storageQueue = new DispatchQueue("storageQueue");
        this.lastTaskId = new AtomicLong(System.currentTimeMillis());
        this.lastDateValue = 0;
        this.lastPtsValue = 0;
        this.lastQtsValue = 0;
        this.lastSeqValue = 0;
        this.lastSecretVersion = 0;
        this.secretPBytes = null;
        this.secretG = 0;
        this.lastSavedSeq = 0;
        this.lastSavedPts = 0;
        this.lastSavedDate = 0;
        this.lastSavedQts = 0;
        this.openSync = new CountDownLatch(1);
        this.currentAccount = currentAccount;
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$l_EMBf4E_fIloSDlPGVY7ULYtZw(this));
    }
    
    public static void addUsersAndChatsFromMessage(final TLRPC.Message message, final ArrayList<Integer> list, final ArrayList<Integer> list2) {
        final int from_id = message.from_id;
        if (from_id != 0) {
            if (from_id > 0) {
                if (!list.contains(from_id)) {
                    list.add(message.from_id);
                }
            }
            else if (!list2.contains(-from_id)) {
                list2.add(-message.from_id);
            }
        }
        final int via_bot_id = message.via_bot_id;
        if (via_bot_id != 0 && !list.contains(via_bot_id)) {
            list.add(message.via_bot_id);
        }
        final TLRPC.MessageAction action = message.action;
        final int n = 0;
        if (action != null) {
            final int user_id = action.user_id;
            if (user_id != 0 && !list.contains(user_id)) {
                list.add(message.action.user_id);
            }
            final int channel_id = message.action.channel_id;
            if (channel_id != 0 && !list2.contains(channel_id)) {
                list2.add(message.action.channel_id);
            }
            final int chat_id = message.action.chat_id;
            if (chat_id != 0 && !list2.contains(chat_id)) {
                list2.add(message.action.chat_id);
            }
            if (!message.action.users.isEmpty()) {
                for (int i = 0; i < message.action.users.size(); ++i) {
                    final Integer n2 = message.action.users.get(i);
                    if (!list.contains(n2)) {
                        list.add(n2);
                    }
                }
            }
        }
        if (!message.entities.isEmpty()) {
            for (int j = n; j < message.entities.size(); ++j) {
                final TLRPC.MessageEntity messageEntity = message.entities.get(j);
                if (messageEntity instanceof TLRPC.TL_messageEntityMentionName) {
                    list.add(((TLRPC.TL_messageEntityMentionName)messageEntity).user_id);
                }
                else if (messageEntity instanceof TLRPC.TL_inputMessageEntityMentionName) {
                    list.add(((TLRPC.TL_inputMessageEntityMentionName)messageEntity).user_id.user_id);
                }
            }
        }
        final TLRPC.MessageMedia media = message.media;
        if (media != null) {
            final int user_id2 = media.user_id;
            if (user_id2 != 0 && !list.contains(user_id2)) {
                list.add(message.media.user_id);
            }
        }
        final TLRPC.MessageFwdHeader fwd_from = message.fwd_from;
        if (fwd_from != null) {
            final int from_id2 = fwd_from.from_id;
            if (from_id2 != 0 && !list.contains(from_id2)) {
                list.add(message.fwd_from.from_id);
            }
            final int channel_id2 = message.fwd_from.channel_id;
            if (channel_id2 != 0 && !list2.contains(channel_id2)) {
                list2.add(message.fwd_from.channel_id);
            }
            final TLRPC.Peer saved_from_peer = message.fwd_from.saved_from_peer;
            if (saved_from_peer != null) {
                final int user_id3 = saved_from_peer.user_id;
                if (user_id3 != 0) {
                    if (!list2.contains(user_id3)) {
                        list.add(message.fwd_from.saved_from_peer.user_id);
                    }
                }
                else {
                    final int channel_id3 = saved_from_peer.channel_id;
                    if (channel_id3 != 0) {
                        if (!list2.contains(channel_id3)) {
                            list2.add(message.fwd_from.saved_from_peer.channel_id);
                        }
                    }
                    else {
                        final int chat_id2 = saved_from_peer.chat_id;
                        if (chat_id2 != 0 && !list2.contains(chat_id2)) {
                            list2.add(message.fwd_from.saved_from_peer.chat_id);
                        }
                    }
                }
            }
        }
        final int ttl = message.ttl;
        if (ttl < 0 && !list2.contains(-ttl)) {
            list2.add(-message.ttl);
        }
    }
    
    private void checkIfFolderEmptyInternal(final int i) {
        try {
            final SQLiteCursor queryFinalized = this.database.queryFinalized("SELECT did FROM dialogs WHERE folder_id = ?", i);
            if (!queryFinalized.next()) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesStorage$PjPiLXNm6_EsiAsuc_msXC__TJg(this, i));
                final SQLiteDatabase database = this.database;
                final StringBuilder sb = new StringBuilder();
                sb.append("DELETE FROM dialogs WHERE did = ");
                sb.append(DialogObject.makeFolderDialogId(i));
                database.executeFast(sb.toString()).stepThis().dispose();
            }
            queryFinalized.dispose();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private void cleanupInternal(final boolean b) {
        this.lastDateValue = 0;
        this.lastSeqValue = 0;
        this.lastPtsValue = 0;
        this.lastQtsValue = 0;
        this.lastSecretVersion = 0;
        this.lastSavedSeq = 0;
        this.lastSavedPts = 0;
        this.lastSavedDate = 0;
        this.lastSavedQts = 0;
        this.secretPBytes = null;
        this.secretG = 0;
        final SQLiteDatabase database = this.database;
        if (database != null) {
            database.close();
            this.database = null;
        }
        if (b) {
            final File cacheFile = this.cacheFile;
            if (cacheFile != null) {
                cacheFile.delete();
                this.cacheFile = null;
            }
            final File walCacheFile = this.walCacheFile;
            if (walCacheFile != null) {
                walCacheFile.delete();
                this.walCacheFile = null;
            }
            final File shmCacheFile = this.shmCacheFile;
            if (shmCacheFile != null) {
                shmCacheFile.delete();
                this.shmCacheFile = null;
            }
        }
    }
    
    private void closeHolesInTable(final String s, final long l, final int i, final int j) {
        try {
            final SQLiteDatabase database = this.database;
            final Locale us = Locale.US;
            final StringBuilder sb = new StringBuilder();
            sb.append("SELECT start, end FROM ");
            sb.append(s);
            sb.append(" WHERE uid = %d AND ((end >= %d AND end <= %d) OR (start >= %d AND start <= %d) OR (start >= %d AND end <= %d) OR (start <= %d AND end >= %d))");
            final SQLiteCursor queryFinalized = database.queryFinalized(String.format(us, sb.toString(), l, i, j, i, j, i, j, i, j), new Object[0]);
            ArrayList<Hole> list = null;
            while (queryFinalized.next()) {
                ArrayList<Hole> list2;
                if ((list2 = list) == null) {
                    list2 = new ArrayList<Hole>();
                }
                final int intValue = queryFinalized.intValue(0);
                final int intValue2 = queryFinalized.intValue(1);
                if (intValue == intValue2 && intValue == 1) {
                    list = list2;
                }
                else {
                    list2.add(new Hole(intValue, intValue2));
                    list = list2;
                }
            }
            queryFinalized.dispose();
            if (list != null) {
                for (int k = 0; k < list.size(); ++k) {
                    final Hole hole = list.get(k);
                    if (j >= hole.end - 1 && i <= hole.start + 1) {
                        final SQLiteDatabase database2 = this.database;
                        final Locale us2 = Locale.US;
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("DELETE FROM ");
                        sb2.append(s);
                        sb2.append(" WHERE uid = %d AND start = %d AND end = %d");
                        database2.executeFast(String.format(us2, sb2.toString(), l, hole.start, hole.end)).stepThis().dispose();
                    }
                    else if (j >= hole.end - 1) {
                        if (hole.end != i) {
                            try {
                                final SQLiteDatabase database3 = this.database;
                                final Locale us3 = Locale.US;
                                final StringBuilder sb3 = new StringBuilder();
                                sb3.append("UPDATE ");
                                sb3.append(s);
                                sb3.append(" SET end = %d WHERE uid = %d AND start = %d AND end = %d");
                                database3.executeFast(String.format(us3, sb3.toString(), i, l, hole.start, hole.end)).stepThis().dispose();
                            }
                            catch (Exception ex) {
                                FileLog.e(ex);
                            }
                        }
                    }
                    else if (i <= hole.start + 1) {
                        if (hole.start != j) {
                            try {
                                final SQLiteDatabase database4 = this.database;
                                final Locale us4 = Locale.US;
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append("UPDATE ");
                                sb4.append(s);
                                sb4.append(" SET start = %d WHERE uid = %d AND start = %d AND end = %d");
                                database4.executeFast(String.format(us4, sb4.toString(), j, l, hole.start, hole.end)).stepThis().dispose();
                            }
                            catch (Exception ex2) {
                                FileLog.e(ex2);
                            }
                        }
                    }
                    else {
                        final SQLiteDatabase database5 = this.database;
                        final Locale us5 = Locale.US;
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("DELETE FROM ");
                        sb5.append(s);
                        sb5.append(" WHERE uid = %d AND start = %d AND end = %d");
                        database5.executeFast(String.format(us5, sb5.toString(), l, hole.start, hole.end)).stepThis().dispose();
                        final SQLiteDatabase database6 = this.database;
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("REPLACE INTO ");
                        sb6.append(s);
                        sb6.append(" VALUES(?, ?, ?)");
                        final SQLitePreparedStatement executeFast = database6.executeFast(sb6.toString());
                        executeFast.requery();
                        executeFast.bindLong(1, l);
                        executeFast.bindInteger(2, hole.start);
                        executeFast.bindInteger(3, i);
                        executeFast.step();
                        executeFast.requery();
                        executeFast.bindLong(1, l);
                        executeFast.bindInteger(2, j);
                        executeFast.bindInteger(3, hole.end);
                        executeFast.step();
                        executeFast.dispose();
                    }
                }
            }
        }
        catch (Exception ex3) {
            FileLog.e(ex3);
        }
    }
    
    public static void createFirstHoles(final long n, final SQLitePreparedStatement sqLitePreparedStatement, final SQLitePreparedStatement sqLitePreparedStatement2, final int n2) throws Exception {
        sqLitePreparedStatement.requery();
        sqLitePreparedStatement.bindLong(1, n);
        int n3;
        if (n2 == 1) {
            n3 = 1;
        }
        else {
            n3 = 0;
        }
        sqLitePreparedStatement.bindInteger(2, n3);
        sqLitePreparedStatement.bindInteger(3, n2);
        sqLitePreparedStatement.step();
        for (int i = 0; i < 5; ++i) {
            sqLitePreparedStatement2.requery();
            sqLitePreparedStatement2.bindLong(1, n);
            sqLitePreparedStatement2.bindInteger(2, i);
            int n4;
            if (n2 == 1) {
                n4 = 1;
            }
            else {
                n4 = 0;
            }
            sqLitePreparedStatement2.bindInteger(3, n4);
            sqLitePreparedStatement2.bindInteger(4, n2);
            sqLitePreparedStatement2.step();
        }
    }
    
    private void doneHolesInTable(final String str, final long n, final int n2) throws Exception {
        if (n2 == 0) {
            final SQLiteDatabase database = this.database;
            final Locale us = Locale.US;
            final StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM ");
            sb.append(str);
            sb.append(" WHERE uid = %d");
            database.executeFast(String.format(us, sb.toString(), n)).stepThis().dispose();
        }
        else {
            final SQLiteDatabase database2 = this.database;
            final Locale us2 = Locale.US;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("DELETE FROM ");
            sb2.append(str);
            sb2.append(" WHERE uid = %d AND start = 0");
            database2.executeFast(String.format(us2, sb2.toString(), n)).stepThis().dispose();
        }
        final SQLiteDatabase database3 = this.database;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("REPLACE INTO ");
        sb3.append(str);
        sb3.append(" VALUES(?, ?, ?)");
        final SQLitePreparedStatement executeFast = database3.executeFast(sb3.toString());
        executeFast.requery();
        executeFast.bindLong(1, n);
        executeFast.bindInteger(2, 1);
        executeFast.bindInteger(3, 1);
        executeFast.step();
        executeFast.dispose();
    }
    
    private void ensureOpened() {
        try {
            this.openSync.await();
        }
        catch (Throwable t) {}
    }
    
    private void fixNotificationSettings() {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$H7ZjmdIrsBPnbbqD1cn5Lti86iY(this));
    }
    
    private void fixUnsupportedMedia(final TLRPC.Message message) {
        if (message == null) {
            return;
        }
        final TLRPC.MessageMedia media = message.media;
        if (media instanceof TLRPC.TL_messageMediaUnsupported_old) {
            if (media.bytes.length == 0) {
                (media.bytes = new byte[1])[0] = 100;
            }
        }
        else if (media instanceof TLRPC.TL_messageMediaUnsupported) {
            message.media = new TLRPC.TL_messageMediaUnsupported_old();
            (message.media.bytes = new byte[1])[0] = 100;
            message.flags |= 0x200;
        }
    }
    
    private String formatUserSearchName(final TLRPC.User user) {
        final StringBuilder sb = new StringBuilder();
        final String first_name = user.first_name;
        if (first_name != null && first_name.length() > 0) {
            sb.append(user.first_name);
        }
        final String last_name = user.last_name;
        if (last_name != null && last_name.length() > 0) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(user.last_name);
        }
        sb.append(";;;");
        final String username = user.username;
        if (username != null && username.length() > 0) {
            sb.append(user.username);
        }
        return sb.toString().toLowerCase();
    }
    
    public static MessagesStorage getInstance(final int n) {
        final MessagesStorage messagesStorage;
        if ((messagesStorage = MessagesStorage.Instance[n]) == null) {
            synchronized (MessagesStorage.class) {
                if (MessagesStorage.Instance[n] == null) {
                    MessagesStorage.Instance[n] = new MessagesStorage(n);
                }
            }
        }
        return messagesStorage;
    }
    
    private int getMessageMediaType(final TLRPC.Message message) {
        if (message instanceof TLRPC.TL_message_secret) {
            if (message.media instanceof TLRPC.TL_messageMediaPhoto || MessageObject.isGifMessage(message)) {
                final int ttl = message.ttl;
                if (ttl > 0 && ttl <= 60) {
                    return 1;
                }
            }
            if (!MessageObject.isVoiceMessage(message) && !MessageObject.isVideoMessage(message)) {
                if (!MessageObject.isRoundVideoMessage(message)) {
                    if (message.media instanceof TLRPC.TL_messageMediaPhoto || MessageObject.isVideoMessage(message)) {
                        return 0;
                    }
                    return -1;
                }
            }
            return 1;
        }
        if (message instanceof TLRPC.TL_message) {
            final TLRPC.MessageMedia media = message.media;
            if ((media instanceof TLRPC.TL_messageMediaPhoto || media instanceof TLRPC.TL_messageMediaDocument) && message.media.ttl_seconds != 0) {
                return 1;
            }
        }
        if (message.media instanceof TLRPC.TL_messageMediaPhoto || MessageObject.isVideoMessage(message)) {
            return 0;
        }
        return -1;
    }
    
    private static boolean isEmpty(final LongSparseArray<?> longSparseArray) {
        return longSparseArray == null || longSparseArray.size() == 0;
    }
    
    private static boolean isEmpty(final SparseArray<?> sparseArray) {
        return sparseArray == null || sparseArray.size() == 0;
    }
    
    private static boolean isEmpty(final SparseIntArray sparseIntArray) {
        return sparseIntArray == null || sparseIntArray.size() == 0;
    }
    
    private static boolean isEmpty(final List<?> list) {
        return list == null || list.isEmpty();
    }
    
    private static boolean isEmpty(final SparseLongArray sparseLongArray) {
        return sparseLongArray == null || sparseLongArray.size() == 0;
    }
    
    private boolean isValidKeyboardToSave(final TLRPC.Message message) {
        final TLRPC.ReplyMarkup reply_markup = message.reply_markup;
        return reply_markup != null && !(reply_markup instanceof TLRPC.TL_replyInlineMarkup) && (!reply_markup.selective || message.mentioned);
    }
    
    private void loadPendingTasks() {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Mh1_3ksiRZzSQICRWjWNRIrSkV4(this));
    }
    
    private ArrayList<Long> markMessagesAsDeletedInternal(int i, int n) {
        try {
            final ArrayList<Long> list = new ArrayList<Long>();
            final LongSparseArray longSparseArray = new LongSparseArray();
            final long l = (long)n | (long)i << 32;
            final ArrayList<File> list2 = new ArrayList<File>();
            n = UserConfig.getInstance(this.currentAccount).getClientUserId();
            final SQLiteDatabase database = this.database;
            final Locale us = Locale.US;
            final int n2 = -i;
            final SQLiteCursor queryFinalized = database.queryFinalized(String.format(us, "SELECT uid, data, read_state, out, mention FROM messages WHERE uid = %d AND mid <= %d", n2, l), new Object[0]);
            try {
                while (queryFinalized.next()) {
                    final long longValue = queryFinalized.longValue(0);
                    if (longValue == n) {
                        continue;
                    }
                    i = queryFinalized.intValue(2);
                    if (queryFinalized.intValue(3) == 0) {
                        Integer[] array;
                        if ((array = (Integer[])longSparseArray.get(longValue)) == null) {
                            array = new Integer[] { 0, 0 };
                            longSparseArray.put(longValue, (Object)array);
                        }
                        if (i < 2) {
                            final Integer n3 = array[1];
                            ++array[1];
                        }
                        if (i == 0 || i == 2) {
                            final Integer n4 = array[0];
                            ++array[0];
                        }
                    }
                    if ((int)longValue != 0) {
                        continue;
                    }
                    final NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(1);
                    if (byteBufferValue == null) {
                        continue;
                    }
                    final TLRPC.Message tLdeserialize = TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                    tLdeserialize.readAttachPath(byteBufferValue, UserConfig.getInstance(this.currentAccount).clientUserId);
                    byteBufferValue.reuse();
                    if (tLdeserialize == null) {
                        continue;
                    }
                    if (tLdeserialize.media instanceof TLRPC.TL_messageMediaPhoto) {
                        int size;
                        File pathToAttach;
                        for (size = tLdeserialize.media.photo.sizes.size(), i = 0; i < size; ++i) {
                            pathToAttach = FileLoader.getPathToAttach(tLdeserialize.media.photo.sizes.get(i));
                            if (pathToAttach != null && pathToAttach.toString().length() > 0) {
                                list2.add(pathToAttach);
                            }
                        }
                    }
                    else {
                        if (!(tLdeserialize.media instanceof TLRPC.TL_messageMediaDocument)) {
                            continue;
                        }
                        final File pathToAttach2 = FileLoader.getPathToAttach(tLdeserialize.media.document);
                        if (pathToAttach2 != null && pathToAttach2.toString().length() > 0) {
                            list2.add(pathToAttach2);
                        }
                        int size2;
                        File pathToAttach3;
                        for (size2 = tLdeserialize.media.document.thumbs.size(), i = 0; i < size2; ++i) {
                            pathToAttach3 = FileLoader.getPathToAttach(tLdeserialize.media.document.thumbs.get(i));
                            if (pathToAttach3 != null && pathToAttach3.toString().length() > 0) {
                                list2.add(pathToAttach3);
                            }
                        }
                    }
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            queryFinalized.dispose();
            FileLoader.getInstance(this.currentAccount).deleteFiles(list2, 0);
            long key;
            Integer[] array2;
            SQLiteDatabase database2;
            StringBuilder sb;
            SQLiteCursor queryFinalized2;
            int intValue;
            SQLitePreparedStatement executeFast;
            for (i = 0; i < longSparseArray.size(); ++i) {
                key = longSparseArray.keyAt(i);
                array2 = (Integer[])longSparseArray.valueAt(i);
                database2 = this.database;
                sb = new StringBuilder();
                sb.append("SELECT unread_count, unread_count_i FROM dialogs WHERE did = ");
                sb.append(key);
                queryFinalized2 = database2.queryFinalized(sb.toString(), new Object[0]);
                if (queryFinalized2.next()) {
                    n = queryFinalized2.intValue(0);
                    intValue = queryFinalized2.intValue(1);
                }
                else {
                    n = 0;
                    intValue = 0;
                }
                queryFinalized2.dispose();
                list.add(key);
                executeFast = this.database.executeFast("UPDATE dialogs SET unread_count = ?, unread_count_i = ? WHERE did = ?");
                executeFast.requery();
                executeFast.bindInteger(1, Math.max(0, n - array2[0]));
                executeFast.bindInteger(2, Math.max(0, intValue - array2[1]));
                executeFast.bindLong(3, key);
                executeFast.step();
                executeFast.dispose();
            }
            this.database.executeFast(String.format(Locale.US, "DELETE FROM messages WHERE uid = %d AND mid <= %d", n2, l)).stepThis().dispose();
            this.database.executeFast(String.format(Locale.US, "DELETE FROM media_v2 WHERE uid = %d AND mid <= %d", n2, l)).stepThis().dispose();
            this.database.executeFast(String.format(Locale.US, "UPDATE media_counts_v2 SET old = 1 WHERE uid = %d", n2)).stepThis().dispose();
            return list;
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
            return null;
        }
    }
    
    private ArrayList<Long> markMessagesAsDeletedInternal(final ArrayList<Integer> c, int i) {
        try {
            final ArrayList list = new ArrayList(c);
            final ArrayList<Long> list2 = new ArrayList<Long>();
            final LongSparseArray longSparseArray = new LongSparseArray();
            String s;
            if (i != 0) {
                final StringBuilder sb = new StringBuilder(c.size());
                for (int j = 0; j < c.size(); ++j) {
                    final long n = c.get(j);
                    final long n2 = i;
                    if (sb.length() > 0) {
                        sb.append(',');
                    }
                    sb.append(n | n2 << 32);
                }
                s = sb.toString();
            }
            else {
                s = TextUtils.join((CharSequence)",", (Iterable)c);
            }
            final ArrayList<File> list3 = new ArrayList<File>();
            final int clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
            final SQLiteCursor queryFinalized = this.database.queryFinalized(String.format(Locale.US, "SELECT uid, data, read_state, out, mention, mid FROM messages WHERE mid IN(%s)", s), new Object[0]);
            Label_0655: {
                Object tLdeserialize = null;
                Label_0650: {
                    try {
                        while (queryFinalized.next()) {
                            final long longValue = queryFinalized.longValue(0);
                            list.remove((Object)queryFinalized.intValue(5));
                            if (longValue == clientUserId) {
                                continue;
                            }
                            try {
                                final int intValue = queryFinalized.intValue(2);
                                if (queryFinalized.intValue(3) == 0) {
                                    if ((tLdeserialize = longSparseArray.get(longValue)) == null) {
                                        tLdeserialize = new Integer[] { 0, 0 };
                                        longSparseArray.put(longValue, tLdeserialize);
                                    }
                                    if (intValue < 2) {
                                        final Integer n3 = tLdeserialize[1];
                                        tLdeserialize[1] = Integer.valueOf((int)tLdeserialize[1] + 1);
                                    }
                                    if (intValue == 0 || intValue == 2) {
                                        final Integer n4 = tLdeserialize[0];
                                        tLdeserialize[0] = Integer.valueOf((int)tLdeserialize[0] + 1);
                                    }
                                }
                                if ((int)longValue != 0) {
                                    continue;
                                }
                                final NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(1);
                                if (byteBufferValue == null) {
                                    continue;
                                }
                                tLdeserialize = TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                                ((TLRPC.Message)tLdeserialize).readAttachPath(byteBufferValue, UserConfig.getInstance(this.currentAccount).clientUserId);
                                byteBufferValue.reuse();
                                if (tLdeserialize == null) {
                                    continue;
                                }
                                if (((TLRPC.Message)tLdeserialize).media instanceof TLRPC.TL_messageMediaPhoto) {
                                    for (int size = ((TLRPC.Message)tLdeserialize).media.photo.sizes.size(), k = 0; k < size; ++k) {
                                        final File pathToAttach = FileLoader.getPathToAttach(((TLRPC.Message)tLdeserialize).media.photo.sizes.get(k));
                                        if (pathToAttach != null && pathToAttach.toString().length() > 0) {
                                            list3.add(pathToAttach);
                                        }
                                    }
                                    continue;
                                }
                                if (((TLRPC.Message)tLdeserialize).media instanceof TLRPC.TL_messageMediaDocument) {
                                    final File pathToAttach2 = FileLoader.getPathToAttach(((TLRPC.Message)tLdeserialize).media.document);
                                    if (pathToAttach2 != null && pathToAttach2.toString().length() > 0) {
                                        list3.add(pathToAttach2);
                                    }
                                    for (int size2 = ((TLRPC.Message)tLdeserialize).media.document.thumbs.size(), l = 0; l < size2; ++l) {
                                        final File pathToAttach3 = FileLoader.getPathToAttach(((TLRPC.Message)tLdeserialize).media.document.thumbs.get(l));
                                        if (pathToAttach3 != null && pathToAttach3.toString().length() > 0) {
                                            list3.add(pathToAttach3);
                                        }
                                    }
                                    continue;
                                }
                                continue;
                            }
                            catch (Exception tLdeserialize) {
                                break Label_0650;
                            }
                            break;
                        }
                        break Label_0655;
                    }
                    catch (Exception ex2) {}
                }
                FileLog.e((Throwable)tLdeserialize);
            }
            final String s2 = s;
            queryFinalized.dispose();
            FileLoader.getInstance(this.currentAccount).deleteFiles(list3, 0);
            for (int n5 = 0; n5 < longSparseArray.size(); ++n5) {
                final long key = longSparseArray.keyAt(n5);
                final Integer[] array = (Integer[])longSparseArray.valueAt(n5);
                final SQLiteDatabase database = this.database;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("SELECT unread_count, unread_count_i FROM dialogs WHERE did = ");
                sb2.append(key);
                final SQLiteCursor queryFinalized2 = database.queryFinalized(sb2.toString(), new Object[0]);
                int intValue2;
                int intValue3;
                if (queryFinalized2.next()) {
                    intValue2 = queryFinalized2.intValue(0);
                    intValue3 = queryFinalized2.intValue(1);
                }
                else {
                    intValue2 = 0;
                    intValue3 = 0;
                }
                queryFinalized2.dispose();
                list2.add(key);
                final SQLitePreparedStatement executeFast = this.database.executeFast("UPDATE dialogs SET unread_count = ?, unread_count_i = ? WHERE did = ?");
                executeFast.requery();
                executeFast.bindInteger(1, Math.max(0, intValue2 - array[0]));
                executeFast.bindInteger(2, Math.max(0, intValue3 - array[1]));
                executeFast.bindLong(3, key);
                executeFast.step();
                executeFast.dispose();
            }
            this.database.executeFast(String.format(Locale.US, "DELETE FROM messages WHERE mid IN(%s)", s2)).stepThis().dispose();
            this.database.executeFast(String.format(Locale.US, "DELETE FROM polls WHERE mid IN(%s)", s2)).stepThis().dispose();
            this.database.executeFast(String.format(Locale.US, "DELETE FROM bot_keyboard WHERE mid IN(%s)", s2)).stepThis().dispose();
            this.database.executeFast(String.format(Locale.US, "DELETE FROM messages_seq WHERE mid IN(%s)", s2)).stepThis().dispose();
            if (list.isEmpty()) {
                final SQLiteCursor queryFinalized3 = this.database.queryFinalized(String.format(Locale.US, "SELECT uid, type FROM media_v2 WHERE mid IN(%s)", s2), new Object[0]);
                SparseArray sparseArray = null;
                while (queryFinalized3.next()) {
                    final long longValue2 = queryFinalized3.longValue(0);
                    i = queryFinalized3.intValue(1);
                    SparseArray sparseArray2;
                    if ((sparseArray2 = sparseArray) == null) {
                        sparseArray2 = new SparseArray();
                    }
                    LongSparseArray longSparseArray2 = (LongSparseArray)sparseArray2.get(i);
                    Integer value;
                    if (longSparseArray2 == null) {
                        longSparseArray2 = new LongSparseArray();
                        value = 0;
                        sparseArray2.put(i, (Object)longSparseArray2);
                    }
                    else {
                        value = (Integer)longSparseArray2.get(longValue2);
                    }
                    Integer value2 = value;
                    if (value == null) {
                        value2 = 0;
                    }
                    longSparseArray2.put(longValue2, (Object)(value2 + 1));
                    sparseArray = sparseArray2;
                }
                queryFinalized3.dispose();
                if (sparseArray != null) {
                    final SQLitePreparedStatement executeFast2 = this.database.executeFast("REPLACE INTO media_counts_v2 VALUES(?, ?, ?, ?)");
                    int key2;
                    LongSparseArray longSparseArray3;
                    int n6;
                    long key3;
                    SQLiteCursor queryFinalized4;
                    int intValue4;
                    int intValue5;
                    int max;
                    for (i = 0; i < sparseArray.size(); ++i) {
                        key2 = sparseArray.keyAt(i);
                        for (longSparseArray3 = (LongSparseArray)sparseArray.valueAt(i), n6 = 0; n6 < longSparseArray3.size(); ++n6) {
                            key3 = longSparseArray3.keyAt(n6);
                            queryFinalized4 = this.database.queryFinalized(String.format(Locale.US, "SELECT count, old FROM media_counts_v2 WHERE uid = %d AND type = %d LIMIT 1", key3, key2), new Object[0]);
                            if (queryFinalized4.next()) {
                                intValue4 = queryFinalized4.intValue(0);
                                intValue5 = queryFinalized4.intValue(1);
                            }
                            else {
                                intValue5 = 0;
                                intValue4 = -1;
                            }
                            queryFinalized4.dispose();
                            if (intValue4 != -1) {
                                executeFast2.requery();
                                max = Math.max(0, intValue4 - (int)longSparseArray3.valueAt(n6));
                                executeFast2.bindLong(1, key3);
                                executeFast2.bindInteger(2, key2);
                                executeFast2.bindInteger(3, max);
                                executeFast2.bindInteger(4, intValue5);
                                executeFast2.step();
                            }
                        }
                    }
                    executeFast2.dispose();
                }
            }
            else if (i == 0) {
                this.database.executeFast("UPDATE media_counts_v2 SET old = 1 WHERE 1").stepThis().dispose();
            }
            else {
                this.database.executeFast(String.format(Locale.US, "UPDATE media_counts_v2 SET old = 1 WHERE uid = %d", -i)).stepThis().dispose();
            }
            this.database.executeFast(String.format(Locale.US, "DELETE FROM media_v2 WHERE mid IN(%s)", s2)).stepThis().dispose();
            DataQuery.getInstance(this.currentAccount).clearBotKeyboard(0L, c);
            return list2;
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return null;
        }
    }
    
    private void markMessagesAsReadInternal(final SparseLongArray sparseLongArray, final SparseLongArray sparseLongArray2, final SparseIntArray sparseIntArray) {
        try {
            final boolean empty = isEmpty(sparseLongArray);
            final int n = 0;
            if (!empty) {
                final SQLitePreparedStatement executeFast = this.database.executeFast("DELETE FROM unread_push_messages WHERE uid = ? AND mid <= ?");
                for (int i = 0; i < sparseLongArray.size(); ++i) {
                    final int key = sparseLongArray.keyAt(i);
                    final long value = sparseLongArray.get(key);
                    this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = read_state | 1 WHERE uid = %d AND mid > 0 AND mid <= %d AND read_state IN(0,2) AND out = 0", key, value)).stepThis().dispose();
                    executeFast.requery();
                    executeFast.bindLong(1, key);
                    executeFast.bindLong(2, value);
                    executeFast.step();
                }
                executeFast.dispose();
            }
            if (!isEmpty(sparseLongArray2)) {
                for (int j = 0; j < sparseLongArray2.size(); ++j) {
                    final int key2 = sparseLongArray2.keyAt(j);
                    this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = read_state | 1 WHERE uid = %d AND mid > 0 AND mid <= %d AND read_state IN(0,2) AND out = 1", key2, sparseLongArray2.get(key2))).stepThis().dispose();
                }
            }
            if (sparseIntArray != null && !isEmpty(sparseIntArray)) {
                for (int k = n; k < sparseIntArray.size(); ++k) {
                    final long n2 = sparseIntArray.keyAt(k);
                    final int value2 = sparseIntArray.valueAt(k);
                    final SQLitePreparedStatement executeFast2 = this.database.executeFast("UPDATE messages SET read_state = read_state | 1 WHERE uid = ? AND date <= ? AND read_state IN(0,2) AND out = 1");
                    executeFast2.requery();
                    executeFast2.bindLong(1, n2 << 32);
                    executeFast2.bindInteger(2, value2);
                    executeFast2.step();
                    executeFast2.dispose();
                }
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private void putChatsInternal(final ArrayList<TLRPC.Chat> list) throws Exception {
        if (list != null) {
            if (!list.isEmpty()) {
                final SQLitePreparedStatement executeFast = this.database.executeFast("REPLACE INTO chats VALUES(?, ?, ?)");
                for (int i = 0; i < list.size(); ++i) {
                    TLRPC.Chat chat2;
                    final TLRPC.Chat chat = chat2 = list.get(i);
                    if (chat.min) {
                        final SQLiteCursor queryFinalized = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM chats WHERE uid = %d", chat.id), new Object[0]);
                        chat2 = chat;
                        if (queryFinalized.next()) {
                            try {
                                final NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(0);
                                chat2 = chat;
                                if (byteBufferValue != null) {
                                    final TLRPC.Chat tLdeserialize = TLRPC.Chat.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                                    byteBufferValue.reuse();
                                    chat2 = chat;
                                    if (tLdeserialize != null) {
                                        tLdeserialize.title = chat.title;
                                        tLdeserialize.photo = chat.photo;
                                        tLdeserialize.broadcast = chat.broadcast;
                                        tLdeserialize.verified = chat.verified;
                                        tLdeserialize.megagroup = chat.megagroup;
                                        if (chat.default_banned_rights != null) {
                                            tLdeserialize.default_banned_rights = chat.default_banned_rights;
                                            tLdeserialize.flags |= 0x40000;
                                        }
                                        if (chat.admin_rights != null) {
                                            tLdeserialize.admin_rights = chat.admin_rights;
                                            tLdeserialize.flags |= 0x4000;
                                        }
                                        if (chat.banned_rights != null) {
                                            tLdeserialize.banned_rights = chat.banned_rights;
                                            tLdeserialize.flags |= 0x8000;
                                        }
                                        if (chat.username != null) {
                                            tLdeserialize.username = chat.username;
                                            tLdeserialize.flags |= 0x40;
                                        }
                                        else {
                                            tLdeserialize.username = null;
                                            tLdeserialize.flags &= 0xFFFFFFBF;
                                        }
                                        chat2 = tLdeserialize;
                                    }
                                }
                            }
                            catch (Exception ex) {
                                FileLog.e(ex);
                                chat2 = chat;
                            }
                        }
                        queryFinalized.dispose();
                    }
                    executeFast.requery();
                    final NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(chat2.getObjectSize());
                    chat2.serializeToStream(nativeByteBuffer);
                    executeFast.bindInteger(1, chat2.id);
                    final String title = chat2.title;
                    if (title != null) {
                        executeFast.bindString(2, title.toLowerCase());
                    }
                    else {
                        executeFast.bindString(2, "");
                    }
                    executeFast.bindByteBuffer(3, nativeByteBuffer);
                    executeFast.step();
                    nativeByteBuffer.reuse();
                }
                executeFast.dispose();
            }
        }
    }
    
    private void putDialogsInternal(final TLRPC.messages_Dialogs ex, final int n) {
        final Exception ex2 = ex;
        try {
            this.database.beginTransaction();
            final LongSparseArray longSparseArray = new LongSparseArray(((TLRPC.messages_Dialogs)ex2).messages.size());
            for (int i = 0; i < ((TLRPC.messages_Dialogs)ex2).messages.size(); ++i) {
                final TLRPC.Message message = ((TLRPC.messages_Dialogs)ex2).messages.get(i);
                longSparseArray.put(MessageObject.getDialogId(message), (Object)message);
            }
            Label_1289: {
                if (((TLRPC.messages_Dialogs)ex2).dialogs.isEmpty()) {
                    break Label_1289;
                }
                try {
                    final SQLitePreparedStatement executeFast = this.database.executeFast("REPLACE INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)");
                    final SQLitePreparedStatement executeFast2 = this.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    final SQLitePreparedStatement executeFast3 = this.database.executeFast("REPLACE INTO media_v2 VALUES(?, ?, ?, ?, ?)");
                    final SQLitePreparedStatement executeFast4 = this.database.executeFast("REPLACE INTO dialog_settings VALUES(?, ?)");
                    SQLitePreparedStatement executeFast5 = this.database.executeFast("REPLACE INTO messages_holes VALUES(?, ?, ?)");
                    final SQLitePreparedStatement executeFast6 = this.database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
                    int index = 0;
                    SQLitePreparedStatement executeFast7 = null;
                    while (true) {
                        final Exception ex3 = ex;
                        if (index >= ((TLRPC.messages_Dialogs)ex3).dialogs.size()) {
                            break;
                        }
                        final TLRPC.Dialog dialog = ((TLRPC.messages_Dialogs)ex3).dialogs.get(index);
                        DialogObject.initDialog(dialog);
                        SQLitePreparedStatement sqLitePreparedStatement = null;
                        Label_1242: {
                            if (n == 1) {
                                final SQLiteDatabase database = this.database;
                                final StringBuilder sb = new StringBuilder();
                                sb.append("SELECT did FROM dialogs WHERE did = ");
                                sb.append(dialog.id);
                                final SQLiteCursor queryFinalized = database.queryFinalized(sb.toString(), new Object[0]);
                                final boolean next = queryFinalized.next();
                                queryFinalized.dispose();
                                if (next) {
                                    sqLitePreparedStatement = executeFast7;
                                    break Label_1242;
                                }
                            }
                            else if (dialog.pinned && n == 2) {
                                final SQLiteDatabase database2 = this.database;
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("SELECT pinned FROM dialogs WHERE did = ");
                                sb2.append(dialog.id);
                                final SQLiteCursor queryFinalized2 = database2.queryFinalized(sb2.toString(), new Object[0]);
                                if (queryFinalized2.next()) {
                                    dialog.pinnedNum = queryFinalized2.intValue(0);
                                }
                                queryFinalized2.dispose();
                            }
                            final SQLitePreparedStatement sqLitePreparedStatement2 = executeFast4;
                            final TLRPC.Message message2 = (TLRPC.Message)longSparseArray.get(dialog.id);
                            int n5;
                            if (message2 != null) {
                                final int max = Math.max(message2.date, 0);
                                if (this.isValidKeyboardToSave(message2)) {
                                    DataQuery.getInstance(this.currentAccount).putBotKeyboard(dialog.id, message2);
                                }
                                this.fixUnsupportedMedia(message2);
                                final NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(message2.getObjectSize());
                                message2.serializeToStream(nativeByteBuffer);
                                long n2 = message2.id;
                                if (message2.to_id.channel_id != 0) {
                                    n2 |= (long)message2.to_id.channel_id << 32;
                                }
                                executeFast.requery();
                                executeFast.bindLong(1, n2);
                                executeFast.bindLong(2, dialog.id);
                                executeFast.bindInteger(3, MessageObject.getUnreadFlags(message2));
                                executeFast.bindInteger(4, message2.send_state);
                                executeFast.bindInteger(5, message2.date);
                                executeFast.bindByteBuffer(6, nativeByteBuffer);
                                int n3;
                                if (MessageObject.isOut(message2)) {
                                    n3 = 1;
                                }
                                else {
                                    n3 = 0;
                                }
                                executeFast.bindInteger(7, n3);
                                executeFast.bindInteger(8, 0);
                                int views;
                                if ((message2.flags & 0x400) != 0x0) {
                                    views = message2.views;
                                }
                                else {
                                    views = 0;
                                }
                                executeFast.bindInteger(9, views);
                                executeFast.bindInteger(10, 0);
                                int n4;
                                if (message2.mentioned) {
                                    n4 = 1;
                                }
                                else {
                                    n4 = 0;
                                }
                                executeFast.bindInteger(11, n4);
                                executeFast.step();
                                if (DataQuery.canAddMessageToMedia(message2)) {
                                    executeFast3.requery();
                                    executeFast3.bindLong(1, n2);
                                    executeFast3.bindLong(2, dialog.id);
                                    executeFast3.bindInteger(3, message2.date);
                                    executeFast3.bindInteger(4, DataQuery.getMediaType(message2));
                                    executeFast3.bindByteBuffer(5, nativeByteBuffer);
                                    executeFast3.step();
                                }
                                nativeByteBuffer.reuse();
                                if (message2.media instanceof TLRPC.TL_messageMediaPoll) {
                                    if (executeFast7 == null) {
                                        executeFast7 = this.database.executeFast("REPLACE INTO polls VALUES(?, ?)");
                                    }
                                    final TLRPC.TL_messageMediaPoll tl_messageMediaPoll = (TLRPC.TL_messageMediaPoll)message2.media;
                                    executeFast7.requery();
                                    executeFast7.bindLong(1, n2);
                                    executeFast7.bindLong(2, tl_messageMediaPoll.poll.id);
                                    executeFast7.step();
                                }
                                createFirstHoles(dialog.id, executeFast5, executeFast6, message2.id);
                                n5 = max;
                            }
                            else {
                                n5 = 0;
                            }
                            final SQLitePreparedStatement sqLitePreparedStatement3 = executeFast5;
                            long n7;
                            final long n6 = n7 = dialog.top_message;
                            if (dialog.peer != null) {
                                n7 = n6;
                                if (dialog.peer.channel_id != 0) {
                                    n7 = (n6 | (long)dialog.peer.channel_id << 32);
                                }
                            }
                            executeFast2.requery();
                            executeFast2.bindLong(1, dialog.id);
                            executeFast2.bindInteger(2, n5);
                            executeFast2.bindInteger(3, dialog.unread_count);
                            executeFast2.bindLong(4, n7);
                            executeFast2.bindInteger(5, dialog.read_inbox_max_id);
                            executeFast2.bindInteger(6, dialog.read_outbox_max_id);
                            executeFast2.bindLong(7, 0L);
                            executeFast2.bindInteger(8, dialog.unread_mentions_count);
                            executeFast2.bindInteger(9, dialog.pts);
                            executeFast2.bindInteger(10, 0);
                            executeFast2.bindInteger(11, dialog.pinnedNum);
                            int n8;
                            if (dialog.unread_mark) {
                                n8 = 1;
                            }
                            else {
                                n8 = 0;
                            }
                            executeFast2.bindInteger(12, n8);
                            executeFast2.bindInteger(13, dialog.folder_id);
                            NativeByteBuffer nativeByteBuffer2;
                            if (dialog instanceof TLRPC.TL_dialogFolder) {
                                final TLRPC.TL_dialogFolder tl_dialogFolder = (TLRPC.TL_dialogFolder)dialog;
                                nativeByteBuffer2 = new NativeByteBuffer(tl_dialogFolder.folder.getObjectSize());
                                tl_dialogFolder.folder.serializeToStream(nativeByteBuffer2);
                                executeFast2.bindByteBuffer(14, nativeByteBuffer2);
                            }
                            else {
                                executeFast2.bindNull(14);
                                nativeByteBuffer2 = null;
                            }
                            executeFast2.step();
                            if (nativeByteBuffer2 != null) {
                                nativeByteBuffer2.reuse();
                            }
                            executeFast5 = sqLitePreparedStatement3;
                            sqLitePreparedStatement = executeFast7;
                            if (dialog.notify_settings != null) {
                                sqLitePreparedStatement2.requery();
                                final long id = dialog.id;
                                int n9 = 1;
                                sqLitePreparedStatement2.bindLong(1, id);
                                if (dialog.notify_settings.mute_until == 0) {
                                    n9 = 0;
                                }
                                sqLitePreparedStatement2.bindInteger(2, n9);
                                sqLitePreparedStatement2.step();
                                sqLitePreparedStatement = executeFast7;
                                executeFast5 = sqLitePreparedStatement3;
                            }
                        }
                        ++index;
                        executeFast7 = sqLitePreparedStatement;
                    }
                    executeFast.dispose();
                    executeFast2.dispose();
                    executeFast3.dispose();
                    executeFast4.dispose();
                    executeFast5.dispose();
                    executeFast6.dispose();
                    if (executeFast7 != null) {
                        executeFast7.dispose();
                    }
                    final ArrayList<TLRPC.User> users = ((TLRPC.messages_Dialogs)ex).users;
                    try {
                        this.putUsersInternal(users);
                        this.putChatsInternal(((TLRPC.messages_Dialogs)ex).chats);
                        this.database.commitTransaction();
                    }
                    catch (Exception ex) {}
                }
                catch (Exception ex) {}
            }
        }
        catch (Exception ex4) {}
        FileLog.e(ex);
    }
    
    private void putMessagesInternal(final ArrayList<TLRPC.Message> ex, final boolean b, final boolean b2, int n, final boolean b3) {
        Label_4071: {
            Label_0124: {
                if (!b3) {
                    break Label_0124;
                }
                try {
                    final TLRPC.Message message = ((ArrayList<TLRPC.Message>)ex).get(0);
                    if (message.dialog_id == 0L) {
                        MessageObject.getDialogId(message);
                    }
                    final SQLiteDatabase database = this.database;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("SELECT last_mid FROM dialogs WHERE did = ");
                    sb.append(message.dialog_id);
                    final SQLiteCursor queryFinalized = database.queryFinalized(sb.toString(), new Object[0]);
                    int intValue;
                    if (queryFinalized.next()) {
                        intValue = queryFinalized.intValue(0);
                    }
                    else {
                        intValue = -1;
                    }
                    queryFinalized.dispose();
                    if (intValue != 0) {
                        return;
                    }
                    break Label_0124;
                }
                catch (Exception ex2) {}
                break Label_4071;
            }
            if (b) {
                this.database.beginTransaction();
            }
            final LongSparseArray longSparseArray = new LongSparseArray();
            final LongSparseArray longSparseArray2 = new LongSparseArray();
            final LongSparseArray longSparseArray3 = new LongSparseArray();
            final LongSparseArray longSparseArray4 = new LongSparseArray();
            final StringBuilder sb2 = new StringBuilder();
            final LongSparseArray longSparseArray5 = new LongSparseArray();
            final LongSparseArray longSparseArray6 = new LongSparseArray();
            final LongSparseArray longSparseArray7 = new LongSparseArray();
            final SQLitePreparedStatement executeFast = this.database.executeFast("REPLACE INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)");
            final SQLitePreparedStatement executeFast2 = this.database.executeFast("REPLACE INTO randoms VALUES(?, ?)");
            final SQLitePreparedStatement executeFast3 = this.database.executeFast("REPLACE INTO download_queue VALUES(?, ?, ?, ?, ?)");
            final SQLitePreparedStatement executeFast4 = this.database.executeFast("REPLACE INTO webpage_pending VALUES(?, ?)");
            LongSparseArray longSparseArray8 = null;
            LongSparseArray longSparseArray9 = null;
            int i = 0;
            StringBuilder sb3 = null;
            while (i < ((ArrayList)ex).size()) {
                final TLRPC.Message message2 = ((ArrayList<TLRPC.Message>)ex).get(i);
                final long n2 = message2.id;
                if (message2.dialog_id == 0L) {
                    MessageObject.getDialogId(message2);
                }
                long n3 = n2;
                if (message2.to_id.channel_id != 0) {
                    n3 = (n2 | (long)message2.to_id.channel_id << 32);
                }
                if (message2.mentioned && message2.media_unread) {
                    longSparseArray7.put(n3, (Object)message2.dialog_id);
                }
                if (!(message2.action instanceof TLRPC.TL_messageActionHistoryClear) && !MessageObject.isOut(message2) && (message2.id > 0 || MessageObject.isUnread(message2))) {
                    Integer n4 = (Integer)longSparseArray5.get(message2.dialog_id);
                    if (n4 == null) {
                        final SQLiteDatabase database2 = this.database;
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("SELECT inbox_max FROM dialogs WHERE did = ");
                        sb4.append(message2.dialog_id);
                        final SQLiteCursor queryFinalized2 = database2.queryFinalized(sb4.toString(), new Object[0]);
                        if (queryFinalized2.next()) {
                            n4 = queryFinalized2.intValue(0);
                        }
                        else {
                            n4 = 0;
                        }
                        queryFinalized2.dispose();
                        longSparseArray5.put(message2.dialog_id, (Object)n4);
                    }
                    if (message2.id < 0 || n4 < message2.id) {
                        if (sb2.length() > 0) {
                            sb2.append(",");
                        }
                        sb2.append(n3);
                        longSparseArray6.put(n3, (Object)message2.dialog_id);
                    }
                }
                if (DataQuery.canAddMessageToMedia(message2)) {
                    if (sb3 == null) {
                        sb3 = new StringBuilder();
                        longSparseArray8 = new LongSparseArray();
                        longSparseArray9 = new LongSparseArray();
                    }
                    if (sb3.length() > 0) {
                        sb3.append(",");
                    }
                    sb3.append(n3);
                    longSparseArray8.put(n3, (Object)message2.dialog_id);
                    longSparseArray9.put(n3, (Object)DataQuery.getMediaType(message2));
                }
                if (this.isValidKeyboardToSave(message2)) {
                    final TLRPC.Message message3 = (TLRPC.Message)longSparseArray4.get(message2.dialog_id);
                    if (message3 == null || message3.id < message2.id) {
                        longSparseArray4.put(message2.dialog_id, (Object)message2);
                    }
                }
                ++i;
            }
            final LongSparseArray longSparseArray10 = longSparseArray9;
            final LongSparseArray longSparseArray11 = longSparseArray;
            final LongSparseArray longSparseArray12 = longSparseArray3;
            final LongSparseArray longSparseArray13 = longSparseArray7;
            final LongSparseArray longSparseArray14 = longSparseArray8;
            for (int j = 0; j < longSparseArray4.size(); ++j) {
                DataQuery.getInstance(this.currentAccount).putBotKeyboard(longSparseArray4.keyAt(j), (TLRPC.Message)longSparseArray4.valueAt(j));
            }
            if (sb3 != null) {
                final SQLiteDatabase database3 = this.database;
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("SELECT mid, type FROM media_v2 WHERE mid IN(");
                sb5.append(sb3.toString());
                sb5.append(")");
                final SQLiteCursor queryFinalized3 = database3.queryFinalized(sb5.toString(), new Object[0]);
                LongSparseArray longSparseArray15 = null;
                final LongSparseArray longSparseArray16 = longSparseArray10;
                while (queryFinalized3.next()) {
                    final long longValue = queryFinalized3.longValue(0);
                    final int intValue2 = queryFinalized3.intValue(1);
                    if (intValue2 == (int)longSparseArray16.get(longValue)) {
                        longSparseArray14.remove(longValue);
                    }
                    else {
                        LongSparseArray longSparseArray17;
                        if ((longSparseArray17 = longSparseArray15) == null) {
                            longSparseArray17 = new LongSparseArray();
                        }
                        longSparseArray17.put(longValue, (Object)intValue2);
                        longSparseArray15 = longSparseArray17;
                    }
                }
                queryFinalized3.dispose();
                final SparseArray sparseArray = new SparseArray();
                int k = 0;
                final LongSparseArray longSparseArray18 = longSparseArray16;
                for (LongSparseArray longSparseArray19 = longSparseArray14; k < longSparseArray19.size(); ++k) {
                    final long key = longSparseArray19.keyAt(k);
                    final long longValue2 = (long)longSparseArray19.valueAt(k);
                    final Integer n5 = (Integer)longSparseArray18.get(key);
                    LongSparseArray longSparseArray20 = (LongSparseArray)sparseArray.get((int)n5);
                    Integer value;
                    if (longSparseArray20 == null) {
                        longSparseArray20 = new LongSparseArray();
                        value = 0;
                        sparseArray.put((int)n5, (Object)longSparseArray20);
                    }
                    else {
                        value = (Integer)longSparseArray20.get(longValue2);
                    }
                    Integer value2 = value;
                    if (value == null) {
                        value2 = 0;
                    }
                    longSparseArray20.put(longValue2, (Object)(value2 + 1));
                    if (longSparseArray15 != null) {
                        final int intValue3 = (int)longSparseArray15.get(key, (Object)(-1));
                        if (intValue3 >= 0) {
                            LongSparseArray longSparseArray21 = (LongSparseArray)sparseArray.get(intValue3);
                            Integer value3;
                            if (longSparseArray21 == null) {
                                longSparseArray21 = new LongSparseArray();
                                value3 = 0;
                                sparseArray.put(intValue3, (Object)longSparseArray21);
                            }
                            else {
                                value3 = (Integer)longSparseArray21.get(longValue2);
                            }
                            Integer value4 = value3;
                            if (value3 == null) {
                                value4 = 0;
                            }
                            longSparseArray21.put(longValue2, (Object)(value4 - 1));
                        }
                    }
                }
                final SparseArray sparseArray2 = sparseArray;
            }
            else {
                final SparseArray sparseArray2 = null;
            }
            LongSparseArray<Integer> longSparseArray22;
            LongSparseArray longSparseArray23;
            if (sb2.length() > 0) {
                final SQLiteDatabase database4 = this.database;
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("SELECT mid FROM messages WHERE mid IN(");
                sb6.append(sb2.toString());
                sb6.append(")");
                final SQLiteCursor queryFinalized4 = database4.queryFinalized(sb6.toString(), new Object[0]);
                while (queryFinalized4.next()) {
                    final long longValue3 = queryFinalized4.longValue(0);
                    longSparseArray6.remove(longValue3);
                    longSparseArray13.remove(longValue3);
                }
                queryFinalized4.dispose();
                for (int l = 0; l < longSparseArray6.size(); ++l) {
                    final long longValue4 = (long)longSparseArray6.valueAt(l);
                    Integer value5;
                    if ((value5 = (Integer)longSparseArray2.get(longValue4)) == null) {
                        value5 = 0;
                    }
                    longSparseArray2.put(longValue4, (Object)(value5 + 1));
                }
                int n6 = 0;
                while (true) {
                    longSparseArray22 = (LongSparseArray<Integer>)longSparseArray2;
                    longSparseArray23 = longSparseArray12;
                    if (n6 >= longSparseArray13.size()) {
                        break;
                    }
                    final long longValue5 = (long)longSparseArray13.valueAt(n6);
                    Integer value6;
                    if ((value6 = (Integer)longSparseArray12.get(longValue5)) == null) {
                        value6 = 0;
                    }
                    longSparseArray12.put(longValue5, (Object)(value6 + 1));
                    ++n6;
                }
            }
            else {
                longSparseArray23 = longSparseArray12;
                longSparseArray22 = (LongSparseArray<Integer>)longSparseArray2;
            }
            int index = 0;
            SQLitePreparedStatement sqLitePreparedStatement = null;
            final SQLitePreparedStatement sqLitePreparedStatement2 = null;
            int n7 = 0;
            final SQLitePreparedStatement sqLitePreparedStatement3 = executeFast;
            final LongSparseArray longSparseArray24 = longSparseArray11;
            final SQLitePreparedStatement sqLitePreparedStatement4 = executeFast2;
            final SQLitePreparedStatement sqLitePreparedStatement5 = executeFast4;
            final SQLitePreparedStatement sqLitePreparedStatement6 = executeFast3;
            final LongSparseArray longSparseArray25 = longSparseArray23;
            SQLitePreparedStatement sqLitePreparedStatement7 = sqLitePreparedStatement2;
            while (index < ((ArrayList)ex).size()) {
                final TLRPC.Message message4 = ((ArrayList<TLRPC.Message>)ex).get(index);
                this.fixUnsupportedMedia(message4);
                sqLitePreparedStatement3.requery();
                long n8 = message4.id;
                if (message4.local_id != 0) {
                    n8 = message4.local_id;
                }
                long n9 = n8;
                if (message4.to_id.channel_id != 0) {
                    n9 = (n8 | (long)message4.to_id.channel_id << 32);
                }
                final NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(message4.getObjectSize());
                message4.serializeToStream(nativeByteBuffer);
                if (!(message4.action instanceof TLRPC.TL_messageEncryptedAction) || message4.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL || message4.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) {
                    final long dialog_id = message4.dialog_id;
                    final LongSparseArray longSparseArray26 = longSparseArray24;
                    final TLRPC.Message message5 = (TLRPC.Message)longSparseArray26.get(dialog_id);
                    if (message5 == null || message4.date > message5.date || (message5.id > 0 && message4.id > message5.id) || (message5.id < 0 && message4.id < message5.id)) {
                        longSparseArray26.put(message4.dialog_id, (Object)message4);
                    }
                }
                sqLitePreparedStatement3.bindLong(1, n9);
                sqLitePreparedStatement3.bindLong(2, message4.dialog_id);
                sqLitePreparedStatement3.bindInteger(3, MessageObject.getUnreadFlags(message4));
                sqLitePreparedStatement3.bindInteger(4, message4.send_state);
                sqLitePreparedStatement3.bindInteger(5, message4.date);
                sqLitePreparedStatement3.bindByteBuffer(6, nativeByteBuffer);
                int n10;
                if (MessageObject.isOut(message4)) {
                    n10 = 1;
                }
                else {
                    n10 = 0;
                }
                sqLitePreparedStatement3.bindInteger(7, n10);
                sqLitePreparedStatement3.bindInteger(8, message4.ttl);
                if ((message4.flags & 0x400) != 0x0) {
                    sqLitePreparedStatement3.bindInteger(9, message4.views);
                }
                else {
                    sqLitePreparedStatement3.bindInteger(9, this.getMessageMediaType(message4));
                }
                sqLitePreparedStatement3.bindInteger(10, 0);
                int n11;
                if (message4.mentioned) {
                    n11 = 1;
                }
                else {
                    n11 = 0;
                }
                sqLitePreparedStatement3.bindInteger(11, n11);
                sqLitePreparedStatement3.step();
                if (message4.random_id != 0L) {
                    sqLitePreparedStatement4.requery();
                    final long random_id = message4.random_id;
                    final SQLitePreparedStatement sqLitePreparedStatement8 = sqLitePreparedStatement4;
                    sqLitePreparedStatement8.bindLong(1, random_id);
                    sqLitePreparedStatement8.bindLong(2, n9);
                    sqLitePreparedStatement8.step();
                }
                SQLitePreparedStatement executeFast5 = sqLitePreparedStatement;
                if (DataQuery.canAddMessageToMedia(message4)) {
                    if ((executeFast5 = sqLitePreparedStatement) == null) {
                        executeFast5 = this.database.executeFast("REPLACE INTO media_v2 VALUES(?, ?, ?, ?, ?)");
                    }
                    executeFast5.requery();
                    executeFast5.bindLong(1, n9);
                    executeFast5.bindLong(2, message4.dialog_id);
                    executeFast5.bindInteger(3, message4.date);
                    executeFast5.bindInteger(4, DataQuery.getMediaType(message4));
                    executeFast5.bindByteBuffer(5, nativeByteBuffer);
                    executeFast5.step();
                }
                SQLitePreparedStatement sqLitePreparedStatement10 = null;
                Label_2360: {
                    SQLitePreparedStatement executeFast6;
                    if (message4.media instanceof TLRPC.TL_messageMediaPoll) {
                        if ((executeFast6 = sqLitePreparedStatement7) == null) {
                            executeFast6 = this.database.executeFast("REPLACE INTO polls VALUES(?, ?)");
                        }
                        final TLRPC.TL_messageMediaPoll tl_messageMediaPoll = (TLRPC.TL_messageMediaPoll)message4.media;
                        executeFast6.requery();
                        executeFast6.bindLong(1, n9);
                        executeFast6.bindLong(2, tl_messageMediaPoll.poll.id);
                        executeFast6.step();
                    }
                    else {
                        executeFast6 = sqLitePreparedStatement7;
                        if (message4.media instanceof TLRPC.TL_messageMediaWebPage) {
                            sqLitePreparedStatement5.requery();
                            final long id = message4.media.webpage.id;
                            final SQLitePreparedStatement sqLitePreparedStatement9 = sqLitePreparedStatement5;
                            sqLitePreparedStatement9.bindLong(1, id);
                            sqLitePreparedStatement9.bindLong(2, n9);
                            sqLitePreparedStatement9.step();
                            sqLitePreparedStatement10 = sqLitePreparedStatement7;
                            break Label_2360;
                        }
                    }
                    sqLitePreparedStatement10 = executeFast6;
                }
                nativeByteBuffer.reuse();
                if (n != 0 && (message4.to_id.channel_id == 0 || message4.post) && message4.date >= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - 3600 && DownloadController.getInstance(this.currentAccount).canDownloadMedia(message4) == 1 && (message4.media instanceof TLRPC.TL_messageMediaPhoto || message4.media instanceof TLRPC.TL_messageMediaDocument || message4.media instanceof TLRPC.TL_messageMediaWebPage)) {
                    final TLRPC.Document document = MessageObject.getDocument(message4);
                    final TLRPC.Photo photo = MessageObject.getPhoto(message4);
                    long n12 = 0L;
                    TLRPC.MessageMedia messageMedia = null;
                    int n13 = 0;
                    Label_2801: {
                        if (MessageObject.isVoiceMessage(message4)) {
                            n12 = document.id;
                            messageMedia = new TLRPC.TL_messageMediaDocument();
                            messageMedia.document = document;
                            messageMedia.flags |= 0x1;
                            n13 = 2;
                        }
                        else {
                            if (MessageObject.isStickerMessage(message4)) {
                                n12 = document.id;
                                messageMedia = new TLRPC.TL_messageMediaDocument();
                                messageMedia.document = document;
                                messageMedia.flags |= 0x1;
                            }
                            else {
                                if (MessageObject.isVideoMessage(message4) || MessageObject.isRoundVideoMessage(message4) || MessageObject.isGifMessage(message4)) {
                                    n12 = document.id;
                                    messageMedia = new TLRPC.TL_messageMediaDocument();
                                    messageMedia.document = document;
                                    messageMedia.flags |= 0x1;
                                    n13 = 4;
                                    break Label_2801;
                                }
                                if (document != null) {
                                    n12 = document.id;
                                    messageMedia = new TLRPC.TL_messageMediaDocument();
                                    messageMedia.document = document;
                                    messageMedia.flags |= 0x1;
                                    n13 = 8;
                                    break Label_2801;
                                }
                                if (photo == null || FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize()) == null) {
                                    n13 = 0;
                                    n12 = 0L;
                                    messageMedia = null;
                                    break Label_2801;
                                }
                                final long id2 = photo.id;
                                final TLRPC.TL_messageMediaPhoto tl_messageMediaPhoto = new TLRPC.TL_messageMediaPhoto();
                                tl_messageMediaPhoto.photo = photo;
                                tl_messageMediaPhoto.flags |= 0x1;
                                n12 = id2;
                                messageMedia = tl_messageMediaPhoto;
                                if (message4.media instanceof TLRPC.TL_messageMediaWebPage) {
                                    tl_messageMediaPhoto.flags |= Integer.MIN_VALUE;
                                    n12 = id2;
                                    messageMedia = tl_messageMediaPhoto;
                                }
                            }
                            n13 = 1;
                        }
                    }
                    if (messageMedia != null) {
                        if (message4.media.ttl_seconds != 0) {
                            messageMedia.ttl_seconds = message4.media.ttl_seconds;
                            messageMedia.flags |= 0x4;
                        }
                        n7 |= n13;
                        sqLitePreparedStatement6.requery();
                        final NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer(messageMedia.getObjectSize());
                        messageMedia.serializeToStream(nativeByteBuffer2);
                        final SQLitePreparedStatement sqLitePreparedStatement11 = sqLitePreparedStatement6;
                        sqLitePreparedStatement11.bindLong(1, n12);
                        sqLitePreparedStatement11.bindInteger(2, n13);
                        sqLitePreparedStatement11.bindInteger(3, message4.date);
                        sqLitePreparedStatement11.bindByteBuffer(4, nativeByteBuffer2);
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("sent_");
                        int channel_id;
                        if (message4.to_id != null) {
                            channel_id = message4.to_id.channel_id;
                        }
                        else {
                            channel_id = 0;
                        }
                        sb7.append(channel_id);
                        sb7.append("_");
                        sb7.append(message4.id);
                        sqLitePreparedStatement11.bindString(5, sb7.toString());
                        sqLitePreparedStatement11.step();
                        nativeByteBuffer2.reuse();
                    }
                }
                ++index;
                sqLitePreparedStatement = executeFast5;
                sqLitePreparedStatement7 = sqLitePreparedStatement10;
            }
            final LongSparseArray longSparseArray27 = longSparseArray24;
            final LongSparseArray<Integer> longSparseArray28 = longSparseArray22;
            sqLitePreparedStatement3.dispose();
            if (sqLitePreparedStatement != null) {
                sqLitePreparedStatement.dispose();
            }
            if (sqLitePreparedStatement7 != null) {
                sqLitePreparedStatement7.dispose();
            }
            sqLitePreparedStatement4.dispose();
            sqLitePreparedStatement6.dispose();
            sqLitePreparedStatement5.dispose();
            final SQLitePreparedStatement executeFast7 = this.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            final SQLitePreparedStatement executeFast8 = this.database.executeFast("UPDATE dialogs SET date = ?, unread_count = ?, last_mid = ?, unread_count_i = ? WHERE did = ?");
            int n14 = 0;
            final LongSparseArray longSparseArray29 = longSparseArray25;
            final LongSparseArray longSparseArray30 = longSparseArray27;
            final SQLitePreparedStatement sqLitePreparedStatement12 = executeFast7;
            SparseArray sparseArray2;
            long key2;
            TLRPC.Message message6;
            int channel_id2;
            SQLiteDatabase database5;
            StringBuilder sb8;
            SQLiteCursor queryFinalized5;
            boolean next;
            int max;
            int intValue4;
            int max2;
            LongSparseArray longSparseArray31;
            Integer n15;
            Integer value7;
            Integer value8;
            long n16;
            long n17;
            int date;
            int date2;
            int intValue5;
            int intValue6;
            LongSparseArray longSparseArray32;
            int n18;
            long key3;
            int key4;
            SQLiteCursor queryFinalized6;
            SQLitePreparedStatement executeFast9;
            int intValue7;
            SparseArray sparseArray3;
            Label_4014_Outer:Label_3915_Outer:Block_102_Outer:Label_3753_Outer:
            while (true) {
                n = longSparseArray30.size();
                while (true) {
                    if (n14 < n) {
                        try {
                            key2 = longSparseArray30.keyAt(n14);
                            if (key2 != 0L) {
                                message6 = (TLRPC.Message)longSparseArray30.valueAt(n14);
                                if (message6 != null) {
                                    channel_id2 = message6.to_id.channel_id;
                                }
                                else {
                                    channel_id2 = 0;
                                }
                                database5 = this.database;
                                sb8 = new StringBuilder();
                                sb8.append("SELECT date, unread_count, last_mid, unread_count_i FROM dialogs WHERE did = ");
                                sb8.append(key2);
                                queryFinalized5 = database5.queryFinalized(sb8.toString(), new Object[0]);
                                next = queryFinalized5.next();
                                if (next) {
                                    n = queryFinalized5.intValue(0);
                                    max = Math.max(0, queryFinalized5.intValue(1));
                                    intValue4 = queryFinalized5.intValue(2);
                                    max2 = Math.max(0, queryFinalized5.intValue(3));
                                }
                                else {
                                    if (channel_id2 != 0) {
                                        MessagesController.getInstance(this.currentAccount).checkChannelInviter(channel_id2);
                                    }
                                    max = 0;
                                    max2 = 0;
                                    intValue4 = 0;
                                    n = 0;
                                }
                                queryFinalized5.dispose();
                                longSparseArray31 = longSparseArray29;
                                n15 = (Integer)longSparseArray31.get(key2);
                                value7 = (Integer)longSparseArray28.get(key2);
                                if (value7 == null) {
                                    value7 = 0;
                                }
                                else {
                                    longSparseArray28.put(key2, (Object)(value7 + max));
                                }
                                if (n15 == null) {
                                    value8 = 0;
                                }
                                else {
                                    longSparseArray31.put(key2, (Object)(n15 + max2));
                                    value8 = n15;
                                }
                                if (message6 != null) {
                                    n16 = message6.id;
                                }
                                else {
                                    n16 = intValue4;
                                }
                                n17 = n16;
                                if (message6 != null) {
                                    n17 = n16;
                                    if (message6.local_id != 0) {
                                        n17 = message6.local_id;
                                    }
                                }
                                if (channel_id2 != 0) {
                                    n17 |= (long)channel_id2 << 32;
                                }
                                if (next) {
                                    executeFast8.requery();
                                    date = n;
                                    if (message6 != null && (!b2 || (date = n) == 0)) {
                                        date = message6.date;
                                    }
                                    executeFast8.bindInteger(1, date);
                                    executeFast8.bindInteger(2, max + value7);
                                    executeFast8.bindLong(3, n17);
                                    executeFast8.bindInteger(4, max2 + value8);
                                    executeFast8.bindLong(5, key2);
                                    executeFast8.step();
                                }
                                else {
                                    sqLitePreparedStatement12.requery();
                                    sqLitePreparedStatement12.bindLong(1, key2);
                                    date2 = n;
                                    if (message6 != null && (!b2 || (date2 = n) == 0)) {
                                        date2 = message6.date;
                                    }
                                    sqLitePreparedStatement12.bindInteger(2, date2);
                                    sqLitePreparedStatement12.bindInteger(3, max + value7);
                                    sqLitePreparedStatement12.bindLong(4, n17);
                                    sqLitePreparedStatement12.bindInteger(5, 0);
                                    sqLitePreparedStatement12.bindInteger(6, 0);
                                    sqLitePreparedStatement12.bindLong(7, 0L);
                                    sqLitePreparedStatement12.bindInteger(8, max2 + value8);
                                    if (channel_id2 != 0) {
                                        n = 1;
                                    }
                                    else {
                                        n = 0;
                                    }
                                    sqLitePreparedStatement12.bindInteger(9, n);
                                    sqLitePreparedStatement12.bindInteger(10, 0);
                                    sqLitePreparedStatement12.bindInteger(11, 0);
                                    sqLitePreparedStatement12.bindInteger(12, 0);
                                    sqLitePreparedStatement12.bindInteger(13, 0);
                                    sqLitePreparedStatement12.bindNull(14);
                                    sqLitePreparedStatement12.step();
                                }
                            }
                            ++n14;
                            continue Label_4014_Outer;
                            // iftrue(Label_4027:, !b)
                            // iftrue(Label_3909:, !queryFinalized6.next())
                            // iftrue(Label_3994:, intValue5 == -1)
                            // iftrue(Label_4014:, sparseArray2 == null)
                            // iftrue(Label_4075:, n7 == 0)
                            // iftrue(Label_4000:, n18 >= longSparseArray32.size())
                            while (true) {
                            Block_107_Outer:
                                while (true) {
                                Label_3820:
                                    while (true) {
                                        Label_4027: {
                                            Label_3994: {
                                            Label_3915:
                                                while (true) {
                                                    Block_104: {
                                                        while (true) {
                                                            Block_106: {
                                                                while (true) {
                                                                    break Block_106;
                                                                    Label_3909: {
                                                                        intValue5 = -1;
                                                                    }
                                                                    intValue6 = 0;
                                                                    break Label_3915;
                                                                    key3 = longSparseArray32.keyAt(n18);
                                                                    queryFinalized6 = this.database.queryFinalized(String.format(Locale.US, "SELECT count, old FROM media_counts_v2 WHERE uid = %d AND type = %d LIMIT 1", key3, key4), new Object[0]);
                                                                    break Block_104;
                                                                    executeFast9.requery();
                                                                    intValue7 = (int)longSparseArray32.valueAt(n18);
                                                                    executeFast9.bindLong(1, key3);
                                                                    executeFast9.bindInteger(2, key4);
                                                                    executeFast9.bindInteger(3, Math.max(0, intValue5 + intValue7));
                                                                    executeFast9.bindInteger(4, intValue6);
                                                                    executeFast9.step();
                                                                    break Label_3994;
                                                                    AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesStorage$EDTDSO5ZD7EJWhgiA72HtAyXwnM(this, n7));
                                                                    return;
                                                                    Label_4006:
                                                                    executeFast9.dispose();
                                                                    continue Label_3915_Outer;
                                                                }
                                                                key4 = sparseArray3.keyAt(n);
                                                                longSparseArray32 = (LongSparseArray)sparseArray3.valueAt(n);
                                                                n18 = 0;
                                                                break Label_3820;
                                                                Label_4000: {
                                                                    ++n;
                                                                }
                                                                break Block_107_Outer;
                                                            }
                                                            this.database.commitTransaction();
                                                            break Label_4027;
                                                            queryFinalized6.dispose();
                                                            continue Block_107_Outer;
                                                        }
                                                    }
                                                    intValue5 = queryFinalized6.intValue(0);
                                                    intValue6 = queryFinalized6.intValue(1);
                                                    continue Label_3915;
                                                }
                                                executeFast8.dispose();
                                                sqLitePreparedStatement12.dispose();
                                                executeFast9 = this.database.executeFast("REPLACE INTO media_counts_v2 VALUES(?, ?, ?, ?)");
                                                n = 0;
                                                sparseArray3 = sparseArray2;
                                                break Block_107_Outer;
                                            }
                                            ++n18;
                                            break Label_3820;
                                        }
                                        MessagesController.getInstance(this.currentAccount).processDialogsUpdateRead(longSparseArray28, (LongSparseArray<Integer>)longSparseArray29);
                                        continue Block_102_Outer;
                                    }
                                    continue Block_107_Outer;
                                }
                                continue Label_3753_Outer;
                            }
                        }
                        // iftrue(Label_4006:, n >= sparseArray3.size())
                        catch (Exception ex) {}
                        break;
                    }
                    continue;
                }
            }
        }
        FileLog.e(ex);
        Label_4075:;
    }
    
    private void putUsersAndChatsInternal(final ArrayList<TLRPC.User> list, final ArrayList<TLRPC.Chat> list2, final boolean b) {
        while (true) {
            if (b) {
                try {
                    this.database.beginTransaction();
                    this.putUsersInternal(list);
                    this.putChatsInternal(list2);
                    if (b) {
                        this.database.commitTransaction();
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                return;
            }
            continue;
        }
    }
    
    private void putUsersInternal(final ArrayList<TLRPC.User> list) throws Exception {
        if (list != null) {
            if (!list.isEmpty()) {
                final SQLitePreparedStatement executeFast = this.database.executeFast("REPLACE INTO users VALUES(?, ?, ?, ?)");
                for (int i = 0; i < list.size(); ++i) {
                    TLRPC.User user2;
                    final TLRPC.User user = user2 = list.get(i);
                    if (user.min) {
                        final SQLiteCursor queryFinalized = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM users WHERE uid = %d", user.id), new Object[0]);
                        user2 = user;
                        if (queryFinalized.next()) {
                            try {
                                final NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(0);
                                user2 = user;
                                if (byteBufferValue != null) {
                                    final TLRPC.User tLdeserialize = TLRPC.User.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                                    byteBufferValue.reuse();
                                    user2 = user;
                                    if (tLdeserialize != null) {
                                        if (user.username != null) {
                                            tLdeserialize.username = user.username;
                                            tLdeserialize.flags |= 0x8;
                                        }
                                        else {
                                            tLdeserialize.username = null;
                                            tLdeserialize.flags &= 0xFFFFFFF7;
                                        }
                                        if (user.photo != null) {
                                            tLdeserialize.photo = user.photo;
                                            tLdeserialize.flags |= 0x20;
                                        }
                                        else {
                                            tLdeserialize.photo = null;
                                            tLdeserialize.flags &= 0xFFFFFFDF;
                                        }
                                        user2 = tLdeserialize;
                                    }
                                }
                            }
                            catch (Exception ex) {
                                FileLog.e(ex);
                                user2 = user;
                            }
                        }
                        queryFinalized.dispose();
                    }
                    executeFast.requery();
                    final NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(user2.getObjectSize());
                    user2.serializeToStream(nativeByteBuffer);
                    executeFast.bindInteger(1, user2.id);
                    executeFast.bindString(2, this.formatUserSearchName(user2));
                    final TLRPC.UserStatus status = user2.status;
                    if (status != null) {
                        if (status instanceof TLRPC.TL_userStatusRecently) {
                            status.expires = -100;
                        }
                        else if (status instanceof TLRPC.TL_userStatusLastWeek) {
                            status.expires = -101;
                        }
                        else if (status instanceof TLRPC.TL_userStatusLastMonth) {
                            status.expires = -102;
                        }
                        executeFast.bindInteger(3, user2.status.expires);
                    }
                    else {
                        executeFast.bindInteger(3, 0);
                    }
                    executeFast.bindByteBuffer(4, nativeByteBuffer);
                    executeFast.step();
                    nativeByteBuffer.reuse();
                }
                executeFast.dispose();
            }
        }
    }
    
    private void saveDiffParamsInternal(final int lastSavedSeq, final int lastSavedPts, final int lastSavedDate, final int lastSavedQts) {
        try {
            if (this.lastSavedSeq == lastSavedSeq && this.lastSavedPts == lastSavedPts && this.lastSavedDate == lastSavedDate && this.lastQtsValue == lastSavedQts) {
                return;
            }
            final SQLitePreparedStatement executeFast = this.database.executeFast("UPDATE params SET seq = ?, pts = ?, date = ?, qts = ? WHERE id = 1");
            executeFast.bindInteger(1, lastSavedSeq);
            executeFast.bindInteger(2, lastSavedPts);
            executeFast.bindInteger(3, lastSavedDate);
            executeFast.bindInteger(4, lastSavedQts);
            executeFast.step();
            executeFast.dispose();
            this.lastSavedSeq = lastSavedSeq;
            this.lastSavedPts = lastSavedPts;
            this.lastSavedDate = lastSavedDate;
            this.lastSavedQts = lastSavedQts;
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private void updateDbToLastVersion(final int n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$UniaOWI4GiF_wLDJANnyTaeI1I0(this, n));
    }
    
    private void updateDialogsWithDeletedMessagesInternal(final ArrayList<Integer> list, final ArrayList<Long> list2, final int n) {
        if (Thread.currentThread().getId() == this.storageQueue.getId()) {
            try {
                final ArrayList<Long> list3 = new ArrayList<Long>();
                if (!list.isEmpty()) {
                    SQLitePreparedStatement sqLitePreparedStatement;
                    if (n != 0) {
                        list3.add((long)(-n));
                        sqLitePreparedStatement = this.database.executeFast("UPDATE dialogs SET last_mid = (SELECT mid FROM messages WHERE uid = ? AND date = (SELECT MAX(date) FROM messages WHERE uid = ?)) WHERE did = ?");
                    }
                    else {
                        final SQLiteCursor queryFinalized = this.database.queryFinalized(String.format(Locale.US, "SELECT did FROM dialogs WHERE last_mid IN(%s)", TextUtils.join((CharSequence)",", (Iterable)list)), new Object[0]);
                        while (queryFinalized.next()) {
                            list3.add(queryFinalized.longValue(0));
                        }
                        queryFinalized.dispose();
                        sqLitePreparedStatement = this.database.executeFast("UPDATE dialogs SET last_mid = (SELECT mid FROM messages WHERE uid = ? AND date = (SELECT MAX(date) FROM messages WHERE uid = ? AND date != 0)) WHERE did = ?");
                    }
                    this.database.beginTransaction();
                    for (int i = 0; i < list3.size(); ++i) {
                        final long longValue = list3.get(i);
                        sqLitePreparedStatement.requery();
                        sqLitePreparedStatement.bindLong(1, longValue);
                        sqLitePreparedStatement.bindLong(2, longValue);
                        sqLitePreparedStatement.bindLong(3, longValue);
                        sqLitePreparedStatement.step();
                    }
                    sqLitePreparedStatement.dispose();
                    this.database.commitTransaction();
                }
                else {
                    list3.add((long)(-n));
                }
                if (list2 != null) {
                    for (int j = 0; j < list2.size(); ++j) {
                        final Long n2 = list2.get(j);
                        if (!list3.contains(n2)) {
                            list3.add(n2);
                        }
                    }
                }
                final String join = TextUtils.join((CharSequence)",", (Iterable)list3);
                final TLRPC.TL_messages_dialogs tl_messages_dialogs = new TLRPC.TL_messages_dialogs();
                final ArrayList list4 = new ArrayList<TLRPC.EncryptedChat>();
                final ArrayList list5 = new ArrayList<Integer>();
                final ArrayList list6 = new ArrayList<Integer>();
                final ArrayList<Integer> list7 = new ArrayList<Integer>();
                final SQLiteCursor queryFinalized2 = this.database.queryFinalized(String.format(Locale.US, "SELECT d.did, d.last_mid, d.unread_count, d.date, m.data, m.read_state, m.mid, m.send_state, m.date, d.pts, d.inbox_max, d.outbox_max, d.pinned, d.unread_count_i, d.flags, d.folder_id, d.data FROM dialogs as d LEFT JOIN messages as m ON d.last_mid = m.mid WHERE d.did IN(%s)", join), new Object[0]);
                while (queryFinalized2.next()) {
                    final long longValue2 = queryFinalized2.longValue(0);
                    TLRPC.Dialog e;
                    if (DialogObject.isFolderDialogId(longValue2)) {
                        final TLRPC.TL_dialogFolder tl_dialogFolder = (TLRPC.TL_dialogFolder)(e = new TLRPC.TL_dialogFolder());
                        if (!queryFinalized2.isNull(16)) {
                            final NativeByteBuffer byteBufferValue = queryFinalized2.byteBufferValue(16);
                            if (byteBufferValue != null) {
                                tl_dialogFolder.folder = TLRPC.TL_folder.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                            }
                            else {
                                tl_dialogFolder.folder = new TLRPC.TL_folder();
                                tl_dialogFolder.folder.id = queryFinalized2.intValue(15);
                            }
                            byteBufferValue.reuse();
                            e = tl_dialogFolder;
                        }
                    }
                    else {
                        e = new TLRPC.TL_dialog();
                    }
                    e.id = longValue2;
                    e.top_message = queryFinalized2.intValue(1);
                    e.read_inbox_max_id = queryFinalized2.intValue(10);
                    e.read_outbox_max_id = queryFinalized2.intValue(11);
                    e.unread_count = queryFinalized2.intValue(2);
                    e.unread_mentions_count = queryFinalized2.intValue(13);
                    e.last_message_date = queryFinalized2.intValue(3);
                    e.pts = queryFinalized2.intValue(9);
                    int flags;
                    if (n == 0) {
                        flags = 0;
                    }
                    else {
                        flags = 1;
                    }
                    e.flags = flags;
                    e.pinnedNum = queryFinalized2.intValue(12);
                    e.pinned = (e.pinnedNum != 0);
                    e.unread_mark = ((queryFinalized2.intValue(14) & 0x1) != 0x0);
                    e.folder_id = queryFinalized2.intValue(15);
                    tl_messages_dialogs.dialogs.add(e);
                    final NativeByteBuffer byteBufferValue2 = queryFinalized2.byteBufferValue(4);
                    if (byteBufferValue2 != null) {
                        final TLRPC.Message tLdeserialize = TLRPC.Message.TLdeserialize(byteBufferValue2, byteBufferValue2.readInt32(false), false);
                        tLdeserialize.readAttachPath(byteBufferValue2, UserConfig.getInstance(this.currentAccount).clientUserId);
                        byteBufferValue2.reuse();
                        MessageObject.setUnreadFlags(tLdeserialize, queryFinalized2.intValue(5));
                        tLdeserialize.id = queryFinalized2.intValue(6);
                        tLdeserialize.send_state = queryFinalized2.intValue(7);
                        final int intValue = queryFinalized2.intValue(8);
                        if (intValue != 0) {
                            e.last_message_date = intValue;
                        }
                        tLdeserialize.dialog_id = e.id;
                        tl_messages_dialogs.messages.add(tLdeserialize);
                        addUsersAndChatsFromMessage(tLdeserialize, list5, list6);
                    }
                    final int n3 = (int)e.id;
                    final int n4 = (int)(e.id >> 32);
                    if (n3 != 0) {
                        if (n4 == 1) {
                            if (list6.contains(n3)) {
                                continue;
                            }
                            list6.add(n3);
                        }
                        else if (n3 > 0) {
                            if (list5.contains(n3)) {
                                continue;
                            }
                            list5.add(n3);
                        }
                        else {
                            final int n5 = -n3;
                            if (list6.contains(n5)) {
                                continue;
                            }
                            list6.add(n5);
                        }
                    }
                    else {
                        if (list7.contains(n4)) {
                            continue;
                        }
                        list7.add(n4);
                    }
                }
                queryFinalized2.dispose();
                if (!list7.isEmpty()) {
                    this.getEncryptedChatsInternal(TextUtils.join((CharSequence)",", (Iterable)list7), list4, list5);
                }
                if (!list6.isEmpty()) {
                    this.getChatsInternal(TextUtils.join((CharSequence)",", (Iterable)list6), tl_messages_dialogs.chats);
                }
                if (!list5.isEmpty()) {
                    this.getUsersInternal(TextUtils.join((CharSequence)",", (Iterable)list5), tl_messages_dialogs.users);
                }
                if (!tl_messages_dialogs.dialogs.isEmpty() || !list4.isEmpty()) {
                    MessagesController.getInstance(this.currentAccount).processDialogsUpdate(tl_messages_dialogs, list4);
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            return;
        }
        throw new RuntimeException("wrong db thread");
    }
    
    private void updateDialogsWithReadMessagesInternal(final ArrayList<Integer> list, final SparseLongArray sparseLongArray, final SparseLongArray sparseLongArray2, final ArrayList<Long> c) {
        try {
            final LongSparseArray longSparseArray = new LongSparseArray();
            final LongSparseArray longSparseArray2 = new LongSparseArray();
            final ArrayList<Integer> list2 = new ArrayList<Integer>();
            if (!isEmpty(list)) {
                final SQLiteCursor queryFinalized = this.database.queryFinalized(String.format(Locale.US, "SELECT uid, read_state, out FROM messages WHERE mid IN(%s)", TextUtils.join((CharSequence)",", (Iterable)list)), new Object[0]);
                while (queryFinalized.next()) {
                    if (queryFinalized.intValue(2) != 0) {
                        continue;
                    }
                    if (queryFinalized.intValue(1) != 0) {
                        continue;
                    }
                    final long longValue = queryFinalized.longValue(0);
                    final Integer n = (Integer)longSparseArray.get(longValue);
                    if (n == null) {
                        longSparseArray.put(longValue, (Object)1);
                    }
                    else {
                        longSparseArray.put(longValue, (Object)(n + 1));
                    }
                }
                queryFinalized.dispose();
            }
            else {
                if (!isEmpty(sparseLongArray)) {
                    for (int i = 0; i < sparseLongArray.size(); ++i) {
                        final int key = sparseLongArray.keyAt(i);
                        final long value = sparseLongArray.get(key);
                        final SQLiteCursor queryFinalized2 = this.database.queryFinalized(String.format(Locale.US, "SELECT COUNT(mid) FROM messages WHERE uid = %d AND mid > %d AND read_state IN(0,2) AND out = 0", key, value), new Object[0]);
                        if (queryFinalized2.next()) {
                            longSparseArray.put((long)key, (Object)queryFinalized2.intValue(0));
                        }
                        queryFinalized2.dispose();
                        final SQLitePreparedStatement executeFast = this.database.executeFast("UPDATE dialogs SET inbox_max = max((SELECT inbox_max FROM dialogs WHERE did = ?), ?) WHERE did = ?");
                        executeFast.requery();
                        final long n2 = key;
                        executeFast.bindLong(1, n2);
                        executeFast.bindInteger(2, (int)value);
                        executeFast.bindLong(3, n2);
                        executeFast.step();
                        executeFast.dispose();
                    }
                }
                if (!isEmpty(c)) {
                    final ArrayList<Long> list3 = new ArrayList<Long>(c);
                    final SQLiteCursor queryFinalized3 = this.database.queryFinalized(String.format(Locale.US, "SELECT uid, read_state, out, mention, mid FROM messages WHERE mid IN(%s)", TextUtils.join((CharSequence)",", (Iterable)c)), new Object[0]);
                    while (queryFinalized3.next()) {
                        final long longValue2 = queryFinalized3.longValue(0);
                        list3.remove(queryFinalized3.longValue(4));
                        if (queryFinalized3.intValue(1) < 2 && queryFinalized3.intValue(2) == 0 && queryFinalized3.intValue(3) == 1) {
                            final Integer n3 = (Integer)longSparseArray2.get(longValue2);
                            if (n3 == null) {
                                final SQLiteDatabase database = this.database;
                                final StringBuilder sb = new StringBuilder();
                                sb.append("SELECT unread_count_i FROM dialogs WHERE did = ");
                                sb.append(longValue2);
                                final SQLiteCursor queryFinalized4 = database.queryFinalized(sb.toString(), new Object[0]);
                                int intValue;
                                if (queryFinalized4.next()) {
                                    intValue = queryFinalized4.intValue(0);
                                }
                                else {
                                    intValue = 0;
                                }
                                queryFinalized4.dispose();
                                longSparseArray2.put(longValue2, (Object)Math.max(0, intValue - 1));
                            }
                            else {
                                longSparseArray2.put(longValue2, (Object)Math.max(0, n3 - 1));
                            }
                        }
                    }
                    queryFinalized3.dispose();
                    for (int j = 0; j < list3.size(); ++j) {
                        final int n4 = (int)((long)list3.get(j) >> 32);
                        if (n4 > 0 && !list2.contains(n4)) {
                            list2.add(n4);
                        }
                    }
                }
                if (!isEmpty(sparseLongArray2)) {
                    for (int k = 0; k < sparseLongArray2.size(); ++k) {
                        final int key2 = sparseLongArray2.keyAt(k);
                        final long value2 = sparseLongArray2.get(key2);
                        final SQLitePreparedStatement executeFast2 = this.database.executeFast("UPDATE dialogs SET outbox_max = max((SELECT outbox_max FROM dialogs WHERE did = ?), ?) WHERE did = ?");
                        executeFast2.requery();
                        final long n5 = key2;
                        executeFast2.bindLong(1, n5);
                        executeFast2.bindInteger(2, (int)value2);
                        executeFast2.bindLong(3, n5);
                        executeFast2.step();
                        executeFast2.dispose();
                    }
                }
            }
            final int n6 = 0;
            if (longSparseArray.size() > 0 || longSparseArray2.size() > 0) {
                this.database.beginTransaction();
                if (longSparseArray.size() > 0) {
                    final SQLitePreparedStatement executeFast3 = this.database.executeFast("UPDATE dialogs SET unread_count = ? WHERE did = ?");
                    for (int l = 0; l < longSparseArray.size(); ++l) {
                        executeFast3.requery();
                        executeFast3.bindInteger(1, (int)longSparseArray.valueAt(l));
                        executeFast3.bindLong(2, longSparseArray.keyAt(l));
                        executeFast3.step();
                    }
                    executeFast3.dispose();
                }
                if (longSparseArray2.size() > 0) {
                    final SQLitePreparedStatement executeFast4 = this.database.executeFast("UPDATE dialogs SET unread_count_i = ? WHERE did = ?");
                    for (int n7 = n6; n7 < longSparseArray2.size(); ++n7) {
                        executeFast4.requery();
                        executeFast4.bindInteger(1, (int)longSparseArray2.valueAt(n7));
                        executeFast4.bindLong(2, longSparseArray2.keyAt(n7));
                        executeFast4.step();
                    }
                    executeFast4.dispose();
                }
                this.database.commitTransaction();
            }
            MessagesController.getInstance(this.currentAccount).processDialogsUpdateRead((LongSparseArray<Integer>)longSparseArray, (LongSparseArray<Integer>)longSparseArray2);
            if (!list2.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).reloadMentionsCountForChannels(list2);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private long[] updateMessageStateAndIdInternal(final long p0, final Integer p1, final int p2, final int p3, final int p4) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: i2l            
        //     3: lstore          7
        //     5: aconst_null    
        //     6: astore          9
        //     8: aload_3        
        //     9: ifnonnull       178
        //    12: aload_0        
        //    13: getfield        org/telegram/messenger/MessagesStorage.database:Lorg/telegram/SQLite/SQLiteDatabase;
        //    16: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //    19: ldc_w           "SELECT mid FROM randoms WHERE random_id = %d LIMIT 1"
        //    22: iconst_1       
        //    23: anewarray       Ljava/lang/Object;
        //    26: dup            
        //    27: iconst_0       
        //    28: lload_1        
        //    29: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //    32: aastore        
        //    33: invokestatic    java/lang/String.format:(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //    36: iconst_0       
        //    37: anewarray       Ljava/lang/Object;
        //    40: invokevirtual   org/telegram/SQLite/SQLiteDatabase.queryFinalized:(Ljava/lang/String;[Ljava/lang/Object;)Lorg/telegram/SQLite/SQLiteCursor;
        //    43: astore          10
        //    45: aload           10
        //    47: astore          9
        //    49: aload           10
        //    51: invokevirtual   org/telegram/SQLite/SQLiteCursor.next:()Z
        //    54: ifeq            75
        //    57: aload           10
        //    59: astore          9
        //    61: aload           10
        //    63: iconst_0       
        //    64: invokevirtual   org/telegram/SQLite/SQLiteCursor.intValue:(I)I
        //    67: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    70: astore          11
        //    72: goto            78
        //    75: aload_3        
        //    76: astore          11
        //    78: aload           11
        //    80: astore_3       
        //    81: aload           10
        //    83: astore          9
        //    85: aload           10
        //    87: ifnull          146
        //    90: aload           10
        //    92: invokevirtual   org/telegram/SQLite/SQLiteCursor.dispose:()V
        //    95: aload           11
        //    97: astore_3       
        //    98: aload           10
        //   100: astore          9
        //   102: goto            146
        //   105: astore          11
        //   107: goto            123
        //   110: astore_3       
        //   111: aload           9
        //   113: astore          10
        //   115: goto            166
        //   118: astore          11
        //   120: aconst_null    
        //   121: astore          10
        //   123: aload           10
        //   125: astore          9
        //   127: aload           11
        //   129: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   132: aload           10
        //   134: ifnull          142
        //   137: aload           10
        //   139: invokevirtual   org/telegram/SQLite/SQLiteCursor.dispose:()V
        //   142: aload           10
        //   144: astore          9
        //   146: aload_3        
        //   147: ifnonnull       152
        //   150: aconst_null    
        //   151: areturn        
        //   152: aload_3        
        //   153: astore          11
        //   155: aload           9
        //   157: astore_3       
        //   158: goto            183
        //   161: astore_3       
        //   162: aload           9
        //   164: astore          10
        //   166: aload           10
        //   168: ifnull          176
        //   171: aload           10
        //   173: invokevirtual   org/telegram/SQLite/SQLiteCursor.dispose:()V
        //   176: aload_3        
        //   177: athrow         
        //   178: aload_3        
        //   179: astore          11
        //   181: aconst_null    
        //   182: astore_3       
        //   183: aload           11
        //   185: invokevirtual   java/lang/Integer.intValue:()I
        //   188: i2l            
        //   189: lstore          12
        //   191: iload           6
        //   193: ifeq            218
        //   196: iload           6
        //   198: i2l            
        //   199: bipush          32
        //   201: lshl           
        //   202: lstore_1       
        //   203: lload           12
        //   205: lload_1        
        //   206: lor            
        //   207: lstore          12
        //   209: lload_1        
        //   210: lload           7
        //   212: lor            
        //   213: lstore          14
        //   215: goto            222
        //   218: lload           7
        //   220: lstore          14
        //   222: aload_3        
        //   223: astore          10
        //   225: aload_0        
        //   226: getfield        org/telegram/messenger/MessagesStorage.database:Lorg/telegram/SQLite/SQLiteDatabase;
        //   229: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //   232: ldc_w           "SELECT uid FROM messages WHERE mid = %d LIMIT 1"
        //   235: iconst_1       
        //   236: anewarray       Ljava/lang/Object;
        //   239: dup            
        //   240: iconst_0       
        //   241: lload           12
        //   243: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   246: aastore        
        //   247: invokestatic    java/lang/String.format:(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   250: iconst_0       
        //   251: anewarray       Ljava/lang/Object;
        //   254: invokevirtual   org/telegram/SQLite/SQLiteDatabase.queryFinalized:(Ljava/lang/String;[Ljava/lang/Object;)Lorg/telegram/SQLite/SQLiteCursor;
        //   257: astore          9
        //   259: aload           9
        //   261: astore          10
        //   263: aload           9
        //   265: astore_3       
        //   266: aload           9
        //   268: invokevirtual   org/telegram/SQLite/SQLiteCursor.next:()Z
        //   271: ifeq            291
        //   274: aload           9
        //   276: astore          10
        //   278: aload           9
        //   280: astore_3       
        //   281: aload           9
        //   283: iconst_0       
        //   284: invokevirtual   org/telegram/SQLite/SQLiteCursor.longValue:(I)J
        //   287: lstore_1       
        //   288: goto            293
        //   291: lconst_0       
        //   292: lstore_1       
        //   293: lload_1        
        //   294: lstore          16
        //   296: aload           9
        //   298: ifnull          337
        //   301: aload           9
        //   303: invokevirtual   org/telegram/SQLite/SQLiteCursor.dispose:()V
        //   306: lload_1        
        //   307: lstore          16
        //   309: goto            337
        //   312: astore_3       
        //   313: goto            940
        //   316: astore          9
        //   318: aload_3        
        //   319: astore          10
        //   321: aload           9
        //   323: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   326: aload_3        
        //   327: ifnull          334
        //   330: aload_3        
        //   331: invokevirtual   org/telegram/SQLite/SQLiteCursor.dispose:()V
        //   334: lconst_0       
        //   335: lstore          16
        //   337: lload           16
        //   339: lconst_0       
        //   340: lcmp           
        //   341: ifne            346
        //   344: aconst_null    
        //   345: areturn        
        //   346: aconst_null    
        //   347: astore_3       
        //   348: aconst_null    
        //   349: astore          10
        //   351: lload           12
        //   353: lload           14
        //   355: lcmp           
        //   356: ifne            478
        //   359: iload           5
        //   361: ifeq            478
        //   364: aload_0        
        //   365: getfield        org/telegram/messenger/MessagesStorage.database:Lorg/telegram/SQLite/SQLiteDatabase;
        //   368: ldc_w           "UPDATE messages SET send_state = 0, date = ? WHERE mid = ?"
        //   371: invokevirtual   org/telegram/SQLite/SQLiteDatabase.executeFast:(Ljava/lang/String;)Lorg/telegram/SQLite/SQLitePreparedStatement;
        //   374: astore          9
        //   376: aload           9
        //   378: astore          10
        //   380: aload           9
        //   382: astore_3       
        //   383: aload           9
        //   385: iconst_1       
        //   386: iload           5
        //   388: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.bindInteger:(II)V
        //   391: aload           9
        //   393: astore          10
        //   395: aload           9
        //   397: astore_3       
        //   398: aload           9
        //   400: iconst_2       
        //   401: lload           14
        //   403: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.bindLong:(IJ)V
        //   406: aload           9
        //   408: astore          10
        //   410: aload           9
        //   412: astore_3       
        //   413: aload           9
        //   415: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.step:()I
        //   418: pop            
        //   419: aload           9
        //   421: ifnull          452
        //   424: aload           9
        //   426: astore_3       
        //   427: goto            448
        //   430: astore_3       
        //   431: goto            466
        //   434: astore          9
        //   436: aload_3        
        //   437: astore          10
        //   439: aload           9
        //   441: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   444: aload_3        
        //   445: ifnull          452
        //   448: aload_3        
        //   449: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.dispose:()V
        //   452: iconst_2       
        //   453: newarray        J
        //   455: dup            
        //   456: iconst_0       
        //   457: lload           16
        //   459: lastore        
        //   460: dup            
        //   461: iconst_1       
        //   462: lload           7
        //   464: lastore        
        //   465: areturn        
        //   466: aload           10
        //   468: ifnull          476
        //   471: aload           10
        //   473: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.dispose:()V
        //   476: aload_3        
        //   477: athrow         
        //   478: aload_0        
        //   479: getfield        org/telegram/messenger/MessagesStorage.database:Lorg/telegram/SQLite/SQLiteDatabase;
        //   482: ldc_w           "UPDATE messages SET mid = ?, send_state = 0 WHERE mid = ?"
        //   485: invokevirtual   org/telegram/SQLite/SQLiteDatabase.executeFast:(Ljava/lang/String;)Lorg/telegram/SQLite/SQLitePreparedStatement;
        //   488: astore          10
        //   490: aload           10
        //   492: astore_3       
        //   493: aload           10
        //   495: iconst_1       
        //   496: lload           14
        //   498: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.bindLong:(IJ)V
        //   501: aload           10
        //   503: astore_3       
        //   504: aload           10
        //   506: iconst_2       
        //   507: lload           12
        //   509: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.bindLong:(IJ)V
        //   512: aload           10
        //   514: astore_3       
        //   515: aload           10
        //   517: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.step:()I
        //   520: pop            
        //   521: aload           10
        //   523: astore_3       
        //   524: aload           10
        //   526: ifnull          650
        //   529: goto            643
        //   532: astore          10
        //   534: aconst_null    
        //   535: astore_3       
        //   536: goto            929
        //   539: astore_3       
        //   540: aconst_null    
        //   541: astore          10
        //   543: aload           10
        //   545: astore_3       
        //   546: aload_0        
        //   547: getfield        org/telegram/messenger/MessagesStorage.database:Lorg/telegram/SQLite/SQLiteDatabase;
        //   550: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //   553: ldc_w           "DELETE FROM messages WHERE mid = %d"
        //   556: iconst_1       
        //   557: anewarray       Ljava/lang/Object;
        //   560: dup            
        //   561: iconst_0       
        //   562: lload           12
        //   564: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   567: aastore        
        //   568: invokestatic    java/lang/String.format:(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   571: invokevirtual   org/telegram/SQLite/SQLiteDatabase.executeFast:(Ljava/lang/String;)Lorg/telegram/SQLite/SQLitePreparedStatement;
        //   574: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.stepThis:()Lorg/telegram/SQLite/SQLitePreparedStatement;
        //   577: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.dispose:()V
        //   580: aload           10
        //   582: astore_3       
        //   583: aload_0        
        //   584: getfield        org/telegram/messenger/MessagesStorage.database:Lorg/telegram/SQLite/SQLiteDatabase;
        //   587: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //   590: ldc_w           "DELETE FROM messages_seq WHERE mid = %d"
        //   593: iconst_1       
        //   594: anewarray       Ljava/lang/Object;
        //   597: dup            
        //   598: iconst_0       
        //   599: lload           12
        //   601: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   604: aastore        
        //   605: invokestatic    java/lang/String.format:(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   608: invokevirtual   org/telegram/SQLite/SQLiteDatabase.executeFast:(Ljava/lang/String;)Lorg/telegram/SQLite/SQLitePreparedStatement;
        //   611: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.stepThis:()Lorg/telegram/SQLite/SQLitePreparedStatement;
        //   614: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.dispose:()V
        //   617: goto            635
        //   620: astore          10
        //   622: goto            929
        //   625: astore          9
        //   627: aload           10
        //   629: astore_3       
        //   630: aload           9
        //   632: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   635: aload           10
        //   637: astore_3       
        //   638: aload           10
        //   640: ifnull          650
        //   643: aload           10
        //   645: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.dispose:()V
        //   648: aconst_null    
        //   649: astore_3       
        //   650: aload_3        
        //   651: astore          10
        //   653: aload_0        
        //   654: getfield        org/telegram/messenger/MessagesStorage.database:Lorg/telegram/SQLite/SQLiteDatabase;
        //   657: ldc_w           "UPDATE media_v2 SET mid = ? WHERE mid = ?"
        //   660: invokevirtual   org/telegram/SQLite/SQLiteDatabase.executeFast:(Ljava/lang/String;)Lorg/telegram/SQLite/SQLitePreparedStatement;
        //   663: astore          9
        //   665: aload           9
        //   667: astore          10
        //   669: aload           9
        //   671: astore_3       
        //   672: aload           9
        //   674: iconst_1       
        //   675: lload           14
        //   677: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.bindLong:(IJ)V
        //   680: aload           9
        //   682: astore          10
        //   684: aload           9
        //   686: astore_3       
        //   687: aload           9
        //   689: iconst_2       
        //   690: lload           12
        //   692: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.bindLong:(IJ)V
        //   695: aload           9
        //   697: astore          10
        //   699: aload           9
        //   701: astore_3       
        //   702: aload           9
        //   704: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.step:()I
        //   707: pop            
        //   708: aload           9
        //   710: astore          10
        //   712: aload           9
        //   714: ifnull          793
        //   717: aload           9
        //   719: astore_3       
        //   720: goto            786
        //   723: astore_3       
        //   724: goto            917
        //   727: astore          10
        //   729: aload_3        
        //   730: astore          10
        //   732: aload_0        
        //   733: getfield        org/telegram/messenger/MessagesStorage.database:Lorg/telegram/SQLite/SQLiteDatabase;
        //   736: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //   739: ldc_w           "DELETE FROM media_v2 WHERE mid = %d"
        //   742: iconst_1       
        //   743: anewarray       Ljava/lang/Object;
        //   746: dup            
        //   747: iconst_0       
        //   748: lload           12
        //   750: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   753: aastore        
        //   754: invokestatic    java/lang/String.format:(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   757: invokevirtual   org/telegram/SQLite/SQLiteDatabase.executeFast:(Ljava/lang/String;)Lorg/telegram/SQLite/SQLitePreparedStatement;
        //   760: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.stepThis:()Lorg/telegram/SQLite/SQLitePreparedStatement;
        //   763: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.dispose:()V
        //   766: goto            779
        //   769: astore          9
        //   771: aload_3        
        //   772: astore          10
        //   774: aload           9
        //   776: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   779: aload_3        
        //   780: astore          10
        //   782: aload_3        
        //   783: ifnull          793
        //   786: aload_3        
        //   787: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.dispose:()V
        //   790: aconst_null    
        //   791: astore          10
        //   793: aload           10
        //   795: astore_3       
        //   796: aload_0        
        //   797: getfield        org/telegram/messenger/MessagesStorage.database:Lorg/telegram/SQLite/SQLiteDatabase;
        //   800: ldc_w           "UPDATE dialogs SET last_mid = ? WHERE last_mid = ?"
        //   803: invokevirtual   org/telegram/SQLite/SQLiteDatabase.executeFast:(Ljava/lang/String;)Lorg/telegram/SQLite/SQLitePreparedStatement;
        //   806: astore          9
        //   808: aload           9
        //   810: astore_3       
        //   811: aload           9
        //   813: astore          10
        //   815: aload           9
        //   817: iconst_1       
        //   818: lload           14
        //   820: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.bindLong:(IJ)V
        //   823: aload           9
        //   825: astore_3       
        //   826: aload           9
        //   828: astore          10
        //   830: aload           9
        //   832: iconst_2       
        //   833: lload           12
        //   835: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.bindLong:(IJ)V
        //   838: aload           9
        //   840: astore_3       
        //   841: aload           9
        //   843: astore          10
        //   845: aload           9
        //   847: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.step:()I
        //   850: pop            
        //   851: aload           9
        //   853: ifnull          888
        //   856: aload           9
        //   858: astore          10
        //   860: goto            883
        //   863: astore          10
        //   865: goto            906
        //   868: astore          9
        //   870: aload           10
        //   872: astore_3       
        //   873: aload           9
        //   875: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   878: aload           10
        //   880: ifnull          888
        //   883: aload           10
        //   885: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.dispose:()V
        //   888: iconst_2       
        //   889: newarray        J
        //   891: dup            
        //   892: iconst_0       
        //   893: lload           16
        //   895: lastore        
        //   896: dup            
        //   897: iconst_1       
        //   898: aload           11
        //   900: invokevirtual   java/lang/Integer.intValue:()I
        //   903: i2l            
        //   904: lastore        
        //   905: areturn        
        //   906: aload_3        
        //   907: ifnull          914
        //   910: aload_3        
        //   911: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.dispose:()V
        //   914: aload           10
        //   916: athrow         
        //   917: aload           10
        //   919: ifnull          927
        //   922: aload           10
        //   924: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.dispose:()V
        //   927: aload_3        
        //   928: athrow         
        //   929: aload_3        
        //   930: ifnull          937
        //   933: aload_3        
        //   934: invokevirtual   org/telegram/SQLite/SQLitePreparedStatement.dispose:()V
        //   937: aload           10
        //   939: athrow         
        //   940: aload           10
        //   942: ifnull          950
        //   945: aload           10
        //   947: invokevirtual   org/telegram/SQLite/SQLiteCursor.dispose:()V
        //   950: aload_3        
        //   951: athrow         
        //   952: astore_3       
        //   953: goto            543
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  12     45     118    123    Ljava/lang/Exception;
        //  12     45     110    118    Any
        //  49     57     105    110    Ljava/lang/Exception;
        //  49     57     161    166    Any
        //  61     72     105    110    Ljava/lang/Exception;
        //  61     72     161    166    Any
        //  127    132    161    166    Any
        //  225    259    316    337    Ljava/lang/Exception;
        //  225    259    312    952    Any
        //  266    274    316    337    Ljava/lang/Exception;
        //  266    274    312    952    Any
        //  281    288    316    337    Ljava/lang/Exception;
        //  281    288    312    952    Any
        //  321    326    312    952    Any
        //  364    376    434    448    Ljava/lang/Exception;
        //  364    376    430    478    Any
        //  383    391    434    448    Ljava/lang/Exception;
        //  383    391    430    478    Any
        //  398    406    434    448    Ljava/lang/Exception;
        //  398    406    430    478    Any
        //  413    419    434    448    Ljava/lang/Exception;
        //  413    419    430    478    Any
        //  439    444    430    478    Any
        //  478    490    539    543    Ljava/lang/Exception;
        //  478    490    532    539    Any
        //  493    501    952    956    Ljava/lang/Exception;
        //  493    501    620    625    Any
        //  504    512    952    956    Ljava/lang/Exception;
        //  504    512    620    625    Any
        //  515    521    952    956    Ljava/lang/Exception;
        //  515    521    620    625    Any
        //  546    580    625    635    Ljava/lang/Exception;
        //  546    580    620    625    Any
        //  583    617    625    635    Ljava/lang/Exception;
        //  583    617    620    625    Any
        //  630    635    620    625    Any
        //  653    665    727    786    Ljava/lang/Exception;
        //  653    665    723    929    Any
        //  672    680    727    786    Ljava/lang/Exception;
        //  672    680    723    929    Any
        //  687    695    727    786    Ljava/lang/Exception;
        //  687    695    723    929    Any
        //  702    708    727    786    Ljava/lang/Exception;
        //  702    708    723    929    Any
        //  732    766    769    779    Ljava/lang/Exception;
        //  732    766    723    929    Any
        //  774    779    723    929    Any
        //  796    808    868    883    Ljava/lang/Exception;
        //  796    808    863    917    Any
        //  815    823    868    883    Ljava/lang/Exception;
        //  815    823    863    917    Any
        //  830    838    868    883    Ljava/lang/Exception;
        //  830    838    863    917    Any
        //  845    851    868    883    Ljava/lang/Exception;
        //  845    851    863    917    Any
        //  873    878    863    917    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 489 out-of-bounds for length 489
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    private void updateUsersInternal(final ArrayList<TLRPC.User> list, final boolean b, final boolean b2) {
        if (Thread.currentThread().getId() == this.storageQueue.getId()) {
            final int n = 0;
            Label_0153: {
                if (!b) {
                    break Label_0153;
                }
                Label_0035: {
                    if (!b2) {
                        break Label_0035;
                    }
                    try {
                        this.database.beginTransaction();
                        final SQLitePreparedStatement executeFast = this.database.executeFast("UPDATE users SET status = ? WHERE uid = ?");
                        for (int size = list.size(), i = 0; i < size; ++i) {
                            final TLRPC.User user = list.get(i);
                            executeFast.requery();
                            if (user.status != null) {
                                executeFast.bindInteger(1, user.status.expires);
                            }
                            else {
                                executeFast.bindInteger(1, 0);
                            }
                            executeFast.bindInteger(2, user.id);
                            executeFast.step();
                        }
                        executeFast.dispose();
                        if (b2) {
                            this.database.commitTransaction();
                            return;
                        }
                        return;
                        ArrayList<TLRPC.User> list2 = null;
                        int index = 0;
                        TLRPC.User user2;
                        SparseArray sparseArray = null;
                        TLRPC.User user3;
                        int index2;
                        TLRPC.User user4;
                        StringBuilder sb = null;
                        int size2;
                        int size3;
                        Block_18_Outer:Block_16_Outer:Block_17_Outer:
                        while (true) {
                            user2 = list2.get(index);
                            user3 = (TLRPC.User)sparseArray.get(user2.id);
                            Label_0274: {
                                while (true) {
                                    Label_0408_Outer:Label_0217_Outer:
                                    while (true) {
                                        while (true) {
                                            Label_0408:Block_13_Outer:
                                            while (true) {
                                            Label_0182:
                                                while (true) {
                                                    while (true) {
                                                        Block_11: {
                                                            break Block_11;
                                                            while (true) {
                                                                while (true) {
                                                                    while (true) {
                                                                        user2.first_name = user3.first_name;
                                                                        user2.last_name = user3.last_name;
                                                                        while (true) {
                                                                            Label_0432: {
                                                                                Label_0356: {
                                                                                    break Label_0356;
                                                                                    this.database.beginTransaction();
                                                                                    break Label_0432;
                                                                                    this.database.commitTransaction();
                                                                                    return;
                                                                                    user2.phone = user3.phone;
                                                                                    break Label_0408;
                                                                                }
                                                                                user2.username = user3.username;
                                                                                break Label_0408;
                                                                            }
                                                                            this.putUsersInternal(list2);
                                                                            continue Block_16_Outer;
                                                                        }
                                                                        user4 = list.get(index2);
                                                                        break Label_0408;
                                                                        continue Block_18_Outer;
                                                                    }
                                                                    Label_0246: {
                                                                        list2 = new ArrayList<TLRPC.User>();
                                                                    }
                                                                    this.getUsersInternal(sb.toString(), list2);
                                                                    size2 = list2.size();
                                                                    index = n;
                                                                    break Label_0274;
                                                                    sb.append(user4.id);
                                                                    sparseArray.put(user4.id, (Object)user4);
                                                                    ++index2;
                                                                    break Label_0182;
                                                                    Label_0369:
                                                                    break Label_0182;
                                                                    continue Block_16_Outer;
                                                                }
                                                                continue Block_13_Outer;
                                                            }
                                                            ++index;
                                                            break Label_0274;
                                                        }
                                                        continue Label_0217_Outer;
                                                    }
                                                    sb = new StringBuilder();
                                                    sparseArray = new SparseArray();
                                                    size3 = list.size();
                                                    index2 = 0;
                                                    continue Label_0182;
                                                }
                                                user2.photo = user3.photo;
                                                continue Label_0408;
                                            }
                                            sb.append(",");
                                            continue Block_17_Outer;
                                        }
                                        Label_0390: {
                                            continue Label_0408_Outer;
                                        }
                                    }
                                    Label_0414: {
                                        continue;
                                    }
                                }
                            }
                            continue Block_16_Outer;
                        }
                    }
                    // iftrue(Label_0408:, user3 == null)
                    // iftrue(Label_0456:, !b2)
                    // iftrue(Label_0217:, sb.length() == 0)
                    // iftrue(Label_0356:, UserObject.isContact(user2))
                    // iftrue(Label_0390:, user3.photo == null)
                    // iftrue(Label_0432:, !b2)
                    // iftrue(Label_0246:, index2 >= size3)
                    // iftrue(Label_0369:, user3.first_name == null || user3.last_name == null)
                    // iftrue(Label_0408:, user3.phone == null)
                    // iftrue(Label_0456:, list2.isEmpty())
                    // iftrue(Label_0414:, index >= size2)
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
            }
            Label_0456: {
                return;
            }
        }
        throw new RuntimeException("wrong db thread");
    }
    
    public void addRecentLocalFile(final String s, final String s2, final TLRPC.Document document) {
        if (s != null && s.length() != 0) {
            if ((s2 != null && s2.length() != 0) || document != null) {
                this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$sz8fEb44b64gI189wg0lApn07kU(this, document, s, s2));
            }
        }
    }
    
    public void applyPhoneBookUpdates(final String s, final String s2) {
        if (s.length() == 0 && s2.length() == 0) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$OreTmYrmWTgoW8Zk3cD5yTn7vp0(this, s, s2));
    }
    
    public void checkIfFolderEmpty(final int n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$icwTQAKHt7cMla2SxcMIgCt_hLo(this, n));
    }
    
    public boolean checkMessageByRandomId(final long n) {
        final boolean[] array = { false };
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$d1ma4ral7Pvbl0aQnDGt6um4wvc(this, n, array, countDownLatch));
        try {
            countDownLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return array[0];
    }
    
    public boolean checkMessageId(final long n, final int n2) {
        final boolean[] array = { false };
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$0gsEtoIKwqYGtFeFeKyba_2TLjY(this, n, n2, array, countDownLatch));
        try {
            countDownLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return array[0];
    }
    
    public void cleanup(final boolean b) {
        if (!b) {
            this.storageQueue.cleanupQueue();
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$ekOTDfJDLwKOj7vRXd_V4MXUJZc(this, b));
    }
    
    public void clearDownloadQueue(final int n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$cshF_4Ao69vigcUys96seLmbz8c(this, n));
    }
    
    public void clearSentMedia() {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$4rfOeATzWNWfd2pWwZkHIx6T8OU(this));
    }
    
    public void clearUserPhoto(final int n, final long n2) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Cj9Zle7GEzC4ItOsJHnwvLaRg2g(this, n, n2));
    }
    
    public void clearUserPhotos(final int n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$1fRs_OhnqRmo_7LJUTfMP7QEmPg(this, n));
    }
    
    public void clearWebRecent(final int n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$IAAcgw0MEJsvXeLksE_HqIMUybc(this, n));
    }
    
    public void closeHolesInMedia(final long n, final int i, final int j, int k) {
        while (true) {
            if (k < 0) {
                try {
                    SQLiteCursor sqLiteCursor = this.database.queryFinalized(String.format(Locale.US, "SELECT type, start, end FROM media_holes_v2 WHERE uid = %d AND type >= 0 AND ((end >= %d AND end <= %d) OR (start >= %d AND start <= %d) OR (start >= %d AND end <= %d) OR (start <= %d AND end >= %d))", n, i, j, i, j, i, j, i, j), new Object[0]);
                    while (true) {
                        ArrayList<Hole> list = null;
                        while (sqLiteCursor.next()) {
                            ArrayList<Hole> list2;
                            if ((list2 = list) == null) {
                                list2 = new ArrayList<Hole>();
                            }
                            k = sqLiteCursor.intValue(0);
                            final int intValue = sqLiteCursor.intValue(1);
                            final int intValue2 = sqLiteCursor.intValue(2);
                            if (intValue != intValue2 || intValue != 1) {
                                list2.add(new Hole(k, intValue, intValue2));
                            }
                            list = list2;
                        }
                        sqLiteCursor.dispose();
                        if (list != null) {
                            Hole hole;
                            SQLitePreparedStatement executeFast;
                            for (k = 0; k < list.size(); ++k) {
                                hole = list.get(k);
                                if (j >= hole.end - 1 && i <= hole.start + 1) {
                                    this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d AND type = %d AND start = %d AND end = %d", n, hole.type, hole.start, hole.end)).stepThis().dispose();
                                }
                                else if (j >= hole.end - 1) {
                                    if (hole.end != i) {
                                        try {
                                            this.database.executeFast(String.format(Locale.US, "UPDATE media_holes_v2 SET end = %d WHERE uid = %d AND type = %d AND start = %d AND end = %d", i, n, hole.type, hole.start, hole.end)).stepThis().dispose();
                                        }
                                        catch (Exception ex) {
                                            FileLog.e(ex);
                                        }
                                    }
                                }
                                else if (i <= hole.start + 1) {
                                    if (hole.start != j) {
                                        try {
                                            this.database.executeFast(String.format(Locale.US, "UPDATE media_holes_v2 SET start = %d WHERE uid = %d AND type = %d AND start = %d AND end = %d", j, n, hole.type, hole.start, hole.end)).stepThis().dispose();
                                        }
                                        catch (Exception ex2) {
                                            FileLog.e(ex2);
                                        }
                                    }
                                }
                                else {
                                    this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d AND type = %d AND start = %d AND end = %d", n, hole.type, hole.start, hole.end)).stepThis().dispose();
                                    executeFast = this.database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
                                    executeFast.requery();
                                    executeFast.bindLong(1, n);
                                    executeFast.bindInteger(2, hole.type);
                                    executeFast.bindInteger(3, hole.start);
                                    executeFast.bindInteger(4, i);
                                    executeFast.step();
                                    executeFast.requery();
                                    executeFast.bindLong(1, n);
                                    executeFast.bindInteger(2, hole.type);
                                    executeFast.bindInteger(3, j);
                                    executeFast.bindInteger(4, hole.end);
                                    executeFast.step();
                                    executeFast.dispose();
                                }
                            }
                            return;
                        }
                        return;
                        sqLiteCursor = this.database.queryFinalized(String.format(Locale.US, "SELECT type, start, end FROM media_holes_v2 WHERE uid = %d AND type = %d AND ((end >= %d AND end <= %d) OR (start >= %d AND start <= %d) OR (start >= %d AND end <= %d) OR (start <= %d AND end >= %d))", n, k, i, j, i, j, i, j, i, j), new Object[0]);
                        continue;
                    }
                }
                catch (Exception ex3) {
                    FileLog.e(ex3);
                }
                return;
            }
            continue;
        }
    }
    
    public long createPendingTask(final NativeByteBuffer nativeByteBuffer) {
        if (nativeByteBuffer == null) {
            return 0L;
        }
        final long andAdd = this.lastTaskId.getAndAdd(1L);
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$TxzHvDLT8O_Bs3__p_EFkrwY3ws(this, andAdd, nativeByteBuffer));
        return andAdd;
    }
    
    public void createTaskForMid(final int n, final int n2, final int n3, final int n4, final int n5, final boolean b) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$KFVjweRV_KgXgJdzwgxmQRw4vps(this, n3, n4, n5, n, n2, b));
    }
    
    public void createTaskForSecretChat(final int n, final int n2, final int n3, final int n4, final ArrayList<Long> list) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$0iUmdZ5k8jYla2OxXvPUMMd0G6M(this, list, n, n4, n2, n3));
    }
    
    public void deleteBlockedUser(final int n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$0TaEdYhzl8U_O7oEChUGdj3lNyw(this, n));
    }
    
    public void deleteContacts(final ArrayList<Integer> list) {
        if (list != null) {
            if (!list.isEmpty()) {
                this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$GMUC_3wWZaXGMALhkVNJZ4ROQdc(this, list));
            }
        }
    }
    
    public void deleteDialog(final long n, final int n2) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$gHn5_xuiEvlIe5KWph9_QlfcuBk(this, n2, n));
    }
    
    protected void deletePushMessages(final long l, final ArrayList<Integer> list) {
        try {
            this.database.executeFast(String.format(Locale.US, "DELETE FROM unread_push_messages WHERE uid = %d AND mid IN(%s)", l, TextUtils.join((CharSequence)",", (Iterable)list))).stepThis().dispose();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void deleteUserChannelHistory(final int n, final int n2) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$_PQj73otok1WW_acAxeuk_40Zls(this, n, n2));
    }
    
    public void doneHolesInMedia(final long n, int i, final int n2) throws Exception {
        final int n3 = 0;
        if (n2 == -1) {
            if (i == 0) {
                this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d", n)).stepThis().dispose();
            }
            else {
                this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d AND start = 0", n)).stepThis().dispose();
            }
            final SQLitePreparedStatement executeFast = this.database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
            for (i = n3; i < 5; ++i) {
                executeFast.requery();
                executeFast.bindLong(1, n);
                executeFast.bindInteger(2, i);
                executeFast.bindInteger(3, 1);
                executeFast.bindInteger(4, 1);
                executeFast.step();
            }
            executeFast.dispose();
        }
        else {
            if (i == 0) {
                this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d AND type = %d", n, n2)).stepThis().dispose();
            }
            else {
                this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d AND type = %d AND start = 0", n, n2)).stepThis().dispose();
            }
            final SQLitePreparedStatement executeFast2 = this.database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
            executeFast2.requery();
            executeFast2.bindLong(1, n);
            executeFast2.bindInteger(2, n2);
            executeFast2.bindInteger(3, 1);
            executeFast2.bindInteger(4, 1);
            executeFast2.step();
            executeFast2.dispose();
        }
    }
    
    public void emptyMessagesMedia(final ArrayList<Integer> list) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$bzcgnKbuFLtWEX1lODvqWEyFuDo(this, list));
    }
    
    public void getBlockedUsers() {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$4q7FLepIWnDxVXQ6fo32wEXrjW8(this));
    }
    
    public void getBotCache(final String s, final RequestDelegate requestDelegate) {
        if (s != null) {
            if (requestDelegate != null) {
                this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$cYU0WnBJxesdCVkll0wOXZ3U0RE(this, ConnectionsManager.getInstance(this.currentAccount).getCurrentTime(), s, requestDelegate));
            }
        }
    }
    
    public void getCachedPhoneBook(final boolean b) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$IEfB3sC9k_J4K6n4lzcQAX4QVAM(this, b));
    }
    
    public int getChannelPtsSync(final int n) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Integer[] array = { 0 };
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$5zCr421jp6BG9VEI5v5DJ7UN8Ng(this, n, array, countDownLatch));
        try {
            countDownLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return array[0];
    }
    
    public TLRPC.Chat getChat(final int i) {
        final TLRPC.Chat chat = null;
        TLRPC.Chat chat2;
        try {
            final ArrayList list = new ArrayList<TLRPC.Chat>();
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(i);
            this.getChatsInternal(sb.toString(), list);
            chat2 = chat;
            if (!list.isEmpty()) {
                chat2 = list.get(0);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
            chat2 = chat;
        }
        return chat2;
    }
    
    public TLRPC.Chat getChatSync(final int n) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final TLRPC.Chat[] array = { null };
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$coeKkTQyQbVSBUMnXP7SoD3ukxc(this, array, n, countDownLatch));
        try {
            countDownLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return array[0];
    }
    
    public void getChatsInternal(String queryFinalized, final ArrayList<TLRPC.Chat> list) throws Exception {
        if (queryFinalized != null && queryFinalized.length() != 0) {
            if (list != null) {
                queryFinalized = (String)this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM chats WHERE uid IN(%s)", queryFinalized), new Object[0]);
                while (((SQLiteCursor)queryFinalized).next()) {
                    try {
                        final NativeByteBuffer byteBufferValue = ((SQLiteCursor)queryFinalized).byteBufferValue(0);
                        if (byteBufferValue == null) {
                            continue;
                        }
                        final TLRPC.Chat tLdeserialize = TLRPC.Chat.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                        byteBufferValue.reuse();
                        if (tLdeserialize == null) {
                            continue;
                        }
                        list.add(tLdeserialize);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
                ((SQLiteCursor)queryFinalized).dispose();
            }
        }
    }
    
    public void getContacts() {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$c0C_JwfaP_juJcmhUEmKrLugWTs(this));
    }
    
    public SQLiteDatabase getDatabase() {
        return this.database;
    }
    
    public long getDatabaseSize() {
        final File cacheFile = this.cacheFile;
        long n = 0L;
        if (cacheFile != null) {
            n = 0L + cacheFile.length();
        }
        final File shmCacheFile = this.shmCacheFile;
        long n2 = n;
        if (shmCacheFile != null) {
            n2 = n + shmCacheFile.length();
        }
        return n2;
    }
    
    public void getDialogFolderId(final long n, final IntCallback intCallback) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$SkuV6Hpjk5GQ4ZDcr1sIT7ixm50(this, n, intCallback));
    }
    
    public void getDialogPhotos(final int n, final int n2, final long n3, final int n4) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$cZWPpHEko_cU5qbg57W0VB3uiqc(this, n3, n, n2, n4));
    }
    
    public int getDialogReadMax(final boolean b, final long n) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Integer[] array = { 0 };
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$NotrdRfLQ2qAM8nf7f6dNfZC9LQ(this, b, n, array, countDownLatch));
        try {
            countDownLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return array[0];
    }
    
    public void getDialogs(final int n, final int n2, final int n3) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$xgSn2rhYdKpJx8E6odU_ONY7l64(this, n, n2, n3));
    }
    
    public void getDownloadQueue(final int n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$65MKjebMe2zja_JD_jfTGLf_uoQ(this, n));
    }
    
    public TLRPC.EncryptedChat getEncryptedChat(final int i) {
        final TLRPC.EncryptedChat encryptedChat = null;
        TLRPC.EncryptedChat encryptedChat2;
        try {
            final ArrayList list = new ArrayList<TLRPC.EncryptedChat>();
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(i);
            this.getEncryptedChatsInternal(sb.toString(), list, null);
            encryptedChat2 = encryptedChat;
            if (!list.isEmpty()) {
                encryptedChat2 = list.get(0);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
            encryptedChat2 = encryptedChat;
        }
        return encryptedChat2;
    }
    
    public void getEncryptedChat(final int n, final CountDownLatch countDownLatch, final ArrayList<TLObject> list) {
        if (countDownLatch != null) {
            if (list != null) {
                this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$T3CnFGNE_u1NlDHvs1A5BarAi2Q(this, n, list, countDownLatch));
            }
        }
    }
    
    public void getEncryptedChatsInternal(String queryFinalized, final ArrayList<TLRPC.EncryptedChat> list, final ArrayList<Integer> list2) throws Exception {
        if (queryFinalized != null && queryFinalized.length() != 0) {
            if (list != null) {
                queryFinalized = (String)this.database.queryFinalized(String.format(Locale.US, "SELECT data, user, g, authkey, ttl, layer, seq_in, seq_out, use_count, exchange_id, key_date, fprint, fauthkey, khash, in_seq_no, admin_id, mtproto_seq FROM enc_chats WHERE uid IN(%s)", queryFinalized), new Object[0]);
                while (((SQLiteCursor)queryFinalized).next()) {
                    try {
                        final NativeByteBuffer byteBufferValue = ((SQLiteCursor)queryFinalized).byteBufferValue(0);
                        if (byteBufferValue == null) {
                            continue;
                        }
                        final TLRPC.EncryptedChat tLdeserialize = TLRPC.EncryptedChat.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                        byteBufferValue.reuse();
                        if (tLdeserialize == null) {
                            continue;
                        }
                        tLdeserialize.user_id = ((SQLiteCursor)queryFinalized).intValue(1);
                        if (list2 != null && !list2.contains(tLdeserialize.user_id)) {
                            list2.add(tLdeserialize.user_id);
                        }
                        tLdeserialize.a_or_b = ((SQLiteCursor)queryFinalized).byteArrayValue(2);
                        tLdeserialize.auth_key = ((SQLiteCursor)queryFinalized).byteArrayValue(3);
                        tLdeserialize.ttl = ((SQLiteCursor)queryFinalized).intValue(4);
                        tLdeserialize.layer = ((SQLiteCursor)queryFinalized).intValue(5);
                        tLdeserialize.seq_in = ((SQLiteCursor)queryFinalized).intValue(6);
                        tLdeserialize.seq_out = ((SQLiteCursor)queryFinalized).intValue(7);
                        final int intValue = ((SQLiteCursor)queryFinalized).intValue(8);
                        tLdeserialize.key_use_count_in = (short)(intValue >> 16);
                        tLdeserialize.key_use_count_out = (short)intValue;
                        tLdeserialize.exchange_id = ((SQLiteCursor)queryFinalized).longValue(9);
                        tLdeserialize.key_create_date = ((SQLiteCursor)queryFinalized).intValue(10);
                        tLdeserialize.future_key_fingerprint = ((SQLiteCursor)queryFinalized).longValue(11);
                        tLdeserialize.future_auth_key = ((SQLiteCursor)queryFinalized).byteArrayValue(12);
                        tLdeserialize.key_hash = ((SQLiteCursor)queryFinalized).byteArrayValue(13);
                        tLdeserialize.in_seq_no = ((SQLiteCursor)queryFinalized).intValue(14);
                        final int intValue2 = ((SQLiteCursor)queryFinalized).intValue(15);
                        if (intValue2 != 0) {
                            tLdeserialize.admin_id = intValue2;
                        }
                        tLdeserialize.mtproto_seq = ((SQLiteCursor)queryFinalized).intValue(16);
                        list.add(tLdeserialize);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
                ((SQLiteCursor)queryFinalized).dispose();
            }
        }
    }
    
    public int getLastDateValue() {
        this.ensureOpened();
        return this.lastDateValue;
    }
    
    public int getLastPtsValue() {
        this.ensureOpened();
        return this.lastPtsValue;
    }
    
    public int getLastQtsValue() {
        this.ensureOpened();
        return this.lastQtsValue;
    }
    
    public int getLastSecretVersion() {
        this.ensureOpened();
        return this.lastSecretVersion;
    }
    
    public int getLastSeqValue() {
        this.ensureOpened();
        return this.lastSeqValue;
    }
    
    public void getMessages(final long n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final boolean b, final int n8) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$07SM_wPgrIcj52iujRCXFFg5cuY(this, n2, n3, b, n, n7, n5, n4, n6, n8));
    }
    
    public void getMessagesCount(final long n, final IntCallback intCallback) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$4QgjEUgSmRBJDMucSQiTE5AXnrw(this, n, intCallback));
    }
    
    public void getNewTask(final ArrayList<Integer> list, final int n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$SkcFNBx8pL2j8q0UCSVux88XNjo(this, list));
    }
    
    public int getSecretG() {
        this.ensureOpened();
        return this.secretG;
    }
    
    public byte[] getSecretPBytes() {
        this.ensureOpened();
        return this.secretPBytes;
    }
    
    public Object[] getSentFile(final String s, final int n) {
        Object[] array2;
        final Object[] array = array2 = null;
        if (s != null) {
            if (s.toLowerCase().endsWith("attheme")) {
                array2 = array;
            }
            else {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                final Object[] array3 = new Object[2];
                this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$7jw738vBdzQ8x4ji7riWY5uMZu8(this, s, n, array3, countDownLatch));
                try {
                    countDownLatch.await();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                array2 = array;
                if (array3[0] != null) {
                    array2 = array3;
                }
            }
        }
        return array2;
    }
    
    public DispatchQueue getStorageQueue() {
        return this.storageQueue;
    }
    
    public void getUnreadMention(final long n, final IntCallback intCallback) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$l8MwXwE6wtta48AYjWKC3wBXwIo(this, n, intCallback));
    }
    
    public void getUnsentMessages(final int n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$51EbzaU3YUe50I1L4RRCcEF_Asw(this, n));
    }
    
    public TLRPC.User getUser(final int i) {
        final TLRPC.User user = null;
        TLRPC.User user2;
        try {
            final ArrayList list = new ArrayList<TLRPC.User>();
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(i);
            this.getUsersInternal(sb.toString(), list);
            user2 = user;
            if (!list.isEmpty()) {
                user2 = list.get(0);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
            user2 = user;
        }
        return user2;
    }
    
    public TLRPC.User getUserSync(final int n) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final TLRPC.User[] array = { null };
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$OGtMr0HOJdFgtGCQ7p3NBahgzn8(this, array, n, countDownLatch));
        try {
            countDownLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return array[0];
    }
    
    public ArrayList<TLRPC.User> getUsers(final ArrayList<Integer> list) {
        final ArrayList<TLRPC.User> list2 = new ArrayList<TLRPC.User>();
        try {
            this.getUsersInternal(TextUtils.join((CharSequence)",", (Iterable)list), list2);
        }
        catch (Exception ex) {
            list2.clear();
            FileLog.e(ex);
        }
        return list2;
    }
    
    public void getUsersInternal(String queryFinalized, final ArrayList<TLRPC.User> list) throws Exception {
        if (queryFinalized != null && queryFinalized.length() != 0) {
            if (list != null) {
                queryFinalized = (String)this.database.queryFinalized(String.format(Locale.US, "SELECT data, status FROM users WHERE uid IN(%s)", queryFinalized), new Object[0]);
                while (((SQLiteCursor)queryFinalized).next()) {
                    try {
                        final NativeByteBuffer byteBufferValue = ((SQLiteCursor)queryFinalized).byteBufferValue(0);
                        if (byteBufferValue == null) {
                            continue;
                        }
                        final TLRPC.User tLdeserialize = TLRPC.User.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                        byteBufferValue.reuse();
                        if (tLdeserialize == null) {
                            continue;
                        }
                        if (tLdeserialize.status != null) {
                            tLdeserialize.status.expires = ((SQLiteCursor)queryFinalized).intValue(1);
                        }
                        list.add(tLdeserialize);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
                ((SQLiteCursor)queryFinalized).dispose();
            }
        }
    }
    
    public void getWallpapers() {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$ANII5DCxsANA4ox20RDROdwMfjc(this));
    }
    
    public boolean hasAuthMessage(final int n) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final boolean[] array = { false };
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$DRnJSL_cQzMMXDYp016D_2ih2II(this, n, array, countDownLatch));
        try {
            countDownLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return array[0];
    }
    
    public boolean isDialogHasMessages(final long n) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final boolean[] array = { false };
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$oKBMd5Lq6aDT_odvjSMscCF162k(this, n, array, countDownLatch));
        try {
            countDownLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return array[0];
    }
    
    public boolean isMigratedChat(final int n) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final boolean[] array = { false };
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$vt3IkrkFUG3bfsSimMlmGH84kQo(this, n, array, countDownLatch));
        try {
            countDownLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return array[0];
    }
    
    public void loadChannelAdmins(final int n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Vnf_RLnltCEPaLdbkpbktxt0AFA(this, n));
    }
    
    public void loadChatInfo(final int n, final CountDownLatch countDownLatch, final boolean b, final boolean b2) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$VOppcN16yRL668WP3aQYIlU1CXM(this, n, countDownLatch, b, b2));
    }
    
    public void loadUnreadMessages() {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$joRU6ZuCNEib3vg1K0JvduLpCMk(this));
    }
    
    public void loadUserInfo(final TLRPC.User user, final boolean b, final int n) {
        if (user == null) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$YgCXdrvaSdGA1hf5LiH9nJofnsg(this, user, b, n));
    }
    
    public void loadWebRecent(final int n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$IRnMGvkA4nj7Im0gunHCNPyjjd0(this, n));
    }
    
    public void markMentionMessageAsRead(final int n, final int n2, final long n3) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$s2Q7bjKkmqsnurRGdapKRuUwrkY(this, n, n2, n3));
    }
    
    public void markMessageAsMention(final long n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$G2AR1YES2RX33l0g8yjj7MWOmwU(this, n));
    }
    
    public void markMessageAsSendError(final TLRPC.Message message) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$CDdpxNeQptYiAHzuKy4PuE_KUXI(this, message));
    }
    
    public ArrayList<Long> markMessagesAsDeleted(final int n, final int n2, final boolean b) {
        if (b) {
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$G8wlamTxGBm2te_IIW2_2dxQkB8(this, n, n2));
            return null;
        }
        return this.markMessagesAsDeletedInternal(n, n2);
    }
    
    public ArrayList<Long> markMessagesAsDeleted(final ArrayList<Integer> list, final boolean b, final int n) {
        if (list.isEmpty()) {
            return null;
        }
        if (b) {
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$CwczV_sujyFOSEA_5BEgE4G3Gcw(this, list, n));
            return null;
        }
        return this.markMessagesAsDeletedInternal(list, n);
    }
    
    public void markMessagesAsDeletedByRandoms(final ArrayList<Long> list) {
        if (list.isEmpty()) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$5tLDgdMiD0B9gd7akTqO_XaBigI(this, list));
    }
    
    public void markMessagesAsRead(final SparseLongArray sparseLongArray, final SparseLongArray sparseLongArray2, final SparseIntArray sparseIntArray, final boolean b) {
        if (b) {
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$7M1fsQyOLqfQ09O_qH6xF5Wf9zk(this, sparseLongArray, sparseLongArray2, sparseIntArray));
        }
        else {
            this.markMessagesAsReadInternal(sparseLongArray, sparseLongArray2, sparseIntArray);
        }
    }
    
    public void markMessagesContentAsRead(final ArrayList<Long> list, final int n) {
        if (isEmpty(list)) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Kd_7KIl84Ykan0Xx9_cq5K_0a4k(this, list, n));
    }
    
    public void onDeleteQueryComplete(final long n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$9qnT2TxRdf9DxlCuMWJHZarRSno(this, n));
    }
    
    public void openDatabase(final int n) {
        File filesDirFixed;
        final File parent = filesDirFixed = ApplicationLoader.getFilesDirFixed();
        if (this.currentAccount != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("account");
            sb.append(this.currentAccount);
            sb.append("/");
            filesDirFixed = new File(parent, sb.toString());
            filesDirFixed.mkdirs();
        }
        this.cacheFile = new File(filesDirFixed, "cache4.db");
        this.walCacheFile = new File(filesDirFixed, "cache4.db-wal");
        this.shmCacheFile = new File(filesDirFixed, "cache4.db-shm");
        final boolean exists = this.cacheFile.exists();
        final int n2 = 3;
        try {
            this.database = new SQLiteDatabase(this.cacheFile.getPath());
            this.database.executeFast("PRAGMA secure_delete = ON").stepThis().dispose();
            this.database.executeFast("PRAGMA temp_store = 1").stepThis().dispose();
            this.database.executeFast("PRAGMA journal_mode = WAL").stepThis().dispose();
            if (exists ^ true) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("create new database");
                }
                this.database.executeFast("CREATE TABLE messages_holes(uid INTEGER, start INTEGER, end INTEGER, PRIMARY KEY(uid, start));").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_end_messages_holes ON messages_holes(uid, end);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE media_holes_v2(uid INTEGER, type INTEGER, start INTEGER, end INTEGER, PRIMARY KEY(uid, type, start));").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_end_media_holes_v2 ON media_holes_v2(uid, type, end);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE messages(mid INTEGER PRIMARY KEY, uid INTEGER, read_state INTEGER, send_state INTEGER, date INTEGER, data BLOB, out INTEGER, ttl INTEGER, media INTEGER, replydata BLOB, imp INTEGER, mention INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_mid_idx_messages ON messages(uid, mid);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_date_mid_idx_messages ON messages(uid, date, mid);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS mid_out_idx_messages ON messages(mid, out);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS task_idx_messages ON messages(uid, out, read_state, ttl, date, send_state);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS send_state_idx_messages ON messages(mid, send_state, date) WHERE mid < 0 AND send_state = 1;").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_mention_idx_messages ON messages(uid, mention, read_state);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE download_queue(uid INTEGER, type INTEGER, date INTEGER, data BLOB, parent TEXT, PRIMARY KEY (uid, type));").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS type_date_idx_download_queue ON download_queue(type, date);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE user_contacts_v7(key TEXT PRIMARY KEY, uid INTEGER, fname TEXT, sname TEXT, imported INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE user_phones_v7(key TEXT, phone TEXT, sphone TEXT, deleted INTEGER, PRIMARY KEY (key, phone))").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS sphone_deleted_idx_user_phones ON user_phones_v7(sphone, deleted);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE dialogs(did INTEGER PRIMARY KEY, date INTEGER, unread_count INTEGER, last_mid INTEGER, inbox_max INTEGER, outbox_max INTEGER, last_mid_i INTEGER, unread_count_i INTEGER, pts INTEGER, date_i INTEGER, pinned INTEGER, flags INTEGER, folder_id INTEGER, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS date_idx_dialogs ON dialogs(date);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS last_mid_idx_dialogs ON dialogs(last_mid);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS unread_count_idx_dialogs ON dialogs(unread_count);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS last_mid_i_idx_dialogs ON dialogs(last_mid_i);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS unread_count_i_idx_dialogs ON dialogs(unread_count_i);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS folder_id_idx_dialogs ON dialogs(folder_id);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE randoms(random_id INTEGER, mid INTEGER, PRIMARY KEY (random_id, mid))").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS mid_idx_randoms ON randoms(mid);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE enc_tasks_v2(mid INTEGER PRIMARY KEY, date INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS date_idx_enc_tasks_v2 ON enc_tasks_v2(date);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE messages_seq(mid INTEGER PRIMARY KEY, seq_in INTEGER, seq_out INTEGER);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS seq_idx_messages_seq ON messages_seq(seq_in, seq_out);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE params(id INTEGER PRIMARY KEY, seq INTEGER, pts INTEGER, date INTEGER, qts INTEGER, lsv INTEGER, sg INTEGER, pbytes BLOB)").stepThis().dispose();
                this.database.executeFast("INSERT INTO params VALUES(1, 0, 0, 0, 0, 0, 0, NULL)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE media_v2(mid INTEGER PRIMARY KEY, uid INTEGER, date INTEGER, type INTEGER, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_mid_type_date_idx_media ON media_v2(uid, mid, type, date);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE bot_keyboard(uid INTEGER PRIMARY KEY, mid INTEGER, info BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS bot_keyboard_idx_mid ON bot_keyboard(mid);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE chat_settings_v2(uid INTEGER PRIMARY KEY, info BLOB, pinned INTEGER, online INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS chat_settings_pinned_idx ON chat_settings_v2(uid, pinned) WHERE pinned != 0;").stepThis().dispose();
                this.database.executeFast("CREATE TABLE user_settings(uid INTEGER PRIMARY KEY, info BLOB, pinned INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS user_settings_pinned_idx ON user_settings(uid, pinned) WHERE pinned != 0;").stepThis().dispose();
                this.database.executeFast("CREATE TABLE chat_pinned(uid INTEGER PRIMARY KEY, pinned INTEGER, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS chat_pinned_mid_idx ON chat_pinned(uid, pinned) WHERE pinned != 0;").stepThis().dispose();
                this.database.executeFast("CREATE TABLE chat_hints(did INTEGER, type INTEGER, rating REAL, date INTEGER, PRIMARY KEY(did, type))").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS chat_hints_rating_idx ON chat_hints(rating);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE botcache(id TEXT PRIMARY KEY, date INTEGER, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS botcache_date_idx ON botcache(date);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE users_data(uid INTEGER PRIMARY KEY, about TEXT)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE users(uid INTEGER PRIMARY KEY, name TEXT, status INTEGER, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE chats(uid INTEGER PRIMARY KEY, name TEXT, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE enc_chats(uid INTEGER PRIMARY KEY, user INTEGER, name TEXT, data BLOB, g BLOB, authkey BLOB, ttl INTEGER, layer INTEGER, seq_in INTEGER, seq_out INTEGER, use_count INTEGER, exchange_id INTEGER, key_date INTEGER, fprint INTEGER, fauthkey BLOB, khash BLOB, in_seq_no INTEGER, admin_id INTEGER, mtproto_seq INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE channel_users_v2(did INTEGER, uid INTEGER, date INTEGER, data BLOB, PRIMARY KEY(did, uid))").stepThis().dispose();
                this.database.executeFast("CREATE TABLE channel_admins(did INTEGER, uid INTEGER, PRIMARY KEY(did, uid))").stepThis().dispose();
                this.database.executeFast("CREATE TABLE contacts(uid INTEGER PRIMARY KEY, mutual INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE user_photos(uid INTEGER, id INTEGER, data BLOB, PRIMARY KEY (uid, id))").stepThis().dispose();
                this.database.executeFast("CREATE TABLE blocked_users(uid INTEGER PRIMARY KEY)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE dialog_settings(did INTEGER PRIMARY KEY, flags INTEGER);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE web_recent_v3(id TEXT, type INTEGER, image_url TEXT, thumb_url TEXT, local_url TEXT, width INTEGER, height INTEGER, size INTEGER, date INTEGER, document BLOB, PRIMARY KEY (id, type));").stepThis().dispose();
                this.database.executeFast("CREATE TABLE stickers_v2(id INTEGER PRIMARY KEY, data BLOB, date INTEGER, hash TEXT);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE stickers_featured(id INTEGER PRIMARY KEY, data BLOB, unread BLOB, date INTEGER, hash TEXT);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE hashtag_recent_v2(id TEXT PRIMARY KEY, date INTEGER);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE webpage_pending(id INTEGER, mid INTEGER, PRIMARY KEY (id, mid));").stepThis().dispose();
                this.database.executeFast("CREATE TABLE sent_files_v2(uid TEXT, type INTEGER, data BLOB, parent TEXT, PRIMARY KEY (uid, type))").stepThis().dispose();
                this.database.executeFast("CREATE TABLE search_recent(did INTEGER PRIMARY KEY, date INTEGER);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE media_counts_v2(uid INTEGER, type INTEGER, count INTEGER, old INTEGER, PRIMARY KEY(uid, type))").stepThis().dispose();
                this.database.executeFast("CREATE TABLE keyvalue(id TEXT PRIMARY KEY, value TEXT)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE bot_info(uid INTEGER PRIMARY KEY, info BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE pending_tasks(id INTEGER PRIMARY KEY, data BLOB);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE requested_holes(uid INTEGER, seq_out_start INTEGER, seq_out_end INTEGER, PRIMARY KEY (uid, seq_out_start, seq_out_end));").stepThis().dispose();
                this.database.executeFast("CREATE TABLE sharing_locations(uid INTEGER PRIMARY KEY, mid INTEGER, date INTEGER, period INTEGER, message BLOB);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE emoji_keywords_v2(lang TEXT, keyword TEXT, emoji TEXT, PRIMARY KEY(lang, keyword, emoji));").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS emoji_keywords_v2_keyword ON emoji_keywords_v2(keyword);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE emoji_keywords_info_v2(lang TEXT PRIMARY KEY, alias TEXT, version INTEGER, date INTEGER);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE wallpapers2(uid INTEGER PRIMARY KEY, data BLOB, num INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS wallpapers_num ON wallpapers2(num);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE unread_push_messages(uid INTEGER, mid INTEGER, random INTEGER, date INTEGER, data BLOB, fm TEXT, name TEXT, uname TEXT, flags INTEGER, PRIMARY KEY(uid, mid))").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS unread_push_messages_idx_date ON unread_push_messages(date);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS unread_push_messages_idx_random ON unread_push_messages(random);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE polls(mid INTEGER PRIMARY KEY, id INTEGER);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS polls_id ON polls(id);").stepThis().dispose();
                this.database.executeFast("PRAGMA user_version = 60").stepThis().dispose();
            }
            else {
                final int intValue = this.database.executeInt("PRAGMA user_version", new Object[0]);
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("current db version = ");
                    sb2.append(intValue);
                    FileLog.d(sb2.toString());
                }
                if (intValue == 0) {
                    throw new Exception("malformed");
                }
                try {
                    final SQLiteCursor queryFinalized = this.database.queryFinalized("SELECT seq, pts, date, qts, lsv, sg, pbytes FROM params WHERE id = 1", new Object[0]);
                    if (queryFinalized.next()) {
                        this.lastSeqValue = queryFinalized.intValue(0);
                        this.lastPtsValue = queryFinalized.intValue(1);
                        this.lastDateValue = queryFinalized.intValue(2);
                        this.lastQtsValue = queryFinalized.intValue(3);
                        this.lastSecretVersion = queryFinalized.intValue(4);
                        this.secretG = queryFinalized.intValue(5);
                        if (queryFinalized.isNull(6)) {
                            this.secretPBytes = null;
                        }
                        else {
                            this.secretPBytes = queryFinalized.byteArrayValue(6);
                            if (this.secretPBytes != null && this.secretPBytes.length == 1) {
                                this.secretPBytes = null;
                            }
                        }
                    }
                    queryFinalized.dispose();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                    try {
                        this.database.executeFast("CREATE TABLE IF NOT EXISTS params(id INTEGER PRIMARY KEY, seq INTEGER, pts INTEGER, date INTEGER, qts INTEGER, lsv INTEGER, sg INTEGER, pbytes BLOB)").stepThis().dispose();
                        this.database.executeFast("INSERT INTO params VALUES(1, 0, 0, 0, 0, 0, 0, NULL)").stepThis().dispose();
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                    }
                }
                if (intValue < 60) {
                    this.updateDbToLastVersion(intValue);
                }
            }
        }
        catch (Exception ex3) {
            FileLog.e(ex3);
            if (n < 3 && ex3.getMessage().contains("malformed")) {
                if (n == 2) {
                    this.cleanupInternal(true);
                    for (int i = 0; i < 2; ++i) {
                        UserConfig.getInstance(this.currentAccount).setDialogsLoadOffset(i, 0, 0, 0, 0, 0, 0L);
                        UserConfig.getInstance(this.currentAccount).setTotalDialogsCount(i, 0);
                    }
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                }
                else {
                    this.cleanupInternal(false);
                }
                int n3 = n2;
                if (n == 1) {
                    n3 = 2;
                }
                this.openDatabase(n3);
            }
        }
        this.loadUnreadMessages();
        this.loadPendingTasks();
        try {
            this.openSync.countDown();
        }
        catch (Throwable t) {}
    }
    
    public void overwriteChannel(final int n, final TLRPC.TL_updates_channelDifferenceTooLong tl_updates_channelDifferenceTooLong, final int n2) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$7ABQwwpjiNBfDEzyTxSwMP9s_yY(this, n, n2, tl_updates_channelDifferenceTooLong));
    }
    
    public void processPendingRead(final long n, final long n2, final long n3, final int n4, final boolean b) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$BFuJtbbUa5cf4B4J2ETafzpLwTU(this, n, n2, b, n3));
    }
    
    public void putBlockedUsers(final SparseIntArray sparseIntArray, final boolean b) {
        if (sparseIntArray != null) {
            if (sparseIntArray.size() != 0) {
                this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$a_e0LwnKknlA9E0BfJ3sY5J_dD0(this, b, sparseIntArray));
            }
        }
    }
    
    public void putCachedPhoneBook(final HashMap<String, ContactsController.Contact> hashMap, final boolean b, final boolean b2) {
        if (hashMap != null) {
            if (!hashMap.isEmpty() || b || b2) {
                this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$d0CKfJCWUy1j7KfKMh_A2PiSqHA(this, hashMap, b));
            }
        }
    }
    
    public void putChannelAdmins(final int n, final ArrayList<Integer> list) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$mAd0DJIW27tlUDIOzW0wIDeqyuE(this, n, list));
    }
    
    public void putChannelViews(final SparseArray<SparseIntArray> sparseArray, final boolean b) {
        if (isEmpty(sparseArray)) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$5OIyS9FCXd0SoEseA3Ut7Uei2nc(this, sparseArray, b));
    }
    
    public void putContacts(final ArrayList<TLRPC.TL_contact> c, final boolean b) {
        if (c.isEmpty() && !b) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$VIjx98VFT9EYmnJ8mUO_SiJ4810(this, b, new ArrayList((Collection<? extends E>)c)));
    }
    
    public void putDialogPhotos(final int n, final TLRPC.photos_Photos photos_Photos) {
        if (photos_Photos != null) {
            if (!photos_Photos.photos.isEmpty()) {
                this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$L4Om6tbm2x7Gby_R4CSBHE04Yg4(this, n, photos_Photos));
            }
        }
    }
    
    public void putDialogs(final TLRPC.messages_Dialogs messages_Dialogs, final int n) {
        if (messages_Dialogs.dialogs.isEmpty()) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$hry9IvsaQDnemeIROZgTEzWio5I(this, messages_Dialogs, n));
    }
    
    public void putEncryptedChat(final TLRPC.EncryptedChat encryptedChat, final TLRPC.User user, final TLRPC.Dialog dialog) {
        if (encryptedChat == null) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$wWMmlnBYX7ztuC_F9UPquAEbR_s(this, encryptedChat, user, dialog));
    }
    
    public void putMessages(final ArrayList<TLRPC.Message> list, final boolean b, final boolean b2, final boolean b3, final int n) {
        this.putMessages(list, b, b2, b3, n, false);
    }
    
    public void putMessages(final ArrayList<TLRPC.Message> list, final boolean b, final boolean b2, final boolean b3, final int n, final boolean b4) {
        if (list.size() == 0) {
            return;
        }
        if (b2) {
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Zbl8iyH6cJ1Eo71mteogwoPbxxQ(this, list, b, b3, n, b4));
        }
        else {
            this.putMessagesInternal(list, b, b3, n, b4);
        }
    }
    
    public void putMessages(final TLRPC.messages_Messages messages_Messages, final long n, final int n2, final int n3, final boolean b) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$6OdG59RQk4gtHeSIVwqv0igXK90(this, messages_Messages, n2, n, n3, b));
    }
    
    public void putPushMessage(final MessageObject messageObject) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$lVMMHudh0dN8CKFv0xYu7ddEmi8(this, messageObject));
    }
    
    public void putSentFile(final String s, final TLObject tlObject, final int n, final String s2) {
        if (s != null && tlObject != null) {
            if (s2 != null) {
                this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$0ihTGohq3QJed2BJHkvrASib2fM(this, s, tlObject, n, s2));
            }
        }
    }
    
    public void putUsersAndChats(final ArrayList<TLRPC.User> list, final ArrayList<TLRPC.Chat> list2, final boolean b, final boolean b2) {
        if (list != null && list.isEmpty() && list2 != null && list2.isEmpty()) {
            return;
        }
        if (b2) {
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$MmeBiywJvVY_laiR4XDpwGlLSH4(this, list, list2, b));
        }
        else {
            this.putUsersAndChatsInternal(list, list2, b);
        }
    }
    
    public void putWallpapers(final ArrayList<TLRPC.WallPaper> list, final int n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$9BkX0ldup_qkhjHCNOhyXfgGwXY(this, n, list));
    }
    
    public void putWebPages(final LongSparseArray<TLRPC.WebPage> longSparseArray) {
        if (isEmpty(longSparseArray)) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$fzFvY5cznRt5WEudcaJ9_2Km0iI(this, longSparseArray));
    }
    
    public void putWebRecent(final ArrayList<MediaController.SearchImage> list) {
        if (!list.isEmpty()) {
            if (list.isEmpty()) {
                this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$2zgETsycS9dz5I652QRWAYabQH4(this, list));
            }
        }
    }
    
    public void readAllDialogs() {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Uv7xGGpYr4xQqc_hYra_gjZD6hY(this));
    }
    
    public void removeFromDownloadQueue(final long n, final int n2, final boolean b) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$uX9RNDdADQA1Zb0x7WR8P7gNoZY(this, b, n2, n));
    }
    
    public void removePendingTask(final long n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$bLsQ9Zez_Ef0tQJtoMR54iM_bHA(this, n));
    }
    
    public void replaceMessageIfExists(final TLRPC.Message message, final int n, final ArrayList<TLRPC.User> list, final ArrayList<TLRPC.Chat> list2, final boolean b) {
        if (message == null) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$pND7SeCnHWGSkCmeT8Uq6NOWqtE(this, message, b, list, list2, n));
    }
    
    public void resetDialogs(final TLRPC.messages_Dialogs messages_Dialogs, final int n, final int n2, final int n3, final int n4, final int n5, final LongSparseArray<TLRPC.Dialog> longSparseArray, final LongSparseArray<MessageObject> longSparseArray2, final TLRPC.Message message, final int n6) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Rsj_LL8Uz4Bh77DLk8Q7Jiw37h4(this, messages_Dialogs, n6, n2, n3, n4, n5, message, n, longSparseArray, longSparseArray2));
    }
    
    public void resetMentionsCount(final long n, final int n2) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$QZA58f_xBct34Cb7F3baqareFec(this, n2, n));
    }
    
    public void saveBotCache(final String s, final TLObject tlObject) {
        if (tlObject != null) {
            if (!TextUtils.isEmpty((CharSequence)s)) {
                this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$7VZ_XJgDXdzbLiASsmURuo_C9GM(this, tlObject, s));
            }
        }
    }
    
    public void saveChannelPts(final int n, final int n2) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$bE1TJhfMynV6_iw8er6RBG5GCDM(this, n2, n));
    }
    
    public void saveDiffParams(final int n, final int n2, final int n3, final int n4) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$_bpF9_lI_H0oz_1CAiE5SbDJe_c(this, n, n2, n3, n4));
    }
    
    public void saveSecretParams(final int n, final int n2, final byte[] array) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$NtpBYQeXRJpUfal8CNuiE9yNR3Y(this, n, n2, array));
    }
    
    public void setDialogFlags(final long n, final long n2) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$uZgJttXUWPBitEWEPJJQPgaQByA(this, n, n2));
    }
    
    public void setDialogPinned(final long n, final int n2) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$pydpGfyEJY86wZVM_5gk1G5enLU(this, n2, n));
    }
    
    public void setDialogUnread(final long n, final boolean b) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$YR6__nj0nsdVFY6J_MW9CHpRtYs(this, n, b));
    }
    
    public void setDialogsFolderId(final ArrayList<TLRPC.TL_folderPeer> list, final ArrayList<TLRPC.TL_inputFolderPeer> list2, final long n, final int n2) {
        if (list == null && list2 == null && n == 0L) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$vU3H4O_uMIIPLYpg8QUqZEOJanU(this, list, list2, n2, n));
    }
    
    public void setLastDateValue(final int lastDateValue) {
        this.ensureOpened();
        this.lastDateValue = lastDateValue;
    }
    
    public void setLastPtsValue(final int lastPtsValue) {
        this.ensureOpened();
        this.lastPtsValue = lastPtsValue;
    }
    
    public void setLastQtsValue(final int lastQtsValue) {
        this.ensureOpened();
        this.lastQtsValue = lastQtsValue;
    }
    
    public void setLastSecretVersion(final int lastSecretVersion) {
        this.ensureOpened();
        this.lastSecretVersion = lastSecretVersion;
    }
    
    public void setLastSeqValue(final int lastSeqValue) {
        this.ensureOpened();
        this.lastSeqValue = lastSeqValue;
    }
    
    public void setMessageSeq(final int n, final int n2, final int n3) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$I_Nkh3lDe_kxZ__U4jy8HVjamUk(this, n, n2, n3));
    }
    
    public void setSecretG(final int secretG) {
        this.ensureOpened();
        this.secretG = secretG;
    }
    
    public void setSecretPBytes(final byte[] secretPBytes) {
        this.ensureOpened();
        this.secretPBytes = secretPBytes;
    }
    
    public void unpinAllDialogsExceptNew(final ArrayList<Long> list, final int n) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$UNG3O_q0_n6IbfxTyxeEDWN4wjc(this, list, n));
    }
    
    public void updateChannelUsers(final int n, final ArrayList<TLRPC.ChannelParticipant> list) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$dK1QMDFhAiUhe4t4z2CkJFVPwJo(this, n, list));
    }
    
    public void updateChatDefaultBannedRights(final int n, final TLRPC.TL_chatBannedRights tl_chatBannedRights, final int n2) {
        if (tl_chatBannedRights != null) {
            if (n != 0) {
                this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$9hiJJNQirqjcrHEWZID3qqycfH0(this, n, n2, tl_chatBannedRights));
            }
        }
    }
    
    public void updateChatInfo(final int n, final int n2, final int n3, final int n4, final int n5) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$KNbNa8I6iljUtQSIcsWsYI2Eqos(this, n, n3, n2, n4, n5));
    }
    
    public void updateChatInfo(final TLRPC.ChatFull chatFull, final boolean b) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$d0gfmXVoBhLfPpsJm2PNGRtagwc(this, chatFull, b));
    }
    
    public void updateChatOnlineCount(final int n, final int n2) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$JoSxg9ZlDc30vVq_9Ro7__Ct4Kk(this, n2, n));
    }
    
    public void updateChatParticipants(final TLRPC.ChatParticipants chatParticipants) {
        if (chatParticipants == null) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$ul28E39n2CqjyGu1NrRVaRUiBf8(this, chatParticipants));
    }
    
    public void updateChatPinnedMessage(final int n, final int n2) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$ZKAInF8vAaa7ZvlZcI_h3lCnx34(this, n, n2));
    }
    
    public void updateDialogsWithDeletedMessages(final ArrayList<Integer> list, final ArrayList<Long> list2, final boolean b, final int n) {
        if (list.isEmpty() && n == 0) {
            return;
        }
        if (b) {
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$MA7HhVEAT2vamXfRirkyxddYYBE(this, list, list2, n));
        }
        else {
            this.updateDialogsWithDeletedMessagesInternal(list, list2, n);
        }
    }
    
    public void updateDialogsWithReadMessages(final SparseLongArray sparseLongArray, final SparseLongArray sparseLongArray2, final ArrayList<Long> list, final boolean b) {
        if (isEmpty(sparseLongArray) && isEmpty(list)) {
            return;
        }
        if (b) {
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$IgPbgy6_ebqi0So_m2IdijRNQZs(this, sparseLongArray, sparseLongArray2, list));
        }
        else {
            this.updateDialogsWithReadMessagesInternal(null, sparseLongArray, sparseLongArray2, list);
        }
    }
    
    public void updateEncryptedChat(final TLRPC.EncryptedChat encryptedChat) {
        if (encryptedChat == null) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Xb58sIGp8srP4NjwJOAz4ujR9zM(this, encryptedChat));
    }
    
    public void updateEncryptedChatLayer(final TLRPC.EncryptedChat encryptedChat) {
        if (encryptedChat == null) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$8Zp97g2M9CG9f_Df_BLJuu2zQDo(this, encryptedChat));
    }
    
    public void updateEncryptedChatSeq(final TLRPC.EncryptedChat encryptedChat, final boolean b) {
        if (encryptedChat == null) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$9zeOYi8rbx6eRgRL0h0GGgHo_pM(this, encryptedChat, b));
    }
    
    public void updateEncryptedChatTTL(final TLRPC.EncryptedChat encryptedChat) {
        if (encryptedChat == null) {
            return;
        }
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$va5klSdwmh7kuOLc57NT0kZlbDY(this, encryptedChat));
    }
    
    public void updateMessagePollResults(final long n, final TLRPC.TL_poll tl_poll, final TLRPC.TL_pollResults tl_pollResults) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$zk6IBPLI4pOBatA30WuVm_5FFz4(this, n, tl_poll, tl_pollResults));
    }
    
    public long[] updateMessageStateAndId(final long n, final Integer n2, final int n3, final int n4, final boolean b, final int n5) {
        if (b) {
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$cHj3_heIm7v3yVK5WTcTNH3ovCc(this, n, n2, n3, n4, n5));
            return null;
        }
        return this.updateMessageStateAndIdInternal(n, n2, n3, n4, n5);
    }
    
    public void updateUserInfo(final TLRPC.UserFull userFull, final boolean b) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$VXYkBnXA4FUiRp4wxPHJnwfBAPc(this, b, userFull));
    }
    
    public void updateUserPinnedMessage(final int n, final int n2) {
        this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$g1iuRG0K_i5oYkMy9HCpNOf8FqU(this, n, n2));
    }
    
    public void updateUsers(final ArrayList<TLRPC.User> list, final boolean b, final boolean b2, final boolean b3) {
        if (list != null) {
            if (!list.isEmpty()) {
                if (b3) {
                    this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$KhcIchuE4KJbeKdTaRAB8CWDcM0(this, list, b, b2));
                }
                else {
                    this.updateUsersInternal(list, b, b2);
                }
            }
        }
    }
    
    public interface BooleanCallback
    {
        void run(final boolean p0);
    }
    
    private class Hole
    {
        public int end;
        public int start;
        public int type;
        
        public Hole(final int start, final int end) {
            this.start = start;
            this.end = end;
        }
        
        public Hole(final int type, final int start, final int end) {
            this.type = type;
            this.start = start;
            this.end = end;
        }
    }
    
    public interface IntCallback
    {
        void run(final int p0);
    }
    
    private class ReadDialog
    {
        public int date;
        public int lastMid;
        public int unreadCount;
    }
}
